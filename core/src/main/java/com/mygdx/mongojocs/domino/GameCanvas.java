
package com.mygdx.mongojocs.domino;
// -----------------------------------------------
// Microjocs MagicCasual v1.0 Rev.0 (2.1.2006)
// ===============================================


//#define RMS_ENGINE

//#ifdef J2ME

	//#ifdef NOKIAUI
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
//#endif

	import java.io.ByteArrayInputStream;
	import java.io.ByteArrayOutputStream;
	import java.io.DataInputStream;
	import java.io.DataOutputStream;
	import java.io.InputStream;


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

	if (!biosPauseEnabled) {gameForceRefresh = true;}
}

public void hideNotify()
{
//#ifdef DEBUG
	Debug.println (" -+- hideNotify()");
//#endif

	if (!gameStarted) {return;}

	gamePaused = true;

	if (biosPauseEnabled) {biosPauseInit();}
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Variables para la gestion del MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int mainSleep = 65;					// 'milis' que han de transcurrir como minimo en una iteracion del bucle principal (un tick)
int gameSleep;						// Contiene los 'milis' que sobraron en la ultima iteracion del bucle principal
long gameMilis, CPU_Milis;			// Gestion del tiempo para el tiempo que dura una iteracion del bucle principal

boolean gameExit = false;			// Forzamos salir del MIDlet al finalizar el bucle principal
boolean gamePaused = false;			// Forzamos pausar el bucle principal del MIDlet
boolean gameForceRefresh = false;	// Forzamos que el bucle principal invoque a los metodos que refrescan el canvas
boolean gameForcePause = false;		// Forzamos que el bucle principal pase al estado de PAUSA
boolean gamePlayGoMenu = false;		// Este flag nos informa que durante el juego ha habido una interrupcion y debemos salir al menu 'IN_GAME'
int gameSoundOld = -1;				// Guardamos el sonido que esta reproduciendose en el momento de una interrupcion
int gameSoundLoop;					// Guardamos el loop del sonido que esta reproduciendose en el momento de una interrupcion
boolean gameStarted = false;		// Este flag nos indica que ya estamos dentro del bucle principal

static Graphics scr;				// Contiene siempre el objeto 'Graphics' con el que podemos dibujar en pantalla

//#ifdef NOKIAUI
	DirectGraphics dscr;			// objeto 'Graphics' para algunos terminales Nokia con caracteristicas adicionales
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para arrancar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void run()
{
	System.gc();
	biosCreate();	// Inicializamos MIDlet, antes de llegar al bucle principal
	System.gc();

	gameStarted = true;

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
// biso - Wait
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean biosHold;

public void biosWait()
{
	biosHold = true;
	while (biosHold)
	{
		biosLoop();
	}

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
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// biso - Wait
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void biosLoop()
{
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

			try {
			//#ifdef DEBUG
				Debug.spdStart(0);
			//#endif
				biosTick();
			//#ifdef DEBUG
				Debug.spdStop(0);
			//#endif

				if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}
				forceRender();

				soundTick();

			} catch (Exception e)
			{
			//#ifdef DEBUG
				Debug.println("** Excep Logic **");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}
		}

	// Controlamos los 'milis' transcurridos y hacemnos la espera adecuada para que todas las iteraciones del bucle principal tarden lo mismo.
		gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
		try	{Thread.sleep( gameSleep < 1? 1:gameSleep );} catch(Exception e) {}

//	System.gc();

}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Forzamos que se refresque la pantalla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void forceRender()
{
// Forzamos el renderizado del MIDlet
	canvasShow = true;
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
				e.printStackTrace();
			//#endif
			}

		//#ifdef DOJA
			ProgBarIMP();
		//#endif

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
boolean limpiaKeyMenu = false;

//#ifdef J2ME
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet lanza este metodo cuando se pulsa una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyPressed(int keycode)
{
//#ifdef FIX_SOFTKEY_RELEASE
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
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//#endif


//#ifdef J2ME
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//#endif


//#ifdef DOJA
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
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
static final int BIOS_LOGOS = 1;
static final int BIOS_MENU = 2;
static final int BIOS_POPUP = 3;
static final int BIOS_PAUSE = 4;
static final int BIOS_NONE = 5;

String[][] gameText;

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
			setFullScreenMode(true);
			try {Thread.sleep(500);} catch (Exception e) {}
			canvasWidth = getWidth();
			canvasHeight = getHeight();
		//#endif
	//#endif
//#endif
// ================================


// --------------------------------
// Inicializamos Debug Engine
// ================================
//#ifdef DEBUG
	Debug.debugCreate(this);
//#endif
// ================================


// --------------------------------
// Limpiamos canvas de blanco y mostramos imagen de loading (reloj de arena)
// ================================
	canvasFillTask(0xffffff);		// Color Blanco
	forceRender();

//#ifdef SI-x55
//#endif
		canvasFillTask(0xffffff);		// Color Blanco
		canvasImg = loadImage("/loading");
		forceRender();
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
// Movido a despues de preguntar si quieres juego con sonido
// ================================


// --------------------------------
// Cargamos archivo de textos generico
// ================================
	gameText = textosCreate( loadFile("/Textos.txt") );
// ================================


// --------------------------------
// Inicializamos tipo de FONT base a usar en el juego
// ================================
	printSetArea();

	printInit_Font(biosGetFont());

//	printInit_Font(loadImage("/font"), loadFile("/font.cor"), 14, 0x30, 1);
// ================================


// --------------------------------
// Inicializamos MIDlet a nivel se usuario
// ================================
	gameCreate();
// ================================

}


// -------------------
// bios Pause Init
// ===================

boolean biosPauseEnabled = true;

public void biosPauseInit()
{
	if (biosStatus != BIOS_PAUSE)
	{
		gameSoundOld = soundOld;
		gameSoundLoop = soundLoop;
		soundStop();

		biosPauseSoftkeyLeft = listenerIdLeft;
		biosPauseSoftkeyRight = listenerIdRight;
		biosPauseState = biosStatus;
		biosStatus = BIOS_PAUSE;
		listenerInit(SOFTKEY_NONE, SOFTKEY_CONTINUE);
	}
}


// -------------------
// bios Tick
// ===================

public void biosTick()
{

//#ifdef DEBUG
	if (keyMisc == 11 && lastKeyMisc == 0) {biosPauseInit();}
	if (keyMisc == 12 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; if (!Debug.enabled) {gameForceRefresh = true;}}
//#endif

// Gestionamos el Thread de conexion
	connectTick();

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
		if ( logosTick() ) {biosHold = false; biosStatus = BIOS_GAME;}
	return;
// --------------------

// --------------------
// menu Bucle
// --------------------
	case BIOS_MENU:
		if ( menuTick() ) {biosHold = false; biosStatus = BIOS_GAME;}
	return;
// --------------------

// --------------------
// popup Bucle
// --------------------
	case BIOS_POPUP:
		if ( popupTick() ) {biosHold = false; biosStatus = biosStatusOld;}
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
			if (gameSoundOld >= 0 && gameSoundLoop==0) {soundPlay(gameSoundOld,gameSoundLoop);}
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
	Debug.println ("biosStatus="+biosStatus);
	Debug.println ("gameStatus="+gameStatus);
	Debug.fillYellow = true;
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

	biosStatusOld = biosStatus;
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
// print - Engine - Rev.1 (3.2.2006)
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
							letra = 0x61 - printFont_Min;
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

	//#ifdef SI-x55
	if (destX < 0) {sourX += -destX; sizeX += destX; destX=0;}
	if (destY < 0) {sourY += -destY; sizeY += destY; destY=0;}
	//#endif

//	scr.setClip(destX, destY, sizeX, sizeY);

	if ((mode & PRINT_MASK) == 0)
	{
		scr.setClip(destX, destY, sizeX, sizeY);
	} else {
		scr.setClip(printX, printY, printWidth, printHeight);
		scr.clipRect(destX, destY, sizeX, sizeY);
	}

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

//#ifdef DEBUG
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
    LastLineColor = rgb;
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
	if (DestX >= canvasWidth || DestY >= canvasHeight) {return;}
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

public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor, int trozos)
{
	frame*=6*trozos;

	for (int i=0 ; i<trozos ; i++)
	{
		int destX=cor[frame++];
		int destY=cor[frame++];
		int sizeX=cor[frame++];
		if (sizeX!=0)
		{
			int sizeY=cor[frame++];
			int flip=0;
			if (sizeX < 0) {sizeX*=-1; flip|=1;}
			if (sizeY < 0) {sizeY*=-1; flip|=2;}
			int sourX=cor[frame++] + 128;
			int sourY=cor[frame++] + 128;

			imageDraw(img, sourX, sourY,  sizeX, sizeY,  x+destX, y+destY,  flip,  0 ,0, canvasWidth, canvasHeight);
		} else {
			frame+=3;
		}
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
	printDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
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
					soundTracks[soundForceRestart].start();
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

static final int SOFTKEY_MAS 			= 11;
static final int SOFTKEY_CERRAR 	= 12;

static final int SOFTKEY_LANZAR		= 13;
static final int SOFTKEY_ROBAR		= 14;
static final int SOFTKEY_PASAR		= 15;

static final int SOFTKEY_JUGAR		= 16;

static final int SOFTKEY_EMPEZAR	= 17;
static final int SOFTKEY_ACTUALIZAR	= 18;
static final int SOFTKEY_INTRODUCIR	= 19;


static final int LISTENER_BAR_RGB = 0x7b948c;

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
static final int listenerHeight = 12 + 2;
//#elifdef S80
//#endif

boolean listenerShow = false;

public void listenerInit(int idLeft, int idRight)
{
	listenerIdLeft = idLeft;
	listenerIdRight = idRight;

	listenerShow = true;
}

public void listenerInit(String strLeft, String strRight) {}

public void listenerDraw()
{
	if (!listenerShow) {return;} else {listenerShow = false;}

	formFontInit(FORM_FONT_SMALL_WHITE);

	printSetArea(0, canvasHeight, canvasWidth, listenerHeight);
	fillDraw(LISTENER_BAR_RGB, printX, printY, printWidth, printHeight);

	canvasHeight += listenerHeight;
	try
	{
	//#ifndef LISTENER_SWAP
		printDraw(gameText[TEXT_SOFTKEYS][listenerIdLeft], 1, 0, formFontPenRGB, PRINT_LEFT|PRINT_BOTTOM);
		printDraw(gameText[TEXT_SOFTKEYS][listenerIdRight], -1, 0, formFontPenRGB, PRINT_RIGHT|PRINT_BOTTOM);
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

	forceRender();
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








// *******************
// -------------------
// form - Engine
// ===================
// *******************

static final int FORM_FONT_SMALL_WHITE 	= 0;
static final int FORM_FONT_SMALL_BLACK 	= 1;
static final int FORM_FONT_SMALL_RED		= 2;
static final int FORM_FONT_LARGE				= 3;


static final int FORM_SLIDER_WIDTH = 4;

static final int FORM_MORE_GAMES_ASPA_WIDTH = 5;


static final int FORM_COLOR_BACKGROUND = 0xffffff;	// Color de fondo para los formularios
static final int FORM_COLOR_MARC = 0xff9C39;		// Color del marco
static final int FORM_COLOR_BOX = 0x946342;			// Color de la caja normal
static final int FORM_COLOR_BOX_SELECTED = 0xffefde;// Color de la caja seleccionada
static final int FORM_COLOR_BOX_DISABLED = 0x7b948c;// Color de la caja oscura

//#ifdef S40
//#elifdef S60
static final int FORM_SEPARATOR = 4;

static final int FORM_MARC_WIDTH = 17;
static final int FORM_MARC_HEIGHT = 17;
static final int FORM_MARC_BORDER = 8;

//#elifdef S80
//#endif


static final int FORM_EDIT_MAX_CHARS = 10;	// Numero maximo de letras para la edicion del nombre del jugador



static final int FORM_NONE = 0;
static final int FORM_OPTION = 1;
static final int FORM_LIST = 2;
static final int FORM_EDIT_NAME = 3;
static final int FORM_MORE_GAMES = 4;


/*
static final int FORM_ARROW_BOX_LEFT = 0x06;
static final int FORM_ARROW_BOX_RIGHT = 0x04;
static final int FORM_ARROW_BOX_UP = 0x02;
static final int FORM_ARROW_BOX_DOWN = 0x00;
*/
static final int FORM_ARROW_LEFT = 0x08;
static final int FORM_ARROW_RIGHT = 0x09;
static final int FORM_ITEM_TITLE = 0x0A;
static final int FORM_ITEM_MORE = 0x0B;



static final int FORM_RENDER_BASIC = 0;		// Renderiza una linea basica.
static final int FORM_RENDER_NUMBER = 1;	// Renderiza una linea basica, pero los numeros los muestra de otro color
static final int FORM_RENDER_INFO = 2;		// Renderiza una linea basica str[0] y a continuacion la info de otro color str[1]
static final int FORM_RENDER_SETTING = 3;	// Renderiza un numero str[0] y un texto str[1] sobre una "barra de progreso"



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

int formListEnterHeight;


byte[] formFontSmallBlackCor;
byte[] formFontSmallWhiteCor;
byte[] formFontSmallRedCor;
byte[] formFontLargeCor;
//#ifdef J2ME
	Image formFontSmallBlackImg;
	Image formFontSmallWhiteImg;
	Image formFontSmallRedImg;
	Image formFontLargeImg;
//#elifdef DOJA
//#endif

boolean formVslider;
int formVsliderX;
int formVsliderY;
int formVsliderWidth;
int formVsliderHeight;

int formPiliItem = -1;

boolean formRunning = false;
boolean formShow = false;

// -------------------
// form Create
// ===================

Image formBodyImg;

public void formCreate()
{
	//MONGOFIX formBodyImg = loadImage("/formBody");
	formBodyImg = loadImage("/formLogo");


	formItemsCor = loadFile("/formItems.cor");
	formFontSmallBlackCor = loadFile("/fontSmallBlack.cor");
	formFontSmallWhiteCor = loadFile("/fontSmallWhite.cor");
	formFontSmallRedCor 	= loadFile("/fontSmallRed.cor");
	formFontLargeCor = loadFile("/fontLarge.cor");

	//#ifdef J2ME
		formItemsImg = loadImage("/formItems");
		formFontSmallBlackImg = loadImage("/fontSmallBlack");
		formFontSmallWhiteImg = loadImage("/fontSmallWhite");
		formFontSmallRedImg 	= loadImage("/fontSmallRed");
		formFontLargeImg = loadImage("/fontLarge");
	//#elifdef DOJA
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
//formPili
	formPiliItem = 1;	// -1: SIN IMAGEN, esta hackeado, para que siempre muestre la "1" si no le dices lo contrario ^_^!
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
	int separatorsHeight = 0;


// Inicializamos variables de sliders
	formVslider = false;
	formVsliderY = 0;


// formLogo: Si hay lotopito, calculamos sus datos
	if (formLogoImg != null)
	{
		formLogoHeight = formLogoImg.getHeight();
	}

	separatorsHeight += FORM_SEPARATOR;


// formTitle: Si hay titula, calculamos sus datos
	if (formTitleStr != null)
	{
		formFontInit(FORM_FONT_LARGE);

		formTitleWidth = canvasWidth - (FORM_SEPARATOR * 2);
		formTitleHeight = printGetHeight() + FORM_SEPARATOR;

		formTitleX = (canvasWidth - formTitleWidth) / 2;
		formTitleY = formLogoHeight + separatorsHeight;

		separatorsHeight += FORM_SEPARATOR;
	}


// formBody: Ajustamos el eje y tama�o destinado al BODY
	formBodyWidth = canvasWidth - ((FORM_SEPARATOR + FORM_MARC_BORDER) * 2);
	formBodyX = (canvasWidth - formBodyWidth) / 2;
	formBodyY = formLogoHeight + formTitleHeight + separatorsHeight + FORM_MARC_BORDER;
	formBodyHeight = canvasHeight - formBodyY - FORM_SEPARATOR - FORM_MARC_BORDER;


// formText
	if (formTextStr != null)
	{
		formFontInit(FORM_FONT_LARGE);

		formTextHeight = (formTextStr.length * printGetHeight()) + FORM_SEPARATOR;
		formTextWidth = formBodyWidth - (FORM_SEPARATOR * 2);
		formTextX = formBodyX + FORM_SEPARATOR;
		formTextY = formBodyY + FORM_SEPARATOR;
	}


// Calculamos area libre para el Body
	int tmpHeight = formBodyHeight - formTextHeight - (FORM_SEPARATOR * 2);
	int tmpY = formBodyY + FORM_SEPARATOR + formTextHeight;


// Flag para activar desbordamiento del espacio del Body
	boolean bodyOverflow = false;


// Representamos tipo de formulario
	switch (formBodyMode)
	{
	case FORM_OPTION:

		formFontInit(FORM_FONT_SMALL_BLACK);

		formListEnterHeight = printGetHeight() + (FORM_SEPARATOR * 2);	// Salto de linea entre la (x,y) de una caja a la siguiente.


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


		formBoxWidth = textoSizeX + (FORM_SEPARATOR * 2) + (optionCicle? FORM_SEPARATOR + (printSpriteWidth(formItemsCor, FORM_ARROW_LEFT) * 2) : 0);
		if (formBoxWidth < formBodyWidth - (formBodyWidth / 3)) {formBoxWidth = formBodyWidth - (formBodyWidth / 3);}

		formBoxHeight = printGetHeight() + FORM_SEPARATOR;
		formBoxX = formBodyX + ((formBodyWidth - formBoxWidth) / 2);


	// Calculamos cuantas lineas de texto nos entran en pantalla
		formListView = tmpHeight / formListEnterHeight;	// lineas visibles en lo que queda de pantalla
		if (formListView > 3) {formListView--;}


	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
		} else {
/*
			int height = tmpHeight - ((FORM_SEPARATOR + printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP)) * 2);	// Reservamos area para dos aspas (arriba y abajo)
			formListView = height / formListEnterHeight;	// lineas visibles en lo que queda de pantalla
*/
			formVslider = true;
/*
			formVsliderX = formBodyX + formBodyWidth;
			formVsliderY = tmpY;
			formVsliderHeight = formBodyHeight - (tmpY - formBodyY);
*/
		}

		formBoxY = tmpY + (tmpHeight - (formListView * formListEnterHeight) + FORM_SEPARATOR) / 2;

	// Ajustamos coordenadas del slider
		formVsliderX = formBoxX + formBoxWidth + FORM_SLIDER_WIDTH;
		formVsliderY = formBoxY;
		formVsliderHeight = (formListView * (formBoxHeight + FORM_SEPARATOR)) - FORM_SEPARATOR;

	// Si la primera opcion es "disabled" saltamos a la opcion 2
		if ( formListPos == 0 && (formListDat[0][0] & 0x10) != 0 ) {formListPos++;}
	break;


	case FORM_LIST:

		formFontInit(FORM_FONT_SMALL_BLACK);

		formListEnterHeight = printGetHeight();	// Salto de linea una linea y la siguiente.

		formBoxWidth = formBodyWidth - (FORM_SEPARATOR);
		formBoxX = formBodyX + (FORM_SEPARATOR / 2);

	// Cortamos el texto si se sale de la pantalla
		int cnt = 0;
		for (int i=0 ; i<formListStr.length ; i++)
		{
			formListStr[i] = printTextBreak(formListStr[i][0], formBoxWidth - FORM_SEPARATOR);
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
		formListView = (tmpHeight - (FORM_SEPARATOR)) / formListEnterHeight;	// lineas visibles en lo que queda de pantalla

	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
		} else {
//			int height = tmpHeight - (FORM_SEPARATOR * 2) - ((FORM_SEPARATOR + printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP)) * 2);	// Reservamos area para dos aspas (arriba y abajo)
//			formListView = height / formListEnterHeight;	// lineas visibles en lo que queda de pantalla

			formVslider = true;
		}

		formBoxHeight = (formListView * formListEnterHeight) + (FORM_SEPARATOR * 2);

		formBoxY = tmpY + (tmpHeight - formBoxHeight) / 2;

		if (formVslider)
		{
			formVsliderX = formBodyX + formBodyWidth;
			formVsliderY = formBoxY;
			formVsliderHeight = formBoxHeight;
		}
	break;


	case FORM_EDIT_NAME:

		formFontInit(FORM_FONT_SMALL_BLACK);

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
		formBoxY = tmpY + FORM_SEPARATOR;

	// Calculamos tama�o y posicion de la rejilla de letras
		formCellWidth = formMaxCharWidth + 3;
		formCellHeight = printGetHeight();

		formCellNumWidth = (formBodyWidth - FORM_SEPARATOR * 2) / formCellWidth;
		formCellNumHeight = (gameText[TEXT_ABECEDARIO][0].length() / formCellNumWidth);
		if ((formCellNumWidth * formCellNumHeight) < gameText[TEXT_ABECEDARIO][0].length()) {formCellNumHeight++;}

		formCellX = ((formBodyWidth - (formCellNumWidth * formCellWidth)) / 2) + formBodyX;
		formCellY = formBoxY + formBoxHeight + FORM_SEPARATOR;

		formEditStr = "";

	// Miramos si nos hemos salido del body
		bodyOverflow = formCellY + ((formCellNumHeight + 1) * formCellHeight) + FORM_SEPARATOR > canvasHeight;
	break;


	case FORM_MORE_GAMES:

		formFontInit(FORM_FONT_SMALL_BLACK);
		formPubliSize = printStringWidth(formPubliStr);
		formPubliScrollX = formBodyWidth + (FORM_MARC_BORDER * 2);
	break;
	}


// Si hay desbordamiento del Body, eliminamos logotipo
	if (bodyOverflow && formLogoImg != null)
	{
		formLogoImg = null;
		formLogoHeight = 0;
		formInit(mode, pos, actionBack, actionAcept);
	}


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

// release para formPubliInit()
	formPubliImg = null;
	formPubliStr = null;

	formPiliItem = -1;

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


// formOption
	switch (formBodyMode)
	{
	case FORM_OPTION:

	// Controlamos desplazamiento vertical
		if (keyY > 0 && lastKeyY == 0 && formListPos < formListStr.length-1) {formListPos++; formShow = true;}
		else
		if (keyY < 0 && lastKeyY == 0 && formListPos > 0) {formListPos--; formShow = true;}

	// Controlamos si hay que hacer scroll vertical
		if (formListIni > formListPos) {formListIni = formListPos;}
		else
		if (formListIni < formListPos-formListView+1) {formListIni = formListPos-formListView+1;}

	// Si pisamos una opcion tipo titulo, saltamos a la siguiente opcion
		if ( (formListDat[formListPos][0] & 0x10) != 0 )
		{
			if (keyY < 0) {if (formListPos >= 1) {formListPos--;} else {formListPos++;}}

			if (keyY > 0) {if (formListPos < formListStr.length-2) {formListPos++;} else {formListPos--;}}
		}

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

			if (posY < 0) {posY = formCellNumHeight - 1;}
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
				if (formListPos < ((formCellNumWidth*formCellNumHeight)+(formCellNumWidth/2)))
				{
				// Hemos pulsado "Borrar" Borramos ultima letra, en el caso de que haya como minimo una.
					if (formEditStr.length() > 0) {formEditStr = formEditStr.substring(0, formEditStr.length()-1);}
				} else {
				// Hemos pulsado "Aceptar"
					if (formEditStr.length() >0)
					{
						gamePlayerNames[menuEditPlayerSelected] = formEditStr;
					}

					formListCMD = MENU_ACTION_BACK;
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

	break;


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


	// Controlamos el scroll horizontal de texto
		if (formPubliScrollX-- < -formPubliSize) {formPubliScrollX = formBodyWidth + (FORM_MARC_BORDER * 2);}
		formPubliScrollShow = true;
	break;
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
		if (formPubliScrollShow) {formPubliScrollShow = false; formPubliScrollDraw();}

		return;
	}
	formShow = false;

// Limpiamos el canvas con el color correcto
	fillDraw(FORM_COLOR_BACKGROUND, 0,0, canvasWidth, canvasHeight);


// formLogo: Renderizamos logotipo del formulario
	if (formLogoImg != null)
	{
		imageDraw(formLogoImg, 0,0);
	}

/*
// formTitle: Renderizamos Titulo del formulario
	if (formTitleStr != null)
	{
		formFontInit(FORM_FONT_LARGE);

		printSetArea(formTitleX, formTitleY, formTitleWidth, formTitleHeight);

		fillDraw(FORM_COLOR_BOX, formTitleX, formTitleY, formTitleWidth, formTitleHeight);
		formMarcDraw(formTitleX, formTitleY, formTitleWidth, formTitleHeight);

		int contentX = (printWidth - (printSpriteWidth(formItemsCor, FORM_ITEM_TITLE) + FORM_SEPARATOR + printStringWidth(formTitleStr)))/2;
		printDraw(formTitleStr, contentX + FORM_SEPARATOR + printSpriteWidth(formItemsCor, FORM_ITEM_TITLE), 0, 0xff0000, PRINT_VCENTER);
		printDraw(formItemsImg, contentX, 0, FORM_ITEM_TITLE, PRINT_VCENTER, formItemsCor);
	}
*/



// formBody: Image
//	fillDraw(0xffffff, formBodyX, formBodyY, formBodyWidth, formBodyHeight);
	if (formBodyImg != null)
	{
		imageDraw(formBodyImg,  0, 0, formBodyWidth, formBodyHeight,  0, 0,  formBodyX, formBodyY, formBodyWidth, formBodyHeight);
	}
//	formMarcDraw(formBodyX, formBodyY, formBodyWidth, formBodyHeight);

	int esquinaFrameBase = 0;
	int rgbBack = FORM_COLOR_MARC;
	if (formBodyMode == FORM_LIST || formBodyMode == FORM_EDIT_NAME || formBodyMode == FORM_NONE)
	{
		esquinaFrameBase = 4;
		rgbBack = FORM_COLOR_BOX;
	}

	printSetArea(formBodyX - FORM_MARC_BORDER, formBodyY - FORM_MARC_BORDER, formBodyWidth + (FORM_MARC_BORDER * 2), formBodyHeight + (FORM_MARC_BORDER * 2) );

	fillDraw(rgbBack, printX + FORM_MARC_WIDTH, printY, printWidth - (FORM_MARC_WIDTH * 2), printHeight);
	fillDraw(rgbBack, printX, printY + FORM_MARC_HEIGHT, printWidth, printHeight - (FORM_MARC_HEIGHT * 2));

	printDraw(formItemsImg, 0, 0, esquinaFrameBase, PRINT_LEFT|PRINT_TOP, formItemsCor);
//	printDraw(formItemsImg, 0, 0, esquinaFrameBase+1, PRINT_RIGHT|PRINT_TOP, formItemsCor);
	fillDraw(rgbBack, printX + printWidth - FORM_MARC_WIDTH, printY, FORM_MARC_WIDTH, FORM_MARC_HEIGHT);
	printDraw(formItemsImg, 0, 0, esquinaFrameBase+2, PRINT_LEFT|PRINT_BOTTOM, formItemsCor);
	printDraw(formItemsImg, 0, 0, esquinaFrameBase+3, PRINT_RIGHT|PRINT_BOTTOM, formItemsCor);


// Renderizamos pili
	if (formPiliItem >= 0)
	{
		piliDraw(printX + (printWidth - 144)/2, printY + (printHeight - 124)/2, formPiliItem);
	}



// formText
	if (formTextStr != null)
	{
		formFontInit(FORM_FONT_LARGE);

		printSetArea(formTextX, formTextY, formTextWidth, formTextHeight);
//		alphaFillDraw(0xAA000000|FORM_COLOR_BOX_SELECTED, formTextX, formTextY, formTextWidth, formTextHeight);
//		fillDraw(FORM_COLOR_BOX_SELECTED, formTextX, formTextY, formTextWidth, formTextHeight);
//		formMarcDraw(formTextX, formTextY, formTextWidth, formTextHeight);
		fillDraw(0xffffff, formTextX, formTextY+formTextHeight-1, formTextWidth, 1);
		printDraw(formTextStr, FORM_SEPARATOR + printSpriteWidth(formItemsCor, FORM_ITEM_TITLE), 0, formFontPenRGB, PRINT_LEFT|PRINT_VCENTER);
		printDraw(formItemsImg, 0, 0, FORM_ITEM_TITLE, PRINT_LEFT|PRINT_VCENTER, formItemsCor);
	}






// formBodyMode
	switch (formBodyMode)
	{
	case FORM_OPTION:

	// Controlamos si hay que hacer scroll vertical
		if (formListIni > formListPos) {formListIni = formListPos;}
		else
		if (formListIni < formListPos-formListView+1) {formListIni = formListPos-formListView+1;}

	// Pintamos todas las opciones del menu
		for (int i=0 ; i<formListView ; i++)
		{
			int addY = i * (formBoxHeight + FORM_SEPARATOR);

		// Pintamos caja de la opcion con el fondo correcto (opcion seleccionada, normal o desactivada)
			int rgb = FORM_COLOR_BOX;
			if (formListIni + i == formListPos) {rgb = FORM_COLOR_BOX_SELECTED;}
			else
			if ( (formListDat[formListIni+i][0] & 0x01) != 0 ) {rgb = FORM_COLOR_BOX_DISABLED;}
//			alphaFillDraw(0xAA000000|rgb, formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
			fillDraw(rgb, formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
//			formMarcDraw(formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);

		// Cortamos texto para que no se salga de la pantalla
			formFontInit( (rgb == FORM_COLOR_BOX? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK) );
			String str = printTextCut(formListStr[formListIni+i][formListDat[formListIni+i][2]], formBoxWidth);

		// Pintamos texto ya cortado
			printSetArea(formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
			printDraw(str, 0, 0, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);

		// Pintamos aspas horizontales si es necesario
			if (formListIni + i == formListPos && formListStr[formListIni+i].length > 1)
			{
				printDraw(formItemsImg,  4, 0, FORM_ARROW_LEFT,  PRINT_VCENTER|PRINT_LEFT,  formItemsCor);
				printDraw(formItemsImg, -4, 0, FORM_ARROW_RIGHT, PRINT_VCENTER|PRINT_RIGHT, formItemsCor);
			}
		}
/*
	// Si hay mas lineas de texto que el area para mostrarlas, dibujamos las aspas de scroll
		if (formListView < formListStr.length)
		{
			printSetArea(formBoxX, formBoxY, formBoxWidth, formListView*(formBoxHeight+FORM_SEPARATOR));
			printDraw(formItemsImg, 0, -FORM_SEPARATOR -printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP), FORM_ARROW_BOX_UP + (formListIni > 0? 0:1),  PRINT_HCENTER,  formItemsCor);
			printDraw(formItemsImg, 0, printSpriteHeight(formItemsCor, FORM_ARROW_BOX_DOWN), FORM_ARROW_BOX_DOWN + (formListIni < formListStr.length - formListView? 0:1),  PRINT_HCENTER|PRINT_BOTTOM,  formItemsCor);
		}
*/

		if (formVslider)
		{
			sliderVDraw(SLIDER_COLOR_BLANCO, SLIDER_COLOR_NARANJA, formListPos, formListStr.length - 1, formVsliderX, formVsliderY, formVsliderHeight);
		}
	break;


	case FORM_LIST:
//		alphaFillDraw(0x88000000|FORM_COLOR_BOX, formBoxX, formBoxY, formBoxWidth, formBoxHeight);
//		fillDraw(FORM_COLOR_BOX, formBoxX, formBoxY, formBoxWidth, formBoxHeight);
//		formMarcDraw(formBoxX, formBoxY, formBoxWidth, formBoxHeight);

	// Pintamos todas las lineas del listado
		printSetArea(formBoxX + (FORM_SEPARATOR / 2), formBoxY + FORM_SEPARATOR, formBoxWidth - FORM_SEPARATOR - (formVslider? FORM_SLIDER_WIDTH : 0), formBoxHeight - (FORM_SEPARATOR*2));
		formFontInit(FORM_FONT_SMALL_WHITE);
		for (int i=0 ; i<formListView ; i++)
		{
		// Cortamos texto para que no se salga de la pantalla
			String str = printTextCut(formListStr[formListIni+i][formListDat[formListIni+i][2]], formBoxWidth);

		// Pintamos texto ya cortado
			printDraw(str, 1, i*formListEnterHeight, formFontPenRGB, (formListView==1? PRINT_HCENTER : PRINT_LEFT));
		}

	// Si hay mas lineas de texto que el area para mostrarlas, dibujamos las aspas de scroll
		if (formListView < formListStr.length)
		{
			printSetArea(formBoxX , formBoxY , formBoxWidth - FORM_SLIDER_WIDTH, formBoxHeight);
/*
			printDraw(formItemsImg, 0, -FORM_SEPARATOR - printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP), FORM_ARROW_BOX_UP + (formListIni > 0? 0:1),  PRINT_HCENTER,  formItemsCor);
			printDraw(formItemsImg, 0, FORM_SEPARATOR + printSpriteHeight(formItemsCor, FORM_ARROW_BOX_DOWN), FORM_ARROW_BOX_DOWN + (formListIni < formListStr.length - formListView? 0:1),  PRINT_HCENTER|PRINT_BOTTOM,  formItemsCor);
*/
		// Imprimimos los "3 puntos suspensivos" si falta texto por mostrar
			if (formListIni < formListStr.length - formListView)
			{
				printDraw(formItemsImg, -FORM_SEPARATOR, -FORM_SEPARATOR/2, FORM_ITEM_MORE, PRINT_RIGHT|PRINT_BOTTOM, formItemsCor);
			}
		}

		if (formVslider)
		{
			sliderVDraw(SLIDER_COLOR_BLANCO, SLIDER_COLOR_MARRON, formListIni, formListStr.length - formListView, formVsliderX, formVsliderY, formVsliderHeight);
		}
	break;


	case FORM_EDIT_NAME:

	// Renderizamos caja donde editamos nombre
		formFontInit(FORM_FONT_SMALL_WHITE);

//		alphaFillDraw(0xAA000000|FORM_COLOR_BOX, formBoxX, formBoxY, formBoxWidth, formBoxHeight);
		fillDraw(FORM_COLOR_BOX_DISABLED, formBoxX, formBoxY, formBoxWidth, formBoxHeight);
		formMarcDraw(formBoxX, formBoxY, formBoxWidth, formBoxHeight);

		for (int i=0 ; i<formEditStr.length() ; i++)
		{
			printSetArea(formBoxX + FORM_SEPARATOR + (i * formMaxCharWidth), formBoxY, formMaxCharWidth, formBoxHeight);
			printDraw(formEditStr.substring(i,i+1), 0, 1, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		}

	// Renderizamos celdas "ABCD..."
		for (int cnt=0, y=0; y<formCellNumHeight ; y++)
		{
			for (int x=0; x<formCellNumWidth ; x++)
			{
				int addX = x*formCellWidth;
				int addY = y*formCellHeight;
				printSetArea(formCellX+addX, formCellY+addY, formCellWidth, formCellHeight);
				int rgb = (cnt == formListPos? FORM_COLOR_MARC:FORM_COLOR_BOX_DISABLED);
//				alphaFillDraw(0xAA000000|rgb, formCellX+addX, formCellY+addY, formCellWidth, formCellHeight);
				fillDraw(rgb, formCellX+addX, formCellY+addY, formCellWidth, formCellHeight);
				if (cnt < gameText[TEXT_ABECEDARIO][0].length())
				{
					formFontInit( cnt != formListPos? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK);
					printDraw(gameText[TEXT_ABECEDARIO][0].substring(cnt++,cnt), 1,1, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
				} else {
					cnt++;
				}
				formMarcDraw(formCellX+addX, formCellY+addY, formCellWidth+1, formCellHeight+1);
			}
		}

	// Miramos si "Borrar" o "Aceptar" estan seleccionados
		boolean eraseBox = false;
		boolean aceptBox = false;

		if (formListPos >= (formCellNumWidth*formCellNumHeight))
		{
			if (formListPos < ((formCellNumWidth*formCellNumHeight)+(formCellNumWidth/2))) {eraseBox = true;} else {aceptBox = true;}
		}

	// Renderizamos celda "Borrar"
		printSetArea(formCellX, formCellY + (formCellNumHeight * formCellHeight), (formCellNumWidth * formCellWidth) / 2, formCellHeight);
		int rgb = ( eraseBox? FORM_COLOR_BOX_SELECTED:FORM_COLOR_BOX_DISABLED);
//		alphaFillDraw(0xAA000000|rgb, printX, printY, printWidth, printHeight);
		fillDraw(rgb, printX, printY, printWidth, printHeight);
		formFontInit( !eraseBox? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK);
		printDraw(gameText[TEXT_BORRAR][0], 0,1, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		formMarcDraw(printX, printY, printWidth+1, printHeight+1);

	// Renderizamos celda "Aceptar"
		printSetArea(formCellX + (formCellNumWidth * formCellWidth) / 2, formCellY + (formCellNumHeight * formCellHeight), (formCellNumWidth * formCellWidth) / 2, formCellHeight);
		rgb = ( aceptBox? FORM_COLOR_BOX_SELECTED:FORM_COLOR_BOX_DISABLED);
//		alphaFillDraw(0xAA000000|rgb, printX, printY, printWidth, printHeight);
		fillDraw(rgb, printX, printY, printWidth, printHeight);
		formFontInit( !aceptBox? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK);
		printDraw(gameText[TEXT_ACEPTAR][0], 0,1, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		formMarcDraw(printX, printY, printWidth+1, printHeight+1);
	break;


	case FORM_MORE_GAMES:

	// calculamos area de pintado
		publiX = formBodyX - FORM_MARC_BORDER;
		publiY = formBodyY - FORM_MARC_BORDER - FORM_SEPARATOR + 1;
		publiW = formBodyWidth + (FORM_MARC_BORDER * 2);
		publiH = formBodyHeight + (FORM_MARC_BORDER * 2) + FORM_SEPARATOR - FORM_MARC_HEIGHT - printGetHeight();


	// renderizamos base de color gris
		fillDraw(POPUP_COLOR_GRIS, publiX, publiY, publiW, publiH);


	// renderizamos imagen de propaganda
		int borde = 5;
		if (formPubliImg != null)
		{
			imageDraw(formPubliImg, publiX + ((publiW - formPubliImg.getWidth())/2), publiY + ((publiH - formPubliImg.getHeight())/2) );
			borde = (((publiW - formPubliImg.getWidth()) / 2) - FORM_MORE_GAMES_ASPA_WIDTH) / 2;
		}

	// aspa Izquierda
		arrowDraw(-1, publiX + borde - FORM_MORE_GAMES_ASPA_WIDTH, publiY + ((publiH-(FORM_MORE_GAMES_ASPA_WIDTH*2))/2), (FORM_MORE_GAMES_ASPA_WIDTH*2), (FORM_MORE_GAMES_ASPA_WIDTH*2), ARROW_LEFT);
	// aspa Derecha
		arrowDraw(-1, publiX + publiW - borde - FORM_MORE_GAMES_ASPA_WIDTH, publiY + ((publiH-(FORM_MORE_GAMES_ASPA_WIDTH*2))/2), (FORM_MORE_GAMES_ASPA_WIDTH*2), (FORM_MORE_GAMES_ASPA_WIDTH*2), ARROW_RIGHT);

	// renderizamos scroll de texto
		formPubliScrollDraw();

	// renderizamos numero de paginas
		printSetArea(publiX, printY + printHeight + 1, publiW - FORM_MARC_WIDTH, FORM_MARC_HEIGHT);
		printDraw("1/2", 0, 0, formFontPenRGB, PRINT_RIGHT|PRINT_VCENTER);
	break;
	}

}

public void formPubliScrollDraw()
{
// renderizamos scroll de texto
	formFontInit(FORM_FONT_SMALL_WHITE);
	printSetArea(publiX, publiY + publiH, publiW, printGetHeight() + 1);
	fillDraw(POPUP_COLOR_MARRON, printX, printY, printWidth, printHeight);
	printDraw(formPubliStr, formPubliScrollX, 1, formFontPenRGB, PRINT_LEFT|PRINT_VCENTER);
	fillDraw(FORM_COLOR_BACKGROUND, printX, printY + printHeight, printWidth, 1);
}

// -------------------
// form Font Init
// ===================

//#ifdef S40
//#elifdef S60
	static final int FONT_SMALL_HEIGHT = 12;
	static final int FONT_LARGE_HEIGHT = 14;
//#elifdef S80
//#endif

int formFontPenRGB;

public void formFontInit(int type)
{
//#ifdef J2ME

	switch (type)
	{
	case FORM_FONT_SMALL_WHITE:

		printInit_Font(formFontSmallWhiteImg, formFontSmallWhiteCor, FONT_SMALL_HEIGHT, 0x20, 1);
//printInit_Font(biosGetFont());
formFontPenRGB = 0xffffff;
	break;

	case FORM_FONT_SMALL_BLACK:

		printInit_Font(formFontSmallBlackImg, formFontSmallBlackCor, FONT_SMALL_HEIGHT, 0x20, 1);
//printInit_Font(biosGetFont());
formFontPenRGB = 0x000000;
	break;

	case FORM_FONT_SMALL_RED:

		printInit_Font(formFontSmallRedImg, formFontSmallRedCor, FONT_SMALL_HEIGHT, 0x20, 1);
//printInit_Font(biosGetFont());
formFontPenRGB = 0xff0000;
	break;

	case FORM_FONT_LARGE:

		printInit_Font(formFontLargeImg, formFontLargeCor, FONT_LARGE_HEIGHT, 0x20, 1);
//printInit_Font(biosGetFont());
formFontPenRGB = 0xffffff;
	break;
	}

//#elifdef DOJA
//#endif

}




// -------------------
// form Marc Draw
// ===================

public void formMarcDraw(int x, int y, int width, int height)
{
	rectDraw(FORM_COLOR_MARC, x,y, width, height);
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

// <=- <=- <=- <=- <=-

// < SCROLL ENGINE ____________________________________________________________

// *******************
// -------------------
// Scroll - Engine v2.0 - Rev.10 (20.7.2005)
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

int tileWidth;
int tileHeight;

int ejeX = 0;
int ejeY = 0;

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
public void scrollInit(byte[] map, byte[] comb, final int tileWidth, final int tileHeight, int width, int height, Image imgA, Image imgB, int bankWidth, int bankHeight)
//#elifdef DOJA
//#endif
{

  this.tileWidth 	= tileWidth;
  this.tileHeight = tileHeight;


// Anotamos Imagen que contiene los tiles
//#ifdef J2ME
	scrollTilesAImg = imgA;
	scrollTilesBImg = imgB;
	scrollTilesBankWidth = bankWidth;
	scrollTileBankSize = bankWidth * bankHeight;
//#elifdef DOJA
//#endif


// Pillamos informacion del mapa
	//comb[0] = numero total de tiles combinados
	scrollTileCombFirst = comb[1] & 0xff;	// Numero de tile inicial para "tiles combinados"
	scrollTileCombCapas = comb[2] & 0xff;	// Numero de capas de los tiles combinados

// Anotamos variables del mapa, tama�o del mapa y tabla de tiles combinados
	scrollFaseMap = map;
	scrollFaseWidth = width;
	scrollFaseHeight = height;
	scrollTileComb = comb;

// Inicializamos variables a cero
	scrollFaseX = scrollFaseY = scrollFondoX = scrollFondoY = scrollX = scrollY = scrollBaseX = scrollBaseY = 0;
	scrollFirstTime = true;


//#ifdef SCROLL_TILE_RENDER
//#else
	scrollWidth = scrollAreaWidth;
	scrollHeight = scrollAreaHeight;
//#endif


// Calculamos tama�o del mapa usado como doble bufer y lo inicializamos
//#ifdef SCROLL_FULL_RENDER
//#elifdef SCROLL_TILE_RENDER
//#elifdef SCROLL_BUFFER_RENDER
	scrollFondoWidth = (scrollWidth / tileWidth) + ((scrollWidth % tileWidth==0)?1:2);
	scrollFondoHeight = (scrollHeight / tileHeight) + ((scrollHeight % tileHeight==0)?1:2);
//#endif


// Controlamos si la fase es mas peque�a que el area de scroll horizontalmente
	if (scrollFondoWidth > width)
	{
		scrollFondoWidth = width;
		scrollWidth = width * tileWidth;
	}

// Controlamos si la fase es mas peque�a que el area de scroll verticalmente
	if (scrollFondoHeight > height)
	{
		scrollFondoHeight = height;
		scrollHeight = height * tileHeight;
	}

//#ifndef SCROLL_FULL_RENDER
	// Inicializamos mapa del doble bufer
	scrollFondoMap = new byte[scrollFondoWidth * scrollFondoHeight];
	for (int i=0 ; i<scrollFondoMap.length ; i++) {scrollFondoMap[i]=-1;}
//#endif


// Calculamos tama�o de los bordes para scroll mas peque�os que el area de scroll o el modo TILE_RENDER
	scrollBorderLeft = (scrollAreaWidth - scrollWidth) / 2;
	scrollBorderRight = scrollAreaWidth - scrollWidth - scrollBorderLeft;
	scrollBorderTop =   (scrollAreaHeight - scrollHeight) / 2;
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
			scrollFondoImg = new Image();
			scrollFondoImg._createImage(scrollFondoWidth*tileWidth, scrollFondoHeight*tileHeight);
			scrollFondoGfx = scrollFondoImg.getGraphics();
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
/*
	while (X<0) {X+=(scrollFaseWidth*tileWidth);}
	while (Y<0) {Y+=(scrollFaseHeight*tileHeight);}

	while (X>=scrollFaseWidth*tileWidth) {X-=(scrollFaseWidth*tileWidth);}
	while (Y>=scrollFaseHeight*tileHeight) {Y-=(scrollFaseHeight*tileHeight);}
*/
//	scrollX = X % (scrollFaseWidth * tileWidth);
//	scrollY = Y % (scrollFaseHeight * tileHeight);

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

    //if (X != scrollX) {scrollX += X > scrollX? 1:-1;}
    //if (Y != scrollY) {scrollY += Y > scrollY? 1:-1;}




	if (X<0) {X=0;} else if (X>=(scrollFaseWidth*tileWidth)-scrollWidth) {X=(scrollFaseWidth*tileWidth)-scrollWidth;}
	if (Y<0) {Y=0;} else if (Y>=(scrollFaseHeight*tileHeight)-scrollHeight) {Y=(scrollFaseHeight*tileHeight)-scrollHeight;}

	while (X<0) {X+=(scrollFaseWidth*tileWidth);}
	while (Y<0) {Y+=(scrollFaseHeight*tileHeight);}

	while (X>=scrollFaseWidth*tileWidth) {X-=(scrollFaseWidth*tileWidth);}
	while (Y>=scrollFaseHeight*tileHeight) {Y-=(scrollFaseHeight*tileHeight);}

	// esto lo capa // BIGTHOR (Quitar el ||
	/*if (scrollFirstTime || scrollX-X < -scrollWidth || scrollX-X > scrollWidth) {scrollX=X;}
	if (scrollFirstTime || scrollY-Y < -scrollHeight || scrollY-Y > scrollHeight) {scrollY=Y;}
	*/
	if (scrollFirstTime) {scrollX=X;scrollY=Y;}

	scrollFirstTime = false;

	if (X > scrollX) {scrollX+=((X-scrollX)>>3)+1;}	// la rotacion marca la suavidad del movimiento para X
	if (X < scrollX) {scrollX-=((scrollX-X)>>3)+1;}	// la rotacion marca la suavidad del movimiento para X

	if (Y > scrollY) {scrollY+=((Y-scrollY)>>3)+1;}	// la rotacion marca la suavidad del movimiento para Y
	if (Y < scrollY) {scrollY-=((scrollY-Y)>>3)+1;}	// la rotacion marca la suavidad del movimiento para Y

	scrollTick(scrollX,scrollY);
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

							// bigthor - aqu� pintar mesa
							// CorY CorX
							int tileBack = (((CorY % 6)*6)+(CorX % 6));
							scrollFondoGfx.drawImage(scrollTilesBImg, (x*tileWidth)-((tileBack%scrollTilesBankWidth)*tileWidth), (y*tileHeight)-((tileBack/scrollTilesBankWidth)*tileHeight), Graphics.TOP|Graphics.LEFT);

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
/*
//#ifdef DEBUG
	scrollFondoGfx.setColor(0xffff00);
	scrollFondoGfx.drawRect(x*tileWidth ,y*tileHeight,  ((x+1)*tileWidth)-1 ,((y+1)*tileHeight)-1);
//#endif
 */

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
			scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, Graphics.TOP|Graphics.LEFT);
		} else {
			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect( scrollViewX,  scrollViewY, sx, scrollHeight);
			scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, Graphics.TOP|Graphics.LEFT);

			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect(sx+scrollViewX,  scrollViewY, px, scrollHeight);
			scr.drawImage(scrollFondoImg, sx+scrollViewX, ny+scrollViewY, Graphics.TOP|Graphics.LEFT);
		}
	} else {
		if (scrollFondoX == 0)
		{
			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect( scrollViewX,  scrollViewY, scrollWidth, sy);
			scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, Graphics.TOP|Graphics.LEFT);

			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect( scrollViewX, sy+scrollViewY, scrollWidth, py);
			scr.drawImage(scrollFondoImg, nx+scrollViewX, sy+scrollViewY  , Graphics.TOP|Graphics.LEFT);
		} else {
			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect( scrollViewX,  scrollViewY, sx, sy);
			scr.drawImage(scrollFondoImg, nx+scrollViewX, ny+scrollViewY, Graphics.TOP|Graphics.LEFT);

			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect(sx+scrollViewX,  scrollViewY, px, sy);
			scr.drawImage(scrollFondoImg, sx+scrollViewX, ny+scrollViewY, Graphics.TOP|Graphics.LEFT);

			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect( scrollViewX, sy+scrollViewY, sx, py);
			scr.drawImage(scrollFondoImg, nx+scrollViewX, sy+scrollViewY  , Graphics.TOP|Graphics.LEFT);

			scr.setClip( scrollViewX,  scrollViewY, scrollWidth, scrollHeight);
			scr.clipRect(sx+scrollViewX, sy+scrollViewY, px, py);
			scr.drawImage(scrollFondoImg, sx+scrollViewX, sy+scrollViewY  , Graphics.TOP|Graphics.LEFT);
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

// >___________________________________________________________________________


// ************************************************************************** //
// Final Clase Bios
// ************************************************************************** //






// *******************
// -------------------
// connect - Engine
// ===================
// *******************

static final int DOWNLOAD_HISCORE = 0;
static final int UPLOAD_HISCORE = 1;



static final int DELETE_FILE = 1;
static final int CHANGE_PRIORITY = 2;
static final int NEW_IMAGE = 3;
static final int NEW_LOGO = 4;
static final int NEW_FILE = 5;
static final int SCORES_DATA = 6;
static final int HIGHSCORE_SCROLLER = 7;



static final int LENGTH_INDEX = 8;
static final int HEADER_SIZE = 36;


String userHashId = "UNREGISTERED";


// Variables para almacenar el resultado de los hi-scores
int scoreFirst;
int scorePos;
int scorePlayers;
String[] scoreNick;
int[] scoreManos;
int [] scorePoints;



boolean connectDownloadOk = false;
boolean connectDownloadAbort = false;

int connectType;

int connectPacketId;

// -------------------
// connect Request
// ===================

public void connectRequest(int type)
{
//#ifdef DEBUG
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
		dos.writeInt(++connectPacketId);// packetId
		dos.writeInt(1);				// gameId (1=Domino)
		dos.writeInt(1);				// hansetId (1=MO-V3xx)
		dos.writeInt(1);				// carrierId (1=Movistar)

		switch (type)
		{
		case DOWNLOAD_HISCORE:

			connectAddHeader(2, dos);

			dos.writeUTF("Batolo");
		break;

		case UPLOAD_HISCORE:

			connectAddHeader(1, dos);

			dos.writeUTF("Batolo");

			dos.writeInt(3);	// Level
			dos.writeInt(10);	// Manos
			dos.writeInt(256);	// Points
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

	ga.connectCreate("http://192.168.0.1:8181/MR3SRV/ThincatServlet", data);
//	ga.connectCreate("http://studio.mjlabs.net:8181/MR3SRV/ThincatServlet", data);

	new Thread(ga).start();
}

// -------------------
// connect Add Header
// ===================

public void connectAddHeader(int command, DataOutputStream dos) throws Exception
{
	dos.writeInt(command);		// commandId: Download hiscore
	dos.writeUTF(userHashId);	// user Hash Id

	dos.writeInt(0);			// Numero de fichas en JAR/RMS
//	dos.writeInt(1);			// Ids de las fichas

	dos.writeByte(gameMoreGamesVisited?1:0);	// visitado mas juegos?
}

// -------------------
// connect Tick
// ===================

public void connectTick()
{
	if (!Game.connectRunning) {return;}

	if (connectDownloadAbort) {Game.connectRunning = false; return;}

	if (Game.connectFinished)
	{
		if (Game.connectResult == null)
		{
		//#ifdef DEBUG
			Debug.println("HTTP_result = null");
		//#endif
			Game.connectRunning = false;
		} else {
		//#ifdef DEBUG
			Debug.println("HTTP_result = "+Game.connectResult.length+"bytes");
		//#endif
			if (connectAction(new DataInputStream(new ByteArrayInputStream(Game.connectResult))))
			{
				connectDownloadOk = true;
			}
			Game.connectResult = null;
			Game.connectRunning = false;
		}
		System.gc();
	}
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
		int resultId = dis.readInt();
		String message = dis.readUTF();
		int size = dis.readInt();

	// common Domino header
		userHashId = dis.readUTF();

	//#ifdef DEBUG
		Debug.println ("packedId:"+packedId);
		Debug.println ("resultId:"+resultId);
		Debug.println ("message:"+message);
		Debug.println ("chunk size:"+size);
		Debug.println ("userHashId:"+userHashId);
		Debug.println ("packedId:"+packedId+", connectPacketId:"+connectPacketId);
	//#endif

	// Si es la respuesta al paquete que pedimos, lo procesamos
		if ( packedId == connectPacketId)
		{
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
				case DELETE_FILE:
	
					int numFiles = dis.readInt();
					for (int i=0 ; i<numFiles ; i++) {dis.readInt();}
				break;
	
				case CHANGE_PRIORITY:
	
					numFiles = dis.readInt();
					for (int i=0 ; i<numFiles ; i++) {dis.readInt(); dis.readInt();}
				break;
	
				case NEW_IMAGE:
	
					int imageId = dis.readInt();
					byte[] data = new byte[dis.readInt()];
					dis.read(data);
				break;
	
				case NEW_LOGO:
	
					int logoId = dis.readInt();
					data = new byte[dis.readInt()];
					dis.read(data);
				break;
	
				case NEW_FILE:

					int fileId = dis.readInt();
					int fileImageId = dis.readInt();
					int fileLogoId = dis.readInt();
					String fileURL = dis.readUTF();
					String fileAltText = dis.readUTF();
					String fileSoftkey = dis.readUTF();

					gameMoreGamesVisited = false;
					savePrefs();
				break;
	
				case SCORES_DATA:

				//#ifdef DEBUG
					Debug.println ("SCORES_DATA >>>>>>>>>>");
				//#endif
					scoreFirst = dis.readInt();
					scorePos = dis.readInt();
					scorePlayers = dis.readInt();

					scoreNick = new String[scorePlayers];
					scoreManos = new int[scorePlayers];
					scorePoints = new int[scorePlayers];

					for (int i=0 ; i<scorePlayers ; i++)
					{
						scoreNick[i] = dis.readUTF();
						scoreManos[i] = dis.readInt();
						scorePoints[i] = dis.readInt();
					//#ifdef DEBUG
						Debug.println (i+": "+scoreNick[i]+" , manos: "+scoreManos[i]+" , puntos: "+scorePoints[i]);
					//#endif
					}
				//#ifdef DEBUG
					Debug.println ("<<<<<<<<<<<<<<<<<");
				//#endif
				break;
	
				case HIGHSCORE_SCROLLER:
	
					String scrollText = dis.readUTF();
				break;
	
				default:
	
					for (int i=0 ; i<chunkSize ; i++) {dis.readByte();}
				break;
			}

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

// <=- <=- <=- <=- <=-










/*
// *******************
// -------------------
// cache - Engine
// ===================
// *******************

static final int cacheMAX = 32;

int cacheC;
int[] cacheP;

int[] cacheId;
int[] cacheImageId;
int[] cacheLogoId;
String[] cacheURL;
String[] cacheTxt;
String[] cacheSoftkey;

// -------------------
// cache Create
// ===================

public void cacheCreate()
{
	cacheC = 0;
	cacheP = new int[cacheMAX];
	for (int i=0 ; i<cacheMAX ; i++) {cacheP[i] = i;}

	cacheId = new int[cacheMAX];
	cacheImageId = new int[cacheMAX];
	cacheLogoId = new int[cacheMAX];

	cacheURL = new String[cacheMAX];
	cacheTxt = new String[cacheMAX];
	cacheSoftkey = new String[cacheMAX];

	for (int i=0 ; i<cacheMAX ; i++)
	{
		cacheId[i] = -1;
		cacheImageId[i] = -1;
		cacheLogoId[i] = -1;

		cacheURL[i] = " ";
		cacheTxt[i] = " ";
		cacheSoftkey[i] = " ";
	}


	byte[] in = rmsLoadFile("list");

	if (in == null || cacheReader(in))
	{
		pelaoNick = "Jugador";

		cacheListAdd(1, "Jerez",   0xF0000205);
		cacheListAdd(2, "Estoril", 0xF0000900);
		cacheListAdd(3, "Shanghai",0xF0000102);
		cacheListAdd(4, "Le Mans", 0xF0000304);
		cacheListAdd(5, "Mugello", 0xF0000403);
		rmsSaveFile("list", cacheGenerator());
	}


//#ifdef Debug
	for (int i=0 ; i<cacheTrackId.length ; i++)
	{
		System.out.println ("Id:"+cacheTrackId[i]+" Name:"+cacheTrackName[i]+" Weather:"+cacheTrackWeather[i]);
	}
//#endif

}





// -------------------
// cache Track Find
// ===================

public int cacheTrackFind(int id)
{
	for (int i=0 ; i<cacheC ; i++)
	{
		if (cacheTrackId[cacheP[i]] == id) {return cacheP[i];}
	}

	return -1;
}


// -------------------
// cache Track Save
// ===================

public boolean cacheTrackSave(int id, String name, int weather, byte[] data)
{
	if ( rmsSaveFile("t"+id+".trk", data) )
	{
		// Error Out Of RMS
		return true;
	} else {
		cacheListAdd(id, name, weather);

		rmsSaveFile("list", cacheGenerator());
	}

	return false;
}


// -------------------
// cache List Add
// ===================

public int cacheListAdd(int newId, String newName, int newWeather)
{
	if (cacheC == cacheMAX) {return -1;}

	int id = cacheP[cacheC++];

	cacheTrackId[id] = newId;
	cacheTrackName[id] = newName;
	cacheTrackWeather[id] = newWeather;

	cacheGhostId[id] = cachePlayerTime[id] = -1;
	cacheGhostName[id] = " ";

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
// cache Ghost Save
// ===================

public boolean cacheGhostSave(int trackId, int ghostId, int ghostTime, String ghostName, byte[] data)
{
	if ( rmsSaveFile("t"+trackId+".gho", data) )
	{
		// Error Out Of RMS
		return true;

	} else {

		int adr = cacheTrackFind(trackId);

		cacheGhostId[adr] = ghostId;
		cacheGhostTime[adr] = ghostTime;
		cacheGhostName[adr] = ghostName;

		rmsSaveFile("list", cacheGenerator());
	}

	return false;
}


// -------------------
// cache Player Save
// ===================

public boolean cachePlayerSave(int trackId, int time, byte[] data)
{
	if ( rmsSaveFile("t"+trackId+".ply", data) )
	{
		// Error Out Of RMS
		return true;

	} else {

		int adr = cacheTrackFind(trackId);

		cachePlayerTime[adr] = time;

		rmsSaveFile("list", cacheGenerator());
	}

	return false;
}


// -------------------
// cache Generator
// ===================

public byte[] cacheGenerator()
{
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	try {
		dos.writeUTF(pelaoNick);

		dos.writeInt(cacheC);	// Numero de Pistas

		for (int i=0 ; i<cacheC ; i++)
		{
			int id = cacheP[i];

			dos.writeInt(cacheTrackId[id]);
			dos.writeUTF(cacheTrackName[id]);
			dos.writeInt(cacheTrackWeather[id]);

			dos.writeInt(cacheGhostId[id]);
			dos.writeInt(cacheGhostTime[id]);
			dos.writeUTF(cacheGhostName[id]);

			dos.writeInt(cachePlayerTime[id]);
		}
	} catch (Exception e)
	{
	//#ifdef Debug
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
		pelaoNick = dis.readUTF();

		int tracks = dis.readInt();		// Numero de Pistas

		for (int i=0 ; i<tracks ; i++)
		{
			int id = cacheListAdd(dis.readInt(), dis.readUTF(), dis.readInt());	// trackId, trackName, trackWeather

			cacheGhostId[id] = dis.readInt();
			cacheGhostTime[id] = dis.readInt();
			cacheGhostName[id] = dis.readUTF();

			cachePlayerTime[i] =  dis.readInt();
		}
	} catch (Exception e)
	{
	//#ifdef Debug
		Debug.println ("cacheReaderError:  "+e.toString());
	//#endif
		return true;
	}

	return false;
}

// <=- <=- <=- <=- <=-
*/










public int textMaxSize(String[] str)
{
	int textoSizeX = 0;
	for (int sizeX, i=0 ; i<str.length ; i++)
	{
		sizeX = printStringWidth(str[i]); if ( textoSizeX < sizeX ) {textoSizeX = sizeX;}
	}

	return textoSizeX;
}




// -------------------
// slider Vertical Draw
// ===================

static final int SLIDER_COLOR_BLANCO = 0xffffff;
static final int SLIDER_COLOR_NARANJA = 0x948663;
static final int SLIDER_COLOR_GRIS = 0x9ec6bf;
static final int SLIDER_COLOR_MARRON = 0xc0a874;

public void sliderVDraw(int penRGB, int backRGB, int pos, int steps, int x, int y, int height)
{
	int barHeight = height / 4;
	int posY = y + regla3(pos, steps, height - barHeight);
	fillDraw(backRGB, x, y, FORM_SLIDER_WIDTH, height);
	fillDraw(penRGB, x, posY, FORM_SLIDER_WIDTH, barHeight);
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

//	putColor(0xffffff); scr.fillRect(x,y,width,height);		// Debug: rellena cuadrado donde dibujar Arrow

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






// *******************
// -------------------
// pili - Engine
// ===================
// *******************

/*
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.*;
//#ifdef J2ME
import javax.microedition.lcdui.Graphics;
//#elifdef DOJA
import com.nttdocomo.ui.Graphics;
//#endif
*/

/**
 * PInta LIneas
 * 
 * Clase para pintarrajear gr�ficos convertidos a l�neas.
 * 
 */

	int[][] palette;

	byte[][] data;

	/**
	 * Constructor de la clase.
	 * 
	 * @param datapack
	 *            paquete de datos de l�neas.
	 */
	public void piliCreate(byte[] datapack) {
		try {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				datapack));
		int pilis = dis.readByte() & 0xFF;
		//#ifdef DEBUG
		Debug.println("PILIS EN PACK: " + pilis);
		//#endif
		palette = new int[pilis][];
		data=new byte[pilis][];
		for (int p = 0; p < pilis; p++) {
			palette[p] = new int[dis.readByte() & 0xFF];
			//#ifdef DEBUG
			Debug.println("PILI: " +p+" NUM.COLORS: " + palette[p].length);
			//#endif			
			for (int c = 0; c < palette[p].length; c++) {
				palette[p][c] = dis.readInt();
			}
			data[p] = new byte[dis.readInt()];
			//#ifdef DEBUG
			Debug.println("PILI: " +p+" DATOS: " + data[p].length);
			//#endif						
			dis.readFully(data[p]);			
		}
		dis.close();
		} catch(Exception e)
		{
		//#ifdef DEBUG
			Debug.println("piliCreate() Exception");
		//#endif
		}

	}

	public void piliDraw(int x, int y, int pili) {
	try {
	//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
	//#endif
		int p = 0;
		do {
			int r = (data[pili][p++] & 0xFF) + y;
			int lc = (data[pili][p++] & 0xFF);
			for (int c = 0; c < lc; c++) {
				scr.setColor(palette[pili][(data[pili][p++] & 0xFF)]);
				scr.drawLine((data[pili][p++] & 0xFF) + x, r, (data[pili][p++] & 0xFF) + x, r);
			}
		} while (p < data[pili].length);
	} catch (Exception e) {}
	}


// <=- <=- <=- <=- <=-




// -------------------
// connect Wait
// ===================

public int connectWait()
{
	while (true)
	{
		biosLoop();

	// Habilitamos que se salte al estado de PAUSA
		biosPauseEnabled = true;

	// Controlamos si la conexion ha terminado
		if (!Game.connectRunning)
		{
			if (popupRunning)
			{
				popupRelease();
				popupExit = false;
				biosStatus = biosStatusOld;
			}

			if (!connectDownloadOk)
			{
				popupInitAcept(null, "Error de red");
				biosWait();
			} else {
				return 1;
			}

			return 0;
		}

	// Controlamos si hemos salido del popup, por lo tanto, cancelado la peticion de red
		if (!popupRunning)
		{
			connectDownloadAbort = true;
			return -1;
		}
	}
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
		if (strChar[i] >= 65 && strChar[i] <= 90)
		{
			if (!may) {strChar[i] += 32;}
			may = false;
		}
		else if (strChar[i] == 32) {may = true;}
	}
	return new String(strChar);
}



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
final static int TEXT_GAME_DIFFICULTY = 4;
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

final static int TEXT_OPTIONS = 19;
final static int TEXT_NEWGAME = 20;
final static int TEXT_GAME_POINTS = 21;
final static int TEXT_GAME_CONTROL = 22;
final static int TEXT_GAME_WITH_HELP = 23;
final static int TEXT_GAME_RULES = 24;

final static int TEXT_EDIT_NAMES = 25;
final static int TEXT_ABECEDARIO = 26;
final static int TEXT_BORRAR = 27;
final static int TEXT_ACEPTAR = 28;

final static int TEXT_PLAYER_NAMES = 29;
final static int TEXT_MULTIPLAYER_MODES = 30;

final static int TEXT_EQUIPOS 						= 31;
final static int TEXT_INDIVIDUAL 					= 32;

final static int TEXT_CONFIRMATION = 33;
final static int TEXT_GAME_WITH_SOUND = 34;
final static int TEXT_NAMES = 35;
final static int TEXT_ENTER_NAME = 36;
final static int TEXT_GAME_MODE = 37;

final static int TEXT_EMPIEZA_PARTIDA								= 38;
final static int TEXT_TURNODE										= 39;
final static int TEXT_PASAMOVIL									= 40;
final static int TEXT_PLAYER_CONFIG							= 41;
final static int TEXT_ROBO_FICHA								= 42;
final static int TEXT_PASO											= 43;
final static int TEXT_DEBES_ROBAR								= 44;
final static int TEXT_DEBES_PASAR								= 45;

final static int TEXT_PARTIDA_RAPIDA						= 46;
final static int TEXT_EMPEZAR_TORNEO						= 47;
//INTRANCE
final static int TEXT_TORNEOS										= 48;
final static int TEXT_TORNEO0										= 49;
final static int TEXT_TORNEO1										= 50;
final static int TEXT_TORNEO2										= 51;
final static int TEXT_TORNEO3										= 52;
final static int TEXT_CLASIFICACION							= 53;
final static int TEXT_NOMBRES_TORNEO0						= 54;
final static int TEXT_NOMBRES_TORNEO1						= 55;
final static int TEXT_NOMBRES_TORNEO2						= 56;
final static int TEXT_NOMBRES_TORNEO3						= 57;
final static int TEXT_REJUGAR										= 58;
final static int TEXT_MORE_GAMES								= 59;
final static int TEXT_CONTINUAR_TORNEO					= 60;
final static int TEXT_HI_SCORES									= 61;
final static int TEXT_VS1												= 62;
final static int TEXT_VS2												= 63;
final static int TEXT_VS3												= 64;
final static int TEXT_VS_Equipos								= 65;
final static int TEXT_HELP_OBJETIVO							= 66;
final static int TEXT_HELP_COMO_JUGAR						= 67;
final static int TEXT_HELP_COMO_GANAR						= 68;
final static int TEXT_HELP_DESACTIVAR_TUTORIAL	= 69;
final static int TEXT_HELP_OBJETIVO_S						= 70;
final static int TEXT_HELP_COMO_JUGAR_S					= 74;
final static int TEXT_HELP_COMO_GANAR_S					= 76;
final static int TEXT_HELP_DESACTIVAR_TUTORIAL_S= 81;


final static int TEXT_PLAYER										= 84;
final static int TEXT_FRASES_BUENAS							= 85;
final static int TEXT_FRASES_MALAS							= 86;
final static int TEXT_YOU_WIN										= 87;
final static int TEXT_YOU_LOSE									= 88;
final static int TEXT_PREMIOS										= 89;
//INTRANCE
//-007-
final static int TEXT_GAME_IN_PAUSE							= 90;
final static int TEXT_CLASIFICATION							= 91;
final static int TEXT_HANDS											= 92;
final static int TEXT_POINTS										= 93;
final static int TEXT_MENU_TORNEOS							= 94;
final static int TEXT_TORNEO_EN_CURSO						= 95;
final static int TEXT_SUPERA_TORNEOS						= 96;
final static int TEXT_SORTEO										= 97;
final static int TEXT_EMPIEZA										= 98;
final static int TEXT_INFO											= 99;
final static int TEXT_FIN_DE_TORNEO							= 100;
final static int TEXT_PREMIO										= 101;
final static int TEXT_TORNEO_COMPLETADO					= 102;
final static int TEXT_PARTIDA_CERRADA						= 103;
final static int TEXT_EL_EQUIPO_GANADO_RONDA		= 104;
final static int TEXT_DICE											= 105;
final static int TEXT_ACTUAL										= 106;
final static int TEXT_TOTAL											= 107;
final static int TEXT_PUNTOS_RONDA							= 108;
final static int TEXT_PUNTOS										= 109;
final static int TEXT_SCORE_PARTIDA_LIBRE				= 110;
//=007=
final static int TEXT_FRASES_MALAS_TORNEO 			= 111;
final static int TEXT_FICHAS_MONTON							= 112;
final static int TEXT_RONDA_ELIMINATORIA 				= 113;
final static int TEXT_BUENA_PUNTUACION 					= 114;

final static int TEXT_DOMINO										= 115;
final static int TEXT_NINGUN_PUNTO									= 116;
final static int TEXT_RECUENTO										= 117;
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int GAME_START = 0;
final static int GAME_LOGOS = 5;

final static int GAME_MENU_START = 10;
final static int GAME_MENU_CARATULA = 15;
final static int GAME_MENU_MAIN = 20;
final static int GAME_MENU_MAIN_END = 21;

final static int GAME_MENU_INGAME = 25;
final static int GAME_MENU_INGAME_HELP = 26;

final static int GAME_MENU_GAMEOVER = 30;

final static int GAME_EXIT = 35;

final static int GAME_PLAY = 40;
final static int GAME_PLAY_INIT = 41;
final static int GAME_PLAY_TICK = 42;
// ===============================================


// Variables para la configuaracion del tipo de juego
byte gameDifficulty;
byte gamePoints;
byte gameControl;
byte gameWithHelp = 1;
byte gameRules;

boolean gameMoreGamesVisited = false;	// Variable que nos indica si se visitado el menu "mas juegos"

// Nombre de los jugadores por defecto
String[] gamePlayerNames;
String[] gamePlayerNamesOnly;
String[] gameTeamNamesOnly;

int gameAlphaExit;

int gameStatus = 0;

// -------------------
// game Create
// ===================

public void gameCreate()
{

// --------------------------------
// Inicializamos nombres de los jugadores por defecto
// ================================
	gamePlayerNames = gameText[TEXT_PLAYER_NAMES];
// ================================


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
			"/m0.mid",		// 0 - Musica de la caratula
			"/m1.mid",		// 1 - Men�s 
			"/m2.mid",		// 2 - Ganar
			"/m3.mid",		// 3 - Perder
			"/f0.mid",		// 4 - Paso
			"/f1.mid",		// 5 - Robo
			"/f2.mid",		// 6 - Lanzo
			"/f3.mid",		// 7 - Inicio partida
			"/f4.mid",		// 8 - Pasar turno
			"/f5.mid",		// 9 - Cerrar partida
			"/f6.mid"			// 10 - Solo queda una ficha
			});

	//#endif


	//#ifdef SG-D410
	//#endif

//#endif
// ================================

		// Cargamos las flechas de scroll
		flechas_gfx			= loadImage("/flechas");
		flechas_gfx_cor	= loadFile("/flechas.cor");
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

	  //#ifdef DEBUG
	    logosInit(new String[] {"/microjocs"}, new int[] {0xffffff}, 10);
	  //#else
		logosInit(new String[] {/*"/movistar",*/"/microjocs"}, new int[] {/*0x004E99,*/ 0xffffff}, 3000);
	  //#endif
		biosWait();

		canvasFillTask(0xffffff);		// Color Blanco
		canvasImg = loadImage("/loading");
		forceRender();
		forceRenderTask(RENDER_LOADING);		// mostramos "loading" por debajo del reloj de arena

//#ifdef FULLSCREEN
	canvasHeight -= listenerHeight;		// Reservamos espacio para el area de las softkeys (listener 'simulado')
//#endif

		gameStatus = GAME_MENU_START;

//		menuCreate();					// *** PARA KITAR ***
//		gameStatus = GAME_PLAY;			// *** PARA KITAR ***	
	break;
// ===============================



// -------------------------------
// Gestor de menus del source base
// ===============================
	case GAME_MENU_START:			// Inicializamos recursos para menus y preguntamos si queremos juego con sonido

		menuCreate();
		piliCreate(loadFile("/data.pili"));
		menuImg = loadImage("/caratula");

// -------------------- pruebas 007 --------------------
//popupInitTable(new String[] {"rd1", "rd2", "rd3","rd1", "rd2", "rd3"}, new String[] {"Juan", "Pedro", "Manolo", "Juan", "Pedro", "Manolo"}, new String[][] {{"1","2","3","1","2","3"},{"1","2","3","1","2","3"},{"1","2","3","1","2","3"},{"1","2","3","1","2","3"},{"1","2","3","1","2","3"},{"1","2","3","1","2","3"}});
//popupTablePos = 2;
//biosWait();

//menuInit(MENU_MORE_GAMES);
// ==================== pruebas 007 ====================

	// Preguntamos si queremos juego con sonido
		forceRenderTask(RENDER_GAME_WIDTH_SOUND);
		listenerInit(SOFTKEY_NO, SOFTKEY_YES);

		gameStatus++;

	case GAME_MENU_START + 1:

		if (lastKeyMenu == 0 && keyMenu != 0)
		{
			gameSound = (keyMenu > 0);
			gameStatus++;
		}
	break;


	case GAME_MENU_START + 2:

		canvasFillTask(0xffffff);			// Color Blanco
		canvasImg = loadImage("/loading");
		forceRender();
		forceRenderTask(RENDER_LOADING);	// mostramos "loading" por debajo del reloj de arena

	// --------------------------------
	// Creamos e inicializamos rmsEngine (Mini Disco Duro)
	// ================================
	//#ifdef RMS_ENGINE
	//#endif
	// ================================

		gameStatus = GAME_MENU_CARATULA;
	break;


	case GAME_MENU_CARATULA:			// Reproducimos musica con la caratula de fondo y ponemos "menu" en softkey

		if (menuImg == null) {menuImg = loadImage("/caratula");}
		canvasImg = menuImg;
		soundPlay(MUSICA_CARATULA, 0);
		listenerInit(SOFTKEY_NONE, SOFTKEY_EMPEZAR);
		gameStatus++;

	case GAME_MENU_CARATULA + 1:		// Esperamos a pulsar 'menu' para acceder al menu principal

		//#ifndef DEBUG
		//#else
			gameStatus = GAME_MENU_MAIN;
		//#endif
	break;


	case GAME_MENU_MAIN:
		menuInit(MENU_MAIN);

		soundPlay(MUSICA_MENUS, 0);

		gameStatus = GAME_MENU_MAIN_END;
	break;

	case GAME_MENU_MAIN_END:

		soundStop();
		menuImg = null;

		gameStatus = GAME_PLAY;
	break;


	case GAME_MENU_INGAME:

	//#ifdef LOW_MEM
	//#endif

		playLeftSoftkeyOld = listenerIdLeft;
		playRightSoftkeyOld = listenerIdRight;
		menuInit(MENU_INGAME);
		gameStatus = GAME_PLAY_TICK;
	break;

	case GAME_MENU_INGAME_HELP:

	//#ifdef LOW_MEM
	//#endif

		playLeftSoftkeyOld = listenerIdLeft;
		playRightSoftkeyOld = listenerIdRight;
		menuInit(MENU_HELP_INGAME);
		gameStatus = GAME_PLAY_TICK;
	break;


	case GAME_MENU_GAMEOVER:

/*
		canvasFillTask(0x000000);
		canvasTextTask(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, PRINT_VCENTER|PRINT_HCENTER | PRINT_OUTLINE);
		forceRender();

		waitTime(3000);
*/
//		gameStatus = GAME_MENU_CARATULA;
		gameStatus = GAME_MENU_MAIN;
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

	// mostramos "loading" dentro de menu para dar paso al juego
		menuInit(MENU_LOADING);
		forceRender();
		menuRelease();
		menuExit = false;
		biosStatus = biosStatusOld;

/*
	// Pintamos la pantalla de negro y mostramos un texto de "Cargando..."
		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, PRINT_VCENTER|PRINT_HCENTER);
	// Forzmos un repaint (refresco de pantalla para que se se borre la pantalla y aparezca el texto.
		forceRender();
*/

	// Cargamos e inicializamos todos los recursos referentes al juego. Este proceso solo se hace una vez.
		System.out.println("MEEEEEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSSAAAAAAAAAAAAAAAAAAAAAAAAAAAA: "+mesaSeleccionada);
		playCreate();
		gameStatus++;

	case GAME_PLAY_INIT:
/*
	// Pintamos la pantalla de negro y mostramos un texto de "Cargando..."
		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, PRINT_VCENTER|PRINT_HCENTER);
	// Forzmos un repaint (refresco de pantalla para que se se borre la pantalla y aparezca el texto.
		forceRender();
*/
	// Cargamos e inicializamos todos los recursos para el nivel actual (faseLevel)
		playInit();
	// Este Flag, nos indica el motivo por el cual debemos terminar el juego (1:Siguiente nivel, 2:muerte, 3:game over forzado desde menu)
		playExit = 0;
		ContinuandoJuego = false;
		playStatus = PLAY_STATUS_CREATE_GAME;

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
		
		if (keyMisc == 11 && lastKeyMisc == 0) {gameStatus = GAME_MENU_INGAME_HELP; break;}

	// Procesamos un TICK del juego, si true: debemos parar el juego...
		if ( !playTick() ) {break;}

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		soundStop();

	// Desalojamos todos los recursos cargados en 'playInit()'
		playRelease();

	// Colocamos el ESTADO en 'GAME_PLAY_INIT'
		gameStatus = GAME_PLAY_INIT;

		switch (playExit)
		{
		case 1:	// Nivel Completado, pasamos al siguiente nivel
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
	case GAME_MENU_START + 1:	// Estamos preguntando si queremos juego con sonido.

		forceRenderTask(RENDER_GAME_WIDTH_SOUND);
	break;

	case GAME_MENU_CARATULA + 1:

		canvasImg = menuImg;
	break;

	case GAME_PLAY_TICK:		// GAME_PLAY_TICK => Estado del juego cuando esta en funcionamiento.

		playRefresh();
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
		fillDraw(POPUP_COLOR_NARANJA, 0, 0, canvasWidth, canvasHeight);
		printSetArea(0, 0, canvasWidth, canvasHeight);
		printDraw(gameText[TEXT_GAME_IN_PAUSE][0], 0, 0, 0xffffff, PRINT_HCENTER|PRINT_VCENTER);
		listenerDraw();
		return;
	}


// Gestiones internes de la BIOS
	if (canvasFillShow) { canvasFillShow=false; canvasFillDraw(canvasFillRGB); }
	if (canvasImg!=null) { imageDraw(canvasImg); canvasImg=null; System.gc(); }
	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }



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

static final int RENDER_LOADING = 0;
static final int RENDER_GAME_WIDTH_SOUND = 1;

// -------------------
// force Render Draw (TASK) :: Tareas que se ejecutan dentro de 'paint()' lanzadas desde la l�gica con 'forceRenderTask(id)'
// ===================

public void forceRenderDraw()
{
	switch (forceRenderType)
	{
	case RENDER_LOADING:

		printInit_Font(biosGetFont());
		printSetArea(0, canvasHeight/2, canvasWidth, canvasHeight/2);
		printDraw(gameText[TEXT_LOADING], 0, 0, 0x000000, PRINT_HCENTER|PRINT_VCENTER);
	break;

	case RENDER_GAME_WIDTH_SOUND:

		fillDraw(0xffffff, 0, 0, canvasWidth, canvasHeight);

		piliDraw((canvasWidth - 141)/2, (canvasHeight - 110)/2, 0);

		formFontInit(FORM_FONT_SMALL_BLACK);
		printSetArea(4, 0, canvasWidth - 8, canvasHeight);
		printDraw(gameText[TEXT_GAME_WITH_SOUND][0], 0, 0, 0x000000, PRINT_HCENTER|PRINT_VCENTER|PRINT_BREAK);
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
static final int MENU_MAIN = 0;
static final int MENU_OPTIONS = 1;
static final int MENU_INGAME = 2;
static final int MENU_HELP = 3;
static final int MENU_SCROLL_HELP = 4;
static final int MENU_SCROLL_ABOUT = 5;
static final int MENU_ASK = 6;
static final int MENU_FINAL = 7;
static final int IN_GAME_HELP = 8;

static final int MENU_EDIT_NAMES = 9;
static final int MENU_INPUT_NAME = 10;

static final int MENU_CONFIG_EQUIPOS = 11;
//INTRANCE
static final int MENU_TORNEO = 12;
//INTRANCE
final static int MENU_NEW_GAME 				= 13;
final static int MENU_MORE_GAMES 			= 14;
final static int MENU_PARTIDA_RAPIDA	= 15;
final static int MENU_HI_SCORES				= 16;
final static int MENU_SCROLL_HELP1		= 17;
final static int MENU_SCROLL_HELP2		= 18;
final static int MENU_SCROLL_HELP3		= 19;
final static int MENU_SCROLL_HELP4		= 20;

final static int MENU_HELP_INGAME			= 21;
final static int MENU_LOADING			= 22;
final static int MENU_DEBUG			= 23;


// ===============================================

// -----------------------------------------------
// Variables staticas para identificar las acciones de los menus...
// ===============================================
static final int MENU_ACTION_NONE = -5;
static final int MENU_ACTION_BACK = -10;
static final int MENU_ACTION_PLAY = 0;
//static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_HELP = 2;
static final int MENU_ACTION_SHOW_HELP = 3;
static final int MENU_ACTION_SHOW_ABOUT = 4;
static final int MENU_ACTION_EXIT_GAME = 5;
static final int MENU_ACTION_EXIT_GAME_OK = 6;
static final int MENU_ACTION_RESTART = 7;
//static final int MENU_ACTION_RESTART_OK = 8;
static final int MENU_ACTION_SOUND_CHG = 9;
static final int MENU_ACTION_VIBRA_CHG = 10;
static final int MENU_ACTION_ASK = 11;
static final int MENU_ACTION_SHOW_OPTIONS = 12;
static final int MENU_ACTION_REPLAY = 13;
static final int MENU_ACTION_REPLAY_OK = 14;
static final int MENU_ACTION_PLAYER_CONFIG_CHG	= 25;
static final int MENU_ACTION_PLAYER_CONFIG			= 26;

static final int MENU_ACTION_GAME_DIFFICULTY_CHG = 15;
static final int MENU_ACTION_GAME_POINTS_CHG = 16;
static final int MENU_ACTION_GAME_CONTROL_CHG = 17;
static final int MENU_ACTION_GAME_WITH_HELP_CHG = 18;
static final int MENU_ACTION_GAME_RULES_CHG = 19;

static final int MENU_ACTION_EDIT_NAMES = 20;
static final int MENU_ACTION_INPUT_NAME = 21;

static final int MENU_ACTION_EQUIPOS 				= 22;
static final int MENU_ACTION_INDIVIDUAL 		= 23;

static final int MENU_ACTION_BACK_TO_GAME = 24;
//INTRANCE
static final int MENU_ACTION_TORNEOS = 50;
static final int MENU_ACTION_TORNEO0 = 51;
static final int MENU_ACTION_TORNEO1 = 52;
static final int MENU_ACTION_TORNEO2 = 53;
static final int MENU_ACTION_TORNEO3 = 54;
static final int MENU_ACTION_TORNEO_LOCKED = 55;
static final int MENU_ACTION_CONTINUE_TORNEO = 56;
//INTRANCE

static final int MENU_ACTION_NEW_GAME				= 60;
static final int MENU_ACTION_MORE_GAMES				= 61;
static final int MENU_ACTION_PARTIDA_RAPIDA 		= 62;
static final int MENU_ACTION_HI_SCORES				= 63;
static final int MENU_ACTION_VS1					= 64;
static final int MENU_ACTION_VS2					= 65;
static final int MENU_ACTION_VS3					= 66;
static final int MENU_ACTION_VS_Equipos				= 67;
static final int MENU_ACTION_SHOW_HELP_1			= 68;
static final int MENU_ACTION_SHOW_HELP_2			= 69;
static final int MENU_ACTION_SHOW_HELP_3			= 70;
static final int MENU_ACTION_SHOW_HELP_4			= 71;

static final int MENU_ACTION_MASJUEGOS = 72;




//#ifdef DEBUG
static final int MENU_ACTION_DEBUG = 50000;
static final int MENU_ACTION_DEBUG_UPLOAD_SCORE = 50001;
//#endif
// ===============================================


// -----------------------------------------------
// Variables staticas para identificar los diferentes logo tipo PILI!
// ===============================================
static final int PILI_GAME_WITH_SOUND = 0;
static final int PILI_CARATULA = 1;
static final int PILI_INFO = 2;
static final int PILI_OPTIONS = 3;
// ===============================================




int menuType;

int menuActionAsk;

boolean menuExit;		// Flag para forzar la salida del stado 'BIOS/MENU'

Image menuImg;
Image menuLogoImg;

boolean menuFadeColor = false;

// variables para el control de la pila en la gestion de la jerarquia de menus
	int menuStackIndex = 0;
	int[][] menuStack = new int[16][2];


int menuEditPlayerSelected;		// guardamos el jugador seleccionado para cambiar su nombre


// -------------------
// menu Create
// ===================

public void menuCreate()
{
	formCreate();
	popupCreate();
	menuLogoImg = loadImage("/formLogo");
}


public void menuResourcesFree()
{
	System.gc();
	menuLogoImg = null;
	formBodyImg = null;
	System.gc();
}

public void menuResourcesReload()
{
	menuLogoImg = loadImage("/formLogo");
//	formBodyImg = loadImage("/formBody");
}


// -------------------
// menu Destroy
// ===================

public void menuDestroy()
{
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
	menuType = type;

	menuExit = false;

	formClear();

	switch (type)
	{
	case MENU_MAIN:
//	  INTRANCE
	    modoTorneo = false;
//	  INTRANCE
		formLogoInit(menuLogoImg);

		formPiliItem = PILI_CARATULA;

//		formTitleInit("Menu principal");

		formListClear();
		//formListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_NONE);
 		/*
 		formListAdd(0, gameText[TEXT_EQUIPOS], MENU_ACTION_EQUIPOS); 		
 		formListAdd(0, gameText[TEXT_INDIVIDUAL], MENU_ACTION_INDIVIDUAL);
 		*/
		formListAdd(0, gameText[TEXT_PLAY], 				MENU_ACTION_NEW_GAME); 		
 		formListAdd(0, gameText[TEXT_HELP], 				MENU_ACTION_HELP);
		formListAdd(0, gameText[TEXT_OPTIONS], 			MENU_ACTION_SHOW_OPTIONS);
		formListAdd(0, gameText[TEXT_MORE_GAMES], 	MENU_ACTION_MORE_GAMES);
		formListAdd(0, gameText[TEXT_EXIT], 				MENU_ACTION_EXIT_GAME);

	//#ifdef DEBUG
		formListAdd(0, "DEBUG", 				MENU_ACTION_DEBUG);
	//#endif

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;
	
	case MENU_NEW_GAME:

		formLogoInit(menuLogoImg);

		formPiliItem = PILI_CARATULA;

//		formTitleInit("Menu principal");

		formListClear();
		formListAdd(0, gameText[TEXT_EMPEZAR_TORNEO], 	MENU_ACTION_TORNEOS);
		if (SavedTorneo>=0) formListAdd(0, gameText[TEXT_CONTINUAR_TORNEO],	MENU_ACTION_CONTINUE_TORNEO);
		formListAdd(0, gameText[TEXT_PARTIDA_RAPIDA],  	MENU_ACTION_PARTIDA_RAPIDA);
		formListAdd(0, gameText[TEXT_EDIT_NAMES], 			MENU_ACTION_EDIT_NAMES);
		formListAdd(0, gameText[TEXT_HI_SCORES], 				MENU_ACTION_HI_SCORES);
		
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;
	
	case MENU_MORE_GAMES:

	// Mostramos "espere..."
		formLogoInit(menuLogoImg);

		formPiliItem = -1;

		formListClear();
		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		forceRender();

		gameMoreGamesVisited = true;
		savePrefs();

	// Cargamos recursos para la publicidad
		formLogoInit(loadImage("/l1"));

		formPubliInit(loadImage("/i1"), "Aqui hay ke vender la moto pa ke compren el juego publicitado...");

		formInit(FORM_MORE_GAMES, 0, MENU_ACTION_BACK, MENU_ACTION_MASJUEGOS);

		listenerInit(SOFTKEY_BACK, SOFTKEY_MASJUEGOS);
	break;
	
	case MENU_PARTIDA_RAPIDA:
	  // configuramos las partidas r�pidas
	  mesaSeleccionada 				= 0; 
	  levelAI[0]							= AI_4;
	  levelAI[1]							= AI_4;
	  levelAI[2]							= AI_4;
	  levelAI[3]							= AI_4;
	  //INTRANCE
	  numPartidas = 0;
	  PuntosActuales[0] = 0;
	  //INTRANCE
	      

		formLogoInit(menuLogoImg);

//		formTitleInit("Menu principal");

		formListClear();
		formListAdd(0, gameText[TEXT_VS1], 							MENU_ACTION_VS1);
		formListAdd(0, gameText[TEXT_VS2],							MENU_ACTION_VS2);
		formListAdd(0, gameText[TEXT_VS3],  						MENU_ACTION_VS3);
		formListAdd(0, gameText[TEXT_VS_Equipos], 			MENU_ACTION_VS_Equipos);
		
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;
	
	case MENU_HI_SCORES:

		formLogoInit(menuLogoImg);

		formPiliItem = -1;

		formTextInit(new String[] {gameText[TEXT_CLASIFICATION][0]});

		formInit(FORM_NONE, 0, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);

		popupSetArea(formBodyX, formTextY + formTextHeight + 2, formBodyWidth, formBodyHeight - formTextHeight - 6);

		String[][] tmpStr = new String[scorePlayers][2];
		for (int i=0 ; i<scorePlayers ; i++)
		{
			tmpStr[i][0] = ""+scoreManos[i];
			tmpStr[i][1] = ""+scorePoints[i];
		}
		popupInitTable(new String[] {gameText[TEXT_HANDS][0], gameText[TEXT_POINTS][0]}, scoreNick, tmpStr);
		popupTablePos = scorePos - scoreFirst;
		biosWait();

		menuAction(MENU_ACTION_BACK	);
	return;


	case MENU_OPTIONS:

		formLogoInit(menuLogoImg);

		formPiliItem = PILI_OPTIONS;

//		formTitleInit(gameText[TEXT_OPTIONS][0]);
		formTextInit(gameText[TEXT_OPTIONS]);

		formListClear();
		//formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_HELP);
	//#ifndef PLAYER_NONE
		formListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		formListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		//formListAdd(0, gameText[TEXT_GAME_DIFFICULTY], MENU_ACTION_GAME_DIFFICULTY_CHG, gameDifficulty);
		//formListAdd(0, gameText[TEXT_GAME_POINTS], MENU_ACTION_GAME_POINTS_CHG, gamePoints);
		//formListAdd(0, gameText[TEXT_GAME_CONTROL], MENU_ACTION_GAME_CONTROL_CHG, gameControl);
		formListAdd(0, gameText[TEXT_GAME_WITH_HELP], MENU_ACTION_GAME_WITH_HELP_CHG, gameWithHelp);
		//formListAdd(0, gameText[TEXT_GAME_RULES], MENU_ACTION_GAME_RULES_CHG, gameRules);
		//formListAdd(0, gameText[TEXT_EDIT_NAMES], MENU_ACTION_EDIT_NAMES);
		formListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_INGAME:

		formLogoInit(menuLogoImg);

		formPiliItem = PILI_CARATULA;

//		formTitleInit("Menu");

		formListClear();
		formListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_BACK_TO_GAME);
	//#ifndef PLAYER_NONE
		formListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		formListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		formListAdd(0, gameText[TEXT_GAME_WITH_HELP], MENU_ACTION_GAME_WITH_HELP_CHG, gameWithHelp);
		formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_HELP);
		formListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		formListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		//formInit(FORM_OPTION, pos, MENU_ACTION_BACK_TO_GAME, MENU_ACTION_NONE);
		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		//listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_HELP:

		formLogoInit(menuLogoImg);

//		formTitleInit("Ayuda");
		formTextInit(gameText[TEXT_HELP]);

		formListClear();
		
		formListAdd(0, gameText[TEXT_HELP_OBJETIVO], 						MENU_ACTION_SHOW_HELP_1);
		formListAdd(0, gameText[TEXT_HELP_COMO_JUGAR], 					MENU_ACTION_SHOW_HELP_2);
		formListAdd(0, gameText[TEXT_HELP_COMO_GANAR], 					MENU_ACTION_SHOW_HELP_3);
		formListAdd(0, gameText[TEXT_HELP_DESACTIVAR_TUTORIAL], MENU_ACTION_SHOW_HELP_4);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;

	case MENU_HELP_INGAME:

		formLogoInit(menuLogoImg);

//		formTitleInit("Ayuda");
		formTextInit(gameText[TEXT_HELP]);

		formListClear();
		
		formListAdd(0, gameText[TEXT_HELP_OBJETIVO], 						MENU_ACTION_SHOW_HELP_1);
		formListAdd(0, gameText[TEXT_HELP_COMO_JUGAR], 					MENU_ACTION_SHOW_HELP_2);
		formListAdd(0, gameText[TEXT_HELP_COMO_GANAR], 					MENU_ACTION_SHOW_HELP_3);
		formListAdd(0, gameText[TEXT_HELP_DESACTIVAR_TUTORIAL], MENU_ACTION_SHOW_HELP_4);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK_TO_GAME, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_SCROLL_HELP:

		formPiliItem = PILI_INFO;

		formLogoInit(menuLogoImg);

//		formTitleInit(gameText[TEXT_HELP][0]);
		formTextInit(gameText[TEXT_HELP]);

		formListClear();
		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		forceRender();

	// Leemos archivo de ayuda
		String[][] tmpText = textosCreate(loadFile("help.txt"));

		formListClear();
		formListAdd(0, tmpText[pos]);

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
	
	case MENU_SCROLL_HELP1:

		formLogoInit(menuLogoImg);
		
		formPiliItem = PILI_INFO;

		formTextInit(gameText[TEXT_HELP_OBJETIVO]);
		
	  formListClear();
		formListAdd(0, AttachText(gameText, TEXT_HELP_OBJETIVO_S, 4));

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
	
	case MENU_SCROLL_HELP2:

	  formLogoInit(menuLogoImg);

		formPiliItem = PILI_INFO;

	  formTextInit(gameText[TEXT_HELP_COMO_JUGAR]);
	  
		formListClear();
		formListAdd(0, AttachText(gameText, TEXT_HELP_COMO_JUGAR_S, 2));

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
	
	case MENU_SCROLL_HELP3:

	  formLogoInit(menuLogoImg);
	  
		formPiliItem = PILI_INFO;

	  formTextInit(gameText[TEXT_HELP_COMO_GANAR]);
	  
		formListClear();
		formListAdd(0, AttachText(gameText, TEXT_HELP_COMO_GANAR_S, 5));

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
	
	case MENU_SCROLL_HELP4:

		formLogoInit(menuLogoImg);
		
		formPiliItem = PILI_INFO;

		formTextInit(gameText[TEXT_HELP_DESACTIVAR_TUTORIAL]);
		
	  formListClear();
		formListAdd(0, AttachText(gameText, TEXT_HELP_DESACTIVAR_TUTORIAL_S, 3));

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;

	case MENU_SCROLL_ABOUT:

		formLogoInit(menuLogoImg);

		formPiliItem = PILI_INFO;

//		formTitleInit(gameText[TEXT_ABOUT][0]);
		formTextInit(gameText[TEXT_ABOUT]);

		formListClear();
		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		forceRender();

		formListClear();
		formListAdd(0, gameText[TEXT_ABOUT_SCROLL]);

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;


	case MENU_ASK:

		formLogoInit(menuLogoImg);

//		formTitleInit(gameText[TEXT_CONFIRMATION][0]);
		formTextInit(gameText[TEXT_CONFIRMATION]);

		formListClear();
		formListAdd(0, gameText[TEXT_ARE_YOU_SURE][0], MENU_ACTION_ASK);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NO, SOFTKEY_YES);
	break;


	case MENU_EDIT_NAMES:

		formLogoInit(menuLogoImg);

//		formTitleInit(gameText[TEXT_NAMES][0]);
		formTextInit(gameText[TEXT_NAMES]);

		formListClear();
		for (int i=0 ; i<gamePlayerNames.length ; i++)
		{
			formListAdd( (i==0 || i==3? 0x01:0x00), gamePlayerNames[i], MENU_ACTION_INPUT_NAME);
		}
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_EDIT);
	break;


	case MENU_INPUT_NAME:

		formLogoInit(menuLogoImg);

		formPiliItem = -1;

//		formTitleInit(gameText[TEXT_ENTER_NAME][0]);
//		formTextInit(new String[] {gamePlayerNames[menuEditPlayerSelected]});

		formTextInit(gameText[TEXT_ENTER_NAME]);

		formInit(FORM_EDIT_NAME, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_INTRODUCIR);
	break;


	case MENU_CONFIG_EQUIPOS:
	    
	  // Forzamos configuraci�n de modo competici�n
	  if (gameMode == GAME_MODE_COMPETITION)
	  {
	      gameConfig[0] = CONTROL_PLAYER;
	      gameConfig[1] = CONTROL_CPU;
	      gameConfig[2] = CONTROL_CPU;
	      gameConfig[3] = CONTROL_CPU;
	  }  
	    
	  String[][] NombresJugadores = new String[MAX_PLAYERS][];

 		formLogoInit(menuLogoImg);
		formListClear();

	  // Configuraci�n de las opciones del men�
	  NombresJugadores[0] = new String[1];
	  NombresJugadores[1] = new String[2];
	  NombresJugadores[2] = new String[2];
	  if (gameMode == GAME_MODE_COMPETITION)
	  {
//		    formTitleInit(gameText[TEXT_EQUIPOS][0]);
		    formTextInit(gameText[TEXT_EQUIPOS]);
	      NombresJugadores[2] = new String[2];
	      NombresJugadores[3] = new String[2];
	  }	else
	  {
//		    formTitleInit(gameText[TEXT_INDIVIDUAL][0]);
		    formTextInit(gameText[TEXT_INDIVIDUAL]);
	      NombresJugadores[2] = new String[3];
	      NombresJugadores[3] = new String[3];
	  }
	  //System.out.println("-------------");
	  for (int NumNombre = 0; NumNombre < MAX_PLAYERS; NumNombre++)
	  {
	      // Control del contenido del array
	      for (int NumOption=0; NumOption < NombresJugadores[NumNombre].length; NumOption++)
	      {
              NombresJugadores[NumNombre][NumOption] = gameText[TEXT_PLAYER_CONFIG][NumOption];
              System.out.println(gameText[TEXT_PLAYER_CONFIG][NumOption]);
	      }
	  }
	  //System.out.println("-------------");
		
		formListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAYER_CONFIG, 0);
		
		for (int NumOption=0 ; NumOption<MAX_PLAYERS ; NumOption++)
		{
		    // T�tulos de equipo
		    if ((gameMode == GAME_MODE_COMPETITION) && (NumOption == 0))
		    {
		        formListAdd(POPUP_READ_ONLY_OPTION, gamePlayerNames[0], MENU_ACTION_PLAYER_CONFIG_CHG);
		        System.out.println(gamePlayerNames[0]);
		    }
		    if ((gameMode == GAME_MODE_COMPETITION) && (NumOption == 2))
		    {
		        formListAdd(POPUP_READ_ONLY_OPTION, gamePlayerNames[3], MENU_ACTION_PLAYER_CONFIG_CHG);
		        System.out.println(gamePlayerNames[3]);
		    }
		        
		    formListAdd(0, NombresJugadores[NumOption], MENU_ACTION_PLAYER_CONFIG_CHG, gameConfig[NumOption]);
		    //System.out.println("Player "+NumOption+" "+NombresJugadores[NumOption][0]);
		}
		
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_NONE);
	break;
//	INTRANCE
	case MENU_TORNEO :
		formLogoInit(menuLogoImg);

//		formTitleInit("TORNEOS");
		formTextInit(gameText[TEXT_MENU_TORNEOS]);

		formListClear();
		formListAdd(0x0, gameText[TEXT_TORNEOS][0], MENU_ACTION_TORNEO0);
		formListAdd(((TorneoLock>>1)&0x01), gameText[TEXT_TORNEOS][1], ((((TorneoLock>>1)&0x01)!=0)?MENU_ACTION_TORNEO_LOCKED:MENU_ACTION_TORNEO1));
		formListAdd(((TorneoLock>>2)&0x01), gameText[TEXT_TORNEOS][2], ((((TorneoLock>>2)&0x01)!=0)?MENU_ACTION_TORNEO_LOCKED:MENU_ACTION_TORNEO2));
		formListAdd(((TorneoLock>>3)&0x01), gameText[TEXT_TORNEOS][3], ((((TorneoLock>>3)&0x01)!=0)?MENU_ACTION_TORNEO_LOCKED:MENU_ACTION_TORNEO3));
		

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
		
	break;
//	INTRANCE

	case MENU_LOADING:

		formLogoInit(menuLogoImg);

		formPiliItem = -1;

		formListClear();
		formListAdd(0, gameText[TEXT_LOADING]);

		formInit(FORM_LIST, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

		forceRender();
	break;



//#ifdef DEBUG
	case MENU_DEBUG:

		formPiliItem = -1;

		formListClear();
		formListAdd(0, "Subir Skore", MENU_ACTION_DEBUG_UPLOAD_SCORE);

		formListAdd(0, "DEBUG", MENU_ACTION_NONE);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);
		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
//#endif
	}

	biosStatusOld = biosStatus;
	biosStatus = BIOS_MENU;
}


// -------------------
// menu Release
// ===================

public void menuRelease()
{
// Sistema Stack para los menus
	menuStack[menuStackIndex][0] = menuType;
	menuStack[menuStackIndex][1] = formListPos;
	if (++menuStackIndex >= menuStack.length) {menuStackIndex -= menuStack.length;}

// Limpiamos pantalla
//	menuListSet_None();
	listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	forceRender();

	formRelease();
}


// -------------------
// menu Init Back
// ===================

public void menuInitBack()
{
// Sistema Stack para los menus
	if ( (menuStackIndex-=2) < 0) {menuStackIndex += menuStack.length;}
	menuInit(menuStack[menuStackIndex][0], menuStack[menuStackIndex][1]);
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{
	switch (cmd)
	{
	
	// BIGTHOR - MENUS
	case MENU_ACTION_PLAYER_CONFIG_CHG:
	    if (gameMode == GAME_MODE_ARCADE)
	    {
	        gameConfig[formListPos-1] = (byte)formListOpt();
	    } else
	    {
	        if ((formListPos > 1) && (formListPos < 4))
	        {
	            gameConfig[formListPos-2] = (byte)formListOpt();
	        } else
	        {
			        if (formListPos > 4)
			        {
			            formListOpt();
			            gameConfig[formListPos-3] = (byte)formListOpt();
			        }
		      }
	    }	
	break;

	case MENU_ACTION_PLAYER_CONFIG:
		menuRelease();

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;	
	break;

	case MENU_ACTION_BACK:	// Ir "Atras" entre los menus

		menuRelease();
		menuInitBack();
	break;


	case MENU_ACTION_BACK_TO_GAME:	// Ir "Atras" al campo de juego

		menuRelease();
		listenerInit(playLeftSoftkeyOld, playRightSoftkeyOld);
		menuExit = true;
	break;


	case -3: // Scroll o Screen ha sido abortado por usuario
	case -2: // Scroll o Screen ha llegado al final

		menuRelease();
		menuInitBack();
	break;
	
	case MENU_ACTION_NEW_GAME:
	
	    menuRelease();
	    menuInit(MENU_NEW_GAME);
  break;

	case MENU_ACTION_EQUIPOS:		// Jugar
		/*menuRelease();

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;*/

		gameMode = GAME_MODE_COMPETITION; // Modo COMPETICI�N por equipos
	  
	  menuRelease();
		menuInit(MENU_CONFIG_EQUIPOS);
	break;

	case MENU_ACTION_INDIVIDUAL:

 		gameMode = GAME_MODE_ARCADE; // Modo COMPETICI�N por equipos

		menuRelease();
		menuInit(MENU_CONFIG_EQUIPOS);
	break;
	
	case MENU_ACTION_VS1:
	  gameMode = GAME_MODE_ARCADE;

	  gameConfig[0] = CONTROL_PLAYER;
	  gameConfig[1] = CONTROL_CPU;
	  gameConfig[2] = CONTROL_NONE;
	  gameConfig[3] = CONTROL_NONE;
		
		menuRelease();

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;

	case MENU_ACTION_VS2:
	  gameMode = GAME_MODE_ARCADE;

	  gameConfig[0] = CONTROL_PLAYER;
	  gameConfig[1] = CONTROL_CPU;
	  gameConfig[2] = CONTROL_CPU;
	  gameConfig[3] = CONTROL_NONE;

		menuRelease();

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;
	
	case MENU_ACTION_VS3:
	  gameMode = GAME_MODE_ARCADE;

	  gameConfig[0] = CONTROL_PLAYER;
	  gameConfig[1] = CONTROL_CPU;
	  gameConfig[2] = CONTROL_CPU;
	  gameConfig[3] = CONTROL_CPU;
	  
		menuRelease();

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;
	
	case MENU_ACTION_VS_Equipos:
    gameMode = GAME_MODE_COMPETITION;

	  gameConfig[0] = CONTROL_PLAYER;
	  gameConfig[1] = CONTROL_CPU;
	  gameConfig[2] = CONTROL_CPU;
	  gameConfig[3] = CONTROL_CPU;
	    
		menuRelease();

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;
	
	case MENU_ACTION_PLAY:		// Jugar

		menuRelease();

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_OPTIONS:		// Mostramos Opciones

		menuRelease();

		menuInit(MENU_OPTIONS);
	break;
	
	case MENU_ACTION_MORE_GAMES:
	
	    menuRelease();
	    
	    menuInit(MENU_MORE_GAMES);
	break;	    

	case MENU_ACTION_HELP:		// Mostramos Menu de Instrucciones...

		menuRelease();

		menuInit(MENU_HELP);
	break;
//	INTRANCE
	case MENU_ACTION_CONTINUE_TORNEO:
		popupClear();
	    CargandoTorneo = true;
	    popupSetArea(formBodyX,formBodyY,formBodyWidth,formBodyHeight);
		popupInit(POPUP_TORNEO, POPUP_ACTION_EXIT, POPUP_ACTION_PLAY_RONDA, SOFTKEY_BACK, SOFTKEY_CONTINUE);
	    LoadTorneo();
	break;
	case MENU_ACTION_TORNEOS:
	    CargandoTorneo = false;
	    menuRelease();
		menuInit(MENU_TORNEO);
	break;
	case MENU_ACTION_TORNEO_LOCKED:
	    popupMessage(gameText[TEXT_SUPERA_TORNEOS]);
	    biosWait();
	break;
//	INTRANCE
	case MENU_ACTION_PARTIDA_RAPIDA:
	  // BIGTHOR - AQU�  
	    
	    menuRelease();
	    
	    menuInit(MENU_PARTIDA_RAPIDA);
	break;

	case MENU_ACTION_HI_SCORES:

	// Preguntamos si se quiere conectar a red para tener los resultados de los scores
		popupInitAsk("Confirmacion", "Se va a proceder a descargar la lista del servidor. �Desea conectar a red?", POPUP_ACTION_EXIT);
		biosWait();

	// Si cancelamos, nos quedamos en el menu en el que estamos
		if (!popupResult) {break;}


	// Deshabilitamos que se salte al estado de PAUSA
		biosPauseEnabled = false;

		connectRequest(DOWNLOAD_HISCORE);


	// Lanzamos conexion para descargar scores
		popupClear();
		popupTextStr = new String[] {"Conectando..."};
		popupBorderGlobe = POPUP_BORDER_CASUAL_GRIS;
		popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
		popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_NONE, SOFTKEY_CANCEL, SOFTKEY_NONE);

		if (connectWait() > 0)
		{
			menuRelease();

			menuInit(MENU_HI_SCORES);
		}

	break;

	case MENU_ACTION_SHOW_HELP:		// Mostramos Instrucciones...

		menuRelease();
		
		menuInit(MENU_SCROLL_HELP, formListPos);
	break;
	case MENU_ACTION_SHOW_HELP_1:
	    
		menuRelease();
		
		menuInit(MENU_SCROLL_HELP1, formListPos);
	break;
	case MENU_ACTION_SHOW_HELP_2:
	    
		menuRelease();
		
		menuInit(MENU_SCROLL_HELP2, formListPos);
	break;
	case MENU_ACTION_SHOW_HELP_3:
	    
		menuRelease();
		
		menuInit(MENU_SCROLL_HELP3, formListPos);
	break;
	case MENU_ACTION_SHOW_HELP_4:
	    
		menuRelease();
		
		menuInit(MENU_SCROLL_HELP4, formListPos);
	break;
	


	case MENU_ACTION_SHOW_ABOUT:	// Mostramos About...

		menuRelease();

		menuInit(MENU_SCROLL_ABOUT);
	break;


	case MENU_ACTION_RESTART:	// Provocamos GAME OVER desde menu (preguntamos)

		popupSetArea(formBodyX + (formBodyWidth/6), formBodyY + (formBodyHeight/6), formBodyWidth - (formBodyWidth/3), formBodyHeight - (formBodyHeight/3));
		popupInitAsk(gameText[TEXT_CONFIRMATION][0], gameText[TEXT_ARE_YOU_SURE][0], POPUP_ACTION_EXIT);

		biosWait();

		if (popupResult)
		{
			menuRelease();
			
			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
			playExit = 3;		// flag para SALIDA de juego tipo GAME OVER
			menuExit = true;
		}
	break;


	case MENU_ACTION_EXIT_GAME:	// Exit Game???

		popupSetArea(formBodyX + (formBodyWidth/6), formBodyY + (formBodyHeight/6), formBodyWidth - (formBodyWidth/3), formBodyHeight - (formBodyHeight/3));
		popupInitAsk(gameText[TEXT_CONFIRMATION][0], gameText[TEXT_ARE_YOU_SURE][0], POPUP_ACTION_EXIT);

		biosWait();

		forceRender();

		if (popupResult)
		{
			menuRelease();
			menuExit = true;

			gameStatus = GAME_EXIT;
		}
	break;



	case MENU_ACTION_SOUND_CHG:

		gameSound = formListOpt() != 0;
		if (!gameSound) {soundStop();}
		else
		{
		    if (menuType == MENU_MAIN) {soundPlay(MUSICA_MENUS, 0);}
// -----------
// Si el juego tiene musica durante el juego:
//		else
		    if (menuType == MENU_INGAME && soundLoop == 0) {soundPlay(MUSICA_MENUS,0);}
		}
// ===========
	break;

//	INTRANCE
	case MENU_ACTION_TORNEO3 :
	case MENU_ACTION_TORNEO2 :
	case MENU_ACTION_TORNEO1 :
	case MENU_ACTION_TORNEO0 :
	    //menuRelease();
	    if (SavedTorneo >= 0)
	    {
	        popupInitAsk(gameText[TEXT_TORNEO_EN_CURSO][0],gameText[TEXT_TORNEO_EN_CURSO][1],POPUP_ACTION_EXIT);
	        biosWait();
	        if (!popupResult)
	        {
	            break;
	        } else
	        {
	            BorrarTorneo();
	        }
	    }
	    
		popupClear();
	    popupSetArea(formBodyX,formBodyY,formBodyWidth,formBodyHeight);
	    popupInit(POPUP_TORNEO, POPUP_ACTION_EXIT, POPUP_ACTION_PLAY_RONDA, SOFTKEY_BACK, SOFTKEY_CONTINUE);
	    CreateTorneo(cmd-MENU_ACTION_TORNEO0);
	break;
//	INTRANCE
	
	case MENU_ACTION_VIBRA_CHG:

		gameVibra = formListOpt() != 0;
		if (gameVibra) {vibraInit(300);}
	break;


	case MENU_ACTION_ASK:

		menuAction(menuActionAsk);
	break;

	case MENU_ACTION_GAME_DIFFICULTY_CHG:
		gameDifficulty = (byte)formListOpt();
	break;

	case MENU_ACTION_GAME_POINTS_CHG:
		gamePoints = (byte)formListOpt();
	break;

	case MENU_ACTION_GAME_CONTROL_CHG:
		gameControl = (byte)formListOpt();
	break;

	case MENU_ACTION_GAME_WITH_HELP_CHG:
		gameWithHelp = (byte)formListOpt();
	break;

	case MENU_ACTION_GAME_RULES_CHG:
		gameRules = (byte)formListOpt();
	break;


	case MENU_ACTION_EDIT_NAMES:

		menuRelease();
		menuInit(MENU_EDIT_NAMES);
	break;

	case MENU_ACTION_INPUT_NAME:

		menuEditPlayerSelected = formListPos;
		menuRelease();
		menuInit(MENU_INPUT_NAME);
	break;


	case MENU_ACTION_MASJUEGOS:

		popupInitInfo(null, new String[] {"Aki va el texto que explica como comprar el juego"}, -1);
//		popupInitInfo(null, new String[] {"Por favor... comprame, somos 40 vocas que alimentar...","Por favor... comprame, somos 40 vocas que alimentar...","Por favor... comprame, somos 40 vocas que alimentar...","Por favor... comprame, somos 40 vocas que alimentar...","Por favor... comprame, somos 40 vocas que alimentar..."}, -1);
		biosWait();
	break;





//#ifdef DEBUG
	case MENU_ACTION_DEBUG:

		menuRelease();
		menuInit(MENU_DEBUG);
	break;

	case MENU_ACTION_DEBUG_UPLOAD_SCORE:

		connectRequest(UPLOAD_HISCORE);
	break;
//#endif
	}

}


// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if (formTick()) {menuAction(formListCMD);}

	if (menuExit) {return true;}

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











// *******************
// -------------------
// popup - Engine
// ===================
// *******************
// -------------------

static final int POPUP_READ_ONLY_OPTION	= 0x11;

// -----------------------------------------------
// Variables staticas para identificar las acciones de los popups...
// ===============================================
static final int POPUP_ACTION_NONE = 0;
static final int POPUP_ACTION_EXIT = 1;
static final int POPUP_ACTION_PLAY_RONDA = 2;
// ===============================================


// -----------------------------------------------
// Constantes para ajustar el aspa del globo
// ===============================================
static final int POPUP_ASPA_DOWN_LEFT = 0;
static final int POPUP_ASPA_DOWN_RIGHT = 1;
static final int POPUP_ASPA_TOP_LEFT = 2;
static final int POPUP_ASPA_TOP_RIGHT = 3;
static final int POPUP_ASPA_DOWN_CENTER = 0x81;
static final int POPUP_ASPA_TOP_CENTER = 0x83;
// ===============================================


// -----------------------------------------------
// Variables para definir el tipo de borde a mostrar en los popups
// ===============================================
static final int POPUP_BORDER_MARCO = 0;
static final int POPUP_BORDER_BOCADILLO = 1;
static final int POPUP_BORDER_CASUAL_NARANJA = 2;
static final int POPUP_BORDER_CASUAL_GRIS = 3;
static final int POPUP_BORDER_CASUAL_MARRON = 4;
// ===============================================


// -----------------------------------------------
// Constantes que definen los tipos de popup
// ===============================================
static final int POPUP_ASK = 0;
static final int POPUP_OPTION = 1;
static final int POPUP_SORTEO = 2;
static final int POPUP_IMAGE = 3;
static final int POPUP_TABLE = 4;
//INTRANCE
static final int POPUP_TORNEO = 5;
static final int POPUP_RECUENTO = 6;
static final int POPUP_RESULTADOS = 7;
//INTRANCE
// ===============================================


// -----------------------------------------------
// Constantes que definen el tama�o de las esquinas redondeadas de los popup
// ===============================================
//#ifdef S40
//#elifdef S60
static final int POPUP_MARC_WIDTH = 18;
static final int POPUP_MARC_HEIGHT = 18;
static final int POPUP_MARC_BORDER = 6;

//#elifdef S80
//#endif
// ===============================================


// -----------------------------------------------
// Constantes para ajustar el tama�o de la barra desplazadora (slider de scrolls)
// ===============================================
static final int POPUP_SLIDER_WIDTH = 6;
static final int POPUP_SLIDER_HEIGHT = 6;
// ===============================================


// -----------------------------------------------
// Constantes para indicar el frame correspondiente en la imagen de sprites
// ===============================================
static final int POPUP_ITEM_ASPA = 0x09;
static final int POPUP_ITEM_TITLE_LOGO = 0x18;
static final int POPUP_ITEM_MORE = 0x0D;
static final int POPUP_ITEM_CASUAL = 0x0F;
// ===============================================


// -----------------------------------------------
// Definicion de los colores usados en popups (RGB)
// ===============================================
int POPUP_COLOR_BORDER = 0x8f6444;		// Marron
int POPUP_COLOR_TITLE = 0xff9c39;		// Naranja
int POPUP_COLOR_BACKGROUND = 0x7a948b;	// Gris

int POPUP_COLOR_NARANJA = 0xff9c39;		// Naranja
int POPUP_COLOR_GRIS = 0x7a948b;		// Gris
int POPUP_COLOR_MARRON = 0x8f6444;		// Marron
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


// Variables para la gestion del tama�o y posicion en pantalla del icono de titulo
int popupTitleIconX;
int popupTitleIconY;
int popupTitleIconWidth;
int popupTitleIconHeight;
int popupTitleIconNum;


// Variables para la gestion del tama�o y posicion en pantalla del icono de "mas/siguiente pagina"
int popupMoreIconX;
int popupMoreIconY;
int popupMoreIconWidth;
int popupMoreIconHeight;


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
boolean popupHslider;
int popupHsliderX;
int popupHsliderWidth;


// Variables para la gestion del popup: ASK
boolean popupResult;	// Resultado del popup (true: Cancel / false: Acept)


// Variables para la gestion del popup: SORTEO
byte[] popupSorteoCor;
//#ifdef J2ME
	Image popupSorteoImg;
//#elifdef DOJA
//#endif
int popupSorteoCnt;		// numero de rotaciones a ejecutar en el sorteo
int popupSorteoRot;		// Para la gestion de la rotacion de la ruleta del sorteo
int popupSorteoRotCnt;


// Varaibles para la gestion del popup: IMAGE
Image popupImageImg;	// Imagen a mostar en el popup


// Varaibles para la gestion del popup: TABLE
int popupTableCellWidth;
int popupTableCellHeight;
int popupTableLeftWidth;
int popupTableIni;
int popupTablePos = -1;		// Si vale "-1" es disabled (no se ve ninguna opcion marcada)
int popupTableView;
String[] popupTableTopStr;
String[] popupTableLeftStr;


// Variables internas para la gestion del popupEngine
boolean popupShow = false;
boolean popupRunning = false;
int popupBorderGlobe;
int popupAspa;
boolean popupIconEnable;
boolean popupExit;
int popupType;				// Tipo de popup (ASK, OPTION, SORTEO...)
int popupMode;				// Modo en el que nos encontramos (rotando sorteo, mostrando el ganador un rato, etc...)
int popupActionCancel;
int popupActionAcept;
int popupLeftSoftkeyBack;
int popupRightSoftkeyBack;

	byte[] popupItemsCor;
//#ifdef J2ME
	Image popupItemsImg;
//#elifdef DOJA
//#endif


// Intrance - Variable de popup por tiempo
int popupLifeTime = -1;

final static int STANDAR_LIFE_TIME	= 40;

// -------------------
// popup Ask
// ===================

public void popupAsk(String titleStr, String str, int actionAcept)
{
	popupTitleStr = titleStr;
	popupTextStr = new String[] {str};
	popupBorderGlobe = POPUP_BORDER_MARCO;
	popupIconEnable = false;
	popupInit(POPUP_ASK, POPUP_ACTION_EXIT, actionAcept, SOFTKEY_NO, SOFTKEY_YES);
}

// -------------------
// popup Robar Pasar
// ===================

public void popupRobarPasar()
{
    // Si controla el player revisar si puedo tirar ficha
    listenerInit(SOFTKEY_MENU, SOFTKEY_LANZAR);
    if (!puedoTirarAlgunaFicha())
    {
        if (puedoRobar())
        {
            popupMessage(gameText[TEXT_DEBES_ROBAR]);
            listenerInit(SOFTKEY_MENU, SOFTKEY_ROBAR);
        } else
        {
            popupMessage(gameText[TEXT_DEBES_PASAR]);
            listenerInit(SOFTKEY_MENU, SOFTKEY_PASAR);
        }
    }
}

// -------------------
// popup Info :: Mostramos popup de tipo 'bocadillo' con informacion generica
// ===================

public void popupInfo(String[] str)
{
  biosRefresh();
  forceRender();
	//popupLifeTime			=	STANDAR_LIFE_TIME;
	popupTitleStr 		= null;
	popupTextStr 			= str;
	popupBorderGlobe 	= POPUP_BORDER_BOCADILLO;
	popupIconEnable 	= false;
	popupAspa 				= POPUP_ASPA_DOWN_LEFT;
	//popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, listenerIdLeft, listenerIdRight);
	popupInit(POPUP_ASK, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_CONTINUE);
	biosWait();
}

public void popupMessage(String[] str)
{
  popupMessage(null,str);
}


public void popupMessage(String Titulo, String[] str)
{
  biosRefresh();
  forceRender();
	//popupLifeTime			=	STANDAR_LIFE_TIME;
	popupTitleStr 		= Titulo;
	popupTextStr 			= str;
	popupBorderGlobe 	= POPUP_BORDER_CASUAL_NARANJA;
	popupIconEnable 	= false;
	//popupAspa 				= POPUP_ASPA_DOWN_LEFT;
	//popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, listenerIdLeft, listenerIdRight);
	popupInit(POPUP_ASK, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_CONTINUE);
	biosWait();
}

public void popupTutorial(String[] str)
{
  biosRefresh();
  forceRender();
	//popupLifeTime			=	STANDAR_LIFE_TIME;
	popupTitleStr 		= null;
	popupTextStr 			= str;
	popupBorderGlobe 	= POPUP_BORDER_CASUAL_NARANJA;
	popupIconEnable 	= false;
	//popupAspa 				= POPUP_ASPA_DOWN_LEFT;
	//popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, listenerIdLeft, listenerIdRight);
	popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, SOFTKEY_JUGAR, SOFTKEY_MAS);
	//popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, 0, 0);
	biosWait();
}
// -------------------
// popup Color
// ===================

public void popupColorCasual()
{
	popupColor(FORM_COLOR_MARC, FORM_COLOR_BOX_SELECTED, FORM_COLOR_BOX);
}

public void popupColor(int marcRGB, int titleRGB, int backgroundRGB)
{
	POPUP_COLOR_BORDER = marcRGB;
	POPUP_COLOR_TITLE = titleRGB;
	POPUP_COLOR_BACKGROUND = backgroundRGB;
}


// -------------------
// popup Init Ask
// ===================

public void popupInitAsk(String titleStr, String str, int actionAcept)
{
	popupClear();
	popupTitleStr = titleStr;
	popupTextStr = new String[] {str};
	popupBorderGlobe = POPUP_BORDER_CASUAL_GRIS;
	popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
	popupInit(POPUP_ASK, POPUP_ACTION_EXIT, actionAcept, SOFTKEY_NO, SOFTKEY_YES);
}

// -------------------
// popup Init Ok
// ===================

public void popupInitAcept(String titleStr, String str)
{
	popupClear();
	popupTitleStr = titleStr;
	popupTextStr = new String[] {str};
	popupBorderGlobe = POPUP_BORDER_CASUAL_GRIS;
	popupSetArea(1, canvasHeight - popupHeight - 1, popupWidth, popupHeight);
	popupInit(POPUP_ASK, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_ACEPT);
}


// -------------------
// popup Init Image
// ===================

public void popupInitImage(String titleStr, Image img)
{
	popupClear();
	popupTitleStr = titleStr;
	popupImageImg = img;
	popupInit(POPUP_IMAGE, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_ACEPT);
}


// -------------------
// popup Init Info
// ===================

public void popupInitInfo(String titleStr, String[] str)
{
	popupInitInfo(titleStr, str, 0);
}

public void popupInitInfo(String titleStr, String[] str, int icon)
{
	popupClear();
	popupTitleStr = titleStr;
	popupTextStr = str;
	popupBorderGlobe = POPUP_BORDER_CASUAL_NARANJA;
	if (icon >= 0)
	{
		popupIconEnable = true;
		popupTitleIconNum = icon;
	}
	popupInit(POPUP_ASK, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_ACEPT);
}


// -------------------
// popup Init Glove
// ===================

public void popupInitGlove(String[] str)
{
	popupInitGlove(str, 7, POPUP_ASPA_DOWN_LEFT);
}

public void popupInitGlove(String[] str, int icon, int aspa)
{
	popupClear();
	popupTextStr = str;
	popupBorderGlobe = POPUP_BORDER_BOCADILLO;
	if (icon>=0) popupIconEnable = true; else popupIconEnable = false;
	popupTitleIconNum = icon;
	popupAspa = aspa;
	popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_CONTINUE);
}

public void popupInitGlove(String[] str, int icon, int aspa, int X, int Y, int W, int H)
{
    popupSetArea(X,Y,W,H);
	popupInitGlove(str,icon,aspa);
}


// -------------------
// popup Init Sorteo
// ===================

public void popupInitSorteo(String[] str)
{
	popupClear();
	popupTitleStr = gameText[TEXT_SORTEO][0];
	popupTextStr = str;
	popupBorderGlobe = POPUP_BORDER_CASUAL_MARRON;
	popupInit(POPUP_SORTEO, POPUP_ACTION_NONE, POPUP_ACTION_NONE, SOFTKEY_NONE, SOFTKEY_NONE);
}


// -------------------
// popup Turno
// ===================

boolean primerTurno;

public void popupTurno()
{
    String[] MensajeTurno = new String[3];
    
    popupTitleStr 		= null;
    if (primerTurno)
    {
        MensajeTurno[0]		= gameText[TEXT_EMPIEZA_PARTIDA][0] + " " + gamePlayerNamesOnly[ActualPlayer];
        primerTurno				= false;
    } else
    {
        MensajeTurno[0]  		= gameText[TEXT_TURNODE][0] + " " + gamePlayerNamesOnly[ActualPlayer];
    }
    MensajeTurno[1] = " ";
    
    if (gameConfig[ActualPlayer]==CONTROL_PLAYER)
    {
        MensajeTurno[2] 	= gameText[TEXT_PASAMOVIL][0];
    } else
    {
        MensajeTurno[2] 	= "";
        popupLifeTime			=	STANDAR_LIFE_TIME;
    }
    
    popupTextStr = MensajeTurno;
    
    popupBorderGlobe	= POPUP_BORDER_MARCO;
    popupIconEnable		= false;
    
    // Lo mostramos menos un 10% de la pantalla
    popupSetAreaBig();
    // ---
    
    popupInit(POPUP_ASK, POPUP_ACTION_EXIT, POPUP_ACTION_EXIT, 0, SOFTKEY_CONTINUE);
    biosWait();    
   
    popupSetAreaStandar();
}


// -------------------
// popup Init Menu
// ===================

public void popupInitMenu()
{
	popupClear();

	popupTitleStr = "Titulo";
	popupTextStr = null;
	popupBorderGlobe = POPUP_BORDER_MARCO;
	popupIconEnable = false;

	popupListClear();
	popupListAdd(0, gameText[TEXT_EQUIPOS], MENU_ACTION_EQUIPOS);
	popupListAdd(0, gameText[TEXT_INDIVIDUAL], MENU_ACTION_INDIVIDUAL);
	popupListAdd(0, new String[] {"uno","dos","tres"}, 2547, 0);
	popupListAdd(0, gameText[TEXT_OPTIONS], MENU_ACTION_SHOW_OPTIONS);
	popupListAdd(0, gameText[TEXT_HELP], MENU_ACTION_HELP);
	popupListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
	popupListAdd(0, new String[] {"uno","dos","tres"}, 2547, 0);
	popupListAdd(0, gameText[TEXT_OPTIONS], MENU_ACTION_SHOW_OPTIONS);
	popupListAdd(0, gameText[TEXT_HELP], MENU_ACTION_HELP);
	popupListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
	popupListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

	popupInit(POPUP_OPTION, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
}


// -------------------
// popup Init Table
// ===================

public void popupInitTable(String[][] Table)
{
    String[] TableTop  = new String[Table[0].length-1];
    String[] TableLeft = new String[Table.length-1];
    String[][] TableContent = new String[TableLeft.length][TableTop.length];
    System.arraycopy(Table[0],1,TableTop,0,TableTop.length);
    for (int ActualLine=0; ActualLine<TableLeft.length; ActualLine++) 
    {
        System.arraycopy(Table[ActualLine+1],1,TableContent[ActualLine],0,TableTop.length);
        TableLeft[ActualLine] = Table[ActualLine+1][0];
    }
    popupInitTable(TableTop,TableLeft,TableContent);
}

public void popupInitTable(String[] TableTop,String[] TableLeft,String[][] TableContent)
{
	popupClear();
	popupTableTopStr = TableTop;
	popupTableLeftStr = TableLeft;
	popupListStr = TableContent;
	popupInit(POPUP_TABLE, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_ACEPT);
}

public void popupInitPizarraTable(String[][] Table)
{
    String[] TableTop  = new String[Table[0].length-1];
    String[] TableLeft = new String[Table.length-1];
    String[][] TableContent = new String[TableLeft.length][TableTop.length];
    System.arraycopy(Table[0],1,TableTop,0,TableTop.length);
    for (int ActualLine=0; ActualLine<TableLeft.length; ActualLine++) 
    {
        System.arraycopy(Table[ActualLine+1],1,TableContent[ActualLine],0,TableTop.length);
        TableLeft[ActualLine] = Table[ActualLine+1][0];
    }
    popupInitPizarraTable(TableTop,TableLeft,TableContent);
}

public void popupInitPizarraTable(String[] TableTop,String[] TableLeft,String[][] TableContent)
{
    popupClear();
	popupTableTopStr = TableTop;
	popupTableLeftStr = TableLeft;
	popupListStr = TableContent;
	popupInit(POPUP_TABLE, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_ACEPT);
}

public void popupInitPizarraResultados()
{
    //POPUP_RESULTADO
}

public void popupInitPizarraRecuento()
{
    popupClear();
    popupSetArea(0, canvasTop, canvasWidth, CanvasScrollHeight);
	popupInit(POPUP_RECUENTO, POPUP_ACTION_NONE, POPUP_ACTION_EXIT, SOFTKEY_NONE, SOFTKEY_CONTINUE);
}

// -------------------
// popup Clear
// ===================

public void popupClear()
{
	popupTitleStr = null;
	popupTextStr = null;
	popupBorderGlobe = POPUP_BORDER_MARCO;
	popupIconEnable = false;
	popupImageImg = null;
}


// -------------------
// popup Create
// ===================

public void popupCreate()
{
	popupItemsCor = loadFile("/popupItems.cor");
	popupSorteoCor = loadFile("/sorteo.cor");

	//#ifdef J2ME
		popupItemsImg = loadImage("/popupItems");
		popupSorteoImg = loadImage("/sorteo");
	//#elifdef DOJA
	//#endif


	int width = canvasWidth - (canvasWidth / 5);
	int height = canvasHeight - (canvasHeight / 2);
	int x = (canvasWidth - width) / 2;
	int y = (canvasHeight - height) / 2;
	popupSetArea(x, y, width, height);
}


// -------------------
// popup Destroy
// ===================

public void popupDestroy()
{
}


// -------------------
// popup Set Area
// ===================

public void popupSetArea(int x, int y, int width, int height)
{
    //INTRANCE
    PizarraBodyX = popupX = x;
    PizarraBodyY = popupY = y;
    PizarraBodyW = popupWidth = width;
    PizarraBodyH = popupHeight = height;
	//INTRANCE
}

// -------------------
// popup setAreaStandar
// ===================

public void popupSetAreaStandar()
{
	int width = canvasWidth - (canvasWidth / 5);
	int height = canvasHeight - (canvasHeight / 2);
	int x = (canvasWidth - width) / 2;
	int y = (canvasHeight - height) / 2;
	popupSetArea(x, y, width, height);
}

// -------------------
// popup setAreaBig
// ===================

public void popupSetAreaBig()
{
	int width = canvasWidth - ((canvasWidth * 10) / 100);
	int height = canvasHeight - ((canvasHeight * 10) / 100);
	int x = ((canvasWidth * 5) / 100);
	int y = ((canvasHeight * 5) / 100);
	popupSetArea(x, y, width, height);
}

// -------------------
// popup Init
// ===================

public void popupInit(int type, int actionCancel, int actionAcept, int leftSoftkey, int rightSoftkey)
{
// Inicializapos la posision inicial
	popupTextIni = 0;
	popupListIni = 0;
	popupListPos = 0;
	popupTableIni = 0;

// Guardamos tipo de popup
	popupType = type;

// El popup arranca en modo inicial (0) Usado para la gestion de subModos dentro del tick...
	popupMode = 0;

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
		formFontInit(FORM_FONT_SMALL_WHITE);

		if (popupBorderGlobe == POPUP_BORDER_MARCO)
		{
			popupTitleHeight = printGetHeight() + 2;
		} else {
			popupTitleHeight = printGetHeight() + (FORM_MARC_HEIGHT / 2) + 2;
		}

		cursorY += popupTitleHeight;

		popupVsliderY += (popupTitleHeight - 1);
	}


// popupTitleIcon
	if (popupIconEnable)
	{
		popupTitleIconWidth = printSpriteWidth(popupItemsCor, 0x0A);
		popupTitleIconHeight = printSpriteHeight(popupItemsCor, 0x0A);
		popupTitleIconX = cursorX;
		popupTitleIconY = cursorY;
		cursorX += popupTitleIconWidth;
		cursorY += popupTitleIconHeight / 2;
	}


// popupBody
	popupBodyX = cursorX;
	popupBodyY = cursorY;
	popupBodyWidth = popupWidth - cursorX - POPUP_MARC_BORDER;
	popupBodyHeight = popupHeight - cursorY - POPUP_MARC_BORDER;


// popupType
	switch (popupType)
	{
	case POPUP_ASK:

		formFontInit(FORM_FONT_SMALL_BLACK);
	
		popupTextWidth = popupBodyWidth;
		popupTextHeight = popupBodyHeight;
	
		popupTextStr = printTextBreak(popupTextStr, popupTextWidth - POPUP_SLIDER_WIDTH);
	
		popupTextView = popupTextHeight / printGetHeight();
	
		if (popupTextView >= popupTextStr.length)
		{
			popupTextView = popupTextStr.length;
		} else {
			popupTextHeight -= printSpriteHeight(popupItemsCor, POPUP_ITEM_MORE);
			popupTextView = popupTextHeight / printGetHeight();
			popupVslider = true;
			popupTextWidth -= POPUP_SLIDER_WIDTH;
		}
	
		int tmpHeight = popupTextView * printGetHeight();
	
		popupTextX = popupBodyX;
		popupTextY = popupBodyY + ((popupTextHeight - tmpHeight) / 2);
	
		popupTextHeight = tmpHeight;
	break;


	case POPUP_OPTION:

		formFontInit(FORM_FONT_SMALL_BLACK);

		popupListView = popupBodyHeight / printGetHeight();
	
		if (popupListView >= popupListStr.length)
		{
			popupListView = popupListStr.length;
		} else {
			popupVslider = true;
			popupBodyWidth -= POPUP_SLIDER_WIDTH;
		}

		popupBodyY += (popupBodyHeight - (popupListView * printGetHeight()) ) / 2;
	break;


	case POPUP_SORTEO:

		popupSorteoCnt = 20 + rnd(20);

		popupSorteoRot = 0;
		popupSorteoRotCnt = 256;

		formFontInit(FORM_FONT_SMALL_BLACK);
		int newHeight = printSpriteHeight(popupSorteoCor, 0) + (printGetHeight() * 3);

		popupHeight += newHeight - popupBodyHeight;
		popupY -= (newHeight - popupBodyHeight) / 2;
		popupBodyHeight = newHeight;
	break;


	case POPUP_IMAGE:

		int packWidth = popupImageImg.getWidth() - popupBodyWidth;
		int packHeight = popupImageImg.getHeight() - popupBodyHeight;

		popupBodyWidth += packWidth;
		popupBodyHeight += packHeight;

		popupWidth += packWidth;
		popupHeight += packHeight;

		popupX -= packWidth / 2;
		popupY -= packHeight / 2;
	break;


	case POPUP_TORNEO:

	    popupPizarraY = Margen+ResultSeparator+TituloPizarraHeight*3+(ResultSeparator+printGetHeight())*PosJugador-PizarraBodyY+1-((PizarraBodyH-2)/2);
	    popupPizarraX = MinPizarraX = 0-PizarraBodyX-1;
	    MinPizarraY = 0-PizarraBodyY-1;
	    MaxPizarraX = TorneoW-PizarraBodyW-2-PizarraBodyX+1;
	    MaxPizarraY = TorneoH-PizarraBodyH-2-PizarraBodyY+1;
	    popupPizarraX = Math.max(MinPizarraX,Math.min(MaxPizarraX,popupPizarraX));
		popupPizarraY = Math.max(MinPizarraY,Math.min(MaxPizarraY,popupPizarraY));
	break;
	
	case POPUP_RECUENTO:
	    int RecuentoW = 300;
	    int RecuentoH = 300;
	    
	    popupPizarraY = MinPizarraY = 0-PizarraBodyY-1;
	    popupPizarraX = MinPizarraX = 0-PizarraBodyX-1;
	    MaxPizarraX = RecuentoW-PizarraBodyW-2-PizarraBodyX+1;
	    MaxPizarraY = RecuentoH-PizarraBodyH-2-PizarraBodyY+1;
	    popupPizarraX = Math.max(MinPizarraX,Math.min(MaxPizarraX,popupPizarraX));
		popupPizarraY = Math.max(MinPizarraY,Math.min(MaxPizarraY,popupPizarraY));
	break;
	
	case POPUP_RESULTADOS:
	    int ResultadosW = 300;
	    int ResultadosH = 300;
	    
	    popupPizarraY = MinPizarraY = 0-PizarraBodyY-1;
	    popupPizarraX = MinPizarraX = 0-PizarraBodyX-1;
	    MaxPizarraX = ResultadosW-PizarraBodyW-2-PizarraBodyX+1;
	    MaxPizarraY = ResultadosH-PizarraBodyH-2-PizarraBodyY+1;
	    popupPizarraX = Math.max(MinPizarraX,Math.min(MaxPizarraX,popupPizarraX));
		popupPizarraY = Math.max(MinPizarraY,Math.min(MaxPizarraY,popupPizarraY));
	break;


	case POPUP_TABLE:

		popupBodyX = 0;
		popupBodyY = 0;
		popupBodyWidth = popupWidth;
		popupBodyHeight = popupHeight;

		formFontInit(FORM_FONT_SMALL_BLACK);

		boolean firstPass = true;
		while(true)
		{
		// Calculamos los anchos maximos de todos los textos
			popupTableLeftWidth = textMaxSize(popupTableLeftStr) + 4;
			popupTableCellWidth = textMaxSize(popupTableTopStr) + 4;
			for (int i=0 ; i<popupListStr.length ; i++) {int width = textMaxSize(popupListStr[i]) + 4; if (popupTableCellWidth < width) {popupTableCellWidth = width;}}
	
		// Calculamos cuantas celdas podemos renderizar dento del popup
			popupTableView = (popupBodyWidth - popupTableLeftWidth) / popupTableCellWidth;
			if (popupTableView >= popupTableTopStr.length)
			{
				popupTableView = popupTableTopStr.length;
			}
			else if (!popupHslider)
			{
				popupHslider = true;
				popupBodyHeight -= POPUP_SLIDER_HEIGHT;
			}
	
		// Repartimos el espacio que sobra horizontalmente entre las celdas
			int sobra = popupBodyWidth - (popupTableLeftWidth + (popupTableView * popupTableCellWidth));
			int suma = sobra / (popupTableView + 1);
			popupTableCellWidth += suma;
			popupTableLeftWidth += sobra - (suma * popupTableView);
	
	
		// Calculamos la altura de las celdas
			popupTableCellHeight = printGetHeight() + 4;
	
		// Calculamos cuantas celdas podemos renderizar dento del popup
			popupListView = (popupBodyHeight / popupTableCellHeight) - 1;
			if (popupListView >= popupListStr.length)
			{
				popupListView = popupListStr.length;
			}
			else if (!popupVslider)
			{
				popupVslider = true;
				popupBodyWidth -= POPUP_SLIDER_WIDTH;
			}
	
		// Repartimos el espacio que sobra verticalmente entre las celdas
			sobra = popupBodyHeight - ((popupListView + 1) * popupTableCellHeight);
			suma = sobra / (popupListView + 1);
			popupTableCellHeight += suma;

		// Ajustamos altura del popup para que no sobre nada
			sobra = popupHeight - ((popupListView + 1) * popupTableCellHeight) - (popupHslider? POPUP_SLIDER_HEIGHT : 0);
			popupHeight -= sobra;
			popupBodyHeight -= sobra;
			popupY += sobra / 2;

			if (!popupVslider || !firstPass) {break;} else {firstPass = false;}
		}

		if (!popupVslider) {popupWidth++;}
		if (!popupHslider) {popupHeight++;}
	break;
	}


// popupVslider
	popupVsliderHeight = popupHeight;
	popupVsliderY = popupY;

// popupHslider
	popupHsliderWidth = popupWidth;
	popupHsliderX = popupX;


	if (popupVslider && popupHslider)
	{
		popupHsliderWidth -= (POPUP_SLIDER_WIDTH - 1);
		popupVsliderHeight -= (POPUP_SLIDER_HEIGHT - 1);
	}


	popupRunning = true;
	popupShow = true;

	popupExit = false;
	biosStatusOld = biosStatus;
	biosStatus = BIOS_POPUP;
}


// -------------------
// popup Release
// ===================

public void popupRelease()
{
	popupShow = false;
	popupRunning = false;

// Ponemos la indicacion de la linea en la tabla de "excel" a 'resaltar' a OFF.
	popupTablePos = -1;

	popupLifeTime = -1;
	listenerInit(popupLeftSoftkeyBack, popupRightSoftkeyBack);
	gameForceRefresh = true;

	int width = canvasWidth - (canvasWidth / 5);
	int height = canvasHeight - (canvasHeight / 2);
	int x = (canvasWidth - width) / 2;
	int y = (canvasHeight - height) / 2;
	popupSetArea(x, y, width, height);
}


// -------------------
// popup Tick
// ===================

public boolean popupTick()
{
	switch (popupType)
	{
	case POPUP_ASK:

    // Controlamos si acaba el tiempo de popup
        if ((popupLifeTime >= 0) && (--popupLifeTime < 0))
        {
            popupResult = false;
            popupAction(popupActionCancel);
        }

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
	break;


//		INTRANCE
	case POPUP_RESULTADOS:
	case POPUP_RECUENTO:
	case POPUP_TORNEO:

    // Controlamos si acaba el tiempo de popup
        if ((popupLifeTime >= 0) && (--popupLifeTime < 0))
        {
            popupResult = false;
            popupAction(popupActionCancel);
        }

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
		
		popupPizarraX += keyX*5; 
		popupPizarraY += keyY*5;
		popupPizarraX = Math.max(MinPizarraX,Math.min(MaxPizarraX,popupPizarraX));
		popupPizarraY = Math.max(MinPizarraY,Math.min(MaxPizarraY,popupPizarraY));
		if ((keyX!=0)||(keyY!=0)||(lastKeyX!=0)||(lastKeyY!=0)) popupShow = true;
	break;
//		INTRANCE


	case POPUP_OPTION:

	// Controlamos desplazamiento vertical
		if (keyY > 0 && lastKeyY == 0 && popupListPos < popupListStr.length-1) {popupListPos++; popupShow = true;}
		else
		if (keyY < 0 && lastKeyY == 0 && popupListPos > 0) {popupListPos--; popupShow = true;}

	// Controlamos si pulsamos desplazamiendo horizontal (solo para rotar opciones)
		if (keyX != 0 && lastKeyX == 0 && popupListStr[popupListPos].length > 1)
		{
			popupListDat[popupListPos][2] += keyX;
			if (popupListDat[popupListPos][2] < 0) {popupListDat[popupListPos][2] = popupListStr[popupListPos].length-1;}
			else
			if (popupListDat[popupListPos][2] == popupListStr[popupListPos].length) {popupListDat[popupListPos][2] = 0;}
			popupShow = true;
			popupAction(popupListDat[popupListPos][1]);
		}

	// Controlamos si pulsamos OK
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			if (++popupListDat[popupListPos][2] == popupListStr[popupListPos].length) {popupListDat[popupListPos][2] = 0;}
			popupShow = true;
			popupAction(popupListDat[popupListPos][1]);
		}

	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			popupAction(popupActionCancel);
		}
	break;


	case POPUP_SORTEO:

		switch (popupMode)
		{
		case 0:
			if (--popupSorteoCnt > 0 || popupSorteoRotCnt > 0)
			{
				int lastRot = popupSorteoRot>>8;

				popupSorteoRot += popupSorteoRotCnt;

				if (popupSorteoCnt <= 0)
				{
					popupSorteoRotCnt -= 4; if (popupSorteoRotCnt < 0) {popupSorteoRotCnt = 0;}
				}

				if (lastRot != popupSorteoRot>>8 && ++popupListPos >= popupTextStr.length) {popupListPos = 0;}

				popupShow = true;
			} else {
				waitStart();
				popupMode++;
			}
		break;

		case 1:
			if (waitFinished(2000)) {popupAction(POPUP_ACTION_EXIT);}
		break;
		}
	break;


	case POPUP_IMAGE:

    // Controlamos si acaba el tiempo de popup
        if ((popupLifeTime >= 0) && (--popupLifeTime < 0))
        {
            popupResult = true;
            popupAction(popupActionAcept);
        }

	// Controlamos si pulsamos softkey Izquierda
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			popupResult = true;
			popupAction(popupActionAcept);
		}
	break;


	case POPUP_TABLE:

	// Controlamos desplazamiento vertical
		if (keyY > 0 && lastKeyY == 0 && popupListPos < popupListStr.length - popupListView) {popupListPos++; popupShow = true;}
		else
		if (keyY < 0 && lastKeyY == 0 && popupListPos > 0) {popupListPos--; popupShow = true;}

	// Controlamos desplazamiento horizontal
		if (keyX > 0 && lastKeyX == 0 && popupTableIni < popupTableTopStr.length - popupTableView) {popupTableIni++; popupShow = true;}
		else
		if (keyX < 0 && lastKeyX == 0 && popupTableIni > 0) {popupTableIni--; popupShow = true;}

	// Controlamos si pulsamos OK
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
			popupAction(popupActionAcept);
		}

	// Controlamos si pulsamos CANCEL
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			popupAction(popupActionCancel);
		}
	break;
	}

	return popupExit;
}


// -------------------
// popup Draw
// ===================

public void popupDraw()
{
    boolean centrado = false;
    if ((popupAspa & 0x80) != 0) centrado = true;
	if (!popupShow) {return;} else {popupShow = false;}


// Renderizamos el cuerpo del popup (interior liso y bordes)
	switch (popupBorderGlobe)
	{
	case POPUP_BORDER_MARCO:

		fillDraw((popupType != POPUP_SORTEO? POPUP_COLOR_BACKGROUND : POPUP_COLOR_MARRON), popupX, popupY, popupWidth, popupHeight);
		rectDraw(POPUP_COLOR_BORDER, popupX, popupY, popupWidth, popupHeight);
	break;

	case POPUP_BORDER_BOCADILLO:

		int width = popupWidth / POPUP_MARC_WIDTH; if (popupWidth % POPUP_MARC_WIDTH != 0) {width++;}
		int height = popupHeight / POPUP_MARC_HEIGHT; if (popupHeight % POPUP_MARC_HEIGHT != 0) {height++;}
		for (int y=0 ; y<height ; y++)
		{
			for (int x=0 ; x<width ; x++)
			{
				int destX = regla3(x, width, popupWidth);
				int destY = regla3(y, height, popupHeight);

				int frame = 4;
				if (x==0) {frame--;} else if (x==width-1) {frame++;  destX = popupWidth - POPUP_MARC_WIDTH;}
				if (y==0) {frame-=3;} else if (y==height-1) {frame+=3; destY = popupHeight - POPUP_MARC_HEIGHT;}

				spriteDraw(popupItemsImg, popupX + destX, popupY + destY, frame, popupItemsCor);
			}
		}
	break;

	case POPUP_BORDER_CASUAL_NARANJA:
	case POPUP_BORDER_CASUAL_GRIS:
	case POPUP_BORDER_CASUAL_MARRON:

		int base = (popupBorderGlobe - POPUP_BORDER_CASUAL_NARANJA) * 3;
		int rgb = new int[] {POPUP_COLOR_NARANJA, POPUP_COLOR_GRIS, POPUP_COLOR_MARRON}[popupBorderGlobe - POPUP_BORDER_CASUAL_NARANJA];

		fillDraw(rgb, popupX, popupY + FORM_MARC_HEIGHT, popupWidth, popupHeight - (FORM_MARC_HEIGHT*2));
		fillDraw(rgb, popupX + FORM_MARC_WIDTH, popupY, popupWidth - (FORM_MARC_WIDTH*2), popupHeight);
		fillDraw(rgb, popupX, popupY + popupHeight - FORM_MARC_HEIGHT, FORM_MARC_WIDTH, FORM_MARC_HEIGHT);

		printSetArea(popupX, popupY, popupWidth, popupHeight);
		printDraw(popupItemsImg, 0, 0, POPUP_ITEM_CASUAL+base, PRINT_LEFT|PRINT_TOP, popupItemsCor);
		printDraw(popupItemsImg, 0, 0, POPUP_ITEM_CASUAL+base+1, PRINT_RIGHT|PRINT_TOP, popupItemsCor);
		printDraw(popupItemsImg, 0, 0, POPUP_ITEM_CASUAL+base+2, PRINT_RIGHT|PRINT_BOTTOM, popupItemsCor);
	break;
	}


// popupTitle
	if (popupTitleStr != null)
	{
		formFontInit(FORM_FONT_SMALL_WHITE);
		printSetArea(popupX, popupY, popupWidth, popupTitleHeight);

		if (popupBorderGlobe == POPUP_BORDER_MARCO)
		{
			fillDraw(POPUP_COLOR_TITLE, popupX, popupY, popupWidth, popupTitleHeight);
			rectDraw(POPUP_COLOR_BORDER, popupX, popupY, popupWidth, popupTitleHeight);

			printDraw(popupTitleStr, 0, 0, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		} else {
			printDraw(popupTitleStr, 0, -2, formFontPenRGB, PRINT_HCENTER|PRINT_BOTTOM);
			fillDraw(formFontPenRGB, popupX + popupBodyX, printY + printHeight - 1, popupBodyWidth, 1);
		}
	}


// popupTitleIcon
	if (popupIconEnable)
	{
		spriteDraw(popupItemsImg, popupX + popupTitleIconX, popupY + popupTitleIconY, POPUP_ITEM_TITLE_LOGO + popupTitleIconNum, popupItemsCor);
	}



// popupType
	switch (popupType)
	{
	case POPUP_ASK:

		formFontInit(popupBorderGlobe == POPUP_BORDER_BOCADILLO? FORM_FONT_SMALL_BLACK : FORM_FONT_SMALL_WHITE);
		printSetArea(popupX + popupTextX, popupY + popupTextY, popupTextWidth, popupTextHeight);
	
		for (int i=0 ; i<popupTextView ; i++)
		{
			printDraw(popupTextStr[popupTextIni + i], 0, i*printGetHeight(), formFontPenRGB, (popupTextView==1? PRINT_HCENTER : PRINT_LEFT));
		}
	
	// Mostramos icono que hace referencia a que hay mas texto por mostrar
		if (popupTextStr.length > popupTextView && popupTextIni < popupTextStr.length - popupTextView)
		{
			printDraw(popupItemsImg,  -2, printSpriteHeight(popupItemsCor, POPUP_ITEM_MORE)+2, POPUP_ITEM_MORE,  PRINT_BOTTOM|PRINT_RIGHT,  popupItemsCor);
		}

	// Mostramos "aspa" del globo si el borde el popup es de tipo globo
		if (popupBorderGlobe == POPUP_BORDER_BOCADILLO)
		{
			int x = regla3(((popupAspa & 0x01) == 0? 20 : 80), 100, popupWidth) - (printSpriteWidth(popupItemsCor, POPUP_ITEM_ASPA + (popupAspa&0x7F)) / 2 );
			int y = ((popupAspa & 0x02) == 0? popupHeight - 2 : -printSpriteHeight(popupItemsCor, POPUP_ITEM_ASPA + (popupAspa&0x7F)) + 2);
			if (centrado) x>>=1;
			spriteDraw(popupItemsImg,  popupX + x, popupY + y, POPUP_ITEM_ASPA + (popupAspa&0x7F), popupItemsCor);
		}

		if (popupVslider)
		{
			sliderVDraw(SLIDER_COLOR_BLANCO, (popupBorderGlobe == POPUP_BORDER_CASUAL_NARANJA? SLIDER_COLOR_NARANJA : SLIDER_COLOR_GRIS), popupTextIni, popupTextStr.length - popupTextView, (popupX + popupBodyX + popupTextWidth), popupY + popupBodyY, popupBodyHeight - 4);
		}
	break;


	case POPUP_OPTION:

	// Controlamos si hay que hacer scroll vertical
		if (popupListIni > popupListPos) {popupListIni = popupListPos;}
		else
		if (popupListIni < popupListPos-popupListView+1) {popupListIni = popupListPos-popupListView+1;}

	// Pintamos todas las lineas del listado
		printSetArea(popupX + popupBodyX, popupY + popupBodyY, popupBodyWidth, popupBodyHeight);
		for (int i=0 ; i<popupListView ; i++)
		{
			formFontInit( (popupListIni + i == popupListPos? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK) );
			int addY = i * printGetHeight();
		// Cortamos texto para que no se salga de la pantalla
			String str = printTextCut(popupListStr[popupListIni+i][popupListDat[popupListIni+i][2]], popupBodyWidth);

		// Sombreamos la opcion activa
			if (popupListIni + i == popupListPos)
			{
				fillDraw(POPUP_COLOR_BORDER, printX, printY + addY, printWidth, printGetHeight());
			}

		// Pintamos texto ya cortado
			printDraw(str, 0, addY, formFontPenRGB, PRINT_HCENTER);

		// Pintamos aspas horizontales si es necesario
			if (popupListIni + i == popupListPos && popupListStr[popupListPos].length > 1)
			{
				int addYicon = (printGetHeight() - printSpriteHeight(formItemsCor, FORM_ARROW_LEFT)) / 2;
				printDraw(formItemsImg,  2, addY + addYicon, FORM_ARROW_LEFT, PRINT_LEFT,  formItemsCor);
				printDraw(formItemsImg, -2, addY + addYicon, FORM_ARROW_RIGHT, PRINT_RIGHT, formItemsCor);
			}

			if (popupVslider)
			{
//				sliderVDraw(popupListPos, popupListStr.length - 1, (popupX + popupBodyX + popupBodyWidth + POPUP_MARC_BORDER), popupVsliderY, popupVsliderHeight);
				int height = popupVsliderHeight / 4;
				int y = popupVsliderY + regla3(popupListPos, popupListStr.length - 1, popupVsliderHeight - height);
				int x = popupX + popupBodyX + popupBodyWidth + POPUP_MARC_BORDER;
				rectDraw(POPUP_COLOR_BORDER, x, popupVsliderY, POPUP_SLIDER_WIDTH, popupVsliderHeight);
				fillDraw(POPUP_COLOR_TITLE, x, y, POPUP_SLIDER_WIDTH, height);
				rectDraw(POPUP_COLOR_BORDER, x, y, POPUP_SLIDER_WIDTH, height);
			}

		}
	break;


	case POPUP_SORTEO:

		formFontInit(FORM_FONT_SMALL_WHITE);
		printSetArea(popupX + popupBodyX, popupY + popupBodyY, popupBodyWidth, popupBodyHeight);

		int sx = (popupBodyWidth - printSpriteWidth(popupSorteoCor, 0)) / 2;
		printDraw(popupSorteoImg, sx, 0, 0, PRINT_LEFT|PRINT_TOP, popupSorteoCor);

		printDraw(popupSorteoImg, sx, 0, 1 + ((popupSorteoRot>>8) % 2), PRINT_LEFT|PRINT_TOP, popupSorteoCor);

		printDraw(popupSorteoImg, sx, 0, 3 + ((popupSorteoRot>>8) % 3), PRINT_LEFT|PRINT_TOP, popupSorteoCor);

		printDraw(popupSorteoImg, sx, 0, 6 + ((popupSorteoRot>>8) % 8), PRINT_LEFT|PRINT_TOP, popupSorteoCor);


		printY += printSpriteHeight(popupSorteoCor, 0);
		printHeight -= printSpriteHeight(popupSorteoCor, 0);

		printDraw(new String [] {gameText[TEXT_EMPIEZA][0], popupTextStr[popupListPos]}, 0, 0, formFontPenRGB, PRINT_VCENTER|PRINT_HCENTER);
	break;


//	INTRANCE
	case POPUP_RESULTADOS:
	case POPUP_RECUENTO:
	case POPUP_TORNEO:
	    printSetArea(PizarraBodyX+1,PizarraBodyY+1,PizarraBodyW-2,PizarraBodyH-2);
	    //#ifdef J2ME
	    scr.setClip(popupX+1,popupY+1,popupWidth-2,popupHeight-2);
	    //#endif
	    PintaPizarra(popupX+1,popupY+1,popupWidth-2,popupHeight-2,0,"");
	    if (popupType==POPUP_TORNEO)
	    {
		    PintaPizarra(popupX+1,popupY+1,popupWidth-2,popupHeight-2,1,((RondaActual>0)?((RondaActual>=NumRondas-1)?((RondaActual>NumRondas-1)?gameText[TEXT_CLASIFICACION][3]:gameText[TEXT_CLASIFICACION][0]+gameText[TEXT_CLASIFICACION][2]):gameText[TEXT_CLASIFICACION][0]+gameText[TEXT_CLASIFICACION][1]+RondaActual):gameText[TEXT_CLASIFICACION][0]+" "+gameText[TEXT_CLASIFICACION][4]+PuntosTorneo[TorneoActual]+gameText[TEXT_CLASIFICACION][5]));
			PintaTorneo(popupX+1 , popupY+1 , popupWidth-2, popupHeight-2);
	    }
	    if (popupType==POPUP_RECUENTO)
	    {
		    PintaPizarra(popupX+1,popupY+1,popupWidth-2,popupHeight-2,1,gameText[TEXT_RECUENTO][0]);
			PintaRecuento(popupX+1 , popupY+1 , popupWidth-2, popupHeight-2);
	    }
	    if (popupType==POPUP_RESULTADOS)
	    {
		    PintaPizarra(popupX+1,popupY+1,popupWidth-2,popupHeight-2,1,gameText[TEXT_CLASIFICACION][3]);
			//PintaTorneo(popupX+1 , popupY+1 , popupWidth-2, popupHeight-2);
	    }
	    //#ifdef J2ME
	    scr.setClip(popupX+1,popupY+1,popupWidth-2,popupHeight-2);
	    //#endif
		PintaPizarra(popupX+1,popupY+1,popupWidth-2,popupHeight-2,2,"");
	break;
//	INTRANCE
	case POPUP_IMAGE:

		imageDraw(popupImageImg, popupX + popupBodyX, popupY + popupBodyY);
	break;


	case POPUP_TABLE:

		for (int y = -1 ; y<popupListView ; y++)
		{
			int fixY = (y==popupListView-1? 1:1);

			if (y < 0)
			{
				for (int x = -1 ; x<popupTableView ; x++)
				{
					int fixX = (x==popupTableView-1? 1:1);

					if (x < 0)
					{
						formFontInit(FORM_FONT_SMALL_BLACK);
						printSetArea(popupX, popupY + ((y+1) * popupTableCellHeight), popupTableLeftWidth + fixX, popupTableCellHeight + fixY);
						fillDraw(POPUP_COLOR_NARANJA, printX, printY, printWidth, printHeight);
//						printDraw(popupTableLeftStr[y], 0, 0, formFontPenRGB, PRINT_VCENTER|PRINT_HCENTER);
					} else {
						formFontInit(FORM_FONT_SMALL_WHITE);
						printSetArea(popupX + popupTableLeftWidth + (x * popupTableCellWidth) , popupY + ((y+1) * popupTableCellHeight) , popupTableCellWidth + fixX, popupTableCellHeight + fixY);
						fillDraw(POPUP_COLOR_GRIS, printX, printY, printWidth, printHeight);
						rectDraw(POPUP_COLOR_NARANJA, printX, printY, printWidth, printHeight);
						printDraw(popupTableTopStr[popupTableIni + x], 0, 0, formFontPenRGB, PRINT_VCENTER|PRINT_HCENTER);
					}
				}

			} else {

				for (int x = -1 ; x<popupTableView ; x++)
				{
					int fixX = (x==popupTableView-1? 1:1);

					if (x < 0)
					{
						printSetArea(popupX, popupY + ((y+1) * popupTableCellHeight), popupTableLeftWidth + fixX, popupTableCellHeight + fixY);
						int penRgb = FORM_FONT_SMALL_WHITE;
						int bacRgb = POPUP_COLOR_GRIS;
						if (popupListPos + y == popupTablePos)
						{
							penRgb = FORM_FONT_SMALL_BLACK;
							bacRgb = 0xffffff;
						}
						formFontInit(penRgb);
						fillDraw(bacRgb, printX, printY, printWidth, printHeight);
						rectDraw(POPUP_COLOR_NARANJA, printX, printY, printWidth, printHeight);
						printDraw(popupTableLeftStr[popupListPos + y], 0, 0, formFontPenRGB, PRINT_VCENTER|PRINT_HCENTER);
					} else {
						printSetArea(popupX + popupTableLeftWidth + (x * popupTableCellWidth) , popupY + ((y+1) * popupTableCellHeight) , popupTableCellWidth + fixX, popupTableCellHeight + fixY);
						int penRgb = FORM_FONT_SMALL_WHITE;
						int bacRgb = POPUP_COLOR_MARRON;
						if (popupListPos + y == popupTablePos)
						{
							penRgb = FORM_FONT_SMALL_BLACK;
							bacRgb = 0xffffff;
						}
						formFontInit(penRgb);
						fillDraw(bacRgb, printX, printY, printWidth, printHeight);
						rectDraw(POPUP_COLOR_NARANJA, printX, printY, printWidth, printHeight);
						printDraw(popupListStr[popupListPos + y][popupTableIni + x], 0, 0, formFontPenRGB, PRINT_VCENTER|PRINT_HCENTER);
					}
				}

			}

		}


		if (popupVslider)
		{
			int height = popupVsliderHeight / 4;
			int y = popupVsliderY + regla3(popupListPos, popupListStr.length - popupListView, popupVsliderHeight - height);
			int x = popupX + popupBodyX + popupBodyWidth;
			fillDraw(POPUP_COLOR_MARRON, x, popupVsliderY, POPUP_SLIDER_WIDTH, popupVsliderHeight);
			rectDraw(POPUP_COLOR_NARANJA, x, popupVsliderY, POPUP_SLIDER_WIDTH, popupVsliderHeight);
			fillDraw(POPUP_COLOR_NARANJA, x, y, POPUP_SLIDER_WIDTH, height);
//			rectDraw(POPUP_COLOR_BORDER, x, y, POPUP_SLIDER_WIDTH, height);
		}

		if (popupHslider)
		{
			int width = popupHsliderWidth / 4;
			int x = popupHsliderX + regla3(popupTableIni, popupListStr[0].length - popupTableView, popupHsliderWidth - width);
			int y = popupY + popupBodyY + popupBodyHeight;
			fillDraw(POPUP_COLOR_MARRON, popupHsliderX, y, popupHsliderWidth, POPUP_SLIDER_HEIGHT);
			rectDraw(POPUP_COLOR_NARANJA, popupHsliderX, y, popupHsliderWidth, POPUP_SLIDER_HEIGHT);
			fillDraw(POPUP_COLOR_NARANJA, x, y, width, POPUP_SLIDER_HEIGHT);
//			rectDraw(POPUP_COLOR_BORDER, x, y, width, POPUP_SLIDER_HEIGHT);
		}

		if (popupVslider && popupHslider)
		{
			fillDraw(POPUP_COLOR_NARANJA, popupX + popupBodyX + popupBodyWidth, popupY + popupBodyY + popupBodyHeight, POPUP_SLIDER_WIDTH, POPUP_SLIDER_HEIGHT);
//			rectDraw(POPUP_COLOR_BORDER, popupX + popupBodyX + popupBodyWidth, popupY + popupBodyY + popupBodyHeight, POPUP_SLIDER_WIDTH, POPUP_SLIDER_HEIGHT);
		}

	break;
	}


//rectDraw(0x00ff00, popupX, popupY, popupWidth, popupHeight);


}


// -------------------
// popup Refresh
// ===================

public void popupRefresh()
{
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
		popupExit = true;
	break;
//	INTRANCE
	case POPUP_ACTION_PLAY_RONDA :
	    if ((!CargandoTorneo)&&(RondaActual>=NumRondas))
	    {
	        //PROGRAMAR PREMIOS
	        popupRelease();
	        biosStatus = BIOS_GAME;
	        popupInitInfo(gameText[TEXT_INFO][0], gameText[TEXT_FIN_DE_TORNEO]);
	        biosWait();
	        playExit = 3;
	        
	        popupInitInfo(gameText[TEXT_PREMIO][0], new String[] {gameText[TEXT_PREMIOS][TorneoActual]});
            biosWait();
	        if ((TorneoActual<3)&&((TorneoLock>>(TorneoActual+1)&0x01)!=0))
	        {
	            popupInitInfo(gameText[TEXT_PREMIO][0], gameText[TEXT_TORNEO_COMPLETADO]);
	            biosWait();
	        }
	        TorneoLock &= (0x0F<<(TorneoActual+2));
	        BorrarTorneo();
	    } else
	    {
	        byte EquipoC=0;
		    byte EquipoA=0;
	        byte EquipoB=0;
	        if ((!CargandoTorneo)&&(RondaActual>0))
	        {
		        SavedRondaActual	= RondaActual;
		        SavedTorneo			= TorneoActual;
		        SavedEquiposTorneo	= new byte[NumEquipos];
		        System.arraycopy(EquiposTorneo,0,SavedEquiposTorneo,0,EquiposTorneo.length);
		        SavedNombreEquipo	= EquiposTorneoName[PosJugador];
		        SavedResultsTorneo = new byte[NumRondas][];
		        for (int ActRonda=0; ActRonda<NumRondas; ActRonda++)
		        {
		            SavedResultsTorneo[ActRonda] = new byte[ResultsTorneo[ActRonda].length];
		            System.arraycopy(ResultsTorneo[ActRonda],0,SavedResultsTorneo[ActRonda],0,ResultsTorneo[ActRonda].length);
		        }
		        for (int ActResult = 0; ActResult<SavedResultsTorneo[SavedRondaActual].length; ActResult++)
			    {
		            EquipoA = SavedResultsTorneo[SavedRondaActual-1][ActResult*2];
		            EquipoB = SavedResultsTorneo[SavedRondaActual-1][ActResult*2+1];
		            if ((EquipoA==0)||(EquipoB==0))
			        {
		                if (EquipoB==0) SavedEnemy = EquipoA; else SavedEnemy = EquipoB;
		                break;//Del for
			        }
			    }
	        }
	        
	        
	        EquipoC=1;
		    EquipoA=0;
	        EquipoB=0;
	        //#ifdef DEBUG
		    System.out.println("INICIO");
		    PintaTorneo();
		    //#endif
		    modoTorneo = true;
		    
	        if (!CargandoTorneo)
	        {
	            for (int ActResult = 0; ActResult<ResultsTorneo[RondaActual].length; ActResult++)
			    {
			        if (RondaActual==0)
			        {
			            ResultsTorneo[RondaActual][ActResult] = EquiposTorneo[ActResult];
			            EquipoC = EquiposTorneo[PosJugador+(((PosJugador%2) == 0)?1:-1)];
			        } else
			        {
			            EquipoA = ResultsTorneo[RondaActual-1][ActResult*2];
			            EquipoB = ResultsTorneo[RondaActual-1][ActResult*2+1];
			        
				        if ((EquipoA!=0)&&(EquipoB!=0))
				        {
				            if ((NivelIA[TorneoActual][EquipoA]+rnd(2))>(NivelIA[TorneoActual][EquipoB]+rnd(2)))
				            {
				                ResultsTorneo[RondaActual][ActResult] = EquipoA;
				            } else
				            {
				                if ((NivelIA[TorneoActual][EquipoA]+rnd(2))<(NivelIA[TorneoActual][EquipoB]+rnd(2)))
				                {
				                    ResultsTorneo[RondaActual][ActResult] = EquipoB;
				                } else
				                {
				                    if (rnd(2)==0) ResultsTorneo[RondaActual][ActResult] = EquipoA; else ResultsTorneo[RondaActual][ActResult] = EquipoB;
				                }
				            }
				            if (ResultsTorneo[RondaActual][ActResult] == EquipoA) EquipoB *= -1; else EquipoA *= -1;
				            if (RondaActual>0)
				            {
				                if (ResultsTorneo[RondaActual-1][ActResult*2]!=ResultsTorneo[RondaActual][ActResult]) ResultsTorneo[RondaActual-1][ActResult*2] *= -1;
				                if (ResultsTorneo[RondaActual-1][ActResult*2+1]!=ResultsTorneo[RondaActual][ActResult]) ResultsTorneo[RondaActual-1][ActResult*2+1] *= -1;
				            }
				            for (int ActEquipo=0; ActEquipo<EquiposTorneo.length; ActEquipo++)
				            {
				                if (EquiposTorneo[ActEquipo]==Math.abs(EquipoA)) EquiposTorneo[ActEquipo] = EquipoA;
				                if (EquiposTorneo[ActEquipo]==Math.abs(EquipoB)) EquiposTorneo[ActEquipo] = EquipoB;
				            }
				        } else
				        {
				            if (EquipoB==0) EquipoC = EquipoA; else EquipoC = EquipoB; 
				        }
			        }
			    }
			    
			    RondaActual++;
	        } else
	        {
	            EquipoC = SavedEnemy;
	           // System.out.println("Cargando Torneo");
	        }
	        popupRelease();
			CargandoTorneo = false;
		    
		    if (biosStatusOld==BIOS_MENU)
		    {
		        menuRelease();
			    menuExit = true;
	
			    gameStatus = GAME_PLAY;
			    biosStatus = BIOS_GAME;
		    } else
		    {
		        popupExit = true;
		    }
		    levelAI[0] = NivelIA[TorneoActual][0]; levelAI[2] = NivelIA[TorneoActual][0];
		    levelAI[1] = NivelIA[TorneoActual][EquipoC]; levelAI[3] = NivelIA[TorneoActual][EquipoC];
		    gameMode = GAME_MODE_COMPETITION;
		    gameConfig[0] = CONTROL_PLAYER;
		    gameConfig[1] = CONTROL_CPU;
		    gameConfig[2] = CONTROL_CPU;
		    gameConfig[3] = CONTROL_CPU;
		    gamePlayerNamesOnly	   = new String[4];
		    gamePlayerNamesOnly[0] = gamePlayerNames[1];
		    System.out.println(EquipoC);
		    System.out.println(gameText[TEXT_NOMBRES_TORNEO0+TorneoActual][(EquipoC-1)*2]);
		    gamePlayerNamesOnly[1] = gameText[TEXT_NOMBRES_TORNEO0+TorneoActual][(EquipoC-1)*2];
		    gamePlayerNamesOnly[2] = gamePlayerNames[2];
		    gamePlayerNamesOnly[3] = gameText[TEXT_NOMBRES_TORNEO0+TorneoActual][(EquipoC-1)*2+1];
		    gameTeamNamesOnly	   = new String[2];
		    gameTeamNamesOnly[0] = EquiposTorneoName[PosicionTorneo(0)];
		    gameTeamNamesOnly[1] = EquiposTorneoName[PosicionTorneo(EquipoC)];
		    EquiposPartida[0] = 0;
		    EquiposPartida[1] = EquipoC;
		    mesaSeleccionada = TorneoActual;
		    
		    //#ifdef DEBUG
		    System.out.println("FINAL");
		    PintaTorneo();
		    System.out.println("-------------------------------------------------------------------------------------");
		    //#endif
	    }
	    savePrefs();
	break;
//	INTRANCE - MIRA AQUI
	}
}










// -------------------
// popupList Clear
// ===================

public void popupListClear()
{
	popupListIni = 0;
	popupListStr = null;
	popupListDat = null;
}

// -------------------
// popupList Add
// ===================

public void popupListAdd(int dato, String texto)
{
	popupListAdd(dato, new String[] {texto}, 0, 0);
}

public void popupListAdd(int dato, String[] texto)
{
	for (int i=0 ; i<texto.length ; i++)
	{
		popupListAdd(dato, texto[i]);
	}
}

public void popupListAdd(int dato, String texto, int dat1)
{
	popupListAdd(dato, new String[] {texto}, dat1, 0);
}

public void popupListAdd(int dato, String[] texto, int dat1)
{
	popupListAdd(dato, new String[] {texto[0]}, dat1, 0);
}

public void popupListAdd(int dat0, String[] texto, int dat1, int dat2)
{
	int lines=(popupListDat!=null)?popupListDat.length:0;

	String str[][] = new String[lines+1][];
	int dat[][] = new int[lines+1][];

	int i=0;

	for (; i<lines; i++)
	{
		dat[i] = popupListDat[i];
		str[i] = popupListStr[i];
	}

	int textoSizeX = 2;
	for (int sizeX, t=0 ; t<texto.length ; t++) {sizeX = printStringWidth(texto[t]); if ( textoSizeX < sizeX ) {textoSizeX=sizeX;}}

	str[i] = texto;
	dat[i] = new int[] {dat0, dat1, dat2, textoSizeX, printGetHeight() };

	popupListStr = str;
	popupListDat = dat;
}


// -------------------
// popupList Opt
// ===================

public int popupListOpt()
{
	return(popupListDat[popupListPos][2]);
}


// <=- <=- <=- <=- <=-











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


int playExit;
boolean playShow = false;

int playLeftSoftkeyOld;		// Guardamos la softkey que hay durante el juego para cuando regresemos del menu "ingame"
int playRightSoftkeyOld;	// Guardamos la softkey que hay durante el juego para cuando regresemos del menu "ingame"
// Gr�ficos de la partida
// ________________________________________________________________________________________________________________
Image		fichas_gfx;				// fichas del tablero
byte[]	fichas_gfx_cor;		// coordenadas de las fichas

Image 	fichasp_gfx;			// fichas del player
byte[] 	fichasp_gfx_cor;	// coordenadas de las fichas del player

Image		mesa_gfx;					// fichas de la mesa
byte[]	mesa_gfx_cor;			// coordenadas de las fichas de la mesa

Image		flechas_gfx;			// Flechas de scroll de mesa y fichas del player
byte[]	flechas_gfx_cor;	// coordenadas de las flechas de scroll de mesa y fichas del player

Image		mesap_gfx;				// Mesa del player

byte[] tilesCombi;				// tile set combinado

Image 	redonda_gfx;			// Redonda que muestra las dos puntas de la mesa

Image		extras_gfx;				// Movil, Tecla izquierda, tecla derecha
byte[]	extras_gfx_cor;
// ________________________________________________________________________________________________________________

// -------------------
// play Create
// ===================

public void playCreate()
{

  // Refrescamos 'RNS_Cont' para tener una semilla aleatoria
	RND_Cont = (int)(System.currentTimeMillis()>>8 & 0x0F);
	
	// Creamos el array de nombres de players sin lo de Equipo A y Equipo B
	gamePlayerNamesOnly = new String[4];
	gamePlayerNamesOnly[0] = gamePlayerNames[1];
	gamePlayerNamesOnly[1] = gamePlayerNames[4];
	gamePlayerNamesOnly[2] = gamePlayerNames[2];
	gamePlayerNamesOnly[3] = gamePlayerNames[5];
	
	// Contamos el n�mero de players
	numPlayers = 0;
	for (int NumPlayer = 0; NumPlayer < MAX_PLAYERS; NumPlayer++)
	{
	    if (gameConfig[NumPlayer] != CONTROL_NONE) numPlayers++;    
	}
    	       	    
	// HACK - Si son dos players que est�n de frente.
	if (numPlayers == 2)
	{
	    gameConfig[PLAYER_3] 					= gameConfig[PLAYER_2];
	    gamePlayerNamesOnly[PLAYER_3] = gamePlayerNamesOnly[PLAYER_2];
	    gameConfig[PLAYER_2] 					= CONTROL_NONE;
	}
	
	// Cargamos los gr�ficos de las fichas del tablero
	fichas_gfx			= loadImage("/fichas");
	fichas_gfx_cor	= loadFile("/fichas.cor");
	 
	// Cargamos los gr�ficos de las fichas del player
	fichasp_gfx 		= loadImage("/fichasp"); 	
	fichasp_gfx_cor	= loadFile("/fichasp.cor");
	
	// Cargamos los gr�ficos de la mesa 
	mesa_gfx				= loadImage(mesa[mesaSeleccionada]);					// fichas de la mesa
	mesa_gfx_cor		= loadFile(mesa[mesaSeleccionada]+".cor");						// coordenadas de las fichas de la mesa
	
	// Cargamos las flechas de scroll
/*	flechas_gfx			= loadImage("/flechas");
	flechas_gfx_cor	= loadFile("/flechas.cor");*/
	
	// Cargamos la mesa del player
	mesap_gfx				= loadImage("/mesap");
	
	// Cargamos gr�fico de redonda
	redonda_gfx			= loadImage("/redonda");

	// Cargamos Movil, Tecla izquierda, tecla derecha
	extras_gfx			= loadImage("/extras");
	extras_gfx_cor	= loadFile("/extras.cor");

	// Calculamos variables necesarias para el render de las fichas
	// Despu�s de cargar los gr�ficos obviamente
	CanvasForFichas			 	= (canvasWidth * 80) / 100; // 80% del canvas util (reservamos 10% por lado para scores)
  CanvasStartY					= canvasHeight - (printSpriteHeight(fichasp_gfx_cor, UP_FICHA) * 2) - 15;
  WidthFicha						= printSpriteWidth(fichasp_gfx_cor, UP_FICHA);
    
  GameNumFichasToShow		= CanvasForFichas / WidthFicha;
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
  // Borramos los gr�ficos de memoria
	fichas_gfx			= null;
	fichas_gfx_cor	= null;

	fichasp_gfx 		= null;
  fichasp_gfx_cor	= null;

	mesa_gfx				= null;					// fichas de la mesa
	mesa_gfx_cor		= null;					// coordenadas de las fichas de la mesa
	
	/*flechas_gfx			= null;
	flechas_gfx_cor	= null;*/
	
	mesap_gfx				= null;
	
	redonda_gfx			= null;

	extras_gfx			= null;
	extras_gfx_cor	= null;
}

// -------------------
// play Init
// ===================

public void playInit()
{
}

// -------------------
// play Release
// ===================

public void playRelease()
{
}

// -------------------
// play Tick
// ===================
// BIGTHOR - CODE

final static int PLAY_STATUS_CREATE_GAME								= 0;
final static int PLAY_STATUS_REPARTIR_FICHAS						= 10;
final static int PLAY_STATUS_DECIDIR_QUIEN_EMPIEZA			= 51;
// .... TODO ... Faltan opciones de juego
final static int PLAY_STATUS_PANTALLA_TIRA_JUGADORX			= 52;
final static int PLAY_STATUS_REALIZAR_JUGADA						= 53;
final static int PLAY_STATUS_PRE_TIRANDO_FICHA					= 54;
final static int PLAY_STATUS_POST_TIRANDO_FICHA					= 55;
final static int PLAY_STATUS_COMPROBAR_GANADOR					= 60;
final static int PLAY_STATUS_COMPROBAR_GANADOR_COUNT		= 61;
final static int PLAY_STATUS_COMPROBAR_GANADOR_FIN			= 62;
final static int PLAY_STATUS_TURNO_SIGUIENTE						= 63;
final static int PLAY_STATUS_MOSTRAR_RECUENTO				= 69;
final static int PLAY_STATUS_MOSTRAR_GANADOR						= 70;
final static int PLAY_STATUS_FIN_RONDA									= 71;
final static int PLAY_STATUS_SHOW_BOARD									= 72;
final static int PLAY_STATUS_REINICIAR_PARTIDA				= 100;
final static int PLAY_STATUS_MOSTRAR_CLASIFICACION			= 101;


// playStatus gestiona en qu� estado dentro del juego nos encontramos 
public int playStatus;


// Datos de configuraci�n de la partida
// ________________________________________________________________________________________________________________
// Jugadores en partida
final static int MAX_PLAYERS 												= 4;			// Como m�ximo 4 players
//INTRANCE
final static int MAX_EQUIPOS 												= 2;			// Como m�ximo 2 equipos
//INTRANC
public int numPlayers 															= 2; 			// Siempre va de 2 a 4
public int ActualPlayer															= 0;

// Tipos de player
final static int 	CONTROL_PLAYER										=	0;
final static int	CONTROL_CPU												= 1;
final static int	CONTROL_NONE											= 2;

final static int    AI_0			= 0;
final static int    AI_1			= 1;
final static int    AI_2			= 2;
final static int    AI_3			= 3;
final static int    AI_4			= 4;


public int[] levelAI = new int[] {AI_4, AI_0, AI_4, AI_0};
        
//#ifndef DEBUG
	//public int[] gameConfig = new int[] {CONTROL_PLAYER, CONTROL_CPU, CONTROL_CPU, CONTROL_CPU};		// Tiene configurada c�mo ser�n los jugadores de la partida PLAYER/CPU/NONE
//#else
	public int[] gameConfig = new int[] {CONTROL_PLAYER, CONTROL_PLAYER, CONTROL_NONE, CONTROL_NONE};		// Tiene configurada c�mo ser�n los jugadores de la partida PLAYER/CPU/NONE
//#endif
public int[] fichaSelected = new int[MAX_PLAYERS];
public int[] fichaOffsetStart = new int[MAX_PLAYERS];

// Modos de juego
final static int	GAME_MODE_ARCADE									= 0;			// Modo ARCADE todos contra todos	
final static int	GAME_MODE_COMPETITION							= 1;			// Modo COMPETITION 4 players por equipos
public int gameMode = GAME_MODE_ARCADE; 											// Modo ARCADE / COMPETICI�N
// ________________________________________________________________________________________________________________

// Datos de Mapa
// ________________________________________________________________________________________________________________
int mapSizeX 														= 100;
int mapSizeY														= 100;
byte[] levelMap 												= new byte[mapSizeX*mapSizeY];
int canvasTop 													= 24;
//private final int canvasTop 						= 0;

final static int	NOT_PUT								= -1;
final static int	LEFT_BRANCH 					= 0;
final static int 	RIGHT_BRANCH					= 1;
final static int  BOTH_PUT							= 2;
final static int	NEW_PUT								= 3;

int[] IncDecX = new int[] {-1,  0, 1, 0};
int[] IncDecY = new int[] { 0,  1, 0,-1};

int[] Branch 														= new int[2];
int[] BranchDirection 									= new int[2];
int[] BranchX														= new int[2];
int[] BranchY														= new int[2];
int[][] BranchTurnX											= new int[][]	{{  0,	0,	0,	0},{  0,	0,	0,  0}};
int[][] BranchTurnY											= new int[][]	{{  1,	0,  0, -1},{  0,	0,	1, -1}};

private final int TABLE_CAPTURE_SIZE		= 5;
int[][] TableMemory											= new int[TABLE_CAPTURE_SIZE][TABLE_CAPTURE_SIZE];
boolean capturedTable;

final static int TOP_SPACING_BRANCH     = 12;
//final static int TOP_SPACING_BRANCH     = 5;

int[] BranchSpacing											= new int[2];

int	DefaultBranch;											// Rama por defecto donde se tira cuando podemos tirar a ambos lados
boolean waitSelection;									// Esperar a la elecci�n de rama por el jugador
boolean ContinuandoJuego = false;

/*
final static int DIRECTION_RIGHT_RIGHT 	= 0;
final static int DIRECTION_RIGHT_DOWN	 	= 1;
final static int DIRECTION_RIGHT_LEFT	 	= 2;
final static int DIRECTION_RIGHT_UP		 	= 3;
*/

final static int DIRECTION_LEFT					= 0;
final static int DIRECTION_DOWN	 				= 1;
final static int DIRECTION_RIGHT				= 2;
final static int DIRECTION_UP		 				= 3;
// ________________________________________________________________________________________________________________

// Gesti�n de la c�mara en el scroll
// ________________________________________________________________________________________________________________
boolean controlCamera;
int posCameraX = 0;
int posCameraY = 0;

int CanvasScrollHeight; // Se calcula en PLAY_STATUS_CREATE_GAME. Guarda el tama�o reservado para el scroll de la mesa 
// ________________________________________________________________________________________________________________

// Tiempos de espera
// ________________________________________________________________________________________________________________
//final static int TIEMPO_FICHA		= 2000;
int TIEMPO_FICHA = 20;
// ________________________________________________________________________________________________________________

// Constantes de render
// ________________________________________________________________________________________________________________
final static int MARGEN_FLECHAS = 3;

// Fichas del player
public int CanvasForFichas;
public int CanvasStartY;
public int WidthFicha;
public int GameNumFichasToShow;

boolean mostrarPopupRobarPasar;
boolean mostrarPopupTutorial;

// ________________________________________________________________________________________________________________
// Tutorial
// ________________________________________________________________________________________________________________
int hintCount;
final static int MAX_HINTS 						= 14;
// ________________________________________________________________________________________________________________

// ________________________________________________________________________________________________________________

// Inteligencia Artificial
// ________________________________________________________________________________________________________________

// Total de los puntos que se est�n jugando en el juego
int TotalPuntosJuego;
int TotalFichasJuego;

// Valoraci�n de las fichas
final static int 	PESO_DE_DOBLE					= 12;
final static int  PESO_GALLO_QUE_REPITE = 10;
final static int  PESO_DOBLE_MUERTO			= 6;

// Apunta de qu� palos hemos pasado
public boolean[][] HistoricoDePases = new boolean[MAX_PLAYERS][MAX_VALOR_FICHA+1];
public int[] UltimoPalo = new int[MAX_PLAYERS];
public boolean[] LastPut = new boolean[2];

/*
void ShellSort(int[][] tantosDePinta) 
{
  int n = MAX_VALOR_FICHA+1;  
	int i, j, k, h, v;
	int va, vb;
	int[] cols = { 336, 112, 48, 21, 7, 3, 1 };

	for (k = 0; k < 7; k++) {
		h = cols[k];
		for (i = h; i < n; i++) {
			v = tantosDePinta[SUMA_PINTA][i];
			va = tantosDePinta[ID_PINTA][i];

			j = i;
			while (j >= h && tantosDePinta[SUMA_PINTA][j - h] > v) {
				tantosDePinta[SUMA_PINTA][j] 	= tantosDePinta[SUMA_PINTA][j - h];
				tantosDePinta[ID_PINTA][j] 		= tantosDePinta[ID_PINTA][j - h];
				j = j - h;
			}
			tantosDePinta[SUMA_PINTA][j] 		= v;
			tantosDePinta[ID_PINTA][j] 			= va;
		}
	}
}
*/

public void calcularPesos()
{
    int IDFicha;
    // Limpiamos los arrays para evitar el Bug de PALO ES GAY DEL PARAGUAY
    
    for (int NumFicha = 0; NumFicha < NUM_FICHAS; NumFicha++)
    {
        FichasDe[PESOS_L][NumFicha] = -1;
        FichasDe[PESOS_R][NumFicha] = -1;
    }
    
    // Calcula los pesos de las fichas de actual player

    //System.out.println("muchos de una pinta ... ");
    // Aplicamos la regla "muchos de una pinta"
    int tantosDePinta[] = new int[MAX_VALOR_FICHA+1];
    for (int NumFicha = 0; NumFicha < GetNumFichasOf(ActualPlayer); NumFicha++)
    {
        IDFicha = FichasDe[ActualPlayer][NumFicha];
        // Para cada pinta acumulamos cuantos tenemos de cada        
        tantosDePinta[getTopFicha(IDFicha)]+=1;
        if (!(esDoble(IDFicha)))
        {
            tantosDePinta[getBottomFicha(IDFicha)]+=1;
        }
    }
    
    //System.out.println("primeros pesos ... ");
    // Calculamos los primeros pesos seg�n el propio peso de la ficha
    for (int NumFicha = 0; NumFicha < GetNumFichasOf(ActualPlayer); NumFicha++)
    {
        IDFicha = FichasDe[ActualPlayer][NumFicha];
        
        for (int Rama = 0; Rama < 2; Rama++)
        {
            if (puedoTirarFicha(Rama,IDFicha) != NOT_PUT)
            {                
                // Los pesos propios de las fichas cuentan
                FichasDe[PESOS_L + Rama][NumFicha] = 1 + getTopFicha(IDFicha) + getBottomFicha(IDFicha);
                if (levelAI[ActualPlayer]>=AI_1)
                {
                    // Para cada pinta acumulamos cuantos tenemos de muchas pintas
                    FichasDe[PESOS_L + Rama][NumFicha] += tantosDePinta[getTopFicha(IDFicha)];
                }
                
                if (levelAI[ActualPlayer]>=AI_2)
                {
						        if (esDoble(IDFicha))
						        {
						            // Si es doble pesa m�s para tirarla antes
						            FichasDe[PESOS_L + Rama][NumFicha]+=PESO_DE_DOBLE; 
						        } else
						        {
						            // Si no es doble acumular los pesos por muchas pintas de la parte inferior tambien
						            FichasDe[PESOS_L + Rama][NumFicha] += tantosDePinta[getBottomFicha(IDFicha)];
						        }
                }
                
                if (levelAI[ActualPlayer]>=AI_3)
                {
						        // Miramos si podemos utilizar la regla "gallo que no repite no es gallo" 
						        if (getNewBranchValue(Rama, IDFicha) == UltimoPalo[ActualPlayer])
						        {
						            FichasDe[PESOS_L + Rama][NumFicha] += PESO_GALLO_QUE_REPITE;
						        }
                }
                
                if (levelAI[ActualPlayer]>=AI_4)
                {   
						        // Comprobar cierre de jugada
						        if (getNewBranchValue(Rama, IDFicha) == Branch[(Rama+1)%2])
						        {
						            if (partidaCasiCerrada(getNewBranchValue(Rama, IDFicha)))
						            {
						                // Mirar si por el n�mero de las fichas de los dem�s interesa cerrar
						                FichasDe[PESOS_L + Rama][NumFicha] += interesaCerrar(IDFicha);				                				                				                
						            }
						            
						        }
                }
            } else
            {
                FichasDe[PESOS_L + Rama][NumFicha] = NOT_PUT;
            }
        }
    }       
    
    // Revisar hist�rico de pases
}

public void seleccionarFichaMayorPeso()
{
    int NumFichasQuePuedoTirar 	= 0;
    int NumPrimeraFicha 				= 0;
    int NumPrimeraRama					= 0;
    int IDFicha;
    int PesoMaximo 	= 0;
    fichaSelected[ActualPlayer] 		= 0;
    System.out.println("seleccionar ficha ------------------ ");
    System.out.println(" ActualPlayer "+ActualPlayer);
    System.out.println(" fichas "+GetNumFichasOf(ActualPlayer));
    // Calculamos los primeros pesos seg�n el propio peso de la ficha
    for (int NumFicha = 0; NumFicha < GetNumFichasOf(ActualPlayer); NumFicha++)
    {
        IDFicha = FichasDe[ActualPlayer][NumFicha];
        if ((puedoTirarFicha(LEFT_BRANCH, IDFicha) != NOT_PUT) || (puedoTirarFicha(RIGHT_BRANCH, IDFicha) != NOT_PUT)) NumFichasQuePuedoTirar++;

        System.out.println("NumFicha = "+NumFicha);
        for (int Rama = 0; Rama < 2; Rama++)
        {
            System.out.println("Peso = "+FichasDe[PESOS_L + Rama][NumFicha]);
            if (FichasDe[PESOS_L + Rama][NumFicha] > PesoMaximo)
            {
                PesoMaximo 											= FichasDe[PESOS_L + Rama][NumFicha];
                fichaSelected[ActualPlayer] 		= NumFicha;
                DefaultBranch										= Rama;
                System.out.println("ficha seleccionada = "+NumFicha+" rama = "+Rama);
            }
            
            if (puedoTirarFicha(Rama, IDFicha) != NOT_PUT)
            {
                NumPrimeraFicha = NumFicha;
                NumPrimeraRama	= Rama;
            }
        }
    }
    
    // Comprobar si s�lo hay una ficha tiramos la primera ficha que podamos
    if (NumFichasQuePuedoTirar==1)
    {
        fichaSelected[ActualPlayer] 		= NumPrimeraFicha;
        DefaultBranch										= NumPrimeraRama;
    }
}

// ________________________________________________________________________________________________________________

public void NextUtilPlayer()
{
  ActualPlayer = getNextUtilPlayer();
  /*
	do
	{
	    ActualPlayer = (++ActualPlayer % MAX_PLAYERS);
	} while (gameConfig[ActualPlayer] == CONTROL_NONE);
	*/
}

public int getNextUtilPlayer()
{
  int myPlayer = ActualPlayer;
	do
	{
	    myPlayer = (++myPlayer % MAX_PLAYERS);
	} while (gameConfig[myPlayer] == CONTROL_NONE);
	return(myPlayer);
}

// Senos
int incSinus = 1;
int senoidal = 0;
final static int sinRange = 5;
int JugadorMano = 0;
int FichaBlink;
final static int BLINK_TIME 		= 15;
//INTRANCE
int PuntosMano = 0;
//INTRANCE

public boolean playTick()
{
    // Seno
    if (Math.abs(senoidal+=incSinus)>sinRange) incSinus *= -1;
    
    switch(playStatus)
    {
    	case PLAY_STATUS_CREATE_GAME:
//    	  INTRANCE
    	    numPartidas++;
    	    
    	    formFontInit(FORM_FONT_SMALL_WHITE);
    	    canvasTop = printGetHeight()*2+7;
//    	  INTRANCE
    	    
    	    // Por defecto no se controla la c�mara
    	    hintCount											= 0;
    	    controlCamera 								= false;
    	    Branch[LEFT_BRANCH]						= NOT_PUT;
    	    Branch[RIGHT_BRANCH]					= NOT_PUT;
    	    BranchDirection[LEFT_BRANCH] 	= DIRECTION_LEFT;
    	    BranchDirection[RIGHT_BRANCH] = DIRECTION_RIGHT; // DEBUG
    	    BranchSpacing[LEFT_BRANCH]		= 0;
    	    BranchSpacing[RIGHT_BRANCH]		= 0;    	    

    	      BranchX[LEFT_BRANCH] 	= 20;
    	      BranchX[RIGHT_BRANCH] = 20;
	    	    BranchY[LEFT_BRANCH] 	= 2;
	    	    BranchY[RIGHT_BRANCH]	= 2;
	    	    DefaultBranch = LEFT_BRANCH;
    	    // todo: solo en debug

    	     
    	    // Aqu� crearemos los arrais de fichas del juego
	    	  TotalPuntosJuego = 0;
	    	  TotalFichasJuego = 0;
	    	  
    	    int NumFicha = 0;
    	    int ContInf;
    	    int ContSup;
    	    
    	    for (ContInf = 0; ContInf <= MAX_VALOR_FICHA; ContInf++)
    	    {
    	        for (ContSup = ContInf; ContSup <= MAX_VALOR_FICHA; ContSup++)
    	        {
    	            // Las metemos todas en el MONT�N
    	            FichasDe[MONTON][NumFicha]		= (ContSup*10)+ContInf;
    	    
    	            // Contamos el total de puntos
    	            TotalPuntosJuego 							+= ContSup + ContInf;
    	            TotalFichasJuego++;
    	            
    	            // Vaciamos el resto de contenedores
    	            FichasDe[PLAYER_1][NumFicha]	= -1;
    	            FichasDe[PLAYER_2][NumFicha]	= -1;
    	            FichasDe[PLAYER_3][NumFicha]	= -1;
    	            FichasDe[PLAYER_4][NumFicha]	= -1;
    	            FichasDe[MESA][NumFicha]			= -1;
    	            NumFicha++;
    	            /*
    	            //#ifdef DEBUG
    	            System.out.println("----------------------------------------------------");
    	            //#endif
    	            */    	            
    	        }
    	        
    	    }
    	    // Inicializamos cuantas fichas tiene cada recipiente
    	    FichasDe[MONTON][GET_NUM_FICHAS]		= NUM_FICHAS;
	        FichasDe[PLAYER_1][GET_NUM_FICHAS]	= 0;
	        FichasDe[PLAYER_2][GET_NUM_FICHAS]	= 0;
	        FichasDe[PLAYER_3][GET_NUM_FICHAS]	= 0;
	        FichasDe[PLAYER_4][GET_NUM_FICHAS]	= 0;
	        FichasDe[MESA][GET_NUM_FICHAS]			= 0;
	        
	        // Vaciamos los hist�ricos y la �ltima ficha tirada
	        UltimoPalo[PLAYER_1] = -1;
	        UltimoPalo[PLAYER_2] = -1;
	        UltimoPalo[PLAYER_3] = -1;
	        UltimoPalo[PLAYER_4] = -1;
	        
	       
	        // Creamos la clase scroll -------
	        // Generamos el mapa
	        int StartOffset = 0;
	        int Offset = 0;
	        for (int TileY = 0; TileY < mapSizeY; TileY++)
	        {	            
	            Offset = 0;
	            for (int TileX = 0; TileX < mapSizeX; TileX++)
	            {
	                /*
	                levelMap[(TileY*mapSizeX)+TileX] = (byte)(OFFSET_TABLE_TILE + StartOffset + Offset);
	                //System.out.println(""+levelMap[(TileY*mapSizeX)+TileX]);
	                //#ifdef !DEBUG
	                	levelMap[(TileY*mapSizeX)+TileX] = OFFSET_TABLE_TILE; // TODO: Quitar esto
	                //#endif
	                Offset = ++Offset % 3;
	                */	                
	                setTile(TileX, TileY, 0);
	            }
	            StartOffset += 3;
	            if (StartOffset >= 9) StartOffset = 0;
	        }
	        
	        //levelMap = loadFile("/mapaPruebas.mmp");
	        // Cargamos el fichero combinado
	        tilesCombi = loadFile("/0.btsc");
	        
	        CanvasScrollHeight = (80 * (canvasHeight/*-canvasTop*/)) / 100; // 80% del canvas
	        
	        scrollCreate(0, canvasTop, canvasWidth, CanvasScrollHeight); // TODO: Calcular estos valores
	        //System.out.println("scrollCreate("+0+","+canvasTop+","+canvasWidth+","+CanvasScrollHeight+");");
	        //scrollCreate(0, 0, canvasWidth, canvasHeight); // TODO: Calcular estos valores
	        
	        //System.out.println("getWidth()         = "+fichas_gfx.getWidth() );
	        //System.out.println("printSpriteWidht() = "+printSpriteWidth(mesa_gfx_cor, 0));
	        
	        scrollInit(	levelMap,														// Info Mapa Mesa 
	                		tilesCombi, 												// Info Tile Sets Combinados
	                		printSpriteWidth(mesa_gfx_cor, 0), 	// Width del tile en pixels 
	                		printSpriteHeight(mesa_gfx_cor, 0), // Height del tile en pixels
	                		mapSizeX, 													// Tama�o del mapa en tiles	
	                		mapSizeY, 													// Tama�o del mapa en tiles
	                		fichas_gfx, 												// Gr�fico para el pintado de fichas
	                		mesa_gfx, 													// Gr�fico para el pintado de la mesa
	                																				// Width en tiles del tileset
	                		fichas_gfx.getWidth() / printSpriteWidth(mesa_gfx_cor, 0), 
	                																				// Height en tiles del tileset
	                		fichas_gfx.getHeight() / printSpriteHeight(mesa_gfx_cor, 0));
	        /*
	          
	         System.out.println("scrollInit(levelMap,tilesCombi,"+printSpriteWidth(mesa_gfx_cor, 0)+","+printSpriteHeight(mesa_gfx_cor, 0)+
	                					 ","+mapSizeX+","+mapSizeY+",fichas_gfx,mesa_gfx,"+fichas_gfx.getWidth() / printSpriteWidth(mesa_gfx_cor, 0)+
	                					 ","+fichas_gfx.getHeight() / printSpriteHeight(mesa_gfx_cor, 0)+");");
	        */
	        // Inicializamos scroll a la 0,0 // TODO: Cambiar 0,0 por una posici�n de start de fichas
	        setCamera(BranchX[LEFT_BRANCH] * tileWidth, BranchY[LEFT_BRANCH] * tileHeight);
	        // -------------------------------
	    	  
	        playStatus = PLAY_STATUS_REPARTIR_FICHAS;
    	break;
    
// BIGTHOR - CODE
    	case PLAY_STATUS_REPARTIR_FICHAS:
    	    // Repartimos 7 fichas por cada player
    	    do
    	    {
    	        // Mientras no tengan fichas se reparten
     	        for (int numFicha = 0; numFicha < NUM_FICHAS_FOR_PLAYER; numFicha++)
    	        {
    	            // Mover una ficha a cada player
    	            MoveFichaById(
    	                    				FichasDe[MONTON][rnd(GetNumFichasOf(MONTON))], 		// Id de la ficha a mover
    	                    				MONTON,																						// Recipiente origen
    	                    				ActualPlayer																			// Recipiente destino
    	            						 );
    	            
    	        }    	        
    	        
    	        // Pasamos al siguiente player util
     	        NextUtilPlayer();

    	    } while (GetNumFichasOf(ActualPlayer)== 0);    
    	    
 	        //#ifdef DEBUG
    	    	DebugRecipients();
    	   	//#endif                     

    	    playStatus = PLAY_STATUS_MOSTRAR_CLASIFICACION;
    	break;

//      INTRANCE
    	case PLAY_STATUS_MOSTRAR_CLASIFICACION :
    	    if ((modoTorneo)&&(PasoRonda)) 
    	    {
    	        popupClear();
    	        popupSetArea(0, canvasTop, canvasWidth, CanvasScrollHeight);
    	        popupInit(POPUP_TORNEO, POPUP_ACTION_NONE, POPUP_ACTION_PLAY_RONDA, SOFTKEY_NONE, SOFTKEY_CONTINUE); 
    	        biosWait();
    	        PasoRonda = false;
    	    }
    	    if (ContinuandoJuego == true)
    	    {
    	        playStatus = PLAY_STATUS_PANTALLA_TIRA_JUGADORX;
    	        ActualPlayer = JugadorMano;
    	        NextUtilPlayer();
    	        JugadorMano = ActualPlayer;
    	    } else playStatus = PLAY_STATUS_DECIDIR_QUIEN_EMPIEZA;
    	break;
//    	    INTRANCE
    	    
    	case PLAY_STATUS_DECIDIR_QUIEN_EMPIEZA:
    	    //System.out.println("DECIDIR QUIEN EMPIEZA ---------------------------------------------");
    	    primerTurno = true;
    	    
    	    String[] NombresSorteo = new String[numPlayers];
    	    int[]	CodigosSorteo = new int[numPlayers];
    	    //System.out.println("numPlayers = "+numPlayers);
    	    
    	    int PosNombre = 0;
    	    for (int NumPlayer=0; NumPlayer < MAX_PLAYERS; NumPlayer++)
    	    {
    	        if (gameConfig[NumPlayer]!= CONTROL_NONE)
    	        {
    	            //System.out.println("NumPlayer = " + NumPlayer);
    	            //System.out.println("gamePlayerNamesOnly[NumPlayer] = " + gamePlayerNamesOnly[NumPlayer]);
    	            NombresSorteo[PosNombre] 		= gamePlayerNamesOnly[NumPlayer];
    	            CodigosSorteo[PosNombre++]	= NumPlayer;
    	        }
    	    }

    	    //#ifndef DEBUG
	    	    /*popupInitSorteo(NombresSorteo);
	    	    biosWait();
	    	    
	    	    ActualPlayer = CodigosSorteo[popupListPos];*/
	    	  //#else
	    	    ActualPlayer = 0;
	    	  //#endif
	    	    JugadorMano = ActualPlayer;

	    	    // BIGTHOR BIGTILE
    	    //setBigTile(1, 1, scrollTileCombFirst + (VERTICAL_TOP 		+ (2 * 4)));
    	    //setCamera(10,10);

	    	    
	    	    
    	    playStatus = PLAY_STATUS_PANTALLA_TIRA_JUGADORX;
    	    //System.out.println("DECIDIR QUIEN EMPIEZA FIN -----------------------------------------");
    	break;
    	
    	case PLAY_STATUS_PANTALLA_TIRA_JUGADORX:
    	    // Mostrar qu� jugador empieza
    	    
    	    //popupTurno();
    	    
    	    mostrarPopupRobarPasar 		= true;
    	    mostrarPopupTutorial 			= true;
    	    listenerInit(SOFTKEY_MENU, SOFTKEY_LANZAR);
    	        	  
/*
    	    if (gameConfig[ActualPlayer] == CONTROL_PLAYER)
    	    {
    	        popupRobarPasar();
    	    }*/
    	    playStatus = PLAY_STATUS_REALIZAR_JUGADA;
    	break;
    	
    	case PLAY_STATUS_REALIZAR_JUGADA:
    	    
    	    
    	    
    	    // TODO:
    	    // Controlar las AI que no puedan cambiar cosas
    	    //public byte[] gameConfig = new byte[] {CONTROL_PLAYER, CONTROL_NONE, CONTROL_CPU, CONTROL_NONE};		// Tiene configurada c�mo ser�n los jugadores de la partida PLAYER/CPU/NONE
    	    if (gameConfig[ActualPlayer] == CONTROL_CPU)
    	    {
    	        playAI();    	    
    	    } else
    	    {
    	        playUser();
    	    }

    	break;
    	
    	case PLAY_STATUS_COMPROBAR_GANADOR:
    	    //waitStart();
    	    playStatus = PLAY_STATUS_COMPROBAR_GANADOR_COUNT;
    	break;
    	    
    	case PLAY_STATUS_COMPROBAR_GANADOR_COUNT:
    	    //#ifndef DEBUG
    	    /*if (waitFinished(TIEMPO_FICHA)) */playStatus = PLAY_STATUS_COMPROBAR_GANADOR_FIN;
    	    //#else
   		    /*if (keyMisc == 1)
   		    {*/
   		        playStatus = PLAY_STATUS_COMPROBAR_GANADOR_FIN;
   		    /*}*/   		    
    	    //#endif
    	    
    	break;
    	
    	case PLAY_STATUS_COMPROBAR_GANADOR_FIN:
    	    
    	    if (GetNumFichasOf(ActualPlayer) == 0) 
    	    {
    	        AutoGlove(gameText[TEXT_DOMINO], ActualPlayer, canvasWidth-(canvasWidth/5), (2*canvasHeight)/3);
    	        
    	        PuntosMano = Puntos(ActualPlayer+1)+Puntos(ActualPlayer+3)+((gameMode == GAME_MODE_COMPETITION)?0:Puntos(ActualPlayer+2));
    	        playStatus = PLAY_STATUS_MOSTRAR_GANADOR;
    	    } else
    	    {
    	        // Comprobar que no se haya cerrado el juego. Para ello las dos puntas tienen que ser iguales
    	        // y debe haber 8 partes del mismo n�mero en tablero
    	        if (partidaCerrada())
    	        {
    	            //popupInfo(new String[] {"Partida cerrada!"}); 
    	            // TODO: Cambiar texto
    	            AutoGlove(gameText[TEXT_PARTIDA_CERRADA], ActualPlayer, canvasWidth-(canvasWidth/5), (2*canvasHeight)/3);
    	            
//    	    	  INTRANCE - TROZO DE CODIGO DEDICADO A PORTING :P
    	            if (gameMode == GAME_MODE_COMPETITION)
    	            {
    	                ActualPlayer = JugadorMano%2;
    	                for (int ActPlayer=0; ActPlayer<MAX_EQUIPOS; ActPlayer++) if ((Puntos(JugadorMano+ActPlayer)+Puntos(JugadorMano+ActPlayer+2))<(Puntos(ActualPlayer)+Puntos(ActualPlayer+2))) ActualPlayer = JugadorMano+ActPlayer;
    	            } else
    	            {
    	                ActualPlayer = JugadorMano;
    	                for (int ActPlayer=0; ActPlayer<MAX_PLAYERS; ActPlayer++) if ((gameConfig[(JugadorMano+ActPlayer)%4] != CONTROL_NONE)&&(Puntos(JugadorMano+ActPlayer)<Puntos(ActualPlayer))) ActualPlayer = JugadorMano+ActPlayer;
    	            }
    	            PuntosMano = Puntos(0)+Puntos(1)+Puntos(2)+Puntos(3);
//        	      INTRANCE

    	            playStatus = PLAY_STATUS_MOSTRAR_GANADOR;
    	        } else
    	        {
    	            playStatus = PLAY_STATUS_TURNO_SIGUIENTE;
    	        }    	        
    	    }
    	break;
    	
    	case PLAY_STATUS_TURNO_SIGUIENTE:
    	    NextUtilPlayer();
    	    
    	    playStatus = PLAY_STATUS_PANTALLA_TIRA_JUGADORX;
    	break;
    	
    	case PLAY_STATUS_MOSTRAR_GANADOR:
    	    if ((ActualPlayer==0)||((modoTorneo)&&((ActualPlayer%2)==0)))
    	    {
    	          // Ganado
    	        	soundPlay(MUSICA_GANAR,1);
    	          popupMessage(gameText[TEXT_CLASIFICACION][3],gameText[TEXT_YOU_WIN]);
    	    } else
    	    {
    	          // Perdido
    	        	soundPlay(MUSICA_PERDER,1);
    	          popupMessage(gameText[TEXT_CLASIFICACION][3],gameText[TEXT_YOU_LOSE]);
    	    }
    	    controlCamera = true;
    	    listenerInit(SOFTKEY_MENU, SOFTKEY_CONTINUE);
    	    playStatus = PLAY_STATUS_SHOW_BOARD;
    	break;
    	
    	case PLAY_STATUS_SHOW_BOARD:
			    // Control de la c�mara en la mesa
			    posCameraX += (keyX * 10); posCameraY += (keyY * 10);
			    
			    if (posCameraX<0) posCameraX = 0;
			    if (posCameraY<0) posCameraY = 0;
			    if (posCameraX>mapSizeX*tileWidth) 	posCameraX 	= mapSizeX*tileWidth;
			    if (posCameraY>mapSizeY*tileHeight)	posCameraY	= mapSizeY*tileHeight;
			    scrollTick_CenterMax(posCameraX, posCameraY);    	        
				  
    	    if ((lastKeyMenu==0) && (keyMenu>0))
    	    {
    	        playStatus = PLAY_STATUS_MOSTRAR_RECUENTO;
    	    }
    	break;
    	
    	case PLAY_STATUS_MOSTRAR_RECUENTO :
    	    if ((ActualPlayer==0)&&(PuntosMano>=30)) popupMessage(new String[] {gameText[TEXT_BUENA_PUNTUACION][rnd(gameText[TEXT_BUENA_PUNTUACION].length)]});
    	    popupInitPizarraRecuento();
    	    playStatus = PLAY_STATUS_FIN_RONDA;
    	break;
    	
    	case PLAY_STATUS_FIN_RONDA:
//    	  INTRANCE
    	  if (modoTorneo)
    	  {
    	      PuntosActuales[ActualPlayer%2] += PuntosMano;
    	      MostrarResultados(SCORES_PARTIDA_TORNEO);
    	      if ((PuntosActuales[ActualPlayer%2])>=PuntosTorneo[TorneoActual])
    	      {
    	          byte EquipoA=EquiposPartida[0];
    	          byte EquipoB=EquiposPartida[1];
    	          byte ActResult = (byte)(PosJugador/exp(2,RondaActual-2));
    	          
    	          
    	          biosRefresh();
    	          forceRender();    	          
    	          popupInitInfo(gameText[TEXT_CLASIFICACION][3], new String[] {gameText[TEXT_EL_EQUIPO_GANADO_RONDA][0] + EquiposTorneoName[PosicionTorneo(EquiposPartida[ActualPlayer%2])] + gameText[TEXT_EL_EQUIPO_GANADO_RONDA][1]});
    	          biosWait();
    	          biosRefresh();
    	          forceRender();

    	          MostrarResultados(SCORES_RONDA_TORNEO);
    	          
    	          //System.out.println("***************Ronda Actual: "+RondaActual+" Act Result: "+ActResult+" Equipo A(0): "+EquipoA+" Equipo B: "+EquipoB+" Ganador: "+EquiposTorneoName[PosicionTorneo(EquiposPartida[ActualPlayer%2])]);
    	          ResultsTorneo[RondaActual-1][ActResult] = EquiposPartida[ActualPlayer%2];
    	          if (ResultsTorneo[RondaActual-1][ActResult] == EquipoA) EquipoB *= -1; else EquipoA *= -1;
    	          if (RondaActual>1)
    	          {
			          if (ResultsTorneo[RondaActual-2][ActResult*2]!=ResultsTorneo[RondaActual-1][ActResult]) ResultsTorneo[RondaActual-2][ActResult*2] *= -1;
			          if (ResultsTorneo[RondaActual-2][ActResult*2+1]!=ResultsTorneo[RondaActual-1][ActResult]) ResultsTorneo[RondaActual-2][ActResult*2+1] *= -1;
    	          }
		          for (int ActEquipo=0; ActEquipo<EquiposTorneo.length; ActEquipo++)
		          {
		              if (EquiposTorneo[ActEquipo]==Math.abs(EquipoA)) EquiposTorneo[ActEquipo] = EquipoA;
		              if (EquiposTorneo[ActEquipo]==Math.abs(EquipoB)) EquiposTorneo[ActEquipo] = EquipoB;
		          }
		            
		          PasoRonda = true;
		          PuntosActuales[0] = 0;
    	          PuntosActuales[1] = 0;
		          if ((ActualPlayer%2)!=0)
    	          {
		              popupMessage(gameText[TEXT_FRASES_MALAS_TORNEO][0],new String[] {gameText[TEXT_FRASES_MALAS_TORNEO][1+rnd((gameText[TEXT_FRASES_MALAS_TORNEO].length-1)/2)*2],gameText[TEXT_FRASES_MALAS_TORNEO][(rnd*2)+2]});
		              popupMessage(gameText[TEXT_RONDA_ELIMINATORIA]);
		              BorrarTorneo();
    	              playExit = 3;
    	              break;
    	          }
    	      } else
    	      {
    	          
    	      }
    	  } else
    	  {
    	      if (ActualPlayer==0)
    	      {
    	          PuntosActuales[ActualPlayer] += PuntosMano;
    	          if (modoTorneo == false)
    	          {
    	              AutoGlove(new String[] {gameText[TEXT_FRASES_BUENAS][rnd(gameText[TEXT_FRASES_BUENAS].length)]}, getNextUtilPlayer(), canvasWidth-(canvasWidth/5), (2*canvasHeight)/3);
    	              //biosWait();
    	          		//popupMessage();
    	          }
    	      } else
    	      {
    	          if (modoTorneo == false)
    	          {
	    	          AutoGlove(new String[] {gameText[TEXT_FRASES_MALAS][rnd(gameText[TEXT_FRASES_MALAS].length)]}, ActualPlayer, canvasWidth-(canvasWidth/5), (2*canvasHeight)/3);
	    	          //biosWait();
    	          }
    	      }
    	      biosRefresh();
	          forceRender();
    	      MostrarResultados(SCORES_PARTIDA_LIBRE);
    	  }

    	  
    	  // popupInitImage(img)
    	  playStatus = PLAY_STATUS_REINICIAR_PARTIDA;
    	  //playExit = 3;
    	
    	case PLAY_STATUS_REINICIAR_PARTIDA:
    	    if (modoTorneo)
    	    {
    	        
    	    } else
    	    {
    	        if (ActualPlayer != 0)
    	        {
    	            popupInitAsk(null, gameText[TEXT_REJUGAR][0], POPUP_ACTION_EXIT);
    	            biosWait();
	    			
	    			if (popupResult)
	    			{
	    			    numPartidas = 0;
	    				PuntosActuales[0] = 0;
	    			} else
	    			{
	    			    playExit = 3;
	    			    break;    			
	    			}
    	        }
    	    }
    	    ContinuandoJuego = true;

    	    playStatus = PLAY_STATUS_CREATE_GAME;
    	break;
//    	INTRANCE
    	
    	case PLAY_STATUS_PRE_TIRANDO_FICHA:
		    if (lastKeyMenu == 0)
		    {
		 	    if (((keyMenu == 2) && (waitSelection)) ||
		 	    	  //(waitFinished(TIEMPO_FICHA) && (!waitSelection)))
		 	          ((TIEMPO_FICHA<=0) && (!waitSelection)))
		 	    {
		 	          waitSelection = false; // Para que no intente pintar los botones
		 	        	FichaBlink = 0;
		 	        	preTirandoFicha(ActualPlayer);
		    	      postTirandoFicha(ActualPlayer);
		    	      playStatus = PLAY_STATUS_COMPROBAR_GANADOR; // TODO: Esto es para debugar
	 	      }
		 	    
    	  }
		  if ((lastKeyX == 0) && (waitSelection))
		  {
	    	  if (keyX < 0)
	    	  {
	    	      setTableMemory(LastBranchX, LastBranchY);
	    	      capturedTable = false;
	    	      FichaBlink = BLINK_TIME;
	    	      DefaultBranch = LEFT_BRANCH;
	    	  }
	    	  if (keyX > 0)
	    	  {
	    	      setTableMemory(LastBranchX, LastBranchY);
	    	      capturedTable = false;
	    	      FichaBlink = BLINK_TIME;
	    	      DefaultBranch = RIGHT_BRANCH;
	    	  }
		  }
    	break;
    }
    


// -----------------------------
// Ejemplo por defecto:
	/*
	pelotaX += pelotaSideX * 3;
	pelotaY += pelotaSideY * 4;

	pelotaSideY --;
	if (pelotaX < 0 || pelotaX > canvasWidth) {pelotaSideX *= -1;}
	if (pelotaY < 0 || pelotaY > canvasHeight) {pelotaSideY *= -1;}
	*/
// =============================


	
	if (playExit!=0) {return true;}		// Debemos salir de fa fase??  Muerte, siguinte nivel, etc...

	playShow = true;		// Notificamos para el "repaint()" se refresque el play Engine. (motor del juego)

	return false;
}

// -------------------
// play Draw
// ===================

public void playDraw()
{
    TIEMPO_FICHA--;
    switch(playStatus)
    {
    	//case PLAY_STATUS_REPARTIR_FICHAS:
    	case PLAY_STATUS_PRE_TIRANDO_FICHA:
    	case PLAY_STATUS_MOSTRAR_CLASIFICACION:
    	case PLAY_STATUS_DECIDIR_QUIEN_EMPIEZA:
    	case PLAY_STATUS_PANTALLA_TIRA_JUGADORX:
     	case PLAY_STATUS_REALIZAR_JUGADA:
     	case PLAY_STATUS_COMPROBAR_GANADOR:
     	case PLAY_STATUS_SHOW_BOARD:
    	case PLAY_STATUS_MOSTRAR_GANADOR:
    	case PLAY_STATUS_FIN_RONDA:
    	case PLAY_STATUS_MOSTRAR_RECUENTO :
     	    
     	    /*
     	    System.out.println("																			PINTAMOS");
     	    canvasFillDraw(0xff0000);
     	    */
     	    scrollTick_CenterMaxSlow(posCameraX, posCameraY);
     	    
     	    //ZonaFondo(0, 0, canvasWidth, canvasTop, 8, int Tipo, int MAXY);     	    
     	    scrollDraw(scr);
     	    pintaNumFichas();     	    
     	    
   	    	//try	{Thread.sleep( 2000 );} catch(Exception e) {}
     	    if (controlCamera)
     	    {
     	        pintaFlechasScroll();
     	    }

 	        pintaFichasPlayer();
 	        
 	        pintaMovilAI();
     	    
     	    if (playStatus==PLAY_STATUS_PRE_TIRANDO_FICHA) preTirandoFicha(ActualPlayer);
    	break;
    	    
    }

}


// -------------------
// play Refresh
// ===================

public void playRefresh()
{
	playShow = true;
}

// <=- <=- <=- <=- <=-

final static int FLECHA_LEFT		= 0;
final static int FLECHA_RIGHT		= 1;
final static int FLECHA_UP			= 2;
final static int FLECHA_DOWN		= 3;
public void pintaFlechasScroll()
{
    int Click;
    int TamFlechas 			= printSpriteWidth(flechas_gfx_cor, 0);
    int Color;
    
    // Flecha izquierda de scroll
    if (scrollX>0)
    {
        Color = 0;
    } else
    {
        Color = 4;
    }
    
    if (keyX<0) Click = 0; else Click = MARGEN_FLECHAS;
    spriteDraw(flechas_gfx,  Click, (CanvasScrollHeight - TamFlechas) / 2,  Color + FLECHA_LEFT, flechas_gfx_cor);
    
    
    // Flecha derecha de scroll
    if (scrollX<((mapSizeX*tileWidth) - canvasWidth))
    {
        Color = 0;
    } else
    {
        Color = 4;
    }
    if (keyX>0) Click = 0; else Click = MARGEN_FLECHAS;
    spriteDraw(flechas_gfx,  canvasWidth - TamFlechas - Click, (CanvasScrollHeight - TamFlechas) / 2,  Color + FLECHA_RIGHT, flechas_gfx_cor);
    
    // Flecha superior
    if (scrollY>0)
    {
        Color = 0;
    } else
    {
        Color = 4;   
    }
    if (keyY<0) Click = 0; else Click = MARGEN_FLECHAS;
    spriteDraw(flechas_gfx,  (canvasWidth - TamFlechas) / 2, canvasTop + Click,  Color + FLECHA_UP, flechas_gfx_cor);

    // Flecha inferior
    if (scrollY<((mapSizeY*tileHeight) - CanvasScrollHeight))
    {
        Color = 0;
    } else
    {
        Color = 4;
    }    
    if (keyY>0) Click = 0; else Click = MARGEN_FLECHAS;
    spriteDraw(flechas_gfx,  (canvasWidth - TamFlechas) / 2, CanvasScrollHeight - TamFlechas - (MARGEN_FLECHAS*3) - Click,  Color + FLECHA_DOWN, flechas_gfx_cor);
}

public void playUser()
{
  if (mostrarPopupRobarPasar)
  {
      popupRobarPasar();
      mostrarPopupRobarPasar = false;
  }
  
  if ((gameWithHelp==1) && (mostrarPopupTutorial) && (hintCount<MAX_HINTS) && (modoTorneo == false))
  {
      popupTutorial(gameText[TEXT_HELP_OBJETIVO_S+(hintCount++)]);
      if (popupResult==false) mostrarPopupTutorial = false;
      if (hintCount == MAX_HINTS) gameWithHelp=0;
  }
  
	// Diferentes controles si estamos en control de scroll (osea controlCamera = true) o estamos en control de fichas
	if (controlCamera)
	{
	    // Control de la c�mara en la mesa
	    posCameraX += (keyX * 10); posCameraY += (keyY * 10);
	    
	    if (posCameraX<0) posCameraX = 0;
	    if (posCameraY<0) posCameraY = 0;
	    if (posCameraX>mapSizeX*tileWidth) 	posCameraX 	= mapSizeX*tileWidth;
	    if (posCameraY>mapSizeY*tileHeight)	posCameraY	= mapSizeY*tileHeight;
	    scrollTick_CenterMax(posCameraX, posCameraY);    	        
	    
	} else
	{
	    // Control de fichas del player
	    
	    // Cambiar la ficha seleccionada ______________
	    if (lastKeyX == 0)
	    {
	        if (keyX != 0)
	        {
	            fichaSelected[ActualPlayer]+=keyX;
	            
	            // Correcci�n del scroll de fichas del player
	            corrigeSeleccionPlayer();
	        }
	    }
	    // ____________________________________________
	    
	    // Tirar una ficha ____________________________
	    if (lastKeyMenu == 0)
	    {
	 	    if (keyMenu > 0)
	 	    {
		        if (puedoTirarAlgunaFicha())
		        {
		            tirarFicha(ActualPlayer);
		            //playStatus = PLAY_STATUS_COMPROBAR_GANADOR;
		        } else
		        {
		            if (puedoRobar())
		            {
		                robarFicha();
		                mostrarPopupRobarPasar = true;
		            } else
		            {
		                playStatus = PLAY_STATUS_COMPROBAR_GANADOR;
		            }		            
		        }		        
	 	    }	 	    
	    }
 	    // ____________________________________________
 	    
	}
	// Control de teclado para comprobar si pasamos de modo selecci�n de fichas a modo scroll
	if (lastKeyMisc == 0)
		{
		    if (keyMisc == 10)
		    {
		        controlCamera = !controlCamera;
		    }
		    
		    //#ifdef DEBUG
		    if (keyMisc == 9)
		    {
		        FichasDe[PLAYER_1][0] = 22;
		        FichasDe[PLAYER_1][1] = 33;
		        FichasDe[PLAYER_1][GET_NUM_FICHAS] = 2;
		        
		        FichasDe[PLAYER_3][0] = 62;
		        FichasDe[PLAYER_3][1] = 51;
		        FichasDe[PLAYER_3][2] = 66;
		        FichasDe[PLAYER_3][3] = 60;
		        FichasDe[PLAYER_3][4] = 63;
		        FichasDe[PLAYER_3][5] = 65;
		        FichasDe[PLAYER_3][6] = 10;
		        FichasDe[PLAYER_3][7] = 32;
		        FichasDe[PLAYER_3][8] = 44;
		        FichasDe[PLAYER_3][GET_NUM_FICHAS] = 9;
		        
 		        FichasDe[MESA][0] = 55;
		        FichasDe[MESA][1] = 54;
 		        FichasDe[MESA][2] = 42;
		        FichasDe[MESA][3] = 52;
 		        FichasDe[MESA][4] = 50;
		        FichasDe[MESA][5] = 0;
 		        FichasDe[MESA][6] = 20;
		        FichasDe[MESA][7] = 21;
 		        FichasDe[MESA][8] = 61;
		        FichasDe[MESA][9] = 64;
 		        FichasDe[MESA][10] = 40;
		        FichasDe[MESA][11] = 53;
 		        FichasDe[MESA][12] = 31;
		        FichasDe[MESA][13] = 11;
 		        FichasDe[MESA][14] = 30;
		        FichasDe[MESA][15] = 43;
 		        FichasDe[MESA][16] = 41;
 		        FichasDe[MESA][GET_NUM_FICHAS] = 17;
 		        
 		        FichasDe[MONTON][GET_NUM_FICHAS] = 0;
 		        
 		        Branch[LEFT_BRANCH] = 4;
 		        Branch[RIGHT_BRANCH] = 4;
 		        
 		        //levelAI[2]=AI_4;

		        DebugRecipients();
		        playStatus = PLAY_STATUS_COMPROBAR_GANADOR;
		    }
		    if (keyMisc == 7)
		    {
		        robarFicha();
		    }
		    if (keyMisc == 1)
		    {
		        ActualPlayer=0;
		        PuntosMano = 10;
		        playStatus = PLAY_STATUS_MOSTRAR_GANADOR;
		    }
		    if (keyMisc == 3)
		    {
		        ActualPlayer=1;
		        PuntosMano = 10;
		        playStatus = PLAY_STATUS_MOSTRAR_GANADOR;
		    }
		    //#endif
		}

}

public void playAI()
{
    if (levelAI[ActualPlayer]==AI_0)
    {
		    // Tipo de AI - B�SICA - 
		    // Ficha que puede tirar ficha que tira
		    for (fichaSelected[ActualPlayer] = 0; fichaSelected[ActualPlayer] < GetNumFichasOf(ActualPlayer); fichaSelected[ActualPlayer]++)
		    {
		        if (tirarFicha(ActualPlayer))
		        {
		            //playStatus = PLAY_STATUS_COMPROBAR_GANADOR;
		            return;
			 	    }
		    }
    }
    // Calculamos los pesos de las fichas para saber cual es recomendable tirar
    if (levelAI[ActualPlayer]>=AI_0)
    {
        calcularPesos();
        //#ifdef DEBUG
        	DebugRecipients();
        //#endif
        seleccionarFichaMayorPeso();
		    if (tirarFicha(ActualPlayer))
		    {
		        return;
		    }
    }
    
    // Parte com�n a todas las AI
    
	  // Robar una ficha si hay para robar, si no pasamos
	  if (GetNumFichasOf(MONTON) > 0)
	  {
        // Robamos
	      //System.out.println("AI: ROBAR FICHA");
	      robarFicha();
        //popupInfo(gameText[TEXT_ROBO_FICHA]);
	      String[] TextRoboFicha = new String[] {"",""};
	      TextRoboFicha[0] = gameText[TEXT_ROBO_FICHA][0];
	      TextRoboFicha[1] = gameText[TEXT_FICHAS_MONTON][0]+" "+GetNumFichasOf(MONTON)+")";
	      
        AutoGlove(TextRoboFicha,ActualPlayer, canvasWidth-(canvasWidth/5), (2*canvasHeight)/3);
        
    } else
    {
        // Pasamos
        //popupInfo(gameText[TEXT_PASO]);
        AutoGlove(gameText[TEXT_PASO],ActualPlayer, canvasWidth-(canvasWidth/5), (2*canvasHeight)/3);
        //System.out.println("AI: PASAMOS");
        playStatus = PLAY_STATUS_COMPROBAR_GANADOR;
    }
}

public boolean controlCierre(int FichasControlCierre, int ValorFichaCierre)
{
    int Repetidas = 0;
    
    // Contar cuantas fichas de ese palo hay en mesa
    for (int NumFicha = 0; NumFicha < GetNumFichasOf(MESA); NumFicha++)
    {
        if (
            (getTopFicha(FichasDe[MESA][NumFicha]) 		== ValorFichaCierre) ||
            (getBottomFicha(FichasDe[MESA][NumFicha]) == ValorFichaCierre)
           )
            
        {
                Repetidas++;
        }
    }
    if (Repetidas >= FichasControlCierre) 
    {
        return(true);
    }
    return(false);
}

public boolean EsDobleMuerto(int IDFicha)
{
    return((esDoble(IDFicha)) && (controlCierre(MAX_VALOR_FICHA, getTopFicha(IDFicha))));
}

public boolean partidaCasiCerrada(int ValorFichaCierre)
{
    return(controlCierre(MAX_VALOR_FICHA, ValorFichaCierre));
}

public boolean partidaCerrada()
{  
    if (Branch[LEFT_BRANCH]==Branch[RIGHT_BRANCH])
    {
        return(controlCierre(MAX_VALOR_FICHA+1,Branch[LEFT_BRANCH]));
    }
   
    return(false);
    
    //return (GetNumFichasOf(MESA)==4);
}

public int interesaCerrar(int IDFicha)
{
    int Resultado = 0;
    int NumFichasCompanyero = 0;
    int NumMenosFichasContrarios = 1000;
    int codPlayer;
    
    int PuntosMedios;
    int PuntosPropios;
    int PuntosMesa;
    int ValorFichaATirar;
    int DoblesMuertos;
    
    int MiPeso;
    int PesoContrario;
    
    // Contar fichas de los contrarios
    for (int NumPlayer = 1; NumPlayer<MAX_PLAYERS; NumPlayer++)
    {
        codPlayer = (ActualPlayer + NumPlayer) % 2;
        //System.out.println("codPlayer				= "+codPlayer);	
        if (gameMode==GAME_MODE_COMPETITION)
        {	            
            // Tener en cuenta equipos
            if ((codPlayer % 2) == 0)
            {
                // va conmigo
                NumFichasCompanyero = GetNumFichasOf(codPlayer);
                //System.out.println("Va conmigo NumFichasCompanyero = "+NumFichasCompanyero);
            } else
            {
                // No va conmigo comparar con el m�s peligroso
                if (GetNumFichasOf(codPlayer) < NumMenosFichasContrarios)
                {
                    NumMenosFichasContrarios = GetNumFichasOf(codPlayer);
                    //System.out.println("No va conmigo NumMenosFichasContrarios = "+NumMenosFichasContrarios);
                }
            }
        } else
        {
            // No va conmigo. Comparar con el m�s peligroso
            if (GetNumFichasOf(codPlayer) < NumMenosFichasContrarios) NumMenosFichasContrarios = GetNumFichasOf(codPlayer);
        }
    }
    
    //System.out.println("NumMenosFichasContrarios = "+NumMenosFichasContrarios);
    //System.out.println("NumFichasCompanyero      = "+NumFichasCompanyero);
    
    // Contar fichas propias
    	// Tener en cuenta equipos
    
    // Calcular valor medio de las fichas que quedan
    	// Se suman los valores de las fichas del MONTON y del resto de PLAYERS 
    	// es decir todas las fichas que desconozco y est�n por jugar
    	// y se divide entre el n�mero de fichas pendientes de jugar (sin contar las propias)
    	PuntosPropios = 0;
    	PuntosMesa    = 0;
    	DoblesMuertos	= 0;
    	for (int numFicha = 0; numFicha<GetNumFichasOf(ActualPlayer); numFicha++)
    	{
    	    PuntosPropios += getTopFicha(FichasDe[ActualPlayer][numFicha]) + getBottomFicha(FichasDe[ActualPlayer][numFicha]);
    	    if (EsDobleMuerto(FichasDe[ActualPlayer][numFicha])) DoblesMuertos++;
    	}
    	ValorFichaATirar = (getTopFicha(FichasDe[ActualPlayer][fichaSelected[ActualPlayer]]) + getBottomFicha(FichasDe[ActualPlayer][fichaSelected[ActualPlayer]]));
    	PuntosPropios -= ValorFichaATirar; 
    	for (int numFicha = 0; numFicha<GetNumFichasOf(MESA); numFicha++)
    	{
    	    PuntosMesa +=getTopFicha(FichasDe[MESA][numFicha]) + getBottomFicha(FichasDe[MESA][numFicha]);
    	}
    	PuntosMesa += ValorFichaATirar;
    	int ValorMedio = (TotalPuntosJuego - PuntosPropios - PuntosMesa) / (TotalFichasJuego - GetNumFichasOf(MESA) - GetNumFichasOf(ActualPlayer)); 

    	//System.out.println("Puntos propios 				= "+PuntosPropios);
    	//System.out.println("Puntos mesa  				  = "+PuntosMesa);
    	//System.out.println("Valor medio           = "+ValorMedio);
    	//System.out.println("TotalPuntosJuego      = "+TotalPuntosJuego);
    	//System.out.println("TotalFichasJueog      = "+TotalFichasJuego);
    	
    	
    // Calcular el valor propio
    	// Por equipos
    		// Sumar mis puntos reales (menos la ficha a tirar) m�s (num fichas compa�ero * valor medio)
    		// Num fichas contrarios * valor medio
    
    	// Individual
    		// Sumar mis puntos reales (menos la ficha a tirar)
    		// De cada jugador (num fichas * valor medio)
    		// Cojer el que tiene la puntuaci�n m�s baja y compararlo con el propio
    
    	if (gameMode == GAME_MODE_COMPETITION)
    	{
    	    MiPeso 				= PuntosPropios + (GetNumFichasOf((ActualPlayer + 2) % 2) * ValorMedio);
    	    PesoContrario	= (GetNumFichasOf((ActualPlayer + 1) % 2) * ValorMedio) + (GetNumFichasOf((ActualPlayer + 3) % 2) * ValorMedio);
    	} else
    	{
    	    MiPeso 				= PuntosPropios;
    	    PesoContrario	= ValorMedio * NumMenosFichasContrarios;
    	}
	    //System.out.println("MiPeso 				= "+MiPeso);
	    //System.out.println("PesoContrario = "+PesoContrario);
	    

	    Resultado = PesoContrario - MiPeso;
	    
	    if (Resultado >= 0)
	    {
	        // Mirar si tengo alg�n doble muerto e interesa cerrar
	        Resultado += (DoblesMuertos * PESO_DOBLE_MUERTO);
	    }
	    
    // Resultado
    	// Diferencia de c�lculos de valor propio
	    //System.out.println("RESULTADO = "+Resultado);

    return(Resultado);
}

public void setCamera(int posX, int posY)
{
    posCameraX = posX * 2;
    posCameraY = posY * 2;
    //scrollTick_CenterMaxSlow(posCameraX, canvasTop+posCameraY);
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
			Debug.println("Prefs corruptas, Inicializando de cero...");
		//#endif
			savePrefs();
			return;
		}

		gameSound = dis.readByte() != 0;
		gameVibra = dis.readByte() != 0;

		gamePlayerNames = new String[dis.readInt()];
		for (int i=0 ; i<gamePlayerNames.length ; i++)
		{
			gamePlayerNames[i] = dis.readUTF();
		}

		gameDifficulty = dis.readByte();
		gamePoints = dis.readByte();
		gameControl = dis.readByte();
		gameWithHelp = dis.readByte();
		gameRules = dis.readByte();

		
		SavedRondaActual	= dis.readByte();
        SavedTorneo			= dis.readByte();
        int SavedNumEquipos = (byte)(gameText[TEXT_TORNEO0+SavedTorneo].length+1);    
        int SavedNumRondas = 0;
        while (SavedNumEquipos/exp(2,SavedNumRondas)>=1) SavedNumRondas++;
        SavedNumRondas++;
        SavedEquiposTorneo	= new byte[SavedNumEquipos];
        for (int ActualPos=0; ActualPos<SavedNumEquipos; ActualPos++) SavedEquiposTorneo[ActualPos] = dis.readByte(); 
        SavedNombreEquipo	= dis.readUTF();
        SavedResultsTorneo = new byte[SavedNumRondas][];
        for (int ActRonda=0; ActRonda<SavedNumRondas; ActRonda++)
        {
            SavedResultsTorneo[ActRonda] = new byte[exp(2,(SavedNumRondas-ActRonda)-1)/2];
            for (int ActResult=0; ActResult<SavedResultsTorneo[ActRonda].length; ActResult++) SavedResultsTorneo[ActRonda][ActResult] = dis.readByte();
        }
        TorneoLock = dis.readByte();
        SavedEnemy = dis.readByte();

		userHashId = dis.readUTF();		// HASH ID del jugador
		gameMoreGamesVisited = dis.readByte() != 0;	// flag que nos indica si hemos visitado "Mas Juegos"



	} catch(Exception e) {}
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

		dos.writeByte(gameSound? 1:0);
		dos.writeByte(gameVibra? 1:0);

		dos.writeInt(gamePlayerNames.length);
		for (int i=0 ; i<gamePlayerNames.length ; i++)
		{
			dos.writeUTF(gamePlayerNames[i]);
		}

		dos.writeByte(gameDifficulty);
		dos.writeByte(gamePoints);
		dos.writeByte(gameControl);
		dos.writeByte(gameWithHelp);
		dos.writeByte(gameRules);
		dos.writeByte(SavedRondaActual);
		dos.writeByte(SavedTorneo);
        for (int ActualPos=0; ActualPos<SavedEquiposTorneo.length; ActualPos++) dos.writeByte(SavedEquiposTorneo[ActualPos]);
        dos.writeUTF(SavedNombreEquipo);
        for (int ActRonda=0; ActRonda<SavedResultsTorneo.length; ActRonda++)
        {
            for (int ActResult=0; ActResult<SavedResultsTorneo[ActRonda].length; ActResult++) dos.writeByte(SavedResultsTorneo[ActRonda][ActResult]);
        }
        dos.writeByte(TorneoLock);
        dos.writeByte(SavedEnemy);

		dos.writeUTF(userHashId);		// HASH ID del jugador
		dos.writeByte(gameMoreGamesVisited? 1:0);	// flag que nos indica si hemos visitado "Mas Juegos"



	// Almacenamos las prefs
		byte[] prefsData = baos.toByteArray();
		byte checksum = 0; for (int i=1 ; i<prefsData.length ; i++) {checksum += prefsData[i];}
		prefsData[0] = checksum;
		updatePrefs(prefsData);		// Almacenamos byte[]

	} catch(Exception e)
	{
	//#ifdef DEBUG
		Debug.println("Error guardando prefs");
	//#endif
	}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// cheats - Engine
// ===================
// *******************

// -------------------
// cheats
// ===================

public void cheats()
{
}

// <=- <=- <=- <=- <=-









// < FICHAS DE DOMIN� _________________________________________________________
// BIGTHOR 

final static int	MAX_VALOR_FICHA				= 6;
final static int 	NUM_FICHAS		 				= 28;
final static int  NUM_FICHAS_FOR_PLAYER = 7;
final static int	GET_NUM_FICHAS				= 28;
final static int 	NUM_RECIPIENTS 				= 8;
final static int	PLAYER_1			 				= 0;
final static int	PLAYER_2			 				= 1;
final static int 	PLAYER_3			 				= 2;
final static int	PLAYER_4			 				= 3;
final static int	MONTON				 				= 4;
final static int	MESA					 				= 5;
final static int  PESOS_L								= 6;
final static int  PESOS_R								= 7;


public int[][] FichasDe = new int[NUM_RECIPIENTS][NUM_FICHAS+1];	// Tiene una posici�n m�s para guardar el n�mero de fichas que tiene

// Render de fichas de player
final static int				BACKGROUND_FICHA		= 9;
final static int				UP_FICHA						= 7;
final static int				DOWN_FICHA					= 8;
final static int				UP_FICHA_REVERSE 		= 10;
final static int				DOWN_FICHA_REVERSE	= 11;
final static int				DENIED_FICHA				= 12;
final static int				PERMITED_FICHA			= 13;

// Indicador de posici�n del valor de la ficha
final static int				VALUE_FICHA_NONE		= -1;
final static int				VALUE_FICHA_TOP			= 0;
final static int 				VALUE_FICHA_BOTTOM	= 1;

public void pintaFichasPlayer()
{
    int margenPuntas = 3;
    
    // Pintamos la mesa de fondo
    
    int NumFichasToShow = GameNumFichasToShow;    
    if (NumFichasToShow > GetNumFichasOf(ActualPlayer))
    {
        NumFichasToShow = GetNumFichasOf(ActualPlayer);
    }

    int CanvasStartX		= (canvasWidth - (NumFichasToShow * WidthFicha)) / 2;//(canvasWidth / 4);

    // Limpiamos el fondo de las fichas
    int NumVueltas;
    NumVueltas = (canvasWidth / mesap_gfx.getWidth()) + 1;
    for (int Contador = 0; Contador < NumVueltas; Contador++)
    {
        imageDraw(mesap_gfx, (Contador * mesap_gfx.getWidth()), CanvasScrollHeight);
    }
            
    //fillDraw(0xFFFFFF, 0, CanvasStartY + 4, canvasWidth, canvasHeight);
    if (gameConfig[ActualPlayer] == CONTROL_PLAYER)
    {
		    // Pintamos los dos lados del tablero
		    if (Branch[LEFT_BRANCH] != NOT_PUT)
		    {
		        scr.setClip(0, 0, canvasWidth, canvasHeight);
		        scr.setColor(0xFFFFFF);
		        //scr.fillArc(margenPuntas, CanvasScrollHeight + ((canvasWidth - CanvasScrollHeight - WidthFicha) / 2),WidthFicha,WidthFicha,0,360);
		        imageDraw(redonda_gfx, margenPuntas, CanvasScrollHeight + ((canvasWidth - CanvasScrollHeight - WidthFicha) / 2));
		        spriteDraw(fichasp_gfx,  margenPuntas, CanvasScrollHeight + ((canvasWidth - CanvasScrollHeight - WidthFicha) / 2),  Branch[LEFT_BRANCH], fichasp_gfx_cor);
		    }
		    if (Branch[RIGHT_BRANCH] != NOT_PUT)
		    {
		        scr.setClip(0, 0, canvasWidth, canvasHeight);
		        scr.setColor(0xFFFFFF);
		        //scr.fillArc(canvasWidth - WidthFicha - margenPuntas, CanvasScrollHeight + ((canvasWidth - CanvasScrollHeight - WidthFicha) / 2),WidthFicha,WidthFicha,0,360);
		        imageDraw(redonda_gfx, canvasWidth - WidthFicha - margenPuntas, CanvasScrollHeight + ((canvasWidth - CanvasScrollHeight - WidthFicha) / 2));
		        spriteDraw(fichasp_gfx,  canvasWidth - WidthFicha - margenPuntas, CanvasScrollHeight + ((canvasWidth - CanvasScrollHeight - WidthFicha) / 2),  Branch[RIGHT_BRANCH], fichasp_gfx_cor);
		    }
    }

    // Pintamos las flechas de scroll
    int TamFlechas 			= printSpriteWidth(flechas_gfx_cor, 0);    
    // Pintamos flecha de scroll de fichas izquierda
    if (fichaOffsetStart[ActualPlayer]>0)
    {
        spriteDraw(flechas_gfx,  MARGEN_FLECHAS, canvasHeight - TamFlechas - MARGEN_FLECHAS,  FLECHA_LEFT, flechas_gfx_cor);
    }
    // Pintamos flecha de scroll de fichas derecha
    if (fichaOffsetStart[ActualPlayer]<GetNumFichasOf(ActualPlayer)-NumFichasToShow)
    {
        spriteDraw(flechas_gfx,  canvasWidth - TamFlechas - MARGEN_FLECHAS, canvasHeight - TamFlechas - MARGEN_FLECHAS,  FLECHA_RIGHT, flechas_gfx_cor);
    }    
    
    // Pintamos las fichas
    for (int NumFicha=fichaOffsetStart[ActualPlayer]; (NumFicha < fichaOffsetStart[ActualPlayer]+NumFichasToShow); NumFicha++)
    {
        boolean Selected = fichaSelected[ActualPlayer]==NumFicha;
        int puedoTirar = 0;
        if ((Selected) && (puedoTirarFicha(FichasDe[ActualPlayer][fichaSelected[ActualPlayer]]) != NOT_PUT)) puedoTirar = 2;
        /*if (
	        	 (Selected==false) ||
	        	 ((Selected == true) && (((FichaBlink/3)%2)!=0))
        	 )
        {*/
            pintaFichaPlayer(FichasDe[ActualPlayer][NumFicha],CanvasStartX+(WidthFicha*(NumFicha-fichaOffsetStart[ActualPlayer])),CanvasStartY-puedoTirar, Selected);
        /*}*/
    }

}

/*
public void pintaFichaPlayer(byte IdFichaToDraw, int posX, int posY)
{
    pintaFichaPlayer(IdFichaToDraw, posX, posY, false);    
}
*/

public void pintaFichaPlayer(int IdFichaToDraw, int posX, int posY, boolean Selected)
{
    int posYTemp = posY;
    int ColorFlechas;
    //spriteDraw(fichasp_gfx,  posX, posY,  7, fichasp_gfx_cor, 1);
    //spriteDraw(fichasp_gfx,  posX, posY,  7, fichasp_gfx_cor);
    
    // Pintar la sombra de la ficha
    posYTemp -= (printSpriteHeight(fichasp_gfx_cor, UP_FICHA) - printSpriteHeight(fichasp_gfx_cor, BACKGROUND_FICHA));
    
    spriteDraw(fichasp_gfx,  posX, posYTemp,  BACKGROUND_FICHA, fichasp_gfx_cor); 
    posYTemp += printSpriteHeight(fichasp_gfx_cor, UP_FICHA);

    if (
            ((gameConfig[ActualPlayer] == CONTROL_PLAYER) && ((playStatus >= PLAY_STATUS_REALIZAR_JUGADA) && (playStatus <= PLAY_STATUS_COMPROBAR_GANADOR)))
            &&
            (playStatus < PLAY_STATUS_POST_TIRANDO_FICHA)  
       )
    {
	    // Pintar la parte superior de la ficha
	    int TopFicha = posYTemp;
	    
	    spriteDraw(fichasp_gfx,  posX, posYTemp,  UP_FICHA, fichasp_gfx_cor);
	    spriteDraw(fichasp_gfx,  posX, posYTemp,  getTopFicha(IdFichaToDraw), fichasp_gfx_cor);
	    posYTemp += printSpriteHeight(fichasp_gfx_cor, UP_FICHA);
	    
	    // Pintar la parte inferior de la ficha
	    spriteDraw(fichasp_gfx,  posX, posYTemp,  DOWN_FICHA, fichasp_gfx_cor);
	    spriteDraw(fichasp_gfx,  posX, posYTemp,  getBottomFicha(IdFichaToDraw), fichasp_gfx_cor);
	    posYTemp += printSpriteHeight(fichasp_gfx_cor, DOWN_FICHA);
	    
	    int BottomFicha = posYTemp;
	    // Hay que hacer Parte superior + Factorial de parte inferior para averiguar la posici�n exacta en el array de Fichas
	    
	    /*
	    // Pintar la ficha seleccionada
	    if (Selected)
	    {
	        int ColorSelected;
	        // TODO: 
	        // Suponemos de momento que se puede tirar cualquiera
	        if (puedoTirarFicha(FichasDe[ActualPlayer][fichaSelected[ActualPlayer]]) != NOT_PUT)
	        {
	            ColorSelected = 0x00FF00;
	        } else
	        {
	            ColorSelected = 0xFF0000;
	        }
	        //rectDraw(ColorSelected, posX, TopFicha, printSpriteWidth(fichasp_gfx_cor, UP_FICHA), BottomFicha-TopFicha);
	        rectDraw(ColorSelected, posX, posY, printSpriteWidth(fichasp_gfx_cor, UP_FICHA), BottomFicha-posY);
	    }
	    */
	    if (puedoTirarFicha(IdFichaToDraw) != NOT_PUT)
	    {
	        ColorFlechas = 0;
	        spriteDraw(fichasp_gfx,  posX, canvasHeight - 18,  PERMITED_FICHA, fichasp_gfx_cor);
	    }	else
	    {
	        ColorFlechas = 4;
	        spriteDraw(fichasp_gfx,  posX, canvasHeight - 18,  DENIED_FICHA, fichasp_gfx_cor);
	    }
	    if ((Selected) && (controlCamera==false))
	    {	        
	        spriteDraw(flechas_gfx,  posX + ((printSpriteWidth(fichasp_gfx_cor, ColorFlechas + UP_FICHA) - printSpriteWidth(flechas_gfx_cor, ColorFlechas + FLECHA_DOWN)) / 2), posY /*- 4*/ - Math.abs(senoidal),  ColorFlechas + FLECHA_DOWN, flechas_gfx_cor);
	        //spriteDraw(flechas_gfx,  posX + ((printSpriteWidth(fichasp_gfx_cor, UP_FICHA) - printSpriteWidth(flechas_gfx_cor, FLECHA_DOWN)) / 2), BottomFicha + 4 + Math.abs(senoidal),  FLECHA_UP, flechas_gfx_cor);
	        spriteDraw(flechas_gfx,  posX + ((printSpriteWidth(fichasp_gfx_cor, ColorFlechas + UP_FICHA) - printSpriteWidth(flechas_gfx_cor, ColorFlechas + FLECHA_DOWN)) / 2), BottomFicha /*+ 4*/ + Math.abs(senoidal) - printSpriteHeight(flechas_gfx_cor, ColorFlechas + FLECHA_DOWN),  ColorFlechas + FLECHA_UP, flechas_gfx_cor);
	    }
    } else
    {
	    // Pintar la parte superior de la ficha
	    int TopFicha = posYTemp;
	    
	    spriteDraw(fichasp_gfx,  posX, posYTemp,  UP_FICHA_REVERSE, fichasp_gfx_cor);
	    posYTemp += printSpriteHeight(fichasp_gfx_cor, UP_FICHA_REVERSE);
	    
	    // Pintar la parte inferior de la ficha
	    spriteDraw(fichasp_gfx,  posX, posYTemp,  DOWN_FICHA_REVERSE, fichasp_gfx_cor);
	    posYTemp += printSpriteHeight(fichasp_gfx_cor, DOWN_FICHA_REVERSE);
    }
    
}

// Funci�n que devuelve el n�mero de fichas de un mont�n
// Se podr�a sacar directamente de la posici�n GET_NUM_FICHAS del array pero es para que el c�digo quede m�s claro
public int GetNumFichasOf(int recipient)
{    
    return(FichasDe[recipient][GET_NUM_FICHAS]);
}

// Funci�n que mueve fichas entre los arrays
// El array que pierde una ficha se reorganiza para que no queden huecos y se actualiza su contador
public void MoveFichaById(int IdFichaToMove, int Origen, int Destiny)
{		int posFicha;
		boolean reorganizar = false;
    // Primero buscamos la posici�n en el array de la ficha a buscar
    for (posFicha = 0; posFicha < GetNumFichasOf(Origen); posFicha++)
    {        
        if (FichasDe[Origen][posFicha] == IdFichaToMove)
        {
            // Copiamos la ficha correspondiente a la nueva posici�n en el array
				    FichasDe[Destiny][GetNumFichasOf(Destiny)] = IdFichaToMove;
				    FichasDe[Destiny][GET_NUM_FICHAS]++;					
				    reorganizar = true;	// Ya hemos copiado la ficha. Compactar el array
        }
        
        if (reorganizar) 
        {
            // Compactamos el array
            FichasDe[Origen][posFicha] = FichasDe[Origen][posFicha+1];
        }        
    }
    
    if (reorganizar)
    {
        // Tenemos una ficha menos
        FichasDe[Origen][GET_NUM_FICHAS]--;
        // En la antigua posici�n grabamos -1 para que quede borrado
        FichasDe[Origen][GetNumFichasOf(Origen)] = -1;
    }
}

public int getTopFicha(int IdFicha)
{
    return(IdFicha / 10);
}

public int getBottomFicha(int IdFicha)
{
    return(IdFicha - ((IdFicha / 10)*10));
}

public boolean esDoble(int IdFicha)
{
    return(getTopFicha(IdFicha)==getBottomFicha(IdFicha));
}

public int getValueFicha(int TypeOfValue, int IdFicha)
{
    if (TypeOfValue == VALUE_FICHA_TOP) return(getTopFicha(IdFicha));
    if (TypeOfValue == VALUE_FICHA_BOTTOM) return(getTopFicha(IdFicha));
    //#ifdef DEBUG
    	//System.out.println("ATENCI�N: Solicitado valor de ficha no v�lido");
    //#endif
    return(VALUE_FICHA_NONE);
}

public boolean puedoRobar()
{
   return(GetNumFichasOf(MONTON)>0);
}

public int getBranchRotation(int BranchToGetRotation, int IdFichaToDraw)
{
/*
    //#ifdef DEBUG_GETBRANCHROTATION
    	System.out.println("Inicio getBranchRotation  -------------------- "+ IdFichaToDraw);
    //#endif
    int BranchToPutFicha = puedoTirarFicha(IdFichaToDraw);
    
    //#ifdef DEBUG_GETBRANCHROTATION
    	System.out.println("getBranchRotation					BranchToPutFicha = "+ BranchToPutFicha);
    //#endif
    if ((BranchToPutFicha == BranchToGetRotation) || (BranchToPutFicha == NEW_PUT) || (BranchToPutFicha == BOTH_PUT)) 
    {
        int RotacionInicialFicha = (getNewBranchDirection(BranchToGetRotation)+1) % 2;
        // Rotaci�n inicial de la ficha seg�n se est� pintando en la mesa para una rama concreta
        //#ifdef DEBUG_GETBRANCHROTATION
        	System.out.println("    BranchDirection[BranchToGetRotation] 	= "+ BranchDirection[BranchToGetRotation]);
        	System.out.println("    RotacionInicialFicha 									= "+ RotacionInicialFicha);
        	System.out.println("    getTopFicha(IdFichaToDraw) 						= " + getTopFicha(IdFichaToDraw));
          System.out.println("    Branch[????_BRANCH] 									= " + Branch[BranchToGetRotation]);
        //#endif

        // Comprobamos si tenemos que darle la vuelta
        if ((BranchToGetRotation == LEFT_BRANCH) || (BranchToGetRotation == RIGHT_BRANCH))
        { 
            // Si NO coincide la ficha con lo que tenemos que ponerla la rotamos 180�
            if (
                ( 
                 (getNewBranchDirection(BranchToGetRotation) == DIRECTION_RIGHT) || 
                 (getNewBranchDirection(BranchToGetRotation) == DIRECTION_DOWN)
                ) &&
                (getTopFicha(IdFichaToDraw)!=Branch[BranchToGetRotation])
               ) 
            {
                RotacionInicialFicha = (RotacionInicialFicha + 2) % 4;
            }
            if (
                ( 
                 (getNewBranchDirection(BranchToGetRotation) == DIRECTION_LEFT) || 
                 (getNewBranchDirection(BranchToGetRotation) == DIRECTION_UP)
                ) &&
                (getBottomFicha(IdFichaToDraw)!=Branch[BranchToGetRotation])
               ) 
            {
                RotacionInicialFicha = (RotacionInicialFicha + 2) % 4;
            }
        }
        /*
        // Si es un doble la ponemos girada 90�
        if (getTopFicha(IdFichaToDraw)==getBottomFicha(IdFichaToDraw))
        {
            RotacionInicialFicha = (RotacionInicialFicha + 1) % 4;            
        }
        */
/*        return(RotacionInicialFicha);
    } else
    {*/
        return(NOT_PUT);
    //}
}

public int getNewBranchValue(int BranchToPutFicha, int IdNewFicha)
{
    int parteQueCoincide = puedoTirarFicha(BranchToPutFicha,IdNewFicha); 
    if (parteQueCoincide != VALUE_FICHA_NONE)
    {
        if (parteQueCoincide == VALUE_FICHA_TOP) 		return(getBottomFicha(IdNewFicha));
        if (parteQueCoincide == VALUE_FICHA_BOTTOM) return(getTopFicha(IdNewFicha));        
    }                
    return(VALUE_FICHA_NONE);
}

public void corrigeSeleccionPlayer()
{
    if (fichaSelected[ActualPlayer] >= GetNumFichasOf(ActualPlayer)) fichaSelected[ActualPlayer] = (GetNumFichasOf(ActualPlayer)-1);
	  if (fichaSelected[ActualPlayer] < 0) fichaSelected[ActualPlayer] = 0;

    if (
            ((fichaSelected[ActualPlayer]-fichaOffsetStart[ActualPlayer])>=GameNumFichasToShow) 
            ||
            ((fichaSelected[ActualPlayer]+GameNumFichasToShow) > GetNumFichasOf(ActualPlayer))
       )
    {
        fichaOffsetStart[ActualPlayer] = (fichaSelected[ActualPlayer] + 1 - GameNumFichasToShow); 
    }
    
    if (fichaSelected[ActualPlayer]<fichaOffsetStart[ActualPlayer])
    {
        fichaOffsetStart[ActualPlayer] = fichaSelected[ActualPlayer]; 
    }
    
    if (fichaOffsetStart[ActualPlayer]<0) fichaOffsetStart[ActualPlayer] = 0;
    
}

public boolean robarFicha()
{
    // Robar una ficha
    if (GetNumFichasOf(MONTON) > 0)
    {
	     // Mover una ficha a cada player
		   MoveFichaById(
                				FichasDe[MONTON][rnd(GetNumFichasOf(MONTON))], 		// Id de la ficha a mover
                				MONTON,																						// Recipiente origen
                				ActualPlayer																			// Recipiente destino
        						 );
		   fichaSelected[ActualPlayer] = (GetNumFichasOf(ActualPlayer) - 1);
		   corrigeSeleccionPlayer();
       //#ifdef DEBUG
		   		DebugRecipients();
       //#endif
		   return(true);
    }
    return(false);

}

public void pintaNumFichas()
{
    String TextoPuntos;
    
    
    // Pintamos Degradado superior
    scr.setClip(0,0,canvasWidth,canvasHeight);
    //ZonaFondo(0, 0, canvasWidth, canvasTop, 12, 1, canvasTop);
    
    FCRed = 0x59; FCGre = 0x4E; FCBlu = 0x2B;
    ICRed = 0x6C; ICGre = 0x5D; ICBlu = 0x38;
    ZonaFondo(0, 0, canvasWidth, canvasTop-printSpriteHeight(formFontSmallWhiteCor,0), 12, 1, canvasTop);
    
    FCRed = 0x43; FCGre = 0x41; FCBlu = 0x2E;
    ICRed = 0x53; ICGre = 0x52; ICBlu = 0x39;
    ZonaFondo(0, canvasTop-printSpriteHeight(formFontSmallWhiteCor,0), canvasWidth, canvasTop, 12, 1, canvasTop);
    
    
    // Detectamos el marco superior derecho
    int TamMarco = (2 * canvasWidth) / 100;
    
    int TamCuadro = (30 * canvasWidth) / 100;
    
    int posCuadroX = /*canvasWidth - TamCuadro - */TamMarco;
    int posCuadroY = TamMarco;
    
    
    fillDraw(DARK_GREEN, 	posCuadroX, posCuadroY, TamCuadro, TamCuadro);
    printSetArea(posCuadroX+2, posCuadroY+2, TamCuadro-4, TamCuadro-4);
    rectDraw(BLACK, posCuadroX, posCuadroY, TamCuadro, TamCuadro);
    rectDraw(BROWN_C, posCuadroX-1, posCuadroY-1, TamCuadro+2, TamCuadro+2);
    rectDraw(BROWN_B, posCuadroX-2, posCuadroY-2, TamCuadro+4, TamCuadro+4);
    rectDraw(BROWN_A, posCuadroX-3, posCuadroY-3, TamCuadro+6, TamCuadro+6);
    
    // Player 1
    	formFontInit(FORM_FONT_SMALL_RED);  // BIGTHOR QUITAR ESTO 

    	if (gameConfig[PLAYER_1]!=CONTROL_NONE)
    	{
				if (ActualPlayer == PLAYER_1) formFontInit(FORM_FONT_SMALL_RED); else formFontInit(FORM_FONT_SMALL_WHITE);
			  /*if ((ActualPlayer != PLAYER_1) || (Math.abs(senoidal)>(sinRange/3)))*/ printDraw(""+GetNumFichasOf(PLAYER_1), 0, 0, COLOR_WHITE, PRINT_HCENTER|PRINT_BOTTOM);
    	}    
    // Player 2
    	if (gameConfig[PLAYER_2]!=CONTROL_NONE)
    	{
	    	if (ActualPlayer == PLAYER_2) formFontInit(FORM_FONT_SMALL_RED); else formFontInit(FORM_FONT_SMALL_WHITE);
	      /*if ((ActualPlayer != PLAYER_2) || (Math.abs(senoidal)>(sinRange/3)))*/ printDraw(""+GetNumFichasOf(PLAYER_2), 0, 0, COLOR_WHITE, PRINT_RIGHT|PRINT_VCENTER);    	    
    	}
    
    // Player 3
    	if (gameConfig[PLAYER_3]!=CONTROL_NONE)
    	{
	    	if (ActualPlayer == PLAYER_3) formFontInit(FORM_FONT_SMALL_RED); else formFontInit(FORM_FONT_SMALL_WHITE);
	      /*if ((ActualPlayer != PLAYER_3) || (Math.abs(senoidal)>(sinRange/3)))*/ printDraw(""+GetNumFichasOf(PLAYER_3), 0, 0, COLOR_WHITE, PRINT_HCENTER|PRINT_TOP);
    	}
    	
    
    // Player 4
    	if (gameConfig[PLAYER_4]!=CONTROL_NONE)
    	{
    	    if (ActualPlayer == PLAYER_4) formFontInit(FORM_FONT_SMALL_RED); else formFontInit(FORM_FONT_SMALL_WHITE);
    	    /*if ((ActualPlayer != PLAYER_4) || (Math.abs(senoidal)>(sinRange/3)))*/ printDraw(""+GetNumFichasOf(PLAYER_4), 0, 0, COLOR_WHITE, PRINT_LEFT|PRINT_VCENTER);
    	}
    	
    if (modoTorneo)
    {
        //TextoPuntos = PuntosActuales[0]+" - "+PuntosActuales[1]+" "+gameText[TEXT_CLASIFICACION][4]+PuntosTorneo[TorneoActual]+gameText[TEXT_CLASIFICACION][5];
        TextoPuntos = PuntosActuales[0]+" - "+PuntosActuales[1]+" "+"("+PuntosTorneo[TorneoActual]+")";
    } else
    {
        TextoPuntos = ((ActualPlayer==0)?gameText[TEXT_PUNTOS][0]+" "+PuntosActuales[0]:"");
    }

/*    if (ActualPlayer != 0)
    {
    */
	    // Nombre del player que est� tirando
	    printSetArea(posCuadroX+TamCuadro+8, 3, canvasWidth - 2 - (posCuadroX+TamCuadro+8), canvasTop-2);
    	formFontInit(FORM_FONT_SMALL_BLACK);  // BIGTHOR QUITAR ESTO
    	printDraw(gamePlayerNamesOnly[ActualPlayer], 1, 1, BLACK, PRINT_LEFT|PRINT_TOP);
    	//formFontInit(FORM_FONT_SMALL_BLACK);  // BIGTHOR QUITAR ESTO
    	printDraw(TextoPuntos, 1, 1, BLACK, PRINT_LEFT|PRINT_BOTTOM);
    	
    	formFontInit(FORM_FONT_SMALL_WHITE);  // BIGTHOR QUITAR ESTO
    	printDraw(gamePlayerNamesOnly[ActualPlayer], 0, 0, COLOR_WHITE, PRINT_LEFT|PRINT_TOP);
    	//formFontInit(FORM_FONT_SMALL_WHITE);  // BIGTHOR QUITAR ESTO
    	printDraw(TextoPuntos, 0, 0, COLOR_WHITE, PRINT_LEFT|PRINT_BOTTOM);
    	//printSetArea(posCuadroX+TamCuadro+8, 2+3+printGetHeight(), canvasWidth - 2 - (posCuadroX+TamCuadro+8), canvasTop-5-printGetHeight());
    	
    /*} else
    {
    	formFontInit(FORM_FONT_SMALL_BLACK);  // BIGTHOR QUITAR ESTO
    	printDraw(gameText[TEXT_PLAYER], 1, 1, BLACK, PRINT_HCENTER|PRINT_VCENTER);
    	formFontInit(FORM_FONT_SMALL_WHITE);  // BIGTHOR QUITAR ESTO
    	printDraw(gameText[TEXT_PLAYER], 0, 0, COLOR_WHITE, PRINT_HCENTER|PRINT_VCENTER);
    }
    */
}

public void pintaMovilAI()
{
    //System.out.println("SPRITE WIDTH"+printSpriteWidth(extras_gfx_cor, 0));
    if ((gameConfig[ActualPlayer] == CONTROL_CPU) && (playStatus!=PLAY_STATUS_SHOW_BOARD))
    {
        if (Math.abs(senoidal)>(sinRange/3)) spriteDraw(extras_gfx, (canvasWidth - printSpriteWidth(extras_gfx_cor, 0)) - 4,  4, 0, extras_gfx_cor);
    }
    //spriteDraw(extras_gfx,  0, 0,  0, extras_gfx_cor);
}

// BIGTHOR FICHA
final static int	ROTATION_0		=	0;
final static int  ROTATION_90		= 1;
final static int	ROTATION_180	= 2;
final static int	ROTATION_270	= 3;

final static int  VERTICAL_TOP			= 0;
final static int  VERTICAL_BOTTOM 	= 1;
final static int	HORIZONTAL_LEFT		= 2;
final static int 	HORIZONTAL_RIGHT	= 3;

final static int 	OFFSET_TABLE_TILE			= 73; // TODO: Quitar esto
final static int 	OFFSET_SOMBRAS_TOTAL	= 66; // TODO: Quitar esto
final static int  TILES_SOMBRA					= 46; // TODO: Quitar esto
final static int 	TIPOS_SOMBRA					= 5;  // TODO: Quitar esto


final static int  TIPO_SOMBRA_H_INICIO	= 109;
final static int  TIPO_SOMBRA_H_MEDIO		= 110;
final static int 	TIPO_SOMBRA_H_FINAL		= 111;
final static int  TIPO_SOMBRA_COMBI			= 112;
final static int 	TIPO_SOMBRA_V_INICIO	= 113;
final static int 	TIPO_SOMBRA_V_MEDIO		= 114;
/*
public void paintSombraToTable(int posX, int posY)
{
    boolean FichaEncima 			= hayFicha(posX, posY-1);
    boolean FichaDebajo				= hayFicha(posX, posY+1);
    boolean FichaIzquierda		= hayFicha(posX-1, posY);
    boolean FichaDerecha			= hayFicha(posX+1, posY);
    boolean FichaAbajoDerecha	= hayFicha(posX+1, posY+1);
    
    // Sombra derecha
    if (!FichaDerecha) // hay ficha
    {
        if (!FichaEncima) 
        {
            setBackGround(posX+1, posY, TIPO_SOMBRA_V_INICIO);
        } else
        {
            setBackGround(posX+1, posY, TIPO_SOMBRA_V_MEDIO);
        }       
    }
    
    // Sombra abajo
    if (!FichaDebajo)
    {
        if (FichaIzquierda)
        {
            setBackGround(posX, posY+1, TIPO_SOMBRA_H_MEDIO);                        
        } else
        {
            setBackGround(posX, posY+1, TIPO_SOMBRA_H_INICIO);
        }
    }
    // Sombra abajo derecha
    if (!FichaAbajoDerecha)
    {
        if ((FichaDerecha) || (FichaDebajo))
        {
	        if ((FichaDerecha) && (FichaDebajo))
	        {
	            // Tile raro jordi
	            // TODO: Meter nuevo tipo de sombra
	            //setBackGround(posX+1, posY+1, TIPO_SOMBRA_COMBI);
	        } else
	        {
		        if (FichaDerecha)
		        {
		            setBackGround(posX+1, posY+1, TIPO_SOMBRA_H_MEDIO);
		        }
		        if (FichaDebajo)
		        {
	            setBackGround(posX+1, posY+1, TIPO_SOMBRA_V_MEDIO);
		        }	        
	        }
        } else
        {
            setBackGround(posX+1, posY+1, TIPO_SOMBRA_H_FINAL);
        }
    }
}
*/
int LastBranchX;
int LastBranchY;
public void paintFichaToTable(int IdFichaToDraw, int posX, int posY, int Rotation, int Direction)
{
    int FichaTop 				= getTopFicha(IdFichaToDraw);
    int FichaBottom			= getBottomFicha(IdFichaToDraw);
    /*int TileTopFicha		= scrollTileCombFirst; 
    int TileBottomFicha	= scrollTileCombFirst;
    */
    int TileTopFicha;
    int TileBottomFicha;
    int Desp;
    
    // Intermitencia de la ficha
    FichaBlink--;

    // Correcci�n de centrado
    if (Direction == DIRECTION_LEFT)
    {
        posX -= getSizeXFicha(Rotation) - 1;
    }
    if (Direction == DIRECTION_UP)
    {
        //System.out.println("ponerEn "+posY+" correcci�n = "+((getSizeXFicha(Rotation) - 1)));
        posY += (getSizeXFicha(Rotation) - 1);
    }
    
    // Revisar si es doble
    if (esDoble(IdFichaToDraw)) Desp = 1; else Desp = 0;
    
    if ((Rotation == ROTATION_0) || (Rotation == ROTATION_180)) // Si la ficha est� en vertical
    {
        // Cuerpo de la ficha
        if (Rotation == ROTATION_0)
        {
            TileTopFicha 				= (VERTICAL_TOP 		+ (FichaTop * 4)); // No hace falta sumar VERTICAL_TOP = 0
            TileBottomFicha 		= (VERTICAL_BOTTOM + (FichaBottom * 4));            
        } else
        {
            TileTopFicha 				= (VERTICAL_TOP 		+ (FichaBottom * 4)); // No hace falta sumar VERTICAL_TOP = 0
            TileBottomFicha 		= (VERTICAL_BOTTOM + (FichaTop * 4));            
        }

        LastBranchX = posX * 2;
        LastBranchY = (posY * 2) - Desp;
        getTableMemory(LastBranchX, LastBranchY);
        
        if (
        	 ((waitSelection==false) && ((((Math.abs(FichaBlink)/3)%2)==0) || (FichaBlink<0)))
            || 
           ((waitSelection==true) && ((((Math.abs(FichaBlink)/3)%2)==0) /*|| (FichaBlink<0)*/))
           )
        {
		        setBigTile(LastBranchX,	LastBranchY,	TileTopFicha);
		        setBigTile(posX * 2,	((posY+1) * 2) 	- Desp,	TileBottomFicha);
		        setBackGround(posX * 2, (posY * 2)     - Desp);
		        setBackGround(posX * 2, ((posY+1) * 2) - Desp);
        } else
        {
            setTableMemory(LastBranchX, LastBranchY);
        }
    } else	// Si la ficha est� en horizontal
    {
        // Cuperpo de la ficha
        if (Rotation == ROTATION_90)
        {
            TileTopFicha 				= HORIZONTAL_LEFT		+ (FichaTop * 4); 
            TileBottomFicha 		= HORIZONTAL_RIGHT		+ (FichaBottom * 4);
        } else
        {
            TileTopFicha 				= HORIZONTAL_LEFT		+ (FichaBottom * 4);
            TileBottomFicha 		= HORIZONTAL_RIGHT		+ (FichaTop * 4);
            
        }
        
        //System.out.println("posX = "+posX+" - "+((posX * 2) - Desp));
        //System.out.println("posY = "+posY);
        LastBranchX = (posX * 2) - Desp;
        LastBranchY = posY * 2;
        getTableMemory(LastBranchX, LastBranchY);
        //System.out.println("LastBranchXY = "+LastBranchX+","+LastBranchY);
        
        if (
        	 ((waitSelection==false) && ((((Math.abs(FichaBlink)/3)%2)==0) || (FichaBlink<0)))
            || 
           ((waitSelection==true) && ((((Math.abs(FichaBlink)/3)%2)==0) /*|| (FichaBlink<0)*/))
           )
        {
		        setBigTile(LastBranchX,				LastBranchY,		TileTopFicha);
		        setBigTile(LastBranchX+2,			LastBranchY,		TileBottomFicha);
		        setBackGround(LastBranchX,		LastBranchY);
		        setBackGround(LastBranchX+2,	LastBranchY);
        } else 
        {
            setTableMemory(LastBranchX, LastBranchY);
        }
    }
}

public void getTableMemory(int posX, int posY)
{
    if (capturedTable==false)
    {
        //System.out.println("get");
        for (int x=0; x<TABLE_CAPTURE_SIZE; x++)
        {
            for (int y=0; y<TABLE_CAPTURE_SIZE; y++)
            {
                //System.out.println("x,y,value = "+(posX+x)+","+(posY+y)+"="+getTile(posX+x,posY+y));
                TableMemory[x][y] = getTile(posX+x,posY+y);
            }
        }
        capturedTable = true;
    }
}

public void setTableMemory(int posX, int posY)
{
    //System.out.println("set");
    if (capturedTable==true)
    {
        for (int x=0; x<TABLE_CAPTURE_SIZE; x++)
        {
            for (int y=0; y<TABLE_CAPTURE_SIZE; y++)
            {
                //System.out.println("x,y,value = "+x+","+y+"="+TableMemory[x][y]);
                setTile(posX+x,posY+y,TableMemory[x][y]);
            }
        }
    }
}

public void setTile(int posX, int posY, int NewTileId)
{
    if ((posX > 0) && (posX < mapSizeX) && (posY > 0) && (posY < mapSizeY))
    {        
        levelMap[(posY*mapSizeX)+posX] = (byte)NewTileId;         
    }    
}

public void setBigTile(int posX, int posY, int NewTileId)
{
   
/*    realX = posX * 2;
    realY = posY * 2;
  */  
    int realTileId = (NewTileId * 4) + scrollTileCombFirst;
    
    // Pintamos los 4 correspondientes
    setTile(posX,		posY,		(realTileId));
    setTile(posX+1,	posY,		(realTileId)+1);
    setTile(posX,		posY+1,	(realTileId)+2);
    setTile(posX+1,	posY+1,	(realTileId)+3);

    
/*		    
final static int  TIPO_SOMBRA_H_INICIO	= 0;
final static int  TIPO_SOMBRA_H_MEDIO		= 1;
final static int 	TIPO_SOMBRA_H_FINAL		= 2;
final static int  TIPO_SOMBRA_COMBI			= 3;
final static int 	TIPO_SOMBRA_V_INICIO	= 4;
final static int 	TIPO_SOMBRA_V_MEDIO		= 5;
*/
    
    
    
    
    /*
    
    boolean FichaEncima 			= hayFicha(posX, posY-1);
    boolean FichaDebajo				= hayFicha(posX, posY+1);
    boolean FichaDerecha			= hayFicha(posX+1, posY);
    boolean FichaAbajoDerecha	= hayFicha(posX+1, posY+1);
   */
}

public void setBackGround(int posX, int posY)
{
    // Pintamos la sombra
    boolean FichaIzquierda					= hayFicha(posX-1, posY+1);
    boolean FichaDerechaArriba			= hayFicha(posX+2, posY);
    boolean FichaDerechaArriba2     = hayFicha(posX+2, posY-1);
    boolean FichaArriba							= hayFicha(posX+1, posY-1);
    boolean FichaDerecha						= hayFicha(posX+2, posY+1);
    boolean FichaAbajoIzquierda			= hayFicha(posX-1, posY+2);
    boolean FichaAbajoIzquierda2		= hayFicha(posX-1, posY+3);
    // Parte baja izquierda de una pieza (una pieza es media ficha)
    if (hayFicha(posX, posY+2)==false)
    {
		    if (FichaIzquierda==false)
		    {
		        setTile(posX, posY+2, TIPO_SOMBRA_H_INICIO);
		    } else
		    {
		        if (FichaAbajoIzquierda == false)
		        {
		            setTile(posX, posY+2, TIPO_SOMBRA_H_MEDIO);
		        } else
		        {
		            setTile(posX, posY+2, TIPO_SOMBRA_COMBI);
		        }
		    }
    }
    
    // Parte baja derecha
    if (hayFicha(posX+1, posY+2)==false)
    {        
        if (FichaAbajoIzquierda2==true)
        {
            setTile(posX+1, posY+2, TIPO_SOMBRA_COMBI);
        } else
        {
            setTile(posX+1, posY+2, TIPO_SOMBRA_H_MEDIO);
        }
    }
    
    // Parte baja derecha canto
    if (hayFicha(posX+2, posY+2)==false)
    {
        if (!FichaDerecha)
        {
            setTile(posX+2, posY+2, TIPO_SOMBRA_H_FINAL);
        } else
        {
            setTile(posX+2, posY+2, TIPO_SOMBRA_H_MEDIO);            
        }       
    }    
    
    // Parte derecha abajo
    if (hayFicha(posX+2, posY+1)==false)
    {
        if (FichaDerechaArriba)
        {
            setTile(posX+2, posY+1, TIPO_SOMBRA_COMBI);
        } else
        {
            setTile(posX+2, posY+1, TIPO_SOMBRA_V_MEDIO);
        }
    }
    
    // Parte superior derecha
    if (hayFicha(posX+2, posY)==false)
    {
        if ((!(FichaDerechaArriba)) && (!(FichaArriba)))
        {
            setTile(posX+2, posY, TIPO_SOMBRA_V_INICIO);
        } else
        {
            if (FichaDerechaArriba2==false)
            {
                setTile(posX+2, posY, TIPO_SOMBRA_V_MEDIO);
            } else
            {
                setTile(posX+2, posY, TIPO_SOMBRA_COMBI);
            }            
        }
    }
}

public int getTile(int posX, int posY)
{
    if ((posX >= 0) && (posX < mapSizeX) && (posY >= 0) && (posY < mapSizeY))
    {
        return(levelMap[(posY*mapSizeX)+posX] & 0xFFFF);
    } else
    {
        return(-1);
    }    
}

public int getTableTile(int posX, int posY)
{
    return (((posY % 3)*3)+(posX % 3));
}


public void setBackGround(int posX, int posY, int BackGroundType)
{
    setTile(posX,posY,(OFFSET_SOMBRAS_TOTAL+(getTableTile(posX,posY)*TIPOS_SOMBRA)+BackGroundType));
}         

public boolean hayFicha(int posX, int posY)
{
    //System.out.println(getTile(posX, posY) >= scrollTileCombFirst);
    return(getTile(posX, posY) >= scrollTileCombFirst);   
}

//#ifdef DEBUG
public void DebugRecipients()
{
    System.out.println("____________________________________________________________________________________________________________________________");
    String[] RenderRecipients = new String[NUM_RECIPIENTS];
    
    RenderRecipients[PLAYER_1]			= "Player 1     = ";
    RenderRecipients[PLAYER_2]			= "Player 2     = ";
    RenderRecipients[PLAYER_3]			= "Player 3     = ";
    RenderRecipients[PLAYER_4]			= "Player 4     = ";
    RenderRecipients[MONTON]				= "MONTON       = ";
    RenderRecipients[MESA]					= "MESA         = ";
    
    
    for (int NumFicha = 0; NumFicha <= NUM_FICHAS; NumFicha++)
    {
        for (int NumRecipient = 0; NumRecipient < NUM_RECIPIENTS; NumRecipient++)
        {
            //if (NumFicha < GetNumFichasOf(NumRecipient))
            {
                RenderRecipients[NumRecipient] += "" + FichasDe[NumRecipient][NumFicha] + "-";                
            }            
        }
        
    }
    
    // Mostramos los strings resultantes
    for (int NumRecipient = 0; NumRecipient < NUM_RECIPIENTS; NumRecipient++)
    {
        System.out.println(RenderRecipients[NumRecipient]);
    }
    System.out.println("____________________________________________________________________________________________________________________________");
    
}
//#endif


// >___________________________________________________________________________



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

// TIRAR FICHAS EN EL TABLERO <____________________________________________________________________________________
public int puedoTirarFicha(int IdFicha)
{
    /*
    if ((Branch[LEFT_BRANCH] 	== NOT_PUT) && (Branch[RIGHT_BRANCH] 	== NOT_PUT))
    {
        return(NEW_PUT);
    }
    
    return(RIGHT_BRANCH);
    */
   
    int FichaTop 				= getTopFicha(IdFicha);
    int FichaBottom			= getBottomFicha(IdFicha);
    System.out.println("FichaTop "		+ FichaTop);
    System.out.println("FichaBottom "	+ FichaBottom);

    if ((Branch[LEFT_BRANCH] 	== NOT_PUT) && (Branch[RIGHT_BRANCH] 	== NOT_PUT))
    {
        System.out.println("NEW_PUT");
        return(NEW_PUT);
    }

    if (
        ((FichaTop 	== Branch[LEFT_BRANCH]) 		&& (FichaBottom 	== Branch[RIGHT_BRANCH])) ||
        ((FichaTop 	== Branch[RIGHT_BRANCH]) 		&& (FichaBottom 	== Branch[LEFT_BRANCH]))
       )
    {
        System.out.println("BOTH_PUT");
        return(BOTH_PUT);
    }
    if ((FichaTop 	== Branch[LEFT_BRANCH]) 		|| (FichaBottom 	== Branch[LEFT_BRANCH]))
    {
        System.out.println("LEFT_BRANCH");
        return(LEFT_BRANCH);
    }
    if ((FichaTop 	== Branch[RIGHT_BRANCH]) 		|| (FichaBottom 	== Branch[RIGHT_BRANCH]))
    {
        System.out.println("RIGHT_BRANCH");
        return(RIGHT_BRANCH);
    }
    System.out.println("NOT_PUT");
    return(NOT_PUT);
}

public int puedoTirarFicha(int BranchToPutFicha, int IdFichaToPut)
{
    if (getTopFicha(IdFichaToPut)			== Branch[BranchToPutFicha]) 		
    {
        return(VALUE_FICHA_TOP);
    }
    if (getBottomFicha(IdFichaToPut) 	== Branch[BranchToPutFicha])
    {
        return(VALUE_FICHA_BOTTOM);
    }
    return(VALUE_FICHA_NONE);
    
}

public boolean puedoTirarAlgunaFicha()
{
    for (int NumFicha = 0; NumFicha < GetNumFichasOf(ActualPlayer); NumFicha++)
    {
        if (puedoTirarFicha(FichasDe[ActualPlayer][NumFicha]) != NOT_PUT) return(true);        
    }
    return(false);
}

int DondePodemosTirarFicha;
int FichaATirar;

public int getNewBranchDirection(int IDFicha, int Branch)
{
    // TODO: Tener en cuenta que si la ficha es doble no podemos hacer el giro.
    
    if (
	       (
	        ((BranchDirection[Branch]==0) && (BranchSpacing[Branch] > TOP_SPACING_BRANCH / 2)) ||
	    	  ((BranchDirection[Branch]!=0) && (BranchSpacing[Branch] > TOP_SPACING_BRANCH))
	    	 ) &&
	    	 (LastPut[Branch] == false) &&
	    	 (esDoble(IDFicha)==false)
	     )
    {
        if (Branch == RIGHT_BRANCH)
        {
            switch(BranchDirection[Branch])
            {
            	case DIRECTION_RIGHT: return(DIRECTION_DOWN);
            	case DIRECTION_DOWN:	return(DIRECTION_LEFT);
            	case DIRECTION_LEFT:	return(DIRECTION_UP);
            	case DIRECTION_UP:		return(DIRECTION_RIGHT);
            }
        } else
        {
            switch(BranchDirection[Branch])
            {
            	case DIRECTION_LEFT: return(DIRECTION_DOWN);
            	case DIRECTION_DOWN:	return(DIRECTION_RIGHT);
            	case DIRECTION_RIGHT:	return(DIRECTION_UP);
            	case DIRECTION_UP:		return(DIRECTION_LEFT);
            }
        }        
    }
    return (BranchDirection[Branch]);
}

public int getDespX(int NewRotation, int NewBranchDirection)
{
    return(getSizeXFicha(NewRotation) * getIncDecX(NewBranchDirection));
}

public int getDespY(int NewRotation, int NewBranchDirection)
{
    return(getSizeYFicha(NewRotation) * getIncDecY(NewBranchDirection));
}

public int getNewXPos(int IdFicha, int Branch)
{
    int NewBranchDirection 	= getNewBranchDirection(IdFicha, Branch);
    int NewRotation 				= getNewFichaRotation(IdFicha, Branch);
    int NewBranchX 					= BranchX[Branch];

    //System.out.println("BranchX["+Branch+"] = "+BranchX[Branch]+" DespX = "+getDespX(NewRotation, NewBranchDirection));
    //NewBranchX += getDespX(NewRotation, NewBranchDirection);
    
    
    if (NewBranchDirection != BranchDirection[Branch])
    {
        NewBranchX +=	BranchTurnX[Branch][NewBranchDirection];
    } 
    return(NewBranchX);
}

public int getNewYPos(int IdFicha, int Branch)
{
    int NewBranchDirection 	= getNewBranchDirection(IdFicha, Branch);
    int NewRotation 				= getNewFichaRotation(IdFicha, Branch);
    int NewBranchY					= BranchY[Branch];

    //NewBranchY += getDespY(NewRotation, NewBranchDirection);     	

    if (NewBranchDirection != BranchDirection[Branch])
    {
        NewBranchY +=	BranchTurnY[Branch][NewBranchDirection];
    } 
    
    return(NewBranchY);
}

public int getNewFichaRotation(int IdFicha, int myBranch)
{
    int NewBranchDirection 	= getNewBranchDirection(IdFicha, myBranch);

    int RotacionInicialFicha = (NewBranchDirection+1) % 2;
    
    // Comprobamos si tenemos que darle la vuelta
    if ((myBranch == LEFT_BRANCH) || (myBranch == RIGHT_BRANCH))
    { 
        // Si NO coincide la ficha con lo que tenemos que ponerla la rotamos 180�
        if (
            ( 
             (NewBranchDirection == DIRECTION_RIGHT) || 
             (NewBranchDirection == DIRECTION_DOWN)
            ) &&
            (getTopFicha(IdFicha)!=Branch[myBranch])
           ) 
        {
            RotacionInicialFicha = (RotacionInicialFicha + 2) % 4;
        }
        if (
            ( 
             (NewBranchDirection == DIRECTION_LEFT) || 
             (NewBranchDirection == DIRECTION_UP)
            ) &&
            (getBottomFicha(IdFicha)!=Branch[myBranch])
           ) 
        {
            RotacionInicialFicha = (RotacionInicialFicha + 2) % 4;
        }
    }
        
    // Si es un doble la ponemos girada 90�
    if (getTopFicha(IdFicha)==getBottomFicha(IdFicha))
    {
        RotacionInicialFicha = (RotacionInicialFicha + 1) % 4;            
    }    
        
    return(RotacionInicialFicha);
}

public boolean tirarFicha(int RecipienteOrigen)
{
    // BIGTHOR 2006

    // Esperar selecci�n de jugador en el caso que se pueda tirar a ambos lados
    //waitSelection = false;
    // Ficha que tratamos de tirar
    FichaATirar = FichasDe[RecipienteOrigen][fichaSelected[RecipienteOrigen]];
    // Rama a la que podemos tirar ficha LEFT_BRANCH, RIGHT_BRANCH, BOTH_PUT, NOT_PUT 
    DondePodemosTirarFicha = puedoTirarFicha(FichaATirar);
    System.out.println("DondePodemosTirarFicha = "+DondePodemosTirarFicha);
    
    if (DondePodemosTirarFicha != NOT_PUT)
    {
        //waitStart();
        TIEMPO_FICHA = 20;
        
        // Si es la primera tirada correcci�n de coordenadas
        /*
		    if (DondePodemosTirarFicha == NEW_PUT)
		    {
		        BranchX[RIGHT_BRANCH]				=	BranchX[RIGHT_BRANCH] - getDespX(getNewFichaRotation(FichaATirar,RIGHT_BRANCH), getNewBranchDirection(FichaATirar,RIGHT_BRANCH));
		        BranchY[RIGHT_BRANCH]				=	BranchY[RIGHT_BRANCH] - getDespY(getNewFichaRotation(FichaATirar,RIGHT_BRANCH), getNewBranchDirection(FichaATirar,RIGHT_BRANCH));
		    }
		    */
        FichaBlink = BLINK_TIME;
        capturedTable = false;
        if ((gameConfig[ActualPlayer] == CONTROL_PLAYER) && (DondePodemosTirarFicha == BOTH_PUT) && (esDoble(FichaATirar)==false))
        {
            waitSelection = true;
        } else
        {
            waitSelection = false;
        }        
        
        playStatus = PLAY_STATUS_PRE_TIRANDO_FICHA;
        return(true);
    }
    return(false);
    
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
    /*
    
    //#ifdef DEBUG_TIRARFICHA
    	System.out.println("tirarFicha() -->");
    //#endif
    	
    
    if (DondePodemosTirarFicha != NOT_PUT)
    {
        //#ifdef DEBUG
	        System.out.println("podemosTirarFicha --------------------------------------");
        	System.out.println("DondePodemostirarFicha = "+DondePodemosTirarFicha);
	        DebugRecipients();
	        System.out.println("podemosTirarFicha --------------------------------------");
	      //#endif
		    // Control de la rotaci�n del tablero
		    if (getTopFicha(FichaATirar)!=getBottomFicha(FichaATirar)) // TODO: Tambien comprobar si la �ltima que habiamos tirado era doble 
		    {
				    if (
				        ((BranchDirection[LEFT_BRANCH]==0) && (BranchSpacing[LEFT_BRANCH] > TOP_SPACING_BRANCH / 2)) ||
				    	  ((BranchDirection[LEFT_BRANCH]!=0) && (BranchSpacing[LEFT_BRANCH] > TOP_SPACING_BRANCH))
				    	 )
		        {
		            BranchDirection[LEFT_BRANCH] = getNewBranchDirection(LEFT_BRANCH); 
		            BranchSpacing[LEFT_BRANCH] = 0;
		            BranchX[LEFT_BRANCH] += BranchTurnX[LEFT_BRANCH][BranchDirection[LEFT_BRANCH]];
		            BranchY[LEFT_BRANCH] += BranchTurnY[LEFT_BRANCH][BranchDirection[LEFT_BRANCH]];
		        }
				    if (
				        ((BranchDirection[RIGHT_BRANCH]==0) && (BranchSpacing[RIGHT_BRANCH] > TOP_SPACING_BRANCH / 2)) ||
				    	  ((BranchDirection[RIGHT_BRANCH]!=0) && (BranchSpacing[RIGHT_BRANCH] > TOP_SPACING_BRANCH))
				    	 )
		        {
		            BranchDirection[RIGHT_BRANCH] = getNewBranchDirection(RIGHT_BRANCH); 
		            BranchSpacing[RIGHT_BRANCH] = 0;
		            BranchX[RIGHT_BRANCH] += BranchTurnX[RIGHT_BRANCH][BranchDirection[RIGHT_BRANCH]];
		            BranchY[RIGHT_BRANCH] += BranchTurnY[RIGHT_BRANCH][BranchDirection[RIGHT_BRANCH]];
		        }
		    }

        waitStart();
        playStatus = PLAY_STATUS_PRE_TIRANDO_FICHA;
		    //#ifdef DEBUG_TIRARFICHA
        	System.out.println("    PLAY_STATUS_PRE_TIRANDO_FICHA");
		    	System.out.println("tirarFicha() <--");
		    //#endif
        return(true);
    }
    //#ifdef DEBUG_TIRARFICHA
    	System.out.println("tirarFicha() <--    No puedo tirar ficha");
    //#endif
    return(false);
    */
}

public void preTirandoFicha(int RecipienteOrigen)
{
    int NewBranchDirection;
    int NewBranchX;
    int NewBranchY;
    //System.out.println("antes antes paint ficha");
    if (DondePodemosTirarFicha != NOT_PUT)
    {
        //System.out.println("DondePodemosTirarFicha ---- > "+DondePodemosTirarFicha);
    
        
        // Comprobar a qu� lado puedo tirarla
        if (DondePodemosTirarFicha == NEW_PUT)
        {
            DefaultBranch = RIGHT_BRANCH;
        }
        
        if ((DondePodemosTirarFicha == LEFT_BRANCH) || (DondePodemosTirarFicha == RIGHT_BRANCH))
        {
            DefaultBranch = DondePodemosTirarFicha;
        }                

        //System.out.println("Default Branch = "+DefaultBranch);
        NewBranchDirection = getNewBranchDirection(FichaATirar, DefaultBranch);
        //System.out.println("NewBranchDirection = "+NewBranchDirection);

        NewBranchX = getNewXPos(FichaATirar, DefaultBranch);
        NewBranchY = getNewYPos(FichaATirar, DefaultBranch);
        //System.out.println("Posici�n = "+NewBranchX+","+NewBranchY);
        
       // System.out.println("antes paint ficha");
        paintFichaToTable(FichaATirar, NewBranchX, NewBranchY, getNewFichaRotation(FichaATirar, DefaultBranch), NewBranchDirection);
       // System.out.println("despues paint ficha");

        if ((NewBranchDirection % 2) == 0)
        {
            // Branca horitzontal
            setCamera(((BranchX[DefaultBranch] + 1) * tileWidth), ((BranchY[DefaultBranch]) * tileHeight) + (tileHeight/2));
        } else
        {
            // vertical
            setCamera(((BranchX[DefaultBranch]) * tileWidth) + (tileWidth / 2), (BranchY[DefaultBranch] + 1) * tileHeight);
        }       
        
        if (waitSelection) 
		{
		        // Linea dedicada a porting 
            //if (Math.abs(senoidal)>(sinRange/2)) spriteDraw(extras_gfx, (canvasWidth*(((DefaultBranch+1)%2)+1))/3 - (printSpriteWidth(extras_gfx_cor,1)/2),  (canvasHeight*2)/3, 1 + ((DefaultBranch + 1) % 2), extras_gfx_cor);
            if (DefaultBranch==LEFT_BRANCH)
            {           
                if (Math.abs(senoidal)>(sinRange/2)) spriteDraw(extras_gfx, (canvasWidth - printSpriteWidth(extras_gfx_cor,1) - 4),  (canvasHeight-printSpriteHeight(extras_gfx_cor,1))/2, 1 + ((DefaultBranch + 1) % 2), extras_gfx_cor);
            } else
            {
                if (Math.abs(senoidal)>(sinRange/2)) spriteDraw(extras_gfx, 4,  (canvasHeight-printSpriteHeight(extras_gfx_cor,1))/2, 1 + ((DefaultBranch + 1) % 2), extras_gfx_cor);

            }
		}

    }
    //System.out.println("despues despues paint ficha");
}

public void postTirandoFicha(int RecipienteOrigen)
{
    int NewBranchDirection;
    int NewRotation;
    
    if (DondePodemosTirarFicha == BOTH_PUT) DondePodemosTirarFicha = DefaultBranch;

    NewBranchDirection 	= getNewBranchDirection(FichaATirar, DefaultBranch);
    NewRotation 				= getNewFichaRotation(FichaATirar, DefaultBranch);
    
    //#ifdef DEBUG
    System.out.println("Ficha a tirar: "+FichaATirar);
    //#endif
    
    if (DondePodemosTirarFicha == NEW_PUT)
    {
        //#ifdef DEBUG
        System.out.println("PRIMERA TIRADA");
        //#endif
        
        // Primera tirada

        BranchX[RIGHT_BRANCH]				=	getNewXPos(FichaATirar, RIGHT_BRANCH) + getDespX(NewRotation, getNewBranchDirection(FichaATirar,RIGHT_BRANCH));
        BranchY[RIGHT_BRANCH]				=	getNewYPos(FichaATirar, RIGHT_BRANCH) + getDespY(NewRotation, getNewBranchDirection(FichaATirar,RIGHT_BRANCH));
        /*
        BranchX[LEFT_BRANCH]				=	getNewXPos(FichaATirar, LEFT_BRANCH) 	+ getDespX(NewRotation, getNewBranchDirection(FichaATirar,LEFT_BRANCH));
        BranchY[LEFT_BRANCH]				=	getNewYPos(FichaATirar, LEFT_BRANCH) 	+ getDespY(NewRotation, getNewBranchDirection(FichaATirar,LEFT_BRANCH));
        */
        BranchX[LEFT_BRANCH]--;
        /*BranchX[LEFT_BRANCH]				=	getNewXPos(FichaATirar, LEFT_BRANCH);
        BranchY[LEFT_BRANCH]				=	getNewYPos(FichaATirar, LEFT_BRANCH);*/

        Branch[RIGHT_BRANCH]				= getTopFicha(FichaATirar);
        Branch[LEFT_BRANCH]					= getBottomFicha(FichaATirar); 	// Right branch = default branch
        UltimoPalo[ActualPlayer]		= Branch[DefaultBranch];				// TODO: Pillar �ltimo palo m�s grande o algo
        
        if (esDoble(FichaATirar))
        {
            LastPut[LEFT_BRANCH] 	= true;
            LastPut[RIGHT_BRANCH] = true;
        } else
        {
            LastPut[LEFT_BRANCH] 	= false;
            LastPut[RIGHT_BRANCH] = false;           
        }
    }
    
    if ((DondePodemosTirarFicha == RIGHT_BRANCH) || (DondePodemosTirarFicha == LEFT_BRANCH))
    {
        /*
        DespX = (getSizeXFicha(DondePodemosTirarFicha, FichaATirar) * getIncDecX(DondePodemosTirarFicha));
        DespY = (getSizeYFicha(DondePodemosTirarFicha, FichaATirar) * getIncDecY(DondePodemosTirarFicha));
        */
        /*
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("getIncDecX(DondePodemosTirarFicha) = "+getIncDecX(DefaultBranch));
        System.out.println("getIncDecY(DondePodemosTirarFicha) = "+getIncDecY(DefaultBranch));
	    	System.out.println("NewRotation = "+NewRotation);
        */
        /*DespX = 2;
        DespY = 0;*/
        
        Branch[DefaultBranch] 				= getNewBranchValue(DefaultBranch, FichaATirar);
        UltimoPalo[ActualPlayer]			= Branch[DefaultBranch];

        /*
        BranchX[DefaultBranch]				=	getNewXPos(FichaATirar, DefaultBranch);
        BranchY[DefaultBranch]				=	getNewYPos(FichaATirar, DefaultBranch);
        */
        BranchX[DefaultBranch]				=	getNewXPos(FichaATirar, DefaultBranch) + getDespX(NewRotation, NewBranchDirection);
        BranchY[DefaultBranch]				=	getNewYPos(FichaATirar, DefaultBranch) + getDespY(NewRotation, NewBranchDirection);

				if (BranchDirection[DefaultBranch] != NewBranchDirection)
				{
				    BranchDirection[DefaultBranch]	= NewBranchDirection;
				    BranchSpacing[DefaultBranch]		=	0;
				}
	      
	      //System.out.println("BranchDirection[] = "+BranchDirection[DefaultBranch]);
	      
				BranchSpacing[DefaultBranch]	+=	Math.abs(getDespX(NewRotation, NewBranchDirection)) + Math.abs(getDespY(NewRotation, NewBranchDirection));

				if (esDoble(FichaATirar))
        {
            LastPut[DefaultBranch] 	= true;
        } else
        {
            LastPut[DefaultBranch] 	= false;
        }				
    }

    // Ponemos la ficha en la mesa
    MoveFichaById(
					    				FichaATirar, 																						// Id de la ficha a mover
					    				ActualPlayer,																						// Recipiente origen
					    				MESA																										// Recipiente destino
    							);
    fichaSelected[ActualPlayer]--;
    corrigeSeleccionPlayer();

    //#ifdef DEBUG_POSTTIRANDOFICHA
    	DebugRecipients();
    	System.out.println("postTirandoFicha() <--");
    //#endif    
}

public int getIncDecX(int Direction)
{
    return(IncDecX[Direction]);
}

public int getIncDecY(int Direction)
{
    return(IncDecY[Direction]);
}

/*
public int getSizeXFicha(int Branch, int IdFicha)
{
    //#ifdef DEBUG_GETSIZEXFICHA
    	System.out.println("---------getSizeXFicha,Branch = "+Branch+" IdFicha = "+IdFicha);
    //#endif
    int rotacionFicha = getBranchRotation(Branch, IdFicha);
    //#ifdef DEBUG_GETSIZEXFICHA
    	System.out.println("-------------rotacionFicha "+rotacionFicha);
    //#endif
    if (BranchDirection[Branch] % 2 == 0) 
    {
        // Horizontales
        //#ifdef DEBUG_GETSIZEXFICHA
        	System.out.println("-------------Horizontales "+((rotacionFicha % 2) + 1));
        //#endif
        return((rotacionFicha % 2) + 1);
    } else
    {
        // Veticales
        //#ifdef DEBUG_GETSIZEXFICHA
        	System.out.println("-------------Verticales   "+(((rotacionFicha + 1) % 2) + 1));
        //#endif
        return(((rotacionFicha + 1) % 2) + 1);
    }
}
*/

public int getSizeXFicha(int Rotacion)
{
    return((Rotacion % 2) + 1);
}

public int getSizeYFicha(int Rotacion)
{
    return(((Rotacion + 1) % 2) + 1);
}


// ---------------------------------------------------------------------------------------------
int FCRed, FCGre, FCBlu;
int ICRed, ICGre, ICBlu;

public void ZonaFondo(int X, int Y, int W, int H, int NumTramos, int Tipo, int MAXY)
{
   if (W<0)
   {
       X -= W;
       W *= -1;
   }
   if (H<0)
   {
       Y -= W;
       H *= -1;
   }
   if (X<0)
   {
       W += X;
       X = 0;
   }
   
   if ((W>0)&&(H>0))
   {
       int ActCol,ActRed,ActGre,ActBlu,Tramo;
       //int GradTam = ((128*3)/2)/NumTramos;
       int GradTam = MAXY/NumTramos;
       for (int Counter = Y; Counter<(Y+H+1); Counter++)
       {
           Tramo = (Counter/GradTam);
           if (Tramo >= NumTramos) Tramo = NumTramos-1;
           ActRed = (((ICRed)*(Tramo))+((FCRed)*((NumTramos)-Tramo)))/(NumTramos);
           ActGre = (((ICGre)*(Tramo))+((FCGre)*((NumTramos)-Tramo)))/(NumTramos);
           ActBlu = (((ICBlu)*(Tramo))+((FCBlu)*((NumTramos)-Tramo)))/(NumTramos);
           Tramo = (Counter/GradTam);
           int Inicio,Final;
           Inicio = (((Tramo*GradTam)>=Y) ? (Tramo*GradTam) : Y);
           Final  = ((((Tramo+1)*GradTam)<=(Y+H)) ? ((Tramo+1)*GradTam) : (Y+H));
           if (Inicio<0) Inicio = 0;
           if (Final<0)  Final = 0;
           if (Inicio>MAXY) Inicio = MAXY;
           if (Final>MAXY)  Final = MAXY;
           if (Final-Inicio>0)
           {
               scr.setColor(ActRed,ActGre,ActBlu);
               scr.fillRect(X,Inicio,W,Final-Inicio);
           }
           Counter = ((Tramo+1)*GradTam);
       }
   }
} 
// --------------------------------------------------------------------------------------------- 
//INTRANCE
int NameWidth,TorneoTitleWidth,TituloPizarraWidth,TituloPizarraHeight;
int popupPizarraX,popupPizarraY;
byte[] EquiposTorneo;
String TorneoTitle; 
String[] EquiposTorneoName; 
byte[][] ResultsTorneo;
byte NumEquipos;
byte NumRondas;
byte PosJugador;
byte RondaActual;
byte TorneoActual;
boolean modoTorneo = false;
byte[] EquiposPartida = new byte[2];
boolean PasoRonda = true;
boolean CargandoTorneo = false;

int[] PuntosActuales = new int[2];
byte[] PuntosTorneo = new byte[] {40,40,40,60};
byte[][] NivelIA = new byte[][] {
    {AI_4,AI_0,AI_0,AI_0}, //Torneo 0
    {AI_4,AI_1,AI_1,AI_1,AI_1,AI_2,AI_0,AI_2}, //Torneo 1
    {AI_4,AI_2,AI_2,AI_2,AI_2,AI_2,AI_2,AI_3}, //Torneo 2
    {AI_4,AI_3,AI_3,AI_3,AI_3,AI_3,AI_3,AI_3,AI_4,AI_4,AI_4,AI_4,AI_4,AI_4,AI_4,AI_4}}; //Torneo 3

public void CreateTorneo(int Torneo)
{
    PuntosActuales[0] = 0;
    PuntosActuales[1] = 0;
    RondaActual = 0;
    TorneoActual = (byte)Torneo;
    formFontInit(FORM_FONT_LARGE);
    TituloPizarraWidth = printStringWidth(gameText[TEXT_CLASIFICACION][0]+" "+gameText[TEXT_CLASIFICACION][4]+PuntosTorneo[TorneoActual]+gameText[TEXT_CLASIFICACION][5]+"0");
    TituloPizarraHeight = printGetHeight();
    formFontInit(FORM_FONT_SMALL_WHITE);
    NameWidth = 0;
    TorneoTitle = gameText[TEXT_TORNEOS][Torneo];
    TorneoTitleWidth = printStringWidth(TorneoTitle);
    NumEquipos = (byte)(gameText[TEXT_TORNEO0+Torneo].length+1);    
    NumRondas = 0;
    while (NumEquipos/exp(2,NumRondas)>=1) NumRondas++;
    NumRondas++;
    EquiposTorneo = new byte[NumEquipos];
    EquiposTorneoName = new String[NumEquipos];
    for (int ActEquipo=0; ActEquipo<NumEquipos; ActEquipo++) EquiposTorneo[ActEquipo] = (byte)(ActEquipo);
    for (int ActEquipo=0; ActEquipo<NumEquipos; ActEquipo++)
    {
        byte SwapEquipo;
        SwapEquipo = EquiposTorneo[ActEquipo];
        EquiposTorneo[ActEquipo] = EquiposTorneo[rnd(EquiposTorneo.length)];
        EquiposTorneo[rnd] = SwapEquipo;
    }
    for (int ActEquipo=0; ActEquipo<NumEquipos; ActEquipo++)
    {
        if (EquiposTorneo[ActEquipo]==0) 
        {
            EquiposTorneoName[ActEquipo] = gameText[TEXT_PLAYER_NAMES][0];
            PosJugador = (byte)(ActEquipo);
        } else EquiposTorneoName[ActEquipo] = gameText[TEXT_TORNEO0+Torneo][EquiposTorneo[ActEquipo]-1];
        NameWidth = Math.max(NameWidth,printStringWidth(EquiposTorneoName[ActEquipo]));
    }
    ResultsTorneo = new byte[NumRondas][];
    for (int ActRonda=0; ActRonda<NumRondas; ActRonda++)
    {
        ResultsTorneo[ActRonda] = new byte[exp(2,(NumRondas-ActRonda)-1)/2];
        for (int ActResult=0; ActResult<ResultsTorneo[ActRonda].length; ActResult++) ResultsTorneo[ActRonda][ActResult] = -100;
    }
    
    TorneoW = 2*Margen + Math.max(ResultSeparator*2+TituloPizarraWidth,NameWidth + ResultSeparator*4 + RondaLength*NumRondas + TorneoTitleWidth);
    TorneoH = 2*Margen + ResultSeparator*3 + TituloPizarraHeight*2 +(ResultSeparator+printGetHeight())*NumEquipos;

    popupPizarraY = Margen+ResultSeparator*2+TituloPizarraHeight*2+(ResultSeparator+printGetHeight())*PosJugador-PizarraBodyY+1-((PizarraBodyH-2)/2);
    popupPizarraX = MinPizarraX = 0-PizarraBodyX-1;
    MinPizarraY = 0-PizarraBodyY-1;
    MaxPizarraX = TorneoW-PizarraBodyW-2-PizarraBodyX+1;
    MaxPizarraY = TorneoH-PizarraBodyH-2-PizarraBodyY+1;
    popupPizarraX = Math.max(MinPizarraX,Math.min(MaxPizarraX,popupPizarraX));
	popupPizarraY = Math.max(MinPizarraY,Math.min(MaxPizarraY,popupPizarraY));
	popupShow = true;
	PasoRonda = true;
}

byte TorneoLock				= 0x0F; //00001111
byte SavedRondaActual		= 1;
byte SavedTorneo			= -1;
byte[] SavedEquiposTorneo 	= new byte[] {0,1,2,3};
String SavedNombreEquipo	= " ";
byte[][] SavedResultsTorneo	= new byte[][] {{0,1,2,3},{-100,-100,-100},{-100,-100},{-100}};
byte SavedEnemy				= 0;
public void LoadTorneo()
{
    PuntosActuales[0] = 0; 
    PuntosActuales[1] = 0;
    RondaActual = SavedRondaActual;
    TorneoActual = SavedTorneo;
    formFontInit(FORM_FONT_LARGE);
    TituloPizarraWidth = printStringWidth(gameText[TEXT_CLASIFICACION][0]+" "+gameText[TEXT_CLASIFICACION][4]+PuntosTorneo[TorneoActual]+gameText[TEXT_CLASIFICACION][5]+"0");
    TituloPizarraHeight = printGetHeight();
    formFontInit(FORM_FONT_SMALL_WHITE);
    NameWidth = 0;
    TorneoTitle = gameText[TEXT_TORNEOS][TorneoActual];
    TorneoTitleWidth = printStringWidth(TorneoTitle);
    NumEquipos = (byte)(gameText[TEXT_TORNEO0+TorneoActual].length+1);
    NumRondas = 0;
    while (NumEquipos/exp(2,NumRondas)>=1) NumRondas++;
    NumRondas++;
    EquiposTorneo = new byte[NumEquipos];
    System.arraycopy(SavedEquiposTorneo,0,EquiposTorneo,0,NumEquipos);
    EquiposTorneoName = new String[NumEquipos];
    for (int ActEquipo=0; ActEquipo<NumEquipos; ActEquipo++)
    {
        if (EquiposTorneo[ActEquipo]==0) 
        {
            EquiposTorneoName[ActEquipo] = SavedNombreEquipo;
            PosJugador = (byte)(ActEquipo);
        } else 
        {
            EquiposTorneoName[ActEquipo] = gameText[TEXT_TORNEO0+TorneoActual][Math.abs(EquiposTorneo[ActEquipo])-1];
        }
        NameWidth = Math.max(NameWidth,printStringWidth(EquiposTorneoName[ActEquipo]));
        System.out.println(EquiposTorneoName[ActEquipo]);
    }
    ResultsTorneo = new byte[NumRondas][];
    for (int ActRonda=0; ActRonda<NumRondas; ActRonda++)
    {
        ResultsTorneo[ActRonda] = new byte[SavedResultsTorneo[ActRonda].length];
        System.arraycopy(SavedResultsTorneo[ActRonda],0,ResultsTorneo[ActRonda],0,SavedResultsTorneo[ActRonda].length);
    }
    TorneoW = 2*Margen + Math.max(ResultSeparator*2+TituloPizarraWidth,NameWidth + ResultSeparator*4 + RondaLength*NumRondas + TorneoTitleWidth);
    TorneoH = 2*Margen + ResultSeparator*3 + TituloPizarraHeight*2 +(ResultSeparator+printGetHeight())*NumEquipos;

    popupPizarraY = Margen+ResultSeparator*2+TituloPizarraHeight*2+(ResultSeparator+printGetHeight())*PosJugador-PizarraBodyY+1-((PizarraBodyH-2)/2);
    popupPizarraX = MinPizarraX = 0-PizarraBodyX-1;
    MinPizarraY = 0-PizarraBodyY-1;
    MaxPizarraX = TorneoW-PizarraBodyW-2-PizarraBodyX+1;
    MaxPizarraY = TorneoH-PizarraBodyH-2-PizarraBodyY+1;
    popupPizarraX = Math.max(MinPizarraX,Math.min(MaxPizarraX,popupPizarraX));
	popupPizarraY = Math.max(MinPizarraY,Math.min(MaxPizarraY,popupPizarraY));
	popupShow = true;
	PasoRonda = true;
}

public void BorrarTorneo()
{
    SavedRondaActual	= 1;
    SavedTorneo			= -1;
    SavedEquiposTorneo 	= new byte[] {0,1,2,3};
    SavedNombreEquipo	= " ";	        
    SavedResultsTorneo	= new byte[][] {{0,1,2,3},{-100,-100,-100},{-100,-100},{-100}};
    SavedEnemy			= 0;   
}

//public int log(int Num, int Base) { for (int ActBase=0; ActBase<Base; ActBase++) Num/=Base; return Num; }
public int exp(int Num, int Base) { int Mul = Num; for (int ActBase=0; ActBase<Base; ActBase++) Num*=Mul; return Num; }

String[] mesa = new String[] {"/mesa1","/mesa2","/mesa2","/mesa3"};
int mesaSeleccionada = 0;
int TorneoW, TorneoH;
int Margen=5;
int RondaLength = 20;
int ResultSeparator = 4;
int LastLineColor = -1;
int PizarraBodyX,PizarraBodyY,PizarraBodyW,PizarraBodyH;
int MinPizarraX,MinPizarraY,MaxPizarraX,MaxPizarraY;
public void PintaTorneo(int X, int Y, int W, int H)
{
    formFontInit(FORM_FONT_SMALL_WHITE);
    printDraw(TorneoTitle ,Margen + NameWidth+ResultSeparator*3+RondaLength*NumRondas-popupPizarraX-printX,Margen + ResultSeparator*2-(printGetHeight()/2)+TituloPizarraHeight*2+(((printGetHeight()+ResultSeparator))/2)*(exp(2,(NumRondas-1))/2)-popupPizarraY-printY,0xFFFFFF,PRINT_LEFT | PRINT_TOP | PRINT_MASK);
    if (RondaActual>0) printDraw(gameText[TEXT_CLASIFICACION][4]+PuntosTorneo[TorneoActual]+gameText[TEXT_CLASIFICACION][5] ,Margen + NameWidth+ResultSeparator*3+RondaLength*NumRondas-popupPizarraX-printX,Margen + ResultSeparator*3+(printGetHeight()/2)+TituloPizarraHeight*2+(((printGetHeight()+ResultSeparator))/2)*(exp(2,(NumRondas-1))/2)-popupPizarraY-printY,0xFFFFFF,PRINT_LEFT | PRINT_TOP | PRINT_MASK);
    for (int ActEquipo=0; ActEquipo<NumEquipos; ActEquipo++)
    {
        if (EquiposTorneo[ActEquipo]<0) formFontInit(FORM_FONT_SMALL_BLACK); else formFontInit(FORM_FONT_SMALL_WHITE);
        printDraw(EquiposTorneoName[ActEquipo], Margen - printStringWidth(EquiposTorneoName[ActEquipo]) + NameWidth - popupPizarraX - printX + ResultSeparator, Margen +TituloPizarraHeight*2+(printGetHeight()/2-4)+ ResultSeparator*2+(ResultSeparator+printGetHeight())*ActEquipo - popupPizarraY - printY, ((EquiposTorneo[ActEquipo]<0)?0x000000:0xFFFFFF) , PRINT_LEFT | PRINT_TOP | PRINT_MASK);
    }
    //#ifdef J2ME
    scr.setClip(X,Y,W,H);
    //#endif
    for (int ActRonda=0; ActRonda<NumRondas; ActRonda++)
    {
        for (int ActResult=0; ActResult<ResultsTorneo[ActRonda].length; ActResult++)
        {
            int ColorLinea = (((ActRonda<(RondaActual))||(ResultsTorneo[ActRonda][ActResult]>-100))?(((ActRonda==RondaActual-1)||(Math.abs(ResultsTorneo[ActRonda][ActResult])==Math.abs(ResultsTorneo[ActRonda+1][ActResult/2])))?((ResultsTorneo[ActRonda][ActResult]==0)?0xFF8000:0xFF0000):0x505050):0xFFFFFF);
            TiraLinea(Margen + NameWidth+ResultSeparator*2+RondaLength*ActRonda,Margen+TituloPizarraHeight*2 + ResultSeparator*2+(((printGetHeight()+ResultSeparator)*(ActResult*2+1))/2)*(exp(2,ActRonda)/2)-1,RondaLength,2,ColorLinea,X,Y,W,H);
            if (ActRonda<(NumRondas-1)) TiraLinea(Margen + NameWidth+ResultSeparator*2+RondaLength*(ActRonda+1),Margen + ResultSeparator*2+TituloPizarraHeight*2+(((printGetHeight()+ResultSeparator)*(ActResult*2+1))/2)*(exp(2,ActRonda)/2)+((ActResult%2>0)?1:-1),2,((printGetHeight()+ResultSeparator)*(exp(2,ActRonda)/2)/2+2)*((ActResult%2>0)?-1:1),ColorLinea,X,Y,W,H);
        }
    }
}

public void PintaPizarra(int X, int Y, int W, int H, int Capa, String Title)
{
    if (Capa == 0)
    {
	    putColor(0x004C14);
	    scr.fillRect(X,Y,W,H);
    }
    
	if (Capa == 1)
	{
	    formFontInit(FORM_FONT_LARGE);
	    TituloPizarraWidth = printStringWidth(Title);
	    TituloPizarraHeight = printGetHeight();
	    TiraLinea(Margen + ResultSeparator,Margen + ResultSeparator+(TituloPizarraHeight*3)/2,TituloPizarraWidth,1,0xFFFFFF,X,Y,W,H);
	    printDraw(Title,Margen + ResultSeparator-popupPizarraX-printX,Margen + ResultSeparator*2-popupPizarraY-printY,0xFFFFFF,PRINT_LEFT | PRINT_TOP | PRINT_MASK);
    }
	    
    if (Capa == 2)
	{
	    putColor(0x6D4519);
	    scr.fillRect(X,Y,W,Margen);
	    scr.fillRect(X,Y+H-Margen,W,Margen);
	    scr.fillRect(X,Y,Margen,H);
	    scr.fillRect(X+W-Margen,Y,Margen,H);
	    putColor(0x9A652C);
	    scr.fillRect(X+1,Y+1,W-(Margen-2),Margen-2);
	    scr.fillRect(X+1,Y+H-(Margen-1),W-(Margen-2),Margen-2);
	    scr.fillRect(X+1,Y+1,2,H-(Margen-2));
	    scr.fillRect(X+W-(Margen-1),Y+1,2,H-(Margen-2));
	    
	    printSpriteDraw(flechas_gfx,X+W-Margen-2*printSpriteWidth(flechas_gfx_cor,0)-1*printSpriteWidth(flechas_gfx_cor,2)+((keyX==-1)?0:1),Y+H-Margen-1*printSpriteHeight(flechas_gfx_cor,0)-1*printSpriteHeight(flechas_gfx_cor,2),0+((popupPizarraX<=MinPizarraX)?4:0),flechas_gfx_cor,PRINT_TOP | PRINT_LEFT | PRINT_MASK);
	    printSpriteDraw(flechas_gfx,X+W-Margen-1*printSpriteWidth(flechas_gfx_cor,1)-0*printSpriteWidth(flechas_gfx_cor,0)-((keyX== 1)?0:1),Y+H-Margen-1*printSpriteHeight(flechas_gfx_cor,1)-1*printSpriteHeight(flechas_gfx_cor,2),1+((popupPizarraX>=MaxPizarraX)?4:0),flechas_gfx_cor,PRINT_TOP | PRINT_LEFT | PRINT_MASK);
	    printSpriteDraw(flechas_gfx,X+W-Margen-1*printSpriteWidth(flechas_gfx_cor,2)-1*printSpriteWidth(flechas_gfx_cor,0),Y+H-Margen-2*printSpriteHeight(flechas_gfx_cor,2)-1*printSpriteHeight(flechas_gfx_cor,0)+((keyY==-1)?0:1),2+((popupPizarraY<=MinPizarraY)?4:0),flechas_gfx_cor,PRINT_TOP | PRINT_LEFT | PRINT_MASK);
	    printSpriteDraw(flechas_gfx,X+W-Margen-1*printSpriteWidth(flechas_gfx_cor,3)-1*printSpriteWidth(flechas_gfx_cor,0),Y+H-Margen-1*printSpriteHeight(flechas_gfx_cor,3)-0*printSpriteHeight(flechas_gfx_cor,0)-((keyY==1 )?0:1),3+((popupPizarraY>=MaxPizarraY)?4:0),flechas_gfx_cor,PRINT_TOP | PRINT_LEFT | PRINT_MASK);
    }
}

public void TiraLinea(int X, int Y, int W , int H, int LineColor, int ClipX, int ClipY, int ClipW, int ClipH)
{
    int FX, FY;
    X -= popupPizarraX;
    Y -= popupPizarraY;
    if ((W == 0)||(H == 0)||(ClipW <= 0)||(ClipH <=0 )) return; 
    if (W < 0)
    {
        W *= -1;
        X -= W; 
    }
    if (H < 0)
    {
        H *= -1;
        Y -= H; 
    }
    FX = Math.min(X+W,ClipX+ClipW); FY = Math.min(Y+H,ClipY+ClipH);
    X = Math.max(X,ClipX); Y = Math.max(Y,ClipY);
    W = FX-X; H = FY-Y;
    if ((W <= 0)||(H <= 0)) return;
    if ((LineColor>=0)&&(LineColor!=LastLineColor)) putColor(LineColor);
    scr.fillRect(X,Y,W,H);
}

public int PosicionTorneo(int Equipo)
{
    for (int ActEquipo=0; ActEquipo<NumEquipos; ActEquipo++)
    {
        if (Equipo==EquiposTorneo[ActEquipo]) return (ActEquipo);
    }
    return 0;
}
//INTRANCE


public void PintaTorneo()
{
    for (int ActRonda=0; ActRonda<NumRondas; ActRonda++)
    {
        for (int ActResult=0; ActResult<ResultsTorneo[ActRonda].length; ActResult++)
        {
            System.out.print(ResultsTorneo[ActRonda][ActResult]+" ");
        }
        System.out.println("  ---- Ronda "+ActRonda);
    }
}

public void PintaRecuento(int X, int Y, int W, int H)
{
    /*printDraw(Title,Margen + ResultSeparator-popupPizarraX-printX,Margen + ResultSeparator*2-popupPizarraY-printY,0xFFFFFF,PRINT_LEFT | PRINT_TOP | PRINT_MASK);
    Margen + ResultSeparator+(TituloPizarraHeight*3)/2*/
}

//ATENCION: Tal como est�, la funci�n misma agrega "Juan dice:" en la primera linea
//ej: AutoGlove(new String[] {"Juan dice",Comentario[0]},JugadorActual, canvasWidth-(canvasWidth/5), (2*canvasHeight)/3);
final static int POPUP_AUTO_D = 0;
final static int POPUP_AUTO_R = 1;
final static int POPUP_AUTO_U = 2;
final static int POPUP_AUTO_L = 3;
public void AutoGlove(String[] str, int Align, int MaxW, int MaxH)
{
	  biosRefresh();
	  forceRender();

    formFontInit(FORM_FONT_SMALL_WHITE);
    int Aspa = 0;
    switch (Align)
    {
	    case POPUP_AUTO_L :
		    Aspa = POPUP_ASPA_DOWN_LEFT;
		break;
	    case POPUP_AUTO_R :
		    Aspa = POPUP_ASPA_DOWN_RIGHT;
		break;
	    case POPUP_AUTO_U :
		    Aspa = POPUP_ASPA_TOP_CENTER;
		break;
		//case POPUP_AUTO_D :
		default :
		    Aspa = POPUP_ASPA_DOWN_CENTER;
		break;
    }
    String[] strOut = new String[str.length+1];
    strOut[0] = (gamePlayerNamesOnly[Align] + gameText[TEXT_DICE][0]);
    System.arraycopy(str,0,strOut,1,str.length);
    int W=0;
    for (int ActLine = 0; ActLine<strOut.length; ActLine++) W = Math.max(W,2*POPUP_MARC_BORDER+printStringWidth(strOut[ActLine])+5+POPUP_SLIDER_WIDTH);
    W=Math.min(MaxW,W);
    int H=Math.min(MaxH-(printSpriteHeight(popupItemsCor, POPUP_ITEM_ASPA + (Aspa&0x7F))),2*POPUP_MARC_BORDER+5+(printGetHeight()*(printTextBreak(strOut,W-(2*POPUP_MARC_BORDER+5+POPUP_SLIDER_WIDTH))).length));
    int X=0;
    int Y=0;
    switch (Align)
    {
	    case POPUP_AUTO_L :
	        X = 0;
	        Y = (canvasHeight-H-printSpriteHeight(popupItemsCor, POPUP_ITEM_ASPA + (Aspa&0x7F)))/2;
		break;
	    case POPUP_AUTO_R :
	        X = (canvasWidth-W);
	        Y = (canvasHeight-H-printSpriteHeight(popupItemsCor, POPUP_ITEM_ASPA + (Aspa&0x7F)))/2;
		break;
	    case POPUP_AUTO_U :
	        X = (canvasWidth-W)/2;
    		Y = canvasTop+printSpriteHeight(popupItemsCor, POPUP_ITEM_ASPA + (Aspa&0x7F));
		break;
    	//case POPUP_AUTO_D :
    	default :
    	    X = (canvasWidth-W)/2;
			Y = CanvasScrollHeight+canvasTop-H-listenerHeight;
    	break;
    }

    //popupLifeTime = STANDAR_LIFE_TIME;
    popupInitGlove(strOut,-1,Aspa,X,Y,W,H);
    biosWait();
}

int numPartidas = 0;
static final int SCORES_PARTIDA_TORNEO = 0;
static final int SCORES_RONDA_TORNEO = 1;
static final int SCORES_PARTIDA_LIBRE = 2;
public void MostrarResultados(int TipoScores)
{
    String[] Top;
    String[] Left;
    String[][] Data;
    
    switch (TipoScores)
    {
    	default :
    	case SCORES_PARTIDA_TORNEO :
    	    Top = new String[] {gameTeamNamesOnly[0],gameTeamNamesOnly[1]};
	        Left = new String[] {gameText[TEXT_ACTUAL][0], gameText[TEXT_TOTAL][0]};
	        Data = new String[][] {{(((PuntosMano>0)&&(ActualPlayer%2==0))?""+PuntosMano:"--"),(((PuntosMano>0)&&(ActualPlayer%2==1))?""+PuntosMano:"--")},{""+PuntosActuales[0],""+PuntosActuales[1]}};
	    break;
	    case SCORES_RONDA_TORNEO :
		    Top = new String[] {gameTeamNamesOnly[0],gameTeamNamesOnly[1], gameText[TEXT_PUNTOS_RONDA][0]};
	        Left = new String[] {gameText[TEXT_PUNTOS][0]};
	        Data = new String[][] {{""+PuntosActuales[0],""+PuntosActuales[1],((PuntosActuales[0]>PuntosActuales[1])?""+(PuntosActuales[0]-PuntosActuales[1]):"--")}};
	    break;
	    case SCORES_PARTIDA_LIBRE :
	        popupMessage(gameText[TEXT_CLASIFICACION][3],((PuntosActuales[0]>0)? (new String[] {gameText[TEXT_SCORE_PARTIDA_LIBRE][0] + PuntosActuales[0]+((PuntosActuales[0]!=1)? gameText[TEXT_SCORE_PARTIDA_LIBRE][1]:gameText[TEXT_SCORE_PARTIDA_LIBRE][2]) + gameText[TEXT_SCORE_PARTIDA_LIBRE][3] + numPartidas+((numPartidas!=1)?gameText[TEXT_SCORE_PARTIDA_LIBRE][4]:gameText[TEXT_SCORE_PARTIDA_LIBRE][5])}):gameText[TEXT_NINGUN_PUNTO]));
	    return;
    }
    //popupInitTable(Top,Left,Data);
    popupInitPizarraTable(Top,Left,Data);
    biosWait();
}

public String[] AttachText(String[][] OriginalText, int Start, int Count)
{
    String[] result = new String[Count];
    
    for (int contador = 0; contador < Count; contador ++)
    {
        result[contador] = OriginalText[Start+contador][0];
    }
    
    return(result);
}

//INTRANCE
public int Puntos(int WorkingPlayer)
{
    int TempPuntos;
    WorkingPlayer %= MAX_PLAYERS;
    
    TempPuntos = 0; //BORRA ESTO
    for (int Ficha = 0; Ficha < GetNumFichasOf(WorkingPlayer); Ficha++)
    {
        TempPuntos += getTopFicha(FichasDe[WorkingPlayer][Ficha]) + getBottomFicha(FichasDe[WorkingPlayer][Ficha]); 
    }
    
    return (TempPuntos);
}
//INTRANCE

// TIRAR FICHAS EN EL TABLERO >____________________________________________________________________________________

// COLORES <_______________________________________________________________________________________________________
	final static int		COLOR_WHITE				= 0xFFFFFF;
	final static int		DARK_GRAY					= 0x0f0f0f;
	
	final static int 		BROWN_A						= 0x6D4519;
	final static int		BROWN_B						= 0x94652C;
	final static int		BROWN_C						= 0x85521B;
	final static int		BLACK							= 0x000000;
	final static int 		DARK_GREEN				= 0x004C14;
// COLORES >_______________________________________________________________________________________________________
	
// SONIDOS <_______________________________________________________________________________________________________
	final static int MUSICA_CARATULA 			= 0; 	// 0 - Musica de la caratula
	final static int MUSICA_MENUS					= 1;	// 1 - Men�s 
	final static int MUSICA_GANAR					= 2;	// 2 - Ganar
	final static int MUSICA_PERDER				= 3;	// 3 - Perder
	final static int FX_PASO							= 4;	// 4 - Paso
	final static int FX_ROBO							= 5;	// 5 - Robo
	final static int FX_LANZO							= 6;	// 6 - Lanzo
	final static int FX_INICIO_PARTIDA		= 7;	// 7 - Inicio partida
	final static int FX_PASAR_TURNO				= 8;	// 8 - Pasar turno
	final static int FX_CERRAR_PARTIDA		= 9;	// 9 - Cerrar partida
	final static int FX_SOLO_UNA_FICHA		= 10;	// 10 - Solo queda una ficha
// SONIDOS >_______________________________________________________________________________________________________	
};