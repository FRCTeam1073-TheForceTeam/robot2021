// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Magazine;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DispenseCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Collector collector;
    private final Magazine magazine;
    private final Bling bling;
    private int numCellsToDispense;

    /**
     * Creates a new DispenseCommand that makes the robot drive a given distance at
     * a given velocity.
     *
     * @param colletor          The collector used by this command.
     * @param magazine          The magazine used by this command.
     * @param bling             The bling used by this command.
     * @param numCellsToDispnse The number of Powercells that will be dispensed
     */
    public DispenseCommand(Collector collector, Magazine magazine, Bling bling, int numCellsToDispense) {
        this.collector = collector;
        this.magazine = magazine;
        this.bling = bling;
        this.numCellsToDispense = numCellsToDispense;
        addRequirements(collector);
        addRequirements(magazine);
        addRequirements(bling);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // TODO: set the collector to running outwards with Bling
        // TODO: set the magazine to running upwards with Bling
        // TODO: end after an experimentally determined time
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
