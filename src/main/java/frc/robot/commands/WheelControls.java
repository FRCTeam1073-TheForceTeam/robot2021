// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.WheelSubsystem;

public class WheelControls extends CommandBase {

  //Subsytems objects go here:
  WheelSubsystem wheel;

  /** Creates a new TestCommand object. */
  public WheelControls(WheelSubsystem wheel_) {
    wheel = wheel_;
    addRequirements(wheel);
  }

  @Override
  public void initialize() {
    // Code that runs (once) when the command starts:

  }

  @Override
  public void execute() {
    wheel.setPower(OI.driverController.getRawAxis(1));
  }

  @Override
  public void end(boolean interrupted) {
    // Code that runs (once) when the command ends:
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Returns true when the command is supposed to end (having 'return false' causes it to run forever):
    return false;
  }
}
