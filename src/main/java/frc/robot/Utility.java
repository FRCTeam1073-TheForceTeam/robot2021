package frc.robot;

public class Utility {

    /**Clips values to [min,max] */
    public static double clipRange(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }

    /**Clips values to [0,1] */
    public static double clipRange(double value) {
        return clipRange(value, 0, 1);
    }

    /**Clips values to [-dist,dist] */
    public static double clipRange(double value, double dist) {
        return clipRange(value, -dist, dist);
    }
}
