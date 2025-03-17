package com.mygdx.mongojocs.parachutist;

// -----------------------------------------------
// SAIYU Game Base iMode v1.1 Rev.2 (6.2.2004)
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
int  gameSleep;

public void run()
{
	_start(); //MONGOFIX

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

public void gameTick()
{
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
		waitInit(5*1000, 50);
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
		ma.MarcoADD(0x101, menuText[13][0]+score);
		ma.MarcoADD(0x101, menuText[14][0]+record);
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

byte[] prefsData = new byte[] {1,1,0,0,0};

// -------------------
// Prefs INI
// ===================

public void prefsCreate()
{
	prefsData = gc.FS_LoadFile(0,0);

	gameSound=prefsData[0] & 1;
	gameVibra=prefsData[1] & 1;
	gameLevel=prefsData[2];
	record=(short)(((prefsData[4] & 0xFF) <<8) | (prefsData[3] & 0xFF));
}

// -------------------
// Prefs SET
// ===================

public void prefsInit()
{
	prefsData[0]=(byte)gameSound;
	prefsData[1]=(byte)gameVibra;
	prefsData[2]=(byte)gameLevel;
	prefsData[3]=(byte)(record & 0xFF);
	prefsData[4]=(byte)((record>>8) & 0xFF);

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

//int protX;
//int protY;
public static final int MAX_SHOTS = 4;
public static final int MAX_FOES = 6;

Cannon ca;
Shot sh[];
Foe fo[];
int cnt, lifes, lastExtraLife;
short score, record;

// -------------------
// Jugar INI
// ===================

public void jugarCreate()
{
	jugarShow = false;
	
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

public void jugarInit()
{
	gc.jugarInit_Gfx();
	
	Foe.GROUNDY = gc.canvasHeight-15;
	
	ca = new Cannon(gc.canvasWidth/2,gc.canvasHeight-10);
	sh = new Shot[MAX_SHOTS];	
	fo = new Foe[MAX_FOES];
	cnt=0; score=0; lifes=4; lastExtraLife=1;

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
	//protX += keyX;
	//protY += keyY;
	cnt++;
	
	if (score/100>=lastExtraLife) {gc.SoundSET(2,1); lifes++; lastExtraLife++;}
		
	ca.update(keyX);	
	
	if(keyMisc==5 && lastKeyMisc!=5){
		
		addShot(ca.x+(Trig.cos(ca.angle)>>6),ca.y-(Trig.sin(ca.angle)>>6),ca.angle);			
		ca.shotTime = 6;		
	}
	
	for(int i=0; i<sh.length; i++)
		if(sh[i]!=null)
			if(sh[i].out){if(score>0) score--; sh[i]=null; }
			else {
				sh[i].update();
				if(sh[i].x<-4 || sh[i].x>=gc.canvasWidth+4 || sh[i].y<-4) sh[i].out=true;
			}
			
	for(int i=0; i<fo.length; i++)
		if(fo[i]!=null)			
			if(fo[i].out){ 
				if(fo[i].state==Foe.CRUSHED) {lifes--;} //else 
				fo[i]=null;
			}else{ 
				fo[i].update(gc);			
				for(int j=0; j<sh.length; j++)
					if(sh[j]!=null)
						if(fo[i].collide(sh[j])){
							fo[i].setState(Foe.PARACHUTE_TAKE);
							sh[j] = null;									
							score += 5;
							if(record<score) record = score;
						}
			}				
			
	if(RND(300)<(cnt/200)+1)
		addFoe(10+RND(gc.canvasWidth-20),-16);
			
	
	jugarShow = true;

	if(lifes<0) {gc.SoundSET(3,1); return true;}
	else return false;
}

// <=- <=- <=- <=- <=-

void addShot(int x, int y, int angle)
{
	int i = 0;
	
	while(i<sh.length && sh[i]!=null){
		
		i++;
	}
	
	if(i<sh.length) sh[i]=new Shot(x,y,angle);
}

void addFoe(int x, int y)
{
	int i = 0;
	
	while(i<fo.length && fo[i]!=null){
		
		i++;
	}
	
	if(i<fo.length) fo[i]=new Foe(x,y,192+(cnt/12));
}





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