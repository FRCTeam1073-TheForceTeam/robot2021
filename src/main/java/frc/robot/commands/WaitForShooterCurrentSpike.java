// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.filters.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Shooter;

public class WaitForShooterCurrentSpike extends CommandBase {

  Shooter shooter;
  int timer;
  LinearFilter nearFilter;
  LinearFilter farFilter;
  LinearFilter hoodPositionFilter;
  boolean finish;

  public WaitForShooterCurrentSpike(Shooter shooter_) {
    this(shooter_, true);
  }

  public WaitForShooterCurrentSpike(Shooter shooter_, boolean finish_) {
    shooter = shooter_;
    addRequirements(shooter);
    nearFilter = LinearFilter.singlePoleIIR(0.1, Robot.kDefaultPeriod);
    farFilter = LinearFilter.singlePoleIIR(0.8, Robot.kDefaultPeriod);
    hoodPositionFilter = LinearFilter.singlePoleIIR(1.5, Robot.kDefaultPeriod);
    finish = finish_;
  }

  public void initialize() {
    timer = 0;
  }

  @Override
  public boolean isFinished() {
    double current = shooter.getFlywheelCurrent();
    double nearVal = nearFilter.calculate(current);
    double farVal = farFilter.calculate(current);
    double smoothHoodPos = hoodPositionFilter.calculate(shooter.getHoodPosition());
    double hoodVelocity = shooter.getHoodVelocity();
    SmartDashboard.putNumber("[ShotDetection] Hood velocity (rads/s) [0]", hoodVelocity);
    double diff = (nearVal - farVal);
    double hoodDiff = shooter.getHoodPosition() - smoothHoodPos;

    if (timer <= 15) {
      timer++;
      return false;
    }
    return (Math.abs(hoodVelocity) >= 5.0);

    // if (hoodDiff >= 4.5 && timer >= 15) {
    //   return finish;
    // }
    // return false;
  }
}
