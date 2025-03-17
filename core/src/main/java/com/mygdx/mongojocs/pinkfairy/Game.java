package com.mygdx.mongojocs.pinkfairy;

// -----------------------------------------------
// SAIYU Sea Invasion - JellyFish Attack v 1.0 (05.03.2004)
// ===============================================
// Logica
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
int  gameSleep,gameWait=50;

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

		gc.gameDraw();

		gameSleep=(gameWait-(int)(System.currentTimeMillis()-gameMillis));
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
	return !(( x2 >= x1+xs1 ) || ( x2+xs2 <= x1 ) || ( y2 >= y1+ys1 ) || ( y2+ys2 <= y1 ));
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

int timeToChange = 0;
int gameSound = 1;
int gameVibra = 1;
int gameScore = 0;

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

public void gameTick()
{
	if (timeToChange>0) timeToChange--;
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
		gc.canvasFillInit(0xffffff);
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

		gc.SoundSET(0,0);

		gameStatus++;
	break;

	case 51:
		if ((keyMenu!=0 && keyMenu!=lastKeyMenu) || (keyMisc!=0 && keyMisc!=lastKeyMisc)) {gameStatus++;panelInit(0);}
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
		textC = (0xFF80FF);
		textX = gc.canvasWidth/2;
		textY = (gc.canvasHeight/2)-50+10;
		textTime = 50;
		text = menuText[13][0]+ActualLevel;
		gameStatus++;
	case 102:
		cheats();

		if (keyMenu==-1 && keyMenu!=lastKeyMenu) {panelInit(1); break;}

		if ( !jugarTick() ) {break;}

		jugarRelease();

		gameStatus++;
	break;

	case 103:
		jugarDestroy();
		gameStatus++;
	break;

	case 104:	// GAME OVER
		panelInit(5);
		waitInit(3*1000, 50);
	break;
	
	case 110: //LEVELFINISHED
		timeToChange = 50;
		textTime = 50;
		textC = (0xFF80FF);
		textX = gc.canvasWidth/2;
		textY = gc.canvasHeight/2;
		text = menuText[15][0];
		gameStatus = 120;	
	
	case 120: //CHANGE LEVEL
		if (timeToChange==0)
		{
			gameStatus = 101;	
		} else { if ( !jugarTick() ) {break;} }
	break;
	
	case 200: //Dead
		shotV = false;
		timeToChange = 40;
		timeInmortal = 100;
		VelY = -7;
		textTime = 40;
		textC = (0xFFFFFF);
		textX = gc.canvasWidth/2;
		textY = gc.canvasHeight/2;
		text = menuText[14][0];
		gameStatus = 205;
		gc.SoundRES();
		gc.SoundSET(3,1);

	
	case 205: 
		if (timeToChange==0)
		{
			gameStatus = 210;
			timeToChange = 60;
			gc.SoundRES();
			gc.SoundSET(2,1);
		} else
		{
			if (((protX-(protW/2))<gc.canvasWidth)&&((protY-(protH/2))<gc.canvasHeight))
			{
				protY += ++VelY;
				protX += 3;
			}
			if ( !jugarTick() ) {break;}
		}
	break;
	
	case 210:
		if (timeToChange==0)
		{
			gameStatus = 50;	
		} else { jugarShow = true; }
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
		ma.MarcoADD(0x111, menuText[TEXT_GAMEOVER]);
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
	if (keyMenu != 0 && lastKeyMenu== 0) {marcoKey=5;}

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
		if (gameSound==0) {gc.SoundRES();} else if (panelType==0) {gc.SoundSET(0,0);}
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
// Prefs - Engine
// ===================
// *******************

byte[] prefsData = new byte[] {1,1,0,0,0,0};

// -------------------
// Prefs INI
// ===================

public void prefsCreate()
{
	prefsData = gc.FS_LoadFile(0,0);

	gameSound=prefsData[0] & 1;
	gameVibra=prefsData[1] & 1;
	int ActualScore=0;
	ActualScore+=prefsData[5];
	ActualScore <<= 8;
	ActualScore+=prefsData[4];
	ActualScore <<= 8;
	ActualScore+=prefsData[3];
	ActualScore <<= 8;
	ActualScore+=prefsData[2];
	gameScore=ActualScore;
}

// -------------------
// Prefs SET
// ===================

public void prefsInit()
{
	prefsData[0]=(byte)gameSound;
	prefsData[1]=(byte)gameVibra;
	int ActualScore=0;
	ActualScore = gameScore;
	prefsData[2] = (byte)((gameScore >> 0) & 0xFF);
	prefsData[3] = (byte)((gameScore >> 8) & 0xFF);
	prefsData[4] = (byte)((gameScore >> 16) & 0xFF);
	prefsData[5] = (byte)((gameScore >> 24) & 0xFF);
	
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



// -------------------------------------------------------------------------------------------
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// -------------------------------------------------------------------------------------------





// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

boolean jugarShow = false;

// -------------------
// Jugar INI
// ===================

public void jugarCreate()
{
	jugarShow = false;
	ActualLevel = 1;
	gc.jugarCreate_Gfx();
}


// -------------------
// Jugar END
// ===================

public void jugarDestroy()
{
	jugarShow = false;

	gc.jugarDestroy_Gfx();
}


// -------------------
// Jugar SET
// ===================

int protX;
int protY;
int protDX;
int protDY;
int protW;
int protH;

int shotX;
int shotY;
int shotDX;
int shotDY;
int shotW;
int shotH;
boolean shotV;

int[] eShotT;
int[] eShotX;
int[] eShotY;
int eShotDX;
int eShotDY;
int eShotW;
int eShotH;
boolean[] eShotV;


int enemX;
int enemY;
int enemDX;
int enemDY;
int enemW;
int enemH;
int enemD;
boolean[][] enemV;
boolean[] enemColE;

int butterFlyX[];
int butterFlyY[];
int butterFlyDX;
int butterFlyDY;
int butterFlyW;
int butterFlyH;
int butterFlyD[];
boolean[] butterFlyV;

int ActualLevel = 1;

int TramosFondo=15; 

int sinoidal,incSinus;

int left,right;

int tipoFondo,tipoSprites;
int enemiesRemain;

int score;

int textX;
int textY;
int textC;
String text="";


byte timeInmortal=30, textTime=0;

int VelY=5;

public void jugarInit()
{
	gc.jugarInit_Gfx();
	
	protW	= 26;
	protH	= 30;
	protX	= gc.canvasWidth/2;
	protY	= gc.canvasHeight-(protH/2)-5;
	protDX	= 3;
	protDY	= 0;
	
	shotW	= 5;
	shotH	= 13;
	shotX	= -10;
	shotY	= -10;
	shotDX	= 0;
	shotDY	= -3 -(((ActualLevel<=15) ? ActualLevel : 15)/3);
	shotV	= false;
	
	if (ActualLevel == 1) score = 0;
	
	int MaxEnemyShot=3;
	eShotT = new int[MaxEnemyShot];
	eShotX = new int[MaxEnemyShot];
	eShotY = new int[MaxEnemyShot];
	eShotDX = 0;
	eShotDY = 2;
	eShotW  = 4;
	eShotH  = 4;
	eShotV = new boolean[MaxEnemyShot];
	
	for (int Counter=0; Counter<eShotV.length; Counter++)
	{
		eShotT[Counter] = ((Counter*(600/MaxEnemyShot))/ActualLevel);
		eShotX[Counter] = 0;
		eShotY[Counter] = 0;
		eShotV[Counter] = false;
	}
	
	enemW	= 68;
	enemH	= 32;
	enemX	= 18+((144-18)/2);
	enemY	= 50+(enemH/2);
	enemDX	= ((ActualLevel+2)/4);
	enemDY	= ((ActualLevel+4)/5);
	enemD	= 1;
	
	int NumColEnemies=6;
	enemV 	= new boolean[3][NumColEnemies];
	enemColE = new boolean[NumColEnemies];
	for (int F=0; F<enemV.length; F++) for (int C=0; C<enemV[F].length; C++) 
	{
		enemV[F][C] = true;
		enemColE[C] = false;
	}
	left = 0;
	right = 0;
	
	sinoidal = 2;
	incSinus = 1;
	tipoFondo = 1;
	tipoSprites = 1;
	
	enemiesRemain = 18;
	
	int MaxButterFly = 5;
	butterFlyX   = new int[MaxButterFly];
	butterFlyY   = new int[MaxButterFly];
	butterFlyDX  = 2;
	butterFlyDY  = -3;
	butterFlyW   = 8;
	butterFlyH   = 8;
	butterFlyD   = new int[MaxButterFly];
	butterFlyV   = new boolean[MaxButterFly];
	
	for (int Counter = 0; Counter<butterFlyV.length; Counter++)
	{
		butterFlyX[Counter] = enemX;
		butterFlyY[Counter] = enemY;
		butterFlyD[Counter] = (Counter%2);
		if (butterFlyD[Counter]==0) butterFlyD[Counter]=-1;
		butterFlyV[Counter] = false;
	}
}


// -------------------
// Jugar RES
// ===================

public void jugarRelease()
{
	gc.jugarRelease_Gfx();
}


// -------------------
// Jugar RUN
// ===================

public boolean jugarTick()
{
	if (timeInmortal>0) timeInmortal--;
	if (textTime>0) textTime--;
	if ((sinoidal==0)||(sinoidal==5)) incSinus *= -1;
	sinoidal += incSinus;
	int lateral = 10;
	if ((timeInmortal<15)&&(((keyX<0)&&((protX-lateral)>(0+protW/2)))||((keyX>0)&&((protX+lateral)<(gc.canvasWidth-protW/2))))) protX += keyX*protDX;
	//if ((lastKeyMisc!=7)&&(keyMisc==7)&&(TramosFondo>1)) TramosFondo--;
	//if ((lastKeyMisc!=9)&&(keyMisc==9)) TramosFondo++;
	//if ((lastKeyMisc!=1)&&(keyMisc==1)) tipoFondo *= -1;
	//if ((lastKeyMisc!=3)&&(keyMisc==3)) tipoSprites *= -1;
	if (enemiesRemain<9) tipoSprites = -1; else tipoSprites =1;
	if ((gameStatus==102)&&((shotV==false)&&(keyMisc == 5)&&(timeInmortal==0)))
	{
		shotV = true;
		shotX = protX;
		shotY = protY - (1+(protH)/2) - ((1+shotH)/2) + (sinoidal-2);
	}
	if (shotV)
	{
		int F;
		int C;
		
		if (((shotY-(2-sinoidal))>(enemY-(enemH/2)))&&(shotX>(enemX-(enemW/2))))
		{
			F = (((shotY-(2-sinoidal)-(enemY-(enemH/2))))/12);
			C = (((shotX-(enemX-(enemW/2))))/12);
					
			if ((F>=0)&&(C>=0)&&(F<enemV.length)&&(C<enemV[F].length)) 
			{
				if ((enemV[F][C])&&(((shotX-enemX-(C*10))<=8)&&((shotY-(2-sinoidal)-enemY-(F*10))<=8)))
				{
					score += 10 * ActualLevel;
					PlaceText("+"+(10 * ActualLevel),shotX,shotY,(byte)0);
					enemV[F][C] = false;
					shotV = false;
					enemiesRemain--;
					gc.SoundRES();
					if (InGameSounds==1) gc.SoundSET(3,1);
					butterFlyCreate(shotX,shotY);
					boolean EmptyCol=true;
					for (int Counter=0; Counter<enemV.length; Counter++) EmptyCol = (EmptyCol && !enemV[Counter][C]);
					if (EmptyCol) enemColE[C] = true;
					
					boolean Eleft=true,Eright=true;
					left = 0;
					right = 0;
					for (int Counter=0; Counter<enemV[0].length; Counter++)
					{
						if ((Eleft)&&(enemColE[Counter]))
						{
							left++;
						} else Eleft = false;
						if ((Eright)&&(enemColE[(enemV[0].length-1)-Counter]))
						{
							right++;
						} else Eright = false;
					}
				}
			}
		}
		
		shotY += shotDY;
		if (shotY<=0) 
		{
			shotV = false;
			if (score >= 5) score -= 5; else score = 0;
		}
	}
	
	if (((enemD<0)&&((enemX-18)<((left*(-12))+enemW/2)))||((enemD>0)&&((enemX)>(144+(right*(12))-(enemW/2))))) 
	{
		enemD *= -1;
		enemY += enemDY;
		if (score >= 10) score -= 10; else score = 0;
	}
	enemX += enemD*enemDX;

	for (int Counter=0; Counter<butterFlyV.length; Counter++)
	{
		if (butterFlyV[Counter]) 
		{
			butterFlyX[Counter] += butterFlyDX*butterFlyD[Counter];
			butterFlyY[Counter] += butterFlyDY;
			if ((0>(butterFlyY[Counter]+(butterFlyH/2)))||(0>(butterFlyX[Counter]+(butterFlyW/2)))||(gc.canvasWidth<(butterFlyX[Counter]-(butterFlyW/2)))) butterFlyV[Counter] = false;
		}
	}
	
	for (int Counter=0; Counter<eShotV.length; Counter++)
	{
		if (eShotV[Counter]) 
		{
			eShotY[Counter] += eShotDY;
			
			if ((timeInmortal == 0)&&(Math.abs(eShotX[Counter]-protX)<((eShotW+protW)/2))&&(Math.abs(eShotY[Counter]-protY)<((eShotH+protH)/2)))
			{
				eShotV[Counter] = false;
				if (score>500) score -= 500; else score = 0;
				PlaceText("-500",protX,protY,(byte)1);
				gc.VibraSET(100);
				timeInmortal = 30;
			}
			
			if ((shotV)&&(Math.abs(eShotX[Counter]-shotX)<((eShotW+shotW)/2))&&(Math.abs(eShotY[Counter]-shotY)<((eShotH+shotH)/2))) 
			{
				score += 100;
				gc.SoundRES();
				if (InGameSounds==1) gc.SoundSET(4,1);
				PlaceText("+100",shotX,shotY,(byte)0);
				eShotV[Counter] = false;
				shotV = false;
			}
			if (eShotY[Counter]>gc.canvasHeight) eShotV[Counter] = false;
		} else
		{
			if (enemiesRemain>0)
			{
				if (eShotT[Counter]==0)
				{
					eShotT[Counter] = (600/ActualLevel);
					eShotV[Counter] = true;
					
					int SelectedEnemy = -1;
					int RandomEnemy = RND(18)+1;
					
					while (RandomEnemy>0)
					{
						SelectedEnemy++;
						if (SelectedEnemy>=18) SelectedEnemy=0;
						if (enemV[SelectedEnemy/6][SelectedEnemy%6])
						{
							RandomEnemy--;
						}
					}
					
					eShotX[Counter]	= enemX + 4 + 12 * (SelectedEnemy%6)- (enemW/2);
					eShotY[Counter]	= enemY + 4 + 12 * (SelectedEnemy/6)- (enemH/2);
				} else eShotT[Counter]--;
			}
		}
	}

	if ((gameStatus==102)&&(enemiesRemain == 0))
	{
		score += 50*ActualLevel;
		ActualLevel++;
		shotV = false;
		gc.SoundRES();
		gc.SoundSET(1,1);
		for (int Counter=0; Counter<eShotV.length; Counter++) eShotV[Counter] = false;
		gameStatus = 110;
	}

	if (gameScore < score) gameScore = score;
	
	jugarShow = true;
	return false;
}

public void butterFlyCreate(int BX, int BY)
{
	int Top=0;
	for (int Counter=0; Counter<butterFlyV.length; Counter++)
	{
		if (butterFlyV[Counter]==false)
		{
			Top = Counter;
			Counter = butterFlyV.length;
		} else
		{
			if (butterFlyY[Counter] < butterFlyY[Top]) Top = Counter;
		}
	}
	butterFlyX[Top] = BX;
	butterFlyY[Top] = BY;
	butterFlyD[Top] *= -1;//(BX>(gc.canvasWidth/2)) ? 1 : -1;
	butterFlyV[Top] = true;
}


public void PlaceText(String WText, int X, int Y, byte C)
{
	text = WText;
	textX = X;
	textY = Y;
	textTime = 10;
	if (C==0) textC = (0xFFFFFF); else textC = (0xFF0000);
}
// <=- <=- <=- <=- <=-


int InGameSounds=1;

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