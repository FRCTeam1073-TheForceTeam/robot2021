// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class PrintTextCommand extends CommandBase {

  TextPrinter printer;
  String textToPrint;
  int numPrints;
  int counter;

  /** Creates a new PrintTextCommand. */
  public PrintTextCommand(TextPrinter printer, String textToPrint, int numPrints) {
    this.printer = printer;
    this.textToPrint = textToPrint;
    this.numPrints = numPrints;
    addRequirements(printer);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    counter = 0;
    printer.printText("Starting PrintTextCommand now!");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    printer.printText(textToPrint);
    counter++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    printer.printText("Ending PrintTextCommand now!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (counter >= numPrints);
  }
}
