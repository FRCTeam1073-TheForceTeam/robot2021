package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class OI extends SubsystemBase {

    public static XboxController driverController;
    public static XboxController operatorController;

    public static void init() {
        driverController = new XboxController(0);
        //operatorController = new XboxController(1);
    }

    
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

}