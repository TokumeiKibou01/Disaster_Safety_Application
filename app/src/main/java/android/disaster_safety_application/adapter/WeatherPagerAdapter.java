package android.disaster_safety_application.adapter;

import android.disaster_safety_application.fragment.WeatherPagerFragment;
import android.disaster_safety_application.manager.JsonConfig;
import android.disaster_safety_application.manager.JsonManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Map;

public class WeatherPagerAdapter extends FragmentStateAdapter {

    private final Map<String, JsonConfig.WeatherLocation> weatherLocationMap;

    public WeatherPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        JsonConfig config = (JsonConfig) new JsonManager(fragmentActivity, JsonManager.FileType.CONFIG).getDeserializedInstance();
        this.weatherLocationMap = config.getWeatherLocationMap();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new WeatherPagerFragment("現在位置\n");
        }
        else {
            ArrayList<String> area_list = new ArrayList<>(weatherLocationMap.keySet());
            return new WeatherPagerFragment(area_list.get(position - 1)); //「現在位置のタブ」分を飛ばすため、マイナス1する
        }
    }

    @Override
    public int getItemCount() {
        return weatherLocationMap.size() + 1; //「現在位置のタブ」分をプラス1とする
    }

}
