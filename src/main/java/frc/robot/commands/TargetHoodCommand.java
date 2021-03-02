
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;
import frc.robot.Constants;
import frc.robot.components.InterpolatorTable;
import frc.robot.components.InterpolatorTable.InterpolatorTableEntry;

/**
 * Command that moves the hood to a position based on the range sensor.
 * @author Ben (though it was Mr.Pack's idea)
 */
public class TargetHoodCommand extends CommandBase {

  Shooter shooter;
  PowerPortTracker portTracker;
  double hoodAngle;

  double range;

  InterpolatorTable hoodTable;

  //Indicates how many valid ranges have been detected.
  int validRangeCounter;
  int requiredValidRangeCount;

  boolean readyToFire;

  /**
   * Moves the hood to a position based on the range sensor.
   * @param shooter_ The shooter subsystem
   * @param portTracker_ The port tracker (not listed under addRequirements so that other commands can use it at the same time)
   * @param requiredValidRangeCount_ The number of valid ranges that need to be collected before the hood can move (defaults to 5)
   */
  public TargetHoodCommand(Shooter shooter_, PowerPortTracker portTracker_, int requiredValidRangeCount_) {
    shooter = shooter_;
    portTracker = portTracker_;

    requiredValidRangeCount = requiredValidRangeCount_;

    hoodTable = new InterpolatorTable(
      new InterpolatorTableEntry(1.79, 0.658353), new InterpolatorTableEntry(2.35, 0.483353),
      new InterpolatorTableEntry(3.05, 0.483353), new InterpolatorTableEntry(3.54, 0.483353),
      new InterpolatorTableEntry(3.97, 0.408353), new InterpolatorTableEntry(4.56, 0.363353),
      new InterpolatorTableEntry(5.02, 0.358353), new InterpolatorTableEntry(5.51, 0.367783),
      new InterpolatorTableEntry(6.03, 0.367783), new InterpolatorTableEntry(6.51, 0.34278)
    );

    validRangeCounter = 0;
    readyToFire = false;

  }
  
  
  /**
   * Moves the hood to a position based on the range sensor (defaulting to 5 valid ranges before moving).
   * @param shooter_ The shooter subsystem
   * @param portTracker_ The port tracker (not listed under addRequirements so that other commands can use it at the same time)
   */
  public TargetHoodCommand(Shooter shooter_, PowerPortTracker portTracker_) {
    this(shooter_, portTracker_, 5);
  }
  
  @Override
  public void initialize() {
    shooter.lowerHood();
    validRangeCounter = 0;
    readyToFire = false;
  }

  public void execute() {
    if (!readyToFire && (validRangeCounter < requiredValidRangeCount)) {
      double currentRange = portTracker.getRange();
      if (currentRange != -1 && (currentRange >= 1.0 && currentRange <= 6.5)) {
        validRangeCounter++;
        if (validRangeCounter == requiredValidRangeCount) {
          readyToFire = true;
          range = currentRange;
        }
      }
    }
    if (readyToFire) {
      hoodAngle = hoodTable.getValue(range);
      SmartDashboard.putNumber("[TargetHood] Hood target angle", hoodAngle);
      SmartDashboard.putNumber("[TargetHood] Actual hood angle", shooter.getHoodAngle());
      SmartDashboard.putNumber("[TargetHood] Range", range);
      shooter.setHoodAngle(hoodAngle);      
    }
  }

  public void end(boolean interrupted) {
  }
  
  public boolean isFinished() {
    return readyToFire && (Math.abs(shooter.getHoodAngle() - hoodAngle) < Constants.ACCEPTABLE_HOOD_ANGLE_DIFFERENCE);
  }
}
