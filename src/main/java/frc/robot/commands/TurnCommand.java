// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Utility;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.OI;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;

/** An example command that uses an example subsystem. */
public class TurnCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final Drivetrain drivetrain;
  private final Bling bling;
  private final double maxSpeed;
  private final double angleToTurn;

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
  public TurnCommand(Drivetrain drivetrain, Bling bling, double angleToTurn, double maxSpeed) {
    this.drivetrain = drivetrain;
    this.bling = bling;
    this.angleToTurn = MathUtil.angleModulus(angleToTurn);
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
  public TurnCommand(Drivetrain drivetrain, Bling bling, double AngletoTurn) {
    this(drivetrain, bling, AngletoTurn, 1.5);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initAngle = drivetrain.getRobotPose().getRotation().getRadians();
    System.out.println("[TurnCommand] Turn has been initialized.");
    SmartDashboard.putBoolean("aaaaa", false);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double angleRemaining = (angleTurned - angleToTurn);
    speed = maxSpeed * Math.signum(angleRemaining) * Math.min(Math.pow(Math.abs(angleRemaining/angleToTurn), 0.5) + 0.2, 1);
    // if (Math.abs(angleRemaining) > Math.PI / 6.0) {
    //   speed = maxSpeed * Math.cbrt(angleRemaining / Math.PI);
    // } else {
    //   speed = maxSpeed * Math.signum(angleRemaining) * Math.sqrt(1.415 * Math.abs(angleRemaining));
    // }
    System.out.println("[TurnCommand] Turn speed is " + speed + " rad/sec");
    // speed = -Utility.deadzone(OI.driverController.getRawAxis(4));
    drivetrain.setVelocity(0.0, speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setVelocity(0.0, 0.0);
    System.out.println("!!!TurnAngle: " + angleTurned);
    if (interrupted) {
      System.out.println("[TurnCommand] Turn has been interrupted!");
    } else {
      System.out.println("[TurnCommand] Turn has finished.");      
    }
    SmartDashboard.putBoolean("aaaaa", true);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    angleTurned=MathUtil.angleModulus(drivetrain.getRobotPose().getRotation().getRadians()-initAngle);
    SmartDashboard.putNumber("[Turn] Target Angle", angleToTurn);
    SmartDashboard.putNumber("[Turn] Current Angle", angleTurned);
    return (angleToTurn - angleTurned) * Math.signum(angleToTurn) <= 0.02;
  }
}