// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestSubsystem extends SubsystemBase {

  // Define devices here:
  public WPI_TalonFX testMotor;

  /** Creates a new TestSubsystem. */
  public TestSubsystem() {
    // Initialize devices here:


  }

  @Override
  public void periodic() {
    // This method will be called once per loop:

  }
}
