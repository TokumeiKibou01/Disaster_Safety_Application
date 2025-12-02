package android.disaster_safety_application.adapter;

import android.disaster_safety_application.fragment.WeatherPagerFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WeatherPagerAdapter extends FragmentStateAdapter {

    private String[] pages = {"北海道", "沖縄", "鹿児島"};

    public WeatherPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new WeatherPagerFragment(pages[position]);
    }

    @Override
    public int getItemCount() {
        return pages.length;
    }

}
