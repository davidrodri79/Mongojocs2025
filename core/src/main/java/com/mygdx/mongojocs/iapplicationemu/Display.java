package com.mygdx.mongojocs.iapplicationemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.mygdx.mongojocs.parachutist.GameCanvas;

public class Display {
    public static final int KEY_RELEASED_EVENT = 1;
    public static final int KEY_PRESSED_EVENT = 2;


    public static final int KEY_UP = 1;
    public static final int KEY_DOWN = 2;
    public static final int KEY_LEFT = 3;
    public static final int KEY_RIGHT = 4;
    public static final int KEY_SELECT = 5;
    public static final int KEY_0 = 10;
    public static final int KEY_1 = 11;
    public static final int KEY_2 = 12;
    public static final int KEY_3 = 13;
    public static final int KEY_4 = 14;
    public static final int KEY_5 = 15;
    public static final int KEY_6 = 16;
    public static final int KEY_7 = 17;
    public static final int KEY_8 = 18;
    public static final int KEY_9 = 19;


    public static final int KEY_SOFT1 = 20;
    public static final int KEY_SOFT2 = 21;
    public static final int KEY_ASTERISK = 30;
    public static final int KEY_POUND = 31;
    public static int width;
    public static int height;

    public static FrameBuffer fbo = null;
    public static Texture screenBuffer = null;

    public static Canvas theCanvas = null;

    public static void setCurrent(Canvas c) {
        theCanvas = c;
    }

    public static void setCurrent(Panel p) {

    }

    public static void setSize(int w, int h) {
        width = w;
        height = h;

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
        screenBuffer = fbo.getColorBufferTexture();

        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fbo.end();

    }
}
