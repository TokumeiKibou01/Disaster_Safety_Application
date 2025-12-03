package android.disaster_safety_application.listener;

import android.disaster_safety_application.R;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class WeatherHomeActivityListener implements MaterialButtonToggleGroup.OnButtonCheckedListener {

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (group.getId() == R.id.home_bottom_button_group) {
            if (group.getId() == R.id.menu_button) {

            }
            else if (group.getId() == R.id.search_button) {

            }
        }
    }

}
