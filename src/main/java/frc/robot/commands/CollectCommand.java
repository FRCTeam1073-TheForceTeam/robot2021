// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Magazine;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class CollectCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Collector subsystem;
    private final Magazine subsystem2;
    private final Bling subsystem3;
    private double power;
    private long time;
    private long initTime = 0;
    private long currentTime = 0;

    /**
     * Creates a new CollectCommand.
     *
     * @param subsystem  The collector used by this command.
     * @param subsystem2 The magazine used by this command.
     * @param subsystem3 The bling used by this command.
     * @param power      The power the collector should run at for this command.
     * @param time       The time the collector should run for for this command.
     */
    public CollectCommand(Collector subsystem, Magazine subsystem2, Bling subsystem3, double power, long time) {
        this.subsystem = subsystem;
        this.subsystem2 = subsystem2;
        this.subsystem3 = subsystem3;
        this.power = power;
        this.time = time;
        addRequirements(subsystem);
        addRequirements(subsystem2);
        addRequirements(subsystem3);
    }

    /**
     * Creates a new CollectCommand that runs the Collector at 10% power for two
     * seconds.
     *
     * @param subsystem  The collector used by this command.
     * @param subsystem2 The magazine used by this command.
     * @param subsystem3 The bling used by this command.
     */
    public CollectCommand(Collector subsystem, Magazine subsystem2, Bling subsystem3) {
        this(subsystem, subsystem2, subsystem3, 0.1, 2000);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        initTime = System.currentTimeMillis();
        subsystem.manipulateIsDeployed(true);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        subsystem.setCollect(power);
        currentTime = System.currentTimeMillis();
        // TODO: Bling
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return time <= currentTime - initTime;
    }
}
