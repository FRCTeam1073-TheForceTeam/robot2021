// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TurnCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Drivetrain drivetrain;
  private final Bling bling;
  private final double maxSpeed;
  private final double angleToTurn;
  private Rotation2d initRotation;
  private Rotation2d endRotation;
  private Rotation2d endRotationRotateBy;
  private Rotation2d currentRotation;
  private double angleTurned;
  private double angleRemaining;
  private double speed;

  /**
   * Creates a new TurnCommand that makes the robot turn around its own axis for a
   * given angle the motors working at a given speed.
   *
   * @param drivetrain  The drivetrain used by this command.
   * @param bling       The bling used by this command.
   * @param AngletoTurn The angle in radians the robot will turn (+ is to the left
   *                    and - is to the right).
   * @param maxSpeed    The max speed the robot's drivetrain should rotate at in
   *                    radians per second.
   */
  public TurnCommand(Drivetrain drivetrain, Bling bling, double angleToTurn, double maxSpeed) {
    this.drivetrain = drivetrain;
    this.bling = bling;
    this.angleToTurn = angleToTurn;
    this.maxSpeed = Math.abs(maxSpeed);
    addRequirements(drivetrain);
    addRequirements(bling);
    SmartDashboard.putNumber("[Turn] Angle Remaining", angleRemaining);
    SmartDashboard.putNumber("[Turn] Current Angle", drivetrain.getRobotPose().getRotation().getRadians());
    SmartDashboard.putNumber("[Turn] Speed", speed);
  }

  /**
   * Creates a new TurnCommand that makes the robot turn around its own axis for a
   * given angle with a max speed of 0.25 radians per second.
   *
   * @param subsystem   The subsystem used by this command.
   * @param AngletoTurn The angle in radians the robot will turn
   */
  public TurnCommand(Drivetrain drivetrain, Bling bling, double AngletoTurn) {
    this(drivetrain, bling, AngletoTurn, 1.5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Rotation2d rotationToDo = new Rotation2d(angleToTurn);
    endRotation = drivetrain.getRobotPose().getRotation();
    endRotation = endRotation.plus(rotationToDo);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentRotation = drivetrain.getRobotPose().getRotation();
    currentRotation = endRotation.minus(currentRotation);
    angleRemaining = currentRotation.getRadians();
    System.out.println("R" + angleRemaining);

    if (angleRemaining > Math.PI / 6 || angleRemaining < -Math.PI / 6) {
      speed = maxSpeed * Math.cbrt(angleRemaining / Math.PI);
    } else if (angleRemaining >= 0) {
      speed = maxSpeed * Math.sqrt(1.415 * angleRemaining);
    } else {
      speed = maxSpeed * -Math.sqrt(1.415 * angleRemaining);
    }

    System.out.println("TRYING TO TURN AT " + speed + " rad/sec");
    drivetrain.setVelocity(0.0, speed);
    SmartDashboard.putNumber("[Turn] Angle Remaining", angleRemaining);
    SmartDashboard.putNumber("[Turn] Current Angle", currentRotation.getRadians());
    SmartDashboard.putNumber("[Turn] Speed", speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setVelocity(0.0, 0.0);
    System.out.println("!!!TurnAngle: " + angleTurned);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (angleRemaining <= 0.2);
  }
}