package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class PurePursuit extends CommandBase {
    private final Drivetrain drivetrain;
    private double angle;
    
    public PurePursuit(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }
    // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetRobotOdometry(new Pose2d(0, Units.feetToMeters(7.5), new Rotation2d(1, 0)));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.curvatureDrive(1.0, 1.0);
    angle = drivetrain.getAngleDegrees();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end. Teleop never quits.
  @Override
  public boolean isFinished() {
    return angle >= 180;
  }
}


