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
			Shooter shooter, PowerCellTracker cellTracker, PowerPortTracker portTracker, Bling bling, int compAutoNum) {
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
			return new SequentialCommandGroup(
				new SequentialCommandGroup(
					new SequentialCommandGroup(
						// Setting up the Shooter
						new ParallelDeadlineGroup(
							new ParallelCommandGroup(
								new WaitCommand(ShuffleboardWidgets.waitTime),
								new ParallelDeadlineGroup(
								new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
													new TargetHoodCommand(shooter, portTracker)),
											new SequentialCommandGroup(new WaitForTarget(portTracker),
													new TargetFlywheelCommand(shooter, portTracker)),
											new TurretPortAlignCommand(turret, portTracker))),
					// Drive off the Initiation Line
					new DriveForwardCommand(drivetrain, bling, distOffInitiationLine, 2.0))),
							// Shooting the three preloaded Powercells
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							// Shutting down the Shooter
							new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
									new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0))));
		} else if (compAutoNum == 1) {
			return new SequentialCommandGroup(
					new ParallelCommandGroup(new SequentialCommandGroup(new SequentialCommandGroup(
							// Setting up the Shooter
							new ParallelDeadlineGroup(
							new ParallelCommandGroup(
								new WaitCommand(ShuffleboardWidgets.waitTime),
							new ParallelDeadlineGroup(
									new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
											new TargetHoodCommand(shooter, portTracker)),
									new SequentialCommandGroup(new WaitForTarget(portTracker),
											new TargetFlywheelCommand(shooter, portTracker)),
									new TurretPortAlignCommand(turret, portTracker))))),
							// Shooting the three preloaded Powercells
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0))),
							// Deploying the collector while shooting
							new DeployCommand(collector)),
					// Driving off the Initiation Line
					new DriveForwardCommand(drivetrain, bling, 1.2, 3.0),
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
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							// Shutting down the Shooter
							new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
									new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0))));
		} else if (compAutoNum == 2) {
			return new SequentialCommandGroup(
				new ParallelCommandGroup(new SequentialCommandGroup(new SequentialCommandGroup(
							// Setting up the Shooter
							new ParallelDeadlineGroup(
							new ParallelCommandGroup(
								new WaitCommand(ShuffleboardWidgets.waitTime),
							new ParallelDeadlineGroup(
									new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
											new TargetHoodCommand(shooter, portTracker)),
									new SequentialCommandGroup(new WaitForTarget(portTracker),
											new TargetFlywheelCommand(shooter, portTracker)),
									new TurretPortAlignCommand(turret, portTracker))))),
							// Shooting the three preloaded Powercells
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0))),
							// Deploying the collector while shooting
							new DeployCommand(collector)),
					// Driving off the Initiation Line
					new DriveForwardCommand(drivetrain, bling, 1.4, 3.0),
					// Turn to face Powercells
					new TurnCommand(drivetrain, bling, 0.2 * Math.PI, 3.5),
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
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							// Shutting down the Shooter
							new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
									new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0))));
		} else if (compAutoNum == 3) {
			return new SequentialCommandGroup(
					new ParallelCommandGroup(new SequentialCommandGroup(new SequentialCommandGroup(
							// Setting up the Shooter
							new ParallelDeadlineGroup(
							new ParallelCommandGroup(
								new WaitCommand(ShuffleboardWidgets.waitTime),
							new ParallelDeadlineGroup(
									new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
											new TargetHoodCommand(shooter, portTracker)),
									new SequentialCommandGroup(new WaitForTarget(portTracker),
											new TargetFlywheelCommand(shooter, portTracker)),
									new TurretPortAlignCommand(turret, portTracker))))),
							// Shooting the three preloaded Powercells
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0))),
							// Deploying the collector while shooting
							new DeployCommand(collector)),
					// Driving off the Initiation Line
					new DriveForwardCommand(drivetrain, bling, 1.4, 3.0),
					// Chasing and collecting three Powercells
					new SequentialCommandGroup(new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),
							new CollectCommand(drivetrain, collector, bling, 1.0, 1),
							new MagazineCommand(collector, magazine, bling, 0.4, 2),
							new AdvanceMagazineCommand(magazine, bling, 0.5, 0.1, 3),
							new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),
							new CollectCommand(drivetrain, collector, bling, 1.0, 1),
							new MagazineCommand(collector, magazine, bling, 0.4, 2),
							new AdvanceMagazineCommand(magazine, bling, 0.5, 0.1, 3),
							new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 2.0, true, false),
							new CollectCommand(drivetrain, collector, bling, 1.0, 1),
							new MagazineCommand(collector, magazine, bling, 0.4, 2)),
					// Turning to heading
					new TurnToHeading(drivetrain, bling, 0, 3.5),
					// Driving back to the initiation line
					new DriveBackwardCommand(drivetrain, bling, 1.8, 3.4), new SequentialCommandGroup(
							new SequentialCommandGroup(
									// Setting up the Shooter
									new ParallelDeadlineGroup(
											new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
													new TargetHoodCommand(shooter, portTracker)),
											new SequentialCommandGroup(new WaitForTarget(portTracker),
													new TargetFlywheelCommand(shooter, portTracker)),
											new TurretPortAlignCommand(turret, portTracker))),
							// Shooting the three collected Powercells
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							new ParallelDeadlineGroup(new WaitForShooterCurrentSpike(shooter, true),
									new AdvanceMagazineCommand(magazine, bling, 0.5, 50.0)),
							// Shutting down the Shooter
							new SequentialCommandGroup(new TurretPositionCommand(turret, 0),
									new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0))));
		}
		return new SequentialCommandGroup();
	}
}