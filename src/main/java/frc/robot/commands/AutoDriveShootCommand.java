// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class AutoDriveShootCommand extends SequentialCommandGroup {
  /** Creates a new AutoDriveShootCommand. */
  public AutoDriveShootCommand(
                Drivetrain drivetrain,
                Bling bling,
                PowerCellTracker cellTracker,
                Collector collector,
                Turret turret,
                Shooter shooter,
                PowerPortTracker portTracker,
                Magazine magazine
  ) {
    super(
      new PrintCommand("RUNNING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1\n!!!!!!!!!!!!!!!!!\n!!!!!!!"),
      //Drive forward 2 meters
      new DriveForwardCommand(drivetrain, bling, 2.0, 1.25),

      //Shoot
      new AutomaticFireCommand(turret, shooter, portTracker, magazine),

      //Chase and collect 3 power cells
      new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3)),
      new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3)),
      new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3)),

      // Rotate backwards
      new TurnToHeadingCommand(drivetrain, bling, Math.PI),
      new TurretPositionCommand(turret, Math.PI),

      //Shoot
      new AutomaticFireCommand(turret, shooter, portTracker, magazine)
      );
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
