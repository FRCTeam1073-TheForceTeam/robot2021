package frc.robot;

import java.util.ArrayList;

public class ErrorToOutputFunction {

    private final double minimum;
    private final double maximum;
    private ArrayList<double[]> functions = new ArrayList<double[]>();
    private double[] function = new double[8];
    private double compareMin;
    private double compareMax;

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

    public boolean addCubeRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtVertex) {
        double a = (OutputAtMin - OutputAtVertex) / Math.sqrt(minimum - vertex);
        double OutputAtMax = a * Math.cbrt(maximum - vertex) + OutputAtVertex;
        double[] function = { 1.0 / 3.0, minimum, maximum, a, vertex, OutputAtVertex, OutputAtMin, OutputAtMax };
        this.function = function;
        if (checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addSquareRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtVertex) {
        double a = (OutputAtMin - OutputAtVertex) / Math.sqrt(minimum - vertex);
        double OutputAtMax = a * Math.sqrt(maximum - vertex) + OutputAtVertex;
        double[] function = { 0.5, minimum, maximum, a, vertex, OutputAtVertex, OutputAtMin, OutputAtMax };
        this.function = function;
        if (vertex <= minimum && checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addFlippedSquareRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtVertex) {
        double a = (OutputAtMin - OutputAtVertex) / Math.sqrt(-(minimum - vertex));
        double OutputAtMax = a * Math.sqrt(-(maximum - vertex)) + OutputAtVertex;
        double[] function = { -0.5, minimum, maximum, a, vertex, OutputAtVertex, OutputAtMin, OutputAtMax };
        this.function = function;
        if (vertex >= maximum && checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

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

    public boolean addLinear(double minimum, double maximum, double slope, double yIntercept) {
        double OutputAtMin = slope * minimum + yIntercept;
        double OutputAtMax = slope * maximum + yIntercept;
        double[] function = { 1.0, minimum, maximum, slope, 0.0, yIntercept, OutputAtMin, OutputAtMax };
        this.function = function;
        if (checkDomain(minimum, maximum) && checkRange(function, 6)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addSquare(double minimum, double maximum, double vertex, double OutputAtMin, double OutputAtVertex) {
        double a = (OutputAtMin - OutputAtVertex) / ((minimum - vertex) * (minimum - vertex));
        double OutputAtMax = a * (maximum - vertex) * (maximum - vertex) + OutputAtVertex;
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

    public int getNumFunctions() {
        return functions.size();
    }

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

    public double getRawOutput(double input) {
        for (byte i = 0; i < functions.size(); i++) {
            if (checkDomain(functions.get(i)[1], functions.get(i)[2], input)) {
                function = functions.get(i);
                return function[3] * Math.pow(Math.signum(function[0]) * (input - function[4]), Math.abs(function[0]))
                        + function[5];
            }
        }
        System.out.println("!!!Error inputted was out of the domain!!!");
        return 0.0;
    }

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
        System.out.println("!!!Error inputted was out of the domain!!!");
        return output;
    }

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
        System.out.println("!!!Error inputted was out of the domain!!!");
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
