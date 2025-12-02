package android.disaster_safety_application.status;

import android.disaster_safety_application.R;

public enum WeatherType {

    SUNNY(R.drawable.mark_tenki_hare), //晴れ
    MOON(R.drawable.mark_tenki_moon), //晴れ（※夜の場合はこれを指定する）
    CLOUDY(R.drawable.mark_tenki_kumori), //曇り
    RAINY(R.drawable.mark_tenki_umbrella), //雨
    SNOW(R.drawable.tenki_snow), //雪
    THUNDER(R.drawable.mark_tenkiu_kaminari), //雷
    UNKNOWN(R.drawable.ic_empty_icon); //不明（透明なアイコンが指定される）

    private int icon_id;

    WeatherType(int icon_id) {
        this.icon_id = icon_id;
    }

    public int getIconID() {
        return icon_id;
    }

    public static WeatherType getConvertType(String name) {
        if (name.contains("晴")) {
            return SUNNY;
        }
        else if (name.contains("曇") || name.contains("雲")) {
            return CLOUDY;
        }
        else if (name.contains("雨")) {
            return RAINY;
        }
        else if (name.contains("雪")) {
            return SNOW;
        }
        else if (name.contains("雷")) {
            return THUNDER;
        }

        return UNKNOWN;
    }

}
