package com.mygdx.mongojocs.sanfermines2006;

// -----------------------------------------------
// Microjocs MagicCasual v1.1 Rev.1 (3.4.2006)
// ===============================================


//#ifdef DOJA
//#define HIDE_SCROLL
//#define HIDE_CUSTOM_FONT
//#define HIDE_MORE_GAMES
//#define HIDE_CONNECTIVITY
//#define SPRITEBACKGROUND
//#define ONERENDERSTEP
//#define PRIMITIVEHUD
//#define NOPARTICLES
//#define BUILD_ONE_PLAYER
//#define NONPCS
//#define NOZSORTING
//#ifdef S60
	//#define LARGEMAPWINDOW	
//#endif
//#endif

//#ifndef HIDE_MORE_GAMES
	//#define RMS_ENGINE
//#endif


//#if !SCROLL_FULL_RENDER && !SCROLL_TILE_RENDER
	//#define SCROLL_BUFFER_RENDER
//#endif



//#ifdef J2ME
	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.audio.Music;
	import com.badlogic.gdx.audio.Sound;
	import com.badlogic.gdx.files.FileHandle;
	import com.badlogic.gdx.graphics.Color;
	import com.mygdx.mongojocs.midletemu.Canvas;
	import com.mygdx.mongojocs.midletemu.DeviceControl;
	import com.mygdx.mongojocs.midletemu.DirectGraphics;
	import com.mygdx.mongojocs.midletemu.DirectUtils;
	import com.mygdx.mongojocs.midletemu.Display;
	import com.mygdx.mongojocs.midletemu.Font;
	import com.mygdx.mongojocs.midletemu.FullCanvas;
	import com.mygdx.mongojocs.midletemu.Graphics;
	import com.mygdx.mongojocs.midletemu.Image;
	import com.mygdx.mongojocs.midletemu.MIDlet;
	import com.mygdx.mongojocs.midletemu.RecordStore;
	//import com.mygdx.mongojocs.midletemu.Runnable;
	//import com.mygdx.mongojocs.midletemu.Thread;

	import java.io.*;
import java.io.InputStream;


//#ifdef HIDE_LISTENER_SWAP
//#undef LISTENER_SWAP
//#endif


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Bios
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

public static GameCanvas gc;

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

boolean notifyHecho;

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

	if (!notifyHecho && biosStatus != BIOS_PAUSE)
	{
		gameSoundOld = soundOld;
		gameSoundLoop = soundLoop;
		notifyHecho = true;
	}
	
//#ifndef HIDE_GAME_IN_PAUSE
	if (biosPauseEnabled)
	{   
		biosPauseStart = true;
		soundStop();	
	}
//#endif	

}

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

static Graphics scr;		// Contiene siempre el objeto 'Graphics' con el que podemos dibujar en pantalla

//#ifdef NOKIAUI
	DirectGraphics dscr;	// objeto 'Graphics' para algunos terminales Nokia con caracteristicas adicionales
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para arrancar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//#ifdef FIX_NO_REPAINT_SCREEN
//#endif
public void run()
{
//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("run(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("run(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

	System.gc();
	biosCreate();	// Inicializamos MIDlet, antes de llegar al bucle principal
	System.gc();

	gameStarted = true;

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("while(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("while(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
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

public void runInit()
{
	//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("run(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
	System.gc();  Debug.println("run(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

	System.gc();
	biosCreate();	// Inicializamos MIDlet, antes de llegar al bucle principal
	System.gc();

	gameStarted = true;

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("while(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
	System.gc();  Debug.println("while(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif
}

public void runTick()
{
	// Empieza el bucle principal:
	//while (!gameExit)
	{
		biosLoop();
	}

// Hemos salido del bucle principal, paramos el sonido, guardamos prefs y salimos del MIDlet
	if(gameExit) {
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
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// bios - Wait
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean biosExit;


public void biosWait()
{
//#if DEBUG && DEBUG_STACK
	Debug.println(" ");
	Debug.println("========>>>>>>>>>>>");
	Debug.println(" biosWait() ENTER index:"+biosStackIndex);
	Debug.println("==================");
//#endif

	biosExit = false;
	while (!biosExit)
	{
		biosLoop();
	}
	biosExit = false;

//#if DEBUG && DEBUG_STACK
	Debug.println("==================");
	Debug.println(" biosWait() EXIT index:"+(biosStackIndex+1));
	Debug.println("<<<<<<<<<=========");
	Debug.println(" ");
//#endif

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

	//MONGOFIX biosWait no bloqueante!!!!
	//boolean biosWaiting = false;
	//boolean biosWaitComplete = false;

	/*public void biosWait()
	{
		Gdx.app.log("biosWait","Estamos en biosWait!!!!");
		biosExit = false;
		while (!biosExit)
		{
			biosLoop();
		}
		biosExit = false;
	}

	public void biosWaitInit()
	{
		biosWaiting = true;
		biosExit = false;
	}

	public void biosWaitTick()
	{
		if(!biosExit) biosLoop();
		else {
			biosExit = false;
			biosWaiting = false;
			biosWaitComplete = true;
		}
	}

	public void biosWaitEnd()
	{
		biosWaitComplete = false;
	}*/
// --------------------
// bios Push
// ====================

public void biosPush()
{
	biosStack[biosStackIndex] = biosStatus;
	if (++biosStackIndex >= biosStack.length) {biosStackIndex -= biosStack.length;}

//#if DEBUG && DEBUG_STACK
	Debug.println("=biosPUSH("+biosStackIndex+")");
//#endif
}

// --------------------
// bios Pop
// ====================

public void biosPop()
{
//#if DEBUG && DEBUG_STACK
	Debug.println("=biosPOP("+biosStackIndex+")");
//#endif

	if (--biosStackIndex < 0) {biosStackIndex += biosStack.length;}
	biosStatus = biosStack[biosStackIndex];
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// bios - Loop
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void biosLoop()
{

//#ifdef ENABLE_GC_TICK
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
	//	if (keyMenu == -1 || keyMenu == 1) {keyMenu *= -1;}
	//#endif

	// Truco para liberar pulsacion de las softkeys en mobiles que usan 'listener' pues no se notifica cuando se suelta la tecla.
		if (limpiaKeyMenu) {limpiaKeyMenu=false; intKeyMenu = 0;}

		//try
		{
		//#ifdef DEBUG
			Debug.spdStart(0);
		//#endif
			biosTick();
		//#ifdef DEBUG
			Debug.spdStop(0);
		//#endif

			//#ifdef FIX_NO_REPAINT_SCREEN
			//#endif

			if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}

			forceRender();

			if (gameSoundRefresh)
			{
				waitTime(1000);
				if (gameSoundOld>=0) soundPlay(gameSoundOld,gameSoundLoop);
				gameSoundRefresh=false;
				notifyHecho = false;
			}
			else
				soundTick();

		}/* catch (Exception e)
		{
		//#ifdef DEBUG
			Debug.println("** Excep Logic **");
			Debug.println("biosStatus:"+biosStatus);
			Debug.println("gameStatus:"+gameStatus);
			Debug.println("playStatus:"+playStatus);
			Debug.println(e.toString());
			//#ifdef DEBUG_RECIPIENTS
			//#endif
			e.printStackTrace();				
		//#endif
		}*/
	}

// Controlamos los 'milis' transcurridos y hacemnos la espera adecuada para que todas las iteraciones del bucle principal tarden lo mismo.
	gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
	try	{Thread.sleep( gameSleep < 1? 1:gameSleep );} catch(Exception e) {}
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Forzamos que se refresque la pantalla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void forceRender()
{
// Forzamos el renderizado del MIDlet
	canvasShow = true;
	
	waitTime(20);
	
	repaint();

//#ifdef J2ME
	serviceRepaints();
//#endif

// Esperamos a que el Thread de 'paint()' finalize totalmente (necesario por bugs de algunos terminales)
	while (canvasShow == true) {try {Thread.sleep(2);} catch(Exception e) {}}

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
// Funcion que se ejecuta al hacer: 'repaint()' creando un nuevo Thread
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void paint(Graphics g)
{
	if (canvasShow)
	{
		synchronized (this)
		{
			scr = g;	// Copiamos objeto 'Graphics' para poder usarlo desde cualquir sitio

		//#ifdef DEBUG
			Debug.spdStart(1);
		//#endif
		
		//#ifdef NOKIAUI
			dscr = DirectUtils.getDirectGraphics(g);
		//#endif

		//#ifdef DOJA
		//#endif

		//#ifdef DEBUG
			if (Debug.fillYellow) {Debug.fillYellow = false; fillDraw(0xfff800, 0, 0, canvasWidth, canvasHeight);}
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
				Debug.println("playStatus:"+playStatus);
				e.printStackTrace();
			//#endif
			}

		//#ifdef DEBUG
			Debug.spdStop(1);
			if (Debug.enabled) {Debug.debugDraw(this, scr);}
		//#endif

		//#ifdef DOJA
		//#endif

			scr = null;			// Liberamos copia de 'Graphics' para evitar usarlo desde la logica del MIDlet

			canvasShow = false;	// Indicamos que el Thread de render ha terminado
		}
	}
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<








// *******************
// -------------------
// keyboard - Engine
// ===================
// *******************

int keyMenu, keyX, keyY, keyMisc;
int intKeyMenu, intKeyX, intKeyY, intKeyMisc;
int lastKeyMenu, lastKeyX, lastKeyY, lastKeyMisc;
boolean limpiaKeyMenu;

//#ifdef J2ME
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet lanza este metodo cuando se pulsa una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//#ifdef NK-3650
//#else
public void keyPressed(int keycode)
{
//#ifdef FIX_SOFTKEY_RELEASE
	//limpiaKeyMenu = true;
//#endif

	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;

	switch (keycode)
	{
	case Canvas.KEY_NUM0:
		intKeyMisc=10;
		//intKeyMenu= 1;
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
//#elifdef SM-MYV65
//#elifdef SD-P600
//#elifdef SD-M570
//#elifdef PA-X500
//#elifdef MO-C450
//#elifdef MO-T720
//#elifdef MO-V3xx
//#elifdef MO-C65x
//#elifdef MO-V180
//#elifdef LG-U8150
//#elifdef LG-U8210
//#elifdef GR-M131
//#elifdef LG-C3380
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
//#elifdef SI-CL75
//#elifdef SI
//#else
	//#ifdef MIDP20
		case -21:	// Standrard MIDP20  - Menu Izquierda
			intKeyMenu=-1;
		return;
	
		case -22:	// Standrard MIDP20 - Menu Derecha
			intKeyMenu= 1;
		return;
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
	//#elifdef GR-M131
	//#endif
	}*/
}
//#endif
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//#endif


//#ifdef J2ME
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	//#ifdef GR-M131
	//#else
	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;
	//#endif
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//#endif


//#ifdef DOJA
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// La iAppli lanza este metodo cuando se producen eventos, como pulsar o soltar una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//#endif

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// bios - Engine
// ===================
// *******************

int biosStatus = 0;

static final int BIOS_GAME = 0;
static final int BIOS_LOGOS = 1;
static final int BIOS_MENU = 2;
static final int BIOS_POPUP = 3;
static final int BIOS_PAUSE = 4;
static final int BIOS_NONE = 5;

int biosPauseSoftkeyLeft;
int biosPauseSoftkeyRight;
int biosPauseState;

// -------------------
// bios Create
// ===================

public void biosCreate()
{

// --------------------------------
// Forzamos a FULL CANVAS para J2ME / MIDP 2.0
// ================================
//#ifdef J2ME
	//#ifdef FULLSCREEN
		//#ifdef MIDP20
		//#endif
		//#ifdef GR-M131
//		canvasWidth = 128;
//		canvasHeight = 128;
		//#endif
	//#endif
//#endif
			
// ================================


// --------------------------------
// Limpiamos canvas de blanco y mostramos imagen de loading (reloj de arena)
// ================================
	fillCanvasRGB = 0;
	forceRenderTask(RENDER_FILL_CANVAS);

/*
//#ifdef SI-x55
	for (int i=0 ; i<3 ; i++)
	{
//#endif
		canvasImg = loadImage("/loading");
		forceRender();
//#ifdef SI-x55
	}
//#endif
*/
// ================================


// --------------------------------
// Inicializamos com.mygdx.mongojocs.sanfermines2006.Debug Engine
// ================================
//#ifdef DEBUG
	Debug.debugCreate(this);
//	com.mygdx.mongojocs.sanfermines2006.Debug.enabled = true;
//#endif
// ================================


// --------------------------------
// Inicializamos tipo de FONT base a usar en el juego y altura del listener
// ================================
	printSetArea(0, 0, canvasWidth, canvasHeight);

	fontInit(FONT_SMALL_SOFTKEY);
//#ifdef HIDE_CUSTOM_FONT
	//listenerHeight += printGetHeight() - FONT_CORRECTION;
//#else
	listenerHeight += printGetHeight();
//#endif

	printInit_Font(biosGetFont());

//	printInit_Font(loadImage("/font"), loadFile("/font.cor"), 14, 0x30, 1);
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
// Movido a despues de preguntar si quieres juego con sonido
// ================================


// --------------------------------
// Inicializamos MIDlet a nivel se usuario
// ================================
	try{
	gameCreate();
	}catch(Exception e){e.printStackTrace();}
// ================================

	fontInit(FONT_SMALL_WHITE);
}


// -------------------
// bios Pause Init
// ===================

boolean biosPauseEnabled;

boolean biosPauseStart;


// -------------------
// bios Tick
// ===================

public void biosTick()
{

//#ifdef DEBUG
//	if (keyMisc == 11 && lastKeyMisc == 0) {biosPauseInit();}
	//#ifdef NK-3650
	//#else
	if (keyMisc == 12 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; if (!Debug.enabled) {gameForceRefresh = true;}}
	//#endif
//#endif


// Gestionamos si saltamos a la pantalla de "GAME IN PAUSE"
	if (biosPauseStart && biosStatus != BIOS_PAUSE)
	{
		biosPauseSoftkeyLeft = listenerIdLeft;
		biosPauseSoftkeyRight = listenerIdRight;
		biosPauseState = biosStatus;
		biosStatus = BIOS_PAUSE;
		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
		notifyHecho = false;
	}
	biosPauseStart = false;



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
		if ( logosTick() ) {biosPop(); biosExit = true;}
	return;
// --------------------

// --------------------
// menu Bucle
// --------------------
	case BIOS_MENU:
		if ( menuTick() ) {biosExit = true;}
	return;
// --------------------

// --------------------
// popup Bucle
// --------------------
	case BIOS_POPUP:
		if ( popupTick() ) {biosExit = true;}
	return;
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
			if (gameSoundOld >= 0 && gameSoundLoop == 0) {soundPlay(gameSoundOld, gameSoundLoop);}
		//#ifdef J2ME
			notifyHecho = false;
		//#endif
		}
	return;
// --------------------
	}
}


// -------------------
// bios Refresh
// ===================

public void biosRefresh()
{
//#ifdef DEBUG
	Debug.println ("biosRefresh()");
//	com.mygdx.mongojocs.sanfermines2006.Debug.println ("biosStatus="+biosStatus);
//	com.mygdx.mongojocs.sanfermines2006.Debug.println ("gameStatus="+gameStatus);
//	com.mygdx.mongojocs.sanfermines2006.Debug.println ("playStatus="+playStatus);
	//Debug.fillYellow = true; MONGOFIX
//#endif

//#ifdef J2ME
	listenerShow = true;
//#endif

	gameRefresh();


	switch (biosStatus)
	{
// --------------------
// game Refresh
// ====================
//	case BIOS_GAME:
//		gameRefresh();
//	return;
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
		formShow=true;
	return;
// ====================

// --------------------
// popup Refresh
// ====================
	case BIOS_POPUP:
		if (formRunning) {formShow=true;}
		popupShow = true;
	return;
// ====================
	}
}






// *******************
// -------------------
// font - Engine
// ===================
// *******************


//#ifdef FIX_FONT_2PIX_PLUS
//#elifdef FIX_FONT_6PIX_PLUS
//#else
	static final int FONT_CORRECTION = 0;
//#endif


static final int FONT_SMALL_WHITE 	= 0;
static final int FONT_SMALL_SOFTKEY	= 1;


//#ifdef S40
//#elifdef S60
	static final int FONT_SMALL_HEIGHT = 14;
	static final int FONT_SOFTKEY_HEIGHT = 6;
//#elifdef S80
//#endif


byte[] fontCor;
byte[] fontSoftkeyCor;
//#ifdef J2ME
	Image fontImg;
	Image fontSoftkeyImg;
//#elifdef DOJA
//#endif

int fontPenRGB;

// -------------------
// font Init
// ===================

public void fontInit(int type)
{
//#ifdef J2ME

	switch (type)
	{
	case FONT_SMALL_WHITE:

	//#ifndef HIDE_CUSTOM_FONT
	//	printInit_Font(fontImg, fontCor, FONT_SMALL_HEIGHT, 0x20, 0);
	//#else
		printInit_Font(biosGetFont());
		fontPenRGB = 0xffffff;
	//#endif
	break;

	case FONT_SMALL_SOFTKEY:

	//#ifndef HIDE_CUSTOM_FONT
	//	printInit_Font(fontSoftkeyImg, fontSoftkeyCor, FONT_SOFTKEY_HEIGHT, 0x20, 1);
	//#else
		printInit_Font(biosGetFont());
		fontPenRGB = 0xffffff;
	//#endif
	break;
	}

//#elifdef DOJA
//#endif

}



// --------------------
// bios Get Font
// ====================

public Font biosGetFont()
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

	biosPush();
	biosStatus = BIOS_LOGOS;
}


public boolean logosTick()
{
	if ( (System.currentTimeMillis() - timeIniLogos) < timeLogos) {return false;}

	if (cntLogos == numLogos) {return true;}

	canvasImg = loadImage(nameLogos[cntLogos]);

	fillCanvasRGB = rgbLogos[cntLogos];
	forceRenderTask(RENDER_FILL_CANVAS);

	cntLogos++;

	timeIniLogos = System.currentTimeMillis();

	return false;
}

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


int printOutlineRGB = 0xff0000;

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

		if ( ((mode & PRINT_VMASK) == 0) || (y >= printY) && (y + (printFontHeight*str.length) <= printY + printHeight) )
		{
	
		//#ifdef DOJA
		//#endif

			switch (printFontType)
			{
			case 1:

destY += FONT_CORRECTION;	// Ajustamos font del mobil para que quede bien, en relacion a la custom

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
								spriteDraw(printFontImg, destX+add, destY, (int)letra, printFontCor, PRINT_MASK, 0, 0, canvasWidth, canvasHeight);
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
long waitMillisSound;

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

public boolean waitFinishedSound(int time)
{
	return ((int)(System.currentTimeMillis() - waitMillisSound)) > time;
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


// -------------------
// ball Colision - (x,y) de la parte superior izquierda, (d) para el diametro
// ===================

public boolean ballColision(int xa, int ya, int da, int xb, int yb, int db)
{
// Calculamos eje central de la bola y radio
	da>>=1;
	xa += da;
	ya += da;
	db>>=1; 
	xb += db;
	yb += db;

// (xa - xb)^2 + (ya - yb)^2 <= (ra + rb)^2
	xa -= xb; xa *= xa;
	ya -= yb; ya *= ya;
	da += db; da *= da;

	return xa + ya <= da;
}


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
//#ifdef TURCO
//#else
public String[][] textosCreate(byte[] tex)
{
//#ifdef DEBUG
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
	
	//#ifdef FIX_NO_OPEN_QUESTION_MARK
	
	for(int i = 0; i < strings.length; i++) {
		
		for(int j = 0; j < strings[i].length; j++) {
	
			strings[i][j] = strings[i][j].replace('�',' ');
		}
	}
	
	//#endif

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
//#endif
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

public void fillCircleDraw(int rgb, int x, int y, int width, int height)
{
//#ifdef J2ME
	scr.setClip(x, y, width, height);
//#endif
	putColor(rgb);
//#ifdef J2ME
	scr.fillArc(x, y, width, height, 0, 360);
//#elifdef DOJA
//#endif
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
//#endif
}

// -------------------
// Image Draw
// ===================

//#ifdef J2ME
// ----------------------------------------------------------

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
//#else
	if (DestX + SizeX < 0 || DestY + SizeY < 0 || DestX >= canvasWidth || DestY >= canvasHeight) {return;}
//#endif
	
	scr.setClip(0, 0, canvasWidth, canvasHeight);
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void imageDraw(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
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


//#ifdef J2ME
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
			dscr.drawImage(img, (destX+sourX)-img.getWidth()+sizeX, destY-sourY, Graphics.LEFT|Graphics.TOP, 0x2000);	// Flip X
		break;
		
		case 2:
			dscr.drawImage(img, destX-sourX, (destY+sourY)-img.getHeight()+sizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
		break;
		
		case 3:
			dscr.drawImage(img, (destX+sourX)-img.getWidth()+sizeX, (destY+sourY)-img.getHeight()+sizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
		break;
		}

	//#elifdef MIDP20
	//#else
	//#endif
	}
}

//#elifdef DOJA
//#endif


// -------------------
// slider Vertical Draw
// ===================

public void sliderVDraw(int pos, int steps, int x, int y, int height)
{
	int barHeight = height / 4;
	int posY = y + regla3(pos, steps, height - barHeight);
//	fillDraw(0xffffff, x, posY, FORM_SLIDER_WIDTH, barHeight);
//	rectDraw(0x660000, x, y, FORM_SLIDER_WIDTH, height);


	fillDraw(0x7f0808, x+2, posY+1, FORM_SLIDER_WIDTH-3, barHeight-1);	// Sombra Roja

	fillDraw(0xd2aaaa, x+1, posY, FORM_SLIDER_WIDTH-3, barHeight-1);	// Brillo Rojo

	fillDraw(0xffffff, x+2, posY+1, FORM_SLIDER_WIDTH-5, barHeight-3);	// Blanco

	fillDraw(FORM_RGB_BACK_LINE, x, y, 1, height);	// Linea roja oscura que hacer de separador
}


// -------------------
// arrow Draw
// ===================

static final int ARROW_LEFT = 0;
static final int ARROW_RIGHT = 1;
static final int ARROW_UP = 2;
static final int ARROW_DOWN = 3;

public void arrowDraw(int rgb, int x, int y, int width, int height, int dir)
{
//#ifdef J2ME
	scr.setClip(x, y, width, height);
//#endif

//	putColor(0xffffff); scr.fillRect(x,y,width,height);		// com.mygdx.mongojocs.sanfermines2006.Debug: rellena cuadrado donde dibujar Arrow

    putColor(rgb);

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









//MONGO





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

public Image[] loadImage(String FileName, byte[] cor)
{
	int maxFrame = 0;
	for (int i=0 ; i<cor.length/6 ; i++)
	{
		int frame = cor[(i*6)+4]&0xFF; if (maxFrame < frame) {maxFrame = frame;}
	}

	return loadImage(FileName, ++maxFrame);
}

// ---------------------------------------------------------

public Image[] loadImage(String FileName, int Frames)
{
	Image Img[] = new Image[Frames];
	for (int i=0 ; i<Frames ; i++) {Img[i] = loadImage(FileName+i);}
	return Img;
}

// ---------------------------------------------------------
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

// ---------------------------------------------------------

public Image loadImage(String file, String palfile) {
	Image img;
	System.gc();
	byte[] data = loadFile(file+".png");
	if (palfile != null) {
		byte[] pal = loadFile(palfile);
		try {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(pal));
			int p = dis.readInt()+4;
			dis.close();
			for (int i = 4; i < pal.length; ++i) {
				data[p++] = pal[i];
			}
		} catch (Exception e) {
			//#ifdef DEBUG
				Debug.println("Unable to load image with palette "+palfile);
			//#endif
		}
	}
	System.gc();
	try	{
		img = Image.createImage(data, 0, data.length);
	} catch (Exception e)
	{
	//#ifdef DEBUG
		Debug.println("loadImage: "+file+" <byte array not valid>");
	//#endif
		return null;
	}
	return img;
}


// ---------------------------------------------------------
//#elifdef DOJA
// ---------------------------------------------------------
// ---------------------------------------------------------
//#endif

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
// Canvas - Engine
// ===================
// *******************

//#ifdef J2ME
	int canvasWidth = getWidth();
	int canvasHeight = getHeight();
//#elifdef DOJA
//#endif

Image canvasImg;

boolean canvasShow;

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
					soundTracks[Ary].setLooping(Loop==0);
					//VolumeControl vc = (VolumeControl) soundTracks[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(${handset.volumen});}
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
					DeviceControl.startVibra(100,Time);
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
	//#elifdef PLAYER_MIDP20_LOAD_HEAP
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
static final int SOFTKEY_YES = 6;
static final int SOFTKEY_NO = 7;

static final int SOFTKEY_EDIT = 8;
static final int SOFTKEY_EXIT = 9;

static final int SOFTKEY_MASJUEGOS = 10;

static final int SOFTKEY_MAS 		= 11;
static final int SOFTKEY_SCORES 	= 12;

static final int SOFTKEY_ACTUALIZAR	= 13;
static final int SOFTKEY_INTRODUCIR	= 14;

static final int SOFTKEY_PAUSE		= 15;
static final int SOFTKEY_CAMBIAR	= 16;
static final int SOFTKEY_VER		= 17;
static final int SOFTKEY_CONECTAR	= 18;
static final int SOFTKEY_REINTENTAR	= 19;

static final int SOFTKEY_SKIP		= 20;

static final int LISTENER_BAR_RGB = 0x000000;

int listenerIdLeft;
int listenerIdRight;

boolean listenerShow;

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
		listenerIdRight = idRight;
		listenerShow = true;
	}

}

public void listenerDraw()
{
	if (!listenerShow) {return;} else {listenerShow = false;}

	fontInit(FONT_SMALL_SOFTKEY);

	printSetArea(0, canvasHeight, canvasWidth, listenerHeight);
	fillDraw(LISTENER_BAR_RGB, printX, printY, printWidth, printHeight);

	canvasHeight += listenerHeight;
	try
	{
	//#ifndef LISTENER_SWAP
		printDraw(gameText[TEXT_SOFTKEYS][listenerIdLeft], 1, 0, fontPenRGB, PRINT_LEFT|PRINT_BOTTOM);
		printDraw(gameText[TEXT_SOFTKEYS][listenerIdRight], -1, 0, fontPenRGB, PRINT_RIGHT|PRINT_BOTTOM);
	//#else
	//#endif
		//System.out.println(gameText[TEXT_SOFTKEYS][listenerIdRight]+"/"+gameText[TEXT_SOFTKEYS][listenerIdLeft]);
	}
	catch (Exception e) {}
	canvasHeight -= listenerHeight;

	fontInit(FONT_SMALL_WHITE);
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
// <=- <=- <=- <=- <=-

//#endif










// *******************
// -------------------
// rms - Engine v2.4 - Rev.12 (18.5.2006)
// ===================
// *******************

//#ifdef RMS_ENGINE

/* ==========================================================================================

	rmsCreate(int tamanyoNecesario)
	-----------
		Inicializamos TODO lo referente al rms Engine para poder trabajar con el.
		A partir de aqui YA podemos usar la gestion de archivos del rms (Load/Save/Delete)
		Si nunca se almacen nada, se formatea automaticamente el rms.


	rmsDestroy()
	------------
		Liberamos toda la memoria usada por el rms - Engine
		A partir de aqui NO podemos usar la gestion de archivos del rms (Load/Save/Delete)


	rms Available()
	-----------
		Resultado: "int = memoria disponible"


	rmsAvailable(String fileName)
	-----------
		Obtenemos la memoria libre del rms descontando el tamanyo del archivo indicado.
		Este metodo es solo para usar cuando asumimos que vamos a reemplazar un archivo.
		Resultado: "int = memoria disponible mas el tamanyo del archivo indicado"


	rmsGetDir()
	-----------
		Recuperamos el nombre de todos los archivos almacenados.
		Resultado: "String[]" con los nombres de archivos.


	rmsCheckFile(String fileName)
	----------------------------
		Comprobamos si un fichero Existe.
		Resultado: "int = tamanyo archivo" si el archivo NO existe su tamanyo sera "-1"


	rmsLoadFile(String fileName)
	----------------------------
		Recuperamos un byte[] con al archivo solicitado.
		Resultado: "byte[] = null" si el archivo NO existe.


	rmsSaveFile(String fileName, byte[] data)
	-----------------------------------------
		Almacenamos un byte[] con el nombre de archivo indicado.
		Resultado: "boolean = true" ERROR, no se pudo almacenar el archivo.


	rmsDeleteFile(String fileName)
	------------------------------
		Eliminamos el archivo indicado.
		Resultado: "boolean = true" ERROR, el archivo no existe.

========================================================================================== */


// BOOT-Block:
// 00 (4) ID: "rms!"
// 04 (1) Version
// 05 (1) Flag OK ( 0=OK ; 1=Error, AutoFormat )
// 06 (2) Reservado
// 08 (4) Size total HD
// 0C (4) Size de la FAT

// Size BOOT = 0x10;

// 10 (n) Empieza HD

private int rmsBootIndex;
private int rmsHDIndex;

private int rmsFatSize = 0;

private byte[] rmsBoot;

private int rmsFiles;
private short[] rmsFileInfo;
private String[] rmsFileName;

private byte[] rmsSector;

private int rmsSectorSize = 3*1024; //1*1024; // DOJAPORT
private int rmsStoreSize;	// Size total HD
private int rmsStoreUsed;
private int rmsFileIndex;

static final int rmsCachedFiles = 8;

// -------------------
// rms Create
// ===================

int maxSizePerCent;
int storePerCent;

public int rmsCreate(int store)
{
//#ifdef DEBUG
	Debug.println("rmsCreate("+store+")");
//#endif

//#ifdef J2ME
	rmsBootIndex = 0;
//#elifdef DOJA
//#endif

	rmsHDIndex = rmsBootIndex + 0x10;

//#ifdef J2ME
	if (rmsSector == null)
	{
		rmsSector = rmsGetFila(0,1);
	}

	if (rmsSector != null && rmsSector[rmsSector.length-1] == 0x76)
	{
		rmsBoot = rmsLoadSegment(rmsBootIndex, 0x10);
	} else {
		rmsSector = null;
		rmsBoot = null;
	}
//#elifdef DOJA
//#endif

	if (rmsBoot == null || rmsBoot[0] != 'r' || rmsBoot[1] != 'm')
	{
	//#ifdef DEBUG
		Debug.println("rmsFormat("+store+")");
	//#endif

	//#ifdef J2ME
		for (int t=0 ; t<2 ; t++)	// Hacemos "dos pasadas" por bug en motorola V525
		{
			String[] names = RecordStore.listRecordStores();
			if (names != null)
			{
				for (int i=0 ; i<names.length ; i++)
				{
					if ( names[i].startsWith("rmsFS") )
					{
						try {
							RecordStore.deleteRecordStore( names[i] );
						} catch(Exception e)
						{
						//#ifdef DEBUGConsole
							e.printStackTrace(); e.toString();
						//#endif
						}
					}
				}
			}
		}

		rmsSector = new byte[((store / rmsSectorSize)<<1)+1];
		rmsSetFila(0, -1, rmsSector, false);		// Reservamos espacio para 'rmsSector'

		byte[] sectorData = new byte[rmsSectorSize];
	
		int maxSize = 0;
		int pack = 1;
		int filas = 0;
		int secPos = 0;

		popupInitAsk(null, new String[] {gameText[TEXT_FINALIZANDO_INSTALACION][0]," "," "}, SOFTKEY_NONE, SOFTKEY_NONE);
		forceRender();

		while (maxSize < store)
		{
		// Gestion de la barra de progreso
			maxSizePerCent = maxSize;
			storePerCent = store;
			forceRenderTask(RMS_FORMAT_SLIDER);

			int filaId = rmsSetFila(pack, -1, sectorData, false);
	
			if ( filaId < 0 )
			{
				if (filas == 0) {break;} else {pack++; filas = 0;}
			} else {
	
				rmsSector[secPos++] = (byte)pack;
				rmsSector[secPos++] = (byte)filaId;
	
				filas++;
				maxSize += sectorData.length;
			}
		}

		popupRelease();

		maxSize -= rmsHDIndex;

	//#elifdef DOJA
	//#endif

		if (maxSize - 200 < 0)
		{
		//#ifdef DEBUG
			Debug.println("rmsFormat: <Out of rms/ScratchPad>");
		//#endif
			return 0;
		}


	// Guardamos rmsSector "OK"
		rmsSector[rmsSector.length-1] = 0x76;	// Flag que 'VALIDA' el rmsSector
		
		rmsSetFila(0, 1, rmsSector, false);		// Guardamos 'rmsSector'



		if (store > maxSize) {store = maxSize;}

		rmsFiles = 0;
		rmsStoreUsed = 0;
		rmsStoreSize = store;
	
		rmsBoot = new byte[0x10];
		rmsBoot[0] = 'r';
	//	rmsBoot[1] = 'm';	// Usamos este caracter como flag para autoFormat!!!
	//	rmsBoot[2] = 's';
	//	rmsBoot[3] = '!';
	
		rmsUpdateFat(0, 0, null, (short)0);
	
	//#ifdef DEBUG
		Debug.println("rmsFormated: "+rmsStoreSize);
	//#endif
	}

	rmsStoreSize = convAry2Int(rmsBoot, 0x8);
	rmsFatSize = convAry2Int(rmsBoot, 0xC);

//#ifdef J2ME
	byte[] in = rmsLoadSegment(rmsHDIndex + rmsStoreSize - rmsFatSize, rmsFatSize);
//#elifdef DOJA
//#endif

	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(in));

	try {
		rmsFiles = dis.readShort();

		rmsFileInfo = new short[rmsFiles+rmsCachedFiles];
		rmsFileName = new String[rmsFiles+rmsCachedFiles];

		for (int i=0 ; i<rmsFiles ; i++)
		{
			rmsFileInfo[i] = dis.readShort();
			rmsFileName[i] = dis.readUTF();
		}
	} catch(Exception e) {}


	rmsStoreUsed = 0;
	for (int i=0 ; i<rmsFiles ; i++)
	{
		rmsStoreUsed += rmsFileInfo[i];
	}

//#ifdef DEBUG
	Debug.println("rmsCreated:");
	Debug.println("-Free: "+(rmsStoreSize-rmsStoreUsed));
	Debug.println("-Used: "+rmsStoreUsed);
	Debug.println("Total: "+rmsStoreSize);
//#endif

	return rmsStoreSize;
}


// -------------------
// rms Destroy
// ===================

public void rmsDestroy()
{
	rmsFileInfo = null;
	rmsFileName = null;
	rmsBoot = null;
}


// -------------------
// rms Save File
// ===================

public boolean rmsSaveFile(String fileName, byte[] data)
{
	if ( data.length > rmsAvailable(fileName) )
	{
	//#ifdef DEBUG
		Debug.println("rmsSaveFile: "+fileName+" <rms disk full>");
	//#endif
		return true;
	}

	rmsDeleteFile(fileName);

//#ifdef DEBUG
	Debug.println("rmsSaveFile: "+fileName+" <"+data.length+"bytes>");
//#endif

// Activamos FLAG de autoFormat:
	rmsBoot[1] = 'x';
//#ifdef J2ME
	rmsSaveSegment(rmsBootIndex, rmsBoot);
//#elifdef DOJA
//#endif

//#ifdef J2ME
	rmsSaveSegment(rmsHDIndex + rmsStoreUsed, data);
//#elifdef DOJA
//#endif

	rmsStoreUsed += data.length;

	return rmsUpdateFat( 1, 0, fileName, (short)data.length);
}


// -------------------
// rms Load Image
// ===================

public Image rmsLoadImage(String fileName)
{
//#ifdef J2ME
	byte[] data = rmsLoadFile(fileName);

	if (data == null) {return null;}

	return Image.createImage(data, 0, data.length);

//#elifdef DOJA
//#endif
}


// -------------------
// rms Load File
// ===================

public byte[] rmsLoadFile(String fileName)
{
	int found = rmsFindFile(fileName);

	if (found == -1)
	{
	//#ifdef DEBUG
		Debug.println("rmsLoadFile: "+fileName+" <File not found>");
	//#endif
		return null;
	}

//#ifdef DEBUG
	Debug.println("rmsLoadFile: "+fileName+" <"+rmsFileInfo[found]+"bytes>");
//#endif

//#ifdef J2ME
	return rmsLoadSegment(rmsHDIndex + rmsFileIndex, rmsFileInfo[found]);
//#elifdef DOJA
//#endif
}


// -------------------
// rms Delete File
// ===================

public boolean rmsDeleteFile(String fileName)
{
	int found = rmsFindFile(fileName);

	if (found == -1)
	{
	//#ifdef DEBUG
		Debug.println("rmsDeleteFile: "+fileName+" <File not found>");
	//#endif
		return true;
	}

//#ifdef DEBUG
	Debug.println("rmsDeleteFile: "+fileName);
//#endif

// Activamos FLAG de autoFormat:
	rmsBoot[1] = 'x';
//#ifdef J2ME
	rmsSaveSegment(rmsBootIndex, rmsBoot);
//#elifdef DOJA
//#endif

	rmsStoreUsed -= rmsFileInfo[found];

	int indexNew = rmsFileIndex;
	int indexOld = rmsFileIndex + rmsFileInfo[found];

	for (int i=found+1 ; i<rmsFiles ; i++)
	{
	//#ifdef J2ME
		rmsSaveSegment(rmsHDIndex + indexNew, rmsLoadSegment(rmsHDIndex + indexOld, rmsFileInfo[i]) );
	//#elifdef DOJA
	//#endif

		indexNew += rmsFileInfo[i];
		indexOld += rmsFileInfo[i];
	}

	return rmsUpdateFat(-1, found, null, (short)0);
}


// -------------------
// rms Get Dir
// ===================

public String[] rmsGetDir()
{
//#ifdef DEBUG
	Debug.println("rmsGetDir:"); for (int i=0 ; i<rmsFiles ; i++) {Debug.println(rmsFileName[i]+" <"+rmsFileInfo[i]+"bytes>");}
//#endif
	String[] tmpStr = new String[rmsFiles];
	System.arraycopy(rmsFileName, 0, tmpStr, 0, rmsFiles);
	return tmpStr;
}


// -------------------
// rms Find File
// ===================

public int rmsFindFile(String fileName)
{
	rmsFileIndex = 0;
	for (int i=0 ; i<rmsFileName.length ; i++) {if (fileName.equals(rmsFileName[i])) {return i;} else {rmsFileIndex += rmsFileInfo[i];}}
	return -1;
}


// -------------------
// rms Available
// ===================

public int rmsAvailable()
{
	return (rmsStoreSize - rmsStoreUsed - rmsFatSize - 64);
}


public int rmsAvailable(String fileName)
{
	int found = rmsFindFile(fileName);
	return (found<0?0:rmsFileInfo[found]) + rmsAvailable();
}


// -------------------
// rms Update Fat
// ===================

private boolean rmsUpdateFat(int suma, int pos, String newName, short newData)
{
	if (suma > 0)
	{
		rmsFileName[rmsFiles  ] = newName;
		rmsFileInfo[rmsFiles++] = newData;
	} else
	if (suma < 0)
	{
		rmsFiles--;
		for (int i=pos ; i<rmsFiles ; i++)
		{
			rmsFileName[i] = rmsFileName[i+1];
			rmsFileInfo[i] = rmsFileInfo[i+1];
		}
	}

// Almacenamos la nueva FAT
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	try {
		dos.writeShort(rmsFiles);

		for (int i=0 ; i<rmsFiles ; i++)
		{
			dos.writeShort(rmsFileInfo[i]);
			dos.writeUTF(rmsFileName[i]);
		}
	} catch(Exception e) {}

	byte[] fat = baos.toByteArray();
	rmsFatSize = fat.length;

// Desactivamos FLAG de autoFormat:
	rmsBoot[1] = 'm';
	convInt2Ary(rmsBoot, 0x08, rmsStoreSize);
	convInt2Ary(rmsBoot, 0x0C, rmsFatSize);

//#ifdef J2ME
	rmsSaveSegment(rmsHDIndex + rmsStoreSize - rmsFatSize, fat);
	rmsSaveSegment(rmsBootIndex, rmsBoot);
//#elifdef DOJA
//#endif

	if (suma > 0 && rmsFiles == rmsFileInfo.length) {rmsCreate(rmsStoreSize);}

	return false;
}


//#ifdef J2ME

// -------------------
// rms Save Segment
// ===================

private void rmsSaveSegment(int index, byte[] data)
{
	int posData = 0;
	int sizeData = data.length;

	while (sizeData > 0)
	{
		int sector = index / rmsSectorSize;

		byte[] bufer = rmsGetFila(rmsSector[sector<<1], rmsSector[(sector<<1)+1]);

		int pos = index - (rmsSectorSize * sector);

		int size = rmsSectorSize - pos;

		if (size > sizeData) {size = sizeData;}

		System.arraycopy(data, posData, bufer, pos, size);

		rmsSetFila(rmsSector[sector<<1], rmsSector[(sector<<1)+1], bufer, false);

		posData += size;

		sizeData -= size;

		index += size;
	}
}


// -------------------
// rms Load Segment
// ===================

private byte[] rmsLoadSegment(int index, int sizeData)
{
	byte[] data = new byte[sizeData];

	int posData = 0;

	while (sizeData > 0)
	{
		int sector = index / rmsSectorSize;

		byte[] bufer = rmsGetFila(rmsSector[sector<<1], rmsSector[(sector<<1)+1]);

		int pos = index - (rmsSectorSize * sector);

		int size = rmsSectorSize - pos;

		if (size > sizeData) {size = sizeData;}

		System.arraycopy(bufer, pos, data, posData, size);

		posData += size;

		sizeData -= size;

		index += size;
	}

	return data;
}


// -------------------
// rms Get Fila
// ===================

private byte[] rmsGetFila(int pack, int fila)
{
	RecordStore rs = null;

	byte[] bufer = null;

	try
	{
		rs = RecordStore.openRecordStore("rmsFS"+pack, false);

		bufer = rs.getRecord(fila);
	}
	catch(Exception e)
	{
	//#ifdef DEBUGConsole
		e.printStackTrace(); e.toString();
	//#endif
	}
	finally {
		try {
			rs.closeRecordStore();
		} catch (Exception e) {}
	}

	return bufer;
}


// -------------------
// rms Set Fila
// ===================

private int rmsSetFila(int pack, int fila, byte[] data, boolean delete)
{
	RecordStore rs = null;

	int result = -1;

	try
	{
		rs = RecordStore.openRecordStore("rmsFS"+pack, data!=null);

		if (data != null)
		{
			if (fila < 0)
			{
				fila = rs.addRecord(data, 0, data.length);
			} else {
				rs.setRecord(fila, data, 0, data.length);
			}

			result = fila;
		}
		else if (delete)
		{
			rs.deleteRecord(fila);
		}
		else
		{
			int size = rs.getRecordSize(fila);
			result = size;
		}
	}
	catch(Exception e)
	{
	//#ifdef DEBUGConsole
		e.printStackTrace(); e.toString();
	//#endif
	}
	finally {
		try {
			rs.closeRecordStore();
		} catch (Exception e) {}
	}

	return result;
}
//#endif


//#else
//#endif

// <=- <=- <=- <=- <=-








// *******************
// -------------------
// form - Engine
// ===================
// *******************

int formListAdjustY;


static final int FORM_SLIDER_WIDTH = 7;

static final int FORM_MORE_GAMES_ASPA_WIDTH = 5;

static final int FORM_EDIT_MAX_CHARS = 10;	// Numero maximo de letras para la edicion del nombre del jugador


// -----------------------------------------------
// Definicion de los colores usados en los formularios
// ===============================================
static final int FORM_RGB_BACKGROUND = 0xff2921;	// Color de fondo para los formularios
static final int FORM_RGB_BACK_LINE = 0xca0000;		// Color de la linea roja oscura para separar elementos en los formularios
static final int FORM_RGB_MARC = 0xff9C39;			// Color del marco
static final int FORM_RGB_BOX = 0x946342;			// Color de la caja normal (marron)
static final int FORM_RGB_BOX_SELECTED = 0xffefde;	// Color de la caja seleccionada
static final int FORM_RGB_BOX_DISABLED = 0x7b948c;	// Color de la caja oscura
// ===============================================


// -----------------------------------------------
// Constantes para gestionar los tama�os de los bordes y separaciones
// ===============================================
//#ifdef S30
//#elifdef S40
//#elifdef S60
static final int FORM_SEPARATOR = 4;

static final int FORM_MARC_WIDTH = 17;
static final int FORM_MARC_HEIGHT = 17;
static final int FORM_MARC_BORDER = 0;

static final int FORM_LOGO_HEIGHT = 56;

//#elifdef S80
//#endif
// ===============================================


// -----------------------------------------------
// Constantes de los tipos de formularios (formType)
// ===============================================
static final int FORM_NONE = 0;			// Formulario vacio
static final int FORM_OPTION = 1;		// Listado de opciones
static final int FORM_LIST = 2;			// Listado de texto
static final int FORM_EDIT_NAME = 3;	// Editor de nombres
static final int FORM_MORE_GAMES = 4;	// Visor de imagenes de propaganda
static final int FORM_PLAYER_SELECT = 5;// Seleccion de jugador
static final int FORM_LISTADO = 6;		// Listados
static final int FORM_RESULT = 7;		// Resultados de una partida
// ===============================================


// -----------------------------------------------
// Constantes de los tipos de formularios FORM_RESULT que pueden haber
// ===============================================
static final int FORM_LISTADO_RANKING = 0;
static final int FORM_LISTADO_LEVEL = 1;
// ===============================================


// -----------------------------------------------
// Constantes para definir en donde se encuantra cada icono en el archivo de sprites
// ===============================================
static final int FORM_ARROW_LEFT = 0x00;
static final int FORM_ARROW_RIGHT = 0x02;
static final int FORM_ARROW_UP = 0x04;
static final int FORM_ARROW_DOWN = 0x06;
static final int FORM_ITEM_CENEFA = 0x08;		// Cursor del editor de nombres
static final int FORM_ITEM_FLECHA_UP = 0x09;	// Flecha de que has subido en el ranking
static final int FORM_ITEM_FLECHA_DOWN = 0x0A;	// Flecha de que has bajado en el ranking
static final int FORM_ITEM_LOCK = 0x0B;
static final int FORM_ITEM_ROSA = 0x0C;
static final int FORM_ITEM_BARRA = 0x0D;
static final int FORM_ITEM_FERMIN = 0x10;
static final int FORM_ITEM_BORDER = 0x18;
static final int FORM_ITEM_FACES = 0x21;
// ===============================================



byte[] formItemsCor;
//#ifdef J2ME
Image formItemsImg;
//#elifdef DOJA
//#endif


String formListStr[][];
int formListDat[][];

int formListIni;
int formListPos;
int formListCMD;

int formBoxEnter;

boolean formVslider;
int formVsliderX;
int formVsliderY;
int formVsliderWidth;
int formVsliderHeight;

String formScrollText;

int[] formBoxesX;
int[] formBoxesW;

String[] formBottomText;
int formBottomTextHeight;

boolean formResultStageSelect;

int formScrollTextHeight;

int formResultMode;

String[] formResultCommentStr;

boolean formScrollPage;

boolean formCellShow;
int formListPosOld;

boolean formRunning;
boolean formShow;

// -------------------
// form Create
// ===================

public void formCreate()
{
	if (formItemsCor == null) {formItemsCor = loadFile("/formItems.cor");}

	//#ifdef J2ME
		if (formItemsImg == null) {formItemsImg = loadImage("/formItems");}
	//#elifdef DOJA
	//#endif
}


// -------------------
// form Destroy
// ===================

public void formDestroy()
{
	formItemsCor = null;
//#if !FIX_GC_GRAPHICS && !DOJA
	formItemsImg = null;
//#endif
}


// -------------------
// form Clear
// ===================

public void formClear()
{
// formLogo
	formLogoImg = null;
	formLogoHeight = 0;
// formTitle
	formTitleStr = null;
	formTitleHeight = 0;
// formOption
// formList
// formText
	formTextStr = null;
	formTextHeight = 0;
//formScrollText
	formScrollText = null;
//formBottomText
	formBottomText = null;
// Por defecto los scrolls verticales se hacen mediante la opcion seleccionada
	formScrollPage = false;
// Texto ya cortado del cometario sobre las rosas pilladas en el resumen del nivel completado
	formResultCommentStr = null;
}

// -------------------
// form Logo Init
// ===================

Image formLogoImg;
int formLogoHeight;

public void formLogoInit(Image img)
{
	formLogoImg = img;
}

public void formLogoInit(int height)
{
	formLogoHeight = height;
}

// -------------------
// form Publi Init
// ===================

Image formPubliImg;
String formPubliStr;
int formPubliSize;
int formPubliScrollX;
int publiX, publiY, publiW, publiH;
boolean formPubliScrollShow;

public void formPubliInit(Image img, String str)
{
	formPubliImg = img;
	formPubliStr = str;
}

// -------------------
// form Title Init
// ===================

int formTitleX;
int formTitleY;
int formTitleWidth;
int formTitleHeight;
String formTitleStr;	// Texto usado como titulo

public void formTitleInit(String str)
{
	formTitleStr = str;
}


// -------------------
// form Text Init
// ===================

int formTextX;
int formTextY;
int formTextWidth;
int formTextHeight;
String[] formTextStr;	// Texto usado para 'formBody'

public void formTextInit(String[] str)
{
	formTextStr = str;
}


// -------------------
// form Body Init
// ===================

int formBodyX;
int formBodyY;
int formBodyWidth;
int formBodyHeight;
int formBodyMode;

int formBoxX;
int formBoxY;
int formBoxWidth;
int formBoxHeight;

int formCellX;
int formCellY;
int formCellWidth;
int formCellHeight;
int formCellNumWidth;
int formCellNumHeight;

int formMaxCharWidth;
String formEditStr;
boolean formEditFirstMove;
boolean formEditStrUpdate;

int formListView;			// Numero de lineas que pueden ser visibles

int formActionBack;
int formActionAcept;

public void formInit(int mode, int pos, int actionBack, int actionAcept)
{
// Guardamos el tipo de Formulario a mostrar
	formBodyMode = mode;

// Guardamos posicion inicial
	formListPos = pos;

// Guardamos la accion a ejecutar al pulsar la softkey izquierda y derecha
	formActionBack = actionBack;
	formActionAcept = actionAcept;


// Anotamos los espacios entre cada elemento
	int cursorY = 0;


// Inicializamos variables de sliders
	formVslider = false;
	formVsliderY = 0;


// formLogo: Si hay lotopito, calculamos sus datos
	if (formLogoImg != null)
	{
		formLogoHeight = formLogoImg.getHeight();
	}
	cursorY += formLogoHeight;


// formTitle: Si hay titulo, calculamos sus datos
	if (formTitleStr != null)
	{
		//fontInit(FONT_SMALL_WHITE);

		formTitleY = cursorY;

		formTitleHeight = printGetHeight();
		cursorY += formTitleHeight;
	}


// formBody: Ajustamos el eje y tama�o destinado al BODY
	formBodyWidth = canvasWidth;
	formBodyX = 0;
	formBodyY = cursorY;
	formBodyHeight = canvasHeight - formBodyY;


// formScrollText:
	if (formScrollText != null)
	{
		//fontInit(FONT_SMALL_WHITE);
		formBodyHeight -= (printGetHeight() + 3);
		publiX = 0;
		publiY = formBodyY + formBodyHeight + (FORM_MARC_BORDER + (FORM_SEPARATOR / 2) + 1);
		publiW = canvasWidth;
		publiH = 0;
		formPubliSize = printStringWidth(formScrollText);
		formPubliScrollX = canvasWidth + (FORM_MARC_BORDER * 2);
	}


// formBottomText:
	if (formBottomText != null)
	{
		formBottomTextHeight = (printGetHeight() * formBottomText.length) + (FORM_SEPARATOR*2);
	} else {
		formBottomTextHeight = 0;
	}


// formText:
	if (formTextStr != null)
	{
		//fontInit(FONT_SMALL_WHITE);

		formTextHeight = (formTextStr.length * printGetHeight()) + FORM_SEPARATOR;
		formTextWidth = formBodyWidth - (FORM_SEPARATOR * 2);
		formTextX = formBodyX + FORM_SEPARATOR;
		formTextY = formBodyY + FORM_SEPARATOR;
	}


// Calculamos area libre para el Body
	int tmpHeight = formBodyHeight - formTextHeight - formBottomTextHeight;
	int tmpY = formBodyY + formTextHeight;


// Flag para activar desbordamiento del espacio del Body
	boolean bodyOverflow = false;


// Representamos tipo de formulario
	switch (formBodyMode)
	{
	case FORM_OPTION:

		//fontInit(FONT_SMALL_WHITE);

		formBoxEnter = printGetHeight() + FORM_SEPARATOR;	// Salto de linea entre la (x,y) de una caja a la siguiente.


	// Calculamos el ancho de todas las lineas para calcular el ancho de las cajas (box)
		boolean optionCicle = false;

		int textoSizeX = 0;
		for (int sizeX, t=0 ; t<formListStr.length ; t++)
		{
			if (formListStr[t].length > 1) {optionCicle = true;}
	
			for (int i=0 ; i<formListStr[t].length ; i++)
			{
				sizeX = printStringWidth(formListStr[t][i]); if ( textoSizeX < sizeX ) {textoSizeX = sizeX;}
			}
		}


		formBoxWidth = textoSizeX + (FORM_SEPARATOR * 2) + (optionCicle? FORM_SEPARATOR + (spriteWidth(formItemsCor, FORM_ARROW_LEFT) * 2) : 0);
		if (formBoxWidth < formBodyWidth - (formBodyWidth / 3)) {formBoxWidth = formBodyWidth - (formBodyWidth / 3);}

		formBoxHeight = formBoxEnter;
		formBoxX = formBodyX + ((formBodyWidth - formBoxWidth) / 2);


	// Calculamos cuantas lineas de texto nos entran en pantalla
		formListView = tmpHeight / formBoxEnter;	// lineas visibles en lo que queda de pantalla

//		if (formListView > 3) {formListView=3;}

	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
		}
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
		else {

			int height = tmpHeight - (((FORM_SEPARATOR*2) + spriteHeight(formItemsCor, FORM_ARROW_UP)) * 2);	// Reservamos area para dos aspas (arriba y abajo)
			formListView = height / formBoxEnter;	// lineas visibles en lo que queda de pantalla

//			if (formListView > 3) {formListView=3;}
		}

	// Forzamos a que el area para mostar opciones sean siempre 3 lineas
		formListView=3;

	// Ajustamos el Body al area minima que necesita
		formBodyHeight = (formListView * formBoxEnter) + (FORM_SEPARATOR*4) + (spriteHeight(formItemsCor, FORM_ARROW_UP)*2);
	// Si hay logo, ajustamos el body abajo de la pantalla, sino lo centramos (menus INGAME)
		if (formLogoHeight != 0)
		{
			formBodyY = tmpY + ((tmpHeight - formBodyHeight));
		} else {
			formBodyY = tmpY + ((tmpHeight - formBodyHeight)/2);
		}

	// Calculamos ejeY para el listado de opciones
		formBoxY = formBodyY + (FORM_SEPARATOR*2) + spriteHeight(formItemsCor, FORM_ARROW_UP);


	// Ajustamos coordenadas del slider
		formVsliderX = formBoxX + formBoxWidth + FORM_SLIDER_WIDTH;
		formVsliderY = formBoxY;
		formVsliderHeight = (formListView * (formBoxHeight + FORM_SEPARATOR)) - FORM_SEPARATOR;
	break;


	case FORM_LIST:

		//fontInit(FONT_SMALL_WHITE);

		formBoxEnter = printGetHeight();	// Salto de linea una linea y la siguiente.

		formBoxWidth = formBodyWidth - ((FORM_SEPARATOR * 2) + FORM_SLIDER_WIDTH);
		formBoxX = formBodyX + FORM_SEPARATOR;

	// Cortamos el texto si se sale de la pantalla
		int cnt = 0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			formListStr[i] = printTextBreak(formListStr[i][0], formBoxWidth);
			cnt += formListStr[i].length;
		}
		int[][] tmpDat = new int[cnt][];
		String[][] tmpStr = new String[cnt][1];
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
		formListView = (tmpHeight - (FORM_SEPARATOR * 2)) / formBoxEnter;	// lineas visibles en lo que queda de pantalla
	
	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
		} else {
			formVslider = true;
		}

		formBoxHeight = (formListView * formBoxEnter);

		formBoxY = tmpY + ((tmpHeight - formBoxHeight) / 2);

		if (formVslider)
		{
			formVsliderX = formBodyX + formBodyWidth - FORM_SLIDER_WIDTH;
			formVsliderY = formBoxY;
			formVsliderHeight = formBoxHeight;
		}
	break;


	case FORM_EDIT_NAME:

		//fontInit(FONT_SMALL_WHITE);

	// Calculamos letra mas ancha para calcular el ancho de la rejilla
		formMaxCharWidth = 0;
		for (int i=0 ; i<gameText[TEXT_ABECEDARIO][0].length() ; i++)
		{
			int width = printStringWidth(gameText[TEXT_ABECEDARIO][0].substring(i,i+1));
			if (formMaxCharWidth < width) {formMaxCharWidth = width;}
		}

	// Calculamos tama�o y posicion de la caja donde editamos el nombre
//		formBoxWidth = (formMaxCharWidth * FORM_EDIT_MAX_CHARS) + printStringWidth(gameText[TEXT_NAME][0]+": ");
		formBoxHeight = printGetHeight() * 2;

	//#ifdef HIDE_CUSTOM_FONT
		if (canvasHeight < 130)
		{
			formBoxHeight -= printGetHeight()/2;
		}
    //#endif

//		formBoxX =  formBodyX + ((formBodyWidth - formBoxWidth));
		formBoxY = tmpY + FORM_SEPARATOR;

	// Calculamos tama�o y posicion de la rejilla de letras
		formCellWidth = formMaxCharWidth + 3;
		formCellHeight = printGetHeight();

		formCellNumWidth = (formBodyWidth - FORM_SEPARATOR * 2) / formCellWidth;

//		formCellNumWidth--; formCellNumWidth &= -2;	// Hack, para que el numero de celdas sean siempre pares

		formCellNumHeight = (gameText[TEXT_ABECEDARIO][0].length() / formCellNumWidth);
		if ((formCellNumWidth * formCellNumHeight) < gameText[TEXT_ABECEDARIO][0].length()) {formCellNumHeight++;}

		formCellX = ((formBodyWidth - (formCellNumWidth * formCellWidth)) / 2) + formBodyX;

		int baseBottomY = formBodyY + formBodyHeight - (printGetHeight() * 2) - FORM_SEPARATOR;

	//#ifdef HIDE_CUSTOM_FONT
		if (canvasHeight < 130)
		{
			baseBottomY += printGetHeight()/2;
		}
	//#endif

		formCellY = (formBoxY + formBoxHeight);
		formCellY += ((baseBottomY - formCellY) - (formCellNumHeight * formCellHeight) ) / 2;


		formEditFirstMove = true;

		formEditStr = gamePlayerNames[1];

		formListPos = (formCellNumWidth * formCellNumHeight) + (formCellNumWidth/2) +1;

	// Miramos si nos hemos salido del body
		bodyOverflow = formCellY - FORM_SEPARATOR < formBoxY + formBoxHeight;

	break;


//#ifndef HIDE_MORE_GAMES
	case FORM_MORE_GAMES:

		//fontInit(FONT_SMALL_WHITE);
		formPubliSize = printStringWidth(formPubliStr);
		formPubliScrollX = canvasWidth / 4;
	break;
//#endif


//#ifndef BUILD_ONE_PLAYER
	case FORM_PLAYER_SELECT:

		//fontInit(FONT_SMALL_WHITE);
		formBoxHeight = playerSelectCell + (printGetHeight()*2) + FORM_SEPARATOR;
		formBoxY = formBodyY + ((formBodyHeight - formBoxHeight) / 2) + FORM_SEPARATOR;

	// Si no hay espacio suficiente, eliminamos logotipo
		bodyOverflow = (formBodyHeight < formBoxHeight + (FORM_SEPARATOR*2) );
	break;
//#endif


	case FORM_LISTADO:

		//fontInit(FONT_SMALL_WHITE);

		formScrollTextHeight = printGetHeight() *2;

	// Calculamos salto de linea
		formBoxEnter = printGetHeight() + FORM_SEPARATOR;
		if (formBoxEnter < spriteHeight(formItemsCor, FORM_ITEM_LOCK)) {formBoxEnter = spriteHeight(formItemsCor, FORM_ITEM_LOCK);}
		if (formBoxEnter < spriteHeight(formItemsCor, FORM_ITEM_ROSA)) {formBoxEnter = spriteHeight(formItemsCor, FORM_ITEM_ROSA);}
		if (formBoxEnter < spriteHeight(formItemsCor, FORM_ITEM_FERMIN)) {formBoxEnter = spriteHeight(formItemsCor, FORM_ITEM_FERMIN);}
		if (formBoxEnter < spriteHeight(formItemsCor, FORM_ITEM_FLECHA_UP)) {formBoxEnter = spriteHeight(formItemsCor, FORM_ITEM_FLECHA_UP);}
		if (formBoxEnter < spriteHeight(formItemsCor, FORM_ITEM_FACES)) {formBoxEnter = spriteHeight(formItemsCor, FORM_ITEM_FACES);}

//		formBoxEnter += 2;

	// Calculamos cuantas lineas de texto nos entran en pantalla
		int aspaHeight = spriteHeight(formItemsCor, FORM_ARROW_UP) * 2;
		formListView = (tmpHeight-aspaHeight) / formBoxEnter;	// lineas visibles en lo que queda de pantalla

	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		int sliderWidth = 0;
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
		} else {
			sliderWidth = FORM_SLIDER_WIDTH;

			if (formResultMode != FORM_LISTADO_RANKING)
			{
				formVslider = true;
			}
		}


	// Calculamos el ancho de todos los elemetos a mostrar en la misma linea
		int spaces = 0;
		switch (formResultMode)
		{
		case FORM_LISTADO_LEVEL:

			formBoxesW = new int[5];
			if (formResultStageSelect)
			{
				formBoxesW[0] = spriteWidth(formItemsCor, FORM_ITEM_LOCK);	// sprite LOCK
			}
			formBoxesW[1] = formListStringWidth(0);		// texto (nombre del encierro)
			formBoxesW[2] = spriteWidth(formItemsCor, FORM_ITEM_ROSA);	// ancho del sprite de la rosa
			formBoxesW[3] = formListStringWidth(1);		// ancho de la puntuacion
		//#ifndef BUILD_ONE_PLAYER
			formBoxesW[4] = spriteWidth(formItemsCor, FORM_ITEM_FERMIN);	// ancho del sprite de las letras de FERMIN
		//#endif

			spaces = (formResultStageSelect? 6:5);

		//#ifdef BUILD_ONE_PLAYER
		//#endif
		break;

		case FORM_LISTADO_RANKING:

			formBoxesW = new int[5];
			formBoxesW[0] = spriteWidth(formItemsCor, FORM_ITEM_FLECHA_UP);	// sprite LOCK
			formBoxesW[1] = formListStringWidth(0);		// texto (nombre del encierro)
		//#ifndef BUILD_ONE_PLAYER
			formBoxesW[2] = spriteWidth(formItemsCor, FORM_ITEM_FACES);	// ancho del sprite de las caras
		//#endif
			formBoxesW[3] = formListStringWidth(1);		// ancho de los nombres
			formBoxesW[4] = formListStringWidth(2);		// ancho de la puntuacion

			spaces = 6;
		break;
		}


	// Calculamos separacion entre todos los elementos
		int separator = (canvasWidth - (formBoxesW[0] + formBoxesW[1] + formBoxesW[2] + formBoxesW[3] + formBoxesW[4] + (formVslider? FORM_SLIDER_WIDTH:0) ) )/spaces;


	// Si la separacion es cero o negativa, debemos recortar el elemento nombre
		if (separator <= 0)
		{
			separator = 1;
			switch (formResultMode)
			{
			case FORM_LISTADO_RANKING:
	
				formBoxesW[3] = (canvasWidth - (formBoxesW[0] + formBoxesW[1] + formBoxesW[2] + formBoxesW[4] + (formVslider? FORM_SLIDER_WIDTH:0) + spaces));
			break;
			}
		}


	// Calculamos X de todos los elemetos
		formBoxesX = new int[5];
		if (formBoxesW[0] != 0) {formBoxesX[0] = separator;}
		formBoxesX[1] = formBoxesX[0] + formBoxesW[0] + separator;
		formBoxesX[2] = formBoxesX[1] + formBoxesW[1] + separator;
		formBoxesX[3] = formBoxesX[2] + formBoxesW[2] + separator;
		formBoxesX[4] = formBoxesX[3] + formBoxesW[3] + separator;


	// Calculamos X de la primera linea de elementos
		formBoxWidth = formBodyWidth - separator - (formVslider? sliderWidth:0);
		formBoxX = formBodyX + ((formBodyWidth - (formVslider? sliderWidth:0) - formBoxWidth) / 2);


	// Calculamos Y de la primera linea de elementos
		formBoxHeight = formBoxEnter;
		formBoxY = tmpY + ((tmpHeight - ((formListView * formBoxEnter) + FORM_SEPARATOR)) / 2);


	// Ajustamos coordenadas del slider
		formVsliderX = canvasWidth - FORM_SLIDER_WIDTH;
		formVsliderY = formBoxY;
		formVsliderHeight = (formListView * formBoxEnter);


	// Controlamos desbordamiento del body, por si es necesario quitar el logotipo
		bodyOverflow = (formListStr.length >= 4 && formListView < 4);
	break;


	case FORM_RESULT:

		//fontInit(FONT_SMALL_WHITE);

	// Calculamos X de la primera linea de elementos
		formBoxWidth = canvasWidth - (FORM_SEPARATOR*3);
		formBoxX = FORM_SEPARATOR*2;

	// Calculamos alturas de todos los elemetos a mostrar en pantalla
		formBoxesW = new int[5];
		formBoxesW[0] = printGetHeight();								// "Encierro x"
		formBoxesW[1] = spriteHeight(formItemsCor, FORM_ITEM_ROSA);		// alto del sprite de la rosa

		if (ferminesItem[levelPlayed-1] != 0)
		{
			formBoxesW[2] = printGetHeight();								// "Letra Conseguida"
			formBoxesW[3] = spriteHeight(formItemsCor, FORM_ITEM_FERMIN);	// alto del sprite de las letras de FERMIN
		}


		int textId = 0;
		if (roseCount > 0 && totalRoses > 0)
		{
			textId++;
			int xcent = regla3(roseCount, totalRoses, 100);
			if (xcent > 5) {textId++;}
			if (xcent > 15) {textId++;}
			if (xcent > 25) {textId++;}
			if (xcent > 35) {textId++;}
			if (xcent > 45) {textId++;}
			if (xcent > 55) {textId++;}
			if (xcent > 65) {textId++;}
			if (xcent > 75) {textId++;}
			if (xcent > 85) {textId++;}
		}


		formResultCommentStr = printTextBreak(gameText[TEXT_RESULT_COMMENTS][textId], formBoxWidth);
		formBoxesW[4] = printGetHeight() * formResultCommentStr.length;	// "texto explicativo"


	// Calculamos separacion entre todos los elementos
		separator = (formBodyHeight - (formBoxesW[0] + formBoxesW[1] + formBoxesW[2] + formBoxesW[3] + formBoxesW[4])) / 5;


	// Calculamos Y de todos los elemetos
		formBoxesX = new int[5];
		formBoxesX[1] = formBoxesX[0] + formBoxesW[0] + separator;
		formBoxesX[2] = formBoxesX[1] + formBoxesW[1] + separator;
		formBoxesX[3] = formBoxesX[2] + formBoxesW[2] + separator;
		formBoxesX[4] = formBoxesX[3] + formBoxesW[3] + separator;


	// Hack para usar el area de "Letra conseguida X" para el texto de resumen del nivel
		if (ferminesItem[levelPlayed-1] == 0)
		{
			formBoxesX[4] = formBoxesX[2];
			formBoxesW[4] = formBoxesW[2] + formBoxesW[3] + formBoxesW[4];
		}


	// Calculamos Y de la primera linea de elementos
		formBoxHeight = formBodyHeight - separator;
		formBoxY = tmpY + (separator/2);


	// Controlamos desbordamiento del body, por si es necesario quitar el logotipo
		bodyOverflow = (formBoxesX[4] + formBoxesW[4] >= formBodyHeight);
	break;
	}


// Si hay desbordamiento del Body, eliminamos logotipo
	if (bodyOverflow && (formLogoImg != null || formLogoHeight != 0))
	{
		formLogoImg = null;
		formLogoHeight = 0;
		formInit(mode, pos, actionBack, actionAcept);
	}

// Comprobamos que la opcion seleccionada exista, sino, ponemos la cero por defecto.
	if (formListStr != null && formListPos >= formListStr.length) {formListPos = 0;}

	formRunning = true;
	formShow = true;
}


// -------------------
// form Release
// ===================

public void formRelease()
{
	formRunning = false;

// release para formLogoInit()
	formLogoImg = null;
	formLogoHeight = 0;

// release para formPubliInit()
	formPubliImg = null;
	formPubliStr = null;

	formListClear();
	formClear();

	formShow = false;
	formPubliScrollShow = false;

	System.gc();
}


// -------------------
// form Tick
// ===================

public boolean formTick()
{

/*
	if (formListAdjustY != 0)
	{
		formListAdjustY += formListAdjustY<0?1:-1;
//		if (--formListAdjustY >= formBoxEnter) {formListAdjustY = 0;}
		formShow = true;
		return false;
	}
*/


// formOption
	switch (formBodyMode)
	{
	case FORM_OPTION:

	// Controlamos desplazamiento vertical
		if (keyY > 0 && lastKeyY == 0 && formListPos < formListStr.length-1) {formListAdjustY = formBoxEnter; formListPos++; formShow = true;}
		else
		if (keyY < 0 && lastKeyY == 0 && formListPos > 0) {formListAdjustY = -formBoxEnter; formListPos--; formShow = true;}

	// Controlamos si hay que hacer scroll vertical
		if (formListIni > formListPos) {formListIni = formListPos;}
		else
		if (formListIni < formListPos-formListView+1) {formListIni = formListPos-formListView+1;}

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

	// Controlamos si pulsamos OK
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			if (++formListDat[formListPos][2] == formListStr[formListPos].length) {formListDat[formListPos][2] = 0;}
			formListCMD = formListDat[formListPos][1];
			formShow = true;
			return true;
		}

	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionBack;
			return true;
		}

	// Actualizamos texto de Softkey Positiva con texto especifico si lo hubiera
		int softkeyId = (formListDat[formListPos][0]>>8) & 0xff;
		listenerInit(listenerIdLeft, (softkeyId>0? softkeyId : SOFTKEY_ACEPT));
	break;


	case FORM_LIST:

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
			formEditStrUpdate = true;
			formCellShow = true;
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

			formCellShow = true;
		}

		if (keyY != 0 && lastKeyY == 0)
		{
			posY += keyY;

			if (posY < 0) {posY = formCellNumHeight;}
			else
			if (posY > formCellNumHeight) {posY = 0;}

			formCellShow = true;
		}

		formListPosOld = formListPos;
		formListPos = (posY * formCellNumWidth) + posX;

	// Controlamos la pulsacion en una letra
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
		// Hemos pulsado en "Borrar/Aceptar" o en una letra de la rejilla?
			if (formListPos >= (formCellNumWidth*formCellNumHeight))
			{
				if (formListPos < ((formCellNumWidth*formCellNumHeight)+(formCellNumWidth/2)))
				{
				// Hemos pulsado "Borrar" Borramos ultima letra, en el caso de que haya como minimo una.
					if (formEditStr.length() > 0) {formEditStr = formEditStr.substring(0, formEditStr.length()-1);}
				} else {
				// Hemos pulsado "Aceptar"
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

			formEditStrUpdate = true;
			formCellShow = true;
		}
	break;


//#ifndef HIDE_MORE_GAMES
	case FORM_MORE_GAMES:

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


	// Controlamos si pulsamos PAD derecha e izquierda
		if (keyX != 0 && lastKeyX == 0)
		{
			publiPos += keyX;

			if (publiPos < 0) {publiPos = 0;}
			else
			if (publiPos >= cacheFichasVisibles) {publiPos = cacheFichasVisibles -1;}
			else
			{
				formListCMD = MENU_ACTION_MORE_GAMES;
				return true;
			}
		}


	// Controlamos el scroll horizontal de texto
		if (formPubliScrollX-- < -formPubliSize) {formPubliScrollX = canvasWidth;}
		formPubliScrollShow = true;
	break;
//#endif


//#ifndef BUILD_ONE_PLAYER
	case FORM_PLAYER_SELECT:

	// Controlamos si pulsamos ACEPT
		if (keyMenu > 0 && lastKeyMenu == 0)
		{

			if (toreroLocked && playerSelected == 2)
			{
				popupInitInfo(null, gameText[TEXT_PLAYER_LOCKED]);
				biosWait();
				forceRender();
				break;
			}

			formListCMD = formActionAcept;
			return true;
		}


	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			formListCMD = formActionBack;
			return true;
		}

	// Controlamos si pulsamos PAD horizontal
		if (keyX != 0 && lastKeyX == 0)
		{
			playerSelected += keyX;
			formShow = true;
		}

		if (playerSelected < 0) {playerSelected = 0;} else if (playerSelected > 2) {playerSelected = 2;}
	break;
//#endif


	case FORM_LISTADO:

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

	// Controlamos desplazamiento vertical
		if (formResultMode != FORM_LISTADO_RANKING)
		{
			if (formScrollPage)
			{
				if (keyY < 0 && lastKeyY == 0 && formListIni > 0) {formListIni--; formShow = true;}
				else
				if (keyY > 0 && lastKeyY == 0 && formListIni < formListStr.length - formListView) {formListIni++; formShow = true;}
			} else {
				if (keyY > 0 && lastKeyY == 0 && formListPos < (menuType==MENU_STAGE_SELECT? currentLevel : formListStr.length-1) ) {formListPos++; formShow = true;}
				else
				if (keyY < 0 && lastKeyY == 0 && formListPos > 0) {formListPos--; formShow = true;}
			}
		}
	break;


	case FORM_RESULT:

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
	}


// Controlamos el scroll horizontal de texto
	if (formScrollText != null)
	{
		if (formPubliScrollX-- < -formPubliSize) {formPubliScrollX = canvasWidth;}
		formPubliScrollShow = true;
	}

	return false;
}


// -------------------
// form Draw
// ===================

public void formDraw()
{

// Debemos refrescar el formulario o algun sub-componente?
	if (!formShow)
	{
	// Mostramos scroll de la publicidad en mas juegos
		if (formPubliScrollShow) {formPubliScrollShow = false; formPubliScrollDraw();}

	// Mostramos celda anterior y antigua del editor de nombres
		if (formCellShow)
		{
			formCellShow = false;
		// Limpiamos Cenefa (machacando letras de las periferia a la seleccionada)
			int xx = formListPosOld%formCellNumWidth;
			int yy = formListPosOld/formCellNumWidth;
			formCellDraw(xx, yy, true);

		// Dibujamos Cenefa
			formCellDraw(formListPos%formCellNumWidth, formListPos/formCellNumWidth, false);

		// Actualizamos todas las letras de la periferia de la cenafa anterior
			formCellDraw(xx-1, yy, false);
			formCellDraw(xx,   yy, false);
			formCellDraw(xx+1, yy, false);

			formCellDraw(xx-1, yy-1, false);
			formCellDraw(xx,   yy-1, false);
			formCellDraw(xx+1, yy-1, false);

			formCellDraw(xx-1, yy+1, false);
			formCellDraw(xx,   yy+1, false);
			formCellDraw(xx+1, yy+1, false);

			fillDraw(FORM_RGB_BACK_LINE, 0, formBoxY+formBoxHeight, canvasWidth, 1);

			if (formListPos >= formCellNumWidth*formCellNumHeight || formListPosOld >= formCellNumWidth*formCellNumHeight)
			{
				formCellCmdDraw();
			}

			if (formEditStrUpdate)
			{
				formEditStrUpdate = false;
				formEditStrDraw();
			}
		}

		return;
	}
	formShow = false;
	formCellShow = false;
	formEditStrUpdate = false;


// Limpiamos el canvas con el color correcto
	switch (formBodyMode)
	{
	case FORM_MORE_GAMES:

		fillDraw(0x3D3D3D, 0, 0, canvasWidth, canvasHeight);
	break;

	default:

		if (menuImg != null)
		{
			fillDraw(FORM_RGB_BACKGROUND, 0,0, canvasWidth, canvasHeight);
			imageDraw(menuImg, 0, 0);
		}
	break;
	}



// formLogo: Renderizamos logotipo del formulario
	if (formLogoImg != null)
	{
		imageDraw(formLogoImg, 0,0);
	}



// formTitle: Renderizamos Titulo del formulario
	if (formTitleStr != null)
	{
		//fontInit(FONT_SMALL_WHITE);

		printSetArea(formTitleX, formTitleY, formTitleWidth, formTitleHeight);

		printDraw(formTitleStr, FORM_SEPARATOR, 0, fontPenRGB, PRINT_VCENTER|PRINT_OUTLINE);
	}


// formBodyMode
	switch (formBodyMode)
	{
	case FORM_OPTION:

	// Controlamos si hay que hacer scroll vertical
		formListIni = formListPos -1;
/*		if (formListIni > formListPos) {formListIni = formListPos;}
		else
		if (formListIni < formListPos-formListView+1) {formListIni = formListPos-formListView+1;}
*/

	// Pintamos el area de fondo donde mostrar las lineas del listado
		if (gameStatus == GAME_PLAY_TICK)
		{
			formBackgroundDraw(formBodyY, formBodyHeight);
		}


	// Pintamos barra de opcion seleccionada
		formSelectionBarDraw(formBoxX, formBoxY + ((formListPos - formListIni)*formBoxEnter), formBoxWidth, formBoxHeight);


	// Pintamos todas las opciones del menu
		for (int i=0 ; i<formListView ; i++)
		{
		// Controlamos desbordamiento (lineas que quedan vacias debido al scroll)
			if (formListIni+i < 0 || formListIni+i >= formListStr.length) {continue;}

			int addY = i * formBoxEnter;
//j			int addY = (i * formBoxEnter) + formListAdjustY;

		// Pintamos caja de la opcion
//			rectDraw(0x000000, formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);

			//fontInit( FONT_SMALL_WHITE );

		// Pintamos texto ya cortado
			printSetArea(formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
			printDraw(formListStr[formListIni+i][formListDat[formListIni+i][2]], 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER |PRINT_OUTLINE);

		// Pintamos aspas horizontales si es necesario
			if (formListIni + i == formListPos && formListStr[formListIni+i].length > 1)
			{
				printDraw(formItemsImg,  4, 0, FORM_ARROW_LEFT,  PRINT_VCENTER|PRINT_LEFT,  formItemsCor);
				printDraw(formItemsImg, -4, 0, FORM_ARROW_RIGHT, PRINT_VCENTER|PRINT_RIGHT, formItemsCor);
			}
		}

	// Si hay mas lineas de texto que el area para mostrarlas, dibujamos las aspas de scroll
		printSetArea(formBoxX, formBoxY, formBoxWidth, formListView*(formBoxEnter));
		if (formListIni > 0)
		{
			printDraw(formItemsImg, 0, -FORM_SEPARATOR -spriteHeight(formItemsCor, FORM_ARROW_UP),   FORM_ARROW_UP,  PRINT_HCENTER|PRINT_TOP,  formItemsCor);
		}

		if (formListIni < formListStr.length - formListView)
		{
			printDraw(formItemsImg, 0,  FORM_SEPARATOR +spriteHeight(formItemsCor, FORM_ARROW_DOWN), FORM_ARROW_DOWN,  PRINT_HCENTER|PRINT_BOTTOM,  formItemsCor);
		}


	// Pintamos slider vertical si existe
		if (formVslider)
		{
			sliderVDraw(formListPos, formListStr.length - 1, formVsliderX, formVsliderY, formVsliderHeight);
		}
	break;


	case FORM_LIST:

	// Pintamos el area de fondo donde mostrar las lineas del listado
		formBackgroundDraw(formBodyY, formBodyHeight);


	// Pintamos todas las lineas del listado
		printSetArea(formBoxX, formBoxY, formBoxWidth, formBoxHeight);
		//fontInit(FONT_SMALL_WHITE);
		for (int i=0 ; i<formListView ; i++)
		{
		// Pintamos texto
			printDraw(formListStr[formListIni+i][formListDat[formListIni+i][2]], 1, i*formBoxEnter, fontPenRGB, (formListView==1? PRINT_HCENTER : PRINT_LEFT));
		}

		if (formVslider)
		{
			sliderVDraw(formListIni, formListStr.length - formListView, formVsliderX, formVsliderY, formVsliderHeight);
		}
	break;


	case FORM_EDIT_NAME:

	// Pintamos el area de fondo donde mostrar las lineas del listado
		formBackgroundDraw(formBodyY, formBodyHeight);


	// Renderizamos caja donde editamos nombre
		formEditStrDraw();


	// Pintamos sprite bajo la letra seleccionada
		int posX = formListPos % formCellNumWidth;
		int posY = formListPos / formCellNumWidth;

		if (posY < formCellNumHeight)
		{
			printSetArea(formCellX+(posX*formCellWidth), formCellY+(posY*formCellHeight), formCellWidth, formCellHeight);
			printDraw(formItemsImg, 0, 1, FORM_ITEM_CENEFA, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
		}


	// Renderizamos celdas "ABCD..."
		for (int cnt=0, y=0; y<formCellNumHeight ; y++)
		{
			for (int x=0; x<formCellNumWidth ; x++)
			{
				formCellDraw(x, y, false);
			}
		}

		formCellCmdDraw();
	break;


//#ifndef HIDE_MORE_GAMES
	case FORM_MORE_GAMES:

		//fontInit(FONT_SMALL_WHITE);

	// Calculamos area donde mostrar scroll de texto horizontal
		formScrollTextHeight = printGetHeight() *2;

	// calculamos area de pintado
		publiX = formBodyX - FORM_MARC_BORDER;
		publiY = formBodyY - FORM_MARC_BORDER - FORM_SEPARATOR + 1;
		publiW = formBodyWidth + (FORM_MARC_BORDER * 2);
		publiH = formBodyHeight + (FORM_MARC_BORDER * 2) + FORM_SEPARATOR - formScrollTextHeight;


	// renderizamos base de color gris
//		fillDraw(FORM_RGB_MOREGAMES_BACKGROUND, publiX, publiY, publiW, publiH);



	// Pintamos el area de fondo donde mostrar las lineas del listado
		formBackgroundDraw(canvasHeight - formScrollTextHeight, formScrollTextHeight);

	// renderizamos scroll de texto
		formPubliScrollDraw();



	// renderizamos imagen de propaganda
		int borde = 5;
		if (formPubliImg != null)
		{
			imageDraw(formPubliImg, publiX + ((publiW - formPubliImg.getWidth())/2), publiY + ((publiH - formPubliImg.getHeight())/2) );
			borde = (((publiW - formPubliImg.getWidth()) / 2) - FORM_MORE_GAMES_ASPA_WIDTH) / 2;
		}

	// aspa Izquierda
		if (publiPos > 0)
		{
			arrowDraw(FORM_RGB_BACKGROUND, publiX + borde - FORM_MORE_GAMES_ASPA_WIDTH, publiY + ((publiH-(FORM_MORE_GAMES_ASPA_WIDTH*2))/2), (FORM_MORE_GAMES_ASPA_WIDTH*2), (FORM_MORE_GAMES_ASPA_WIDTH*2), ARROW_LEFT);
		}
	// aspa Derecha
		if (publiPos < cacheC-1)
		{
			arrowDraw(FORM_RGB_BACKGROUND, publiX + publiW - borde - FORM_MORE_GAMES_ASPA_WIDTH, publiY + ((publiH-(FORM_MORE_GAMES_ASPA_WIDTH*2))/2), (FORM_MORE_GAMES_ASPA_WIDTH*2), (FORM_MORE_GAMES_ASPA_WIDTH*2), ARROW_RIGHT);
		}
	break;
//#endif


//#ifndef BUILD_ONE_PLAYER
	case FORM_PLAYER_SELECT:

	// Pintamos el area de fondo donde mostrar las lineas del listado
		formBackgroundDraw(formBodyY, formBodyHeight);


		printSetArea(0, formBoxY, canvasWidth, formBoxHeight);

		int width = playerSelectCell;

		int posSelX = (canvasWidth - width)/2;

		int selected = playerSelected;
		if (toreroLocked && selected == 2) {selected = 3;}

		//#ifdef J2ME
		imageDraw(playerSelectImg, selected*playerSelectCell, 0, playerSelectCell, playerSelectCell, posSelX, printY);
		//#elifdef DOJA
		//imageDraw(playerSelectImg, selected*playerSelectCell, 0, playerSelectCell, playerSelectCell, posSelX, printY);
		//#endif




	// Pintamos nombre del personaje
		//fontInit(FONT_SMALL_WHITE);
		int arrowWidth = spriteWidth(formItemsCor, FORM_ARROW_LEFT);
		int nameWidth = printStringWidth(gameText[TEXT_PLAYERS_NAMES][selected]) + ((arrowWidth + (FORM_SEPARATOR*2))*2);

		printSetArea(arrowWidth, printY + playerSelectCell + FORM_SEPARATOR, canvasWidth-(arrowWidth*2), printGetHeight()*2);


	// Cortamos texto si es necesario
		String[] nameStr = printTextBreak(gameText[TEXT_PLAYERS_NAMES][selected], printWidth);
		int textoSizeX = 0;
		for (int sizeX, i=0 ; i<nameStr.length ; i++)
		{
			sizeX = printStringWidth(nameStr[i]); if ( textoSizeX < sizeX ) {textoSizeX = sizeX;}
		}

		textoSizeX = (canvasWidth - (arrowWidth*2) - textoSizeX)/4;

		printDraw(nameStr, 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

		if (playerSelected > 0)
		{
			printDraw(formItemsImg, -arrowWidth+textoSizeX, 0, FORM_ARROW_LEFT, PRINT_LEFT|PRINT_VCENTER, formItemsCor);
		}
		if (playerSelected < 2)
		{
			printDraw(formItemsImg, arrowWidth-textoSizeX, 0, FORM_ARROW_RIGHT, PRINT_RIGHT|PRINT_VCENTER, formItemsCor);
		}

	break;
//#endif


	case FORM_LISTADO:

	// Controlamos si hay que hacer scroll vertical
		if (!formScrollPage)
		{
			if (formListIni > formListPos) {formListIni = formListPos;}
			else
			if (formListIni < formListPos-formListView+1) {formListIni = formListPos-formListView+1;}
		}


	// Pintamos el area de fondo donde mostrar las lineas del listado
		formBackgroundDraw(formBodyY, formBodyHeight);


	// Pintamos barra de opcion seleccionada
		if (formListPos >= formListIni && formListPos < formListIni+formListView)
		{
			formSelectionBarDraw(formBoxX, formBoxY + ((formListPos - formListIni)*formBoxEnter), formBoxWidth, formBoxHeight);
		}

	// Pintamos todas las opciones del menu
		for (int i=0 ; i<formListView ; i++)
		{
			int addY = i * formBoxEnter;

			//fontInit(FONT_SMALL_WHITE);

			switch (formResultMode)
			{
			case FORM_LISTADO_LEVEL:

				if (formBoxesW[0] != 0)
				{
					printSetArea(formBoxesX[0], formBoxY + addY, formBoxesW[0], formBoxHeight);

					if ( (formListIni+i) > currentLevel )
					{
						printDraw(formItemsImg, 0, 0, FORM_ITEM_LOCK, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
					}
				}
	
				printSetArea(formBoxesX[1], formBoxY + addY, formBoxesW[1], formBoxHeight);
				printDraw(formListStr[formListIni+i][0], 0, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER|PRINT_OUTLINE);

			// Si solo hay un parametro, es que solo queremos mostar un texto simple
				if ( formListStr[(formListIni+i)][1] != null)
				{
					printSetArea(formBoxesX[2], formBoxY + addY, formBoxesW[2], formBoxHeight);
					printDraw(formItemsImg, 0, 0, FORM_ITEM_ROSA, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
		
					printSetArea(formBoxesX[3], formBoxY + addY, formBoxesW[3], formBoxHeight);
					printDraw(formListStr[formListIni+i][1], 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER|PRINT_OUTLINE);
		
					if (formListDat[formListIni+i][2] >= 0)
					{
						printSetArea(formBoxesX[4], formBoxY + addY, formBoxesW[4], formBoxHeight);
						printDraw(formItemsImg, 0, 0, FORM_ITEM_FERMIN+formListDat[formListIni+i][2], PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
					}
				}

			// Pintamos flechas verticales
				if (!formScrollPage && formListPos >= formListIni && formListPos < formListIni+formListView)
				{
					printSetArea(formBoxesX[0], formBoxY + ((formListPos - formListIni)*formBoxEnter), formBoxesW[0], formBoxHeight);

					if (formListPos > 0)
					{
						printDraw(formItemsImg, 0, -printHeight, FORM_ARROW_UP, PRINT_HCENTER|PRINT_BOTTOM, formItemsCor);
					}

					if (formListPos < (menuType == MENU_STAGE_SELECT? currentLevel : formListStr.length-1) )
					{
						printDraw(formItemsImg, 0, printHeight, FORM_ARROW_DOWN, PRINT_HCENTER|PRINT_TOP, formItemsCor);
					}
				}
			break;

			case FORM_LISTADO_RANKING:

				if (formListIni + i == formListPos && rankingArrow >= 0)
				{
					printSetArea(formBoxesX[0], formBoxY + addY, formBoxesW[0], formBoxHeight);
					printDraw(formItemsImg, 0, 0, FORM_ITEM_FLECHA_UP+rankingArrow, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
				}
	
				printSetArea(formBoxesX[1], formBoxY + addY, formBoxesW[1], formBoxHeight);
				printDraw(formListStr[formListIni+i][0], 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER|PRINT_OUTLINE);

			//#ifndef BUILD_ONE_PLAYER
				printSetArea(formBoxesX[2], formBoxY + addY, formBoxesW[2], formBoxHeight);
				printDraw(formItemsImg, 0, 0, FORM_ITEM_FACES + formListDat[formListIni+i][2], PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
			//#endif
	
				printSetArea(formBoxesX[3], formBoxY + addY, formBoxesW[3], formBoxHeight);
				String tmpStr = printTextCut(formListStr[formListIni+i][1], printWidth+formBoxesW[4]-FORM_SEPARATOR-printStringWidth(formListStr[formListIni+i][2]));
				printDraw(tmpStr, 0, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER|PRINT_OUTLINE);
	
				printSetArea(formBoxesX[4], formBoxY + addY, formBoxesW[4], formBoxHeight);
				printDraw(formListStr[formListIni+i][2], 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER|PRINT_OUTLINE);
			break;
			}
		}

		if (formVslider)
		{
			if (formScrollPage)
			{
				sliderVDraw(formListIni, formListStr.length - formListView, formVsliderX, formVsliderY, formVsliderHeight);
			} else {
				sliderVDraw(formListPos, formListStr.length - 1, formVsliderX, formVsliderY, formVsliderHeight);
			}
		}
	break;


	case FORM_RESULT:

	// Pintamos el area de fondo donde mostrar las lineas del listado
		formBackgroundDraw(formBodyY, formBodyHeight);

		//fontInit(FONT_SMALL_WHITE);

		printSetArea(formBoxX, formBoxY + formBoxesX[0], formBoxWidth, formBoxesW[0]);
		printDraw(gameText[TEXT_ENCIERRO][0]+" "+levelPlayed, 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

		printSetArea(formBoxX, formBoxY + formBoxesX[1], formBoxWidth, formBoxesW[1]);
		int rx = (printWidth - (spriteWidth(formItemsCor, FORM_ITEM_ROSA) + FORM_SEPARATOR + printStringWidth("x"+roseCount)))/2;

		printDraw(formItemsImg, rx, 0, FORM_ITEM_ROSA, PRINT_LEFT|PRINT_VCENTER, formItemsCor);
		printDraw("x"+roseCount, rx + spriteWidth(formItemsCor, FORM_ITEM_ROSA) + FORM_SEPARATOR, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);

		if (ferminesItem[levelPlayed-1] != 0)
		{
			printSetArea(formBoxX, formBoxY + formBoxesX[2], formBoxWidth, formBoxesW[2]);
			printDraw(gameText[TEXT_LETRA_CONSEGUIDA][0], 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

			printSetArea(formBoxX, formBoxY + formBoxesX[3], formBoxWidth, formBoxesW[3]);
			printDraw(formItemsImg, 0, 0, FORM_ITEM_FERMIN+levelPlayed-1, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
		}

		printSetArea(formBoxX, formBoxY + formBoxesX[4], formBoxWidth, formBoxesW[4]);
		printDraw(formResultCommentStr, 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
	break;
	}


// formText:
	if (formTextStr != null)
	{
		//fontInit(FONT_SMALL_WHITE);

		printSetArea(formTextX, formTextY, formTextWidth, formTextHeight);
		fillDraw(0xffffff, formTextX, formTextY+formTextHeight-1, formTextWidth, 1);
		printDraw(formTextStr, FORM_SEPARATOR /*+ spriteWidth(formItemsCor, FORM_ITEM_TITLE)*/, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
//		printDraw(formItemsImg, 0, 0, FORM_ITEM_TITLE, PRINT_LEFT|PRINT_VCENTER, formItemsCor);
	}

// formBottomText:
	if (formBottomText != null)
	{
		//fontInit(FONT_SMALL_WHITE);

		int alto = formBodyHeight - (formListView * formBoxEnter) - ((formBoxY - formBodyY)*2);
		printSetArea(formBoxX, formBodyY+formBodyHeight-alto, formBodyWidth-(formBoxX*2), alto);
		printDraw(formBottomText, 0, 0, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
		printDraw(new String[] {""+hiscore}, 0, 0, fontPenRGB, PRINT_RIGHT|PRINT_VCENTER);

		fillDraw(FORM_RGB_BACK_LINE, 0, printY, canvasWidth, 1);
	}

// formScrollText:
	if (formScrollText != null)
	{
		formPubliScrollDraw();
	}

}


// -------------------
// form PubliScroll Draw :: Pintamos scroll de texto horizontal pixel a pixel
// ===================

public void formPubliScrollDraw()
{
// renderizamos scroll de texto
	//fontInit(FONT_SMALL_WHITE);
	printSetArea(0, canvasHeight - formScrollTextHeight, canvasWidth, formScrollTextHeight);
	formBackgroundDraw(printY, printHeight);
	printDraw((formScrollText!=null? formScrollText : formPubliStr), formPubliScrollX, 1, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
}


// -------------------
// form Background Draw :: Pintamos el area de color rojo que hay de fondo de los menus
// ===================

public void formBackgroundDraw(int y, int height)
{
	printSetArea(0, y, canvasWidth, height);
	fillDraw(FORM_RGB_BACKGROUND, printX, printY, printWidth, printHeight);
	int x=0; while (x < canvasWidth)
	{
		printDraw(formItemsImg,  x, 0, FORM_ITEM_BORDER+1,  PRINT_TOP|PRINT_LEFT,  formItemsCor);
		printDraw(formItemsImg,  x, 0, FORM_ITEM_BORDER+7,  PRINT_BOTTOM|PRINT_LEFT,  formItemsCor);
		x += spriteWidth(formItemsCor, FORM_ITEM_BORDER+1);
	}
}


// -------------------
// form SelectionBar Draw :: Pintamos la bara de seleccion
// ===================

public void formSelectionBarDraw(int x, int y, int width, int height)
{
	printSetArea(x, y, width, height);
	int ancho = spriteWidth(formItemsCor, FORM_ITEM_BARRA);
	int unidades = formBoxWidth / ancho; if (formBoxWidth % ancho != 0) {unidades++;}
	for (int xl=0 ; xl<unidades ; xl++)
	{
		int destX = regla3(xl, unidades, formBoxWidth);

		int frame = FORM_ITEM_BARRA+1;
		if (xl==0) {frame--;} else if (xl==unidades-1) {frame++;  destX = formBoxWidth - ancho;}

		printDraw(formItemsImg,  destX, 1, frame,    PRINT_VCENTER|PRINT_LEFT,   formItemsCor);
	}
}




// -------------------
// form Cell Draw
// ===================

public void formEditStrDraw()
{
	printSetArea(0, formBodyY, canvasWidth, formBoxHeight);
	fillDraw(FORM_RGB_BACKGROUND, printX, printY, printWidth, printHeight);
	int x=0; while (x < canvasWidth)
	{
		printDraw(formItemsImg,  x, 0, FORM_ITEM_BORDER+1,  PRINT_TOP|PRINT_LEFT,  formItemsCor);
		x += spriteWidth(formItemsCor, FORM_ITEM_BORDER+1);
	}

	//fontInit(FONT_SMALL_WHITE);
	int txtX = canvasWidth - printStringWidth(gameText[TEXT_NAME][0]+": "+formEditStr);
	if (txtX > formCellX) {txtX = formCellX;}

	printSetArea(txtX, formBoxY, canvasWidth, formBoxHeight);
	printDraw(gameText[TEXT_NAME][0]+": " + formEditStr + (formEditStr.length() < FORM_EDIT_MAX_CHARS? "_":""), 0, 1, fontPenRGB, PRINT_LEFT|PRINT_VCENTER);
}



// -------------------
// form Cell Draw
// ===================

public void formCellDraw(int x, int y, boolean limpia)
{
	if (x<0 || x>=formCellNumWidth || y<0 || y>=formCellNumHeight) {return;}

	int cnt = (y*formCellNumWidth)+x;

	int addX = x*formCellWidth;
	int addY = y*formCellHeight;
	printSetArea(formCellX+addX, formCellY+addY, formCellWidth, formCellHeight);

	if (limpia)
	{
		printWidth = spriteWidth(formItemsCor, FORM_ITEM_CENEFA) +2;
		printHeight = spriteHeight(formItemsCor, FORM_ITEM_CENEFA) +2;
		printX -= (printWidth - formCellWidth)/2;
		printY -= (printHeight - formCellHeight)/2;

		fillDraw(FORM_RGB_BACKGROUND, printX, printY, printWidth, printHeight);
	} else {

		if (cnt == formListPos)
		{
			printDraw(formItemsImg, 0, 1, FORM_ITEM_CENEFA, PRINT_HCENTER|PRINT_VCENTER, formItemsCor);
		}

		if (cnt < gameText[TEXT_ABECEDARIO][0].length())
		{
			//fontInit(FONT_SMALL_WHITE);
			printDraw(gameText[TEXT_ABECEDARIO][0].substring(cnt,cnt+1), 1,1, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER| (cnt == formListPos?PRINT_OUTLINE:0));
		}
	}
}


public void formCellCmdDraw()
{
	// Limpiamos area de los comandos "borrar" y "aceptar"
		printSetArea(0, formBodyY + formBodyHeight - formBoxHeight, canvasWidth, formBoxHeight);
		fillDraw(FORM_RGB_BACKGROUND, printX, printY, printWidth, printHeight);
		int x=0; while (x < canvasWidth)
		{
			printDraw(formItemsImg,  x, 0, FORM_ITEM_BORDER+7,  PRINT_BOTTOM|PRINT_LEFT,  formItemsCor);
			x += spriteWidth(formItemsCor, FORM_ITEM_BORDER+1);
		}


	// Pintamos lineas separadoras
		//fontInit(FONT_SMALL_WHITE);
		fillDraw(FORM_RGB_BACK_LINE, 0, formBoxY+formBoxHeight, canvasWidth, 1);
		int baseBottomY = formBodyY + formBodyHeight - (printGetHeight() * 2) - FORM_SEPARATOR;
		fillDraw(FORM_RGB_BACK_LINE, 0, formBodyY + formBodyHeight - formBoxHeight, canvasWidth, 1);
		fillDraw(FORM_RGB_BACK_LINE, canvasWidth/2, formBodyY + formBodyHeight - formBoxHeight, 1, formBoxHeight);


	// Miramos si "Borrar" o "Aceptar" estan seleccionados
		boolean eraseBox = false;
		boolean aceptBox = false;

		if (formListPos >= (formCellNumWidth*formCellNumHeight))
		{
			if (formListPos < ((formCellNumWidth*formCellNumHeight)+(formCellNumWidth/2))) {eraseBox = true;} else {aceptBox = true;}
		}


	// Renderizamos celda "Borrar"
		printSetArea(0, formBodyY + formBodyHeight - formBoxHeight, canvasWidth/2, formBoxHeight);
		//fontInit(FONT_SMALL_WHITE);
		if (eraseBox)
		{
			int sizeX = printStringWidth(gameText[TEXT_BORRAR][0]);
			int sizeL = printStringWidth(gameText[TEXT_BORRAR][0].substring(0,1));

			spriteDraw(formItemsImg, 0, 1, FORM_ITEM_CENEFA, formItemsCor, PRINT_FIT|PRINT_HCENTER|PRINT_VCENTER, printX + ((printWidth - sizeX)/2), printY, sizeL, printHeight);
		}
		printDraw(gameText[TEXT_BORRAR][0], 0,1, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);


	// Renderizamos celda "Aceptar"
		printSetArea(canvasWidth/2, formBodyY + formBodyHeight - formBoxHeight, canvasWidth/2, formBoxHeight);
		//fontInit(FONT_SMALL_WHITE);
		if (aceptBox)
		{
			int sizeX = printStringWidth(gameText[TEXT_ACEPTAR][0]);
			int sizeL = printStringWidth(gameText[TEXT_ACEPTAR][0].substring(0,1));

			spriteDraw(formItemsImg, 0, 1, FORM_ITEM_CENEFA, formItemsCor, PRINT_FIT|PRINT_HCENTER|PRINT_VCENTER, printX + ((printWidth - sizeX)/2), printY, sizeL, printHeight);
		}
		printDraw(gameText[TEXT_ACEPTAR][0], 0,1, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

}







// -------------------
// formList Clear
// ===================

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


// -------------------
// form List String Width :: Calculamos el ancho maximo de la "columna" indicada.
// ===================

public int formListStringWidth(int index)
{
	int width = 0;
	for (int i=0 ; i<formListStr.length ; i++)
	{
		try {
			int size = printStringWidth(formListStr[i][index]);
			if (width < size) {width = size;}
		} catch(Exception e) {}
	}

	return width;
}






// <=- <=- <=- <=- <=-







// *******************
// -------------------
// popup - Engine
// ===================
// *******************
// -------------------


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


// Variables que nos informan del area que disponemos en el popup para nuestro uso customizado
int popupBodyX;
int popupBodyY;
int popupBodyWidth;
int popupBodyHeight;


// Variables para la gestion del texto del popup (area y posicion dentro del popup)
int popupTextX;
int popupTextY;
int popupTextWidth;
int popupTextHeight;
int popupTextIni;
int popupTextView;
String[] popupTextStr;


// Variables para la gestion del titulo del popup
int popupTitleHeight;
String popupTitleStr;


// Variables para la gestion de listados (menus/tablas/textos) dentro del popup
int popupListIni;
int popupListPos;
int popupListView;
String popupListStr[][];
int popupListDat[][];


// Variables para gestionar sliders
boolean popupVslider;
int popupVsliderY;
int popupVsliderHeight;


// Variables para la gestion del popup: ASK
boolean popupResult;	// Resultado del popup (true / false)


// Variables internas para la gestion del popupEngine
boolean popupShow;
boolean popupRunning;
int popupActionCancel;
int popupActionAcept;
int popupLeftSoftkeyBack;
int popupRightSoftkeyBack;


// -------------------
// popup Init Ask
// ===================

public void popupInitAsk(String titleStr, String[] str, int leftSoftkey, int rightSoftkey)
{
	popupClear();
	popupTitleStr = titleStr;
	popupTextStr = str;
	popupInit(POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, leftSoftkey, rightSoftkey);
}


// -------------------
// popup Init Info
// ===================

public void popupInitInfo(String titleStr, String[] str)
{
	popupClear();
	popupTitleStr = titleStr;
	popupTextStr = str;
	popupInit(POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_ACEPT);
}


// -------------------
// popup Clear
// ===================

public void popupClear()
{
	popupTitleStr = null;
	popupTextStr = null;
}


// -------------------
// popup Create
// ===================

public void popupCreate()
{
	int width = canvasWidth - (canvasWidth / 8);
	int height = canvasHeight - (canvasHeight / (canvasHeight<128? 3:2) );
	int x = (canvasWidth - width) / 2;
	int y = (canvasHeight - height) / 2;
	popupSetArea(x, y + (FORM_LOGO_HEIGHT/2), width, height);
}


// -------------------
// popup Destroy
// ===================

public void popupDestroy()
{
	popupClear();
}


// -------------------
// popup Set Area
// ===================

public void popupSetArea(int x, int y, int width, int height)
{
    popupX = x;
    popupY = y;
    popupWidth = width;
    popupHeight = height;
}

// -------------------
// popup Init
// ===================

public void popupInit(int actionCancel, int actionAcept, int leftSoftkey, int rightSoftkey)
{
// Inicializapos la posision inicial
	popupTextIni = 0;
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
	popupVslider = false;
	popupVsliderY = 0;

	int cursorX = POPUP_MARC_BORDER;
	int cursorY = POPUP_MARC_BORDER;


// popupTitle
	if (popupTitleStr != null)
	{
		//fontInit(FONT_SMALL_WHITE);

		popupTitleHeight = printGetHeight() + (FORM_MARC_HEIGHT / 2) + 2;

//		cursorY += popupTitleHeight;

		popupVsliderY += (popupTitleHeight - 1);
	}


// popupBody
	popupBodyX = cursorX;
	popupBodyY = cursorY;
	popupBodyWidth = popupWidth - cursorX - POPUP_MARC_BORDER;
	popupBodyHeight = popupHeight - cursorY - POPUP_MARC_BORDER;


// popupType
		//fontInit(FONT_SMALL_WHITE);
	
		popupTextWidth = popupBodyWidth;
		popupTextHeight = popupBodyHeight;
	
		popupTextStr = printTextBreak(popupTextStr, popupTextWidth - POPUP_SLIDER_WIDTH);
	
		popupTextView = popupTextHeight / printGetHeight();
	
		if (popupTextView >= popupTextStr.length)
		{
			popupTextView = popupTextStr.length;
		} else {
			popupTextView = popupTextHeight / printGetHeight();
			popupVslider = true;
			popupTextWidth -= POPUP_SLIDER_WIDTH;
		}
	
		int tmpHeight = popupTextView * printGetHeight();
	
		popupTextX = popupBodyX;
		popupTextY = popupBodyY + ((popupTextHeight - tmpHeight) / 2);
	
		popupTextHeight = tmpHeight;


// popupVslider
	popupVsliderHeight = popupHeight;
	popupVsliderY = popupY;


	popupRunning = true;
	popupShow = true;

	biosPush();
	biosStatus = BIOS_POPUP;
}


// -------------------
// popup Release
// ===================

public void popupRelease()
{
//#if DEBUG && DEBUG_MENU
	//Debug.println("popupRelease("+popupType+")");
//#endif

	popupShow = false;
	popupRunning = false;

//	popupLifeTime = -1;
	listenerInit(popupLeftSoftkeyBack, popupRightSoftkeyBack);
	gameForceRefresh = true;

 	popupCreate();	// hack para optimizar bytecode

	biosPop();
}


// -------------------
// popup Tick
// ===================

public boolean popupTick()
{
		if (keyMenu != 0 && lastKeyMenu == 0)
		{
			popupResult = keyMenu > 0;
			if ( (popupResult? popupActionAcept:popupActionCancel) == POPUP_ACTION_EXIT)
			{
				popupRelease();
				biosExit = true;		// Activamos salir del "biosWait()" actual
			}
		}

/*
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
*/
	// Controlamos si pulsamos PAD Arriba
		if (keyY < 0 && lastKeyY == 0 && popupTextIni > 0)
		{
			popupTextIni--;
			popupShow = true;
		}

	// Controlamos si pulsamos PAD Abajo
		if (keyY > 0 && lastKeyY == 0 && popupTextIni < popupTextStr.length - popupTextView)
		{
			popupTextIni++;
			popupShow = true;
		}

	return false;
}


// -------------------
// popup Draw
// ===================

public void popupDraw()
{
	if (!popupShow) {return;} else {popupShow = false;}


// Renderizamos fondo del popup
	if (menuImg != null)
	{
		imageDraw(menuImg, 0, 0);
	}


// Renderizamos el cuerpo del popup (interior liso y bordes)
	int POPUP_MARC_WIDTH = spriteWidth(formItemsCor, FORM_ITEM_BORDER + 4);
	int POPUP_MARC_HEIGHT = spriteHeight(formItemsCor, FORM_ITEM_BORDER + 4);

	int width = popupWidth / POPUP_MARC_WIDTH; if (popupWidth % POPUP_MARC_WIDTH != 0) {width++;}
	int height = popupHeight / POPUP_MARC_HEIGHT; if (popupHeight % POPUP_MARC_HEIGHT != 0) {height++;}
	for (int y=0 ; y<height ; y++)
	{
		for (int x=0 ; x<width ; x++)
		{
			int destX = regla3(x, width, popupWidth);
			int destY = regla3(y, height, popupHeight);

			int frame = FORM_ITEM_BORDER + 4;
			if (x==0) {frame--;} else if (x==width-1) {frame++;  destX = popupWidth - POPUP_MARC_WIDTH;}
			if (y==0) {frame-=3;} else if (y==height-1) {frame+=3; destY = popupHeight - POPUP_MARC_HEIGHT;}

			spriteDraw(formItemsImg, popupX + destX, popupY + destY, frame, formItemsCor);
		}
	}


// popupTitle: Render del titulo del popup
	if (popupTitleStr != null)
	{
		//fontInit(FONT_SMALL_WHITE);
		printSetArea(popupX, popupY, popupWidth, popupTitleHeight);

		printDraw(popupTitleStr, 0, -printGetHeight()-2, fontPenRGB, PRINT_LEFT|PRINT_TOP|PRINT_OUTLINE);
	}


// popupType: Render del cuerpo del popup (body) que depende del tipo de popup
		//fontInit(FONT_SMALL_WHITE);
		printSetArea(popupX + popupTextX, popupY + popupTextY, popupTextWidth, popupTextHeight);
	
		for (int i=0 ; i<popupTextView ; i++)
		{
			printDraw(popupTextStr[popupTextIni + i], 0, i*printGetHeight(), fontPenRGB, (popupTextView<=4? PRINT_HCENTER : PRINT_LEFT));
		}
	
		if (popupVslider)
		{
			sliderVDraw(popupTextIni, popupTextStr.length - popupTextView, (popupX + popupBodyX + popupTextWidth), popupY + popupBodyY, popupBodyHeight - 4);
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

		biosExit = true;		// Activamos salir del "biosWait()" actual
	break;
	}
}

// <=- <=- <=- <=- <=-




// ************************************************************************** //
// Final Clase Bios
// ************************************************************************** //














//#ifndef HIDE_SCROLL

// *******************
// -------------------
// Scroll - Engine v2.0 - Rev.11 (25.7.2005)
// ===================
// *******************
// Dise�ado y programado por Juan Antonio Gomez Galvez (2002-2005)

// El objetivo de este engine es el soporte de un sistema de scroll de funcionamiendo standard para la mayoria de moviles.
// Para que esto sea posible, este engine se puede configurar de direfentes formas para obtener los mejores resultados
// En funcion del movil que usemos.


// SYMBOLS:
// --------
// *** SCROLL_FULL_RENDER ***
// Renderizamos siempre todos los tiles en pantalla obteniendo un scroll pixel a pixel.
// Este sistema esta orientado para los terminales iMode que sean rapidos y asi obtener un scroll con desplazamiento pixel a pixel.


// *** TILE_RENDER ***
// Renderiamos en pantalla obteniendo un movimiento de scroll a base de tiles (no pixels)
// Este sistema esta orientado para los terminales iMode que son lentos de CPU y los terminales J2ME que disponen de poca memoria
// y no se pueden permitir tener un 'doblebufer' de toda la pantalla.

// Este sistema no refresca al 100% la pantalla, por lo tanto, todo lo que se dibuje encima del area de scroll,
// luego debe ser actualizado, la forma que comunicarle esto al engine es llamando al metodo:
// scrollUpdate(x,y,w,h) con el cuadrado modificado en al area de scroll.
// scrollUpdate() forzamos a que se refresque todo el area de scroll.


// *** SCROLL_BUFFER_RENDER ***
// Renderizamos en pantalla usando un doble bufer para ganar tiempo de CPU (scroll a pixles)
// Este sistema esta orientado a los terminales J2ME que son lentos de CPU y tienen un minimo de memoria para alojar un 'doblebufer'


// Metodos:
// --------
// A continuacion se explica el ciclo de vida de un scroll:

// scrollCreate()
// Creamos un area para alojar un scroll de mapa de tiles.

// scrollInit()
// Proporcionamos toda la informacion sobre el mapa, tiles, etc.. que sera renderizado en el area de scroll.

// scrollTick() (se llama en cada iteracion)
// Indicamos las coordenas X,Y del mapa de tiles (en pixels) para que el scroll se situe en esa coordenada.

// scrollDraw() (se llama en cada iteracion)
// Renderizamos el scroll en pantalla.

// scrollBorderMask()
// Solo para iMode, al renderizar sprites es posible que queden rastros por los perimetros del scroll,
// con este metodo definimos un cuadrado para pintar de un color solido 'scrollBorderRGB', exceptuando el area de scroll.

// scrollDestroy()
// Destruimos toda la informacion y recursos del scroll
 

// Diferentes sistemas de movimiento de scroll:
// --------------------------------------------
// scrollTick() (Standrad)
// Situamos el scroll en la coordenda X,Y obtenida.

// scrollTick_Max()
// Situamos el scroll en la coordenada X,Y pero respetando que el mapa no se desborde en los estremos.

// scrollTick_Center()
// Proporcionamos la coordenada X,Y que queremos que aparezca justo en el centro del scroll. (ideal para que el scroll siga a un sprite)

// scrollTick_CenterMax()
// Igual que 'scrollTick_Center()' pero respetando que el mapa no se desborde en los estremos.

// scrollTick_CenterMaxSlow()
// Igual que 'scrollTick_CenterMax()' pero creando un efecto de seguimiento. (el scroll se movera suevamente hasta la nueva corrdenada)

// scrollTick_CenterMaxSemiscroll()
// Gestionamos un semiscroll que consiste en no mover el scroll hasta que la coordenada dada este proxima a los extremos,
// una vez se de este cado, el scroll se movera poco a poco hasta situar esta coordenada en el contro de la pantalla.
// Este sistema es ideal para iMode con el sistema 'TILE_RENDER'


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

boolean scrollFirstTime;

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
	byte[] scrollTilesCor;
	Image scrollTilesImg;
	int scrollTilesBankWidth;
	int scrollTileBankSize;
//#elifdef DOJA
//#endif


///#ifdef SCROLL_BUFFER_RENDER
Image scrollFondoImg;
Graphics scrollFondoGfx;
//#endif




// -------------------
// scroll Create
// ===================

public void scrollCreate(int x, int y, int width, int height)
{
// Anotamos el area del canvas destinado a representar el scroll de tiles
	scrollAreaX = x;
	scrollAreaY = y;
	scrollAreaWidth = width;
	scrollAreaHeight = height;
}


// -------------------
// scroll Init
// ===================

//#ifdef J2ME
public void scrollInit(byte[] map, byte[] comb, int width, int height, Image img, byte[] cor, int bankWidth, int bankHeight)
//#elifdef DOJA
//#endif
{

// Anotamos Imagen que contiene los tiles
//#ifdef J2ME
	scrollTilesCor = cor;
	scrollTilesImg = img;
	scrollTilesBankWidth = bankWidth;
	scrollTileBankSize = bankWidth * bankHeight;
//#elifdef DOJA
//#endif


// Pillamos informacion del mapa
	//comb[0] = numero total de tiles combinados
	scrollTileCombFirst = comb[1] & 0xff;    // Numero de tile inicial para "tiles combinados"
	scrollTileCombCapas = comb[2] & 0xff;    // Numero de capas de los tiles combinados

// Anotamos variables del mapa, tama�o del mapa y tabla de tiles combinados
	scrollFaseMap = map;
	scrollFaseWidth = width;
	scrollFaseHeight = height;
	scrollTileComb = comb;

// Inicializamos variables a cero
	scrollFaseX = scrollFaseY = scrollFondoX = scrollFondoY = scrollX = scrollY = scrollBaseX = scrollBaseY = 0;
	scrollFirstTime = true;


//#ifdef SCROLL_TILE_RENDER
// Calculamos tama�o del scroll, para que sea multiplo al tama�o de los tiles
//#else
	scrollWidth = scrollAreaWidth;
	scrollHeight = scrollAreaHeight;
//#endif


// Calculamos tama�o del mapa usado como doble bufer y lo inicializamos
//#ifdef SCROLL_FULL_RENDER
//#elifdef SCROLL_TILE_RENDER
//#elifdef SCROLL_BUFFER_RENDER
	scrollFondoWidth = (scrollWidth / tileWidth) + ((scrollWidth % tileWidth == 0) ? 1 : 2);
	scrollFondoHeight = (scrollHeight / tileHeight) + ((scrollHeight % tileHeight == 0) ? 1 : 2);
//#endif


// Controlamos si la fase es mas peque�a que el area de scroll horizontalmente
	if (scrollFondoWidth > width) {
		scrollFondoWidth = width;
		scrollWidth = width * tileWidth;
	}

// Controlamos si la fase es mas peque�a que el area de scroll verticalmente
	if (scrollFondoHeight > height) {
		scrollFondoHeight = height;
		scrollHeight = height * tileHeight;
	}

//#ifndef SCROLL_FULL_RENDER
// Inicializamos mapa del doble bufer
	scrollFondoMap = new byte[scrollFondoWidth * scrollFondoHeight];
	for (int i = 0; i < scrollFondoMap.length; i++) {
		scrollFondoMap[i] = -1;
	}
//#endif


// Calculamos tama�o de los bordes para scroll mas peque�os que el area de scroll o el modo TILE_RENDER
	scrollBorderLeft = (scrollAreaWidth - scrollWidth) / 2;
	scrollBorderRight = scrollAreaWidth - scrollWidth - scrollBorderLeft;
	scrollBorderTop = (scrollAreaHeight - scrollHeight) / 2;
	scrollBorderBotton = scrollAreaHeight - scrollHeight - scrollBorderTop;


// Calculamos tama�o del mapa interno para usar "semiscroll"
	scrollBaseWidth = scrollWidth / scrollCortesX;
	scrollBaseHeight = scrollHeight / scrollCortesY;


// Calculamos eje X,Y a partir de donde se dibuja el cuadrado del scroll
	scrollViewX = scrollAreaX + scrollBorderLeft;
	scrollViewY = scrollAreaY + scrollBorderTop;


//#ifdef SCROLL_BUFFER_RENDER
// Generamos imagen para doble bufer
	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			if (scrollFondoImg == null) {
				scrollFondoImg = new Image();
				scrollFondoImg._createImage(scrollFondoWidth * tileWidth, scrollFondoHeight * tileHeight);
				scrollFondoGfx = scrollFondoImg.getGraphics();
			}
		}
	});

//#endif

}

// -------------------
// scroll Restore
// ===================

public void scrollRestore()
{
//#ifdef SCROLL_BUFFER_RENDER
	//#ifndef FIX_GC_GRAPHICS
	scrollFondoImg = null;
	scrollFondoGfx = null;
	//#endif
//#endif

	scrollFaseMap=null;
	scrollFondoMap=null;
//#ifdef J2ME
	scrollTilesImg=null;
//#elifdef DOJA
//#endif

}



// -------------------
// scroll Destroy
// ===================

public void scrollDestroy()
{
	scrollRestore();
}



// -------------------
// scroll Tick
// ===================

public void scrollTick(int X, int Y)
{

	while (X<0) {X+=(scrollFaseWidth*tileWidth);}
	while (Y<0) {Y+=(scrollFaseHeight*tileHeight);}

	while (X>=scrollFaseWidth*tileWidth) {X-=(scrollFaseWidth*tileWidth);}
	while (Y>=scrollFaseHeight*tileHeight) {Y-=(scrollFaseHeight*tileHeight);}

//	scrollX = X % (scrollFaseWidth * tileWidth);
//	scrollY = Y % (scrollFaseHeight * tileHeight);

	scrollX = X;
	scrollY = Y;

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
//#endif
}




public void scrollUpdate(int X, int Y, int SizeX, int SizeY)
{
//#ifdef SCROLL_TILE_RENDER
//#endif
}





//#ifdef SCROLL_BUFFER_RENDER

// -------------------
// scroll Buffer Update
// ===================

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

								if (tileNum < 0)
								{
									scrollFondoGfx.setColor(0);
									scrollFondoGfx.fillRect(x*tileWidth ,y*tileHeight,  tileWidth,tileHeight);
									continue;
								}

								if (tileNum+1 < scrollTileCombFirst)
								{
/*
								if (tileNum < scrollTileBankSize)
								{
									scrollFondoGfx.drawImage(scrollTilesAImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
								} else {
									tileNum -= scrollTileBankSize;
									scrollFondoGfx.drawImage(scrollTilesBImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
								}
*/
//								spriteDraw(scrollFondoGfx, scrollTilesImg, (x*tileWidth), (y*tileHeight), tileNum, scrollTilesCor);
									tileNum*=6; int destX = scrollTilesCor[tileNum++] + (x*tileWidth); int destY = scrollTilesCor[tileNum++] + (y*tileHeight);
									scrollFondoGfx.setClip(destX, destY, scrollTilesCor[tileNum++], scrollTilesCor[tileNum++]);
									scrollFondoGfx.drawImage(scrollTilesImg, destX-(scrollTilesCor[tileNum++]+128), destY-(scrollTilesCor[tileNum]+128), 20);
								} else {

									int comTile = 3+((tileNum+1 - scrollTileCombFirst)*scrollTileCombCapas);
									for (int z=0 ; z<scrollTileCombCapas ;z++)
									{
										tileNum = scrollTileComb[comTile++]-1;
										if (tileNum < 0) {continue;}
/*
									if (tileNum < scrollTileBankSize)
									{
										scrollFondoGfx.drawImage(scrollTilesAImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
									} else {
										tileNum -= scrollTileBankSize;
										scrollFondoGfx.drawImage(scrollTilesBImg, (x*tileWidth)-((tileNum%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileNum/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);
									}
*/
//									spriteDraw(scrollFondoGfx, scrollTilesImg, (x*tileWidth), (y*tileHeight), tileNum, scrollTilesCor);
										tileNum*=6; int destX = scrollTilesCor[tileNum++] + (x*tileWidth); int destY = scrollTilesCor[tileNum++] + (y*tileHeight);
										scrollFondoGfx.setClip(destX, destY, scrollTilesCor[tileNum++], scrollTilesCor[tileNum++]);
										scrollFondoGfx.drawImage(scrollTilesImg, destX-(scrollTilesCor[tileNum++]+128), destY-(scrollTilesCor[tileNum]+128), 20);
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
			//#ifdef Debug
			Debug.println("scrollUpdate FAIL");
			//#endif
		}
	}
//#endif




// -------------------
// scroll Draw
// ===================

public void scrollDraw(Graphics scr)
{
//#ifdef SCROLL_BUFFER_RENDER

	int x = scrollX % tileWidth;
	int y = scrollY % tileHeight;

	int sx = ((scrollFondoWidth-scrollFondoX)*tileWidth)-x;
	int sy = ((scrollFondoHeight-scrollFondoY)*tileHeight)-y;

	int px = scrollWidth-sx;
	int py = scrollHeight-sy;

	int nx =- (scrollFondoX*tileWidth)-x;
	int ny =- (scrollFondoY*tileHeight)-y;


	if (scrollFondoY == 0)
	{
		if (scrollFondoX == 0)
		{
			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, 20);
		} else {
			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect( scrollViewX,  scrollViewY, sx, scrollHeight);
			scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, 20);

			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect(sx+scrollViewX,  scrollViewY, px, scrollHeight);
			scr.drawImage(scrollFondoImg, sx+scrollViewX, ny+scrollViewY, 20);
		}
	} else {
		if (scrollFondoX == 0)
		{
			if (sy > 0)
			{
				scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
				scr.clipRect( scrollViewX,  scrollViewY, scrollWidth, sy);
				scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, 20);
			}

			if (py > 0)
			{
				scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
				scr.clipRect( scrollViewX, sy+scrollViewY, scrollWidth, py);
				scr.drawImage(scrollFondoImg, nx+scrollViewX, sy+scrollViewY  , 20);
			}
		} else {
			if (sy > 0)
			{
				if (sx > 0)
				{
					scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
					scr.clipRect( scrollViewX,  scrollViewY, sx, sy);
					scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, 20);
				}

				if (px > 0)
				{
					scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
					scr.clipRect(sx+scrollViewX,  scrollViewY, px, sy);
					scr.drawImage(scrollFondoImg, sx+scrollViewX, ny+scrollViewY, 20);
				}
			}

			if (py > 0)
			{
				if (sx > 0)
				{
					scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
					scr.clipRect( scrollViewX, sy+scrollViewY, sx, py);
					scr.drawImage(scrollFondoImg, nx+scrollViewX, sy+scrollViewY  , 20);
				}

				if (px > 0)
				{
					scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
					scr.clipRect(sx+scrollViewX, sy+scrollViewY, px, py);
					scr.drawImage(scrollFondoImg, sx+scrollViewX, sy+scrollViewY  , 20);
				}
			}
		}
	}


//#else
//#endif
}


// -------------------
// scroll Border Mask
// ===================

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


// -------------------
// scroll Sprite Draw
// ===================

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

// <=- <=- <=- <=- <=-

//#endif





















// *******************
// -------------------
// connect - Engine
// ===================
// *******************

//#ifndef HIDE_CONNECTIVITY

String userHashId = "UNREGISTERED";

static final int DOWNLOAD_HISCORE = 0;
//static final int UPLOAD_HISCORE = 1;
static final int RETURN_HASH_OK = 2;

static final int HASH_ID_CONFIRMATION = 99;

static final int DELETE_FILE = 1;
static final int CHANGE_PRIORITY = 2;
static final int NEW_LOGO = 3;
static final int NEW_IMAGE = 4;
static final int NEW_FILE = 5;
static final int SCORES_DATA = 6;
static final int HIGHSCORE_SCROLLER = 7;
static final int NEW_HASH_ID = 9;

static final int LENGTH_INDEX = 8;
static final int HEADER_SIZE = 36;




boolean connectDownloadOk;
boolean connectDownloadAbort;

int connectType;

int connectPacketId;

int connectResultId;
String connectMessage;

boolean connectConfirmHash;

// -------------------
// connect Request
// ===================

public void connectRequest(int type)
{
//#ifdef DEBUG
	Debug.println("=-=-=-=-=-=-=>");
	Debug.println("connect Request: " + type + " userHasdId: "+userHashId);
//#endif

	connectType = type;
	connectDownloadOk = false;
	connectDownloadAbort = false;

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	byte[] data = null;

	try
	{
		dos.writeInt(0x5468696E);		// Magic
		dos.writeInt(0x43617421);		// Word

		dos.writeInt(0);				// Longitud total del Paquete

		dos.writeInt(1);				// protocolId
		dos.writeInt(!connectConfirmHash? ++connectPacketId:connectPacketId);// packetId
		dos.writeInt(ID_GAME);			// gameId (1-001 = CasualGames/MoviDomino)
		dos.writeInt(ID_HANDSET);		// hansetId (1-01 = MO-V3xx)
		dos.writeInt(ID_CARRIER);		// carrierId (1-01 = Spain/Movistar)

		switch (type)
		{
		case DOWNLOAD_HISCORE:

			connectAddHeader(1, dos);

			dos.writeUTF(gamePlayerNames[1]);

			dos.writeInt(playerSelected);	// Cara: Esto se ha de programar para cada juego
			dos.writeInt(hiscore);			// Puntos: Esto se ha de programar para cada juego
		break;
/*
		case UPLOAD_HISCORE:

			connectAddHeader(1, dos);

			dos.writeUTF(gamePlayerNames[1]);
			dos.writeInt(playerSelected);	// Cara: Esto se ha de programar para cada juego
			dos.writeInt(hiscore);			// Puntos: Esto se ha de programar para cada juego
		break;
*/
		case RETURN_HASH_OK :

		    connectAddHeader(HASH_ID_CONFIRMATION, dos);
		break;
		}

	// Calculamos y guardamos CRC
		dos.writeInt(0);
		data = baos.toByteArray();
		convInt2Ary(data, LENGTH_INDEX, data.length);		// Guardamos Tama�o de los datos
		convInt2Ary(data, data.length - 4, connectCRC(data));

	} catch (Exception e)
	{
	//#ifdef DEBUG
		Debug.println(e.toString());
		Debug.println("connectEngine: outPutStream Error!");
		e.printStackTrace();
	//#endif
		return;
	}

	connectCreate(gameText[TEXT_SERVER_URL][1], data);

	new Thread((Runnable) ga).start();
}

// -------------------
// connect Add Header
// ===================

public void connectAddHeader(int command, DataOutputStream dos) throws Exception
{
	dos.writeInt(command);		// commandId: Download hiscore
	dos.writeUTF(userHashId);	// user Hash Id

// Enviamos todas las ID de las fichas que tenemos almacenadas
	dos.writeInt(cacheC);			// Numero de fichas en JAR/RMS
	for (int i=0 ; i<cacheC ; i++)
	{
		dos.writeInt(cacheFichaId[cacheP[i]]);		// Ids de las fichas
	}

// Enviamos si hemos visitado o no el menu MAS JUEGOS
	dos.writeByte(cacheFichasVisibles==0 || gameMoreGamesVisited?1:0);	// visitado mas juegos?
}

// -------------------
// connect Action
// ===================

public boolean connectAction(DataInputStream dis)
{
//#ifdef DEBUG
	Debug.println("connectAction()");
//#endif

	try {
	// common ThinCat header
		int packedId = dis.readInt();

	// Si es la respuesta al paquete que pedimos, lo procesamos
		if ( packedId == connectPacketId)
		{
			connectResultId = dis.readInt();
			connectMessage = dis.readUTF();

		// Controlamos que el resultado sea correcto (0: ok, 1:Error con mensage, 2:lanzar Browser)
			if (connectResultId != 0) {return false;}

			int size = dis.readInt();

		// common Domino header
			userHashId = dis.readUTF();

		//#ifdef DEBUG
			Debug.println ("packedId:"+packedId);
			Debug.println ("resultId:"+connectResultId);
			Debug.println ("message:"+connectMessage);
			Debug.println ("chunk size:"+size);
			Debug.println ("userHashId:"+userHashId);
			Debug.println ("packedId:"+packedId+", connectPacketId:"+connectPacketId);
		//#endif

			int chunks = dis.readInt();

			//#ifdef DEBUG
				Debug.println ("numero de chunks:"+chunks);
			//#endif

		// Procesamos todos los chunks que hay en el paquete
			for (int c=0 ; c<chunks ; c++)
			{
				int chunkId = dis.readInt();
				int chunkSize = dis.readInt();

			//#ifdef DEBUG
				Debug.println ("Procesamos chunk: "+c);
				Debug.println ("chunk ID:"+chunkId);
				Debug.println ("chunkSize:"+chunkSize);
			//#endif

				switch (chunkId)
				{
				case NEW_HASH_ID:

				//#ifdef DEBUG
					Debug.println (">> NEW_HASH_ID >>>>>>>");
				//#endif

					savePrefs();

					connectConfirmHash = true;
				break;

			//#ifndef HIDE_MORE_GAMES
				case DELETE_FILE:

				//#ifdef DEBUG
					Debug.println (">> DELETE_FILE >>>>>>>");
				//#endif

					cacheDeleteFicha(dis.readInt());

					rmsSaveFile("list", cacheGenerator());
				break;

				case CHANGE_PRIORITY:
	
				//#ifdef DEBUG
					Debug.println (">> CHANGE_PRIORITY >>>>>>>");
				//#endif

					int id = cacheFichaFind(dis.readInt());
					int newPrio = dis.readInt();
					if (id >= 0)
					{
						cachePrio[id] &= 0xffff;
						cachePrio[id] |= newPrio<<16;
					}
					rmsSaveFile("list", cacheGenerator());
				//#ifdef DEBUG
					Debug.println ("Ficha Id:"+cacheFichaId[id]+" newPrio:"+newPrio);
				//#endif

				break;

				case NEW_IMAGE:
	
				//#ifdef DEBUG
					Debug.println (">> NEW_IMAGE >>>>>>>");
				//#endif

					int imageId = dis.readInt();
					byte[] data = new byte[dis.readInt()];
					dis.read(data);

				// Buscamos si tenemos alguna ficha que use el logo descargado, si es asi, lo guardamos en el rms, sino pasamos de guardarlo
					for (int i=0 ; i<cacheC ; i++)
					{
						if (imageId == cacheImageId[cacheP[i]])
						{
							rmsSaveFile("/i"+imageId, data);
							break;
						}
					}
				break;
	
				case NEW_LOGO:
	
				//#ifdef DEBUG
					Debug.println (">> NEW_LOGO >>>>>>>");
				//#endif

					int logoId = dis.readInt();
					data = new byte[dis.readInt()];
					dis.read(data);

				// Buscamos si tenemos alguna ficha que use la imagen descargada, si es asi, lo guardamos en el rms, sino pasamos de guardarlo
					for (int i=0 ; i<cacheC ; i++)
					{
						if (logoId == cacheLogoId[cacheP[i]])
						{
							rmsSaveFile("/l"+logoId, data);
							break;
						}
					}
				break;
	
				case NEW_FILE:

				//#ifdef DEBUG
					Debug.println (">> NEW_FILE >>>>>>>");
				//#endif

					int fileId = dis.readInt();
					int fileImageId = dis.readInt();
					int fileImageSize =  dis.readInt();
					int fileLogoId = dis.readInt();
					int fileLogoSize =  dis.readInt();
					String fileURL = dis.readUTF();
					String fileScrText = dis.readUTF();
					String fileAltText = dis.readUTF();
					String fileSoftkey = dis.readUTF();

				//#ifdef DEBUG
					Debug.println("FichaId:" + fileId);
					Debug.println("Espacio libre en rms:" + rmsAvailable());
					Debug.println("Espacio Requerido:" + (fileImageSize + fileLogoSize + 2024));
				//#endif


				// Si no hay espacio en rms, borramos fichas viejas
					int pos = cacheC-1;
					while (rmsAvailable() < fileImageSize + fileLogoSize + 2024)
					{
						if (pos == -1) {pos = -123; break;}

						if ((cachePrio[cacheP[pos]]>>16) != 1)
						{
						//#ifdef DEBUG
							Debug.println("Borrando fichaId:" + cacheFichaId[cacheP[pos]]);
						//#endif
	
							cacheDeleteFicha(cacheFichaId[cacheP[pos]]);
						}

						pos--;
					}
					if (pos == -123)
					{
					//#ifdef DEBUG
						Debug.println("No hay espacion para almacenar la ficha:"+fileId);
					//#endif
					} else {
						cacheListAdd(fileId, fileImageId, fileLogoId, -1, fileURL, fileScrText, fileAltText, fileSoftkey);
					}

					rmsSaveFile("list", cacheGenerator());

					gameMoreGamesVisited = false;
					savePrefs();
				break;
			//#endif
	
				case SCORES_DATA:

				//#ifdef DEBUG
					Debug.println (">> SCORES_DATA >>>>>>>");
				//#endif
					scoreFirst = dis.readInt();
					int numPlayers = dis.readInt();

					scoreNick = new String[numPlayers];
					scoreFace = new int[numPlayers];
					scorePoints = new int[numPlayers];

					for (int i=0 ; i<numPlayers ; i++)
					{
						scoreNick[i] = dis.readUTF();
						scoreFace[i] = dis.readInt();
						scorePoints[i] = dis.readInt();
					//#ifdef DEBUG
						Debug.println (i+": "+scoreNick[i]+" , cara: "+scoreFace[i]+" , puntos: "+scorePoints[i]);
					//#endif
					}
				break;
	
				case HIGHSCORE_SCROLLER:
	
				//#ifdef DEBUG
					Debug.println (">> HIGHSCORE_SCROLLER >>>>>>>");
				//#endif

					hiscore_scrollText = dis.readUTF();

				//#ifdef DEBUG
					Debug.println (hiscore_scrollText);
				//#endif
				break;
	
				default:
	
				//#ifdef DEBUG
					Debug.println (">> default >>>>>>>");
				//#endif

					for (int i=0 ; i<chunkSize ; i++) {dis.readByte();}
				break;
				}

			//#ifdef DEBUG
				Debug.println ("<<<<<<<<<<<<<<<<<");
				Debug.println (" ");
			//#endif
			}
		}

	} catch (Exception e)
	{
	//#ifdef DEBUG
		Debug.println(e.toString());
		Debug.println("connectEngine: connectAction() inputStream Error!");
		e.printStackTrace();
	//#endif
		return false;
	}

	return true;
}


// -------------------
// connect CRC
// ===================

public int connectCRC(byte[] data)
{
	int checksum = 0;
	for (int pos = 0, i = HEADER_SIZE; i < data.length - 4 ; pos++, i++)
	{
		checksum += data[i];
		checksum = checksum ^ pos;
	}
	return checksum;
}


static public byte[] connectResult = null;

static public boolean connectFinished = false;
static public boolean connectRunning = false;
static public int connectNetRequestCount = 0;
static public int connectStatus;

private String connectUrl;
private byte[] connectData;


// -------------------
// connect Create
// ===================

public void connectCreate(String u, byte[] in)
{
	connectUrl = u;
	connectData = in;

	connectNetRequestCount++;

	connectFinished = false;
	connectRunning = true;
}

// -------------------
// connect run :: Creamos un nueva Thread para la conexion
// ===================

public void connectRun()
{
//#ifdef DEBUG
	Debug.println("Thread: HTTP_RUNNING");
//#endif

	int currentCount = connectNetRequestCount;

	try {
		byte[] temp = connectPost(connectData);

	// Si este Thread SE cancela, descartamos la respuesta, para evitar que se pise una conexion nueva contra una cancelada
		if (currentCount != connectNetRequestCount)
		{
		//#ifdef DEBUG
			Debug.println(">>>>>>>>>>>>>");
			Debug.println("> Paquete recibido y descartado por Abort/New conexion");
			Debug.println("<<<<<<<<<<<<<");
		//#endif
			return;
		}

		connectResult = temp;

	} catch (Exception e)
	{
	//#ifdef DEBUG
		Debug.println("Thread: HTTP_ERROR:");
		Debug.println(e.toString());
		e.printStackTrace();
	//#endif
		connectResult = null;
	}

	connectRunning = false;
	connectFinished = true;

//#ifdef DEBUG
	Debug.println((connectResult == null? "HTTP_result = null" : "HTTP_result = "+connectResult.length+"bytes"));
	Debug.println("Thread: HTTP_FINISHED");
//#endif
}

// -------------------
// connect Post
// ===================

public byte[] connectPost(byte[] data) throws IOException
{
	byte[] res = null;
	/*HttpConnection c = null;
	OutputStream os = null;
	InputStream is = null;
	try {
		c = (HttpConnection)Connector.open(connectUrl);
		c.setRequestProperty("Connection", "close");
		c.setRequestMethod(HttpConnection.POST);
		c.setRequestProperty("Content-Type", "application/mjbinary");

		// Set the POST data
		// nota -> a partir de open output stream no se puede llamar a metodos que impliquen
		// cambiar los headers. en el wtk rula pero en el resto del trastos peta.

		os = c.openOutputStream();

		// Si ya estamos logados; lo primero que hay que enviar es la cookie y el
		// ultimo server time

		os.write(data);

		os.flush();

		os.close();
		os = null;

	//#ifdef DOJA
	//#endif
		// Get the status code, causing the connection to be made
		connectStatus = c.getResponseCode();

		// Only HTTP_OK (200) means the content is returned.
		if (connectStatus != HttpConnection.HTTP_OK)
		{
		//#ifdef DEBUG
			throw new IOException("Response status not OK [" + connectStatus + "]");
		//#else
		//#endif
		}

	// Agregar la respuesta al buffer de recepcion
		is = c.openInputStream();
		ByteArrayOutputStream tos = new ByteArrayOutputStream();
		try {

			byte[] buffer = new byte[512];
			int size = 0;
			do {
				size = is.read(buffer, 0, buffer.length);
				if (size > 0) {tos.write(buffer, 0, size);}
			} while (size >= 0);

			tos.flush();
			
			res = tos.toByteArray();
			
			tos.close();
			tos = null;
		} finally {
			if (tos != null) {
				tos.close();
			}
		}
		
		is.close();
		c.close();
		is = null;
		c = null;
	} finally {
		if (os != null) {
			os.close();
		}
		if (is != null) {
			is.close();
		}
		if (c != null) {
			c.close();
		}
		
		System.gc();
	}*/
	
	return res;
}


// -------------------
// connect Wait
// ===================

public int connectWait()
{
// Mostramos popup de "Conectando..."
	popupClear();
	popupTextStr = gameText[TEXT_CONECTANDO];
//	popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
	popupInit(POPUP_ACTION_EXIT, POPUP_ACTION_NONE, SOFTKEY_CANCEL, SOFTKEY_NONE);

	waitStart();

	while (true)
	{
	// Ejecutamos un tick de la bios para no perder la ejecucion de otras 'tareas'
		biosLoop();


	// Controlamos si la conexion ha terminado
		if (connectFinished)
		{
		// Si esta activo el popup de "conectando" lo quitamos
			if (popupRunning)
			{
				popupRelease();
 			}

		// Si no hemos recibido nada, ha ocurrido un error de red
			if (connectResult == null)
			{
			// Mostramos popup de error
				popupClear();
				popupTextStr = gameText[TEXT_CONNECT_ERROR];
//				popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
				popupInit(POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, SOFTKEY_REINTENTAR, SOFTKEY_ACEPT);

				biosWait();

				return (popupResult? 0 : -2);	// Aceptar : Reintentar
			}

		// Mostramos popup informando que estamos procesando los datos
			popupClear();
			popupTextStr = gameText[TEXT_ESPERE_PROCESANDO];
//			popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
			popupInit(POPUP_ACTION_NONE, POPUP_ACTION_NONE, SOFTKEY_NONE, SOFTKEY_NONE);

			forceRender();

		// Procesamos los datos recibidos solicitados al servidor
			boolean ok = connectAction(new DataInputStream(new ByteArrayInputStream(connectResult)));

			connectResult = null;
			System.gc();

		// Eliminamos popup de informacion
			popupRelease();

		// Si la respuesta del servidor es de error, lo controlamos
			if (!ok)
			{
			// Si es mensaje de error, mostramos mensaje de error enviado por el servidor
				if (connectResultId == 1)
				{
				// Mostramos popup de error
					popupClear();
					popupTextStr = new String[] {connectMessage};
//					popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
					popupInit(POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, SOFTKEY_REINTENTAR, SOFTKEY_ACEPT);

					biosWait();

					return (popupResult? 0 : -2);
				}

			// Si es peticion de lanzar Browser...
//				if (resultId == 2)
//				{
//				}
			}

			return 1;	// Descarga Ok
		}

	// Controlamos si hemos salido del popup, por lo tanto, cancelado la peticion de red
		if (!popupRunning)
		{
			connectNetRequestCount++;	// Incrementamo numero de peticion, de esta forma, cuando recibibamos el paquete solicitado se descartara...
			return -1;
		}

	// Controlamos si hemos escedido el tiempo de espera
		if (waitFinished(30000))
		{
		// Si esta activo el popup de "conectando" lo quitamos
			if (popupRunning)
			{
				popupRelease();
			}

		// Mostramos popup informando que estamos procesando los datos
			popupClear();
			popupTextStr = gameText[TEXT_CONNECT_TIMEOUT];
//			popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
			popupInit(POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, SOFTKEY_REINTENTAR, SOFTKEY_ACEPT);

			forceRender();

			biosWait();

		// Si pulsamos aceptar, es como si cancelasemos la conexion, sino queremos ayuda ahora
			return (popupResult? -1 : -2);
		}
	}
}

//#endif

// <=- <=- <=- <=- <=-








// *******************
// -------------------
// cache - Engine
// ===================
// *******************

static final int cacheMAX = 32;

int cacheFichasVisibles;

int cacheC;
int[] cacheP;

int[] cacheFichaId;
int[] cacheImageId;
int[] cacheLogoId;
int[] cachePrio;
String[] cacheURL;
String[] cacheScrTxt;
String[] cacheAltTxt;
String[] cacheSoftkey;

// -------------------
// cache Create
// ===================

public void cacheCreate()
{
	cacheC = 0;
	cacheP = new int[cacheMAX];
	for (int i=0 ; i<cacheMAX ; i++) {cacheP[i] = i;}

	cacheFichaId = new int[cacheMAX];
	cacheImageId = new int[cacheMAX];
	cacheLogoId = new int[cacheMAX];
	cachePrio = new int[cacheMAX];

	cacheURL = new String[cacheMAX];
	cacheScrTxt = new String[cacheMAX];
	cacheAltTxt = new String[cacheMAX];
	cacheSoftkey = new String[cacheMAX];


	for (int i=0 ; i<cacheMAX ; i++)
	{
		cacheFichaId[i] = -1;
		cacheImageId[i] = -1;
		cacheLogoId[i] = -1;
		cachePrio[i] = -1;

		cacheURL[i] = " ";
		cacheScrTxt[i] = " ";
		cacheAltTxt[i] = " ";
		cacheSoftkey[i] = " ";
	}


	byte[] in = rmsLoadFile("list");

	if (in == null || cacheReader(in))
	{
		byte[] bin = null; //MONGOFIX loadFile("/fichas.txt");
		if (bin != null)
		{
			String[][] str = textosCreate(bin);

			for (int i=0 ; i<str.length ; i++)
			{
			    cacheListAdd(Integer.parseInt(str[i][0]), Integer.parseInt(str[i][1]), Integer.parseInt(str[i][2]), -2, str[i][3], str[i][4], str[i][5], str[i][6]);
			}
		}

		if (rmsSaveFile("list", cacheGenerator()))
		{
			cacheFichasVisibles = 0;
			cacheC = 0;
		}
	}

}





// -------------------
// cache Ficha Find
// ===================

int cacheFindPos;

public int cacheFichaFind(int id)
{
	for (int i=0 ; i<cacheC ; i++)
	{
		if (cacheFichaId[cacheP[i]] == id) {cacheFindPos = i; return cacheP[i];}
	}

	cacheFindPos = -1;
	return -1;
}


// -------------------
// cache List Add
// ===================

public int cacheListAdd(int fichaId, int logoId, int imageId, int prio, String url, String scrText, String altText, String softkey)
{
	if (cacheC == cacheMAX) {return -1;}

	int id = cacheP[cacheC++];

	cacheFichaId[id] = fichaId;
	cacheImageId[id] = logoId;
	cacheLogoId[id] = imageId;

	if (prio == -2)
	{
		cachePrio[id] = 1<<16;
		antiguedad++;
	}
	else if (prio != -1)
	{
		cachePrio[id] = prio;
	} else {
		cachePrio[id] = 9<<16;
		cachePrio[id] += antiguedad++;
	}

	cacheURL[id] = url;
	cacheScrTxt[id] = scrText;
	cacheAltTxt[id] = altText;
	cacheSoftkey[id] = softkey;

	return id;
}

// -------------------
// cache List Del
// ===================

public void cacheListDel(int pos)
{
	if (cacheC > pos)
	{
		int adr = cacheP[pos];
		cacheP[pos] = cacheP[--cacheC];
		cacheP[cacheC] = adr;
	}
}


// -------------------
// cache Generator
// ===================

public byte[] cacheGenerator()
{
	cachePrioOrder();

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	try {
		dos.writeInt(cacheC);	// Numero de Fichas

		for (int i=0 ; i<cacheC ; i++)
		{
			int id = cacheP[i];

			dos.writeInt(cacheFichaId[id]);
			dos.writeInt(cacheImageId[id]);
			dos.writeInt(cacheLogoId[id]);
			dos.writeInt(cachePrio[id]);

			dos.writeUTF(cacheURL[id]);
			dos.writeUTF(cacheScrTxt[id]);
			dos.writeUTF(cacheAltTxt[id]);
			dos.writeUTF(cacheSoftkey[id]);
		}

	} catch (Exception e)
	{
	//#ifdef DEBUG
		Debug.println ("cacheGeneratorError:  "+e.toString());
	//#endif
	}

	return baos.toByteArray();
}

// -------------------
// cache Reader
// ===================

public boolean cacheReader(byte[] in)
{
	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(in));

	try {
		int fichas = dis.readInt();		// Numero de fichas

		for (int i=0 ; i<fichas ; i++)
		{
			cacheListAdd(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt(), dis.readUTF(), dis.readUTF(),dis.readUTF(), dis.readUTF());
		}
	} catch (Exception e)
	{
	//#ifdef DEBUG
		Debug.println ("cacheReaderError:  "+e.toString());
	//#endif
		return true;
	}

	cachePrioOrder();

	return false;
}


// -------------------
// cache Prio Order :: Ordenamos todas las fichas segun su prioridad, de mayor a menor
// ===================

public void cachePrioOrder()
{
	int conta = cacheC - 1;

	for (int t=0 ; t<cacheC ; t++)
	{
		for (int i=0 ; i<conta ; i++)
		{
			if (cachePrio[cacheP[i]] < cachePrio[cacheP[i+1]])
			{
			int dato = cacheP[i+1];
			cacheP[i+1] = cacheP[i];
			cacheP[i] = dato;
			}
		}
	conta--;
	}

	cacheFichasVisibles = 0;
	for (int i=0 ; i<cacheC ; i++)
	{
		if (cachePrio[cacheP[i]]>>16 != 0)
		{
			cacheFichasVisibles++;
		}
	}
}


// -------------------
// cache Delete Ficha
// ===================

public void cacheDeleteFicha(int id)
{
	id = cacheFichaFind(id);

	if (id >= 0)
	{
		boolean used = false;
		for (int i=0 ; i<cacheC ; i++) {if (id != cacheP[i] && cacheLogoId[cacheP[i]] == cacheLogoId[id]) {used = true;}}
		if (!used) {rmsDeleteFile("/l"+cacheLogoId[id]);}

		used = false; for (int i=0 ; i<cacheC ; i++) {if (id != cacheP[i] && cacheImageId[cacheP[i]] == cacheImageId[id]) {used = true;}}
		if (!used) {rmsDeleteFile("/i"+cacheImageId[id]);}

		cacheListDel(cacheFindPos);
	}
}

// <=- <=- <=- <=- <=-






















// ************************************************************************** //
// Inicio Clase com.mygdx.mongojocs.sanfermines2006.Game
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
final static int TEXT_PLAY				= 0;
final static int TEXT_CONTINUE			= 1;
final static int TEXT_OPT_SOUND			= 2;
final static int TEXT_OPT_VIBRA			= 3;
final static int TEXT_OPT_DIFFICULTY	= 4;
final static int TEXT_GAMEOVER			= 5;
final static int TEXT_HELP				= 6;
final static int TEXT_ABOUT				= 7;
final static int TEXT_RESTART			= 8;
final static int TEXT_EXIT				= 9;
final static int TEXT_LOADING			= 10;
final static int TEXT_WAIT				= 11;
final static int TEXT_LEVEL				= 12;
final static int TEXT_LEVEL_COMPLETED	= 13;
final static int TEXT_CONGRATULATIONS	= 14;
final static int TEXT_ARE_YOU_SURE		= 15;
final static int TEXT_SOFTKEYS			= 16;
final static int TEXT_PLAYER_SELECT		= 17;
final static int TEXT_ABOUT_SCROLL		= 18;

final static int TEXT_OPTIONS			= 19;
final static int TEXT_NEWGAME			= 20;
final static int TEXT_GAME_WITH_HELP	= 21;

final static int TEXT_EDIT_NAMES		= 22;
final static int TEXT_ABECEDARIO		= 23;
final static int TEXT_BORRAR			= 24;
final static int TEXT_ACEPTAR			= 25;

final static int TEXT_PLAYER_NAMES		= 26;
final static int TEXT_PLAYERS_NAMES		= 27;

final static int TEXT_CONFIRMATION		= 28;
final static int TEXT_GAME_WITH_SOUND	= 29;
final static int TEXT_ENTER_NAME		= 30;
final static int TEXT_MORE_GAMES		= 31;
final static int TEXT_HI_SCORES			= 32;
final static int TEXT_HELP_INTRODUCCION	= 33;
final static int TEXT_HELP_COMO_JUGAR	= 34;
final static int TEXT_HELP_COMO_GANAR	= 35;
final static int TEXT_HELP_RECORDS		= 36;
final static int TEXT_HELP_CONTROLES	= 37;

final static int TEXT_PLAYER_LOCKED		= 38;

final static int TEXT_GAME_IN_PAUSE		= 39;
final static int TEXT_CLASIFICATION		= 40;
final static int TEXT_DESCARGAR_SCORES	= 41;
final static int TEXT_CONECTANDO		= 42;
final static int TEXT_QUIERES_SUBIR_SCORE	= 43;

final static int TEXT_TU_PUNTUACION		= 44;

final static int TEXT_ESPERE_PROCESANDO	= 45;
final static int TEXT_CONNECT_TIMEOUT	= 46;
final static int TEXT_CONNECT_ERROR		= 47;
final static int TEXT_INTRODUCIR_NOMBRE_SURE	= 48;
final static int TEXT_CONECTIVIDAD		= 49;

final static int TEXT_NAME				= 50;
final static int TEXT_RECUENTO			= 51;
final static int TEXT_ENCIERRO			= 52;
final static int TEXT_SOBREVIVE			= 53;
final static int TEXT_RECORD_ACTUAL		= 54;

final static int TEXT_SCORE_NAMES		= 55;
final static int TEXT_DISCLAIMER		= 56;
final static int TEXT_RESULT_COMMENTS	= 57;
final static int TEXT_LETRA_CONSEGUIDA	= 58;

final static int TEXT_TUTORIAL			= 59;

final static int TEXT_CARRIER_ID		= 60;
final static int TEXT_SERVER_URL		= 61;

final static int TEXT_SELECT_LEVEL		= 62;

final static int TEXT_FINALIZANDO_INSTALACION = 63;

final static int TEXT_ENCIERRO_COMPLETADO = 64;

final static int TEXT_SOBREVIBE_DESBLOQUEADO = 65;
final static int TEXT_TORERO_DESBLOQUEADO = 66;
final static int TEXT_SOBREVIBE_TORERO_DESBLOQUEADO = 67;

final static int TEXT_MORE_GAMES_POPUP = 68;
// ===============================================


// -----------------------------------------------
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int GAME_START = 0;
final static int GAME_LOGOS = 5;

final static int GAME_MENU_START = 10;
final static int GAME_MENU_MAIN = 20;

final static int GAME_MENU_INGAME = 25;

final static int GAME_MENU_GAMEOVER = 30;

final static int GAME_EXIT = 35;

final static int GAME_PLAY = 40;
final static int GAME_PLAY_INIT = 41;
final static int GAME_PLAY_TICK = 42;

final static int GAME_MENU_RECUENTO = 50;

// ===============================================


// -----------------------------------------------
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int MUSIC_MENU = 0;
final static int MUSIC_INGAME = 1;
final static int MUSIC_LEVEL_COMPLETED = 2;
final static int MUSIC_PLAYER_OVER = 3;
final static int MUSIC_CHUPINAZO = 4;
final static int MUSIC_CLIN = 5;
// ===============================================


// Variables para la configuaracion del juego
boolean gameSound = true;
boolean gameVibra = true;
boolean gameLight = true;
byte gameWithHelp = 1;


// Nombre de los jugadores por defecto
String[] gamePlayerNames;


// Variables para la gestion de las fichas (publicidad)
int antiguedad;	// Usado para saber la antiguedad de las fichas descargadas
int publiPos;
String hiscore_scrollText = " ";
boolean gameMoreGamesVisited;	// Variable que nos indica si se ha visitado el menu "mas juegos"


// Variables internas
String[][] gameText;
Image loadingImg;
int gameAlphaExit;
int gameStatus = 0;


// Variables para almacenar el resultado de los hi-scores
int scoreFirst;
String[] scoreNick;
int[] scoreFace;
int [] scorePoints;


// Variables del juego
int levelSelected;				// Guardamos el nivel seleccionado para jugar
int currentLevel = 1;			// Nivel al que estamos jugando actualmente (0 a 9) 0=Tutorial, 1=Encierro 1, etc...
int levelPlayed;
int playerSelected;				// Jugador con el que estamos jugando actualmente (0 a 3)
byte[] ferminesItem = new byte[8];	// Anotamos las letras de FERMINES que hemos pillado en cada encierro
int hiscore;					// Puntuacion maxima conseguida
boolean toreroLocked = true;	// Personaje Torero bloquado?
int[] levelPoints = new int[9];	// Puntuacion obtenida en cada nivel (de 0 a 8) [0]=Encierro 1, [8]=level infinito
boolean nextLevelFlag;			// usado para ir al menu principal y saltar automaticamente a seleccionar nivel. (al pasar al siguiente nivel)
boolean playerSelectFlag;		// usado para preguntar si hay que seleccionar player o no. (al pasar al siguiente nivel)
int lastRankingPos = 1024;		// guardamos nuestra ultima posicion
int rankingArrow = -1;			// Flag para indicar la flecha a mostar (-1: ninguna, 0:Arriba, 1:Abajo)
boolean updateRankingArrow;		// Flag para refrescar la flecha que indica si hemos subido o bajado de posicion en el ranking
boolean tutorialViewed;			// Flag para saber si hemos pasado por el modo Tutorial
boolean protInmune;				// El protagonista es inmune


int loadingProgBarState;
int loadingProgBarTotal;

boolean muestraFelicidades;
boolean muestraDesbloqueo;

// -------------------
// game Create
// ===================

public void gameCreate()
{
	waitStart();


	loadingImg = loadImage("/vendor");
	loadingProgBarTotal = 5;
	loadingProgBarState = 0;
	forceRenderTask(RENDER_LOADING_PROGBAR);

// --------------------------------
// Cargamos archivo de textos generico
// ================================
	gameText = textosCreate( loadFile("/Textos.txt") );
	//#ifdef LG-U8210
	//#endif
	

/*	for (int i = 0; i < gameText.length; i++)
	{
	    com.mygdx.mongojocs.sanfermines2006.Debug.println("" + i + " :: "+gameText[i][0]);
	}*/

// Actualizamos el carrier ID desde el archivo de textos
	try {
		ID_CARRIER = Integer.parseInt(gameText[TEXT_CARRIER_ID][1]);
	} catch(Exception e) {}

// ================================

	loadingProgBarState++;
	forceRenderTask(RENDER_LOADING_PROGBAR);

// --------------------------------
// Inicializamos nombres de los jugadores por defecto
// ================================
	gamePlayerNames = new String[gameText[TEXT_PLAYER_NAMES].length];
	System.arraycopy(gameText[TEXT_PLAYER_NAMES], 0, gamePlayerNames, 0, gamePlayerNames.length);
// ================================


// --------------------------------
// Inicializamos nombres de los jugadores por defecto
// ================================
	scoreFirst = 1;
	scoreFace = new int[] {0,1,1,0,1,1,0,1,0,1,1,0,1,1,0,0,1,0,0,0};
	scoreNick = gameText[TEXT_SCORE_NAMES];
	scorePoints = new int[] {666,598,510,483,460,402,347,312,305,281,249,215,196,180,152,115,90,35,18,5};
// ================================


// --------------------------------
// Cargamos Preferencias
// ================================
	loadPrefs();
// ================================

	loadingProgBarState++;
	forceRenderTask(RENDER_LOADING_PROGBAR);

// --------------------------------
// Fijamos por defecto el nivel al que jugar
// ================================
	levelSelected = currentLevel;
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
			"/caratula.mid",		// 0 - Musica de la caratula
			"/ingame.mid",			// 1 - Musica durante el juego
			"/ganar.mid",			// 2 - Musica de nivel completado
			"/perder.mid",			// 3 - Sonido de "cogido por el toro"
			"/chupinazo.mid",		// 4 - Chupinazo
			"/clin.mid",			// 5 - Clin de SONIDO SI
			});

	//#endif


	//#ifdef SG-D410
//TODO:		durations = new long[] {64000, 64000, 7000, 2000, 2000, 1000};	// Se ha de poner a MANO los milis que dura cada midi.
	//#endif

//#endif
// ================================

	loadingProgBarState++;
	forceRenderTask(RENDER_LOADING_PROGBAR);

// --------------------------------
// Cargamos FONT CUSTOM
// ================================
	//#ifndef HIDE_CUSTOM_FONT
		fontCor = loadFile("/font.cor");
		fontSoftkeyCor = loadFile("/fontSoftkey.cor");
	
		//#ifdef J2ME
			fontImg = loadImage("/font");
			fontSoftkeyImg = loadImage("/fontSoftkey");
		//#elifdef DOJA
		//#endif
	//#endif
// ================================

	loadingProgBarState++;
	forceRenderTask(RENDER_LOADING_PROGBAR);



	menuCreate();


	// Cortar algunas strings conflictivas
	
	//fontInit(FONT_SMALL_WHITE);	
	gameText[TEXT_ENCIERRO_COMPLETADO] = printTextBreak(gameText[TEXT_ENCIERRO_COMPLETADO], canvasWidth);
	
		
	loadingProgBarState++;
	forceRenderTask(RENDER_LOADING_PROGBAR);

	loadingProgBarTotal=0;
	forceRenderTask(RENDER_LOADING_PROGBAR);


// Si desde que hicimos waitStart() no han pasado 2,5 seg, esperamos el tiempo que falte...
	waitStop(2500);


// Mostramos texto Publiser
	
//#ifndef HIDE_SPLASH_VENDOR
	
	fillCanvasRGB = 0;
	forceRenderTask(RENDER_FILL_CANVAS);
	forceRenderTask(RENDER_MICROJOCS_PUBLISHER);
	waitTime(2500);
	
//#endif
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
// Lanzamos secuencia inicial de logotipos
// ===============================
	case GAME_LOGOS:

	//#ifdef FULLSCREEN
		canvasHeight -= listenerHeight;		// Reservamos espacio para el area de las softkeys (listener 'simulado')
	//#endif

		gameStatus = GAME_MENU_START;
	break;
// ===============================



// -------------------------------
// Gestor de menus del source base
// ===============================
	case GAME_MENU_START:			// Inicializamos recursos para menus y preguntamos si queremos juego con sonido

		{
			biosPauseEnabled = true;

//		menuCreate();

			fillCanvasRGB = 0;
			forceRenderTask(RENDER_FILL_CANVAS);

			//#ifndef PLAYER_NONE
			popupInitAsk(null, gameText[TEXT_GAME_WITH_SOUND], SOFTKEY_NO, SOFTKEY_YES);
			biosWait();

			gameSound = popupResult;
			soundPlay(MUSIC_CLIN, 1);
			//#endif

			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
			gameStatus++;
		}
	break;


	case GAME_MENU_START + 1:


		forceRenderTask(RENDER_MENU_IMG);

	// --------------------------------
	// Creamos e inicializamos rmsEngine (Mini Disco Duro)
	// ================================
	//#ifdef RMS_ENGINE

		//#ifndef HIDE_CONNECTIVITY
			int rmsSize = rmsCreate(21 * 1024);
		//#else
		//#endif
	// ================================

	// Inicializamos engine para fichas de publicidad
		if (rmsSize > 500)
		{
			cacheCreate();
		}
	//#endif
	// ================================

		loadingImg = null;

		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:
		{
			menuInit(MENU_MAIN);
			if (soundOld != MUSIC_MENU) {
				soundPlay(MUSIC_MENU, 0);

				//#ifdef FIX_SOUNDSTOP_OTA
				//#endif
			}

			//#ifdef DEBUG_PC_USED_MEM
			System.gc();
			System.out.println("menu_Main: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
			//#ifdef DEBUG
			System.gc();
			Debug.println("menu_Main: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
			//#endif
			//#endif

			playerSelectFlag = true;

			gameStatus = GAME_PLAY;

			if (nextLevelFlag) {
				nextLevelFlag = false;
				playerSelectFlag = false;
				menuRelease();
				menuInit(MENU_STAGE_SELECT);
				if (!menuAcept) {
					playerSelectFlag = true;
					biosWait();
				}
			} else {
				biosWait();
			}

//#ifdef DEBUG
			System.out.println("opppps!!!!!! hemos salido del biosWait principal");
//#endif

			soundStop();

			// mostramos "loading" dentro de menu para dar paso al juego
			if (gameStatus == GAME_PLAY) {
				menuInit(MENU_LOADING);
				forceRender();
				menuRelease(true);
			}

			menuDestroy();
		}
	break;


	case GAME_MENU_GAMEOVER:
/*
		canvasFillTask(0x000000);
		canvasTextTask(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, PRINT_VCENTER|PRINT_HCENTER | PRINT_OUTLINE);
		forceRender();

		waitTime(3000);
*/
		menuCreate(false);

		menuInit(MENU_LOADING);
		forceRender();
		menuRelease(true);

		menuCreate();


		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_RECUENTO:

		{
			menuCreate(false);

			menuInit(MENU_LOADING);
			forceRender();
			menuRelease(true);

			savePrefs();

			menuCreate();

			if (levelPlayed > 0 && levelPlayed < 9) {
				soundPlay(MUSIC_MENU, 0);

				menuInit(MENU_RECUENTO);
				biosWait();
			}

			// Si hemos completado todos los encierros, mostramos "felicidades" e invitamos a jugar en el modo "sobrevive"
			// Si hemos recogido todas las letras "FERMINES", mostramos popup indicando que hemos desbloqueado
			if (muestraFelicidades || muestraDesbloqueo) {
				int tmp = 0;
				if (muestraFelicidades) {
					tmp = TEXT_SOBREVIBE_DESBLOQUEADO;
				}
				if (muestraDesbloqueo) {
					tmp = TEXT_TORERO_DESBLOQUEADO;
				}
				if (muestraFelicidades && muestraDesbloqueo) {
					tmp = TEXT_SOBREVIBE_TORERO_DESBLOQUEADO;
				}

				popupInitInfo(null, gameText[tmp]);
				biosWait();

				muestraDesbloqueo = false;
			}


			nextLevelFlag = true;
			gameStatus = GAME_MENU_MAIN;
		}
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
/*
	// mostramos "loading" dentro de menu para dar paso al juego
		menuInit(MENU_LOADING);
		forceRender();
		menuRelease(true);
*/

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("playCreate(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("playCreate(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif
 
		playCreate();
		gameStatus++;

	case GAME_PLAY_INIT:

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("playInit(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("playInit(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

	// Cargamos e inicializamos todos los recursos para empezar una partida, ya sea la primera o tras haber perdido una partida anterior
		playInit();

	// Este Flag, nos indica el motivo por el cual debemos terminar el juego (1:Siguiente nivel, 2:muerte, 3:game over forzado desde menu)
		playExit = 0;

		gameStatus++;

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("playTick(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("playTick(): "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

	listenerInit(SOFTKEY_PAUSE, (levelSelected == 0? SOFTKEY_SKIP:SOFTKEY_NONE));

	break;

	case GAME_PLAY_TICK:


		//#ifdef CHEATS
		cheats();
		//#endif

		// Si pulsamos la softKey de ir a menu, pasamos al ESTADO de menu durante juego.
		if (keyMenu == -1 && lastKeyMenu == 0)
		{
			playLeftSoftkeyOld = listenerIdLeft;
			playRightSoftkeyOld = listenerIdRight;

			menuCreate(false);

			menuInit(MENU_INGAME);
			biosWait();

			menuDestroy();
			break;
		}


		// Si estamos en el Juego Tutorial, la softkey Izquierda es para saltar el tutorial
		if (levelSelected == 0 && keyMenu == 1 && lastKeyMenu == 0)
		{
			playExit = 1;
		}


		// Procesamos un TICK del juego, si true: debemos parar el juego...
		if (playExit == 0) {if (!playTick()) {break;}}

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		soundStop();

		// Desalojamos todos los recursos cargados en 'playInit()'
		playRelease();

		// Colocamos el ESTADO en 'GAME_PLAY_INIT'
		gameStatus = GAME_PLAY_INIT;

		switch (playExit)
		{
			case 1:	// Nivel Completado, pasamos al siguiente nivel

				levelPlayed = levelSelected;

				// Actualizamos rosas pilladas en este nivel
				if (levelSelected > 0) {if (levelPoints[levelSelected - 1] < roseCount) {levelPoints[levelSelected - 1] = roseCount;}}

				// Actualizamos hiscores sumando las rosas pilladas en todos los niveles
				hiscore = 0; for (int i=0 ; i<levelPoints.length ; i++) {hiscore += levelPoints[i];}

				updateRankingArrow = true;

				// Destruimos todos los recursos cargados en 'playCreate()'
				playDestroy();

				// Si tenemos TODAS las letras, desbloqueamos torero
				for (int i=0 ; i<8 ; i++)
				{
					if (ferminesItem[i] == 0) {break;}
					//#ifndef BUILD_ONE_PLAYER
					if (i==7) {muestraDesbloqueo = toreroLocked; toreroLocked = false;}
					//#endif
				}


				// Si acabado de completar el nivel 8, activamos mostar popup de "felicidades"
				muestraFelicidades = (currentLevel == levelSelected && currentLevel == 8);


				// Si hemos jugado al ultimo nivel disponible, liberamos el siguiente
				if (levelSelected == currentLevel)
				{
					if (currentLevel < 9) {currentLevel++; levelSelected++;}
				}
				else
				if (levelSelected == 0)
				{
					levelSelected++;
				}

				gameStatus = GAME_MENU_RECUENTO;
				break;

			//case 2:	// Una vida menos y regresamos al inicio del nivel
			//break;

			case 3:	// Producir Game Over

				// Destruimos todos los recursos cargados en 'playCreate()'
				playDestroy();

				// Saltamos al ESTADO de 'GAME OVER'
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
/*
	case GAME_MENU_START + 1:

		canvasImg = menuImg;
	break;
*/

	case GAME_PLAY_TICK:		// GAME_PLAY_TICK => Estado del juego cuando esta en funcionamiento.

		if (playExit == 0) playRefresh();
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
		fillDraw(FORM_RGB_BACKGROUND, 0, 0, canvasWidth, canvasHeight);
		printSetArea(0, 0, canvasWidth, canvasHeight);
		//fontInit(FONT_SMALL_WHITE);
		printDraw(gameText[TEXT_GAME_IN_PAUSE][0], 0, 0, fontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		listenerShow = true;
		listenerDraw();
		return;
	}


// Gestiones internes de la BIOS
	if (canvasImg!=null) { imageDraw(canvasImg); canvasImg=null; System.gc(); }



// Hack para refrescar el campo de juego mientras estemos en el menu 'INGAME'
	if (biosStatus == BIOS_MENU && gameStatus == GAME_PLAY_TICK && formShow) {playShow = true;}


// Renderizamos el campo de juego
	if (playShow) { playShow=false; playDraw(); }


// Renderizamos el fadeOut al quitar del juego
	if (gameStatus == GAME_EXIT+1) {alphaFillDraw(0x44000000, 0,0, canvasWidth, canvasHeight);}


// Renderizamos gestor de menus
	menuDraw();


// Renderizamos gestor de popups
	popupDraw();


// Renderizamos textos de las softkeys
	listenerDraw();
}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// forceRenderTask - Engine
// ===================
// *******************

static final int RENDER_FILL_CANVAS = 0;
static final int RENDER_LOADING = 1;
static final int RMS_FORMAT_SLIDER = 2;
static final int RENDER_MICROJOCS_PUBLISHER = 3;
static final int RENDER_LOADING_PROGBAR = 4;
static final int RENDER_MENU_IMG = 5;

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


	case RENDER_MENU_IMG:

		imageDraw(menuImg, 0, 0);
	break;

/*
	case RENDER_LOADING:

		printInit_Font(biosGetFont());
		printSetArea(0, canvasHeight/2, canvasWidth, canvasHeight/2);
		printDraw(gameText[TEXT_LOADING], 0, 0, 0xffffff, PRINT_HCENTER|PRINT_VCENTER);
	break;
*/

//#ifdef RMS_ENGINE
	case RMS_FORMAT_SLIDER:
	{
		popupShow = true;
		popupDraw();

		int width = popupWidth - (popupWidth/4);

		int x = popupX + (popupWidth/8);
		int y = popupY + (popupHeight - (popupHeight/4));

		int penRGB = 0xFFFFFF;
		int backRGB = 0x82827C;

		int size = regla3(maxSizePerCent, storePerCent, width);

		fillDraw(backRGB, x, y, width, FORM_SEPARATOR);
		fillDraw(penRGB, x, y, size, FORM_SEPARATOR);
	}
	break;
//#endif


	case RENDER_MICROJOCS_PUBLISHER:

		printInit_Font(biosGetFont());
		int alto2 = printGetHeight() * 4;
		printSetArea(0, (canvasHeight - alto2)/2, canvasWidth, alto2);
		printDraw(gameText[TEXT_DISCLAIMER][0], 0, 0, 0xc0c0c0, PRINT_HCENTER|PRINT_TOP|PRINT_OUTLINE);
		printDraw(gameText[TEXT_DISCLAIMER][1], 0, 0, 0xc0c0c0, PRINT_HCENTER|PRINT_BOTTOM|PRINT_BREAK);
	break;


	case RENDER_LOADING_PROGBAR:
	
		fillDraw(0x000000, 0, 0, canvasWidth, canvasHeight);

		int logoHeight = 0;
		if (loadingImg != null) {imageDraw(loadingImg); logoHeight += loadingImg.getHeight();}

		if (loadingProgBarTotal != 0)
		{

		//#ifdef S80
		//#elifdef S40
		//#else
			int alto = 4, width = 106;
		//#endif

			int penRGB = 0xFC741C;
			int backRGB = 0x82827C;
			int x = (canvasWidth - width) / 2;
			int y = (canvasHeight/4)*3;
		
			int size = regla3(loadingProgBarState, loadingProgBarTotal, width);

			fillDraw(backRGB, x, y, width, alto);
			fillDraw(penRGB, x, y, size, alto);

	    //#ifdef DOJA
	    //#endif
		}
	break;
	}
}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// menu - Engine
// ===================
// *******************

// -----------------------------------------------
// Variables staticas para identificar los estados de los menus...
// ===============================================
static final int MENU_MAIN			= 0;
static final int MENU_HELP			= 1;
static final int MENU_OPTIONS		= 2;
static final int MENU_INGAME		= 3;
static final int MENU_SHOW_TEXT		= 4;
static final int MENU_EDIT_NAMES	= 5;
static final int MENU_INPUT_NAME	= 6;
static final int MENU_MORE_GAMES	= 8;
final static int MENU_SCORES		= 9;
final static int MENU_LOADING		= 10;
static final int MENU_EMPTY			= 11;
static final int MENU_WAIT			= 12;
static final int MENU_INPUT_NAME_SCORE	= 13;
static final int MENU_STAGE_SELECT	= 14;
static final int MENU_PLAYER_SELECT	= 15;
static final int MENU_RECUENTO		= 16;


//#ifdef DEBUG
final static int MENU_DEBUG				= 5000;
static final int MENU_DEBUG_SHOW_CACHE	= 5001;
//#endif
// ===============================================


// -----------------------------------------------
// Variables staticas para identificar las acciones de los menus...
// ===============================================
static final int MENU_ACTION_NONE =			-5;
static final int MENU_ACTION_BACK =			-10;
static final int MENU_ACTION_BACK_HELP =	-11;
static final int MENU_ACTION_NEW_MENU =		1;
static final int MENU_ACTION_SHOW_HELP =	2;
static final int MENU_ACTION_SHOW_ABOUT =	3;
static final int MENU_ACTION_EXIT_GAME =	4;
static final int MENU_ACTION_RESTART =		5;
static final int MENU_ACTION_SOUND_CHG =	6;
static final int MENU_ACTION_VIBRA_CHG =	7;
static final int MENU_ACTION_TUTORIAL_CHG =	8;
static final int MENU_ACTION_BACK_TO_GAME =	9;
static final int MENU_ACTION_MORE_GAMES =	10;
static final int MENU_ACTION_HI_SCORES =	11;
static final int MENU_ACTION_MASJUEGOS =	12;
static final int MENU_ACTION_MENU_EXIT =	13;
static final int MENU_ACTION_MENU_EXIT_OK =	14;
static final int MENU_ACTION_ASK_NEW_GAME =	15;
static final int MENU_ACTION_MORE_GAMES_POPUP =	16;

//#ifdef DEBUG
static final int MENU_ACTION_DEBUG_SHOW_CACHE = 50002;
static final int MENU_ACTION_DEBUG_RESET_GAME = 50004;
static final int MENU_ACTION_DEBUG_LEVEL_FREE = 50005;
static final int MENU_ACTION_DEBUG_INMUNE_CHG = 50006;
static final int MENU_ACTION_DEBUG_RESET_HASH = 50007;
static final int MENU_ACTION_DEBUG_TORERO_FREE = 50008;
static final int MENU_ACTION_DEBUG_SUMA_5_ROSAS = 50009;
static final int MENU_ACTION_DEBUG_SUMA_25_ROSAS = 50010;
//#endif
// ===============================================



// variables para el control de la pila en la gestion de diferentes estados de la BIOS
	int biosStackIndex = 0;
	int[] biosStack = new int[8];


// variables para el control de la pila en la gestion de la jerarquia de menus
	int menuStackIndex = 0;
	int[][] menuStack = new int[16][2];


// Variables usadas en el menu para editar nombres
	int menuEditPlayerSelected;		// guardamos el jugador seleccionado para cambiar su nombre


// Variables usadas por 'MENU_SHOW_TEXT' como parametros para saber que textos ha de mostrar
	String[] menuShowTextTitleStr;
	String[] menuShowTextBodyStr;


// Variables para tener el archivo de textos de las ayudas de forma externa a los textos basicos
	String[][] gameTextHelp;


// Variables internas
	int menuType;
	int menuActionAsk;
	Image menuImg;
	Image menuLogoImg;
//	Image popupImg;
	boolean menuFadeColor;
	boolean menuAcept;

// Variables del juego

//#ifdef S40
//#elifdef S60
	static final int playerSelectCell = 75;
//#elifdef S80
//#endif
	
	//#ifdef J2ME 
	Image playerSelectImg;
	//#elifdef DOJA
	//#endif


// -------------------
// menu Create
// ===================

public void menuCreate()
{
	menuCreate(true);
}

public void menuCreate(boolean fullRead)
{
	if (fullRead)
	{
		if (menuImg == null) {
			
			//#ifdef HACK_XX65
			//#else
			
			menuImg = loadImage("/caratula");
			
			//#endif
		}

	//#ifndef BUILD_ONE_PLAYER
		//#ifdef J2ME
			if (playerSelectImg == null) {playerSelectImg = loadImage("/caras");}
		//#elifdef DOJA
		//#endif
	//#endif
	}

	formCreate();
	popupCreate();
}


// -------------------
// menu Destroy
// ===================

public void menuDestroy()
{
	System.gc();

//#ifndef FIX_GC_GRAPHICS
//#endif

	formDestroy();
	popupDestroy();

	System.gc();
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
//#if DEBUG && DEBUG_MENU
	Debug.println("menuInit("+type+")");
//#endif

	menuAcept = false;

	menuType = type;

	biosPush();
	biosStatus = BIOS_MENU;

	formClear();
	formListClear();

	switch (type)
	{
	case MENU_MAIN:

		formLogoInit(FORM_LOGO_HEIGHT);

		formListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_NEW_MENU | MENU_STAGE_SELECT<<16);

		formListAdd(0, gameText[TEXT_HI_SCORES], MENU_ACTION_NEW_MENU | MENU_SCORES<<16);

		formListAdd(0, gameText[TEXT_OPTIONS], MENU_ACTION_NEW_MENU | MENU_OPTIONS<<16);

 		formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_NEW_MENU | MENU_HELP<<16);

	//#ifndef HIDE_MORE_GAMES
	// Si hay fichas visibles, muestra la opcion de "MAS JUEGOS"
		if (cacheFichasVisibles > 0)
		{
			formListAdd(0, gameText[TEXT_MORE_GAMES], MENU_ACTION_MORE_GAMES);
		}
	//#else
	//#endif

		formListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

	//#ifdef DEBUG
		//formListAdd(0, "DEBUG", MENU_ACTION_NEW_MENU | MENU_DEBUG<<16);
	//#endif

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_STAGE_SELECT:
		{
			if (!tutorialViewed) {
				menuRelease(true);
				levelSelected = 0;
				//#ifndef BUILD_ONE_PLAYER
				menuInit(MENU_PLAYER_SELECT);
				//#else
				//#endif
				break;
			}

			formLogoInit(FORM_LOGO_HEIGHT);

			formTitleInit(gameText[TEXT_SELECT_LEVEL][0]);

			formListAdd(0, new String[]{gameText[TEXT_TUTORIAL][0], null}, 0, -1);
			for (int i = 0; i < 8; i++) {
				formListAdd(0, new String[]{gameText[TEXT_ENCIERRO][0] + " " + (i + 1), "" + levelPoints[i]}, 0, (ferminesItem[i] != 0 ? i : -1));
			}
			formListAdd(0, new String[]{gameText[TEXT_SOBREVIVE][0], "" + levelPoints[8]}, 0, -1);

			formBottomText = gameText[TEXT_RECORD_ACTUAL];

			formResultStageSelect = true;
			formResultMode = FORM_LISTADO_LEVEL;

			pos = levelSelected;
			formInit(FORM_LISTADO, pos, MENU_ACTION_MENU_EXIT, MENU_ACTION_MENU_EXIT_OK);

			// Centramos la opcion seleccionada en el area de vision
			formListIni = formListPos - (formListView / 2);
			if (formListIni < 0) {
				formListIni = 0;
			} else if (formListIni > formListStr.length - formListView) {
				formListIni = formListStr.length - formListView;
			}

			listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);

			biosWait();

			if (menuAcept) {
				levelSelected = formListPos;

				//#ifndef BUILD_ONE_PLAYER
				if (playerSelectFlag) {
					menuInit(MENU_PLAYER_SELECT);
				} else {
					biosExit = true;
				}
				//#else
				//#endif

			} else {
				menuInitBack();
			}
		}
	break;


//#ifndef BUILD_ONE_PLAYER
	case MENU_PLAYER_SELECT:

	 {
			formLogoInit(FORM_LOGO_HEIGHT);

			formTitleInit(gameText[TEXT_PLAYER_SELECT][0]);

			for (int i = 0; i < 2; i++) {
				formListAdd(0, gameText[TEXT_PLAYERS_NAMES][i], MENU_ACTION_MENU_EXIT_OK);
			}

			formInit(FORM_PLAYER_SELECT, pos, MENU_ACTION_MENU_EXIT, MENU_ACTION_MENU_EXIT_OK);

			listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);

			biosWait();

			if (menuAcept) {
				biosExit = true;
			} else {
				menuInitBack();
			}
		}
	break;
//#endif


	case MENU_OPTIONS:

		formLogoInit(FORM_LOGO_HEIGHT);

//		formTitleInit(gameText[TEXT_OPTIONS][0]);

	//#ifndef PLAYER_NONE
		formListAdd(0|SOFTKEY_CAMBIAR<<8, gameText[TEXT_OPT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		formListAdd(0|SOFTKEY_CAMBIAR<<8, gameText[TEXT_OPT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif

		menuEditPlayerSelected = 1;
		formListAdd(0, gameText[TEXT_EDIT_NAMES][0], MENU_ACTION_NEW_MENU | MENU_INPUT_NAME<<16);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_HELP:

		if (gameStatus != GAME_PLAY_TICK)
		{
			formLogoInit(FORM_LOGO_HEIGHT);
		}

//		formTitleInit(gameText[TEXT_HELP][0]);

		formListAdd(0|SOFTKEY_VER<<8, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);

		formListAdd(0|SOFTKEY_VER<<8, gameText[TEXT_HELP_COMO_JUGAR], MENU_ACTION_SHOW_HELP);
		formListAdd(0|SOFTKEY_VER<<8, gameText[TEXT_HELP_CONTROLES], MENU_ACTION_SHOW_HELP);
	//#ifndef HIDE_CONNECTIVITY
		formListAdd(0|SOFTKEY_VER<<8, gameText[TEXT_CONECTIVIDAD], MENU_ACTION_SHOW_HELP);
	//#endif

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK_HELP, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_SHOW_TEXT:

	// Mostramos "espere..."
		formLogoInit(FORM_LOGO_HEIGHT);

		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		forceRender();

		formListClear();

	// Procesamos texto a mostrar
		formLogoInit(FORM_LOGO_HEIGHT);

		formTitleInit(menuShowTextTitleStr[0]);

		formListAdd(0, menuShowTextBodyStr);

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;


	case MENU_INPUT_NAME:
	case MENU_INPUT_NAME_SCORE:
/*
	// Mostramos "espere..."
		formLogoInit(FORM_LOGO_HEIGHT);

		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		forceRender();

		formListClear();
*/

		/*formLogoInit(FORM_LOGO_HEIGHT);

		formTitleInit(gameText[TEXT_ENTER_NAME][0]);

		formInit(FORM_EDIT_NAME, pos, MENU_ACTION_MENU_EXIT, MENU_ACTION_MENU_EXIT_OK);

		listenerInit(SOFTKEY_BACK, SOFTKEY_INTRODUCIR);*/ //MONGOFIX

		if (type == MENU_INPUT_NAME)
		{
			//biosWait(); MONGOFIX
			menuAcept = false;

		// Tras haber editado nuestro nombre, si hemos pulsado aceptar lo actualizamos...
			if (menuAcept)
			{
				formEditStr = formEditStr.trim();
		
				if (formEditStr.length() >0)
				{
					gamePlayerNames[menuEditPlayerSelected] = formEditStr;
				}
			}
	
			menuInitBack();
		}
	break;


//#ifndef HIDE_MORE_GAMES
	case MENU_MORE_GAMES:

	// Mostramos "espere..."
		formLogoInit(FORM_LOGO_HEIGHT);

		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		forceRender();

		formListClear();

		gameMoreGamesVisited = true;
		savePrefs();

	// Cargamos recursos para la publicidad
		Image img = rmsLoadImage("/l"+cacheLogoId[cacheP[publiPos]]);
		if (img == null) {img = loadImage("/l"+cacheLogoId[cacheP[publiPos]]);}
		formLogoInit(img);

		img = null;

		String fichaName = "/i"+cacheImageId[cacheP[publiPos]];

		//#ifdef HACK_XX65
		//#endif
		
		img = rmsLoadImage(fichaName);
		if (img == null) {img = loadImage(fichaName);}

		formPubliInit(img, cacheScrTxt[cacheP[publiPos]]);
		gameText[TEXT_SOFTKEYS][SOFTKEY_MASJUEGOS] = cacheSoftkey[cacheP[publiPos]];

		formInit(FORM_MORE_GAMES, 0, MENU_ACTION_BACK, MENU_ACTION_MASJUEGOS);

		listenerInit(SOFTKEY_BACK, SOFTKEY_MASJUEGOS);
	break;
//#endif

	case MENU_SCORES:

		formLogoInit(FORM_LOGO_HEIGHT);

		if (hiscore_scrollText != null && !hiscore_scrollText.equals(" "))
		{
			formScrollText = hiscore_scrollText;
		} else {
			formScrollText = null;
		}



		boolean added = false;
		int posRank = scoreFirst;
		int myRank = 0;
		for (int i=0 ; i<scoreNick.length ; i++)
		{
			if (!added && hiscore >= scorePoints[i])
			{
				if (i == 0) {if (--posRank <= 0) {posRank = 1;}}
				added = true;
				myRank = posRank;
				formListAdd(0, new String[] {""+(posRank++), gamePlayerNames[1], ""+hiscore}, 0, playerSelected);
				pos = i;
			}

			formListAdd(0, new String[] {""+(posRank++), scoreNick[i], ""+scorePoints[i]}, 0, scoreFace[i]);
		}

		if (!added)
		{
			myRank = posRank;
			formListAdd(0, new String[] {""+(posRank++), gamePlayerNames[1], ""+hiscore}, 0, playerSelected);
			pos = scoreNick.length;
		}

		formTitleInit(gameText[TEXT_TU_PUNTUACION][0]);


	// Si hemos realizado una partida o descargado scores, actualizamos la flecha que indica si hemos subido o bajado en el ranking
		if (updateRankingArrow)
		{
			updateRankingArrow = false;

			rankingArrow = -1;
			if (lastRankingPos > myRank)
			{
				rankingArrow = 0;
			}
			else
			if (lastRankingPos < myRank)
			{
				rankingArrow = 1;
			}
		}
		lastRankingPos = myRank;


		formScrollPage = true;
		formResultMode = FORM_LISTADO_RANKING;

	//#ifndef HIDE_CONNECTIVITY
		formInit(FORM_LISTADO, pos, MENU_ACTION_BACK, MENU_ACTION_HI_SCORES);
		listenerInit(SOFTKEY_BACK, SOFTKEY_ACTUALIZAR);
	//#else
	//#endif

	// Centramos la opcion seleccionada en el area de vision
		formListIni = formListPos - (formListView/2);
		if (formListIni < 0) {formListIni = 0;}
		else
		if (formListIni > formListStr.length - formListView) {formListIni = formListStr.length - formListView;}

	return;


	case MENU_EMPTY:

		formLogoInit(FORM_LOGO_HEIGHT);

		formInit(FORM_NONE, 0, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	return;


	case MENU_LOADING:

//		formLogoInit(menuLogoImg);

		formListAdd(0, gameText[TEXT_LOADING]);

		formInit(FORM_LIST, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		forceRender();
	break;


	case MENU_WAIT:

		formLogoInit(FORM_LOGO_HEIGHT);

		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		forceRender();
	break;


	case MENU_INGAME:

//		formLogoInit(menuLogoImg);

		formListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_BACK_TO_GAME);

	//#ifndef HIDE_SOUND_INGAME
		//#ifndef PLAYER_NONE
			formListAdd(0|SOFTKEY_CAMBIAR<<8, gameText[TEXT_OPT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
		//#endif
	//#endif

	//#ifdef VIBRATION
		formListAdd(0|SOFTKEY_CAMBIAR<<8, gameText[TEXT_OPT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_NEW_MENU | MENU_HELP<<16);
		formListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		formListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

	//#ifdef DEBUG
		//formListAdd(0, "DEBUG", MENU_ACTION_NEW_MENU | MENU_DEBUG<<16);
	//#endif

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_RECUENTO:

		formLogoInit(FORM_LOGO_HEIGHT);

		formTitleInit(gameText[TEXT_RECUENTO][0]);
		formInit(FORM_RESULT, pos, MENU_ACTION_NEW_MENU|MENU_SCORES<<16, MENU_ACTION_MENU_EXIT_OK);
		listenerInit(SOFTKEY_SCORES, SOFTKEY_ACEPT);
	break;








//#ifdef DEBUG
	case MENU_DEBUG:


		formListAdd(0, new String[] {"Inmune: NO", "Inmune: SI"}, MENU_ACTION_DEBUG_INMUNE_CHG, protInmune?1:0);

		formListAdd(0, "Reset game", MENU_ACTION_DEBUG_RESET_GAME);

		formListAdd(0, "Reset HASH", MENU_ACTION_DEBUG_RESET_HASH);

		formListAdd(0, "Level Free", MENU_ACTION_DEBUG_LEVEL_FREE);

		formListAdd(0, "Torero Free", MENU_ACTION_DEBUG_TORERO_FREE);

		formListAdd(0, "Suma 5 Rosas", MENU_ACTION_DEBUG_SUMA_5_ROSAS);

		formListAdd(0, "Suma 25 Rosas", MENU_ACTION_DEBUG_SUMA_25_ROSAS);

		formListAdd(0, "Show Cache", MENU_ACTION_DEBUG_SHOW_CACHE);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);
		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;


	case MENU_DEBUG_SHOW_CACHE:


		if (cacheC > 0)
		{
			for (int i=0 ; i< cacheC ; i++)
			{
				int adr = cacheP[i];
				formListAdd(0, "id:"+cacheFichaId[adr]+" Pri:"+((cachePrio[adr]>>16)&0xffff)+" old:"+(cachePrio[adr]&0xffff), MENU_ACTION_NONE);
			}
		} else {
			formListAdd(0, "No hay fichas", MENU_ACTION_NONE);
		}

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);
		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
//#endif
	}

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

	listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

	formRelease();

	biosPop();
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

	menuInit(menuStack[menuStackIndex][0], menuStack[menuStackIndex][1]);
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{
	int newMenuId = cmd>>16; if (cmd >= 0 ) {cmd &= 0xffff;}

	switch (cmd)
	{
	case MENU_ACTION_BACK_HELP:	// Ir "Atras" desde el menu de Ayuda

		gameTextHelp = null;

	case MENU_ACTION_BACK:	// Ir "Atras" entre los menus

		menuRelease();
		menuInitBack();
	break;


	case MENU_ACTION_NEW_MENU:	// lanzar nuevo menu, segun parametro en menuCMD

		menuRelease();
		menuInit(newMenuId);
	break;


	case MENU_ACTION_MENU_EXIT_OK:	// Salir de los menus, OK

		menuAcept = true;

	case MENU_ACTION_MENU_EXIT:	// Salir de los menus

		menuRelease();

		biosExit = true;		// Activamos salir del "biosWait()" actual
	break;


	case MENU_ACTION_BACK_TO_GAME:	// Ir "Atras" al campo de juego

//		soundStop();
		menuRelease(true);
		listenerInit(playLeftSoftkeyOld, playRightSoftkeyOld);
		biosExit = true;		// Activamos salir del "biosWait()" actual
	break;


	case MENU_ACTION_MORE_GAMES:	// Mostramos Menu "Mas juegos"
	
		menuRelease();

		if (menuType != MENU_MORE_GAMES)
		{
			publiPos = 0;
		} else {
			if ( (menuStackIndex--) < 0) {menuStackIndex += menuStack.length;}
		}

		menuInit(MENU_MORE_GAMES);
	break;	    


//#ifdef BUILD_MORE_GAMES_POPUP
//#endif


	case MENU_ACTION_HI_SCORES:

//#ifndef HIDE_CONNECTIVITY
	// Preguntamos si se quiere conectar a red para tener los resultados de los scores
		//popupInitAsk(gameText[TEXT_CONFIRMATION][0], gameText[TEXT_DESCARGAR_SCORES], SOFTKEY_NO, SOFTKEY_YES);
		//biosWait();
		popupResult = false; //MONGOFIX
	// Si cancelamos, volvemos a la pantalla anterior
		if (!popupResult)
		{
			break;
		}

	// Eliminamos el menu que nos ha enviado aqui
		menuRelease();


	// Si no hemos puesto nunca nuestro nombre, nos lo pregunta...
		if (!inputName())
		{
			if (++menuStackIndex < 0) {menuStackIndex -= menuStack.length;}
			menuInitBack();
			break;
		}


	// Mostramos un formulacion vacio, en plan espera...
		menuInit(MENU_EMPTY);
		forceRender();


	// Deshabilitamos que se salte al estado de PAUSA
		biosPauseEnabled = false;


	// Lanzamos conexion para descargar scores
		int result = 0;
		do {
			connectRequest(DOWNLOAD_HISCORE);
	
			result = connectWait();
	
		} while (result == -2);


	// Si acabamos de recibir HashUserId, Lanzamos conexion para confirmarlo
		if (connectConfirmHash)
		{
			result = 0;
			do {
				connectRequest(RETURN_HASH_OK);

				result = connectWait();

			} while (result == -2);

			connectConfirmHash = false;
		}


	// Habilitamos que se salte al estado de PAUSA
		biosPauseEnabled = true;


	// Eliminamos MENU_EMPTY, SIN guardarlo en el Stack
		menuRelease();

		updateRankingArrow = true;

	// Regresamos al menu que lanzo la peticion de actualizar scores
		menuInitBack();
//#else
//#endif
	break;


	case MENU_ACTION_SHOW_HELP:		// Mostramos Instrucciones...

		int pos = formListPos - 1;	// Hay que restar "1" porque la primera opcion es "Acerca de..."

		menuRelease();

		menuInit(MENU_WAIT);
		forceRender();


		if (gameTextHelp == null)
		{
			gameTextHelp = textosCreate(loadFile("/help.txt"));
		}

		switch (pos)
		{
		case 0:
			menuShowTextTitleStr = gameText[TEXT_HELP_COMO_JUGAR];
			menuShowTextBodyStr	= gameTextHelp[0];
		break;

		case 1:
			menuShowTextTitleStr = gameText[TEXT_HELP_CONTROLES];
			menuShowTextBodyStr	= gameTextHelp[1];
		break;

		case 2:
			menuShowTextTitleStr = gameText[TEXT_CONECTIVIDAD];
			menuShowTextBodyStr	= gameTextHelp[2];
		break;
		}

		menuRelease(true);

		menuInit(MENU_SHOW_TEXT);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// Mostramos About...

		menuRelease();

		menuShowTextTitleStr = gameText[TEXT_ABOUT];
		menuShowTextBodyStr	= gameText[TEXT_ABOUT_SCROLL];

		menuInit(MENU_SHOW_TEXT);
	break;


	case MENU_ACTION_RESTART:	// Provocamos GAME OVER desde menu (preguntamos)

//		popupSetArea(formBodyX + (formBodyWidth/6), formBodyY + (formBodyHeight/6), formBodyWidth - (formBodyWidth/3), formBodyHeight - (formBodyHeight/3));
		popupInitAsk(gameText[TEXT_CONFIRMATION][0], gameText[TEXT_ARE_YOU_SURE], SOFTKEY_NO, SOFTKEY_YES);

		biosWait();

		forceRender();

		if (popupResult)
		{
			menuRelease();

			biosExit = true;		// Activamos salir del "biosWait()" actual

			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
			playExit = 3;
		}
	break;


	case MENU_ACTION_EXIT_GAME:	// Exit com.mygdx.mongojocs.sanfermines2006.Game???

//		popupSetArea(formBodyX + (formBodyWidth/6), formBodyY + (formBodyHeight/6), formBodyWidth - (formBodyWidth/3), formBodyHeight - (formBodyHeight/3));
		//popupInitAsk(gameText[TEXT_CONFIRMATION][0], gameText[TEXT_ARE_YOU_SURE], SOFTKEY_NO, SOFTKEY_YES);

		/*biosWait();

		forceRender();

		if (popupResult)*/ //MONGOFIX
		{
			menuRelease();

			biosExit = true;

			gameStatus = GAME_EXIT;
		}
	break;


	case MENU_ACTION_SOUND_CHG:

		gameSound = formListOpt() != 0;
		if (!gameSound) {soundStop();}
		else
		{
		    if (menuType == MENU_OPTIONS) {soundPlay(MUSIC_MENU, 0);}
// -----------
// Si el juego tiene musica durante el juego:
		else
		    if (menuType == MENU_INGAME && soundLoop == 0) {soundPlay(MUSIC_INGAME,0);}
		}
// ===========
	break;

	
	case MENU_ACTION_VIBRA_CHG:

		gameVibra = formListOpt() != 0;
		if (gameVibra) {vibraInit(300);}
	break;


	case MENU_ACTION_TUTORIAL_CHG:

		gameWithHelp = (byte)formListOpt();
	break;


//#ifndef HIDE_MORE_GAMES
	case MENU_ACTION_MASJUEGOS:

	//#if !DISABLE_BROWSER && FEATURE_LAUNCH_BROWSER
		soundStop();

		/* MONGOFIX
		try {
			ga.platformRequest(cacheURL[cacheP[publiPos]]);
		} catch (Exception e)
		{
		//#ifdef DEBUG
			Debug.println("platformRequest Error:"+e.toString());
		//#endif

			soundPlay(MUSIC_MENU, 0);

			popupInitInfo(null, new String[] {cacheAltTxt[cacheP[publiPos]]});
			biosWait();
			break;
		}*/

		//#ifndef FIX_LAUNCH_BROWSER_NO_EXIT
			biosExit = true;
			gameExit = true;
		//#else
		/*	// Preparamos info para tocar musica al salir de "Juego en Pausa"
			gameSoundOld = MUSIC_MENU;
			gameSoundLoop = 0;
			notifyHecho = true;*/
		//#endif
	//#else
	//	popupInitInfo(null, new String[] {cacheAltTxt[cacheP[publiPos]]});
//		biosWait();
	//#endif
	break;
//#endif












// -------------------------------------------------------
// - * - * - * - * -    D  E  B  U  G    - * - * - * - * -
//#ifdef DEBUG

//#ifndef HIDE_CONNECTIVITY
	case MENU_ACTION_DEBUG_SHOW_CACHE:

		Debug.println("cacheFichasVisibles:"+cacheFichasVisibles);

		for (int i=0 ; i< cacheC ; i++)
		{
			int adr = cacheP[i];
			Debug.println("------------------------");
			Debug.println(
			"FichaId: "+cacheFichaId[adr]+
			" ; ImageId: "+cacheImageId[adr]+
			" ; LogoId: "+cacheLogoId[adr]+
			" ; Prio: "+((cachePrio[adr]>>16)&0xffff)+
			" ; Antiguedad: "+(cachePrio[adr]&0xffff)+
			" ; URL: "+cacheURL[adr]+
			" ; ScrTxt: "+cacheScrTxt[adr]+
			" ; AltTxt: "+cacheAltTxt[adr]+
			" ; Softkey: "+cacheSoftkey[adr]
			);
		}
		Debug.println("------------------------");

		menuRelease();

		menuInit(MENU_DEBUG_SHOW_CACHE);
	break;
//#endif


	case MENU_ACTION_DEBUG_RESET_GAME:

	//#ifndef HIDE_CONNECTIVITY
		userHashId = "UNREGISTERED";
    //#endif

		levelSelected = 0;			// Guardamos el nivel seleccionado para jugar
		currentLevel = 1;			// Nivel al que estamos jugando actualmente (0 a 9) 0=Tutorial, 1=Encierro 1, etc...
		playerSelected = 0;			// Jugador con el que estamos jugando actualmente (0 a 3)
		ferminesItem = new byte[8];	// Anotamos las letras de FERMINES que hemos pillado en cada encierro
		hiscore = 0;				// Puntuacion maxima conseguida
		toreroLocked = true;		// Personaje Torero bloquado?
		levelPoints = new int[9];	// Puntuacion obtenida en cada nivel (de 0 a 8) [0]=Encierro 1, [8]=level infinito
		lastRankingPos = 1024;		// guardamos nuestra ultima posicion
		rankingArrow = -1;			// Flag para indicar la flecha a mostar (-1: ninguna, 0:Arriba, 1:Abajo)
		updateRankingArrow = false;	// Flag para refrescar la flecha que indica si hemos subido o bajado de posicion en el ranking

		scoreFirst = 1;
		scoreFace = new int[] {0,1,2,0,1,2,0,1,2};
		scoreNick = gameText[TEXT_SCORE_NAMES];
		scorePoints = new int[] {666, 512, 481, 316, 299, 265, 152, 144, 72};;

		popupInitInfo(null, new String[] {"Juego Reseteado"});
		biosWait();
	break;


	case MENU_ACTION_DEBUG_RESET_HASH:

	//#ifndef HIDE_CONNECTIVITY
		userHashId = "UNREGISTERED";
    //#endif

		popupInitInfo(null, new String[] {"userHASH Reseteado"});
		biosWait();
	break;


	case MENU_ACTION_DEBUG_LEVEL_FREE:

		currentLevel = 9;

		popupInitInfo(null, new String[] {"Todos los niveles liberados"});
		biosWait();
	break;


	case MENU_ACTION_DEBUG_TORERO_FREE:

		toreroLocked = false;

		popupInitInfo(null, new String[] {"Torero liberado"});
		biosWait();
	break;


	case MENU_ACTION_DEBUG_INMUNE_CHG:

		protInmune = formListOpt() != 0;
	break;


	case MENU_ACTION_DEBUG_SUMA_5_ROSAS:

		levelPoints[8] += 5;
		updateRankingArrow = true;
	// Actualizamos hiscores sumando las rosas pilladas en todos los niveles
		hiscore = 0; for (int i=0 ; i<levelPoints.length ; i++) {hiscore += levelPoints[i];}
	break;

	case MENU_ACTION_DEBUG_SUMA_25_ROSAS:

		levelPoints[8] += 25;
		updateRankingArrow = true;
	// Actualizamos hiscores sumando las rosas pilladas en todos los niveles
		hiscore = 0; for (int i=0 ; i<levelPoints.length ; i++) {hiscore += levelPoints[i];}
	break;

//#endif
// - * - * - * - * -    D  E  B  U  G    - * - * - * - * -
// =======================================================
	}

}


// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if (formTick()) {menuAction(formListCMD);}

	return false;
}


// -------------------
// menu Draw
// ===================

public void menuDraw()
{
	formDraw();

	if (menuFadeColor) {alphaFillDraw(0x80000000, 0,0, canvasWidth, canvasHeight);}
}

// <=- <=- <=- <=- <=-







public boolean inputName()
{
// Si no hemos cambiado nuestro nombre, pedimos que se introduzca
	if (gameText[TEXT_PLAYER_NAMES][1].equals(gamePlayerNames[1]))
	{
		while (true)
		{
			menuInit(MENU_INPUT_NAME_SCORE);
			biosWait();
			if (--menuStackIndex < 0) {menuStackIndex += menuStack.length;}
	
		// Tras haber editado nuestro nombre, si hemos pulsado aceptar lo actualizamos...
			if (menuAcept)
			{
				formEditStr = formEditStr.trim();
		
				if (formEditStr.length() > 0)
				{
					gamePlayerNames[1] = formEditStr;
					savePrefs();

					break;
				}
			}


		// Mostramos popup de error al no haber introducido nombre
			popupClear();
			popupTextStr = gameText[TEXT_INTRODUCIR_NOMBRE_SURE];
//			popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
			popupInit(POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, SOFTKEY_NO, SOFTKEY_YES);

			biosWait();

			if (!popupResult) {return false;}
		}
	}

	return true;
}














// *******************
// -------------------
// play - Engine
// ===================
// *******************
// -------------------

// -----------------------------
// Ejemplo por defecto:
int pelotaX, pelotaY;
int pelotaSideX=1, pelotaSideY=1;
// =============================

int playStatus;
int playExit;
boolean playShow;

int playLeftSoftkeyOld;		// Guardamos la softkey que hay durante el juego para cuando regresemos del menu "ingame"
int playRightSoftkeyOld;	// Guardamos la softkey que hay durante el juego para cuando regresemos del menu "ingame"




//#ifdef BUILD_ANIMATOR
AnimationBank animBank, animBankVasco, animBankChica, animBankTorero, animBankToro, animBankNpc;
//#endif

//#ifdef S40
//#elifdef S60

static final int tileWidth = 16;
static final int tileHeight = 16;

//#elifdef S80
//#endif


int faseWidth;
int faseHeight;


byte[] faseMap;
byte[] faseCol;
byte[] faseCom;

//#ifdef J2ME
	byte[] tilesCor;
	Image tilesImg;
//#elifdef DOJA
//#endif


// -------------------
// play Create
// ===================

public void playCreate()
{
// Refrescamos 'RND_Cont' para tener una semilla aleatoria
	RND_Cont = (int)(System.currentTimeMillis()>>8 & 0x0F);


// Ponemos flag de modo tutorial jugado como minumo una vez
	tutorialViewed = true;





//#ifndef HIDE_SCROLL
// ------------------------------------
// Cargamos tileSets
// ====================================
	scrollCreate(0,0, canvasWidth, canvasHeight);	// Creamos el area del canvas para usar como scroll de la fase

	//#ifdef J2ME
		tilesCor = loadFile("/tiles.cor");

		//#ifdef HIDE_MULTI_PALETE
	    //#else
			tilesImg = loadImage("/tiles", "/tiles_c"+(levelSelected%4)+".pal");
	    //#endif

	//#elifdef DOJA
	//#endif
// ====================================

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("** conTileSets: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("** conTileSets: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

// ====================================
	// Cargamos mapa del juego, pillamos Tama�o.
		faseMap = loadFile("/nivel1.mmp");
		faseWidth = faseMap[0] & 0xff;
		faseHeight = faseMap[1] & 0xff;
		System.arraycopy(faseMap, 2, faseMap, 0, faseWidth*faseHeight);
	
	// Cargamos tabla de tiles combinados
		faseCom = loadFile("/nivel.btsc");
// ====================================

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("** conMapas: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("** conMapas: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

// ====================================
	//#ifdef J2ME
		scrollInit(faseMap, faseCom, faseWidth, faseHeight, tilesImg, tilesCor, 14, 5);
	//#elifdef DOJA
	//#endif
// ====================================
//#endif



//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("** conScroll: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("** conScroll: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

// #ifdef BUILD_ANIMATOR
// ------------------------------------
// Cargamos e inicializamos Re-Animator
// ====================================
	//#ifdef S60
	int spritesMode = 0;
	//#else
	//#endif

	try {
//		 Indicamos la raiz donde estan los recursos ubicados
		if(playerSelected == 0) {

			if(animBankVasco == null) {

				AnimationBank.DATA_DIR = "/vasco";
//				 	Contruimos el banco de animacion (cargamos en memoria TODOS los recursos, binarios y graficos)
				animBankVasco = new AnimationBank(PRUEBAS_VASCO_B0, spritesMode);
			}

			animBank = animBankVasco;

		}

//#ifndef BUILD_ONE_PLAYER

		if(playerSelected == 1) {

			if(animBankChica == null) {

				AnimationBank.DATA_DIR = "/chica";
//			 		Contruimos el banco de animacion (cargamos en memoria TODOS los recursos, binarios y graficos)
				animBankChica = new AnimationBank(PRUEBAS_VASCO_B0, spritesMode);
			}

			animBank = animBankChica;
		}
		if(playerSelected == 2) {

			if(animBankTorero == null) {

				AnimationBank.DATA_DIR = "/torero";
//			 		Contruimos el banco de animacion (cargamos en memoria TODOS los recursos, binarios y graficos)
				animBankTorero = new AnimationBank(PRUEBAS_VASCO_B0, spritesMode);
			}

			animBank = animBankTorero;

		}

//#endif

//		 Contruimos el banco de animacion (cargamos en memoria TODOS los recursos, binarios y graficos)

		if(animBankToro == null) {

//			 Indicamos la raiz donde estan los recursos ubicados
			AnimationBank.DATA_DIR = "/toro";
			animBankToro = new AnimationBank(TORITO_B0, spritesMode);

		}

//#ifndef NONPCS

//		 Contruimos el banco de animacion (cargamos en memoria TODOS los recursos, binarios y graficos)
		if(animBankVasco != null) {

			animBankNpc = animBankVasco;

		} else {

//		 	Indicamos la raiz donde estan los recursos ubicados
			AnimationBank.DATA_DIR = "/vasco";
//		 	Contruimos el banco de animacion (cargamos en memoria TODOS los recursos, binarios y graficos)
			animBankVasco = new AnimationBank(PRUEBAS_VASCO_B0, spritesMode);

			animBankNpc = animBankVasco;
		}
		//#endif

	} catch (Exception e) {
		Gdx.app.log("Animator load exception", e.toString());
	}
// ====================================
//#endif
		
//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("** conAnimator: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("** conAnimator: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif



// ------------------------------------
// Cargamos sprites genericos
// ====================================
		if (obstaclesImg == null) {
			
			//#ifdef J2ME
			obstaclesImg = loadImage("/obstacles");
			//#elifdef DOJA
			//#endif
			obstaclesCor = loadFile("/obstacles.cor");
		}
		
	//#ifndef PRIMITIVEHUD
		if (hudImg == null){
			hudImg = loadImage("/hud");
		}
	//#endif
		if (miscImg == null){
			
			//#ifdef J2ME
			miscImg = loadImage("/misc");
			//#elifdef DOJA
			//#endif
			
			miscCor = loadFile("/misc.cor");
		}
		
		if(levelSelected == 0) {
			
			// Load tutorial specific texts
			
			tutorialText = textosCreate( loadFile("/tutor.txt") );
			
			for(int i = 0; i < tutorialText.length; i++) {
				
				tutorialText[i] = printTextBreak(tutorialText[i][0], canvasWidth);
			}
		}
// ====================================

		
//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("** conSprites: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("** conSprites: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif
	 
		
//#ifdef SPRITEBACKGROUND
//#endif
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
//#ifndef FIX_GC_GRAPHICS
//#endif
	
//#ifndef HIDE_SCROLL
	tilesCor = null;
//#endif

	faseMap = null;
	faseCol = null;
	faseCom = null;


//#ifndef HIDE_SCROLL
	scrollDestroy();	// Desalojamos de memoria todos los elementos de la fase, tiles, sprites, etc...
//#endif
	
	//#ifndef FIX_GC_GRAPHICS
	/*
	//#ifndef DOJA
		AnimationBank.releaseUnusedResources(animBank);
		AnimationBank.releaseUnusedResources(animBankToro);
	
			//#ifndef NONPCS	
				AnimationBank.releaseUnusedResources(animBankNpc);	
			//#endif
	
		AnimationBank.freeAll();
	//#endif
	
	animBankVasco = null;
	animBankChica = null;
	animBankTorero = null;
	animBankToro = null;
	
	//#endif
	
	animBank = null;
	
	//#ifndef NONPCS
	animBankNpc = null;
	//#endif
*/

//#ifndef FIX_GC_GRAPHICS
		obstaclesImg = null;
		obstaclesCor = null;
	//#ifndef PRIMITIVEHUD
		hudImg = null;
	//#endif
		miscImg = null;
		miscCor = null;
//#endif

		
	tutorialText = null;
	
	System.gc();

//#ifdef BUILD_ANIMATOR
	//com.mygdx.mongojocs.sanfermines2006.AnimationBank.releaseUnusedResources(protAnim.bank);
	//protAnim = null;
//#endif
	
//#ifndef FIX_GC_GRAPHICS
	//#ifdef SPRITEBACKGROUND
	//#endif
//#endif

//#ifdef DEBUG_PC_USED_MEM
	System.gc(); System.out.println("playDestroy()End: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#ifdef DEBUG
		System.gc();  Debug.println("playDestroy()End: "+(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	//#endif
//#endif

/*
	com.mygdx.mongojocs.sanfermines2006.Debug.enabled = true;
	forceRender();
	waitTime(5000);
*/
}

// -------------------
// play Init
// ===================

public void playInit()
{

	/* David */
		
	runner = new Entity(Entity.CLASS_PLAYER);
	runner.entitySubclass = playerSelected;
	runner.reset();
		
	runner.sprite = new AnimatedSprite(animBank);
	runner.sprite.setSequence(GameCanvas.QUIETO_B0_S4);
		
	bull = new Entity[NUM_BULLS];
		
	camera = new Entity(-1);
			
	//com.mygdx.mongojocs.sanfermines2006.Entity.createMap(500);
	Entity.loadMap("/encierro"+levelSelected+".mmp");
	
	if(levelSelected == SURVIVAL_LEVEL) {
		
		Entity.backupMap();
		Entity.createRandomMap();
	}
		
	entityManagerInit();
	entityManagerAdd(runner);
	
	runner.setPosition(0,Entity.mapLength * Entity.TILE_WIDTH - 20,0);
	runner.updateBoundingBox();
		
	// Create bulls
			
	if(levelSelected > 0) {
		
		createBulls(270, Entity.BULL_RUN_SPEED);
	}
						
	// Create bots
				
	//#ifndef NONPCS
	
	createNPCs();
			
	//#endif
		
	// Create chupinazo
	
	
	//#ifndef NOPARTICLES
	
	Entity e = new Entity(Entity.CLASS_PARTICLE);
	e.entitySubclass = Entity.PARTICLE_SUBCLASS_CHUPINAZO;
	
	e.setPosition(0, Entity.mapLength * Entity.TILE_WIDTH + 65, 0);
		
	e.lifeTime = 20;
	e.rVelocityX = 0;
	e.rVelocityY = -1024;
	e.rVelocityZ = 2048;
	
	e.rAccelerationZ = 0;

	entityManagerAdd(e);
	
	//#endif
				
	wideScreenTop = levelSelected == 0 ? canvasHeight / 3 : 0;
	wideScreenBot = 0;
	//#ifdef S80
	//#else
	screenBarSize = 20;
	//#endif
	gameTicks = 0;	
	tutorialStep = 0;
	playStatus = PLAY_STATUS_INTRO;
	tutorialMsg = null;
}

public void createBulls(int playerDist, int maxSpeed) {
	
	for(int i = 0; i < NUM_BULLS; i++) {
		
		bull[i] = new Entity(Entity.CLASS_BULL);
		bull[i].setPosition(-30 + 20*i, runner.iBboxBotY - 20 + playerDist + 5*(i%2), 0);
		bull[i].link = runner;
		bull[i].miscData[Entity.BULL_DATA_MAXSPEED] = maxSpeed;
		bull[i].rVelocityY = maxSpeed;
		bull[i].updateBoundingBox();
			
		bull[i].sprite = new AnimatedSprite(animBankToro);
		bull[i].sprite.setSequence(CORRER_B0_S0);
			
		for(int j = 0; j < i; j++) {
				
			bull[i].sprite.updateFrameLoop();
		}
					
	}
		
	for(int i = 0; i < NUM_BULLS; i++) {
			
		bull[i].collidable = new Entity[NUM_BULLS - 1];
			
		int index = 0;
			
		for(int j = 0; j < NUM_BULLS; j++) {
				
			if(j != i) {
				
				bull[i].collidable[index] = bull[j];
				index++;
			}
		}
			
		entityManagerAdd(bull[i]);		
	}	
}

//#ifndef NONPCS

void createNPCs() {

	
	for(int i = 0; i < Entity.MAP_WIDTH; i++) {
		for(int j = 0; j < Entity.mapLength; j++) {
			
			if(Entity.levelMap[Entity.MAP_WIDTH*j + i] == 14) {
		
				bot = new Entity(Entity.CLASS_NPC);
				bot.entitySubclass = rnd(2);
					
				bot.sprite = new AnimatedSprite(animBankNpc);
				bot.sprite.setSequence(bot.entitySubclass == 0 ? BOOT1_CORRER_B0_S13 : BOOT2_CORRER_B0_S17);
				bot.link = runner;															
				bot.setPosition(i * Entity.TILE_WIDTH - Entity.STREET_WIDTH / 2, j * Entity.TILE_WIDTH,0);
					
				entityManagerAdd(bot);
					
			}
		}
	}
}

//#endif

// -------------------
// play Release
// ===================

public void playRelease()
{
	/* David */
	
	entityManagerRelease();
	runner = null;
	bull = null;
	camera = null;
}

// -------------------
// play Tick
// ===================

public static final int PLAY_STATUS_INTRO = 0;
public static final int PLAY_STATUS_RUN = 1;
public static final int PLAY_STATUS_EXITED = 2;
public static final int PLAY_STATUS_PAUSED = 3;

public int gameTicks;

public boolean playTick()
{

// -----------------------------
// Ejemplo por defecto:
	/*pelotaX += pelotaSideX * 3;
	pelotaY += pelotaSideY * 4;

	if (pelotaX < 0 || pelotaX > canvasWidth) {pelotaSideX *= -1;}
	if (pelotaY < 0 || pelotaY > canvasHeight) {pelotaSideY *= -1;}*/
// =============================

	gameTicks ++;
	
	//#ifdef FAST150
	//#endif
	
	
	// Tutorial features
	
	if(levelSelected == 0) {
		
					
		int mapY = runner.iBboxBotY / Entity.TILE_WIDTH;
	
		if(mapY < -1 && tutorialStep < 11) {
			
			tutorialMsg = null;			
			tutorialStep = 11;
		
		} else if(mapY < 2 && tutorialStep < 10) {
			
			tutorialMsg = tutorialText[10];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 10;				
		
		} else if(mapY < 9 && tutorialStep < 9) {
			
			tutorialMsg = tutorialText[9];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 9;
			
		} else if(mapY < 41 && tutorialStep < 8) {
			
			tutorialMsg = tutorialText[8];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 8;
			
		} else if (mapY < 48 && tutorialStep < 7) {
			
			tutorialMsg = tutorialText[7];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 7;
						
		} else if (mapY < 66 && tutorialStep < 6) {
			
			tutorialMsg = tutorialText[6];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 6;
			
		} else if (mapY < 74 && tutorialStep < 5) {
			
			tutorialMsg = tutorialText[5];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 5;
			
		} else if (mapY < 88 && tutorialStep < 4) {
			
			tutorialMsg = tutorialText[4];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 4;
			
		} else if (mapY < 98 && tutorialStep < 3) {
						
			tutorialMsg = tutorialText[3];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 3;
				
		} else if (mapY < 123 && tutorialStep < 2) {
			
			tutorialMsg = tutorialText[2];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 2;
			
		} else if (mapY < 168 && tutorialStep < 1) {
		
			tutorialMsg = tutorialText[1];
			playStatus = PLAY_STATUS_PAUSED;
			tutorialStep = 1;
			
		} else if (tutorialStep == 0) {
			
			tutorialMsg = tutorialText[0];
		}
		
		
		// Bulls appear
		
		if(runner.miscData[Entity.PLAYER_DATA_TURBOS] > 0 && bull[0] == null) {
			
			createBulls(100, Entity.BULL_RUN_SPEED);
		}
		
		// Prevent bulls from catching player
		
		/*if(bull[0] != null) {
			
			for(int i = 0; i < bull.length; i++) {
																					
				if(bull[i].iBboxBotY < runner.iBboxBotY + 40) {
																	
					bull[i].miscData[com.mygdx.mongojocs.sanfermines2006.Entity.BULL_DATA_MAXSPEED] = runner.rVelocityY;
					bull[i].rVelocityY = bull[i].miscData[com.mygdx.mongojocs.sanfermines2006.Entity.BULL_DATA_MAXSPEED];
				}
			}
		}	*/	
		
		// Pause management
		
		if(playStatus == PLAY_STATUS_PAUSED) {
			
			if(wideScreenBot < canvasHeight / 4) {
			
				wideScreenBot += 4;
			}
			
			if(keyMenu != 0 && lastKeyMenu != keyMenu) {
			
				playStatus = PLAY_STATUS_RUN;
			}
		}
	}
	
	// Random loop level
	
	if(levelSelected == SURVIVAL_LEVEL) {
		
		// Recycle bulls
		
		for(int i = 0; i < bull.length; i++) {
			
			//System.out.println("Bull "+i+":"+bull[i].iBboxBotY+", Player:"+runner.iBboxBotY);
			
			bull[i].miscData[Entity.BULL_DATA_MAXSPEED] = Entity.BULL_RUN_SPEED + 40 - gameTicks / 20;
			
			if(bull[i].iBboxBotY < runner.iBboxBotY - 250) {
				
				bull[i].rPosY += 500 << 8;
				bull[i].updateBoundingBox();

				boolean conflict = Entity.mapCollision(bull[i].iBboxBotX + bull[i].iBboxSizeX / 2, bull[i].iBboxBotY); 
				
				for(int j = 0; j < bull.length; j++) {
				
					if(i != j && Entity.bboxIntersection(bull[i], bull[j])) {
					
						conflict = true;
					}
				}
				
				while(conflict) {
					
					bull[i].rPosY += Entity.TILE_WIDTH << 8;
					bull[i].updateBoundingBox();
										
					conflict = Entity.mapCollision(bull[i].iBboxBotX + bull[i].iBboxSizeX / 2, bull[i].iBboxBotY); 
					
					for(int j = 0; j < bull.length; j++) {
					
						if(i != j && Entity.bboxIntersection(bull[i], bull[j])) {
						
							conflict = true;
						}
					}
				}
			}
						
		}
	
		// Reset level
		
		if (runner.iBboxBotY < 5 * Entity.TILE_WIDTH) {
			
			for(int i = 0; i < entityCount; i++) {
			
				Entity e = entityList[i];
				
				e.rPosY += e.intToReal(190 * Entity.TILE_WIDTH);
				e.updateBoundingBox();
				
			}			
			
			Entity.resetRandomMap();
			
			//#ifndef NONPCS
			
			createNPCs();
					
			//#endif
		}
		
	}
	
	// World entities update 
	
	if(playStatus != PLAY_STATUS_PAUSED) {
		
		entityManagerUpdateAll();
		
	}
	
	
	// Camera update
	
	if(runner.entityState != Entity.PLAYER_STATE_HORNED) {
		
		camera.rPosX = 0;
		camera.rPosY = runner.rPosY;
		camera.rPosZ = 0;
		
		if(camera.rPosY < 90<<8 && levelSelected != SURVIVAL_LEVEL) {
			
			camera.rPosY = 90<<8;
		}
		
		camera.updateBoundingBox();
	}	
	
	if(playStatus == PLAY_STATUS_INTRO) {
	
		// Final de la secuencia del chupinazo
		
		if(gameTicks > 40) {
		
			playStatus = PLAY_STATUS_RUN;
			runner.setState(Entity.PLAYER_STATE_RUN);
			runner.sprite.setSequence(CORRER_B0_S0);
			
//			-----------
//			Si el juego tiene musica durante el juego:
		//#ifndef HIDE_SOUND_INGAME
			soundPlay(MUSIC_INGAME, 0);
		//#endif
//			===========
		}
	}
		
	if(playStatus == PLAY_STATUS_RUN) {
	
		if (wideScreenTop > 0 && levelSelected != 0) {
		
			wideScreenTop -= 4;
		}
		
		if (wideScreenBot > 0) {
			
			wideScreenBot -= 4;
		}
		
		if(screenBarSize > 0) {
			
			screenBarSize -= 2;
		}
		
		if(runner.entityState == Entity.PLAYER_STATE_HORNED && runner.ticks > 40) {
			
			// Nos ha cogido el Toro
			
			
			if(levelSelected == SURVIVAL_LEVEL) {
				
				// Survival end: count score
				
				roseCount = runner.miscData[Entity.PLAYER_DATA_ROSE_COUNT];
				
				playExit = 1;
				
			} else {
												
				playExit = 2;
			}
		}
		
			
		if(runner.iBboxBotY < -20 && runner.entityState != Entity.PLAYER_STATE_HORNED) {
		
			// Nivel completado
			
			playStatus = PLAY_STATUS_EXITED;
			gameTicks = 0;
			soundPlay(MUSIC_LEVEL_COMPLETED,1);
			tutorialMsg = null;
			if(runner.miscData[Entity.PLAYER_DATA_LETTER_FOUND] != 0) {
				
				ferminesItem[levelSelected - 1] = 1;
			}
						
		}
			
	}
	
	if(playStatus == PLAY_STATUS_EXITED) {
					
		//wideScreenTop += 4;					
		//wideScreenBot += 4;
		
		if(wideScreenTop > 0) {
			
			wideScreenTop -= 4;
		}
		
		if(wideScreenBot > 0) {
			
			wideScreenBot -= 4;
		}
		
		//#ifdef S80
		//#else
		if(screenBarSize < 20*gameText[TEXT_ENCIERRO_COMPLETADO].length) {
		//#endif
			
			screenBarSize += 2;
		}
		
		if(gameTicks > 40) {
			
			// Nivel completado
			
			roseCount = runner.miscData[Entity.PLAYER_DATA_ROSE_COUNT];
									
			playExit = 1;
		}
	}
	
	if(gameTicks == 10 && playStatus == PLAY_STATUS_INTRO) {
		
		soundPlay(MUSIC_CHUPINAZO,1);
	}
	
	if(wideScreenTop > canvasHeight / 2) {
		
		wideScreenTop = canvasHeight / 2;
	}
	
	if(wideScreenTop < 0) {
		
		wideScreenTop = 0;
	}
	
	if(wideScreenBot > canvasHeight / 2) {
		
		wideScreenBot = canvasHeight / 2;
	}
	
	if(wideScreenBot < 0) {
		
		wideScreenBot = 0;
	}
		
	if (playExit!=0) {return true;}		// Debemos salir de fa fase??  Muerte, siguinte nivel, etc...

	playShow = true;		// Notificamos para el "repaint()" se refresque el play Engine. (motor del juego)

	return false;
}

// -------------------
// play Draw
// ===================


public void playDraw()
{
	
	//#ifdef FRAMESKIP
	//#endif
	
	//#ifdef FRAMESKIP1OF3
	//#endif
			
	//fillDraw(0, 0, 0, canvasWidth, canvasHeight);

	cameraTarget = camera; 

	//#ifdef SMALLBACKGROUND
	//#elifdef SPRITEBACKGROUND
	//#else
	// 38, 60
	int scrollx = isoX(cameraTarget.iBboxBotX, 
		    		   (cameraTarget.iBboxBotY % 608) + 752),
		    		   
		scrolly = isoY(cameraTarget.iBboxBotX, 
			    	   (cameraTarget.iBboxBotY % 608) + 752, 
			    	   cameraTarget.iBboxBotZ) + 
			    	   
			    	   //#ifdef S60
			    	   -64;
					   //#elifdef S40
					   //#elifdef S80
					   //#endif
	//#endif
	
	cameraIsoX = isoX(cameraTarget.iBboxBotX, 
					  cameraTarget.iBboxBotY);
	
	cameraIsoY = isoY(cameraTarget.iBboxBotX, 
		    		  cameraTarget.iBboxBotY, 
		    		  cameraTarget.iBboxBotZ);


//#ifndef HIDE_SCROLL
// ----------------------------
// Gestionamos las coordenadas del scroll del juego
// ============================

	//#ifdef DEBUG
		Debug.spdStart(2);
	//#endif

	//scrollTick_Center(scrollx, scrolly);
	scrollTick_Center(scrollx, scrolly);
// ============================

	//#ifdef DEBUG
		Debug.spdStop(2);
		Debug.spdStart(3);
	//#endif

// ----------------------------
// Renderizamos el scroll del juego
// ============================
	scrollDraw(scr);
// ============================
//#else

		
//#endif

//#endif
	
	
	
	






//#ifdef DEBUG
	Debug.spdStop(3);
//#endif	
	

//#ifdef BUILD_ANIMATOR
// ----------------------------
// Renderizamos todos los sprites del gestor de animaciones
// ============================
	
// ============================
//#endif



/*
// -----------------------------
// Ejemplo por defecto:
	fillDraw(0, 0, 0, canvasWidth, canvasHeight);

	fillDraw(0xffffff, pelotaX, pelotaY, 4, 4);
// =============================
*/
	
	/* David */
			
	//fillDraw(0xaaaaaa, 0, 0, canvasWidth, canvasHeight);
	
	
//#ifdef DEBUG
	Debug.spdStart(4);
//#endif
	
	
	int ix;
	int iy;
				
	//#ifdef SMALLMAPWINDOW
	//#elifdef LARGEMAPWINDOW
	int windowBotY = (camera.iBboxBotY/Entity.TILE_WIDTH)*Entity.TILE_WIDTH - 160;
	int windowTopY = (camera.iBboxBotY/Entity.TILE_WIDTH)*Entity.TILE_WIDTH + 140;
	//#else
	//#endif
	
	//#ifndef ONERENDERSTEP	
			
	for(int x = 0; x < Entity.MAP_WIDTH; x++) {

		for(int i = windowBotY; i < windowTopY; i += Entity.TILE_WIDTH) {
						
			int mapY = i/Entity.TILE_WIDTH;
					
			if(mapY >= 0 && mapY < Entity.mapLength) {
										
				// Background elements
										
				byte tileValue = Entity.levelMap[mapY*Entity.MAP_WIDTH + x];
				
				if(tileValue != Entity.TILE_EMPTY) {
					
					ix = screenIsoX(x*Entity.TILE_WIDTH - Entity.STREET_WIDTH/2,mapY*Entity.TILE_WIDTH);
					iy = screenIsoY(x*Entity.TILE_WIDTH - Entity.STREET_WIDTH/2,mapY*Entity.TILE_WIDTH + Entity.TILE_WIDTH/2,0);
					
					//#ifdef S40
					//#elifdef S60					
					ix -= 18;
					iy -= 43;
					//#elifdef S80
					//#endif
										
					switch(tileValue) {
					
						case 1 :
						spriteDraw(obstaclesImg,  ix, iy,  5, obstaclesCor);
						break;
							
						case 2 :
						spriteDraw(obstaclesImg,  ix, iy,  4, obstaclesCor);
						break;
										
						case 5 :
						spriteDraw(obstaclesImg,  ix, iy,  1, obstaclesCor);
						break;
							
						case 6 :
						spriteDraw(obstaclesImg,  ix, iy,  0, obstaclesCor);
						break;
																								
						case 15 :
						spriteDraw(obstaclesImg,  ix, iy,  16, obstaclesCor);
						break;
						
						case 23 :
						spriteDraw(obstaclesImg,  ix, iy,  17, obstaclesCor);
						break;
						
						case 24 :
						spriteDraw(obstaclesImg,  ix, iy,  18, obstaclesCor);
						break;
						
						case 25 :
						spriteDraw(obstaclesImg,  ix, iy,  19, obstaclesCor);
						break;
						
					}
				}
			}			
		}
	}
	
	//#endif
		
	int e = 0;
	
	for(int x = 0; x < Entity.MAP_WIDTH; x++) {

		for(int i = windowBotY; i < windowTopY; i += Entity.TILE_WIDTH) {
							
			int mapY = i/Entity.TILE_WIDTH;
			
			if(mapY >= 0 && mapY < Entity.mapLength) {
										
				// Entities
					
				if (e < entityCount) {
					
					boolean entitiesPending = true;
				
					while(e < entityCount && entitiesPending) {
						
						// INLINE: entitiesPending = ! inFrontOf(entityList[e], x, mapY);
						// ===============================================================
						
						int cx;
						int cy;
						Entity ee = entityList[e];
						
						//#ifndef NOPARTICLES
						
						if(ee.entityClass == Entity.CLASS_PARTICLE) {
							
							cx = ((ee.rPosX >> 8) + Entity.STREET_WIDTH / 2) / Entity.TILE_WIDTH;
									
						} else {
							
						//#endif
							
							cx = (ee.iBboxBotX + ee.iBboxSizeX / 2 + Entity.STREET_WIDTH / 2) / Entity.TILE_WIDTH;
							
						
                        //#ifndef NOPARTICLES
							
						}
						
						//#endif
						
						if (cx < x) {
							
							entitiesPending = true;
									
						} else if(cx == x) {
							
							//#ifndef NOPARTICLES
							
							if(ee.entityClass == Entity.CLASS_PARTICLE) {
											
								cy = (ee.rPosY >> 8) / Entity.TILE_WIDTH;
								
							} else {
								
							//#endif
										
								cy = (ee.iBboxBotY + ee.iBboxSizeY / 2) / Entity.TILE_WIDTH;
								
                            //#ifndef NOPARTICLES
								
							}
							
							//#endif
							
							entitiesPending = cy <= mapY;
							
						} else {
							
							entitiesPending = false;		
						}
																	
						// ===============================================================
							
						if(entitiesPending) {
							
							renderEntity(entityList[e]);
																					
							e++;
						}							
					}
				}
										
				// Background elements
					
				//scr.setClip(0,0,canvasWidth,canvasHeight);
					
				byte tileValue = Entity.levelMap[mapY*Entity.MAP_WIDTH + x];
					
				if(tileValue != Entity.TILE_EMPTY) {
						
					//INLINE: ix = screenIsoX(x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2,mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH);
					//============================================================================================
					
					//INLINE: sx = isoX(x, y);
					//=======================================
					
					//#ifdef S40

					//#elifdef S60
					
					int sx = x*Entity.TILE_WIDTH - Entity.STREET_WIDTH/2 - mapY*Entity.TILE_WIDTH;
					
					//#elifdef S80
					
					//#endif
					
					//=======================================

					ix = canvasWidth/2 + sx - cameraIsoX;
					
					//==============================================
					
					
					//INLINE: iy = screenIsoY(x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2,mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH + com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH/2,0);
					//============================================================================================
					
					//INLINE: sy = isoY(x, y, z);
					//=======================================
					
					//#ifdef S40
					
					//#elifdef S60
					
					int sy = ((mapY*Entity.TILE_WIDTH + Entity.TILE_WIDTH/2)>>1) + ((x*Entity.TILE_WIDTH - Entity.STREET_WIDTH/2)>>1);
					
					//#elifdef S80
					//#endif
					
					//=======================================
						
					iy = 
					
                    //#ifdef S60
					
					2*canvasHeight/3 + 20
					
					//#elifdef S40
					//#elifdef S80
					//#endif
					
					+ sy - cameraIsoY;
					
					//============================================================================================
					
					//#ifdef S40
					//#elifdef S60					
					ix -= 18;
					iy -= 43;
					//#elifdef S80
					//#endif
														
					switch(tileValue) {
					
						//#ifdef ONERENDERSTEP
						//#endif
												
						case 3 :						
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH/4}, 0xff0000);
						spriteDraw(obstaclesImg,  ix, iy,  3, obstaclesCor);
						break;
							
						case 4 :						
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH/4}, 0xff0000);
						spriteDraw(obstaclesImg,  ix, iy,  2, obstaclesCor);
						break;
																														
						case 7 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH * 2}, 0xff0000);
						spriteDraw(obstaclesImg,  ix, iy,  8, obstaclesCor);
						break;
						
						case 8 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH * 2}, 0xff0000);
						spriteDraw(obstaclesImg,  ix, iy,  23, obstaclesCor);
						break;
						
						case 9 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH * 2}, 0xff0000);
						spriteDraw(obstaclesImg,  ix, iy,  22, obstaclesCor);
						break;
						
						case 10 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH * 2}, 0xff0000);							spriteDraw(obstaclesImg,  ix, iy,  3, obstaclesCor);
						spriteDraw(obstaclesImg,  ix, iy,  20, obstaclesCor);
						break;
						
						case 11 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH * 2}, 0xff0000);
						spriteDraw(obstaclesImg,  ix, iy,  35 + (gameTicks % 4), obstaclesCor);
						break;
						
						case 12 :
						spriteDraw(obstaclesImg,  ix, iy,  
								(runner.entityState == Entity.PLAYER_STATE_HIGH_JUMP && runner.ticks < 20 && (runner.iBboxBotX + runner.iBboxSizeX / 2 + Entity.STREET_WIDTH / 2) / Entity.TILE_WIDTH == x ? 32 + (runner.ticks % 3) : 32), 
								obstaclesCor);
						break;						
																															
						case 13 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						//imageDraw(vespaImg,0,0,41,43,ix + 18 - 13, iy + 43 - 32);
						//#ifdef S60
						spriteDraw(miscImg, ix + 5, iy + 11, 72, miscCor);
						//#elifdef S40
						//#elifdef S80
						//#endif
						break;
						
						case 16 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						spriteDraw(obstaclesImg,  ix, iy,  9, obstaclesCor);
						break;
						
						case 17 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						spriteDraw(obstaclesImg,  ix, iy,  10, obstaclesCor);
						break;
						
						case 18 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						spriteDraw(obstaclesImg,  ix, iy,  11, obstaclesCor);
						break;
						
						case 19 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						spriteDraw(obstaclesImg,  ix, iy,  12, obstaclesCor);
						break;
						
						case 20 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						spriteDraw(obstaclesImg,  ix, iy,  13, obstaclesCor);
						break;
						
						case 21 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						spriteDraw(obstaclesImg,  ix, iy,  14, obstaclesCor);
						break;
						
						case 22 :
						//drawBoundingBox(new int[] {x*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.STREET_WIDTH/2, mapY*com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH - com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, 0, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH*2, com.mygdx.mongojocs.sanfermines2006.Entity.TILE_WIDTH}, 0xff8000);
						spriteDraw(obstaclesImg,  ix, iy,  15, obstaclesCor);
						break;
						
						//#ifndef BUILD_ONE_PLAYER
						
						case 26 :
						if(ferminesItem[levelSelected - 1] == 0) {
								
							spriteDraw(obstaclesImg,  ix, iy,  24 + levelSelected - 1, obstaclesCor);
						}
						break;
						
						//#endif
																																																							
					}
				}
			}
		}			
	}
	
	// Entities
	
	while(e < entityCount) {
			
		if(entityList[e].iBboxBotY < windowTopY) {
			
			renderEntity(entityList[e]);
		}
		
		e++;	
	}
	
//#ifdef DEBUG
	Debug.spdStop(4);
	Debug.spdStart(5);
//#endif	

	
	// HUD
	
	hudDraw();

	
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif	
	
	if(gameTicks == 21 && playStatus == PLAY_STATUS_INTRO) {
					
		fillDraw(0xffffff, 0, 0, canvasWidth, canvasHeight);
	}
	
	// Wide screen bands
	
	int wideScreenTop2 = wideScreenTop;
	
	if(tutorialMsg != null) {
		
		wideScreenTop2 = tutorialMsg.length * printGetHeight() > wideScreenTop ? tutorialMsg.length * printGetHeight() + 10 : wideScreenTop;
	}
	
	if(wideScreenTop2 > 0) {
		
		fillDraw(0xff2920, 0, 0, canvasWidth, wideScreenTop2);
		fillDraw(0xff5a3f, 0, wideScreenTop2 - 6, canvasWidth, 2);
		fillDraw(0xff7459, 0, wideScreenTop2 - 4, canvasWidth, 2);
		fillDraw(0xcc0000, 0, wideScreenTop2 - 2, canvasWidth, 1);
		fillDraw(0x000000, 0, wideScreenTop2 - 1, canvasWidth, 1);
	}
	
	if(wideScreenBot > 0) {
	
		fillDraw(0xff2920, 0, canvasHeight - wideScreenBot, canvasWidth, wideScreenBot);
		
		if(wideScreenBot >= 1) {
			
			fillDraw(0x000000, 0, canvasHeight - wideScreenBot, canvasWidth, 2);
		}
	
		if(wideScreenBot >= 3) {
		
			fillDraw(0xcc0000, 0, canvasHeight - wideScreenBot + 1, canvasWidth, 2);
		}
	
		if(wideScreenBot >= 5) {
		
			fillDraw(0xeb0016, 0, canvasHeight - wideScreenBot + 3, canvasWidth, 2);
		}
	}
	
	// Screen bar
	
	String msg[] = null;
			
	if (screenBarSize > 0) {
		
		int by = (canvasHeight - screenBarSize) / 2;
		
		fillDraw(0xff2920, 0, by,  canvasWidth, screenBarSize);
		
		if (screenBarSize >= 7) {
			
			fillDraw(0xff5a3f, 0, by + 5, canvasWidth, 2);
		}
		
		if (screenBarSize >= 5) {
			
			fillDraw(0xff7459, 0, by + 3, canvasWidth, 2);
		}
		
		if (screenBarSize >= 3) {
			
			fillDraw(0xcc0000, 0, by + 1, canvasWidth, 1);
		}
		
		if (screenBarSize >= 1) {
			
			fillDraw(0x000000, 0, by, canvasWidth, 1);
		}
		
		if (screenBarSize >= 5) {
			
			fillDraw(0xeb0016, 0, by + screenBarSize - 5, canvasWidth, 2);
		}
		
		if (screenBarSize >= 3) {
			
			fillDraw(0xcc0000, 0, by + screenBarSize - 3, canvasWidth, 2);
		}
		
		if (screenBarSize >= 1) {
			
			fillDraw(0x000000, 0, by + screenBarSize - 1, canvasWidth, 2);
		}
		
		msg = null;
		
		if(playStatus == PLAY_STATUS_INTRO && screenBarSize >= 10) {
			
			if (levelSelected == 9) {
				
				msg = gameText[53];
				
			} else if (levelSelected == 0) {
				
				msg = gameText[59];
					
			} else {
				
				msg = new String[] { gameText[TEXT_ENCIERRO][0]+" "+levelSelected };
			}			
		}
		
		if(playStatus == PLAY_STATUS_EXITED && screenBarSize >= 10) {
		
			msg = gameText[TEXT_ENCIERRO_COMPLETADO];
		}
		
		if(msg != null) {
			
			//fontInit(FONT_SMALL_WHITE);
			printSetArea(0, 0, canvasWidth, canvasHeight);
			printDraw(msg,0,0,0xffffff,PRINT_HCENTER|PRINT_VCENTER);
		}
	}
	
	
	if(levelSelected == 0 && tutorialMsg != null) {
									
		//fontInit(FONT_SMALL_WHITE);
		printSetArea(0, 0, canvasWidth, wideScreenTop2);
		printDraw(tutorialMsg,0,0,0xffffff,PRINT_HCENTER|PRINT_VCENTER);		
		
	}
			
	if(playStatus == PLAY_STATUS_PAUSED) {
		
		//fontInit(FONT_SMALL_WHITE);
		printSetArea(0, canvasHeight - wideScreenBot, canvasWidth, wideScreenBot);
		printDraw(tutorialText[11],0,0,0xffffff,PRINT_HCENTER|PRINT_VCENTER|PRINT_MASK);
	}
		
				
	//#ifdef DEBUG
	Debug.spdStop(5);
	//#endif
	
	
}


// -------------------
// play Refresh
// ===================

public void playRefresh()
{
	playShow = true;
}

// <=- <=- <=- <=- <=-


//David ===========================================================================

//#ifdef J2ME
Image obstaclesImg, hudImg, miscImg;
//#elifdef DOJA
//#endif
byte obstaclesCor[], miscCor[];
int wideScreenTop, wideScreenBot, screenBarSize = 0;
int roseCount, totalRoses;
String tutorialMsg[], tutorialText[][];
int tutorialStep;

//#ifdef SPRITEBACKGROUND
//#endif

public static final int NUM_BULLS = 3;
public static final int SURVIVAL_LEVEL = 9;

Entity runner;
Entity bull[];
Entity bot;
Entity camera;

Entity cameraTarget;

int cameraIsoX;
int cameraIsoY;

public int isoX(int x, int y) {

	//#ifdef S40
	//#elifdef S60
	
	return x - y;
	
	//#elifdef S80
	//#endif
	
}

public int isoY(int x, int y, int z) {

	//#ifdef S40
	//#elifdef S60
	
	return (y>>1) + (x>>1) - z;
	
	//#elifdef S80
	//#endif
	
	
}

public int screenIsoX(int x, int y) {
	
	return canvasWidth/2 + isoX(x, y) - cameraIsoX;	
}

public int screenIsoY(int x, int y, int z) {
		
	return 
	
	//#ifdef S60
	
	2*canvasHeight/3 + 20
	
	//#elifdef S40
	//#elifdef S80
	//#endif
	
	+ isoY(x, y, z) - cameraIsoY;
		   
}


public void drawBoundingBox(int b[], int c)
{
	//if (!showBoundingBoxes) return;
	
	//#ifdef J2ME
	scr.setClip(0, 0, canvasWidth, canvasHeight);
	//#endif
	
	putColor(c);
	
	/*
	scr.drawLine(screenIsoX(b[0],b[1],b[2]), screenIsoY(b[0],b[1],b[2]), screenIsoX(b[0] + b[3],b[1],b[2]), screenIsoY(b[0] + b[3],b[1],b[2]));
	scr.drawLine(screenIsoX(b[0],b[1],b[2]), screenIsoY(b[0],b[1],b[2]), screenIsoX(b[0],b[1] + b[4],b[2]), screenIsoY(b[0],b[1] + b[4],b[2]));
	scr.drawLine(screenIsoX(b[0],b[1] + b[4],b[2]), screenIsoY(b[0],b[1] + b[4],b[2]), screenIsoX(b[0] + b[3],b[1] + b[4],b[2]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1],b[2]), screenIsoY(b[0] + b[3],b[1],b[2]), screenIsoX(b[0] + b[3],b[1] + b[4],b[2]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2]));
	
	scr.drawLine(screenIsoX(b[0],b[1],b[2]), screenIsoY(b[0],b[1],b[2]), screenIsoX(b[0],b[1],b[2] + b[5]), screenIsoY(b[0],b[1],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1],b[2]), screenIsoY(b[0] + b[3],b[1],b[2]), screenIsoX(b[0] + b[3],b[1],b[2] + b[5]), screenIsoY(b[0] + b[3],b[1],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0],b[1] + b[4],b[2]), screenIsoY(b[0],b[1] + b[4],b[2]), screenIsoX(b[0],b[1] + b[4],b[2] + b[5]), screenIsoY(b[0],b[1] + b[4],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1] + b[4],b[2]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2]), screenIsoX(b[0] + b[3],b[1] + b[4],b[2] + b[5]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2] + b[5]));
		
	scr.drawLine(screenIsoX(b[0],b[1],b[2] + b[5]), screenIsoY(b[0],b[1],b[2] + b[5]), screenIsoX(b[0] + b[3],b[1],b[2] + b[5]), screenIsoY(b[0] + b[3],b[1],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0],b[1],b[2] + b[5]), screenIsoY(b[0],b[1],b[2] + b[5]), screenIsoX(b[0],b[1] + b[4],b[2] + b[5]), screenIsoY(b[0],b[1] + b[4],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0],b[1] + b[4],b[2] + b[5]), screenIsoY(b[0],b[1] + b[4],b[2] + b[5]), screenIsoX(b[0] + b[3],b[1] + b[4],b[2] + b[5]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1],b[2] + b[5]), screenIsoY(b[0] + b[3],b[1],b[2] + b[5]), screenIsoX(b[0] + b[3],b[1] + b[4],b[2] + b[5]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2] + b[5]));
	*/
	
	scr.drawLine(screenIsoX(b[0],b[1]), screenIsoY(b[0],b[1],b[2]), screenIsoX(b[0] + b[3],b[1]), screenIsoY(b[0] + b[3],b[1],b[2]));
	scr.drawLine(screenIsoX(b[0],b[1]), screenIsoY(b[0],b[1],b[2]), screenIsoX(b[0],b[1] + b[4]), screenIsoY(b[0],b[1] + b[4],b[2]));
	scr.drawLine(screenIsoX(b[0],b[1] + b[4]), screenIsoY(b[0],b[1] + b[4],b[2]), screenIsoX(b[0] + b[3],b[1] + b[4]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1]), screenIsoY(b[0] + b[3],b[1],b[2]), screenIsoX(b[0] + b[3],b[1] + b[4]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2]));
	
	scr.drawLine(screenIsoX(b[0],b[1]), screenIsoY(b[0],b[1],b[2]), screenIsoX(b[0],b[1]), screenIsoY(b[0],b[1],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1]), screenIsoY(b[0] + b[3],b[1],b[2]), screenIsoX(b[0] + b[3],b[1]), screenIsoY(b[0] + b[3],b[1],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0],b[1] + b[4]), screenIsoY(b[0],b[1] + b[4],b[2]), screenIsoX(b[0],b[1] + b[4]), screenIsoY(b[0],b[1] + b[4],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1] + b[4]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2]), screenIsoX(b[0] + b[3],b[1] + b[4]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2] + b[5]));
		
	scr.drawLine(screenIsoX(b[0],b[1]), screenIsoY(b[0],b[1],b[2] + b[5]), screenIsoX(b[0] + b[3],b[1]), screenIsoY(b[0] + b[3],b[1],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0],b[1]), screenIsoY(b[0],b[1],b[2] + b[5]), screenIsoX(b[0],b[1] + b[4]), screenIsoY(b[0],b[1] + b[4],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0],b[1] + b[4]), screenIsoY(b[0],b[1] + b[4],b[2] + b[5]), screenIsoX(b[0] + b[3],b[1] + b[4]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2] + b[5]));
	scr.drawLine(screenIsoX(b[0] + b[3],b[1]), screenIsoY(b[0] + b[3],b[1],b[2] + b[5]), screenIsoX(b[0] + b[3],b[1] + b[4]), screenIsoY(b[0] + b[3],b[1] + b[4],b[2] + b[5]));
}

public boolean inFrontOf(Entity e, int x, int y) {
	
	int cx;
	int cy;
	
	//#ifndef NOPARTICLES
	
	if(e.entityClass == Entity.CLASS_PARTICLE) {
			
		cx = ((e.rPosX >> 8) + Entity.STREET_WIDTH / 2) / Entity.TILE_WIDTH;
				
	} else {
		
	//#endif
			
		cx = (e.iBboxBotX + e.iBboxSizeX / 2 + Entity.STREET_WIDTH / 2) / Entity.TILE_WIDTH;
		
    //#ifndef NOPARTICLES
		
	}
	
	//#endif
	
	if (cx < x) {
		
		return false;
				
	} else if(cx == x) {
		
		//#ifndef NOPARTICLES
		
		if(e.entityClass == Entity.CLASS_PARTICLE) {
						
			cy = (e.rPosY >> 8) / Entity.TILE_WIDTH;
			
		} else {
			
		//#endif
					
			cy = (e.iBboxBotY + e.iBboxSizeY / 2) / Entity.TILE_WIDTH;
			
        //#ifndef NOPARTICLES
			
		}
		
		//#endif
		
		return cy > y;
		
	} else {
		
		return true;		
	}
	
}

public void renderEntity(Entity e){
	
	int ix, iy;
	
	switch(e.entityClass) {
	
		//#ifndef NONPCS
		case Entity.CLASS_NPC :
		//#endif
		case Entity.CLASS_PLAYER :
			
			//#ifndef NOSHADOWS			
			
				//#ifdef S80
				//#else
				drawEntityShadow(e, 10);
				//#endif
			
			//#endif
													
			//drawBoundingBox(e.iBbox, 0xffff00);
			
			if(e.entityState == Entity.PLAYER_STATE_TURBO) {
				
				if(e.entitySubclass == Entity.PLAYER_SUBCLASS_VASCO) {
					
					//#ifdef S60
					spriteDraw(miscImg, screenIsoX(e.iBboxBotX, e.iBboxBotY), screenIsoY(e.iBboxBotX, e.iBboxBotY, e.iBboxBotZ)-45, 8 + (e.ticks % 4), miscCor);
					//#elifdef S40
					//#elifdef S80
					//#endif
					
				}
				
//#ifndef BUILD_ONE_PLAYER				
				
				if(e.entitySubclass == Entity.PLAYER_SUBCLASS_CHICA) {
					
					//#ifdef S60
					spriteDraw(miscImg, screenIsoX(e.iBboxBotX, e.iBboxBotY)-20, screenIsoY(e.iBboxBotX, e.iBboxBotY, e.iBboxBotZ)-27, 8 + (e.ticks % 4), miscCor);
					//#elifdef S40
					//#elifdef S80
					//#endif
				}
				
				if(e.entitySubclass == Entity.PLAYER_SUBCLASS_TORERO) {
	
					//#ifdef S60
					spriteDraw(miscImg, screenIsoX(e.iBboxBotX, e.iBboxBotY)-2, screenIsoY(e.iBboxBotX, e.iBboxBotY, e.iBboxBotZ)-48, 8 + (e.ticks % 4), miscCor);
					//#elifdef S40
					//#elifdef S80
					//#endif
					
				}
//#endif
				
			}
			
			if(e.entityState == Entity.PLAYER_STATE_RIDING) {
								
				//#ifdef S60
				spriteDraw(miscImg, screenIsoX(e.iBboxBotX, e.iBboxBotY)-25, screenIsoY(e.iBboxBotX, e.iBboxBotY, e.iBboxBotZ)-19 , 72, miscCor);
				//#elifdef S40
				//#elifdef S80
				//#endif
			}		
			
			
			
			renderEntitySprite(e);
			
			break;
			
		case Entity.CLASS_BULL :	
			
			//drawBoundingBox(e.iBbox, 0x000000);
			
			renderEntitySprite(e);
			
			if(e.miscData[Entity.BULL_DATA_SHOWGLOBE] != 0) {
			
				ix = screenIsoX(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2);
				iy = screenIsoY(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2,  e.iBboxBotZ + e.iBboxSizeZ);
		
				//#ifdef S60
				spriteDraw(miscImg, ix - 22, iy - 47, 48, miscCor);
				//#elifdef S40
				//#elifdef S80
				//#endif
			}
			break;
			
		case Entity.CLASS_POOL :	
						
			//drawBoundingBox(e.iBbox, 0xffff00);
			ix = screenIsoX(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2);
			iy = screenIsoY(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2,  e.iBboxBotZ + e.iBboxSizeZ);
			
			//#ifdef S60
			spriteDraw(miscImg, ix - 22, iy - 22, 56 + (e.ticks >= 10 ? 4 : e.ticks / 2), miscCor);
			//#elifdef S40
			//#elifdef S80
			//#endif
			
									
			break;
			
        //#ifndef NOPARTICLES
			
		case Entity.CLASS_PARTICLE :
			
			ix = screenIsoX(e.rPosX>>8,e.rPosY>>8);
			iy = screenIsoY(e.rPosX>>8,e.rPosY>>8,e.rPosZ>>8);
			
			//#ifdef S60
			ix -= 22;
			iy -= 22;
			//#elifdef S40
			//#elifdef S80
			//#endif
						
			//#ifndef NOSHADOWS
															
			if(e.entitySubclass != Entity.PARTICLE_SUBCLASS_SMOKE && e.entitySubclass != Entity.PARTICLE_SUBCLASS_SMOKE2 && e.entitySubclass != Entity.PARTICLE_SUBCLASS_WATERDROP) {
			
				//#ifdef S80
				//#else
				drawEntityShadow(e, 6);
				//#endif				
			}
			
			//#endif
			
			switch(e.entitySubclass) {
			
				case Entity.PARTICLE_SUBCLASS_WOOD: 
					int frame = (1600 * e.ticks -  20 * (e.ticks * e.ticks)) / 1600;
									
					spriteDraw(miscImg, ix, iy, 64 + (frame % 4), miscCor);
					break;
					
				case Entity.PARTICLE_SUBCLASS_ITEMFADE:
					spriteDraw(miscImg, ix, iy, 32 + e.ticks / 2, miscCor);
					break;
					
				case Entity.PARTICLE_SUBCLASS_ROSEPLUS1:	
					//#ifdef S80
					//#else
					printSetArea(screenIsoX(e.rPosX>>8,e.rPosY>>8) - 8,screenIsoY(e.rPosX>>8,e.rPosY>>8,e.rPosZ>>8) - 8,16,16);
					//#endif
					//fontInit(FONT_SMALL_WHITE);
					printDraw("+1",0,0,0xffffff,PRINT_HCENTER|PRINT_VCENTER|PRINT_MASK);
					break;
					
				case Entity.PARTICLE_SUBCLASS_ROSEPLUS5:					
					//#ifdef S80
					//#else
					printSetArea(screenIsoX(e.rPosX>>8,e.rPosY>>8) - 8,screenIsoY(e.rPosX>>8,e.rPosY>>8,e.rPosZ>>8) - 8,16,16);
					//#endif
					//fontInit(FONT_SMALL_WHITE);
					printDraw("+5",0,0,0xffffff,PRINT_HCENTER|PRINT_VCENTER|PRINT_MASK);
					break;
				
				case Entity.PARTICLE_SUBCLASS_TURBO_ROCKET:
				case Entity.PARTICLE_SUBCLASS_CHUPINAZO:
					spriteDraw(miscImg, ix, iy, e.ticks % 4, miscCor);
					break;
												
				case Entity.PARTICLE_SUBCLASS_EXPLOSION:
					spriteDraw(miscImg, ix, iy, 40 + e.ticks / 3, miscCor);
					break;
					
				case Entity.PARTICLE_SUBCLASS_SMOKE:
					spriteDraw(miscImg, ix, iy, 16 + (e.ticks/2), miscCor);					
					break;
					
				case Entity.PARTICLE_SUBCLASS_SMOKE2:
					spriteDraw(miscImg, ix, iy, 24 + (e.ticks/2), miscCor);					
					break;
					
				case Entity.PARTICLE_SUBCLASS_WATERDROP:
					//scr.setClip(0,0,canvasWidth,canvasHeight);
					//putColor(0xffffff);
					fillDraw(0xffffff,screenIsoX(e.rPosX>>8,e.rPosY>>8) - 1,screenIsoY(e.rPosX>>8,e.rPosY>>8,e.rPosZ>>8) - 1,2,2);
					break;
					
			}
			break;			
			//	drawBoundingBox(entityList[i].iBbox, 0xff0000);
			//#endif
	}

}

//#ifndef NOSHADOWS

public void drawEntityShadow(Entity e, int radius) {
	
	int cx, cy;
	
	//#ifndef NOPARTICLES
	
	if (e.entityClass == Entity.CLASS_PARTICLE) {
		
		cx = screenIsoX(e.rPosX >> 8, e.rPosY >> 8);
		cy = screenIsoY(e.rPosX >> 8, e.rPosY >> 8, 0);
		
	} else {
		
	//#endif
		
		cx = screenIsoX(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2);
		cy = screenIsoY(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2, 0);
		
    //#ifndef NOPARTICLES
	}
	//#endif
	
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif
	
	putColor(0x000000);
	
	//#ifdef J2ME
	scr.fillArc(cx - radius / 2,
				cy - radius / 4,
				radius, radius / 2, 0, 360);
	//#endif
					
}

//#endif

public void renderEntitySprite(Entity e) {
			
	e.sprite.x = screenIsoX(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2);
	e.sprite.y = screenIsoY(e.iBboxBotX + e.iBboxSizeX / 2, e.iBboxBotY + e.iBboxSizeY / 2, e.iBboxBotZ);
	e.sprite.paint(scr);
	
}

public static void createPool(int x, int y) {

	Entity e;
	
	e = new Entity(Entity.CLASS_POOL);
			
	e.setPosition(x,y,-9);
			
	entityManagerAdd(e);		
	
}

//#ifndef NOPARTICLES

public static void createWoodParticles(int x, int y) {
	
	//#ifndef NOHEAVYPARTICLES	
	
	Entity e;
	
	for(int i = 0; i < 3; i++) {
		
		e = new Entity(Entity.CLASS_PARTICLE);
		e.entitySubclass = Entity.PARTICLE_SUBCLASS_WOOD;
		
		e.setPosition(x + 5*(i%2),y,1 + 5*(i/2));
			
		e.lifeTime = 40;
		e.rVelocityX = gc.rnd(2048) - 1024;
		e.rVelocityY = -gc.rnd(1024);
		e.rVelocityZ = gc.rnd(1536);
	
		entityManagerAdd(e);		
	}
	
	//#endif
}

public static void createItemfadeEffect(int x, int y, int type) {
		
	//#ifndef NOHEAVYPARTICLES
	
	Entity e;
				
	e = new Entity(Entity.CLASS_PARTICLE);
	e.entitySubclass = type;
		
	e.setPosition(x,y,1);
		
	e.lifeTime = 12;
	e.rVelocityX = 0;
	e.rVelocityY = 0;
	e.rVelocityZ = 1024;
	
	entityManagerAdd(e);
	
	//#endif
}

public static void createExplosion(int x, int y) {
	
	
	Entity e;
				
	e = new Entity(Entity.CLASS_PARTICLE);
	e.entitySubclass = Entity.PARTICLE_SUBCLASS_EXPLOSION;
		
	e.setPosition(x,y,1);
		
	e.lifeTime = 22;
	e.rVelocityX = 0;
	e.rVelocityY = 0;
	e.rVelocityZ = 0;
	
	entityManagerAdd(e);		
		
}

public static void createTurboEndingRocket(Entity p) {
	
		
	Entity e;
				
	e = new Entity(Entity.CLASS_PARTICLE);
	e.entitySubclass = Entity.PARTICLE_SUBCLASS_TURBO_ROCKET;
		
	e.setPosition(p.iBboxBotX + p.iBboxSizeX / 2,
				  p.iBboxBotY + p.iBboxSizeY / 2,
				  p.iBboxBotZ + p.iBboxSizeZ / 2);
						
	e.lifeTime = 40;
	
	e.rVelocityX = p.rVelocityX;
	e.rVelocityY = p.rVelocityY;
	e.rVelocityZ = 0;
	
	e.rAccelerationZ = 512;
	
	entityManagerAdd(e);		
			
}

public static void createSmoke(int x, int y, int z, int type) {

	//#ifndef NOHEAVYPARTICLES
	Entity e;
	
	e = new Entity(Entity.CLASS_PARTICLE);
	e.entitySubclass = type;
		
	e.setPosition(x,y,z);
		
	e.lifeTime = 15;
	e.rVelocityX = 0;
	e.rVelocityY = 0;
	e.rVelocityZ = 0;
	e.rAccelerationZ = 0;
		
	entityManagerAdd(e);		
	//#endif
}

public static void createWaterSplash(int x, int y) {

	//#ifndef NOHEAVYPARTICLES
	
	Entity e;
	
	e = new Entity(Entity.CLASS_PARTICLE);
	e.entitySubclass = Entity.PARTICLE_SUBCLASS_WATERDROP;
		
	e.setPosition(x,y,1);
		
	e.lifeTime = 10;
	e.rVelocityX = -128 - gc.rnd(256);
	e.rVelocityY = 0;
	e.rVelocityZ = 512;	
		
	entityManagerAdd(e);
	
	
	e = new Entity(Entity.CLASS_PARTICLE);
	e.entitySubclass = Entity.PARTICLE_SUBCLASS_WATERDROP;
		
	e.setPosition(x,y,1);
		
	e.lifeTime = 10;
	e.rVelocityX = 128 + gc.rnd(256);
	e.rVelocityY = 0;
	e.rVelocityZ = 512;	
		
	entityManagerAdd(e);
	
	//#endif
	
}

//#endif

public void hudDraw() {

	//#ifdef J2ME
	scr.setClip(0, 0, canvasWidth, canvasHeight);
	//#endif
	

	int vel = (runner.rVelocityY + 800) / 11;
	
	//System.out.println("Vel:"+runner.rVelocity[1]);
			
	if(vel < -26) {
		vel = -26;
	}
	if(vel > 0) {
		vel = 0;
	}

	// Distance map
	
	if(levelSelected != SURVIVAL_LEVEL && levelSelected != 0) {
	
		//#ifdef S60
		
		//#ifdef PRIMITIVEHUD
		//#else
						
		int barx = (canvasWidth - 176) / 2;
		int barStart = barx + 6;
		int barLength = 156;					
		int mapx;
		
		imageDraw(hudImg,0,0,176,13,barx,1);
		imageDraw(hudImg,160,12,16,8,barx+160,14);
		
		mapx = ((runner.iBboxBotY * (barLength))/(Entity.TILE_WIDTH * Entity.mapLength));
		
		imageDraw(hudImg, 0, 13, barLength - mapx, 4, barx + 6, 6);
					
				
		for(int i = 0; i < bull.length; i++) {
											
			//scr.setColor(0x000000);
			mapx = ((bull[i].iBboxBotY * (barLength))/(Entity.TILE_WIDTH * Entity.mapLength));
			imageDraw(hudImg, 153,40,13,11,barStart + barLength - mapx - 6, 3);
			
		}
		
		//scr.setColor(0xffff00);
		mapx = ((runner.iBboxBotY * (barLength))/(Entity.TILE_WIDTH * Entity.mapLength));
		
		switch(runner.entitySubclass) {
		
			case Entity.PLAYER_SUBCLASS_VASCO:
				imageDraw(hudImg, 113,40,11,11, barStart + barLength - mapx - 5, 3);
			break;
			
			case Entity.PLAYER_SUBCLASS_CHICA:
				imageDraw(hudImg, 125,41,14,10, barStart + barLength - mapx - 7, 3);
			break;
				
			case Entity.PLAYER_SUBCLASS_TORERO:
				imageDraw(hudImg, 141,41,10,10, barStart + barLength - mapx - 5, 3);
			break;
		
		}
		
		//#endif
		
		//#elifdef S40
		//#elifdef S80
		//#endif
											
	}
	
	// Turbo indicator
		
	if(runner.miscData[Entity.PLAYER_DATA_TURBOS] > 0) {
				
		//#ifdef S60
			//#ifdef PRIMITIVEHUD
			//#else
			imageDraw(hudImg,84,25,29,26,2,16);
			//#endif
		//#elifdef S40
		//#elifdef S80
		//#endif
				
	}
	
	//#ifdef S60
	
	//#ifdef PRIMITIVEHUD
	//#else
	
	// Speed 
	
	imageDraw(hudImg,3,17,81,33,canvasWidth - 81, canvasHeight - 36);
	
	spriteDraw(miscImg, canvasWidth - 30, canvasHeight - 34, 80 + playerSelected, miscCor);
	
	imageDraw(hudImg,0,19 + 25 + vel, 3, -vel + 1, canvasWidth - 38, canvasHeight - 34 + 25 + vel);
	
	//fontInit(FONT_SMALL_WHITE);
	
	printSetArea(0, 0, canvasWidth, canvasHeight);
	
	// Roses count
	
	printDraw(""+runner.miscData[Entity.PLAYER_DATA_ROSE_COUNT], canvasWidth - 76, canvasHeight - 18, 0xffffff, 0);
	
	//#endif
	
	//#elifdef S40
	//#elifdef S80
	//#endif
		
	//scr.fillRect(canvasWidth - 20, canvasHeight / 2 + 50 + vel, 10, - vel);
}


/*====================================
 * com.mygdx.mongojocs.sanfermines2006.Entity Manager - Engine
 * ===================================
 */

static Entity entityList[];
static int entityCount;

public static void entityManagerInit() {
	
	entityList = new Entity[100];
	entityCount = 0;
}

public static void entityManagerRelease() {
	
	entityList = null;
	entityCount = 0;
	
	System.gc();
}

public static void entityManagerAdd(Entity e) {
	
	if(entityCount < entityList.length) {
		
		entityList[entityCount] = e;
		entityCount++;
		
		// Insertion sorting
		
		int index = entityCount - 1;
				
		while(index > 0 && entityBehind(entityList[index], entityList[index - 1])) {
				
			Entity aux = entityList[index];
			entityList[index] = entityList[index - 1];
			entityList[index - 1] = aux;
				
			index--;
											
		}
		
	} else {
	
		//#ifdef com.mygdx.mongojocs.sanfermines2006.Debug
		Debug.println("com.mygdx.mongojocs.sanfermines2006.Entity Manager - Error : Too many Entities");
		//#endif
	}
		
}

public static void entityManagerUpdateAll() {
	
	int i = 0;
			
	while( i < entityCount) {
		
		entityList[i].doUpdate();
		
		if(entityList[i].expired) {
			
			// Delete entity
			
			for(int j = i; j < entityCount - 1; j++) {
				
				entityList[j] = entityList[j + 1];
								
			}
			
			entityList[entityCount - 1] = null;
			
			entityCount--;
															
		} else {
			
			i++;
		}				
	}
	
	// com.mygdx.mongojocs.sanfermines2006.Entity sorting
	
	for(i = 0; i < entityCount; i++) {
	
		int index = i;
		
									
		while(index > 0 && entityBehind(entityList[index], entityList[index - 1])) {
				
			Entity e = entityList[i];
			entityList[i] = entityList[index - 1];
			entityList[index - 1] = e;
				
			index--;
											
		}
		
		index = i;
				
		while(index < entityCount - 1 && !entityBehind(entityList[index], entityList[index + 1])) {
				
			Entity e = entityList[i];
			entityList[i] = entityList[index + 1];
			entityList[index + 1] = e;
				
			index++;
											
		}
	}
			
}

public static boolean entityBehind(Entity a, Entity b) {
	
	int x1, x2, y1, y2, z1, z2;
	
	//#ifndef NOZSORTING
	
	
	//#ifndef NOPARTICLES
	if(a.entityClass == Entity.CLASS_PARTICLE) {
		
		z1 = (a.rPosZ >> 8) / Entity.TILE_WIDTH;		
		
	} else {
	//#endif
		
		z1 = a.iBboxBotZ / Entity.TILE_WIDTH/* + a.iBboxSizeZ / 2*/;
		
    //#ifndef NOPARTICLES
	}
	//#endif
	
	//#ifndef NOPARTICLES
	if(b.entityClass == Entity.CLASS_PARTICLE) {
		
		z2 = (b.rPosZ >> 8) / Entity.TILE_WIDTH;
				
	} else {
	//#endif
		
		z2 = b.iBboxBotZ / Entity.TILE_WIDTH/* + b.iBboxSizeZ / 2*/;
		
    //#ifndef NOPARTICLES
	}
	//#endif
	
	if(z1 < z2) {
		
		return true;
		
	} else if(z1 > z2) {
		
		return false;
		
	} else {
		
	//#endif
	
		//#ifndef NOPARTICLES
		if(a.entityClass == Entity.CLASS_PARTICLE) {
			
			x1 = a.rPosX >> 8;		
			
		} else {
		//#endif
			
			x1 = a.iBboxBotX + a.iBboxSizeX / 2;
			
		//#ifndef NOPARTICLES
		}
		//#endif
		
		//#ifndef NOPARTICLES
		if(b.entityClass == Entity.CLASS_PARTICLE) {
			
			x2 = b.rPosX >> 8;
					
		} else {
		//#endif
			
			x2 = b.iBboxBotX + b.iBboxSizeX / 2;
			
        //#ifndef NOPARTICLES
		}
		//#endif
		
		if (x1 < x2) {
			
			return true;
			
		} else if(x1 == x2) {
			
			//#ifndef NOPARTICLES
			if(a.entityClass == Entity.CLASS_PARTICLE) {
				
				y1 = a.rPosY >> 8;
				
			} else {
			//#endif
				
				y1 = a.iBboxBotY + a.iBboxSizeY / 2;
				
			//#ifndef NOPARTICLES
			}
			//#endif
			
			//#ifndef NOPARTICLES
			if(b.entityClass == Entity.CLASS_PARTICLE) {
				
				y2 = b.rPosY >> 8;
				
			} else {
			//#endif
				
				y2 = b.iBboxBotY + b.iBboxSizeY / 2;
				
            //#ifndef NOPARTICLES
			}
			//#endif
			
			return y1 < y2;
			
		} else {
			
			return false;
		
		}
		
	//#ifndef NOZSORTING
	}
	//#endif
}





// ----------------------------------------------------------------------------



// David end ===========================================================================







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

	byte[] prefsData = updatePrefs(null);	// Recuperamos byte[] almacenado la ultima vez

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
		gameVibra = dis.readByte() != 0;

		gamePlayerNames = new String[dis.readInt()];
		for (int i=0 ; i<gamePlayerNames.length ; i++)
		{
			gamePlayerNames[i] = dis.readUTF();
		}

		gameWithHelp = dis.readByte();
		hiscore_scrollText = dis.readUTF();
		antiguedad = dis.readInt();
		
	//#ifndef HIDE_CONNECTIVITY
		userHashId = dis.readUTF();		// HASH ID del jugador
	//#endif

		gameMoreGamesVisited = dis.readByte() != 0;	// flag que nos indica si hemos visitado "Mas Juegos"
		
// ... a partir de aqui el resto de variables ...

		hiscore = dis.readInt();

		toreroLocked = dis.readByte() != 0;

		currentLevel = dis.readByte();
		playerSelected = dis.readByte();

		for (int i=0 ; i<levelPoints.length ; i++)
		{
			levelPoints[i] = dis.readInt();
		}

		for (int i=0 ; i<ferminesItem.length ; i++)
		{
			ferminesItem[i] = dis.readByte();
		}

		lastRankingPos = dis.readInt();

		scoreFirst = dis.readInt();
		int numPlayers = dis.readInt();
		scoreNick = new String[numPlayers];
		scoreFace = new int[numPlayers];
		scorePoints = new int[numPlayers];
		for (int i=0 ; i<numPlayers ; i++)
		{
			scoreNick[i] = dis.readUTF();
			scoreFace[i] = dis.readInt();
			scorePoints[i] = dis.readInt();
		}


		lastRankingPos = dis.readInt();
		rankingArrow = dis.readInt();
		updateRankingArrow = dis.readByte() != 0;
		tutorialViewed = dis.readByte() != 0;

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
		dos.writeByte(gameVibra? 1:0);

		dos.writeInt(gamePlayerNames.length);
		for (int i=0 ; i<gamePlayerNames.length ; i++)
		{
			dos.writeUTF(gamePlayerNames[i]);
		}

		dos.writeByte(gameWithHelp);
		dos.writeUTF(hiscore_scrollText);
		dos.writeInt(antiguedad);

	//#ifndef HIDE_CONNECTIVITY
		dos.writeUTF(userHashId);		// HASH ID del jugador
	//#endif

		dos.writeByte(gameMoreGamesVisited? 1:0);	// flag que nos indica si hemos visitado "Mas Juegos"

// ... a partir de aqui el resto de variables ...


		dos.writeInt(hiscore);

		dos.writeByte(toreroLocked?1:0);

		dos.writeByte(currentLevel);
		dos.writeByte(playerSelected);

		for (int i=0 ; i<levelPoints.length ; i++)
		{
			dos.writeInt(levelPoints[i]);
		}

		for (int i=0 ; i<ferminesItem.length ; i++)
		{
			dos.writeByte(ferminesItem[i]);
		}

		dos.writeInt(lastRankingPos);

		dos.writeInt(scoreFirst);
		dos.writeInt(scoreNick.length);
		for (int i=0 ; i<scoreNick.length ; i++)
		{
			dos.writeUTF(scoreNick[i]);
			dos.writeInt(scoreFace[i]);
			dos.writeInt(scorePoints[i]);
		}


		dos.writeInt(lastRankingPos);
		dos.writeInt(rankingArrow);
		dos.writeByte(updateRankingArrow? 1:0);
		dos.writeByte(tutorialViewed? 1:0);

// ===============================

	// Almacenamos las prefs
		byte[] prefsData = baos.toByteArray();
		byte checksum = 0; for (int i=1 ; i<prefsData.length ; i++) {checksum += prefsData[i];}
		prefsData[0] = checksum;
		updatePrefs(prefsData);		// Almacenamos byte[]

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
// cheats - Engine
// ===================
// *******************

//#ifdef CHEATS

// -------------------
// cheats
// ===================

public void cheats()
{

	if (keyMisc == 11 && lastKeyMisc == 0) {playExit = 1;}
	
	//#ifdef DEBUG
	//if (keyMisc == 7 && lastKeyMisc == 0) {showBoundingBoxes = !showBoundingBoxes;}
	//if (keyMisc == 9 && lastKeyMisc == 0) {showAnimSprites = !showAnimSprites;}
	//#endif
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







// Vasco AnimationConstans
public static final int PRUEBAS_VASCO_B0 = 0;
public static final int CORRER_B0_S0 = 0;
public static final int SALTAR_B0_S1 = 1;
public static final int CORNEADO_B0_S2 = 2;
public static final int PISADO_B0_S3 = 3;
public static final int QUIETO_B0_S4 = 4;
public static final int MOTO_B0_S5 = 5;
public static final int CELEBRACION_B0_S6 = 6;
public static final int SALTAR2_B0_S7 = 7;
public static final int CORRER_MIRAR_ATRAS_B0_S8 = 8;
public static final int TROPEZAR_B0_S9 = 9;
public static final int TURBO_PARTE1_B0_S10 = 10;
public static final int TURBO_PARTE2_B0_S11 = 11;
public static final int TURBO_PARTE3_B0_S12 = 12;
public static final int BOOT1_CORRER_B0_S13 = 13;
public static final int BOOT1_SALTAR_B0_S14 = 14;
public static final int BOOT1_CORNEADO_B0_S15 = 15;
public static final int BOOT1_PISADO_B0_S16 = 16;
public static final int BOOT2_CORRER_B0_S17 = 17;
public static final int BOOT2_SALTAR_B0_S18 = 18;
public static final int BOOT2_CORNEADO_B0_S19 = 19;
public static final int BOOT2_PISADO_B0_S20 = 20;
public static final int SINSKIN_B0_K0 = 0;



	
// Toro
	public static final int TORITO_B0 = 0;	
    //public static final int CORRER_B0_S0 = 0;
    public static final int ENVESTIR_B0_S1 = 1;
    public static final int CORNADA2_B0_S2 = 2;
    public static final int CORRER2_B0_S3 = 3;
    public static final int TOROVEJUNO_B0_S4 = 4;
    //public static final int SINSKIN_B0_K0 = 0;










// -------------------
// IDENTIFICADORES HANDSET, OPERADORA Y JUEGO
// ===================

// Esta es la id del juego
	final static int ID_GAME 	= 3001;

// La id de la operadora se carga desde el archivo de textos.
	int ID_CARRIER = 100;	// 100: Version Generica

	final static int ID_HANDSET = 1;//${handset.handset_ID};

// <=- <=- <=- <=- <=-

};