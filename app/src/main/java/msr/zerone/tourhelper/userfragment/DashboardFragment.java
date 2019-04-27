package msr.zerone.tourhelper.userfragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.calculation.SumCalculator;
import msr.zerone.tourhelper.eventfragment.model.Cost;
import msr.zerone.tourhelper.eventfragment.model.EventModel;

import static msr.zerone.tourhelper.THfirebase.costReference;
import static msr.zerone.tourhelper.THfirebase.fAuth;
import static msr.zerone.tourhelper.THfirebase.userReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment{
    private CardView weatherDashItem, galleryDashItem, eventDashItem, mapDashItem;
    private PieChart pieChart;

    private UserPreference preference;
    private FirebaseUser user;
    private SumCalculator calculator;

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
        pieChart = view.findViewById(R.id.pieChart);

        preference = new UserPreference(getContext());
        user = fAuth.getCurrentUser();
        calculator = new SumCalculator();

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        final FragmentInter inter = (FragmentInter) getActivity();

//        setUpPieChart();

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

        dataCollect();

//        Log.e("DataCollect", "onViewCreated: "+costList.size());

    }

    void setUpPieChart(float road, float meal, float medical, float shopping, float tickets, float hotel){

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(18f);

        float[] cost = {road,meal,medical,shopping,tickets, hotel};
        String[] costTitle = {"Road", "Meal", "Medical", "Shopping", "Tickets", "Hotel"};
//        String[] costTitle = getContext().getResources().getStringArray(R.array.cost_category);


        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < cost.length; i++) {
            if (cost[i] > 0.0) {
                pieEntries.add(new PieEntry(cost[i], costTitle[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Cost");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(18f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);

        pieChart.setCenterText("Cost");

        pieChart.setData(data);
    }

    void dataCollect(){
        final List<Cost> roadlist = new ArrayList<>();
        final List<Cost> meallist = new ArrayList<>();
        final List<Cost> medicallist = new ArrayList<>();
        final List<Cost> shoppinglist = new ArrayList<>();
        final List<Cost> ticketslist = new ArrayList<>();
        final List<Cost> hotellist = new ArrayList<>();
        costReference.child(preference.getHomeuId()).child(preference.getHomeEventId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Cost cost = dataSnapshot.getValue(Cost.class);
                if(cost.getFoss().equals("Road")) {
                    roadlist.add(cost);
                }
                if(cost.getFoss().equals("Meal")) {
                    meallist.add(cost);
                }
                if(cost.getFoss().equals("Medical")) {
                    medicallist.add(cost);
                }
                if(cost.getFoss().equals("Shopping")) {
                    shoppinglist.add(cost);
                }
                if(cost.getFoss().equals("Tickets")) {
                    ticketslist.add(cost);
                }
                if(cost.getFoss().equals("Hotel")) {
                    ticketslist.add(cost);
                }

                float road = (float) calculator.getCostSum(roadlist);
                float meal = (float) calculator.getCostSum(meallist);
                float medical = (float) calculator.getCostSum(medicallist);
                float shopping = (float) calculator.getCostSum(shoppinglist);
                float tickets = (float) calculator.getCostSum(ticketslist);
                float hotel = (float) calculator.getCostSum(hotellist);

                setUpPieChart(road, meal, medical, shopping, tickets, hotel);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
