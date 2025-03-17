package com.mygdx.mongojocs.thaiwarrior.genmidp;


import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;
import com.mygdx.mongojocs.thaiwarrior.common.Audio;
/* Created on 17-nov-2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrador
 * 
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class AudioMma extends Audio {
    String[] audioFile = { "/title_theme.mid", "/high_score.mid",
            "/game_over.mid", "/level_start.mid", "/level_end.mid",
            "/life_lost.mid", "/ingame1.mid", "/ingame3.mid", "/ingame2.mid",
            "/game_completed.mid","/intro.mid"};

    boolean[] loopEnabled = { true, true, false, false, false, false, true,
            true, true, true, false, true, false };

    int lastPlayedMsx = -1;

    int lastPlayedSfx = -1;
    
    String mediaType = "audio/midi";

    //MM Api
    Player[] audioPlayer;

    VolumeControl[] volumeControl;

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
        audioPlayer = new Player[audioFile.length];
        if (DEBUG) {
            System.out.println("Loading audio files. Audio file count: "
                    + audioFile.length);
        }
        for (int c = 0; c < audioFile.length; c++) {
            if (DEBUG) {
                System.out.println("Loading audio file: " + c + " -> "
                        + audioFile[c]);
            }
            audioPlayer[c] = Manager.createPlayer(audioFile[c], mediaType);
            audioPlayer[c].realize();
            if (loopEnabled[c]) {
                audioPlayer[c].setLoopCount(-1);
            }

        }
        if (DEBUG) {
            System.out.println("\n\nAUDIO INIT COMPLETED.\n\n");
        }
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
        if (audioEnabled) {
            try {
                stopAudio();
//                if (audioPlayer[index] == null) {
//                    audioPlayer[index] = Manager.createPlayer(this.getClass()
//                            .getResourceAsStream(audioFile[index]), mediaType);
//                    audioPlayer[index].realize();
//                    if (loopEnabled[index]) {
//                        audioPlayer[index].setLoopCount(-1);
//                    }
//                }
                audioPlayer[index].start();
                lastPlayedMsx = index;
                Thread.sleep(10);
            } catch (Exception e) {
                if (DEBUG) {
                    System.out.println("Error playing audio: " + index);
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#stopAudio()
     */
    public void stopAudio() {
        if (lastPlayedMsx >= 0) {
            try {
                audioPlayer[lastPlayedMsx].stop();
                audioPlayer[lastPlayedMsx].setMediaTime(0);
                //audioPlayer[lastPlayedMsx].deallocate();
                //audioPlayer[lastPlayedMsx]=null;
                Thread.sleep(10);
                lastPlayedMsx = -1;
            } catch (Exception e) {
                if (DEBUG) {
                    System.out
                            .println("Error stopping audio: " + lastPlayedMsx);
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#pauseAudio()
     */
    public void pauseAudio() {
        if (lastPlayedMsx >= 0) {
            try {
                audioPlayer[lastPlayedMsx].stop();
                Thread.sleep(10);
            } catch (Exception e) {
                if (DEBUG) {
                    System.out.println("Error pausing audio: " + lastPlayedMsx);
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#resumeAudio()
     */
    public void resumeAudio() {
        if (lastPlayedMsx >= 0 && audioEnabled) {
            try {
                audioPlayer[lastPlayedMsx].start();
                Thread.sleep(10);
            } catch (Exception e) {
                if (DEBUG) {
                    System.out
                            .println("Error resuming audio: " + lastPlayedMsx);
                    e.printStackTrace();
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    // Vibration
    //--------------------------------------------------------------------------

    static boolean vibrationEnabled = false;

    static boolean vibrationSupported = false;

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
        if (vibrationEnabled && vibrationSupported) {
            DeviceControl.startVibra(freq, millis);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Audio#stopVibration()
     */
    public void stopVibration() {
        DeviceControl.stopVibra();
    }
}