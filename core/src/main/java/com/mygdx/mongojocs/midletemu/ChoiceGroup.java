package com.mygdx.mongojocs.midletemu;

public class ChoiceGroup extends Item {
    public ChoiceGroup(String label,
                       int choiceType,
                       String[] stringElements,
                       Image[] imageElements) {
        super();
    }

    public void setSelectedIndex(int cheatIndex, boolean enabled) {
    }

    public boolean isSelected(int cheatIndex) {
        return false;
    }
}
