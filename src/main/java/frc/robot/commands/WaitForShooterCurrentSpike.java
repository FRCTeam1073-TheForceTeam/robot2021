// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class WaitForShooterCurrentSpike extends CommandBase {

  Shooter shooter;
  double shooterThreshold = 28;
  int timer;

  public WaitForShooterCurrentSpike(Shooter shooter_) {
    shooter=shooter_;
    addRequirements(shooter);
    timer = 0;
  }

  @Override
  public boolean isFinished() {
    if (timer <= 20) {
      timer++;
    }
    return (shooter.getFlywheelCurrent()>=15 && timer>=10);
  }
}
