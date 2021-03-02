package frc.robot.memory;

import java.util.ArrayList;

public class CommandFeedback {
    private ArrayList<String> titles;
    private ArrayList<Double> values;
    private boolean isInputValid;

    public CommandFeedback(ArrayList<String> titles, ArrayList<Double> values) {
        isInputValid = true;
        if (titles.size() != values.size()) {
            isInputValid = false;
        }
        for (String title : titles) {
            for (String t : titles) {
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
            this.titles = titles;
            this.values = values;
        } else {
            this.titles = new ArrayList<String>();
            this.values = new ArrayList<Double>();
        }
    }

    public CommandFeedback() {
        this.titles = new ArrayList<String>();
        this.values = new ArrayList<Double>();
    }

    public boolean addFeedback(String title, double value) {
        isInputValid = true;
        for (String t : titles) {
            if (title.equals(t)) {
                isInputValid = false;
                break;
            }
        }

        if (isInputValid) {
            titles.add(title);
            values.add(value);
        }
        return isInputValid;
    }

    public void removeFeedback(int index) {
        titles.remove(index);
        values.remove(index);
    }

    public ArrayList<String> getTitels() {
        return titles;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public String getTitel(int index) {
        return titles.get(index);
    }

    public double getValue(int index) {
        return values.get(index);
    }

    public int getIndex(String title) {
        return titles.indexOf(title);
    }

    public String toString() {
        String returnString = "";
        for (int i = 0; i < titles.size(); i++) {
            returnString += "At index " + i + " is " + titles.get(i) + " with the value " + values.get(i) + "\n";
        }
        return returnString;
    }
}
