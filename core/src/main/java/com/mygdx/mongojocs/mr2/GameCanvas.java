

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.7 (08.4.2005)
// ===============================================
// Representacion Grafica
// ------------------------------------


//#define RMS_ENGINE


//#ifdef PACKAGE
package com.mygdx.mongojocs.mr2;
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
		//#ifdef MO-C450
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

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga) 
{
	this.ga = ga;
	//#ifdef FORCE_FULLSCREEN
	setFullScreenMode(true);
	//#endif
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public static final int PELAO_PROTOCOL_ID = 1;

//#ifdef FILE_PACK
/*public static final int GFX_BANDERAS_COR = 0;
public static final int GFX_BANDERAS_PNG = 1;
public static final int GFX_CARATULA_PNG = 2;
public static final int GFX_LOADING_PNG = 3;
public static final int GFX_MENUITEMS_COR = 4;
public static final int GFX_MENUITEMS_PNG = 5;
public static final int GFX_MICROJOCS_PNG = 6;
public static final int GFX_MOVISTAR_PNG = 7;
public static final int GFX_SOFTKEYS_COR = 8;
public static final int GFX_SOFTKEYS_PNG = 9;
public static final int GFX_TEXTOS_TXT = 10;
public static final int GFX_V2_COR = 11;
public static final int GFX_V2_PNG = 12;



public static final int[] filePositions = {0,126,1105,3665,3864,3882,4193,5221,6831,6891,7174,10713,11673,12622};*/
//#endif

//track
	public static final int TRACK_CENTRAL_BIT = 0x01;
	public static final int TRACK_LEFT_BIT = 0x02;
	public static final int TRACK_RIGHT_BIT = 0x04;
	public static final int TRACK_SPEEDUP_BIT = 0x08;
	public static final int TRACK_SLOWDOWN_BIT = 0x10;
	public static final int TRACK_SKID_BIT = 0x20;
	public static final int TRACK_STRECH_BIT = 0x40;

	public static final int TRACK_INTS_PER_PATCH = 10;
	public static final int TRACK_INTS_PER_LINE = 7;
	
	//#ifdef UBER_LOW_QUALITY
	//#else
	public static final int TRACK_NUM_SECTIONS_AHEAD = 18;	//18;
	public static final int TRACK_SECTIONS_CUT = 2;
	//#endif
	public final static int TRACK_LONGITUDE_FACTOR_DIV = 11;//8;//8; //10




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

	//#ifndef SD-P600
	gamePaused = false;

	gameForceRefresh = true;

//#ifndef DISABLE_SOUND_REFRESH
	gameSoundRefresh = true;
//#endif

	intKeyX = intKeyY = intKeyMisc = intKeyMenu = 0;
	//#endif
	
	//#ifdef BIG_PANEL
	//firstFrame = true;
	//resetCachedValues();
	//#endif
}

public void hideNotify()
{
//#ifdef DebugConsole
	Debug.println (" -+- hideNotify()");
//#endif

//#ifndef SD-P600
		gamePaused = true;

	//solventar que no corra el tiempo si nos llaman (no probado)
		menuStart = 1;
	//#ifndef DISABLE_SOUND_REFRESH
		gameSoundOld = soundOld;
		gameSoundLoop = soundLoop;
		soundStop();
	//#endif
//#endif
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit;
boolean gamePaused;
boolean gameForceRefresh;
boolean gameSoundRefresh;
int gameSoundOld = -1;
int gameSoundLoop;

long gameMilis, CPU_Milis;
int gameSleep;
int mainSleep = 50;
//Integer tDifMutex = new Integer(0);
long menuStart;
long timeDiff;
//long timeDiff2 = 0;
long lastChange;
int tickCount;
int lastTickCount;
int lastDeltaTime;
int deltaTime;
		
boolean forceTick;
boolean ghostBeaten;
//#ifdef BIG_PANEL
/*boolean turboFB = false;
boolean firstFrame;
long chLastLapTime;
long chBestTime;
long chLapTime;
int chVel;
int chPos;
int chMaxPos;
int chLapCounter;*/
//#endif

//#ifdef CHEATS
//	public boolean cheatsEnabled = false;
//#endif

//trackgamerenderer
public static final int TRACK_ARROW_THRESHOLD = 15;		//1.5 secs
	public static final int BOOST_THRESHOLD = 25;
	//#ifdef HORIZON_QUALITY_4
	//#elifdef HORIZON_QUALITY_3
	//#elifdef HORIZON_QUALITY_2
	public static final int HORIZON_CUT_SIZE = 8;
	//#elifdef HORIZON_QUALITY_1
	//#elifdef HORIZON_QUALITY_0
	//#else
	//#endif
	
	//#ifdef RENDER_DETAIL_4
	//#elifdef RENDER_DETAIL_3
	//#elifdef RENDER_DETAIL_2
	//#elifdef RENDER_DETAIL_1
	//#elifdef RENDER_DETAIL_0
	//#else
	public static final int	max_detail = 5;
	//#endif

	
	public boolean boostMode;
	public Graphics gBuffer;
	public GameCanvas gc;
	public int frames;
	public Image panelImage;
	public Image bikeImageA;	//motos gegants (nivell 0)
	public Image bikeImageB;	//motos grans i mitjanes (nivells 1 - 2)
	//#ifdef J2ME
	public Image bikeImageC;	//motos petites (nivells 3 - 7)
	public Image bikeImageD;	//ostia moto
	public Image smoke;
	//#elifdef DOJA
	//#endif
	public Image bikeWheelB;
	public Image turbo;	
	public byte[] turboCoord;
	public byte[] smokeCoord;
	public byte[] bikeAcoord;
	public byte[] bikeBcoord;
	public byte[] bikeCcoord;
	public byte[] bikeDcoord;
	public byte[] bikeWBcoord;
	public int flagCnt;
	//public Image horizonImage
	public Image ckmImage;
	
	//#ifndef NK-s40
	public Image arrow;
	//#ifdef J2ME
		public Image trackArrows;
	//#elifdef DOJA
	//#endif	
	public Image panel0;
	public Image panel1;
	public Image panelSem;
	public Image semR;
	public Image semG;
	
	//#ifdef J2ME
		public Image font0;
		public Image font1;
	//#elifdef DOJA
	//#endif
	
	//#endif
	
	public int lastArrow;
	public long lastArrowTime = -TRACK_ARROW_THRESHOLD;
	public long lastBoostTime = -BOOST_THRESHOLD;
	public int lastUsid;
	public int skipped;
	public int[] xformedVertexs = new int[TRACK_NUM_SECTIONS_AHEAD * 4 + 2 * 4];
	public int heightCanvas=666;
	//public int[] depthLevels = {12, 21, 36, 51, 70, 100, 127};	//ripejada de toca

	public int ydispl;
	public int frameCounter;
	public Font renderFont;
	public int framepair;
	public Image horizonImg;
	public int pos = 2;
	
	public int phantomx;
	public int phantomy;
	public int phantomz;
	
	
	//track
	public int[] track_arrayVxy;
	public byte[] track_arrayAlt;
	
	public int[] track_arrayLong;
	
	public int[] track_vertexSectionArray;
	public byte[] track_typeSectionArray;
	public byte[] track_verticalTypeSectionArray;
	
	public int track_shadeColor=0x80E0F0;
	public int track_shadeLerpFactor=32;
	public int track_startBackgroundTime;
	public int track_startSkyColor;
	public int track_startHorizonColor;
	
	public int track_endBackgroundTime;
	public int track_endSkyColor;
	public int track_endHorizonColor;

	//public TrackSection[] track_trackSectionsMap;
	//public TrackSection[] track_verticalTrackSectionsMap;
	public int[][]	track_trackSectionsMap_patchList;
	public int[][]	track_trackSectionsMap_lineList;
	public byte[]	track_trackSectionsMap_flags;
	
	public int[][]	track_verticalTrackSectionsMap_patchList;
	public int[][]	track_verticalTrackSectionsMap_lineList;
	public byte[]	track_verticalTrackSectionsMap_flags;
	
	public int track_calcPosX;
	public int track_calcPosY;
	public int track_calcPosZ;
	public int track_fpCalcPosX;
	public int track_fpCalcPosY;
	public int[] track_posArrayX = new int[TRACK_NUM_SECTIONS_AHEAD];
	public int[] track_posArrayY = new int[TRACK_NUM_SECTIONS_AHEAD];
	public int[] track_posArrayZ = new int[TRACK_NUM_SECTIONS_AHEAD];
	public int track_calcDirX;
	public int track_calcDirY;
	
	
	//28 anims                                                                                            ^ mark!!
//	byte[] frame = {  0,   0,   0,   1,   1,    1,    2,    2,     2,    2,    3,    3,    3,    3,    4,    4,    4,    4,    5,    5,    5,    5,    6,    6,    6,    6,    6,    6};
//	byte[] pframe = { 0,   0,   0,   0,   0,    0,    0,     0,    1,    1,    1,    2,    2,    2,    3,    3,    3,    4,    4,    4,    4,    5,    5,    5,    6,    6,    6,    6};
//	byte[] xpos  = {-50, -49, -46, -42, -36,  -34,  -30,   -27,  -26,  -22,  -22,  -20,  -16,  -14,  -12,  -10,   -9,   -8,    0,    2,    4,    7,   12,   13,   17,   18,   19,   20};
//	byte[] ypos  = {-50, -60, -76, -77, -85,  -92,  -92,   -95,  -93,  -91,  -91,  -88,  -85,  -81,  -78,  -75,  -72,  -66,  -72,  -77,  -71,  -74,  -71,  -72,  -74,  -74,  -74,  -74};//-74,  -72,  -75,  -91,  -95,  -98,  -95, -103, -102, -100, -100, -100, -100};
//	int[] pxpos   = { 0,   0,   0,   0,   0,  -45,  -51,   -56,  -65,  -69,  -72,  -81,  -87,  -92,  -99, -106, -109, -123, -126, -129, -131, -131, -135, -138, -140, -142, -142, -144};
//	byte[] pypos  = { 0,   0,   0,   0,   0, -100, -106,  -110, -110, -110, -108, -106, -104, -103, -100,  -97,  -95,  -89,  -93,  -94,  -94,  -94,  -96,  -97,  -99,  -99,  -99,  -99};

	byte[] frame;
	byte[] pframe;
	byte[] xpos;
	byte[] ypos;
	int[] pxpos;
	byte[] pypos;
	//#ifdef NO_UNLOAD
	public boolean dataLoaded;
	//#endif
	
	//#ifdef NK-s40
	//#endif
//eo trackgamerenderer

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
			
			//#ifdef CHEATS
			/*if(keyMisc == 10) {
				if(cheatsEnabled) {
					cheatsEnabled = false;
				} else {
					cheatsEnabled = true;
				}
			}*/
			//#endif

			try {
				biosTick();
				if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}
				gameDraw();

				if (gameSoundRefresh)
				{
					gameSoundRefresh = false;
	
				//#ifndef NK-s60
					//if (gameSoundOld >= 0 && gameSoundLoop==0) {soundPlay(gameSoundOld,gameSoundLoop);}
					//rai metio mano
					//solo queremos que nos suene el sonido del menu principal, en pausa no hay ninguna musica a resumir
				//#else
					gameSound = false;
				//#endif
				} else {
					soundTick();
				}

			} catch (Exception e)
			{
			//#ifdef com.mygdx.mongojocs.mr2.Debug
				Debug.println("*** Exception Logica ***" + e.getMessage());
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
	//DOJAPORT
	//biosDestroy();
	// ---------------------------------------

	//#ifdef J2ME
		savePrefs();
	//#elifdef DOJA
	//#endif

		//gameDestroy(); // DOJAPORT - Está vacio
	// ---------------------------------------

	ga.destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Forzamos que se refresque la pantalla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void gameDraw()
{
	//try {Thread.sleep(100);} catch(Exception e) {}
	lastFrameMillis = startMillis;
	startMillis = System.currentTimeMillis();
	
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
	synchronized (this)
	{
		if (canvasShow)
		{
			scr = g;

		//#ifdef DOJA
		//#endif

			try {
				canvasDraw();
			} catch (Exception e)
			{
			//#ifdef com.mygdx.mongojocs.mr2.Debug
				Debug.println("*** Exception Grafica ***");
				Debug.println("Msg:"+e.getMessage());
				Debug.println("Class:"+e.getClass().toString());
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}

//#ifdef com.mygdx.mongojocs.mr2.Debug
	if (Debug.enabled) {Debug.debugDraw(this, scr); return;}
//	com.mygdx.mongojocs.mr2.DebugX.getInstance().drawVersion(g, canvasWidth, canvasHeight);
//#endif
		
		//#ifdef CHEATS
		/*
		//#ifdef J2ME
		g.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		if(cheatsEnabled) {
			g.setColor(0xFF0000);
		} else {
			g.setColor(0x00FF00);
		}
		g.fillRect(0,0,2,2);*/
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
// ===============================

// --------------------------------
// Inicializamos com.mygdx.mongojocs.mr2.Debug Engine
// ================================
//#ifdef com.mygdx.mongojocs.mr2.Debug
	Debug.debugCreate(this);
	Debug.println("Total:"+ga.debugMemIni);
	Debug.println("Free:"+ga.debugMenFin);
//#endif
// ================================


// --------------------------------
// Pintamos TODO el canvas de color NEGRO de forma INMEDIATA
// ================================
	canvasFillTask(0xffffff);		// Color Blanco
	gameDraw();
//#ifdef FULLSCREEN
	//#ifndef LISTENER_DOWN
		canvasHeight -= listenerHeight;
	//#endif
//#endif
	//#ifndef FILE_PACK
	canvasImg = loadImage("/loading");
	//#else
	//#endif
	gameDraw();						// FORZAMOS una llamada a 'canvasDraw()' AHORA MISMO.
// ================================


// --------------------------------
// Descargamos el MFS de internet, gestionado por una barra de progreso
// ================================
//#ifdef DOJA
//#endif
// ================================


	//#ifndef FILE_PACK
	gameText = textosCreate( loadFile("/Textos.txt") );
	//#else
	//#endif
	
	System.gc();
// --------------------------------
// Creamos e inicializamos rmsEngine (Mini Disco Duro)
// ================================
//#ifdef RMS_ENGINE
	//#ifdef VERY_BIG_RMS
	//#elifdef BIG_RMS
	//#elifdef MEDIUM_RMS
		rmsCreate(50 * 1024);		// Solicitamos 50Kb
	//#elifdef ULTRA_SMALL_RMS
	//#else
	//#endif
//#endif
// ================================


//#ifdef J2ME
	String version = ga.getAppProperty("MIDlet-Version");
//#elifdef DOJA
//#endif

	if (version != null) {gameText[TEXT_ABOUT_SCROLL][1] = version;}


// --------------------------------
// Cargamos Preferencias
// ================================

//#ifdef J2ME
	loadPrefs();
//#elifdef DOJA
//#endif

// ================================


	gameCreate();


}


//DOJAPORT
/*
// -------------------
// bios Destroy
// ===================

public void biosDestroy()
{
	savePrefs();

	gameDestroy();
}
*/

// -------------------
// bios Tick
// ===================

public void biosTick()
{

//#ifdef com.mygdx.mongojocs.mr2.Debug
	if (keyMisc == 12 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; if (!Debug.enabled) {gameForceRefresh=true;}}

	if (Debug.enabled)
	{
		if (keyMisc == 11 && lastKeyMisc == 0) {rmsGetDir();}
	}
//#endif

	pelaoTick();


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
	}
}

// -------------------
// bios Refresh
// ===================

public void biosRefresh()
{
//#ifdef com.mygdx.mongojocs.mr2.Debug
	Debug.println ("biosRefresh()");
	Debug.println("fm " + Runtime.getRuntime().freeMemory());
	Debug.println("tm " + Runtime.getRuntime().totalMemory());
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

		formShow=true;
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

int numLogos = 0;
int cntLogos = 0;
//#ifndef FILE_PACK
String[] nameLogos;
//#else
//#endif
int[] rgbLogos;
int timeLogos;
long timeIniLogos;

//#ifndef FILE_PACK
public void logosInit(String[] fileNames, int[] rgb, int time)
//#else
//#endif
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

//#ifdef J2ME

public byte[] updatePrefs(byte[] bufer)
{

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
}
//#endif

// <=- <=- <=- <=- <=-
















// *******************
// -------------------
// util - Engine
// ===================
// *******************

// -------------------
// regla3
// ===================

//public int regla3(int x, int min, int max) {return (x*max)/min;}


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

	if (destX >= canvasWidth || destY >= canvasHeight || (destX + sizeX) <= 0 || (destY + sizeY) <= 0) {return;}

//#ifdef SI-x55
//#endif

	scr.setClip(destX, destY, sizeX, sizeY);
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

	/*
	System.gc();
	byte[] buffer = null;
//#ifdef DOJA
//#endif
	InputStream is = getClass().getResourceAsStream(Nombre);

	buffer = new byte[1024];

	try
	{
		int Size = 0;

		while (true)
		{
			int Desp = is.read(buffer, 0, buffer.length);
			if (Desp <= 0) {break;}
			Size += Desp;
		}

		is = null; System.gc();

		buffer = new byte[Size];

		is = getClass().getResourceAsStream(Nombre);
		Size = is.read(buffer, 0, buffer.length);

		while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}

		is.close();
	}
	catch(Exception exception)
	{
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("loadFile: "+Nombre+" <File not found>");
	//#endif
		return null;
	}

//#ifdef com.mygdx.mongojocs.mr2.Debug
	Debug.println("loadFile: "+Nombre+" <"+buffer.length+">");
//#endif

	System.gc();
	return buffer;*/
}
//#ifdef FILE_PACK
//#endif


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// -------------------
// load Image
// ===================

//#ifdef J2ME
// ---------------------------------------------------------

//#ifdef MGF
	//#endif


public Image loadImage(String FileName)
{
	
	//#ifdef MGF
	//#else
	FileName += ".png";

	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e)
	{
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("loadImage: "+FileName+" <File not found>");
		return null;
	//#endif
	}

	//#ifdef com.mygdx.mongojocs.mr2.Debug
	Debug.println("loadImage: "+FileName);
	//#endif

	System.gc();
	return Img;
	//#endif
}
//#ifdef FILE_PACK
//#endif

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
/*
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
	textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
}
*/
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
	textDraw(textBreak(Str, printerSizeX, formFont), X, Y, RGB, Mode);
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
	textDraw(Str, X, Y, RGB, Mode, 0, Str.length);
}

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode, int ini, int size)
{
	Font f = formFont;
	scr.setFont(f);

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()*size) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  printerSizeY-(f.getHeight()*size) ;}

	X += printerX;
	Y += printerY;

	for (int pos=0, i=ini ; i<ini+size ; i++)
	{

	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX +=( printerSizeX-f.stringWidth(Str[i]) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX += printerSizeX-f.stringWidth(Str[i]) ;}


		int despY = pos++ * f.getHeight();


		if ((Mode & TEXT_OUTLINE)!=0)
		{
		putColor(0);
//#ifdef J2ME
		scr.drawString(Str[i],  X-1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY , 20);
		//rai malo
		scr.drawString(Str[i],  X+despX, Y+1+despY , 20);
		scr.drawString(Str[i],  X+despX, Y-1+despY , 20);
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

//	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }


	Draw();		// Imprimimos el juego


//	menuListDraw(scr);

	formDraw();

	popupDraw();

	listenerDraw();
	
	//#ifdef DOJA
	//#endif
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
					VolumeControl vc = (VolumeControl) soundTracks[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
					soundTracks[Ary].start();
				}
				catch(Exception e)
				{
				//#ifdef com.mygdx.mongojocs.mr2.Debug
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
				//#ifdef com.mygdx.mongojocs.mr2.Debug
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
				//#ifdef com.mygdx.mongojocs.mr2.Debug
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

public short[] convByte2Short(byte[] in)
{
	short[] out = new short[in.length / 2];
	
	for (int pos=0, i=0 ; i<out.length ; i++)
	{
		out[i] = (short) (((in[pos++]&0xFF)<<8) | (in[pos++]&0xFF));
	}

	return out;
}

// ----------------
// conv int[] 2 byte - Convertimos un int[] en byte[].
// ================

public byte[] convShort2Byte(short[] in) {
	byte[] out = new byte[in.length << 1];
	
	for(int i = 0, pos = 0; i < in.length; i++) {
		int data = in[i];
		
		out[pos++] = (byte) ((data >>  8) & 0xff);
		out[pos++] = (byte) ((data      ) & 0xff);
	}
	return out;
}

// <=- <=- <=- <=- <=-












// ****************
// ----------------
// ihd - Engine - ihd (iMode Hard Disk) File System v1.0 - Rev.2 (7.4.2005)
// ----------------
// Dise�ado y Programado por Juan Antonio Gomez Galvez.
// Para uso exclusivo del autor y autorizados por el mismo.
// ================
// ****************

//#ifdef DOJA


//#endif

// <=- <=- <=- <=- <=-


//#ifdef IHD_UNPACK
// <=- <=- <=- <=- <=-

//#endif


/*
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
*/









// *******************
// -------------------
// rms - Engine v2.2 - Rev.10 (30.5.2005)
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

private int rmsSectorSize = 1024; //1*1024; // DOJAPORT
private int rmsStoreSize;	// Size total HD
private int rmsStoreUsed;
private int rmsFileIndex;

static final int rmsCachedFiles = 8;

// -------------------
// rms Create
// ===================

public int rmsCreate(int store)
{
//#ifdef com.mygdx.mongojocs.mr2.Debug
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

	if (rmsSector != null)
	{
		rmsBoot = rmsLoadSegment(rmsBootIndex, 0x10);
	} else {
		rmsBoot = null;
	}
//#elifdef DOJA
//#endif

	if (rmsBoot == null || rmsBoot[0] != 'r' || rmsBoot[1] != 'm')
	{
	//#ifdef com.mygdx.mongojocs.mr2.Debug
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
						//#ifdef DebugConsole
							e.printStackTrace(); e.toString();
						//#endif
						}
					}
				}
			}
		}

		rmsSector = new byte[(store / rmsSectorSize)<<1];
		rmsSetFila(0, -1, rmsSector, false);		// Reservamos espacio para 'rmsSector'

		byte[] sectorData = new byte[rmsSectorSize];
	
		int maxSize = 0;
		int pack = 1;
		int filas = 0;
		int secPos = 0;
	
		while (maxSize < store)
		{
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

		rmsSetFila(0, 1, rmsSector, false);		// Guardamos 'rmsSector'

		maxSize -= rmsHDIndex;

	//#elifdef DOJA
	//#endif

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
	
	//#ifdef com.mygdx.mongojocs.mr2.Debug
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

//#ifdef com.mygdx.mongojocs.mr2.Debug
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
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("rmsSaveFile: "+fileName+" <rms disk full>");
	//#endif
		return true;
	}

	rmsDeleteFile(fileName);

//#ifdef com.mygdx.mongojocs.mr2.Debug
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
//#ifdef DOJA
//#endif


// -------------------
// rms Load File
// ===================

public byte[] rmsLoadFile(String fileName)
{
	int found = rmsFindFile(fileName);

	if (found == -1)
	{
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("rmsLoadFile: "+fileName+" <File not found>");
	//#endif
		return null;
	}

//#ifdef com.mygdx.mongojocs.mr2.Debug
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
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("rmsDeleteFile: "+fileName+" <File not found>");
	//#endif
		return true;
	}

//#ifdef com.mygdx.mongojocs.mr2.Debug
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
//#ifdef com.mygdx.mongojocs.mr2.Debug
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
	//#ifdef DebugConsole
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
	//#ifdef DebugConsole
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










// **************************************************************************//
// Final Clase Bios
// **************************************************************************//







// *******************
// -------------------
// menu - Engine
// ===================
// *******************

int[] cielosRGB = new int[] {0x00AAff,0x70C88E,0x00659E,0xCCEEff};

// -----------------------------------------------
// Variables staticas para identificar los estados de los menus...
// ===============================================
static final int MENU_MAIN = 0;
static final int MENU_SECOND = 1;
static final int MENU_SCROLL_HELP = 2;
static final int MENU_SCROLL_ABOUT = 3;
static final int MENU_CLASIFICATION = 4;
static final int MENU_OPONENT_LIST = 5;
static final int MENU_BOXES = 6;
static final int MENU_CIRCUITOS = 7;
static final int MENU_OPONENTES = 8;
static final int MENU_PRESENT = 9;
static final int MENU_RESUMEN = 10;
static final int MENU_WAIT = 11;
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
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;
static final int MENU_ACTION_CLASIFICATION = 8;
//static final int MENU_ACTION_SCORES = 9;
//static final int MENU_ACTION_BOXES = 10;
static final int MENU_ACTION_CIRCUITOS = 11;

static final int ACTION_OPONENTE_OK = 12;
static final int ACTION_OPONENTE_TU_OK = 13;
static final int ACTION_DOWN_OPONENT = 14;
static final int ACTION_FORM_NO = 15;
static final int ACTION_DOWN_OPONENT_OK = 16;

static final int ACTION_CLASIFICACION_NEXT = 17;
static final int ACTION_MENU_MAIN = 18;

static final int ACTION_CIRCUITO_CACHED = 19;
static final int ACTION_CIRCUITO_LIST = 20;
static final int ACTION_CIRCUITO_DOWNLOAD = 21;

static final int ACTION_OPONENTE_LIST = 22;

static final int ACTION_OPONENT_LIST_NEXT = 23;
static final int ACTION_OPONENT_LIST_DOWNLOAD = 24;
static final int ACTION_OPONENT_LIST_EXIT = 25;

static final int ACTION_CIRCUITOS_MENUBACK = 26;
static final int ACTION_OPONENTES_MENUBACK = 27;
static final int ACTION_OPONENT_LIST_MENUBACK = 28;

static final int ACTION_FORM_OK = 29;
static final int ACTION_RESUMEN_ACEPT = 30;
// ===============================================

int menuType;
int menuTypeBack;

boolean menuExit;

boolean menuResumPage = false;

boolean starsEnabled = false;

Image menuImg = null;
Image relojImg = null;

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuInit(type, null);
}

public void menuInit(int type, String str)
{
	//#ifdef NK-s40
	System.gc();
	//#endif
	menuTypeBack = menuType;
	menuType = type;

	menuExit = false;

	formListKeyBack = 0;

	formRelease();

	formSquareShow = true;



/*
	formInit(FORM_WAIT_CLOCK, false, false);
	listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	gameDraw();
*/


	formMarginWidth = formMarginTopHeight = 12;

	formListClear();




	switch (type)
	{
	case MENU_WAIT:

	// formTitle
		formTitleInit(str, BAND_MOVISTAR);

		formInit(FORM_WAIT_CLOCK, true, false, SOFTKEY_NONE, SOFTKEY_NONE);
	break;


	case MENU_MAIN:
		formMarginWidth = formMarginTopHeight = 0;
 		formListAdd(0, 0, gameText[TEXT_PLAY], MENU_ACTION_PLAY);
		formListAdd(0, 0, gameText[TEXT_CLASIFICATION], MENU_ACTION_CLASIFICATION);
	//#ifndef PLAYER_NONE
		formListAdd(0, 0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		formListAdd(0, 0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		formListAdd(0, 0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		formListAdd(0, 0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		formListAdd(0, 0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		formSquareShow = false;

		formInit(FORM_LIST_CICLE, false, false, SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_SECOND:

		formListClear();
		formListAdd(0, 0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
	//#ifndef PLAYER_NONE
		formListAdd(0, 0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		formListAdd(0, 0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
	//#ifndef NK-s40
		formListAdd(0, 0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
	//#endif
		formListAdd(0, 0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		formListAdd(0, 0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		formInit(FORM_LIST_CICLE, false, false, SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;


	case MENU_SCROLL_HELP:

	// formTitle
		formTitleInit(gameText[TEXT_HELP][0], BAND_AYUDA);	// Ayuda

		formListTextWidth = canvasWidth - formMarginWidth*2;

		formListHeight = formGetListHeight();
		formTextBlock(gameText[TEXT_HELP_SCROLL]);

		formInit(FORM_LIST_SCREEN, false, false, SOFTKEY_MENU, SOFTKEY_CONTINUE);
	break;


	case MENU_SCROLL_ABOUT:

	// formTitle
		formTitleInit(gameText[TEXT_ABOUT][0], BAND_ABOUT);	// Acerca de...

		formListTextWidth = formWidth - formMarginWidth*2;
		formListHeight = formGetListHeight();
		formTextBlock(gameText[TEXT_ABOUT_SCROLL]);

		formInit(FORM_LIST_SCREEN, false, false, SOFTKEY_MENU, SOFTKEY_CONTINUE);
	break;









	case MENU_CLASIFICATION:

	// formTitle
		formTitleInit(gameText[TEXT_CLASIFICATION][0], BAND_CLASIFICACION);

	// formList
		for (int i=0 ; i<pelaoClasifNick.length ; i++)
		{
			formListAdd(1, pelaoClasifPoints[i], new String[] {(clasifActual+i+1)+". ",pelaoClasifNick[i],""+pelaoClasifPoints[i]}, 0, 0);
		}


		if (pelaoClasificationIsEnd)
		{
			formActionNo = ACTION_MENU_MAIN;
			formActionOk = ACTION_NONE;

		//#ifdef S60
			starsEnabled = true;
		//#elifdef S80
		//#endif
			formInit(FORM_LIST_POINTS, false, true, SOFTKEY_MENU, SOFTKEY_NONE);
		} else {
			formActionNo = ACTION_MENU_MAIN;
			formActionOk = ACTION_CLASIFICACION_NEXT;

		//#ifdef S60
			starsEnabled = true;
		//#elifdef S80
		//#endif
			formInit(FORM_LIST_POINTS, false, true, SOFTKEY_MENU, SOFTKEY_CONTINUE);
		}
	break;





	case MENU_CIRCUITOS:

	// formTitle
		formTitleInit(gameText[TEXT_CIRCUITOS][0], BAND_CIRCUITOS);

	// formList
		if (!profileOk)
		{
			for (int i=0 ; i<cacheC ; i++)
			{
				int adr = cacheP[i];
				formListAdd(0x04|(cacheTrackWeather[adr]&0xff00), cacheTrackId[adr], new String[] {cacheTrackName[adr]}, ACTION_CIRCUITO_CACHED, 0);
			}

			formListAdd(0x00, 0, new String[] {gameText[TEXT_MAS_CIRCUITOS][0]}, ACTION_CIRCUITO_LIST, 0);
		} else {
			for (int i=0 ; i<trackName.length ; i++)
			{
				int adr = cacheTrackFind(trackId[i]);
				if ( adr >= 0)
				{
					formListAdd(0x04|(cacheTrackWeather[adr]&0xff00), trackId[i], new String[] {trackName[i]}, ACTION_CIRCUITO_CACHED, 0);
				} else {
					formListAdd(0x14|(trackWeather[i]&0xff00), trackId[i], new String[] {trackName[i]}, ACTION_CIRCUITO_DOWNLOAD, 0);
				}
			}
		}

		formListKeyBack = ACTION_CIRCUITOS_MENUBACK;

		formInit(FORM_LIST_SELECT, false, true, SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;





	case MENU_OPONENTES:

	// formTitle
		formTitleInit(gameText[TEXT_OPONENTE][0], BAND_OPONENTES);

	// formList
		int adr = cacheTrackFind(tmpTrackId);

		if (adr >= 0 && cacheGhostId[adr] >= 0)
		{
			formListAdd(0, 0, new String[] {cacheGhostName[adr]}, ACTION_OPONENTE_OK, 0);
		}

//		if (adr >= 0 && cachePlayerTime[adr] >= 0)
		{
			formListAdd(0, 0, new String[] {gameText[TEXT_CONTRA_TU_MEJOR_MARCA][0]}, ACTION_OPONENTE_TU_OK, 0);
		}

		formListAdd(0, 0, new String[] {gameText[TEXT_DESCARGAR_OPONENTE][0]}, ACTION_OPONENTE_LIST, 0);

		formInit(FORM_LIST_SELECT, false, true, SOFTKEY_BACK, SOFTKEY_ACEPT);

		formListKeyBack = ACTION_OPONENTES_MENUBACK;
	break;




	case MENU_OPONENT_LIST:

	// formTitle
		formTitleInit(gameText[TEXT_OPONENTES][0], BAND_OPONENTES);

	// formList
		if (ghostListNick.length == 0)
		{
			formListAdd(0, 0,  new String[] {gameText[TEXT_NO_HAY_OPONENTES][0]}, ACTION_OPONENT_LIST_EXIT, 0);
		} else {

			for (int i=0 ; i<ghostListNick.length ; i++)
			{
				formListAdd(1, ghostListId[i], new String[] {(i+1)+".", ghostListNick[i], getStrTime(ghostListTime[i])}, ACTION_OPONENT_LIST_DOWNLOAD, 0);
			}
	
			if (!ghostListIsEnd)
			{
				formListAdd(0, 0,  new String[] {gameText[TEXT_SIGIENTE_PAGINA][0]}, ACTION_OPONENT_LIST_NEXT, 0);
			}
		}


		formListKeyBack = ACTION_OPONENT_LIST_MENUBACK;

		starsEnabled = false;
		formInit(FORM_LIST_SELECT, false, true, SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;





	case MENU_PRESENT:

	// formTitle
		formTitleInit(tmpTrackName, tmpTrackWeather>>8&0xff );

	// formImgScroll
		horizonteImg = horizonImg = null;
		System.gc();
		//#ifndef NO_HORIZON
		//#ifdef NK-s40
		//#else
		horizonteImg = loadImage("/horizonte"+(tmpTrackWeather&0xff));
		//#endif
		//#endif
		
		formImgScrollInit( cielosRGB[tmpTrackWeather>>16&0xff], horizonteImg);

		adr = cacheTrackFind(tmpTrackId);
		// DOJAPORT
		/*
		pelaoTime = cachePlayerTime[adr]; if (pelaoTime <= 0) {pelaoTime= 99 * 60000 + 59 * 1000 + 999;}
		bestTime = 99 * 60000 + 59 * 1000 + 999;//pelaoTime;
		*/
		pelaoTime = cachePlayerTime[adr]; if (pelaoTime <= 0) {pelaoTime= 5999999;}
		bestTime = 5999999;//pelaoTime;

	// formText
		if (gameContraOponente)
		{
			formTextInit(new String[] {gameText[TEXT_OPONENTE][0]+":"}, 0);
		} else {
			formTextInit(new String[] {gameText[TEXT_MEJOR_MARCA][0]}, 0);
		}

	// formList
		if (gameContraOponente)
		{
			formListAdd(2, 0, new String[] {tmpGhostNick,getStrTime(tmpGhostTime)}, 0, 0);
		} else {
			formListAdd(2, 0, new String[] {pelaoNick, getStrTime(pelaoTime)}, 0, 0);
		}

		formActionOk = ACTION_FORM_OK;
		formActionNo = ACTION_FORM_NO;

		formInit(FORM_LIST_POINTS, false, false, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
	break;



	case MENU_RESUMEN:

	// formTitle
		formTitleInit(tmpTrackName, tmpTrackWeather>>8&0xff );


//#ifdef TWO_SCR_REVIEW
//#endif

	// formText
		if (gameContraOponente)
		{
			if (pelaoTimeNew < tmpGhostTime)
			{
				formTextInit(new String[] {gameText[TEXT_HAS_VENCIDO_OPONENTE][0]}, TEXT_HCENTER|TEXT_VCENTER);
			} else {
				formTextInit(new String[] {gameText[TEXT_HAS_PERDIDO_OPONENTE][0]}, TEXT_HCENTER|TEXT_VCENTER);
			}
		} else {
			if (pelaoTimeNew < pelaoTime)
			{
				formTextInit(new String[] {gameText[TEXT_HAS_MEJORADO_TU_MARCA][0]}, TEXT_HCENTER|TEXT_VCENTER);
			} else {
				formTextInit(new String[] {gameText[TEXT_NO_HAS_MEJORADO_TU_MARCA][0]}, TEXT_HCENTER|TEXT_VCENTER);
			}
		}

//#ifdef TWO_SCR_REVIEW
//#endif

	// formList
		formListAdd(3, 0, new String[] {gameText[TEXT_MEJOR_VUELTA][0]}, 0, 0);
		if (gameContraOponente)
		{
			formListAdd(2, 0, new String[] {pelaoNick, getStrTime(pelaoTimeNew)}, 0, 0);
			formListAdd(2, 0, new String[] {tmpGhostNick, getStrTime(tmpGhostTime)}, 0, 0);
		} else {
			formListAdd(2, 0, new String[] {gameText[TEXT_NUEVA_MARCA][0],getStrTime(pelaoTimeNew)}, 0, 0);
			formListAdd(2, 0, new String[] {gameText[TEXT_ANTERIOR_MARCA][0],getStrTime(pelaoTime)}, 0, 0);
		}

//#ifdef TWO_SCR_REVIEW
//#endif

		formActionNo = ACTION_NONE;
		formActionOk = ACTION_RESUMEN_ACEPT;

		formInit(FORM_LIST_POINTS, true, true, SOFTKEY_NONE, SOFTKEY_ACEPT);

	break;

	}


	formShow = true;


	biosStatus = BIOS_MENU;
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{
	switch (cmd)
	{

	case -3: // Scroll o Screen ha sido cortado por usuario
	case -2: // Scroll o Screen ha llegado al final
		menuInit(menuTypeBack);
	break;


	case MENU_ACTION_PLAY:		// Jugar de 0

//		menuInit(MENU_CIRCUITOS);
		gameStatus = GAME_CIRCUITOS;
		menuExit = true;
	break;

	case MENU_ACTION_CONTINUE:	// Continuar
		/*synchronized(tDifMutex) {
			if(menuStart != -1) {
				//timeDiff2 += System.currentTimeMillis() - menuStart;
				timeDiff  += System.currentTimeMillis() - menuStart;
				menuStart = -1;
			}
		}
		*/
		menuStart = -1;
		//#ifdef BIG_PANEL
		//#endif
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_HELP:		// Mostramos Instrucciones...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// Mostramos About...

		menuInit(MENU_SCROLL_ABOUT);
	break;



	case MENU_ACTION_CLASIFICATION:

//		menuInit(MENU_CLASIFICATION);
		gameStatus = GAME_CLASIFICATION;
		menuExit = true;
	break;

	case ACTION_CLASIFICACION_NEXT:

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;


/*
	case MENU_ACTION_SCORES:

		menuInit(MENU_OPONENT_LIST);
	break;
*/
/*
	case MENU_ACTION_BOXES:

		menuInit(MENU_BOXES);
	break;
*/








	case ACTION_CIRCUITOS_MENUBACK:

		gameStatus = GAME_MENU_MAIN;
		menuExit = true;
	break;



	case ACTION_OPONENTES_MENUBACK:

		gameStatus = GAME_CIRCUITOS;
		menuExit = true;
	break;



	case ACTION_OPONENT_LIST_MENUBACK:

		gameStatus = GAME_OPONENTES;
		menuExit = true;
	break;

	case ACTION_OPONENT_LIST_NEXT:

		ghostListFirst += ghostListMax;

		downloadReturnOk = GAME_OPONENT_LIST;
		downloadReturnCancel = GAME_OPONENT_LIST;
		downloadType = DOWNLOAD_GHOST_LIST;
		gameStatus = GAME_PILLA_PERFIL;
		menuExit = true;
	break;



	case ACTION_OPONENT_LIST_DOWNLOAD:

		gameContraOponente = true;

		tmpGhostId = formListId();
		downloadReturnOk = GAME_OPONENT_LIST+1;
		downloadReturnCancel = GAME_OPONENTES;
		downloadType = DOWNLOAD_GHOST;
		gameStatus = GAME_PILLA_PERFIL;

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;


	case ACTION_OPONENT_LIST_EXIT:

		menuInit(MENU_OPONENTES);
	break;








	case MENU_ACTION_RESTART:	// Provocamos GAME OVER desde menu

		popupInit(gameText[TEXT_ABANDONAR_CARRERA][0],  POPUP_ASK, POPUP_ACTION_NO, POPUP_ACTION_PLAY_EXIT, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
	break;






	case MENU_ACTION_EXIT_GAME:	// Exit Game

//		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
//		gameExit = true;

		popupInit(gameText[TEXT_PREGUNTA_SALIR][0],  POPUP_ASK, POPUP_ACTION_NO, POPUP_ACTION_GAME_EXIT,  SOFTKEY_CANCEL, SOFTKEY_ACEPT);
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = formListOpt() != 0;
		if(!gameSound) {
			soundStop();
		} else {
			if(menuType == MENU_MAIN) {
				soundPlay(SOUND_MENU,0);
			} else {
				soundPlay(SOUND_FINISH,1);
			}
		}
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = formListOpt() != 0;
		if (gameVibra) {vibraInit(300);}
	break;


// ---------------------------
// ACTIONS MENU CIRCUITOS
// ===========================

// Cargamos un circuito que se encuantra en JAR o RMS

	case ACTION_CIRCUITO_CACHED:

		tmpTrackId = formListId();
		int adr = cacheTrackFind(tmpTrackId);
		tmpTrackName = cacheTrackName[adr];
		tmpTrackWeather = cacheTrackWeather[adr];
		tmpTrackData = loadFile("/t"+tmpTrackId+".trk");
		
		if (tmpTrackData==null)
		{
			tmpTrackData = rmsLoadFile("t"+tmpTrackId+".trk");
		}

		gameStatus = GAME_OPONENTES;

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;


// Descargamos un circuito de internet y lo almacenamos en el RMS

	case ACTION_CIRCUITO_DOWNLOAD:

		tmpTrackId = formListId();

		downloadReturnOk = GAME_CIRCUITOS+1;
		downloadReturnCancel = GAME_CIRCUITOS;
		downloadType = DOWNLOAD_TRACK;
		gameStatus = GAME_PILLA_PERFIL;

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;


// Solicitamos el listado completo de circuitos al servidor

	case ACTION_CIRCUITO_LIST:

		downloadReturnOk = GAME_CIRCUITOS;
		downloadReturnCancel = GAME_CIRCUITOS;
		downloadType = DOWNLOAD_PROFILE;
		gameStatus = GAME_PILLA_PERFIL;

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;

// ===========================






// ---------------------------
// ACTIONS MENU OPONENTE
// ===========================

// Seleccionamos un oponente que YA se encuantra en el JAR o RMS

	case ACTION_OPONENTE_OK:

		gameContraOponente = true;

		adr = cacheTrackFind(tmpTrackId);
		tmpGhostId = cacheGhostId[adr];
		tmpGhostNick = cacheGhostName[adr];
		tmpGhostTime = cacheGhostTime[adr];
		
		//hack de rai
		// DOJAPORT
		//if(tmpGhostTime < 2000) tmpGhostTime = 99 * 60000 + 59 * 1000 + 999;
		if(tmpGhostTime < 2000) tmpGhostTime = 5999999;
		
		//hack de rai
		
		//#ifndef FILE_PACK
		tmpGhostData = loadFile("/t"+tmpTrackId+".gho");
		//#else
		//#endif
		if (tmpGhostData==null)
		{
			tmpGhostData = rmsLoadFile("t"+tmpTrackId+".gho");
		}

		gameStatus = GAME_PRESENT;
		menuExit = true;
	break;


// Seleccionamos jugar contra tu mejor marca

	case ACTION_OPONENTE_TU_OK:

		gameContraOponente = false;

		adr = cacheTrackFind(tmpTrackId);
		if (adr >= 0 && cachePlayerTime[adr] >= 0)
		{
			tmpGhostId = 0;
			tmpGhostNick = pelaoNick;
			tmpGhostTime = cachePlayerTime[adr];
			tmpGhostData = rmsLoadFile("t"+tmpTrackId+".ply");
		} else {
			tmpGhostData = null;
		}

		gameStatus = GAME_PRESENT;
		menuExit = true;
	break;


// Solicitamos el listado completo de ghosts al servidor

	case ACTION_OPONENTE_LIST:

		ghostListFirst = 0;

		downloadReturnOk = GAME_OPONENT_LIST;
		downloadReturnCancel = GAME_OPONENTES;
		downloadType = DOWNLOAD_GHOST_LIST;
		gameStatus = GAME_PILLA_PERFIL;

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;

// ===========================







	case ACTION_FORM_NO:

		formAskAcept = false;
		menuExit = true;
	break;

	case ACTION_FORM_OK:

		formAskAcept = menuExit = true;
	break;




	case ACTION_MENU_MAIN:

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		gameStatus = GAME_MENU_MAIN;
		menuExit = true;
	break;


	case ACTION_RESUMEN_ACEPT:

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

	//#ifdef TWO_SCR_REVIEW
	//#endif

		menuExit = true;
	break;


	}

}

// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if ( formTick( (keyY!=lastKeyY? keyY:(keyX!=lastKeyX? keyX:0) ), keyMenu!=lastKeyMenu? keyMenu:0 ) )
	{
		menuAction(formListCMD);
	}

	if (menuType == MENU_WAIT) {gameTick();}

	if (menuExit) {return true;}

	return false;
}

// <=- <=- <=- <=- <=-















// *******************
// -------------------
// form - Engine
// ===================
// *******************

/*
static final int GREEN_BORDER = 0x000052;
static final int BLACK = 0x000052;
static final int BLUE  = 0xFFFFFF;
static final int GREEN = 0xFFFFFF;
static final int WHITE = 0xFFFFFF;
static final int CELESTE = 0x298CFF;
static final int BLACK = 0x000052;
*/

static final int BACKGROUND  = 0x000000;

static final int TOPBAR_BACKGROUND = 0x000052;
static final int TOPBAR_FONT = 0xFFFFFF;

static final int SELECT_BACKGROUND = 0xFFFFFF;
static final int SELECT_FONT_SELECTED = 0x003984;
static final int SELECT_FONT_OK = 0xFFFFFF;
static final int SELECT_FONT_BAD = 0x949494;

static final int BASIC_FONT = 0xFFFFFF;
static final int BASIC_FONT2 = 0x298CFF;


static final int formSquareWidth = 32;
static final int formSquareHeight = 32;

static final int formStarWidth = 11;
static final int formStarHeight = 11;


int formActionOk;
int formActionNo;


int formWidth;
int formHeight;


int formSquareX;
int formSquareY;


int formBreakHeight;
int formTextAddY;
int formStarAddY;
int formBandAddY;

boolean formAskAcept;

Font formFont;		// Font a usar para el formulario
int formFontHeight;


byte[] menuItemsCor;
//#ifdef J2ME
Image menuItemsImg;
//#elifdef DOJA
//#endif


static final int BAND_CIRCUITOS = 15;
static final int BAND_OPONENTES = 16;
static final int BAND_AYUDA = 17;
static final int BAND_ABOUT = 18;
static final int BAND_MOVISTAR = 19;
static final int BAND_CLASIFICACION = 20;

byte[] banderasCor;
//#ifdef J2ME
Image banderasImg;
//#elifdef DOJA
//#endif


// formSquare
boolean formSquareShow;




// formMargin
int formMarginWidth;		// Ancho para usar como margenes
int formMarginTopHeight;	// size Y del borde negro superior

// formTitle
String formTitleStr;	// Texto a mostar como titulo (zona verde)
int formTitleBandera;	// numero de sprite para la bandera
int formTitleY;
int formTitleHeight;
int formTitleAddX;

// formText
String[] formTextStr;	// Texto estatico
int formTextY;
int formTextHeight;
int formTextAdjust;

// formImgScroll
int formImgScrollRGB;
Image formImgScrollImg;
int formImgScrollX;
int formImgScrollY;
int formImgScrollWidth;
int formImgScrollHeight;

// formImage
Image formImageImg;
int formImageY;
int formImageHeight;

// formList
String[][] formListStr;	// Textos para lista de seleccion
int[][] formListDat;		// Datos sobre textos de la lista de seleccion

int formListIni;
int formListPos;
int formListSize;
int formListView;

int formListCMD;
int formListType;
int formListY;
int formListHeight;

static final int FORM_LIST_POINTS = 0;
static final int FORM_LIST_SELECT = 1;
static final int FORM_LIST_CICLE = 2;
static final int FORM_LIST_SCREEN = 3;
static final int FORM_WAIT_KEYBOARD = 4;
static final int FORM_WAIT_CLOCK = 5;

int formListPosWidth;
int formListTextWidth;
int formListPuntWidth;
int formListStarWidth;

int	formListTabIndexPos;
int formListTabIndexSize;
short[] formListTabIndex;

int formListAspaHeight;

boolean formShow;

int formListKeyBack;


// -------------------
// form Create
// ===================

public void formCreate()
{
	formWidth = canvasWidth;
	formHeight = canvasHeight;

	//#ifndef FILE_PACK
	menuItemsCor = loadFile("/menuitems.cor");
	banderasCor = loadFile("/banderas.cor");
	//#else
	//#endif
	
//#ifdef J2ME
	//#ifndef FILE_PACK
	menuItemsImg = loadImage("/menuitems");
	//#else
	//#endif
//#elifdef DOJA
//#endif
	
//#ifdef J2ME
	//#ifndef FILE_PACK
	banderasImg = loadImage("/banderas");
	//#else
	//#endif
//#elifdef DOJA
//#endif

//#ifdef J2ME
	//#ifdef FONT_MEDIUM_FORCED
	formFont = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	//#else
	//#endif
	
//#elifdef DOJA
//#endif
	formFontHeight = formFont.getHeight();


	int formBandHeight = banderasCor[3];


	formBreakHeight = (formStarHeight > formFontHeight)? formStarHeight:formFontHeight;
	formBreakHeight = (formBreakHeight > formBandHeight)? formBreakHeight:formBandHeight;



	formStarAddY = (formBreakHeight - formStarHeight)/2;
	formTextAddY = (formBreakHeight - formFontHeight)/2;
	formBandAddY = (formBreakHeight - formBandHeight)/2;
}

// -------------------
// form Destroy
// ===================

public void formDestroy()
{
	menuItemsCor = null; 
	menuItemsImg = null;
}




// -------------------
// form Init
// ===================

public void formInit(int type, boolean textCenter, boolean listCenter, int softLeft,int softRight)
{
	listenerInit(softLeft, softRight);

	formListType = type;

	if (type == FORM_WAIT_CLOCK)
	{
		formShow = true;
		return;
	}


	formWidth = canvasWidth - (formMarginWidth*2);


// formList
	if (formListDat != null) {formListInit();}

// formText
	if (formTextStr != null)
	{
		formTextStr = textBreak(formTextStr, formWidth, formFont);
		formTextHeight = formTextStr.length * formFontHeight;
	}


// Calculamos coordenadas Y de cada elemento:
	formCalcYs(0,0);


	if (type != FORM_LIST_SCREEN)
	{
		formListHeight = formListStr!=null?(formListStr.length * formBreakHeight):0;
	
		int sobraHeight = formHeight - (formListY + formListHeight);


		if (sobraHeight < 0)
		{
			formMarginTopHeight = 0;
			formCalcYs(0,0);
			sobraHeight = formHeight - (formListY + formListHeight);

			if (sobraHeight < 0 && formImgScrollHeight > 0)
			{
				formImgScrollHeight += sobraHeight;
				formCalcYs(0,0);
				sobraHeight = formHeight - (formListY + formListHeight);
			}
		}

	
		if (sobraHeight > 3)
		{
			if (textCenter && listCenter)
			{
				formCalcYs(sobraHeight/3, sobraHeight/3);
			} else
			if (textCenter)
			{
				formCalcYs(sobraHeight/2, 0);
			} else
			if (listCenter)
			{
				formCalcYs(0, sobraHeight/2);
			}
		}



		if (formListStr != null)
		{
			formListAspaHeight = 0;
			formListView = ((formHeight - formListY) / formBreakHeight);
			if (formListView < formListStr.length) {formListView--; formListAspaHeight = formBreakHeight / 2;}
			else
			if (formListView > formListStr.length) {formListView = formListStr.length;}
	
			formListHeight = (formListView * formBreakHeight) + (formListAspaHeight * 2);
		}
	}

}


public void formCalcYs(int textAvance, int listAvance)
{
	formTitleY = formMarginTopHeight;
	formImgScrollY = formTitleY + formTitleHeight;
	formTextY = formImgScrollY + formImgScrollHeight; if (formImgScrollImg != null) {formTextY += formMarginTopHeight;}
	formTextY += textAvance;
	formImageY = formTextY + formTextHeight;
	formListY = formImageY + formImageHeight;
	formListY += listAvance;
}

/*
public void formMarginInit(int width, int topHeight)
{
	formMarginWidth = width;
	formMarginTopHeight = topHeight;
}
*/

public void formTitleInit(String str, int bandera)
{
	formTitleStr = str;
	formTitleHeight = formFontHeight + 2;
	formTitleAddX = 0;

	formTitleBandera = bandera;

	if (bandera >= 0)
	{
		formTitleAddX = banderasCor[(bandera*6)] + banderasCor[(bandera*6)+2] + 4;

		if (banderasCor[(bandera*6)+3] > formTitleHeight)
		{
			formTitleHeight = banderasCor[(bandera*6)+3] + 4;
		}
	}
}

public void formTextInit(String[] str, int adjust)
{
	formTextStr = str;
	formTextAdjust = adjust;
}

public void formImageInit(Image img)
{
	formImageImg = img;
	formImageHeight = img.getHeight();
}

public void formImgScrollInit(int rgb, Image img)
{
	formImgScrollX = 0;
	formImgScrollRGB = rgb;
	formImgScrollImg = img;
	//#ifdef NO_HORIZON
	//#else
	formImgScrollWidth = img.getWidth();
	formImgScrollHeight = img.getHeight();
	//#endif
}

public void formListInit()
{
	int width = 0;
	formListPosWidth = formListTextWidth = formListPuntWidth = formListStarWidth = 0;

	for (int i=0 ; i<formListStr.length ; i++)
	{

		switch (formListDat[i][0] & 0x000F)
		{
		case 0:		// Texto
			width = formFont.stringWidth(formListStr[i][0]);
			if (formListTextWidth < width) {formListTextWidth = width;}
		break;

		case 1:		// Posicion + Texto + Puntos + Estrellas
			width = formFont.stringWidth(formListStr[i][0]);
			if (formListPosWidth < width) {formListPosWidth = width;}

			width = formFont.stringWidth(formListStr[i][1]);
			if (formListTextWidth < width) {formListTextWidth = width;}

			width = formFont.stringWidth(formListStr[i][2]);
			if (formListPuntWidth < width) {formListPuntWidth = width;}

			if (starsEnabled)
			{
				formListStarWidth = (formStarWidth * 3);
			} else {
				formListStarWidth = 0;
			}
		break;

		case 4:		// Bandera + Texto
			formListPosWidth = banderasCor[2] + 4;		// Pillo ancho de una bandera + 4

			width = formFont.stringWidth(formListStr[i][0]);
			if (formListTextWidth < width) {formListTextWidth = width;}
		break;
		}
	}

	int total = formListPosWidth + formListTextWidth + formListPuntWidth + formListStarWidth;

	if (total > formWidth)
	{
		if (canvasWidth > total)
		{
			formMarginWidth = (canvasWidth - total) / 2;
			formWidth = canvasWidth - (formMarginWidth * 2);
		} else {
			int sobra = canvasWidth - (formListPosWidth + formListPuntWidth + formListStarWidth);
			formListTextWidth = 0;

			for (int i=0 ; i<formListStr.length ; i++)
			{
				if ((formListDat[i][0] & 0x000F) == 1)
				{
					formListStr[i][1] = textCut(formListStr[i][1], sobra, formFont);

					width = formFont.stringWidth(formListStr[i][1]);
					if (formListTextWidth < width) {formListTextWidth = width;}
				}
			}

			formMarginWidth = 0;
			formWidth = canvasWidth;
		}
	}

	formListTextWidth = formWidth - formListPosWidth - formListPuntWidth - formListStarWidth;
}



public int formGetListHeight()
{
	return formHeight - formMarginTopHeight - formTitleHeight - formTextHeight - formImageHeight;
}





public void formTextBlock(String[] strIn)
{
	String[] strTemp = textBreak(strIn, formListTextWidth, formFont);

	formListStr = new String[strTemp.length][1];
	for (int i=0 ; i<strTemp.length ; i++) {formListStr[i][0] = strTemp[i];}

	formListTabIndexPos = formListTabIndexSize = 0;

	formListTabIndex = new short[512];

	int pos = 0;
	int size = 0;
	int ultSpc = 0;
	int act = 0;

	boolean skipSpc = true;

	while (act<formListStr.length)
	{
		if (skipSpc)
		{
			if (formListStr[act][0] == " ")
			{
				act++;
			} else {
				pos = act;
				size = 0;
				ultSpc = -1;
				skipSpc = false;
			}

		} else {

			if (formListStr[act][0] == " ")
			{
				ultSpc = act;
			}

			boolean finalCampo = false;
	
			if ( (size += formBreakHeight) > formListHeight ) {act--; finalCampo = true;}
	
			if ( act == formListStr.length-1 ) {ultSpc = -1; finalCampo = true;}
	
			if ( finalCampo )
			{
				if (ultSpc != -1) {act = ultSpc - 1;}
	
				formListTabIndex[(formListTabIndexSize++)] = (short)(pos);
				formListTabIndex[(formListTabIndexSize++)] = (short)(act-pos+1);
	
				skipSpc = true;
			}

			act++;
		}
	}
}






// -------------------
// formList Clear
// ===================

public void formListClear()
{
	formListStr = null; 
	formListTabIndex = null;
	
	formListPos	=	0;
	
	formListDat = null; 
}


// -------------------
// formList Add
// ===================

public void formListAdd(int render, int id, String[] text, int cmd)
{
	formListAdd(render, id, text, cmd, 0);
}

public void formListAdd(int render, int id, String[] text, int cmd, int index)
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

	str[i] = text;
	dat[i] = new int[] {render, cmd, index, id};

	formListStr = str;
	formListDat = dat;
}






// -------------------
// form Release
// ===================

public void formRelease()
{
// formMargin
	formMarginWidth = formMarginTopHeight = 0;

// formTitle
	formTitleStr = null;
	formTitleHeight = formImgScrollHeight = formTextHeight = formImageHeight = formListHeight = 0;

// formImgScroll
	formImgScrollImg = formImgScrollImg = formImageImg = null;


// formText
	formTextStr = null;

// formImage

// formList
	formListDat = null;
	formListStr = null;
}

// -------------------
// form Tick
// ===================

public boolean formTick(int movY, int key)
{
	boolean back = key==-1;
	boolean fire = key>0;


	switch (formListType)
	{
	case FORM_LIST_POINTS:

		if (formListAspaHeight > 0 && movY != 0)
		{
			formListPos += movY; formShow = true;

			if (formListPos < 0) {formListPos = 0;}
			else
			if (formListPos > formListStr.length-formListView) {formListPos = formListStr.length-formListView;}
			formListIni = formListPos;
		}

		if (back) {formListCMD = formActionNo; return true;}
		else
		if (fire) {formListCMD = formActionOk; return true;}
	break;


	case FORM_LIST_CICLE:
	case FORM_LIST_SELECT:

		if (back && formListKeyBack!=0) {formListCMD = formListKeyBack; return true;}

		if (movY != 0)
		{
			formListPos += movY; formShow = true;

			if (formListPos < 0) {formListPos = formListStr.length-1;}
			else
			if (formListPos >= formListStr.length) {formListPos = 0;}
		}


		if (fire)
		{
			formShow = true;
			if (++formListDat[formListPos][2] == formListStr[formListPos].length) {formListDat[formListPos][2]=0;}
			formListCMD = formListDat[formListPos][1];
			return true;
		}
	break;


	case FORM_LIST_SCREEN:

		if (back) {formListCMD = -2; return true;}

		if (movY != 0)
		{
			int newPos = formListTabIndexPos + (movY * 2);

			if (newPos >= 0 && newPos < formListTabIndexSize)
			{
				formListTabIndexPos = newPos;
				formShow=true;
			}
		}
		else if (fire)
		{
			formListTabIndexPos += 2;

			if (formListTabIndexPos >= formListTabIndexSize) {formListCMD=-2; return true;}

			formShow=true;
		}
	break;

	case FORM_WAIT_KEYBOARD:

		if (back) {formListCMD = formActionNo; return true;}
		else
		if (fire) {formListCMD = formActionOk; return true;}
	break;
	}




	if (menuType == MENU_PRESENT)
	{
		formImgScrollX--; if (formImgScrollX < -formImgScrollWidth) {formImgScrollX += formImgScrollWidth;}
		formShow = true;
	}




//	formShow = true;

	return false;
}

// -------------------
// form Draw
// ===================

public void formDraw()
{
	if (!formShow) {return;}

	formShow = false;

//#ifdef J2ME
	scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif

	scr.setFont(formFont);


// Fondo
	putColor(BACKGROUND);
	scr.fillRect(0, 0, canvasWidth, canvasHeight);

	if (menuImg == null || formSquareShow)
	{
//		if (--formSquareX <= -formSquareWidth) {formSquareX=0;}
//		if (--formSquareY <= -formSquareHeight) {formSquareY=0;}

		int partsX = (canvasWidth / formSquareWidth)+1;
		int partsY = (canvasHeight / formSquareHeight)+1;

		for (int y=0 ; y<partsY ; y++)
		{
			for (int x=0 ; x<partsX ; x++)
			{
				spriteDraw(menuItemsImg, formSquareX+(x*formSquareWidth), formSquareY+(y*formSquareHeight), 0, menuItemsCor);
			}
		}
	} else {
		showImage(menuImg);
	}
	

//#ifdef J2ME
	scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif



// formMarginTopDraw
	if (formMarginTopHeight > 0)
	{
		putColor(BACKGROUND);
		scr.fillRect(0, 0, canvasWidth, formMarginTopHeight);
	}



// formTitleDraw
	if (formTitleStr != null)
	{
		putColor(TOPBAR_BACKGROUND);
		scr.fillRect(0, formTitleY, canvasWidth, formTitleHeight);

		putColor(TOPBAR_FONT);
		formTextDraw(formTitleStr, 0, 0, TEXT_VCENTER, formMarginWidth+formTitleAddX, formTitleY, formWidth, formTitleHeight);

		if (formTitleBandera >= 0)
		{
			renderSpriteDraw(banderasImg, formMarginWidth, 0, formTitleBandera, banderasCor, formMarginWidth, formTitleY, canvasWidth, formTitleHeight);
		}
	}



// formImgScrollDraw
	if (formImgScrollImg != null)
	{
		fillDraw(formImgScrollRGB, 0, formImgScrollY, canvasWidth, formImgScrollHeight);

		showImage(formImgScrollImg, 0,0, formImgScrollWidth, formImgScrollHeight, formImgScrollX, formImgScrollY);

		if (formImgScrollX+formImgScrollWidth < canvasWidth)
		{
			showImage(formImgScrollImg, 0,0, formImgScrollWidth, formImgScrollHeight, formImgScrollX+formImgScrollWidth, formImgScrollY);
		}

		fillDraw(BACKGROUND, 0, formImgScrollY+formImgScrollHeight, canvasWidth, formMarginTopHeight);
	}


//#ifdef J2ME
	scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif


// formTextDraw
	if (formTextStr != null)
	{
		putColor(BASIC_FONT);
		formTextDraw(formTextStr, 0, 0, formTextAdjust, formMarginWidth, formTextY, formWidth, formTextHeight);
	}


// formImageDraw
	if (formImageImg != null)
	{
	//#ifdef J2ME
		scr.drawImage(formImageImg, (canvasWidth - formImageImg.getWidth())/2, formImageY, 20);
	//#elifdef DOJA
	//#endif
	}




// formListDraw
	if (formListType == FORM_WAIT_CLOCK || formListStr != null)
	{

//	scr.drawRect(0, formListY, canvasWidth, formListHeight);	// Cuadrado de pruebas

		switch (formListType)
		{
		case FORM_LIST_POINTS:
		case FORM_LIST_SELECT:

			if (formListPos-formListIni >= formListView) {formListIni = formListPos - formListView +1;}
			else
			if (formListPos < formListIni) {formListIni = formListPos;}

		// Dibujamos Aspa Superior si la hay
			if (formListAspaHeight > 0)
			{
				if (formListIni > 0)
				{
					for (int i=1 ; i<formListAspaHeight-2 ; i++)
					{
						int size = (i)*2;
						fillDraw(SELECT_FONT_OK, (canvasWidth-size)/2, formListY+i,  size, 1);
					}
				}
			}

			int actY = formListY + formListAspaHeight;

			int x = formMarginWidth;
			int y = actY + (formListPos-formListIni) * formBreakHeight;
			int width = formWidth;
			int height = formBreakHeight -1;


			for (int i=0 ; i<formListView ; i++)
			{
				int act = i+formListIni;

			//#ifdef J2ME
				scr.setClip(0, 0, canvasWidth, canvasHeight);
			//#endif
	
				boolean selectedLine = false;
				if (formListType == FORM_LIST_SELECT && formListPos == act)
				{
					selectedLine = true;
					putColor(SELECT_BACKGROUND);
					scr.fillRect(x, y-1, width, height+2);
					scr.fillRect(x-1, y, width+2, height);
				}


				putColor(SELECT_FONT_OK);
				int actX = formMarginWidth;
	

				switch (formListDat[act][0] & 0x0F)
				{
				case 0:
					if (selectedLine) {putColor(SELECT_FONT_SELECTED);}
					if ((formListDat[act][0] & 0x10) != 0) {putColor(BASIC_FONT2);}
					formTextDraw(formListStr[act][0], 0, formTextAddY, 0, actX,actY, formWidth, formBreakHeight);
				break;
				
				case 1:
					putColor(SELECT_FONT_BAD);
					if (selectedLine) {putColor(SELECT_FONT_SELECTED);}
					formTextDraw(formListStr[act][0], 0, formTextAddY, TEXT_RIGHT, actX,actY, formListPosWidth, formBreakHeight);
					actX += formListPosWidth;

					putColor(SELECT_FONT_OK);
					if (selectedLine) {putColor(SELECT_FONT_SELECTED);}
					formTextDraw(formListStr[act][1], 0, formTextAddY, 0,          actX,actY, formListTextWidth, formBreakHeight);
					actX += formListTextWidth;
		
					putColor(BASIC_FONT2);
					if (selectedLine) {putColor(SELECT_FONT_SELECTED);}
					formTextDraw(formListStr[act][2], 0, formTextAddY, TEXT_RIGHT, actX,actY, formListPuntWidth, formBreakHeight);
					actX += formListPuntWidth;

					if (starsEnabled)
					{
						int basePoints = (scoreMaxPoints>>2); if (basePoints == 0) {basePoints=1;}
						spriteDraw(menuItemsImg, actX, formStarAddY+actY, (formListDat[act][3]/basePoints>=1?1:2), menuItemsCor);
						actX += formStarWidth;

						spriteDraw(menuItemsImg, actX, formStarAddY+actY, (formListDat[act][3]/basePoints>=2?1:2), menuItemsCor);
						actX += formStarWidth;

						spriteDraw(menuItemsImg, actX, formStarAddY+actY, (formListDat[act][3]/basePoints>=3?1:2), menuItemsCor);
					}
				break;

				case 2:
					if (selectedLine) {putColor(SELECT_FONT_SELECTED);}
					formTextDraw(formListStr[act][0], 0, formTextAddY, 0, actX,actY, formWidth, formBreakHeight);
					putColor(BASIC_FONT2);
					formTextDraw(formListStr[act][1], 0, formTextAddY, TEXT_RIGHT, actX,actY, formWidth, formBreakHeight);
				break;

				case 3:
					if ((formListDat[act][0] & 0x10) != 0) {putColor(BASIC_FONT2);}
					if (selectedLine) {putColor(SELECT_FONT_SELECTED);}
					formTextDraw(formListStr[act][0], 0, formTextAddY, TEXT_HCENTER, actX,actY, formWidth, formBreakHeight);
				break;

				case 4:
					spriteDraw(banderasImg, actX, formBandAddY+actY, (formListDat[act][0]>>8)&0xff, banderasCor);
					actX += formListPosWidth;

			//#ifdef J2ME
				scr.setClip(0, 0, canvasWidth, canvasHeight);
			//#endif
					if ((formListDat[act][0] & 0x10) != 0) {putColor(SELECT_FONT_BAD);}
					if (selectedLine) {putColor(SELECT_FONT_SELECTED);}
					formTextDraw(formListStr[act][0], 0, formTextAddY, 0, actX,actY, formListTextWidth, formBreakHeight);
				break;
				}
	
				actY += formBreakHeight;
			}


		// Dibujamos Aspa Inferior si la hay
			if (formListAspaHeight > 0 && formListIni < formListStr.length-formListView)
			{
				for (int i=1 ; i<formListAspaHeight-2 ; i++)
				{
					int size = (i)*2;
					fillDraw(SELECT_FONT_OK, (canvasWidth-size)/2, actY+formListAspaHeight-2-i,  size, 1);
				}
				actY += formListAspaHeight;
			}
		break;


		case FORM_LIST_CICLE:

			putColor(BASIC_FONT);
			formTextDraw("<"+formListStr[formListPos][formListDat[formListPos][2]]+">", 0, 0, TEXT_HCENTER|TEXT_VCENTER, 0,canvasHeight-50, canvasWidth, 50);
		break;


		case FORM_LIST_SCREEN:

			y = 0;
	
			int ini =  formListTabIndex[formListTabIndexPos];
			int size = formListTabIndex[formListTabIndexPos+1];

			for (int i=0 ; i<size ; i++)
			{
				y += formBreakHeight;
			}
	
			actY = formListY + ((formListHeight - y) / 2);


//	scr.setColor(-1);
//	scr.drawRect(0, formListY, canvasWidth, formListHeight);	// Cuadrado de pruebas

	
			for (int i=0 ; i<size ; i++)
			{
//				menuListTextDraw(g, menuListDat[i][0], menuListStr[ini+i][0], 0, y);
				putColor(BASIC_FONT);
				formTextDraw(formListStr[ini+i][0], 0, 0, TEXT_HCENTER, formMarginWidth,actY, formListTextWidth, formBreakHeight);
				actY += formBreakHeight;
			}

		break;

		case FORM_WAIT_CLOCK:

			showImage(relojImg);
		break;
		}

	}

}


// -------------------
// formList Opt
// ===================

public int formListOpt()
{
	return(formListDat[formListPos][2]);
}


// -------------------
// formList Id
// ===================

public int formListId()
{
	return(formListDat[formListPos][3]);
}




// -------------------
// form text Draw
// ===================



public void formTextDraw(String[] Str, int X, int Y, int Mode, int topX, int topY, int width, int height)
{

//	scr.drawRect(topX, topY, width, height);	// Cuadrado de pruebas

	scr.setFont(formFont);

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( height-(formFont.getHeight()*Str.length) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  height-(formFont.getHeight()*Str.length) ;}

	X += topX;
	Y += topY;

	for (int i=0 ; i<Str.length ; i++)
	{
		int despX = 0;
	
		if ((Mode & TEXT_HCENTER)!=0) {despX =( width-formFont.stringWidth(Str[i]) )/2;}
		else
		if ((Mode & TEXT_RIGHT)!=0) {despX = width-formFont.stringWidth(Str[i]) ;}
	
		int despY = i * formFontHeight;
	
	//#ifdef J2ME
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
	//#elifdef DOJA
	//#endif
	}

}


public void formTextDraw(String Str, int X, int Y, int Mode, int topX, int topY, int width, int height)
{
	formTextDraw(new String[] {Str}, X, Y, Mode, topX, topY, width, height);

/*
	scr.setFont(formFont);

//#ifdef DOJA
	Y += formFont.getAscent();
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( height-(formFont.getHeight()) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  height-(formFont.getHeight()) ;}

	X += topX;
	Y += topY;

	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX =( width-formFont.stringWidth(Str) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX = width-formFont.stringWidth(Str) ;}

//	scr.setClip(topX, topY, width, height);

//#ifdef J2ME
	scr.drawString(Str,  X+despX, Y , 20);
//#elifdef DOJA
	scr.drawString(Str,  X+despX, Y);
//#endif
*/
}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// popup - Engine
// ===================
// *******************
// -------------------

static final int POPUP_ASK = 0;
static final int POPUP_OK = 1;
static final int POPUP_DOWNLOAD = 2;

static final int POPUP_BODY = 0x298CFF;
static final int POPUP_BORDER = 0x006BAD;
static final int POPUP_FONT = 0xFFFFFF;

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

int popupType = -1;

int popupTextIni;
int popupTextSize;

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
//#endif
	popupFontHeight = popupFont.getHeight();


	popupActionNo = actionNo;
	popupActionOk = actionOk;


	popupWidth = canvasWidth - (canvasWidth>>3);
	popupHeight = canvasHeight - (canvasHeight>>3);

	popupStr = textBreak(str, popupWidth - (popupBorder*2), popupFont);

	if (popupStr.length*popupFontHeight > popupHeight)
	{
		popupWidth = canvasWidth - 2;
		popupHeight = canvasHeight - 2;
	
		popupStr = textBreak(str, popupWidth - (popupBorder*2), popupFont);
	}

	popupX = (canvasWidth - popupWidth) / 2;
	popupY = (canvasHeight - popupHeight) / 2;


	popupTextIni = 0;
	popupTextSize = (popupHeight / popupFontHeight);
	if (popupTextSize > popupStr.length) {popupTextSize = popupStr.length;}


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
	popupType = -1;
	popupStr = null;
	popupShow = false;
}

// -------------------
// popup Tick
// ===================

public boolean popupTick()
{
	if (popupType == POPUP_DOWNLOAD && !Game.running)
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
		// Si estamos en pantalla de 'descarga' notificamos a 'pelaoTick() que aborte
			if (popupType == POPUP_DOWNLOAD) {pelaoDownloadAbort = true;}

			popupRelease();
			popupAction(popupActionNo);
			gameForceRefresh = true;
			return true;
		}
		else if (keyMenu > 0 && popupActionOk != POPUP_ACTION_NONE)
		{
			if (popupTextIni+popupTextSize >= popupStr.length)
			{
				popupRelease();
				popupAction(popupActionOk);
				gameForceRefresh = true;
				return true;
			} else {
				popupTextIni+=popupTextSize;
				if (popupTextIni+popupTextSize > popupStr.length) {popupTextSize = popupStr.length-popupTextIni;}
				popupShow = true;
			}
		}
	}

	return false;
}

// -------------------
// popup Draw
// ===================

public void popupDraw()
{
	if (!popupShow) {return;}
	popupShow = false;

	fillDraw(POPUP_BODY, popupX, popupY, popupWidth, popupHeight);

	putColor(POPUP_BORDER);
	scr.drawRect(popupX, popupY, popupWidth-1, popupHeight-1);

	canvasTextCreate(popupX, popupY, popupWidth, popupHeight);
	textDraw(popupStr, 0,0, POPUP_FONT, TEXT_VCENTER|TEXT_HCENTER, popupTextIni, popupTextSize);
}

// -------------------
// popup Action
// ===================

static final int POPUP_ACTION_NONE = 0;
static final int POPUP_ACTION_NO = 1;
static final int POPUP_ACTION_OK = 2;
static final int POPUP_ACTION_GAME_EXIT = 3;
static final int POPUP_ACTION_PLAY_EXIT = 4;
static final int POPUP_ACTION_SUSCRIBE = 5;

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

	case POPUP_ACTION_PLAY_EXIT:

		playExit = 4;	// Abandonamos GAME_PLAY

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;

//#ifdef FEATURE_LAUNCH_BROWSER
//#endif
	}
}

// <=- <=- <=- <=- <=-















//#ifdef J2ME

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
		prefsData = new byte[] {1,1,0};		// Inicializamos preferencias ya que NO estaban grabadas
	}

// Actualizamos las variables del juego segun las prefs leidas / inicializadas

	gameSound = prefsData[0] != 0;
	gameVibra = prefsData[1] != 0;
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

//#endif








//#ifdef J2ME
public void renderSpriteDraw(Image img, int x, int y, int frame, byte[] cor, int topX, int topY, int width, int height)
{
	int destY = ((height - cor[(frame*6)+3])/2) + topY + y;

	spriteDraw(img, topX, destY, frame, cor);
}
//#elifdef DOJA
//#endif











// *******************
// -------------------
// pelao - Engine
// ===================
// *******************

static final int DOWNLOAD_PROFILE = 0;
static final int DOWNLOAD_CLASIFICATION = 1;
static final int DOWNLOAD_TRACK = 2;
static final int DOWNLOAD_TRACK_LIST = 3;
static final int DOWNLOAD_GHOST = 4;
static final int DOWNLOAD_GHOST_LIST = 5;
static final int UPLOAD_GHOST = 6;

static final int HEAD_PROFILE = 10;
static final int HEAD_CLASIFICATION = 8;
static final int HEAD_TRACK = 5;
static final int HEAD_TRACK_LIST = 9;
static final int HEAD_GHOST = 6;
static final int HEAD_GHOST_LIST = 4;

static final int HEAD_GHOST_UPLOAD = 3;


String pelaoNick;
int pelaoPos;
int pelaoPuntos;
String welcomeMsg;
boolean profileOk = false;

int pelaoTime;		// Time del "ghost" del player para subir
int pelaoTimeNew;
byte[] pelaoData;	// Datos del "ghost" del player para subir

String[] scoreNick;
int[] scorePoints;
int scoreMaxPoints;

String[] trackName;
String[] trackDate;
int[] trackId;
int[] trackWeather;
boolean[] trackAvailable;

boolean pelaoClasifIsEnd;
String[] pelaoClasifNick;
int[] pelaoClasifPoints;

String[] pelaoTrackListName;
String[] pelaoTrackListDate;
int[] pelaoTrackListId;
boolean[] pelaoTrackListIsAval;


int ghostListFirst;
int ghostListMax = 8;	// Numero de ghosts a descargar del servidor por cada solicitud

boolean ghostListIsEnd;
String[] ghostListNick;
int[] ghostListTime;
int[] ghostListId;



int tmpTrackWeather;
String tmpTrackName;
byte[] tmpTrackData;


int tmpTrackId;			// ID del track que vamos a descargar

int clasifActual;
int clasifFirst;
int clasifMax;

int tmpGhostId;			// ID del ghost que keremos descargar

int tmpGhostTime;
String tmpGhostNick;
byte[] tmpGhostData;

boolean pelaoClasificationIsEnd;


boolean pelaoDownloadOk = false;
boolean pelaoDownloadAbort = false;

int pelaoType;

// -------------------
// pelao Download
// ===================

public void pelaoDownload(int type)
{
//#ifdef com.mygdx.mongojocs.mr2.Debug
	String strTmp = "";
	switch (type)
	{
	case DOWNLOAD_TRACK:		strTmp+="DOWNLOAD_TRACK";	break;
	case DOWNLOAD_TRACK_LIST:	strTmp+="DOWNLOAD_TRACK_LIST";	break;
	case DOWNLOAD_CLASIFICATION:strTmp+="DOWNLOAD_CLASIFICATION";	break;
	case DOWNLOAD_PROFILE:		strTmp+="DOWNLOAD_PROFILE";	break;
	case DOWNLOAD_GHOST:		strTmp+="DOWNLOAD_GHOST";	break;
	case DOWNLOAD_GHOST_LIST:	strTmp+="DOWNLOAD_GHOST_LIST";	break;
	case UPLOAD_GHOST:			strTmp+="UPLOAD_GHOST";	break;
	}
	Debug.println("pelao CMD: "+strTmp);
//#endif

	pelaoType = type;
	pelaoDownloadOk = false;
	pelaoDownloadAbort = false;

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	DataOutputStream dos = new DataOutputStream(baos);

	try {

		switch (type)
		{
		case DOWNLOAD_TRACK:		// Descargar track

			pelaoHeader(HEAD_TRACK, dos);
			dos.writeInt(tmpTrackId);
		break;
/*
		case DOWNLOAD_TRACK_LIST:

			pelaoHeader(HEAD_TRACK_LIST, dos);
		break;
*/
		case DOWNLOAD_CLASIFICATION:

			pelaoHeader(HEAD_CLASIFICATION, dos);
            dos.writeInt(clasifFirst);
            dos.writeInt(clasifMax);
		break;

		case DOWNLOAD_PROFILE:

			pelaoHeader(HEAD_PROFILE, dos);
		break;



		case DOWNLOAD_GHOST:

			pelaoHeader(HEAD_GHOST, dos);
            dos.writeInt(tmpTrackId);
            dos.writeInt(tmpGhostId);
		break;

		case DOWNLOAD_GHOST_LIST:		// (Ranking)

			pelaoHeader(HEAD_GHOST_LIST, dos);

			dos.writeInt(tmpTrackId);
			dos.writeInt(ghostListFirst);
			dos.writeInt(ghostListMax);
		break;

		case UPLOAD_GHOST:

			pelaoHeader(HEAD_GHOST_UPLOAD, dos);
			dos.writeInt(tmpTrackId);
			dos.writeInt(pelaoTimeNew);
			dos.writeInt(pelaoData.length);
			dos.write(pelaoData);
		break;
		}

	} catch (Exception e)
	{
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println(e.toString());
		Debug.println("pelaoEngine: outPutStream Error!");
		e.printStackTrace();
	//#endif
		return;
	}

	//#ifdef LOCAL_TESTING
	ga.ConnectionHTTP("http://studio.mjlabs.net:19085/mr2/app_use/use.php", baos.toByteArray());
	//#else
	//#endif
	
	new Thread(ga).start();
}

public void pelaoHeader(int id, DataOutputStream dos) throws Exception {
	dos.writeInt(id);

	//#ifdef com.mygdx.mongojocs.mr2.Debug
		if(ga.userid == null) ga.userid = "92124291";
	//#else
	//#endif

	dos.writeUTF(ga.userid);	// hashUserId
	dos.writeInt(1);	// handsetId
	dos.writeInt(PELAO_PROTOCOL_ID);	// protocolId
}

// -------------------
// pelao Tick
// ===================

public void pelaoTick()
{
	if (!Game.running) {return;}

	if (pelaoDownloadAbort) {Game.running = false; return;}

	if (Game.finished)
	{
		if (Game.result == null)
		{
		//#ifdef com.mygdx.mongojocs.mr2.Debug
			Debug.println("HTTP_result = null");
		//#endif
			Game.running = false;
			System.gc();
			popupRelease();
			biosStatus = biosStatusOld;
			popupInit(gameText[TEXT_ERROR_RED][0],  POPUP_OK);
		} else {
		//#ifdef com.mygdx.mongojocs.mr2.Debug
			Debug.println("HTTP_result = "+Game.result.length+"bytes");
		//#endif
			if (pelaoAction(new DataInputStream(new ByteArrayInputStream(Game.result))))
			{
				pelaoDownloadOk = true;
			}
			Game.result = null;
			Game.running = false;
			System.gc();
		}
	}
}

// -------------------
// pelao Action
// ===================

public boolean pelaoAction(DataInputStream dis)
{
//#ifdef com.mygdx.mongojocs.mr2.Debug
	Debug.println("pelaoAction()");
//#endif

	try {

		int input = dis.readInt();
		if ( input == 0)
		{

			int header = dis.readInt();

			switch (pelaoType)
			{
			case DOWNLOAD_TRACK:

				tmpTrackWeather = dis.readInt();
				tmpTrackName = dis.readUTF();
				tmpTrackData = new byte[dis.readInt()];
				dis.read(tmpTrackData);
			break;
/*
			case DOWNLOAD_TRACK_LIST:

				int numTracks = dis.readInt();

				pelaoTrackListName = new String[numTracks];
				pelaoTrackListDate = new String[numTracks];
				pelaoTrackListId = new int[numTracks];
				pelaoTrackListIsAval = new boolean[numTracks];

				for (int i=0 ; i<numTracks; i++)
				{
					pelaoTrackListName[i] = dis.readUTF();
					pelaoTrackListDate[i] = dis.readUTF();
					pelaoTrackListId[i] = dis.readInt();
					pelaoTrackListIsAval[i] = dis.readInt()==1;
				}
			break;
*/
			case DOWNLOAD_CLASIFICATION:

				int numPlayers = dis.readInt();
				pelaoClasificationIsEnd = dis.readInt()==1;

				pelaoClasifNick = new String[numPlayers];
				pelaoClasifPoints = new int[numPlayers];

				for (int i=0 ; i<numPlayers ; i++)
				{
					pelaoClasifNick[i] = dis.readUTF();
					pelaoClasifPoints[i] = dis.readInt();
				}
			break;

			case DOWNLOAD_PROFILE:

			// info nick
				pelaoNick = dis.readUTF();
				pelaoPos = dis.readInt();
				pelaoPuntos = dis.readInt();
				welcomeMsg = dis.readUTF();

			//#ifdef com.mygdx.mongojocs.mr2.Debug
				Debug.println("Tu nombre es:"+pelaoNick);
			//#endif

			// llista puntuacions
				numPlayers = dis.readInt();
				scoreNick = new String[numPlayers];
				scorePoints = new int[numPlayers];

				for (int i=0; i<numPlayers; i++)
				{
					scoreNick[i] = dis.readUTF();
					scorePoints[i] = dis.readInt();

					if (i==0) {scoreMaxPoints = scorePoints[i];}
				}

				pelaoClasificationIsEnd = numPlayers<10;

			// llista pistes disponibles
				int numTracks = dis.readInt();
				trackName = new String[numTracks];
				trackWeather = new int[numTracks];
				trackDate = new String[numTracks];
				trackId = new int[numTracks];
				trackAvailable = new boolean[numTracks];

				for (int i=0; i<numTracks; i++)
				{
					trackName[i] = dis.readUTF();
					trackWeather[i] = dis.readInt();
					trackDate[i] = dis.readUTF();
					trackId[i] = dis.readInt();
					trackAvailable[i] = dis.readInt() == 1;
				}            
				profileOk = true;
			break;


			case DOWNLOAD_GHOST:

				tmpGhostTime = dis.readInt();
				tmpGhostNick = dis.readUTF();
				tmpGhostData = new byte[dis.readInt()];
				dis.read(tmpGhostData);
			break;
	
			case DOWNLOAD_GHOST_LIST:		// (Ranking)
	
				int numGhosts = dis.readInt();

				ghostListIsEnd = dis.readInt()==1;

				ghostListNick = new String[numGhosts];
				ghostListTime = new int[numGhosts];
				ghostListId = new int[numGhosts];

				for (int i=0 ; i<numGhosts ; i++)
				{
					ghostListNick[i] = dis.readUTF();
					ghostListTime[i] = dis.readInt();
					ghostListId[i] = dis.readInt();
				}
			break;

			case UPLOAD_GHOST:

				popupRelease();
				biosStatus = biosStatusOld;
				popupInit(dis.readUTF(),  POPUP_OK);
			break;
			}
	//#ifdef FEATURE_LAUNCH_BROWSER
	//#endif
		} else {
			popupRelease();
			biosStatus = biosStatusOld;
			popupInit(dis.readUTF(),  POPUP_OK);
			return false;
		}

	} catch (Exception e)
	{
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println(e.toString());
		Debug.println("pelaoEngine: pelaoAction() inputStream Error!");
		e.printStackTrace();
	//#endif
	}

	return true;
}

// <=- <=- <=- <=- <=-























// *******************
// -------------------
// cache - Engine
// ===================
// *******************

static final int cacheMAX = 32;

int cacheC;
int[] cacheP;

int[] cacheTrackId;
String[] cacheTrackName;
int[] cacheTrackWeather;

int[] cacheGhostId;
int[] cacheGhostTime;
String[] cacheGhostName;

int[] cachePlayerTime;

// -------------------
// cache Create
// ===================

public void cacheCreate()
{
	cacheC = 0;
	cacheP = new int[cacheMAX];
	for (int i=0 ; i<cacheMAX ; i++) {cacheP[i] = i;}

	cacheTrackId = new int[cacheMAX];
	cacheTrackName = new String[cacheMAX];
	cacheTrackWeather = new int[cacheMAX];

	cacheGhostId = new int[cacheMAX];
	cacheGhostTime = new int[cacheMAX];
	cacheGhostName = new String[cacheMAX];

	cachePlayerTime = new int[cacheMAX];

	for (int i=0 ; i<cacheMAX ; i++)
	{
		cacheGhostId[i] = -1;
		cacheGhostName[i] = " ";
		cachePlayerTime[i] = -1;
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


//#ifdef com.mygdx.mongojocs.mr2.Debug
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
/*
	System.out.println ("cacheTrackId.length:"+cacheTrackId.length);
	System.out.println ("cacheTrackName.length:"+cacheTrackName.length);
	System.out.println ("cacheTrackWeather.length:"+cacheTrackWeather.length);

	System.out.println ("cacheGhostId.length:"+cacheGhostId.length);
	System.out.println ("cacheGhostName.length:"+cacheGhostName.length);
*/
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
	//#ifdef com.mygdx.mongojocs.mr2.Debug
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
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println ("cacheReaderError:  "+e.toString());
	//#endif
		return true;
	}

	return false;
}


// <=- <=- <=- <=- <=-







public String getStrTime(int time)
{
	int mil = time%1000; mil = mil / 10;	//regla3(mil, 1000, 60);
	int seg = time/1000;
	int min = seg/60; if (min > 99) {min=99;seg=59;mil=99;}

	seg = seg%60;

	String milStr = ""+mil; if (milStr.length()==1) {milStr = "0"+milStr;}
	String segStr = ""+seg; if (segStr.length()==1) {segStr = "0"+segStr;}
	String minStr = ""+min; if (minStr.length()==1) {minStr = "0"+minStr;}

	return minStr+":"+segStr+":"+milStr;
}




// **************************************************************************//
// Inicio Clase Game
// **************************************************************************//

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

final static int TEXT_CLASIFICATION = 16;
final static int TEXT_SCORES = 17;
final static int TEXT_BOXES = 18;
final static int TEXT_CIRCUITOS = 19;
final static int TEXT_MAS_CIRCUITOS = 20;
final static int TEXT_OPONENTE = 21;
final static int TEXT_OPONENTES = 22;
final static int TEXT_CONTRA_TU_MEJOR_MARCA = 23;
final static int TEXT_DESCARGAR_OPONENTE = 24;
final static int TEXT_NO_HAY_OPONENTES = 25;
final static int TEXT_SIGIENTE_PAGINA = 26;
final static int TEXT_DESCAR_PERFIL = 27;
final static int TEXT_DESCARGANDO = 28;
final static int TEXT_ACTUALIZANDO = 29;
final static int TEXT_PREGUNTA_SALIR = 30;
final static int TEXT_ERROR_RED = 31;
final static int TEXT_DESEA_CONECTAR = 32;
final static int TEXT_NO_HAY_ESPACIO_GUARDAR_PISTA = 33;
final static int TEXT_NO_HAY_ESPACIO_GUARDAR_PISTA_SUSTITUIR = 34;
final static int TEXT_ERROR_GUARDAR_PISTA = 35;
final static int TEXT_MARCA_MEJORADA_PREGUNTA_SUBIR = 36;
final static int TEXT_ABANDONAR_CARRERA = 37;
final static int TEXT_MEJOR_MARCA = 38;
final static int TEXT_HAS_VENCIDO_OPONENTE = 39;
final static int TEXT_HAS_PERDIDO_OPONENTE = 40;
final static int TEXT_HAS_MEJORADO_TU_MARCA = 41;
final static int TEXT_NO_HAS_MEJORADO_TU_MARCA = 42;
final static int TEXT_MEJOR_VUELTA = 43;
final static int TEXT_NUEVA_MARCA = 44;
final static int TEXT_ANTERIOR_MARCA = 45;
final static int TEXT_JUGADOR = 46;
final static int TEXT_GUARDANDO = 47;
final static int TEXT_PULSA_1 = 48;
final static int TEXT_PULSA_2 = 49;
final static int TEXT_HELP_1 = 50;
final static int TEXT_HELP_2 = 51;
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int GAME_LOGOS = 0;

final static int GAME_MENU_START = 10;
final static int GAME_MENU_MAIN = 11;
final static int GAME_MENU_MAIN_END = 12;

final static int GAME_MENU_SECOND = 25;

final static int GAME_MENU_GAMEOVER = 30;

final static int GAME_PLAY = 20;

final static int GAME_PILLA_PERFIL = 40;
final static int GAME_CLASIFICATION = 50;
final static int GAME_CIRCUITOS = 60;
final static int GAME_OPONENTES = 70;
final static int GAME_OPONENT_LIST = 80;
final static int GAME_PRESENT = 90;
final static int GAME_RESUMEN = 100;
final static int GAME_DELETE_TRACKS = 110;
// ===============================================
public static final int SOUND_MENU = 0;
public static final int SOUND_CLASSIFICATION = 1;
public static final int SOUND_FINISH = 2;
public static final int SOUND_TICK = 3;
public static final int SOUND_COUNTDOWN = 4;
public static final int SOUND_CRASH = 5;
public static final int SOUND_BLUEPOWER = 6;
public static final int SOUND_BRIEFING = 7;

int downloadType;
int downloadReturnOk;
int downloadReturnCancel;

int toDeleteTrackIni;
int toDeleteTrackPos;
int toDeleteTrackAdr;
int toDeleteTrackNeeded;
int toDeleteGameStatusRet;
boolean toDeleteTrackOk;
String toDeteleFileName;

Image horizonteImg;

boolean gameContraOponente;

int gameStatus = 0;

// -------------------
// game Create
// ===================

public void gameCreate()
{
// --------------------------------
// Cargamos e inicializamos TODOS los sonidos del juego
// ================================
//#ifndef PLAYER_NONE

	//#ifdef PLAYER_OTA
	//#elifdef PLAYER_SAMSUNG
	//#elifdef PLAYER_SHARP
	//#else
		//#ifdef J2ME
			soundCreate( new String[] {
				"/menu.mid",			// 0 - Musica de la caratula
				"/classification.mid",		// 1 - Classification
				"/finish.mid",			// 2 - Finish 
				"/tick.mid",			// 3 - Tick 
				"/countdown.mid",		// 4 - countdown
				"/crash.mid",			// 5 - crash
				"/bluepower.mid",		// 6 - bluepower
				"/briefing.mid",		// 7 - briefing
				});
		//#elifdef DOJA
		//#endif
	//#endif


	//#ifdef SG-D410
	//#endif

//#endif
// ================================


	//#ifndef NO_COLLISION
	//#ifndef FILE_PACK
	byte[] tmpAnimData = loadFile("/anim.dat");
	//#else
	//#endif
	
	frame = new byte[28];
	pframe = new byte[28];
	xpos = new byte[28];
	ypos = new byte[28];
	pxpos = new int[28];
	pypos = new byte[28];
	for(int i = 0; i < 28; i++) {
		frame[i] = tmpAnimData[i     ];
		pframe[i] = tmpAnimData[i + 28];
		xpos[i] = (byte) ((canvasWidth * tmpAnimData[i + 28 * 2]) / 176);
		ypos[i] = (byte) ((canvasHeight * tmpAnimData[i + 28 * 3]) / 208);
		pxpos[i] = (int) ((canvasWidth * tmpAnimData[i + 28 * 4]) / 176) << 1;
		pypos[i] = (byte) ((canvasHeight * tmpAnimData[i + 28 * 5]) / 208);
		//xpos[i] = (byte) ((canvasWidth * xpos[i]) / 176);
		//pxpos[i] = (int) ((canvasWidth * pxpos[i]) / 176);
		//ypos[i] = (byte) ((canvasHeight * ypos[i]) / 208);
		//pypos[i] = (byte) ((canvasHeight * pypos[i]) / 208);
	}
	
	FwdVehicle.LAT_BORDER = (((canvasWidth * FwdVehicle.LAT_BORDER) / 2) / 176) + (FwdVehicle.LAT_BORDER / 2);
	
	//#endif
//#ifdef J2ME
	//#ifdef FULLSCREEN
		
		//#ifndef FILE_PACK
		listenerImg = loadImage("/softkeys");
		//#else
		//#endif
		
		//#ifndef FILE_PACK
		listenerCor = loadFile("/softkeys.cor");
		//#else
		//#endif
	//#endif
//#endif



	cacheCreate();


//#ifdef com.mygdx.mongojocs.mr2.Debug
	Debug.println("- DIR Contents:");
	rmsGetDir();
	Debug.println("-----");
//#endif



}

// DOJAPORT
/*
// -------------------
// game Destroy
// ===================

public void gameDestroy()
{
}
*/

// -------------------
// game Tick
// ===================

public void gameTick()
{
	switch (gameStatus)
	{

	case GAME_LOGOS:
		//#ifndef FILE_PACK
		logosInit(new String[] {"/movistar","/microjocs"}, new int[] {0xffffff,0xffffff}, 3000);
		//#else
		//#endif
		gameStatus = GAME_MENU_START;
	break;



	case GAME_MENU_START:

		resetMenus();
		gameStatus = GAME_MENU_MAIN;
	break;



// --------------------------------
// Gestionamos Descarga de archivos
// ================================
	case GAME_PILLA_PERFIL:

//		if (menuImg == null) {menuImg = loadImage("/caratula");}
//		canvasImg = menuImg;

		popupInit(gameText[TEXT_DESEA_CONECTAR][0],  POPUP_ASK, POPUP_ACTION_NO, POPUP_ACTION_OK,  SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		gameStatus++;
	break;

	case GAME_PILLA_PERFIL+1:

		if (!popupAcept) {gameStatus = downloadReturnCancel; break;}

		if (!profileOk)
		{
			popupInit(gameText[TEXT_DESCAR_PERFIL][0],  POPUP_DOWNLOAD, POPUP_ACTION_NO, POPUP_ACTION_NONE,  SOFTKEY_CANCEL, SOFTKEY_NONE);
			pelaoDownload(DOWNLOAD_PROFILE);
			gameStatus++;
		} else {
			gameStatus+=2;
		}
	break;

	case GAME_PILLA_PERFIL+2:

		if (!pelaoDownloadOk) {gameStatus = downloadReturnCancel; break;}

	case GAME_PILLA_PERFIL+3:

		if (downloadType == DOWNLOAD_PROFILE) {gameStatus = downloadReturnOk; break;}

			String tmpStr = gameText[(downloadType!=UPLOAD_GHOST?TEXT_DESCARGANDO:TEXT_ACTUALIZANDO)][0];
			popupInit(tmpStr,  POPUP_DOWNLOAD, POPUP_ACTION_NO, POPUP_ACTION_NONE,  SOFTKEY_CANCEL, SOFTKEY_NONE);
			pelaoDownload(downloadType);
			gameStatus++;
	break;

	case GAME_PILLA_PERFIL+4:

		gameStatus = pelaoDownloadOk? downloadReturnOk:downloadReturnCancel;
	break;
// ================================





// --------------------------------
// Gestionamos visualizacion de la Clasificacion
// ================================
	case GAME_CLASIFICATION:

		clasifActual = 0;
		clasifFirst = 0;
		clasifMax = 10;

		if (profileOk) {gameStatus++; break;}

		downloadReturnOk = GAME_CLASIFICATION+1;
		downloadReturnCancel = GAME_MENU_MAIN;
		downloadType = DOWNLOAD_PROFILE;
		gameStatus = GAME_PILLA_PERFIL;
	break;

	case GAME_CLASIFICATION+1:

		pelaoClasifNick = scoreNick;
		pelaoClasifPoints = scorePoints;
		gameStatus++;
	break;

	case GAME_CLASIFICATION+2:
		soundPlay(SOUND_CLASSIFICATION, 1);
		menuInit(MENU_CLASIFICATION);
		gameStatus++;
	break;

	case GAME_CLASIFICATION+3:

		clasifFirst = clasifActual + pelaoClasifNick.length;

		downloadReturnOk = GAME_CLASIFICATION+4;
		downloadReturnCancel = GAME_CLASIFICATION+2;
		downloadType = DOWNLOAD_CLASIFICATION;
		gameStatus = GAME_PILLA_PERFIL;

//		menuInit(MENU_CLASIFICATION);
//		gameStatus++;
	break;

	case GAME_CLASIFICATION+4:

		clasifActual = clasifFirst;
		gameStatus = GAME_CLASIFICATION+2;
	break;
// ================================








// --------------------------------
// Gestionamos la seleccion y descarga de los Circuitos
// ================================
	case GAME_CIRCUITOS:

	// Mostramos Listado de circuitos para jugar/descargar
		menuInit(MENU_CIRCUITOS);
	break;

	case GAME_CIRCUITOS+1:

	// Mostramos pantalla de espera...
		menuInit(MENU_WAIT, gameText[TEXT_GUARDANDO][0]);
		gameDraw();


	// Chequeamos memoria libre en RMS...
		toDeleteTrackNeeded = tmpTrackData.length;
		toDeleteGameStatusRet = gameStatus+1;
		toDeteleFileName = "t"+tmpTrackId+".trk";
		gameStatus = GAME_DELETE_TRACKS;
	break;

	case GAME_CIRCUITOS+2:

	// Hemos descargado un circuito y debemos almacenarlo en la cache
		if (toDeleteTrackOk && cacheTrackSave(tmpTrackId, tmpTrackName, tmpTrackWeather, tmpTrackData))
		{
			popupInit(gameText[TEXT_ERROR_GUARDAR_PISTA][0],  POPUP_OK);
			toDeleteTrackOk = false;
		}

		if (!toDeleteTrackOk)
		{
			gameStatus = GAME_CIRCUITOS;
		} else {
			gameStatus = GAME_OPONENTES;
		}

		menuExit = true;
	break;

// ================================







// --------------------------------
// Gestionamos la seleccion y descarga de los Oponentes
// ================================
	case GAME_OPONENTES:

		menuInit(MENU_OPONENTES);
	break;


	case GAME_OPONENT_LIST:

	// Mostramos Listado de ghosts para descargar
		menuInit(MENU_OPONENT_LIST);
	break;

	case GAME_OPONENT_LIST+1:

	// Mostramos pantalla de espera...
		menuInit(MENU_WAIT, gameText[TEXT_GUARDANDO][0]);
		gameDraw();

	// Chequeamos memoria libre en RMS...
		toDeleteTrackNeeded = tmpGhostData.length;
		toDeleteGameStatusRet = gameStatus+1;
		toDeteleFileName = "t"+tmpTrackId+".gho";
		gameStatus = GAME_DELETE_TRACKS;
	break;

	case GAME_OPONENT_LIST+2:

	// Hemos descargado un ghost y debemos almacenarlo en la cache

		if (toDeleteTrackOk && cacheGhostSave(tmpTrackId, tmpGhostId, tmpGhostTime, tmpGhostNick, tmpGhostData))
		{
			popupInit(gameText[TEXT_ERROR_GUARDAR_PISTA][0],  POPUP_OK);
		}

		menuExit = true;
		gameStatus = GAME_PRESENT;
	break;
// ================================







// --------------------------------
// Gestionamos El borrado de pistas almacenadas
// ================================
	case GAME_DELETE_TRACKS:

		toDeleteTrackOk = false;
		toDeleteTrackIni = 0;
		gameStatus++;

	case GAME_DELETE_TRACKS+1:

	// Hay espacio en RMS para guardar Track?
		int libre = rmsAvailable(toDeteleFileName) - 32;

	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println ("Libre:"+libre);
		Debug.println ("Neded:"+toDeleteTrackNeeded);
		Debug.println ("Final:"+(libre-toDeleteTrackNeeded));
	//#endif

		if ( libre > toDeleteTrackNeeded )
		{
			toDeleteTrackOk = true;
			gameStatus = toDeleteGameStatusRet;
			break;
		}

		toDeleteTrackAdr = -1;

	// Buscamos track en RMS para borrar... (las ke hay en JAR no valen!!! ^_^!)
		for ( ; toDeleteTrackIni<cacheC ; toDeleteTrackIni++)
		{
			int adr = cacheP[toDeleteTrackIni];

			if ((cacheTrackWeather[adr] & 0xF0000000) != 0)		// Pista en JAR?
			{
			// Si no hay ghost ni ghostPlayer saltamos el borrar esta pista
				if (cacheGhostId[adr] < 0 && cachePlayerTime[adr] < 0) {continue;}
			}

		// No podemos borrar la pista a la que estamos jugado... la eskivamos...
			if (cacheTrackId[adr] == tmpTrackId) {continue;}

			toDeleteTrackPos = toDeleteTrackIni;
			toDeleteTrackAdr = adr;
			break;
		}

	// Podemos borrar otra pista ya almacenada en el RMS?
		if (toDeleteTrackAdr < 0)
		{
			popupInit(gameText[TEXT_NO_HAY_ESPACIO_GUARDAR_PISTA][0],  POPUP_OK);
			gameStatus = toDeleteGameStatusRet;
		} else {
			popupInit(gameText[TEXT_NO_HAY_ESPACIO_GUARDAR_PISTA_SUSTITUIR][0]+" "+cacheTrackName[toDeleteTrackAdr]+"?",  POPUP_ASK, POPUP_ACTION_NO, POPUP_ACTION_OK, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
			gameStatus++;
		}

//		menuExit = true;
	break;

	case GAME_DELETE_TRACKS+2:

	// Aceptamos borrar del RMS la pista
		if (popupAcept)
		{
			rmsDeleteFile("t"+cacheTrackId[toDeleteTrackAdr]+".trk");	// Borramos Pista
			rmsDeleteFile("t"+cacheTrackId[toDeleteTrackAdr]+".gho");	// Ghost descargado
			rmsDeleteFile("t"+cacheTrackId[toDeleteTrackAdr]+".ply");	// Ghost del Jugador

		// Si la pista esta en el JAR solo borramos el Ghost y GhostPlayer
			if ((cacheTrackWeather[toDeleteTrackAdr] & 0xF0000000) == 0)		// Pista en RMS?
			{
				cacheListDel(toDeleteTrackPos);
			} else {
				cacheGhostId[toDeleteTrackPos] = -1;	// Eliminamos referencia del Ghost en el RMS para esta pista
				cacheGhostName[toDeleteTrackPos] = " ";	// Borramos nombre del Ghost
				cachePlayerTime[toDeleteTrackPos] = -1;	// Eliminamos referencia del GhostPlayer en el RMS para esta pista
			}

			rmsSaveFile("list", cacheGenerator());

			gameStatus = GAME_DELETE_TRACKS+1;
		} else {
			gameStatus = toDeleteGameStatusRet;
		}
	break;
// ================================


















// --------------------------------
// Gestionamos la Presentacion del circuito donde jugar
// ================================
	case GAME_PRESENT:

	//#ifdef com.mygdx.mongojocs.mr2.Debug
	try {
		Debug.println("---DEBUG---");
		Debug.println("GamePresent");
		Debug.println("Track Name: "+tmpTrackName+" : Checksum:"+checksum(tmpTrackData)+" : Size:"+tmpTrackData.length);
		Debug.println("Ghost Name: "+tmpGhostNick+" : Checksum:"+checksum(tmpGhostData)+" : Size:"+tmpGhostData.length);
		Debug.println("---DEBUG---");
	} catch (Exception e)
	{
		Debug.println(e.toString());
	}
	//#endif


		horizonteImg = null;

		//bestTime = tmpGhostTime;
		//if(bestTime < 2000) {
		//	bestTime = 99 * 60000 + 59 * 1000 + 999;
		//}
		bestTime = 0;

		soundPlay(SOUND_BRIEFING, 1);
		menuInit(MENU_PRESENT);
		gameStatus++;
	break;

	case GAME_PRESENT+1:

		if (formAskAcept)
		{
			soundStop();
	
			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	
			menuInit(MENU_WAIT, gameText[TEXT_LOADING][0]+"...");

			//gameStatus = GAME_PLAY;
			gameStatus = GAME_MENU_MAIN_END;
		} else {
		popupInit(gameText[TEXT_ABANDONAR_CARRERA][0],  POPUP_ASK, POPUP_ACTION_NO, POPUP_ACTION_OK, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		gameStatus++;
		}
	break;

	case GAME_PRESENT+2:

		if (popupAcept)
		{
			horizonteImg = null;

			gameStatus = GAME_MENU_MAIN;
		} else {
			gameStatus = GAME_PRESENT;
		}
	break;
// ================================







// --------------------------------
// Gestionamos Resumen de la carrera, almacenamos player y solicitud de subir tu puntuacion
// ================================
	case GAME_RESUMEN:

		menuInit(MENU_RESUMEN);
		gameStatus++;
	break;


	case GAME_RESUMEN+1:

	// Mejor puntuacion ke la anterior???
		if (pelaoTimeNew > pelaoTime)
		{
			gameStatus = GAME_PRESENT;
			break;
		}

	// Mostramos pantalla de espera...
		menuInit(MENU_WAIT, gameText[TEXT_GUARDANDO][0]);
		gameDraw();

	// Chequeamos memoria libre en RMS...
		toDeleteTrackNeeded = pelaoData.length;
		toDeleteGameStatusRet = gameStatus+1;
		toDeteleFileName = "t"+tmpTrackId+".ply";
		gameStatus = GAME_DELETE_TRACKS;
	break;

	case GAME_RESUMEN+2:

	// Hemos descargado un ghost y debemos almacenarlo en la cache

		if (toDeleteTrackOk && cachePlayerSave(tmpTrackId, pelaoTimeNew, pelaoData))
		{
			popupInit(gameText[TEXT_ERROR_GUARDAR_PISTA][0],  POPUP_OK);
		}

		if (!gameContraOponente)
		{
			tmpGhostTime = pelaoTimeNew;
			tmpGhostData = pelaoData;
		}

		menuExit = true;
		gameStatus++;	// = GAME_PRESENT;
	break;




	case GAME_RESUMEN+3:

		popupInit(gameText[TEXT_MARCA_MEJORADA_PREGUNTA_SUBIR][0],  POPUP_ASK, POPUP_ACTION_NO, POPUP_ACTION_OK, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		gameStatus++;
	break;

	case GAME_RESUMEN+4:

		if (popupAcept)
		{
			downloadReturnOk = GAME_PRESENT;
			downloadReturnCancel = GAME_RESUMEN;
			downloadType = UPLOAD_GHOST;
			gameStatus = GAME_PILLA_PERFIL+3;
		} else {
			gameStatus = GAME_PRESENT;
		}
	break;
// ================================





// --------------------------------
// Gestionamos los menus Horizontales (Principal y secundario)
// ================================
	case GAME_MENU_MAIN:

		menuInit(MENU_MAIN);
		soundPlay(SOUND_MENU,0);
		gameStatus = GAME_MENU_MAIN_END;
	break;

	case GAME_MENU_MAIN_END:

		soundStop();
		menuImg = null;
		System.gc();

		gameStatus = GAME_PLAY;
	break;



	case GAME_MENU_SECOND:

		menuInit(MENU_SECOND);
		gameStatus = GAME_PLAY+2;
	break;

	case GAME_MENU_GAMEOVER:

/*
		canvasFillTask(0x000000);
		canvasTextTask(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, TEXT_VCENTER|TEXT_HCENTER | TEXT_OUTLINE);
		gameDraw();

		waitTime(3000);
		*/
		//gameStatus = GAME_MENU_MAIN;


		gameStatus = GAME_RESUMEN;
	break;
// ================================









// --------------------------------
// Gestionamos motor del juego
// ================================
	case GAME_PLAY:
		playCreate();
		gameStatus++;

	case GAME_PLAY+1:
		playInit();
		playExit = 0;
		menuExit = true;
		gameStatus++;
	break;

	case GAME_PLAY+2:
		if (keyMenu == -1 && lastKeyMenu == 0) {
			menuStart = 1;
			gameStatus = GAME_MENU_SECOND;
			break;
		}
		
		if(keyMisc == 11 && ((lastChange + 1000) < System.currentTimeMillis())) {
			//#ifndef NO_ROLL
			if(gameStatus != GAME_MENU_SECOND) {
				if(myVehicle != null && !myVehicle.lateralFall && !myVehicle.collision) {
					gameMode = (gameMode + 1) % 2;
				} else {
					gameMode = 0;
				}
			}
			//#endif
			lastChange = System.currentTimeMillis();
		}

		if ( !playTick() ) {break;}

		playRelease();

		gameStatus--;

		switch (playExit)
		{
		case 1:	// Carrera Terminada "legalmente"

			playDestroy();
			gameStatus = GAME_RESUMEN;
		break;

		case 3:	// Producir Game Over

			playDestroy();

			gameStatus = GAME_MENU_GAMEOVER;
		break;

		case 4:	// Abandonamos Carrera

			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);

			playDestroy();

			menuInit(MENU_MAIN);
			soundPlay(SOUND_MENU,0);
		break;
		}

		playExit=0;
	break;
// ================================


	}

}

public void resetMenus() {
	formCreate();
	if(menuImg == null) {
		//#ifndef FILE_PACK
		menuImg = loadImage("/caratula");
		//#else
		//#endif
	}
	
	if(relojImg == null) {
		//#ifndef FILE_PACK
		relojImg = loadImage("/loading");
		//#else
		//#endif
	}
}

// -------------------
// game Refresh :: Este metodo se llama tras regresar de un 'incomming call' y debemos restaurar el juego.
// ===================

public void gameRefresh()
{
	switch (gameStatus)
	{
	case GAME_PLAY+2:		// GAME_PLAY+2 => Estado del juego en funcionamiento.
		gameStatus = GAME_MENU_SECOND;
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

//	if (biosStatus == BIOS_MENU && gameStatus == GAME_PLAY+2 && menuListShow) {playShow = true;}

	if (playShow) { playShow=false; playDraw(); }

// ================================

}

// <=- <=- <=- <=- <=-






// -----------------------
// Pruebas DEBUG: de JUAN:
// =======================

//#ifdef com.mygdx.mongojocs.mr2.Debug

public int checksum(byte[] data)
{
	if (data==null) {return -1;}

	int suma = 0;
	for (int i=0 ; i<data.length ; i++)
	{
		suma += data[i];
	}
	return suma;
}

//#endif

// <=- <=- <=- <=- <=-



























//#ifdef J2ME
//#ifdef NOKIAUI
// ----------------------------------------------------------

DirectGraphics LCD_dGfx = null;

// ----------------------------------------------------------

public void spriteDraw(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
{
	Frame*=(Trozos*6);

	for (int i=0 ; i<Trozos ; i++)
	{
	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int Flip=0;
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}
	if (SizeX < 0) {SizeX*=-1; Flip|=1;}
	if (SizeY < 0) {SizeY*=-1; Flip|=2;}
	
	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY,  Flip);
	}
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       > canvasWidth)
	||	(DestX+SizeX < 0)
	||	(DestY       > canvasHeight)
	||	(DestY+SizeY < 0))
	{return;}

	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
		scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
		break;

	case 1:
		LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
		break;

	case 2:
		LCD_dGfx.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
		break;

	case 3:
		LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip X+Y
		break;
	}
	//hack
	scr.setClip(0, 0, canvasWidth, canvasHeight);
	scr.clipRect(0, 0, canvasWidth, canvasHeight);
}
//#endif
//#ifdef MIDP20FLIP
//#endif

//#ifndef NOKIAUI
//#ifndef MIDP20FLIP
// ----------------------------------------------------------

//#endif
//#endif
//#endif



// *******************
// -------------------
// play - Engine
// ===================
// *******************
// -------------------

boolean playShow;

int playExit;


/**
 * Here comes the shit!
*/

	public static GameCanvas instance = null;
	public int gameMode = 0;


	//camera variables
	public int fov = 37;
	public int height = 60;
	public int pitch = 7;
	
	//race variables
	public long raceTime = 0;
	public int endRace = 0;
	public long endRaceTime = 0;
	//public long bestTime = 99 * 60000 + 59 * 1000 + 999;	//9min will be enough
	public long bestTime = 5999999;	//9min will be enough
	
	public long lapTime = 0;
	public long lastLapTime = 0;
	public long timePause = -1;
	public int lastLPusid = -1;
	public int lapCounter = 0;
	public boolean forcedTick = false;
	//public long ts = 0;
	//public int[] depthLevels = {
	//	7, 21-5, 36-5, 51-5, 66-5, 82-5, 98-5, 114-5, 129-5
	//};
	
	public int[] depthLevels = {7, 18, 32, 46, 61, 72, 82, 92, 101};
	//public int[] depthLevels = {15, 18, 32, 46, 61, 72, 82, 92, 101};
	
	//car
	public FwdVehicle myVehicle = null;
	public FwdVehicle phantom = null;
	public boolean bs = false;
	//public int curvepoints[];

// -------------------
// play Create
// ===================

public void playCreate()
{
	instance = this;
	loadData();
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	freeData();
	//#ifdef FREE_MENUS
	//#ifndef FILE_PACK
	banderasImg = loadImage("/banderas");
	banderasCor = loadFile("/banderas.cor");
	//#else
	//#endif
	//#endif
	resetMenus();
}

// -------------------
// play Init
// ===================

public void playInit()
{
	startRace();
	//aixi executa dos frames abans de pintar res
	startMillis = System.currentTimeMillis() - 500;
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

long lastFrameMillis, startMillis, remainingMillis, rm2;
int  phantomTick = 0;
public boolean playTick()
{
	
	if (playExit!=0) {return true;}
	playShow = true;
	
	//aixi executa un frame segur
	try {
		long delta = startMillis - lastFrameMillis;
		
		if(!forceTick) {
			remainingMillis += delta;
			rm2 += delta;
		} else {
			remainingMillis += delta + 200;
			rm2 += delta + 60;
			forceTick = false;
		}
		
		if(myVehicle != null) {
			//cridat a 16,67fps
			while(rm2 >= 60) {
				if(endRace == 1) {
					endRaceTime++;
				}
				
				if(endRaceTime > 40) {
					endRace = 0;
					endRaceTime = 0;
					
					//pelaoTimeNew = (int) phantom.bestTime;
					pelaoTimeNew = (int) bestTime;
					if(phantom.phantom_isSet) {
						pelaoData = convShort2Byte(phantom.phantom_bestTrack);
					}
					
					//gameStatus = GAME_MENU_MAIN;
					listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
					playExit = 3;		// flag para SALIDA de juego tipo GAME OVER
					menuExit = true;
					//#ifdef com.mygdx.mongojocs.mr2.Debug
					Debug.println("contraOponente? " + gameContraOponente);
					Debug.println("pelaoTime: " + pelaoTime);
					Debug.println("tmpGhostTime: " + tmpGhostTime);
					Debug.println("");
					Debug.println("pelaoTimeNew: " + pelaoTimeNew);
					//#endif
				}
				myVehicle.fastTick();
				rm2 -= 60;
			}
			
			//cridat a 5fps
			while(remainingMillis >= 200) {
				myVehicle.saveState();
				myVehicle.timedTick();
				if(lapCounter > 0) {
					phantom.phantom_incTickBase();
				}
				
				if(phantomTick == 0) {
					//phantom.phantom_addKey((int) lapTime, myVehicle.w_subusid, myVehicle.w_latdisplace, myVehicle.w_roll, myVehicle.getXYZ((byte)(0)), myVehicle.getXYZ((byte)(1)), myVehicle.getXYZ((byte)(2)), phantomz);
					phantom.phantom_addKey(myVehicle.w_subusid, myVehicle.w_latdisplace);
					phantomTick = FwdVehicle.MAX_PHANTOM_TICKS;
				}
				phantomTick--;
				tickCount++;
				remainingMillis -= 200;
			}
			deltaTime = (int) remainingMillis;
			int alpha = toFP((int) remainingMillis) / 200;
			myVehicle.adjustVars(alpha);
			phantom.adjustVars(alpha);
		} else {
			remainingMillis = 0;
			rm2 = 0;
		}	
	} catch (Exception e) {
		e.printStackTrace();
	}
	//#ifdef com.mygdx.mongojocs.mr2.Debug
//		com.mygdx.mongojocs.mr2.DebugX.l = (int)(System.currentTimeMillis()-mssl);		// Comentado x juan para poder compilar
	//#endif
	raceTick();

	return false;
}

// -------------------
// play Draw
// ===================

public void playDraw()
{
	boolean flash = false;
		
	if(myVehicle != null && myVehicle.flash != 0) {
		//canvasFillDraw(myVehicle.flash);
		scr.setColor(myVehicle.flash);
		
		//#ifdef BIG_PANEL
		//#ifdef J2ME
		//#ifdef CARS_S40
		//#else
		scr.setClip(0, 49, canvasWidth, canvasHeight - 49);
		//#endif
		//#endif
		
		//#ifdef CARS_S40
		//#else
		scr.fillRect(0, 49, canvasWidth, canvasHeight - 49);
		//#endif
		
		//#else
		//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		scr.fillRect(0, 0, canvasWidth, canvasHeight);
		//#endif
		myVehicle.flash = 0;
		flash = true;
	} //else {
		//canvasFillDraw(0x0000ff);
		//#ifdef NOKIAUI
			LCD_dGfx = DirectUtils.getDirectGraphics(scr);
			//LCD_dGfx.setARGBColor(0xFFFFFFFF);
		//#endif
	//}
	
	
	//#ifdef CARS_S40
	//#ifndef NO_ROLL
	
	//#endif
	//#endif
	
	//#ifdef BIG_PANEL
	//#endif
	
	render(scr, flash);
	
	
}

// <=- <=- <=- <=- <=-










	void loadInitTrack() {

	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("Loading track");
	//#endif

		// cache? not atm
		System.gc();
		
		// cache?
		//#ifdef com.mygdx.mongojocs.mr2.Debug
		System.out.println("pre-loadTrack");
		//#endif
		loadTrack();
		System.gc();
		//#ifdef com.mygdx.mongojocs.mr2.Debug
		System.out.println("post-loadTrack");
		//#endif
		
		// preprocess
		for (int i = 0; i < track_trackSectionsMap_patchList.length; ++i) {
			//TrackSection ts = track_trackSectionsMap[i];
			
			if (track_trackSectionsMap_patchList[i] != null) {
				for (int j = 0; j < track_trackSectionsMap_patchList[i].length; j+= TRACK_INTS_PER_PATCH) {
					//System.out.println(ts.patchList[j] + ", " + ts.patchList[j + 1] + " ; " + ts.patchList[j + 4] + ", " + ts.patchList[j + 5]);
						int type = track_trackSectionsMap_patchList[i][j+7];
						
						for(int k = 0; k < 4; k++) {
							track_trackSectionsMap_patchList[i][j + k] -= 200;
						}
						
						if ((type & TRACK_LEFT_BIT) != 0) {
							track_trackSectionsMap_patchList[i][j] = track_trackSectionsMap_patchList[i][j+2] = -1200;
						}
	
						if ((type & TRACK_RIGHT_BIT) != 0) {
							track_trackSectionsMap_patchList[i][j+1] = track_trackSectionsMap_patchList[i][j+3] = 1200;
						}
						
						for(int k = 0; k < 4; k++) {
							track_trackSectionsMap_patchList[i][j + k] = (track_trackSectionsMap_patchList[i][j + k]  << 8) / 6;
						}
						
						track_trackSectionsMap_patchList[i][j+4] = (track_trackSectionsMap_patchList[i][j+4] << 16) / 140;
						track_trackSectionsMap_patchList[i][j+5] = (track_trackSectionsMap_patchList[i][j+5] << 16) / 140;
				}
			}
			if (track_trackSectionsMap_lineList[i] != null) {
				for (int j = 0; j < track_trackSectionsMap_lineList[i].length; j+= TRACK_INTS_PER_LINE) {
						track_trackSectionsMap_lineList[i][j    ] = ((track_trackSectionsMap_lineList[i][j    ] - 200) << 8) / 6;
						track_trackSectionsMap_lineList[i][j + 2] = ((track_trackSectionsMap_lineList[i][j + 2] - 200) << 8) / 6;
						track_trackSectionsMap_lineList[i][j + 1] = (track_trackSectionsMap_lineList[i][j + 1] << 16) / 140;
						track_trackSectionsMap_lineList[i][j + 3] = (track_trackSectionsMap_lineList[i][j + 3] << 16) / 140;
				}
			}
		}
		System.gc();
		for (int i = 0; i < track_verticalTrackSectionsMap_patchList.length; ++i) {
			if (track_verticalTrackSectionsMap_patchList[i] != null) {
				// VERTICAL //
				for (int j = 0; j < track_verticalTrackSectionsMap_patchList[i].length; j+= TRACK_INTS_PER_PATCH) {
					int type = track_verticalTrackSectionsMap_patchList[i][j+7];
					
					int tmp;
					for(int k = 0; k < 4; k++) {
						tmp = track_verticalTrackSectionsMap_patchList[i][j + k] - 200;
						
						if ((type & TRACK_STRECH_BIT) != 0) {
							tmp <<= 2;
						}
						
						track_verticalTrackSectionsMap_patchList[i][j + k] = (tmp << 8) / 6;
					}
					
					track_verticalTrackSectionsMap_patchList[i][j + 4] = (track_verticalTrackSectionsMap_patchList[i][j + 4] - 140) >> 2;
					track_verticalTrackSectionsMap_patchList[i][j + 5] = (track_verticalTrackSectionsMap_patchList[i][j + 5] - 140) >> 2;
				}
			}
			
			if (track_verticalTrackSectionsMap_lineList[i] != null) {
				for (int j = 0; j < track_verticalTrackSectionsMap_lineList[i].length; j+= TRACK_INTS_PER_LINE) {
					track_verticalTrackSectionsMap_lineList[i][j    ] = ((track_verticalTrackSectionsMap_lineList[i][j    ] - 200) << 8) / 6;
					track_verticalTrackSectionsMap_lineList[i][j + 2] = ((track_verticalTrackSectionsMap_lineList[i][j + 2] - 200) << 8) / 6;
					track_verticalTrackSectionsMap_lineList[i][j + 1] = (track_verticalTrackSectionsMap_lineList[i][j + 1] - 140) >> 2;
					track_verticalTrackSectionsMap_lineList[i][j + 3] = (track_verticalTrackSectionsMap_lineList[i][j + 3] - 140) >> 2;
				}
			}
		}
		System.gc();
	}
	
	public void loadTrack() {
		track_arrayVxy = new int[0];
		track_arrayAlt = new byte[0];
		
		try {
			//InputStream is = getClass().getResourceAsStream(trackName);
			//DataInputStream dis = new DataInputStream(is);
			
			//byte[] fileData = loadFile(trackName);
			
		//#ifdef com.mygdx.mongojocs.mr2.Debug
			Debug.println("Track data size: "+tmpTrackData.length);
		//#endif

			ByteArrayInputStream bais = new ByteArrayInputStream(tmpTrackData);
			DataInputStream dis = new DataInputStream(bais);
			
			
			//byte[] fileData = loadFile("t4.trk");
			//byte[] fileData = loadFile("rectamuerte.trk");
			//System.out.println("Track data size: "+fileData.length);
			
			//ByteArrayInputStream bais = new ByteArrayInputStream(fileData);
			//DataInputStream dis = new DataInputStream(bais);
			
			//reset();
			//track_reset();
			// read track
			int nvert            = dis.readShort();
		//#ifdef com.mygdx.mongojocs.mr2.Debug
			Debug.println("NumVerts in track: "+nvert);
		//#endif

			//fis.read();
			for (int i = 0; i < nvert; ++i) {
				int px    = dis.readShort();
				int py    = dis.readShort();
				byte alt  = dis.readByte();
				track_addPoint(px, py, alt);
			}
			
			int vpal_size = dis.readShort();
			int[] v_pal = new int[vpal_size];
			for(int i = 0; i < vpal_size; i++) {
				v_pal[i] = dis.readInt();
			}
			    
			int vcol_size = dis.readByte();
			int[] v_col = new int[vcol_size];
			for(int i = 0; i < vcol_size; i++) {
				v_col[i] = dis.readInt();
			}
			
			loadTrackSub(0, dis, v_pal, v_col);
			loadTrackSub(1, dis, v_pal, v_col);
			
			/*loadTrackSub(dis, v_pal, v_col, 0);
			loadTrackSub(dis, v_pal, v_col, 1);*/
			
			//loadTrackSub(dis, v_pal, v_col, track_trackSectionsMap_patchList, track_trackSectionsMap_lineList, track_trackSectionsMap_flags, false, track_typeSectionArray);
			//loadTrackSub(dis, v_pal, v_col, track_verticalTrackSectionsMap_patchList, track_verticalTrackSectionsMap_lineList, track_verticalTrackSectionsMap_flags, true, track_verticalTypeSectionArray);

/*			
			track_verticalTypeSectionArray = new byte[numVertSec];
			for (int i = 0; i < track_verticalTypeSectionArray.length; ++i) {
				track_verticalTypeSectionArray[i] = dis.readByte();
			}
*/			
			v_pal = v_col = null;
			System.gc();
			track_shadeColor 					= dis.readInt();
			track_shadeLerpFactor 		= dis.readInt();
            
			track_startBackgroundTime = dis.readInt();
			track_startSkyColor 			= dis.readInt();
			//#ifdef DOJA
			//#else
			track_startHorizonColor 	= dis.readInt();
			//#endif
			track_endBackgroundTime 	= dis.readInt();
			track_endSkyColor 				= dis.readInt();
			track_endHorizonColor 		= dis.readInt();
			dis.close();
		} catch (Exception e) {	e.printStackTrace(); }
		
		track_calculateLongitudes();
		track_update();
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("Updated track.. all ok");				
	//#endif
	
	//#ifdef RENDER_QUALITY_LOW
	//#endif
	}
	
	public void loadTrackSub(int Option, DataInputStream dis, int[] v_pal, int[] v_col)
	{
		boolean isVert;
		byte[] typeSectionArray;
		try
		{
			isVert = (Option != 0);
	
			// Load TrackSections
			int maxTrackSects = dis.readByte();
			int numTrackSects = dis.readByte();
			
			if (!isVert) {
				track_trackSectionsMap_patchList 	= new int[maxTrackSects+numTrackSects][];
				track_trackSectionsMap_lineList 	= new int[maxTrackSects+numTrackSects][];
				track_trackSectionsMap_flags 			= new byte[maxTrackSects+numTrackSects];
			} else {
				track_verticalTrackSectionsMap_patchList 	= new int[maxTrackSects+numTrackSects][];
				track_verticalTrackSectionsMap_lineList 	= new int[maxTrackSects+numTrackSects][];
				track_verticalTrackSectionsMap_flags 			= new byte[maxTrackSects+numTrackSects];
			}
			
			
			//track_trackSectionsMap = new TrackSection[maxTrackSects+numTrackSects]; // hack
				
			for (int i = 0; i < numTrackSects; ++i) 
			{
				int intKey = dis.readByte();
				int flags = dis.readInt();
					
				int[] pl = new int[dis.readShort()];
				for (int k = 0; k < pl.length; ++k)	
				{
					int idx = k % TRACK_INTS_PER_PATCH;
					int pal_i = dis.readByte();
					if(pal_i < 0) pal_i += 256;
					switch(idx) 
					{
						case 0: case 1: case 2: case 3: case 4: case 5:
							pl[k] = v_pal[pal_i];
						break;
						case 6: case 8:
							pl[k] = v_col[pal_i];
						break;
						default:
							pl[k] = pal_i;
					}
				}
					
				int[] ll = new int[dis.readShort()];
				for (int k = 0; k < ll.length; ++k)	
				{
					int idx = k % TRACK_INTS_PER_LINE;
					int pal_i = dis.readByte();
					if(pal_i < 0) pal_i += 256;
					switch(idx) 
					{
						case 0: case 1: case 2: case 3:
							ll[k] = v_pal[pal_i];
						break;
						case 4: case 5:
							ll[k] = v_col[pal_i];
						break;
						default:
							ll[k] = pal_i;
					}
				}
				//createTrackSection(flags, pl, ll, intKey, isVert);
				
				int realPL = 0;
				int realLL = 0;
				
				if(!isVert) {
					//byte flags = ((byte)flgs);
					int[] patchList = null;
					int[] lineList = null;
					
					if(pl == null) {
						patchList = null;
					}  else {
						for(int j = 0; j < pl.length; j += TRACK_INTS_PER_PATCH) {
							if(pl[j + TRACK_INTS_PER_PATCH - 1] <= max_detail) {
								realPL++;
							}
						}
						patchList = new int[realPL * TRACK_INTS_PER_PATCH];
						realPL = 0;
						for(int j = 0; j < pl.length; j += TRACK_INTS_PER_PATCH) {
							if(pl[j + TRACK_INTS_PER_PATCH - 1] <= max_detail) {
								for(int k = 0; k < TRACK_INTS_PER_PATCH; k++) {
									patchList[realPL + k] = pl[j + k];
								}
								realPL += TRACK_INTS_PER_PATCH;
							}
						}
					}
					
					if(ll == null) {
						lineList = null;
					}  else {
						for(int j = 0; j < ll.length; j += TRACK_INTS_PER_LINE) {
							if(ll[j + TRACK_INTS_PER_LINE - 1] <= max_detail) {
								realLL++;
							}
						}
						lineList = new int[realLL * TRACK_INTS_PER_LINE];
						realLL = 0;
						for(int j = 0; j < ll.length; j += TRACK_INTS_PER_LINE) {
							if(ll[j + TRACK_INTS_PER_LINE - 1] <= max_detail) {
								for(int k = 0; k < TRACK_INTS_PER_LINE; k++) {
									lineList[realLL + k] = ll[j + k];
								}
								realLL += TRACK_INTS_PER_LINE;
							}
						}
					}
					track_trackSectionsMap_patchList[intKey] = patchList;
					track_trackSectionsMap_lineList[intKey] = lineList;
					track_trackSectionsMap_flags[intKey] = ((byte)flags);
				} else {
					track_verticalTrackSectionsMap_patchList[intKey] = pl;
					track_verticalTrackSectionsMap_lineList[intKey] = ll;
					track_verticalTrackSectionsMap_flags[intKey] = ((byte)flags);
				}

		
			}
				
			// load section assigs
			int numSec = dis.readShort();
			typeSectionArray = new byte[numSec];
			if((numSec % 2) != 0) 
			{
				numSec--;
			}
			for (int i = 0; i < numSec; i += 2) 
			{
				int pck = dis.readByte();
				if(pck < 0) pck += 256;
				typeSectionArray[i] = (byte) (pck >> 4);
				typeSectionArray[i + 1] = (byte) (pck & 0xf);
			}
						
			if(numSec != typeSectionArray.length) 
			{
				typeSectionArray[typeSectionArray.length - 1] = dis.readByte();
			}
			
			if(!isVert) {
				track_typeSectionArray = typeSectionArray;
			} else {
				track_verticalTypeSectionArray = typeSectionArray;
			}
			
			/*
			if (Option == 0)
			{
				track_trackSectionsMap_patchList							= trackSectionsMap_patchList;
				track_trackSectionsMap_lineList								= trackSectionsMap_lineList;
				track_trackSectionsMap_flags									= trackSectionsMap_flags;
				track_typeSectionArray												= typeSectionArray;
			} else
			{
				track_verticalTrackSectionsMap_patchList			= trackSectionsMap_patchList; 
				track_verticalTrackSectionsMap_lineList 			= trackSectionsMap_lineList; 
				track_verticalTrackSectionsMap_flags 					= trackSectionsMap_flags;
				track_verticalTypeSectionArray								= typeSectionArray;
			}
			*/

			
		} catch (Exception e) {	e.printStackTrace(); } 
	}
	
	
	// Track
	// Vert Track
	/*public void loadTrackSub(DataInputStream disTmp, int[] v_palTmp, int[] v_colTmp, int Bloque)
	//public void loadTrackSub(DataInputStream dis, int[] v_pal, int[] v_col, int[][] SectionsMap_patchList, int[][] SectionsMap_lineList, byte[] SectionsMap_flags, boolean isVert, byte[] track_typeSectionArrayTmp)
	{
		int[][] SectionsMap_patchListTmp; 
		int[][] SectionsMap_lineListTmp;
		byte[] SectionsMap_flagsTmp;
		boolean isVert;
		byte[] track_typeSectionArrayTmp;
		
		try
		{
			if(Bloque==0)
			{
				SectionsMap_patchListTmp	= track_trackSectionsMap_patchList; 
				SectionsMap_lineListTmp 	= track_trackSectionsMap_lineList;
				SectionsMap_flagsTmp			= track_trackSectionsMap_flags;
				isVert										= false;
				track_typeSectionArrayTmp	= track_typeSectionArray;
			} else
			{
				SectionsMap_patchListTmp	= track_verticalTrackSectionsMap_patchList; 
				SectionsMap_lineListTmp 	= track_verticalTrackSectionsMap_lineList;
				SectionsMap_flagsTmp			= track_verticalTrackSectionsMap_flags;
				isVert										= true;
				track_typeSectionArrayTmp	= track_verticalTypeSectionArray;
			}
			
			// Load TrackSections
			int maxSects = disTmp.readByte();
			int numSects = disTmp.readByte();
				
			SectionsMap_patchListTmp 	= null;
			SectionsMap_lineListTmp 	= null;
			SectionsMap_flagsTmp 			= null;
			System.gc();
			
			SectionsMap_patchListTmp 	= new int[maxSects+numSects][];
			SectionsMap_lineListTmp		= new int[maxSects+numSects][];
			SectionsMap_flagsTmp 			= new byte[maxSects+numSects];
				//track_trackSectionsMap = new TrackSection[maxTrackSects+numTrackSects]; // hack
				
				for (int i = 0; i < numSects; ++i) {
					
					int intKey = disTmp.readByte();
					int flags = disTmp.readInt();
					
					int[] pl = new int[disTmp.readShort()];
					for (int k = 0; k < pl.length; ++k)	{
						int idx = k % TRACK_INTS_PER_PATCH;
						int pal_i = disTmp.readByte();
						if(pal_i < 0) pal_i += 256;
						switch(idx) {
							case 0: case 1: case 2: case 3: case 4: case 5:
								pl[k] = v_palTmp[pal_i];
								break;
							case 6: case 8:
								pl[k] = v_colTmp[pal_i];
								break;
							default:
								pl[k] = pal_i;
						}
					}
					
					int[] ll = new int[disTmp.readShort()];
					for (int k = 0; k < ll.length; ++k)	{
						int idx = k % TRACK_INTS_PER_LINE;
						int pal_i = disTmp.readByte();
						if(pal_i < 0) pal_i += 256;
						switch(idx) {
							case 0: case 1: case 2: case 3:
								ll[k] = v_palTmp[pal_i];
								break;
							case 4: case 5:
								ll[k] = v_colTmp[pal_i];
								break;
							default:
								ll[k] = pal_i;
						}
					}
					createTrackSection(flags, pl, ll, intKey, isVert);
				}
				
				// load section assigs
				int numSec = disTmp.readShort();
				track_typeSectionArrayTmp = new byte[numSec];
				if((numSec % 2) != 0) { numSec--; }
				for (int i = 0; i < numSec; i += 2) {
					int pck = disTmp.readByte();
					if(pck < 0) pck += 256;
					track_typeSectionArrayTmp[i] = (byte) (pck >> 4);
					track_typeSectionArrayTmp[i + 1] = (byte) (pck & 0xf);
				}
						
				if(numSec != track_typeSectionArrayTmp.length) {
					track_typeSectionArrayTmp[track_typeSectionArrayTmp.length - 1] = disTmp.readByte();
				}
						
				// Load VertTrackSections
				//int maxVertTrackSects = dis.readByte();
				//int numVertTrackSects = dis.readByte();
	
				/*
				track_verticalTrackSectionsMap_patchList 	= null; 
				track_verticalTrackSectionsMap_lineList 	= null; 
				track_verticalTrackSectionsMap_flags 			= null;
				System.gc();
				
				track_verticalTrackSectionsMap_patchList = new int[maxVertTrackSects+numVertTrackSects][];
				track_verticalTrackSectionsMap_lineList = new int[maxVertTrackSects+numVertTrackSects][];
				track_verticalTrackSectionsMap_flags = new byte[maxVertTrackSects+numVertTrackSects];
				//track_verticalTrackSectionsMap = new TrackSection[maxVertTrackSects+numVertTrackSects]; // hack
				*/
				
				/*for (int i = 0; i < numVertTrackSects; ++i) {
					
					int intKey = dis.readByte();
					int flags = dis.readInt();
					*/
					/*
					int[] pl = new int[dis.readShort()];
					for (int k = 0; k < pl.length; ++k)	{
						int idx = k % TRACK_INTS_PER_PATCH;
						int pal_i = dis.readByte();
						if(pal_i < 0) pal_i += 256;
						switch(idx) {
							case 0: case 1: case 2: case 3: case 4: case 5:
								pl[k] = v_pal[pal_i];
								break;
							case 6: case 8:
								pl[k] = v_col[pal_i];
								break;
							default:
								pl[k] = pal_i;
						}
					}
					*//*
					int[] ll = new int[dis.readShort()];
					for (int k = 0; k < ll.length; ++k)	{
						int idx = k % TRACK_INTS_PER_LINE;
						int pal_i = dis.readByte();
						if(pal_i < 0) pal_i += 256;
						switch(idx) {
							case 0: case 1: case 2: case 3:
								ll[k] = v_pal[pal_i];
								break;
							case 4: case 5:
								ll[k] = v_col[pal_i];
								break;
							default:
								ll[k] = pal_i;
						}
					}
					createTrackSection(flags, pl, ll, intKey, true);
				}
				*//*
				// load vertical section assigs
				int numVertSec = dis.readShort();
				
						track_verticalTypeSectionArray = new byte[numVertSec];
						if((numVertSec % 2) != 0) {
					numVertSec--;
						}
						
						for (int i = 0; i < numVertSec; i += 2) {
					int pck = dis.readByte();
					if(pck < 0) pck += 256;
					track_verticalTypeSectionArray[i] = (byte) (pck >> 4);
					track_verticalTypeSectionArray[i + 1] = (byte) (pck & 0xf);
						}
						
						if(numVertSec != track_verticalTypeSectionArray.length) {
					track_verticalTypeSectionArray[track_verticalTypeSectionArray.length - 1] = dis.readByte();
						}
						*/
	/*	} catch (Exception e) {	e.printStackTrace(); }
	}*/

	public void startRace() {
		//curvepoints = null;
		//canvasImg = null;
		//relojImg = null;
		
		
		//#ifdef FREE_MENUS
		//#endif
		
		System.gc();
		
		loadInitTrack();
		raceTime = -2999;
		
		myVehicle = new FwdVehicle(false);
		lastLPusid = -1;
		myVehicle.init();
		
		phantom = new FwdVehicle(true);
		phantomTick = 0;
		
		if(tmpGhostData != null) {
			phantom.phantom_tracking = convByte2Short(tmpGhostData);
			phantom.phantom_setPhantomTrack(tmpGhostTime);
		}
		phantom.phantom_resetTracking();
		
		lapCounter = 0;
		endRace = 0;
		endRaceTime = 0;
		tickCount = 0;
		lastTickCount = 0;
		deltaTime = 0;
		lastDeltaTime = 0;
		lapTime = 0;
		lastLapTime = 0;
		lastArrowTime = -TRACK_ARROW_THRESHOLD;
		lastBoostTime = -BOOST_THRESHOLD;
		//synchronized(tDifMutex) {
			menuStart = -1;
			timeDiff = 0;
			//timeDiff2 = 0;
		//}
		System.gc();
		
		//#ifndef NO_SOUND_IN_GAME
		soundPlay(SOUND_COUNTDOWN,1);
		//#endif
		
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		
		//#ifdef com.mygdx.mongojocs.mr2.Debug
		System.out.println("contraOponente? " + gameContraOponente);
		System.out.println("pelaoTime: " + pelaoTime);
		System.out.println("tmpGhostTime: " + tmpGhostTime);
		//#endif
		
		ghostBeaten = false;
		
		//#ifdef BIG_PANEL
		//#endif
	}
	
	//#ifdef BIG_PANEL
	//#endif
	
	public void releaseTrack() {
		phantom = null;
		//!!!
		System.gc();
	}
	
	public long getTimeDiff() {
		long tlapsed = (tickCount - lastTickCount) * 200 + deltaTime - lastDeltaTime;
		lastDeltaTime = deltaTime;
		lastTickCount = tickCount;
		return (int) tlapsed;
	}

	
	public void raceTick() { 
		long deltaTimeFrame = getTimeDiff();
		raceTime += deltaTimeFrame;
		
		if(raceTime >= 0) {
			lapTime += deltaTimeFrame;
			if(endRace == 1) return;
			
			if(myVehicle.crashSound) {
				//#ifndef NO_SOUND_IN_GAME
				soundPlay(SOUND_CRASH,1);
				//#endif
				vibraInit(250);
				myVehicle.crashSound = false;
			}
			// perform all game logic
			int vu = (myVehicle.usid + 2) % (track_vertexSectionArray.length / 3);
			int su = 0;	//goal is always at usid 0 !!
			//System.out.println(lastLPusid + " " + vu + " ,, " + su);
			if((lastLPusid > (su + 1)) && (vu == su || vu == (su + 1))) {
				lapCounter++;
				//#ifndef NO_SOUND_IN_GAME
				soundPlay(SOUND_FINISH,1);
				//#endif	
			//#ifdef com.mygdx.mongojocs.mr2.Debug
				Debug.println("lap++");
				Debug.println(lapTime + " vs " + bestTime);
			//#endif

				if(lapCounter > 1)  {
					lastLapTime = lapTime;
					if(lapTime < bestTime) {
						bestTime = lapTime;
						if((gameContraOponente && (lapTime < tmpGhostTime)) || (!gameContraOponente && (lapTime < pelaoTime))) {
							phantom.phantom_addKey(myVehicle.w_subusid, myVehicle.w_latdisplace);
							phantom.phantom_setBestTrack((int) lapTime);
							ghostBeaten = true;
							//tmpGhostNick = pelaoNick;
							//#ifdef com.mygdx.mongojocs.mr2.Debug
							Debug.println("setting best track");
							//#endif
						} else {
							phantom.phantom_resetTracking();
						}
						phantomTick = 0;
					} else {
						phantom.phantom_resetTracking();
						phantomTick = 0;
					}
				} else {
					phantom.phantom_resetTracking();
					phantomTick = 0;
				}
				//#ifdef CHEATS
				//if(((lapCounter > 3 && !cheatsEnabled) || (lapCounter > 1 && cheatsEnabled))&& endRace == 0) {
				//#else
				if(lapCounter > 3 && endRace == 0) {
				//#endif
					myVehicle.forceBrake = true;
					lapCounter = 3;
					endRace = 1;
					endRaceTime = 0;
				}
				lapTime = 0;
			}
			
			lastLPusid = vu;
			
			if((myVehicle.status > 0 && keyY != 0) || myVehicle.status <= 0) myVehicle.status = -keyY;
			myVehicle.wheelRotation = keyX;
		}
	}







// ----------------------------------------------------------------------------
	public void loadData() {
		horizonImg = horizonteImg;
		
		System.gc();
		
		//#ifdef com.mygdx.mongojocs.mr2.Debug
		System.out.println("loading data");
		//#endif
		
		//#ifdef NO_UNLOAD
		//#endif
		
		try {
		//#ifdef J2ME
			//horizonImage = loadImage("/horizonte");
			
			//#ifdef BIG_MOTO
			//#endif
			
			//#ifndef FILE_PACK
				//#ifndef NO_ROLL
				panelImage = loadImage("/panel");
				//#endif
			
				//#ifdef NK-s40
				//#else
					trackArrows = loadImage("/trackarrows");
					panel0 = loadImage("/panel0");
					panel1 = loadImage("/panel1");
					panelSem = loadImage("/panel2");
					font0 = loadImage("/font0");
					font1 = loadImage("/font1");
					semR = loadImage("/sem0");
					semG = loadImage("/sem1");
					arrow = loadImage("/arrow");
				//#endif
			//#else
			//#endif
			
			//#ifndef CARS_S40
			//#ifndef FILE_PACK
			bikeImageB = loadImage("/motoB");
			//#else
			//#endif
			//#endif
			
			//#ifdef com.mygdx.mongojocs.mr2.Debug
			System.out.println("pre loading moto");
			//#endif
			
			//#ifndef FILE_PACK
			//#ifdef J2ME
				bikeImageC = loadImage("/motoC");
				bikeImageD = loadImage("/motoD");
			//#endif
			//#else
			//#endif
			
			//#ifdef com.mygdx.mongojocs.mr2.Debug
			System.out.println("post loading moto");
			//#endif
			
			//#ifndef FEW_FRAMES
			//#ifndef CARS_S40
			//#ifndef FILE_PACK
			bikeWheelB = loadImage("/motoBwheel");
			//#ifdef BIG_MOTO
			//#else
			turbo = loadImage("/turboB");
			smoke = loadImage("/smokeB");
			//#endif
			//#else
			//#endif
			
			//#endif
			//#endif
			
			//#ifndef CARS_S40
			//#ifndef FILE_PACK
			bikeBcoord = loadFile("/motoB.cor");
			//#else
			//#endif
			//#endif
			
			//#ifndef FILE_PACK
			bikeCcoord = loadFile("/motoC.cor");
			bikeDcoord = loadFile("/motoD.cor");
			//#else
			//bikeCcoord = loadFile(GFX_MOTOC_COR);
			//bikeDcoord = loadFile(GFX_MOTOD_COR);
			//#endif
			
			//#ifndef FEW_FRAMES
			//#ifndef CARS_S40
			//#ifndef FILE_PACK
			bikeWBcoord = loadFile("/motoBwheel.cor");
			//#ifdef BIG_MOTO
			//#else
			turboCoord = loadFile("/turboB.cor");
			smokeCoord = loadFile("/smokeB.cor");
			//#endif
			//#else
			//#endif
			
			//#endif
			//#endif
			
		//#elifdef DOJA
		//#endif

			System.gc();
		} catch(Exception e) {
			//#ifdef com.mygdx.mongojocs.mr2.Debug
			Debug.println("Exception: " + e.getMessage());
			//#endif
		}
		//#ifdef NO_UNLOAD
		dataLoaded = true;
		//#endif
	}
	
	public void freeData() {
		//#ifndef NO_UNLOAD
		//horizonImage = null;
		bikeImageA = null;
		bikeImageB = null;
		bikeImageC = null;
		bikeImageD = null;
		panelImage = null;
		turbo = null;
		smoke = null;
		//#ifndef NK-s40
		trackArrows = null;
		panel0 = null;
		panel1 = null;
		panelSem = null;
		font0 = null;
		font1 = null;
		//#ifdef J2ME
			semR = null;
			semG = null;
		//#endif
		arrow = null;
		//#else
		//#endif
		bikeWheelB = null;
		bikeAcoord = null;
		bikeBcoord = null;
		bikeCcoord = null;
		bikeDcoord = null;
		turboCoord = null;
		smokeCoord = null;
		bikeWBcoord = null;
		//#endif
		
		//phantom
		phantom.phantom_tracking = null;
		phantom.phantom_bestTrack = null;
		phantom.phantom_phantom = null;
		
		//track
		track_arrayVxy = null;
		track_arrayAlt = null;
		track_arrayLong = null;
		track_vertexSectionArray = null;
		track_typeSectionArray = null;
		track_verticalTypeSectionArray = null;
		track_trackSectionsMap_patchList = null;
		track_trackSectionsMap_lineList = null;
		track_trackSectionsMap_flags = null;
		track_verticalTrackSectionsMap_patchList = null;
		track_verticalTrackSectionsMap_lineList = null;
		track_verticalTrackSectionsMap_flags = null;
		System.gc();
	}
	
	/**
	 *  Renders track
	 *
	 * @param  g       graphics objects
	 */
	public void render(Graphics g, boolean flash) {
		int	fps = 0;
		int 	usid = 0;
		int	vehicleUsid = 0;
		int	ifov = 0;
		
		//#ifdef J2ME
		renderFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		g.setFont(renderFont);
		//#endif
		frameCounter++;
		
		heightCanvas = canvasHeight;
		gBuffer = g;

		//#ifdef J2ME
		//#ifdef BIG_PANEL
		//#endif
		
		//long msRender = System.currentTimeMillis();
		//System.out.println(" game is not null");
		
		usid = myVehicle.w_subusid >> 7;
		
		//height = 80;
		
		
		//restore old game mode after a crash
		if(myVehicle.restoreGM) {
			myVehicle.restoreGM = false;
			gameMode = myVehicle.lastGameMode;
		}
		
		//Change to 3rd person view if there are a collision.
		if(myVehicle.lateralFall || myVehicle.collision) {
			gameMode = 0;
		} else {
			myVehicle.lastGameMode = gameMode;
		}
		
		//TO TEST
		//test if changing the height provokes changes in the results
		//TO TEST
		//#ifndef NO_ROLL
		if(gameMode == 0) {
			height = 80;
		} else {
			height = 55;
		}
		//#else
		//#endif
		//TO TEST
		
		if(myVehicle.haltBoost) {
			lastBoostTime -= BOOST_THRESHOLD + 1;
			myVehicle.haltBoost = false;
		}
		
		boostMode = (tickCount - lastBoostTime < BOOST_THRESHOLD);
		myVehicle.boostMode = boostMode;
		/*
		if(boostMode && !myVehicle.boostMode) {
			myVehicle.boostMode = true;
			myVehicle.boostTick = 0;
		}
		
		if(!boostMode) {
			myVehicle.boostMode = false;
			myVehicle.boostTick = 0;
		}
		*/
		vehicleUsid = myVehicle.getRealUsid(gameMode);
		
		//#ifdef com.mygdx.mongojocs.mr2.Debug
		if(raceTime <= 0) {
			frames = 0;
		} else {
			fps = (int) ((100000 * frames) / raceTime);
			DebugX.fps = fps;
		}
		frames++;
		//#endif
		
		int camHeight = height;
		
		ifov = fov - (myVehicle.fov >> 3);
		camHeight -=  (myVehicle.fov >> 4);
		
		ifov = ifov * canvasWidth/128;		
		
		// draw background
		//
		//g.setClip(0,0, width, height);
		//g.setColor(0x80E0F0);
		//g.fillRect(0, 0, width, height);
						
						//long msCam = System.currentTimeMillis();
						
		int shadeLerpInvFactor = 65536/track_shadeLerpFactor;
		int oldshadelerp = 0;
		int lerpCol = track_shadeColor; //0x808080;
				
		int curValue = myVehicle.getXYZ((byte)(2));
		//int lateralDisplace = myVehicle.getLateralDisplace();
		// DOJAPORT - getLateralDisplace
		int lateralDisplace = myVehicle.w_latdisplace >> 8;
		
		// get vehicle pos
		int pvx = myVehicle.getXYZ((byte)(0));
		int pvy = myVehicle.getXYZ((byte)(1));
		int pvz = myVehicle.getXYZ((byte)(2));
		
		int pdirx = myVehicle.getDirX();
		int pdiry = myVehicle.getDirY();
		int pdirz = myVehicle.getDirZ();
		
			// Correct displacement
//!!!!!!!
//			pvx += myVehicle.ang*toInt(pdiry*2048)/4096;
//			pvy -= myVehicle.ang*toInt(pdirx*2048)/4096;
				
		int width2 = canvasWidth >> 1;
		int height2 = canvasHeight >> 1;
		
		//#ifndef CARS_S40
		ydispl = -(myVehicle.boostTick << 1);
		//#else
		//#endif
		
		// draw track
		//
		
		//int ipdl = div(ONE, sqrt(mul(pdirx, pdirx) + mul(pdiry, pdiry)));
		
		//int CAMHEIGHT = camHeight / 5; //10;
		int CAMHDIVFACTOR = 50;
		
		calcMatrixCamera(0, toFP(camHeight / 5)/CAMHDIVFACTOR, 0,
		                    pdiry, toFP(pitch)/10, pdirx);
						
		int off = 0;	//toInt(myVehicle.fpViewAng*64);

		//#ifndef RENDER_QUALITY_LOW
		//#ifndef NO_ROLL
		int rollDeg = myVehicle.w_roll;
		if(gameMode == 0) {
			rollDeg = rollDeg >> 2;
		}
		
		int sRoll = sin(rollDeg);
		int cRoll = cos(rollDeg); 
		//#endif
		//#endif
		
		// draw sections ahead
		//
		int lastvttz = -60000;
		int indexVertexs = 0;
		int max = TRACK_NUM_SECTIONS_AHEAD;
		
		//si la moto esta amb el turbo, dibuixar menys seccions pq vagi mes rapid
		if(boostMode) max -= TRACK_SECTIONS_CUT;
		
		if(!flash) {
/*
 *
 * XForm Vertexs
 *
 *
 */
 		// init phantom
		boolean isPhantomVisible = false;
		boolean isNextPusid = false;
 
 		int pusid = -1;
		int pusubusid;
		int interpusid=0;
		if(phantom != null) {
			pusid = phantom.w_subusid >> 7;
			pusubusid = phantom.w_subusid;
			interpusid = pusubusid - (pusid<<7);
			pusid %= track_vertexSectionArray.length / 3;
		}
		
		int msid = ((usid + max + (track_vertexSectionArray.length - 4)) * 3) % track_vertexSectionArray.length;
		if(pusid >= msid && pusid < msid + 3) {
			max += 2;
		}
			
		for(int i = 0; i < max; ++i) {
			int sid = (usid + i + (track_vertexSectionArray.length - 4)) * 3;
			sid %= track_vertexSectionArray.length;		// hack
			
			int fvx = track_vertexSectionArray[sid];	//posArrayX[i];
			int fvy = track_vertexSectionArray[sid+1];	//posArrayY[i];
			int fh  = track_vertexSectionArray[sid+2];	//posArrayZ[i];
			int fvz  = toInt(fh);
			
			int sectype = track_typeSectionArray[sid / 3];
			int vertsectype = track_verticalTypeSectionArray[sid / 3];

			int vttx = fvy;
			int vtty = fvz;
			int vttz = fvx;
			
			int tvttx = pvy;
			int tvtty = camHeight / 5;
			int tvttz = pvx;
			
			vttx -= tvttx;
			vtty -= tvtty;
			vttz -= tvttz;
			
			vtty -= toInt(pvz);
			
			xformVector(vttx, vtty, vttz);
			
			vttx = xformX;
			vtty = xformY;
			vttz = xformZ;
			
			vtty += tvtty;
			
			vttz = vttz - lastvttz < 2 ? lastvttz+2 : vttz;
			
			
			int vtz = vttz > 48 ? 42+(vttz-48>>3) : (-vttz*vttz>>8)+vttz;
			
			if(gameMode == 0) {
				xformedVertexs[indexVertexs] = vttx*vtz/10 + (lateralDisplace >> 1);
			} else {
				xformedVertexs[indexVertexs] = vttx*vtz/10 + (4 * lateralDisplace / 5);
			}
			xformedVertexs[indexVertexs+1] = vtty;
			xformedVertexs[indexVertexs+2] = vttz;
			xformedVertexs[indexVertexs+3] = sectype + (vertsectype<<8);
			//System.out.println("p: " + pusid + " o: " + (sid / 3));
			if(pusid == (sid / 3)) {
				isPhantomVisible = true;
				isNextPusid = true;
				phantomx = vttx*vtz/10;
				phantomy = vtty;
				phantomz = vttz;
			} else if (isNextPusid) {
				isNextPusid = false;
				phantomx = (interpusid * ((vttx*vtz/10)-phantomx) >>7) + phantomx;
				phantomy = (interpusid * (vtty - phantomy) >> 7) + phantomy;
				phantomz = (interpusid * (vttz - phantomz) >> 7) + phantomz;
			}

			lastvttz = vttz;
			indexVertexs+=4;
		}
		
		//#ifdef com.mygdx.mongojocs.mr2.Debug
			//com.mygdx.mongojocs.mr2.DebugX.x = (int)(System.currentTimeMillis() - msXform);
		//#endif
		
		// DOJAPORT
		// ---------
		/*
		int lpty = xformedVertexs[TRACK_NUM_SECTIONS_AHEAD*4+1-4];
		int lptz = xformedVertexs[TRACK_NUM_SECTIONS_AHEAD*4+2-4];
		*/
		int lpty = xformedVertexs[TRACK_NUM_SECTIONS_AHEAD*4-3];
		int lptz = xformedVertexs[TRACK_NUM_SECTIONS_AHEAD*4-2];
		// ----------
		if (lptz <= 0) lptz = 1;
		
		int lpjyz = (( ifov * lpty )/ lptz) >> 2;
		lpjyz = lpjyz > 6 ? 6 : lpjyz;
		
		//#ifdef HORIZON_QUALITY_0
		//#else
				int lastYproj = height2+ (height2/3) + lpjyz;
		//#endif
/*
 *
 * Draw Sky Gradient
 *
 *
 */
	//#ifdef SKY_GRADIENT_LO
		//#ifdef J2ME
		g.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		int sizeY = canvasHeight / 10;
		int startR = (track_startSkyColor >> 16) & 0xff;
		int startG = (track_startSkyColor >>  8) & 0xff;
		int startB = (track_startSkyColor >>  0) & 0xff;
		
		int dR = (((track_startHorizonColor >> 16) & 0xff) - startR) / 10;
		int dG = (((track_startHorizonColor >>  8) & 0xff) - startG) / 10;
		int dB = (((track_startHorizonColor >>  0) & 0xff) - startB) / 10;
		
		for(int i = 0; i < 10; i++) {
			g.setColor((startR << 16) | (startG << 8) | (startB));
			g.fillRect(0, i * sizeY + ydispl, canvasWidth, sizeY);
			startR += dR;
			startG += dG;
			startB += dB;
		}
	//#endif

	//#ifndef RENDER_QUALITY_LOW
	//#ifndef NO_ROLL
		int httx = cRoll;
		int htty = -sRoll;
	
		int uttx = sRoll;
		int utty = cRoll;
	//#endif
	//#endif
		
	//#ifdef SKY_GRADIENT_HI
	//#endif
	
	//#ifdef SKY_FLAT
	//#endif

/*
 *
 * Draw Horizon
 *
 *
 */
 
 	//#ifdef CARS_S40
	//#endif
	
	//#ifndef NO_HORIZON
		if (horizonImg != null) {
	//DOJAPORT //#ifdef J2ME
			int lastYprojImg = lastYproj - horizonImg.getHeight();
			
			//off = toInt(div(myVehicle.w_fpviewang, PI) * (horizonImage.getWidth() >> 2));
			
			//#ifdef DOJA
			// DOJAPORT
			//#else
			off = toInt(div(myVehicle.w_fpviewang, PI << 1) * horizonImg.getWidth());
			//#endif
			while (off < 0) off += horizonImg.getWidth();
			off %= horizonImg.getWidth();
			
			//System.out.println(off);
			//System.out.println(off);
			
			
			//toca
			//off = toInt(myVehicle.w_fpviewang * 32);
			//while (off < 0) off += horizonImage.getWidth();
			//off %= horizonImage.getWidth();

			
			
			int x00 = off - horizonImg.getWidth();
			int x01 = off;
			int x10 = off;
			int x11 = off + horizonImg.getWidth();
		//#ifdef HORIZON_QUALITY_0
		//#else
			int yHorizStart = lastYprojImg + ydispl;
			int iY = mul(sRoll, toFP(canvasWidth / 2));
			int iYi = iY;
			int dy = sRoll;

			for(int i = 0; i < canvasWidth / HORIZON_CUT_SIZE + 1; i++) {
				int aY = toInt(iY);
				//#ifdef BIG_PANEL
				//#else
				g.setClip(i * HORIZON_CUT_SIZE, 0, HORIZON_CUT_SIZE, canvasHeight);
				//#endif
				if(!(x10 <= 0 && x11 > canvasWidth)) g.drawImage(horizonImg, off - horizonImg.getWidth(), yHorizStart + aY, Graphics.LEFT | Graphics.TOP);
				if(!(x00 <= 0 && x01 > canvasWidth)) g.drawImage(horizonImg, off, yHorizStart + aY, Graphics.LEFT | Graphics.TOP);
				iY -= dy * HORIZON_CUT_SIZE;
			}
			
			/*
			//Tripada de Jordi -- too slow
			int yHorizStart = lastYprojImg;
			for(int i = 0; i < width / 16 + 1; i++) {
				for(int j = -2; j < horizonImg.getHeight() / 16 + 1 + 2; j++) {
					int nx = toFP(i * 16 - width2);
					int ny = toFP(j * 16 - horizonImg.getHeight() / 2);
					
					int kx = toInt( mul(nx, cRoll) + mul(ny, sRoll));
					int ky = toInt(-mul(nx, sRoll) + mul(ny, cRoll));
					
					g.setClip(i * 16 - 2, yHorizStart + j * 16 - 2, 20, 20);
					g.drawImage(horizonImg, off - horizonImg.getWidth() + kx - i * 16 + width2 - 2, yHorizStart + ky - j * 16 + horizonImg.getHeight() / 2 - 2, Graphics.LEFT | Graphics.TOP);
					g.drawImage(horizonImg, off + kx - i * 16 + width2 - 2, yHorizStart + ky - j * 16 + horizonImg.getHeight() / 2 - 2, Graphics.LEFT | Graphics.TOP);
				}
			}
			*/
		//#endif
	//DOJAPORT //#endif
		}
		//#endif

		//System.out.println(" horizon is ok");
		
		int lerpColRB = lerpCol & 0xff00ff;
		int lerpColG  = lerpCol & 0x00ff00;
		
		skipped = 0;
		
		//#ifdef J2ME
		//#ifdef BIG_PANEL
		//#else
		g.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		//#endif
		
/*
 *
 * Render Track
 *
 *
 */
		//#ifdef com.mygdx.mongojocs.mr2.Debug
			long mssr = System.currentTimeMillis();
		//#endif

//#ifdef RENDER_QUALITY_LOW

//#else
		//#ifdef J2ME
		
		//#ifdef BIG_PANEL
		//#ifdef CARS_S40
		//#else
		g.setClip(0, 0, canvasWidth, canvasHeight + ydispl);
		//#endif
		//#endif
		
		//for(int i = max - 1; i > 5; --i) {
		int lerp = shadeLerpInvFactor * xformedVertexs[((max - 1) << 2)+2] >>10;
		for(int i = max - 1; i > 5; --i) {
			int indx = i<<2;
			int ottx = xformedVertexs[indx-4];
			int otty = xformedVertexs[indx-4+1];
			int ottz = xformedVertexs[indx-4+2];
			
			int vttx = xformedVertexs[indx];
			int vtty = xformedVertexs[indx+1];
			int vttz = xformedVertexs[indx+2];
			
			int sectype = xformedVertexs[indx+3] & 0xff;
			int vertsectype = (xformedVertexs[indx+3]>>8) & 0xff;

			//TrackSection ts = (TrackSection) track_trackSectionsMap[sectype];
			if(vertsectype < 0 || vertsectype >= track_verticalTrackSectionsMap_patchList.length) {
				vertsectype = 0;
			}
			//TrackSection vts = (TrackSection) track_verticalTrackSectionsMap[vertsectype];
			
			//if (ts == null) continue;
			int[] patchList = track_trackSectionsMap_patchList[sectype];
			int[] lineList = track_trackSectionsMap_lineList[sectype];
			
			if (vttz > 0) {
				if (ottz <= 0)		ottz = 1;
				
				int dttx = ottx - vttx;
				int dtty = otty - vtty;
				int dttz = ottz - vttz;
				
				//int xdttx = -dttz;
				//int xdttz =  dttx;
				
				int vsttx = vttx << 8;
				int vstty = vtty << 6;
				int vsttz = vttz << 6;
				
				int oldlerp = lerp;
				lerp = shadeLerpInvFactor*vttz >>10;

				if (lerp > 255) lerp = 255;
				else if (lerp < 0) lerp = 0;
				
				int xlt,xrt,xlb,xrb,yi,yf,col,type,altCol,x0,y0,x1,y1,x2,y2,x3,y3,xi,xf;
				
				for (int j = 0; j <patchList.length; j+= TRACK_INTS_PER_PATCH) {
					xlt = patchList[j] << 1;
					xrt = patchList[j+1] << 1;
					xlb = patchList[j+2] << 1;
					xrb = patchList[j+3] << 1;
					yi = patchList[j+4];
					yf = patchList[j+5];
					col = patchList[j+6];
					type = patchList[j+7];
					altCol = patchList[j+8];
					
					int ptdz = ((dttz*yi)>>10) + vsttz;
					int otdz = ((dttz*yf)>>10) + vsttz;
					
					if(ptdz <= 0) {
						ptdz = 1;
					}
					if(otdz <= 0) {
						otdz = 1;
					}
					
					int ptdy = ((dtty * yi)>>10) + vstty;
					int otdy = ((dtty * yf)>>10) + vstty;
					
					int iptdz = (ifov << 16) / ptdz;
					int iotdz = (ifov << 16) / otdz;

					int ptdx = ((dttx * yi)>>8) + vsttx;
					int otdx = ((dttx * yf)>>8) + vsttx;
					
					int spjy = ptdy * iptdz >> 16; // /ptdz;
					int sopjy = otdy * iotdz >> 16; // /otdz;
					
					xlt = iptdz * (xlt + ptdx) >> 18;
					xrt = iptdz * (xrt + ptdx) >> 18;
					xlb = iotdz * (xlb + otdx) >> 18;
					xrb = iotdz * (xrb + otdx) >> 18;
					
					//#ifndef NO_ROLL
					//roll
					x0 = (((xlt * httx) + (spjy * uttx)) >> 16) + width2;
					y0 = (((xlt * htty) + (spjy * utty)) >> 16) + height2;
					
					x1 = (((xrt * httx) + (spjy * uttx)) >> 16) + width2;
					y1 = (((xrt * htty) + (spjy * utty)) >> 16) + height2;
					
					x2 = (((xrb * httx) + (sopjy * uttx)) >> 16) + width2;
					y2 = (((xrb * htty) + (sopjy * utty)) >> 16) + height2;
					
					x3 = (((xlb * httx) + (sopjy * uttx)) >> 16) + width2;
					y3 = (((xlb * htty) + (sopjy * utty)) >> 16) + height2;
					//roll
					//#else
					//#endif
					
					int lc = lerp;
					//boolean lighting = true;
					if ((type & TRACK_CENTRAL_BIT) != 0) {
						if (((i+usid) & 1) == 0) col = altCol;
					} else {
						if (((i+usid) & 2) == 0) col = altCol;
						if (((i+usid) & 1) != 0) {
							lc = oldlerp;
						}
					}
					
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
						
					colRB += (lerpColRB-colRB)*lc >> 8;
					colG  += (lerpColG-colG)*lc >> 8;
						
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
					
					/*
					//#ifdef com.mygdx.mongojocs.mr2.Debug
					if((i + usid) == vehicleUsid) {
						//pintar de vermell la seccio que toca
						col = 0xff0000;
					}
					//#endif
					*/
					if(x0 == x3 && x1 == x2 && y0 == y1 && y2 == y3) {
						g.setColor(col);
						g.fillRect(x0, y0 + ydispl, x1 - x0, y2 - y1);
					} else {
						//#ifndef NO_ROLL
						quadFillEsp(x0, y0, x1, y1, x2, y2, x3, y3, col);
						//#else
						y0 += ydispl;
						y2 += ydispl;
						quadFill(x0, x1, x3, x2, y0, y2, col);
						//#endif
					}
				}
				
				for (int j = 0; j <lineList.length; j+= TRACK_INTS_PER_LINE) {
					xi = lineList[j] << 1;
					yi = lineList[j+1];
					xf = lineList[j+2] << 1;
					yf = lineList[j+3];
					col = lineList[j+4];
					altCol = lineList[j+5];
										
					int ptdz = ((dttz*yi)>>10) + vsttz;
					int otdz = ((dttz*yf)>>10) + vsttz;
					
					int ptdy = ((dtty*yi)>>10) + vstty;
					int otdy = ((dtty*yf)>>10) + vstty;
					
					int ptdx = ((dttx*yi)>>8) + vsttx;
					int otdx = ((dttx*yf)>>8) + vsttx;

					if(ptdz <= 0) {
						ptdz = 1;
					}
					if(otdz <= 0) {
						otdz = 1;
					}
					
					int iptdz = (ifov << 16) / ptdz;
					int iotdz = (ifov << 16) / otdz;

					int spjy = ptdy*iptdz >> 16;
					int sopjy =otdy*iotdz >> 16;
										
					xi = iptdz*(xi+ptdx) >> 18;
					xf = iotdz*(xf+otdx) >> 18;

					if (((i+usid) & 1) == 0)	col = altCol;
					
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
					
					colRB += (lerpColRB-colRB)*lerp >> 8;
					colG  += (lerpColG-colG)*lerp >> 8;
					
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
					
					/*
					if((i + usid) == vehicleUsid) {
						//pintar de vermell la seccio que toca
						col = 0xff0000;
					}
					*/
					
					
					//#ifndef NO_ROLL
					//roll
					x0 = ((xi * httx) + (spjy * uttx)) >> 16;
					y0 = ((xi * htty) + (spjy * utty)) >> 16;
					
					x1 = ((xf * httx) + (sopjy * uttx)) >> 16;
					y1 = ((xf * htty) + (sopjy * utty)) >> 16;
					//roll
					//#else
					//#endif
					
					g.setColor(col);
					g.drawLine(x0 + width2, y0 + height2 + ydispl, x1 + width2, y1 + height2 + ydispl);
				}
				
				
			//	System.out.println("vertsecType" + vertsectype);
				
				/// VERTICAL ////////////////--
				if(track_verticalTrackSectionsMap_patchList[vertsectype] == null) continue;
				//no seria del tot correcte l'if anterior si hi hagues una secció només amb linies
				patchList = track_verticalTrackSectionsMap_patchList[vertsectype];
				lineList = track_verticalTrackSectionsMap_lineList[vertsectype];
				
			/*	com.mygdx.mongojocs.mr2.Debug.println("drawin vertical sections! " + i);
				com.mygdx.mongojocs.mr2.Debug.println("patchList " + patchList.length);
				com.mygdx.mongojocs.mr2.Debug.println("lineList " + lineList.length);
			*/	
				int vttzo = vttz;
				int vttzm = vttz - (dttz >> 1);
				for (int j = 0; j < patchList.length; j+= TRACK_INTS_PER_PATCH) {
					xlt = patchList[j] << 1;
					xrt = patchList[j+1] << 1;
					xlb = patchList[j+2] << 1;
					xrb = patchList[j+3] << 1;
					yi = 3 * patchList[j+4] >> 1;
					yf = 3 * patchList[j+5] >> 1;
					col = patchList[j+6];
					int typep = patchList[j + 7];
					altCol = patchList[j+8];
					
					if((typep & TRACK_SPEEDUP_BIT) != 0) {
						vttz = vttzo;
					} else {
						vttz = vttzm;
					}

					int ptdy = (yi)+vtty;
					int otdy = (yf)+vtty;
					
					int spjy = (ifov*ptdy)/vttz;
					int sopjy = (ifov*otdy)/vttz;

					int pjdx = (ifov*vttx)/vttz;

					xlt = ((ifov*xlt)/(vttz) >>8) + pjdx;
					xrt = ((ifov*xrt)/(vttz) >>8) + pjdx;
					xlb = ((ifov*xlb)/(vttz) >>8) + pjdx;
					xrb = ((ifov*xrb)/(vttz) >>8) + pjdx;
					
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
					
					colRB += (lerpColRB-colRB)*lerp >> 8;
					colG  += (lerpColG-colG)*lerp >> 8;
					
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
					
					/*
					if((typep & Track_SLOWDOWN_BIT) != 0) {
						col = 0xff0000;
					}
					*/
					
					//#ifndef NO_ROLL
					//roll
					x0 = (((xlt * httx) + (spjy * uttx)) >> 16) + width2;
					y0 = (((xlt * htty) + (spjy * utty)) >> 16) + height2;
					
					x1 = (((xrt * httx) + (spjy * uttx)) >> 16) + width2;
					y1 = (((xrt * htty) + (spjy * utty)) >> 16) + height2;
					
					x2 = (((xrb * httx) + (sopjy * uttx)) >> 16) + width2;
					y2 = (((xrb * htty) + (sopjy * utty)) >> 16) + height2;
					
					x3 = (((xlb * httx) + (sopjy * uttx)) >> 16) + width2;
					y3 = (((xlb * htty) + (sopjy * utty)) >> 16) + height2;
					//#else
					//#endif
					
					//roll
					if(x0 == x3 && x1 == x2 && y0 == y1 && y2 == y3) {
						g.setColor(col);
						g.fillRect(x0, y0 + ydispl, x1 - x0, y2 - y1);
					} else {
						//#ifndef NO_ROLL
						quadFillEsp(x0, y0, x1, y1, x2, y2, x3, y3, col);
						//#else
						//#endif
					}
				}
				
				vttz = vttzm;		//linies sempre al mig
				for (int j = 0; j <lineList.length; j+= TRACK_INTS_PER_LINE) {
					xi = lineList[j] << 1;
					yi = (3 * lineList[j+1]) >> 1;
					xf = lineList[j+2] << 1;
					yf = (3 * lineList[j+3]) >> 1;
					col = lineList[j+4];
					altCol = lineList[j+5];

					int ptdy = (yi)+vtty;
					int otdy = (yf)+vtty;
					
					int spjy = (ifov*ptdy)/vttz;
					int sopjy = (ifov*otdy)/vttz;

					int pjdx = (ifov*vttx)/vttz;
					
					xi = ((ifov*xi)/(vttz) >>8) + pjdx;
					xf = ((ifov*xf)/(vttz) >>8) + pjdx;
					
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
					
					colRB += (lerpColRB-colRB)*lerp >> 8;
					colG  += (lerpColG-colG)*lerp >> 8;
					
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
					
					/*
					if((i + usid) == vehicleUsid) {
						//toma
						col = 0xff0000;
					}
					*/
					//#ifndef NO_ROLL
					//roll
					x0 = ((xi * httx) + (spjy * uttx)) >> 16;
					y0 = ((xi * htty) + (spjy * utty)) >> 16;
					
					x1 = ((xf * httx) + (sopjy * uttx)) >> 16;
					y1 = ((xf * htty) + (sopjy * utty)) >> 16;
					//roll
					//#else
					//#endif
					
					g.setColor(col);
					g.drawLine(x0 + width2, y0 + height2 + ydispl, x1 + width2, y1 + height2 + ydispl);
				}
			}
		}
//#endif
		//#ifdef com.mygdx.mongojocs.mr2.Debug
		//	com.mygdx.mongojocs.mr2.DebugX.r = (int)(System.currentTimeMillis() - mssr);
		//#endif
		
/*
 *
 * End of Render Track
 *
 */
		//#ifdef J2ME
		g.setClip(0, 0, canvasWidth, canvasHeight);
		
		if(ydispl != 0) {
			g.setColor(0);
			//#ifdef BIG_PANEL
			//#else
			g.fillRect(0, 0, canvasWidth, -ydispl);
			//#endif
			g.fillRect(0, canvasHeight + ydispl, canvasWidth, -ydispl);
			//#ifdef BIG_PANEL
			//#endif
		}
		//#endif

//#ifndef NO_PHANTOM
/*
 *
 * Draw Phantom
 *
 *
 */
 
	 //if(phantom != null) {
	//	 int maxuid =  (track_vertexSectionArray.length / 3) << 7;
		// System.out.println("ph:" + ((phantom.w_subusid % maxuid) >> 7)+ " own:" + ((myVehicle.w_subusid % maxuid) >> 7)+ " lp:" + lapCounter + " ipv:" + isPhantomVisible + " psr:" + phantom.phantom_stillRunning); 
	 //}
	 if(phantom != null && phantom.phantom_isSet && phantom.phantom_stillRunning && isPhantomVisible && lapCounter > 0) {
			FwdVehicle vh = phantom;
			// DOJAPORT - getLateralDisplace
			int vttx = phantomx - (phantom.w_latdisplace >> 9);
			int vtty = phantomy;
			int vttz = phantomz;
			
			if(vttz > 0) {
				int maxuid =  (track_vertexSectionArray.length / 3) << 7;
				int vhsu = ((vh.w_subusid + 127) % maxuid);
				int mvsu = (myVehicle.w_subusid % maxuid);
				int udif = (vhsu - mvsu);

				if(udif < 0) udif = maxuid + udif;
				//only draw phantom if it is in front of us and before the last section drawn
				if((udif >> 7) <= TRACK_NUM_SECTIONS_AHEAD * 2) {
					//#ifdef NO_ROLL
					//#else
					int x0 = ((ifov * vttx) / vttz);
					int y0 = ((ifov * vtty) / vttz);
					int carx = (((x0 * httx) + (y0 * uttx)) >> 16) + width2;
					int cary = (((x0 * htty) + (y0 * utty)) >> 16) + height2 + ydispl + height/6; //hack de la muerte!! whahhahah
					//#endif
					int depthLevel = 0;
					int depth = (udif / TRACK_NUM_SECTIONS_AHEAD);// >> 1;
					
					for(;depthLevel < depthLevels.length; ++depthLevel) {
						if(depthLevels[depthLevel] > depth) break;
					}
					
					if(depthLevel >= 0 && depthLevel < depthLevels.length - 1) {
						if((depthLevel > 1) && (depthLevel < depthLevels.length) && (depth > ((depthLevels[depthLevel - 1] + depthLevels[depthLevel]) >> 1))) {
							depthLevel++;
						}
						drawVehicle(vh, g, carx, cary, -toInt(div(vh.w_roll * 180,PI)) >> 2, depthLevel, false);
					}
				}
			}
			pos = 2;
			int pv = vh.w_subusid % ((track_vertexSectionArray.length / 3) << 7);
			int ov = myVehicle.w_subusid % ((track_vertexSectionArray.length / 3) << 7);
			if(pv <= ov && lapCounter > 0) {
				pos = 1;
				int diff = (myVehicle.w_latdisplace - vh.w_latdisplace) >> 8;
				int mid = canvasWidth >> 1;
				//dibuixar fletxa
				if(diff < -mid) diff = -mid;
				if(diff >  mid) diff =  mid;
				//#ifdef J2ME
				g.setClip(0, 0, canvasWidth, canvasHeight);
				//#ifndef NK-s40
				g.drawImage(arrow, mid + diff, canvasHeight - arrow.getHeight(), 20);
				//#else
				//#endif
				//#endif
			}
		}
//#endif

	}
	//System.out.println("e-drawphant");

//#ifndef NO_OWN_VEHICLE
/*
 *
 * Draw my vehicle
 *
 *
 */
/*********************///		if (true) return;	/*********************************************************************/
		int rndx = 0;
		int rndy = 0;
		
		if(gameMode == 0) {
			if(myVehicle.slowDown) {
				rndx = rnd(toInt(myVehicle.w_vel)) >> 5;
				rndy = rnd(toInt(myVehicle.w_vel)) >> 4;
				
				if(toInt(myVehicle.w_vel) == 0) {
					rndx = 0;
					rndy = 0;
				}
			}
			
			//#ifndef CARS_S40
			// DOJAPORT - getLateralDisplace
			int carx = width2 - (myVehicle.w_latdisplace >> 9) + rndx;
			//#else
			//#endif
			int cary = canvasHeight - canvasHeight / 12 + (ydispl >> 1) + rndy;
			
			drawVehicle(myVehicle, g, carx, cary, -toInt(div(mul(myVehicle.w_roll,toFP(180)),PI)) >> 2, 0, true);
		} else {
			int xdisp = -toInt(div(myVehicle.w_roll,PI) * 180);
			int ydisp = -Math.abs(xdisp);
			
			rndx = (rnd(toInt(myVehicle.w_vel)) >> 6) + (rnd(xdisp) >> 3);
			rndy = (rnd(toInt(myVehicle.w_vel)) >> 6) + (rnd(ydisp) >> 3);
			
			if(myVehicle.slowDown) {
				rndx *= 3;
				rndy *= 3;
			}
			
			if(toInt(myVehicle.w_vel) == 0) {
				rndx = 0;
				rndy = 0;
			}
			
			xdisp += rndx;
			ydisp += rndy;
			
			xdisp -= (panelImage.getWidth() - canvasWidth) >> 1;
			ydisp += 25 + canvasHeight - panelImage.getHeight();
			//#ifdef J2ME
			g.setClip(0, -ydispl, canvasWidth, canvasHeight + ydispl + ydispl);
			g.drawImage(panelImage, xdisp, ydisp + ydispl, 20);
			//#endif
			
			int rVel = div(mul((myVehicle.w_vel << 1) + (230 << 15), PI), toFP(180));
			int sVel = sin(rVel);
			int cVel = cos(rVel);
			
			//canviar o optimitzar
			
			//int kx = toInt( mul(0, cVel) + mul(toFP(20), sVel));
			//int ky = toInt(-mul(0, sVel) + mul(toFP(20), cVel));
			//#ifndef CARS_S40 
			int kx = toInt( mul(0, cVel) + mul(toFP(20), sVel));
			int ky = toInt(-mul(0, sVel) + mul(toFP(20), cVel));
			//int kx = toInt(sVel * 20 );
			//int ky = toInt(cVel * 20);
			//#else
			//int kx = toInt(sVel * 12);
			//int ky = toInt(sVel * 12);
			//#endif
			
			//#ifndef CARS_S40
			g.setColor(0x707C7E);	//112 124 126
			// DOJAPORT
			//g.drawLine(xdisp + 152, ydisp + 123 + ydispl, xdisp + 152 + 4 + kx, ydisp + 123 - ky + 4 + ydispl);
			g.drawLine(xdisp + 152, ydisp + 123 + ydispl, xdisp + 156 + kx, ydisp + 127 - ky + ydispl);
			
			g.setColor(0x1811b8);	//184 17 24
			g.drawLine(xdisp + 152, ydisp + 123 + ydispl, xdisp + 152 + kx, ydisp + 123 - ky + ydispl);
			//#else
			//#endif
		}
		
		
		//black margin
		//#ifdef J2ME
		g.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		g.setColor(0);
		g.fillRect(0, canvasHeight + ydispl, canvasWidth, -ydispl);

//#endif
	
/*
 *
 * Flags
 *
 */
		int sid = (vehicleUsid + (track_vertexSectionArray.length - 4)) * 3;
		sid = (sid % track_vertexSectionArray.length) / 3;
		//sec horitzontal: on esta el 'poder azul'
		int sectype = track_typeSectionArray[sid];
		//sec vertical: fletxes a posar
		int vertsectype = track_verticalTypeSectionArray[sid];
		if(track_trackSectionsMap_flags != null) {
			int flagsH = track_trackSectionsMap_flags[sectype];
			if(flagsH != 0 && !myVehicle.collision && !myVehicle.lateralFall) {
				int boost = (flagsH < 8)?(flagsH >> 1):((flagsH >> 2) + ((flagsH >> 3) & 0x1));
				boost -= 2;	//centrar en el 0
				
				boost *= 2;
				//comprovar el despla�ament lateral segons el valor del flag
				//System.out.println("boost at = " + boost + " bike at " + myVehicle.getLateralPos());
				// DOJAPORT - getLateralDisplace
				int vp = -(myVehicle.w_latdisplace >> 12);
				if(vp >= boost - 1 && vp <= boost + 1) {
					//#ifndef NO_SOUND_IN_GAME
					//evitar que sonin massa seguits
					if(tickCount - lastBoostTime > 6) {	//1,2 sec
						soundPlay(SOUND_BLUEPOWER,1);
					}
					//#endif
					myVehicle.boost = true;
					myVehicle.boosted = true;
					myVehicle.flash = 0xC0FF00;
					lastBoostTime = tickCount; 
				}
			}
		}
		
		if(track_verticalTrackSectionsMap_flags != null || myVehicle.boost) {
			int flagsV = 0;
			
			if(myVehicle.boost) {
				flagsV = 32;
			} else {
				flagsV = track_verticalTrackSectionsMap_flags[vertsectype];
			}
			
			//DOJAPORT //#ifdef J2ME
			
			if(flagsV != 0) {
				lastArrow = (flagsV < 8)?(flagsV >> 1):((flagsV >> 2) + ((flagsV >> 3) & 0x1));
				if(flagsV == 32) lastArrow = 5;	//boost!
				lastArrowTime = tickCount;
			}
			
			if(tickCount - lastArrowTime < TRACK_ARROW_THRESHOLD) {
				//#ifndef CARS_S40
				//noch
				//#ifdef J2ME
					g.setClip(canvasWidth/2 - 20, 52 + (ydispl >> 1) + 18, 40, 35);
					g.drawImage(trackArrows, canvasWidth/2 - 20 - 40 * lastArrow, 52 + (ydispl >> 1) + 18, 20);
				//#elifdef DOJA
				//#endif
				
				//#else
				//#endif
				//#endif
				//#endif
			}
			//DOJAPORT //#endif
		}
		
	if(!flash) {	
		 if(ydispl == 0) {
			 
		//#ifdef TRACK_INGAME
/*
 *
 * Draw mini map of the track
 *
 *
 */
			//#ifdef J2ME
			int CRVDIV = (width2 << 8) / 400;
			
			//#ifdef BIG_PANEL
			//#else
			g.setColor(0x00116C);
			//#endif
			
			//#ifndef CARS_S40
			g.setClip(panel0.getWidth(),0,canvasWidth - panel1.getWidth() - panel0.getWidth(),42);
			//#else
			//#endif
			
			try {
				//mirar els usids reals
				int px = 0;
				int py = 0;
				if(phantom != null && phantom.phantom_isSet) {
					int p = ((phantom.w_subusid >> 7) * 3) % track_vertexSectionArray.length;
					//#ifndef CARS_S40
					px = (track_vertexSectionArray[p] * CRVDIV) >> 8;
					py = (track_vertexSectionArray[p + 1] * CRVDIV) >> 8;
					//#else
					//#endif
				}
				
				int p = (((myVehicle.w_subusid >> 7) + 2) * 3) % track_vertexSectionArray.length;
				//#ifndef CARS_S40
				int centerx = (track_vertexSectionArray[p] * CRVDIV) >> 8;
				int centery = (track_vertexSectionArray[p + 1] * CRVDIV) >> 8;
				//#else
				//#endif
				
				int xdesp = width2;
				//#ifndef CARS_S40
				int ydesp = 25;
				//#else
				//#endif
				
				if(centerx + xdesp < 0) 	xdesp += centerx + 5;
				if(centerx + xdesp > 0)		xdesp -= centerx + 5;
				if(centery + ydesp < 0) 	ydesp += centery + 5;
				if(centery + ydesp > 0) 	ydesp -= centery + 5;
				
				//#ifndef CARS_S40
				int ox = (track_vertexSectionArray[0] * CRVDIV) >> 8;
				int oy = (track_vertexSectionArray[1] * CRVDIV) >> 8;
				//#else
				//#endif
				int oxcr = ox;
				int oycr = oy;
				for (int i = 0; i < track_vertexSectionArray.length / 12; i ++) {
					p = (i * 12) % track_vertexSectionArray.length;
					//#ifndef CARS_S40
					int cx = (track_vertexSectionArray[p    ] * CRVDIV) >> 8;
					int cy = (track_vertexSectionArray[p + 1] * CRVDIV) >> 8;
					//#else
					//#endif
					
					g.drawLine(ox + xdesp, oy + ydesp , cx + xdesp , cy + ydesp);
					
					ox = cx;
					oy = cy;
				}
				g.drawLine(ox + xdesp, oy + ydesp , oxcr + xdesp , oycr + ydesp);
				g.setColor(0x40FF00);
				g.fillRect(centerx + xdesp - 2, centery + ydesp - 2, 4, 4);
				
				if(phantom != null && phantom.phantom_isSet) {
					g.setColor(0xFFC000);
					g.fillRect(px + xdesp - 2, py + ydesp - 2, 4, 4);
				}
				
				int dx = Math.abs(track_vertexSectionArray[1 * 3    ] - track_vertexSectionArray[0 * 3    ]);
				int dy = Math.abs(track_vertexSectionArray[1 * 3 + 1] - track_vertexSectionArray[0 * 3 + 1]);
				//#ifndef CARS_S40
				// DOJAPORT
				//px = ((track_vertexSectionArray[0 * 3    ] * CRVDIV) >> 8) + xdesp;
				//py = ((track_vertexSectionArray[0 * 3 + 1] * CRVDIV) >> 8) + ydesp;
				
				px = ((track_vertexSectionArray[0        ] * CRVDIV) >> 8) + xdesp;
				py = ((track_vertexSectionArray[        1] * CRVDIV) >> 8) + ydesp;
				//#else
				// DOJAPORT
				//#endif
				
				g.setColor(0x00116C);
				if(dy > dx) {
					g.drawLine(px - 4, py, px + 4,  py);
				} else {
					g.drawLine(px, py - 4, px, py + 4);
				}
			} catch (Exception e) {
				//getInstance().lastException = e.getMessage();
				e.printStackTrace();
			}
			//#endif
		//#endif
		
//#ifndef NO_DRAW_PANELS
/*
 *
 * Draw Panels
 *
 *
 */
			//#ifdef J2ME
				g.setClip(0, 0, canvasWidth, canvasHeight);
				//#ifdef SD-P600
				//#endif
			//#endif
			
			//#ifndef NK-s40
				//#ifndef DOJA
					g.drawImage(panel0, 0, 0, 20);
					g.drawImage(panel1, canvasWidth - panel1.getWidth(), 0, 20);
				//#else
				//#endif
			//#else
			//DOJAPORT
			//#endif
//#endif
			//#ifdef J2ME
			g.setClip(0, 0, canvasWidth, canvasHeight);
			//#endif
			if(raceTime < 2000) {
				//#ifndef CARS_S40
				int x = (canvasWidth - panelSem.getWidth()) >> 1;
				//#ifndef DOJA
				//DOJAPORT
				g.drawImage(panelSem, x, 42, 20);
				//#else
				//System.out.println("DOJAPORT 1");
				//#endif
				// DOJAPORT
				//drawNumber(g, x + 27, 42 + 8, 2, raceTime < 0 ? (int)(-raceTime / 1000) + 1: 0);
				drawNumber(g, x + 27, 50, 2, raceTime < 0 ? (int)(-raceTime / 1000) + 1: 0);
				
				//#ifndef DOJA
				//DOJAPORT
				g.setClip(0, 0, canvasWidth, canvasHeight);
				//#endif
				if(raceTime < 0) {
					//#ifndef DOJA
					//DOJAPORT
					g.drawImage(semR, x + 6, 42 + 31, 20);
					//#else
					//#endif
				} else {
					//#ifndef DOJA
					//DOJAPORT
					g.drawImage(semG, x + 6, 42 + 42, 20);
					g.drawImage(semG, x + 6, 42 + 53, 20);
					//#else
					//#endif
				}
				//#else
				//#endif
			}
			//DOJAPORT//#endif
			
			//#ifndef CARS_S40
			if(lapCounter < 1) {
				//#ifdef BIG_PANEL
				//#else
				drawTime(g, 5, 3, 0);
				//#endif
			} else {
				//#ifdef BIG_PANEL
				//#else
				drawTime(g, 5, 3, lapTime);
				//#endif
			}
			//#ifdef BIG_PANEL
			//#else
			drawTime(g, 5, 12, lastLapTime);
			//#endif
			
			
			if(gameContraOponente) {
				if(bestTime < tmpGhostTime) {
					//#ifdef BIG_PANEL
					//#else
					drawTime(g, 5, 21, bestTime);
					//#endif
				} else {
					//#ifdef BIG_PANEL
					//#else
					drawTime(g, 5, 21, tmpGhostTime);
					//#endif
				}
			} else {
				if(phantom.phantom_isSet) {
					if(bestTime < pelaoTime) {
						//#ifdef BIG_PANEL
						//#else
						drawTime(g, 5, 21, bestTime);
						//#endif
					} else {
						//#ifdef BIG_PANEL
						//#else
						drawTime(g, 5, 21, pelaoTime);
						//#endif
					}
				} else {
					//#ifdef BIG_PANEL
					//#else
					drawTime(g, 5, 21, pelaoTime);
					//#endif
				}
			}
			//#else
			if(lapCounter < 1) {
				//#ifdef BIG_PANEL
				//#else
				drawTime(g, 2, 17, 0);
				//#endif
			} else {
				//#ifdef BIG_PANEL
				//#else
				drawTime(g, 2, 17, lapTime);
				//#endif
			}
			//#endif
			
			//#ifndef CARS_S40
			int vel2p = toInt(myVehicle.w_vel * 3) >> 1;
			
			//#ifdef BIG_PANEL
			//#else
			drawNumber(g, 2, 30, 0, vel2p);
			//#endif
			
			//#ifdef BIG_PANEL
			//#else
			drawNumber(g, canvasWidth - panel1.getWidth() + 12, 6, 1, lapCounter);
			//#endif
			
			//#else
			//#endif
			
			//#ifndef NK-s40
				//DOJAPORT
				//#ifdef DOJA
				//#elifdef J2ME
					//#ifdef BIG_PANEL
					//#else
					drawSingleNumber(g, canvasWidth - panel1.getWidth() + 4, 1 + 2, 2, lapCounter); // +2 )(?)
					//#endif
				//#endif
			//#else 
			
			//#ifdef BIG_PANEL
			//#else
			drawSingleNumber(g, canvasWidth - 28 + 4, 2, 1, lapCounter);
			//#endif
			
			//#endif
			//#endif
			
			//#ifndef CARS_S40
			if(phantom.phantom_isSet) {
				//#ifdef BIG_PANEL
				//#else
				drawNumber(g, canvasWidth - panel1.getWidth() + 2, 24, 2, pos);
				//#endif
				
				
				//#ifdef BIG_PANEL
				//#else
				drawSingleNumber(g, canvasWidth - panel1.getWidth() + 19, 24, 1, 2);
				//#endif
			} else {
				//#ifdef BIG_PANEL
				//#else
				drawNumber(g, canvasWidth - panel1.getWidth() + 2, 24, 2, 1);
				//#endif
				
				//#ifdef BIG_PANEL
				//#else
				drawSingleNumber(g, canvasWidth - panel1.getWidth() + 19, 24, 1, 1);
				//#endif
			}
			//#endif
			
			//#ifndef DOJA
			//DOJAPORT
			g.setClip(0, 0, canvasWidth, canvasHeight);
			//#endif
			//#ifndef NO_ROLL
			if(raceTime > 2500 && raceTime < 6000) {
				textDraw(gameText[TEXT_PULSA_1][0], 0, (canvasHeight >> 1) - (formFontHeight >> 1) - (formFontHeight << 1), 0xffffff, TEXT_HCENTER|TEXT_OUTLINE);
				textDraw(gameText[TEXT_PULSA_2][0], 0, (canvasHeight >> 1) + (formFontHeight >> 1) - (formFontHeight << 1), 0xffffff, TEXT_HCENTER|TEXT_OUTLINE);
			}
			
			if(raceTime > 6500 && raceTime < 10000) {
				textDraw(gameText[TEXT_HELP_1][0], 0, (canvasHeight >> 1) - (formFontHeight >> 1) - (formFontHeight << 1), 0xffffff, TEXT_HCENTER|TEXT_OUTLINE);
				textDraw(gameText[TEXT_HELP_2][0], 0, (canvasHeight >> 1) + (formFontHeight >> 1) - (formFontHeight << 1), 0xffffff, TEXT_HCENTER|TEXT_OUTLINE);
			}
			//#else
			//#endif
		}
	}

//#ifdef DOJA
	// DOJAPORT 
	// BIGTHOR
	// WIDE SCREEN
	if (myVehicle.boostTick>0)
	{
		g.setColor(0x000000);
		g.fillRect(0, 0, canvasWidth, (canvasHeight / 4));
		g.fillRect(0, canvasHeight-(canvasHeight / 4), canvasWidth, canvasHeight);
	}
//#endif
}


	

	public void drawTime(Graphics g, int x, int y, long time) {
		byte[] nums = new byte[3];
		if(time == 0) {
			nums[0] = 0;
			nums[1] = 0;
			nums[2] = 0;
		} else {
			int mins = (int) (time / 60000);
			int secs = (int) (time /  1000) % 60;
			int millis = (int) (time %  1000) / 10;
			
			if(mins > 99) mins = 99;
			nums[0] = (byte) mins;
			nums[1] = (byte) secs;
			nums[2] = (byte) millis;
		}
		//#ifndef CARS_S40
		drawNumber(g, x     , y, 1, nums[0]);
		drawNumber(g, x + 14, y, 1, nums[1]);
		drawNumber(g, x + 28, y, 1, nums[2]);
		//#else
		//#endif
	}
	
	public void drawNumber(Graphics g, int x, int y, int fsize, int n) {
		//DOJAPORT //#ifdef J2ME
	
		//#ifndef NK-s40
		
			//#ifndef DOJA
			//DOJAPORT
			Image font = font1;
			//#else
			//#endif
		
		//#else
		int spriteBase = 30;
		//#endif
		//#ifndef CARS_S40
		int size = 6;
		//#else
		//#endif
		boolean threeDigits = false;
		boolean twoDigits = true;
		
		if(n < 0) n += 256;
		if(fsize == 2) twoDigits = false;
		
		if((fsize & 0x1) == 0) {
			//#ifndef NK-s40
			font = font0;
			
			//#ifdef DOJA
			//#else
			spriteBase = 10;
			//#endif
			
			//#ifndef CARS_S40
			size = 11;
			//#else
			//#endif
			if(fsize != 2) threeDigits = true;
		}
		
		if(threeDigits) {
			//#ifndef DOJA
			//DOJAPORT
			g.setClip(x, y, size, size << 1);
			//#endif
			//#ifndef NK-s40
				//#ifndef DOJA
				//DOJAPORT
					g.drawImage(font, x - (n / 100) * size, y, 20);
				//#else
				//#endif
			//#else
			//#endif
			x +=  size;
			//#ifdef CARS_S40
			x++;
			//#endif
		}
			
		if(twoDigits) {
			//#ifndef DOJA
			//DOJAPORT
			g.setClip(x, y, size, size << 1);
			//#endif
			//#ifndef NK-s40
				//#ifndef DOJA
				//DOJAPORT
					g.drawImage(font, x - ((n % 100)/ 10) * size, y, 20);
				//#else
				//#endif
			//#else
			//#endif
			x += size;
			//#ifdef CARS_S40
			//#endif
		}
		
		//#ifndef DOJA
		//DOJAPORT
		g.setClip(x, y, size, size << 1);
		//#endif
		//#ifndef NK-s40
			//#ifndef DOJA
			//DOJAPORT
				g.drawImage(font, x - ((n % 100) % 10) * size, y, 20);
			//#else
			//#endif
		//#else
		//#endif
		//DOJAPORT //#endif
	}
	
	public void drawSingleNumber(Graphics g, int x, int y, int fsize, int n) {
		//DOJAPORT //#ifdef J2ME
		//#ifndef NK-s40
			//#ifndef DOJA
			//DOJAPORT
				Image font = font1;
			//#else
			//#endif
			
		//#else
		//#endif
		
		//#ifndef CARS_S40
		int size = 6;
		//#else
		//#endif
		
		//#ifndef DOJA
		//DOJAPORT 
		g.setClip(x, y, size, size << 1);
		//#endif
		//#ifndef NK-s40
			//#ifndef DOJA
			//DOJAPORT
				g.drawImage(font, x - ((n % 100) % 10) * size, y, 20);
			//#else
			//#endif
		//#else
		//#endif
		//DOJAPORT //#endif
	}
	
	//#ifdef J2ME

	/*
	 *   x0      x1
	 *    +------+   top
	 *   /        \
	 *  +----------+	 bottom
	 *  x3          x2
	 */
	/**
	 *  Description of the Method
	 *

	 * @param  x0       Description of the Parameter
	 * @param  x1       Description of the Parameter
	 * @param  x2       Description of the Parameter
	 * @param  x3       Description of the Parameter

	 */
	 //#ifndef NO_ROLL
	 void quadFillEsp(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3, int col) {
		 y0 += ydispl;
		 y1 += ydispl;
		 y2 += ydispl;
		 y3 += ydispl;
		 
		 if(y0 > canvasHeight && y1 > canvasHeight) return;
		 if(x1 < 0 && x2 < 0) return;
		 if(x0 > canvasWidth && x3 > canvasWidth) return;
		 
		 
		 //uberhack per arreglar el bug del poligon de la dreta
		 // DOJAPORT - getLateralDisplace
		 if(x2 < x3 && myVehicle.w_latdisplace >> 8 < 0) {
			 int t = x2;
			 x2 = x3;
			 x3 = x2;
		 }
		 
		 //#ifdef NOKIA_POLY
		 //#endif
		 
		 //#ifdef MIDP20_TRIANGLE
		 //#endif
		 
		 //#ifdef NOKIA_TRIANGLE
		 LCD_dGfx.fillTriangle(x0, y0, x1, y1, x2, y2, 0xFF000000 | col);
		 LCD_dGfx.fillTriangle(x0, y0, x2, y2, x3, y3, 0xFF000000 | col);
		 //#endif
		 
		 //UBER LOW QUALITY FILLER ... draw contour lines, do not fill it. :\
		 //#ifdef ULQ_FILLER
		 //#endif
	}
	//#endif
	/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 *   x0      x1
	 *    +------+   top
	 *   /        \
	 *  +----------+	 bottom
	 *  x2          x3
	 */
	
	//#ifdef QUAD_NOKIA
	void quadFill(int x0, int x1, int x2, int x3, int top, int bottom,int col) {
		int dxle;
		int dxre;
		int oxle;
		int oxre;
		int yRun  = 0;
		int dy    = bottom - top;
		//int col2 = col | 0xff000000;
		
		if (dy < 1) {
			return;
		}
		
		if(top > heightCanvas) {
			return;
		}

		//pure rect
		if(x0 == x2 && x1 == x3) {
			gBuffer.fillRect(x0, top, x1 - x0, bottom - top);
			return;
		}
		LCD_dGfx.fillTriangle(x0, top, x1, top, x3, bottom, 0xFF000000 | col);
		LCD_dGfx.fillTriangle(x0, top, x2, bottom, x3, bottom, 0xFF000000 | col);
	}
	//#endif
	
	//#ifdef QUAD_NOKIA2
	//#endif
	
	//#ifdef QUAD_MIDP20
	//#endif
	
	//#ifdef QUAD_MIDP20_2
	//#endif
	
	
	//#ifdef QUAD_RASTER_FULL
//#endif

//#ifdef RENDER_QUALITY_LOW
//DOJAPORT by rai
//#endif

	// 128 bytes
	//#ifdef USE_FASTINVSQRT
	int invRootsGuesses [] = {
	5931639,4194302,2965820,2097152,1482910,1048576,741455,524288,370727,262144,185363,131072,92681,65536,
	46340,32768,23170,16384,11585,8192,5792,4096,2896,2048,1448,1024,724,512,362,256,181,0,0
	};
	
	public int myFastInvSqrt(int x) {
		int yn1 = 0;
		int xx = x;
		int ipx = 0;
		while (xx != 0) {
			xx = xx>>1;
			++ipx;
		}
		yn1 = invRootsGuesses[ipx];
		// dojaport
		//yn1 = mul(yn1,(32768*3-mul(x,mul(yn1,yn1))))>>1;
		//yn1 = mul(yn1,(32768*3-mul(x,mul(yn1,yn1))))>>1;
		yn1 = mul(yn1,(98304-mul(x,mul(yn1,yn1))))>>1;
		yn1 = mul(yn1,(98304-mul(x,mul(yn1,yn1))))>>1;
		return yn1;
	}
	//#endif
	
	int[] matCam = new int[3*3];
	
	void calcMatrixCamera(int px, int py, int pz, int tx, int ty, int tz) {
				
		// z-axis = target - pos
		int zax = tx - px;
		int zay = ty - py;
		int zaz = tz - pz;
		
		//#ifdef USE_FASTINVSQRT
		//#else
		int fpuinv = div(ONE, sqrt(mul(zax,zax) + mul(zay,zay) + mul(zaz,zaz)));
		//#endif
						
		zax = mul(zax,fpuinv);
		zay = mul(zay,fpuinv);
		zaz = mul(zaz,fpuinv);
				
		// x-axis = za x (0, 1, 0)
		int xax = - zaz;
		int xay = 0;
		int xaz = zax;
		
		//#ifdef USE_FASTINVSQRT
		//#else
		fpuinv = div(ONE, sqrt(mul(xax,xax) + mul(xaz,xaz)));
		//#endif
		xax = mul(xax,fpuinv);
		xaz = mul(xaz,fpuinv);
		
		// y-axis = ya x za
		int yax = - mul(xaz,zay);
		int yay = mul(xaz,zax) - mul(xax,zaz);
		int yaz = mul(xax,zay);
				
		matCam[0] = -xax;
		matCam[1] = -xay;
		matCam[2] = -xaz;
		
		matCam[3] = -yax;
		matCam[4] = -yay;
		matCam[5] = -yaz;
		
		matCam[6] = zax;
		matCam[7] = zay;
		matCam[8] = zaz;
	}
	
	int xformX;
	int xformY;
	int xformZ;
	
	void xformVector(int px, int py, int pz) {
		int rx, ry, rz;
		
		rx = matCam[0]*px + matCam[1]*py + matCam[2]*pz;
		ry = matCam[3]*px + matCam[4]*py + matCam[5]*pz;
		rz = matCam[6]*px + matCam[7]*py + matCam[8]*pz;
		
		xformX = rx >> 15;
		xformY = ry >> 15;
		xformZ = rz >> 15;
	}
	
	public void drawVehicle(FwdVehicle vehicle, Graphics g, int x, int y, int rot, int depthLevel, boolean ownVehicle) {
		int flip = 1;
		
		if(!ownVehicle) {
			rot = 0;	//phantom
		}
		
		//s30 -> depthLevel+=4
		if (depthLevel < 0)
			depthLevel = 0;
		
		//#ifndef CARS_S40
			// FEW_FRAMES
				
			//dif
			if(!ownVehicle) depthLevel -= 2; 			//dibuixar-ho un pel mes gran
			if(depthLevel < 0) depthLevel = 0;
			//#ifndef BIG_MOTO
			if(depthLevel > 6) depthLevel = 6;
			//#else
			//#endif
		//#else
		//#endif
		
		//DOJAPORT - GETLATERALDISPLACE
		int lp = vehicle.w_latdisplace >> 12;
		//level B
		if(lp < -2) lp = -2;
		if(lp > 2) lp = 2;
		
		//calcular sprite
		//#ifndef FEW_FRAMES
		if(rot < 0) {rot = -rot; flip = -1;}
		if(rot > 3) rot = 3;
		//#else
		//#endif
		
		//#ifdef FEW_FRAMES
		//#else
			if(rot == 0) {
				rot = 16;
				if(lp > 1) {
					rot = 15;
				}
				
				if(lp < -1) {
					rot = 18;
				}
			} else {
				rot = rot * 5 - 1;		//center of the group of sprites n� rot
				rot += 17;			//rang: [17..33]
				
				if(flip > 0) {
					rot += lp;
					rot = 33 - rot;
				} else {
					rot -= lp;
				}
			}
		//#endif
		
		if(ownVehicle) {
			//big collision
			//#ifndef NO_COLLISION
			if(vehicle.collision) {
				int vframe = vehicle.fallFrame;
				if(vframe >= frame.length) vframe = frame.length - 1;
				
				int vf = frame[vframe];
				int pf = pframe[vframe];
				
				//#ifdef DOJA
				//#else
				int yv 		= canvasHeight - 70 + ypos[vframe]; // int yv = canvasHeight - 100 - 20 + 50 + ypos[vframe];
				int ypv 	= canvasHeight - 45 + pypos[vframe]; // int ypv = canvasHeight - 100 + 50 + pypos[vframe] + 5;
				//#endif
				int xv = 0;
				int xpv = 0;
				int xorg = vehicle.colX;
				
				if(vehicle.colRoll > 0) {
					xv  = xorg + xpos[vframe];
					xpv = xorg + pxpos[vframe];
				} else {
					xv  = xorg - 100 - xpos[vframe];
					xpv = xorg - 100 - pxpos[vframe];
					pf = -(pf + 1);
					vf = -(vf + 1);
				}
				
				//DOJAPORT //#ifdef J2ME
				//#ifdef NOFLIP
				//#else
				spriteDraw(bikeImageD, xv, yv, vf + 40, bikeDcoord, 3); // spriteDraw(bikeImageD, xv, yv, vf + 16 * 2 + 8, bikeDcoord, 3);
				//#endif
				
				if(vframe > 5) {
					//#ifdef NOFLIP
					//#else
					spriteDraw(bikeImageD, xpv, ypv, pf + 56, bikeDcoord, 3); // spriteDraw(bikeImageD, xpv, ypv, pf + 16 * 3 + 8, bikeDcoord, 3);
					//#endif
				}
				//DOJAPORT //#else
				
				//DOJAPORT //#endif
				return;
			}
			//#endif

			//lateralFall
			if(vehicle.lateralFall) {
				//vehicle.haltBoost = true;
				int ff = vehicle.fallFrame;
				if(ff < 0) ff = 0;
				int xv = 0;
				int xpv = 0;
				int fact = (14 - ff);
				int yv 	= canvasHeight - 110 - (canvasHeight * ((7 * fact) >> 1) / 208);	// >> 2//int yv = canvasHeight - 100 - 20 -  (canvasHeight * ((7 * fact) >> 1) / 208) + 10;	// >> 2 
				int ypv = canvasHeight - 110 - ((6 * canvasHeight) / 208); // int ypv = canvasHeight - 100 - 20 - (canvasHeight * (3 * (14 - 10) >> 1) / 208) + 10;
				
				int vframe = ff >> 1;
				int vf = vframe;
				
				if(vehicle.colRoll > 0) {
					xv = x - 39 + (fact << 1); // xv = x - 50 + 11 + (fact << 1);
					xpv = xv + (fact << 3);
				} else {
					xv = x - 61 - (fact << 1); // xv = x - 50 - 11 - (fact << 1);
					xpv = xv - (fact << 3);
					vframe = 15 - vframe;
				}
				
				//DOJAPORT //#ifdef J2ME
				//#ifdef NOFLIP
				//#else
				spriteDraw(bikeImageD, xv, yv, vframe, bikeDcoord, 3);
				//#endif
				//DOJAPORT //#endif
				
				//pilot
				if(vframe < 2) vframe = 2;
				if(vframe > 13) vframe = 13;
				if(vf <= 4) {
					//System.out.println("pilot frame: " + vframe);
					//DOJAPORT //#ifdef J2ME
					//#ifdef NOFLIP
					//#else
					spriteDraw(bikeImageD, xpv, ypv, vframe + 16, bikeDcoord, 3);
					//#endif
					//DOJAPORT //#endif
				}
				return;
			}
		}
		//normal
		//#ifdef BIG_MOTO
		//#elifndef CARS_S40
		//#else	
			//#ifndef DOJA
			//DOJAPORT
			Image bike = bikeImageC;
			byte[] coord = bikeCcoord;
			//#else
			//#endif
			//#ifndef NK-s40
			
			//#ifndef DOJA
			//#ifdef NK-s40
			if(ownVehicle) depthLevel++;
			//#endif
			
			//#ifdef MO-C65x
			//#endif
			
			//#endif
			y -= depthLevel * 2;
			//#endif
		//#endif
		if(bike != null) {
			vehicle.colX = x;	//last X
			// BIGTHOR
			/*
			if((vehicle.slowDown || vehicle.smoke) && vehicle.w_vel > 0) {
				if(rot >= 16) rot++;*/
				//spriteDraw(smoke, x - 50, y - 100, rot + 35 * (framepair & 1), smokeCoord, 1);
				//spriteDraw(smoke, x - 50, y - 100, 0, smokeCoord, 1);
			/*}*/
			
			//#ifdef FEW_FRAMES
			// DOJAPORT
			//#else
			spriteDraw(bike, x - 50, y - 100, rot + depthLevel * 35, coord, 3);
			//#endif
		//#ifdef J2ME
			//#ifndef FEW_FRAMES
			if(bike == bikeImageB && vehicle.w_vel > 0 && ((framepair++) & 0x01) != 0 && vehicle.status != -1) {
				spriteDraw(bikeWheelB, x - 50, y - 100, rot, bikeWBcoord, 1); //208 - 100 - 20
			}
			//#endif
			
			if(!ownVehicle) {
				//#ifdef BIG_PANEL
				//#else
				g.setClip(0, 0, canvasWidth, canvasHeight);
				g.clipRect(0, 0, canvasWidth, canvasHeight);
				//#endif
				g.setColor(0);
				
				String nick = pelaoNick;
				
				if(gameContraOponente && !ghostBeaten && tmpGhostNick != null) {
					nick = tmpGhostNick;
				}
				g.drawString(nick, x - (renderFont.stringWidth(nick) >> 1), y - 70 - renderFont.getHeight() + 8 * depthLevel, 20);
			}
			//#endif
			
			//#ifndef FEW_FRAMES
			if(vehicle.boostMode) {
				if(rot >= 16) rot++;
				//#ifdef J2ME
				spriteDraw(turbo, x - 50, y - 100 - 2, rot + 35 * (framepair & 1), turboCoord, 1);
				//#endif
			}
			//#endif
			
			
			//#ifndef FEW_FRAMES
			if((vehicle.slowDown || vehicle.smoke) && vehicle.w_vel > 0) {
				if(rot >= 16) rot++;
				spriteDraw(smoke, x - 50, y - 100, rot + 35 * (framepair & 1), smokeCoord, 1);
			}
			//#endif
		}
		
	}
	
	public int track_getNumOfPoints() {
		return track_arrayVxy.length >> 1;
	}

	public int track_getNumOfSegments() {
		return (track_getNumOfPoints() - 1) / 3;
	}

	public int track_getPointPosX(int psg) {
		int numsg  = track_arrayVxy.length >> 1;

		int index  = (psg % numsg) << 1;
		return track_arrayVxy[index];
	}

	public int track_getPointPosY(int psg) {
		int numsg  = track_arrayVxy.length >> 1;

		int index  = (psg % numsg) << 1;
		return track_arrayVxy[index + 1];
	}

	public byte track_getPointAlt(int psg) {
		int numsg  = track_arrayVxy.length >> 1;

		int index  = (psg % numsg);
		return track_arrayAlt[index];
	}
	
	public int track_getNumOfSubsegments(int sg) {
		return track_arrayLong[sg % track_getNumOfSegments()];
	}
	
	public int track_getNumOfAllSubsegments() {
		int r=0;
		for (int i = 0; i < track_getNumOfSegments(); ++i) {
			r += track_getNumOfSubsegments(i);
		}
		return r;
	}
	
	public void track_calcPosition(int sg, int fpsubsg) {
		if (track_getNumOfPoints() > 3) {
			sg %= (track_getNumOfPoints() - 1) / 3;

			int i    = sg * 3;

			int v1x  = track_getPointPosX(i);
			int v1y  = track_getPointPosY(i);
			int v2x  = track_getPointPosX(i + 1);
			int v2y  = track_getPointPosY(i + 1);
			int v3x  = track_getPointPosX(i + 2);
			int v3y  = track_getPointPosY(i + 2);
			int v4x  = track_getPointPosX(i + 3);
			int v4y  = track_getPointPosY(i + 3);

			int t    = fpsubsg;

			int ti   = toFP(1) - t;
			int t2   = mul(t, t);
			int ti2  = mul(ti, ti);

			int h1   = mul(ti2, ti);
			int h2   = 3 * mul(t, ti2);
			int h3   = 3 * mul(t2, ti);
			int h4   = mul(t2, t);

			int fx   = h1 * v1x + h2 * v2x + h3 * v3x + h4 * v4x;
			int fy   = h1 * v1y + h2 * v2y + h3 * v3y + h4 * v4y;

			// store result
			track_fpCalcPosX = fx;
			track_fpCalcPosY = fy;
			track_calcPosX = toInt(fx);
			track_calcPosY = toInt(fy);
			track_calcPosZ = 0;
		}
	}

	public int track_calcHeight(int sg, int fpsubsg) {

		if (track_getNumOfPoints() > 3) {
			sg %= (track_getNumOfPoints() - 1) / 3;

			int i    = sg * 3;
			int v1x  = track_getPointAlt(i);
			int v2x  = track_getPointAlt(i+1);
			int v3x  = track_getPointAlt(i+2);
			int v4x  = track_getPointAlt(i+3);

			int t    = fpsubsg;

			int ti   = toFP(1) - t;
			int t2   = mul(t, t);
			int ti2  = mul(ti, ti);

			int h1   = mul(ti2, ti);
			int h2   = 3 * mul(t, ti2);
			int h3   = 3 * mul(t2, ti);
			int h4   = mul(t2, t);

			int fx   = h1 * v1x + h2 * v2x + h3 * v3x + h4 * v4x;
			// store result
			track_calcPosZ = toInt(fx);
			return fx;
		}
		return 0;
	}
	
	public void track_calcPositionArray(int usid) {
		int index=0;
		int iseg=0;
		int numSegs = track_getNumOfSegments();
		
		try { 
			while (index + track_arrayLong[iseg] < usid) {
				index += track_arrayLong[iseg++];
			}
			
			int ipa = 0;
			int lastUsid = usid + TRACK_NUM_SECTIONS_AHEAD;
			int currentFpSub = usid - index;
			int currentNumSub = track_arrayLong[iseg];
			int deltaSub = div(ONE, toFP(track_arrayLong[iseg]));
			
			while (usid < lastUsid) {
				if (currentFpSub >= currentNumSub) {
					currentFpSub = 0;
					currentNumSub = track_arrayLong[++iseg];
				}
				
				// calculate point position
				track_calcPosition(iseg, currentFpSub * deltaSub);
				
				track_posArrayX[ipa] = track_calcPosX;
				track_posArrayY[ipa] = track_calcPosY;
				track_posArrayZ[ipa] = track_calcHeight(iseg, currentFpSub * deltaSub);
				
				++currentFpSub;
				++ipa;
				++usid;
			}
		} catch (Exception e) {
			//#ifdef com.mygdx.mongojocs.mr2.Debug
			System.out.println("Track not large enough");
			//#endif
			
		}	
	}
	
	public void track_precalculateSectionArray() {
		
		int index = 0;
		int len = track_getNumOfAllSubsegments();
		
		track_vertexSectionArray = new int[len*3];
		if (track_typeSectionArray==null || track_typeSectionArray.length != len)
			track_typeSectionArray = new byte[len];

		if (track_verticalTypeSectionArray==null || track_verticalTypeSectionArray.length != len)
			track_verticalTypeSectionArray = new byte[len];
		
		for (int sg = 0; sg < track_getNumOfSegments(); ++sg) {			
			int currentNumSub = track_arrayLong[sg];
			int deltaSub = div(ONE, toFP(currentNumSub));
			
			for (int ns = 0; ns < currentNumSub; ++ns) {

				// calculate point position
				track_calcPosition(sg, ns * deltaSub);
				
				track_vertexSectionArray[index  ] = track_calcPosX;
				track_vertexSectionArray[index+1] = track_calcPosY;
				track_vertexSectionArray[index+2] = track_calcHeight(sg, ns * deltaSub);
				
				index += 3;
			}
		}
	}
	
	public void track_update() {
		track_calculateLongitudes();
		track_precalculateSectionArray();
	}
	
	public void track_calcDirection(int sg, int fpsubsg) {

		track_calcPosition(sg, fpsubsg);

		int curPosX  = track_calcPosX;
		int curPosY  = track_calcPosY;

		track_calcPosition(sg, fpsubsg + ONE/10);

		int dirPosX  = track_calcPosX - curPosX;
		int dirPosY  = track_calcPosY - curPosY;

		// Normalize vector
		int fpudiv   = div(ONE, sqrt((dirPosX * dirPosX) + (dirPosY * dirPosY)));

		dirPosX *= fpudiv;
		dirPosY *= fpudiv;

		track_calcDirX = dirPosX;
		track_calcDirY = dirPosY;
	}


	public int track_addPoint(int px, int py, byte alt) {

		System.gc();
		//System.out.println(" Adding point "+arrayAlt.length+"  (mem "+Runtime.getRuntime().freeMemory()+")");
		
		// recreate arrays
		int lavxy     = track_arrayVxy.length + 2;
		int laalt     = track_arrayAlt.length + 1;
		int[] tavxy   = new int[lavxy];
		byte[] taalt  = new byte[laalt];

		System.arraycopy(track_arrayVxy, 0, tavxy, 0, track_arrayVxy.length);
		System.arraycopy(track_arrayAlt, 0, taalt, 0, track_arrayAlt.length);

		// Add the new point
		tavxy[lavxy - 2] = px;
		tavxy[lavxy - 1] = py;

		taalt[laalt - 1] = alt;

		// replace arrays
		track_arrayVxy = tavxy;
		track_arrayAlt = taalt;	
		
		return laalt - 1;
	}

	public void track_calculateLongitudes() {
		int numSegs  = track_getNumOfSegments();
		
		track_arrayLong = new int[numSegs];
		for (int i = 0; i < numSegs; ++i) {
			track_arrayLong[i] = toInt(track_getLongitudeFactor(i) / TRACK_LONGITUDE_FACTOR_DIV);
		}
	}

	public int track_getLongitudeFactor(int sg) {
		int longitude  = 0;
		sg %= (track_getNumOfPoints() - 1) / 3;
		int i          = sg * 3;
		if (track_getNumOfPoints() > 0) {
			int ox        = toFP(track_getPointPosX(i));
			int oy        = toFP(track_getPointPosY(i));
			int v1x       = track_getPointPosX(i);
			int v1y       = track_getPointPosY(i);
			int v2x       = track_getPointPosX(i + 1);
			int v2y       = track_getPointPosY(i + 1);
			int v3x       = track_getPointPosX(i + 2);
			int v3y       = track_getPointPosY(i + 2);
			int v4x       = track_getPointPosX(i + 3);
			int v4y       = track_getPointPosY(i + 3);

			int numSteps  = 10;
			for (int steps = 0; steps < numSteps; ++steps) {

				int t    = toFP(steps) / ((int) numSteps - 1);
				int t2   = mul(t, t);
				int t3   = mul(t, t2);

				int mit  = toFP(1) - t;

				int h1   = mul(mul(mit, mit), mit);
				int h2   = 3 * mul(mul(t, mit), mit);
				int h3   = 3 * mul(mul(t, t), mit);
				int h4   = mul(mul(t, t), t);

				int fx   = (int) (h1 * v1x + h2 * v2x + h3 * v3x + h4 * v4x);
				int fy   = (int) (h1 * v1y + h2 * v2y + h3 * v3y + h4 * v4y);

				longitude += sqrt(mul((fx - ox), (fx - ox)) + mul((fy - oy), (fy - oy)));

				ox = fx;
				oy = fy;
			}
		}
		return longitude;
	}

// *******************
// -------------------
// ===================
// *******************
private static final int maxPrecision = 15;
	private static final int maxDigits = 4;
	private static final int maxDigitsInt = 10000;
    
	/**
	 * number of fractional bits in all operations, do not modify directly
	 */
	public static int precision = 0;
	private static int frac_mask = 0;
    private static final int e = 178145;
    private static final int pi = 205887;
    private static int one_eighty_over_pi;
    private static int pi_over_one_eighty;
    public static int ONE, HALF, TWO, E, PI;
    /**
	 * small number used to round off errors
	 */
    public static final int EPSILON = 0x00000005;
    /**
	 * largest possible number
	 */
    public static final int INFINITY = 0x7fffffff;
    
    private static final int sk1 = 498;
    private static final int sk2 = 10882;
    private static int SK1, SK2;
    
    static final int as1 = -1228;
    static final int as2 = 4866;
    static final int as3 = 13901;
    static final int as4 = 102939;
    private static int AS1, AS2, AS3, AS4;
    
    static final int ln2 = 45426;
    static final int ln2_inv = 94548;
    private static int LN2, LN2_INV;
    
    private static final int lg[] = {
    		43690,
    		26214,
			18724,
			14563,
			11916,
			10036,
			9698
    };
    private static int LG[] = new int[lg.length];
    
    private static final int exp_p[] = {
    		10922,
			-182,
			4
    };
    private static int EXP_P[] = new int[exp_p.length];
	
    static {
    	setPrecision(maxPrecision);
    }
    
    /**
     * Converts an int to a FP.
     * @param x int to convert
     * @return FP
     */
	public static final int toFP(int x) {
		return (x < 0) ? -(-x << precision) : x << precision;
    }
	
	/**
     * Converts a FP to an int.
     * @param x FP to convert
     * @return int
     */
	public static final int toInt(int x) {
        return (x < 0) ? -(-x >> precision) : x >> precision;
    }
	
	/**
	 * Converts a FP to the current set precision.
	 * @param x the FP to convert
	 * @param p the precision of the FP passed in
	 * @return a FP of the current precision
	 */
	public static final int convert(int x, int p) {
		int num, xabs = abs(x);
		if (p > maxPrecision || p < 0) return x;
		if (p > precision)
			num = xabs >> (p - precision);
		else
			num = xabs << (precision - p);
		if (x < 0)
			num = -num;
		return num;
	}
	
	/**
	 * Converts a string to a FP.
	 * <br>
	 * <br>The string should trimmed of any whitespace before-hand.
	 * <br>
	 * <br>A few examples of valid strings:<br>
	 * <pre>
	 * .01
	 * 0.01
	 * 10
	 * 130.0
	 * -30000.12345
	 * </pre>
	 * @param s the string to convert
	 * @return FP
	 */
    public static final int toFP(String s)
    {
    	int x, i, integer, frac;
    	String fracstring = null;
        boolean neg = false;
        if (s.charAt(0) == '-') {
        	neg = true;
        	s = s.substring(1, s.length());
        }
        int index = s.indexOf('.');
        
        if (index < 0) {
        	integer = Integer.parseInt(s);
        } else if (index == 0) {
        	integer = 0;
        	fracstring = s.substring(1, s.length());
        } else if (index == s.length()-1) {
        	integer = Integer.parseInt(s.substring(0, index));
        } else {
        	integer = Integer.parseInt(s.substring(0, index));
        	fracstring = s.substring(index+1, s.length());
        }
        
        if (fracstring != null) {
        	if (fracstring.length() > maxDigits) fracstring = fracstring.substring(0, maxDigits);
        	frac = Integer.parseInt(fracstring);
        	for (i = 0; i < maxDigits - fracstring.length(); i++)
        		frac *= 10;
        } else
        	frac = 0;
        x = (integer << precision) + ((frac * ONE) / maxDigitsInt);
        if (neg)
        	x = -x;
        return x;
    }

    /**
     * Converts a FP to a string.
     * 
     * @param x the FP to convert
     * @param min_frac_digits minimum decimal digits in string (padded with zeros) 
     * @param max_frac_digits maximum decimal digits in string
     * @return a string representing the FP
     */

    public static final String toString(int x, int min_frac_digits, int max_frac_digits) {
    	int i, len;
        boolean neg = false;
        if (x < 0) {
        	neg = true;
        	x = -x;
        }
        String fracString = Integer.toString(((x & frac_mask) * maxDigitsInt) / ONE);
        len = maxDigits - fracString.length();
        for (i = 0; i < len; i++) fracString = "0" + fracString;
        
        for (i = fracString.length()-1; i >= 0; i--) {
        	if (fracString.charAt(i) != '0')
        		break;
        }
        fracString = fracString.substring(0, i+1);
        for (i = fracString.length(); i < min_frac_digits; i++) {
        	fracString += "0";
        }
        if (fracString.length() > max_frac_digits)
        	fracString = fracString.substring(0, max_frac_digits);
        if (fracString.length() > 0)
        	fracString = "." + fracString;
        return (neg ? "-" : "") + Integer.toString(x >> precision) + fracString;
    }

    /**
     * Converts a FP to a string.
     * @param x the FP to convert
     * @return a string representing the FP with a minimum of decimals in the string
     */
    public static final String toString(int x) {
        return toString(x, 0, maxDigits);
    }
    
    /**
     * Sets the precision for all fixed-point operations.
     * <br>
     * <br>The maximum precision is 13 bits by default.
     * @param p the desired precision in number of bits
     */
    public static final void setPrecision(int p) {
    	if (p > maxPrecision || p < 0) return;
    	int i;
        precision = p;
        PI = pi >> (16-p);
        ONE = 1 << p;
        HALF = (p > 0) ? 1 << (p-1) : 0;
        TWO = 1 << (p+1);
        E = e >> (16-p);
        SK1 = sk1 >> (16-p);
        SK2 = sk2 >> (16-p);
        AS1 = as1 >> (16-p);
        AS2 = as2 >> (16-p);
        AS3 = as3 >> (16-p);
        AS4 = as4 >> (16-p);
        LN2 = ln2 >> (16-p);
        LN2_INV = ln2_inv >> (16-p);
        for (i = 0; i < lg.length; i++) LG[i] = lg[i] >> (16-p);
        for (i = 0; i < exp_p.length; i++) EXP_P[i] = exp_p[i] >> (16-p);
        frac_mask = ONE-1;
        pi_over_one_eighty = div(PI, toFP(180));
        one_eighty_over_pi = div(toFP(180), PI);
    }

    public static final int abs(int x) {
    	return (x < 0) ? -x : x;
    }
    
    public static final int ceil(int x) {
    	boolean neg = false;
    	if (x < 0) {
    		x = -x;
    		neg = true;
    	}
    	if ((x&frac_mask) == 0)
    		return (neg) ? -x : x;
    	if (neg)
    		return -(x&~frac_mask);
    	return (x&~frac_mask) + ONE;
    }
    
    public static final int floor(int x) {
    	boolean neg = false;
    	if (x < 0) {
    		x = -x;
    		neg = true;
    	}
    	if ((x&frac_mask) == 0)
    		return (neg) ? -x : x;
    	if (neg)
    		return -(x&~frac_mask) - ONE;
    	return (x&~frac_mask);
    }
    
    /**
     * Removes the fractional part of a FP
     * @param x the FP to truncate
     * @return a truncated FP
     */
    
    public static final int trunc(int x) {
    	return (x < 0) ? -(-x&~frac_mask) : x&~frac_mask;
    }
    
    /**
     * Returns the fractional part of a FP
     * @param x FP to get fractional part of
     * @return positive fractional FP if input is positive, negative fractional otherwise
     */
    
    public static final int frac(int x) {
    	return (x < 0) ? -(-x&frac_mask) : x&frac_mask;
    }
    
    /**
     * Rounds a FP to the nearest whole number.
     * @param x the FP to round
     * @return a rounded FP
     */
    public static final int round(int x) {
    	boolean neg = false;
    	if (x < 0) {
    		x = -x;
    		neg = true;
    	}
    	x += HALF;
    	x &= ~frac_mask;
		return (neg) ? -x : x;
    }
    
    /**
     * Multiplies two FPs.
     * @param x
     * @param y
     * @return
     */
    public static final int mul(int x, int y) {
    	if (x == 0 || y == 0) return 0;
    	int xneg = 0, yneg = 0;
    	if (x < 0) {
    		xneg = 1;
    		x = -x;
    	}
    	if (y < 0) {
    		yneg = 1;
    		y = -y;
    	}
    	int res = ((x >> precision)*(y >> precision) << precision)
			+ (x & frac_mask)*(y >> precision)
			+ (x >> precision)*(y & frac_mask)
			+ ((x & frac_mask)*(y & frac_mask) >> precision);
    	if ((xneg^yneg) == 1)
    		res = -res;
    	return res;
    }
    
    /**
     * Divides two FPs.
     * @param x
     * @param y
     * @return
     */
    public static final int div(int x, int y) {
    	if (x == 0) return 0;
    	if (y == 0) return (x < 0) ? -INFINITY : INFINITY;
    	int xneg = 0, yneg = 0;
    	if (x < 0) {
    		xneg = 1;
    		x = -x;
    	}
    	if (y < 0) {
    		yneg = 1;
    		y = -y;
    	}
    	int msb = 0, lsb = 0;
    	while ((x & (1 << 30 - msb)) == 0)
    		msb++;
    	while ((y & (1 << lsb)) == 0)
    		lsb++;
    	int shifty = precision - (msb+lsb);
    	int res = ((x << msb) / (y >> lsb));
    	if (shifty > 0)
    		res <<= shifty;
    	else
    		res >>= -shifty;
    	if ((xneg^yneg) == 1)
    		res = -res;
    	return res;
    }
    
    public static final int sqrt(int x) {
    	int s = (x + ONE) >> 1;
    	for (int i = 0; i < 8; i++) {
    	    s = (s + div(x, s)) >> 1;
    	}
    	return s;
    }

    /**
     * FP angle must be in radians.
     * 
     * 
     * @param f the angle in radians
     * @return sin() of the angle
     */
    public static final int sin(int f) {
		int sign = 1;
		int PI_OVER_2 = PI/2;
		f %= PI*2;
		if(f < 0)
            f = PI*2 + f;
		if ((f > PI_OVER_2) && (f <= PI)) {
		    f = PI - f;
		} else if ((f > PI) && (f <= (PI + PI_OVER_2))) {
		    f = f - PI;
		    sign = -1;
		} else if (f > (PI + PI_OVER_2)) {
		    f = (PI<<1)-f;
		    sign = -1;
		}
	
		int sqr = mul(f,f);
		int result = SK1;
		result = mul(result, sqr);
		result -= SK2;
		result = mul(result, sqr);
		result += ONE;
		result = mul(result, f);
		return sign * result;
    }
    
    /**
     * FP angle must be in radians.
     * 
     * 
     * @param f the angle in radians
     * @return cos() of the angle
     */
    public static final int cos(int f) {
    	return sin(PI/2 - f);
    }
    
    /**
     * FP angle must be in radians.
     * 
     * 
     * @param f the angle in radians
     * @return tan() of the angle
     */
    public static final int tan(int f) {
    	return div(sin(f), cos(f));
    }

    
    /**
     * input range: [-1, 1] -- output range: [-PI/2, PI/2]
     * @param f
     * @return
     */
    public static final int asin(int f) {
    	boolean neg = false;
    	if (f < 0) {
    		neg = true;
    		f = -f;
    	}
		int fRoot = sqrt(ONE-f);
		int result = AS1;
		result = mul(result, f);
		result += AS2;
		result = mul(result, f);
		result -= AS3;
		result = mul(result, f);
		result += AS4;
		result = PI/2 - (mul(fRoot,result));
		if (neg)
			result = -result;
		return result;
    }
    
    /**
     * input range: [-1, 1] -- output range: [0, PI]
     * @param f
     * @return
     */
    public static final int acos(int f) {
        return PI/2 - asin(f);
    }
    
    /**
     * input range: [-INF, INF] -- output range: [-PI/2, PI/2]
     * @param f
     * @return
     */
    public static final int atan(int f) {
        return asin(div(f, sqrt(ONE + mul(f, f))));
    }
    
    /**
     * input range: [-INF, INF] -- output range: [-PI, PI] -- Converts (x,y) in Cartesian to (r, <i>theta</i>) in polar, and returns <i>theta</i>.
     * @param y
     * @param x
     * @return
     */
    public static final int atan2(int y, int x) {
        if (y == 0) {
        	if (x >= 0) {
        		return 0;
        	} else if (x < 0) {
        		return PI;
        	}
        } else if (x == 0) {
        	return (y > 0) ? PI/2 : -PI/2;
        }
        int z = atan(abs(div(y, x)));
        if (x > 0) {
        	return (y > 0) ? z : -z;
        } else {
        	return (y > 0) ? PI - z : z - PI;
        }
    }
    
    /**
     * E raised to the FP x (e**x)
     * @param x
     * @return
     */
    public static final int exp(int x) {
    	if (x == 0) return ONE;
    	int xabs = abs(x);
    	int k = mul(xabs, LN2_INV);
    	k += HALF;
    	k &= ~frac_mask;
    	if (x < 0)
    		k = -k;
    	x -= mul(k, LN2);
    	int z  = mul(x,x);
    	int R = TWO + mul(z,EXP_P[0]+mul(z,EXP_P[1]+mul(z,EXP_P[2])));
    	int xp = ONE + div(mul(TWO,x), R - x);
    	if (k < 0)
    		k = ONE >> (-k >> precision);
    	else
    		k = ONE << (k >> precision);
    	return mul(k, xp);
    }
    
    /**
     * Natural logarithm ln(x)
     * @param x
     * @return
     */
    public static final int log(int x) {
    	if (x < 0) return 0;
    	if (x == 0) return -INFINITY;
    	int log2 = 0, xi = x;
    	while (xi >= TWO) {
    		xi >>= 1;
    		log2++;
    	}
    	int f = xi - ONE;
    	int s = div(f,TWO+f); 
    	int z = mul(s,s);
    	int w = mul(z,z);
    	int R = mul(w,LG[1]+mul(w,LG[3]+mul(w,LG[5]))) + mul(z,LG[0]+mul(w,LG[2]+mul(w,LG[4]+mul(w,LG[6]))));
    	return mul(LN2, (log2 << precision)) + f - mul(s,f - R);
    }
    
    /**
     * Logarithm with specified FP base
     * @param x
     * @return
     */
    public static final int log(int x, int base) {
    	return div(log(x),log(base));
    }
    
    /**
     * FP x raised to FP y.
     * @param x
     * @param y
     * @return
     */
    public static final int pow(int x, int y) {
    	if (y == 0) return ONE;
    	if (x < 0) return 0;
    	return exp(mul(log(x), y));
    }
    
    public static final int min(int x, int y) {
        return (x < y) ? x : y;
    }
    
    public static final int max(int x, int y) {
        return (x > y) ? x : y;
    }

    /**
     * Converts degrees to radians.
     * @param x FP in degrees
     * @return FP in radians
     */
    public static final int deg2rad(int x) {
    	return mul(x, pi_over_one_eighty);
    }
    
    /**
     * Converts radians to degrees.
     * @param x FP in radians
     * @return FP in degrees
     */
    public static final int rad2deg(int x) {
    	return mul(x, one_eighty_over_pi);
    }
// -------------------
// ===================

/*
	public void createTrackSection(int flgs, int[] pl, int[] ll,int idx, boolean isVert) {
		int realPL = 0;
		int realLL = 0;
		
		if(!isVert) {
			//byte flags = ((byte)flgs);
			int[] patchList = null;
			int[] lineList = null;
			
			if(pl == null) {
				patchList = null;
			}  else {
				for(int i = 0; i < pl.length; i += TRACK_INTS_PER_PATCH) {
					if(pl[i + TRACK_INTS_PER_PATCH - 1] <= max_detail) {
						realPL++;
					}
				}
				patchList = new int[realPL * TRACK_INTS_PER_PATCH];
				realPL = 0;
				for(int i = 0; i < pl.length; i += TRACK_INTS_PER_PATCH) {
					if(pl[i + TRACK_INTS_PER_PATCH - 1] <= max_detail) {
						for(int j = 0; j < TRACK_INTS_PER_PATCH; j++) {
							patchList[realPL + j] = pl[i + j];
						}
						realPL += TRACK_INTS_PER_PATCH;
					}
				}
			}
			
			if(ll == null) {
				lineList = null;
			}  else {
				for(int i = 0; i < ll.length; i += TRACK_INTS_PER_LINE) {
					if(ll[i + TRACK_INTS_PER_LINE - 1] <= max_detail) {
						realLL++;
					}
				}
				lineList = new int[realLL * TRACK_INTS_PER_LINE];
				realLL = 0;
				for(int i = 0; i < ll.length; i += TRACK_INTS_PER_LINE) {
					if(ll[i + TRACK_INTS_PER_LINE - 1] <= max_detail) {
						for(int j = 0; j < TRACK_INTS_PER_LINE; j++) {
							lineList[realLL + j] = ll[i + j];
						}
						realLL += TRACK_INTS_PER_LINE;
					}
				}
			}
			track_trackSectionsMap_patchList[idx] = patchList;
			track_trackSectionsMap_lineList[idx] = lineList;
			track_trackSectionsMap_flags[idx] = ((byte)flgs);
		} else {
			track_verticalTrackSectionsMap_patchList[idx] = pl;
			track_verticalTrackSectionsMap_lineList[idx] = ll;
			track_verticalTrackSectionsMap_flags[idx] = ((byte)flgs);
		}
	}
*/
// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------

//#ifdef DOJA
// DOJAPORT VARIABLES <________________________________________________________
// DOJAPORT VARIABLES >_________________________________________________________
//#endif

//#ifdef RENDER_QUALITY_LOW
//#endif

// ******************* //
//  Final de la Clase  //
// ******************* //
};