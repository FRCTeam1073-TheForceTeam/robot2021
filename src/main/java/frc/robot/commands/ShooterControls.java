// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.Shooter;

public class ShooterControls extends CommandBase {
  Shooter shooter;
  double flywheelVelocity;
  double hoodAngle;
  int currDPadState;
  int prevDPadState;

  public ShooterControls(Shooter shooter) {
    this.shooter = shooter;
    addRequirements(shooter);
    flywheelVelocity = 0;
    hoodAngle = shooter.hoodAngleHigh;
    currDPadState = -1;
    prevDPadState = -1;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // flywheelVelocity = shooter.getFlywheelTargetVelocity();
    hoodAngle = shooter.getHoodAngle();
    // shooter.setHoodPower(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currDPadState = OI.operatorController.getPOV();

    flywheelVelocity = shooter.getFlywheelTargetVelocity();

    if (prevDPadState == -1) {
      if (currDPadState == 90) {
        flywheelVelocity += 31.25 * 0.5;
      }
      if (currDPadState == 270) {
        flywheelVelocity -= 31.25 * 0.5;
      }
      if (currDPadState == 180) {
        hoodAngle += 0.025 * 0.5;
      }
      if (currDPadState == 0) {
        hoodAngle -= 0.025 * 0.5;
      }
    }

    prevDPadState = currDPadState;

    flywheelVelocity = Math.max(0, flywheelVelocity);

    shooter.setFlywheelVelocity(flywheelVelocity);
    shooter.setHoodAngle(hoodAngle);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      flywheelVelocity = shooter.getFlywheelVelocity();
      hoodAngle = shooter.getHoodAngle();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
