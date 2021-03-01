// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
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
// Import commands: Add commands here.
import frc.robot.commands.AdvanceMagazineCommand;
import frc.robot.commands.AutomaticFireCommand;
import frc.robot.commands.ChaseCommand;
import frc.robot.commands.CollectCommand;
import frc.robot.commands.DriveForwardCommand;
import frc.robot.commands.DriveToPointCommand;
import frc.robot.commands.MagazineCommand;
import frc.robot.commands.SquareTestCommand;
import frc.robot.commands.TurnCommand;
// Import components: add software components (ex. InterpolatorTable, ErrorToOutput) here

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // Subsystems: Add subsystems here
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

  private final ParallelCommandGroup teleopCommand = teleDrive.alongWith(teleMagazine, teleTurret, teleShooter, teleCollect);

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
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // JoystickButton magazineUpBinding = new JoystickButton(OI.operatorController, XboxController.Button.kB.value);
    // JoystickButton fireBinding = new JoystickButton(OI.driverController, XboxController.Button.kA.value);
    // magazineUpBinding.whenPressed(new AdvanceMagazineCommand(magazine));
    // fireBinding.whenPressed(
    //     (new ShooterSetCommand(shooter, shooter.hoodAngleLow+0.2, 250)
    //         .andThen(new AdvanceMagazineCommand(magazine, 2, 4))
    //         .andThen(new ShooterSetCommand(shooter, shooter.hoodAngleHigh, 0)))
    //         .alongWith(new TurretPortAlignCommand(turret, portTracker))
    // );

    //return new TurretPortAlignCommand(turret, portTracker);
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
            new CollectCommand(drivetrain, collector, magazine, bling), new MagazineCommand(collector, magazine, bling),
            new AdvanceMagazineCommand(magazine, 0.35, 1.0));
      case 1:
        return new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true).andThen(
            new CollectCommand(drivetrain, collector, magazine, bling), new MagazineCommand(collector, magazine, bling),
            new AdvanceMagazineCommand(magazine, 0.35, 1.0),
            new ChaseCommand(drivetrain, cellTracker, bling, 2.5, 1.25, true),
            new CollectCommand(drivetrain, collector, magazine, bling), new MagazineCommand(collector, magazine, bling),
            new AdvanceMagazineCommand(magazine, 0.35, 1.0));
      case 2:
        return new SquareTestCommand(drivetrain, bling, 1.0, 2.0, 1.25, 1.25);
      case 3:
        return new DriveToPointCommand(drivetrain, bling, 1.0, 2.0, 1.5);
      case 4:
        return new DriveForwardCommand(drivetrain, bling, 1.5, 1.25);
      case 5:
        return new AutomaticFireCommand(turret, shooter, portTracker, magazine);
      default:
        return new TurnCommand(drivetrain, bling, 0.0);
    }
  }

  // Command that we run in teleoperation mode.
  public Command getTeleopCommand() {
    drivetrain.resetRobotOdometry();
    // return turretPositionTestCommand;
    return teleopCommand;

    //    return teleopCommand;
  }

  public Command getTestCommand() {
    return teleDrive;
  }

  public static Bling getBling() {
    return bling;
  }
}