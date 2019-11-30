package com.jaemo.jaemo.customer.drawer_fragments.pickup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.osmdroid.views.MapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import com.jaemo.jaemo.R;
import com.jaemo.jaemo.customer.dropoff.DropoffActivity;

public class PickupFragment extends Fragment implements LocationListener {

    private static final String Fine_Location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int Location_Permission_Request_Code = 1234;
    private static final float DEFAULT_ZOOM = 16;
    private boolean LocPermissionGranted = false;

    private LocationManager locationManager;
    private Location location;
    private MapController mapController;
    private PickupViewModel pickupViewModel;
    private MapView mMapView;
    private View root;

    private String TAG = "osmdroidMap";
    private EditText chooseTime;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.chooseTime = getActivity().findViewById(R.id.editText2);
        pickupViewModel = ViewModelProviders.of(this).get(PickupViewModel.class);
        root = inflater.inflate(R.layout.fragment_pickup, container, false);
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(getActivity().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));
        getLocationPermission();
        /*final TextView textView = root.findViewById(R.id.text_home);
        pickupViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    public void chooseTime(View view){
    }

    //if permissions are already granted then initialize the map else request for permissions
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), Fine_Location) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), Coarse_Location) == PackageManager.PERMISSION_GRANTED) {
                LocPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, Location_Permission_Request_Code);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, Location_Permission_Request_Code);
        }
    }

    //request user for permissions and then initialize the map
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
        Toast.makeText(getActivity(), "map is ready", Toast.LENGTH_SHORT).show();
        mMapView = (MapView) root.findViewById(R.id.mapview);
        if (mMapView != null) {
            mapController = (MapController) mMapView.getController();
            mapController.setZoom(DEFAULT_ZOOM);
            mMapView.setTileSource(TileSourceFactory.MAPNIK);
            mMapView.setTilesScaledToDpi(true);
            mMapView.setBuiltInZoomControls(false);
            mMapView.setMultiTouchControls(true);
            getDeviceLocation();
        }
    }

    //get devices location and move camera to that location
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // Getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                //showalert in activity
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            moveCamera(new GeoPoint(location.getLatitude(), location.getLongitude()));
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 0, 0, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                moveCamera(new GeoPoint(location.getLatitude(), location.getLongitude()));
                            }
                        }
                    }
                }
            }
            //moveCamera(new GeoPoint(33.642525, 72.98642));
        } catch (SecurityException se) {
            Log.e(TAG, "getDeviceLocation: SecurityException:" + se.getMessage());
        }
    }

    //take a latLng object, move to the location and draw a marker on that location
    private void moveCamera(GeoPoint currentLocation) {
        mapController.animateTo(currentLocation);
        Marker my_location = new Marker(mMapView);
        my_location.setPosition(currentLocation);
        my_location.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(my_location);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}