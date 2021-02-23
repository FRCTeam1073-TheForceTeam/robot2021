package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {

    private WPI_TalonSRX turretRotator;
    private double P = 0;
    private double I = 0;
    private double D = 0;
    private double F = 0.591;
      //0.25 375
      //0.50 904
      //0.75 1458

    private double turretVelocity;
    private double turretTicksPerRadian = 4163.175;

    public double turretMinimumAngle = -2.039;
    public double turretMaximumAngle = 3.795;

    public Turret() {
      turretRotator = new WPI_TalonSRX(24);
      turretRotator.configFactoryDefault();
      turretRotator.setSafetyEnabled(false);
      turretRotator.setInverted(true);
      turretRotator.setNeutralMode(NeutralMode.Brake);
      turretRotator.configPeakCurrentLimit(10);
      turretRotator.configContinuousCurrentLimit(10);
      ErrorCode turretEncoderAttached = turretRotator
          .configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
      if (turretEncoderAttached.value != 0) {
        System.out.println("[Magazine] Error: Turret motor (CAN ID 24) cannot find encoder (Error code: "
            + turretEncoderAttached.value + ").");
      }
      turretRotator.setSelectedSensorPosition(0); //Note: turret does not have limit switches and needs to be indexed manually.
      turretRotator.setIntegralAccumulator(0);
      setPIDF();
    }

    /// Returns the accumulated position of the turret in radians.
    public double getPosition() {
      return turretRotator.getSelectedSensorPosition(0) / turretTicksPerRadian;
    }

    /// Returns the velocity of the turret in radians/second.
    public double getVelocity() {
      return turretRotator.getSelectedSensorVelocity(0) * 10.0 / turretTicksPerRadian;
    }

    /// Sets the velocity of the turret in radians/second.
    public void setVelocity(double angularVelocity) {
      if ((getPosition() < turretMinimumAngle && angularVelocity < 0) || (getPosition() > turretMaximumAngle && angularVelocity > 0)) {
        turretRotator.set(ControlMode.Velocity, 0);
        return;
      }
      turretVelocity = angularVelocity * turretTicksPerRadian * 0.1;
      turretRotator.set(ControlMode.Velocity, turretVelocity);
    }

    /// Directly sets the power of the turret motor.
    public void setPower(double power) {
      if ((getPosition() < turretMinimumAngle && power < 0) || (getPosition() > turretMaximumAngle && power > 0)) {
        turretRotator.set(ControlMode.PercentOutput, 0);
        return;
      }
      turretRotator.set(ControlMode.PercentOutput, power);
    }

    @Override
    public void periodic() {
      SmartDashboard.putNumber("Turret Velocity [TURRET]",
      turretRotator.getSelectedSensorVelocity());
      SmartDashboard.putNumber("Turret Target Velocity [TURRET]", turretVelocity);
      SmartDashboard.putNumber("Turret Position (raw) [TURRET]", turretRotator.getSelectedSensorPosition());
      SmartDashboard.putNumber("Turret Power (raw) [TURRET]", turretRotator.getMotorOutputPercent());
      SmartDashboard.putNumber("Turret Error (raw) [TURRET]", turretRotator.getClosedLoopError());
      SmartDashboard.putNumber("Turret Position (radians) [TURRET]",
          turretRotator.getSelectedSensorPosition(0) / turretTicksPerRadian);
      SmartDashboard.putNumber("Turret Velocity (radians/s) [TURRET]",
          turretRotator.getSelectedSensorVelocity(0) * 10.0 / turretTicksPerRadian);
      SmartDashboard.putNumber("Raw Turret Velocity (ticks/0.1) [TURRET]",
          turretRotator.getSelectedSensorVelocity(0));
      SmartDashboard.putNumber("Turret current load (amperes) [TURRET]",
          turretRotator.getSupplyCurrent());
      SmartDashboard.putNumber("Turret temperature (degs C) [TURRET]",
          turretRotator.getTemperature());
    }

    public void setPIDF() {
      turretRotator.config_kP(0, P);
      turretRotator.config_kI(0, I);
      turretRotator.config_kD(0, D);
      turretRotator.config_kF(0, F);
    }
  
}
