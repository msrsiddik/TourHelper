package msr.zerone.tourhelper.eventfragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.eventfragment.model.EventModel;

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
    public void onBindViewHolder(@NonNull EventHolder eventHolder, int i) {
        EventModel model = eventModelList.get(i);

        eventHolder.eventName.setText(model.getName());
        eventHolder.eventDate.setText(model.getDeparDate());
    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }

    class EventHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameView);
            eventDate = itemView.findViewById(R.id.eventDateView);
        }
    }
}
