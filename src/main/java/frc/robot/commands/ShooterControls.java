// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.Shooter;

public class ShooterControls extends CommandBase {
  Shooter shooter;
  double flywheelPower;
  double hoodPower;

  public ShooterControls(Shooter shooter) {
    this.shooter = shooter;
    flywheelPower = 0;
    hoodPower = 0;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.setFlywheelPower(0);
    shooter.setHoodPower(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // OI.operatorController.setRumble(RumbleType.kLeftRumble, 0);
    // OI.operatorController.setRumble(RumbleType.kRightRumble, 0);
    // if (OI.operatorController.getRawButtonPressed(XboxController.Button.kBumperLeft.value)) {
    //   flywheelPower += MathUtil.clamp(flywheelPower + 0.25, 0, 0.75);
    //   OI.operatorController.setRumble(RumbleType.kLeftRumble, 0.5);
    //   OI.operatorController.setRumble(RumbleType.kRightRumble, 0);
    // }
    // if (OI.operatorController.getRawButtonPressed(XboxController.Button.kBumperLeft.value)) {
    //   flywheelPower += MathUtil.clamp(flywheelPower + 0.25, 0, 0.5);
    //   OI.operatorController.setRumble(RumbleType.kLeftRumble, 0);
    //   OI.operatorController.setRumble(RumbleType.kRightRumble, 0.5);
    // }
    //flywheelPower = 0.01 * OI.operatorController.getRawAxis(3);
    hoodPower = MathUtil.clamp(OI.operatorController.getRawAxis(1) * 0.1, -0.05, 0.05);
//    shooter.setFlywheelPower(flywheelPower);
    shooter.setHoodPosition(16.306*0.5+16.306*0.4*OI.operatorController.getRawAxis(1));
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
