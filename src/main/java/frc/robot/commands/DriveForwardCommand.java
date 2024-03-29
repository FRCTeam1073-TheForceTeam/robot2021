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
  private final Drivetrain drivetrain;
  private final Bling bling;
  private double velocity;
  private double distance;
  private double currentDistance;
  private Pose2d initPose;
  private Pose2d currentPose;

  /**
   * Creates a new DriveForwardCommand that makes the robot drive a given
   * distance.
   *
   * @param drivetrain The drivetrain used by this command.
   * @param bling      The bling used by this command.
   * @param distance   The distance this command should move the robot by.
   * @param velocity   The velocity the robot's drivetrain motors run at for this
   *                   command.
   */
  public DriveForwardCommand(Drivetrain drivetrain, Bling bling, double distance, double velocity) {
    this.drivetrain = drivetrain;
    this.bling = bling;
    this.distance = distance;
    this.velocity = velocity;
    addRequirements(drivetrain);
    addRequirements(bling);
  }

  /**
   * Creates a new DriveForwardCommand that drives a distance at 1 m/s.
   *
   * @param drivetrain The drivetrain used by this command.
   * @param bling      The bling used by this command.
   * @param distance   The distance this command should move the robot by.
   */
  public DriveForwardCommand(Drivetrain drivetrain, Bling bling, double distance) {
    this(drivetrain, bling, distance, 0.5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    bling.setArray("blue");
    bling.setSlot(1, bling.rgbArr[0], bling.rgbArr[1], bling.rgbArr[2]);
    initPose = drivetrain.getRobotPose();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentPose = drivetrain.getRobotPose();
    drivetrain.setVelocity(velocity, 0.0);
    currentDistance = Math.hypot(currentPose.getX() - initPose.getX(), currentPose.getY() - initPose.getY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setVelocity(0.0, 0.0);
    bling.setColorRGBAll(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return distance <= currentDistance;
  }
}
