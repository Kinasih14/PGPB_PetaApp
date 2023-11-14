package com.example.apppeta;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.apppeta.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;


import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Boolean oke = false;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private boolean currentlocation = false;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager getCurrentLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        getCurrentLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (currentlocation) {
                    LatLng newloc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(newloc).title("My Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newloc, 20));
                }
            }
        });
    }

    public void onProviderEnabled(@NonNull String Provider){

    }

    public void onProviderDisabled(@NonNull String Provider){

    }

    public void onStatusChanged(String Provider, int status, Bundle extras){

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentlocation = true;

        setMapOnLongClick(googleMap);
        setPoiClick(googleMap);


        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dtk, 20));



        LatLng Yogyakarta  = new LatLng( -7.782153345416471, 110.36745991830131 );
        mMap.addMarker(new MarkerOptions().position(Yogyakarta).title("Yogyakarta").icon(bitmapDescriptor(getApplicationContext(),R.drawable.yogyakarta)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Yogyakarta, 15));

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().isMyLocationButtonEnabled();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        mMap.setMyLocationEnabled(true);

    }

    private BitmapDescriptor bitmapDescriptor(Context context,int vectoreResId){
        Drawable vectorDrawabel = ContextCompat.getDrawable(context,vectoreResId);
        vectorDrawabel.setBounds(0,0,vectorDrawabel.getIntrinsicWidth(),vectorDrawabel.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawabel.getIntrinsicWidth(),vectorDrawabel.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);


        Canvas canvas=new Canvas(bitmap);
        vectorDrawabel.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private Bitmap resizeBitmap(int imageResource, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), imageResource);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.normal_map){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (item.getItemId() == R.id.satellite_map) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if (item.getItemId() == R.id.terrain_map) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        return true;
    }
    private void setMapOnLongClick(final GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {

                String text = String.format(Locale.getDefault(),
                        "Lat : %1$.5f, Long : %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                map.addMarker(new MarkerOptions().position(latLng)
                        .title("Dropped pin")
                        .snippet(text));
            }
        });

    }
    private void setPoiClick(final GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(pointOfInterest.latLng)
                        .title(pointOfInterest.name));
                poiMarker.showInfoWindow();

                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                poiMarker.setIcon(icon);

                poiMarker.showInfoWindow();

            }
        });
    }
}