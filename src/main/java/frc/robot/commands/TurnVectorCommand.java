// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;

/** An example command that uses an example subsystem. */
public class TurnVectorCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Drivetrain drivetrain;
  private final Bling bling;
  private final double angleToTurn;
  private final double maxSpeed;
  private Vector2d endVector;
  private Vector2d endVector90off;
  private Vector2d vector;
  private double initAngle;
  private double endAngle;
  private double angle;
  private double dot;
  private double sign;
  private double sign2;
  private double speedMultiplier;

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
  public TurnVectorCommand(Drivetrain drivetrain, Bling bling, double angleToTurn, double maxSpeed) {
    this.drivetrain = drivetrain;
    this.bling = bling;
    this.angleToTurn = angleToTurn;
    this.maxSpeed = Math.abs(maxSpeed);
    addRequirements(drivetrain);
    addRequirements(bling);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initAngle = drivetrain.getRobotPose().getRotation().getRadians();
    endAngle = MathUtil.angleModulus(initAngle + angleToTurn);
    endVector = new Vector2d(Math.cos(endAngle), Math.sin(endAngle));
    sign = Math.signum(angleToTurn);
    endVector90off = new Vector2d(Math.cos(endAngle + sign * 0.5 * Math.PI), Math.sin(endAngle + sign * 0.5 * Math.PI));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    angle = drivetrain.getRobotPose().getRotation().getRadians();
    vector = new Vector2d(Math.cos(angle), Math.sin(angle));
    dot = vector.dot(endVector);
    sign2 = Math.signum(vector.dot(endVector90off));
    if (sign != sign2 && sign2 != 0.0) {
      sign = sign2;
    }
    speedMultiplier = sign * Math.abs((angleToTurn + initAngle - angle) / angleToTurn);
    drivetrain.setVelocity(0.0, speedMultiplier * maxSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setVelocity(0.0, 0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return dot >= 0.8;
  }
}