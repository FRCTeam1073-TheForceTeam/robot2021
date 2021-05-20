// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class AlignMagazineLoadCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Magazine magazine;
  private final Shooter shooter;
  private final Bling bling;

  /**
   * Creates a new AlignMagazineLoadCommand that makes the robot aling all the
   * powercells in the magazine (all powercells will be laying in a line at the
   * top end of the magazine once this command finished).
   *
   * @param magazine The magazine used by this command.
   * @param shooter  The shooter used by this command.
   * @param bling    The bling used by this command.
   */
  public AlignMagazineLoadCommand(Magazine magazine, Shooter shooter, Bling bling) {
    this.magazine = magazine;
    this.shooter = shooter;
    this.bling = bling;
    addRequirements(magazine);
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // TODO: set the deadzoneroller to running downwards with Bling
    // TODO: set the magazine to running upwards with Bling
    // TODO: end after an experimentally determined time
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
