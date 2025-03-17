

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.11 (11.7.2005)
// ===============================================


//#define !RMS_ENGINE


//#define LAYOUT_MINIMUM
//#define !LAYOUT_SIMPLE
//#define !LAYOUT_CUSTOM


//#ifdef PACKAGE
package com.mygdx.mongojocs.sextron;
//#endif


//#ifdef J2ME

	//#ifdef NOKIAUI
	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.files.FileHandle;
	import com.badlogic.gdx.graphics.Color;
	import com.mygdx.mongojocs.midletemu.Canvas;
	import com.mygdx.mongojocs.midletemu.CommandListener;
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

	//#endif

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
//#ifdef com.mygdx.mongojocs.sextron.Debug
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
//#ifdef com.mygdx.mongojocs.sextron.Debug
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
boolean gamePlayGoMenu = false;
int gameSoundOld = -1;
int gameSoundLoop;


boolean gameStarted = false;

long gameMilis, CPU_Milis;
int gameSleep;
int mainSleep = 50;

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
			//#ifdef com.mygdx.mongojocs.sextron.Debug
				Debug.spdStart(0);
			//#endif
				biosTick();
			//#ifdef com.mygdx.mongojocs.sextron.Debug
				Debug.spdStop(0);
			//#endif

				if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}
				gameDraw();

				if (gameSoundRefresh)
				{
				//#ifdef INCOMING_CALL_STOP_SOUND
					gameSound = false;
					if (biosStatus == 0 && gameStatus == GAME_PLAY_TICK) {gamePlayGoMenu = true;}
					if (biosStatus == BIOS_MENU && (menuType == MENU_MAIN || menuType == MENU_INGAME)) {menuInit(menuType, menuListPos);}
				//#endif

					gameSoundRefresh = false;
					if (gameSoundOld >= 0 && gameSoundLoop==0) {soundPlay(gameSoundOld,gameSoundLoop);}
				} else {
					soundTick();
				}

			} catch (Exception e)
			{
			//#ifdef com.mygdx.mongojocs.sextron.Debug
				Debug.println("** Excep Logic **");
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
		//#ifdef com.mygdx.mongojocs.sextron.Debug
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
			//#ifdef com.mygdx.mongojocs.sextron.Debug
				Debug.println("*** Exception Grafica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}

		//#ifdef DOJA
		//#endif

		//#ifdef com.mygdx.mongojocs.sextron.Debug
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
// Inicializamos com.mygdx.mongojocs.sextron.Debug Engine
// ================================
//#ifdef com.mygdx.mongojocs.sextron.Debug
	Debug.debugCreate(this);
//#endif
// ================================


// --------------------------------
// Limpiamos canvas de blanco y mostramos imagen de loading (reloj de arena)
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


// --------------------------------
// Cargamos archivo de textos
// ================================
	gameText = textosCreate( loadFile("/Textos.txt") );
// ================================


// --------------------------------
// Cargamos recursos del menu para el LAYOUT CUSTOM
// ================================
//#ifdef LAYOUT_CUSTOM
//#endif
// ================================


// --------------------------------
// Inicializamos tipo de FONT base a usar en el juego
// ================================
	printSetArea();

	printInit_Font(magicBiosGetFont(0));

	gameFontImg = loadImage("/font");
	gameFontCor = loadFile("/font.cor");

//	printInit_Font(gameFontImg, gameFontCor, 8, 0x30, 1);
// ================================


// --------------------------------
// Inicializamos juego a nivel se usuario
// ================================
	gameCreate();
// ================================

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

//#ifdef com.mygdx.mongojocs.sextron.Debug
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
//#ifdef com.mygdx.mongojocs.sextron.Debug
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
//	case BIOS_POPUP:
//		formShow=true;
//		popupShow = true;
//	return;
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

int menuListIni;
int menuListPos;
int menuListCMD;

int menuListX;
int menuListY;
int menuListWidth;
int menuListHeight;

int menuListScrY;
int menuListScrSizeY;

String	menuListStr[][];
int		menuListDat[][];

static final int ML_TEXT = 1;
static final int ML_SCROLL = 2;
static final int ML_CICLE = 3;
static final int ML_SCREEN = 4;
static final int ML_NONE = 5;
static final int ML_LIST = 6;
static final int ML_OPTION = 7;

int menuListTabIndexPos;
int menuListTabIndexSize;
short[] menuListTabIndex;

int menuListOptionBoxHeight;

// -------------------
// menuList Init
// ===================

public void menuListInit(int x, int y, int sizeX, int sizeY)
{
	menuListX = x;
	menuListY = y;
	menuListWidth = sizeX;
	menuListHeight = sizeY;
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

public void menuListAdd(int dato, String texto)
{
	String[] textos = printTextBreak(texto, menuListWidth);

	for (int i=0 ; i<textos.length ; i++)
	{
		menuListAdd(dato, new String[] {textos[i]}, 0, 0);
	}
}


public void menuListAdd(int dato, String[] texto)
{
	for (int i=0 ; i<texto.length ; i++)
	{
		menuListAdd(dato, texto[i]);
	}
}


public void menuListAdd(int dato, String texto, int dat1)
{
	menuListAdd(dato, new String[] {texto}, dat1, 0);
}

public void menuListAdd(int dato, String[] texto, int dat1)
{
	menuListAdd(dato, new String[] {texto[0]}, dat1, 0);
}

public void menuListAdd(int dat0, String[] texto, int dat1, int dat2)
{
	int lines=(menuListDat!=null)?menuListDat.length:0;

	String str[][] = new String[lines+1][];
	int dat[][] = new int[lines+1][];

	int i=0;

	for (; i<lines; i++)
	{
		dat[i] = menuListDat[i];
		str[i] = menuListStr[i];
	}

	int textoSizeX = 2;
	for (int sizeX, t=0 ; t<texto.length ; t++) {sizeX = printStringWidth(texto[t]); if ( textoSizeX < sizeX ) {textoSizeX=sizeX;}}

	str[i] = texto;
	dat[i] = new int[] {dat0, dat1, dat2, textoSizeX, printGetHeight() };

	menuListStr = str;
	menuListDat = dat;
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
	menuListScrY = menuListHeight;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCROLL;

	menuListScrSizeY = 0; for (int i=0 ; i<menuListDat.length ; i++) {menuListScrSizeY += menuListDat[i][4];}
}

public void menuListSet_Cicle()
{
	menuListSet_Cicle(0);
}

public void menuListSet_Cicle(int Pos)
{
	menuListPos=Pos;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_CICLE;

//#ifdef LAYOUT_SIMPLE
//#endif

	menuFadeColor = false;
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

// Calculamos area para mostrar listado de opciones
	int maxWidth = 0; for (int i=0 ; i<menuListDat.length ; i++) {if (maxWidth < menuListDat[i][3]) {maxWidth = menuListDat[i][3];}}
	int sobra = (menuListWidth - maxWidth)/3;
	menuListX += sobra;
	menuListY += printGetHeight();
	menuListWidth -= sobra*2;
	menuListHeight -= printGetHeight();

//#ifdef LAYOUT_CUSTOM
//#else
	menuListOptionBoxHeight = printGetHeight() + 4;
//#endif

	menuListTabIndexPos = 0;
	menuListTabIndexSize = (menuListHeight / menuListOptionBoxHeight)>menuListStr.length? menuListStr.length:(menuListHeight / menuListOptionBoxHeight);

//#ifndef LAYOUT_MINIMUM
// Si no hace falta scroll, eliminamos Slider vertical (menuVSlider)
	if ( (menuListHeight / menuListOptionBoxHeight) > menuListStr.length)
	{
		menuVSliderWidth = 0;
	}
//#endif

	menuFadeColor = true;
}

public void menuListSet_Screen()
{

	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCREEN;


	menuFadeColor = true;


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
	
			if ( (size += menuListDat[act][4]) > menuListHeight ) {act--; finalCampo = true;}
	
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


public void menuListSet_List()
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_LIST;

	menuListTabIndexPos = 0;
	menuListTabIndexSize = (menuListHeight / printGetHeight() )>menuListStr.length? menuListStr.length:(menuListHeight / printGetHeight());

//#ifndef LAYOUT_MINIMUM
// Si no hace falta scroll, eliminamos Slider vertical (menuVSlider)
	if ( (menuListHeight / printGetHeight() ) > menuListStr.length)
	{
		menuVSliderWidth = 0;
	}
//#endif

	menuFadeColor = true;
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
	case ML_CICLE:
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

		if (back) {menuListCMD=ACTION_BACK; return true;}

		if (menuListMode == ML_OPTION)
		{
			if (menuListTabIndexPos > menuListPos) {menuListTabIndexPos = menuListPos;}
			if (menuListTabIndexPos+menuListTabIndexSize <= menuListPos) {menuListTabIndexPos = menuListPos-menuListTabIndexSize+1;}
		}
	break;


	case ML_SCROLL:
		menuListScrY--;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}
		if (back) {menuListCMD=ACTION_BACK; return true;}

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

		if (back) {menuListCMD=ACTION_BACK; return true;}

	break;


	case ML_LIST:
		if (movY == -1 && menuListTabIndexPos > 0) {menuListTabIndexPos--; menuListShow=true;}
		if (movY ==  1 && menuListTabIndexPos + menuListTabIndexSize < menuListStr.length) {menuListTabIndexPos++; menuListShow=true;}

		if (back) {menuListCMD = ACTION_BACK; return true;}
		if (fire && menuListDat[0][1] != 0) {menuListCMD = menuListDat[0][1]; return true;}
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
//#ifdef J2ME
	scr.setClip(menuListX, menuListY, menuListWidth, menuListHeight);
//#endif

	int y;

	switch (menuListMode)
	{
	case ML_CICLE:
	//#ifdef LAYOUT_MINIMUM
	//#elifdef LAYOUT_SIMPLE
	//#elifdef LAYOUT_CUSTOM
	//#endif
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

//		alphaFillDraw(0x80000000, 0,0, canvasWidth, canvasHeight);

		if (menuListScreenClear) {menuListScreenClear = false; break;}

		y = 0;

		int ini =  menuListTabIndex[menuListTabIndexPos];
		int size = menuListTabIndex[menuListTabIndexPos+1];

		for (int i=0 ; i<size ; i++)
		{
			y += menuListDat[ini+i][4];
		}

		y = ((menuListHeight - y) >> 1);

		for (int i=0 ; i<size ; i++)
		{
			menuListTextDraw(menuListDat[i][0], menuListStr[ini+i][0], 0, y);

			y += menuListDat[i][4];
		}
	break;


	case ML_LIST:

//		alphaFillDraw(0x80000000, 0,0, canvasWidth, canvasHeight);

//		if (menuListScreenClear) {menuListScreenClear = false; break;}

//		y = menuListY;

		y = (menuListHeight - (menuListTabIndexSize * printGetHeight()))/2;
	
		for (int i=0 ; i<menuListTabIndexSize ; i++)
		{
			menuListTextDraw(menuListDat[menuListTabIndexPos+i][0], menuListStr[menuListTabIndexPos+i][0], 0, y);

			y += menuListDat[i][4];
		}

	break;


	case ML_OPTION:

	// Dibujamos Marco de seleccion

	//#ifndef LAYOUT_CUSTOM
		rectDraw(0xffffff, menuListX, menuListY+((menuListPos-menuListTabIndexPos)*menuListOptionBoxHeight), menuListWidth, menuListOptionBoxHeight);
	//#else
	//#endif

	// Imprimimos Letras
		printHeight = menuListOptionBoxHeight;
		for (int i=0 ; i<menuListTabIndexSize ; i++)
		{
			printDraw(menuListStr[menuListTabIndexPos+i][menuListDat[menuListTabIndexPos+i][2]], 4, 0, 0xaaaaaa, PRINT_VCENTER|PRINT_OUTLINE);

			printY += menuListOptionBoxHeight;
		}

	break;
	}
}



public void menuListTextDraw(int format, String str, int x, int y)
{
	printDraw(str, x, y, 0xaaaaaa, (format==0?PRINT_HCENTER:0) | PRINT_OUTLINE);
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
// print - Engine - Rev.0 (3.3.2004)
// ===================
// *******************

static final int PRINT_LEFT =	0x0000;
static final int PRINT_RIGHT =	0x0001;
static final int PRINT_HCENTER=	0x0002;
static final int PRINT_TOP =	0x0000;
static final int PRINT_BOTTOM =	0x0004;
static final int PRINT_VCENTER=	0x0008;

static final int PRINT_OUTLINE=	0x1000;

static final int PRINT_MASK =	0x6000;
static final int PRINT_HMASK =	0x2000;
static final int PRINT_VMASK =	0x4000;

//static final int PRINT_BREAK=	0x8000;

int printOutlineRGB = 0x000066;

int printX;
int printY;
int printWidth;
int printHeight;

Font printFont;
Image printFontImg;
byte[] printFontCor;

int printFontType;
int printFontHeight;
int printAscent;

int printFont_Min;
int printFont_Spc;

int printMaskAddX;

// -------------------
// print Create
// ===================

public void printSetArea()
{
	printX = 0;
	printY = 0;
	printWidth = canvasWidth;
	printHeight = canvasHeight;
}


public void printSetArea(int x, int y, int width, int height)
{
	printX = x;
	printY = y;
	printWidth = width;
	printHeight = height;
}


// -------------------
// print Destroy
// ===================

public void printDestroy()
{
	printFont = null;
	printFontImg = null;
	printFontCor = null;
}


// -------------------
// print Init
// ===================

public void printInit_Font(Font f)
{
	printFont = f;
	printFontHeight = f.getHeight();

//#ifdef DOJA
//#endif

	printFontType = 1;
}


public void printInit_Font(Image img, byte[] cor, int height, int letraMin, int letraSpc)
{
	printFontImg = img;
	printFontCor = cor;
	printFontHeight = height;
	printFont_Min = letraMin;
	printFont_Spc = letraSpc;

	printFontType = 2;
}


// -------------------
// print Fill
// ===================

public void printFill(int rgb)
{
//#ifdef J2ME
	scr.setClip(printX, printY, printWidth, printHeight);
//#endif
	putColor(rgb);
	scr.fillRect(printX, printY, printWidth, printHeight);
}


// -------------------
// print Draw
// ===================

public void printDraw(String str, int x, int y, int RGB, int mode)
{
	printDraw(new String[] {str}, x, y, RGB, mode);
}

public void printDraw(String[] str, int x, int y, int RGB, int mode)
{
//#ifdef DOJA
//#endif

	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - (printFontHeight*str.length) )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - (printFontHeight*str.length) );}

/*
	if ((mode & PRINT_HMASK) != 0)
	{
		str = printTextMask(str, x);
		x += printMaskAddX;
	}
*/

	x += printX;
	y += printY;


	for (int line=0 ; line<str.length ; line++)
	{
		int destX = x;
		int destY = y + (line * printFontHeight);

		if ((mode & PRINT_HCENTER)!=0) {destX +=( printWidth - printStringWidth(str[line]) )/2;}
		else
		if ((mode & PRINT_RIGHT)!=0)   {destX +=( printWidth - printStringWidth(str[line]) );}

		if ( ((mode & PRINT_VMASK) == 0) || (y >= printY) && (y + (printFontHeight*str.length) <= printY + printHeight) )
		{
	
		//#ifdef DOJA
		//#endif
	
			switch (printFontType)
			{
			case 1:

			//#ifdef J2ME
				if ((mode & PRINT_MASK) == 0)
				{
					scr.setClip(0, 0, canvasWidth, canvasHeight);
				} else {
					scr.setClip(printX, printY, printWidth, printHeight);
				}
			//#endif

				scr.setFont(printFont);
				putColor(RGB);

				if ((mode & PRINT_OUTLINE) != 0)
				{
					putColor(printOutlineRGB);
				//#ifdef J2ME
					scr.drawString(str[line],  destX-1, destY , 20);
					scr.drawString(str[line],  destX+1, destY , 20);
					scr.drawString(str[line],  destX, destY-1 , 20);
					scr.drawString(str[line],  destX, destY+1 , 20);

					scr.drawString(str[line],  destX-1, destY-1 , 20);
					scr.drawString(str[line],  destX+1, destY+1 , 20);
					scr.drawString(str[line],  destX+1, destY-1 , 20);
					scr.drawString(str[line],  destX-1, destY+1 , 20);
				//#elifdef DOJA
				//#endif
				}

				putColor(RGB);
			//#ifdef J2ME
				scr.drawString(str[line],  destX, destY , 20);
			//#elifdef DOJA
			//#endif
			break;
		
		//#ifndef NO_CUSTOM_FONT
			case 2:
				byte[] tex = str[line].getBytes();
		
				for (int add=0, i=0 ; i<tex.length ; i++)
				{
					try
					{
						int letra = (tex[i] & 0xff);

						if (letra == 0x20)
						{
							letra = 0x41 - printFont_Min;
						} else {
							letra -= printFont_Min;
			
							if (letra >= 0 && letra < printFontCor.length/6)	// Esta dentro de las letras permitidas???
							{
								printSpriteDraw(printFontImg, destX+add, destY, letra , printFontCor, mode);
							}
						}
	
						add += printFontCor[(letra * 6) + 2 ] + printFont_Spc;
		
					} catch (Exception e) {}
				}
			break;
		//#endif
			}
		}
	}

}


// -------------------
// print Sprite Draw
// ===================

public void printSpriteDraw(Image img, int x, int y, int frame, byte[] cor, int mode)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame  ] + 128;

//#ifdef J2ME

	if (destX >= canvasWidth || destY >= canvasHeight || (destX + sizeX) <= 0 || (destY + sizeY) <= 0) {return;}

	//#ifdef x55
	if (destX < 0) {sourX += -destX; sizeX += destX; destX=0;}
	if (destY < 0) {sourY += -destY; sizeY += destY; destY=0;}
	//#endif

	scr.setClip(destX, destY, sizeX, sizeY);
/*
	if ((mode & PRINT_MASK) == 0)
	{
	scr.setClip(destX, destY, sizeX, sizeY);
	} else {
	scr.setClip(printX, printY, printWidth, printHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);
	}
*/
	scr.drawImage(img, destX-sourX, destY-sourY, 20);
//#endif

//#ifdef DOJA
//#endif

}


// -------------------
// print String Width
// ===================

public int printStringWidth(String str)
{
//#ifdef NO_CUSTOM_FONT
//#else
	int size = 0;

	switch (printFontType)
	{
	case 1:
		size = printFont.stringWidth(str);
	break;

	case 2:
		byte[] tex = str.getBytes();

		for (int i=0 ; i<tex.length ; i++)
		{
			try {
				int letra = (tex[i] & 0xff);

				if (letra == 0x20) {letra = 0x41;}

 				letra -= printFont_Min;

				size += printFontCor[(letra * 6) + 2 ] + printFont_Spc;
			} catch (Exception e) {}
		}
	break;
	}

	return size;
//#endif
}


// -------------------
// print Text Height
// ===================

public int printGetHeight()
{
	return printFontHeight;
}


// -------------------
// print Sprite Width
// ===================

public int printSpriteWidth(byte[] cor, int frame)
{
	return cor[(frame*6)+2];
}


// -------------------
// print Sprite Height
// ===================

public int printSpriteHeight(byte[] cor, int frame)
{
	return cor[(frame*6)+3];
}


// -------------------
// print Draw
// ===================

//#ifdef J2ME
public void printDraw(Image img, int x, int y, int frame, int mode, byte[] cor)
{
	frame*=6;

	int despX=cor[frame++];
	int despY=cor[frame++];
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame  ] + 128;


	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - sizeY )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - sizeY );}


	if ((mode & PRINT_HCENTER)!=0) {x +=( printWidth - sizeX )/2;}
	else
	if ((mode & PRINT_RIGHT)!=0)   {x +=( printWidth - sizeX );}


	x += printX + despX;
	y += printY + despY;


	scr.setClip(x, y, sizeX, sizeY);
	scr.drawImage(img, x-sourX, y-sourY, 20);
}
//#elifdef DOJA
//#endif




// -------------------
// print Text Break
// ===================

short[] printTextBreakParts;


public int[] printTextBreak(int[] data, int num)
{
	int[] tempData = new int[num];

	for (int pos=0, i=0 ; i<data.length ; i++)
	{
		for (int t=0 ; t<printTextBreakParts[i] ; t++)
		{
			tempData[pos++] = data[i];
		}
	}

	return tempData;
}


public String[] printTextBreak(String[] str, int width)
{
	printTextBreakParts = new short[str.length];

	String[] tempStr = new String[256];
	int tempNum = 0;

	for (int i=0 ; i<str.length ; i++)
	{
		String[] tempLines = printTextBreak(str[i] , width);
		printTextBreakParts[i] = (short) tempLines.length;

		for (int t=0; t<tempLines.length ; t++)
		{
		tempStr[tempNum++] = tempLines[t];
		}
	}

	String[] finalStr = new String[tempNum];

	for (int i=0 ; i<tempNum ; i++)
	{
	finalStr[i] = tempStr[i];
	}

	return finalStr;
}

public String[] printTextBreak(String str, int width)
{
//#ifdef NO_CUSTOM_FONT
//#else
	switch (printFontType)
	{
	case 1:
	return textBreak(str, width, printFont);

	case 2:
	return textBreak(str, width, printFontCor, printFont_Min, printFont_Spc);
	}

	return new String[] {str};
//#endif
}


// -------------------
// print Text Cut
// ===================

public String printTextCut(String str, int width)
{
//#ifdef NO_CUSTOM_FONT
//#else
	switch (printFontType)
	{
	case 1:
		str = textCut(str, width, printFont);
	break;

	case 2:
		str = textCut(str, width, printFontCor, printFont_Min, printFont_Spc);
	break;
	}
//#endif

	return str;
}


// -------------------
// print Text Mask
// ===================

public String printTextMask(String str, int x, int width)
{
	byte[] sizes = null;

//#ifdef NO_CUSTOM_FONT
//#else
	if (printFontType == 1)
	{
		sizes = textSized(str, printFont);
	} else {
		sizes = textSized(str, printFontCor, printFont_Min, printFont_Spc);
	}
//#endif

	int total = 0; for (int i=0 ; i<sizes.length ; i++) {total += sizes[i];}

	printMaskAddX = x;
	int ini = 0; while ( printMaskAddX < 0 ) {printMaskAddX += sizes[ini++];}

	printMaskAddX -= x;

	int posX = x + total;
	int fin = str.length();	while ( posX > width ) {posX -= sizes[--fin];}

	return str.substring( ini, fin );
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

int rnd = 0;
public int rnd(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	rnd = (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
	return rnd;
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
// textos - Engine - v1.3 - Rev.5 (20.9.2005)
// ===================
// *******************

// -------------------
// textos Create
// ===================

public String[][] textosCreate(byte[] tex)
{
//#ifdef com.mygdx.mongojocs.sextron.Debug
	try {
//#endif
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

//#ifdef com.mygdx.mongojocs.sextron.Debug
	} catch(Exception e)
	{
		Debug.println("textosCreate() Exception");
		Debug.println(e.toString());
	}

	return null;
//#endif
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
	scr.drawRect(x, y, width-1, height-1);
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
//	putColor(argb & 0x00ffffff);
//#ifndef DOJA
	scr.setClip(x, y, width, height);
//#endif
//#ifndef VI-TSM100
	for (int i=0 ; i<height ; i+=2) {scr.fillRect(x,y+i, width, 1);}
	for (int i=0 ; i<width ; i+=2) {scr.fillRect(x+i,y, 1, height);}
//#else
//#endif
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

public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor, int topX, int topY, int topWidth, int topHeight)
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

	imageDraw(img, sourX, sourY,  sizeX, sizeY,  destX, destY,  flip,  topX, topY, topWidth, topHeight);
}

public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor, int trozos)
{
	frame*=6;

	for (int i=0 ; i<trozos ; i++)
	{
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

		imageDraw(img, sourX, sourY,  sizeX, sizeY,  x+destX, y+destY,  flip,  0 ,0, canvasWidth, canvasHeight);
	}
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

//#ifdef LOAD_FILE_CACHING
//#endif

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
	//#ifdef com.mygdx.mongojocs.sextron.Debug
		Debug.println("loadImage: "+FileName+" <File not found>");
		return null;
	//#endif
	}

//#ifdef com.mygdx.mongojocs.sextron.Debug
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
	printSetArea(0,0, canvasWidth, canvasHeight);
	printInit_Font(magicBiosGetFont(0));
	printDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
}

// <=- <=- <=- <=- <=-














/*

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
	Y += f.getAscent();
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

	//#ifdef J2ME
		scr.setClip(printerX, printerY, printerSizeX, printerSizeY);
	//#endif

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
		scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
	//#elifdef DOJA
		scr.drawString(Str[i],  X-1+despX, Y+despY);
		scr.drawString(Str[i],  X+1+despX, Y+despY);
		scr.drawString(Str[i],  X+despX, Y-1+despY);
		scr.drawString(Str[i],  X+despX, Y+1+despY);

		scr.drawString(Str[i],  X-1+despX, Y-1+despY);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY);
		scr.drawString(Str[i],  X+1+despX, Y-1+despY);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY);
	//#endif
		}

		putColor(RGB);
	//#ifdef J2ME
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
	//#elifdef DOJA
		scr.drawString(Str[i],  X+despX, Y+despY);
	//#endif
	}

}

// <=- <=- <=- <=- <=-

*/












// *******************
// -------------------
// textBreak - Engine - Rev.5 (4.11.2005)
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


// -------------------
// text Cut - IN: String ; OUT: String - (SYSTEM FONT)
// ===================

public String textCut(String texto, int width, Font f)
{
	int pos = 0, size = 0, textLength = texto.length();

	while ( size < width )
	{
		if ( pos == textLength) {pos++; break;}

		int dat = texto.charAt(pos++);

//#ifdef J2ME
		size += f.charWidth((char)dat);
//#elifdef DOJA
//#endif
    }

	return texto.substring(0,--pos);
}


// -------------------
// text Cut - IN: String ; OUT: String - (CUSTOM FONT)
// ===================

public String textCut(String texto, int width, byte[] f, int letraMin, int letraSpc)
{
	int pos = 0, size = 0, textLength = texto.length();

	while ( size < width )
	{
		if ( pos == textLength ) {pos++; break;}

		int dat = texto.charAt(pos++);

		if ( dat == 0x20 ) { dat=0x41; }

		try {
			size += ( f[ ((dat - letraMin) *6) +2 ] + letraSpc );
		} catch(Exception e) {size+=4;}
	}

	return texto.substring(0,--pos);
}


// -------------------
// text Sized - IN: String ; OUT: byte[] con width de cada letra - (SYSTEM FONT)
// ===================

public byte[] textSized(String texto, Font f)
{
	byte[] sizes = new byte[texto.length()];

	for (int i=0 ; i<sizes.length ; i++)
	{
	//#ifdef J2ME
		sizes[i] = (byte)f.charWidth((char)texto.charAt(i));
	//#endif

	//#ifdef DOJA
	//#endif
	}

	return sizes;
}


// -------------------
// text Sized - IN: String ; OUT: byte[] con width de cada letra - (CUSTOM FONT)
// ===================

public byte[] textSized(String texto, byte[] f, int letraMin, int letraSpc)
{
	byte[] sizes = new byte[texto.length()];

	for (int i=0 ; i<sizes.length ; i++)
	{
		int letra = texto.charAt(i);

		if (letra == 0x20) {letra=0x41;}	// Espacio???

		try {
			sizes[i] = (byte)( f[ ((letra - letraMin) *6) +2 ] + letraSpc );
		} catch(Exception e) {sizes[i] = 4;}
	}

	return sizes;
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
// Gestiones internes de la BIOS
	if (canvasFillShow) { canvasFillShow=false; canvasFillDraw(canvasFillRGB); }
	if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }
	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }



// Hack para refrescar el campo de juego mientras estemos en el menu 'INGAME'
	if (biosStatus == BIOS_MENU && gameStatus == GAME_PLAY_TICK && menuListShow) {playShow = true;}


// Renderizamos el campo de juego
	if (playShow) { playShow=false; playDraw(); }


// Renderizamos el fadeOut al quitar del juego
	if (gameStatus == GAME_EXIT+1) {alphaFillDraw(0x44000000, 0,0, canvasWidth, canvasHeight);}


// Renderizamos gestor de menus
	menuDraw();


// Renderizamos textos de las softkeys
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
				//#ifdef com.mygdx.mongojocs.sextron.Debug
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
				//#ifdef com.mygdx.mongojocs.sextron.Debug
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
				//#ifdef com.mygdx.mongojocs.sextron.Debug
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
static final int SOFTKEY_YES = 6;
static final int SOFTKEY_NO = 7;

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
//#endif

	listenerIdLeft = idLeft;
	listenerIdRight = idRight;
}

public void listenerInit(String strLeft, String strRight) {}

public void listenerDraw()
{
//#ifdef LISTENER_DOWN
/*
	if (listenerDown < 0) {return;}

	if (listenerDown-- == 0)
	{
		gameForceRefresh = true;
		return;
	}
*/
//#endif

	if (listenerImg != null)
	{
		int listenerY = canvasHeight;

//#ifdef LISTENER_DOWN
//#endif

//		alphaFillDraw(0x88000000, 0, listenerY, canvasWidth, 6);	// Para hacerlo semitransparente

		putColor(0);
		scr.setClip(0, listenerY, canvasWidth, listenerHeight);
//#ifdef LISTENER_DOWN
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
// ihd - Engine - ihd (iMode Hard Disk) File System v1.0 - Rev.5 (22.9.2005)
// ----------------
// Dise�ado y Programado por Juan Antonio Gomez Galvez.
// 2005 (c) Juan Antonio Gomez Galvez
// Version autorizada a Microjocs Mobile S.L.
// ================
// ****************

//#ifdef DOJA

//#endif

// <=- <=- <=- <=- <=-






//#ifdef IHD_UNPACK

//#endif






//#ifdef IHD_EXPLODE

//#endif







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
// rms - Engine v2.2 - Rev.10 (30.5.2005)
// ===================
// *******************

//#ifdef RMS_ENGINE
//#else

public byte[] rmsFileInfo;
public void rmsCreate(int store) {}
public void rmsDestroy() {}
public boolean rmsSaveFile(String fileName, byte[] in) {return true;}
public byte[] rmsLoadFile(String fileName) {return null;}
public void rmsDeleteFile(String fileName) {}
public String[] rmsGetDir() {return null;}
public int rmsAvailable() {return 0;}
public int rmsAvailable(String fileName) {return 0;}

//#endif

// <=- <=- <=- <=- <=-











// **************************************************************************//
// Final Clase Bios
// **************************************************************************//







// *******************
// -------------------
// menu - Engine - Rev.10 (20.9.2005)
// ===================
// *******************

// Introduccion
// ============
// Este motor gestiona todo el tema relacionado con los diferentes menus de un juego.
// Intentando que todo funcione de forma generica y se pueda usar facilmente.


// Diferentes menus soportados
// ===========================
// - ML_CICLE
// - ML_OPTION
// - ML_SCREEN
// - ML_SCROLL
// - ML_LIST

// - ML_CICLE:
// Es un menu de opciones donde solo se muestra una opcion en pantalla y estas van rotando tras pulsar una tecla de desplazamiento

// - ML_OPTION:
// Es un menu de opciones donde se muestra como una lista vertical, cada opcion ocupa una linea horizontal

// - ML_SCREEN:
// Es una visualizacion de un texto repartido en varias pantallas. La rutina esta preparada para cortar todo el texto
// en diferentes pantallas de forma 'inteligente'
// Con las teclas de desplazamiento podemas avanzar o retroceder entre estas pantallas

// - ML_SCROLL:
// Es una visualizacion de un texto usando un scroll pixel a pixel que avanza desde el inferior de la pantalla hasta desaparecer por arriba de esta

// - ML_LIST:
// Es una visualizacion de un texto usando una lista que avanza verticalmente linea a linea usando las teclas de desplazamiento


// Como se crea un nuevo menu o listado de texto?
// ==============================================
// Todos los menus se inicializan dentro del metodo 'menuInit(int type)'
// A este metodo se le pasa una identificacion que se define unas lineas mas abajo (ej: static final int MENU_MAIN = 0;)
// Dentro de este metodo, veremos un switch(type) que es el que separa todos los posibles menus definidos
// Dentro de este switch debemos crear nuestro nuevo menu.


// Como se a�ade la informacion del menu o listado de texto?
// =========================================================
// Todos los menus y listados de texto se cargan de datos de la misma forma:

// Primero: Llamamos a 'menuListClear()' para limpiar e inicializar todas la variables del motor.

// Segundo: Agregamos todas las lineas de texto que componen nuestro menu o listado de texto con 'menuListAdd()'
// Existen varios metodos 'menuListAdd()':

// - menuListAdd(int dato, String texto):
// Agregamos una linea de texto con un numero (dato) asociado.

// - menuListAdd(int dato, String[] texto):
// Agregamos varias lineas de texto con un mismos numero (dato) asociado.

// - menuListAdd(int dato, String texto, int dat1):
// Igual que el anterior, pero 'dat1' es un identificador relacionado a un evento (solo para lista de opciones)
// Al seleccionar esta linea de opcion, se lanzara un evento con la ID:dat1

// - menuListAdd(int dato, String[] texto, int dat1, int dat2):
// Igual que anterior pero 'dat2' nos indica que index de 'texto' usar por defecto
// Este metodo es usado para listas de opciones donde un texto puede variar. (Ej: "Sound ON","Sound OFF")
// Al seleccionar esta linea de opcion, el texto rotara internamente y se lanzara un evento con la ID:dat1

// Podemos llamar a 'menuListAdd()' tantas veces queramos, todos los textos se almaceranran y mostraran en el mismo orden.

// Tercero: Indicar que tipo de menu queremos (CICLE, OPTION, SCREEN, etc...):
// - menuListSet_Cicle(): Gestionamos los textos agregados como un menu de opciones ciclico.
// - menuListSet_Option(): Gestionamos los textos agregados como un menu de opciones tipo listado.
// - menuListSet_Screen(): Gestionamos los textos agregados como un listado dividido en pantallas.
// - menuListSet_Scroll(): Gestionamos los textos agregados como un listado tipo scroll. (CUIDADO: suele ser muy lento)
// - menuListSet_List(): Gestionamos los textos agregados como un listado tipo lista con desplazamiento vertical linea a linea.


// Como agregar 'contenido adicional' a los menus
// ==============================================
// Estos son los contenidos posibles:
// - Contador de paginas para el tipo: SCREEN (ej: 1/5 -> pagina 1 de 5)
// - Slider vertical para los tipos: OPTION y LIST
// - logo (imagen: 'logo.png') para mostar arriba del todo de la pantalla en todos los menus.
// - Titulo, un texto que aparece bajo el logo y sobre los menus, funciona con todos los tipos de menu.
// - Separador, una separacion (linea o gr�fico) que aparece entre titulo y menu, funciona con todos los tipos de menu.
// La forma de activar estos 'contenidos adicionales' se hace con un solo metodo:
// menuContentInit(String title, int contents):
// donde title es un texto con el titulo, o null si no queremos titulo
// donde contents es una o varios variables sumadas logicamente:

// CONT_LOGO
// CONT_LOGO_CICLE
// CONT_SEPARATOR
// CONT_VSLIDER
// CONT_PAGES

// - CONT_LOGO:
// Se mostrara la imagen 'logo.png' arriba del todo de la pantalla, quedando la parte inferior para los textos del menu

// - CONT_LOGO_CICLE:
// (Solo es valido para menus tipo ML_CICLE)
// Se mostrara la imagen 'logoCicle.png' en la parte inferior de la pantalla, y dentro de esta los textos del menu de opciones

// - CONT_SEPARATOR:
// Se muestra una linea o imagen ('separator.png') por debajo del titulo

// - CONT_VSLIDER:
// Se muestra un slider vertical en los menus ML_OPTION y ML_LIST, solo si estos necesitan hacer scroll

// - CONT_PAGES:
// Se muestra un contador de paginas abajo del todo, solo para menus ML_SCREEN


// Ejemplo de como se agregan los contenidos, se ha de hacer antes de llamar a 'menuListClear()'
// (ej: menuContentInit(null, CONT_LOGO|CONT_SEPARATOR|CONT_PAGES);)


// Diferentes LAYOUT del motor de menus: (definir SYMBOL para su activacion)
// =========================================================================
// - LAYOUT_MINIMUM: Motor al minimo de recursos
// - LAYOUT_SIMPLE:  Motor con se�alizaciones (contador de paginas, <aspas>, slider vertical) con primitivas
// - LAYOUT_CUSTOM:  Motor con se�alizaciones customizadas con imagenes

// resources necesarios para cada LAYOUT:

// - LAYOUT_MINIMUM:
// No necesita ningun resource.

// - LAYOUT_SIMPLE:
// Todo se realiza con primitivas, excepto el logo del juego que aparece arriba del todo de la pantalla
// este logo se llama 'logo.png'

// - LAYOUT_CUSTOM:
// Todo se realiza con imagenes.
// Las imagenes consisten de:
// - 'logo.png' para ser mostrado en la parte superior de la pantalla en los diferentes menus
// - 'logoCicle.png' para ser usado en el modo CICLE, donde la imagen se usa de marco para las opciones
// - 'menuItems.png' y 'menuItems.cor' son los items de flechas y sliders. Ha de estar reducido con Reductor de Sprites (TileMAX)




// -----------------------------------------------
// Variables staticas para identificar los estados de los menus...
// ===============================================
static final int MENU_MAIN = 0;
static final int MENU_OPTIONS = 1;
static final int MENU_INGAME = 2;
static final int MENU_SCROLL_HELP = 3;
static final int MENU_SCROLL_ABOUT = 4;
static final int MENU_ASK = 5;
static final int MENU_FINAL = 6;
static final int IN_GAME_HELP = 7;
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar las acciones de los menus...
// ===============================================
static final int ACTION_BACK = -10;
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
static final int MENU_ACTION_ASK = 10;
static final int MENU_ACTION_SHOW_OPTIONS = 11;
static final int MENU_ACTION_REPLAY = 12;
static final int MENU_ACTION_REPLAY_OK = 13;
// ===============================================

int menuType;
int menuTypeBack;

int menuLastPos;

int menuActionAsk;

boolean menuExit;

Image menuImg;
boolean menuFadeColor = false;

int ingameHelpText;


//#ifndef LAYOUT_MINIMUM

// menuLogo
Image menuLogoImg;
int menuLogoX;
int menuLogoY;
int menuLogoWidth;
int menuLogoHeight;

// menuTitle
String[] menuTitleStr;
int menuTitleY;
int menuTitleHeight;

// menuPages
int menuPagesY;
int menuPagesHeight;
int menuPagesAspaWidth;

// menuVSlider
int menuVSliderY;
int menuVSliderWidth;
int menuVSliderAspaHeight;

// menuSeparator
int menuSeparatorY;
int menuSeparatorHeight;
Image menuSeparatorImg;

// menuCicle
Image menuCicleImg;
Image menuCicleLogoImg;

//#endif


//#ifdef LAYOUT_CUSTOM
//#endif


// Colores RGB para las primitivas de los contenidos de los menus
int menuLinesRGB = 0x636b8c;	// Color lineas Slider y linea separadora horizontal
int menuSliderRGB = 0x949cad;	// Color de la barrita interior del slider
int menuArrowRGB = 0xc6ced6;	// Color de las aspas/flechas del Slider y contador de p�ginas


// -------------------
// menu Create
// ===================

public void menuCreate()
{
//#ifndef LAYOUT_MINIMUM
	menuLogoImg = loadImage("/logo");
	menuSeparatorImg = loadImage("/separator");
//#endif
}


// -------------------
// menu Destroy
// ===================

public void menuDestroy()
{
//#ifndef LAYOUT_MINIMUM
	menuLogoImg = null;
	menuSeparatorImg = null;
//#endif
}


// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuInit(type, 0);
}

public void menuInit(int type, int pos)
{
	printInit_Font(magicBiosGetFont(0));


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
//		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		menuListSet_Option(pos);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_INGAME:

//		menuContentInit(gameText[TEXT_OPTIONS][0], CONT_LOGO|CONT_SEPARATOR);

		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
	//#ifndef PLAYER_NONE
//		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
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

		menuContentInit(gameText[TEXT_HELP][0], CONT_LOGO|CONT_SEPARATOR|CONT_PAGES);

		menuListClear();
		menuListAdd(0, gameText[TEXT_WAIT]);
		menuListSet_Screen();

		gameDraw();

		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_List();

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;


	case MENU_SCROLL_ABOUT:

		menuContentInit(gameText[TEXT_ABOUT][0], CONT_LOGO|CONT_SEPARATOR|CONT_PAGES);

		menuListClear();
		menuListAdd(0, gameText[TEXT_WAIT]);
		menuListSet_Screen();

		gameDraw();

		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_List();

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;

/*
	case MENU_FINAL:

		menuContentInit(null, CONT_LOGO|CONT_SEPARATOR|CONT_PAGES);

		menuListClear();
		menuListAdd(0, gameText[TEXT_WAIT]);
		menuListSet_Screen();

		gameDraw();

		menuListClear();
		menuListAdd(0, gameText[TEXT_END_GAME]);
		menuListSet_Screen();

		menuTypeBack = MENU_MAIN;

		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
	break;
*/

	case MENU_ASK:

		menuContentInit(null, CONT_LOGO|CONT_SEPARATOR);

		menuListClear();
		menuListAdd(0, gameText[TEXT_ARE_YOU_SURE][0], MENU_ACTION_ASK);
	//#ifdef LAYOUT_MINIMUN
	//#else
		menuListSet_List();
	//#endif

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
	break;
/*
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
*/
	}

	biosStatusOld = biosStatus;
	biosStatus = BIOS_MENU;
}



// -------------------
// menu Contens Init
// ===================

static final int CONT_LOGO = 0x0001;
static final int CONT_LOGO_CICLE = 0x0002;
static final int CONT_SEPARATOR = 0x0004;
static final int CONT_VSLIDER = 0x0008;
static final int CONT_PAGES = 0x0010;

public void menuContentInit(String title, int contents)
{
//#ifndef LAYOUT_MINIMUM

	if ((CONT_LOGO & contents) != 0)
	{
// Inicializamos menuLogo
		menuLogoWidth = canvasWidth;
		menuLogoHeight = menuLogoImg.getHeight();
	}


	if ((CONT_LOGO_CICLE & contents) != 0)
	{
// Inicializamos menuCicleLogo
		menuCicleLogoImg = menuLogoImg;
	}


	if (title != null)
	{
// Inicializamos menuTitle
		menuTitleStr = new String[] {title};
		menuTitleHeight = menuTitleStr.length * printGetHeight();
	}


	if ((CONT_SEPARATOR & contents) != 0)
	{
// Inicializamos menuSeparator
	//#ifdef LAYOUT_SIMPLE
	//#elifdef LAYOUT_CUSTOM
	//#endif
	}


	if ((CONT_VSLIDER & contents) != 0)
	{
// Inicializamos VSlider
	//#ifdef LAYOUT_SIMPLE
	//#elifdef LAYOUT_CUSTOM
	//#endif
	}


	if ((CONT_PAGES & contents) != 0)
	{
// Inicializamos menuPages
	//#ifdef LAYOUT_SIMPLE
	//#elifdef LAYOUT_CUSTOM
	//#endif
	}



// Calculamos areas para cada elemento para menuList
	menuLogoX = menuLogoY = 0;												// Logo Superior
	menuTitleY = menuLogoHeight;											// Titulo de texto
	menuSeparatorY = menuLogoHeight + menuTitleHeight;						// Separador
	menuVSliderY = menuLogoHeight + menuTitleHeight + menuSeparatorHeight;	// Slider Vertical

	menuListX = 0;
	menuListY = menuLogoHeight + menuTitleHeight + menuSeparatorHeight;
	menuListHeight = canvasHeight - menuLogoHeight - menuTitleHeight - menuSeparatorHeight - menuPagesHeight;
	menuListWidth = canvasWidth - menuVSliderWidth;

//#endif
}


// -------------------
// menu Release
// ===================

public void menuRelease()
{
//#ifndef LAYOUT_MINIMUM
// inicializamos variables menu
	menuCicleLogoImg = null;

//	menuLogoImg = null;
	menuLogoWidth = menuLogoHeight = 0;

	menuTitleStr = null;
	menuTitleHeight = 0;

//	menuSeparatorImg = null;
	menuSeparatorHeight = 0;

	menuPagesHeight = 0;

	menuVSliderWidth = 0;
//#endif


// Limpiamos pantalla
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

	case ACTION_BACK:	// Ir "Atras"

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

		case MENU_OPTIONS:

			menuRelease();
			menuInit(MENU_MAIN);
		break;
		}
	break;


	case -3: // Scroll o Screen ha sido abortado por usuario
	case -2: // Scroll o Screen ha llegado al final

		menuRelease();
/*
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
*/
			menuInit(menuTypeBack, menuLastPos);
//		}
	break;

/*
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
*/

	case MENU_ACTION_PLAY:		// Jugar de 0
		menuRelease();

//		if (gameLevelLast == 1) {gameScore = 0;}

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;

	case MENU_ACTION_CONTINUE:	// Continuar

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_OPTIONS:

		menuRelease();

		menuInit(MENU_OPTIONS);
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

		menuRelease();

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
// -----------
// Si el juego tiene musica durante el juego:
//		else
//		if (menuType == MENU_SECOND && soundLoop == 0) {soundPlay(1,0);}
// ===========
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

// -------------------
// menu Draw
// ===================

public void menuDraw()
{

	if (!menuList_ON || !menuListShow) {return;}

	menuListShow = false;


	printInit_Font(magicBiosGetFont(0));


	canvasFillDraw(0);
	if (menuImg!=null)
	{
		showImage(menuImg, 0,0);
	}


	if (menuFadeColor)
	{
		alphaFillDraw(0x80000000, 0,0, canvasWidth, canvasHeight);
	}




//#ifndef LAYOUT_MINIMUM

// menuLogo Draw
	if (menuLogoHeight > 0)
	{
		showImage(menuLogoImg, menuLogoX, menuLogoY);
	}


// menuTitle Draw
	if (menuTitleStr != null)
	{
		printSetArea(0, menuTitleY, canvasWidth, menuTitleHeight);
		printDraw(menuTitleStr[0], 0,0, 0xaaaaaa, PRINT_HCENTER|PRINT_VCENTER|PRINT_OUTLINE);
	}


// menuSeparator Draw
	if (menuSeparatorHeight > 0)
	{
	//#ifdef LAYOUT_SIMPLE
	//#elifdef LAYOUT_CUSTOM
	//#endif
	}


// menuPages Draw
	if (menuPagesHeight > 0)
	{
		printSetArea(0,canvasHeight-menuPagesHeight, canvasWidth, menuTitleHeight);
		String tmpStr = ((menuListTabIndexPos/2)+1)+"/"+(menuListTabIndexSize/2);
		int sizeX = printStringWidth(tmpStr);
		int posX = (canvasWidth-sizeX)/2;
		printDraw(tmpStr, posX,0, 0xaaaaaa, PRINT_VCENTER);

	//#ifdef LAYOUT_SIMPLE
	//#elifdef LAYOUT_CUSTOM
	//#endif
	}


// menuVSlider Draw
	if (menuVSliderWidth > 0)
	{
	//#ifdef LAYOUT_SIMPLE
	//#elifdef LAYOUT_CUSTOM
	//#endif
	}

//#endif


// menuList Draw
	printSetArea(menuListX, menuListY, menuListWidth, menuListHeight);
	menuListDraw();
}










// -------------------
// arrow Draw
// ===================

static final int ARROW_LEFT = 0;
static final int ARROW_RIGHT = 1;
static final int ARROW_UP = 2;
static final int ARROW_DOWN = 3;

public void arrowDraw(int x, int y, int width, int height, int dir)
{
//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
//#endif

//	putColor(0xffffff); scr.fillRect(x,y,width,height);		// com.mygdx.mongojocs.sextron.Debug: rellena cuadrado donde dibujar Arrow

    putColor(menuArrowRGB);

	switch (dir)
	{
	case ARROW_RIGHT:
		for (int i=0 ; i<width ; i++)
		{
			int size = height-(i*2); if (size<0) {size=0;}
			scr.fillRect(x+i, y+i, 1, size);
		}
	break;

	case ARROW_LEFT:
		for (int i=0 ; i<width ; i++)
		{
			int size = height-(i*2); if (size<0) {size=0;}
			scr.fillRect(x+width-1-i, y+i, 1, size);
		}
	break;

	case ARROW_DOWN:
		for (int i=0 ; i<height ; i++)
		{
			int size = width-(i*2); if (size<0) {size=0;}
			scr.fillRect(x+i, y+i, size, 1);
		}
	break;

	case ARROW_UP:
		for (int i=0 ; i<height ; i++)
		{
			int size = width-(i*2); if (size<0) {size=0;}
			scr.fillRect(x+i, y+height-1-i, size, 1);
		}
	break;
	}

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
// Variables staticas para identificar los textos, libres desde el 20...
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
final static int TEXT_WAIT = 11;
final static int TEXT_LEVEL = 12;
final static int TEXT_LEVEL_COMPLETED = 13;
final static int TEXT_CONGRATULATIONS = 14;
final static int TEXT_ARE_YOU_SURE = 15;
final static int TEXT_SOFTKEYS = 16;
final static int TEXT_HELP_SCROLL = 17;
final static int TEXT_ABOUT_SCROLL = 18;

final static int TEXT_LIVES = 19;
final static int TEXT_TIME = 20;
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int GAME_START = 0;
final static int GAME_LOGOS = 5;

final static int GAME_MENU_START = 10;
final static int GAME_MENU_MAIN = 11;
final static int GAME_MENU_MAIN_END = 12;

final static int GAME_MENU_INGAME = 25;

final static int GAME_MENU_GAMEOVER = 30;

final static int GAME_EXIT = 35;

final static int GAME_PLAY = 20;
final static int GAME_PLAY_INIT = 21;
final static int GAME_PLAY_TICK = 22;

final static int GAME_COMPLETED = 23;
// ===============================================

int gameAlphaExit;

Image gameFontImg;
byte[] gameFontCor;


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
/*
//#ifndef PLAYER_NONE

	//#ifdef PLAYER_OTA

		soundCreate( new String[] {
			"024A3A7D11BDB5A5B9BD7DB585A5B881D1A1940400D91E6E0A2702CC2702CD26D2302702902CC28C28C2682702702902C826849B41B8288C09C126E0A23026D22D23049A69A81A81B8288C126E0A23027029027049B8289C0B309C0B349B48C09C0A40B30A30A309A09C09C0A40B209A126D06E0A23027049B8288C09B48B48C1269A6A06A06E0A23049B8288C09C0A409C0A40B409C08C0B20B40C40B40A40D30C40D50C40DA000",	// 0 - Musica de la caratula
			"024A3A7D11BDB5A5B9BD7DC1BDB995C88199A4040006568A354000",				// 1 - 
			"024A3A7D11BDB5A5B9BD7DC9BD8985C88199A40400111E6A882D4314495A1AA000",	// 2 - 
			"024A3A7D11BDB5A5B9BD7DC1BDB995C88199A4040006568A354000",				// 3 - 
			});

	//#elifdef PLAYER_SAMSUNG

		soundCreate( new String[] {
			"/Astro_title.mmf",		// 0 - Musica de la caratula
			"/Astro_item.mmf",		// 1 - 
			"/Astro_muerte.mmf",	// 2 - 
			"/Astro_level.mmf",		// 3 - 
			});

	//#elifdef PLAYER_SHARP

		soundCreate( new String[] {
			"/Astro_title",		// 0 - Musica de la caratula
			"/Astro_item",		// 1 - 
			"/Astro_muerte",	// 2 - 
			"/Astro_level",		// 3 - 
			}, new int[] {7,4,2,1});

	//#else

		soundCreate( new String[] {
			"/Astro_title.mid",		// 0 - Musica de la caratula
			"/Astro_item.mid",		// 1 - 
			"/Astro_muerte.mid",	// 2 - 
			"/Astro_level.mid",		// 3 - 
			});

	//#endif


	//#ifdef SG-D410
		durations = new long[] {12000, 5000, 6000, 8000};	// Se ha de poner a MANO los milis que dura cada midi.
	//#endif

//#endif
*/
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

		logosInit(new String[] {"/private"}, new int[] {0xffffff}, 3000);
		gameStatus = GAME_MENU_START;
	break;
// ===============================



// -------------------------------
// Gestor de menus del source base
// ===============================
	case GAME_MENU_START:

		menuCreate();
		menuImg = loadImage("/caratula");

		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:

		menuInit(MENU_MAIN);
		soundPlay(0,0);
		gameStatus = GAME_MENU_MAIN_END;
	break;

	case GAME_MENU_MAIN_END:

		soundStop();
		menuImg = null;

		gameStatus = GAME_PLAY;
	break;



	case GAME_MENU_INGAME:

		menuInit(MENU_INGAME);
		gameStatus = GAME_PLAY_TICK;
	break;

	case GAME_MENU_GAMEOVER:


		canvasFillTask(0x000000);
		canvasTextTask(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, PRINT_VCENTER|PRINT_HCENTER | PRINT_OUTLINE);
		gameDraw();

		waitTime(3000);

		gameStatus = GAME_MENU_START;
	break;
// ===============================




// -------------------------------
// Fade-Out al salir del juego
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
// Gestor del motor del juego
// ===============================
	case GAME_PLAY:

	// Pintamos la pantalla de negro y mostramos un texto de "Cargando..."
		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, PRINT_VCENTER|PRINT_HCENTER);
	// Forzmos un repaint (refresco de pantalla para que se se borre la pantalla y aparezca el texto.
		gameDraw();

	// Cargamos e inicializamos todos los recursos referentes al juego. Este proceso solo se hace una vez.
		playCreate();
		gameStatus++;

	case GAME_PLAY_INIT:

	// Pintamos la pantalla de negro y mostramos un texto de "Cargando..."
		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, PRINT_VCENTER|PRINT_HCENTER);
	// Forzmos un repaint (refresco de pantalla para que se se borre la pantalla y aparezca el texto.
		gameDraw();

	// Cargamos e inicializamos todos los recursos para el nivel actual (faseLevel)
		playInit();
	// Este Flag, nos indica el motivo por el cual debemos terminar el juego (1:Siguiente nivel, 2:muerte, 3:game over forzado desde menu)
		playExit = 0;

	// Mostramos el texto de 'menu' en las softKeys
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);

// -----------
// Si el juego tiene musica durante el juego:
//		soundPlay(1,0);
// ===========

		gameStatus++;
	break;

	case GAME_PLAY_TICK:

	//#ifdef CHEATS
	//#endif

	// Si pulsamos la softKey de ir a menu, pasamos al ESTADO de menu durante juego.
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_INGAME; break;}

	// Procesamos un TICK del juego, si true: debemos parar el juego...
		if ( !playTick() ) {break;}

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		soundStop();

	// Desalojamos todos los recursos cargados en 'playInit()'
		playRelease();

	// Colocamos el ESTADO en 'GAME_PLAY_INIT'
		gameStatus--;

		switch (playExit)
		{
		case 1:	// Nivel Completado, pasamos al siguiente nivel

			if (++JugarLevel > 4) {JugarLevel=0;}
			JugarNivel++;
			gameStatus = GAME_COMPLETED;
		break;

		case 2:	// Una vida menos y regresamos al inicio del nivel
		break;

		case 3:	// Producir Game Over

		// Destruimos todos los recursos cargados en 'playCreate()'
			playDestroy();

		// Saltamos al ESTADO de 'GAME OVER'
			gameStatus = GAME_MENU_GAMEOVER;
		break;
		}

		playExit=0;
	break;


	case GAME_COMPLETED:

		canvasFillTask(0);
		canvasImg = ChatisImg;
		ChatisImg = null;

		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);

		gameStatus++;
	break;

	case GAME_COMPLETED + 1:

		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			gameStatus = GAME_PLAY_INIT;

			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		}
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

// <=- <=- <=- <=- <=-









// *******************
// -------------------
// play - Engine
// ===================
// *******************
// -------------------


int JugarLevel;
int JugarVidas;
int JugarPuntos;
int JugarNivel;

int JugarTime;
boolean JugarStop;
boolean JugarSlow;
boolean JugarInmune;

int JugarCiento;


// --------------------------------------------------
// Variables para Customizar el Juego segun el Device
// ==================================================

//#ifdef S30
//#elifdef S40
//#elifdef S60
	static final int FaseTopX=16;
	static final int FaseTopY=16;

	static final int FaseSizeX=72;
	static final int FaseSizeY=72;

	static final int Factor=2;

	static final int extraX = 60;
	static final int extraY = 172;

//#elifdef S80
//#endif





final int Borde_RGB=0xFF0000;
final int Corte_RGB=0x0000FF;




Image BackImg;
Graphics BackGfx;


Image SpritesImg;
Image ChatisImg;
Image MarcoImg;
Image ExtraImg;
Image FontImg;

byte[] SpritesCor;
byte[] ExtraCor;





int playExit;
boolean playShow = false;

// -------------------
// play Create
// ===================

public void playCreate()
{
	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			if (BackImg == null)
			{
				BackImg = new Image();
				BackImg._createImage(canvasWidth, canvasHeight);
				BackGfx = BackImg.getGraphics();
			}
		}
	});

	JugarLevel=0;
	JugarNivel=1;

	JugarVidas=3;

	JugarPuntos=0;


	MarcoImg = loadImage("/marco");
	SpritesImg = loadImage("/sprites");
	ExtraImg = loadImage("/extra");
	FontImg = loadImage("/font");

	SpritesCor = loadFile("/sprites.cor");
	ExtraCor = loadFile("/extra.cor");


	FaseINI();

	playShow = true;
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
//#ifdef NK-s60
	BackImg = null;
//#endif

	ChatisImg = null;

	MarcoImg = null;
	SpritesImg = null;
	ExtraImg = null;
	FontImg = null;

	SpritesCor = null;
	ExtraCor = null;

	playShow = false;
}

// -------------------
// play Init
// ===================

public void playInit()
{
	JugarStop=false;
	JugarSlow=false;
	JugarInmune=false;
	JugarTime=0;

	JugarCiento=0;

	FaseSET();



	ChatisImg = loadImage("/00"+(JugarLevel+1));

	BackUpdate();



	soundPlay(0,1);

}

// -------------------
// play Release
// ===================

public void playRelease()
{
//	System.gc();
//	ChatisImg = null;
	System.gc();
}

// -------------------
// play Tick
// ===================

public boolean playTick()
{


	FaseRUN();

	if (JugarStop || JugarSlow || JugarInmune) {if (--JugarTime<0) {JugarTime=0; JugarStop=false; JugarSlow=false; JugarInmune=false;}}


	
	if (playExit!=0) {return true;}		// Debemos salir de fa fase??  Muerte, siguinte nivel, etc...

	playShow = true;		// Notificamos para el "repaint()" se refresque el play Engine. (motor del juego)

	return false;
}

// -------------------
// play Draw
// ===================

public void playDraw()
{

	fillDraw(0, 0,0, canvasWidth, canvasHeight);


	showImage(BackImg,  0,0);


// Imprimimos AnimSprites
// ----------------------
	AniSprIMP(AniSprs, AniSprP, AniSprC);


// Imprimimos Camino Prot
// -----------------------
	if (ProtMode==10) {DrawLine(ProtCutX,ProtCutY,Corte_RGB);}


// Imprimimos Protagonista
// -----------------------

	if ( !JugarInmune || (JugarTime & 0x01) == 0)
	{
	spriteDraw(SpritesImg, FaseTopX+(ProtX*Factor)-8, FaseTopY+(ProtY*Factor)-8,  36+ProtFrame, SpritesCor);
	}



// Imprimimos Time
// ------------------
/*
	PrinterINI(25,180,  32, 25);

	PrinterSET(FontImg, 0,0,  8,8,  16,  FontSizeX);

	if (JugarTime==0)
	{
	PrinterIMP(";"+Integer.toString(JugarVidas), 0x1);
	} else {
	PrinterIMP(Integer.toString(JugarTime), 0x1);
	}

	PrinterIMP(Integer.toString(JugarPuntos), 0x1);
	PrinterIMP(Integer.toString(JugarCiento)+"\\", 0x1);

	PrinterEND();
*/


//#ifdef S40
//#elifdef S60
	printSetArea(25, 182,  38, 27);
//#endif

	printInit_Font(gameFontImg, gameFontCor, 8, 0x30, 1);
	printDraw(JugarVidas + " " + gameText[TEXT_LIVES][0], 0,0, 0x000000, PRINT_HCENTER);


}


int TimeX;
int TimeY;


public void DrawLine(int X, int Y, int RGB)
{
	scr.setClip(0,0, canvasWidth,canvasHeight);

	int Mode=-1;
	int IniX=X, IniY=Y;
	int Col=FaseMap[Y*FaseSizeX+X];

	boolean Bucle=true;

	while (Bucle)
	{
		int NowX=X, NowY=Y;

		if ( Mode!=3 && FaseCheck(X+1,Y, Col) ) { X++; Mode=1;} else
		if ( Mode!=0 && FaseCheck(X,Y+1, Col) ) { Y++; Mode=2;} else
		if ( Mode!=1 && FaseCheck(X-1,Y, Col) ) { X--; Mode=3;} else
		if ( Mode!=2 && FaseCheck(X,Y-1, Col) ) { Y--; Mode=0;} else {return;}


		int Desp=2;

		while (true)
		{
		if (IniX==X && IniY==Y) {Bucle=false; break;}

		if ( Mode==0 && FaseCheck(X,Y-1, Col) ) {Y--;} else
		if ( Mode==1 && FaseCheck(X+1,Y, Col) ) {X++;} else
		if ( Mode==2 && FaseCheck(X,Y+1, Col) ) {Y++;} else
		if ( Mode==3 && FaseCheck(X-1,Y, Col) ) {X--;} else {break;}
		Desp++;
		}


		scr.setColor(RGB);
		switch (Mode)
		{
		case 0: scr.fillRect(FaseTopX+NowX*Factor,FaseTopY+(NowY-Desp+1)*Factor, 2,Desp*Factor);	break;
		case 2: scr.fillRect(FaseTopX+NowX*Factor,FaseTopY+(NowY)*Factor, 2,Desp*Factor); break;
		case 1: scr.fillRect(FaseTopX+(NowX)*Factor,FaseTopY+NowY*Factor, Desp*Factor,2);	break;
		case 3: scr.fillRect(FaseTopX+(NowX-Desp+1)*Factor,FaseTopY+NowY*Factor, Desp*Factor,2);	break;
		}
	}


}







public void BackUpdate()
{

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {


	scr = BackGfx;

//	CanvasFill(0);



// Imprimimos Rejilla
// -------------------
	for (int y=0 ; y<(FaseSizeY*Factor) ; y+=16)
	{
		for (int x=0 ; x<(FaseSizeX*Factor) ; x+=16)
		{
		spriteDraw(SpritesImg, FaseTopX+x,FaseTopY+y,  54+JugarLevel, SpritesCor, FaseTopX, FaseTopY, FaseSizeX*Factor, FaseSizeY*Factor);
		}
	}



// Imprimimos Marco
// -------------------
	showImage(MarcoImg, 0,0);



// Imprimimos Chatis
// -------------------
	for (int y=0 ; y<FaseSizeY ; y++)
	{
		for (int x=0 ; x<FaseSizeX ; x++)
		{
			if (FaseMap[y*FaseSizeX+x]==2)
			{
			int CoorX=x++, SizeX=1;
			while (x<FaseSizeX && FaseMap[(y*FaseSizeX)+x]==2 ) {SizeX++; x++;}
			showImage(ChatisImg, CoorX*Factor, y*Factor, SizeX*Factor, 2, FaseTopX+(CoorX*Factor), FaseTopY+(y*Factor));
			}
		}
	}


// Imprimimos Cuadro Exterior
// ---------------------------

Buscar:
	{
		for (int y=0 ; y<FaseSizeY-1 ; y++)
		{
			for (int x=0 ; x<FaseSizeX-1 ; x++)
			{
				if (FaseMap[y*FaseSizeX+x]==1 && FaseMap[y*FaseSizeX+x+1]==1 && FaseMap[(y+1)*FaseSizeX+x]==1)
				{
				DrawLine(x,y,Borde_RGB);
				break Buscar;
				}
			}
		}
	}




// Imprimimos Curva %
// ------------------
	int Por=7-((JugarCiento*8)/90); if (Por < 0) {Por=0;} else if (Por > 7) {Por=7;}
	spriteDraw(ExtraImg, extraX, extraY,  Por, ExtraCor);




/*
// Imprimimos Textos
// ------------------

	PrinterINI(87,188,  32, 20);

	PrinterSET(FontImg, 0,0,  8,8,  16,  FontSizeX);

	PrinterIMP("NIVEL "+Integer.toString(JugarNivel),  0xA);

	PrinterEND();
*/

		}
	});
}

// <=- <=- <=- <=- <=-













// *******************
// -------------------
// Fase - Engine
// ===================
// *******************

byte[] FaseMap;

int FaseChica;

// -------------------
// Fase INI
// ===================

public void FaseINI()
{
//	LoadFile(AnimMap, "/Data/Anims.map");
}


// -------------------
// Fase END
// ===================

public void FaseEND()
{
	ItemEND();
	BolaEND();
	PlasmaEND();

	AniSprEND();

	ProtEND();

	System.gc();
}


// -------------------
// Fase SET
// ===================

public void FaseSET()
{
	FaseMap = new byte[FaseSizeX*FaseSizeY];


	AniSprINI();

	ItemINI();
	BolaINI();
	PlasmaINI();


	ProtINI();


	ProtSET();


	ItemSET( rnd(FaseSizeX-40)+20,rnd(FaseSizeY-40)+20,  rnd(3) );


	BolaSET( rnd(FaseSizeX-20)+10, rnd(FaseSizeY-20)+10,  1, 1,  0);

	if (JugarNivel>1)
	{
	BolaSET( rnd(FaseSizeX-20)+10, rnd(FaseSizeY-20)+10,  1,-1,  1);
	}

	if (JugarNivel>2)
	{
	PlasmaSET( rnd(FaseSizeX-20)+10, rnd(FaseSizeY-20)+10,  -1,-1);
	}

	FaseCuboDraw(0,0, FaseSizeX,FaseSizeY, 1);

}


// -------------------
// Fase RUN
// ===================

public void FaseRUN()
{
	ItemRUN();

	if ( !JugarSlow || (JugarTime & 0x01) == 0 )
	{
	BolaRUN();
	PlasmaRUN();
	}

	AniSprRUN();

	ProtRUN();
}




public void FaseCuboDraw(int X, int Y, int SizeX, int SizeY, int Data)
{
	for (int x=0 ; x<SizeX ; x++)
	{
	FaseMap[Y*FaseSizeX+X+x]=(byte)Data;
	FaseMap[(Y+SizeY-1)*FaseSizeX+X+x]=(byte)Data;
	}

	for (int y=0 ; y<SizeY ; y++)
	{
	FaseMap[(Y+y)*FaseSizeX+X]=(byte)Data;
	FaseMap[(Y+y)*FaseSizeX+X+SizeX-1]=(byte)Data;
	}
}




public boolean FaseCheck(int X, int Y, int Data)
{
	if (X < 0  ||  X >= FaseSizeX  ||  Y < 0  ||  Y >= FaseSizeY) {return false;}

	return FaseMap[Y*FaseSizeX + X] == Data;
}





public void FaseFind(int IniX, int IniY, int FinX, int FinY)
{


// Corremos Camino A
// -----------------
	byte Dato=10;

	int A=0;

	int X=IniX; int Y=IniY;

	while (X!=FinX || Y!=FinY)
	{
	FaseMap[Y*FaseSizeX + X]=Dato;
	if ( X<FaseSizeX-1 && FaseMap[Y*FaseSizeX + X + 1] == 1 ) {X++;} else
	if ( X>0           && FaseMap[Y*FaseSizeX + X - 1] == 1 ) {X--;} else
	if ( Y<FaseSizeY-1 && FaseMap[(Y+1)*FaseSizeX + X ] == 1 ) {Y++;} else
	if ( Y>0           && FaseMap[(Y-1)*FaseSizeX + X ] == 1 ) {Y--;}
	A++;
	}


// Corremos Camino A
// -----------------
	Dato=11;

	int B=0;

	X=IniX; Y=IniY;

	while (X!=FinX || Y!=FinY)
	{
	FaseMap[Y*FaseSizeX + X]=Dato;
	if ( X>0           && FaseMap[Y*FaseSizeX + X - 1] == 1 ) {X--;} else
	if ( X<FaseSizeX-1 && FaseMap[Y*FaseSizeX + X + 1] == 1 ) {X++;} else
	if ( Y>0           && FaseMap[(Y-1)*FaseSizeX + X ] == 1 ) {Y--;} else
	if ( Y<FaseSizeY-1 && FaseMap[(Y+1)*FaseSizeX + X ] == 1 ) {Y++;}
	B++;
	}



// Pintamos Nuevo Camino
// ---------------------

	int NewCamino= (A < B)? 11:10;

	FaseMap[IniY*FaseSizeX + IniX]=3;
	FaseMap[FinY*FaseSizeX + FinX]=3;


	FaseChica=0;

	for (int Dir=0, y=0 ; y<FaseSizeY ; y++)
	{
		for (int Mode=0, x=0 ; x<FaseSizeX ; x++)
		{
		byte Data=FaseMap[Dir];

		if (Data==NewCamino) {FaseMap[Dir]=1; Data=1;}

		switch (Mode)
		{
		case 0:
			if ( Data >= 3 ) {Mode=1; FaseMap[Dir] = (byte)(Data==3? 1:2);}
		break;

		case 1:
			if (Data==1) {Mode=0; break;}

			FaseMap[Dir] = (byte)(Data==3? 1:2);

			if ( x<FaseSizeX-1 && Data >= 3 && FaseMap[Dir+1] < 3 )
			{
			if ( y==0 || FaseMap[(Dir-FaseSizeX)+1] != 2 ) {Mode=0;}
			}
		break;
		}

		if (FaseMap[Dir++] == 2) {FaseChica++;}

		}
	}

}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Prot - Engine
// ====================
// ********************

int ProtX;
int ProtY;
int ProtSide, ProtSideOld;
int ProtMode;

int ProtAnim;

int ProtFrame;

int ProtOrigX;
int ProtOrigY;

int ProtCutX;
int ProtCutY;

int[][] ProtGiro = { {2,3,6,1},{3,4,5,0},{2,5,6,7},{1,4,7,0} };

// ---------------
// Prot INI
// ===============

public void ProtINI()
{
}


// ---------------
// Prot END
// ===============

public void ProtEND()
{
}


// ---------------
// Prot SET
// ===============

public void ProtSET()
{
	ProtX=0;
	ProtY=0;

	ProtMode=0;
	ProtSide=2;
	ProtSideOld=2;
}


// ---------------
// Prot RUN
// ===============

public void ProtRUN()
{
	if (ProtMode >= 80) {ProtMorirRUN(); return;}


	switch (ProtSide)
	{
	case 0:
		if (ProtY < FaseSizeY-1 && (ProtY % 3) != 0 )
		{
		keyX=0;
		keyY=-1;
		} else {
		if (keyX != 0 ) {ProtSide= keyX==1? 1:3;} else if (keyY != 0 ) {ProtSide= keyY==1? 2:0;}
		}
	break;

	case 2:
		if (ProtY < FaseSizeY-1 && (ProtY % 3) != 0 )
		{
		keyX=0;
		keyY=1;
		} else {
		if (keyX != 0 ) {ProtSide= keyX==1? 1:3;} else if (keyY != 0 ) {ProtSide= keyY==1? 2:0;}
		}
	break;

	case 3:
		if (ProtX < FaseSizeX-1 && (ProtX % 3) != 0 )
		{
		keyX=-1;
		keyY=0;
		} else {
		if (keyX != 0 ) {ProtSide= keyX==1? 1:3;} else if (keyY != 0 ) {ProtSide= keyY==1? 2:0;}
		}
	break;

	case 1:
		if (ProtX < FaseSizeX-1 && (ProtX % 3) != 0 )
		{
		keyX=1;
		keyY=0;
		} else {
		if (keyX != 0 ) {ProtSide= keyX==1? 1:3;} else if (keyY != 0 ) {ProtSide= keyY==1? 2:0;}
		}
	break;
	}


	ProtFrame= new int[] {2,4,6,0}[ProtSide];


	if (ProtSide!=ProtSideOld)
	{
	ProtFrame=ProtGiro[ProtSideOld][ProtSide];
	}


	ProtSideOld=ProtSide;

	int DestX=ProtX+keyX; int DestY=ProtY+keyY;	if (keyX!=0) {DestY=ProtY;}

	int Data=-1; if (keyX != 0 || keyY != 0) {if (DestX >= 0 && DestX < FaseSizeX && DestY >= 0 && DestY < FaseSizeY) {Data=FaseMap[DestY*FaseSizeX + DestX];}}


	switch (ProtMode)
	{
	case 0:		// Normal

		switch (Data)
		{
		case 1:
			ProtX=DestX;
			ProtY=DestY;
		break;

		case 0:
			ProtOrigX=ProtX;
			ProtOrigY=ProtY;

			ProtX=DestX;
			ProtY=DestY;

			ProtCutX=DestX;
			ProtCutY=DestY;

			FaseMap[ProtY*FaseSizeX+ProtX]=3;

			ProtMode=10;
		break;
		}

	break;	

	case 10:	// Pisando...

		FaseMap[ProtY*FaseSizeX+ProtX]=3;

		switch (Data)
		{
		case 0:
			if ( ProtSide != 2 && FaseCheck(DestX, DestY-1, 3) ) {ProtBack();break;}
			if ( ProtSide != 0 && FaseCheck(DestX, DestY+1, 3) ) {ProtBack();break;}
			if ( ProtSide != 3 && FaseCheck(DestX+1, DestY, 3) ) {ProtBack();break;}
			if ( ProtSide != 1 && FaseCheck(DestX-1, DestY, 3) ) {ProtBack();break;}

			ProtX=DestX;
			ProtY=DestY;
		break;

		case 1:
			if (ProtOrigX==DestX && ProtOrigY==DestY) {FaseMap[ProtY*FaseSizeX+ProtX]=0;}

			ProtX=DestX;
			ProtY=DestY;

			if (ProtOrigX!=ProtX || ProtOrigY!=ProtY)
			{
			soundPlay(1,1);

			FaseFind(ProtOrigX, ProtOrigY, ProtX, ProtY);

			JugarPuntos+=FaseChica;
			JugarCiento=(FaseChica*100)/FaseMap.length;
			if (JugarCiento >= 75) {JugarCiento=100; playExit=1;}

			BackUpdate();
			}

			ProtMode=0;
		break;

		case 3:
			FaseMap[ProtY*FaseSizeX+ProtX]=0;
			ProtX=DestX;
			ProtY=DestY;
		break;

		}

	break;	
	}

}




public void ProtBack()
{
	if (ProtMode>=80) {return;}

	vibraInit(250);
	soundPlay(3,1);

	ProtFrame=-1;

	ProtAnim = AniSprSET(-1, ProtX, ProtY, 44, 4, 1, 0x007);
	AniSprs[ProtAnim].Repetir=3;

	for (int i=0 ; i<FaseMap.length ; i++) {if (FaseMap[i]==3) {FaseMap[i]=0;}}

	ProtX=ProtOrigX;
	ProtY=ProtOrigY;

	ProtMode=80;
}




public void ProtMorirRUN()
{
	if (AniSprs[ProtAnim].Pause==0)
	{
		AniSprRES(ProtAnim);
		if (--JugarVidas < 0) {playExit = 3;} else {ProtMode=0;}
	}
}



// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Anim Sprite - Engine
// ====================
// ********************

final int AniSprMAX=32;
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



public void AniSprIMP(AnimSprite[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
	switch (s[i].Bank)
	{
	case 0:
	spriteDraw(SpritesImg,  FaseTopX+((s[i].CoorX)*Factor)-8, FaseTopY+((s[i].CoorY)*Factor)-8,  s[i].FrameIni + s[i].FrameAct, SpritesCor);
	break;	
	}

	}
}

// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Bola - Engine
// ====================
// ********************

final int BolaMAX=8;
int[][] Bolas;
int BolaC=0;

// ---------------
// Bola SET
// ===============

public void BolaSET(int CoorX, int CoorY, int AddX, int AddY, int Fr)
{
	if (BolaC == BolaMAX) {return;}

	int i=0; for (; i<BolaMAX ; i++) {if (Bolas[i] == null) {break;}}

	int Anim = AniSprSET(-1, CoorX,CoorY, Fr==0? 0:12, Fr==0? 7:3, 1, 0x001);


	Bolas[i] = new int[5];

	Bolas[i][0] = Anim;
	Bolas[i][1] = Fr;
	Bolas[i][2] = AddX;
	Bolas[i][3] = AddY;

	BolaC++;
}


// ---------------
// Bola Play
// ===============

public boolean BolaPlay(int[] Data)
{
	if (!JugarInmune)
	{
	for (int y=-1 ; y<2 ; y++) {for (int x=-1 ; x<2 ; x++)
	{ if ( FaseCheck(AniSprs[Data[0]].CoorX+(Data[2]*2)+x, AniSprs[Data[0]].CoorY+y, 3) ) {ProtBack(); return false;} }}
	}


	if ( !FaseCheck(AniSprs[Data[0]].CoorX+(Data[2]*2), AniSprs[Data[0]].CoorY, 0) ) {Data[2]*=-1;}
	if (!JugarStop) {AniSprs[Data[0]].CoorX+=Data[2];}


	if (!JugarInmune)
	{
	for (int y=-1 ; y<2 ; y++) {for (int x=-1 ; x<2 ; x++)
	{ if ( FaseCheck(AniSprs[Data[0]].CoorX+x, AniSprs[Data[0]].CoorY+(Data[3]*2)+y, 3) ) {ProtBack(); return false;} }}
	}


	if ( !FaseCheck(AniSprs[Data[0]].CoorX, AniSprs[Data[0]].CoorY+(Data[3]*2), 0) ) {Data[3]*=-1;}
	if (!JugarStop) {AniSprs[Data[0]].CoorY+=Data[3];}


	if ( FaseCheck(AniSprs[Data[0]].CoorX, AniSprs[Data[0]].CoorY, 2) ) {AniSprSET(Data[0], AniSprs[Data[0]].CoorX, AniSprs[Data[0]].CoorY, Data[1]==0? 7:15, Data[1]==0? 5:6 , 2, 0x000); return true;}

	return false;
}

// ---------------
// Bola INI
// ===============

public void BolaINI()
{
	BolaC = 0;
	Bolas = new int[BolaMAX][];
}

// ---------------
// Bola END
// ===============

public void BolaEND()
{
	Bolas = null;
	BolaC = 0;
}

// ---------------
// Bola RUN
// ===============

public void BolaRUN()
{
	for (int i=0 ; i<BolaMAX ; i++)
	{
	if (Bolas[i] != null) {if ( BolaPlay(Bolas[i]) ) {Bolas[i]=null; BolaC--;}}
	}
}

// <=- <=- <=- <=- <=-








// ********************
// --------------------
// Plasma - Engine
// ====================
// ********************

final int PlasmaMAX=8;
int[][] Plasmas;
int PlasmaC=0;

// ---------------
// Plasma SET
// ===============

public void PlasmaSET(int CoorX, int CoorY, int AddX, int AddY)
{
	if (PlasmaC == PlasmaMAX) {return;}

	int i=0; for (; i<PlasmaMAX ; i++) {if (Plasmas[i] == null) {break;}}

	int Anim = AniSprSET(-1, CoorX,CoorY, 24, 4, 0, 0x001);


	Plasmas[i] = new int[5];

	Plasmas[i][0] = Anim;
	Plasmas[i][1] = 0;
	Plasmas[i][2] = AddX;
	Plasmas[i][3] = AddY;

	PlasmaC++;
}


// ---------------
// Plasma Play
// ===============

public boolean PlasmaPlay(int[] Data)
{
	if (!JugarInmune)
	{
	for (int y=-1 ; y<2 ; y++) {for (int x=-1 ; x<2 ; x++)
	{ if ( FaseCheck(AniSprs[Data[0]].CoorX+(Data[2]*2)+x, AniSprs[Data[0]].CoorY+y, 3) ) {ProtBack(); return false;} }}
	}


	if ( !FaseCheck(AniSprs[Data[0]].CoorX+(Data[2]*2), AniSprs[Data[0]].CoorY, 0) ) {Data[2]*=-1;}
	if (!JugarStop) {AniSprs[Data[0]].CoorX+=Data[2];}


	if (!JugarInmune)
	{
	for (int y=-1 ; y<2 ; y++) {for (int x=-1 ; x<2 ; x++)
	{ if ( FaseCheck(AniSprs[Data[0]].CoorX+x, AniSprs[Data[0]].CoorY+(Data[3]*2)+y, 3) ) {ProtBack(); return false;} }}
	}


	if ( !FaseCheck(AniSprs[Data[0]].CoorX, AniSprs[Data[0]].CoorY+(Data[3]*2), 0) ) {Data[3]*=-1;}
	if (!JugarStop) {AniSprs[Data[0]].CoorY+=Data[3];}


	if ( FaseCheck(AniSprs[Data[0]].CoorX, AniSprs[Data[0]].CoorY, 2) ) {AniSprSET(Data[0], AniSprs[Data[0]].CoorX, AniSprs[Data[0]].CoorY, 28, 4 , 2, 0x000); return true;}

	return false;
}

// ---------------
// Plasma INI
// ===============

public void PlasmaINI()
{
	PlasmaC = 0;
	Plasmas = new int[PlasmaMAX][];
}

// ---------------
// Plasma END
// ===============

public void PlasmaEND()
{
	Plasmas = null;
	PlasmaC = 0;
}

// ---------------
// Plasma RUN
// ===============

public void PlasmaRUN()
{
	for (int i=0 ; i<PlasmaMAX ; i++)
	{
	if (Plasmas[i] != null) {if ( PlasmaPlay(Plasmas[i]) ) {Plasmas[i]=null; PlasmaC--;}}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Item - Engine
// ====================
// ********************

final int ItemMAX=3;
int[][] Items;
int ItemC=0;

// ---------------
// Item SET
// ===============

public void ItemSET(int CoorX, int CoorY, int Type)
{
	if (ItemC == ItemMAX) {return;}

	int i=0; for (; i<ItemMAX ; i++) {if (Items[i] == null) {break;}}

	int Anim = AniSprSET(-1, CoorX,CoorY, 48+(Type*2), 2, 0, 0x001);

	Items[i] = new int[2];

	Items[i][0] = Anim;
	Items[i][1] = Type;

	ItemC++;
}


// ---------------
// Item Play
// ===============

public boolean ItemPlay(int[] Data)
{
	if ( !FaseCheck(AniSprs[Data[0]].CoorX, AniSprs[Data[0]].CoorY, 2) ) {return false;}

	AniSprRES(Data[0]);

	JugarSlow=false;
	JugarStop=false;
	JugarInmune=false;

	switch (Data[1])
	{
	case 0:
		JugarSlow=true;
		JugarTime=150;
	break;	

	case 1:
		JugarStop=true;
		JugarTime=150;
	break;	

	case 2:
		JugarInmune=true;
		JugarTime=150;
	break;	
	}

	return true;
}

// ---------------
// Item INI
// ===============

public void ItemINI()
{
	ItemC = 0;
	Items = new int[ItemMAX][];
}

// ---------------
// Item END
// ===============

public void ItemEND()
{
	Items = null;
	ItemC = 0;
}

// ---------------
// Item RUN
// ===============

public void ItemRUN()
{
	for (int i=0 ; i<ItemMAX ; i++)
	{
	if (Items[i] != null) {if ( ItemPlay(Items[i]) ) {Items[i]=null; ItemC--;}}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Bufer - Engine
// ====================
// ********************

final int BuferMAX=300;
int[][] Bufers;
int BuferC=0;

// ---------------
// Bufer SET
// ===============

public void BuferSET(int CoorX, int CoorY)
{
	if (BuferC == BuferMAX) {return;}

	int i=0; for (; i<BuferMAX ; i++) {if (Bufers[i] == null) {break;}}

	Bufers[i] = new int[3];

	Bufers[i][0] = CoorX;
	Bufers[i][1] = CoorY;
	Bufers[i][2] = 0;

	BuferC++;
}


// ---------------
// Bufer Play
// ===============

public boolean BuferPlay(int[] Data)
{
	do {
	int X=Data[0];
	int Y=Data[1];

		while (true)
		{
		if ( Data[2]==0 && FaseCheck(X,Y-1, 0) ) { Y--;} else
		if ( Data[2]==1 && FaseCheck(X+1,Y, 0) ) { X++;} else
		if ( Data[2]==2 && FaseCheck(X,Y+1, 0) ) { Y++;} else
		if ( Data[2]==3 && FaseCheck(X-1,Y, 0) ) { X--;} else {break;}
		FaseMap[Y*FaseSizeX+X]=2;
		}

	if (X!=Data[0] || Y!=Data[1]) {BuferSET(X,Y);}

	} while (++Data[2] < 4);

	return true;
}

// ---------------
// Bufer INI
// ===============

public void BuferINI()
{
	BuferC = 0;
	Bufers = new int[BuferMAX][];
}

// ---------------
// Bufer END
// ===============

public void BuferEND()
{
	Bufers = null;
	BuferC = 0;
}

// ---------------
// Bufer RUN
// ===============

public boolean BuferRUN()
{
	for (int i=0 ; i<BuferMAX ; i++)
	{
	if (Bufers[i] != null) {if ( BuferPlay(Bufers[i]) ) {Bufers[i]=null; BuferC--;}}
	}
	return BuferC==0;
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
			prefsData = new byte[] {1, 1};		// Inicializamos preferencias ya que NO estaban grabadas
		}

	// Actualizamos las variables del juego segun las prefs leidas / inicializadas
		try {

			gameSound = prefsData[0] != 0;
			gameVibra = prefsData[1] != 0;

		} catch(Exception e)
		{
	// Si catch, entonces prefs corruptas, inicializarlas de cero.
		//#ifdef com.mygdx.mongojocs.sextron.Debug
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

// Almacenamos las prefs

	updatePrefs(prefsData);		// Almacenamos byte[]
}

// <=- <=- <=- <=- <=-



// -------------------
// ===================

public void cheats()
{
	if (keyMisc == 11 && lastKeyMisc == 0)
	{
		playExit = 1;
	}
}

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