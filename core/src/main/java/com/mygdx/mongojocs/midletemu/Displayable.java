package com.mygdx.mongojocs.midletemu;

public class Displayable {

    public boolean isShown()
    {
        return Display.currentDisplayable == this;
    }

    public void addCommand(Command doneCommand) {
    }

    public void removeCommand(Command doneCommand) {
    }

    public void setCommandListener(CommandListener m) {
    }
}
