package frc.robot.memory;

import java.util.ArrayList;

import edu.wpi.first.wpiutil.math.MathUtil;

public class Memory {
    private static ArrayList<String> names;
    private static ArrayList<Boolean> haveFinishedNormally;
    private static ArrayList<CommandFeedback> specifics;

    public Memory() {
        names = new ArrayList<String>();
        haveFinishedNormally = new ArrayList<Boolean>();
        specifics = new ArrayList<CommandFeedback>();
    }

    public void addToMemory(String name, boolean hasFinishedNormally) {
        for (int i = 0; i < names.size(); i++) {
            if (name.equals(names.get(i))) {
                name += "I";
                i = 0;
            }
        }
        names.add(name);
        haveFinishedNormally.add(hasFinishedNormally);
        specifics.add(new CommandFeedback());
    }

    public void addToMemory(String name, boolean hasFinishedNormally, CommandFeedback specific) {
        for (int i = 0; i < names.size(); i++) {
            if (name.equals(names.get(i))) {
                name += "I";
                i = 0;
            }
        }
        names.add(name);
        haveFinishedNormally.add(hasFinishedNormally);
        specifics.add(specific);
    }

    public void clearMemory() {
        names.clear();
        haveFinishedNormally.clear();
        specifics.clear();
    }

    public int getIndex(String name) {
        return names.indexOf(name);
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public ArrayList<Boolean> getHaveFinishedNormally() {
        return haveFinishedNormally;
    }

    public ArrayList<CommandFeedback> getAllFeedbacks() {
        return specifics;
    }

    public String getName(int index) {
        return names.get(index);
    }

    public String getlastName() {
        return names.get(names.size() - 1);
    }

    public boolean getHasFinished(int index) {
        return haveFinishedNormally.get(index);
    }

    public boolean getHasFinished(String name) {
        return haveFinishedNormally.get(names.indexOf(name));
    }

    public boolean getLastHasFinished() {
        return haveFinishedNormally.get(names.size() - 1);
    }

    public boolean havePreviousFinished(int number) {
        number = MathUtil.clamp(number, 1, names.size());
        for (int i = 1; i <= number; i++) {
            if (!haveFinishedNormally.get(names.size() - i)) {
                return false;
            }
        }
        return true;
    }

    public CommandFeedback getFeedback(int index) {
        return specifics.get(index);
    }

    public CommandFeedback getFeedback(String name) {
        return specifics.get(names.indexOf(name));
    }

    public CommandFeedback getLastFeedback() {
        return specifics.get(names.size() - 1);
    }

}
