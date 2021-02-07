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

    private enum AlignState {
        NOT_VISIBLE, LEFT, ALIGNED, RIGHT;
    }

    private AlignState alignState;

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

    private AlignState alignState() {
        AlignState alignState;
        if (!powerCellTracker.getCellData(powerCellData)) {
            alignState = AlignState.NOT_VISIBLE;
        } else if (powerCellData.cx >= 156 && powerCellData.cx <= 165) {
            alignState = AlignState.ALIGNED;
        } else if (powerCellData.cx < 156) {
            alignState = AlignState.LEFT;
        } else {
            alignState = AlignState.RIGHT;
        }
        return alignState;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        alignState = alignState();
        if (alignState == AlignState.LEFT) {
            drivetrain.setVelocity(0.0, 0.4);
        } else if (alignState == AlignState.RIGHT) {
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
