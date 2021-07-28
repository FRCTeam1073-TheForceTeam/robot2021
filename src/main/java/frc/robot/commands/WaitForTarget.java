// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class WaitForTarget extends CommandBase {

  PowerPortTracker portTracker;
  PowerPortData portData;

  //The number of valid port data readings counted so far.
  int validPortDataCounter;
  //The number of valid port data readings required for the command to finish.
  int requiredValidPortDataCount;

  boolean isPortTrackerAligned;

  double coordinateSeparation;

  boolean firstFrame = false;

  /**
   * Waits until the power port tracker has a certain number of valid detections. 
   * @param portTracker_ The port tracker subsystem
   * @param requiredValidPortDataCount_ The number of valid readings before the command ends (defaults to 5).
   */
  public WaitForTarget(PowerPortTracker portTracker_, int requiredValidPortDataCount_) {
    portTracker = portTracker_;
    requiredValidPortDataCount = requiredValidPortDataCount_;
  }

  @Override
  public void initialize() {
    portData = new PowerPortData();
    validPortDataCounter = 0;
    coordinateSeparation = -1; //Arbitrary value that won't trip isfinished 
    isPortTrackerAligned = false;
  }

  /**
   * Waits until the power port tracker has a certain number of valid detections, defaulting to 5.
   * @param portTracker_ The port tracker subsystem
   */
  public WaitForTarget(PowerPortTracker portTracker_) {
    this(portTracker_, 5);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (firstFrame) {
      firstFrame = false;
      return false;
    }

    boolean hasData = portTracker.getPortData(portData);
    if (hasData) {
      validPortDataCounter++;
    }
    if (validPortDataCounter >= requiredValidPortDataCount) {
      coordinateSeparation = 1 - 2 * (((double) portData.cx) / ((double) portTracker.getImageWidth()));
    }

    isPortTrackerAligned = (validPortDataCounter >= requiredValidPortDataCount) && (Math.abs(coordinateSeparation) <= Constants.ACCEPTABLE_PORT_TRACKER_ALIGNMENT);
    
    SmartDashboard.putBoolean("[WaitForTarget] HASDATA", hasData);
    SmartDashboard.putNumber("[WaitForTarget] CTR", validPortDataCounter);
    return isPortTrackerAligned;
  }
}
