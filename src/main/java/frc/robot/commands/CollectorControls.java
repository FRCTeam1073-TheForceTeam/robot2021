// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.OI;

public class CollectorControls extends CommandBase {

    private Collector collector;
    private double power;

    /** Creates a new MagazineControls. */
    public CollectorControls(Collector collector) {
        this.collector = collector;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(collector);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        collector.setCollect(0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (OI.operatorController.getAButton()) {
            power = OI.operatorController.getRawAxis(1);
        } else {
            power = 0;
        }
        SmartDashboard.putNumber("Collector Power", power);
        collector.setCollect(power);
        if (OI.operatorController.getBumperPressed(Hand.kLeft)) {
            collector.lower();
        }
        if (OI.operatorController.getBumperPressed(Hand.kRight)) {
            collector.raise();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        collector.setCollect(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
