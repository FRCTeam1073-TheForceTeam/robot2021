// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ShuffleboardWidgets;
import frc.robot.subsystems.*;

public class PrintTextCommand extends CommandBase {

  ShuffleboardWidgets shuffle;
  String textToPrint;
  int numPrints;
  int counter;

  /** Creates a new PrintTextCommand. */
  public PrintTextCommand(ShuffleboardWidgets shuffle, String textToPrint, int numPrints) {
    this.shuffle = shuffle;
    this.textToPrint = textToPrint;
    this.numPrints = numPrints;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    counter = 0;
    shuffle.printText("Starting PrintTextCommand now!");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shuffle.printText(textToPrint);
    counter++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shuffle.printText("Ending PrintTextCommand now!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (counter >= numPrints);
  }
}
