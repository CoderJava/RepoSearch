package com.example.satyakresna.reposearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    ListFragment mListFragment;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            if (isNetworkConnected()) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                makeRequestWithOkHttp("https://api.github.com/repositories");
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("No Internet Connection")
                        .setMessage("It looks like your internet connection is off. Please turn it " +
                                "on and try again")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        }
    }

    private void showListFragment(ArrayList<Repository> repositories) {
        mListFragment = ListFragment.newInstance(repositories);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mListFragment).
                commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    /**
     * isNetworkConnected() checks that the device has an active Internet Connection as follows:
     * 1. Retrieves an instance of the ConnectivityManager class from the current application text
     * 2. Retrieves an instance of the NetworkInfo class that represents the current network connection.
     *    This will be null if no network is available.
     * 3. Check if there is an available network connection and the device is connected.
     */
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    /**
     * When you have an app that retrieves huge amounts of data, you might want to restrict network
     * connections to particular network types, such as WI-FI. You can do this using getType() on
     * the NetworkInfo object
     *
     */
    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }

    private void startDownload() {
    }

    public void downloadComplete(ArrayList<Repository> repositories) {
        showListFragment(repositories);
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    private void makeRequestWithOkHttp(String url) {
        OkHttpClient client = new OkHttpClient();   // 1
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();  // 2

        client.newCall(request).enqueue(new okhttp3.Callback() { // 3
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response)
                    throws IOException {
                final String result = response.body().string();  // 4

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            downloadComplete(Util.retrieveRepositoriesFromResponse(result));  // 5
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
