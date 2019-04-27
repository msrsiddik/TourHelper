package msr.zerone.tourhelper.eventfragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.eventfragment.model.EventModel;

import static msr.zerone.tourhelper.THfirebase.eventReference;
import static msr.zerone.tourhelper.THfirebase.fAuth;
import static msr.zerone.tourhelper.eventfragment.TourDateCollection.dateList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventHomeFragment extends Fragment{
    private Context context;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private PopupWindow popupWindow;

    TourDateCollection pickerAlert;

    public EventHomeFragment() {
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
        return inflater.inflate(R.layout.fragment_event_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButton = view.findViewById(R.id.fabAddEvent);
        recyclerView = view.findViewById(R.id.recyclerEventView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pickerAlert = new TourDateCollection();

        final List<EventModel> eventModels = new ArrayList<>();
        eventReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventModel model = dataSnapshot.getValue(EventModel.class);
                FirebaseUser user  = fAuth.getCurrentUser();
                if (user.getUid().equals(model.getUid())){
                    eventModels.add(model);
                }
                EventRecyclerAdapter adapter = new EventRecyclerAdapter(context,eventModels);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                for(EventModel m:eventModels){
                    try {
                        TourDateCollection.dateRange(m.getDeparDate(),m.getReturnDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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


        floatingActionButton.setOnClickListener(addEventListener);

    }

    View.OnClickListener addEventListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final View popupView = getLayoutInflater().inflate(R.layout.add_event_layout, null);

            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            // If the PopupWindow should be focusable
            popupWindow.setFocusable(true);

            // If you need the PopupWindow to dismiss when when touched outside
            popupWindow.setBackgroundDrawable(new ColorDrawable());

            FloatingActionButton close = popupView.findViewById(R.id.closeEventAddWindow);
            final TextInputLayout eventName, startLocation, destination, estimatedBudget;
            ImageButton departureDatePicker = popupView.findViewById(R.id.departureDatePicker);
            final EditText departureDate = popupView.findViewById(R.id.departureDate);
            ImageButton returnDatePicker = popupView.findViewById(R.id.returnDatePicker);
            final EditText returnDate = popupView.findViewById(R.id.returnDate);
            Button createEventBtn = popupView.findViewById(R.id.createEventBtn);
            eventName = popupView.findViewById(R.id.eventName);
            startLocation = popupView.findViewById(R.id.startLocation);
            destination = popupView.findViewById(R.id.destination);
            estimatedBudget = popupView.findViewById(R.id.estimatedBudget);

            createEventBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser user = fAuth.getCurrentUser();
                    String createdDate = new SimpleDateFormat("MMM dd, yyyy").format(new Date());
                    String id = eventReference.push().getKey();
                    String en = eventName.getEditText().getText().toString();
                    String sl = startLocation.getEditText().getText().toString();
                    String dst = destination.getEditText().getText().toString();
                    String dtd = departureDate.getText().toString();
                    String rtd = returnDate.getText().toString();
                    String bdgt = estimatedBudget.getEditText().getText().toString();

//                    boolean okay = false;
                    if (!dateList.isEmpty()) {
                        for (String s : dateList) {
                            if (s.equals(dtd) || s.equals(rtd)) {
                                Toast.makeText(popupView.getContext(), "Please change the date, these days you have tours", Toast.LENGTH_SHORT).show();
                                break;
//                            } else {
//                                okay = true;
                            }
                        }
                    }

                    if (!en.isEmpty() && !sl.isEmpty() && !dst.isEmpty() && !dtd.isEmpty() && !rtd.isEmpty() && !bdgt.isEmpty()) {

//                        if (okay) {
                            EventModel model = new EventModel(id, user.getUid(), en, sl, dst, createdDate, dtd, rtd, bdgt);
                            eventReference.child(id).setValue(model);
                            popupWindow.dismiss();
//                        }
                    }else {
                        Toast.makeText(popupView.getContext(), "Please fill up all field", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            departureDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar todaysCalender = Calendar.getInstance();
                    int year = todaysCalender.get(Calendar.YEAR);
                    int month = todaysCalender.get(Calendar.MONTH);
                    int day = todaysCalender.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog =
                            new DatePickerDialog(getView().getContext(),
                                    listener, year, month, day);
                    datePickerDialog.show();

                }
                private DatePickerDialog.OnDateSetListener listener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("MMM dd, yyyy");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(i, i1, i2);

                                String dob = simpleDateFormat.format(calendar.getTime());

                                if (!dateList.isEmpty()) {
                                    for (String s : dateList) {
                                        if (s.equals(dob)) {
                                            Toast.makeText(popupView.getContext(), "Please change the date, these days you have tours", Toast.LENGTH_LONG).show();
                                            break;
                                        }
                                        departureDate.setText(dob);
                                    }
                                }

                                if (dateList.isEmpty()){
                                    departureDate.setText(dob);
                                }

                            }
                        };

            });

            returnDatePicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar todaysCalender = Calendar.getInstance();
                                int year = todaysCalender.get(Calendar.YEAR);
                                int month = todaysCalender.get(Calendar.MONTH);
                                int day = todaysCalender.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog =
                                        new DatePickerDialog(getView().getContext(),
                                                listener, year, month, day);
                                datePickerDialog.show();
                            }
                            private DatePickerDialog.OnDateSetListener listener =
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                            SimpleDateFormat simpleDateFormat =
                                                    new SimpleDateFormat("MMM dd, yyyy");
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(i, i1, i2);

                                            String dob = simpleDateFormat.format(calendar.getTime());

                                            if (!dateList.isEmpty()) {
                                                for (String s : dateList) {
                                                    if (s.equals(dob)) {
                                                        Toast.makeText(popupView.getContext(), "Please change the date, these days you have tours", Toast.LENGTH_LONG).show();
                                                        break;
                                                    }
                                                    returnDate.setText(dob);
                                                }
                                            }

                                            if (dateList.isEmpty()){
                                                returnDate.setText(dob);
                                            }


                                        }
                                    };

                        });




            int location[] = new int[2];

            // Get the View's(the one that was clicked in the Fragment) location
            getView().getLocationOnScreen(location);

            // Using location, the PopupWindow will be displayed right under anchorView
            popupWindow.showAtLocation(getView(), Gravity.NO_GRAVITY,
                    location[0], location[1] + getView().getHeight());

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
    };

}
