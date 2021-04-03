package frc.robot.commands;

import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.components.WaypointPath;
import frc.robot.components.WaypointPath.Waypoint;
import frc.robot.subsystems.Drivetrain;

public class PurePursuit extends CommandBase {
    private final Drivetrain drivetrain;
    private final WaypointPath points;
    private int place;
    private Pose2d robot;
    private Vector2d botVec;
    private Waypoint nextPoint;
    private Vector2d nextVector;
    private double radius;
    private boolean isFinished;
    
    public PurePursuit(Drivetrain drivetrain, WaypointPath points, double x, double y) {
      this.drivetrain = drivetrain;
      this.points = points;
      addRequirements(drivetrain);
      drivetrain.resetRobotOdometry(new Pose2d(x, y, new Rotation2d(1, 0)));
    }

    public PurePursuit(Drivetrain drivetrain, WaypointPath points) {
      this(drivetrain, points, 0.0, Units.feetToMeters(7.5));
    }

    // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("PurePursuit Init");
    robot = drivetrain.getRobotPose();
    botVec = new Vector2d(robot.getX(), robot.getY());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    nextPoint = points.getNextWaypoint();
    robot = drivetrain.getRobotPose();
    nextVector = points.getRobotCoordinates(nextPoint, robot);
    if (Math.abs(nextVector.y) >= 0.05) {
      radius = (Math.pow(nextVector.x, 2) + Math.pow(nextVector.y, 2)) / (2 * nextVector.y);
    } else {
      radius = 0;
    }
    drivetrain.curvatureDrive(radius, nextPoint.speed);
    botVec = new Vector2d(robot.getX(), robot.getY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end. Teleop never quits.
  @Override
  public boolean isFinished() {
    place = points.findClosestWaypoint(botVec);
    if (place != points.getIndex()) {
      System.out.println("Changed to next Waypoint from: " + place);
    }
    points.updateCurrentWaypoint(place);
    isFinished = points.getNextWaypoint() == null;
    System.out.println("isFinished condition: " + isFinished);
    return isFinished;
  }
}