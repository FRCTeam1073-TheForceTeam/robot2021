package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;

public class SquareTestCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Bling bling;
    private double distance;
    private double driveVelocity;
    private double turnSpeed;
    private boolean isFinished;

    /**
     * Creates a new SquareTestCommand that drives a square with a given side
     * length.
     *
     * @param drivetrain    The drivetrain used by this command.
     * @param bling         The bling used by this command.
     * @param distance      The side length the robot will drive a square in.
     * @param driveVelocity The velocity of the robot while driving.
     * @param turnSpeed     The speed the robot will turn at.
     */
    public SquareTestCommand(Drivetrain drivetrain, Bling bling, double distance, double driveVelocity,
            double turnSpeed) {
        this.drivetrain = drivetrain;
        this.bling = bling;
        this.distance = distance;
        this.driveVelocity = driveVelocity;
        this.turnSpeed = turnSpeed;
        addRequirements(drivetrain);
        addRequirements(bling);
        addCommands(new DriveForwardCommand(drivetrain, bling, distance, driveVelocity),
                new TurnCommand(drivetrain, bling, Math.PI / 2, turnSpeed),
                new DriveForwardCommand(drivetrain, bling, distance, driveVelocity),
                new TurnCommand(drivetrain, bling, Math.PI / 2, turnSpeed),
                new DriveForwardCommand(drivetrain, bling, distance, driveVelocity),
                new TurnCommand(drivetrain, bling, Math.PI / 2, turnSpeed),
                new DriveForwardCommand(drivetrain, bling, distance, driveVelocity),
                new TurnCommand(drivetrain, bling, Math.PI / 2, turnSpeed));
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
