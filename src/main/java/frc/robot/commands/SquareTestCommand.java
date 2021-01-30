package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SquareTestCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Bling bling;
    private double distance;
    private double drivePower;
    private double turnPower;
    private boolean isFinished;

    /**
     * Creates a new SquareTestCommand that drives a square with a given side
     * length.
     *
     * @param drivetrain The drivetrain used by this command.
     * @param bling      The bling used by this command.
     * @param distance   The side length the robot will drive a square in.
     * @param drivePower The power the motors will be set to while driving.
     * @param turnPower  The power the motors will be set to while turning.
     */
    public SquareTestCommand(Drivetrain drivetrain, Bling bling, double distance, double drivePower, double turnPower) {
        this.drivetrain = drivetrain;
        this.bling = bling;
        this.distance = distance;
        this.drivePower = drivePower;
        this.turnPower = turnPower;
        addRequirements(drivetrain);
        addRequirements(bling);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // TODO: Bling
        new DriveForwardCommand(drivetrain, bling, distance, drivePower);
        new TurnCommand(drivetrain, bling, Math.PI / 2, turnPower);
        new DriveForwardCommand(drivetrain, bling, distance, drivePower);
        new TurnCommand(drivetrain, bling, Math.PI / 2, turnPower);
        new DriveForwardCommand(drivetrain, bling, distance, drivePower);
        new TurnCommand(drivetrain, bling, Math.PI / 2, turnPower);
        new DriveForwardCommand(drivetrain, bling, distance, drivePower);
        new TurnCommand(drivetrain, bling, Math.PI / 2, turnPower);
        isFinished = true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
