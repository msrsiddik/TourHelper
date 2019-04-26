package msr.zerone.tourhelper.map;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.map.nearby.NearbyResponse;
import msr.zerone.tourhelper.map.nearby.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMarkerClickListener {

    private Context context;
    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;
    private boolean mLocationPermissionGranted = true;
    private FusedLocationProviderClient client;
//    private MarkerOptions currentPositionMarker = null;
//    private Marker currentLocationMarker;
    private FloatingActionButton nearbyBtn;
    private RecyclerView placeRecyclerView;
    private Spinner spinnerView;
    private Button findBtn;

    private String typeNearbyPlace;
    private LatLng currentLatLng;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nearbyBtn = view.findViewById(R.id.nearbyBtn);
        placeRecyclerView = view.findViewById(R.id.placeRecyclerView);
        spinnerView = view.findViewById(R.id.spinnerView);
        findBtn = view.findViewById(R.id.findBtn);

        client = LocationServices.getFusedLocationProviderClient(context);
        getLocationPermission();


        FragmentManager fm = getActivity().getSupportFragmentManager();/// getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.mapFrag);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapFrag, supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        nearbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNearbyPlaces(currentLatLng);
            }
        });

        final String[] nearByPlaceType = {"airport","atm","bank","bus_station","cafe","car_repair","gas_station",
                "hospital","jewelry_store","movie_theater","park","police","restaurant","shopping_mall",
                "supermarket","taxi_stand","train_station"};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.nearby_place_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerView.setAdapter(adapter);
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeNearbyPlace = nearByPlaceType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNearbyPlaces(currentLatLng);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        //mMap.setOnMarkerDragListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setTrafficEnabled(true);
        if(mLocationPermissionGranted){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        //updateLocationUI();

        getCurrentLocation();

    }

    private void getCurrentLocation() {
        if(mLocationPermissionGranted){
            client.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location == null){
                                return;
                            }

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng currentLatLnt = new LatLng(latitude, longitude);
                            /*Marker myPositionMarker =
                                    mMap.addMarker(new MarkerOptions()
                                    .position(currentLatLnt)
                                    .title("I am here")
                                    .draggable(true));*/
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLnt, 14f));
//                            getNearbyPlaces(new LatLng(latitude, longitude));
                        }
                    });
        }

    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getCurrentLocation();
                }
            }
        }
        updateLocationUI();
    }

    private void getNearbyPlaces(LatLng latLng){
        final String apiKey = getString(R.string.nearby_place_api_key);
        String endUrl = String.format("place/nearbysearch/json?location=%f,%f&radius=1500&type=%s&key=%s",
                latLng.latitude, latLng.longitude, typeNearbyPlace, apiKey);
        NearbyService service = RetrofitClient.getClient()
                .create(NearbyService.class);
        service.getNearbyPlaces(endUrl)
                .enqueue(new Callback<NearbyResponse>() {
                    @Override
                    public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                        if(response.isSuccessful()){
                            NearbyResponse nearbyResponse = response.body();
                            List<Result> resultList = nearbyResponse.getResults();
                            for(Result r : resultList){
                                double lat = r.getGeometry().getLocation().getLat();
                                double lng = r.getGeometry().getLocation().getLng();
                                LatLng rest = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions()
                                        .position(rest)
                                        .title(r.getName()));
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                            NearbyRecyclerViewAdapter adapter = new NearbyRecyclerViewAdapter(context, resultList);
                            placeRecyclerView.setLayoutManager(layoutManager);
                            placeRecyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<NearbyResponse> call, Throwable t) {

                    }
                });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(context, "started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(context, "stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mMap.getCameraPosition().target;
//        getNearbyPlaces(latLng);
        currentLatLng = latLng;
    }

    @Override
    public void onCameraMove() {
        LatLng latLng = mMap.getCameraPosition().target;
        mMap.clear();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(context, marker.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }
}
