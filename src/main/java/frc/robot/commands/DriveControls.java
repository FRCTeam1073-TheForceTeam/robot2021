package frc.robot.commands;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;
import frc.robot.Utility;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.OI;

public class DriveControls extends CommandBase {
    Drivetrain drivetrain;
    private double forward;
    private double rotation;
    private double rightOutput;
    private double leftOutput;
    private double multiplier;
    public DriveControls(Drivetrain drivetrain_) {
        drivetrain = drivetrain_;
        addRequirements(drivetrain);
        forward = 0;
        rotation = 0;
        leftOutput = 0;
        rightOutput = 0;
        multiplier = 1;
    }

    public void initialize() {
        System.out.println("[Drivetrain: DriveControls] DriveControls online.");
        drivetrain.engageDrivetrain();
    }

    private void arcadeCompute() {
        rotation *= -1;
        double maxInput = Math.copySign(Math.max(Math.abs(forward), Math.abs(rotation)), forward);
		if (forward >= 0.0) {
			if (rotation >= 0.0) {
				leftOutput = maxInput;
				rightOutput = forward - rotation;
			} else {
				leftOutput = forward + rotation;
				rightOutput = maxInput;
			}
        } else {
            if (rotation >= 0.0) {
                leftOutput = forward + rotation;
                rightOutput = maxInput;
            } else {
                leftOutput = maxInput;
                rightOutput = forward - rotation;
            }
        }
	}

    public void execute() {
        multiplier = Math.exp(-Constants.THROTTLE_FALLOFF * Utility.deadzone(OI.driverController.getRawAxis((3))));
        forward = Utility.deadzone(OI.driverController.getRawAxis(1)) * multiplier;
        rotation = Utility.deadzone(OI.driverController.getRawAxis(4)) * multiplier;
        arcadeCompute();
        System.out.println("Output power: [" + leftOutput + "," + rightOutput + "]");
        drivetrain.setPower(leftOutput, rightOutput);
         // ensures that the driver doesn't accidentally reset the odometry but makes it an option
         if (OI.driverController.getStartButtonPressed() && OI.driverController.getBackButtonPressed()) {
            drivetrain.resetRobotOdometry();
        }
    }
    public boolean isFinished() {
        return false;
    }
}
