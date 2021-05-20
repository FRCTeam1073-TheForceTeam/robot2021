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

  int frameCounter;

  double initFlywheelTargetVelocity = 0;

  public WaitToFire(Shooter shooter_, PowerPortTracker portTracker_) {
    shooter = shooter_;
    portTracker = portTracker_;
    isPortTrackerAligned = false;
    isShooterReady = false;
    portData = new PowerPortData();
    coordinateSeparation = 0;
    frameCounter = 0;
    hasData = false;
  }

  @Override
  public void initialize() {
    initFlywheelTargetVelocity = shooter.getFlywheelTargetVelocity();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (frameCounter < 2) {
      frameCounter++;
      return false;
    }
    hasData = portTracker.getPortData(portData);

    isPortTrackerAligned = hasData && (Math.abs(coordinateSeparation) <= Constants.ACCEPTABLE_PORT_TRACKER_ALIGNMENT);
    isShooterReady =
      // Checks to see if flywheel target velocity is any different (even if we line up perfectly, the chances of the shooter deciding on literally the
      // exact same flywheel velocity are very low.)
      (Math.abs(shooter.getFlywheelTargetVelocity()) != initFlywheelTargetVelocity) && (Math.abs(shooter.getFlywheelTargetVelocity()) != 0)
      // Checks to see if the shooter is doing reasonable things (that it isn't crazy slow and that it's close enough to the target velocity)
      && (Math.abs(shooter.getFlywheelVelocity()) > Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE)
      && (Math.abs(shooter.getFlywheelVelocity() - shooter.getFlywheelTargetVelocity()) <= Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE);

    SmartDashboard.putNumber("[WaitToFire] Get", shooter.getFlywheelVelocity());
    SmartDashboard.putNumber("[WaitToFire] Target", shooter.getFlywheelTargetVelocity());
    SmartDashboard.putNumber("[WaitToFire] CSep", coordinateSeparation);
    SmartDashboard.putBoolean("[WaitToFire] iPTA", isPortTrackerAligned);
    SmartDashboard.putBoolean("[WaitToFire] iSR", isShooterReady);
    return (isPortTrackerAligned && isShooterReady);
  }
}