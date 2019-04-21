package msr.zerone.tourhelper.weather;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.weather.forecastpojo.ForecastWeather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static msr.zerone.tourhelper.weather.WeatherHomeFragment.API;
import static msr.zerone.tourhelper.weather.WeatherHomeFragment.WEATEHR_BASE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastWeatherFragment extends Fragment implements LocationFindInterface {
    private TextView date1, date2, date3, date4, date5;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5;

    public ForecastWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        date1 = view.findViewById(R.id.date1);
        date2 = view.findViewById(R.id.date2);
        date3 = view.findViewById(R.id.date3);
        date4 = view.findViewById(R.id.date4);
        date5 = view.findViewById(R.id.date5);
        recyclerView1 = view.findViewById(R.id.recyclerViewInForecastId1);
        recyclerView2 = view.findViewById(R.id.recyclerViewInForecastId2);
        recyclerView3 = view.findViewById(R.id.recyclerViewInForecastId3);
        recyclerView4 = view.findViewById(R.id.recyclerViewInForecastId4);
        recyclerView5 = view.findViewById(R.id.recyclerViewInForecastId5);


    }

    @Override
    public void receiveLocation(double latitude, double longitude) {
        if (WeatherHomeFragment.searchCityName.isEmpty()) {
            forecastByLocation(latitude, longitude, null);
        }else {
            forecastByLocation(0.0, 0.0, WeatherHomeFragment.searchCityName);
        }

    }


    public void forecastByLocation(double latitude, double longitude, String cityName) {
        String url="";
        if (WeatherHomeFragment.searchCityName.isEmpty()) {
             url = String.format("data/2.5/forecast?lat=%f&lon=%f&units=metric&appid=%s", latitude, longitude, API);
        }else {
            url = String.format("data/2.5/forecast?q=%s&units=metric&appid=%s",cityName, API);
        }

        WeatherApi weatherApi = RetrofitClient.getClient(WEATEHR_BASE_URL).create(WeatherApi.class);
        weatherApi.Forecast(url).enqueue(new Callback<ForecastWeather>() {
            @Override
            public void onResponse(Call<ForecastWeather> call, Response<ForecastWeather> response) {
                if (response.isSuccessful()) {
                    ForecastWeather forecastWeather = response.body();

                    ArrayList allDate = new ArrayList<>();
                    for (int i = 0; i < forecastWeather.getList().size(); i++) {
                        allDate.add(new SimpleDateFormat("yyyy-MM-dd").format(new Date(forecastWeather.getList().get(i).getDt()*1000)));
                    }
                    ArrayList<String> singleDate = removeDuplicates(allDate);

                    List<msr.zerone.tourhelper.weather.forecastpojo.List> forecastWeatherList1 = new ArrayList<>();
                    List<msr.zerone.tourhelper.weather.forecastpojo.List> forecastWeatherList2 = new ArrayList<>();
                    List<msr.zerone.tourhelper.weather.forecastpojo.List> forecastWeatherList3 = new ArrayList<>();
                    List<msr.zerone.tourhelper.weather.forecastpojo.List> forecastWeatherList4 = new ArrayList<>();
                    List<msr.zerone.tourhelper.weather.forecastpojo.List> forecastWeatherList5 = new ArrayList<>();

                    for (int i = 0; i < forecastWeather.getList().size(); i++) {
                        String date = forecastWeather.getList().get(i).getDtTxt().subSequence(0,10).toString();
                        if (singleDate.get(0).equals(date)){
                            forecastWeatherList1.add(forecastWeather.getList().get(i));
                        }
                    }

                    for (int i = 0; i < forecastWeather.getList().size(); i++) {
                        String date = forecastWeather.getList().get(i).getDtTxt().subSequence(0,10).toString();
                        if (singleDate.get(1).equals(date)){
                            forecastWeatherList2.add(forecastWeather.getList().get(i));
                        }
                    }

                    for (int i = 0; i < forecastWeather.getList().size(); i++) {
                        String date = forecastWeather.getList().get(i).getDtTxt().subSequence(0,10).toString();
                        if (singleDate.get(2).equals(date)){
                            forecastWeatherList3.add(forecastWeather.getList().get(i));
                        }
                    }

                    for (int i = 0; i < forecastWeather.getList().size(); i++) {
                        String date = forecastWeather.getList().get(i).getDtTxt().subSequence(0,10).toString();
                        if (singleDate.get(3).equals(date)){
                            forecastWeatherList4.add(forecastWeather.getList().get(i));
                        }
                    }

                    for (int i = 0; i < forecastWeather.getList().size(); i++) {
                        String date = forecastWeather.getList().get(i).getDtTxt().subSequence(0,10).toString();
                        if (singleDate.get(4).equals(date)){
                            forecastWeatherList5.add(forecastWeather.getList().get(i));
                        }
                    }


                    date1.setText(singleDate.get(0));
                    date2.setText(singleDate.get(1));
                    date3.setText(singleDate.get(2));
                    date4.setText(singleDate.get(3));
                    date5.setText(singleDate.get(4));

                    LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    LinearLayoutManager layoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    LinearLayoutManager layoutManager5 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    ForecastAdapter adapter1 = new ForecastAdapter(getContext(), forecastWeatherList1);
                    ForecastAdapter adapter2 = new ForecastAdapter(getContext(), forecastWeatherList2);
                    ForecastAdapter adapter3 = new ForecastAdapter(getContext(), forecastWeatherList3);
                    ForecastAdapter adapter4 = new ForecastAdapter(getContext(), forecastWeatherList4);
                    ForecastAdapter adapter5 = new ForecastAdapter(getContext(), forecastWeatherList5);
                    recyclerView1.setLayoutManager(layoutManager1);
                    recyclerView2.setLayoutManager(layoutManager2);
                    recyclerView3.setLayoutManager(layoutManager3);
                    recyclerView4.setLayoutManager(layoutManager4);
                    recyclerView5.setLayoutManager(layoutManager5);
                    recyclerView1.setAdapter(adapter1);
                    recyclerView2.setAdapter(adapter2);
                    recyclerView3.setAdapter(adapter3);
                    recyclerView4.setAdapter(adapter4);
                    recyclerView5.setAdapter(adapter5);

                }
            }

            @Override
            public void onFailure(Call<ForecastWeather> call, Throwable t) {
                Log.e("Failure", "onFailure: "+t.getLocalizedMessage() );
            }
        });
    }


    // Function to remove duplicates from an ArrayList
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
}
