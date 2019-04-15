package msr.zerone.tourhelper.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import msr.zerone.tourhelper.R;

import static msr.zerone.tourhelper.weather.TodayWeatherFragment.WEATHER_ICON_URL;

public class TodayForecastRecyclerViewAdaper extends RecyclerView.Adapter<TodayForecastRecyclerViewAdaper.TodayViewHolder> {

    private Context context;
    private List<msr.zerone.tourhelper.weather.todayforecast.List> lists;

    public TodayForecastRecyclerViewAdaper(Context context, List<msr.zerone.tourhelper.weather.todayforecast.List> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public TodayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TodayViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.forecast_item_3h_design, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodayViewHolder todayViewHolder, int i) {

        msr.zerone.tourhelper.weather.todayforecast.List list = lists.get(i);

        Picasso.get().load(WEATHER_ICON_URL+list.getWeather().get(0).getIcon()+".png")
                .into(todayViewHolder.icon);

        todayViewHolder.temp.setText(String.valueOf(list.getMain().getTemp()));
        todayViewHolder.status.setText(String.valueOf(list.getWeather().get(0).getMain()));
        String formattedDate =
                new SimpleDateFormat("h:mm a")
                        .format(new Date(list.getDt()*1000));

        todayViewHolder.time.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class TodayViewHolder extends RecyclerView.ViewHolder{
        private TextView temp, status, time;
        private ImageView icon;
        public TodayViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.temp);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
