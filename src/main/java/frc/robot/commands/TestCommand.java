<<<<<<< HEAD
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
    testSubsystem.testMotor.set(
      OI.driverController.getRawAxis(1)*0.2
    );
  }

  @Override
  public void end(boolean interrupted) {
    // Code that runs (once) when the command ends:

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Returns true when the command is supposed to end (having 'return false' causes it to run forever):
=======
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OI; // OI is a static instance.
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Magazine;

// The TestCommand is a top-level command for running hardware support / test code.
public class TestCommand extends CommandBase {
  private final Drivetrain drivetrain;
  private final Collector collector;
  private final Magazine magazine;

  public TestCommand(Drivetrain drivetrain, Collector collector, Magazine magazine) {
    this.drivetrain = drivetrain;
    this.collector = collector;
    this.magazine = magazine;

    addRequirements(drivetrain);
    addRequirements(collector);
    addRequirements(magazine);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.setPower(0.0, 0.0);
    collector.setCollect(0.0);
    magazine.setVelocity(0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // Send driver joystick to drivetrain:
    drivetrain.setPower(OI.driverController.getX(), OI.driverController.getY());

    // Send operator joysticks to magazine and collector:
    collector.setCollect(OI.operatorController.getX());
    magazine.setVelocity(OI.operatorController.getY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end. Teleop never quits.
  @Override
  public boolean isFinished() {
    // For now this just runs forever.
>>>>>>> parent of 1eadf2d (Cleared out subsystems and commands folders for rookie training stuff)
    return false;
  }
}
