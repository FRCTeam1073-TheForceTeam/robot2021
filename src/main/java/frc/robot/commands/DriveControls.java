package frc.robot.commands;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import frc.robot.RobotContainer;
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
    private SlewRateLimiter multiplierRateLimiter;

    public DriveControls(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
        forward = 0;
        rotation = 0;
        leftOutput = 0;
        rightOutput = 0;
        multiplier = 1;
        multiplierRateLimiter = new SlewRateLimiter(4.0, Math.exp(Constants.THROTTLE_FALLOFF));
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

    // double maxForwardSpeed = 3.65; // in m/s
    // double maxRotationalSpeed = 8.5; // in radians/s
    // double forwardSuper = 0.0;

    double maxForwardSpeed = 1.5; // in m/s
    double maxRotationalSpeed = 3.0; // in radians/s

    public void execute() {
        //multiplier = Math.exp(-Constants.THROTTLE_FALLOFF * MathUtil.clamp(1 - (0.5 * (Utility.deadzone(OI.driverController.getRawAxis(3)) + Utility.deadzone(OI.driverController.getRawAxis(2)))), 0, 1));
        multiplier = Math.exp(-Constants.THROTTLE_FALLOFF * MathUtil.clamp(1 - (Utility.deadzone(OI.driverController.getRawAxis(3))), 0, 1));
        // forwardSuper = 0.3 * MathUtil.clamp(Utility.deadzone(OI.driverController.getRawAxis(2)), 0, 1);
        // multiplier = 0.5 + Utility.deadzone(OI.driverController.getRawAxis(3) / 2;
        // multiplier = multiplierRateLimiter.calculate(Math.exp(-Constants.THROTTLE_FALLOFF * MathUtil.clamp(1 - (Utility.deadzone(OI.driverController.getRawAxis(3)) + 0.0 * Utility.deadzone(OI.driverController.getRawAxis(2))), 0, 1)));
        // multiplier *= Math.pow(2.25, Utility.deadzone(OI.driverController.getRawAxis(2)));

        // forward = Utility.deadzone(-OI.driverController.getRawAxis(1)) * (multiplier + forwardSuper) * maxForwardSpeed;
        // if(OI.driverController.getPOV()==180) {multiplier*=0.25;}
        forward = Utility.deadzone(-OI.driverController.getRawAxis(1)) * multiplier * maxForwardSpeed;
        rotation = Utility.deadzone(-OI.driverController.getRawAxis(4)) * multiplier * maxRotationalSpeed;
        drivetrain.setVelocity(forward, rotation);
    }

    public boolean isFinished() {
        return false;
    }
}
