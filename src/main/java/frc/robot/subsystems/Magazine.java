package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Magazine extends SubsystemBase {

    private WPI_TalonSRX magazineMotor;

    public Magazine() {
      magazineMotor = new WPI_TalonSRX(26);
    }

    /// Set the induct forward/reverse velocity.
    public void setVelocity(double inductVelocity) {

    }

    /// Return load on the magazine motor.
    public double getLoad() {
      return 0.0;
    }

    /// Return the accumulated position of the magazine.
    public double getPosition() {
      return 0.0;
    }

    /// Return the current power cell count.
    public int getPowerCellCount() {
      return 0;
    }


    // Add to the number of power cells.
    public void addPowerCell() {

    }
    
    // Reduce the number of power cells.
    public void removePowerCell() {

    }

    // Reset number of power cells to zero.
    public void resetPowerCellCount() {
    
    }

    
    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
}
