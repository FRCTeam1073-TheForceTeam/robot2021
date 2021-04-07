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
                        new Waypoint(2.064,-1.23,0.5,0),
                        new Waypoint(2.04,-.487,0.5,0),
                        new Waypoint(2.26,-.011,0.5,0),
                        new Waypoint(3.18,.019,0.5,0),
                        new Waypoint(3.65,.025,0.5,0),
                        new Waypoint(4.037,.028,0.5,0),
                        new Waypoint(4.63,.032,0.5,0),
                        new Waypoint(5.2,.032,0.5,0),
                        new Waypoint(5.83,.185,0.5,0),
                        new Waypoint(6.138,.53,0.5,0),
                        new Waypoint(6.16,1.036,0.5,0),
                        new Waypoint(5.78,1.425,0.5,0),
                        new Waypoint(5.26,1.628,0.5,0),
                        new Waypoint(4.92,1.628,0.5,0)
                    );
                    break;
                case BARREL_3:
                    double K = 0.2;
                    double G = 0.2;
                    double Y = -0.2;
                    addPoints(path,
                        new Waypoint(4.711,1.53,0.5,0),
                        new Waypoint(4.508,1.16,0.5,0),
                        new Waypoint(4.459,0.69,0.5,0),
                        new Waypoint(4.53,0.329-K,0.5,0),
                        new Waypoint(4.80,0.048-K,0.5,0),
                        new Waypoint(5.28,-0.34-K,0.5,0),
                        new Waypoint(5.63,-0.613-K,0.5,0),
                        new Waypoint(5.94,-0.95-K,0.5,0),
                        new Waypoint(6.53-G,-1.26-K,0.5,0),
                        new Waypoint(6.89-G,-1.413-K,0.5,0),
                        new Waypoint(7.42-G,-1.420-K,0.5,0),
                        new Waypoint(7.76-G,-1.156-0.5*Y-K,0.5,0),
                        new Waypoint(7.85-G,-0.836-Y-K,0.5,0),
                        new Waypoint(7.815-G,-0.608-Y-K,0.5,0),
                        new Waypoint(7.725-G,-0.440-Y-K,0.5,0),
                        new Waypoint(7.5-G,-0.25-Y-K,0.5,0)
                    );
                    break;
                case BARREL_4:
                    addPoints(path,
                        new Waypoint(7.3056,-0.244,0.5,0),
                        new Waypoint(6.963,-0.816,0.5,0),
                        new Waypoint(6.3991,-0.022,0.5,0)
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
                        new Waypoint(0.0, 0.6, 0.5, 0.09),
                        new Waypoint(0.0, 0.4, 0.5, 0.09), 
                        new Waypoint(0.0, 0.2, 0.5, 0.09)
                    );
                    break;
            }
            return path;
        }
    }

}
