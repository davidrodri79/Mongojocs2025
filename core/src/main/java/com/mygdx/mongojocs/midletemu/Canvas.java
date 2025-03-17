package com.mygdx.mongojocs.midletemu;

public class Canvas extends Displayable {
    public static final int KEY_NUM0 = 48;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_POUND = 35;
    public static final int KEY_STAR = 42;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int DOWN = 6;
    public static final int FIRE = 8;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;


    public static Canvas theCanvas = null;
    public Graphics graphics;
    public boolean repaintAsked = false;

    public Canvas()
    {
        theCanvas = this;
        graphics = new Graphics();
        graphics.init();
    }

    public int getWidth()
    {
        return Display.width;
    }

    public int getHeight()
    {
        return Display.height;
    }

    public void repaint()
    {
        repaintAsked = true;
    }

    public void repaint(int x, int y, int width, int height)
    {
        repaint();
    }

    public void paint(Graphics g)
    {

    }

    public void serviceRepaints()
    {

    }

    public void flushRepaints()
    {
        if(repaintAsked) {
            graphics.camera.update();
            graphics.setClip(0, 0, getWidth(), getHeight());
            graphics.translatex = 0;
            graphics.translatey = 0;
            paint(graphics);
            repaintAsked = false;
        }
    }

    public void keyPressed(int keycode)
    {

    }

    public void keyReleased(int keycode)
    {

    }

    public int getGameAction(int keyData) {

        switch (keyData)
        {
            case KEY_NUM2 : return UP;
            case KEY_NUM4 : return LEFT;
            case KEY_NUM8 : return DOWN;
            case KEY_NUM6 : return RIGHT;
            case KEY_NUM5 : return FIRE;
        }
        return 0;
    }

    public void setFullScreenMode(boolean b) {
    }


    protected void showNotify() {
    }

    protected void hideNotify() {
    }
}
