package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PowerCellTracker extends SubsystemBase {

    /**
     * This is the data for each active power cell target visible.
     */
    public static class PowerCellData {
      public int cx = 0;  // X coordinate of target center in image (pixels)
      public int cy = 0;  // Y coordinate of target center in image (pixels)
      public int vx = 0;  // X veloicity of target in image pixels/second
      public int vy = 0;  // Y velocity of target in image pixels/second
      public int targetType = 0; // Target type: 0-15 allows different targets
      public int quality = 0; // Quality of target data in percent. 0 = lost/empty, 100 = perfect lock.
      public long timestamp = 0; // Timestamp of this target data update.

      public PowerCellData(int _cx, int _cy, int _vx, int _vy, int _targetType, int _quality) {
          cx = _cx;
          cy = _cy;
          vx = _vx;
          vy = _vy;
          targetType = _targetType;
          quality = _quality;
          timestamp = 0;
      }
    }

    public PowerCellTracker() {

    }

    /// Return number of active targets.
    public int getNumTargets() {
      // TODO:
      return 0;
    }

    /// Return set of active targets.
    public PowerCellData[] getPowerCells() {
      // TODO:
      return new PowerCellData[1];
    }

    /// Range sensor detects if power cell is in the collector.
    public boolean powerCellInCollector() {
      // TODO:
      return false;
    }

    
    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
}
