package android.disaster_safety_application.fragment;

import android.disaster_safety_application.api.OpenWeatherAPI;
import android.disaster_safety_application.layout.WeatherDetailedAdapter;
import android.disaster_safety_application.layout.WeatherFewDayAdapter;
import android.disaster_safety_application.layout.WeatherFewHourAdapter;
import android.disaster_safety_application.layout.WeatherLayout;
import android.disaster_safety_application.listener.WeatherPagerFragmentListener;
import android.disaster_safety_application.status.AppColor;
import android.disaster_safety_application.status.WeatherType;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherPagerFragment extends Fragment {

    private String area;
    private Address address;
    private View root_view;

    private final List<WeatherLayout> fewhour_LayoutList = new ArrayList<>();
    private final List<WeatherLayout> detailed_LayoutList = new ArrayList<>();
    private final List<WeatherLayout> fewday_LayoutList = new ArrayList<>();
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

        Geocoder geocoder = new Geocoder(getActivity());
        if (!area.contains("現在位置")) {
            try {
                address = geocoder.getFromLocationName(area, 10).get(0);
            } catch (IOException e) {}
        }

        if (address == null) {
            address = new Address(Locale.JAPAN);
            address.setLongitude(139.45f);
            address.setLatitude(35.41f);
        }

        //ボタングループ
        MaterialButtonToggleGroup buttonGroup = root_view.findViewById(R.id.weather_date_button_group);
        MaterialButton fewHour_button = buttonGroup.findViewById(R.id.fewhour_button);
        fewHour_button.setBackgroundColor(AppColor.CHECK_COLOR.getColorInteger());
        buttonGroup.check(R.id.fewhour_button); //初期設定は、「数時間天気」のボタンにチェックを入れる＆色を塗る
        buttonGroup.addOnButtonCheckedListener(new WeatherPagerFragmentListener(this)); //リスナーに登録

        //リスト
        fewday_LayoutList.clear();
        fewhour_LayoutList.clear();
        detailed_LayoutList.clear();

        //リストのビュー
        recyclerView = root_view.findViewById(R.id.weather_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root_view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        //初期設定
        setFewhourLayout(3, true);
    }

    public Address getAddress() {
        return address;
    }

    public List<WeatherLayout> getFewhourLayout() {
        return fewhour_LayoutList;
    }

    /**
     * 数時間天気のレイアウトを設定する関数
     * @param get_hour 何時間おきに取得するか
     */
    public void setFewhourLayout(int get_hour, boolean update) {
        if (!update && !fewhour_LayoutList.isEmpty()) {
            recyclerView.removeAllViews();
            recyclerView.setAdapter(new WeatherFewHourAdapter(fewhour_LayoutList));
            return;
        }

        for (int n = 0; n < 24; n+=get_hour) { //24時間先まで追加
            WeatherLayout weatherLayout = new WeatherLayout(
                    LocalDateTime.now(),
                    WeatherType.UNKNOWN,
                    0.0f,
                    0.0f
            );
            fewhour_LayoutList.add(weatherLayout);
        }

        new Thread(() -> { //ネットワークはメインスレッドできないので、別スレッドにする
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault()); //現在時刻
            OpenWeatherAPI openWeatherAPI = OpenWeatherAPI.getInstance(requireActivity(), address.getLatitude(), address.getLongitude()); //APIから取得

            int i = 0;
            for (OpenWeatherAPI.WeatherList weatherList : openWeatherAPI.getWeatherList()) {
                List<OpenWeatherAPI.WeatherList.Weather> weather = weatherList.getWeather();
                OpenWeatherAPI.WeatherList.Weather first_weather = weather.get(0);
                OpenWeatherAPI.WeatherList.Main main = weatherList.getMain();
                LocalDateTime api_weather_time = weatherList.getLocalDateTime();
                int diff_hour = Math.abs(api_weather_time.getHour() - localDateTime.getHour());
                int sub_hour = Math.min(diff_hour, 24 - diff_hour);

                if (sub_hour <= 1) {
                    if (i >= 24 / get_hour) {
                        break;
                    }

                    WeatherLayout weatherLayout = fewhour_LayoutList.get(i);
                    weatherLayout.setTime(api_weather_time);
                    weatherLayout.setWeatherIconID(WeatherType.getConvertType(first_weather.getDescription()).getIconID());
                    weatherLayout.setTempMax((float) main.getTempMax(false));
                    weatherLayout.setTempMin((float) main.getTempMin(false));
                    i++;
                    localDateTime = localDateTime.plusHours(get_hour);
                }
            }


            getActivity().runOnUiThread(() -> {
                recyclerView.removeAllViews();
                recyclerView.setAdapter(new WeatherFewHourAdapter(fewhour_LayoutList));
            });

        }).start();
    }

    public List<WeatherLayout> getDetailedLayout() {
        return detailed_LayoutList;
    }

    /**
     * 詳細天気のレイアウトを設定する関数
     * @param get_day 何日おきに取得するか
     */
    public void setDetailedLayout(int get_day, boolean update) {
        if (!update && !detailed_LayoutList.isEmpty()) {
            recyclerView.removeAllViews();
            recyclerView.setAdapter(new WeatherDetailedAdapter(detailed_LayoutList));
            return;
        }

        for (int n = 0; n < 5; n+=get_day) {
            WeatherLayout weatherLayout = new WeatherLayout(
                    LocalDateTime.now(),
                    WeatherType.UNKNOWN.getIconID(),
                    "",
                    0.0f,
                    0.0f
            );
            detailed_LayoutList.add(weatherLayout);
        }

        new Thread(() -> { //ネットワークはメインスレッドできないので、別スレッドにする
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault()); //現在時刻
            OpenWeatherAPI openWeatherAPI = OpenWeatherAPI.getInstance(requireActivity(), address.getLatitude(), address.getLongitude()); //APIから取得

            int i = 0;
            for (OpenWeatherAPI.WeatherList weatherList : openWeatherAPI.getWeatherList()) {
                if (i == 5) {
                    break;
                }

                List<OpenWeatherAPI.WeatherList.Weather> weather = weatherList.getWeather();
                OpenWeatherAPI.WeatherList.Weather first_weather = weather.get(0);
                OpenWeatherAPI.WeatherList.Main main = weatherList.getMain();
                LocalDateTime api_weather_time = weatherList.getLocalDateTime();

                WeatherLayout weatherLayout = detailed_LayoutList.get(i);

                if (api_weather_time.getDayOfMonth() == localDateTime.getDayOfMonth()) {
                    weatherLayout.setTime(api_weather_time);
                    weatherLayout.setWeatherIconID(WeatherType.getConvertType(first_weather.getDescription()).getIconID());
                    weatherLayout.setWeather(first_weather.getDescription());
                    weatherLayout.setTempMax((float) main.getTempMax(false));
                    weatherLayout.setTempMin((float) main.getTempMin(false));

                    localDateTime = localDateTime.plusDays(get_day);
                    i++;
                }
            }


            getActivity().runOnUiThread(() -> {
                recyclerView.removeAllViews();
                recyclerView.setAdapter(new WeatherDetailedAdapter(detailed_LayoutList));
            });

        }).start();
    }

    public List<WeatherLayout> getFewdayLayout() {
        return fewday_LayoutList;
    }

    /**
     * 数日天気のレイアウトを設定する関数
     * @param get_day 何日おきに取得するか
     */
    public void setFewdayLayout(int get_day, boolean update) {
        if (!update && !fewday_LayoutList.isEmpty()) {
            recyclerView.removeAllViews();
            recyclerView.setAdapter(new WeatherFewDayAdapter(fewday_LayoutList));
            return;
        }

        for (int n = 0; n < 5; n+=get_day) {
            WeatherLayout weatherLayout = new WeatherLayout(
                    LocalDateTime.now(),
                    WeatherType.UNKNOWN,
                    0.0f,
                    0.0f
            );
            fewday_LayoutList.add(weatherLayout);
        }

        new Thread(() -> { //ネットワークはメインスレッドできないので、別スレッドにする
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault()); //現在時刻
            OpenWeatherAPI openWeatherAPI = OpenWeatherAPI.getInstance(requireActivity(), address.getLatitude(), address.getLongitude()); //APIから取得

            int i = 0;
            for (OpenWeatherAPI.WeatherList weatherList : openWeatherAPI.getWeatherList()) {
                if (i == 5) {
                    break;
                }

                List<OpenWeatherAPI.WeatherList.Weather> weather = weatherList.getWeather();
                OpenWeatherAPI.WeatherList.Weather first_weather = weather.get(0);
                OpenWeatherAPI.WeatherList.Main main = weatherList.getMain();
                LocalDateTime api_weather_time = weatherList.getLocalDateTime();

                WeatherLayout weatherLayout = fewday_LayoutList.get(i);

                if (api_weather_time.getDayOfMonth() == localDateTime.getDayOfMonth()) {
                    weatherLayout.setTime(api_weather_time);
                    weatherLayout.setWeatherIconID(WeatherType.getConvertType(first_weather.getDescription()).getIconID());
                    weatherLayout.setTempMax((float) main.getTempMax(false));
                    weatherLayout.setTempMin((float) main.getTempMin(false));
                    localDateTime = localDateTime.plusDays(get_day);
                    i++;
                }

            }

            getActivity().runOnUiThread(() -> {
                recyclerView.removeAllViews();
                recyclerView.setAdapter(new WeatherFewDayAdapter(fewday_LayoutList));
            });

        }).start();
    }

}