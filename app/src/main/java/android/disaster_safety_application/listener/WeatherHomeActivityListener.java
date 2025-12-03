package android.disaster_safety_application.listener;

import android.app.Activity;
import android.content.Intent;
import android.disaster_safety_application.R;
import android.disaster_safety_application.activity.MenuActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class WeatherHomeActivityListener implements MaterialButtonToggleGroup.OnButtonCheckedListener {

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (group.getId() == R.id.home_bottom_button_group) {
            if (checkedId == R.id.menu_button) {
                Intent intent = new Intent(group.getContext(), MenuActivity.class);
                group.getContext().startActivity(intent);
            }
            else if (checkedId == R.id.search_button) {

            }
        }
    }

}
