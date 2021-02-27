package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Collector;

public class DeployCommand extends InstantCommand {
  private final Collector collector;

  public DeployCommand(Collector collector) {
    this.collector = collector;

    addRequirements(collector);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    collector.lower();
  }
}
