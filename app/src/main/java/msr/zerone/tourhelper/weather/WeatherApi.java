package msr.zerone.tourhelper.weather;

import msr.zerone.tourhelper.weather.forecastpojo.ForecastWeather;
import msr.zerone.tourhelper.weather.todayforecast.TodayForecast3h;
import msr.zerone.tourhelper.weather.todaypojo.TodayWeatherService;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherApi {
    @GET
    Call<TodayWeatherService> todayWeatherServiceCall(@Url String url);

    @GET
    Call<TodayForecast3h> todayForecast(@Url String url);

    @GET
    Call<ForecastWeather> Forecast(@Url String url);
}
