package com.foogle.adnansakel.bdl_food_app.Helper;

/**
 * Created by Adnan Sakel on 5/20/2016.
 */
import android.content.Context;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class ConnectionCheck {

    private Context _context;

    public ConnectionCheck(Context context){
        this._context = context;
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected == false) {
            Toast.makeText(_context, "No Internet Connection Found. Please connect to the internet and try again.", Toast.LENGTH_LONG).show();
            //System.out.println("Toastcheck");
        }

        //System.out.println("isConnected: " + isConnected);
        return isConnected;
    }

    public boolean isConnectedWithoutToastMessage(){
        ConnectivityManager cm = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }
}

