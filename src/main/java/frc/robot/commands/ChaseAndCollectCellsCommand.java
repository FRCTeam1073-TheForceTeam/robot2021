// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerCellTracker.PowerCellData;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class ChaseAndCollectCellsCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Collector collector;
    private final Magazine magazine;
    private final PowerCellTracker powerCellTracker;
    private final Bling bling;
    private final int numCollectCells;
    private final boolean shouldScan360;
    private final int runNumLoopsWithoutData;
    private final double maxRotationalSpeed;
    private final double maxVelocity;
    private CollectCommand collect;

    private enum AlignState {
        NOT_VISIBLE, LEFT, ALIGNED, RIGHT;
    }

    private int collectedCells;
    private PowerCellData powerCellData;
    private boolean hasData;
    private int loopsWithoutData;
    private boolean skipScan;
    private AlignState alignState;
    private double rotationalSpeedMultiplier;
    private double velocityMultiplier;
    private boolean shouldCollect;
    private Rotation2d initRotation;
    private Rotation2d currentRotation;

    /**
     * Creates a new Command.
     *
     * @param subsystem The subsystem used by this command.
     */
    public ChaseAndCollectCellsCommand(Drivetrain drivetrain, Collector collector, Magazine magazine,
            PowerCellTracker powerCellTracker, Bling bling, int numCollectCells, boolean shouldScan360,
            int runNumLoopsWithoutData, double maxRotationalSpeed, double maxVelocity) {
        this.drivetrain = drivetrain;
        this.collector = collector;
        this.magazine = magazine;
        this.powerCellTracker = powerCellTracker;
        this.bling = bling;
        this.numCollectCells = numCollectCells;
        this.shouldScan360 = shouldScan360;
        this.runNumLoopsWithoutData = runNumLoopsWithoutData;
        this.maxRotationalSpeed = maxRotationalSpeed;
        this.maxVelocity = maxVelocity;
        addRequirements(drivetrain);
        addRequirements(bling);
    }

    public ChaseAndCollectCellsCommand(Drivetrain drivetrain, Collector collector, Magazine magazine,
            PowerCellTracker powerCellTracker, Bling bling, int numCollectCells, boolean shouldScan360,
            int runNumLoopsWithoutData) {
        this(drivetrain, collector, magazine, powerCellTracker, bling, numCollectCells, shouldScan360,
                runNumLoopsWithoutData, 1.5, 2.0);
    }

    public ChaseAndCollectCellsCommand(Drivetrain drivetrain, Collector collector, Magazine magazine,
            PowerCellTracker powerCellTracker, Bling bling) {
        this(drivetrain, collector, magazine, powerCellTracker, bling, 5, false, 2, 1.5, 2.0);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        powerCellData = new PowerCellData();
        loopsWithoutData = 0;
        collectedCells = 0;
        shouldCollect = false;
        collect = new CollectCommand(collector, magazine, bling, 0.6, 5500);
    }

    private void update() {
        hasData = powerCellTracker.getCellData(powerCellData);

        if (hasData) {
            loopsWithoutData = 0;
            skipScan = true;
            alignState = alignState();

        } else if (!hasData && loopsWithoutData < runNumLoopsWithoutData) {
            loopsWithoutData++;

        } else {
            System.out.println("LOST TRACK FOR THE " + loopsWithoutData + "TH TIME");
            skipScan = false;
            alignState = alignState();
            loopsWithoutData++;

        }
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

    private void multipliers() {
        if (alignState == AlignState.LEFT || alignState == AlignState.RIGHT) {
            rotationalSpeedMultiplier = -(powerCellData.cx - 160) / 160.0;
            velocityMultiplier = powerCellData.cy / 240.0;

            if (rotationalSpeedMultiplier > 0 && rotationalSpeedMultiplier < 0.25) {
                rotationalSpeedMultiplier = 0.25;

            } else if (rotationalSpeedMultiplier < 0 && rotationalSpeedMultiplier > -0.25) {
                rotationalSpeedMultiplier = -0.25;

            }

            if (powerCellData.cy > 40) {
                velocityMultiplier = 1.0;

            } else if (velocityMultiplier < 0.25) {
                velocityMultiplier = 0.25;
            }

        } else if (alignState == AlignState.ALIGNED) {
            rotationalSpeedMultiplier = 0.0;
            velocityMultiplier = powerCellData.cy / 240.0;

            if (powerCellData.cy > 40) {
                velocityMultiplier = 1.0;

            } else if (velocityMultiplier < 0.25) {
                velocityMultiplier = 0.25;
            }

            if (powerCellData.cy <= 40) {
                shouldCollect = true;
            }

        } else {
            rotationalSpeedMultiplier = 0.0;
            velocityMultiplier = 0.0;

        }
    }

    private void scan360() {
        if (!skipScan) {
            initRotation = drivetrain.getRobotPose().getRotation();
            currentRotation = drivetrain.getRobotPose().getRotation();

            while (!hasData && initRotation.minus(currentRotation).getRadians() < 2 * Math.PI) {
                drivetrain.setVelocity(0.0, 0.25 * maxRotationalSpeed);
                hasData = powerCellTracker.getCellData(powerCellData);
                currentRotation = drivetrain.getRobotPose().getRotation();

            }
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        update();
        if (hasData) {
            multipliers();
        }
        drivetrain.setVelocity(velocityMultiplier * maxVelocity, rotationalSpeedMultiplier * maxRotationalSpeed);
        if (shouldCollect) {
            ParallelCommandGroup.parallel(collect);
            collectedCells++;
        }
        if (shouldScan360) {
            scan360();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return collectedCells >= numCollectCells;
    }
}
