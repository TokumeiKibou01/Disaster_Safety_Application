package android.disaster_safety_application.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.disaster_safety_application.adapter.WeatherPagerAdapter;
import android.disaster_safety_application.listener.WeatherHomeActivityListener;
import android.disaster_safety_application.manager.JsonConfig;
import android.disaster_safety_application.manager.JsonManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.disaster_safety_application.R;
import android.os.Looper;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("MissingPermission")
public class WeatherHomeActivity extends AppCompatActivity {

    private boolean isFirstFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstFlag) {
            JsonManager json = new JsonManager(this, JsonManager.FileType.CONFIG);
            JsonObject root_obj = json.getRawElement().getAsJsonObject();
            JsonObject loc_obj = root_obj.getAsJsonObject("weather_location");
            TabLayout tabLayout = findViewById(R.id.weather_location_tabLayout);
            for (int n = tabLayout.getTabCount() - 1; n >= 1; n--) {
                tabLayout.removeTabAt(n);
            }
            for (Map.Entry<String, JsonElement> entry : loc_obj.entrySet()) {
                String key = entry.getKey();
                //JsonObject value = entry.getValue().getAsJsonObject();
                TabLayout.Tab newTab = tabLayout.newTab();
                newTab.setText(key);
                tabLayout.addTab(newTab);
            }
        }
        isFirstFlag = false;
    }

    private void initActivity() {
        WeatherHomeActivityListener weatherHomeActivityListener = new WeatherHomeActivityListener(this);
        ViewPager2 viewPager = findViewById(R.id.weather_viewPager);
        TabLayout tabLayout = findViewById(R.id.weather_location_tabLayout);
        MaterialButtonToggleGroup materialButtonToggleGroup = findViewById(R.id.home_bottom_button_group);
        materialButtonToggleGroup.addOnButtonCheckedListener(weatherHomeActivityListener);
        tabLayout.addOnTabSelectedListener(weatherHomeActivityListener);

        WeatherPagerAdapter adapter = new WeatherPagerAdapter(this);
        viewPager.setAdapter(adapter);
        JsonConfig config = (JsonConfig) new JsonManager(WeatherHomeActivity.this, JsonManager.FileType.CONFIG).getDeserializedInstance();
        Map<String, JsonConfig.WeatherLocation> weatherLocationMap = config.getWeatherLocationMap();
        ArrayList<String> area_list = new ArrayList<>(weatherLocationMap.keySet());

        updateNowLocationTab();

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0) {
                            tab.setText("現在位置\n");
                        }
                        else {
                            tab.setText(area_list.get(position - 1)); //「現在位置のタブ」分を飛ばすため、マイナス1する
                        }
                    }
                }).attach();
    }

    public void updateNowLocationTab() {
        TabLayout tabLayout = findViewById(R.id.weather_location_tabLayout);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        TabLayout.Tab now_location_tab = tabLayout.getTabAt(0);
        now_location_tab.setText("現在位置\n取得中");
        final Address[] address = {null};
        fusedLocationClient.requestLocationUpdates(getLocationRequest(),  new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addresse_list = geocoder.getFromLocation(
                                location.getLatitude(),
                                location.getLongitude(),
                                1
                        );
                        address[0] = addresse_list.get(0);
                    } catch (IOException e) {
                        address[0] = null;
                    }

                }
            }
        }, Looper.getMainLooper());

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                WeatherHomeActivity.this.runOnUiThread(() -> {
                    if (address[0] == null) {
                        now_location_tab.setText("現在位置\n取得失敗");
                    }
                    else {
                        now_location_tab.setText("現在位置\n" + address[0].getAdminArea());
                    }
                });
            }
        };
        timer.schedule(task, 1000*10L);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private LocationRequest getLocationRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1000);
        }

        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
                .setMaxUpdates(1)
                .build();
    }

}