// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class CollectCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;
    private final Collector collector;
    private final Magazine magazine;
    private final Bling bling;
    private final double maxPower;
    private double powerMultiplier;
    private double velocity;
    private boolean firstLoop;
    private long initialTime;
    private long time;
    private boolean shouldUnstall;
    private boolean isFinished;
    private boolean hasFinishedNormally;
    private int checkNumPreviousMemoryEntries;
    private int trueLoops;

    /**
     * Creates a new CollectCommand.
     *
     * @param colletor The collector used by this command.
     * @param magazine The magazine used by this command.
     * @param bling    The bling used by this command.
     * @param maxPower    The power the collector should run at for this command.
     * @param time     The time the collector should run for for this command
     *                 (milliseconds).
     */
    public CollectCommand(Drivetrain drivetrain, Collector collector, Magazine magazine, Bling bling, double maxPower,
            int checkNumPreviousMemoryEntries) {
        this.drivetrain = drivetrain;
        this.collector = collector;
        this.magazine = magazine;
        this.bling = bling;
        this.maxPower = maxPower;
        this.checkNumPreviousMemoryEntries = checkNumPreviousMemoryEntries;
        addRequirements(drivetrain);
        addRequirements(collector);
        addRequirements(magazine);
        addRequirements(bling);
    }

    /**
     * Creates a new CollectCommand that runs the Collector at 100%.
     *
     * @param colletor  The collector used by this command.
     * @param magazine  The magazine used by this command.
     * @param bling     The bling used by this command.
     * @param maxPower  The power the collector should run at for this command.
     */
    public CollectCommand(Drivetrain drivetrain, Collector collector, Magazine magazine, Bling bling, double maxPower) {
        this(drivetrain, collector, magazine, bling, maxPower, 0);
    }

    /**
     * Creates a new CollectCommand that runs the Collector at 100%.
     *
     * @param colletor The collector used by this command.
     * @param magazine The magazine used by this command.
     * @param bling    The bling used by this command.
     */
    public CollectCommand(Drivetrain drivetrain, Collector collector, Magazine magazine, Bling bling) {
        this(drivetrain, collector, magazine, bling, 1.0, 0);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        firstLoop = true;
        powerMultiplier = 1.0;
        velocity = 0.35;
        shouldUnstall = false;
        isFinished = false;
        trueLoops = 0;
        hasFinishedNormally = true;
        if (checkNumPreviousMemoryEntries > 0
                && !RobotContainer.memory.havePreviousFinished(checkNumPreviousMemoryEntries)) {
            hasFinishedNormally = false;
            isFinished = true;
        }
    }

    public boolean didStall() {
        return shouldUnstall;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (firstLoop) {
            firstLoop = false;
            initialTime = System.currentTimeMillis();
        }
        time = System.currentTimeMillis();
        if (shouldUnstall) {
            if (time - initialTime >= 5000) {
                powerMultiplier = 0.0;
                velocity = 0.0;
                drivetrain.setVelocity(0.0, 0.0);
                collector.setCollect(0.0);
                isFinished = true;
            }
        } else {
            if (collector.isStalled()) {
                bling.setArray("red");
                initialTime = System.currentTimeMillis();
                shouldUnstall = true;
                velocity = 0.0;
                powerMultiplier *= -1;
                hasFinishedNormally = false;
            } else if (time - initialTime >= 1600) {
                velocity = 0.0;
                bling.setArray("purple");
            } else {
                bling.setArray("blue");
            }
        }

        collector.setCollect(powerMultiplier * maxPower);
        drivetrain.setVelocity(velocity, 0.0);
        bling.setColorRGBAll(bling.rgbArr[0], bling.rgbArr[1], bling.rgbArr[2]);
        if (collector.getSensor()) {
            trueLoops++;
        } else {
            trueLoops = 0;
        }
        if (trueLoops > 4) {
            isFinished = true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.setVelocity(0.0, 0.0);
        collector.setCollect(0.0);
        bling.setColorRGBAll(0, 0, 0);
        RobotContainer.memory.addToMemory("CollectCommand", hasFinishedNormally);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
