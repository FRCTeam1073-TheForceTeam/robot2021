// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WheelSubsystem extends SubsystemBase {

  // Define devices here:
  public WPI_TalonFX testMotor;
  public int ticksPerRevolution = 2048;

  /** Creates a new TestSubsystem. */
  public WheelSubsystem() {
    // Initialize devices here:
    testMotor = new WPI_TalonFX(20);
    testMotor.configFactoryDefault();
    testMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    testMotor.setSelectedSensorPosition(0);
  }
  
  public double getPosition() {
    return testMotor.getSelectedSensorPosition() * (2 * Math.PI) / ticksPerRevolution;
  }

  public void setPower(double power) {
    testMotor.set(ControlMode.PercentOutput, power);
  }

  @Override
  public void periodic() {
    
  }
}