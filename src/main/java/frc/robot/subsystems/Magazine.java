package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;

public class Magazine extends SubsystemBase {

  private WPI_TalonSRX magazineMotor;
  private final DigitalInput inTakeSensor = new DigitalInput(0);
  private boolean isInTaking;
  private int cellCount;
  private double P = 0.6;
  private double I = 0.01;
  private double D = 0;
  private double F = 0;
  // private boolean cellCheck, cellEntering, cellExiting;
  private double magazineTicksPerMeter = ((18054.0 + 18271.0 + 18296.0 + 18282.0 + 18243.0) / 5.0)
      / Units.inchesToMeters(25.0);

  public Magazine() {
    SmartDashboard.putNumber("AAAAA", magazineTicksPerMeter);
    magazineMotor = new WPI_TalonSRX(26);
    cellCount = 0;
    // cellCheck = false;
    // cellEntering = false;
    // cellExiting = false;
    magazineMotor.configFactoryDefault();
    magazineMotor.setSafetyEnabled(false);
    magazineMotor.setNeutralMode(NeutralMode.Brake);
    ErrorCode magazineEncoderAttached = magazineMotor
        .configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    if (magazineEncoderAttached.value != 0) {
      System.out.println("[Magazine] Error: Magazine motor (CAN ID 26) cannot find encoder (Error code: "
          + magazineEncoderAttached.value + ").");
    }
    setPID();
  }

  public void setPID() {
    magazineMotor.config_kP(0, P);
    magazineMotor.config_kI(0, I);
    magazineMotor.config_kD(0, D);
    magazineMotor.config_kF(0, F);
  }

  double magazineVelocity = 0;

  /// Set the induct forward/reverse velocity in meters/second.
  public void setVelocity(double inductVelocity) {
    // Converting meters/second to ticks/0.1s (or if you're feeling fancy,
    // ticks/decisecond)
    magazineVelocity = inductVelocity * magazineTicksPerMeter * 0.1;
    magazineMotor.set(ControlMode.Velocity, magazineVelocity);
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
    return magazineMotor.getSelectedSensorPosition(0) / magazineTicksPerMeter;
  }

  /// Return the current power cell count.
  public int getPowerCellCount() {
    return cellCount;
  }

  public boolean getSensor() {
    return isInTaking;
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
    // if (OI.operatorController.getBumper(Hand.kRight)) {
    // magazineMotor.setSelectedSensorPosition(0);
    // }
    isInTaking = !inTakeSensor.get();
    SmartDashboard.putBoolean("inTakeSensor.get() [MAG]", isInTaking);
    SmartDashboard.putNumber("Magazine Velocity [MAG]", magazineMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Magazine Target Velocity [MAG]", magazineVelocity);
    SmartDashboard.putNumber("Magazine Position [MAG]", magazineMotor.getSelectedSensorPosition());
    SmartDashboard.putNumber("Magazine Power [MAG]", magazineMotor.getMotorOutputPercent());
    SmartDashboard.putNumber("Magazine Error [MAG]", magazineMotor.getClosedLoopError());
  }
}
