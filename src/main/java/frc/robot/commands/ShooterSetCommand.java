// Copyright (c) FIRST and other WPILib contributors.
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class ShooterSetCommand extends CommandBase {

  Shooter shooter;
  double hoodAngle;
  double flywheelVelocity;
  double currentFlywheelVelocity;
  int ctr;

  public ShooterSetCommand(Shooter shooter_, double hoodAngle_, double flywheelVelocity_) {
    shooter = shooter_;
    hoodAngle = hoodAngle_;
    flywheelVelocity = flywheelVelocity_;
    currentFlywheelVelocity = 0;
    addRequirements(shooter);
  }
  
  @Override
  public void initialize() {
    shooter.setHoodAngle(hoodAngle);
    ctr = 0;
  }

  public void execute() {
    shooter.setHoodAngle(hoodAngle);
    shooter.setFlywheelVelocity(flywheelVelocity);
    currentFlywheelVelocity = shooter.getFlywheelVelocity();
  }

  public void end(boolean interrupted) {
    shooter.setHoodAngle(hoodAngle);
    SmartDashboard.putString("SSC", "Ended");
  }
  
  public boolean isFinished() {
    ctr++;
    return (ctr>2)&&(Math.abs(shooter.getHoodAngle()-hoodAngle)<0.05)&&(Math.abs(currentFlywheelVelocity - flywheelVelocity) <= Constants.ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE);
  }
}