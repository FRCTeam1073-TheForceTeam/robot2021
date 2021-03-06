// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.time.Instant;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// Import subsystems: Add subsystems here.
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Map;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
// Import controls: Add controls here.
import frc.robot.commands.CollectorControls;
import frc.robot.commands.DriveControls;
import frc.robot.commands.MagazineControls;
import frc.robot.commands.ShooterControls;
import frc.robot.commands.ShooterSetCommand;
import frc.robot.commands.TurretControls;
import frc.robot.commands.TurretPortAlignCommand;
import frc.robot.commands.TurretPositionCommand;
import frc.robot.commands.WaitForTarget;
import frc.robot.commands.WaitToFire;
// Import commands: Add commands here.
import frc.robot.commands.AdvanceMagazineCommand;
import frc.robot.commands.AimingCalibrationControls;
import frc.robot.commands.AutomaticFireCommand;
import frc.robot.commands.ChaseCommand;
import frc.robot.commands.CollectCommand;
import frc.robot.commands.DriveForwardCommand;
import frc.robot.commands.DriveToPointCommand;
import frc.robot.commands.MagazineCommand;
import frc.robot.commands.SquareTestCommand;
import frc.robot.commands.TargetFlywheelCommand;
import frc.robot.commands.TargetHoodCommand;
import frc.robot.commands.TurnCommand;
import frc.robot.commands.TurnToHeading;
// Import components: add software components (ex. InterpolatorTable, ErrorToOutput) here
import frc.robot.memory.Memory;

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
  private final Drivetrain drivetrain = new Drivetrain();
  private final Collector collector = new Collector();
  private final Magazine magazine = new Magazine();
  private final Turret turret = new Turret();
  private final Shooter shooter = new Shooter();
  private final Map map = new Map();
  // private final Localizer localizer = new Localizer(drivetrain);
  private final PowerPortTracker portTracker = new PowerPortTracker();
  private final PowerCellTracker cellTracker = new PowerCellTracker();
  private final ShuffleboardWidgets shuffle = new ShuffleboardWidgets(drivetrain, collector, magazine, turret, shooter,
      cellTracker, portTracker);

  // Controls: Add controls here.
  private final DriveControls teleDrive = new DriveControls(drivetrain);
  private final MagazineControls teleMagazine = new MagazineControls(magazine);
  private final ShooterControls teleShooter = new ShooterControls(shooter);
  private final CollectorControls teleCollect = new CollectorControls(collector);
  private final TurretControls teleTurret = new TurretControls(turret);

  // private final ParallelCommandGroup teleopCommand = teleDrive.alongWith(teleCollect);

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

    drivetrain.setDefaultCommand(teleDrive);
    turret.setDefaultCommand(teleTurret);
    collector.setDefaultCommand(teleCollect);
    shooter.setDefaultCommand(teleShooter);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // (new JoystickButton(OI.driverController, XboxController.Button.kA.value))
    //     .whenPressed(
    //       // new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
    //       new SequentialCommandGroup(
    //         // new CollectCommand(drivetrain, collector, magazine, bling, 1.0),
    //         // new MagazineCommand(collector, magazine, bling, 0.35, 2),
    //         // new AdvanceMagazineCommand(magazine, 0.35, 0.25, 2)
    //       )
    //     );
    (new JoystickButton(OI.operatorController, XboxController.Button.kX.value))
      .whenPressed(
        new SequentialCommandGroup(
          // new InstantCommand(shooter::interruptCurrentCommand, shooter),
          new InstantCommand(shooter::stop, shooter),
          new InstantCommand(shooter::lowerHood, shooter),
          new ParallelDeadlineGroup(
            new SequentialCommandGroup(
              new WaitToFire(shooter, portTracker),
              new TargetHoodCommand(shooter, portTracker)
            ),
            new SequentialCommandGroup(
              new WaitForTarget(portTracker),
              new TargetFlywheelCommand(shooter, portTracker)
            ),
            new TurretPortAlignCommand(turret, portTracker)
          )
        )
      );
    (new JoystickButton(OI.operatorController, XboxController.Button.kB.value))
        .whenPressed(
          new ParallelCommandGroup(
            new TurretPositionCommand(turret, 0),
            new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)
          )
            // new InstantCommand(shooter::interruptCurrentCommand, shooter),
            // new ShooterControls(shooter),
            // new InstantCommand(
            //   () -> {
            //       SmartDashboard.putString("[InstantCommand2]whatsTheName",
            //           shooter.getCurrentCommand().getName());
            //       SmartDashboard.putBoolean("[InstantCommand2]isTheShooterDoingAnything[IC2]",
            //           shooter.getCurrentCommand() != null);
            //   }, shooter
            // )
          // )
        );
    (new JoystickButton(OI.operatorController, XboxController.Button.kA.value))
      .whenPressed(
        new TurretPositionCommand(turret, 0)
      );
    (new JoystickButton(OI.operatorController, XboxController.Button.kY.value))
        .whenPressed(
          new SequentialCommandGroup(
            // new InstantCommand(shooter::interruptCurrentCommand, shooter),
            new AdvanceMagazineCommand(magazine, 1.25, 4)

            // new TurretPositionCommand(turret, 0),
            // new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)

            // new InstantCommand(shooter::interruptCurrentCommand, shooter),
            // new InstantCommand(shooter::stop, shooter),
            // new InstantCommand(shooter::lowerHood, shooter),
            // new ShooterControls(shooter)

            // new InstantCommand(shooter::stop,shooter),
            // new InstantCommand(shooter::lowerHood,shooter),
            // new InstantCommand(shooter::interruptCurrentCommand, shooter),
            // // new InstantCommand(turret::interruptCurrentCommand, turret),
            // new PrintCommand("DSADSAFDSAFDSAFAFDSA\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"),
            // new InstantCommand(
            //   () -> {
            //     SmartDashboard.putNumber("@$$@$$@", 4141414);
            //     SmartDashboard.putString("[InstantCommand]whatsTheName");
            //       SmartDashboard.putBoolean("[InstantCommand]isTheShooterDoingAnything[IC]",
            //           shooter.getCurrentCommand() != null);
            //   }, shooter
            // )
          )
        );
    (new JoystickButton(OI.operatorController, XboxController.Button.kBumperLeft.value))
      .whenPressed(new AdvanceMagazineCommand(magazine, 1.25, 1));
    (new JoystickButton(OI.operatorController, XboxController.Button.kBumperRight.value))
      .whenPressed(new AdvanceMagazineCommand(magazine, 1.25, -1));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    drivetrain.resetRobotOdometry();
    switch (ShuffleboardWidgets.auto) {
      case 0:
        return new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3));
      case 1:
        return new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3),
            new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true),
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3));
      case 2:
        return new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3),
            new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true),
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3),
            new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true),
            new CollectCommand(drivetrain, collector, magazine, bling, 1.0, 1),
            new MagazineCommand(collector, magazine, bling, 0.35, 2),
            new AdvanceMagazineCommand(magazine, 0.35, 0.25, 3));
      case 3:
        return new SquareTestCommand(drivetrain, bling, 1.0, 2.0, 1.25, 1.25);
      case 4:
        return new DriveToPointCommand(drivetrain, bling, 1.0, 2.0, 1.5);
      case 5:
        return new AutomaticFireCommand(turret, shooter, portTracker, magazine);
      case 12:
        return new SequentialCommandGroup(
          //[[EMERGENCY BACKUP AUTONOMOUS]]
          //[[IN CASE OF COLLECTOR DOING WEIRD THINGS BREAK GLASS]]
          new PrintCommand("[BackupAutonomousRoutine] INITIALIZED!!!\n.\n.\n.\n."),
    
          //Drive forward 2 meters
          new DriveForwardCommand(drivetrain, bling, 2.0, 1.25),
    
          //Shoot
          new AutomaticFireCommand(turret, shooter, portTracker, magazine, 1.4),
          new InstantCommand(shooter::stop, shooter),
          new TurretPositionCommand(turret, 0),
    
          //Drive forward 1.5 more meters, going a bit faster.
          new DriveForwardCommand(drivetrain, bling, 1.5, 1.5),
    
          //Shoot
          new AutomaticFireCommand(turret, shooter, portTracker, magazine, 1.4),
          new InstantCommand(shooter::stop, shooter),
          new TurretPositionCommand(turret, 0),
    
          // Rotate 90 degrees to the left (positive rotation on flyhweel, negative rotation on turret).
          new TurnToHeading(drivetrain, bling, Math.PI * 0.5),
          new TurretPositionCommand(turret, -Math.PI * 0.5),
    
          //Drive forward 2.5 more meters at the same speed (I'd make it go faster but I want this to work every time).
          new DriveForwardCommand(drivetrain, bling, 1.5, 1.5),
    
          //Shoot
          new AutomaticFireCommand(turret, shooter, portTracker, magazine, 5),
          new InstantCommand(shooter::stop, shooter),
          new TurretPositionCommand(turret, 0),
    
          new TurnToHeading(drivetrain, bling, 0),
          new TurretPositionCommand(turret, 0)
        );
      default:
        return new TurnCommand(drivetrain, bling, 0.0);
    }
  }

  // Command that we run in teleoperation mode.
  public Command getTeleopCommand() {
    drivetrain.resetRobotOdometry();
    return null;
    // return teleShooter;
  }

  public Command getTestCommand() {
    return teleDrive;
  }

  public static Bling getBling() {
    return bling;
  }
}
