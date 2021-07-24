package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class RevisedCompetitionAutonomous extends SequentialCommandGroup {
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
    public RevisedCompetitionAutonomous(Drivetrain drivetrain, Collector collector, Magazine magazine, Turret turret,
        Shooter shooter, PowerCellTracker cellTracker, PowerPortTracker portTracker, Bling bling) {
        this.drivetrain = drivetrain;
        this.collector = collector;
        this.magazine = magazine;
        this.turret = turret;
        this.shooter = shooter;
        this.cellTracker = cellTracker;
        this.portTracker = portTracker;
        this.bling = bling;

        addCommands(RevisedCompAuto());
    }

  private SequentialCommandGroup RevisedCompAuto() {
    return new SequentialCommandGroup(
        new ParallelDeadlineGroup(
            new DriveForwardCommand(drivetrain, bling, 0.8, 2.5),
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
            new SequentialCommandGroup(
                // Shooting the three preloaded Powercells
                new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                    new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                    new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
                new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                    new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0))
            )
        ),
        new DeployCommand(collector),

        // Chasing and collecting two Powercells
        new SequentialCommandGroup(
            new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),

            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.4, 2),
            new AdvanceMagazineCommand(magazine, bling, 0.5, 0.1, 3),

            new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),

            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.4, 2)
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
            // Shooting the two collected Powercells
            new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
            new ParallelDeadlineGroup((new WaitForShooterCurrentSpike(shooter, true)),
                new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
            // Shutting down the Shooter
            new SequentialCommandGroup(
                new TurretPositionCommand(turret, 0),
                new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)
            )
        )
    );
  }
}