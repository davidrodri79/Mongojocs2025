package com.mygdx.mongojocs.sanfermines2006;

// -----------------------------------------------
// com.mygdx.mongojocs.sanfermines2006.Debug - class
// ===============================================


//#ifdef DEBUG


import com.badlogic.gdx.Gdx;
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
static int debugFontHeight;

static long[] milis = new long[6];
static int[] time = new int[6];

static boolean fillYellow = false;

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
	debugFontHeight = debugFont.getHeight();
	cachedStr = new String[(gc.canvasHeight-gc.listenerHeight) / debugFontHeight];
}


// -------------------
// spd start
// ===================

static synchronized public void spdStart(int type)
{
	milis[type] = System.currentTimeMillis();
}


// -------------------
// spd start
// ===================

static synchronized public void spdStop(int type)
{
	time[type] = (int)(System.currentTimeMillis() - milis[type]);
}


// -------------------
// debug println
// ===================

static synchronized public void println(String str)
{
	System.out.println(str);
	Gdx.app.log("Debug", str);

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
}


// -------------------
// debug printFreeMem
// ===================

static synchronized public void printFreeMem(String str)
{
	System.gc();
	println("*FM*("+str+"): "+Runtime.getRuntime().freeMemory());
	println("MemUsed: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
}

// -------------------
// debug Draw
// ===================

static synchronized public void debugDraw(GameCanvas gc, Graphics scr)
{
	scr.setFont(debugFont);

	gc.fillDraw(0x000000,  0,0, gc.canvasWidth, gc.canvasHeight);


// Imprimimos Consola
	int y = 0;

	gc.putColor(0xffffff);
	for (int i=0 ; i<cachedStr.length ; i++)
	{
		if (cachedStr[i] != null)
		{
			drawString(scr, cachedStr[i], 0, y);
			y += debugFont.getHeight();
		}
	}

//#ifdef DEBUG_MEMORY

// Imprimimos CPU y GFX speed
	gc.fillDraw(0x000000,  0,0, gc.canvasWidth, debugFontHeight*4);

	gc.putColor(0xffffff);

	drawString(scr, "C:"+time[0]+" G:"+time[1], 0, 0);
	drawString(scr, "ST:"+time[2]+" SD:"+time[3], 0, debugFontHeight);
	drawString(scr, "I:"+time[4]+" H:"+time[5], 0, debugFontHeight*2);
	System.gc();
	drawString(scr, "FM:"+Runtime.getRuntime().freeMemory(), 0, debugFontHeight*3);

//#endif
}



static public void drawString(Graphics scr, String str, int x, int y)
{
//#ifdef J2ME
	scr.drawString(str, x, y, 20);
//#elifdef DOJA
//#endif
}

// <=- <=- <=- <=- <=-

};

//#endif
