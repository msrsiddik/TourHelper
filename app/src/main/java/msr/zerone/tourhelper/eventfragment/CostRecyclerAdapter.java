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
import msr.zerone.tourhelper.eventfragment.model.Cost;

public class CostRecyclerAdapter extends RecyclerView.Adapter<CostRecyclerAdapter.CostViewHolder> {
    private Context context;
    private List<Cost> costList;

    public CostRecyclerAdapter(Context context, List<Cost> costList) {
        this.context = context;
        this.costList = costList;
    }

    @NonNull
    @Override
    public CostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CostViewHolder(LayoutInflater.from(context).inflate(R.layout.cost_row_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CostViewHolder costViewHolder, int i) {
        Cost cost = costList.get(i);

        costViewHolder.categoryVcost.setText(cost.getFoss());
        costViewHolder.dateVcost.setText(cost.getDate());
        costViewHolder.amountVcost.setText(cost.getAmount());
    }

    @Override
    public int getItemCount() {
        return costList.size();
    }

    class CostViewHolder extends RecyclerView.ViewHolder {
        TextView categoryVcost, dateVcost, amountVcost;
        public CostViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryVcost = itemView.findViewById(R.id.categoryVcost);
            dateVcost = itemView.findViewById(R.id.dateVcost);
            amountVcost = itemView.findViewById(R.id.amountVcost);
        }
    }
}
