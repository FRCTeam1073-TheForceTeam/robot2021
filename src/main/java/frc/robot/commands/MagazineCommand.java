// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Magazine;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class MagazineCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Collector collector;
    private final Magazine magazine;
    private final Bling bling;
    private final double velocity;
    private boolean sensor;
    private boolean hadNothing;
    private boolean isFinished;
    private boolean hasFinishedNormally;
    private int checkNumPreviousMemoryEntries;
    private int loopsFalse;

    /**
     * Creates a new MagazineCommand.
     *
     * @param magazine The magazine used by this command.
     * @param bling    The bling used by this command.
     * @param power    The power the magazine isset to by this command.
     */
    public MagazineCommand(Collector collector, Magazine magazine, Bling bling, double velocity,
            int checkNumPreviousMemoryEntries) {
        this.collector = collector;
        this.magazine = magazine;
        this.bling = bling;
        this.velocity = velocity;
        this.checkNumPreviousMemoryEntries = checkNumPreviousMemoryEntries;
        addRequirements(magazine);
        addRequirements(bling);
    }

    /**
     * Creates a new MagazineCommand that runs at 15% power.
     *
     * @param magazine The magazine used by this command.
     * @param bling    The bling used by this command.
     */
    public MagazineCommand(Collector collector, Magazine magazine, Bling bling) {
        this(collector, magazine, bling, 0.35, 0);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        sensor = magazine.getSensor();
        hadNothing = false;
        isFinished = false;
        hasFinishedNormally = true;
        if (!sensor) {
            bling.setArray("red");
            bling.setColorRGBAll(bling.rgbArr[0], bling.rgbArr[1], bling.rgbArr[2]);
            hadNothing = true;
            hasFinishedNormally = false;
            isFinished = true;
        }
        loopsFalse = 0;
        if (checkNumPreviousMemoryEntries > 0
                && !RobotContainer.memory.havePreviousFinished(checkNumPreviousMemoryEntries)) {
            hasFinishedNormally = false;
            isFinished = true;
        }
    }

    public boolean hadNothing() {
        return hadNothing;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        sensor = magazine.getSensor();
        if (sensor) {
            collector.setCollect(0.45);
            magazine.setVelocity(velocity);
            bling.setArray("blue");
            loopsFalse = 0;
        } else {
            collector.setCollect(0.0);
            magazine.setVelocity(0.0);
            bling.setArray("purple");
            loopsFalse++;
        }
        bling.setColorRGBAll(bling.rgbArr[0], bling.rgbArr[1], bling.rgbArr[2]);
        if (loopsFalse > 9) {
            isFinished = true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        magazine.setPower(0.0);
        collector.setCollect(0.0);
        RobotContainer.memory.addToMemory("MagazineCommand", hasFinishedNormally);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
