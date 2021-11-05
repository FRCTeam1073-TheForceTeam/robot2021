package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.ShuffleboardWidgets;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class RevisedCompetitionAutonomous extends SequentialCommandGroup {
    private static Drivetrain drivetrain;
    private static Collector collector;
    private static Magazine magazine;
    private static Turret turret;
    private static Shooter shooter;
    private static PowerCellTracker cellTracker;
    private static PowerPortTracker portTracker;
    private static Bling bling;

    public static void init(Drivetrain drivetrain_, Collector collector_, Magazine magazine_, Turret turret_, Shooter shooter_, PowerCellTracker cellTracker_, PowerPortTracker portTracker_, Bling bling_) {
        drivetrain = drivetrain_;
        collector = collector_;
        magazine = magazine_;
        turret = turret_;
        shooter = shooter_;
        cellTracker = cellTracker_;
        portTracker = portTracker_;
        bling = bling_;
    }

    public static SequentialCommandGroup getAuto5Cells() {
        return new SequentialCommandGroup(
                // Optional Wait
                new ParallelDeadlineGroup(
                        new SequentialCommandGroup(new WaitCommand(ShuffleboardWidgets.waitTime),
                                new DriveForwardCommand(drivetrain, bling, 0.8, 2.5)),
                        new ShooterSetCommand(shooter, 0.354, 474.609375)),
                new SequentialCommandGroup(new SequentialCommandGroup(
                        // Setting up the Shooter
                        new ParallelDeadlineGroup(
                                new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
                                        new TargetHoodCommand(shooter, portTracker)),
                                new SequentialCommandGroup(new WaitForTarget(portTracker),
                                        new TargetFlywheelCommand(shooter, portTracker)),
                                new TurretPortAlignCommand(turret, portTracker))),
                        new ParallelCommandGroup(new SequentialCommandGroup(
                                // Shooting the three preloaded Powercells
                                new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                                        new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                                new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                                        new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                                new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                                        new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0))),
                                new DeployCommand(collector))),

                // Chasing and collecting two Powercells
                new SequentialCommandGroup(new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),

                        new CollectCommand(drivetrain, collector, bling, 1.0, 1),
                        new MagazineCommand(collector, magazine, bling, 0.4, 2),
                        new AdvanceMagazineCommand(magazine, bling, 0.5, 0.1, 3),

                        new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),

                        new CollectCommand(drivetrain, collector, bling, 1.0, 1),
                        new MagazineCommand(collector, magazine, bling, 0.4, 2)),
                new SequentialCommandGroup(new SequentialCommandGroup(
                        // Setting up the Shooter
                        new ParallelDeadlineGroup(
                                new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
                                        new TargetHoodCommand(shooter, portTracker)),
                                new SequentialCommandGroup(new WaitForTarget(portTracker),
                                        new TargetFlywheelCommand(shooter, portTracker)),
                                new TurretPortAlignCommand(turret, portTracker))),
                        // Shooting the two collected Powercells
                        new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                                new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                        new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                                new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                        // Shutting down the Shooter
                        new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
                                new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0))));
    }

    public static SequentialCommandGroup getAuto4Cells() {
        return new SequentialCommandGroup(
            // Optional Wait
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    new WaitCommand(ShuffleboardWidgets.waitTime),
                    new DriveForwardCommand(drivetrain, bling, 0.8, 2.5)
                ),
                new ShooterSetCommand(shooter,0.354,474.609375)
            ),
            new SequentialCommandGroup(
                new SequentialCommandGroup(
                    // Setting up the Shooter
                    new ParallelDeadlineGroup(
                        new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
                            new TargetHoodCommand(shooter, portTracker)),
                        new SequentialCommandGroup(new WaitForTarget(portTracker),
                            new TargetFlywheelCommand(shooter, portTracker)),
                        new TurretPortAlignCommand(turret,portTracker)
                    )
                ),
                new ParallelCommandGroup(
                    new SequentialCommandGroup(
                        // Shooting the three preloaded Powercells
                        new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                            new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                        new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                            new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                        new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                            new AdvanceMagazineCommand(magazine, bling, 0.5, 8.0)),
                        new AdvanceMagazineCommand(magazine, bling, 0.5, 0.3)
                    ),
                    new DeployCommand(collector)
                )
            ),
    
            // Chasing and collecting two Powercells
            new SequentialCommandGroup(
                new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),
    
                new CollectCommand(drivetrain, collector, bling, 1.0, 1),
                new MagazineCommand(collector, magazine, bling, 0.4, 2),
                new AdvanceMagazineCommand(magazine, bling, 0.5, 0.1, 3)
            ),
            new SequentialCommandGroup(
                new SequentialCommandGroup(
                    // Setting up the Shooter
                    new ParallelDeadlineGroup(
                        new SequentialCommandGroup(
                            new WaitToFire(shooter, portTracker),
                            new TargetHoodCommand(shooter, portTracker)
                        ),
                        new SequentialCommandGroup(
                            new WaitForTarget(portTracker),
                            new TargetFlywheelCommand(shooter, portTracker)
                        ),
                        new TurretPortAlignCommand(turret,portTracker)
                    )
                ),
                // Shooting the collected Powercell
                new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                    new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                new AdvanceMagazineCommand(magazine, bling, 0.5, 0.3),
                    // Shutting down the Shooter
                new SequentialCommandGroup(
                    new TurretPositionCommand(turret, 0),
                    new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)
                )
            )
        );
    }
}