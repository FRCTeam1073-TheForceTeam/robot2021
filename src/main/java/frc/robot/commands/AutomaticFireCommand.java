// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

/**
 * Command that automatically aligns the turret to the power port, determines the
 * range from the range sensor, and fires a power cell with hood angle and flywheel
 * velocity based on that range and a pair of InterpolatorTables with the hood angle
 * and flywheel velocity data collected previously.
 * 
 * @author Ben (though Mr.Pack helped a ton with simplifying the code and creating the
 * structure of the command).
 */
public class AutomaticFireCommand extends ParallelDeadlineGroup {

  /** Creates a new AutomaticFireCommand. */
  public AutomaticFireCommand(Turret turret, Shooter shooter, PowerPortTracker portTracker, Magazine magazine) {
    super(
      //Deadline command
      new SequentialCommandGroup(
          new WaitToFire(shooter, portTracker),
          new TargetHoodCommand(shooter, portTracker),
          new AdvanceMagazineCommand(magazine, 1, 12),
          new InstantCommand(shooter::stop, shooter),
          new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)
      ),

      //Commands that run until deadline
      new SequentialCommandGroup(
          new WaitForTarget(portTracker),
          new TargetFlywheelCommand(shooter, portTracker)
      ),
      new TurretPortAlignCommand(turret, portTracker)
    );
  }
}
