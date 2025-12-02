package android.disaster_safety_application.listener;

import android.disaster_safety_application.R;
import android.disaster_safety_application.fragment.WeatherPagerFragment;
import android.disaster_safety_application.status.AppColor;
import android.graphics.Color;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class WeatherPagerFragmentListener implements MaterialButtonToggleGroup.OnButtonCheckedListener {

    private final WeatherPagerFragment fragment;

    public WeatherPagerFragmentListener(WeatherPagerFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (group.getId() == R.id.weather_date_button_group) {
            for (int n = 0; n < group.getChildCount(); n++) {
                group.getChildAt(n).setBackgroundColor(AppColor.UNCHECK_COLOR.getColorInteger());
            }
            group.findViewById(checkedId).setBackgroundColor(AppColor.CHECK_COLOR.getColorInteger());

            if (checkedId == R.id.fewhour_button) {

            }
            else if (checkedId == R.id.weatherDetailed_button) {

            }
            else if (checkedId == R.id.fewday_button) {

            }
        }
    }
}
