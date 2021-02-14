// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Magazine;

public class AdvanceMagazineCommand extends CommandBase {
  /** Creates a new AdvancedMagazineCommand. */
  Magazine magazine;
  double magVelocity;
  double initPosition;
  double distanceTraveled;
  double numPowerCells;

  /**
   * Advances magazine by a certain number of power cell diameters.
   * @param magazine_ Magazine subsystem
   * @param magVelocity_ Magazine velocity (m/s) (defaults to 1 m/s)
   * @param numPowerCells_ Number of power cells (defaults to 1)
   */
  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_, double numPowerCells_) {
    magazine = magazine_;
    magVelocity = magVelocity_;
    numPowerCells = numPowerCells_;
    addRequirements(magazine);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_) {
    this(magazine_, magVelocity_, 1);
  }

  public AdvanceMagazineCommand(Magazine magazine_) {
    this(magazine_, 0.15);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initPosition = magazine.getPosition();
    distanceTraveled = 0;
    magazine.setVelocity(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    magazine.setVelocity(magVelocity);
    System.out.println(distanceTraveled+" [AAAAAAAAAA] "+ Constants.POWER_CELL_DIAMETER * numPowerCells);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    magazine.setVelocity(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    distanceTraveled = (magazine.getPosition() - initPosition);
    SmartDashboard.putNumber("y [MAGCTL]", distanceTraveled);
    return (distanceTraveled >= Constants.POWER_CELL_DIAMETER * numPowerCells);
  }
}
