// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// Import subsystems: Add subsystems here.
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Localizer;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Map;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
// Import commands: Add commands here.
import frc.robot.commands.CollectCommand;
import frc.robot.commands.DriveForwardCommand;
import frc.robot.commands.DriveControls;
import frc.robot.commands.ExampleCommand;
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
  private final Bling m_bling = new Bling();
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final Collector m_collector = new Collector();
  private final Magazine m_magazine = new Magazine();
  private final Turret m_turret = new Turret();
  private final Shooter m_shooter = new Shooter();
  private final Map m_map = new Map();
  private final Localizer m_localizer = new Localizer(m_drivetrain);
  private final OI m_oi = new OI();

  // Commands: Add commands here.
  private final TestCommand m_testCommand = new TestCommand(m_drivetrain, m_collector, m_magazine);
  private final ExampleCommand m_autoCommand = new ExampleCommand(m_drivetrain, m_bling);
  private final TeleopCommand m_teleopCommand = new TeleopCommand(m_drivetrain, m_oi);
  private final DriveControls m_driveControls = new DriveControls(m_drivetrain);
  private final CollectCommand collect = new CollectCommand(m_collector, m_magazine, m_bling, 0.5, 5000);
  private final DriveForwardCommand forward = new DriveForwardCommand(m_drivetrain, m_bling, 0.25, 0.35);
  private final TurnCommand turn90 = new TurnCommand(m_drivetrain, m_bling, Math.PI / 2, 0.15);
  private final SquareTestCommand squareTest = new SquareTestCommand(m_drivetrain, m_bling, 1.75, 0.5, 0.25);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    // Initialize static OI class:
    OI.init();

    // Additional subsystem setup:
    m_map.loadData();

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

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new SequentialCommandGroup(turn);// , turn, forward, turn, forward, turn, forward, turn);
    //m_collector.manipulateIsDeployed(true);
    //return collect;
  }

  public Command getTeleopCommand() {
    // Command that we run in teleoperation mode.
    return m_driveControls;
  }

  public Command getTestCommand() {
    return m_driveControls;

  }

}
