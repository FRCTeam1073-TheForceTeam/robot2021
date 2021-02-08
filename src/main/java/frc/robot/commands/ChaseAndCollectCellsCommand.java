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
    private boolean isFinished;

    /**
     * The enum for the states the tracked powercell can be in: NOT_VISIBLE, LEFT,
     * ALIGNED, and RIGHT
     */
    private enum AlignState {
        NOT_VISIBLE, LEFT, ALIGNED, RIGHT;
    }

    private AlignState alignState;
    private int collectedCells;
    private PowerCellData powerCellData;
    private boolean hasData;
    private int loopsWithoutData;
    private boolean skipScan;
    private double rotationalSpeedMultiplier;
    private double velocityMultiplier;
    private boolean shouldCollect;
    private boolean isCollecting;
    private Rotation2d initRotation;
    private Rotation2d currentRotation;
    private long initTime;
    private long currentTime;
    private double collectPower;
    private double magVelocity;

    /**
     * Creates a new ChaseAndCollectCellsCommand. This command will chase and
     * collect as many powercells (in it's field of vision) as you input. It will
     * stop after scanning for it unsuccessfully if you want this command to make
     * the robot scan for powercells. You can also decide how many loops of not
     * receiving updated data this command can use old data for. Maximum rotational
     * speed and maximum velocity can also be set.
     *
     * @param drivetrain             The drivetrain used by this command.
     * @param collector              The collector used by this command.
     * @param magazine               The magazine used by this command.
     * @param powerCellTracker       The powerCellTracker used by this command.
     * @param bling                  The bling used by this command.
     * @param numCollectCells        The number of power cells that this command
     *                               will try to collect.
     * @param shouldScan360          If this command should turn up to two radians
     *                               (360 degrees) scanning for powercells if it
     *                               can't see any.
     * @param runNumLoopsWithoutData The amount of execute loops this command should
     *                               pretend nothing changed from the last time it
     *                               could update its tracker data.
     * @param maxRotationalSpeed     The maximum speed this command will rotate the
     *                               robot at (the minimum is 25% of this).
     * @param maxVelocity            The maximum velocity this command will move the
     *                               robot at (the minimum is 25% of this).
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

    /**
     * Creates a new ChaseAndCollectCellsCommand. This command will chase and
     * collect as many powercells (in it's field of vision) as you input. It will
     * stop after scanning for it unsuccessfully if you want this command to make
     * the robot scan for powercells. You can also decide how many loops of not
     * receiving updated data this command can use old data for.
     *
     * @param drivetrain             The drivetrain used by this command.
     * @param collector              The collector used by this command.
     * @param magazine               The magazine used by this command.
     * @param powerCellTracker       The powerCellTracker used by this command.
     * @param bling                  The bling used by this command.
     * @param numCollectCells        The number of power cells that this command
     *                               will try to collect.
     * @param shouldScan360          If this command should turn up to two radians
     *                               (360 degrees) scanning for powercells if it
     *                               can't see any.
     * @param runNumLoopsWithoutData The amount of execute loops this command should
     *                               pretend nothing changed from the last time it
     *                               could update its tracker data.
     * @param maxRotationalSpeed     Is set to 1.5 radians per second for the
     *                               overloaded version of this command.
     * @param maxVelocity            Is set to 2.0 meters per second for the
     *                               overloaded version of this command.
     */
    public ChaseAndCollectCellsCommand(Drivetrain drivetrain, Collector collector, Magazine magazine,
            PowerCellTracker powerCellTracker, Bling bling, int numCollectCells, boolean shouldScan360,
            int runNumLoopsWithoutData) {
        this(drivetrain, collector, magazine, powerCellTracker, bling, numCollectCells, shouldScan360,
                runNumLoopsWithoutData, 1.5, 2.0);
    }

    /**
     * Creates a new ChaseAndCollectCellsCommand. This command will chase and
     * collect as many powercells (in it's field of vision) as you input.
     *
     * @param drivetrain             The drivetrain used by this command.
     * @param collector              The collector used by this command.
     * @param magazine               The magazine used by this command.
     * @param powerCellTracker       The powerCellTracker used by this command.
     * @param bling                  The bling used by this command.
     * @param numCollectCells        is set to 5 for the overloaded version of this
     *                               command will try to collect.
     * @param shouldScan360          The overloaded version of this command does not
     *                               make the robot scan for powercells if it can't
     *                               see any.
     * @param runNumLoopsWithoutData The overloaded version of this command can run
     *                               two execute loops on old data if it doesn't
     *                               update.
     * @param maxRotationalSpeed     Is set to 1.5 radians per second for the
     *                               overloaded version of this command.
     * @param maxVelocity            Is set to 2.0 meters per second for the
     *                               overloaded version of this command.
     */
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
        isCollecting = false;
        skipScan = true;
        collectPower = 0.0;
        magVelocity = 0.0;
    }

    /**
     * Updates powerCellData if possible and calls alignState() if it updated or if
     * the number of loopsWithoutData is not acceptable. It also manages the all the
     * variables that keep track of what to run in the execute loop (for example
     * skipScan).
     */
    private void update() {
        hasData = powerCellTracker.getCellData(powerCellData);

        if (shouldCollect) {
            System.out.println("COLLECTING");
            initTime = System.currentTimeMillis();
            shouldCollect = false;
            isCollecting = true;
            skipScan = true;

        } else if (isCollecting) {
            System.out.println("COLLECTING");

        } else if (hasData) {
            loopsWithoutData = 0;
            skipScan = true;
            alignState();

        } else if (!hasData && loopsWithoutData < runNumLoopsWithoutData) {
            loopsWithoutData++;

        } else if (skipScan) {
            System.out.println("LOST TRACK FOR THE " + loopsWithoutData + "TH TIME");
            skipScan = false;
            initRotation = drivetrain.getRobotPose().getRotation();
            initTime = System.currentTimeMillis();
            if (powerCellData.vx < 0) {
                rotationalSpeedMultiplier = 0.5;
            } else {
                rotationalSpeedMultiplier = -0.5;
            }
            velocityMultiplier = 0.0;
            alignState();
            loopsWithoutData++;

        } else {
            System.out.println("LOST TRACK FOR THE " + loopsWithoutData + "TH TIME");
            skipScan = false;
            alignState();
            loopsWithoutData++;
        }
    }

    /**
     * Sets the enum AlignState alignState (that keeps track of the states of the
     * tracked powercell) based on powerCellData.
     */
    private void alignState() {
        if (!hasData) {
            alignState = AlignState.NOT_VISIBLE;

        } else if (powerCellData.cx >= 156 && powerCellData.cx <= 165) {
            alignState = AlignState.ALIGNED;

        } else if (powerCellData.cx < 156) {
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
                collectPower = 0.6;
                magVelocity = 1.0;
            }

        } else {
            rotationalSpeedMultiplier = 0.0;
            velocityMultiplier = 0.0;
        }
    }

    /**
     * While the collector is running, this function will stop the drivetrain,
     * collector, and magazine after a set time (1 second for the drivetrain and 5
     * seconds for the collector and the magazine).
     */
    private void collect() {
        currentTime = System.currentTimeMillis();

        if (initTime - currentTime >= 1000) {
            velocityMultiplier = 0.0;
        }

        if (initTime - currentTime >= 5000) {
            collectPower = 0.0;
            magVelocity = 0.0;
            isCollecting = false;
            collectedCells++;
        }
    }

    /**
     * Will make the robot turn in the direction the last tracked powercell rolled
     * (based on powerCellData) until it tracks a powercell or made a whole rotation
     * around its own axis. If it makes a whole rotation without tracking any
     * powercell the command ends.
     */
    private void scan360() {
        currentRotation = drivetrain.getRobotPose().getRotation();
        currentTime = System.currentTimeMillis();

        if (initTime - currentTime >= 1000
                && initRotation.minus(currentRotation).getRadians() <= rotationalSpeedMultiplier * maxRotationalSpeed
                && initRotation.minus(currentRotation).getRadians() >= 0) {
            isFinished = true;
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        update();
        if (!isCollecting && skipScan) {
            multipliers();
        }
        if (isCollecting || shouldCollect) {
            collect();
        }
        drivetrain.setVelocity(velocityMultiplier * maxVelocity, rotationalSpeedMultiplier * maxRotationalSpeed);
        collector.setCollect(collectPower);
        magazine.setVelocity(magVelocity);
        isFinished = collectedCells >= numCollectCells;
        if (shouldScan360 && !skipScan) {
            scan360();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.setVelocity(0.0, 0.0);
        collector.setCollect(0.0);
        magazine.setVelocity(0.0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
