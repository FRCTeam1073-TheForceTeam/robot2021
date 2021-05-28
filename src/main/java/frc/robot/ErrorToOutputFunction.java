package frc.robot;

import java.util.ArrayList;

public class ErrorToOutputFunction {

    private final double minimum;
    private final double maximum;
    private ArrayList<double[]> functions = new ArrayList<double[]>();
    private double[] function = new double[8];
    private double compareMin;
    private double compareMax;

    /**
     * Creates a new ErrorToOutputFunction object whichs domain has to be filled up
     * with functions using the public add commands
     * 
     * @param minimum the absolute minimum the error inputted could be
     * @param maximum the absolute maximum the error inputted could be
     */
    public ErrorToOutputFunction(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    private boolean checkDomain(double minimum, double maximum) {
        if (this.minimum > minimum || this.maximum < maximum) {
            return false;
        }
        for (byte i = 0; i < functions.size(); i++) {
            compareMin = functions.get(i)[1];
            compareMax = functions.get(i)[2];
            if ((compareMin >= minimum && compareMin < maximum) || (compareMax > minimum && compareMax <= maximum)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDomain(double minimum, double maximum, double value) {
        return minimum <= value && maximum >= value;
    }

    private boolean checkRange(double[] placeholder, int start) {
        for (byte i = (byte) start; i < placeholder.length; i++) {
            if (placeholder[i] < -1 || placeholder[i] > 1) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRange(double test) {
        if (test < -1 || test > 1) {
            return false;
        }
        return true;
    }

    private void sortFunctions() {
        for (byte i = 0; i < functions.size() - 1; i++) {
            function = functions.get(i);
            for (byte j = (byte) (i + 1); j < functions.size(); j++) {
                if (function[1] > functions.get(j)[1]) {
                    functions.set(i, functions.get(j));
                    functions.set(j, function);
                }
            }
        }
    }

    private boolean domainIsFilled() {
        if (this.minimum != functions.get(0)[1]) {
            return false;
        }
        if (this.maximum != functions.get(functions.size() - 1)[2]) {
            return false;
        }
        for (byte i = 0; i < functions.size() - 1; i++) {
            if (functions.get(i)[2] != functions.get(i + 1)[1]) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNoRangeSpikes() {
        for (byte i = 0; i < functions.size() - 1; i++) {
            if (Math.abs(functions.get(i)[7] - functions.get(i + 1)[6]) > 0.05) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a cube root function to the created ErrorToOutputFunction Object
     * 
     * @param minimum     the beginning of the domain of this function within the
     *                    domain of the piecewise function from the Object
     * @param maximum     the end of the domain of this function within the domain
     *                    of the piecewise function from the Object
     * @param vertex      the x coordinate of the vertex of the cube root function
     * @param OutputAtMin the output you will get at the previously set minimum
     * @param OutputAtMax the output you will get at the previously set maximum
     */
    public boolean addCubeRoot(double minimum, double maximum, double vertex, double OutputAtMin, double OutputAtMax) {
        double a = (OutputAtMax - OutputAtMin) / (Math.cbrt(maximum - vertex) - Math.cbrt(minimum - vertex));
        double OutputAtVertex = -a * Math.cbrt(minimum - vertex) + OutputAtMin;
        double[] function = { 1.0 / 3.0, minimum, maximum, a, vertex, OutputAtVertex, OutputAtMin, OutputAtMax };
        this.function = function;
        if (checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    /**
     * Adds a square root function opening to the right to the created
     * ErrorToOutputFunction Object
     * 
     * @param minimum     the beginning of the domain of this function within the
     *                    domain of the piecewise function from the Object
     * @param maximum     the end of the domain of this function within the domain
     *                    of the piecewise function from the Object
     * @param vertex      the x coordinate of the vertex of the square root function
     *                    (has to be at the minimum or further left than it)
     * @param OutputAtMin the output you will get at the previously set minimum
     * @param OutputAtMax the output you will get at the previously set maximum
     */
    public boolean addSquareRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtMax) {
        double a = (OutputAtMax - OutputAtMin) / (Math.sqrt(maximum - vertex) - Math.sqrt(minimum - vertex));
        double OutputAtVertex = -a * Math.sqrt(minimum - vertex) + OutputAtMin;
        double[] function = { 0.5, minimum, maximum, a, vertex, OutputAtVertex, OutputAtMin, OutputAtMax };
        this.function = function;
        if (vertex <= minimum && checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    /**
     * Adds a square root function opening to the left to the created
     * ErrorToOutputFunction Object
     * 
     * @param minimum     the beginning of the domain of this function within the
     *                    domain of the piecewise function from the Object
     * @param maximum     the end of the domain of this function within the domain
     *                    of the piecewise function from the Object
     * @param vertex      the x coordinate of the vertex of the square root function
     *                    (has to be at the mmaximum or further right than it)
     * @param OutputAtMin the output you will get at the previously set minimum
     * @param OutputAtMax the output you will get at the previously set maximum
     */
    public boolean addFlippedSquareRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtMax) {
        double a = (OutputAtMax - OutputAtMin) / (Math.sqrt(-(maximum - vertex)) - Math.sqrt(-(minimum - vertex)));
        double OutputAtVertex = -a * Math.sqrt(-(minimum - vertex)) + OutputAtMin;
        double[] function = { 0.5, minimum, maximum, a, vertex, OutputAtVertex, OutputAtMin, OutputAtMax };
        this.function = function;
        if (vertex >= maximum && checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    /**
     * Adds a horizontal to the created ErrorToOutputFunction Object
     * 
     * @param minimum the beginning of the domain of this function within the domain
     *                of the piecewise function from the Object
     * @param maximum the end of the domain of this function within the domain of
     *                the piecewise function from the Object
     * @param heigth  the heigth at which the horizontal should be
     */
    public boolean addHorizontal(double minimum, double maximum, double heigth) {
        double[] function = { 0.0, minimum, maximum, 0.0, 0.0, heigth, heigth, heigth };
        this.function = function;
        if (checkDomain(minimum, maximum) && checkRange(heigth)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    /**
     * Adds a linear function to the created ErrorToOutputFunction Object
     * 
     * @param minimum     the beginning of the domain of this function within the
     *                    domain of the piecewise function from the Object
     * @param maximum     the end of the domain of this function within the domain
     *                    of the piecewise function from the Object
     * @param OutputAtMin the output you will get at the previously set minimum
     * @param OutputAtMax the output you will get at the previously set maximum
     */
    public boolean addLinear(double minimum, double maximum, double OutputAtMin, double OutputAtMax) {
        double slope = (OutputAtMax - OutputAtMin) / (maximum - minimum);
        double yIntercept = -slope * minimum + OutputAtMin;
        double[] function = { 1.0, minimum, maximum, slope, 0.0, yIntercept, OutputAtMin, OutputAtMax };
        this.function = function;
        if (checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    /**
     * Adds a square function opening to the right to the created
     * ErrorToOutputFunction Object
     * 
     * @param minimum     the beginning of the domain of this function within the
     *                    domain of the piecewise function from the Object
     * @param maximum     the end of the domain of this function within the domain
     *                    of the piecewise function from the Object
     * @param vertex      the x coordinate of the vertex of the square root function
     *                    (has to be at the minimum or further left than it)
     * @param OutputAtMin the output you will get at the previously set minimum
     * @param OutputAtMax the output you will get at the previously set maximum
     */
    public boolean addSquare(double minimum, double maximum, double vertex, double OutputAtMin, double OutputAtMax) {
        double a = (OutputAtMax - OutputAtMin)
                / ((maximum - vertex) * (maximum - vertex) - (minimum - vertex) * (minimum - vertex));
        double OutputAtVertex = -a * (minimum - vertex) * (minimum - vertex) + OutputAtMin;
        double[] function = { 2.0, minimum, maximum, a, vertex, OutputAtVertex, OutputAtMin, OutputAtMax };
        this.function = function;
        if (checkDomain(minimum, maximum, vertex) && checkDomain(minimum, maximum) && checkRange(OutputAtVertex)
                && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        } else if (checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    /** Returns the number of functions added to this object */
    public int getNumFunctions() {
        return functions.size();
    }

    /**
     * Returns true if the object's domain is filled up in a safe way and will print
     * out why it returned false if it did
     */
    public boolean canOutput() {
        if (!domainIsFilled()) {
            System.out.println("The domain for this ErrorToOutputFunction is not fully described");
            return false;
        } else if (!hasNoRangeSpikes()) {
            System.out.println("The Range for this ErrorToOutputFunction has too high spikes");
            return false;
        }
        return true;
    }

    /**
     * Returns the raw output value calculated using the functions in their domain
     * that were added to this object (might be unsatisfying weirdly rounded values)
     * 
     * @param input the error inputed to calculate the output
     */
    public double getRawOutput(double input) {
        for (byte i = 0; i < functions.size(); i++) {
            if (checkDomain(functions.get(i)[1], functions.get(i)[2], input)) {
                function = functions.get(i);
                return function[3] * Math.pow(Math.signum(function[0]) * (input - function[4]), Math.abs(function[0]))
                        + function[5];
            }
        }
        return 0.0;
    }

    /**
     * Returns a rounded output value calculated using the functions in their domain
     * that were added to this object
     * 
     * @param input          the error inputed to calculate the output
     * @param toDecimalPoint the number of decimal points the output will to be
     *                       rounded to
     */
    public double getRoundedOutput(double input, int toDecimalPoint) {
        double output = 0.0;
        for (byte i = 0; i < functions.size(); i++) {
            if (checkDomain(functions.get(i)[1], functions.get(i)[2], input)) {
                function = functions.get(i);
                output = function[3] * Math.pow(Math.signum(function[0]) * (input - function[4]), Math.abs(function[0]))
                        + function[5];
                output = Math.round(output * Math.pow(10.0, toDecimalPoint)) / Math.pow(10.0, toDecimalPoint);
                return output;
            }
        }
        return output;
    }

    /**
     * Returns the output value calculated using the functions in their domain that
     * were added to this object rounded to the ten thousandths
     * 
     * @param input the error inputed to calculate the output
     */
    public double getRoundedOutput(double input) {
        double output = 0.0;
        for (byte i = 0; i < functions.size(); i++) {
            if (checkDomain(functions.get(i)[1], functions.get(i)[2], input)) {
                function = functions.get(i);
                output = function[3] * Math.pow(Math.signum(function[0]) * (input - function[4]), Math.abs(function[0]))
                        + function[5];
                output = Math.round(output * 10000) / 10000.0;
                return output;
            }
        }
        return output;
    }

    public String toString() {
        String returnString = "This ErrorToOutputFunction is a piecewise function out of " + this.getNumFunctions()
                + " functions\n";
        for (byte i = 0; i < functions.size(); i++) {
            returnString += "function #" + (i + 1) + " is a " + type(functions.get(i));
        }
        return returnString;
    }

    private String type(double[] function) {
        int type = (int) Math.round(function[0] * 10);
        String domain = "from " + function[1] + " to " + function[2];
        String equa = " with the equation: " + function[3];
        String tion = "(x - " + function[4] + ")^" + function[0] + " + " + function[5] + "\n";
        String tion2 = "(-(x - " + function[4] + "))^" + function[0] + " + " + function[5] + "\n";

        switch (type) {
            case 3:
                return "Cubic Root Function " + domain + equa + tion;

            case 5:
                return "Square Root Function " + domain + equa + tion;

            case -5:
                return "Flipped Square Root Function " + domain + equa + tion2;

            case 0:
                return "Horizontal " + domain + " at " + function[7] + "\n";

            case 10:
                return "Linear Function " + domain + equa + "x + " + function[5] + "\n";

            case 20:
                return "Square Function " + domain + equa + tion;

            default:
                return "No type";
        }
    }
}
