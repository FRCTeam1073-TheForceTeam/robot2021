// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveForwardCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Drivetrain subsystem;
  private final Bling subsystem2;
  private double power;
  private double distance;
  private long initTime = 0;
  private long currentTime = 0;
  private Pose2d initPose;
  private Pose2d currentPose;

  /**
   * Creates a new DriveForwardCommand that makes the robot drive a given
   * distance.
   *
   * @param subsystem  The drivetrain used by this command.
   * @param subsystem2 The bling used by this command.
   * @param distance   The distance this command should move the robot by
   *                   (meters).
   * @param power      The power the robot's drivetrain motors run at for this
   *                   command.
   */
  public DriveForwardCommand(Drivetrain subsystem, Bling subsystem2, double distance, double power) {
    this.subsystem = subsystem;
    this.subsystem2 = subsystem2;
    this.distance = distance;
    this.power = power;
    addRequirements(subsystem);
    addRequirements(subsystem2);
  }

  /**
   * Creates a new DriveForwardCommand that drives a distance at 50% power.
   *
   * @param subsystem  The subsystem used by this command.
   * @param subsystem2 The second subsystem used by this command.
   * @param distance   The distance this command should move the robot by.
   */
  public DriveForwardCommand(Drivetrain subsystem, Bling subsystem2, double distance) {
    this(subsystem, subsystem2, distance, 0.5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // initTime = System.currentTimeMillis(); // TODO: calculations with velocity
    // and distance and convert to internal unit for time - Bling?
    initPose = subsystem.getRobotPose();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // currentTime = System.currentTimeMillis(); // TODO: set the velocity into the
    // drivetrain in an if loop of calculated time - Bling?
    currentPose = subsystem.getRobotPose();
    subsystem.setPower(power, power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return distance <= currentPose.getTranslation().getDistance(initPose.getTranslation());
  }
}
