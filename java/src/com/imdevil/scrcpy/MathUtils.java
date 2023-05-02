package com.imdevil.scrcpy;

import java.awt.*;

public class MathUtils {

    public static int clamp(int min, int max, int value) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public static String toString(Point point) {
        if (point == null) return null;
        return "[" + point.x + ", " + point.y + "]";
    }
}
