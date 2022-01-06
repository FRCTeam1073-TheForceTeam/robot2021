// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;

// Import subsystems: Add subsystems here.

import frc.robot.subsystems.*;

// Import controls: Add controls here.

// Import commands: Add commands here.

// Import components: add software components (ex. InterpolatorTable, ErrorToOutput, DataRecorder) here

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // Subsystems: Add subsystems here
  WheelSubsystem wheel = new WheelSubsystem();
  Bling bling = new Bling();
  TextPrinter textPrinter = new TextPrinter();
  SonarSensor sonar = new SonarSensor();

  // Controls: Add controls here.
  WheelControls wheelControls = new WheelControls(wheel);
  BlingTestCommand blingTest = new BlingTestCommand(bling);
  ShuffleboardWidgets shuffle = new ShuffleboardWidgets();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Initialize static OI class:
    OI.init();
    wheel.setDefaultCommand(wheelControls);
    bling.setDefaultCommand(blingTest);
    configureButtonBindings();
    shuffle.initialize();
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
    // Return the command that will run during autonomous ('return null' means no command will be run)
    //Put your autonomous code here in place of the null.
    return new ParallelDeadlineGroup(
      new SequentialCommandGroup(
        new MoveWheelCommand(6, wheel),
        new WaitCommand(5)
      ),
      new SequentialCommandGroup(
        new SetBlingCommand(bling, "blue"),
        new WaitCommand(0.5),
        new SetBlingCommand(bling, "purple"),
        new WaitCommand(0.5),
        new SetBlingCommand(bling, "green")
      ),
      new SequentialCommandGroup(
        new PrintTextCommand(shuffle, "bling off", 1),
        new WaitCommand (5)
      )
    );
  }

  public Command getTeleopCommand() {
    // Return the command that will run during teleop ('return null' means no command will be run)
    return null;
  }

  public Command getTestCommand() {
    // Return the command that will run during test mode (it's not that important)
    // ('return null' means no command will be run)
    return null;
  }
}
