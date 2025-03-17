package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.mygdx.mongojocs.bravewar.GameCanvas;
import com.mygdx.mongojocs.pingpoyo.Bios;

public class Display {

    public static Display theDisplay = new Display();
    public static Displayable currentDisplayable;
    public static int width;
    public static int height;
    public static Texture screenBuffer;
    public static Texture clippedAreaTexture;
    public static FrameBuffer fbo = null;
    public static FrameBuffer clippedAreafbo = null;
    public static OrthographicCamera clippedAreaCamera;

    public static Display getDisplay(MIDlet m)
    {
       return theDisplay;
    }

    public void setCurrent(Displayable c) {
        currentDisplayable = c;
    }

    public static void setSize(int w, int h)
    {
        width = w; height = h;

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
        screenBuffer = fbo.getColorBufferTexture();

        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fbo.end();

        clippedAreafbo = new FrameBuffer(Pixmap.Format.RGBA8888, 600, 800, false);
        clippedAreaTexture = clippedAreafbo.getColorBufferTexture();

        clippedAreaCamera = new OrthographicCamera();
        clippedAreaCamera.setToOrtho(false, 600, 800);

        clippedAreafbo.begin();
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        clippedAreafbo.end();
    }

    public Displayable getCurrent() {
        return currentDisplayable;
    }

    public void vibrate(int time) {
        Gdx.input.vibrate(time);
    }
}
