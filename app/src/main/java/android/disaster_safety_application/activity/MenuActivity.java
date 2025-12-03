package android.disaster_safety_application.activity;

import android.disaster_safety_application.listener.MenuActivityListener;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.disaster_safety_application.R;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initLibrary();
    }

    private void initLibrary() {
        MenuActivityListener menuListener = new MenuActivityListener(this);
        MaterialButton apiKey_button = findViewById(R.id.apikey_settingButton);
        apiKey_button.setOnClickListener(menuListener);
        ImageButton backButton = findViewById(R.id.menu_backHomeButton);
        backButton.setOnClickListener(menuListener);
    }
}