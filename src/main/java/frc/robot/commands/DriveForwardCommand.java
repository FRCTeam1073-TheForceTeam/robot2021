// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveForwardCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drivetrain subsystem;
  private double velocity;
  private double distance;
  private int time;
  private double initRotation;
  private double currentRotation;

  /**
   * Creates a new DriveForwardCommand that makes the robot drive a given distance at a given velocity.
   *
   * @param subsystem The subsystem used by this command.
   * @param distance The distance this command should move the robot by.
   * @param velocity The velocity of the robot for this command.
   */
  public DriveForwardCommand(Drivetrain subsystem, double distance, double velocity) {
    this.subsystem = subsystem;
    this.distance = distance;
    this.velocity = velocity;
    addRequirements(subsystem);
  }

  /**
   * Creates a new DriveForwardCommand that drives a distance at maximum velocity.
   *
   * @param subsystem The subsystem used by this command.
   * @param distance The distance this command should move the robot by.
   */
  public DriveForwardCommand(Drivetrain subsystem, double distance) {
    this(subsystem, distance, 0.0); // need a constant from Constant.java for the maximum velocity
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    time = 0; //Do calculations with velocity and distance and convert to internal unit for time
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // set the velocity into the drivetrain in an if loop of calculated time
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
