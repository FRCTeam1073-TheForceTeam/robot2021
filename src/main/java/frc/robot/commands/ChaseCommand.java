// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerCellTracker.PowerCellData;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;

public class ChaseCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final PowerCellTracker powerCellTracker;
    private final Bling bling;
    private final double maxRotationalSpeed;
    private final double maxVelocity;
    private final boolean shouldScan;
    private final boolean scanRight;
    private boolean isFinished;
    private boolean hasFinishedNormally;

    /**
     * The enum for the states the tracked powercell can be in: NOT_VISIBLE, LEFT,
     * ALIGNED, and RIGHT
     */
    private enum AlignState {
        NOT_VISIBLE, LEFT, ALIGNED, RIGHT;
    }

    private AlignState alignState;
    private PowerCellData powerCellData;
    private PowerCellData lastData;
    private boolean hasData;
    private int loopsWithoutData;
    private double rotationalSpeedMultiplier;
    private double scanRotationalSpeedMultiplier;
    private double velocityMultiplier;
    private double initialAngle;
    private long initialTime;
    private long time;
    private long timeToTurn;
    private boolean skipScan;
    private boolean isScanning;

    /**
     * Creates a new ChaseAndCollectCellsCommand. This command will find and line up
     * to a powercells. It will stop after scanning for it unsuccessfully if you
     * want this command to make the robot scan for powercells. Maximum rotational
     * speed and maximum velocity can also be set.
     *
     * @param drivetrain         The drivetrain used by this command.
     * @param powerCellTracker   The powerCellTracker used by this command.
     * @param bling              The bling used by this command.
     * @param maxRotationalSpeed The maximum speed this command will rotate the
     *                           robot at (the minimum is 25% of this).
     * @param maxVelocity        The maximum velocity this command will move the
     *                           robot at (the minimum is 25% of this).
     * @param shouldScan         If true this command should turn scanning for
     *                           powercells until it can see one or has turned 2
     *                           radians. If the latter is the case it will end the
     *                           program.
     * @param scanRight          Should the robot start scanning to the right, if
     *                           set to false it will start to the left
     */
    public ChaseCommand(Drivetrain drivetrain, PowerCellTracker powerCellTracker, Bling bling,
            double maxRotationalSpeed, double maxVelocity, boolean shouldScan, boolean scanRight) {
        this.drivetrain = drivetrain;
        this.powerCellTracker = powerCellTracker;
        this.bling = bling;
        this.maxRotationalSpeed = maxRotationalSpeed;
        this.maxVelocity = maxVelocity;
        this.shouldScan = shouldScan;
        this.scanRight = scanRight;
        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        powerCellData = new PowerCellData();
        lastData = new PowerCellData();
        hasData = false;
        loopsWithoutData = 0;
        skipScan = true;
        isScanning = false;
        isFinished = false;
        hasFinishedNormally = true;
    }

    /**
     * Updates powerCellData if possible and calls alignState() if it updated or if
     * the number of loopsWithoutData is not acceptable. It also manages the all the
     * variables that keep track of what to run in the execute loop (for example
     * skipScan).
     */
    private void update() {
        hasData = powerCellTracker.getCellData(powerCellData);

        if (hasData) {
            bling.setArray("green");
            alignState();
            loopsWithoutData = 0;
            skipScan = true;
            isScanning = false;
            lastData = powerCellData;

        } else if (isScanning) {
            bling.setArray("yellow");
            time = System.currentTimeMillis();

        } else if (!skipScan && shouldScan) {
            loopsWithoutData++;
            alignState();
            skipScan = true;
            isScanning = true;
            initialAngle = drivetrain.getRobotPose().getRotation().getRadians();
            if (scanRight) {
                // if (Math.signum(initialAngle) < 0) {
                scanRotationalSpeedMultiplier = MathUtil.clamp(Math.abs(-(lastData.cx - 146) / 100.0), 0.35, 1);
            } else {
                scanRotationalSpeedMultiplier = -MathUtil.clamp(Math.abs(-(lastData.cx - 146) / 100.0), 0.35, 1);
            }
            // timeToTurn = (long) (1000 * (2 * Math.PI) / (scanRotationalSpeedMultiplier *
            // maxVelocity));
            timeToTurn = 8000;
            initialTime = System.currentTimeMillis();
        } else {
            bling.setArray("orange");
            loopsWithoutData++;
            if (loopsWithoutData > 7) {
                bling.setArray("red");
                rotationalSpeedMultiplier = 0.0;
                velocityMultiplier = 0.0;
            } else {
                powerCellData = lastData;
            }
            skipScan = true;
            isScanning = false;
            alignState();
        }
    }

    /**
     * Sets the enum AlignState alignState (that keeps track of the states of the
     * tracked powercell) based on powerCellData.
     */
    private void alignState() {
        if (!hasData) {
            alignState = AlignState.NOT_VISIBLE;

        } else if (powerCellData.cx >= 144 && powerCellData.cx <= 175) {
            alignState = AlignState.ALIGNED;

        } else if (powerCellData.cx < 144) {
            alignState = AlignState.LEFT;

        } else {
            alignState = AlignState.RIGHT;

        }
    }

    /**
     * Sets the multipliers for rotation and velovity (rotationalSpeedMultiplier and
     * velocityMultiplier), the collectPower, and the magVelocity based on enum
     * AlignState alignState and powerCellData - unless the robot is collecting or
     * scanning for powercells.
     */
    private void multipliers() {
        if (alignState == AlignState.LEFT || alignState == AlignState.RIGHT) {
            rotationalSpeedMultiplier = MathUtil.clamp(-(powerCellData.cx - 146) / 100.0, -1.0, 1.0);
            velocityMultiplier = MathUtil.clamp(-(powerCellData.cy - 239) / 140.0, 0.2, 1.0);

        } else if (alignState == AlignState.ALIGNED) {
            rotationalSpeedMultiplier = MathUtil.clamp(-(powerCellData.cx - 146) / 100.0, -1.0, 1.0);
            velocityMultiplier = MathUtil.clamp(-(powerCellData.cy - 239) / 120.0, 0.2, 1.0);

        } else {
            if (loopsWithoutData > 5) {
                rotationalSpeedMultiplier = 0.0;
                velocityMultiplier = 0.0;
            }
            if (shouldScan) {
                skipScan = false;
            }
        }

        if (powerCellData.cy >= 170) {
            drivetrain.setVelocity(0.0, 0.0);
            rotationalSpeedMultiplier = 0.0;
            velocityMultiplier = 0.0;
            isFinished = true;
        }
    }

    /**
     * Will make the robot turn in the direction the last tracked powercell rolled
     * (based on powerCellData) until it tracks a powercell or made a whole rotation
     * around its own axis. If it makes a whole rotation without tracking any
     * powercell the command ends.
     */
    private void scan360() {

        if (time - initialTime >= 100) {
            velocityMultiplier = 0.0;
            rotationalSpeedMultiplier = scanRotationalSpeedMultiplier;
        }

        if ((time - initialTime >= timeToTurn)) {
            rotationalSpeedMultiplier = 0.0;
            velocityMultiplier = 0.0;
            // hasFinishedNormally = false;
            // isFinished = true;
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        update();
        if (!isScanning || skipScan) {
            multipliers();
        } else if (shouldScan && (isScanning || !skipScan)) {
            scan360();
        }
        drivetrain.setVelocity(velocityMultiplier * maxVelocity, rotationalSpeedMultiplier * maxRotationalSpeed);
        bling.setColorRGBAll(bling.rgbArr[0], bling.rgbArr[1], bling.rgbArr[2]);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.setVelocity(0.0, 0.0);
        bling.setColorRGBAll(0, 0, 0);
        RobotContainer.memory.addToMemory("ChaseCommand", hasFinishedNormally);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}