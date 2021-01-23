package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OI; // OI is a static instance.
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Magazine;

// The TestCommand is a top-level command for running hardware support / test code.
public class TestCommand extends CommandBase {
    private final Drivetrain m_drivetrain;
    private final Collector m_collector;
    private final Magazine m_magazine;

  public TestCommand(Drivetrain drivetrain, Collector collector, Magazine magazine) {
      m_drivetrain = drivetrain;
      m_collector = collector;
      m_magazine = magazine;

      addRequirements(m_drivetrain);
      addRequirements(m_collector);
      addRequirements(m_magazine);
  }  

    // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      m_drivetrain.setPower(0.0, 0.0);
      m_collector.setCollect(0.0);
      m_magazine.setVelocity(0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // Send driver joystick to drivetrain:
    m_drivetrain.setPower(OI.driverController.getX(), OI.driverController.getY());

    // Send operator joysticks to magazine and collector:
    m_collector.setCollect(OI.operatorController.getX());
    m_magazine.setVelocity(OI.operatorController.getY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end. Teleop never quits.
  @Override
  public boolean isFinished() {
    // For now this just runs forever.
    return false;
  }
}
