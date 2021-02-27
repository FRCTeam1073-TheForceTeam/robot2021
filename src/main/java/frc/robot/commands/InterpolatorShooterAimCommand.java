// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.components.InterpolatorTable;
import frc.robot.components.InterpolatorTable.InterpolatorTableEntry;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class InterpolatorShooterAimCommand extends SequentialCommandGroup {
  Shooter shooter;
  PowerPortTracker portTracker;
  Magazine magazine;
  Turret turret;
  PowerPortData portData;

  public InterpolatorShooterAimCommand(Shooter shooter, Magazine magazine, PowerPortTracker portTracker,
      Turret turret) {
    addCommands(
        // new TurretPortAlignCommand(turret, portTracker),
        new TurretPortAlignCommand(turret, portTracker, true),
        new ShooterSetCommand(shooter, portTracker),
        new AdvanceMagazineCommand(magazine, 2, 4));
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
}

