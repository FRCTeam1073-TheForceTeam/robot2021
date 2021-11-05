/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.components;

/**
 * A component for generating output values based on a set of input-output pairs,
 * linearly interpolating between values and optionally either extrapolating past
 * the domain of input points or clamping between the minimum or maximum.
 */

public class InterpolatorTable {
    private InterpolatorTableEntry[] entries;
    boolean extrapolate = false;

    /**
     * Constructs an InterpolatorTable object.
     * Optionally allows for extrapolation past the lower and upper bounds
     * (it clamps the input between the minimum and maximum otherwise)
     * @param extrapolate Whether or not to extrapolate
     * @param entries_ The array of entries
     */
    public InterpolatorTable(boolean extrapolate_, InterpolatorTableEntry... entries_) {
        extrapolate = extrapolate_;
        entries = entries_;
    }

    public InterpolatorTable(InterpolatorTableEntry... entries_) {
        this(false, entries_);
    }

    public double getValue(double input) {
        int minIdx = -1;
        int maxIdx = -1;
        double minDistance = 1000000000;
        double maxDistance = -1000000000;
        for (int i = 0; i < entries.length; i++) {
            double value = entries[i].x;
            double distance = input - value;
            if (distance < 0 && Math.abs(distance) < Math.abs(minDistance)) {
                minIdx = i;
                minDistance = distance;
            } else if (distance >= 0 && Math.abs(distance) < Math.abs(maxDistance)) {
                maxIdx = i;
                maxDistance = distance;
            }
        }
        if (minIdx == -1) {
            return entries[maxIdx].y;
        } else if (maxIdx == -1) {
            return entries[minIdx].y;
        }
        double interpolationValue = (input - entries[minIdx].x) / (entries[maxIdx].x - entries[minIdx].x);
        return entries[minIdx].y + (entries[maxIdx].y - entries[minIdx].y) * interpolationValue;
    }

    public static class InterpolatorTableEntry {
        double x = 0;
        double y = 0;

        public InterpolatorTableEntry(double x_, double y_) {
            x = x_;
            y = y_;
        }
    }
}