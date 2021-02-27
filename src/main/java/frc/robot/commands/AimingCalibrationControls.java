// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.ctre.phoenix.Util;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import frc.robot.Utility;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class AimingCalibrationControls extends CommandBase {


  Shooter shooter;
  Magazine magazine;
  PowerPortTracker portTracker;
  PowerPortData portData;
  Drivetrain drivetrain;

  double targetFlywheelVelocity;
  double targetHoodAngle;
  double currentRange;
  double initRange;
  double recordedFlywheelVelocity;
  double recordedHoodAngle;
  double recordedRange;

  /**
   * The controlliest teleop controls I could come up with.
   * It's for testing the aiming, and it uses four different subsystems (five if you count the hood and flywheel separately).
   * @param shooter_ The shooter subsystem
   * @param magazine_ The magazine subsystem
   * @param portTracker_ The power port tracker subsystem
   * @param drivetrain_ The drivetrain subsystem
   * @param initRange_ The starting range (in meters)
   */

  public AimingCalibrationControls(Shooter shooter_, Magazine magazine_, PowerPortTracker portTracker_, Drivetrain drivetrain_, double initRange_) {
    shooter = shooter_;
    magazine = magazine_;
    portTracker = portTracker_;
    targetFlywheelVelocity = 0;
    targetHoodAngle = shooter.hoodAngleHigh;
    recordedFlywheelVelocity = 0;
    recordedHoodAngle = 0;
    recordedRange = 0;
    drivetrain = drivetrain_;
    initRange = initRange_;
    portData = new PowerPortData();
    addRequirements(shooter, magazine, portTracker, drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    magazine.setVelocity(0);
    shooter.setFlywheelPower(0);
    shooter.lowerHood();
    drivetrain.resetRobotOdometry();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // Controls:
    //  Driver controls:
    //    Drive controls are standard (left Y to go forward/backward, right X to turn, start and back at same time to reset odometry)
    //  Operator controls (*inhales*):
    //    Flywheel controls:
    //      A button (on press) to increment flywheel speed by 20 radians/sec
    //      B button (on press) to decrement flyhweel speed by 20 radians/sec
    //      Left stick Y to change the flywheel speed by 10 radians/sec^2
    //      X button (on press) to set flywheel speed to zero (with slew rate limiter)
    //      Start button (on press) to instantly stop the flywheel
    //    Hood controls:
    //      Left bumper to increment the hood angle by 0.1 radians (retracts the turret to get a steeper angle)
    //      Right bumper to decrement the hood angle by 0.1 radians (extends the turret to get a shallower angle)
    //      Right stick Y to change the hood angle by 0.05 radians/sec (that's like 3 degrees/sec)
    //    Magazine controls:
    //      Y button (on hold) to move the magazine at 2 m/s upwards (does not move otherwise)
    //    Meta controls:
    //      Back button (on press) to record the current hood angle, flywheel velocity, and range (will be displayed on SmartDashboard)
    
    double multiplier = Math.exp(-Constants.THROTTLE_FALLOFF * Utility.deadzone(OI.driverController.getRawAxis((3))));
    double forward = Utility.deadzone(-OI.driverController.getRawAxis(1)) * 2.0 * multiplier;
    double rotation = Utility.deadzone(OI.driverController.getRawAxis(4)) * 2.0 * multiplier;
    drivetrain.setVelocity(forward, rotation);
    if ((OI.driverController.getStartButtonPressed() && OI.driverController.getBackButton()) || (OI.driverController.getStartButton() && OI.driverController.getBackButtonPressed())) {
        drivetrain.resetRobotOdometry();
    }
    
    boolean hasData = portTracker.getPortData(portData);

    if (hasData) {
      currentRange = portTracker.getRange();
    }

    if (OI.operatorController.getAButtonPressed()) {
      targetFlywheelVelocity += 20.0;
    } else if (OI.operatorController.getBButtonPressed()) {
      targetFlywheelVelocity -= 20.0;
    } else {
      targetFlywheelVelocity += 10.0 * (Utility.deadzone(OI.operatorController.getRawAxis(5)) / 50.0);
      //Changes velcoity by 10 radians/sec^2
    }

    if (OI.operatorController.getXButtonPressed()) {
      targetFlywheelVelocity = 0;
    }

    if (OI.operatorController.getStartButtonPressed()) {
      targetFlywheelVelocity = 0;
      shooter.stop();
    }

    if (OI.operatorController.getYButton()) {
      magazine.setVelocity(2);
    } else {
      magazine.setVelocity(0);
    }

    targetFlywheelVelocity = MathUtil.clamp(targetFlywheelVelocity, 0, Constants.MAXIMUM_SAFE_FLYHWEEL_SPEED * 0.9);

    if (OI.operatorController.getBumperPressed(Hand.kRight)) {
      targetHoodAngle -= 0.1;
    } else if (OI.operatorController.getBumperPressed(Hand.kLeft)) {
      targetHoodAngle += 0.1;
    } else {
      //Changes angle by 0.1 radians/sec
      targetHoodAngle += 0.05 * (Utility.deadzone(OI.operatorController.getRawAxis(1)) / 50.0);
    }
    targetHoodAngle = MathUtil.clamp(targetHoodAngle, shooter.hoodAngleLow, shooter.hoodAngleHigh);

    if (OI.operatorController.getBackButtonPressed()) {
      recordedFlywheelVelocity = targetFlywheelVelocity;
      recordedHoodAngle = targetHoodAngle;
      recordedRange = currentRange;
    }

    shooter.setFlywheelVelocity(targetFlywheelVelocity);
    shooter.setHoodAngle(targetHoodAngle);

    SmartDashboard.putNumber("[AimingCalibrationControls] Flywheel velocity (radians/second)",
        shooter.getFlywheelVelocity());
    SmartDashboard.putNumber("[AimingCalibrationControls] Hood angle (radians)",
        shooter.getHoodAngle());
    if (hasData) {
      SmartDashboard.putNumber("[AimingCalibrationControls] PowerPortTracker range", currentRange);
      SmartDashboard.putNumber("[AimingCalibrationControls] Odometry range (meters)",
          drivetrain.getRobotPose().getX() + initRange);
    }
    SmartDashboard.putNumber("[AimCalibControls-Record] Recorded flywheel velocity (radians/sec)",
        recordedFlywheelVelocity);
    SmartDashboard.putNumber("[AimCalibControls-Record] Recorded hood angle (radians)",
        recordedHoodAngle);
    SmartDashboard.putNumber("[AimCalibControls-Record] Recorded range (meters)",
        recordedRange);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    magazine.setVelocity(0);
    shooter.setFlywheelPower(0);
    shooter.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
