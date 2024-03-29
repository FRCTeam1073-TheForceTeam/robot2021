// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Utility;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;

/** An example command that uses an example subsystem. */
public class TurnToHeading extends CommandBase {
  private final Drivetrain drivetrain;
  private final Bling bling;
  private final double maxSpeed;
  private double targetHeading;
  private double angleToTurn;

  private double initAngle;
  private double angleTurned;
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
  public TurnToHeading(Drivetrain drivetrain, Bling bling, double targetHeading, double maxSpeed) {
    this.drivetrain = drivetrain;
    this.bling = bling;
    this.targetHeading = targetHeading;
    angleToTurn = 0; //If something goes wrong, the robot just shouldn't turn at all.
    this.maxSpeed = Math.abs(maxSpeed);
    addRequirements(drivetrain);
    addRequirements(bling);
  }

  /**
   * Creates a new TurnCommand that makes the robot turn around its own axis for a
   * given angle with a max speed of 0.25 radians per second.
   *
   * @param subsystem   The subsystem used by this command.
   * @param AngletoTurn The angle in radians the robot will turn
   */
  public TurnToHeading(Drivetrain drivetrain, Bling bling, double targetHeading) {
    this(drivetrain, bling, targetHeading, 1.5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initAngle = drivetrain.getRobotPose().getRotation().getRadians();
    angleToTurn = MathUtil.angleModulus(targetHeading - initAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    angleTurned = MathUtil.angleModulus(drivetrain.getRobotPose().getRotation().getRadians() - initAngle);
    double angleRemaining = (angleToTurn - angleTurned);
    speed = maxSpeed * Math.signum(angleRemaining)
        * Math.min(Math.pow(Math.abs(angleRemaining / angleToTurn), 0.5) + 0.4, 1);
    drivetrain.setVelocity(0.0, speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setVelocity(0.0, 0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (angleToTurn - angleTurned) * Math.signum(angleToTurn) <= 0.1;
  }
} 