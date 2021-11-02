// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.*;
// Import subsystems: Add subsystems here.
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Climber;
// Import controls: Add controls here.
import frc.robot.commands.CollectorControls;
import frc.robot.commands.CompetitionAutonomous;
import frc.robot.commands.DriveControls;
import frc.robot.commands.RevisedCompetitionAutonomous;
import frc.robot.commands.ShooterControls;
import frc.robot.commands.ShooterSetCommand;
import frc.robot.commands.TurretControls;
import frc.robot.commands.TurretPortAlignCommand;
import frc.robot.commands.TurretPositionCommand;
import frc.robot.commands.WaitForShooterCurrentSpike;
import frc.robot.commands.WaitForTarget;
import frc.robot.commands.WaitToFire;
// Import commands: Add commands here.
import frc.robot.commands.AdvanceMagazineCommand;
import frc.robot.commands.CollectCommand;
import frc.robot.commands.MagazineCommand;
import frc.robot.commands.TargetFlywheelCommand;
import frc.robot.commands.TargetHoodCommand;
import frc.robot.commands.TurnCommand;
import frc.robot.commands.ClimberTestControls;
// Import components: add software components (ex. InterpolatorTable, ErrorToOutput, DataRecorder) here
import frc.robot.memory.Memory;
import frc.robot.components.DataRecorder;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // Subsystems: Add subsystems here
  public static Memory memory = new Memory();
  private static final Bling bling = new Bling();
  public final Drivetrain drivetrain = new Drivetrain();
  private final Collector collector = new Collector();
  private final Magazine magazine = new Magazine();
  private final Turret turret = new Turret();
  private static final Shooter shooter = new Shooter();
  private final Climber climber = new Climber();
  private final frc.robot.subsystems.Map map = new frc.robot.subsystems.Map();
  // private final Localizer localizer = new Localizer(drivetrain);
  private final PowerPortTracker portTracker = new PowerPortTracker();
  private final PowerCellTracker cellTracker = new PowerCellTracker();
  private final ShuffleboardWidgets shuffle = new ShuffleboardWidgets(drivetrain, collector, magazine, turret, shooter,
      cellTracker, portTracker);

  // Controls: Add controls here.
  private final DriveControls teleDrive = new DriveControls(drivetrain);
  private final ShooterControls teleShooter = new ShooterControls(shooter);
  private final CollectorControls teleCollect = new CollectorControls(collector);
  private final TurretControls teleTurret = new TurretControls(turret);
  private final SequentialCommandGroup autoFireCommand = new SequentialCommandGroup(
      new ParallelDeadlineGroup(
        new SequentialCommandGroup(new WaitToFire(shooter, portTracker),
            new TargetHoodCommand(shooter, portTracker)),
        new SequentialCommandGroup(new WaitForTarget(portTracker), new TargetFlywheelCommand(shooter, portTracker)),
        new TurretPortAlignCommand(turret, portTracker)
      ),
      new ParallelDeadlineGroup(
        (new WaitForShooterCurrentSpike(shooter, true)),
        new ParallelCommandGroup(
          new TurretPortAlignCommand(turret, portTracker),
          new AdvanceMagazineCommand(magazine, bling, 0.5, 8)
        )
      )
  );
  private final ClimberTestControls teleClimberTest = new ClimberTestControls(climber);
  private final SequentialCommandGroup teleopChaseCommand = new SequentialCommandGroup(
    new CollectCommand(drivetrain, collector, bling, 1.0, 0),
    new MagazineCommand(collector, magazine, bling, 0.4, 1)
  );

  public static final DataRecorder aimingDataRecorder = new DataRecorder("/tmp/AimingDataFile.txt");

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    // Initialize static OI class:
    OI.init();

    // Additional subsystem setup:
    map.loadData();

    // Configure the button bindings
    configureButtonBindings();

    // start shuffleboard
    shuffle.initialize();

    // start bling
    bling.initialize();

    drivetrain.setDefaultCommand(teleDrive);
    turret.setDefaultCommand(teleTurret);
    collector.setDefaultCommand(teleCollect);
    shooter.setDefaultCommand(teleShooter);
    climber.setDefaultCommand(teleClimberTest);
  }

  /**
   * \\
   * 
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    (new Trigger(() -> {return OI.driverController.getTriggerAxis(Hand.kLeft) > 0.5;}))
      .whileActiveContinuous(teleopChaseCommand);
    (new JoystickButton(OI.driverController, XboxController.Axis.kRightTrigger.value))
      .whenActive(autoFireCommand);
    (new JoystickButton(OI.operatorController, XboxController.Button.kBumperLeft.value))
      .cancelWhenPressed(autoFireCommand);
    (new JoystickButton(OI.operatorController, XboxController.Button.kA.value))
        .whenPressed(new TurretPositionCommand(turret, 0));
    (new JoystickButton(OI.operatorController, XboxController.Button.kB.value)).whenPressed(new ParallelCommandGroup(
        new TurretPositionCommand(turret, 0), new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)));
    (new JoystickButton(OI.operatorController, XboxController.Button.kY.value))
      .whenPressed(autoFireCommand);
    (new JoystickButton(OI.operatorController, XboxController.Button.kBack.value))
        .whenPressed(new AdvanceMagazineCommand(magazine, bling, 1.25, 1));
    (new JoystickButton(OI.operatorController, XboxController.Button.kStart.value))
        .whenPressed(new AdvanceMagazineCommand(magazine, bling, 1.25, -0.1));
    (new JoystickButton(OI.driverController, XboxController.Button.kY.value))
        .whenPressed(new AdvanceMagazineCommand(magazine, bling, 1.25, 1));
    (new JoystickButton(OI.driverController, XboxController.Button.kA.value))
        .whenPressed(new AdvanceMagazineCommand(magazine, bling, 1.25, -0.1));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    System.out.println("\n\n\n\n\n\n\n\n!!!!!!!!!!!!!! auto: " + ShuffleboardWidgets.auto);
    switch (ShuffleboardWidgets.auto) {
      case 0:
        return new CompetitionAutonomous(drivetrain, collector, magazine, turret, shooter, cellTracker, portTracker,
            bling, 0);
      case 1:
        return new RevisedCompetitionAutonomous(drivetrain, collector, magazine, turret, shooter, cellTracker,
            portTracker, bling);
      case 2:
        return new CompetitionAutonomous(drivetrain, collector, magazine, turret, shooter, cellTracker, portTracker,
            bling, 2);
      case 3:
        return new CompetitionAutonomous(drivetrain, collector, magazine, turret, shooter, cellTracker, portTracker,
            bling, 3);
      default:
        return new TurnCommand(drivetrain, bling, 0.0);
    }
  }

  // Command that we run in teleoperation mode.
  public Command getTeleopCommand() {
    return null;
  }

  public Command getTestCommand() {
    return teleDrive;
  }
  public static Shooter getShooter() {
    return shooter;
  }
  public static Bling getBling() {
    return bling;
  }
}