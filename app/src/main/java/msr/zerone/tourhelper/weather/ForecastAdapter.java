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

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private Context context;
    private List<msr.zerone.tourhelper.weather.forecastpojo.List> lists;

    public ForecastAdapter(Context context, List<msr.zerone.tourhelper.weather.forecastpojo.List> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ForecastViewHolder(LayoutInflater.from(context).inflate(R.layout.forecast_row_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder forecastViewHolder, int i) {
        msr.zerone.tourhelper.weather.forecastpojo.List list = lists.get(i);
        forecastViewHolder.date.setText(new SimpleDateFormat("h:mm a").format(new Date(list.getDt()*1000)));
        Picasso.get().load(WEATHER_ICON_URL+list.getWeather().get(0).getIcon()+".png").into(forecastViewHolder.imageView);
        forecastViewHolder.temp.setText(""+list.getMain().getTemp());
        forecastViewHolder.main.setText(list.getWeather().get(0).getMain());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

     class ForecastViewHolder extends RecyclerView.ViewHolder{
        private TextView date, temp, main;
        private ImageView imageView;
        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.imageview);
            temp = itemView.findViewById(R.id.temp);
            main = itemView.findViewById(R.id.main);
        }
    }
}
