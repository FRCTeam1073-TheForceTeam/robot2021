// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooter extends SubsystemBase {
  private WPI_TalonFX shooterFlywheel1;
  private WPI_TalonFX shooterFlywheel2;
  private CANSparkMax hood;
  private CANEncoder hoodHallSensor;
  private CANEncoder hoodEncoder;
  private CANPIDController hoodController;

  private double flywheelP = 0.15;
  private double flywheelI = 0.001;
  private double flywheelD = 0;
  private double flywheelF = 0.045;

  private double hoodP_HSensor = 1.8e-1;
  private double hoodI_HSensor = 1.2e-5;
  private double hoodD_HSensor = 0;
  private double hoodF_HSensor = 0;

  private double hoodP_External = 1.8e-1;
  private double hoodI_External = 1.2e-5;
  private double hoodD_External = 0;
  private double hoodF_External = 0;

  private double flywheelTargetVelocity = 0;

  private double[] flywheelTemperatures;

  private double hoodMotorCurrent;

  private final double flywheelTicksPerRevolution = 2048;
  private final int hoodEncoderTPR = 4096;
  public final double minHoodPosition = 0;
  public final double maxHoodPosition = 16.306;
  public final double maxHoodPosition2 = 16.306;
  public final double hoodAngleHigh = 49.18 * Math.PI / 180.0;
  public final double hoodAngleLow = 19.64 * Math.PI / 180.0;
  public final double kRawMotorRange = 2.523808240890503;
  private boolean usingExternalHoodEncoder = true;
  SlewRateLimiter rateLimiter;

  public Shooter() {
    shooterFlywheel1 = new WPI_TalonFX(22);
    shooterFlywheel2 = new WPI_TalonFX(23);

    shooterFlywheel1.configFactoryDefault();
    shooterFlywheel2.configFactoryDefault();

    shooterFlywheel2.follow(shooterFlywheel1);

    // If the second flywheel motor *isn't* inverted, that would be pretty bad.
    // Like, 'the flywheel tears itself apart' kind of bad.
    shooterFlywheel1.setInverted(false);
    shooterFlywheel2.setInverted(true);

    shooterFlywheel1.setSafetyEnabled(false);
    shooterFlywheel2.setSafetyEnabled(false);

    shooterFlywheel1.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 20, 20, 0.1));
    shooterFlywheel2.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 20, 20, 0.1));

    shooterFlywheel1.setNeutralMode(NeutralMode.Brake);
    shooterFlywheel2.setNeutralMode(NeutralMode.Brake);

    shooterFlywheel1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    shooterFlywheel1.setSelectedSensorPosition(0);
    shooterFlywheel1.setIntegralAccumulator(0);
    // shooterFlywheel1.configClosedloopRamp(0.25);

    shooterFlywheel1.config_kP(0, flywheelP);
    shooterFlywheel1.config_kI(0, flywheelI);
    shooterFlywheel1.config_kD(0, flywheelD);
    shooterFlywheel1.config_kF(0, flywheelF);
    flywheelTemperatures = new double[] { -273.15, 15e6 };

    rateLimiter = new SlewRateLimiter(2500);

    hood = new CANSparkMax(28, MotorType.kBrushless);
    hood.clearFaults();
    hood.restoreFactoryDefaults();
    hood.setIdleMode(IdleMode.kBrake);
    hood.setInverted(true);

    hoodHallSensor = hood.getEncoder(EncoderType.kHallSensor, 1);
    hoodHallSensor.setPosition(0);

    hoodEncoder = hood.getAlternateEncoder(AlternateEncoderType.kQuadrature, hoodEncoderTPR);
    hoodEncoder.setPosition(0);
    hoodEncoder.setInverted(true);

    hoodController = hood.getPIDController();
    if (usingExternalHoodEncoder) {
      hoodController.setFeedbackDevice(hoodEncoder);
      hoodController.setP(hoodP_External);
      hoodController.setI(hoodI_External);
      hoodController.setD(hoodD_External);
      hoodController.setFF(hoodF_External);
    } else {
      hoodController.setFeedbackDevice(hoodHallSensor);
      hoodController.setP(hoodP_HSensor);
      hoodController.setI(hoodI_HSensor);
      hoodController.setD(hoodD_HSensor);
      hoodController.setFF(hoodF_HSensor);
    }
    hood.setClosedLoopRampRate(0.15);
  }

  /**
   * Directly sets the power to the flywheel motors.
   */
  public void setFlywheelPower(double power) {
    shooterFlywheel1.set(ControlMode.PercentOutput, power);
  }

  /**
   * Sets the flywheel velocity in radians/second.
   */
  public void setFlywheelVelocity(double velocity) {
    // flywheelTargetVelocity = velocity * 0.1 * flywheelTicksPerRevolution / (2.0 *
    // Math.PI);
    flywheelTargetVelocity = Math.max(0, velocity);
  }

  /**
   * Gets the flywheel velocity in radians/second
   * 
   * @return The flywheel velocity in radians/second
   */
  public double getFlywheelVelocity() {
    return shooterFlywheel1.getSelectedSensorVelocity() * 10.0 * (2.0 * Math.PI) / flywheelTicksPerRevolution;
  }

  /**
   * Gets the hood velocity in radians/second
   * 
   * @return The hood velocity in radians/second
   */
  public double getHoodVelocity() {
    return hoodEncoder.getVelocity() * 2.0 * Math.PI / 60.0;
  }

  /**
   * Gets the flywheel temperatures in Celsius
   * 
   * @return both flywheel motor's temperatures in Celsius
   */
  public double[] getFlywheelTemperatures() {
    return flywheelTemperatures;
  }

  /**
   * Gets the hood's motor's current in amperage
   * 
   * @return hood's current in amperage
   */
  public double getHoodMotorCurrent() {
    return hoodMotorCurrent;
  }

  /**
   * Instantly stops the flywheel motor. Due to the PIDF loop being tuned for high
   * velocities, this may cause some oscillation.
   */
  public void stop() {
    rateLimiter.reset(0);
    shooterFlywheel1.set(ControlMode.Velocity, 0);
  }

  /**
   * Cancels whatever command is currently requiring the shooter. Due to the way
   * the shooter works, this is almost always ShooterControls. This is necessary
   * for the auto-aiming in the teleop controls, but it really shouldn't be used
   * in situations where it's not properly accounted for and it could cause a
   * bunch of super weird problems if the current command is canceled when the
   * rest of the code doesn't expect it, so, uh, use it responsibly?
   * 
   * Also, if there isn't currently a command requiring the shooter, nothing
   * should happen.
   */
  public void interruptCurrentCommand() {
    Command currentCommand = getCurrentCommand();
    if (currentCommand != null) {
      getCurrentCommand().cancel();
    }
  }

  /**
   * Causes the flywheel motor to ramp down to zero (as opposed to stop() which
   * causes it to halt near-instantly).
   */
  public void setZero() {
    setFlywheelVelocity(0);
  }

  /**
   * Directly sets the power to the hood motor.
   */
  public void setHoodPower(double power) {
    if ((getHoodPosition() < minHoodPosition && power < 0) || (getHoodPosition() > maxHoodPosition && power > 0)) {
      power = 0;
    }
    hood.set(power);
  }

  public double getHoodPosition() {
    if (usingExternalHoodEncoder) {
      return hoodEncoder.getPosition() * 2.0 * Math.PI;
    } else {
      return hoodHallSensor.getPosition() * 2.0 * Math.PI;
    }
  }

  public double getHoodAngle() {
    return hoodAngleHigh + (hoodAngleLow - hoodAngleHigh)
        * ((getHoodPosition() - minHoodPosition) / (maxHoodPosition - minHoodPosition));
  }

  public double getFlywheelTargetVelocity() {
    return flywheelTargetVelocity;
  }

  /**
   * Sets the hood motor's position (as opposed to setHoodAngle, which sets the
   * actual angle of the hood). Values are clamped between the minimum and maximum
   * positions to prevent the mechanism from damaging itself. This requires that
   * the hood is manually reset at the start of each match (auto-indexing is
   * possible, but the only simple way to do it puts the mechanism under too much
   * stress).
   * 
   * @param position The target position of the hood motor in radians
   */
  public void setHoodPosition(double position) {
    hoodController.setReference(MathUtil.clamp(position, minHoodPosition, maxHoodPosition) / (2.0 * Math.PI),
        ControlType.kPosition);
  }

  /**
   * Set's the hood's position to its lower bound (the steepest angle). Equivalent
   * to setHoodPosition(0).
   */
  public void lowerHood() {
    setHoodPosition(0);
  }

  /**
   * Sets the hood angle, with values clamped between the lowest possible angle
   * (fully extended) and the steepest possible angle (fully retracted).
   * 
   * @param angle The angle of the hood in radians
   */
  public void setHoodAngle(double angle) {
    double position = minHoodPosition
        + (maxHoodPosition - minHoodPosition) * ((angle - hoodAngleHigh) / (hoodAngleLow - hoodAngleHigh));
    setHoodPosition(position);
  }

  public double getFlywheelCurrent() {
    return (shooterFlywheel1.getSupplyCurrent() + shooterFlywheel2.getSupplyCurrent()) * 0.5;
  }

  @Override
  public void periodic() {
    double limitedFlywheelVelocity = rateLimiter
        .calculate(flywheelTargetVelocity * 0.1 * flywheelTicksPerRevolution / (2.0 * Math.PI));
    shooterFlywheel1.set(ControlMode.Velocity, limitedFlywheelVelocity);
    flywheelTemperatures[0] = shooterFlywheel1.getTemperature();
    flywheelTemperatures[1] = shooterFlywheel2.getTemperature();
    hoodMotorCurrent = hood.getOutputCurrent();
    SmartDashboard.putNumber(":", getFlywheelCurrent());
  }
}
