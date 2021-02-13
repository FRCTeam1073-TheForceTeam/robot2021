// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// Import subsystems: Add subsystems here.
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Localizer;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Map;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.commands.ChaseAndCollectCellsCommand;
import frc.robot.commands.AdvanceMagazineCommand;
// Import commands: Add commands here.
import frc.robot.commands.CollectCommand;
import frc.robot.commands.CollectorControls;
import frc.robot.commands.DriveForwardCommand;
import frc.robot.commands.DrivetrainPowerTestCommand;
import frc.robot.commands.DriveControls;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.MagazineControls;
import frc.robot.commands.SquareTestCommand;
import frc.robot.commands.TeleopCommand;
import frc.robot.commands.TestCommand;
import frc.robot.commands.TurnCommand;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // Subsystems: Add subsystems here
  private final OI oi = new OI();
  private final Bling bling = new Bling();
  private final Drivetrain drivetrain = new Drivetrain();
  private final Collector collector = new Collector();
  private final Magazine magazine = new Magazine();
  private final Turret turret = new Turret();
  private final Shooter shooter = new Shooter();
  private final Map map = new Map();
  private final Localizer localizer = new Localizer(drivetrain);
  private final PowerPortTracker portTracker = new PowerPortTracker();
  private final PowerCellTracker cellTracker = new PowerCellTracker();

  // Commands: Add commands here.
  private final DrivetrainPowerTestCommand drivetrainTestCommand = new DrivetrainPowerTestCommand(drivetrain, 0.75);
  private final TestCommand testCommand = new TestCommand(drivetrain, collector, magazine);
  private final ExampleCommand autoCommand = new ExampleCommand(drivetrain, bling);
  private final DriveControls teleDrive = new DriveControls(drivetrain);
  private final MagazineControls teleMagazine = new MagazineControls(magazine);
  private final CollectorControls teleCollect = new CollectorControls(collector);
  private final CollectCommand collect = new CollectCommand(collector, magazine, bling, 0.5, 5000);
  private final DriveForwardCommand forward = new DriveForwardCommand(drivetrain, bling, 0.25, 0.35);
  private final TurnCommand turn90 = new TurnCommand(drivetrain, bling, Math.PI / 2, 0.15);
  private final TurnCommand turn = new TurnCommand(drivetrain, bling, 2 * Math.PI, 0.5);
  private final SquareTestCommand squareTest = new SquareTestCommand(drivetrain, bling, 1.25, 0.5, 1.75);
  private final ChaseAndCollectCellsCommand chaseAndCollect = new ChaseAndCollectCellsCommand(drivetrain, collector,
      magazine, cellTracker, bling, 5, true, 0, 1.5, 1.0);
  private final ParallelCommandGroup teleopCommand = teleDrive.alongWith(teleCollect);

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
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    JoystickButton magazineUpBinding = new JoystickButton(OI.operatorController, XboxController.Button.kY.value);
    magazineUpBinding.whenPressed(new AdvanceMagazineCommand(magazine));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // return (new PrintCommand("[RobotContainer] Starting autonomous test (driving
    // forward).")
    // .andThen(new TurnCommand(drivetrain, bling, Math.PI * 2, 1.0)));
    // return squareTest;
    // collector.manipulateIsDeployed(true);
    // return collect;
    return turn;
  }

  public Command getTeleopCommand() {
    // Command that we run in teleoperation mode.
    collector.manipulateIsDeployed(true);

    return teleopCommand;
  }

  public Command getTestCommand() {
    return teleDrive;

  }

}
