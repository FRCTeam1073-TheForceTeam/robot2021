// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class TestCommandIvan extends CommandBase 
{
  private WheelSubsystem wheel;
  private Bling bling;

  /** Creates a new TestCommand. */
  public TestCommandIvan(WheelSubsystem wheel, Bling bling) 
  {
    this.wheel = wheel;
    this.bling = bling;
    addRequirements(wheel, bling);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return false;
  }
}
