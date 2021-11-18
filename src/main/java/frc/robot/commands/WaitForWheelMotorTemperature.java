// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.WheelSubsystem;

/**
 * Wait command that ends when the wheel motor's temperature exceeds a certain value.
 */
public class WaitForWheelMotorTemperature extends CommandBase {

  WheelSubsystem wheel;
  double temperature;

  /** Creates a new WaitForMotorTemperature. */
  public WaitForWheelMotorTemperature(double temperature, WheelSubsystem wheel) {
    this.wheel = wheel;
    this.temperature = temperature;
    /* This command doesn't require the wheel subsystem, because it is supposed to run while
    the wheel is being used by another command.*/
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (wheel.getMotorTemperature() >= temperature);
  }
}
