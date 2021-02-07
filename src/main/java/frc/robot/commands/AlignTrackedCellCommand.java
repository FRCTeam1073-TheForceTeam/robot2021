// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerCellTracker.PowerCellData;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AlignTrackedCellCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final PowerCellTracker powerCellTracker;
    private final Bling bling;
    private PowerCellData powerCellData;
    private PowerCellData lastPowerCellData;
    private boolean hasData;
    private int loopsWithoutData = 0;
    private boolean shouldScan;

    private enum AlignState {
        NOT_VISIBLE, LEFT, ALIGNED, RIGHT;
    }

    private AlignState alignState;
    private final double maxRotationalSpeed = 1.0;
    private final double maxVelocity = 2.0;
    private double rotationalSpeedMultiplier;
    private double velocityMultiplier;
    private Rotation2d initRotation;
    private Rotation2d currentRotation;

    /**
     * Creates a new Command.
     *
     * @param subsystem The subsystem used by this command.
     */
    public AlignTrackedCellCommand(Drivetrain drivetrain, PowerCellTracker powerCellTracker, Bling bling) {
        this.drivetrain = drivetrain;
        this.powerCellTracker = powerCellTracker;
        this.powerCellData = new PowerCellData();
        this.lastPowerCellData = new PowerCellData();
        this.bling = bling;
        addRequirements(drivetrain);
        addRequirements(bling);
    }

    private AlignState alignState() {
        AlignState alignState;
        if (!hasData) {
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

    private void multiplier() {
        rotationalSpeedMultiplier = -(powerCellData.cx - 160) / 160.0;
        velocityMultiplier = 0;
        if (rotationalSpeedMultiplier > 0 && rotationalSpeedMultiplier < 0.35) {
            rotationalSpeedMultiplier = 0.35;
        } else if (rotationalSpeedMultiplier < 0 && rotationalSpeedMultiplier > -0.35) {
            rotationalSpeedMultiplier = -0.35;
        }
        if (powerCellData.cx >= 136 && powerCellData.cx <= 176) {
            velocityMultiplier = 0.175;
        }
    }

    private void scanForCell() {
        initRotation = drivetrain.getRobotPose().getRotation();
        currentRotation = drivetrain.getRobotPose().getRotation();
        while (!hasData && initRotation.minus(currentRotation).getRadians() < 2 * Math.PI) {
            drivetrain.setVelocity(0.0, 0.0);
            hasData = powerCellTracker.getCellData(powerCellData);
            currentRotation = drivetrain.getRobotPose().getRotation();
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        powerCellTracker.getCellData(lastPowerCellData);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        hasData = powerCellTracker.getCellData(powerCellData);
        if (!hasData && loopsWithoutData < 2) {
            powerCellData = lastPowerCellData;
            loopsWithoutData++;
        } else if (hasData) {
            loopsWithoutData = 0;
            lastPowerCellData = powerCellData;
            shouldScan = true;
        }
        alignState = alignState();
        if (alignState == AlignState.LEFT || alignState == AlignState.RIGHT) {
            multiplier();
            drivetrain.setVelocity(velocityMultiplier * maxVelocity, rotationalSpeedMultiplier * maxRotationalSpeed);
        } else {
            drivetrain.setVelocity(0.0, 0.0);
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
