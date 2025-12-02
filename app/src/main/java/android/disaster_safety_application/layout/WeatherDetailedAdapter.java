package android.disaster_safety_application.layout;

import android.disaster_safety_application.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class WeatherDetailedAdapter extends RecyclerView.Adapter<WeatherDetailedAdapter.WeatherDetailedHolder> {

    private List<WeatherLayout> detailedList;

    public WeatherDetailedAdapter(List<WeatherLayout> fewhourList) {
        this.detailedList = fewhourList;
    }

    @NonNull
    @Override
    public WeatherDetailedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_weather_detailed, parent, false);
        return new WeatherDetailedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDetailedHolder holder, int position) {
        WeatherLayout weatherLayout = detailedList.get(position);
        holder.timeText.setText(weatherLayout.getTime().getDayOfMonth() + "(" + weatherLayout.getTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPAN) + ")");
        holder.weatherText.setText(weatherLayout.getWeather());
        holder.weatherIcon.setImageResource(weatherLayout.getWeatherIconID());
        holder.tempMaxText.setText(weatherLayout.getTempMax() + "℃");
        holder.tempMinText.setText(weatherLayout.getTempMin() + "℃");
    }

    @Override
    public int getItemCount() {
        return detailedList.size();
    }

    public static class WeatherDetailedHolder extends RecyclerView.ViewHolder {
        private final TextView timeText;
        private final TextView weatherText;
        private final TextView tempMaxText, tempMinText;
        private final ImageView weatherIcon;

        public WeatherDetailedHolder (@NonNull View itemView) {
            super(itemView);
            this.timeText = itemView.findViewById(R.id.weatherDetailed_dateText);
            this.weatherText = itemView.findViewById(R.id.weatherDetailed_weatherText);
            this.weatherIcon = itemView.findViewById(R.id.weatherDetailed_weatherIcon);
            this.tempMaxText = itemView.findViewById(R.id.weatherDetailed_maxTemp);
            this.tempMinText = itemView.findViewById(R.id.weatherDetailed_minTemp);
        }
    }

}
