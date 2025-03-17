

// -----------------------------------------------
// Microjocs MagicCasual v1.0 Rev.0 (2.1.2006)
// ===============================================


//#define !RMS_ENGINE



//#ifdef J2ME

package com.mygdx.mongojocs.petanca;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
//#ifdef DEBUG
	Debug.println (" -+- showNotify()");
//#endif

	if (!gameStarted) {return;}

	gamePaused = false;

	gameForceRefresh = true;

//#ifndef DISABLE_SOUND_REFRESH
	gameSoundRefresh = true;
//#endif

	intKeyX = intKeyY = intKeyMisc = intKeyMenu = 0;
}

public void hideNotify()
{
//#ifdef DEBUG
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
// Variables para la gestion del MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int mainSleep = 65;					// 'milis' que han de transcurrir como minimo en una iteracion del bucle principal (un tick)
int gameSleep;						// Contiene los 'milis' que sobraron en la ultima iteracion del bucle principal
long gameMilis, CPU_Milis;			// Gestion del tiempo para el tiempo que dura una iteracion del bucle principal

boolean gameExit = false;			// Forzamos salir del MIDlet al finalizar el bucle principal
boolean gamePaused = false;			// Forzamos pausar el bucle principal del MIDlet
boolean gameForceRefresh = false;	// Forzamos que el bucle principal invoque a los metodos que refrescan el canvas
boolean gameSoundRefresh = false;	// Forzamos que el bucle principal reproduzca el ultimo sonido reproducido, solo si este estaba en modo 'loop'
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

				if (gameSoundRefresh)
				{
				//#ifdef INCOMING_CALL_STOP_SOUND
					// #endif

					gameSoundRefresh = false;
					if (gameSoundOld >= 0 && gameSoundLoop==0) {soundPlay(gameSoundOld,gameSoundLoop);}
				} else {
					soundTick();
				}

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

			try {
				gameDraw();
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
//#elifdef MO-C450
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
static final int BIOS_LOGOS = 11;
static final int BIOS_MENU = 22;
static final int BIOS_POPUP = 33;

String[][] gameText;

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

//#ifdef FULLSCREEN
	//#ifndef LISTENER_DOWN
		canvasHeight -= listenerHeight;		// Reservamos espacio para el area de las softkeys (listener 'simulado')
	//#endif
//#endif

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
//#ifdef RMS_ENGINE
//#endif
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
// bios Tick
// ===================

public void biosTick()
{

//#ifdef DEBUG
	if (keyMisc == 12 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; if (!Debug.enabled) {gameForceRefresh = true;}}
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
		if ( popupTick() ) {biosStatus = biosStatusOld;}
	return;
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
		formShow=true;
	return;
// ====================

// --------------------
// popup Refresh
// ====================
	case BIOS_POPUP:
//		formShow=true;
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
		
//						if (letra == 0xD1 || letra == 0xF1) {letra = 0x7F;}	// es la � o � ???
	
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

//				if (letra == 0xD1 || letra == 0xF1) {letra = 0x7F;}	// es la � o � ???

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

	int size = 0;
	String textos = new String(tex);

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

	scr.setClip(DestX, DestY, SizeX, SizeY);
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
	// <=- <=- <=- <=- <=-

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
// <=- <=- <=- <=- <=-

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

static final int FORM_FONT_SMALL_WHITE = 0;
static final int FORM_FONT_SMALL_BLACK = 1;
static final int FORM_FONT_LARGE = 2;


static final int FORM_COLOR_BACKGROUND = 0xffffff;	// Color de fondo para los formualrios
static final int FORM_COLOR_MARC = 0x525243;		// Color del marco
static final int FORM_COLOR_BOX = 0xE7DEE7;			// Color de la caja normal
static final int FORM_COLOR_BOX_SELECTED = 0xff0000;// Color de la caja seleccionada

//#ifdef S40
//#elifdef S60
static final int FORM_SEPARATOR = 4;
//#elifdef S80
//#endif


static final int FORM_EDIT_MAX_CHARS = 10;	// Numero maximo de letras para la edicion del nombre del jugador


// formLogo
// formTitle
// formOption
// formList
// formSelectRider
// formSelectTrack
// formBikeSettings


static final int FORM_OPTION = 1;
static final int FORM_LIST = 2;
static final int FORM_EDIT_NAME = 3;



static final int FORM_ARROW_BOX_LEFT = 0x06;
static final int FORM_ARROW_BOX_RIGHT = 0x04;
static final int FORM_ARROW_BOX_UP = 0x02;
static final int FORM_ARROW_BOX_DOWN = 0x00;
static final int FORM_ARROW_LEFT = 0x0C;
static final int FORM_ARROW_RIGHT = 0x08;

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
byte[] formFontLargeCor;
//#ifdef J2ME
	Image formFontSmallBlackImg;
	Image formFontSmallWhiteImg;
	Image formFontLargeImg;
//#elifdef DOJA
//#endif


boolean formShow = false;

// -------------------
// form Create
// ===================

Image formBodyImg;

public void formCreate()
{
	formBodyImg = loadImage("/formBody");

	formItemsCor = loadFile("/formItems.cor");
	formFontSmallBlackCor = loadFile("/fontSmallBlack.cor");
	formFontSmallWhiteCor = loadFile("/fontSmallWhite.cor");
	formFontLargeCor = loadFile("/fontLarge.cor");

	//#ifdef J2ME
		formItemsImg = loadImage("/formItems");
		formFontSmallBlackImg = loadImage("/fontSmallBlack");
		formFontSmallWhiteImg = loadImage("/fontSmallWhite");
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
// formSelectRider
// formSelectTrack
// formBikeSettings
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
	formBodyWidth = canvasWidth - (FORM_SEPARATOR * 2);
	formBodyX = (canvasWidth - formBodyWidth) / 2;
	formBodyY = formLogoHeight + formTitleHeight + separatorsHeight;
	formBodyHeight = canvasHeight - formBodyY - FORM_SEPARATOR;


// formText
	if (formTextStr != null)
	{
		formFontInit(FORM_FONT_SMALL_WHITE);

		formTextHeight = (formTextStr.length * printGetHeight()) + FORM_SEPARATOR;
		formTextWidth = formBodyWidth - (FORM_SEPARATOR * 2);
		formTextX = formBodyX + FORM_SEPARATOR;
		formTextY = formBodyY + FORM_SEPARATOR;
	}



	int tmpHeight = formBodyHeight - formTextHeight - FORM_SEPARATOR;
	int tmpY = formBodyY + FORM_SEPARATOR + formTextHeight;


	switch (formBodyMode)
	{
	case FORM_OPTION:
		formFontInit(FORM_FONT_SMALL_BLACK);

		formListEnterHeight = printGetHeight() + (FORM_SEPARATOR * 2);	// Salto de linea entre la (x,y) de una caja a la siguiente.

		formBoxWidth = formBodyWidth - (FORM_SEPARATOR * 2);
		formBoxHeight = printGetHeight() + FORM_SEPARATOR;
		formBoxX = formBodyX + FORM_SEPARATOR;


	// Calculamos cuantas lineas de texto nos entran en pantalla
		formListView = tmpHeight / formListEnterHeight;	// lineas visibles en lo que queda de pantalla
	
	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
		} else {
			int height = tmpHeight - ((FORM_SEPARATOR + printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP)) * 2);	// Reservamos area para dos aspas (arriba y abajo)
	
			formListView = height / formListEnterHeight;	// lineas visibles en lo que queda de pantalla
		}

		formBoxY = tmpY + (tmpHeight - (formListView * formListEnterHeight) + FORM_SEPARATOR) / 2;
	break;


	case FORM_LIST:
		formFontInit(FORM_FONT_SMALL_BLACK);

		formListEnterHeight = printGetHeight();	// Salto de linea una linea y la siguiente.

		formBoxWidth = formBodyWidth - (FORM_SEPARATOR * 2);
		formBoxX = formBodyX + FORM_SEPARATOR;


	// Calculamos cuantas lineas de texto nos entran en pantalla
		formListView = (tmpHeight - (FORM_SEPARATOR * 4)) / formListEnterHeight;	// lineas visibles en lo que queda de pantalla
	
	// Si tenemos menos lineas que el area para mostrar, centramos la impresion, y anulamos el posible scroll vertical
		if (formListView >= formListStr.length)
		{
			formListView = formListStr.length;
	// Si tenemos mas lineas que area para mostrar, reservamos espacio para las aspas del scroll y recalculamos area a mostrar.
		} else {
			int height = tmpHeight - (FORM_SEPARATOR * 2) - ((FORM_SEPARATOR + printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP)) * 2);	// Reservamos area para dos aspas (arriba y abajo)
	
			formListView = height / formListEnterHeight;	// lineas visibles en lo que queda de pantalla
		}

		formBoxHeight = (formListView * formListEnterHeight) + (FORM_SEPARATOR * 2);

		formBoxY = tmpY + (tmpHeight - formBoxHeight) / 2;
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
	break;
	}


/*
		if (formBoxY + formBoxHeight + FORM_SEPARATOR > canvasHeight)
		{
			formLogoImg = null;
			formInit(mode, pos, actionBack, actionAcept);
		}
*/


	formShow = true;
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
		if (keyY > 0 && lastKeyY == 0 && formListPos < formListStr.length-1) {formListPos++;formShow = true;}
		else
		if (keyY < 0 && lastKeyY == 0 && formListPos > 0) {formListPos--;formShow = true;}

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
				}
			}

			formShow = true;
		}

	break;
	}

	return false;
}


// -------------------
// form Draw
// ===================

public void formDraw()
{

// Debemos refrescar el formulario?
	if (!formShow) {return;} else {formShow = false;}


// Limpiamos el canvas con el color correcto
	fillDraw(FORM_COLOR_BACKGROUND, 0,0, canvasWidth, canvasHeight);


// formLogo: Renderizamos logotipo del formulario
	if (formLogoImg != null)
	{
		imageDraw(formLogoImg, 0,0);
	}


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




// formBody: Image
//	fillDraw(FORM_COLOR_BOX, formBodyX, formBodyY, formBodyWidth, formBodyHeight);
	imageDraw(formBodyImg,  0, 0, formBodyWidth, formBodyHeight,  0, 0,  formBodyX, formBodyY, formBodyWidth, formBodyHeight);
	formMarcDraw(formBodyX, formBodyY, formBodyWidth, formBodyHeight);





// formText
	if (formTextStr != null)
	{
		formFontInit(FORM_FONT_SMALL_WHITE);

		printSetArea(formTextX, formTextY, formTextWidth, formTextHeight);
		alphaFillDraw(0xAA000000|FORM_COLOR_BOX_SELECTED, formTextX, formTextY, formTextWidth, formTextHeight);
		formMarcDraw(formTextX, formTextY, formTextWidth, formTextHeight);
		printDraw(formTextStr, 0, 0, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
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
		// Pintamos caja de la opcion con el fondo correcto (opcion seleccionada o no)
			int rgb = (formListIni + i == formListPos? FORM_COLOR_BOX_SELECTED:FORM_COLOR_BOX);
//			fillDraw(rgb, formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
			alphaFillDraw(0xAA000000|rgb, formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
			formMarcDraw(formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
	
		// Cortamos texto para que no se salga de la pantalla
			formFontInit( (formListIni + i == formListPos? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK) );
			String str = printTextCut(formListStr[formListIni+i][formListDat[formListIni+i][2]], formBoxWidth);
	
		// Pintamos texto ya cortado
			printSetArea(formBoxX, formBoxY + addY, formBoxWidth, formBoxHeight);
			printDraw(str, 0, 0, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
	
		// Pintamos aspas horizontales si es necesario
			if (formListStr[formListIni+i].length > 1)
			{
				printDraw(formItemsImg,  4, 0, FORM_ARROW_LEFT + (formListIni + i == formListPos? 1:0),  PRINT_VCENTER|PRINT_LEFT,  formItemsCor);
				printDraw(formItemsImg, -4, 0, FORM_ARROW_RIGHT + (formListIni + i == formListPos? 1:0), PRINT_VCENTER|PRINT_RIGHT, formItemsCor);
			}
		}

	// Si hay mas lineas de texto que el area para mostrarlas, dibujamos las aspas de scroll
		if (formListView < formListStr.length)
		{
			printSetArea(formBoxX, formBoxY, formBoxWidth, formListView*(formBoxHeight+FORM_SEPARATOR));
			printDraw(formItemsImg, 0, -FORM_SEPARATOR -printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP), FORM_ARROW_BOX_UP + (formListIni > 0? 0:1),  PRINT_HCENTER,  formItemsCor);
			printDraw(formItemsImg, 0, printSpriteHeight(formItemsCor, FORM_ARROW_BOX_DOWN), FORM_ARROW_BOX_DOWN + (formListIni < formListStr.length - formListView? 0:1),  PRINT_HCENTER|PRINT_BOTTOM,  formItemsCor);
		}
	break;


	case FORM_LIST:
		alphaFillDraw(0xAA000000|FORM_COLOR_BOX, formBoxX, formBoxY, formBoxWidth, formBoxHeight);
		formMarcDraw(formBoxX, formBoxY, formBoxWidth, formBoxHeight);

	// Pintamos todas las lineas del listado
		printSetArea(formBoxX + FORM_SEPARATOR, formBoxY + FORM_SEPARATOR, formBoxWidth - (FORM_SEPARATOR*2), formBoxHeight - (FORM_SEPARATOR*2));
		formFontInit(FORM_FONT_SMALL_BLACK);
		for (int i=0 ; i<formListView ; i++)
		{
		// Cortamos texto para que no se salga de la pantalla
			String str = printTextCut(formListStr[formListIni+i][formListDat[formListIni+i][2]], formBoxWidth);
	
		// Pintamos texto ya cortado
			printDraw(str, 0, i*formListEnterHeight, formFontPenRGB, PRINT_HCENTER);
		}

	// Si hay mas lineas de texto que el area para mostrarlas, dibujamos las aspas de scroll
		if (formListView < formListStr.length)
		{
			printSetArea(formBoxX , formBoxY , formBoxWidth, formBoxHeight);
			printDraw(formItemsImg, 0, -FORM_SEPARATOR - printSpriteHeight(formItemsCor, FORM_ARROW_BOX_UP), FORM_ARROW_BOX_UP + (formListIni > 0? 0:1),  PRINT_HCENTER,  formItemsCor);
			printDraw(formItemsImg, 0, FORM_SEPARATOR + printSpriteHeight(formItemsCor, FORM_ARROW_BOX_DOWN), FORM_ARROW_BOX_DOWN + (formListIni < formListStr.length - formListView? 0:1),  PRINT_HCENTER|PRINT_BOTTOM,  formItemsCor);

		// Imprimimos los "3 puntos suspensivos" si falta texto por mostrar
			if (formListIni < formListStr.length - formListView)
			{
				printDraw(formItemsImg, -FORM_SEPARATOR, -FORM_SEPARATOR/2, FORM_ITEM_MORE, PRINT_RIGHT|PRINT_BOTTOM, formItemsCor);
			}
		}
	break;


	case FORM_EDIT_NAME:



	// Renderizamos caja donde editamos nombre
		formFontInit(FORM_FONT_SMALL_BLACK);

		alphaFillDraw(0xAA000000|FORM_COLOR_BOX, formBoxX, formBoxY, formBoxWidth, formBoxHeight);
		formMarcDraw(formBoxX, formBoxY, formBoxWidth, formBoxHeight);

		for (int i=0 ; i<formEditStr.length() ; i++)
		{
			printSetArea(formBoxX + FORM_SEPARATOR + (i * formMaxCharWidth), formBoxY, formMaxCharWidth, formBoxHeight);
			printDraw(formEditStr.substring(i,i+1), 0, 1, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		}

//		printDraw("AAAAA", 0, 0, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);


	// Renderizamos celdas "ABCD..."
		for (int cnt=0, y=0; y<formCellNumHeight ; y++)
		{
			for (int x=0; x<formCellNumWidth ; x++)
			{
				int addX = x*formCellWidth;
				int addY = y*formCellHeight;
				printSetArea(formCellX+addX, formCellY+addY, formCellWidth, formCellHeight);
				int rgb = (cnt == formListPos? FORM_COLOR_BOX_SELECTED:FORM_COLOR_BOX);
				alphaFillDraw(0xAA000000|rgb, formCellX+addX, formCellY+addY, formCellWidth, formCellHeight);
				if (cnt < gameText[TEXT_ABECEDARIO][0].length())
				{
					formFontInit( cnt == formListPos? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK);
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
		int rgb = ( eraseBox? FORM_COLOR_BOX_SELECTED:FORM_COLOR_BOX);
		alphaFillDraw(0xAA000000|rgb, printX, printY, printWidth, printHeight);
		formFontInit( eraseBox? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK);
		printDraw(gameText[TEXT_BORRAR][0], 0,1, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		formMarcDraw(printX, printY, printWidth+1, printHeight+1);

	// Renderizamos celda "Aceptar"
		printSetArea(formCellX + (formCellNumWidth * formCellWidth) / 2, formCellY + (formCellNumHeight * formCellHeight), (formCellNumWidth * formCellWidth) / 2, formCellHeight);
		rgb = ( aceptBox? FORM_COLOR_BOX_SELECTED:FORM_COLOR_BOX);
		alphaFillDraw(0xAA000000|rgb, printX, printY, printWidth, printHeight);
		formFontInit( aceptBox? FORM_FONT_SMALL_WHITE:FORM_FONT_SMALL_BLACK);
		printDraw(gameText[TEXT_ACEPTAR][0], 0,1, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
		formMarcDraw(printX, printY, printWidth+1, printHeight+1);



	break;
	}


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

/*
	String[] textos = printTextBreak(texto, menuListWidth);

	for (int i=0 ; i<textos.length ; i++)
	{
		formListAdd(dato, new String[] {textos[i]}, 0, 0);
	}
*/
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

	int textoSizeX = 2;
	for (int sizeX, t=0 ; t<texto.length ; t++) {sizeX = printStringWidth(texto[t]); if ( textoSizeX < sizeX ) {textoSizeX=sizeX;}}

	str[i] = texto;
	dat[i] = new int[] {dat0, dat1, dat2, textoSizeX, printGetHeight() };

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
final static int TEXT_ONE_PLAYER = 31;
final static int TEXT_MORE_PLAYER = 32;
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
// ===============================================


// Variables para la configuaracion del tipo de juego
byte gameDifficulty;
byte gamePoints;
byte gameControl;
byte gameWidthHelp;
byte gameRules;

// Nombre de los jugadores por defecto
String[] gamePlayerNames;

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
			"/Astro_title.mid",		// 0 - Musica de la caratula
			"/Astro_item.mid",		// 1 - 
			"/Astro_muerte.mid",	// 2 - 
			"/Astro_level.mid",		// 3 - 
			});

	//#endif


	//#ifdef SG-D410
	//#endif

//#endif
// ================================



// --------------------------------
// Cargamos imagenes de las softkeys
// ================================
//#ifdef J2ME
	//#ifdef FULLSCREEN
		listenerImg = loadImage("/softkeys");
		listenerCor = loadFile("/softkeys.cor");
	//#endif
//#endif
// ================================



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

		menuCreate();
		menuImg = loadImage("/caratula");

		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:

		menuInit(MENU_MAIN);
//		soundPlay(0,0);

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
		forceRender();

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
		forceRender();

	// Cargamos e inicializamos todos los recursos referentes al juego. Este proceso solo se hace una vez.
		playCreate();
		gameStatus++;

	case GAME_PLAY_INIT:

	// Pintamos la pantalla de negro y mostramos un texto de "Cargando..."
		canvasFillTask(0);
		canvasTextTask(gameText[TEXT_LOADING][0], 0, 0, 0xffffff, PRINT_VCENTER|PRINT_HCENTER);
	// Forzmos un repaint (refresco de pantalla para que se se borre la pantalla y aparezca el texto.
		forceRender();

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
static final int MENU_MULTIPLAYER = 11;
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar las acciones de los menus...
// ===============================================
static final int MENU_ACTION_NONE = -5;
static final int MENU_ACTION_BACK = -10;
static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_HELP = 2;
static final int MENU_ACTION_SHOW_HELP = 3;
static final int MENU_ACTION_SHOW_ABOUT = 4;
static final int MENU_ACTION_EXIT_GAME = 5;
static final int MENU_ACTION_EXIT_GAME_OK = 6;
static final int MENU_ACTION_RESTART = 7;
static final int MENU_ACTION_RESTART_OK = 8;
static final int MENU_ACTION_SOUND_CHG = 9;
static final int MENU_ACTION_VIBRA_CHG = 10;
static final int MENU_ACTION_ASK = 11;
static final int MENU_ACTION_SHOW_OPTIONS = 12;
static final int MENU_ACTION_REPLAY = 13;
static final int MENU_ACTION_REPLAY_OK = 14;

static final int MENU_ACTION_GAME_DIFFICULTY_CHG = 15;
static final int MENU_ACTION_GAME_POINTS_CHG = 16;
static final int MENU_ACTION_GAME_CONTROL_CHG = 17;
static final int MENU_ACTION_GAME_WITH_HELP_CHG = 18;
static final int MENU_ACTION_GAME_RULES_CHG = 19;

static final int MENU_ACTION_EDIT_NAMES = 20;
static final int MENU_ACTION_INPUT_NAME = 21;
static final int MENU_ACTION_MULTIPLAYER = 22;

static final int MENU_ACTION_BACK_TO_GAME = 23;
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

		formLogoInit(menuLogoImg);

//		formTitleInit("Menu principal");

		formListClear();
 		formListAdd(0, gameText[TEXT_ONE_PLAYER], MENU_ACTION_PLAY);
 		formListAdd(0, gameText[TEXT_MORE_PLAYER], MENU_ACTION_MULTIPLAYER);
		formListAdd(0, gameText[TEXT_OPTIONS], MENU_ACTION_SHOW_OPTIONS);
		formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_HELP);
		formListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		formListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_OPTIONS:

		formLogoInit(menuLogoImg);

		formTitleInit(gameText[TEXT_OPTIONS][0]);

		formListClear();
	//#ifndef PLAYER_NONE
		formListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		formListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		formListAdd(0, gameText[TEXT_GAME_DIFFICULTY], MENU_ACTION_GAME_DIFFICULTY_CHG, gameDifficulty);
		formListAdd(0, gameText[TEXT_GAME_POINTS], MENU_ACTION_GAME_POINTS_CHG, gamePoints);
		formListAdd(0, gameText[TEXT_GAME_CONTROL], MENU_ACTION_GAME_CONTROL_CHG, gameControl);
		formListAdd(0, gameText[TEXT_GAME_WITH_HELP], MENU_ACTION_GAME_WITH_HELP_CHG, gameWidthHelp);
		formListAdd(0, gameText[TEXT_GAME_RULES], MENU_ACTION_GAME_RULES_CHG, gameRules);
		formListAdd(0, gameText[TEXT_EDIT_NAMES], MENU_ACTION_EDIT_NAMES);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_INGAME:

		formLogoInit(menuLogoImg);

//		formTitleInit("Menu");

		formListClear();
		formListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
	//#ifndef PLAYER_NONE
		formListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		formListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_HELP);
		formListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		formListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK_TO_GAME, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_HELP:

		formLogoInit(menuLogoImg);

//		formTitleInit("Ayuda");

		formListClear();
		formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_SCROLL_HELP:

		formLogoInit(menuLogoImg);

		formTitleInit(gameText[TEXT_HELP][0]);

		formListClear();
		formListAdd(0, gameText[TEXT_WAIT]);

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		forceRender();

		formListClear();
		formListAdd(0, gameText[TEXT_HELP_SCROLL]);

		formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;


	case MENU_SCROLL_ABOUT:

		formLogoInit(menuLogoImg);

		formTitleInit(gameText[TEXT_ABOUT][0]);

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

		formTitleInit("Confirmacion");

		formListClear();
		formListAdd(0, gameText[TEXT_ARE_YOU_SURE][0], MENU_ACTION_ASK);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
	break;


	case MENU_EDIT_NAMES:

		formLogoInit(menuLogoImg);

		formTitleInit("Nombres");

		formListClear();
		for (int i=0 ; i<gamePlayerNames.length ; i++)
		{
			formListAdd(0, gamePlayerNames[i], MENU_ACTION_INPUT_NAME);
		}
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_INPUT_NAME:

		formLogoInit(menuLogoImg);

		formTitleInit("Introducir nombre");

		formTextInit(new String[] {gamePlayerNames[menuEditPlayerSelected]});

		formInit(FORM_EDIT_NAME, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);
/*
	// Hack para eliminar el logotipo de la pantalla del menu si este no entra en el canvas
		if (formBoxY + formBoxHeight + FORM_SEPARATOR > canvasHeight)
		{
			formLogoImg = null;
			formInit(FORM_EDIT_NAME, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);
		}
*/
		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;


	case MENU_MULTIPLAYER:

		formLogoInit(menuLogoImg);

		formTitleInit(gameText[TEXT_MORE_PLAYER][0]);

		formTextInit(new String[] {"Modo de juego"});

		formListClear();
		for (int i=0 ; i<gameText[TEXT_MULTIPLAYER_MODES].length ; i++)
		{
			formListAdd(0, gameText[TEXT_MULTIPLAYER_MODES][i], MENU_ACTION_PLAY);
		}
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
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
// Sistema Stack para los menus
	menuStack[menuStackIndex][0] = menuType;
	menuStack[menuStackIndex][1] = formListPos;
	if (++menuStackIndex >= menuStack.length) {menuStackIndex -= menuStack.length;}

// Limpiamos pantalla
//	menuListSet_None();
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
		menuInit(menuStack[menuStackIndex][0], menuStack[menuStackIndex][1]);
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{
	switch (cmd)
	{

	case MENU_ACTION_BACK:	// Ir "Atras" entre los menus

		menuRelease();
		menuInitBack();
	break;


	case MENU_ACTION_BACK_TO_GAME:	// Ir "Atras" al campo de juego

		menuRelease();
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case -3: // Scroll o Screen ha sido abortado por usuario
	case -2: // Scroll o Screen ha llegado al final

		menuRelease();
		menuInitBack();
	break;


	case MENU_ACTION_PLAY:		// Jugar
		menuRelease();

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_CONTINUE:	// Continuar

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_OPTIONS:		// Mostramos Opciones

		menuRelease();

		menuInit(MENU_OPTIONS);
	break;


	case MENU_ACTION_HELP:		// Mostramos Menu de Instrucciones...

		menuRelease();

		menuInit(MENU_HELP);
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

		popupAsk("�Est� seguro?", POPUP_ACTION_GAME_EXIT);
	break;	



	case MENU_ACTION_SOUND_CHG:

		gameSound = formListOpt() != 0;
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
		gameWidthHelp = (byte)formListOpt();
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

	case MENU_ACTION_MULTIPLAYER:

		menuRelease();
		menuInit(MENU_MULTIPLAYER);
	break;

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

static final int POPUP_ASK = 0;


static final int POPUP_MARC_WIDTH = 24;
static final int POPUP_MARC_HEIGHT = 24;
static final int POPUP_MARC_BORDER = 4;


static final int POPUP_ITEM_ASPA = 0x09;
static final int POPUP_ITEM_TITLE_LOGO = 0x0A;
static final int POPUP_ITEM_MORE = 0x0B;




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
int popupTextPos;
int popupTextView;
String[] popupTextStr;


// Variables para la gestion del titulo del popup
int popupTitleHeight;
String popupTitleStr;


// Variables internas para la gestion del popupEngine
boolean popupBorderGlobe;
boolean popupIconEnable;
boolean popupExit;
int popupType;
int popupActionCancel;
int popupActionAcept;
int popupLeftSoftkeyBack;
int popupRightSoftkeyBack;

	byte[] popupItemsCor;
//#ifdef J2ME
	Image popupItemsImg;
//#elifdef DOJA
//#endif

boolean popupShow = false;

// -------------------
// popup Create
// ===================

public void popupCreate()
{

	popupItemsCor = loadFile("/popupItems.cor");

	//#ifdef J2ME
		popupItemsImg = loadImage("/popupItems");
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
	popupX = x;
	popupY = y;
	popupWidth = width;
	popupHeight = height;
}



public void popupAsk(String str, int actionAcept)
{

	str = "Hola me llamo juan y esto es una prueba para ver si se corta bien el texto pues si no lo hace pues no conseguimos nada de nada, por este motivo espero que este texto se corte bien!";


	popupTextStr = new String[] {str};

	popupTitleStr = "Confirmaci�n";

	popupBorderGlobe = false;

	popupIconEnable = false;

	popupInit(POPUP_ASK, POPUP_ACTION_CANCEL, actionAcept, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
}



// -------------------
// popup Init
// ===================

public void popupInit(int type, int actionCancel, int actionAcept, int leftSoftkey, int rightSoftkey)
{
// Guardamos tipo de popup
	popupType = type;


// Guardamos acciones a ejecutar tras pulsar softkeys
	popupActionCancel = actionCancel;
	popupActionAcept = actionAcept;


// Actualizamos listener con texto para el popup
	popupLeftSoftkeyBack = listenerIdLeft;
	popupRightSoftkeyBack = listenerIdRight;
	listenerInit(leftSoftkey, rightSoftkey);


	int cursorX = POPUP_MARC_BORDER;
	int cursorY = POPUP_MARC_BORDER;

// popupTitle
	if (popupTitleStr != null)
	{
		formFontInit(FORM_FONT_SMALL_WHITE);
		popupTitleHeight = printGetHeight() + 2;
		cursorY += popupTitleHeight;
	}


// popupTitleIcon
	if (popupIconEnable)
	{
		popupTitleIconWidth = printSpriteWidth(popupItemsCor, 0x0A);
		popupTitleIconHeight = printSpriteHeight(popupItemsCor, 0x0A);
		popupTitleIconX = popupX + cursorX;
		popupTitleIconY = popupY + cursorY;
		cursorX += popupTitleIconWidth;
		cursorY += popupTitleIconHeight / 2;
	}


// popupBody
	popupBodyX = popupX + cursorX;
	popupBodyY = popupY + cursorY;
	popupBodyWidth = popupWidth - cursorX - POPUP_MARC_BORDER;
	popupBodyHeight = popupHeight - cursorY - POPUP_MARC_BORDER;



// popupType

	switch (popupType)
	{
	case POPUP_ASK:

	// popupText
		formFontInit(FORM_FONT_SMALL_BLACK);
	
		popupTextWidth = popupBodyWidth;
		popupTextHeight = popupBodyHeight;
	
		popupTextStr = printTextBreak(popupTextStr, popupTextWidth);
	
		popupTextView = popupTextHeight / printGetHeight();
	
		if (popupTextView > popupTextStr.length)
		{
			popupTextView = popupTextStr.length;
		} else {
			popupTextHeight -= printSpriteHeight(popupItemsCor, POPUP_ITEM_MORE);
			popupTextView = popupTextHeight / printGetHeight();
		}
	
		int tmpHeight = popupTextView * printGetHeight();
	
		popupTextX = popupBodyX;
		popupTextY = popupBodyY + ((popupTextHeight - tmpHeight) / 2);
	
		popupTextHeight = tmpHeight;
	break;
	}



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
	listenerInit(popupLeftSoftkeyBack, popupRightSoftkeyBack);
	gameForceRefresh = true;
}


// -------------------
// popup Tick
// ===================

public boolean popupTick()
{
	switch (popupType)
	{
	case POPUP_ASK:

	// Controlamos si pulsamos softkey Izquierda
		if (keyMenu < 0 && lastKeyMenu == 0)
		{
			popupAction(popupActionCancel);
		}

	// Controlamos si pulsamos softkey Derecha
		if (keyMenu > 0 && lastKeyMenu == 0)
		{
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
	}

	return popupExit;
}


// -------------------
// popup Draw
// ===================

public void popupDraw()
{
	if (!popupShow) {return;} else {popupShow = false;}


// Renderizamos el cuerpo del popup (interior liso y bordes)
	if (!popupBorderGlobe)
	{
		fillDraw(FORM_COLOR_BOX, popupX, popupY, popupWidth, popupHeight);
		rectDraw(FORM_COLOR_MARC, popupX, popupY, popupWidth, popupHeight);
	} else {
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
	}


// popupTitle
	if (popupTitleStr != null)
	{
		formFontInit(FORM_FONT_SMALL_WHITE);
		printSetArea(popupX, popupY, popupWidth, popupTitleHeight);
		if (!popupBorderGlobe)
		{
			fillDraw(FORM_COLOR_BOX_SELECTED, popupX, popupY, popupWidth, popupTitleHeight);
			rectDraw(FORM_COLOR_MARC, popupX, popupY, popupWidth, popupTitleHeight);
		}
		printDraw(popupTitleStr, 0, 0, formFontPenRGB, PRINT_HCENTER|PRINT_VCENTER);
	}


// popupTitleIcon
	if (popupTitleIconHeight != 0)
	{
		spriteDraw(popupItemsImg, popupTitleIconX, popupTitleIconY, POPUP_ITEM_TITLE_LOGO, popupItemsCor);
	}


// popupType
	switch (popupType)
	{
	case POPUP_ASK:
		formFontInit(FORM_FONT_SMALL_BLACK);
		printSetArea(popupTextX, popupTextY, popupTextWidth, popupTextHeight);
	
//rectDraw(0xffffff, popupTextX, popupTextY, popupTextWidth, popupTextHeight);
	
		for (int i=0 ; i<popupTextView ; i++)
		{
			printDraw(popupTextStr[popupTextIni + i], 0, i*printGetHeight(), formFontPenRGB, PRINT_HCENTER);
		}
	
	// Mostramos icono que hace referencia a que hay mas texto por mostrar
		if (popupTextStr.length > popupTextView)
		{
			printDraw(popupItemsImg,  0, printSpriteHeight(popupItemsCor, POPUP_ITEM_MORE), POPUP_ITEM_MORE,  PRINT_BOTTOM|PRINT_RIGHT,  popupItemsCor);
		}

	// Mostramos "aspa" del globo si el borde el popup es de tipo globo
		if (popupBorderGlobe)
		{
			spriteDraw(popupItemsImg,  popupX + (popupWidth / 8), popupY + popupHeight - 1, POPUP_ITEM_ASPA, popupItemsCor);
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

static final int POPUP_ACTION_CANCEL = 1;
static final int POPUP_ACTION_GAME_EXIT = 2;

public void popupAction(int action)
{
	switch (action)
	{
	case POPUP_ACTION_CANCEL:

		popupRelease();
		popupExit = true;
	break;

	case POPUP_ACTION_GAME_EXIT:

		popupRelease();
		popupExit = true;

		menuRelease();
		menuExit = true;

		gameStatus = GAME_EXIT;
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


// -----------------------------
// Ejemplo por defecto:
int pelotaX, pelotaY;
int pelotaSideX=1, pelotaSideY=1;
// =============================


int playExit;
boolean playShow = false;

// -------------------
// play Create
// ===================

public void playCreate()
{
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
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

public boolean playTick()
{


// -----------------------------
// Ejemplo por defecto:
	pelotaX += pelotaSideX * 3;
	pelotaY += pelotaSideY * 4;

	pelotaSideY --;
	if (pelotaX < 0 || pelotaX > canvasWidth) {pelotaSideX *= -1;}
	if (pelotaY < 0 || pelotaY > canvasHeight) {pelotaSideY *= -1;}
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


// -----------------------------
// Ejemplo por defecto:
	canvasFillDraw(0x0000aa);
	fillDraw(0xffffff, pelotaX-2, pelotaY-2, 4, 4);
// =============================




}


// -------------------
// play Refresh
// ===================

public void playRefresh()
{
}

// <=- <=- <=- <=- <=-













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

	do {
	// Si es null es que nunca se han grabado prefs, por lo cual INICIALIZAMOS las prefs del juego
		if (prefsData == null)
		{
		// Inicializamos preferencias ya que NO estaban grabadas
			savePrefs();
			return;
		}

	// Actualizamos las variables del juego segun las prefs leidas / inicializadas
		try {

			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(prefsData, 0, prefsData.length));

			gameSound = dis.readByte() != 0;
			gameVibra = dis.readByte() != 0;

			gamePlayerNames = new String[dis.readInt()];
			for (int i=0 ; i<gamePlayerNames.length ; i++)
			{
				gamePlayerNames[i] = dis.readUTF();
			}


		} catch(Exception e)
		{
	// Si catch, entonces prefs corruptas, inicializarlas de cero.
		//#ifdef DEBUG
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
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	try {
	// Ponemos las varibles del juego a salvar como prefs.

		dos.writeByte(gameSound? 1:0);
		dos.writeByte(gameVibra? 1:0);

		dos.writeInt(gamePlayerNames.length);
		for (int i=0 ; i<gamePlayerNames.length ; i++)
		{
			dos.writeUTF(gamePlayerNames[i]);
		}

	// Almacenamos las prefs
		updatePrefs(baos.toByteArray());		// Almacenamos byte[]

	} catch(Exception e)
	{
	//#ifdef DEBUG

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