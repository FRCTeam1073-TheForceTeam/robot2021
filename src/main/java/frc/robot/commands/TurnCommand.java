// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;

/** An example command that uses an example subsystem. */
public class TurnCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Drivetrain drivetrain;
  private final Bling bling;
  private double speed;
  private final double maxSpeed;
  private double AngletoTurn;
  private double angleTurned;
  private Rotation2d initRotation;
  private Rotation2d currentRotation;
  private final double constant = 0.5;

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
  public TurnCommand(Drivetrain drivetrain, Bling bling, double AngletoTurn, double maxSpeed) {
    this.drivetrain = drivetrain;
    this.bling = bling;
    this.AngletoTurn = AngletoTurn;
    this.maxSpeed = maxSpeed;
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
  public TurnCommand(Drivetrain drivetrain, Bling bling, double AngletoTurn) {
    this(drivetrain, bling, AngletoTurn, 0.25);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initRotation = drivetrain.getRobotPose().getRotation();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentRotation = drivetrain.getRobotPose().getRotation();
    angleTurned = (currentRotation.minus(initRotation)).getRadians();
    speed = MathUtil.clamp(constant * Math.PI / (AngletoTurn - angleTurned), 0.1, maxSpeed);
    drivetrain.setVelocity(0.0, speed);
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
    return (Math.abs(angleTurned) >= Math.abs(AngletoTurn));
  }
}