// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Magazine;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class MagazineCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Magazine magazine;
    private final Bling bling;
    private final double speed;
    private boolean sensor;
    private boolean hadNothing;
    private boolean isFinished;

    /**
     * Creates a new MagazineCommand.
     *
     * @param magazine The magazine used by this command.
     */
    public MagazineCommand(Magazine magazine, Bling bling, double speed) {
        this.magazine = magazine;
        this.bling = bling;
        this.speed = speed;
        addRequirements(magazine);
        addRequirements(bling);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        sensor = magazine.getSensor();
        hadNothing = false;
        isFinished = false;
        if (!sensor) {
            bling.setArray("red");
            bling.setColorRGBAll(bling.rgbArr[0], bling.rgbArr[1], bling.rgbArr[2]);
            hadNothing = true;
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
            magazine.setPower(0.15);
            bling.setArray("yellow");
        } else {
            magazine.setPower(0.0);
            bling.setArray("green");
            isFinished = true;
        }
        bling.setColorRGBAll(bling.rgbArr[0], bling.rgbArr[1], bling.rgbArr[2]);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        magazine.setPower(0.0);
        bling.setColorRGBAll(0, 0, 0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
