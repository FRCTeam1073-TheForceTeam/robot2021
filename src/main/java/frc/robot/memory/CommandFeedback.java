package frc.robot.memory;

import java.util.ArrayList;

public class CommandFeedback {
    private ArrayList<String> titels;
    private ArrayList<Double> values;
    private boolean isInputValid;

    public CommandFeedback(ArrayList<String> titels, ArrayList<Double> values) {
        isInputValid = true;
        if (titels.size() != values.size()) {
            isInputValid = false;
        }
        for (String title : titels) {
            for (String t : titels) {
                if (title.equals(t)) {
                    isInputValid = false;
                    break;
                }
            }
            if (!isInputValid) {
                break;
            }
        }
        if (isInputValid) {
            this.titels = titels;
            this.values = values;
        } else {
            this.titels = new ArrayList<String>();
            this.values = new ArrayList<Double>();
        }
    }

    public CommandFeedback() {
        this.titels = new ArrayList<String>();
        this.values = new ArrayList<Double>();
    }

    public boolean addFeedback(String titel, double value) {
        isInputValid = true;
        for (String title : titels) {
            for (String t : titels) {
                if (title.equals(t)) {
                    isInputValid = false;
                    break;
                }
            }
            if (!isInputValid) {
                break;
            }
        }
        if (isInputValid) {
            titels.add(titel);
            values.add(value);
        }
        return isInputValid;
    }

    public void removeFeedback(int index) {
        titels.remove(index);
        values.remove(index);
    }

    public ArrayList<String> getTitels() {
        return titels;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public String getTitel(int index) {
        return titels.get(index);
    }

    public double getValue(int index) {
        return values.get(index);
    }

    public int getIndex(String titel) {
        return titels.indexOf(titel);
    }

    public String toString() {
        String returnString = "";
        for (int i = 0; i < titels.size(); i++) {
            returnString += "At index " + i + " is " + titels.get(i) + " with the value " + values.get(i) + "\n";
        }
        return returnString;
    }
}
