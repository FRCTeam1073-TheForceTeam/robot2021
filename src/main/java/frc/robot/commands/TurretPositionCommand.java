// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class TurretPositionCommand extends CommandBase {

  Turret turret;
  PowerPortTracker portTracker;
  double currentPosition;
  double targetPosition;

  /**
   * Sets the turret's angle in radians.
   * @param turret_ The turret object
   * @param targetPosition_ The target angle in radians
   */
  public TurretPositionCommand(Turret turret_, double targetPosition_) {
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
    if (Math.abs(input) > 0.0375) {
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
    double currentPosition = turret.getPosition();
    double input = MathUtil.clamp((currentPosition - targetPosition), -1, 1);
    double output = -1.5 * curve(input);
    double actualOutput = 1.5 * OI.operatorController.getRawAxis(4);
    turret.setVelocity(actualOutput);
    SmartDashboard.putNumber("[T-POS] Actual velocity", actualOutput);
    SmartDashboard.putString("[T-POS] Output log", "[D]: INPUT (" + input + "), OUTPUT (" + output + ")");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(targetPosition - currentPosition) <= 0);
  }
}
