// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Bling;

public class BlingTestCommand extends CommandBase {
  Bling bling;
  int ctr;

  public BlingTestCommand(Bling bling_) {
    bling = bling_;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ctr = 0;
    bling.setColor("black");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    float time = ((float) RobotController.getFPGATime()) * 1.0e-6f;
    for (int i = 0; i < 8; i++) {
      // if((ctr>>i)%2==1){
        bling.setLEDDouble(
          i,
            0.4 * Math.exp(-2 * Math.pow(i - 8 * Math.pow(Math.sin(time * 3.0+0.1),2),2)),
            0.4 * Math.exp(-2 * Math.pow(i - 8 * Math.pow(Math.sin(time * 3.0),2),2)),
            0.4 * Math.exp(-2 * Math.pow(i - 8 * Math.pow(Math.sin(time * 3.0-0.2),2),2))
        );
      // } else {
        // bling.setLEDDouble(i, 0, 0, 0);
      // }
    }
    ctr++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
