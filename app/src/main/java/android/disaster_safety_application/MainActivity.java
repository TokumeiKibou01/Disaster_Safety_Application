package android.disaster_safety_application;

import android.content.Intent;
import android.disaster_safety_application.activity.WeatherHomeActivity;
import android.disaster_safety_application.adapter.WeatherPagerAdapter;
import android.disaster_safety_application.manager.JsonManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initLibrary();

        Intent intent = new Intent(this, WeatherHomeActivity.class);
        startActivity(intent);
    }

    private void initLibrary() {
        Arrays.stream(JsonManager.FileType.values()).forEach(type -> new JsonManager(this, type).createJson());
    }
}