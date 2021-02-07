// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerCellTracker.PowerCellData;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AlignTrackedCellCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final PowerCellTracker powerCellTracker;
    private PowerCellData powerCellData;
    private final Bling bling;

    private enum IsAligned {
        NOT_VISIBLE, LEFT, ALIGNED, RIGHT;
    }

    private IsAligned isAligned;

    /**
     * Creates a new Command.
     *
     * @param subsystem The subsystem used by this command.
     */
    public AlignTrackedCellCommand(Drivetrain drivetrain, PowerCellTracker powerCellTracker, Bling bling) {
        this.drivetrain = drivetrain;
        this.powerCellTracker = powerCellTracker;
        this.bling = bling;
        addRequirements(drivetrain);
        addRequirements(bling);
    }

    private IsAligned isAligned() {
        IsAligned isAligned;
        if (!powerCellTracker.getCellData(powerCellData)) {
            isAligned = IsAligned.NOT_VISIBLE;
        } else if (powerCellData.cx >= 156 && powerCellData.cx <= 165) {
            isAligned = IsAligned.ALIGNED;
        } else if (powerCellData.cx < 156) {
            isAligned = IsAligned.LEFT;
        } else {
            isAligned = IsAligned.RIGHT;
        }
        return isAligned;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        isAligned = isAligned();
        if (isAligned == IsAligned.LEFT) {
            drivetrain.setVelocity(0.0, 0.4);
        } else if (isAligned == IsAligned.RIGHT) {
            drivetrain.setVelocity(0.0, -0.4);
        }
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
