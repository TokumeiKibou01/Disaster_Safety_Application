package android.disaster_safety_application.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.disaster_safety_application.manager.JsonConfig;
import android.disaster_safety_application.manager.JsonManager;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class OpenWeatherAPI {

    private static final double KELVIN = 273.15;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @SerializedName("list")
    List<WeatherList> weatherList;

    public static OpenWeatherAPI getInstance(Activity activity, double lat, double lon) {
        try {
            SharedPreferences sharedPref = activity.getSharedPreferences("Main", Context.MODE_PRIVATE);
            String apiKey = sharedPref.getString("openweather_apikey", null);

            URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&lang=ja");
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();
            return new Gson().fromJson(content.toString(), OpenWeatherAPI.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 各時間の天気予報を取得する
     * @return {@link List<WeatherList>} で返す
     */
    public List<WeatherList> getWeatherList() {
        return weatherList;
    }

    /**
     * 天気予報のクラス
     */
    public static class WeatherList {
        Main main;
        List<Weather> weather;
        String dt_txt;

        /**
         * 気温などの数字の情報を取得する関数
         * @return {@link Main} として返す
         */
        public Main getMain() {
            return main;
        }

        /**
         * 天気を取得する関数
         * @return {@link List<Weather>} として返す
         * @apiNote 基本的には0番目を使用する
         */
        public List<Weather> getWeather() {
            return weather;
        }

        /**
         * この天気予報の時間を返す
         * @return {@link LocalDateTime} で返す
         */
        public LocalDateTime getLocalDateTime() {
            SimpleDateFormat raw_format = new SimpleDateFormat(DATE_FORMAT);
            try {
                Date date = raw_format.parse(dt_txt);
                return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public static class Main {

            //気温
            private float temp_min; //最低気温
            private float temp_max; //最高気温
            private float humidity; //湿度

            //気圧
            private float pressure; //気圧
            private float sea_level; //海面気圧
            private float grnd_level; //地点気圧

            /**
             * 最低気温を取得する関数
             * @param raw_data　生データにするかどうか
             * @return 気温を返す（生データの場合、絶対温度[K]として返し、じゃない場合はセルシウス[℃、-273.15]として返す）
             */
            public double getTempMin(boolean raw_data) {
                if (raw_data) {
                    return temp_min;
                }
                else {
                    return (new BigDecimal(temp_min)
                            .subtract(new BigDecimal(KELVIN))
                            .setScale(1, RoundingMode.HALF_DOWN)
                            .doubleValue());
                }
            }

            /**
             * 最高気温を取得する関数
             * @param raw_data　生データにするかどうか
             * @return 気温を返す（生データの場合、絶対温度[K]として返し、じゃない場合はセルシウス[℃、-273.15]として返す）
             */
            public double getTempMax(boolean raw_data) {
                if (raw_data) {
                    return temp_max;
                }
                else {
                    return (new BigDecimal(temp_max)
                            .subtract(new BigDecimal(KELVIN))
                            .setScale(1, RoundingMode.HALF_DOWN)
                            .doubleValue());
                }
            }

            /**
             * 湿度を返す関数
             */
            public double getHumidity() {
                return humidity;
            }

            /**
             * 気圧を返す関数
             */
            public double getPressure() {
                return pressure;
            }

            /**
             * 海面気圧を返す関数
             */
            public double getSeaLevel() {
                return sea_level;
            }

            /**
             * 地点気圧を返す関数
             */
            public double getGrndLevel() {
                return grnd_level;
            }
        }

        public static class Weather {
            String main;
            String description;

            /**
             * 天気を取得する関数
             * @apiNote {@link #getDescription()} を推奨（英語表記のため）。
             */
            public String getMain() {
                return main;
            }

            /**
             * 詳細天気を取得する関数
             * @return 天気を返す（例：曇りがち、厚い雲　など）
             */
            public String getDescription() {
                return description;
            }
        }
    }

}
