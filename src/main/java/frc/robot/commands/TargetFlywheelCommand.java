
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.Constants;
import frc.robot.Constants.PowerPortConfiguration;
import frc.robot.components.InterpolatorTable;
import frc.robot.components.InterpolatorTable.InterpolatorTableEntry;

/**
 * Command that sets the flywheel speed based on the range sensor.
 * 
 * @author Ben (though it was Mr.Pack's idea)
 */
public class TargetFlywheelCommand extends CommandBase {

  Shooter shooter;
  PowerPortTracker portTracker;
  double flywheelVelocity;
  double currentFlywheelVelocity;
  double range;

  /**
   * Table for shooting in the gym, or anywhere else with the full-size power
   * port.
   **/
  InterpolatorTable flywheelTableHigh = new InterpolatorTable(
    new InterpolatorTableEntry(1.52, 312.5),
    new InterpolatorTableEntry(2.01, 336.94),
    new InterpolatorTableEntry(2.50, 332.57),
    new InterpolatorTableEntry(3.00, 383.82),
    new InterpolatorTableEntry(3.52, 421.875),
    new InterpolatorTableEntry(4.04, 437.5),
    new InterpolatorTableEntry(4.54, 453.125),
    new InterpolatorTableEntry(5.01, 468.75),
    new InterpolatorTableEntry(5.49, 500.00)
  );

  /** Table for shooting in 107, or anywhere else with the small power port. **/
  InterpolatorTable flywheelTableLow = new InterpolatorTable(
      /*
       * Scaled down all the speeds by the same factor as the high one vs. the low
       * one. It works pretty well for now, we'll need to retune this later.
       */
      new InterpolatorTableEntry(1.79, 281.25 * (281.25 / 332.0)),
      new InterpolatorTableEntry(2.35, 343.75 * (281.25 / 332.0)),
      new InterpolatorTableEntry(3.05, 343.75 * (281.25 / 332.0)),
      new InterpolatorTableEntry(3.54, 375 * (281.25 / 332.0)),
      new InterpolatorTableEntry(3.97, 406.25 * (281.25 / 332.0)),
      new InterpolatorTableEntry(4.56, 437.3 * (281.25 / 332.0)),
      new InterpolatorTableEntry(5.02, 437.3 * (281.25 / 332.0)),
      new InterpolatorTableEntry(5.51, 468.75 * (281.25 / 332.0)),
      new InterpolatorTableEntry(6.03, 468.75 * (281.25 / 332.0)),
      new InterpolatorTableEntry(6.51, 468.75 * (281.25 / 332.0)));

  boolean hasValidRangeData;

  int numLoops;
  int rangeUpdatePeriod;

  // How close (in radians/sec difference) the flywheel velocity should be to the
  // target flyhweel velocity before the command ends.
  // Would be included as a parameter in the constructor, but to ensure that
  // WaitToFire agrees with TargetFlywheelCommand on what
  // that value is, it was made into a constant.
  public static final double acceptableVelocityDifference = Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE;

  InterpolatorTable flywheelTable;

  /**
   * Uses the range sensor to set the flywheel speed.
   * 
   * @param shooter_                      The shooter subsystem
   * @param portTracker_                  The port tracker (not listed under
   *                                      addRequirements so that other commands
   *                                      can use it at the same time)
   * @param rangeUpdatePeriod_            The amount of execute loops between
   *                                      updates to the range value (defaults to
   *                                      1)
   * @param acceptableVelocityDifference_ How close (in radians/sec difference)
   *                                      the flywheel velocity should be to the
   *                                      target flyhweel velocity before the
   *                                      command ends (defaults to 1 radian/sec).
   */
  public TargetFlywheelCommand(Shooter shooter_, PowerPortTracker portTracker_, int rangeUpdatePeriod_,
      double acceptableVelocityDifference_) {
    rangeUpdatePeriod = rangeUpdatePeriod_;

    if (Constants.portConfig == PowerPortConfiguration.LOW) {
      flywheelTable = flywheelTableLow;
    } else if (Constants.portConfig == PowerPortConfiguration.HIGH) {
      flywheelTable = flywheelTableHigh;
    }

    shooter = shooter_;
    portTracker = portTracker_;
    // addRequirements(shooter);
  }

  /**
   * Uses the range sensor to set the flywheel speed.
   * 
   * @param shooter_           The shooter subsystem
   * @param portTracker_       The port tracker (not listed under addRequirements
   *                           so that other commands can use it at the same time)
   * @param rangeUpdatePeriod_ The amount of execute loops between updates to the
   *                           range value (defaults to 1)
   */
  public TargetFlywheelCommand(Shooter shooter_, PowerPortTracker portTracker_, int rangeUpdatePeriod_) {
    this(shooter_, portTracker_, rangeUpdatePeriod_, 1);
  }

  /**
   * Uses the range sensor to set the flywheel speed.
   * 
   * @param shooter_     The shooter subsystem
   * @param portTracker_ The port tracker (not listed under addRequirements so
   *                     that other commands can use it at the same time)
   */
  public TargetFlywheelCommand(Shooter shooter_, PowerPortTracker portTracker_) {
    this(shooter_, portTracker_, 1, 1);
  }

  @Override
  public void initialize() {
    currentFlywheelVelocity = 0;
    flywheelVelocity = 0;
    range = 0;
    numLoops = 0;
    hasValidRangeData = false;
    // shooter.setFlywheelVelocity(0);
  }

  public void execute() {
    // Only check every (rangeUpdatePeriod) loops
    if (numLoops % rangeUpdatePeriod == 0) {
      double currentRange = portTracker.getRange();
      if (currentRange != -1 && (currentRange >= 1 && currentRange <= Constants.MAXIMUM_DETECTABLE_RANGE)) {
        hasValidRangeData = true;
        range = currentRange;
      }
    }
    numLoops++;
    if (hasValidRangeData) {
      flywheelVelocity = flywheelTable.getValue(range);
      shooter.setFlywheelVelocity(flywheelVelocity);
      currentFlywheelVelocity = shooter.getFlywheelVelocity();
      SmartDashboard.putNumber("[TGTF] Range [0]", range);
      SmartDashboard.putNumber("[TGTF] Target flywheel vel [1]", flywheelVelocity);
      SmartDashboard.putNumber("[TGTF] Actual flywheel vel [2]", currentFlywheelVelocity);
    }
  }

  public void end(boolean interrupted) {

  }

  public boolean isFinished() {
    return false;
    // return Math.abs(currentFlywheelVelocity - flywheelVelocity) <=
    // acceptableVelocityDifference;
  }
}