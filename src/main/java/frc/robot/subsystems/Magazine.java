package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Utility;

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
    ErrorCode magazineEncoderAttached = magazineMotor
        .configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    if (magazineEncoderAttached.value != 0) {
      System.out.println("[Magazine] Error: Magazine motor (CAN ID 26) cannot find encoder (Error code: "
          + magazineEncoderAttached.value + ").");
    }
    magazineMotor.config_kP(0, P);
    magazineMotor.config_kI(0, I);
    magazineMotor.config_kD(0, D);
    magazineMotor.config_kF(0, F);
  }

  /// Set the induct forward/reverse velocity.
  public void setVelocity(double inductVelocity) {
    magazineMotor.set(ControlMode.Velocity, inductVelocity);
  }

  /// Set the induct forward/reverse velocity.
  public void setPower(double power) {
    magazineMotor.set(ControlMode.PercentOutput, MathUtil.clamp(power, -1, 1));
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
    cellCount = 0;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Magazine Velocity", magazineMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Magazine Position", magazineMotor.getSelectedSensorPosition() * 679.0);
    if (OI.operatorController.getAButtonPressed()) {
      magazineMotor.setSelectedSensorPosition(0);
    }
  }
}
