// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.OI;

public class MagazineControls extends CommandBase {

  private Magazine magazine;
  private double magazineVelocity;

  /** Creates a new MagazineControls. */
  public MagazineControls(Magazine magazine_) {
    magazine = magazine_;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(magazine);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    magazineVelocity = OI.operatorController.getRawAxis(1);
    SmartDashboard.putNumber("Magazine Power", magazineVelocity);
    magazine.setPower(magazineVelocity);
    // magazine.setVelocity(magazineVelocity);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}