// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

  public WPI_TalonFX leftClimber;
  public WPI_TalonFX rightClimber;
  
  private double[] climberTargetVelocity;

  public final double maximumClimberPosition = 0.5;

  //TODO: PID tuning. Do it.
  double kP = 0;
  double kI = 0;
  double kD = 0;
  double kF = 0;

  //TODO: Get an actual value for this.
  final double climberTicksPerMeter = 2048.0;

  /** Creates a new Climber. */
  public Climber() {
    leftClimber = new WPI_TalonFX(28);
    rightClimber = new WPI_TalonFX(29);
    climberTargetVelocity = new double[2];
    rightClimber.setInverted(true);
    // configureClimber();
  }

  public void setPower(double leftPower, double rightPower) {
    // double[] positions = getPositions();
    // if (isAtLimit(positions[0], leftPower)) {
    //   leftPower = 0;
    // }
    // if (isAtLimit(positions[1], rightPower)) {
    //   rightPower = 0;
    // }
    leftClimber.set(ControlMode.PercentOutput, leftPower);
    rightClimber.set(ControlMode.PercentOutput, rightPower);
  }

  public boolean isAtLimit(double position, double inputVelocity) {
    return ((position < 0) && (inputVelocity < 0))
        || ((position > maximumClimberPosition) && (inputVelocity > 0));
  }

  /**
   * Sets the climber velocity in m/s on both sides.
   * The climber motors are controlled independently but 
   * @param climbVelocity The climber velocity in m/s
   */
  public void setClimberVelocity(double climbVelocity) {
    double[] positions = getPositions();
    if (isAtLimit(positions[0], climbVelocity)) {
      climberTargetVelocity[0] = 0;
    } else {
      climberTargetVelocity[0] = climbVelocity;
    }

    if (isAtLimit(positions[1], climbVelocity)) {
      climberTargetVelocity[1] = 0;
    } else {
      climberTargetVelocity[1] = climbVelocity;
    }

    leftClimber.set(ControlMode.Velocity, 0.1 * climberTargetVelocity[0] * climberTicksPerMeter);
    rightClimber.set(ControlMode.Velocity, 0.1 * climberTargetVelocity[1] * climberTicksPerMeter);
  }
  
  /**
   * Returns the climber motor positions.
   * @return An array with the climber positions (left is 0, right is 1)
   */
  public double[] getPositions() {
    return new double[]{
      leftClimber.getSelectedSensorPosition(0) / climberTicksPerMeter,
      rightClimber.getSelectedSensorPosition(0) / climberTicksPerMeter
    };
  }
  
  public double[] getRawPositions() {
    return new double[]{
      leftClimber.getSelectedSensorPosition(0),
      rightClimber.getSelectedSensorPosition(0)
    };
  }

  public double[] getRawVelocities() {
    return new double[]{
      leftClimber.getSelectedSensorVelocity(0),
      rightClimber.getSelectedSensorVelocity(0)
    };
  }

  /**
   * Returns the current climber velocities in m/s.
   * @return The climber velocities in m/s (left is 0, right is 1).
   */
  public double[] getVelocities() {
    return new double[] {
      leftClimber.getSelectedSensorVelocity() * 10.0 / climberTicksPerMeter,
      rightClimber.getSelectedSensorVelocity() * 10.0 / climberTicksPerMeter
    };
  }

  public void configureClimber() {
    leftClimber.configFactoryDefault();
    rightClimber.configFactoryDefault();

    leftClimber.setInverted(TalonFXInvertType.CounterClockwise);  //equivalent to (false).
    rightClimber.setInverted(TalonFXInvertType.Clockwise);        //equivalent to (true).

    leftClimber.neutralOutput();
    rightClimber.neutralOutput();

    leftClimber.setSafetyEnabled(false);
    rightClimber.setSafetyEnabled(false);

    leftClimber.setNeutralMode(NeutralMode.Brake);
    rightClimber.setNeutralMode(NeutralMode.Brake);

    leftClimber.configPeakOutputForward(1.0);
    rightClimber.configPeakOutputForward(1.0);

    leftClimber.configPeakOutputReverse(-1.0);
    rightClimber.configPeakOutputReverse(-1.0);

    leftClimber.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));
    rightClimber.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));

    leftClimber.setSensorPhase(true);
    rightClimber.setSensorPhase(true);

    leftClimber.setSelectedSensorPosition(0);
    rightClimber.setSelectedSensorPosition(0);
    
    leftClimber.config_kP(0, kP);
    rightClimber.config_kP(0, kP);

    leftClimber.config_kI(0, kI);
    rightClimber.config_kI(0, kI);

    leftClimber.config_kD(0, kD);
    rightClimber.config_kD(0, kD);

    leftClimber.config_kF(0, kF);
    rightClimber.config_kF(0, kF);

    leftClimber.configMaxIntegralAccumulator(0, 400);
    rightClimber.configMaxIntegralAccumulator(0, 400);
    
    leftClimber.setIntegralAccumulator(0);
    rightClimber.setIntegralAccumulator(0);
  }

  @Override
  public void periodic() {
    double[] rawPositions = getRawPositions();
    double[] rawVelocities = getRawVelocities();
    SmartDashboard.putBoolean("IS LEFT ALIVE", leftClimber.isAlive());
    
    SmartDashboard.putString("[Climber] Climber positions (raw) [C0]", rawPositions[0] + ", " + rawPositions[1]);
    SmartDashboard.putString("[Climber] Climber velocities (raw) [C1]", rawVelocities[0] + ", " + rawVelocities[1]);
    SmartDashboard.putString("[Climber] Climber powers [C2]", leftClimber.getMotorOutputPercent() + ", " + rightClimber.getMotorOutputPercent());
    double ratio = 1023.0 * ((leftClimber.get() / rawVelocities[0]));
    SmartDashboard.putNumber("[Climber] Power/velocity ratio [C3]", ratio);
    SmartDashboard.putString("[Climber] Climber current loads (A) [C4]", leftClimber.getSupplyCurrent() + ", " + rightClimber.getSupplyCurrent());
  }
}
