package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;

public class DriveToPointCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Bling bling;
    private double x;
    private double y;
    private double maxVelocity;
    private boolean isFinished;

    /**
     * Creates a new SquareTestCommand that drives a square with a given side
     * length.
     *
     * @param drivetrain    The drivetrain used by this command.
     * @param bling         The bling used by this command.
     * @param distance      The side length the robot will drive a square in.
     * @param driveVelocity The velocity of the robot while driving.
     * @param turnSpeed     The speed the robot will turn at in radians per second.
     */
    public DriveToPointCommand(Drivetrain drivetrain, Bling bling, double x, double y, double maxVelocity) {
        this.drivetrain = drivetrain;
        this.bling = bling;
        this.x = x;
        this.y = y;
        this.maxVelocity = maxVelocity;
        addRequirements(drivetrain);
        addRequirements(bling);
        double angle = 0;
        double hypotnuse = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        addCommands(new TurnCommand(drivetrain, bling, angle, maxVelocity),
                new DriveForwardCommand(drivetrain, bling, hypotnuse, maxVelocity));
    }

}