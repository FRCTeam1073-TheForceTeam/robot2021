package frc.robot;

public class Utility {
    /**
     * sets raw axis value inside the deadzone to zero
     * @param rawAxisValue
     * @return deadzoned axisValue
     */
    public static double deadzone(double rawAxisValue, double deadzone) {
        if (Math.abs(rawAxisValue) < deadzone) {
            return 0;
        } else {
            return (Math.signum(rawAxisValue) / (1 - deadzone)) * (Math.abs(rawAxisValue) - deadzone);
        }
    }

    public static double deadzone(double rawAxisValue) {
        return deadzone(rawAxisValue, Constants.CONTROLLER_DEADZONE);
    }

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
