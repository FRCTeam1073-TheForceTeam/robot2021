// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SonarSensor extends SubsystemBase {
  private AnalogInput sensor;
  private int currentRange;
  /** Creates a new SonarSensor. */
  public SonarSensor() {
    sensor = new AnalogInput(2);
    currentRange = 0;
  }

  public int getRange(){
    return currentRange;
  }

  @Override
  public void periodic() {
    currentRange = sensor.getValue();
    SmartDashboard.putNumber("Sonar range", currentRange);
    // This method will be called once per scheduler run
  }
}
