// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.Utility;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.OI;

public class CollectorControls extends CommandBase {

    private Collector collector;
    private Bling bling;
    private double power;

    /** Creates a new MagazineControls. */
    public CollectorControls(Collector collector) {
        this.collector = collector;
        bling = RobotContainer.getBling();
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
        if (collector.isStalled()) {
            power = -0.3;  
        } else if (OI.driverController.getBumper(Hand.kLeft)) {
            power = 1;
            bling.setSlot(2, 255, 128, 0);
        }else if (OI.driverController.getBumper(Hand.kRight)) {
            power = -1;
            bling.setSlot(2, 255, 0, 255);
        } else {
            power = 0;
            bling.setSlot(2, 0, 0, 0);
        }
        collector.setCollect(power);
        if (OI.driverController.getBackButtonPressed()) {
            collector.raise();
        }
        if (OI.driverController.getStartButtonPressed()) {
            collector.lower();
        }
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        collector.setCollect(0);
        bling.setSlot(2, 0, 0, 0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
