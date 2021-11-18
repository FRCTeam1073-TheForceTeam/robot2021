// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.*;

public class MoveWheelCommand extends CommandBase {

  WheelSubsystem wheel;

  double targetPosition;
  double diffPosition;
  double power = 0;

  /** Creates a new MoveWheelCommand. */
  public MoveWheelCommand(double targetPosition, WheelSubsystem wheel) {
    this.wheel = wheel;
    this.targetPosition = targetPosition;
    addRequirements(wheel);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    wheel.setPower(0);
    diffPosition = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double wheelPosition = wheel.getPosition();
    diffPosition = targetPosition - wheelPosition;
    power = curve(diffPosition);
    wheel.setPower(power);
  }

  public double curve(double input) {
    return Math.signum(input) * (Math.pow(Math.min(1.0, 0.4 * Math.abs(input)), 0.6) * 0.125 + 0.05);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("YEAAA");
    wheel.setPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(diffPosition) <= 0.2)&&(power<=0.1); //(0.05 radians)*(1 revolution/2pi radians)*(360 degrees/radian) = approx. 2.86 degrees
  }
}
