// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;

/** An example command that uses an example subsystem. */
public class ShootCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Shooter shooter;
    private final Bling bling;
    private final double hoodAngle;
    private final double maxVelocity;
    private final long milliseconds;
    private boolean getInit;
    private long initialTime;
    private long time;

    /**
     * Creates a new ShooterCommand that sets the hood to an angle and then runs the
     * shooter for a set time with a velocity limit.
     *
     * @param shooter      The shooter used by this command.
     * @param bling        The bling used by this command.
     * @param hoodAngle    The angle the hood should be set to by this command.
     * @param maxVelocity  The maximum velocity this command will set the shooter
     *                     to.
     * @param milliseconds The milliseconds this command will run for.
     */
    public ShootCommand(Shooter shooter, Bling bling, double hoodAngle, double maxVelocity, long milliseconds) {
        this.shooter = shooter;
        this.bling = bling;
        this.hoodAngle = MathUtil.clamp(hoodAngle, shooter.getMinHoodAngle(), shooter.getMaxHoodAngle());
        this.maxVelocity = maxVelocity;
        this.milliseconds = milliseconds;
        addRequirements(shooter);
        addRequirements(bling);
    }

    /**
     * Creates a new ShooterCommand that runs the shooter for 3 seconds with a
     * velocity limit of 2 radians per second.
     *
     * @param shooter The shooter used by this command.
     * @param bling   The bling used by this command.
     */
    public ShootCommand(Shooter shooter, Bling bling) {
        this(shooter, bling, shooter.getHoodAngle(), 2.0, 3000);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        getInit = true;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // TODO: Bling
        if (getInit && Math.abs(shooter.getHoodAngle() - hoodAngle) < 0.02) {
            initialTime = System.currentTimeMillis();
            getInit = false;
        } else if (getInit) {
            shooter.setHoodAngle(hoodAngle);
        }
        time = System.currentTimeMillis();
        if (time - initialTime < milliseconds) {
            shooter.setFlywheelVelocity(maxVelocity);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        shooter.setFlywheelVelocity(0.0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return time - initialTime >= milliseconds;
    }
}
