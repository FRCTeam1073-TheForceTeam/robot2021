// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.TestSubsystem;

public class TestCommand extends CommandBase {

  //Subsytems objects go here:
  TestSubsystem testSubsystem;

  /** Creates a new TestCommand object. */
  public TestCommand(TestSubsystem testSubsystem_) {
    testSubsystem = testSubsystem_;
    addRequirements(testSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  @Override
  public void initialize() {
    // Code that runs (once) when the command starts:

  }

  @Override
  public void execute() {
    // Code that loops until the command ends (do actual motor stuff here!):
    testSubsystem.testMotor.set(OI.driverController.getRawAxis(1)*0.4);


  }

  @Override
  public void end(boolean interrupted) {
    // Code that runs (once) when the command ends:

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Returns true when the command is supposed to end (having 'return false' causes it to run forever):
    return false;
  }
}
