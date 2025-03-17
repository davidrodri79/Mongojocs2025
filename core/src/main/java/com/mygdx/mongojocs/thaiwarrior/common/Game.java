package com.mygdx.mongojocs.thaiwarrior.common;// Notas sobre cosas que hacer.
// ---- Juego (porting - c�digo) ----
//TODO: Audio por tonos para nokia
// TODO: Sistemas de audio propietarios que no sean MMA ni para el TSM6
// TODO: Versionado de la implementaci�n gr�fica del juego para los m�viles m�s
// limitados.
// TODO: Implementaci�n gr�fica para iMode, y adaptaci�n del MFS.
// TODO: Implementaci�n 'm�nima' (series 40, etc).
// 
// ---- Juego (gr�ficos) ----
// TODO: Versiones reducidas de gr�ficos de fondo del t�tulo, intro, etc...
// 
// ---- Juego (sonido) ----
// TODO: Versiones para tones, spf, etc...



/*
 * 
 *  
 */

import com.mygdx.mongojocs.thaiwarrior.genmidp.Cheats;

import java.util.Random;

/**
 * @author Administrador
 */
public abstract class Game {

    public static final boolean DEBUG = false;

    public static boolean CHEATS = false;

    // Game key codes
    public static final int KEY_NONE = 0;

    public static final int KEY_UP = 1;

    public static final int KEY_DOWN = 2;

    public static final int KEY_LEFT = 3;

    public static final int KEY_RIGHT = 4;

    public static final int KEY_ACTION_A = 5;

    public static final int KEY_ACTION_B = 6;

    public static final int KEY_ACTION_C = 7;

    public static final int KEY_ACTION_D = 8;

    public static final int KEY_SOFT_1 = 9;

    public static final int KEY_SOFT_2 = 10;

    public static final int KEY_FIRE = 11;

    public static final int KEY_UPLEFT = 12;

    public static final int KEY_DOWNLEFT = 13;

    public static final int KEY_UPRIGHT = 14;

    public static final int KEY_DOWNRIGHT = 15;

    public int keyCode;

    public boolean keyPressed;

    // Main states

    public static final int STATE_CRASH = -3;

    public static final int STATE_INIT = -2;

    public static final int STATE_LOADING = -1;

    public static final int STATE_INTRO = 0;

    public static final int STATE_TITLE = 1;

    public static final int STATE_MENU = 2;

    public static final int STATE_INSTRUCTIONS = 3;

    public static final int STATE_ABOUT = 4;

    public static final int STATE_PLAY = 5;

    public static final int STATE_HIGH_SCORE = 6;

    public static final int STATE_QUIT = 7;

    protected int state;

    protected int nextState;

    //
    public static long currentTick;

    public static boolean finished;

    public Audio audio;

    //    public Gfx gfx;

    public static Random random;

    /**
     * Game class constructor
     */
    public Game(Audio au) {
        state = STATE_INIT;
        nextState = STATE_INIT;
        finished = false;
        keyCode = -9999;
        this.audio = au;
        //this.gfx = gfx;
        random = new Random(System.currentTimeMillis());
    }

    /**
     *  
     */
    public abstract void saveData();

    /**
     * 
     *  
     */
    public abstract void loadData();

    /**
     * 
     *  
     */
    public void pauseGame() {
        audio.pauseAudio();
        if (state == STATE_PLAY) {
            nextState = STATE_MENU;
        }
    }

    /**
     * 
     *  
     */
    public void resumeGame() {
        audio.resumeAudio();
    }

    //--------------------------------------------------------------------------
    // Game Logic
    //--------------------------------------------------------------------------

    /**
     * Game logic update
     */
    public void updateLogic() {
        if (nextState != state) {
            keyPressed = false;
            keyCode = KEY_NONE;
            state = nextState;
            //audio.stopAudio();
            try {
                switch (state) {
                    case STATE_CRASH:
                        break;
                    case STATE_INIT:
                        break;
                    case STATE_LOADING:
                        break;
                    case STATE_INTRO:
                        introStateTransition();
                        break;
                    case STATE_TITLE:
                        titleStateTransition();
                        break;
                    case STATE_MENU:
                        menuStateTransition();
                        break;
                    case STATE_INSTRUCTIONS:
                        instructionsTransition();
                        break;
                    case STATE_ABOUT:
                        aboutTransition();
                        break;
                    case STATE_PLAY:
                        playStateTransition();
                        break;
                    case STATE_HIGH_SCORE:
                        highScoreTransition();
                        break;
                    case STATE_QUIT:
                        break;
                }
                System.gc();
                Thread.yield();
                if (DEBUG) {
                    System.out.println("Free mem: "
                            + Runtime.getRuntime().freeMemory());
                    System.out.println("Used mem: "
                            + (Runtime.getRuntime().totalMemory() - Runtime
                                    .getRuntime().freeMemory()));
                }
            } catch (Exception e) {
                if (DEBUG) {
                    System.out.println("CRASH AT STATE " + state
                            + " TRANSITION.");
                    e.printStackTrace();
                }
                finished = true;
            }
        }
        try {
            switch (state) {
                case STATE_INIT:
                    nextState = STATE_LOADING;
                    break;
                case STATE_LOADING:
                    loadingStateLogic();
                    break;
                case STATE_INTRO:
                    introStateLogic();
                    break;
                case STATE_TITLE:
                    titleStateLogic();
                    break;
                case STATE_MENU:
                    menuStateLogic();
                    break;
                case STATE_INSTRUCTIONS:
                    instructionsStateLogic();
                    break;
                case STATE_ABOUT:
                    aboutStateLogic();
                    break;
                case STATE_PLAY:
                    playStateLogic();
                    break;
                case STATE_HIGH_SCORE:
                    highScoreLogic();
                    break;
                case STATE_QUIT:
                    quitStateLogic();
                    break;
                case STATE_CRASH:
                    break;
            }
            currentTick++;
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("CRASH AT STATE " + state + " LOGIC.");
                e.printStackTrace();
            }
            finished = true;
        }
    }

    //--------------------------------------------------------------------------
    // Display
    //--------------------------------------------------------------------------

    boolean playIntermMapDrawn = false;

    /**
     * Game display update
     */
    public void updateDisplay() {
        try {
            switch (state) {
                case STATE_INIT:
                case STATE_LOADING:
                    drawLogoMicro();
                    break;
                case STATE_INTRO:
                    drawIntro();
                    break;
                case STATE_TITLE:
                    drawTitle();
                    break;
                case STATE_MENU:
                    drawTitle();
                    drawMenuArrows();
                    drawMenuOption(menuOption);
                    break;
                case STATE_INSTRUCTIONS:
                    drawInstructionsPage();
                    drawScrollUpDown();
                    break;
                case STATE_ABOUT:
                    drawAboutPage();
                    drawScrollUpDown();
                    break;
                case STATE_PLAY:
                    switch (playState) {
                        case PLAY_STATE_START:
                            //if (!playIntermMapDrawn) {
                            drawMap();
                            drawSprites();
                            drawParticles();
                            drawBonus();
                            drawScoreboard();
                            //  playIntermMapDrawn = true;
                            //}
                            drawGameStart();
                            break;
                        case PLAY_STATE_INGAME:
                            if (!playerSpecial) {
                                drawMap();
                            } else {
                                drawSpecialBackground();
                            }
                            drawSprites();
                            drawParticles();
                            drawBonus();
                            drawScoreboard();
                            break;
                        case PLAY_STATE_DEAD:
                            drawMap();
                            drawSprites();
                            drawParticles();
                            drawBonus();
                            drawScoreboard();
                            drawPlayerDead();
                            break;
                        case PLAY_STATE_GAME_OVER:
                            //if (!playIntermMapDrawn) {
                            drawMap();
                            drawSprites();
                            drawParticles();
                            drawBonus();
                            drawScoreboard();
                            //                                playIntermMapDrawn = true;
                            //                            }
                            drawGameOver();
                            break;
                        case PLAY_STATE_LEVEL_END:
                            //if (!playIntermMapDrawn) {
                            drawMap();
                            drawSprites();
                            drawParticles();
                            drawBonus();
                            drawScoreboard();
                            //  playIntermMapDrawn = true;
                            //}
                            drawLevelCompleted();
                            break;
                        case PLAY_STATE_GAME_COMPLETED:
                            //if (!playIntermMapDrawn) {
                            //                            drawMap();
                            //                            drawSprites();
                            //                            drawParticles();
                            //                            drawBonus();
                            //                            drawScoreboard();
                            //                                playIntermMapDrawn = true;
                            //                            }
                            //                            drawLevelCompleted();
                            drawGameCompleted();
                            break;
                    }
                    break;
                case STATE_HIGH_SCORE:
                    drawHighScore();
                    break;
                case STATE_QUIT:
                    break;
            }
            if (state != STATE_INIT && state != STATE_LOADING) {
                drawSoftLabels();
            }
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("CRASH AT STATE " + state + " DISPLAY.");
                e.printStackTrace();
            }
            finished = true;
        }
    }

    /*
     * Loading State Logic
     */
    private void loadingStateLogic() {
        try {
            audio.loadAudio();
            initGfx();
            loadData();
            setAudioOptionOn(audio.isAudioEnabled());
            setVibraOptionOn(audio.isVibrationEnabled());
            nextState = STATE_INTRO;
            currentTick = 0;
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("Error at loading state logic!");
                e.printStackTrace();
            }
            finished = true;
            //nextState = STATE_CRASH;
        }
    }

    //--------------------
    // -Intro State -
    //--------------------

    /**
     * Intro State Logic
     */
    protected abstract void introStateLogic();

    /**
     * Intro State transition
     */
    protected abstract void introStateTransition();

    //--------------------
    // -Title State -
    //--------------------

    /*
     * Title State Logic
     */
    private void titleStateLogic() {
        if (keyPressed) {
            switch (keyCode) {
                case KEY_UP:
                    break;
                case KEY_DOWN:
                    break;
                case KEY_LEFT:
                    break;
                case KEY_RIGHT:
                    break;
                case KEY_ACTION_A:
                    break;
                case KEY_ACTION_B:
                    break;
                case KEY_ACTION_C:
                    break;
                case KEY_ACTION_D:
                    break;
                case KEY_SOFT_1:
                case KEY_FIRE:
                    nextState = STATE_MENU;
                    break;
                case KEY_SOFT_2:
                    break;
            }
        }
        if (currentTick > 730) {
            nextState = STATE_INTRO;
        }
    }

    /*
     * Title State Transition
     */
    private void titleStateTransition() {
        setSoftLabel(0, SOFT_LABEL_MENU);
        setSoftLabel(1, SOFT_LABEL_NONE);
        //audio.stopAudio();
        audio.playMsx(Audio.MSX_TITLE_THEME);
        inGameMenu = false;
        currentTick = 0;
        initTitle();
    }

    //---------------------
    // Menu State
    //---------------------

    public static final int MENU_OPTION_GAME = 0;

    public static final int MENU_OPTION_AUDIO = 1;

    public static final int MENU_OPTION_VIBRA = 2;

    public static final int MENU_OPTION_INSTRUCTIONS = 3;

    public static final int MENU_OPTION_ABOUT = 4;

    public static final int MENU_OPTION_QUIT = 5;

    public int menuOption;

    public boolean inGameMenu;

    long cheatActivationCounter;

    /*
     * Menu State Logic
     */
    private void menuStateLogic() {
        if (keyPressed) {
            if (keyCode == KEY_UP) {
                cheatActivationCounter++;
                keyPressed = false;
                if (cheatActivationCounter > 10) {
                    Game.CHEATS = true;
                }
            } else {
                cheatActivationCounter = 0;
                switch (keyCode) {
                    case KEY_LEFT:
                        menuOption--;
                        if (menuOption == MENU_OPTION_VIBRA
                                && !audio.isVibrationSupported()) {
                            menuOption--;
                        }
                        if (menuOption == MENU_OPTION_AUDIO
                                && !audio.isAudioSupported()) {
                            menuOption--;
                        }
                        if (menuOption < MENU_OPTION_GAME) {
                            menuOption = MENU_OPTION_QUIT;
                        }
                        if (DEBUG) {
                            System.out.println("MENU OPT: " + menuOption);
                        }
                        showMenuArrows = true;
                        menuArrowPressed = -1;
                        break;
                    case KEY_RIGHT:
                        menuOption++;
                        if (menuOption == MENU_OPTION_AUDIO
                                && !audio.isAudioSupported()) {
                            menuOption++;
                        }
                        if (menuOption == MENU_OPTION_VIBRA
                                && !audio.isVibrationSupported()) {
                            menuOption++;
                        }
                        if (menuOption > MENU_OPTION_QUIT) {
                            menuOption = MENU_OPTION_GAME;
                        }
                        if (DEBUG) {
                            System.out.println("MENU OPT: " + menuOption);
                        }
                        showMenuArrows = true;
                        menuArrowPressed = 1;
                        break;
                    case KEY_ACTION_A:
                    case KEY_ACTION_B:
                    case KEY_ACTION_C:
                    case KEY_ACTION_D:
                    case KEY_SOFT_2:
                    case KEY_FIRE:
                        switch (menuOption) {
                            case MENU_OPTION_GAME:
                                nextState = STATE_PLAY;
                                break;
                            case MENU_OPTION_AUDIO:
                                audio.setAudioEnabled(!audio.isAudioEnabled());
                                setAudioOptionOn(audio.isAudioEnabled());
                                if (audio.isAudioEnabled()) {
                                    audio.resumeAudio();
                                } else {
                                    audio.pauseAudio();
                                }
                                break;
                            case MENU_OPTION_VIBRA:
                                audio.setVibrationEnabled(audio
                                        .isVibrationEnabled());
                                setVibraOptionOn(audio.isVibrationEnabled());
                                break;
                            case MENU_OPTION_INSTRUCTIONS:
                                nextState = STATE_INSTRUCTIONS;
                                break;
                            case MENU_OPTION_ABOUT:
                                nextState = STATE_ABOUT;
                                break;
                            case MENU_OPTION_QUIT:
                                if (inGameMenu) {
                                    nextState = STATE_INTRO;
                                } else {
                                    nextState = STATE_QUIT;
                                }
                                break;
                        }
                        saveData();
                        break;
                    case KEY_SOFT_1:
                        if (inGameMenu) {
                            nextState = STATE_TITLE;
                        } else {
                            nextState = STATE_QUIT;
                        }
                        break;
                }
            }
            //keyPressed = false;
            keyCode = KEY_NONE;
        } else {
            menuArrowPressed = 0;
        }
    }

    /*
     * Menu State Transition
     */
    private void menuStateTransition() {
        setSoftLabel(0, SOFT_LABEL_QUIT);
        setSoftLabel(1, SOFT_LABEL_SELECT);
        setMainMenuMode(inGameMenu);
        menuOption = MENU_OPTION_GAME;
        finishTitle();
    }

    //--------------------
    // Instructions State
    //--------------------

    /*
     *  
     */
    private void instructionsTransition() {
        setSoftLabel(0, SOFT_LABEL_MENU);
        setSoftLabel(1, SOFT_LABEL_NONE);
        insLine = 0;
        showScrollUp = insLine > 0;
        showScrollDown = insLine < (instructionsText.length - insVisLines);
    }

    /*
     *  
     */
    private void instructionsStateLogic() {
        if (keyPressed) {
            switch (keyCode) {
                case KEY_UP:
                    if (insLine > 0) {
                        insLine--;
                    }
                    scrollArrowPressed = -1;
                    break;
                case KEY_DOWN:
                    if (insLine < instructionsText.length - insVisLines) {
                        insLine++;
                    }
                    scrollArrowPressed = 1;
                    break;
                case KEY_ACTION_A:
                case KEY_ACTION_B:
                case KEY_ACTION_C:
                case KEY_ACTION_D:
                case KEY_SOFT_1:
                case KEY_SOFT_2:
                case KEY_FIRE:
                    nextState = STATE_MENU;
                    break;
            }
            showScrollUp = insLine > 0;
            showScrollDown = insLine < (instructionsText.length - insVisLines);
        } else {
            scrollArrowPressed = 0;
        }
    }

    //--------------------
    // About State
    //--------------------

    /*
     *  
     */
    private void aboutTransition() {
        setSoftLabel(0, SOFT_LABEL_MENU);
        setSoftLabel(1, SOFT_LABEL_NONE);
        aboutLine = 0;
        showScrollUp = aboutLine > 0;
        showScrollDown = aboutLine < (aboutText.length - aboutVisLines);
    }

    /*
     *  
     */
    private void aboutStateLogic() {
        if (keyPressed) {
            switch (keyCode) {
                case KEY_UP:
                    if (aboutLine > 0) {
                        aboutLine--;
                    }
                    break;
                case KEY_DOWN:
                    if (aboutLine < aboutText.length - aboutVisLines) {
                        aboutLine++;
                    }
                    break;
                case KEY_ACTION_A:
                case KEY_ACTION_B:
                case KEY_ACTION_C:
                case KEY_ACTION_D:
                case KEY_SOFT_1:
                case KEY_SOFT_2:
                case KEY_FIRE:
                    nextState = STATE_MENU;
                    break;
            }
            showScrollUp = aboutLine > 0;
            showScrollDown = aboutLine < (aboutText.length - aboutVisLines);
        }
    }

    //--------------------
    // - Play State -
    //--------------------

    public int currentCombo;

    public static boolean playerAttack;

    public static boolean playerJump;

    public static boolean playerSpecial;

    public static final int PLAY_STATE_NONE = -1;

    public static final int PLAY_STATE_START = 0;

    public static final int PLAY_STATE_INGAME = 1;

    public static final int PLAY_STATE_DEAD = 2;

    public static final int PLAY_STATE_GAME_OVER = 3;

    public static final int PLAY_STATE_LEVEL_END = 4;

    public static final int PLAY_STATE_GAME_COMPLETED = 5;

    public int playState;

    public int nextPlayState;

    public long timerStart;

    public boolean advanceLevel;

    public int gameCompleteStep;

    public int gameCompleteTextY;

    //public int nextExtraLifeAt;

    //public int enemiesLeft;

    /*
     * Play State Logic
     */
    private void playStateLogic() throws Exception {
        if (keyPressed && keyCode == KEY_SOFT_1
                && playState != PLAY_STATE_GAME_OVER) {
            audio.pauseAudio();
            nextState = STATE_MENU;
            return;
        }
        if (nextPlayState != playState) {
            switch (nextPlayState) {
                case PLAY_STATE_START:
                    showGoIcon = false;
                    audio.playMsx(Audio.MSX_LEVEL_START);
                    playerSpecials = 3;
                    if (advanceLevel) {
                        if (playerTries < 3) {
                            playerTries++;
                        }
                        mapIndex++;
                        mapSectionIndex = 0;
                        for (int c = 0; c < mapSectionTriggered[mapIndex].length; c++) {
                            mapSectionTriggered[mapIndex][c] = false;
                        }
                        resetPlayer();
                        for (int c = 1; c < NUM_SPRITES; c++) {
                            spriteVisible[c] = false;
                        }
                        for (int c = 0; c < childStartX[mapIndex].length; c++) {
                            if (DEBUG) {
                                System.out.println("Setting child: " + c + "/"
                                        + (SPRITE_INDEX_CHILD_START + c) + ": "
                                        + childStartX[mapIndex][c] + ", "
                                        + childStartY[mapIndex][c]);
                            }
                            spriteX[SPRITE_INDEX_CHILD_START + c] = childStartX[mapIndex][c] - 24;
                            spriteY[SPRITE_INDEX_CHILD_START + c] = childStartY[mapIndex][c] - 32;
                            spriteVisible[SPRITE_INDEX_CHILD_START + c] = true;
                            spriteFacing[SPRITE_INDEX_CHILD_START + c] = SPRITE_FACING_LEFT;
                            spriteType[SPRITE_INDEX_CHILD_START + c] = ANIM_INDEX_NIÑO;
                            animBank[SPRITE_INDEX_CHILD_START + c] = ANIM_INDEX_NIÑO + 1;
                            playAnim(SPRITE_INDEX_CHILD_START + c,
                                    ANIM_INDEX_NIÑO_STAND);
                        }
                        advanceLevel = false;
                        bossMode = false;
                        lastEnemyHit = -1;
                        //                        if (mapIndex == MAP_COUNT - 1) {
                        //                            spriteVisible[SPRITE_INDEX_ENEMY_START] = true;
                        //                            spriteFacing[SPRITE_INDEX_ENEMY_START] =
                        // SPRITE_FACING_RIGHT;
                        //                            spriteType[SPRITE_INDEX_ENEMY_START] =
                        // ANIM_INDEX_ENEMY_ABUELO;
                        //                            animBank[SPRITE_INDEX_ENEMY_START] =
                        // spriteType[SPRITE_INDEX_ENEMY_START] + 1;
                        //                            enemyKey[SPRITE_INDEX_ENEMY_START] = KEY_RIGHT;
                        //                            enemyJump[SPRITE_INDEX_ENEMY_START] = false;
                        //                            enemyReactionTime[SPRITE_INDEX_ENEMY_START] = 1;
                        //                            spriteX[SPRITE_INDEX_ENEMY_START] =
                        // spriteX[SPRITE_INDEX_PLAYER]
                        //                                    + tileWidth * 4;
                        //                            spriteY[SPRITE_INDEX_ENEMY_START] =
                        // spriteY[SPRITE_INDEX_PLAYER];
                        //                            playAnim(
                        //                                    SPRITE_INDEX_ENEMY_START,
                        //                                    ANIM_INDEX_ENEMY_WALK[spriteType[SPRITE_INDEX_ENEMY_START]]);
                        //                        }
                    } else {
                        resetPlayer();
                        for (int c = SPRITE_INDEX_ENEMY_START; c <= SPRITE_INDEX_ENEMY_END; c++) {
                            if (spriteVisible[c]) {
                                spriteVisible[c] = false;
                                spawnsLeft++;
                            }
                        }
                        if (bossMode && mapIndex > 0) {
                            spriteX[SPRITE_INDEX_BOSS] = bossStartX[mapIndex];
                            spriteY[SPRITE_INDEX_BOSS] = bossStartY[mapIndex];
                            spriteFacing[SPRITE_INDEX_BOSS] = SPRITE_FACING_LEFT;
                            playAnim(
                                    SPRITE_INDEX_BOSS,
                                    ANIM_INDEX_ENEMY_WALK[spriteType[SPRITE_INDEX_BOSS]]);
                        }
                    }
                    updateMap();
                    break;
                case PLAY_STATE_INGAME:
                    showGoIcon = true;
                    audio.playMsx(Audio.MSX_INGAME1 + mapIndex % 3);
                    break;
                case PLAY_STATE_DEAD:
                    showGoIcon = false;
                    playerTries--;
                    if (CHEATS
                            && Cheats
                                    .isCheatEnabled(Cheats.CHEAT_INFINITE_LIVES)) {
                        playerTries = 3;
                    }
                    //audio.playSfx(Audio.SFX_SPEECH_KILLED);
                    //if (playerTries > 0) {
                    audio.playMsx(Audio.MSX_LIFE_LOST);
                    //}
                    break;
                case PLAY_STATE_GAME_OVER:
                    showGoIcon = false;
                    audio.playMsx(Audio.MSX_GAME_OVER);
                    //audio.playSfx(Audio.SFX_SPEECH_LEVEL_GAME_OVER);
                    break;
                case PLAY_STATE_LEVEL_END:
                    showGoIcon = false;
                    audio.playMsx(Audio.MSX_LEVEL_END);
                    lastEnemyHit = -1;
                    break;
                case PLAY_STATE_GAME_COMPLETED:
                    showGoIcon = false;
                    audio.playMsx(Audio.MSX_GAME_COMPLETED);
                    lastEnemyHit = -1;
                    gameCompleteStep = 0;
                    gameCompleteTextY = displayHeight;
                    setSoftLabel(0, SOFT_LABEL_NONE);
                    setSoftLabel(1, SOFT_LABEL_NONE);
                    break;
            }
            timerStart = System.currentTimeMillis();
            keyPressed = false;
            playState = nextPlayState;
            playIntermMapDrawn = false;
            System.gc();
            Thread.yield();
        }
        switch (playState) {
            case PLAY_STATE_START:
                if ((keyPressed || System.currentTimeMillis() - timerStart > 4000)
                        && System.currentTimeMillis() - timerStart > 1000) {
                    nextPlayState = PLAY_STATE_INGAME;
                    showGoIcon = !bossMode;
                }
                break;
            case PLAY_STATE_INGAME:
                if (CHEATS && keyPressed
                        && (keyCode == KEY_SOFT_2 || keyCode == KEY_ACTION_D)) {
                    Cheats.show();
                }
                try {
                    if (bossMode && mapIndex != 0) {
                        updateBossSprite();
                        //System.out.println("BOSS!");
                    } else {
                        enemySpawnCounter--;
                        if (enemySpawnCounter <= 0) {
                            enemySpawnCounter = ENEMY_SPAWN_DELAY;
                            try {
                                spawnEnemy();
                            } catch (Exception e) {
                                if (DEBUG) {
                                    throw new Exception(
                                            "Error spawning enemy: " + e);
                                }
                            }
                        }
                        updateEnemySprites();
                    }
                } catch (Exception e) {
                    if (DEBUG) {
                        throw new Exception("Error updating enemy sprites:\n"
                                + e.toString());
                    }
                }
                try {
                    updateChildSprites();
                } catch (Exception e) {
                    if (DEBUG) {
                        throw new Exception("Error updating child:\n"
                                + e.toString());
                    }
                }
                try {
                    //if (playerJump) {
                    updatePlayerSprite();
                    //}
                    //updatePlayerSprite();
                } catch (Exception e) {
                    if (DEBUG) {
                        throw new Exception("Error updating player sprite:\n"
                                + e.toString());
                    }
                }
                try {
                    updateMap();
                } catch (Exception e) {
                    if (DEBUG) {
                        throw new Exception("Error updating map:\n"
                                + e.toString());
                    }
                }
                break;
            case PLAY_STATE_DEAD:
                if ((keyPressed || System.currentTimeMillis() - timerStart > 6000)
                        && System.currentTimeMillis() - timerStart > 1000) {
                    if (playerTries > 0) {
                        nextPlayState = PLAY_STATE_START;
                    } else {
                        nextPlayState = PLAY_STATE_GAME_OVER;
                    }
                } else {
                    updateEnemySprites();
                    updateSprite(SPRITE_INDEX_PLAYER);
                    updateMap();
                }
                break;
            case PLAY_STATE_GAME_OVER:
                if ((keyPressed || System.currentTimeMillis() - timerStart > 10000)
                        && System.currentTimeMillis() - timerStart > 1000) {
                    if (playerScore >= hiScore) {
                        nextState = STATE_HIGH_SCORE;
                    } else {
                        nextState = STATE_INTRO;
                    }
                    inGameMenu = false;
                }
                break;
            case PLAY_STATE_LEVEL_END:
                if ((keyPressed || System.currentTimeMillis() - timerStart > 4000)
                        && System.currentTimeMillis() - timerStart > 1000) {
                    if (mapIndex + 1 < MAP_COUNT) {
                        nextPlayState = PLAY_STATE_START;
                        advanceLevel = true;
                    } else {
                        audio.playMsx(Audio.MSX_GAME_COMPLETED);
                        nextPlayState = PLAY_STATE_GAME_COMPLETED;
                        advanceLevel = false;
                    }
                    bossMode = false;
                } else {
                    updateSprite(SPRITE_INDEX_PLAYER);
                    updateMap();
                }
                break;
            case PLAY_STATE_GAME_COMPLETED:
                if ((keyPressed || System.currentTimeMillis() - timerStart > 60000)
                        && System.currentTimeMillis() - timerStart > 2000) {
                    if (gameCompleteStep < 1) {
                        gameCompleteStep++;
                        gameCompleteTextY = displayHeight;
                    } else {
                        if (playerScore >= hiScore) {
                            nextState = STATE_HIGH_SCORE;
                        } else {
                            nextState = STATE_INTRO;
                        }
                        inGameMenu = false;
                    }
                    keyPressed = false;
                    timerStart = System.currentTimeMillis();
                } else {
                    //                    updateSprite(SPRITE_INDEX_PLAYER);
                    //                    updateMap();
                }
                break;
        }
    }

    /*
     *  
     */
    private void playStateTransition() {
        setSoftLabel(0, SOFT_LABEL_PAUSE);
        setSoftLabel(1, SOFT_LABEL_NONE);
        //audio.stopAudio();
        if (!inGameMenu) {
            inGameMenu = true;
            spriteFallRate[SPRITE_INDEX_PLAYER] = 8;
            enemySpawnCounter = ENEMY_SPAWN_DELAY;
            nextEnemyIndex = SPRITE_INDEX_ENEMY_START;
            mapIndex = -1;
            //mapIndex = 4;
            playerTries = 3;
            playerSpecials = 3;
            for (int c = 1; c < NUM_SPRITES; c++) {
                spriteVisible[c] = false;
            }
            advanceLevel = true;
            playState = PLAY_STATE_NONE;
            nextPlayState = PLAY_STATE_START;
        } else {
            audio.resumeAudio();
            if (CHEATS) {
                if (Cheats.isCheatEnabled(Cheats.CHEAT_CLEAR_SECTION)) {
                    Cheats.setCheatEnabled(Cheats.CHEAT_CLEAR_SECTION, false);
                    mapSectionTriggered[mapIndex][mapSectionIndex] = true;
                    if (mapSectionIndex < mapSection[mapIndex].length - 1) {
                        mapSectionIndex++;
                    }
                    for (int d = SPRITE_INDEX_ENEMY_START; d <= SPRITE_INDEX_ENEMY_END; d++) {
                        spriteVisible[d] = false;
                        enemyAttack[d] = false;
                        spriteEnergy[d] = 0;
                        enemiesLeft = 0;
                        spawnsLeft = 0;
                    }
                }
                if (Cheats.isCheatEnabled(Cheats.CHEAT_NEXT_LEVEL)) {
                    Cheats.setCheatEnabled(Cheats.CHEAT_NEXT_LEVEL, false);
                    nextPlayState = PLAY_STATE_LEVEL_END;
                }
            }
        }
        //        
    }

    public int playerUpDownPresses;

    public int playerAttackDamage;

    public int comboPower;

    public boolean comboMode;

    boolean playerFirePressed;

    boolean[] spriteFalling = new boolean[NUM_SPRITES];

    boolean[] spriteHit = new boolean[NUM_SPRITES];

    /*
     *  
     */
    private void updatePlayerSprite() {
        boolean playerOnFloor = isOnPlatform(SPRITE_INDEX_PLAYER);
        int animSel = animIndex[SPRITE_INDEX_PLAYER];
        //int deltaY;
        updateSprite(SPRITE_INDEX_PLAYER);
        if (spriteY[SPRITE_INDEX_PLAYER] - spritePreviousY[SPRITE_INDEX_PLAYER] > 0) {
            spriteDeltaY[SPRITE_INDEX_PLAYER] = spriteY[SPRITE_INDEX_PLAYER]
                    - spritePreviousY[SPRITE_INDEX_PLAYER];
        }
        spritePreviousY[SPRITE_INDEX_PLAYER] = spriteY[SPRITE_INDEX_PLAYER];
        if (isAnimFinished(SPRITE_INDEX_PLAYER)) {
            if (animSel == ANIM_INDEX_PLAYER_ATTACKBACK) {
                spriteFacing[SPRITE_INDEX_PLAYER] = spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_LEFT
                        ? SPRITE_FACING_RIGHT : SPRITE_FACING_LEFT;
            }
            if (playerOnFloor) {
                animSel = ANIM_INDEX_PLAYER_STAND;
                spriteY[SPRITE_INDEX_PLAYER] -= spriteY[SPRITE_INDEX_PLAYER]
                        % tileHeight;
            } else if (!spriteFalling[SPRITE_INDEX_PLAYER]) {
                animSel = ANIM_INDEX_PLAYER_FALL;
                spriteFalling[SPRITE_INDEX_PLAYER] = true;
                spriteFallRate[SPRITE_INDEX_PLAYER] = spriteDeltaY[SPRITE_INDEX_PLAYER];
                //System.out.println("DELTAY: " + deltaY);
            }
            playerAttack = false;
            comboMode = false;
        }
        playerJump = animSel == ANIM_INDEX_PLAYER_JUMP
                || animSel == ANIM_INDEX_PLAYER_JUMP_DOWN
                || animSel == ANIM_INDEX_PLAYER_JUMP_UP
                || animSel == ANIM_INDEX_PLAYER_JUMP_UP_SHORT;
        playerSpecial = (playerAttack && animSel == ANIM_INDEX_PLAYER_SPECIAL);
        for (int c = SPRITE_INDEX_ENEMY_START; c <= SPRITE_INDEX_BOSS; c++) {
            if (spriteVisible[c]
                    && enemyAttack[c]
                    && animIndex[SPRITE_INDEX_PLAYER] != ANIM_INDEX_PLAYER_DIE
                    && animIndex[SPRITE_INDEX_PLAYER] != ANIM_INDEX_PLAYER_JUMP
                    && !playerAttack
                    && playerOnFloor
                    && intersects(c, SPRITE_INDEX_PLAYER)
                    && (spriteBaseY[c] == spriteBaseY[SPRITE_INDEX_PLAYER] || (enemyJump[c] && spriteY[SPRITE_INDEX_PLAYER] >= spriteY[c]))) {
                keyCode = KEY_NONE;
                keyPressed = false;
                playerAttack = false;
                spriteFacing[SPRITE_INDEX_PLAYER] = (spriteFacing[c] == SPRITE_FACING_RIGHT
                        ? SPRITE_FACING_LEFT : SPRITE_FACING_RIGHT);
                spriteEnergy[SPRITE_INDEX_PLAYER] -= bossMode ? 10 + Math
                        .abs(random.nextInt() % 10) : 5;
                if (CHEATS
                        && Cheats.isCheatEnabled(Cheats.CHEAT_INFINITE_ENERGY)) {
                    spriteEnergy[SPRITE_INDEX_PLAYER] = 100;
                }
                if (spriteEnergy[SPRITE_INDEX_PLAYER] > 0) {
                    animSel = ANIM_INDEX_PLAYER_HIT;
                    //audio.playSfx(Audio.SFX_PUNCH);
                    //audio.playMsx(Audio.AUDIO_PUNCH);
                    if (comboPower > 0) {
                        comboPower -= 10;
                    }
                    genSpriteHitFx(SPRITE_INDEX_PLAYER, false);
                    vibraHit();
                } else {
                    spriteEnergy[SPRITE_INDEX_PLAYER] = 0;
                    animSel = ANIM_INDEX_PLAYER_DIE;
                    nextPlayState = PLAY_STATE_DEAD;
                    //audio.playSfx(Audio.SFX_FALL);
                    //audio.playMsx(Audio.AUDIO_);
                    comboPower = 0;
                    playerFirePressed = false;
                    keyCode = KEY_NONE;
                    keyPressed = false;
                    genSpriteHitFx(SPRITE_INDEX_PLAYER, true);
                    vibraDead();
                }
                playAnim(SPRITE_INDEX_PLAYER, animSel);
                shakeScreen(10);
            }
        }
        if (playerJump && getFrameOffsetY(SPRITE_INDEX_PLAYER) >= 0
                && isOnPlatform(SPRITE_INDEX_PLAYER)) {
            animSel = ANIM_INDEX_PLAYER_STAND;
            spriteY[SPRITE_INDEX_PLAYER] -= spriteY[SPRITE_INDEX_PLAYER]
                    % tileHeight;
            //while (!isOnPlatform(SPRITE_INDEX_PLAYER)) {
            //                spriteY[SPRITE_INDEX_PLAYER]--;
            //            }
            playerJump = false;
            spriteFalling[SPRITE_INDEX_PLAYER] = false;
            //System.out.println("!");
        }
        if (spriteFalling[SPRITE_INDEX_PLAYER]) {
            playerOnFloor = false;
            int y;
            for (y = 0; y < spriteFallRate[SPRITE_INDEX_PLAYER]
                    && !playerOnFloor; y++) {
                playerOnFloor = isOnPlatform(SPRITE_INDEX_PLAYER, 0, y);
            }
            spriteY[SPRITE_INDEX_PLAYER] += y;
            if (playerOnFloor) {
                spriteFalling[SPRITE_INDEX_PLAYER] = false;
                animSel = ANIM_INDEX_PLAYER_STAND;
                spriteY[SPRITE_INDEX_PLAYER] -= spriteY[SPRITE_INDEX_PLAYER]
                        % tileHeight;
            } else {
                spriteFallRate[SPRITE_INDEX_PLAYER] += 2;
                //                System.out.println("SFR: "
                //                        + spriteFallRate[SPRITE_INDEX_PLAYER]);
            }
        }
        if (keyPressed) {
            if (!playerOnFloor) {
                //                switch (keyCode) {
                //                    case KEY_LEFT:
                //                        spriteFacing[SPRITE_INDEX_PLAYER] = SPRITE_FACING_LEFT;
                //                        spriteX[SPRITE_INDEX_PLAYER] -= 2;
                //                        break;
                //                    case KEY_RIGHT:
                //                        spriteFacing[SPRITE_INDEX_PLAYER] = SPRITE_FACING_RIGHT;
                //                        spriteX[SPRITE_INDEX_PLAYER] += 2;
                //                        break;
                //                }
            } else if ((animSel == ANIM_INDEX_PLAYER_STAND || animSel == ANIM_INDEX_PLAYER_WALK)) {
                switch (keyCode) {
                    case KEY_UP:
                        if (isOnPlatform(SPRITE_INDEX_PLAYER, 0, -16)) {
                            animSel = ANIM_INDEX_PLAYER_JUMP_UP_SHORT;
                        } else {
                            animSel = ANIM_INDEX_PLAYER_JUMP_UP;
                        }
                        playerUpDownPresses--;
                        //audio.playSfx(Audio.SFX_JUMP);
                        //audio.playMsx(Audio.AUDIO_JUMP);
                        break;
                    case KEY_UPLEFT:
                        spriteFacing[SPRITE_INDEX_PLAYER] = SPRITE_FACING_LEFT;
                        animSel = ANIM_INDEX_PLAYER_JUMP;
                        //audio.playSfx(Audio.SFX_JUMP);
                        //audio.playMsx(Audio.AUDIO_JUMP);
                        break;
                    case KEY_UPRIGHT:
                        spriteFacing[SPRITE_INDEX_PLAYER] = SPRITE_FACING_RIGHT;
                        animSel = ANIM_INDEX_PLAYER_JUMP;
                        //audio.playSfx(Audio.SFX_JUMP);
                        //audio.playMsx(Audio.AUDIO_JUMP);
                        break;
                    case KEY_DOWN:
                        if (spriteY[SPRITE_INDEX_PLAYER] < (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                                * tileHeight - 48) {
                            animSel = ANIM_INDEX_PLAYER_FALL;
                            spriteFalling[SPRITE_INDEX_PLAYER] = true;
                            isOnPlatformBypass[SPRITE_INDEX_PLAYER] = 6;
                            spriteFallRate[SPRITE_INDEX_PLAYER] = 2;
                            //spriteY[SPRITE_INDEX_PLAYER] += 24;
                        }
                        playerUpDownPresses++;
                        break;
                    case KEY_ACTION_A:
                    case KEY_DOWNLEFT:
                        spriteFacing[SPRITE_INDEX_PLAYER] = SPRITE_FACING_LEFT;
                        animSel = ANIM_INDEX_PLAYER_SWEEP;
                        //audio.playSfx(Audio.SFX_SWING);
                        //audio.playMsx(Audio.AUDIO_KICK);
                        playerAttack = true;
                        playerAttackDamage = 10;
                        break;
                    case KEY_ACTION_B:
                    case KEY_DOWNRIGHT:
                        spriteFacing[SPRITE_INDEX_PLAYER] = SPRITE_FACING_RIGHT;
                        animSel = ANIM_INDEX_PLAYER_SWEEP;
                        //audio.playSfx(Audio.SFX_SWING);
                        //audio.playMsx(Audio.AUDIO_KICK);
                        playerAttackDamage = 10;
                        playerAttack = true;
                        break;
                    case KEY_LEFT:
                        if (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_LEFT) {
                            animSel = ANIM_INDEX_PLAYER_WALK;
                        } else {
                            //spriteFacing[SPRITE_INDEX_PLAYER] =
                            // SPRITE_FACING_LEFT;
                            animSel = ANIM_INDEX_PLAYER_ATTACKBACK;
                            playerAttackDamage = 10;
                            playerAttack = true;
                        }
                        break;
                    case KEY_RIGHT:
                        if (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_RIGHT) {
                            animSel = ANIM_INDEX_PLAYER_WALK;
                        } else {
                            //spriteFacing[SPRITE_INDEX_PLAYER] =
                            // SPRITE_FACING_RIGHT;
                            animSel = ANIM_INDEX_PLAYER_ATTACKBACK;
                            playerAttackDamage = 10;
                            playerAttack = true;
                        }
                        break;
                    case KEY_FIRE:
                        comboPower += 15;
                        if (comboPower >= 100) {
                            comboPower = 100;
                        }
                        playerFirePressed = true;
                        break;
                    case KEY_ACTION_C:
                        if (playerSpecials > 0) {
                            animSel = ANIM_INDEX_PLAYER_SPECIAL;
                            playerAttackDamage = 100;
                            playerAttack = true;
                            playerSpecials--;
                        }
                        if (CHEATS
                                && Cheats
                                        .isCheatEnabled(Cheats.CHEAT_INFINITE_SPECIALS)) {
                            playerSpecials = 3;
                        }
                        break;
                }
                if (keyCode != KEY_LEFT && keyCode != KEY_RIGHT
                        && keyCode != KEY_FIRE) {
                    keyPressed = false;
                    keyCode = KEY_NONE;
                }
            }
        } else if (animSel == ANIM_INDEX_PLAYER_WALK) {
            animSel = ANIM_INDEX_PLAYER_STAND;
            spriteY[SPRITE_INDEX_PLAYER] = spriteBaseY[SPRITE_INDEX_PLAYER];
        } else if (playerFirePressed) {
            if (comboPower >= 100) {
                currentCombo++;
                if (currentCombo >= ANIM_INDEX_PLAYER_COMBO_SEQ.length) {
                    currentCombo = 0;
                }
                animSel = ANIM_INDEX_PLAYER_COMBO_SEQ[currentCombo];
                playerAttackDamage = 50;
                playerAttack = true;
                comboMode = true;
                comboPower = 0;
            } else {
                animSel = ANIM_INDEX_PLAYER_RANDOM_ATTACK_SIMPLE[Math
                        .abs(random.nextInt()
                                % ANIM_INDEX_PLAYER_RANDOM_ATTACK_SIMPLE.length)];
                playerAttack = true;
                playerAttackDamage = comboPower / 2;
                comboPower = 0;
            }
            playerFirePressed = false;
        }
        if (animSel != animIndex[SPRITE_INDEX_PLAYER]) {
            playAnim(SPRITE_INDEX_PLAYER, animSel);
        }
        playerJump = animSel == ANIM_INDEX_PLAYER_JUMP
                || animSel == ANIM_INDEX_PLAYER_JUMP_DOWN
                || animSel == ANIM_INDEX_PLAYER_JUMP_UP;
        playerOnFloor = isOnPlatform(SPRITE_INDEX_PLAYER)
                || (playerAttack && playerSpecial);
        if (!playerOnFloor && !playerJump && animSel != ANIM_INDEX_PLAYER_DIE
                && !spriteFalling[SPRITE_INDEX_PLAYER]) {
            spriteFalling[SPRITE_INDEX_PLAYER] = true;
            playerAttack = false;
            playAnim(SPRITE_INDEX_PLAYER, ANIM_INDEX_PLAYER_FALL);
            spriteFallRate[SPRITE_INDEX_PLAYER] = 1;
        }
        if (spriteY[SPRITE_INDEX_PLAYER] > (mapRows[mapIndex] + 8) * tileHeight) {
            nextPlayState = PLAY_STATE_DEAD;
        }
        if (!playerFirePressed && comboPower > 0 && currentTick % 2 == 0) {
            comboPower--;
        }
        isOnPlatformBypass[SPRITE_INDEX_PLAYER]--;
        //System.out.println("BaseY: " + spriteBaseY[1]);
    }

    public int enemyReactionTime[] = new int[NUM_SPRITES];

    public int enemyKey[] = new int[NUM_SPRITES];

    public boolean enemyAttack[] = new boolean[NUM_SPRITES];

    int nextEnemyAttackDelay;

    /*
     *  
     */
    private void updateEnemySprites() {
        //int k;
        boolean enemyWasHit = false;
        nextEnemyAttackDelay--;
        for (int c = SPRITE_INDEX_ENEMY_START; c <= SPRITE_INDEX_ENEMY_END; c++) {
            if (spriteVisible[c]) {
                // caida abismo
                if (spriteY[c] > (mapRows[mapIndex] + 8) * tileHeight) {
                    spriteVisible[c] = false;
                    enemyAttack[c] = false;
                    enemiesLeft--;
                    if (DEBUG) {
                        System.out.println("EnemiesLeft: " + enemiesLeft);
                    }
                    if (enemiesLeft == 0) {
                        if (bossMode) {
                            nextPlayState = PLAY_STATE_LEVEL_END;
                        } else if (mapSectionIndex < mapSection[mapIndex].length - 1) {
                            mapSectionIndex++;
                        }
                    }
                }
                // muri�
                if (animIndex[c] == ANIM_INDEX_ENEMY_DIE[spriteType[c]]) {
                    if (isAnimFinished(c)) {
                        if (spriteEnergy[c] <= 0) {
                            spriteVisible[c] = false;
                            enemyAttack[c] = false;
                            if (bossMode && enemiesLeft == 0) {
                                nextPlayState = PLAY_STATE_LEVEL_END;
                            }
                        } else {
                            playAnim(c, ANIM_INDEX_ENEMY_WALK[spriteType[c]]);
                        }
                    }
                    // comprobar si fu� golpeado por el jugador
                } else if (playerAttack
                        && animIndex[c] != ANIM_INDEX_ENEMY_DIE[spriteType[c]]
                        && animIndex[c] != ANIM_INDEX_ENEMY_HIT[spriteType[c]]
                        && intersects(SPRITE_INDEX_PLAYER, c)
                        && spriteBaseY[c] == spriteBaseY[SPRITE_INDEX_PLAYER]) {
                    if (animIndex[SPRITE_INDEX_PLAYER] == ANIM_INDEX_PLAYER_ATTACKBACK) {
                        spriteFacing[c] = (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_RIGHT
                                ? SPRITE_FACING_RIGHT : SPRITE_FACING_LEFT);
                    } else {
                        spriteFacing[c] = (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_RIGHT
                                ? SPRITE_FACING_LEFT : SPRITE_FACING_RIGHT);
                    }
                    spriteEnergy[c] -= playerAttackDamage;
                    spriteHit[c] = true;
                    enemyAttack[c] = false;
                    shakeScreen(2);
                    if (spriteEnergy[c] > 0) {
                        playAnim(c, ANIM_INDEX_ENEMY_HIT[spriteType[c]]);
                        enemyReactionTime[c] = 100;
                        lastEnemyHit = c;
                        playerScore += playerAttackDamage;
                        comboPower += 10;
                        genSpriteHitFx(c, false);
                        enemyWasHit = true;
                    } else {
                        playAnim(c, ANIM_INDEX_ENEMY_DIE[spriteType[c]]);
                        spriteEnergy[c] = 0;
                        lastEnemyHit = -1;
                        playerScore += playerAttackDamage + 50;
                        comboPower += 20;
                        enemiesLeft--;
                        enemyAttack[c] = false;
                        genSpriteHitFx(c, true);
                        enemyWasHit = true;
                        if (DEBUG) {
                            System.out.println("EnemiesLeft: " + enemiesLeft);
                        }
                        if (!bossMode
                                && enemiesLeft == 0
                                && mapSectionIndex < mapSection[mapIndex].length) {
                            mapSectionIndex++;
                        }
                    }
                    return;
                    // acciones varias
                } else {
                    if (isAnimFinished(c)
                            || animIndex[c] == ANIM_INDEX_ENEMY_STAND[spriteType[c]]) {
                        spriteY[c] -= spriteY[c] % tileHeight; //reajuste al
                        // tile
                        enemyKey[c] = KEY_NONE;
                        enemyReactionTime[c] /= 2;
                    }
                    enemyReactionTime[c]--;
                    if ((enemyReactionTime[c] <= 0 || enemyWasHit)
                            && (isAnimFinished(c)
                                    || animIndex[c] == ANIM_INDEX_ENEMY_WALK[spriteType[c]] || animIndex[c] == ANIM_INDEX_ENEMY_STAND[spriteType[c]])) {
                        enemyReactionTime[c] = Math.abs(random.nextInt() % 10) + 10;
                        //media distancia al jugador
                        if (intersects(c, SPRITE_INDEX_PLAYER, 96)
                                || intersects(c, SPRITE_INDEX_PLAYER, -64)) {
                            // mirar al jugador
                            if (spriteX[c] < spriteX[SPRITE_INDEX_PLAYER]) {
                                spriteFacing[c] = SPRITE_FACING_RIGHT;
                            } else {
                                spriteFacing[c] = SPRITE_FACING_LEFT;
                            }
                            //corta distancia
                            if ((intersects(c, SPRITE_INDEX_PLAYER, 8))) {
                                if (spriteBaseY[c] == spriteBaseY[SPRITE_INDEX_PLAYER]) {
                                    // si el jugador no est� siendo golpeado o
                                    // est�
                                    // muerto, intentar atacar
                                    if ((animIndex[SPRITE_INDEX_PLAYER] != ANIM_INDEX_PLAYER_DIE)
                                            && (animIndex[SPRITE_INDEX_PLAYER] != ANIM_INDEX_PLAYER_HIT)) {
                                        if (nextEnemyAttackDelay <= 0) {
                                            enemyKey[c] = isEnemyOnPlatform(
                                                    c,
                                                    spriteFacing[c] == SPRITE_FACING_LEFT
                                                            ? -24 : 24, 0)
                                                    ? KEY_FIRE
                                                    : spriteFacing[c] == SPRITE_FACING_LEFT
                                                            ? SPRITE_FACING_RIGHT
                                                            : SPRITE_FACING_LEFT;
                                            enemyReactionTime[c] += 5 + Math
                                                    .abs(random.nextInt() % 10);
                                            nextEnemyAttackDelay = 5 + Math
                                                    .abs(random.nextInt() % 5);
                                        } else { // rutina de espera de ataque
                                            spriteFacing[c] = (spriteFacing[c] == SPRITE_FACING_RIGHT
                                                    ? SPRITE_FACING_LEFT
                                                    : SPRITE_FACING_RIGHT);
                                            enemyKey[c] = (spriteFacing[c] == SPRITE_FACING_RIGHT
                                                    ? KEY_RIGHT : KEY_LEFT);
                                            enemyReactionTime[c] = 4 + random
                                                    .nextInt() % 2;
                                        }
                                    } else {
                                        // qu� hacer cuando el jugador est�
                                        // siendo
                                        // golpeado y el enemigo est� cerca
                                    }
                                } else if (spriteBaseY[c] < spriteBaseY[SPRITE_INDEX_PLAYER]) {
                                    enemyKey[c] = KEY_DOWN;
                                    // si est� en un nivel inferior, intenta
                                    // subir
                                } else if (spriteY[c] > spriteY[SPRITE_INDEX_PLAYER]
                                        && (isEnemyOnPlatform(c, 0, -8)
                                                || isEnemyOnPlatform(c, 0, -16)
                                                || isEnemyOnPlatform(c, 0, -24) || isEnemyOnPlatform(
                                                c, 0, -32))) {
                                    enemyKey[c] = KEY_UP;
                                }
                            }
                            // larga distancia, cambiar acci�n 50% posib.
                        } else if (random.nextInt() % 50 > 0) {
                            // si puede dirigirse al jugador, va hacia �l
                            if (spriteX[c] < spriteX[SPRITE_INDEX_PLAYER]) {
                                enemyKey[c] = isEnemyOnPlatform(c, 8, 2)
                                        ? KEY_RIGHT : KEY_LEFT;
                            } else {
                                enemyKey[c] = isEnemyOnPlatform(c, -8, 2)
                                        ? KEY_LEFT : KEY_RIGHT;
                            }
                            // si est� en un nivel superior, intenta bajar
                        } else if (spriteBaseY[c] < spriteBaseY[SPRITE_INDEX_PLAYER]
                                && spriteBaseY[SPRITE_INDEX_PLAYER] < (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                                        * tileHeight) {
                            enemyKey[c] = KEY_DOWN;
                            // si est� en un nivel inferior, intenta subir
                        } else if (spriteBaseY[c] > spriteBaseY[SPRITE_INDEX_PLAYER]
                                && (isEnemyOnPlatform(c, 0, -16)
                                        || isEnemyOnPlatform(c, 0, -32)
                                        || isEnemyOnPlatform(c, 0, -48) || isEnemyOnPlatform(
                                        c, 0, -64))) {
                            int delta = spriteX[c]
                                    - spriteX[SPRITE_INDEX_PLAYER];
                            if (delta < -tileWidth * 3) {
                                enemyKey[c] = KEY_UPRIGHT;
                            } else if (delta > tileWidth * 3) {
                                enemyKey[c] = KEY_UPLEFT;
                            } else {
                                enemyKey[c] = KEY_UP;
                            }
                        } else {
                            // rutina de espera
                            if (spriteBaseY[c] == spriteBaseY[SPRITE_INDEX_PLAYER]) {
                                playAnim(c,
                                        ANIM_INDEX_ENEMY_WALK[spriteType[c]]);
                            } else {
                                if (random.nextInt() > 0) {
                                    playAnim(
                                            c,
                                            ANIM_INDEX_ENEMY_STAND[spriteType[c]]);
                                    enemyKey[c] = KEY_NONE;
                                } else {
                                    enemyKey[c] = random.nextInt() > 0
                                            ? KEY_LEFT : KEY_RIGHT;
                                    playAnim(
                                            c,
                                            ANIM_INDEX_ENEMY_WALK[spriteType[c]]);
                                }
                            }
                        }
                    } else {
                        boolean eop;
                        // rutina para saltar abismos y alguna plataforma
                        if (animIndex[c] == ANIM_INDEX_ENEMY_WALK[spriteType[c]]
                                || animIndex[c] == ANIM_INDEX_ENEMY_STAND[spriteType[c]]) {
                            switch (spriteFacing[c]) {
                                case SPRITE_FACING_LEFT:
                                    eop = isEnemyOnPlatform(c, -16, 0);
                                    if ((spriteY[c] + 16 > (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                                            * tileHeight - 48)
                                            && !eop) {
                                        enemyKey[c] = KEY_UPLEFT;
                                    } else if (!eop) {
                                        enemyKey[c] = KEY_RIGHT;
                                        enemyReactionTime[c] = 8;
                                    }
                                    break;
                                case SPRITE_FACING_RIGHT:
                                    eop = isEnemyOnPlatform(c, 16, 0);
                                    if ((spriteY[c] + 16 > (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                                            * tileHeight - 48)
                                            && !eop) {
                                        enemyKey[c] = KEY_UPRIGHT;
                                    } else if (!eop) {
                                        enemyKey[c] = KEY_LEFT;
                                        enemyReactionTime[c] = 8;
                                    }
                                    break;
                            }
                        }
                    }
                }
                //                if (spriteType[c] == ANIM_INDEX_ENEMY_JULIAN) {
                //                    String[] keyName = { "KEY_NONE", "KEY_UP", "KEY_DOWN",
                //                            "KEY_LEFT", "KEY_RIGHT", "KEY_ACTION_A",
                //                            "KEY_ACTION_B", "KEY_ACTION_C", "KEY_ACTION_D",
                //                            "KEY_SOFT1", "KEY_SOFT2", "KEY_FIRE", "KEY_UPLEFT",
                //                            "KEY_DOWNLEFT", "KEY_UPRIGHT", "KEY_DOWNRIGHT" };
                //                    //System.out.println(keyName[enemyKey[c]]);
                //                    enemyKey[c] = keyCode;
                //                }
                updateEnemySprite(c);
            }
        }
    }

    /*
     *  
     */
    private void updateBossSprite() {
        boolean enemyWasHit = false;
        int c = SPRITE_INDEX_BOSS;
        if (spriteVisible[c]) {
            if (spriteEnergy[c] > 0 && spriteEnergy[c] < 100
                    && currentTick % 4 == 0) {
                spriteEnergy[c] += 1;
            }
            // caida abismo
            if (spriteY[c] > (mapRows[mapIndex] + 8) * tileHeight) {
                spriteVisible[c] = false;
                enemyAttack[c] = false;
                enemiesLeft--;
                if (DEBUG) {
                    System.out.println("EnemiesLeft: " + enemiesLeft);
                }
                if (enemiesLeft < 0) {
                    nextPlayState = PLAY_STATE_LEVEL_END;
                }
            }
            // muri�
            if (animIndex[c] == ANIM_INDEX_ENEMY_DIE[spriteType[c]]) {
                if (isAnimFinished(c)) {
                    if (spriteEnergy[c] <= 0) {
                        spriteVisible[c] = false;
                        enemyAttack[c] = false;
                        nextPlayState = PLAY_STATE_LEVEL_END;
                    } else {
                        playAnim(c, ANIM_INDEX_ENEMY_WALK[spriteType[c]]);
                    }
                }
                // comprobar si fu� golpeado por el jugador
            } else if (playerAttack
                    && animIndex[c] != ANIM_INDEX_ENEMY_DIE[spriteType[c]]
                    && animIndex[c] != ANIM_INDEX_ENEMY_HIT[spriteType[c]]
                    && intersects(SPRITE_INDEX_PLAYER, c)
                    && spriteBaseY[c] == spriteBaseY[SPRITE_INDEX_PLAYER]) {
                if (animIndex[SPRITE_INDEX_PLAYER] == ANIM_INDEX_PLAYER_ATTACKBACK) {
                    spriteFacing[c] = (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_RIGHT
                            ? SPRITE_FACING_RIGHT : SPRITE_FACING_LEFT);
                } else {
                    spriteFacing[c] = (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_RIGHT
                            ? SPRITE_FACING_LEFT : SPRITE_FACING_RIGHT);
                }
                spriteEnergy[c] -= playerAttackDamage / 5;
                spriteHit[c] = true;
                enemyAttack[c] = false;
                shakeScreen(2);
                if (spriteEnergy[c] > 0) {
                    playAnim(c, ANIM_INDEX_ENEMY_HIT[spriteType[c]]);
                    enemyReactionTime[c] = 50;
                    lastEnemyHit = c;
                    playerScore += playerAttackDamage;
                    comboPower += 10;
                    genSpriteHitFx(c, false);
                    enemyWasHit = true;
                } else {
                    playAnim(c, ANIM_INDEX_ENEMY_DIE[spriteType[c]]);
                    spriteEnergy[c] = 0;
                    lastEnemyHit = -1;
                    playerScore += playerAttackDamage + 50;
                    comboPower += 20;
                    enemiesLeft--;
                    enemyAttack[c] = false;
                    genSpriteHitFx(c, true);
                    enemyWasHit = true;
                }
                return;
                // acciones varias
            } else {
                if (isAnimFinished(c)
                        || animIndex[c] == ANIM_INDEX_ENEMY_STAND[spriteType[c]]) {
                    spriteY[c] -= spriteY[c] % tileHeight; //reajuste al
                    // tile
                    if (spriteX[SPRITE_INDEX_PLAYER] < spriteX[c]) {
                        enemyKey[c] = KEY_LEFT;
                    } else {
                        enemyKey[c] = KEY_RIGHT;
                    }
                    enemyReactionTime[c] /= 2;
                }
                enemyReactionTime[c]--;
                if ((enemyReactionTime[c] <= 0 || enemyWasHit)
                        && (isAnimFinished(c)
                                || animIndex[c] == ANIM_INDEX_ENEMY_WALK[spriteType[c]] || animIndex[c] == ANIM_INDEX_ENEMY_STAND[spriteType[c]])) {
                    enemyReactionTime[c] = Math.abs(random.nextInt() % 10) + 2;
                    //media distancia al jugador
                    if (intersects(c, SPRITE_INDEX_PLAYER, 96)
                            || intersects(c, SPRITE_INDEX_PLAYER, -64)) {
                        // mirar al jugador
                        if (spriteX[c] < spriteX[SPRITE_INDEX_PLAYER]) {
                            spriteFacing[c] = SPRITE_FACING_RIGHT;
                        } else {
                            spriteFacing[c] = SPRITE_FACING_LEFT;
                        }
                        //corta distancia
                        if ((intersects(c, SPRITE_INDEX_PLAYER, 8))) {
                            if (spriteBaseY[c] == spriteBaseY[SPRITE_INDEX_PLAYER]) {
                                // si el jugador no est� siendo golpeado o
                                // est�
                                // muerto, intentar atacar
                                if ((animIndex[SPRITE_INDEX_PLAYER] != ANIM_INDEX_PLAYER_DIE)
                                        && (animIndex[SPRITE_INDEX_PLAYER] != ANIM_INDEX_PLAYER_HIT)) {
                                    enemyKey[c] = isEnemyOnPlatform(
                                            c,
                                            spriteFacing[c] == SPRITE_FACING_LEFT
                                                    ? -24 : 24, 0)
                                            ? KEY_FIRE
                                            : spriteFacing[c] == SPRITE_FACING_LEFT
                                                    ? SPRITE_FACING_RIGHT
                                                    : SPRITE_FACING_LEFT;
                                    enemyReactionTime[c] += 2 + Math.abs(random
                                            .nextInt() % 5);
                                    nextEnemyAttackDelay = 2 + Math.abs(random
                                            .nextInt() % 2);
                                } else {
                                    // qu� hacer cuando el jugador est�
                                    // siendo
                                    // golpeado y el enemigo est� cerca
                                }
                            } else if (spriteBaseY[c] < spriteBaseY[SPRITE_INDEX_PLAYER]) {
                                enemyKey[c] = KEY_DOWN;
                                // si est� en un nivel inferior, intenta
                                // subir
                            } else if (spriteY[c] > spriteY[SPRITE_INDEX_PLAYER]
                                    && (isEnemyOnPlatform(c, 0, -8)
                                            || isEnemyOnPlatform(c, 0, -16)
                                            || isEnemyOnPlatform(c, 0, -24) || isEnemyOnPlatform(
                                            c, 0, -32))) {
                                enemyKey[c] = KEY_UP;
                            }
                        }
                        // larga distancia, cambiar acci�n 50% posib.
                    } else if (random.nextInt() % 50 > 0) {
                        // si puede dirigirse al jugador, va hacia �l
                        if (spriteX[c] < spriteX[SPRITE_INDEX_PLAYER]) {
                            enemyKey[c] = isEnemyOnPlatform(c, 8, 2)
                                    ? KEY_RIGHT : KEY_LEFT;
                        } else {
                            enemyKey[c] = isEnemyOnPlatform(c, -8, 2)
                                    ? KEY_LEFT : KEY_RIGHT;
                        }
                        // si est� en un nivel superior, intenta bajar
                    } else if (spriteBaseY[c] < spriteBaseY[SPRITE_INDEX_PLAYER]
                            && spriteBaseY[SPRITE_INDEX_PLAYER] < (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                                    * tileHeight) {
                        enemyKey[c] = KEY_DOWN;
                        // si est� en un nivel inferior, intenta subir
                    } else if (spriteBaseY[c] > spriteBaseY[SPRITE_INDEX_PLAYER]
                            && (isEnemyOnPlatform(c, 0, -16)
                                    || isEnemyOnPlatform(c, 0, -32)
                                    || isEnemyOnPlatform(c, 0, -48) || isEnemyOnPlatform(
                                    c, 0, -64))) {
                        int delta = spriteX[c] - spriteX[SPRITE_INDEX_PLAYER];
                        if (delta < -tileWidth * 3) {
                            enemyKey[c] = KEY_UPRIGHT;
                        } else if (delta > tileWidth * 3) {
                            enemyKey[c] = KEY_UPLEFT;
                        } else {
                            enemyKey[c] = KEY_UP;
                        }
                    } else {
                        playAnim(c, ANIM_INDEX_ENEMY_WALK[spriteType[c]]);
                        enemyKey[c] = spriteX[SPRITE_INDEX_PLAYER] > spriteX[SPRITE_INDEX_BOSS]
                                ? KEY_LEFT : KEY_RIGHT;
                    }
                } else {
                    boolean eop;
                    // rutina para saltar abismos y alguna plataforma
                    if (animIndex[c] == ANIM_INDEX_ENEMY_WALK[spriteType[c]]
                            || animIndex[c] == ANIM_INDEX_ENEMY_STAND[spriteType[c]]) {
                        switch (spriteFacing[c]) {
                            case SPRITE_FACING_LEFT:
                                eop = isEnemyOnPlatform(c, -16, 0);
                                if ((spriteY[c] + 16 > (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                                        * tileHeight - 48)
                                        && !eop
                                        && (isEnemyOnPlatform(c, -80, 0) || isEnemyOnPlatform(
                                                c, -80, -16))) {
                                    enemyKey[c] = KEY_UPLEFT;
                                } else if (!eop) {
                                    enemyReactionTime[c] = 10;
                                    enemyKey[c] = KEY_RIGHT;
                                    spriteFacing[c] = SPRITE_FACING_RIGHT;
                                    playAnim(
                                            c,
                                            ANIM_INDEX_ENEMY_WALK[spriteType[c]]);
                                }
                                break;
                            case SPRITE_FACING_RIGHT:
                                eop = isEnemyOnPlatform(c, 16, 0);
                                if ((spriteY[c] + 16 > (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                                        * tileHeight - 48)
                                        && !eop
                                        && (isEnemyOnPlatform(c, 80, 0) || isEnemyOnPlatform(
                                                c, 80, -16))) {
                                    enemyKey[c] = KEY_UPRIGHT;
                                } else if (!eop) {
                                    enemyReactionTime[c] = 10;
                                    enemyKey[c] = KEY_LEFT;
                                    spriteFacing[c] = SPRITE_FACING_LEFT;
                                    playAnim(
                                            c,
                                            ANIM_INDEX_ENEMY_WALK[spriteType[c]]);
                                }
                                break;
                        }
                    }
                }
            }
            updateEnemySprite(c);
        }
    }

    public static final int ENEMY_SPAWN_DELAY = 10;

    public int enemySpawnCounter;

    public int nextEnemyIndex = SPRITE_INDEX_ENEMY_START;

    public boolean[] enemyJump = new boolean[NUM_SPRITES];

    public static boolean bossMode;

    int[] spritePreviousY = new int[NUM_SPRITES];

    int[] spriteDeltaY = new int[NUM_SPRITES];

    /**
     * 
     * @param sIndex
     * @param dead
     */
    protected abstract void genSpriteHitFx(int sIndex, boolean dead);

    /*
     *  
     */
    private void updateEnemySprite(int sIndex) {
        boolean enemyOnFloor;// = isEnemyOnPlatform(sIndex, 0, 0);
        int animSel = animIndex[sIndex];
        //System.out.println("EK: " + sIndex + " -> " + enemyKey[sIndex]);
        updateSprite(sIndex);
        enemyOnFloor = isEnemyOnPlatform(sIndex, 0, 0);
        if (spriteY[SPRITE_INDEX_PLAYER] - spritePreviousY[sIndex] > 0) {
            spriteDeltaY[sIndex] = spriteY[SPRITE_INDEX_PLAYER]
                    - spritePreviousY[sIndex];
        }
        spritePreviousY[sIndex] = spriteY[SPRITE_INDEX_PLAYER];
        if (animIndex[sIndex] == ANIM_INDEX_ENEMY_DIE[spriteType[sIndex]]) {
            return;
        }
        if (isAnimFinished(sIndex)
                && animSel != ANIM_INDEX_ENEMY_STAND[spriteType[sIndex]]) {
            if (enemyOnFloor) {
                animSel = ANIM_INDEX_ENEMY_STAND[spriteType[sIndex]];
                enemyAttack[sIndex] = false;
                enemyKey[sIndex] = KEY_NONE;
                enemyReactionTime[sIndex] = 0;
                spriteY[sIndex] -= spriteY[sIndex] % tileHeight;
            } else if (!spriteFalling[sIndex]) {
                spriteFalling[sIndex] = true;
                animSel = ANIM_INDEX_ENEMY_FALL[spriteType[sIndex]];
                spriteFalling[sIndex] = true;
                enemyAttack[sIndex] = false;
                spriteFallRate[sIndex] = spriteDeltaY[sIndex];
                //System.out.println("DELTAY: " + deltaY);
            }
            spriteHit[sIndex] = false;
        }
        enemyJump[sIndex] = false;
        for (int c = 0; c < ANIM_INDEX_ENEMY_JUMP[spriteType[sIndex]].length
                && !enemyJump[sIndex]; c++) {
            enemyJump[sIndex] = (animSel == ANIM_INDEX_ENEMY_JUMP[spriteType[sIndex]][c]);
        }
        if (enemyJump[sIndex] && getFrameOffsetY(sIndex) > 0
                && isEnemyOnPlatform(sIndex, 0, 0)) {
            animSel = ANIM_INDEX_ENEMY_STAND[spriteType[sIndex]];
            spriteY[sIndex] -= spriteY[sIndex] % tileHeight;
            enemyJump[sIndex] = false;
            enemyAttack[sIndex] = false;
            enemyKey[sIndex] = KEY_NONE;
            spriteFalling[sIndex] = false;
            enemyReactionTime[sIndex] = 1;
            spriteHit[sIndex] = false;
        }
        if (spriteFalling[sIndex]) {
            spriteHit[sIndex] = false;
            enemyAttack[sIndex] = false;
            //enemyKey[sIndex] = KEY_NONE;
            enemyOnFloor = false;
            int y;
            for (y = 0; y < spriteFallRate[sIndex] && !enemyOnFloor; y++) {
                enemyOnFloor = isEnemyOnPlatform(sIndex, 0, y);
            }
            spriteY[sIndex] += y;
            //            System.out.println(enemyOnFloor + " " + spriteY[sIndex] + " "
            //                    + spriteFallRate[sIndex]);
            if (enemyOnFloor) {
                spriteFalling[sIndex] = false;
                animSel = ANIM_INDEX_ENEMY_STAND[spriteType[sIndex]];
                spriteY[sIndex] -= spriteY[sIndex] % tileHeight;
            } else {
                spriteFallRate[sIndex] += 2;
            }
        }
        if ((animSel == ANIM_INDEX_ENEMY_STAND[spriteType[sIndex]] || animSel == ANIM_INDEX_ENEMY_WALK[spriteType[sIndex]])) {
            switch (enemyKey[sIndex]) {
                case KEY_NONE:
                    break;
                case KEY_UP:
                    animSel = ANIM_INDEX_ENEMY_JUMP[spriteType[sIndex]][1];
                    break;
                case KEY_UPLEFT:
                    spriteFacing[sIndex] = SPRITE_FACING_LEFT;
                    animSel = ANIM_INDEX_ENEMY_JUMP[spriteType[sIndex]][0];
                    if (spriteType[sIndex] == ANIM_INDEX_ENEMY_CHEMA) {
                        enemyAttack[sIndex] = true;
                    }
                    break;
                case KEY_UPRIGHT:
                    spriteFacing[sIndex] = SPRITE_FACING_RIGHT;
                    animSel = ANIM_INDEX_ENEMY_JUMP[spriteType[sIndex]][0];
                    if (spriteType[sIndex] == ANIM_INDEX_ENEMY_CHEMA) {
                        enemyAttack[sIndex] = true;
                    }
                    break;
                case KEY_DOWN:
                    if (spriteY[sIndex] < (mapRows[mapIndex] - mapFloorHeight[mapIndex])
                            * tileHeight
                            && !spriteFalling[sIndex]) {
                        animSel = ANIM_INDEX_ENEMY_FALL[spriteType[sIndex]];
                        isOnPlatformBypass[sIndex] = 6;
                        enemyOnFloor = false;
                        spriteFalling[sIndex] = true;
                        spriteFallRate[sIndex] = 2;
                        //                        System.out.println("DOWN: " + sIndex
                        //                                + isEnemyOnPlatform(sIndex, 0, 0));
                    }
                    enemyAttack[sIndex] = false;
                    enemyKey[sIndex] = KEY_NONE;
                    break;
                case KEY_ACTION_A:
                case KEY_DOWNLEFT:
                    spriteFacing[sIndex] = SPRITE_FACING_LEFT;
                    break;
                case KEY_ACTION_B:
                case KEY_DOWNRIGHT:
                    spriteFacing[sIndex] = SPRITE_FACING_RIGHT;
                    break;
                case KEY_LEFT:
                    if (spriteFacing[sIndex] == SPRITE_FACING_LEFT) {
                        animSel = ANIM_INDEX_ENEMY_WALK[spriteType[sIndex]];
                    } else {
                        spriteFacing[sIndex] = SPRITE_FACING_LEFT;
                    }
                    break;
                case KEY_RIGHT:
                    if (spriteFacing[sIndex] == SPRITE_FACING_RIGHT) {
                        animSel = ANIM_INDEX_ENEMY_WALK[spriteType[sIndex]];
                    } else {
                        spriteFacing[sIndex] = SPRITE_FACING_RIGHT;
                    }
                    break;
                case KEY_FIRE:
                    animSel = ANIM_INDEX_ENEMY_ATTACK[spriteType[sIndex]][Math
                            .abs(random.nextInt()
                                    % ANIM_INDEX_ENEMY_ATTACK[spriteType[sIndex]].length)];
                    if (CHEATS
                            && Cheats
                                    .isCheatEnabled(Cheats.CHEAT_IGNORE_ENEMIES_ATTACK)) {
                        enemyAttack[sIndex] = false;
                    } else {
                        enemyAttack[sIndex] = true;
                    }
                    enemyKey[sIndex] = KEY_NONE;
                    break;
                case KEY_ACTION_C:
                    break;
                case KEY_ACTION_D:
                    break;
            }
        }
        if (animSel != animIndex[sIndex]) {
            spriteHit[sIndex] = false;
            //spriteY[sIndex] -= spriteY[sIndex] % tileHeight;
            playAnim(sIndex, animSel);
        }
        enemyJump[sIndex] = false;
        for (int c = 0; c < ANIM_INDEX_ENEMY_JUMP[spriteType[sIndex]].length
                && !enemyJump[sIndex]; c++) {
            enemyJump[sIndex] = (animSel == ANIM_INDEX_ENEMY_JUMP[spriteType[sIndex]][c]);
        }
        enemyOnFloor = isEnemyOnPlatform(sIndex, 0, 0);
        if (!enemyOnFloor && !enemyJump[sIndex] && !spriteFalling[sIndex]) {
            playAnim(sIndex, ANIM_INDEX_ENEMY_FALL[spriteType[sIndex]]);
            enemyAttack[sIndex] = false;
            spriteFallRate[sIndex] = 1;
            spriteFalling[sIndex] = true;
            spriteHit[sIndex] = false;
        }
        isOnPlatformBypass[sIndex]--;
    }

    /*
     *  
     */
    private void spawnEnemy() {
        if (spawnsLeft > 0) {
            if (!spriteVisible[nextEnemyIndex]) {
                if (DEBUG) {
                    System.out.println("TRYING SPAWN: " + nextEnemyIndex
                            + " enemiesLeft: " + enemiesLeft + " spawnsLeft: "
                            + spawnsLeft);
                }
                spriteX[nextEnemyIndex] = cameraX
                        + (random.nextInt() > 0 ? displayWidth + 8 : -40);
                if (!bossMode) {
                    spriteX[nextEnemyIndex] = cameraX
                            + (random.nextInt() > 0 ? displayWidth + 8 : -40);
                    int ty = cameraY
                            + (random.nextInt() > 0 ? (mapRows[mapIndex]
                                    - mapFloorHeight[mapIndex] - 4)
                                    * tileHeight : (random.nextInt() > 0 ? 0
                                    : (mapRows[mapIndex]
                                            - mapFloorHeight[mapIndex] - 3)
                                            * tileHeight / 2));
                    spriteY[nextEnemyIndex] = ty;
                } else {
                    spriteX[nextEnemyIndex] = (mapCols[mapIndex] - 1)
                            * tileWidth;
                    spriteY[nextEnemyIndex] = (mapRows[mapIndex]
                            - mapFloorHeight[mapIndex] - 3)
                            * tileHeight;
                }
                isOnPlatformBypass[nextEnemyIndex] = 0;
                spriteY[nextEnemyIndex] -= spriteY[nextEnemyIndex] % tileHeight;
                spriteX[nextEnemyIndex] -= spriteX[nextEnemyIndex] % tileWidth;
                if (spriteY[nextEnemyIndex] > (mapRows[mapIndex]
                        - mapFloorHeight[mapIndex] - 3)
                        * tileHeight) {
                    spriteY[nextEnemyIndex] = (mapRows[mapIndex]
                            - mapFloorHeight[mapIndex] - 3)
                            * tileHeight;
                }
                if (spriteY[nextEnemyIndex] < 0) {
                    spriteY[nextEnemyIndex] = 0;
                }
                if (DEBUG) {
                    System.out.println("SPAWN X: " + spriteX[nextEnemyIndex]
                            + " Y: " + spriteY[nextEnemyIndex]);
                    System.out.println("PLAYER X: " + spriteX[0] + " Y: "
                            + spriteY[0]);
                }
                enemyAttack[nextEnemyIndex] = false;
                spriteFacing[nextEnemyIndex] = (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_LEFT
                        ? SPRITE_FACING_RIGHT : SPRITE_FACING_LEFT);
                spriteType[nextEnemyIndex] = ENEMIES_ON_LEVEL[mapIndex][Math
                        .abs(random.nextInt()
                                % ENEMIES_ON_LEVEL[mapIndex].length)];
                animBank[nextEnemyIndex] = spriteType[nextEnemyIndex] + 1;
                enemyKey[nextEnemyIndex] = spriteFacing[nextEnemyIndex] == SPRITE_FACING_LEFT
                        ? KEY_LEFT : KEY_RIGHT;
                enemyJump[nextEnemyIndex] = false;
                enemyReactionTime[nextEnemyIndex] = 8;
                playAnim(nextEnemyIndex,
                        ANIM_INDEX_ENEMY_FALL[spriteType[nextEnemyIndex]]);
                spriteFalling[nextEnemyIndex] =true;
                updateSprite(nextEnemyIndex);
                boolean abortSpawn = false;
                while (!isEnemyOnPlatform(nextEnemyIndex, 0, 0) && !abortSpawn
                        && !bossMode) {
                    if (spriteY[nextEnemyIndex] > (mapRows[mapIndex]
                            - mapFloorHeight[mapIndex] - 3)
                            * tileHeight) {
                        if (DEBUG) {
                            System.out.println("SPAWN ABORTED");
                        }
                        abortSpawn = true;
                    } else {
                        spriteY[nextEnemyIndex]++;
                    }
                }
                if (!abortSpawn) {
                    spriteVisible[nextEnemyIndex] = true;
                    spriteEnergy[nextEnemyIndex] = 100;
                    spawnsLeft--;
                }
            } else {
                if (DEBUG) {
                    System.out.println("SPAWN SKIP INDEX: " + nextEnemyIndex);
                }
                nextEnemyIndex++;
                if (nextEnemyIndex > SPRITE_INDEX_ENEMY_END) {
                    nextEnemyIndex = SPRITE_INDEX_ENEMY_START;
                }
            }
        }
    }

    /*
     *  
     */
    private void updateChildSprites() {
        for (int c = SPRITE_INDEX_CHILD_START; c <= SPRITE_INDEX_CHILD_END; c++) {
            if (spriteVisible[c]) {
                updateSprite(c);
                switch (animIndex[c]) {
                    case ANIM_INDEX_NIÑO_STAND:
                        if (intersects(SPRITE_INDEX_PLAYER, c)) {
                            playAnim(c, ANIM_INDEX_NIÑO_STAND_UP);
                            playerScore += 500;
                            showBonus(spritePx[c] + spriteCw[c] / 2,
                                    spritePy[c]);
                        }
                        break;
                    case ANIM_INDEX_NIÑO_STAND_UP:
                    case ANIM_INDEX_NIÑO_JUMP:
                        if (isAnimFinished(c)) {
                            playAnim(c, ANIM_INDEX_NIÑO_WALK);
                        }
                        break;
                    case ANIM_INDEX_NIÑO_WALK:
                        if (spriteX[c] < spriteX[SPRITE_INDEX_PLAYER]
                                - displayWidth * 2) {
                            spriteVisible[c] = false;
                        }
                        if (!isOnPlatform(c, -8, 0)) {
                            playAnim(c, ANIM_INDEX_NIÑO_JUMP);
                        }
                        if (CHEATS && Cheats.isCheatEnabled(Cheats.CHEAT_GORE)) {
                            genSpriteHitFx(c, false);
                        }
                        break;
                }
            }
        }
    }

    /*
     *  
     */
    private void resetPlayer() {
        spriteVisible[SPRITE_INDEX_PLAYER] = true;
        spriteFacing[SPRITE_INDEX_PLAYER] = SPRITE_FACING_RIGHT;
        if (advanceLevel || mapSectionIndex < 1) {
            spriteX[SPRITE_INDEX_PLAYER] = playerStartX[mapIndex];
            spriteY[SPRITE_INDEX_PLAYER] = playerStartY[mapIndex];
        } else {
            spriteX[SPRITE_INDEX_PLAYER] = mapSection[mapIndex][mapSectionIndex - 1][0]
                    + tileWidth;
            spriteY[SPRITE_INDEX_PLAYER] = mapSection[mapIndex][mapSectionIndex][1]
                    - tileHeight * 2;
            if (spriteY[SPRITE_INDEX_PLAYER] > (mapRows[mapIndex]
                    - mapFloorHeight[mapIndex] - 3)
                    * tileHeight) {
                while (!isOnPlatform(SPRITE_INDEX_PLAYER)) {
                    spriteY[SPRITE_INDEX_PLAYER] -= tileHeight;
                }
            }
        }
        playAnim(SPRITE_INDEX_PLAYER, ANIM_INDEX_PLAYER_STAND);
        spriteEnergy[SPRITE_INDEX_PLAYER] = 100;
        cameraX = spriteX[SPRITE_INDEX_PLAYER];
        cameraY = spriteY[SPRITE_INDEX_PLAYER];
        updateSprite(SPRITE_INDEX_PLAYER);
        playerUpDownPresses = 0;
    }

    //  --------------------
    // - Quit State -
    //--------------------

    /*
     * Quit State Logic
     */
    private void quitStateLogic() {
        audio.stopAudio();
        finished = true;
    }

    /**
     * 
     * @param s1
     * @param s2
     * @return
     */
    public boolean intersects(int s1, int s2) {
        int x1 = spritePx[s1];
        int w1 = spriteCw[s1] / 2;
        if (spriteFacing[s1] == SPRITE_FACING_RIGHT) {
            x1 += w1;
        }
        return intersects(x1, spritePy[s1], w1, spriteCh[s1], spritePx[s2] + 4,
                spritePy[s2], spriteCw[s2] - 8, spriteCh[s2]);
    }

    /**
     * 
     * @param s1
     * @param s2
     * @param s1xp
     * @return
     */
    public boolean intersects(int s1, int s2, int s1xp) {
        return intersects(spritePx[s1] - s1xp, spritePy[s1], spriteCw[s1]
                + s1xp * 2, spriteCh[s1], spritePx[s2], spritePy[s2],
                spriteCw[s2], spriteCh[s2]);
    }

    /**
     * 
     * @param x1
     * @param y1
     * @param xs1
     * @param ys1
     * @param x2
     * @param y2
     * @param xs2
     * @param ys2
     * @return
     */
    public boolean intersects(int x1, int y1, int xs1, int ys1, int x2, int y2,
            int xs2, int ys2) {
        return !((x2 > x1 + xs1) || (x2 + xs2 < x1) || (y2 > y1 + ys1) || (y2
                + ys2 < y1));
    }

    /**
     * 
     *  
     */
    public void vibraHit() {
        audio.startVibration(70 + random.nextInt() % 30, 50);
    }

    /**
     * 
     *  
     */
    public void vibraDead() {
        audio.startVibration(90 + random.nextInt() % 10, 100);
    }

    //******************************************************************************
    // Gfx
    //    
    //    
    //******************************************************************************

    public static final int IMAGE_TITLE = 0;

    public static final int IMAGE_SOFTLABELS = 1;

    public static final int IMAGE_MENU_ARROWS = 2;

    public static final int IMAGE_SCROLL_UD_ARROWS = 3;

    public int displayWidth;

    public int displayHeight;

    protected int fontHeight;

    public boolean customSoftKeyHandling;

    /**
     * 
     * @throws Exception
     */
    protected abstract void initGfx() throws Exception;

    /**
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     * @param color
     */
    protected abstract void fillRect(int x, int y, int w, int h, int color);

    //--------------------------------------------------------------------------

    public boolean showMenuArrows;

    public int menuArrowsRate = 4;

    public int menuArrowPressed;

    public int menuTextColor = 0xFFFFFF;

    public int menuOutlineColor = 0;

    /**
     * 
     *  
     */
    protected abstract void drawMenuArrows();

    //--------------------------------------------------------------------------

    public int softLabelWidth = 24;

    public int softLabelHeight = 8;

    public boolean showSoftArrow;

    public int softArrowBlinkRate = 8;

    public int[] softLabelX = new int[2];

    public int[] softLabelY = new int[2];

    public int[] softLabelSelected = new int[2];

    public static final int SOFT_LABEL_NONE = 0;

    public static final int SOFT_LABEL_SELECT = 1;

    public static final int SOFT_LABEL_MENU = 2;

    public static final int SOFT_LABEL_PAUSE = 3;

    public static final int SOFT_LABEL_QUIT = 4;

    /**
     * 
     *  
     */
    protected abstract void drawSoftLabels();

    /**
     * 
     * @param index
     * @param label
     */
    protected abstract void setSoftLabel(int index, int label);

    //--------------------------------------------------------------------------

    protected abstract void drawIntro();

    //--------------------------------------------------------------------------

    public static final int MENU_TEXT_NEW_GAME = 0;

    public static final int MENU_TEXT_CONTINUE_GAME = 1;

    public static final int MENU_TEXT_SOUND_ON = 2;

    public static final int MENU_TEXT_SOUND_OFF = 3;

    public static final int MENU_TEXT_VIBRA_ON = 4;

    public static final int MENU_TEXT_VIBRA_OFF = 5;

    public static final int MENU_TEXT_INSTRUCTIONS = 6;

    public static final int MENU_TEXT_ABOUT = 7;

    public static final int MENU_TEXT_QUIT_APP = 8;

    public static final int MENU_TEXT_QUIT_GAME = 9;

    /**
     * 
     *  
     */
    protected abstract void initTitle();

    /**
     * 
     *  
     */
    protected abstract void finishTitle();

    public String[] hiScoreText;

    public int hiScore;

    public int hiLevel;

    public String hiName;

    public String hiScoreStr;

    public String[] hiScoreChars;

    /**
     * 
     *  
     */
    protected abstract void drawTitle();

    public String[] menuOptions;

    public String[] menuText;

    /**
     * 
     * @param mo
     */
    protected abstract void drawMenuOption(int mo);

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#setAudioOptionOn(boolean)
     */
    public void setAudioOptionOn(boolean on) {
        menuOptions[Game.MENU_OPTION_AUDIO] = menuText[on ? MENU_TEXT_SOUND_ON
                : MENU_TEXT_SOUND_OFF];

    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#setVibraOptionOn(boolean)
     */
    public void setVibraOptionOn(boolean on) {
        menuOptions[Game.MENU_OPTION_VIBRA] = menuText[on ? MENU_TEXT_VIBRA_ON
                : MENU_TEXT_VIBRA_OFF];

    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#setPauseMenuMode(boolean)
     */
    public void setMainMenuMode(boolean on) {
        menuOptions[Game.MENU_OPTION_GAME] = menuText[on
                ? MENU_TEXT_CONTINUE_GAME : MENU_TEXT_NEW_GAME];
        menuOptions[Game.MENU_OPTION_QUIT] = menuText[on ? MENU_TEXT_QUIT_GAME
                : MENU_TEXT_QUIT_APP];

    }

    //--------------------------------------------------------------------------

    public String[] instructionsText;

    public int insLine;

    public int insVisLines;

    public boolean showScrollUD;

    public int scrollUDRate = 4;

    public int scrollArrowPressed;

    public boolean showScrollUp;

    public boolean showScrollDown;

    protected abstract void drawInstructionsPage();

    protected abstract void drawTextPage(String[] txt, int vl, int cl);

    protected abstract void drawScrollUpDown();

    //--------------------------------------------------------------------------

    public String[] aboutText;

    public int aboutLine;

    public int aboutVisLines;

    /**
     * 
     *  
     */
    protected abstract void drawAboutPage();

    //--------------------------------------------------------------------------
    // sprites (logic)
    //--------------------------------------------------------------------------
    public static final int SPRITE_INDEX_PLAYER = 0;

    //
    public static final int SPRITE_FACING_LEFT = 1;

    public static final int SPRITE_FACING_RIGHT = 0;

    //    0 -> bajar_plataforma
    //    1 -> especial
    //    2 -> morir
    //    3 -> salto
    //    4 -> salto_corto
    //    5 -> caer
    //    6 -> salto_vertical
    //    7 -> quietoparao
    //    8 -> segada
    //    9 -> pu�etazo
    //    10 -> codazo atras
    //    11 -> patada
    //    12 -> correr
    //    13 -> combo7
    //    14 -> combo6
    //    15 -> combo5
    //    16 -> combo4
    //    17 -> combo3
    //    18 -> combo2
    //    19 -> recibir
    //    20 -> combo1
    //    21 -> pu�etazo3
    //    22 -> pu�etazo2
    //    23 -> patada4
    //    24 -> patada3
    //    25 -> patada2

    public static final int ANIM_INDEX_PLAYER_JUMP_DOWN = 0;

    public static final int ANIM_INDEX_PLAYER_JUMP_UP = 6;

    public static final int ANIM_INDEX_PLAYER_JUMP_UP_SHORT = 4;

    public static final int ANIM_INDEX_PLAYER_STAND = 7;

    public static final int ANIM_INDEX_PLAYER_HIT = 19;

    public static final int ANIM_INDEX_PLAYER_WALK = 12;

    public static final int ANIM_INDEX_PLAYER_JUMP = 3;

    public static final int ANIM_INDEX_PLAYER_FALL = 5;

    public static final int ANIM_INDEX_PLAYER_DIE = 2;

    public static final int ANIM_INDEX_PLAYER_COMBO1 = 20;

    public static final int ANIM_INDEX_PLAYER_COMBO2 = 18;

    public static final int ANIM_INDEX_PLAYER_COMBO3 = 17;

    public static final int ANIM_INDEX_PLAYER_COMBO4 = 16;

    public static final int ANIM_INDEX_PLAYER_COMBO5 = 15;

    public static final int ANIM_INDEX_PLAYER_COMBO6 = 14;

    public static final int ANIM_INDEX_PLAYER_COMBO7 = 13;

    public static final int[] ANIM_INDEX_PLAYER_COMBO_SEQ = {
            ANIM_INDEX_PLAYER_COMBO1, ANIM_INDEX_PLAYER_COMBO2,
            ANIM_INDEX_PLAYER_COMBO3, ANIM_INDEX_PLAYER_COMBO4,
            ANIM_INDEX_PLAYER_COMBO5, ANIM_INDEX_PLAYER_COMBO6,
            ANIM_INDEX_PLAYER_COMBO7 };

    public static final int ANIM_INDEX_PLAYER_ATTACKBACK = 10;

    public static final int ANIM_INDEX_PLAYER_SPECIAL = 1;

    public static final int ANIM_INDEX_PLAYER_SWEEP = 8;

    public static final int ANIM_INDEX_PLAYER_PUNCH1 = 9;

    public static final int ANIM_INDEX_PLAYER_PUNCH2 = 21;

    public static final int ANIM_INDEX_PLAYER_PUNCH3 = 22;

    public static final int[] ANIM_INDEX_PLAYER_PUNCH_SEQ = {
            ANIM_INDEX_PLAYER_PUNCH1, ANIM_INDEX_PLAYER_PUNCH2,
            ANIM_INDEX_PLAYER_PUNCH3 };

    public static final int ANIM_INDEX_PLAYER_KICK1 = 11;

    public static final int ANIM_INDEX_PLAYER_KICK2 = 25;

    public static final int ANIM_INDEX_PLAYER_KICK3 = 24;

    public static final int ANIM_INDEX_PLAYER_KICK4 = 23;

    public static final int[] ANIM_INDEX_PLAYER_KICK_SEQ = {
            ANIM_INDEX_PLAYER_KICK1, ANIM_INDEX_PLAYER_KICK2,
            ANIM_INDEX_PLAYER_KICK3, ANIM_INDEX_PLAYER_KICK4 };

    public static final int[] ANIM_INDEX_PLAYER_RANDOM_ATTACK_SIMPLE = {
            ANIM_INDEX_PLAYER_PUNCH1, ANIM_INDEX_PLAYER_PUNCH2,
            ANIM_INDEX_PLAYER_PUNCH3, ANIM_INDEX_PLAYER_KICK1,
            ANIM_INDEX_PLAYER_KICK2, ANIM_INDEX_PLAYER_KICK3,
            ANIM_INDEX_PLAYER_KICK4 };

    //
    // ABUELO
    public static final int ANIM_INDEX_ABUELO_WALK = 1;

    public static final int ANIM_INDEX_ABUELO_DIE = 0;

    // CHEMA
    //    0 -> salto_vertical
    //    1 -> caer
    //    2 -> ostia3
    //    3 -> recibir
    //    4 -> ostia2
    //    5 -> ostia1
    //    6 -> morir
    //    7 -> quieto
    //    8 -> andar
    //    9 -> salto

    public static final int ANIM_INDEX_CHEMA_WALK = 8;

    public static final int ANIM_INDEX_CHEMA_HIT = 3;

    public static final int ANIM_INDEX_CHEMA_FALL = 1;

    public static final int ANIM_INDEX_CHEMA_STAND = 7;

    public static final int ANIM_INDEX_CHEMA_JUMP = 9;

    public static final int ANIM_INDEX_CHEMA_VERTICAL_JUMP = 0;

    public static final int ANIM_INDEX_CHEMA_DIE = 6;

    public static final int ANIM_INDEX_CHEMA_ATTACK1 = 5;

    public static final int ANIM_INDEX_CHEMA_ATTACK2 = 4;

    public static final int ANIM_INDEX_CHEMA_ATTACK3 = 2;

    public static final int ANIM_INDEX_CHEMA_ATTACKS[] = {
            ANIM_INDEX_CHEMA_ATTACK1, ANIM_INDEX_CHEMA_ATTACK2,
            ANIM_INDEX_CHEMA_ATTACK3 };

    // JULIAN
    //    0 -> caer
    //    1 -> saltovertical
    //    2 -> torta4
    //    3 -> torta3
    //    4 -> torta2
    //    5 -> torta1
    //    6 -> recibir
    //    7 -> segada
    //    8 -> correr
    //    9 -> morir
    //    10 -> quieto
    //    11 -> salto
    //

    public static final int ANIM_INDEX_JULIAN_WALK = 7;

    public static final int ANIM_INDEX_JULIAN_FALL = 0;

    public static final int ANIM_INDEX_JULIAN_HIT = 6;

    public static final int ANIM_INDEX_JULIAN_STAND = 9;

    public static final int ANIM_INDEX_JULIAN_DIE = 10;

    public static final int ANIM_INDEX_JULIAN_VERTICAL_JUMP = 1;

    public static final int ANIM_INDEX_JULIAN_JUMP = 11;

    public static final int ANIM_INDEX_JULIAN_ATTACK1 = 5;

    public static final int ANIM_INDEX_JULIAN_ATTACK2 = 4;

    public static final int ANIM_INDEX_JULIAN_ATTACK3 = 3;

    public static final int ANIM_INDEX_JULIAN_ATTACK4 = 2;

    public static final int ANIM_INDEX_JULIAN_SWEEP = 8;

    public static final int ANIM_INDEX_JULIAN_ATTACKS[] = {
            ANIM_INDEX_JULIAN_ATTACK1, ANIM_INDEX_JULIAN_ATTACK2,
            ANIM_INDEX_JULIAN_ATTACK3, ANIM_INDEX_JULIAN_ATTACK4,
            ANIM_INDEX_JULIAN_SWEEP };

    // MANOLITO
    //    0 -> salto_vertical
    //    1 -> caer
    //    2 -> punetazo3
    //    3 -> punetazo2
    //    4 -> punetazo1
    //    5 -> recibir
    //    6 -> morir
    //    7 -> quieto
    //    8 -> andar
    //    9 -> salto

    public static final int ANIM_INDEX_MANOLITO_WALK = 8;

    public static final int ANIM_INDEX_MANOLITO_FALL = 1;

    public static final int ANIM_INDEX_MANOLITO_HIT = 5;

    public static final int ANIM_INDEX_MANOLITO_STAND = 7;

    public static final int ANIM_INDEX_MANOLITO_JUMP = 9;

    public static final int ANIM_INDEX_MANOLITO_VERTICAL_JUMP = 0;

    public static final int ANIM_INDEX_MANOLITO_DIE = 6;

    public static final int ANIM_INDEX_MANOLITO_ATTACK1 = 4;

    public static final int ANIM_INDEX_MANOLITO_ATTACK2 = 3;

    public static final int ANIM_INDEX_MANOLITO_ATTACK3 = 2;

    public static final int ANIM_INDEX_MANOLITO_ATTACKS[] = {
            ANIM_INDEX_MANOLITO_ATTACK1, ANIM_INDEX_MANOLITO_ATTACK2,
            ANIM_INDEX_MANOLITO_ATTACK3 };

    // MARIANO
    //    0 -> torata1
    //    1 -> salto_vertical
    //    2 -> caer
    //    3 -> parao
    //    4 -> torta3
    //    5 -> torta2
    //    6 -> recibir
    //    7 -> morir
    //    8 -> andar
    //    9 -> salto
    //    10 -> torata4

    public static final int ANIM_INDEX_MARIANO_JUMP = 9;

    public static final int ANIM_INDEX_MARIANO_FALL = 2;

    public static final int ANIM_INDEX_MARIANO_VERTICAL_JUMP = 1;

    public static final int ANIM_INDEX_MARIANO_WALK = 8;

    public static final int ANIM_INDEX_MARIANO_STAND = 3;

    public static final int ANIM_INDEX_MARIANO_HIT = 6;

    public static final int ANIM_INDEX_MARIANO_DIE = 7;

    public static final int ANIM_INDEX_MARIANO_ATTACK1 = 0;

    public static final int ANIM_INDEX_MARIANO_ATTACK2 = 4;

    public static final int ANIM_INDEX_MARIANO_ATTACK3 = 5;

    public static final int ANIM_INDEX_MARIANO_ATTACK4 = 10;

    public static final int ANIM_INDEX_MARIANO_ATTACKS[] = {
            ANIM_INDEX_MARIANO_ATTACK1, ANIM_INDEX_MARIANO_ATTACK2,
            ANIM_INDEX_MARIANO_ATTACK3, ANIM_INDEX_MARIANO_ATTACK4 };

    // NI�O
    //  0 -> correr
    //  1 -> arriba
    //  2 -> quieto
    //  3 -> salto
    public static final int ANIM_INDEX_NIÑO_WALK = 0;

    public static final int ANIM_INDEX_NIÑO_STAND_UP = 1;

    public static final int ANIM_INDEX_NIÑO_STAND = 2;

    public static final int ANIM_INDEX_NIÑO_JUMP = 3;

    //    PACO
    //    0 -> salto_vertical
    //    1 -> caer
    //    2 -> torta4
    //    3 -> ostia3
    //    4 -> recibir
    //    5 -> ostia2
    //    6 -> ostia1
    //    7 -> morir
    //    8 -> quieto
    //    9 -> andar
    //    10 -> salto

    public static final int ANIM_INDEX_PACO_WALK = 9;

    public static final int ANIM_INDEX_PACO_STAND = 8;

    public static final int ANIM_INDEX_PACO_FALL = 1;

    public static final int ANIM_INDEX_PACO_HIT = 4;

    public static final int ANIM_INDEX_PACO_DIE = 7;

    public static final int ANIM_INDEX_PACO_JUMP = 10;

    public static final int ANIM_INDEX_PACO_VERTICAL_JUMP = 0;

    public static final int ANIM_INDEX_PACO_ATTACK1 = 6;

    public static final int ANIM_INDEX_PACO_ATTACK2 = 5;

    public static final int ANIM_INDEX_PACO_ATTACK3 = 3;

    public static final int ANIM_INDEX_PACO_ATTACK4 = 2;

    public static final int ANIM_INDEX_PACO_ATTACKS[] = {
            ANIM_INDEX_PACO_ATTACK1, ANIM_INDEX_PACO_ATTACK2,
            ANIM_INDEX_PACO_ATTACK3, ANIM_INDEX_PACO_ATTACK4 };

    //    TOMAS
    //    0 -> golpe1
    //    1 -> quieto
    //    2 -> morir
    //    3 -> recibir
    //    4 -> andar
    //    5 -> golpe2

    public static final int ANIM_INDEX_TOMAS_WALK = 4;

    public static final int ANIM_INDEX_TOMAS_DIE = 2;

    public static final int ANIM_INDEX_TOMAS_STAND = 1;

    public static final int ANIM_INDEX_TOMAS_HIT = 3;

    public static final int ANIM_INDEX_TOMAS_ATTACK1 = 0;

    public static final int ANIM_INDEX_TOMAS_ATTACK2 = 5;

    public static final int ANIM_INDEX_TOMAS_ATTACKS[] = {
            ANIM_INDEX_TOMAS_ATTACK1, ANIM_INDEX_TOMAS_ATTACK2 };

    //
    public static final int ANIM_INDEX_ENEMY_ABUELO = 0;

    public static final int ANIM_INDEX_ENEMY_CHEMA = 1;

    public static final int ANIM_INDEX_ENEMY_JULIAN = 2;

    public static final int ANIM_INDEX_ENEMY_MANOLITO = 3;

    public static final int ANIM_INDEX_ENEMY_MARIANO = 4;

    public static final int ANIM_INDEX_NIÑO = 5;

    public static final int ANIM_INDEX_ENEMY_PACO = 6;

    public static final int ANIM_INDEX_ENEMY_TOMAS = 7;

    public static final int[] BOSS_ON_LEVEL = { -1, ANIM_INDEX_ENEMY_MANOLITO,
            ANIM_INDEX_ENEMY_JULIAN, ANIM_INDEX_ENEMY_PACO,
            ANIM_INDEX_ENEMY_MARIANO, ANIM_INDEX_ENEMY_TOMAS };

    public static final int[][] ENEMIES_ON_LEVEL = {
            { ANIM_INDEX_ENEMY_CHEMA },
            { ANIM_INDEX_ENEMY_CHEMA },
            { ANIM_INDEX_ENEMY_CHEMA, ANIM_INDEX_ENEMY_CHEMA,
                    ANIM_INDEX_ENEMY_MANOLITO },
            { ANIM_INDEX_ENEMY_CHEMA, ANIM_INDEX_ENEMY_MANOLITO,
                    ANIM_INDEX_ENEMY_JULIAN },
            { ANIM_INDEX_ENEMY_PACO, ANIM_INDEX_ENEMY_PACO,
                    ANIM_INDEX_ENEMY_CHEMA, ANIM_INDEX_ENEMY_MANOLITO,
                    ANIM_INDEX_ENEMY_JULIAN }, { ANIM_INDEX_ENEMY_ABUELO } };

    public static final int ANIM_INDEX_ENEMY_WALK[] = { ANIM_INDEX_ABUELO_WALK,
            ANIM_INDEX_CHEMA_WALK, ANIM_INDEX_JULIAN_WALK,
            ANIM_INDEX_MANOLITO_WALK, ANIM_INDEX_MARIANO_WALK,
            ANIM_INDEX_NIÑO_WALK, ANIM_INDEX_PACO_WALK, ANIM_INDEX_TOMAS_WALK };

    public static final int ANIM_INDEX_ENEMY_JUMP[][] = {
            { ANIM_INDEX_ABUELO_WALK, ANIM_INDEX_ABUELO_WALK },
            { ANIM_INDEX_CHEMA_JUMP, ANIM_INDEX_CHEMA_VERTICAL_JUMP },
            { ANIM_INDEX_JULIAN_JUMP, ANIM_INDEX_JULIAN_VERTICAL_JUMP },
            { ANIM_INDEX_MANOLITO_JUMP, ANIM_INDEX_MANOLITO_VERTICAL_JUMP },
            { ANIM_INDEX_MARIANO_JUMP, ANIM_INDEX_MARIANO_VERTICAL_JUMP },
            { ANIM_INDEX_NIÑO_JUMP, ANIM_INDEX_NIÑO_JUMP },
            { ANIM_INDEX_PACO_JUMP, ANIM_INDEX_PACO_VERTICAL_JUMP },
            { ANIM_INDEX_TOMAS_WALK, ANIM_INDEX_TOMAS_WALK } };

    public static final int ANIM_INDEX_ENEMY_FALL[] = { ANIM_INDEX_ABUELO_WALK,
            ANIM_INDEX_CHEMA_FALL, ANIM_INDEX_JULIAN_FALL,
            ANIM_INDEX_MANOLITO_FALL, ANIM_INDEX_MARIANO_FALL,
            ANIM_INDEX_NIÑO_STAND, ANIM_INDEX_PACO_FALL, ANIM_INDEX_TOMAS_STAND };

    public static final int ANIM_INDEX_ENEMY_STAND[] = {
            ANIM_INDEX_ABUELO_WALK, ANIM_INDEX_CHEMA_STAND,
            ANIM_INDEX_JULIAN_STAND, ANIM_INDEX_MANOLITO_STAND,
            ANIM_INDEX_MARIANO_STAND, ANIM_INDEX_NIÑO_STAND,
            ANIM_INDEX_PACO_STAND, ANIM_INDEX_TOMAS_STAND };

    public static final int ANIM_INDEX_ENEMY_HIT[] = { ANIM_INDEX_ABUELO_DIE,
            ANIM_INDEX_CHEMA_HIT, ANIM_INDEX_JULIAN_HIT,
            ANIM_INDEX_MANOLITO_HIT, ANIM_INDEX_MARIANO_HIT,
            ANIM_INDEX_NIÑO_STAND_UP, ANIM_INDEX_PACO_HIT, ANIM_INDEX_TOMAS_HIT };

    public static final int ANIM_INDEX_ENEMY_DIE[] = { ANIM_INDEX_ABUELO_DIE,
            ANIM_INDEX_CHEMA_DIE, ANIM_INDEX_JULIAN_DIE,
            ANIM_INDEX_MANOLITO_DIE, ANIM_INDEX_MARIANO_DIE,
            ANIM_INDEX_NIÑO_STAND_UP, ANIM_INDEX_PACO_DIE, ANIM_INDEX_TOMAS_DIE };

    public static final int ANIM_INDEX_ENEMY_ATTACK[][] = {
            { ANIM_INDEX_ABUELO_WALK }, ANIM_INDEX_CHEMA_ATTACKS,
            ANIM_INDEX_JULIAN_ATTACKS, ANIM_INDEX_MANOLITO_ATTACKS,
            ANIM_INDEX_MARIANO_ATTACKS, { ANIM_INDEX_NIÑO_STAND },
            ANIM_INDEX_PACO_ATTACKS, ANIM_INDEX_TOMAS_ATTACKS };

    //
    public static final int SPRITE_CELLS_PLAYER = 0;

    public static final int SPRITE_CELLS_ABUELO = 1;

    public static final int SPRITE_CELLS_CHEMA = 2;

    public static final int SPRITE_CELLS_JULIAN = 3;

    public static final int SPRITE_CELLS_MANOLITO = 4;

    public static final int SPRITE_CELLS_MARIANO = 5;

    public static final int SPRITE_CELLS_NENE = 6;

    public static final int SPRITE_CELLS_PACO = 7;

    public static final int SPRITE_CELLS_TOMAS = 8;

    public static final int NUM_ANIM_BANKS = 9;

    public static final int NUM_SPRITES = 16;

    public static final int SPRITE_INDEX_ENEMY_START = 1;

    public static final int SPRITE_INDEX_ENEMY_END = 6;

    public static final int SPRITE_INDEX_BOSS = 7;

    public static final int SPRITE_INDEX_CHILD_START = 8;

    public static final int SPRITE_INDEX_CHILD_END = 15;

    public short[][][] rects = new short[NUM_ANIM_BANKS][][];

    public short[][][] gaps = new short[NUM_ANIM_BANKS][][];

    public short[][][] animCellIndex = new short[NUM_ANIM_BANKS][][];

    public short[] animCellIndexCounter = new short[NUM_SPRITES];

    public short[][][] animCellDelay = new short[NUM_ANIM_BANKS][][];

    public short[] animCellDelayCounter = new short[NUM_SPRITES];

    public short[][][] animCellOffsetX = new short[NUM_ANIM_BANKS][][];

    public short[][][] animCellOffsetY = new short[NUM_ANIM_BANKS][][];

    public short[][] animRepeats = new short[NUM_ANIM_BANKS][];

    public short[] animRepeatsCounter = new short[NUM_SPRITES];

    public int[] animIndex = new int[NUM_SPRITES];

    public boolean[] animFinished = new boolean[NUM_SPRITES];

    public int[] spriteX = new int[NUM_SPRITES];

    public int[] spriteY = new int[NUM_SPRITES];

    public int[] numAnims = new int[NUM_ANIM_BANKS];

    public int[] frameIndex = new int[NUM_SPRITES];

    public int[] cellIndex = new int[NUM_SPRITES];

    public int[] spriteCx = new int[NUM_SPRITES];

    public int[] spriteCy = new int[NUM_SPRITES];

    public int[] spriteCw = new int[NUM_SPRITES];

    public int[] spriteCh = new int[NUM_SPRITES];

    public int[] spritePx = new int[NUM_SPRITES];

    public int[] spritePy = new int[NUM_SPRITES];

    public int[] animBank = new int[NUM_SPRITES];

    public int[] spriteFacing = new int[NUM_SPRITES];

    public int[] spriteFallRate = new int[NUM_SPRITES];

    public int[] spriteBaseY = new int[NUM_SPRITES];

    public static final int SPRITE_FALL_RATE_INIT = 8;

    public static final int SPRITE_FALL_RATE_MAX = 24;

    public boolean[] spriteVisible = new boolean[NUM_SPRITES];

    public int spriteEnergy[] = new int[NUM_SPRITES];

    public int spriteType[] = new int[NUM_SPRITES];

    /**
     * 
     * @throws Exception
     */
    protected abstract void loadAnims() throws Exception;

    /**
     * 
     * @param sIndex
     * @param aIndex
     */
    public void playAnim(int sIndex, int aIndex) {
        animIndex[sIndex] = aIndex;
        animCellIndexCounter[sIndex] = 0;
        animCellDelayCounter[sIndex] = animCellDelay[animBank[sIndex]][aIndex][0];
        //System.out.println("PA: " + sIndex+", " +aIndex+",
        // "+animCellDelayCounter[sIndex]); 31 16
        animRepeatsCounter[sIndex] = animRepeats[animBank[sIndex]][aIndex];
        animFinished[sIndex] = false;
        updateAnimSpriteData(sIndex);
        int lastSpriteY = spriteY[sIndex];
        spriteY[sIndex] = spriteY[sIndex] - spriteY[sIndex] % tileHeight;
        if (spriteY[sIndex] - lastSpriteY < 0) {
            spriteY[sIndex] += tileHeight;
        }
        spriteBaseY[sIndex] = spriteY[sIndex];
    }

    /**
     * 
     * @param sIndex
     */
    public void stopAnim(int sIndex) {
        animFinished[sIndex] = true;
    }

    /**
     * 
     * @param sIndex
     */
    public void resumeAnim(int sIndex) {
        animFinished[sIndex] = false;
    }

    /**
     * 
     * @param sIndex
     * @return
     */
    public boolean isAnimFinished(int sIndex) {
        return animFinished[sIndex];
    }

    /**
     * 
     * @param sIndex
     * @return
     */
    public int updateSprite(int sIndex) {
        int deltaY = 0;
        if (!animFinished[sIndex]) {
            animCellDelayCounter[sIndex]--;
            //System.out.println(sIndex + ": " + animCellDelayCounter[sIndex]);
            if (animCellDelayCounter[sIndex] < 0) {
                if (animCellIndexCounter[sIndex] + 1 >= animCellIndex[animBank[sIndex]][animIndex[sIndex]].length) {
                    if (animRepeatsCounter[sIndex] != 0) {
                        animRepeatsCounter[sIndex]--;
                        if (sIndex == SPRITE_INDEX_PLAYER
                                && animIndex[sIndex] == ANIM_INDEX_PLAYER_WALK) {
                            animCellIndexCounter[sIndex] = 1;
                        } else {
                            animCellIndexCounter[sIndex] = 0;
                        }
                        animCellDelayCounter[sIndex] = animCellDelay[animBank[sIndex]][animIndex[sIndex]][0];
                    } else {
                        animCellIndexCounter[sIndex] = (short) (animCellIndex[animBank[sIndex]][animIndex[sIndex]].length - 1);
                        animFinished[sIndex] = true;
                    }
                } else {
                    animCellIndexCounter[sIndex]++;
                    animCellDelayCounter[sIndex] = animCellDelay[animBank[sIndex]][animIndex[sIndex]][animCellIndexCounter[sIndex]];
                }
                if (!animFinished[sIndex]) {
                    deltaY = updateAnimSpriteData(sIndex);
                }
            }
        }
        return deltaY;
    }

    /**
     * 
     * @param sIndex
     * @return
     */
    public int getFrameOffsetY(int sIndex) {
        return animCellOffsetY[animBank[sIndex]][animIndex[sIndex]][frameIndex[sIndex]];
    }

    /**
     * 
     * @param sIndex
     * @return
     */
    public int updateAnimSpriteData(int sIndex) {
        boolean swapDirection = false;
        int newDirection = spriteFacing[sIndex];
        frameIndex[sIndex] = animCellIndexCounter[sIndex];
        //        if (sIndex ==0) {
        //        System.out.println(frameIndex[sIndex]);
        //        }
        cellIndex[sIndex] = animCellIndex[animBank[sIndex]][animIndex[sIndex]][frameIndex[sIndex]];
        spriteCx[sIndex] = rects[animBank[sIndex]][cellIndex[sIndex]][0];
        spriteCy[sIndex] = rects[animBank[sIndex]][cellIndex[sIndex]][1];
        spriteCw[sIndex] = rects[animBank[sIndex]][cellIndex[sIndex]][2];
        spriteCh[sIndex] = rects[animBank[sIndex]][cellIndex[sIndex]][3];

        spriteY[sIndex] += animCellOffsetY[animBank[sIndex]][animIndex[sIndex]][frameIndex[sIndex]];
        spriteX[sIndex] += (spriteFacing[sIndex] == SPRITE_FACING_RIGHT ? 1
                : -1)
                * animCellOffsetX[animBank[sIndex]][animIndex[sIndex]][frameIndex[sIndex]];
        spritePx[sIndex] = spriteX[sIndex]
                + (spriteFacing[sIndex] == SPRITE_FACING_RIGHT
                        ? gaps[animBank[sIndex]][cellIndex[sIndex]][0]
                        : 32 - (gaps[animBank[sIndex]][cellIndex[sIndex]][0] + spriteCw[sIndex]));
        spritePy[sIndex] = spriteY[sIndex]
                + gaps[animBank[sIndex]][cellIndex[sIndex]][1];
        if (spriteType[sIndex] != ANIM_INDEX_NIÑO) {
            int offsetLeft = tileWidth * 4;
            int offsetRight = -tileWidth * 4;
            if (sIndex == SPRITE_INDEX_PLAYER) {
                offsetLeft = 40;
                offsetRight = 56;
            }
            if (spriteX[sIndex] < -8) {
                spriteX[sIndex] = -8;
                swapDirection = true;
                newDirection = SPRITE_FACING_RIGHT;
            }
            if (mapSectionTriggered[mapIndex][mapSectionIndex]
                    && mapSectionIndex > 0
                    && spriteX[sIndex] < mapSection[mapIndex][mapSectionIndex - 1][0]
                            - offsetLeft) {
                spriteX[sIndex] = mapSection[mapIndex][mapSectionIndex - 1][0]
                        - offsetLeft;
                swapDirection = true;
                newDirection = SPRITE_FACING_RIGHT;
            }
            if (!mapSectionTriggered[mapIndex][mapSectionIndex]
                    && mapSectionIndex > 1
                    && spriteX[sIndex] < mapSection[mapIndex][mapSectionIndex - 2][0]
                            - offsetLeft) {
                spriteX[sIndex] = mapSection[mapIndex][mapSectionIndex - 2][0]
                        - offsetLeft;
                swapDirection = true;
                newDirection = SPRITE_FACING_RIGHT;
            }
            if (spriteX[sIndex] > mapCols[mapIndex] * tileWidth - offsetRight) {
                spriteX[sIndex] = mapCols[mapIndex] * tileWidth - offsetRight;
                swapDirection = true;
                newDirection = SPRITE_FACING_LEFT;
            }
            if (spriteX[sIndex] > mapSection[mapIndex][mapSectionIndex][0]
                    - offsetRight) {
                spriteX[sIndex] = mapSection[mapIndex][mapSectionIndex][0]
                        - offsetRight;
                swapDirection = true;
                newDirection = SPRITE_FACING_LEFT;
            }
            if (swapDirection) {
                if (sIndex != SPRITE_INDEX_PLAYER) {
                    spriteFacing[sIndex] = newDirection;
                    enemyKey[sIndex] = (newDirection == SPRITE_FACING_LEFT
                            ? KEY_LEFT : KEY_RIGHT);
                } else if (animIndex[SPRITE_INDEX_PLAYER] == ANIM_INDEX_PLAYER_WALK) {
                    playAnim(SPRITE_INDEX_PLAYER, ANIM_INDEX_PLAYER_STAND);
                }
            }
        }
        return animCellOffsetY[animBank[sIndex]][animIndex[sIndex]][frameIndex[sIndex]];
    }

    public int[] isOnPlatformBypass = new int[NUM_SPRITES];

    /**
     * 
     * @param sIndex
     * @return
     */
    public boolean isOnPlatform(int sIndex) {
        return isOnPlatform(sIndex, 0, 0);
    }

    /**
     * 
     * @param sIndex
     * @param offsetX
     * @param offsetY
     * @return
     */
    public boolean isOnPlatform(int sIndex, int offsetX, int offsetY) {
        int tx = (offsetX + spriteX[sIndex] + 32) / tileWidth;
        int ty = ((playerAttack || playerSpecial
                || animIndex[SPRITE_INDEX_PLAYER] == ANIM_INDEX_PLAYER_DIE
                || animIndex[SPRITE_INDEX_PLAYER] == ANIM_INDEX_PLAYER_HIT
                || animIndex[SPRITE_INDEX_PLAYER] == ANIM_INDEX_PLAYER_WALK
                ? spriteBaseY[sIndex] : spriteY[sIndex])
                + offsetY + 32)
                / tileHeight + 1;
        return (tx >= 0 && ty >= 0 && ty < tileIndex[mapIndex].length
                && tx < tileIndex[mapIndex][ty].length && collisionIndex[mapIndex][ty][tx] == 5)
                && isOnPlatformBypass[sIndex] <= 0;
    }

    /**
     * 
     * @param sIndex
     * @param offsetX
     * @param offsetY
     * @return
     */
    public boolean isEnemyOnPlatform(int sIndex, int offsetX, int offsetY) {
        int tx = (offsetX + spriteX[sIndex] + 32) / tileWidth;
        int ty = ((animIndex[sIndex] == ANIM_INDEX_ENEMY_WALK[spriteType[sIndex]]
                ? spriteBaseY[sIndex] : spriteY[sIndex])
                + offsetY + 32)
                / tileHeight + 1;
        boolean ignore = enemyAttack[sIndex]; //(spriteType[sIndex] ==
        // ANIM_INDEX_ENEMY_JULIAN &&
        // enemyAttack[sIndex]);
        return ignore
                || spriteHit[sIndex]
                || ((tx >= 0 && ty >= 0 && ty < tileIndex[mapIndex].length
                        && tx < tileIndex[mapIndex][ty].length && collisionIndex[mapIndex][ty][tx] == 5) && isOnPlatformBypass[sIndex] <= 0);
    }

    //--------------------------------------------------------------------------
    // sprites (display)
    //--------------------------------------------------------------------------

    /**
     *  
     */
    public void drawSprites() {
        for (int c = 0; c < NUM_SPRITES; c++) {
            if (spriteVisible[c]) {
                drawSprite(c);
            }
        }
    }

    /**
     *  
     */
    protected abstract void drawSprite(int sIndex);

    //--------------------------------------------------------------------------
    // Scenery (logic)
    //--------------------------------------------------------------------------
    public static final int MAP_COUNT = 6;

    public int[] mapCols = new int[MAP_COUNT];

    public int[] mapRows = new int[MAP_COUNT];

    public byte[][][] tileIndex = new byte[MAP_COUNT][][];

    public byte[][][] collisionIndex = new byte[MAP_COUNT][][];

    public byte[][] combinedTiles;

    public int combiTileStartIndex;

    public int combiTileLayers;

    public int numCombiTiles;

    public int mapIndex;

    public int mapCol;

    public int mapRow;

    public int mapOffsetX;

    public int mapOffsetY;

    public int viewMapCols;

    public int viewMapRows;

    public int tileWidth = 16;

    public int tileHeight = 16;

    public int cameraX;

    public int cameraY;

    public int[] mapFloorHeight = { 2, 2, 2, 2, 2, 3 };

    public int[] playerStartX = new int[MAP_COUNT];

    public int[] playerStartY = new int[MAP_COUNT];

    public int[] bossStartX = new int[MAP_COUNT];

    public int[] bossStartY = new int[MAP_COUNT];

    public int[][] childStartX = new int[MAP_COUNT][];

    public int[][] childStartY = new int[MAP_COUNT][];

    public int[][][] mapSection = new int[MAP_COUNT][][];

    public boolean[][] mapSectionTriggered = new boolean[MAP_COUNT][];

    public int enemiesLeft;

    public int spawnsLeft;

    public int mapSectionIndex;

    /**
     *  
     */
    protected abstract void loadMaps() throws Exception;

    /**
     *  
     */
    public void updateMap() {
        int targetX = spriteX[SPRITE_INDEX_PLAYER] - displayWidth / 2
                + tileWidth;
        int steps = 4;
        if (shakeTime > 0) {
            cameraX += random.nextInt() % shakeTime;
        }
        if (cameraX < targetX) {
            cameraX += 1 + (targetX - cameraX) / steps;
        } else if (cameraX > targetX) {
            cameraX -= 1 + (cameraX - targetX) / steps;
        }
        if (cameraX < 0) {
            cameraX = 0;
        }
        if (mapIndex >= 1) {
            if (cameraX + displayWidth > bossStartX[mapIndex] - 32 && !bossMode) {
                spriteX[SPRITE_INDEX_BOSS] = bossStartX[mapIndex];
                spriteY[SPRITE_INDEX_BOSS] = bossStartY[mapIndex];
                spriteEnergy[SPRITE_INDEX_BOSS] = 100;
                spriteFacing[SPRITE_INDEX_BOSS] = SPRITE_FACING_LEFT;
                spriteType[SPRITE_INDEX_BOSS] = BOSS_ON_LEVEL[mapIndex];
                animBank[SPRITE_INDEX_BOSS] = spriteType[SPRITE_INDEX_BOSS] + 1;
                playAnim(SPRITE_INDEX_BOSS,
                        ANIM_INDEX_ENEMY_WALK[spriteType[SPRITE_INDEX_BOSS]]);
                spriteVisible[SPRITE_INDEX_BOSS] = true;
                bossMode = true;
                showGoIcon = false;
                lastEnemyHit = SPRITE_INDEX_BOSS;
                if (DEBUG) {
                    System.out.println("BOSS ACTIVE");
                }
                //mapSectionTriggered[mapIndex][mapSectionIndex] = true;
            }
        } else {
            //            System.out.println("CAMX: " + cameraX+" BOSSX: " +
            // bossStartX[mapIndex]);
            if (spriteX[SPRITE_INDEX_PLAYER] + displayWidth / 2 > /* bossStartX[mapIndex] */(mapCols[mapIndex] - 1)
                    * tileWidth
                    && !bossMode) {
                bossMode = true;
                enemiesLeft = 15;
                spawnsLeft = 15;
                showGoIcon = false;
                if (DEBUG) {
                    System.out.println("BOSS ACTIVE");
                }
                //mapSectionTriggered[mapIndex][mapSectionIndex] = true;
            }
        }
        if (cameraX > (mapCols[mapIndex] - viewMapCols) * tileWidth) {
            cameraX = (mapCols[mapIndex] - viewMapCols) * tileWidth;
        }
        if (mapSectionIndex < mapSection[mapIndex].length - 1
                && cameraX > (mapSection[mapIndex][mapSectionIndex][0]
                        - displayWidth - 32)) {
            cameraX = (mapSection[mapIndex][mapSectionIndex][0] - displayWidth - 32);
        }
        if (mapSectionIndex > 0
                && mapSectionTriggered[mapIndex][mapSectionIndex]
                && cameraX < (mapSection[mapIndex][mapSectionIndex - 1][0] - 32)) {
            cameraX = mapSection[mapIndex][mapSectionIndex - 1][0] - 32;
        }
        if (!mapSectionTriggered[mapIndex][mapSectionIndex]
                && mapSectionIndex > 1
                && cameraX < mapSection[mapIndex][mapSectionIndex - 2][0] - 32) {
            cameraX = mapSection[mapIndex][mapSectionIndex - 2][0] - 32;
        }
        if (!mapSectionTriggered[mapIndex][mapSectionIndex]
                && cameraX > (mapSection[mapIndex][mapSectionIndex][0]
                        - displayWidth - 64)) {
            enemiesLeft = 6;
            spawnsLeft = enemiesLeft;
            mapSectionTriggered[mapIndex][mapSectionIndex] = true;
            if (DEBUG) {
                System.out.println("Map section triggered: " + mapSectionIndex);
            }
        }
        mapCol = cameraX / tileWidth;
        if (mapCol < 0) {
            mapCol = 0;
            //mapOffsetX = 0;
        } else if (mapCol > mapCols[mapIndex] - viewMapCols) {
            mapCol = mapCols[mapIndex] - viewMapCols;
            //mapOffsetX = 0;
        } else {
            mapOffsetX = cameraX % tileWidth;
        }
        //
        int targetY = spriteY[SPRITE_INDEX_PLAYER] - displayHeight / 2;
        if (shakeTime > 0) {
            cameraY += random.nextInt() % shakeTime;
            shakeTime -= 2;
        }
        if (cameraY < targetY) {
            cameraY += 1 + (targetY - cameraY) / steps;
        } else if (cameraY > targetY) {
            cameraY -= 1 + (cameraY - targetY) / steps;
        }
        if (cameraY < 0) {
            cameraY = 0;
        } else if (cameraY > (mapRows[mapIndex] - viewMapRows) * tileHeight) {
            cameraY = (mapRows[mapIndex] - viewMapRows) * tileHeight;
        }
        mapRow = cameraY / tileHeight;
        if (mapRow < 0) {
            mapRow = 0;
        } else if (mapRow > mapRows[mapIndex] - viewMapRows) {
            mapRow = mapRows[mapIndex] - viewMapRows;
            if (mapRow < 0) {
                mapRow = 0;
            }
        } else {
            mapOffsetY = cameraY % tileHeight;
        }
    }

    //--------------------------------------------------------------------------
    // Scenery (display)
    //--------------------------------------------------------------------------

    public int shakeTime;

    /**
     * 
     * @param intensity
     */
    public void shakeScreen(int intensity) {
        shakeTime += intensity;
        if (shakeTime > 10) {
            shakeTime = 10;
        }
    }

    /**
     *  
     */
    protected abstract void drawMap();

    /**
     * 
     *  
     */
    protected abstract void drawSpecialBackground();

    /**
     * 
     *  
     */
    protected abstract void drawParticles();

    public int bonusX;

    public int bonusY;

    public int bonusVisible;

    /**
     * 
     * @param x
     * @param y
     */
    public void showBonus(int x, int y) {
        bonusX = x;
        bonusY = y;
        bonusVisible = 16;
    }

    /**
     * 
     *  
     */
    protected abstract void drawBonus();

    //--------------------------------------------------------------------------
    // Text util
    //--------------------------------------------------------------------------

    /**
     *  
     */
    protected abstract void drawOutlineText(String text, int foreColor,
            int backColor, int x, int y, int anchor);

    /**
     * 
     * @param text
     * @param foreColor
     * @param x
     * @param y
     * @param anchor
     */
    protected abstract void drawText(String text, int foreColor, int x, int y,
            int anchor);

    /**
     * 
     * @param file
     * @return
     * @throws Exception
     */
    protected abstract String[] readTextFile(String file) throws Exception;

    public int lastEnemyHit = -1;

    public int playerScore;

    public int playerTries;

    public int playerSpecials;

    public boolean showGoIcon;

    /**
     * 
     *  
     */
    protected abstract void drawScoreboard();

    /**
     * 
     *  
     */
    protected abstract void drawLogoMicro();

    /**
     * 
     *  
     */
    protected abstract void drawGameStart();

    /**
     * 
     *  
     */
    protected abstract void drawPlayerDead();

    /**
     * 
     *  
     */
    protected abstract void drawGameOver();

    /**
     * 
     *  
     */
    protected abstract void drawLevelCompleted();

    /**
     * 
     *  
     */
    protected abstract void drawGameCompleted();

    public int progress = 0;

    // HIGH SCORE

    int selectedCharIndex;
    String newHiName;

    /**
     *  
     */
    public void highScoreLogic() {
        switch (keyCode) {
            case KEY_RIGHT:
                selectedCharIndex++;
                if (selectedCharIndex >= hiScoreChars.length) {
                    selectedCharIndex = 0;
                }
                hiName = newHiName + hiScoreChars[selectedCharIndex];
                keyCode = KEY_NONE;
                break;
            case KEY_LEFT:
                selectedCharIndex--;
                if (selectedCharIndex < 0) {
                    selectedCharIndex = hiScoreChars.length - 1;
                }
                hiName = newHiName + hiScoreChars[selectedCharIndex];
                keyCode = KEY_NONE;
                break;
            case KEY_FIRE:
                newHiName += hiScoreChars[selectedCharIndex];
                if (newHiName.length() >= 8) {
                    hiName = newHiName;
                    saveData();
                    nextState = STATE_INTRO;
                } else {
                    hiName = newHiName + hiScoreChars[selectedCharIndex];
                }
                keyCode = KEY_NONE;
                break;
            case KEY_SOFT_1:
                hiName = newHiName;
                saveData();
                nextState = STATE_INTRO;
                keyCode = KEY_NONE;
                break;
        }
    }

    /**
     * 
     *  
     */
    public void highScoreTransition() {
        audio.stopAudio();
        selectedCharIndex = 0;
        setSoftLabel(1, SOFT_LABEL_NONE);
        setSoftLabel(0, SOFT_LABEL_SELECT);
        hiScore = playerScore;
        hiLevel = mapIndex + 1;
        hiScoreStr = hiScoreText[2] + " " + String.valueOf(hiScore) + " "
                + hiScoreText[1] + " " + String.valueOf(hiLevel);
        hiName = hiScoreChars[0];
        newHiName = "";
        keyCode = KEY_NONE;
        audio.playMsx(Audio.MSX_HIGHSCORE_THEME);
        keyPressed = false;
        try {
            Thread.sleep(50);
        } catch (Exception e) {

        }
    }

    /**
     * 
     *  
     */
    public abstract void drawHighScore();
}