package com.mygdx.mongojocs.mr2;


// -----------------------------------------------
// Microjocs The Love Boat Project Rev.0 (1.3.2004)
// ===============================================
// Client com.mygdx.mongojocs.mr2.Debug-Engine class
// -----------------------------------------------


//#ifdef com.mygdx.mongojocs.mr2.Debug


//#ifdef J2ME

//#endif

//#ifdef DOJA
//#endif


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
		String[] strPartido = gc.textBreak(str, gc.canvasWidth, debugFont);

		for (int t=0 ; t<strPartido.length ; t++)
		{
			for (int i=1 ; i<cachedStr.length ; i++)
			{
				cachedStr[i-1] = cachedStr[i];
			}

			cachedStr[cachedStr.length-1] = strPartido[t];
		}
	}

/*
	if (!gc.canvasShow)
	{
		gc.gameDraw();
		gc.waitTime(1200);
	}
*/
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

};

//#endif
