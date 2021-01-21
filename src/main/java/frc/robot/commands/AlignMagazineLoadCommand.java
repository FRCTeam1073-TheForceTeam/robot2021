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
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Magazine subsystem;
  private final Shooter subsystem2;
  private final Bling subsystem3;

  /**
   * Creates a new AlignMagazineLoadCommand that makes the robot drive a given distance at a given velocity.
   *
   * @param subsystem The subsystem used by this command.
   * @param distance The distance this command should move the robot by.
   * @param velocity The velocity of the robot for this command.
   */
  public AlignMagazineLoadCommand(Magazine subsystem, Shooter subsystem2, Bling subsystem3) {
    this.subsystem = subsystem;
    this.subsystem2 = subsystem2;
    this.subsystem3 = subsystem3;
    addRequirements(subsystem);
    addRequirements(subsystem2);    
    addRequirements(subsystem3);
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // TODO: set the deadzoneroller to running downwards with Bling
    // TODO: set the magazine to running upwards with Bling
    // TODO: end after an experimentally determined time
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
