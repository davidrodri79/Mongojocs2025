package com.mygdx.mongojocs.iapplicationemu;

import com.badlogic.gdx.Gdx;

public class PhoneSystem {
    public static final int DEV_VIBRATOR =  1;
    public static final int ATTR_VIBRATOR_ON = 2;
    public static final int ATTR_VIBRATOR_OFF = 4;

    public static void setAttribute(int attr, int value) {

        if(attr == DEV_VIBRATOR)
        {
            if(value == ATTR_VIBRATOR_ON)
            {
                Gdx.input.vibrate(100);
            }
        }
    }
}
