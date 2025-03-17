package com.mygdx.mongojocs.midletemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public class Font {

    public static final int FACE_SYSTEM = 0;
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;

    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_UNDERLINED = 4;

    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_SMALL = 8;
    public static final int SIZE_LARGE = 16;

    //public static final int pixelWidths[] = { 8, 10, 12};
    //public static final int pixelHeights[] = { 10, 12, 14};



    int face;
    int style;
    int size;

    GlyphLayout layout;

    Font()
    {
        layout = new GlyphLayout();
    }

    public static Font getFont(int face, int style, int size)
    {
        Font f = new Font();

       f.face = face;
       f.style = style;
       f.size = size;

       /*String hash = face+"-"+ style+"-"+size;
       if(Graphics.bitmapFonts.get(hash) == null)
           Graphics.fontGenerate(face, style, size, Color.WHITE);*/

       return f;
    }

    public static Font getDefaultFont()
    {
        return getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    }

    public int getHeight()
    {
        if((size & SIZE_LARGE) != 0)
            return 16;
        else if((size & SIZE_SMALL) != 0)
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

    public int stringWidth(String s)
    {
        String hash = face+"-"+ style+"-"+size;
        BitmapFont fnt = null;

        if(!Graphics.bitmapFonts.containsKey(hash))
        {
            Graphics.fontGenerate(face, style, size, Color.WHITE);
        }
        fnt = Graphics.bitmapFonts.get(hash);

        if(fnt != null) {
            layout.setText(fnt, s);
            return (int) layout.width;
        }
        else
            return 0;

    }

    public int _stringWidth(String s)
    {
        String hash = face+"-"+ style+"-"+size;
        BitmapFont fnt = null;

        if(!Graphics.bitmapFonts.containsKey(hash))
        {
            Graphics._fontGenerate(face, style, size, Color.WHITE);
        }
        fnt = Graphics.bitmapFonts.get(hash);

        if(fnt != null) {
            layout.setText(fnt, s);
            return (int) layout.width;
        }
        else
            return 0;

    }

    public int charWidth(char c) {

        char array[]={c};
        int space;
        if((size & SIZE_LARGE) != 0)
            space = 4;
        else if((size & SIZE_SMALL) != 0)
            space = 2;
        else
            space = 3;
        return stringWidth(new String(array))+space;

    }

    public int _charWidth(char c) {

        char array[]={c};
        int space;
        if((size & SIZE_LARGE) != 0)
            space = 4;
        else if((size & SIZE_SMALL) != 0)
            space = 2;
        else
            space = 3;
        return _stringWidth(new String(array))+space;

    }
}
