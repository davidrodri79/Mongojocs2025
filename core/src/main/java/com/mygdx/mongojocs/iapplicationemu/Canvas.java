package com.mygdx.mongojocs.iapplicationemu;

import com.mygdx.mongojocs.astro.GamePlay;
import com.mygdx.mongojocs.iapplicationemu.Graphics;

public class Canvas {

    public static final int SOFT_KEY_1 = 1;
    public static final int SOFT_KEY_2 = 1;

    public Graphics graphics;
    public boolean repaintAsked = false;
    public static String softKey1 = null;
    public static String softKey2 = null;

    public Canvas()
    {
        graphics = new Graphics();
        graphics.init();
    }

    public void processEvent(int type, int param)
    {

    }

    protected void paint(Graphics g)
    {

    }

    public void repaint()
    {
        repaintAsked = true;
    }

    public void setSoftLabel(int k, String s)
    {
        if(k == Frame.SOFT_KEY_1)
            softKey1 = s;
        else if(k == Frame.SOFT_KEY_2)
            softKey2 = s;
    }

    public int getWidth()
    {
        return Display.width;
    }

    public int getHeight()
    {
        return Display.height;
    }

    public void flushRepaints() {

        if(repaintAsked) {
            graphics.camera.update();
            paint(graphics);
            repaintAsked = false;
        }
    }
}
