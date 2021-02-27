// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import frc.robot.components.InterpolatorTable;
import frc.robot.components.InterpolatorTable.InterpolatorTableEntry;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class InterpolatorShooterAimCommand extends CommandGroupBase {

  InterpolatorTable flywheelTable;
  InterpolatorTable hoodTable;
  Shooter shooter;
  PowerPortTracker portTracker;
  Magazine magazine;
  Turret turret;
  PowerPortData portData;

  public InterpolatorShooterAimCommand(Shooter shooter_, Magazine magazine_, PowerPortTracker portTracker_) {
    shooter = shooter_;
    portTracker = portTracker_;
    portData = new PowerPortData();
    flywheelTable = new InterpolatorTable(
      new InterpolatorTableEntry(0,250)
    );
    hoodTable = new InterpolatorTable(
      new InterpolatorTableEntry(0,250)
    );
    double range = getRange();
    if (range != -1) {
      addCommands(
        (new ShooterSetCommand(shooter, hoodTable.getValue(range), hoodTable.getValue(range))
        .andThen(new AdvanceMagazineCommand(magazine,2,4))
        .andThen(new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)))
        .alongWith(new TurretPortAlignCommand(turret, portTracker))
      );
    }
  }
  
  public double getRange() {
    return 10.0;
    // for (int i = 0; i < 10; i++) {
    //   boolean hasData = portTracker.getPortData(portData);
    //   if (hasData) {
    //     return portTracker.getRange();
    //   }
    // }
    // return -1;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void addCommands(Command... commands) {
    // TODO Auto-generated method stub

  }
}
