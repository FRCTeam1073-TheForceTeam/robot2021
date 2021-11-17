// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Bling;

public class FlashBlingCommand extends CommandBase {
  Bling bling;
  double duration;
  String color1, color2;

  double startTime;

  public FlashBlingCommand(Bling bling, String color1) {
    this(bling, color1, "black");
  }

  public FlashBlingCommand(Bling bling, String color1, String color2) {
    this.bling = bling;
    this.color1 = color1;
    this.color2 = color2;
    addRequirements(bling);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    bling.setColor("black");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    int i = (int) (10.0 * (Timer.getFPGATimestamp() - startTime));
    SmartDashboard.putNumber("i", i);
    bling.setColor((i % 2 == 0) ? color1 : color2);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    bling.setColor("black");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
