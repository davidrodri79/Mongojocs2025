package com.mygdx.mongojocs.thaiwarrior.common;
/*
 * Created on 17-nov-2004
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrador
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Audio {

    public static final  boolean DEBUG = true;
    //--------------------------------------------------------------------------
    // Audio
    //--------------------------------------------------------------------------
    public static final  int MSX_TITLE_THEME = 0;

    public static final  int MSX_HIGHSCORE_THEME = 1;

    public static final  int MSX_GAME_OVER = 2;

    public static final  int MSX_LEVEL_START = 3;

    public static final  int MSX_LEVEL_END = 4;
    
    public static final  int MSX_LIFE_LOST = 5;
    
    public static final  int MSX_INGAME1 = 6;
    
    public static final  int MSX_INGAME2 = 7;
    
    public static final  int MSX_INGAME3 = 8;
    
    public static final  int MSX_GAME_COMPLETED = 9;
    
    public static final  int MSX_INTRO = 10;
    
//    public static final  int SFX_PUNCH = 0;
//
//    public static final  int SFX_SWING = 1;
//
//    public static final  int SFX_FALL = 2;
//
//    public static final  int SFX_JUMP = 3;
//    
//    public static final  int SFX_SPEECH_LEVEL_1 = 4;
//    public static final  int SFX_SPEECH_LEVEL_2 = 5;
//    public static final  int SFX_SPEECH_LEVEL_3 = 6;
//    public static final  int SFX_SPEECH_LEVEL_4 = 7;
//    public static final  int SFX_SPEECH_LEVEL_5 = 8;
//    public static final  int SFX_SPEECH_LEVEL_6 = 9;
//    public static final  int SFX_SPEECH_KILLED= 10;
//    public static final  int SFX_SPEECH_LEVEL_START= 11;
//    public static final  int SFX_SPEECH_LEVEL_COMPLETED= 12;
//    public static final  int SFX_SPEECH_LEVEL_GAME_OVER= 13;

    /**
     * 
     * @return
     */
    public abstract boolean isAudioSupported();
    
    /**
     * 
     * @return
     */
    public abstract boolean isAudioEnabled();
    
    /**
     * 
     * @param e
     */
    public abstract void setAudioEnabled(boolean e);
    
    /**
     * 
     * @throws Exception
     */
    public abstract void loadAudio() throws Exception;    

    /**
     * 
     * @param index
     */
//    public abstract void playSfx(int index);    

    /**
     * 
     * @param index
     */
    public abstract void playMsx(int index);

    /**
     * 
     *
     */
    public abstract void stopAudio();
    
    /**
     * 
     *
     */
    public abstract void resumeAudio();
    
    /**
     * 
     *
     */
    public abstract void pauseAudio() ;

    /**
     * 
     * @return
     */
    public abstract boolean isVibrationSupported();
    
    /**
     * 
     * @return
     */
    public abstract boolean isVibrationEnabled();
    
    /**
     * 
     * @param e
     */
    public abstract void setVibrationEnabled(boolean e);
    
    /**
     * 
     * @param freq
     * @param millis
     */
    public abstract void startVibration(int freq, int millis);
    
    /**
     * 
     *
     */
    public abstract void stopVibration();

}
