// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Bling;

public class SetBlingCommand extends CommandBase {
  Bling bling;
  double duration;
  String color;

  double startTime;

  /** Creates a new PulseBlingCommand. */
  public SetBlingCommand(Bling bling, String color) {
    this.bling = bling;
    this.color = color;
    addRequirements(bling);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    bling.setColor("black");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    bling.setColor(color);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
