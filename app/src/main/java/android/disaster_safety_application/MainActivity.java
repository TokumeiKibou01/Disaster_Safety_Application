package android.disaster_safety_application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.disaster_safety_application.activity.WeatherHomeActivity;
import android.disaster_safety_application.adapter.WeatherPagerAdapter;
import android.disaster_safety_application.api.OpenWeatherAPI;
import android.disaster_safety_application.manager.JsonManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

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
    }

    private void initLibrary() {
        Arrays.stream(JsonManager.FileType.values()).forEach(type -> new JsonManager(this, type).createJson());

        new Thread(() -> {
            if (OpenWeatherAPI.getInstance(this, 35, 160) == null) {
                SharedPreferences sharedPref = getSharedPreferences("Main", Context.MODE_PRIVATE);
                EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                runOnUiThread(() -> {
                    new AlertDialog.Builder(this)
                            .setTitle("APIキーを再設定してください")
                            .setView(editText)
                            .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    sharedPref.edit().putString("openweather_apikey", editText.getEditableText().toString()).apply();
                                    Intent intent = new Intent(editText.getContext(), WeatherHomeActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    Intent intent = new Intent(editText.getContext(), WeatherHomeActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                });

            }
            else {
                Intent intent = new Intent(this, WeatherHomeActivity.class);
                startActivity(intent);
            }
        }).start();
    }
}