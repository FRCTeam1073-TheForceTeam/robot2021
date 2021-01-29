// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TurnCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drivetrain subsystem;
  private double power;
  private double left;
  private double right;
  private double rotationAngle;
  private Pose2d initPose;
  private Pose2d currentPose;

  /**
   * Creates a new TurnCommand that makes the robot turn around its own axis for a given angle the motors working at a given power.
   *
   * @param subsystem The subsystem used by this command.
   * @param rotationAngle The angle in radians the robot will turn (+ is to the left and - is to the right).
   * @param power The power the robot's drivetrain motors should rotate itself at.
   */
  public TurnCommand(Drivetrain subsystem, double rotationAngle, double power) {
    this.subsystem = subsystem;
    this.rotationAngle = rotationAngle;
    this.power = Math.abs(power);
    addRequirements(subsystem);
  }

  /**
   * Creates a new TurnCommand that makes the robot turn around its own axis for a given angle at 50% of the motors power.
   *
   * @param subsystem The subsystem used by this command.
   * @param rotationAngle The angle in radians the robot will turn
   */
  public TurnCommand(Drivetrain subsystem, double rotationAngle) {
    this(subsystem, rotationAngle, 0.5);
  }

  double constant;

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initPose = subsystem.getRobotPose();
    if (rotationAngle < 0) {
      constant = 1;
    } else {
      constant = -1;
    }
  }

  double diffAngle;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentPose = subsystem.getRobotPose();
    diffAngle = (currentPose.getRotation().getRadians() - initPose.getRotation().getRadians());
    System.out.println(diffAngle + "," + rotationAngle);
    subsystem.setPower(power*constant, -power*constant);
    // set the speed into the drivetrain
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {subsystem.setPower(0, 0);}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(rotationAngle) <= Math.abs(diffAngle)) && (rotationAngle==0 || Math.signum(diffAngle)==Math.signum(rotationAngle));
  }
}
