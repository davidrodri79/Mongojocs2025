

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.7 (08.4.2005)
// ===============================================
// Representacion Grafica
// ------------------------------------


//#define RMS_ENGINEx


//#ifdef PACKAGE
package com.mygdx.mongojocs.nopasseu;

//#endif


//#ifdef J2ME



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

//#ifdef FULLSCREEN
		//#ifdef PLAYER_MOTC450
		//#elifdef MIDP20
		//#elifdef NOKIAUI
			public class GameCanvas extends FullCanvas
		//#else
		//#endif
	//#else
	//#endif

 implements Runnable

//#ifndef FULLSCREEN
//#endif

//#elifdef DOJA
//#endif
{

Game ga;

static GameCanvas gc;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga) 
{
	this.ga = ga;
	gc = this;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME
public void showNotify()
{
//#ifdef DebugConsole
	Debug.println (" -+- showNotify()");
//#endif

	gamePaused = false;

	gameForceRefresh = true;
	gameSoundRefresh = true;

	intKeyX = intKeyY = intKeyMisc = intKeyMenu = 0;
}

public void hideNotify()
{
//#ifdef DebugConsole
	Debug.println (" -+- hideNotify()");
//#endif

	gamePaused = true;

	gameSoundOld = soundOld;
	gameSoundLoop = soundLoop;
	soundStop();
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;
boolean gamePaused = false;
boolean gameForceRefresh = false;
boolean gameSoundRefresh = false;
int gameSoundOld = -1;
int gameSoundLoop;
int gameLevelLast;

long gameMilis, CPU_Milis;
int gameSleep;
int mainSleep = 50;

public void run()
{
	System.gc();
	biosCreate();
	System.gc();

	while (!gameExit)
	{
		gameMilis = System.currentTimeMillis();

		if (!gamePaused)
		{
			lastKeyMenu = keyMenu;	// Keys del Frame Anterior
			lastKeyX    = keyX;
			lastKeyY    = keyY;
			lastKeyMisc = keyMisc;

			keyMenu = intKeyMenu;	// Keys del Frame Actual
			keyX    = intKeyX;
			keyY    = intKeyY;
			keyMisc = intKeyMisc;

			if (limpiaKeyMenu) {limpiaKeyMenu=false; intKeyMenu = 0;}

			try {
				Gdx.app.log("GameCanvas", "run() iteration");
				biosTick();
				if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}
				gameDraw();

				if (gameSoundRefresh)
				{
					gameSoundRefresh = false;
	
				//#ifndef NK-s60
				//#else
					gameSound = false;
				//#endif
				} else {
					soundTick();
				}

			} catch (Exception e)
			{
			//#ifdef Debug
				Debug.println("*** Exception Logica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}
		}

		gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
		try	{Thread.sleep( gameSleep < 1? 1:gameSleep );} catch(Exception e) {}

//	System.gc();

	}

	soundStop();
	biosDestroy();
	ga.destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Forzamos que se refresque la pantalla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void gameDraw()
{
	//try {Thread.sleep(100);} catch(Exception e) {}
	canvasShow = true;
	repaint();

//#ifdef J2ME
	serviceRepaints();
//#elifdef DOJA
//#endif
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics scr;

public void paint (Graphics g)
{
	Gdx.app.log("GameCanvas", "paint() call");
	if (canvasShow)
	{
		synchronized (this)
		{
			canvasShow=false;
		
			scr = g;

		//#ifdef DOJA
		//#endif

			try {
				canvasDraw();
			} catch (Exception e)
			{
			//#ifdef Debug
				Debug.println("*** Exception Grafica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}

//#ifdef Debug
	if (Debug.enabled) {Debug.debugDraw(this, scr);}
//#endif
		
		//#ifdef DOJA
		//#endif
		
			scr = null;
		}
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<









// *******************
// -------------------
// keyboard - Engine
// ===================
// *******************

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyMenu, keyX, keyY, keyMisc;
int intKeyMenu, intKeyX, intKeyY, intKeyMisc;
int lastKeyMenu, lastKeyX, lastKeyY, lastKeyMisc;
boolean limpiaKeyMenu = false;

//#ifdef J2ME

public void keyPressed(int keycode)
{
	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;

	switch (keycode)
	{
	case Canvas.KEY_NUM0:
		intKeyMisc=10;
		intKeyMenu= 1;
	return;

	case Canvas.KEY_NUM1:
		intKeyMisc=1; intKeyX = intKeyY = -1;
	return;

	case Canvas.KEY_NUM2:	// Arriba
		intKeyMisc=2; intKeyY=-1;
	return;

	case Canvas.KEY_NUM3:
		intKeyMisc=3; intKeyX= 1; intKeyY=-1;
	return;

	case Canvas.KEY_NUM4:	// Izquierda
		intKeyMisc=4; intKeyX=-1;
	return;

	case Canvas.KEY_NUM5:	// Disparo
		intKeyMisc=5;
		intKeyMenu=2;
	return;

	case Canvas.KEY_NUM6:	// Derecha
		intKeyMisc=6; intKeyX=1;
	return;

	case Canvas.KEY_NUM7:
		intKeyMisc=7; intKeyX=-1; intKeyY= 1;
	return;

	case Canvas.KEY_NUM8:	// Abajo
		intKeyMisc=8; intKeyY=1;
	return;

	case Canvas.KEY_NUM9:
		intKeyMisc=9; intKeyX = intKeyY = 1;
	return;

	case 35:		// *
		intKeyMisc=11;
	return;

	case 42:		// #
		intKeyMisc=12;
	return;

// -----------------------------------------

//#ifdef MO-T720
//#elifdef MO-V3xx
//#elifdef MO-C65x
//#elifdef AL-756
//#elifdef LG-U8150
//#elifdef SG-Z105
//#elifdef SG
//#elifdef NK
	case -6:	// Nokia - Menu Izquierda
		intKeyMenu = -1;
	return;

	case -7:	// Nokia - Menu Derecha
		intKeyMenu = 1;
	return;

//#elifdef SE
//#elifdef SI
//#endif
	}



	switch(getGameAction(keycode))
	{
		case 1:	intKeyY= -1;	return;	// Arriba
		case 6:	intKeyY=  1;	return;	// Abajo
		case 5:	intKeyX=  1;	return;	// Derecha
		case 2:	intKeyX= -1;	return;	// Izquierda
		case 8:	intKeyMenu= 2;	return;	// Fuego
	//#ifdef VI-TSM100
	//#endif
	}
}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void keyReleased(int keycode)
{
	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;
}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#ifdef DOJA

//#endif

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// bios - Engine
// ===================
// *******************

int biosStatus = 0;
int biosStatusOld;

boolean gameSound = true;
boolean gameVibra = true;
boolean gameLight = true;

static final int BIOS_GAME = 0;
static final int BIOS_LOGOS = 11;
static final int BIOS_MENU = 22;
static final int BIOS_POPUP = 33;

// -------------------
// bios Create
// ===================

String[][] gameText;

public void biosCreate()
{
// --------------------------------
// Forzamos a FULL CANVAS para J2ME / MIDP 2.0
// ================================
//#ifdef J2ME
	//#ifdef FULLSCREEN
		//#ifdef MIDP20
			setFullScreenMode(true);
			try {Thread.sleep(300);} catch (Exception e) {}
			canvasWidth = getWidth();
			canvasHeight = getHeight();
		//#endif
	//#endif
//#endif
// ================================


// --------------------------------
// Inicializamos Debug Engine
// ================================
//#ifdef Debug
	Debug.debugCreate(this);
//#endif
// ================================


// --------------------------------
// Pintamos TODO el canvas de forma INMEDIATA
// ================================
	canvasFillTask(0xffffff);		// Color Blanco
	gameDraw();
//#ifdef FULLSCREEN
	canvasHeight -= listenerHeight;
//#endif
	canvasFillTask(0xffffff);		// Color Blanco
	canvasImg = loadImage("/loading");
	gameDraw();						// FORZAMOS una llamada a 'canvasDraw()' AHORA MISMO.
// ================================


// --------------------------------
// Descargamos el MFS de internet, gestionado por una barra de progreso
// ================================
//#ifdef DOJA
//#endif
// ================================



// --------------------------------
// Creamos e inicializamos rmsEngine (Mini Disco Duro)
// ================================
//#ifdef RMS_ENGINE
//#endif
// ================================


	gameCreate();

}


// -------------------
// bios Destroy
// ===================

public void biosDestroy()
{
	savePrefs();

	gameDestroy();
}

// -------------------
// bios Tick
// ===================

public void biosTick()
{

//#ifdef Debug
	if (keyMisc == 11 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; if (!Debug.enabled) {gameForceRefresh=true;}}
//#endif


	switch (biosStatus)
	{
// --------------------
// game Bucle
// --------------------
	case BIOS_GAME:
		gameTick();
	return;
// --------------------

// --------------------
// logos Bucle
// --------------------
	case BIOS_LOGOS:
		if ( logosTick() ) {biosStatus = BIOS_GAME;}
	return;
// --------------------

// --------------------
// menu Bucle
// --------------------
	case BIOS_MENU:
		if ( menuTick() ) {biosStatus = BIOS_GAME;}
	return;
// --------------------

// --------------------
// popup Bucle
// --------------------
	case BIOS_POPUP:
//		if ( popupTick() ) {biosStatus = biosStatusOld;}
	return;
// --------------------
	}
}

// -------------------
// bios Refresh
// ===================

public void biosRefresh()
{
//#ifdef Debug
	Debug.println ("biosRefresh()");
	Debug.println ("biosStatus="+biosStatus);
//#endif

	switch (biosStatus)
	{
// --------------------
// game Refresh
// ====================
	case BIOS_GAME:
		gameRefresh();
	return;
// ====================

// --------------------
// logos Refresh
// ====================
//	case BIOS_LOGOS:
//	return;
// ====================

// --------------------
// menu Refresh
// ====================

	case BIOS_MENU:

		menuListShow=true;
	return;
// ====================

// --------------------
// popup Refresh
// ====================
	case BIOS_POPUP:

//		formShow=true;
//		popupShow = true;
	return;
// ====================
	}
}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// logos - Engine
// ===================
// *******************

int numLogos = 0;
int cntLogos = 0;
String[] nameLogos;
int[] rgbLogos;
int timeLogos;
long timeIniLogos;

public void logosInit(String[] fileNames, int[] rgb, int time)
{
	nameLogos = fileNames;
	numLogos = rgb.length;
	cntLogos = 0;
	rgbLogos = rgb;
	timeLogos = time;

	timeIniLogos = System.currentTimeMillis() - timeLogos;

	biosStatus = BIOS_LOGOS;
}


public boolean logosTick()
{
	if ( (System.currentTimeMillis() - timeIniLogos) < timeLogos) {return false;}

	if (cntLogos == numLogos) {return true;}

	canvasFillTask(rgbLogos[cntLogos]);

	canvasImg = loadImage(nameLogos[cntLogos]);

	cntLogos++;

	timeIniLogos = System.currentTimeMillis();

	return false;
}

// <=- <=- <=- <=- <=-









































// *******************
// -------------------
// menuList - Engine - Rev.0 (20.6.2003)
// ===================
// *******************

boolean menuList_ON=false;		// Estado del menuList Engine
boolean menuListShow=false;

boolean menuListScreenClear = false;

int menuListMode;

int menuListPos;
int menuListCMD;

int menuListX;
int menuListY;
int menuListSizeX;
int menuListSizeY;

int menuListScrY;
int menuListScrSizeY;

String	menuListStr[][];
int		menuListDat[][];

static final int ML_TEXT = 1;
static final int ML_SCROLL = 2;
static final int ML_OPTION = 3;
static final int ML_SCREEN = 4;
static final int ML_NONE = 5;

int menuListTabIndexPos;
int menuListTabIndexSize;
short[] menuListTabIndex;

// -------------------
// menuList Init
// ===================

public void menuListInit(int x, int y, int sizeX, int sizeY)
{
	menuListX = x;
	menuListY = y;
	menuListSizeX = sizeX;
	menuListSizeY = sizeY;
}

// -------------------
// menuList Clear
// ===================

public void menuListClear()
{
	menuList_ON=false;
	menuListShow=false;

	menuListStr=null;
	menuListDat=null;

//	menuListPos=0;

	menuListTabIndex = null;
}

// -------------------
// menuList Add
// ===================

public void menuListAdd(int Dato, String Texto)
{
	Font f = menuListGetFont(Dato);

	if ( f.stringWidth(Texto) <= menuListSizeX)
	{
		menuListAdd(Dato, new String[] {Texto}, 0, 0);
	} else {

		String[] Textos = textBreak(Texto, menuListSizeX, menuListGetFont(Dato) );

		for (int i=0 ; i<Textos.length ; i++)
		{
		menuListAdd(Dato, new String[] {Textos[i]}, 0, 0);
		}
	}
}


public void menuListAdd(int Dato, String[] Texto)
{
	for (int i=0 ; i<Texto.length ; i++)
	{
	menuListAdd(Dato, Texto[i]);
	}
}


public void menuListAdd(int Dato, String Texto, int Dat1)
{
	menuListAdd(Dato, new String[] {Texto}, Dat1, 0);
}

public void menuListAdd(int Dato, String[] Texto, int Dat1)
{
	menuListAdd(Dato, new String[] {Texto[0]}, Dat1, 0);
}

public void menuListAdd(int Dat0, String[] Texto, int Dat1, int Dat2)
{
	int Lines=(menuListDat!=null)?menuListDat.length:0;

	String Str[][] = new String[Lines+1][];
	int Dat[][] = new int[Lines+1][];

	int i=0;

	for (; i<Lines; i++)
	{
	Dat[i] = menuListDat[i];
	Str[i] = menuListStr[i];
	}

	Font f = menuListGetFont(Dat0);

	int TextoSizeX = 2;
	for (int SizeX, t=0 ; t<Texto.length ; t++) {SizeX=f.stringWidth(Texto[t]); if ( TextoSizeX < SizeX ) {TextoSizeX=SizeX;}}


	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2, TextoSizeX, f.getHeight() };

	menuListStr=Str;
	menuListDat=Dat;
}


// -------------------
// menuList Set
// ===================

public void menuListSet_Text()
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_TEXT;
}

public void menuListSet_Scroll()
{
	menuListScrY = menuListSizeY;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCROLL;

	menuListScrSizeY = 0; for (int i=0 ; i<menuListDat.length ; i++) {menuListScrSizeY += menuListDat[i][4];}
}

public void menuListSet_Option()
{
	menuListSet_Option(0);
}

public void menuListSet_Option(int Pos)
{
	menuListPos=Pos;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_OPTION;
}

public void menuListSet_Screen()
{

	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCREEN;


	menuListTabIndexPos = 0;
	menuListTabIndexSize = 0;
	menuListTabIndex = new short[512];

	int pos = 0;
	int size = 0;
	int ultSpc = 0;
	int act = 0;

	boolean skipSpc = true;

	while (act<menuListDat.length)
	{
		if (skipSpc)
		{
			if (menuListStr[act][0].equals(" "))
			{
				act++;
			} else {
				pos = act;
				size = 0;
				ultSpc = -1;
				skipSpc = false;
			}

		} else {

			if (menuListStr[act][0].equals(" "))
			{
				ultSpc = act;
			}

			boolean finalCampo = false;
	
			if ( (size += menuListDat[act][4]) > menuListSizeY ) {act--; finalCampo = true;}
	
			if ( act == menuListDat.length-1 ) {ultSpc = -1; finalCampo = true;}
	
			if ( finalCampo )
			{
				if (ultSpc != -1) {act = ultSpc - 1;}
	
				menuListTabIndex[(menuListTabIndexSize++)] = (short)(pos);
				menuListTabIndex[(menuListTabIndexSize++)] = (short)(act-pos+1);
	
				skipSpc = true;
			}

			act++;
		}
	}

}


public void menuListSet_None()
{
	menuListClear();

	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_NONE;
}



// -------------------
// menuList Tick
// ===================

public boolean menuListTick(int movY, int boton)
{
	boolean fire = boton>0;
	boolean back = boton<0;

	switch (menuListMode)
	{
	case ML_OPTION:

		if (movY ==-1 && menuListPos > 0) {menuListPos--; menuListShow=true;}
		else
		if (movY == 1 && menuListPos < menuListDat.length-1) {menuListPos++; menuListShow=true;}
		else
		if (movY ==-1 && menuListPos == 0) {menuListPos = menuListDat.length-1; menuListShow=true;}
		else
		if (movY == 1 && menuListPos == menuListDat.length-1) {menuListPos = 0; menuListShow=true;}

		if (fire)
		{
		if (++menuListDat[menuListPos][2] == menuListStr[menuListPos].length) {menuListDat[menuListPos][2]=0;}
		menuListCMD=menuListDat[menuListPos][1];
		menuListShow=true;
		return true;
		}

		if (back) {menuListCMD=ACTION_NONE; return true;}
	break;


	case ML_SCROLL:
		menuListScrY--;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}
		if (back) {menuListCMD=ACTION_NONE; return true;}

		menuListShow=true;
	break;



	case ML_SCREEN:

		if (movY != 0)
		{
			int newPos = menuListTabIndexPos + (movY * 2);

			if (newPos >= 0 && newPos < menuListTabIndexSize)
			{
				menuListScreenClear = true;
				menuListShow=true;
				gameDraw();

				menuListTabIndexPos = newPos;
				menuListShow=true;
			}
		}
		else if (fire)
		{
			menuListTabIndexPos += 2;

			if (menuListTabIndexPos >= menuListTabIndexSize) {menuListCMD=-2; return true;}

			menuListScreenClear = true;
			menuListShow=true;
			gameDraw();

			menuListShow=true;
		}

		if (back) {menuListCMD=ACTION_NONE; return true;}

	break;
	}


	return false;
}


// -------------------
// menuList Opt
// ===================

public int menuListOpt()
{
	return(menuListDat[menuListPos][2]);
}


// -------------------
// menuList Draw
// ===================

public void menuListDraw(Graphics g)
{
	if (!menuList_ON || !menuListShow) {return;}

	menuListShow = false;

	if (menuImg!=null) {canvasFillDraw(0); showImage(menuImg, 0,0);}


//#ifdef J2ME
	g.setClip(menuListX, menuListY, menuListSizeX, menuListSizeY);
//#endif

	switch (menuListMode)
	{
	case ML_OPTION:

		int aspaWidth = menuListStr.length>1?menuListGetFont(0).charWidth('<'):0;

		String[] texto = textBreak(menuListStr[menuListPos][menuListDat[menuListPos][2]], (menuListSizeX - aspaWidth - 4), menuListGetFont(0) );

		int height = (texto.length * menuListDat[menuListPos][4]);

    //int y = menuListY + (( menuListSizeY / 4) * 3) - (height/2);

		int y = menuListY + (( menuListSizeY / 6) * 5);
		if (y + height > menuListSizeY) {y = menuListY + (menuListSizeY-height);}


		alphaFillDraw(0x80000000, 0,y-2, canvasWidth, height+4 );

	// Imprimimos "<" y ">"
		if (aspaWidth > 0)
		{
			menuListTextDraw(g, 1, "<", 1, y + ((height-menuListDat[menuListPos][4])/2) );
			menuListTextDraw(g, 1, ">", canvasWidth - aspaWidth - 1, y + ((height-menuListDat[menuListPos][4])/2) );
		}


		for (int i=0 ; i<texto.length ; i++)
		{
			menuListTextDraw(g, menuListDat[i][0], texto[i], 0,y);

			y += menuListDat[menuListPos][4];
		}
	break;


	case ML_SCROLL:

		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			menuListTextDraw(g, menuListDat[i][0], menuListStr[i][0], 0,y);

			y += menuListDat[i][4];
		}
	break;


	case ML_SCREEN:

		alphaFillDraw(0x80000000, 0,0, canvasWidth, canvasHeight);

		if (menuListScreenClear) {menuListScreenClear = false; break;}

		y = 0;

		int ini =  menuListTabIndex[menuListTabIndexPos];
		int size = menuListTabIndex[menuListTabIndexPos+1];

		for (int i=0 ; i<size ; i++)
		{
			y += menuListDat[ini+i][4];
		}

		y = ((menuListSizeY - y) >> 1) + menuListY;

		for (int i=0 ; i<size ; i++)
		{
			menuListTextDraw(g, menuListDat[i][0], menuListStr[ini+i][0], menuListX, y);

			y += menuListDat[i][4];
		}
	break;
	}
}






public void menuListTextDraw(Graphics g, int format, String str, int x, int y)
{
	Font font = menuListGetFont(format);

	g.setFont(font);

	if (format == 0)
	{
		x = menuListX + (( menuListSizeX - font.stringWidth(str) ) / 2);
	}

//#ifdef J2ME

	g.setColor(0);
	g.drawString(str, x-1,y-1, 20);
	g.drawString(str, x+1,y-1, 20);
	g.drawString(str, x-1,y+1, 20);
	g.drawString(str, x+1,y+1, 20);

	g.setColor(0xFFFFFF);
	g.drawString(str, x,y, 20);

//#elifdef DOJA
//#endif

}



public Font menuListGetFont(int dat)
{
//#ifdef J2ME
	return Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL);
//#elifdef DOJA
//#endif
}


// <=- <=- <=- <=- <=-











// *******************
// -------------------
// wait - Engine
// ===================
// *******************

long waitMillis;

// -------------------
// wait Start - Guardamos Tiempo ACTUAL para posteriores usos...
// ===================

public void waitStart()
{
	waitMillis = System.currentTimeMillis();
}

// -------------------
// wait Stop - Hacemos una espera tomando como tiempo inicial cuando se llamo a "waitStart()"
// ===================

public void waitStop(int time)
{
	int wait = time - ((int)(System.currentTimeMillis() - waitMillis));
	if (wait > 0) {waitTime(wait);}
}

// -------------------
// wait Finished - Devuelve "true" si ha pasado el tiempo especificado desde que se llamo a "waitStart()"
// ===================

public boolean waitFinished(int time)
{
	return (time - ((int)(System.currentTimeMillis() - waitMillis))) <= 0;
}

// -------------------
// wait Time - Hacemos una espera del tiempo especificado
// ===================

public void waitTime(int time)
{
	try {
		Thread.sleep(time);
	} catch (Exception e) {}
}

// <=- <=- <=- <=- <=-
















// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

// -------------------
// prefs Update - Leemos/Salvamos archivo de preferencias, resultado: byte[] que grabamos la ultima vez o "null" si NO Existe
// ===================

// Leemos cuando le pasamos "null" como paremetro.
// Salvamos cuando le pasamos "byte[]" como parametro.

public byte[] updatePrefs(byte[] bufer)
{
//#ifdef J2ME

	RecordStore rs;

	try {

		rs = RecordStore.openRecordStore("prefs", true);

		if (bufer != null && bufer.length > 0)
		{
			if (rs.getNumRecords() == 0)
			{
				rs.addRecord(bufer, 0, bufer.length);
			} else {
				rs.setRecord(1, bufer, 0, bufer.length);
			}
		} else {
			if (rs.getNumRecords() == 0)
			{
			bufer = null;
			} else {
			bufer = rs.getRecord(1);
			}
		}

		rs.closeRecordStore();

	} catch(Exception e) {return null;}

	return bufer;

//#elifdef DOJA
//#endif
}

// <=- <=- <=- <=- <=-
















// *******************
// -------------------
// util - Engine
// ===================
// *******************

// -------------------
// regla3
// ===================

public int regla3(int x, int min, int max) {return (x*max)/min;}


// -------------------
// rnd
// ===================

int RND_Cont = (int)(System.currentTimeMillis()>>8 & 0x0F);
int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

public int rnd(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
}


// -------------------
// colision
// ===================

public boolean colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 >= x1+xs1 ) || ( x2+xs2 <= x1 ) || ( y2 >= y1+ys1 ) || ( y2+ys2 <= y1 ));
}

// <=- <=- <=- <=- <=-














// *******************
// -------------------
// textos - Engine - v1.2 - Rev.4 (28.9.2004)
// ===================
// *******************

// -------------------
// textos Create
// ===================

public String[][] textosCreate(byte[] tex)
{
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;

	// MONGOFIX ==========================================
	char tex_char[] = new char[tex.length];
	for(int i = 0; i < tex.length; i++)
		if(tex[i] < 0)
			tex_char[i]=(char)(tex[i]+256);
		else
			tex_char[i]=(char)tex[i];
	//=================================================

	int size = 0;
	String textos = new String(tex_char);

	for (int i=0 ; i<textos.length() ; i++)
	{
		int letra = textos.charAt(i) & 0xff;

		if (campo)		// Estamos buscando el FINAL de un campo?
		{
			if (letra == 0x7D)		// '}' Final del campo?
			{
				subCampo = false;
			}

			if (letra < 0x20 || letra == 0x7C || letra == 0x7D)	// Buscamos cuando Termina un campo
			{
				data[dataPos + 1] = (short) i;				// Nos apuntamos el index final del campo
				dataPos += 2;

				campo = false;		// Pasamos a modo Buscar inicio de un campo
			}

		} else {	// Estamos buscando cuando EMPIEZA un campo.

			if (letra == 0x7D)	// '}'
			{
				subCampo = false;
			}
			else
			if (letra == 0x7B)	// '{'
			{
				dataBak = dataPos;		// Nos apuntamos el index para marcar numero de sub-campos en esta ID
//				data[dataPos++] = 0;	// Ponemos '0' sub-campos para esta ID
				dataPos++;
				subCampo = true;
				size++;					// Incrementamos una ID nueva

				primerEnter = true;		// Activamos ke debemos eskivar el primer Enter ke encontremos
			}
			else
			if (subCampo && letra == 0x0A)	// Campos vacios en los sub-campos?
			{
				if (primerEnter)			// Si es el primer enter lo eskivamos
				{
					primerEnter = false;

				} else {

//					data[dataPos++] = 0;	// Apuntamos index de la linea vacia
					dataPos++;
					data[dataPos++] = -1;	// Tama�o de 'enter'
					data[dataBak]--;	// Incrementamos numero de subcampos (negativo para poder identificarlo luego)
				}
			}
			else
			if (letra >= 0x20)	// Buscamos cuando Empieza un campo nuevo (Caracter ASCII valido)
			{
				campo = true;	// Pasamos a modo campo encontrado, buscar fin del campo.

				data[dataPos] = (short) i;	// Nos apuntamos el index inicial de campo

				if (!subCampo)	// Si estamos en un subCampo (una ID con varios campos):
				{
					size++;		// Incrementamos una ID nueva
				} else {
					data[dataBak]--;	// Incrementamos numero de subcampos (negativo para poder identificarlo luego)
				}

				primerEnter = true;		// Activamos ke debemos eskivar el primer Enter ke encontremos
			}
		}
	}

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
		int numCampos = data[dataPos];

		if (numCampos < 0)
		{
			numCampos *= -1;
			dataPos++;
		} else {
			numCampos = 1;
		}

		strings[i] = new String[numCampos];

		for (int t=0 ; t<numCampos ; t++)
		{
			int posIni = data[dataPos++];
			int posFin = data[dataPos++];
			strings[i][t] = ( posFin<0? " " : textos.substring(posIni, posFin) );
		}
	}

	return strings;
}

// <=- <=- <=- <=- <=-
















// *******************
// -------------------
// render - Engine
// ===================
// *******************

// -------------------
// put Color :: La forma correcta de poner un color siendo 100% compatible con todos los handsets
// ===================

public void putColor(int rgb)
{
//#ifdef J2ME
	scr.setColor(rgb);
//#elifdef DOJA
//#endif
}


// -------------------
// fill Draw
// ===================

public void fillDraw(int rgb, int x, int y, int width, int height)
{
//#ifdef J2ME
	scr.setClip(x, y, width, height);
//#endif
	putColor(rgb);
	scr.fillRect(x, y, width, height);
}

// -------------------
// rect Draw
// ===================

public void rectDraw(int rgb, int x, int y, int width, int height)
{
//#ifdef J2ME
	scr.setClip(x, y, width, height);
//#endif
	putColor(rgb);
	scr.drawRect(x, y, width, height);
}


// -------------------
// alpha Fill Draw
// ===================

public void alphaFillDraw(long argb, int x, int y, int width, int height)
{
//#ifdef MIDP20

	if(scr.fromImage == null)
		Display.fbo.begin();
	else
		scr.fromImage.fbo.begin();

	Gdx.gl.glEnable(GL20.GL_BLEND);
	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	scr.shapeRenderer.setProjectionMatrix(scr.camera.combined);
	scr.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	float a = ((argb >> 24)  & 0xff) /255.f;
	float r = ((argb & 0x00ff0000) >> 16) /255.f;
	float g = ((argb & 0x0000ff00) >> 8) /255.f;
	float b = (argb & 0x000000ff)/255.f;
	scr.shapeRenderer.setColor(new Color(r,g,b,a));
	scr.shapeRenderer.rect(x, y, width, height);
	scr.shapeRenderer.end();
	Gdx.gl.glDisable(GL20.GL_BLEND);

	if(scr.fromImage == null)
		Display.fbo.end();
	else
		scr.fromImage.fbo.end();
//#endif
}

// -------------------
// show Image
// ===================

//#ifdef J2ME
// ----------------------------------------------------------

public void showImage(Image Img)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ----------------------------------------------------------

public void showImage(Image Img, int X, int Y)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{ 
//#ifdef SI
//#else
	if (DestX >= canvasWidth || DestY >= canvasHeight) {return;}
//#endif

	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void showImage(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	scr.setClip(TopX, TopY, FinX, FinY);
	scr.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------
//#elifdef DOJA
// ----------------------------------------------------------
//#endif


// -------------------
// sprite Draw
// ===================

//#ifdef J2ME
// ----------------------------------------------------------

public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int flip=0;
	if (sizeX < 0) {sizeX*=-1; flip|=1;}
	if (sizeY < 0) {sizeY*=-1; flip|=2;}
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame  ] + 128;

	if (destX >= canvasWidth || destY >= canvasHeight || (destX + sizeX) <= 0 || (destY + sizeY) <= 0) {return;}

//#ifdef SI-x55
//#endif

	scr.setClip(0, 0, canvasWidth, scrollHeight);	// pipo
//	scr.setClip(0, 0, canvasWidth, canvasHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);
	scr.drawImage(img, destX-sourX, destY-sourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------
//#elifdef DOJA
// ----------------------------------------------------------
// ----------------------------------------------------------
//#endif

// <=- <=- <=- <=- <=-















// *********************
// ---------------------
// inOut - Engine
// =====================
// *********************

// -------------------
// load File
// ===================

public byte[] loadFile(String Nombre)
{
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	return bytes;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void loadFile(byte[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		is.read(buffer, 0, buffer.length);
		is.close();
	}catch(Exception e) {}
	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public byte[] loadFile(String Nombre, int Pos, short[] Size)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] Dest = new byte[Size[Pos]];

	try {
		for (int i=0 ; i<Pos ; i++) {is.skip(Size[i]);}
		is.read(Dest, 0, Dest.length);
		is.close();
	} catch(Exception e) {}
	System.gc();

	return Dest;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void loadFile(short[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		for (int i=0 ; i<buffer.length ; i++)
		{
		buffer[i]  = (short) (is.read() << 8);
		buffer[i] |= is.read();
		}
	is.close();
	} catch(Exception exception) {}

	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// -------------------
// load Image
// ===================

//#ifdef J2ME
// ---------------------------------------------------------

public Image loadImage(String FileName)
{
	FileName += ".png";

	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e)
	{
	//#ifdef Debug
		Debug.println("loadImage: "+FileName+" <File not found>");
		return null;
	//#endif
	}

//#ifdef Debug
	Debug.println("loadImage: "+FileName);
//#endif

	System.gc();
	return Img;
}

// ---------------------------------------------------------
//#elifdef DOJA
// ---------------------------------------------------------

// ---------------------------------------------------------
//#endif

// <=- <=- <=- <=- <=-
















// *********************
// ---------------------
// canvasText - Engine - Gfx
// =====================
// *********************

String canvasTextStr;
int canvasTextX;
int canvasTextY;
int canvasTextRGB;
int canvasTextMode;
boolean canvasTextShow = false;

int printerX = 0;
int printerY = 0;
int printerSizeX = getWidth();
int printerSizeY = getHeight();

// -------------------
// canvasText Create
// ===================

public void canvasTextCreate()
{
	printerX = 0;
	printerY = 0;
	printerSizeX = canvasWidth;
	printerSizeY = canvasHeight;
}

public void canvasTextCreate(int x, int y, int sizeX, int sizeY)
{
	printerX = x;
	printerY = y;
	printerSizeX = sizeX;
	printerSizeY = sizeY;
}

// -------------------
// canvasText Init
// ===================

public void canvasTextTask(String str, int x, int y, int rgb, int mode)
{
	canvasTextStr = str;
	canvasTextX = x;
	canvasTextY = y;
	canvasTextRGB = rgb;
	canvasTextMode = mode;
	canvasTextShow = true;
}

// -------------------
// canvasText Draw
// ===================

public void canvasTextDraw()
{
	canvasTextCreate(0,0, canvasWidth, canvasHeight);
	textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
}

// <=- <=- <=- <=- <=-
















// *********************
// ---------------------
// text - Engine - Gfx
// =====================
// *********************

static final int TEXT_PLAIN =	0x000;
static final int TEXT_BOLD =	0x010;

static final int TEXT_SMALL =	0x000;
static final int TEXT_MEDIUM =	0x100;
static final int TEXT_LARGER =	0x200;

static final int TEXT_LEFT =	0x000;
static final int TEXT_RIGHT =	0x001;
static final int TEXT_HCENTER =	0x002;
static final int TEXT_TOP =		0x000;
static final int TEXT_BOTTON =	0x004;
static final int TEXT_VCENTER =	0x008;

static final int TEXT_OUTLINE =	0x1000;

// -------------------
// text Draw
// ===================

public void textDraw(String Str, int X, int Y, int RGB, int Mode)
{
//#ifdef J2ME
	textDraw(textBreak(Str, printerSizeX, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
//#elifdef DOJA
//#endif
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
//#ifdef J2ME
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
//#elifdef DOJA
//#endif
	scr.setFont(f);

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()*Str.length) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  printerSizeY-(f.getHeight()*Str.length) ;}

	X += printerX;
	Y += printerY;

	for (int i=0 ; i<Str.length ; i++)
	{

	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX +=( printerSizeX-f.stringWidth(Str[i]) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX += printerSizeX-f.stringWidth(Str[i]) ;}


		int despY = i * f.getHeight();


		if ((Mode & TEXT_OUTLINE)!=0)
		{
		putColor(0);
//#ifdef J2ME
		scr.drawString(Str[i],  X-1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY , 20);
//#elifdef DOJA
//#endif
		}

		putColor(RGB);
//#ifdef J2ME
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
//#elifdef DOJA
//#endif
	}

}

// <=- <=- <=- <=- <=-














// *******************
// -------------------
// textBreak - Engine - Rev.4 (4.1.2005)
// ===================
// *******************

short textBreakPos[] = new short[512];

// -------------------
// text Break - IN: String[] ; OUT: String[] - (SYSTEM FONT)
// ===================

public String[] textBreak(String[] str, int width, Font f)
{
	String[] tempStr = new String[256];
	int tempNum = 0;

	for (int i=0 ; i<str.length ; i++)
	{
		String[] tempLines = textBreak(str[i], width, f);

		for (int t=0; t<tempLines.length ; t++) {tempStr[tempNum++] = tempLines[t];}
	}

	String[] finalStr = new String[tempNum];

	for (int i=0 ; i<tempNum ; i++)
	{
		finalStr[i] = tempStr[i];
	}

	return finalStr;
}


// -------------------
// text Break - IN: String ; OUT: String[] - (SYSTEM FONT)
// ===================

public String[] textBreak(String texto, int width, Font f)
{
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int count = 0;

	while ( posIni < texto.length() )
	{
		size = 0;

		pos = posIni;
		posOld = posIni;

		while ( size < width )
		{
			if ( pos == texto.length()) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat == 0x20 ) { posOld = pos - 1; }

//#ifdef J2ME
			size += f.charWidth((char)dat);
//#elifdef DOJA
//#endif
	    }

	    if (posOld == posIni)
		{ 
			if (posIni == pos-1)
			{
				posOld = pos;
			} else {
				posOld = pos - 1;
			}
		}

		if (posIni < posOld)
		{
			textBreakPos[count++] = (short) posIni;
			textBreakPos[count++] = (short) posOld;
		}

		posIni = posOld;
		while ( posIni < texto.length() && (texto.charAt(posIni) == 0x20)) {posIni++;}
	}

	String str[] = new String[count/2];

	for (int i=0 ; i<count ; )
	{
		str[i/2] = texto.substring(textBreakPos[i++], textBreakPos[i++]);
	}

	return str;
}


// -------------------
// text Break - IN: String ; OUT: String[] - (CUSTOM FONT)
// ===================

public String[] textBreak(String texto, int width, byte[] f, int letraMin, int letraSpc)
{
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int count = 0;

	while ( posIni < texto.length() )
	{
		size = 0;

		pos = posIni;
		posOld = posIni;

		while ( size < width )
		{
			if ( pos == texto.length() ) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat == 0x20 ) { dat=0x41; posOld = pos - 1; }

			try {
				size += ( f[ ((dat - letraMin) *6) +2 ] + letraSpc );
			} catch(Exception e) {size+=4;}

	    }

	    if (posOld == posIni)
		{ 
			posOld = pos - 1;
		}

		if (posIni < posOld)
		{
		textBreakPos[count++] = (short) posIni;
		textBreakPos[count++] = (short) posOld;
		}

		posIni = posOld;
		while ( posIni < texto.length() && texto.charAt(posIni) == 0x20) {posIni++;}
	}

	String str[] = new String[count/2];

	for (int i=0 ; i<count ; )
	{
		str[i/2] = texto.substring(textBreakPos[i++], textBreakPos[i++]);
	}

	return str;
}

// <=- <=- <=- <=- <=-













// *******************
// -------------------
// Canvas - Engine
// ===================
// *******************

int canvasWidth = getWidth();
int canvasHeight = getHeight();

int canvasFillRGB;
boolean canvasFillShow = false;
Image canvasImg;

boolean canvasShow = true;


// -------------------
// canvas Tick
// ===================

public void canvasDraw()
{
	if (canvasFillShow) { canvasFillShow=false; canvasFillDraw(canvasFillRGB); }

	if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }


	Draw();		// Imprimimos el juego


	menuListDraw(scr);

//	popupDraw();

	listenerDraw();
}



// -------------------
// canvas Fill Task
// ===================

public void canvasFillTask(int RGB)
{
	canvasFillRGB = RGB;
	canvasFillShow = true;
}

// -------------------
// canvas Fill Draw
// ===================

public void canvasFillDraw(int RGB)
{
//#ifdef J2ME
	scr.setClip (0,0, canvasWidth,canvasHeight);
	putColor(RGB);
//#elifdef DOJA
//#endif
	scr.fillRect(0,0, canvasWidth,canvasHeight);
}

// <=- <=- <=- <=- <=-













// *******************
// -------------------
// ant Sound - Engine Rev.10 (25.2.2005)
// ===================
// *******************

/* ===================================================================

	soundCreate()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	soundPlay(n� Sonido , Repetir)
	-----------------------------
		Hacemos que suene un sonido (0 a x) y que se repita x veces.
		Repetir == 0: Repeticion infinita

	soundStop()
	----------
		Paramos el ultimo sonido.

	soundTick()
	----------
		Debemos ejecutar este metodo en CADA ciclo para gestionar 'tiempos'

	vibraInit(microsegundos)
	-----------------------
		Hacemos vibrar el mobil x microsegundos

=================================================================== */

//#ifdef J2ME

	//#ifdef PLAYER_NONE
	//#elifdef PLAYER_OTA
	//#elifdef PLAYER_MIDP20

		// *******************
		// -------------------
		// Sound - Engine - Rev.0 (28.11.2003)
		// -------------------
		// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
		// ===================
		// *******************

		Player[] soundTracks;

		int soundOld = -1;
		int soundLoop;
		int soundForceRestart = -1;
		int soundVolumenAct;
		boolean soundBajaVolumen = false;


		// -------------------
		// Sound INI
		// ===================
		
		public void soundCreate(String[] sons)
		{	
			soundTracks = new Player[sons.length];

			for (int i=0 ; i<sons.length ; i++)
			{
				soundTracks[i] = SoundCargar(sons[i]);
			}
		}

		public Player SoundCargar(String Nombre)
		{
			Player p = null;
		
			try
			{
				p = Manager.createPlayer( Nombre , "audio/midi");
				p.realize();
				p.prefetch();

				soundVolumenAct = 100;
				VolumeControl vc = (VolumeControl) p.getControl("VolumeControl"); if (vc != null) {vc.setLevel(soundVolumenAct);}
			}
			catch(Exception e) {e.printStackTrace();}
		
			return p;
		}
		
		// -------------------
		// Sound SET
		// ===================
		
		public void soundPlay(int Ary, int Loop)
		{
			soundLoop = Loop;

			soundStop();

			if (gameSound)
			{
				if (Loop<1) {Loop=-1;}

				try
				{
//					soundBajaVolumen = false;
//					soundTracks[Ary].setLoopCount(Loop);
//					VolumeControl vc = (VolumeControl) soundTracks[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(soundVolumenAct);}
					soundTracks[Ary].setLoopCount(Loop);
					soundTracks[Ary].start();
				}
				catch(Exception e)
				{
				//#ifdef Debug
					Debug.println(e.toString());
				//#endif

					soundForceRestart = Ary;
					return;
				}

				soundOld = Ary;
			}
		}
		
		// -------------------
		// Sound RES
		// ===================
		
		public void soundStop()
		{
			soundForceRestart = -1;

			if (soundOld != -1)
			{
				try
				{
					soundTracks[soundOld].stop();
				}
				catch (Exception e)
				{
				//#ifdef Debug
					Debug.println(e.toString());
				//#endif
				}
		
				soundOld = -1;
			}
		}
		
		// -------------------
		// Sound RUN
		// ===================
		
		public void soundTick()
		{
/*
			if (soundOld >= 0 && soundBajaVolumen)
			{
				if ( (soundVolumenAct-=10) < 0)
				{
					soundStop();
				} else {
					VolumeControl vc = (VolumeControl) soundTracks[soundOld].getControl("VolumeControl"); if (vc != null) {vc.setLevel(soundVolumenAct);}
				}
			}
*/

/*
			if (soundForceRestart >= 0)
			{
				try
				{
					soundTracks[soundForceRestart].start();
				}
				catch(Exception e)
				{
				//#ifdef Debug
					Debug.println(e.toString());
				//#endif
					return;
				}
			}
*/
			soundForceRestart = -1;
		}

		// -------------------
		// sound Volumen
		// ===================
/*
		public void soundVolumen()
		{
			soundBajaVolumen = true;
		}
*/		
		// -------------------
		// Vibra SET
		// ===================
		
		public void vibraInit(int Time)
		{
		//#ifdef VIBRATION
			if (gameVibra)
			{
				try
				{
					Display.getDisplay(ga).vibrate(Time);
				}
				catch (Exception e) {}
			}
		//#endif
		}

		// <=- <=- <=- <=- <=-


	//#elifdef PLAYER_MIDP20_CACHED
	//#elifdef PLAYER_MIDP20_FORCED
	//#elifdef PLAYER_MIDP20_SIMPLE
	//#elifdef PLAYER_MIDP20_LOAD
	//#elifdef PLAYER_MIDP20_LOAD_CACHED
	//#elifdef PLAYER_MIDP20_TICK
	//#elifdef PLAYER_MOTC450
	//#elifdef PLAYER_SIEMENS_MIDI
	//#elifdef PLAYER_TSM6
	//#elifdef PLAYER_SAMSUNG
	//#elifdef PLAYER_MO-E1000
	//#elifdef PLAYER_SHARP
	//#endif

//#elifdef DOJA
//#endif

// <=- <=- <=- <=- <=- Ant Sound Engine















// **************************
// --------------------------
// Listener - Engine v1.0 - Rev.0 (9.7.2003)
// ==========================
// **************************

static final int SOFTKEY_NONE = 0;
static final int SOFTKEY_MENU = 1;
static final int SOFTKEY_ACEPT = 2;
static final int SOFTKEY_BACK = 3;
static final int SOFTKEY_CONTINUE = 4;
static final int SOFTKEY_CANCEL = 5;

int listenerIdLeft;
int listenerIdRight;

//#ifdef J2ME
//#ifdef FULLSCREEN

int listenerHeight = 8;

Image listenerImg;
byte[] listenerCor;

public void listenerInit(int idLeft, int idRight)
{
	listenerIdLeft = idLeft;
	listenerIdRight = idRight;
}

public void listenerInit(String strLeft, String strRight) {}

public void listenerDraw()
{
	if (listenerImg != null)
	{
		int listenerY = canvasHeight;

		putColor(0);
		scr.setClip(0, listenerY, canvasWidth, listenerHeight);
		scr.fillRect(0, listenerY, canvasWidth, listenerHeight);

		listenerLabelDraw(listenerImg,  1, listenerY+1,  listenerIdLeft, listenerCor);
		listenerLabelDraw(listenerImg,  canvasWidth - listenerCor[(listenerIdRight*6)+2]-1, listenerY+1,  listenerIdRight, listenerCor);
	}
}

public void listenerLabelDraw(Image img,  int x, int y,  int frame, byte[] cor)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame  ] + 128;

//#ifdef SI-x55
//#endif

	scr.setClip(destX, destY, sizeX, sizeY);
	scr.drawImage(img, destX-sourX, destY-sourY, Graphics.TOP|Graphics.LEFT);
}

//#else
//#endif

//#elifdef DOJA
//#endif

// <=- <=- <=- <=- <=-












// *******************
// -------------------
// conversion - Engine Rev.0 (7.2.2005) Rutinas de conversion de numeros y formatos
// ===================
// *******************

// -------------------
// conv Ary 2 Int (Convertimos 4 bytes de un array a un entero)
// ===================

public int convAry2Int(byte[] bufer, int index)
{
	return ((bufer[index++]&0xFF)<<24) | ((bufer[index++]&0xFF)<<16) | ((bufer[index++]&0xFF)<<8) | (bufer[index]&0xFF);
}

// -------------------
// conv Int 2 Ary (Escribimos un entero dentro de un byte[])
// ===================

public void convInt2Ary(byte[] bufer, int index, int dato)
{
	bufer[index++] = (byte)((dato >> 24) & 0xFF);
	bufer[index++] = (byte)((dato >> 16) & 0xFF);
	bufer[index++] = (byte)((dato >>  8) & 0xFF);
	bufer[index  ] = (byte)((dato      ) & 0xFF);
}

// ----------------
// conv Byte[] 2 Int - Convertimos un array de byte[] en array de int[]. (byte[] DEBE ser multiple de 4)
// ================

public int[] convByte2Int(byte[] in)
{
	int[] out = new int[in.length / 4];
	
	for (int pos=0, i=0 ; i<out.length ; i++)
	{
		out[i] = ((in[pos++]&0xFF)<<24) | ((in[pos++]&0xFF)<<16) | ((in[pos++]&0xFF)<<8) | (in[pos++]&0xFF);
	}

	return out;
}

// <=- <=- <=- <=- <=-









// ****************
// ----------------
// ihd - Engine - ihd (iMode Hard Disk) File System v1.0 - Rev.3 (9.5.2005)
// ----------------
// Dise�ado y Programado por Juan Antonio Gomez Galvez.
// Para uso exclusivo del autor y autorizados por el mismo.
// ================
// ****************

//#ifdef DOJA
//#endif

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// ProgBar - Engine - Rev.2 (20.1.2004)
// ===================
// *******************

int ProgBarX;
int ProgBarY;
int ProgBarSizeX;
int ProgBarSizeY;
int ProgBarPos;
int ProgBarTotal;
boolean ProgBar_ON = false;

// -------------------
// ProgBar INI
// ===================

public void ProgBarINI(int SizeX, int SizeY, int DestX, int DestY)
{
	ProgBarX = DestX;
	ProgBarY = DestY;
	ProgBarSizeX = SizeX;
	ProgBarSizeY = SizeY;
}

// -------------------
// ProgBar END
// ===================

public void ProgBarEND()
{
	ProgBarSET(1,1);
	ProgBar_ON = false;
}

// -------------------
// ProgBar SET
// ===================

public void ProgBarSET(int Pos)
{
	ProgBarSET(Pos, ProgBarTotal);
}

public void ProgBarSET(int Pos, int Total)
{
	ProgBarPos = Pos;
	ProgBarTotal = Total;

	ProgBar_ON = true;

	gameDraw();
}

// -------------------
// ProgBar INS
// ===================

public void ProgBarINS(int suma)
{
	ProgBarTotal += suma;
}

// -------------------
// ProgBar ADD
// ===================

public void ProgBarADD()
{
	ProgBarSET(++ProgBarPos);
}

// -------------------
// ProgBar IMP
// ===================

public void ProgBarIMP()
{
	if (ProgBar_ON)
	{
	putColor(0x188CCE);	// Borde
	scr.fillRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+6, ProgBarSizeY+6);
	putColor(0xDEE7F7); // Papel
	scr.drawRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+5, ProgBarSizeY+5);
	putColor(0x00005A);	// Lapiz
	scr.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);
	}
}

// <=- <=- <=- <=- <=-
















// *******************
// -------------------
// rms - Engine v1.2 - Rev.6 (31.3.2005)
// ===================
// *******************

//#ifdef RMS_ENGINE
//#else

public byte[] rmsFileInfo;
public void rmsCreate(int store) {}
public void rmsDestroy() {}
public void rmsFormat(int store) {}
public boolean rmsSaveFile(String fileName, byte[] in) {return true;}
public byte[] rmsLoadFile(String fileName) {return null;}
public void rmsDeleteFile(String fileName) {}
public String[] rmsGetDir() {return null;}

//#endif

// <=- <=- <=- <=- <=-



// **************************************************************************//
// Final Clase Bios
// **************************************************************************//







// *******************
// -------------------
// menu - Engine
// ===================
// *******************

// -----------------------------------------------
// Variables staticas para identificar los estados de los menus...
// ===============================================
static final int MENU_MAIN = 0;
static final int MENU_SECOND = 1;
static final int MENU_SCROLL_HELP = 2;
static final int MENU_SCROLL_ABOUT = 3;

static final int IN_GAME_HELP = 4;

static final int MENU_MAIN_PLAY_SELECT = 5;
static final int MENU_MAIN_IDIOMA = 6;
static final int MENU_FINAL = 7;
static final int MENU_ASK = 8;
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar las acciones de los menus...
// ===============================================
static final int ACTION_NONE = -10;
static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_EXIT_GAME_OK = 5;
static final int MENU_ACTION_RESTART = 6;
static final int MENU_ACTION_SOUND_CHG = 7;
static final int MENU_ACTION_VIBRA_CHG = 8;
static final int MENU_ACTION_REPLAY = 9;
static final int MENU_ACTION_REPLAY_OK = 10;

static final int MENU_ACTION_ASK = 11;

static final int MENU_ACTION_IDIOMA = 12;
static final int MENU_ACTION_IDIOMA_CATALA = 13;
static final int MENU_ACTION_IDIOMA_CASTELLANO = 14;
// ===============================================

int menuType;
int menuTypeBack;

int menuLastPos;

int menuActionAsk;

boolean menuExit;

Image menuImg;
Image relojImg;

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuInit(type, 0);
}

public void menuInit(int type, int pos)
{
	menuTypeBack = menuType;
	menuType = type;

	menuLastPos = menuListPos;

	menuExit = false;

	menuListInit(3, 3, canvasWidth-6, canvasHeight-6);


	switch (type)
	{
	case MENU_MAIN:

		menuListClear();
 		menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY);
	//#ifndef PLAYER_NONE
		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif

		menuListAdd(0, gameText[TEXT_CHANGEIDIOMA], MENU_ACTION_IDIOMA);
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);

		if (gameLevelLast > 1)
		{
	 		menuListAdd(0, gameText[TEXT_NEWGAME], MENU_ACTION_REPLAY);
		}

		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		menuListSet_Option(pos);

		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_ASK:

		menuListClear();
		menuListAdd(0, gameText[TEXT_ARE_YOU_SURE][0], MENU_ACTION_ASK);
		menuListSet_Option();

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
	break;



	case MENU_MAIN_IDIOMA:

		menuListClear();
 		menuListAdd(0, gameText[TEXT_IDIOMAS][0], MENU_ACTION_IDIOMA_CATALA);
 		menuListAdd(0, gameText[TEXT_IDIOMAS][1], MENU_ACTION_IDIOMA_CASTELLANO);
		menuListSet_Option(gameIdioma);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;



	case MENU_SECOND:

		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
	//#ifndef PLAYER_NONE
		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option(pos);

		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_SCROLL_HELP:

		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Screen();

		listenerInit(SOFTKEY_MENU, SOFTKEY_CONTINUE);
	break;


	case MENU_SCROLL_ABOUT:

	//#ifdef J2ME
		String version = ga.getAppProperty("MIDlet-Version");
	//#elifdef DOJA
	//#endif

		if (version != null) {gameText[TEXT_ABOUT_SCROLL][1] = version;}

		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Screen();

		listenerInit(SOFTKEY_MENU, SOFTKEY_CONTINUE);
	break;

	case IN_GAME_HELP:

		menuListClear();
		menuListAdd(0, gameText[ingameHelpText]);
		menuListSet_Screen();

		if (ingameHelpText == TEXT_LEVEL_REVIEW)
		{
			listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
		} else {
			listenerInit(SOFTKEY_CANCEL, SOFTKEY_CONTINUE);
		}
	break;

	case MENU_FINAL:

		menuListClear();
		menuListAdd(0, gameText[TEXT_END_GAME]);
		menuListSet_Screen();

		menuTypeBack = MENU_MAIN;

		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
	break;
	}

	biosStatusOld = biosStatus;
	biosStatus = BIOS_MENU;
}


// -------------------
// menu Release
// ===================

public void menuRelease()
{
	menuListSet_None();
	listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	gameDraw();
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{
	switch (cmd)
	{

	case ACTION_NONE:	// Ir "Atras"

		switch (menuType)
		{
		case IN_GAME_HELP:
			biosStatus = biosStatusOld;
			listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		break;


		case MENU_SCROLL_HELP:
		case MENU_SCROLL_ABOUT:
		case MENU_MAIN_IDIOMA:
		case MENU_ASK:

			menuRelease();
	
			menuInit(menuTypeBack, menuLastPos);
		break;
		}
	break;


	case -3: // Scroll o Screen ha sido cortado por usuario
	case -2: // Scroll o Screen ha llegado al final

		menuRelease();

		if (menuType == IN_GAME_HELP)
		{
			if (ingameHelpText == TEXT_LEVEL_REVIEW)
			{
				listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
			} else {
				listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
			}

			biosStatus = biosStatusOld;
		} else {
			menuInit(menuTypeBack, menuLastPos);
		}
	break;


	case MENU_ACTION_ASK:

		menuAction(menuActionAsk);
	break;


	case MENU_ACTION_IDIOMA:

		menuRelease();

		menuInit(MENU_MAIN_IDIOMA);
	break;

	case MENU_ACTION_IDIOMA_CATALA:

		menuRelease();

		gameText = textosCreate(loadFile("/Textos2.txt"));

		listenerImg = loadImage("/softkeys2");
		listenerCor = loadFile("/softkeys2.cor");

		gameIdioma = 0;
		savePrefs();

		menuInit(MENU_MAIN, menuLastPos);
	break;

	case MENU_ACTION_IDIOMA_CASTELLANO:

		menuRelease();

		gameText = textosCreate(loadFile("/Textos.txt"));

		listenerImg = loadImage("/softkeys");
		listenerCor = loadFile("/softkeys.cor");

		gameIdioma = 1;
		savePrefs();

		menuInit(MENU_MAIN, menuLastPos);
	break;



	case MENU_ACTION_REPLAY:	// Jugar de 0

		menuRelease();

		menuActionAsk = MENU_ACTION_REPLAY_OK;
		menuInit(MENU_ASK);
	break;


	case MENU_ACTION_REPLAY_OK:	// Jugar de 0

		menuRelease();

		gameLevelLast = 1;
		gameScore = 0;
		savePrefs();

		menuExit = true;
	break;


	case MENU_ACTION_PLAY:		// Jugar deste ultima partida

		menuRelease();

		menuExit = true;
	break;

	case MENU_ACTION_CONTINUE:	// Continuar

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_HELP:		// Mostramos Instrucciones...

		menuRelease();

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// Mostramos About...

		menuRelease();

		menuInit(MENU_SCROLL_ABOUT);
	break;


	case MENU_ACTION_RESTART:	// Provocamos GAME OVER desde menu

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		playExit = 3;		// flag para SALIDA de juego tipo GAME OVER
		menuExit = true;
	break;




	case MENU_ACTION_EXIT_GAME:	// Exit Game???

		menuRelease();

		menuActionAsk = MENU_ACTION_EXIT_GAME_OK;
		menuInit(MENU_ASK);
	break;	

	case MENU_ACTION_EXIT_GAME_OK:	// Exit Game

		menuRelease();

		gameStatus = GAME_EXIT;
		menuExit = true;
	break;	



	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListOpt() != 0;
		if (!gameSound) {soundStop();} else if (menuType == MENU_MAIN) {soundPlay(0,0);}
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListOpt() != 0;
		if (gameVibra) {vibraInit(300);}
	break;
	}

}

// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if ( menuListTick( (keyY!=lastKeyY? keyY:(keyX!=lastKeyX? keyX:0) ), keyMenu!=lastKeyMenu? keyMenu:0 ) )
	{
		menuAction(menuListCMD);
	}

	if (menuExit) {return true;}

	return false;
}

// <=- <=- <=- <=- <=-












// *******************
// -------------------
// popup - Engine
// ===================
// *******************
// -------------------
/*
static final int POPUP_ASK = 0;
static final int POPUP_OK = 1;
static final int POPUP_DOWNLOAD = 2;

String[] popupStr;

int popupX;
int popupY;
int popupWidth;
int popupHeight;

int popupActionNo;
int popupActionOk;

int popupSoftkeyLeftBack;
int popupSoftkeyRightBack;

Font popupFont;
int popupFontHeight;

static final int popupBorder = 4;

boolean popupShow = false;

boolean popupAcept;

int popupType;

// -------------------
// popup Init
// ===================

public void popupInit(String str, int type)
{
	popupInit(str, type, POPUP_ACTION_NONE, POPUP_ACTION_OK, SOFTKEY_NONE, SOFTKEY_ACEPT);
}

public void popupInit(String str, int type, int actionNo, int actionOk, int softkeyLeft, int softkeyRight)
{
	popupType = type;

	popupSoftkeyLeftBack = listenerIdLeft;
	popupSoftkeyRightBack = listenerIdRight;
	listenerInit(softkeyLeft, softkeyRight);

//#ifdef J2ME
	popupFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_SMALL );
//#elifdef DOJA
	popupFont = Font.getFont( Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL );
//#endif
	popupFontHeight = popupFont.getHeight();



	popupActionNo = actionNo;
	popupActionOk = actionOk;

	popupWidth = canvasWidth - 24;
	popupHeight = canvasHeight - 24;
	popupX = (canvasWidth - popupWidth) / 2;
	popupY = (canvasHeight - popupHeight) / 2;

	popupStr = textBreak(str, popupWidth - (popupBorder*2), popupFont);




	popupShow = true;

	biosStatusOld = biosStatus;
	biosStatus = BIOS_POPUP;
}

// -------------------
// popup Release
// ===================

public void popupRelease()
{
	listenerInit(popupSoftkeyLeftBack, popupSoftkeyRightBack);
	popupStr = null;
	popupShow = false;
}

// -------------------
// popup Tick
// ===================

public boolean popupTick()
{
	if (popupType == POPUP_DOWNLOAD && net == null)
	{
		popupRelease();
//		popupAction(popupActionOk);
//		gameForceRefresh = true;
		return true;
	}

	if (lastKeyMenu == 0)
	{
		if (keyMenu == -1 && popupActionNo != POPUP_ACTION_NONE)
		{
			popupRelease();
			popupAction(popupActionNo);
			gameForceRefresh = true;
			return true;
		}
		else if (keyMenu > 0 && popupActionOk != POPUP_ACTION_NONE)
		{
			popupRelease();
			popupAction(popupActionOk);
			gameForceRefresh = true;
			return true;
		}
	}

//	popupShow = true;

	return false;
}

// -------------------
// popup Draw
// ===================

public void popupDraw()
{
	if (!popupShow) {return;}
	popupShow = false;

	fillDraw(GREEN, popupX, popupY, popupWidth, popupHeight);

	putColor(GREEN_BORDER);
	scr.drawRect(popupX, popupY, popupWidth-1, popupHeight-1);


//	putColor(BLACK);
//	formTextDraw(popupStr[0], 0, 0, 0,   popupX, popupY, popupWidth, popupHeight);

	canvasTextCreate(popupX, popupY, popupWidth, popupHeight);
	textDraw(popupStr, 0,0, BLACK, TEXT_VCENTER|TEXT_HCENTER);
}

// -------------------
// popup Action
// ===================

static final int POPUP_ACTION_NONE = 0;
static final int POPUP_ACTION_NO = 1;
static final int POPUP_ACTION_OK = 2;
static final int POPUP_ACTION_GAME_EXIT = 3;

public void popupAction(int action)
{
	switch (action)
	{
	case POPUP_ACTION_NO:
		popupAcept = false;
	break;

	case POPUP_ACTION_OK:
		popupAcept = true;
	break;

	case POPUP_ACTION_GAME_EXIT:

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		gameExit = true;
	break;
	}
}
*/
// <=- <=- <=- <=- <=-



// ************************************************************************** //
// Final Clase Bios
// ************************************************************************** //












// ************************************************************************** //
// Inicio Clase Game
// ************************************************************************** //


// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// ===========================================


// *******************
// -------------------
// game - Engine
// ===================
// *******************

// -----------------------------------------------
// Variables staticas para identificar los textos, libres desde el 16...
// ===============================================
final static int TEXT_PLAY = 0;
final static int TEXT_CONTINUE = 1;
final static int TEXT_SOUND = 2;
final static int TEXT_VIBRA = 3;
final static int TEXT_MODE = 4;
final static int TEXT_GAMEOVER = 5;
final static int TEXT_HELP = 6;
final static int TEXT_ABOUT = 7;
final static int TEXT_RESTART = 8;
final static int TEXT_EXIT = 9;
final static int TEXT_LOADING = 10;
final static int TEXT_HELP_SCROLL = 11;
final static int TEXT_ABOUT_SCROLL = 12;
final static int TEXT_LEVEL = 13;
final static int TEXT_CONGRATULATIONS = 14;
final static int TEXT_SOFTKEYS = 15;
final static int TEXT_ARE_YOU_SURE = 16;

final static int TEXT_ROOM = 17;
final static int TEXT_ROOM_COMPLET = 18;

final static int TEXT_NEWGAME = 19;

final static int TEXT_CHANGEIDIOMA = 20;
final static int TEXT_IDIOMAS = 21;

final static int TEXT_INGAME_HELP_INICIO_NIVEL = 22;
final static int TEXT_INGAME_HELP_SOBRE_PUERTA = 23;
final static int TEXT_INGAME_HELP_SOBRE_CAIXA = 24;
final static int TEXT_INGAME_HELP_SOBRE_RELOJ = 25;
final static int TEXT_INGAME_HELP_SOBRE_3XL = 26;
final static int TEXT_INGAME_HELP_SOBRE_CACAO = 27;
final static int TEXT_INGAME_HELP_TODAS_LLAVES = 28;
final static int TEXT_END_GAME = 29;
final static int TEXT_LEVEL_REVIEW = 30;

// ===============================================

// -----------------------------------------------
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int GAME_START = 0;
final static int GAME_LOGOS = 5;

final static int GAME_MENU_START = 10;
final static int GAME_MENU_MAIN = 11;
final static int GAME_MENU_MAIN_END = 12;

final static int GAME_MENU_SECOND = 25;

final static int GAME_MENU_GAMEOVER = 30;

final static int GAME_EXIT = 35;

final static int GAME_PLAY = 20;
final static int GAME_PLAY_INIT = 21;
final static int GAME_PLAY_TICK = 22;
// ===============================================

int gameAlphaExit;

boolean gameShowEnd = false;

int gameIdioma;
int gameScore;
int gameHiScore;

int gameStatus = 0;

// -------------------
// game Create
// ===================

public void gameCreate()
{

// --------------------------------
// Cargamos Preferencias
// ================================

	loadPrefs();

// ================================


// --------------------------------
// Cargamos e inicializamos TODOS los sonidos del juego
// ================================
//#ifndef PLAYER_NONE

	//#ifdef PLAYER_OTA
	//#elifdef PLAYER_SAMSUNG
	//#elifdef PLAYER_SHARP
	//#else

		soundCreate( new String[] {
			"/nopasseu_caratula.mid",			// 0 - Musica de la caratula
			"/nopasseu_inici_nivell.mid",		// 1 - Inicio del nivel
			"/nopasseu_nivell_acomplert.mid",	// 2 - Nivel completado
			"/nopasseu_ms_frame_morta.mid",		// 3 - Muerte Ms Frame
			"/nopasseu_item.mid",				// 4 - Pillamos item
			"/nopasseu_item_3xl.mid",			// 5 - Pillamos item de 3xl.net
			"/nopasseu_llancar_caixa.mid",		// 6 - lanzamos la caja
			"/nopasseu_ms_frame_tocada.mid",	// 7 - ms frame recibe "un toke"
			"/nopasseu_enemic_mort.mid",		// 8 - enemigo muere
			"/nopasseu_salt.mid",				// 9 - Ms Frame salta
			"/nopasseu_salt_trampoli.mid",		//10 - Ms Frame salta con trampolin
			});

	//#endif


	//#ifdef SG-D410
	//#endif

//#endif
// ================================



//#ifdef J2ME
	//#ifdef FULLSCREEN
		if (gameIdioma == 0)
		{
			gameText = textosCreate( loadFile("/Textos2.txt") );
			listenerImg = loadImage("/softkeys2");
			listenerCor = loadFile("/softkeys2.cor");
		} else {
			gameText = textosCreate( loadFile("/Textos.txt") );
			listenerImg = loadImage("/softkeys");
			listenerCor = loadFile("/softkeys.cor");
		}
	//#endif
//#endif



}

// -------------------
// game Destroy
// ===================

public void gameDestroy()
{
}

// -------------------
// game Tick
// ===================

public void gameTick()
{
	switch (gameStatus)
	{
// -------------------------------
// STATUS de arranque, primer TICK del game Engine
// ===============================
	case GAME_START:

		gameStatus = GAME_LOGOS;
	break;
// ===============================



// -------------------------------
// Lanzamos secuancia inicial de logotipos
// ===============================
	case GAME_LOGOS:

		logosInit(new String[] {"/microjocs"}, new int[] {0xffffff}, 3000);
		gameStatus = GAME_MENU_START;
	break;
// ===============================



// -------------------------------
// Gestor de menus del source base
// ===============================
	case GAME_MENU_START:

		menuImg = loadImage("/caratula");

		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:

		menuInit(gameShowEnd? MENU_FINAL:MENU_MAIN);
		gameShowEnd = false;

		soundPlay(0,0);
		gameStatus = GAME_MENU_MAIN_END;
	break;

	case GAME_MENU_MAIN_END:

		soundStop();
		menuImg = null;

		gameStatus = GAME_PLAY;
	break;




	case GAME_MENU_SECOND:

		menuInit(MENU_SECOND);
		gameStatus = GAME_PLAY_TICK;
	break;

	case GAME_MENU_GAMEOVER:


		canvasFillTask(0x000000);
		canvasTextTask(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, TEXT_VCENTER|TEXT_HCENTER | TEXT_OUTLINE);
		gameDraw();

		waitTime(3000);

		gameStatus = GAME_MENU_START;
	break;


	case GAME_EXIT:

		soundStop();

		gameAlphaExit = 4;
		gameStatus++;

	case GAME_EXIT+1:

		if (gameAlphaExit != 0)
		{
			gameAlphaExit--;
		} else {
			gameExit = true;
		}
	break;
// ===============================





// -------------------------------
// Gestor del motor del juego
// ===============================
	case GAME_PLAY:

		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, TEXT_VCENTER|TEXT_HCENTER);
		gameDraw();

		playCreate();
		gameStatus++;

	case GAME_PLAY_INIT:

		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, TEXT_VCENTER|TEXT_HCENTER);
		gameDraw();

		playInit();
		playExit=0;

		soundPlay(1,1);

		faseStr = gameText[TEXT_ROOM][0]+" "+faseLevel;
		playShow = true;
		gameDraw();

		waitTime(3000);

		faseStr = null;

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);

		gameStatus++;
	break;

	case GAME_PLAY_TICK:

	//#ifdef CHEATS
	//#endif

		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}

		if ( !playTick() ) {break;}

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		playRelease();

		gameStatus--;

		switch (playExit)
		{
		case 1:	// Nivel Completado, pasamos al siguiente nivel

			if (++faseLevel == 11)
			{
				gameShowEnd = true;

				faseLevel = 1;

				playDestroy();

				gameStatus = GAME_MENU_START;
			}

			gameLevelLast = faseLevel;
			savePrefs();
		break;

		case 2:	// Una vida menos y regresamos al inicio del nivel
		break;

		case 3:	// Producir Game Over

			playDestroy();

			gameStatus = GAME_MENU_GAMEOVER;
		break;
		}

		playExit=0;
	break;
// ===============================
	}
}

// -------------------
// game Refresh :: Este metodo se llama tras regresar de un 'incomming call' y debemos restaurar el juego.
// ===================

public void gameRefresh()
{
	switch (gameStatus)
	{
	case GAME_PLAY_TICK:		// GAME_PLAY_TICK => Estado del juego cuando esta en funcionamiento.
	break;
	}
}

// -------------------
// Draw : Llamamos a este metodo para renderizar en pantalla, el objeto 'scr' es nuestro GRAPHICS.
// ===================

public void Draw()
{

// --------------------------------
// Renderizamos PLAY Engine
// ================================

	if (biosStatus == BIOS_MENU && gameStatus == GAME_PLAY_TICK && menuListShow) {playShow = true;}

	if (playShow) { playShow=false; playDraw(); }

	if (gameStatus == GAME_EXIT+1) {alphaFillDraw(0x44000000, 0,0, canvasWidth, canvasHeight);}

// ================================

}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// play - Engine
// ===================
// *******************
// -------------------

int playExit;
boolean playShow;

Scroll sc;

// -------------------
// play Create
// ===================

public void playCreate()
{
	sc = new Scroll();

	panelCreate();

	sc.ScrollINI(canvasWidth, scrollHeight);	// pipo
//	sc.ScrollINI(canvasWidth, canvasHeight);

	faseCreate();
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	faseDestroy();

	sc.ScrollEND();
	sc = null;
}

// -------------------
// play Init
// ===================

public void playInit()
{

	AniSprINI();
	llibreINI();
	caixaINI();
	cotxeINI();
	calcetinINI();
	globoINI();


	faseInit();

}

// -------------------
// play Release
// ===================

public void playRelease()
{
	faseRelease();

	caixaEND();
	llibreEND();
	AniSprEND();
	cotxeEND();
	calcetinEND();
	globoEND();

	faseMap = null;
	System.gc();
}

// -------------------
// play Tick
// ===================

public boolean playTick()
{
	AniSprRUN();
	llibreRUN();
	caixaRUN();
	cotxeRUN();
	calcetinRUN();
	globoRUN();


	faseTick();

	protTick();


	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}

// -------------------
// play Draw
// ===================

public void playDraw()
{

	if (biosStatus == BIOS_GAME)
	{
		if (protMode != PROT_MUERTE)
		{
			sc.ScrollRUN_Centro_Max(protX+(protWidth/2) + (protSide<0? -canvasWidth/5:canvasWidth/5)  , protY+(protHeight/2));
		} else {
			sc.ScrollRUN(protMortX, protMortY);
		}
	} else {
		sc.ScrollRUN(sc.ScrollX, sc.ScrollY);
	}


	scr.translate(0, scrollY);	// pipo


	sc.ScrollIMP(scr);


	AniSprIMP(llibres, llibreP, llibreC);
	AniSprIMP(cotxes, cotxeP, cotxeC);
	AniSprIMP(calcetins, calcetinP, calcetinC);
	AniSprIMP(caixas, caixaP, caixaC);
	AniSprIMP(AniSprs, AniSprP, AniSprC);
	AniSprIMP(globos, globoP, globoC);



	if (faseStr != null)
	{
		canvasTextCreate(0,0, canvasWidth, scrollHeight);	// pipo
//		canvasTextCreate(0,0, canvasWidth, canvasHeight);
		scr.setClip(0,0, canvasWidth, scrollHeight);	// pipo
//		scr.setClip(0,0, canvasWidth, canvasHeight);
		textDraw(faseStr, 0,0, 0xffffff, TEXT_VCENTER|TEXT_HCENTER|TEXT_OUTLINE);
	}

	scr.translate(0, -scrollY);	// pipo


	panelDraw();

}

// <=- <=- <=- <=- <=-







// *******************
// -------------------
// panel - Engine
// ===================
// *******************

static final int panelItemWidth = 16;
static final int panelItemHeight = 13;
static final int panelNumWidth = 5;
static final int panelNumHeight = 5;

static final int panelSpcWidth = 4;

static final int contadoresMinHeight = 18;

int panelClauX;
int panelClauNumX;

int panelCorX;

int panelRelojX;
int panelRelojNumX;

int panelHeight;

int panelItemY;
int panelNumY;

// -------------------
// panel Create
// ===================

public void panelCreate()
{
	scrollHeight = ((canvasHeight - contadoresMinHeight) / faseTileSize) * faseTileSize;
	scrollY = canvasHeight - scrollHeight;

	panelHeight = scrollY; // pipo
//	panelHeight = contadoresMinHeight;


	int width = (panelItemWidth*5) + (panelNumWidth*6) + (panelSpcWidth*4);

	int sobra = (canvasWidth - width) / 2;

	panelClauX = panelSpcWidth;
	panelClauNumX = panelClauX + panelItemWidth + panelSpcWidth;

	panelCorX = panelClauNumX + (panelNumWidth*3) + sobra;

	panelRelojX = panelCorX + (panelItemWidth*3) + sobra;
	panelRelojNumX = panelRelojX + panelItemWidth;

	panelItemY = (panelHeight - panelItemHeight) / 2;
	panelNumY = (panelHeight - panelNumHeight) / 2;
}

// -------------------
// panel Draw
// ===================

public void panelDraw()
{
	fillDraw(0x000000, 0,0, canvasWidth, panelHeight);	// pipo
//	alphaFillDraw(0x88000000, 0,0, canvasWidth, panelHeight);

	if (faseClaus > 0 || (fasePanelClausCnt & 0x08) == 0)
	{
		spriteDraw(contadoresImg, panelClauX,                   panelItemY,   0, contadoresCor);
	}

	spriteDraw(contadoresImg, panelClauNumX,                    panelNumY,   11, contadoresCor);
	spriteDraw(contadoresImg, panelClauNumX+ panelNumWidth,     panelNumY,   12+(faseClaus/10), contadoresCor);
	spriteDraw(contadoresImg, panelClauNumX+(panelNumWidth*2),  panelNumY,   12+(faseClaus%10), contadoresCor);

	for (int i=0 ; i<protToques ; i++)
	{
		spriteDraw(contadoresImg, panelCorX+(panelItemWidth*i), panelItemY,  1, contadoresCor);
	}

	spriteDraw(contadoresImg, panelRelojX,                      panelItemY,   2, contadoresCor);
	spriteDraw(contadoresImg, panelRelojNumX,                   panelNumY,   12+(faseTemps%1000)/100, contadoresCor);
	spriteDraw(contadoresImg, panelRelojNumX+ panelNumWidth,    panelNumY,   12+(faseTemps%100)/10, contadoresCor);
	spriteDraw(contadoresImg, panelRelojNumX+(panelNumWidth*2), panelNumY,   12+(faseTemps%10), contadoresCor);
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// fase - Engine
// ===================
// *******************

static final int faseTileSize = 16;

static final int tileWidth = 16;
static final int tileHeight = 16;

int scrollX, scrollY;
int faseSpeed = 4;

int faseLevel;
int faseClaus;
int faseTemps, faseTempsCnt;
int faseItems;

int faseWidth;
int faseHeight;

String faseStr;

int scrollHeight;

int faseTrampolin;

int fasePanelClausCnt;

boolean faseRelojStop;

byte[] faseMap;
byte[] faseCol;
byte[] faseCom;
Image tilesAImg;
Image tilesBImg;

byte[] llibreCor;
Image llibreImg;

byte[] particulesCor;
Image particulesImg;

byte[] cotxeCor;
Image cotxeImg;

byte[] calcetinCor;
Image calcetinImg;

byte[] globoCor;
Image globoImg;

byte[] contadoresCor;
Image contadoresImg;

// -------------------
// fase Create
// ===================

public void faseCreate()
{
	llibreCor = loadFile("/llibre.cor");
	llibreImg = loadImage("/llibre");

	particulesCor = loadFile("/particules.cor");
	particulesImg = loadImage("/particules");

	cotxeCor = loadFile("/cotxe.cor");
	cotxeImg = loadImage("/cotxe");

	calcetinCor = loadFile("/calcetin.cor");
	calcetinImg = loadImage("/calcetin");

	globoCor = loadFile("/globo.cor");
	globoImg = loadImage("/globo");

	contadoresCor = loadFile("/contadores.cor");
	contadoresImg = loadImage("/contadores");

	faseLevel = gameLevelLast;
//	faseLevel = 7;

	ingameHelpCreate();

	protCreate();
}

// -------------------
// fase Destroy
// ===================

public void faseDestroy()
{
	System.gc();

	llibreCor = null;
	llibreImg = null;

	particulesCor = null;
	particulesImg = null;

	cotxeCor = null;
	cotxeImg = null;

	globoCor = null;
	globoImg = null;

	calcetinCor = null;
	calcetinImg = null;

	contadoresCor = null;
	contadoresImg = null;

	protDestroy();

	System.gc();
}

// -------------------
// fase Init
// ===================

public void faseInit()
{
	int bank = (faseLevel-1)%3;
	tilesAImg = loadImage("/tiles"+bank+"A");
	tilesBImg = loadImage("/tiles"+bank+"B");


	faseMap = loadFile("/nivel"+faseLevel+".mmp");
	faseWidth = faseMap[0] & 0xff;
	faseHeight = faseMap[1] & 0xff;
	System.arraycopy(faseMap, 2, faseMap, 0, faseWidth*faseHeight);

	faseCol = loadFile("/nivel"+faseLevel+".mcl");
	System.arraycopy(faseCol, 2, faseCol, 0, faseWidth*faseHeight);

	faseCom = loadFile("/nivel"+faseLevel+".btsc");


	faseClaus = 0;

	faseItems = 0;

	faseTemps = new int[] {500,450,450,400,350,350,300,300,350,150}[faseLevel-1];

	faseTempsCnt = 0;

	faseTrampolin = -1;		// Index del trampilon recien pisado para restaurarlo al tile NO PISADO

	faseRelojStop = false;

// Procesamos los elementos del nivel:
	int faseCnt = 0;
	for (int y=0 ; y<faseHeight ; y++)
	{
		for (int x=0 ; x<faseWidth ; x++, faseCnt++)
		{
			switch (faseCol[faseCnt])
			{
			case 15:	// llibre
				llibreSET(x*faseTileSize, y*faseTileSize);
			break;

			case 17:	// cotxe
				cotxeSET(x*faseTileSize, ((y+1)*faseTileSize)-cotxeHeight, 1);
			break;

			case 19:	// calcetin
				calcetinSET(x*faseTileSize, ((y+1)*faseTileSize)-calcetinHeight, 1);
			break;

			case 16:	// globo
				globoSET(x*faseTileSize, ((y+2)*faseTileSize));
			break;
			}

			switch (faseMap[faseCnt])
			{
			case 80:	// Clau
				faseClaus++;
			break;
			}
		}
	}


	protInit();


	sc.ScrollSET(faseMap, faseCom, faseWidth, faseHeight, tilesAImg, tilesBImg, 10);
}

// -------------------
// fase Release
// ===================

public void faseRelease()
{
	System.gc();

	faseMap = null;
	faseCol = null;
	faseCom = null;

	tilesAImg = null;
	tilesBImg = null;

	sc.ScrollRES();

	System.gc();
}

// -------------------
// fase Tick
// ===================

public void faseTick()
{
	if (--fasePanelClausCnt < 0) {fasePanelClausCnt = 1024;}

	if (!faseRelojStop)
	{
		if (--faseTempsCnt < 0) {faseTempsCnt=8; faseTemps--;}
		if (faseTemps <= 0) {faseTemps=0; protMuerteInit(1);}
	}

	if (faseTrampolin >= 0) {faseMap[faseTrampolin] = 76;}
}

// -------------------
// check Tile
// ===================

int checkTileDir;

public int checkTile(int x, int y)
{
	checkTileDir = ((y/tileHeight) * faseWidth) + (x/tileWidth);

	if (checkTileDir < 0 || checkTileDir >= faseCol.length) {return -1;}


	int tile = (faseMap[checkTileDir] & 0xff) - 72;

	if (tile < 0) {return -1;}


	switch (tile)
	{
	case 0:	// Disco
	case 1:	// Disco
		faseItems++;
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
	//#ifndef SOUND_LOW
		soundPlay(4,1);
	//#endif
	break;

	case 2:	// Estrella
	case 3:	// Estrella
		faseItems++;
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
	//#ifndef SOUND_LOW
		soundPlay(4,1);
	//#endif
	break;

//	case 4:	// Trampolin
//	case 5:	// Trampolin
//	break;

	case 6:	// Corazon de puntos
	case 7:	// Corazon de puntos
		faseItems++;
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
	//#ifndef SOUND_LOW
		soundPlay(4,1);
	//#endif
	break;

	case 8:	// Llave
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
		soundPlay(4,1);

		if (--faseClaus < 0) {faseClaus=0;}
	break;

	case 9:	// Reloj
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
		soundPlay(4,1);

		faseTemps += 100; if (faseTemps > 999) {faseTemps=999;}
	break;

	case 10:	// Colacao
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
		soundPlay(4,1);

		if (++protToques > 3) {protToques = 3;}
	break;

	case 11:	// Anillo
	case 12:	// Anillo
		faseItems++;
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
	//#ifndef SOUND_LOW
		soundPlay(4,1);
	//#endif
	break;

//	case 13:	// Caja
//	break;

	case 14:	// 3xl
		faseMap[checkTileDir] = faseCom[3+(tile*5)];
		AniSprSET(-1, (checkTileDir%faseWidth)*faseTileSize, (checkTileDir/faseWidth)*faseTileSize, 0, 8, 1, 0x200);
		soundPlay(1,1);

		protInmune = 150;
	break;
	}

	return tile;
}


// -------------------
// check Suelo
// ===================

public boolean checkSuelo(int X, int Y, int despY)
{
	if ((Y < 0) || (Y+despY >= faseHeight*faseTileSize) || (X < 0) || (X >= faseWidth*faseTileSize)) {return false;}

	int posA = ((Y/faseTileSize)*faseWidth)+(X/faseTileSize);
	int posB = (((Y+despY)/faseTileSize)*faseWidth)+(X/faseTileSize);

	if (posA == posB) {return true;}

	if (faseCol[posA] != 1 && faseCol[posB] != 1) {return true;}
	if (faseCol[posA] != 1 && faseCol[posB] == 1) {return false;}
	if (faseCol[posA] == 1 && faseCol[posB] != 1) {return true;}

	return false;
}


// -------------------
// check Puerta
// ===================

public boolean checkPuerta(int X, int Y)
{
	if ((Y < 0) || (Y >= faseHeight*faseTileSize) || (X < 0) || (X >= faseWidth*faseTileSize)) {return false;}

	int posA = ((Y/faseTileSize)*faseWidth)+((X-(faseTileSize/2))/faseTileSize);
	int posB = ((Y/faseTileSize)*faseWidth)+((X+(faseTileSize/2))/faseTileSize);

	if (posA != posB && faseCol[posA] == 3 && faseCol[posB] == 3) {return true;}

	return false;
}

// <=- <=- <=- <=- <=-








// *******************
// -------------------
// prot - Engine
// ===================
// *******************

static final int protWidth = 32;
static final int protHeight = 48;
static final int protCuX = 12;
static final int protCuY = 20;
static final int protCuWidth = 8;
static final int protCuHeight = 28;

static final int protCentX = protWidth/2;
static final int protCentY = protHeight/2;


static final int PROT_CORRER = 0;
static final int PROT_SALTAR = 1;
static final int PROT_CAER = 2;
static final int PROT_MUERTE = 3;
static final int PROT_PILLACAJA = 4;
static final int PROT_TIRACAJA = 5;
static final int PROT_COMPLET = 6;

int protX;
int protY;
int protSuperX;
int protSuperY;
int protSide;
int protMode;
int protModeExt;
int protAnim;

int protMortX, protMortY;

int protSpd = 4;

boolean protConCaja = false;

int protCajaPilladaDir;

int protInmune;
int protToques;

byte[] spritesCor;
Image spritesImg;

// -------------------
// prot Create
// ===================

public void protCreate()
{
	spritesCor = loadFile("/msframe.cor");
	spritesImg = loadImage("/msframe");
}

// -------------------
// prot Destroy
// ===================

public void protDestroy()
{
	spritesCor = null;
	spritesImg = null;
}

// -------------------
// prot Init
// ===================

public void protInit()
{
	protAnim = AniSprSET(-1, 0, 0, 0, 1, 1, 0x002);

	protX = faseTileSize - (protWidth/2);
	protY = (faseHeight*faseTileSize)-faseTileSize-protHeight;
	protSide = 1;

	protConCaja = false;

	protToques = 3;
	protInmune = 0;

	protCorrerInit();
}

// -------------------
// prot Release
// ===================

public void protRelease()
{
}

// -------------------
// prot Tick
// ===================

public void protTick()
{

	switch (protMode)
	{
	case PROT_CORRER:	protCorrerTick();	break;
	case PROT_SALTAR:	protSaltarTick();	break;
	case PROT_CAER:		protCaerTick();		break;
	case PROT_MUERTE:	protMuerteTick();	break;
	case PROT_PILLACAJA:protPillaCajaTick();break;
	case PROT_TIRACAJA:	protTiraCajaTick();	break;
	case PROT_COMPLET:	protCompletTick();	break;
	}


// Colisionamos con enemigos???
	if (protInmune==0)
	{
		if (protMode != PROT_MUERTE && protMode != PROT_COMPLET)
		{
			if (llibreColision(protX+protCuX, protY+protCuY, protCuWidth, protCuHeight)
			||	cotxeColision(protX+protCuX, protY+protCuY, protCuWidth, protCuHeight)
			||	globoColision(protX+protCuX, protY+protCuY, protCuWidth, protCuHeight)
			||	calcetinColision(protX+protCuX, protY+protCuY, protCuWidth, protCuHeight))
			{
				protMuerteInit(0);
			}
		}
	} else {
		protInmune--;
	}



	AniSprs[protAnim].CoorX = protX;
	AniSprs[protAnim].CoorY = protY;
	AniSprs[protAnim].FrameBase=(protSide!=1? 36:0);	// Fijamos side (derecha o izquierda)


	AniSprs[protAnim].show = (protInmune & 1) == 0;

}


// -------------------
// prot Correr Init
// ===================

public void protCorrerInit()
{
	protY =((protY+protHeight) & -faseTileSize)-protHeight;

	int base = protConCaja? 18:0;

	AniSprSET(protAnim, protX, protY, base, 8, 2, 0x001);
	AniSprs[protAnim].Pause = 1;

	protMode = PROT_CORRER;
}

// -------------------
// prot Correr Tick
// ===================

public void protCorrerTick()
{


// Chequeamos si tenemos todas las llaves y estamos sobre puerta para terminar la habitacion
	if (faseClaus == 0 && checkPuerta(protX+(protCentX), protY+(protHeight-2)) )
	{
		protCompletInit();
		return;
	}


// Chequeamos si pisamos los items de informacion
	if ( ingameHelpTick() ) {return;}



// Cheqeamos items pisados
	checkTile(protX+(protWidth/2), protY+((protHeight/3)));
	checkTile(protX+(protWidth/2), protY+(protHeight-(protHeight/3)));



// Saltamos???
	if (keyY < 0 && lastKeyY == 0)
	{
		protSaltarInit(0);
		return;
	}


// Controlamos desplazamiento horizontal
	int base = protConCaja? 18:0;
	boolean quieta = false;

	if (keyX != 0)
	{
		protSide = keyX;
		protX += keyX * protSpd;

		if (protX < -protCuX)
		{
			protX = -protCuX;
			quieta = true;
		} else
		if (protX > faseWidth*faseTileSize - (protCuX + protCuWidth))
		{
			protX = faseWidth*faseTileSize - (protCuX + protCuWidth);
			quieta = true;
		}

		AniSprs[protAnim].FrameIni = base+1;
		AniSprs[protAnim].Pause = 1;
	} else {
		quieta = true;
	}

	if (quieta)
	{
		AniSprs[protAnim].FrameIni = base;
		AniSprs[protAnim].FrameAct = 0;
		AniSprs[protAnim].Pause = 0;
	}


// Hay suelo?
	if (checkSuelo(protX+(protCuX)              , protY+(protHeight)-1, 2)
	&&  checkSuelo(protX+(protCuX+protCuWidth-1), protY+(protHeight)-1, 2))
	{
		protCaerInit();
		return;
	}



// Nos dejamos caer???
	if (keyY > 0 && lastKeyY == 0)
	{
		if (protY + protHeight + 4 < (faseHeight * faseTileSize) - faseTileSize)
		{
			protY += 4;
		}
	}


// Pillamos caja???
	if (keyMenu > 0 && lastKeyMenu == 0)
	{
		if (!protConCaja
		&& (checkTile(protX+(protWidth/2)+(protSide*(protWidth/4)), protY+protHeight-4) == 13
		|| checkTile(protX+(protWidth/2), protY+protHeight-4) == 13))
		{
			protCajaPilladaDir = checkTileDir;
			protPillaCajaInit();
			return;
		} else {
		}

// Tiramos caja
		if (protConCaja)
		{
			protTiraCajaInit();
			return;
		}
	}
	
}


// -------------------
// prot Saltar Init
// ===================

int[] protSaltarTabNormal = new int[] {-8,-7,-6,-5,-4,-2,-1,0,0};
int[] protSaltarTabTrampolin = new int[] {-8,-8,-8,-7,-7,-7,-6,-6,-6,-5,-5,-5,-4,-4,-3,-3,-2,-2,-1,-1,0,0,0};
int[] protSaltarTab;
int protSaltarPos=0;

public void protSaltarInit(int type)
{
	if (type == 0)
	{
		protSaltarTab = protSaltarTabNormal;
	//#ifndef SOUND_LOW
		soundPlay(9,1);
	//#endif
	} else {
		protSaltarTab = protSaltarTabTrampolin;
	//#ifndef SOUND_LOW
		soundPlay(10,1);
	//#endif
	}

	int base = protConCaja? 18:0;

	AniSprSET(protAnim, protX, protY, base+5, 1, 1, 0x002);

	protSaltarPos = protConCaja? 2:0;

	protMode = PROT_SALTAR;
}

// -------------------
// prot Saltar Tick
// ===================

public void protSaltarTick()
{

	checkTile(protX+(protWidth/2), protY+((protHeight/3)));
	checkTile(protX+(protWidth/2), protY+(protHeight-(protHeight/3)));


	if (keyX != 0)
	{
		protSide = keyX;
		protX += keyX * protSpd;
	}

	if (protX < -protCuX)
	{
		protX = -protCuX;
	} else
	if (protX > faseWidth*faseTileSize - (protCuX + protCuWidth))
	{
		protX = faseWidth*faseTileSize - (protCuX + protCuWidth);
	}


	protY += protSaltarTab[protSaltarPos];
	if (protY < -protHeight-10 || ++protSaltarPos == protSaltarTab.length) {protCaerInit();}


// Tiramos caja
	if (protConCaja && keyMenu > 0 && lastKeyMenu == 0)
	{
		protTiraCajaInit();
		return;
	}

}


// -------------------
// prot Caer Init
// ===================

int[] protCaerTab = new int[] {2,4,6,8};
int protCaerPos=0;

public void protCaerInit()
{
	int base = protConCaja? 18:0;

	AniSprSET(protAnim, protX, protY, base+2, 1, 1, 0x002);

	protCaerPos=0;

	protMode = PROT_CAER;
}

// -------------------
// prot Caer Tick
// ===================

public void protCaerTick()
{

	checkTile(protX+(protWidth/2), protY+((protHeight/3)));
	checkTile(protX+(protWidth/2), protY+(protHeight-(protHeight/3)));


	if (keyX != 0)
	{
		protSide = keyX;
		protX += keyX * protSpd;
	}

	if (protX < -protCuX)
	{
		protX = -protCuX;
	} else
	if (protX > faseWidth*faseTileSize - (protCuX + protCuWidth))
	{
		protX = faseWidth*faseTileSize - (protCuX + protCuWidth);
	}


// Chequeamos si estamos a punto de pisar el suelo
	int despY = protCaerTab[protCaerPos];
	if (++protCaerPos == protCaerTab.length) {protCaerPos--;}


	if (!checkSuelo(protX+(protCuX)              , protY+(protHeight)-1, despY)
	||  !checkSuelo(protX+(protCuX+protCuWidth-1), protY+(protHeight)-1, despY))
	{
		protY += despY;

		protCorrerInit();
		return;
	} else {
		protY += despY;
	}



// Chequeamos si estamos cayendo sobre un trampolin
	int dir1 = (((protY+(protHeight-(tileHeight/2)))/faseTileSize)*faseWidth)+((protX+protCentX)/faseTileSize);
	int dir2 = (((protY+(protHeight               ))/faseTileSize)*faseWidth)+((protX+protCentX)/faseTileSize);
	if (dir1 == dir2 && faseMap[dir1] == 76)
	{
		faseMap[dir1] = 77;
		faseTrampolin = dir1;

		protSaltarInit(1);
		return;
	}



// Tiramos caja
	if (protConCaja && keyMenu > 0 && lastKeyMenu == 0)
	{
		protTiraCajaInit();
		return;
	}


}


// -------------------
// prot Muerte Init
// ===================

int[] protMuerteTab = new int[] {-9,-8,-7,-6,-5,-4,-2,-1,0,0,1,2,4,6,8};
int protMuertePos=0;

public void protMuerteInit(int type)
{
	if (protMode == PROT_MUERTE) {return;}

	switch (type)
	{
	case 0:
		if (protInmune>0) {return;}

		if (--protToques > 0)
		{
			protInmune = 30;

			vibraInit(200);
			return;
		}
	break;

	case 1:
		faseStr = "Tiempo agotado";
	break;
	}

	AniSprSET(protAnim, protX, protY, 16, 2, 1, 0x001);

	protMuertePos = 0;

	protMortX = sc.ScrollX;
	protMortY = sc.ScrollY;

	vibraInit(300);

	soundPlay(3,1);

	protMode = PROT_MUERTE;
}

// -------------------
// prot Muerte Tick
// ===================

public void protMuerteTick()
{
	protY += protMuerteTab[protMuertePos];
	if (++protMuertePos == protMuerteTab.length) {protMuertePos--;}


	if (protY > sc.ScrollY+sc.ScrollSizeY + 150)
	{
		playExit = 2;
	}
}


// -------------------
// prot Pilla Caja Init
// ===================

public void protPillaCajaInit()
{
	AniSprSET(protAnim, protX, protY, 10, 1, 1, 0x002);
	protMode = PROT_PILLACAJA;
}

// -------------------
// prot Pilla Caja Tick
// ===================

public void protPillaCajaTick()
{
	if (AniSprs[protAnim].Pause == 0)
	{
		faseMap[protCajaPilladaDir] = faseCom[3+(13*5)];	// 13 es el numero de tile combinado de la caja

		protConCaja = true;
		protCorrerInit();
	}
}


// -------------------
// prot Tira Caja Init
// ===================

public void protTiraCajaInit()
{
	AniSprSET(protAnim, protX, protY, 11, 4, 1, 0x002);
	AniSprs[protAnim].Pause = 2;

	protModeExt = 0;

	protMode = PROT_TIRACAJA;
}

// -------------------
// prot Tira Caja Tick
// ===================

public void protTiraCajaTick()
{
	switch (protModeExt)
	{
	case 0:
		if (AniSprs[protAnim].Pause == 0)
		{
			AniSprs[protAnim].Pause = -1;

			protConCaja = false;

			int desp = protSide <0? 2:10;

			caixaSET(protX+desp, protY, protSide);

		//#ifndef SOUND_LOW
			soundPlay(6,1);
		//#endif			
			protModeExt++;
		}
	break;

	case 1:
		if (AniSprs[protAnim].Pause == 0)
		{
			protConCaja = false;
			protCorrerInit();
		}
	break;
	}
}


// -------------------
// prot Complet Init
// ===================

public void protCompletInit()
{
	if (protMode != PROT_COMPLET)
	{
		AniSprSET(protAnim, protX, protY, 28, 5, 1, 0x007);
		AniSprs[protAnim].Repetir = 5;
		protMode = PROT_COMPLET;

		faseRelojStop = true;

		faseStr = gameText[TEXT_ROOM_COMPLET][0];

		soundPlay(2,1);

		protModeExt = 0;
	}
}

// -------------------
// prot Complet Tick
// ===================

public void protCompletTick()
{
	switch (protModeExt)
	{
	case 0:
		if (AniSprs[protAnim].Pause == 0)
		{
			faseStr = null;
			protModeExt++;
		}
	break;

	case 1:
		gameText[TEXT_LEVEL_REVIEW][1] = faseTemps+" x10";
		gameText[TEXT_LEVEL_REVIEW][3] = faseItems+" x100";
		gameScore += (faseTemps*10)+(faseItems*100);
		gameText[TEXT_LEVEL_REVIEW][6] = gameScore+"";
		if (gameHiScore < gameScore) {gameHiScore = gameScore;}
		gameText[TEXT_LEVEL_REVIEW][8] = gameHiScore+"";

		ingameHelpText = TEXT_LEVEL_REVIEW;
		menuInit(IN_GAME_HELP);
		protModeExt++;
	break;

	case 2:
		playExit = 1;
	break;
	}
}


// <=- <=- <=- <=- <=-














// ********************
// --------------------
// llibre - Engine
// ====================
// ********************

static final int llibreWidth = 17;
static final int llibreHeight = 17;

int[] llibreCaerTab = new int[] {2,4,6,8};
int llibreCaerPos=0;

// ---------------
// llibre SET
// ===============

public int llibreSET(int CoorX, int CoorY)
{
	if (llibreC == llibreMAX) {return(-1);}

	int i=llibreP[llibreC++];

	llibres[i] = new llibre();

	llibres[i].SpriteSET(CoorX, CoorY,  0, 1, 1, 0x122);

	return i;
}

// ---------------
// llibre class
// ===============

public class llibre extends AnimSprite
{
int mode=0;

// ----------------
// llibre Play
// ================

public boolean Play()
{
	switch (mode)
	{
	case 0:
		if ( colision(protX+protCentX-1, protY+protCentY-1, 2, 2,   CoorX+4, CoorY+17, 8, 2*faseTileSize) )
		{
			SpriteSET(CoorX, CoorY,  0, 2, 0, 0x107);
			Repetir = 2;
			mode++;
		}
	break;

	case 1:
		if (Pause == 0)
		{
			SpriteSET(CoorX, CoorY,  2, 7, 0, 0x102);
			mode++;
		}
	break;

	case 2:
		int despY = llibreCaerTab[llibreCaerPos];
		if (++llibreCaerPos == llibreCaerTab.length) {llibreCaerPos--;}

		CoorY += despY;

		if (CoorY > sc.ScrollY+sc.ScrollSizeY) {return true;}
	break;
	}

	return super.Play();
}

};


// ---------------
// llibre INI
// ===============

final int llibreMAX=28;
int llibreC;
int[] llibreP;
llibre[] llibres;

public void llibreINI()
{
	llibreC = 0;
	llibreP = new int[llibreMAX];
	llibres = new llibre[llibreMAX];
	for (int i=0 ; i<llibreMAX ; i++) {llibreP[i]=i;}
}

// ---------------
// llibre RES
// ===============

public void llibreRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=llibreP[i];
	llibres[t] = null;
	llibreP[i--] = llibreP[--llibreC];
	llibreP[llibreC] = t;
}

// ---------------
// llibre END
// ===============

public void llibreEND()
{
	llibreP = null;
	llibres = null;
	llibreC = 0;
}

// ---------------
// llibre RUN
// ===============

public void llibreRUN()
{
	for (int i=0 ; i<llibreC ; i++)
	{
	if ( llibres[llibreP[i]].Play() )
		{
		int t=llibreP[i];
		llibres[t] = null;
		llibreP[i--] = llibreP[--llibreC];
		llibreP[llibreC] = t;
		}
	}
}

// ---------------
// llibre Colision
// ===============

public boolean llibreColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<llibreC ; i++)
	{
		int anim = llibreP[i];
		if ( llibres[anim].mode == 2 && colision(llibres[anim].CoorX, llibres[anim].CoorY, llibreWidth-2, llibreHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
//			llibres[anim].destroy = true;
			return true;
		}
	}
	return false;
}

// <=- <=- <=- <=- <=-










// ********************
// --------------------
// cotxe - Engine
// ====================
// ********************

static final int cotxeWidth = 48;
static final int cotxeHeight = 48;

static final int cotxeCuX = 10;
static final int cotxeCuY = 34;
static final int cotxeCuWidth = 28;
static final int cotxeCuHeight = 14;

int[] morirTab = new int[] {-8,-6,-2,-1,0,2,5,8,12,16};

// ---------------
// cotxe SET
// ===============

public int cotxeSET(int CoorX, int CoorY, int side)
{
	if (cotxeC == cotxeMAX) {return(-1);}

	int i=cotxeP[cotxeC++];

	cotxes[i] = new cotxe();

	cotxes[i].SpriteSET(CoorX, CoorY,  0, 2, 1, 0x301);
	cotxes[i].side = side;

	return i;
}

// ---------------
// cotxe class
// ===============

public class cotxe extends AnimSprite
{
int mode=0;
int side;

int morirPos;

// ----------------
// cotxe Play
// ================

public boolean Play()
{
	switch (mode)
	{
	case 0:

	// Si colisiona con caja, cotxe destruido
		if ( caixaColision(CoorX+cotxeCuX, CoorY+cotxeCuY,  cotxeCuWidth, cotxeCuHeight) )
		{
			vibraInit(150);
			side = caixas[caixaColisionAnim].Side;;
			Speed = 0;
			mode = 80;
			break;
		}


	// Desplazamiento horizontal
		CoorX += side * 2;


	// Hay suelo?
		if (checkSuelo(CoorX+(cotxeCuX)               , CoorY+(cotxeHeight)-1, 2)
		||  checkSuelo(CoorX+(cotxeCuX+cotxeCuWidth-1), CoorY+(cotxeHeight)-1, 2))
		{
			side *= -1;
			CoorX += side * 2;
		}


	// Controlamos colision contra las paredes
		if (CoorX < 0) {side=1;}
		if (CoorX > (faseWidth*faseTileSize)-cotxeWidth) {side = -1;}



	// Tenemos a Ms Frame a la vista?
		int desp = side<0?(sc.ScrollSizeX/2)*side:0;
		if ( colision(CoorX+(cotxeWidth/2)+desp, CoorY+cotxeCuY, sc.ScrollSizeX/2, cotxeCuHeight,  protX+protCuX, protY+protCuY, protCuWidth, protCuHeight))
		{
			SpriteSET(2, 2, 1, 0x301);
			mode++;
			break;
		}
	break;

	case 1:

	// NO Tenemos a Ms Frame a la vista?
		desp = side<0?(sc.ScrollSizeX/2)*side:0;
		if ( !colision(CoorX+(cotxeWidth/2)+desp, CoorY, sc.ScrollSizeX/2, cotxeHeight,  protX+protCuX, protY+protCuY, protCuWidth, protCuHeight))
		{
			SpriteSET(0, 2, 1, 0x301);
			mode--;
			break;
		}

	// Si colisiona con caja, cotxe destruido
		if ( caixaColision(CoorX+cotxeCuX, CoorY+cotxeCuY,  cotxeCuWidth, cotxeCuHeight) )
		{
			vibraInit(150);
			side = caixas[caixaColisionAnim].Side;;
			Speed = 0;
			mode = 80;
			break;
		}


	// Desplazamiento horizontal
		CoorX += side * 4;


	// Hay suelo?
		if (checkSuelo(CoorX+(cotxeCuX)               , CoorY+(cotxeHeight)-1, 2)
		||  checkSuelo(CoorX+(cotxeCuX+cotxeCuWidth-1), CoorY+(cotxeHeight)-1, 2))
		{
			SpriteSET(0, 2, 1, 0x301);
			side *= -1;
			CoorX += side * 4;
		}


	// Controlamos colision contra las paredes
		if (CoorX < 0) {side=1;}
		if (CoorX > (faseWidth*faseTileSize)-cotxeWidth) {side = -1;}

	break;

	case 80:
		CoorX += side * 4;
		CoorY += morirTab[morirPos];
		if (++morirPos == morirTab.length) {morirPos--;}

		if (CoorY > faseHeight*tileHeight) {return true;}
	break;
	}

	FrameBase = side<0?0:4;

	return super.Play();
}

};


// ---------------
// cotxe INI
// ===============

final int cotxeMAX=16;
int cotxeC;
int[] cotxeP;
cotxe[] cotxes;

public void cotxeINI()
{
	cotxeC = 0;
	cotxeP = new int[cotxeMAX];
	cotxes = new cotxe[cotxeMAX];
	for (int i=0 ; i<cotxeMAX ; i++) {cotxeP[i]=i;}
}

// ---------------
// cotxe RES
// ===============

public void cotxeRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=cotxeP[i];
	cotxes[t] = null;
	cotxeP[i--] = cotxeP[--cotxeC];
	cotxeP[cotxeC] = t;
}

// ---------------
// cotxe END
// ===============

public void cotxeEND()
{
	cotxeP = null;
	cotxes = null;
	cotxeC = 0;
}

// ---------------
// cotxe RUN
// ===============

public void cotxeRUN()
{
	for (int i=0 ; i<cotxeC ; i++)
	{
	if ( cotxes[cotxeP[i]].Play() )
		{
		int t=cotxeP[i];
		cotxes[t] = null;
		cotxeP[i--] = cotxeP[--cotxeC];
		cotxeP[cotxeC] = t;
		}
	}
}

// ---------------
// cotxe Colision
// ===============

public boolean cotxeColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<cotxeC ; i++)
	{
		int anim = cotxeP[i];
		if ( colision(cotxes[anim].CoorX+cotxeCuX, cotxes[anim].CoorY+cotxeCuY, cotxeCuWidth, cotxeCuHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
//			cotxes[anim].destroy = true;
			return true;
		}
	}
	return false;
}

// <=- <=- <=- <=- <=-













// ********************
// --------------------
// calcetin - Engine
// ====================
// ********************

static final int calcetinWidth = 30;
static final int calcetinHeight = 30;

static final int calcetinCuX = 5;
static final int calcetinCuY = 20;
static final int calcetinCuWidth = 20;
static final int calcetinCuHeight = 10;

// ---------------
// calcetin SET
// ===============

public int calcetinSET(int CoorX, int CoorY, int side)
{
	if (calcetinC == calcetinMAX) {return(-1);}

	int i=calcetinP[calcetinC++];

	calcetins[i] = new calcetin();

	calcetins[i].SpriteSET(CoorX, CoorY,  0, 7, 1, 0x601);
	calcetins[i].side = side;

	return i;
}

// ---------------
// calcetin class
// ===============

public class calcetin extends AnimSprite
{
int mode=0;
int side;

int morirPos;

// ----------------
// calcetin Play
// ================

public boolean Play()
{
	switch (mode)
	{
	case 0:
		CoorX += side;


	// Hay suelo?
		if (checkSuelo(CoorX+(calcetinCuX)                  , CoorY+(calcetinHeight)-1, 2)
		||  checkSuelo(CoorX+(calcetinCuX+calcetinCuWidth-1), CoorY+(calcetinHeight)-1, 2))
		{
			side *= -1;
			CoorX += side;
		}


		if (CoorX < 0) {side=1;}
		if (CoorX > (faseWidth*faseTileSize)-calcetinWidth) {side = -1;}


		if ( caixaColision(CoorX+calcetinCuX, CoorY+calcetinCuY,  calcetinCuWidth, calcetinCuHeight) )
		{
			vibraInit(150);
			side = caixas[caixaColisionAnim].Side;;
			Speed = 0;
			mode = 80;
			break;
		}

	break;

	case 80:
		CoorX += side * 4;
		CoorY += morirTab[morirPos];
		if (++morirPos == morirTab.length) {morirPos--;}

		if (CoorY > faseHeight*tileHeight) {return true;}
	break;
	}

	FrameBase = side<0?0:7;

	return super.Play();
}

};


// ---------------
// calcetin INI
// ===============

final int calcetinMAX=16;
int calcetinC;
int[] calcetinP;
calcetin[] calcetins;

public void calcetinINI()
{
	calcetinC = 0;
	calcetinP = new int[calcetinMAX];
	calcetins = new calcetin[calcetinMAX];
	for (int i=0 ; i<calcetinMAX ; i++) {calcetinP[i]=i;}
}

// ---------------
// calcetin RES
// ===============

public void calcetinRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=calcetinP[i];
	calcetins[t] = null;
	calcetinP[i--] = calcetinP[--calcetinC];
	calcetinP[calcetinC] = t;
}

// ---------------
// calcetin END
// ===============

public void calcetinEND()
{
	calcetinP = null;
	calcetins = null;
	calcetinC = 0;
}

// ---------------
// calcetin RUN
// ===============

public void calcetinRUN()
{
	for (int i=0 ; i<calcetinC ; i++)
	{
	if ( calcetins[calcetinP[i]].Play() )
		{
		int t=calcetinP[i];
		calcetins[t] = null;
		calcetinP[i--] = calcetinP[--calcetinC];
		calcetinP[calcetinC] = t;
		}
	}
}

// ---------------
// calcetin Colision
// ===============

public boolean calcetinColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<calcetinC ; i++)
	{
		int anim = calcetinP[i];
		if ( colision(calcetins[anim].CoorX+calcetinCuX, calcetins[anim].CoorY+calcetinCuY, calcetinCuWidth, calcetinCuHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
//			calcetins[anim].destroy = true;
			return true;
		}
	}
	return false;
}

// <=- <=- <=- <=- <=-








// ********************
// --------------------
// globo - Engine
// ====================
// ********************

static final int globoWidth = 25;
static final int globoHeight = 25;

static final int globoCuX = 5;
static final int globoCuY = 0;
static final int globoCuWidth = 15;
static final int globoCuHeight = 20;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Tabla Seno
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
int[] tablaSeno = new int[] {
	0x00,0x04,0x09,0x0D,0x12,0x16,0x1B,0x1F,0x24,0x28,0x2C,0x31,0x35,
	0x3A,0x3E,0x42,0x47,0x4B,0x4F,0x53,0x58,0x5C,0x60,0x64,0x68,
	0x6C,0x70,0x74,0x78,0x7C,0x80,0x84,0x88,0x8B,0x8F,0x93,0x96,
	0x9A,0x9E,0xA1,0xA5,0xA8,0xAB,0xAF,0xB2,0xB5,0xB8,0xBB,0xBE,
	0xC1,0xC4,0xC7,0xCA,0xCC,0xCF,0xD2,0xD4,0xD7,0xD9,0xDB,0xDE,
	0xE0,0xE2,0xE4,0xE6,0xE8,0xEA,0xEC,0xED,0xEF,0xF1,0xF2,0xF3,
	0xF5,0xF6,0xF7,0xF8,0xF9,0xFA,0xFB,0xFC,0xFD,0xFE,0xFE,0xFF,
	0xFF,0xFF,0x100,0x100,0x100,0x100};
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// ---------------
// globo SET
// ===============

public int globoSET(int CoorX, int CoorY)
{
	if (globoC == globoMAX) {return(-1);}

	int i=globoP[globoC++];

	globos[i] = new globo();

	globos[i].SpriteSET(CoorX, CoorY,  0, 1, 1, 0x502);

	globos[i].baseX = CoorX;
	globos[i].superY = CoorY<<4;
	globos[i].time = rnd(250)+250;

	return i;
}

// ---------------
// globo class
// ===============

public class globo extends AnimSprite
{
int mode=0;

int baseX;
int superY;
int time;
int conta;
boolean destroy = false;

// ----------------
// globo Play
// ================

public boolean Play()
{

	switch (mode)
	{
	case 0:

		if (--time < 0) {mode++;}

	break;

	case 1:

		if ( destroy || caixaColision(CoorX+globoCuX, CoorY+globoCuY,  globoCuWidth, globoCuHeight) )
		{
			vibraInit(150);
			SpriteSET(1, 2, 3, 0x500);
			mode++;
			return false;
		}


		int pos = conta % tablaSeno.length;
		switch (conta / tablaSeno.length)
		{
		case 0:
			CoorX = baseX + (regla3(tablaSeno[pos],0x100, 12));
		break;

		case 1:
			CoorX = baseX + (regla3(tablaSeno[tablaSeno.length-pos],0x100, 12));
		break;

		case 2:
			CoorX = baseX - (regla3(tablaSeno[pos],0x100, 12));
		break;

		case 3:
			CoorX = baseX - (regla3(tablaSeno[tablaSeno.length-pos],0x100, 12));
		break;
		}
		conta += 8;
		if (conta >= tablaSeno.length*4) {conta-=tablaSeno.length*4;}




		superY -= 16;
		CoorY = superY>>4;


		if (  CoorY+globoHeight < 0)
		{
			CoorY = faseHeight*tileHeight;
			superY = CoorY<<4;
		}
	break;
	}

	return super.Play();
}

};


// ---------------
// globo INI
// ===============

final int globoMAX=16;
int globoC;
int[] globoP;
globo[] globos;

public void globoINI()
{
	globoC = 0;
	globoP = new int[globoMAX];
	globos = new globo[globoMAX];
	for (int i=0 ; i<globoMAX ; i++) {globoP[i]=i;}
}

// ---------------
// globo RES
// ===============

public void globoRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=globoP[i];
	globos[t] = null;
	globoP[i--] = globoP[--globoC];
	globoP[globoC] = t;
}

// ---------------
// globo END
// ===============

public void globoEND()
{
	globoP = null;
	globos = null;
	globoC = 0;
}

// ---------------
// globo RUN
// ===============

public void globoRUN()
{
	for (int i=0 ; i<globoC ; i++)
	{
	if ( globos[globoP[i]].Play() )
		{
		int t=globoP[i];
		globos[t] = null;
		globoP[i--] = globoP[--globoC];
		globoP[globoC] = t;
		}
	}
}

// ---------------
// globo Colision
// ===============

public boolean globoColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<globoC ; i++)
	{
		int anim = globoP[i];
		if ( colision(globos[anim].CoorX+globoCuX, globos[anim].CoorY+globoCuY, globoCuWidth, globoCuHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
			globos[anim].destroy = true;
			return true;
		}
	}
	return false;
}

// <=- <=- <=- <=- <=-










// ********************
// --------------------
// caixa - Engine
// ====================
// ********************

static final int caixaWidth = 16;
static final int caixaHeight = 16;

static final int caixaCuX = 1;
static final int caixaCuY = 1;
static final int caixaCuWidth = 14;
static final int caixaCuHeight = 14;

// ---------------
// caixa SET
// ===============

public int caixaSET(int CoorX, int CoorY, int side)
{
	if (caixaC == caixaMAX) {return(-1);}

	int i=caixaP[caixaC++];

	caixas[i] = new caixa();

	caixas[i].SpriteSET(CoorX, CoorY,  15, 1, 1, 0x002);
	caixas[i].Side = side;

	return i;
}

// ---------------
// caixa class
// ===============

public class caixa extends AnimSprite
{
int mode=0;


// ----------------
// caixa Play
// ================

public boolean Play()
{

	if ( !colision(sc.ScrollX, sc.ScrollY, sc.ScrollSizeX, sc.ScrollSizeY,  CoorX, CoorY, caixaWidth, caixaHeight) )
	{
		return true;
	}


	CoorX += Side * 12;
	CoorY += 6;


	return super.Play();
}

};


// ---------------
// caixa INI
// ===============

final int caixaMAX=6;
int caixaC;
int[] caixaP;
caixa[] caixas;

public void caixaINI()
{
	caixaC = 0;
	caixaP = new int[caixaMAX];
	caixas = new caixa[caixaMAX];
	for (int i=0 ; i<caixaMAX ; i++) {caixaP[i]=i;}
}

// ---------------
// caixa RES
// ===============

public void caixaRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=caixaP[i];
	caixas[t] = null;
	caixaP[i--] = caixaP[--caixaC];
	caixaP[caixaC] = t;
}

// ---------------
// caixa END
// ===============

public void caixaEND()
{
	caixaP = null;
	caixas = null;
	caixaC = 0;
}

// ---------------
// caixa RUN
// ===============

public void caixaRUN()
{
	for (int i=0 ; i<caixaC ; i++)
	{
	if ( caixas[caixaP[i]].Play() )
		{
		int t=caixaP[i];
		caixas[t] = null;
		caixaP[i--] = caixaP[--caixaC];
		caixaP[caixaC] = t;
		}
	}
}

// ---------------
// caixa Colision
// ===============

int caixaColisionAnim;

public boolean caixaColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<caixaC ; i++)
	{
		int anim = caixaP[i];
		if ( colision(caixas[anim].CoorX+caixaCuX, caixas[anim].CoorY+caixaCuY, caixaCuWidth, caixaCuHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
			caixaColisionAnim = anim;
//			caixas[anim].destroy = true;
			return true;
		}
	}
	return false;
}

// <=- <=- <=- <=- <=-










// ********************
// --------------------
// Anim Sprite - Engine
// ====================
// ********************

final int AniSprMAX = 32;
int AniSprC;
int[] AniSprP;
AnimSprite[] AniSprs;

// ---------------
// Anim Sprite SET
// ===============

public int AniSprSET(int i, int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	if (i<0)
	{
	if (AniSprC == AniSprMAX) {return(-1);}
	i=AniSprP[AniSprC++];
	AniSprs[i] = new AnimSprite();
	}

	AniSprs[i].SpriteSET(CoorX, CoorY, FrameIni, Frames, Speed, Mode);

	return (i);
}


public int AniSprSET(int i, int FrameIni, int Frames, int Speed, int Mode)
{
	return AniSprSET(i, AniSprs[i].CoorX, AniSprs[i].CoorY, FrameIni, Frames, Speed, Mode);
}


// ---------------
// Anim Sprite INI
// ===============

public void AniSprINI()
{
	AniSprC = 0;
	AniSprP = new int[AniSprMAX];
	AniSprs = new AnimSprite[AniSprMAX];
	for (int i=0 ; i<AniSprMAX ; i++) {AniSprP[i]=i;}
}

// ---------------
// Anim Sprite RES
// ===============

public int AniSprRES(int i)
{
	if (i!=-1) {AniSprs[i].Frames=0; AniSprs[i].Pause=0;}
	return -1;
}

// ---------------
// Anim Sprite END
// ===============

public void AniSprEND()
{
	AniSprs = null; AniSprP = null; AniSprC = 0;
}

// ---------------
// Anim Sprite RUN
// ===============

public void AniSprRUN()
{
	for (int i=0 ; i<AniSprC ; i++)
	{
	if ( AniSprs[AniSprP[i]].Play() )
		{
		int t=AniSprP[i]; AniSprs[t] = null;
		AniSprP[i--] = AniSprP[--AniSprC]; AniSprP[AniSprC] = t;
		}
	}
}

// -------------------
// Anim Sprite Ordenar
// ===================
/*
public void AniSprOrdenar()
{
	int Conta=AniSprC-1;

	for (int t=0 ; t<AniSprC ; t++)
	{
		for (int i=0 ; i<Conta ; i++)
		{
			if (AniSprs[AniSprP[i]].Prio < AniSprs[AniSprP[i+1]].Prio)
			{
			int dato=AniSprP[i+1];
			AniSprP[i+1] = AniSprP[i];
			AniSprP[i] = dato;
			}
		}
	Conta--;
	}
}
*/
// <=- <=- <=- <=- <=-






public void AniSprIMP(AnimSprite[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
		i=P[t];
		if (!s[i].show) {continue;}
		switch (s[i].Bank)
		{
		case 0:
			spriteDraw(spritesImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY+2,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, spritesCor);
		break;

		case 1:
			spriteDraw(llibreImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, llibreCor);
		break;

		case 2:
			spriteDraw(particulesImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, particulesCor);
		break;

		case 3:
			spriteDraw(cotxeImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, cotxeCor);
		break;

		case 5:
			spriteDraw(globoImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, globoCor);
		break;

		case 6:
			spriteDraw(calcetinImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, calcetinCor);
		break;
		}
	}
}




/*
// -------------------
// sprite Draw
// ===================

//#ifdef J2ME
public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int flip=0;
	if (sizeX < 0) {sizeX*=-1; flip|=1;}
	if (sizeY < 0) {sizeY*=-1; flip|=2;}
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame  ] + 128;


	if (destX >= playWidth || destY >= playHeight || (destX + sizeX) <= 0 || (destY + sizeY) <= 0) {return;}

//#ifdef SI-x55
	if (destX < 0) {sourX+=-destX; sizeX+=destX; destX=0;}
	if (destY < 0) {sourY+=-destY; sizeY+=destY; destY=0;}
//#endif

	scr.setClip (0, 0, canvasWidth, canvasHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);
	scr.drawImage(img, destX-sourX, destY-sourY, Graphics.TOP|Graphics.LEFT);
}
//#endif

//#ifdef DOJA
public void spriteDraw(Image[] img,  int x, int y,  int frame, byte[] cor)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;

	scr.drawImage(img[(cor[frame+2]&0xff)], destX, destY);

//	ga.ScrollUpdate(destX, destY, cor[frame++], cor[frame]);
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




//#ifdef NOKIAUI
public void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       >= canvasWidth)
	||	(DestX+SizeX < 0)
	||	(DestY       >= canvasHeight)
	||	(DestY+SizeY < 0))
	{return;}

	scr.setClip (0, 0, playWidth, playHeight);
	scr.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
	dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
	return;

	case 2:
	dscr.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
	dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
	return;
	}
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
*/







// *******************
// -------------------
// ingameHelp - Engine
// ===================
// *******************

int[] ingameHelp = new int[] {
	0, 1,30, TEXT_INGAME_HELP_INICIO_NIVEL,
	0, 5,30, TEXT_INGAME_HELP_SOBRE_PUERTA,
	0,19,30, TEXT_INGAME_HELP_SOBRE_CAIXA,
	0, 5,13, TEXT_INGAME_HELP_SOBRE_RELOJ,
	0,27,13, TEXT_INGAME_HELP_SOBRE_3XL,
	0, 5, 3, TEXT_INGAME_HELP_SOBRE_CACAO,
};

int ingameHelpText;
boolean ingameClaus;

// -------------------
// ingameHelp Create
// ===================

public void ingameHelpCreate()
{
	for (int i=0 ; i<ingameHelp.length ; i+=4)
	{
		ingameHelp[i] = 0;
	}
	ingameClaus = true;
}

// -------------------
// ingameHelp Tick
// ===================

public boolean ingameHelpTick()
{
	if (faseLevel != 1) {return false;}

	if (ingameClaus && faseClaus == 0)
	{
		ingameClaus = false;
		ingameHelpText = TEXT_INGAME_HELP_TODAS_LLAVES;
		menuInit(IN_GAME_HELP);
		return true;
	}

	checkTile(protX+protCentX, protY+protHeight-4);

	for (int i=0 ; i<ingameHelp.length ; i+=4)
	{
		if (ingameHelp[i] == 0 && checkTileDir == (ingameHelp[i+2] * faseWidth) + ingameHelp[i+1])
		{
			ingameHelp[i] = 1;
			ingameHelpText = ingameHelp[i+3];
			menuInit(IN_GAME_HELP);
			return true;
		}
	}

	return false;
}

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

byte[] prefsData;

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{

// Cargamos un byte[] con las ultimas prefs grabadas

	prefsData = updatePrefs(null);	// Recuperamos byte[] almacenado la ultima vez

// Si es null es que nunca se han grabado prefs, por lo cual INICIALIZAMOS las prefs del juego

	if (prefsData == null)
	{
		prefsData = new byte[] {1, 1, 1, 0, 0,0,0,0, 0,0,0,0};		// Inicializamos preferencias ya que NO estaban grabadas
	}

// Actualizamos las variables del juego segun las prefs leidas / inicializadas

	gameSound = prefsData[0] != 0;
	gameVibra = prefsData[1] != 0;
	gameLevelLast = prefsData[2];
	gameIdioma = prefsData[3];
	gameScore = convAry2Int(prefsData, 4);
	gameHiScore = convAry2Int(prefsData, 8);
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{

// Ponemos las varibles del juego a salvar como prefs.

	prefsData[0] = (byte)(gameSound? 1:0);
	prefsData[1] = (byte)(gameVibra? 1:0);
	prefsData[2] = (byte)gameLevelLast;
	prefsData[3] = (byte)gameIdioma;
	convInt2Ary(prefsData, 4, gameScore);
	convInt2Ary(prefsData, 8, gameHiScore);

// Almacenamos las prefs

	updatePrefs(prefsData);		// Almacenamos byte[]
}

// <=- <=- <=- <=- <=-



// -------------------
// cheats
// ===================

//#ifdef CHEATS
//#endif

// <=- <=- <=- <=- <=-





// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------

// ******************* //
//  Final de la Clase  //
// ******************* //
};