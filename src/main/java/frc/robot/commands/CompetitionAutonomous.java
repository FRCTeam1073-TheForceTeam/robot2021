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
        private final int compAutoNum;
        private final double distOffInitiationLine = 0.5;

        /**
         * Creates a new CompetitionAutonoumous that runs a autonmous routine
         * 
         * @param drivetrain  The drivetrain used by this command
         * @param collector   The collector used by this command
         * @param magazine    The magazine used by this command
         * @param turret      The turret used by this command
         * @param shooter     The shooter used by this command
         * @param cellTracker The cellTracker used by this command
         * @param portTracker The portTracker used by this command
         * @param bling       The bling used by this command
         */
        public CompetitionAutonomous(Drivetrain drivetrain, Collector collector, Magazine magazine, Turret turret,
                        Shooter shooter, PowerCellTracker cellTracker, PowerPortTracker portTracker, Bling bling,
                        int compAutoNum) {
                this.drivetrain = drivetrain;
                this.collector = collector;
                this.magazine = magazine;
                this.turret = turret;
                this.shooter = shooter;
                this.cellTracker = cellTracker;
                this.portTracker = portTracker;
                this.bling = bling;
                this.compAutoNum = compAutoNum;

                addCommands(CompAuto());
        }

        private SequentialCommandGroup CompAuto() {
                if (compAutoNum <= 0) {
                        return new SequentialCommandGroup(new ParallelDeadlineGroup(
                                        // Shooting three preloaded Powercells
                                        new SequentialCommandGroup(new SequentialCommandGroup(
                                                        new InstantCommand(shooter::lowerHood, shooter),
                                                        new ParallelDeadlineGroup(new SequentialCommandGroup(
                                                                        new WaitToFire(shooter, portTracker),
                                                                        new TargetHoodCommand(shooter, portTracker)),
                                                                        new SequentialCommandGroup(
                                                                                        new WaitForTarget(portTracker),
                                                                                        new TargetFlywheelCommand(
                                                                                                        shooter,
                                                                                                        portTracker)),
                                                                        new TurretPortAlignCommand(turret,
                                                                                        portTracker))),
                                                        new WaitCommand(0.5),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.85),
                                                        new WaitCommand(1.0),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.4),
                                                        new WaitCommand(1.0),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.85),
                                                        new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
                                                                        new ShooterSetCommand(shooter,
                                                                                        shooter.hoodAngleHigh, 0))),
                                        // deploying the collector while shooting
                                        new DeployCommand(collector)),
                                        // driving off the initiation line, further into the field
                                        new DriveForwardCommand(drivetrain, bling, 1.4, 1.5),
                                        // Chasing and collecting three Powercells
                                        new SequentialCommandGroup(
                                                        new ChaseCommand(drivetrain, cellTracker, bling, 1.7, 1.7, true,
                                                                        false),

                                                        new CollectCommand(drivetrain, collector, magazine, bling, 1.0,
                                                                        1),
                                                        new MagazineCommand(collector, magazine, bling, 0.35, 2),
                                                        new AdvanceMagazineCommand(magazine, 0.2, 0.1, 3),

                                                        new ChaseCommand(drivetrain, cellTracker, bling, 1.7, 1.7, true,
                                                                        true),

                                                        new CollectCommand(drivetrain, collector, magazine, bling, 1.0,
                                                                        1),
                                                        new MagazineCommand(collector, magazine, bling, 0.35, 2),
                                                        new AdvanceMagazineCommand(magazine, 0.2, 0.1, 3),

                                                        new ChaseCommand(drivetrain, cellTracker, bling, 1.7, 1.7, true,
                                                                        true),

                                                        new CollectCommand(drivetrain, collector, magazine, bling, 1.0,
                                                                        1),
                                                        new MagazineCommand(collector, magazine, bling, 0.35, 2),
                                                        new AdvanceMagazineCommand(magazine, 0.2, 0.1, 3)),
                                        // driving back to the initiation line
                                        new DriveBackwardCommand(drivetrain, bling, 1.4, 1.5),
                                        // Shooting three collected Powercells
                                        new SequentialCommandGroup(new SequentialCommandGroup(
                                                        new InstantCommand(shooter::lowerHood, shooter),
                                                        new ParallelDeadlineGroup(new SequentialCommandGroup(
                                                                        new WaitToFire(shooter, portTracker),
                                                                        new TargetHoodCommand(shooter, portTracker)),
                                                                        new SequentialCommandGroup(
                                                                                        new WaitForTarget(portTracker),
                                                                                        new TargetFlywheelCommand(
                                                                                                        shooter,
                                                                                                        portTracker)),
                                                                        new TurretPortAlignCommand(turret,
                                                                                        portTracker))),
                                                        new WaitCommand(0.5),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.85),
                                                        new WaitCommand(1.0),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.4),
                                                        new WaitCommand(1.0),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.85),
                                                        new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
                                                                        new ShooterSetCommand(shooter,
                                                                                        shooter.hoodAngleHigh, 0))));
                } else if (compAutoNum == 1) {
                        return new SequentialCommandGroup(
                                        new DriveForwardCommand(drivetrain, bling, distOffInitiationLine, 1.5),
                                        new SequentialCommandGroup(new SequentialCommandGroup(
                                                        new InstantCommand(shooter::lowerHood, shooter),
                                                        new ParallelDeadlineGroup(new SequentialCommandGroup(
                                                                        new WaitToFire(shooter, portTracker),
                                                                        new TargetHoodCommand(shooter, portTracker)),
                                                                        new SequentialCommandGroup(
                                                                                        new WaitForTarget(portTracker),
                                                                                        new TargetFlywheelCommand(
                                                                                                        shooter,
                                                                                                        portTracker)),
                                                                        new TurretPortAlignCommand(turret,
                                                                                        portTracker))),
                                                        new WaitCommand(0.5),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.85),
                                                        new WaitCommand(1.0),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.4),
                                                        new WaitCommand(1.0),
                                                        new AdvanceMagazineCommand(magazine, 0.9, 1.85),
                                                        new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
                                                                        new ShooterSetCommand(shooter,
                                                                                        shooter.hoodAngleHigh, 0))));
                }
                return new SequentialCommandGroup();
        }
}
