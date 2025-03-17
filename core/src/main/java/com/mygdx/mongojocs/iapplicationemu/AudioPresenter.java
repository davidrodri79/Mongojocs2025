package com.mygdx.mongojocs.iapplicationemu;

import com.mygdx.mongojocs.parachutist.GameCanvas;

public class AudioPresenter extends MediaPresenter {

    public static final int AUDIO_COMPLETE = 1;

    static MediaSound currentSound = null;

    public static AudioPresenter getAudioPresenter() {
        return new AudioPresenter();
    }

    public void setMediaListener(MediaListener ml) {
    }

    public void setSound(MediaSound mediaSound) {
        currentSound = mediaSound;
    }

    public void play() {
        currentSound.music.play();
    }

    public void stop() {
        currentSound.music.stop();
    }
}
