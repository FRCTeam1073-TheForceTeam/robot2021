import java.util.ArrayList;

public class ErrorToOutputFunction {

    private final double domain;
    private final double minimum;
    private final double maximum;
    private ArrayList<ArrayList<Double>> breakpoints = new ArrayList<ArrayList<Double>>();
    private ArrayList<Double> breakpoint = new ArrayList<Double>();
    private double compareMin;
    private double compareMax;

    public ErrorToOutputFunction(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.domain = maximum - minimum;
    }

    private boolean checkDomain(double minimum, double maximum) {
        if (this.minimum <= minimum && this.maximum >= maximum) {
            for (byte i = 0; i < breakpoints.size(); i++) {
                compareMin = breakpoints.get(i).get(1);
                compareMax = breakpoints.get(i).get(2);
                if ((compareMin >= minimum && compareMin <= maximum)
                        || (compareMax >= minimum && compareMax <= maximum)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean checkDomain(double test) {
        return this.minimum <= test && this.maximum >= test;
    }

    private boolean checkRange(ArrayList<Double> placeholder, int start) {
        for (byte i = (byte) start; i < placeholder.size(); i++) {
            if (placeholder.get(i) < -1 || placeholder.get(i) > 1) {
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

    public boolean addSquareRoot(double minimum, double maximum, double vertex, double OutputAtMin,
            double OutputAtMax) {
        return false;
    }

    public boolean addHorizontal(double minimum, double maximum, double yIntercept) {
        breakpoint.clear();
        breakpoint.add(0.0);
        breakpoint.add(minimum);
        breakpoint.add(maximum);
        breakpoint.add(yIntercept);
        if (checkDomain(minimum, maximum) && checkRange(breakpoint, 3)) {
            breakpoints.add(breakpoint);
            return true;
        }
        return false;
    }

    public boolean addLinear(double minimum, double maximum, double slope, double yIntercept) {
        breakpoint.clear();
        breakpoint.add(1.0);
        breakpoint.add(minimum);
        breakpoint.add(maximum);
        breakpoint.add(slope);
        breakpoint.add(yIntercept);
        if (checkDomain(minimum, maximum) && checkRange(slope * minimum + yIntercept)
                && checkRange(slope * maximum + yIntercept)) {
            breakpoints.add(breakpoint);
            return true;
        }
        return false;
    }

    public boolean addSquare(double minimum, double maximum, double vertex, double OutputAtMin, double OutputAtVertex) {
        breakpoint.clear();
        breakpoint.add(0.0);
        breakpoint.add(minimum);
        breakpoint.add(maximum);
        double a = (OutputAtMin - OutputAtVertex) / ((minimum - vertex) * (minimum - vertex));
        breakpoint.add(a);
        breakpoint.add(vertex);
        breakpoint.add(OutputAtVertex);
        if (checkDomain(minimum, maximum) && checkDomain(vertex) && checkRange(OutputAtMin)
                && checkRange(OutputAtVertex)
                && checkRange(a * (maximum - vertex) * (maximum - vertex) + OutputAtVertex)) {
            breakpoints.add(breakpoint);
            return true;
        }
        return false;
    }

}
