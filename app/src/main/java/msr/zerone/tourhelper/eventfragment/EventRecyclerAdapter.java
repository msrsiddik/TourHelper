package msr.zerone.tourhelper.eventfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.eventfragment.model.EventModel;

import static msr.zerone.tourhelper.THfirebase.eventReference;
import static msr.zerone.tourhelper.THfirebase.fAuth;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventHolder> {

    private Context context;
    private List<EventModel> eventModelList;

    public EventRecyclerAdapter(Context context, List<EventModel> eventModelList) {
        this.context = context;
        this.eventModelList = eventModelList;
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
        eventHolder.eventDate.setText(model.getDeparDate());
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
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameView);
            eventDate = itemView.findViewById(R.id.eventDateView);
            option = itemView.findViewById(R.id.option);
        }
    }
}
