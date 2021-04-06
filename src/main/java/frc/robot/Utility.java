package frc.robot;

import frc.robot.components.WaypointPath;
// import frc.robot.components.WaypointPath.Waypoint;
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
            BARREL_1, BARREL_2, BARREL_3, BARREL_4, SLALOM, BOUNCE, TEST
        };
        public static void addPoints(WaypointPath path, Waypoint... points){
            for (Waypoint pt : points) {
                path.addWaypoint(pt);
            }
        }
        public static WaypointPath getPath(PathIndex index){
            WaypointPath path = new WaypointPath();
            double tolerance = 0.08;
            switch(index){
                case BARREL_1:
                    addPoints(path,
                        new Waypoint(1.407,0,0.5,0.09),
                        new Waypoint(2.363,0.114,0.5,0.09),
                        new Waypoint(3.276,0.145,0.5,0.09),
                        new Waypoint(3.793,-0.288,0.5,0.09),
                        new Waypoint(3.865,-0.530,0.5,0.09),
                        new Waypoint(3.853,-1.047,0.5,0.09),
                        new Waypoint(3.688,-1.444,0.5,0.09),
                        new Waypoint(3.390,-1.716,0.5,0.09),
                        new Waypoint(2.833,-1.810,0.5,0.09),
                        new Waypoint(2.455,-1.800,0.5,0.09),
                        new Waypoint(2.114,-1.507,0.5,0.09)
                    );
                    break;
                case BARREL_2:
                    addPoints(path,
                        new Waypoint(0.39, -0.05, 0.5, 0.09),
                        new Waypoint(0.83, -0.49, 0.5, 0.09),
                        new Waypoint(1.04, -7.34, 0.5, 0.09),
                        new Waypoint(0.50, -1.83, 0.5, 0.09),
                        new Waypoint(-0.15,-2.46, 0.5, 0.09),
                        new Waypoint(-0.92, -3.14, 0.5, 0.09),
                        new Waypoint(-1.33, -3.76, 0.5, 0.09),
                        new Waypoint(-1.11, -4.45, 0.5, 0.09),
                        new Waypoint(-0.30,-4.73, 0.5, 0.09),
                        new Waypoint(0.46, -4.15, 0.5, 0.09),
                        new Waypoint(0.61, -3.63, 0.5, 0.09)
                    );
                    break;
                case TEST:
                    addPoints(path,
                        new Waypoint(0.0, 0.0, 0.5, 0.09),
                        new Waypoint(0.4, 0.0, 0.5, 0.09),
                        new Waypoint(0.8, 0.0,0.5, 0.09),
                        new Waypoint(1.2, 0.0, 0.5, 0.09),
                        new Waypoint(1.6, 0.0, 0.5, 0.09),
                        new Waypoint(2.0, 0.0, 0.5, 0.09),
                        new Waypoint(2.0, 0.2, 0.5, 0.09),
                        new Waypoint(2.0, 0.4, 0.5, 0.09),
                        new Waypoint(2.0, 0.6, 0.5, 0.09),
                        new Waypoint(2.0, 0.8, 0.5, 0.09),
                        new Waypoint(2.0, 1.0, 0.5, 0.09),
                        new Waypoint(1.6, 1.0, 0.5, 0.09),
                        new Waypoint(1.2, 1.0, 0.5, 0.09),
                        new Waypoint(0.8, 1.0, 0.5, 0.09),
                        new Waypoint(0.4, 1.0, 0.5, 0.09),
                        new Waypoint(0.0, 1.0, 0.5, 0.09),
                        new Waypoint(0.0, 0.8, 0.5, 0.09),
                        new Waypoint(0.0, 0.6, 0.5, 0.09)//,
                        //new Waypoint(0.0, 0.4, 0.5, 0.09), new Waypoint(0.0, 0.2, 0.5, 0.09)
                    );
                    break;
            }
            return path;
        }
    }

}
