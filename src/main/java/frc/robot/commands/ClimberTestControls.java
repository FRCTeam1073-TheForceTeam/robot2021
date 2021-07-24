// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.OI;

public class ClimberTestControls extends CommandBase {
  Climber climber;

  /** Creates a new ClimberTestControls. */
  public ClimberTestControls(Climber climber_) {
    climber = climber_;
    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    climber.setPower(0, 0, true);
  }

  boolean a = false;
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    a = !a;
    double leftPower=0;
    double 
    rightPower=0;
    SmartDashboard.putBoolean("$A$", a);
    if(OI.operatorController.getTriggerAxis(Hand.kLeft)>0.5) {
      leftPower = -OI.operatorController.getRawAxis(1);
    }
    if(OI.operatorController.getTriggerAxis(Hand.kRight)>0.5) {
      rightPower = -OI.operatorController.getRawAxis(1);
    }
    climber.setPower(leftPower, rightPower);
    // climber.setPower(leftPower, rightPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.setPower(0, 0, true);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
