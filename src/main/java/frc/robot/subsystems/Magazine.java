package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Utilities;

public class Magazine extends SubsystemBase {
    
    private WPI_TalonSRX magazineMotor;
    private int cellCount;
    private double P = 0;
    private double I = 0;
    private double D = 0;
    private double F = 0;
    private boolean cellCheck, cellEntering, cellExiting;
  
    public Magazine() {
      magazineMotor = new WPI_TalonSRX(26);
      cellCount = 0;
      cellCheck = false;
      cellEntering = false;
      cellExiting = false;
      magazineMotor.configFactoryDefault();
      magazineMotor.setSafetyEnabled(false);
      magazineMotor.setNeutralMode(NeutralMode.Brake);
      magazineMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
      // magazineMotor.config_kP(0, P);
      // magazineMotor.config_kI(0, I);
      // magazineMotor.config_kD(0, D);
      // magazineMotor.config_kF(0, F);
    }

    /// Set the induct forward/reverse velocity.
    public void setVelocity(double inductVelocity) {

    }

    /// Set the induct forward/reverse velocity.
    public void setPower(double power) {
      magazineMotor.set(ControlMode.PercentOutput, Utilities.clipRange(power, 1));
    }

    /// Return load on the magazine motor.
    public double getLoad() {
      return 0.0;
    }

    /// Return the accumulated position of the magazine.
    public double getPosition() {
      return magazineMotor.getSelectedSensorPosition(0);
    }

    /// Return the current power cell count.
    public int getPowerCellCount() {
      return cellCount;
    }


    // Add to the number of power cells.
    public void addPowerCell() {
      cellCount++;
    }
    
    // Reduce the number of power cells.
    public void removePowerCell() {
      cellCount--;
    }

    // Reset number of power cells to zero.
    public void resetPowerCellCount() {
      cellCount=0;    
    }

    
    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
}
