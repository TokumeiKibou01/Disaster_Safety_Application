package android.disaster_safety_application.layout;

import android.disaster_safety_application.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherFewDayAdapter extends RecyclerView.Adapter<WeatherFewDayAdapter.FewDayHolder> {

    private List<WeatherLayout> fewdayList;

    public WeatherFewDayAdapter(List<WeatherLayout> fewdayList) {
        this.fewdayList = fewdayList;
    }

    @NonNull
    @Override
    public FewDayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_weather_fewday, parent, false);
        return new FewDayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FewDayHolder holder, int position) {
        WeatherLayout weatherLayout = fewdayList.get(position);
        holder.timeText.setText(weatherLayout.getTime().getDayOfMonth() + "日");
        holder.weatherIcon.setImageResource(weatherLayout.getWeatherIconID());
        holder.tempMaxText.setText(weatherLayout.getTempMax() + "℃");
        holder.tempMinText.setText(weatherLayout.getTempMin() + "℃");
    }

    @Override
    public int getItemCount() {
        return fewdayList.size();
    }

    public static class FewDayHolder extends RecyclerView.ViewHolder {
        private final TextView timeText;
        private final TextView tempMaxText, tempMinText;
        private final ImageView weatherIcon;

        public FewDayHolder(@NonNull View itemView) {
            super(itemView);
            this.timeText = itemView.findViewById(R.id.fewday_datetext);
            this.weatherIcon = itemView.findViewById(R.id.fewday_weatherIcon);
            this.tempMaxText = itemView.findViewById(R.id.fewday_maxTemp);
            this.tempMinText = itemView.findViewById(R.id.fewday_minTemp);
        }
    }

}
