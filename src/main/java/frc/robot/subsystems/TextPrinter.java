// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TextPrinter extends SubsystemBase {
  long lastTime;
  String repeatedText;

  public TextPrinter() {
    lastTime = System.currentTimeMillis();
    repeatedText = "";
    //Sadly, there aren't any arrays in here so I can't make a 'Hey-stack' joke :P
  }

  public void printText(String text) {
    System.out.println(text);
  }

  public void repeatText(String repeatedText_) {
    repeatedText = repeatedText_;
  }

  public void hey(String thingToSay) {
    System.out.println("Hey, "+thingToSay+".");
  }

  @Override
  public void periodic() {
    // Check if it's been at least a second since the last time it printed.
    long currentTime=System.currentTimeMillis();
    if ((currentTime - lastTime) >= 1000) {
      System.out.println(repeatedText);
      // Set the last time to be the new current time, so a new 1-second wait can start
      lastTime = currentTime;
    }
  }
}
