

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.7 (08.4.2005)
// ===============================================
// Representacion Grafica
// ------------------------------------


//#define !RMS_ENGINE


//#ifdef PACKAGE
package com.mygdx.mongojocs.toro2;
//#endif


//#ifdef J2ME
//#endif

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
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


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME

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
//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.println (" -+- showNotify()");
//#endif

	if (!gameStarted) {return;}

	gamePaused = false;

	gameForceRefresh = true;

//#ifndef DISABLE_SOUND_REFRESH
	gameSoundRefresh = true;
//#endif

/*
	if (biosStatus == 0)
	{
		biosStatusOld = biosStatus;
		biosStatus = BIOS_PAUSE;
		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
	}
*/

	intKeyX = intKeyY = intKeyMisc = intKeyMenu = 0;
}

public void hideNotify()
{
//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.println (" -+- hideNotify()");
//#endif

	if (!gameStarted) {return;}

	gamePaused = true;

//#ifndef DISABLE_SOUND_REFRESH
	gameSoundOld = soundOld;
	gameSoundLoop = soundLoop;
	soundStop();
//#endif
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

boolean gameStarted = false;

long gameMilis, CPU_Milis;
int gameSleep;

//#ifdef MO-C450
//#else
int mainSleep = 65;
//#endif

public void run()
{
	System.gc();
	biosCreate();
	System.gc();

	gameStarted = true;

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
			//#ifdef com.mygdx.mongojocs.toro2.Debug
				Debug.spdStart(0);
			//#endif
				biosTick();
			//#ifdef com.mygdx.mongojocs.toro2.Debug
				Debug.spdStop(0);
			//#endif

				if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}
				gameDraw();

				if (gameSoundRefresh)
				{
				//#ifdef FIX_NK_INCOMING_CALL
				//#endif

					gameSoundRefresh = false;
					if (gameSoundOld >= 0 && gameSoundLoop==0) {soundPlay(gameSoundOld,gameSoundLoop);}
				} else {
					soundTick();
				}

			} catch (Exception e)
			{
			//#ifdef com.mygdx.mongojocs.toro2.Debug
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

//#ifdef NOKIAUI
DirectGraphics dscr;
//#endif

public void paint (Graphics g)
{
	if (canvasShow)
	{
		synchronized (this)
		{
		//#ifdef com.mygdx.mongojocs.toro2.Debug
			Debug.spdStart(1);
		//#endif
		
			scr = g;

		//#ifdef NOKIAUI
			dscr = DirectUtils.getDirectGraphics(g);
		//#endif

		//#ifdef DOJA
		//#endif

			try {
				canvasDraw();
			} catch (Exception e)
			{
			//#ifdef com.mygdx.mongojocs.toro2.Debug
				Debug.println("*** Exception Grafica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}

		//#ifdef DOJA
		//#endif

		//#ifdef com.mygdx.mongojocs.toro2.Debug
			Debug.spdStop(1);
			if (Debug.enabled) {Debug.debugDraw(this, scr);}
		//#endif

		//#ifdef DOJA
		//#endif

			scr = null;
			canvasShow=false;
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
//#ifdef FIX_SOFTKEY_RELEASE
//#endif

	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;

	switch (keycode)
	{
//#ifndef NK-3650
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

	case 42:		// *
		intKeyMisc=11;
	return;

	case 35:		// #
		intKeyMisc=12;
	return;
//#endif

// -----------------------------------------

//#ifdef SM-MYVxx
//#elifdef SD-P600
//#elifdef PA-X500
//#elifdef MO-C450
//#elifdef MO-T720
//#elifdef MO-V3xx
//#elifdef MO-C65x
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
//#else
	//#ifdef MIDP20
	//#endif
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
static final int BIOS_PAUSE = 44;

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
// Inicializamos com.mygdx.mongojocs.toro2.Debug Engine
// ================================
//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.debugCreate(this);
//#endif
// ================================


// --------------------------------
// Pintamos TODO el canvas de forma INMEDIATA
// ================================
	canvasFillTask(0xffffff);		// Color Blanco
	gameDraw();
//#ifdef FULLSCREEN
	//#ifndef LISTENER_DOWN
		canvasHeight -= listenerHeight;
	//#endif
//#endif

//#ifdef SI-x55
//#endif
		canvasFillTask(0xffffff);		// Color Blanco
		canvasImg = loadImage("/loading");
		gameDraw();						// FORZAMOS una llamada a 'canvasDraw()' AHORA MISMO.
//#ifdef SI-x55
//#endif

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



	gameText = textosCreate( loadFile("/Textos.txt") );


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

//#ifdef com.mygdx.mongojocs.toro2.Debug
	if (keyMisc == 12 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; if (!Debug.enabled) {gameForceRefresh=true;}}
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
//	case BIOS_POPUP:
//		if ( popupTick() ) {biosStatus = biosStatusOld;}
//	return;
// --------------------

// --------------------
// pause Bucle
// --------------------
//	case BIOS_PAUSE:
//		if (keyMenu > 0 && lastKeyMenu == 0) {biosStatus = biosStatusOld;}
//	return;
// --------------------
	}
}

// -------------------
// bios Refresh
// ===================

public void biosRefresh()
{
//#ifdef com.mygdx.mongojocs.toro2.Debug
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
	Font f = magicBiosGetFont(Dato);

	if ( f.stringWidth(Texto) <= menuListSizeX)
	{
		menuListAdd(Dato, new String[] {Texto}, 0, 0);
	} else {

		String[] Textos = textBreak(Texto, menuListSizeX, magicBiosGetFont(Dato) );

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

	Font f = magicBiosGetFont(Dat0);

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

public void menuListDraw()
{
	if (!menuList_ON || !menuListShow) {return;}

	menuListShow = false;

	if (menuImg!=null) {canvasFillDraw(0); showImage(menuImg, 0,0);}


//#ifdef J2ME
	scr.setClip(menuListX, menuListY, menuListSizeX, menuListSizeY);
//#endif

	switch (menuListMode)
	{
	case ML_OPTION:

		int aspaWidth = menuListStr.length>1?magicBiosGetFont(0).stringWidth("<"):0;

		String[] texto = textBreak(menuListStr[menuListPos][menuListDat[menuListPos][2]], (menuListSizeX - aspaWidth - 4), magicBiosGetFont(0) );

		int height = (texto.length * menuListDat[menuListPos][4]);

    //int y = menuListY + (( menuListSizeY / 4) * 3) - (height/2);

		int y = menuListY + (( menuListSizeY / 8) * 7)+3;
		if (y + height > menuListSizeY) {y = menuListY + (menuListSizeY-height);}


		alphaFillDraw(0x80000000, 0,y-2, canvasWidth, height+4 );

	// Imprimimos "<" y ">"
		if (aspaWidth > 0)
		{
			menuListTextDraw(1, "<", 1, y + ((height-menuListDat[menuListPos][4])/2) );
			menuListTextDraw(1, ">", canvasWidth - aspaWidth - 1, y + ((height-menuListDat[menuListPos][4])/2) );
		}


		for (int i=0 ; i<texto.length ; i++)
		{
			menuListTextDraw(menuListDat[i][0], texto[i], 0,y);

			y += menuListDat[menuListPos][4];
		}
	break;


	case ML_SCROLL:

		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			menuListTextDraw(menuListDat[i][0], menuListStr[i][0], 0,y);

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
			menuListTextDraw(menuListDat[i][0], menuListStr[ini+i][0], menuListX, y);

			y += menuListDat[i][4];
		}
	break;
	}
}






public void menuListTextDraw(int format, String str, int x, int y)
{
	Font font = magicBiosGetFont(format);

	scr.setFont(font);

	if (format == 0)
	{
		x = menuListX + (( menuListSizeX - font.stringWidth(str) ) / 2);
	}

//#ifdef J2ME

	putColor(0);
	scr.drawString(str, x-1,y, 20);
	scr.drawString(str, x+1,y, 20);
	scr.drawString(str, x,y-1, 20);
	scr.drawString(str, x,y+1, 20);

	scr.drawString(str, x-1,y-1, 20);
	scr.drawString(str, x+1,y+1, 20);

	putColor(0xFFFFFF);
	scr.drawString(str, x,y, 20);

//#elifdef DOJA
//#endif

}





public Font magicBiosGetFont(int dat)
{

//#ifdef BIOS_FONT_SIZE_LARGE
//#elifdef BIOS_FONT_SIZE_MEDIUM
//#else
	//#ifdef J2ME
		Font f = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL);
	//#elifdef DOJA
	//#endif
//#endif


//#ifdef FIX_SG_BAD_FONT_WIDTH
//#endif

	return f;
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
	return ((int)(System.currentTimeMillis() - waitMillis)) > time;
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

public void alphaFillDraw(int argb, int x, int y, int width, int height)
{
//#ifdef MIDP20
//#elifdef NOKIAUI
	//#ifdef NK-s60
		int[] temp = new int[width];
		for (int i=0 ; i<width ; i++) {temp[i] = argb;}
		scr.setClip(x, y, width, height);
		for (int i=0 ; i<height ; i++)
		{
			dscr.drawPixels(temp, true, 0,width, x,y++, width,1, 0, DirectGraphics.TYPE_INT_8888_ARGB);
		}
	//#else
	//#endif
//#else
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

	imageDraw(img, sourX, sourY,  sizeX, sizeY,  destX, destY,  flip,  0 ,0, canvasWidth, canvasHeight);
}

static final int R_HCENTER = 0x04;
static final int R_VCENTER = 0x08;

public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor, int cuadX, int cuadY, int cuadWidth, int cuadHeight, int mode)
{
	frame*=6;

	int destX=cor[frame++];
	int destY=cor[frame++];
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int flip=0;
	if (sizeX < 0) {sizeX*=-1; flip|=1;}
	if (sizeY < 0) {sizeY*=-1; flip|=2;}
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame  ] + 128;


	if ((mode & R_HCENTER) != 0 ) {destX = (cuadWidth - sizeX - (destX<<1))>>1;}
	if ((mode & R_VCENTER) != 0 ) {destY = (cuadHeight - sizeY - (destY<<1))>>1;}

	destX += x + cuadX;
	destY += y + cuadY;

	imageDraw(img, sourX, sourY,  sizeX, sizeY,  destX, destY,  flip,  cuadX ,cuadY, cuadWidth, cuadHeight);
}

// ----------------------------------------------------------
//#elifdef DOJA
// ----------------------------------------------------------
// ----------------------------------------------------------
//#endif



//#ifdef J2ME
// ----------------------------------------------------------

//#ifdef MIDP20
//#elifdef NOKIAUI
public void imageDraw(Image img, int sourX, int sourY, int sizeX, int sizeY, int destX, int destY, int flip, int topX, int topY, int topWidth, int topHeight)
{
	if ((destX       >= topX+topWidth)
	||	(destX+sizeX < topX)
	||	(destY       >= topY+topHeight)
	||	(destY+sizeY < topY))
	{return;}

	scr.setClip (topX, topY, topWidth, topHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);

	switch (flip)
	{
	case 0:
		scr.drawImage(img, destX-sourX, destY-sourY, 20);
	return;

	case 1:
		dscr.drawImage(img, (destX+sourX)-img.getWidth()+sizeX, destY-sourY, Graphics.LEFT|Graphics.TOP, 0x2000);	// Flip X
	return;

	case 2:
		dscr.drawImage(img, destX-sourX, (destY+sourY)-img.getHeight()+sizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
		dscr.drawImage(img, (destX+sourX)-img.getWidth()+sizeX, (destY+sourY)-img.getHeight()+sizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
	return;
	}
}
//#else
//#endif

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
	//#ifdef com.mygdx.mongojocs.toro2.Debug
		Debug.println("loadImage: "+FileName+" <File not found>");
		return null;
	//#endif
	}

//#ifdef com.mygdx.mongojocs.toro2.Debug
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
	textDraw(textBreak(Str, printerSizeX, magicBiosGetFont(Mode) ), X, Y, RGB, Mode);
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
	Font f = magicBiosGetFont(Mode);
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
		scr.drawString(Str[i],  X-1+despX, Y+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y+despY , 20);
		scr.drawString(Str[i],  X+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X+despX, Y+1+despY , 20);

		scr.drawString(Str[i],  X-1+despX, Y-1+despY , 20);
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


	menuListDraw();

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
				//InputStream is = getClass().getResourceAsStream(Nombre);
				p = Manager.createPlayer( Nombre , "audio/midi");
				p.realize();
				p.prefetch();
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
					soundTracks[Ary].setLoopCount(Loop);
					VolumeControl vc = (VolumeControl) soundTracks[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(100);}
					soundTracks[Ary].start();
				}
				catch(Exception e)
				{
				//#ifdef com.mygdx.mongojocs.toro2.Debug
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
				//#ifdef com.mygdx.mongojocs.toro2.Debug
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
			if (soundForceRestart >= 0)
			{
				try
				{
					soundTracks[soundForceRestart].start();
				}
				catch(Exception e)
				{
				//#ifdef com.mygdx.mongojocs.toro2.Debug
					Debug.println(e.toString());
				//#endif
					return;
				}
			}

			soundForceRestart = -1;
		}
		
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
	//#elifdef PLAYER_MIDP20_TICK
	//#elifdef PLAYER_MOTC450
	//#elifdef PLAYER_SIEMENS_MIDI
	//#elifdef PLAYER_TSM6
	//#elifdef PLAYER_SAMSUNG
	//#elifdef PLAYER_MO-E1000
	//#elifdef PLAYER_SHARP
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
int listenerIdLeftOld = -1;
int listenerIdRightOld = -1;

int listenerDown = -1;
int listenerYadd;

//#ifdef J2ME
//#ifdef FULLSCREEN

//#ifdef S30
//#elifdef S40
//#elifdef S60
static final int listenerAddY = 1;
static final int listenerHeight = 8;
//#elifdef S80
//#endif

Image listenerImg;
byte[] listenerCor;

public void listenerInit(int idLeft, int idRight)
{
//#ifdef LISTENER_DOWN
	/*if (listenerIdLeft != idLeft || listenerIdRight != idRight)
	{
		listenerDown = (idLeft==0 && idRight==0)? 0:70;
		listenerYadd = listenerHeight;
	}

	listenerIdLeftOld = listenerIdLeft;
	listenerIdRightOld = listenerIdRight;*/

//#endif

	listenerIdLeft = idLeft;
	listenerIdRight = idRight;
}

public void listenerInit(String strLeft, String strRight) {}

public void listenerDraw()
{
//#ifdef LISTENER_DOWN
	/*if (listenerDown < 0) {return;}

	if (listenerDown-- == 0)
	{
		gameForceRefresh = true;
		return;
	}*/
//#endif

	if (listenerImg != null)
	{
		int listenerY = canvasHeight;

//#ifdef LISTENER_DOWN
		/*if (--listenerYadd < 0) {listenerYadd=0;}
		listenerY += listenerYadd;
		listenerY -= listenerHeight;*/
//#endif

//		alphaFillDraw(0x88000000, 0, listenerY, canvasWidth, 6);	// Para hacerlo semitransparente

		putColor(0);
		scr.setClip(0, listenerY, canvasWidth, listenerHeight);
//#ifdef LISTENER_DOWN
		/*if (listenerIdLeft != 0)
		{
			scr.fillRect(0, listenerY, listenerCor[(listenerIdLeft*6)+2]+2, listenerHeight);
		}

		if (listenerIdRight != 0)
		{
			scr.fillRect(canvasWidth - listenerCor[(listenerIdRight*6)+2]-2, listenerY, listenerCor[(listenerIdRight*6)+2]+2, listenerHeight);
		}*/
//#else
		scr.fillRect(0, listenerY, canvasWidth, listenerHeight);
//#endif

		listenerLabelDraw(listenerImg,  1, (listenerY+listenerAddY),  listenerIdLeft, listenerCor);
		listenerLabelDraw(listenerImg,  canvasWidth - listenerCor[(listenerIdRight*6)+2]-1, (listenerY+listenerAddY),  listenerIdRight, listenerCor);
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
// ihd - Engine - ihd (iMode Hard Disk) File System v1.0 - Rev.4 (22.5.2005)
// ----------------
// Dise�ado y Programado por Juan Antonio Gomez Galvez.
// 2005 (c) Juan Antonio Gomez Galvez
// Version autorizada a Microjocs Mobile S.L.
// ================
// ****************

//#ifdef DOJA

//#endif







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
static final int MENU_ASK = 4;
static final int MENU_FINAL = 5;
static final int IN_GAME_HELP = 6;
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
static final int MENU_ACTION_RESTART_OK = 7;
static final int MENU_ACTION_SOUND_CHG = 8;
static final int MENU_ACTION_VIBRA_CHG = 9;
static final int MENU_ACTION_REPLAY = 10;
static final int MENU_ACTION_REPLAY_OK = 11;
static final int MENU_ACTION_ASK = 12;
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
		if (gameLevelLast == 1)
		{
 			menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY);
		} else {
	 		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_PLAY);
		}
	//#ifndef PLAYER_NONE
		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
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
		menuListAdd(0, gameText[TEXT_WAIT]);
		menuListSet_Screen();

		gameDraw();

		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Screen();

		listenerInit(SOFTKEY_MENU, SOFTKEY_CONTINUE);
	break;


	case MENU_SCROLL_ABOUT:

		menuListClear();
		menuListAdd(0, gameText[TEXT_WAIT]);
		menuListSet_Screen();

		gameDraw();

	//#ifdef J2ME
		String version = ga.getAppProperty("MIDlet-Version");
	//#elifdef DOJA
	//#endif

		gameText[TEXT_ABOUT_SCROLL][2] = (version != null? version:" ");

		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Screen();

		listenerInit(SOFTKEY_MENU, SOFTKEY_CONTINUE);
	break;


	case MENU_FINAL:

		menuListClear();
		menuListAdd(0, gameText[TEXT_END_GAME]);
		menuListSet_Screen();

		menuTypeBack = MENU_MAIN;

		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
	break;


	case MENU_ASK:

		menuListClear();
		menuListAdd(0, gameText[TEXT_ARE_YOU_SURE][0], MENU_ACTION_ASK);
		menuListSet_Option();

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
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
		case MENU_ASK:

			menuRelease();
	
			menuInit(menuTypeBack, menuLastPos);
		break;
		}
	break;


	case -3: // com.mygdx.mongojocs.toro2.Scroll o Screen ha sido cortado por usuario
	case -2: // com.mygdx.mongojocs.toro2.Scroll o Screen ha llegado al final

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


	case MENU_ACTION_REPLAY:	// Jugar de 0 (Pregunta)

		menuRelease();

		menuActionAsk = MENU_ACTION_REPLAY_OK;
		menuInit(MENU_ASK);
	break;


	case MENU_ACTION_REPLAY_OK:	// Jugar de 0 (Respuesta ok)

		menuRelease();

		gameLevelLast = 1;
		gameScore = 0;
		savePrefs();

		menuExit = true;
	break;


	case MENU_ACTION_PLAY:		// Jugar deste ultima partida

		menuRelease();

		if (gameLevelLast == 1) {gameScore = 0;}

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


	case MENU_ACTION_RESTART:	// Provocamos GAME OVER desde menu (preguntamos)

		menuRelease();

		menuActionAsk = MENU_ACTION_RESTART_OK;
		menuInit(MENU_ASK);
	break;


	case MENU_ACTION_RESTART_OK:	// Provocamos GAME OVER desde menu (Accion)

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
		if (!gameSound) {soundStop();}
		else
		if (menuType == MENU_MAIN) {soundPlay(0,0);}
		else
//#ifndef VI-TSM30i
		if (menuType == MENU_SECOND && soundLoop == 0) {soundPlay(1,0);}
//#endif
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListOpt() != 0;
		if (gameVibra) {vibraInit(300);}
	break;


	case MENU_ACTION_ASK:

		menuAction(menuActionAsk);
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
final static int TEXT_SELECT_PLAYER = 16;
final static int TEXT_PLAYER_NAMES = 17;
final static int TEXT_LEVEL_COMPLETED = 18;
final static int TEXT_TORO_TE_A_COGIDO = 19;
final static int TEXT_ARE_YOU_SURE = 20;
final static int TEXT_WAIT = 21;
final static int TEXT_END_GAME = 22;
final static int TEXT_LEVEL_REVIEW = 23;
final static int TEXT_NEWGAME = 24;
final static int TEXT_INGAME_HELP_RUEDA = 25;
final static int TEXT_INGAME_HELP_BIDON = 26;
final static int TEXT_INGAME_HELP_CAJA = 27;
final static int TEXT_INGAME_HELP_BARRERA = 28;
final static int TEXT_INGAME_HELP_ROSA = 29;
final static int TEXT_INGAME_HELP_MANZANA = 30;
final static int TEXT_INGAME_HELP_CAJA_AZUL = 31;
final static int TEXT_INGAME_HELP_ITEM_SPECIAL = 32;
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

final static int GAME_PLAYER_SELECT = 40;
// ===============================================

int gameAlphaExit;

boolean gameShowEnd = false;

boolean gamePlayGoMenu = false;

int gameScore;
int gameHiScore;

boolean gameCompleted;

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
			"/toro_title.mid",			// 0 - Musica de la caratula
			"/toro_ingame.mid",			// 1 - Musica durante el juego
			"/toro_level_completed.mid",// 2 - Musica de nivel completado
			"/toro_life_lost.mid",		// 3 - Sonido de "cogido por el toro"
//			"/toro_speedup_item.mid",	// 4 - Sonido de item (aumento velocidad)
//			"/toro_bonus_item.mid",		// 5 - Sonido de item (ganar puntos)
			});

	//#endif


	//#ifdef SG-D410
	//#endif

//#endif
// ================================



//#ifdef J2ME
	//#ifdef FULLSCREEN
		listenerImg = loadImage("/softkeys");
		listenerCor = loadFile("/softkeys.cor");
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

		if (soundOld != 0) {soundPlay(0,0);}

		gameStatus = GAME_MENU_MAIN_END;
	break;

	case GAME_MENU_MAIN_END:

		menuImg = null;

		if (gameLevelLast == 1)
		{
			gameStatus = GAME_PLAYER_SELECT;
		} else {
			soundStop();
			gameStatus = GAME_PLAY;
		}
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
// ===============================






// -------------------------------
// Fade al salir del juego
// ===============================
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
// Seleccion de personajes
// ===============================
	case GAME_PLAYER_SELECT:

		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, TEXT_VCENTER|TEXT_HCENTER);
		gameDraw();

		playerSelectCreate();
		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		gameStatus++;

	case GAME_PLAYER_SELECT+1:

		if ( playerSelectTick() )
		{
			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

			playerSelectDestroy();
	
			menuListSet_None();

			canvasFillTask(0);
			gameDraw();

			if (keyMenu > 0)
			{
				soundStop();
				gameStatus = GAME_PLAY;
			} else {
				canvasFillTask(0);
				canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, TEXT_VCENTER|TEXT_HCENTER);
				gameDraw();

				gameStatus = GAME_MENU_START;
			}
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

		faseStr = gameText[TEXT_LEVEL][0]+" "+faseLevel;
		playShow = true;
		gameDraw();

		waitTime(3000);

		faseStr = null;

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);

//#ifndef VI-TSM30i
		soundPlay(1,0);
//#endif

		gameStatus++;
	break;

	case GAME_PLAY_TICK:

	//#ifdef CHEATS
	//#endif

		if (gamePlayGoMenu || (keyMenu == -1 && lastKeyMenu == 0)) {gamePlayGoMenu = false; gameStatus = GAME_MENU_SECOND; break;}

		if ( !playTick() ) {break;}

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		soundStop();

		playRelease();

		gameStatus--;

		switch (playExit)
		{
		case 1:	// Nivel Completado, pasamos al siguiente nivel

			if (++faseLevel == 9)
			{
				gameShowEnd = true;
				gameCompleted = true;

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
//	case GAME_PLAY_TICK:		// GAME_PLAY_TICK => Estado del juego cuando esta en funcionamiento.
//	break;

	case GAME_PLAYER_SELECT+1:
		playerSelectShow = true;
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

	playerSelectDraw();

	if (gameStatus == GAME_EXIT+1) {alphaFillDraw(0x44000000, 0,0, canvasWidth, canvasHeight);}
/*
	if (biosStatus == BIOS_PAUSE)
	{
		canvasTextCreate(0,0, canvasWidth, canvasHeight);
		textDraw("Game Paused", 0,0, 0xffffff, TEXT_HCENTER|TEXT_VCENTER|TEXT_OUTLINE);
	}
*/
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

byte[] faseMap;
byte[] faseCol;
byte[] faseCom;
Image tilesAImg;
Image tilesBImg;

// -------------------
// play Create
// ===================

public void playCreate()
{
	sc = new Scroll();
//#ifdef DOJA
//#endif

	faseCreate();
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	faseDestroy();
}

// -------------------
// play Init
// ===================

public void playInit()
{
	AniSprINI();
	toroINI();
	cerbezaINI();

	faseInit();
}

// -------------------
// play Release
// ===================

public void playRelease()
{
	cerbezaEND();
	toroEND();
	AniSprEND();

	faseRelease();

	System.gc();
}

// -------------------
// play Tick
// ===================

public boolean playTick()
{
	AniSprRUN();
	toroRUN();
	cerbezaRUN();


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

//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.spdStart(2);
//#endif

	if (protMode != PROT_MUERTE)
	{
		sc.ScrollRUN_Centro_Max(protX+(protWidth/2), protY+(protHeight/2));
	}

//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.spdStop(2);
	Debug.spdStart(3);
//#endif

	sc.ScrollIMP(scr);

//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.spdStop(3);
	Debug.spdStart(4);
//#endif

//#ifdef DOJA
//#endif

//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.spdStop(4);
	Debug.spdStart(5);
//#endif

	AniSprIMP(cerbezas, cerbezaP, cerbezaC);
	AniSprIMP(AniSprs, AniSprP, AniSprC);
	AniSprIMP(toros, toroP, toroC);

//#ifdef com.mygdx.mongojocs.toro2.Debug
	Debug.spdStop(5);
//#endif

	panelDraw();


	if (faseStr != null)
	{
		canvasTextCreate(0,0, scrollWidth, scrollHeight);
	//#ifdef J2ME
		scr.setClip(0,0, scrollWidth, scrollHeight);
	//#endif
		textDraw(faseStr, 0,0, 0xffffff, TEXT_VCENTER|TEXT_HCENTER|TEXT_OUTLINE);
	}



/*
	scr.setColor(rnd(0x1000000));
	scr.setClip(0,0, canvasWidth, canvasHeight);
	scr.fillRect(protX + protCuX - sc.ScrollX, protY + protCuY - sc.ScrollY, protCuWidth,2);
*/

/*
	scr.setColor(rnd(0x1000000));
	scr.setClip(0,0, canvasWidth, canvasHeight);
	scr.fillRect(puntoX - sc.ScrollX, puntoY-sc.ScrollY, protCuWidth,2);
*/

/*
	canvasTextCreate(0,0, canvasWidth, canvasHeight);
	scr.setClip(0,0, canvasWidth, canvasHeight);
	if (toros != null && toros.length > 0)
	{
		textDraw("Prot:"+protSpdOptima+" Toro:"+toros[0].toroSpd, 2, 2, 0xffffff, TEXT_OUTLINE);
	}
*/

/*
	scr.setClip(0,0, canvasWidth, canvasHeight);
	scr.setColor(0xff0000);
	scr.drawRect(protX+protCuX-sc.ScrollX, protY+protCuY-sc.ScrollY, protCuWidth, protCuHeight);

	for (int i=0 ; i<toroC ; i++)
	{
		int adr = toroP[i];
		scr.setClip(0,0, canvasWidth, canvasHeight);
		scr.setColor(0x0000ff);
		scr.drawRect(toros[adr].CoorX+toroCuX-sc.ScrollX, toros[adr].CoorY+toroCuY-sc.ScrollY, toroCuWidth, toroCuHeight);
		scr.setColor(0xffffff);
		scr.drawRect(toros[adr].CoorX+toroAstaX-sc.ScrollX, toros[adr].CoorY+toroAstaY-sc.ScrollY, toroAstaWidth, toroAstaHeight);
	}
*/


}


//#ifdef DOJA
//#endif

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// fase - Engine
// ===================
// *******************

//#ifdef S30
//#elifdef S40
//#elifdef S60
	static final int tileWidth = 16;
	static final int tileHeight = 16;
//#elifdef S80
//#endif

int faseX, faseY;
int faseSpeed = 4;

int faseWidth;
int faseHeight;

byte[] toroCor;
byte[] panelFontCor;
byte[] particulesCor;
byte[] objetosCor;

//#ifdef J2ME
Image toroImg;
Image panelFontImg;
Image particulesImg;
Image objetosImg;
//#elifdef DOJA
//#endif

Image panelImg;

String faseStr;

int faseLevel;

int scrollWidth;
int scrollHeight;

int faseRosas;

// -------------------
// fase Create
// ===================

public void faseCreate()
{
	toroCor = loadFile("/toro.cor");
	objetosCor = loadFile("/objetos.cor");
	particulesCor = loadFile("/particules.cor");
	panelFontCor = loadFile("/panelFont.cor");

//#ifdef J2ME
	toroImg = loadImage("/toro");
	objetosImg = loadImage("/objetos");
	particulesImg = loadImage("/particules");
	panelFontImg = loadImage("/panelFont");
//#elifdef DOJA
//#endif

//#ifdef J2ME
	tilesAImg = loadImage("/tilesA");
	tilesBImg = loadImage("/tilesB");
//#endif

	panelImg = loadImage("/panel");

	faseCom = loadFile("/nivel.btsc");

	faseLevel = gameLevelLast;

	scrollWidth = canvasWidth - panelNetWidth;
	scrollHeight = canvasHeight;

	sc.ScrollINI(scrollWidth, scrollHeight);

	ingameHelpCreate();

	protCreate();
}

// -------------------
// fase Destroy
// ===================

public void faseDestroy()
{
	toroCor = null;
	toroImg = null;

//#ifdef J2ME
	tilesAImg = null;
	tilesBImg = null;
//#endif

	objetosCor = null;
	objetosImg = null;

	particulesCor = null;
	particulesImg = null;

	faseCom = null;

	protDestroy();

	sc.ScrollEND();
	sc = null;
}

// -------------------
// fase Init
// ===================

public void faseInit()
{
	faseMap = loadFile("/nivel"+faseLevel+".mmp");
	faseWidth = faseMap[0] & 0xff;
	faseHeight = faseMap[1] & 0xff;
	System.arraycopy(faseMap, 2, faseMap, 0, faseWidth*faseHeight);

	faseCol = loadFile("/nivel"+faseLevel+".mcl");
	System.arraycopy(faseCol, 2, faseCol, 0, faseWidth*faseHeight);

	protInit();

	faseRosas = 0;

	int centro = (scrollWidth-toroWidth)>>1;


	for (int i=0 ; i<faseCol.length ; i++)
	{
		if (faseCol[i] == 0x0D) {faseCol[i-faseWidth] = 0x2D;}
		if (faseCol[i] == 0x0E) {faseCol[i-faseWidth] = 0x2E;}
	}


	toroSET(centro, (faseHeight*tileHeight) - (toroHeight), toroSpdMAX);
	toroSET(centro-(toroCuWidth+tileWidth), (faseHeight*tileHeight) - (toroHeight>>1), toroSpdMAX);
	toroSET(centro+(toroCuWidth+tileWidth), (faseHeight*tileHeight) - (toroHeight>>1), toroSpdMAX);

//#ifdef J2ME
	sc.objetosImg = objetosImg;
	sc.objetosCor = objetosCor;
	sc.ScrollSET(faseMap, faseCol, faseCom, faseWidth, faseHeight, tilesAImg, tilesBImg, 8);
//#elifdef DOJA
//#endif
}

// -------------------
// fase Release
// ===================

public void faseRelease()
{
	faseMap = null;
	faseCol = null;

//#ifdef J2ME
	sc.objetosImg = null;
	sc.objetosCor = null;
//#endif

//#ifdef DOJA
//#endif
		sc.ScrollRES();
//#ifdef DOJA
//#endif
}

// -------------------
// fase Tick
// ===================

public void faseTick()
{
	ingameHelpTick();
}


// -------------------
// fase Check Tiles
// ===================

public void faseCheckTiles(int x, int y, boolean prot, boolean restas)
{
	int tile = checkTile(x,y);

// Reaccion sobre el mapa al pisar un Item:
	switch (tile)
	{
	case 0:	// Rueda
		faseCol[checkTileDir] = 0;
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);
		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_RUEDA);
	break;

	case 1:	// Bidon
		faseCol[checkTileDir-faseWidth] = 0;
		faseCol[checkTileDir] = (12+8);	// ponemos el bidon chafado
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);
	break;

	case 2:	// Caja
		faseCol[checkTileDir-faseWidth] = 0;
		faseCol[checkTileDir] = 0;
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);

		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_MADERA1);
		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_MADERA2);
	break;

	case 3:	// Barrera
		faseCol[checkTileDir] = 0;
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);

		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_MADERA1);
		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_MADERA2);
	break;
/*
	case 4:	// Rosa
	break;

	case 5:	// Manzana
	break;

	case 6:	// Caja Azul
	break;

	case 7:	// Item Special
	break;
*/
	}

	if (!prot) {return;}

// Reaccion sobre el jugador al pisar un Item de puntos
	switch (tile)
	{
	case 0:	// Rueda
	case 1:	// Bidon
	case 2:	// Caja
	case 3:	// Barrera
		ingameHelpInit(tile);
	break;

	case 4:	// Rosa
		if (++faseRosas > 999) {faseRosas = 999;}

		ingameHelpInit(tile);

		faseCol[checkTileDir] = 0;
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);
		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_PARTICULAS);
	break;

	case 5:	// Manzana
		protSpdOptima = protSpdMAX;

		ingameHelpInit(tile);

		faseCol[checkTileDir] = 0;
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);
		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_PARTICULAS);
	break;

	case 6:	// Caja Azul
		if ( (faseRosas+=10) > 999) {faseRosas = 999;}

		ingameHelpInit(tile);

		faseCol[checkTileDir] = 0;
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);
		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_PARTICULAS);
	break;

	case 7:	// Item Special
		protItem = true;

		ingameHelpInit(tile);

		faseCol[checkTileDir] = 0;
		sc.ScrollUpdate(x/tileWidth, y/tileHeight);
		cerbezaSET((checkTileDir%faseWidth)*tileWidth, (checkTileDir/faseWidth)*tileHeight, SUELO_PARTICULAS);
	break;
	}

	if (!restas) {return;}

// Reaccion sobre el jugador al pisar un Item que resta velocidad
	switch (tile)
	{
	case 0:	// Rueda
		protSpdActual += protSpdRueda; if (protSpdActual < 0) {protSpdActual = 0;}
	break;

	case 1:	// Bidon
		protSpdActual += protSpdBidon; if (protSpdActual < 0) {protSpdActual = 0;}
	break;

	case 2:	// Caja
		protSpdActual += protSpdCaja; if (protSpdActual < 0) {protSpdActual = 0;}
	break;

	case 3:	// Barrera
		protSpdActual += protSpdBarrera; if (protSpdActual < 0) {protSpdActual = 0;}
	break;
	}
}



// -------------------
// check Pixel
// ===================

int pixelAdjust;

public boolean checkPixel(int x, int y)
{
	pixelAdjust = 0;

	int dir = ((y/tileHeight) * faseWidth) + (x/tileWidth);

	if (dir < 0 || dir >= faseCol.length) {return true;}

	int baseX, baseY;

	switch (faseCol[dir])
	{
	case 1:
	return false;

	case 2:
		baseX = x%tileWidth;
		pixelAdjust = (-baseX)+(tileWidth>>1);
		if ( (baseX) < (tileWidth>>1) ) {return false;}
	break;

	case 3:
		baseX = x%tileWidth;
		pixelAdjust = (-baseX)+(tileWidth>>1)-1;
		if ( (baseX) >= (tileWidth>>1) ) {return false;}
	break;

	case 8:
		baseX = x%tileWidth;
		baseY = y%tileHeight;
		pixelAdjust = (-baseX)+(baseY>>1);
		if ( (baseX) > (baseY>>1) ) {return false;}
	break;

	case 10:
		baseX = x%tileWidth;
		baseY = y%tileHeight;
		pixelAdjust = (-baseX+(tileWidth>>1))+(baseY>>1);
		if ( (baseX-(tileWidth>>1)) > (baseY>>1) ) {return false;}
	break;

	case 9:
		baseX = x%tileWidth;
		baseY = y%tileHeight;
		pixelAdjust = (-baseX)+(tileHeight)-(baseY>>1);
		if ( (baseX) < (tileHeight)-(baseY>>1) ) {return false;}
	break;

	case 11:
		baseX = x%tileWidth;
		baseY = y%tileHeight;
		pixelAdjust = (-baseX)+(tileHeight>>1)-(baseY>>1);
		if ( (baseX) < (tileHeight>>1)-(baseY>>1) ) {return false;}
	break;
	}

	return true;
}

// -------------------
// check Line
// ===================

int lineAdjust;
int lineSide;

public boolean checkLine(int x, int y, int width, int desp)
{
	x += desp;

	lineSide = lineAdjust = 0;

//	if (desp == 0)
//	{
	// Chequeamos pixel interior
		if ( checkPixel(x, y) )
		{

			for (int i=1 ; i<width ; i++)
			{
				if ( !checkPixel(x+i, y) )
				{
					i--;
					if ( checkPixel(x-width+i, y) )
					{
						lineAdjust = desp-width+i;
						lineSide = -1;
						return false;
					} else {
						lineAdjust = 0;
						return false;
					}
				}
			}

			return true;

		} else 
	// Chequeamos pixel exterior
		if ( checkPixel(x+width-1, y) )
		{
			for (int i=width-2 ; i>=0 ; i--)
			{
				if ( !checkPixel(x+i, y) )
				{
					i++;
					if ( checkPixel(x+width+i, y) )
					{
						lineAdjust = desp+i;
						lineSide = 1;
						return false;
					} else {
						lineAdjust = 0;
						return false;
					}
				}
			}

			return true;
		}

		return false;
}






int checkTileDir;

public int checkTile(int x, int y)
{
	checkTileDir = ((y/tileHeight) * faseWidth) + (x/tileWidth);

	if (checkTileDir < 0 || checkTileDir >= faseCol.length) {return -1;}

	return faseCol[checkTileDir] - 12;
}



// <=- <=- <=- <=- <=-








// *******************
// -------------------
// prot - Engine
// ===================
// *******************

//#ifdef S30
//#elifdef S40
//#elifdef S60
	static final int protWidth = 32;
	static final int protHeight = 32;
	static final int protCuX = 10;
	static final int protCuY = 24;
	static final int protCuWidth = 12;
	static final int protCuHeight = 8;
	
	static final int protCoheteAdjX = -2;
	static final int protCoheteAdjY = -15;

// Valores para control de velocidad
	static final int protSpdMuerte = 2560;
	
	static final int protDespX = 4;
	
	static final int protSpdMAX = 1300;
	static final int protSpdCohete = 1600;
	static final int protSpdInercia = 32;
	static final int protSpdCansancio = -2;

//#elifdef S80
//#endif




static final int protSpdPared = -2 * protSpdInercia;
static final int protSpdRueda = -10 * protSpdInercia;
static final int protSpdBidon = -10 * protSpdInercia;
static final int protSpdCaja = -10 * protSpdInercia;
static final int protSpdBarrera = -10 * protSpdInercia;



int protX;
int protY;
int protSuperX;
int protSuperY;
int protSide;
int protMode;
int protAnim;


static final int PROT_CORRER = 0;
static final int PROT_SPECIAL = 1;
static final int PROT_MUERTE = 2;
static final int PROT_COMPLET = 3;

int protSpdOptima;
int protSpdActual;

byte[] spritesCor;

//#ifdef J2ME
Image spritesImg;
//#elifdef DOJA
//#endif

int protSelect;

int protFaseCnt;

boolean protItem;

static final int PROT_VASCO = 0;
static final int PROT_ALEMAN = 1;
static final int PROT_SUMO = 2;
static final int PROT_TORO = 3;

// -------------------
// prot Create
// ===================

public void protCreate()
{

	protSelect = playerSelected;

	switch (protSelect)
	{
	case PROT_VASCO:
		spritesCor = loadFile("/vasco.cor");
	//#ifdef J2ME
		spritesImg = loadImage("/vasco");
	//#elifdef DOJA
	//#endif
	break;

	case PROT_SUMO:
		spritesCor = loadFile("/sumo.cor");
	//#ifdef J2ME
		spritesImg = loadImage("/sumo");
	//#elifdef DOJA
	//#endif
	break;

	case PROT_ALEMAN:
		spritesCor = loadFile("/aleman.cor");
	//#ifdef J2ME
		spritesImg = loadImage("/aleman");
	//#elifdef DOJA
	//#endif
	break;

	case PROT_TORO:
		spritesCor = loadFile("/hombretoro.cor");
	//#ifdef J2ME
		spritesImg = loadImage("/hombretoro");
	//#elifdef DOJA
	//#endif
	break;
	}
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

//	protSuperX = (10)<<8;
	protSuperX = ((scrollWidth-protWidth)/2)<<8;

	protSuperY = ((faseHeight*tileHeight)-(5*tileHeight)-protHeight)<<8;
//	protSuperY = ((144*tileHeight)-(canvasHeight -protHeight)/2)<<8;


	protX = protSuperX>>8;
	protY = protSuperY>>8;

	protSpdActual = 0;
	protSpdOptima = protSpdMAX - protSpdInercia;

	protAnim = AniSprSET(-1, -protWidth, 0, 0, 1, 1, 0x002);

	protFaseCnt = protY / tileHeight;

	protItem = false;

	protAnimExtA = -1;
	protAnimExtB = -1;

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

	if (protY+protHeight < 0 && protMode != PROT_MUERTE)
	{
		protCompletInit();
	}


	switch (protMode)
	{
	case PROT_CORRER:	protCorrerTick();	break;
	case PROT_SPECIAL:	protSpecialTick();	break;
	case PROT_MUERTE:	protMuerteTick();	break;
	case PROT_COMPLET:	protCompletTick();	break;
	}

	AniSprs[protAnim].CoorX = protX;
	AniSprs[protAnim].CoorY = protY;

}





// -------------------
// prot Correr Init
// ===================

public void protCorrerInit()
{
	AniSprSET(protAnim, protX, protY, 4, 4, 1, 0x001);
	protMode = PROT_CORRER;
}

// -------------------
// prot Correr Tick
// ===================

public void protCorrerTick()
{

	if (protSpdOptima < 0)
	{
		protMuerteInit(1);
		return;
	}

// ---------------------------
// Controlamos movimiento prot horizontal
// ===========================

	int movX = 0;

// Desplazar horizonalmente?
	if (keyX != 0)
	{
	// Comprobamos colision de linea:
		if ( checkLine(protX + protCuX, protY + protCuY, protCuWidth, keyX*protDespX) )
		{
		// Check LIBRE: aplicamos desplazamiento
			protX += keyX*protDespX;
			protSuperX = protSuperX<<8;

		// El prot debe mirar hacia el lado correcto (protSide)
			movX = keyX;
		} else {
		// Check COLISION
			if (lineAdjust != 0)
			{
			// Si hay posibilidad de ajuste, lo aplicamos.
				protX += lineAdjust;
				protSuperX = protSuperX<<8;

			// El prot debe mirar hacia el lado del ajuste (protSide)
//				movX = lineSide;
			}
		}
	}
// ===========================



// ---------------------------
// Avanzamos prot verticalmente
// ===========================

// Aplicamos cansancio por cada tile avanzado
	if (protFaseCnt != protY / tileHeight)
	{
		protFaseCnt = protY / tileHeight;
		protSpdOptima += protSpdCansancio;
	}

	if (protSpdActual+protSpdInercia > protSpdOptima && protSpdActual-protSpdInercia < protSpdOptima )
	{
		protSpdActual = protSpdOptima;
	} else {
		protSpdActual += (protSpdActual < protSpdOptima? protSpdInercia:-protSpdInercia);
	}

//#ifdef CHEATS
//#endif
		protSuperY -= protSpdActual;
		protY = protSuperY>>8;
//#ifdef CHEATS
//#endif

// Comprobamos colision de linea:
	if (!checkLine(protX+protCuX, protY+protCuY, protCuWidth, 0))
	{
	// Check COLISION
		if (lineAdjust == 0)
		{
		// Colision, dejamos coordenadas como estaban
			protSuperY += protSpdActual;
			protY = protSuperY>>8;

		// al colisionar, debemos poner la inercia a cero.
			protSpdActual = 0;
		} else {
		// Si hay posibilidad de ajuste, lo aplicamos.
			protX += lineAdjust;
			protSuperX = protX<<8;

		// El prot debe mirar hacia el lado del ajuste (protSide)
			movX = lineSide;
		}
	}
// ===========================



	AniSprs[protAnim].FrameIni = 4+(movX*4);



	faseCheckTiles(protX+(protWidth/2), protY+(protHeight)-4, true, true);



	if ( toroAstaColision(protX+(protWidth/2)-2, protY+protHeight-5, 4, 4) )
	{
		toros[toroAstaColisionAnim].enviste = true;
		protMuerteInit(0);
		return;
	}



	if ( toroCuerpoColision(protX+(protWidth/2)-2, protY+protHeight-5, 4, 4) )
	{
		protMuerteInit(1);
		return;
	}



	if (protItem && keyMenu > 0 && lastKeyMenu == 0)
	{
		protItem = false;

		protSpecialInit();
		return;
	}

}


// -------------------
// prot Special Init
// ===================

public void protSpecialInit()
{
	switch (protSelect)
	{
	case PROT_VASCO:
		AniSprSET(protAnim, protX, protY, 12, 1, 1, 0x002);
	break;

	case PROT_SUMO:
		AniSprSET(protAnim, protX, protY, 12, 7, 1, 0x002);
	break;

	case PROT_ALEMAN:
		AniSprSET(protAnim, protX, protY, 12, 4, 1, 0x002);
	break;

	case PROT_TORO:
		AniSprSET(protAnim, protX, protY, 12, 5, 0, 0x002);
	break;
	}

	protMode = PROT_SPECIAL;
	protModeExt = 0;
}

// -------------------
// prot Special Tick
// ===================

int protModeExt;
int protTime;
int protAnimExtA;
int protAnimExtB;

public void protSpecialTick()
{

	if ( toroCuerpoColision(protX+(protWidth/2)-2, protY+5, 4, protHeight-10) )
	{
		if (protAnimExtA != -1) {AniSprs[protAnimExtA].Mode = 0;}
		if (protAnimExtB != -1) {AniSprs[protAnimExtB].Mode = 0;}

		protAnimExtA = -1;
		protAnimExtB = -1;

		protMuerteInit(1);
		return;
	}


	faseCheckTiles(protX+(protWidth/2), protY+(protHeight)-4, true, false);


	protFaseCnt = protY / tileHeight;


	switch (protSelect)
	{
	case PROT_VASCO:
	// ---------------------------
	// Avanzamos prot verticalmente
	// ===========================
	
		protSuperY -= protSpdCohete;
		protY = protSuperY>>8;
	
	// Comprobamos colision de linea:
		if (!checkLine(protX+protCuX, protY+protCuY, protCuWidth, 0))
		{
		// Check COLISION
			if (lineAdjust == 0)
			{
			// Colision, dejamos coordenadas como estaban
				protSuperY += protSpdCohete;
				protY = protSuperY>>8;
	
			// al colisionar, debemos poner la inercia a cero.
				protSpdActual = 0;
			} else {
			// Si hay posibilidad de ajuste, lo aplicamos.
				protX += lineAdjust;
				protSuperX = protX<<8;
			}
		}
	// ===========================
	
		switch (protModeExt)
		{
		case 0:
			if (AniSprs[protAnim].Pause == 0)
			{
				AniSprSET(protAnim, protX, protY, 13, 2, 1, 0x001);
	
				protAnimExtA = AniSprSET(-1, protX+protCoheteAdjX, protY+protCoheteAdjY, 30, 1, 1, 0x201);
				protAnimExtB = AniSprSET(-1, protX+protCoheteAdjX, protY+protCoheteAdjY+tileHeight, 35, 1, 1, 0x201);
	
				protTime = 10;
				protModeExt++;
			}
		break;
	
		case 1:
			if (protAnimExtA >= 0)
			{
				AniSprs[protAnimExtA].CoorX = protX+protCoheteAdjX;
				AniSprs[protAnimExtA].CoorY = protY+protCoheteAdjY;
			}
			if (protAnimExtB >= 0)
			{
				AniSprs[protAnimExtB].CoorX = protX+protCoheteAdjX;
				AniSprs[protAnimExtB].CoorY = protY+protCoheteAdjY+tileHeight;
			}
	
			if (--protTime < 0)
			{
				AniSprSET(protAnim, protX, protY, 15, 6, 1, 0x002);
				protModeExt++;
	
				if (protAnimExtA >= 0)
				{
					AniSprs[protAnimExtA].Mode = 0;
					protAnimExtA = -1;
				}
				if (protAnimExtB >= 0)
				{
					AniSprs[protAnimExtB].Mode = 0;
					protAnimExtB = -1;
				}
			}
		break;
	
		case 2:
			if (AniSprs[protAnim].Pause == 0)
			{
				protCorrerInit();
			}
		break;
		}
	break;


	case PROT_SUMO:
	// ---------------------------
	// Avanzamos prot verticalmente
	// ===========================
	
		protSuperY -= protSpdCohete;
		protY = protSuperY>>8;
	
	// Comprobamos colision de linea:
		if (!checkLine(protX+protCuX, protY+protCuY, protCuWidth, 0))
		{
		// Check COLISION
			if (lineAdjust == 0)
			{
			// Colision, dejamos coordenadas como estaban
				protSuperY += protSpdCohete;
				protY = protSuperY>>8;
	
			// al colisionar, debemos poner la inercia a cero.
				protSpdActual = 0;
			} else {
			// Si hay posibilidad de ajuste, lo aplicamos.
				protX += lineAdjust;
				protSuperX = protX<<8;
			}
		}
	// ===========================
	
		switch (protModeExt)
		{
		case 0:
			if (AniSprs[protAnim].Pause == 0)
			{
				AniSprSET(protAnim, protX, protY, 13, 6, 1, 0x002);
				protModeExt++;
			}
		break;

		case 1:
			if (AniSprs[protAnim].Pause == 0)
			{
				protCorrerInit();
			}
		break;
		}
	break;

	case PROT_ALEMAN:

	// ---------------------------
	// Avanzamos prot verticalmente
	// ===========================
	
		protSuperY -= protSpdActual;
		protY = protSuperY>>8;
	
	// Comprobamos colision de linea:
		if (!checkLine(protX+protCuX, protY+protCuY, protCuWidth, 0))
		{
		// Check COLISION
			if (lineAdjust == 0)
			{
			// Colision, dejamos coordenadas como estaban
				protSuperY += protSpdCohete;
				protY = protSuperY>>8;
	
			// al colisionar, debemos poner la inercia a cero.
				protSpdActual = 0;
			} else {
			// Si hay posibilidad de ajuste, lo aplicamos.
				protX += lineAdjust;
				protSuperX = protX<<8;
			}
		}
	// ===========================

		switch (protModeExt)
		{
		case 0:
			if (AniSprs[protAnim].Pause == 0)
			{
				cerbezaSET(protX, protY, SUELO_CERBEZA);
				protCorrerInit();
			}
		break;
		}
	break;


	case PROT_TORO:

	// ---------------------------
	// Avanzamos prot verticalmente
	// ===========================
	
		protSuperY -= protSpdCohete;
		protY = protSuperY>>8;
	
	// Comprobamos colision de linea:
		if (!checkLine(protX+protCuX, protY+protCuY, protCuWidth, 0))
		{
		// Check COLISION
			if (lineAdjust == 0)
			{
			// Colision, dejamos coordenadas como estaban
				protSuperY += protSpdCohete;
				protY = protSuperY>>8;
	
			// al colisionar, debemos poner la inercia a cero.
				protSpdActual = 0;
			} else {
			// Si hay posibilidad de ajuste, lo aplicamos.
				protX += lineAdjust;
				protSuperX = protX<<8;
			}
		}
	// ===========================

		switch (protModeExt)
		{
		case 0:
			if (AniSprs[protAnim].Pause == 0)
			{
				AniSprSET(protAnim, protX, protY, 17, 3, 1, 0x007);
				AniSprs[protAnim].Repetir = 3;
				protModeExt++;
			}
		break;

		case 1:
			if (AniSprs[protAnim].Pause == 0)
			{
				AniSprSET(protAnim, protX, protY, 12, 5, 0, 0x012);
				protModeExt++;
			}
		break;

		case 2:
			if (AniSprs[protAnim].Pause == 0)
			{
				protCorrerInit();
			}
		break;
		}
	break;

	}



}


// -------------------
// prot Muerte Init
// ===================

int protMuerteType;
int protMuerteTime;

public void protMuerteInit(int type)
{
	if (protMode == PROT_MUERTE) {return;}

	soundPlay(3,1);
	vibraInit(250);

	protMuerteType = type;

	switch (protSelect)
	{
	case PROT_VASCO:
		switch (type)
		{
		case 0:
			AniSprSET(protAnim, protX, protY, 24, 7, 0, 0x002);
		break;

		case 1:
			AniSprSET(protAnim, protX, protY, 32, 1, 0, 0x002);
			protMuerteTime = 10;
		break;
		}
	break;

	case PROT_SUMO:
		switch (type)
		{
		case 0:
			AniSprSET(protAnim, protX, protY, 20, 3, 0, 0x001);
		break;
	
		case 1:
			AniSprSET(protAnim, protX, protY, 20, 1, 0, 0x002);
			protMuerteTime = 10;
		break;
		}
	break;

	case PROT_ALEMAN:
		switch (type)
		{
		case 0:
			AniSprSET(protAnim, protX, protY, 16, 3, 0, 0x001);
		break;
	
		case 1:
			AniSprSET(protAnim, protX, protY, 17, 1, 0, 0x002);
			protMuerteTime = 10;
		break;
		}
	break;

	case PROT_TORO:
		switch (type)
		{
		case 0:
			AniSprSET(protAnim, protX, protY, 20, 5, 0, 0x002);
		break;
	
		case 1:
			AniSprSET(protAnim, protX, protY, 20, 1, 0, 0x002);
			protMuerteTime = 10;
		break;
		}
	break;
	}

	protMode = PROT_MUERTE;
}

// -------------------
// prot Muerte Tick
// ===================

public void protMuerteTick()
{
	switch (protSelect)
	{
	case PROT_VASCO:
	case PROT_SUMO:
	case PROT_ALEMAN:
	case PROT_TORO:

		switch (protMuerteType)
		{
		case 0:
			protSuperY -= protSpdMuerte;
			protY = protSuperY>>8;
		
			if (protY+protHeight < sc.ScrollY-120)
			{
				protMuerteType = 50;
			}
		break;

		case 1:
			if (--protMuerteTime < 0)
			{
				protMuerteType = 50;
			}
		break;


	// Solo para sacar el texto final
		case 50:
			faseStr = gameText[TEXT_TORO_TE_A_COGIDO][0];
			protMuerteTime = 30;
			protMuerteType++;

		case 51:
			if (--protMuerteTime < 0)
			{
				playExit = 2;
			}
		break;

		}
	}
}


// -------------------
// prot Complet Init
// ===================

int protCompletTime;

public void protCompletInit()
{
	if (protMode != PROT_COMPLET)
	{
		faseStr = gameText[TEXT_LEVEL_COMPLETED][0];
	
		protCompletTime = 50;
	
		protModeExt = 0;

		soundPlay(2,1);

		protMode = PROT_COMPLET;
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
		if (--protCompletTime < 0)
		{
			faseStr = null;
			protModeExt++;
		}
	break;

	case 1:
		gameText[TEXT_LEVEL_REVIEW][1] = faseRosas+" x100";
		gameScore += (faseRosas*100);
		gameText[TEXT_LEVEL_REVIEW][4] = gameScore+"";
		if (gameHiScore < gameScore) {gameHiScore = gameScore;}
		gameText[TEXT_LEVEL_REVIEW][6] = gameHiScore+"";

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
// toro - Engine
// ====================
// ********************

//#ifdef S30
//#elifdef S40
//#elifdef S60
static final int toroWidth = 50;
static final int toroHeight = 50;

static final int toroCuX = 17;
static final int toroCuY = 28;
static final int toroCuWidth = 16;
static final int toroCuHeight = 22;

static final int toroAstaX = 11;
static final int toroAstaY =  12;
static final int toroAstaWidth = 28;
static final int toroAstaHeight = 4;

static final int toroDespX = 2;
static final int toroSpdMAX = 1300;
static final int toroSpdInercia = 32;

//#elifdef S80
//--
//#endif


// ---------------
// toro SET
// ===============

public int toroSET(int CoorX, int CoorY, int spd)
{
	if (toroC == toroMAX) {return(-1);}

	int i=toroP[toroC++];

	toros[i] = new toro();

	toros[i].SpriteSET(CoorX, CoorY,  0, 4, 1, 0x101);
	toros[i].FrameAct = rnd(4);
	toros[i].toroSpd = spd;
	toros[i].toroX = CoorX<<8;
	toros[i].toroY = CoorY<<8;

	return i;
}

// ---------------
// toro class
// ===============

public class toro extends AnimSprite
{
int Mode=0;

int toroX;
int toroY;
int toroSpd;
int toroSpdAct;

boolean enviste = false;

int sentido=0;

boolean toProt = false;

// ----------------
// toro Play
// ================

public boolean Play()
{

	faseCheckTiles(CoorX+(toroCuX),             CoorY+toroCuY, false, false);
	faseCheckTiles(CoorX+(toroCuX+toroCuWidth), CoorY+toroCuY, false, false);


	switch (Mode)
	{
	case 0:

	// ---------------------------
	// Si toro desaparece del nivel lo destruimos
	// ===========================
		if (CoorY+toroHeight < 0) {return true;}
	// ===========================
	
	
	// ---------------------------
	// Debemos envestir?
	// ===========================
		if (enviste)
		{
			enviste = false;

			if (FrameIni == 16) {SpriteSET(23, 1, 1, 0x102);}
			else
			if (FrameIni == 0)  {SpriteSET( 6, 1, 1, 0x102);}
			else
			if (FrameIni == 8)  {SpriteSET(15, 1, 1, 0x102);}

			toroSpdAct = toroSpd/2;
			Mode++;
			break;
		}
	// ===========================
	
	
	// ---------------------------
	// Si pisamos un charco de cerbeza, perdemos velocidad
	// ===========================
		if ( cerbezaColision(CoorX+toroCuX, CoorY+toroCuY, toroCuWidth, toroCuHeight) )
		{
			toroSpdAct = toroSpd / 2;
		}
	// ===========================
	
	
	// ---------------------------
	// Controlamos movimiento toro horizontal
	// ===========================
		int oldX = CoorX;

		int movX = 0;

		if (toProt)
		{
		// Avanzamos a por el prota
			if (CoorX+(toroWidth/2) > protX+protCuX+protCuWidth) {movX =-1;}
			else
			if (CoorX+(toroWidth/2) < protX+protCuX) {movX = 1;}
		} else {
		// Si no podemos avanzar nos movemos horizontalmente buscando una salida
			if (sentido != 0)
			{
				movX = sentido;
			}
		}

	
	// Desplazar horizonalmente?
		if (movX != 0)
		{
		// Comprobamos colision de linea:
			if ( checkLine(CoorX + toroCuX, CoorY + toroCuY, toroCuWidth, movX*toroDespX) )
			{
			// Check LIBRE: aplicamos desplazamiento
				CoorX += movX*toroDespX;
			} else {
			// Check COLISION
				if (lineAdjust != 0)
				{
				// Si hay posibilidad de ajuste, lo aplicamos.
					CoorX += lineAdjust;
				} else {
				// Buscamos salida x el lado opuesto al actual
					sentido = movX * -1;

				// Como NO podemos desplazarnos de X, el toro debe estar recto hacia arraba
					movX = 0;
				}
			}
		}

		if (torosColisions(this) )
		{
			CoorX = oldX;
			movX = 0;

			sentido *= -1;
		} else {
			toroX = CoorX<<8;
		}
	// ===========================
	
	
	// ---------------------------
	// Avanzamos toro verticalmente
	// ===========================

	// Incremento de la inercia
//		toroSpdOptima += toroSpdCansancio;
	
		if (toroSpdAct+toroSpdInercia > toroSpd && toroSpdAct-toroSpdInercia < toroSpd )
		{
			toroSpdAct = toroSpd;
		} else {
			toroSpdAct += (toroSpdAct < toroSpd? toroSpdInercia:-toroSpdInercia);
		}

		toroY -= toroSpdAct;
		CoorY = toroY>>8;
	
	// Comprobamos colision de linea:
		if (!checkLine(CoorX+toroCuX, CoorY+toroCuY, toroCuWidth, 0))
		{
		// Check COLISION
			if (lineAdjust == 0)
			{
			// Colision, dejamos coordenadas como estaban
				toroY += toroSpdAct;
				CoorY = toroY>>8;
	
			// al colisionar, debemos poner la inercia a cero.
				toroSpdAct = 0;

			// buscamos una salida horizontal
				if (sentido == 0) {sentido = 1;}

			} else {

			// Si hay posibilidad de ajuste, lo aplicamos.
				CoorX += lineAdjust;
				toroX = CoorX<<8;
	
			// Si tenemos ke ajustar el side x colision, que el toro tenga el side correcto
				movX = lineSide;

			// Controlamos colisiones con otros toros
				if (torosColisions(this) )
				{
					CoorX -= lineAdjust;
					toroX = CoorX<<8;

					movX = 0;
				}
			}

		} else {

		// Controlamos colisiones con otros toros
			if (torosColisions(this) )
			{
				toroY += toroSpdAct;
				CoorY = toroY>>8;

				movX = 0;
			} else {
			// Dado que podemos avanzar, seguimos recto
				sentido = 0;
			}
		}
		

	// ===========================

	
	// ---------------------------
	// Controlamos el side del toro
	// ===========================
		switch (movX)
		{
		case -1: if (FrameIni != 16) {SpriteSET(16, 7, 1, 0x101);} break;
		case 0: if (FrameIni != 0) {SpriteSET(0, 6, 1, 0x101);} break;
		case 1: if (FrameIni != 8) {SpriteSET(8, 7, 1, 0x101);} break;
		}
	// ===========================

	break;

	case 1:

		if (Pause == 0)
		{
			Mode = 0;
		}
	break;
	}


	return super.Play();
}

};


// ---------------
// toro INI
// ===============

final int toroMAX=6;
int toroC;
int[] toroP;
toro[] toros;

public void toroINI()
{
	toroC = 0;
	toroP = new int[toroMAX];
	toros = new toro[toroMAX];
	for (int i=0 ; i<toroMAX ; i++) {toroP[i]=i;}
}

// ---------------
// toro RES
// ===============

public void toroRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=toroP[i];
	toros[t] = null;
	toroP[i--] = toroP[--toroC];
	toroP[toroC] = t;
}

// ---------------
// toro END
// ===============

public void toroEND()
{
	toroP = null;
	toros = null;
	toroC = 0;
}

// ---------------
// toro RUN
// ===============

public void toroRUN()
{
// Buscamos el toro mas proximo al prot
	toro toroTemp = null;
	int lastToroY = (faseHeight*tileHeight)+200;
	for (int i=0 ; i<toroC ; i++)
	{
		int anim = toroP[i];
		if (protY < toros[anim].CoorY && lastToroY > toros[anim].CoorY)
		{
			lastToroY = toros[anim].CoorY;
			toroTemp = toros[anim];
		}
		toros[anim].toProt = false;
	}
	if (toroTemp != null) {toroTemp.toProt = true;}



	for (int i=0 ; i<toroC ; i++)
	{
	if ( toros[toroP[i]].Play() )
		{
		int t=toroP[i];
		toros[t] = null;
		toroP[i--] = toroP[--toroC];
		toroP[toroC] = t;
		}
	}

	toroOrdenar();
}


// -------------------
// Anim Sprite Ordenar
// ===================

public void toroOrdenar()
{
	int Conta=toroC-1;

	for (int t=0 ; t<toroC ; t++)
	{
		for (int i=0 ; i<Conta ; i++)
		{
			if (toros[toroP[i]].CoorY > toros[toroP[i+1]].CoorY)
			{
			int dato=toroP[i+1];
			toroP[i+1] = toroP[i];
			toroP[i] = dato;
			}
		}
	Conta--;
	}
}

// ---------------
// toro Colision
// ===============

int toroAstaColisionAnim;

public boolean toroAstaColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<toroC ; i++)
	{
		int anim = toroP[i];
		if ( colision(toros[anim].CoorX+toroAstaX, toros[anim].CoorY+toroAstaY, toroAstaWidth, toroAstaHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
			toroAstaColisionAnim = anim;
//			toros[anim].destroy = true;
			return true;
		}
	}
	return false;
}


public boolean toroCuerpoColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<toroC ; i++)
	{
		int anim = toroP[i];
		if ( colision(toros[anim].CoorX+toroCuX, toros[anim].CoorY+toroCuY, toroCuWidth, toroCuHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
//			toros[anim].destroy = true;
			return true;
		}
	}
	return false;
}

// ---------------
// toros Colisions
// ===============

public boolean torosColisions(toro obj)
{
	for (int i=0 ; i<toroC ; i++)
	{
		int anim = toroP[i];

		if ( toros[toroP[i]] != obj && colision(toros[anim].CoorX+toroCuX, toros[anim].CoorY+toroCuY, toroCuWidth, toroCuHeight,  obj.CoorX+toroCuX, obj.CoorY+toroCuY, toroCuWidth, toroCuHeight ))
		{
			return true;
		}
	}
	return false;
}

// <=- <=- <=- <=- <=-









// ********************
// --------------------
// cerbeza - Engine
// ====================
// ********************

static final int SUELO_CERBEZA = 0 *20;
static final int SUELO_RUEDA = 1 *20;
static final int SUELO_MADERA1 = 2 *20;
static final int SUELO_MADERA2 = 3 *20;
static final int SUELO_PARTICULAS = 4 *20;

static final int cerbezaWidth = 16;
static final int cerbezaHeight = 16;

static final int cerbezaCuX = 0;
static final int cerbezaCuY = 0;
static final int cerbezaCuWidth = 16;
static final int cerbezaCuHeight = 16;

// ---------------
// cerbeza SET
// ===============

public int cerbezaSET(int CoorX, int CoorY, int mode)
{
	if (cerbezaC == cerbezaMAX) {return(-1);}

	int i=cerbezaP[cerbezaC++];

	cerbezas[i] = new cerbeza();
	cerbezas[i].CoorX = CoorX;
	cerbezas[i].CoorY = CoorY;
	cerbezas[i].mode = mode;
	cerbezas[i].modeBase = mode;
	cerbezas[i].Play();

	return i;
}

// ---------------
// cerbeza class
// ===============

public class cerbeza extends AnimSprite
{
int mode, modeBase;
int sentido = 1;

// ----------------
// cerbeza Play
// ================

public boolean Play()
{
	switch (mode)
	{
	case SUELO_CERBEZA:
		SpriteSET(CoorX, CoorY,  20, 4, 1, 0x002);
		mode++;
	break;

	case SUELO_CERBEZA+1:
		if ( !colision(CoorX+cerbezaCuX, CoorY+cerbezaCuY, cerbezaCuWidth, cerbezaCuHeight, sc.ScrollX, sc.ScrollY, sc.ScrollSizeX, sc.ScrollSizeY) )
		{
			return true;
		}
	break;



	case SUELO_RUEDA:
		SpriteSET(CoorX, CoorY,  0, 10, 0, 0x204);
		Repetir = 3;
		mode++;
	break;

	case SUELO_RUEDA+1:
		CoorY -= 3;
	break;



	case SUELO_MADERA1:
		SpriteSET(CoorX, CoorY,  11, 4, 1, 0x204);
		Repetir = 4;
		mode++;
	break;

	case SUELO_MADERA1+1:
		CoorY -= 3;
		CoorX -= sentido * 2;
	break;



	case SUELO_MADERA2:
		SpriteSET(CoorX, CoorY,  16, 4, 1, 0x204);
		Repetir = 4;
		mode++;
	break;

	case SUELO_MADERA2+1:
		CoorY -= 3;
		CoorX += sentido * 2;
	break;



	case SUELO_PARTICULAS:

		SpriteSET(CoorX+((tileWidth-16)>>1), CoorY,  0, 8, 1, 0x300);
		mode++;
	break;
	}

	return super.Play();
}

};


// ---------------
// cerbeza INI
// ===============

final int cerbezaMAX=16;
int cerbezaC;
int[] cerbezaP;
cerbeza[] cerbezas;

public void cerbezaINI()
{
	cerbezaC = 0;
	cerbezaP = new int[cerbezaMAX];
	cerbezas = new cerbeza[cerbezaMAX];
	for (int i=0 ; i<cerbezaMAX ; i++) {cerbezaP[i]=i;}
}

// ---------------
// cerbeza RES
// ===============

public void cerbezaRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=cerbezaP[i];
	cerbezas[t] = null;
	cerbezaP[i--] = cerbezaP[--cerbezaC];
	cerbezaP[cerbezaC] = t;
}

// ---------------
// cerbeza END
// ===============

public void cerbezaEND()
{
	cerbezaP = null;
	cerbezas = null;
	cerbezaC = 0;
}

// ---------------
// cerbeza RUN
// ===============

public void cerbezaRUN()
{
	for (int i=0 ; i<cerbezaC ; i++)
	{
	if ( cerbezas[cerbezaP[i]].Play() )
		{
		int t=cerbezaP[i];
		cerbezas[t] = null;
		cerbezaP[i--] = cerbezaP[--cerbezaC];
		cerbezaP[cerbezaC] = t;
		}
	}
}

// ---------------
// cerbeza Colision
// ===============

public boolean cerbezaColision(int CoorX, int CoorY, int SizeX, int SizeY)
{
	for (int i=0 ; i<cerbezaC ; i++)
	{
		int anim = cerbezaP[i];
		if ( cerbezas[anim].modeBase == SUELO_CERBEZA && colision(cerbezas[anim].CoorX+cerbezaCuX, cerbezas[anim].CoorY+cerbezaCuY, cerbezaCuWidth, cerbezaCuHeight,  CoorX, CoorY, SizeX, SizeY ))
		{
//			cerbezas[anim].destroy = true;
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
		switch (s[i].Bank)
		{
		case 0:
			spriteDraw(spritesImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, spritesCor);
		break;	

		case 1:
			spriteDraw(toroImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, toroCor);
		break;	

		case 2:
			spriteDraw(objetosImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, objetosCor);
		break;	

		case 3:
			spriteDraw(particulesImg,  s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, particulesCor);
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
// panel - Engine
// ===================
// *******************

//#ifdef S30
//#elifdef S40
//#elifdef S60

	// Eje de los numeros de rosas pilladas
	static final int panelRosasX = 3;
	static final int panelRosasY = 30;
	
	// Ancho de la font numerica
	static final int panelFontWidth = 6;
	
	// Cuadrado de la barra de velocidad
	static final int panelBarraX = 8;
	static final int panelBarraY = 49;
	static final int panelBarraWidth = 7;
	static final int panelBarraHeight = 66;
	
	// Cuadrado donde mostrar item especial
	static final int panelItemX = 2;
	static final int panelItemY = 125;
	static final int panelItemWidth = 20;
	static final int panelItemHeight = 31;
	
	// acho bruto del panel
	static final int panelWidth = 22;
	
	// acho neto del panel
	static final int panelNetWidth = 20;

//#elifdef S80
//#endif

// -------------------
// panel Draw
// ===================

public void panelDraw()
{
	int panelX = canvasWidth - panelWidth;

	showImage(panelImg, panelX, 0);

//#ifdef J2ME
//#ifdef S40
//#endif
//#endif

	spriteDraw(panelFontImg, panelX+panelRosasX,                    panelRosasY, (faseRosas%1000)/100, panelFontCor);
	spriteDraw(panelFontImg, panelX+panelRosasX+ panelFontWidth,    panelRosasY, (faseRosas%100)/10,   panelFontCor);
	spriteDraw(panelFontImg, panelX+panelRosasX+(panelFontWidth*2), panelRosasY, (faseRosas%10),       panelFontCor);


//	int size = panelBarraHeight - regla3(protSpdOptima, protSpdMAX, panelBarraHeight);
	int size = panelBarraHeight - regla3(protSpdActual, protSpdMAX, panelBarraHeight);

	fillDraw(0, panelX+panelBarraX, panelBarraY, panelBarraWidth, size);



	if (protItem)
	{
		int x = panelX + panelItemX + ((panelItemWidth - tileWidth)/2);
		int y = panelItemY + ((panelItemHeight - tileHeight)/2);
		spriteDraw(objetosImg, x, y, 45, objetosCor);
	}

}


// <=- <=- <=- <=- <=-














// *******************
// -------------------
// playerSelect - Engine
// ===================
// *******************

byte[] playerSelectCor;

//#ifdef J2ME
Image playerSelectImg;
//#elifdef DOJA
//#endif

boolean playerSelectShow;

int playerSelected;

boolean playerSelectNoPlay;

// -------------------
// playerSelect Create
// ===================

public void playerSelectCreate()
{
	playerSelectShow = false;

	playerSelectCor = loadFile("/seleccion.cor");
//#ifdef J2ME
	playerSelectImg = loadImage("/seleccion");
//#elifdef DOJA
//#endif

	playerSelected = 0;

	playerSelectNoPlay = false;

	playerSelectShow = true;
}

// -------------------
// playerSelect Destroy
// ===================

public void playerSelectDestroy()
{
	playerSelectShow = false;

	playerSelectImg = null;
	playerSelectCor = null;

	System.gc();
}

// -------------------
// playerSelect Tick
// ===================

public boolean playerSelectTick()
{

	if (keyX != 0 && lastKeyX == 0) {playerSelected += keyX; playerSelectShow = true; playerSelectNoPlay = false;}
	else
	if (keyY != 0 && lastKeyY == 0) {playerSelected += keyY; playerSelectShow = true; playerSelectNoPlay = false;}

	if (playerSelected < 0) {playerSelected = 0;}
	else
	if (playerSelected > 3) {playerSelected = 3;}

	if (keyMenu > 0 && lastKeyMenu == 0)
	{
		if (playerSelected < 3 || gameCompleted)
		{
			return true;
		} else {
			playerSelectNoPlay = !playerSelectNoPlay;
			playerSelectShow = true;
		}
	}

	if (keyMenu < 0 && lastKeyMenu == 0)
	{
		return true;
	}

	return false;
}

// -------------------
// playerSelect Draw
// ===================

public void playerSelectDraw()
{
	if (!playerSelectShow) {return;} else {playerSelectShow = false;}


	int mayaWidth =  playerSelectCor[(8*6)+2];
	int mayaHeight = playerSelectCor[(8*6)+3];
	int trozoWidth = (canvasWidth / mayaWidth)+1;
	int trozoHeight = (canvasHeight / mayaHeight)+1;


	int cuadradoWidth = playerSelectCor[(0*2)+2];
	int cuadradoHeight = playerSelectCor[(0*2)+3];
	int cuadradoX = (canvasWidth - cuadradoWidth)/2;
	int cuadradoY = (canvasHeight - cuadradoHeight)/2;


	for (int y=0 ; y<trozoHeight ; y++)
	{
		for (int x=0 ; x<trozoWidth ; x++)
		{
			spriteDraw(playerSelectImg, x*mayaWidth, y*mayaHeight,  8, playerSelectCor);
		}
	}

	spriteDraw(playerSelectImg, cuadradoX, cuadradoY,  3+(playerSelected==3 && !gameCompleted?4:playerSelected), playerSelectCor);

	spriteDraw(playerSelectImg, cuadradoX, cuadradoY,  0, playerSelectCor);

	if (playerSelected > 0)
	{
		spriteDraw(playerSelectImg, 0, 0,  1, playerSelectCor, 0,cuadradoY, cuadradoX, cuadradoHeight, R_HCENTER|R_VCENTER);
	}

	if (playerSelected < 3)
	{
		spriteDraw(playerSelectImg, 0, 0,  2, playerSelectCor, cuadradoX+cuadradoWidth, cuadradoY, cuadradoX, cuadradoHeight, R_HCENTER|R_VCENTER);
	}

	canvasTextCreate(0,0, canvasWidth, cuadradoY);
//#ifdef J2ME
	scr.setClip(0,0, canvasWidth, canvasHeight);
//#endif
	textDraw(gameText[TEXT_SELECT_PLAYER][0], 0,0, 0xffffff, TEXT_VCENTER|TEXT_HCENTER|TEXT_OUTLINE);


	canvasTextCreate(0,cuadradoY+cuadradoHeight, canvasWidth, cuadradoY);
//#ifdef J2ME
	scr.setClip(0,0, canvasWidth, canvasHeight);
//#endif


	String strTemp = null;
	if (!playerSelectNoPlay)
	{
		strTemp = gameText[TEXT_PLAYER_NAMES][playerSelected];
	} else {
		strTemp = gameText[TEXT_PLAYER_NAMES][4];
	}

	int modeTemp = 0;
	if ( textBreak(strTemp, printerSizeX, magicBiosGetFont(0)).length * magicBiosGetFont(0).getHeight() < printerSizeY)
	{
		modeTemp = (TEXT_HCENTER|TEXT_VCENTER|TEXT_OUTLINE);
	} else {
		modeTemp = (TEXT_HCENTER|TEXT_BOTTON|TEXT_OUTLINE);
	}
	textDraw(strTemp, 0,0, 0xffffff, modeTemp);
}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// ingameHelp - Engine
// ===================
// *******************

int[] ingameHelp = new int[] {
	0, TEXT_INGAME_HELP_RUEDA,
	0, TEXT_INGAME_HELP_BIDON,
	0, TEXT_INGAME_HELP_CAJA,
	0, TEXT_INGAME_HELP_BARRERA,
	0, TEXT_INGAME_HELP_ROSA,
	0, TEXT_INGAME_HELP_MANZANA,
	0, TEXT_INGAME_HELP_CAJA_AZUL,
	0, TEXT_INGAME_HELP_ITEM_SPECIAL,
};

int ingameHelpText;

// -------------------
// ingameHelp Create
// ===================

public void ingameHelpCreate()
{
	for (int i=0 ; i<ingameHelp.length ; i+=2)
	{
		ingameHelp[i] = 0;
	}
}

// -------------------
// ingameHelp Init
// ===================

public void ingameHelpInit(int type)
{
	if (ingameHelp[type<<1] == 0) {ingameHelp[type<<1] = 1;}
}

// -------------------
// ingameHelp Tick
// ===================

public boolean ingameHelpTick()
{
	if (faseLevel != 1) {return false;}

	for (int i=0 ; i<ingameHelp.length ; i+=2)
	{
		if (ingameHelp[i] == 1)
		{
			ingameHelp[i] = 2;
			ingameHelpText = ingameHelp[i+1];
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

	do {
	// Si es null es que nunca se han grabado prefs, por lo cual INICIALIZAMOS las prefs del juego
		if (prefsData == null)
		{
			prefsData = new byte[] {1, 1, 1, 0, 0,0,0,0, 0,0,0,0, 0};		// Inicializamos preferencias ya que NO estaban grabadas
		}
	
	// Actualizamos las variables del juego segun las prefs leidas / inicializadas

		try {

			gameSound = prefsData[0] != 0;
			gameVibra = prefsData[1] != 0;
			gameLevelLast = prefsData[2];
			playerSelected = prefsData[3];
			gameScore = convAry2Int(prefsData, 4);
			gameHiScore = convAry2Int(prefsData, 8);
			gameCompleted = prefsData[12] != 0;

		} catch(Exception e)
		{
		// Si catch, entonces prefs corruptas, inicializarlas de cero.
		//#ifdef com.mygdx.mongojocs.toro2.Debug
			Debug.println("Prefs corruptas, Inicializando de cero...");
		//#endif
			prefsData = null;
		}
	} while(prefsData == null);

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
	prefsData[3] = (byte)playerSelected;
	convInt2Ary(prefsData, 4, gameScore);
	convInt2Ary(prefsData, 8, gameHiScore);
	prefsData[12] = (byte)(gameCompleted? 1:0);

// Almacenamos las prefs

	updatePrefs(prefsData);		// Almacenamos byte[]
}

// <=- <=- <=- <=- <=-



// -------------------
// cheats
// ===================

//#ifdef CHEATS
public void cheats()
{
	if (keyMisc == 11 && lastKeyMisc == 0) {protCompletInit();}
}
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