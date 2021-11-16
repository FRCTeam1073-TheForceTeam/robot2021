// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.*;

public class MoveWheelCommand extends CommandBase {

  WheelSubsystem wheel;
  Bling bling;

  double targetPosition;
  double diffPosition;

  /** Creates a new MoveWheelCommand. */
  public MoveWheelCommand(double targetPosition, WheelSubsystem wheel, Bling bling) {
    this.wheel = wheel;
    this.bling = bling;
    this.targetPosition = targetPosition;
    addRequirements(wheel);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    wheel.setPower(0);
    bling.setColor("black");
    diffPosition = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    bling.setColor("orange");

    double wheelPosition = wheel.getPosition();
    double diffPosition = targetPosition - wheelPosition;
    wheel.setPower(curve(diffPosition));
  }

  public double curve(double input) {
    return MathUtil.clamp(input, -0.1, 0.1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    wheel.setPower(0);
    bling.setColor("black");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(diffPosition) <= 0.05; //(0.05 radians)*(1 revolution/2pi radians)*(360 degrees/radian) = approx. 2.86 degrees
  }
}
