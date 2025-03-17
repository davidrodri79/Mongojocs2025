package com.mygdx.mongojocs.ranran;// RanRan port for IMode

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.iapplicationemu.AudioPresenter;
import com.mygdx.mongojocs.iapplicationemu.Canvas;
import com.mygdx.mongojocs.iapplicationemu.Connector;
import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.Font;
import com.mygdx.mongojocs.iapplicationemu.Frame;
import com.mygdx.mongojocs.iapplicationemu.Graphics;
import com.mygdx.mongojocs.iapplicationemu.HttpConnection;
import com.mygdx.mongojocs.iapplicationemu.IApplication;
import com.mygdx.mongojocs.iapplicationemu.Image;
import com.mygdx.mongojocs.iapplicationemu.Label;
import com.mygdx.mongojocs.iapplicationemu.MediaImage;
import com.mygdx.mongojocs.iapplicationemu.MediaListener;
import com.mygdx.mongojocs.iapplicationemu.MediaManager;
import com.mygdx.mongojocs.iapplicationemu.MediaPresenter;
import com.mygdx.mongojocs.iapplicationemu.MediaSound;
import com.mygdx.mongojocs.iapplicationemu.Panel;
import com.mygdx.mongojocs.iapplicationemu.PhoneSystem;
import com.mygdx.mongojocs.iapplicationemu.SoftKeyListener;
import com.mygdx.mongojocs.iapplicationemu.TextBox;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Random;


public class Game extends IApplication implements MediaListener,
        SoftKeyListener {

    // system data
    static final boolean DEBUG = true;

    static final int UPDATE_DELAY = 40;

    static int speedMultiplier = 3;

    static {
        /*String platform = System.getProperty("microedition.platform")
                .toUpperCase();*/
        String[] platforms = { "TSM30I", "M340I" };
        int speedM[] = { 4, 2 };
        int c = 0;
        /*for (c = 0; c < platforms.length
                && platforms[c].compareTo(platform) != 0; c++)
            ;*/
        if (c < platforms.length) {
            speedMultiplier = speedM[c];
        }
    }

    GameCanvas gameCanvas;

    static Graphics graphics;

    static boolean paintFlag;

    static int canvasWidth;

    static int canvasHeight;

    Font font = Font.getDefaultFont();

    int fontHeight = font.getHeight();

    static Game gameInstance;

    static boolean finished;

    long startTime;

    long deltaTime;

    long customDelayStartTime;

    long currentTick;

    static int keyCode;

    static boolean keyPressed;

    boolean soundEnabled = true;

    boolean vibrationEnabled = true;

    boolean initialized;

    static final int SRAM_HEADER_OFFSET = 0;

    static final int SRAM_HEADER_LENGTH = 2;

    static final int SRAM_AUDIO_PREFS_OFFSET = SRAM_HEADER_LENGTH;

    static final int SRAM_AUDIO_PREFS_LENGTH = 1;

    static final int SRAM_VIBRA_PREFS_OFFSET = SRAM_AUDIO_PREFS_OFFSET
            + SRAM_AUDIO_PREFS_LENGTH;

    static final int SRAM_VIBRA_PREFS_LENGTH = 1;

    static final int SRAM_SCORES_OFFSET = SRAM_VIBRA_PREFS_OFFSET
            + SRAM_VIBRA_PREFS_LENGTH;

    static final int SRAM_SCORES_LENGTH = 15;

    // -------------------------------------------------------------------------
    // application
    // -------------------------------------------------------------------------
    public Game() throws Exception {
        if (DEBUG) {
            System.out.println("Game");
        }
        finished = false;
        gameInstance = this;
        readSRam();
        gameCanvas = new GameCanvas();
        System.gc();
    }

    //
    public void start() {
        Display.setCurrent(gameCanvas);
        gameCanvas.repaint();
        run();
    }
    
    void loadSplashData() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                splashImage = loadImage("/logo.gif");
            }
        });

    }

    // main loop
    public void run() {
        Display.setCurrent(gameCanvas);

        loadSplashData();           
        try {
            while (!finished) {
                startTime = System.currentTimeMillis();
                keyCode = gameCanvas.lastKeyCode;
                keyPressed = gameCanvas.lastKeyPressed;
                updateLogic();
                gameCanvas.lastKeyPressed = keyPressed;
                if (mainState != STATE_ENTER_NAME) {
                    paintFlag = true;
                    gameCanvas.repaint();
                    while (paintFlag) {
                        Thread.sleep(1);
                    }
                }
                deltaTime = System.currentTimeMillis() - startTime;
                if (deltaTime < UPDATE_DELAY) {
                    Thread.sleep(UPDATE_DELAY - deltaTime);
                } else {
                    Thread.sleep(1);
                }
                currentTick++;
            }
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("Error at main loop.");
                System.out.println("State: " + mainState);
                if (mainState == STATE_INGAME) {
                    System.out.println("In-Game state:" + inGameState);
                }
                e.printStackTrace();
            }
        }
        try {
            writeSRam();
        } catch (Exception e) {
            if (DEBUG) {
                System.out
                        .println("Error writing sram at the end of main loop.");
                e.printStackTrace();
            }
        }
        terminate();
    }

    // -------------------------------------------------------------------------
    // util
    // -------------------------------------------------------------------------
    public byte[] loadFile(String Nombre) {
        System.gc();

        InputStream is = getClass().getResourceAsStream(Nombre);

        byte[] buffer = new byte[1024];

        try {
            int Size = 0;

            while (true) {
                int Desp = is.read(buffer, 0, buffer.length);
                if (Desp <= 0) {
                    break;
                }
                Size += Desp;
            }

            is = null;
            System.gc();

            buffer = new byte[Size];

            is = getClass().getResourceAsStream(Nombre);
            Size = is.read(buffer, 0, buffer.length);

            while (Size < buffer.length) {
                Size += is.read(buffer, Size, buffer.length - Size);
            }

            is.close();
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("Error loading file: " + Nombre);
                e.printStackTrace();
            }
            finished = true;
        }

        System.gc();

        return buffer;
    }

    int scratchTemp = 40960;

    //
    public Image[] loadImage(byte[] bufer) {
        int index = 0;
        int numImages = (bufer[index++] << 8 & 0xff00)
                | (bufer[index++] & 0xff);
        int buferIndex = (numImages + 1) * 2;

        Image img[] = new Image[numImages];

        for (int i = 0; i < numImages; i++) {
            int buferSize = (bufer[index++] << 8 & 0xff00)
                    | (bufer[index++] & 0xff);

            try {
                OutputStream out = Connector
                        .openOutputStream("scratchpad:///0;pos=" + scratchTemp);
                out.write(bufer, buferIndex, buferSize);
                out.close();
            } catch (Exception e) {
            }

            img[i] = loadImage(scratchTemp);

            buferIndex += buferSize;
        }

        return img;
    }

    //
    boolean intersects(int x1, int y1, int xs1, int ys1, int x2, int y2,
            int xs2, int ys2) {
        return !((x2 > x1 + xs1) || (x2 + xs2 < x1) || (y2 > y1 + ys1) || (y2
                + ys2 < y1));
    }

    //
    Image[] loadImage(String fileName, int frames) {
        Image Img[] = new Image[frames];
        for (int i = 0; i < frames; i++) {
            Img[i] = loadImage(fileName + i + ".gif");
        }
        return Img;
    }

    //
    Image loadImage(String fileName) {
        MediaImage mimage = MediaManager.getImage("resource://" + fileName);
        try {
            mimage.use();
        } catch (Exception ui) {
            if (DEBUG) {
                System.out.println("Error loading image: " + fileName);
                ui.printStackTrace();
            }
            finished = true;
        }
        return mimage.getImage();
    }

    //     
    Image loadImage(int Pos) {
        MediaImage mimage = MediaManager.getImage("scratchpad:///0;pos=" + Pos);

        try {
            mimage.use();
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("Error loading image from sp: " + Pos);
                e.printStackTrace();
            }
            finished = true;
        }
        return mimage.getImage();
    }

    int sramOffset = 36000;

    //
    void readSRam() {
        byte[] formatData = { 'M', 'J' };
        byte[] buffer;
        int c;
        buffer = new byte[formatData.length];
        readSRamData(0, buffer);
        for (c = 0; c < buffer.length && formatData[c] == buffer[c]; c++)
            ;
        if (c < buffer.length) {
            writeSRamData(SRAM_HEADER_OFFSET, formatData);
            writeSRamData(SRAM_AUDIO_PREFS_OFFSET, new byte[] { 1 });
            writeSRamData(SRAM_VIBRA_PREFS_OFFSET, new byte[] { 1 });
            writeSRamData(SRAM_SCORES_OFFSET,
                    new byte[] { 'M', 'I', 'C', 0x00, 0x64, 'R', 'O', 'J',
                            0x00, 0x64, 'O', 'C', 'S', 0x00, 0x64, });
            soundEnabled = true;
            vibrationEnabled = true;

        } else {
            buffer = new byte[SRAM_AUDIO_PREFS_LENGTH];
            readSRamData(SRAM_AUDIO_PREFS_OFFSET, buffer);
            soundEnabled = (buffer[0] == 1);
            buffer = new byte[SRAM_VIBRA_PREFS_LENGTH];
            readSRamData(SRAM_VIBRA_PREFS_OFFSET, buffer);
            vibrationEnabled = (buffer[0] == 1);
        }
    }

    //
    void readSRamData(int offset, byte[] data) {
        offset += sramOffset;
        try {
            InputStream is = Connector.openInputStream("scratchpad:///0;pos="
                    + offset);
            is.read(data);
            is.close();
            is = null;
            System.gc();
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("Error reading data from sp at offset: "
                        + offset + " length: " + data.length);
                e.printStackTrace();
            }
            finished = true;
        }
    }

    //
    void writeSRam() {
        writeSRamData(SRAM_AUDIO_PREFS_OFFSET,
                new byte[] { soundEnabled ? (byte) 1 : 0 });
        writeSRamData(SRAM_VIBRA_PREFS_OFFSET,
                new byte[] { vibrationEnabled ? (byte) 1 : 0 });
        byte[] scData = new byte[SRAM_SCORES_LENGTH];
        for (int c = 0; c < 3; c++) {
            for (int d = 0; d < 3; d++) {
                scData[c * 3 + d] = (byte) highScoreName[c][d];
            }
            scData[9 + c * 2] = (byte) ((highScorePoints[c] & 0xFF00) >> 8);
            scData[10 + c * 2] = (byte) (highScorePoints[c] & 0xFF);
        }
        writeSRamData(SRAM_SCORES_OFFSET, scData);
    }

    //
    void writeSRamData(int offset, byte[] data) {
        offset += sramOffset;
        try {
            OutputStream os = Connector.openOutputStream("scratchpad:///0;pos="
                    + offset);
            os.write(data);
            os.close();
            os = null;
            System.gc();
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("Error writing data to sp at offset: "
                        + offset + " length: " + data.length);
                e.printStackTrace();
            }
            finished = true;
        }
    }

    //--------------------------------------------------------------------------
    // game
    //--------------------------------------------------------------------------

    // main state data
    static final byte STATE_SPLASH = 0;

    static final byte STATE_TITLE = 1;

    static final byte STATE_MENU = 2;

    static final byte STATE_INSTRUCTIONS = 3;

    static final byte STATE_INGAME = 4;

    static final byte STATE_SCORES = 5;

    static final byte STATE_ENTER_NAME = 6;

    static final byte STATE_ABOUT = 7;

    static final byte STATE_EXIT = 8;

    byte mainState;

    byte nextMainState;

    // splash data
    Image splashImage;

    // title data
    Image titleImage;

    // menu data
    static final byte MENU_OPTION_START_GAME = 0;

    static final byte MENU_OPTION_INSTRUCTIONS = 1;

    static final byte MENU_OPTION_SOUND = 2;

    static final byte MENU_OPTION_VIBRATION = 3;

    static final byte MENU_OPTION_ABOUT = 4;

    static final byte MENU_OPTION_EXIT = 5;

    static final byte MENU_OPTION_MIN = MENU_OPTION_START_GAME;

    static final byte MENU_OPTION_MAX = MENU_OPTION_EXIT;

    byte menuOption;

    static final byte MENU_MODE_MAIN = 0;

    static final byte MENU_MODE_PAUSE = 1;

    byte menuMode;

    static final int MENU_TEXT_COLOR = Graphics.getColorOfName(Graphics.RED);

    static final int MENU_TEXT_SELECTED = Graphics
            .getColorOfName(Graphics.WHITE);

    static final int MENU_TEXT_SHADOW = Graphics.getColorOfName(Graphics.BLACK);

    static final int MENU_TEXT_BACKGROUND = 0x602010;

    static final int MENU_FRAME_COLOR = 0x908010;

    static final int MENU_TITLE_TEXT = 20;

    static final int[] MENU_OPTIONS_TEXT = { 0, 6, 2, 3, 7, 9 };

    int[] menuOptionsIndexes = { 0, 2, 3, 5, 7 };

    int maxOptionTextWidth;

    // instructions data
    static final int INSTRUCTIONS_TEXT_COLOR = Graphics
            .getColorOfName(Graphics.WHITE);

    static final int INSTRUCTIONS_TEXT_SHADOW = Graphics
            .getColorOfName(Graphics.BLACK);

    static final int INSTRUCTIONS_TEXT_BACKGROUND = 0x602010;

    static final int INSTRUCTIONS_TEXT = 22;

    int maxInstructionsTextWidth;

    // in-game data
    static final byte INGAME_STATE_SENSEI = -1;

    static final byte INGAME_STATE_LEVEL_SETUP = 0;

    static final byte INGAME_STATE_LEVEL_START = 1;

    static final byte INGAME_STATE_PLAYING = 2;

    static final byte INGAME_STATE_LEVEL_COMPLETED = 3;

    static final byte INGAME_STATE_PLAYER_HIT = 4;

    static final byte INGAME_STATE_GAME_OVER = 5;

    byte inGameState;

    byte nextInGameState;

    Image scenery[];

    static final int SCENERY_BG_TOP = 0;

    static final int SCENERY_STAGE_COMPLETED = 1;

    static final int SCENERY_BG_WATER_START = 2;

    static final int SCENERY_BG_WATER_END = 4;

    static final int SCENERY_BRIDGE = 5;

    static final int SCENERY_SCROLL = 6;

    int waterIndex = SCENERY_BG_WATER_START;

    // scoreboard data
    short level;

    int score;

    static final int[] BASE_HIT_SCORE = { 10, 20, 15 };

    final int scoreLabel = 15;

    char[] scoreChars = { '0', '0', '0', '0' };

    static final int SCORE_CHARS_X_OFFSET = Font.getDefaultFont()._getBBoxWidth(
            "0000");

    static final int SCOREBOARD_TEXT_COLOR = Graphics
            .getColorOfName(Graphics.WHITE);

    static final int SCOREBOARD_TEXT_OUTLINE = Graphics
            .getColorOfName(Graphics.BLACK);

    // misc

    static final int SOFT_MENU_TEXT = 17;

    static final Random random = new Random(System.currentTimeMillis());

    static final int[][] TEXT_OUTLINE_OFFSET = { { 0, 1 }, { 0, -1 }, { 1, 0 },
            { -1, 0 } };

    char[] levelLabel;

    int levelLabelWidth;

    static final int LEVEL_LABEL_COLOR = Graphics
            .getColorOfName(Graphics.WHITE);

    static final int LEVEL_LABEL_OUTLINE = Graphics
            .getColorOfName(Graphics.BLACK);

    int floorY;

    static final int FLOOR_HEIGHT = 8;

    static final int FLOOR_COLOR = Graphics.getColorOfRGB(90, 50, 10);

    // player
    static final int PLAYER_ANIMATION_STANDING_LEFT = 0;

    static final int PLAYER_ANIMATION_WALKING_LEFT = 1;

    static final int PLAYER_ANIMATION_PUNCH_LEFT = 2;

    static final int PLAYER_ANIMATION_HIGH_KICK_LEFT = 3;

    static final int PLAYER_ANIMATION_LOW_KICK_LEFT = 4;

    static final int PLAYER_ANIMATION_STANDING_RIGHT = 5;

    static final int PLAYER_ANIMATION_WALKING_RIGHT = 6;

    static final int PLAYER_ANIMATION_PUNCH_RIGHT = 7;

    static final int PLAYER_ANIMATION_HIGH_KICK_RIGHT = 8;

    static final int PLAYER_ANIMATION_LOW_KICK_RIGHT = 9;

    static final int PLAYER_ANIMATION_HIT = 10;

    Image[][] playerFrame = new Image[11][];
    
    int playerWalkFrameAdd = 1;

    int playerX;

    //    int playerY;

    int playerFrameIndex;

    int playerAnimationIndex;

    static final int PLAYER_FACING_LEFT = 0;

    static final int PLAYER_FACING_RIGHT = 1;

    int playerFacing;

    static final int PLAYER_ATTACK_NONE = 0;

    static final int PLAYER_ATTACK_PUNCH = 1;

    static final int PLAYER_ATTACK_HIGH_KICK = 2;

    static final int PLAYER_ATTACK_LOW_KICK = 3;

    int playerAttack;

    static final int[] PLAYER_JUMP_Y_OFFSET = { 0, -4, -8, -11, -14, -16, -18,
            -19, -20, -21, -20, -19, -18, -16, -14, -11, -8, -4 };

    static final int[] BRIDGE_JUMP_BLEND = { 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8,
            8, 8, 8, 8, 8, 8, 8 };

    static final int[] PLAYER_JUMP_FRAME_INDEX = { 0, 1, 1, 1, 1, 2, 2, 2, 2,
            2, 2, 2, 2, 1, 1, 1, 1, 0 };

    static final int[] PLAYER_SWEEP_FRAME_INDEX = { 0, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0 };

    int kickDataOffsetIndex;

    int xDeltaKick;

    static final int[] PLAYER_ATTACK_KICK_HIT_FRAME = { 2, 1 };

    static final int[] PLAYER_ATTACK_HIT_Y_OFFSET = { 4, 4, 10 };

    // sensei
    Image sensei;

    Image senseiText;

    Image senseiFloor;

    // exit
    static final int EXIT_TEXT = 21;

    static final int EXIT_TEXT_COLOR = Graphics.getColorOfName(Graphics.WHITE);

    static final int EXIT_TEXT_SHADOW = Graphics.getColorOfName(Graphics.BLACK);

    static final int EXIT_TEXT_BACKGROUND = 0x602010;

    static final int EXIT_FRAME_COLOR = 0x908010;

    // watermelons
    static final int WATERMELON_TYPE_BIG = 0;

    static final int WATERMELON_TYPE_SMALL = 0;

    static final int WATERMELON_NUMBER = 5;

    Image[][] watermelonFrame;

    int[] watermelonFrameIndex = new int[WATERMELON_NUMBER];

    int[] watermelonType = new int[WATERMELON_NUMBER];

    int[] watermelonX = new int[WATERMELON_NUMBER];

    int[] watermelonY = new int[WATERMELON_NUMBER];

    int[] watermelonSpeed = new int[WATERMELON_NUMBER];

    int[] watermelonWind = new int[WATERMELON_NUMBER];

    boolean[] watermelonHit = new boolean[WATERMELON_NUMBER];

    static final int MAX_WATERMELON_PIECES = 5;

    int[][] wmpX = new int[WATERMELON_NUMBER][MAX_WATERMELON_PIECES];

    int[][] wmpY = new int[WATERMELON_NUMBER][MAX_WATERMELON_PIECES];

    int[][] wmpIY = new int[WATERMELON_NUMBER][MAX_WATERMELON_PIECES];

    int[][] wmpIX = new int[WATERMELON_NUMBER][MAX_WATERMELON_PIECES];

    int[][] wmpU = new int[WATERMELON_NUMBER][MAX_WATERMELON_PIECES];

    int[][] wmpF = new int[WATERMELON_NUMBER][MAX_WATERMELON_PIECES];

    boolean[][] wmpV = new boolean[WATERMELON_NUMBER][MAX_WATERMELON_PIECES];

    // scores table
    char[][] highScoreName = new char[][] { { 'M', 'I', 'C' },
            { 'R', 'O', 'J' }, { 'O', 'C', 'S' } };

    short[] highScorePoints = new short[] { 100, 100, 100 };

    int newHighScorePos;

    int highScoreTitle = 11;

    static final int HIGHSCORES_HEADER_COLOR = 0xFFFFFF;

    static final int HIGHSCORES_SHADOW = 0x000000;

    static final int[] HIGHSCORES_COLOR_CYCLE = { 0xFF0000, 0xFF00, 0xFF,
            0xFFFF00, 0xFFFF, 0xFF00FF };

    int highScoresColorCycleIndex;

    static final int SOFTTEXT_OK = 18;

    static final int SOFTTEXT_CANCEL = 19;

    static final int HIGHSCORE_COLUMN_WIDTH = Font.getDefaultFont()
            .getBBoxWidth("0000");

    char[] highScoresPointsChars = new char[4];

    TextBox textBox = new TextBox("TSA", 3, 1, TextBox.DISPLAY_ANY);

    Label label1;

    Label label2;

    Panel enterNamePanel = new Panel();

    //
    String[][] textData;

    //

    // main game logic update
    void updateLogic() {
        switch (mainState) {
            case STATE_SPLASH:
                updateSplashState();
                break;
            case STATE_TITLE:
                updateTitleState();
                break;
            case STATE_MENU:
                updateMenuState();
                break;
            case STATE_INSTRUCTIONS:
                updateInstructionsState();
                break;
            case STATE_INGAME:
                updateInGameState();
                break;
            case STATE_SCORES:
                updateScoresState();
                break;
            case STATE_ENTER_NAME:
                updateEnterName();
                break;
            case STATE_ABOUT:
                updateAboutState();
                break;
            case STATE_EXIT:
                updateExitState();
                break;
        }
        if (mainState != nextMainState) {
            if (mainState == STATE_TITLE) {
                titleImage = null;
            }
            mainState = nextMainState;
            if (DEBUG) {
                System.out.println("STATE CHANGED TO: " + mainState);
            }
            System.gc();
        }
    }

    //
    

    // splash state logic
    void updateSplashState() {
        Image tmpImg[];        
        ProgBarINI(100,8,31,160);
        ProgBarSET(0,20);
        if (FS_Create()) {
            terminate();
        }                     
        //
        //textData = textosCreate(loadFile("/textos.txt"));
        textData = textosCreate(FS_LoadFile(18, 0));
        ProgBarADD();
        //
        //titleImage = loadImage(loadFile("/1_title.img"))[0];
        titleImage = FS_LoadImage(1, 0);
        ProgBarADD();
        //
        //scenery = loadImage(loadFile("/3_scenery.img"));
        scenery = FS_LoadImage(2);
        ProgBarADD();
        //System.out.println(scenery.length);
        //
        //playerFrame[PLAYER_ANIMATION_STANDING_LEFT] =
        // loadImage(loadFile("/20_player_standing_left.img"));
        //        playerFrame[PLAYER_ANIMATION_WALKING_LEFT] =
        // loadImage(loadFile("/21_player_walking_left.img"));
        //        playerFrame[PLAYER_ANIMATION_PUNCH_LEFT] =
        // loadImage(loadFile("/22_player_punch_left.img"));
        //        playerFrame[PLAYER_ANIMATION_HIGH_KICK_LEFT] =
        // loadImage(loadFile("/23_player_high_kick_left.img"));
        //        playerFrame[PLAYER_ANIMATION_LOW_KICK_LEFT] =
        // loadImage(loadFile("/24_player_low_kick_left.img"));
        //        playerFrame[PLAYER_ANIMATION_STANDING_RIGHT] =
        // loadImage(loadFile("/25_player_standing_right.img"));
        //        playerFrame[PLAYER_ANIMATION_WALKING_RIGHT] =
        // loadImage(loadFile("/26_player_walking_right.img"));
        //        playerFrame[PLAYER_ANIMATION_PUNCH_RIGHT] =
        // loadImage(loadFile("/27_player_punch_right.img"));
        //        playerFrame[PLAYER_ANIMATION_HIGH_KICK_RIGHT] =
        // loadImage(loadFile("/28_player_high_kick_right.img"));
        //        playerFrame[PLAYER_ANIMATION_LOW_KICK_RIGHT] =
        // loadImage(loadFile("/29_player_low_kick_right.img"));
        //        playerFrame[PLAYER_ANIMATION_HIT] =
        // loadImage(loadFile("/30_player_hit.img"));
        for (int c = PLAYER_ANIMATION_STANDING_LEFT; c <= PLAYER_ANIMATION_HIT; c++) {
            playerFrame[c] = FS_LoadImage(6 + c);
            ProgBarADD();
        }

        //
        //tmpImg = loadImage(loadFile("/4_sensei.img"));
        //        sensei = tmpImg[0];
        //        senseiText = tmpImg[1];
        //        senseiFloor = tmpImg[2];
        sensei = FS_LoadImage(3, 0);
        ProgBarADD();
        senseiText = FS_LoadImage(3, 1);
        ProgBarADD();
        senseiFloor = FS_LoadImage(3, 2);
        ProgBarADD();
        //
        //        watermelonFrame = new Image[][] {
        //                loadImage(loadFile("/10_big_watermelon.img")),
        //                loadImage(loadFile("/11_small_watermelon.img")) };
        watermelonFrame = new Image[][] { FS_LoadImage(4), FS_LoadImage(5) };
        ProgBarADD();
        //
        for (int c = MENU_OPTION_MIN; c < MENU_OPTION_MAX; c++) {
            int txtWidth = font.getBBoxWidth(textData[MENU_OPTIONS_TEXT[c]][0]);
            if (txtWidth > maxOptionTextWidth) {
                maxOptionTextWidth = txtWidth;
            }
        }
        for (int c = 0; c < textData[INSTRUCTIONS_TEXT].length; c++) {
            int txtWidth = font.getBBoxWidth(textData[INSTRUCTIONS_TEXT][c]);
            if (txtWidth > maxInstructionsTextWidth) {
                maxInstructionsTextWidth = txtWidth;
            }
        }
        menuOptionsIndexes[2] = soundEnabled ? 3 : 4;
        menuOptionsIndexes[3] = vibrationEnabled ? 5 : 6;
        SoundINI();
        ProgBarADD();
        nextMainState = STATE_TITLE;
        if (soundEnabled) {
            SoundSET(AUDIO_INTRO, 0);
        }
        //splashImage = null;
        enterNamePanel.setSoftLabel(Frame.SOFT_KEY_1, textData[SOFTTEXT_OK][0]);
        label1 = new Label(textData[13][0], Label.LEFT);
        label2 = new Label(textData[14][0], Label.LEFT);
        enterNamePanel.add(label1);
        enterNamePanel.add(textBox);
        enterNamePanel.add(label2);
        enterNamePanel.setTitle(textData[12][0]);
        enterNamePanel.setSoftKeyListener(this);
        levelLabel = textData[16][0].toCharArray();
        levelLabelWidth = Font.getDefaultFont().getBBoxWidth(textData[16][0]);
        initialized = true;
        ProgBarEND();
        System.gc();
        customDelayStartTime = System.currentTimeMillis();        
    }

    //
    void updateScoresState() {
        updateWater();
        if (currentTick % (8 / speedMultiplier) == 0) {
            highScoresColorCycleIndex++;
            if (highScoresColorCycleIndex >= HIGHSCORES_COLOR_CYCLE.length) {
                highScoresColorCycleIndex = 0;
            }
        }
        if (keyPressed
                || System.currentTimeMillis() - customDelayStartTime > 10000) {
            nextMainState = STATE_MENU;
            customDelayStartTime = System.currentTimeMillis();
            keyPressed = false;
        }
    }

    //
    void updateExitState() {
        if (keyPressed) {
            switch (menuMode) {
                case MENU_MODE_MAIN:
                    if (keyCode == Display.KEY_SOFT2) {
                        finished = true;
                    } else if (keyCode == Display.KEY_SOFT1) {
                        nextMainState = STATE_MENU;
                    }
                    break;
                case MENU_MODE_PAUSE:
                    if (keyCode == Display.KEY_SOFT2) {
                        menuMode = MENU_MODE_MAIN;
                        for (newHighScorePos = 0; newHighScorePos < highScorePoints.length
                                && score < highScorePoints[newHighScorePos]; newHighScorePos++)
                            ;
                        if (newHighScorePos < highScorePoints.length) {
                            nextMainState = STATE_ENTER_NAME;
                            Display.setCurrent(enterNamePanel);
                            textBox.requestFocus();
                        } else {
                            nextMainState = STATE_MENU;
                        }
                    } else if (keyCode == Display.KEY_SOFT1) {
                        nextMainState = STATE_MENU;
                    }
                    break;
            }
            if (keyCode == Display.KEY_SOFT1 || keyCode == Display.KEY_SOFT2) {
                gameCanvas.setSoftLabel(Canvas.SOFT_KEY_1, null);
                gameCanvas.setSoftLabel(Canvas.SOFT_KEY_2, null);
            }
            keyPressed = false;
        }
    }

    // title state logic
    void updateTitleState() {
        if (keyPressed
                || System.currentTimeMillis() - customDelayStartTime > 10000) {
            keyPressed = false;
            customDelayStartTime = System.currentTimeMillis();
            nextMainState = STATE_MENU;
        }
    }

    // menu state logic
    void updateMenuState() {
        if (keyPressed) {
            switch (keyCode) {
                case Display.KEY_UP:
                case Display.KEY_2:
                    menuOption--;
                    if (menuOption < MENU_OPTION_MIN) {
                        menuOption = MENU_OPTION_MAX;
                    }
                    break;
                case Display.KEY_DOWN:
                case Display.KEY_8:
                    menuOption++;
                    if (menuOption > MENU_OPTION_MAX) {
                        menuOption = MENU_OPTION_MIN;
                    }
                    break;
                case Display.KEY_SELECT:
                case Display.KEY_ASTERISK:
                case Display.KEY_SOFT2:
                    switch (menuOption) {
                        case MENU_OPTION_START_GAME:
                            switch (menuMode) {
                                case MENU_MODE_MAIN:
                                    nextMainState = STATE_INGAME;
                                    inGameState = INGAME_STATE_SENSEI;
                                    nextInGameState = INGAME_STATE_SENSEI;
                                    resetGame();
                                    menuMode = MENU_MODE_PAUSE;
                                    customDelayStartTime = System
                                            .currentTimeMillis();
                                    break;
                                case MENU_MODE_PAUSE:
                                    gameCanvas.setSoftLabel(Canvas.SOFT_KEY_1,
                                            textData[SOFT_MENU_TEXT][0]);
                                    nextMainState = STATE_INGAME;
                                    break;
                            }
                            SoundRES();
                            break;
                        case MENU_OPTION_INSTRUCTIONS:
                            nextMainState = STATE_INSTRUCTIONS;
                            break;
                        case MENU_OPTION_SOUND:
                            soundEnabled = !soundEnabled;
                            menuOptionsIndexes[2] = soundEnabled ? 3 : 4;
                            if (!soundEnabled) {
                                SoundRES();
                            } else {
                                SoundSET(AUDIO_INTRO, 0);
                            }
                            break;
                        case MENU_OPTION_VIBRATION:
                            vibrationEnabled = !vibrationEnabled;
                            menuOptionsIndexes[3] = vibrationEnabled ? 5 : 6;
                            break;
                        case MENU_OPTION_ABOUT:
                            nextMainState = STATE_ABOUT;
                            break;
                        case MENU_OPTION_EXIT:
                        case Display.KEY_SOFT1:
                            gameCanvas.setSoftLabel(Canvas.SOFT_KEY_1,
                                    textData[SOFTTEXT_CANCEL][0]);
                            gameCanvas.setSoftLabel(Canvas.SOFT_KEY_2,
                                    textData[SOFTTEXT_OK][0]);
                            nextMainState = STATE_EXIT;
                            break;
                    }
                    break;
            }
            customDelayStartTime = System.currentTimeMillis();
            keyPressed = false;
        }
        updateWater();
        if (menuMode == MENU_MODE_MAIN
                && System.currentTimeMillis() - customDelayStartTime > 10000) {
            nextMainState = STATE_SCORES;
            customDelayStartTime = System.currentTimeMillis();
        }
    }

    // instructions state logic
    void updateInstructionsState() {
        if (keyPressed) {
            keyPressed = false;
            customDelayStartTime = System.currentTimeMillis();
            nextMainState = STATE_MENU;
        }
        updateWater();
    }

    // about state logic
    void updateAboutState() {
        if (keyPressed) {
            keyPressed = false;
            customDelayStartTime = System.currentTimeMillis();
            nextMainState = STATE_MENU;
        }
        updateWater();
    }

    // in-game state logic
    void updateInGameState() {
        if (inGameState != nextInGameState) {
            inGameState = nextInGameState;
        }
        switch (inGameState) {
            case INGAME_STATE_SENSEI:
                if ((keyPressed && System.currentTimeMillis()
                        - customDelayStartTime > 1000)
                        || System.currentTimeMillis() - customDelayStartTime > 8000) {
                    nextInGameState = INGAME_STATE_LEVEL_SETUP;
                    keyPressed = false;
                }
                break;
            case INGAME_STATE_LEVEL_SETUP:
                nextInGameState = INGAME_STATE_LEVEL_START;
                keyPressed = false;
                updateLevelChars();
                customDelayStartTime = System.currentTimeMillis();
                break;
            case INGAME_STATE_LEVEL_START:
                if ((keyPressed && System.currentTimeMillis()
                        - customDelayStartTime > 1000)
                        || System.currentTimeMillis() - customDelayStartTime > 2000) {
                    nextInGameState = INGAME_STATE_PLAYING;
                    gameCanvas.setSoftLabel(Canvas.SOFT_KEY_1,
                            textData[SOFT_MENU_TEXT][0]);
                    keyPressed = false;
                }
                break;
            case INGAME_STATE_PLAYING:
                if (keyPressed) {
                    switch (keyCode) {
                        case Display.KEY_SOFT1:
                            nextMainState = STATE_MENU;
                            gameCanvas.setSoftLabel(Canvas.SOFT_KEY_1, null);
                            keyPressed = false;
                            break;
                    }
                }
                updatePlayer();
                updateWatermelons();
                updateWatermelonPieces();
                checkCollisions();
                break;
            case INGAME_STATE_LEVEL_COMPLETED:
                if ((keyPressed && System.currentTimeMillis()
                        - customDelayStartTime > 1000)
                        || System.currentTimeMillis() - customDelayStartTime > 5000) {
                    resetPlayer();
                    resetWatermelons();
                    setPlayerAnimation(PLAYER_ANIMATION_STANDING_LEFT);
                    nextInGameState = INGAME_STATE_LEVEL_SETUP;
                    keyPressed = false;
                    level++;
                }
                updateWatermelons();
                updateWatermelonPieces();
                break;
            case INGAME_STATE_PLAYER_HIT:
                updateWatermelons();
                updateWatermelonPieces();
                if (kickDataOffsetIndex > 0) {
                    kickDataOffsetIndex++;
                    if (kickDataOffsetIndex >= PLAYER_JUMP_Y_OFFSET.length) {
                        kickDataOffsetIndex = 0;
                    }
                }
                if (currentTick % 4 == 0) {
                    playerFrameIndex++;
                    if (playerFrameIndex >= playerFrame[playerAnimationIndex].length) {
                        playerFrameIndex = 0;
                    }
                }
                if ((keyPressed && System.currentTimeMillis()
                        - customDelayStartTime > 1000)
                        || System.currentTimeMillis() - customDelayStartTime > 4000) {
                    keyPressed = false;
                    //
                    resetPlayer();
                    resetWatermelons();
                    setPlayerAnimation(PLAYER_ANIMATION_STANDING_LEFT);
                    nextInGameState = INGAME_STATE_LEVEL_SETUP;
                }
                break;
            case INGAME_STATE_GAME_OVER:
                break;
        }
        updateWater();
    }

    //
    void checkCollisions() {
        int pw = playerFrame[playerAnimationIndex][playerFrameIndex].getWidth();
        int ph = playerFrame[playerAnimationIndex][playerFrameIndex]
                .getHeight();
        int px = playerX - pw / 2;
        int py = floorY
                - ph
                + (playerAttack == PLAYER_ATTACK_HIGH_KICK ? PLAYER_JUMP_Y_OFFSET[kickDataOffsetIndex]
                        : 0);
        boolean done = false;
        boolean playerMissed;
        for (int c = 0; c < WATERMELON_NUMBER && !done; c++) {
            if (!watermelonHit[c]) {
                int mw = watermelonFrame[watermelonType[c]][watermelonFrameIndex[c]]
                        .getWidth();
                int mh = watermelonFrame[watermelonType[c]][watermelonFrameIndex[c]]
                        .getHeight();
                int mx = watermelonX[c] - mw / 2;
                int my = watermelonY[c] - mh / 2;
                done = false;
                switch (playerAttack) {
                    case PLAYER_ATTACK_PUNCH:
                        if (intersects(px
                                + (playerFacing == PLAYER_FACING_RIGHT ? pw - 4
                                        : -3), py
                                + PLAYER_ATTACK_HIT_Y_OFFSET[0], 6, 6, mx, my,
                                mw, mh)
                                && isPlayerFacingwatermelon(c)) {
                            watermelonHit(c);
                            done = true;
                        }
                        break;
                    case PLAYER_ATTACK_HIGH_KICK:
                        if (intersects(px
                                + (playerFacing == PLAYER_FACING_RIGHT ? pw - 2
                                        : -2), py
                                + PLAYER_ATTACK_HIT_Y_OFFSET[1], 6, 6, mx, my,
                                mw, mh)
                                && playerFrameIndex == PLAYER_ATTACK_KICK_HIT_FRAME[0]
                                && isPlayerFacingwatermelon(c)) {
                            watermelonHit(c);
                            done = true;
                        }
                        break;
                    case PLAYER_ATTACK_LOW_KICK:
                        if (intersects(px
                                + (playerFacing == PLAYER_FACING_RIGHT ? pw - 2
                                        : -2), py
                                + PLAYER_ATTACK_HIT_Y_OFFSET[2], 6, 6, mx, my,
                                mw, mh)
                                && playerFrameIndex == PLAYER_ATTACK_KICK_HIT_FRAME[1]
                                && isPlayerFacingwatermelon(c)) {
                            watermelonHit(c);
                            done = true;
                        }
                        break;
                }
                if (!done
                        && intersects(px + 4, py + 2, pw - 8, ph - 4, mx + 1,
                                my + 1, mw - 2, mh - 2)) {
                    playerHit();
                    done = true;
                }
            }
        }
    }

    boolean isPlayerFacingwatermelon(int index) {
        return ((playerFacing == PLAYER_FACING_LEFT && playerX > watermelonX[index]) || (playerFacing == PLAYER_FACING_RIGHT && playerX < watermelonX[index]));
    }

    //
    void watermelonHit(int index) {
        SoundSET(AUDIO_BEAT1 + playerAttack - 1, 1);
        score += BASE_HIT_SCORE[playerAttack - 1] + 5 * watermelonType[index];
        updateScoreChars(score, scoreChars);
        if (score / 200 >= level) {
            SoundSET(AUDIO_ENDLEVEL, 1);
            nextInGameState = INGAME_STATE_LEVEL_COMPLETED;
            customDelayStartTime = System.currentTimeMillis();
        }
        watermelonHit[index] = true;
        VibraSET(150);
        explodeWatermelon(index);
    }

    //
    void playerHit() {
        SoundSET(AUDIO_DEAD, 1);
        nextInGameState = INGAME_STATE_PLAYER_HIT;
        setPlayerAnimation(PLAYER_ANIMATION_HIT);
        //        score /= 2;
        //        if (score < (level - 1) * 200) {
        //            score = (level - 1) * 200;
        //        }
        updateScoreChars(score, scoreChars);
        keyPressed = false;
        VibraSET(500);
        customDelayStartTime = System.currentTimeMillis();
    }

    //
    void updateWatermelons() {
        for (int c = 0; c < WATERMELON_NUMBER; c++) {
            watermelonY[c] += watermelonSpeed[c] * speedMultiplier;
            if (currentTick
                    % ((13 - level) < speedMultiplier ? speedMultiplier
                            : (13 - level) / speedMultiplier) == 0) {
                watermelonX[c] += watermelonWind[c];
            }
            //            if (watermelonHit[c] && (currentTick % 2 == 0)) {
            //                watermelonFrameIndex[c]++;
            //            }
            if (watermelonY[c] > canvasHeight + 16) {
                if (!watermelonHit[c]) {
                    if (floorY > canvasHeight / 2) {
                        floorY--;
                    }
                    resetWatermelon(c);
                } else {
                    boolean ok = false;
                    for (int d = 0; d < MAX_WATERMELON_PIECES; d++) {
                        ok = ok | wmpV[c][d];
                    }
                    if (!ok) {
                        resetWatermelon(c);
                    }
                }
            }
        }
    }

    //
    void updatePlayer() {
        if (keyPressed && playerAttack == PLAYER_ATTACK_NONE) {
            switch (keyCode) {
                case Display.KEY_LEFT:
                case Display.KEY_4:
                    if (playerX > 0) {
                        playerX -= 1 * speedMultiplier;
                        setPlayerAnimation(PLAYER_ANIMATION_WALKING_LEFT);
                        playerFacing = PLAYER_FACING_LEFT;
                    }
                    break;
                case Display.KEY_RIGHT:
                case Display.KEY_6:
                    if (playerX < canvasWidth) {
                        playerX += 1 * speedMultiplier;
                        setPlayerAnimation(PLAYER_ANIMATION_WALKING_RIGHT);
                        playerFacing = PLAYER_FACING_RIGHT;
                    }
                    break;
                case Display.KEY_7:
                    setPlayerAnimation(PLAYER_ANIMATION_LOW_KICK_LEFT);
                    playerFacing = PLAYER_FACING_LEFT;
                    playerAttack = PLAYER_ATTACK_LOW_KICK;
                    kickDataOffsetIndex = 0;
                    xDeltaKick = -1 * speedMultiplier;
                    keyPressed = false;
                    break;
                case Display.KEY_9:
                    setPlayerAnimation(PLAYER_ANIMATION_LOW_KICK_RIGHT);
                    playerFacing = PLAYER_FACING_RIGHT;
                    playerAttack = PLAYER_ATTACK_LOW_KICK;
                    kickDataOffsetIndex = 0;
                    xDeltaKick = 1 * speedMultiplier;
                    keyPressed = false;
                    break;
                case Display.KEY_DOWN:
                case Display.KEY_8:
                    setPlayerAnimation(playerFacing == PLAYER_FACING_LEFT ? PLAYER_ANIMATION_LOW_KICK_LEFT
                            : PLAYER_ANIMATION_LOW_KICK_RIGHT);
                    playerAttack = PLAYER_ATTACK_LOW_KICK;
                    kickDataOffsetIndex = 0;
                    xDeltaKick = 0;
                    keyPressed = false;
                    break;
                case Display.KEY_1:
                    setPlayerAnimation(PLAYER_ANIMATION_HIGH_KICK_LEFT);
                    playerAttack = PLAYER_ATTACK_HIGH_KICK;
                    playerFacing = PLAYER_FACING_LEFT;
                    xDeltaKick = -2;
                    kickDataOffsetIndex = 0;
                    keyPressed = false;
                    break;
                case Display.KEY_3:
                    setPlayerAnimation(PLAYER_ANIMATION_HIGH_KICK_RIGHT);
                    playerAttack = PLAYER_ATTACK_HIGH_KICK;
                    playerFacing = PLAYER_FACING_RIGHT;
                    xDeltaKick = 2;
                    kickDataOffsetIndex = 0;
                    keyPressed = false;
                    break;
                case Display.KEY_UP:
                case Display.KEY_2:
                    setPlayerAnimation(playerFacing == PLAYER_FACING_LEFT ? PLAYER_ANIMATION_HIGH_KICK_LEFT
                            : PLAYER_ANIMATION_HIGH_KICK_RIGHT);
                    playerAttack = PLAYER_ATTACK_HIGH_KICK;
                    xDeltaKick = 0;
                    kickDataOffsetIndex = 0;
                    keyPressed = false;
                    break;
                case Display.KEY_SELECT:
                case Display.KEY_5:
                    setPlayerAnimation(playerFacing == PLAYER_FACING_LEFT ? PLAYER_ANIMATION_PUNCH_LEFT
                            : PLAYER_ANIMATION_PUNCH_RIGHT);
                    playerAttack = PLAYER_ATTACK_PUNCH;
                    keyPressed = false;
                    break;
                default:
                    break;
            }
        } else if (playerAttack == PLAYER_ATTACK_NONE) {
            switch (keyCode) {
                case Display.KEY_LEFT:
                case Display.KEY_4:
                    setPlayerAnimation(PLAYER_ANIMATION_STANDING_LEFT);
                    break;
                case Display.KEY_RIGHT:
                case Display.KEY_6:
                    setPlayerAnimation(PLAYER_ANIMATION_STANDING_RIGHT);
                    break;

            }
        }
        switch (playerAttack) {
            case PLAYER_ATTACK_HIGH_KICK:
                if (playerX + xDeltaKick > 0
                        && playerX + xDeltaKick < canvasWidth) {
                    playerX += xDeltaKick;
                }
                kickDataOffsetIndex++;
                if (kickDataOffsetIndex >= PLAYER_JUMP_Y_OFFSET.length) {
                    playerAttack = PLAYER_ATTACK_NONE;
                    kickDataOffsetIndex = 0;
                    setPlayerAnimation(playerFacing == PLAYER_FACING_LEFT ? PLAYER_ANIMATION_STANDING_LEFT
                            : PLAYER_ANIMATION_STANDING_RIGHT);
                } else {
                    playerFrameIndex = PLAYER_JUMP_FRAME_INDEX[kickDataOffsetIndex];
                }
                break;
            case PLAYER_ATTACK_LOW_KICK:
                if (playerX + xDeltaKick > 0
                        && playerX + xDeltaKick < canvasWidth) {
                    playerX += xDeltaKick;
                }
                kickDataOffsetIndex++;
                if (kickDataOffsetIndex >= PLAYER_SWEEP_FRAME_INDEX.length) {
                    playerAttack = PLAYER_ATTACK_NONE;
                    kickDataOffsetIndex = 0;
                    setPlayerAnimation(playerFacing == PLAYER_FACING_LEFT ? PLAYER_ANIMATION_STANDING_LEFT
                            : PLAYER_ANIMATION_STANDING_RIGHT);
                } else {
                    playerFrameIndex = PLAYER_SWEEP_FRAME_INDEX[kickDataOffsetIndex];
                }
                break;
            case PLAYER_ATTACK_PUNCH:
                if (currentTick % 2 * speedMultiplier == 0) {
                    playerFrameIndex++;
                    if (playerFrameIndex >= playerFrame[playerAnimationIndex].length) {
                        playerAttack = PLAYER_ATTACK_NONE;
                        setPlayerAnimation(playerFacing == PLAYER_FACING_LEFT ? PLAYER_ANIMATION_STANDING_LEFT
                                : PLAYER_ANIMATION_STANDING_RIGHT);
                    }
                }
                break;
            default:
                if (currentTick % (6 / speedMultiplier) == 0) {
                    playerFrameIndex++;
                    if (playerFrameIndex >= playerFrame[playerAnimationIndex].length) {
                        playerFrameIndex = 0;
                    }
                }
                break;
        }
    }

    //
    void setPlayerAnimation(int index) {
        if (playerAnimationIndex != index) {
            playerFrameIndex = 0;
            playerAnimationIndex = index;
        }
    }

    //
    void updateWater() {
        if (currentTick % 3 == 0) {
            waterIndex++;
            if (waterIndex > SCENERY_BG_WATER_END) {
                waterIndex = SCENERY_BG_WATER_START;
            }
        }
    }

    // reset game values (new game)
    void resetGame() {
        score = 0;
        level = 1;
        resetPlayer();
        resetWatermelons();
        updateScoreChars(score, scoreChars);
    }

    //
    void resetPlayer() {
        playerX = canvasWidth / 2;
        floorY = canvasHeight - FLOOR_HEIGHT;
        playerAttack = PLAYER_ATTACK_NONE;
    }

    //
    void resetWatermelons() {
        for (int c = 0; c < WATERMELON_NUMBER; c++) {
            resetWatermelon(c);
            watermelonY[c] -= c * 64;
        }
    }

    //
    void resetWatermelon(int index) {
        watermelonY[index] = -(Math.abs(random.nextInt() % 96) + 64);
        watermelonX[index] = Math.abs(random.nextInt() % canvasWidth);
        watermelonFrameIndex[index] = 0;
        watermelonHit[index] = false;
        watermelonType[index] = Math.abs(random.nextInt() % 2);
        watermelonSpeed[index] = 1 + (level > 1 ? Math.abs(random.nextInt()
                % level) : 0);
        if (level >= 5) {
            watermelonWind[index] = random.nextInt() % 2;
        } else {
            watermelonWind[index] = 0;
        }
    }

    //
    void updateEnterName() {
    }

    void explodeWatermelon(int index) {
        for (int c = 0; c < MAX_WATERMELON_PIECES; c++) {
            wmpX[index][c] = watermelonX[index];
            wmpY[index][c] = watermelonY[index];
            if (playerX > watermelonX[index]) {
                wmpIX[index][c] = (playerAttack == PLAYER_ATTACK_HIGH_KICK ? -2
                        : -1)
                        - c / 2;
            } else if (playerX < watermelonX[index]) {
                wmpIX[index][c] = (playerAttack == PLAYER_ATTACK_HIGH_KICK ? 2
                        : 1)
                        - c / 2;
            } else {
                wmpIX[index][c] = 0;
            }
            wmpIY[index][c] = -2 - c;
            wmpU[index][c] = Math.abs(random.nextInt() % 4) + 1;
            wmpF[index][c] = 1;
            wmpV[index][c] = true;
        }
    }

    void updateWatermelonPieces() {
        for (int index = 0; index < MAX_WATERMELON_PIECES; index++) {
            for (int c = 0; c < MAX_WATERMELON_PIECES; c++) {
                if (wmpV[index][c]) {
                    wmpX[index][c] += wmpIX[index][c];
                    wmpY[index][c] += wmpIY[index][c];
                    if (currentTick % wmpU[index][c] == 0) {
                        wmpF[index][c]++;
                        if (wmpF[index][c] > 8) {
                            wmpF[index][c] = 1;
                        }
                        if (wmpIY[index][c] < 6) {
                            wmpIY[index][c]++;
                        }
                    }
                    if (wmpX[index][c] > canvasWidth + 64
                            || wmpX[index][c] < -64
                            || wmpY[index][c] > canvasHeight + 64) {
                        wmpV[index][c] = false;
                    }
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    // display
    //--------------------------------------------------------------------------
    void updateDisplay() {
        switch (mainState) {
            case STATE_SPLASH:
                break;
            case STATE_TITLE:
                drawTitleState();
                break;
            case STATE_MENU:
                drawMenuState();
                break;
            case STATE_INSTRUCTIONS:
                drawInstructionsState();
                break;
            case STATE_INGAME:
                drawInGameState();
                break;
            case STATE_SCORES:
                drawScoresTable();
                break;
            case STATE_EXIT:
                drawExitState();
                break;
            case STATE_ABOUT:
                drawAboutState();
                break;
            case STATE_ENTER_NAME:
                drawEnterName();
                break;
        }
    }

    //
    void drawEnterName() {

    }

    //
    void drawTitleState() {
        drawImageCentered(titleImage);
    }

    //
    void drawMenuState() {
        int y = 32 + fontHeight;
        drawScenery();
        //        graphics.setColor(MENU_TEXT_BACKGROUND);
        //        graphics.fillRect(8, 8, canvasWidth - 16, canvasHeight - 16);
        //        graphics.setColor(MENU_FRAME_COLOR);
        //        graphics.drawRect(8, 8, canvasWidth - 16, canvasHeight - 16);
        //        graphics.drawRect(10, 10, canvasWidth - 20, canvasHeight - 20);
        graphics.drawImage(scenery[SCENERY_SCROLL], canvasWidth / 2
                - scenery[SCENERY_SCROLL].getWidth() / 2, canvasHeight / 2
                - scenery[SCENERY_SCROLL].getHeight() / 2);
        drawCenteredText(textData[MENU_TITLE_TEXT][menuMode], 0xFFFFFF,
                0x000000, y);
        y += 10;
        int d;
        int e;
        for (int c = MENU_OPTION_MIN; c <= MENU_OPTION_MAX; c++) {
            d = MENU_OPTIONS_TEXT[c];
            e = 0;
            y += fontHeight + 2;
            switch (c) {
                case MENU_OPTION_START_GAME:
                    d += menuMode;
                    break;
                case MENU_OPTION_SOUND:
                    if (soundEnabled) {
                        e = 1;
                    }
                    break;
                case MENU_OPTION_VIBRATION:
                    if (vibrationEnabled) {
                        e = 1;
                    }
                    break;
            }
            drawCenteredText(textData[d][e],
                    menuOption == c ? MENU_TEXT_SELECTED : MENU_TEXT_COLOR,
                    MENU_TEXT_SHADOW, y);
        }
    }

    //
    void drawInstructionsState() {
        int y = 24 + fontHeight;
        drawScenery();
        graphics.drawImage(scenery[SCENERY_SCROLL], canvasWidth / 2
                - scenery[SCENERY_SCROLL].getWidth() / 2, canvasHeight / 2
                - scenery[SCENERY_SCROLL].getHeight() / 2);
        //        graphics.setColor(INSTRUCTIONS_TEXT_BACKGROUND);
        //        graphics.fillRect(4, 8, canvasWidth - 8, canvasHeight - 16);
        //        graphics.setColor(MENU_FRAME_COLOR);
        //        graphics.drawRect(4, 8, canvasWidth - 8, canvasHeight - 16);
        //        graphics.drawRect(6, 10, canvasWidth - 12, canvasHeight - 20);
        //int y = 10;
        for (int c = 0; c < textData[INSTRUCTIONS_TEXT].length; c++) {
            y += fontHeight + 2;
            drawCenteredText(textData[INSTRUCTIONS_TEXT][c],
                    INSTRUCTIONS_TEXT_COLOR, INSTRUCTIONS_TEXT_SHADOW, y);
        }
    }

    //
    void drawAboutState() {
        int y = 24 + fontHeight;
        drawScenery();
        graphics.drawImage(scenery[SCENERY_SCROLL], canvasWidth / 2
                - scenery[SCENERY_SCROLL].getWidth() / 2, canvasHeight / 2
                - scenery[SCENERY_SCROLL].getHeight() / 2);
        //        graphics.setColor(INSTRUCTIONS_TEXT_BACKGROUND);
        //        graphics.fillRect(4, 8, canvasWidth - 8, canvasHeight - 16);
        //        graphics.setColor(MENU_FRAME_COLOR);
        //        graphics.drawRect(4, 8, canvasWidth - 8, canvasHeight - 16);
        //        graphics.drawRect(6, 10, canvasWidth - 12, canvasHeight - 20);
        for (int c = 0; c < textData[23].length; c++) {
            y += fontHeight + 2;
            drawCenteredText(textData[23][c], INSTRUCTIONS_TEXT_COLOR,
                    INSTRUCTIONS_TEXT_SHADOW, y);
        }
    }

    //
    void drawInGameState() {
        if (inGameState != INGAME_STATE_SENSEI) {
            drawScenery();
            drawScoreboard();
            drawFloor();
        }
        switch (inGameState) {
            case INGAME_STATE_SENSEI:
                drawScenery();
                drawSensei();
                break;
            case INGAME_STATE_LEVEL_SETUP:
                break;
            case INGAME_STATE_LEVEL_START:
                drawOutlinedText(levelLabel, canvasWidth / 2 - levelLabelWidth
                        / 2, canvasHeight / 2, LEVEL_LABEL_COLOR,
                        LEVEL_LABEL_OUTLINE);
                drawPlayer();
                break;
            case INGAME_STATE_PLAYING:
                drawWatermelonPieces();
                drawPlayer();
                drawWatermelons();
                break;
            case INGAME_STATE_LEVEL_COMPLETED:
                drawWatermelonPieces();
                drawWatermelons();
                drawImageCentered(scenery[SCENERY_STAGE_COMPLETED]);
                break;
            case INGAME_STATE_PLAYER_HIT:
                drawWatermelonPieces();
                drawPlayer();
                drawWatermelons();
                break;
            case INGAME_STATE_GAME_OVER:
                break;
        }
    }

    //
    void drawExitState() {
        drawScenery();
        graphics.drawImage(scenery[SCENERY_SCROLL], canvasWidth / 2
                - scenery[SCENERY_SCROLL].getWidth() / 2, canvasHeight / 2
                - scenery[SCENERY_SCROLL].getHeight() / 2);
        //        graphics.setColor(EXIT_TEXT_BACKGROUND);
        //        graphics.fillRect(8, 8, canvasWidth - 16, canvasHeight - 16);
        //        graphics.setColor(EXIT_FRAME_COLOR);
        //        graphics.drawRect(8, 8, canvasWidth - 16, canvasHeight - 16);
        //        graphics.drawRect(10, 10, canvasWidth - 20, canvasHeight - 20);
        drawCenteredText(textData[EXIT_TEXT][menuMode], EXIT_TEXT_COLOR,
                EXIT_TEXT_SHADOW, canvasHeight / 2 - fontHeight / 2);
    }

    //
    void drawScoresTable() {
        drawScenery();
        drawCenteredText(textData[highScoreTitle][0], HIGHSCORES_HEADER_COLOR,
                HIGHSCORES_SHADOW, fontHeight);
        int y = fontHeight * 3;
        int ci = highScoresColorCycleIndex;
        for (int c = 0; c < 3; c++) {
            drawOutlinedText(highScoreName[c], 8, y,
                    HIGHSCORES_COLOR_CYCLE[ci], HIGHSCORES_SHADOW);
            updateScoreChars(highScorePoints[c] & 0xFFFF, highScoresPointsChars);
            drawOutlinedText(highScoresPointsChars, canvasWidth - 8
                    - HIGHSCORE_COLUMN_WIDTH, y, HIGHSCORES_COLOR_CYCLE[ci],
                    HIGHSCORES_SHADOW);
            ci++;
            if (ci >= HIGHSCORES_COLOR_CYCLE.length) {
                ci = 0;
            }
            y += fontHeight * 2 + 2;
        }
    }

    //--------------------------------------------------------------------------
    // util
    //--------------------------------------------------------------------------

    //
    void drawFloor() {
        int ey = floorY
                - playerFrame[playerAnimationIndex][playerFrameIndex]
                        .getHeight()
                + (playerAttack == PLAYER_ATTACK_HIGH_KICK ? PLAYER_JUMP_Y_OFFSET[kickDataOffsetIndex]
                        : 0);
        // playerAttack
        // kickDataOffsetIndex
        int mody;
        if (playerAttack == PLAYER_ATTACK_HIGH_KICK) {
            mody = BRIDGE_JUMP_BLEND[kickDataOffsetIndex];
        } else {
            mody = 0;
        }
        int iw = scenery[SCENERY_BRIDGE].getWidth();
        for (int x = 0; x < canvasWidth; x += iw) {
            int y = floorY
                    - Math.min(Math.abs(playerX - x) / 8 + mody, FLOOR_HEIGHT);
            graphics.drawImage(scenery[SCENERY_BRIDGE], x, y);
        }
    }

    //
    void drawScenery() {
        graphics.drawImage(scenery[waterIndex], 38, 146);
        graphics.drawImage(scenery[SCENERY_BG_TOP], 0, 0);
    }

    //
    void drawSensei() {
        for (int x = 0; x < canvasWidth; x += senseiFloor.getWidth()) {
            graphics.drawImage(senseiFloor, x, canvasHeight
                    - senseiFloor.getHeight());
        }
        graphics.drawImage(sensei, 8, canvasHeight - sensei.getHeight());
        graphics.drawImage(senseiText, 10 + sensei.getWidth(), canvasHeight
                - sensei.getHeight());
    }

    //
    void drawWatermelons() {
        for (int c = 0; c < WATERMELON_NUMBER; c++) {
            if (!watermelonHit[c]) {
                graphics
                        .drawImage(
                                watermelonFrame[watermelonType[c]][watermelonFrameIndex[c]],
                                watermelonX[c]
                                        - watermelonFrame[watermelonType[c]][watermelonFrameIndex[c]]
                                                .getWidth() / 2,
                                watermelonY[c]
                                        - watermelonFrame[watermelonType[c]][watermelonFrameIndex[c]]
                                                .getHeight() / 2);
            }
        }
    }

    //
    void drawPlayer() {
        graphics
                .drawImage(
                        playerFrame[playerAnimationIndex][playerFrameIndex],
                        playerX
                                - playerFrame[playerAnimationIndex][playerFrameIndex]
                                        .getWidth() / 2,
                        floorY
                                - playerFrame[playerAnimationIndex][playerFrameIndex]
                                        .getHeight()
                                + (playerAttack == PLAYER_ATTACK_HIGH_KICK ? PLAYER_JUMP_Y_OFFSET[kickDataOffsetIndex]
                                        : 0));
    }

    //
    void drawScoreboard() {
        drawOutlinedText(textData[scoreLabel][0], 1, 1 + fontHeight,
                SCOREBOARD_TEXT_COLOR, SCOREBOARD_TEXT_OUTLINE);
        drawOutlinedText(scoreChars, canvasWidth - SCORE_CHARS_X_OFFSET,
                1 + fontHeight, SCOREBOARD_TEXT_COLOR, SCOREBOARD_TEXT_OUTLINE);
    }

    //
    void updateScoreChars(int sc, char[] chars) {
        chars[0] = (char) ('0' + (sc / 1000));
        sc = sc % 1000;
        chars[1] = (char) ('0' + (sc / 100));
        sc = sc % 100;
        chars[2] = (char) ('0' + (sc / 10));
        sc = sc % 10;
        chars[3] = (char) ('0' + sc);
    }

    //
    void updateLevelChars() {
        levelLabel[7] = (char) ('0' + (level / 10));
        levelLabel[8] = (char) ('0' + (level % 10));
    }

    //
    void drawOutlinedText(String text, int x, int y, int color, int outline) {
        graphics.setColor(outline);
        for (int c = 0; c < TEXT_OUTLINE_OFFSET.length; c++) {
            graphics.drawString(text, x + TEXT_OUTLINE_OFFSET[c][0], y
                    + TEXT_OUTLINE_OFFSET[c][1]);
        }
        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }

    //
    void drawOutlinedText(char[] text, int x, int y, int color, int outline) {
        graphics.setColor(outline);
        for (int c = 0; c < TEXT_OUTLINE_OFFSET.length; c++) {
            graphics.drawChars(text, x + TEXT_OUTLINE_OFFSET[c][0], y
                    + TEXT_OUTLINE_OFFSET[c][1], 0, text.length);
        }
        graphics.setColor(color);
        graphics.drawChars(text, x, y, 0, text.length);
    }

    //
    void drawImageCentered(Image img) {
        if(graphics != null) graphics.drawImage(img, canvasWidth / 2 - img.getWidth() / 2,
                canvasHeight / 2 - img.getHeight() / 2);
    }

    //
    void drawCenteredText(String text, int color, int shadow, int y) {
        int x = canvasWidth / 2 - font.getBBoxWidth(text) / 2;
        graphics.setColor(shadow);
        graphics.drawString(text, x + 1, y + 1);
        graphics.setColor(color);
        graphics.drawString(text, x, y);
    }

    void drawWatermelonPieces() {
        for (int w = 0; w < WATERMELON_NUMBER; w++) {
            for (int p = 0; p < MAX_WATERMELON_PIECES; p++) {
                if (wmpV[w][p]) {
                    graphics.drawImage(
                            watermelonFrame[watermelonType[w]][wmpF[w][p]],
                            wmpX[w][p], wmpY[w][p]);
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    // canvas
    //--------------------------------------------------------------------------
    class GameCanvas extends Canvas {

        int lastKeyCode;

        boolean lastKeyPressed;

        public GameCanvas() {
            if (DEBUG) {
                System.out.println("GameCanvas");
            }
            canvasWidth = getWidth();
            canvasHeight = getHeight();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.nttdocomo.ui.Canvas#paint(com.nttdocomo.ui.Graphics)
         */
        synchronized public void paint(Graphics g) {
            Game.graphics = g;
            switch (mainState) {
                case STATE_MENU:
                case STATE_INSTRUCTIONS:
                case STATE_SCORES:
                case STATE_EXIT:
                    System.gc();
                    break;
            }
            g.lock();
            if (initialized) {
                gameInstance.updateDisplay();
                //                g.setColor(0xFFFFFF);
                //                g.drawLine(0, 0, canvasWidth, 0);
                //                g.drawLine(0, canvasHeight - 1, canvasWidth, canvasHeight -
                // 1);
                //                g.drawLine(0, canvasHeight - 2, canvasWidth, canvasHeight -
                // 2);
            } else if (splashImage != null) {
                drawImageCentered(splashImage);
                splashImage = null;
                //                g.setColor(0x0000FF);
                //                g.drawString(platform, canvasWidth / 2, canvasHeight
                //                        - fontHeight);
            } else {
                ProgBarIMP(g);
                FS_ErrorDraw(g, canvasWidth, canvasHeight);
            }
            g.unlock(true);
            SoundRUN();
            paintFlag = false;
        }

        public void processEvent(int type, int param) {
            lastKeyPressed = (type == Display.KEY_PRESSED_EVENT);
            lastKeyCode = param;
        }
    }

    //

    /*
     * ===================================================================
     * 
     * SoundINI() ---------- Inicializamos to lo referente al los sonidos
     * (cargar en memoria, crear bufers, etc...)
     * 
     * SoundSET(n? Sonido , Repetir) ----------------------------- Hacemos que
     * suene un sonido (0 a x) y que se repita x veces. Repetir == 0: Repeticion
     * infinita
     * 
     * SoundRES() ---------- Paramos el ultimo sonido.
     * 
     * SoundRUN() ---------- Debemos ejecutar este metodo en CADA ciclo para
     * gestionar 'tiempos'
     * 
     * VibraSET(microsegundos) ----------------------- Hacemos vibrar el mobil x
     * microsegundos
     * 
     * ===================================================================
     */

    //     *******************
    //     -------------------
    //     Sound - Engine - Rev.0 (28.11.2003)
    //     -------------------
    //     Adaptacion: iMode - Rev.0 (28.11.2003)
    //     ===================
    //     *******************
    AudioPresenter SoundPlayer;

    MediaSound[] Sound;

    int SoundOld = -1;

    int SoundLoop;

    //     -------------------
    //     Sound INI
    //     ===================

    static final int AUDIO_BEAT1 = 0;

    static final int AUDIO_BEAT2 = 1;

    static final int AUDIO_BEAT3 = 2;

    static final int AUDIO_INTRO = 3;

    static final int AUDIO_ENDLEVEL = 4;

    static final int AUDIO_DEAD = 5;

    public void SoundINI() {
        String[] files = { "/ranran_beat1.mid", "/ranran_beat2.mid",
                "/ranran_beat3.mid", "/ranran_intro.mid",
                "/ranran_endlevel.mid", "/ranran_dead.mid" };
        SoundPlayer = AudioPresenter.getAudioPresenter();

        Sound = new MediaSound[files.length];

        for (int c = 0; c < Sound.length; c++) {
            //Sound[c] = LoadSound(files[c]);
            Sound[c] = LoadSound(17, c);
        }

        //      for (int i=0 ; i<Sound.length ; i++) {Sound[i] = LoadSound(7,i);} //
        // leer desde MFS

        SoundPlayer.setMediaListener(this);
    }

    //     -------------------
    //     Sound SET
    //     ===================

    public void SoundSET(int Ary, int Loop) {
        if (soundEnabled) {
            SoundRES();

            try {
                SoundPlayer.setSound(Sound[Ary]);
                SoundPlayer.play();
                SoundOld = Ary;
                SoundLoop = Loop - 1;
            } catch (Exception e) {
            }
        }
    }

    //     -------------------
    //     Sound RES
    //     ===================

    public void SoundRES() {
        if (SoundOld != -1) {
            try {
                SoundPlayer.stop();
                SoundOld = -1;
            } catch (Exception e) {
            }
        }
    }

    //     -------------------
    //     Sound RUN
    //     ===================

    public void SoundRUN() {
        if (Vibra_ON
                && (System.currentTimeMillis() - VibraTimeIni) > VibraTimeFin) {
            Vibra_ON = false;
            try {
                PhoneSystem.setAttribute(PhoneSystem.DEV_VIBRATOR,
                        PhoneSystem.ATTR_VIBRATOR_OFF);
            } catch (Exception e) {
            }
        }
    }

    //     -------------------
    //     Vibra SET
    //     ===================

    boolean Vibra_ON;

    long VibraTimeIni;

    int VibraTimeFin;

    public void VibraSET(int Time) {
        VibraTimeIni = System.currentTimeMillis();
        VibraTimeFin = Time;
        Vibra_ON = vibrationEnabled;

        try {
            if (vibrationEnabled) {
                PhoneSystem.setAttribute(PhoneSystem.DEV_VIBRATOR,
                        PhoneSystem.ATTR_VIBRATOR_ON);
            }
        } catch (Exception e) {
        }
    }

    //     -------------------
    //     mediaAction para controlar Start, Stop, Complete y as� hacer loops
    //     ===================

    public void mediaAction(MediaPresenter mp, int type, int value) {
        if (SoundOld != -1 && mp == SoundPlayer
                && type == AudioPresenter.AUDIO_COMPLETE) {
            if (SoundLoop > 0) {
                SoundLoop--;
            }

            if (SoundLoop != 0) {
                try {
                    SoundPlayer.play();
                } catch (Exception e) {
                }
            }
        }
    }

    public MediaSound LoadSound(String name) {
        MediaSound msound = MediaManager.getSound("resource://" + name);

        try {
            msound.use();
        } catch (Exception e) {
        }

        return msound;
    }

    public MediaSound LoadSound(int Pos) {
        MediaSound msound = MediaManager.getSound("scratchpad:///0;pos=" + Pos);

        try {
            msound.use();
        } catch (Exception e) {
        }

        return msound;
    }

    public MediaSound LoadSound(int Pos, int SubPos) {
        //Pos = (FS_Data[Pos] / 4) + SubPos;
        //return LoadSound(FS_Data[Pos]);
        // // MONGOFIX

        MediaSound s = new MediaSound();
        s.music = Gdx.audio.newMusic(Gdx.files.internal(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".mid"));
        return s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nttdocomo.ui.SoftKeyListener#softKeyPressed(int)
     */
    public void softKeyPressed(int arg0) {
        gameCanvas.lastKeyPressed = true;
        gameCanvas.lastKeyCode = (arg0 == Panel.SOFT_KEY_1 ? Display.KEY_SOFT1
                : Display.KEY_SOFT2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nttdocomo.ui.SoftKeyListener#softKeyReleased(int)
     */
    public void softKeyReleased(int arg0) {
        gameCanvas.lastKeyPressed = false;
        gameCanvas.lastKeyCode = (arg0 == Frame.SOFT_KEY_1 ? Display.KEY_SOFT1
                : Display.KEY_SOFT2);
        if (arg0 == Frame.SOFT_KEY_1) {
            keyPressed = false;
            Display.setCurrent(gameCanvas);
            highScorePoints[newHighScorePos] = (short) score;
            highScoreName[newHighScorePos] = textBox.getText().substring(0, 3)
                    .toCharArray();
            menuOption = 0;
            writeSRam();
            nextMainState = STATE_SCORES;
            SoundSET(AUDIO_INTRO, 0);
            customDelayStartTime = System.currentTimeMillis();
        }
    }

    /*
     * public MediaSound LoadSound(int Pos) { MediaSound msound =
     * MediaManager.getSound("scratchpad:///0;pos="+Pos);
     * 
     * try { msound.use(); } catch (Exception e) {}
     * 
     * return msound; }
     * 
     * 
     * public MediaSound LoadSound(int Pos, int SubPos) { Pos = (FS_Data[Pos] /
     * 4 ) + SubPos;
     * 
     * return LoadSound(FS_Data[Pos]); }
     */

    //     <=- <=- <=- <=- <=-
    //  *******************
    //  -------------------
    //  textos - Engine - v1.0 - Rev.2 (5.5.2004)
    //  ===================
    //  *******************
    final static int TEXT_PLAY = 0;

    final static int TEXT_CONTINUE = 1;

    final static int TEXT_SOUND = 2;

    final static int TEXT_VIBRA = 3;

    final static int TEXT_MODE = 4;

    final static int TEXT_GAMEOVER = 5;

    final static int TEXT_HELP = 6;

    final static int TEXT_ABOUT = 7;

    final static int TEXT_RESTART = 8;

    final static int TEXT_EXIT = 9;

    final static int TEXT_LOADING = 10;

    final static int TEXT_HELP_SCROLL = 11;

    final static int TEXT_ABOUT_SCROLL = 12;

    //  -------------------
    //  textos Create
    //  ===================

    String[][] textosCreate(byte[] tex) {
        char textos[];
        int dataPos = 0;
        int dataBak = 0;
        short[] data = new short[1024];

        boolean campo = false;
        boolean subCampo = false;
        boolean primerEnter = true;
        int size = 0;

        String tmpstr = null;

        tmpstr = new String(tex);
        textos = tmpstr.toCharArray();
        tmpstr = null;
        System.gc();

        for (int i = 0; i < textos.length; i++) {
            if (campo) {
                if (textos[i] == 0x7D) {
                    subCampo = false;
                    campo = true;
                }

                if ((textos[i] < 0x20 && textos[i] >= 0) || textos[i] == 0x7C
                        || textos[i] == 0x7D) // Buscamos cuando Termina un
                // campo
                {
                    data[dataPos + 1] = (short) (i - data[dataPos]);

                    dataPos += 2;

                    campo = false;
                }

            } else {

                if (textos[i] == 0x7D) {
                    subCampo = false;
                    continue;
                }

                if (textos[i] == 0x7B) {
                    dataBak = dataPos;
                    data[dataPos++] = 0;
                    campo = false;
                    subCampo = true;
                    size++;
                    continue;
                }

                if (subCampo && textos[i] == 0x0A) {
                    if (primerEnter) {
                        primerEnter = false;
                    } else {
                        data[dataPos++] = (short) i;
                        data[dataPos++] = 1;
                        if (!subCampo) {
                            size++;
                        } else {
                            data[dataBak]--;
                        }
                    }
                    continue;
                }

                if (textos[i] >= 0x20) // Buscamos cuando Empieza un campo nuevo
                {
                    campo = true;
                    data[dataPos] = (short) i;
                    if (!subCampo) {
                        size++;
                    } else {
                        data[dataBak]--;
                    }
                    primerEnter = true;
                }
            }
        }

        String[][] strings = new String[size][];

        dataPos = 0;

        for (int i = 0; i < size; i++) {
            int num = data[dataPos];

            if (num < 0) {
                num *= -1;
                dataPos++;
            } else {
                num = 1;
            }

            strings[i] = new String[num];

            for (int t = 0; t < num; t++) {
                strings[i][t] = new String(textos, data[dataPos++],
                        data[dataPos++]);
                if (strings[i][t].length() < 2) {
                    strings[i][t] = " ";
                }
            }
        }

        return strings;
    }

// *************************
// -------------------------
// iMode Microjocs FileSystem v2.0 - Engine - Rev.7 (26.1.2004)
// =========================
// *************************

// ------------------------
// HEAD Data Format
// ------------------------
// 00 - (C) - ID - "MICROJOCS_FS"
// 0C - (1) - Version del FileSystem
// 0D - (1) - Tipo de Compresion DATA
// 0E - (1) - Checksum HEAD
// 0F - (1) - Checksum DATA
// 10 - (4) - Size Total Scratch-pad consumido
// 14 - (4) - Size HEAD / Index FAT-Directorios)
// 18 - (4) - Size Total FAT
// 1C - (4) - Size Total DATA (Todos los archivos)

// 20 - (1) - n� de Bloques
// 21 - (1) - n� de Bloques cargados Ok
// 22 - (4) - Size x Bloque
// 26 - (A) - Checksums para 10 Bloques (0 a 9)

// (0x30 = Size del HEAD)
// ========================

// ------------------------
// Errores: SI FS_Create() devuelve TRUE:
// ------------------------
// FS_Error:
// 1: Error de Conexion inicial (No se puede crear una conexion / user DEBE activar las conexiones)
// 2: Error descargando los archivos (Checksum error)
// ========================

    int FS_Error;
    boolean FS_ErrorShow = false;

    int[] FS_Data;
    byte[] FS_Head;

// ---------------
// FS_Create
// ==============="
public boolean FS_Create() {
    return FS_Create("ranran/Data");
}

    public boolean FS_Create(String FileName) {
        FS_Head = FS_LoadData(0, 0x30);

        if (FS_Head == null || FS_Head[0] != 0x4D || FS_Head[1] != 0x49) {
            int Retry = 5;
            while (true) {
                if (Retry-- == 0) {
                    FS_ErrorCreate();
                    return true;
                }

                byte[] Bufer = FS_DownloadData(FileName + ".mfs");

                if (Bufer == null || Bufer.length != 0x30) {
                    FS_Error = 1;
                    continue;
                }    // Controlamos que el Size sea correcto.

                int Checksum = Bufer[0x0E];
                Bufer[0x0E] = 0;
                if (Checksum != FS_Checksum(Bufer, 0, Bufer.length)) {
                    FS_Error = 2;
                    continue;
                }    // Comprobamos Checksum

                FS_SaveData(0, Bufer);
                FS_Head = FS_LoadData(0, 0x30);
                break;
            }
        }

        ProgBarINS(FS_Head[0x20]);    // Agregamos numero de bloques para cargar en el Slider
        FS_Notify();                // Se ha descargado un .mfs Ok (HEAD)

        int BankSize = ((FS_Head[0x22] & 0xFF) << 24) | ((FS_Head[0x23] & 0xFF) << 16) | ((FS_Head[0x24] & 0xFF) << 8) | (FS_Head[0x25] & 0xFF);

        for (int i = 0; i < FS_Head[0x21]; i++) {
            FS_Notify();
        }

        for (int i = FS_Head[0x21]; i < FS_Head[0x20]; i++) {
            int Retry = 5;
            while (true) {
                if (Retry-- == 0) {
                    FS_ErrorCreate();
                    return true;
                }

                byte[] Bufer = FS_DownloadData(FileName + i + ".mfs");

                if (Bufer == null || Bufer.length != BankSize) {
                    FS_Error = 1;
                    continue;
                }    // Controlamos que el Size sea correcto.

                if (FS_Head[0x26 + i] != FS_Checksum(Bufer, 0, Bufer.length)) {
                    FS_Error = 2;
                    continue;
                }    // Comprobamos Checksum

                FS_SaveData(FS_Head.length + (i * BankSize), Bufer);
                FS_SaveData(0x21, new byte[]{(byte) (i + 1)});
                break;
            }
            FS_Notify();    // Se ha descargado un .mfs Ok (DATA)
        }

// Cargamos la "FAT" del MFS
// -------------------------
        int Pos = ((FS_Head[0x14] & 0xFF) << 24) | ((FS_Head[0x15] & 0xFF) << 16) | ((FS_Head[0x16] & 0xFF) << 8) | (FS_Head[0x17] & 0xFF);
        int Size = ((FS_Head[0x18] & 0xFF) << 24) | ((FS_Head[0x19] & 0xFF) << 16) | ((FS_Head[0x1A] & 0xFF) << 8) | (FS_Head[0x1B] & 0xFF);

        FS_Data = new int[(Size / 4)];

        try {

            //InputStream in = Connector.openInputStream("scratchpad:///0;pos=" + Pos);
            // MONGOFIX
            File file = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder, "scratchpad");
            FileInputStream in = new FileInputStream(file);
            in.skip(Pos);

            for (int i = 0; i < FS_Data.length; i++) {
                short s;
                s = (short)in.read(); if(s<0) s+=256;
                FS_Data[i] = (s & 0xFF) << 24;
                s = (short)in.read(); if(s<0) s+=256;
                FS_Data[i] |= (s & 0xFF) << 16;
                s = (short)in.read(); if(s<0) s+=256;
                FS_Data[i] |= (s & 0xFF) << 8;
                s = (short)in.read(); if(s<0) s+=256;
                FS_Data[i] |= (s & 0xFF);
            }

            in.close();
        } catch (IOException e) {
        }

        FS_Error = 0;
        return false;
    }


// ---------------
// FS_LoadData
// ===============

    public byte[] CargaFichero(String f)
    {
        FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+f);
        byte[] bytes = file.readBytes();

        return bytes;
    }

    public byte[] FS_LoadData(int Pos, int Size)
    {
	/*
	byte[] Bufer = new byte[Size];

	try {
		InputStream in = Connector.openInputStream("scratchpad:///0;pos="+Pos);
		in.read(Bufer,0,Bufer.length);
		in.close();
	} catch (IOException e) {return null;}

	return Bufer;
*/

	/*File file = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder, "scratchpad");

	if(file.exists())
	{
		try {
			FileInputStream fis = new FileInputStream(file);
			byte b[] = new byte[Size];
			fis.read(b, Pos, Size);
			return b;
		}
		catch(IOException e)
		{
			return null;
		}
	}
	else return null;*/

        RandomAccessFile file;

        try {
            file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder + "/scratchpad", "r");

            byte b[] = new byte[Size];

            try {
                file.seek(Pos);
                file.read(b, 0, Size);
                file.close();
                return b;

            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }

        }catch(FileNotFoundException e)
        {
            return null;
        }
    }

// ---------------
// FS_SaveData
// ===============

    public int FS_SaveData(int Pos, byte[] Bufer)
    {
	/*try {
		OutputStream out=Connector.openOutputStream("scratchpad:///0;pos="+Pos);
		out.write(Bufer,0,Bufer.length);
		out.close();
	} catch (Exception e) {return 0;}

	return Bufer.length;*/

        RandomAccessFile file;
        try {
            file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder + "/scratchpad", "rw");

        }catch(FileNotFoundException e)
        {
            File dirs = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder);
            dirs.mkdirs();
            try {
                file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder+ "/scratchpad", "rw");

            } catch (IOException e2) {
                e2.printStackTrace();
                return 0;
            }
        }

        try {
            file.seek(Pos);
            file.write(Bufer);
            return Bufer.length;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
/*
	if(!file.())
	{
		File dirs = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder);
		dirs.mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	try {
		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(Bufer, Pos, Bufer.length);
		fos.close();
		return Bufer.length;

	} catch(Exception e)
	{
		return 0;
	}*/
    }

// ---------------
// FS_LoadFile
// ===============

    public byte[] FS_LoadFile(int Pos, int SubPos)
    {
        Pos = (FS_Data[Pos] / 4 ) + SubPos;

        return FS_LoadData(FS_Data[Pos], FS_Data[Pos+1] - FS_Data[Pos]);
    }


// ---------------
// FS_SaveFile
// ===============

    public void FS_SaveFile(int Pos, int SubPos, byte[] Bufer)
    {
        Pos = (FS_Data[Pos] / 4 ) + SubPos;

        FS_SaveData(FS_Data[Pos], Bufer);
    }


// ---------------
// FS_LoadImage
// ===============

    public Image[] FS_LoadImage(int Pos)
    {
        int Ini = FS_Data[Pos];
        int Size = (FS_Data[Pos+1]-Ini)/4;

        Ini/=4;

        Image[] Img = new Image[Size];

        for (int i=0 ; i<Size ; i++) {
            Img[i] = Image.createImage(IApplication.assetsFolder+"/"+Pos+"/"+i+".gif");
            //Img[i] = loadImage(FS_Data[Ini++]);
        }

        return Img;
    }


// ---------------
// FS_LoadImage
// ===============

    public Image FS_LoadImage(int Pos, int SubPos)
    {
        Image im = Image.createImage(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".gif");
        return im;

        //return loadImage(FS_Data[(FS_Data[Pos]/4)+SubPos]);
    }


// ---------------
// FS_DownloadData
// ===============

    public byte[] FS_DownloadData(String Filename)
    {
        FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+Filename);
        byte[] bytes = file.readBytes();

        return bytes;
	/*byte[] dat = null;

	int Retry=5;
	boolean FileOk = false;

	do {
		HttpConnection conn = null;
		try {
			conn=(HttpConnection)(Connector.open(IApplication.getCurrentApp().getSourceURL()+"../mfs_file/" + Filename, (Integer) Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://www.iwapserver.com/microjocs/games/mfs_file/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://mjj.no-ip.biz:8085/microjocs/games/mfs_file/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://www.arrakis.es/%7ejoanant/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open(IApplication.getCurrentApp().getSourceURL() + Filename, Connector.READ));
			conn.setRequestMethod(HttpConnection.GET);
			conn.connect();

			try {
				InputStream in=conn.openInputStream();
				dat = new byte[(int)conn.getLength()];
				in.read(dat);
				in.close();
				FileOk = true;
			} catch (Exception e) {}

		} catch (Exception e) {}

		try {
			if (conn!=null) {conn.close(); conn=null;}
		} catch (Exception e) {}

		if (!FileOk && --Retry==0) {return null;}
	}
	while (!FileOk);

	return dat;*/
    }

// ---------------
// FS_Checksum
// ===============

    public byte FS_Checksum(byte[] Bufer, int Pos, int Size)
    {
        int Checksum=0;
        for (int i=0 ; i<Size ; i++) {Checksum+=Bufer[Pos+i]; Checksum = (Checksum^i)&0xFF;}
        return (byte)(Checksum & 0xFF);
    }

// ---------------
// FS_Notify - Notifica que un Bloque ha sido leido OK, PONER aqu� el "repaint()" para actualizar el 'Slider' de Descarga.
// ===============

    public void FS_Notify()
    {
        ProgBarADD();
    }

// ---------------
// FS_Error Create
// ===============

    public void FS_ErrorCreate()
    {
        FS_ErrorShow = true;

        long time = System.currentTimeMillis();

        while ((System.currentTimeMillis()-time) < 16000 )
        {
            //canvasShow=true; repaint();
            try {Thread.sleep(2000);} catch (Exception e) {}
        }
    }

// ---------------
// FS_Error Draw
// ===============

    public void FS_ErrorDraw(Graphics g, int cSizeX, int cSizeY)
    {
        if (FS_ErrorShow)
        {
            g.setColor(0xFFFFFF);
            g.fillRect(0,0, cSizeX, cSizeY);
            g.setColor(0x0);

            Font f = Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
            g.setFont(f);
            int y = f.getAscent();
            int height = f.getHeight();

//	for (int i=0 ; i<FS_strError.length ; i++) {g.drawString(FS_strError[i], 4, y); y += height;}

            for (int i=0 ; i<FS_strError.length ; i++)
            {
                int Pos=0, PosIni=0, PosOld=0, Size=0;

                while ( PosOld < FS_strError[i].length() )
                {
                    Size=0;

                    Pos=PosIni;

                    while ( Size < (cSizeX-6) )
                    {
                        if ( Pos == FS_strError[i].length() ) {PosOld=Pos; break;}

                        int Dat = FS_strError[i].charAt(Pos++);
                        if (Dat==0x20) {PosOld=Pos-1;}

                        Size += f.stringWidth(new String(new char[] {(char)Dat}));
                    }

                    if (PosOld-PosIni < 1) { while ( Pos < FS_strError[i].length() && FS_strError[i].charAt(Pos) >= 0x30 ) {Pos++;} PosOld=Pos; }

                    g.drawString(FS_strError[i].substring(PosIni,PosOld), 3, y); y += height;

                    PosIni=PosOld+1;
                }
            }
        }
    }

    String[] FS_strError = new String[] {
            "- ERROR -",
            " ",
            "- Si es la primera vez que ejecuta esta aplicaci�n, aseg�rese de tener activado el acceso a la red.",
            " ",
            "- Posible problema de cobertura GPRS. Int�ntelo de nuevo m�s tarde.",
    };

// <=- <=- <=- <=- <=-


    //  *******************
    //  -------------------
    //  ProgBar - Engine - Rev.2 (20.1.2004)
    //  ===================
    //  *******************

    int ProgBarX;

    int ProgBarY;

    int ProgBarSizeX;

    int ProgBarSizeY;

    int ProgBarPos;

    int ProgBarTotal;

    boolean ProgBar_ON = false;

    //  -------------------
    //  ProgBar INI
    //  ===================

    public void ProgBarINI(int SizeX, int SizeY, int DestX, int DestY) {
        ProgBarX = DestX;
        ProgBarY = DestY;
        ProgBarSizeX = SizeX;
        ProgBarSizeY = SizeY;
    }

    //  -------------------
    //  ProgBar END
    //  ===================

    public void ProgBarEND() {
        ProgBarSET(1, 1);
        ProgBar_ON = false;
    }

    //  -------------------
    //  ProgBar SET
    //  ===================

    public void ProgBarSET(int Pos) {
        ProgBarSET(Pos, ProgBarTotal);
    }

    public void ProgBarSET(int Pos, int Total) {
        ProgBarPos = Pos;
        ProgBarTotal = Total;

        ProgBar_ON = true;
        paintFlag = true;
        gameCanvas.repaint();
        while (paintFlag) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }

    //  -------------------
    //  ProgBar INS
    //  ===================

    public void ProgBarINS(int suma) {
        ProgBarTotal += suma;
    }

    //  -------------------
    //  ProgBar ADD
    //  ===================

    public void ProgBarADD() {
        ProgBarSET(++ProgBarPos);
    }

    //  -------------------
    //  ProgBar IMP
    //  ===================

    public void ProgBarIMP(Graphics Gfx) {
        if (ProgBar_ON) {
            Gfx.setColor(0xFFFFFF);
            Gfx.fillRect(ProgBarX - 3, ProgBarY - 3, ProgBarSizeX + 6,
                    ProgBarSizeY + 6);
            Gfx.setColor(0);
            Gfx.drawRect(ProgBarX - 3, ProgBarY - 3, ProgBarSizeX + 5,
                    ProgBarSizeY + 5);
            Gfx.fillRect(ProgBarX, ProgBarY,
                    ((ProgBarPos * ProgBarSizeX) / ProgBarTotal), ProgBarSizeY);
        }
    }

    //  <=- <=- <=- <=- <=-

}