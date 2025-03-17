package com.mygdx.mongojocs.midletemu;

public class Command {
    public static final int OK = 1;
    public static final int SCREEN = 1;
    public static final int BACK = 2;
    public static final int CANCEL = 3;

    String label;
    int priority;
    int commandType;

    public Command(String lab, int ctype, int pri)
    {
        label = lab; commandType = ctype; priority= pri;
    }

    public String getLabel() {
        return label;
    }

    public int getPriority() { return priority; }

    public int getCommandType() { return commandType; }
}
