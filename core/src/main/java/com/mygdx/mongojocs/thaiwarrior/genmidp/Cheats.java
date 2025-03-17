package com.mygdx.mongojocs.thaiwarrior.genmidp;/*
 * Created on 14-dic-2004
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */


import com.mygdx.mongojocs.midletemu.Choice;
import com.mygdx.mongojocs.midletemu.ChoiceGroup;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;

/**
 * @author Administrador
 * 
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class Cheats {

    static Form form;

    static ChoiceGroup cg;

    static String[] cheatOps = { "Infinite lives", "Infinite Energy",
            "Infinite specials", "Clear section", "Next level","Ignore enemy attacks.","Gore","Turbo" };

    public static Displayable lastDisplayable;

    static Display display;
    
    static Cheats instance;
    
    public static boolean[] isCheatEnabled = new boolean[cheatOps.length];
    
    public static final int CHEAT_INFINITE_LIVES = 0;
    public static final int CHEAT_INFINITE_ENERGY = 1;
    public static final int CHEAT_INFINITE_SPECIALS = 2;
    public static final int CHEAT_CLEAR_SECTION = 3;
    public static final int CHEAT_NEXT_LEVEL = 4;
    public static final int CHEAT_IGNORE_ENEMIES_ATTACK = 5;
    public static final int CHEAT_GORE = 6;
    public static final int CHEAT_TURBO = 7;

    public Cheats(Display dis) {
        form = new Form("Thai Warrior Trainer +"+cheatOps.length);
        cg = new ChoiceGroup("Cheats:", Choice.MULTIPLE, cheatOps, null);
        display = dis;
        form.append(cg);
        form.addCommand(new Command("OK", Command.OK, 1));
        form.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                if (c.getCommandType() == Command.OK) {
                    hide();
                }
            }
        });
        instance = null;
        instance = this;
    }
    
    public static boolean isCheatEnabled(int cheatIndex) {
        return cg.isSelected(cheatIndex);
    }
    
    public static void setCheatEnabled(int cheatIndex, boolean enabled) {
        cg.setSelectedIndex(cheatIndex,enabled);
    }

    public static void show() {
        //lastDisplayable = display.getCurrent();
        display.setCurrent(form);
    }
    
    public static void hide() {
        display.setCurrent(lastDisplayable);        
    }
}