package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SquareTestCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain subsystem;
    private final Bling subsystem2;
    private double distance;
    // private double rotationAngle;
    private double power;
    private boolean isFinished;

    /**
     * Creates a new SquareTestCommand that drives a square with a given side
     * length.
     *
     * @param subsystem  The subsystem used by this command.
     * @param subsystem2 The second subsystem used by this command.
     * @param distance   The side length the robot will drive a square in.
     * @param power      The power the motors will be turning at.
     */
    public SquareTestCommand(Drivetrain subsystem, Bling subsystem2, double distance, double power) {
        this.subsystem = subsystem;
        this.subsystem2 = subsystem2;
        this.distance = distance;
        this.power = power;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // TODO: Bling
        new DriveForwardCommand(subsystem, subsystem2, distance, power);
        new TurnCommand(subsystem, Math.PI / 2, power);
        new DriveForwardCommand(subsystem, subsystem2, distance, power);
        new TurnCommand(subsystem, Math.PI / 2, power);
        new DriveForwardCommand(subsystem, subsystem2, distance, power);
        new TurnCommand(subsystem, Math.PI / 2, power);
        new DriveForwardCommand(subsystem, subsystem2, distance, power);
        new TurnCommand(subsystem, Math.PI / 2, power);
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
