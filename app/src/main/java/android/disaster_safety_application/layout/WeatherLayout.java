package android.disaster_safety_application.layout;

import android.disaster_safety_application.status.WeatherType;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class WeatherLayout {

    private LocalDateTime time;
    private int weatherIcon;
    private String weather;
    private float tempMax;
    private float tempMin;

    public WeatherLayout(LocalDateTime time, WeatherType type, float tempMax, float tempMin) {
        this(time, type.getIconID(), tempMax, tempMin);
    }

    public WeatherLayout(LocalDateTime time, int weatherIcon, float tempMax, float tempMin) {
        this(time, weatherIcon, "", tempMax, tempMin);
    }

    public WeatherLayout(LocalDateTime time, int weatherIcon, String weather, float tempMax, float tempMin) {
        this.time = time;
        this.weatherIcon = weatherIcon;
        this.weather = weather;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }

    /**
     * レイアウトが持っている時間
     * @return {@link LocalDateTime} として返す
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * レイアウトが持っている時間を変更する
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    /**
     * レイアウトが持っている天気アイコンのandroid上のID
     * @return {@link Integer} として返す
     */
    public int getWeatherIconID() {
        return weatherIcon;
    }

    /**
     * レイアウトが持っている天気アイコンを変更する
     * @param id
     */
    public void setWeatherIconID(int id) {
        this.weatherIcon = id;
    }

    /**
     * レイアウトが持っている最高気温
     */
    public float getTempMax() {
        return tempMax;
    }

    /**
     * レイアウトが持っている最高気温
     */
    public void setTempMax(float max) {
        this.tempMax = max;
    }

    /**
     * レイアウトが持っている最低気温
     */
    public float getTempMin() {
        return tempMin;
    }

    /**
     * レイアウトが持っている最低気温
     */
    public void setTempMin(float min) {
        this.tempMin = min;
    }

    /**
     * レイアウトが持っている詳細天気
     * @return {@link String} として返す（詳細天気でしか使われないため、数時間天気/数日間天気ではNullを返す）
     */
    @Nullable
    public String getWeather() {
        return weather;
    }

    /**
     * レイアウトが持っている詳細天気を変更する
     */
    public void setWeather(String weather) {
        this.weather = weather;
    }

}
