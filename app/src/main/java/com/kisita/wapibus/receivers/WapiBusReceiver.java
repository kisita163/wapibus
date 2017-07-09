package com.kisita.wapibus.receivers;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.wapibus.location.LocationService;

/**
 * Created by HuguesKi on 8/07/2017.
 */

public class WapiBusReceiver extends BroadcastReceiver {

    private final static String TAG = "### WapiBusReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Boot completed. Starting location service");
        context.startService(new Intent(context, LocationService.class));
    }
}
