package frc.robot.subsystems;

import edu.wpi.first.hal.CANData;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PowerPortTracker extends OpenMVSubsystem {

      /**
     * This is the data for power port objects.
     */
    public static class PowerPortData {
      public int cx = 0;  // X coordinate of target center in image (pixels)
      public int cy = 0;  // Y coordinate of target center in image (pixels)
      public int vx = 0;  // X veloicity of target in image pixels/second
      public int vy = 0;  // Y velocity of target in image pixels/second
      public int area = 0; // Area of the tracked object.
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

      public PowerPortData() {
        clear();
      }

      public void clear() {
        cx = 0;
        cy = 0;
        vx = 0;
        vy = 0;
        targetType = 0;
        quality = 0;
        timestamp = 0;
      }

    }

    private CANData targetData;
    private long lastDataUpdate = 0;
    private long failedReadCount = 0;
    private PowerPortData m_portData;

    private double m_range = -1.0;
    private double m_rangeSignalStrength = 0.0;
    private long m_rangeFailedCount = 0;
    private long m_lastRangeUpdate = 0;

    public PowerPortTracker() {
      super(1); // Constructor with device ID for OpenMVProtocol.
      targetData = new CANData();
      m_portData = new PowerPortData();
    }


  /**
   * Fills out port data if there is a tracked target, otherwise, clears port data and
   * return false.
   * 
   * @return true if there is a tracked target, false if there is not.
   */    
    public boolean getPortData(PowerPortData data) {
     
      // If !visible => clear data and return false.
      // Else copy data and return true.
      if (m_portData.quality > 10) {
        data.cx = m_portData.cx;
        data.cy = m_portData.cy;
        data.vx = m_portData.vx;
        data.vy = m_portData.vy;
        data.targetType = m_portData.targetType;
        data.quality = m_portData.quality;
        data.timestamp = lastDataUpdate;
        return true;
      } else {
        data.clear();
        return false;
      }
    }

    // Returns range sent by range sensor or -1.0 if there is an error.
    public double getRange() {
      if (m_rangeSignalStrength < 10)
        return -1.0;
      else
        return m_range;
    }

    public double getRangeSignalStrength() {
      return m_rangeSignalStrength;
    }

    // Updates our config and mode:
    private boolean readAdvancedTracking() {
      if (read(apiIndex(5, 1), targetData) == true && targetData.length == 8) {
        int cxhi = targetData.data[0] & 0xFF;
        int cxlo = targetData.data[1] & 0xF0;
        m_portData.cx = (cxhi << 4) | (cxlo >> 4);
        int cyhi = targetData.data[1] & 0x0F;
        int cylo = targetData.data[2] & 0xFF;
        m_portData.cy = (cyhi << 8) | cylo;
        int areahi = targetData.data[3] & 0xFF;
        int arealo = targetData.data[4] & 0xFF;
        m_portData.area = (areahi << 8) | arealo;
        m_portData.targetType = targetData.data[5];
        m_portData.quality = targetData.data[6];
        m_portData.timestamp = targetData.timestamp; // Assign the CANBus message timestamp
        lastDataUpdate = targetData.timestamp;
        return true;
      }
      else {
        // 10 Failures is ~ 0.5 seconds of no data @ 50Hz loop.
        if(failedReadCount++ >= 10){
          m_portData.quality = 0;
          failedReadCount = 0;
          return false;
        }
        else return true;
      }
    }

    private boolean readRange() {
      if (read(apiIndex(6, 1), targetData) == true && targetData.length == 3) {
        // Range is 16-bit distance in mm.
        int rangehi = targetData.data[0] & 0xFF;
        int rangelo = targetData.data[1] & 0xFF;
        m_range = ((rangehi << 8) | rangelo) * 0.001; // Convert mm reading to meters.
        m_rangeSignalStrength = targetData.data[2];
        m_lastRangeUpdate = targetData.timestamp;
        return true;
      } else {
        // 10 Failures is ~ 0.5 seconds of no data @ 50Hz loop.
        if (m_rangeFailedCount++ >= 10) {
          m_rangeSignalStrength = 0.0;
          m_range = -1.0;
          m_rangeFailedCount = 0;
          return false;
        }
        else {
          return true;
        }
      }
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
      super.periodic(); // The OpenMVSubsystem needs you to call this in your periodic method.

      // Now do PowerPortTracker specific things.
      readAdvancedTracking();
      SmartDashboard.putNumber("PowerPort.CX", m_portData.cx);
      SmartDashboard.putNumber("PowerPort.CY", m_portData.cy);
      SmartDashboard.putNumber("PowerPort.Qual", m_portData.quality);
      SmartDashboard.putNumber("PowerPort.Area", m_portData.area);

     readRange();
     SmartDashboard.putNumber("PowerPort.Range", m_range);
     SmartDashboard.putNumber("PowerPort.RangeSignal", m_rangeSignalStrength);
    }
    
}
