package android.disaster_safety_application.listener;

import android.app.Activity;
import android.content.Intent;
import android.disaster_safety_application.R;
import android.disaster_safety_application.activity.MenuActivity;
import android.disaster_safety_application.activity.WeatherHomeActivity;
import android.disaster_safety_application.activity.WeatherSearchActivity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherHomeActivityListener implements MaterialButtonToggleGroup.OnButtonCheckedListener, TabLayout.OnTabSelectedListener {

    private WeatherHomeActivity activity;

    public WeatherHomeActivityListener(WeatherHomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (group.getId() == R.id.home_bottom_button_group) {
            if (checkedId == R.id.menu_button) {
                Intent intent = new Intent(group.getContext(), MenuActivity.class);
                group.getContext().startActivity(intent);
            }
            else if (checkedId == R.id.search_button) {
                Intent intent = new Intent(group.getContext(), WeatherSearchActivity.class);
                group.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            activity.updateNowLocationTab();
        }
    }
}
