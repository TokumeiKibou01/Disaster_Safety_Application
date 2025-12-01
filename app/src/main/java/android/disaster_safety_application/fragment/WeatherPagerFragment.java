package android.disaster_safety_application.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.disaster_safety_application.R;
import android.widget.TextView;

public class WeatherPagerFragment extends Fragment {

    private String text;

    public WeatherPagerFragment() {}

    public WeatherPagerFragment(String text) {
        this.text = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView test_text = view.findViewById(R.id.text);
        test_text.setText(text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_pager, container, false);
    }
}