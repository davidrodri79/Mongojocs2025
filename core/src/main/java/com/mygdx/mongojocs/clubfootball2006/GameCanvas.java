

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.7 (08.4.2005)
// Representacion Grafica
// ------------------------------------


//#define NOTOURNAMENT

//#ifdef WEBSUBSCRIPTION
	//#define NOSCROLLBAR
	//#define NOSAMPLESHIRT
	//#define NOMINIFIELD
	//#define NOTEAMLOGOATSELECT
	//#define NOBACKGROUNDBARS
	//#define NOCHEAT
//#endif


//#ifdef PACKAGE
package com.mygdx.mongojocs.clubfootball2006;
//#endif


//#ifdef J2ME

//#elifdef DOJA
    //#endif

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.mongojocs.midletemu.Canvas;
//import com.mygdx.mongojocs.midletemu.Runnable;
import com.mygdx.mongojocs.midletemu.CommandListener;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Random;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.sanfermines2006.Game Canvas - Bios
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

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga) 
{
	this.ga = ga;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME
public void showNotify()
{

	//#ifndef NOINTERRUPTIONPROCESS
	
	//#ifdef DebugConsole
		Debug.println (" -+- showNotify()");
	//#endif
		
		gamePaused = false;
	
		gameForceRefresh = true;
		gameSoundRefresh = true;
	
		intKeyX = intKeyY = intKeyMisc = intKeyMenu = 0;
		
	//#endif
}

public void hideNotify()
{
	//#ifndef NOINTERRUPTIONPROCESS

		//#ifdef DebugConsole
		Debug.println (" -+- hideNotify()");
		//#endif
						
		gamePaused = true;
			
		gameSoundOld = soundOld;
		gameSoundLoop = soundLoop;
		gameSound = false;
		//#ifdef STOPVIBRAATHIDENOTIFY
		//#endif
		//#ifdef NULLMIDIPATCH
		//#else
		soundStop();
		//#endif
	
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

long gameMilis, CPU_Milis;
int gameSleep;
final static int mainSleep = 70;
//Integer tDifMutex = new Integer(0);
long menuStart = 0;
//long timeDiff = 0;
//long timeDiff2 = 0;

//#ifndef NOCHEAT
boolean cheatActive = false;
int iCheat;
final static int stringCheat[] = {10,4,6,6,6,5,5,5,-6};
//#endif

public void runInit()
{
	System.gc();
	try{
		biosCreate();
	}catch(Exception e){

		//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		Debug.println("Fallo en inicializacion");
		Debug.println(e.toString());
		//#endif
	}
	System.gc();

	//#ifndef FORCESTARTUPREPAINT
	//gameDraw();
	//#endif

	//try	{Thread.sleep( 100000 );} catch(Exception e) {}
}

public void runTick()
{
	//while (!gameExit)
	{
		gameMilis = System.currentTimeMillis();

		if (!gamePaused)
		{
			// KEYBOARD TICK
			lastKeyMenu = keyMenu;	// Keys del Frame Anterior
			lastKeyX    = keyX;
			lastKeyY    = keyY;
			lastKeyMisc = keyMisc;

			keyMenu = intKeyMenu;	// Keys del Frame Actual
			keyX    = intKeyX;
			keyY    = intKeyY;
			keyMisc = intKeyMisc;

			//#ifndef NOCHEAT
			if(stringCheat[iCheat] == -6){iCheat = 0;cheatActive = !cheatActive;}
			if (keyMisc != 0 && lastKeyMisc == 0)
				if (stringCheat[iCheat] == keyMisc) iCheat++; else iCheat = 0;
			//#endif

			if (limpiaKeyMenu) {
				limpiaKeyMenu=false;
				if(intKeyMenu != 2)
					intKeyMenu = 0;
			}

			try {
				//#ifdef DebugTime
				biosTick_milis = System.currentTimeMillis();
				//#endif
				biosTick();
				//#ifdef DebugTime
				biosTick_milis = System.currentTimeMillis()-biosTick_milis;
				//#endif
				if (gameForceRefresh) {

					gameForceRefresh = false;
					//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
					Debug.println ("biosRefresh()");
					Debug.println ("biosStatus="+biosStatus);
					//#endif

					switch (biosStatus)
					{
						// --------------------
						// game Refresh
						// ====================
						case BIOS_GAME:
							//////////////////////////////////////
							//gameRefresh();
							//////////////////////////////////////
							switch(gameStatus)
							{
								case GAME_MATCH_END_LED:
								case GAME_HALF_MATCH_LED:
								case GAME_GOAL_LED:
								case GAME_PENALTY_LED:
								case GAME_PLAY_TICK:
									gameStatus = GAME_MENU_SECOND;
									//#ifndef NOLED
									ledFirstDraw = true;
									//#endif
									break;
								//#ifndef DOJA
							/*case GAME_PICTURE:
							case GAME_MENU_FORMATION:
							case GAME_MENU_CLASSIFICATION:
							case GAME_VERSUS_SCREEN:
							case GAME_MATCH_ENDED:
							case GAME_LEAGUE_END:
							case GAME_MENU_EDIT_CUSTOM:
								playShow = true;
								break;*/
								//#endif
							}
							playShow = true;
							break;
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
							//#ifdef DOJA
							//#else
							int t = menuType;
							int tPos = menuListPos;
							int menuTypeBackCopy = menuTypeBack;

							menuInit(t);
							menuTypeBack = menuTypeBackCopy;

							if (menuListMode == ML_OPTION)
								menuListPos=tPos;

							//#endif
							break;
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
				gameDraw();

				if (gameSoundRefresh)
				{
					gameSoundRefresh = false;

					soundStop();
					/*
				//#ifndef NK-s60
					if (gameSoundOld >= 0 && gameSoundLoop==0) {soundPlay(gameSoundOld,gameSoundLoop);}
				//#else
					gameSound = false;
				//#endif
				 */

				} else {
					soundTick();
				}

			} catch (Exception e)
			{
				//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
				Debug.println("*** Exception Logica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
				//#endif
			}
		}
		//#ifdef DebugTime
		Debug.println("# biosTick_milis: "+biosTick_milis);
		Debug.println("# debug_totalmilis: "+(System.currentTimeMillis() - gameMilis));
		//#endif
		gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
		try	{Thread.sleep( gameSleep < 1? 1:gameSleep );} catch(Exception e) {}

		//System.gc();

	}

	if(gameExit) {
		soundStop();
//biosDestroy();
		savePrefs();
//gameDestroy();
		ga.destroyApp(true);
	}
}

public void runEnd()
{
}

public void run()
{
	System.gc();
	try{
		biosCreate();
	}catch(Exception e){
		
		//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		Debug.println("Fallo en inicializacion");
		Debug.println(e.toString());
		//#endif
	}
	System.gc();

	//#ifndef FORCESTARTUPREPAINT
	//gameDraw();
	//#endif

	//try	{Thread.sleep( 100000 );} catch(Exception e) {}
	
	while (!gameExit)
	{
		gameMilis = System.currentTimeMillis();

		if (!gamePaused)
		{
		    // KEYBOARD TICK
			lastKeyMenu = keyMenu;	// Keys del Frame Anterior
			lastKeyX    = keyX;
			lastKeyY    = keyY;
			lastKeyMisc = keyMisc;

			keyMenu = intKeyMenu;	// Keys del Frame Actual
			keyX    = intKeyX;
			keyY    = intKeyY;
			keyMisc = intKeyMisc;
			
			//#ifndef NOCHEAT
            if(stringCheat[iCheat] == -6){iCheat = 0;cheatActive = !cheatActive;}
            if (keyMisc != 0 && lastKeyMisc == 0)            
                if (stringCheat[iCheat] == keyMisc) iCheat++; else iCheat = 0;
            //#endif

			if (limpiaKeyMenu) {
				limpiaKeyMenu=false;
				if(intKeyMenu != 2)
				intKeyMenu = 0;
			}

			try {
                //#ifdef DebugTime    
                biosTick_milis = System.currentTimeMillis();
                //#endif
				biosTick();
                //#ifdef DebugTime    
                biosTick_milis = System.currentTimeMillis()-biosTick_milis;
                //#endif
				if (gameForceRefresh) {
					
					gameForceRefresh = false; 
					//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
					Debug.println ("biosRefresh()");
					Debug.println ("biosStatus="+biosStatus);
					//#endif
				
					switch (biosStatus)
					{
				// --------------------
				// game Refresh
				// ====================
					case BIOS_GAME:
					    //////////////////////////////////////
						//gameRefresh();
						//////////////////////////////////////
						switch(gameStatus)
						{
							case GAME_MATCH_END_LED:
							case GAME_HALF_MATCH_LED:
							case GAME_GOAL_LED:
							case GAME_PENALTY_LED:												
							case GAME_PLAY_TICK:
								gameStatus = GAME_MENU_SECOND;
								//#ifndef NOLED
								ledFirstDraw = true;
								//#endif
							break;
							//#ifndef DOJA
							/*case GAME_PICTURE:															
							case GAME_MENU_FORMATION:
							case GAME_MENU_CLASSIFICATION:
							case GAME_VERSUS_SCREEN:
							case GAME_MATCH_ENDED:
							case GAME_LEAGUE_END:
							case GAME_MENU_EDIT_CUSTOM:
								playShow = true;
								break;*/
							//#endif
						}
						playShow = true;
					break;
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
						//#ifdef DOJA
						//#else
						int t = menuType;
						int tPos = menuListPos;
						int menuTypeBackCopy = menuTypeBack;
																	
						menuInit(t);
						menuTypeBack = menuTypeBackCopy; 
												
						if (menuListMode == ML_OPTION)
							menuListPos=tPos;
						
						//#endif
					break;
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
				gameDraw();
                
				if (gameSoundRefresh)
				{
					gameSoundRefresh = false;
										
					soundStop();
					/*
				//#ifndef NK-s60
					if (gameSoundOld >= 0 && gameSoundLoop==0) {soundPlay(gameSoundOld,gameSoundLoop);}
				//#else
					gameSound = false;
				//#endif				 
				 */
					
				} else {
					soundTick();
				}

			} catch (Exception e)
			{
			//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
				Debug.println("*** Exception Logica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}
		}
        //#ifdef DebugTime
        Debug.println("# biosTick_milis: "+biosTick_milis);
        Debug.println("# debug_totalmilis: "+(System.currentTimeMillis() - gameMilis));
        //#endif
		gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
		try	{Thread.sleep( gameSleep < 1? 1:gameSleep );} catch(Exception e) {}

	//System.gc();

	}

	soundStop();
	
	//biosDestroy();
	savePrefs();
	//gameDestroy();
	
	ga.destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//#ifdef DebugTime
long biosTick_milis, showfield_milis, drawplayer_milis;
//#endif

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
//#ifdef DebugTime    
long paintMilis;
//#endif

public void paint (Graphics g)
{       
    //#ifdef DebugTime    
    paintMilis = System.currentTimeMillis();
    //#endif
	synchronized (this)
	{
		
		if (canvasShow)
		{
			canvasShow=false;
		
			scr = g;
            //#ifdef NOKIAUI
            dscr = DirectUtils.getDirectGraphics(scr);
            //#endif

		//#ifdef DOJA
		//#endif

        	try {
        		
				// canvasDraw();
        		if (canvasFillShow) { canvasFillShow=false; canvasFillDraw(canvasFillRGB); }

        		if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }

        		if (canvasTextShow) { 
        			canvasTextShow=false; 
        			//canvasTextDraw(); 
        			textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
        		}

        		if (biosStatus == BIOS_MENU && gameStatus == GAME_PLAY_TICK && menuListShow) {playShow = true;}

        		if (playShow) { playShow=false; playDraw(); }

        		menuListDraw(scr);
				
			} catch (Exception e)
			{
			//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
				Debug.println("*** Exception Grafica ***");
				Debug.println("biosStatus:"+biosStatus);
				Debug.println("gameStatus:"+gameStatus);
				e.printStackTrace();
			//#endif
			}
	    
//#ifdef DebugTime    
    paintMilis = System.currentTimeMillis()-paintMilis;
    Debug.println("# paintMilis: "+paintMilis);
//#endif

		
//#ifdef DOJA
//#endif
	
//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
// El MIDlet llama a esta funci�n cuando se PULSA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyMenu, keyX, keyY, keyMisc;
int intKeyMenu, intKeyX, intKeyY, intKeyMisc;
int lastKeyMenu, lastKeyX, lastKeyY, lastKeyMisc;
boolean limpiaKeyMenu = false;

//#ifdef J2ME

public void keyPressed(int keycode)
{
	intKeyMenu = intKeyX = intKeyY = intKeyMisc = 0;
	
	//#ifdef ALWAYSMENUKEYCLEAR
	//#endif

	switch (keycode)
	{
	//#ifdef NK-3650
	//#else
	case Canvas.KEY_NUM0:
		intKeyMisc=10;
		intKeyMenu= 1;
	return;

	case Canvas.KEY_NUM1:
		intKeyMisc=1;
		intKeyX = -1;
		intKeyY = -1;
	return;

	case Canvas.KEY_NUM2:	// Arriba
		intKeyMisc=2; 
		intKeyY=-1;
	return;

	case Canvas.KEY_NUM3:
		intKeyMisc=3; 
		intKeyX = 1; 
		intKeyY = -1;
	return;

	case Canvas.KEY_NUM4:	// Izquierda
		intKeyMisc=4; 
		intKeyX=-1;
	return;

	case Canvas.KEY_NUM5:	// Disparo
		intKeyMisc=5;
		intKeyMenu=2;
	return;

	case Canvas.KEY_NUM6:	// Derecha
        intKeyMisc=6; 
        intKeyX=1;
	return;

	case Canvas.KEY_NUM7:
		intKeyMisc=7; intKeyX=-1;intKeyY= 1;
	return;

	case Canvas.KEY_NUM8:	// Abajo
		intKeyMisc=8; 
        intKeyY=1;
	return;

	case Canvas.KEY_NUM9:
		intKeyMisc=9; intKeyX = 1;intKeyY = 1;
	return;

	case 35:		// *
		intKeyMisc=11;
	return;

	case 42:		// #
		intKeyMisc=12;
	return;
	//#endif

// -----------------------------------------

//#ifdef PA-VS3
//	#ifdef PA-X500
//#elifdef VI-TSM100
//#elifdef MO-T720
//#elifdef MO-E1070
//#elifdef MO-V3xx
//#elifdef MO-V9xx
//#elifdef MO-C450
//#elifdef MO-C65x
//#elifdef AL-otx56
//#elifdef LG-U8150
//#elifdef LG-U8210
//#elifdef SG-Z105
//#elifdef SM-MYVxx
//#elifdef SM-MYV56
//#elifdef SM-MYV76
//#elifdef SD
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
//#elifdef SH-902
//#elifdef SH-903
//#elifdef O2
//#endif
	}

	/*switch(getGameAction(keycode))
	{
		case 1:	intKeyY= -1;	return;	// Arriba
		case 6:	intKeyY=  1;	return;	// Abajo
		case 5:	intKeyX=  1;	return;	// Derecha
		case 2:	intKeyX= -1;	return;	// Izquierda
		case 8:	intKeyMenu= 2;	return;	// Fuego
	//#ifdef VI-TSM100
	//#endif
	}*/
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
//boolean gameLight = true;

/*
static final int BIOS_GAME = 0;
static final int BIOS_LOGOS = 11;
static final int BIOS_MENU = 22;*/
//PROVA
static final int BIOS_GAME = 0;
static final int BIOS_LOGOS = 1;
static final int BIOS_MENU = 2;


//static final int BIOS_POPUP = 33;

// -------------------
// bios Create
// ===================

String[][] gameText, legalText;

public void biosCreate()
{
// --------------------------------
// Forzamos a FULL CANVAS para J2ME / MIDP 2.0
// ================================
//#ifdef J2ME
	//#ifdef FULLSCREEN
		//#ifdef MIDP20
		//#endif
		//canvasHeight -= listenerHeight;
	//#endif
//#endif
// ================================

	//#ifdef SMALLUI
	//#else
	canvasTextCreate(10,0,canvasWidth - 20,canvasHeight);
	//#endif

	menuListFont =
//#ifdef J2ME
	Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN ,
	//#ifdef AVOIDSMALLFONT
	//#else
	Font.SIZE_SMALL
	//#endif
	);
//#elifdef DOJA
//#endif	
	menuListFontHeight = menuListFont.getHeight();
	
// --------------------------------
// Inicializamos com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug Engine
// ================================
//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.debugCreate(this);
//#endif
// ================================


// --------------------------------
// Pintamos TODO el canvas de color NEGRO de forma INMEDIATA
// ================================
	//canvasFillTask(0xffffff);		// Color Blanco
    canvasFillRGB = 0xffffff;
    canvasFillShow = true;
    //#ifdef J2ME
	canvasImg = loadImage("/loading");
	//#endif
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
	rmsCreate(4 * 1024);		// Solicitamos 10Kb
//#endif
// ================================

	 //#ifdef FORCESTARTUPREPAINT
    //canvasImg = loadImage("/loading"); gameDraw();
    //#endif
	
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("Creaci�n de textos...");
	//#endif

	gameText = textosCreate( loadFile("/Textos.txt") );
	
	legalText = textosCreate( loadFile("/clubLegal.txt") );

//#ifdef J2ME
	String version = "MONGOJOCS";//ga.getAppProperty("MIDlet-Version");
//#elifdef DOJA
//#endif

	if (version != null) {gameText[TEXT_ABOUT_SCROLL][1] = version;}
	
	//#ifdef FORCESTARTUPREPAINT
	//canvasImg = loadImage("/loading"); gameDraw();
	//#endif
	
		
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("Personalizando datos de UI...");
	//#endif
	
	String tempStr[][] = textosCreate( loadFile("/uicolors.txt") );
	int tempRGB[] = new int[tempStr.length];
	
	for(int i = 0; i < tempStr.length; i++)
	{	    
		tempRGB[i] = (int)((Integer.valueOf(tempStr[i][0]).intValue() << 16) | 
						   (Integer.valueOf(tempStr[i][1]).intValue() << 8) |  
						    Integer.valueOf(tempStr[i][2]).intValue());	
		
	}
	
	UI_BACKGROUND_RGB     =	tempRGB[0];
	UI_FONT_RGB           =	tempRGB[1];
	UI_FONTOUTLINE_RGB    = tempRGB[2];
	UI_DARK_RGB           =	tempRGB[3];
	UI_LIGHT_RGB          =	tempRGB[4];
	UI_FRAME_RGB          =	tempRGB[5];
	UI_TOPBORDER_RGB      =	tempRGB[6];
	UI_BOTBORDER_RGB      =	tempRGB[7];
	UI_TABLETITLE_RGB     = tempRGB[8];
	UI_TABLEFONT_RGB      =	tempRGB[9];
	UI_PICTUREWINDOW_RGB  =	tempRGB[10];
	
	//#ifdef FORCESTARTUPREPAINT
	//canvasImg = loadImage("/loading"); gameDraw();
	//#endif
	
	tempStr = textosCreate( loadFile("/headline.txt") );
	
	headLineTeam = (byte)Integer.valueOf(tempStr[0][0]).intValue();
	domHeadLineTeam = (byte)Integer.valueOf(tempStr[0][1]).intValue();

	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("com.mygdx.mongojocs.sanfermines2006.Game create...");
	//#endif

	gameCreate();
	
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("Carga banderas...");
	//#endif
	
    //#ifdef J2ME
    imgFlags = loadImage("/flags");
    //#elifdef DOJA
    //#endif
        	
    //#ifdef LARGEUI  
    	//#ifdef J2ME
    	loadTeamsData(-1, -1, true);
    	bigFlagsImg = loadImage("/bigFlags");    	
    	//#elifdef DOJA
    	//#endif
    //#endif
    	
    //#ifdef FORCESTARTUPREPAINT
    //canvasImg = loadImage("/loading"); gameDraw();
    //#endif
}


// -------------------
// bios Destroy
// ===================

/*public void biosDestroy()
{
	savePrefs();

	gameDestroy();
}*/

// -------------------
// bios Tick
// ===================

public void biosTick()
{

//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
        if ( (System.currentTimeMillis() - timeIniLogos) < timeLogos) {return;}
    
        if (cntLogos == numLogos) {//return true;
        biosStatus = BIOS_GAME;return;
        }
    
        canvasFillRGB = rgbLogos[cntLogos];
        canvasFillShow = true;
    
        //canvasFillTask(rgbLogos[cntLogos]);
    
        canvasImg = loadImage(nameLogos[cntLogos]);
    
        cntLogos++;
    
        timeIniLogos = System.currentTimeMillis();
    
        //return false;
	
		//if ( logosTick() ) {biosStatus = BIOS_GAME;}
	return;
// --------------------

// --------------------
// menu Bucle
// --------------------
	case BIOS_MENU:
        if ( menuListTick( (keyY!=lastKeyY? keyY: 0 ), (keyX!=lastKeyX? keyX:0), (keyMenu!=lastKeyMenu? keyMenu:0) ) )
        {
            menuAction(menuListCMD);
        }

        //if (menuExit) {return true;}

        //return false;
	
		if ( menuExit
		//menuTick() 
		) {biosStatus = BIOS_GAME; }
	return;
// --------------------

// --------------------
// popup Bucle
// --------------------
//	case BIOS_POPUP:
//		if ( popupTick() ) {biosStatus = biosStatusOld;}
//	return;
// --------------------
	}
}

// -------------------
// bios Refresh
// ===================

/*public void biosRefresh()
{
//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug.println ("biosRefresh()");
	com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug.println ("biosStatus="+biosStatus);
//#endif

	switch (biosStatus)
	{
// --------------------
// game Refresh
// ====================
	case BIOS_GAME:
	    //////////////////////////////////////
		//gameRefresh();
		//////////////////////////////////////
		
		
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
}*/

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// logos - Engine
// ===================
// *******************

int numLogos = 0;
int cntLogos = 0;
final static String[] nameLogos = {"/jamdat", "/codemasters"};
final static int[] rgbLogos = {0xffffff, 0xffffff};
final static int timeLogos = 3000;
long timeIniLogos;
/*
public void logosInit(String[] fileNames, int[] rgb, int time)
{
	nameLogos = fileNames;
	numLogos = rgb.length;
	cntLogos = 0;
	rgbLogos = rgb;
	timeLogos = time;

	timeIniLogos = System.currentTimeMillis() - timeLogos;

	biosStatus = BIOS_LOGOS;
}*/

/*
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
*/

// <=- <=- <=- <=- <=-









































// *******************
// -------------------
// menuList - Engine - Rev.0 (20.6.2003)
// ===================
// *******************

boolean menuList_ON=false;		// Estado del menuList Engine
boolean menuListShow=false;

int menuListMode;

int menuListPos;
int menuListCMD;
int menuListBackwardCMD;

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

	menuListPos=0;
	menuListBackwardCMD = ACTION_NONE;

	menuListTabIndex = null;
}

// -------------------
// menuList Add
// ===================

public void menuListAdd(int Dato, String Texto)
{
	
	if ( menuListFont.stringWidth(Texto) <= menuListSizeX)
	{
		menuListAdd(Dato, new String[] {Texto}, 0, 0);
	} else {

		String[] Textos = textBreak(Texto, menuListSizeX, menuListFont );

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

	Font f = menuListFont;

	int TextoSizeX = 2;
	for (int SizeX, t=0 ; t<Texto.length ; t++) {SizeX=menuListFont.stringWidth(Texto[t]); if ( TextoSizeX < SizeX ) {TextoSizeX=SizeX;}}


	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2, TextoSizeX, menuListFontHeight };

	menuListStr=Str;
	menuListDat=Dat;
}


// -------------------
// menuList Set
// ===================
/*
public void menuListSet_Text()
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_TEXT;
}
*/
/*public void menuListSet_Scroll()
{
	menuListScrY = menuListSizeY;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCROLL;

	menuListScrSizeY = 0; for (int i=0 ; i<menuListDat.length ; i++) {menuListScrSizeY += menuListDat[i][4];}
}*/

/*public void menuListSet_Option()
{
	menuListSet_Option(0);
}
*/
public void menuListSet_Option()
{
	menuListPos=0;
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
			if (menuListStr[act][0] == " ")
			{
				act++;
			} else {
				pos = act;
				size = 0;
				ultSpc = -1;
				skipSpc = false;
			}

		} else {

			if (menuListStr[act][0] == " ")
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



// -------------------
// menuList Tick
// ===================


//#ifdef AUTOREFRESH
//int menuTimer;
//#endif

public boolean menuListTick(int movY, int movX, int boton)
{
	boolean fire = boton>0, back = boton<0;

	//#ifdef AUTOREFRESH
	//menuTimer++;
	//if (menuTimer%6 == 0) menuListShow=true;
	//#endif
	
	switch (menuListMode)
	{
	case ML_OPTION:

		if (movY ==-1 && menuListPos > 0) {menuListPos--; menuListShow=true;}
		else
		if (movY == 1 && menuListPos < menuListDat.length-1) {menuListPos++; menuListShow=true;}
		/*else
		if (movY ==-1 && menuListPos == 0) {menuListPos = menuListDat.length-1; menuListShow=true;}
		else
		if (movY == 1 && menuListPos == menuListDat.length-1) {menuListPos = 0; menuListShow=true;}*/

		if(movX != 0)
		if(menuListStr[menuListPos].length > 1){
		
			menuListDat[menuListPos][2] =  (menuListStr[menuListPos].length + menuListDat[menuListPos][2] + movX) % menuListStr[menuListPos].length;
			menuListCMD=menuListDat[menuListPos][1];
			menuListShow=true;
			return true;
		}

		if (fire)
		{
			if (++menuListDat[menuListPos][2] == menuListStr[menuListPos].length) {menuListDat[menuListPos][2]=0;}
			menuListCMD=menuListDat[menuListPos][1];
			menuListShow=true;
			return true;
		}
		
		if (back) 
		{			
			menuListCMD = menuListBackwardCMD; 
			return true;
		}
	break;


	/*case ML_SCROLL:
		menuListScrY--;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}

		menuListShow=true;
	break;*/



	case ML_SCREEN:
		if (movY != 0)
		{
			int newPos = menuListTabIndexPos + (movY * 2);

			if (newPos >= 0 && newPos < menuListTabIndexSize)
			{
				menuListTabIndexPos = newPos;
				menuListShow=true;
			}
			//#ifndef DOJA
			//if (newPos+2 >= menuListTabIndexSize)
			//	listenerInit(SOFTKEY_BACK, SOFTKEY_BACK);
			//else
            //#endif					
		}
		else if (fire)
		{
			//menuListTabIndexPos += 2;

			//if (menuListTabIndexPos >= menuListTabIndexSize) {menuListCMD=-2; return true;}
			
			if (menuListTabIndexPos + 2 < menuListTabIndexSize) menuListTabIndexPos += 2;
			
			menuListShow=true;
		}
		

		if(menuListShow == true)
		{
			if (menuListTabIndexPos+2 >= menuListTabIndexSize)
				listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
			else
				listenerInit(SOFTKEY_BACK, SOFTKEY_CONTINUE);
		}
		
		
		if(back) {menuListCMD=-2; return true;}
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
	
	if (menuImg!=null) {canvasFillDraw(0); showImage(menuImg);}
	//#ifdef DOUBLEDRAW
	//if (menuImg!=null) {canvasFillDraw(0); showImage(menuImg);}
	//#endif
	drawMenuBackground();

	

//#ifdef J2ME
	g.setClip(menuListX, menuListY, menuListSizeX, menuListSizeY);
//#endif

	switch (menuListMode)
	{
	case ML_OPTION:

		//String[] texto = textBreak(menuListStr[menuListPos][menuListDat[menuListPos][2]], menuListSizeX, menuListGetFont(0) );		

		int height = (menuListDat[menuListPos][4]), y = menuListY + (( menuListSizeY / 6) * 5), x, width, wholeHeight,
			boxHeight;
			
    	//int y = menuListY + (( menuListSizeY / 4) * 3) - (height/2);
		//if (y + height > menuListSizeY) {y = menuListY + (menuListSizeY-height);}
				
		//#ifdef AVOIDSMALLFONT
		//Font font = menuListFont;
		//#else
		//Font font = menuListFont;
		//#endif
		boolean downArrow = false, upArrow = false;

		g.setFont(menuListFont);	
		
		//#ifdef SAMSUNGSTRINGDRAW
		//#endif 
		
		//#ifdef J2ME
		scr.setClip(menuListX,menuListY,menuListSizeX,menuListSizeY);			
		//#endif
				
		//#ifdef FONTHEIGHTPATCH
		//#else
		height = menuListFontHeight;
		//#endif
		
		boxHeight = height+UI_TEXTBOX_VMARGIN*2;
		
		wholeHeight = (menuListDat.length * boxHeight) + ((menuListDat.length - 1)*UI_TEXTBOX_GAP);
		
		
		// Need scroll
		if(wholeHeight > menuListSizeY)
		{
			y = menuListY  - (boxHeight)/2 + menuListSizeY/2 - (menuListPos*(boxHeight + UI_TEXTBOX_GAP));
									
			while(y > menuListY + boxHeight + UI_TEXTBOX_GAP) y -= boxHeight + UI_TEXTBOX_GAP;			
			while(y + wholeHeight < menuListY + menuListSizeY - boxHeight - UI_TEXTBOX_GAP) y += boxHeight + UI_TEXTBOX_GAP;
			
		// It fits, only vertically center		
		}else
			y = menuListY + (menuListSizeY - wholeHeight)/2;
		
				
		//menuListTextDraw(g, menuListDat[i][0], texto[i], 0,y);
				
		
		for(int i = 0; i < menuListStr.length; i++)
		{
		
			if(y >= menuListY &&  y + boxHeight <= menuListY + menuListSizeY)
			{				
				String texto = menuListStr[i][menuListDat[i][2]];
			
				width = menuListFont.stringWidth(texto);
									
				//x = menuListX + (( menuListSizeX - width ) / 2);
				
				//#ifdef J2ME
				scr.setClip(menuListX,menuListY,menuListSizeX,menuListSizeY);		
				//#endif
								
				drawBox(menuListX, y, menuListSizeX, boxHeight,(i == menuListPos));
								
				if(i > 0 && y < menuListY + boxHeight + UI_TEXTBOX_GAP)
					drawArrow(menuListX + menuListSizeX/2, y - UI_ARROW_SIZE - UI_VARROW_MARGIN, 2); 		
					
				if(i < menuListStr.length - 1 && y + boxHeight + UI_TEXTBOX_GAP + boxHeight > menuListY + menuListSizeY)
					drawArrow(menuListX + menuListSizeX/2, y + boxHeight + UI_ARROW_SIZE + UI_VARROW_MARGIN - 1, 3); 							
				
				if(i == menuListPos && menuListStr[i].length > 1)
				{				
					drawArrow(menuListX + UI_HARROW_MARGIN, y + boxHeight/2 - 1, 1); 
					drawArrow(menuListX + menuListSizeX - UI_HARROW_MARGIN, y + boxHeight/2 - 1, 0); 
				}
			
				putColor(UI_FONT_RGB);
				
																		
				//#ifdef J2ME										
				
				if(menuListFont.stringWidth(texto) > menuListSizeX - UI_TEXTBOX_HMARGIN*2 + 6) 
					g.drawString("...", menuListX + menuListSizeX - UI_TEXTBOX_HMARGIN, y+UI_TEXTBOX_VMARGIN, 20);		
				
				scr.setClip(menuListX + UI_TEXTBOX_HMARGIN, menuListY , menuListSizeX - UI_TEXTBOX_HMARGIN*2, menuListSizeY);				
				g.drawString(texto, menuListX + UI_TEXTBOX_HMARGIN, y+UI_TEXTBOX_VMARGIN, 20);		
				
				//#elifdef DOJA
				//#ifndef LARGEUI
				//#endif

				
				//#endif
			}					
			y += boxHeight + UI_TEXTBOX_GAP;
		}
						
	break;


	/*case ML_SCROLL:

		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			menuListTextDraw(g, menuListDat[i][0], menuListStr[i][0], 0,y);

			y += menuListDat[i][4];
		}
	break;*/


	case ML_SCREEN:

		y = 0;

		int ini =  menuListTabIndex[menuListTabIndexPos];
		int size = menuListTabIndex[menuListTabIndexPos+1];

		for (int i=0 ; i<size ; i++)
		{
			y += menuListDat[ini+i][4];
		}

		y = menuListY + (menuListSizeY - y) / 2;

		for (int i=0 ; i<size ; i++)
		{
			menuListTextDraw(g, menuListDat[i][0], menuListStr[ini+i][0], 0, y);

			y += menuListDat[i][4];
		}
		
		// Scroll arrows
		
		int barSize = menuListSizeY - 6*UI_ARROW_SIZE,
			bx=canvasWidth - UI_MENU_MARGIN - 2, by=menuListY + 3*UI_ARROW_SIZE;
		
		if(menuListTabIndexPos > 0)
			drawArrow(bx + 2, menuListY + UI_ARROW_SIZE,2);
			
		if(menuListTabIndexPos + 2 < menuListTabIndexSize)			
			drawArrow(bx + 2, menuListY + menuListSizeY - UI_ARROW_SIZE,3);
		
		// Scroll bar
		
		//#ifndef NOSCROLLBAR
		putColor(UI_FONT_RGB);
		scr.drawRect(bx, by, 4, barSize);			
		scr.fillRect(bx, by + 1 + (barSize*menuListTabIndexPos/menuListTabIndexSize), 4, (barSize*2/menuListTabIndexSize));
		//#endif
		
	break;
	}
}






public void menuListTextDraw(Graphics g, int format, String str, int x, int y)
{
	//#ifdef AVOIDSMALLFONT
	//#endif
	
	//Font font = menuListFont;

	g.setFont(menuListFont);
	
	//#ifdef SAMSUNGSTRINGDRAW
	//#endif 

	x = menuListX + (( menuListSizeX - menuListFont.stringWidth(str) ) / 2);

	putColor(UI_FONT_RGB);

//#ifdef J2ME

	/*putColor(UI_FONTOUTLINE_RGB);
	g.drawString(str, x-1,y-1, 20);
	g.drawString(str, x+1,y-1, 20);
	g.drawString(str, x-1,y+1, 20);
	g.drawString(str, x+1,y+1, 20);*/

	
	g.drawString(str, x,y, 20);

//#elifdef DOJA
//#endif

}

public static Font menuListFont; 
public static int menuListFontHeight;

/*public Font menuListGetFont()
{
//#ifdef J2ME
	return Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN ,
	//#ifdef AVOIDSMALLFONT
	Font.SIZE_MEDIUM
	//#else
	Font.SIZE_SMALL
	//#endif
	);
//#elifdef DOJA
	return Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_PLAIN | Font.SIZE_SMALL);
//#endif
}*/


// <=- <=- <=- <=- <=-











// *******************
// -------------------
// wait - Engine
// ===================
// *******************
/*
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
*/















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

static int RND_Cont = (int)(System.currentTimeMillis()>>8 & 0x0F);
static int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

public static int rnd(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
}


// -------------------
// colision
// ===================
/*
public boolean colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 >= x1+xs1 ) || ( x2+xs2 <= x1 ) || ( y2 >= y1+ys1 ) || ( y2+ys2 <= y1 ));
}*/

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

/*public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor, int Trozos)
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
      if (SizeX < 0) {SizeX*=-1; Flip|=1;}
      if (SizeY < 0) {SizeY*=-1; Flip|=2;}
      int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
      int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}
    
      ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY,  Flip);
   }


   for (int i=0 ; i<Trozos ; i++)
   {
    
            frame*=(Trozos*6);
        
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
        	if (destX < 0) {sourX+=-destX; sizeX+=destX; destX=0;}
        	if (destY < 0) {sourY+=-destY; sizeY+=destY; destY=0;}
        //#endif
        
        //#ifdef NOKIAUI
         imageDraw(img, sourX, sourY, sizeX, sizeY, destX, destY, flip);
        //#else
         scr.setClip (destX, destY, sizeX, sizeY);
         scr.clipRect(destX, destY, sizeX, sizeY);
         scr.drawImage(img, destX-sourX, destY-sourY, Graphics.TOP|Graphics.LEFT);
        //#endif
    }
    //#ifdef SCRFIX
    ga.ScrollUpdate(destX, destY, sizeX, sizeY);
    //#endif

}
*/

// ----------------------------------------------------------
//#elifdef DOJA
// ----------------------------------------------------------
// ----------------------------------------------------------
//#endif

// <=- <=- <=- <=- <=-




//#ifdef NOKIAUI
public void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
 /*if ((DestX       >= canvasWidth)
 || (DestX+SizeX < 0)
 || (DestY       >= canvasHeight)
 || (DestY+SizeY < 0))
 {return;}*/
 
 scr.setClip (0, 0, canvasWidth, canvasHeight);
 scr.clipRect(DestX, DestY, SizeX, SizeY);
 
 switch (Flip)
 {
 case 0:
 scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
 return;
 
 case 1:
 dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);  // Flip X
 return;
 
 case 2:
 dscr.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000); // Flip Y
 return;
 
 case 3:
 dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180); // Flip XY
 return;
 }
}
//#endif













// *********************
// ---------------------
// inOut - Engine
// =====================
// *********************

//-------------------
//load File
//===================

public byte[] loadFile(String Nombre)
{
	/*//#ifdef LOAD_FILE_CACHING
	//#endif
	
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
	
	//is.reset();
	
	Size = is.read(buffer, 0, buffer.length);
	
	while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}
	
	is.close();
	}
	catch(Exception exception)
	{
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	com.mygdx.mongojocs.sanfermines2006.Debug.println("loadFile: "+Nombre+" <File not found>");
	//#endif
	return null;
	}
	
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	com.mygdx.mongojocs.sanfermines2006.Debug.println("loadFile: "+Nombre+" <"+buffer.length+">");
	//#endif
	
	System.gc();
	
	//#ifdef LOAD_FILE_CACHING
	//#endif
	
	return buffer;*/
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
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		Debug.println("loadImage: "+FileName+" <File not found>");
		return null;
	//#endif
	}

//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("loadImage: "+FileName);
//#endif

	System.gc();
	return Img;
}

// ---------------------------------------------------------
//#elifdef DOJA
// ---------------------------------------------------------
// ---------------------------------------------------------


// ---------------------------------------------------------
//#endif

// <=- <=- <=- <=- <=-

    

// *********************
// ---------------------
// Menu graphic UI 
// =====================
// *********************

public static int UI_BACKGROUND_RGB = 0x000000;
public static int UI_FONT_RGB = 0xfff799;
public static int UI_FONTOUTLINE_RGB = 0xf26522;
public static int UI_DARK_RGB = 0x182446;
public static int UI_LIGHT_RGB = 0x20448b;
public static int UI_FRAME_RGB = 0xfff799;
public static int UI_TOPBORDER_RGB = 0x3963bd;
public static int UI_BOTBORDER_RGB = 0x182446;
public static int UI_TABLETITLE_RGB = 0x6dcff5;
public static int UI_TABLEFONT_RGB = 0xffffff;
public static int UI_PICTUREWINDOW_RGB = 0x832026;


//#ifdef LARGEUI

public static final int UI_MENU_FRAMEMARGIN = 8;
public static final int UI_MENU_FRAMEWIDTH = 2;
public static final int UI_MENU_FRAMEWINDOWGAP = 2;
public static final int UI_MENU_MARGIN = UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + UI_MENU_FRAMEWINDOWGAP + 5;
public static final int UI_MENU_MARGIN2 = UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + UI_MENU_FRAMEWINDOWGAP + 5;
public static final int UI_LISTENER_MARGIN = UI_MENU_MARGIN2;
public static final int UI_MENU_TITLEX = UI_MENU_MARGIN2 + 10;
public static final int UI_MENU_TITLEY = UI_MENU_MARGIN2 + 6;
public static final int UI_TEXTBOX_HMARGIN = 20;
public static final int UI_TEXTBOX_VMARGIN = 4;
public static final int UI_TEXTBOX_GAP = 4;
public static final int UI_HARROW_MARGIN = 8;
public static final int UI_VARROW_MARGIN = 3;
public static final int UI_ARROW_SIZE = 3;
public static final int UI_TABLE_MARGIN = 2;
public static final int UI_MINIFIELD_WIDTH = 37;
public static final int UI_MINIFIELD_HEIGHT = 49;

//#elifdef MEDIUMUI
//#elifdef SMALLUI
//#endif


void drawStandardBackGround(String title, int windowY, int windowYLimit, boolean bars, boolean logo)
{
	int x = 0, y = 0, sx = canvasWidth, sy = canvasHeight;
		
	//#ifdef J2ME		
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif
	putColor(UI_BACKGROUND_RGB);
	scr.fillRect(x,y,sx,sy);
	
	x += UI_MENU_FRAMEMARGIN; y += UI_MENU_FRAMEMARGIN; sx -= UI_MENU_FRAMEMARGIN*2; sy -= UI_MENU_FRAMEMARGIN*2;
	
	putColor(UI_FRAME_RGB);
	scr.fillRect(x,y,sx,sy);
	
	x += UI_MENU_FRAMEWIDTH; y += UI_MENU_FRAMEWIDTH; sx -= UI_MENU_FRAMEWIDTH*2; sy -= UI_MENU_FRAMEWIDTH*2;
	
	putColor(UI_DARK_RGB);
	scr.fillRect(x,y,sx,sy);
			
	
	if(title != null)
	{
			
		//#ifdef LARGEUI
		canvasTextCreate(0,UI_MENU_MARGIN,canvasWidth,windowY - (bars ? 8 : 0) - UI_MENU_MARGIN);
		textDrawNoCut(title,UI_MENU_TITLEX,0,UI_FONT_RGB,TEXT_OUTLINE | TEXT_BOLD | TEXT_VCENTER | TEXT_LEFT);
		//#else
		//#endif
		
		canvasTextCreate(0,0,canvasWidth,canvasHeight);
			
	}
		
	listenerDraw();
	
	x += UI_MENU_FRAMEWINDOWGAP; y = windowY; sx -= UI_MENU_FRAMEWINDOWGAP*2; sy = windowYLimit - windowY;
	
	putColor(UI_LIGHT_RGB);
	scr.fillRect(x,y,sx,sy);
	
	//#ifndef NOBACKGROUNDBARS
	if(bars)
	{
		//#ifdef LARGEUI
		scr.fillRect(x,y - 8,sx,6);
		scr.fillRect(x,y + sy + 2,sx,6);
		//#else
		//#endif
	}
	//#endif
	
	//#ifdef LARGEUI
		if(logo)
		//#ifdef CENTERBKGLOGO
		//#else
		showTeamLogo(canvasWidth - UI_MENU_MARGIN - 14, UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEMARGIN + 9,(isDomestic ? domHeadLineTeam : headLineTeam));
		//#endif
	//#endif
	
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif

}


public void drawBox(int x, int y, int sx, int sy, boolean hilight)
{
	//#ifndef SMALLUI	
	if(hilight)
	{		
		putColor(UI_LIGHT_RGB);
		scr.fillRect(x,y,sx,sy);
		
		putColor(UI_BOTBORDER_RGB);		
		
		scr.drawLine(x,y+sy-1,x+sx-1,y+sy-1);
		//#ifdef LARGEUI
		scr.drawLine(x,y,x,y+sy-1);		
		//#endif
				
		putColor(UI_TOPBORDER_RGB);
		
		scr.drawLine(x,y,x+sx-1,y);		
		//#ifdef LARGEUI	
		scr.drawLine(x+sx-1,y,x+sx-1,y+sy-1);
		//#endif
		
	}else{		
	//#endif
				
		putColor(UI_DARK_RGB);
		scr.fillRect(x,y,sx,sy);
		
		//#ifdef SMALLUI
		//#endif
		
	//#ifndef SMALLUI	
	}
	//#endif
			
}

public void drawArrow(int x, int y, int dir)
{
	putColor(UI_FONT_RGB);
	
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif
	
	if(x < UI_ARROW_SIZE) x = UI_ARROW_SIZE;
	if(x > canvasWidth - UI_ARROW_SIZE) x = canvasWidth - UI_ARROW_SIZE;
	if(y < UI_ARROW_SIZE) y = UI_ARROW_SIZE;
	if(y > canvasHeight - UI_ARROW_SIZE) y = canvasHeight - UI_ARROW_SIZE;
	
	switch(dir)
	{
		case 0 : 
		for(int i = 1; i <= UI_ARROW_SIZE; i++)
		scr.drawLine(x - i, y - i, x - i, y + i);
		
		//#ifndef DOJA
		putColor(UI_BOTBORDER_RGB);
		scr.drawLine(x,y,x-UI_ARROW_SIZE,y-UI_ARROW_SIZE);
		//#endif				
		
		putColor(UI_TOPBORDER_RGB);
		scr.drawLine(x,y+1,x-UI_ARROW_SIZE,y+UI_ARROW_SIZE+1);
		break;
		
		case 1 : 
		for(int i = 1; i <= UI_ARROW_SIZE; i++)
		scr.drawLine(x + i, y - i, x + i, y + i);
		
		//#ifndef DOJA
		putColor(UI_BOTBORDER_RGB);		
		scr.drawLine(x,y,x+UI_ARROW_SIZE,y-UI_ARROW_SIZE);
		//#endif
		
		putColor(UI_TOPBORDER_RGB);						
		scr.drawLine(x,y+1,x+UI_ARROW_SIZE,y+UI_ARROW_SIZE+1);
		break;
		
		case 2 :
		for(int i = 0; i <= UI_ARROW_SIZE; i++)
			scr.drawLine(x - i, y + i, x + i, y + i);
		break;
		
		case 3 :
		for(int i = 0; i <= UI_ARROW_SIZE; i++)
			scr.drawLine(x - i, y - i, x + i, y - i);
		break;
	}
				
}

void drawCell(int x, int y, int sx, int sy, String s, int rgb)
{
	//#ifdef J2ME
	scr.setClip(x,y,sx,sy);
	//#endif
	
	putColor(UI_BACKGROUND_RGB);	
	scr.fillRect(x,y,sx,sy);
	
	//#ifdef J2ME
	scr.setClip(x,y,sx+2,sy+2);
	//#endif
	
	putColor(UI_FRAME_RGB);	
	scr.drawRect(x,y,sx,sy);
		
	putColor(rgb);	
	
	//#ifdef J2ME
	scr.setClip(x+UI_TABLE_MARGIN,y+UI_TABLE_MARGIN,sx-UI_TABLE_MARGIN*2,sy-UI_TABLE_MARGIN*2);
	scr.drawString(s,x+UI_TABLE_MARGIN,y+UI_TABLE_MARGIN+1,20);
	//#elifdef DOJA
	//#endif
}

void drawClassificationTable(int x, int y, int sx, int sy)
{
	
	int fontHeight, cellWidth, cellHeight, nameWidth = 60, xOffset = 0, yOffset = 0, column, row;
		
	//#ifdef AVOIDSMALLFONT
	//Font font = menuListFont;
	//#else
	//Font font = menuListFont;
	//#endif
	scr.setFont(menuListFont);
		
	//#ifdef TABLECELLSLARGEWIDTH
	//#else
		//#ifdef SMALLUI
		//#else
		cellWidth = menuListFont.stringWidth("100");		
		//#endif
	//#endif
	
		
	//#ifdef FONTHEIGHTPATCH
	//#else
	cellHeight = menuListFontHeight + UI_TABLE_MARGIN*2;
	//#endif
	
	x += ((sx-nameWidth)%cellWidth)/2;
	y += (sy%cellHeight)/2; 
					
	drawCell(x + xOffset,y + yOffset,cellWidth,cellHeight,"",UI_TABLETITLE_RGB);
	xOffset += cellWidth;
		
	drawCell(x + xOffset,y + yOffset,nameWidth,cellHeight,gameText[17][0],UI_TABLETITLE_RGB);
	xOffset += nameWidth;		
	
	row = tableStartRow - 1;
					
	while(xOffset + cellWidth < sx && row < 6){
				
		drawCell(x + xOffset,y + yOffset,cellWidth,cellHeight,gameText[16][row],UI_TABLETITLE_RGB);
		xOffset += cellWidth;
		row++;
	}
	
	if(tableStartRow > 1) drawArrow(x + cellWidth + nameWidth, y - 2*UI_ARROW_SIZE, 1);
	
	if(tableStartRow < 6) drawArrow(x + xOffset, y - 2*UI_ARROW_SIZE, 0);
	
	column = tableStartColumn;
	
	yOffset += cellHeight;
	
							
	while(yOffset + cellHeight < sy && column < teamsNumber){
		
		xOffset = 0;
				
		drawCell(x + xOffset,y + yOffset,cellWidth,cellHeight,(column+1)+".",(leagueRank[column][0] == playerTeam ? UI_FONT_RGB : UI_TABLEFONT_RGB));		
		xOffset += cellWidth;
		
		//System.out.println ("Equipo "+leagueRank[column][0]);
		
		drawCell(x + xOffset,y + yOffset,nameWidth,cellHeight,teamNames[leagueRank[column][0]],(leagueRank[column][0] == playerTeam ? UI_FONT_RGB : UI_TABLEFONT_RGB));
		xOffset += nameWidth;		
		
		row = tableStartRow;
			
		while(xOffset + cellWidth < sx && row < 7){
				
			drawCell(x + xOffset,y + yOffset,cellWidth,cellHeight,""+leagueRank[column][row],(leagueRank[column][0] == playerTeam ? UI_FONT_RGB : UI_TABLEFONT_RGB));
			xOffset += cellWidth;
			row++;
		}
		
		putColor(UI_LIGHT_RGB);
		scr.fillRect(x + xOffset + 1,y + yOffset,canvasWidth - x - xOffset - 1 - (UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + UI_MENU_FRAMEWINDOWGAP),cellHeight);
		
		yOffset += cellHeight;
		column++;
			
	}
	
	if(tableStartColumn > 0) drawArrow(x - 2*UI_ARROW_SIZE, y + cellHeight, 2);		
	
	if(tableStartColumn < teamsNumber - 1) drawArrow(x - 2*UI_ARROW_SIZE, y + yOffset, 3);
		
}

void drawFormationScreen(boolean changesLimit)
{
	
	//#ifdef AVOIDSMALLFONT
	//#else
	Font font = menuListFont;
	//#endif
	int fontHeight =
		//#ifdef FONTHEIGHTPATCH
		//#else
		font.getHeight()
		//#endif
		, x, y, topy, boty, linesThatFit, index = 0, textBoxWidth, margin;
			
	scr.setFont(font);

	margin = UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + UI_MENU_FRAMEWINDOWGAP;
	
	// Draw the minifield
	
	x = margin + (((canvasWidth - margin*2)/2) - UI_MINIFIELD_WIDTH)/2;	
	textBoxWidth = (canvasWidth - margin*2) /2 - 10;
	
	//#ifdef LARGEUI
		//#ifndef NOPLAYERDATAPREVIEW
		//#else
		y = UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + (4*canvasHeight/5 - UI_MINIFIELD_HEIGHT - fontHeight - 16)/2;
		//#endif
	//#else
	//#endif
		
	//#ifndef NOMINIFIELD
	putColor(0x00bb00);
	scr.fillRect(x, y,UI_MINIFIELD_WIDTH,UI_MINIFIELD_HEIGHT);
	
	putColor(0xffffff);
	scr.drawRect(x, y,UI_MINIFIELD_WIDTH,UI_MINIFIELD_HEIGHT);
	
	scr.drawLine(x, y + UI_MINIFIELD_HEIGHT/2, x + UI_MINIFIELD_WIDTH,y + UI_MINIFIELD_HEIGHT/2);	
	
	//#ifdef J2ME
	scr.drawArc(x + UI_MINIFIELD_WIDTH/2 - 4, y + UI_MINIFIELD_HEIGHT/2 - 4, 8,8,0,360);
	//#endif
		
	// Color spots for player in minifield
	
	putColor(0 == subOption ? 0xffff00 : 0xff0000);
	
	scr.fillRect(x + (UI_MINIFIELD_WIDTH/2),
				 y + (6*UI_MINIFIELD_HEIGHT/7),
				 2,2);
	
	for(int i = 0; i < 10; i++)
	{
		
		putColor(i == subOption - 1 ? 0xffff00 : 0xff0000);
		
		scr.fillRect(x + (UI_MINIFIELD_WIDTH * (7 - formation[myFormation][i][1]))/8,
					 y + (UI_MINIFIELD_HEIGHT * (5 - formation[myFormation][i][0]))/7,
					 2,2);
	}
	//#endif
	
	// Formation info text
			
	y += UI_MINIFIELD_HEIGHT;
	
	//#ifdef LARGEUI
	y += 4;
	//#elifdef MEDIUMUI
	//#elifdef SMALLUI
	//#endif
	
	x = margin + (((canvasWidth - margin*2)/2) - textBoxWidth)/2;
	
	drawBox(x,y,textBoxWidth,fontHeight+8,true);
	
	//#ifndef SMALLUI	
	drawArrow(x + 4,y+fontHeight/2 + 2,1);
	drawArrow(x +textBoxWidth - 4,y+fontHeight/2 + 2,0);
	//#endif
	
	//#ifdef SAMSUNGSTRINGDRAW
	//#endif 
		
	putColor(UI_FONT_RGB);
	//#ifdef J2ME
	scr.drawString(gameText[14][myFormation],x + (textBoxWidth - font.stringWidth(gameText[14][myFormation]))/2,y + 4,20);
	//#elifdef DOJA
	//#endif
	
	//#ifdef LARGEUI	
	//#ifndef NOPLAYERDATAPREVIEW
	
	// Stats bars
	/*
	int barRGB[] = {0xfa2714,0xff5221,0xfda94a,0xfeed65,0xd6ff5a,0xa9ff4b,0x67ff3d,0x03ff00,0x03ff00,0x03ff00};
	
	y += fontHeight + 12;
	
	x = UI_MENU_MARGIN + 4;
	
	boty = 2 + (fontHeight+1)*5;
	
	putColor(UI_DARK_RGB);
	scr.fillRect(x,y,textBoxWidth - 8,boty);
	
	y++;

	for(int i = 0; i < 5; i++)	
	{		
		putColor(UI_FONT_RGB);
		
		//#ifdef J2ME
		scr.drawString(gameText[35][i],x+1,y,20);
		scr.drawString(""+AplayerData[myAlignment[subOption]][i+2],x + textBoxWidth - 8 - font.stringWidth("0"),y,20);
		//#elifdef DOJA
		scr.drawString(gameText[35][i],x+1,y+font.getAscent());
		scr.drawString(nullString+AplayerData[myAlignment[subOption]][i+2],x + textBoxWidth - 8 - font.stringWidth("0"),y+font.getAscent());
		//#endif
		
		int bx, ex, step;
		
		bx = x + 2 + font.stringWidth("AA");
		ex = x + textBoxWidth - 8 - 2 - font.stringWidth("0"); 
		step = (ex-bx)/10;
		
		putColor(UI_LIGHT_RGB);
		
		// Color squares
		
		for(int j = 0; j <10; j ++)
		{
			putColor((AplayerData[myAlignment[subOption]][i+2] > j ? barRGB[j] : UI_LIGHT_RGB));
			
			scr.fillRect(bx + step*j,y+2,step-1,fontHeight-4);
		}
		
		y += fontHeight + 1;
	}*/
	
	//#endif			
	//#endif
			
	// Player list			
			
	//#ifdef LARGEUI			
	topy = y = UI_MENU_MARGIN + 4;
	boty = 4*canvasHeight/5 - 12;
	//#elifdef MEDIUMUI
	//#elifdef SMALLUI
	//#endif
	
	x = canvasWidth/2;
	y = topy;
	
	linesThatFit = (boty-topy)/(fontHeight+2);	
	if(subOptionScroll > subOption ) subOptionScroll = subOption;
	if(subOptionScroll + linesThatFit < subOption) subOptionScroll = subOption - linesThatFit;
	
	if(subOptionScroll > 0) drawArrow(x + textBoxWidth/2,y - 2*UI_ARROW_SIZE,2);
				
	while(y < boty)
	{
		putColor(index + subOptionScroll < 11 ? 0x008000 : UI_DARK_RGB);
		//#ifdef J2ME
		scr.setClip(x,y,textBoxWidth,fontHeight+2);
		//#endif
		scr.fillRect(x,y,textBoxWidth,fontHeight + 2);
		
		
		if(changesLimit){
			
			if(index + subOptionScroll == subOption) putColor(UI_FONTOUTLINE_RGB);
			else if(index + subOptionScroll == chosenPlayer) putColor(0xff8000);
			else if(changesList[myAlignment[index + subOptionScroll]]) putColor(UI_BACKGROUND_RGB);
			else putColor(UI_TABLEFONT_RGB);
						
		}
		else
			putColor(index + subOptionScroll == subOption ? UI_FONTOUTLINE_RGB : 
					(index + subOptionScroll == chosenPlayer ? 0xff8000 : UI_TABLEFONT_RGB));		

		//#ifdef J2ME					
		scr.setClip(x+1,y+1,textBoxWidth - 2,fontHeight);
		scr.drawString( 
						//#ifdef LARGEUI
						(index+subOptionScroll+1)+". "+
						//#endif
						AplayerNames[myAlignment[index + subOptionScroll]],x+1,y+1,20);		
		//#elifdef DOJA
		//#endif
		
		y += fontHeight + 2;
		index++;
	}	
	
	//#ifdef DOJA
	//#endif
		
	if(subOptionScroll < AplayerNames.length - linesThatFit - 1) drawArrow(x + textBoxWidth/2,y + 2*UI_ARROW_SIZE,3);
}

void showTeamIcon(int x, int y, int id)
{
	id = teamLogoIds[id];
	//#ifdef J2ME
	showImage(imgFlags, 12*(id%4), 12*(id/4), 12, 12, x - 6, y - 6);
	//#elifdef DOJA
	//#endif
}

//#ifdef LARGEUI
void showTeamLogo(int x, int y, int id)
{	
	id = teamLogoIds[id];
	
	//#ifdef J2ME
	showImage(bigFlagsImg,(id%4)*32,(id/4)*32,32,32,x - 16, y - 16);
	//#elifdef DOJA
	//#endif
}
//#endif

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

int printerX;// = 0;
int printerY;// = 0;
int printerSizeX;// = getWidth();
int printerSizeY;// = getHeight();

// -------------------
// canvasText Create
// ===================

/*public void canvasTextCreate()
{
	printerX = 0;
	printerY = 0;
	printerSizeX = canvasWidth;
	printerSizeY = canvasHeight;
}*/

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
/*
public void canvasTextDraw()
{
	textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
}*/

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
	//#ifdef AVOIDSMALLFONT
	//#else
	textDraw(textBreak(Str, printerSizeX, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
	//#endif
//#elifdef DOJA
//#endif
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
//#ifdef J2ME
	//#ifdef AVOIDSMALLFONT
	//#else
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
	//#endif
//#elifdef DOJA
//#endif
	scr.setFont(f);
	
	//#ifdef SAMSUNGSTRINGDRAW
	//#endif 

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
		putColor(UI_FONTOUTLINE_RGB);
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


public void textDrawNoCut(String Str, int X, int Y, int RGB, int Mode)
{
//#ifdef J2ME
	//#ifdef AVOIDSMALLFONT
	//#else
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
	//#endif
//#elifdef DOJA
//#endif
	scr.setFont(f);
	
	//#ifdef SAMSUNGSTRINGDRAW
	scr.drawString("", 0, -100, 20);
	//#endif 

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  printerSizeY-(f.getHeight()) ;}

	X += printerX;
	Y += printerY;

	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX +=( printerSizeX-f.stringWidth(Str) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX += printerSizeX-f.stringWidth(Str) ;}


	int despY = 0;//f.getHeight();

    //#ifdef J2ME
    scr.setClip(0, 0, canvasWidth, canvasHeight);
    //#endif
    
    
	if ((Mode & TEXT_OUTLINE)!=0)
	{
		putColor(UI_FONTOUTLINE_RGB);
//#ifdef J2ME
    	scr.drawString(Str, X-1+despX, Y-1+despY , 20);
		scr.drawString(Str, X+1+despX, Y-1+despY , 20);
		scr.drawString(Str, X-1+despX, Y+1+despY , 20);
		scr.drawString(Str, X+1+despX, Y+1+despY , 20);
//#elifdef DOJA
//#endif
	}

	putColor(RGB);
//#ifdef J2ME
	scr.drawString(Str,  X+despX, Y+despY , 20);
//#elifdef DOJA
//#endif
	

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

boolean canvasShow = false;


// -------------------
// canvas Tick
// ===================

/*public void canvasDraw()
{
	if (canvasFillShow) { canvasFillShow=false; canvasFillDraw(canvasFillRGB); }

	if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; 
	//canvasTextDraw(); 
    textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
	}


	if (biosStatus == BIOS_MENU && gameStatus == GAME_PLAY_TICK && menuListShow) {playShow = true;}
	if (playShow) { playShow=false; playDraw(); }


	menuListDraw(scr);

//	popupDraw();

	//listenerDraw();
}*/



// -------------------
// canvas Fill Task
// ===================
/*
public void canvasFillTask(int RGB)
{
	canvasFillRGB = RGB;
	canvasFillShow = true;
}*/

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
					//VolumeControl vc = (VolumeControl) soundTracks[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
					soundTracks[Ary].play();
				}
				catch(Exception e)
				{
				//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
				//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
				//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
static final int SOFTKEY_CHANGE = 6;
static final int SOFTKEY_FORMATION_CHANGE = 7;
static final int SOFTKEY_ATTRIB_CHANGE = 8;

int listenerIdLeft;
int listenerIdRight;

//#ifdef J2ME
//#ifdef FULLSCREEN
 
int listenerHeight = 8;

//Image listenerImg;
byte[] listenerCor;

public void listenerInit(int idLeft, int idRight)
{
	listenerIdLeft = idLeft;
	listenerIdRight = idRight;
}

public void listenerInit(String strLeft, String strRight) {}

public void listenerDraw()
{
	/*if (listenerImg != null)
	{
		int listenerY = canvasHeight;

		putColor(0);
		scr.setClip(0, listenerY, canvasWidth, listenerHeight);
		scr.fillRect(0, listenerY, canvasWidth, listenerHeight);

		listenerLabelDraw(listenerImg,  1, listenerY+1,  listenerIdLeft, listenerCor);
		listenerLabelDraw(listenerImg,  canvasWidth - listenerCor[(listenerIdRight*6)+2]-1, listenerY+1,  listenerIdRight, listenerCor);
	}*/
			
	textDrawNoCut(gameText[15][listenerIdLeft],UI_LISTENER_MARGIN,-UI_LISTENER_MARGIN,UI_FONT_RGB,TEXT_BOTTON | TEXT_LEFT);
	
	String right = gameText[15][listenerIdRight];
	
	switch(listenerIdRight)
	{		
		case SOFTKEY_FORMATION_CHANGE : right = right + "("+changesLeft+")"; break;
		case SOFTKEY_ATTRIB_CHANGE : right = right + "("+(MAXIMUMPOINTS-1-attribSum)+")"; break;
	}	
	textDrawNoCut(right,-UI_LISTENER_MARGIN,-UI_LISTENER_MARGIN,UI_FONT_RGB,TEXT_BOTTON | TEXT_RIGHT);		
	
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
/*
public int[] convByte2Int(byte[] in)
{
	int[] out = new int[in.length / 4];
	
	for (int pos=0, i=0 ; i<out.length ; i++)
	{
		out[i] = ((in[pos++]&0xFF)<<24) | ((in[pos++]&0xFF)<<16) | ((in[pos++]&0xFF)<<8) | (in[pos++]&0xFF);
	}

	return out;
}*/

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
/*
public void ProgBarINI(int SizeX, int SizeY, int DestX, int DestY)
{
	ProgBarX = DestX;
	ProgBarY = DestY;
	ProgBarSizeX = SizeX;
	ProgBarSizeY = SizeY;
}*/

// -------------------
// ProgBar END
// ===================
/*
public void ProgBarEND()
{
	ProgBarSET(1,1);
	ProgBar_ON = false;
}*/

// -------------------
// ProgBar SET
// ===================
/*
public void ProgBarSET(int Pos)
{
	ProgBarSET(Pos, ProgBarTotal);
}*/

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
/*
public void ProgBarADD()
{
	//ProgBarSET(++ProgBarPos);
    ProgBarSET(++ProgBarPos, ProgBarTotal);
}*/

// -------------------
// ProgBar IMP
// ===================

public void ProgBarIMP()
{
	if (ProgBar_ON)
	{
		
	/*tColor(0x188CCE);	// Borde
	scr.fillRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+6, ProgBarSizeY+6);
	putColor(0xDEE7F7); // Papel
	scr.drawRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+5, ProgBarSizeY+5);
	putColor(0x00005A);	// Lapiz
	scr.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);*/
	
	progressPercent = (100*ProgBarPos)/ProgBarTotal;
		
	
	drawStandardBackGround(null,canvasHeight/4,3*canvasHeight/4,false,false);
	drawBox(UI_MENU_MARGIN,5*canvasHeight/12,canvasWidth - UI_MENU_MARGIN*2,canvasHeight/6,true);
	drawBox(UI_MENU_MARGIN*2,7*canvasHeight/16,canvasWidth - UI_MENU_MARGIN*4,canvasHeight/8,false);
	if(progressPercent > 0)
		drawBox(UI_MENU_MARGIN*2,7*canvasHeight/16,(progressPercent < 100 ? progressPercent : 100)*(canvasWidth - UI_MENU_MARGIN*4)/100,canvasHeight/8,true);
		
	}
	
}

// <=- <=- <=- <=- <=-
















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
//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
	
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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

//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		Debug.println("rmsSaveFile: "+fileName+" <rms disk full>");
	//#endif
		return true;
	}

	rmsDeleteFile(fileName);

//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		Debug.println("rmsLoadFile: "+fileName+" <File not found>");
	//#endif
		return null;
	}

//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		Debug.println("rmsDeleteFile: "+fileName+" <File not found>");
	//#endif
		return true;
	}

//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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
//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
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

// -----------------------------------------------
// Variables staticas para identificar los estados de los menus...
// ===============================================
static final int MENU_MAIN = 0;
static final int MENU_SECOND = 1;
static final int MENU_SCROLL_HELP = 2;
static final int MENU_SCROLL_ABOUT = 3;
static final int MENU_COMPETITION = 4;
static final int MENU_EXIT_CONFIRM = 5;
static final int MENU_OPTIONS = 6;
static final int MENU_NEW_COMPETITION = 7;
static final int MENU_RESTART_CONFIRM = 8;
static final int MENU_EDIT_CUSTOM = 9;
static final int MENU_CHOOSE_SHIRT = 10;
static final int MENU_COMPETITION_TYPE = 11;
//#ifndef DOJA
static final int MENU_NEW_COMPETITION_CONFIRM = 12;
//#endif
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar las acciones de los menus...
// ===============================================

static final int ACTION_NONE = -10;
//static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;
static final int MENU_ACTION_NEW_COMPETITION = 8;
static final int MENU_ACTION_CONTINUE_COMPETITION = 9;
static final int MENU_ACTION_NEXT_MATCH = 10;
static final int MENU_ACTION_SEE_CLASSIFICATION = 11;
static final int MENU_ACTION_EDIT_FORMATION = 12;
static final int MENU_ACTION_OPTIONS = 13;
static final int MENU_ACTION_TEAM_CHG = 14;
static final int MENU_ACTION_LEAGUEDURATION_CHG = 15;
static final int MENU_ACTION_MATCHDURATION_CHG = 16;
static final int MENU_ACTION_CREATE_COMPETITION = 17;
static final int MENU_ACTION_RESTART_MENU = 18;
static final int MENU_ACTION_EXIT_CONFIRM = 19;
static final int MENU_ACTION_RESTART_CONFIRM = 20;
static final int MENU_ACTION_INGAME_EDIT_FORMATION = 21;
static final int MENU_ACTION_ASHIRT_CHG = 22;
static final int MENU_ACTION_BSHIRT_CHG = 23;
static final int MENU_ACTION_SHIRT_CONFIRM = 24;
static final int MENU_ACTION_EDIT_CUSTOM = 25;
static final int MENU_ACTION_EDITING_PLAYER_CHG = 26;
static final int MENU_ACTION_NAME_CHG = 27;
static final int MENU_ACTION_SKIN_CHG = 28;
static final int MENU_ACTION_HAIR_CHG = 29;
static final int MENU_ACTION_SPEED_CHG = 30;
static final int MENU_ACTION_SHOOT_CHG = 31;
static final int MENU_ACTION_PASS_CHG = 32;
static final int MENU_ACTION_TACKLE_CHG = 33;
static final int MENU_ACTION_FREEKICK_CHG = 34;
static final int MENU_ACTION_SUPERLEAGUE = 35;
static final int MENU_ACTION_DOMESTICLEAGUE = 36;
static final int MENU_ACTION_BACK_TO_COMPETITION_MENU = 37;
//#ifndef DOJA
static final int MENU_ACTION_NEW_COMPETITION_CONFIRM = 38;
//#endif


// ===============================================

int menuType;
int menuTypeBack;

boolean menuExit;

Image menuImg;
Image relojImg;

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuTypeBack = menuType;
	menuType = type;

	menuExit = false;

	menuListInit(0, 0, canvasWidth, canvasHeight);


	switch (type)
	{
	case MENU_MAIN:
		
		//#ifdef LARGEUI
			//#ifdef MAINMENUPATCH
			//#else
			menuListInit(canvasWidth/10,(2*canvasHeight/3) - 5,8*canvasWidth/10,canvasHeight/3 + 10);
			//#endif
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		menuListClear();
		if(storedCompetition) menuListAdd(0, gameText[0][1], MENU_ACTION_CONTINUE_COMPETITION);
		menuListAdd(0, gameText[0][0], MENU_ACTION_NEW_COMPETITION);
 		menuListAdd(0, gameText[4][0], MENU_ACTION_OPTIONS);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_CONFIRM);
		menuListSet_Option();

		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
	break;
	
	case MENU_OPTIONS:
		
		//#ifdef LARGEUI
		menuListInit(UI_MENU_MARGIN,canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,3*canvasHeight/5);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		menuListClear();		
	//#ifndef PLAYER_NONE
		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		//#ifndef NOCUSTOMPLAYERS
		menuListAdd(0, gameText[24][0], MENU_ACTION_EDIT_CUSTOM);	
		//#endif
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		//menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_RESTART_MENU);
		menuListSet_Option();
		
		menuListBackwardCMD = MENU_ACTION_RESTART_MENU;

		listenerInit(SOFTKEY_MENU, SOFTKEY_ACEPT);
	break;
	
	case MENU_COMPETITION_TYPE:
		
		menuImg = null;
		storedCompetition = false;
		
		//#ifdef SMALLUI
		//#else			
		menuListInit(UI_MENU_MARGIN,0,canvasWidth - UI_MENU_MARGIN*3,canvasHeight);
		//#endif
				
		menuListClear();
		menuListAdd(0, gameText[39][0], MENU_ACTION_SUPERLEAGUE);
		menuListAdd(0, gameText[39][1], MENU_ACTION_DOMESTICLEAGUE);
		
		menuListBackwardCMD = MENU_ACTION_RESTART_MENU;//-2;ZNR
		
		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		
		menuListSet_Option();		
	break;


	case MENU_NEW_COMPETITION:

		//#ifdef LARGEUI
		menuListInit(UI_MENU_MARGIN,2*canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,2*canvasHeight/5);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
				
		menuListClear();			
        menuListAdd(0, gameText[20], MENU_ACTION_CREATE_COMPETITION);
		menuListAdd(0, teamNames, MENU_ACTION_TEAM_CHG, playerTeam);
		menuListAdd(0, gameText[18], MENU_ACTION_LEAGUEDURATION_CHG, wholeLeague? 1:0);	
		menuListAdd(0, gameText[19], MENU_ACTION_MATCHDURATION_CHG, matchTime);				
		menuListSet_Option();
		
		menuListBackwardCMD = MENU_ACTION_RESTART_MENU;

		listenerInit(SOFTKEY_MENU, SOFTKEY_CHANGE);
	break;


	case MENU_SECOND:		
		//#ifdef LARGEUI
		menuListInit(UI_MENU_MARGIN,canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,3*canvasHeight/5);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
		menuListAdd(0, gameText[22][0], MENU_ACTION_INGAME_EDIT_FORMATION);		
	//#ifndef PLAYER_NONE
		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART_CONFIRM);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_CONFIRM);
		menuListSet_Option();
		
		menuListBackwardCMD = MENU_ACTION_CONTINUE;

		listenerInit(SOFTKEY_CONTINUE, SOFTKEY_ACEPT);
	break;


	case MENU_SCROLL_HELP:
		//#ifdef LARGEUI
		menuListInit(UI_MENU_MARGIN,canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,3*canvasHeight/5);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Screen();
		
		menuListBackwardCMD = MENU_ACTION_RESTART_MENU;

		listenerInit(SOFTKEY_BACK, SOFTKEY_CONTINUE);
	break;


	case MENU_SCROLL_ABOUT:
		//#ifdef LARGEUI
		menuListInit(UI_MENU_MARGIN,canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,3*canvasHeight/5);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
				
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListAdd(0, legalText[0]);
		menuListSet_Screen();
		
		menuListBackwardCMD = MENU_ACTION_RESTART_MENU;

		listenerInit(SOFTKEY_BACK, SOFTKEY_CONTINUE);
	break;
	
	case MENU_COMPETITION :
		//#ifdef LARGEUI
		menuListInit(UI_MENU_MARGIN,canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,3*canvasHeight/5);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		
		menuListClear();
		menuListAdd(0, gameText[21][0], MENU_ACTION_NEXT_MATCH);
		menuListAdd(0, gameText[22][0], MENU_ACTION_EDIT_FORMATION);
		menuListAdd(0, gameText[23][0], MENU_ACTION_SEE_CLASSIFICATION);
		//menuListAdd(0, gameText[15][1], MENU_ACTION_RESTART_MENU);
		
		menuListBackwardCMD = MENU_ACTION_RESTART_MENU;
		
		listenerInit(SOFTKEY_MENU, SOFTKEY_ACEPT);
		
		menuListSet_Option();
	break;
	
	case MENU_EXIT_CONFIRM :
			
		//#ifdef SMALLUI
		//#else			
		menuListInit(UI_MENU_MARGIN,0,canvasWidth - UI_MENU_MARGIN*3,canvasHeight);
		//#endif
				
		menuListClear();
		menuListAdd(0, gameText[13][0], MENU_ACTION_EXIT_GAME);
		menuListAdd(0, gameText[13][1], -2);
		
		menuListBackwardCMD = -2;
		
		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		
		menuListSet_Option();
	break;
	
	case MENU_RESTART_CONFIRM :
			
		//#ifdef SMALLUI
		//#else			
		menuListInit(UI_MENU_MARGIN,0,canvasWidth - UI_MENU_MARGIN*3,canvasHeight);
		//#endif
				
		menuListClear();
		menuListAdd(0, gameText[13][0], MENU_ACTION_RESTART);
		menuListAdd(0, gameText[13][1], -2);
		
		menuListBackwardCMD = -2;
		
		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		
		menuListSet_Option();
	break;
	
	//#ifndef DOJA
	case MENU_NEW_COMPETITION_CONFIRM :
		
		//#ifdef SMALLUI
		//#else			
		menuListInit(UI_MENU_MARGIN,0,canvasWidth - UI_MENU_MARGIN*3,canvasHeight);
		//#endif
				
		//confirmed
		menuListClear();
		menuListAdd(0, gameText[13][0], MENU_ACTION_NEW_COMPETITION_CONFIRM);
		menuListAdd(0, gameText[13][1], -2);
		
		menuListBackwardCMD = -2;
		
		listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
		
		menuListSet_Option();
		break;
	//#endif
		
	//#ifndef NOCUSTOMPLAYERS	
	case MENU_EDIT_CUSTOM:
		
		recalcAttribSum();
		
		//#ifdef LARGEUI
		menuListInit(UI_MENU_MARGIN,canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,3*canvasHeight/5);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		menuListClear();		
		menuListAdd(0, customPlayerNames, MENU_ACTION_EDITING_PLAYER_CHG, editingPlayer);
		//#ifndef NOEDITABLENAMES
		menuListAdd(0, gameText[28][0], MENU_ACTION_NAME_CHG);
		//#endif
		//#ifdef SKINNING
		menuListAdd(0, gameText[29], MENU_ACTION_SKIN_CHG,customPlayerData[editingPlayer][0]);
		menuListAdd(0, gameText[30], MENU_ACTION_HAIR_CHG, customPlayerData[editingPlayer][1]);
		//#endif
		for(int i = 0; i < 5; i++)
		{
			/*String aux[] = new String[10];
			
			for(int j = 0; j < 10; j++)			
				aux[j] = gameText[31][i]+": "+(j+1);*/										
		
			menuListAdd(0, gameText[31][i]+": "+(customPlayerData[editingPlayer][2+i]), MENU_ACTION_SPEED_CHG + i);
			//menuListAdd(0, gameText[31][i]+": "+(customPlayerData[editingPlayer][2+i]+1), MENU_ACTION_SPEED_CHG + i);
		}
						
		menuListSet_Option();
		
		//menuListBackwardCMD = MENU_ACTION_RESTART_MENU;
		menuListBackwardCMD = MENU_ACTION_OPTIONS;

		listenerInit(SOFTKEY_ACEPT, SOFTKEY_ATTRIB_CHANGE);
	break;
	//#endif
	
	case MENU_CHOOSE_SHIRT:
	//#ifdef LARGEUI
	menuListInit(UI_MENU_MARGIN,2*canvasHeight/5,canvasWidth - UI_MENU_MARGIN*3,2*canvasHeight/5);
	//#elifdef MEDIUMUI
	//#elifdef SMALLUI
	//#endif
				
	menuListClear();				
    menuListAdd(0, gameText[20][0], MENU_ACTION_SHIRT_CONFIRM);     
	menuListAdd(0, gameText[33], MENU_ACTION_ASHIRT_CHG, AhomeCostume? 0:1);	
	menuListAdd(0, gameText[34], MENU_ACTION_BSHIRT_CHG, BhomeCostume? 0:1);			
	menuListSet_Option();
		
	menuListBackwardCMD = MENU_ACTION_BACK_TO_COMPETITION_MENU;

	listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;
	
	
	/*case MENU_CLASSIFICATION :
		menuListClear();
		for(int i = 0; i < 16; i++)
			menuListAdd(0, (i+1)+"-Team "+compet.leagueRank[i][0]+" P:"+compet.leagueRank[i][1]+" V:"+compet.leagueRank[i][2]+" L:"+compet.leagueRank[i][3]+" E:"+compet.leagueRank[i][4]+" GF:"+compet.leagueRank[i][5]+" GC:"+compet.leagueRank[i][6]);
		menuListSet_Screen();

		listenerInit(SOFTKEY_MENU, SOFTKEY_CONTINUE);
	break;
	*/	
	}

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

	case MENU_ACTION_SUPERLEAGUE:
		isDomestic = false;
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		gameStatus = GAME_MENU_MAIN_END;
		menuExit = true;		
	break;
	
	case MENU_ACTION_DOMESTICLEAGUE:
		isDomestic = true;
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		gameStatus = GAME_MENU_MAIN_END;
		menuExit = true;		
	break;
	
	case MENU_ACTION_NEW_COMPETITION:		// Jugar de 0

		//#ifndef DOJA
		if(storedCompetition)
			menuInit(MENU_NEW_COMPETITION_CONFIRM);
 		else
 		{
 			listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
 			menuExit = true;
 			menuInit(MENU_COMPETITION_TYPE); 			
 		}
		//#else
		//#endif
	break;
	
	//#ifndef DOJA
	//confirmed
	case MENU_ACTION_NEW_COMPETITION_CONFIRM:		
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
		menuInit(MENU_COMPETITION_TYPE);
		break;
	//#endif
	
	
	case MENU_ACTION_CREATE_COMPETITION:
		menuExit = true;
		setGameStatus(GAME_CREATE_COMPETITION);		
	break;
	
	case MENU_ACTION_CONTINUE_COMPETITION:		// Jugar de 0		
		menuExit = true;		
		setGameStatus(GAME_CONTINUE_COMPETITION);		
	break;

	case MENU_ACTION_CONTINUE:	// Continuar

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;		
	break;


	case MENU_ACTION_SHOW_HELP:		// Mostramos Instrucciones...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// Mostramos About...

		menuInit(MENU_SCROLL_ABOUT);
	break;


	case MENU_ACTION_RESTART:	// Provocamos GAME OVER desde menu

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		playExit = 3;		// flag para SALIDA de juego tipo GAME OVER
		destroyMatch();
		
		//competitionDestroy
		//#ifndef NOLEAGUE
		leagueRank = null; league = null;
		//#endif
		
		//#ifndef NOTOURNAMENT
		tourTree = null; tourGoals = null;
		//#endif
		
		changesList = null;

		//destroyTeamsData
		teamNames = null;
		
		AplayerNames = null;
		BplayerNames = null;
		
		AplayerData = null;
		BplayerData = null;
		
		AshirtsRGB = null;
		BshirtsRGB = null;	
		//teamLogoIds = null;	
		
		
		
		gameStatus = GAME_MENU_START; menuExit = true;		
	break;
	
	case MENU_ACTION_RESTART_MENU:	// Provocamos GAME OVER desde menu
	
		savePrefs();
		destroyMatch();
		
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		playExit = 3;		// flag para SALIDA de juego tipo GAME OVER
		menuExit = true;
		setGameStatus(GAME_MENU_MAIN); 
		soundPlay(0,0);			
	break;

	
	case MENU_ACTION_OPTIONS:
		setGameStatus(GAME_MENU_MAIN);
		menuInit(MENU_OPTIONS);		
	break;
	
	case MENU_ACTION_EXIT_CONFIRM:		
		menuInit(MENU_EXIT_CONFIRM);		
	break;
	
	case MENU_ACTION_RESTART_CONFIRM:		
		menuInit(MENU_RESTART_CONFIRM);		
	break;

	case MENU_ACTION_EXIT_GAME:	// Exit com.mygdx.mongojocs.sanfermines2006.Game

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		gameExit = true;

//		popupInit("�Realmente desea salir?",  POPUP_ASK, POPUP_ACTION_NO, POPUP_ACTION_GAME_EXIT,  SOFTKEY_CANCEL, SOFTKEY_ACEPT);
	break;	



	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListOpt() != 0;
		if (!gameSound) {soundStop();} else {if (menuType == MENU_OPTIONS) soundPlay(0,0); else soundPlay(5,1);}
		//#ifdef SAVEPREFSONCHANGE
		//#endif		
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListOpt() != 0;
		if (gameVibra) {vibraInit(300);}
		//#ifdef SAVEPREFSONCHANGE
		//#endif
	break;
	
	//#ifndef NOEDITABLENAMES
	case MENU_ACTION_NAME_CHG:
		setGameStatus(GAME_IDLE);
		ga.inputDialogCreate(gameText[27][0],gameText[27][1],gameText[27][2],10);
		ga.inputDialogInit();
	break;
	//#endif
	
	case MENU_ACTION_TEAM_CHG:
		playerTeam = (byte)menuListOpt();
	break;
	
	case MENU_ACTION_LEAGUEDURATION_CHG:
		wholeLeague = menuListOpt() != 0;
	break;
	
	case MENU_ACTION_MATCHDURATION_CHG:
		matchTime = (byte)menuListOpt();
	break;
	
	
	case MENU_ACTION_NEXT_MATCH:
		menuExit = true;
		setGameStatus(GAME_VERSUS_LOAD);
	break;	
	
	case MENU_ACTION_SEE_CLASSIFICATION:
		menuExit = true;
		setGameStatus(GAME_MENU_CLASSIFICATION);
	break;	
	
	case MENU_ACTION_EDIT_FORMATION:
		menuExit = true;
		//#ifdef FORMATIONHELP
		//#else
		setGameStatus(GAME_MENU_FORMATION);
		//#endif
	break;	
	
	case MENU_ACTION_INGAME_EDIT_FORMATION:
		menuExit = true;
		//#ifdef FORMATIONHELP
		//#else
		setGameStatus(GAME_MENU_INGAME_FORMATION);
		//#endif
	break;			
	
	//#ifndef NOCUSTOMPLAYERS
	case MENU_ACTION_EDIT_CUSTOM:
		menuExit = true;
		setGameStatus(GAME_MENU_EDIT_CUSTOM);
	break;	
		
	case MENU_ACTION_EDITING_PLAYER_CHG:
		editingPlayer = menuListOpt();
		menuInit(MENU_EDIT_CUSTOM);
	break;		
	
	case MENU_ACTION_SKIN_CHG:	
	case MENU_ACTION_HAIR_CHG:
		customPlayerData[editingPlayer][cmd - MENU_ACTION_SKIN_CHG] = (byte)menuListOpt();
		break;
		
	case MENU_ACTION_SPEED_CHG:	
	case MENU_ACTION_SHOOT_CHG:	
	case MENU_ACTION_PASS_CHG:	
	case MENU_ACTION_TACKLE_CHG:	
	case MENU_ACTION_FREEKICK_CHG:
		int pos = cmd - MENU_ACTION_SKIN_CHG, menuLastPos = menuListPos;
						
		customPlayerData[editingPlayer][pos]++;
		
		recalcAttribSum();
		
		if(customPlayerData[editingPlayer][pos] >= 10 || attribSum >= MAXIMUMPOINTS)
			customPlayerData[editingPlayer][pos] = 0;
		
		menuInit(menuType); menuListPos = menuLastPos;
	break;
	//#endif
	
	case MENU_ACTION_ASHIRT_CHG:
		AhomeCostume = menuListOpt() == 0;
	break;
	
	case MENU_ACTION_BSHIRT_CHG:
		BhomeCostume = menuListOpt() == 0;
	break;
	
	case MENU_ACTION_SHIRT_CONFIRM:
		menuExit = true;
		setGameStatus(GAME_PLAY);
	break;
	
	case MENU_ACTION_BACK_TO_COMPETITION_MENU:
		menuExit = true;
		setGameStatus(GAME_MENU_COMPETITION);
	break;
	}

}

//#ifndef NOCUSTOMPLAYERS

int attribSum;

void recalcAttribSum()
{
	attribSum = 0;

	for(int i = 2; i < 7; i++)
		attribSum += customPlayerData[editingPlayer][i];
}
//#endif


// -------------------
// menu Tick
// ===================
/*
public boolean menuTick()
{
	if ( menuListTick( (keyY!=lastKeyY? keyY: 0 ), (keyX!=lastKeyX? keyX:0), (keyMenu!=lastKeyMenu? keyMenu:0) ) )
	{
		menuAction(menuListCMD);
	}

	if (menuExit) {return true;}

	return false;
}*/

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
// ===============================================

// -----------------------------------------------
// Variables staticas para identificar los estados del juego...
// ===============================================
final static int GAME_START = 0;
final static int GAME_LOGOS = 5;

final static int GAME_MENU_START = 10;
final static int GAME_PICTURE = 15;
final static int GAME_MENU_MAIN = 11;
final static int GAME_MENU_MAIN_END = 12;

final static int GAME_MENU_SECOND = 25;

final static int GAME_MENU_GAMEOVER = 30;

final static int GAME_PLAY = 20;
final static int GAME_PLAY_INIT = 21;
final static int GAME_PLAY_TICK = 22;

final static int GAME_MENU_NEW_COMPETITION = 40;
final static int GAME_CREATE_COMPETITION = 41;
final static int GAME_CONTINUE_COMPETITION = 42;
final static int GAME_MENU_COMPETITION = 50;
final static int GAME_MENU_CLASSIFICATION = 60;
final static int GAME_VERSUS_SCREEN = 70;
final static int GAME_MENU_FORMATION = 80;
final static int GAME_MATCH_ENDED = 90;
final static int GAME_VERSUS_LOAD = 100;
final static int GAME_MENU_INGAME_FORMATION = 110;
final static int GAME_MENU_EDIT_CUSTOM = 120;
final static int GAME_MATCH_END_LED = 130;
final static int GAME_HALF_MATCH_LED = 131;
final static int GAME_GOAL_LED = 132;
final static int GAME_PENALTY_LED = 133;
final static int GAME_MENU_CHOOSE_SHIRT = 140;
final static int GAME_LEAGUE_END = 150;
final static int GAME_MENU_FORMATION_HELP = 160;
final static int GAME_MENU_INGAME_FORMATION_HELP = 170;
final static int GAME_SPLASH_SCREEN = 180;
final static int GAME_IDLE = 1000;

// ===============================================

//#ifndef NOCUSTOMPLAYERS
final static int CUSTOM_PLAYERS_NUMBER = 5;
final static int MAXIMUMPOINTS = 35;
//#else
//#endif

/*com.mygdx.mongojocs.clubfootball2006.ConnectionHTTP net;

int downloadType;
int downloadReturnOk;
int downloadReturnCancel;*/

int gameStatus = 0;

int progressPercent = 0;
byte playerTeam = 0, cpuTeam = 0;
int subOption = 0, subOptionScroll = 0, matchTime = 0, chosenPlayer = -1, changesLeft = 0, editingPlayer = 0;
boolean wholeLeague = true, storedCompetition = false, changesList[];
int tableStartColumn = 0, tableStartRow = 0;

//Competition compet;

String teamNames[];
String AplayerNames[];
String BplayerNames[];
String customPlayerNames[];
byte AplayerData[][];
byte BplayerData[][];
byte customPlayerData[][];
int AshirtsRGB[][];
int BshirtsRGB[][];
boolean AhomeCostume, BhomeCostume;
byte teamLogoIds[];



public void loadTeamsData(int a, int b, boolean namesOrData)
{
	
	//#ifdef INITIALLOAD
	//#else
	byte buffer[] = loadFile(isDomestic ? "/Domestic.dat" : "/Licensed.dat");
	//#endif
	int rgb, area;
	
	try{
	
	
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer,0,buffer.length));
		
		teamsNumber = dis.readByte();
		
		byte numTeams = (byte)teamsNumber; 
				
		teamLogoIds = new byte[numTeams];
		
		if(namesOrData)				
			teamNames = new String[numTeams];
		else
		{		
			AshirtsRGB = new int [2][12];
			BshirtsRGB = new int [2][12];
		}
		
				
		for(int i = 0; i < numTeams; i++)
		{
			
			if(namesOrData)
			{
				teamNames[i] = dis.readUTF();
				//#ifdef NOSPECIALCHARSINDB
				//#endif
			}
			else
				dis.readUTF();
						
			for(int l = 0; l < 2; l++)
			{
						
				byte numRGB = dis.readByte();
						
				for(int j = 0; j < numRGB; j++)
				{				
					rgb = dis.readInt();
					
					byte numAreas = dis.readByte();
					
					for(int k = 0; k < numAreas; k++)
					{
						area = dis.readByte();
						
						if(i==a) AshirtsRGB[l][area] = rgb;
						else if(i==b) BshirtsRGB[l][area] = rgb;									
					}
											
				}
								
			}
			
			teamLogoIds[i] = dis.readByte();
				
					
			byte numPlayers = dis.readByte();
			
			if(i == a) 
			{
				AplayerNames = new String[numPlayers + CUSTOM_PLAYERS_NUMBER];
				AplayerData = new byte[numPlayers + CUSTOM_PLAYERS_NUMBER][7];
			}
			if(i == b) 
			{
				BplayerNames = new String[numPlayers];
				BplayerData = new byte[numPlayers][7];
			}
			
			
			for(int j = 0; j < numPlayers; j++)
			{
				
				if(i == a)
				{
					AplayerNames[j] = dis.readUTF();
					//#ifdef NOSPECIALCHARSINDB
					//#endif
				}
				else if(i == b)
				{
					BplayerNames[j] = dis.readUTF();
					//#ifdef NOSPECIALCHARSINDB
					//#endif
				}
				else dis.readUTF();
				
				for(int k = 0; k < 7; k++)
				{				
					if(i == a) AplayerData[j][k] = dis.readByte();
					else if(i == b) BplayerData[j][k] = dis.readByte();
					else dis.readByte();
										
				}				
			}
		}
		
		//#ifndef NOCUSTOMPLAYERS
		if(!namesOrData)
		for(int i = 0; i < CUSTOM_PLAYERS_NUMBER; i++)
		{		
			AplayerNames[i + AplayerNames.length - CUSTOM_PLAYERS_NUMBER] = customPlayerNames[i];
			
			for(int j = 0; j < 7; j++)
				AplayerData[i + AplayerNames.length - CUSTOM_PLAYERS_NUMBER][j] = customPlayerData[i][j];
		}
		//#endif
							
	}catch(Exception e){e.printStackTrace();}

    
}

//#ifdef NOSPECIALCHARSINDB
//#endif

/*void destroyTeamsData()
{
	teamNames = null;
	
	AplayerNames = null;
	BplayerNames = null;
	
	AplayerData = null;
	BplayerData = null;
	
	AshirtsRGB = null;
	BshirtsRGB = null;	
	teamLogoIds = null;	
}*/

void destroyMatch()
{
	// LOGICA
	int i = 0;
	while (i < 11) {
		//for(int i = 0;i < 11;i++) {        
    	sortedEntities[i] = teamA[i] = null;                
    	sortedEntities[i+11] = teamB[i] = null;        
    	i++;
	}

	sortedEntities[22] = ball = null;                

	gkA = null;
	gkB = null;
	
	//GFX
	
	imgTeam[0] = imgTeam[1] = null;
	
	//#ifndef INITIALLOAD
		playerCor = miscCor = ledAnim = null;
		pori = pord = null;	
		imgMisc = imgNumbers = null;
		//#ifndef NOGRASSIMG
		grassImg = null;
		//#endif
		//#ifdef CIRCLEIMG
		//#endif
	//#endif
}

// -------------------
// game Create
// ===================

public void gameCreate()
{

// --------------------------------
// Cargamos Preferencias
// ================================

	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("Loading prefs...");
	//#endif
	
	try{
	loadPrefs();
	}catch(Exception e ){
		System.out.println(e.toString());
	}
	
//#ifdef WEBSUBSCRIPTION
//#endif	
		
//#ifdef FORCESTARTUPREPAINT
//#endif
	
	
// ================================


// --------------------------------
// Cargamos e inicializamos TODOS los sonidos del juego
// ================================
//#ifndef PLAYER_NONE
	
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("Loading sound...");
	//#endif

	//#ifdef PLAYER_OTA
	//#elifdef PLAYER_SAMSUNG
	//#elifdef PLAYER_SHARP
	//#elifdef DOJA
	//#else
		soundCreate( new String[] {
			"/0.mid",		// 0 - Musica de la caratula
			"/1.mid",		// 1 - 
			"/2.mid",	// 2 - 
			"/3.mid",		// 3 - 
			"/4.mid",		// 3 - 
			"/5.mid",		// 3 -
			//#ifdef NULLMIDIPATCH
			//#else
			"/6.mid"		// 3 -
			//#endif
			});

	//#endif


	//#ifdef SG-D410
	//#endif

//#endif
// ================================



//#ifdef J2ME
	//#ifdef FULLSCREEN
		//listenerImg = loadImage("/softkeys");
		//listenerCor = loadFile("/softkeys.cor");
	//#endif
//#endif

		
	//#ifdef FORCESTARTUPREPAINT
	//#endif
		
	//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("Formation info...");
	//#endif

 	formation = new int[6][10][2];
    
    byte aux[] = loadFile("/formation.dat");
    
    for(int i = 0; i<120; i++) 
    formation[i/20][(i/2)%10][i%2] = aux[i];
    
    aux = null;	
    
    //#ifdef INITIALLOAD
    //#endif
}

// -------------------
// game Destroy
// ===================

/*public void gameDestroy()
{
}*/

public void progressBarInit()
{
	listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
	progressPercent = 0; playShow = true; gameDraw();
}

public void progressBarStep(int s)
{
	progressPercent += s; if(progressPercent > 100) progressPercent = 100;
	playShow = true; gameDraw();
}


public void setGameStatus(int newStatus)
{
	
	gameStatus = newStatus; repaintRequest = true;
	
	switch(newStatus)
	{
		case GAME_MENU_NEW_COMPETITION :	
		
		menuImg = null;								        
		playerTeam = (isDomestic ? domHeadLineTeam : headLineTeam); 
		matchTime = 0; wholeLeague = true;
		break;
		
		//case GAME_CREATE_COMPETITION :							      
		//break;	
		
		case GAME_CONTINUE_COMPETITION :		
		menuImg = null;		
		break;
		
		case GAME_MENU_COMPETITION :
		soundPlay(1,0);		 
		break;
		
		//case GAME_VERSUS_LOAD :				
		//break;
				
		case GAME_VERSUS_SCREEN :						
		listenerInit(SOFTKEY_NONE,SOFTKEY_CONTINUE);
		break;	
		
		case GAME_MENU_CLASSIFICATION:				
		tableStartRow = 1;
		tableStartColumn = 0;				
		listenerInit(SOFTKEY_NONE,SOFTKEY_ACEPT);
		break;
		
		case GAME_MATCH_ENDED :
		
        destroyMatch(); 
        			
		competitionSimulate();
		setTourGoals(playerTeam,(byte)goalsA);
		setTourGoals(cpuTeam,(byte)goalsB);
		competitionStep();
		
		//rmsSaveFile("storedGame",competitionWrite());		
		storedCompetition = true;
		savePrefs();
		
		soundPlay(2,1);
		
		listenerInit(SOFTKEY_NONE, SOFTKEY_ACEPT);
		break;
		
		case GAME_PLAY :		
		listenerInit(SOFTKEY_MENU,SOFTKEY_NONE);
		break;
		
		//#ifdef FORMATIONHELP
		//#endif
		case GAME_MENU_INGAME_FORMATION :
		chosenPlayer = -1;
		subOption = 0;
		subOptionScroll = 0;
		listenerInit(SOFTKEY_MENU, SOFTKEY_FORMATION_CHANGE);
		break;	
			
		case GAME_MENU_FORMATION :
		chosenPlayer = -1;
		subOption = 0;
		subOptionScroll = 0;
		listenerInit(SOFTKEY_MENU, SOFTKEY_CHANGE);
		break;
		
		case GAME_MENU_CHOOSE_SHIRT :
		AhomeCostume = true;
		BhomeCostume = true;
		break;
		

		case GAME_GOAL_LED:
		//#ifndef NOLED		
		ledFirstDraw = true; ledLastFrame = -1; ledBeginFrame = 5; ledEndFrame = 8; ledCurrentFrame = ledBeginFrame; ledLoop = true;
		//#endif
		//#ifndef NOSOUNDFX
		soundPlay(3,1);
		//#endif
		break;
		
		case GAME_MATCH_END_LED:
		//#ifndef NOLED		
		ledFirstDraw = true; ledLastFrame = -1; ledBeginFrame = 18; ledEndFrame = 23; ledCurrentFrame = ledBeginFrame; ledLoop = false;
		//#endif
		//#ifndef NOSOUNDFX
		soundPlay(6,1);		
		//#endif
		break;
		
		case GAME_HALF_MATCH_LED:
		//#ifndef NOLED		
		ledFirstDraw = true; ledLastFrame = -1; ledBeginFrame = 0; ledEndFrame = 4; ledCurrentFrame = ledBeginFrame; ledLoop = false;
		//#endif
		//#ifndef NOSOUNDFX
		soundPlay(6,1);
		//#endif
		break;
		
		//#ifndef NOLED		
		case GAME_PENALTY_LED:		
		ledFirstDraw = true; ledLastFrame = -1; ledBeginFrame = 10; ledEndFrame = 17; ledCurrentFrame = ledBeginFrame; ledLoop = false;		
		break;
		//#endif		
						
	}
		
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

      //

		gameStatus = GAME_LOGOS;
	break;
// ===============================



// -------------------------------
// Lanzamos secuancia inicial de logotipos
// ===============================
	case GAME_LOGOS:

		//logosInit(new String[] {"/microjocs"}, new int[] {0xffffff}, 3000);
		
        //nameLogos = fileNames;
        numLogos = rgbLogos.length;
        cntLogos = 0;
        //rgbLogos = rgb;
        //timeLogos = time;

        timeIniLogos = System.currentTimeMillis() - timeLogos;

        biosStatus = BIOS_LOGOS;
		gameStatus = GAME_SPLASH_SCREEN;
	break;
// ===============================

	case GAME_SPLASH_SCREEN+2:
	case GAME_SPLASH_SCREEN+1:
	case GAME_SPLASH_SCREEN:
		timeIniLogos = System.currentTimeMillis();
		playShow = true; gameDraw();
		while(System.currentTimeMillis() < timeIniLogos + 2500);
		gameStatus++;
		//#ifdef LARGEUI
		if(gameStatus > GAME_SPLASH_SCREEN) gameStatus = GAME_MENU_START;
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
	break;

// -------------------------------
// Gestor de menus del source base
// ===============================
	case GAME_MENU_START:

		//#ifdef INITIALLOAD
		//#else
		menuImg = loadImage("/caratula");
		//#endif
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		playShow = true;
		soundPlay(0,0);		
		gameStatus = GAME_PICTURE;
	break;
	
	case GAME_PICTURE:		
		if((keyX != 0 && keyX != lastKeyX) || 
			(keyY != 0 && keyY != lastKeyY) || 
			(keyMenu != 0 && keyMenu != lastKeyMenu))
			gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:
		//if(menuImg == null) 
		//#ifdef LARGEUI		
			//#ifdef INITIALLOAD
			//#else
			menuImg = loadImage("/menuimage");
			//#endif
		//#endif
		menuInit(MENU_MAIN);		
	break;
	

	case GAME_MENU_MAIN_END:
		
		menuImg = null;

		//setGameStatus(GAME_MENU_NEW_COMPETITION_CONFIRM);
		setGameStatus(GAME_MENU_NEW_COMPETITION);
	break;



	case GAME_MENU_SECOND:

		menuInit(MENU_SECOND);
		//keyMenu = 0;
		backFromMenu = true;
		gameStatus = GAME_PLAY_TICK;
	break;
    /*
	case GAME_MENU_GAMEOVER:


		canvasFillTask(0x000000);
		canvasTextTask(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, TEXT_VCENTER|TEXT_HCENTER | TEXT_OUTLINE);
		gameDraw();

		waitTime(3000);

		gameStatus = GAME_MENU_START;
	break;*/
	
	case GAME_MENU_COMPETITION:
		menuInit(MENU_COMPETITION);
	break;
// ===============================



// -------------------------------
// Gestor del motor del juego
// ===============================
	
		
	case GAME_MENU_NEW_COMPETITION:
	
		progressBarInit();
		
		loadTeamsData(-1, -1, true);
		
		progressBarStep(100);
		
		menuInit(MENU_NEW_COMPETITION);
		/*playShow = true;
		
		if (keyY != 0 && keyY != lastKeyY ) {
			subOption = (3 + subOption + keyY)%3;
		}		
		if (keyX != 0 && keyX != lastKeyX ) {
			
			switch(subOption)
			{
				case 0 : 
				playerTeam = (byte)((16 + playerTeam + keyX)%16); 
				break;
				
				case 1 :
				wholeLeague = !wholeLeague;
				break;
				
				case 2 :				
				matchTime = (3 + matchTime + keyX)%3; 
				break;
				
			}
			
		}		
		
		if (keyMenu != 0 && lastKeyMenu != keyMenu) {
												
			setGameStatus(GAME_CREATE_COMPETITION);			
		}		
		*/
	break;
	
	case GAME_CREATE_COMPETITION:		
	
		progressBarInit();
								
		loadTeamsData(playerTeam, -1, false);				
				
		// Create competition					
		
		progressBarStep(50);
		
		competitionStartUp(teamsNumber,LEAGUE);		
		
		progressBarStep(50);
		
		myTeam = playerTeam;
		
		// Set the initial alignment
		
		for(int i = 0; i<myAlignment.length; i++)
			myAlignment[i] = (byte)i;
			
	
		setGameStatus(GAME_MENU_COMPETITION);
	break;
	
	case GAME_CONTINUE_COMPETITION:	
	
		progressBarInit();
		
		loadTeamsData(-1, -1, true);
		
		progressBarStep(33);
		
		loadTeamsData(myTeam, -1, false);				
		
		progressBarStep(33);
				
		// Restore competition				
		loadPrefs();
		
		progressBarStep(33);
		
		playerTeam = myTeam;
		
		setGameStatus(GAME_MENU_COMPETITION);
	break;

	case GAME_MENU_CLASSIFICATION:
										
		if (keyX != 0 && lastKeyX == 0) 
		{
			if(tableStartRow > 1 && keyX < 0) tableStartRow--;
			if(tableStartRow < 6 && keyX > 0) tableStartRow++;			
			playShow = true;
		}		
		
		if (keyY != 0 && lastKeyY == 0) 
		{
			if(tableStartColumn > 0 && keyY < 0) tableStartColumn--;
			if(tableStartColumn < teamsNumber - 1 && keyY > 0) tableStartColumn++;			
			playShow = true;
		}		
		
		if (keyMenu > 0 && lastKeyMenu == 0) {
			//ZNR
			if(current < (wholeLeague ? (leagueRank.length-1)*2 : (leagueRank.length-1)  ))			
				setGameStatus(GAME_MENU_COMPETITION);			
			else {				
				storedCompetition = false;
				savePrefs();
				setGameStatus(GAME_LEAGUE_END);			
			}
		}		
	break;
	
	//#ifdef FORMATIONHELP
	/*case GAME_MENU_FORMATION_HELP:
		if((keyX != 0 && keyX != lastKeyX) || 
				(keyY != 0 && keyY != lastKeyY) || 
				(keyMenu != 0 && keyMenu != lastKeyMenu))
				gameStatus = GAME_MENU_FORMATION;
	break;
	
	case GAME_MENU_INGAME_FORMATION_HELP:
		if((keyX != 0 && keyX != lastKeyX) || 
				(keyY != 0 && keyY != lastKeyY) || 
				(keyMenu != 0 && keyMenu != lastKeyMenu))
				gameStatus = GAME_MENU_INGAME_FORMATION;
	break;*/
	//#endif
	
	case GAME_MENU_FORMATION:
		playShow = false;
		
		if (keyX != 0 && lastKeyX == 0) 
		{
			myFormation = (formation.length + myFormation + keyX)%formation.length;
			playShow = true;
		}		
		
		if (keyY != 0 && lastKeyY == 0) 
		{
			subOption = (AplayerNames.length + subOption + keyY)%AplayerNames.length;
			playShow = true;
		}		
		
		if (keyMenu < 0 && lastKeyMenu == 0) 
		{
			if(chosenPlayer < 0)								
				setGameStatus(GAME_MENU_COMPETITION);			
			else {			
				chosenPlayer = -1;
				listenerInit(SOFTKEY_BACK, SOFTKEY_CHANGE);				
				playShow = true;
			}
		}		
		
		if (keyMenu > 0 && lastKeyMenu == 0) 
		{
			if(chosenPlayer < 0)
			{
				chosenPlayer = subOption;
				listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
				
			}else{
				
				// Do change
				byte aux = myAlignment[chosenPlayer];
				myAlignment[chosenPlayer] = myAlignment[subOption];
				myAlignment[subOption] = aux;
				
				chosenPlayer = -1;
				listenerInit(SOFTKEY_BACK, SOFTKEY_CHANGE);				
			}				
			playShow = true;			
		}		
	break;
	
	case GAME_MENU_INGAME_FORMATION :
		playShow = false;
		
		if (keyX != 0 && lastKeyX == 0) 
		{
			myFormation = (formation.length + myFormation + keyX)%formation.length;
			playShow = true;
		}		
		
		if (keyY != 0 && lastKeyY == 0) 
		{
			if(chosenPlayer < 0)
				subOption = (11 + subOption + keyY)%11;
			else{			
				subOption += keyY;
				if(subOption >= AplayerNames.length) subOption -= AplayerNames.length - 11;
				if(subOption < 11) subOption += AplayerNames.length - 11;
			}
			playShow = true;
		}		
		
		if (keyMenu < 0 && lastKeyMenu == 0) 
		{
			if(chosenPlayer < 0)
			{
				//changeFormation(myFormation);
            	changeFormation = true;
            	newFormation = myFormation;
            	assignPlayerData();
				setGameStatus(GAME_MENU_SECOND);			
				
			}else{			
								
				chosenPlayer = -1; subOption = 0;
				listenerInit(SOFTKEY_BACK, SOFTKEY_FORMATION_CHANGE);				
				playShow = true;								
			}
		}		
		
		if (keyMenu > 0 && lastKeyMenu == 0) 
		if(changesLeft > 0)
		if(!changesList[myAlignment[subOption]])
		{
			if(chosenPlayer < 0)
			{
				chosenPlayer = subOption; subOption = 11;
				listenerInit(SOFTKEY_CANCEL, SOFTKEY_ACEPT);
				
			}else{
				
				changesList[myAlignment[chosenPlayer]] = true;
				
				// Do change
				byte aux = myAlignment[chosenPlayer];
				myAlignment[chosenPlayer] = myAlignment[subOption];
				myAlignment[subOption] = aux;
												
				chosenPlayer = -1; changesLeft--; subOption = 0;
				listenerInit(SOFTKEY_BACK, SOFTKEY_FORMATION_CHANGE);				
			}				
			playShow = true;			
		}		
	break;	
	
	//#ifndef NOCUSTOMPLAYERS
	case GAME_MENU_EDIT_CUSTOM:
		menuInit(MENU_EDIT_CUSTOM);
	break;
	//#endif
	
	case GAME_VERSUS_LOAD:
	
		progressBarInit();
		
		cpuTeam = myOpp(playerTeam);				
		loadTeamsData(playerTeam, cpuTeam, false);		
		
		progressBarStep(100);
		
		setGameStatus(GAME_VERSUS_SCREEN);
	break;
	
	case GAME_VERSUS_SCREEN:
		//playShow = true;
		if (keyMenu > 0 && lastKeyMenu == 0) {
			setGameStatus(GAME_MENU_CHOOSE_SHIRT);			
		}				
	break;
	
	case GAME_LEAGUE_END:		
		if (keyMenu > 0 && lastKeyMenu == 0) 
		{			
			menuAction(MENU_ACTION_RESTART);
		}				
	break;
		
	case GAME_MENU_CHOOSE_SHIRT:
		menuInit(MENU_CHOOSE_SHIRT);
	break;
	
	case GAME_MATCH_END_LED:
		playShow = true;
		if (keyMenu != 0 && lastKeyMenu == 0) {
			setGameStatus(GAME_MATCH_ENDED);
		}
	break;
	
    case GAME_HALF_MATCH_LED:
    case GAME_GOAL_LED:
    case GAME_PENALTY_LED:
        playShow = true;
        if (keyMenu != 0 && lastKeyMenu == 0) {
            setGameStatus(GAME_PLAY_TICK);           
        }
    break;
    
	
	
	case GAME_MATCH_ENDED:
		//playShow = true;
		if (keyMenu > 0 && lastKeyMenu == 0) {
			setGameStatus(GAME_MENU_CLASSIFICATION);
		}
	break;

	case GAME_PLAY:
		//playCreate();
        initResources();
		gameStatus++;

	case GAME_PLAY_INIT:
		//playInit();
        changesLeft = 3;
        changesList = new boolean[AplayerNames.length];
        for(int i = 0; i < changesList.length; i++)
            changesList[i] = false;
        initMatch(myFormation);
        listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		playExit=0;	
		soundStop();			
		gameStatus++;
	break;

	case GAME_PLAY_TICK:
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}

		if (backFromMenu) {intKeyMenu = 0;backFromMenu = false;}
		
		if ( !playTick() ) {break;}

        if (firstPart)
        {
             setGameStatus(GAME_HALF_MATCH_LED);
             halfMatch = true;   
             firstPart = false;             
        }
        else
        {
		    //playRelease();

		    gameStatus--;

		/*switch (playExit)
		{
		case 1:	// Nivel Completado, pasamos al siguiente nivel
		break;

		case 2:	// Una vida menos y regresamos al inicio del nivel
		break;

		case 3:	// Producir com.mygdx.mongojocs.sanfermines2006.Game Over*/

			//playDestroy();		    
            
			//gameStatus = GAME_MENU_GAMEOVER;
			setGameStatus(GAME_MATCH_END_LED);
		}
		/*break;
		}*/

		playExit=0;
	break;
// ===============================
	}
	
	if(repaintRequest) {playShow = true; repaintRequest = false;}	
	
}

// -------------------
// game Refresh :: Este metodo se llama tras regresar de un 'incomming call' y debemos restaurar el juego.
// ===================
/*
public void gameRefresh()
{
	switch (gameStatus)
	{
	case GAME_PLAY_TICK:		// GAME_PLAY_TICK => Estado del juego cuando esta en funcionamiento.
	break;
	}
}*/

// -------------------
// Draw : Llamamos a este metodo para renderizar en pantalla, el objeto 'scr' es nuestro GRAPHICS.
// ===================

/*public void Draw()
{

// --------------------------------
// Renderizamos PLAY Engine
// ================================

	if (biosStatus == BIOS_MENU && gameStatus == GAME_PLAY_TICK && menuListShow) {playShow = true;}

	if (playShow) { playShow=false; playDraw(); }

// ================================

}*/

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// play - Engine
// ===================
// *******************
// -------------------

int playExit;
boolean playShow, repaintRequest = false;

// -------------------
// play Create
// ===================
/*
public void playCreate()
{
   initResources();
}*/

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
}

// -------------------
// play Init
// ===================
/*
public void playInit()
{
     changesLeft = 3;
	 changesList = new boolean[AplayerNames.length];
     for(int i = 0; i < changesList.length; i++)
	 	changesList[i] = false;
     initMatch(myFormation, 0);
}*/

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
	//#ifndef NOCHEAT
	if (cheatActive && lastKeyMisc == 0)
	{
		if (intKeyMisc == 11) {goalsA++;}
		if (intKeyMisc == 12) {goalsB++;}		
		if (intKeyMisc == 10) {playExit = 1;}		
	}
	//#endif

    //#ifdef DebugTime    
    debug_logicmilis = System.currentTimeMillis();	
    //#endif
    matchTick();
    
    //#ifdef DebugTime    
    debug_logicmilis = System.currentTimeMillis() - debug_logicmilis;   	    
    Debug.println ("# debug_logicmilis: "+debug_logicmilis);
    //#endif
    
   int cx, cy;
      
   if(ball.theOwner == null) { cx = ball.x; cy = ball.y; }
   else { 
         cx = ball.x + (ball.cos(32*(ball.theOwner.direction - 2))>>6); 
         cy = ball.y + (ball.sin(32*(ball.theOwner.direction - 2))>>6);
         //#ifdef SMALLCANVAS
         if (matchState == constants.PENALTIES)
         {
        	 if (ball.y < HGROUNDHEIGHT)
        		 cy = GROUNDBORDER;
        	 else
        		 cy = GROUNDHEIGHT-canvasHeight+GROUNDBORDER+22;
         }
       	 //#endif
   }
      
   cameraX += (cx - cameraX)/4;
   cameraY += (cy - cameraY)/4;


	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}

// -------------------
// play Draw
// ===================

public void playDraw()
{
	switch(gameStatus)
	{
		/*case GAME_MENU_NEW_COMPETITION :
		canvasFillDraw(0xff0000);
		textDraw("Team "+playerTeam,0,canvasHeight/4,subOption == 0 ? 0xffffff : 0xaaaaaa,TEXT_OUTLINE | TEXT_HCENTER);
		textDraw((wholeLeague ? "Liga completa" : "Media liga"),0,2*canvasHeight/4,subOption == 1 ? 0xffffff : 0xaaaaaa,TEXT_OUTLINE | TEXT_HCENTER);
		textDraw("Tiempo de partido "+matchTime,0,3*canvasHeight/4,subOption == 2 ? 0xffffff : 0xaaaaaa,TEXT_OUTLINE | TEXT_HCENTER);
		break;*/
	
		case GAME_SPLASH_SCREEN+2:
		case GAME_SPLASH_SCREEN+1:
		case GAME_SPLASH_SCREEN:
			canvasFillDraw(0xffffff);
			//#ifdef LARGEUI
			textDraw(gameText[40][0]+gameText[40][1]+gameText[40][2],0,0,0x000000,TEXT_HCENTER | TEXT_VCENTER);
			//#elifdef MEDIUMUI
			//#elifdef SMALLUI
			//#endif		
		break;
		
		case GAME_PICTURE:
			if (menuImg!=null) {canvasFillDraw(0); showImage(menuImg);}
			//#ifdef DOUBLEDRAW
			if (menuImg!=null) {canvasFillDraw(0); showImage(menuImg);}
			//#endif
			//#ifdef J2ME
			scr.setClip(0,0,canvasWidth,canvasHeight);
			//#endif
			textDraw(gameText[38][0],0,
			//#ifdef LARGEUI
			8*canvasHeight/9,
			//#elifdef MEDIUMUI
			//#elifdef SMALLUI
			//#endif
			UI_FONT_RGB,TEXT_HCENTER);
		break;
			
		case GAME_MENU_CLASSIFICATION :
		
		//textDraw("Esta es la tabla de la clasificaci�n de la liga o el �rbol del torneo",0,0,0xffffff,TEXT_OUTLINE | TEXT_HCENTER | TEXT_VCENTER);				
		
		//#ifdef LARGEUI
		drawStandardBackGround(gameText[23][1],canvasHeight/6,5*canvasHeight/6,false,false);
		drawClassificationTable(UI_MENU_MARGIN,canvasHeight/6,canvasWidth - UI_MENU_MARGIN*2, 2*canvasHeight/3 );				
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		break;
		
		//#ifdef FORMATIONHELP
		//#endif
		case GAME_MENU_FORMATION :
		//#ifdef LARGEUI
		drawStandardBackGround(null,UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + 4,4*canvasHeight/5,false,false);
		drawFormationScreen(false);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif			
		break;
		
		//#ifdef FORMATIONHELP
		//#endif
		case GAME_MENU_INGAME_FORMATION :
		//#ifdef LARGEUI
		drawStandardBackGround(null,UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + 4,4*canvasHeight/5,false,false);			
		drawFormationScreen(true);
		/*putColor(UI_FONT_RGB);		
			//#ifdef J2ME
			scr.drawString(changesLeft+nullString,UI_MENU_MARGIN, UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + 8,20);
			//#elifdef DOJA
			scr.drawString(changesLeft+nullString,UI_MENU_MARGIN, UI_MENU_FRAMEMARGIN + UI_MENU_FRAMEWIDTH + 8);
			//#endif		
		*/
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif			
		break;
				
		case GAME_VERSUS_SCREEN :
		//canvasFillDraw(0xffff00);
		//textDraw("Jornada "+(compet.current+1)+" Team "+playerTeam+" VS Team "+cpuTeam,0,0,0xffffff,TEXT_OUTLINE | TEXT_HCENTER | TEXT_VCENTER);
		//#ifdef LARGEUI
		drawStandardBackGround(gameText[25][0]+(current+1),canvasHeight/4,3*canvasHeight/4,true,true);
		
		showTeamLogo(canvasWidth/4, canvasHeight/2 + menuListFontHeight/2, myTeam);
		showTeamLogo(3*canvasWidth/4, canvasHeight/2 + menuListFontHeight/2, cpuTeam);
		
		textDrawNoCut(teamNames[playerTeam],0,7*canvasHeight/20,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		textDrawNoCut(gameText[25][1],0,canvasHeight/2,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		textDrawNoCut(teamNames[cpuTeam],0,13*canvasHeight/20,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif		
		
		break;
				
		case GAME_PLAY_TICK :
            //#ifdef DebugTime 
            debug_drawmilis = System.currentTimeMillis();  
            //#endif
            if(biosStatus == BIOS_GAME){
            	matchDraw(); 
            	hudDraw();
            }
            //#ifdef DebugTime
            debug_drawmilis = System.currentTimeMillis() - debug_drawmilis;              
            Debug.println ("# debug_drawmilis: "+debug_drawmilis);
            //#endif
		break;
						
        case GAME_GOAL_LED:
        case GAME_PENALTY_LED:
        case GAME_HALF_MATCH_LED:
        case GAME_MATCH_END_LED:        
        	//#ifndef NOLED        	
        	if(ledFirstDraw) {
        		        		
        		matchDraw();        		
        	}
        	
        	if(ledLoop || ledCurrentFrame < ledEndFrame)
        	{
        		ledCurrentFrame++;
        		if(ledCurrentFrame > ledEndFrame) ledCurrentFrame = ledBeginFrame;
        	}        	
            drawLed(ledFirstDraw);
            
            if(ledFirstDraw) ledFirstDraw = false;
            //#else
            //#endif
        break;
        
		case GAME_MATCH_ENDED :
		/*canvasFillDraw(0xffff00);
		textDraw("Final de partido",0,canvasHeight/4,0xffffff,TEXT_OUTLINE | TEXT_HCENTER);		
		textDraw(" Team "+playerTeam+":"+goalsA+"   Team "+cpuTeam+":"+goalsB,0,canvasHeight/2,0xffffff,TEXT_OUTLINE | TEXT_HCENTER);		
		textDraw(goalsA < goalsB ? "Has perdido" : "�Tu ganas!",0,3*canvasHeight/4,0xffffff,TEXT_OUTLINE | TEXT_HCENTER);		
		*/
		
		//#ifdef LARGEUI
		drawStandardBackGround(gameText[5][0],canvasHeight/4,3*canvasHeight/4,true,false);
		textDrawNoCut(teamNames[playerTeam]+" "+goalsA,0,2*canvasHeight/5,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		textDrawNoCut(teamNames[cpuTeam]+" "+goalsB,0,canvasHeight/2,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		textDrawNoCut(goalsA == goalsB ? gameText[5][1] : (goalsA < goalsB ? gameText[5][3] : gameText[5][2]),0,3*canvasHeight/5,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		break;
		
		case GAME_MENU_NEW_COMPETITION :			
		case GAME_CREATE_COMPETITION :		
		case GAME_CONTINUE_COMPETITION :		
		case GAME_VERSUS_LOAD :		
		case GAME_PLAY :
		//System.out.println ("Print the bar");
		drawStandardBackGround(gameText[10][0],canvasHeight/4,3*canvasHeight/4,false,false);
		drawBox(UI_MENU_MARGIN,5*canvasHeight/12,canvasWidth - UI_MENU_MARGIN*2,canvasHeight/6,true);
		drawBox(UI_MENU_MARGIN*2,7*canvasHeight/16,canvasWidth - UI_MENU_MARGIN*4,canvasHeight/8,false);
		if(progressPercent >= 0){
		
			//#ifdef SMALLUI
			//#else
			drawBox(UI_MENU_MARGIN*2,7*canvasHeight/16,(progressPercent < 100 ? progressPercent : 100)*(canvasWidth - UI_MENU_MARGIN*4)/100,canvasHeight/8,true);
			//#endif
		}
		break;
		
		case GAME_LEAGUE_END :
		//canvasFillDraw(0xffff00);
		//textDraw("Jornada "+(compet.current+1)+" Team "+playerTeam+" VS Team "+cpuTeam,0,0,0xffffff,TEXT_OUTLINE | TEXT_HCENTER | TEXT_VCENTER);
		//#ifdef LARGEUI
		drawStandardBackGround(gameText[37][0],canvasHeight/4,3*canvasHeight/4,true,true);
				
		textDrawNoCut(teamNames[leagueRank[0][0]],0,7*canvasHeight/20,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		textDrawNoCut(gameText[37][1],0,canvasHeight/2,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);		
		textDrawNoCut(gameText[37][leagueRank[0][0] == playerTeam ? 2 : 3],0,13*canvasHeight/20,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
		//#elifdef MEDIUMUI
		//#elifdef SMALLUI
		//#endif
		
		break;
		
	}
   
   
	//canvasFillDraw(0x0000aa);
	
}

//#ifdef FORMATIONHELP
/*void drawListenerBar()
{
	putColor(UI_LIGHT_RGB);
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif
	int fontHeight = menuListFontHeight;
	scr.fillRect(0,canvasHeight-fontHeight-4,canvasWidth,fontHeight+4);
	textDrawNoCut(gameText[15][listenerIdLeft],2,-2,UI_FONT_RGB,TEXT_BOTTON | TEXT_LEFT);
	textDrawNoCut(gameText[15][listenerIdRight],-2,-2,UI_FONT_RGB,TEXT_BOTTON | TEXT_RIGHT);
}*/
//#endif

// <=- <=- <=- <=- <=-

void drawMenuBackground()
{
	switch(gameStatus)
	{
		case GAME_MENU_MAIN:
			switch(menuType){
				case MENU_MAIN:
				//#ifdef LARGEUI
					//#ifdef J2ME
					scr.setClip(0,0,canvasWidth,canvasHeight);
					//#endif
				drawArrow(canvasWidth - 8, canvasHeight - 4, 3);	
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				case MENU_OPTIONS:					
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[4][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				case MENU_SCROLL_ABOUT:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[7][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				case MENU_SCROLL_HELP:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[6][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				case MENU_EXIT_CONFIRM:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[9][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				case MENU_COMPETITION_TYPE:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[26][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				//#ifndef DOJA
				case MENU_NEW_COMPETITION_CONFIRM:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[41][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				
				//#endif
			}
		break;
		
		case GAME_PLAY_TICK:
			switch(menuType){
			
				case MENU_SECOND:		
					
					//#ifdef LARGEUI
					drawStandardBackGround(gameText[4][0],canvasHeight/5,4*canvasHeight/5,false,true);						
					//#elifdef MEDIUMUI
					//#elifdef SMALLUI
					//#endif	
					
				/*	
				//#ifdef LARGEUI
				drawListenerBar();						
				//#elifdef MEDIUMUI
				//#ifdef J2ME
				scr.setClip(0,0,canvasWidth,canvasHeight);
				//#endif
				putColor(UI_LIGHT_RGB);
				scr.fillRect(0,2*canvasHeight/3,canvasWidth,canvasHeight);
				listenerDraw();
				//#elifdef SMALLUI
				//#ifdef J2ME
				scr.setClip(0,0,canvasWidth,canvasHeight);
				//#endif
				putColor(UI_LIGHT_RGB);
				scr.fillRect(0,canvasHeight/2,canvasWidth,canvasHeight);
				listenerDraw();
				//#endif
				*/
				break;
				
				case MENU_SCROLL_HELP:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[6][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				
				case MENU_RESTART_CONFIRM:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[8][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
				
				case MENU_EXIT_CONFIRM:
				//#ifdef LARGEUI
				drawStandardBackGround(gameText[9][0],canvasHeight/5,4*canvasHeight/5,false,true);						
				//#elifdef MEDIUMUI
				//#elifdef SMALLUI
				//#endif
				break;
			}
		break;
				
		case GAME_MENU_CLASSIFICATION:
		case GAME_MENU_COMPETITION: 
			//#ifdef LARGEUI
			drawStandardBackGround(gameText[26][1]+(current+1),canvasHeight/5,4*canvasHeight/5,false,true);						
			//#elifdef MEDIUMUI
			//#elifdef SMALLUI
			//#endif
		break;
		case GAME_MENU_NEW_COMPETITION:
			//#ifdef LARGEUI
			drawStandardBackGround(gameText[26][0],canvasHeight/5,4*canvasHeight/5,false,true);						
			putColor(UI_DARK_RGB);
			scr.fillRect(menuListX,canvasHeight/5 + 2, menuListSizeX,canvasHeight/5 - 4);
			putColor(UI_PICTUREWINDOW_RGB);
			scr.fillRect(menuListX + 2,canvasHeight/5 + 4, menuListSizeX - 4,canvasHeight/5 - 8);
			showTeamLogo(menuListX + menuListSizeX/2, canvasHeight/5 + canvasHeight/10, playerTeam);
			//#elifdef MEDIUMUI
			//#elifdef SMALLUI
			//#endif			
		break;
		
		//#ifndef NOCUSTOMPLAYERS
		case GAME_MENU_EDIT_CUSTOM:		
			//#ifdef LARGEUI
			drawStandardBackGround(gameText[24][0],canvasHeight/5,4*canvasHeight/5,false,true);						
			//#elifdef MEDIUMUI
			//#elifdef SMALLUI
			//#endif
		break;
		//#endif
		
		case GAME_MENU_CHOOSE_SHIRT:		
			//#ifdef LARGEUI
			drawStandardBackGround(gameText[32][0],canvasHeight/5,4*canvasHeight/5,false,true);		
			putColor(UI_DARK_RGB);
			scr.fillRect(menuListX,canvasHeight/4, canvasWidth - menuListX*2, canvasHeight/6);
			putColor(UI_PICTUREWINDOW_RGB);
			scr.fillRect(menuListX + 2,canvasHeight/4 + 2, canvasWidth - menuListX*2 - 4, canvasHeight/6 - 4);			
			drawSampleShirt(canvasWidth/3,canvasHeight/3,AshirtsRGB[AhomeCostume ? 0 : 1]);
			drawSampleShirt(2*canvasWidth/3,canvasHeight/3,BshirtsRGB[BhomeCostume ? 0 : 1]);
			//#elifdef MEDIUMUI
			//#endif
			//#elifdef SMALLUI
			//#endif
		break;
	}	
}


//#ifndef NOLED

public static final int LED_LIGHT_RGB = 0xee8100;
public static final int LED_DARK_RGB = 0x643202;
public static final int LED_WIDTH = 45;
public static final int LED_HEIGHT = 30;

//#ifdef LARGEUI
public static final int LED_PIXEL_SIZE = 2;
public static final int LED_INTERPIXEL_SPACE = 1;
//#else
//#endif

public static final int LED_CELL_SIZE = LED_PIXEL_SIZE + LED_INTERPIXEL_SPACE;


int ledLastFrame = -1, ledBeginFrame, ledEndFrame, ledCurrentFrame;
boolean ledLoop, ledFirstDraw;

//#ifndef NOLEDFRAMEIMAGE
Image imgLedH, imgLedV;
//#endif


public void drawLed(boolean repaintAll)
{
	int x, y, offset;
	byte pos;
	boolean currentStatus, lastStatus = false;
			
	x = (canvasWidth - LED_CELL_SIZE*LED_WIDTH)/2;
	y = (canvasHeight - LED_CELL_SIZE*LED_HEIGHT)/2;
	
	if(repaintAll)
	{		
					
		//#ifdef NOLEDFRAMEIMAGE
		//#else
		
			//#ifdef LARGEUI			
			showImage(imgLedH,0,0,135,17,x,y - 17);
			showImage(imgLedH,0,17,145,10,x,y + LED_CELL_SIZE*LED_HEIGHT);
			
			showImage(imgLedV,0,0,18,117,x - 18, y - 17);
			showImage(imgLedV,18,0,18,117,x + LED_CELL_SIZE*LED_WIDTH, y - 17);			
			//#else
			//#endif
	
			//#ifdef J2ME
			scr.setClip(0,0,canvasWidth,canvasHeight);
			//#endif
			
		//#endif
		
		putColor(0x000000);
		
		scr.fillRect(x,y,LED_CELL_SIZE*LED_WIDTH,LED_CELL_SIZE*LED_HEIGHT);				
		
	}
	
	//System.out.println("LedLastFrame:"+ledLastFrame+" Led Current Frame:"+ledCurrentFrame+" repaintAll:"+repaintAll);
	
	for(int j = 0; j < LED_HEIGHT; j++)
	for(int i = 0; i < LED_WIDTH; i++)
	{
				
		// Last time status (remaining status of pixel)
		
		if(!repaintAll){
						
			if(ledLastFrame < 0) lastStatus = false;
			else{
									
				offset = LED_WIDTH*j + i + ledLastFrame*LED_WIDTH*LED_HEIGHT;
			
				if((offset/8)>=ledAnim.length) pos = 0;		
				else pos = ledAnim[offset/8];
		
				lastStatus = (((pos>>(7-(offset%8))) & 0x01) != 0);
			}
		}
			
		// Current status of pixel (How should it appear now)
		
		offset = LED_WIDTH*j + i + ledCurrentFrame*LED_WIDTH*LED_HEIGHT;
		
		if((offset/8)>=ledAnim.length) pos = 0;		
		else pos = ledAnim[offset/8];
	
		currentStatus = (((pos>>(7-(offset%8))) & 0x01) != 0);
				
		if(currentStatus != lastStatus || repaintAll)
		{
			putColor(currentStatus ? LED_LIGHT_RGB : LED_DARK_RGB);
			scr.fillRect(x + i*LED_CELL_SIZE, y + j*LED_CELL_SIZE, LED_PIXEL_SIZE, LED_PIXEL_SIZE);
		}
							
	}
	
	ledLastFrame = ledCurrentFrame;
}

//#endif


void drawSampleShirt(int x, int y, int rgb[])
{
	x -= 10; y -= 8;
	
	putColor(rgb[0]);	
	scr.fillRect(x,y,4,4);
	
	for(int i = 0; i < 9; i++)			
	{
		putColor(rgb[1 + i]);
		scr.fillRect(x + 4 + (i/3)*4,y + (i%3)*4,4,4);
	}
					
	putColor(rgb[11]);						
	scr.fillRect(x+4,y+13,12,4);
		
	putColor(rgb[10]);		
	scr.fillRect(x+16,y,4,4);
		
}




//#ifdef J2ME
//#ifdef NOKIAUI
// ----------------------------------------------------------

DirectGraphics dscr = null;

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
	
    if (X+DestX+SizeX < 0 ||
        Y+DestY+SizeY < 0 ||
        X+DestX > canvasWidth ||
        Y+DestY > canvasHeight        
    ) continue;
	
	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY,  Flip);
	}
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
    /*
	if ((DestX       > canvasWidth)
	||	(DestX+SizeX < 0)
	||	(DestY       > canvasHeight)
	||	(DestY+SizeY < 0))
	{return;}*/

	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
		scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
		break;

	case 1:
		dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
		break;

	case 2:
        dscr.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);    // Flip Y
		break;

	case 3:
        dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);   // Flip X+Y
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
//#endif






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
		
		gameSound = true;
		gameVibra = true;
		storedCompetition = false;
		
		//#ifndef NOCUSTOMPLAYERS				
		customPlayerNames = new String[CUSTOM_PLAYERS_NUMBER];		
		customPlayerData = new byte[CUSTOM_PLAYERS_NUMBER][7];
						
		for(int i = 0; i < CUSTOM_PLAYERS_NUMBER; i++)
		{			
			customPlayerNames[i] = gameText[24][1]+(i+1);

			for(int j = 0; j < 2; j++)
				customPlayerData[i][j] = (byte)(j < 2 ? 0 : 2);													
							
		}
		//#endif
		
		//#ifdef WEBSUBSCRIPTION
		//suscriptionRetry = 0;
		//#endif
		
		savePrefs();
		
		prefsData = updatePrefs(null);
				
		/*try{
			
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	    	DataOutputStream ostream = new DataOutputStream(bstream);				
															
			ostream.writeBoolean(true);
			ostream.writeBoolean(true);
			ostream.writeBoolean(false);
			
			//#ifndef NOCUSTOMPLAYERS
			for(int i = 0; i < CUSTOM_PLAYERS_NUMBER; i++)
			{			
				ostream.writeUTF(gameText[24][1]+(i+1));
				ostream.writeByte(0);
				ostream.writeByte(0);
				ostream.writeByte(2);
				ostream.writeByte(2);
				ostream.writeByte(2);
				ostream.writeByte(2);				
				ostream.writeByte(2);				
			}
			//#endif
						
			ostream.flush();
	    	ostream.close();
	    
	    	prefsData = bstream.toByteArray();

						    		    	
    	}catch (Exception e){ 	
    		//System.out.println ("Excepci�n en defaults!!!");
    	 }
    	*/

	}

// Actualizamos las variables del juego segun las prefs leidas / inicializadas

	try
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(prefsData,0,prefsData.length));
			
		gameSound = dis.readBoolean();
		gameVibra = dis.readBoolean();
		storedCompetition = dis.readBoolean();
				
		//#ifndef NOCUSTOMPLAYERS				
		customPlayerNames = new String[CUSTOM_PLAYERS_NUMBER];		
		customPlayerData = new byte[CUSTOM_PLAYERS_NUMBER][7];
						
		for(int i = 0; i < CUSTOM_PLAYERS_NUMBER; i++)
		{			
			customPlayerNames[i] = dis.readUTF();			
				
			for(int j = 0; j < 7; j++)
				customPlayerData[i][j] = dis.readByte();				
		}
		//#endif
		
		if(storedCompetition)
			competitionRead(dis);
		
		//#ifdef WEBSUBSCRIPTION 
		//suscriptionRetry = dis.readByte();
		//#endif
																			
	}catch (Exception e){ /*System.out.println ("Excepci�n en carga!!!");*/ }
		
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{

// Ponemos las varibles del juego a salvar como prefs.

	try
	{

		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	    DataOutputStream ostream = new DataOutputStream(bstream);				
															
		ostream.writeBoolean(gameSound);
		ostream.writeBoolean(gameVibra);
		ostream.writeBoolean(storedCompetition);
		
		//#ifndef NOCUSTOMPLAYERS
		for(int i = 0; i < CUSTOM_PLAYERS_NUMBER; i++)
		{			
			ostream.writeUTF(customPlayerNames[i]);
				
			for(int j = 0; j < 7; j++)
				ostream.writeByte(customPlayerData[i][j]);				
		}		
		//#endif
		
		if(storedCompetition)
			competitionWrite(ostream);
		
		//#ifdef WEBSUBSCRIPTION 
		//ostream.writeByte(suscriptionRetry);
		//#endif
							    
	    prefsData = bstream.toByteArray();
	    
	}catch (Exception e){  }
	
// Almacenamos las prefs

	updatePrefs(prefsData);		// Almacenamos byte[]
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

/*
 * Inicialitza un partit
 * @param formationA. Formaci� de l'equip A
 * @param formationA. Formaci� de l'equip B
 */
public void initMatch(int formationA) {
    
	int formationB = cpuTeam%6;
    teamGoal  = -1;
    pointer = HGROUNDWIDTH;
    firstPass = true;
    goalsA = 0;
    goalsB = 0;
    changesLeft = 3;
    matchState = constants.GAME;
    teamDown = TEAMA;   
    strikeTeam = teamA;    
    //oppAlignment = new byte [playerNames[oppTeam].length];
    
    
    //for(int i = 0; i<oppAlignment.length; i++)
    //   oppAlignment[i] = (byte)i;

    bestW = (160-24);
    bestE = (224+24);
    
    ball = new soccerPlayer(this, -1);//8,8, this);
    ball.activate(HGROUNDWIDTH, HGROUNDHEIGHT);
    
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
        
    
    assignPlayerData();
        
    
    for(int i = 0;i < 10;i++) {
                
        teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA, true);
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB, true);
        
    }
        
    gkA.activate(TEAMA);                       
    gkB.activate(TEAMB);
    
    sortedEntities[22] = ball;
    //#ifdef SLOW_DEVICE
    //#endif
    
    nfc = 0;
    firstPart = true;
    humanSoccerPlayer = null;
    
            
    ejected[TEAMA] = ejected[TEAMB] = 0;
    recalculateSpecialIds(teamA, TEAMA);
    recalculateSpecialIds(teamB, TEAMB);

    freeKick = false;
    
    //System.out.println ("TIEMPO:" + matchTime);
    switch(matchTime)
    {
        case 1: totalMatchTime = MEDIUMMATCHTIME;break;
        case 2: totalMatchTime = LONGMATCHTIME;break;
        default: totalMatchTime = SHORTMATCHTIME;break;
    }
        
    extraMatchTime = Math.abs(rnd.nextInt()%(totalMatchTime>>3));
}


/*
public void changeFormation(int _formation){
    
    changeFormation = true;
    newFormation = _formation;
    
}*/



public void halfMatch(int formationA)
{
	int formationB = nfc%6;
    teamGoal  = -1;
    firstPass = true;
    humanSoccerPlayer = null;
    ball.activate(HGROUNDWIDTH, HGROUNDHEIGHT);
    strikeTeam = teamB;
    teamDown = TEAMB;   
    //int side = (GROUNDWIDTH / 8);
    //int line = (GROUNDHEIGHT / 10);
    for(int i = 0;i < 10;i++)    
    {
        teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA, true);         
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB, true);         
    }
    gkA.activate(TEAMA);        
    gkB.activate(TEAMB);
    gkA.state = soccerPlayer.FREE;
    gkB.state = soccerPlayer.FREE;
    
    nfc = 0;
    matchState = constants.GAME;
    pointer = HGROUNDWIDTH;
    freeKick = false;
}  
  
  

  
static int nfc;  
int formation[][][];
final static int GROUNDWIDTH    = 300;
final static int GROUNDHEIGHT   = 440;
final static int HGROUNDWIDTH   = GROUNDWIDTH/2;
final static int HGROUNDHEIGHT  = GROUNDHEIGHT/2;

final static int AREAHEIGHT     = 90;
final static int AREAWIDTH      = 180;
final static int LITTLEAREAHEIGHT   = 35;
final static int LITTLEAREAWIDTH    = 90;
final static int GOALSIZE       = 62;
final static int GOALHEIGHT     = 32;
final static int GOALDEPTH = 7;
final static int HGOALSIZE      = GOALSIZE/2;

final static int NUMGRASSTYPES      = 4;
//#ifdef FASTMATCH
//#else
final static int SHORTMATCHTIME     = 2000;
final static int MEDIUMMATCHTIME    = 4000;
final static int LONGMATCHTIME      = 8000;
//#endif

final static int POINTERSPEED     = 4;
static int matchState;

int theTeam;
int wait;
soccerPlayer    theTeam2[];
static soccerPlayer    teamA[] = new soccerPlayer[11];
static soccerPlayer    teamB[] = new soccerPlayer[11];
static soccerPlayer    teams[][] = {teamA,teamB}; 
//soccerPlayer    dummy = new soccerPlayer(this,1,null);
soccerPlayer    gkA, gkB;
static soccerPlayer            ball;
static int             teamDown;    
static soccerPlayer    strikeTeam[], lastStrikeTeam[];

static Random rnd;
int             goalsA, goalsB;
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
static int            keyPresTime;
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
//String tmpFormation[] = {"4-4-2", "4-3-3", "3-5-2", "5-4-1", "3-4-3", "2-3-5"};
/*int sacadorCentro1 = 8;
int sacadorCentro2 = 9;
int sacadorCorner = 5;
int sacadorBanda = 2;*/
static int specialId[][] = {{0,0,0,0},{0,0,0,0}};
static int ejected[] = new int[2];
static int eject[] = {-1, -1};
static int displKickOut;
//#ifndef NOSKIP
boolean skip;
//#endif
int totalMatchTime;
int extraMatchTime;
boolean firstPart;
static int teamGoal;
//#ifdef DEBUG
long debug_logicmilis;
long debug_drawmilis;
//#endif
//#ifdef SLOW_DEVICE
//#endif


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

  final static int side = (GROUNDWIDTH / 8);
  final static int line = (GROUNDHEIGHT / 10);
  //#ifndef DOJA
  static long lastVibra; 
  //#endif
  

public void matchTick()
{
	//if (debug != null) System.out.println("debug 0:"+debug.state);
    for(int i = 0; i < 2;i++)
        if (eject[i] >= 0) 
        {        
            teams[i][eject[i]].state = soccerPlayer.EJECTED;                
            eject[i] = -1;
            recalculateSpecialIds(teams[i], i);
            ejected[i]++;
        } 
    if (halfMatch) {halfMatch(myFormation);halfMatch = false;}
    if (changeFormation)
    {
      
    
        for(int i = 0;i < 10;i++)
            teamA[i].activate((formation[newFormation][i][1]+1)*side, (formation[newFormation][i][0]+1)*line, TEAMA, false);                              
        changeFormation = false;
        //System.out.println("Formation:"+tmpFormation[newFormation]);
    }
    
    if (matchState == constants.GOAL || matchState == constants.ENDGAME) return;
    
    if (strikeTeam != null) lastStrikeTeam  = strikeTeam;
    
    //if (humanSoccerPlayer == null) keyPresTime = 0;
    
    if (matchState >= constants.WAITCORNER) 
    {
    	//System.out.println("wait "+wait);
        humanSoccerPlayer = null;
        wait--;
        //#ifndef NOSOUNDFX
        if (wait == 5) soundPlay(5,1);
        //#endif

        ball.updateBall();

        
        if (matchState == constants.WAITGOAL)
        {
             if (ball.x <= HGROUNDWIDTH - (HGOALSIZE) + GOALPOSTSIZE
             || ball.x > HGROUNDWIDTH + (HGOALSIZE) - GOALPOSTSIZE)             
             {
                ball.fx -= ball.sx;
                ball.sx = 0;      
             }
             if (ball.y < -GOALDEPTH) ball.y = -GOALDEPTH;
             if (ball.y > GROUNDHEIGHT+GOALDEPTH) ball.y = GROUNDHEIGHT+GOALDEPTH;
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
        if (matchState >= constants.WAITFAULT) {kickPlayer.animation();kickPlayer.timing++;}
        if (wait <= 0)
        {
            int tmp = matchState;
            matchState = preMatchState;
            //#ifndef DOJA
            GameCanvas.keyPresTime = 0;
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
                           setGameStatus(GAME_GOAL_LED);
                        //goal(theTeam);
                        break;       
                case constants.WAITFAULT:
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
                        setGameStatus(GAME_PENALTY_LED);
                        //penalty();
                        break;  
                 case constants.WAITGOALKICK:
                	  ball.h = 0;
                	  setTeamsState(soccerPlayer.FREE);    
                      kickPlayer.takeBallUnconditional();
                      break;
                 //#ifdef OFFSIDE
                 /*case constants.WAITOFFSIDE:
                	  kickPlayer.angleShot = 192-(kickPlayer.attackDirection*32);		
               	  	  matchState = constants.WAITFAULT;
               	  	  kickPlayer.fx = ax;
                	  kickPlayer.fy = ay;
                	  kickPlayer.x = ax>>8;
                	  kickPlayer.y = ay>>8;               	  	  
               	  	  break;*/
               	 //#endif
            }
        }
        return;
    }
    
    //#ifndef NOFAULTBUG
	if (freezeHumanPlayer > 0) 
	{
		freezeHumanPlayer--;
		if (freezeHumanPlayer == 0)
		{
			keyPresTime = 0;
		}
	}
	//#endif
	
    nfc++;    
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
                    }
                    else 
                        //GOL
                     {
                        //pregoal(teamGoal);
                        humanSoccerPlayer  = null;
                        preMatchState = matchState;
                        theTeam = teamGoal;
                        matchState = constants.WAITGOAL;
                        wait = 9;                           
                    }
            }   
            else
            {
                    //FUERA DE PORTERIA O CORNER
                    if (teamDown == teamComp) 
                    {
                        if (lastStrikeTeam == teamA)                            
                            precorner(teamB);        
                        else {
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
    
    if (matchState == constants.GAME && nfc > totalMatchTime+extraMatchTime) playExit = 1;
    
    //if (debug != null) System.out.println("debug n:"+debug.state);
}   






//#ifdef OFFSIDE
/*int ax, ay;

public void preoffside(int _tid)
{	
	preMatchState = matchState;
	matchState = constants.WAITOFFSIDE;
	theTeam2 = teams[_tid];
	kickPlayer = theTeam2[specialId[_tid][2]];
	ax = ball.fx;
	ay = ball.fy;
	//com.mygdx.mongojocs.sanfermines2006.GameCanvas.setTeamsState(FREE);
	//com.mygdx.mongojocs.sanfermines2006.GameCanvas.humanSoccerPlayer = null;
	wait = 20;    
	humanSoccerPlayer = null;
}*/
//#endif


//soccerPlayer marca = null;
//#ifdef STRATEGY
/*final static int NORMAL = 0;
final static int DEFENSIVE = 1;
final static int OFFENSIVE = 2;
final static int COUNTERATTACK = 3;
final static int CENTEROFFSET[] = {0, 30, -30, 0};*/
//#endif
//#ifdef DIFFICULTYLEVELS
/*final static int EASY = 0;
final static int NORMAL = 1;
final static int DIFFICULT = 2;
final static int INSANE = 3;*/
//#endif


//static soccerPlayer debug;

public void updatePlayers()
{
	//if (debug != null) System.out.println("debug 1:"+debug.state);
      //teamStrategy(); ////////////////////////////////////////        
	  int bally = ball.y;
	  if (ball.theOwner == gkA || ball.theOwner == gkB) bally = HGROUNDHEIGHT;
	    
        
        for(int i = 0; i < 2;i++) {
            
        	teamCenterX[i] = GROUNDWIDTH + ((ball.x - HGROUNDWIDTH) / 6);
        	
            if (teamDown == i) {
                
                if (teams[i] == lastStrikeTeam) {
                    
                    //
                    teamCenterY[i] = HGROUNDHEIGHT + bally;
                    teamAtt[i] = 140;
                    int factor = ((GROUNDHEIGHT-bally)*150) / GROUNDHEIGHT;
                    teamCenterY[i] = teamCenterY[i] - (250-factor*2);
                    
                } else {
                    
                    //teamCenterX[i] = GROUNDWIDTH + ((ball.x - HGROUNDWIDTH) >> 2);
                    teamCenterY[i] = HGROUNDHEIGHT + bally;
                    if (teamCenterY[i] > 620) teamCenterY[i] = 620;
                    int factor = (((GROUNDHEIGHT-bally)*150) / GROUNDHEIGHT);
                    teamCenterY[i] = -10 + teamCenterY[i] - (200-factor*2);
                    teamAtt[i] = 30+(factor);
                    
                }
                
            } else {
                
            	teamCenterX[i] = ((ball.x - HGROUNDWIDTH) /6);
            	
                if (teams[i] == lastStrikeTeam) {
                    
                    //teamCenterX[i] = ((ball.x - HGROUNDWIDTH) >> 2);
                    teamCenterY[i] = bally - HGROUNDHEIGHT;
                    teamAtt[i] = 140;
                    int factor = (bally*150 / GROUNDHEIGHT);
                    teamCenterY[i] = teamCenterY[i] + (250-factor*2);
                    
                } else {
                    
                    //teamCenterX[i] = ((ball.x - HGROUNDWIDTH) >> 2);
                    teamCenterY[i] = bally - HGROUNDHEIGHT;
                    if (teamCenterY[i] < -180) teamCenterY[i] = -180;
                    int factor = (bally*150 / GROUNDHEIGHT);
                    teamCenterY[i] = 10 + teamCenterY[i] + (200-factor*2);
                    teamAtt[i] = 30 + ((factor*7)/10);                                               
                    
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
        if (
        		//#ifndef NOFAULTBUG
        		GameCanvas.freezeHumanPlayer == 0 &&
        		//#endif
        		!holdPlayer &&  (
        		//#ifndef DOJA
        		teamA[i].state == soccerPlayer.OUT ||
        		//#endif
        		teamA[i].state == soccerPlayer.FOLLOW || teamA[i].state == soccerPlayer.OWNBALL || teamA[i].state == soccerPlayer.KICKBALL || teamA[i].state == soccerPlayer.PENALTY)) 
        {  
            humanSoccerPlayer = teamA[i];                                        
          
        }
        else 
            teamA[i].update();    
        //EKIPO B
        if (holdEnemies && teamB[i].state > 0)   {teamB[i].state = soccerPlayer.FREE;
        //if (debug != null) System.out.println("cambio a 0");
        }
        teamB[i].update();        
        if ((teamB[i].state == soccerPlayer.KICKBALL || holdPlayer )&& humanSoccerPlayer != null)
        {
        	holdEnemies = true;
        	holdPlayer = true;
        	//System.out.println("codigo inutil?");
            humanSoccerPlayer.state = soccerPlayer.FREE;
            humanSoccerPlayer.update();
            humanSoccerPlayer = null;             
        }
        //if (matchState >= constants.WAITFAULT)
    }
    //if (debug != null) System.out.println("debug 2:"+debug.state);
    //#ifndef NOSKIP
    skip = false;
    //#endif
    //np = -1;
            
    gkA.update(ball);
    gkB.update(ball);    
    
    if (humanSoccerPlayer != null) {    
        //#ifdef LATERALVIEW
        //humanSoccerPlayer.manualUpdate(keyY, keyX, keyMenu == 2);
        //#else
        humanSoccerPlayer.manualUpdate(keyX, keyY, keyMenu == 2);        
        //#endif        
    }
    
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
                              //teamA[i].state != soccerPlayer.GOPLACE 
                              //&& teamA[i].state != soccerPlayer.WAIT
                              teamA[i].state >= 0
                              ) teamA[i].state = soccerPlayer.FREE;
                        teamA[fPlayer].state = soccerPlayer.FOLLOW;
                        //System.out.println("FOLLOW ="+fPlayer+" tnp:"+np);
                    }
                    if (np >= 0)                    
                    {
                        teamA[fPlayer].state = soccerPlayer.FREE;
                        teamA[np].state = soccerPlayer.OWNBALL;
                        np = -1;
                    }
                }              
            }
            //if (debug != null) System.out.println("debug 3:"+debug.state);
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
            //if (debug != null) System.out.println("debug 4:"+debug.state);
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



/*
public void goal(int team)
{
    //GOL 
    humanSoccerPlayer = null;
    
    if (team == TEAMA)
    {
        goalsA++;
        strikeTeam = teamB;       
        ball.activate(HGROUNDWIDTH, HGROUNDHEIGHT);     
   }
   else
   {
        //GOL 
        goalsB++;
        strikeTeam = teamA;
        ball.activate(HGROUNDWIDTH, HGROUNDHEIGHT);            
   }
   setTeamsState(soccerPlayer.PREMATCH);
   gkA.state = soccerPlayer.FREE;
   gkB.state = soccerPlayer.FREE;       
   firstPass = true;
   //if (matchState == PENALTIES) nextPenalty();
   //if (matchState == GAME) matchState = GOAL;
   setGameStatus(GAME_GOAL_LED);
}*/


public void prefault(soccerPlayer[] _team, boolean penalty)
{    
    preMatchState = matchState;
    theTeam2 = _team;
     
      wait = 15;    
    if (penalty)
    {
        strikeTeam = theTeam2;
        matchState = constants.WAITPENALTY;
        //wait = 15;    
    }
    else
    {
        //humanSoccerPlayer = null;
        //preMatchState = matchState;
        //theTeam2 = _team;
        matchState = constants.WAITFAULT;
        //wait = 15;    
    }
    if (_team == teamB) humanSoccerPlayer = null;
}

/*
public void prepenalty(soccerPlayer[] _team)
{    
    preMatchState = matchState;
    theTeam2 = _team;
    strikeTeam = theTeam2;
    matchState = constants.WAITPENALTY;
    wait = 15;    
}

public void penalty()
{
    System.out.println("penalty");
    matchState = PENALTIES;
    //ball.sx = 0;
    //ball.sy = 0;
                        
    //nPenalty = 0;
    //restA = 5;
    //restB = 5;
    //int primens = (rnd.nextInt() & 256) ;
    //if (primens >= 128)
    //{
        strikeTeam = theTeam2;
        //strikeTeam = teamA;
        //teamDown = TEAMA;
    //}
    //else
    //{
        //strikeTeam = teamB;
        //teamDown = TEAMB;
    //}
    strikeTeam[nPP].formed = false;
    ball.activate(HGROUNDWIDTH, strikeTeam[nPP].relativeY(GROUNDHEIGHT - GROUNDHEIGHT/8));
    setTeamsState(soccerPlayer.PREPENALTY);
    gkA.state = soccerPlayer.PREPENALTY;
    gkB.state = soccerPlayer.PREPENALTY;
}
*/



public void precorner(soccerPlayer[] _team)
{    
    humanSoccerPlayer = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = constants.WAITCORNER;
    wait = 10;    
}

public void preout(soccerPlayer[] _team)
{
    humanSoccerPlayer  = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = constants.WAITOUT;
    wait = 10;    
}



/*
public void pregoal(int _team)
{
        humanSoccerPlayer  = null;
                        preMatchState = matchState;
                        theTeam = _team;
                        matchState = constants.WAITGOAL;
                        wait = 10;   
}
*/

/*
public void out(soccerPlayer[] outTeam)
{    
    humanSoccerPlayer = null;    
    outSide = ball.x < HGROUNDWIDTH?0:GROUNDWIDTH-1;
    if (outTeam == teamA) strikeTeam = teamB;           
    else strikeTeam = teamA;
    if (ball.y < 0) ball.y = 1;
    if (ball.y >= GROUNDHEIGHT)  ball.y = GROUNDHEIGHT-1;
    ball.activate(outSide , ball.y);                    
    setTeamsState(soccerPlayer.PREOUT);        
}


public void corner(soccerPlayer[] cornerTeam)
{    
    humanSoccerPlayer = null;    
    outSide = ball.x < HGROUNDWIDTH?0:GROUNDWIDTH-1;
    outSideV = ball.y < HGROUNDHEIGHT?0:GROUNDHEIGHT-1;
    ball.activate(outSide , outSideV);                    
    setTeamsState(soccerPlayer.PRECORNER);    
    strikeTeam = cornerTeam;    
}*/


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


/*
public static int distance(soccerPlayer a, soccerPlayer b)
{
    int dx = a.x - b.x;
    int dy = a.y - b.y;    
    int angle = soccerPlayer.atan(dy, dx);
    int deno = soccerPlayer.sin(angle);
    if (deno == 0) return Math.abs(dx);
    int d = ((dy << 10) / deno );       
    return Math.abs(d);
}


public static int angle(soccerPlayer a, soccerPlayer b)
{
    int dx = a.x - b.x;
    int dy = a.y - b.y;    
    int angle = ball.atan(dy, dx);
    //System.out.println("angle: " + angle);
    return (angle);    
}
*/

  
  


public void assignPlayerData() {
    
    int plId;   
    String[] namesTeamA = new String[11];
    String[] namesTeamB = new String[11];
    int[][] skillsTeamA = new int[11][7];    
    int[][] skillsTeamB = new int[11][7];    
    
    for(int i = 0; i < 11; i++) {
        
        plId = myAlignment[i];
        namesTeamA[i] = AplayerNames[plId];      
        namesTeamB[i] = BplayerNames[i];
    
        for(int j = 0; j<7; j++) {     
        
            skillsTeamA[i][j] = AplayerData[plId][j];            
            skillsTeamB[i][j] = BplayerData[i][j];
            //System.out.println(nullString+i+" "+j+": "+skillsTeamA[i][j]);
            
        }      
   }
      
       
   for(int i = 0;i < 10;i++) {    
   
      teamA[i].name = namesTeamA[i+1];
      teamA[i].setPlayerSkills(skillsTeamA[i+1]);
      teamB[i].name = namesTeamB[i+1];
      teamB[i].setPlayerSkills(skillsTeamB[i+1]);     
   }
   
   gkA.name = namesTeamA[0];
   gkA.setPlayerSkills(skillsTeamA[0]);
   
   gkB.name = namesTeamB[0];
   gkB.setPlayerSkills(skillsTeamB[0]);
   
   /*
   namesTeamA = null;
   skillsTeamA = null;
   namesTeamB = null;
   skillsTeamB = null;
      
   System.gc();          */
}



/**
 * team Stategy
 */   
/*
public void teamStrategy() {
    
    soccerPlayer[][] teams = new soccerPlayer[2][];
    teams[0] = teamA;
    teams[1] = teamB;
    int bally = ball.y;
    if (ball.theOwner == gkA || ball.theOwner == gkB) ball.y = HGROUNDHEIGHT;
    
    for(int i = 0; i < 2;i++) {
        
        if (teamDown == i) {
            
            if (teams[i] == lastStrikeTeam) {
                
                teamCenterX[i] = GROUNDWIDTH + ((ball.x - HGROUNDWIDTH) >> 2);
                teamCenterY[i] = HGROUNDHEIGHT + bally;
                teamAtt[i] = 140;
                int factor = ((GROUNDHEIGHT-bally)*150) / GROUNDHEIGHT;
                teamCenterY[i] = teamCenterY[i] - (250-factor*2);
                
            } else {
                
                teamCenterX[i] = GROUNDWIDTH + ((ball.x - HGROUNDWIDTH) >> 2);
                teamCenterY[i] = HGROUNDHEIGHT + bally;
                if (teamCenterY[i] > 620) teamCenterY[i] = 620;
                int factor = (((GROUNDHEIGHT-bally)*150) / GROUNDHEIGHT);
                teamCenterY[i] = -10 + teamCenterY[i] - (200-factor*2);
                teamAtt[i] = 30+(factor);
                
            }
            
        } else {
            
            if (teams[i] == lastStrikeTeam) {
                
                teamCenterX[i] = ((ball.x - HGROUNDWIDTH) >> 2);
                teamCenterY[i] = bally - HGROUNDHEIGHT;
                teamAtt[i] = 140;
                int factor = (bally*150 / GROUNDHEIGHT);
                teamCenterY[i] = teamCenterY[i] + (250-factor*2);
                
            } else {
                
                teamCenterX[i] = ((ball.x - HGROUNDWIDTH) >> 2);
                teamCenterY[i] = bally - HGROUNDHEIGHT;
                if (teamCenterY[i] < -180) teamCenterY[i] = -180;
                int factor = (bally*150 / GROUNDHEIGHT);
                teamCenterY[i] = 10 + teamCenterY[i] + (200-factor*2);
                teamAtt[i] = 30 + ((factor*7)/10);                                               
                
            }
        }   
            
    }
}
*/

//GFX
//final static int squareGrassWidth = 40;
final static int MIDCIRCLEDIAMETER = 73;
final static int BORDERSIZE = 20;
int camX, camY, cameraX, cameraY;
//#ifdef LATERALVIEW
//#else
    //#ifdef GFXBIG
    //#elifdef GFXMEDIUM
    final static int XScale = 6;
    final static int YScale = 6;    
    //#else
    //#endif
//#endif


Image pori, pord, grassImg;
//#ifdef J2ME
Image imgTeam[] = new Image[2];
Image imgMisc;
Image imgNumbers;
Image imgFlags;
Image bigFlagsImg;
//#ifdef CIRCLEIMG
//#endif
//#else
//#endif



byte [] playerCor, miscCor;
byte ledAnim[];
soccerPlayer sortedEntities[] = new soccerPlayer[23];
//#ifdef GFXBIG
//#elifdef GFXMEDIUM
final static int MISCPARTS = 1;
final static int PLAYERPARTS = 1;
final static int MISCWIDTH = 32;
final static int PLAYERWIDTH = 48;
final static int BALLRADIUS = 6;
final static int GROUNDBORDER = 32;//48
final static int POWERBARY = 36;
final static int POWERBARWIDTH = 22;
final static int POWERBARHEIGHT = 5;
final static int NUMBERDESPX = 17;
final static int NUMBERDESPY = 12;
final static int PLAYEROFFHEIGHT = 42;
//#ifdef CIRCLEIMG
//#endif
//#else
//#endif



int clockFrame;

public void hudDraw() {
    //#ifdef LOWSHIELDS
	// #else
	spriteDraw(imgMisc, 0, canvasHeight-(MISCWIDTH), 18, miscCor, MISCPARTS);
    spriteDraw(imgMisc, canvasWidth-MISCWIDTH, canvasHeight-(MISCWIDTH), 19, miscCor, MISCPARTS);    	
	//#endif
    
    if (nfc%10 == 0)
    {
        int percentTime = (nfc*100)/totalMatchTime;
        if (percentTime < 67) clockFrame = percentTime/17;
        else clockFrame = 4+((percentTime-67)/7);
    }

    spriteDraw(imgMisc, 0, 0, 48, miscCor,MISCPARTS);
    spriteDraw(imgMisc, 0, 0, 48+Math.min(clockFrame,8), miscCor,MISCPARTS);
    //#ifndef NODISPLAYPLAYER
    if (ball.theOwner != null)
        textDrawNoCut(ball.theOwner.name,0,0,UI_FONT_RGB,TEXT_HCENTER | TEXT_BOLD);
    //#endif    
        
    int gA = Math.min(goalsA, 19);
    int gB = Math.min(goalsB, 19);
    int exA = 0, exB = 0;
    
    //#ifdef GFXBIG
    //#elifdef GFXBIG
    //#else
        //#ifdef GFXMEDIUM
        final int exY = 9;
        final int CELWIDTH = 28;
        //#else
        //#endif
        if (goalsA > 9) {
            //#ifdef J2ME
            showImage(imgNumbers, 10, 0, 1, 7, NUMBERDESPX, exY+canvasHeight-MISCWIDTH+NUMBERDESPY);
            //#else
            //#endif
            gA-=10;
            exA = 2;
        }
        if (goalsB > 9) {
            //#ifdef J2ME
            showImage(imgNumbers, 10, 0, 1, 7, canvasWidth+(-NUMBERDESPX-6), exY+canvasHeight+(-MISCWIDTH+NUMBERDESPY));
            //#else
            //#endif
            gB-=10;
            exB = 2;
        }
        //#ifdef J2ME
        showImage(imgNumbers, (gA*6), 0, 6, 7, NUMBERDESPX+exA, exY+canvasHeight-MISCWIDTH+NUMBERDESPY);
        showImage(imgNumbers, (gB*6), 0, 6, 7, canvasWidth-NUMBERDESPX-6+exB, exY+canvasHeight-MISCWIDTH+NUMBERDESPY);
        //#else
        //#endif
    //#endif
    
        //#ifndef NOFLAGSINSCORE
        showTeamIcon(6, canvasHeight+(-CELWIDTH+6+8+6), playerTeam);
        showTeamIcon(canvasWidth-6, canvasHeight+(-CELWIDTH+6+8+6), cpuTeam);
        //#endif

  
}        



//#ifdef LATERALVIEW
//#else
///////////////////////////////////////////////////////////
// VISTA CENITAL
///////////////////////////////////////////////////////////

public int fx(int x) {
    //#ifdef GFXSMALL
    //#else
    return ((x*XScale)>>2) - camX;
    //#endif
    
}


public int fy(int y) {
    //#ifdef GFXSMALL
    //#else
    return ((y*YScale)>>2) - camY;
    //#endif
}


public int sx(int x) {
    //#ifdef GFXSMALL
    //#else
    return (x*XScale)>>2;
    //#endif
}


public int sy(int y) {
    //#ifdef GFXSMALL
    //#else
    return (y*YScale)>>2;
    //#endif
}


public void matchDraw() {
   
   
   
    /*if (nfc%2 == 0) putColor(0xff0000);
    else putColor(0x00ff00);
    scr.setClip(0,0,canvasWidth,canvasHeight);
    scr.fillRect(0,0,canvasWidth,canvasHeight);
    */
    // VISTA CENITAL
	    
    int px = -1;
    int py = 0;
            
    camX = sx(cameraX) - canvasWidth/2;
    camY = sy(cameraY) - canvasHeight/2;

    if(camX<-GROUNDBORDER) camX=-GROUNDBORDER;
    if(camY<-GROUNDBORDER) camY=-GROUNDBORDER;
   
    if(camX+canvasWidth > ((XScale*GROUNDWIDTH)>>2)+GROUNDBORDER) camX=((XScale*GROUNDWIDTH)>>2)+GROUNDBORDER-canvasWidth;
    if(camY+canvasHeight > ((YScale*GROUNDHEIGHT)>>2)+GROUNDBORDER) camY=((YScale*GROUNDHEIGHT)>>2)+GROUNDBORDER-canvasHeight;   
   
    //#ifdef DebugTime
    showfield_milis = System.currentTimeMillis();
    //#endif
    showField();    
    //#ifdef DebugTime
    showfield_milis = System.currentTimeMillis() - showfield_milis;
    //#endif
    
    //if (1 == 1) return; 
        
    if (humanSoccerPlayer != null)
    {                  
        //#ifdef GOALADVICE
         /*   if (humanSoccerPlayer.relativeY(humanSoccerPlayer.y) > GROUNDHEIGHT-(GROUNDHEIGHT/4) &&
                ball.theOwner == humanSoccerPlayer)
                {
                    //scr.setClip(0,0,canvasWidth,canvasHeight);
                    putColor(0xff0000+(nfc%6)*0x000050);                    
                    	scr.drawLine(fx(humanSoccerPlayer.x), fy(humanSoccerPlayer.y), fx(HGROUNDWIDTH-HGOALSIZE), fy(humanSoccerPlayer.relativeY(GROUNDHEIGHT)));
                    	scr.drawLine(fx(humanSoccerPlayer.x), fy(humanSoccerPlayer.y), fx(HGROUNDWIDTH+HGOALSIZE), fy(humanSoccerPlayer.relativeY(GROUNDHEIGHT)));
                }*/
        //#endif
    
        
        soccerPlayer pass = humanSoccerPlayer.destPass;
        if (pass != null) 
        {
            int x = fx(pass.x);
            //#ifndef DOJA
            int y = fy(pass.y)-(soccerPlayer.sin(nfc<<6)>>8)-(PLAYEROFFHEIGHT-(MISCWIDTH/2));
            //#else
            //#endif
            if (x <= 0 || x >= canvasWidth || y <= 0 || y >= canvasHeight)
            {
                    x = Math.min(Math.max(x,5),canvasWidth-5);
                    y = Math.min(Math.max(y,5),canvasHeight-5);                        
            }
            px = x-(MISCWIDTH/2);
            py = y-(MISCWIDTH/2);                
        }
         
        int x = fx(humanSoccerPlayer.x)-(MISCWIDTH/2);
        int y = fy(humanSoccerPlayer.y)-(MISCWIDTH/2);
        //#ifdef J2ME
        //scr.setClip(0, 0, canvasWidth, canvasHeight);
        //#endif
        //scr.setColor(0xffff00);
        //scr.drawArc(x, y, sx(22), sy(22), 0, 360);        
        //int frame = nfc%6;        
        spriteDraw(imgMisc, x, y, nfc%6, miscCor, MISCPARTS);                        
        
        //scr.setColor(0xffff00);  
        //#ifdef J2ME
        //scr.setClip(0,0, canvasWidth, canvasHeight);
        //scr.fillArc(fx(pointer-3), fy(GROUNDHEIGHT), sx(6), sy(6), 0, 360); 
        //scr.fillArc(fx(pointer-3), fy(-6), sx(6), sy(6), 0, 360);  
        //#endif    
    }
    
   
   //#ifndef NOHEIGHT
    if (keyPresTime != 0 && ball.theOwner != null && ball.theOwner.team == TEAMA)
    {
        soccerPlayer fp = humanSoccerPlayer;
        if (ball.theOwner == gkA || humanSoccerPlayer == null) fp = gkA;        
        putColor(0xffffff);
        scr.drawRect(fx(fp.x)-(POWERBARWIDTH/2), fy(fp.y)-POWERBARY, POWERBARWIDTH, POWERBARHEIGHT+1);
        putColor(0xff8000);
        int leng = Math.min(nfc-keyPresTime, 6);//(int)Math.min((System.currentTimeMillis() - keyPresTime), 500);
        leng = (leng*POWERBARWIDTH)/6;//500;
        if (leng == POWERBARWIDTH) putColor(0xffff00);
        //#ifdef VI-TSM100
        //#else
        scr.fillRect(fx(fp.x)-((POWERBARWIDTH/2)-1), fy(fp.y)-POWERBARY+1, leng, POWERBARHEIGHT);
        //#endif
        
    }                
   //#endif
                   
    //#ifdef DebugTime
    drawplayer_milis = System.currentTimeMillis();
    //#endif
     
    //sortEntities();
                                            
            //#ifdef SLOW_DEVICE
            //#else
            
            boolean repeat;
            int intentos = 0;
                
            do{
                
                
                intentos++;
                repeat = false;
                for(int i = 0; i < 22;i++)
                {
                    //comps++;
                    //#ifdef LATERALVIEW
                    //#else
                    if (sortedEntities[i+1].y < sortedEntities[i].y) 
                    //#endif
                    {
                        repeat = true;
                        soccerPlayer t = sortedEntities[i+1];
                        sortedEntities[i+1] = sortedEntities[i];
                        sortedEntities[i] = t;
                        //break;
                    }
                }      
            }while(repeat && intentos < 2);
            //#endif                       
    
    //end sortentities()
    
    
    
    
        
    
    for(int i = 0; i < 23;i++) {
        
        if (sortedEntities[i].id >= 0) {
            
            drawPlayer(sortedEntities[i]);            
            
        } else
        	//#ifndef DOJA
        	if (ball.h>>8 <= GOALHEIGHT)
        	//#endif
        {
        		
        	
            //#ifdef GFXSMALL
            //#else
            int desf = ((ball.h >> 9)*XScale)>>2;
            //#endif
            int frame = Math.min(desf / 12, 3);
            
            spriteDraw(imgMisc, fx(ball.x)-(MISCWIDTH/2), fy(ball.y)-(MISCWIDTH/2)-desf , frame+6, miscCor, MISCPARTS);
            //#ifndef DOJA
            //PORTERIA ARRIBA
        	showImage(pord, fx(HGROUNDWIDTH)-pord.getWidth()/2, fy(0)-pord.getHeight());
        	//#endif
            
        }
    }
    //#ifdef DebugTime
    drawplayer_milis = System.currentTimeMillis()-drawplayer_milis;
    //#endif
    
    if (px != -1) spriteDraw(imgMisc, px, py, 30+(nfc%6), miscCor, MISCPARTS);
    
    
   	//PORTERIA ABAJO
    showImage(pori, fx(HGROUNDWIDTH)-pori.getWidth()/2, fy(GROUNDHEIGHT)-pori.getHeight()/2);   
         
     
    //DETECTAR
    //#ifndef DOJA
    if (ball.h>>8 > GOALHEIGHT || (ball.y > GROUNDHEIGHT && matchState != constants.WAITGOAL))
   
    {
        //#ifdef GFXSMALL
        //#else
        int desf = ((ball.h >> 9)*XScale)>>2;
        //#endif
        int frame = Math.min(desf / 12, 3);
        
        spriteDraw(imgMisc, fx(ball.x)-(MISCWIDTH/2), fy(ball.y)-(MISCWIDTH/2)-desf , frame+6, miscCor, MISCPARTS);

    }
    
    	// PORTERIA ARRIBA
    	//showImage(pord, fx(HGROUNDWIDTH)-pord.getWidth()/2, fy(0)-pord.getHeight());
    //#endif
    
    //POINTER     
    if (teamA[0].relativeY(ball.y) > (GROUNDHEIGHT-(GROUNDHEIGHT/4)))
    {
            /*if (humanSoccerPlayer != null && ball.theOwner == humanSoccerPlayer)
            {
                scr.setClip(0,0,canvasWidth,canvasHeight);
                scr.setColor(0xff0000);
                scr.drawLine(fx(humanSoccerPlayer.x), fy(humanSoccerPlayer.y), fx(HGROUNDWIDTH-HGOALSIZE), fy(humanSoccerPlayer.relativeY(GROUNDHEIGHT)));
                scr.drawLine(fx(humanSoccerPlayer.x), fy(humanSoccerPlayer.y), fx(HGROUNDWIDTH+HGOALSIZE), fy(humanSoccerPlayer.relativeY(GROUNDHEIGHT)));
            }*/
    		if (teamDown == TEAMA)
    			spriteDraw(imgMisc, fx(pointer)-(MISCWIDTH/2), Math.max(5,fy(0))-(MISCWIDTH/2), (6*6)+(nfc%6), miscCor, MISCPARTS);
    		else
    			spriteDraw(imgMisc, fx(pointer)-(MISCWIDTH/2), Math.min(canvasHeight-5,fy(GROUNDHEIGHT))-(MISCWIDTH/2), (6*6)+(1*6)+(nfc%6), miscCor, MISCPARTS);    
    }
    
    //#ifdef DebugTime
    Debug.println("SF: "+showfield_milis+" VS DP:"+drawplayer_milis);
    //#endif
}


    

public void showField()
{
    // VISTA CENITAL
    
    
    //#ifdef GFXSMALL
    //#else
    int fx0 = fx(0);
    int fy0 = fy(0);
    int fx1 = fx(GROUNDWIDTH);
    int fxh = fx(GROUNDWIDTH/2);
    int fy1 = fy(GROUNDHEIGHT);
    //#endif    
    
    /*
    //#ifdef J2ME
    scr.setColor(92,153,0);
    //#elifdef DOJA
    scr.setColor(0x00ff00);
    //#endif
    scr.fillRect(0,0,canvasWidth,canvasHeight);*/
    
    
        
    int x, y;
    
    x = (64+camX)%64;
    
    //#ifdef GFXSMALL
    //#elifdef GFXMEDIUM
    y = (128+camY)%128;
    //#else
    //#endif

    int f = 60+(nfc&0x3);
  
    //System.out.println("camY:" + camY+ "    y"+y);
  
    //#ifndef NOGRASSIMG  
        //#ifdef GFXBIG
        //#else
        for(int i = 0; i<((canvasWidth+64)/192)+1; i++)
        for(int j = 0; j<(canvasHeight/64)+2; j++)      
        //#endif        
            
            showImage(grassImg,0,0,192,64,(192*i)-x%64,(64*j)-y%64);                
    //#else
    //#endif
    
    
    
    
    
    //#ifdef J2ME  
    //scr.setClip(0, 0, canvasWidth, canvasHeight);
    //#endif
   
    //SOMBRAS    
    
    //#ifndef NOSHADOWS
    for(int i = 0; i < 11;i++) {
        spriteDraw(imgMisc, fx(teamA[i].x)-(MISCWIDTH/2), fy(teamA[i].y)-(MISCWIDTH/2),17, miscCor, MISCPARTS);
        spriteDraw(imgMisc, fx(teamB[i].x)-(MISCWIDTH/2), fy(teamB[i].y)-(MISCWIDTH/2),17, miscCor, MISCPARTS);
        //scr.fillArc(fx(teamA[i].x)-5, fy(teamA[i].y)-3, 10, 6, 0,360);       
        //scr.fillArc(fx(teamB[i].x)-5, fy(teamB[i].y)-3, 10, 6, 0,360);               
    }
    //#endif
    spriteDraw(imgMisc, fx(ball.x)-(MISCWIDTH/2), fy(ball.y)-(MISCWIDTH/2)+BALLRADIUS,12+Math.min(ball.h>>12,3), miscCor, MISCPARTS);
    
    //scr.setColor(0x404040);
    //#ifdef J2ME
    //scr.fillArc(fx(ball.x)-5, fy(ball.y)-3, 10, 6, 0,360); 
    //#endif
    
   
   
      
    putColor(0xa2c77a);
    //BORDE
    scr.drawRect(fx0, fy0, sx(GROUNDWIDTH), sy(GROUNDHEIGHT));      
    
    
    
    int yc = camY;
    
    
    //AREA ARRIBA
    if (yc < sy(AREAHEIGHT+(MIDCIRCLEDIAMETER/2))) {
                               
        scr.drawRect( fx((GROUNDWIDTH/2)-(AREAWIDTH/2)), fy0, sx(AREAWIDTH), sy(AREAHEIGHT)); 
        scr.drawRect( fx((GROUNDWIDTH/2)-(LITTLEAREAWIDTH/2)), fy0, sx(LITTLEAREAWIDTH), sy(LITTLEAREAHEIGHT));   
        //#ifndef NOAREACIRCLES                
                scr.setClip(0,-camY+sy(AREAHEIGHT),canvasWidth, MIDCIRCLEDIAMETER);                
                scr.drawArc(fx((GROUNDWIDTH/2)-(MIDCIRCLEDIAMETER/2)), fy(AREAHEIGHT-((MIDCIRCLEDIAMETER*2)/3)), sx(MIDCIRCLEDIAMETER), sy(MIDCIRCLEDIAMETER), 0, 360);
                scr.setClip(0,0,canvasWidth,canvasHeight);              
        //#endif

        
        //CIRCULO PENALTY 
        spriteDraw(imgMisc, fxh-(MISCWIDTH/2), fy(GROUNDHEIGHT/8)-(MISCWIDTH/2), 21, miscCor, MISCPARTS);
            
        //CORNERS    
        spriteDraw(imgMisc, fx1-(MISCWIDTH), fy0, 22, miscCor, MISCPARTS);
        spriteDraw(imgMisc, fx0, fy0, 23, miscCor, MISCPARTS);
        //PALOS
        spriteDraw(imgMisc, fx0-(MISCWIDTH/2), fy0-(MISCWIDTH/2), f, miscCor, MISCPARTS);
        spriteDraw(imgMisc, fx1-((MISCWIDTH/2)+1), fy0-(MISCWIDTH/2), f+6, miscCor, MISCPARTS);                
        //System.out.println ("corners1");
        //#ifdef CIRCLEIMG
        //#endif
    
        //PORTERIA ARRIBA
        showImage(pord, fxh-pord.getWidth()/2, fy0-pord.getHeight());                       
        
    }
    //else if (yc + ((canvasHeight<<2)/YScale) > GROUNDHEIGHT - AREAHEIGHT - MIDCIRCLEDIAMETER) {
    //AREA ABAJO
    else if (yc + sy(canvasHeight) > sy(GROUNDHEIGHT - AREAHEIGHT - (MIDCIRCLEDIAMETER/2))) {
        
        //System.out.println ("corners2");
        scr.drawRect( fx((GROUNDWIDTH/2)-(AREAWIDTH/2)), fy(GROUNDHEIGHT-AREAHEIGHT),sx(AREAWIDTH), sy(AREAHEIGHT));
        scr.drawRect( fx((GROUNDWIDTH/2)-(LITTLEAREAWIDTH/2)), fy(GROUNDHEIGHT-LITTLEAREAHEIGHT), sx(LITTLEAREAWIDTH), sy(LITTLEAREAHEIGHT));
        
        //#ifndef NOAREACIRCLES
                scr.setClip(0,-camY+sy(GROUNDHEIGHT-AREAHEIGHT-MIDCIRCLEDIAMETER),canvasWidth, sy(MIDCIRCLEDIAMETER));
                scr.drawArc(fx((GROUNDWIDTH/2)-(MIDCIRCLEDIAMETER/2)), fy(GROUNDHEIGHT-AREAHEIGHT-((MIDCIRCLEDIAMETER*1)/3)), sx(MIDCIRCLEDIAMETER), sy(MIDCIRCLEDIAMETER), 0, 360);
                scr.setClip(0,0,canvasWidth,canvasHeight);              
        //#endif
        
                
        //CIRCULO PENALTY 
        spriteDraw(imgMisc, fxh-(MISCWIDTH/2), fy(GROUNDHEIGHT-(GROUNDHEIGHT/8))-(MISCWIDTH/2), 21, miscCor, MISCPARTS);
        //CORNERS    
        spriteDraw(imgMisc, fx0, fy1-(MISCWIDTH), 29, miscCor, MISCPARTS);
        spriteDraw(imgMisc, fx1-(MISCWIDTH), fy1-(MISCWIDTH), 28, miscCor, MISCPARTS);    
        //PALOS
        spriteDraw(imgMisc, fx1-((MISCWIDTH/2)+1), fy1-(MISCWIDTH/2), f+6, miscCor, MISCPARTS);        
        spriteDraw(imgMisc, fx0-(MISCWIDTH/2), fy1-(MISCWIDTH/2), f, miscCor, MISCPARTS);        
        //#ifdef CIRCLEIMG
        //#endif
        
    }
    //else System.out.println ("corners0");
    //if (yc + ((canvasHeight<<2)/YScale) > GROUNDHEIGHT - AREAHEIGHT) 
    //if (yc + ((canvasHeight<<2)/YScale) > GROUNDHEIGHT - AREAHEIGHT) 
    {
    
        //#ifdef J2ME  
        scr.setClip(0, 0, canvasWidth, canvasHeight);
        //#endif    
        
        //MEDIO CAMPO
        scr.drawLine( fx0 ,fy(HGROUNDHEIGHT), fx1, fy(HGROUNDHEIGHT));   
    
        //CICULO CENTRAL        
        //#ifndef CIRCLEIMG
        scr.drawArc(fx((GROUNDWIDTH/2)-(MIDCIRCLEDIAMETER/2)), fy((GROUNDHEIGHT/2)-(MIDCIRCLEDIAMETER/2)), sx(MIDCIRCLEDIAMETER), sy(MIDCIRCLEDIAMETER), 0, 360);
        //#else
        //#endif
        spriteDraw(imgMisc, fxh-(MISCWIDTH/2), fy(HGROUNDHEIGHT)-(MISCWIDTH/2), 21, miscCor, MISCPARTS);
    }
    //#ifdef J2ME    
    //scr.fillArc(fx((HGROUNDWIDTH)-(2)), fy((GROUNDHEIGHT/8)-(2)), sx(4), sy(4), 0, 360);
    //scr.fillArc(fx((HGROUNDWIDTH)-(2)), fy(GROUNDHEIGHT-(GROUNDHEIGHT/8)-(2)), sx(4), sy(4), 0, 360);
    //CICULO CENTRAL
    //scr.fillArc(fx((GROUNDWIDTH/2)-(2)), fy((GROUNDHEIGHT/2)-(2)), sx(4), sy(4), 0, 360);
    
    //#endif
                       
    

}    

//#ifdef J2ME
public void drawPlayer(soccerPlayer sp) {
//#else
//#endif

    //CENITAL        
    
    int desf = ((sp.h >> 9)*XScale)>>2;
    //#ifdef GFXSMALL
    //#else
    int x = fx(sp.x);    
    int y = fy(sp.y)-desf+BALLRADIUS;    
    //#endif
    if (x < -(PLAYERWIDTH/2) || x > canvasWidth + (PLAYERWIDTH/2) || y < 0 || y > canvasHeight+PLAYERWIDTH) return;
    
    
    //#ifdef GFXLITE
    //#else
    int frame = sp.animFrame + (sp.animSeq*8);    
    //#endif
    
    spriteDraw(imgTeam[sp.team], x-(PLAYERWIDTH/2), y-PLAYERWIDTH , frame, playerCor, PLAYERPARTS);        
    //#ifdef SKINNING
    spriteDraw(imgSkin[sp.playerSkin], x-(PLAYERWIDTH/2), y-PLAYERWIDTH , frame, skinCor[sp.playerSkin], PLAYERPARTS);        
    spriteDraw(imgHair[sp.playerHead], x-(PLAYERWIDTH/2), y-PLAYERWIDTH , frame, hairCor[sp.playerHead], PLAYERPARTS);        
    //#endif
    
    //FALTA/SAKEPUERTA
    if (sp.freeKick) {
        //#ifdef J2ME
        int val = (nfc%4)*3;
        //#else
        //#endif
        
        //System.out.println("SP: "+sp.state+"("+nfc+")");
        //#ifdef J2ME
        for(int i = 1; i < 9;i++) {
        //#else
        //#endif    
            	//#ifdef J2ME
        			//#ifdef FKLINES
        			//#else
                	scr.setColor(0xff,0x02*val,0x30);                
                	int error = 45-(sp.playerFKick*4);
                	scr.drawArc(fx(sp.x-val), fy(sp.y-val), sx(val*2), sy(val*2),  -(((sp.angleShot+(error/2))*360)/256), (error*360)/256 );
                	//#endif
                //#else
                //#endif
                val += 4+i+i;
        }                             
    }

    if (sp.numFaults >= 2) {
        //#ifdef DOJA
        //#else                 
        if (sp.numFaults >= 3)
            frame = 10;
        else
            frame = 11;
        //#endif
        spriteDraw(imgMisc, fx(sp.x)-(MISCWIDTH/2), fy(sp.y)-desf+(-PLAYEROFFHEIGHT) , frame, miscCor, MISCPARTS);
    }
      
    
    
}


//#endif


/*  
public void sortEntities()
{
    boolean repeat;
    int intentos = 0;
    //int comps = 0;
    //nums++;
    
    //#ifdef SLOW_DEVICE
        //#ifdef LATERALVIEW
        
        //#else
        if (ball.theOwner != null) {        
            if (ball.y < ball.theOwner.y)
            {
                sortedEntities[idBall] = sortedEntities[0];
                sortedEntities[0] = ball;
                idBall = 0; 
            }
            else
            {
                sortedEntities[idBall] = sortedEntities[22];
                sortedEntities[22] = ball;
                idBall = 22; 
            }
        }
        //#endif
    //#else
    do{
    
        intentos++;
        repeat = false;
        for(int i = 0; i < 22;i++)
        {
            //comps++;
            //#ifdef LATERALVIEW
            if (sortedEntities[i+1].x < sortedEntities[i].x) 
            //#else
            if (sortedEntities[i+1].y < sortedEntities[i].y) 
            //#endif
            {
                repeat = true;
                unit t = sortedEntities[i+1];
                sortedEntities[i+1] = sortedEntities[i];
                sortedEntities[i] = t;
                //break;
            }
        }      
    }while(repeat && intentos < 2);
    //#endif
    
    //suma += comps;
    //System.out.println("media :"+((suma)/nums));
}
*/
 
  
//#ifdef SKINNING
Image imgSkin[] = new Image[2];
Image imgHair[] = new Image[5];
byte skinCor[][] = new byte[2][];
byte hairCor[][] = new byte[5][];
//#endif


//#ifdef INITIALLOAD
//#else
public void initResources()
{
	progressBarInit();
    
    soccerPlayer.initTables(this);       
    
    rnd = new Random(System.currentTimeMillis());
    
    progressBarStep(20);
    
    //#ifdef SKINNING
    for(int i = 0; i < 5;i++) {
        hairCor[i] = loadFile("/hair"+i+".cor");
        imgHair[i] = loadImage("/hair"+i);
    }
    
    for(int i = 0; i < 2;i++) {
        skinCor[i] = loadFile("/skin"+i+".cor");
        imgSkin[i] = loadImage("/skin"+i);
    }
    //#endif
    
    
    //GFX        
    playerCor = loadFile("/plant.cor");
    //#ifdef LATERALVIEW
    //#else
    pori = loadImage("/porteria_abajo");
    pord = loadImage("/porteria_arriba");
    //#ifndef NOGRASSIMG
    grassImg = loadImage("/grass");
    //int grass = Math.abs(rnd.nextInt())%NUMGRASSTYPES;
    //grassImg = loadImage("/cesped"+(grass+1));
    
    //#endif
    //#endif
    
    progressBarStep(20);
          
    //#ifdef J2ME
    imgMisc = loadImage("/misc");
    //#else
    //#endif
    miscCor = loadFile("/misc.cor");
    
    progressBarStep(20);
    
    //#ifdef J2ME
    byte inbuf[] = loadFile("/plant.png");        
    imgTeam[0] = changePal(inbuf, AshirtsRGB[AhomeCostume ? 0 : 1]);
    inbuf = null;   
    progressBarStep(20);
    //#else
    //#endif
    
    
    //#ifdef J2ME
    inbuf = loadFile("/plant.png");
    
    imgTeam[1] = changePal(inbuf, BshirtsRGB[BhomeCostume ? 0 : 1]);
                        
    inbuf = null;   
    progressBarStep(20);
    //#endif    
       
    //#ifndef NOLED
    ledAnim = loadFile("/led.raw");
    //#endif

    //#ifdef J2ME
    imgNumbers = loadImage("/numbers");
    //#else
    //#endif
    
    //#ifdef J2ME
    //#ifndef NOLEDFRAMEIMAGE
    imgLedH = loadImage("/ledh");
    imgLedV = loadImage("/ledv");
    //#endif
    //#endif
    
    //#ifdef CIRCLEIMG
    //#endif    
}
//#endif

/*
int colorA[] = {0xff0000, 0x0000c0, 0x0000c0, 0x0000c0, 
                0xff0000, 0xff0000, 0xff0000, 0x0000c0, 
                0x0000c0, 0x0000c0, 0xff0000, 0x2020f0};

int colorB[] = {0xEE2223, 0xEE2223, 0xEE2223, 0xEE2223, 
                0xEE2223, 0xEE2223, 0xEE2223, 0xEE2223, 
                0xEE2223, 0xEE2223, 0xEE2223, 0xffffff};


int colorB[] = {0x008f00, 0x000000, 0xffffff, 0x000000, 
                0xffffff, 0x000000, 0xffffff, 0x000000, 
                0xffffff, 0x000000, 0x008f00, 0x008f00};
*/              

byte rgbs[][] = {{(byte)102,(byte)45,(byte)145}, //A
                 {(byte)237,(byte)28,(byte)36},  //B
                 {(byte)168,(byte)100,(byte)168},  //C
                 {(byte)0,(byte)255,(byte)255},  //D
                 
                 {(byte)0,(byte)255,(byte)0}, //E
                 {(byte)117,(byte)76,(byte)36}, //F
                 {(byte)255,(byte)255,(byte)255},  //G
                 {(byte)0,(byte)0,(byte)255},  //H
                 
                 {(byte)247,(byte)148,(byte)29},  //I
                 {(byte)0,(byte)88,(byte)38},  //J
                 {(byte)236,(byte)0,(byte)140},  //K
                 {(byte)255,(byte)242,(byte)0}};  //L
                
//#ifdef J2ME
Image changePal(byte inbuf[], int shirtCol[]) {
    
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
   
 /* Return the CRC of the bytes buf[0..len-1]. */
 int crc(byte buf[], int off, int len)
 {
   return update_crc(0xffffffff, buf, off, len) ^ 0xffffffff;
 }


   
int crc_table[] = new int[256];
   
   
boolean crc_table_computed = false;
   
/* Make the table for a fast CRC. */
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
//#else
//#endif


// =========================================================================================================
// Competition stuff
// =========================================================================================================


//public class Competition
//{
	
	public static final int LEAGUE = 0;
	public static final int TOURNAMENT = 1;
	
	public int current, teamsNumber, duration, type, myFormation;
			 
	// League Data		 
	byte[][][] league; 		
	byte[] myAlignment = new byte[40];
	byte myTeam, headLineTeam, domHeadLineTeam; 	
	//leagueRank[teamId,points,victories,loses,draws,goalsScored,goalsConceded]		
	short[][] leagueRank;
	boolean isDomestic;
	
	// Tournament Data	
	int[][] tourGoals;
	int total, levels;
	byte[][] tourTree;
		
				
	public void competitionStartUp(int nt, int theType)
	{
		
		type = theType;
		myFormation = 0;
		
		// LEAGUE STARTUP
		
		//#ifndef NOLEAGUE
		if(type == LEAGUE) {
		
				
			byte teamA, teamB, i1, i2;
			short aux;
					
			teamsNumber = nt;
						
			// Change the order of the teams, a different league every time you play
			
			
			leagueRank = new short[teamsNumber][7];
			for(byte i = 0; i < teamsNumber; i++) {
				
				leagueRank[i][0] = (short)(i);
				
				for(byte j = 1; j < 5; j++) {
				
					leagueRank[i][j] = 0;
				}
								
			}
			
			for(int i = 0; i<teamsNumber*teamsNumber; i++) {
				
				i1 = (byte)GameCanvas.rnd(teamsNumber);
				i2 = (byte)GameCanvas.rnd(teamsNumber);
				
				aux = leagueRank[i1][0];
				leagueRank[i1][0] = leagueRank[i2][0];
				leagueRank[i2][0] = aux;
			}
			
			// Startup League
							
			league = new byte[2*(teamsNumber-1)][teamsNumber/2][4];	
			
			for(int i = 0; i < league.length; i++) {
					
				for(int j = 0; j < league[i].length; j++) {
					
					league[i][j][0] = -1;
					league[i][j][1] = -1;
					league[i][j][2] = -1;
					league[i][j][3] = -1;
				}
			}
			
			// First half: Home Play
			
			// First day
			
			for(int i = 0; i < league[0].length; i++) {
				
				league[1][i][0] = (byte)(i);
				league[1][i][1] = (byte)(teamsNumber - 1 - i);						
				
				league[0][i][1] = (byte)(teamsNumber/2 - 1 - i);
			}
			
			league[0][0][0] = (byte)(teamsNumber - 1);
			
			for(int i = 1; i<league[0].length; i++) {
				
				league[0][i][0] = (byte)(league[0].length - 1 + i);
			}
			
			// Rotate
			
			for(int i = 2; i <= league.length/2; i++) {
						
				if(i%2 == 1){
									
					for(int j = 0; j < league[i].length - 1; j++) {
					
						league[i][j][0] = league[i-2][j+1][0];
					}
						
					league[i][league[i].length - 1][0] = league[i-2][league[i].length - 1][1];
						
					for(int j = 2; j < league[i].length; j++) {
					
						league[i][j][1] = league[i-2][j-1][1];
					}
						
					league[i][0][1] = league[i-2][0][1];
					league[i][1][1] = league[i-2][0][0];
					
				} else {
				
					league[i][0][0] = league[i-2][0][0];
					league[i][league[i].length - 1][0] = league[i-2][league[i].length - 1][1];
					league[i][0][1] = league[i-2][1][0];
				
					for(int j = 1; j < league[i].length - 1; j++) {
					
						league[i][j][0] = league[i-2][j+1][0];
					}
						
					for(int j = 1; j < league[i].length; j++) {
					
						league[i][j][1] = league[i-2][j-1][1];
					}
										
				}
			}
			
			
			// Second half: Visitor play
			
			for(int i = 0; i < teamsNumber - 1; i++){
			
				for(int j = 0; j < teamsNumber/2; j++) {
			
					league[teamsNumber -1 + i][j][0] = league[i][j][1];
					league[teamsNumber -1 + i][j][1] = league[i][j][0];
				}
			}
			
			// Mix teams
			
			for(int i = 0; i < league.length; i++) {
				
				for(int j = 0; j < league[i].length; j++) {
			
					league[i][j][0] = (byte)leagueRank[league[i][j][0]][0];
					league[i][j][1] = (byte)leagueRank[league[i][j][1]][0];
				
				}		
			}
			
			current = 0;
			
			// com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
			/*
			for(int i = 0; i < 2*(teamsNumber - 1); i++){
			
				System.out.print("Jornada "+i+": ");
				
				for(int j = 0; j < teamsNumber/2; j++){
					System.out.print((league[i][j][0]+0)+"VS"+(league[i][j][1]+0)+" ");
				}
				System.out.println();
			}
			*/
		
		}
		//#endif
		
		// TOURNAMENT STARTUP				
		
		//#ifndef NOTOURNAMENT
		if(type == TOURNAMENT) {
					
			levels = nt;
			
			tourTree = new byte[levels][];
			tourGoals = new int[levels][];
			
			tourTree[0] = new byte[1]; tourTree[0][0] = -1; tourGoals[0] = new int[1]; tourGoals[0][0] = 0;
			
			for(int i = 1; i< tourTree.length; i++){
				
				tourTree[i] = new byte[tourTree[i-1].length*2];
				tourGoals[i] = new int[tourTree[i-1].length*2];
				
				for(int j = 0; j<tourTree[i].length; j++){
					tourTree[i][j] = -1;
					tourGoals[i][j] = 0;
				}
			}
					
			current = tourTree.length - 1;
			
			total = tourTree[current].length;
			
			for(int i = 0; i<total; i++)
				tourTree[current][i] = (byte)i;
				
			//System.out.println("Nivells : "+levels+", totals:"+total+" Actual:"+current);
			
			for(int i=0; i<total*total; i++)
			{
				int i1, i2, aux;
				i1 = GameCanvas.rnd(total);
				i2 = GameCanvas.rnd(total);
				
				aux = tourTree[current][i1];
				tourTree[current][i1] = tourTree[current][i2];
				tourTree[current][i2] = (byte)aux;
							
			}
		}
		//#endif
			
	}
	
	public void competitionSimulate()
	{
		//#ifndef NOLEAGUE
		if(type == LEAGUE) {
		
			for(int i = 0; i < league[current].length; i++) {
			
				league[current][i][2] = (byte)GameCanvas.rnd(5);
				league[current][i][3] = (byte)GameCanvas.rnd(5);									
			}		
		}
		//#endif
		
		//#ifndef NOTOURNAMENT
		if(type == TOURNAMENT) {
			
			if(current>0) {
			
				for(int i = 0; i<tourTree[current].length; i+=2) {
					
					tourGoals[current][i] = GameCanvas.rnd(4);
				
					do {
					
						tourGoals[current][i+1] = GameCanvas.rnd(4);
					
					} while(tourGoals[current][i] == tourGoals[current][i+1]);
				}
			}
		}
		//#endif
	}
	
	public void competitionStep()
	{
		byte teamA, teamB, posA, posB, tourGoalsA, tourGoalsB;
		
		//#ifndef NOLEAGUE
		if(type == LEAGUE) {
			
			for(int i = 0; i < league[current].length; i++) {
				
				teamA = league[current][i][0];
				teamB = league[current][i][1];
				tourGoalsA = league[current][i][2];
				tourGoalsB = league[current][i][3];
				
				posA = findPos(teamA);
				posB = findPos(teamB);
				
				leagueRank[posA][5] += tourGoalsA; 
				leagueRank[posA][6] += tourGoalsB; 
				leagueRank[posB][5] += tourGoalsB; 
				leagueRank[posB][6] += tourGoalsA; 
				
				if(tourGoalsA > tourGoalsB) {
					
					// Team A wins
					leagueRank[posA][1] += 3; 
					leagueRank[posA][2] += 1; 
													
					leagueRank[posB][3] += 1; 
					
									
				} else if(tourGoalsA < tourGoalsB) {
					
					// Team B wins
					leagueRank[posB][1] += 3; 
					leagueRank[posB][2] += 1; 
									
					leagueRank[posA][3] += 1; 
					
				} else {
					
					// Draw
					leagueRank[posA][1] += 1;
					leagueRank[posA][4] += 1; 
									
					leagueRank[posB][1] += 1;
					leagueRank[posB][4] += 1; 
				}
			}		
			
			sortLeagueRank();
			
			current++;
			
			/*System.out.println("0 played against "+myOpp((byte)0));
		
			System.out.println("Jornada "+current);
			for(int i = 0; i<leagueRank.length; i++)
				System.out.println( "      "+(i+1)+"� : "+teamNames[leagueRank[i][0]]+", "+leagueRank[i][1]+" points");
			*/
		}
		//#endif
		
		//#ifndef NOTOURNAMENT
		if(type == TOURNAMENT) {
			
			if(current>0) {
				
				/*System.out.println ("Etapa "+current);
				
				for(int i = 0; i < tourTree[current].length; i+=2){
					
					System.out.println (tourTree[current][i]+" VS "+tourTree[current][i + 1]);
				}*/
							
				for(int i = 0; i<tourTree[current].length; i+=2) {
				
					if(tourGoals[current][i] > tourGoals[current][i+1]) {
						
						tourTree[current-1][i/2] = tourTree[current][i]; 
						
					} else {
						
						tourTree[current-1][i/2] = tourTree[current][i+1]; 			
					}
				}
			
				current--;	
											
			}
						
		}
		//#endif
						
	}
	
	public byte myOpp(byte team)
	{
		int i = 0;
		
		//#ifndef NOLEAGUE
		if(type == LEAGUE) {
										
			while(league[current][i][0] != team && league[current][i][1] != team && i < league[current].length) {
							
				i++;				
			}
				
			if(i < league[current].length) { 
			
				if(league[current][i][0] == team) {
					
					 return league[current][i][1];
					 
				}else if(league[current][i][1] == team) {
					
					return league[current][i][0];
				}
			}
		}
		//#endif
		
		//#ifndef NOTOURNAMENT
		if(type == TOURNAMENT) {
						
			int pos=0;
		
			for(i = 0; i<tourTree[current].length; i++) {
			
				if(tourTree[current][i] == myTeam) {
					
					pos = i;
				}
			}
			
			if(pos%2 == 0) {
				
				return tourTree[current][pos+1];
				
			} else {
				
				return tourTree[current][pos-1];
			}

		}
		//#endif
		
		return -1;
	}
	
	public void setTourGoals(byte team, byte goal)
	{
		
		//#ifndef NOLEAGUE
		if(type == LEAGUE) {
		
			for(int i = 0; i < league[current].length; i++) {
				
				if(league[current][i][0] == team) {
					
					league[current][i][2] = goal; 
					return;
				}
				
				if(league[current][i][1] == team) {
					
					league[current][i][3] = goal; 
					return;
				}			
			}
		}
		//#endif
		
		//#ifndef NOTOURNAMENT
		if(type == TOURNAMENT) {
			
			int pos=0;
		
			for(int i=0; i<tourTree[current].length; i++) {
			
				if(tourTree[current][i] == team) pos = i;
			}
			
			if(pos< tourGoals[current].length) {
				
				tourGoals[current][pos] = goal;
			}
						
		}
		//#endif
	}
	
	public byte findPos(int team)
	{
		byte i = 0;
		
		//#ifndef NOLEAGUE
		if(type == LEAGUE) {
		
			while(leagueRank[i][0] != team && i < leagueRank.length) {
				
				 i++;
			}
				
			if(i < leagueRank.length) {
				
				 return i;
			}
		}
		//#endif
		
		return -1;
	}
	
	//#ifndef NOLEAGUE
	public void sortLeagueRank()
	{
		short aux[];
		
		if(type == LEAGUE) {
		
			for(int j = 0; j < teamsNumber; j++) {
			
				for(int i = 0; i < leagueRank.length - 1; i++) {
							
					if(leagueRank[i][1] < leagueRank[i+1][1] || 
						(leagueRank[i][1] == leagueRank[i+1][1] && leagueRank[i][5]-leagueRank[i][6] < leagueRank[i+1][5]-leagueRank[i+1][6]) ||
						(leagueRank[i][1] == leagueRank[i+1][1] && leagueRank[i][5]-leagueRank[i][6] == leagueRank[i+1][5]-leagueRank[i+1][6] 
						&& leagueRank[i][5] < leagueRank[i+1][5]))		
					{
						// Swap		
						aux = leagueRank[i];
						leagueRank[i] = leagueRank[i+1];
						leagueRank[i+1] = aux;					
					}
				}
			}
		}
							
	}
	//#endif
		
	public void competitionWrite(DataOutputStream ostream)
	{
		
		try{
			
			//ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	    	//DataOutputStream ostream = new DataOutputStream(bstream);				
					
			ostream.writeInt(type);
			
			//#ifndef NOLEAGUE
			if(type == LEAGUE) {
		
				ostream.writeInt(teamsNumber);
				ostream.writeInt(current);			
				ostream.writeInt(duration);
				ostream.writeByte(myTeam);				
				ostream.writeBoolean(wholeLeague);
				ostream.writeBoolean(isDomestic);
				ostream.writeInt(myFormation);
				
				for(int i = 0; i<myAlignment.length; i++) {
				
					ostream.writeByte(myAlignment[i]);
				}
				
				for(int i = 0; i<teamsNumber; i++) {
										
					for(int j = 0; j<7; j++) {
					
						ostream.writeShort(leagueRank[i][j]);
					}
				}
												
				for(int i = 0; i<2*(teamsNumber-1); i++) {
				
					for(int j = 0; j<teamsNumber/2; j++) {
				
						for(int k = 0; k<4; k++){
					
							ostream.writeByte(league[i][j][k]);
						}
					}
				}
			}
			//#endif
			
			//#ifndef NOTOURNAMENT
			if(type == TOURNAMENT) {
				
				ostream.writeInt(levels);
				ostream.writeInt(current);
				ostream.writeInt(total);
		
				for(int i = 0; i<tourTree.length; i++){
				
					for(int j = 0; j<tourTree[i].length; j++) {
				
						ostream.writeByte(tourTree[i][j]);
					}
				}
				
				for(int i = 0; i<tourTree.length; i++) {
				
					for(int j = 0; j<tourTree[i].length; j++) {
					
						ostream.writeInt(tourGoals[i][j]);					
					}
				}
			}
			//#endif
			
			//return bstream.toByteArray();
						
		}catch(Exception e){/*System.out.println("Error en escritura de competici�n");*/}
		
		//return null;
	}
	
	public void competitionRead(DataInputStream istream)
	{
		
		try{
			
			//DataInputStream istream = new DataInputStream(new ByteArrayInputStream(data,0,data.length));
			
			type = istream.readInt();
			
			//#ifndef NOLEAGUE
			if(type == LEAGUE) {
		
				teamsNumber = istream.readInt();
				//System.out.println("teamsNumber: "+teamsNumber);
				competitionStartUp(teamsNumber,LEAGUE);
				
				current = istream.readInt();			
				duration = istream.readInt();
				myTeam = istream.readByte();				
				wholeLeague = istream.readBoolean();
				isDomestic = istream.readBoolean();
				myFormation = istream.readInt();
				
				for(int i = 0; i<myAlignment.length; i++) {
										
					myAlignment[i] = istream.readByte();
				}
				
				for(int i = 0; i<teamsNumber; i++) {
				
					for(int j = 0; j<7; j++) {
					
						leagueRank[i][j] = istream.readShort();
					}
				}
												
				for(int i = 0; i<2*(teamsNumber-1); i++) {
				
					for(int j = 0; j<teamsNumber/2; j++) {
					
						for(int k = 0; k<4; k++) {
						
							league[i][j][k] = istream.readByte();
						}
					}
				}
			}
			//#endif
			
			//#ifndef NOTOURNAMENT
			if(type == TOURNAMENT) {
				
				levels = istream.readInt();
				
				competitionStartUp(levels,TOURNAMENT);
				
				current = istream.readInt();
				total = istream.readInt();
			
				for(int i = 0; i<tourTree.length; i++) {
				
					for(int j = 0; j<tourTree[i].length; j++) {
					
						tourTree[i][j] = istream.readByte();
					}
				}
				
				for(int i = 0; i<tourTree.length; i++) {
				
					for(int j = 0; j<tourTree[i].length; j++) {
					
						tourGoals[i][j] = istream.readInt();
					}
				}
				
			}
			//#endif
						
		}catch(Exception e){/*System.out.println("Error en carga de competici�n");*/}
	}
	
	/*public void competitionDestroy()
	{		
		//#ifndef NOLEAGUE
		leagueRank = null; league = null;
		//#endif
		
		//#ifndef NOTOURNAMENT
		tourTree = null; tourGoals = null;
		//#endif
		
		System.gc();
	}*/

//}


	boolean backFromMenu;
	
	
//#ifdef WEBSUBSCRIPTION
//#endif


static int freezeHumanPlayer = 0;

	
// ******************* //
//  Final de la Clase  //
// ******************* //
};