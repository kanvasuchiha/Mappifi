package com.smartherd.mappifi;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartherd.mappifi.Model.CountryDataSource;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String receivedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        //WE NEED TO RECEIVE RECEIVEDCOUNTRY NAME BEFOR THE MAP IS READY TO BE ABLE TO DISPLAY IT.
        //Now finding the intent that launched this activity
        Intent mainActivityIntent = this.getIntent();
        receivedCountry = mainActivityIntent.getStringExtra(CountryDataSource.COUNTREY_KEY);
        if(receivedCountry == null)
        {
            receivedCountry=CountryDataSource.DEFAULT_COUNTRY_NAME;
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



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



        //SET MARKER TO YOUR HOSTEL ROOM BY FINDING WHAT'S HERE.
        //30.352345, 76.360353  TIET, M Hostel
        // Add a marker in TIET and move the camera
        /*LatLng hostel = new LatLng(30.352345, 76.360353);
        mMap.addMarker(new MarkerOptions().position(hostel).title("Marker in M hostel, TIET").snippet("Fantastic"));
        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(hostel, 15.0f);
        mMap.moveCamera(cameraUpdate);
        CircleOptions circleOptions= new CircleOptions();
        circleOptions.center(hostel);
        circleOptions.radius(300);
        circleOptions.strokeWidth(20.0f);
        circleOptions.strokeColor(Color.YELLOW);
        mMap.addCircle(circleOptions);*/




        //FINAL CODE

        double countryLatitude= CountryDataSource.DEFAULT_COUNTRY_LATITUDE;
        double countryLongitude= CountryDataSource.DEFAULT_COUNTRY_LONGITUDE;

        CountryDataSource countryDataSource = MainActivity.countryDataSource;
        String countryMessage = countryDataSource.getInfoOfTheCountry(receivedCountry);

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try{

            String countryAddress = receivedCountry;
            List<Address> countryAddresses = geocoder.getFromLocationName(countryAddress, 10);
            if (countryAddresses!=null)
            {
                countryLatitude= countryAddresses.get(0).getLatitude();
                countryLongitude= countryAddresses.get(0).getLongitude();
            } else
            {
                receivedCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;

            }

        } catch (IOException ioe)
        {
            receivedCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;
        }


        LatLng myCountryLocation =  new LatLng(countryLatitude,countryLongitude);
        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(myCountryLocation, 8.0f);
        mMap.moveCamera(cameraUpdate);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myCountryLocation);
        markerOptions.title(countryMessage);
        markerOptions.snippet(CountryDataSource.DEFAULT_MESSAGE);
        mMap.addMarker(markerOptions);



        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(myCountryLocation);
        circleOptions.radius(400);
        circleOptions.strokeWidth(15f);
        circleOptions.strokeColor(Color.GREEN);
        mMap.addCircle(circleOptions);

    }
}
