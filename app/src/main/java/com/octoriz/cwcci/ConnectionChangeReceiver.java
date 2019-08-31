package com.octoriz.cwcci;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionChangeReceiver extends BroadcastReceiver {

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            /**
             * If ConnectivityManager says connected,
             * then check really any active connection of not!
             */
            new InternetCheck().execute();
        } else {
            /**
             * ConnectivityManager says Not connected,
             * So Not connected, no further question.. :)
             */
            sendInternetInfo(false);
        }
    }

    private void sendInternetInfo(Boolean isConnected) {
        if (mContext.getClass() == LoginActivity.class) {
            ((LoginActivity) mContext).updateInternetConnectionStatusView(isConnected);
        }
        if (mContext.getClass() == AddActivity.class) {
            ((AddActivity) mContext).updateInternetConnectionStatusView(isConnected);

        }
    }

    /**
     * Help: https://stackoverflow.com/a/27312494/2263329
     * <p>
     * Using "8.8.8.8:53" always connect!
     * So hare used "www.google.com:80".
     */
    class InternetCheck extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Socket sock = new Socket();
                sock.connect(new InetSocketAddress("www.google.com", 80), 1500);
                sock.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {

            sendInternetInfo(isConnected);

        }
    }
}

