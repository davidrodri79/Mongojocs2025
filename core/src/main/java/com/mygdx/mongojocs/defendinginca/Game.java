package com.mygdx.mongojocs.defendinginca;

// -----------------------------------------------
// SAIYU Defending INCA v 1.0 (18.2.2004)
// ===============================================
// Logica
// ------------------------------------



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.IApplication;


import java.io.InputStream;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

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

public void _start()
{
	Display.setCurrent(gc);

	//run();
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
	_start();

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

public void initRND()
{
	for (int Counter=0; Counter<RND_Data.length; Counter++)
	{
		RND_Data[Counter] ^= (Counter+1)*(System.currentTimeMillis());	
	}	
}

public int RND(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
}

/*public int RND(int Max)
{
	return ((int)((System.currentTimeMillis())%Max));
}*/
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
	if ((gameStatus!=900)&&(timeToChange>0)) timeToChange--;
	switch (gameStatus)
	{
	case 0:
		lastTimeToChange = timeToChange+1;
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
		gameStatus=100; 
	break;
// ===================


// ------------------
// Jugar Bucle
// ------------------
	case 100:
		jugarCreate();
		gc.SoundRES();
		gameStatus++;
	case 101:
		jugarInit();
		textC = (0xFFFFFF);
		textX = gc.canvasWidth/2;
		textY = (gc.canvasHeight/2)-50+10;
		textTime = 50;
		text = protWin+" - "+enemWin+"  " + menuText[13][0]+ActualLevel+" / "+MaxLevel;
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
	
	case 150: //LEVELFINISHED
		timeToChange = 50;
		if ((CPU)&&((Math.abs(explX-enemX)<((explW+enemW)/2))&&(Math.abs(explY-enemY)<((explH+enemH)/2)))) textTime = 50;
		textC = (0xFFFFFF);
		textX = gc.canvasWidth/2;
		textY = gc.canvasHeight/2;
		text = menuText[15][0];
		gameStatus = 160;
		
	case 160: //CHANGE LEVEL
		if (timeToChange==0)
		{
			ActualLevel++;
			gameStatus = 101;	
		} else { if ( !jugarTick() ) {break;} }
	break;
	
	case 200: //Dead
		shotV = false;
		timeInmortal = 100;
		textC = (0xFFFFFF);
		textX = gc.canvasWidth/2;
		textY = gc.canvasHeight/2;
		text = menuText[14][0];
		gameStatus = 205;

	case 205: 
		if (timeToChange==0)
		{
			gameStatus = 210;
			timeToChange = 84;
			gc.SoundRES();
			if (enemWin>protWin) gc.SoundSET(6,1); else gc.SoundSET(5,1);
		}
	
	case 210:
		if (timeToChange>= 83)
		{
			gc.Enem 	= null;
			gc.eShot 	= null;
			gc.shot 	= null;
			gc.Prot 	= null;
			gc.smoke 	= null;
			gc.expl 	= null;
			gc.TileSet 	= null;
			gc.MenuImg 	= null;
			System.gc();
		}
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
		ma.MarcoADD(0x000, menuText[18],10);
		ma.MarcoADD(0x000, menuText[TEXT_MODE], 4, Dificultad);
		ma.MarcoADD(0x000, menuText[19], 5, Rondas);
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
		CPU = true;
		gc.canvasFillInit(0);
	break;
	
	case 10: // Jugar a dobles
		panelDestroy();
		CPU = false;
		gc.canvasFillInit(0);
	break;

	case 1:	// Continuar
		panelDestroy();
		gc.Repintar=true;
		gc.canvasFillInit(0);
	break;

	case 2:	// Sound ON/OFF
		gameSound = ma.getOption();
		if (gameSound==0) {gc.SoundRES();} else if (panelType==0) {gc.SoundSET(0,0);}
	break;
	
	case 4:	// Dificultad
		Dificultad = ma.getOption();
	break;
	
	case 5:	// Rondas
		Rondas = ma.getOption();
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

byte[] prefsData = new byte[] {1,1,0,0};

// -------------------
// Prefs INI
// ===================

public void prefsCreate()
{
	prefsData = gc.FS_LoadFile(0,0);

	gameSound=prefsData[0] & 1;
	gameVibra=prefsData[1] & 1;
	Rondas   =prefsData[2];
	Dificultad=prefsData[3];
}

// -------------------
// Prefs SET
// ===================

public void prefsInit()
{
	prefsData[0]=(byte)gameSound;
	prefsData[1]=(byte)gameVibra;
	prefsData[2]=(byte)Rondas;
	prefsData[3]=(byte)Dificultad;
	
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
	MaxLevel = 3+(Rondas*2);
	protWin = 0;
	enemWin = 0;
	initRND();
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
byte protA;
int protW;
int protH;
int protWin;

int enemX;
int enemY;
byte enemA;
int enemW;
int enemH;
int enemWin;

int explX;
int explY;
int explW;
int explH;
int explGW;
int explGH;
boolean explV;

int shotX;
int shotY;
int shotDX;
int shotDY;
int shotW;
int shotH;
boolean shotV;

int Jump,JumpC,Acc;

int ActScX,LstScY;

int ActualLevel = 1, MaxLevel = 5;
int Dificultad=1;
int Rondas=0;

int TramosFondo=22; 

int sinoidal,incSinus;

int score;

int textX;
int textY;
int textC;
String text="";

int MapSizeX = 16*3; 
int MapSizeY = 32;

int Gravedad = 10;

byte fuerza,viento;

byte ultimoTurno;
byte turno;

byte timeInmortal=30, textTime=0;

boolean MiniMap=true;

boolean CPU=false;

int[] smokeX,smokeY;

int AnguloOK,FuerzaOK;

int RectificarF = 0;
int LevelLostOfPrecision,LostOfPrecision = 0;
boolean StartSound=true;

public void jugarInit()
{
	smokeX = null;
	smokeY = null;
	System.gc();
	
	////////////////////////
	
	CreateMap(MapSizeX,MapSizeY);
	gc.jugarInit_Gfx();
	
	protW	= 20;
	protH	= 21;
	protX	= (12+(8*3)*RND(4));
	protY	= (255-MapHeight[protX/8]*8)-(protH/2);
	protA	= 0;

	enemW	= 24;
	enemH	= 21;
	enemX	= (128*3)-(12+(8*3)*RND(4));
	enemY	= (255-MapHeight[enemX/8]*8)-(enemH/2);
	enemA	= 0;

	explX	= 0;
	explY	= 0;
	explW	= 30;
	explH	= 30;
	explGW	= 26;
	explGH	= 20;
	explV	= false;

	shotX	= 0;
	shotY	= 0;
	shotDX	= 0;
	shotDY	= 0;
	shotW	= 9;
	shotH	= 9;
	shotV	= false;
	
	fuerza  = 0;
	viento	= 0;
	
	turno = (((ActualLevel%2)==1) ? (byte)(1) : (byte)(-1));
	ultimoTurno = (((ActualLevel%2)==1) ? (byte)(1) : (byte)(-1));
	
	viento =  (byte)(RND(41)-20);
	
	sinoidal = 2;
	incSinus = 1;
	
	int MaxSmokes=64;
	smokeX = new int[MaxSmokes];
	smokeY = new int[MaxSmokes];
	
	for (int Counter=0; Counter<smokeX.length; Counter++)
	{
		smokeX[Counter] = 0;
		smokeY[Counter] = 0;
	}
	
	ActScX  = (128*(1-turno))+(128/2);
	
	AnguloOK = calcAngulo();
	FuerzaOK = calcFuerza(AnguloOK);
	EnemAngulo = AnguloOK;
	EnemFuerza = FuerzaOK;
	
	timeToChange = 10;
	keyMisc = 0;
	lastKeyMisc = 0;
	
	RectificarF = 0;
	LevelLostOfPrecision = (2-Dificultad)*15;
	LostOfPrecision = LevelLostOfPrecision;
	
	MiniMap = true;
}

int EnemAngulo,EnemFuerza;

// -------------------
// Jugar RES
// ===================
int lastTimeToChange;

public void jugarRelease()
{
	gc.jugarRelease_Gfx();
}


// -------------------
// Jugar RUN
// ===================
boolean ChangeTurn=false;

int LstScX=0;

public boolean jugarTick()
{
	viento = (byte)(viento + (sinoidal-2)/2);
	if ((turno == 2)||(turno == -2))
	{
		Jump += 2*Acc;	
		JumpC += Acc;
		Acc--;
		
		if (timeToChange == 0)
		{
			if (turno == 2)
			{
				enemWin++;
				gc.SoundRES();	
				if (CPU) gc.SoundSET(2,1); else gc.SoundSET(1,1);
			}	
			if (turno == -2)
			{
				protWin++;
				gc.SoundRES();
				gc.SoundSET(1,1);
			}
			if (ActualLevel < MaxLevel) gameStatus = 150; else gameStatus = 200;
			
		}
	}
	
	if ((ChangeTurn)&&((turno/2)==0))
	{
		//try	{ Thread.sleep(200); } catch(Exception e) {}
		turno = (byte)(ultimoTurno*(-1));
		ultimoTurno = turno;
		fuerza = 10;
		keyMisc = 0;	
		gc.Repintar = true;
		lastKeyMisc = 0;	
		timeToChange = 10;	
		int Angle;
		Angle = AnguloOK;
		if (turno == 1)
		{
			if (shotX>(4*100))
			{
				if ((shotDY <= 0)&&(((shotY/100)>protY)||(Math.abs(((shotY/100)-protY)*shotDX-viento)<Math.abs(((shotX/100)-protX)*shotDY))))
				{
					/*if ((shotX/100)<protX) RectificarF -= (((protX-(shotX/100))*100)/400);
					else RectificarF += ((((shotX/100)-protX)*100)/400);*/
					//RectificarF -= ((((protY-(shotY/100))*100)*shotDX)/(400*shotDY))+(((protX-(shotX/100))*100)/400);
					if (shotDY == 0) shotDY = 1;
					RectificarF -= (((protY*100-shotY)*viento)/((-3)*2*shotDY))+(((protY*100-shotY)*shotDX)/(400*shotDY))+((protX*100-shotX)/400);
				}
			} else
			{
				if (AnguloOK>16)
				{
					AnguloOK = (AnguloOK+AnguloOK+15)/3;
					FuerzaOK = calcFuerza(AnguloOK);
					RectificarF -= 10/*((shotDY>=0)?-10:0)*/;
					//LostOfPrecision = LevelLostOfPrecision;
				} else
				{
					RectificarF -= (((protY*100-shotY)*viento)/((-3)*2*shotDY))+(((protY*100-shotY)*shotDX)/(400*shotDY))+((protX*100-shotX)/400)+10;
				}
			}
		}
		if (LostOfPrecision>0) LostOfPrecision--;
		EnemAngulo = AnguloOK+RND(LostOfPrecision)-(LostOfPrecision/2);
		EnemFuerza = FuerzaOK+RectificarF+RND(LostOfPrecision)-(LostOfPrecision/2);
		while (((EnemFuerza > 100)||(EnemFuerza < 30)||((AnguloOK>40)&&(viento>0)))&&(EnemAngulo>16))
		{
			AnguloOK = (AnguloOK+AnguloOK+15)/3;
			FuerzaOK = calcFuerza(AnguloOK);
			//LostOfPrecision = LevelLostOfPrecision;
			RectificarF = 0;
			EnemAngulo = AnguloOK+RND(LostOfPrecision)-(LostOfPrecision/2);
			EnemFuerza = FuerzaOK+RectificarF+RND(LostOfPrecision)-(LostOfPrecision/2);
		}
		if (Angle!=AnguloOK) LostOfPrecision+=2;
		if (EnemFuerza > 100) EnemFuerza = 100;
		if (EnemFuerza < 30) EnemFuerza = 30;
		if (EnemAngulo > 64) EnemAngulo = 64;
		if (EnemAngulo < 15) EnemAngulo = 15;
	}
	ChangeTurn = false;
	
	if ((lastTimeToChange == 1)&&((turno==1)||(turno==-1))) ActScX = (128*(1-turno))+(128/2);
	
	if (timeInmortal>0) timeInmortal--;
	if (textTime>0) textTime--;
	if ((sinoidal==0)||(sinoidal==4)) incSinus *= -1;
	sinoidal += incSinus;
	
	if ((timeToChange==0)&&((turno==1)&&(keyX != 0))) ActScX  += keyX*30;
	if ((timeToChange==0)&&((!CPU)&&(turno==-1)&&(keyX != 0))) ActScX  += keyX*30;
	
	if ((lastKeyMisc == 10)&&(keyMisc != 10))
	{
		MiniMap = !MiniMap;
		gc.Repintar=true;
	}
	
	if ((timeToChange==0)&&((turno==0)||(turno==-2)||(turno==2)))
	{
		ActScX = (shotX/100);
	}
	
	if (turno == 1) protA -= keyY;
	if ((!CPU)&&(turno == -1)) enemA -= keyY;
	if (protA > 65) protA = 65;
	if (protA < -20)  protA = -20;
	if (enemA > 65) enemA = 65;
	if (enemA < -20)  enemA = -20;
	
	
	//if (ActScX>(128*3-60)) ActScX = (128*3-65);
	if (ActScX<60) ActScX = 60;
	
	/*if ((gameStatus==102)&&(turno==1)&&(lastKeyMisc!=5)&&(keyMisc == 5)) fuerza=10;
	if ((!CPU)&&(gameStatus==102)&&(turno==-1)&&(lastKeyMisc!=5)&&(keyMisc == 5)) fuerza=10;*/
	
	if ((timeToChange==0)&&(gameStatus==102)&&(turno==1)&&(keyMisc == 5)&&(UsedSmokes==0))
	{
		shotV = true;
		shotX = (protX*100+(Trig.cos(protA)*30*100/1024));
		shotY = (protY*100-(Trig.sin(protA)*30*100/1024));
		if (StartSound) gc.SoundSET(7,1);
		StartSound = false;
		
		int Altura;
		
		Altura = 100*8*MapHeight[(((shotX/100)-4)/8)];
		if (Altura<(100*8*MapHeight[(((shotX/100)+4)/8)])) Altura = (100*8*MapHeight[(((shotX/100)+4)/8)]);
		
		Altura = Altura + (4*100);
		
		if (((Math.abs((shotX/100)-enemX)<((shotW+enemW)/2))&&(Math.abs((shotY/100)-enemY)<((shotH+enemH)/2)))||
		   ((Math.abs((shotX/100)-protX)<((shotW+protW)/2))&&(Math.abs((shotY/100)-protY)<((shotH+protH)/2)))||
		   (shotY>((255*100)-Altura)))
		{
			shotX = (protX + (protW+explW)/2)*100;
			StartSound = true;
			DoExplosion();	
		}
		
		/*if ((MapInf[(shotX/800)+(48*(shotY/800))]>0)&&(MapInf[(shotX/800)+(48*(shotY/800))]<66))
		{
			shotX = (protX + (protW+explW)/2)*100;
			DoExplosion();
		}*/
		
		shotDX = 0;
		shotDY = 0;
		
		if (fuerza<100) fuerza++;
	}
	if ((timeToChange==0)&&(!CPU)&&(gameStatus==102)&&(turno==-1)&&(keyMisc == 5)&&(UsedSmokes==0))
	{
		shotV = true;
		shotX = (enemX*100-(Trig.cos(enemA)*30*100/1024));
		shotY = (enemY*100-(Trig.sin(enemA)*30*100/1024));
		if (StartSound) gc.SoundSET(7,1);
		StartSound = false;
		
		int Altura;
		
		Altura = 100*8*MapHeight[(((shotX/100)-4)/8)];
		if (Altura<(100*8*MapHeight[(((shotX/100)+4)/8)])) Altura = (100*8*MapHeight[(((shotX/100)+4)/8)]);
		
		Altura = Altura + (4*100);
		
		if (((Math.abs((shotX/100)-enemX)<((shotW+enemW)/2))&&(Math.abs((shotY/100)-enemY)<((shotH+enemH)/2)))||
		   ((Math.abs((shotX/100)-protX)<((shotW+protW)/2))&&(Math.abs((shotY/100)-protY)<((shotH+protH)/2)))||
		   (shotY>((255*100)-Altura)))
		{
			shotX = (enemX - (enemW+explW)/2)*100;
			StartSound = true;
			DoExplosion();	
		}
		
		/*if ((MapInf[(shotX/800)+(48*(shotY/800))]>0)&&(MapInf[(shotX/800)+(48*(shotY/800))]<66))
		{
			shotX = (enemX - (enemW+explW)/2)*100;
			DoExplosion();
		}*/
		
		shotDX = 0;
		shotDY = 0;
		
		if (fuerza<100) fuerza++;
	}
	
	if ((timeToChange==0)&&(gameStatus==102)&&(turno==1)&&(lastKeyMisc == 5)&&(keyMisc != 5)&&(shotV))
	{
		shotDX = (10*fuerza*Trig.cos(protA))/1024;
		shotDY = (10*fuerza*Trig.sin(protA))/1024;
		turno = 0;
		gc.SoundRES();
		gc.SoundSET(4,1);
		StartSound = true;
	}
	if ((timeToChange==0)&&(!CPU)&&(gameStatus==102)&&(turno==-1)&&(lastKeyMisc == 5)&&(keyMisc != 5)&&(shotV))
	{
		shotDX = (0-(10*fuerza*Trig.cos(enemA))/1024);
		shotDY = (10*fuerza*Trig.sin(enemA))/1024;
		turno = 0;
		gc.SoundRES();
		gc.SoundSET(4,1);
		StartSound = true;
	}
	
	if ((timeToChange==0)&&(turno==-1)&&(CPU))
	{
		if (enemA<EnemAngulo) enemA++;
		if (enemA>EnemAngulo) enemA--;
		if ((enemA==EnemAngulo)&&(shotV==false)&&(UsedSmokes==0))
		{
			shotV = true;
			shotX = (enemX*100-(Trig.cos(enemA)*30*100/1024));
			shotY = (enemY*100-(Trig.sin(enemA)*30*100/1024));
			gc.SoundSET(7,1);
		}
		if (shotV)
		{
			int Altura;
		
			Altura = 100*8*MapHeight[(((shotX/100)-4)/8)];
			if (Altura<(100*8*MapHeight[(((shotX/100)+4)/8)])) Altura = (100*8*MapHeight[(((shotX/100)+4)/8)]);
			
			Altura = Altura + (4*100);
			
			if (((Math.abs((shotX/100)-enemX)<((shotW+enemW)/2))&&(Math.abs((shotY/100)-enemY)<((shotH+enemH)/2)))||
			   ((Math.abs((shotX/100)-protX)<((shotW+protW)/2))&&(Math.abs((shotY/100)-protY)<((shotH+protH)/2)))||
			   (shotY>((255*100)-Altura)))
			{
				shotX = (enemX -(enemW+explW)/2)*100;
				DoExplosion();	
			} else
			{
				if (UsedSmokes==0)
				{
					shotDX = 0;
					shotDY = 0;
					
					if (fuerza<EnemFuerza) fuerza++;
					
					if (fuerza==EnemFuerza)
					{
						shotDX = (0-(10*fuerza*Trig.cos(enemA))/1024);
						shotDY = (10*fuerza*Trig.sin(enemA))/1024;
						turno = 0;
						gc.SoundRES();
						gc.SoundSET(4,1);
						EnemAngulo = AnguloOK+RND(10)-5;
						EnemFuerza = FuerzaOK+RND(50)-25;	
					}
				}
			}	
		}
	}
	
	if (shotV)
	{
		shotX += shotDX;
		shotY -= shotDY;
		
		shotDX += (viento/3);
		shotDY -= Gravedad; 
		
		if ((shotX<((128*3-4)*100))&&(shotX > (4*100)))
		{
			int Altura;
		
			Altura = 100*8*MapHeight[(((shotX/100)-4)/8)];
			if (Altura<(100*8*MapHeight[(((shotX/100)+4)/8)])) Altura = (100*8*MapHeight[(((shotX/100)+4)/8)]);
			
			Altura = Altura + (4*100);
		
			if (((Math.abs((shotX/100)-enemX)<((shotW+enemW)/2))&&(Math.abs((shotY/100)-enemY)<((shotH+enemH)/2)))||
			   ((Math.abs((shotX/100)-protX)<((shotW+protW)/2))&&(Math.abs((shotY/100)-protY)<((shotH+protH)/2)))||
			   (shotY>((255*100)-Altura)))
			{
				DoExplosion();	
			}
		} else 
		{
			shotV = false;
			ChangeTurn = true;
		}
	}
	
	if ((false)&&(gameStatus==102))
	{
		ActualLevel++;
		shotV = false;
		gc.SoundRES();
		gc.SoundSET(1,1);
		gameStatus = 110;
	}

	if (gameScore < score) gameScore = score;

	int ActScXT,ActScYT,ActScY;
	if (shotV) ActScY = (shotY/100); else ActScY = 256;
	
	ActScXT = (ActScX/60)*60;
	ActScYT = (ActScY/60)*60;
	if (ActScYT <= 0) ActScYT = 64;
	if (ActScYT > 256) ActScYT = 256;
	
	if (ActScY <= 0) ActScY = 64;
	if (ActScY > 256) ActScY = 256;
	
	if (ActScXT < 64) ActScXT = 64;
	if (ActScXT > 128*3-70) ActScXT = 128*3-63;
	if (ActScX >= 128*3-70) 
	{
		ActScXT = 128*3-63;
		ActScX = 128*3-63;
	}
	
	gc.sc.ScrollRUN_Centro_Max(ActScXT,ActScYT);
	
	jugarShow = true;
	
	/*if (LstScX != ActScX) gc.Repintar = true;*/
	LstScX = ActScX;
	
	lastTimeToChange = timeToChange;
		
	return false;
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




// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------


byte[] MapInf;
byte[] MapCol;
byte[] MapHeight;

public void CreateMap(int W, int H)
{
	byte[][] Map;
	
	MapInf 		= new byte[W*H];
	MapCol 		= new byte[W*H];
	Map 		= new byte[H][W];
	MapHeight	= new byte[W];
	///////////////////////////////////////////////

	for (int Counter=0; Counter<(W*H); Counter++)
	{
		MapInf[Counter]=0;
	} 
	
	///////////////////////////////////////////////
	
	for (int Col=0; Col<W; Col+=3)
	{
		MapHeight[Col] 	 = (byte)(2+RND(12));
		MapHeight[Col+1] = MapHeight[Col];
		MapHeight[Col+2] = MapHeight[Col];
		
		PutColumns(Col,32-MapHeight[Col],MapHeight[Col]-2);
		PutColumn(Col,30,-1);
	}	
	
	for (int Nub=0; Nub<10; Nub++)
	{
		int ActX = RND(45);
		int ActY = RND(10);
		while ((MapInf[ActX+ActY*48]!=0)||(MapInf[ActX+1+ActY*48]!=0)||(MapInf[ActX+2+ActY*48]!=0)||
		       (MapInf[ActX+(ActY+1)*48]!=0)||(MapInf[ActX+1+(ActY+1)*48]!=0)||(MapInf[ActX+2+(ActY+1)*48]!=0))
		{
			ActX = RND(45);
			ActY = RND(10);
		}
		PutColumn(ActX,ActY,6);
	}
}

public void PutColumns(int X, int Y, int H)
{
	while (H>0)
	{
		int MaxRND = 6;
		if (H<4) MaxRND = 4;
		if (H<3) MaxRND = 2;
		if (H<2) MaxRND = 1;
		int ActCol = RND(MaxRND);
		int ActH = 1;
		if (ActCol > 0) ActH = 2;
		if (ActCol > 1) ActH = 3;
		if (ActCol > 3) ActH = 4;
		PutColumn(X,Y,ActCol);
		Y += ActH;
		H -= ActH;
	}	
}

public void PutColumn(int X, int Y, int C)
{
	switch (C)
	{
		case 0:   PutTiles (X,Y,51,1);	
		break;
		case 1:   PutTiles (X,Y,33,2);	
		break;
		case 2:   PutTiles (X,Y,6,3);	
		break;
		case 3:   PutTiles (X,Y,3,3);	
		break;
		case 4:   PutTiles (X,Y,27,4);	
		break;
		case 5:   PutTiles (X,Y,30,4);	
		break;
		case 6:   
			PutTiles (X,Y,66,1);	
			PutTiles (X,Y+1,69,1);
		break;
		default : PutTiles (X,Y,9,2);	
	}
}

public void PutTiles(int X, int Y, int P, int H)
{
	for (int Counter=0; Counter<H; Counter++)
	{
		MapInf[X+((Y+Counter)*48)] = (byte)(P + (9*Counter));
		MapInf[X+1+((Y+Counter)*48)] = (byte)(P +1+ (9*Counter));
		MapInf[X+2+((Y+Counter)*48)] = (byte)(P +2+ (9*Counter));
	}	
}


public void DoExplosion()
{
	gc.SoundRES();
	gc.SoundSET(3,1);
	shotV = false;
	explV = true;
	gc.ActFrame = 0;
	explX = (shotX/100);
	explY = (shotY/100);
	
	gc.VibraSET(50);
	
	int H=0;
	
	for (int Col=((explX-(explW/2))/8); Col<((explX+(explW/2))/8); Col++)
	{
		if ((Col>=0)&&(Col<48))
		{
			int Row;
			H = 0;
			for (Row=((explY-(explH/2))/8); Row<(((explY+(explH/2))/8)+1); Row++)
			{
				if ((Row<30)&&(Row>=0))
				{
					if (MapInf[Col+(Row*48)]!=0) H++;
					MapInf[Col+(Row*48)] = 0;
				}
			}
			for (Row=((explY+(explH/2))/8);Row>(31-MapHeight[Col]);Row--)
			{
				if (Row<30) smokeCreate(Col,Row);
			}
			MapHeight[Col] -= H;
			Acumulate(Col);
			protY	= (255-MapHeight[protX/8]*8)-(protH/2);
			if (protY>((255-MapHeight[(protX/8)-1]*8)-(protH/2))) protY = (255-MapHeight[(protX/8)-1]*8)-(protH/2);
			if (protY>((255-MapHeight[(protX/8)+1]*8)-(protH/2))) protY = (255-MapHeight[(protX/8)+1]*8)-(protH/2);
			enemY	= (255-MapHeight[enemX/8]*8)-(enemH/2);
			if (enemY>((255-MapHeight[(enemX/8)-1]*8)-(enemH/2))) enemY = (255-MapHeight[(enemX/8)-1]*8)-(enemH/2);
			if (enemY>((255-MapHeight[(enemX/8)+1]*8)-(enemH/2))) enemY = (255-MapHeight[(enemX/8)+1]*8)-(enemH/2);
		}
	}
	
	if ((Math.abs(explX-protX)<((explW+protW)/2))&&(Math.abs(explY-protY)<((explH+protH)/2)))
	{
		turno = 2;	
		MiniMap = false;
		Jump = 0;
		JumpC = 0;
		Acc = 3;
		timeToChange = 60;
		keyMisc = 0;	
		lastKeyMisc = 0;	
		gc.Repintar = true;
		gc.VibraSET(300);
	}
	
	if ((Math.abs(explX-enemX)<((explW+enemW)/2))&&(Math.abs(explY-enemY)<((explH+enemH)/2)))
	{
		turno = -2;
		MiniMap = false;
		Jump = 0;
		JumpC = 0;
		Acc = 3;
		timeToChange = 60;
		keyMisc = 0;	
		lastKeyMisc = 0;
		gc.Repintar = true;	
		gc.VibraSET(300);
	}
}

public void Acumulate(int C)
{
	for (int R=31; R>0; R--)
	{
		if ((MapInf[C+(R*48)] == 0)&&(MapInf[C+((R-1)*48)] != 0)&&(MapInf[C+((R-1)*48)] < 66))
		{
			MapInf[C+(R*48)] = MapInf[C+((R-1)*48)];
			MapInf[C+((R-1)*48)] = 0;
			R = 31;
		}
	}
}

int UsedSmokes=0;

public void smokeCreate(int X, int Y)
{
	if (UsedSmokes<smokeX.length)
	{
		smokeX[UsedSmokes] = X*8;
		smokeY[UsedSmokes] = Y*8;
		UsedSmokes++;
	}
}

public int calcAngulo()
{
	int MaxAngle=5;
	int ActAngleP=5;
	int ActAngleE=5;
	for (int Counter = (1+protX/8); Counter<(enemX/8); Counter++)
	{
		if (MapHeight[Counter]>((255-protY)/8)) ActAngleP = Trig.acos(((Counter-protX/8)*1024)/(SquareRoot.sqrt(((Counter-protX/8)*(Counter-protX/8))+((MapHeight[Counter]+1-((255-protY)/8))*(MapHeight[Counter]+1-((255-protY)/8))))));
		if (MapHeight[Counter]>((255-enemY)/8)) ActAngleE = Trig.acos(((enemX/8-Counter)*1024)/(SquareRoot.sqrt(((enemX/8-Counter)*(enemX/8-Counter))+((MapHeight[Counter]+1-((255-enemY)/8))*(MapHeight[Counter]+1-((255-enemY)/8))))));
		if (MaxAngle<ActAngleP) MaxAngle=ActAngleP;
		if (MaxAngle<ActAngleE) MaxAngle=ActAngleE;
	}
	if (MaxAngle<50) MaxAngle += 4; else MaxAngle = 54;
	return MaxAngle;
}

public int calcFuerza(int Angulo)
{
	int MejorFuerza,ActFuerza,MinError,ActError,a,b,Ax,Yp,Ye;
	Ax = (viento*2);
	MejorFuerza = 35;
	MejorFuerza = SquareRoot.sqrt((((enemX-(Trig.cos(Angulo)*30/1024))-protX)*100*Gravedad*Gravedad)/((2*((Gravedad*100*Trig.sin(Angulo)*Trig.cos(Angulo))-(9*Ax*Trig.sin(Angulo)*Trig.sin(Angulo))))/(1024*1024)));
	if (MejorFuerza<10) MejorFuerza = 10;
	if (MejorFuerza>100) MejorFuerza = 100;
	return (MejorFuerza);	
}

byte SoundsInGame=0;

// **************************************************************************//
} // Final Clase MIDlet
// **************************************************************************//