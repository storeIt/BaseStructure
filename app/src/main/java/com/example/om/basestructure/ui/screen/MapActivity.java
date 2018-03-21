package com.example.om.basestructure.ui.screen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.example.om.basestructure.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

    public static final String ADDRESS = "address";
    public static final String COORDINATES = "coordinates";
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Button mBtnSaveLocation;
    private Place mSelectedLocation;
    private LatLng mSelectedCoordinates;
    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            if (mSelectedCoordinates != null) {
                mGoogleMap.addMarker(new MarkerOptions().position(mSelectedCoordinates).title("ToDo destination"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mSelectedCoordinates, 14.0f));
            }
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(MapActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(MapActivity.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSelectedCoordinates = bundle.getParcelable(COORDINATES);
        }
        mMapView = (MapView) findViewById(R.id.map_view_content_maps);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(onMapReadyCallback);
        mBtnSaveLocation = (Button) findViewById(R.id.btn_save_location_content_map);
        mBtnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedLocation != null) {
                    String address = mSelectedLocation.getAddress().toString();
                    LatLng coordinates = mSelectedLocation.getLatLng();
                    Bundle bundle = new Bundle();
                    bundle.putString(ADDRESS, address);
                    bundle.putParcelable(COORDINATES, coordinates);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }

            }
        });
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.maps_auto_complete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mSelectedLocation = place;
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng())
                        .title("ToDo destination"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14.0f));
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mGoogleMap = googleMap;
//        if (mSelectedCoordinates != null) {
//            mGoogleMap.addMarker(new MarkerOptions().position(mSelectedCoordinates).title("ToDo destination"));
//        }
//        if (Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        } else {
//            mGoogleMap.setMyLocationEnabled(true);
//        }
//    }
}
