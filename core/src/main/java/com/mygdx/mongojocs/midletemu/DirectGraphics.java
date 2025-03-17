package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DirectGraphics {

    public static final int FLIP_HORIZONTAL = 0x2000;
    public static final int FLIP_VERTICAL = 0x4000;
    public static final int TYPE_INT_8888_ARGB = 1;
    public static final int ROTATE_90 = 90;
    public static final int ROTATE_180 = 180;
    public static final int ROTATE_270 = 270;
    Graphics g;

    DirectGraphics(Graphics g)
    {
        this.g = g;
    }

    public static DirectGraphics getDirectGraphics(Graphics g) {
        return new DirectGraphics(g);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int c) {

        if(g.allClipped) return;

        if(g.fromImage != null)
            g.fromImage.fbo.begin();
        else
            Display.fbo.begin();

        g.shapeRenderer.setProjectionMatrix(g.camera.combined);
        g.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        g.shapeRenderer.setColor(new Color(c));
        g.shapeRenderer.triangle(x1,y1,x2,y2,x3,y3,g.currentColor,g.currentColor,g.currentColor);
        g.shapeRenderer.end();

        if(g.fromImage != null)
            g.fromImage.fbo.end();
        else
            Display.fbo.end();
    }

    public void drawImage(Image im, int x, int y, int anchor, int flags) {

        if(g.allClipped) return;

        boolean flipX = false;
        boolean flipY = true;
        float rotation = 0;
        int rotatedWidth = im.getWidth();
        int rotatedHeight = im.getHeight();


        if(flags == ROTATE_90)
        {
            rotation = 270; y+=im.texture.getWidth();
            rotatedWidth = im.getHeight(); rotatedHeight = im.getWidth();
        }
        else if(flags == ROTATE_180)
        {
            rotation = 180; x+=im.texture.getWidth(); y+=im.texture.getHeight();
        }
        else if(flags == ROTATE_270)
        {
            rotation = 90; x+=im.texture.getHeight();
            rotatedWidth = im.getHeight(); rotatedHeight = im.getWidth();
        }
        else
        {
            flipX = (flags & FLIP_HORIZONTAL) != 0;
            flipY = (flags & FLIP_VERTICAL) == 0;
        }

        if ((anchor & Graphics.HCENTER) != 0)
            x -= rotatedWidth / 2;
        if ((anchor & Graphics.VCENTER) != 0)
            y -= rotatedHeight / 2;
        if ((anchor & Graphics.RIGHT) != 0)
            x -= rotatedWidth;
        if ((anchor & Graphics.BOTTOM) != 0)
            y -= rotatedHeight;



        Display.clippedAreafbo.begin();

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        g.batch.setProjectionMatrix(Display.clippedAreaCamera.combined);
        g.batch.begin();
        //g.batch.draw(im.texture, x, y, w, h, u, v, u2, v2);
        g.batch.draw(im.texture, x,y,0,0, im.getWidth(),im.getHeight(),1.0f,1.0f,
                rotation,0,0,im.getWidth(),im.getHeight(),
                flipX,
                flipY);
        g.batch.end();

        Display.clippedAreafbo.end();

        if(g.fromImage == null)
            Display.fbo.begin();
        else
            g.fromImage.fbo.begin();

        g.drawClippedArea();

        if(g.fromImage == null)
            Display.fbo.end();
        else
            g.fromImage.fbo.end();
/*
        Texture texture = im.texture;

        int w = texture.getWidth();
        int h = texture.getHeight();
        float u = 0;
        float v = 0;
        float u2 = 1;
        float v2 = 1;


        //Clipping
        if(x < g.clipx)
        {
            int dx = g.clipx - x;
            x += dx; w -= dx; u+= (float)dx/texture.getWidth();
        }
        if(x + w > g.clipx + g.clipw)
        {
            int dx = (x + w) - (g.clipx + g.clipw);
            w -= dx; u2-= (float)dx/texture.getWidth();
        }
        if(y < g.clipy)
        {
            int dy = g.clipy - y;
            y += dy; h -= dy; v+= (float)dy/texture.getHeight();
        }
        if(y + h > g.clipy + g.cliph)
        {
            int dy = (y + h) - (g.clipy + g.cliph);
            h -= dy; v2-= (float)dy/texture.getHeight();
        }

        float aux;
        if((flags & FLIP_HORIZONTAL) != 0)
        {
            u = 1.0f - u; u2 = 1.0f - u2;
        }

        if((flags & FLIP_VERTICAL) != 0)
        {
            v = 1.0f - v; v2 = 1.0f - v2;
        }

         if((flags & ROTATE_90) != 0)
        {
            u = u;
            aux = v; v = v2; v2 = aux;
            u2 = u2;
        }
        else if((flags & ROTATE_180) != 0)
        {
            aux = u; u = u2; u2 = aux;
            aux = v; v = v2; v2 = aux;
        }
        else if((flags & ROTATE_270) != 0)
        {
            aux = u; u = u2; u2 = aux;
            v = v;
            v2 = v2;
        }

        if(g.fromImage != null)
            g.fromImage.fbo.begin();
        else
            Display.fbo.begin();

        g.batch.setProjectionMatrix(g.camera.combined);
        g.batch.begin();
        g.batch.draw(im.texture, x, y, w, h, u, v, u2, v2);
        g.batch.end();

        if(g.fromImage != null)
            g.fromImage.fbo.end();
        else
            Display.fbo.end();
*/

    }

    public void drawPixels(int[] temp, boolean b, int i, int width, int x, int i1, int width1, int i2, int i3, int typeInt8888Argb) {
    }

    public void setARGBColor(int i) {
    }
}
