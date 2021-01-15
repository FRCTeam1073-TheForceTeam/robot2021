// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.OI;

/** An example command that uses an example subsystem. */
public class TeleopCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Drivetrain m_drivetrain;
  private final OI m_OI;

  /**
   * Creates a new TeleopCommand
   *
   * @param subsystem The subsystem used by this command.
   */
  public TeleopCommand(Drivetrain drivetrain, OI oi) {
    m_drivetrain = drivetrain;
    m_OI = oi;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // Send joystick to drivetrain:
    m_drivetrain.setPower(m_OI.driverController.getX(), m_OI.driverController.getY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end. Teleop never quits.
  @Override
  public boolean isFinished() {
    return false;
  }
}
