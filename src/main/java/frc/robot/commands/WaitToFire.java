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

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (frameCounter < 2) {
      return false;
    }
    frameCounter++;
    hasData = portTracker.getPortData(portData);

    isPortTrackerAligned = hasData && (Math.abs(coordinateSeparation) <= Constants.ACCEPTABLE_PORT_TRACKER_ALIGNMENT);
    isShooterReady =
      // No matter what the difference between target and actual velocities says, there is no way the flywheel is ready to fire if isn't trying to move.
      (Math.abs(shooter.getFlywheelTargetVelocity()) != 0)
      // And same with the actual velocity (though in this case the flywheel might actually be moving for some reason so a interval centered around zero is necessary).
      && (Math.abs(shooter.getFlywheelVelocity()) < Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE)
      && (Math.abs(shooter.getFlywheelVelocity() - shooter.getFlywheelTargetVelocity()) <= Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE);

      SmartDashboard.putNumber("[WaitToFire] Get", shooter.getFlywheelVelocity());
    SmartDashboard.putNumber("[WaitToFire] Target", shooter.getFlywheelTargetVelocity());
    return (isPortTrackerAligned && isShooterReady);
  }
}
