// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class TurnCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drivetrain subsystem;
  private double rotationalSpeed;
  private double rotation;
  private double initRotation;
  private double currentRotation;

  /**
   * Creates a new TurnCommand that makes the robot turn around its own axis for a given angle at a given speed.
   *
   * @param subsystem The subsystem used by this command.
   * @param rotation The angle in radians the robot will turn
   * @param rotationalSpeed The speed at which the robot should rotate
   */
  public TurnCommand(Drivetrain subsystem, double rotation, double rotationalSpeed) {
    this.subsystem = subsystem;
    this.rotation = rotation;
    this.rotationalSpeed = rotationalSpeed;
    addRequirements(subsystem);
  }

  /**
   * Creates a new TurnCommand that makes the robot turn around its own axis for a given angle at maximum speed.
   *
   * @param subsystem The subsystem used by this command.
   * @param rotation The angle in radians the robot will turn
   */
  public TurnCommand(Drivetrain subsystem, double rotation) {
    this(subsystem, rotation, 0.0); // need a constant from Constant.java for the maximum rotational speed
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initRotation = 0.0; //get Rotation from Drivetrain
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentRotation = 0.0; //get Rotation from Drivetrain
    // set the speed into the drivetrain
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
