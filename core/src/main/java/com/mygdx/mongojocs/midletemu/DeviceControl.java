package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;

public class DeviceControl {


    public static void startVibra(int i, int time)
    {
        Gdx.input.vibrate(time);
    }

    public static void setLights(int i, int i1) {
    }

    public static void stopVibra() {
    }
}
