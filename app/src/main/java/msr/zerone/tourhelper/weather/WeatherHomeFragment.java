package msr.zerone.tourhelper.weather;


import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import msr.zerone.tourhelper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherHomeFragment extends Fragment {
    private Context context;
    private Toolbar toolbar;
    private SearchView searchView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LocationFindInterface locationForToday;
    private LocationFindInterface locationForForcast;
    private FusedLocationProviderClient client;
    private double latitude, longitude;
    private boolean isLocationPermissionGranted=false;

    public static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
    public static final String WEATEHR_BASE_URL = "http://api.openweathermap.org/";
    public static String API = "";
    public static String searchCityName = "";

    public static boolean reStartWeather = false;


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

        toolbar = view.findViewById(R.id.toolbar);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        API = getString(R.string.weather_api_key_1);


        searchItemRecent();

        setHasOptionsMenu(true);
        toolbar.setTitle("Weather");
        toolbar.inflateMenu(R.menu.weather_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                SearchManager manager = (SearchManager) getActivity().getSystemService(context.SEARCH_SERVICE);
                switch (menuItem.getItemId()) {
                    case R.id.search_menu_item:
                        searchView = (SearchView) menuItem.getActionView();
                        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
                        searchView.setSubmitButtonEnabled(true);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                reStartWeather = true;
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                return false;
                            }
                        });
                        return true;
                }
                return true;
            }
        });




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

    private void searchItemRecent(){
        Intent intent = getActivity().getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String city = intent.getStringExtra(SearchManager.QUERY);
            reStartWeather = true;
            Toast.makeText(context, city, Toast.LENGTH_SHORT).show();
            searchCityName = city;
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(context, RecentQueryProvider.AUTHORITY, RecentQueryProvider.MODE);
            suggestions.saveRecentQuery(city, null);
        }
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

    protected class TabAdapter extends FragmentStatePagerAdapter {
        private int noOfTabPage;
        protected TodayWeatherFragment todayFragment;
        protected ForecastWeatherFragment forecastFragment;

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
