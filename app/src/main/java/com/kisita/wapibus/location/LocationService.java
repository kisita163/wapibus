package com.kisita.wapibus.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class LocationService extends Service {
    public LocationService() {
    }
    private final String TAG = getClass().getName();
    private LocationManager locationMgr = null;
    private LocationListener onLocationChange = new LocationListener()
    {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        }

        @Override
        public void onProviderDisabled(String provider)
        {
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            Log.i(TAG,"lat = "+latitude + " and long = "+longitude);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/positions/" + getUid() + "/latitude",latitude);
            childUpdates.put("/positions/" + getUid() + "/longitude",longitude);

            getDb().updateChildren(childUpdates);
        }
    };

    public DatabaseReference getDb() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        Log.i(TAG,"Location service created");
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,
        //        0, onLocationChange);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
                onLocationChange);


        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
        locationMgr.removeUpdates(onLocationChange);
    }
}

