package msr.zerone.tourhelper.userfragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private CardView weatherDashItem, galleryDashItem, eventDashItem, mapDashItem;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherDashItem = view.findViewById(R.id.weatherDashItem);
        galleryDashItem = view.findViewById(R.id.galleryDashItem);
        eventDashItem = view.findViewById(R.id.eventDashItem);
        mapDashItem = view.findViewById(R.id.mapDashItem);

        final FragmentInter inter = (FragmentInter) getActivity();

        weatherDashItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.gotoWeatherFragment();
            }
        });

        galleryDashItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.gotoGalleryFragment();
            }
        });

        eventDashItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.gotoEventFragment();
            }
        });

        mapDashItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.gotoMapFragment();
            }
        });

    }
}
