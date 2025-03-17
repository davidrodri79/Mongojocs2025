package com.mygdx.mongojocs.lma2007;
//#ifdef REM_ADVANCED_FINANCES
	//#define REM_FINANCES_PRICESSET
	//#define REM_FINANCES_ATTENDANCE
	//#define REM_FINANCES_EMPLOYEES
//#endif

//#ifdef REM_ADVANCED_TRANSFERS
	//#define REM_TRANSFERS_FILTER
	//#define REM_TRANSFERS_SCOUT
	//#define REM_TRANSFERS_NEGOTIATIONS
	//#define REM_BANK_INFO_CURRENCY
//#endif

//#if REM_FANTASY && REM_TRANSFERS_FILTER
	//#define REM_FILTER
//#endif

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.11 (11.7.2005)
// ===============================================




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.lma2007.Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
//import com.mygdx.mongojocs.midletemu.Runnable;
import com.mygdx.mongojocs.midletemu.RecordStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Random;

//#ifdef FULLSCREEN
		//#ifdef PLAYER_MOTC450
		//#elifdef MIDP20
		//#elifdef NOKIAUI
			public class GameCanvas extends FullCanvas
		//#else
		//#endif
	//#else
	//#endif

 implements Runnable, HandsetConstants

//#ifndef FULLSCREEN
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
//#ifdef DEBUG
	Debug.println (" -+- showNotify()");
//#endif

	if (!gameStarted) {return;}

	gamePaused = false;

	intKeyX = intKeyY = intKeyMisc = intKeyMenu = 0;

	if (!biosPauseEnabled)
	{
		gameForceRefresh = true;
		gameSoundRefresh = true;
	}
}

public void hideNotify()
{
//#ifdef DEBUG
	Debug.println (" -+- hideNotify()");
//#endif

	if (!gameStarted) {return;}

	gamePaused = true;

	if (!notifyHecho && biosStatus!=BIOS_PAUSE)
	{
		gameSoundOld = soundOld;
		gameSoundLoop = soundLoop;
		notifyHecho = true;
	}
	
	if (biosPauseEnabled)
	{
		biosPauseStart = true;
		soundStop();
	}
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Variables para la gestion del MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int mainSleep = 65;			// 'milis' que han de transcurrir como minimo en una iteracion del bucle principal (un tick)
int gameSleep;				// Contiene los 'milis' que sobraron en la ultima iteracion del bucle principal
long gameMilis;				// Gestion del tiempo para el tiempo que dura una iteracion del bucle principal

boolean gameExit;			// Forzamos salir del MIDlet al finalizar el bucle principal
boolean gamePaused;			// Forzamos pausar el bucle principal del MIDlet
boolean gameForceRefresh;	// Forzamos que el bucle principal invoque a los metodos que refrescan el canvas
boolean gameSoundRefresh;	// Forzamos que el bucle principal invoque a los metodos de sonido
int gameSoundOld = -1;		// Guardamos el sonido que esta reproduciendose en el momento de una interrupcion
int gameSoundLoop;			// Guardamos el loop del sonido que esta reproduciendose en el momento de una interrupcion
boolean gameStarted;		// Este flag nos indica que ya estamos dentro del bucle principal
boolean notifyHecho;		// Flag usado para evitar ejecuciones repetitivas de un X-notify()
boolean biosPauseEnabled;
boolean biosPauseStart;
int biosDebugState;

static Graphics scr;		// Contiene siempre el objeto 'Graphics' con el que podemos dibujar en pantalla

//#ifdef NOKIAUI
	DirectGraphics dscr;	// objeto 'Graphics' para algunos terminales Nokia con caracteristicas adicionales
//#endif

int canvasWidth = getWidth();
int canvasHeight = getHeight();

Image canvasImg;

boolean canvasShow;

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public void runInit()
{
	//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("run()");
//#endif
//#ifdef DEBUG_TIME_PASSED
	showTimePassed("run() start");
//#endif

	System.gc();
	biosCreate();	// Inicializamos MIDlet, antes de llegar al bucle principal
	System.gc();

	gameStarted = true;

//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("while()");
//#endif
//#ifdef DEBUG_TIME_PASSED
	showTimePassed("while() start");
//#endif

}

public void runTick()
{
	biosLoop();

	if(gameExit)
	{
		// Hemos salido del bucle principal, paramos el sonido, guardamos prefs y salimos del MIDlet
		soundStop();
		savePrefs();

//#ifdef J2ME
		ga.notifyDestroyed();
//#elifdef DOJA
//#endif
	}
}

public void runEnd()
{

}

public void run()
{
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("run()");
//#endif
//#ifdef DEBUG_TIME_PASSED
	showTimePassed("run() start");
//#endif

	System.gc();
	biosCreate();	// Inicializamos MIDlet, antes de llegar al bucle principal
	System.gc();

	gameStarted = true;

//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("while()");
//#endif
//#ifdef DEBUG_TIME_PASSED
	showTimePassed("while() start");
//#endif

// Empieza el bucle principal:
	while (!gameExit)
	{
		biosLoop();
	}

// Hemos salido del bucle principal, paramos el sonido, guardamos prefs y salimos del MIDlet
	soundStop();
	savePrefs();

//#ifdef J2ME
	ga.notifyDestroyed();
//#elifdef DOJA
//#endif
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// bios Loop
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int biosPauseSoftkeyLeft;
int biosPauseSoftkeyRight;
int biosPauseState;

public void biosLoop()
{

//System.out.println ("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

//#ifdef ENABLE_GC_TICK
	System.gc();
//#endif

	gameMilis = System.currentTimeMillis();

	if (!gamePaused)
	{
	// Gestion del teclado
		lastKeyMenu = keyMenu;	// Keys del Frame Anterior
		lastKeyX    = keyX;
		lastKeyY    = keyY;
		lastKeyMisc = keyMisc;

		keyMenu = intKeyMenu;	// Keys del Frame Actual
		keyX    = intKeyX;
		keyY    = intKeyY;
		keyMisc = intKeyMisc;

	//#ifdef LISTENER_SWAP
	//#endif

	// Truco para liberar pulsacion de las softkeys en mobiles que usan 'listener' pues no se notifica cuando se suelta la tecla.
		if (limpiaKeyMenu) {limpiaKeyMenu=false; intKeyMenu = 0;}

		try {
		//#ifdef DEBUG
			Debug.spdStart(0);
			biosTick();
			Debug.spdStop(0);
		//#else
		//#endif

			if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}

			forceRender();

			if (gameSoundRefresh)
			{
				waitTime(1000);
				soundPlay(gameSoundOld, gameSoundLoop);
				gameSoundRefresh = false;
				notifyHecho = false;
			}
			else
				soundTick();

		} catch (Exception e)
		{
		//#ifdef DEBUG
			Debug.println("** Excep Logic **");
			Debug.println("biosStatus:"+biosStatus);
			Debug.println("gameStatus:"+gameStatus);
			Debug.println(e.toString());
			e.printStackTrace();				
			//#ifdef DEBUG_STOP
				biosDebugState = biosStatus;
				biosStatus = BIOS_DEBUG;
				Debug.enabled = true;
		    //#endif
		//#endif
		}
	}

// Controlamos los 'milis' transcurridos y hacemnos la espera adecuada para que todas las iteraciones del bucle principal tarden lo mismo.
	gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
	waitTime(gameSleep < 1? 1:gameSleep);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Forzamos que se refresque la pantalla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void forceRender()
{
//#ifdef DEBUG
	if (canvasShow) {Debug.println("* * * ERROR, repaint() dentro de paint()!!!");}
//#endif

// Forzamos el renderizado del MIDlet
	canvasShow = true;
	repaint();

//#ifdef J2ME
	serviceRepaints();
//#endif

// Esperamos a que el Thread de 'paint()' finalize totalmente (necesario por bugs de algunos terminales)
	while (canvasShow == true) {waitTime(2);}

	forceRenderType = -1;
}

int forceRenderType = -1;

public void forceRenderTask(int type)
{
	forceRenderType = type;
	forceRender();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//#ifdef DOUBLE_BUFFER
Graphics bscr;
Image bimg;
//#endif

public void paint(Graphics g)
{
	if (canvasShow)
	{
		synchronized (this)
		{
			scr = g;	// Copiamos objeto 'Graphics' para poder usarlo desde cualquir sitio
			//#ifdef DOUBLE_BUFFER
			if (bimg == null)
			{
				bimg = new Image();
				bimg._createImage(getWidth(), getHeight());
				bscr = bimg.getGraphics();
			}
			scr = bscr;
			//#endif
			
		//#ifdef DEBUG
			Debug.spdStart(1);
			if (Debug.fillYellow) {Debug.fillYellow = false; fillDraw(0xfff800, 0, 0, canvasWidth, canvasHeight);}
		//#endif

		//#ifdef NOKIAUI
			dscr = DirectUtils.getDirectGraphics(g);
		//#endif

			try {
				if (forceRenderType < 0)
				{
					gameDraw();
				} else {
					forceRenderDraw();
				}
			} catch (Exception e)
			{
			//#ifdef DEBUG
				Debug.println("*** Exception Grafica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
				//#ifdef DEBUG_STOP
					biosDebugState = biosStatus;
					biosStatus = BIOS_DEBUG;
					Debug.enabled = true;
			    //#endif
			//#endif
			}

		//#ifdef DEBUG
			Debug.spdStop(1);
			if (Debug.enabled) {Debug.debugDraw(this, scr);}
		//#endif

			
			//#ifdef DOUBLE_BUFFER
			g.drawImage(bimg,0,0,0);
			//#endif
			scr = null;			// Liberamos copia de 'Graphics' para evitar usarlo desde la logica del MIDlet
			canvasShow = false;	// Indicamos que el Thread de render ha terminado
			
		}
	}
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<








//#ifdef DEBUG_PC_USED_MEM
int lastUsedMem;
public void showMemUsed(String str)
{
	System.gc();
 	int mem = (int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	//#ifdef DEBUG
		Debug.println("-*- " + str + ": " + mem + " (" + (mem - lastUsedMem) + ")");
	//#else
	//	System.out.println("-*- " + str + ": " + mem + " (" + (mem - lastUsedMem) + ")");
	//#endif
	lastUsedMem = mem;
}
//#endif




//#ifdef DEBUG_TIME_PASSED
long lastTimeMillis = System.currentTimeMillis();
public void showTimePassed(String str)
{
 	int time = (int)(System.currentTimeMillis() - lastTimeMillis);
	//#ifdef DEBUG
		Debug.println("-T- " + str + ": " + time);
	//#else
	//	System.out.println("-T- " + str + ": " + time);
	//#endif
	lastTimeMillis = System.currentTimeMillis();
}
//#endif






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
boolean limpiaKeyMenu;


public void keyPressed(int keycode)
{
//#ifdef FIX_SOFTKEY_RELEASE
//	limpiaKeyMenu = true;
//#endif

	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;

	switch (keycode)
	{
	case Canvas.KEY_NUM0:
		intKeyMisc=10;
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

//#ifdef SD-P600
//#elifdef PA-X500
//#elifdef MO-C450
//#elifdef MO-T720
//#elifdef LG-U8150
//#elifdef SG-Z105
//#elifdef MO
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
//#elifdef SM
//#else
	//#ifdef MIDP20
	//#endif
//#endif
	}

/*
	switch(getGameAction(keycode))
	{
		case 1:	intKeyY= -1;	return;	// Arriba
		case 6:	intKeyY=  1;	return;	// Abajo
		case 5:	intKeyX=  1;	return;	// Derecha
		case 2:	intKeyX= -1;	return;	// Izquierda
		case 8:	intKeyMenu= 2;	return;	// Fuego
	//#ifdef VI-TSM100
		case  9: intKeyMenu= -1;	return;	// Menu
		case 10: intKeyMenu=  1;	return;	// Aceptar
		case 11: intKeyMisc= 11;	return;	// *
		case 12: intKeyMisc= 12;	return;	// #
	//#endif
	}
	*/
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// bios - Engine
// ===================
// *******************

int biosStatus = 0;
int biosStatusOld;

int gameVolumePerCent = 100;//${handset.volumehigh};
//#ifndef REM_VOLUME
	int gameVolume = 2;
//#else
//#endif
boolean gameSound = true;
boolean gameVibra = true;
boolean gameAutosave = true;

static final int BIOS_GAME = 0;
static final int BIOS_MENU = 1;
static final int BIOS_POPUP = 2;
static final int BIOS_MENUBAR = 3;
static final int BIOS_PAUSE = 4;
static final int BIOS_LOGOS = 5;

static final int BIOS_DEBUG = 5000;


// -------------------
// bios Create
// ===================

String[][] gameText;
byte[] menuText;
//#ifdef FEA_MATCH_REPORT
String[][] reportText;
//#endif

//#ifdef FEA_MATCH_REPORT
String[] matchReport;
//#endif


public void biosCreate()
{
// --------------------------------
// Forzamos a FULL CANVAS para J2ME / MIDP 2.0
// ================================
	//#ifdef FULLSCREEN
		//#ifdef MIDP20
			//setFullScreenMode(true);
			waitTime(300);
			canvasWidth = getWidth();
			canvasHeight = getHeight();
		//#endif
	//#endif
// ================================


// --------------------------------
// Inicializamos com.mygdx.mongojocs.lma2007.Debug Engine
// ================================
//#ifdef DEBUG
	Debug.debugCreate(this);
//#endif
// ================================


// --------------------------------
// Limpiamos canvas de blanco y mostramos imagen de loading (reloj de arena)
// ================================

	fillCanvasRGB = 0xffffff;
	forceRenderTask(RENDER_FILL_CANVAS);

//#ifdef SI-x55
//#endif
		canvasImg = loadImage("/loading");
		forceRender();
//#ifdef SI-x55
//#endif
// ================================


// -=>>  Se pospone la carga de textos hasta que se seleccione el idioma  <<=-
	// --------------------------------
	// Cargamos archivo de textos
	// ================================
//		gameText = textosCreate( loadFile("/Textos.txt") );
//		menuText = textosCreate( loadFile("/menu.txt") );
	// ================================


// --------------------------------
// Inicializamos tipo de FONT base a usar en el juego
// ================================
//#ifndef HIDE_CUSTOM_FONT

	//#ifdef DEBUG_PC_USED_MEM
		showMemUsed("Antes de cargar CustomFonts");
	//#endif


	//#ifndef REM_CUSTOM_SMALL_FONT
		fontCor = loadFile("/font.cor");
		fontWhiteImg = loadImage("/fontWhite");
		fontBlackImg =  changePal(loadFile("/fontWhite.png"), fontWhiteRgbs, fontBlackRGB);
		fontOrangeImg = changePal(loadFile("/fontWhite.png"), fontWhiteRgbs, fontOrangeRGB);

		for (int i=0 ; i<fontCor.length ; i+=6) {fontCor[i] = 0;}
    //#endif


	//#ifndef REM_CUSTOM_BIG_FONT
		fontBigCor = loadFile("/fontBig.cor");
		fontBigWhiteImg = loadImage("/fontBigWhite");
		fontBigBlackImg = changePal(loadFile("/fontBigWhite.png"), fontBigWhiteRgbs, fontBigBlackRGB);
		fontBigYellowImg = changePal(loadFile("/fontBigWhite.png"), fontBigWhiteRgbs, fontBigYellowRGB);
		//#ifndef REM_FONT_GREY
			fontBigGreyImg = changePal(loadFile("/fontBigWhite.png"), fontBigWhiteRgbs, fontBigGreyRGB);
		//#else
		//#endif

		for (int i=0 ; i<fontCor.length ; i+=6) {fontBigCor[i] = 0;}
    //#endif


	//#ifdef DEBUG_PC_USED_MEM
		showMemUsed("Despues de cargar CustomFonts");
	//#endif

//#endif

// Reservamos espacio para listener
	fontInit(FONT_WHITE);
	listenerHeight += printGetHeight();
// ================================


// --------------------------------
// Inicializamos juego a nivel se usuario
// ================================

//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Antes de gameCreate()");
//#endif

	gameCreate();

//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Despues de gameCreate()");
//#endif

// ================================

}


// -------------------
// bios Tick
// ===================

public void biosTick()
{
	
//#ifdef DEBUG
	if (keyMisc == 11 && lastKeyMisc == 0 && biosPauseEnabled && biosStatus != BIOS_PAUSE)
	{
		biosPauseSoftkeyLeft = listenerIdLeft;
		biosPauseSoftkeyRight = listenerIdRight;
		biosPauseState = biosStatus;
		biosStatus = BIOS_PAUSE;
		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
	}

	if (keyMisc == 12 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; if (!Debug.enabled) {gameForceRefresh=true;}}
//#endif



// Gestionamos si saltamos a la pantalla de "GAME IN PAUSE"
	if (biosPauseStart && biosStatus != BIOS_PAUSE)
	{
		biosPauseSoftkeyLeft = listenerIdLeft;
		biosPauseSoftkeyRight = listenerIdRight;
		biosPauseState = biosStatus;
		biosStatus = BIOS_PAUSE;
		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
	}
	biosPauseStart = false;




	switch (biosStatus)
	{
// --------------------
// game Bucle
// --------------------
	case BIOS_GAME:
		gameTick();
	break;
// --------------------

// --------------------
// logos Bucle
// --------------------
//	case BIOS_LOGOS:
//		if ( logosTick() ) {biosStatus = BIOS_GAME;}
//	break;
// --------------------

// --------------------
// menu Bucle
// --------------------
	case BIOS_MENU:

		if (!menuExit && formTick()) {menuAction(formListCMD);}

		consoleShow = consoleRunning && formShow;

		if (menuExit) {biosStatus = BIOS_GAME;}
	break;
// --------------------

// --------------------
// menuBar Bucle
// --------------------
	case BIOS_MENUBAR:		
		menuBarTick();
	break;
// --------------------

// --------------------
// popup Bucle
// --------------------
	case BIOS_POPUP:
		if ( popupTick() ) {biosStatus = biosStatusOld;}
	break;
// --------------------

// --------------------
// pause Bucle
// --------------------
	case BIOS_PAUSE:

		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			listenerInit(biosPauseSoftkeyLeft, biosPauseSoftkeyRight);
			biosStatus = biosPauseState;
			gameForceRefresh = true;
			if (gameSoundOld >= 0 && gameSoundLoop==0)
			{
				soundPlay(gameSoundOld,gameSoundLoop);
			}
			notifyHecho = false;
		}
	return;
// --------------------

// --------------------
// debug Bucle
// --------------------
//#ifdef DEBUG
	case BIOS_DEBUG:

		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			biosStatus = biosDebugState;
			gameForceRefresh = true;
		}
	return;
//#endif
// --------------------

	}

	

	//#ifdef ENABLE_GC_TICK
	System.gc();
	//#endif
}

// -------------------
// bios Refresh
// ===================

public void biosRefresh()
{
//#ifdef DEBUG
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
		formShow = true;
		titleBarShow = true;					
		consoleShow = consoleRunning;	
		if(gameStatus == GAME_GFXMATCH) {
			gfxMatchShow = true;	
		}
	return;
// ====================

// --------------------
// menuBar Refresh
// ====================
	case BIOS_MENUBAR:
		menuBarShow = true;
		menuBarRefresh = true;
		titleBarShow = true;
	return;
// ====================

// --------------------
// popup Refresh
// ====================
	case BIOS_POPUP:
		menuBarRefresh = (biosStatusOld == BIOS_MENUBAR);
		formShow = formRunning;
		caratulaShow = (gameStatus == GAME_CARATULA);
		popupShow = true;
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
/*
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

	fillCanvasRGB = rgbLogos[cntLogos];
	forceRenderTask(RENDER_FILL_CANVAS);

	canvasImg = loadImage(nameLogos[cntLogos]);

	cntLogos++;

	timeIniLogos = System.currentTimeMillis();

	return false;
}
*/
// <=- <=- <=- <=- <=-















// -----------------------------------------------
// Definicion de los colores usados en los formularios
// ===============================================
static final int FORM_RGB_BACKGROUND = 0xffffff;	// Color de fondo para los formularios
static final int FORM_RGB_MARC = 0xff9C39;			// Color del marco
static final int FORM_RGB_BOX = 0x946342;			// Color de la caja normal (marron)
static final int FORM_RGB_BOX_SELECTED = 0xffefde;	// Color de la caja seleccionada
static final int FORM_RGB_BOX_DISABLED = 0x7b948c;	// Color de la caja oscura

static final int FORM_RGB_MOREGAMES_BACKGROUND = 0x7a948b;	// gris

static final int FORM_RGB_SCROLL_BACKGROUND = 0x946342;	// marron
// ===============================================


// *******************
// -------------------
// form - Engine
// ===================
// *******************

//#ifdef S30
//#elifdef S40
//#elifdef S60
	static final int FORM_SEPARATOR = 4;
	static final int FORM_TABLE_MAX_SEPARATOR = 10;
	static final int FORM_TABLE_MIN_SEPARATOR = 4;
//#elifdef S80
//#endif

static final int FORM_EDIT_MAX_CHARS = 10;	// Numero maximo de letras para la edicion del nombre del jugador


int FORM_SLIDER_WIDTH = 10;

int FORM_OPTION_BAR_HEIGH;


//#ifndef REM_LEAGUE_GRAPHIC
static final int FORM_GRAPHBAR_WIDTH = 16;
static final int FORM_GRAPHBAR_SEPARATOR = 8;
//#endif


// -----------------------------------------------
// Constantes de los tipos de formularios (formType)
// ===============================================
static final int FORM_NONE = 0;			// Formulario vacio
static final int FORM_OPTION = 1;		// Listado de opciones
static final int FORM_LIST = 2;			// Listado de texto
static final int FORM_CICLE = 3;		// Rotacion horizontal de opciones
static final int FORM_VERSUS = 4;		// Pantalla de versus
static final int FORM_CONSOLE = 5;		// Consola del match de texto
static final int FORM_TABLE = 6;		// Listado de opciones/texto tipo tabla
static final int FORM_EDIT_NAME = 7;	// Formulario para editar nombre
//#ifndef REM_LEAGUE_GRAPHIC
static final int FORM_GRAPHIC = 8;		// Formulario para mostrar graficas
//#endif
//#ifndef REM_CALENDAR
static final int FORM_CALENDAR = 9;		// Mostramos calendario
//#endif
static final int FORM_GRAPHBAR = 10;	// Formulario para mostrar grafica de barras
//#ifndef REM_FOOTBALL_ONE
static final int FORM_FOOTBALL_ONE = 11;// Formulario Football One
//#endif
static final int FORM_LOADING = 12;		// Mostramos formulario vacio con relog de arena en el centro
// ===============================================




// -----------------------------------------------
// Constantes de los tipos de renders para FORM-TABLE a nivel grobal (formTableRender)
// ===============================================
static final int TABLE_COLUM =	0;		// Render tipo Tabla de excel
static final int TABLE_INFO =	1;		// Render tipo listado de informacion
// ===============================================



// -----------------------------------------------
// Constantes de los tipos de renders para FORM-TABLE en lineas individuales (formListDat[line][0])
// ===============================================
static final int LINE_STD =			0x00000000;	// Render standard
static final int LINE_SOMBRA =		0x00000001;	// Render sombra (usado para el titulo)
static final int LINE_NO_CALC =		0x00000002;	// Se ignora el calcular el ancho de casa texto
static final int LINE_NO_SELECT =	0x00000004;	// Linea no seleccionable
// ===============================================



// -----------------------------------------------
// Constantes para definir los elementos de formItems
// ===============================================
static final int ITEM_ARROW_LEFT =		0x00;
static final int ITEM_ARROW_RIGHT =		0x01;
static final int ITEM_YELLOW_CARD =		0x02;
static final int ITEM_RED_CARD =		0x03;
static final int ITEM_ARROW_UP =		0x04;
static final int ITEM_ARROW_DOWN =		0x05;
static final int ITEM_SLIDER_ARROWS =	0x06;
static final int ITEM_OPTION_BAR =		0x14;
static final int ITEM_CALENDAR =		0x18;
static final int ITEM_MAIL =			0x1E;
static final int ITEM_STARS =			0x1F;
static final int ITEM_BALLON =			0x24;
static final int ITEM_RESERVAS =		0x25;
static final int ITEM_CHECK_OK =		0x23;
static final int ITEM_CHECK_NO =		0x29;
static final int ITEM_PAPELERA =		0x2A;
// ===============================================


static final int ITEM_IMAGES =			0x00;



// -----------------------------------------------
// Constantes con los RGB usado como barra de seleccion y barra del slider (formBaseColor)
// ===============================================
static final int FORM_RGB_INDEX_SQUAD = 0;	// TBD (no se el color exacto)
static final int FORM_RGB_INDEX_TRAINING = 1;
static final int FORM_RGB_INDEX_LAPTOP = 2;
static final int FORM_RGB_INDEX_INFO = 3;
static final int FORM_RGB_INDEX_TRANSFERS = 4;
static final int FORM_RGB_INDEX_FINANCE = 5;
static final int FORM_RGB_INDEX_STADIUM = 6;
static final int FORM_RGB_INDEX_STATS = 8;	// TBD (no se el color exacto)
static final int FORM_RGB_INDEX_LISTENER = 9;	// TBD (no se el color exacto)

static final int[] FORM_RGB_PANTALLAS = new int[] {0x60876F,0xE79B52,0xACC953,0xC14E5E,0x826AB0,0xB4A848,0x609DB3};
// ===============================================






// Variables para la gestion de los titulos de los listados de opciones
	String[] formTitleStr;


// Variables para la gestion de Tablas
	int[] formTableItems;		// Informacion de cada columna de la tabla
	int[] formTableMasterCell;	// Indicamos la celdas "master" las cuales aumentara o se cortara para ajustarse horizontalmente a la tabla
	boolean formTableNumerar;	// Forzamos a numerar las lineas de la tabla por la parte izquerda
	boolean formTableTitle;		// Forzamos a usar la primea linea de la tabla como titulo
	boolean formTableOption;	// Forzamos a que la tabla sea tipo OPTION, con opciones seleccionables, por defecto es tipo LIST
	int formTableRender;		// Tipo de render de la tabla


// Variables para la gestion del slider vertical
	int formVsliderX;
	int formVsliderY;
	int formVsliderWidth;
	int formVsliderHeight;


// Variables para la gestion de areas
	int formBodyMode;
	int formBodyX;
	int formBodyY;
	int formBodyWidth;
	int formBodyHeight;
	int formBoxX;
	int formBoxY;
	int formBoxWidth;
	int formBoxHeight;
	int[] formBoxesX;
	int[] formBoxesW;


// Variables para la gestion de la opcion CICLE
	int formCiclePos;
	String formCicleTitle;
	String[] formCicleStr;


// Variables para la gestion del CAMPO
	Image formCampoImg;
	
//#ifdef S30
//#elifdef S40
//#elifdef S60
	public static final int MINI_CAMPO_WIDTH = 65;
	public static final int MINI_CAMPO_HEIGHT = 85;	
//#endif


// Variables para la gestion del area SWAP
	boolean formSwapEnabled;
	int formSwapY;
	int formSwapSelected;


// Variables para la gestion de EDIT_NAME
	int formCellX;
	int formCellY;
	int formCellWidth;
	int formCellHeight;
	int formCellNumWidth;
	int formCellNumHeight;
	int formMaxCharWidth;
	String formEditStr;
	boolean formEditFirstMove;


// Variables para la gestion de las imagenes de CUSTOMIZATION SELECT LEAGUE/TEAM
//#ifndef REM_TEAMSHIELDS
	Image formShieldImg;
//#endif

// Variables para la gestion del area REST (imagen y puntos restantes para el trainig line)
	boolean formRestEnabled;


// Variables para la gestion del area STADIUM (lineas extra)
	//#ifndef REM_STADIUM
	boolean formStadiumEnabled;
	//#endif

	//#ifndef REM_BONUS_CODES
// Variables para la gestion del bonus codes
	byte[] formBonusCodes;
	//#endif

// Variables internas
	int formListView;
	int formListCMD;
	int formListEnter;
	int formListScroll;
	int formListScrollPos;
	int formListScrollCnt;
	int formListScrollSized;

	int formActionBack;
	int formActionAcept;
	int formActionCicle;

	byte[] formItemsCor;
	Image formItemsImg;

//#ifndef REM_FLECHAS_IMAGE
	byte[] flechasCor;
	Image flechasImg;
//#endif

	int formType;

	int formBaseColor;

	int[] formListNum;

	boolean formRunning;
	boolean formShow;

	boolean formTableLineShow;
	
	boolean floatingForm;

	boolean formVersusResult;
// -------------------

// -------------------
// form Clear
// ===================

public void formClear()
{
// titleBar
//	menuListTitleA = null;
//	menuListTitleB = null;

// formTable
	formTableRender = TABLE_COLUM;
	formTableItems = null;
	formTableMasterCell = null;
	formTableNumerar = false;
	formTableTitle = false;
	formTableOption = false;

// formTitle
	formTitleStr = null;

// formList
	formListClear();

// formCicle
	formCiclePos = 0;
	formCicleTitle = null;
	formCicleStr = null;

// formCampo
	formCampoImg = null;

// formSwap
	formSwapEnabled = false;
	formSwapSelected = -1;

// formShield
//#ifndef REM_TEAMSHIELDS
	formShieldImg = null;
//#endif
// formRest
	formRestEnabled = false;

// formStadium
//#ifndef REM_STADIUM
	formStadiumEnabled = false;
//#endif
	
// formListScroll
	formListScroll = 0;
	formListScrollPos = formListScrollCnt = -1;

//#ifndef REM_BONUS_CODES
// formBonusCodes
	formBonusCodes = null;
//#endif
	
// form
	formBaseColor = 3;
	formActionCicle = 0;
	formListNum = null;
	floatingForm = false;
	formVersusResult = false;
}



// -------------------
// form Init
// ===================

public void formInit(int mode, int pos, int actionBack, int actionAcept)
{
// Guardamos el tipo de Formulario a mostrar
	formBodyMode = mode;

// Guardamos posicion inicial (cursor)
	formListPos = pos;

// Guardamos la accion a ejecutar al pulsar la softkey izquierda y derecha
	formActionBack = actionBack;
	formActionAcept = actionAcept;



// Fijamos area disponible para los elementos
	if(floatingForm)
	{
		formBodyX = 0;
		formBodyY = 0;
		formBodyWidth = canvasWidth-20;
		formBodyHeight = canvasHeight-30;
	
	} else {

		formBodyX = 2;		
		formBodyY = titleAreaHeight;
		formBodyWidth = canvasWidth-4;
		formBodyHeight = canvasHeight - formBodyY - FORM_SEPARATOR;
	}




// formSwap: Si existe elemento para intercambiar jugadores, reservamos el area que usaremos
	if (formSwapEnabled)
	{
		formSwapY = formBodyY + FORM_SEPARATOR;

		fontInit(FONT_BLACK);

	//#ifndef REM_SQUAD_SEPARATOR
		formBodyY += printFontHeight * 4;
		formBodyHeight -= printFontHeight * 4;
	//#else
    //#endif
	}



// formCampo: Si hay que mostrar campo a la izquierda, ajustamos body a la derecha del campo
	if (formCampoImg != null)
	{
//#ifdef REM_CAMPO_IMAGE
//#else
		formBodyY = formBodyY + ((formBodyHeight - formCampoImg.getHeight())/2);
		formBodyHeight = formCampoImg.getHeight();
		formBodyX += formCampoImg.getWidth();
		formBodyWidth -= formCampoImg.getWidth();
//#endif
	}



// formRest: Si existe resumen inferior de resto de puntos, reservamos el area necesaria
	if (formRestEnabled)
	{
	//#ifndef REM_TRAINING_IMAGE
		formBodyHeight -= spriteHeight(trainingCor, ITEM_IMAGES);
	//#else
	//#endif
	}


// formRest: Si existe resumen inferior de resto de puntos, reservamos el area necesaria
//#ifndef REM_STADIUM
	if (formStadiumEnabled)
	{
		fontInit(FONT_WHITE);
		formBodyHeight -= (printFontHeight * 3) + FORM_SEPARATOR;
	}
//#endif


	int cnt;
	int[][] tmpDat;
	String[][] tmpStr;


// Representamos tipo de formulario
	switch (formBodyMode)
	{
	case FORM_TABLE:

	// Controlamos que la opcion por defecto, exista, sino, marcamos la cero
		if (formListPos >= formListStr.length) {formListPos = 0;}

	// Seleccionamos font
		fontInit(FONT_WHITE);


	// Calculamos Salto de linea
		formListEnter = printGetHeight();	// Salto de linea entre la (x,y) de una caja a la siguiente.


	// Reservamos area para numeracion de las lineas
		if (formTableNumerar)
		{
			int space = printStringWidth("28") + FORM_TABLE_MIN_SEPARATOR;
			formBodyX += space;
			formBodyWidth -= space;
		}


	// Reservamos area para opcion Cicle si la hay...
		if (formCicleStr != null)
		{
			formBodyY += formListEnter * 2;
			formBodyHeight -= formListEnter * 2;
		}


	// Reservamos area para que las letras no queden pegadas al borde
		formBodyX += FORM_TABLE_MIN_SEPARATOR;
		formBodyWidth -= (FORM_TABLE_MIN_SEPARATOR * 2);



	// Cortamos lineas de texto que se salgan de la pantalla
		if (formTableRender == TABLE_INFO)
		{
			cnt = 0;
			boolean[] tmpTab = new boolean[formListStr.length];
			for (int i=0 ; i<formListStr.length ; i++)
			{
				if (formListStr[i].length == 1)
				{
					formListStr[i] = printTextBreak(formListStr[i][0], formBodyWidth - FORM_SLIDER_WIDTH);
					cnt += formListStr[i].length;
					tmpTab[i] = true;
				} else {
					cnt++;
				}
			}
			tmpDat = new int[cnt][];
			tmpStr = new String[cnt][];
			cnt=0;
			for (int i=0 ; i<formListStr.length ; i++)
			{
				if (tmpTab[i])
				{
					for (int t=0 ; t<formListStr[i].length ; t++)
					{
						tmpDat[cnt] = formListDat[i];
						tmpStr[cnt] = new String[1];
						tmpStr[cnt++][0] = formListStr[i][t];
					}
				} else {
					tmpDat[cnt] = formListDat[i];
					tmpStr[cnt++] = formListStr[i];
				}
			}
			formListDat = tmpDat;
			formListStr = tmpStr;


		// Comprobamos que el "dato" no se sale de la pantalla
			for (int i=0 ; i<formListStr.length ; i++)
			{
				if (formListStr[i].length == 2)
				{
					if (printStringWidth(formListStr[i][0]) + FORM_TABLE_MIN_SEPARATOR + printStringWidth(formListStr[i][1]) > formBodyWidth - FORM_SLIDER_WIDTH)
					{
						formListAdd(0, new String[] {" ", " "}, 0,0);
	
						int[] tmpDat2 = formListDat[formListDat.length-1];
						String[] tmpStr2 = formListStr[formListStr.length-1];
						for (int t=formListStr.length-2 ; t>i ; t--)
						{
							formListDat[t+1] = formListDat[t];
							formListStr[t+1] = formListStr[t];
						}
						formListDat[i+1] = tmpDat2;
						formListStr[i+1] = tmpStr2;
	
						formListStr[i+1][1] = formListStr[i][1];
						formListStr[i+1][0] = formListStr[i][1] = "";
	
						i++;
					}
				}
			}
		}



	// Calculamos cuantas lineas de texto nos entran en el area disponible
		formListView = formBodyHeight / formListEnter;
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
		} else {
		// Reservamos area para slider vertical
			formBodyWidth -= FORM_SLIDER_WIDTH;
		}



	// Calculamos el ancho de todos los elemetos a mostrar en la misma linea
		formBoxWidth = 0;
		formBoxesW = new int[formTableItems.length];
		formBoxX = formBodyX;
		formBoxesX = new int[formBoxesW.length];

			for (int i=0 ; i<formTableItems.length ; i++)
			{
				int sprite = (formTableItems[i]>>8) & 0xff;
	
				if (sprite < 0x80)
				{
					fontInit(sprite);
					formBoxesW[i] = formListStringWidth(i);
				} else {
					formBoxesW[i] = spriteWidth(formItemsCor, sprite & 0x7f);
				}
	
				formBoxWidth += formBoxesW[i];
			}

	
		if (formTableItems.length > 1)
		{
	
		// Calculamos separacion entre todos los elementos
			int separator = (formBodyWidth - formBoxWidth) / (formBoxesW.length - 1);
	
	
		// Si existe una celda "master" ajustamos el espacio que sobra o falta sobre su "ancho"
			if (formTableMasterCell != null)
			{
				if (separator > FORM_TABLE_MAX_SEPARATOR)
				{
					int size = (separator - FORM_TABLE_MAX_SEPARATOR) * (formBoxesW.length - 1);
					for (int i=0 ; i<formTableMasterCell.length ; i++)
					{
						formBoxesW[formTableMasterCell[i]] += size / formTableMasterCell.length;
					}
					separator = FORM_TABLE_MAX_SEPARATOR;
				}
				else if (separator < FORM_TABLE_MIN_SEPARATOR)
				{
					int size = (formBoxWidth - formBodyWidth) + (FORM_TABLE_MIN_SEPARATOR * (formBoxesW.length - 1));
					for (int i=0 ; i<formTableMasterCell.length ; i++)
					{
						formBoxesW[formTableMasterCell[i]] -= size / formTableMasterCell.length;
					}
					separator = FORM_TABLE_MIN_SEPARATOR;
				}
			}

		// Calculamos X de todos los elemetos
			formBoxesX[0] = formBoxX;
			for (int i=1 ; i<formBoxesX.length ; i++)
			{
				formBoxesX[i] = formBoxesX[i-1] + formBoxesW[i-1] + separator;
			}
		} else {
			formBoxesX[0] = formBoxX;
			formBoxesW[0] = formBodyWidth;
		}


	// Anotamos el ancho maximo con todos los elementos
		formBoxWidth = formBodyWidth;


		if(!floatingForm) {
			
			formBoxY = formBodyY + ((formBodyHeight - (formListView * formListEnter))/2);
			
		} else {
				
			formBoxY = canvasHeight - (formListView * formListEnter);
		}
		
		formBoxHeight = formListEnter;



	// Si hay que mostrar campo, justificamos formulario segun campo
		if (formCampoImg != null)
		{
			formBoxY = formBodyY;
		}



	// Restauramos area para que las letras no queden pegadas al borde
		formBodyX -= FORM_TABLE_MIN_SEPARATOR;
		formBodyWidth += (FORM_TABLE_MIN_SEPARATOR * 2);



		formVsliderX = formBodyX + formBodyWidth;
		formVsliderY = formBoxY + (formTableTitle? formListEnter:0);
		formVsliderHeight = formListEnter * (formListView - (formTableTitle? 1:0));



	// Evitamos que el titulo este seleccionado
		if (formTableTitle && formListPos == 0)
		{
			formListPos++;
		}
	break;


	case FORM_OPTION:

	// Controlamos que la opcion por defecto, exista, sino, marcamos la cero
		if (formListPos >= formListStr.length) {formListPos = 0;}

	// Seleccionamos font
		fontInit(FONT_BIG_WHITE);

	// Calculamos salto de linea
		formListEnter = FORM_OPTION_BAR_HEIGH + FORM_SEPARATOR;	// Salto de linea entre la (x,y) de una caja a la siguiente.

	// Ajustamos alto del salto de linea
		formBoxHeight = FORM_OPTION_BAR_HEIGH;

	// Calculamos eje X donde mostrar las lineas
//		formBoxX = formBodyX + ((formBodyWidth - formBoxWidth) / 2);
		formBoxX = formBodyX + spriteWidth(formItemsCor, ITEM_OPTION_BAR);

		formBoxWidth = formBodyWidth - formBoxX - spriteWidth(formItemsCor, ITEM_OPTION_BAR);


		if (menuType == MENU_FANTASY_COLOUR || formListNum != null)
		{
			formBoxWidth -= spriteWidth(formItemsCor, ITEM_OPTION_BAR) * 2;
		}


	// Reservamos area para Titulo si lo hay...
		if (formTitleStr != null)
		{
			int size = 0;
			if (menuType == MENU_CUST_LEAGUE_TEAM || menuType == MENU_EXHI_LEAGUE_TEAM_A || menuType == MENU_EXHI_LEAGUE_TEAM_B)
			{
				size = spriteWidth(formItemsCor, ITEM_BALLON);
			}

			formTitleStr = printTextBreak(formTitleStr, formBodyWidth - formBoxX - size);

			formBodyY += formListEnter * formTitleStr.length;
			formBodyHeight -= formListEnter * formTitleStr.length;
		}


	// Calculamos cuantas lineas de texto nos entran en pantalla
		formListView = formBodyHeight / formListEnter;	// lineas visibles en lo que queda de pantalla

	
	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
//		} else {
//			formVslider = true;
		}

		formBoxY = formBodyY + (formBodyHeight - (formListView * formListEnter)) / 2;
	break;


	case FORM_LIST:

		fontInit(FONT_WHITE);

		formListEnter = printGetHeight();	// Salto de una linea a la siguiente.


	// Reservamos area para Titulo si lo hay...
		if (formTitleStr != null)
		{
			formTitleStr = printTextBreak(formTitleStr, formBodyWidth);

			formBodyY += formListEnter * formTitleStr.length;
			formBodyHeight -= formListEnter * formTitleStr.length;
		}


	// Reservamos area para slider vertical
		formBodyWidth -= FORM_SLIDER_WIDTH;


		formBoxWidth = formBodyWidth - (FORM_SEPARATOR * 4);
		formBoxX = formBodyX + (FORM_SEPARATOR * 2);

	// Cortamos el texto si se sale de la pantalla
		cnt = 0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			formListStr[i] = printTextBreak(formListStr[i][0], formBoxWidth - FORM_SEPARATOR);
			cnt += formListStr[i].length;
		}
		tmpDat = new int[cnt][];
		tmpStr = new String[cnt][1];
		cnt=0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			for (int t=0 ; t<formListStr[i].length ; t++)
			{
				tmpDat[cnt] = formListDat[i];
				tmpStr[cnt++][0] = formListStr[i][t];
			}
		}
		formListDat = tmpDat;
		formListStr = tmpStr;

	// Calculamos cuantas lineas de texto nos entran en pantalla
		formListView = formBodyHeight / formListEnter;	// lineas visibles en lo que queda de pantalla
	
	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
//		} else {
//			formVslider = true;
		}

		formBoxHeight = (formListView * formListEnter);

		formBoxY = formBodyY + (formBodyHeight - formBoxHeight) / 2;


		formVsliderX = formBoxX + formBoxWidth;
		formVsliderY = formBoxY;
		formVsliderHeight = formListEnter * formListView;
	break;

	
//#ifndef REM_TEXTMATCH	
	
	case FORM_CONSOLE:
		
		fontInit(FONT_WHITE);

		formListEnter = printGetHeight();	// Salto de linea una linea y la siguiente.

		formBoxWidth = formBodyWidth - (FORM_SEPARATOR);
		formBoxX = formBodyX + (FORM_SEPARATOR / 2);

	// Cortamos el texto si se sale de la pantalla
		cnt = 0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			formListStr[i] = printTextBreak(formListStr[i][0], formBoxWidth - FORM_SEPARATOR);
			cnt += formListStr[i].length;
		}
		tmpDat = new int[cnt][];
		tmpStr = new String[cnt][1];
		cnt=0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			for (int t=0 ; t<formListStr[i].length ; t++)
			{
				tmpDat[cnt] = formListDat[i];
				tmpStr[cnt++][0] = formListStr[i][t];
			}
		}
		formListDat = tmpDat;
		formListStr = tmpStr;

	// Calculamos cuantas lineas de texto nos entran en pantalla
		formListView = formBodyHeight / formListEnter;	// lineas visibles en lo que queda de pantalla
	
	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
		} else {
//			formVslider = true;
		}

		//formBoxHeight = (formListView * formListEnter);
		formBoxY = formBodyY + (formBodyHeight - formBoxHeight) / 2;
	break;

//#endif







	case FORM_EDIT_NAME:

		fontInit(FONT_BIG_BLACK);

	// Calculamos letra mas ancha para calcular el ancho de la rejilla
		formMaxCharWidth = 0;
		for (int i=0 ; i<gameText[TEXT_ABECEDARIO][0].length() ; i++)
		{
			int width = printStringWidth(gameText[TEXT_ABECEDARIO][0].substring(i,i+1));
			if (formMaxCharWidth < width) {formMaxCharWidth = width;}
		}

	// Calculamos tama�o y posicion de la caja donde editamos el nombre
		formBoxWidth = (formMaxCharWidth * FORM_EDIT_MAX_CHARS) + (FORM_SEPARATOR * 2);
		formBoxHeight = printGetHeight() + (FORM_SEPARATOR / 2);

		formBoxX =  formBodyX + ((formBodyWidth - formBoxWidth) / 2);


	// Calculamos tama�o y posicion de la rejilla de letras
		formCellWidth = formMaxCharWidth + 3;
		formCellHeight = printGetHeight();

		formCellNumWidth = (formBodyWidth - FORM_SEPARATOR * 2) / formCellWidth;

		//formCellNumWidth--; formCellNumWidth &= -2;	// Hack, para que el numero de celdas sean siempre pares

		formCellNumHeight = (gameText[TEXT_ABECEDARIO][0].length() / formCellNumWidth);
		if ((formCellNumWidth * formCellNumHeight) < gameText[TEXT_ABECEDARIO][0].length()) {formCellNumHeight++;}


		int h = formBoxHeight + FORM_SEPARATOR + ((formCellNumHeight + 1) * formCellHeight) + FORM_SEPARATOR;
		formBoxY = formBodyY + ((formBodyHeight - h)/2);


		formCellX = ((formBodyWidth - (formCellNumWidth * formCellWidth)) / 2) + formBodyX;
		formCellY = formBoxY + formBoxHeight + FORM_SEPARATOR;

	// Pillamos nombre a editar, el del manager o un jugador seleccionado
		if (menuType == MENU_CUST_EDIT_NAME)
		{
			formEditStr = custName;
		}
		//#ifndef REM_FANTASY
		else if (menuType == MENU_FANTASY_EDIT_NAME)
		{
			formEditStr = fantasyName;
		}
		//#endif
		 else {
			formEditStr = league.playerGetName(league.userTeam.playerIds[playerSelectedToChangeName]);
		}

//		formEditFirstMove = true;	// Codemasters no quieren que se borre el nombre al primer movimiento...
		formEditFirstMove = false;
		formListPos = (formCellNumWidth * formCellNumHeight);
	break;


//#ifndef REM_FOOTBALL_ONE
	case FORM_FOOTBALL_ONE:

		fontInit(FONT_WHITE);

		formBodyX = 0;
		formBodyWidth = canvasWidth;
		formBodyY = fOneMainImg.getHeight();
		formBodyHeight = canvasHeight - formBodyY - (fOneLogoImg.getHeight()*2);


	// Reservamos area para slider vertical
		formBodyWidth -= FORM_SLIDER_WIDTH;


		formBoxWidth = formBodyWidth - (FORM_SEPARATOR * 4);
		formBoxX = formBodyX + (FORM_SEPARATOR * 2);

	// Cortamos el texto si se sale de la pantalla
		cnt = 0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			formListStr[i] = printTextBreak(formListStr[i][0], formBoxWidth - FORM_SEPARATOR);
			cnt += formListStr[i].length;
		}
		tmpDat = new int[cnt][];
		tmpStr = new String[cnt][1];
		cnt=0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			for (int t=0 ; t<formListStr[i].length ; t++)
			{
				tmpDat[cnt] = formListDat[i];
				tmpStr[cnt++][0] = formListStr[i][t];
			}
		}
		formListDat = tmpDat;
		formListStr = tmpStr;


		formListView = (formBodyHeight / printFontHeight) + 1;
		if (formListView > formListStr.length) {formListView = formListStr.length;}


		formVsliderX = canvasWidth - FORM_SLIDER_WIDTH;
		formVsliderY = formBodyY;
		formVsliderHeight = printFontHeight * formListView;
	break;
//#endif
	}

	formRunning = true;
	formShow = true;
}



// -------------------
// form Release
// ===================

public void formRelease()
{
	formClear();

	formRunning = false;
	formShow = false;

//#ifndef REM_FOOTBALL_ONE
	footballOneStr = null;
//#endif

//#ifndef REM_GC_GRAPHICS
	fOneMainImg = null;
	fOneLogoImg = null;
//#endif

//#if !REM_TRAINING_IMAGE && !REM_GC_GRAPHICS
	if (menuType != MENU_TRAINING_SCHEDULE)
	{
		trainingImg = null;
		trainingCor = null;
	}
//#endif
}


// -------------------
// form Tick
// ===================

int keyRepeatX;
int keyRepeatY;

public boolean formTick()
{
	switch (formBodyMode)
	{
	case FORM_TABLE:

	// Controlamos si pulsamos las softkeys
		if (keyMenu != 0 && lastKeyMenu == 0)
		{
			formListCMD = (keyMenu > 0? formActionAcept:formActionBack);
			return true;
		}


	// Repeticion de tecla
		if (keyY == 0) {keyRepeatY = 0;} else if (keyY == lastKeyY && ++keyRepeatY > 6) {lastKeyY = 0; keyRepeatY--;}
		
		if (keyX == 0) {keyRepeatX = 0;} else if (keyX == lastKeyX && ++keyRepeatX > 6) {lastKeyX = 0; keyRepeatX--;}


	// Controlamos desplazamiento vertical
		if (keyY != 0 && lastKeyY == 0)
		{
			formListPos += keyY;

		// Controlamos desbordamiento vertical
			if (formTableOption)
			{
				if (formListPos >= formListStr.length) {formListPos = (formTableTitle? 1:0);}
				if (formListPos < (formTableTitle? 1:0)) {formListPos = formListStr.length - 1;}
			} else {
				if (formListPos >= formListStr.length) {formListPos = formListStr.length - 1;}
				if (formListPos < (formTableTitle? 1:0)) {formListPos = (formTableTitle? 1:0);}
			}

		// Si no es un listado de opciones, adaptamos datos para simular un litado de texto
			if (!formTableOption)
			{
				if (formListPos > formListStr.length - formListView + (formTableTitle? 1:0))
				{
					formListPos = formListStr.length - formListView + (formTableTitle? 1:0);
				}
				formListIni = formListPos;
			}

		// Si pisamos una opcion tipo 'titulo', saltamos a la siguiente opcion
			if ( (formListDat[formListPos][0] & LINE_NO_SELECT) != 0 )
			{
				if (keyY < 0) {if (formListPos >= 1) {formListPos--;} else {formListPos++;}}
				else
				if (keyY > 0) {if (formListPos < formListStr.length-1) {formListPos++;} else {formListPos--;}}
			}

			formShow = true;
		}


	// Controlamos desplazamiento horizontal
		if (keyX != 0 && lastKeyX == 0)
		{
			menuAction(formActionCicle);
		}

	// Controlamos el scroll horizontal del texto actualmente seleccionado si es necesario
		if (formListScrollCnt== 0 || (formListScrollCnt > 0 && --formListScrollCnt == 0))
		{
			if ((formListScroll-=2) < -formListScrollSized) {formListScroll = 0; formListScrollCnt = 10;}
			formTableLineShow = true;
		}

	// Actualizamos texto de Softkey Positiva con texto especifico si lo hubiera
		int softkeyId = (formListDat[formListPos][0]>>16) & 0xff;
		listenerInit(listenerIdLeft, (softkeyId>0? softkeyId : lastListenerRightId));
	break;


	case FORM_OPTION:

	// Repeticion de tecla
		if (keyY == 0) {keyRepeatY = 0;} else if (keyY == lastKeyY && ++keyRepeatY > 6) {lastKeyY = 0; keyRepeatY--;}


	// Controlamos desplazamiento vertical
		if (keyY > 0 && lastKeyY == 0 && formListPos < formListStr.length-1) {formListPos++; formShow = true;}
		else
		if (keyY < 0 && lastKeyY == 0 && formListPos > 0) {formListPos--; formShow = true;}

	// Controlamos si pulsamos desplazamiendo horizontal (solo para rotar opciones)
		if (keyX != 0 && lastKeyX == 0 && formListStr[formListPos].length > 1)
		{
			formListDat[formListPos][2] += keyX;
			if (formListDat[formListPos][2] < 0) {formListDat[formListPos][2] = formListStr[formListPos].length-1;}
			else
			if (formListDat[formListPos][2] == formListStr[formListPos].length) {formListDat[formListPos][2] = 0;}
			formListCMD = formListDat[formListPos][1];
			formShow = true;
			return true;
		}
	// Controlamos si pulsamos desplazamiendo horizontal (solo para modo numerico)
		else if (keyX != 0 && lastKeyX == 0 && (formListNum != null
				//#ifndef REM_FANTASY
				|| menuType == MENU_FANTASY_COLOUR
				//#endif
				))
		{
			menuAction(formActionCicle);
		}


	// Controlamos si pulsamos OK
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			if (formActionAcept == 0)
			{
				if (++formListDat[formListPos][2] == formListStr[formListPos].length) {formListDat[formListPos][2] = 0;}
				formListCMD = formListDat[formListPos][1];
				formShow = true;
			} else {
				formListCMD = formActionAcept;
			}
			return true;
		}

	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionBack;
			return true;
		}


	// Controlamos el scroll horizontal del texto actualmente seleccionado si es necesario
		if (formListScrollCnt== 0 || (formListScrollCnt > 0 && --formListScrollCnt == 0))
		{
			if ((formListScroll-=2) < (formBoxWidth/2) -formListScrollSized) {formListScroll = 0; formListScrollCnt = 10;}
			formShow = true;
		}


	// Actualizamos texto de Softkey Positiva con texto especifico si lo hubiera
		softkeyId = (formListDat[formListPos][0]>>16) & 0xff;
		listenerInit(listenerIdLeft, (softkeyId>0? softkeyId : lastListenerRightId));
	break;


//#ifndef REM_FOOTBALL_ONE
	case FORM_FOOTBALL_ONE:

		// Controlamos desplazamiento horizontal
			if (keyX != 0 && lastKeyX == 0)
			{
				menuAction(formActionCicle);
			}
//#endif

	case FORM_LIST:

	// Repeticion de tecla
		if (keyY == 0) {keyRepeatY = 0;} else if (keyY == lastKeyY && ++keyRepeatY > 6) {lastKeyY = 0; keyRepeatY--;}


	// Controlamos si pulsamos ACEPT
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionAcept;
			return true;
		}


	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionBack;
			return true;
		}


	// Controlamos si pulsamos PAD Arriba
		if (keyY < 0 && lastKeyY == 0 && formListIni > 0)
		{
			formListIni--;
			formShow = true;
		}


	// Controlamos si pulsamos PAD Abajo
		if (keyY > 0 && lastKeyY == 0 && formListIni < formListStr.length - formListView)
		{
			formListIni++;
			formShow = true;
		}
	break;



		case FORM_VERSUS:
		//#ifndef REM_CALENDAR
		case FORM_CALENDAR:
		//#endif
		// Controlamos si pulsamos OK
			if (keyMenu > 0 && lastKeyMenu == 0)
			{
				formListCMD = formActionAcept;
				return true;
			}
	
		// Controlamos si pulsamos CANCEL
			if (keyMenu < 0 && lastKeyMenu == 0)
			{
				formListCMD = formActionBack;
				return true;
			}

		// Controlamos desplazamiento horizontal
			if (keyX != 0 && lastKeyX == 0)
			{
				menuAction(formActionCicle);
			}

		break;

			
//#ifndef REM_TEXTMATCH			
			
		case FORM_CONSOLE:
			
			if(textMatchTick()) {
			
				formListCMD = MENU_ACTION_TEXTMATCHEND;
				return true;
			}
			
			// Ir al men� durante el partido
			if (keyMenu < 0 && lastKeyMenu == 0)
			{						
				//return true;
			}
	
			// Controlamos si pulsamos SKIP
			if (keyMenu > 0 && lastKeyMenu == 0)
			{				
				formListCMD = MENU_ACTION_TEXTMATCHEND;
				return true;
			}									

			formShow = true;

		break;

//#endif



	case FORM_EDIT_NAME:

	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionBack;
			return true;
		}

	// Controlamos que si nada mas entrar nos desplazamos, borramos nombre por defecto
		if (formEditFirstMove && ((keyX != 0 && lastKeyX == 0) || (keyY != 0 && lastKeyY == 0)))
		{
			formEditFirstMove = false;
			formEditStr = "";
			formShow = true;
		}

	// Controlamos desplazamiento horizontal y vertical
		int posX = formListPos % formCellNumWidth;
		int posY = formListPos / formCellNumWidth;

		if (keyX != 0 && lastKeyX == 0)
		{
			if (posY != formCellNumHeight)
			{
				posX += keyX;
			} else {
				posX += keyX * (formCellNumWidth / 2);
			}

			if (posX < 0) {posX = formCellNumWidth - 1;}
			else
			if (posX >= formCellNumWidth) {posX = 0;}

			formShow = true;
		}

		if (keyY != 0 && lastKeyY == 0)
		{
			posY += keyY;

			if (posY < 0) {posY = formCellNumHeight;}
			else
			if (posY > formCellNumHeight) {posY = 0;}

			formShow = true;
		}

		formListPos = (posY * formCellNumWidth) + posX;

	// Controlamos la pulsacion en una letra
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
		// Hemos pulsado en "Borrar/Aceptar" o en una letra de la rejilla?
			if (formListPos >= (formCellNumWidth*formCellNumHeight))
			{
				if (formListPos >= ((formCellNumWidth*formCellNumHeight)+(formCellNumWidth/2)))
				{
				// Hemos pulsado "Borrar" Borramos ultima letra, en el caso de que haya como minimo una.
					if (formEditStr.length() > 0) {formEditStr = formEditStr.substring(0, formEditStr.length()-1);}
				} else {
				// Hemos pulsado "Aceptar"

					if (menuType == MENU_CUST_EDIT_NAME)
					{
						custName = formEditStr;
					}
					//#ifndef REM_FANTASY
					else if (menuType == MENU_FANTASY_EDIT_NAME)
					{
						fantasyName = formEditStr;
					} 
					//#endif
					else {
						league.playerSetName(league.userTeam.playerIds[playerSelectedToChangeName], formEditStr);
					}

					formListCMD = formActionAcept;
					return true;
				}
			} else {
			// Solo agregamos letra si la celda coniene una letra, pues las ultimas celdas suelen estar vacias.
				if (formListPos < gameText[TEXT_ABECEDARIO][0].length())
				{
				// Si hemos superado el maximo de letras para el nombre, eliminamos la ultima letra para alojar a la nueva.
					if (formEditStr.length() == FORM_EDIT_MAX_CHARS) {formEditStr = formEditStr.substring(0, formEditStr.length()-1);}
				// A�adimos nueva letra marcada
					formEditStr += gameText[TEXT_ABECEDARIO][0].substring(formListPos, formListPos+1);
					formEditStr = convMay2Min(formEditStr);
				}
			}

			formShow = true;
		}

		if (menuType == MENU_CUST_EDIT_NAME)
		{
			menuListTitleB = formEditStr + "_";
		}
	break;



//#ifndef REM_LEAGUE_GRAPHIC
	case FORM_GRAPHIC:

	// Repeticion de tecla
		if (keyY == 0) {keyRepeatY = 0;} else if (keyY == lastKeyY && ++keyRepeatY > 6) {lastKeyY = 0; keyRepeatY--;}

	// Controlamos desplazamiento vertical
		if (keyY != 0 && lastKeyY == 0)
		{
			formListIni += keyY;

			if (formListIni < 0) {formListIni = 0;}
			else
			if (formListIni > league.teams[league.userLeague].length - formListView) {formListIni = league.teams[league.userLeague].length - formListView;}

			formShow = true;
		}

//#ifndef REM_FINANCES_ATTENDANCE		
		
	case FORM_GRAPHBAR:

	// Controlamos si pulsamos ACEPT
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionAcept;
			return true;
		}


	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionBack;
			return true;
		}

	break;
//#endif
//#endif


	}

	return false;
}


// -------------------
// form List String Width :: Calculamos el ancho maximo de la "columna" indicada.
// ===================

public int formListStringWidth(int index)
{
	int width = 0;
	for (int i=0 ; i<formListStr.length ; i++)
	{
	// Solo procesamos las lineas con RENDER especifico para FORM-TABLE
		if ((formListDat[i][0] & LINE_NO_CALC) == 0)
		{
			try {
				int size = printStringWidth(formListStr[i][index]);
				if (width < size) {width = size;}
			} catch(Exception e) {}
		}
	}

	return width;
}


// -------------------
// form Draw
// ===================

public void formDraw()
{
// Debemos refrescar el formulario o algun sub-componente?
	if (!formShow)
	{
		if (formTableLineShow)
		{
			formTableLineShow = false;
			fillDraw(SUBMENU_COLORS[formBaseColor], formBodyX, formBoxY + ((formListPos-formListIni) * formListEnter), formBodyWidth, formBoxHeight);
			formTableColumLineDraw(formListPos, (formListPos-formListIni) * formListEnter);
		}


		return;
	}
	formShow = false;
	formTableLineShow = false;


	if(!floatingForm) {
		
		// Borramos pantalla
		fillDraw(0x000000, 0, 0, canvasWidth, canvasHeight);


		// Pintamos pantalla de fondo de los menus
		if (backgroundImg != null)
		{
			imageDraw(backgroundImg);
		} else {
			fillDraw(0x003300, 0, 0, canvasWidth, canvasHeight);
		}


		// Solicitamos la impresion de la barra de titulos
		titleBarShow = true;
		
	} else {
	
		titleBarShow = false;
	}



//formSwapDraw
	if (formSwapEnabled)
	{
		boolean muestraCostePlayer = false;

		fontInit(FONT_WHITE);

		int opWidth = printStringWidth("WW") + FORM_TABLE_MIN_SEPARATOR;

		
		//#ifndef REM_FANTASY
		int swapWidth = (opWidth*5) + FORM_SEPARATOR+(menuType==MENU_FANTASY_CHOOSE_PLAYERS? printStringWidth("88.888 M"):0);
		//#else
		//#endif

	// Fondo del SWAP
		fillDraw(0x000000, 0, formSwapY, swapWidth, formListEnter);

		fillDraw(0x424242, 0, formSwapY + formListEnter,                     swapWidth, formListEnter);
		fillDraw(0x636363, 0, formSwapY + formListEnter + formListEnter - 1, swapWidth, 1);

		fillDraw(0x424242, 0, formSwapY + (formListEnter*2),                     swapWidth, formListEnter);
		fillDraw(0x636363, 0, formSwapY + (formListEnter*2) + formListEnter - 1, swapWidth, 1);



	// Titulo del SWAP
		fontInit(FONT_ORANGE);
		printSetArea(0, formSwapY, opWidth, formListEnter);
		printDraw(getMenuText(MENTXT_PS_SH_PA_SP_DE)[0],  0,          0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
		printDraw(getMenuText(MENTXT_PS_SH_PA_SP_DE)[1],  opWidth,    0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
		printDraw(getMenuText(MENTXT_PS_SH_PA_SP_DE)[2], (opWidth*2), 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
		printDraw(getMenuText(MENTXT_PS_SH_PA_SP_DE)[3], (opWidth*3), 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
		printDraw(getMenuText(MENTXT_PS_SH_PA_SP_DE)[4], (opWidth*4), 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);

		//#ifndef REM_FANTASY
		if (menuType==MENU_FANTASY_CHOOSE_PLAYERS)
		{
			printDraw(moneyUnit(), (opWidth*5), 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
		}
		//#endif


		fontInit(FONT_WHITE);

	// Primera fila del SWAP
		int playerPos = formListPos-1;

		//#ifndef REM_FANTASY
		int playerId = (menuType==MENU_FANTASY_CHOOSE_PLAYERS? fantasyPlayersId[playerPos]:league.userTeam.playerIds[playerPos]);
		//#else
		//#endif
		
		int addY = formListEnter;

		if (playerId >= 0)
		{
			if (formSwapSelected >= 0)
			{
				playerPos = formSwapSelected-1;
				//#ifndef REM_FANTASY
				playerId = (menuType==MENU_FANTASY_CHOOSE_PLAYERS? fantasyPlayersId[playerPos]:league.userTeam.playerIds[playerPos]);
				//#else
				//#endif
			}


			printDraw(getMenuText(MENTXT_LST_POSITION)[league.playerGetPosition( playerId)], 0, addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			printDraw(""+league.playerGetSkill(playerId, 0),  opWidth,    addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			printDraw(""+league.playerGetSkill(playerId, 1), (opWidth*2), addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			printDraw(""+league.playerGetSkill(playerId, 2), (opWidth*3), addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			printDraw(""+league.playerGetSkill(playerId, 3), (opWidth*4), addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);

			muestraCostePlayer = true;

			addY += formListEnter;
	
		// Segunda fila del SWAP
			if (formSwapSelected >= 0)
			{
				playerPos = formListPos-1;
				playerId = league.userTeam.playerIds[playerPos];
	
				printDraw(getMenuText(MENTXT_LST_POSITION)[league.playerGetPosition(playerId)], 0, addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
				printDraw(""+league.playerGetSkill(playerId, 0),  opWidth,    addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
				printDraw(""+league.playerGetSkill(playerId, 1), (opWidth*2), addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
				printDraw(""+league.playerGetSkill(playerId, 2), (opWidth*3), addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
				printDraw(""+league.playerGetSkill(playerId, 3), (opWidth*4), addY, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			}
		}


	// Si estamos en Fantasy com.mygdx.mongojocs.lma2007.Game, mostramos el dinero que nos queda
		//#ifndef REM_FANTASY
		if (menuType==MENU_FANTASY_CHOOSE_PLAYERS)
		{
			printSetArea(0, formSwapY + formListEnter, swapWidth, formListEnter);

			if (muestraCostePlayer)
			{
				printDraw(moneyStr(playerCost(playerId), false), -FORM_TABLE_MIN_SEPARATOR, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			}

			printDraw(getMenuText(MENTXT_CLUB_FUNDS)[0]+": " + moneyStr(getAvailableCash()-fantasyTotalCost, true), 0, formListEnter, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
			fontInit(FONT_ORANGE);
			printDraw(getMenuText(MENTXT_CLUB_FUNDS)[0]+": ", 0, formListEnter, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
		}
		//#endif
	}





	switch (formBodyMode)
	{
	case FORM_TABLE:

	// Seleccionamos font
		fontInit(FONT_WHITE);

	// Controlamos si hay que hacer scroll vertical
		if (formListIni > formListPos - (formTableTitle? 1:0) ) {formListIni = formListPos - (formTableTitle? 1:0);}
		else
		if (formListIni < formListPos-formListView+1) {formListIni = formListPos-formListView+1;}


	// Pintamos imagen del campo
		if (formCampoImg != null)
		{
//#ifdef REM_CAMPO_IMAGE
//#else
			formCampoDraw(0, formBoxY, formCampoImg.getWidth(), formCampoImg.getHeight());
//#endif
		}


	// Pintamos opcion Ciclica si existe
		formCicleDraw(formBodyX, formBoxY - (formListEnter*2), formBodyWidth, formListEnter, formBoxX - formBodyX);


	// Pintamos todas las opciones de la Tabla
		for (int i=0 ; i<formListView ; i++)
		{
			int addY = i * formListEnter;


			int currLine = formListIni + i;

			if (i == 0 && formTableTitle) {currLine = 0;}

		// Pintamos color de fondo de una linea
			if (formTableOption && currLine == formListPos)
			{
			//#ifdef S30
			//#else
				fillDraw(SUBMENU_COLORS[formBaseColor], formBodyX, formBoxY + addY, formBodyWidth, formBoxHeight);
		    //#endif
			} else {
				if ((formListDat[currLine][0] & LINE_SOMBRA) != 0)
				{
					fillDraw(0x000000, formBodyX, formBoxY + addY, formBodyWidth, formBoxHeight);
				} else {
					fillDraw(0x424242, formBodyX, formBoxY + addY, formBodyWidth, formBoxHeight);
					fillDraw(0x636363, formBodyX, formBoxY + addY + formListEnter - 1, formBodyWidth, 1);
				}
			}


		// Pintamos numeracion de lineas si esta opcion esta activada
			if ((i != 0 || !formTableTitle) && formTableNumerar)
			{
				printSetArea(formBodyX - FORM_TABLE_MIN_SEPARATOR, formBoxY + addY, 1, formBoxHeight);
				fontInit(FONT_WHITE);
				printDraw(""+(formTableTitle? currLine:currLine+1), 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			}

			switch (formTableRender)
			{
			case TABLE_COLUM:

				formTableColumLineDraw(currLine, addY);
			break;


			case TABLE_INFO:



				int curX = formBoxX;
				for (int t=0 ; t<formTableItems.length ; t++)
				{
					if (t >= formListStr[currLine].length) {continue;}

					printSetArea(curX, formBoxY + addY, formBoxWidth-curX+formBoxX, formBoxHeight);
	
					int sprite = (formTableItems[t]>>8) & 0xff;
					int align = (formTableItems[t]) & 0xff;
	
					if (sprite < 0x80)
					{
						fontInit(sprite);
	
					// Si se especifica font a nivel de linea horizontal, esta se asume para toda la linea
						int font = (formListDat[currLine][0]>>8) & 0xff;
						if (font != 0) {fontInit(font);}

						String str = formListStr[currLine][t];
	
						printDraw(str, 0, 0, fontPenRGB, align|PRINT_VCENTER);
						curX += printStringWidth(str) + FORM_TABLE_MIN_SEPARATOR;
					} else {
						printDraw(formItemsImg, 0, 0, sprite & 0x7f, align|PRINT_VCENTER, formItemsCor);
						curX += spriteWidth(formItemsCor, sprite & 0x7f) + FORM_TABLE_MIN_SEPARATOR;
					}
				}
			break;
			}
		}

		if(!floatingForm) {
			
			int steps = (formListStr.length - (formTableTitle && formTableOption? 1:0) - (formTableOption? 1:formListView));
			int view = formListView - (formTableTitle && formTableOption? 1:0);

			sliderVDraw(formListPos - (formTableTitle? 1:0), (formTableOption && steps < view? 0:steps), formVsliderX, formVsliderY, formVsliderHeight);
		}
	break;


	case FORM_OPTION:

	// Seleccionamos font
		fontInit(FONT_BIG_WHITE);

	// Controlamos si hay que hacer scroll vertical
		if (formListIni > formListPos) {formListIni = formListPos;}
		else
		if (formListIni < formListPos-formListView+1) {formListIni = formListPos-formListView+1;}


	// Pintamos Titulo si existe
		if (formTitleStr != null)
		{
			printSetArea(formBoxX, formBoxY, formBoxWidth, formBoxHeight);
			printDraw(formTitleStr, 0, -(formListEnter * formTitleStr.length), fontPenRGB, PRINT_TOP|PRINT_LEFT);
		}

	// Pintamos todas las opciones del menu
		for (int i=0 ; i<formListView ; i++)
		{
			int addY = i * formListEnter;

			// Pintamos opcion seleccionada
				int frameBase = ((formListIni + i == formListPos)? ITEM_OPTION_BAR+2 : ITEM_OPTION_BAR);

				fontInit((formListIni + i == formListPos)? FONT_BIG_YELLOW:FONT_BIG_BLACK);

			// Renderizamos la linea de fondo mediante sprites
				int width = spriteWidth(formItemsCor, frameBase);
				int veces = formBodyWidth / width; if (formBodyWidth % width != 0) {veces++;}
				for (int x=0 ; x<veces ; x++)
				{
					int destX = regla3(x, veces, formBodyWidth);
					spriteDraw(formItemsImg, formBodyX + destX, formBoxY + addY, (x==veces-1? frameBase+1:frameBase), formItemsCor);
			
					if (formListIni + i == formListPos && x==veces-1)
					{
						spriteDraw(formItemsImg, 0, 0, ITEM_ARROW_RIGHT, formItemsCor, PRINT_FIT|PRINT_HCENTER|PRINT_VCENTER, formBodyX + destX, formBoxY + addY, width, formBoxHeight);

						if (formListStr[formListIni + i].length > 1)
						{
							spriteDraw(formItemsImg, 0, 0, ITEM_ARROW_LEFT, formItemsCor, PRINT_FIT|PRINT_HCENTER|PRINT_VCENTER, formBodyX, formBoxY + addY, width, formBoxHeight);
						}
					}
				}
	
		// Cortamos texto para que no se salga de la pantalla
			//String str = printTextCut(formListStr[formListIni+i][formListDat[formListIni+i][2]], formBoxWidth);


		// Controlamos scroll horizontal si es necesario
			if (formListScrollPos != formListPos && formListIni + i == formListPos)
			{
				formListScrollPos = formListPos;
				formListScroll = 0;
				formListScrollSized = printStringWidth(formListStr[formListIni+i][formListDat[formListIni+i][2]]);
				if (formListScrollSized > formBoxWidth)
				{
					formListScrollCnt = 15;
				} else {
					formListScrollCnt = -1;
				}
			}

		// Pintamos texto ya cortado
			printSetArea(formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
			printDraw(formListStr[formListIni+i][formListDat[formListIni+i][2]], (formListIni + i == formListPos? formListScroll:0), 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER|PRINT_MASK);

			if (formListNum != null)
			{
				printSetArea(formBodyX + formBodyWidth - (width*2), formBoxY + addY, width, formBoxHeight);
				printDraw(""+formListNum[formListIni + i], 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

				printX -= width;
				printDraw(formItemsImg, 0, 0, ITEM_ARROW_LEFT, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
			}

			//#ifndef REM_BONUS_CODES
			if (formBonusCodes != null && formListIni + i < formBonusCodes.length)
			{
				printSetArea(formBoxX - width, formBoxY + addY, width, formBoxHeight);
				printDraw(formItemsImg, 0, 0, (formBonusCodes[formListIni + i]==0? ITEM_CHECK_NO:ITEM_CHECK_OK), PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
			}
			//#endif
			//#ifndef REM_FANTASY
			if (menuType == MENU_FANTASY_COLOUR)
			{
				printSetArea(formBodyX + formBodyWidth - (width*2), formBoxY + addY, width, formBoxHeight);
				fillDraw(fantasyRGB[fantasyRGBidx[i]], printX+2, printY+2, printWidth-4, printHeight-4);
				printX -= width;
				printDraw(formItemsImg, 0, 0, ITEM_ARROW_LEFT, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
			}
			//#endif
		}

//rectDraw(-1, formBodyX, formBodyY, formBodyWidth, formBodyHeight);

	break;


	case FORM_LIST:

		fontInit(FONT_WHITE);

	// Pintamos todas las lineas del listado
		printSetArea(formBoxX, formBoxY, formBoxWidth, formBoxHeight);

	// Pintamos fondo del texto
		fillDraw(0x434343, printX, printY, printWidth, printHeight);


	// Pintamos Titulo si existe
		if (formTitleStr != null)
		{
			fillDraw(0x000000, printX, printY-formListEnter, printWidth, formListEnter);
			printDraw(formTitleStr, 0, -(formListEnter * formTitleStr.length), fontPenRGB, PRINT_TOP|PRINT_LEFT);
		}


		for (int i=0 ; i<formListView ; i++)
		{
			fillDraw(0x626060, formBoxX, formBoxY + ((i+1)*formListEnter)-2, formBoxWidth, 1);

		// Pintamos texto
			printDraw(formListStr[formListIni+i][formListDat[formListIni+i][2]], 0, i*formListEnter, fontPenRGB, PRINT_HCENTER);
		}


		sliderVDraw(formListIni, formListStr.length - formListView, formVsliderX, formVsliderY, formVsliderHeight);


//rectDraw(-1, formBodyX, formBodyY, formBodyWidth, formBodyHeight);
//rectDraw(0x00ff00, formBoxX, formBoxY, formBoxWidth, formBoxHeight);
	break;

	
	case FORM_VERSUS:

		if (backgroundImg != null)
		{
			imageDraw(backgroundImg);
		} else {
			fillDraw(0x003300, 0, 0, canvasWidth, canvasHeight);
		}

		fontInit(FONT_WHITE);
		formBoxHeight = SHIELD_HEIGHT + (printFontHeight * 2);
		formBoxY = formBodyY + ((formBodyHeight - formBoxHeight) / 2);

		fillDraw(0x424242, 0, formBoxY, canvasWidth, formBoxHeight);
		fillDraw(0x000000, 0, formBoxY - printFontHeight, canvasWidth, printFontHeight);
		fillDraw(0x000000, 0, formBoxY + formBoxHeight, canvasWidth, printFontHeight);

		if (!formVersusResult)
		{
			printSetArea(0, formBoxY, canvasWidth, formBoxHeight);
			printDraw(league.userMatch[0].name, 0, -printFontHeight, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
			printDraw(league.userMatch[1].name, 0, +printFontHeight, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
			fontInit(FONT_ORANGE);
			printDraw(getMenuText(MENTXT_VERSUSABBR)[0], 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
	
			int maxWidth = Math.max(printStringWidth(league.userMatch[0].name), printStringWidth(league.userMatch[1].name));

			//#ifndef REM_TEAMSHIELDS
			int sobra = ((canvasWidth-maxWidth)/2);
			cellDraw(matchShieldsImg[0], (sobra - SHIELD_WIDTH)/2, formBoxY + ((formBoxHeight - SHIELD_HEIGHT)/2),  league.userMatch[0].teamLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, wisth,height)
			cellDraw(matchShieldsImg[1], canvasWidth - sobra + (sobra - SHIELD_WIDTH)/2, formBoxY + ((formBoxHeight - SHIELD_HEIGHT)/2),  league.userMatch[1].teamLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, wisth,height)
			//#endif

		} else {

		// Mostramos resultado del partido

		//#ifndef FEA_SHOW_VERSUS_SIMPLE
			int maxWidth = Math.max(printStringWidth(league.userMatch[0].name), printStringWidth(league.userMatch[1].name));

			String a = printTextCut(league.userMatch[0].name, (canvasWidth/2)-FORM_SEPARATOR);
			String b = printTextCut(league.userMatch[1].name, (canvasWidth/2)-FORM_SEPARATOR);

			printSetArea(printFontHeight, formBoxY, maxWidth, formBoxHeight);

			if (maxWidth > (canvasWidth/2)-FORM_SEPARATOR)
			{
				maxWidth = (canvasWidth/2)-FORM_SEPARATOR;
				printX = FORM_SEPARATOR/2;
				printWidth = maxWidth;
			}

			printDraw(a, 0, -printFontHeight/2, fontPenRGB, PRINT_HCENTER|PRINT_BOTTOM);
		//#ifndef REM_TEAMSHIELDS
			cellDraw(matchShieldsImg[0], printX + ((printWidth - SHIELD_WIDTH)/2), printY + ((printHeight - SHIELD_HEIGHT)/2) -(printFontHeight/2),  league.userMatch[0].teamLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, wisth,height)
		//#endif
			printX = canvasWidth - printWidth - printX;
	
			printDraw(b, 0, -printFontHeight/2, fontPenRGB, PRINT_HCENTER|PRINT_BOTTOM);
		//#ifndef REM_TEAMSHIELDS
			cellDraw(matchShieldsImg[1], printX + ((printWidth - SHIELD_WIDTH)/2), printY + ((printHeight - SHIELD_HEIGHT)/2) -(printFontHeight/2),  league.userMatch[1].teamLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, wisth,height)
		//#endif
			printX = 0;
			printWidth = canvasWidth;
			fontInit(FONT_BIG_WHITE);
	
			printDraw(""+league.userMatch[0].matchGoals+getMenuText(MENTXT_VERSUSABBR)[1]+league.userMatch[1].matchGoals, 0, -printFontHeight/2, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

	    //#else
		//#endif
		}

	// Pintamos opcion Ciclica si existe
		fontInit(FONT_WHITE);
		formCicleDraw(0, formBoxY + formBoxHeight, canvasWidth, printFontHeight, formListEnter);

		break;

//#ifndef REM_TEXTMATCH
		
	case FORM_CONSOLE:

		// Pintamos todas las lineas del listado
			printSetArea(formBoxX, formBoxY, formBoxWidth, formBoxHeight);

			fontInit(FONT_WHITE);
			for (int i=0 ; i<formListView ; i++)
			{
			// Pintamos texto
				//printDraw(formListStr[formListIni+i][0], 0, i*formListEnter, fontPenRGB, PRINT_LEFT);
				printDraw(formListStr[formListStr.length - 1 - i][0], 0,  -i*formListEnter, fontPenRGB, PRINT_LEFT|PRINT_BOTTOM);
			}
			
		break;		

//#endif




	case FORM_EDIT_NAME:

	// Renderizamos caja donde editamos nombre
		printSetArea(formBoxX, formBoxY, formBoxWidth, formBoxHeight);
		if (menuType == MENU_CUST_EDIT_NAME)
		{
			fontInit(FONT_BIG_WHITE);
			printDraw(getMenuText(MENTXT_CUST_INSERT_NAME)[0], 0, 1, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		} else {

			fillDraw(0x000000, 0, printY, canvasWidth, printHeight);
			fontInit(FONT_ORANGE);
			printDraw(getMenuText(MENTXT_NAME)[0]+":", 0, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);

			fontInit(FONT_WHITE);
			printDraw(formEditStr+"_", printStringWidth(getMenuText(MENTXT_NAME)[0]+":")+FORM_SEPARATOR, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
		}


	// Fondo de las celdas "ABCD..."
		fillDraw(0x63846B, 0, formCellY - (FORM_SEPARATOR/2), canvasWidth, ((formCellNumHeight + 1) * formCellHeight) + FORM_SEPARATOR);


	// Fondo de "Aceptar / Borrar"
		fillDraw(0xB4B7B2, 0, formCellY + (formCellNumHeight * formCellHeight), canvasWidth, formCellHeight);


	// Renderizamos celdas "ABCD..."
		for (int cnt=0, y=0; y<formCellNumHeight ; y++)
		{
			for (int x=0; x<formCellNumWidth ; x++)
			{
				int addX = x*formCellWidth;
				int addY = y*formCellHeight;
				printSetArea(formCellX+addX, formCellY+addY, formCellWidth, formCellHeight);
				int rgb = (cnt == formListPos? FORM_RGB_MARC:FORM_RGB_BOX_DISABLED);

				if (cnt == formListPos)
				{
					fillDraw(0xB4B7B2, formCellX+addX, formCellY+addY, formCellWidth+1, formCellHeight+1);
					rectDraw(0xffffff, formCellX+addX, formCellY+addY, formCellWidth+1, formCellHeight+1);
				}

				if (cnt < gameText[TEXT_ABECEDARIO][0].length())
				{
					fontInit( cnt != formListPos? FONT_BIG_YELLOW:FONT_BIG_BLACK);
					printDraw(gameText[TEXT_ABECEDARIO][0].substring(cnt++,cnt), 1,1, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
				} else {
					cnt++;
				}

			}
		}

	// Miramos si "Borrar" o "Aceptar" estan seleccionados
		boolean eraseBox = false;
		boolean aceptBox = false;

		if (formListPos >= (formCellNumWidth*formCellNumHeight))
		{
			if (formListPos < ((formCellNumWidth*formCellNumHeight)+(formCellNumWidth/2))) {aceptBox = true;} else {eraseBox = true;}
		}


	// Renderizamos celda "Aceptar"
		printSetArea(formCellX, formCellY + (formCellNumHeight * formCellHeight), (formCellNumWidth * formCellWidth) / 2, formCellHeight);
		int rgb = ( aceptBox? FORM_RGB_BOX_SELECTED:FORM_RGB_BOX_DISABLED);
		fontInit(FONT_BIG_BLACK);
		printDraw(gameText[TEXT_OK_DELETE][0], 0,1, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		if (aceptBox)
		{
			rectDraw(0xffffff, printX, printY, printWidth+1, printHeight+1);
		}

	// Renderizamos celda "Borrar"
		printSetArea(formCellX + (formCellNumWidth * formCellWidth) / 2, formCellY + (formCellNumHeight * formCellHeight), (formCellNumWidth * formCellWidth) / 2, formCellHeight);
		rgb = ( eraseBox? FORM_RGB_BOX_SELECTED:FORM_RGB_BOX_DISABLED);
		fontInit(FONT_BIG_BLACK);
		printDraw(gameText[TEXT_OK_DELETE][1], 0,1, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		if (eraseBox)
		{
			rectDraw(0xffffff, printX, printY, printWidth+1, printHeight+1);
		}
	break;



//#ifndef REM_LEAGUE_GRAPHIC
	case FORM_GRAPHIC:

		fontInit(FONT_WHITE);

		int formGraphicX = formBodyX + (FORM_SEPARATOR + printStringWidth("28") + FORM_SEPARATOR);
		int formGraphicWidth = formBodyWidth - (FORM_SEPARATOR + printStringWidth("28") + FORM_SEPARATOR + FORM_SLIDER_WIDTH);


		int formGraphicColum = league.leagueJourneys.length;
		int formGraphicFiles = league.teams[league.userLeague].length;


		int formGraphicCellWidth = formGraphicWidth / formGraphicColum;
		int formGraphicCellHeight = printFontHeight;

		int formGraphicHeight = formBodyHeight - printFontHeight - FORM_SEPARATOR;



		formListView = formGraphicHeight / formGraphicCellHeight;
		if (formListView > formGraphicFiles)
		{
			formListView = formGraphicFiles;
		}


		formGraphicHeight = formGraphicCellHeight * formListView;

//		int formGraphicY = formBodyY + printFontHeight + ((formBodyHeight - (formListView * formGraphicCellHeight))/2);
		int formGraphicY = formBodyY + printFontHeight;



	// Pintamos numero de jornada (fila horizontal superior)
		for (int i=0 ; i<=6 ; i++)
		{
			int x = regla3(i, 6, formGraphicColum-1);
			printSetArea(formGraphicX + (x*formGraphicCellWidth), formGraphicY - printFontHeight, formGraphicCellWidth, printFontHeight);
			fontInit(FONT_WHITE);
			printDraw(""+(x+1), 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		}

		for (int y=0 ; y<formListView ; y++)
		{
		// Pintamos lineas horizontales de fondo
			printSetArea(formGraphicX, formGraphicY + (y*formGraphicCellHeight), formGraphicWidth, formGraphicCellHeight);
			fillDraw(0x424242, printX, printY, printWidth, printHeight);
			fillDraw(0x636363, printX, printY + printHeight - 3, printWidth, 1);

		// Pintamos numero de posicion en la clasificacion (columna vertical izquierda)
			printSetArea(printX - FORM_SEPARATOR, printY, 1, printHeight);
			fontInit(FONT_WHITE);
			printDraw(""+(formListIni+y+1), 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
		}


		int baseY = formListIni * formGraphicCellHeight;

		if (league.currentWeek > 0)
		{
			int xOld=0, yOld=0;
			for (int i=0 ; i<league.currentWeek ; i++)
			{
				int x = formGraphicX + (i * formGraphicCellWidth) + (formGraphicCellWidth/2);
				int y = formGraphicY + ((league.userTeamLeagueHistorial[i]+1) * formGraphicCellHeight)-3-baseY;
				
				if ( i!= 0)
				{
					scr.setClip(formGraphicX, formGraphicY, formGraphicWidth, formGraphicHeight);
					putColor(0xC64A5A);
					scr.drawLine(x, y, xOld,yOld);
				}
	
				xOld = x;
				yOld = y;
			}
	
	
			for (int i=0 ; i<league.currentWeek ; i++)
			{
				if (league.userTeamLeagueHistorial[i] >= formListIni && league.userTeamLeagueHistorial[i] < formListIni + formListView )
				{
					int x = formGraphicX + (i * formGraphicCellWidth) + (formGraphicCellWidth/2);
					int y = formGraphicY + ((league.userTeamLeagueHistorial[i]+1) * formGraphicCellHeight)-3-baseY;
				
					fillDraw(0xffffff, x -1, y -1, 3,3);
				}
			}
		}

		sliderVDraw(formListIni, league.teams[league.userLeague].length - formListView, formGraphicX+formGraphicWidth, formGraphicY, formGraphicHeight);
	break;
//#endif


//#ifndef REM_FINANCES_ATTENDANCE
	case FORM_GRAPHBAR:

	// Calculamos maximo de publico y jornadas jugadas en nuestro estadio
		int publicoMax = getStadiumCapacity();
		int barrasNum = 0;
		int[] barrasDat = new int[league.currentWeek];
		for (int i=0 ; i<league.currentWeek ; i++)
		{
			if (attendanceHistorial[i] > 0) {barrasDat[barrasNum++] = attendanceHistorial[i];}

			if (publicoMax < attendanceHistorial[i]) {publicoMax = attendanceHistorial[i];}

		//#ifdef DEBUG
			Debug.println (": "+attendanceHistorial[i]);
	    //#endif
		}

		fontInit(FONT_WHITE);

		formListView = formBodyHeight / printFontHeight;

	// Calculamos ancho de la columna de 'numero de espectadores'
		int maxWidth = 0;
		for (int y=0 ; y<formListView ; y++)
		{
			String num = ""+regla3( (formListView-2)-(y-1), formListView-2, publicoMax);
			if (num.length() > 2) {num = num.substring(0, num.length()-2) + "00";}
			int width = printStringWidth(num); if (maxWidth < width) {maxWidth = width;}
		}


	// Pintamos lineas de fondo horizontales y la primera columna de 'numero de espectadores'
		for (int y=0 ; y<formListView ; y++)
		{
		// Calculamos area donde dibujar una linea
			printSetArea(formBodyX, formBodyY + (y*printFontHeight), formBodyWidth, printFontHeight);

			if (y == 0)
			{
			// Pintamos linea horizontal que hace de titulo
				fillDraw(0x000000, printX, printY, printWidth, printHeight);
			} else {
			// Pintamos lineas horizontales de fondo
				fillDraw(0x424242, printX, printY, printWidth, printHeight);
				fillDraw(0x636363, printX, printY + printHeight - 1, printWidth, 1);

			// Pintamos numero de posicion en la clasificacion (columna vertical izquierda)
				String num = ""+regla3( (formListView-2)-(y-1), formListView-2, publicoMax);
				if (num.length() > 2) {num = num.substring(0, num.length()-2) + "00";}
				printWidth = maxWidth;
				printDraw(num, FORM_SEPARATOR, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
			}
		}


	// Si no hay barras que mostrar, no mostramos nada
		if (barrasNum == 0) {break;}


		int barraWidth = formBodyWidth - (maxWidth + FORM_SEPARATOR + FORM_GRAPHBAR_SEPARATOR);
		int barraX = formBodyX + (maxWidth + FORM_SEPARATOR + FORM_GRAPHBAR_SEPARATOR);

		int barraView = barraWidth / (FORM_GRAPHBAR_WIDTH + FORM_GRAPHBAR_SEPARATOR);

	// Si hay mas 'huecos' para poner barras que barras a mostrar, forzamos los 'huecos' al numero de barras que hay
		int barraIni = 0;
		if (barraView > barrasNum)
		{
			barraView = barrasNum;
		} else {
			barraIni = barrasNum - barraView;
		}

	// Pintamos barras
		for (int i=0 ; i<barraView ; i++)
		{
			int x = regla3(i, barraView, barraWidth);
			int maxHeight = (formListView-2)*printFontHeight;
			int height = regla3(barrasDat[barraIni + i], publicoMax, maxHeight);
			int y = formBodyY + (printFontHeight*2) + (maxHeight - height);

		// Fade de colores
			for (int y2=0 ; y2<height ; y2++)
			{
				fillDraw(fadeColor(0xC6CE73, 0xCE7B42, regla3(y2, height, 256)), barraX + x, y + y2, FORM_GRAPHBAR_WIDTH, 1);
			}
		}
	break;

//#endif	

	//#ifndef REM_CALENDAR
	case FORM_CALENDAR:
	
		// Pintamos calendario
		calendarDraw();	
	break;
	//#endif

//#ifndef REM_FOOTBALL_ONE
	case FORM_FOOTBALL_ONE:

		imageDraw(fOneMainImg, 0, 0);
		fillDraw(0xFFD75A, 0, formBodyY, canvasWidth, canvasHeight - formBodyY);
		fillDraw(0x000000, 0, canvasHeight - fOneLogoImg.getHeight(), canvasWidth, fOneLogoImg.getHeight());
		imageDraw(fOneLogoImg, canvasWidth - fOneLogoImg.getWidth(), canvasHeight - fOneLogoImg.getHeight());

		fontInit(FONT_WHITE);

		printSetArea(10, 10, canvasWidth, printFontHeight);
		printDraw(getFormattedDate(), 0, 0, fontPenRGB, PRINT_LEFT|PRINT_TOP);

		printSetArea(10, 23, canvasWidth, printFontHeight);
		printDraw(getMenuText(MENTXT_NEWSFLASHES)[0], 0, 0, fontPenRGB, PRINT_LEFT|PRINT_TOP);


		int view = (formBodyHeight / printFontHeight)+1;
		for (int i=0 ; i<view ; i++)
		{
			printSetArea(0, formBodyY + (i*printFontHeight), canvasWidth, printFontHeight);
		// Pintamos lineas horizontales de fondo
			fillDraw(0x424242, printX, printY, printWidth, printHeight);
			fillDraw(0x636363, printX, printY + printHeight - 1, printWidth, 1);
		}



		printSetArea(0, formBodyY, formBodyWidth, printFontHeight);
		for (int i=0 ; i<formListView ; i++)
		{
			printDraw(formListStr[formListIni+i][formListDat[formListIni+i][2]], 0, i*printFontHeight, fontPenRGB, PRINT_HCENTER);
		}


		sliderVDraw(formListIni, formListStr.length - formListView, formVsliderX, formVsliderY, formVsliderHeight);


	// Pintamos opcion Ciclica si existe
		formCicleDraw(0, formBodyY + (view * printFontHeight), canvasWidth - fOneLogoImg.getWidth(), printFontHeight, formBoxX - formBodyX);


		titleBarShow = false;
	break;
//#endif


	case FORM_LOADING:

		imageDraw(loadImage("/loading"));

		loadingDraw();
/*
		fontInit(FONT_WHITE);
		printSetArea(0, 0, canvasWidth, canvasHeight);
		printDraw(gameText[TEXT_LOADING][0], 0, (canvasHeight/3)*2, fontPenRGB, PRINT_HCENTER);
*/
	break;
	}


//#ifndef REM_TEAMSHIELDS
// formShieldDraw :: Mostramos escudo del equipo en el menu de seleccion de equipo
	if (formShieldImg != null)
	{
	// Pintamos escudo
		cellDraw(formShieldImg, canvasWidth/3, formBoxY+(formListEnter*formListView),  xLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, wisth,height)

	// Pintamos balon de futbol sobre las lineas de opciones
		printSetArea(formBodyX, formBoxY-FORM_SEPARATOR, formBodyWidth, 0);
		printDraw(formItemsImg, 0, 0, ITEM_BALLON, PRINT_RIGHT|PRINT_BOTTOM, formItemsCor);
	}
//#endif

// formRestDraw :: Mostramos imagen asociada al Training Schedule
	if (formRestEnabled)
	{
		//#ifndef REM_TRAINING_IMAGE
		// Pintamos balon de futbol sobre las lineas de opciones
		printSetArea(formBodyX, formBoxY+(formListEnter*formListView) + FORM_SEPARATOR, formBodyWidth, spriteHeight(trainingCor, ITEM_IMAGES));
	
		printDraw(trainingImg, 0, 0, ITEM_IMAGES + formListPos -1, PRINT_LEFT|PRINT_TOP, trainingCor);

		// Pintamos el resto de puntos disponibles
		int ejeX = printX + spriteWidth(trainingCor, ITEM_IMAGES);
	//#else		
	//#endif
		
		fontInit(FONT_BIG_YELLOW);

		printSetArea(ejeX, printY + ((printHeight - printFontHeight)/2), canvasWidth - ejeX, printFontHeight);
		fillDraw(0x434343, printX, printY, printWidth, printHeight);
		printDraw(""+trainingSchedule[trainingLine][0], -FORM_SLIDER_WIDTH, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
		printDraw(getMenuText(MENTXT_TRAINING_REST)[0], -FORM_SLIDER_WIDTH - printStringWidth("199"), 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
	}


//#ifndef REM_STADIUM
// formStadiumDraw :: Mostramos lineas adicionales del Stadium
	if (formStadiumEnabled)
	{
	// Pintamos fondo
		int baseY = formBoxY + (formListView*formListEnter);
		fillDraw(0x000000, formBodyX, baseY, formBodyWidth, formBoxHeight);
		baseY += formListEnter;
		fillDraw(0x424242, formBodyX, baseY, formBodyWidth, formBoxHeight);
		fillDraw(0x636363, formBodyX, baseY + formListEnter - 1, formBodyWidth, 1);
		baseY += formListEnter;
		fillDraw(0x424242, formBodyX, baseY, formBodyWidth, formBoxHeight);
		fillDraw(0x636363, formBodyX, baseY + formListEnter - 1, formBodyWidth, 1);

	// Pintamos letras
		fontInit(FONT_WHITE);
		printSetArea(formBoxesX[0], formBoxY + ((formListView+1)*formListEnter), formBoxesW[0], formBoxHeight);
		printDraw(getMenuText(MENTXT_COST)[0], 0, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
		printSetArea(formBoxesX[1], printY, formBoxesW[1], formBoxHeight);
		String cs = ""+moneyStr(stadiumCost[formListPos-1][stadiumLevel[formListPos-1]], true);
		if (stadiumBuildStage[formListPos-1] > 0 || stadiumLevel[formListPos-1] == 4)
			cs = "-";
		printDraw(cs, 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);

		printSetArea(formBoxesX[0], printY + formListEnter, formBoxesW[0], formBoxHeight);
		int t =  -(stadiumBuildStage[formListPos-1]);
		printDraw(t < 0? getMenuText(MENTXT_TIME_LEFT)[0]:getMenuText(MENTXT_BUILD_TIME)[0], 0, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
		printSetArea(formBoxesX[1], printY, formBoxesW[1], formBoxHeight);

		if (t < 0) t++;
		t += stadiumBuildTime[formListPos-1][stadiumLevel[formListPos-1]];
		String ts = ""+t+" "+getMenuText(MENTXT_WEEKS)[0];
		if (stadiumLevel[formListPos-1] == 4)
			ts = "-";
		
		printDraw(ts, 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
	}
//#endif
}


// -------------------
// form Table Colum Line Draw
// ===================

public void formTableColumLineDraw(int currLine, int addY)
{
	for (int t=0 ; t<formListStr[currLine].length ; t++)
	{
		printSetArea(formBoxesX[t], formBoxY + addY, formBoxesW[t], formBoxHeight);
	
		int sprite = (formTableItems[t]>>8) & 0xff;
		int align = (formTableItems[t]) & 0xff;
	
	// Celda de texto?
		if (sprite < 0x80)
		{
		// Tipo de font para esta columna, aunque puede ser remplazada si se especifica a nivel de linea
			fontInit(sprite);
	
		// Si se especifica font a nivel de linea horizontal, esta se asume para toda la linea
			int font = (formListDat[currLine][0]>>8) & 0xff;
			if (font != 0) {fontInit(font);}
	
			if (formTableOption && formListPos == currLine) {fontInit(FONT_BLACK);}
	
		// Al texto de la celda "master" le hacemos un CUT por si el texto se sale...
			boolean useMask = false;
			String str = formListStr[currLine][t];
			if (formTableMasterCell != null)
			{
				for (int c=0 ; c<formTableMasterCell.length ; c++)
				{
					if ((formListDat[currLine][0]&LINE_NO_CALC)==0 && t == formTableMasterCell[c])
					{
						if (!formTableOption || c==0 && currLine != formListPos)
						{
							str = printTextCut(str, printWidth+2);
						} else {
							useMask = true;
						}
						break;
					}
				}
			}
	
		// Controlamos scroll horizontal si es necesario
			boolean forceAlignLeft = useMask && formListScrollCnt > 0;
			if (useMask)
			{
				if (formListScrollPos != formListPos && currLine == formListPos)
				{
					formListScrollPos = formListPos;
					formListScroll = 0;
					formListScrollSized = printStringWidth(str);
					if (formListScrollSized > formBoxesW[formTableMasterCell[0]])
					{
						formListScrollCnt = 15;
						forceAlignLeft = true;
					} else {
						formListScrollCnt = -1;
					}
				}
			}
	
			printDraw(str, (useMask?formListScroll:0), 0, fontPenRGB, (forceAlignLeft?PRINT_LEFT:align)|PRINT_VCENTER|(useMask?PRINT_MASK:0));
	
		} else {
		// Celda de sprite
			if (formListStr[currLine][t] != null && formListStr[currLine][t].length() == 1)
			{
				sprite += formListStr[currLine][t].charAt(0) & 0x0F;
				printDraw(formItemsImg, 0, 0, sprite & 0x7f, align|PRINT_VCENTER, formItemsCor);
			}
			else if (formListStr[currLine][t] == null && ((formTableItems[t] & 0x10000) == 0 || currLine == formListPos))
			{
				printDraw(formItemsImg, 0, 0, sprite & 0x7f, align|PRINT_VCENTER, formItemsCor);
			}
		}
	}
}




// -------------------
// slider V Draw
// ===================

public void sliderVDraw(int pos, int steps, int x, int y, int height)
{
	if (steps > 0)
	{
		printSetArea(x, y, FORM_SLIDER_WIDTH, height);

		y += spriteHeight(formItemsCor, ITEM_SLIDER_ARROWS) + 1;
		height -= (spriteHeight(formItemsCor, ITEM_SLIDER_ARROWS)*2) + 2;
	
		x++;
		int width = FORM_SLIDER_WIDTH - 2;
	
		fillDraw(0x000000, x, y, width, height);
		rectDraw(SUBMENU_COLORS[formBaseColor], x, y, width, height);
	
		x += 2;
		width -= 4;
		y += 2;
		height -= 4;
	
		int barHeight = height / 4;
		int posY = y + regla3(pos, steps, height - barHeight);
		fillDraw(SUBMENU_COLORS[formBaseColor], x, posY, width, barHeight);

	// Pintamos las flechitas de arriba y abajo
		printDraw(formItemsImg, 0, 0, ITEM_SLIDER_ARROWS + (new int[] {1,2,4,3,4,2,5,1,6,0}[formBaseColor]*2), PRINT_TOP, formItemsCor);
		printDraw(formItemsImg, 0, 0, ITEM_SLIDER_ARROWS+1+(new int[] {1,2,4,3,4,2,5,1,6,0}[formBaseColor]*2), PRINT_BOTTOM, formItemsCor);
	}
}



// -------------------
// form Cicle Draw
// ===================

public void formCicleDraw(int x, int y, int w, int h, int add)
{
// Pintamos opcion ciclica
	if (formCicleStr != null)
	{
		fontInit(FONT_ORANGE);
		printSetArea(x, y, w, h);
		fillDraw(0x000000, printX, printY, printWidth, printHeight);
		printDraw(formCicleTitle, add, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);

		fontInit(FONT_WHITE);
		printY += printHeight;

		int barWidth = spriteWidth(formItemsCor, ITEM_OPTION_BAR);

		fillDraw(0x424242, printX, printY, printWidth, printHeight);
		fillDraw(0x636363, printX, printY + printHeight - 1, printWidth, 1);

		printDraw(formCicleStr[formCiclePos], barWidth, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);

		if (formCicleStr.length > 1)
		{
			printWidth = barWidth;
			printDraw(formItemsImg, 0, 0, ITEM_ARROW_LEFT, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
			printX += barWidth + printStringWidth(formCicleStr[formCiclePos]);
			printDraw(formItemsImg, 0, 0, ITEM_ARROW_RIGHT, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
		}
	}
}


// -------------------
// form Campo Draw
// ===================

byte[][][] formCampoEjes = new byte[][][] {
{
	{28,25,  50,22,  72,25},
	{19,52,  42,55,  62,55,  86,52},
	{29,77,  50,77,  72,77},
	{50,92},
},{
	{41,24,  61,24},
	{26,46,  50,54,  71,46},
	{12,71,  31,77,  50,77,  69,77,  89,71},
	{50,92},
},{
	{41,24,  61,24},
	{18,48,  41,53,  61,53,  85,47},
	{18,74,  41,78,  61,78,  85,74},
	{50,92},
},{
	{28,24,  50,22,  74,24},
	{29,47,  50,51,  72,47},
	{18,72,  41,76,  61,76,  85,72},
	{50,92},
},{
	{41,24,  61,24},
	{12,45,  31,54,  50,46,  69,54,  89,45},
	{29,77,  50,77,  72,77},
	{50,92},
},{
	{50,19},
	{18,43,  41,50,  61,50,  85,43},
	{12,72,  31,78,  50,78,  69,78,  89,72},
	{50,92},

},{

	{17,25,  50,21,  82,25},
	{25,45,  50,38,  77,45,  50,59},
	{28,76,  50,76,  71,76},
	{50,92},
},{
	{36,22,  65,22},
	{25,47,  50,39,  75,47,  50,60},
	{19,75,  41,79,  61,79,  85,74},
	{50,92},
}};

public void formCampoDraw(int x, int y, int w, int h)
{
	
//#ifdef REM_CAMPO_IMAGE
//#else
	imageDraw(formCampoImg, x, y);
//#endif

	int starWidth = spriteWidth(formItemsCor, ITEM_STARS) / 2;




	switch (menuType)
	{
// Pintamos "puntos" que identifican reservas y suplentes
	case MENU_SQUAD_SELECT_TEAM:
	case MENU_SQUAD_CHANGE_NEEDED:
	case MENU_SQUAD_INJURED_SUBS_NEEDED:
	case MENU_SUBSTITUTE:
	case MENU_SQUAD_EDIT_NAMES:
//#ifndef REM_FANTASY
	case MENU_FANTASY_CHOOSE_PLAYERS:
//#endif
		int pointColor;
		int width = spriteWidth(formItemsCor, ITEM_RESERVAS) + 1;

		printSetArea(x, y+h+FORM_SEPARATOR, w, 0);
		for (int i=12 ; i<formListStr.length ; i++)
		{
			pointColor = 0;
			if (((formListDat[i][0]>>8)&0xff) == FONT_ORANGE) {pointColor = 3;}
			if (i == formListPos) {pointColor = (pointColor == 3? 2:1);}
			if (i == formSwapSelected) {pointColor = 2;}
			printDraw(formItemsImg, (i-12)*width, 0, ITEM_RESERVAS + pointColor, 0, formItemsCor);
		}

		if (formListPos > 11)
		{
			printSetArea(x, y, w, h);
			fontInit(FONT_BLACK);
			String str = printTextCut((formListPos>=17? getMenuText(MENTXT_RESERVE)[0]:getMenuText(MENTXT_SUBS_BENCH)[0]), printWidth);
			printDraw(str, 0, 0, fontPenRGB, PRINT_HCENTER);
		}
		//#ifndef REM_2DMATCH
		changeFormation = true;    	    	
    	//#endif
	break;

//#ifndef REM_FLECHAS_IMAGE
	case MENU_SQUAD_STYLE:

		switch (formListPos)
		{
		case 0:

			printSetArea(x,y+(h/2), w, h/2);
			printDraw(flechasImg, 0, 0, 2, PRINT_HCENTER|PRINT_TOP, flechasCor);
	
			printSetArea(x,y+(h/2), w/2, h/2);
			printDraw(flechasImg, 0, 0, 2, PRINT_HCENTER|PRINT_TOP, flechasCor);
	
			printSetArea(x+(w/2),y+(h/2), w/2, h/2);
			printDraw(flechasImg, 0, 0, 2, PRINT_HCENTER|PRINT_TOP, flechasCor);
		break;

		case 1:

			printSetArea(x,y, w, h);
			printDraw(flechasImg, 0, FORM_SEPARATOR, 3, PRINT_HCENTER|PRINT_VCENTER, flechasCor);

			printSetArea(x,y, w/2, h);
			printDraw(flechasImg, 0, FORM_SEPARATOR, 3, PRINT_HCENTER|PRINT_VCENTER, flechasCor);

			printSetArea(x+(w/2),y, w/2, h);
			printDraw(flechasImg, 0, FORM_SEPARATOR, 3, PRINT_HCENTER|PRINT_VCENTER, flechasCor);
		break;

		case 2:

			printSetArea(x,y, w, h/2);
			printDraw(flechasImg, 0, 0, 1, PRINT_HCENTER|PRINT_BOTTOM, flechasCor);
	
			printSetArea(x,y, w/2, h/2);
			printDraw(flechasImg, 0, 0, 1, PRINT_HCENTER|PRINT_BOTTOM, flechasCor);
	
			printSetArea(x+(w/2),y, w/2, h/2);
			printDraw(flechasImg, 0, 0, 1, PRINT_HCENTER|PRINT_BOTTOM, flechasCor);
		break;
		}
	break;
//#endif


//#ifndef REM_SQUAD_TACTICS
	case MENU_SQUAD_TACTICS:
		
		switch (formListPos)
		{
		case 0:

			for (int i=0 ; i<formCampoEjes[userFormation][2].length ; )
			{
				spriteDraw(flechasImg, x + regla3(formCampoEjes[userFormation][2][i++], 100, w)-starWidth, y + regla3(formCampoEjes[userFormation][2][i++], 100, h)+(starWidth/2), 4, flechasCor);
			}
		break;

		case 1:

			printSetArea(x,y, w, h);
			printDraw(flechasImg, 0, 0, 0, PRINT_HCENTER|PRINT_VCENTER, flechasCor);

			printSetArea(x,y, w/2, h);
			printDraw(flechasImg, 0, 0, 0, PRINT_HCENTER|PRINT_VCENTER, flechasCor);

			printSetArea(x+(w/2),y, w/2, h);
			printDraw(flechasImg, 0, 0, 0, PRINT_HCENTER|PRINT_VCENTER, flechasCor);
		break;

		case 2:

			printSetArea(x,y, w, h);
			printDraw(flechasImg, 0, 0, 0, PRINT_HCENTER|PRINT_VCENTER, flechasCor);
		break;

		case 3:

			for (int i=0 ; i<formCampoEjes[userFormation][0].length ; )
			{
				spriteDraw(flechasImg, x + regla3(formCampoEjes[userFormation][0][i++], 100, w)-starWidth, y + regla3(formCampoEjes[userFormation][0][i++], 100, h)-(starWidth/2)-spriteHeight(flechasCor, 5), 5, flechasCor);
			}
			for (int i=0 ; i<formCampoEjes[userFormation][1].length ; )
			{
				spriteDraw(flechasImg, x + regla3(formCampoEjes[userFormation][1][i++], 100, w)-starWidth, y + regla3(formCampoEjes[userFormation][1][i++], 100, h)-(starWidth/2)-spriteHeight(flechasCor, 5), 5, flechasCor);
			}
		break;

		case 4:

			printSetArea(x,y, w/2, h);
			printDraw(flechasImg, 0, 0, 0, PRINT_HCENTER|PRINT_VCENTER, flechasCor);

			printSetArea(x+(w/2),y, w/2, h);
			printDraw(flechasImg, 0, 0, 0, PRINT_HCENTER|PRINT_VCENTER, flechasCor);
		break;
		}
	break;
//#endif

	case MENU_SQUAD_FORMATION:

		userFormation = formListPos;
	break;
	}

	




	int pos = 0;
	int starColor = 0;
	for (int ly=0 ; ly<formCampoEjes[userFormation].length ; ly++)
	{
		for (int lx=0 ; lx<formCampoEjes[userFormation][ly].length ; )
		{
			switch (menuType)
			{
		// Pintamos estrellas de colores, y jugador seleccionado
			case MENU_SQUAD_SELECT_TEAM:
			case MENU_SQUAD_CHANGE_NEEDED:
			case MENU_SQUAD_INJURED_SUBS_NEEDED:
			case MENU_SUBSTITUTE:
			case MENU_SQUAD_EDIT_NAMES:
			//#ifndef REM_FANTASY
			case MENU_FANTASY_CHOOSE_PLAYERS:
			//#endif
				starColor = 0;

				if (11 - pos > 0)
				{
					if (((formListDat[11-pos][0]>>8)&0xff) == FONT_ORANGE) {starColor = 3;}
				}
				if (11-pos == formListPos) {starColor = (starColor == 3? 2:1);}
				if (11-pos == formSwapSelected) {starColor = 2;}
			break;
	
			case MENU_TRAINING_LINE:
		
				starColor = (formListPos == ly? starColor = trainingStyle+1:0);
			break;
	
			case MENU_TRAINING_STYLE:
		
				starColor = formListPos + 1;
			break;
			}
	
			spriteDraw(formItemsImg, x + regla3(formCampoEjes[userFormation][ly][lx++], 100, w)-starWidth, y + regla3(formCampoEjes[userFormation][ly][lx++], 100, h)-starWidth, ITEM_STARS + starColor, formItemsCor);

			pos++;
		}
	}
}



// -------------------
// form Loading
// ===================

public void formLoading()
{
	formBodyMode = FORM_LOADING;
	formShow = true;
	forceRender();
}




// -------------------
// formList Clear
// ===================

int formListIni;
int formListPos;
String[][] formListStr;
int[][] formListDat;

public void formListClear()
{
	formListIni = 0;
	formListStr = null;
	formListDat = null;
}

// -------------------
// formList Add
// ===================

public void formListAdd(int dato, String texto)
{
	formListAdd(dato, new String[] {texto}, 0, 0);
}

public void formListAdd(int dato, String[] texto)
{
	for (int i=0 ; i<texto.length ; i++)
	{
		formListAdd(dato, texto[i]);
	}
}

public void formListAdd(int dato, String texto, int dat1)
{
	formListAdd(dato, new String[] {texto}, dat1, 0);
}

public void formListAdd(int dato, String[] texto, int dat1)
{
	formListAdd(dato, new String[] {texto[0]}, dat1, 0);
}

public void formListAdd(int dat0, String[] texto, int dat1, int dat2)
{
	int lines=(formListDat!=null)?formListDat.length:0;

	String str[][] = new String[lines+1][];
	int dat[][] = new int[lines+1][];

	int i=0;

	for (; i<lines; i++)
	{
		dat[i] = formListDat[i];
		str[i] = formListStr[i];
	}

	str[i] = texto;
	dat[i] = new int[] {dat0, dat1, dat2};

	formListStr = str;
	formListDat = dat;
}

// -------------------
// form List Opt
// ===================

public int formListOpt()
{
	return(formListDat[formListPos][2]);
}

// <=- <=- <=- <=- <=-































// *******************
// -------------------
// popup - Engine
// ===================
// *******************
// -------------------

// -----------------------------------------------
// Constantes para los tipos de popups...
// ===============================================
static final int POPUP_ASK = 0;
static final int POPUP_LANG = 1;
static final int POPUP_VOLUME = 2;
// ===============================================


// -----------------------------------------------
// Constantes para las acciones de los popups... (se envia como parametro a popupAction(int))
// ===============================================
static final int POPUP_ACTION_NONE = 0;		// sin efecto
static final int POPUP_ACTION_EXIT = 1;		// Cerramos popup, salimos de biosWait y refrescamos pantalla.
// ===============================================


// -----------------------------------------------
// Constantes que definen el tama�o de las esquinas redondeadas de los popup
// ===============================================
//#ifdef S30
//#elifdef S40

//#elifdef S60
static final int POPUP_MARC_BORDER = 6;

//#elifdef S80
//#endif
// ===============================================


// -----------------------------------------------
// Constantes para ajustar el tama�o de la barra desplazadora (slider de scrolls)
// ===============================================
static final int POPUP_SLIDER_WIDTH = 6;	// Ancho para barras verticales
static final int POPUP_SLIDER_HEIGHT = 6;	// Alto para barras horizontales
// ===============================================


// -----------------------------------------------
// Definicion de los colores usados en popups (RGB)
// ===============================================
static final int POPUP_RGB_MARCO_BACKGROUND = 0x7a948b;	// Gris
static final int POPUP_RGB_MARCO_TITLE = 0xff9c39;		// Naranja
static final int POPUP_RGB_MARCO_BORDER = 0x8f6444;		// Marron
// ===============================================


// Variables para la gestion del texto del popup
int popupX;
int popupY;
int popupWidth;
int popupHeight;

/*
// Variables que nos informan del area que disponemos en el popup para nuestro uso customizado
int popupBodyX;
int popupBodyY;
int popupBodyWidth;
int popupBodyHeight;
*/

// Variables para la gestion del texto del popup (area y posicion dentro del popup)
int popupTextX;
int popupTextY;
int popupTextWidth;
int popupTextHeight;
String[] popupTextStr;


// Variables para la gestion de listados (menus/tablas/textos) dentro del popup
int popupListIni;
int popupListPos;
int popupListView;
String popupListStr[][];
int popupListDat[][];


// Variables para gestionar sliders
//boolean popupVslider;
//int popupVsliderY;
//int popupVsliderHeight;


// Variables para la gestion del popup: ASK
boolean popupResult;	// Resultado del popup (true / false)


// Variables internas para la gestion del popupEngine
int popupType;
boolean popupShow;
boolean popupRunning;
int popupActionCancel;
int popupActionAcept;
int popupLeftSoftkeyBack;
int popupRightSoftkeyBack;
boolean popupRefreshOnClose = true;


// -------------------
// popup Init Ask
// ===================

public void popupInitAsk(String[] str, int leftSoftkey, int rightSoftkey)
{
	popupClear();
	popupTextStr = str;
	popupInit(POPUP_ASK, (leftSoftkey!=SOFTKEY_NONE? POPUP_ACTION_EXIT:POPUP_ACTION_NONE), (rightSoftkey!=SOFTKEY_NONE? POPUP_ACTION_EXIT:POPUP_ACTION_NONE), leftSoftkey, rightSoftkey);
}


// -------------------
// popup Init Info
// ===================

public void popupInitInfo(String[] str)
{
	popupClear();
	popupTextStr = str;
	popupInit(POPUP_ASK, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_CONTINUE);
}


public void popupInitBack(String[] str)
{
	popupClear();
	popupTextStr = str;
	popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_NONE, SOFTKEY_BACK, SOFTKEY_NONE);
}


// -------------------
// popup Clear
// ===================

public void popupClear()
{
	popupTextStr = null;
}


// -------------------
// popup Create
// ===================

public void popupCreate()
{
//#ifdef S30
//#else
	int height = (canvasHeight / 8);
//    popupX = 0;
    popupY = height*2;
    popupWidth = canvasWidth;
    popupHeight = height*4;
//#endif
}

// -------------------
// popup Init
// ===================

public void popupInit(int type, int actionCancel, int actionAcept, int leftSoftkey, int rightSoftkey)
{
	popupType = type;

// Inicializapos la posision inicial
	popupListIni = 0;
	popupListIni = 0;
	popupListPos = 0;


// Guardamos acciones a ejecutar tras pulsar softkeys
	popupActionCancel = actionCancel;
	popupActionAcept = actionAcept;


// Actualizamos listener con texto para el popup haciendo una copia del contenido anterior
	popupLeftSoftkeyBack = listenerIdLeft;
	popupRightSoftkeyBack = listenerIdRight;
	listenerInit(leftSoftkey, rightSoftkey);


// Inicializamos variables basicas
//	popupVslider = false;
//	popupVsliderY = 0;

// popupBody
	popupTextX = POPUP_MARC_BORDER;
	popupTextY = POPUP_MARC_BORDER;
	popupTextWidth = popupWidth - POPUP_MARC_BORDER;
	popupTextHeight = popupHeight - POPUP_MARC_BORDER;


// popupType
	switch (popupType)
	{
	case POPUP_ASK:

		fontInit(FONT_WHITE);
	
		popupTextStr = printTextBreak(popupTextStr, popupTextWidth - POPUP_SLIDER_WIDTH);

		popupListView = popupTextHeight / printGetHeight();
	
		if (popupListView >= popupTextStr.length)
		{
			popupListView = popupTextStr.length;
		} else {
			popupListView = (popupTextHeight-(spriteHeight(formItemsCor, ITEM_ARROW_UP)*2)) / printGetHeight();
//			popupVslider = true;
			popupTextWidth -= POPUP_SLIDER_WIDTH;
		}
	
		int tmpHeight = popupListView * printGetHeight();
	
		popupTextY = popupTextY + ((popupTextHeight - tmpHeight) / 2);
	
		popupTextHeight = tmpHeight;
	break;


//#ifndef REM_MULTILANGUAGE
	case POPUP_LANG:
		fontInit(FONT_BIG_WHITE);
	
		popupListView = popupTextHeight / printGetHeight();
	
		if (popupListView >= popupTextStr.length)
		{
			popupListView = popupTextStr.length;
		} else {
			popupListView = popupTextHeight / printGetHeight();
//			popupVslider = true;
			popupTextWidth -= POPUP_SLIDER_WIDTH;

	// chapu, para que se vean todas las lineas sin necesidad de scroll
			int diferencia = (popupTextStr.length - popupListView);
			popupHeight += diferencia * printFontHeight;
			popupTextHeight += diferencia * printFontHeight;
			popupY -= (diferencia * printFontHeight)/2;
			popupListView = popupTextStr.length;
		}
	
		tmpHeight = popupListView * printGetHeight();
	
		popupTextY = popupTextY + ((popupTextHeight - tmpHeight) / 2);
	
		popupTextHeight = tmpHeight;
	break;
//#endif
	}


// popupVslider
//	popupVsliderHeight = popupHeight;
//	popupVsliderY = popupY;


	popupRunning = true;
	popupShow = true;

//	biosPush();
	biosStatusOld = biosStatus;
	biosStatus = BIOS_POPUP;
}


// -------------------
// popup Release
// ===================

public void popupRelease()
{
//#if DEBUG && DEBUG_MENU
	Debug.println("popupRelease("+popupType+")");
//#endif

	popupShow = false;
	popupRunning = false;

	listenerInit(popupLeftSoftkeyBack, popupRightSoftkeyBack);
	gameForceRefresh = popupRefreshOnClose;

 	popupCreate();	// hack para optimizar bytecode

//	biosPop();
	biosStatus = biosStatusOld;
}


// -------------------
// popup Tick
// ===================

public boolean popupTick()
{
// popupType
	switch (popupType)
	{
//#ifndef REM_VOLUME
	case POPUP_VOLUME:

	// Controlamos desplazamiento horizontal
		if (keyX != 0 && lastKeyX == 0)
		{
			gameVolume += keyX;
			if (gameVolume < 0) {gameVolume = 2;} else if (gameVolume > 2) {gameVolume = 0;}

			popupTextStr[popupTextStr.length-1] = "< "+getMenuText(MENTXT_OPTI_VOLUME)[gameVolume]+" >";
			caratulaShow = true;
			popupShow = true;
		}
//#endif

	case POPUP_ASK:

	// Controlamos si pulsamos softkey Izquierda
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			popupResult = false;
			popupAction(popupActionCancel);
		}

	// Controlamos si pulsamos softkey Derecha
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			popupResult = true;
			popupAction(popupActionAcept);
		}


	// Controlamos si pulsamos PAD Arriba
		if (keyY < 0 && lastKeyY == 0 && popupListIni > 0)
		{
			popupListIni--;
			gameForceRefresh = true;
			popupShow = true;
		}

	// Controlamos si pulsamos PAD Abajo
		if (keyY > 0 && lastKeyY == 0 && popupListIni < popupTextStr.length - popupListView)
		{
			popupListIni++;
			gameForceRefresh = true;
			popupShow = true;
		}
	break;


//#ifndef REM_MULTILANGUAGE
	case POPUP_LANG:
	// Controlamos si pulsamos softkey Izquierda
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			popupResult = false;
			popupAction(popupActionCancel);
		}

	// Controlamos si pulsamos softkey Derecha
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			popupResult = true;
			popupAction(popupActionAcept);
		}


	// Controlamos si pulsamos PAD Arriba
		if (keyY < 0 && lastKeyY == 0 && popupListPos > 0)
		{
			popupListPos--;
			popupShow = true;

			gameText[TEXT_SOFTKEYS][1] = langText[popupListPos][1];
			listenerIdRight = 0;
			listenerInit(0, 1);
		}

	// Controlamos si pulsamos PAD Abajo
		if (keyY > 0 && lastKeyY == 0 && popupListPos < popupTextStr.length-1)
		{
			popupListPos++;
			popupShow = true;

			gameText[TEXT_SOFTKEYS][1] = langText[popupListPos][1];
			listenerIdRight = 0;
			listenerInit(0, 1);
		}
	break;
//#endif
	}

	return false;
}


// -------------------
// popup Draw
// ===================

public void popupDraw()
{
	if (!popupShow) {return;} else {popupShow = false;}


//#ifndef REM_MULTILANGUAGE
	if (popupType == POPUP_LANG)
	fillDraw(0x000000, 0, 0, canvasWidth, canvasHeight);
	else
//#endif
	alphaFillDraw(0x88000000, 0, 0, canvasWidth, canvasHeight);


	fillDraw(0x000000, popupX, popupY, popupWidth, popupHeight);
	fillDraw(SUBMENU_COLORS[option], popupX, popupY, popupWidth, 2);
	fillDraw(SUBMENU_COLORS[option], popupX, popupY+popupHeight, popupWidth, 2);



// popupType: Render del cuerpo del popup (body) que depende del tipo de popup
// popupType
	switch (popupType)
	{
	case POPUP_ASK:
	case POPUP_VOLUME:
		fontInit(FONT_WHITE);
		printSetArea(popupX + popupTextX, popupY + popupTextY, popupTextWidth, popupTextHeight);
	
		for (int i=0 ; i<popupListView ; i++)
		{
			printDraw(popupTextStr[popupListIni + i], 0, i*printGetHeight(), fontPenRGB, (popupListView<=4? PRINT_HCENTER : PRINT_LEFT));
		}


		if (popupListIni > 0)
		{
			printDraw(formItemsImg, 0, -spriteHeight(formItemsCor, ITEM_ARROW_UP), ITEM_ARROW_UP, PRINT_TOP|PRINT_HCENTER, formItemsCor);
		}
		if (popupListIni < popupTextStr.length - popupListView)
		{
			printDraw(formItemsImg, 0, spriteHeight(formItemsCor, ITEM_ARROW_DOWN), ITEM_ARROW_DOWN, PRINT_BOTTOM|PRINT_HCENTER, formItemsCor);
		}


	/*
		if (popupVslider)
		{
			sliderVDraw(popupListIni, popupTextStr.length - popupListView, (popupX + popupBodyX + popupTextWidth), popupY + popupBodyY, popupBodyHeight - 4);
		}
	*/
	break;


//#ifndef REM_MULTILANGUAGE
	case POPUP_LANG:

	// Controlamos si hay que hacer scroll vertical
		if (popupListIni > popupListPos) {popupListIni = popupListPos;}
		else
		if (popupListIni < popupListPos-popupListView+1) {popupListIni = popupListPos-popupListView+1;}

		printSetArea(popupX + popupTextX, popupY + popupTextY, popupTextWidth, popupTextHeight);

		for (int i=0 ; i<popupListView ; i++)
		{
			fontInit(popupListPos == popupListIni + i? FONT_BIG_YELLOW:FONT_BIG_WHITE);
			printDraw(popupTextStr[popupListIni + i], 0, i*printGetHeight(), fontPenRGB, (popupListView<=4? PRINT_HCENTER : PRINT_LEFT));
		}
	break;
//#endif
	}
}


// -------------------
// popup Action
// ===================

public void popupAction(int action)
{
	switch (action)
	{
	case POPUP_ACTION_EXIT:

		popupRelease();

		popupExit = true;		// Activamos salir del "biosWait()" actual
	break;
	}
}


// -------------------
// popup Wait
// ===================

boolean popupExit;

public void popupWait()
{
	popupExit = false;
	while (!popupExit)
	{
		biosLoop();
	}
	popupExit = false;
	popupResult = true; //MONGOHACK

// Gestion del teclado, es necesario para evitar problemas...
	lastKeyMenu = keyMenu;	// Keys del Frame Anterior
	lastKeyX    = keyX;
	lastKeyY    = keyY;
	lastKeyMisc = keyMisc;

	keyMenu = intKeyMenu;	// Keys del Frame Actual
	keyX    = intKeyX;
	keyY    = intKeyY;
	keyMisc = intKeyMisc;
}

// <=- <=- <=- <=- <=-






// HUGGY



boolean consoleRunning = false;
boolean consoleShow = false;

//#ifndef REM_TEXTMATCH

//*******************
//-------------------
//console - Engine
//===================
//*******************


static final int CONSOLE_SEPARATOR = 4;


int CONSOLE_SLIDER_WIDTH = 10;

int CONSOLE_OPTION_BAR_HEIGH;

static final int CONSOLE_TABLE_MAX_SEPARATOR = 10;
static final int CONSOLE_TABLE_MIN_SEPARATOR = 4;

//-----------------------------------------------
//Constantes de los tipos de consoleularios (consoleType)
//===============================================
static final int CONSOLE_CONSOLE = 5;		// Consola del match de texto
//===============================================

static final int CONSOLE_MAX_LINES = 10;


//Variables para la gestion de areas
	int consoleBodyMode;
	int consoleBodyX;
	int consoleBodyY;
	int consoleBodyWidth;
	int consoleBodyHeight;
	int consoleBoxX;
	int consoleBoxY;
	int consoleBoxWidth;
	int consoleBoxHeight;
	int[] consoleBoxesX;
	int[] consoleBoxesW;


//Variables internas
	int consoleListView;
	int consoleListCMD;
	int consoleListEnter;

	int consoleActionBack;
	int consoleActionAcept;
	int consoleActionCicle;

	byte[] consoleItemsCor;
	Image consoleItemsImg;

	int consoleType;

	int consoleBaseColor;


//-------------------
//console Clear
//===================

public void consoleClear()
{

//consoleList
	consoleListClear();

//console
	consoleBaseColor = 3;
	consoleActionCicle = 0;
}



//-------------------
//console Init
//===================

public void consoleInit(int mode, int pos, int actionBack, int actionAcept)
{
//Guardamos el tipo de Formulario a mostrar
	consoleBodyMode = mode;

//Guardamos posicion inicial (cursor)
	consoleListPos = pos;

//Guardamos la accion a ejecutar al pulsar la softkey izquierda y derecha
	consoleActionBack = actionBack;
	consoleActionAcept = actionAcept;



//Fijamos area disponible para los elementos
	consoleBodyX = 2;
	consoleBodyY = 50;
//#ifdef FEA_CONSOLE_SMALL_SCREEN
	consoleBodyWidth = canvasWidth;
//#else
	consoleBodyWidth = canvasWidth-4;
//#endif
	consoleBodyHeight = canvasHeight-60;




	int cnt;
	int[][] tmpDat;
	String[][] tmpStr;


//Representamos tipo de consoleulario
	
		
	fontInit(FONT_WHITE);

	consoleListEnter = printGetHeight();	// Salto de linea una linea y la siguiente.
	
	
	consoleBoxWidth = consoleBodyWidth - (CONSOLE_SEPARATOR);
	consoleBoxX = consoleBodyX + (CONSOLE_SEPARATOR / 2);

// Cortamos el texto si se sale de la pantalla
	cnt = 0;
	for (int i=0 ; i<consoleListStr.length ; i++)
	{
		consoleListStr[i] = printTextBreak(consoleListStr[i][0], consoleBoxWidth - CONSOLE_SEPARATOR - 4);
		cnt += consoleListStr[i].length;		
	}
	tmpDat = new int[cnt][];
	tmpStr = new String[cnt][1];
	cnt=0;
	for (int i=0 ; i<consoleListStr.length ; i++)
	{
		for (int t=0 ; t<consoleListStr[i].length ; t++)
		{
			tmpDat[cnt] = consoleListDat[i];
			tmpStr[cnt++][0] = consoleListStr[i][t];
		}
	}
	consoleListDat = tmpDat;
	consoleListStr = tmpStr;

// Calculamos cuantas lineas de texto nos entran en pantalla
	consoleListView = consoleBodyHeight / consoleListEnter;	// lineas visibles en lo que queda de pantalla
	
// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
	if (consoleListView >= consoleListStr.length)
	{
		consoleListView = consoleListStr.length;
// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
	} else {
//		consoleVslider = true;
	}

	//consoleBoxHeight = (consoleListView * consoleListEnter);
	consoleBoxY = consoleBodyY + (consoleBodyHeight - consoleBoxHeight) / 2;
					
	consoleRunning = true;
	consoleShow = true;
}



//-------------------
//console Release
//===================

public void consoleRelease()
{
	consoleRunning = false;
	consoleShow = false;
}


//-------------------
//console Tick
//===================

public boolean consoleTick()
{
			
	if(textMatchTick()) {
			
		menuAction(MENU_ACTION_TEXTMATCHEND);
		return true;
		
	}
	
	if(!playerSquadNoInjured()) {
		
//		 Reseteamos cache de menus para que cuando desbordemos la cache, regresemos al menuBar... (menuType = -1) ;-)
		menuStackIndex = 1;
		menuStack[0][0] = -1;

		menuInit(MENU_SUBSTITUTE);
		return true;
	}
					
	if (keyMenu > 0 && lastKeyMenu == 0)
	{				
		//menuAction(MENU_ACTION_TEXTMATCHEND);
		menuInit(MENU_TEXTMATCH);
		return true;
	}		
	
	if(keyX > 0 && lastKeyX == 0 && textMatchSpeed < 10) {
		
		textMatchSpeed += 4;
	}
	
	if(keyX < 0 && lastKeyX == 0 && textMatchSpeed > 2) {
		
		textMatchSpeed -= 4;
	}
	
	listenerInit(SOFTKEY_NONE, SOFTKEY_MENU);

	consoleShow = true;

	return false;
}


//-------------------
//console List String Width :: Calculamos el ancho maximo de la "columna" indicada.
//===================

public int consoleListStringWidth(int index)
{
	int width = 0;
	for (int i=0 ; i<consoleListStr.length ; i++)
	{
	// Solo procesamos las lineas con RENDER especifico para CONSOLE-TABLE
		if ((consoleListDat[i][0] & LINE_NO_CALC) == 0)
		{
			try {
				int size = printStringWidth(consoleListStr[i][index]);
				if (width < size) {width = size;}
			} catch(Exception e) {}
		}
	}

	return width;
}


//-------------------
//console Draw
//===================

public static final int CONSOLE_RGB_BACKGROUND = 	0x000000;
public static final int CONSOLE_RGB_DARK = 			0x28381a;
public static final int CONSOLE_RGB_LIGHT =			0x3e5b2b;
public static final int CONSOLE_RGB_BARS1 = 		0x979c96;
public static final int CONSOLE_RGB_BARS2 = 		0xd7d0ce;

//#ifdef FEA_CONSOLE_SMALL_SCREEN
//#else
public static final int CONSOLE_TEAMNAME_MAX_LENGTH = 10;
//#endif


public void consoleDrawBox(int rgb1, int rgb2) {
	
	fillDraw(rgb2, printX, printY, printWidth, printHeight);
	fillDraw(rgb1, printX + 1, printY + 1, printWidth - 2, printHeight - 2);
}

public void consoleDraw()
{
//Debemos refrescar el consoleulario o algun sub-componente?
	if (!consoleShow)
	{
		return;
	}
	consoleShow = false;
	
	titleBarShow = false;

//Solicitamos la impresion de la barra de titulos
//	titleBarShow = true;
	
		
//	 Pintamos pantalla de fondo de los menus
	if (backgroundImg != null)
	{
		imageDraw(backgroundImg);
	} else {
		fillDraw(0x003300, 0, 0, canvasWidth, canvasHeight);
	}
	
	// MATCH INFO
	
	fontInit(FONT_BIG_WHITE);
	
	int conY = 10;
	int clockWidth = printStringWidth("99 - 99");
	int namesWidth = (canvasWidth - clockWidth) / 2;
		
	printSetArea(namesWidth, conY, clockWidth, printGetHeight());
	consoleDrawBox(CONSOLE_RGB_DARK, CONSOLE_RGB_LIGHT);	
	printDraw(textMatchGoalsA + " - " + textMatchGoalsB, 0, 0, fontPenRGB, PRINT_VCENTER | PRINT_HCENTER);
	
	printSetArea(0, conY, namesWidth, printGetHeight());
	consoleDrawBox(CONSOLE_RGB_DARK, CONSOLE_RGB_LIGHT);
				
	printSetArea(namesWidth + clockWidth, conY, namesWidth, printGetHeight());
	consoleDrawBox(CONSOLE_RGB_DARK, CONSOLE_RGB_LIGHT);
		
//#ifndef REM_TEAMSHIELDS
	cellDraw(matchShieldsImg[0], namesWidth / 2 - (SHIELD_WIDTH/2), conY + printGetHeight()/2 - (SHIELD_HEIGHT/2),  league.userMatch[0].teamLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, wisth,height)
	cellDraw(matchShieldsImg[1], namesWidth + clockWidth + namesWidth / 2 - (SHIELD_WIDTH/2), conY + printGetHeight()/2 - (SHIELD_HEIGHT/2),  league.userMatch[1].teamLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, wisth,height)
//#endif
		
	conY += 8;//printGetHeight();
					
	conY += printGetHeight();
	
	fontInit(matchClock >= 45*60 && !textMatchHalfTime || matchClock >= 90*60 ? FONT_BIG_YELLOW : FONT_BIG_WHITE);
	
	printSetArea(namesWidth, conY, clockWidth, printGetHeight());
	consoleDrawBox(CONSOLE_RGB_DARK, CONSOLE_RGB_LIGHT);	
	printDraw(matchClockStr(matchClock), 0, 0, fontPenRGB, PRINT_VCENTER | PRINT_HCENTER);
	
	printSetArea(0, conY, namesWidth, printGetHeight());
	consoleDrawBox(CONSOLE_RGB_DARK, CONSOLE_RGB_LIGHT);
	fontInit(FONT_ORANGE);
	
	String name;
	name = league.userMatch[0].name;
	if(name.length() > CONSOLE_TEAMNAME_MAX_LENGTH) {
		
		name = name.substring(0,CONSOLE_TEAMNAME_MAX_LENGTH - 1) + ".";
	}
	
	printDraw(name, 0, 0, fontPenRGB, PRINT_VCENTER | PRINT_HCENTER | PRINT_MASK);
	
	fontInit(FONT_BIG_WHITE);
	printSetArea(namesWidth + clockWidth, conY, namesWidth, printGetHeight());
	consoleDrawBox(CONSOLE_RGB_DARK, CONSOLE_RGB_LIGHT);
	fontInit(FONT_ORANGE);
	
	name = league.userMatch[1].name;
	if(name.length() > CONSOLE_TEAMNAME_MAX_LENGTH) {
		
		name = name.substring(0,CONSOLE_TEAMNAME_MAX_LENGTH - 1) + ".";
	}
	printDraw(name, 0, 0, fontPenRGB, PRINT_VCENTER | PRINT_HCENTER | PRINT_MASK);
	
	fontInit(FONT_BIG_WHITE);
	
	conY += printGetHeight();
	
	fontInit(FONT_ORANGE);
	
	// CONSOLE
	
	conY += 2;
	
//#ifndef REM_CONSOLE_STATS	
	
	int consoleHeight = canvasHeight - conY - printGetHeight() * 5;
	
//#else
//#endif
				
	printSetArea((canvasWidth - consoleBoxWidth + CONSOLE_SEPARATOR) /2, conY, consoleBoxWidth - CONSOLE_SEPARATOR, consoleHeight);
	
	consoleDrawBox(CONSOLE_RGB_BACKGROUND, CONSOLE_RGB_DARK);
	
	printSetArea((canvasWidth - consoleBoxWidth + CONSOLE_SEPARATOR + 4)/2, conY + 2, consoleBoxWidth - CONSOLE_SEPARATOR - 4, consoleHeight - 4);

	fontInit(FONT_WHITE);

	
	//rectDraw(-1, printX, printY, printWidth, printHeight);
	
	for (int i=0 ; i<consoleListView ; i++)
	{
		// Pintamos texto
		printDraw(consoleListStr[consoleListStr.length - 1 - i][0], 0,  -i*consoleListEnter, fontPenRGB, PRINT_LEFT|PRINT_BOTTOM|PRINT_MASK);
	}
			
	conY += consoleHeight;
	
	// Stats area
	
	printSetArea(0,0,canvasWidth,canvasHeight);
	
//#ifndef REM_CONSOLE_STATS		
	
	fontInit(FONT_ORANGE);
	printDraw(league.journeyType == league.LEAGUEJOURNEY ? getMenuText(MENTXT_JOURNEYTYPES)[0]+": "+getMenuText(MENTXT_WEEK)[0]+" "+(league.currentWeek+1) : getMenuText(MENTXT_JOURNEYTYPES)[1], 0, conY, fontPenRGB, PRINT_HCENTER);
	
	conY += printGetHeight();
	fontInit(FONT_BLACK);
	
	//conY += printGetHeight();
	consoleStatsBar(conY,textMatchShotCount[0],textMatchShotCount[1]);
	printDraw(getMenuText(MENTXT_TEXTMATCH_STATS)[0], 0, conY, fontPenRGB, PRINT_HCENTER);
	conY += printGetHeight();
	
	
	//conY += printGetHeight();
	consoleStatsBar(conY,textMatchFoulCount[0],textMatchFoulCount[1]);
	printDraw(getMenuText(MENTXT_TEXTMATCH_STATS)[1], 0, conY, fontPenRGB, PRINT_HCENTER);
	conY += printGetHeight();
	
	
	//conY += printGetHeight();
	
	if(matchClock < 4*60) {
	
		consoleStatsBar(conY,50,50);
		
	} else {
		
		int totalPossession = textMatchPossession[0] + textMatchPossession[1];	
		if(totalPossession == 0) {
			
			totalPossession = 1;
		}
		int v1 = (100 * textMatchPossession[0]) / totalPossession;
		int v2 = (100 * textMatchPossession[1]) / totalPossession;
		
		if(v1 + v2 <= 100) {
			
			v1 = 100 - v2;
		}
		
		consoleStatsBar(conY,v1,v2);
	}
	printDraw(getMenuText(MENTXT_TEXTMATCH_STATS)[2], 0, conY, fontPenRGB, PRINT_HCENTER);
	conY += printGetHeight();
				
//#endif
	
	fillDraw(CONSOLE_RGB_BACKGROUND, 0, conY, canvasWidth, printGetHeight());
	fontInit(FONT_ORANGE);
	
	String str;
	
	if(textMatchSpeed == 2) {
	
		str = getMenuText(MENTXT_GFXMATCHSPEED)[0];
		
	} else if(textMatchSpeed == 6) {
		
		str = getMenuText(MENTXT_GFXMATCHSPEED)[1];
		
	} else {
		
		str = getMenuText(MENTXT_GFXMATCHSPEED)[2];
	}
	
	printDraw(str, 0, conY, fontPenRGB, PRINT_HCENTER);
	
	
}

public void consoleStatsBar(int y, int v1, int v2) {
	
	int total = canvasWidth - 40;
	int half = v1 == v2 ? half = total /2 : (total * v1) / (v1 + v2);
	
	
	fontInit(FONT_WHITE);
	printDraw(""+v1, 5, y, fontPenRGB, 0);
	printDraw(""+v2, canvasWidth - 15, y, fontPenRGB, 0);
	
	fillDraw(CONSOLE_RGB_BARS1, 20, y + 2, half, printGetHeight() - 4);
	fillDraw(CONSOLE_RGB_BARS2, 20 + half, y + 2, total - half, printGetHeight() - 4);
	
	fontInit(FONT_BLACK);
}


//-------------------
//consoleList Clear
//===================

int consoleListIni;
int consoleListPos;
String[][] consoleListStr;
int[][] consoleListDat;

public void consoleListClear()
{
	consoleListIni = 0;
	consoleListStr = null;
	consoleListDat = null;
}

//-------------------
//consoleList Add
//===================

/*
public void consoleListAdd(int dato, String texto)
{
	consoleListAdd(dato, new String[] {texto}, 0, 0);
}

public void consoleListAdd(int dato, String[] texto)
{
	for (int i=0 ; i<texto.length ; i++)
	{
		consoleListAdd(dato, texto[i]);
	}
}

public void consoleListAdd(int dato, String texto, int dat1)
{
	consoleListAdd(dato, new String[] {texto}, dat1, 0);
}

public void consoleListAdd(int dato, String[] texto, int dat1)
{
	consoleListAdd(dato, new String[] {texto[0]}, dat1, 0);
}*/

public void consoleListAdd(int dat0, String[] texto, int dat1, int dat2)
{
	int lines=(consoleListDat!=null)?consoleListDat.length:0;

	
	String str[][] = new String[lines+1][];
	int dat[][] = new int[lines+1][];
	
	int i=0;
	
	for (; i<lines; i++)
	{
		dat[i] = consoleListDat[i];
		str[i] = consoleListStr[i];
	}
	
	str[i] = texto;
	dat[i] = new int[] {dat0, dat1, dat2};
	
	consoleListStr = str;
	consoleListDat = dat;
	
	lines++;
		
	if(lines > CONSOLE_MAX_LINES) {
		
		str = new String[CONSOLE_MAX_LINES][];
		dat = new int[CONSOLE_MAX_LINES][];
			
		for(i = 0; i < CONSOLE_MAX_LINES; i++) {
			
			str[i] = consoleListStr[i + lines - CONSOLE_MAX_LINES];
			dat[i] = consoleListDat[i + lines - CONSOLE_MAX_LINES];
		}
			
		consoleListStr = str;
		consoleListDat = dat;
	}
	
}

//-------------------
//console List Opt
//===================

public int consoleListOpt()
{
	return(consoleListDat[consoleListPos][2]);
}

public void consoleDestroy()
{
	consoleListClear();
	consoleRunning = false;
	consoleShow = false;
}


//<=- <=- <=- <=- <=-

//#else
//#endif




















public String matchClockStr(int clock) {

	String mins = ""+(clock / 60);
	String secs = ""+(clock % 60);
	
	return (mins.length() < 2 ? "0"+mins : mins) + ":" + (secs.length() < 2 ? "0"+secs : secs);
}

public String substringReplace(String s, String s1, String s2) {
	
	int i;
	
	do {
	
		i = s.indexOf(s1);
	
		if(i >= 0) {
		
			s = s.substring(0,i) + s2 + (i + s1.length() < s.length() ? s.substring(i + s1.length(), s.length()) : "");
			
		}/* else {
		
			return s;
		}
		*/
	} while (i >= 0);
	
	return s;
}

public boolean playerSquadNoInjured() {
	
	for(int i = 0; i < 11; i++) {
		
		int playerIdx = league.extendedPlayer(league.userTeam.playerIds[i]);
		
		if(league.userPlayerStats[playerIdx][league.INJURY_JOURNEYS] > 0) {
		
			return false;
		}
		
	}

	return true;
}

public boolean playerSquadOk() {
	
	for(int i = 0; i < 11; i++) {
		
		int playerIdx = league.extendedPlayer(league.userTeam.playerIds[i]);
		
		if(league.userPlayerStats[playerIdx][league.SANCTION_JOURNEYS] > 0 || league.userPlayerStats[playerIdx][league.INJURY_JOURNEYS] > 0) {
		
			return false;
		}
		
	}

	return true;
}

public int userTeamAvailablePlayers() {
	
	int sum = 0;
	
	for(int i = 0; i < league.userTeam.playerCount; i++) {
		
		int playerIdx = league.extendedPlayer(league.userTeam.playerIds[i]);
		
		if(league.userPlayerStats[playerIdx][league.SANCTION_JOURNEYS] == 0 && league.userPlayerStats[playerIdx][league.INJURY_JOURNEYS] == 0) {
		
			sum++;
		}
		
	}

	return sum;
}

// Text Match

boolean textMatchDisableLog = false;

//#ifndef REM_TEXTMATCH

String textMatchComments[][];
int matchClock;
int textMatchSpeed = 2;
int textMatchPlayerAtt;
int textMatchPlayerDef;
int textMatchPlayerCoop;
int textMatchGoalsA;
int textMatchGoalsB;
int textMatchBallOwner;
int textMatchStatus;
int textMatchBallX;
int textMatchBallY;
int nextEventClock;
int textMatchLastMsgVariant;
int textMatchShotCount[] = new int[2];
int textMatchFoulCount[] = new int[2];
int textMatchPossession[] = new int[2];
boolean textMatchHalfTime;
short textMatchYellowCards[];
short textMatchRedCards[];
int textMatchInterEventDelay;

int formationPositions[][][] = {

		{{0},{1,2,3},{4,5,6,7},{8,9,10}}, //3-4-3
		{{0},{1,2,3,4,5},{6,7,8},{9,10}}, //5-3-2
		{{0},{1,2,3,4},{5,6,7,8},{9,10}}, //4-4-2
		{{0},{1,2,3,4},{5,6,7},{8,9,10}}, //4-3-3
		{{0},{1,2,3},{4,5,6,7,8},{9,10}}, //3-5-2
		{{0},{1,2,3},{4,5,6,7},{8,9,10}}, //3-4-3
		{{0},{1,2,3,4},{5,6,7,8},{9,10}}, //4-4-2
};


public static final int TEXTMATCH_STATUS_KICKOFF = 0;
public static final int TEXTMATCH_STATUS_PLAY = 1;
public static final int TEXTMATCH_STATUS_REMATE = 2;
public static final int TEXTMATCH_STATUS_GOALSHOT = 3;
public static final int TEXTMATCH_STATUS_GOALSCORED = 4;
public static final int TEXTMATCH_STATUS_GOALKICK = 5;
public static final int TEXTMATCH_STATUS_FREEKICK = 6;
public static final int TEXTMATCH_STATUS_PENALTY = 7;
public static final int TEXTMATCH_STATUS_THROWIN = 8;
public static final int TEXTMATCH_STATUS_CORNERKICK = 9;
public static final int TEXTMATCH_STATUS_PREFOUL = 10;
public static final int TEXTMATCH_STATUS_GOALIERETURN = 11;
public static final int TEXTMATCH_STATUS_SECONDHALF_KICKOFF = 12;


public void textMatchLog(int id) {
	
	textMatchLastMsgVariant = (textMatchLastMsgVariant + 1) % textMatchComments[id].length; 
	textMatchLog(textMatchComments[id][textMatchLastMsgVariant]);
}

public void textMatchLog(String s) {

	if(textMatchDisableLog) {
	
		return;
	}
	
	s = substringReplace(s,"[teamAtt]", league.userMatch[textMatchBallOwner].name);
	s = substringReplace(s,"[teamDef]", league.userMatch[1 - textMatchBallOwner].name);
	s = substringReplace(s,"[playerAtt]", league.playerGetName(textMatchPlayerAtt));
	s = substringReplace(s,"[playerDef]", league.playerGetName(textMatchPlayerDef));
	s = substringReplace(s,"[playerCoop]", league.playerGetName(textMatchPlayerCoop));
	s = substringReplace(s,"[teamWin]", textMatchGoalsA > textMatchGoalsB ? league.userMatch[0].name : league.userMatch[1].name);
	s = substringReplace(s,"[teamLose]", textMatchGoalsA < textMatchGoalsB ? league.userMatch[0].name : league.userMatch[1].name);
	consoleListAdd(0, new String[] {matchClockStr(matchClock) + " - " + s}, 0, 0);
	consoleInit(CONSOLE_CONSOLE, consoleListStr.length - 1, 0, 0);
	
	textMatchInterEventDelay = (2 * s.length()) / textMatchSpeed;
}

public int textMatchPickPlayerId(int team, int category) {
	
	
	// Find out if in current line we have any available player to pass
	
	int availablePlayers = 0, playerId, playerIdx;
	boolean notSuspended;
	
	for(int i = 0; i < formationPositions[userFormation][category].length; i++) {
		
		playerId = league.userMatch[team].playerIds[formationPositions[userFormation][category][i]];
		
		playerIdx = league.extendedPlayer(playerId);
		
		notSuspended = true;
		
		if(playerIdx >= 0) {
			
			notSuspended = league.playerCanPlay(playerId);
		}
		
		if(playerId != textMatchPlayerCoop && notSuspended) {
		
			availablePlayers++;
		}
		
	}
	
	if(availablePlayers < 1) {
		
		if(category < 2) {
			
			category ++;
			
		} else {
		
			category --;
		}
	}
	
	
	do{
	
		notSuspended = true;
		
		playerId = league.userMatch[team].playerIds[formationPositions[userFormation][category][rnd(formationPositions[userFormation][category].length)]];		
									
		notSuspended = !existsValueInArray(textMatchRedCards, (short)playerId);
		
					 		
	} while(!notSuspended);
	
	return playerId;	
	
	/*switch(category) {
	
		// Goalkeeper
		case 0 :
			return league.userMatch[team].playerIds[0];
		
		// Defenders
		case 1 :
			return league.userMatch[team].playerIds[1 + rnd(3)];
			
		// Mid-fielders
		case 2 :
			return league.userMatch[team].playerIds[4 + rnd(4)];
			
		// Attackers
		case 3 :
			return league.userMatch[team].playerIds[8 + rnd(3)];		
	}
	
	return -1;*/
}

public int textMatchPickPlayerFromCurrentBallPos(int team, int offset) {
	
 	
	int id = -1;
	int area = textMatchBallY + (team == 0 ? offset : -offset);
	
	if(area > 2) {
		
		area = 2;	
	}
	
	if(area < -2) {
		
		area = -2;	
	}
	
	do {
		
		int relativePos = (team == 0 ? area : - area);
		
		switch(relativePos) {
		
			case -2 : id = textMatchPickPlayerId(team, 1); break;
			case -1 :
			case 0 	: id = textMatchPickPlayerId(team, 2); break;
			case 1 	: 
			case 2 	: id = textMatchPickPlayerId(team, 3); break;
		}
		
	} while (id == textMatchPlayerCoop);
	
	return id;
}

public void textMatchSpawnNewEvent(int minTime, int deviation) {
	
	nextEventClock = matchClock + minTime + rnd(deviation);
}

public void textMatchChangeOwner() {

	textMatchBallOwner = 1 - textMatchBallOwner;
	
	int aux = textMatchPlayerDef;
	textMatchPlayerDef = textMatchPlayerAtt;
	textMatchPlayerAtt = aux;
}

public void textMatchInit() {
	
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Antes crear textos textmatch");
//#endif

	textMatchComments = textosCreate(loadFile("/"+langStr+"Comments.txt"));
	
	
//#ifdef DEBUG
	/*for(int i = 0; i < textMatchComments.length; i++) {
		
		com.mygdx.mongojocs.lma2007.Debug.println("-----------Category "+i);
	
		for(int j = 0; j < textMatchComments[i].length; j++) {
		
			com.mygdx.mongojocs.lma2007.Debug.println(textMatchComments[i][j]);
		}
	}*/
//#endif
	league.recordOwnGoals = false;
	
	matchClock = 0;
	textMatchLog(TEXT_COMMENT_INITIAL);
	textMatchLog(TEXT_COMMENT_KICKOFF);	
	textMatchGoalsA = 0;
	textMatchGoalsB = 0;
	textMatchHalfTime = false;
	
	textMatchShotCount = new int[] {0, 0};
	textMatchFoulCount = new int[] {0, 0};
	textMatchPossession = new int[] {0, 0};
	textMatchYellowCards = new short[] {};
	textMatchRedCards = new short[] {};
	
	textMatchLastMsgVariant = 0;
	
	playingMatch = true;
	
	substitutionsLeft = 3;
	substitutedPlayers = new short[] {};
				
	textMatchKickOff(0);
	
	textMatchInterEventDelay = 0;
	
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Despues crear textos text match");
//#endif
	
	soundPlayEx(SOUND_APPLAUSE);
		
}

public void textMatchDestroy() {

	textMatchComments = null;
	
	textMatchShotCount = null;
	textMatchFoulCount = null;
	textMatchPossession = null;
	textMatchYellowCards = null;
	textMatchRedCards = null;
	
	System.gc();
}

public void textMatchKickOff(int owner) {
	
	textMatchStatus = TEXTMATCH_STATUS_KICKOFF;
	textMatchBallOwner = owner;	
	textMatchBallX = 0;
	textMatchBallY = 0;
	textMatchSpawnNewEvent(30,40);	
	textMatchPlayerAtt = textMatchPickPlayerId(textMatchBallOwner,3);
	textMatchPlayerDef = textMatchPickPlayerId(1 - textMatchBallOwner,3);
	
}

public boolean textMatchConfront(int attacker, int defender) {

		
	/*int att = league.userMatch[attacker].getQuality();
	int def = league.userMatch[defender].getQuality();
	
	return (rnd(1 * att + 3 * def) < 1 * att);*/
	
	int diffA = league.userMatch[0].matchGoals - textMatchGoalsA + 1;
	int diffB = league.userMatch[1].matchGoals - textMatchGoalsB + 1;
	
	//System.out.println("diffA:"+diffA+" diffB:"+diffB);
			
	if(attacker == 0) {
		
		return (rnd(diffB*6 + diffA) > diffB*6);
		
	} else {
		
		return (rnd(diffB + diffA*6) > diffA*6);
			
	}
		
}

public boolean textMatchTryShoot(int attacker, int defender) {

			
	int diffA = league.userMatch[0].matchGoals - textMatchGoalsA + 1;
	int diffB = league.userMatch[1].matchGoals - textMatchGoalsB + 1;
		
	if(attacker == 0) {
		
		return (rnd(diffA + 2*diffB) < diffA);
		
	} else {
		
		return (rnd(2*diffA + diffB) < diffB);
			
	}
		
}

public boolean textMatchTick() {
	
	String eventStr = null;

	menuListTitleA = null;
	menuListTitleB = null;
	
	
	if(textMatchInterEventDelay > 0) {
	
//#ifdef FEA_FAST_TEXTMATCH
//#else
		textMatchInterEventDelay--;
//#endif
		return false;
	}
	
//#ifdef FEA_FAST_TEXTMATCH
//#else
	matchClock += textMatchSpeed;
//#endif
	
	textMatchPossession[textMatchBallOwner] += textMatchSpeed;
	
	
	// Rare events
	
	if(rnd(5400) == 0) {
		
		textMatchLog(TEXT_COMMENT_RARE);
		textMatchSpawnNewEvent(120,60);
		soundPlayEx(SOUND_UY);
	}
	
	if(rnd(54000) == 0) {
		
		textMatchLog(TEXT_COMMENT_VERYRARE);
		textMatchSpawnNewEvent(120,60);
		soundPlayEx(SOUND_UY);
	}
	
	
	if(matchClock >= nextEventClock) {
					
		// Saque inicial
		
		if(textMatchStatus == TEXTMATCH_STATUS_KICKOFF) {
			
			
			//textMatchLog(TEXT_COMMENT_KICKOFF);
			textMatchStatus = TEXTMATCH_STATUS_PLAY;
			
			textMatchSpawnNewEvent(30,20);
			soundPlayEx(SOUND_WHISTLE);
		}
		
		// Saque inicial segunda parte
		/*
		if(textMatchStatus == TEXTMATCH_STATUS_SECONDHALF_KICKOFF) {
			
			matchClock = 45*60;
			textMatchLog(TEXT_COMMENT_SECONDHALF_KICKOFF);
			textMatchStatus = TEXTMATCH_STATUS_PLAY;
			
			textMatchSpawnNewEvent(30,20);
			soundPlayEx(SOUND_WHISTLE);
		}
		*/	
		
		// Saque de puerta
		
		else if(textMatchStatus == TEXTMATCH_STATUS_GOALKICK) {
									
			textMatchLog(TEXT_COMMENT_GOALKICK);
			textMatchStatus = TEXTMATCH_STATUS_PLAY;
			textMatchPlayerAtt = textMatchPickPlayerId(textMatchBallOwner,2);
			textMatchPlayerDef = textMatchPickPlayerId(1- textMatchBallOwner,2);
				
			if(textMatchBallOwner == 0) {
				
				textMatchBallY = -1;
				
			} else { 
				
				textMatchBallY = 1;
			}
									
			textMatchSpawnNewEvent(30,20);
		}
		
		// Pase del portero adelante
		
		else if(textMatchStatus == TEXTMATCH_STATUS_GOALIERETURN) {
									
			textMatchPlayerCoop = textMatchPlayerAtt;
			textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, +1);
			textMatchStatus = TEXTMATCH_STATUS_PLAY;	
			textMatchLog(TEXT_COMMENT_FWDPASS);
		
			textMatchSpawnNewEvent(30,40);			
		}
		
		// Saque de falta
		
		else if(textMatchStatus == TEXTMATCH_STATUS_FREEKICK) {
										
			textMatchLog(TEXT_COMMENT_FREEKICK);
			textMatchStatus = TEXTMATCH_STATUS_PLAY;
			
			textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, 0);
													
			textMatchSpawnNewEvent(30,20);
			soundPlayEx(SOUND_WHISTLE);
		}
		
		// Saque de banda
		
		else if(textMatchStatus == TEXTMATCH_STATUS_THROWIN) {
								
			textMatchLog(TEXT_COMMENT_THROWIN);
			textMatchStatus = TEXTMATCH_STATUS_PLAY;
			
			textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, 0);
						
			textMatchSpawnNewEvent(30,20);
			soundPlayEx(SOUND_WHISTLE);
		}
		
		// Saque de corner
		
		else if(textMatchStatus == TEXTMATCH_STATUS_CORNERKICK) {
								
			textMatchLog(TEXT_COMMENT_CORNERKICK);
			textMatchStatus = TEXTMATCH_STATUS_REMATE;
			
			textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, 0);
						
			textMatchSpawnNewEvent(30,20);
			soundPlayEx(SOUND_WHISTLE);
		}
		
		// oportunidad de remate
		
		else if(textMatchStatus == TEXTMATCH_STATUS_REMATE) {
			
			switch(rnd(4)) {
				
				case 0 : 
					textMatchLog(TEXT_COMMENT_REMATE);
					textMatchStatus = TEXTMATCH_STATUS_GOALSHOT;								
					textMatchSpawnNewEvent(5,20);
				break;
				
				case 1 : 
					textMatchLog(TEXT_COMMENT_DOCORNER);
					textMatchStatus = TEXTMATCH_STATUS_CORNERKICK;								
					textMatchSpawnNewEvent(60,30);
				break;
				
				case 2 : 
					textMatchLog(TEXT_COMMENT_GOALIECATCH);
					textMatchChangeOwner();
					textMatchStatus = TEXTMATCH_STATUS_GOALKICK;
					textMatchPlayerAtt = textMatchPickPlayerId(textMatchBallOwner,0);													
					textMatchSpawnNewEvent(60,30);
				break;
				
				case 3 : 
					textMatchLog(TEXT_COMMENT_DEFFTAKE);
					textMatchChangeOwner();
					textMatchStatus = TEXTMATCH_STATUS_PLAY;								
					textMatchSpawnNewEvent(60,30);
				break;
								
			}
									
		}
		
		// Tiro a puerta !!
		
		else if(textMatchStatus == TEXTMATCH_STATUS_GOALSHOT) {
			
			textMatchShotCount[textMatchBallOwner]++;
			
			if(textMatchTryShoot(textMatchBallOwner,1 - textMatchBallOwner)) {
				
				//  GOL!!!!!
				
				if(textMatchBallOwner == 0) {
					
					textMatchGoalsA++;
																			
				} else {
					
					textMatchGoalsB++;				
				}
				
//#ifndef REM_TOP_SCORERS				
				//if(league.journeyType == league.LEAGUEJOURNEY) {
					
					recordMatchGoal(textMatchPlayerAtt, textMatchBallOwner);
					//league.recordGoal(textMatchPlayerAtt);
				//}
//#endif				
								
				textMatchLog(TEXT_COMMENT_GOALSCORED);
				textMatchStatus = TEXTMATCH_STATUS_GOALSCORED;				
				textMatchSpawnNewEvent(60,50);
				
				soundPlayEx(league.userMatch[textMatchBallOwner] == league.userTeam ? SOUND_GOALSCORED : SOUND_GOALRECEIVED);
				
			} else {
				
				switch(rnd(4)) {
				
					case 0 :
						textMatchLog(TEXT_COMMENT_GOALIESAVED);
						textMatchStatus = TEXTMATCH_STATUS_PLAY;
						textMatchSpawnNewEvent(30,50);
					break;
					
					case 1 :
						textMatchLog(TEXT_COMMENT_GOALPOST);
						textMatchStatus = TEXTMATCH_STATUS_PLAY;
						textMatchSpawnNewEvent(30,50);
					break;
					
					case 2 :
						textMatchLog(TEXT_COMMENT_GOALIECATCH);
						textMatchChangeOwner();
						textMatchStatus = TEXTMATCH_STATUS_GOALKICK;
						textMatchPlayerAtt = textMatchPickPlayerId(textMatchBallOwner,0);
						textMatchSpawnNewEvent(50,50);
					break;
					
					case 3 :
						textMatchLog(TEXT_COMMENT_GOALMISS);
						textMatchChangeOwner();
						textMatchStatus = TEXTMATCH_STATUS_GOALKICK;						
						textMatchPlayerAtt = textMatchPickPlayerId(textMatchBallOwner,0);
						textMatchSpawnNewEvent(50,50);
					break;
				
				}	
				
				soundPlayEx(SOUND_UY);
			}									
		}
		
		// Gol marcado
		
		else if(textMatchStatus == TEXTMATCH_STATUS_GOALSCORED) {
			
			textMatchLog(TEXT_COMMENT_GOALCOMMENT);	
			
			if(Math.abs(textMatchGoalsA - textMatchGoalsB) > 1) {
				
				textMatchLog(rnd(2) == 0 ? TEXT_COMMENT_GOODRESULT : TEXT_COMMENT_BADRESULT);
			}
			
			textMatchKickOff(1 - textMatchBallOwner);			
		
		}
		
		// Tiro de penalty
		
		else if(textMatchStatus == TEXTMATCH_STATUS_PENALTY) {
									
			textMatchLog(TEXT_COMMENT_PENALTYKICK);
			textMatchStatus = TEXTMATCH_STATUS_GOALSHOT;				
			textMatchPlayerDef = textMatchPickPlayerId(1 - textMatchBallOwner,0);				
												
			textMatchSpawnNewEvent(10,20);
			soundPlayEx(SOUND_WHISTLE);
		}
		
		// Entrada violenta
		
		else if(textMatchStatus == TEXTMATCH_STATUS_PREFOUL) {
			
			if(rnd(2) == 0) {
				
				// No hay falta
				
				textMatchLog(TEXT_COMMENT_NOFOUL);
				textMatchStatus = TEXTMATCH_STATUS_PLAY;
				textMatchSpawnNewEvent(30,40);
				soundPlayEx(SOUND_UY); 
				
			} else {
				
				// El arbitro indica falta
				
				if(existsValueInArray(textMatchYellowCards, (short)textMatchPlayerDef)) {
					
					// Expulsi�n!!!
					
					textMatchLog(TEXT_COMMENT_REDCARD);
					
					textMatchYellowCards = removeValueFromArray(textMatchYellowCards, (short)textMatchPlayerDef);
					textMatchRedCards = addValueToArray(textMatchRedCards, (short)textMatchPlayerDef);
					
					int playerIdx = league.extendedPlayer(textMatchPlayerDef);
					
					if(playerIdx >= 0) {
						
						//#ifndef NOPLAYERELIMINATION
						
						league.userPlayerStats[playerIdx][league.SANCTION_JOURNEYS] = 2;
						
						//#endif
					}
					soundPlayEx(SOUND_CARD); 
					
				} else {
															
					// Anota tarjeta amarilla
				
					textMatchLog(TEXT_COMMENT_YELLOWCARD);
					textMatchYellowCards = addValueToArray(textMatchYellowCards, (short)textMatchPlayerDef);
					soundPlayEx(SOUND_CARD); 
															
				}
				
				if(rnd(10) == 0) {
					
					// Lesi�n!!
					
					textMatchLog(TEXT_COMMENT_INJURED);
					
					league.injurePlayer(textMatchPlayerAtt);
										
					soundPlayEx(SOUND_INJURY);
					
					textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, 0);
				}	
				
				textMatchPlayerDef = textMatchPickPlayerFromCurrentBallPos(1 - textMatchBallOwner, 0);
																																								
				textMatchFoulCount[1 - textMatchBallOwner]++;
		
				if(textMatchBallOwner == 0 && textMatchBallY == 2 || 
				   textMatchBallOwner == 1 && textMatchBallY == -2) {
						
					if(rnd(2) == 0) {
								
						// Corner
								
						textMatchLog(TEXT_COMMENT_DOCORNER);
								
						textMatchStatus = TEXTMATCH_STATUS_CORNERKICK;
						textMatchSpawnNewEvent(60,40);
						
						soundPlayEx(SOUND_WHISTLE);
								
					} else {
								
						// Es penalty
																				
						textMatchLog(TEXT_COMMENT_DOPENALTY);
						textMatchLog(TEXT_COMMENT_REFEREEPENALTY);
																				
						textMatchStatus = TEXTMATCH_STATUS_PENALTY;
						textMatchSpawnNewEvent(90,40);
						
						soundPlayEx(SOUND_WHISTLE);
					}
							
				} else {
							
					// Tiro de falta normal
							
					textMatchStatus = TEXTMATCH_STATUS_FREEKICK;
																																												
					textMatchSpawnNewEvent(60,40);
					
					soundPlayEx(SOUND_WHISTLE);
				}
			}
		}
		
		// Bal�n en juego
			
		else if(textMatchStatus == TEXTMATCH_STATUS_PLAY) {
			
			// Fuera de banda
			
			if(rnd(40) == 0) {
				
				textMatchLog(TEXT_COMMENT_SIDEOUT);
				textMatchStatus = TEXTMATCH_STATUS_THROWIN;
				textMatchSpawnNewEvent(90,40);
				
				textMatchChangeOwner();				
							
			
			// El equipo no en posesi�n intenta recuperar...
			
			} else if(textMatchConfront(1 - textMatchBallOwner,textMatchBallOwner)) {
				
				if(rnd(6) == 0) {
					
					// El equipo no en posesi�n realiza una entrada	violenta			
										
					textMatchLog(TEXT_COMMENT_DIRTYSTEAL);
					textMatchSpawnNewEvent(30,40);
					textMatchStatus = TEXTMATCH_STATUS_PREFOUL;
					
					
				} else {
					
					// El equipo en posesi�n pierde el bal�n
										
					textMatchPlayerDef = textMatchPickPlayerFromCurrentBallPos(1 - textMatchBallOwner,0);
														
					textMatchChangeOwner();
																												
					textMatchLog(TEXT_COMMENT_SUCCESSTEAL);
									
					textMatchSpawnNewEvent(30,40);
				}
				
            // El equipo en posesi�n realiza su jugada con �xito
				
			} else {
				
				int rndValue = rnd(10);
				int choice = 1;
				
				switch(league.userMatch[textMatchBallOwner] == league.userTeam ? squadStyle : rnd(3)) {
				
					// Muy Defensivo
					case 0 :
						if(rndValue < 3) {
							
							choice = 1;
							
						} else if(rndValue < 7) {
							
							choice = 0;
						
						} else {
						
							choice = 2;
						}
					break;
						
					// Medio
					case 1 :
						if(rndValue < 1) {
							
							choice = 1;
							
						} else if(rndValue < 5) {
							
							choice = 0;
						
						} else {
						
							choice = 2;
						}
						
					break;
				
					// Muy ofensivo
					case 2 :						
						if(rndValue < 5) {
							
							choice = 0;
						
						} else {
						
							choice = 2;
						}						
					break;
				}
																																																
				if(choice == 0) {
						
					// Pasa el bal�n a un compa�ero m�s avanzado
					
					textMatchBallY += (textMatchBallOwner == 0 ? 1 : -1);
					
					if(textMatchBallY > 3) {
						
						textMatchBallY = 3;
					}
					
					if(textMatchBallY < -3) {
						
						textMatchBallY = -3;
					}
					
					int relativePos = (textMatchBallOwner == 0 ? textMatchBallY : -textMatchBallY);
															
					if(relativePos < 3) {
						
						// Pasa a un jugador m�s avanzado
						
						textMatchPlayerCoop = textMatchPlayerAtt;
						textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, +1);
						
						textMatchLog(TEXT_COMMENT_FWDPASS);
					
						textMatchSpawnNewEvent(30,40);
						
					} else {
						
						if(rnd(2) == 0) {
																				
							// Chuta a puerta
						
							textMatchLog(TEXT_COMMENT_GOALSHOOT);
							textMatchStatus = TEXTMATCH_STATUS_GOALSHOT;							
							textMatchSpawnNewEvent(5,10);
							
						} else {
							
							// Centra para que un compa�ero remate
							
							textMatchLog(TEXT_COMMENT_CENTERBALL);
							textMatchPlayerCoop = textMatchPlayerAtt;
							textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, 0);														
							textMatchStatus = TEXTMATCH_STATUS_REMATE;							
							textMatchSpawnNewEvent(15,10);
							
						}
					}
						
				} else if (choice == 1){
					
					// Pasa el bal�n a un compa�ero que est� detr�s
										
					textMatchBallY += (textMatchBallOwner == 0 ? -1 : 1);
					
					if(textMatchBallY > 3) {
						
						textMatchBallY = 3;
					}
					
					if(textMatchBallY < -3) {
						
						textMatchBallY = -3;
					}
					
					int relativePos = (textMatchBallOwner == 0 ? textMatchBallY : -textMatchBallY);
					
					if(relativePos > -3) {
						
						// Otro compa�ero que est� detr�s
						
						textMatchPlayerCoop = textMatchPlayerAtt;
						textMatchPlayerAtt = textMatchPickPlayerFromCurrentBallPos(textMatchBallOwner, -1);
						
						textMatchLog(TEXT_COMMENT_BWDPASS);
					
						textMatchSpawnNewEvent(30,40);
						
					} else {
						
						// Pasa a las manos del portero
						
						textMatchLog(TEXT_COMMENT_GOALIEPASS);
						textMatchPlayerAtt = textMatchPickPlayerId(textMatchBallOwner,0);						
						textMatchStatus = TEXTMATCH_STATUS_GOALIERETURN;							
						textMatchSpawnNewEvent(60,10);
					}
																														
				} else if (choice == 2){
					
					// Decide avanzar �l s�lo
					
					textMatchBallY += (textMatchBallOwner == 0 ? 1 : -1);
					
					if(textMatchBallY > 3) {
						
						textMatchBallY = 3;
					}
					
					if(textMatchBallY < -3) {
						
						textMatchBallY = -3;
					}
											
					textMatchLog(TEXT_COMMENT_ALONERUN);					
					textMatchSpawnNewEvent(15,10);
										
				}
			}									
		}
	}
			
	// Half time
	
	if(matchClock >= 46*60 && !textMatchHalfTime && textMatchStatus == TEXTMATCH_STATUS_PLAY) {
		
		textMatchLog(TEXT_COMMENT_HALFTIME);		
		textMatchHalfTime = true;		
	
		matchClock = 45*60;		
		textMatchKickOff(1);
		//textMatchStatus = TEXTMATCH_STATUS_SECONDHALF_KICKOFF;
		soundPlayEx(SOUND_3WHISTLE);
		textMatchLog(TEXT_COMMENT_SECONDHALF_KICKOFF);
		//textMatchStatus = TEXTMATCH_STATUS_PLAY;		
		textMatchSpawnNewEvent(30,20);
	
	}
	
	// Full time
	
	if(matchClock >= 90*60 && textMatchStatus == TEXTMATCH_STATUS_PLAY) {
		
		textMatchLog(TEXT_COMMENT_MATCHEND);
		return true;
	}
	
	return false;
	
}

//#endif



//*******************
//-------------------
//2d match - Engine
//gfxmatch - Engine
//===================
//*******************

boolean gfxMatchShow;

//#ifndef REM_2DMATCH
int gfxMatchSpeed;
static int nfc;  
static int timeCounter;
//int formation[][][];
final static int GROUNDWIDTH    = 300;
final static int GROUNDHEIGHT   = 440;
final static int HGROUNDWIDTH   = GROUNDWIDTH/2;
final static int HGROUNDHEIGHT  = GROUNDHEIGHT/2;
final static int PENALTYHEIGHT   = (GROUNDHEIGHT/8);

final static int AREAHEIGHT     = 90;
final static int AREAWIDTH      = 180;
final static int LITTLEAREAHEIGHT   = 35;
final static int LITTLEAREAWIDTH    = 90;
final static int GOALSIZE       = 50; // 62
final static int GOALHEIGHT     = 32;
final static int GOALDEPTH = 14;
final static int HGOALSIZE      = GOALSIZE/2;

final static int NUMGRASSTYPES      = 4;
//final static int SHORTMATCHTIME     = 2000;
final static int MEDIUMMATCHTIME    = 2000;
//final static int LONGMATCHTIME      = 8000;

final static int POINTERSPEED     = 4;
static int matchState;

int theTeam;
int wait;
soccerPlayer    theTeam2[];
static soccerPlayer    teamA[];
static soccerPlayer    teamB[];
static soccerPlayer    teams[][]; 
//soccerPlayer    dummy = new soccerPlayer(this,1,null);
soccerPlayer    gkA, gkB;
static soccerPlayer            ball;
static int             teamDown;    
static soccerPlayer    strikeTeam[], lastStrikeTeam[];

static Random gfxMatchRnd;
byte             goalsA, goalsB;
static int             pointer;
int pointerSx = POINTERSPEED;
final static int             bestN = 192;
final static int             bestS = 64;
static int             bestW;
static int             bestE;
final static int TEAMA = 0;
final static int TEAMB = 1;
final static int INFINITE = 10000;
static boolean         firstPass;
int             nformed = 0;
static          soccerPlayer    humanSoccerPlayer;
int             preMatchState;
static int             outSide;
static int             outSideV;
final static int GOALPOSTSIZE = 3;
static int             np = -1;
//static int            keyPresTime;
int             checkBallRestrics;
static boolean         freeKick;
static soccerPlayer    kickPlayer;
static int nPP = 0;
boolean         halfMatch, changeFormation;
int             newFormation;
//static int teamAt[] = new int[2];
static int teamCenterX[] = new int[2];
static int teamCenterY[] = new int[2];
//static int teamHeight[] = new int[2];
static int teamAtt[] = new int[2];
static int specialId[][] = {{0,0,0,0},{0,0,0,0}};
static int ejected[] = new int[2];
static int eject[] = {-1, -1};
static int displKickOut;
//#ifndef NOSKIP
boolean skip;
//#endif
int totalMatchTime;
int globalMatchTime;
int extraMatchTime;
boolean firstPart;
static int teamGoal;
//#ifdef SLOW_DEVICE
//#endif

//final static int side = (GROUNDWIDTH / 8);
//final static int line = (GROUNDHEIGHT / 10);
//int matchTime;


//GFX

final static int PLAYGROUNDBORDER = 30;
//final static int MIDCIRCLEDIAMETER = 73;
int camX, camY, cameraX, cameraY;



soccerPlayer sortedEntities[];


//static int freezeHumanPlayer = 0;
//int clockFrame;

//
int myFormation = 0;
//


int formationA;
int formationB;

boolean userTeamIsA;
/*
 * Inicialitza un partit
 * @param formationA. Formaci� de l'equip A
 * @param formationA. Formaci� de l'equip B
 */
public void gfxMatchInit() 
{
//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 0");			
//#endif
	rainOn = false;
	teamA = new soccerPlayer[11];
	teamB = new soccerPlayer[11];
	teams = new soccerPlayer[2][];
	teams[0] = teamA;
	teams[1] = teamB;

	gfxMatchEvent = -1; 
	lastgfxMatchEvent = -1;

	
	sortedEntities = new soccerPlayer[23];
//#ifndef REM_BONUS_CODES
	if (gc.bonusCodes[GameCanvas.BONUS_RAININMATCH] != 0)
		rainOn = rnd(100) < rainProb[league.userLeague];
//#endif
	league.recordOwnGoals = false;
	
	isHalfMatch = false;
	globalMatchTime = 0;
	matchInjuredCount = 0;
	matchSuspendedCount = 0;
	
	if (league.userMatch[0].globalIdx == league.userTeam.globalIdx)
	{ 
		userTeamIsA = true;
		//com.mygdx.mongojocs.lma2007.Debug.println("*** userTEAM ES A");
	}
	else
		userTeamIsA = false;//com.mygdx.mongojocs.lma2007.Debug.println("*** userTEAM ES B");

//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 1");			
//#endif
	
	if (userTeamIsA)
	{
		formationA = userFormation;
		formationB = rnd(6);
	}
	else
	{
		formationB = userFormation;
		formationA = rnd(6);
	}

//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 2");			
//#endif

	teamGoal  = -1;
    pointer = HGROUNDWIDTH;
    firstPass = true;
    goalsA = 0;
    goalsB = 0;
    //changesLeft = 3;
    matchState = constants.GAME;
    teamDown = TEAMA;   
    strikeTeam = teamA;    
    
    bestW = (160-24);
    bestE = (224+24);
    
    ball = new soccerPlayer(this, -1);//8,8, this);
    ball.activate(HGROUNDWIDTH, HGROUNDHEIGHT);
    
//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 3");			
//#endif
   
    //int side = (GROUNDWIDTH / 8);
    //int line = (GROUNDHEIGHT / 10);
        
    
    for(int i = 0;i < 11;i++) {
        
        sortedEntities[i] = teamA[i] = new soccerPlayer(this, i);                
        sortedEntities[i].ball = ball;
        sortedEntities[i+11] = teamB[i] = new soccerPlayer(this, i);
        sortedEntities[i+11].ball = ball;
                
    }
    
    gkA = teamA[10];
    gkB = teamB[10];
        
    
//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 4");			
//#endif
    
    gfxMatchAssignPlayerData();
    
    changeToFormation(true);
    
    gkA.activate(TEAMA);                       
    gkB.activate(TEAMB);
    
    sortedEntities[22] = ball;
    //#ifdef SLOW_DEVICE
    //#endif
    
    nfc = 0;
    timeCounter = 0;
    firstPart = true;
    humanSoccerPlayer = null;
    
//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 5");			
//#endif

    
    ejected[TEAMA] = ejected[TEAMB] = 0;
    recalculateSpecialIds(teamA, TEAMA);
    recalculateSpecialIds(teamB, TEAMB);
//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 5.1");			
//#endif
    
    freeKick = false;
       
    totalMatchTime = MEDIUMMATCHTIME;
        
    extraMatchTime = Math.abs(gfxMatchRnd.nextInt()%(totalMatchTime>>3));
    
//#ifdef DEBUG			
	Debug.println("gfxMatchInit() 6");			
//#endif
   
}
public void gfxMatchDestroy()
{
	sortedEntities = null;
	teams[0] = teams[1] = null;
	teamA = teamB = null;
	strikeTeam =  lastStrikeTeam = null;
	teams = null;
	theTeam2 = null;
	ball = null;
	gkA = gkB = null;
	humanSoccerPlayer = null;
	kickPlayer = null;
	System.gc();
}


public void halfMatch()
{
	soundPlayEx(SOUND_3WHISTLE);
	gfxMatchEvent = GFXMATCH_EVENT_HALFMATCH;
	if (userTeamIsA)
	{
		formationA = userFormation;
		formationB = league.rand()%6;
	}
	else
	{
		formationB = userFormation;
		formationA = league.rand()%6;
	}
	
	teamGoal  = -1;
    firstPass = true;
    humanSoccerPlayer = null;
    ball.activate(HGROUNDWIDTH, HGROUNDHEIGHT);
    strikeTeam = teamB;
    teamDown = TEAMB;   
    //int side = (GROUNDWIDTH / 8);
    //int line = (GROUNDHEIGHT / 10);
    /*
    for(int i = 0;i < 10;i++)    
    {
        teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA, true);         
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB, true);         
    }*/
    changeToFormation(true);
    
    gkA.activate(TEAMA);        
    gkB.activate(TEAMB);
    gkA.state = soccerPlayer.FREE;
    gkB.state = soccerPlayer.FREE;
    
    nfc = 0;
    timeCounter = 0;
    matchState = constants.GAME;
    pointer = HGROUNDWIDTH;
    freeKick = false;
    isHalfMatch = true;
}  

boolean isHalfMatch;

//#ifdef FEA_EA
//String EAnamesTeamA[] = {"Toldo","R. Carlos","Cannavaro","Puyol","Beckham","Zidane","Gerrard","Kak�","Ronaldo","Ronaldinho","Eto'o"};
//String EAnamesTeamB[] = {"J.Gomez","Pedro","E.Lozano","J.Palomer","A.Llado","Rai","Dami�","Itthi","D.Rodr�guez","M.Sort","C.Peris"};

//#endif
public void gfxMatchAssignPlayerData() 
{
    
    int plId;   
    String[] namesTeamA = new String[11];
    String[] namesTeamB = new String[11];
    int[][] skillsTeamA = new int[11][7];    
    int[][] skillsTeamB = new int[11][7];    
    
    for(int i = 0; i < 11; i++) {
                
    	//#ifndef FEA_EA
    	namesTeamA[i] = league.playerGetName(league.userMatch[0].playerIds[i]);
    	namesTeamB[i] = league.playerGetName(league.userMatch[1].playerIds[i]);
    	//#else
    	//namesTeamA[i] = EAnamesTeamA[i];
    	//namesTeamB[i] = EAnamesTeamB[i];
    	//#endif
    	
        for(int j = 0; j < 4; j++) {     
        
            skillsTeamA[i][j] = league.playerGetGameSkill(league.userMatch[0].playerIds[i], j);//8;//AplayerData[plId][j];            
            skillsTeamB[i][j] = league.playerGetGameSkill(league.userMatch[1].playerIds[i], j);//8;//BplayerData[i][j];
        }
    
        skillsTeamA[i][4] = league.userMatch[0].playerIds[i];
        skillsTeamB[i][4] = league.userMatch[1].playerIds[i];                                                     
   }      
       
    
   for(int i = 0;i < 10;i++) {    
      //teamA[i].setPlayerSkills(namesTeamA[i+1], skillsTeamA[i+1], league.userMatch[0].isUserTeam()?league.playerCanPlay(i):false);      
      //teamB[i].setPlayerSkills(namesTeamB[i+1], skillsTeamB[i+1], league.userMatch[1].isUserTeam()?league.playerCanPlay(i):false);
	  teamA[i].setPlayerSkills(namesTeamA[i+1], skillsTeamA[i+1]);
	  teamB[i].setPlayerSkills(namesTeamB[i+1], skillsTeamB[i+1]);     
   }
      
   gkA.setPlayerSkills(namesTeamA[0], skillsTeamA[0]);   
   gkB.setPlayerSkills(namesTeamB[0], skillsTeamB[0]);
   
}


public void recalculateSpecialIds(soccerPlayer[] t, int idt) {
    
    int sId;
    
    sId = 8;
    while (t[sId].state == soccerPlayer.EJECTED) sId = (sId+1)%10;
    specialId[idt][0] = sId;
    
    sId = 9;
    while (t[sId].state == soccerPlayer.EJECTED || sId == specialId[idt][0]) sId = (sId+1)%10;
    specialId[idt][1] = sId;
 
    sId = 5;
    while (t[sId].state == soccerPlayer.EJECTED) sId = (sId+1)%10;
    specialId[idt][2] = sId;
 
    sId = 2;
    while (t[sId].state == soccerPlayer.EJECTED) sId = (sId+1)%10;
    specialId[idt][3] = sId;   
    
}



public static void setTeamsState(int st)
{
	
	    for(int i = 0;i < 10;i++)
        {           
            if (teamA[i].state != soccerPlayer.EJECTED) teamA[i].state = st;//setState(st);                    
            if (teamB[i].state != soccerPlayer.EJECTED) teamB[i].state = st;//setState(st);        
            if (st == soccerPlayer.PREMATCH)
            {
                 teamA[i].formed = false;        
                 teamB[i].formed = false;        
            }
        }
        
}

public int closestPlayer2Ball(soccerPlayer team[])
{
    int thePlayer = -1;
    int minDist = INFINITE;
    for(int i = 0; i < 10;i++)
    {
        if (
        //team[i].state == soccerPlayer.WAIT || team[i].state == soccerPlayer.GOPLACE
        team[i].state < 0
        ) 
            continue;
        int d;
        int dx = team[i].x - ball.x;
        int dy = team[i].y - ball.y;    
        int angle = ball.atan(dy, dx);
        int deno = soccerPlayer.sin(angle);
        if (deno == 0) d = Math.abs(dx);
        else d = Math.abs((dy << 10) / deno );           
            
        //int d = soccerPlayer.distance(team[i], ball);
        
        if (d < minDist)
        {
            minDist = d;
            thePlayer = i;
        }
    }
    return thePlayer;
}

public void precorner(soccerPlayer[] _team)
{    
	soundPlayEx(SOUND_WHISTLE);
	gfxMatchEvent = GFXMATCH_EVENT_CORNER;
    humanSoccerPlayer = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = constants.WAITCORNER;
    wait = 10;    
}

public void preout(soccerPlayer[] _team)
{
	soundPlayEx(SOUND_WHISTLE);
	gfxMatchEvent = GFXMATCH_EVENT_OUT;
    humanSoccerPlayer  = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = constants.WAITOUT;
    wait = 10;    
}


public void prefault(soccerPlayer[] _team, boolean penalty)
{    
	soundPlayEx(SOUND_WHISTLE);
    preMatchState = matchState;
    theTeam2 = _team;
     
    wait = 15;    
    if (penalty)
    {
    	gfxMatchEvent = GFXMATCH_EVENT_PENALTY;
        strikeTeam = theTeam2;
        matchState = constants.WAITPENALTY;
        //wait = 15;    
    }
    else
    {
    	gfxMatchEvent = GFXMATCH_EVENT_FAULT;
        //humanSoccerPlayer = null;
        //preMatchState = matchState;
        //theTeam2 = _team;
        matchState = constants.WAITFAULT;
        //wait = 15;    
    }
    if (_team == teamB) humanSoccerPlayer = null;
}

public void gfxMatchCamTick()
{
	int cx, cy;

	if(ball.theOwner == null) 
	{ 
		if (wait > 0 && matchState == constants.WAITOFFSIDE)
		{
			cx = ax; cy = ay;
		}
		else
		{				
			cx = ball.fx; cy = ball.fy;
		}
	}
	else 
	{ 
			cx = ball.fx + (ball.cos(32*(ball.theOwner.direction - 2))<<2); 
			cy = ball.fy + (ball.sin(32*(ball.theOwner.direction - 2))<<2);		
	}

	
	cameraX += (cx - cameraX)/4;
	cameraY += (cy - cameraY)/4;
	
	//#ifdef FEA_CHAPAS
	//int cw2 = canvasWidth/2;
	
	/*if (cameraX < (canvasWidth-20)<<8) cameraX = (canvasWidth-20)<<8;
	if (cameraX > (20+GROUNDWIDTH-(canvasWidth)<<8)) cameraX = (20+GROUNDWIDTH-(canvasWidth)<<8);
	
	//if (cameraY < (canvasHeight-40)<<8) cameraY = (canvasHeight-20)<<8;
	if (cameraY < (canvasHeight-80)<<8) cameraY = (canvasHeight-80)<<8;
	if (cameraY > (60+GROUNDHEIGHT-(canvasHeight)<<8)) cameraY = (60+GROUNDHEIGHT-(canvasHeight)<<8);*/
	
	//if (cameraX > (HGROUNDWIDTH+canvasWidth+10)<<8) cameraX = (HGROUNDWIDTH+canvasWidth+10)<<8;
	//System.out.println("cameraX:"+(cameraX>>8)+" - cw2:"+canvasWidth);
	//if (cx > (cw2+GROUNDWIDTH+10)<<8) cx = (cw2+GROUNDWIDTH+10)<<8; 
	//#endif
}

public void changeToFormation(boolean reset)
{
	int lin, col;
	
	if (userTeamIsA)	
	{
		formationA = userFormation;		
	}
	else
		formationB = userFormation;
	
	lin = 0;
    col = 0;
    
    for(int i = 9;i >= 0;i--) 
    {
                
    	teamA[i].activate(
    			regla3(100-formCampoEjes[formationA][lin][col], 100, GROUNDWIDTH),
    			regla3(100-formCampoEjes[formationA][lin][col+1], 200, GROUNDHEIGHT),     			 
    			TEAMA, reset);
    	
    	col += 2;
    	if (col >= formCampoEjes[formationA][lin].length)
    	{
    		lin++;
    		col = 0;
    	}
    }
    
    
    lin = 0;
    col = 0;
    
    for(int i = 9;i >= 0;i--) 
    {
                
    	teamB[i].activate(
    			regla3(100-formCampoEjes[formationB][lin][col], 100, GROUNDWIDTH),
    			regla3(100-formCampoEjes[formationB][lin][col+1], 200, GROUNDHEIGHT),     			 
    			TEAMB, reset);
    	
    	col += 2;
    	if (col >= formCampoEjes[formationB][lin].length)
    	{
    		lin++;
    		col = 0;
    	}
    }
    
    /*
    for(int i = 0;i < 10;i++) {
    	    	
        //teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA, true);
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB, reset);
        
    }*/
                                  
}

int disadA, disadB;
static boolean needToScore[] = new boolean[2];//A, needToScoreB;
static boolean noScore[] = new boolean[2];


public boolean gfxMatchTick()
{
	listenerInit(SOFTKEY_NONE, SOFTKEY_MENU);
	menuListTitleA = menuListTitleB = null;
	
	needToScore[TEAMA] = needToScore[TEAMB] = false;
	int pA, cA;
	int pB, cB;
	
	pA = (league.userMatch[0].matchGoals*globalMatchTime) / (totalMatchTime);
	cA = ((goalsA)*globalMatchTime) / (totalMatchTime);
	
	pB = (league.userMatch[1].matchGoals*globalMatchTime) / (totalMatchTime);
	cB = ((goalsB)*globalMatchTime) / (totalMatchTime);	
	
	if (pA > cA) needToScore[TEAMA] = true;
	if (pB > cB) needToScore[TEAMB] = true;
	/*
	if (nfc%200 == 0)
	{
		com.mygdx.mongojocs.lma2007.Debug.println("needToScore[TEAMA]+"+needToScore[TEAMA]+" :"+pA+" - "+cA);
		com.mygdx.mongojocs.lma2007.Debug.println("needToScore[TEAMB]+"+needToScore[TEAMB]);
	}*/
	noScore[TEAMA] = noScore[TEAMB] = false;
	if (league.userMatch[0].matchGoals <= goalsA) noScore[TEAMA] = true;
	if (league.userMatch[1].matchGoals <= goalsB) noScore[TEAMB] = true;
		
	
	//disadA = league.userMatch[1].matchGoals - league.userMatch[0].matchGoals;
	//disadB = league.userMatch[0].matchGoals - league.userMatch[1].matchGoals;
	
	
	//if (debug != null) com.mygdx.mongojocs.lma2007.Debug.println("debug 0:"+debug.state);
    for(int i = 0; i < 2;i++)
        if (eject[i] >= 0) 
        {        
            teams[i][eject[i]].state = soccerPlayer.EJECTED;  
            if (league.userMatch[i].isUserTeam())
            {
            	int exId = league.extendedPlayer(teams[i][eject[i]].pid);
    			if (exId != -1) league.userPlayerStats[exId][League.SANCTION_JOURNEYS] = 2;
            	//league.userPlayerStats[eject[i]][com.mygdx.mongojocs.lma2007.League.SANCTION_JOURNEYS] = 2;
            	league.matchSuspended[matchSuspendedCount++] = league.userPlayerStats[eject[i]][League.EX_PLAYERID]; 
            }
            
            eject[i] = -1;
            recalculateSpecialIds(teams[i], i);
            ejected[i]++;
        } 
    if (halfMatch) {halfMatch();halfMatch = false;}
    if (changeFormation)
    {
    	newFormation = userFormation;
    	changeToFormation(false);
    	gfxMatchAssignPlayerData();
    	
    	league.reMatchPlay(league.userMatch[0], league.userMatch[1], (globalMatchTime*50)/totalMatchTime, goalsA, goalsB);		
		
        changeFormation = false;
        //com.mygdx.mongojocs.lma2007.Debug.println("Formation:"+tmpFormation[newFormation]);
    }
    
    if (matchState == constants.GOAL || matchState == constants.ENDGAME) return false;
    
    if (strikeTeam != null) lastStrikeTeam  = strikeTeam;
    
    //if (humanSoccerPlayer == null) keyPresTime = 0;
    
    if (matchState >= constants.WAITCORNER) 
    {
    	//com.mygdx.mongojocs.lma2007.Debug.println("wait "+wait);
        humanSoccerPlayer = null;
        wait--;
       

        ball.updateBall();

        if (ball.x < -PLAYGROUNDBORDER) ball.fx = -(PLAYGROUNDBORDER)<<8;
        if (ball.x > GROUNDWIDTH+PLAYGROUNDBORDER) ball.fx = (GROUNDWIDTH+PLAYGROUNDBORDER)<<8;     
        if (ball.y < -PLAYGROUNDBORDER) ball.fy = -(PLAYGROUNDBORDER)<<8;
        if (ball.y > GROUNDHEIGHT+PLAYGROUNDBORDER) ball.fy = (GROUNDHEIGHT+PLAYGROUNDBORDER)<<8;
     
        
        if (ball.y < -(GOALDEPTH/2)+GOALPOSTSIZE) ball.fy = -((GOALDEPTH/2)+GOALPOSTSIZE)<<8;
        if (ball.y > GROUNDHEIGHT+(GOALDEPTH/2)-GOALPOSTSIZE) ball.fy = (GROUNDHEIGHT+(GOALDEPTH/2)-GOALPOSTSIZE)<<8;
     
        
        if (matchState == constants.WAITGOAL)
        {
             if (ball.x <= HGROUNDWIDTH - (HGOALSIZE) + GOALPOSTSIZE
             || ball.x > HGROUNDWIDTH + (HGOALSIZE) - GOALPOSTSIZE)             
             {
                ball.fx -= ball.sx;
                ball.sx = 0;      
             }             
        }
        else
        {
        	// BALL CONSTRAINS
            //#ifndef DOJA                        
            if (
            		(		ball.relativeY(ball.y) > GROUNDHEIGHT + 3 &&
            				(ball.sx > 0 && Math.abs(ball.x - (HGROUNDWIDTH - HGOALSIZE)) < 6) 
            				|| 
            				(ball.sx < 0 && Math.abs(ball.x - (HGROUNDWIDTH + HGOALSIZE)) < 6) 
            		)		
            		&& ball.h>>8 < GOALHEIGHT)
            	ball.sx = ball.sx>>3;
            	//ball.sy = ball.sy>>1;
            //#endif
        }
        if (matchState >= constants.WAITFAULT) {kickPlayer.animation(true);kickPlayer.timing++;}
        if (wait <= 0)
        {
            int tmp = matchState;
            matchState = preMatchState;
            //#ifndef DOJA
            //com.mygdx.mongojocs.lma2007.GameCanvas.keyPresTime = 0;
            //#endif
            switch(tmp)
            {
                case constants.WAITCORNER:
                        //corner(theTeam2);                        
                        humanSoccerPlayer = null;    
                        outSide = ball.x < HGROUNDWIDTH?0:GROUNDWIDTH-1;
                        outSideV = ball.y < HGROUNDHEIGHT?0:GROUNDHEIGHT-1;
                        ball.activate(outSide , outSideV);                    
                        setTeamsState(soccerPlayer.PRECORNER);    
                        strikeTeam = theTeam2;    
                        break;
                case constants.WAITOUT:
                        //out(theTeam2);
                            humanSoccerPlayer = null;    
                            outSide = ball.x < HGROUNDWIDTH?0:GROUNDWIDTH-1;
                            if (theTeam2 == teamA) strikeTeam = teamB;           
                            else strikeTeam = teamA;
                            if (ball.y < 0) ball.y = 1;
                            if (ball.y >= GROUNDHEIGHT)  ball.y = GROUNDHEIGHT-1;
                            ball.activate(outSide , ball.y);                    
                            setTeamsState(soccerPlayer.PREOUT);        
                        break;
                case constants.WAITGOAL:
                        
                            //GOL 
                			recordMatchGoal(ball.pid, theTeam);
                            humanSoccerPlayer = null;
                            
                            if (theTeam == TEAMA)
                            {
                                goalsA++;
                                strikeTeam = teamB;                                       
                           }
                           else
                           {
                                //GOL 
                                goalsB++;
                                strikeTeam = teamA;
                           }
                           ball.activate(HGROUNDWIDTH, HGROUNDHEIGHT);            
                           setTeamsState(soccerPlayer.PREMATCH);
                           gkA.state = soccerPlayer.FREE;
                           gkB.state = soccerPlayer.FREE;       
                           firstPass = true;                           
                           //setGameStatus(GAME_GOAL_LED);
                        
                        break;       
                case constants.WAITFAULT:
                		if (ball.distance(kickPlayer) > 8) break; 
                        ball.activate(kickPlayer.x, kickPlayer.y);
                        /*ball.fx = kickPlayer.fx;
                        ball.fy = kickPlayer.fy;
                        ball.sx = 0;
                        ball.sy = 0;*/
                        setTeamsState(soccerPlayer.FREE);
                        kickPlayer.state = soccerPlayer.KICKBALL;
                        kickPlayer.wait = 23;
                        kickPlayer.timing = 0;
                        freeKick = true;
                        
                //        goal(theTeam);
                        break; 
                case constants.WAITPENALTY:
                        matchState = constants.PENALTIES;
                        strikeTeam = theTeam2;
                        strikeTeam[nPP].formed = false;
                        ball.activate(HGROUNDWIDTH, strikeTeam[nPP].relativeY(GROUNDHEIGHT - GROUNDHEIGHT/8));
                        setTeamsState(soccerPlayer.PREPENALTY);
                        gkA.state = soccerPlayer.PREPENALTY;
                        gkB.state = soccerPlayer.PREPENALTY;
                        //setGameStatus(GAME_PENALTY_LED);
                        //penalty();
                        break;  
                 case constants.WAITGOALKICK:
                	  ball.activate(GROUNDWIDTH/2, ball.y > HGROUNDHEIGHT?((GROUNDHEIGHT-LITTLEAREAHEIGHT)):((LITTLEAREAHEIGHT)));
                	  /*ball.h = 0;
                	  ball.fx = (GROUNDWIDTH)<<7;
                	  ball.fy = ball.y > HGROUNDHEIGHT?((GROUNDWIDTH-LITTLEAREAHEIGHT)<<8):((LITTLEAREAHEIGHT)<<8);*/
                	  //if (ball.theOwner == kickPlayer)
                	  //{
                		  setTeamsState(soccerPlayer.PREGOALKICK);    
                		  kickPlayer.state = soccerPlayer.PREGOALKICK;
                		  lastStrikeTeam = strikeTeam = kickPlayer.myTeam;
                		  ball.theOwner = kickPlayer;
                		  //takeBallUnconditional();
                	  //}
                	  break;
                 //#ifdef OFFSIDE
                 case constants.WAITOFFSIDE:
                	  kickPlayer.angleShot = 192-(kickPlayer.attackDirection*32);		
               	  	  matchState = constants.WAITFAULT;
               	  	  kickPlayer.fx = ax;
                	  kickPlayer.fy = ay;
                	  kickPlayer.x = ax>>8;
                	  kickPlayer.y = ay>>8;
                	  ball.activate(kickPlayer.x, kickPlayer.y); 
               	  	  break;
               	 //#endif
            }
        }
        return false;
    }
    
    //#ifndef NOFAULTBUG
    /*
	if (freezeHumanPlayer > 0) 
	{
		freezeHumanPlayer--;
		if (freezeHumanPlayer == 0)
		{
			keyPresTime = 0;
		}
	}*/
	//#endif
	
    nfc++;
    globalMatchTime++;
    if (teamA[0].state != soccerPlayer.PREMATCH) timeCounter++;
    //if (keyMenu != 0 && lastKeyMenu == 0) beginPenaltyRound();//halfMatch(3,4);
    pointer += pointerSx;
    if (Math.abs(pointer-HGROUNDWIDTH) > HGOALSIZE)  pointerSx = -pointerSx;
    
    
    
    ////////////////////////////
    //UPDATES///////////////////
    ball.updateBall();        
    updatePlayers();        
    ///////////////////////////
    
    //if (checkBallRestrics == 0)
    //{
        if (ball.y < 0 || ball.y >= GROUNDHEIGHT)
        {
                int teamComp = TEAMA;
                teamGoal = 1 - teamDown;
                int fy = (GROUNDHEIGHT-1)<<8;
                
            if (ball.y < 0)
            {
                teamComp = TEAMB;
                teamGoal = teamDown;
                fy = 0;
            }
            
            
            if (ball.x > HGROUNDWIDTH - HGOALSIZE && ball.x <= HGROUNDWIDTH + HGOALSIZE && ball.h>>8 < GOALHEIGHT)
            {
                    //POSTE O GOL
                    if (ball.x <= HGROUNDWIDTH - HGOALSIZE + GOALPOSTSIZE
                     || ball.x > HGROUNDWIDTH + HGOALSIZE - GOALPOSTSIZE || ball.h>>8 > GOALHEIGHT-GOALPOSTSIZE)
                    {
                        
                        //POSTE
                        ball.fy = fy;
                        ball.sy = -(ball.sy*65)/100;     
                        soundPlayEx(SOUND_UY);
                    }
                    else 
                        //GOL
                     {
                        //pregoal(teamGoal);
                    	int fx = SOUND_GOALRECEIVED;
                    	if (userTeamIsA && teamGoal == TEAMA) fx = SOUND_GOALSCORED;
                    	if (!userTeamIsA && teamGoal == TEAMB) fx = SOUND_GOALSCORED;
                    	soundPlayEx(fx);
                    	
                    	gfxMatchEvent = GFXMATCH_EVENT_GOAL;
                        humanSoccerPlayer  = null;
                        preMatchState = matchState;
                        theTeam = teamGoal;
                        matchState = constants.WAITGOAL;
                        wait = 9;                           
                    }
            }   
            else
            {
            	if (ball.x >= HGROUNDWIDTH - GOALSIZE || ball.x <= HGROUNDWIDTH + GOALSIZE) soundPlayEx(SOUND_UY);
                    //FUERA DE PORTERIA O CORNER
                    if (teamDown == teamComp) 
                    {
                        if (lastStrikeTeam == teamA)                            
                            precorner(teamB);        
                        else {
                        	
                        	gfxMatchEvent = GFXMATCH_EVENT_OUT;
                            preMatchState = matchState;
                            matchState = constants.WAITGOALKICK;
                            wait = 10;    
                            kickPlayer = gkA;
                            //gkA.takeBallUnconditional();
                            }
                    }
                    else
                    { 
                        if (lastStrikeTeam == teamB)                            
                            precorner(teamA);                    
                        else {
                        	gfxMatchEvent = GFXMATCH_EVENT_OUT;
                            preMatchState = matchState;
                            matchState = constants.WAITGOALKICK;
                            wait = 10;        
                            kickPlayer = gkB;
                            //gkB.takeBallUnconditional(); 
                            }
                    }            
                }
        }
        else 
                //FUERA BANDA
                if (ball.x < 0 || ball.x >= GROUNDWIDTH)     preout(lastStrikeTeam);  
    //}
    //else checkBallRestrics--;
    
    // ZNR TODO fin de partido
        
    if (matchState == constants.GAME && timeCounter > totalMatchTime+extraMatchTime)
    {
    	if (firstPart)
        {
             halfMatch = true;   
             firstPart = false;             
        }
    	else
    	{
    		return true;
    	}
    }
    	
    //if (debug != null) com.mygdx.mongojocs.lma2007.Debug.println("debug n:"+debug.state);
    return false;
}   


//#ifdef OFFSIDE
int ax, ay;

public void preoffside(soccerPlayer sp)
{	
	gfxMatchEvent = GFXMATCH_EVENT_OFFSIDE;
	int _tid = 1 - sp.team;
	//com.mygdx.mongojocs.lma2007.Debug.println("#########OFFSIDE");
	preMatchState = matchState;
	matchState = constants.WAITOFFSIDE;
	theTeam2 = teams[_tid];
	strikeTeam = theTeam2;
	kickPlayer = theTeam2[specialId[_tid][2]];
	ax = sp.fx;
	ay = sp.fy;
	//ball.sx = ball.sy = 0;
	//com.mygdx.mongojocs.lma2007.GameCanvas.setTeamsState(FREE);
	//com.mygdx.mongojocs.lma2007.GameCanvas.humanSoccerPlayer = null;
	wait = 40;    
	humanSoccerPlayer = null;
}
//#endif

int teamStyleAtt[] = {-20, -10, 0};
int teamTacticAtt[] = {20, 20};

public void updatePlayers()
{
	if (userTeamIsA)
	{
		teamTacticAtt[0] = teamStyleAtt[squadStyle];
		teamTacticAtt[1] = teamStyleAtt[1];
	}
	else
	{
		teamTacticAtt[0] = teamStyleAtt[1];
		teamTacticAtt[1] = teamStyleAtt[squadStyle];
	}
	
	
	//if (debug != null) com.mygdx.mongojocs.lma2007.Debug.println("debug 1:"+debug.state);
      //teamStrategy(); ////////////////////////////////////////        
	  int bally = ball.y;
	  if (ball.theOwner == gkA || ball.theOwner == gkB) bally = HGROUNDHEIGHT;
	    
        
        for(int i = 0; i < 2;i++) {
            
        	teamCenterX[i] = GROUNDWIDTH + ((ball.x - HGROUNDWIDTH) / 6);
        	
            if (teamDown == i) {
                
                if (teams[i] == lastStrikeTeam) {
                    
                    teamCenterY[i] = HGROUNDHEIGHT + bally;
                    teamAtt[i] = 140+teamTacticAtt[i];
                    int factor = ((GROUNDHEIGHT-bally)*150) / GROUNDHEIGHT;
                    teamCenterY[i] = teamCenterY[i] - (250-factor*2);
                    
                } else {
                    
                    teamCenterY[i] = HGROUNDHEIGHT + bally;
                    if (teamCenterY[i] > 620) teamCenterY[i] = 620;
                    int factor = (((GROUNDHEIGHT-bally)*150) / GROUNDHEIGHT);
                    teamCenterY[i] = -10 + teamCenterY[i] - (200-factor*2);
                    teamAtt[i] = 30+(factor)+teamTacticAtt[i];                    
                }
                
            } else {
                
            	teamCenterX[i] = ((ball.x - HGROUNDWIDTH) /6);
            	
                if (teams[i] == lastStrikeTeam) {
                    
                    teamCenterY[i] = bally - HGROUNDHEIGHT;
                    teamAtt[i] = 140+teamTacticAtt[i];
                    int factor = (bally*150 / GROUNDHEIGHT);
                    teamCenterY[i] = teamCenterY[i] + (250-factor*2);
                    
                } else {
                    
                    teamCenterY[i] = bally - HGROUNDHEIGHT;
                    if (teamCenterY[i] < -180) teamCenterY[i] = -180;
                    int factor = (bally*150 / GROUNDHEIGHT);
                    teamCenterY[i] = 10 + teamCenterY[i] + (200-factor*2);
                    teamAtt[i] = 30 + ((factor*7)/10) + teamTacticAtt[i];                                               
                    
                }
            }   
                
        }
    //TEAM STRATEGY
    
    
    
    // BEGIN PENALTY
    if (matchState == constants.PENALTIES)
    {          
        if (strikeTeam[nPP].formed && (gkA.wait == -10 || gkB.wait == -10))
        {
            strikeTeam[nPP].state = soccerPlayer.PENALTY;
            strikeTeam[nPP].timing = 0;
            gkA.timing = 0;gkB.timing = 0;    
        }
        //if (gkA.state == soccerPlayer.OWNBALL || gkB.state == soccerPlayer.OWNBALL) matchState = GAME;//nextPenalty();
    }
    // END PENALTY
    
    //#ifndef NOSKIP
    if (keyMenu == 2 && lastKeyMenu == 0) skip = true;
    //#endif
    
    boolean holdEnemies = false;    
    boolean holdPlayer = false;    
    
    for(int i = 0;i < 10;i++)
    {          
    	if (teamA[i].state == soccerPlayer.KICKBALL || 
            matchState >= constants.WAITFAULT) holdEnemies = true;
        /*if (
        		//#ifndef NOFAULTBUG
        		com.mygdx.mongojocs.lma2007.GameCanvas.freezeHumanPlayer == 0 &&
        		//#endif
        		!holdPlayer &&  (
        		//#ifndef DOJA
        		teamA[i].state == soccerPlayer.OUT ||
        		//#endif
        		teamA[i].state == soccerPlayer.FOLLOW || teamA[i].state == soccerPlayer.OWNBALL || teamA[i].state == soccerPlayer.KICKBALL || teamA[i].state == soccerPlayer.PENALTY)) 
        {  
            humanSoccerPlayer = teamA[i];                                        
          
        }
        else*/ 
            teamA[i].update();    
        //EKIPO B
        if (holdEnemies && teamB[i].state > 0)   {teamB[i].state = soccerPlayer.FREE;
        //if (debug != null) com.mygdx.mongojocs.lma2007.Debug.println("cambio a 0");
        }
        teamB[i].update();        
        if ((teamB[i].state == soccerPlayer.KICKBALL || holdPlayer ) /*&& humanSoccerPlayer != null*/)
        {
        	holdEnemies = true;
        	holdPlayer = true;
        	//com.mygdx.mongojocs.lma2007.Debug.println("codigo inutil?");
            /*humanSoccerPlayer.state = soccerPlayer.FREE;
            humanSoccerPlayer.update();
            humanSoccerPlayer = null;*/             
        }
        //if (matchState >= constants.WAITFAULT)
    }
    //if (debug != null) com.mygdx.mongojocs.lma2007.Debug.println("debug 2:"+debug.state);
    //#ifndef NOSKIP
    skip = false;
    //#endif
    //np = -1;
            
    gkA.update(ball);
    gkB.update(ball);    
    
    /*if (humanSoccerPlayer != null) {    
        //#ifdef LATERALVIEW
        humanSoccerPlayer.manualUpdate(keyY, keyX, keyMenu == 2);
        //#else
        humanSoccerPlayer.manualUpdate(keyX, keyY, keyMenu == 2);        
        //#endif        
    }*/
    
    int ip = nfc%10;
    if (ip == 0) nformed = 0;
    
    soccerPlayer pUpdate;
    pUpdate = teamA[ip];
    pUpdate.myStatus = pUpdate.status();
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;
    pUpdate = teamB[ip];
    pUpdate.myStatus = pUpdate.status();
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;        
    
    //updateSoccerPlayer(ip*2);
    //updateSoccerPlayer((ip*2)+1);
    
    int si = specialId[strikeTeam[9].team][1];
    if (nformed >= 20-ejected[TEAMA]-ejected[TEAMB] && strikeTeam[si].state == soccerPlayer.PREMATCH)
    {
    	//#ifdef COINTOSS
    	//setTeamsState(soccerPlayer.TRUEPREMATCH);
    	//#else
        setTeamsState(soccerPlayer.FREE);
        ball.takeBall(strikeTeam[si]);//Unconditional();
        //#endif       
    }
    
     
    //if (matchState != PENALTIES)
    //{
        if (ball.theOwner != gkA && ball.theOwner != gkB)
        {
            if (humanSoccerPlayer == null || (keyX == 0 && keyY == 0))//AKI AKI!!!!
            {
                int fPlayer = -1;
                if (matchState == constants.GAME) fPlayer = closestPlayer2Ball(teamA);
                if (fPlayer != -1 && !holdPlayer)                
                {
                    if (teamA[fPlayer].state == soccerPlayer.FREE || fPlayer == np)
                    {
                        for(int i = 0; i < 10;i++)
                            if (
                              teamA[i].state >= 0
                              ) teamA[i].state = soccerPlayer.FREE;
                        teamA[fPlayer].state = soccerPlayer.FOLLOW;
                        //com.mygdx.mongojocs.lma2007.Debug.println("FOLLOW ="+fPlayer+" tnp:"+np);
                    }
                    if (np >= 0)                    
                    {
                        teamA[fPlayer].state = soccerPlayer.FREE;
                        teamA[np].state = soccerPlayer.OWNBALL;
                        np = -1;
                    }
                }              
            }
            //if (debug != null) com.mygdx.mongojocs.lma2007.Debug.println("debug 3:"+debug.state);
            int fPlayer = -1;
            if (matchState == constants.GAME) fPlayer = closestPlayer2Ball(teamB);
            if (fPlayer != -1 && !holdEnemies)
            {
                if (teamB[fPlayer].state == soccerPlayer.FREE)
                {
                    for(int i = 0; i < 10;i++)
                        if (teamB[i].state > 0) teamB[i].state = soccerPlayer.FREE;
                    teamB[fPlayer].state = soccerPlayer.FOLLOW;
                }
            }
            //if (debug != null) com.mygdx.mongojocs.lma2007.Debug.println("debug 4:"+debug.state);
        }
        else 
        {
            //UN PORTERO TIENE LA PELOTA
            /*if (ball.theOwner == gkA)
                humanSoccerPlayer = gkA;
            else */
                humanSoccerPlayer = null;
            setTeamsState(soccerPlayer.FREE);                       
        }
    //}    
        
        //PATCH
        if (matchState == constants.WAITFAULT) {humanSoccerPlayer = null;}
}




public void sortEntities()
{
    boolean repeat;
    int intentos = 0;
    //int comps = 0;
    //nums++;
    
    //#ifdef ISOMETRICMATCH
    
    do{
        
        intentos++;
        repeat = false;
        for(int i = 0; i < 22;i++)
        {
            if (sortedEntities[i+1].fx + sortedEntities[i+1].fy < sortedEntities[i].fx + sortedEntities[i].fy) 
            {
                repeat = true;
                soccerPlayer t = sortedEntities[i+1];
                sortedEntities[i+1] = sortedEntities[i];
                sortedEntities[i] = t;
                //break;
            }
        }      
    }while(repeat && intentos < 2);
    
    return;
    //#else
	//#endif
    
    //suma += comps;
    //com.mygdx.mongojocs.lma2007.Debug.println("media :"+((suma)/nums));
}



// RES

//byte[] domesticLeagueBuffer, licensedLeagueBuffer, plantOriginal, plantTemp;
//Image persistentMenuimage, persistentCaratula;

public void initialResourcesInit()
{	
	/*
	formation = new int[6][10][2];
    
    byte aux[] = loadFile("/formation.dat");
    
    for(int i = 0; i<120; i++) 
    formation[i/20][(i/2)%10][i%2] = aux[i];
    
    aux = null;	
    */
	
    gfxMatchRnd = new Random(System.currentTimeMillis());
    
	soccerPlayer.initTables(this);
	
	//plantOriginal = loadFile("/plant.png");
	//plantTemp = new byte[plantOriginal.length];
	            
    
    
    	
}



public void gfxMatchDraw()
{
	if (!gfxMatchShow) return;
	
	gfxMatchShow = false;
		
	//#ifndef FEA_CHAPAS	
	isometricMatchDraw();
	//#else
	//chapasMatchDraw();
	//#endif
	        
}

//#ifdef FEA_CHAPAS

Image grassImg;
Image chapasImg;
Image goalsImg;

public void chapasMatchDraw()
{
	//fillDraw(0x69a209,0,0,canvasWidth,canvasHeight);
	
	int offx = -32+chX(0)%32;
	int offy = -32+chY(0)%32;
	for(int i = 0; i < canvasHeight+64;i += 32)
		for(int j = 0; j < canvasWidth+64;j += 32)
		{
			imageDraw(grassImg, 0, 0, 32, 32, offx+j, offy+i);
		}
	
	scr.setClip(0,0,canvasWidth,canvasHeight);
	putColor(0xffffff);
	
	scr.drawRect(chX(0), chY(0), shX(GROUNDWIDTH), shY(GROUNDHEIGHT));
	scr.drawLine(chX(0), chY(HGROUNDHEIGHT), chX(GROUNDWIDTH), chY(HGROUNDHEIGHT));
	
	
	scr.drawRect(chX((GROUNDWIDTH - AREAWIDTH) / 2), chY(0), shX(AREAWIDTH), shY(AREAHEIGHT));
	scr.drawRect(chX((GROUNDWIDTH - LITTLEAREAWIDTH) / 2), chY(0), shX(LITTLEAREAWIDTH), shY(LITTLEAREAHEIGHT));
	
	scr.drawRect(chX((GROUNDWIDTH - AREAWIDTH) / 2), chY(GROUNDHEIGHT - AREAHEIGHT), shX(AREAWIDTH), shY(AREAHEIGHT));
	scr.drawRect(chX((GROUNDWIDTH - LITTLEAREAWIDTH) / 2), chY(GROUNDHEIGHT) - shY(LITTLEAREAHEIGHT), shX(LITTLEAREAWIDTH), shY(LITTLEAREAHEIGHT));

	scr.drawArc(chX(HGROUNDWIDTH)-20, chY(HGROUNDHEIGHT)-20, 40, 40, 0, 360);
	
	/*
	scr.drawRect(chX((GROUNDWIDTH - GOALSIZE) / 2), 
			     chY(GROUNDHEIGHT), 
			     shX(GOALSIZE), 
			     shY(10));

	scr.drawRect(chX((GROUNDWIDTH - GOALSIZE) / 2), 
		     chY(-10), 
		     shX(GOALSIZE), 
		     shY(10));
		     */
	imageDraw(goalsImg, 0, 0, 26, 6, chX(HGROUNDWIDTH)-13, chY(0)-5);
	imageDraw(goalsImg, 0, 6, 26, 6, chX(HGROUNDWIDTH)-13, chY(GROUNDHEIGHT));
	
	
	//scr.drawArc(chX(HGROUNDWIDTH)-20, chY(HGROUNDHEIGHT)-20, 40, 40, 0, 360);
	
	
	//scr.fillArc(chX(ball.x)-1, chY(ball.y)-1, 3, 3, 0, 360);
	imageDraw(chapasImg, 10, 0, 5, 5, chX(ball.x)-2, chY(ball.y)-2);
	
	
	for(int i = 0; i < 11;i++)
	{
		imageDraw(chapasImg, 0, 0, 5, 5, chX(teamA[i].x)-2, chY(teamA[i].y));
		imageDraw(chapasImg, 5, 0, 5, 5, chX(teamB[i].x)-2, chY(teamB[i].y));
		//putColor(0xff00000);
		//scr.fillArc(chX(teamA[i].x)-2, chY(teamA[i].y)-2, 5, 5, 0, 360);
		
		//putColor(0x00000ff);
		//scr.fillArc(chX(teamB[i].x)-2, chY(teamB[i].y)-2, 5, 5, 0, 360);
	}
	
	
	if(ball.theOwner != null) {
		
		lastBallOwner = ball.theOwner;
	}
				
	if(lastBallOwner != null) {
					
		soccerPlayer e = ball.theOwner; 
		
		printSetArea(chX(lastBallOwner.x) - canvasWidth/4 , chY(lastBallOwner.y) - 5 - printGetHeight(), canvasWidth/2, printGetHeight());
				  
		fontInit(FONT_WHITE);
       
		printDraw(lastBallOwner.name, 0, 0, fontPenRGB, PRINT_HCENTER);
    	
    }
	
	//printDraw("X: "+(cameraX>>8)+", Y: "+(cameraY>>8), 0, 0, fontPenRGB, PRINT_HCENTER);
	//HUD
	displayGfxMatchEvent();
	
	int x = 10, y = 10, nameMaxWidth = 65;
	
	fontInit(FONT_BLACK);
	
	fillDraw(0xdbdbdb,x,y,nameMaxWidth,printGetHeight()*2);
	//fillDraw(league.userMatch[0].homeColor[2],x+nameMaxWidth - 10,y,5,printGetHeight());
	//fillDraw(league.userMatch[1].homeColor[2],x+nameMaxWidth - 10,y + printGetHeight(),5,printGetHeight());
	fillDraw(0,x+nameMaxWidth,y,20,printGetHeight()*2);
	fillDraw(0,x,y+printGetHeight()*2,30,printGetHeight());
		
	printSetArea(x,y,nameMaxWidth,printGetHeight()*2);
	String name = league.userMatch[0].name;
	if(name.length() >= 10) {
	
		name = name.substring(0,9) + ".";
	}
	printDraw(name, 4, 0, fontPenRGB, PRINT_MASK);
	name = league.userMatch[1].name;
	if(name.length() >= 10) {
	
		name = name.substring(0,9) + ".";
	}
	printDraw(name, 4, printGetHeight(), fontPenRGB, PRINT_MASK);
	
	fontInit(FONT_WHITE);
	printSetArea(x+nameMaxWidth,y,20,printGetHeight()*2);
	printDraw(""+goalsA, 0, 0, fontPenRGB, PRINT_HCENTER | PRINT_MASK);
	printDraw(""+goalsB, 0, printGetHeight(), fontPenRGB, PRINT_HCENTER | PRINT_MASK);
	
	fontInit(timeCounter > totalMatchTime ? FONT_ORANGE : FONT_WHITE);
	printSetArea(x,y+2*printGetHeight(),30,printGetHeight());
	printDraw(""+matchClockStr((isHalfMatch ? 2700 : 0) + (timeCounter * 2700)/(totalMatchTime)), 0, 0, fontPenRGB, PRINT_HCENTER | PRINT_MASK);
	
	//com.mygdx.mongojocs.lma2007.Debug.println(""+halfMatch);
	
	
	fontInit(FONT_BLACK);
	
    printSetArea(canvasWidth/4, canvasHeight-printGetHeight()*2, canvasWidth/2, printGetHeight());
    fillDraw(LISTENER_BAR_RGB, canvasWidth/4, canvasHeight-printGetHeight()*2, canvasWidth/2, printGetHeight());
			    
	printDraw(getMenuText(MENTXT_GFXMATCHSPEED)[gfxMatchSpeed], 0, 0, fontPenRGB, PRINT_HCENTER);

}


public int chX(int x) {
	
	x -= cameraX>>8;
		
	return (x>>1) + (canvasWidth/2);
}

public int chY(int y) {
	
	y -= cameraY>>8;
	
	return (y>>1) + (canvasHeight/2);
}

public int shX(int x) {
		
	return (x>>1);
}

public int shY(int y) {
		
	return (y>>1);
}
//#endif

    	//minimapa
/*    
    	int x = 32;
    	int y = 20;
    	int gw = GROUNDWIDTH/6;
    	int gh = GROUNDHEIGHT/6;
    	
    	putColor(0xffff80);
    	scr.setClip(0, 0, canvasWidth, canvasHeight);
    	scr.drawRect(x, y, gh, gw);
    	putColor(0xffffff);
    	
    	soccerPlayer mapTeam[];
    	
    	if (userTeamIsA) 
    		mapTeam = teamA;
    	else 
    		mapTeam = teamB;
    	
    	for(int i = 0; i < 11; i++)
    	{
    		int xx = mapTeam[i].y;
    		int yy = mapTeam[i].x;
    		
    		xx = (xx * gh) / GROUNDHEIGHT;
    		yy = (yy * gw) / GROUNDWIDTH;
    		
    		scr.drawLine(x+xx, y+yy, x+xx, y+yy);
    	}
    	
    	
    	putColor(0xff0000);
    	if (userTeamIsA) 
    		mapTeam = teamB;
    	else 
    		mapTeam = teamA;
    	
    	for(int i = 0; i < 11; i++)
    	{
    		int xx = mapTeam[i].y;
    		int yy = mapTeam[i].x;
    		
    		xx = (xx * gh) / GROUNDHEIGHT;
    		yy = (yy * gw) / GROUNDWIDTH;
    		
    		scr.drawLine(x+xx, y+yy, x+xx, y+yy);
    	}
  */  	
    	

//=================== end 2d match

// David

public static final int RAINQUANTITY = 30;
public static final int RAINFALLSPEED = 2048;
public static final int RAINSTARTHEIGHT = 40;
public static final int RAINRANGE = 100;
public static final int RAINCOLOR = 0xe3dede;

int rainData[][] = new int[RAINQUANTITY][3];


AnimationBank animBankA, animBankB, animBankGK, animBankBall;
Image isoGoal, isoShadow, isoBallShadow;
byte isoGoalCor[];

soccerPlayer lastBallOwner;

int colorB[];
//#ifndef FEA_CHAPAS
public void isometricMatchInit() 
{
	
//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_BIG_FONT
	//#ifndef FEA_EA
	fontBigWhiteImg = null;
	fontBigYellowImg = null;
	//#endif
//#endif

	backgroundImg = null;
	menuIconsImg = null;
	menuIconsImg1 = null;//loadImage("/iconos");
	menuIconsImg2 = null;//loadImage("/iconosx");	
	flechasImg = null;

	//#ifndef REM_TEAMSHIELDS
	shieldImg = null;
	//#endif

	
	System.gc();
	

//#ifndef SIMPLE_2DMATCH_FIELD
	
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Antes crear scroll()");
//#endif
	
	
	byte faseMap[] = loadFile("/campo.mmp");
	byte faseCom[] = loadFile("/0.btsc");
	
	int faseWidth = faseMap[0] & 0xff;
	int faseHeight = faseMap[1] & 0xff;
	System.arraycopy(faseMap, 2, faseMap, 0, faseWidth*faseHeight);
	
	
	Image tilesAImg = loadImage("/medio_tile1");
	Image tilesBImg = loadImage("/medio_tile2");
		
	scrollCreate(0, 0, canvasWidth, canvasHeight);	
	scrollInit(faseMap, faseCom, faseWidth, faseHeight, tilesAImg, tilesBImg, 8, 4);
	
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Despues crear scroll()");
//#endif	
	
//#endif
	
	
	//#ifndef REM_TEAMCOLORS
	if (gameMode == FANTASY)
	{
		for(int i = 0; i < 3;i++)
		{
			league.userTeam.homeColor[i] = league.userTeam.flagColor[0];
			league.userTeam.awayColor[i] = 0xffffff^league.userTeam.flagColor[0];
		}
		league.userTeam.homeColor[3] = league.userTeam.flagColor[1];
		league.userTeam.awayColor[3] = 0xffffff^league.userTeam.flagColor[1];
	}
	//#endif
	
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Antes crear datos animator");
//#endif	
	
	try {
	
		
		AnimationBank.inputPalette = new byte[][] {{(byte)0,(byte)0,(byte)255},
												   {(byte)3,(byte)1,(byte)108},
												   {(byte)172,(byte)15,(byte)17},
												   {(byte)111,(byte)0,(byte)0},
												   {(byte)100,(byte)203,(byte)245},
												   {(byte)60,(byte)132,(byte)198}, 
												   {(byte)241,(byte)92,(byte)28}, 
												   {(byte)191,(byte)74,(byte)23}};
		int rgb[] = new int[8];
		
		for(int i = 0; i < 4; i++) {
					
			int b = league.userMatch[0].homeColor[i] & 0xff;
			int g = (league.userMatch[0].homeColor[i] & 0xff00) >> 8;
			int r = (league.userMatch[0].homeColor[i] & 0xff0000) >> 16;
		
			rgb[2*i] = league.userMatch[0].homeColor[i];
			rgb[2*i + 1] = ((3*r/4) << 16) | ((3*g/4) << 8) | ((3*b/4) << 0);
			//rgb[2*i + 1] = rgb[2*i];
					
		}
		
								
		//com.mygdx.mongojocs.lma2007.AnimationBank.inputPalette = null;
		AnimationBank.outputPalette = rgb;
		AnimationBank.DATA_DIR = "/";					
		animBankA = new AnimationBank(AnimationConstants.FUTBOLISTA_B0, 0);
		// SAFE ZONE UNTIL HERE
		
		int colorA[] = league.userMatch[0].homeColor;
		colorB = league.userMatch[1].homeColor;
		
		int mask = 0xc0c0c0;
		if (((colorA[1] & mask) == (colorB[1] & mask)
			&& (colorA[2] & mask) == (colorB[2] & mask))
			||
			((colorA[1] & mask) == (colorB[2] & mask)
			&& (colorA[2] & mask) == (colorB[1] & mask)))
				colorB = league.userMatch[1].awayColor;
			
		for(int i = 0; i < 4; i++) {		
				
			int b = colorB[i] & 0xff;
			int g = (colorB[i] & 0xff00) >> 8;
			int r = (colorB[i] & 0xff0000) >> 16;
		
			rgb[2*i] = colorB[i];
			rgb[2*i + 1] = ((3*r/4) << 16) | ((3*g/4) << 8) | ((3*b/4) << 0);
			//rgb[2*i + 1] = rgb[2*i];
					
		}
		
		//com.mygdx.mongojocs.lma2007.AnimationBank.inputPalette = null;
		AnimationBank.outputPalette = rgb;
		AnimationBank.DATA_DIR = "/";					
		animBankB = new AnimationBank(AnimationConstants.FUTBOLISTA_B0, 0);
		
		// Memory save!!!
		
		animBankB.rects = animBankA.rects;
		animBankB.cellIndex = animBankA.cellIndex;		
		animBankB.frames = animBankA.frames;		
		animBankB.layers = animBankA.layers;
		
		System.gc();
		    
											
		for(int j = 0; j < 10; j++) {
				
			teamA[j].sprite = new AnimatedSprite(animBankA);
			teamA[j].sprite.setSequence(0);
			
			teamB[j].sprite = new AnimatedSprite(animBankB);
			teamB[j].sprite.setSequence(0);
		}		
		
		
		AnimationBank.inputPalette = null;
						
		AnimationBank.DATA_DIR = "/goalie";					
		animBankGK = new AnimationBank(AnimationConstants.PORTERO_B0, 0);
						
		teamA[10].sprite = new AnimatedSprite(animBankGK);
		teamA[10].sprite.setSequence(0);
		
		teamB[10].sprite = new AnimatedSprite(animBankGK);
		teamB[10].sprite.setSequence(0);
		
		AnimationBank.DATA_DIR = "/ball";					
		animBankBall = new AnimationBank(AnimationConstants.BALL_B0, 0);
		
		ball.sprite = new AnimatedSprite(animBankBall);
		ball.sprite.setSequence(AnimationConstants.BOLA_ALANTE_B0_S0);
		
		isoGoal = loadImage("/goal");
		isoGoalCor = loadFile("/goalCor.cor");		
		isoShadow = loadImage("/shadow");
		isoBallShadow = loadImage("/ballShadow");
		
		// Rain
		
		for(int i = 0; i < rainData.length; i++) {
		
			rainData[i][2] = ((i * RAINSTARTHEIGHT)/rainData.length) << 8; 
		}
		
			
		
	} catch(Exception e) {
		
		//#ifdef DEBUG
		Debug.println("Error initializing Animator resources!!!");
		//#endif
		
	};
	
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("despues crear datos animator");
//#endif
	
	lastBallOwner = null;
	menuListTitleA = null;
	menuListTitleB = null;
}

public void isometricMatchdestroy() {

//#ifndef SIMPLE_2DMATCH_FIELD	
	
	scrollDestroy();

//#endif
	
	for(int i = 0; i < teamA.length; i++) {
	
		teamA[i].sprite = null;
	}
	
	for(int i = 0; i < teamB.length; i++) {
		
		teamB[i].sprite = null;
	}
	
	ball.sprite = null;
	

	animBankA = null;
	animBankB = null;
	animBankGK = null;
	animBankBall = null;
	isoGoal = null;
	isoShadow = null;
	isoBallShadow = null;
	//com.mygdx.mongojocs.lma2007.AnimationBank.inputPalette = null;
	//com.mygdx.mongojocs.lma2007.AnimationBank.outputPalette = null;
	System.gc();
	
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("tras destruir datos animator");
//#endif
		
	flechasImg = loadImage("/flechas");
	
	//#ifndef REM_TEAMSHIELDS
	shieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.userTeam.flagColor);
	//#endif
	menuIconsImg1 = loadImage("/iconos");
	//#ifndef OLD_MENU_BAR
	menuIconsImg2 = loadImage("/iconosx");
	//#endif
	
	//#ifndef REM_BACKGROUND_IMAGE
	backgroundImg = loadImage("/back1");
	//#endif

//	JUAN QUERIDO, AKI CARGA LAS FONTS

//	#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_BIG_FONT
		fontBigWhiteImg = loadImage("/fontBigWhite");
		fontBigYellowImg = changePal(loadFile("/fontBigWhite.png"), fontBigWhiteRgbs, fontBigYellowRGB);
//	#endif

}

public void isometricMatchDraw() {
	
	
	
//#ifndef SIMPLE_2DMATCH_FIELD
	
	/*scrollTick(933 + 66 - 24 - isoX(0, 0), 204 - 16 - 24 - isoY(0, 0));
	scrollDraw(scr);*/
//#else
	fillDraw(0x89b209,0,0,canvasWidth,canvasHeight);
//#endif
					
	scr.setClip(0,0,canvasWidth,canvasHeight);
	
	drawFieldRect(0, 0, GROUNDWIDTH, GROUNDHEIGHT);
	drawFieldLine(0, HGROUNDHEIGHT, GROUNDWIDTH, HGROUNDHEIGHT);
	
	// Bottom Goal
	
	//imageDraw(isoGoal, 0, 0, 108, 108, isoX(HGROUNDWIDTH<<8, 0) - 40, isoY(HGROUNDWIDTH<<8, 0) - 64);
	//imageDraw(isoGoal, 108, 0, 108, 108, isoX(HGROUNDWIDTH<<8, 0) - 40, isoY(HGROUNDWIDTH<<8, 0) - 64);
	
	spriteDraw(isoGoal, isoX(HGROUNDWIDTH<<8, 0) - 40, isoY(HGROUNDWIDTH<<8, 0) - 64, 0, isoGoalCor, 4);
	spriteDraw(isoGoal, isoX(HGROUNDWIDTH<<8, 0) - 40, isoY(HGROUNDWIDTH<<8, 0) - 64, 1, isoGoalCor, 4);
		
	// Corners
	
	scr.setClip(0,0,canvasWidth,canvasHeight);
	
	drawFieldLine(4, 0, 0, 4);
	drawFieldLine(GROUNDWIDTH - 4, 0, GROUNDWIDTH, 4);
	drawFieldLine(4, GROUNDHEIGHT, 0, GROUNDHEIGHT - 4);
	drawFieldLine(GROUNDWIDTH - 4, GROUNDHEIGHT, GROUNDWIDTH, GROUNDHEIGHT - 4);
	
	// Small areas
	
	drawFieldRect((GROUNDWIDTH - AREAWIDTH) / 2, 0, AREAWIDTH, AREAHEIGHT);
	drawFieldRect((GROUNDWIDTH - LITTLEAREAWIDTH) / 2, 0, LITTLEAREAWIDTH, LITTLEAREAHEIGHT);
	
	drawFieldRect((GROUNDWIDTH - AREAWIDTH) / 2, GROUNDHEIGHT - AREAHEIGHT, AREAWIDTH, AREAHEIGHT);
	drawFieldRect((GROUNDWIDTH - LITTLEAREAWIDTH) / 2, GROUNDHEIGHT - LITTLEAREAHEIGHT, LITTLEAREAWIDTH, LITTLEAREAHEIGHT);
	
	/*
	drawFieldRect((GROUNDWIDTH - GOALSIZE) / 2, - GOALDEPTH, GOALSIZE, GOALDEPTH);
	drawFieldRect((GROUNDWIDTH - GOALSIZE + GOALPOSTSIZE*2) / 2, - GOALDEPTH + GOALPOSTSIZE, GOALSIZE - GOALPOSTSIZE*2, GOALDEPTH - GOALPOSTSIZE);
	
	drawFieldRect((GROUNDWIDTH - GOALSIZE) / 2, GROUNDHEIGHT, GOALSIZE, GOALDEPTH);
	drawFieldRect((GROUNDWIDTH - GOALSIZE + GOALPOSTSIZE*2) / 2, GROUNDHEIGHT, GOALSIZE - GOALPOSTSIZE*2, GOALDEPTH - GOALPOSTSIZE);
	*/
	
	// Circles
	
	scr.drawArc(isoX((HGROUNDWIDTH<<8), (HGROUNDHEIGHT<<8)) - 100, isoY((HGROUNDWIDTH<<8), (HGROUNDHEIGHT<<8)) - 50, 200, 100, 0, 360);
	scr.fillArc(isoX((HGROUNDWIDTH<<8), (HGROUNDHEIGHT<<8)) - 4, isoY((HGROUNDWIDTH<<8), (HGROUNDHEIGHT<<8)) - 2, 8, 4, 0, 360);
	
	scr.drawArc(isoX((HGROUNDWIDTH<<8), (PENALTYHEIGHT<<8)) - 150, isoY((HGROUNDWIDTH<<8), (PENALTYHEIGHT<<8)) - 75, 300, 150, 177, 96);
	scr.fillArc(isoX((HGROUNDWIDTH<<8), (PENALTYHEIGHT<<8)) - 4, isoY((HGROUNDWIDTH<<8), (PENALTYHEIGHT<<8)) - 2, 8, 4, 0, 360);
	scr.drawArc(isoX((HGROUNDWIDTH<<8), ((GROUNDHEIGHT - PENALTYHEIGHT)<<8)) - 150, isoY((HGROUNDWIDTH<<8), ((GROUNDHEIGHT - PENALTYHEIGHT)<<8)) - 75, 300, 150, 358, 96);
	scr.fillArc(isoX((HGROUNDWIDTH<<8), ((GROUNDHEIGHT - PENALTYHEIGHT)<<8)) - 4, isoY((HGROUNDWIDTH<<8), ((GROUNDHEIGHT - PENALTYHEIGHT)<<8)) - 2, 8, 4, 0, 360);

	sortEntities();
	
	for(int i = 0; i < sortedEntities.length; i++) {
		
		if(sortedEntities[i].id >= 0) {
		
			drawSoccerPlayer((soccerPlayer) sortedEntities[i], 12);
			
		} else {
			
			drawSoccerPlayer((soccerPlayer) sortedEntities[i], 8);
			
		}			
	}
	
		
	if(ball.theOwner != null) {
		
		lastBallOwner = ball.theOwner;
	}
				
	if(lastBallOwner != null) {
					
		soccerPlayer e = ball.theOwner; 
		
		printSetArea(isoX(lastBallOwner.fx, lastBallOwner.fy) - canvasWidth/4 , isoY(lastBallOwner.fx, lastBallOwner.fy) - 45, canvasWidth/2, printGetHeight());
				  
		fontInit(FONT_WHITE);
       
		printDraw(lastBallOwner.name, 0, 0, fontPenRGB, PRINT_HCENTER);
    	
    }

	// Front Goal
	
	//imageDraw(isoGoal, 0, 108, 108, 108, isoX(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 65, isoY(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 57);
	//imageDraw(isoGoal, 108, 108, 108, 108, isoX(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 65, isoY(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 57);
	
	spriteDraw(isoGoal, isoX(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 65, isoY(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 57, 2, isoGoalCor, 4);
	spriteDraw(isoGoal, isoX(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 65, isoY(HGROUNDWIDTH<<8, GROUNDHEIGHT<<8) - 57, 3, isoGoalCor, 4);
	
	// Bottom goal when overlaps ball
	
	if(ball.y < 0 && (ball.x < HGROUNDWIDTH - HGOALSIZE || ball.y < -GOALDEPTH)) {
		
		spriteDraw(isoGoal, isoX(HGROUNDWIDTH<<8, 0) - 40, isoY(HGROUNDWIDTH<<8, 0) - 64, 0, isoGoalCor, 4);		
	}
	
	if(ball.y < 0 && ball.x < HGROUNDWIDTH + HGOALSIZE) {
		
		spriteDraw(isoGoal, isoX(HGROUNDWIDTH<<8, 0) - 40, isoY(HGROUNDWIDTH<<8, 0) - 64, 1, isoGoalCor, 4);
	}
	

	
	// Rain

	if(rainOn) {
	
		scr.setClip(0,0,canvasWidth,canvasHeight);
		putColor(RAINCOLOR);
	
		for(int i = 0; i < rainData.length; i++) {
		
			rainData[i][0] -= RAINFALLSPEED/2;
			rainData[i][1] += RAINFALLSPEED/4;
			rainData[i][2] -= RAINFALLSPEED;
			
			int sx = isoX(rainData[i][0],rainData[i][1]);
			int sy = isoY(rainData[i][0],rainData[i][1]) - (rainData[i][2]>>6);
							
			if(rainData[i][2] < 0) {
				
				scr.fillRect(sx - 1, sy - 1, 2, 2);
		
				rainData[i][0] = ((cameraX>>8) + 30 - RAINRANGE/2 + rnd(RAINRANGE))<<8;
				rainData[i][1] = ((cameraY>>8) - RAINRANGE/2 + rnd(RAINRANGE))<<8;
				rainData[i][2] = RAINSTARTHEIGHT << 8;
				
			} else {
							
				scr.drawLine(sx, sy, sx + 2, sy - 4);			
			}
												
		}
	}
		
	
	displayGfxMatchEvent();
	//#ifndef FEA_EA
	// HUD
	
	int x = 10, y = 10, nameMaxWidth = 65;
	
	fontInit(FONT_BLACK);
	
	fillDraw(0xdbdbdb,x,y,nameMaxWidth,printGetHeight()*2);
	fillDraw(league.userMatch[0].homeColor[2],x+nameMaxWidth - 10,y,5,printGetHeight());
	fillDraw(colorB[2],x+nameMaxWidth - 10,y + printGetHeight(),5,printGetHeight());
	fillDraw(0,x+nameMaxWidth,y,20,printGetHeight()*2);
	fillDraw(0,x,y+printGetHeight()*2,30,printGetHeight());
		
	printSetArea(x,y,nameMaxWidth,printGetHeight()*2);
	String name = league.userMatch[0].name;
	if(name.length() >= 10) {
	
		name = name.substring(0,9) + ".";
	}
	printDraw(name, 4, 0, fontPenRGB, PRINT_MASK);
	name = league.userMatch[1].name;
	if(name.length() >= 10) {
	
		name = name.substring(0,9) + ".";
	}
	printDraw(name, 4, printGetHeight(), fontPenRGB, PRINT_MASK);
	
	fontInit(FONT_WHITE);
	printSetArea(x+nameMaxWidth,y,20,printGetHeight()*2);
	printDraw(""+goalsA, 0, 0, fontPenRGB, PRINT_HCENTER | PRINT_MASK);
	printDraw(""+goalsB, 0, printGetHeight(), fontPenRGB, PRINT_HCENTER | PRINT_MASK);
	
	fontInit(timeCounter > totalMatchTime ? FONT_ORANGE : FONT_WHITE);
	printSetArea(x,y+2*printGetHeight(),30,printGetHeight());
	printDraw(""+matchClockStr((isHalfMatch ? 2700 : 0) + (timeCounter * 2700)/(totalMatchTime)), 0, 0, fontPenRGB, PRINT_HCENTER | PRINT_MASK);
	
	//com.mygdx.mongojocs.lma2007.Debug.println(""+halfMatch);
	
	
	fontInit(FONT_BLACK);
	
    printSetArea(canvasWidth/4, canvasHeight-printGetHeight()*2, canvasWidth/2, printGetHeight());
    fillDraw(LISTENER_BAR_RGB, canvasWidth/4, canvasHeight-printGetHeight()*2, canvasWidth/2, printGetHeight());
			    
	printDraw(getMenuText(MENTXT_GFXMATCHSPEED)[gfxMatchSpeed], 0, 0, fontPenRGB, PRINT_HCENTER);
	
	/*printSetArea(canvasWidth/4, canvasHeight-printGetHeight()*4, canvasWidth/2, printGetHeight());
	
    fillDraw(CONSOLE_RGB_BACKGROUND, canvasWidth/4, canvasHeight-printGetHeight()*4, canvasWidth/2, printGetHeight());
			  
    fontInit(FONT_ORANGE);
    
    if(ball.theOwner != null) {
    
    	printDraw(ball.theOwner.name, 0, 0, fontPenRGB, PRINT_HCENTER);
    	
    }*/
	//#endif
}


public void drawEntityShadow(soccerPlayer e, int size) {
				
	int x = isoX(e.fx, e.fy);
	int y = isoY(e.fx, e.fy);
			
	if(e.id < 0) {
				
		int frame = e.h / 2500;
		
		if(frame >= 4) {
		
			frame = 3;
		}
		
		imageDraw(isoBallShadow, 13 * frame, 0, 13, 15, x - 6, y - 7);
		
	} else {
		
		imageDraw(isoShadow, 0, 0, 21, 23, x - 10, y - 11);
	}

}

public void drawSoccerPlayer(soccerPlayer e, int shadowSize) {
	
		
	drawEntityShadow(e, shadowSize);
	
	e.sprite.x = isoX(e.fx, e.fy);
	e.sprite.y = isoY(e.fx, e.fy) - (e.h>>8);
				
	e.sprite.paint(scr);
	
	if(e.numFaults == 2) {
	
		fillDraw(0xffff00, e.sprite.x - 2, e.sprite.y - 40, 4, 8);
	}
	
	if(e.numFaults >= 3) {
		
		fillDraw(0xff0000, e.sprite.x - 2, e.sprite.y - 40, 4, 8);
	}
	
	if(e.injured) {
		
		fillDraw(0xffffff, e.sprite.x - 4, e.sprite.y - 40, 8, 8);
		fillDraw(0xff0000, e.sprite.x - 4, e.sprite.y - 37, 8, 2);
		fillDraw(0xff0000, e.sprite.x - 1, e.sprite.y - 40, 2, 8);
	}
	
}

public void drawFieldRect(int x, int y, int sx, int sy) {

	drawFieldLine(x, y, x+sx, y);
	drawFieldLine(x, y, x, y+sy);
	drawFieldLine(x+sx, y, x+sx, y+sy);
	drawFieldLine(x, y+sy, x+sx, y+sy);
}

public void drawFieldLine(int x1, int y1, int x2, int y2) {
	
	x1 = x1<<8;
	x2 = x2<<8;
	y1 = y1<<8;
	y2 = y2<<8;
	
	putColor(0xB5DD80);
	scr.drawLine(isoX(x1,y1),isoY(x1,y1),isoX(x2,y2),isoY(x2,y2));
}

public int isoX(int x, int y) {
	
	x -= cameraX;
	y -= cameraY;
	
	return (x>>7) - (y>>7) + canvasWidth/2;
}

public int isoY(int x, int y) {
	
	x -= cameraX;
	y -= cameraY;
	
	return (x>>8) + (y>>8) + canvasHeight/2;
}
//#endif

//#ifndef SIMPLE_2DMATCH_FIELD

//*******************
//-------------------
//Scroll - Engine v2.0 - Rev.11 (25.7.2005)
//===================
//*******************
//Dise�ado y programado por Juan Antonio Gomez Galvez (2002-2005)

//El objetivo de este engine es el soporte de un sistema de scroll de funcionamiendo standard para la mayoria de moviles.
//Para que esto sea posible, este engine se puede configurar de direfentes formas para obtener los mejores resultados
//En funcion del movil que usemos.


//SYMBOLS:
//--------
//*** SCROLL_FULL_RENDER ***
//Renderizamos siempre todos los tiles en pantalla obteniendo un scroll pixel a pixel.
//Este sistema esta orientado para los terminales iMode que sean rapidos y asi obtener un scroll con desplazamiento pixel a pixel.


//*** TILE_RENDER ***
//Renderiamos en pantalla obteniendo un movimiento de scroll a base de tiles (no pixels)
//Este sistema esta orientado para los terminales iMode que son lentos de CPU y los terminales J2ME que disponen de poca memoria
//y no se pueden permitir tener un 'doblebufer' de toda la pantalla.

//Este sistema no refresca al 100% la pantalla, por lo tanto, todo lo que se dibuje encima del area de scroll,
//luego debe ser actualizado, la forma que comunicarle esto al engine es llamando al metodo:
//scrollUpdate(x,y,w,h) con el cuadrado modificado en al area de scroll.
//scrollUpdate() forzamos a que se refresque todo el area de scroll.


//*** SCROLL_BUFFER_RENDER ***
//Renderizamos en pantalla usando un doble bufer para ganar tiempo de CPU (scroll a pixles)
//Este sistema esta orientado a los terminales J2ME que son lentos de CPU y tienen un minimo de memoria para alojar un 'doblebufer'


//Metodos:
//--------
//A continuacion se explica el ciclo de vida de un scroll:

//scrollCreate()
//Creamos un area para alojar un scroll de mapa de tiles.

//scrollInit()
//Proporcionamos toda la informacion sobre el mapa, tiles, etc.. que sera renderizado en el area de scroll.

//scrollTick() (se llama en cada iteracion)
//Indicamos las coordenas X,Y del mapa de tiles (en pixels) para que el scroll se situe en esa coordenada.

//scrollDraw() (se llama en cada iteracion)
//Renderizamos el scroll en pantalla.

//scrollBorderMask()
//Solo para iMode, al renderizar sprites es posible que queden rastros por los perimetros del scroll,
//con este metodo definimos un cuadrado para pintar de un color solido 'scrollBorderRGB', exceptuando el area de scroll.

//scrollDestroy()
//Destruimos toda la informacion y recursos del scroll


//Diferentes sistemas de movimiento de scroll:
//--------------------------------------------
//scrollTick() (Standrad)
//Situamos el scroll en la coordenda X,Y obtenida.

//scrollTick_Max()
//Situamos el scroll en la coordenada X,Y pero respetando que el mapa no se desborde en los estremos.

//scrollTick_Center()
//Proporcionamos la coordenada X,Y que queremos que aparezca justo en el centro del scroll. (ideal para que el scroll siga a un sprite)

//scrollTick_CenterMax()
//Igual que 'scrollTick_Center()' pero respetando que el mapa no se desborde en los estremos.

//scrollTick_CenterMaxSlow()
//Igual que 'scrollTick_CenterMax()' pero creando un efecto de seguimiento. (el scroll se movera suevamente hasta la nueva corrdenada)

//scrollTick_CenterMaxSemiscroll()
//Gestionamos un semiscroll que consiste en no mover el scroll hasta que la coordenada dada este proxima a los extremos,
//una vez se de este cado, el scroll se movera poco a poco hasta situar esta coordenada en el contro de la pantalla.
//Este sistema es ideal para iMode con el sistema 'TILE_RENDER'

//#ifndef SCROLL_FULL_RENDER
	//#define SCROLL_BUFFER_RENDER
//#endif

static final int tileWidth = 16;
static final int tileHeight = 16;

byte[] scrollFaseMap;
int scrollFaseWidth;
int scrollFaseHeight;
int scrollFaseX;
int scrollFaseY;

byte[] scrollFondoMap;
int scrollFondoWidth;
int scrollFondoHeight;
int scrollFondoX;
int scrollFondoY;

 
int scrollX;
int scrollY;
int scrollWidth;
int scrollHeight;

int scrollViewX;
int scrollViewY;

int scrollBaseX;
int scrollBaseY;
int scrollBaseWidth;
int scrollBaseHeight;

static final int scrollCortesX = 4;
static final int scrollCortesY = 4;

boolean scrollFirstTime = false;

byte[] scrollTileComb;
int scrollTileCombFirst;	// Primer numero de tile que forma el banco de tiles combinados
int scrollTileCombCapas;	// Numero de capas que tiene un tile combinado


int scrollAreaX;
int scrollAreaY;
int scrollAreaWidth;
int scrollAreaHeight;

int scrollBorderLeft;
int scrollBorderRight;
int scrollBorderTop;
int scrollBorderBotton;

int scrollBorderRGB = 0xff0000;

//#ifdef J2ME
	Image scrollTilesAImg;
	Image scrollTilesBImg;
	int scrollTilesBankWidth;
	int scrollTileBankSize;
//#elifdef DOJA
//#endif


//#ifdef SCROLL_BUFFER_RENDER
	Image scrollFondoImg;
	Graphics scrollFondoGfx;
//#endif


//-------------------
//scroll Create
//===================

public void scrollCreate(int x, int y, int width, int height)
{
//Anotamos el area del canvas destinado a representar el scroll de tiles
	scrollAreaX = x;
	scrollAreaY = y;
	scrollAreaWidth = width;
	scrollAreaHeight = height;
}


//-------------------
//scroll Init
//===================

//#ifdef J2ME
public void scrollInit(byte[] map, byte[] comb, int width, int height, Image imgA, Image imgB, int bankWidth, int bankHeight)
//#elifdef DOJA
//#endif
{

//Anotamos Imagen que contiene los tiles
//#ifdef J2ME
	scrollTilesAImg = imgA;
	scrollTilesBImg = imgB;
	scrollTilesBankWidth = bankWidth;
	scrollTileBankSize = bankWidth * bankHeight;
//#elifdef DOJA
//#endif


//Pillamos informacion del mapa
	//comb[0] = numero total de tiles combinados
	scrollTileCombFirst = comb[1] & 0xff;	// Numero de tile inicial para "tiles combinados"
	scrollTileCombCapas = comb[2] & 0xff;	// Numero de capas de los tiles combinados

//Anotamos variables del mapa, tama�o del mapa y tabla de tiles combinados
	scrollFaseMap = map;
	scrollFaseWidth = width;
	scrollFaseHeight = height;
	scrollTileComb = comb;

//Inicializamos variables a cero
	scrollFaseX = scrollFaseY = scrollFondoX = scrollFondoY = scrollX = scrollY = scrollBaseX = scrollBaseY = 0;
	scrollFirstTime = true;


//#ifdef SCROLL_TILE_RENDER
//Calculamos tama�o del scroll, para que sea multiplo al tama�o de los tiles
	scrollWidth = scrollAreaWidth - (scrollAreaWidth % tileWidth);
	scrollHeight = scrollAreaHeight - (scrollAreaHeight % tileHeight);
//#else
	scrollWidth = scrollAreaWidth;
	scrollHeight = scrollAreaHeight;
//#endif

//Calculamos tama�o del mapa usado como doble bufer y lo inicializamos
//#ifdef SCROLL_FULL_RENDER
	scrollFondoWidth = (scrollWidth / tileWidth) +2;
	scrollFondoHeight = (scrollHeight / tileHeight) +2;
//#elifdef SCROLL_TILE_RENDER
	scrollFondoWidth = (scrollWidth / tileWidth);
	scrollFondoHeight = (scrollHeight / tileHeight);
//#elifdef SCROLL_BUFFER_RENDER
	scrollFondoWidth = (scrollWidth / tileWidth) + ((scrollWidth % tileWidth==0)?1:2);
	scrollFondoHeight = (scrollHeight / tileHeight) + ((scrollHeight % tileHeight==0)?1:2);
//#endif


//Controlamos si la fase es mas peque�a que el area de scroll horizontalmente
	if (scrollFondoWidth > width)
	{
		scrollFondoWidth = width;
		scrollWidth = width * tileWidth;
	}

//Controlamos si la fase es mas peque�a que el area de scroll verticalmente
	if (scrollFondoHeight > height)
	{
		scrollFondoHeight = height;
		scrollHeight = height * tileHeight;
	}

//#ifndef SCROLL_FULL_RENDER
//Inicializamos mapa del doble bufer
	scrollFondoMap = new byte[scrollFondoWidth * scrollFondoHeight];
	for (int i=0 ; i<scrollFondoMap.length ; i++) {scrollFondoMap[i]=-1;}
//#endif


//Calculamos tama�o de los bordes para scroll mas peque�os que el area de scroll o el modo TILE_RENDER
	scrollBorderLeft = (scrollAreaWidth - scrollWidth) / 2;
	scrollBorderRight = scrollAreaWidth - scrollWidth - scrollBorderLeft;
	scrollBorderTop =   (scrollAreaHeight - scrollHeight) / 2;
	scrollBorderBotton = scrollAreaHeight - scrollHeight - scrollBorderTop;



//Calculamos tama�o del mapa interno para usar "semiscroll"
	scrollBaseWidth = scrollWidth / scrollCortesX;
	scrollBaseHeight = scrollHeight / scrollCortesY;


//Calculamos eje X,Y a partir de donde se dibuja el cuadrado del scroll
	scrollViewX = scrollAreaX + scrollBorderLeft;
	scrollViewY = scrollAreaY + scrollBorderTop;


//#ifdef SCROLL_BUFFER_RENDER
//Generamos imagen para doble bufer
	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			scrollFondoImg = new Image();
			scrollFondoImg._createImage(scrollFondoWidth*tileWidth, scrollFondoHeight*tileHeight);
			scrollFondoGfx = scrollFondoImg.getGraphics();
		}
	});

//#endif
}



//-------------------
//scroll Restore
//===================

public void scrollRestore()
{
//#ifdef SCROLL_BUFFER_RENDER
	scrollFondoImg = null;
	scrollFondoGfx = null;
//#endif

	scrollFaseMap=null;
	scrollFondoMap=null;
//#ifdef J2ME
	scrollTilesAImg=null;
	scrollTilesBImg=null;
//#elifdef DOJA
//#endif

}



//-------------------
//scroll Destroy
//===================

public void scrollDestroy()
{
	scrollRestore();
}



//-------------------
//scroll Tick
//===================

public void scrollTick(int X, int Y)
{
	scrollX = X % (scrollFaseWidth * tileWidth);
	scrollY = Y % (scrollFaseHeight * tileHeight);

//	scrollX = X;
//	scrollY = Y;

	scrollFaseX = (X / tileWidth);
	scrollFaseY = (Y / tileHeight);

	scrollFondoX = scrollFaseX % scrollFondoWidth;
	scrollFondoY = scrollFaseY % scrollFondoHeight;

//#ifdef SCROLL_BUFFER_RENDER
	scrollBufferUpdate();
//#endif
}


public void scrollTick_Max(int X, int Y)
{
	if (X<0) {X=0;} else if (X>=(scrollFaseWidth*tileWidth)-scrollWidth) {X=(scrollFaseWidth*tileWidth)-scrollWidth;}
	if (Y<0) {Y=0;} else if (Y>=(scrollFaseHeight*tileHeight)-scrollHeight) {Y=(scrollFaseHeight*tileHeight)-scrollHeight;}

	scrollTick(X,Y);
}


public void scrollTick_Center(int X, int Y)
{
	scrollTick(X-(scrollWidth/2), Y-(scrollHeight/2));
}


public void scrollTick_CenterMax(int X, int Y)
{
	scrollTick_Max(X-(scrollWidth/2), Y-(scrollHeight/2));
}


public void scrollTick_CenterMaxSemiscroll(int X, int Y)
{
	if (scrollFirstTime)
	{
		scrollFirstTime = false;
		scrollX = X-(scrollWidth/2); scrollBaseX = scrollX;
		scrollY = Y-(scrollHeight/2); scrollBaseY = scrollY;
	}

	if (X < scrollBaseX+(scrollBaseWidth)) {scrollBaseX -= scrollBaseWidth;}
	else
	if (X > scrollBaseX+(scrollBaseWidth*(scrollCortesX-1))) {scrollBaseX += scrollBaseWidth;}

	if (Y < scrollBaseY+(scrollBaseHeight)) {scrollBaseY -= scrollBaseHeight;}
	else
	if (Y > scrollBaseY+(scrollBaseHeight*(scrollCortesY-1))) {scrollBaseY += scrollBaseHeight;}


	if (scrollBaseX<0) {scrollBaseX=0;} else if (scrollBaseX>=(scrollFaseWidth*tileWidth)-scrollWidth) {scrollBaseX=(scrollFaseWidth*tileWidth)-scrollWidth;}
	if (scrollBaseY<0) {scrollBaseY=0;} else if (scrollBaseY>=(scrollFaseHeight*tileHeight)-scrollHeight) {scrollBaseY=(scrollFaseHeight*tileHeight)-scrollHeight;}

	scrollX -= scrollX % tileWidth;
	scrollY -= scrollY % tileHeight;
	scrollBaseX -= scrollBaseX % tileWidth;
	scrollBaseY -= scrollBaseY % tileHeight;

	if (scrollBaseX != scrollX) {scrollX += (scrollX < scrollBaseX? tileWidth:-tileWidth);}
	if (scrollBaseY != scrollY) {scrollY += (scrollY < scrollBaseY? tileHeight:-tileHeight);}

	scrollTick_Max(scrollX, scrollY);
}




public void scrollTick_CenterMaxSlow(int X, int Y)
{
	X -= (scrollWidth/2);
	Y -= (scrollHeight/2);

	if (X<0) {X=0;} else if (X>=(scrollFaseWidth*tileWidth)-scrollWidth) {X=(scrollFaseWidth*tileWidth)-scrollWidth;}
	if (Y<0) {Y=0;} else if (Y>=(scrollFaseHeight*tileHeight)-scrollHeight) {Y=(scrollFaseHeight*tileHeight)-scrollHeight;}

	while (X<0) {X+=(scrollFaseWidth*tileWidth);}
	while (Y<0) {Y+=(scrollFaseHeight*tileHeight);}

	while (X>=scrollFaseWidth*tileWidth) {X-=(scrollFaseWidth*tileWidth);}
	while (Y>=scrollFaseHeight*tileHeight) {Y-=(scrollFaseHeight*tileHeight);}

	if (scrollFirstTime || scrollX-X < -scrollWidth || scrollX-X > scrollWidth) {scrollX=X;}
	if (scrollFirstTime || scrollY-Y < -scrollHeight || scrollY-Y > scrollHeight) {scrollY=Y;}

	scrollFirstTime = false;

	if (X > scrollX) {scrollX+=((X-scrollX)>>3)+1;}	// la rotacion marca la suavidad del movimiento para X
	if (X < scrollX) {scrollX-=((scrollX-X)>>3)+1;}	// la rotacion marca la suavidad del movimiento para X

	if (Y > scrollY) {scrollY+=((Y-scrollY)>>3)+1;}	// la rotacion marca la suavidad del movimiento para Y
	if (Y < scrollY) {scrollY-=((scrollY-Y)>>3)+1;}	// la rotacion marca la suavidad del movimiento para Y

	scrollFaseX = (scrollX / tileWidth);
	scrollFaseY = (scrollY / tileHeight);

	scrollFondoX = scrollFaseX % scrollFondoWidth;
	scrollFondoY = scrollFaseY % scrollFondoHeight;

//#ifdef SCROLL_BUFFER_RENDER
	scrollBufferUpdate();
//#endif
}




public void scrollUpdate()
{
//#ifdef SCROLL_TILE_RENDER
	if (scrollFondoMap != null)
	{
		for (int i=0 ; i<scrollFondoMap.length ; i++) {scrollFondoMap[i] = -1;}
	}
//#endif
}




public void scrollUpdate(int X, int Y, int SizeX, int SizeY)
{
//#ifdef SCROLL_TILE_RENDER
	X -= scrollViewX;
	Y -= scrollViewY;

	SizeX = ((X+SizeX+(tileWidth-1))/tileWidth)-(X/=tileWidth);
	SizeY = ((Y+SizeY+(tileHeight-1))/tileHeight)-(Y/=tileHeight);

	for (int y=0 ; y<SizeY ; y++)
	{
		if ( Y+y >=0 && Y+y <scrollFondoHeight )
		{
			for (int x=0 ; x<SizeX ; x++)
			{
				if ( X+x >=0 && X+x <scrollFondoWidth ) {scrollFondoMap[((Y+y)*scrollFondoWidth)+X+x] = -1;}
			}
		}
	}
//#endif
}





//#ifdef SCROLL_BUFFER_RENDER

//-------------------
//scroll Buffer Update
//===================

public void scrollBufferUpdate()
{
	try
	{
		int FondoDir=0;
		int CorX=scrollFaseX+(scrollFondoWidth-scrollFondoX);
		int CorY=scrollFaseY+(scrollFondoHeight-scrollFondoY);

		if (CorY< 0) {CorY+=scrollFaseHeight;}
		if (CorY>=scrollFaseHeight) {CorY-=scrollFaseHeight;}

		if (CorX< 0) {CorX+=scrollFaseWidth;}
		if (CorX>=scrollFaseWidth) {CorX-=scrollFaseWidth;}


		for (int y=0 ; y<scrollFondoHeight ; y++)
		{
		if (y==scrollFondoY) {if ((CorY-=scrollFondoHeight) < 0) {CorY+=scrollFaseHeight;}}

			for (int x=0, x2=scrollFondoX, i=0 ; i<2; i++)
			{
				for (; x<x2 ; x++)
				{
					int faseIndex=(CorY*scrollFaseWidth)+CorX; if (++CorX >= scrollFaseWidth) {CorX-=scrollFaseWidth;}

					if (faseIndex >= 0 && faseIndex < scrollFaseMap.length)
					{
						if (scrollFondoMap[FondoDir++] != scrollFaseMap[faseIndex])
						{
							int tileNum=scrollFaseMap[faseIndex] & 0xff;
							scrollFondoMap[FondoDir-1] = (byte)tileNum;

							tileNum--;

				// ---------------------------------------------------------
				// Renderizamos tile en J2ME
				// =========================================================
							scrollFondoGfx.setClip(x*tileWidth ,y*tileHeight,  tileWidth,tileHeight);

							if (tileNum+1 < scrollTileCombFirst)
							{
								if (tileNum < scrollTileBankSize)
								{
									scrollFondoGfx.drawImage(scrollTilesAImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
								} else {
									tileNum -= scrollTileBankSize;
									scrollFondoGfx.drawImage(scrollTilesBImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
								}

							} else {

								int comTile = (tileNum+1 - scrollTileCombFirst)*scrollTileCombCapas;
								for (int z=0 ; z<scrollTileCombCapas ;z++)
								{
									tileNum = scrollTileComb[3+(comTile++)]-1;
									if (tileNum < 0) {continue;}
									if (tileNum < scrollTileBankSize)
									{
										scrollFondoGfx.drawImage(scrollTilesAImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
									} else {
										tileNum -= scrollTileBankSize;
										scrollFondoGfx.drawImage(scrollTilesBImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
									}
								}
							}
				// =========================================================

						}
					} else {
						FondoDir++;
					}

				}

				if (i==0)
				{
					if ((CorX-=scrollFondoWidth) < 0) {CorX+=scrollFaseWidth;}
					x2=scrollFondoWidth;
				} else {
					if (++CorY >= scrollFaseHeight) {CorY-=scrollFaseHeight;}
				}
			}
		}

	} catch (Exception e)
	{
		scrollX=0;
		scrollY=0;
	//#ifdef com.mygdx.mongojocs.lma2007.Debug
		Debug.println("scrollUpdate FAIL");
	//#endif
	}
}
//#endif



//-------------------
//scroll Draw
//===================

public void scrollDraw(Graphics scr)
{
//#ifdef SCROLL_BUFFER_RENDER
//#else


//	com.mygdx.mongojocs.lma2007.GameCanvas.gc.fillDraw(0, 0,0, 200,200);

//#ifdef FIX_TSM_DRAW_IMAGE
//#endif

	int faseMapSize = scrollFaseWidth * scrollFaseHeight;

	int faseIndex2 = ((scrollY/tileWidth)*scrollFaseWidth) + (scrollX/tileWidth);

//#ifdef SCROLL_FULL_RENDER
	int ejeX = scrollViewX -(scrollX % tileWidth);
	int ejeY = scrollViewY -(scrollY % tileHeight);
//#elifdef SCROLL_TILE_RENDER
//#endif

	int FondoDir=0;

	try {

		for (int y=0 ; y<scrollFondoHeight ; y++)
		{
			int faseIndex = faseIndex2;
			for (int x=0 ; x<scrollFondoWidth ; x++)
			{
				int tileNum = (scrollFaseMap[faseIndex++] & 0xFF);

			//#ifdef SCROLL_TILE_RENDER
			//#endif

					tileNum--;

				// ---------------------------------------------------------
				// Renderizamos tile en J2ME
				// =========================================================
				//#ifdef J2ME
					scr.setClip( scrollAreaX, scrollAreaY,  scrollAreaWidth, scrollAreaHeight);
					scr.clipRect( ejeX+(x*tileWidth), ejeY+(y*tileHeight),  tileWidth,tileHeight);

					if (tileNum+1 < scrollTileCombFirst)	// Es tile NO combiando???
					{
						if (tileNum < scrollTileBankSize)
						{
							scr.drawImage(scrollTilesAImg, ejeX+(x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), ejeY+(y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), 20);
						} else {
							tileNum -= scrollTileBankSize;
							scr.drawImage(scrollTilesBImg, ejeX+(x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), ejeY+(y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), 20);
						}

					} else {

						int comTile = (tileNum+1 - scrollTileCombFirst)*scrollTileCombCapas;
						for (int z=0 ; z<scrollTileCombCapas ; z++)
						{
							tileNum = scrollTileComb[3+(comTile++)]-1;
							if (tileNum < 0) {continue;}
							if (tileNum < scrollTileBankSize)
							{
								scr.drawImage(scrollTilesAImg, ejeX+(x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), ejeY+(y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), 20);
							} else {
								tileNum -= scrollTileBankSize;
								scr.drawImage(scrollTilesBImg, ejeX+(x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), ejeY+(y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), 20);
							}
						}
					}
				//#endif
				// =========================================================



				// ---------------------------------------------------------
				// Renderizamos tile en DOJA
				// =========================================================
				//#ifdef DOJA
				//#endif
				// =========================================================


			//#ifdef SCROLL_TILE_RENDER
			//#endif
				if (faseIndex % scrollFaseWidth == 0) {faseIndex -= scrollFaseWidth;}
			}
			faseIndex2 += scrollFaseWidth; if (faseIndex2 >= faseMapSize) {faseIndex2 -= faseMapSize;}
		}

	} catch (Exception e)
	{
	//#ifdef com.mygdx.mongojocs.lma2007.Debug
		Debug.println("scrollDraw FAIL");
	//#endif
	}

//#endif
}


//-------------------
//scroll Border Mask
//===================

public void scrollBorderMask(Graphics scr, int x, int y, int width, int height)
{
	int left = scrollViewX - x;
	int top = scrollViewY - y;
	int right = width - scrollWidth - left;
	int botton = height - scrollHeight - top;

//#ifdef J2ME
	scr.setClip( x, y, width, height);
//#endif
	GameCanvas.gc.putColor(scrollBorderRGB);

	scr.fillRect(x, y, width, top);
	scr.fillRect(x, y, left, height);
	scr.fillRect(scrollViewX + scrollWidth, y, right, height);
	scr.fillRect(x, scrollViewY + scrollHeight, width, botton);
}


//-------------------
//scroll Sprite Draw
//===================

//#ifdef J2ME
public void scrollSpriteDraw(Image img,  int x, int y,  int frame, byte[] cor)
{
	x += (-scrollX + scrollViewX);
	y += (-scrollY + scrollViewY);

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

	imageDraw(img, sourX, sourY,  sizeX, sizeY,  destX, destY,  flip,  scrollViewX ,scrollViewY, scrollWidth, scrollHeight);

	scrollUpdate(destX, destY, sizeX, sizeY);
}
//#elifdef DOJA
//#endif

//<=- <=- <=- <=- <=-


//#endif
//#endif
// end David



// *******************
// -------------------
// font - Engine
// ===================
// *******************

// ATENCION: No deninir el numero 0 (cero) a ninguna font...

static final int FONT_WHITE 	= 1;
static final int FONT_BLACK 	= 2;
static final int FONT_ORANGE 	= 3;
static final int FONT_SOFTKEY	= 4;

static final int FONT_BIG_WHITE	= 5;
static final int FONT_BIG_BLACK	= 6;
static final int FONT_BIG_GREY 	= 7;
static final int FONT_BIG_YELLOW= 8;

// ATENCION: No deninir el numero 0 (cero) a ninguna font...


byte[][] fontWhiteRgbs = {{(byte)0xF8, (byte)0xF8, (byte)0xF8}};
int[] fontBlackRGB = {0x000000};
int[] fontOrangeRGB = {0xF08018};


byte[][] fontBigWhiteRgbs = {{(byte)0xF8, (byte)0xF8, (byte)0xF8}};
int[] fontBigBlackRGB = {0x383838};
int[] fontBigGreyRGB = {0x808080};
int[] fontBigYellowRGB = {0xF0E800};


//#ifdef S30

//#elifdef S40

//#elifdef S60
	static final int FONT_HEIGHT = 12;
	static final int FONT_BIG_HEIGHT = 15;
//#elifdef S80

//#endif


byte[] fontCor;
Image fontWhiteImg;
Image fontBlackImg;
Image fontOrangeImg;

byte[] fontBigCor;
Image fontBigWhiteImg;
Image fontBigBlackImg;
Image fontBigGreyImg;
Image fontBigYellowImg;


int fontPenRGB;

int fontLast = -1;

// -------------------
// font Init
// ===================

public void fontInit(int type)
{
	if (fontLast == type) {return;} else {fontLast = type;}



//#ifdef REM_TWO_FONTS
	switch (type)
	{
	case FONT_BIG_WHITE:	type = FONT_WHITE;	break;
	case FONT_BIG_BLACK:	type = FONT_BLACK;	break;
	case FONT_BIG_GREY:		type = FONT_BLACK;	break;
	case FONT_BIG_YELLOW:	type = FONT_ORANGE;	break;
	}
//#endif



	switch (type)
	{
	case FONT_WHITE:

	//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_SMALL_FONT
		printInit_Font(fontWhiteImg, fontCor, FONT_HEIGHT, 0x20, 1);
	//#else !HIDE_SYSTEM_FONT
	//#endif
	break;


	case FONT_ORANGE:

	//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_SMALL_FONT
		printInit_Font(fontOrangeImg, fontCor, FONT_HEIGHT, 0x20, 1);
	//#else !HIDE_SYSTEM_FONT
	//#endif
	break;


	case FONT_BLACK:
	case FONT_SOFTKEY:

	//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_SMALL_FONT
		printInit_Font(fontBlackImg, fontCor, FONT_HEIGHT, 0x20, 1);
	//#else !HIDE_SYSTEM_FONT
	//#endif
	break;

//#ifndef REM_TWO_FONTS
	case FONT_BIG_WHITE:

	//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_BIG_FONT
		printInit_Font(fontBigWhiteImg, fontBigCor, FONT_BIG_HEIGHT, 0x20, 1);
	//#else !HIDE_SYSTEM_FONT
	//#endif
	break;


	case FONT_BIG_BLACK:

	//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_BIG_FONT
		printInit_Font(fontBigBlackImg, fontBigCor, FONT_BIG_HEIGHT, 0x20, 1);
	//#else !HIDE_SYSTEM_FONT
	//#endif
	break;


	case FONT_BIG_GREY:

	//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_BIG_FONT
		printInit_Font(fontBigGreyImg, fontBigCor, FONT_BIG_HEIGHT, 0x20, 1);
	//#else !HIDE_SYSTEM_FONT
	//#endif
	break;


	case FONT_BIG_YELLOW:

	//#if !HIDE_CUSTOM_FONT && !REM_CUSTOM_BIG_FONT
		printInit_Font(fontBigYellowImg, fontBigCor, FONT_BIG_HEIGHT, 0x20, 1);
	//#else !HIDE_SYSTEM_FONT
	//#endif
	break;
//#endif
	}

}



// --------------------
// bios Get Font
// ====================

//#ifndef HIDE_SYSTEM_FONT
public Font biosGetFont()
{

//#ifdef BIOS_FONT_SIZE_LARGE
	//#ifdef J2ME
		Font f = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_LARGE);
	//#elifdef DOJA
	//#endif
//#elifdef BIOS_FONT_SIZE_MEDIUM
	//#ifdef J2ME
	//	Font f = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_MEDIUM);
	//#elifdef DOJA
	//#endif
//#else
	//#ifdef J2ME
	//	Font f = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL);
	//#elifdef DOJA
	//#endif
//#endif


//#ifdef FIX_SG_BAD_FONT_WIDTH
	Image img = Image.createImage(16,16);
	Graphics gfx = img.getGraphics();
	gfx.setFont(f);
	gfx.drawString(" ", 0,0, 20);
//#endif

	return f;
}
//#endif

// <=- <=- <=- <=- <=-














// *******************
// -------------------
// print - Engine - Rev.1 (3.2.2006)
// ===================
// *******************

static final int PRINT_LEFT =	0x0000;
static final int PRINT_RIGHT =	0x0001;
static final int PRINT_HCENTER=	0x0002;
static final int PRINT_TOP =	0x0000;
static final int PRINT_BOTTOM =	0x0004;
static final int PRINT_VCENTER=	0x0008;
static final int PRINT_FIT=		0x0010;

static final int PRINT_OUTLINE=	0x1000;

static final int PRINT_MASK =	0x6000;
static final int PRINT_HMASK =	0x2000;
static final int PRINT_VMASK =	0x4000;

static final int PRINT_BREAK=	0x8000;


int printOutlineRGB = 0x205000;

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
//#ifdef FIX_FONT_HEIGHT_MINUS_2
//#else
	printFontHeight = f.getHeight();
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

	if ((mode & PRINT_BREAK) != 0)
	{
		str = printTextBreak(str, printWidth);
	}

	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - (printFontHeight*str.length) )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - (printFontHeight*str.length) );}

/*
	if ((mode & PRINT_HMASK) != 0)
	{
		str = printTextMask(str, x, printWidth);
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

//		if ( ((mode & PRINT_VMASK) == 0) || (y >= printY) && (y + (printFontHeight*str.length) <= printY + printHeight) )
		{
	
		//#ifdef DOJA
			y += printAscent;
		//#endif
	
			switch (printFontType)
			{
		//#ifndef HIDE_SYSTEM_FONT
			case 1:

				if ((mode & PRINT_MASK) == 0)
				{
					scr.setClip(0, 0, canvasWidth, canvasHeight);
				} else {
					scr.setClip(printX, printY, printWidth, printHeight);
				}
			
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
		//#endif
		
		//#ifndef HIDE_CUSTOM_FONT
			case 2:
				for (int add=0, i=0 ; i<str[line].length() ; i++)
				{
					try
					{
						char letra = str[line].charAt(i);

						if (letra == 0x20)
						{
							letra = (char) (0x61 - printFont_Min);
						} else {
							letra -= printFont_Min;
			
							if (letra >= 0 && letra < printFontCor.length/6)	// Esta dentro de las letras permitidas???
							{
								//spriteDraw(printFontImg, destX+add, destY, (int)letra , 0, printFontCor);
								if ((mode & PRINT_MASK) == 0)
								{
									spriteDraw(printFontImg, destX+add, destY, (int)letra, printFontCor, PRINT_MASK, 0, 0, canvasWidth, canvasHeight);
								} else {
									spriteDraw(printFontImg, destX+add, destY, (int)letra, printFontCor, PRINT_MASK, printX, printY, printWidth, printHeight);									
								}
								
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
// print String Width
// ===================

public int printStringWidth(String str)
{
//#ifdef HIDE_CUSTOM_FONT
//#elifdef HIDE_SYSTEM_FONT
//#else
	int size = 0;

	switch (printFontType)
	{
	case 1:
		size = printFont.stringWidth(str);
	break;

	case 2:
		for (int i=0 ; i<str.length() ; i++)
		{
			try {
				char letra = str.charAt(i);

				if (letra == 0x20) {letra = 0x61;}

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

public int spriteWidth(byte[] cor, int frame)
{
	return Math.abs(cor[(frame*6)+2]);
}


// -------------------
// print Sprite Height
// ===================

public int spriteHeight(byte[] cor, int frame)
{
	return Math.abs(cor[(frame*6)+3]);
}


// -------------------
// print Draw
// ===================

//#ifdef J2ME
public void printDraw(Image img, int x, int y, int frame, int mode, byte[] cor)
{
	spriteDraw(img, x, y, frame, cor, PRINT_FIT|mode, printX, printY, printWidth, printHeight);
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
//#ifdef HIDE_CUSTOM_FONT
//#elifdef HIDE_SYSTEM_FONT
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
//#ifdef HIDE_CUSTOM_FONT
//#elifdef HIDE_SYSTEM_FONT
//#else
	switch (printFontType)
	{
	case 1:
	return textCut(str, width, printFont);

	case 2:
	return textCut(str, width, printFontCor, printFont_Min, printFont_Spc);
	}
	return null;
//#endif
}


// -------------------
// print Text Mask
// ===================

public String printTextMask(String str, int x, int width)
{
	byte[] sizes = null;

//#ifdef HIDE_CUSTOM_FONT
//#elifdef HIDE_SYSTEM_FONT
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

public byte[] updatePrefs(byte[] bufer, String name)
{

	RecordStore rs;

	try {

		rs = RecordStore.openRecordStore(name, true);

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

/*int RND_Cont = (int)(System.currentTimeMillis()>>8 & 0x0F);
int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

int rnd = 0;
public int rnd(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	rnd = (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
	return rnd;
}*/

Random rndObject = new Random();

public int rnd(int Max)
{
	if(Max == 0) {
	
		return 0;
	}
	
	return (Math.abs(rndObject.nextInt()))%Max;
}


// -------------------
// colision
// ===================

// -------------------
// conv May2Min
// ===================

public String convMay2Min(String str)
{
	char[] strChar = str.toCharArray();
	boolean may = true;
	for (int i=0 ; i<strChar.length ; i++)
	{
		if (strChar[i] >= 65 && strChar[i] <= 90 || strChar[i] == 0xD1)
		{
			if (!may) {strChar[i] += 32;}
			may = false;
		}
		else if (strChar[i] == 32) {may = true;}
	}
	return new String(strChar);
}

// -------------------
// fade Color
// ===================

public int fadeColor(int colorStart, int colorEnd, int index)
{
   int r=((((colorEnd&0xFF0000)-(colorStart&0xFF0000))>>16)*index)>>8;
   int g=((((colorEnd&0x00FF00)-(colorStart&0x00FF00))>>8)*index)>>8;
   int b=(((colorEnd&0x0000FF)-(colorStart&0x0000FF))*index)>>8;
   return(((r<<16)+(g<<8)+b)+colorStart);
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
//#ifdef DEBUG
	try {
//#endif
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[(1024+256)];

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

//#ifdef DEBUG
	} catch(Exception e)
	{
		Debug.println("textosCreate() Exception");
		Debug.println(e.toString());
	}

	return null;
//#endif
}



// -------------------
// getMenuText
// ===================

byte[][][] convString3Byte(String[][] strIn)
{
	byte[][][] strOut = new byte[strIn.length][][];

	for (int i=0 ; i<strOut.length ; i++)
	{
		strOut[i] = new byte[strIn[i].length][];

		for (int t=0 ; t<strOut[i].length ; t++)
		{
			strOut[i][t] = new byte[strIn[i][t].length()];

			for (int c=0 ; c<strOut[i][t].length ; c++)
			{
				strOut[i][t][c] = (byte)strIn[i][t].charAt(c);
			}
		}
	}

	return strOut;
}




byte[] convString1Byte(String[][] strIn)
{
	int cnt = 1;
	int size = 0;
	for (int i=0 ; i<strIn.length ; i++)
	{
		cnt += strIn[i].length;

		for (int t=0 ; t<strIn[i].length ; t++)
		{
			size += strIn[i][t].length();
		}
	}

	menuTextIndex = new short[strIn.length * 2];
	menuTextOffs = new short[cnt];
	byte[] strOut = new byte[size];

	cnt = 0;
	size = 0;
	for (int i=0 ; i<strIn.length ; i++)
	{
		menuTextIndex[i*2] = (short)size;
		menuTextIndex[(i*2)+1] = (short)strIn[i].length;

		for (int t=0 ; t<strIn[i].length ; t++)
		{
			menuTextOffs[size++] = (short)cnt;

			for (int c=0 ; c<strIn[i][t].length() ; c++)
			{
				strOut[cnt++] = (byte)strIn[i][t].charAt(c);
			}
		}
	}
	menuTextOffs[size] = (short)cnt;

	return strOut;
}


short[] menuTextIndex;
short[] menuTextOffs;


// -------------------
// getMenuText
// ===================

String[] getMenuText(int id)
{
	//System.out.println(""+id);
	String[] str = new String[menuTextIndex[(id*2)+1]];

	int baseOffset = menuTextIndex[id*2];

	for (int i=0 ; i<str.length ; i++)
	{
		int ini = menuTextOffs[baseOffset++];
		char menuTextChar[] = new char[menuTextOffs[baseOffset] - ini];
		for(int j=0; j < menuTextChar.length; j++)
			menuTextChar[j] = (char)(menuText[ini + j] < 0 ? menuText[ini + j] + 256 : menuText[ini + j]);
		//str[i] = new String(menuText, ini, menuTextOffs[baseOffset] - ini); MONGOFIX
		str[i] = new String(menuTextChar);
	}
	//System.out.println("dsds "+str[0]);
	return str;
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
	scr.setColor(rgb);
}


// -------------------
// fill Draw
// ===================

public void fillDraw(int rgb, int x, int y, int width, int height)
{
	scr.setClip(x, y, width, height);
	putColor(rgb);
	scr.fillRect(x, y, width, height);
}


// -------------------
// rect Draw
// ===================

public void rectDraw(int rgb, int x, int y, int width, int height)
{
	scr.setClip(x, y, width, height);
	putColor(rgb);
	scr.drawRect(x, y, width-1, height-1);
}


// -------------------
// alpha Fill Draw
// ===================

public void alphaFillDraw(int argb, int x, int y, int width, int height)
{
	/*
	putColor(0x000011);
	scr.setClip(x, y, width, height);
//#ifndef VI-TSM100
	for (int i=0 ; i<height ; i+=2) {scr.fillRect(x,y+i, width, 1);}
	for (int i=0 ; i<width ; i+=2) {scr.fillRect(x+i,y, 1, height);}
//#else
	for (int i=0 ; i<height ; i+=2) {scr.fillRect(x,y+i, width, 0);}
	for (int i=0 ; i<width ; i+=2) {scr.fillRect(x+i,y, 0, height);}
//#endif
	
	if (1 == 1) return;
	*/
	
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
//#ifndef VI-TSM100
//#else
//#endif
//#endif
}


// -------------------
// image Draw
// ===================

public void imageDraw(Image Img)
{
	imageDraw(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ----------------------------------------------------------

public void imageDraw(Image Img, int X, int Y)
{
	imageDraw(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

public void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{ 
//#ifdef SI
//#endif

	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	scr.setClip(TopX, TopY, FinX, FinY);
	scr.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void imageDraw(Image img, int sourX, int sourY, int sizeX, int sizeY, int destX, int destY, int flip, int topX, int topY, int topWidth, int topHeight)
{
	if ((destX       >= topX+topWidth)
	||	(destX+sizeX < topX)
	||	(destY       >= topY+topHeight)
	||	(destY+sizeY < topY))
	{return;}

	scr.setClip (topX, topY, topWidth, topHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);

//#ifdef MIDP20
//#elifdef NOKIAUI
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
//#else
//#endif
}

// ----------------------------------------------------------

// -------------------
// cell Draw
// ===================

public void cellDraw(Image img, int x, int y, int cell, int width, int height)
{
	int lineCells = img.getWidth() / width;
	imageDraw(img, (cell%lineCells)*width, (cell/lineCells)*height, width, height, x, y);
}


// -------------------
// sprite Draw
// ===================

// Disponemos de dos metodos:
// - No indicamos "clipArea": Renderizamos el sprite teniendo como eje: (0,0) del canvas del movil.
// - Si Indicamos "clipArea": Renderizamos el sprite con la opcion de hacer un clip del area indicada, o ajustar y centrarlo seg�n este area

// PRINT_MASK: el sprite se muestra aplicando el MASK proporcionado.
// PRINT_LEFT|PRINT_HCENTER|PRINT_RIGHT: Ajustes horizontales
// PRINT_TOP|PRINT_VCENTER|PRINT_BOTTOM: Ajustes verticales
// IMPORTANTE: para que los ajustes horizontales y verticales funcionen, se ha de marcar: PRINT_FIT



public void spriteDraw(Image img, int x, int y, int frame, byte[] cor, int trozos) {
	
	for(int i = 0; i < trozos; i++) {
		
		spriteDraw(img, x, y, trozos * frame + i, cor, PRINT_MASK, 0, 0, canvasWidth, canvasHeight);
	}
	
}


public void spriteDraw(Image img, int x, int y, int frame, byte[] cor) {spriteDraw(img, x, y, frame, cor, PRINT_MASK, 0, 0, canvasWidth, canvasHeight);}
public void spriteDraw(Image img, int x, int y, int frame, byte[] cor, int mode, int clipX, int clipY, int clipWidth, int clipHeight)
{
	frame *= 6;

	int destX = cor[frame++] + x;
	int destY = cor[frame++] + y;
	int sizeX = cor[frame++];

	if (sizeX != 0)
	{
		int sizeY = cor[frame++];

	//#if MIDP20 || NOKIAUI
		int flip = 0;
		if (sizeX < 0) {sizeX *= -1; flip |= 1;}
		if (sizeY < 0) {sizeY *= -1; flip |= 2;}
	//#endif

		int sourX = cor[frame++] + 128;
		int sourY = cor[frame++] + 128;

		if ((mode & PRINT_FIT) != 0)
		{
			destX += clipX;
			destY += clipY;

			if ((mode & PRINT_HCENTER) != 0 ) {destX += ((clipWidth - sizeX)>>1);}
			else
			if ((mode & PRINT_RIGHT)   != 0 ) {destX += (clipWidth - sizeX);}

			if ((mode & PRINT_VCENTER) != 0 ) {destY += ((clipHeight - sizeY)>>1);}
			else
			if ((mode & PRINT_BOTTOM)  != 0 ) {destY += (clipHeight - sizeY);}
		}

		
		if ((mode & PRINT_MASK) != 0)
		{
			if (destX >= clipX + clipWidth || destY >= clipY + clipHeight || (destX + sizeX) <= clipX || (destY + sizeY) <= clipY) {return;}
		}

		
	//#ifdef SI-x55
	//#endif

		if ((mode & PRINT_MASK) == 0)
		{
			scr.setClip(destX, destY, sizeX, sizeY);
		} else {
			scr.setClip(clipX, clipY, clipWidth, clipHeight);
			scr.clipRect(destX, destY, sizeX, sizeY);
		}

	//#ifdef NOKIAUI
		switch (flip)
		{
		case 0:
			scr.drawImage(img, destX-sourX, destY-sourY, 20);
		break;
		
		case 1:
			dscr.drawImage(img, (destX+sourX)-img.getWidth()+sizeX, destY-sourY, 20, 0x2000);	// Flip X
		break;
		
		case 2:
			dscr.drawImage(img, destX-sourX, (destY+sourY)-img.getHeight()+sizeY, 20, 0x4000);	// Flip Y
		break;
		
		case 3:
			dscr.drawImage(img, (destX+sourX)-img.getWidth()+sizeX, (destY+sourY)-img.getHeight()+sizeY, 20, 180);	// Flip XY
		break;
		}

	//#elifdef MIDP20
	//#else
		scr.drawImage(img, destX-sourX, destY-sourY, 20);
	//#endif
	}
}





// -------------------
// arrow Draw
// ===================
/*
static final int ARROW_LEFT = 0;
static final int ARROW_RIGHT = 1;
static final int ARROW_UP = 2;
static final int ARROW_DOWN = 3;

public void arrowDraw(int x, int y, int width, int height, int dir)
{
	scr.setClip(0,0,canvasWidth,canvasHeight);

//	putColor(0xffffff); scr.fillRect(x,y,width,height);		// com.mygdx.mongojocs.lma2007.Debug: rellena cuadrado donde dibujar Arrow

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
*/


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
	byte[] bytes;

	if(file.exists())
		bytes = file.readBytes();
	else
		bytes = null;

	return bytes;
}

//#ifdef LOAD_FILE_CACHING
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// -------------------
// load Image
// ===================

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
	//#ifdef DEBUG
		Debug.println("loadImage: "+FileName+" <File not found>");		
	//#endif
		return null;
	}

//#ifdef DEBUG
	Debug.println("loadImage: "+FileName);
//#endif

	System.gc();
	return Img;
}

// <=- <=- <=- <=- <=-
















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

			if ( dat == 0x20 ) { dat=0x61; posOld = pos - 1; }

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

		if ( dat == 0x20 ) { dat=0x61; }

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

		if (letra == 0x20) {letra=0x61;}	// Espacio???

		try {
			sizes[i] = (byte)( f[ ((letra - letraMin) *6) +2 ] + letraSpc );
		} catch(Exception e) {sizes[i] = 4;}
	}

	return sizes;
}


// -------------------
// text Max Size
// ===================

public int textMaxSize(String[] str)
{
	int textoSizeX = 0;
	for (int sizeX, i=0 ; i<str.length ; i++)
	{
		sizeX = printStringWidth(str[i]); if ( textoSizeX < sizeX ) {textoSizeX = sizeX;}
	}

	return textoSizeX;
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
final static int MIN_SOUND_GAP = 500; 
static long lastSoundMillis; 


final static int SOUND_GOALSCORED = 1;
final static int SOUND_GOALRECEIVED = 2;
final static int SOUND_WINNING = 3;
final static int SOUND_LOSING = 4;
final static int SOUND_WHISTLE = 5;
final static int SOUND_3WHISTLE = 6;
//#ifdef LESS_SOUNDS
//#else
final static int SOUND_CARD = 7;
final static int SOUND_INJURY = 8;
final static int SOUND_APPLAUSE = 9;
final static int SOUND_UY = 10;
//#endif
public static void soundPlayEx(int Ary)
{
//#ifndef RES_INGAMESOUNDS

	if (lastSoundMillis + MIN_SOUND_GAP > System.currentTimeMillis()) return;
	
	gc.soundPlay(Ary, 1);
	lastSoundMillis = System.currentTimeMillis();
//#endif
}


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

		Music[] soundTracks;

		int soundOld = -1;
		int soundLoop;
		int soundForceRestart = -1;

		// -------------------
		// Sound INI
		// ===================
		
		public void soundCreate(String[] sons)
		{	
			soundTracks = new Music[sons.length];

			for (int i=0 ; i<sons.length ; i++)
			{
				soundTracks[i] = SoundCargar(sons[i]);
			}
		}

		public Music SoundCargar(String Nombre)
		{
			return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre));
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
					soundTracks[Ary].setLooping(Loop == 0);
					//VolumeControl vc = (VolumeControl) soundTracks[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(gameVolumePerCent);}
					soundTracks[Ary].play();
				}
				catch(Exception e)
				{
				//#ifdef DEBUG
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
				//#ifdef DEBUG
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
					soundTracks[soundForceRestart].play();
				}
				catch(Exception e)
				{
				//#ifdef DEBUG
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
					DeviceControl.startVibra(100, Time);
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
static final int SOFTKEY_BUY = 8;
static final int SOFTKEY_SELL = 9;
static final int SOFTKEY_CONFIRM = 10;
static final int SOFTKEY_NEGOTIATE = 11;
static final int SOFTKEY_MARK = 12;
static final int SOFTKEY_SELECT = 13;
static final int SOFTKEY_CHANGE = 14;
static final int SOFTKEY_INSERT = 15;
static final int SOFTKEY_READ = 16;
static final int SOFTKEY_START = 17;
static final int SOFTKEY_AUTO = 18;
static final int SOFTKEY_HIRE = 19;
static final int SOFTKEY_FIRE = 20;
static final int SOFTKEY_EDIT = 21;
static final int SOFTKEY_ENABLE = 22;
static final int SOFTKEY_DISABLE = 23;
static final int SOFTKEY_SEARCH = 24;
static final int SOFTKEY_DONE = 25;


static final int LISTENER_BAR_RGB = 0xFDCF56;

int listenerIdLeft;
int listenerIdRight;

int listenerDown = -1;
int listenerYadd;

int lastListenerRightId;

//boolean listenerShow;

//#ifdef J2ME
//#ifdef FULLSCREEN

//#ifdef S30
//#elifdef S40
//#elifdef S60
int listenerHeight = 2;
//#elifdef S80
//#endif

public void listenerInit(int idLeft, int idRight)
{
	if (listenerIdLeft != idLeft || listenerIdRight != idRight)
	{
		listenerIdLeft = idLeft;
		listenerIdRight = lastListenerRightId = idRight;
//		listenerShow = true;
	}
}

public void listenerInit(String strLeft, String strRight) {}

public void listenerDraw()
{
//	if (!listenerShow) {return;} else {listenerShow = false;}

	printSetArea(0, canvasHeight, canvasWidth, listenerHeight);
//	fillDraw(LISTENER_BAR_RGB, printX, printY, printWidth, printHeight);


//#ifdef REM_LISTENER_IMAGE
	fillDraw(LISTENER_BAR_RGB, printX, printY, printWidth, printHeight);
	fontInit(FONT_BLACK);
//#else
	switch (gameStatus)
	{
	case GAME_START:
	case GAME_LOGOS:
	case GAME_LOGOS + 1:
	case GAME_LEGAL:
	case GAME_LEGAL + 1:
	case GAME_LEGAL + 2:
	case GAME_CARATULA:
	case GAME_CARATULA + 1:
	case GAME_LANGUAGE:

		fillDraw(0x000000, printX, printY, printWidth, printHeight);
		fontInit(FONT_WHITE);
	break;

	case GAME_CONSOLE:
	case GAME_GFXMATCH:
		//#ifndef FEA_EA
		fillDraw(LISTENER_BAR_RGB, printX, printY, printWidth, printHeight);
		//#else
		//#endif
		fontInit(FONT_BLACK);
	break;

	default:
	// Pintamos imagen de fondo de las softkeys
		if (borderBottomImg != null)
		{
			imageDraw(borderBottomImg, 0, canvasHeight + listenerHeight - borderBottomHeight);
		}

		fontInit(FONT_SOFTKEY);
	break;
	}
//#endif


	canvasHeight += listenerHeight;
	try
	{
		//#ifndef LISTENER_SWAP
		printDraw(gameText[TEXT_SOFTKEYS][listenerIdLeft], 1, 0, fontPenRGB, PRINT_LEFT|PRINT_BOTTOM);
		printDraw(gameText[TEXT_SOFTKEYS][listenerIdRight], -1, 0, fontPenRGB, PRINT_RIGHT|PRINT_BOTTOM);
		//#else
		//#endif
	}
	catch (Exception e) {}
	canvasHeight -= listenerHeight;
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




// **************************************************************************//
// Final Clase Bios
// **************************************************************************//







// *******************
// -------------------
// menu - Engine - Rev.10 (20.9.2005)
// ===================
// *******************



// -----------------------------------------------
// Variables staticas para identificar los estados de los menus...
// ===============================================
static final int MENU_MAIN = 0;
static final int MENU_OPTIONS = 1;
static final int MENU_INGAME = 2;
static final int MENU_SHOW_TEXT = 3;
static final int MENU_SCROLL_ABOUT = 4;
static final int MENU_SCROLL_CREDITS = 5;
//static final int MENU_ASK = 6;
static final int MENU_HELP = 7;
static final int MENU_SOUND = 8;






static final int MENU_SQUAD_SELECT_TEAM =	100;
//#ifndef REM_SQUAD_TACTICS
static final int MENU_SQUAD_TACTICS =		101;
//#endif
static final int MENU_SQUAD_FORMATION =		102;
static final int MENU_SQUAD_STYLE =			103;
static final int MENU_SQUAD_INJURED =		104;
static final int MENU_SQUAD_SUSPENDED =		105;
static final int MENU_SUBSTITUTE = 			106;
static final int MENU_SQUAD_CHANGE_NEEDED = 107;
static final int MENU_SQUAD_INJURED_SUBS_NEEDED = 108;
static final int MENU_SQUAD_EDIT_NAMES =	109;
static final int MENU_SQUAD_EDIT_NAME =		110;



static final int MENU_TRAINING_LINE =		120;
static final int MENU_TRAINING_SCHEDULE =	121;
static final int MENU_TRAINING_STYLE =		122;


static final int MENU_LAPTOP =				126;
static final int MENU_READMESSAGE =			127;


static final int MENU_INFO_LEAGUE_TABLE1 =	130;
static final int MENU_INFO_LEAGUE_TABLE2 =	131;
static final int MENU_INFO_LEAGUE_GRAPHIC =	132;
static final int MENU_INFO_CHAMPIONS =		133;
static final int MENU_INFO_CLUB_DETAILS =	134;
//#ifndef REM_TOP11
static final int MENU_INFO_TOP11 =			135;
//#endif
//#ifndef REM_TOP_SCORERS
static final int MENU_INFO_TOP_SCORERS =	136;
//#endif
static final int MENU_INFO_MANAGER =		137;


static final int MENU_TRANSFERS_SELL = 			140;
static final int MENU_TRANSFERS_TEAM_SELECT =	141;
static final int MENU_TRANSFERS_PLAYER_SELECT =	142;
//#ifndef REM_TRANSFERS_FILTER
static final int MENU_TRANSFERS_FILTER_SEARCH = 143;
//#endif
static final int MENU_TRANSFERS_SCOUTS		=	144;
static final int MENU_TRANSFERS_NEGOTIATE =		146;
//#ifndef REM_FANTASY
static final int MENU_FANTASY_FILTER = 147;
static final int MENU_FANTASY_PLAYER_SELECT =	148;
//#endif

static final int MENU_FINANCE_BANK_INFO =		150;
static final int MENU_FINANCE_EMPLOYEE_LIST = 	151;
static final int MENU_FINANCE_EMPLOYEE_CHOOSE = 152;
static final int MENU_FINANCE_SPONSORS =		153;
static final int MENU_FINANCE_PRICES_SET =		154;
static final int MENU_FINANCE_ATTENDANCE =		155;


static final int MENU_STADIUM =				160;


static final int MENU_CALENDAR_MATCHES =	170;



static final int MENU_EXHI_LEAGUE_TEAM_A =	180;
static final int MENU_EXHI_LEAGUE_TEAM_B =	181;






static final int MENU_STATS_MATCH = 12;
static final int MENU_STATS_LEAGUE = 13;
static final int MENU_STATS_EUROPE = 14;

static final int MENU_BONUS_CODES = 15;
static final int MENU_PREMATCH = 22;
static final int MENU_PLAY_MATCH_RESULT = 23;



static final int MENU_BANKINFO = 26;
static final int MENU_TEXTMATCH = 27;
static final int MENU_GFXMATCH = 28;



static final int MENU_PLAY =				70;
static final int MENU_CUST_DIFFICULT =		71;
static final int MENU_CUST_MANAGER =		72;
static final int MENU_CUST_LEAGUE_TEAM =	73;
static final int MENU_CUST_EDIT_NAME =		74;
static final int MENU_CUST_NATIONALITY =	75;
static final int MENU_CUST_AGE =			76;
static final int MENU_CUST_PREFERRED_CLUB =	77;
static final int MENU_CUST_SKILLS =			78;
static final int MENU_READY_TO_PLAY =		79;

//#ifndef REM_FANTASY
static final int MENU_FANTASY_LEAGUE =		190;
static final int MENU_FANTASY_CUSTOMIZATION = 191;
static final int MENU_FANTASY_EDIT_NAME	=	192;
static final int MENU_FANTASY_CHOOSE_PLAYERS = 194;
//#endif
static final int MENU_FANTASY_COLOUR =		193;


static final int MENU_FOOTBALL_ONE =	200;

static final int MENU_LMA_TIPS = 210;

//static final int MENU_FOO = 1000;
// ===============================================


// -----------------------------------------------
// Variables staticas para identificar las acciones de los menus...
// ===============================================
static final int MENU_ACTION_NONE =			0;
static final int MENU_ACTION_BACK_HELP =	1;
static final int MENU_ACTION_BACK =			2;
static final int MENU_ACTION_CONTINUE =		3;

static final int MENU_ACTION_SHOW_HELP =	4;

static final int MENU_ACTION_EXIT_GAME =	6;
static final int MENU_ACTION_NEW_MENU = 	7;
static final int MENU_ACTION_NEW_GAME_FANTASY = 8;
static final int MENU_ACTION_VOLUME_CHG =	9;
static final int MENU_ACTION_SOUND_CHG =	10;
static final int MENU_ACTION_VIBRA_CHG =	11;
static final int MENU_ACTION_ASK =			12;
static final int MENU_ACTION_AUTOSAVE_CHG =	13;
static final int MENU_ACTION_REPLAY =		14;
static final int MENU_ACTION_REPLAY_OK =	15;
//static final int MENU_ACTION_SELECTLEAGUE =	16;
//static final int MENU_ACTION_SELECTTEAM =	17;
static final int MENU_ACTION_READMESSAGE =	18;
static final int MENU_ACTION_EXHIBITION =	19;
static final int MENU_ACTION_MAINMENU =		20;
static final int MENU_ACTION_MATCH =		21;

static final int MENU_ACTION_TRAININGSCHEDULE_LINE = 22;
static final int MENU_ACTION_TRAININGSCHEDULE_CHANGE = 23;

static final int MENU_ACTION_TRANSFERS_SELL_CONFIRM	= 30;
static final int MENU_ACTION_TRANSFERS_BUY_CONFIRM = 31;
static final int MENU_ACTION_TRANSFERS_SELECT_TEAM = 32;
static final int MENU_ACTION_EMPLOYEE_INFO = 	33;
static final int MENU_ACTION_EMPLOYEE_HIRE = 	34;
static final int MENU_ACTION_EMPLOYEE_CHOOSE =	35;
static final int MENU_ACTION_TRANSFERS_MARK = 	36;
static final int MENU_ACTION_JOURNEY_END =		40;
static final int MENU_ACTION_LAPTOP_SELL = 		41;
static final int MENU_ACTION_LAPTOP_NEGOTIATE = 42;
static final int MENU_ACTION_NEGOTIATE_CONFIRM = 43;
static final int MENU_ACTION_BUY_COMPLETE = 	44;
static final int MENU_ACTION_SHIRT_SPONSOR_ACCEPT = 45;
static final int MENU_ACTION_FENCE_SPONSOR_ACCEPT = 46;

final static int ACTION_MENUBAR =			50;

static final int MENU_ACTION_TEXTMATCHEND = 51;

static final int MENU_ACTION_MENU_EXIT =	52;

static final int MENU_ACTION_COMMAND = 		53;


static final int MENU_ACTION_FINANCE_CICLE =56;
static final int MENU_ACTION_NEGOTIATE_OPTIONS_CICLE = 57;
static final int MENU_ACTION_TEXTMATCH_CICLE = 58;
static final int MENU_ACTION_TEXTMATCH_SKIP = 59;

static final int MENU_ACTION_CUSTOMIZATION =60;
static final int MENU_ACTION_SKILLS_CICLE =	61;
static final int MENU_ACTION_TRANSFERS_SEARCH_CICLE =	62;
//#ifndef REM_FILTER
static final int MENU_ACTION_FILTER_OPTIONS_CICLE =	63;
static final int MENU_ACTION_FILTERED_SEARCH = 64;
//#endif
static final int MENU_ACTION_SCOUT_PLAYERLIST = 65;
//#ifndef REM_FANTASY
static final int MENU_ACTION_FANTASY_FILTER = 66;
static final int MENU_ACTION_FANTASY_PLAYER_CONFIRM = 67;
//#endif

static final int MENU_ACTION_PREMATCH_CICLE = 	68;

static final int MENU_ACTION_PLAY_MATCH_RESULT_CICLE =	69;
static final int MENU_ACTION_PLAY_MATCH_RESULT_ACCEPT =	70;

static final int MENU_ACTION_SQUAD_FORMATION_ACEPT = 71;

static final int MENU_ACTION_SQUAD_STYLE_ACEPT =	72;

static final int MENU_ACTION_SQUAD_TACTICS_ACEPT = 73;

static final int MENU_ACTION_TRAINING_STYLE_ACEPT =	74;

static final int MENU_ACTION_SQUAD_SELECT_TEAM_ACEPT = 75;
static final int MENU_ACTION_SQUAD_SELECT_TEAM_CANCEL = 76;

static final int MENU_ACTION_CUST_SKILLS_ACCEPT =	77;

static final int MENU_ACTION_NEW_GAME = 	78;

static final int MENU_ACTION_CALENDAR_MATCHES_CICLE = 79;
static final int MENU_ACTION_CALENDAR_MATCHES_ACCEPT = 80;

static final int MENU_ACTION_TRAINING_SCHEDULE_AUTO = 81;

static final int MENU_CUST_LEAGUE_TEAM_ACCEPT = 82;

static final int MENU_ACTION_STADIUM_UPGRADE = 83;
//#ifndef REM_BONUS_CODES
static final int MENU_ACTION_BONUS_CODES_ACCEPT = 84;
//#endif
static final int MENU_ACTION_FANTASY_CUSTOMIZATION_ACCEPT = 85;

static final int MENU_ACTION_FANTASY_CHOOSE_PLAYERS_CANCEL = 86;
static final int MENU_ACTION_FANTASY_CHOOSE_PLAYERS_ACCEPT = 87;

static final int MENU_ACTION_FANTASY_COLOUR_CICLE = 88;

static final int MENU_ACTION_LAPTOP_CICLE = 89;

static final int MENU_ACTION_SPONSOR_SWITCH_TO_EXPERT = 90;
// ===============================================


// variables para el control de la pila en la gestion de la jerarquia de menus
	int menuStackIndex = 0;
	int[][] menuStack = new int[16][2];


int menuType;

int menuActionAsk;

boolean menuExit;

Image menuImg;
Image menu2Img;
Image menuChapaImg;


// Variables usadas por 'MENU_SHOW_TEXT' como parametros para saber que textos ha de mostrar
	String[] menuShowTextTitleStr;
	String[] menuShowTextBodyStr;


// Variables para tener el archivo de textos de las ayudas de forma externa a los textos basicos
	String[][] gameTextHelp;


// Colores RGB para las primitivas de los contenidos de los menus
int menuLinesRGB = 0x636b8c;	// Color lineas Slider y linea separadora horizontal
int menuSliderRGB = 0x949cad;	// Color de la barrita interior del slider
int menuArrowRGB = 0xc6ced6;	// Color de las aspas/flechas del Slider y contador de p�ginas

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuInit(type, 0);
}


// -------------------
// menu Release
// ===================

public void menuRelease()
{
	menuRelease(false);
}

public void menuRelease(boolean noStack)
{
// Sistema Stack para los menus
	if (!noStack)
	{
//#if DEBUG && DEBUG_MENU
	Debug.println("menuRelease(type:"+menuType+" PUSH["+menuStackIndex+"])");
//#endif
		menuStack[menuStackIndex][0] = menuType;
		menuStack[menuStackIndex][1] = formListPos;
		if (++menuStackIndex >= menuStack.length) {menuStackIndex -= menuStack.length;}
	}
//#if DEBUG && DEBUG_MENU
	else {Debug.println("menuRelease(NO STACK)");}
//#endif

	formRelease();

	menuShowTextTitleStr = null;
	menuShowTextBodyStr = null;

// Limpiamos pantalla
	listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	forceRender();
}


// -------------------
// menu Init Back
// ===================

public void menuInitBack()
{
// Sistema Stack para los menus
	if ( (menuStackIndex-=2) < 0) {menuStackIndex += menuStack.length;}

//#if DEBUG && DEBUG_MENU
	Debug.println("menuInitBack(POP["+menuStackIndex+"] type:"+menuStack[menuStackIndex][0]+")");
//#endif

// Lanzamos menu anterior o salimos del motor de menus...
	if (menuStack[menuStackIndex][0] >= 0)
	{
	// Si el menuType es positivo, es un menu correcto
		menuInit(menuStack[menuStackIndex][0], menuStack[menuStackIndex][1]);
	} else {
	// Si el menuType es negativo, es un menu invalido, por lo tanto hacemos un EXIT del motor de menus...
		menuAction(MENU_ACTION_MENU_EXIT);
	}
}


// -------------------
// menu Action
// ===================



// ************************************************************************** //
// Inicio Clase com.mygdx.mongojocs.lma2007.Game
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
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int GAME_START = 0;
final static int GAME_LOGOS = 5;

final static int GAME_LEGAL = 50;

final static int GAME_LANGUAGE = 60;

final static int GAME_CARATULA = 40;

final static int GAME_MENU_START = 10;
final static int GAME_MENU_MAIN = 11;
final static int GAME_MENU_MAIN_END = 12;

final static int GAME_MENU_INGAME = 25;

final static int GAME_MENU_GAMEOVER = 30;

final static int GAME_EXIT = 35;

final static int GAME_PLAY_INIT = 20;
final static int GAME_PLAY_TICK = 21;

final static int GAME_CONSOLE = 23;
final static int GAME_GFXMATCH = 24;
// ===============================================


//#ifdef S30
//#elifdef S40
//#elifdef S60
	static final int SHIELD_WIDTH = 26;
	static final int SHIELD_HEIGHT = 29;
//#elifdef S80
//#endif




int gameAlphaExit;
int gameStatus = 0;



Image campoImg;

Image fOneMainImg;
Image fOneLogoImg;

//#ifndef REM_TRAINING_IMAGE
byte[] trainingCor;
Image trainingImg;
//#endif

//#ifndef REM_FOOTBALL_ONE
String[] footballOneStr;
//#endif


// Variables para el editor del MANAGER CUSTOMIZATION

final static int custYearBase = 1900;		// Partimo a parir de este a�o
final static int custYearDisp = 100;		// cantidad de a�os seleccionables


int custCurrentMonth;
int custCurrentAge = 80;

int custDifficulty;		// 0: Facil,  1:Dificil

int custLeague;
int custTeam;

String custName = "";
//#ifndef REM_FANTASY
String fantasyName = "";
//#endif
int custNationality;

int custPreferredClubLeague;
int custPreferredClubTeam;

int custSkillPoints = 0;
int[] custSkillNum = new int[] {5,5,5,5};


// Variables varias
boolean playgameStarted;

int exhiLeagueA;
int exhiTeamA;
int exhiLeagueB;
int exhiTeamB;

//#ifndef REM_BONUS_CODES
byte[] bonusCodes = new byte[5];
boolean[] checkBonusCodes = new boolean[5];
//#endif

//#ifndef REM_FANTASY
short[] fantasyPlayersId = new short[22];
//#endif


// variables provisionales:
int squadTactic;
int trainingStyle;


boolean caratulaShow;


// Variables varias (no hace falta almacenarlas en rms)
int xLeague;
int xTeam;
int playerSelectedToChangeName;

boolean lmaInitialized;

//#ifndef REM_FANTASY
int[] fantasyRGB = new int[] {0xFFFFFF, 0xFFED00, 0xFF9C00, 0xD00202, 0x944702, 0x760021, 0x7B109D, 0x073884, 0x1BBCF0, 0x004C14, 0x60B703, 0x535353, 0x959595, 0x000000};
byte[] fantasyRGBidx = new byte[] {3,1};
//#endif

// Variables para la gestion de los textos legales
	String[][] legalText;
	int legalIni;
	int legalLines;
	boolean legalShow;


// Variables para la gestion del multidioma
	String[][] langText;
	String langStr = "";
	int langSelected;


// -------------------
// game Create
// ===================

public void gameCreate()
{

// --------------------------------
// Cargamos Preferencias
// ================================

	loadPrefs();
	
//#ifdef DEBUG_PC_USED_MEM	
	showMemUsed("despues loadPrefs()");
//#endif	

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
			"/m0.mid",		// 0 - Musica de la caratula
			"/m1.mid",		// 1 - 
			"/m2.mid",	    // 2 - 
			"/m3.mid",		// 3 -
			"/m4.mid",		// 4 -
			"/s0.mid",		// 5 -
			"/s1.mid",		// 6 -
			"/s2.mid",		// 7 -
			"/s3.mid",		// 8 -
			"/s4.mid",		// 9 -
			"/s5.mid",		// 10 -
			});

	//#endif


	//#ifdef SG-D410
	//#endif

//#endif
// ================================

	popupCreate();

}

// -------------------
// game Tick
// ===================

public void gameTick()
{
	//HACK
	if (league != null) league.menuText = menuText;
	
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

//		logosInit(new String[] {"/codemasters", "/microjocs"}, new int[] {0xffffff, 0x000000}, 2000);

		canvasImg = loadImage("/codemasters");
		fillCanvasRGB = 0xffffff;
		forceRenderTask(RENDER_FILL_CANVAS);
		forceRender();

		waitTime(2000);

		canvasImg = loadImage("/microjocs");
		fillCanvasRGB = 0x000000;
		forceRenderTask(RENDER_FILL_CANVAS);
		forceRender();

		waitTime(2000);

		gameStatus = GAME_LANGUAGE;

//		gameStatus++;
	break;
/*
	case GAME_LOGOS + 1:

		gameStatus = GAME_LANGUAGE;
	break;
*/
// ===============================



// -------------------------------
// Seleccionamos idioma
// ===============================

	case GAME_LANGUAGE:

		//#ifdef DEBUG
		Debug.println("GAME_LANGUAGE");
		//#endif
		
//#ifndef REM_MULTILANGUAGE

	//#ifdef FULLSCREEN
		canvasHeight -= listenerHeight;		// Reservamos area para las softkeys
	//#endif

	// Limpiamos la pantalla
		fillCanvasRGB = 0x000000;
		forceRenderTask(RENDER_FILL_CANVAS);


	// MultiIdioma
		popupClear();
		langText = textosCreate(loadFile("/lang.txt"));
		popupTextStr = new String[langText.length];
		for (int i=0 ; i<popupTextStr.length ; i++)
		{
			popupTextStr[i] = langText[i][0];
		}
	
		gameText = new String[TEXT_SOFTKEYS+1][2];
		gameText[TEXT_SOFTKEYS][0] = " ";
		gameText[TEXT_SOFTKEYS][1] = langText[langSelected][1];
	
		popupInit(POPUP_LANG, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, 0, 1);
		popupListPos = langSelected;
		popupWait();

	//#ifdef FULLSCREEN
		canvasHeight += listenerHeight;		// Liberamos area para las softkeys
	//#endif

		langSelected = 3; //popupListPos;
		popupListPos = langSelected;
		langStr = langText[popupListPos][2] + "_";
//#endif

	//#ifdef FULLSCREEN
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
    //#endif

	// Limpiamos la pantalla
		fillCanvasRGB = 0x000000;
		forceRenderTask(RENDER_FILL_CANVAS);

	// Mostramos icono de "loading"
		canvasImg = loadImage("/loading");
		forceRender();

	// --------------------------------
	// Cargamos archivo de textos del idioma seleccionado
	// ================================

//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Antes de leer textos");
//#endif

	//#ifdef DEBUG
	Debug.println("textos create");
	//#endif
	
	
	//#ifndef REM_MULTILANGUAGE

// HACK  HACK   HACK  HACK   HACK  HACK   HACK  HACK  
		//gameText = textosCreate( loadFile("/EN_Textos.txt") );
		//menuText = convString1Byte(textosCreate( loadFile("/EN_menu.txt") ));
		
// HACK  HACK   HACK  HACK   HACK  HACK   HACK  HACK   HACK  HACK  


		gameText = textosCreate( loadFile("/"+langStr+"Textos.txt") );
		menuText = convString1Byte(textosCreate( loadFile("/"+langStr+"menu.txt") ));
	//#else
		//gameText = textosCreate( loadFile("/Textos.txt") );
		//menuText = convString1Byte(textosCreate( loadFile("/menu.txt") ));
		
		//#ifdef FEA_MATCH_REPORT
		//reportText = textosCreate( loadFile("/report.txt") );		
		//#endif
	//#endif

		
		
	//for(int i = 0; i < menuTextIndex.length/2;i++)	
	//	System.out.println(i+": "+getMenuText(i)[0]);
		
		
		
		
//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Despues de leer textos");
//#endif

		if (custName.length() == 0) {custName = gameText[TEXT_PLAYER_NAME][0];}
		//#ifndef REM_FANTASY
		if (fantasyName.length() == 0) {fantasyName = gameText[TEXT_FANTASY_TEAM_NAME][0];}
		//#endif

	// ================================

		biosPauseEnabled = true;

		gameStatus = GAME_LEGAL;
	break;

// ===============================



// -------------------------------
// Mostramos Textos legales
// ===============================
	case GAME_LEGAL:

		String[][] legalText2 = textosCreate(loadFile("/"+langStr+"legal.txt"));

	// Cortamos los textos legales segun el canvas
		fontInit(FONT_WHITE);
		legalText = new String[legalText2[0].length][];
		for (int i=0 ; i<legalText.length ; i++)
		{
			legalText[i] = printTextBreak(legalText2[0][i], canvasWidth);
		}

		legalIni = 0;

	case GAME_LEGAL + 1:

	// Calculamos el maximo de lineas que podemos mostrar en este canvas
		legalLines = 0;
//		int linesMax = 4;
		int linesMax = canvasHeight / printFontHeight;
		for (int i=legalIni ; i<legalText.length ; i++)
		{
			linesMax -= legalText[i].length;
			if (linesMax < 0) {break;}
			legalLines++;
		}

		legalShow = true;
		gameStatus++;
	break;

	case GAME_LEGAL + 2:

		waitTime(2500);

		legalIni += legalLines;

		if (legalIni < legalText.length)
		{
			gameStatus--;
		} else {

		//#ifdef FULLSCREEN
			canvasHeight -= listenerHeight;		// Reservamos area para las softkeys
		//#endif

			gameStatus = GAME_CARATULA;
		}
	break;
// ===============================



// -------------------------------
// Mostramos Caratula
// ===============================

	case GAME_CARATULA:

	// Limpiamos la pantalla
		fillCanvasRGB = 0x000000;
		forceRenderTask(RENDER_FILL_CANVAS);

	// Mostramos icono de "loading"
		canvasImg = loadImage("/loading");
		forceRender();
		forceRenderTask(RENDER_LOADING);

//#ifndef REM_CARATULA
	//#ifdef REM_MULTILANGUAGE
    //#else
		menuImg = loadImage("/caratulaA");
		menuChapaImg = loadImage("/"+langStr+"chapa");
		menu2Img = loadImage("/caratulaB");
    //#endif
		caratulaShow = true;
//#endif
		//System.out.println("ZNR 1");
	//#ifndef PLAYER_NONE
		//#ifdef REM_VOLUME
		//#else
			//popupInitAsk(new String[] {getMenuText(MENTXT_GAME_WITH_SOUND)[0], " ", "< "+getMenuText(MENTXT_OPTI_VOLUME)[gameVolume]+" >"}, SOFTKEY_NO, SOFTKEY_YES);
			//popupType = POPUP_VOLUME;
	    //#endif
		//System.out.println("ZNR 2");
		//popupWait();
		gameSound = true; //popupResult;
		gameVolumePerCent = new int[] { 25, 50, 100}[gameVolume];
//		soundPlay(MUSIC_CLIN, 1);
	//#endif

		soundPlay(0,0);

//#ifndef REM_CARATULA
		caratulaShow = true;

		listenerInit(SOFTKEY_NONE, SOFTKEY_START);
		gameStatus++;
	break;

	case GAME_CARATULA + 1:

		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			menuImg = null;
			menuChapaImg = null;
			menu2Img = null;
			gameStatus = GAME_MENU_START;
		}
	break;
//#else
//#endif
// ===============================


// -------------------------------
// Gestor de menus del source base
// ===============================
	case GAME_MENU_START:

// Mostramos Loading...
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	// Limpiamos la pantalla
		fillCanvasRGB = 0x000000;
		forceRenderTask(RENDER_FILL_CANVAS);
	// Mostramos icono de "loading"
		canvasImg = loadImage("/loading");
		forceRender();
		forceRenderTask(RENDER_LOADING);



// Inicializamos LMA Engine, si es la primera ejecucion
		if (!lmaInitialized)
		{
			lmaInitialized = true;

			//#ifdef DEBUG_PC_USED_MEM
				showMemUsed("Antes lmaInit()");
			//#endif
			//#ifdef DEBUG_TIME_PASSED
				//showTimePassed("lmaInit() start");
			//#endif

			lmaInit();

			//#ifdef DEBUG_PC_USED_MEM
				showMemUsed("Despues lmaInit()");
			//#endif
			//#ifdef DEBUG_TIME_PASSED
				//showTimePassed("lmaInit() end");
			//#endif

			league.gameText = gameText;
			league.menuText = menuText;
		
			//#ifdef DEBUG_TIME_PASSED
				showTimePassed("loadGame() start");
			//#endif

			loadGame();

			//#ifdef DEBUG_TIME_PASSED
				showTimePassed("loadGame() start");
			//#endif
		}



// Eliminamos recursos que no nos hacen falta


	//#ifndef REM_GC_GRAPHICS
		//#ifndef REM_FLECHAS_IMAGE
			flechasImg = null;
		//#endif
			menuIconsImg = null;
			menuIconsImg1 = null;
		//#ifndef OLD_MENU_BAR
			menuIconsImg2 = null;
		//#endif
	//#endif


// Cargamos Recursos necesarios

	//#ifdef DEBUG_PC_USED_MEM	
		showMemUsed("Antes menu Gfx");
	//#endif

	//#ifndef REM_BACKGROUND_IMAGE
		if (backgroundImg == null)	backgroundImg = loadImage("/back1");
	//#endif
	
		if (formItemsCor == null)	formItemsCor = loadFile("/formItems.cor");
		if (formItemsImg == null)	formItemsImg = loadImage("/formItems");		
	
	//#ifdef REM_CAMPO_IMAGE
	//#else
		if (campoImg == null)	campoImg = loadImage("/campo");
	//#endif

		borderTypeMenu = true;
		
    //#ifndef REM_BORDER_TOP_IMAGE 
		borderTopImg = loadImage("/borderMenuTop");
	//#endif
		
	//#ifndef REM_LISTENER_IMAGE
		borderBottomImg = loadImage("/borderMenuBottom");
		borderBottomHeight = borderBottomImg.getHeight();
	//#else
	//#endif
	
	//formCreate()
		FORM_SLIDER_WIDTH = spriteWidth(formItemsCor, ITEM_SLIDER_ARROWS);
		FORM_OPTION_BAR_HEIGH = spriteHeight(formItemsCor, ITEM_OPTION_BAR);
	
	//#ifdef DEBUG_PC_USED_MEM	
		showMemUsed("Despues menu Gfx");
	//#endif


		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:

		menuInit(MENU_MAIN);

		gameStatus = GAME_MENU_MAIN_END;
	break;


	case GAME_MENU_MAIN_END:

// Mostramos Loading...
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	// Limpiamos la pantalla
		fillCanvasRGB = 0x000000;
		forceRenderTask(RENDER_FILL_CANVAS);
	// Mostramos icono de "loading"
		canvasImg = loadImage("/loading");
		forceRender();



// Inicializamos los bordes superiores e inferiores
		borderTypeMenu = false;
	//#ifndef REM_BORDER_TOP_IMAGE	
		borderTopImg = loadImage("/borderTop");
	//#endif
	//#ifndef REM_LISTENER_IMAGE
		borderBottomImg = loadImage("/borderBottom");
		borderBottomHeight = borderBottomImg.getHeight();
	//#else
		borderBottomHeight = listenerHeight;
	//#endif

	//#ifndef REM_TEAMSHIELDS
		shieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.userTeam.flagColor);
	//#endif

		if (menuIconsImg1 == null)	menuIconsImg1 = loadImage("/iconos");
	//#ifndef OLD_MENU_BAR
		if (menuIconsImg2 == null)	menuIconsImg2 = loadImage("/iconosx");
	//#endif


	//#ifndef REM_FLECHAS_IMAGE
		if (flechasCor == null)	flechasCor = loadFile("/flechas.cor");
		if (flechasImg == null)	flechasImg = loadImage("/flechas");
	//#endif

		financesUpdateValues();


		gameStatus = GAME_PLAY_INIT;
	break;


/*
	case GAME_MENU_INGAME:

		menuInit(MENU_INGAME);
		gameStatus = GAME_PLAY_TICK;
	break;
*/
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
	case GAME_PLAY_INIT:

		//#ifdef DEBUG
		System.out.println("GAME_PLAY_INIT");
		//#endif
// -----------
// Si el juego tiene musica durante el juego:
//		soundPlay(1,0);
// ===========
		
	// Mostramos el texto de 'menu' en las softKeys
		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);

		gameStatus++;
	break;

	case GAME_PLAY_TICK:

	//#ifdef CHEATS
	//	cheats();
	//#endif

		//#ifdef DEBUG
		System.out.println("GAME_PLAY_TICK"+exhibitionFlag);
		//#endif
		if (!exhibitionFlag)
		{
			biosStatus = BIOS_MENUBAR;
			gameStatus--;
			menuBarShow = true;
			menuBarRefresh = true;
			menuBarEngine = true;
			league.preMatch(); //ZNR CUIDADO
		}
		
	break;
// ===============================
	
//#ifndef REM_TEXTMATCH	
	case GAME_CONSOLE:
		consoleTick();
	break;
//#endif	
	
	
//#ifndef REM_2DMATCH	
	case GAME_GFXMATCH:
		 boolean exit = false;
		
		 exit = gfxMatchTick();
		 if (gfxMatchSpeed > 0) exit |= gfxMatchTick();
		 if (gfxMatchSpeed > 1) 
		 {
			 exit |= gfxMatchTick();
			 gfxMatchCamTick();
			 exit |= gfxMatchTick();
			 exit |= gfxMatchTick();
		 }
		 
		 if (substitution)
		 {
			 substitution = false;
			 menuStackIndex = 1;
	    	 menuStack[0][0] = -1;
	    	 menuInit(MENU_SUBSTITUTE);
		 }
		 
		 gfxMatchShow = true;
		 
		 gfxMatchCamTick();
		 
			if (keyMenu > 0 && lastKeyMenu == 0)
			{				
				menuInit(MENU_TEXTMATCH);
				
				//Hago cambio siempre
				changeFormation = true;
            	return;
			}		
			
			if(keyX > 0 && lastKeyX == 0 && gfxMatchSpeed < 2) {
				
				gfxMatchSpeed += 1;
			}
			
			if(keyX < 0 && lastKeyX == 0 && gfxMatchSpeed > 0) {
				
				gfxMatchSpeed -= 1;
			}
			
			if (exit) 
			{								
				menuAction(MENU_ACTION_TEXTMATCHEND);
			}
	break;
//#endif	
	}
}


//#ifndef REM_2DMATCH
boolean substitution;

public void gfxMatchSkip()
{
	//league.rawPlayerNames = loadFile("/plyn");
	//league.rawPlayerStats = loadFile("/plys");
	
	league.reMatchPlay(league.userMatch[0], league.userMatch[1], 0, goalsA, goalsB);
	
	//Goles
	league.countedGoals[0] = goalsA;
	league.countedGoals[1] = goalsB;
	
	goalsA = league.userMatch[0].matchGoals;
	goalsB = league.userMatch[1].matchGoals;
	
	//Expulsados
	for(int i = matchSuspendedCount ; i < league.matchSuspendedCount;i++)
	{
		//NO controlo fuera de rango
		int exId = league.extendedPlayer(league.matchSuspended[i]);
		league.userPlayerStats[exId][League.SANCTION_JOURNEYS] = 2;
	}
	
	// Lesionados
	for(int i = matchInjuredCount ; i < league.matchInjuredCount;i++)
	{
		league.injurePlayer(league.matchInjured[i]);		
		//int exId = league.extendedPlayer(league.matchInjured[i]);
		//league.userPlayerStats[exId][com.mygdx.mongojocs.lma2007.League.INJURY_JOURNEYS] = 2 + league.rand()%4;
	}
	
	league.recordOwnGoals = true;
}

//#endif

// -------------------
// game Refresh :: Este metodo se llama tras regresar de un 'incomming call' y debemos restaurar el juego.
// ===================

public void gameRefresh()
{
	switch (gameStatus)
	{
	case GAME_CARATULA + 1:

		caratulaShow = true;
	break;
	}
}


// -------------------
// game Draw
// ===================

public void gameDraw()
{
// SI estamos en modo pause, pintamos pantalla de pausa
	if (biosStatus == BIOS_PAUSE)
	{
		fillDraw(0x000000, 0, 0, canvasWidth, canvasHeight);
		printSetArea(0, 0, canvasWidth, canvasHeight);
		fontInit(FONT_WHITE);
		printDraw(gameText[TEXT_GAME_IN_PAUSE], 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

//		listenerShow = true;
		listenerDraw();
		return;
	}


// Gestiones internes de la BIOS
	if (canvasImg!=null) { imageDraw(canvasImg); canvasImg=null; System.gc(); }


// Pintamos caratula
	if (caratulaShow)
	{
		caratulaShow = false;

		if (menuImg != null)
		{
		//#ifdef FEA_CENTER_COVER
	    //#else
			imageDraw(menuImg, 0, 0);
	    //#endif

		//#ifndef REM_MULTILANGUAGE
			imageDraw(menuChapaImg, 0, menuImg.getHeight());
			if (menu2Img != null)
			{
				imageDraw(menu2Img, 0, menuImg.getHeight() + menuChapaImg.getHeight());
			}
		//#endif
		}
	}


// Pintamos textos legales
	if (legalShow)
	{
		legalShow = false;

	// Limpiamos el canvas
		fillDraw(0x000000, 0, 0, canvasWidth, canvasHeight);

		fontInit(FONT_WHITE);

	// Calculamos cuantas lineas vamos a imprimir
		int lines = 0; for (int i=0 ; i<legalLines ; i++) {lines += legalText[legalIni + i].length;}

	// Pintamos textos legales
		printSetArea(0, (canvasHeight-(lines * printFontHeight))/2, canvasWidth, (lines * printFontHeight));
		int cursor = 0;
		for (int i=0 ; i<legalLines ; i++)
		{
			printDraw(legalText[legalIni + i], 0, cursor, fontPenRGB, PRINT_HCENTER);
			cursor += legalText[legalIni + i].length * printFontHeight;
		}
	}


// Renderizamos menu de barra de opciones
	menuBarDraw();


// Renderizamos el fadeOut al quitar el juego
	if (gameStatus == GAME_EXIT+1) {alphaFillDraw(0x44000000, 0,0, canvasWidth, canvasHeight);}

	
// Renderizamos consola de texto del partido
	
//#ifndef REM_TEXTMATCH
	consoleDraw();
//#endif	

// Renderizamos partido en isometrico
//#ifndef REM_2DMATCH
	gfxMatchDraw();
//#endif
	
// Renderizamos gestor de menus
	formDraw();


// Renderizamos Titulo
	titleBarDraw();
	
		
// Renderizamos gestor de popups
	popupDraw();


// Renderizamos textos de las softkeys
	listenerDraw();


//#ifdef DEBUG_SHOW_FREE_MEM
	/*fontInit(FONT_WHITE);
	printSetArea(0, 0, canvasWidth, printFontHeight);
	System.gc();
// 	String mem = ""+((int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
 	String mem = ""+(int)(Runtime.getRuntime().freeMemory());
	fillDraw(0x000000, printX, printY, printStringWidth(mem)+2, printHeight+2);
	printDraw(mem, 1, 1, fontPenRGB, PRINT_LEFT);*/
//#endif
	
}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// forceRenderTask - Engine
// ===================
// *******************

static final int RENDER_FILL_CANVAS = 0;
static final int RENDER_LOADING = 1;

int fillCanvasRGB;

// -------------------
// force Render Draw (TASK) :: Tareas que se ejecutan dentro de 'paint()' lanzadas desde la l�gica con 'forceRenderTask(id)'
// ===================

public void forceRenderDraw()
{
	switch (forceRenderType)
	{
	case RENDER_FILL_CANVAS:

		fillDraw(fillCanvasRGB, 0, 0, canvasWidth, canvasHeight);
	break;


	case RENDER_LOADING:

		loadingDraw();
	break;
	}
}

// <=- <=- <=- <=- <=-


public void loadingDraw()
{
	fontInit(FONT_WHITE);
	printSetArea(0, 0, canvasWidth, canvasHeight);
//#ifdef S30
//#else
	printDraw(gameText[TEXT_LOADING][0], 0, (canvasHeight/3)*2, fontPenRGB, PRINT_HCENTER);
//#endif
}



// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{

// Cargamos un byte[] con las ultimas prefs grabadas

	byte[] prefsData = updatePrefs(null, "prefs");	// Recuperamos byte[] almacenado la ultima vez

// Si es null es que nunca se han grabado prefs, por lo cual INICIALIZAMOS las prefs del juego
	if (prefsData == null)
	{
	// Inicializamos preferencias ya que NO estaban grabadas
		savePrefs();
		return;
	}

// Actualizamos las variables del juego segun las prefs leidas
	try {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(prefsData, 0, prefsData.length));

		byte checksum = 0; for (int i=1 ; i<prefsData.length ; i++) {checksum += prefsData[i];}
		if (checksum != dis.readByte())
		{
		// Si checksum error, entonces prefs corruptas, inicializarlas de cero.
		//#ifdef DEBUG
			Debug.println("* * * Prefs corruptas, Inicializando de cero...");
		//#endif
			savePrefs();
			return;
		}

// -------------------------------
// Comenzamos a leer las variables de las prefs
// ===============================

		gameSound = dis.readByte() != 0;
		gameAutosave = dis.readByte() != 0;
		gameVolume = dis.readInt();
		gameVolumePerCent = dis.readInt();
		gameVibra = dis.readByte() != 0;
		langSelected = dis.readByte();

		//#ifndef REM_BONUS_CODES
		for (int i=0 ; i<bonusCodes.length ; i++) {bonusCodes[i] = dis.readByte(); checkBonusCodes[i] = dis.readByte() != 0;}
		//#endif

// ===============================

	} catch(Exception e)
	{
	//#ifdef DEBUG
		Debug.println("* * * loadPrefs() Error cargando variables * * ");
		Debug.println(e.toString());
		return;
	//#endif
	}

//#ifdef DEBUG
	Debug.println("Prefs leidas correctamente");
//#endif
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	try {
	// Ponemos las varibles del juego a salvar como prefs.

		dos.writeByte(0);

// -------------------------------
// Comenzamos a guardar las variables de las prefs
// ===============================

		dos.writeByte(gameSound? 1:0);
		dos.writeByte(gameAutosave? 1:0);
		dos.writeInt(gameVolume);
		dos.writeInt(gameVolumePerCent);
		dos.writeByte(gameVibra? 1:0);
		dos.writeByte(langSelected);
		//#ifndef REM_BONUS_CODES
		for (int i=0 ; i<bonusCodes.length ; i++) {dos.writeByte(bonusCodes[i]); dos.writeByte(checkBonusCodes[i]? 1:0);}
		//#endif
// ===============================

	// Almacenamos las prefs
		byte[] prefsData = baos.toByteArray();
		byte checksum = 0; for (int i=1 ; i<prefsData.length ; i++) {checksum += prefsData[i];}
		prefsData[0] = checksum;
		updatePrefs(prefsData, "prefs");		// Almacenamos byte[]

	} catch(Exception e)
	{
	//#ifdef DEBUG
		Debug.println("Error guardando prefs");
		Debug.println(e.toString());
		return;
	//#endif
	}

//#ifdef DEBUG
	Debug.println("Preferencias guardadas correctamente");
//#endif
}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// save com.mygdx.mongojocs.lma2007.Game - Engine
// ===================
// *******************

// -------------------
// savegame INI
// ===================

public void loadGame()
{
	//com.mygdx.mongojocs.lma2007.Debug.println("######## load");
	//#ifdef DEBUG
	znrloadreset();
	//#endif
// Cargamos un byte[] con las ultimas savegame grabadas

	byte[] prefsData = updatePrefs(null, "game");	// Recuperamos byte[] almacenado la ultima vez

// Si es null es que nunca se han grabado prefs, por lo cual INICIALIZAMOS las prefs del juego
	if (prefsData == null)
	{
	// Inicializamos preferencias ya que NO estaban grabadas
		saveGame();
		return;
	}

// Actualizamos las variables del juego segun las prefs leidas
	try {
		ByteArrayInputStream bais = new ByteArrayInputStream(prefsData, 0, prefsData.length);
		DataInputStream dis = new DataInputStream(bais);

		byte checksum = 0; for (int i=1 ; i<prefsData.length ; i++) {checksum += prefsData[i];}
		//#ifdef DEBUG
		znrread("nose", 1);
		//#endif
		if (checksum != dis.readByte())
		{
		// Si checksum error, entonces prefs corruptas, inicializarlas de cero.
		//#ifdef DEBUG
			Debug.println("* * * savegame corruptas, Inicializando de cero...");
		//#endif
			saveGame();
			return;
		}

// -------------------------------
// Comenzamos a leer las variables de las prefs
// ===============================
		//#ifdef DEBUG
		znrread("nose", 13);
		//#endif

		playgameStarted = dis.readByte() != 0;
		//#ifndef REM_FANTASY
		fantasyName = dis.readUTF();
		//#endif
		int flagColor0 = dis.readInt();
		int flagColor1 = dis.readInt();
		
		gameMode = dis.readInt();
		//#ifdef DEBUG
		znrread("autoManagement", autoManagement.length);
		//#endif
		for (int i=0 ; i<autoManagement.length ; i++) {autoManagement[i] = dis.readByte() != 0;}

		//#ifdef DEBUG
		znrread("cust", 8);
		//#endif
		custCurrentMonth = dis.readInt();
		custCurrentAge = dis.readInt();
		//#ifdef DEBUG
		znrread("nose", 4);
		//#endif
		custDifficulty = dis.readInt();
		//#ifdef DEBUG
		znrread("nose", 8);
		//#endif
		custLeague = dis.readInt();
		custTeam = dis.readInt();
		//#ifdef DEBUG
		znrread("name", 0);
		//#endif
		custName = dis.readUTF();
		//#ifdef DEBUG
		znrread("nose", 4);
		//#endif
		custNationality = dis.readInt();
		//#ifdef DEBUG
		znrread("preferred", 8);
		//#endif
		custPreferredClubLeague = dis.readInt();
		custPreferredClubTeam = dis.readInt();
		//#ifdef DEBUG
		znrread("nose", 4*5);
		//#endif
		custSkillPoints = dis.readInt();
		
		custSkillNum[0] = dis.readInt();
		custSkillNum[1] = dis.readInt();
		custSkillNum[2] = dis.readInt();
		custSkillNum[3] = dis.readInt();

		preferedClubId = dis.readInt();


		exhiLeagueA = dis.readInt();
		exhiTeamA = dis.readInt();
		exhiLeagueB = dis.readInt();
		exhiTeamB = dis.readInt();


//#ifdef DEBUG
	Debug.println ("---------------------> START LOAD savegame");
//#endif


		// VARIABLES LIGA
		//#ifdef DEBUG
		znrread("leaguevars", 4*5);
		//#endif
		league.season = dis.readInt();
		league.currentJourney = dis.readInt();
		league.journeyType = dis.readInt();
		league.lastJourneyType = dis.readInt();
		league.currentWeek = dis.readInt();
		league.currentYear = dis.readInt();
		league.champEnded = dis.readBoolean();
		//#ifdef DEBUG
		znrread("nose", 4*3);
		//#endif
		league.playerSeasonGoal = (byte)dis.readInt();
		league.userLeague = dis.readInt();

		league.userTeam = league.getTeamFromGlobalIdx(dis.readInt());
		//#ifndef REM_FANTASY
		league.userTeam.name = fantasyName;
		//#endif
		//#ifndef REM_TEAMCOLORS
		league.userTeam.flagColor[0] = flagColor0;
		league.userTeam.flagColor[1] = flagColor1;
		//#endif

		trainingStyle = dis.readInt();
		squadStyle = dis.readInt();
		squadTactic = dis.readInt();
		
		//#ifdef DEBUG
		Debug.println("IDX:    "+league.userTeam.globalIdx+""+league.userTeam.name);
		//#endif
		//#ifdef DEBUG
		znrread("champ", 8+1);
		//#endif
		league.playingChamp = dis.readByte() != 0;
		league.champRound = dis.readInt();

		league.champTeams = new Team[dis.readInt()];
		for (int i = 0; i < league.champTeams.length; i++) 
		{
			//#ifdef DEBUG
			znrread("champteams", 4*2);
			//#endif
			league.champTeams[i] = league.getTeamFromGlobalIdx(dis.readInt());
			league.champTeams[i].europeanMatchGoals = dis.readByte();
		}
		
//#ifdef DEBUG
	Debug.println ("league.season: "+league.season);
	Debug.println ("league.currentJourney: "+league.currentJourney);
	Debug.println ("league.lastJourneyType: "+league.lastJourneyType);
	Debug.println ("league.currentWeek: "+league.currentWeek);
	Debug.println ("league.currentYear: "+league.currentYear);
	Debug.println ("league.playerSeasonGoal: "+league.playerSeasonGoal);
	Debug.println ("league.userLeague: "+league.userLeague);
	Debug.println ("league.userTeam: "+league.userTeam);
	Debug.println ("league.playingChamp: "+league.playingChamp);
	Debug.println ("league.champRound: "+league.champRound);
	Debug.println ("league.champTeams(length): "+league.champTeams.length);
//#endif

//#ifndef REM_FANTASY
	if (gameMode == FANTASY)
	{
		while(league.userTeam.playerCount > 0) {
			
			league.userTeam.removePlayer(league.userTeam.playerIds[0]);	
			//#ifdef DEBUG
			Debug.println("###: "+league.userTeam.playerCount);
			//#endif
		}
	}
//#endif
	
	
	
	//#ifdef DEBUG
	znrread("transfersRecord.length", 4);
	//#endif
	league.transfersRecord = new int[dis.readInt()][2];
	
	//#ifdef DEBUG
	znrread("transfersRecord", league.transfersRecord.length * 8);
	//#endif
	
	for(int i = 0; i < league.transfersRecord.length; i++) {
		
		league.transfersRecord[i][0] = dis.readInt();
		league.transfersRecord[i][1] = dis.readInt();
		
		// Update league with completed transfers
		Team to, tf;
		//if league.extendedPlayer(league.transfersRecord[i][0]) == -1)
		to = league.getTeamWherePlays((short)league.transfersRecord[i][0]);
		tf = league.getTeamFromGlobalIdx(league.transfersRecord[i][1]);
		if (to != null && to.globalIdx != tf.globalIdx)
		{
			to.removePlayer(league.transfersRecord[i][0]);
			tf.addPlayer(league.transfersRecord[i][0]);
			//com.mygdx.mongojocs.lma2007.Debug.println(to.name+" a "+tf.name+" Count:"+tf.playerCount);
		}
	}
	


		league.userTeam.playerCount = 0;
		// alineacion equipo user
		for (int i = 0; i < Team.MAXPLAYERS; i++) 
		{
			//#ifdef DEBUG
			znrread("team", 4);
			//#endif
			league.userTeam.playerIds[i] = (short)dis.readInt();
			if (league.userTeam.playerIds[i] != -1) league.userTeam.playerCount++; 
		}
		//#ifdef DEBUG
		System.out.println("PLAYERCOUNT: "+league.userTeam.playerCount);
		//#endif


	
		// stats jugadores del user
		for (int i = 0; i < Team.MAXPLAYERS; i++) 
		{
			int p = league.userTeam.playerIds[i];
			//#ifdef DEBUG
			znrread("player stats", 4);
			//#endif
			if (p >= 0) 
			{
				league.userPlayerStats[i][league.EX_PLAYERID] = p;

				league.playerSetSkill(p, 0, dis.readByte());
				league.playerSetSkill(p, 1, dis.readByte());
				league.playerSetSkill(p, 2, dis.readByte());
				league.playerSetSkill(p, 3, dis.readByte());
				league.userPlayerStats[i][league.SANCTION_JOURNEYS] = dis.readByte();
				league.userPlayerStats[i][league.INJURY_JOURNEYS] = dis.readByte();
				league.playerSetName(p, dis.readUTF());				
			} else {
				league.userPlayerStats[i][league.EX_PLAYERID] = -1;
				/*dis.readByte();
				dis.readByte();
				dis.readByte();
				dis.readByte();*/
			}
		}

		
		
		//#ifdef DEBUG
		znrread("nose", 2);
		//#endif
		// jornadas liga
           int l1 = dis.readByte();
           int l2 = dis.readByte();
           league.leagueJourneys = new Team[l1][];
           for (int i = 0; i < league. leagueJourneys .length; i++)
           {
               league.leagueJourneys[i] = new Team[l2];
               for (int j = 0; j < league.leagueJourneys[0].length; j++)
               {
            	   //#ifdef DEBUG
            	   znrread("league", 1);
            	   //#endif
                   league.leagueJourneys[i][j] = league.getTeamFromGlobalIdx(dis.readByte());
               }
           } 
        //#ifdef DEBUG
        znrread("leaguestats", 6+2+2);
        //#endif
		// stats equipos en la liga
		for (int i = 0; i < league.teams[league.userLeague].length; i++) 
		{
			//com.mygdx.mongojocs.lma2007.Team t = league.teams[league.userLeague][i];
			//dos.writeByte((byte)t.globalIdx);
			Team t = league.getTeamFromGlobalIdx(dis.readByte());
			t.points = dis.readByte();
			t.goalsScored = dis.readShort();
			t.goalsReceived = dis.readShort();
			t.matchesPlayed = dis.readByte();
			t.matchesWon = dis.readByte();
			t.matchesDrew = dis.readByte();
			t.matchesLost = dis.readByte();
		}
		
		
		
		
		
		// LAST LEAGUE RESULTS / LAST CHAMPIONS RESULTS
		// TODO
		///////////////

		// MENSAJES
		
		messageCount = dis.readInt();
		for (int i = 0; i < MAXMESSAGES;i++)
		{
			messageTitle[i] = dis.readUTF();
			messageBody[i] = dis.readUTF();
			messageType[i] = dis.readInt();
			messageAge[i] = dis.readInt();
			int t = dis.readInt();
			if (t != 0)
			{
				messageData[i] = new int[t];
				for(int j = 0; j < messageData[i].length; j++)
					messageData[i][j] = dis.readInt();
			}
		}
		//#ifdef DEBUG
		znrread("userFormation", 4);
		//#endif
		userFormation = dis.readInt();
		
		//	 FINANZAS
		// TODO
		///////////////
		//#ifdef DEBUG
		znrread("coachCash", 4);
		//#endif
		coachCash = dis.readInt();
		
		clubIncome = new int[4];
		//#ifdef DEBUG
		znrread("clubIncome", 16);
		//#endif
		for(int i = 0; i < clubIncome.length; i++) {
			
			clubIncome[i] = dis.readInt();
		}
		
		clubExpenses = new int[4];
		//#ifdef DEBUG
		znrread("clubExpenses", 16);
		//#endif
		for(int i = 0; i < clubExpenses.length; i++) {
			
			clubExpenses[i] = dis.readInt();
		}
		
		//#ifdef DEBUG
		znrread("employees", employees.length*1);
		//#endif
		for(int i = 0; i < employees.length; i++) {
			
			employees[i] = dis.readByte();
		}
		
		//#ifdef DEBUG
		znrread("ticketPrices", ticketPrices.length*4);
		//#endif
		for(int i = 0; i < ticketPrices.length; i++) {
			
			ticketPrices[i] = dis.readInt();
		}
		//#ifdef DEBUG
		znrread("sponsors", sponsors.length * 12);
		//#endif
		for(int i = 0; i < sponsors.length; i++) {
		
			sponsors[i][0] = dis.readInt();
			sponsors[i][1] = dis.readInt();
			sponsors[i][2] = dis.readInt();
		}
		
		// CONTRACTS
		// TODO
		///////////////
		//#ifdef DEBUG
		znrread("contracts.length", 4);
		//#endif
		contracts = new int[dis.readInt()][4];
		//#ifdef DEBUG
		znrread("contracts", contracts.length*16);
		//#endif
		for(int i = 0; i < contracts.length; i++) {
		
			contracts[i][0] = dis.readInt();
			contracts[i][1] = dis.readInt();
			contracts[i][2] = dis.readInt();
			contracts[i][3] = dis.readInt();
		}
	
		// TRANSFERS
		// TODO
		///////////////
		//#ifdef DEBUG
		znrread("sellingPlayers.length", 4);
		//#endif
		sellingPlayers = new short[dis.readInt()];
		//#ifdef DEBUG
		znrread("sellingPlayers", sellingPlayers.length * 2);
		//#endif
		for(int i = 0; i < sellingPlayers.length; i++) {
			
			sellingPlayers[i] = dis.readShort();
		}
		//#ifdef DEBUG
		znrread("buyingPlayers.length", 4);
		//#endif
		buyingPlayers = new short[dis.readInt()];
		//#ifdef DEBUG
		znrread("buyingPlayers", buyingPlayers.length * 2);
		//#endif
		for(int i = 0; i < buyingPlayers.length; i++) {
			
			buyingPlayers[i] = dis.readShort();
		}
		
//#ifndef REM_TRANSFERS_NEGOTIATIONS		
		
		//#ifdef DEBUG
		znrread("negotiations.length", 4);
		//#endif
		negotiations = new int[dis.readInt()][5];
		
		//#ifdef DEBUG
		znrread("negotiations", negotiations.length*16);
		//#endif
		for(int i = 0; i < negotiations.length; i++) {
			int j =	dis.readInt();
			if (j == -1) 
				negotiations[i] = null;
			else
			{
				negotiations[i][0] = j; 
				negotiations[i][1] = dis.readInt();
				negotiations[i][2] = dis.readInt();
				negotiations[i][3] = dis.readInt();
				negotiations[i][4] = dis.readInt();
			}
		}
		
//#endif

		// ATTENDANCE	
		//#ifdef DEBUG
		znrread("attendance", attendanceHistorial.length*4);
		//#endif
		for(int i = 0; i < attendanceHistorial.length; i++) {
			
			attendanceHistorial[i] = dis.readInt();
		}
		
		// TRAININGS
		for(int i = 0; i < trainingSchedule.length; i++) 
		{			
			for(int j = 0; j < trainingSchedule[i].length; j++) 
			{
				//#ifdef DEBUG
				znrread("training", 4);
				//#endif
				GameCanvas.trainingSchedule[i][j] = dis.readInt();
			}
		}



	// grafica de la liga
		for(int i = 0; i < league.userTeamLeagueHistorial.length; i++) {
			//#ifdef DEBUG
			znrread("grafica liga", 2);
			//#endif
			league.userTeamLeagueHistorial[i] = dis.readShort();
		}

		
		// END VARIABLES LIGA
    // ESTADIO
		for(int i = 0; i < 10;i++)
		{
			//#ifdef DEBUG
			znrread("stadium", 8);
			//#endif
			stadiumLevel[i] = dis.readInt();
			stadiumBuildStage[i] = dis.readInt();
		}
    // END VARIABLES ESTADIO	
		//PLAYER GOALS
		//#ifndef REM_TOP_SCORERS
		//#ifdef DEBUG
		znrread("topScorers", league.topScorers.length*2);
		//#endif
		for(int i = 0; i < league.topScorers.length;i++)
		{
			league.topScorers[i] = dis.readShort();
		}
		
		//#ifdef DEBUG
		znrread("playerGoals", league.rawPlayerGoals.length);
		//#endif
		for(int i = 0; i < league.rawPlayerGoals.length;i++)
		{
			league.rawPlayerGoals[i] = dis.readByte();
		}
		//#endif
	//END PLAYER GOALS
		
		userTeamStartSeasonPosition = dis.readInt();
		lastLeaguePosition[0] = dis.readInt();
		lastLeaguePosition[1] = dis.readInt();
		lastLeaguePosition[2] = dis.readInt();
//	LAST RESULTS
		
		int lem = dis.readByte();
		int llm = dis.readByte();
		league.lastEuropeanMatches = new Team[lem];
		league.lastEuropeanResults = new byte[lem];
		league.lastLeagueMatches = new Team[llm];
		league.lastLeagueResults = new byte[llm];
				
		for(int i = 0; i < lem;i++)
		{			
			league.lastEuropeanMatches[i] = league.getTeamFromGlobalIdx(dis.readByte()); 
			league.lastEuropeanResults[i] = dis.readByte();
		}
		for(int i = 0; i < llm;i++)
		{
			league.lastLeagueMatches[i] = league.getTeamFromGlobalIdx(dis.readByte());
			league.lastLeagueResults[i] = dis.readByte();
		}
		
		
		
		//POSTPROCESO
		//if (league.currentWeek > 0) league.sortLeague();
		league.startJourney();
		
//#ifdef DEBUG
	Debug.println ("---------------------> END LOAD savegame");
//#endif

// ===============================

	} catch(Exception e)
	{
	//#ifdef DEBUG
		Debug.println("* * * loadsavegame() Error cargando variables * * ");
		Debug.println(e.toString());
		return;
	//#endif
	}

//#ifdef DEBUG
	Debug.println("savegame leidas correctamente");
//#endif
}

// -------------------
// savegame SET
// ===================

public void saveGame()
{
	//com.mygdx.mongojocs.lma2007.Debug.println("##########save");
	//#ifdef DEBUG
	znrreset();
	//#endif
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	try {
	// Ponemos las varibles del juego a salvar como savegame.

		//#ifdef DEBUG
		znrwrite("nose", 1);
		//#endif
		dos.writeByte(0);
		
// -------------------------------
// Comenzamos a guardar las variables de las savegame
// ===============================
		//#ifdef DEBUG
		znrwrite("nose", 13);
		//#endif
		dos.writeByte(playgameStarted? 1:0);
		//#ifndef REM_FANTASY
		dos.writeUTF(league.userTeam.name);
		//#endif
		//#ifndef REM_TEAMCOLORS
		dos.writeInt(league.userTeam.flagColor[0]);
		dos.writeInt(league.userTeam.flagColor[1]);
		//#else
		//#endif
		dos.writeInt(gameMode);

		//#ifdef DEBUG
		znrwrite("autoManagement", autoManagement.length);
		//#endif
		for (int i=0 ; i<autoManagement.length ; i++) {dos.writeByte(autoManagement[i]? 1:0);}


		//#ifdef DEBUG
		znrwrite("cust", 8);
		//#endif
		dos.writeInt(custCurrentMonth);
		dos.writeInt(custCurrentAge);
		//#ifdef DEBUG
		znrwrite("nose", 4);
		//#endif
		dos.writeInt(custDifficulty);
		//#ifdef DEBUG
		znrwrite("nose", 8);
		//#endif
		dos.writeInt(custLeague);
		dos.writeInt(custTeam);
		//#ifdef DEBUG
		znrwrite("name", 0);
		//#endif
		dos.writeUTF(custName);
		//#ifdef DEBUG
		znrwrite("nose", 4);
		//#endif
		dos.writeInt(custNationality);
		//#ifdef DEBUG
		znrwrite("preferred", 8);
		//#endif
		dos.writeInt(custPreferredClubLeague);
		dos.writeInt(custPreferredClubTeam);
		//#ifdef DEBUG
		znrwrite("nose", 4*5);
		//#endif
		dos.writeInt(custSkillPoints);

		dos.writeInt(custSkillNum[0]);
		dos.writeInt(custSkillNum[1]);
		dos.writeInt(custSkillNum[2]);
		dos.writeInt(custSkillNum[3]);

		dos.writeInt(preferedClubId);


		dos.writeInt(exhiLeagueA);
		dos.writeInt(exhiTeamA);
		dos.writeInt(exhiLeagueB);
		dos.writeInt(exhiTeamB);


//#ifdef DEBUG
	Debug.println ("---------------------> START SAVE savegame");
//#endif


		// VARIABLES LIGA
		//#ifdef DEBUG
		znrwrite("leaguevars", 4*5);
		//#endif
		dos.writeInt(league.season);
		dos.writeInt(league.currentJourney);
		dos.writeInt(league.journeyType);
		dos.writeInt(league.lastJourneyType);
		dos.writeInt(league.currentWeek);
		dos.writeInt(league.currentYear);
		dos.writeBoolean(league.champEnded);		
		//#ifdef DEBUG
		znrwrite("nose", 4*3);
		//#endif
		dos.writeInt(league.playerSeasonGoal);
		dos.writeInt(league.userLeague);
		dos.writeInt(league.userTeam.globalIdx);
		dos.writeInt(trainingStyle);
		dos.writeInt(squadStyle);
		dos.writeInt(squadTactic);
//		System.out.println("IDX:  "+league.userTeam.globalIdx);
		
		//#ifdef DEBUG
		znrwrite("champ", 8+1);
		//#endif
		dos.writeByte(league.playingChamp ? 1 : 0);		
		dos.writeInt(league.champRound);
		dos.writeInt(league.champTeams.length);
		
		for (int i = 0; i < league.champTeams.length; i++) 
		{
			//#ifdef DEBUG
			znrwrite("champteams", 4*2);
			//#endif
			dos.writeInt(league.champTeams[i].globalIdx);
			dos.writeByte(league.champTeams[i].europeanMatchGoals);
		}


//#ifdef DEBUG
	Debug.println ("league.season: "+league.season);
	Debug.println ("league.currentJourney: "+league.currentJourney);
	Debug.println ("league.lastJourneyType: "+league.lastJourneyType);
	Debug.println ("league.currentWeek: "+league.currentWeek);
	Debug.println ("league.currentYear: "+league.currentYear);
	Debug.println ("league.playerSeasonGoal: "+league.playerSeasonGoal);
	Debug.println ("league.userLeague: "+league.userLeague);
	Debug.println ("league.userTeam: "+league.userTeam);
	Debug.println ("league.playingChamp: "+league.playingChamp);
	Debug.println ("league.champRound: "+league.champRound);
	Debug.println ("league.champTeams(length): "+league.champTeams.length);
	Debug.println ("--------------");
//#endif

		
	//#ifdef DEBUG
	znrwrite("transfersRecord.length", 4);
	//#endif
	dos.writeInt(league.transfersRecord.length);
	
	//#ifdef DEBUG
	znrwrite("transfersRecord", league.transfersRecord.length * 8);
	//#endif
	for(int i = 0; i < league.transfersRecord.length; i++) {
		
		dos.writeInt(league.transfersRecord[i][0]);
		dos.writeInt(league.transfersRecord[i][1]);
	}
	
	
		// alineacion equipo user
		for (int i = 0; i < Team.MAXPLAYERS; i++) 
		{
			//#ifdef DEBUG
			znrwrite("team", 4);
			//#endif
			if (i < league.userTeam.playerCount)
				dos.writeInt(league.userTeam.playerIds[i]);
			else 
				dos.writeInt(-1);
		}
		//#ifdef DEBUG
		System.out.println("PLAYERCOUNT: "+league.userTeam.playerCount);
		//#endif
		
		// stats jugadores del user
		for (int i = 0; i < Team.MAXPLAYERS; i++) 
		{
			//#ifdef DEBUG
			znrwrite("player stats", 4);
			//#endif
			int p = league.userTeam.playerIds[i];
			if (p >= 0) 
			{
				int ex = league.extendedPlayer(league.userTeam.playerIds[i]);
				dos.writeByte(league.playerGetSkill(p, 0) );
				dos.writeByte(league.playerGetSkill(p, 1) );
				dos.writeByte(league.playerGetSkill(p, 2) );
				dos.writeByte(league.playerGetSkill(p, 3) );
				dos.writeByte(league.userPlayerStats[ex][league.SANCTION_JOURNEYS]);
				dos.writeByte(league.userPlayerStats[ex][league.INJURY_JOURNEYS]);			
				dos.writeUTF(league.playerGetName(p));
			} else {
				/*
				dos.writeByte(0);
				dos.writeByte(0);
				dos.writeByte(0);
				dos.writeByte(0);*/
			}
		}
		
		
	

		
		//#ifdef DEBUG
		znrwrite("nose", 2);
		//#endif
		// jornadas liga
		dos.writeByte(league.leagueJourneys.length);
		dos.writeByte(league.leagueJourneys[0].length);
		
		for (int i = 0; i < league.leagueJourneys.length; i++) 
		{
			for (int j = 0; j < league.leagueJourneys[0].length; j++) 
			{
				//#ifdef DEBUG
				znrwrite("league", 1);
				//#endif
				dos.writeByte(league.leagueJourneys[i][j].globalIdx);
			}
		}
		//#ifdef DEBUG
		znrwrite("leaguestats", 6+2+2);
		//#endif
		// stats equipos en la liga
		for (int i = 0; i < league.teams[league.userLeague].length; i++) 
		{
			
			Team t = league.teams[league.userLeague][i];
			dos.writeByte((byte)t.globalIdx);
			dos.writeByte((byte)t.points);
			dos.writeShort((short)t.goalsScored);
			dos.writeShort((short)t.goalsReceived);
			dos.writeByte((byte)t.matchesPlayed);
			dos.writeByte((byte)t.matchesWon);
			dos.writeByte((byte)t.matchesDrew);
			dos.writeByte((byte)t.matchesLost);
		}
		
		
		// LAST LEAGUE RESULTS / LAST CHAMPIONS RESULTS
		// TODO
		///////////////

		// MENSAJES
		
		dos.writeInt(messageCount);
		for (int i = 0; i < MAXMESSAGES;i++)
		{
			
			if (i < messageCount)
			{
				dos.writeUTF(messageTitle[i]);
				dos.writeUTF(messageBody[i]);
			}
			else
			{
				dos.writeUTF("none");
				dos.writeUTF("none");
			}				
			dos.writeInt(messageType[i]);
			dos.writeInt(messageAge[i]);
			if (messageData[i] == null)
				dos.writeInt(0);
			else
			{
				dos.writeInt(messageData[i].length);
				for(int j = 0; j < messageData[i].length;j++)
					dos.writeInt(messageData[i][j]);	
			}
						
		}
		
		//#ifdef DEBUG
		znrwrite("userFormation", 4);
		//#endif
		dos.writeInt(userFormation);
		
		//	 FINANZAS
		// TODO
		///////////////
		//#ifdef DEBUG
		znrwrite("coachCash", 4);
		//#endif		
		dos.writeInt(coachCash);
		
		//#ifdef DEBUG
		znrwrite("clubIncome", clubIncome.length * 4);
		//#endif
		for(int i = 0; i < clubIncome.length; i++) {
		
			dos.writeInt(clubIncome[i]);
		}
		
		//#ifdef DEBUG
		znrwrite("clubExpenses", clubExpenses.length * 4);
		//#endif
		for(int i = 0; i < clubExpenses.length; i++) {
			
			dos.writeInt(clubExpenses[i]);
		}
		
		//#ifdef DEBUG
		znrwrite("employees", employees.length);
		//#endif
		for(int i = 0; i < employees.length; i++) {
			
			dos.writeByte(employees[i]);
		}
		
		//#ifdef DEBUG
		znrwrite("ticketPrices", ticketPrices.length * 4);
		//#endif
		for(int i = 0; i < ticketPrices.length; i++) {
			
			dos.writeInt(ticketPrices[i]);
		}
		
		//#ifdef DEBUG
		znrwrite("sponsors", sponsors.length * 12);
		//#endif
		for(int i = 0; i < sponsors.length; i++) {
		
			dos.writeInt(sponsors[i][0]);
			dos.writeInt(sponsors[i][1]);
			dos.writeInt(sponsors[i][2]);
		}
		
		// CONTRACTS
		// TODO
		///////////////
		
		//#ifdef DEBUG
		znrwrite("contracts.length", 4);
		//#endif
		dos.writeInt(contracts.length);
		
		//#ifdef DEBUG
		znrwrite("contracts", contracts.length * 16);
		//#endif
		for(int i = 0; i < contracts.length; i++) {
		
			dos.writeInt(contracts[i][0]);
			dos.writeInt(contracts[i][1]);
			dos.writeInt(contracts[i][2]);
			dos.writeInt(contracts[i][3]);
		}
			
		// TRANSFERS
		// TODO
		///////////////
		
		//#ifdef DEBUG
		znrwrite("sellingPlayers.length", 4);
		//#endif
		dos.writeInt(sellingPlayers.length);
		
		//#ifdef DEBUG
		znrwrite("sellingPlayers", sellingPlayers.length*2);
		//#endif
		for(int i = 0; i < sellingPlayers.length; i++) {
			
			dos.writeShort(sellingPlayers[i]);
		}
		
		//#ifdef DEBUG
		znrwrite("buyingPlayers.length", 4);
		//#endif
		dos.writeInt(buyingPlayers.length);
		//#ifdef DEBUG
		znrwrite("buyingPlayers", buyingPlayers.length*2);
		//#endif
		for(int i = 0; i < buyingPlayers.length; i++) {
			
			dos.writeShort(buyingPlayers[i]);
		}
		
//#ifndef REM_TRANSFERS_NEGOTIATIONS
		//#ifdef DEBUG
		znrwrite("negotiations.length", 4);
		//#endif
		dos.writeInt(negotiations.length);
		

		//#ifdef DEBUG
		znrwrite("negotiations", negotiations.length * 16);
		//#endif
		for(int i = 0; i < negotiations.length; i++) {
			if (negotiations[i] == null)
			{
				dos.writeInt(-1);						
			}
			else
			{
				dos.writeInt(negotiations[i][0]);
				dos.writeInt(negotiations[i][1]);
				dos.writeInt(negotiations[i][2]);
				dos.writeInt(negotiations[i][3]);
				dos.writeInt(negotiations[i][4]);
			}
		}
		
//#endif
		
		// ATTENDANCE		
		//#ifdef DEBUG
		znrwrite("attendance", attendanceHistorial.length*4);
		//#endif
		for(int i = 0; i < attendanceHistorial.length; i++) {
			
			 dos.writeInt(attendanceHistorial[i]);
		}
				
		
		// TRAININGS
		for(int i = 0; i < trainingSchedule.length; i++) 
		{			
			for(int j = 0; j < trainingSchedule[i].length; j++) 
			{
				//#ifdef DEBUG
				znrwrite("training", 4);
				//#endif
				dos.writeInt(GameCanvas.trainingSchedule[i][j]);
			}
		}


	// grafica de la liga
		for(int i = 0; i < league.userTeamLeagueHistorial.length; i++) {
			//#ifdef DEBUG
			znrwrite("grafica liga", 2);
			//#endif
			dos.writeShort(league.userTeamLeagueHistorial[i]);
		}


		// END VARIABLES LIGA
	// ESTADIO
		for(int i = 0; i < 10;i++)
		{
			//#ifdef DEBUG
			znrwrite("stadium", 8);
			//#endif
			dos.writeInt(stadiumLevel[i]);
			dos.writeInt(stadiumBuildStage[i]);
		}
    // END VARIABLES ESTADIO	
	//PLAYER GOALS
		//#ifndef REM_TOP_SCORERS
		//#ifdef DEBUG
		znrwrite("topScorers", league.topScorers.length*2);
		//#endif
		for(int i = 0; i < league.topScorers.length;i++)
		{
			dos.writeShort(league.topScorers[i]);
		}
		
		//#ifdef DEBUG
		znrwrite("playerGoals", league.rawPlayerGoals.length);
		//#endif
		for(int i = 0; i < league.rawPlayerGoals.length;i++)
		{
			dos.writeByte(league.rawPlayerGoals[i]);
		}
		//#endif
		
	//END PLAYER GOALS	
		dos.writeInt(userTeamStartSeasonPosition);
		dos.writeInt(lastLeaguePosition[0]);
		dos.writeInt(lastLeaguePosition[1]);
		dos.writeInt(lastLeaguePosition[2]);
	// LAST RESULTS
		int lem = 0;
		int llm = 0;
		if (league.lastEuropeanMatches != null) lem = league.lastEuropeanMatches.length;
		if (league.lastLeagueMatches != null) llm = league.lastLeagueMatches.length;
		
		dos.writeByte(lem);
		dos.writeByte(llm);
				
		for(int i = 0; i < lem;i++)
		{
			dos.writeByte(league.lastEuropeanMatches[i].globalIdx);
			dos.writeByte(league.lastEuropeanResults[i]);
		}
		for(int i = 0; i < llm;i++)
		{
			dos.writeByte(league.lastLeagueMatches[i].globalIdx);
			dos.writeByte(league.lastLeagueResults[i]);
		}
			
			
		
		
//#ifdef DEBUG
	Debug.println ("---------------------> END SAVE savegame");
//#endif


		

// ===============================

	// Almacenamos las savegame
		byte[] prefsData = baos.toByteArray();
		byte checksum = 0; for (int i=1 ; i<prefsData.length ; i++) {checksum += prefsData[i];}
		prefsData[0] = checksum;
		updatePrefs(prefsData, "game");		// Almacenamos byte[]

	} catch(Exception e)
	{
	//#ifdef DEBUG
		Debug.println("Error guardando savegame");
		Debug.println(e.toString());
		return;
	//#endif
	}

//#ifdef DEBUG
	Debug.println("Preferencias guardadas correctamente");
//#endif
	
//#ifdef DEBUGPREFS
	loadGame();
//#endif
}

// <=- <=- <=- <=- <=-
//#ifdef DEBUG
//#ifndef DEBUGPREFS
//#else

int writecounter = 0;
int readcounter = 0;
String scheck[] = new String[2000];
int check[] = new int[2000];
int scounter;

public void znrreset()
{
	writecounter = 0;
	readcounter = 0;
	scounter = 0;
}

public void znrloadreset()
{
	readcounter = 0;
	scounter = 0;
}


public void znrwrite(String what, int b)
{
	check[scounter] = b;
	scheck[scounter++] = what;
	writecounter += b;
	Debug.println(writecounter+": "+what+" ("+b+")");
}

public void znrread(String what, int b)
{
	if (readcounter >= writecounter)
	{
		Debug.println("fin: readcounter: "+readcounter+"   writecounter:"+writecounter);
		return;
	}
	else if (b != check[scounter])
	{
		Debug.println("Tama�o incorrecto: "+ what+ "read:"+b+ "  written:"+check[scounter]);
	}
	else if (what.compareTo(scheck[scounter]) != 0)
	{
		Debug.println("Nombre incorrecto: " + what);
	}	
	scounter++;
	readcounter += b;
	Debug.println("Correcto: "+readcounter+": "+what+" ("+b+")");
}
//#endif
//#endif

// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-






//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
//----------------------------------LMA MANAGER 2007 -------------------------
//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
//----------------------------------------------------------------------------


// David

public static final int EMPLOYEES_ASSISTMANAGER = 0;
public static final int EMPLOYEES_COACH = 1;
public static final int EMPLOYEES_SCOUT = 2;
public static final int EMPLOYEES_FITNESSINSTRUCTOR = 3;
public static final int EMPLOYEES_PHYSIOTHERAPIST = 4;
public static final int EMPLOYEES_COMERCIALMANAGER = 5;

public static final int TICKET_STANDING = 0;
public static final int TICKET_NORMAL = 1;
public static final int TICKET_VIP = 2;
public static final int TICKET_PARKING = 3;

public static final int INCOME_SPONSORSHIP = 0;
public static final int INCOME_TRANSFERS = 1;
public static final int INCOME_MATCH = 2;
public static final int INCOME_PARKING = 3;

public static final int EXPENSES_WAGES = 0;
public static final int EXPENSES_TRANSFERS = 1;
public static final int EXPENSES_FACILITIES = 2;
public static final int EXPENSES_SQUADFEES = 3;

public static final int SPONSOR_SHIRTS = 0;
public static final int SPONSOR_FENCES = 1;

public static final int CONTRACT_PLAYERID = 0;
public static final int CONTRACT_DURATION = 1;
public static final int CONTRACT_SALARY = 2;
public static final int CONTRACT_BONUS = 3;

//#ifndef REM_FILTER
public static final int FILTER_LIST_SIZE = 200;
//#endif
public static final int PLAYER_LIST_MAX = 100;



byte employees[];
static int[][] employeeCost = {{500,675,1010},{375,500,750},{200,275,415},{400,525,790},{415,550,825},{340,450,680}};

int ticketPrices[];

int clubIncome[];
int clubExpenses[];
int sponsors[][];
short sellingPlayers[];
short buyingPlayers[];
short buySelectionList[];
short substitutedPlayers[];
//#ifndef REM_TRANSFERS_NEGOTIATIONS
int negotiations[][];
//#endif
int contracts[][];

int transfersSelectedLeague;
int transfersSelectedTeam;
int transfersMarkedPlayers;

int currentChoosingEmployee;

int negotiateYears;
int negotiateSalary;
int negotiateBonus;
int negotiatePatience;

//#ifndef REM_FILTER
int filterLeague;
int filterPosition;
int filterMaxCost;
//#endif

int userFormation;
int squadStyle = 0;

int coachCash;

int lastMatchAttendance;

int attendanceHistorial[] = new int[40];


boolean playingMatch = false;

int substitutionsLeft;

//#ifndef REM_FANTASY
int fantasyPlayerSelecting;
int fantasyTotalCost;
//#endif

// TBD: Mover al fichero de textos
 
//String getMenuText(MENTXT_ATTACK_STYLE)[] = {"Defensive", "Medium", "Offensive"};
//String getMenuText(MENTXT_FILTER_LEAGUE)[] = {"England","Scotland","Germany","Italy","France","Spain","Any"};
//String getMenuText(MENTXT_FILTER_POSITION)[] = {"GK", "DF", "MF", "AT", "Any"};
//String getMenuText(MENTXT_SHIRTSPONSOR_NAMES)[] = {"FUNSTA", "MICROJOCS", "MOVIJUEGOS", "PALOJUEGOS", "ACTIVEMINDS"};
//String getMenuText(MENTXT_FENCESPONSOR_NAMES)[] = {"KIKE", "PORTA-COLA", "LEGUMBRES JUANANTONIO", "GALLETAS JANA", "COLOC�N", "COLECOVISION", "OZONE"};


void gameVariablesStartup() {

	employees = new byte[6];// {0, 0, 0, 0, 0, 0};
	clubIncome = new int[4];// {0, 0, 0, 0};
	clubExpenses = new int[4];// {0, 0, 0, 0};	
	ticketPrices = new int[] {50, 100, 200, 10};	
	sponsors = new int [2][3];// {{0, 0, 0}, {0, 0, 0}};//{{1500, 0, 0}, {600, 3, 0}};
	sellingPlayers = new short[] {};
	buyingPlayers = new short[] {};
	buySelectionList = null;
//#ifndef REM_TRANSFERS_NEGOTIATIONS	
	negotiations = new int[][] {};
//#endif
	contracts = new int[][] {};
	
//#ifndef REM_FILTER	
	filterLeague = 0;
	filterPosition = 3;
	filterMaxCost = 15000000;
//#endif

	userFormation = 0;
	
	// Create contracts for initial squad
	
	for(int i = 0; i < league.userTeam.playerCount; i++) {
		
		int playerId = league.userTeam.playerIds[i];
	
		addContract(playerId, league.playerGetQuality(playerId) / 15, playerExpectedSalary(playerId), playerExpectedSalary(playerId) / 6);
				
	}
				

}



public boolean isInArray(short[]array, short value) {
	
	for(int i = 0; i < array.length; i++) {
	
		if(array[i] == value) {
			
			return true;
		}
	}
	
	return false;
}

public String moneyUnit() {
	
	//return "�";
	return getMenuText(MENTXT_CURRENCY)[0];
}

public String moneyStr(long m, boolean unit) {
	
	String res; 
	
	// GBP conversion (default is Euros)
	
	if(getMenuText(MENTXT_CURRENCY)[0].equals("GBP")) {
	
		m = (m*69)/100;
	}
				
	if(m > 1000000) {
		
		res = (m/1000000) + "."+((m%1000000)/100000)+" M";
				
	} else {
	
		res = m+" ";
	}
		
	if(unit) {
	
		res = res+moneyUnit();
	}
	
	return res;
}

public void financesUpdateValues() {
			
	clubIncome[INCOME_SPONSORSHIP] = (sponsors[SPONSOR_SHIRTS][1] > 0 ? sponsors[SPONSOR_SHIRTS][0] : 0) + (sponsors[SPONSOR_FENCES][1] > 0 ? sponsors[SPONSOR_FENCES][0] : 0);
	//clubIncome[INCOME_TRANSFERS] = 0;
	clubIncome[INCOME_MATCH] = (lastMatchAttendance * ticketPrices[TICKET_NORMAL]) / 2 + (lastMatchAttendance * ticketPrices[TICKET_VIP]) / 4 + (lastMatchAttendance * ticketPrices[TICKET_STANDING]) / 4; // Fake
	clubIncome[INCOME_PARKING] = ((lastMatchAttendance / 2) * ticketPrices[TICKET_PARKING]); // Fake
	
	clubExpenses[EXPENSES_WAGES] = 0;
	
	for(int i = 0; i < contracts.length; i++) {
			
		clubExpenses[EXPENSES_WAGES] += contracts[i][CONTRACT_SALARY];
				
	}
	
	for(int i = 0; i < employees.length; i++) {
	
		if(employees[i] > 0) {
			
			clubExpenses[EXPENSES_WAGES] += employeeCost[i][employees[i] - 1];
		}
	}
		
	//clubExpenses[EXPENSES_TRANSFERS] = 0;
	clubExpenses[EXPENSES_FACILITIES] = 0;	
	
}

public int playerCost(int id) {
	
	return league.playerGetQuality(id) * 333333;
}

public int playerExpectedSalary(int id) {
	
	return playerCost(id) / 1360;
}

public short[] removeValueFromArray(short []arr, short val) {
	
	short aux[] = new short[arr.length];
	short pos = 0;
	
	for(int i = 0; i < arr.length; i++) {
	
		if(arr[i] != val) {
		
			aux[pos] = arr[i]; 
			pos++;
		}
	}
	
	short[] res = new short[pos];
	
	System.arraycopy(aux,0,res,0,pos);
	
	return res;
}

public short[] addValueToArray(short []arr, short val) {
	
	// If value already in vector, do nothing
	
	//com.mygdx.mongojocs.lma2007.Debug.println("Adding "+val);
	
	for(int i = 0; i < arr.length; i++) {
	
		if(arr[i] == val) {
		
			return arr;
		}
	}
	
	short[] res = new short[arr.length + 1];
			
	System.arraycopy(arr,0,res,0,arr.length);
	
	res[res.length - 1] = val;
	
	return res;
}

public boolean existsValueInArray(short[] arr, short val) {

	if(arr == null) {
	
		return false;
	}
	
	for(int i = 0; i < arr.length; i++) {
		
		if(arr[i] == val) {
		
			return true;
		}
	}
	
	return false;
}

//#ifndef REM_TRANSFERS_NEGOTIATIONS

public int pendingNegotiationIndex(int playerId) {
	

	for(int i = 0; i < negotiations.length; i++) {
	
		if(negotiations[i] != null) {
			
			if(negotiations[i][0] == playerId) {
			
				return i;
			}
		}
	}
	
	return -1;
}

//#endif

public int contractIndex(int playerId) {

	for(int i = 0; i < contracts.length; i++) {
	
		if(contracts[i][CONTRACT_PLAYERID] == playerId) {
		
			return i;
		}
	}
	
	return -1;
}

public void addContract(int playerId, int duration, int salary, int bonus) {
	
	int previousIndex = contractIndex(playerId);
	
	if(previousIndex >= 0) {
		
		contracts[previousIndex] = new int[] {playerId, duration, salary, bonus};
		
	} else {
	
		int auxCon[][] = new int[contracts.length + 1][4];
		
		for(int i = 0; i < contracts.length; i++) {
		
			auxCon[i] = contracts[i];
		}
		
		auxCon[contracts.length] = new int[] {playerId, duration, salary, bonus};
		
		contracts = auxCon;
	}
			
}

public void deleteContract(int playerId) {

	if(contractIndex(playerId) >= 0) {
		
		int auxCon[][] = new int[contracts.length - 1][4];
		
		int index = 0;
		
		for(int i = 0; i < contracts.length; i++) {
			
			if(contracts[i][CONTRACT_PLAYERID] != playerId) {
				
				auxCon[index] = contracts[i];
				index++;
			}			
		}
		
		contracts = auxCon;
	} 	
}


/*
public void quickSort(short a[], int inf, int sup) {
	
	boolean cont = true;
	short pivot = a[sup], temp;
	int i = inf, j = sup - 1;
	
	
	System.out.println("Quick sort for "+inf+","+sup+" (pivot "+pivot+")");
	
	if(i >= j) {
	
		return;
	}
	
	while(cont) {
									
		while(a[++i] < pivot && i < a.length) {
			
			System.out.println("	      i:"+i);			
		}
											
		while(a[--j] > pivot && j >= 0) {
							
			System.out.println("	      j:"+j);			
		}
		
		if(i < j) {
		
			temp = a[i];
			a[i] = a[j];
			a[j] = temp;
						
		} else {
			
			cont = false;
		}
		
		System.out.println("	"+i+","+j);
	}
		
	temp = a[i];
	a[i] = a[sup];
	a[sup] = temp;
	
	quickSort(a,inf,i - 1);
	quickSort(a,i + 1,sup);
			
}
*/

public void buySelectionListSort() {

	//short a[] = new short[] {5, 3, 7, 6, 2, 1, 4};
	//quickSort(a, 0, 6);
	//quickSort(buySelectionList, 0, buySelectionList.length - 1);
	
	
	
	for(int i = 0; i < buySelectionList.length; i++) {
		
		for(int j = 0; j < buySelectionList.length - 1; j++) {
	
			if(league.playerGetQuality(buySelectionList[j]) < league.playerGetQuality(buySelectionList[j + 1])) {
			
				short temp = buySelectionList[j];
				
				buySelectionList[j] = buySelectionList[j + 1];
				buySelectionList[j + 1] = temp;
				
			}
		}
	}		
		
}

public void autoManageEmployees() {

//#ifndef REM_FINANCES_EMPLOYEES	
	if(autoManagement[AUTO_FINANCES_EMPLOYEE]) {
//#endif
		
		int q = league.userTeam.getQuality();
		byte type;
		
		if(q < 45) {
			
			type = 1;
						
		} else if (q < 62) {
			
			type = 2;
			
		} else {
			
			type = 3;
		}
		
		for(int i = 0; i < employees.length; i++) {
		
			employees[i] = type;
		}
		
//#ifndef REM_FINANCES_EMPLOYEES		
	}
//#endif
	
}

public void autoManageSetPrices() {

//#ifndef REM_FINANCES_PRICESSET
	if(autoManagement[AUTO_FINANCES_SETPRICES]) {
//#endif
		
		int matchQuality = league.userMatch[0].getQuality() + league.userMatch[1].getQuality();
					
		ticketPrices[TICKET_NORMAL] = 3*matchQuality / 4;
		ticketPrices[TICKET_VIP] = 3*matchQuality / 2;
		ticketPrices[TICKET_STANDING] = 2*matchQuality / 3;
		ticketPrices[TICKET_PARKING] = 5;
		
//#ifndef REM_FINANCES_PRICESSET		
	}
//#endif
	
}

public void journeyEndProcess() {

	// Finances ==============================================

	//System.out.println("*** 1");
	autoManageEmployees();
	//System.out.println("*** 2");		
	
	coachCash += clubIncome[0] + clubIncome[1] + clubIncome[2] + clubIncome[3];
				
	coachCash -= clubExpenses[0] + clubExpenses[1] + clubExpenses[2] + clubExpenses[3];
	
	financesUpdateValues();
					
	//#ifndef REM_BONUS_CODES	
	if(bonusCodes[BONUS_UNLIMITEDMONEY] != 0) {
		
		coachCash = 999999999;
	}
	//#endif
	
	//System.out.println("*** 3");
	clubIncome[INCOME_TRANSFERS] = 0;
	clubExpenses[EXPENSES_TRANSFERS] = 0;
	
	
	
	// Sponsors ==============================================

	if(sponsors[SPONSOR_SHIRTS][1] > 0) {
		
		sponsors[SPONSOR_SHIRTS][1]--;
		
	}
	
	int money = 1500 + 150 * rnd(league.userTeam.getQuality()/10), duration = 52, id = rnd(getMenuText(MENTXT_SHIRTSPONSOR_NAMES).length);
	
	if(autoManagement[AUTO_FINANCES_SPONSORS]) {
		
		if(sponsors[SPONSOR_SHIRTS][1] == 0) {
									
			sponsors[SPONSOR_SHIRTS][0] = (money * (100 + employees[EMPLOYEES_COMERCIALMANAGER] * 10)) / money;
			sponsors[SPONSOR_SHIRTS][1] = duration;
			sponsors[SPONSOR_SHIRTS][2] = id;
							
			addMessage(MSG_SPONSOR_NOTIFY, getMenuText(MENTXT_SHIRTSPONSOR_CONTRACTED)[0],substringReplace(substringReplace(substringReplace(getMenuText(MENTXT_SHIRTSPONSOR_OFFER_MSG)[2], "[name]", getMenuText(MENTXT_SHIRTSPONSOR_NAMES)[id]), "[money]", moneyStr(money,true)), "[time]", ""+duration), new int[] {});
			
		}
		
	} else {
	
		if(sponsors[SPONSOR_SHIRTS][1] < 3 && rnd(3) == 0) {
						
			addMessage(MSG_SHIRT_SPONSOR_OFFER, getMenuText(MENTXT_SHIRTSPONSOR_OFFER_MSG)[0],substringReplace(substringReplace(substringReplace(getMenuText(MENTXT_SHIRTSPONSOR_OFFER_MSG)[1], "[name]", getMenuText(MENTXT_SHIRTSPONSOR_NAMES)[id]), "[money]", moneyStr(money,true)), "[time]", ""+duration), new int[] {money, duration, id});
			
		}						
	}
	//System.out.println("*** 4");
	if(sponsors[SPONSOR_FENCES][1] > 0) {
		
		sponsors[SPONSOR_FENCES][1]--;
	
	}
	
	money = 500 + 50 * rnd(league.userTeam.getQuality()/10); 
	duration = 4; 
	id = rnd(getMenuText(MENTXT_FENCESPONSOR_NAMES).length);
	
	if(autoManagement[AUTO_FINANCES_SPONSORS]) {
		
		if(sponsors[SPONSOR_FENCES][1] == 0) {
									
			sponsors[SPONSOR_FENCES][0] = money;
			sponsors[SPONSOR_FENCES][1] = duration;
			sponsors[SPONSOR_FENCES][2] = id;
							
			addMessage(MSG_SPONSOR_NOTIFY, getMenuText(MENTXT_FENCESPONSOR_CONTRACTED)[0],substringReplace(substringReplace(substringReplace(getMenuText(MENTXT_FENCESPONSOR_OFFER_MSG)[2], "[name]", getMenuText(MENTXT_FENCESPONSOR_NAMES)[id]), "[money]", moneyStr(money,true)), "[time]", ""+duration), new int[] {});
			
		}
		
	} else {
	
		if(sponsors[SPONSOR_FENCES][1] < 3 && rnd(3) == 0) {
				
			addMessage(MSG_FENCE_SPONSOR_OFFER, getMenuText(MENTXT_FENCESPONSOR_OFFER_MSG)[0],substringReplace(substringReplace(substringReplace(getMenuText(MENTXT_FENCESPONSOR_OFFER_MSG)[1], "[name]", getMenuText(MENTXT_FENCESPONSOR_NAMES)[id]), "[money]", moneyStr(money,true)), "[time]", ""+duration), new int[] {money, duration, id});
		}
		
	}
	//System.out.println("*** 5");
	//com.mygdx.mongojocs.lma2007.Debug.println("Current week:"+league.currentWeek);
				//
	if(league.currentWeek == league.leagueJourneys.length) {
		//System.out.println("SeasonEndProcess");
		seasonEndProcess();		
	}

	//  Transfer / negotiations ======================================================
	
	for(int i = 0; i < sellingPlayers.length; i++) {
		
		int amountOffered = playerCost(sellingPlayers[i]) + (-10 + rnd(20))*50000;
									
		addMessage(MSG_BUY_OFFER, substringReplace(getMenuText(MENTXT_BUY_OFFER_MSG)[0], "[name]", league.playerGetName(sellingPlayers[i])), substringReplace(substringReplace(getMenuText(MENTXT_BUY_OFFER_MSG)[1], "[name]", league.playerGetName(sellingPlayers[i])), "[money]", moneyStr(amountOffered,true)), new int[] {sellingPlayers[i], amountOffered});
					
		//com.mygdx.mongojocs.lma2007.Debug.println("GENERATING MESSAGE!!!!");
		
	}
	//System.out.println("*** 6");
//#ifndef REM_TRANSFERS_NEGOTIATIONS
	
	//#ifdef DEBUG
	Debug.println("buyingPlayers.length:"+buyingPlayers.length);
	//#endif
	
	for(int i = 0; i < buyingPlayers.length; i++) {
		
//		System.out.println("BP: "+i);
	//	System.out.println("BP name: "+league.playerGetName(buyingPlayers[i]));
		//System.out.println("CASH: "+getAvailableCash());
		
		//#ifdef DEBUG
		Debug.println(league.playerGetName(buyingPlayers[i]));
		//#endif
		if(league.playerGetQuality(buyingPlayers[i]) > league.userTeam.getQuality() + 30 || getAvailableCash() < playerCost(buyingPlayers[i])) {
			
			//System.out.println("branch 1");
			// El jugador rechaza entablar negociaciones porque nuestro club no tiene nivel suficiente (o no tenemos pasta -> hack)
			
			addMessage(MSG_FOO, substringReplace(getMenuText(MENTXT_BUY_OFFER_REJECT_MSG)[0], "[name]", league.playerGetName(buyingPlayers[i])), substringReplace(getMenuText(MENTXT_BUY_OFFER_REJECT_MSG)[1], "[name]", league.playerGetName(buyingPlayers[i])));
			buyingPlayers = removeValueFromArray(buyingPlayers, buyingPlayers[i]);
			i--;
			
																		
		} else {
			
			//System.out.println("branch 2");
			int patience = league.userTeam.getQuality() - league.playerGetQuality(buyingPlayers[i]);
			
			if(patience < 2) {
				
				patience = 2;
			}
						
			addMessage(MSG_NEGOTIATE_OFFER, league.playerGetName(buyingPlayers[i])+" "+getMenuText(MENTXT_WANTS_TO_NEGOTIATE)[0], league.playerGetName(buyingPlayers[i])+" "+getMenuText(MENTXT_WANTS_TO_KNOW_WHAT)[0], new int[] {buyingPlayers[i], 2, 10000, 2000, patience});
			buyingPlayers = removeValueFromArray(buyingPlayers, buyingPlayers[i]);
			i--;
		}
		
		//com.mygdx.mongojocs.lma2007.Debug.println("GENERATING MESSAGE!!!!");
					
	}

	
	//#ifdef DEBUG
	Debug.println("########### negotiations.length:"+negotiations.length);
	//#endif
	
	//System.out.println("*** 6.5 "+negotiations);
	for(int i = 0; i < negotiations.length; i++) {
	
		
		
		if(negotiations[i] != null) {
			
			//#ifdef DEBUG
			Debug.println(i+": "+league.playerGetName(negotiations[i][0]));
			//#endif
			
			int expectedSalary = playerExpectedSalary(negotiations[i][0]);			
			int expectedBonus = playerExpectedSalary(negotiations[i][0]) / 6;
			int expectedDuration = league.playerGetQuality(negotiations[i][0]) / 15;
			
			//#ifdef DEBUG
			Debug.println("Expected Salary:"+expectedSalary+" Expected Bonus:"+expectedBonus+" Expected Duration:"+expectedDuration);
			Debug.println("Offered Salary:"+negotiations[i][2]+" Offered Bonus:"+negotiations[i][3]+" Offered Duration:"+negotiations[i][1]);
			Debug.println("Patience:"+negotiations[i][4]);
			//#endif				
			
			Team where = league.getTeamWherePlays((short)negotiations[i][0]);
			
			
			//System.out.println("*** NEG 1");
			if(where.playerCount >= 15 && league.userTeam.playerCount < 30 && expectedDuration <= negotiations[i][1] && expectedSalary <= negotiations[i][2] && expectedBonus <= negotiations[i][3]) {

				//System.out.println("*** NEG 2");
				// Offer is good enough for that player
				
				addMessage(MSG_NEGOTIATE_ACCEPT, 
						league.playerGetName(negotiations[i][0])+" "+getMenuText(MENTXT_ACCEPTED_YOU_OFFER)[0], 
						league.playerGetName(negotiations[i][0])+" "+getMenuText(MENTXT_ACCEPTS_YOUR_CONDITIONS)[0], 
						new int[] {negotiations[i][0], negotiations[i][1], negotiations[i][2], negotiations[i][3]});
				//System.out.println("GENERATING MESSAGE!!!!");
				
				menuAction(MENU_ACTION_BUY_COMPLETE);
				/*
				for(int currentMessage = 0; currentMessage < messageCount;)
				{
					if (messageType[currentMessage] == MSG_NEGOTIATE_ACCEPT &&  messageData[currentMessage][0] == negotiations[i][0])
					{
						System.out.println("mes: "+currentMessage+" - "+messageData[currentMessage].length);
						//currentMessage = 0;
						menuAction(MENU_ACTION_BUY_COMPLETE);
						break;
					}					
					currentMessage++;
				}
				*/
				
				int auxneg[][] = new int[negotiations.length][];
				
				System.arraycopy(negotiations,0,auxneg,0,negotiations.length);
				
				for(int j = i; j < auxneg.length - 1; j++) {
				
					auxneg[j] = auxneg[j + 1];
					
				}
				
				negotiations = new int[auxneg.length - 1][];
				
				System.arraycopy(auxneg,0,negotiations,0,negotiations.length);
				i--;
				
			} else {
											
				// Offer rejected
							
				if(negotiations[i][4] == 0 || league.userTeam.playerCount >= 30) {
					
					// Stop negotiation beacause player is angry
					
					addMessage(MSG_FOO, substringReplace(getMenuText(MENTXT_STOP_NEGOTIATE_MSG)[0], "[name]", league.playerGetName(negotiations[i][0])), substringReplace(getMenuText(MENTXT_STOP_NEGOTIATE_MSG)[1], "[name]", league.playerGetName(negotiations[i][0])), null);
										 
					//com.mygdx.mongojocs.lma2007.Debug.println("GENERATING MESSAGE!!!!");
					negotiations[i] = null;
															
				} else {
					
					//System.out.println("*** NEG 8");
					String message = null;
					//#ifdef DEBUG
					Debug.println("Expected Salary:"+expectedSalary+" Offered Salary:"+negotiations[i][2]);
					//#endif
					if(expectedSalary > negotiations[i][2]) {
												
						message = substringReplace(getMenuText(MENTXT_RENEGOTIATE_MSG)[1], "[name]", league.playerGetName(negotiations[i][0]));						
						
					} else if (expectedDuration > negotiations[i][1]) {
											
						message = substringReplace(getMenuText(MENTXT_RENEGOTIATE_MSG)[2], "[name]", league.playerGetName(negotiations[i][0]));						
						
					} else if(expectedBonus > negotiations[i][3]) {
											
						message = substringReplace(getMenuText(MENTXT_RENEGOTIATE_MSG)[3], "[name]", league.playerGetName(negotiations[i][0]));
					}
					
					// Negotiate again
					
					addMessage(MSG_NEGOTIATE_OFFER, substringReplace(getMenuText(MENTXT_RENEGOTIATE_MSG)[0], "[name]", league.playerGetName(negotiations[i][0])), message, new int[] {negotiations[i][0], negotiations[i][1], negotiations[i][2], negotiations[i][3], negotiations[i][4]});
					//#ifdef DEBUG
					Debug.println("GENERATING MESSAGE!!!!");
					//#endif	
					negotiations[i] = null;
																									
				}					
			}				
		}
	}
	//System.out.println("*** 7");
//#else
	
	for(int i = 0; i < buyingPlayers.length; i++) {
		
		Team where = league.getTeamWherePlays(buyingPlayers[i]);
		
		if(league.userTeam.playerCount >= Team.MAXPLAYERS
				|| where.playerCount < 15
				|| league.playerGetQuality(buyingPlayers[i]) > league.userTeam.getQuality() + 30 
				|| getAvailableCash() < playerCost(buyingPlayers[i])) {
			
			// El jugador rechaza entablar negociaciones porque nuestro club no tiene nivel suficiente (o no tenemos pasta -> hack)
			
			addMessage(MSG_FOO, substringReplace(getMenuText(MENTXT_BUY_OFFER_REJECT_MSG)[0], "[name]", league.playerGetName(buyingPlayers[i])), substringReplace(getMenuText(MENTXT_BUY_OFFER_REJECT_MSG)[1], "[name]", league.playerGetName(buyingPlayers[i])));
			buyingPlayers = removeValueFromArray(buyingPlayers, buyingPlayers[i]);
			i--;
		} else {
			
			// Your team is good enough for that player
			
			int expectedSalary = playerExpectedSalary(buyingPlayers[i]);
			int expectedBonus = playerExpectedSalary(buyingPlayers[i]) / 6;
			int expectedDuration = league.playerGetQuality(buyingPlayers[i]) / 15;
		
			
			addMessage(MSG_NEGOTIATE_ACCEPT, league.playerGetName(buyingPlayers[i])+" "+getMenuText(MENTXT_ACCEPTED_YOU_OFFER)[0], league.playerGetName(buyingPlayers[i])+" "+getMenuText(MENTXT_ACCEPTS_YOUR_CONDITIONS)[0], new int[] {buyingPlayers[i], expectedSalary, expectedDuration, expectedBonus});
			
			/*
			for(int currentMessage = 0; currentMessage < messageCount;currentMessage++)
			{
				if (messageData[currentMessage] != null && messageData[currentMessage][0] == buyingPlayers[i])
				//currentMessage = 0;
				menuAction(MENU_ACTION_BUY_COMPLETE);
				break;
			}*/
			
			menuAction(MENU_ACTION_BUY_COMPLETE);
						
			buyingPlayers = removeValueFromArray(buyingPlayers, buyingPlayers[i]);
			i--;
			//currentMessage = 0;
			//menuAction(MENU_ACTION_BUY_COMPLETE);
			//System.out.println("GENERATING MESSAGE!!!!");
		}
	}
				
//#endif
	
	// Contracts =====================================================
		
//#ifndef REM_TRANSFERS_NEGOTIATIONS	
	
	if(league.currentWeek == 1) {
	
		for(int i = 0; i < contracts.length; i++) {
	
			// Check which contracts are in their last year
		
			if(contracts[i][CONTRACT_DURATION] == 1) {
			
				if(pendingNegotiationIndex(contracts[i][CONTRACT_PLAYERID]) < 0) {
				
					addMessage(MSG_NEGOTIATE_OFFER, 
							substringReplace(getMenuText(MENTXT_CONTRACT_IS_ABOUT_TO_EXPIRE)[0], "[playername]", league.playerGetName(contracts[i][CONTRACT_PLAYERID])),
							substringReplace(getMenuText(MENTXT_CONTRACT_ENDS_THIS_YEAR)[0], "[playername]", league.playerGetName(contracts[i][CONTRACT_PLAYERID])),
							//league.playerGetName(contracts[i][CONTRACT_PLAYERID])+" "+getMenuText(MENTXT_CONTRACT_IS_ABOUT_TO_EXPIRE)[0], 
							//league.playerGetName(contracts[i][CONTRACT_PLAYERID])+" "+getMenuText(MENTXT_CONTRACT_ENDS_THIS_YEAR)[0], 
							new int[] {contracts[i][CONTRACT_PLAYERID], 
							league.playerGetQuality(contracts[i][CONTRACT_PLAYERID]) / 15, 
							playerExpectedSalary(contracts[i][CONTRACT_PLAYERID]), 
							playerExpectedSalary(contracts[i][CONTRACT_PLAYERID]) / 6, 
							1000});									
				}
			}
		}
	}
	
//#endif	
	//System.out.println("*** 8");
	// ZNR
//#ifndef REM_STADIUM
	// Stadium =========================================================
	for(int i = 0; i < stadiumBuildStage.length;i++)
	{
		int level = stadiumLevel[i];
		
		if (stadiumBuildStage[i] >= 1)
		{			
			stadiumBuildStage[i]++;
			if (stadiumBuildStage[i] > stadiumBuildTime[i][level])
			{
				addMessage(MSG_FOO, getMenuText(MENTXT_FACILITY_IMPROVEMENT_COMPLETED)[0], 						
						substringReplace(getMenuText(MENTXT_UPGRADE_HAS_BEEN_COMPLETED)[0],"[facility]",getMenuText(MENTXT_STADIUM_FACILITIES)[i]));				
				stadiumLevel[i]++;
				stadiumBuildStage[i] = 0;				
			}
				
		}
	}
//#endif	
	
	//#ifndef REM_TOP11
	league.calculateTopPlayers();
	//#endif	
	// END ZNR
	
}

public void seasonEndProcess() {

	// Check season goal ==============================================
	
	int teamPosition = league.getTeamLeaguePosition(league.userTeam);
	
	//com.mygdx.mongojocs.lma2007.Debug.println("You placed "+teamPosition+"st");
						
	
	// Update contracts info
	for(int i = 0; i < contracts.length; i++) {
	
		if(contracts[i][CONTRACT_DURATION] > 0) {
			
			contracts[i][CONTRACT_DURATION]--;
		}
		
		if(contracts[i][CONTRACT_DURATION] == 0 && league.userTeam.playerCount > 16) {
								
			// The player leaves the club beacause contract wasn't renewed
			
			int playerId = contracts[i][CONTRACT_PLAYERID];
			
			addMessage(MSG_FOO, substringReplace(getMenuText(MENTXT_PLAYER_LEAVE_MSG)[0], "[name]", league.playerGetName(playerId)), substringReplace(getMenuText(MENTXT_PLAYER_LEAVE_MSG)[1], "[name]", league.playerGetName(playerId)));
									
			Team t = league.findTeamByQuality(league.playerGetQuality(playerId), league.userTeam);
			
			t.addPlayer(playerId);
			
			league.userTeam.removePlayer(playerId);
			
			// TBD: Register the transfer
									
		}
	}
	
	for(int i = 1; i < 3;i++)
	{
		lastLeaguePosition[i] = lastLeaguePosition[i-1];		
	}
	
	lastLeaguePosition[0] = league.getTeamLeaguePosition(league.userTeam);
	
	
		
	// com.mygdx.mongojocs.lma2007.League winner!
	
	if(league.getTeamLeaguePosition(league.userTeam) == 0) {
						
		popupInitInfo(new String[] {getMenuText(MENTXT_WEARETHECHAMPIONS)[0], getMenuText(MENTXT_WEARETHECHAMPIONS)[2]});
		popupWait();
	}
	
	// Bonus codes =========================================================
	//#ifdef DEBUG
	Debug.println("FINAL DE LEAGUE: "+league.getTeamLeaguePosition(league.userTeam));
	//#endif
	
//#ifndef REM_BONUS_CODES
	if(!checkBonusCodes[BONUS_UNLIMITEDMONEY] && lastLeaguePosition[0] == 0 && lastLeaguePosition[1] == 0 && lastLeaguePosition[2] == 0) {
		
		checkBonusCodes[BONUS_UNLIMITEDMONEY] = true;
		
		popupInitInfo(new String[] {getMenuText(MENTXT_UNLOCKED_BONUS_CODE)[0], getMenuText(MENTXT_BONUS_UNLIMITED_FUNDS)[0]});
		popupWait();
	}
	
	if(!checkBonusCodes[BONUS_RAININMATCH] && league.getTeamLeaguePosition(league.userTeam) < 4) {
		
		checkBonusCodes[BONUS_RAININMATCH] = true;
		
		popupInitInfo(new String[] {getMenuText(MENTXT_UNLOCKED_BONUS_CODE)[0], getMenuText(MENTXT_BONUS_RAIN_IN_MATCH)[0]});
		popupWait();
	}
	
	if(!checkBonusCodes[BONUS_MOREFORMATIONS] && league.getTeamLeaguePosition(league.userTeam) < 6) {
		
		checkBonusCodes[BONUS_MOREFORMATIONS] = true;
		
		popupInitInfo(new String[] {getMenuText(MENTXT_UNLOCKED_BONUS_CODE)[0], getMenuText(MENTXT_BONUS_EXTRA_FORMATIONS)[0]});
		popupWait();
	}
	
	if(!checkBonusCodes[BONUS_TRAINING] && league.getTeamLeaguePosition(league.userTeam) < league.teams[league.userLeague].length - 4 && userTeamStartSeasonPosition >= league.teams[league.userLeague].length - 3) {
		
		checkBonusCodes[BONUS_TRAINING] = true;
		
		popupInitInfo(new String[] {getMenuText(MENTXT_UNLOCKED_BONUS_CODE)[0], getMenuText(MENTXT_BONUS_FAST_TRAINING)[0]});
		popupWait();
	}
//#endif
	

	userTeamStartSeasonPosition = league.getTeamLeaguePosition(league.userTeam);

	custCurrentAge++;
}


//#ifndef REM_FANTASY
public boolean fantasyTeamAutoComplete()
{
	short newTeam[] = new short[fantasyPlayersId.length];
	short playerId;
	int tries = 0;
	int bakCost = fantasyTotalCost;
	
	//#ifdef DEBUG
	Debug.println("Autocomplete fantasy team");
	//#endif
	
	do {
			
		System.arraycopy(fantasyPlayersId,0,newTeam,0,fantasyPlayersId.length);
		
		for(int i = 0; i < newTeam.length; i++) {
					
			int desiredPosition;
			
			switch(i) {
			
				case 0 : desiredPosition = 0; break;
				case 1 : 
				case 2 : 
				case 3 : 
				case 4 : desiredPosition = 1; break;
				case 5 : 
				case 6 : 
				case 7 : 
				case 8 : desiredPosition = 2; break;
				case 9 : 
				case 10: desiredPosition = 3; break;
				default: desiredPosition = 4; break;				
			}
		
			if(newTeam[i] < 0) {
			
				// Create candidate list
				
				short tempList[] = new short[1000];
				int tempCount = 0;
				
				for(int l = 0; l < league.teams.length; l++) {
				
					for(int j = 0; j < league.teams[l].length; j++) {
						
						for(int k = 0; k < league.teams[l][j].playerCount; k++) {
						
							playerId = league.teams[l][j].playerIds[k];
							
							if(tempCount < 1000 &&						  
							   (league.playerGetPosition(playerId) == desiredPosition || desiredPosition == 4) &&
							   !existsValueInArray(fantasyPlayersId,playerId)						   
							) {
								
								tempList[tempCount] = playerId;
								tempCount++;
								
							}
						}
					}						
				}
								
				// Pick a random player
				
				newTeam[i] = tempList[rnd(tempCount)];
				
				//#ifdef DEBUG
				Debug.println("Adding "+league.playerGetName(newTeam[i])+" to fantasy team!");
				//#endif
										
			}		
		}
		
		// Compute total cost
		
		fantasyTotalCost = 0;
		
		for(int i = 0; i < fantasyPlayersId.length; i++) {
			
			if(newTeam[i] >= 0) {
				
				fantasyTotalCost += playerCost(newTeam[i]); 
			}
		}
		
		//#ifdef DEBUG
		Debug.println("Candidate team cost: "+fantasyTotalCost+", total cash:"+coachCash);
		//#endif
		
		tries++;
		
	} while(fantasyTotalCost > coachCash && tries < 15); 
	
	if(tries < 15) {
	
		fantasyPlayersId = newTeam;
		
	} else {
	
		fantasyTotalCost = bakCost;
		return false;
	}
	
	return true;
	
}
//#endif


public int getAvailableCash()
{
	
	int totalIncome = clubIncome[0]+clubIncome[1]+clubIncome[2]+clubIncome[3];
	int totalExpenses = clubExpenses[0]+clubExpenses[1]+clubExpenses[2]+clubExpenses[3];
	
	return coachCash + totalIncome - totalExpenses;
}


// David END

//#ifndef REM_TEAMSHIELDS
Image shieldImg;
Image matchShieldsImg[] = new Image[2];
//#endif
Image menuIconsImg, menuIconsImg1, menuIconsImg2;
Image backgroundImg;

boolean borderTypeMenu;
Image borderTopImg;
Image borderBottomImg;
int borderBottomHeight;




League league = null;


void lmaInit()
{

//#ifdef DEBUG_PC_USED_MEM	
	showMemUsed("antes de crear com.mygdx.mongojocs.lma2007.League");
//#endif
//#ifdef DEBUG_TIME_PASSED
	showTimePassed("new league start");
//#endif

	league = new League(gc, gameText);
	
//#ifdef DEBUG_TIME_PASSED
	showTimePassed("new league end");
//#endif

	
//#ifdef DEBUG_PC_USED_MEM	
	showMemUsed("despu�s crear com.mygdx.mongojocs.lma2007.League");
//#endif
	
	
	league.init();
	
//#ifdef DEBUG_TIME_PASSED
	showTimePassed("league init end");
//#endif
//#ifdef DEBUG_PC_USED_MEM	
	showMemUsed("despu�s com.mygdx.mongojocs.lma2007.League.init");
//#endif

	
	//	HACK
	league.season--;
	selectTeam(0);

//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("antes gameVariablesStartup()");
//#endif
	
	gameVariablesStartup();
	
//#ifdef DEBUG_PC_USED_MEM	
	showMemUsed("despues gameVariablesStartup()");
//#endif

}


static final int ST_SELECTLEAGUE = 0;
static final int ST_SELECTTEAM = 1;
static final int ST_MAIN = 2;
int gameMode;
final static int CAREER = 0;
//#ifndef REM_FANTASY
final static int FANTASY = 1;
//#endif
final static int EXHIBITION = 2;


	


void selectTeam(int _i)
{
	//#ifdef DEBUG
	System.out.println("selectteam: "+_i+ " season: "+league.season);
	//#endif	
	
	boolean firstSeason = (league.season == 0);
	
	if (firstSeason/*_i != -1*/ || league.season == -1 ) //por bug de balfa
	{
		league.userTeam = league.teams[league.userLeague][_i];
		league.initExtendedPlayers(league.userTeam);
		
		//ESTADIO
		stadiumLevel = new int[10];
		stadiumBuildStage = new int[10];
		
		//TEAM STYLE
		squadStyle = 1;
		
		initTrainingSchedule();
		initStadium();		
	}
	
	
	
	//#ifndef REM_FANTASY	
	if (firstSeason && gameMode != FANTASY)
	//#else
	if (firstSeason)
	//#endif			 			
		coachCash = league.userTeam.cash * 1000000;
	
	
	//#ifndef REM_BONUS_CODES
	if(bonusCodes[BONUS_UNLIMITEDMONEY] != 0) {		
		coachCash = 999999999;		
	}
	//#endif
	
	if (firstSeason)
	{
		lastLeaguePosition[0] = lastLeaguePosition[1] = lastLeaguePosition[2] = -1;
		userTeamStartSeasonPosition = league.getTeamLeaguePosition(league.userTeam);
		//System.out.println("userTeamStartSeasonPosition: "+userTeamStartSeasonPosition);
	}

	
	league.startSeason();
	league.startJourney();

	
	
	//#ifndef REM_TEAMSHIELDS	
		shieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.userTeam.flagColor);
	//#endif

		

	//	 Season goal set
	
	int quality = league.userTeam.getQuality();
	
	//com.mygdx.mongojocs.lma2007.Debug.println("My quality: "+quality);
	
	if(quality < 45) {
		
		league.playerSeasonGoal = 4;
		
	} else if (quality < 50) {
		
		league.playerSeasonGoal = 3;
		
	} else if (quality < 58) {
		
		league.playerSeasonGoal = 2;
		
	} else if (quality < 65) {
	
		league.playerSeasonGoal = 1;
		
	} else {
		
		league.playerSeasonGoal = 0;
	}
	
	//	KILOS DE MAS EN VERANO
	for(int i = 0; i < league.userTeam.playerCount;i++)
		for(int j = 0; j < 4;j++)
			league.playerIncSkill(league.userTeam.playerIds[i], j, -2);
	
}





// *******************
// -------------------
// titleBar - Engine
// ===================
// *******************

boolean titleBarShow;
String menuListTitleA;
String menuListTitleB;
String menuListTitleAold;
String menuListTitleBold;
int titleAScrollX;
int titleBScrollX;
boolean titleAScroll;
boolean titleBScroll;
int titleAOffsetX;
int titleBOffsetX;
int titleAreaWidth;
int titleAreaHeight;
int titleAreaX;
int titleScrollDelay;
final static int TITLE_SCROLL_DELAY = 16;


//Autodetecta y hace refresh si:
// - Hay un cambio en los titulos
// - El titulo se pasa del area reservada
//	  - Hay un cambio en el form
//Si falla diselo a: ZNR

// -------------------
// titleBar Tick
// ===================
//#ifndef TITLE_BAR_ONE_LINE
public void titleBarTick()
{
	
	fontInit(FONT_BIG_BLACK);
	
	titleAreaX = SHIELD_WIDTH + (FORM_SEPARATOR*2);
	titleAreaWidth = canvasWidth - titleAreaX - FORM_SEPARATOR;

	//#ifdef REM_BORDER_TOP_IMAGE
	//#else
	if (borderTopImg != null)	
		titleAreaHeight = borderTopImg.getHeight();
	//#endif
	
	
	boolean changed = false;
	
	if (menuListTitleA != menuListTitleAold) changed = true;
	if (menuListTitleB != menuListTitleBold) changed = true;
	
	menuListTitleAold = menuListTitleA;
	menuListTitleBold = menuListTitleB;
	
	if (titleScrollDelay > 0) titleScrollDelay--; 
	
	if (!changed)
	{
		if (menuListTitleA != null && printStringWidth(menuListTitleA) > titleAreaWidth)
		{
			titleAOffsetX = printStringWidth(menuListTitleA) + (canvasWidth/3);
			if (titleScrollDelay == 0) titleAScrollX -= 3;
			titleAScroll = true;
			if (titleAScrollX + titleAOffsetX < 0)
				titleAScrollX += titleAOffsetX;
			titleBarShow = true;
		}
	
	
		if (menuListTitleB != null && printStringWidth(menuListTitleB) > titleAreaWidth)
		{
			titleBOffsetX = printStringWidth(menuListTitleB) + (canvasWidth/3);
			if (titleScrollDelay == 0) titleBScrollX -= 3;
			titleBScroll = true;
			if (titleBScrollX + titleBOffsetX < 0)
				titleBScrollX += titleBOffsetX;
			titleBarShow = true;
		}
	}


	if (changed)
	{		
		titleAScroll = false;
		titleBScroll = false;
		titleBarShow = true;
		titleAScrollX = titleBScrollX = 0;
		titleAOffsetX = titleBOffsetX = 0;
		
		if (menuListTitleA != null && printStringWidth(menuListTitleA) > titleAreaWidth)
		{
			titleAScrollX = printStringWidth(menuListTitleA) - titleAreaWidth;			
			//titleAScroll = true;			
		}
		
		if (menuListTitleB != null && printStringWidth(menuListTitleB) > titleAreaWidth)
		{
			titleBScrollX = printStringWidth(menuListTitleB) - titleAreaWidth;
			//titleBOffsetX = 0;
			//titleBOffsetX = printStringWidth(menuListTitleB) + (canvasWidth/3);
			
			//titleBScroll = true;
			//titleBarTick();
		}
		
		titleScrollDelay = TITLE_SCROLL_DELAY;
	}



	if (formShow)
	{
		titleBarShow = true;
	}
	fontInit(FONT_WHITE);
}
//#else
//#endif

// -------------------
// titleBar Draw
// ===================

//#ifndef TITLE_BAR_ONE_LINE

public void titleBarDraw()
{
	titleBarTick(); //ZNR (JUAN NO ME PEGUES)

	
	if (!titleBarShow) return;
	if (menuListTitleA == null && menuListTitleB == null) return;
	
//#ifdef REM_BORDER_TOP_IMAGE
//#else
	if (borderTopImg != null)
	{
		imageDraw(borderTopImg, 0, 0);
	}
	//#ifndef REM_TEAMSHIELDS
		if (!borderTypeMenu && shieldImg != null) 
		{
			cellDraw(shieldImg, 3, 3,  league.userLeague,  SHIELD_WIDTH, SHIELD_HEIGHT);	// cellDraw(x,y, cell, width,height)
		}
	//#endif
//#endif
	
	if (!borderTypeMenu && menuListTitleA != null)
	{
		fontInit(FONT_BIG_GREY);
		printSetArea(titleAreaX, 1, titleAreaWidth, printGetHeight());
		printDraw(menuListTitleA, titleAScrollX, 0, 0x000000, PRINT_VCENTER|PRINT_MASK|PRINT_RIGHT);
		if (titleAScroll)
			printDraw(menuListTitleA, titleAScrollX + titleAOffsetX, 0, 0x000000, PRINT_VCENTER|PRINT_MASK|PRINT_RIGHT);
	}

	if (menuListTitleB != null)
	{
		fontInit(FONT_BIG_BLACK);
		int flags;
		if (borderTypeMenu)
		{
			//System.out.println("111");
			printSetArea(12, 11, canvasWidth, printGetHeight());
			flags = PRINT_VCENTER|PRINT_MASK|PRINT_LEFT;
			//if (titleBScroll) flags = PRINT_VCENTER|PRINT_MASK|PRINT_RIGHT;
			printDraw(menuListTitleB, titleBScrollX, 0, 0x000000, flags);
		} else {
			//System.out.println("222");
			flags = PRINT_VCENTER|PRINT_MASK|PRINT_RIGHT;
			printSetArea(titleAreaX, 2+printGetHeight(), titleAreaWidth, printGetHeight());
			printDraw(menuListTitleB, titleBScrollX, 0, 0x000000, flags);
		}

		if (titleBScroll)
			printDraw(menuListTitleB, titleBScrollX + titleBOffsetX, 0, 0x000000, flags);
	}
	
	fontInit(FONT_WHITE);

	titleBarShow = false;	
	titleAScroll = false;
	titleBScroll = false;
}
//#else
//#endif


//<=- <=- <=- <=- <=-










// *******************
// -------------------
// menuBar - Engine
// ===================
// *******************


//#ifdef S30

//#elifdef S40

//#elifdef S60
final static int ICONOFFSET = 2;
final static int MENU_ICONS_HEIGHT = 19;
final static int MENU_ICONS_GFXHEIGHT = 15;

final static int MENU_ICONS_CALENDAR_WIDTH = 21;
final static int MENU_ICONS_CALENDAR_HEIGHT = 15;

//#elifdef S80

//#endif



final static int ICONSOFFSETY 	= 6;
final static int TITLEBORDER 	= 2;
final static int SUBMENU_COLORS[] = 
	{0xE99F58, 0xA9C559, 0xA898C6, 0xC3716F, 0x85A6D3, 0x66C48F, 
	//#ifndef REM_STADIUM
	0xBFB448, 
	//#endif
	0xdF8958, 0x6AB4BA, 
	//#ifndef FEA_EA
	LISTENER_BAR_RGB};
	//#else
	//#endif

//#ifdef LESS_ICONS
//#else

final static int ICON_SQUAD = 0;
final static int ICON_TRAINING = 1;
final static int ICON_LAPTOP = 2;
final static int ICON_INFO = 3;
final static int ICON_TRANSFERS = 4;
final static int ICON_FINANCE = 5;
final static int ICON_STADIUM = 6;
final static int ICON_OPTIONS = 7;
final static int ICON_CALENDAR = 8;
final static int ICON_SOUND = 9;
final static int ICON_SAVE = 10;

final static int ICON_TACTICS = 12;
final static int ICON_FORMATION = 12;
final static int ICON_STYLE = 13;
final static int ICON_INJURED = 14;
final static int ICON_SUSPENDED = 11;
final static int ICON_SCHEDULE = 1;
final static int ICON_TABLE = 15;
final static int ICON_GRAPH = 16;
final static int ICON_TOP = 17;
final static int ICON_SEARCH = 18;
final static int ICON_SELL = 19;
final static int ICON_PRICE = 20;
final static int ICON_SPONSOR = 21;
final static int ICON_QUIT = 22;
final static int ICON_CHAMPIONS = 23;

final static int MENU_ICONS_ICONLINE = 12;
//#endif

final static int MAIN_MENU_ICONS[] = {
	ICON_SQUAD,
	ICON_TRAINING,	
	ICON_LAPTOP,
	ICON_INFO,
	ICON_TRANSFERS,
	ICON_FINANCE,
//#ifndef REM_STADIUM
	ICON_STADIUM,
//#endif
	ICON_OPTIONS,
	ICON_CALENDAR};


final static int SUBMENU_ICONS[][] = 
{
	{ICON_SQUAD, 
//#ifndef REM_SQUAD_TACTICS
		ICON_TACTICS, 
//#endif		
		ICON_FORMATION, ICON_STYLE, ICON_INJURED, ICON_SUSPENDED, ICON_OPTIONS},
	{ICON_TRAINING, ICON_STYLE},
	{ICON_LAPTOP},
	{ICON_TABLE,ICON_TABLE,
//#ifndef REM_LEAGUE_GRAPHIC
		ICON_GRAPH,		
//#endif
		ICON_CHAMPIONS,ICON_INFO,
		//#ifndef REM_TOP11
		ICON_TOP,
		//#endif
		//#ifndef REM_TOP_SCORERS
		ICON_TOP,
		//#endif
		ICON_INFO},
	{ICON_SEARCH,
//#ifndef REM_TRANSFERS_FILTER			
	ICON_SEARCH,
//#endif
//#ifndef REM_TRANSFERS_SCOUT		
	ICON_SEARCH,
//#endif
	ICON_SELL},
	
	{
	 ICON_FINANCE, 
//#ifndef REM_FINANCES_PRICESSET	 
	 ICON_PRICE,
//#endif
//#ifndef REM_FINANCES_EMPLOYEES
	 ICON_TABLE,
//#endif
	 ICON_SPONSOR,
//#ifndef REM_FINANCES_ATTENDANCE	 
	 ICON_GRAPH,
//#endif
	 },
//#ifndef REM_STADIUM
	{ICON_STADIUM},
//#endif
	{ICON_INFO, ICON_SOUND, ICON_SAVE, ICON_QUIT},
	{ICON_CALENDAR}
};


boolean menuBarShow;
boolean menuBarRefresh;
boolean menuBarRequest;
boolean menuBarEngine;
int option = 0;
int subOption = 0;
int cursorY;


int subMenuTable[][] = {	
	{0, 
		//#ifndef REM_SQUAD_TACTICS
		1, 
		//#endif
		2, 3, 4, 5, 6},
	{0, 1},
	{0},
	{0, 1,
		//#ifndef REM_LEAGUE_GRAPHIC
		2, 
		//#endif
		3, 4, 
		//#ifndef REM_TOP_SCORERS
		5, 
		//#endif
		//#ifndef REM_TOP11
		6, 
		//#endif
		7},
	{0, 
			//#ifndef REM_TRANSFERS_FILTER
			1,
			//#endif
			//#ifndef REM_TRANSFERS_SCOUT
			2,
			//#endif
			3},
	{0, 
				//#ifndef REM_FINANCES_PRICESSET
				1,
				//#endif
				//#ifndef REM_FINANCES_EMPLOYEES
				2, 
				//#endif
				3, 
				//#ifndef REM_FINANCES_ATTENDANCE
				4
				//#endif
				},
//#ifndef REM_STADIUM			
	{0},
//#endif
	{0, 1, 2, 3},
	{0}
};

// -------------------
// menuBar Draw
// ===================

void menuBarDraw()
{
	if (!menuBarRefresh)
	{
		return;
	}
	menuBarRefresh = false;


// Pintamos background
//#ifdef REM_BACKGROUND_IMAGE
//#else
	if (backgroundImg != null)
	{
		imageDraw(backgroundImg);
	} else {
		fillDraw(0x003300, 0, 0, canvasWidth, canvasHeight);
	}
//#endif


// Forzamos el pintado de la barra de titulo
	titleBarShow = true;


// Si la barra de menus esta activa y esta en funcionamiento, actualizamos titulos y calendario
	if (menuBarEngine && biosStatus == BIOS_MENUBAR)
	{
		//#ifdef REM_STADIUM
		//#else
		menuListTitleA = gameText[TEXT_BAR1][option];
		menuListTitleB = gameText[TEXT_BAR1+option+1][subMenuTable[option][subOption]];		
		//#endif
		
		//calendarDraw();		
		// PARA JUAN
		
		printSetArea(2, titleAreaHeight + FORM_SEPARATOR, canvasWidth-4, canvasHeight - (titleAreaHeight + FORM_SEPARATOR) - ((borderBottomHeight - listenerHeight) + (MENU_ICONS_HEIGHT * 2)));

		fontInit(FONT_WHITE);

	//#ifndef REM_CALENDAR
		int cursor = printFontHeight/2;
	//#else
	//#endif


		int begin = 0;

	// Generamos todas las lineas a mostrar

//#ifdef S30
//#else

		String[] tmp2Str = new String[4];
		//#ifndef REM_CALENDAR
		tmp2Str[0] = getFormattedDate();
		//#else
		//#endif
		tmp2Str[1] = league.userTeam.name;		
		tmp2Str[2] = getMenuText(MENTXT_CLUB_FUNDS)[0]+": " + moneyStr(getAvailableCash(), false) + moneyUnit();
		tmp2Str[3] = getMenuText(MENTXT_JOURNEYTYPES)[league.journeyType]+": "
		          +league.userMatch[0].name
		          +getMenuText(MENTXT_VERSUSABBR)[1]
		          +league.userMatch[1].name;
//#endif










	// Partimos todos los textos para que no se salgan de la pantalla
		tmp2Str = printTextBreak(tmp2Str, canvasWidth - (FORM_SEPARATOR*2));


	// Calculamos la linea mas larga
		int ancho = 0;
		for (int i = begin ; i<tmp2Str.length ; i++)
		{
			int width = printStringWidth(tmp2Str[i]); if (ancho < width) {ancho = width;}
		}
		ancho += printX + FORM_SEPARATOR;



	// Pintamos todas las lineas
		for (int i = begin ; i<tmp2Str.length ; i++)
		{
			fillDraw(0x424242, 0, printY + cursor, ancho, printFontHeight);
			fillDraw(0x636363, 0, printY + cursor + printFontHeight - 1, ancho, 1);
			printDraw(tmp2Str[i], 0, cursor, fontPenRGB, PRINT_LEFT|PRINT_TOP);
			cursor += printFontHeight;
		}

	// Dejamos una linea vacia
	//#ifndef REM_CALENDAR
		cursor += printFontHeight;
	//#endif

		String[] tmpStr = printTextBreak(getMenuText(MENTXT_SEASONGOALS)[league.playerSeasonGoal], printWidth);

	//#ifndef REM_CALENDAR
	// Si hay desbordamiento, no dejamos linea de separacion para el "expectation"
		if (printY + cursor + ((tmpStr.length+1)*printFontHeight) > canvasHeight - (borderBottomHeight - listenerHeight) - (MENU_ICONS_HEIGHT * 2)) {cursor -= printFontHeight;}
	//#endif

	// Pintamos linea final, la del espectation
//#ifndef S30
		fontInit(FONT_ORANGE);
		if (tmpStr.length < 3)
		{
			fillDraw(0x000000, 0, printY + cursor, printX + printStringWidth(getMenuText(MENTXT_EXPECTATION)[0]) + FORM_SEPARATOR, printFontHeight);
			printDraw(getMenuText(MENTXT_EXPECTATION)[0], 0, cursor, fontPenRGB, PRINT_LEFT|PRINT_TOP);
			cursor += printFontHeight;
		}
//#endif

		for (int i=0 ; i<tmpStr.length ; i++)
		{
			fillDraw(0x424242, 0, printY + cursor + (i*printFontHeight), canvasWidth, printFontHeight);
			fillDraw(0x636363, 0, printY + cursor + (i*printFontHeight) + printFontHeight - 1, canvasWidth, 1);
		}
		if (league.seasonGoalAcomplished()) fontInit(FONT_WHITE);
		printDraw(tmpStr, 0, cursor, fontPenRGB, PRINT_LEFT|PRINT_TOP);

/*		
		com.mygdx.mongojocs.lma2007.Debug.println("Fecha: "+dateStr());
		com.mygdx.mongojocs.lma2007.Debug.println("Pasta: "+moneyStr(coachCash, false) + moneyUnit());
		com.mygdx.mongojocs.lma2007.Debug.println("Jornada: " + journeyTypeStr[league.journeyType]);
		com.mygdx.mongojocs.lma2007.Debug.println("Expectation: " + targetsStr[league.playerSeasonGoal]);
		com.mygdx.mongojocs.lma2007.Debug.println("Mail: " + unreadMail());
*/

		if (unreadMail())
		{
			printDraw(formItemsImg, -FORM_SEPARATOR, -FORM_SEPARATOR, ITEM_MAIL, PRINT_RIGHT|PRINT_BOTTOM, formItemsCor);
		}

	}				


// Pintamos la barra de menus	
	if (menuBarShow)
	{
	// Calculamos ancho y alto de menuBar
		int NI = MAIN_MENU_ICONS.length;
		int w = (MENU_ICONS_HEIGHT * NI);
		int menuBarHeight = (borderBottomHeight - listenerHeight) + (MENU_ICONS_HEIGHT * 2);
		
	// Calculamos eje para pintar ICONS superiores
		int x = (canvasWidth-(MENU_ICONS_HEIGHT*NI))/2;
		int y =  canvasHeight - menuBarHeight;

	// Limpiamos base de los ICONS superiores
		fillDraw(0xffffff, 0, y, canvasWidth, MENU_ICONS_HEIGHT);

	// Pintamos opcion activa en ICONS superiores
		
		fillDraw(SUBMENU_COLORS[option], x + (option*MENU_ICONS_HEIGHT), y, MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT);		
		
	// Limpiamos base de los ICONS inferiores
		fillDraw(SUBMENU_COLORS[option], 0, y + MENU_ICONS_HEIGHT, canvasWidth, MENU_ICONS_HEIGHT);

	// Pintamos opcion activa en ICONS inferiores
		//
		if (cursorY == 1)
			fillDraw(0x606060, x+(NI-SUBMENU_ICONS[option].length+subOption)*MENU_ICONS_HEIGHT, y + MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT);
		//

	// Pintamos todos los ICONS superiores
		for(int i = 0; i < MAIN_MENU_ICONS.length;i++)
		{
			//#ifndef OLD_MENU_BAR
			if (option == i && cursorY == 0) menuIconsImg = menuIconsImg2;
			else menuIconsImg = menuIconsImg1;
			//#else
			menuIconsImg = menuIconsImg1;
			//#endif	
			imageDraw(menuIconsImg, (MAIN_MENU_ICONS[i]%MENU_ICONS_ICONLINE)*MENU_ICONS_GFXHEIGHT, (MAIN_MENU_ICONS[i]/MENU_ICONS_ICONLINE)*MENU_ICONS_GFXHEIGHT,	MENU_ICONS_GFXHEIGHT, MENU_ICONS_GFXHEIGHT,	x+ICONOFFSET, y+ICONOFFSET);
			
			//if (MAIN_MENU_ICONS_DEACTIVATED[i])
			//    alphaFillDraw(0x80000000,x, y, MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT);                           

			x += MENU_ICONS_HEIGHT;
		}
				
		
	// Calculamos eje para pintar ICONS inferiores
		x = ((canvasWidth-(MENU_ICONS_HEIGHT*NI))/2) + (NI-SUBMENU_ICONS[option].length)*MENU_ICONS_HEIGHT;
		y += MENU_ICONS_HEIGHT;

	// Pintamos todos los ICONS inferiores
		for(int i = 0; i < SUBMENU_ICONS[option].length;i++)
		{
			//#ifndef OLD_MENU_BAR
			if (subOption == i && cursorY == 1) menuIconsImg = menuIconsImg2;
			else menuIconsImg = menuIconsImg1;
			//#endif
			imageDraw(menuIconsImg, (SUBMENU_ICONS[option][i]%MENU_ICONS_ICONLINE)*MENU_ICONS_GFXHEIGHT, (SUBMENU_ICONS[option][i]/MENU_ICONS_ICONLINE)*MENU_ICONS_GFXHEIGHT ,MENU_ICONS_GFXHEIGHT, MENU_ICONS_GFXHEIGHT, x+ICONOFFSET , y+ICONOFFSET);
			
			//if (SUBMENU_ICONS_DEACTIVATED[option][i])
			//    alphaFillDraw(0x80000000,x, y, MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT);                           
			
			x += MENU_ICONS_HEIGHT;
		}
		
		//#ifdef OLD_MENU_BAR
		
	// Calculamos eje del cursor
		if (cursorY == 0){
			x = ((canvasWidth-(MENU_ICONS_HEIGHT*NI))/2)+(option*MENU_ICONS_HEIGHT);
			y -= MENU_ICONS_HEIGHT;
		} else {
			x = ((canvasWidth-(MENU_ICONS_HEIGHT*NI))/2)+(NI-SUBMENU_ICONS[option].length+subOption)*MENU_ICONS_HEIGHT;
		}

		// Pintamos cursor		
		
		rectDraw(0x000000, x-2, y-2, MENU_ICONS_HEIGHT+3, MENU_ICONS_HEIGHT+3);
		rectDraw(0x000000, x-1, y-1, MENU_ICONS_HEIGHT+1, MENU_ICONS_HEIGHT+1);
		//#endif
	}

}


// -------------------
// menuBar Update
// ===================

void menuBarTick()
{	
	int eventX = 0, eventY = 0;

	if (!menuBarShow)
	{
		return;
	}
	
	
	if (keyX != 0 && lastKeyX == 0) {eventX = keyX;menuBarRefresh = true;}
	if (keyY != 0 && lastKeyY == 0) {eventY = keyY;menuBarRefresh = true;}
	
	if (eventY != 0)  		
		cursorY = 1-cursorY;
	
	if (cursorY == 0) 
	{
		if (eventX > 0)
		{
			option++;
			subOption = 0;			
		}
		if (eventX < 0)
		{
			option--;
			subOption = 0;
		}
		
		if (option >= MAIN_MENU_ICONS.length) option = 0; 
		if (option < 0 ) option = MAIN_MENU_ICONS.length - 1;			
	}
	else
	{
		if (eventX > 0)
			subOption = (subOption + 1) % SUBMENU_ICONS[option].length;
		if (eventX < 0)
			subOption = (SUBMENU_ICONS[option].length + subOption - 1) % SUBMENU_ICONS[option].length;
	}

	if (keyMenu > 0 && lastKeyMenu == 0) menuBarAction(option, subOption);

}



final static int MENU_SQUAD_ACTIONS[] = {	
	MENU_SQUAD_SELECT_TEAM,
//#ifndef REM_SQUAD_TACTICS
	MENU_SQUAD_TACTICS,
//#endif
	MENU_SQUAD_FORMATION,
	MENU_SQUAD_STYLE,
	MENU_SQUAD_INJURED,
	MENU_SQUAD_SUSPENDED,
	MENU_SQUAD_EDIT_NAMES};

final static int MENU_INFO_ACTIONS[] = {
	MENU_INFO_LEAGUE_TABLE1,
	MENU_INFO_LEAGUE_TABLE2,
//#ifndef REM_LEAGUE_GRAPHIC
	MENU_INFO_LEAGUE_GRAPHIC,
//#endif
	MENU_INFO_CHAMPIONS,
	MENU_INFO_CLUB_DETAILS,
	//#ifndef REM_TOP_SCORERS
	MENU_INFO_TOP_SCORERS,
	//#endif
	//#ifndef REM_TOP11
	MENU_INFO_TOP11,
	//#endif
	MENU_INFO_MANAGER
};

final static int MENU_TRANSFER_ACTIONS[] = {
	MENU_TRANSFERS_TEAM_SELECT,
//#ifndef REM_TRANSFERS_FILTER	
	MENU_TRANSFERS_FILTER_SEARCH,
//#endif
//#ifndef REM_TRANSFERS_SCOUT	
	MENU_TRANSFERS_SCOUTS,
//#endif	
	MENU_TRANSFERS_SELL
	};

final static int MENU_FINANCE_ACTIONS[] = {
	MENU_FINANCE_BANK_INFO,
//#ifndef REM_FINANCES_PRICESSET	
	MENU_FINANCE_PRICES_SET,
//#endif
//#ifndef REM_FINANCES_EMPLOYEES
	MENU_FINANCE_EMPLOYEE_LIST,
//#endif
	MENU_FINANCE_SPONSORS,
//#ifndef REM_FINANCES_ATTENDANCE	
	MENU_FINANCE_ATTENDANCE,
//#endif
	};



final static int MENU_BAR_ACTION_SQUAD = ICON_SQUAD;
final static int MENU_BAR_ACTION_TRAINING = ICON_TRAINING;
final static int MENU_BAR_ACTION_LAPTOP = ICON_LAPTOP;
final static int MENU_BAR_ACTION_INFO = ICON_INFO;
final static int MENU_BAR_ACTION_TRANSFERS = ICON_TRANSFERS;
final static int MENU_BAR_ACTION_FINANCE = ICON_FINANCE;
//#ifdef REM_STADIUM
//#else
final static int MENU_BAR_ACTION_STADIUM = ICON_STADIUM;
final static int MENU_BAR_ACTION_OPTIONS = ICON_OPTIONS;
final static int MENU_BAR_ACTION_CALENDAR = ICON_CALENDAR;
//#endif
									
// -------------------
// menuBar Action
// ===================

void menuBarAction(int _option, int _subOption)
{
// Reseteamos cache de menus para que cuando desbordemos la cache, regresemos al menuBar... (menuType = -1) ;-)
	menuStackIndex = 1;
	menuStack[0][0] = -1;


	switch(_option)
	{
		case MENU_BAR_ACTION_SQUAD:
			menuInit(MENU_SQUAD_ACTIONS[_subOption]);
			menuBarShow = false;
			break;


		case MENU_BAR_ACTION_TRAINING:
			switch(subOption)
			{
				case 0:	menuInit(MENU_TRAINING_LINE); break;
				case 1:	menuInit(MENU_TRAINING_STYLE); break;
			}
			menuBarShow = false;
			break;

		case MENU_BAR_ACTION_LAPTOP:
			menuInit(MENU_LAPTOP);
			menuBarShow = false;
			break;


		case MENU_BAR_ACTION_INFO:				
			menuInit(MENU_INFO_ACTIONS[_subOption]);
			menuBarShow = false;
			break;


		case MENU_BAR_ACTION_TRANSFERS:
			menuInit(MENU_TRANSFER_ACTIONS[_subOption]);
			break;


		case MENU_BAR_ACTION_FINANCE:
			menuInit(MENU_FINANCE_ACTIONS[_subOption]);			
			menuBarShow = false;
			break;

//#ifndef REM_STADIUM
		case MENU_BAR_ACTION_STADIUM:
				menuInit(MENU_STADIUM);
				menuBarShow = false;
				break;
//#endif

		case MENU_BAR_ACTION_OPTIONS:
			switch(subOption)
			{
			// Menu de Ayuda
				case 0:
					menuInit(MENU_HELP);
					menuBarShow = false;
				break;

				case 1:
					menuInit(MENU_SOUND);
					menuBarShow = false;
/*
					gameSound = !gameSound;

					if (!gameSound) {soundStop();} else {soundPlay(0,0);}

					popupInitAsk(null, new String[] {getMenuText(MENTXT_SOUND)[0]+" "+(gameSound? getMenuText(MENTXT_ENABLED)[0]:getMenuText(MENTXT_DISABLED)[0])}, SOFTKEY_NONE, SOFTKEY_NONE);
					forceRender();
					waitTime(1000);
					popupRelease();
*/
				break;

				case 2:

					popupInitAsk(getMenuText(MENTXT_SAVE_GAME), SOFTKEY_NO, SOFTKEY_YES);
					popupWait();

					if (popupResult)
					{
						waitStart();
						popupInitAsk(getMenuText(MENTXT_SAVING_DATA), SOFTKEY_NONE, SOFTKEY_NONE);
						forceRender();
						saveGame();
						waitStop(1200);
						popupRelease();
					}
				break;

			// Salir del juego?
				case 3:
					popupInitAsk(gameText[TEXT_ARE_YOU_SURE], SOFTKEY_NO, SOFTKEY_YES);
					popupWait();
			
					if (popupResult)
					{
						listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

					// Mostramos y salvamos preferencias

						popupInitAsk(getMenuText(MENTXT_SAVE_GAME), SOFTKEY_NO, SOFTKEY_YES);
						popupWait();

						if (popupResult)
						{
							waitStart();
							popupInitAsk(getMenuText(MENTXT_SAVING_DATA), SOFTKEY_NONE, SOFTKEY_NONE);
							forceRender();
							saveGame();
							waitStop(1200);
							popupRelease();
						} else {
							loadGame();
						}

					// Salimos del juego al menu principal
						biosStatus = BIOS_GAME;
						gameStatus = GAME_MENU_START;
					}
				break;
			}
//			menuBarShow = false;
			break;


		case MENU_BAR_ACTION_CALENDAR: 
			
				if(playerSquadOk()) {
			
					menuBarRequest = false;

				//#ifndef REM_CALENDAR
					menuInit(MENU_CALENDAR_MATCHES);
				//#else
				//#endif

					menuBarShow = false;
										
				} else {

					menuInit(MENU_SQUAD_CHANGE_NEEDED);
				}
				break;
				
		/*default:
				menuInit(MENU_FOO);
				break;*/
	}

	menuBarRefresh = true;
}

//<=- <=- <=- <=- <=-










// *******************
// -------------------
// Calendar - Engine
// ===================
// *******************
//#ifndef REM_CALENDAR
public void calendarDraw()
{	
	fontInit(FONT_WHITE);

	if (league.currentWeek == league.leagueJourneys.length)
	{
		printSetArea(0, 0, canvasWidth, canvasHeight);
		
		
		printDraw(getMenuText(MENTXT_THE_LEAGUE_WINNER_IS)[0]+" "+league.teams[league.userLeague][0].name, 0,
		//#ifdef REM_BORDER_TOP_IMAGE
		//#else
		borderTopImg.getHeight() + 5,
		//#endif
		0xffffff, PRINT_HCENTER|PRINT_TOP|PRINT_OUTLINE);
		
		return;
	}
	

// Pintamos fecha	
	String str = getFormattedDate();
	//#ifdef REM_BORDER_TOP_IMAGE
	//#else
	printSetArea(0, borderTopImg.getHeight() + FORM_SEPARATOR, canvasWidth, printGetHeight()*2);
	//#endif
	printDraw(str, 0, 0, 0xffffff, PRINT_HCENTER|PRINT_VCENTER|PRINT_OUTLINE|PRINT_BREAK);


// Pintamos calendario
	int start = Math.max(league.currentWeek-2, 0);
	
	league.getCurrentDate(start, 0);
	int dia = league.lastDay;

	
	int offsetX = (canvasWidth - (MENU_ICONS_CALENDAR_WIDTH*7)) / 2;
	int offsetY = printY + printHeight + FORM_SEPARATOR;
	
	int curMonth = league.getMonth(start, 0);
	//int dia = league.CalcFirstOfMonth(com.mygdx.mongojocs.lma2007.League.FIRSTWEEKYEAR + league.season - 1, curMonth)-1;

	
	int uMonth = league.getMonth(league.currentWeek, league.currentJourney%2 == 0?0:3);
	fontInit(FONT_WHITE);

	for(int i=0 ; i<5 ; i++)
	{
		for(int j = 0; j < 7;j++)
		{			
			int m = league.getMonth(start+i, j);
			if (curMonth != m)
			{
				dia = 1;
				curMonth = m;
			}
			
			int spr = 0;
			
		// Es copa de europa o champion????
			if (j == 6) {spr += 2;}
			else
			if (j == 2 && league.playingChamp && league.isChampJourney(((start+i)*2))) {spr++;}

		// ES HOY?
			if (start+i == league.currentWeek && j == (league.currentJourney%2 == 0?0:3)) {spr += 3;}

			spriteDraw(formItemsImg, offsetX+j*MENU_ICONS_CALENDAR_WIDTH, offsetY+i*MENU_ICONS_CALENDAR_HEIGHT, ITEM_CALENDAR + spr, formItemsCor);

			printSetArea(offsetX+j*MENU_ICONS_CALENDAR_WIDTH, offsetY+i*MENU_ICONS_CALENDAR_HEIGHT, MENU_ICONS_CALENDAR_WIDTH, MENU_ICONS_CALENDAR_HEIGHT);

			fontInit(uMonth != curMonth?FONT_WHITE:FONT_BLACK);
			
			printDraw(" "+dia, 0, 0, fontPenRGB, PRINT_TOP);

			dia++;
		}
	}


// Pintamos opcion Ciclica si existe
	formCicleDraw(0, offsetY + (5 * MENU_ICONS_CALENDAR_HEIGHT), canvasWidth, printFontHeight, printFontHeight);

}
//#endif
//<=- <=- <=- <=- <=-









// *******************
// -------------------
// Messaging - Engine
// ===================
// *******************
//#ifdef LESS_MESSAGES
//#else
final static int MAXMESSAGES = 15;
//#endif
String messageTitle[] = new String[MAXMESSAGES];
String messageBody[] = new String[MAXMESSAGES];
int messageType[] = new int[MAXMESSAGES];
int messageAge[] = new int[MAXMESSAGES];
int messageData[][] = new int[MAXMESSAGES][];

int messageCount;
int currentMessage;

final static int MSG_INVALID = -1;
final static int MSG_FOO = 0;
final static int MSG_BUY_OFFER = 2;
final static int MSG_NEGOTIATE_OFFER = 3;
final static int MSG_NEGOTIATE_ACCEPT = 4;
final static int MSG_SHIRT_SPONSOR_OFFER = 5;
final static int MSG_FENCE_SPONSOR_OFFER = 6;
final static int MSG_CONTRACT_END = 7;
final static int MSG_FOOTBALLONE = 8;
final static int MSG_SPONSOR_NOTIFY = 9;

public void initMessaging()
{
	messageCount = 0;
	
	for(int i = 0; i < MAXMESSAGES; i++)
	{
		messageType[i] = MSG_INVALID;
	}
}

public void addMessage(int _type, String _title, String _body)
{
	addMessage(_type, _title, _body, null);
}


public void addMessage(int _type, String _title, String _body, int[] _data)
{
	//com.mygdx.mongojocs.lma2007.Debug.println("BEFORE: "+messageCount);
	
	for(int i = 0; i < MAXMESSAGES; i++)	
		messageAge[i]++;
	
	if (messageCount == MAXMESSAGES) deleteOldestMessage();
	
	messageType[messageCount] = _type;
	messageTitle[messageCount] = _title;
	messageBody[messageCount] = _body;
	messageAge[messageCount] = 0;
	messageData[messageCount] = _data;
	messageCount++;
		
	sortMessagesByAge();
	
}


public boolean unreadMail()
{
	boolean unread = false;
	
	for(int i = 0; i < MAXMESSAGES; i++) {
		if (messageType[i] != MSG_INVALID) {
			if ((messageAge[i] & 0x800000) == 0) unread = true; 
		}
	}
	return unread;
}


public void readMessage(int i)
{
	messageAge[i] = messageAge[i]|0x800000;
	currentMessage = i;


//#ifndef REM_FOOTBALL_ONE
	if (messageType[i] != MSG_FOOTBALLONE)
	{
//#endif
		menuInit(MENU_READMESSAGE);
//#ifndef REM_FOOTBALL_ONE
	} else {
		footballOneStr = new String[] {messageTitle[i], messageBody[i]};
		menuInit(MENU_FOOTBALL_ONE);
	}
//#endif
}


public void deleteOldestMessage()
{
	//Asumo que el vector esta lleno
	//Por lo tanto todos los mensajes son validos
	
	int oldest = 0;
	
	for(int i = 1; i < MAXMESSAGES; i++)
	{
		if (messageAge[i] > messageAge[oldest]) oldest = i;	
	}
	
	deleteMessage(oldest);
}



public void deleteMessage(int i)
{
	if (messageType[i] != MSG_INVALID)
	{
		messageCount--;
		messageType[i] = MSG_INVALID;
	}
	sortMessagesByAge();
}


public void deleteMessages()
{
	for(int i = 0; i < MAXMESSAGES;i++)
		messageType[i] = MSG_INVALID;
	
	messageCount = 0;
}

public void sortMessagesByAge()
{
	int i = 0;
	
	//PACK MESSAGES
	while (i < MAXMESSAGES-1)
	{
		if (messageType[i] == MSG_INVALID) moveMessage(i, i+1);			
		i++;
	}
	
	//SORT BY AGE 
	for(i = 0;i < MAXMESSAGES-1;i++)
	{		
		for(int j = i+1;j < MAXMESSAGES;j++)		
			if (messageType[j] != MSG_INVALID && messageType[i] != MSG_INVALID)
			{
				if ((messageAge[j]&0x7fffff) < (messageAge[i]&0x7fffff))
				{
					swapMessage(i, j);
				}
			}
	}	
	
}


public void moveMessage(int dest, int src)
{
	messageType[dest] = messageType[src];
	messageTitle[dest] = messageTitle[src];
	messageBody[dest] = messageBody[src];
	messageAge[dest] = messageAge[src];
	messageData[dest] = messageData[src];
	messageType[src] = MSG_INVALID;
	
}


public void swapMessage(int dest, int src)
{
	int type = messageType[dest];
	String title = messageTitle[dest];
	String body = messageBody[dest];
	int age = messageAge[dest];
	int data[] = messageData[dest];
	
	messageType[dest] = messageType[src];
	messageTitle[dest] = messageTitle[src];
	messageBody[dest] = messageBody[src];
	messageAge[dest] = messageAge[src];
	messageData[dest] = messageData[src];
	
	messageType[src] = type;
	messageTitle[src] = title;
	messageBody[src] = body;
	messageAge[src] = age;
	messageData[src] = data;
}

//<=- <=- <=- <=- <=-







		byte shieldRgbs[][] = {{(byte)0,(byte)255,(byte)0}, // A
		        {(byte)255,(byte)0,(byte)255}  // B		        
		};
		        

		Image changePal(byte inbuf[], byte rgbs[][], int shirtCol[]) {
		    
			
		    int i = 0x20;
		    while (true)
		    {
		        if (inbuf[i] == 80 && 
		            inbuf[i+1] == 76 &&
		            inbuf[i+2] == 84 && 
		            inbuf[i+3] == 69
		            ) break;
		        i++;
		    }
		    int b = i;
		    i += 4;    
		    
		    int length = inbuf[i-5];
		   
		    for(int j = 0; j < length; j+=3) {
		        
		        boolean found = false;
		        int k = 0;                
		        
		        while (!found && k < shirtCol.length) {
		                    
		            if (
		            ((inbuf[i+j]&0xf0) == (rgbs[k][0]&0xf0)) &&
		            ((inbuf[i+j+1]&0xf0) == (rgbs[k][1]&0xf0)) &&
		            ((inbuf[i+j+2]&0xf0) == (rgbs[k][2]&0xf0)) 
		            )                                 
		            {                
		                inbuf[i+j] = (byte)((shirtCol[k]>>16)& 0x000000ff);
		                inbuf[i+j+1] = (byte)((shirtCol[k]>>8)& 0x000000ff);
		                inbuf[i+j+2] = (byte)(shirtCol[k] &0x000000ff); 
		                found = true;
		            }
		            k++;
		        }
		        
		    }
		    
		    i += length;        
		    
		    int crc = crc(inbuf, b, length + 4);
		    inbuf[i] = (byte)((crc&0xff000000)>>24);
		    inbuf[i+1] = (byte)((crc&0x00ff0000)>>16);
		    inbuf[i+2] = (byte)((crc&0x0000ff00)>>8);
		    inbuf[i+3] = (byte)(crc&0x000000ff);
		    
		    
		    return  Image.createImage(inbuf,0,inbuf.length);        
		}


		int update_crc(int crc, byte buf[], int off, int len)
		{
		     int c = crc;
		     int n = off;
		   
		     if (!crc_table_computed) make_crc_table();
		       
		     for (n = 0; n < len; n++) {
		       c = crc_table[(c ^ buf[off+n]) & 0xff] ^ (c >>> 8);
		     }
		     return c;
		 }
		   
		 // Return the CRC of the bytes buf[0..len-1]. 
		 int crc(byte buf[], int off, int len)
		 {
		   return update_crc(0xffffffff, buf, off, len) ^ 0xffffffff;
		 }


		   
		int crc_table[] = new int[256];
		   
		   
		boolean crc_table_computed = false;
		   
		// Make the table for a fast CRC. 
		void make_crc_table()
		{
		     int c;
		     int n, k;
		   
		     for (n = 0; n < 256; n++) {
		       c =  n;
		       for (k = 0; k < 8; k++) {
		         if ((c & 1) == 1)
		     
		           c = 0xedb88320 ^ (c >>> 1);
		         else
		           c = c >>> 1;
		       }
		       crc_table[n] = c;
		     }
		     crc_table_computed = true;
		}

//		*******************
//		-------------------
//		Training - Engine
//		===================
//		*******************

final static int TOTAL_TRAINING_TIME = 20*7;
final static int trainingSchedule[][] = new int[4][7];
int trainingLine;

public void initTrainingSchedule()
{
	for(int i = 0; i < trainingSchedule.length; i++)
	{
		int percTotal = 0;
		for(int j = 0; j < trainingSchedule[i].length; j++)
		{
			int perc = TOTAL_TRAINING_TIME / trainingSchedule[i].length;
			percTotal += perc;
			trainingSchedule[i][j] = perc;
		}
		trainingSchedule[i][0] += TOTAL_TRAINING_TIME-percTotal;
	}
}

/*
public String dateStr()
{
	String s[] = league.getCurrentDate(league.currentWeek, league.currentJourney%2 == 0?0:3);
	String str = s[0]+" "+s[1]+" "+s[2]+" "+s[3];
	return str;
}
*/
///////////////////////////////////////////////////////////////////////////
// STADIUM
///////////////////////////////////////////////////////////////////////////

final static int STADIUM_NORTHSTAND = 0;
final static int STADIUM_SOUTHSTAND = 1;
final static int STADIUM_EASTSTAND = 2;
final static int STADIUM_WESTSTAND = 3;

final static int STADIUM_PARKING = 4;

final static int STADIUM_TRAININGCAMP = 5;

final static int STADIUM_GYM = 6;
final static int STADIUM_MEDICAL = 7;
final static int STADIUM_GRASS = 8;
final static int STADIUM_SCOREBOARDS = 9;

int stadiumLevel[] = new int[10];

//#ifndef REM_STADIUM
int stadiumCost[][] = {
//final static int STADIUM_NORTHSTAND = 0;
		{4832000, 7104000, 11568000, 13512000, 5000},
//final static int STADIUM_SOUTHSTAND = 1;
		{4832000, 7104000, 11568000, 13512000, 5000},
//final static int STADIUM_EASTSTAND = 2;
		{8416000, 13840000, 20000000, 24288000, 5000},
//final static int STADIUM_WESTSTAND = 3;
		{8416000, 13840000, 20000000, 24288000, 5000},
//final static int STADIUM_PARKING = 4;
		{100000, 200000, 300000, 400000, 5000},
//final static int STADIUM_TRAININGCAMP = 5;
		{10000, 20000, 30000, 40000, 5000},
//final static int STADIUM_GYM = 6;
		{3000, 6000, 9000, 12000, 5000},
//final static int STADIUM_MEDICAL = 7;
		{10000, 20000, 30000, 60000, 5000},
//final static int STADIUM_GRASS = 8;
		{3000, 6000, 9000, 12000, 5000},
//final static int STADIUM_SCOREBOARDS = 9;
		{1000, 2000, 6000, 10000, 5000}
		
};
//#endif

int stadiumRecordValue[][] = {
//		final static int STADIUM_NORTHSTAND = 0;
				{5000, 8000, 13000, 18000, 23000},
//		final static int STADIUM_SOUTHSTAND = 1;
				{5000, 8000, 13000, 18000, 23000},
//		final static int STADIUM_EASTSTAND = 2;
				{5000, 12000, 17000, 22000, 27000},
//		final static int STADIUM_WESTSTAND = 3;
				{5000, 12000, 17000, 22000, 27000},
//		final static int STADIUM_PARKING = 4;
				{1000, 5000, 10000, 15000, 20000},
//		final static int STADIUM_TRAININGCAMP = 5;
				{0, 1, 2, 3, 4},
//		final static int STADIUM_GYM = 6;
				{0, 1, 2, 3, 4},
//		final static int STADIUM_MEDICAL = 7;
				{0, 1, 2, 3, 4},
//		final static int STADIUM_GRASS = 8;
				{0, 1, 2, 3, 4},
//		final static int STADIUM_SCOREBOARDS = 9;
				{0, 1, 2, 3, 4}
};

//#ifndef REM_STADIUM
int stadiumBuildTime[][] = {
//		final static int STADIUM_NORTHSTAND = 0;
				{4, 8, 12, 16, 6},
//		final static int STADIUM_SOUTHSTAND = 1;
				{4, 8, 12, 16, 6},
//		final static int STADIUM_EASTSTAND = 2;
				{4, 10, 16, 20, 6},
//		final static int STADIUM_WESTSTAND = 3;
				{4, 10, 16, 20, 6},
//		final static int STADIUM_PARKING = 4;
				{3, 3, 4, 6, 8},
//		final static int STADIUM_TRAININGCAMP = 5;
				{4, 4, 6, 8, 10},
//		final static int STADIUM_GYM = 6;
				{2, 2, 4, 5, 6},
//		final static int STADIUM_MEDICAL = 7;
				{2, 2, 2, 3, 4},
//		final static int STADIUM_GRASS = 8;
				{6, 6, 6, 6, 6},
//		final static int STADIUM_SCOREBOARDS = 9;
				{1, 1, 2, 2, 3},
};
//#endif

int stadiumBuildStage[] = new int[10];


int getStadiumCapacity()
{
	int total =  0;
	
	for(int i = 0; i < 4;i++)
	{
		int level = stadiumLevel[i];
		total += stadiumRecordValue[i][level];
	}
	
	return total;
}


public void initStadium()
{
	for(int i = 0; i < stadiumLevel.length;i++)
	{
		//#ifndef REM_FANTASY
		if (gameMode == FANTASY)
			stadiumLevel[i] = 2;
		else
		//#endif	
			stadiumLevel[i] = Math.min((league.userTeam.cash+(league.rand()%50))/60, 4);
	}
}


// END STADIUM ///////////////////////////////////////////////////////////////////////////

int matchInjuredCount;
int matchSuspendedCount;

//#ifdef DEBUG
//#ifdef ALWAYSWIN
//#else
boolean cheatAlwaysWin = false;
//#endif
//#endif




//#ifndef REM_SQUAD_TACTICS
static int facCloseToSide;
static int facCloseToEnemy;
static int facCloseToGoal;
static int facPress;

final static int TACTIC_DEFENSE = 0;
final static int TACTIC_FOCUS = 1;
final static int TACTIC_PASSING = 2;
final static int TACTIC_ATTACKING = 3;
final static int TACTIC_DISTRIBUTION = 4;
//#endif
//#ifndef REM_2DMATCH

static void clearOffside()
{
	  for(int i = 0;i < 10;i++)
      {           
          teamA[i].offsidePosition = false;                    
          teamB[i].offsidePosition = false;                
      }
}

//#endif


String matchGoalScorers[] = new String[20];
int matchGoals[][] = new int[10][2];
int numGoal;
int numGoals[] = new int[2];


public void initRecordMatch()
{
	matchGoalScorers = new String[20];
	matchGoals = new int[10][2];
	numGoal = 0;
	numGoals = new int[2];
	
}


public void recordMatchGoal(int pid, int team)
{
	numGoals[team]++;
	
	matchGoalScorers[numGoal] = league.playerGetName(pid);
	matchGoals[numGoal][0] = numGoals[0];
	matchGoals[numGoal][1] = numGoals[1];
	//matchSuffleGoal[numGoal] = suffle;
	
	
	//matchGoalMinutes[numGoal] = minute;
	
	//com.mygdx.mongojocs.lma2007.Debug.println("########## GOL!!!!");
	//com.mygdx.mongojocs.lma2007.Debug.println("("+matchGoalMinutes[numGoal]+") "+matchGoals[numGoal][0]+" - "+matchGoals[numGoal][1]);
	//com.mygdx.mongojocs.lma2007.Debug.println("goal de "+matchGoalScorers[numGoal]);
	
	numGoal++;
//#ifndef REM_TOP_SCORERS	                 
	if (league.journeyType == League.LEAGUEJOURNEY) league.recordGoal(pid);
//#endif
}


///////////////////////////////////////////////////////////////
// BONUS CODES
///////////////////////////////////////////////////////////////
final static int BONUS_RAININMATCH = 0;
final static int BONUS_MOREFORMATIONS = 1;
final static int BONUS_TRAINING = 2;
final static int BONUS_NOINJURIES = 3;
final static int BONUS_UNLIMITEDMONEY = 4;



///////////////////////////////////////////////////////////////
// NORMAL DIFFICULTY 
///////////////////////////////////////////////////////////////
boolean autoManagement[] = new boolean[5];

final static int AUTO_TRAINING_SCHEDULE = 0;
final static int AUTO_TRAINING_STYLE = 1;
final static int AUTO_FINANCES_SETPRICES = 2;
final static int AUTO_FINANCES_EMPLOYEE = 3;
final static int AUTO_FINANCES_SPONSORS = 4;

///////////////////////////////////////////////////////////////

int lastLeaguePosition[] = new int[3];
int userTeamStartSeasonPosition;

//#ifndef REM_CALENDAR
String getFormattedDate()
{
	//A�O/MES/DIA/NOMBREDIA
	String dat[] = league.getCurrentDate(league.currentWeek, league.currentJourney%2 == 0?0:3);
	
	if (menuType == MENU_PLAY_MATCH_RESULT)
	{
		//com.mygdx.mongojocs.lma2007.Debug.println("F111111111");
		dat = league.getCurrentDate(league.lastJourneyType == League.CHAMPIONSJOURNEY?league.currentWeek:league.currentWeek-1,  league.lastJourneyType == League.CHAMPIONSJOURNEY?2:6);
	}	
	
	
	String ret = new String();
	String format = getMenuText(MENTXT_DATEFORMAT)[0];
	
	//return s[0]+" "+s[1]+" "+s[2]+" "+s[3];

	for(int i = 0; i < format.length();i++) {
		switch(format.charAt(i))
		{
			case 'Y': ret = ret+dat[0];break;
			case 'M': ret = ret+dat[1];break;
			case 'D': ret = ret+dat[2];break;
			case 'W': ret = ret+dat[3];break;
			case '*': 
				if (dat[2] == "1" || dat[2] == "21" || dat[2] == "31")
					ret = ret+getMenuText(MENTXT_DAYORDINAL)[0];
				if (dat[2] == "2" || dat[2] == "22")
					ret = ret+getMenuText(MENTXT_DAYORDINAL)[1];
				if (dat[2] == "3" || dat[2] == "23")
					ret = ret+getMenuText(MENTXT_DAYORDINAL)[2];
				else
					ret = ret+getMenuText(MENTXT_DAYORDINAL)[3];
				break;
			default: ret = ret+format.charAt(i);break;
		}
	}
	return ret;

}
//#endif



public void autoTrainingSchedule()
{

	int line = trainingLine;		
	int strongest = 0;
	int strongestValue = 0;
	
	
//#ifdef DEBUG
	//com.mygdx.mongojocs.lma2007.Debug.println("*** AT 1");
//#endif
	
	// STRONG
	for(int i = 0; i < 4; i++)
	{
		//com.mygdx.mongojocs.lma2007.Debug.println("valor: "+league.facTable[line][i]);
		if (league.facTable[line][i] > strongestValue) 
		{
			strongestValue = league.facTable[line][i];
			strongest = i;
		}		
	}
	
//#ifdef DEBUG
	//com.mygdx.mongojocs.lma2007.Debug.println("*** AT 2");
//#endif

	//com.mygdx.mongojocs.lma2007.Debug.println("STRONGEST is: "+strongest+" - "+strongestValue);
	strongest++;
	
	// RESET
	trainingSchedule[line][0] = TOTAL_TRAINING_TIME;
	
	for(int i = 1; i < trainingSchedule[line].length; i++)
	{
		trainingSchedule[line][i] = 10;
		trainingSchedule[line][0] -= 10;
	}
	
	// Buscar el mejor entrenamiento
	boolean full[] = new boolean[7];
	
	int restTime = 30-(trainingStyle*10);
	
//#ifdef DEBUG
	//com.mygdx.mongojocs.lma2007.Debug.println("*** AT 3 Training Style: "+trainingStyle);
//#endif
	
	
	while (trainingSchedule[line][0] > restTime)
	{
//#ifdef DEBUG
		//com.mygdx.mongojocs.lma2007.Debug.println("   *** AT B1");
//#endif

		int bestExercise = 1;
		int bestRatio = -10;
		
		for(int i = 1; i < trainingSchedule[line].length;i++)
		{
//#ifdef DEBUG
			//com.mygdx.mongojocs.lma2007.Debug.println("      *** AT C1");
//#endif

			if (!full[i] && bestRatio < league.allTrainings[i][strongest])
			{
				bestExercise = i;
				bestRatio = league.allTrainings[i][strongest];
			}
		}
		
		while (trainingSchedule[line][0] > restTime && trainingSchedule[line][bestExercise] < 30)
		{
//#ifdef DEBUG
			//com.mygdx.mongojocs.lma2007.Debug.println("      *** AT D1");
//#endif

			trainingSchedule[line][0]--;
			trainingSchedule[line][bestExercise]++;				
		}
		
		full[bestExercise] = true;
	}
	
//#ifdef DEBUG
	//com.mygdx.mongojocs.lma2007.Debug.println("*** AT 4");
//#endif

}


public void autoTrainingStyle()
{
	if (league.currentWeek < 5) trainingStyle = 0;
	else if (league.currentWeek < 15) trainingStyle = 1;
	else if (league.currentWeek < 25) trainingStyle = 2;
	else if (league.currentWeek < 35) trainingStyle = 1;
	else trainingStyle = 0;
}



///////////////////////////////////////////////////////////////////
// GFXMATCH EVENT - engine
///////////////////////////////////////////////////////////////////
//#ifndef REM_2DMATCH
boolean rainOn = false;
int rainProb[] = {50, 80, 40, 20, 30, 20};

final static int GFXMATCH_EVENT_NONE = -1;
final static int GFXMATCH_EVENT_GOAL = 0;
final static int GFXMATCH_EVENT_HALFMATCH = 1;
final static int GFXMATCH_EVENT_FAULT = 2;
final static int GFXMATCH_EVENT_PENALTY = 3;
final static int GFXMATCH_EVENT_OUT = 4;
final static int GFXMATCH_EVENT_CORNER = 5;
final static int GFXMATCH_EVENT_OFFSIDE = 6;

static int gfxMatchEvent = -1; 
int lastgfxMatchEvent = -1;
long eventMillis; 


public void displayGfxMatchEvent()
{
	if (lastgfxMatchEvent != gfxMatchEvent)
	{
		
		eventMillis = System.currentTimeMillis();
	}
	
	lastgfxMatchEvent = gfxMatchEvent;
	
	if (System.currentTimeMillis() > eventMillis + 2000)
	{
		gfxMatchEvent = -1;	
	}
	
	if (gfxMatchEvent != -1)
	{
		//#ifndef FEA_EA		
		fontInit(FONT_BIG_BLACK);
		printSetArea(0, 0, canvasWidth, canvasHeight);
		scr.setClip(0, 0, canvasWidth, canvasHeight);
		putColor(0xffffff);
		
		//#else
		
		//#endif
		scr.fillRect(0, (canvasHeight-printFontHeight)/2, canvasWidth, printFontHeight);
		printDraw(getMenuText(MENTXT_GFXMATCH_EVENT)[gfxMatchEvent], 0, 0, fontPenRGB, PRINT_HCENTER | PRINT_VCENTER);
		
	}
	
}
//#endif



//#ifdef BUILD_ONE_CLASS
	//#define INSERT_EXTRA_CODE
	//#include src/com.mygdx.mongojocs.lma2007.GameLaunch.java
	//#endinclude
//#else
	public void menuInit(int a, int b) {}
	public void menuAction(int a) {}
//#endif


	int preferedClubId;
	boolean exhibitionFlag;
// ******************* //
//  Final de la Clase  //
// ******************* //
};