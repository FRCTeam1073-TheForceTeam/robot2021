package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PowerPortTracker extends SubsystemBase {

      /**
     * This is the data for each active power cell target visible.
     */
    public static class PowerPortData {
      public int cx = 0;  // X coordinate of target center in image (pixels)
      public int cy = 0;  // Y coordinate of target center in image (pixels)
      public int vx = 0;  // X veloicity of target in image pixels/second
      public int vy = 0;  // Y velocity of target in image pixels/second
      public int targetType = 0; // Target type: 0-15 allows different targets
      public int quality = 0; // Quality of target data in percent. 0 = lost/empty, 100 = perfect lock.
      public long timestamp = 0; // Timestamp of this target data update.

      public PowerPortData(int _cx, int _cy, int _vx, int _vy, int _targetType, int _quality) {
          cx = _cx;
          cy = _cy;
          vx = _vx;
          vy = _vy;
          targetType = _targetType;
          quality = _quality;
          timestamp = 0;
      }
    }

    public PowerPortTracker() {

    }

    /// Return number of active targets.
    public boolean isPortVisible() {
      // TODO:
      return false;
    }

    /// Return active target data.
    public PowerPortData getPowerPort() {
      // TODO:
      return new PowerPortData(0,0,0,0,0,0);
    }

    // Returns azimuth to target if port is visible.
    public double getAzimuth() {
      // TODO:
      return 0.0;
    }

    // Returns elevation if port is visible.
    public double getElevation() {
      // TODO:
      return 0.0;
    }

    // Returns range if port is visible.
    public double getRange() {
      // TODO:
      return 0.0;
    }

    // Returns if the flight path is blocked.
    public boolean flightPathBlocked() {
      // TODO:
      return true;
    }

    
    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
    
}
