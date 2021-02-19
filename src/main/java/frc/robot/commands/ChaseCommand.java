// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

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
    private boolean isFinished;

    /**
     * The enum for the states the tracked powercell can be in: NOT_VISIBLE, LEFT,
     * ALIGNED, and RIGHT
     */
    private enum AlignState {
        NOT_VISIBLE, LEFT, ALIGNED, RIGHT;
    }

    private AlignState alignState;
    private PowerCellData powerCellData;
    private boolean hasData;
    private int loopsWithoutData;
    private double rotationalSpeedMultiplier;
    private double velocityMultiplier;
    private double initialAngle;
    private double angle;
    private long initialTime;
    private long time;
    private boolean skipScan;
    private boolean isScanning;

    /**
     * Creates a new ChaseAndCollectCellsCommand. This command will chase and
     * collect as many powercells (in it's field of vision) as you input. It will
     * stop after scanning for it unsuccessfully if you want this command to make
     * the robot scan for powercells. You can also decide how many loops of not
     * receiving updated data this command can use old data for. Maximum rotational
     * speed and maximum velocity can also be set.
     *
     * @param drivetrain             The drivetrain used by this command.
     * @param powerCellTracker       The powerCellTracker used by this command.
     * @param bling                  The bling used by this command.
     * @param runNumLoopsWithoutData The amount of execute loops this command should
     *                               pretend nothing changed from the last time it
     *                               could update its tracker data.
     * @param maxRotationalSpeed     The maximum speed this command will rotate the
     *                               robot at (the minimum is 25% of this).
     * @param maxVelocity            The maximum velocity this command will move the
     *                               robot at (the minimum is 25% of this).
     */
    public ChaseCommand(Drivetrain drivetrain, PowerCellTracker powerCellTracker, Bling bling,
            double maxRotationalSpeed, double maxVelocity) {
        this.drivetrain = drivetrain;
        this.powerCellTracker = powerCellTracker;
        this.bling = bling;
        this.maxRotationalSpeed = maxRotationalSpeed;
        this.maxVelocity = maxVelocity;
        addRequirements(drivetrain);
        addRequirements(bling);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        powerCellData = new PowerCellData();
        loopsWithoutData = 0;
        skipScan = true;
        isScanning = false;
        isFinished = false;
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
            alignState();
            loopsWithoutData = 0;
            skipScan = true;
            isScanning = true;

        } else if (isScanning) {
            time = System.currentTimeMillis();
            angle = drivetrain.getRobotPose().getRotation().getRadians();
            System.out.println("SCANNING FOR " + (time - initialTime));
            velocityMultiplier = 0.0;

        } else if (!skipScan) {
            loopsWithoutData++;
            initialTime = System.currentTimeMillis();
            initialAngle = drivetrain.getRobotPose().getRotation().getRadians();
            System.out.println("LOST TRACK FOR THE " + loopsWithoutData + "TH TIME");
            alignState();
            skipScan = true;
            isScanning = true;
            rotationalSpeedMultiplier = Math.max(-(powerCellData.cx - 160) / 160.0, 0.25);
            velocityMultiplier = 0.0;
        } else {
            loopsWithoutData++;
            System.out.println("LOST TRACK FOR THE " + loopsWithoutData + "TH TIME");
            alignState();
            rotationalSpeedMultiplier = 0.0;
            velocityMultiplier = 0.0;
        }
    }

    /**
     * Sets the enum AlignState alignState (that keeps track of the states of the
     * tracked powercell) based on powerCellData.
     */
    private void alignState() {
        if (!hasData) {
            alignState = AlignState.NOT_VISIBLE;

        } else if (powerCellData.cx >= 146 && powerCellData.cx <= 175) {
            alignState = AlignState.ALIGNED;

        } else if (powerCellData.cx < 146) {
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
            rotationalSpeedMultiplier = Math.max(-(powerCellData.cx - 160) / 160.0, 0.25);
            velocityMultiplier = -(powerCellData.cy - 240) / 240.0;

            if (rotationalSpeedMultiplier > 0 && rotationalSpeedMultiplier < 0.25) {
                rotationalSpeedMultiplier = 0.25;

            } else if (rotationalSpeedMultiplier < 0 && rotationalSpeedMultiplier > -0.25) {
                rotationalSpeedMultiplier = -0.25;

            }

            if (powerCellData.vy < -30) {
                velocityMultiplier = 1.0;

            }

        } else if (alignState == AlignState.ALIGNED) {
            rotationalSpeedMultiplier = Math.max(-(powerCellData.cx - 160) / 160.0, 0.15);
            ;
            velocityMultiplier = -(powerCellData.cy - 240) / 240.0;

            if (powerCellData.vy < -30) {
                velocityMultiplier = 1.0;
            }

            if (powerCellData.cy >= 200) {
                System.out.println("DONEDONEDONE");
                isFinished = true;
            }

        } else {
            rotationalSpeedMultiplier = 0.0;
            velocityMultiplier = 0.0;
            skipScan = false;
        }
    }

    /**
     * Will make the robot turn in the direction the last tracked powercell rolled
     * (based on powerCellData) until it tracks a powercell or made a whole rotation
     * around its own axis. If it makes a whole rotation without tracking any
     * powercell the command ends.
     */
    private void scan360() {
        if (time - initialTime >= 1000
                && MathUtil.angleModulus(angle - initialAngle) <= rotationalSpeedMultiplier * maxRotationalSpeed
                && MathUtil.angleModulus(angle - initialAngle) >= 0) {
            isFinished = true;
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        update();
        if (!isScanning || skipScan) {
            multipliers();
        } else {
            scan360();
        }
        System.out.println("vmultiplier " + velocityMultiplier + " rmultiplier " + rotationalSpeedMultiplier);
        drivetrain.setVelocity(velocityMultiplier * maxVelocity, rotationalSpeedMultiplier * maxRotationalSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.setVelocity(0.0, 0.0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
