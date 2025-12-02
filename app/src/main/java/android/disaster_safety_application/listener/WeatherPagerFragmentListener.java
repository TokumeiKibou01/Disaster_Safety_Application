package android.disaster_safety_application.listener;

import android.disaster_safety_application.R;
import android.disaster_safety_application.fragment.WeatherPagerFragment;
import android.disaster_safety_application.status.AppColor;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class WeatherPagerFragmentListener implements MaterialButtonToggleGroup.OnButtonCheckedListener {

    private final WeatherPagerFragment fragment;

    public WeatherPagerFragmentListener(WeatherPagerFragment fragment) {
        this.fragment = fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (group.getId() == R.id.weather_date_button_group) {
            for (int n = 0; n < group.getChildCount(); n++) {
                group.getChildAt(n).setBackgroundColor(AppColor.UNCHECK_COLOR.getColorInteger());
            }
            group.findViewById(checkedId).setBackgroundColor(AppColor.CHECK_COLOR.getColorInteger());

            if (checkedId == R.id.fewhour_button) {
                fragment.setFewhourLayout(3);
            }
            else if (checkedId == R.id.weatherDetailed_button) {
                fragment.setDetailedLayout(1);
            }
            else if (checkedId == R.id.fewday_button) {
                fragment.setFewdayLayout(1);
            }
        }
    }
}
