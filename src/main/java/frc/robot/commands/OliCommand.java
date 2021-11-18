// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class OliCommand extends CommandBase {
  WheelSubsystem wheel;
  Bling bling;

  /** Creates a new OliCommand. */
  public OliCommand(WheelSubsystem wheel, Bling bling) {
    this.wheel=wheel;
    this.bling=bling;
    addRequirements(wheel, bling);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    wheel.setPower(0);
    bling.setColor("orange");
  }

  // // Called every time the scheduler runs while the command is scheduled.
  // @Override
  // public void execute() {
  //   wheel.setPower(OI.driverController);
  //   bling.setColor("blue");
  // }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    wheel.setPower(0);
    bling.setColor("black");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
