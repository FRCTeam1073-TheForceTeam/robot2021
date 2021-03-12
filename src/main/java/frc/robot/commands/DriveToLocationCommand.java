// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Utility;
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
  double distanceVelocityScale = 1.5;
  // How fast the robot will turn based on its angle from the target (in units of (radians/s)/radian).
  double rotationalVelocityScale = 2.0;

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


  public double curveVelocity(double distance, double angleDiff) {
    if (distance > 0.05) {
      return Math.cos(MathUtil.clamp(3.0*angleDiff, -Math.PI*0.5, Math.PI*0.5))*distanceVelocityScale * Math.signum(distance) * (0.3 + Math.pow(
          MathUtil.clamp(Math.abs(distance / 10.0),0,1),
          0.65));
    } else {
      return 0;
    }
  }

  public double curveAngularVelocity(double angle, double distanceNorm) {
    if (Math.abs(angle) > 0.03) {
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

    double angleDifference = MathUtil.angleModulus(
      Math.atan2(distanceRemaining.getY(), distanceRemaining.getX()) - currentRobotPose.getRotation().getRadians()
    );

    // The velocity of the robot is based on the dot product of the distance vector with the robot's orientation vector.
    /* It also doesn't let the robot drive backwards, which I think makes sense right now (of course, the robot can drive backwards
    as well as it does forwards, but doing so efficiently would make the calculations for the rotational velocity more
    complicated and probably a lot harder to debug).*/

    //Minus because the angle is inverted.
    double projectedDistance = Math.max(0,
      distanceRemaining.getX() * Math.cos(currentRobotPose.getRotation().getRadians()) +
      distanceRemaining.getY() * Math.sin(currentRobotPose.getRotation().getRadians())
    );

    // Both the forward distance and rotation are run through curve functions, the same way as a lot of the other auto-aligning commands.
    forwardVelocity = curveVelocity(projectedDistance, angleDifference);
    turnVelocity = curveAngularVelocity(angleDifference, distanceRemaining.getNorm());

    bling.rangeRGB(20, 40,
      (int)(255*turnVelocity / rotationalVelocityScale),
      (int)(255*(1-turnVelocity / rotationalVelocityScale)),
      (int)(255*(forwardVelocity/distanceVelocityScale))
    );

    SmartDashboard.putNumber("[DtLoc] Difference X", distanceRemaining.getX());
    SmartDashboard.putNumber("[DtLoc] Difference Y", distanceRemaining.getY());
    SmartDashboard.putNumber("[DtLoc] Projected distance", projectedDistance);
    SmartDashboard.putNumber("[DtLoc] Angle distance", angleDifference);
    SmartDashboard.putNumber("[DtLoc] Target forward vel", forwardVelocity);
    SmartDashboard.putNumber("[DtLoc] Target angular vel", turnVelocity);
    SmartDashboard.putNumber("[DtLoc] scompX", distanceRemaining.getX() * Math.cos(currentRobotPose.getRotation().getRadians()));
    SmartDashboard.putNumber("[DtLoc] scompY", distanceRemaining.getY() * Math.sin(currentRobotPose.getRotation().getRadians()));
    SmartDashboard.putNumber("[DtLoc] rrot", currentRobotPose.getRotation().getRadians());
    SmartDashboard.putNumber("[DtLoc] hah!", Math.atan2(distanceRemaining.getY(), distanceRemaining.getX()));

    if (OI.driverController.getXButton()) {
      drivetrain.setVelocity(forwardVelocity, turnVelocity);
    } else {
      drivetrain.setVelocity(0, 0);
    }

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
    return (distanceRemaining.getNorm() < 0.1);
  }
}
