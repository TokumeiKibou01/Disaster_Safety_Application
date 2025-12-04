package android.disaster_safety_application.activity;

import android.disaster_safety_application.adapter.WeatherPagerAdapter;
import android.disaster_safety_application.listener.WeatherHomeActivityListener;
import android.disaster_safety_application.manager.JsonConfig;
import android.disaster_safety_application.manager.JsonManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.disaster_safety_application.R;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

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
        ViewPager2 viewPager = findViewById(R.id.weather_viewPager);
        TabLayout tabLayout = findViewById(R.id.weather_location_tabLayout);
        MaterialButtonToggleGroup materialButtonToggleGroup = findViewById(R.id.home_bottom_button_group);
        materialButtonToggleGroup.addOnButtonCheckedListener(new WeatherHomeActivityListener());

        WeatherPagerAdapter adapter = new WeatherPagerAdapter(this);
        viewPager.setAdapter(adapter);
        JsonConfig config = (JsonConfig) new JsonManager(WeatherHomeActivity.this, JsonManager.FileType.CONFIG).getDeserializedInstance();
        Map<String, JsonConfig.WeatherLocation> weatherLocationMap = config.getWeatherLocationMap();
        ArrayList<String> area_list = new ArrayList<>(weatherLocationMap.keySet());

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
}