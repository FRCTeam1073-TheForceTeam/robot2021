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
    private double diffX;
    private double diffY;
    private double distanceToDrive;
    private Rotation2d currentRotation;
    private Rotation2d rotationNeeded;
    private double angleToTurn;
    private double velocity;
    private double rotationalSpeed;
    private Pose2d pose;

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
        pose = drivetrain.getRobotPose();
    }

    private void update() {
        diffX = diffX();
        diffY = diffY();
        currentRotation = drivetrain.getRobotPose().getRotation();
        rotationNeeded = new Rotation2d(Math.atan2(diffX, diffX));
        angleToTurn = (rotationNeeded.minus(currentRotation)).getRadians();
    }

    private double diffX() {
        return x - drivetrain.getRobotPose().getX();
    }

    private double diffY() {
        return y - drivetrain.getRobotPose().getY();
    }

    private boolean isInDeadzone() {
        return false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        update();
        drivetrain.setVelocity(0.0, 0.0);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.setVelocity(0.0, 0.0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isInDeadzone();
    }
}