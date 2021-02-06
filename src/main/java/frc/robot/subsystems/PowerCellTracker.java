package frc.robot.subsystems;

import edu.wpi.first.hal.CANData;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PowerCellTracker extends OpenMVSubsystem {

    /**
     * This is the data for each active power cell target visible.
     */
    public static class PowerCellData {
      public int cx = 0;  // X coordinate of target center in image (pixels)
      public int cy = 0;  // Y coordinate of target center in image (pixels)
      public int vx = 0;  // X veloicity of target in image pixels/second
      public int vy = 0;  // Y velocity of target in image pixels/second
      public int area = 0; // Area of power cell.
      public int targetType = 0; // Target type: 0-15 allows different targets
      public int quality = 0; // Quality of target data in percent. 0 = lost/empty, 100 = perfect lock.
      public long timestamp = 0; // Timestamp of this target data update.

      public PowerCellData() {
        clear();
      }
      
      public PowerCellData(int _cx, int _cy, int _vx, int _vy, int _targetType, int _quality) {
          cx = _cx;
          cy = _cy;
          vx = _vx;
          vy = _vy;
          targetType = _targetType;
          quality = _quality;
          timestamp = 0;
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
    private PowerCellData m_cellData;

    public PowerCellTracker() {
      super(2);
      targetData = new CANData();
      m_cellData = new PowerCellData();
    }

    /// Return set of active targets.
    public boolean getCellData(PowerCellData data) {
      if (m_cellData.quality > 10) {
        data.cx = m_cellData.cx;
        data.cy = m_cellData.cy;
        data.vx = m_cellData.vx;
        data.vy = m_cellData.vy;
        data.targetType = m_cellData.targetType;
        data.quality = m_cellData.quality;
        data.timestamp = lastDataUpdate;
        return true;
      } else {
        data.clear();
        return false;
      }
    }

    // Updates our config and mode:
    private boolean readAdvancedTracking() {
      if (read(apiIndex(5, 1), targetData) == true && targetData.length == 8) {
        int cxhi = targetData.data[0] & 0xFF;
        int cxlo = targetData.data[1] & 0xF0;
        m_cellData.cx = (cxhi << 4) | (cxlo >> 4);
        int cyhi = targetData.data[1] & 0x0F;
        int cylo = targetData.data[2] & 0xFF;
        m_cellData.cy = (cyhi << 8) | cylo;
        int areahi = targetData.data[3] & 0xFF;
        int arealo = targetData.data[4] & 0xFF;
        m_cellData.area = (areahi << 8) | arealo;
        m_cellData.targetType = targetData.data[5];
        m_cellData.quality = targetData.data[6];
        m_cellData.timestamp = targetData.timestamp; // Assign the CANBus message timestamp
        lastDataUpdate = targetData.timestamp;
        return true;
      }
      else {
        if(failedReadCount++ >= 7){
          m_cellData.quality = 0;
          failedReadCount = 0;
          return false;
        }
        else return true;
      }
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
      super.periodic();

      // Now do PowerPortTracker specific things.
      if (readAdvancedTracking()) {
        SmartDashboard.putNumber("PowerCell.CX", m_cellData.cx);
        SmartDashboard.putNumber("PowerCell.CY", m_cellData.cy);
        SmartDashboard.putNumber("PowerCell.Qual", m_cellData.quality);
        SmartDashboard.putNumber("PowerCell.Area", m_cellData.area);
      }
    }
}
