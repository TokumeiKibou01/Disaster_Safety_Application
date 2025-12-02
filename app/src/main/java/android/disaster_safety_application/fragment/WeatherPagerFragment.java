package android.disaster_safety_application.fragment;

import android.disaster_safety_application.listener.WeatherPagerFragmentListener;
import android.disaster_safety_application.status.AppColor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.disaster_safety_application.R;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class WeatherPagerFragment extends Fragment {

    private String area;
    private View root_view;

    public WeatherPagerFragment() {}

    public WeatherPagerFragment(String area) {
        this.area = area;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.root_view = view;
        initActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_pager, container, false);
    }

    private void initActivity() {
        TextView test_text = root_view.findViewById(R.id.text);
        test_text.setText(area);

        //ボタングループ
        MaterialButtonToggleGroup buttonGroup = root_view.findViewById(R.id.weather_date_button_group);
        MaterialButton fewHour_button = buttonGroup.findViewById(R.id.fewhour_button);
        fewHour_button.setBackgroundColor(AppColor.CHECK_COLOR.getColorInteger());
        buttonGroup.check(R.id.fewhour_button); //初期設定は、「数時間天気」のボタンにチェックを入れる＆色を塗る
        buttonGroup.addOnButtonCheckedListener(new WeatherPagerFragmentListener(this)); //リスナーに登録
    }
}