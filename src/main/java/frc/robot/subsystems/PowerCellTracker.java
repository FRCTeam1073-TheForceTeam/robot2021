package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Iterator;

import edu.wpi.first.hal.CANData;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PowerCellTracker extends OpenMVSubsystem {

    /**
     * This is the data for each active power cell target visible.
     */
    public static class PowerCellData implements Cloneable {
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
      
      @Override
      protected Object clone() throws CloneNotSupportedException {
        PowerCellData clone = null;

        clone = new PowerCellData();
        clone.cx = cx;
        clone.cy = cy;
        clone.vx = vx;
        clone.vy = vy;
        clone.targetType = targetType;
        clone.quality = quality;
        clone.timestamp = timestamp;
        
        return clone;
      }
    }

    private CANData targetData;
    private long lastDataUpdate = 0;
    private long failedReadCount = 0;
    private ArrayList<PowerCellData> m_cellData;
    private int m_lastCellDataSize = 0;
    private int m_lastLastCellDataSize = 0;
    private int m_lastLastLastCellDataSize = 0;
    
    private double m_range = -1.0;
    private double m_rangeSignalStrength = 0.0;
    private long m_rangeFailedCount = 0;
    private long m_lastRangeUpdate = 0;

    public PowerCellTracker() {
      super(2);
      targetData = new CANData();
      m_cellData = new ArrayList<PowerCellData>();
    }

    /// Return set of active targets.
    public boolean getCellData(PowerCellData data) {
      if (m_cellData.size() > 0) {
        if (m_cellData.get(0).quality > 10) {
          data.cx = m_cellData.get(0).cx;
          data.cy = m_cellData.get(0).cy;
          data.vx = m_cellData.get(0).vx;
          data.vy = m_cellData.get(0).vy;
          data.targetType = m_cellData.get(0).targetType;
          data.quality = m_cellData.get(0).quality;
          data.timestamp = lastDataUpdate;
          return true;
        } else {
          data.clear();
          return false;
        }
      } else {
        data.clear();
        return false;
      }
    }

    public boolean getCellData(ArrayList<PowerCellData> cells) {
      if (m_cellData.size() > 0) {
        cells.clear();
        Iterator<PowerCellData> iterator = m_cellData.iterator();
        while (iterator.hasNext()) {
          try {
            cells.add((PowerCellData)iterator.next().clone());
          }

          catch (CloneNotSupportedException e) {
          }
        }
        return true;
      } else {
        cells.clear();
        return false;
      }
    }

  //If there is a cell in the list, we "have data" and will return true 
  public boolean hasData() {
    return (m_cellData.size() > 0) || (m_lastCellDataSize > 0) || (m_lastLastCellDataSize > 0) || (m_lastLastLastCellDataSize > 0);
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
      m_lastLastLastCellDataSize = m_lastLastCellDataSize;
      m_lastLastCellDataSize = m_lastCellDataSize;
      m_lastCellDataSize = m_cellData.size();
      m_cellData.clear();
      //Used to be 3 instead of 1
      for (int index = 1; index <= 1; index++) {
        if (read(apiIndex(5, index), targetData) == true && targetData.length == 8) {
          if (targetData.data[6]!=0) {
            PowerCellData tempCellData = new PowerCellData();
            int cxhi = targetData.data[0] & 0xFF;
            int cxlo = targetData.data[1] & 0xF0;
            tempCellData.cx = (cxhi << 4) | (cxlo >> 4);
            int cyhi = targetData.data[1] & 0x0F;
            int cylo = targetData.data[2] & 0xFF;
            tempCellData.cy = (cyhi << 8) | cylo;
            int areahi = targetData.data[3] & 0xFF;
            int arealo = targetData.data[4] & 0xFF;
            tempCellData.area = (areahi << 8) | arealo;
            tempCellData.targetType = targetData.data[5];
            tempCellData.quality = targetData.data[6];
            tempCellData.timestamp = targetData.timestamp; // Assign the CANBus message timestamp
            lastDataUpdate = targetData.timestamp;
            m_cellData.add(tempCellData);              
          }
        } else {
          // 10 Failures is ~ 0.5 seconds with 50Hz loop.
          failedReadCount++;
        }
      }
      if (m_cellData.size() > 0) {
        return true;
      } else {
        return false;
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
      super.periodic();

      // Now do PowerPortTracker specific things.
      readAdvancedTracking();
      // for (int index = 0; index < 3; index++) {
      //   if (m_cellData.size() > index) {
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].CX", index), m_cellData.get(index).cx);
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].CY", index), m_cellData.get(index).cy);
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].Qual", index), m_cellData.get(index).quality);
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].Area", index), m_cellData.get(index).area);
      //   } else {
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].CX", index), 0);
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].CY", index), 0);
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].Qual", index), 0);
      //     SmartDashboard.putNumber(String.format("PowerCell[%d].Area", index), 0);
      //   }
      // }

      // SmartDashboard.putBoolean("[PowerCell.HasData", hasData());

      // SmartDashboard.putNumber("PowerCell.Range", m_range);
      // SmartDashboard.putNumber("PowerCell.RangeSignal", m_rangeSignalStrength);
      readRange();
    }
}
