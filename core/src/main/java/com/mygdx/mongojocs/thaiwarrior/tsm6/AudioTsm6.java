package com.mygdx.mongojocs.thaiwarrior.tsm6;
/*
 * Created on 17-nov-2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

import com.mygdx.mongojocs.thaiwarrior.common.Audio;

/**
 * @author Administrador
 * 
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class AudioTsm6 extends Audio {
    String[] audioFile = { "/title_theme.mid", null, null,
            "/level_start.mid", "/level_end.mid", "/life_lost.mid",
            null, "/ingame2.mid", null, null };

    boolean[] loopEnabled = { true, true, false, false, false, false, true,
            true, true, true };

    int lastPlayedMsx = -1;

    int lastPlayedSfx = -1;

    //AudioSound[] audioPlayer;

    boolean audioEnabled = true;

    boolean audioSupported = true;

    /*
     * (non-Javadoc)
     * 
     * @see Audio#isAudioEnabled()
     */
    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#setAudioEnabled(boolean)
     */
    public void setAudioEnabled(boolean v) {
        audioEnabled = v;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#isAudioSupported()
     */
    public boolean isAudioSupported() {
        return audioSupported;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#loadAudio()
     */
    public void loadAudio() throws Exception {
        //String mediaType = "audio/midi";
       /* audioPlayer = new AudioSound[audioFile.length];
        if (DEBUG) {
            System.out.println("Loading audio files. Audio file count: "
                    + audioFile.length);
        }
        for (int c = 0; c < audioFile.length; c++) {
            if (DEBUG) {
                System.out.println("Loading audio file: " + c + " -> "
                        + audioFile[c]);
            }
            if (audioFile[c] != null) {
                audioPlayer[c] = new AudioSound(3, audioFile[c]);
            }
        }
        audioFile[MSX_HIGHSCORE_THEME] = audioFile[MSX_TITLE_THEME];
        audioFile[MSX_GAME_COMPLETED] = audioFile[MSX_TITLE_THEME];
        audioFile[MSX_INGAME1] = audioFile[MSX_INGAME2];
        audioFile[MSX_INGAME3] = audioFile[MSX_INGAME2];        
        audioFile[MSX_GAME_OVER] = audioFile[MSX_LIFE_LOST];
        if (DEBUG) {
            System.out.println("\n\nAUDIO INIT COMPLETED.\n\n");
        }
        //playMsx(7);*/
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#playSfx(int)
     */
    public void playSfx(int index) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#playMsx(int)
     */
    public void playMsx(int index) {
       /* if (audioEnabled) {
            try {
                stopAudio();
                if (loopEnabled[index]) {
                    audioPlayer[index].play(100, 4);
                } else {
                    audioPlayer[index].play(1, 4);
                }
                lastPlayedMsx = index;
                Thread.sleep(50);
            } catch (Exception e) {
                if (DEBUG) {
                    System.out.println("Error playing audio: " + index);
                    e.printStackTrace();
                }
            }
        }*/
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#stopAudio()
     */
    public void stopAudio() {
       /* if (lastPlayedMsx >= 0) {
            try {
                audioPlayer[lastPlayedMsx].stop();
                Thread.sleep(50);
                lastPlayedMsx = -1;
            } catch (Exception e) {
                if (DEBUG) {
                    System.out
                            .println("Error stopping audio: " + lastPlayedMsx);
                    e.printStackTrace();
                }
            }
        }*/
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#pauseAudio()
     */
    public void pauseAudio() {
       /* if (lastPlayedMsx >= 0) {
            try {
                audioPlayer[lastPlayedMsx].pause();
                Thread.sleep(50);
            } catch (Exception e) {
                if (DEBUG) {
                    System.out.println("Error pausing audio: " + lastPlayedMsx);
                    e.printStackTrace();
                }
            }
        }*/
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#resumeAudio()
     */
    public void resumeAudio() {
        /*if (lastPlayedMsx >= 0 && audioEnabled) {
            try {
                audioPlayer[lastPlayedMsx].resume();
                Thread.sleep(50);
            } catch (Exception e) {
                if (DEBUG) {
                    System.out
                            .println("Error resuming audio: " + lastPlayedMsx);
                    e.printStackTrace();
                }
            }
        }*/
    }

    //--------------------------------------------------------------------------
    // Vibration
    //--------------------------------------------------------------------------

    static boolean vibrationEnabled = true;

    static boolean vibrationSupported = true;

    /*
     * (non-Javadoc)
     * 
     * @see Audio#isVibrationEnabled()
     */
    public boolean isVibrationEnabled() {
        return vibrationEnabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#isVibrationSupported()
     */
    public boolean isVibrationSupported() {
        return vibrationSupported;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#setAudioEnabled(boolean)
     */
    public void setVibrationEnabled(boolean v) {
        vibrationEnabled = v;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#startVibration(int, int)
     */
    public void startVibration(int freq, int millis) {
        /*if (vibrationEnabled && vibrationSupported) {
            PhoneDevice.vibrate(millis);
        }*/
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#stopVibration()
     */
    public void stopVibration() {
        //PhoneDevice.vibrate(0);
    }
}