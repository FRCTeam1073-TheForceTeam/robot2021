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
    private double F = 0;
    private double turretVelocity;
    private double turretTicksPerRadian = 6441.318;

    public Turret() {
      turretRotator = new WPI_TalonSRX(24);
      turretRotator.configFactoryDefault();
      turretRotator.setSafetyEnabled(false);
      turretRotator.setNeutralMode(NeutralMode.Brake);
      ErrorCode turretEncoderAttached = turretRotator
          .configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
      if (turretEncoderAttached.value != 0) {
        System.out.println("[Magazine] Error: Turret motor (CAN ID 24) cannot find encoder (Error code: "
            + turretEncoderAttached.value + ").");
      }
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
      turretVelocity = angularVelocity * turretTicksPerRadian * 0.1;
      turretRotator.set(ControlMode.Velocity, turretVelocity);
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
    }

    public void setPIDF() {
      turretRotator.config_kP(0, P);
      turretRotator.config_kI(0, I);
      turretRotator.config_kD(0, D);
      turretRotator.config_kF(0, F);
    }
  
}
