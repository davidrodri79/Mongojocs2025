package com.mygdx.mongojocs.iapplicationemu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygdx.mongojocs.iapplicationemu.Graphics;

public class Font {

    public static final int FACE_PROPORTIONAL = 1;
    public static final int FACE_SYSTEM = 2;
    public static final int STYLE_PLAIN = 4;
    public static final int STYLE_BOLD = 8;
    public static final int SIZE_SMALL = 32;
    public static final int SIZE_MEDIUM = 64;
    public static final int SIZE_LARGE = 128;


    public int flags;

    GlyphLayout layout;

    public Font(int f)
    {
        layout = new GlyphLayout();
        flags = f;
    }

    public static Font getFont(int f) {
        return new Font(f);
    }

    public static Font getDefaultFont() {
        return new Font(FACE_PROPORTIONAL | STYLE_PLAIN | SIZE_MEDIUM);
    }

    public int getAscent() {
        return 0;
    }

    public int stringWidth(String s)
    {
        String hash = flags+"";
        BitmapFont fnt = null;

        if(!Graphics.bitmapFonts.containsKey(hash))
        {
           Graphics.fontGenerate(flags, Color.WHITE);
        }
        fnt = Graphics.bitmapFonts.get(hash);

        if(fnt != null) {

            int space;
            if((flags & SIZE_LARGE) != 0)
                space = 4;
            else if((flags & SIZE_SMALL) != 0)
                space = 2;
            else
                space = 3;

            layout.setText(fnt, s);
            return (int) layout.width + (s.length() == 1 ? space : 0);
        }
        else
            return 0;

    }

    public int _stringWidth(String s)
    {
        String hash = flags+"";
        BitmapFont fnt = null;

        if(!com.mygdx.mongojocs.midletemu.Graphics.bitmapFonts.containsKey(hash))
        {
            Graphics._fontGenerate(flags, Color.WHITE);
        }
        fnt = Graphics.bitmapFonts.get(hash);

        if(fnt != null) {
            layout.setText(fnt, s);
            return (int) layout.width;
        }
        else
            return 0;

    }

    public int getHeight()
    {
        if((flags & SIZE_LARGE) != 0)
            return 16;
        else if((flags & SIZE_SMALL) != 0)
            return 12;
        else
            return 14;
    }

    public static  int getPixelWidth(int size)
    {
        if((size & SIZE_LARGE) != 0)
            return 14;
        else if((size & SIZE_SMALL) != 0)
            return 10;
        else
            return 12;
    }

    public int getBBoxWidth(String s) {
        return stringWidth(s);
    }

    public int _getBBoxWidth(String s) {
        return _stringWidth(s);
    }
}
