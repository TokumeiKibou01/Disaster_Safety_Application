package android.disaster_safety_application.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.disaster_safety_application.R;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

public class MenuActivityListener implements View.OnClickListener {

    private final Activity activity;

    public MenuActivityListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.menu_backHomeButton) {
            activity.finish();
        }
        else if (view.getId() == R.id.apikey_settingButton) {
            SharedPreferences sharedPref = activity.getSharedPreferences("Main", Context.MODE_PRIVATE);
            String apiKey = sharedPref.getString("openweather_apikey", null);

            EditText editText = new EditText(view.getContext());
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setText(apiKey == null ? "" : apiKey);

            new AlertDialog.Builder(view.getContext())
                    .setView(editText)
                    .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sharedPref.edit().putString("openweather_apikey", editText.getEditableText().toString()).apply();
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show();
        }
    }

}
