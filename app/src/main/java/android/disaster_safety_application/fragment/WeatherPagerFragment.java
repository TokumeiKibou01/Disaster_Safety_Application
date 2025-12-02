package android.disaster_safety_application.fragment;

import android.disaster_safety_application.layout.WeatherDetailedAdapter;
import android.disaster_safety_application.layout.WeatherFewDayAdapter;
import android.disaster_safety_application.layout.WeatherFewHourAdapter;
import android.disaster_safety_application.layout.WeatherLayout;
import android.disaster_safety_application.listener.WeatherPagerFragmentListener;
import android.disaster_safety_application.status.AppColor;
import android.disaster_safety_application.status.WeatherType;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WeatherPagerFragment extends Fragment {

    private String area;
    private View root_view;

    private final List<WeatherLayout> fewhour_Layout = new ArrayList<>();
    private final List<WeatherLayout> detailed_Layout = new ArrayList<>();
    private final List<WeatherLayout> fewday_Layout = new ArrayList<>();
    private RecyclerView recyclerView;

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

        //リスト
        fewday_Layout.clear();
        fewhour_Layout.clear();
        detailed_Layout.clear();

        //TODO 後で消す
        //テストデータ
        fewhour_Layout.add(new WeatherLayout(LocalDateTime.now(), WeatherType.SUNNY, 30f, 10f));
        fewday_Layout.add(new WeatherLayout(LocalDateTime.now(), WeatherType.MOON, 20f, 10f));
        detailed_Layout.add(new WeatherLayout(LocalDateTime.now(),  WeatherType.SNOW.getIconID(), "雪", 15f, 10f));

        //リストのビュー
        recyclerView = root_view.findViewById(R.id.weather_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root_view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        //初期設定
        setFewhourLayout();
    }

    public List<WeatherLayout> getFewhourLayout() {
        return fewhour_Layout;
    }

    public void setFewhourLayout() {
        recyclerView.setAdapter(new WeatherFewHourAdapter(fewhour_Layout));
    }

    public List<WeatherLayout> getDetailedLayout() {
        return detailed_Layout;
    }

    public void setDetailedLayout() {
        recyclerView.setAdapter(new WeatherDetailedAdapter(detailed_Layout));
    }

    public List<WeatherLayout> getFewdayLayout() {
        return fewday_Layout;
    }

    public void setFewdayLayout() {
        recyclerView.setAdapter(new WeatherFewDayAdapter(fewday_Layout));
    }

}