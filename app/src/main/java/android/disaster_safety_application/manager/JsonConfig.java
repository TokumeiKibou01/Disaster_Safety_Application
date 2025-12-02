package android.disaster_safety_application.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JsonConfig implements JsonManager.DeserializedJson {

    private Map<String, WeatherLocation> weather_location;

    public JsonConfig() {}

    public Map<String, WeatherLocation> getWeatherLocationMap() {
        return Objects.requireNonNullElseGet(
                weather_location,
                () -> Map.of("東京", new WeatherLocation(35.41f, 139.45f))
        );
    }

    public static class WeatherLocation {
        private float latitude; //緯度
        private float longitude; //経度

        public WeatherLocation() {}

        public WeatherLocation(float latitude, float longitude) {}

        public float getLatitude() {
            return latitude;
        }

        public float getLongitude() {
            return longitude;
        }
    }

}

