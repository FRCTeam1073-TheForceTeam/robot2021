package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class CompetitionAutonomous extends SequentialCommandGroup {
        @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
        private final Drivetrain drivetrain;
        private final Collector collector;
        private final Magazine magazine;
        private final Turret turret;
        private final Shooter shooter;
        private final PowerCellTracker cellTracker;
        private final PowerPortTracker portTracker;
        private final Bling bling;
        private final double distOffInitiationLine = 0.5;

        /** */
        public CompetitionAutonomous(Drivetrain drivetrain, Collector collector, Magazine magazine, Turret turret,
                        Shooter shooter, PowerCellTracker cellTracker, PowerPortTracker portTracker, Bling bling) {
                this.drivetrain = drivetrain;
                this.collector = collector;
                this.magazine = magazine;
                this.turret = turret;
                this.shooter = shooter;
                this.cellTracker = cellTracker;
                this.portTracker = portTracker;
                this.bling = bling;

                addCommands(new DeployCommand(collector),
                                new DriveForwardCommand(drivetrain, bling, distOffInitiationLine, 1.5),
                                new SequentialCommandGroup(new SequentialCommandGroup(
                                                new InstantCommand(shooter::lowerHood, shooter),
                                                new ParallelDeadlineGroup(
                                                                new SequentialCommandGroup(
                                                                                new WaitToFire(shooter,
                                                                                                portTracker),
                                                                                new TargetHoodCommand(shooter,
                                                                                                portTracker)),
                                                                new SequentialCommandGroup(
                                                                                new WaitForTarget(portTracker),
                                                                                new TargetFlywheelCommand(shooter,
                                                                                                portTracker)),
                                                                new TurretPortAlignCommand(turret, portTracker))), new WaitCommand(0.5),
                                                new AdvanceMagazineCommand(magazine, 0.9, 1.85), new WaitCommand(1.0),
                                                new AdvanceMagazineCommand(magazine, 0.9, 1.4), new WaitCommand(1.0),
                                                new AdvanceMagazineCommand(magazine, 0.9, 1.85),
                                                new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
                                                                new ShooterSetCommand(shooter, shooter.hoodAngleHigh,
                                                                                0))),
                                new ChaseCommand(drivetrain, cellTracker, bling, 1.7, 1.7, true).andThen(

                                                new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
                                                new MagazineCommand(collector, magazine, bling, 0.35, 2),
                                                new AdvanceMagazineCommand(magazine, 0.2, 0.1, 3),

                                                new ChaseCommand(drivetrain, cellTracker, bling, 1.7, 1.7, true),

                                                new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
                                                new MagazineCommand(collector, magazine, bling, 0.35, 2),
                                                new AdvanceMagazineCommand(magazine, 0.2, 0.1, 3),

                                                new ChaseCommand(drivetrain, cellTracker, bling, 1.7, 1.7, true),

                                                new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
                                                new MagazineCommand(collector, magazine, bling, 0.35, 2),
                                                new AdvanceMagazineCommand(magazine, 0.2, 0.1, 3)));
        }
}
