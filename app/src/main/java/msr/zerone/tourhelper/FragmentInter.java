package msr.zerone.tourhelper;

public interface FragmentInter {
    void gotoRegistrationFragment();
    void gotoFullImageViewFragment();
    void gotoWeatherFragment();
    void gotoGalleryFragment();
    void gotoEventFragment();
    void gotoMapFragment();
    void gotoDashboardFragment();
    void login(boolean b);
    void onNetworkConnectionChanged(String status);
}
