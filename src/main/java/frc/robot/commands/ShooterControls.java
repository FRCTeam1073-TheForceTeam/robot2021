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
    shooter.setFlywheelPower(0);
    shooter.lowerHood();
    // shooter.setHoodPower(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currDPadState = OI.operatorController.getPOV();

    if (currDPadState == 90 && prevDPadState == -1) {
      flywheelVelocity += 31.25;
    }
    if (currDPadState == 270 && prevDPadState == -1) {
      flywheelVelocity -= 31.25;
    }
    if (currDPadState == 180 && prevDPadState == -1) {
      hoodAngle += 0.025;
    }
    if (currDPadState == 0 && prevDPadState == -1) {
      hoodAngle -= 0.025;
    }

    if (OI.operatorController.getBButtonPressed()) {
      flywheelVelocity = 0;
      hoodAngle = shooter.hoodAngleHigh;
    }

    
    prevDPadState = currDPadState;

    flywheelVelocity = Math.max(0, flywheelVelocity);

    shooter.setFlywheelVelocity(flywheelVelocity);
    shooter.setHoodAngle(hoodAngle);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.setFlywheelPower(0);
    shooter.lowerHood();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
