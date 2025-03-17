package com.mygdx.mongojocs.turtleandsons;

// -----------------------------------------------
// Source Base iMode v1.1 Rev.1 (3.2.2004)
// ===============================================
// iMode
// ------------------------------------






// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.IApplication;

import java.io.InputStream;

public class Game extends IApplication
{

GameCanvas gc;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()
{
	System.gc();
	gc = new GameCanvas(this); 
	System.gc();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// start
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void start()
{
	Display.setCurrent(gc);

	run();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	gameExit=true;
	terminate();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;

long gameMillis;
int  gameSleep;

public void run()
{
	Display.setCurrent(gc);

	System.gc();
	gameCreate(); System.gc();
	gc.canvasInit(); System.gc();
	gameInit(); System.gc();

	while (!gameExit)
	{
		gameMillis = System.currentTimeMillis();

		keyboardTick();

		gameTick();

		gc.canvasShow=true;
		gc.repaint();

		//while (gc.canvasShow==true) {try {Thread.sleep(1);} catch(Exception e) {}}

		gameSleep=(50-(int)(System.currentTimeMillis()-gameMillis));
		if (gameSleep<1) {gameSleep=1;}
		try	{ Thread.sleep(gameSleep); } catch(Exception e) {}
	}

	gameDestroy();

	destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

void loadFile(byte[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
	is.read(buffer, 0, buffer.length);
	is.close();
	}catch(Exception exception) {}
	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public byte[] loadFile(String Nombre)
{
	FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+Nombre);
	byte[] bytes = file.readBytes();

	return bytes;
	/*System.gc();

	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] buffer = null;

	try
	{
		int Size = 0;
		while (true)
		{
		int Desp = (int) is.skip(1024);
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
	catch(Exception exception) {}

	System.gc();

	return buffer;*/
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// RND - Engine
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int RND_Cont=0;
int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

public int RND(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 > x1+xs1 ) ||	( x2+xs2 < x1 ) ||	( y2 > y1+ys1 ) || ( y2+ys2 < y1 ));
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// *******************
// -------------------
// Keyboard - Engine
// ===================
// *******************

int keyX, keyY, keyMisc, keyMenu;
int lastKeyX, lastKeyY, lastKeyMisc, lastKeyMenu;

// -------------------
// Keyboard RUN
// ===================

public void keyboardTick()
{
	lastKeyX = keyX;	// Keys del Frame Anterior
	lastKeyY = keyY;
	lastKeyMisc = keyMisc;
	lastKeyMenu = keyMenu;

	keyX = gc.keyX;	// Keys del Frame Actual
	keyY = gc.keyY;
	keyMisc = gc.keyMisc;
	keyMenu = gc.keyMenu;
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// textos - Engine - v1.0 - Rev.0 (28.01.2004)
// ===================
// *******************

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
//////////////////////////TEXTOS EXTRAS/////////////////////////////////////////
final static int TEXT_GAMEOVER2 = 13;
final static int TEXT_LEVEL = 14;
final static int TEXT_POINTS = 15;
final static int TEXT_PREPARE = 16;
////////////////////////////////////////////////////////////////////////////////

// -------------------
// textos Create
// ===================

String[][] textosCreate(byte[] textos)
{
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;
	int size = 0;

	for (int i=0 ; i<textos.length ; i++)
	{
		if (campo)
		{
			if (textos[i] == 0x7D)
			{
			subCampo = false;
			campo = true;
			}

			if (textos[i] < 0x20 || textos[i] == 0x7C || textos[i] == 0x7D)	// Buscamos cuando Termina un campo
			{
			data[dataPos+1] = (short) (i - data[dataPos]);

			dataPos+=2;

			campo=false;
			}

		} else {

			if (textos[i] == 0x7D)
			{
			subCampo = false;
			continue;
			}

			if (textos[i] == 0x7B)
			{
			dataBak = dataPos;
			data[dataPos++] = 0;
			campo = false;
			subCampo = true;
			size++;
			continue;
			}

			if (subCampo && textos[i] == 0x0A)
			{
				if (primerEnter)
				{
					primerEnter = false;
				} else {
					data[dataPos++] = (short) i;
					data[dataPos++] = 1;
					if (!subCampo) {size++;} else {data[dataBak]--;}
				}
				continue;
			}

			if (textos[i] >= 0x20)	// Buscamos cuando Empieza un campo nuevo
			{
				campo=true;
				data[dataPos] = (short) i;
				if (!subCampo) {size++;} else {data[dataBak]--;}
				primerEnter = true;
			}
		}
	}

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
	int num = data[dataPos];

	if (num<0) {num*=-1;dataPos++;} else {num = 1;}

	strings[i] = new String[num];

	for (int t=0 ; t<num; t++) {strings[i][t] = new String(textos, data[dataPos++], data[dataPos++]);}
	}

	return strings;
}

// <=- <=- <=- <=- <=-




// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------







// *******************
// -------------------
// Game - Engine
// ===================
// *******************

int gameStatus=0;

int gameSound = 1;
int gameVibra = 1;
int gameLevel = 0;

Marco ma;

String[][] menuText;

// -------------------
// Game INI
// ===================

public void gameCreate()
{
	ma = new Marco(gc.canvasWidth, gc.canvasHeight);
}

// -------------------
// Game SET
// ===================

public void gameInit()
{
	prefsCreate();		// Leemos Archivo Preferencias

	menuText = textosCreate(loadFile("/Textos.txt"));		// Cargamos Textos.
	
}

// -------------------
// Game END
// ===================

public void gameDestroy()
{
	prefsInit();		// Grabamos Archivo Preferencias
}


// -------------------
// Game RUN
// ===================

long gameTicks = 0;

public void gameTick()
{
	gameTicks++;
	
	switch (gameStatus)
	{
	case 0:
		gameStatus++;
	break;

	case 1:
		gameStatus=10;
	break;


// ------------------
//  Slide Logos
// ------------------
	case 10:
		gc.canvasFillInit(0xFFFFFF);
		gc.canvasImg = gc.FS_LoadImage(0,1);
		waitInit(3*1000, gameStatus+1);
	break;

	case 11:
		gameStatus = 50;
	break;
// ===================





// ------------------
// Menu Bucle
// ------------------
	case 50:
		gc.canvasFillInit(0);
		gc.canvasImg = gc.FS_LoadImage(0,2);

		gc.SoundSET(SFX_MAIN_THEME,0);

		gameStatus++;
	break;

	case 51:
		if ((keyMenu!=0 && keyMenu!=lastKeyMenu) || (keyMisc!=0 && keyMisc!=lastKeyMisc)) {
			gc.bClearBorder = true;
			gameStatus++;
			panelInit(0);
		}
	break;

	case 52:
		gc.SoundRES();
		gameStatus=100; 
	break;
// ===================


// ------------------
// Jugar Bucle
// ------------------
	case 100:
		jugarCreate();
		gameStatus++;
	case 101:
		jugarInit();
		gameStatus++;
	case 102:
		cheats();

		if (keyMenu==-1 && keyMenu!=lastKeyMenu) {panelInit(1); break;}

		if ( !jugarTick() ) {break;}
	
		if(NIVEL == 200) gameStatus += 2 ;
		gameStatus++;
	break;

	case 103:	// GAME OVER
		PUNTUACION = 10;
		LAST_NIVEL = NIVEL;
		NIVEL = 1;
		PARAM_SALIDA_TORTUGAS = 100; //menos(100..26)mas
		PARAM_DELAY_APARICION = 1000;
		PARAM_DELAY_ESPERA = 900;
		gameSubStatus = EJ_READY;
		gc.bClearBorder = true;
		gc.lastLevel = 1;
		
		panelInit(5);
		waitInit(7*1000, 50);
	break;

	case 104:	// CONGRATULATIONS VISIAUUU!!!!
		panelInit(5);
		waitInit(3*1000, 50);
	break;
	
	
// ===================



// ------------------
// Wait Bucle
// ------------------
	case 1000:
		waitTick();
	break;
// ==================


// ------------------
// Panel Bucle
// ------------------
	case 900:
		gc.bClearBorder = true;
		panelTick();
	break;
// ==================
	}

}

// <=- <=- <=- <=- <=-







// *******************
// -------------------
// Panel - Engine
// ===================
// *******************

int panelType;
int panelTypeOld;
int panelStatus;

// -------------------
// Panel SET
// ===================

public void panelInit(int Type)
{
	panelStatus = gameStatus;
	gameStatus = 900;

	panelTypeOld = panelType;
	panelType = Type;

	switch (Type)
	{
	case 0:
		ma.MarcoINI();
		ma.MarcoADD(0x000, menuText[TEXT_PLAY], 0);
		ma.MarcoADD(0x000, menuText[TEXT_SOUND], 2, gameSound);
		ma.MarcoADD(0x000, menuText[TEXT_VIBRA], 3, gameVibra);
		ma.MarcoADD(0x000, menuText[TEXT_HELP], 6);
		ma.MarcoADD(0x000, menuText[TEXT_ABOUT], 7);
		ma.MarcoADD(0x000, menuText[TEXT_EXIT], 9);
		ma.MarcoSET_Option();
	break;	

	case 1:
		ma.MarcoINI();
		ma.MarcoADD(0x000, menuText[TEXT_CONTINUE], 1);
		ma.MarcoADD(0x000, menuText[TEXT_SOUND], 2, gameSound);
		ma.MarcoADD(0x000, menuText[TEXT_VIBRA], 3, gameVibra);
		ma.MarcoADD(0x000, menuText[TEXT_HELP], 6);
		ma.MarcoADD(0x000, menuText[TEXT_RESTART], 8);
		ma.MarcoADD(0x000, menuText[TEXT_EXIT], 9);
		ma.MarcoSET_Option();
	break;	

	case 5:
		ma.MarcoINI();
		//ma.MarcoADD(0x111, menuText[TEXT_GAMEOVER]);
		//String s = new String("Fin de juego.\nHas perdido todos tus puntos.\nNIVEL [ "+LAST_NIVEL+" ].");
		//String s = new String("Fin de juego.\nNIVEL [ "+LAST_NIVEL+" ].");

		ma.MarcoFIX(0x001, menuText[TEXT_GAMEOVER][0]);
		ma.MarcoFIX(0x001, menuText[TEXT_GAMEOVER2][0]);
		ma.MarcoFIX(0x001, menuText[TEXT_LEVEL][0]+"   >>  "+LAST_NIVEL+"  <<");
		
		//ma.MarcoFIX(0x001, s);
		ma.MarcoSET_Texto();
	break;

	case 6:
		ma.MarcoINI();
		ma.MarcoADD(0x001, menuText[TEXT_HELP_SCROLL]);
		ma.MarcoSET_Scroll(0);
	break;

	case 7:
		ma.MarcoINI();
		ma.MarcoADD(0x001, menuText[TEXT_ABOUT_SCROLL]);
		ma.MarcoSET_Scroll(0);
	break;
	}

}


// -------------------
// Panel END
// ===================

public void panelDestroy()
{
	ma.MarcoEND();
	gameStatus = panelStatus;
}


// -------------------
// Panel RUN
// ===================

public void panelTick()
{
	int marcoKey=0;
	if (keyY==-1 && lastKeyY!=-1) {marcoKey=2;}
	if (keyY== 1 && lastKeyY!= 1) {marcoKey=8;}
	if (keyMenu!= 0 && lastKeyMenu== 0) {marcoKey=5;}

	panelAction( ma.MarcoRUN(marcoKey, keyY) );
}

// ---------------
// Panel EXE
// ===============

public void panelAction(int cmd)
{
	switch (cmd)
	{
	case -2: // Scroll ha llegado al final
		panelDestroy();
		gc.canvasFillInit(0);
		panelInit(panelTypeOld);
	break;

	case 0:	// Jugar de 0
		panelDestroy();
		gc.canvasFillInit(0);
	break;

	case 1:	// Continuar
		panelDestroy();
		gc.canvasFillInit(0);
	break;

	case 2:	// Sound ON/OFF
		gameSound = ma.getOption();
		if (panelType==0) {if (gameSound!=0) {gc.SoundSET(SFX_MAIN_THEME,0);} else {gc.SoundRES();}}
	break;

	case 3:	// Vibra ON/OFF
		gameVibra = ma.getOption();
	break;

	case 6:	// Controles...
	case 7:	// About...
		panelDestroy();
		gc.canvasFillInit(0);
		panelInit(cmd);
	break;

	case 8:	// Restart
		panelDestroy();
		gc.canvasFillInit(0);
		gameStatus++;
	break;

	case 9:	// Exit Game
		gc.canvasFillInit(0);
		gameExit = true;
	break;	
	}

}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// Wait - Engine
// ===================
// *******************

long waitTiempoIni, waitTiempoFin;
int waitRetorno;

// -------------------
// Wait INI
// ===================

public void waitCreate(int Tiempo, int Ret)
{
	waitRetorno=Ret;
	waitTiempoFin = System.currentTimeMillis() + Tiempo;
}

// -------------------
// Wait SET
// ===================

public void waitInit()
{
	gameStatus=1000;
}

public void waitInit(int Tiempo, int Ret)
{
	waitRetorno=Ret;
	waitTiempoFin = System.currentTimeMillis() + Tiempo;
	gameStatus=1000;
}

// -------------------
// Wait RUN
// ===================

public void waitTick()
{
	if (waitTiempoFin < System.currentTimeMillis()) {gameStatus=waitRetorno;}
}

// <=- <=- <=- <=- <=-














// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

boolean jugarShow = false;

int protX;
int protY;

// -------------------
// Jugar INI
// ===================

public void jugarCreate()
{
	jugarShow = false;
	PROTA = new Prota(this,gc);
	gc.jugarCreate_Gfx();
}


// -------------------
// Jugar END
// ===================

public void jugarDestroy()
{
	jugarShow = false;
}


// -------------------
// Jugar SET
// ===================

public void jugarInit()
{
	gc.jugarInit_Gfx();
	PROTA.reset();
}


// -------------------
// Jugar RES
// ===================

public void jugarRelease()
{
}


// -------------------
// Jugar RUN
// ===================

int PUNTUACION = 10;


////////////////////////////////////////////////////////////////////////////////
// ESTADOS DE JUEGO
////////////////////////////////////////////////////////////////////////////////

static final int EJ_READY	= 1;
static final int EJ_JUEGO	= 2;


int gameSubStatus = EJ_READY;


public boolean jugarTick()
{

	switch(gameSubStatus) {


		case EJ_READY:

			gc.bImpTextosTrans = true;
			gc.bImpFondo = true;
			gameSubStatus = EJ_JUEGO;
			break;
		
		case EJ_JUEGO:
			jugarShow = true; 
			gc.bImpFondo = true; 
			gc.bImpProta = true;
			if(gc.bImpTextosTrans) {
				return false;
			}
			if(NIVEL == 200) return true;
			
			jugarShow = true; 
			/*gc.bImpFondo = true;*/ gc.bImpProta = true;	
			gc.bImpEnemigos = true; gc.bImpTextos = true;
				
			generaEnemigo();
		
			for(int i=0;i<4;i++) if(ENEMIGOS[i] != null) ENEMIGOS[i].RUN();
			for(int i=0;i<8;i++) if(TEXTOS[i] != null) TEXTOS[i].RUN();
			PROTA.RUN();
		
			if(PUNTUACION >= NIVEL*30) {
				NIVEL++;
				PUNTUACION = 10*NIVEL;
				PARAM_SALIDA_TORTUGAS = 100-NIVEL; //menos(100..26)mas
				PARAM_DELAY_APARICION = 1000-NIVEL*50;
				PARAM_DELAY_ESPERA = 500-NIVEL*10;
			} else if(PUNTUACION < 0) {
				return true;
			}
					
			break;
	}
	return false;
			
}

// <=- <=- <=- <=- <=-
////////////////////////////////////////////////////////////////////////////////
// SONIDOS
////////////////////////////////////////////////////////////////////////////////

static final int SFX_MAIN_THEME = 0;
static final int SFX_LOOSE 		= 1;
static final int SFX_HIT 		= 2;
static final int SFX_WIN 		= 3;

////////////////////////////////////////////////////////////////////////////////
// PARAMETROS QUE RIGEN LA DIFICULTAD
////////////////////////////////////////////////////////////////////////////////

int PARAM_SALIDA_TORTUGAS = 100; //menos(100..26)mas
long PARAM_DELAY_APARICION = 1000;
long PARAM_DELAY_ESPERA = 900;

int NIVEL = 1;
int LAST_NIVEL = 1;
////////////////////////////////////////////////////////////////////////////////
// GENERACION/DESTRUCCION ENEMIGOS
////////////////////////////////////////////////////////////////////////////////

Prota PROTA;

Enemigo ENEMIGOS[] = {null,null,null,null};

boolean posOcupada[] = {false,false,false,false};
private long timerAparicion = System.currentTimeMillis();

private void generaEnemigo() {
	if(System.currentTimeMillis()-timerAparicion < PARAM_DELAY_APARICION) return;
	int i = buscaSlotLibre(); if(i < 0) return;
	int pos = buscaPosLibre(); if(pos < 0) return;

	//System.out.println("Generando enemigo: id= "+i+" pos= "+pos);
	int k=0;
	if(RND(PARAM_SALIDA_TORTUGAS)>25) k=1;
	ENEMIGOS[i] = new Enemigo(this,gc,k,i,pos);
	
	posOcupada[pos] = true;

	timerAparicion = System.currentTimeMillis();
}




private int buscaSlotLibre() {
	for(int i=0;i<4;i++) if(ENEMIGOS[i]==null) return i;
	return -1;	
}
private int buscaPosLibre() {
	int j;
	for(j=0;j<4;j++) if(!posOcupada[j]) break;
	if(j == 4) return -1;
	
	int i = RND(4);
	while(posOcupada[i]) i = RND(4);
	return i;	
}

private int buscaEnemEnPos(int pos) {
	for(int i=0;i<4;i++) if(ENEMIGOS[i] != null) if(ENEMIGOS[i].pos == pos) return i;
	return -1;
}
public void destroyMe(int id) {
	if(ENEMIGOS[id] != null) {
		posOcupada[ENEMIGOS[id].pos] = false;
		ENEMIGOS[id] = null;
		System.gc();
	}
}


public void mazazoEnPos(int pos) {
	if(pos < 0 || pos > 3) return;
	int i = buscaEnemEnPos(pos); if(i < 0) return;
	
	if(gameSound != 0) gc.SoundSET(SFX_HIT,1);
	
	ENEMIGOS[i].ostia();
	//gc.scr.setOrigin(50,50);
}


Texto TEXTOS[] = {null,null,null,null,null,null,null,null};
public void ponPuntos(int x,int y, int pnts) {
	for(int i=0;i<8;i++) { 
		if(TEXTOS[i] == null) {
			TEXTOS[i] = new Texto(this,x,y,Integer.toString(pnts),i);
			break;
		}
	}
}			

public void destroyText(int id) {
	TEXTOS[id] = null;
	System.gc();
}


// *******************
// -------------------
// Prefs - Engine
// ===================
// *******************

byte[] prefsData = new byte[] {1,1,0};

// -------------------
// Prefs INI
// ===================

public void prefsCreate()
{
	prefsData = gc.FS_LoadFile(0,0);

	gameSound=prefsData[0] & 1;
	gameVibra=prefsData[1] & 1;
	gameLevel=prefsData[2];
}

// -------------------
// Prefs SET
// ===================

public void prefsInit()
{
	prefsData[0]=(byte)gameSound;
	prefsData[1]=(byte)gameVibra;
	prefsData[2]=(byte)gameLevel;

	gc.FS_SaveFile(0,0, prefsData);
}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// cheats - Engine
// ===================
// *******************

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


// **************************************************************************//
} // Final Clase MIDlet
// **************************************************************************//