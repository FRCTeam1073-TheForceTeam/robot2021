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
  private boolean preventInterrupt;
  
  /** This is isn't an ideal way of doing this, but just setting the commands we want to be
  un-interruptible isn't a good solution, since we want the auto-shooting command itself to
  be interruptible (just not by another AdvanceMagazineCommand). This will just mean that
  the auto-firing can't be interrupted by manual inputs from the driver or operator.*/
  private static boolean isInstanceRunning = false;
  
  /**
   * Advances magazine by a certain number of power cell diameters.
   * If the number of diameters is negative, it will run backwards.
   * 
   * @param magazine_      Magazine subsystem
   * @param magVelocity_   (Absolute value of) magazine velocity (m/s) (defaults to 1 m/s)
   * @param numPowerCells_ Number of power cells (defaults to 1)
   */
  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_, double numPowerCells_,
      int checkNumPreviousMemoryEntries, boolean preventInterrupt_) {
    magazine = magazine_;
    magVelocity = magVelocity_;
    numPowerCells = numPowerCells_;
    //If another uninterruptible command is running, other commands act as if they don't take priority.
    preventInterrupt = preventInterrupt_&&!(isInstanceRunning);
    if (preventInterrupt) {
      isInstanceRunning = true;
    }
    magVelocity = Math.abs(magVelocity) * Math.signum(numPowerCells);
    this.checkNumPreviousMemoryEntries = checkNumPreviousMemoryEntries;
    addRequirements(magazine);
  }

  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_, double numPowerCells_) {
    this(magazine_, magVelocity_, numPowerCells_, 0, false);
  }

  public AdvanceMagazineCommand(Magazine magazine_, double numPowerCells_, double magVelocity_, int checkNumPreviousMemoryEntries) {
    this(magazine_, magVelocity_, numPowerCells_, checkNumPreviousMemoryEntries, false);
  }

  public AdvanceMagazineCommand(Magazine magazine_, double magVelocity_) {
    this(magazine_, magVelocity_, 1, 0, false);
  }

  public AdvanceMagazineCommand(Magazine magazine_, double numPowerCells_, double magVelocity_, boolean preventInterrupt_) {
    this(magazine_, magVelocity_, numPowerCells_, 0, preventInterrupt_);
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
    if (preventInterrupt) {
      isInstanceRunning = false;
    }
    magazine.setVelocity(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (!preventInterrupt && isInstanceRunning) {
      return true;
    } else {
      return isFinished;
    }
  }
}
