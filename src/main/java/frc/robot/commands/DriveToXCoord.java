// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;

enum DriveDirection {
  FORWARD, BACKWARD
}

public class DriveToXCoord extends CommandBase {
  Drivetrain drivetrain;
  Bling bling;
  double destinationX;
  double currentX;
  double diffX;
  double velocity;
  DriveDirection direction;
  // How close the robot can be (in meters) past the destination before it can stop.
  final double distanceLimit = 0.05;

  /** Creates a new DriveToXCoord. */
  public DriveToXCoord(Drivetrain drivetrain_, double destinationX_, double velocity_, DriveDirection direction_, Bling bling_) {
    drivetrain = drivetrain_;
    bling = bling_;
    destinationX = destinationX_;
    addRequirements(drivetrain, bling);
    currentX = 0;
    diffX = 0;
    velocity = velocity_;
    direction = direction_;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  /** Creates a new DriveToXCoord. */
  public DriveToXCoord(Drivetrain drivetrain_, double destinationX_, DriveDirection direction_, Bling bling_) {
    this(drivetrain_, destinationX_, 1.5, direction_, bling_);
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.setVelocity(0, 0);
    currentX = drivetrain.getRobotPose().getX();
    bling.setColorRGBAll(255, 149, 0);
  }

  public double curve(double input) {
      if (Math.abs(input) > distanceLimit*0.75) {
        return Math.signum(input) * (0.6 + Math.pow(
            MathUtil.clamp(Math.abs(input / 10.0), 0, 1),
            0.65));
      } else { // Deadzone
        return 0;
      }
    }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentX = drivetrain.getRobotPose().getX();
    diffX = destinationX - currentX;
    double forwardVelocity = velocity * curve(diffX);
    if (direction.equals(DriveDirection.FORWARD)) {
      forwardVelocity = Math.max(0, forwardVelocity);
    } else if (direction.equals(DriveDirection.BACKWARD)) {
      forwardVelocity = Math.min(0, forwardVelocity);
    } else {
      System.out.println("[DriveToXCoord] Error!!");
      forwardVelocity = 0;
    }
    drivetrain.setVelocity(forwardVelocity, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.setVelocity(0, 0);
    bling.clearLEDs();
    bling.cleared();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (direction.equals(DriveDirection.FORWARD) && (diffX <= -distanceLimit))
    || (direction.equals(DriveDirection.BACKWARD) && (diffX >= distanceLimit));
  }
}
