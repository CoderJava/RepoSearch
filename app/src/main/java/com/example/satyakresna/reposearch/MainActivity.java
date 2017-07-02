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
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                makeRetrofitCalls();
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

    /**
     * The code does following:
     * 1. Specifies base URL.
     * 2. Specifies GsonConverterFactory as the converter which uses Gson for its deserialization.
     * 3. Generates an implementation of the RetrofitAPI.
     * 4. Generates the request call.
     * 5. Queues the request call.
     * 6. Passes the results to the downloadComplete()
     */
    private void makeRetrofitCalls() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com") // 1
                .addConverterFactory(GsonConverterFactory.create()) // 2
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class); // 3
        retrofit2.Call<ArrayList<Repository>> call = retrofitAPI.retrieveRepositories(); // 4

        call.enqueue(new Callback<ArrayList<Repository>>() { // 5
            @Override
            public void onResponse(retrofit2.Call<ArrayList<Repository>> call, retrofit2.Response<ArrayList<Repository>> response) {
                downloadComplete(response.body()); // 6
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<Repository>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
