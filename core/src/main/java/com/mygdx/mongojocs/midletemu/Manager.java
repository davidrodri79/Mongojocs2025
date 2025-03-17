package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;

import java.io.InputStream;

public class Manager {
    public static Player createPlayer(String Nombre, String s) {
        Player p = new Player();
        p.music = Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre));
        return p;
    }
}
