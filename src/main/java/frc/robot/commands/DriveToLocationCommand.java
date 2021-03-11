// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.OI;

/**
 * A command that drives the robot to a certain point on the field based on odometry. The
 * command adjusts based on its current location, so hopefully the error won't accumulate as much over time.
 */
public class DriveToLocationCommand extends CommandBase {
  Drivetrain drivetrain;
  Bling bling;
  Translation2d destination;
  Translation2d distanceRemaining;
  Pose2d initialRobotPose;
  Pose2d currentRobotPose;
  // How fast the robot will move based on its distance from the destination (in units of (meters/s)/meter).
  double distanceVelocityScale = 1.0;
  // How fast the robot will turn based on its angle from the target (in units of (radians/s)/radian).
  double rotationalVelocityScale = 1.0;

  public DriveToLocationCommand(Drivetrain drivetrain_, Translation2d destination_, Bling bling_) {
    drivetrain = drivetrain_;
    destination = destination_;
    bling = bling_;
    addRequirements(drivetrain, bling);
    initialRobotPose = new Pose2d();
    currentRobotPose = new Pose2d();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initialRobotPose = drivetrain.getRobotPose();
    bling.setColorRGBAll(40, 194, 255);
  }


  public double curveVelocity(double distance) {
    if (distance > 0.05) {
      return distanceVelocityScale * Math.signum(distance) * (0.3 + Math.pow(
          MathUtil.clamp(Math.abs(distance / 10.0),0,1),
          0.65));
    } else {
      return 0;
    }
  }

  public double curveAngularVelocity(double angle) {
    if (angle > 0.05) {
      return rotationalVelocityScale * Math.signum(angle) * (0.3 + Math.pow(
          MathUtil.clamp(Math.abs(angle),0,1),
          0.65));
    } else {
      return 0;
    }    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Get the current robot pose (translation and rotation).
    currentRobotPose = drivetrain.getRobotPose();
    Translation2d currentPosition = currentRobotPose.getTranslation();

    // distanceRemaining is a vector indicating where the robot needs to go relative to where it is now.
    distanceRemaining = destination.minus(currentPosition);

    double forwardVelocity = 0;
    double turnVelocity = 0;
    // The velocity of the robot is based on the dot product of the distance vector with the robot's orientation vector.
    /* It also doesn't let the robot drive backwards, which I think makes sense right now (of course, the robot can drive backwards
    as well as it does forwards, but doing so efficiently would make the calculations for the rotational velocity more
    complicated and probably a lot harder to debug).*/
    double projectedDistance = Math.min(0,
      distanceRemaining.getX() * Math.cos(currentRobotPose.getRotation().getRadians()) +
      distanceRemaining.getY() * Math.sin(currentRobotPose.getRotation().getRadians())
    );
    double angleDifference = MathUtil.angleModulus(
      Math.atan2(distanceRemaining.getY(), distanceRemaining.getX())- currentRobotPose.getRotation().getRadians()
    );

    // For testing purposes, the auto-driving will only run when the X button is held down on the driver controller.
    if (OI.driverController.getXButton()) {
      // Both the forward distance and rotation are run through curve functions, the same way as a lot of the other auto-aligning commands.
      forwardVelocity = curveVelocity(projectedDistance);
      turnVelocity = curveAngularVelocity(angleDifference);
    }

    drivetrain.setVelocity(forwardVelocity, turnVelocity);
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
    // Ends the command when the robot's within 10 cm of its end point.
    /* 10cm is a lot less precision than I'd like (that's like 4 inches), but I want to avoid situations where the robot ends up driving in a
    small circle around the destination because it's just slightly off, so hopefully this will work as an end condition for now.*/
    return distanceRemaining.getNorm() < 0.1;
  }
}
