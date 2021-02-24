// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class TurretPortAlignCommand extends CommandBase {

  Turret turret;
  PowerPortTracker portTracker;
  PowerPortData portData;

  public TurretPortAlignCommand(Turret turret_, PowerPortTracker portTracker_) {
    turret = turret_;
    portTracker = portTracker_;
    addRequirements(turret, portTracker);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    turret.setVelocity(0);
  }

  public double curve(double input, double maxSpeed) {
    if (Math.abs(input) < 0.05) {
      return Math.signum(input) * (0.1 + Math.pow(Math.abs(input / maxSpeed), 0.8) * maxSpeed);
    } else {
      return 0;
    }
  }

  public double curve(double input) {
    return curve(input, 1); 
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean hasData = portTracker.getPortData(portData);
    if (!hasData) {
      double input = 2 * (portData.cx / portTracker.getImageWidth()) - 1;
      double output = -1.5 * curve(input);
      turret.setVelocity(1.5 * OI.operatorController.getRawAxis(4));
      SmartDashboard.putString("[PowerPortTracker] Output log", "[D]: INPUT (" + input + "), OUTPUT (" + output + ")");
    } else {
      turret.setVelocity(0);
    }
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
