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
    return false;
  }
}
