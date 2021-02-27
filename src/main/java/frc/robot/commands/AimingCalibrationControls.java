// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
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

  double targetFlywheelVelocity;
  double targetHoodAngle;

  public AimingCalibrationControls(Shooter shooter_, Magazine magazine_, PowerPortTracker portTracker_) {
    shooter = shooter_;
    magazine = magazine_;
    portTracker = portTracker_;
    targetFlywheelVelocity = 0;
    targetHoodAngle = shooter.hoodAngleHigh;
    portData = new PowerPortData();
    addRequirements(shooter, magazine, portTracker);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    magazine.setVelocity(0);
    shooter.setFlywheelPower(0);
    shooter.lowerHood();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean hasData = portTracker.getPortData(portData);

    if (OI.operatorController.getAButtonPressed()) {
      targetFlywheelVelocity -= 20;
    } else if (OI.operatorController.getBButtonPressed()) {
      targetFlywheelVelocity += 20;
    } else {
      targetFlywheelVelocity += 50 * (OI.operatorController.getRawAxis(5) / 50.0);
    }

    if (OI.operatorController.getXButtonPressed()) {
      targetFlywheelVelocity = 0;
    }

    if (OI.operatorController.getYButton()) {
      magazine.setVelocity(2);
    } else {
      magazine.setVelocity(0);
    }

    targetFlywheelVelocity = MathUtil.clamp(targetHoodAngle, 0, 400);

    if (OI.operatorController.getBumperPressed(Hand.kLeft)) {
      targetHoodAngle -= 0.1;
    } else if (OI.operatorController.getBumperPressed(Hand.kRight)) {
      targetHoodAngle += 0.1;
    }
    targetHoodAngle = MathUtil.clamp(targetHoodAngle, shooter.hoodAngleLow, shooter.hoodAngleHigh);

    SmartDashboard.putNumber("[AimingCalibrationControls] Flywheel velocity (radians/second)",
        shooter.getFlywheelVelocity());
    SmartDashboard.putNumber("[AimingCalibrationControls] Hood angle (radians)",
        shooter.getHoodAngle());
    if (hasData) {
      SmartDashboard.putNumber("[AimingCalibrationControls] PowerPortTracker range", portTracker.getRange());
    }
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
