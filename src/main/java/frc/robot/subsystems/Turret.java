package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {

    private WPI_TalonSRX turretRotator;
    private double P = 0.37;
    private double I = 0;
    private double D = 0;
    private double F = 0.51;//91;
      //0.25 375
      //0.50 904
      //0.75 1458

    private double turretVelocity;
    private double turretTargetVelocity;
    private double turretTicksPerRadian = 4163.175;

    public double turretMinimumAngle = -3;
    public double turretMaximumAngle = 192.9 * (Math.PI / 180.0);

    public SlewRateLimiter turretRateLimiter;

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
      turretRateLimiter = new SlewRateLimiter(8);
    }

    /// Returns the accumulated position of the turret in radians.
    public double getPosition() {
      return turretRotator.getSelectedSensorPosition(0) / turretTicksPerRadian;
    }

    /// Returns the velocity of the turret in radians/second.
    public double getVelocity() {
      return turretRotator.getSelectedSensorVelocity(0) * 10.0 / turretTicksPerRadian;
    }

    /**
     * Returns true if the turret is at the programmed end positions.
     */
    public boolean isAtEndpoint() {
      return ((getPosition() < turretMinimumAngle) || (getPosition() > turretMaximumAngle));
    }

    /// Sets the velocity of the turret in radians/second.
    public void setVelocity(double angularVelocity) {
      if ((getPosition() < turretMinimumAngle && angularVelocity < 0) || (getPosition() > turretMaximumAngle && angularVelocity > 0)) {
        turretTargetVelocity = 0;
        turretRotator.set(ControlMode.Velocity, 0);
        turretRateLimiter.reset(0);
        return;
      }
      turretTargetVelocity = angularVelocity;
    }

    /// Directly sets the power of the turret motor.
    public void setPower(double power) {
      if ((getPosition() < turretMinimumAngle && power < 0) || (getPosition() > turretMaximumAngle && power > 0)) {
        turretRotator.set(ControlMode.PercentOutput, 0);
        turretRateLimiter.reset(0);
        return;
      }
      turretRotator.set(ControlMode.PercentOutput, power);
    }

    public void stop() {
      turretTargetVelocity = 0;
      turretRateLimiter.reset(0);
    }
    
    /**
     * Kills whatever command is running on the turret.
     * This could cause some unintended behavior if the code assumes a command is active
     * and it isn't, so it should be used only in situations where it is properly
     * accounted for.
     */
    public void interruptCurrentCommand() {
      Command currentCommand = getCurrentCommand();
      if (currentCommand != null) {
        getCurrentCommand().cancel();
      }
    }

    @Override
    public void periodic() {
      turretVelocity = turretRateLimiter.calculate(turretTargetVelocity) * turretTicksPerRadian * 0.1;
      turretRotator.set(ControlMode.Velocity, turretVelocity);
    }

    public void setPIDF() {
      turretRotator.config_kP(0, P);
      turretRotator.config_kI(0, I);
      turretRotator.config_kD(0, D);
      turretRotator.config_kF(0, F);
    }
  
}
