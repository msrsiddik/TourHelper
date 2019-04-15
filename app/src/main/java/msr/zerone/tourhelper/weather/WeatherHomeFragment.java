package msr.zerone.tourhelper.weather;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import msr.zerone.tourhelper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherHomeFragment extends Fragment {
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LocationFindInterface locationForToday;
    private LocationFindInterface locationForForcast;
    private FusedLocationProviderClient client;
    private double latitude, longitude;
    private boolean isLocationPermissionGranted=false;


    public WeatherHomeFragment() {
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
        return inflater.inflate(R.layout.fragment_weather_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = LocationServices.getFusedLocationProviderClient(context);
//
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
//
        TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    111);
        } else {
            isLocationPermissionGranted = true;
            getDeviceCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionGranted = true;
                getDeviceCurrentLocation();
            }
        }
    }

    private void getDeviceCurrentLocation() {
        if (isLocationPermissionGranted) {
            client.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location == null){
                                return;
                            }
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            locationForToday.receiveLocation(latitude,longitude);
                            locationForForcast.receiveLocation(latitude,longitude);

                        }
                    });
        }
    }

    private class TabAdapter extends FragmentStatePagerAdapter {
        private int noOfTabPage;
        private TodayWeatherFragment todayFragment;
        private ForecastWeatherFragment forecastFragment;

        public TabAdapter(FragmentManager fm, int noOfTabPage) {
            super(fm);
            this.noOfTabPage = noOfTabPage;
            todayFragment = new TodayWeatherFragment();
            forecastFragment = new ForecastWeatherFragment();
            locationForToday = todayFragment;
            locationForForcast = forecastFragment;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return todayFragment;
                case 1:
                    return forecastFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return noOfTabPage;
        }
    }

}
