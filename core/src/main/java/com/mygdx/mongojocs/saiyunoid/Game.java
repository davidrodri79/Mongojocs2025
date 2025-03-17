package com.mygdx.mongojocs.saiyunoid;

// -----------------------------------------------
// SAIYU Game Base iMode v1.1 Rev.2 (6.2.2004)
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

		gc.gameDraw();

		gameSleep=(40-(int)(System.currentTimeMillis()-gameMillis));
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
	FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+Nombre);
	byte[] bytes = file.readBytes();

	for(int i = 0; i < buffer.length && i < bytes.length; i++)
		buffer[i] = bytes[i];
	/*System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
	is.read(buffer, 0, buffer.length);
	is.close();
	}catch(Exception exception) {}
	System.gc();*/
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
//		cheats();

		if (keyMenu==-1 && keyMenu!=lastKeyMenu) {panelInit(1); break;}

		if ( !jugarTick() ) {break;}

		jugarRelease();

		gameStatus = 101;

		switch (FaseExit)
		{
		case 1:
//			gc.canvasFillInit(0); gc.canvasImg = gc.FS_LoadImage(3, JugarLevel % gc.ChatisMAX); gameStatus=110;	// Version CHATIS
			if (++JugarLevel > gc.FasesMAX) {JugarLevel = 0;}
			Nivel++;
		break;

		case 2:
			JugarNew = false;
			if (--JugarVidas < 0) {gameStatus = 103;}
		break;
		}

		FaseExit=0;
	break;

	case 103:
		jugarDestroy();
		gameStatus++;
	break;

	case 104:	// GAME OVER
		panelInit(5);
		waitInit(3*1000, 50);
	break;

	case 110:	// Espera a pulsar una tecla
		if (( keyMenu!=0 && lastKeyMenu==0 ) || ( keyMisc!=0 && lastKeyMisc==0 )) {gameStatus=101;}
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
		gc.JugarTodoPaint = true;
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

// -------------------
// WaitNow
// ===================

public void waitNow(int Tiempo)
{
	try {
		Thread.sleep(Tiempo);
	} catch (Exception e) {}
}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

boolean jugarShow = false;

int JugarLevel;
int JugarVidas;
boolean JugarNew;

int Nivel;

// -------------------
// Jugar INI
// ===================

public void jugarCreate()
{
	jugarShow = false;

	JugarLevel=0;
	Nivel = 1;

	JugarVidas=3;

	gc.jugarCreate_Gfx();

	JugarNew=true;

	FaseINI();
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
	FaseSET();

	gc.jugarInit_Gfx();
}


// -------------------
// Jugar RES
// ===================

public void jugarRelease()
{
	jugarShow = false;

	gc.jugarRelease_Gfx();
}


// -------------------
// Jugar RUN
// ===================

public boolean jugarTick()
{
	FaseRUN();

	if (FaseExit!=0) {return true;}

	jugarShow = true;

	return false;
}

// <=- <=- <=- <=- <=-







// *******************
// -------------------
// Fase - Engine
// ===================
// *******************

int ParedPixeX;
int ParedPixeY;

byte[] ParedMapa;

int FaseExit;

// -------------------
// Fase INI
// ===================

public void FaseINI()
{

//	loadFile(AnimMap, "/Data/Anims.map");

}


// -------------------
// Fase END
// ===================

public void FaseEND()
{
	BolaEND();
	CapsulaEND();
	DisparoEND();
	DestelloEND();

	System.gc();
}


// -------------------
// Fase SET
// ===================

public void FaseSET()
{
	if (JugarNew)
	{
	ParedMapa = new byte[gc.ParedSizeX*(gc.ParedSizeY+8)];
	loadFile(ParedMapa, "/Fases/Fase"+JugarLevel+".map");
	}

	JugarNew=true;

	FaseExit=0;

	BolaINI();
	CapsulaINI();
	DisparoINI();
	DestelloINI();

	PalaSET();
}


// -------------------
// Fase RUN
// ===================

public void FaseRUN()
{
	if ( ParedCheck() )
	{
		jugarShow = true;
		gc.gameDraw();

		gc.SoundSET(1,1);

		gc.canvasTextInit(menuText[14][0], 0,0, 0xffffff, gc.TEXT_VCENTER|gc.TEXT_HCENTER|gc.TEXT_BOLD|gc.TEXT_MEDIUM|gc.TEXT_OUTLINE);
		gc.gameDraw();

		waitNow(3*1000);
		gc.JugarTodoPaint = true;

		FaseExit=1;
		return;
	}

	DestelloRUN();

	PalaRUN();

	BolaRUN();
	CapsulaRUN();
	DisparoRUN();
}


public boolean ParedCheck()
{
	for (int i=0 ; i<ParedMapa.length ; i++)
	{
	if (ParedMapa[i]!=5 && ParedMapa[i]!=0) {return false;}
	}
	return true;
}

int ParedDir;

public boolean ParedCheck(int X, int Y)
{
	ParedDir = ((Y/gc.TochoSizeY)*gc.ParedSizeX)+(X/gc.TochoSizeX);

	X=(ParedDir%gc.ParedSizeX)*gc.TochoSizeX;
	Y=(ParedDir/gc.ParedSizeX)*gc.TochoSizeY;

	if (ParedMapa[ParedDir] == 5)
	{
	DestelloSET( X , Y , 0 );
	return true;
	}
	else if (ParedMapa[ParedDir] == 1)
	{
	ParedMapa[ParedDir]=4;
	DestelloSET( X , Y , 0 );
	gc.TochoUpdate_Gfx( X , Y , 4);
	return true;
	}
	else if (ParedMapa[ParedDir] != 0)
	{
	ParedMapa[ParedDir]=0;
	CapsulaSET( X , Y );
	DestelloSET( X , Y , 1 );
	gc.TochoUpdate_Gfx( X , Y , 0);
	return true;
	}

	return false;
}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Pala - Engine
// ====================
// ********************

int PalaX;
int PalaSizeX, PalaSizeX_Go;
int PalaMode;
int PalaType;
int PalaFrame;
int PalaBolaDesp;
int PalaCnt;
boolean PalaDisp;
boolean PalaStop;
boolean PalaCatch;
boolean PalaNormal;

// ---------------
// Pala SET
// ===============

public void PalaSET()
{
	PalaMode=0;
	PalaFrame=0;
	PalaDisp=false;
	PalaCatch=false;
	PalaStop=true;
	PalaNormal=true;

	PalaSizeX = gc.SpritesObj[(6*4)+2] + gc.SpritesObj[(7*4)+2];
	PalaSET(1);

	PalaX=(ParedPixeX/2)-(PalaSizeX/2);

	PalaMode=10;
}


public void PalaSET(int Type)
{
	PalaType=Type;

	if (Type==20) {gc.VibraSET(320);  gc.SoundSET(3,1); PalaMode=20; PalaCnt=2; PalaSizeX_Go = gc.SpritesObj[(6*4)+2] + gc.SpritesObj[(7*4)+2];}

	switch (PalaMode)
	{
	case 0:
		if (Type!=3)
		{
		PalaSizeX_Go=gc.PalaSizes[Type];
		} else {
		PalaSizeX_Go = gc.SpritesObj[(6*4)+2] + gc.SpritesObj[(7*4)+2];
		PalaMode=1;
		}
	break;

	case 3:
		if (Type!=3)
		{
		PalaSizeX_Go = gc.SpritesObj[(6*4)+2] + gc.SpritesObj[(7*4)+2];
		PalaMode++;
		}
	break;
	}
}


// ---------------
// Pala RUN
// ===============

public void PalaRUN()
{

	switch (PalaMode)
	{
	case 1:	// Pala Normal Cerrandose para Disparo
		if (PalaSizeX==PalaSizeX_Go) {PalaSizeX_Go=gc.PalaSizes[PalaType]; PalaNormal=false; PalaMode++;}
	break;

	case 2:	// Pala Disparo Abriendose
		if (PalaSizeX==PalaSizeX_Go) {PalaMode++;}
	break;

	case 3:	// Pala Disparo Running
		if (PalaFrame == 3) {
		if (keyMisc==5 && lastKeyMisc!=5) {PalaDisp=true;}
		} else {PalaFrame++;}
	break;

	case 4:	// Pala Disparo Cerrandose
		if (PalaFrame==0) {
		if (PalaSizeX==PalaSizeX_Go) {PalaSizeX_Go=gc.PalaSizes[PalaType];  PalaNormal=true; PalaMode=0;}
		} else {PalaFrame--;}
	break;


	case 10:// Pala Abriendose por Primera vez
		if (PalaSizeX==PalaSizeX_Go)
			{
			PalaBolaDesp=(PalaSizeX/2)-(gc.BolaSizeX/2);
			BolaSET(PalaX+PalaBolaDesp, gc.PalaY-gc.BolaSizeY, gc.BolaSpeed, -gc.BolaSpeed);

			gc.SoundSET(4,1);

			gc.canvasTextInit(menuText[13][0]+" "+Nivel, 0,0, 0xffffff, gc.TEXT_VCENTER|gc.TEXT_HCENTER|gc.TEXT_BOLD|gc.TEXT_LARGER|gc.TEXT_OUTLINE);
			gc.gameDraw();

			waitNow(3*1000);
			gc.JugarTodoPaint = true;

			PalaMode=0;
			}
	break;


	case 20:// Pala Cerrandose para Morir
		if (PalaSizeX==PalaSizeX_Go) {PalaSizeX_Go=gc.PalaSizes[1]; PalaMode++;}
	break;

	case 21:// Pala Abriendose para Morir
		if (PalaSizeX==PalaSizeX_Go) {PalaSizeX_Go=PalaSizeX_Go = gc.SpritesObj[(6*4)+2] + gc.SpritesObj[(7*4)+2]; if (--PalaCnt>0) {PalaMode--;} else {PalaMode=22;}}
	break;

	case 22:// Retardo Tras Morir
		if (PalaCnt++ > 20) {FaseExit=2; PalaMode++;}
	break;
	}


	if (PalaDisp)
	{
	PalaDisp=false;
	DisparoSET(PalaX, gc.PalaY-gc.SpritesObj[(4*4)+3]);
	}


	if (PalaMode < 10)
	{
	PalaX+=keyX*gc.PalaSpeed;
	}

	if (PalaSizeX < PalaSizeX_Go) {PalaSizeX+=2; PalaX--;}
	else
	if (PalaSizeX > PalaSizeX_Go) {PalaSizeX-=2; PalaX++;}

	if (PalaX < 0) {PalaX=0;} else if (PalaX+PalaSizeX > ParedPixeX) {PalaX=ParedPixeX-PalaSizeX;}
}

// <=- <=- <=- <=- <=-










// ********************
// --------------------
// Bola - Engine
// ====================
// ********************

final int BolaMAX=3;
int[][] Bolas;
int BolaC=0;

// ---------------
// Bola SET
// ===============

public void BolaSET(int CoorX, int CoorY, int AddX, int AddY)
{
	if (BolaC == BolaMAX) {return;}

	int i=0; for (; i<BolaMAX ; i++) {if (Bolas[i] == null) {break;}}

	Bolas[i] = new int[5];

	Bolas[i][0] = CoorX;
	Bolas[i][1] = CoorY;
	Bolas[i][2] = AddX;
	Bolas[i][3] = AddY;
	Bolas[i][4] = 0;

	BolaC++;
}


// ---------------
// Bola Play
// ===============

public boolean BolaPlay(int[] Data)
{
	if (PalaStop)
	{
	Data[0]=PalaX+PalaBolaDesp;
	Data[1]=gc.PalaY-gc.BolaSizeY;
	if (keyMisc==5 && lastKeyMisc!=5) {PalaStop=false;} else {return false;}
	}

	int SumaX=Data[2]<0? 0:gc.BolaSizeX;
	int SumaY=Data[3]<0? 0:gc.BolaSizeY;

	if ((Data[0] + Data[2] < 0)
	||	(Data[0] + Data[2] + gc.BolaSizeX > ParedPixeX)
	||	(ParedCheck(Data[0]+Data[2]+SumaX, Data[1]+SumaY) ))
	{/*gc.SoundSET(5,1);*/ Data[2]*=-1; if (++Data[4]>20) {Data[4]=0; BolaGrado(Data);}}

	Data[0]+=Data[2];


	if ((Data[1] + Data[3] < 0)
//	||	(Data[1] + Data[3] + gc.BolaSizeY > ParedPixeY)
	||	(ParedCheck(Data[0]+SumaX, Data[1]+Data[3]+SumaY) ))
	{/*gc.SoundSET(5,1);*/ Data[3]*=-1; if (++Data[4]>20) {Data[4]=0; BolaGrado(Data);}}

	if (Data[1] > ParedPixeY) {if (BolaC==1) {PalaSET(20);} return true;}	// Bola Over;

	Data[1]+=Data[3];



	if (Data[3] > 0 && colision(Data[0], Data[1],  gc.BolaSizeX, gc.BolaSizeY,  PalaX, gc.PalaY, PalaSizeX, gc.PalaSizeY/3 ) )
	{
//		gc.SoundSET(5,1); 

		Data[4]=0;

		if (PalaCatch) {PalaStop=true; PalaBolaDesp=Data[0]-PalaX;}

		int X = (((Data[0]+(gc.BolaSizeX/2))-PalaX)*256) / ((PalaSizeX*256)/6);

		if (X < 0) {X=0;} else if (X > 5) {X=5;}

		switch (X)
		{
		case 0:
			Data[2]=-gc.BolaSpeed;
			Data[3]=-gc.BolaSpeed/2;
		break;

		case 1:
			Data[2]=-gc.BolaSpeed;
			Data[3]=-gc.BolaSpeed;
		break;

		case 2:
			Data[2]=-gc.BolaSpeed/2;
			Data[3]=-gc.BolaSpeed;
		break;

		case 3:
			Data[2]=gc.BolaSpeed/2;
			Data[3]=-gc.BolaSpeed;
		break;

		case 4:
			Data[2]=gc.BolaSpeed;
			Data[3]=-gc.BolaSpeed;
		break;

		case 5:
			Data[2]=gc.BolaSpeed;
			Data[3]=-gc.BolaSpeed/2;
		break;
		}
	}

	return false;
}


public void BolaGrado(int[] Data)
{
	int X= Data[2]<0? -1:1; Data[2]*=X;
	int Y= Data[3]<0? -1:1; Data[3]*=Y;

	if (Data[2]==gc.BolaSpeed && Data[3]==gc.BolaSpeed)   {Data[2]=(gc.BolaSpeed)*X; Data[3]=(gc.BolaSpeed/2)*Y;}
	else
	if (Data[2]==gc.BolaSpeed && Data[3]==gc.BolaSpeed/2) {Data[2]=(gc.BolaSpeed/2)*X; Data[3]=(gc.BolaSpeed)*Y;}
	else
	if (Data[2]==gc.BolaSpeed/2 && Data[3]==gc.BolaSpeed) {Data[2]=(gc.BolaSpeed)*X; Data[3]=(gc.BolaSpeed)*Y;}
}

// ---------------
// Bola INI
// ===============

public void BolaINI()
{
	BolaC = 0;
	Bolas = new int[BolaMAX][];
}

// ---------------
// Bola END
// ===============

public void BolaEND()
{
	Bolas = null;
	BolaC = 0;
}

// ---------------
// Bola RUN
// ===============

public void BolaRUN()
{
	for (int i=0 ; i<BolaMAX ; i++)
	{
	if (Bolas[i] != null) {if ( BolaPlay(Bolas[i]) ) {Bolas[i]=null; BolaC--;}}
	}
}


// ---------------
// Bola Triple
// ===============

public void BolaTriple()
{
	if (BolaC == 1)
	{
	int i=0; for (; i<BolaMAX ; i++) {if (Bolas[i] != null) {break;}}

	int CX=Bolas[i][0];
	int CY=Bolas[i][1];
	int AX=Bolas[i][2]<0?-1:1;
	int AY=Bolas[i][3]<0?-1:1;

	Bolas[i]=null;
	BolaC=0;

	BolaSET(CX,CY, AX*gc.BolaSpeed,   AY*gc.BolaSpeed);
	BolaSET(CX,CY, AX*gc.BolaSpeed/2, AY*gc.BolaSpeed);
	BolaSET(CX,CY, AX*gc.BolaSpeed,   AY*gc.BolaSpeed/2);
	}
}

// <=- <=- <=- <=- <=-










// ********************
// --------------------
// Capsula - Engine
// ====================
// ********************

int CapsulaCnt;

final int CapsulaMAX=4;
int[][] Capsulas;
int CapsulaC=0;

// ---------------
// Capsula SET
// ===============

public void CapsulaSET(int CoorX, int CoorY)
{
	if (--CapsulaCnt>0 || CapsulaC == CapsulaMAX) {return;} else {CapsulaCnt=gc.CapsulaConta;}

	int Type=0;
	boolean Ok;
	do {
		Ok=true;

		Type=RND(4);

		switch (Type)
		{
		case 0:	// Catch
			if (PalaCatch || BolaC!=1) {Ok=false;}
		break;

		case 1:	// Triple Bola
			if (BolaC!=1) {Ok=false;}
		break;

		case 2:	// Pala Larga
			if (PalaType==2) {Ok=false;}
		break;

		case 3:	// Pala Disparo
			if (PalaType==3) {Ok=false;}
		break;
		}
	} while (!Ok);


	int i=0; for (; i<CapsulaMAX ; i++) {if (Capsulas[i] == null) {break;}}

	Capsulas[i] = new int[4];

	Capsulas[i][0] = CoorX;
	Capsulas[i][1] = CoorY;
//	Capsulas[i][2] = FrameAct;
	Capsulas[i][3] = Type;

	CapsulaC++;
}


// ---------------
// Capsula Play
// ===============

public boolean CapsulaPlay(int[] Data)
{
	if (++Data[2] > 4) {Data[2]=0;}

	Data[1]+=gc.CapsulaSpeed;

	if (Data[1] > ParedPixeY) {return true;}

	if ( colision(Data[0], Data[1], gc.TochoSizeX, gc.TochoSizeY/2,   PalaX, gc.PalaY, PalaSizeX, gc.PalaSizeY/2) )
	{
	gc.VibraSET(100);
//	gc.SoundSET(2,1);

	PalaCatch=false;
	PalaStop=false;

	switch (Data[3])
	{
	case 0:
		PalaSET(1);
		PalaCatch=true;
	break;

	case 1:
		BolaTriple();
	break;

	case 2:
		PalaSET(2);
	break;

	case 3:
		PalaSET(3);
	break;
	}


	return true;
	}

	return false;
}


// ---------------
// Capsula INI
// ===============

public void CapsulaINI()
{
	CapsulaCnt=gc.CapsulaConta;

	CapsulaC = 0;
	Capsulas = new int[CapsulaMAX][];
}

// ---------------
// Capsula END
// ===============

public void CapsulaEND()
{
	Capsulas = null;
	CapsulaC = 0;
}

// ---------------
// Capsula RUN
// ===============

public void CapsulaRUN()
{
	for (int i=0 ; i<CapsulaMAX ; i++)
	{
	if (Capsulas[i] != null) {if ( CapsulaPlay(Capsulas[i]) ) {Capsulas[i]=null; CapsulaC--;}}
	}
}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Disparo - Engine
// ====================
// ********************

final int DisparoMAX=3;
int[][] Disparos;
int DisparoC=0;

// ---------------
// Disparo SET
// ===============

public void DisparoSET(int CoorX, int CoorY)
{
	if (DisparoC == DisparoMAX) {return;}

	int i=0; for (; i<DisparoMAX ; i++) {if (Disparos[i] == null) {break;}}

	Disparos[i] = new int[4];

	Disparos[i][0] = CoorX;
	Disparos[i][1] = CoorY;
	Disparos[i][2] = 1;		// Bala Izquierda ON
	Disparos[i][3] = 1;		// Bala Derecha ON

	DisparoC++;
}


// ---------------
// Disparo Play
// ===============

public boolean DisparoPlay(int[] Data)
{
	Data[1]-=gc.DisparoSpeed;

	if (Data[1] < 0) {return true;}

	int ParedDisp=0;
	if ( Data[2]!=0 && ParedCheck(Data[0]+gc.DisparoAdd1, Data[1]) ) {ParedDisp=ParedDir; Data[2]=0;}
	if ( Data[3]!=0 && ParedCheck(Data[0]+gc.DisparoAdd2, Data[1]) || (ParedDir==ParedDisp) ) {Data[3]=0;}

	if (Data[2]==0 &&  Data[3]==0) {return true;}


	return false;
}


// ---------------
// Disparo INI
// ===============

public void DisparoINI()
{
	DisparoC = 0;
	Disparos = new int[DisparoMAX][];
}

// ---------------
// Disparo END
// ===============

public void DisparoEND()
{
	Disparos = null;
	DisparoC = 0;
}

// ---------------
// Disparo RUN
// ===============

public void DisparoRUN()
{
	for (int i=0 ; i<DisparoMAX ; i++)
	{
	if (Disparos[i] != null) {if ( DisparoPlay(Disparos[i]) ) {Disparos[i]=null; DisparoC--;}}
	}
}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Destello - Engine
// ====================
// ********************

final int DestelloMAX=8;
int[][] Destellos;
int DestelloC=0;

// ---------------
// Destello SET
// ===============

public void DestelloSET(int CoorX, int CoorY, int Type)
{
	if (DestelloC == DestelloMAX) {return;}

	int i=0; for (; i<DestelloMAX ; i++) {if (Destellos[i] == null) {break;}}

	Destellos[i] = new int[4];

	Destellos[i][0] = CoorX;
	Destellos[i][1] = CoorY;
	Destellos[i][2] = 0;	// Frame
	Destellos[i][3] = Type;

	DestelloC++;
}


// ---------------
// Destello Play
// ===============

public boolean DestelloPlay(int[] Data)
{
	if (Data[3]!=0 || ++Data[2] > 4 ) {return true;}

	return false;
}


// ---------------
// Destello INI
// ===============

public void DestelloINI()
{
	DestelloC = 0;
	Destellos = new int[DestelloMAX][];
}

// ---------------
// Destello END
// ===============

public void DestelloEND()
{
	Destellos = null;
	DestelloC = 0;
}

// ---------------
// Destello RUN
// ===============

public void DestelloRUN()
{
	for (int i=0 ; i<DestelloMAX ; i++)
	{
	if (Destellos[i] != null) {if ( DestelloPlay(Destellos[i]) ) {Destellos[i]=null; DestelloC--;}}
	}
}

// <=- <=- <=- <=- <=-












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



// -------------------
// Cheats - Engine
// ===================

public void cheats()
{
	if (keyMisc == 1 && lastKeyMisc != 0) {FaseExit=1;}
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