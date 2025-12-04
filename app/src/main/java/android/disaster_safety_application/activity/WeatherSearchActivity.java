package android.disaster_safety_application.activity;

import android.disaster_safety_application.listener.WeatherSearchListener;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.disaster_safety_application.R;

public class WeatherSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initLibrary();
    }

    private void initLibrary() {
        WeatherSearchListener listener = new WeatherSearchListener(this);
        findViewById(R.id.weather_search_editText).setOnKeyListener(listener);
    }
}