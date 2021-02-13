package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;

public class DriveToPointCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Bling bling;
    private final double x;
    private final double y;
    private final double maxVelocity;
    private Pose2d currentPose;
    private Rotation2d currentRotation;
    private double diffX;
    private double diffY;
    private Rotation2d rotationNeeded;
    private double angleToTurn;
    private double rotationalSpeed;
    private double distanceToDrive;
    private double velocity;

    /**
     * Creates a new DriveToPointCommand that makes the robot drive to a point on
     * the plane it drives on with a maximum velocity set to the input,
     * 
     * @param drivetrain  - the drivetrain used by this command
     * @param bling       - the bling used by this command
     * @param x           - the x coordinate that should be driven to (in meters)
     * @param y           - the y coordinate that should be driven to (in meters)
     * @param maxVelocity - the maximum velocity the robot should drive at
     */
    public DriveToPointCommand(Drivetrain drivetrain, Bling bling, double x, double y, double maxVelocity) {
        this.drivetrain = drivetrain;
        this.bling = bling;
        this.x = x;
        this.y = y;
        this.maxVelocity = maxVelocity;
        addRequirements(drivetrain);
        addRequirements(bling);
    }

    @Override
    public void initialize() {
        currentPose = drivetrain.getRobotPose();
        diffX = x - currentPose.getX();
        diffY = y - currentPose.getY();
    }

    /** If the inputted value is between -0.1 and 0.1 then this outputs 0.0 */
    private double deadzone(double doub) {
        if (doub < 0.1 && doub > -0.1) {
            doub = 0;
        }
        return doub;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        currentPose = drivetrain.getRobotPose();
        currentRotation = currentPose.getRotation();
        diffX = x - currentPose.getX();
        diffY = y - currentPose.getY();
        rotationNeeded = new Rotation2d(Math.atan2(diffX, diffY));
        angleToTurn = (rotationNeeded.minus(currentRotation)).getRadians();
        rotationalSpeed = Math.max(0.25, Math.min(maxVelocity * 2, angleToTurn / (0.075 * Math.PI)));
        distanceToDrive = Math.hypot(diffX, diffY);
        velocity = Math.max(0.15, Math.min(maxVelocity, distanceToDrive * 10));
        drivetrain.setVelocity(velocity, rotationalSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.setVelocity(0.0, 0.0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return deadzone(diffX) == 0 && deadzone(diffY) == 0;
    }
}