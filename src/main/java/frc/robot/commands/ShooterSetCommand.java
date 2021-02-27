// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;
import frc.robot.components.InterpolatorTable;
import frc.robot.components.InterpolatorTable.InterpolatorTableEntry;

public class ShooterSetCommand extends CommandBase {

  Shooter shooter;
  PowerPortTracker portTracker;
  double hoodAngle;
  double flywheelVelocity;
  double currentFlywheelVelocity;
  PowerPortData portData;

  InterpolatorTable flywheelTable;
  InterpolatorTable hoodTable;

  boolean hasFound;

  public ShooterSetCommand(Shooter shooter_, PowerPortTracker portTracker_) {

    hasFound = false;
    portTracker = portTracker_;

    flywheelTable = new InterpolatorTable(new InterpolatorTableEntry(1.79, 281.25),
        new InterpolatorTableEntry(2.35, 343.75), new InterpolatorTableEntry(3.02, 343.75),
        new InterpolatorTableEntry(3.05, 343.75), new InterpolatorTableEntry(3.54, 375),
        new InterpolatorTableEntry(3.97, 406.25), new InterpolatorTableEntry(4.56, 437.3),
        new InterpolatorTableEntry(5.02, 437.3), new InterpolatorTableEntry(5.51, 468.75),
        new InterpolatorTableEntry(6.03, 468.75), new InterpolatorTableEntry(6.51, 468.75));
    hoodTable = new InterpolatorTable(new InterpolatorTableEntry(1.79, 0.658353),
        new InterpolatorTableEntry(2.35, 0.483353), new InterpolatorTableEntry(3.02, 0.508353),
        new InterpolatorTableEntry(3.05, 0.483353), new InterpolatorTableEntry(3.54, 0.483353),
        new InterpolatorTableEntry(3.97, 0.408353), new InterpolatorTableEntry(4.56, 0.363353),
        new InterpolatorTableEntry(5.02, 0.358353), new InterpolatorTableEntry(5.51, 0.367783),
        new InterpolatorTableEntry(6.03, 0.367783), new InterpolatorTableEntry(6.51, 0.34278));
    shooter = shooter_;
    currentFlywheelVelocity = 0;
    addRequirements(shooter);
  }
  
  public void check() {
    if (!hasFound) {
      double range = portTracker.getRange();
      if (range != -1) {
        hoodAngle = hoodTable.getValue(range);
        flywheelVelocity = flywheelTable.getValue(range);
        hasFound = true;
      }
    }
  }
  
  @Override
  public void initialize() {
    check();
  }

  public void execute() {
    check();
    shooter.setHoodAngle(hoodAngle);
    shooter.setFlywheelVelocity(flywheelVelocity);
  }

  public void end(boolean interrupted) {
  }
  
  public boolean isFinished() {
    return Math.abs(shooter.getFlywheelVelocity() - flywheelVelocity) <= 1;
  }
}
