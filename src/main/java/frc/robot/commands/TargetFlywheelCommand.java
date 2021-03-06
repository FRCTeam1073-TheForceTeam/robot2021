
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.Constants;
import frc.robot.components.InterpolatorTable;
import frc.robot.components.InterpolatorTable.InterpolatorTableEntry;

/**
 * Command that sets the flywheel speed based on the range sensor.
 * @author Ben (though it was Mr.Pack's idea)
 */
public class TargetFlywheelCommand extends CommandBase {

  Shooter shooter;
  PowerPortTracker portTracker;
  double flywheelVelocity;
  double currentFlywheelVelocity;
  double range;

  boolean hasValidRangeData;

  int numLoops;
  int rangeUpdatePeriod;

  //How close (in radians/sec difference) the flywheel velocity should be to the target flyhweel velocity before the command ends.
  //Would be included as a parameter in the constructor, but to ensure that WaitToFire agrees with TargetFlywheelCommand on what
  //that value is, it was made into a constant.
  public static final double acceptableVelocityDifference = Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE;
  
  InterpolatorTable flywheelTable;
  
  /**
   * Uses the range sensor to set the flywheel speed.
   * @param shooter_ The shooter subsystem
   * @param portTracker_ The port tracker (not listed under addRequirements so that other commands can use it at the same time)
   * @param rangeUpdatePeriod_ The amount of execute loops between updates to the range value (defaults to 1)
   * @param acceptableVelocityDifference_ How close (in radians/sec difference) the flywheel velocity should be to the target flyhweel velocity before the command ends (defaults to 1 radian/sec).
   */
  public TargetFlywheelCommand(Shooter shooter_, PowerPortTracker portTracker_, int rangeUpdatePeriod_, double acceptableVelocityDifference_) {
    portTracker = portTracker_;
    rangeUpdatePeriod = rangeUpdatePeriod_;
    flywheelTable = new InterpolatorTable(
        new InterpolatorTableEntry(1.79, 281.25), new InterpolatorTableEntry(2.35, 343.75),
        new InterpolatorTableEntry(3.05, 343.75), new InterpolatorTableEntry(3.54, 375),
        new InterpolatorTableEntry(3.97, 406.25), new InterpolatorTableEntry(4.56, 437.3),
        new InterpolatorTableEntry(5.02, 437.3), new InterpolatorTableEntry(5.51, 468.75),
        new InterpolatorTableEntry(6.03, 468.75), new InterpolatorTableEntry(6.51, 468.75));
    shooter = shooter_;
    currentFlywheelVelocity = 0;
    flywheelVelocity = 0;
    range = 0;
    numLoops = 0;
    hasValidRangeData = false;
    // addRequirements(shooter);
  }

  /**
   * Uses the range sensor to set the flywheel speed.
   * @param shooter_ The shooter subsystem
   * @param portTracker_ The port tracker (not listed under addRequirements so that other commands can use it at the same time)
   * @param rangeUpdatePeriod_ The amount of execute loops between updates to the range value (defaults to 1)
   */
  public TargetFlywheelCommand(Shooter shooter_, PowerPortTracker portTracker_, int rangeUpdatePeriod_) {
    this(shooter_, portTracker_, rangeUpdatePeriod_, 1);
  }


  /**
   * Uses the range sensor to set the flywheel speed.
   * @param shooter_ The shooter subsystem
   * @param portTracker_ The port tracker (not listed under addRequirements so that other commands can use it at the same time)
   */
  public TargetFlywheelCommand(Shooter shooter_, PowerPortTracker portTracker_) {
    this(shooter_, portTracker_, 1, 1);
  }


  @Override
  public void initialize() {
    shooter.setFlywheelVelocity(0);
  }

  public void execute() {
    //Only check every (rangeUpdatePeriod) loops
    if (numLoops % rangeUpdatePeriod == 0) {
      double currentRange = portTracker.getRange();
      if (currentRange != -1 && (currentRange >= 1 && currentRange <= 6.5)) {
        hasValidRangeData = true;
        range = currentRange;
      }        
    }
    numLoops++;
    if (hasValidRangeData) {
      flywheelVelocity = flywheelTable.getValue(range);
      shooter.setFlywheelVelocity(flywheelVelocity);
      SmartDashboard.putNumber("[TargetFlywheel] Flywheel target vel", flywheelVelocity);
      SmartDashboard.putNumber("[TargetFlywheel] Flywheel range", range);
      currentFlywheelVelocity = shooter.getFlywheelVelocity();
      SmartDashboard.putNumber("[TargetFlywheel] Flywheel actual vel", currentFlywheelVelocity);
    }
  }

  public void end(boolean interrupted) {
  }
  
  public boolean isFinished() {
    return false;
    // return Math.abs(currentFlywheelVelocity - flywheelVelocity) <= acceptableVelocityDifference;
  }
}