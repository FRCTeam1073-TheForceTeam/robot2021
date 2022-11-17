package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;

public class OI {
    public static XboxController driverController;

    public static void init() {
        driverController = new XboxController(0);
    }
}