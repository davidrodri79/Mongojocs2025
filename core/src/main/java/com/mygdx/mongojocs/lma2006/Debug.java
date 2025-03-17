package com.mygdx.mongojocs.lma2006;


import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;

public class Debug
{

// *******************
// -------------------
// debug - Engine
// ===================
// *******************

static String[] cachedStr;

static boolean enabled = false;

static GameCanvas gc;

static Font debugFont;

// -------------------
// debug Create
// ===================

static synchronized public void debugCreate(GameCanvas gCanvas)
{
	gc = gCanvas;
//#ifdef J2ME
	debugFont = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL);
//#elifdef DOJA
//#endif
	cachedStr = new String[gc.canvasHeight / debugFont.getHeight()];
}


// -------------------
// debug println
// ===================

static synchronized public void println(String str)
{
//#ifdef DebugConsole
	System.out.println(str);
//#endif

	if (cachedStr != null)
	{
		String[] strPartido = textBreak(str, gc.canvasWidth, debugFont);

		for (int t=0 ; t<strPartido.length ; t++)
		{
			for (int i=1 ; i<cachedStr.length ; i++)
			{
				cachedStr[i-1] = cachedStr[i];
			}

			cachedStr[cachedStr.length-1] = strPartido[t];
		}
	}
}


// -------------------
// debug Draw
// ===================

static synchronized public void debugDraw(GameCanvas gc, Graphics scr)
{
	scr.setColor(0);
//#ifdef J2ME
	scr.setClip (0,0, gc.canvasWidth, gc.canvasHeight );
//#endif
	scr.fillRect(0,0, gc.canvasWidth, gc.canvasHeight );

	scr.setFont(debugFont);

	int y = 0;

	scr.setColor(0xffffff);
	for (int i=0 ; i<cachedStr.length ; i++)
	{
		if (cachedStr[i] != null)
		{
		//#ifdef J2ME
			scr.drawString(cachedStr[i], 0, y, 20);
		//#elifdef DOJA
		//#endif
			y += debugFont.getHeight();
		}
	}
}

// <=- <=- <=- <=- <=-

public static String[] textBreak(String texto, int width, Font f)
{
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int[] positions = new int[512];
	int count = 0;

	while ( posOld < texto.length() )
	{
		size = 0;

		pos = posIni;

		while ( size < width )
		{
			if ( pos == texto.length()) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//juan			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat==0x20 ) { posOld = pos - 1; }
//#ifdef J2ME
			size += f.charWidth((char)dat);
//#elifdef DOJA
//#endif
	    }
    
	    if (posOld - posIni < 1)
		{ 
			while ( pos < texto.length() && texto.charAt(pos) >= 0x30 ) {pos++;}
			posOld = pos;
		}

		posIni = posOld + 1;
		positions[count] = posOld;
		count++;
	}

	String str[] = new String[count];
	posIni = 0;
	int posEnd;

	for(int i = 0; i < count; i++)
	{
		posEnd = positions[i];
		str[i] = texto.substring(posIni, posEnd);
		posIni = posEnd + 1;
	}

	return str;
}

};

//#endif
