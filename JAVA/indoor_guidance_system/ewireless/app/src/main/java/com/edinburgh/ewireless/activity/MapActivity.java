package com.edinburgh.ewireless.activity;

/**
 * Author: yijianzheng
 * Date: 19/04/2023 20:07
 * <p>
 * Notes:
 */

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.edinburgh.ewireless.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private ArrayList<LatLng> coordinates;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get the coordinates from the Intent
        coordinates = getIntent().getParcelableArrayListExtra("coordinates");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**

     Called when the Google Map is ready to be used
     @param googleMap the GoogleMap object that is ready to be used
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable the blue dot indicating the user's location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Log.d(TAG, "onMapReady:  Marks size" + coordinates.size());
//        for (LatLng coordinate : coordinates) {
//            Log.d(TAG, "onMapReady: Marks Latitude: " + coordinate.latitude + " Longitude:  " + coordinate.longitude);
//            mMap.addMarker(new MarkerOptions().position(coordinate).title("Marker"));
//        }
        for (int i = 0; i < coordinates.size(); i++) {
            LatLng point = coordinates.get(i);
            mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("Point " + (i + 1))
                    .snippet("Order: " + (i + 1)));
        }
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(coordinates)
                .width(10)
                .color(Color.RED)
                .geodesic(true);
        mMap.addPolyline(polylineOptions);

        // Move the camera to the current location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Log.d(TAG, "onMapReady: Latitude" + location.getLatitude() + " Longitude:  " + location.getLongitude());
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    }
                });
        // Add markers for each coordinate



        // Move the camera to the first coordinate and set zoom level
//        if (!coordinates.isEmpty()) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates.get(0), 14));
//        }
    }
}
