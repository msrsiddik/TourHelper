package msr.zerone.tourhelper.weather;

public interface LocationFindInterface {
    void receiveLocation(double latitude, double longitude);
    void searchCity(String cityName);
}
