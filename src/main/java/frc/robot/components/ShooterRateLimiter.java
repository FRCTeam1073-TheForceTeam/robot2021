// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.components;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Utility;

/**
 * Custom rate limiter for the shooter.
 * It behaves like the SlewRateLimiter, but it can accelerate faster at lower speeds.
 */
public class ShooterRateLimiter {
    private double baseRateLimit;
    private double initialValue;
    private double flywheelVelocity;
    private double internalValue;
    private double prevTime;
    //The rate limit at 0 radians/s (units: raw units)
    private double zeroRateLimit;
    //The flywheel velocity where the base rate limit applies (units: radians/s)
    private double standardLimitVelocity;

    public ShooterRateLimiter(double baseRateLimit_, double initialValue_, double flywheelVelocity_, double zeroRateLimit_, double standardLimitVelocity_) {
        initialValue = initialValue_;
        internalValue = initialValue;
        flywheelVelocity = flywheelVelocity_;

        zeroRateLimit = zeroRateLimit_;
        baseRateLimit = baseRateLimit_;
        standardLimitVelocity = standardLimitVelocity_;
    }

    public ShooterRateLimiter(double baseRateLimit_, double initialValue_, double flywheelVelocity_) {
        this(baseRateLimit_, initialValue_, flywheelVelocity_, 4500, 300);
    }

    public ShooterRateLimiter(double baseRateLimit_, double initialValue_) {
        this(baseRateLimit_, initialValue_, 0);
    }

    public ShooterRateLimiter(double baseRateLimit_) {
        this(baseRateLimit_, 0, 0);
    }

    public double getRateLimit() {
        return Utility.lerp(zeroRateLimit, baseRateLimit, MathUtil.clamp(flywheelVelocity / standardLimitVelocity, 0, 1));
    }

    /**
     * Updates internal velocity and returns it
     * @param input Input target velocity
     * @param velocity Current actual velocity
     * @return The value of internalVelocity, now updated
     */
    public double calculate(double input, double velocity) {
        flywheelVelocity = velocity;
        double currentTime = Timer.getFPGATimestamp();
        double elapsed = currentTime - prevTime;
        internalValue += MathUtil.clamp(input - internalValue, -getRateLimit() * elapsed, getRateLimit() * elapsed);
        return internalValue;
    }
    
    /**
     * Resets the internal velocity to a set value.
     * @param value The new target value
     */
    public void reset(double value) {
        internalValue = value;
        prevTime = Timer.getFPGATimestamp();
    }
}
