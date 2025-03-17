package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.audio.Music;

public class Player {

    Music music;

    public void realize() {
    }

    public void deallocate() {
    }

    public void prefetch() {
    }

    public VolumeControl getControl(String volumeControl) {
        return new VolumeControl();
    }

    public void start() {
        music.play();
    }

    public void setLoopCount(int loop) {

        music.setLooping(loop == 0);
    }

    public void setMediaTime(int time) {
    }

    public void stop() {
        music.stop();
    }
}
