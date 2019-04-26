package msr.zerone.tourhelper.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.map.nearby.Result;

public class NearbyRecyclerViewAdapter extends RecyclerView.Adapter<NearbyRecyclerViewAdapter.NearbyViewHolder> {
    private Context context;
    private List<Result> resultList;

    public NearbyRecyclerViewAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NearbyViewHolder(LayoutInflater.from(context).inflate(R.layout.place_item_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder nearbyViewHolder, int i) {
        Result result = resultList.get(i);
        nearbyViewHolder.placeName.setText(result.getName());
        nearbyViewHolder.vicinity.setText(result.getVicinity());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class NearbyViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, vicinity;
        public NearbyViewHolder(@NonNull View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.placeName);
            vicinity = itemView.findViewById(R.id.vicinity);
        }
    }
}
