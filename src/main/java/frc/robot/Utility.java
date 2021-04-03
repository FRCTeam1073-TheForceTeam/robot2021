package frc.robot;

import frc.robot.components.WaypointPath;
import frc.robot.components.WaypointPath.Waypoint;

public class Utility {
    /**
     * sets raw axis value inside the deadzone to zero
     * 
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

    static class PathBuilder {
        public enum PathIndex {
            BARREL, SLALOM, BOUNCE, TEST
        };
        public static WaypointPath getPath(PathIndex index){
            WaypointPath path = new WaypointPath();
            switch(index){
            case BARREL:
                addPoint()
            }
            return path;
        }
    }

}
