// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Magazine;

public class AdvanceMagazineCommand extends CommandBase {
  /** Creates a new AdvancedMagazineCommand. */
  private Magazine magazine;
  private double magVelocity;
  private double initPosition;
  private double distanceTraveled;
  private double numPowerCells;
  private int checkNumPreviousMemoryEntries;
  private boolean isFinished;

  /**
   * Advances magazine by a certain number of power cell diameters.
   * If the number of diameters is negative, it will run backwards.
   * 
   * @param magazine_      Magazine subsystem
   * @param magVelocity_   (Absolute value of) magazine velocity (m/s) (defaults to 1 m/s)
   * @param numPowerCells_ Number of power cells (defaults to 1)
   */
  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_, double numPowerCells_,
      int checkNumPreviousMemoryEntries) {
    magazine = magazine_;
    magVelocity = magVelocity_;
    numPowerCells = numPowerCells_;
    magVelocity = Math.abs(magVelocity) * Math.signum(numPowerCells);
    this.checkNumPreviousMemoryEntries = checkNumPreviousMemoryEntries;
    addRequirements(magazine);
  }

  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_, double numPowerCells_) {
    this(magazine_, magVelocity_, numPowerCells_, 0);
  }

  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_) {
    this(magazine_, magVelocity_, 1, 0);
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
    isFinished = false;
    if (checkNumPreviousMemoryEntries > 0
        && !RobotContainer.memory.havePreviousFinished(checkNumPreviousMemoryEntries)) {
      isFinished = true;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    distanceTraveled = (magazine.getPosition() - initPosition);
    magazine.setVelocity(magVelocity);
    isFinished = (Math.signum(numPowerCells) * distanceTraveled >= Math.signum(numPowerCells) * Constants.POWER_CELL_DIAMETER * numPowerCells);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("#############################################");
    System.out.println("#############################################");
    System.out.println("#############################################");
    magazine.setVelocity(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
