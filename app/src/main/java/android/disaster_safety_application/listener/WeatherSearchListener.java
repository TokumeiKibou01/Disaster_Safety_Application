package android.disaster_safety_application.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.disaster_safety_application.R;
import android.disaster_safety_application.manager.JsonManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WeatherSearchListener implements View.OnKeyListener {

    private final Activity activity;

    public WeatherSearchListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (view.getId() == R.id.weather_search_editText) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                EditText edit_text = activity.findViewById(R.id.weather_search_editText);
                String text = edit_text.getText().toString();
                if (text.isEmpty()) {
                    Toast toast = new Toast(activity);
                    toast.setText("文字を入れてください");
                    toast.show();
                    return false;
                }

                Geocoder geocoder = new Geocoder(activity);
                try {
                    List<Address> address_list = geocoder.getFromLocationName(edit_text.getText().toString(), 20);
                    for (int n = 0; n < address_list.size(); n++) {
                        Address address = address_list.get(n);
                        createLocationButton(address);
                    }
                } catch (IOException e) {

                }
            }
        }
        return false;
    }

    private void createLocationButton(Address address) {
        LinearLayout result_layout = activity.findViewById(R.id.weather_search_resultLayout);
        result_layout.removeAllViews();

        FrameLayout frame = new FrameLayout(activity);
        LinearLayout.LayoutParams linear_pram = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear_pram.setMargins(0, 5, 0, 3);
        frame.setLayoutParams(linear_pram);
        frame.setBackgroundColor(Color.parseColor("#FFFFFF"));

        MaterialButton materialButton = new MaterialButton(activity);
        materialButton.setTag(address);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Address address = (Address) view.getTag();
                AlertDialog builder = new AlertDialog.Builder(activity)
                        .setMessage("登録しますか？")
                        .setPositiveButton("はい", (dialogInterface, i) -> {
                            JsonManager json = new JsonManager(activity, JsonManager.FileType.CONFIG);
                            JsonObject root_obj = json.getRawElement().getAsJsonObject();
                            JsonObject loc_obj = root_obj.getAsJsonObject("weather_location");
                            JsonObject new_loc = new JsonObject();
                            new_loc.addProperty("latitude", address.getLatitude());
                            new_loc.addProperty("longitude", address.getLongitude());
                            loc_obj.add(address.getFeatureName(), new_loc);
                            json.updateJson();
                            activity.finish();
                        })
                        .setNegativeButton("いいえ", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                        }).create();
                builder.show();
            }
        });
        materialButton.setBackgroundColor(Color.WHITE);
        materialButton.setRippleColor(ColorStateList.valueOf(Color.LTGRAY));
        materialButton.setStateListAnimator(null);
        materialButton.setCornerRadius(0);
        frame.addView(materialButton);

        EditText edit_text = activity.findViewById(R.id.weather_search_editText);
        TextView name_text = new TextView(activity);
        name_text.setTextSize(30);
        name_text.setText(edit_text.getText().toString());
        TextView location_text = new TextView(activity);
        location_text.setTextSize(20);
        location_text.setText("（緯度" + address.getLatitude() + ",経度：" + address.getLongitude() + ")");

        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(name_text);
        linearLayout.addView(location_text);
        frame.addView(linearLayout);

        result_layout.addView(frame);
    }

}
