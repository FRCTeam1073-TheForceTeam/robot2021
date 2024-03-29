// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.OI;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;
import frc.robot.subsystems.Bling;


public class TurretPortAlignCommand extends CommandBase {

  Turret turret;
  PowerPortTracker portTracker;
  PowerPortData portData;
  double coordinateSeparation;
  boolean endWhenAligned;
  int framesWithoutSignal;
  int maxFramesWithoutSignal;
  int commandingZero;
  Bling bling;

  public TurretPortAlignCommand(Turret turret_, PowerPortTracker portTracker_, boolean endWhenAligned_, int maxFramesWithoutSignal_) {
	turret = turret_;
	portTracker = portTracker_;
	endWhenAligned = endWhenAligned_;
	maxFramesWithoutSignal = maxFramesWithoutSignal_;
	commandingZero = 0;
	bling = RobotContainer.getBling();
	addRequirements(turret);
	// Use addRequirements() here to declare subsystem dependencies.
  }

  public TurretPortAlignCommand(Turret turret_, PowerPortTracker portTracker_, boolean endWhenAligned_) {
	this(turret_, portTracker_, endWhenAligned_, 10);
  }

  public TurretPortAlignCommand(Turret turret_, PowerPortTracker portTracker_) {
	this(turret_, portTracker_, false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
	coordinateSeparation = 0;
	portData = new PowerPortData();
	framesWithoutSignal = 0;
	commandingZero = 0;
	turret.setVelocity(0);
	bling.setSlot(3, 0, 128, 128);
  }

  public double curve(double input, double maxSpeed) {
	if (Math.abs(input) > 0.02) {
	  return Math.signum(input) * (0.15 + Math.pow(Math.abs(input / maxSpeed), 0.65) * maxSpeed);
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
	double actualOutput = 1.5 * OI.operatorController.getRawAxis(4);
	if (hasData) {
	  framesWithoutSignal = 0;
	  //Inverted (left is 1, right is -1) so it moves in the right direction.
	  coordinateSeparation = 1 - 2 * (((double) portData.cx) / ((double) portTracker.getImageWidth()));
	} else if (framesWithoutSignal != 0) {
	  framesWithoutSignal = 0;
	} else {
	  framesWithoutSignal++;
	}

	// if (hasData) {
	//   SmartDashboard.putString("[T-AGN] Camera signal", "[CONNECTED]");
	// } else {
	//   SmartDashboard.putString("[T-AGN] Camera signal", "[NO DATA]");
	// }

	if (hasData || (framesWithoutSignal < maxFramesWithoutSignal)) {
	  // if (hasData) {
	  //   SmartDashboard.putString("[T-AGN] Scan status", "[CONNECTED]");
	  // } else {
	  //   SmartDashboard.putString("[T-AGN] Scan status", "[DISCONNECTED; USING LAST DATA]");
	  // }

	  //I know that it shouldn't need clamping, but I want to make sure
	  double input = MathUtil.clamp(coordinateSeparation, -1, 1);
	  double curveit = 1.25 * curve(input);
	  if (Math.abs(curveit) < 0.05) {
		  bling.setSlot(3, 128, 0, 128);
		  commandingZero++;
	  } else {
		  bling.setSlot(3, 0, 255, 255);
		  commandingZero = 0;
	  }
	  double output = 1.25 * curve(input);
	  turret.setVelocity(output);
	  // SmartDashboard.putNumber("[T-AGN] Intended velocity", output);
	  // SmartDashboard.putNumber("[T-AGN] Camera coordinate separation", coordinateSeparation);
	} else {
	  // SmartDashboard.putString("[T-AGN] Scan status", "[TOO LONG WITHOUT DATA, STOPPING]");
	  // turret.setVelocity(actualOutput);
	}
	// SmartDashboard.putNumber("[T-AGN] Frames without signal", framesWithoutSignal);
	// SmartDashboard.putNumber("[T-AGN] Actual velocity", actualOutput);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
	bling.setSlot(3, 0, 0, 0);
	turret.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
	return (endWhenAligned && 
	(Math.abs(coordinateSeparation) <= Constants.ACCEPTABLE_PORT_TRACKER_ALIGNMENT))
	 || (endWhenAligned && commandingZero > 3);
  }
}
