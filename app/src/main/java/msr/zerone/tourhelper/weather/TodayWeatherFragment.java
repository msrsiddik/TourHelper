package msr.zerone.tourhelper.weather;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.weather.todayforecast.TodayForecast3h;
import msr.zerone.tourhelper.weather.todaypojo.TodayWeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static msr.zerone.tourhelper.weather.WeatherHomeFragment.API;
import static msr.zerone.tourhelper.weather.WeatherHomeFragment.WEATEHR_BASE_URL;
import static msr.zerone.tourhelper.weather.WeatherHomeFragment.WEATHER_ICON_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment implements LocationFindInterface {
    private ImageView weather_icon;
    private TextView location, temperature, celsius, fahrenheit, main_status, update_time, wind_deg, wind_speed, visibility, pressure, humidity, sunrise, sunset;
    private RecyclerView recyclerViewInTodayWeather;
    private SwipeRefreshLayout swipeRefresh;
    private double latitude, longitude;



    public TodayWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        location = view.findViewById(R.id.location);
        weather_icon = view.findViewById(R.id.weather_icon);
        temperature = view.findViewById(R.id.temperature);
        celsius = view.findViewById(R.id.celsius);
        fahrenheit = view.findViewById(R.id.fahrenheit);
        main_status = view.findViewById(R.id.main_status);
        update_time = view.findViewById(R.id.update_time);
        wind_deg = view.findViewById(R.id.wind_deg);
        wind_speed = view.findViewById(R.id.wind_speed);
        visibility = view.findViewById(R.id.visibility);
        pressure = view.findViewById(R.id.pressure);
        humidity = view.findViewById(R.id.humidity);
        recyclerViewInTodayWeather = view.findViewById(R.id.recyclerViewInTodayWeather);
        sunrise = view.findViewById(R.id.sunrise);
        sunset = view.findViewById(R.id.sunset);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                weatherInfoDisplay(latitude,longitude,null);
                swipeRefresh.setRefreshing(false);
            }
        });

    }


    @Override
    public void receiveLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (WeatherHomeFragment.searchCityName.isEmpty()) {
            weatherInfoDisplay(latitude, longitude, null);
        }else {
            weatherInfoDisplay(0.0,0.0,WeatherHomeFragment.searchCityName);
        }
    }

    private void weatherInfoDisplay(double latitude, double longitude, String cityName) {
        String urlC = null, urlF = null;
        if (WeatherHomeFragment.searchCityName.isEmpty()) {
            urlF = String.format("data/2.5/forecast?lat=%f&lon=%f&units=metric&cnt=7&appid=%s",latitude, longitude, API);
            urlC = String.format("data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s", latitude, longitude, API);

        }else {
            urlF = String.format("data/2.5/forecast?q=%s&units=metric&cnt=7&appid=%s",cityName, API);
            urlC = String.format("data/2.5/weather?q=%s&units=metric&appid=%s", cityName, API);
        }

        WeatherApi weatherApi = RetrofitClient.getClient(WEATEHR_BASE_URL).create(WeatherApi.class);
        weatherApi.todayWeatherServiceCall(urlC).enqueue(new Callback<TodayWeatherService>() {
            @Override
            public void onResponse(Call<TodayWeatherService> call, Response<TodayWeatherService> response) {
                if (response.isSuccessful()){
                    TodayWeatherService today = response.body();
                    location.setText(today.getName()+", "+today.getSys().getCountry());
                    Picasso.get().load(WEATHER_ICON_URL+today.getWeather().get(0).getIcon()+".png").into(weather_icon);
                    temperature.setText(""+today.getMain().getTemp());
                    main_status.setText(today.getWeather().get(0).getMain());
                    update_time.setText("Updated as of "+new SimpleDateFormat("h:mm a").format(new Date(today.getDt()*1000)));
                    wind_deg.setText("Feels like "+today.getWind().getDeg());
                    wind_speed.setText("Wind "+today.getWind().getSpeed());
                    visibility.setText("Visibility "+(today.getVisibility()/1000)+"km");
                    pressure.setText("Barometer "+today.getMain().getPressure()+"mb");
                    humidity.setText("Humidity "+today.getMain().getHumidity()+"%");

                    sunrise.setText("Sunrise "+new SimpleDateFormat("h:mm a").format(new Date(today.getSys().getSunrise()*1000)));
                    sunset.setText("Sunset "+new SimpleDateFormat("h:mm a").format(new Date(today.getSys().getSunset()*1000)));

                }
            }

            @Override
            public void onFailure(Call<TodayWeatherService> call, Throwable t) {
                Log.e("Today", "onFailure: "+t.getLocalizedMessage() );
            }
        });


//        String url1 = String.format("data/2.5/forecast?q=%s&units=metric&cnt=7&appid=%s",city,API);
//        String url1 = String.format("data/2.5/forecast?q=%s&units=Imperial&cnt=7&appid=%s",city,API);
        weatherApi.todayForecast(urlF).enqueue(new Callback<TodayForecast3h>() {
            @Override
            public void onResponse(Call<TodayForecast3h> call, Response<TodayForecast3h> response) {
                if (response.isSuccessful()){
                    TodayForecast3h list = response.body();

                    TodayForecastRecyclerViewAdaper adapter = new TodayForecastRecyclerViewAdaper(getContext(),list.getList());
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerViewInTodayWeather.setLayoutManager(llm);
                    recyclerViewInTodayWeather.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<TodayForecast3h> call, Throwable t) {
                Log.e("Failure", "onFailure: "+t.getLocalizedMessage() );
            }
        });

    }
}
