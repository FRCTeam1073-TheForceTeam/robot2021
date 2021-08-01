// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final double CONTROLLER_DEADZONE = 0.06;
    public static final double THROTTLE_FALLOFF = 0.7;

    public enum PowerPortConfiguration {
        LOW,HIGH
    };

    public static final PowerPortConfiguration portConfig = PowerPortConfiguration.HIGH;

    /**Power cell diameter in meters.**/
    public static final double POWER_CELL_DIAMETER = 0.18;

    /**The highest flywheel velocity in radians/sec (equivalent to 5968 RPM) that can be
    considered safe (based on our kF values and some reasonable estimates of what the word
    'safety' means).**/
    public static final double MAXIMUM_SAFE_FLYHWEEL_SPEED = 625;

    /**How close the actual flywheel velocity should be to the target velocity set in an
     * autonomous command before it can be considered to have completed.
     * 
     * Units are in radians/sec difference.
     * */
    // public static final double ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE = 2.0;
    public static final double ACCEPTABLE_FLYWHEEL_VELOCITY_DIFFERENCE = 3.0;


    /**
     * How close the center of the power port needs to be to the center in proportion to
     * the image width (where -1 is all the way to the left and 1 is all the way to the right)
     * for autonomous port tracking commands to be considered to be aligned.
     */
    // public static final double ACCEPTABLE_PORT_TRACKER_ALIGNMENT = 0.0375;
    public static final double ACCEPTABLE_PORT_TRACKER_ALIGNMENT = 0.05;

    public static final double ACCEPTABLE_HOOD_ANGLE_DIFFERENCE = 0.02;

    public static final double MAX_FLYWHEEL_SPEED = 0.8;

    /**
     * The farthest distance from the power port (in meters) before the results are unreliable.
     */
    public static final double MAXIMUM_DETECTABLE_RANGE = 9.5;
    public static final double DRIVETRAIN_WIDTH = 0.647;
}