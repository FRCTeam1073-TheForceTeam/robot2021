package frc.robot.components;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.geometry.Pose2d;

/**
 * This class represents a waypoint path as a collection of segments with start and end points.
 * The segment defines the goal speed for driving the segment and the desired driving direction
 * as well as the radii tolerance for reaching the end of the segment and moving on.
 */
public class WaypointPath {

    /**
     * This class represents one waypoint on the path including speed information
     * and the lookahead to use when this is the current waypoint.
     */
    public class Waypoint {

        public Vector2d point;            // Location of the waypoint.
        public double   speed;            // The desired speed for this waypoint.
        public double   tolerance;        // The tolerance for being at this point.

        public Waypoint(double x, double y, double spd, double tol) {
            point.x = x;
            point.y = y;
            speed = spd;
            tolerance = tol;
        }

        // Is the given position at the end of the segment (given the tolerance)
        boolean atPoint(Vector2d pos) {
            Vector2d delta = new Vector2d(pos.x - point.x, pos.y - point.y);
            if (delta.magnitude() < tolerance)
                return true;
            else
                return false;
        }
    }

    private ArrayList<Waypoint> path;  // Collection of waypoints is a path.
    private int currentWaypoint;       // Current waypoint in the path.

    public WaypointPath() {
        path = new ArrayList<Waypoint>();
        currentWaypoint = 0;
    }
    
    /// Add a segment to the end of the path. At least two points are required.
    public void addWaypoint(Waypoint wp) {
        path.add(wp);
    }

    /// Resets the path cursor to the first segment of the path.
    public void resetCurrentPath() {
        currentWaypoint = 0; // First segment in the path.
    }

    /// Finds the closest waypoint index to the given point: Starting with current waypoint.
    public int findClosestWaypoint(Vector2d pos) {
        double minDist = Double.MAX_VALUE;
        int closest = -1;
        for (int idx = currentWaypoint; idx < path.size(); idx++) {
            double dist = Math.sqrt( (pos.x - path.get(idx).point.x) * (pos.x - path.get(idx).point.x) +
            (pos.y - path.get(idx).point.y) * (pos.y - path.get(idx).point.y));
            if (dist < minDist) {
                minDist = dist;
                closest = idx;
            }
        }
        return closest;
    }

    // Update current waypoint and return true if there are more waypoints in the path, else return false.
    public boolean updateCurrentWaypoint(int closest) {
        currentWaypoint = closest;

        if (currentWaypoint >= path.size()-1) {
            return false;
        } else {
            return true;
        }
    }

    /// Get the current segment or null if there is no current segment.
    public Waypoint getCurrentWaypoint() {
        if (currentWaypoint >= 0 && currentWaypoint < path.size()) {
            return path.get(currentWaypoint);
        } else {
            return null;
        }
    }

    public int getIndex() {
        return currentWaypoint;
    }

    /// Get the next waypoint on the path.
    public Waypoint getNextWaypoint() {
        if (currentWaypoint >= 0 && currentWaypoint < (path.size() - 1))
            return path.get(currentWaypoint+1);
        else
            return null;
    }

    /// Get robot coordinates of a waypoint:
    public Vector2d getRobotCoordinates(Waypoint wp, Pose2d odometry) {
        Vector2d result = new Vector2d();

        // Rewrite the waypoint location into robot coordinates.
        result.x = wp.point.x - odometry.getTranslation().getX();
        result.y = wp.point.y - odometry.getTranslation().getY();
        result.rotate(-odometry.getRotation().getDegrees());

        return result;
    }
}
