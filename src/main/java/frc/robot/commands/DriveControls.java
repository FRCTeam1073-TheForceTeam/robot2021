package frc.robot.commands;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;

public class DriveControls extends CommandBase {
    Drivetrain drivetrain;
    private double deadzone = Constants.CONTROLLER_DEADZONE;
    private double multiplier;
    private double forward;
    private double rotation;
    private double rightOutput;
    private double leftOutput;

}
