package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class PurePursuit extends CommandBase {
    private final Drivetrain drivetrain;
    private final double[][] xyv;
    private double[] point;
    private double[] nextPoint;
    private double radius;
    private int counter;
    
    public PurePursuit(Drivetrain drivetrain, double[][] xyv) {
      this.drivetrain = drivetrain;
      this.xyv = xyv;
      addRequirements(drivetrain);
    }
    // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetRobotOdometry(new Pose2d(0, Units.feetToMeters(7.5), new Rotation2d(1, 0)));
    System.out.println("PurePursuit Init");
    counter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    point = xyv[counter];
    nextPoint = xyv[counter + 1];
    radius = (Math.pow(point[0], 2) + Math.pow(point[1], 2)) / (2 * point[1]);// Translate
    drivetrain.curvatureDrive(xyv[counter][3], xyv[counter][5]);
    if (point != xyv[counter] && xyv[counter + 1] != null) {
      counter++;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end. Teleop never quits.
  @Override
  public boolean isFinished() {
    return xyv[counter + 1] == null;
  }
}