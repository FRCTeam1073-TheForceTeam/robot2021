// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.subsystems.Turret;
import frc.robot.Utility;
import frc.robot.subsystems.OI;

public class TurretControls extends CommandBase {

  private Turret turret;
  private double turretVelocity;

  /** Creates a new MagazineControls. */
  public TurretControls(Turret turret) {
    this.turret = turret;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turretVelocity = 1.5 * Utility.deadzone(OI.operatorController.getRawAxis(4));
      // turretVelocity = 0;
      // if (Math.abs(OI.operatorController.getRawAxis(4)) > 0.2) {
      //   turretVelocity = 0.75 * Math.signum(OI.operatorController.getRawAxis(4));
      // }
    //SmartDashboard.putNumber("Magazine Power", magazineVelocity);
    turret.setVelocity(turretVelocity);
    // magazine.setVelocity(magazineVelocity);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
