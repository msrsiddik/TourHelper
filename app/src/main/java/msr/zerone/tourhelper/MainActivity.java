package msr.zerone.tourhelper;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import msr.zerone.tourhelper.weather.WeatherHomeFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragmentContainer, new WeatherHomeFragment()).commit();
    }
}
