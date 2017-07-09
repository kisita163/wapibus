package com.kisita.wapibus.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kisita.wapibus.R;
import com.kisita.wapibus.TransportContent.TransportContent;
import com.kisita.wapibus.location.LocationService;

public class WapiBusMapsActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener,MapsFragment.OnMapsFragmentInteractionListener {

    private GoogleMap mMap;
    private static final int REQUEST_COARSE_LOCATION = 100;
    private FirebaseAuth mAuth;
    private static String TAG = "### Wapibus";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wapibus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.wapibus_toolbar);

        setFragment(1);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        authentication();
    }

    void setFragment(int fid) {
        String title = "";
        Fragment f = null;
        switch (fid) {
            case (0):
                f = ItemFragment.newInstance(1);
                break;
            case(1):
                f = MapsFragment.newInstance("","");
                break;
        }
        if(f != null) {
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.content_frame, f)
                    .commit();
        }
    }

    private void authentication() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("kisita2002@yahoo.fr","kisita")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Toast.makeText(WapiBusMapsActivity.this, "You have signed up successfully",
                                    Toast.LENGTH_SHORT).show(); // Then start localisation service
                            requestPermissionsForLocalisation();
                        } else {
                            Toast.makeText(WapiBusMapsActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestPermissionsForLocalisation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }else{
            if(!isMyServiceRunning(LocationService.class)){
                Log.i(TAG,"Location service is not running. Start it now");
                startService(new Intent(WapiBusMapsActivity.this,LocationService.class));
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(!isMyServiceRunning(LocationService.class)){
                        Log.i(TAG,"Location service is not running. Start it now");
                        startService(new Intent(WapiBusMapsActivity.this,LocationService.class));
                    }

                } else {
                    //TODO
                }
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        Log.i(TAG,"isMyServiceRunning ?");
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.i(TAG,"Service : "+service.service.getClassName());
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG,"Service found !!! ");
                return true;
            }
        }
        Log.i(TAG,"Research finished !!!");
        return false;
    }

    @Override
    public void onListFragmentInteraction(TransportContent.TransportItem item) {
        Log.i(TAG,"Line selected");
        setFragment(1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
