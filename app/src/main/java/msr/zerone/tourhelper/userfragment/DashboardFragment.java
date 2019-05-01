package msr.zerone.tourhelper.userfragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.calculation.SumCalculator;
import msr.zerone.tourhelper.eventfragment.model.Budget;
import msr.zerone.tourhelper.eventfragment.model.Cost;
import msr.zerone.tourhelper.eventfragment.model.EventModel;

import static msr.zerone.tourhelper.THfirebase.budgetReference;
import static msr.zerone.tourhelper.THfirebase.costReference;
import static msr.zerone.tourhelper.THfirebase.eventReference;
import static msr.zerone.tourhelper.THfirebase.fAuth;
import static msr.zerone.tourhelper.THfirebase.photoReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private CardView weatherDashItem, galleryDashItem, eventDashItem, mapDashItem;
    private PieChart pieChart;
    private ProgressBar progressBarTD;
    private SwipeRefreshLayout refreshDash;
    private TextView tourNameV, barTxt;

    private UserPreference preference;
    private BudgetAndCostPref budgetAndCostPref;
    private FirebaseUser user;
    private SumCalculator calculator;

    private boolean isLocationPermissionGranted=false;

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
        progressBarTD = view.findViewById(R.id.progressBarTD);
        refreshDash = view.findViewById(R.id.refreshDash);
        tourNameV = view.findViewById(R.id.tourNameV);
        barTxt = view.findViewById(R.id.barTxt);

        preference = new UserPreference(getContext());
        budgetAndCostPref = new BudgetAndCostPref(getContext());
        user = fAuth.getCurrentUser();
        calculator = new SumCalculator();

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);

        pieChart.setCenterText("Cost");

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

        tourNameV.setText(budgetAndCostPref.getEventName());
        dataCollect();
        totalBudget();

        setUpPieChart(0,0,0,0,0,0);

        refreshDash.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataCollect();
                totalBudget();
                tourNameV.setText(budgetAndCostPref.getEventName());
                refreshDash.setRefreshing(false);
            }
        });

        pieChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "Worked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    void setUpPieChart(float road, float meal, float medical, float shopping, float tickets, float hotel){

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
        dataSet.setValueTextSize(14f);

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

                int totalBudget = (budgetAndCostPref.getBudget()+budgetAndCostPref.getNewBudget());
                int totalCost = (int) (road+meal+medical+shopping+tickets+hotel);
                progressBarTD.setMax(totalBudget);
                progressBarTD.setProgress(totalCost);

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

    void totalBudget(){
        final SumCalculator calculator = new SumCalculator();

        final List<Budget> budgets = new ArrayList<>();
        budgetReference.child(preference.getHomeuId()).child(preference.getHomeEventId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Budget budget = dataSnapshot.getValue(Budget.class);
                budgets.add(budget);

                double sum = calculator.getBudgetSum(budgets);
                budgetAndCostPref.setNewBudget((int) sum);
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

//        final List<Integer> eventBudget = new ArrayList<>();
        eventReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventModel eventModel = dataSnapshot.getValue(EventModel.class);
                if (preference.getHomeEventId().equals(eventModel.getEventId())) {
//                    eventBudget.add(Integer.valueOf(eventModel.getBudget()));
                    budgetAndCostPref.setBudget(Integer.parseInt(eventModel.getBudget()));
                    budgetAndCostPref.setEventName(eventModel.getName());
                }

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

    @Override
    public void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    111);
        } else {
            isLocationPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionGranted = true;
            }
        }
    }
}
