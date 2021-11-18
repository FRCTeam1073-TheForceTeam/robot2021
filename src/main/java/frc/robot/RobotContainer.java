// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.TestCommand;

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
  TestSubsystem testSubsystem = new TestSubsystem();

  // Controls: Add controls here.
  TestCommand testCommand = new TestCommand(testSubsystem);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Initialize static OI class:
    OI.init();
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
<<<<<<< Updated upstream
    return null;
=======
    //Put your autonomous code here in place of the null.
    return new SequentialCommandGroup(
      
    new SetBlingCommand(bling, "white"),
    new WaitCommand(2.0),
    new ParallelDeadlineGroup(
    new MoveWheelCommand(50, wheel, bling),
    new SetBlingCommand(bling, "red")),
    new WaitCommand(2.0),
    new SetBlingCommand(bling, "green"),
    new WaitCommand(2.0)
    );
    /*
     Sequential: flash bling
    wait 2 seconds
    Parallel: run motor and set bling to red

    Sequential: set bling to green for 2 seconds
    */
    
  
>>>>>>> Stashed changes
  }

  // Command that we run in teleoperation mode.
  public Command getTeleopCommand() {
    // Return the command that will run during teleop ('return null' means no command will be run)
    return testCommand;
  }

  public Command getTestCommand() {
    // Return the command that will run during test mode (it's not that important)
    // ('return null' means no command will be run)
    return null;
  }
}
