
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
 * @author Ben (though it was Mr.Pack's idea)
 */
public class TargetFlywheelCommand extends CommandBase {

  Shooter shooter;
  PowerPortTracker portTracker;
  double flywheelVelocity;
  double currentFlywheelVelocity;
  double range;

  /**Table for shooting in the gym, or anywhere else with the full-size power port.**/
  InterpolatorTable flywheelTableHigh = new InterpolatorTable(
    new InterpolatorTableEntry(1.6, 332.0),
    new InterpolatorTableEntry(1.8, 348.0),
    new InterpolatorTableEntry(3.48, 410.1),
    new InterpolatorTableEntry(4.86, 509.8),
    new InterpolatorTableEntry(6.18,527.15)
  );

  /**Table for shooting in 107, or anywhere else with the small power port.**/
  InterpolatorTable flywheelTableLow = new InterpolatorTable(
    new InterpolatorTableEntry(1.79, 281.25), new InterpolatorTableEntry(2.35, 343.75),
    new InterpolatorTableEntry(3.05, 343.75), new InterpolatorTableEntry(3.54, 375),
    new InterpolatorTableEntry(3.97, 406.25), new InterpolatorTableEntry(4.56, 437.3),
    new InterpolatorTableEntry(5.02, 437.3), new InterpolatorTableEntry(5.51, 468.75),
    new InterpolatorTableEntry(6.03,468.75),new InterpolatorTableEntry(6.51,468.75)
  );
  

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
    rangeUpdatePeriod = rangeUpdatePeriod_;

    if (Constants.portConfig == PowerPortConfiguration.LOW) {
      flywheelTable = flywheelTableLow;
    } else if (Constants.portConfig == PowerPortConfiguration.HIGH) {
      flywheelTable = flywheelTableHigh;
    }


      // new InterpolatorTableEntry(1.5,343.75),
      // new InterpolatorTableEntry(1.759,343.75),
      // new InterpolatorTableEntry(2.609,375),
      // new InterpolatorTableEntry(3.34,360.1),
      // new InterpolatorTableEntry(4.76,430),
      // new InterpolatorTableEntry(5.019,467.8),
      // new InterpolatorTableEntry(6.52,503.5),
      // new InterpolatorTableEntry(8.25,531.25),
      // new InterpolatorTableEntry(8.7,562.5)
    // new InterpolatorTableEntry(1.79, 281.25), new InterpolatorTableEntry(2.35, 343.75),
        // new InterpolatorTableEntry(3.05, 343.75), new InterpolatorTableEntry(3.54, 375),
        // new InterpolatorTableEntry(3.97, 406.25), new InterpolatorTableEntry(4.56, 437.3),
        // new InterpolatorTableEntry(5.02, 437.3), new InterpolatorTableEntry(5.51, 468.75),
        // new InterpolatorTableEntry(6.03, 468.75), new InterpolatorTableEntry(6.51, 468.75));
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
    // shooter.setFlywheelVelocity(0);
  }

  public void execute() {
    //Only check every (rangeUpdatePeriod) loops
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
    }
  }

  public void end(boolean interrupted) {
  }
  
  public boolean isFinished() {
    return false;
    // return Math.abs(currentFlywheelVelocity - flywheelVelocity) <= acceptableVelocityDifference;
  }
}