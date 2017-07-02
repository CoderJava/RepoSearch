# RepoSearch
This is an example Android Networking library and check the network connection

## Content
The purpose of this repo:
  1. Check wheter internet connection is available or not.
  2. To share and teach how to fetch data from internet using
asyntask, volley, okhttp and retrofit.

## Check network connection
If you want to check network connection, make sure you add the 2 permissions to **AndroidManifest.xml**.<br>
`<uses-permission android:name="android.permission.INTERNET" />`
`<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`<br>
In MainActivity create method `isNetworkConnected()` to check the phone is connected to Internet.
```java
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
```
**NOTE**: If you want to check WI-FI is available because you don't want user use mobile data a.k.a KUOTA,
you can create method `isWifiConnected()`.
```java
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
```
Call those method in `onCreate()` method in MainActivity.
**More detail in :** [AndroidManifest.xml](https://github.com/satyakresna/RepoSearch/blob/Retrofit/app/src/main/AndroidManifest.xml) and [MainActivity](https://github.com/satyakresna/RepoSearch/blob/Retrofit/app/src/main/java/com/example/satyakresna/reposearch/MainActivity.java)

## Courtesy
This sample source from [Ray Wenderlich](https://raywenderlich.com/126770/android-networking-tutorial-getting-started) in 2016. But, because of today is 2017, there's something deprecated in that source. So, I have initiative to modified it.

## Usage
Just switch branch i.e. master > Volley or Volley > OkHttp etc.
