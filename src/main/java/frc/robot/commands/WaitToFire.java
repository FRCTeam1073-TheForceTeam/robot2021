// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class WaitToFire extends CommandBase {
  Shooter shooter;
  PowerPortTracker portTracker;

  boolean isPortTrackerAligned;
  boolean isShooterReady;

  double coordinateSeparation;
  boolean hasData;

  PowerPortData portData;

  boolean firstFrame = true;

  public WaitToFire(Shooter shooter_, PowerPortTracker portTracker_) {
    shooter = shooter_;
    portTracker = portTracker_;
    isPortTrackerAligned = false;
    isShooterReady = false;
    portData = new PowerPortData();
    coordinateSeparation = 0;
    hasData = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (firstFrame) {
      firstFrame = false;
      return false;
    }
    hasData = portTracker.getPortData(portData);
    if (hasData) {
      coordinateSeparation = 1 - 2 * (((double) portData.cx) / ((double) portTracker.getImageWidth()));
    }

    isPortTrackerAligned = hasData && (Math.abs(coordinateSeparation) <= Constants.ACCEPTABLE_PORT_TRACKER_ALIGNMENT);
    isShooterReady = Math.abs(shooter.getFlywheelVelocity() - shooter.getFlywheelTargetVelocity()) <= Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE;

    SmartDashboard.putNumber("[WaittoFire] get", shooter.getFlywheelVelocity());
    SmartDashboard.putNumber("[WaittoFire] target", shooter.getFlywheelTargetVelocity());
    return (isPortTrackerAligned && isShooterReady);
  }
}
