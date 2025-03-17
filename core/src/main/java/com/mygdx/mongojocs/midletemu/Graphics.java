package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;

public class Graphics {

    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int BASELINE = 64;

    public static final int SOLID = 0;
    public static final int DOTTED = 1;


    public Image fromImage = null;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public OrthographicCamera camera, textCamera;
    Font currentFont;
    Color currentColor;
    static int scissorsSet = 0;
    boolean allClipped = false;
    int clipx, clipy, clipw, cliph;
    int translatex, translatey;

    public static HashMap<String, BitmapFont> bitmapFonts = new HashMap<>();
    public static final String fontChars = "abcçdefghijklmnñopqrstuvwxyzáéíóúABCÇDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ0123456789¡!¿?[]()+-*/=,.;:%&#@|<>_'\"";


    public Graphics() {
        translatex = 0;
        translatey = 0;
    }

    public void init() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Display.width, Display.height);
        textCamera = new OrthographicCamera();
        textCamera.setToOrtho(true, Display.width, Display.height);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        clipx = 0;
        clipy = 0;
        clipw = Display.width;
        cliph = Display.height;
        currentFont = Font.getDefaultFont();
    }

    public void setFont(Font f) {

        currentFont = f;
    }

    public Font getFont() {
        return currentFont;
    }

    public void setColor(int c) {
        currentColor = new Color(((c & 0xff0000) >> 16) / 255.f,
                ((c & 0xff00) >> 8) / 255.f,
                (c & 0xff) / 255.f,
                1);
    }

    public int getColor() {
        return currentColor.toIntBits();
    }

    public void setColor(int r, int g, int b) {
        currentColor = new Color(r / 255.f, g / 255.f, b / 255.f, 1);
    }

    public void translate(int X, int Y) {
        translatex += X;
        translatey += Y;
        clipx += X;
        clipy += Y;
    }

    public static boolean generatingFont = false;

    static class FontGenerateTask implements Runnable {

        int face, style, size;
        Color color;

        FontGenerateTask(int f, int st, int si, Color c) {
            face = f;
            style = st;
            size = si;
            color = c;
        }

        @Override
        public void run() {
            Graphics._fontGenerate(face, style, size, color);
            generatingFont = false;
        }
    }

    public static void fontGenerate(int face, int style, int size, Color color) {
        generatingFont = true;
        Gdx.app.postRunnable(new FontGenerateTask(face, style, size, color));
        while (generatingFont) {
            try {
                Gdx.app.log("Graphics", "Waiting for main thread to generate font...");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static BitmapFont _fontGenerate(int face, int style, int size, Color color) {
        String hash = face + "-" + style + "-" + size + "-" + color;
        String hashNoColor = face + "-" + style + "-" + size;

        String fontFile;

        if ((style & Font.STYLE_ITALIC) != 0)
            fontFile = "prazo-cursiva.otf";
        else {
            if ((style & Font.STYLE_BOLD) != 0)
                fontFile = "8bitOperatorPlus-Bold.ttf";
            else
                fontFile = "8bitOperatorPlus-Regular.ttf";
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFile));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.minFilter = Texture.TextureFilter.Nearest;
        params.magFilter = Texture.TextureFilter.Nearest;
        params.size = Font.getPixelWidth(size);
        params.borderWidth = 0; //style == Font.STYLE_BOLD ? 1 : 0;
        params.borderColor = color;
        params.color = color;
        params.characters = fontChars;
        BitmapFont f = generator.generateFont(params); // font size 12 pixels
        generator.dispose();

        bitmapFonts.put(hash, f);
        bitmapFonts.put(hashNoColor, f);

        return f;
    }

    public void drawString(String str, int x, int y, int flags) {


        x += translatex;
        y += translatey;

        String hash = currentFont.face + "-" + currentFont.style + "-" + currentFont.size + "-" + currentColor;
        BitmapFont f = null;

        // Generate font if needed
        if (!bitmapFonts.containsKey(hash)) {
            _fontGenerate(currentFont.face, currentFont.style, currentFont.size, currentColor);
        }
        f = bitmapFonts.get(hash);

        // Draw text into clipped area texture
        if ((flags & HCENTER) != 0) {
            x -= currentFont.stringWidth(str) / 2;
        }

        if ((flags & RIGHT) != 0) {
            x -= currentFont.stringWidth(str);
        }

        if ((flags & BOTTOM) != 0) {
            y -= currentFont.getHeight();
        }

        Display.clippedAreafbo.begin();

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();
        if (f != null) f.draw(batch, str, x, Display.height - y - 2);
        batch.end();

        Display.clippedAreafbo.end();

        // Copy clipped area texture to screen of image
        if (fromImage == null)
            Display.fbo.begin();
        else
            fromImage.fbo.begin();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Display.clippedAreaTexture, clipx, clipy, clipw, cliph,
                clipx / (float) Display.width, clipy / (float) Display.height,
                (clipx + clipw) / (float) Display.width, (clipy + cliph) / (float) Display.height);
        batch.end();

        if (fromImage == null)
            Display.fbo.end();
        else
            fromImage.fbo.end();
    }

    public void setClip(int destX, int destY, int sizeX, int sizeY) {

        allClipped = false;

        destX += translatex;
        destY += translatey;

        int maxx = fromImage == null ? Display.width : fromImage.getWidth();
        int maxy = fromImage == null ? Display.height : fromImage.getHeight();

        if (destX < 0) {
            sizeX += destX;
            destX = 0;
        }
        if (destX >= maxx) destX = maxx;
        if (destY < 0) {
            sizeY += destY;
            destY = 0;
        }
        if (destY >= maxy) destY = maxy;
        if (destX + sizeX >= maxx) sizeX = maxx - destX;
        if (destY + sizeY >= maxy) sizeY = maxy - destY;


        clipx = destX;
        clipy = destY;
        clipw = sizeX;
        cliph = sizeY;

        if (clipw <= 0) {
            allClipped = true;
            return;
        }
        if (cliph <= 0) {
            allClipped = true;
            return;
        }

    }

    public void clipRect(int destX, int destY, int sizeX, int sizeY) {

        destX += translatex;
        destY += translatey;

        if (clipx < destX) {
            clipw -= destX - clipx;
            clipx = destX;
        }
        if (clipx + clipw > destX + sizeX) clipw = (destX + sizeX) - clipx;
        if (clipy < destY) {
            cliph -= destY - clipy;
            clipy = destY;
        }
        if (clipy + cliph > destY + sizeY) cliph = (destY + sizeY) - clipy;

        if (clipw <= 0) {
            allClipped = true;
            return;
        }
        if (cliph <= 0) {
            allClipped = true;
            return;
        }

    }

    public void drawImage(Image img, int x, int y, int flags) {
        _drawImage(this, img, x, y, flags);
    }

    public static void _drawImage(Graphics g, Image img, int x, int y, int flags) {
        if (g.allClipped || img.texture == null) return;

        {
            if ((flags & HCENTER) != 0)
                x -= img.getWidth() / 2;
            if ((flags & VCENTER) != 0)
                y -= img.getHeight() / 2;
            if ((flags & RIGHT) != 0)
                x -= img.getWidth();
            if ((flags & BOTTOM) != 0)
                y -= img.getHeight();

            x += g.translatex;
            y += g.translatey;

            int w = img.texture.getWidth();
            int h = img.texture.getHeight();
            float u = 0;
            float v = 0;
            float u2 = 1;
            float v2 = 1;

            //Clipping
            if (x < g.clipx) {
                int dx = g.clipx - x;
                x += dx;
                w -= dx;
                u += (float) dx / img.texture.getWidth();
            }
            if (x + w > g.clipx + g.clipw) {
                int dx = (x + w) - (g.clipx + g.clipw);
                w -= dx;
                u2 -= (float) dx / img.texture.getWidth();
            }
            if (y < g.clipy) {
                int dy = g.clipy - y;
                y += dy;
                h -= dy;
                v += (float) dy / img.texture.getHeight();
            }
            if (y + h > g.clipy + g.cliph) {
                int dy = (y + h) - (g.clipy + g.cliph);
                h -= dy;
                v2 -= (float) dy / img.texture.getHeight();
            }

            int finaly = y;

            if (g.fromImage == null)
                Display.fbo.begin();
            else
                g.fromImage.fbo.begin();

            g.batch.setProjectionMatrix(g.camera.combined);
            g.batch.begin();
            g.batch.draw(img.texture, x, finaly, w, h, u, v, u2, v2);
            g.batch.end();

            if (g.fromImage == null)
                Display.fbo.end();
            else
                g.fromImage.fbo.end();
        }

    }

    public void drawRect(int x, int y, int w, int h) {
        _drawRect(this, x, y, w, h);
    }

    public static void _drawRect(Graphics g, int x, int y, int w, int h) {
        if (g.allClipped) return;

        x += g.translatex;
        y += g.translatey;

        if (g.fromImage == null)
            Display.fbo.begin();
        else
            g.fromImage.fbo.begin();

        g.shapeRenderer.setProjectionMatrix(g.camera.combined);
        g.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        g.shapeRenderer.setColor(g.currentColor);
        g.shapeRenderer.rect(x, y, w, 1);
        g.shapeRenderer.rect(x, y, 1, h);
        g.shapeRenderer.rect(x, y + h, w, 1);
        g.shapeRenderer.rect(x + w - 1, y, 1, h);
        g.shapeRenderer.end();

        if (g.fromImage == null)
            Display.fbo.end();
        else
            g.fromImage.fbo.end();
    }

    public void startPrimitives()
    {
        if (fromImage == null)
            Display.fbo.begin();
        else
            fromImage.fbo.begin();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    public void endPrimitives()
    {
        shapeRenderer.end();

        if (fromImage == null)
            Display.fbo.end();
        else
            fromImage.fbo.end();
    }

    public void fillRect(int x, int y, int w, int h) {

        if (allClipped) return;

        if(w<0 || h<0) return;

        startPrimitives();

        fillRectPrimitive(this, x, y, w, h);

        endPrimitives();

    }

    public static void fillRectPrimitive(Graphics g, int x, int y, int w, int h) {

       /* if(g.allClipped)
            Gdx.app.log("Graphics", "_fillRect (AllClipped)");
        else
            Gdx.app.log("Graphics", "_fillRect ("+x+","+y+","+w+","+h+")");*/
        // WARNING: No esta afectado por el clip!!
        if (g.allClipped) return;

        x += g.translatex;
        y += g.translatey;

        if (x < g.clipx) {
            w -= (g.clipx - x);
            x = g.clipx;
        }

        if (y < g.clipy) {
            h -= (g.clipy - y);
            y = g.clipy;
        }

        if (x + w > g.clipx + g.clipw) {
            w = (g.clipx + g.clipw) - x;
        }

        if (y + h > g.clipy + g.cliph) {
            h = (g.clipy + g.cliph) - y;
        }

        if(w<0 || h<0) return;

        int finaly = y;

        g.shapeRenderer.setColor(g.currentColor);
        g.shapeRenderer.rect(x, finaly, w, h);

    }

    public void fillArc(int cx, int cy, int rx, int ry, int a0, int a1) {
        if (allClipped) return;

        cx += translatex;
        cy += translatey;

        Display.clippedAreafbo.begin();

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(Display.clippedAreaCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.ellipse(cx, cy, rx, ry);

        shapeRenderer.end();

        Display.clippedAreafbo.end();

        if (fromImage == null)
            Display.fbo.begin();
        else
            fromImage.fbo.begin();

        drawClippedArea();

        if (fromImage == null)
            Display.fbo.end();
        else
            fromImage.fbo.end();
    }

    public void drawArc(int cx, int cy, int rx, int ry, int a0, int a1) {
        if (allClipped) return;

        cx += translatex;
        cy += translatey;

        Display.clippedAreafbo.begin();

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(Display.clippedAreaCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.ellipse(cx, cy, rx, ry);

        shapeRenderer.end();

        Display.clippedAreafbo.end();

        if (fromImage == null)
            Display.fbo.begin();
        else
            fromImage.fbo.begin();

        drawClippedArea();

        if (fromImage == null)
            Display.fbo.end();
        else
            fromImage.fbo.end();
    }

    public void fillRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
        fillRect(i, i1, i2, i3);
    }

    public void drawRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
        drawRect(i, i1, i2, i3);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        if (allClipped) return;

        x1 += translatex;
        y1 += translatey;
        x2 += translatex;
        y2 += translatey;

        int minx, maxx, miny, maxy;

        if (x1 < x2) {
            minx = x1;
            maxx = x2;
        } else {
            minx = x2;
            maxx = x1;
        }

        if (y1 < y2) {
            miny = y1;
            maxy = y2;
        } else {
            miny = y2;
            maxy = y1;
        }

        if (minx > clipx + clipw || miny > clipy + cliph || maxx < clipx || maxy < clipy) return;

        Display.clippedAreafbo.begin();

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(Display.clippedAreaCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(currentColor);
        shapeRenderer.rectLine(x1 + 1, y1 + 1, x2 + 1, y2 + 1, 1);
        shapeRenderer.end();

        Display.clippedAreafbo.end();

        if (fromImage == null)
            Display.fbo.begin();
        else
            fromImage.fbo.begin();

        drawClippedArea();

        if (fromImage == null)
            Display.fbo.end();
        else
            fromImage.fbo.end();
    }

    public void drawClippedArea()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(Display.clippedAreaTexture, clipx, clipy, clipw, cliph,
                clipx / (float) Display.clippedAreaTexture.getWidth(), clipy / (float) Display.clippedAreaTexture.getHeight(),
                (clipx + clipw) / (float) Display.clippedAreaTexture.getWidth(), (clipy + cliph) / (float) Display.clippedAreaTexture.getHeight());
        batch.end();

    }
}
