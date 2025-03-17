package com.mygdx.mongojocs.thaiwarrior.genmidp;


/*
 * Created on 17-nov-2004
 * 
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.DataInputStream;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordEnumeration;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.thaiwarrior.common.Audio;
import com.mygdx.mongojocs.thaiwarrior.common.Game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.DataOutputStream;
import java.util.Vector;

/**
 * @author Administrador
 * 
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class StdGame extends Game {

    public static Graphics graphics;

    Image logoMicro;

    Image background;

    Image background2;

    Image background3;

    int pby;

    /**
     * 
     *  
     */
    public StdGame(Audio au) {
        super(au);
        fontHeight = Font.getDefaultFont().getHeight();
        try {
            logoMicro = new Image();
            logoMicro._createImage("/logo_micro.png");
            pby = -100;
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            Game.finished = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#loadImages()
     */
    public void initGfx() throws Exception {
        //DataInputStream dis;
        pby = (displayHeight + logoMicro.getHeight()) / 2 + 12;
        progress = 5;
        updateProgress();
        for (int c = 0; c < NUM_ANIM_BANKS; c++) {
            if (DEBUG) {
                System.out.println("ANIMBANK: " + c);
                System.out.println("CELLS: " + cellsFile[c][0] + ", "
                        + cellsFile[c][1]);
            }
            animCells[c][0] = Image.createImage(cellsFile[c][0]);
            updateProgress();
            animCells[c][1] = Image.createImage(cellsFile[c][1]);
            updateProgress();
        }
        tiles = Image.createImage(tilesFile[0]);
        updateProgress();
        softLabels = Image.createImage("/soft_labels.png");
        updateProgress();
        menuArrows = Image.createImage("/menu_arrows.png");
        updateProgress();
        for (int c = 0; c < 2; c++) {
            softLabelY[c] = displayHeight - softLabelHeight * 2;
        }
        softLabelX[0] = 0;
        softLabelX[1] = displayWidth - softLabelWidth - 2;
        softLabelSelected[0] = SOFT_LABEL_MENU;
        softLabelSelected[1] = SOFT_LABEL_SELECT;
        background = Image.createImage("/fondo.png");
        background2 = Image.createImage("/fondo2.png");
        background3 = Image.createImage("/fondo3.png");
        //
        menuText = readTextFile("/menu.txt");
        updateProgress();
        menuOptions = new String[] { menuText[MENU_TEXT_NEW_GAME],
                menuText[MENU_TEXT_SOUND_ON], menuText[MENU_TEXT_VIBRA_ON],
                menuText[MENU_TEXT_INSTRUCTIONS], menuText[MENU_TEXT_ABOUT],
                menuText[MENU_TEXT_QUIT_APP] };
        menuY = displayHeight / 2;
        logoThai = Image.createImage("/logo_thai.png");
        logoFighter = Image.createImage("/logo_warrior.png");
        updateProgress();
        motionBlur = Image.createImage("/motionblur.png");
        runLine = Image.createImage("/runline.png");
        introBaddies = Image.createImage("/tiposmalos.png");
        introPlayerPose1 = Image.createImage("/torsopose1.png");
        introPlayerPose2 = Image.createImage("/pose2.png");
        updateProgress();
        //
        scrollArrows = Image.createImage("/scroll_arrows.png");
        specialIcon = Image.createImage("/special.png");
        goIcon = Image.createImage("/adelanter.png");
        comboPowerBar = Image.createImage("/combo_power.png");
        comboLabel = Image.createImage("/combo_label.png");
        updateProgress();
        introPlayerLeftArm = Image.createImage("/brazoizqpose1.png");
        introPlayerRightArm1 = Image.createImage("/brazoderpose1.png");
        introPlayerRightArm2 = Image.createImage("/hombroderpose1.png");
        bonusLabel = Image.createImage("/bonus.png");
        instructionsText = readTextFile("/instrucciones.txt");
        updateProgress();
        insVisLines = (displayHeight - scrollArrows.getHeight()) / fontHeight
                - 1;
        //
        aboutText = readTextFile("/acerca_de.txt");
        hiScoreText = readTextFile("/max_puntua.txt");
        hiScoreChars = readTextFile("/max_pun_caracteres.txt");
        gameCompleteText = new String[][] { readTextFile("/final.txt"),
                readTextFile("/final2.txt") };
        hiScore = 1000;
        hiLevel = 1;
        hiScoreStr = hiScoreText[2] + " " + String.valueOf(hiScore) + " "
                + hiScoreText[1] + " " + String.valueOf(hiLevel);
        hiName = "TING";
        bossLabel = Image.createImage("/boss_label.png");
        updateProgress();
        aboutVisLines = (displayHeight - scrollArrows.getHeight()) / fontHeight
                - 1;
        //
        loadMaps();
        updateProgress();
        loadAnims();
        updateProgress();
        //
        setSoftLabel(0, SOFT_LABEL_NONE);
        setSoftLabel(1, SOFT_LABEL_NONE);
        //
        energyBar = Image.createImage("/energy_bar.png");
        playerTry = Image.createImage("/player_try.png");
        updateProgress();
        scoreNumbers = Image.createImage("/score_numbers.png");
        updateProgress();
        enemyIcon = new Image[enemyIconFile.length];
        for (int c = 0; c < enemyIcon.length; c++) {
            if (enemyIconFile[c] != null) {
                enemyIcon[c] = Image.createImage(enemyIconFile[c]);
            }
        }
        updateProgress();
        for (int c = 0; c < NUM_FIRE_PARTICLE; c++) {
            fireParticleCycleIndex[c] = -1;
        }
        inGameText = readTextFile("/jugando.txt");
        woodBar = Image.createImage("/woodbar.png");
        logoMicro = null;
        updateProgress();
        if (DEBUG) {
            System.out.println("\n\nGFX INIT COMPLETED.\n\n");
            System.out.println(progress);
        }
    }

    /*
     *  
     */
    private void updateProgress() {
        progress++;
        canvas.repaint();
        canvas.serviceRepaints();
        System.gc();
        try {
            Thread.sleep(10);
        } catch (Exception e) {

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#fillRect(int, int, int, int, int)
     */
    public void fillRect(int x, int y, int w, int h, int color) {
        graphics.setColor(color);
        graphics.fillRect(x, y, w, h);
    }

    Image logoThai;

    Image logoFighter;

    int logoThaiX;

    int logoThaiY;

    int logoFighterX;

    int logoFighterY;

    boolean flashBackground;

    int showHighScore;

    int hiBackHeight;

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#initTitle()
     */
    public void initTitle() {
        logoThaiX = -logoThai.getWidth() - 64;
        logoFighterX = displayWidth + logoFighter.getWidth() + 64;
        logoThaiY = 16;
        logoFighterY = displayHeight - 16 - logoFighter.getHeight();
        showHighScore = -1;
        hiBackHeight = 4;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#finishTitle()
     */
    public void finishTitle() {
        logoThaiX = (displayWidth - logoThai.getWidth()) / 2;
        logoFighterX = (displayWidth - logoFighter.getWidth()) / 2;
        flashBackground = false;
        showHighScore = -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawTitle()
     */
    public void drawTitle() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        if (flashBackground) {
            graphics.setColor(0xFFFFFF);
            graphics.fillRect(0, 0, displayWidth, displayHeight);
            flashBackground = false;
        } else {
            drawBackPattern();
            if (logoThaiX < (displayWidth - logoThai.getWidth()) / 2) {
                logoThaiX += 16;
                if (logoThaiX >= (displayWidth - logoThai.getWidth()) / 2) {
                    logoThaiX = (displayWidth - logoThai.getWidth()) / 2;
                    flashBackground = true;
                }
            }
            if (logoFighterX > (displayWidth - logoFighter.getWidth()) / 2) {
                logoFighterX -= 16;
                if (logoFighterX <= (displayWidth - logoFighter.getWidth()) / 2) {
                    logoFighterX = (displayWidth - logoFighter.getWidth()) / 2;
                    flashBackground = true;
                    showHighScore = 6;
                }
            }
            graphics.setClip(0, 0, displayWidth, displayHeight);
            graphics.drawImage(logoThai, logoThaiX, logoThaiY, ANCHOR);
            graphics.drawImage(logoFighter, logoFighterX, logoFighterY, ANCHOR);
            if (showHighScore > 0) {
                showHighScore--;
                if (showHighScore == 0) {
                    flashBackground = true;
                }
            } else if (showHighScore == 0 && !inGameMenu) {
                graphics.setClip(0, displayHeight / 2 - hiBackHeight / 2,
                        displayWidth, hiBackHeight);
                graphics.setColor(0x880000);
                graphics.fillRect(0, displayHeight / 2 - fontHeight * 2
                        - fontHeight / 2, displayWidth, fontHeight * 5);
                graphics.setColor(0xFFFFFF);
                graphics.drawString(hiScoreText[state == STATE_HIGH_SCORE?3:0], displayWidth / 2,
                        (displayHeight - fontHeight * 5) / 2 + 4, Graphics.TOP
                                | Graphics.HCENTER);
                graphics.drawString(hiName, displayWidth / 2,
                        (displayHeight - fontHeight) / 2, Graphics.TOP
                                | Graphics.HCENTER);
                graphics.drawString(hiScoreStr, displayWidth / 2,
                        (displayHeight + fontHeight * 4) / 2 - 8, Graphics.TOP
                                | Graphics.HCENTER);
                graphics.setClip(0, 0, displayWidth, displayHeight);
                for (int x = 0; x <= displayWidth; x += 16) {
                    graphics.drawImage(woodBar, x, displayHeight / 2
                            - hiBackHeight / 2 - 2, ANCHOR);
                    graphics.drawImage(woodBar, x, displayHeight / 2
                            + hiBackHeight / 2 - 1, ANCHOR);
                }
                if (hiBackHeight < fontHeight * 5) {
                    hiBackHeight += 4;
                }
            }
        }
        if (currentTick % 180 == 0 && state != STATE_HIGH_SCORE) {
            initTitle();
        }
    }

    /*
     *  
     */
    private void drawBackPattern() {
        graphics.drawImage(background, 0, 0, ANCHOR);
    }

    //--------------------
    // -Intro State -
    //--------------------

    Image introBaddies;

    Image introPlayerPose1;

    Image introPlayerPose2;

    Image introPlayerLeftArm;

    Image introPlayerRightArm1;

    Image introPlayerRightArm2;

    /*
     * Intro State Logic
     */
    public void introStateLogic() {
        if (currentTick >= 110 || keyPressed && keyCode == KEY_FIRE
                || keyCode == KEY_SOFT_1 || keyCode == KEY_SOFT_2) {
            nextState = STATE_TITLE;
        }
    }

    /*
     * Intro State transition
     */
    public void introStateTransition() {
        int dh = displayHeight / 2;
        audio.stopAudio();
        currentTick = -10;
        introBy = 48;
        introPax = -160;
        introPx = 32;
        introPy = 52;
        introRunMotionIndex = 0;
        for (int c = 0; c < INTRO_RUN_LINES; c++) {
            introLineX[c] = c * 24;
            introLineY[c] = dh + random.nextInt() % dh;
        }
        setSoftLabel(0, SOFT_LABEL_NONE);
        setSoftLabel(1, SOFT_LABEL_NONE);
        keyPressed = false;
        audio.playMsx(Audio.MSX_INTRO);
    }

    int introPx;

    int introPy;

    int introPax = 0;

    int introBlurX = 0;

    int[] introRunMotion = { 6, 2, 0 };

    int[] introRunMotionRa = { 4, 2, 0 };

    int introRunMotionIndex;

    int introBy = 48;

    Image runLine;

    Image motionBlur;

    static final int INTRO_RUN_LINES = 8;

    int[] introLineX = new int[INTRO_RUN_LINES];

    int[] introLineY = new int[INTRO_RUN_LINES];

    private void drawIntroRunningBackground() {
        //        int color = 0x00;
        //        int bh = (displayHeight - 96) / 32;
        //        for (int c = 0; c < 0x0F; c++) {
        //            graphics.setColor(color << 16);
        //            graphics.fillRect(0, 48 + c * bh, displayWidth, bh);
        //            graphics.fillRect(0, displayHeight - 48 - c * bh, displayWidth, bh);
        //            color += 0x11;
        //        }
        //        graphics.fillRect(0, 48 + bh * 0x0f, displayWidth, 25);
        for (int c = -introBlurX; c < displayWidth + introBlurX; c += motionBlur
                .getWidth()) {
            graphics.drawImage(motionBlur, c, 48, ANCHOR);
        }
        introBlurX += 24;
        if (introBlurX >= motionBlur.getWidth()) {
            introBlurX -= motionBlur.getWidth();
        }
        for (int c = 0; c < INTRO_RUN_LINES; c++) {
            introLineX[c] -= 48;
            if (introLineX[c] < -runLine.getWidth()) {
                int dh = displayHeight / 2;
                introLineX[c] = displayWidth;
                introLineY[c] = dh + random.nextInt() % dh;
            }
            graphics.drawImage(runLine, introLineX[c], introLineY[c], ANCHOR);
        }
        introPx++;
    }

    /*
     *  
     */
    private void drawIntroRunningPlayer() {
        int y = introPy + introRunMotion[introRunMotionIndex];
        int ay = y + (3 - introRunMotionIndex);
        graphics.drawImage(introPlayerLeftArm, introPx + 48, ay + 52, ANCHOR);
        graphics.drawImage(introPlayerPose1, introPx, y, ANCHOR);
        graphics.drawImage(introPlayerRightArm2, introPx - 21, ay + 38, ANCHOR);
        graphics.drawImage(introPlayerRightArm1, introPx - 36, ay + 52
                + introRunMotionRa[introRunMotionIndex], ANCHOR);
        introRunMotionIndex++;
        if (introRunMotionIndex >= introRunMotion.length) {
            introRunMotionIndex = 0;
        }
    }

    /*
     *  
     */
    private void drawIntroAttackingPlayer() {
        int y = introPy;// + (currentTick % 2 == 0 ? 4 : 0);
        introPax += 24;
        graphics.drawImage(introPlayerPose2, introPx + introPax, y, ANCHOR);
    }

    /*
     *  
     */
    private void drawIntroBaddies() {
        graphics.drawImage(introBaddies, 0, introBy, ANCHOR);
        introBy--;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Game#drawIntro()
     */
    public void drawIntro() {
        graphics.setColor(0);
        graphics.fillRect(0, 0, displayWidth, 48);
        graphics.fillRect(0, displayHeight - 48, displayWidth, 48);
        graphics.setClip(0, 48, displayWidth, displayHeight - 96);
        if (currentTick < 0) {
            graphics.fillRect(0, 48, displayWidth, displayHeight - 96);
        } else if (currentTick < 20) {
            drawIntroRunningBackground();
            drawIntroRunningPlayer();
        } else if (currentTick < 44) {
            drawIntroBaddies();
        } else if (currentTick < 60) {
            drawIntroRunningBackground();
            drawIntroRunningPlayer();
        } else if (currentTick < 84) {
            drawIntroBaddies();
        } else if (currentTick < 100) {
            drawIntroRunningBackground();
            drawIntroAttackingPlayer();
        } else if (currentTick < 109) {
            if (currentTick % 2 == 0) {
                graphics.setColor(0);
            } else {
                graphics.setColor(0xFFFFFF);
            }
            graphics.fillRect(0, 48, displayWidth, displayHeight - 96);
        }
    }

    Image menuArrows;

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawMenuLR()
     */
    public void drawMenuArrows() {
        graphics.setClip(0, menuY - menuArrows.getHeight() / 2, menuArrows
                .getWidth() / 2, menuArrows.getHeight() / 2);
        graphics.drawImage(menuArrows, 0, menuY - menuArrows.getHeight() / 2
                - ((menuArrowPressed == -1) ? menuArrows.getHeight() / 2 : 0),
                ANCHOR);
        graphics.setClip(displayWidth - menuArrows.getWidth() / 2, menuY
                - menuArrows.getHeight() / 2, menuArrows.getWidth() / 2,
                menuArrows.getHeight() / 2);
        graphics.drawImage(menuArrows, displayWidth - menuArrows.getWidth(),
                menuY
                        - menuArrows.getHeight()
                        / 2
                        - ((menuArrowPressed == 1) ? menuArrows.getHeight() / 2
                                : 0), ANCHOR);
    }

    Image softLabels;

    /*
     *  
     */
    public void drawSoftLabels() {
        if (customSoftKeyHandling) {
            if (Game.currentTick % softArrowBlinkRate == 0) {
                showSoftArrow = !showSoftArrow;
            }
            for (int c = 0; c < 2; c++) {
                if (softLabelSelected[c] > 0) {
                    graphics.setClip(softLabelX[c], softLabelY[c],
                            softLabelWidth, softLabelHeight);
                    graphics.drawImage(softLabels, softLabelX[c]
                            - softLabelSelected[c] * softLabelWidth,
                            softLabelY[c], ANCHOR);
                    if (showSoftArrow) {
                        graphics.setClip(softLabelX[c], softLabelY[c]
                                + softLabelHeight, softLabelWidth,
                                softLabelHeight);
                        graphics.drawImage(softLabels, softLabelX[c],
                                softLabelY[c] + softLabelHeight, ANCHOR);
                    }
                }
            }
        }
    }

    Command[] softCommand = new Command[2];

    String[] commandLabels = { " ", "Select", "Menu", "Pause", "Quit" };

    public static Canvas canvas;

    //CommandListener commandListener;

    public void setSoftLabel(int index, int label) {
        softLabelSelected[index] = label;
        if (!customSoftKeyHandling) {
            if (softCommand[index] != null) {
                canvas.removeCommand(softCommand[index]);
                softCommand[index] = null;
            }
            if (label != SOFT_LABEL_NONE) {
                softCommand[index] = new Command(commandLabels[label],
                        index == 0 ? Command.BACK : Command.OK, index);
                canvas.addCommand(softCommand[index]);

            }
        }
    }

    int menuY;

    /**
     *  
     */
    public void drawMenuOption(int index) {
        drawOutlineText(menuOptions[index], menuTextColor, menuOutlineColor,
                displayWidth / 2, menuY - fontHeight, Graphics.HCENTER
                        | Graphics.TOP);
        if (Game.CHEATS) {
            drawOutlineText("<cheats enabled>", 0xFFFF0000, 0xFFFFFF00,
                    displayWidth / 2, menuY + fontHeight, Graphics.HCENTER
                            | Graphics.TOP);
        }
    }

    /**
     *  
     */
    public void drawInstructionsPage() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        drawBackPattern();
        drawTextPage(instructionsText, insVisLines, insLine);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawTextPage(java.lang.String[],
     *      int, int)
     */
    public void drawTextPage(String[] txt, int vl, int cl) {
        int y = scrollArrows.getHeight() / 2;
        for (int c = 0; c < vl; c++) {
            drawText(txt[cl + c], 0, displayWidth / 2 + 1, y + 1,
                    Graphics.HCENTER | Graphics.TOP);
            drawText(txt[cl + c], menuTextColor, displayWidth / 2, y,
                    Graphics.HCENTER | Graphics.TOP);
            y += fontHeight;
        }
    }

    Image scrollArrows;

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawScrollUpDown()
     */
    public void drawScrollUpDown() {
        if (showScrollUp) {
            graphics.setClip(displayWidth / 2 - scrollArrows.getWidth() / 2, 0,
                    scrollArrows.getWidth() / 2, scrollArrows.getHeight() / 2);
            graphics.drawImage(scrollArrows, displayWidth
                    / 2
                    - (scrollArrowPressed == -1 ? scrollArrows.getWidth()
                            : scrollArrows.getWidth() / 2), 0, ANCHOR);
        }
        if (showScrollDown) {
            graphics.setClip(displayWidth / 2 - scrollArrows.getWidth() / 2,
                    displayHeight - scrollArrows.getHeight() / 2
                            - (customSoftKeyHandling ? 16 : 0), scrollArrows
                            .getWidth() / 2, scrollArrows.getHeight() / 2);
            graphics.drawImage(scrollArrows, displayWidth
                    / 2
                    - (scrollArrowPressed == 1 ? scrollArrows.getWidth()
                            : scrollArrows.getWidth() / 2), displayHeight
                    - scrollArrows.getHeight()
                    - (customSoftKeyHandling ? 16 : 0), ANCHOR);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawAboutPage()
     */
    public void drawAboutPage() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        drawBackPattern();
        drawTextPage(aboutText, aboutVisLines, aboutLine);
    }

    //--------------------------------------------------------------------------
    // Sprites
    //--------------------------------------------------------------------------
    String[][] cellsFile = { { "/player_cells.png", "/player_cells2.png" },
            { "/abuelo_cells.png", "/abuelo_cells2.png" },
            { "/chema_cells.png", "/chema_cells2.png" },
            { "/julian_cells.png", "/julian_cells2.png" },
            { "/manolito_cells.png", "/manolito_cells2.png" },
            { "/mariano_cells.png", "/mariano_cells2.png" },
            { "/nene_cells.png", "/nene_cells2.png" },
            { "/paco_cells.png", "/paco_cells2.png" },
            { "/tomas_cells.png", "/tomas_cells2.png" } };

    String[] rectsFile = { "/player_rects.bin", "/abuelo_rects.bin",
            "/chema_rects.bin", "/julian_rects.bin", "/manolito_rects.bin",
            "/mariano_rects.bin", "/nene_rects.bin", "/paco_rects.bin",
            "/tomas_rects.bin" };

    String[] animsFile = { "/player_anims.bin", "/abuelo_anims.bin",
            "/chema_anims.bin", "/julian_anims.bin", "/manolito_anims.bin",
            "/mariano_anims.bin", "/nene_anims.bin", "/paco_anims.bin",
            "/tomas_anims.bin" };

    static final int ANCHOR = Graphics.TOP | Graphics.LEFT;

    Image[][] animCells = new Image[NUM_ANIM_BANKS][2];

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#loadAnims()
     */
    public void loadAnims() throws Exception {
        DataInputStream dis;
        for (int c = 0; c < NUM_ANIM_BANKS; c++) {
            if (DEBUG) {
                System.out.println("RECTS: " + rectsFile[c]);
            }
            dis = new DataInputStream(
                    rectsFile[c]);
            rects[c] = new short[dis.readByte() & 0xFF][4];
            gaps[c] = new short[rects[c].length][2];
            for (int d = 0; d < rects[c].length; d++) {
                if (DEBUG) {
                    //                    System.out.println("Loading rect " + d + " of "
                    //                            + (rects[c].length - 1));
                }
                rects[c][d][0] = (short) (dis.readByte() & 0xFF);
                rects[c][d][1] = (short) (dis.readByte() & 0xFF);
                rects[c][d][2] = (short) (dis.readByte() & 0xFF);
                rects[c][d][3] = (short) (dis.readByte() & 0xFF);
                gaps[c][d][0] = (short) (dis.readByte() & 0xFF);
                gaps[c][d][1] = (short) (dis.readByte() & 0xFF);
            }
            dis.close();
            if (DEBUG) {
                System.out.println("ANIMS: " + animsFile[c]);
            }
            dis = new DataInputStream(
                    animsFile[c]);
            numAnims[c] = dis.readByte() & 0xFF;
            animCellIndex[c] = new short[numAnims[c]][];
            animCellDelay[c] = new short[numAnims[c]][];
            animCellOffsetX[c] = new short[numAnims[c]][];
            animCellOffsetY[c] = new short[numAnims[c]][];
            animRepeats[c] = new short[numAnims[c]];
            for (int d = 0; d < numAnims[c]; d++) {
                if (DEBUG) {
                    //                    System.out.println("Loading anim " + d + " of "
                    //                            + (numAnims[c] - 1));
                }
                animRepeats[c][d] = dis.readByte();
                int numFrames = dis.readByte() & 0xFF;
                if (DEBUG) {
                    //                    System.out.println("Num.Frames: " + numFrames);
                }
                animCellIndex[c][d] = new short[numFrames];
                animCellDelay[c][d] = new short[numFrames];
                animCellOffsetX[c][d] = new short[numFrames];
                animCellOffsetY[c][d] = new short[numFrames];
                for (int e = 0; e < numFrames; e++) {
                    animCellIndex[c][d][e] = (short) (dis.readByte() & 0xFF);
                    animCellDelay[c][d][e] = (short) (dis.readByte() & 0xFF);
                    animCellOffsetX[c][d][e] = dis.readShort();
                    animCellOffsetY[c][d][e] = dis.readShort();
                }
            }
            dis.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawSprite(int)
     */
    public void drawSprite(int sIndex) {
        int dx = spritePx[sIndex] - cameraX;
        int dy = spritePy[sIndex] - cameraY;
        //        if (spriteType[sIndex] == ANIM_INDEX_ENEMY_PACO) {
        //            dy -= 32;
        //        }
        if (dx + spriteCw[sIndex] > 0 && dx < displayWidth
                && dy + spriteCh[sIndex] > 0 && dy < displayHeight) {
            graphics.setClip(dx, dy, spriteCw[sIndex], spriteCh[sIndex]);
            //            if (sIndex == 0) {
            //                graphics.setColor(0xFF8040);
            //                graphics.fillRect(dx, dy, spriteCw[sIndex], spriteCh[sIndex]);
            //            } else {
            graphics.drawImage(
                    animCells[animBank[sIndex]][spriteFacing[sIndex]],
                    -spriteCx[sIndex] + dx, -spriteCy[sIndex] + dy, ANCHOR);
            //            }
        }
        //System.out.println("SPRITE DRAWN: " + sIndex + ", " +
        // spriteVisible[sIndex]);
    }

    //--------------------------------------------------------------------------
    // Scenery
    //--------------------------------------------------------------------------
    String[] mapFile = { "/mapa1.mmp", "/mapa2.mmp", "/mapa3.mmp",
            "/mapa4.mmp", "/mapa5.mmp", "/mapa6.mmp" };

    String[] collisionFile = { "/mapa1.mmp.collision", "/mapa2.mmp.collision",
            "/mapa3.mmp.collision", "/mapa4.mmp.collision",
            "/mapa5.mmp.collision", "/mapa6.mmp.collision" };

    String combiTilesFile = "/mapa.mmp.btsc";

    String[] tilesFile = { "/map_tiles.png" };

    Image tiles;

    Image combTiles;

    //int tilesPerRow;
    int tilesPerCol;

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#loadMaps()
     */
    public void loadMaps() throws Exception {
        DataInputStream dis;
        if (DEBUG) {
            System.out.println("Loading maps. Map count: " + MAP_COUNT);
        }
        for (int c = 0; c < MAP_COUNT; c++) {
            if (DEBUG) {
                System.out.println("Loading map n�: " + c + " file: "
                        + mapFile[c]);
            }
            /* MONGOFIX
            dis = new DataInputStream(this.getClass().getResourceAsStream(
                    mapFile[c]));*/
            FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+mapFile[c].substring(1));
            byte[] bytes = file.readBytes();
            int byteIndex = 0;
            mapCols[c] = bytes[byteIndex]/*dis.readByte()*/ & 0xFF; byteIndex++;
            mapRows[c] = bytes[byteIndex]/*dis.readByte()*/ & 0xFF; byteIndex++;
            tileIndex[c] = new byte[mapRows[c]][mapCols[c] + 1];
            for (int y = 0; y < mapRows[c]; y++) {
                for (int x = 0; x < mapCols[c]; x++) {
                    tileIndex[c][y][x] = bytes[byteIndex]; byteIndex++; //dis.readByte();
                }
            }
            //dis.close();
            if (DEBUG) {
                System.out.println("Loading collision file: "
                        + collisionFile[c]);
            }
            /*dis = new DataInputStream(this.getClass().getResourceAsStream(
                    collisionFile[c]));*/
            file = Gdx.files.internal(MIDlet.assetsFolder+"/"+collisionFile[c].substring(1));
            bytes = file.readBytes();
            byteIndex = 0;
            int cc = bytes[byteIndex]/*dis.readByte()*/ & 0xFF; byteIndex++;
            int rr = bytes[byteIndex]/*dis.readByte()*/ & 0xFF; byteIndex++;
            collisionIndex[c] = new byte[rr][cc + 16];
            Vector ms = new Vector();
            Vector ch = new Vector();
            for (int y = 0; y < mapRows[c]; y++) {
                for (int x = 0; x < mapCols[c]; x++) {
                    collisionIndex[c][y][x] = bytes[byteIndex]; byteIndex++; //dis.readByte();
                    if (collisionIndex[c][y][x] == 3) {
                        playerStartX[c] = x * tileWidth;
                        playerStartY[c] = (y - 2) * tileHeight;
                        if (DEBUG) {
                            System.out.println("Player start point: "
                                    + playerStartX[c] + ", " + playerStartY[c]);
                        }
                    } else if (collisionIndex[c][y][x] == 2) {
                        bossStartX[c] = x * tileWidth;
                        bossStartY[c] = (y - 2) * tileHeight;
                        if (DEBUG) {
                            System.out.println("Boss start point: "
                                    + bossStartX[c] + ", " + bossStartY[c]);
                        }
                    } else if (collisionIndex[c][y][x] == 1) {
                        ch
                                .addElement(new int[] { x * tileWidth,
                                        y * tileHeight });
                    }
                }
            }
            for (int x = 0; x < mapCols[c]; x++) {
                for (int y = 0; y < mapRows[c]; y++) {
                    if (collisionIndex[c][y][x] == 6) {
                        ms.addElement(new int[] { (x + 2) * tileWidth,
                                y * tileHeight });
                    }
                }
            }
            for (int x = mapCols[c]; x < collisionIndex[c][0].length; x++) {
                for (int y = mapRows[c] - mapFloorHeight[c]; y < mapRows[c]; y++) {
                    collisionIndex[c][y][x] = 5;
                }
            }
            ms.addElement(new int[] { (mapCols[c] + 4) * tileWidth,
                    mapRows[c] * tileHeight });
            int mss = ms.size();
            mapSection[c] = new int[mss + 1][];
            mapSectionTriggered[c] = new boolean[mss + 1];
            for (int s = 0; s < mss; s++) {
                mapSection[c][s] = (int[]) ms.elementAt(s);
                if (DEBUG) {
                    System.out.println("MAP INDEX: " + c + " MAP SECTION: " + s
                            + " COORDS: " + mapSection[c][s][0] + ", "
                            + mapSection[c][s][1]);
                }
            }
            int chs = ch.size();
            if (DEBUG) {
                System.out.println("NUM.CHILD: " + chs);
            }
            childStartX[c] = new int[chs];
            childStartY[c] = new int[chs];
            for (int s = 0; s < chs; s++) {
                int[] sp = (int[]) ch.elementAt(s);
                childStartX[c][s] = sp[0];
                childStartY[c][s] = sp[1];
                if (DEBUG) {
                    System.out.println("Child " + s + ": " + sp[0] + ", "
                            + sp[1]);
                }
            }
        }
        mapIndex = 0;
        viewMapCols = displayWidth / tileWidth + 2;
        if (displayWidth % tileWidth != 0) {
            viewMapCols++;
        }
        viewMapRows = displayHeight / tileHeight + 1;
        if (displayHeight % tileHeight != 0) {
            viewMapRows++;
        }
        tilesPerCol = tiles.getWidth() / tileWidth;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#paintMap()
     */
    public void drawMap() {
        int x = -tileWidth - mapOffsetX;
        int y = -tileHeight - mapOffsetY;
        for (int r = mapRow; r <= mapRow + viewMapRows
                && r < tileIndex[mapIndex].length; r++) {
            x = -tileWidth - mapOffsetX;
            for (int c = mapCol; c < mapCol + viewMapCols; c++) {
                graphics.setClip(x, y, tileWidth, tileHeight);
                int tile = tileIndex[mapIndex][r][c] & 0xFF;
                if (tile != 0) {
                    tile--;
                    graphics.drawImage(tiles, x - tileWidth
                            * (tile % tilesPerCol), y - tileHeight
                            * (tile / tilesPerCol), ANCHOR);
                }
                x += tileWidth;
            }
            y += tileHeight;
        }
    }

    int[] specialFlashColor = { 0, 0xFFFFFF };

    int specialFlashIndex;

    int[] specialRayColor = { 0xFFFFFF, 0xFFFF00, 0xFF0000 };

    //
    static final int FIRE_POINT[][] = { { 2, 15 }, { 2, 15 }, { 2, 15 },
            { 1, 2 }, { 1, 2 }, { 11, 12 }, { 26, 11 }, { 26, 11 }, { 28, 11 },
            { 28, 11 }, { 28, 11 }, { 24, 12 }, { 24, 12 }, { 24, 24 } };

    static final int FIRE_CYCLE[] = { 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
            0xFFFFFFFF, 0xFFFFFFCC, 0xFFFFFFAA, 0xFFFFFF88, 0xFFFFAA66,
            0xFFFFDD55, 0xFFFFCC55, 0xFFFFBB44, 0xFFFF9944, 0xFFFF8833,
            0xFFFF7733, 0xFFFF6622, 0xFFFF4422, 0xFFFF3311, 0xFFFF1100,
            0xFFFF0000, 0xCCCC0000, 0xAAAA0000, 0x88880000, 0x66660000,
            0x44440000, 0x22220000, 0x000000 };

    static final int NUM_FIRE_PARTICLE = 150;

    int[] fireParticleX = new int[NUM_FIRE_PARTICLE];

    int[] fireParticleY = new int[NUM_FIRE_PARTICLE];

    int[] fireParticleCycleIndex = new int[NUM_FIRE_PARTICLE];

    int[] fireParticleSize = new int[NUM_FIRE_PARTICLE];

    int lastFireParticle;

    static final int PARTICLE_SPEED = 2;

    static final int NUM_PARTICLES_GEN = 20;

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawSpecialBackground()
     */
    public void drawSpecialBackground() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        graphics.setColor(specialFlashColor[specialFlashIndex]);
        graphics.fillRect(0, 0, displayWidth, displayHeight);
        //specialFlashIndex++;
        if (specialFlashIndex >= specialFlashColor.length) {
            specialFlashIndex = 0;
        }
        //
        for (int c = 0; c < NUM_PARTICLES_GEN; c++) {
            lastFireParticle++;
            if (lastFireParticle >= NUM_FIRE_PARTICLE) {
                lastFireParticle = 0;
            }
            fireParticleCycleIndex[lastFireParticle] = 0;
            fireParticleX[lastFireParticle] = spritePx[SPRITE_INDEX_PLAYER]
                    + (spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_RIGHT
                            ? FIRE_POINT[animCellIndexCounter[SPRITE_INDEX_PLAYER]][0]
                            : -FIRE_POINT[animCellIndexCounter[SPRITE_INDEX_PLAYER]][0] + 24)
                    + (Game.random.nextInt() % 8);
            fireParticleY[lastFireParticle] = spritePy[SPRITE_INDEX_PLAYER]
                    + FIRE_POINT[animCellIndexCounter[SPRITE_INDEX_PLAYER]][1]
                    + (Game.random.nextInt() % 4);
            fireParticleSize[lastFireParticle] = 2;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawParticles()
     */
    public void drawParticles() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        for (int c = 0; c < NUM_FIRE_PARTICLE; c++) {
            if (fireParticleCycleIndex[c] >= 0) {
                fireParticleX[c] += spriteFacing[SPRITE_INDEX_PLAYER] == SPRITE_FACING_LEFT
                        ? PARTICLE_SPEED : -PARTICLE_SPEED;
                fireParticleY[c] -= Math.abs(Game.random.nextInt() % 3) + 1;
                if (fireParticleX[c] - cameraX > displayWidth
                        || fireParticleX[c] - cameraX < 0) {
                    fireParticleCycleIndex[c] = -1;
                } else {
                    fireParticleCycleIndex[c] += 3;
                    if (fireParticleCycleIndex[c] % 8 == 0
                            && fireParticleSize[c] > 1) {
                        fireParticleSize[c]--;
                    }
                    if (fireParticleCycleIndex[c] >= FIRE_CYCLE.length) {
                        fireParticleCycleIndex[c] = -1;
                    } else {
                        graphics
                                .setColor(FIRE_CYCLE[fireParticleCycleIndex[c]]);
                        if (fireParticleSize[c] > 1) {
                            graphics.fillRect(fireParticleX[c] - cameraX,
                                    fireParticleY[c] - cameraY,
                                    fireParticleSize[lastFireParticle],
                                    fireParticleSize[lastFireParticle]);
                        } else {
                            graphics.drawLine(fireParticleX[c] - cameraX,
                                    fireParticleY[c] - cameraY,
                                    fireParticleX[c] - cameraX,
                                    fireParticleY[c] - cameraY);
                        }
                    }
                }
            }
        }
        for (int c = 0; c < NUM_HIT_PARTICLES; c++) {
            if (spriteHitFxSpeeds[c][1] < 10) {
                graphics.setColor(spriteHitFxColor[c]);
                if (spriteHitFxSpeeds[c][1] < 0) {
                    graphics.fillRect(spriteHitFxCoords[c][0] - cameraX,
                            spriteHitFxCoords[c][1] - cameraY, 2, 2);
                } else {
                    graphics.drawLine(spriteHitFxCoords[c][0] - cameraX,
                            spriteHitFxCoords[c][1] - cameraY,
                            spriteHitFxCoords[c][0] - cameraX,
                            spriteHitFxCoords[c][1] - cameraY);
                }
                spriteHitFxCoords[c][0] += spriteHitFxSpeeds[c][0];
                spriteHitFxCoords[c][1] += spriteHitFxSpeeds[c][1];
                spriteHitFxSpeeds[c][1]++;
            }
        }
    }

    /**
     *  
     */
    public void drawBonus() {
        if (bonusVisible > 0) {
            bonusVisible--;
            bonusY -= 2;
            graphics.setClip(0, 0, displayWidth, displayHeight);
            graphics.drawImage(bonusLabel, bonusX - cameraX, bonusY - cameraY,
                    Graphics.TOP | Graphics.HCENTER);
        }
    }

    //--------------------------------------------------------------------------
    // Text util
    //--------------------------------------------------------------------------

    int[] outX = { 1, 1, 1, 1, 0, 0, -1, -1, -1, -1, -1, -1, 0, 0, 1, 1 };

    int[] outY = { 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, -1, -1, -1, -1, -1, -1 };

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawOutlineText(java.lang.String, int, int, int, int, int)
     */
    public void drawOutlineText(String text, int foreColor, int backColor,
            int x, int y, int anchor) {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        graphics.setColor(backColor);
        for (int c = 0; c < outX.length; c++) {
            graphics.drawString(text, x + outX[c], y + outY[c], anchor);
            //  graphics.drawString(text, x + outX[c]*2, y + outY[c]*2, anchor);
        }
        graphics.setColor(foreColor);
        graphics.drawString(text, x, y, anchor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawOutlineText(java.lang.String, int, int, int, int, int)
     */
    public void drawText(String text, int foreColor, int x, int y, int anchor) {
        graphics.setColor(foreColor);
        graphics.drawString(text, x, y, anchor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#readTextFile(java.lang.String)
     */
    public String[] readTextFile(String file) throws Exception {
        DataInputStream dis;
        Vector data = new Vector();
        //String line = null;
        String[] r;
        StringBuffer sb = new StringBuffer();
        int read;
        if (DEBUG) {
            System.out.println("LOADING TEXT FILE: " + file);
        }
        //MONGOFIX
        //dis = new DataInputStream(this.getClass().getResourceAsStream(file));
        FileHandle f = Gdx.files.internal(MIDlet.assetsFolder+"/"+file.substring(1));
        byte[] bytes = f.readBytes();
        int fIndex = 0;
        do {
            if(fIndex < bytes.length) {
                read = bytes[fIndex] + (bytes[fIndex] < 0 ? 256 : 0);
                fIndex++;
            }
            else
                read = -1;
            //dis.read();
            if (read >= 0) {
                if (read == 13) {
                    data.addElement(sb.toString());
                    //          System.out.println(sb.toString());
                    sb.delete(0, sb.length());
                } else if (read != 10) {
                    sb.append((char) read);
                }
            }
        } while (read != -1);
        //dis.close();
        r = new String[data.size()];
        for (int c = 0; c < r.length; c++) {
            r[c] = (String) data.elementAt(c);
        }
        //System.out.println("Text file loaded.");
        return r;
    }

    //

    //

    Image energyBar;

    Image playerTry;

    Image specialIcon;

    Image goIcon;

    Image[] enemyIcon;

    Image comboPowerBar;

    Image comboLabel;

    Image bonusLabel;

    String[] enemyIconFile = { null, "/jetos_chema.png", "/jetos_julian.png",
            "/jetos_manolito.png", "/jetos_mariano.png", null,
            "/jetos_paco.png", "/jetos_tomas.png" };

    Image scoreNumbers;

    Image bossLabel;

    int numberWidth = 10;

    int numberHeight = 10;

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawScoreboard()
     */
    public void drawScoreboard() {
        graphics.setClip(0, 16, spriteEnergy[SPRITE_INDEX_PLAYER] / 2, 8);
        graphics.drawImage(energyBar, 0, 16, ANCHOR);
        graphics.setClip(0, 49, comboPower / 2, 4);
        graphics.drawImage(comboPowerBar, 0, 49, ANCHOR);
        if (lastEnemyHit > 0) {
            graphics.setClip(displayWidth - energyBar.getWidth(), 16,
                    spriteEnergy[lastEnemyHit] / 2, 8);
            graphics.drawImage(energyBar, displayWidth - energyBar.getWidth(),
                    16, ANCHOR);
        }
        int sc = playerScore;
        int dv = 10000;
        int x;
        for (int c = 0; c < 5; c++) {
            graphics.setClip(c * numberWidth /* + c * 2 */, 26, numberWidth,
                    numberHeight);
            x = (c - sc / dv) * numberWidth /* + c * 2 */;
            graphics.drawImage(scoreNumbers, x, 26, ANCHOR);
            sc = sc % dv;
            dv /= 10;
        }
        graphics.setClip(0, 0, displayWidth, displayHeight);
        if (comboPower >= 100 && currentTick % 2 == 0) {
            graphics.drawImage(comboLabel, 2, 48, ANCHOR);
        }
        if (lastEnemyHit > 0 && enemyIcon[spriteType[lastEnemyHit]] != null) {
            graphics.drawImage(enemyIcon[spriteType[lastEnemyHit]],
                    displayWidth - energyBar.getWidth(), 2, ANCHOR);
            if (lastEnemyHit == SPRITE_INDEX_BOSS) {
                graphics.drawImage(bossLabel, displayWidth
                        - energyBar.getWidth() + 18, 5, ANCHOR);
            }
        }
        for (int c = 0; c < playerTries; c++) {
            graphics.drawImage(playerTry, c * (playerTry.getWidth() + 2), 0,
                    ANCHOR);
        }
        if (showGoIcon && !mapSectionTriggered[mapIndex][mapSectionIndex]) {
            graphics.drawImage(goIcon, displayWidth - goIcon.getWidth() - 16
                    + (int) (Game.currentTick % 4) * 4, (displayHeight - goIcon
                    .getHeight()) / 2, ANCHOR);
        }
        for (int c = 0; c < playerSpecials; c++) {
            graphics.drawImage(specialIcon, c * (specialIcon.getWidth() + 1),
                    26 + numberHeight + 2, ANCHOR);
        }
        if (Game.playerSpecial && Game.currentTick % 2 == 0) {
            graphics.drawImage(specialIcon, playerSpecials
                    * (specialIcon.getWidth() + 1), 26 + numberHeight + 2,
                    ANCHOR);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Gfx#drawLogoMicro()
     */
    public void drawLogoMicro() {
        if (logoMicro != null) {
            graphics.setColor(0xFFFFFF);
            graphics.fillRect(0, 0, displayWidth, displayHeight);
            graphics.drawImage(logoMicro,
                    (displayWidth - logoMicro.getWidth()) / 2,
                    (displayHeight - logoMicro.getHeight()) / 2 - 16, ANCHOR);
        }
        graphics.setColor(0x04AFEF);
        graphics.fillRect(16, pby, progress * (displayWidth - 32) / 39, 8);
        graphics.drawRect(14, pby - 2, displayWidth - 29, 11);
        //        if (Game.CHEATS) {
        //            graphics.setColor(0xFF0000);
        //            graphics.drawString("�OJO, TRAMPICHUELAS!", displayWidth / 2,
        //                    progress, Graphics.TOP | Graphics.HCENTER);
        //        }
        if (Game.DEBUG) {
            graphics.setColor(0xFF0000);
            graphics.drawString("FMEM: " + Runtime.getRuntime().freeMemory(),
                    0, displayHeight - fontHeight - 4, Graphics.TOP
                            | Graphics.LEFT);
            graphics.drawString("TMEM: " + Runtime.getRuntime().totalMemory(),
                    0, displayHeight - fontHeight * 3 - 4, Graphics.TOP
                            | Graphics.LEFT);
            graphics.drawString("UMEM: "
                    + (Runtime.getRuntime().totalMemory() - Runtime
                            .getRuntime().freeMemory()), 0, displayHeight
                    - fontHeight * 2 - 4, Graphics.TOP | Graphics.LEFT);
        }
    }

    String[] inGameText;

    static final int INGAME_TEXT_LEVEL = 0;

    static final int INGAME_TEXT_READY = 1;

    static final int INGAME_TEXT_LEVEL_COMPLETED = 2;

    static final int INGAME_TEXT_DEAD = 3;

    static final int INGAME_TEXT_GAME_OVER = 4;

    int gameOverH;

    Image woodBar;

    /*
     *  
     */
    private void drawTextLineBackground(int y) {
        y--;
        graphics.setColor(0x880000);
        graphics.fillRect(0, y, displayWidth, fontHeight);
        for (int c = 0; c <= displayWidth; c += 16) {
            graphics.drawImage(woodBar, c, y - 4, ANCHOR);
            graphics.drawImage(woodBar, c, y + fontHeight, ANCHOR);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawGameOver()
     */
    public void drawGameOver() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        if (gameOverH < displayHeight / 2) {
            gameOverH += 8;
        }
        graphics.setColor(0);
        graphics.fillRect(0, 0, displayWidth, gameOverH);
        graphics
                .fillRect(0, displayHeight - gameOverH, displayWidth, gameOverH);
        drawTextLineBackground((displayHeight - fontHeight) / 2);
        drawText(inGameText[INGAME_TEXT_GAME_OVER], 0xFFFFFF, displayWidth / 2,
                (displayHeight - fontHeight) / 2, Graphics.HCENTER
                        | Graphics.TOP);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawGameStart()
     */
    public void drawGameStart() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        drawTextLineBackground(displayHeight / 2 - fontHeight);
        drawTextLineBackground(displayHeight / 2 + fontHeight);
        drawText(inGameText[INGAME_TEXT_LEVEL] + " " + (mapIndex + 1),
                0xFFFFFF, displayWidth / 2, displayHeight / 2 - fontHeight,
                Graphics.HCENTER | Graphics.TOP);
        drawText(inGameText[INGAME_TEXT_READY], 0xFFFFFF, displayWidth / 2,
                displayHeight / 2 + fontHeight, Graphics.HCENTER | Graphics.TOP);
        gameOverH = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawPlayerDead()
     */
    public void drawPlayerDead() {
        //graphics.setClip(0, 0, displayWidth, displayHeight);
        //drawTextLineBackground((displayHeight - fontHeight) / 2);
        //graphics.setClip(0, 0, displayWidth, displayHeight);
        //        drawText(inGameText[INGAME_TEXT_DEAD], 0xFFFFFF, displayWidth / 2,
        //                (displayHeight - fontHeight) / 2, Graphics.HCENTER
        //                        | Graphics.TOP);
    }

    /*
     * (non-Javadoc)
     * 
     * @see Gfx#drawLevelCompleted()
     */
    public void drawLevelCompleted() {
        graphics.setClip(0, 0, displayWidth, displayHeight);
        drawTextLineBackground((displayHeight - fontHeight) / 2);
        drawText(inGameText[INGAME_TEXT_LEVEL_COMPLETED], 0xFFFFFF,
                displayWidth / 2, (displayHeight - fontHeight) / 2,
                Graphics.HCENTER | Graphics.TOP);
    }

    int NUM_HIT_PARTICLES = 2000;

    int[][] spriteHitFxCoords = new int[NUM_HIT_PARTICLES][2];

    int[][] spriteHitFxSpeeds = new int[NUM_HIT_PARTICLES][2];

    int[] spriteHitFxColor = new int[NUM_HIT_PARTICLES];

    int[] spriteHitFxColorTable = { 0xFF0000, 0xDD0000, 0xBB0000, 0x990000,
            0x770000, 0xFF1100, 0xEE1100, 0xCC1100, 0xAA1100, 0x991100,
            0xFF0011, 0x550011, 0xDD0011, 0x660011, 0xBB0011, 0xFF1111,
            0xEE1111, 0xDD1111, 0xAA1111, 0x991111, 0xFF2200, 0xEE2200,
            0xDD2200, 0xCC2200, 0xBB2200, 0xFF0022, 0x440022, 0xDD0022,
            0x660022, 0xBB0022, 0xFF4444, 0x99E2222, 0xDD2222, 0x772222,
            0xBB2222 };

    int lastSpriteHitFxIndex;

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Game#executeEnemyHitFx(int,
     *      boolean)
     */
    public void genSpriteHitFx(int sIndex, boolean dead) {
        int howMany = sIndex == SPRITE_INDEX_PLAYER ? (dead ? 50 : 25) : (dead
                ? playerAttackDamage / 2 : playerAttackDamage / 4);
        if (CHEATS && Cheats.isCheatEnabled(Cheats.CHEAT_GORE)) {
            if (spriteType[sIndex] == ANIM_INDEX_NIÑO) {
                howMany = 25;
            } else {
                howMany *= 10;
            }
        }
        for (int c = 0; c < howMany; c++) {
            spriteHitFxColor[lastSpriteHitFxIndex] = spriteHitFxColorTable[Math
                    .abs(random.nextInt() % spriteHitFxColorTable.length)];
            spriteHitFxCoords[lastSpriteHitFxIndex][0] = spriteX[sIndex] + 16
                    + random.nextInt() % 16;
            spriteHitFxCoords[lastSpriteHitFxIndex][1] = spriteY[sIndex] + 10
                    + random.nextInt() % 10;
            spriteHitFxSpeeds[lastSpriteHitFxIndex][0] = (spriteFacing[sIndex] == SPRITE_FACING_LEFT
                    ? -3 : 3)
                    + random.nextInt() % 3;
            if (CHEATS && Cheats.isCheatEnabled(Cheats.CHEAT_GORE)
                    && spriteType[sIndex] == ANIM_INDEX_NIÑO) {
                spriteHitFxSpeeds[lastSpriteHitFxIndex][0] *= -1;
                spriteHitFxCoords[lastSpriteHitFxIndex][0] = spriteX[sIndex]
                        + 8 + random.nextInt() % 8;
                spriteHitFxCoords[lastSpriteHitFxIndex][1] = spriteY[sIndex]
                        + 16 + random.nextInt() % 5;
            }
            spriteHitFxSpeeds[lastSpriteHitFxIndex][1] = (dead ? -4 : -2)
                    + random.nextInt() % 3;
            lastSpriteHitFxIndex++;
            if (lastSpriteHitFxIndex >= NUM_HIT_PARTICLES) {
                lastSpriteHitFxIndex = 0;
            }
        }
    }

    private RecordStore sram;
    private int sramDataId;

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Game#saveData()
     */
    public void loadData() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            byte[] sramData;
            sram = RecordStore.openRecordStore("sram", true);
            if (sram.getNumRecords() == 0) {
                if (DEBUG) {
                    System.out.println("RMS CREATED");
                }
                dos.writeBoolean(audio.isAudioEnabled());
                dos.writeBoolean(audio.isVibrationEnabled());
                dos.writeUTF(hiName);
                dos.writeByte(hiLevel);
                dos.writeInt(hiScore);
                sramData = baos.toByteArray();
                dos.close();
                sramDataId = sram.addRecord(sramData, 0, sramData.length);
            } else {
                if (DEBUG) {
                    System.out.println("RMS EXITS. LOADING.");
                }
                RecordEnumeration re = sram.enumerateRecords(null, null, false);
                sramDataId = re.nextRecordId();
                sramData = sram.getRecord(sramDataId);
                java.io.DataInputStream dis = new java.io.DataInputStream(
                        new ByteArrayInputStream(sramData));
                audio.setAudioEnabled(dis.readBoolean());
                audio.setVibrationEnabled(dis.readBoolean());
                hiName = dis.readUTF();
                hiLevel = dis.readByte() & 0xFF;
                hiScore = dis.readInt();
            }
            sram.closeRecordStore();
            sram = null;
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("RMS LOAD: " + e);
            }
        }
        System.gc();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Game#loadData()
     */
    public void saveData() {
        try {
            if (DEBUG) {
                System.out.println("RMS SAVING.");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            byte[] sramData;
            sram = RecordStore.openRecordStore("sram", true);
            dos.writeBoolean(audio.isAudioEnabled());
            dos.writeBoolean(audio.isVibrationEnabled());
            dos.writeUTF(hiName);
            dos.writeByte(hiLevel);
            dos.writeInt(hiScore);
            sramData = baos.toByteArray();
            dos.close();
            sram.setRecord(sramDataId, sramData, 0, sramData.length);
            sram.closeRecordStore();
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("RMS SAVE: " + e);
            }
        }
        System.gc();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Game#drawHighScore()
     */
    public void drawHighScore() {
        showHighScore = 0;
        drawTitle();
    }

    String[][] gameCompleteText;

    /*
     * (non-Javadoc)
     * 
     * @see com.microjocs.thaiwarrior.common.Game#drawGameCompleted()
     */
    protected void drawGameCompleted() {
        int y = (displayHeight - gameCompleteText[gameCompleteStep].length
                * fontHeight) / 2;
        if (gameCompleteTextY > y) {
            gameCompleteTextY--;
        }
        if (gameCompleteStep == 0) {
            graphics.drawImage(background3, 0, 0, ANCHOR);
        } else {
            graphics.drawImage(background2, 0, 0, ANCHOR);
        }
        for (int c = 0; c < gameCompleteText[gameCompleteStep].length; c++) {
            drawOutlineText(gameCompleteText[gameCompleteStep][c], 0xFFFFFF, 0,
                    displayWidth / 2, gameCompleteTextY + c * fontHeight,
                    Graphics.HCENTER | Graphics.TOP);
        }
    }
}