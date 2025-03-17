package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class Image {

    static class ImageLoadTask implements Runnable {

        Image image;
        String filename;
        boolean finished;

        ImageLoadTask(Image i, String f)
        {
            image = i;
            filename = f;
            finished = false;
        }

        @Override
        public void run() {
            try {
                image._createImage(filename);
            }catch (Exception e) {
            }
            finished = true;
        }
    };

    static class ImageCreateTask implements Runnable {

        Image image;
        int width, height;
        boolean finished = false;

        ImageCreateTask(Image i, int w, int h)
        {
            image = i;
            width = w; height = h;
        }

        @Override
        public void run() {
            image._createImage(width, height);
            finished = true;
        }
    };

    static class ImageCreateBinaryTask implements Runnable {

        Image image;
        byte buffer[];
        int start, length;
        boolean finished = false;

        ImageCreateBinaryTask(Image i, byte[] b, int s, int l)
        {
            image = i;
            buffer = b;
            start = s; length = l;
        }

        @Override
        public void run() {
            image._createImage(buffer, start, length);
            finished = true;
        }
    };

    public Texture texture = null;
    public FrameBuffer fbo = null;

    public Image()
    {

    }

    public static Image createImage(String fileName)
    {
        Image im = new Image();
        ImageLoadTask task = new ImageLoadTask(im, fileName);
        Gdx.app.postRunnable(task);

        while(!task.finished)
        {
            try {
                Gdx.app.log("Image","Waiting for main thread to create image from file...");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return im;
    }

    public void _createImage(String fileName)
    {
        texture = new Texture(MIDlet.assetsFolder+"/"+fileName.substring(1));
        Gdx.app.log("Image", "createImage "+fileName.toString());
    }

    public static Image createImage(int w, int h)
    {
        Image im = new Image();
        ImageCreateTask task = new ImageCreateTask(im, w, h);
        Gdx.app.postRunnable(task);

        while(!task.finished)
        {
            try {
                Gdx.app.log("Image","Waiting for main thread to create blank image...");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return im;
    }

    public void _createImage(int w, int h)
    {
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
        texture = fbo.getColorBufferTexture();

        fbo.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        fbo.end();

    }

    public static Image createImage(byte[] inbuf, int start, int length)
    {
        Image im = new Image();
        ImageCreateBinaryTask task = new ImageCreateBinaryTask(im, inbuf, start, length);
        Gdx.app.postRunnable(task);
        while(!task.finished)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return im;
    }

    public void _createImage(byte[] inbuf, int start, int length)
    {
        try {

            Pixmap pixMap = new Pixmap(inbuf, start, length);
            texture = new Texture(pixMap);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Graphics getGraphics()
    {
        Graphics g = new Graphics();
        g.fromImage = this;
        g.init();
        g.camera.setToOrtho(false, getWidth(), getHeight());
        g.batch.setProjectionMatrix(g.camera.combined);
        return g;
    }

    public int getWidth() {
        return texture == null ? 0 : texture.getWidth();
    }

    public int getHeight() {
        return texture == null ? 0 : texture.getHeight();
    }

    public void flipVertical()
    {
        if(fbo != null) {

            TextureRegion textureRegion = new TextureRegion(texture);
            textureRegion.flip(false, true);
        }
    }
}
