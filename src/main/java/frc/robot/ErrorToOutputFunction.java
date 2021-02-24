package frc.robot;

import java.util.ArrayList;

public class ErrorToOutputFunction {

    private final double minimum;
    private final double maximum;
    private ArrayList<ArrayList<Double>> functions = new ArrayList<ArrayList<Double>>();
    private ArrayList<Double> function = new ArrayList<Double>();
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
            compareMin = functions.get(i).get(1);
            compareMax = functions.get(i).get(2);
            if ((compareMin >= minimum && compareMin <= maximum) || (compareMax >= minimum && compareMax <= maximum)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDomain(double minimum, double maximum, double value) {
        return minimum <= value && maximum >= value;
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
                if (function.get(1) > functions.get(j).get(1)) {
                    functions.set(i, functions.get(j));
                    functions.set(j, function);
                }
            }
        }
    }

    public boolean addCubeRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtVertex) {
        function.clear();
        function.add(1.0 / 3.0);
        function.add(minimum);
        function.add(maximum);
        double a = (OutputAtMin - OutputAtVertex) / Math.sqrt(minimum - vertex);
        function.add(a);
        function.add(vertex);
        function.add(OutputAtVertex);
        double OutputAtMax = a * Math.cbrt(maximum - vertex) + OutputAtVertex;
        function.add(OutputAtMin);
        function.add(OutputAtMax);
        if (checkDomain(minimum, maximum) && checkRange(OutputAtMin) && checkRange(OutputAtMax)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addSquareRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtVertex) {
        function.clear();
        function.add(0.5);
        function.add(minimum);
        function.add(maximum);
        double a = (OutputAtMin - OutputAtVertex) / Math.sqrt(minimum - vertex);
        function.add(a);
        function.add(vertex);
        function.add(OutputAtVertex);
        double OutputAtMax = a * Math.sqrt(maximum - vertex) + OutputAtVertex;
        function.add(OutputAtMin);
        function.add(OutputAtMax);
        if (vertex <= minimum && checkDomain(minimum, maximum) && checkRange(OutputAtMin) && checkRange(OutputAtMax)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addFlippedSquareRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtVertex) {
        function.clear();
        function.add(-0.5);
        function.add(minimum);
        function.add(maximum);
        double a = (OutputAtMin - OutputAtVertex) / Math.sqrt(-(minimum - vertex));
        function.add(a);
        function.add(vertex);
        function.add(OutputAtVertex);
        double OutputAtMax = a * Math.sqrt(-(maximum - vertex)) + OutputAtVertex;
        function.add(OutputAtMin);
        function.add(OutputAtMax);
        if (vertex >= maximum && checkDomain(minimum, maximum) && checkRange(OutputAtMin) && checkRange(OutputAtMax)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addHorizontal(double minimum, double maximum, double heigth) {
        function.clear();
        function.add(0.0);
        function.add(minimum);
        function.add(maximum);
        function.add(heigth);
        function.add(heigth);
        function.add(heigth);
        if (checkDomain(minimum, maximum) && checkRange(heigth)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addLinear(double minimum, double maximum, double slope, double yIntercept) {
        function.clear();
        function.add(1.0);
        function.add(minimum);
        function.add(maximum);
        function.add(slope);
        function.add(yIntercept);
        double OutputAtMin = slope * minimum + yIntercept;
        double OutputAtMax = slope * maximum + yIntercept;
        function.add(OutputAtMin);
        function.add(OutputAtMax);
        if (checkDomain(minimum, maximum) && checkRange(OutputAtMin) && checkRange(OutputAtMax)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public boolean addSquare(double minimum, double maximum, double vertex, double OutputAtMin, double OutputAtVertex) {
        function.clear();
        function.add(2.0);
        function.add(minimum);
        function.add(maximum);
        double a = (OutputAtMin - OutputAtVertex) / ((minimum - vertex) * (minimum - vertex));
        function.add(a);
        function.add(vertex);
        function.add(OutputAtVertex);
        double OutputAtMax = a * (maximum - vertex) * (maximum - vertex) + OutputAtVertex;
        function.add(OutputAtMin);
        function.add(OutputAtMax);
        if (checkDomain(minimum, maximum, vertex) && checkDomain(minimum, maximum) && checkRange(OutputAtMin)
                && checkRange(OutputAtVertex) && checkRange(OutputAtMax)) {
            functions.add(function);
            sortFunctions();
            return true;
        } else if (checkDomain(minimum, maximum) && checkRange(OutputAtMin) && checkRange(OutputAtMax)) {
            functions.add(function);
            sortFunctions();
            return true;
        }
        return false;
    }

    public int getNumFunctions() {
        return functions.size();
    }

    private boolean domainIsFilled() {
        if (this.minimum != functions.get(0).get(1)) {
            return false;
        }
        if (this.maximum != functions.get(functions.size() - 1).get(2)) {
            return false;
        }
        for (byte i = 0; i < functions.size() - 1; i++) {
            if (functions.get(i).get(2) != functions.get(i + 1).get(1)) {
                return false;
            }
        }
        return true;
    }

}
