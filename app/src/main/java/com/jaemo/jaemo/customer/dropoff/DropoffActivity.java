package com.jaemo.jaemo.customer.dropoff;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jaemo.jaemo.R;
import com.jaemo.jaemo.customer.customer_info.CustomerInfoActivity;
import com.jaemo.jaemo.customer.drawer_fragments.pickup.PickupViewModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class DropoffActivity extends AppCompatActivity {
    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int Location_Permission_Request_Code = 1234;
    private static final float DEFAULT_ZOOM = 16;
    private boolean LocPermissionGranted = false;

    private MapController mapController;
    private MapView mMapView;
    private boolean M, TU, W, TH, F;
    private boolean SA, SU = false;

    private String TAG = "osmdroidMap";
    private EditText chooseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropoff);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.dropoff_title));
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        getLocationPermission();
        M = true;TU = true;W = true;TH = true;F = true;
    }

    //if permissions are already granted then initialize the map else request for permissions
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Fine_Location) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Coarse_Location) == PackageManager.PERMISSION_GRANTED) {
                LocPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, Location_Permission_Request_Code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, Location_Permission_Request_Code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocPermissionGranted = false;
        switch (requestCode) {
            case Location_Permission_Request_Code: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            LocPermissionGranted = false;
                            return;
                        }
                    }
                    LocPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    //initialize the map
    private void initMap() {
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        mMapView = (MapView) findViewById(R.id.mapview);
        if (mMapView != null) {
            mapController = (MapController) mMapView.getController();
            mapController.setZoom(DEFAULT_ZOOM);
            mMapView.setTileSource(TileSourceFactory.MAPNIK);
            mMapView.setTilesScaledToDpi(true);
            mMapView.setBuiltInZoomControls(false);
            mMapView.setMultiTouchControls(true);
        }
        moveCamera(new GeoPoint(33.642525, 72.98642));
    }

    //take a latLng object, move to the location and draw a marker on that location
    private void moveCamera(GeoPoint currentLocation) {
        mapController.animateTo(currentLocation);
        Marker my_location = new Marker(mMapView);
        my_location.setPosition(currentLocation);
        my_location.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(my_location);
    }

    public void dayToggle(View view) {
        Button b = (Button)view;
        String day = (String)b.getText();
        Log.w("myApp", day);

        if(day.equals("M")){
            if (M) {
                b.setBackgroundResource(R.drawable.day_button_unselected);
                b.setTextColor(getResources().getColor(R.color.theme1));
                M = false;
            } else {
                b.setBackgroundResource(R.drawable.day_button_selected);
                b.setTextColor(getResources().getColor(R.color.white));
                M = true;
            }
        }
        else if(day.equals("TU")){
            if (TU) {
                b.setBackgroundResource(R.drawable.day_button_unselected);
                b.setTextColor(getResources().getColor(R.color.theme1));
                TU = false;
            } else {
                b.setBackgroundResource(R.drawable.day_button_selected);
                b.setTextColor(getResources().getColor(R.color.white));
                TU = true;
            }
        }
        else if(day.equals("W")){
            if (W) {
                b.setBackgroundResource(R.drawable.day_button_unselected);
                b.setTextColor(getResources().getColor(R.color.theme1));
                W = false;
            } else {
                b.setBackgroundResource(R.drawable.day_button_selected);
                b.setTextColor(getResources().getColor(R.color.white));
                W = true;
            }
        }
        else if(day.equals("TH")){
            if (TH) {
                b.setBackgroundResource(R.drawable.day_button_unselected);
                b.setTextColor(getResources().getColor(R.color.theme1));
                TH = false;
            } else {
                b.setBackgroundResource(R.drawable.day_button_selected);
                b.setTextColor(getResources().getColor(R.color.white));
                TH = true;
            }
        }
        else if(day.equals("F")){
            if (F) {
                b.setBackgroundResource(R.drawable.day_button_unselected);
                b.setTextColor(getResources().getColor(R.color.theme1));
                F = false;
            } else {
                b.setBackgroundResource(R.drawable.day_button_selected);
                b.setTextColor(getResources().getColor(R.color.white));
                F = true;
            }
        }
        else if(day.equals("SA")){
            if (SA) {
                b.setBackgroundResource(R.drawable.day_button_unselected);
                b.setTextColor(getResources().getColor(R.color.theme1));
                SA = false;
            } else {
                b.setBackgroundResource(R.drawable.day_button_selected);
                b.setTextColor(getResources().getColor(R.color.white));
                SA = true;
            }
        }
        else if(day.equals("SU")){
            if (SU) {
                b.setBackgroundResource(R.drawable.day_button_unselected);
                b.setTextColor(getResources().getColor(R.color.theme1));
                SU = false;
            } else {
                b.setBackgroundResource(R.drawable.day_button_selected);
                b.setTextColor(getResources().getColor(R.color.white));
                SU = true;
            }
        }else
            Log.w("myApp", "no day found error");
    }

    public void confirmDropoff(View view) {
        Intent intent = new Intent(getApplicationContext(), CustomerInfoActivity.class);
        startActivity(intent);
    }
}