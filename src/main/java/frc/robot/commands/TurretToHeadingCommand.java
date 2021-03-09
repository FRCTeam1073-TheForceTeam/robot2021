// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Turret;

public class TurretToHeadingCommand extends CommandBase {

  Turret turret;
  PowerPortTracker portTracker;
  double currentPosition;
  double targetPosition;
  double angSeparation = 0;

  /**
   * Sets the turret's angle in radians.
   * @param turret_ The turret object
   * @param targetPosition_ The target angle in radians
   */
  public TurretToHeadingCommand(Turret turret_, double targetPosition_) {
    turret = turret_;
    currentPosition = 0;
    targetPosition = targetPosition_;
    addRequirements(turret);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentPosition = 0;
    turret.setVelocity(0);
  }

  public double curve(double input, double maxSpeed) {
    if (Math.abs(input) > 0.0225) {
      return Math.signum(input) * (0.3 + Math.pow(Math.abs(input / maxSpeed), 0.65) * maxSpeed);
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
    currentPosition = turret.getPosition();
    angSeparation = targetPosition - currentPosition;
    double input = MathUtil.clamp(angSeparation, -1, 1);
    double output = 2 * curve(input);
    double actualOutput = 1.5 * OI.operatorController.getRawAxis(4);
    turret.setVelocity(output);
    SmartDashboard.putNumber("[T-POS] Actual velocity", actualOutput);
    SmartDashboard.putNumber("[T-POS] Intended velocity", output);
    SmartDashboard.putNumber("[T-POS] Angular separation", angSeparation);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(angSeparation) <= 0.03);
  }
}
