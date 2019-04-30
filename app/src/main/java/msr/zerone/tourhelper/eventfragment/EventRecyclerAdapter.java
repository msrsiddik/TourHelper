package msr.zerone.tourhelper.eventfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.calculation.SumCalculator;
import msr.zerone.tourhelper.eventfragment.model.Budget;
import msr.zerone.tourhelper.eventfragment.model.Cost;
import msr.zerone.tourhelper.eventfragment.model.EventModel;
import msr.zerone.tourhelper.userfragment.UserPreference;

import static msr.zerone.tourhelper.THfirebase.budgetReference;
import static msr.zerone.tourhelper.THfirebase.costReference;
import static msr.zerone.tourhelper.THfirebase.eventReference;
import static msr.zerone.tourhelper.THfirebase.fAuth;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventHolder> {

    private Context context;
    private List<EventModel> eventModelList;
    private UserPreference preference;

    public EventRecyclerAdapter(Context context, List<EventModel> eventModelList) {
        this.context = context;
        this.eventModelList = eventModelList;
        preference = new UserPreference(context);
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EventHolder(LayoutInflater.from(context).inflate(R.layout.event_item_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final EventHolder eventHolder, int i) {
        final int listId = i;
        final EventModel model = eventModelList.get(i);

        eventHolder.eventName.setText(model.getName());
        eventHolder.eventDate.setText(model.getDeparDate()+"  <=>  "+model.getReturnDate());

        eventHolder.eventItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View popupView = LayoutInflater.from(context).inflate(R.layout.event_details, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());

                final Dialog dialog = new Dialog(popupView.getContext());

                FloatingActionButton close = popupView.findViewById(R.id.closeEventAddWindow);
                final TextView eventHomeV, destinationV, deparDateV, returnDateV, budgetV, totalCostV;
                CardView budgetAdd, costAdd;
                final RecyclerView costRecyclerView;

                eventHomeV = popupView.findViewById(R.id.eventHomeV);
                destinationV = popupView.findViewById(R.id.destinationV);
                deparDateV = popupView.findViewById(R.id.deparDateV);
                returnDateV = popupView.findViewById(R.id.returnDateV);
                budgetV = popupView.findViewById(R.id.budgetV);
                budgetAdd = popupView.findViewById(R.id.budgetAdd);
                totalCostV = popupView.findViewById(R.id.totalCostV);
                costAdd = popupView.findViewById(R.id.costAdd);
                costRecyclerView = popupView.findViewById(R.id.costRecyclerView);

                eventHomeV.setText(model.getName());
                destinationV.setText(model.getDestin());
                deparDateV.setText(model.getDeparDate());
                returnDateV.setText(model.getReturnDate());
                budgetV.setText(model.getBudget());

                costRecyclerView.setLayoutManager(new LinearLayoutManager(popupView.getContext()));

                final SumCalculator calculator = new SumCalculator();

                final List<Budget> budgets = new ArrayList<>();
                budgetReference.child(model.getUid()).child(model.getEventId()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Budget budget = dataSnapshot.getValue(Budget.class);
                        budgets.add(budget);

                        double oBudget = Double.parseDouble(String.valueOf(budgetV.getText()));
                        double sum = calculator.getBudgetSum(budgets) + oBudget;
                        budgetV.setText(""+sum);

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

                final List<Cost> costList = new ArrayList<>();
                costReference.child(model.getUid()).child(model.getEventId()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Cost cost = dataSnapshot.getValue(Cost.class);
                        costList.add(cost);

                        CostRecyclerAdapter adapter = new CostRecyclerAdapter(popupView.getContext(), costList);
                        adapter.notifyDataSetChanged();
                        costRecyclerView.setAdapter(adapter);

                        double sum = calculator.getCostSum(costList);
                        totalCostV.setText("Total Cost : "+sum);

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

                budgetAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setContentView(R.layout.add_amount);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView title = dialog.findViewById(R.id.titleAddAmount);
                        final TextInputLayout description = dialog.findViewById(R.id.description);
                        final TextInputLayout amount = dialog.findViewById(R.id.amount);
                        CardView cancel, add;
                        cancel = dialog.findViewById(R.id.cancel);
                        add = dialog.findViewById(R.id.add);

                        title.setText("Budget Add");

                        final String createdDate = new SimpleDateFormat("MMM dd, yyyy").format(new Date());

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = budgetReference.child(model.getUid()).child(model.getEventId()).push().getKey();
                                Budget budget = new Budget(id, description.getEditText().getText().toString().trim(),
                                        createdDate, amount.getEditText().getText().toString().trim());

                                budgetReference.child(model.getUid()).child(model.getEventId()).child(id).setValue(budget);
                                dialog.dismiss();
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                });

                costAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setContentView(R.layout.add_amount);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView title = dialog.findViewById(R.id.titleAddAmount);
                        final TextInputLayout description = dialog.findViewById(R.id.description);
                        final TextInputLayout amount = dialog.findViewById(R.id.amount);
                        final Spinner spinnerCostCategory = dialog.findViewById(R.id.spinnerCostCategory);
                        spinnerCostCategory.setVisibility(View.VISIBLE);
                        CardView cancel, add;
                        cancel = dialog.findViewById(R.id.cancel);
                        add = dialog.findViewById(R.id.add);

                        title.setText("Cost Add");

                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                                R.array.cost_category, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCostCategory.setAdapter(adapter);

                        final String createdDate = new SimpleDateFormat("MMM dd, yyyy").format(new Date());

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = costReference.child(model.getUid()).child(model.getEventId()).push().getKey();
                                Cost cost = new Cost(id,description.getEditText().getText().toString().trim(), spinnerCostCategory.getSelectedItem().toString(),
                                        createdDate, amount.getEditText().getText().toString().trim());

                                costReference.child(model.getUid()).child(model.getEventId()).child(id).setValue(cost);
                                dialog.dismiss();
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                });

//                int budgetT = Integer.valueOf(budgetV.getText().toString());
//                int costT = (int) calculator.getCostSum(costList);


                int location[] = new int[2];

                v.getLocationOnScreen(location);

                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                        location[0], location[1] + v.getHeight());

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        eventHolder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.event_option_menu,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.delete_event){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirmation");
                            builder.setMessage("Are you sure, you want to Delete");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String id = model.getEventId();
                                    eventReference.child(id).removeValue();
                                    eventModelList.remove(listId);
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.create().show();
                        }


                        else if (menuItem.getItemId() == R.id.edit_event){

                            View popupView = LayoutInflater.from(context).inflate(R.layout.add_event_layout, null);
                            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            popupWindow.setFocusable(true);
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

                            eventName.getEditText().setText(model.getName());
                            startLocation.getEditText().setText(model.getStartloc());
                            destination.getEditText().setText(model.getDestin());
                            departureDate.setText(model.getDeparDate());
                            returnDate.setText(model.getReturnDate());
                            estimatedBudget.getEditText().setText(model.getBudget());

                            createEventBtn.setText("Update Event");

                            createEventBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseUser user = fAuth.getCurrentUser();
                                    String createdDate = new SimpleDateFormat("MMM dd, yyyy").format(new Date());
                                    String id = model.getEventId();
                                    final EventModel modelUpdate = new EventModel(id,user.getUid(),eventName.getEditText().getText().toString(),
                                            startLocation.getEditText().getText().toString(),
                                            destination.getEditText().getText().toString(),
                                            createdDate,
                                            departureDate.getText().toString(),
                                            returnDate.getText().toString(),
                                            estimatedBudget.getEditText().getText().toString());

                                    eventReference.child(id).setValue(modelUpdate);

                                    eventModelList.set(listId,modelUpdate);
                                    notifyDataSetChanged();

                                    popupWindow.dismiss();

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
                                            new DatePickerDialog(v.getContext(),
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
                                                departureDate.setText(dob);
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
                                            new DatePickerDialog(v.getContext(),
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
                                                returnDate.setText(dob);
                                            }
                                        };

                            });

                            int location[] = new int[2];

                            v.getLocationOnScreen(location);

                            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                                    location[0], location[1] + v.getHeight());

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });

                        }
                        else if (menuItem.getItemId() == R.id.set_home){
                            preference.setHomeEventId(model.getEventId());
                            preference.setHomeuId(model.getUid());
                        }
                        return false;
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }

    class EventHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate, option;
        CardView eventItemBtn;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameView);
            eventDate = itemView.findViewById(R.id.eventDateView);
            option = itemView.findViewById(R.id.option);
            eventItemBtn = itemView.findViewById(R.id.eventItemBtn);
        }
    }
}
