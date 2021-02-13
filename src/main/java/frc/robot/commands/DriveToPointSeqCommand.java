package frc.robot.commands;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;

public class DriveToPointSeqCommand extends SequentialCommandGroup {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Bling bling;
    private final double x;
    private final double y;
    private double diffX;
    private double diffY;
    private final double velocity;

    /**
     * Creates a new DriveToPointCommand that makes the robot turn to face the point
     * it's supposed to be at and then drives there at a constant velocity.
     * 
     * @param drivetrain - the drivetrain used by this command
     * @param bling      - the bling used by this command
     * @param x          - the x coordinate that should be driven to
     * @param y          - the y coordinate that should be driven to
     * @param velocity   - the velocity the robot should drive at
     */
    public DriveToPointSeqCommand(Drivetrain drivetrain, Bling bling, double x, double y, double velocity) {
        this.drivetrain = drivetrain;
        this.bling = bling;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        addRequirements(drivetrain);
        addRequirements(bling);
        diffX = diffX();
        diffY = diffY();
        addCommands(new TurnCommand(drivetrain, bling, angle(diffX, diffY), velocity),
                new DriveForwardCommand(drivetrain, bling, Math.hypot(diffX, diffY), velocity));
    }

    private double angle(double diffX, double diffY) {
        return (new Rotation2d(Math.atan2(diffX, diffY)).minus(drivetrain.getAngleRadians())).getRadians();
    }

    private double diffX() {
        return x - drivetrain.getRobotPose().getX();
    }

    private double diffY() {
        return y - drivetrain.getRobotPose().getY();
    }
}