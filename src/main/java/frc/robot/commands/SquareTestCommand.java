package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;

public class SquareTestCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Bling bling;
    private double distanceX;
    private double distanceY;
    private double driveVelocity;
    private double turnSpeed;
    private boolean isFinished;

    /**
     * Creates a new SquareTestCommand that drives a rectangle with given side
     * lengths.
     *
     * @param drivetrain    The drivetrain used by this command.
     * @param bling         The bling used by this command.
     * @param distanceX     The width of the rectangle.
     * @param distanceY     The height of the rectangle.
     * @param driveVelocity The velocity of the robot while driving.
     * @param turnSpeed     The speed the robot will turn at in radians per second.
     */
    public SquareTestCommand(Drivetrain drivetrain, Bling bling, double distanceX, double distanceY, double driveVelocity,
            double turnSpeed) {
        this.drivetrain = drivetrain;
        this.bling = bling;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.driveVelocity = driveVelocity;
        this.turnSpeed = turnSpeed;
        addRequirements(drivetrain);
        addRequirements(bling);
        addCommands(new DriveForwardCommand(drivetrain, bling, distanceX, driveVelocity),
                new TurnCommand(drivetrain, bling, -Math.PI / 2, turnSpeed),
                new DriveForwardCommand(drivetrain, bling, distanceY, driveVelocity),
                new TurnCommand(drivetrain, bling, -Math.PI / 2, turnSpeed),
                new DriveForwardCommand(drivetrain, bling, distanceX, driveVelocity),
                new TurnCommand(drivetrain, bling, -Math.PI / 2, turnSpeed),
                new DriveForwardCommand(drivetrain, bling, distanceY, driveVelocity),
                new TurnCommand(drivetrain, bling, -Math.PI / 2, turnSpeed));
    }

    /**
     * Creates a new SquareTestCommand that drives a square with a given side
     * length.
     *
     * @param drivetrain    The drivetrain used by this command.
     * @param bling         The bling used by this command.
     * @param distance     The side length (width and height) of the rectangle.
     * @param driveVelocity The velocity of the robot while driving.
     * @param turnSpeed     The speed the robot will turn at in radians per second.
     */
    public SquareTestCommand(Drivetrain drivetrain, Bling bling, double distance, double driveVelocity,
            double turnSpeed) {
        this(drivetrain, bling, distance, distance, driveVelocity, turnSpeed);
    }

    /* 
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        super.initialize();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // TODO: Bling
        super.execute();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }
    */

}
