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

public class WeatherFewHourAdapter extends RecyclerView.Adapter<WeatherFewHourAdapter.FewHourHolder> {

    private List<WeatherLayout> fewhourList;

    /**
     * 数時間天気のアダプタークラス
     * @param fewhourList 数時間天気のレイアウトにしたいものを {@link List<WeatherLayout>} として引数に入れる
     */
    public WeatherFewHourAdapter(List<WeatherLayout> fewhourList) {
        this.fewhourList = fewhourList;
    }

    @NonNull
    @Override
    public FewHourHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_weather_fewhour, parent, false);
        return new FewHourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FewHourHolder holder, int position) {
        WeatherLayout weatherLayout = fewhourList.get(position);
        holder.timeText.setText(String.valueOf(weatherLayout.getTime().getHour())); //時間（例：3）
        holder.weatherIcon.setImageResource(weatherLayout.getWeatherIconID()); //お天気アイコン
        holder.tempMaxText.setText(weatherLayout.getTempMax() + "℃"); //最高気温（例：20℃）
        holder.tempMinText.setText(weatherLayout.getTempMin() + "℃"); //最低気温（例：10℃）
    }

    @Override
    public int getItemCount() {
        return fewhourList.size();
    }

    public static class FewHourHolder extends RecyclerView.ViewHolder {
        private final TextView timeText;
        private final TextView tempMaxText, tempMinText;
        private final ImageView weatherIcon;

        public FewHourHolder(@NonNull View itemView) {
            super(itemView);
            this.timeText = itemView.findViewById(R.id.fewhour_datetext);
            this.weatherIcon = itemView.findViewById(R.id.fewhour_weatherIcon);
            this.tempMaxText = itemView.findViewById(R.id.fewhour_maxTemp);
            this.tempMinText = itemView.findViewById(R.id.fewhour_minTemp);
        }
    }

}
