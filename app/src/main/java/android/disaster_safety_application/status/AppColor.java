package android.disaster_safety_application.status;

import android.graphics.Color;

public enum AppColor {

    CHECK_COLOR(Color.parseColor("#408EE9")),
    UNCHECK_COLOR(Color.parseColor("#c1c3cc"));

    private final int color;

    AppColor(int color) {
        this.color = color;
    }

    public int getColorInteger() {
        return color;
    }

    public Color getColor() {
        return Color.valueOf(color);
    }
}
