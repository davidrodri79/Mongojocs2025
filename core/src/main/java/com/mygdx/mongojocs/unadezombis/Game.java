package com.mygdx.mongojocs.unadezombis;


// -----------------------------------------------
// Una de Zombies v1.0 Rev.0 (26.11.2003)
// ===============================================
// Programado por Juan Antonio G�mez
// ------------------------------------




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;

import java.io.InputStream;

public class Game extends MIDlet implements Runnable
{

GameCanvas gc;
Thread thread;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()
{
	System.gc();

	gc = new GameCanvas(this);

	System.gc();
	
	Display.getDisplay(this).setCurrent(gc);

	thread=new Thread(this); System.gc();
	thread.start();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// startApp - Al inicio y cada vez que se hizo pauseApp()
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void startApp()
{
	Display.getDisplay(this).setCurrent(gc);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// pauseApp - Cada vez que se PAUSA el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void pauseApp() {}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// destroyApp - Para DESTRUIR el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	PrefsSET();
	GameExit=1;
	notifyDestroyed();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int GameExit=0;

long GameMilis;
int  GameSleep;

public void run()
{
	System.gc();
	GameINI(); System.gc();
	gc.CanvasSET(); System.gc();
	GameSET(); System.gc();

	while (GameExit==0)
	{
		GameMilis = System.currentTimeMillis();

		KeyboardRUN();

//	try {
		GameRUN();
//	} 	catch (Exception e) {e.printStackTrace(); e.toString();}

		gc.repaint();
		gc.serviceRepaints();

		GameSleep=(40-(int)(System.currentTimeMillis()-GameMilis));
		if (GameSleep<1) {GameSleep=1;}


//	System.gc();


		try	{
		thread.sleep(GameSleep);
		} catch(java.lang.InterruptedException e) {}
	}

	GameEND(); System.gc();

	destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

void LoadFile(byte[] buffer, String Nombre)
{
	/*System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		for (int i=0 ; i<buffer.length ; i++)
		{
		int Dato = is.read();
		if (Dato==-1) {break;}
		buffer[i] = (byte)Dato;
		}
//	is.read(buffer, 0, buffer.length);
	is.close();
	}catch(Exception exception) {}
	System.gc();*/
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	for(int i = 0; i < bytes.length; i++)
		buffer[i] = bytes[i];
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean Colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 > x1+xs1 ) ||	( x2+xs2 < x1 ) ||	( y2 > y1+ys1 ) ||	( y2+ys2 < y1 ));
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



// *******************
// -------------------
// Keyboard - Engine
// ===================
// *******************

int KeybX1, KeybY1, KeybB1, KeybM1;
int KeybX2, KeybY2, KeybB2, KeybM2;

// -------------------
// Keyboard RUN
// ===================

public void KeyboardRUN()
{
	KeybX2 = KeybX1;	// Keys del Frame Anterior
	KeybY2 = KeybY1;
	KeybB2 = KeybB1;
	KeybM2 = KeybM1;

	KeybX1 = gc.KeybX;	// Keys del Frame Actual
	KeybY1 = gc.KeybY;
	KeybB1 = gc.KeybB;
	KeybM1 = gc.KeybM;

	if (KeybB1!=0 && KeybB1!=KeybB2) {CheatRUN(KeybB1);}
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

int GameX;
int GameY;
int GameSizeX;
int GameSizeY;
int GameMaxX=176;	// 176
int GameMaxY=208;	// 208

int GameStatus=0;

int GameSound = 1;
int GameVibra = 1;

// -------------------
// Game INI
// ===================

public void GameINI()
{

	GameSizeX=gc.CanvasSizeX; if (GameSizeX <= GameMaxX) {GameX=0;} else {GameX=(GameSizeX-GameMaxX)/2;GameSizeX=GameMaxX;}
	GameSizeY=gc.CanvasSizeY; if (GameSizeY <= GameMaxY) {GameY=0;} else {GameY=(GameSizeY-GameMaxY)/2;GameSizeY=GameMaxY;}

}

// -------------------
// Game END
// ===================

public void GameEND()
{
}

// -------------------
// Game SET
// ===================

public void GameSET()
{
	PrefsINI();
}

// -------------------
// Game RUN
// ===================

public void GameRUN()
{
	switch (GameStatus)
	{
	case 0:
		gc.CanvasImageSET("/Gfx/Manga.png", 0);
		WaitSET(2500, GameStatus+1);
	break;

	case 1:
		gc.CanvasImageSET("/Gfx/Producido.png", 0);
		WaitSET(2500, GameStatus+1);
	break;

	case 2:
		gc.CanvasImageSET("/Gfx/Caratula.png", 0);
		GameStatus=100;
	break;


// ------------------
// Jugar Bucle
// ------------------
	case 100:
		JugarINI();
		GameStatus++;
	break;

	case 101:
		GameStatus++;
		MenuSET(-1);
	break;

	case 102:
		gc.SoundRES();
		JugarINI_Game();
		GameStatus++;
	case 103:
		JugarSET();
		GameStatus++;
	case 104:
		if (KeybM1==-1 && KeybM1!=KeybM2) {MenuSET(1); break;}

		if (!SantiIniFlag) {SantiIniSET();}

		if ( !JugarRUN() ) {break;}

		FaseEND();


		GameStatus=103;

		switch (FaseExit)
		{
		case 1:	// Pasamos de Nivel
			JugarRES();
			JugarLevel=JugarLevelNext;

			switch (JugarLevel)
			{
			case 90:
				Stage = 0;
				JugarLevel = 10;
				ProtFaseID = 1;
			break;

			case 91:
				Stage = 1;
				JugarLevel = 1;
				ProtFaseID = 0;
			break;

			case 92:
				Stage = 2;
				JugarLevel = 1;
				ProtFaseID = 0;
			break;

			case 99:
				Stage = 0;
				JugarLevel = 6;
				ProtFaseID = 2;
			break;
			}

		break;

		case 2:	// Perdemos una Vida
		break;

		case 3:	// Terminar Juego
			JugarEND();
			GameStatus=102;
			MenuSET(8);
		break;
		}

		FaseExit=0;
	break;
// ===================


// ------------------
// Santi Bucle
// ------------------
	case 200:
		if ( SantiRUN() ) {SantiRES();}
	break;
// ===================

// ------------------
// Menu Bucle
// ------------------
	case 50:
		if ( MenuRUN() ) {MenuRES();}
	break;
// ===================

// ------------------
// Wait Bucle
// ------------------
	case 1000:
		WaitRUN();
	break;
// ===================
	}

}

// <=- <=- <=- <=- <=-









// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

boolean JugarPaint=false;
boolean JugarFabrica;
int JugarLevel;
int JugarLevelNext;
int JugarVidas;
int Stage;
int JugarState;

int JugarLlaves;
int JugarEnergy;

int[] JugarBalas = new int[4];
int[] JugarBalasItem = new int[] {0,100,50,10};

// -------------------
// Jugar INI
// ===================

public void JugarINI()
{
	JugarINI_Game();

	FaseINI();

	gc.JugarINI_Gfx();
}


public void JugarINI_Game()
{
	SantiIniFlag = false;

	ProtFaseID=0;
	ProtType=0;

	Stage=0;

	JugarLevel=1;

	JugarVidas=3;

	JugarState = 0;

	JugarEnergy = 50;
	JugarLlaves = 0;
	JugarBalas[1]=0;
	JugarBalas[2]=0;
	JugarBalas[3]=0;

	ProtInmune=false;

	for (int s=0 ; s<FasePortaTab.length ; s++)
	{
		for (int f=0 ; f<FasePortaTab[s].length ; f++)
		{
			for (int i=0 ; i<FasePortaTab[s][f].length ; i++)
			{
				if (FasePortaTab[s][f][i].length >0 && FasePortaTab[s][f][i][0] < 0) {FasePortaTab[s][f][i][0] *= -1;}
			}
		}
	}


	FaseItemTab = new byte[3][][][];

	FaseItemTab[0] = new byte[13][][];
	FaseItemTab[1] = new byte[14][][];
	FaseItemTab[2] = new byte[8][][];


	JugarFabrica=false;

	JugarPaint=false;
}


// -------------------
// Jugar SET
// ===================

public void JugarSET()
{
	FaseSET();

	gc.JugarSET_Gfx();
}

// -------------------
// Jugar END
// ===================

public void JugarRES()
{
	JugarPaint=false;

	gc.JugarRES_Gfx();
}

// -------------------
// Jugar END
// ===================

public void JugarEND()
{
	JugarRES();

//	gc.JugarEND_Gfx();
}


// -------------------
// Jugar RUN
// ===================

public boolean JugarRUN()
{
	FaseRUN();

	if (FaseExit!=0) {return true;}

	JugarPaint=true;

	return false;
}

// <=- <=- <=- <=- <=-












// *******************
// -------------------
// Fase - Engine
// ===================
// *******************

static final int TileSize=8;

byte[] FaseSuelo = new byte[] {0x1A, 0x01, 0x01};

boolean Cabina;

byte[][][] FasePortaTiles = new byte[][][]
{{
{},{},{},{}	// Stage 0
},{
{0x1A,0x1B,0x1C},{0x08,0x18,0x28},{0x0C,0x0B,0x0A},{0x29,0x19,0x09}	// Stage 1
},{
{0x1B,0x1C,0x1D},{0x09,0x19,0x29},{0x0D,0x0C,0x0B},{0x2A,0x1A,0x0A}	// Stage 2
}};


byte[][] FaseSize = new byte[][] {
	{ 1,1, 29,48,  48,26,  26,26,  26,48,  26,26,  26,26,  26,26,  26,48,  26,26,  26,48,  26,26,  26,26 },			// Stage 0
	{ 1,1, 32,32,  22,22,  22,26,  48,32,  48,32,  48,32,  48,32,  22,22,  22,26,  48,32,  48,32,  48,32,  48,32 },	// Stage 1
	{ 1,1, 26,32,  26,26,  48,26,  26,48,  48,26,  26,48,  26,48, },												// Stage 2
};

int FaseSizeX=1;
int FaseSizeY=1;
byte[] FaseMap;

int FaseExit;


byte[][][][] FaseItemTab;


//      3
//      |
// 2 <=-X-=> 0
//      |
//      1

byte[][][][] FasePortaTab = new byte[][][][]
{{
// -----------------------
// Fase 0
// =======================
{{
//		C A L L E S
}},
// -----------------------
// Fase 1
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 2
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 3
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 4
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 5
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 6
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 7
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 8
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 9
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 10
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 11
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 12
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
},{
// -----------------------
// Fase 0
// =======================
{{
//		E D I F I C I O
}},
// -----------------------
// Fase 1
// =======================
{{
	21,		// ID (2x: Puerta)
	2,		// Tipo de Puerta
	10,3,	// Coor X,Y
},{
	20,		// ID (2x: Puerta)
	1,		// Tipo de Puerta
	2,22,	// Coor X,Y
},{
	20,		// ID (2x: Puerta)
	1,		// Tipo de Puerta
	24,22,	// Coor X,Y
}},
// -----------------------
// Fase 2
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 3
// =======================
{{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	0,4,	// Coor X,Y
},{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	0,11,	// Coor X,Y
},{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	21,4,	// Coor X,Y
},{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	21,11,	// Coor X,Y
}},
// -----------------------
// Fase 4
// =======================
{{
	21,		// ID (2x: Puerta)
	2,		// Tipo de Puerta
	16,15,	// Coor X,Y
},{
	20,		// ID (2x: Puerta)
	1,		// Tipo de Puerta
	18,23,	// Coor X,Y
},{
	20,		// ID (2x: Puerta)
	1,		// Tipo de Puerta
	35,23,	// Coor X,Y
}},
// -----------------------
// Fase 5
// =======================
{{
	22,		// ID (2x: Puerta)
	3,		// Tipo de Puerta
	39,8,	// Coor X,Y
}},
// -----------------------
// Fase 6
// =======================
{{
	20,		// ID (2x: Puerta)
	1,		// Tipo de Puerta
	4,10,	// Coor X,Y
},{
	23,		// ID (2x: Puerta)
	0,		// Tipo de Puerta
	32,7,	// Coor X,Y
},{
	23,		// ID (2x: Puerta)
	0,		// Tipo de Puerta
	32,19,	// Coor X,Y
}},
// -----------------------
// Fase 7
// =======================
{{
	22,		// ID (2x: Puerta)
	3,		// Tipo de Puerta
	8,8,	// Coor X,Y
}},
// -----------------------
// Fase 8
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 9
// =======================
{{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	0,4,	// Coor X,Y
},{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	0,11,	// Coor X,Y
},{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	21,4,	// Coor X,Y
},{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	21,11,	// Coor X,Y
}},
// -----------------------
// Fase 10
// =======================
{{
	21,		// ID (2x: Puerta)
	2,		// Tipo de Puerta
	16,15,	// Coor X,Y
},{
	20,		// ID (2x: Puerta)
	1,		// Tipo de Puerta
	18,23,	// Coor X,Y
}},
// -----------------------
// Fase 11
// =======================
{{
	22,		// ID (2x: Puerta)
	3,		// Tipo de Puerta
	39,8,	// Coor X,Y
}},
// -----------------------
// Fase 12
// =======================
{{
	22,		// ID (2x: Puerta)
	3,		// Tipo de Puerta
	8,8,	// Coor X,Y
}},
// -----------------------
// Fase 13
// =======================
{{
	20,		// ID (2x: Puerta)
	1,		// Tipo de Puerta
	4,10,	// Coor X,Y
},{
	23,		// ID (2x: Puerta)
	0,		// Tipo de Puerta
	32,19,	// Coor X,Y
}},
// -----------------------
},{
// -----------------------
// Fase 0
// =======================
{{
//		F A B R I C A
}},
// -----------------------
// Fase 1
// =======================
{{
	22,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	7,0,	// Coor X,Y
},{
	23,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	25,23,	// Coor X,Y
}},
// -----------------------
// Fase 2
// =======================
{{
	0,		// ID (0: Nada)
}},
// -----------------------
// Fase 3
// =======================
{{
	22,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	7,0,	// Coor X,Y
},{
	23,		// ID (2x: Puerta)
	0,		// Tipo de Puerta
	38,23,	// Coor X,Y
}},
// -----------------------
// Fase 4
// =======================
{{
	21,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	0,3,	// Coor X,Y
},{
	23,		// ID (2x: Puerta)
	0,		// Tipo de Puerta
	17,45,	// Coor X,Y
}},
// -----------------------
// Fase 5
// =======================
{{
	22,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	44,0,	// Coor X,Y
}},
// -----------------------
// Fase 6
// =======================
{{
	22,		// ID (2x: Puerta)
	4,		// Tipo de Puerta
	23,0,	// Coor X,Y
}},
// -----------------------
// Fase 7
// =======================
{{
	20,		// ID (2x: Puerta)
	3,		// Tipo de Puerta
	9,39,	// Coor X,Y
},{
	22,		// ID (2x: Puerta)
	3,		// Tipo de Puerta
	16,39,	// Coor X,Y
}},
// -----------------------
}};



byte[][][][] FaseExitTab = new byte[][][][]
{{
// -----------------------
// Fase 0
// =======================
{{
//		C A L L E S
}},
// -----------------------
// Fase 1
// =======================
{{
	10,		// ID (10: Arriba)
	1,		// Coor Inicial
	17,		// Coor Final
	10,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	8,		// Coor Inicial
	23,		// Coor Final
	3,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	32,		// Coor Inicial
	48,		// Coor Final
	2,		// Fase en la que apareces
	-22,	// Ajuste de X/Y
}},
// -----------------------
// Fase 2
// =======================
{{
	11,		// ID (11: Izquierda)
	10,		// Coor Inicial
	26,		// Coor Final
	1,		// Fase en la que apareces
	22,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	10,		// Coor Inicial
	26,		// Coor Final
	4,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
// Fase 3
// =======================
{{
	11,		// ID (11: Izquierda)
	8,		// Coor Inicial
	23,		// Coor Final
	1,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
// Fase 4
// =======================
{{
	11,		// ID (11: Izquierda)
	10,		// Coor Inicial
	27,		// Coor Final
	2,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	10,		// Coor Inicial
	24,		// Coor Final
	5,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	33,		// Coor Inicial
	47,		// Coor Final
	6,		// Fase en la que apareces
	-23,	// Ajuste de X/Y
}},
// -----------------------
// Fase 5
// =======================
{{
	11,		// ID (11: Izquierda)
	10,		// Coor Inicial
	38,		// Coor Final
	4,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
// Fase 6
// =======================
{{
	11,		// ID (11: Izquierda)
	10,		// Coor Inicial
	24,		// Coor Final
	4,		// Fase en la que apareces
	23,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	6,		// Coor Inicial
	17,		// Coor Final
	8,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	10,		// Coor Inicial
	38,		// Coor Final
	7,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
// Fase 7
// =======================
{{
	11,		// ID (11: Izquierda)
	10,		// Coor Inicial
	38,		// Coor Final
	6,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
// Fase 8
// =======================
{{
	10,		// ID (10: Arriba)
	6,		// Coor Inicial
	18,		// Coor Final
	6,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	15,		// Coor Inicial
	27,		// Coor Final
	9,		// Fase en la que apareces
	-5,		// Ajuste de X/Y
}},
// -----------------------
// Fase 9
// =======================
{{
	11,		// ID (11: Izquierda)
	10,		// Coor Inicial
	38,		// Coor Final
	8,		// Fase en la que apareces
	5,		// Ajuste de X/Y
},{
}},
// -----------------------
// Fase 10
// =======================
{{
	11,		// ID (11: Izquierda)
	4,		// Coor Inicial
	9,		// Coor Final
	91,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	1,		// Coor Inicial
	17,		// Coor Final
	1,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	2,		// Coor Inicial
	13,		// Coor Final
	12,		// Fase en la que apareces
	6,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	30,		// Coor Inicial
	44,		// Coor Final
	11,		// Fase en la que apareces
	-24,	// Ajuste de X/Y
}},
// -----------------------
// Fase 11
// =======================
{{
	10,		// ID (10: Arriba)
	5,		// Coor Inicial
	19,		// Coor Final
	12,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	11,		// ID (11: Izquierda)
	7,		// Coor Inicial
	20,		// Coor Final
	10,		// Fase en la que apareces
	24,		// Ajuste de X/Y
}},
// -----------------------
// Fase 12
// =======================
{{
	11,		// ID (11: Izquierda)
	8,		// Coor Inicial
	19,		// Coor Final
	10,		// Fase en la que apareces
	-6,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	5,		// Coor Inicial
	19,		// Coor Final
	11,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
},{
// -----------------------
// Fase 0
// =======================
{{
//		E D I F I C I O
}},
// -----------------------
// Fase 1
// =======================
{{
	12,		// ID (12: Abajo)
	13,		// Coor Inicial
	17,		// Coor Final
	90,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	4,		// Coor Inicial
	8,		// Coor Final
	2,		// Fase en la que apareces
	-2,		// Ajuste de X/Y
}},
// -----------------------
// Fase 2
// =======================
{{
	11,		// ID (11: Izquierda)
	2,		// Coor Inicial
	6,		// Coor Final
	1,		// Fase en la que apareces
	2,		// Ajuste de X/Y
}},
// -----------------------
// Fase 3
// =======================
{{
	11,		// ID (11: Izquierda)
	5,		// Coor Inicial
	8,		// Coor Final
	5,		// Fase en la que apareces
	20,		// Ajuste de X/Y
},{
	11,		// ID (11: Izquierda)
	12,		// Coor Inicial
	15,		// Coor Final
	4,		// Fase en la que apareces
	-8,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	2,		// Coor Inicial
	6,		// Coor Final
	2,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	5,		// Coor Inicial
	8,		// Coor Final
	7,		// Fase en la que apareces
	20,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	12,		// Coor Inicial
	15,		// Coor Final
	6,		// Fase en la que apareces
	-8,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	20,		// Coor Inicial
	24,		// Coor Final
	8,		// Fase en la que apareces
	-18,	// Ajuste de X/Y
}},
// -----------------------
// Fase 4
// =======================
{{
	13,		// ID (13: Derecha)
	4,		// Coor Inicial
	7,		// Coor Final
	3,		// Fase en la que apareces
	8,		// Ajuste de X/Y
}},
// -----------------------
// Fase 5
// =======================
{{
	13,		// ID (13: Derecha)
	25,		// Coor Inicial
	28,		// Coor Final
	3,		// Fase en la que apareces
	-20,	// Ajuste de X/Y
}},
// -----------------------
// Fase 6
// =======================
{{
	11,		// ID (11: Izquierda)
	4,		// Coor Inicial
	7,		// Coor Final
	3,		// Fase en la que apareces
	8,		// Ajuste de X/Y
}},
// -----------------------
// Fase 7
// =======================
{{
	11,		// ID (11: Izquierda)
	25,		// Coor Inicial
	28,		// Coor Final
	3,		// Fase en la que apareces
	-20,	// Ajuste de X/Y
}},
// -----------------------
// Fase 8
// =======================
{{
	11,		// ID (11: Izquierda)
	2,		// Coor Inicial
	6,		// Coor Final
	3,		// Fase en la que apareces
	18,		// Ajuste de X/Y
}},
// -----------------------
// Fase 9
// =======================
{{
	11,		// ID (11: Izquierda)
	5,		// Coor Inicial
	8,		// Coor Final
	11,		// Fase en la que apareces
	20,		// Ajuste de X/Y
},{
	11,		// ID (11: Izquierda)
	12,		// Coor Inicial
	15,		// Coor Final
	10,		// Fase en la que apareces
	-8,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	2,		// Coor Inicial
	6,		// Coor Final
	8,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	5,		// Coor Inicial
	8,		// Coor Final
	12,		// Fase en la que apareces
	20,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	12,		// Coor Inicial
	15,		// Coor Final
	13,		// Fase en la que apareces
	-8,		// Ajuste de X/Y
}},
// -----------------------
// Fase 10
// =======================
{{
	13,		// ID (13: Derecha)
	4,		// Coor Inicial
	7,		// Coor Final
	9,		// Fase en la que apareces
	8,		// Ajuste de X/Y
}},
// -----------------------
// Fase 11
// =======================
{{
	13,		// ID (13: Derecha)
	25,		// Coor Inicial
	28,		// Coor Final
	9,		// Fase en la que apareces
	-20,	// Ajuste de X/Y
}},
// -----------------------
// Fase 12
// =======================
{{
	11,		// ID (11: Izquierda)
	25,		// Coor Inicial
	28,		// Coor Final
	9,		// Fase en la que apareces
	-20,	// Ajuste de X/Y
}},
// -----------------------
// Fase 13
// =======================
{{
	11,		// ID (11: Izquierda)
	4,		// Coor Inicial
	7,		// Coor Final
	9,		// Fase en la que apareces
	8,		// Ajuste de X/Y
}},
// -----------------------
},{
// -----------------------
// Fase 0
// =======================
{{
//		F A B R I C A
}},
// -----------------------
// Fase 1
// =======================
{{
	10,		// ID (10: Arriba)
	4,		// Coor Inicial
	7,		// Coor Final
	3,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	10,		// Coor Inicial
	16,		// Coor Final
	99,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	20,		// Coor Inicial
	23,		// Coor Final
	2,		// Fase en la que apareces
	-16,	// Ajuste de X/Y
}},
// -----------------------
// Fase 2
// =======================
{{
	11,		// ID (11: Izquierda)
	4,		// Coor Inicial
	7,		// Coor Final
	1,		// Fase en la que apareces
	16,		// Ajuste de X/Y
}},
// -----------------------
// Fase 3
// =======================
{{
	10,		// ID (10: Arriba)
	4,		// Coor Inicial
	7,		// Coor Final
	4,		// Fase en la que apareces
	0,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	4,		// Coor Inicial
	7,		// Coor Final
	1,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
// Fase 4
// =======================
{{
	11,		// ID (11: Izquierda)
	4,		// Coor Inicial
	7,		// Coor Final
	5,		// Fase en la que apareces
	17,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	4,		// Coor Inicial
	7,		// Coor Final
	3,		// Fase en la que apareces
	0,		// Ajuste de X/Y
}},
// -----------------------
// Fase 5
// =======================
{{
	10,		// ID (10: Arriba)
	41,		// Coor Inicial
	44,		// Coor Final
	6,		// Fase en la que apareces
	-22,	// Ajuste de X/Y
},{
	13,		// ID (13: Derecha)
	21,		// Coor Inicial
	24,		// Coor Final
	4,		// Fase en la que apareces
	-17,	// Ajuste de X/Y
}},
// -----------------------
// Fase 6
// =======================
{{
	10,		// ID (10: Arriba)
	20,		// Coor Inicial
	23,		// Coor Final
	7,		// Fase en la que apareces
	-1,		// Ajuste de X/Y
},{
	12,		// ID (12: Abajo)
	19,		// Coor Inicial
	22,		// Coor Final
	5,		// Fase en la que apareces
	22,		// Ajuste de X/Y
}},
// -----------------------
// Fase 7
// =======================
{{
	12,		// ID (12: Abajo)
	19,		// Coor Inicial
	22,		// Coor Final
	6,		// Fase en la que apareces
	1,		// Ajuste de X/Y
}},
// -----------------------
}};


// -------------------
// Fase INI
// ===================

public void FaseINI()
{
//	LoadFile(AnimMap, "/Data/Anims.map");

}


// -------------------
// Fase END
// ===================

public void FaseEND()
{
	ProtEND();

	EnemyEND();
	SeguraEND();

	AniSprEND();

	System.gc();
}


// -------------------
// Fase SET
// ===================

public void FaseSET()
{
	FaseExit=0;

	switch (Stage)
	{
	case 0:	TilDatTab = TilDatTab0; break;
	case 1:	TilDatTab = TilDatTab1; break;
	case 2:	TilDatTab = TilDatTab2; break;
	}

	FaseSizeX=FaseSize[Stage][(JugarLevel*2)+0];
	FaseSizeY=FaseSize[Stage][(JugarLevel*2)+1];
	FaseMap = new byte[FaseSizeX*FaseSizeY];

	LoadFile(FaseMap, "/Data/Fase"+Stage+""+JugarLevel+".map");


	if (JugarState==0 && Stage==1) {JugarState=1;}


	AniSprINI();

	EnemyINI();
	SeguraINI();

	ProtINI();


	ProtSET();

	EnemyTime = 50+RND(30);

	Cabina = true;

	FaseSET_Level();
}


// -------------------
// Fase SET Level
// ===================

public void FaseSET_Level()
{

	FaseUpdateItems();


	if (JugarFabrica && Stage==0 && JugarLevel==6)
	{
	FaseMap[(12*FaseSizeX)+ 9]=0x1A;
	FaseMap[(12*FaseSizeX)+10]=0x1A;
	FaseMap[(12*FaseSizeX)+11]=0x1A;
	FaseMap[(12*FaseSizeX)+12]=0x1A;
	FaseMap[(12*FaseSizeX)+13]=0x1A;
	FaseMap[(12*FaseSizeX)+14]=0x1A;
	}



	if (Stage==2 && JugarLevel==7)
	{
	SeguraSET();
	}


}


public void FaseUpdateItems()
{
// -----------------------------------------------------------------------
// Si YA habiamos entrado en esta pantalla, borramos los Items YA pillados
// =======================================================================
	if (FaseItemTab[Stage][JugarLevel] != null)
	{

	// Items
	// -----
		for (int i=0 ; i<FaseItemTab[Stage][JugarLevel].length ; i++)
		{
			if (FaseItemTab[Stage][JugarLevel][i][0] < 0)
			{
				int Dir = (FaseItemTab[Stage][JugarLevel][i][3] * FaseSizeX) + FaseItemTab[Stage][JugarLevel][i][2];
				FaseMap[Dir]=FaseSuelo[Stage];
				FaseMap[Dir+1]=FaseSuelo[Stage];
				FaseMap[Dir+FaseSizeX]=FaseSuelo[Stage];
				FaseMap[Dir+FaseSizeX+1]=FaseSuelo[Stage];
			}
		}

	// Puertas
	// -------
		for (int i=0 ; i<FasePortaTab[Stage][JugarLevel].length ; i++)
		{
		boolean PuertaAbrir = false;

			if (FasePortaTab[Stage][JugarLevel][i][0] < 0)
			{
				int Dir = (FasePortaTab[Stage][JugarLevel][i][3] * FaseSizeX) + FasePortaTab[Stage][JugarLevel][i][2];

				switch (FasePortaTab[Stage][JugarLevel][i][0] * -1)
				{
				case 20:	// Puerta Derecha
					FaseMap[++Dir]=FaseSuelo[Stage];
					FaseMap[++Dir]=FaseSuelo[Stage];
					FaseMap[++Dir]=FaseSuelo[Stage];
					PuertaAbrir = true;
				break;

				case 21:	// Puerta Abajo
					FaseMap[Dir+=FaseSizeX]=FaseSuelo[Stage];
					FaseMap[Dir+=FaseSizeX]=FaseSuelo[Stage];
					FaseMap[Dir+=FaseSizeX]=FaseSuelo[Stage];
					PuertaAbrir = true;
				break;

				case 22:	// Puerta Izquierda
					FaseMap[--Dir]=FaseSuelo[Stage];
					FaseMap[--Dir]=FaseSuelo[Stage];
					FaseMap[--Dir]=FaseSuelo[Stage];
					PuertaAbrir = true;
				break;

				case 23:	// Puerta Arriba
					FaseMap[Dir-=FaseSizeX]=FaseSuelo[Stage];
					FaseMap[Dir-=FaseSizeX]=FaseSuelo[Stage];
					FaseMap[Dir-=FaseSizeX]=FaseSuelo[Stage];
					PuertaAbrir = true;
				break;
				}
			}


			if (PuertaAbrir)
			{
			int PuertaDir = FasePortaTab[Stage][JugarLevel][i][1];
			int MapDir = (FasePortaTab[Stage][JugarLevel][i][3] * FaseSizeX) + FasePortaTab[Stage][JugarLevel][i][2];

				switch (PuertaDir)
				{
				case 0:
					FaseMap[++MapDir]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[++MapDir]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[++MapDir]=FasePortaTiles[Stage][PuertaDir][2];
				break;

				case 1:
					FaseMap[MapDir+=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[MapDir+=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[MapDir+=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][2];
				break;

				case 2:
					FaseMap[--MapDir]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[--MapDir]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[--MapDir]=FasePortaTiles[Stage][PuertaDir][2];
				break;

				case 3:
					FaseMap[MapDir-=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[MapDir-=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[MapDir-=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][2];
				break;
				}
			}

	// -------
		}
	return;
	}
// -----------------------------------------------------------------------

// -----------------------------------------------------------------------
// Generamos la tabla de Items para esta nueva pantalla visitada
// =======================================================================
	int Items=0;

	for (int i=0 ; i<FaseMap.length ; i++)
	{
		switch (Stage)
		{
		case 0:
			switch (FaseMap[i] & 0xFF)
			{
			case 0x60:
			case 0x62:
			case 0x64:
			case 0x66:
			case 0x68:
				Items++;
			break;
			}
		break;

		case 1:
			switch (FaseMap[i] & 0xFF)
			{
			case 0x68:
			case 0x6A:
			case 0x6C:
			case 0x6E:
				Items++;
			break;
			}
		break;

		case 2:
			switch (FaseMap[i] & 0xFF)
			{
			case 0x37:
			case 0x39:
			case 0x3B:
			case 0x3D:
				Items++;
			break;
			}
		break;
		}
	}


	FaseItemTab[Stage][JugarLevel] = new byte[Items][4];


	for (int Pos=0, i=0 ; i<FaseMap.length ; i++)
	{
		boolean ItemOk=false;
		int Type=0;

		switch (Stage)
		{
		case 0:
			switch (FaseMap[i] & 0xFF)
			{
			case 0x60:	Type=1; ItemOk=true; break;
			case 0x62:	Type=2; ItemOk=true; break;
			case 0x64:	Type=3; ItemOk=true; break;
			case 0x66:	Type=4; ItemOk=true; break;
			case 0x68:	Type=5; ItemOk=true; break;
			}
		break;

		case 1:
			switch (FaseMap[i] & 0xFF)
			{
			case 0x68:	Type=2; ItemOk=true; break;
			case 0x6A:	Type=3; ItemOk=true; break;
			case 0x6C:	Type=4; ItemOk=true; break;
			case 0x6E:	Type=5; ItemOk=true; break;
			}
		break;

		case 2:
			switch (FaseMap[i] & 0xFF)
			{
			case 0x37:	Type=2; ItemOk=true; break;
			case 0x39:	Type=3; ItemOk=true; break;
			case 0x3B:	Type=4; ItemOk=true; break;
			case 0x3D:	Type=5; ItemOk=true; break;
			}
		break;
		}


		if (ItemOk)
		{
			FaseItemTab[Stage][JugarLevel][Pos][0] = 30;					// ID
			FaseItemTab[Stage][JugarLevel][Pos][1] = (byte)Type;			// Type
			FaseItemTab[Stage][JugarLevel][Pos][2] = (byte)(i%FaseSizeX);	// X
			FaseItemTab[Stage][JugarLevel][Pos][3] = (byte)(i/FaseSizeX);	// Y
			Pos++;
		}
	}
// -----------------------------------------------------------------------
}



// -------------------
// Fase RUN
// ===================

public void FaseRUN()
{
	ProtRUN();


	EnemyProximo = null;
	EnemyDist = 1000000;

	EnemyRUN();
	SeguraRUN();

	if (EnemyProximo!=null)
	{
	EnemyProximo.Herido = (new int[]{1,2,4,50}[ProtType]);
	EnemyProximo = null;
	}

	AniSprRUN();

	FaseRUN_Level();
}


// -------------------
// Fase RUN Level
// ===================

public void FaseRUN_Level()
{

	if (Stage==0)
	{
		switch (JugarLevel)
		{
		case 6:
			if ( Colision( 8*8, 6*8, 8*8, 1*8,   ProtX+ProtCuX, ProtY+ProtCuY,  ProtCuSizeX, ProtCuSizeY) )
			{
			FaseExit=1;
			JugarLevelNext = 92;
			}

			if (!JugarFabrica && ProtDisp && ProtType==3 && ProtSideY==-1 && ProtX>7*8 && ProtX<16*8)
			{
			JugarFabrica = true;
			FaseMap[(12*FaseSizeX)+ 9]=0x1A;
			FaseMap[(12*FaseSizeX)+10]=0x1A;
			FaseMap[(12*FaseSizeX)+11]=0x1A;
			FaseMap[(12*FaseSizeX)+12]=0x1A;
			FaseMap[(12*FaseSizeX)+13]=0x1A;
			FaseMap[(12*FaseSizeX)+14]=0x1A;

		gc.SoundSET(1,1);

			AniSprSET(-1, ( 9*8)-4, (11*8)-4, 3,5, 1, 0x200);
			AniSprSET(-1, (12*8)-4, (11*8)-4, 3,5, 1, 0x200);
			}

		break;
		}
	}

	if (Stage==1)
	{
		switch (JugarLevel)
		{
		case 2:
			if ( Colision( 2*8, 7*8, 4*8, 1*8,   ProtX+ProtCuX, ProtY+ProtCuY,  ProtCuSizeX, ProtCuSizeY) )
			{
			ProtFaseID=10;
			FaseExit=1;
			JugarLevelNext = 3;
			}
		break;

		case 8:
			if ( Colision( 2*8, 7*8, 4*8, 1*8,   ProtX+ProtCuX, ProtY+ProtCuY,  ProtCuSizeX, ProtCuSizeY) )
			{
			ProtFaseID=10;
			FaseExit=1;
			JugarLevelNext = 9;
			}
		break;
		}
	}

	EnemyADD();

}




// --------------------
// Fase Check Tiles
// ====================

int FaseChkDir;

public int CheckTile(int X, int Y, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y >= FaseSizeY*8) {Y=(FaseSizeY-1)*8;}
	if (X < 0) {X=0;} else if (X >= FaseSizeX*8) {X=(FaseSizeX-1)*8;}

	FaseChkDir = ((Y/8)*FaseSizeX)+(X/8);
	return ((int)TilDatTab[FaseMap[FaseChkDir]+128]) & Mask;
}


public int CheckTile(int X, int Y, int SizeX, int SizeY, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y+SizeY >= FaseSizeY*8) {Y=(FaseSizeY-1)*8; SizeY=1;}
	if (X < 0) {X=0;} else if (X+SizeX >= FaseSizeX*8) {X=(FaseSizeX-1)*8; SizeX=1;}

int TabXY = ((Y/8)*FaseSizeX)+(X/8);
int TabSizeX = ((X+SizeX)/8)-(X/8);
int TabSizeY = ((Y+SizeY)/8)-(Y/8);

int Dato=0;

	for (; TabSizeY>-1 ; TabSizeY--)
	{
		for (int i=TabSizeX ; i>-1 ; i--)
		{
			Dato |= TilDatTab[FaseMap[TabXY+i]+128];
		}
	TabXY+=FaseSizeX;
	}

	return (int)(Dato & Mask);
}


byte TilDatTab[];
byte TilDatTab0[] = new byte[256];
byte TilDatTab1[] = new byte[256];
byte TilDatTab2[] = new byte[256];

/*
final byte TilDatTab0[] = new byte[] {
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 8
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 9
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // A
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // B
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // C
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // D
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // E
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // F
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x000,0x000,0x000, // 0
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x000,0x000,0x000,0x000, // 1
0x000,0x000,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x001,0x001,0x001,0x000,0x000,0x000,0x000, // 2
0x000,0x000,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x001, // 3
0x001,0x001,0x000,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x001, // 4
0x001,0x001,0x000,0x000,0x000,0x001,0x001,0x001,0x000,0x001,0x001,0x000,0x000,0x001,0x001,0x001, // 5
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x001,0x000,0x001,0x001,0x001, // 6
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x000,0x000,0x010,0x000, // 7
};


final byte TilDatTab1[] = new byte[] {
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // 8
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 9
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // A
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // B
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // C
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // D
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // E
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // F
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x000,0x000,0x000,0x000,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x001, // 0
0x000,0x001,0x001,0x001,0x001,0x001,0x000,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000, // 1
0x001,0x001,0x001,0x000,0x000,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000, // 2
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 3
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 4
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 5
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 6
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 7
};


final byte TilDatTab2[] = new byte[] {
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 8
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 9
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // A
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // B
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // C
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // D
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // E
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // F
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000, // 0
0x000,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000, // 1
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000, // 2
0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 3
0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 4
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000, // 5
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000, // 6
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000, // 7
};
*/

// <=- <=- <=- <=- <=-








// *******************
// -------------------
// Prot - Engine
// ===================
// *******************

static final int ProtCuX =  10;
static final int ProtCuY =  10;
static final int ProtCuSizeX = 12;
static final int ProtCuSizeY = 12;

static final int ProtCuCentX =  16;
static final int ProtCuCentY =  16;

int ProtSpd=3;

boolean ProtDisp;
boolean ProtInmune = false;

int ProtX;
int ProtY;

int ProtFaseID;

int ProtAnim=-1;

int ProtSide;
int ProtSideX;
int ProtSideY;

int ProtMode;
int ProtType;

// -------------------
// Prot INI
// ===================

public void ProtINI()
{
	ProtAnim = AniSprSET(-1, 100,100, 0,6, 1, 0x001);
}

// -------------------
// Prot END
// ===================

public void ProtEND()
{
}

// -------------------
// Prot SET
// ===================

public void ProtSET()
{
	ProtDisp = false;


	switch (ProtFaseID)
	{
	case 10:
		ProtY = (FaseSizeY*8)-ProtCuCentY;
	break;

	case 11:
		ProtX = (FaseSizeX*8)-ProtCuCentX;
	break;

	case 12:
		ProtY = -ProtCuCentY;

		if (Stage==1 && (JugarLevel==2 || JugarLevel==8)) {ProtY=8*8;}
	break;

	case 13:
		ProtX = -ProtCuCentX;
	break;

	default:
		switch (Stage)
		{
		case 0:
			if (ProtFaseID==0)
			{
			ProtX=11*8;
			ProtY=44*8;
			ProtSide=6;
			} else if (ProtFaseID==1) {
			ProtX=0*8;
			ProtY=4*8;
			ProtSide=0;
			} else {
			ProtX=10*8;
			ProtY=7*8;
			ProtSide=2;
			}
		break;

		case 1:
			ProtX=13*8;
			ProtY=28*8;
			ProtSide=6;
		break;

		case 2:
			ProtX=11*8;
			ProtY=28*8;
			ProtSide=6;
		break;
		}
	break;
	}

	ProtSideX=1;
	ProtSideY=0;

	ProtCaminarSET();
}

// -------------------
// Prot RUN
// ===================

public void ProtRUN()
{
	ProtDisp = false;


	switch (ProtMode)
	{
	case 0:	ProtCaminarRUN();	break;	// Caminar
	case 80:ProtMorirRUN();		break;	// Morir
	}


	if (ProtMode<80)
	{
		ProtFaseCheck();
	}



	if (ProtX + ProtCuCentX < 0) {ProtX = -ProtCuCentX; ProtY+=FaseExitCheck(11, (ProtY+ProtCuCentY)/8);}
	else
	if (ProtX + ProtCuCentX > FaseSizeX*8) {ProtX = (FaseSizeX*8)-ProtCuCentX; ProtY+=FaseExitCheck(13, (ProtY+ProtCuCentY)/8);}
	else
	if (ProtY + ProtCuCentY < 0) {ProtY = -ProtCuCentY; ProtX+=FaseExitCheck(10, (ProtX+ProtCuCentX)/8);}
	else
	if (ProtY + ProtCuCentY > FaseSizeY*8) {ProtY = (FaseSizeY*8)-ProtCuCentY; ProtX+=FaseExitCheck(12, (ProtX+ProtCuCentX)/8);}




	AniSprs[ProtAnim].CoorX = ProtX;
	AniSprs[ProtAnim].CoorY = ProtY;


	if (ProtMode<80)
	{
	AniSprs[ProtAnim].FrameBase = (ProtSide*24) + ProtType*6;
	}

}


public int FaseExitCheck(int Type, int Coor)
{
	for (int i=0 ; i<FaseExitTab[Stage][JugarLevel].length ; i++)
	{
		if ( FaseExitTab[Stage][JugarLevel][i][0]==Type && Coor >= FaseExitTab[Stage][JugarLevel][i][1] && Coor <= FaseExitTab[Stage][JugarLevel][i][2])
		{
		ProtFaseID=Type;
		FaseExit=1;
		JugarLevelNext = FaseExitTab[Stage][JugarLevel][i][3];
		return FaseExitTab[Stage][JugarLevel][i][4] * 8;
		}
	}

	return 0;
}


// -------------------
// ProtFaseCheck
// ===================

public void ProtFaseCheck()
{
// ---------------------------
// Chequeamos Objetos
// ===========================

	if ( Cabina && CheckTile(ProtX+ProtCuCentX-2, ProtY+ProtCuCentY-2, 3,3, 0x10) != 0 ) {Cabina=false; SantiSET();}


	byte Suelo = FaseSuelo[Stage];

	if (JugarLlaves > 0)
	{
		int X = ProtX + ProtCuX + (ProtSideX * TileSize);
		int Y = ProtY + ProtCuY + (ProtSideY * TileSize);

		for (int i=0 ; i<FasePortaTab[Stage][JugarLevel].length ; i++)
		{
	//		Puertas
	//		---------
			boolean PuertaAbrir = false;

			int ID = FasePortaTab[Stage][JugarLevel][i][0];

			if ( ID==20 && Colision((FasePortaTab[Stage][JugarLevel][i][2]+1)*TileSize, (FasePortaTab[Stage][JugarLevel][i][3])*TileSize, 3*TileSize, 1*TileSize,   X,Y, ProtCuSizeX,ProtCuSizeY) )
			{
			int MapDir = (FasePortaTab[Stage][JugarLevel][i][3] * FaseSizeX) + FasePortaTab[Stage][JugarLevel][i][2];
			FaseMap[++MapDir]=Suelo;
			FaseMap[++MapDir]=Suelo;
			FaseMap[++MapDir]=Suelo;

			FasePortaTab[Stage][JugarLevel][i][0] *= -1;
			PuertaAbrir = true;
			}

			else
			if ( ID==21 && Colision((FasePortaTab[Stage][JugarLevel][i][2])*TileSize, (FasePortaTab[Stage][JugarLevel][i][3]+1)*TileSize, 1*TileSize, 3*TileSize,   X,Y, ProtCuSizeX,ProtCuSizeY) )
			{
			int MapDir = (FasePortaTab[Stage][JugarLevel][i][3] * FaseSizeX) + FasePortaTab[Stage][JugarLevel][i][2];
			FaseMap[MapDir+=FaseSizeX]=Suelo;
			FaseMap[MapDir+=FaseSizeX]=Suelo;
			FaseMap[MapDir+=FaseSizeX]=Suelo;

			FasePortaTab[Stage][JugarLevel][i][0] *= -1;
			PuertaAbrir = true;
			}

			else
			if ( ID==22 && Colision((FasePortaTab[Stage][JugarLevel][i][2]-3)*TileSize, (FasePortaTab[Stage][JugarLevel][i][3])*TileSize, 3*TileSize, 1*TileSize,   X,Y, ProtCuSizeX,ProtCuSizeY) )
			{
			int MapDir = (FasePortaTab[Stage][JugarLevel][i][3] * FaseSizeX) + FasePortaTab[Stage][JugarLevel][i][2];
			FaseMap[--MapDir]=Suelo;
			FaseMap[--MapDir]=Suelo;
			FaseMap[--MapDir]=Suelo;

			FasePortaTab[Stage][JugarLevel][i][0] *= -1;
			PuertaAbrir = true;
			}

			else
			if ( ID==23 && Colision((FasePortaTab[Stage][JugarLevel][i][2])*TileSize, (FasePortaTab[Stage][JugarLevel][i][3]-3)*TileSize, 1*TileSize, 3*TileSize,   X,Y, ProtCuSizeX,ProtCuSizeY) )
			{
			int MapDir = (FasePortaTab[Stage][JugarLevel][i][3] * FaseSizeX) + FasePortaTab[Stage][JugarLevel][i][2];
			FaseMap[MapDir-=FaseSizeX]=Suelo;
			FaseMap[MapDir-=FaseSizeX]=Suelo;
			FaseMap[MapDir-=FaseSizeX]=Suelo;

			FasePortaTab[Stage][JugarLevel][i][0] *= -1;
			PuertaAbrir = true;
			}


			if (PuertaAbrir)
			{
			gc.SoundSET(5,1);

			JugarLlaves--;

			int PuertaDir = FasePortaTab[Stage][JugarLevel][i][1];
			int MapDir = (FasePortaTab[Stage][JugarLevel][i][3] * FaseSizeX) + FasePortaTab[Stage][JugarLevel][i][2];

				switch (PuertaDir)
				{
				case 0:
					FaseMap[++MapDir]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[++MapDir]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[++MapDir]=FasePortaTiles[Stage][PuertaDir][2];
				break;

				case 1:
					FaseMap[MapDir+=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[MapDir+=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[MapDir+=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][2];
				break;

				case 2:
					FaseMap[--MapDir]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[--MapDir]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[--MapDir]=FasePortaTiles[Stage][PuertaDir][2];
				break;

				case 3:
					FaseMap[MapDir-=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][0];
					FaseMap[MapDir-=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][1];
					FaseMap[MapDir-=FaseSizeX]=FasePortaTiles[Stage][PuertaDir][2];
				break;
				}
			}

		}
	}


	for (int i=0 ; i<FaseItemTab[Stage][JugarLevel].length ; i++)
	{
//		Items
//		---------
		if ( FaseItemTab[Stage][JugarLevel][i][0]==30 && Colision(FaseItemTab[Stage][JugarLevel][i][2]*TileSize, FaseItemTab[Stage][JugarLevel][i][3]*TileSize, 1*TileSize, 1*TileSize,   ProtX+ProtCuX,ProtY+ProtCuY, ProtCuSizeX,ProtCuSizeY) )
		{
		int MapDir = (FaseItemTab[Stage][JugarLevel][i][3] * FaseSizeX) + FaseItemTab[Stage][JugarLevel][i][2];
		FaseMap[MapDir]=Suelo;
		FaseMap[MapDir+1]=Suelo;
		FaseMap[MapDir+=FaseSizeX]=Suelo;
		FaseMap[MapDir+1]=Suelo;

		FaseItemTab[Stage][JugarLevel][i][0] *= -1;

			switch (FaseItemTab[Stage][JugarLevel][i][1])
			{
			case 3:
				if (JugarState==1) {JugarState=2;}
			case 2:
			case 1:
				ProtType=FaseItemTab[Stage][JugarLevel][i][1];
				JugarBalas[ProtType]+=JugarBalasItem[ProtType];
				if (JugarBalas[ProtType] > 999) {JugarBalas[ProtType]=999;}
			break;

			case 4:		// Botiquin
				JugarEnergy+=20;
				if (JugarEnergy > 99) {JugarEnergy=99;}
			break;

			case 5:		// Llave
				JugarLlaves++;
				if (JugarLlaves > 99) {JugarLlaves=99;}
			break;
			}

		gc.SoundSET(2,1);
		}
	}

// ---------------------------

}






// <<<<<<<<<<<<<<<<<<<


byte[] ProtDispXTab = new byte[] {16,16,0,-16,-16,-16,0,16};
byte[] ProtDispYTab = new byte[] {0,16,16,16,0,-16,-16,-16};


// -------------------
// ProtCaminarSET
// ===================

public void ProtCaminarSET()
{
	ProtMode = 0;
	AniSprSET(ProtAnim, 0,6,1, 0x021);
}

// -------------------
// ProtCaminarRUN
// ===================

public void ProtCaminarRUN()
{
	if (JugarEnergy <= 0) {ProtMorirSET(); return;}


	if (KeybM1>0 && KeybM1!=KeybM2) {ProtDispSET();}


	if (KeybB1==10 && KeybB1!=KeybB2)
	{
		for (int i=0 ; i<4 ; i++)
		{
		ProtType++; ProtType&=3;
		if (ProtType==0 || JugarBalas[ProtType]>0) {gc.SoundSET(6,1); break;}
		}
	}


	if (KeybX1 != 0 || KeybY1 != 0) {AniSprs[ProtAnim].Pause=1;}




	if (KeybX1==0 && KeybY1==0)
	{
		AniSprs[ProtAnim].FrameAct = 0;
	} else {
		if (KeybX1!=0)
		{
		ProtSideX = KeybX1;
			if ( CheckTile(ProtX+ProtCuX+(KeybX1*ProtSpd), ProtY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			ProtX+=KeybX1*ProtSpd;
			AniSprs[ProtAnim].Pause = 1;
			}

			else if ( CheckTile(ProtX+ProtCuX+(KeybX1*ProtSpd), ProtY+ProtCuY-TileSize, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			ProtY-=ProtSpd;
			ProtX+=KeybX1*ProtSpd;
			AniSprs[ProtAnim].Pause = 1;
			}

			else if ( CheckTile(ProtX+ProtCuX+(KeybX1*ProtSpd), ProtY+ProtCuY+TileSize, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			ProtY+=ProtSpd;
			ProtX+=KeybX1*ProtSpd;
			AniSprs[ProtAnim].Pause = 1;
			}
		}

		if (KeybY1!=0)
		{
		ProtSideY = KeybY1;
			if ( CheckTile(ProtX+ProtCuX, ProtY+ProtCuY+(KeybY1*ProtSpd), ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			ProtY+=KeybY1*ProtSpd;
			AniSprs[ProtAnim].Pause = 1;
			}

			else if ( CheckTile(ProtX+ProtCuX-TileSize, ProtY+ProtCuY+(KeybY1*ProtSpd), ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			ProtX-=ProtSpd;
			ProtY+=KeybY1*ProtSpd;
			AniSprs[ProtAnim].Pause = 1;
			}

			else if ( CheckTile(ProtX+ProtCuX+TileSize, ProtY+ProtCuY+(KeybY1*ProtSpd), ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			ProtX+=ProtSpd;
			ProtY+=KeybY1*ProtSpd;
			AniSprs[ProtAnim].Pause = 1;
			}
		}
	}




	if (KeybX1== 1 && KeybY1== 0) {ProtSide=0;} else
	if (KeybX1== 1 && KeybY1== 1) {ProtSide=1;} else
	if (KeybX1== 0 && KeybY1== 1) {ProtSide=2;} else
	if (KeybX1==-1 && KeybY1== 1) {ProtSide=3;} else
	if (KeybX1==-1 && KeybY1== 0) {ProtSide=4;} else
	if (KeybX1==-1 && KeybY1==-1) {ProtSide=5;} else
	if (KeybX1== 0 && KeybY1==-1) {ProtSide=6;} else
	if (KeybX1== 1 && KeybY1==-1) {ProtSide=7;}


}

// <<<<<<<<<<<<<<<<<<<

// -------------------
// ProtMorirSET
// ===================

public void ProtMorirSET()
{
	gc.VibraSET(300);
	gc.SoundSET(4,1);

	JugarEnergy=0;

	ProtMode = 80;
	AniSprSET(ProtAnim, 0, 7*24, 0, 0x007);
	AniSprs[ProtAnim].Side = 24;
	AniSprs[ProtAnim].Repetir = 4;
	AniSprs[ProtAnim].FrameBase = 0;
}

// -------------------
// ProtMorirRUN
// ===================

public void ProtMorirRUN()
{
	JugarEnergy=0;

	if (AniSprs[ProtAnim].Pause==0)
	{
	FaseExit=3;
	}
}

// <<<<<<<<<<<<<<<<<<<


// -------------------
// ProtDispSET
// ===================

public void ProtDispSET()
{
	ProtDisp = true;

	int Spr = AniSprSET(-1, ProtX+ProtDispXTab[ProtSide],ProtY+ProtDispYTab[ProtSide], (ProtSide*24)+(ProtType*6), 2*192, 1, 0x700);
	if (Spr!=-1) {AniSprs[Spr].Side=192;}

	if (ProtType>0 && --JugarBalas[ProtType] < 1)
	{
		while (ProtType>0)
		{
		if (JugarBalas[--ProtType]>0) {break;}
		}
	}
}

// <=- <=- <=- <=- <=-











// ********************
// --------------------
// Anim Sprite - Engine
// ====================
// ********************

final int AniSprMAX=32;
int AniSprC;
int[] AniSprP;
AnimSprite[] AniSprs;

// ---------------
// Anim Sprite SET
// ===============

public int AniSprSET(int i, int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	if (i<0)
	{
	if (AniSprC == AniSprMAX) {return(-1);}
	i=AniSprP[AniSprC++];
	AniSprs[i] = new AnimSprite();
	}

	AniSprs[i].SpriteSET(CoorX, CoorY, FrameIni, Frames, Speed, Mode);

	return (i);
}


public int AniSprSET(int i, int FrameIni, int Frames, int Speed, int Mode)
{
	return AniSprSET(i, AniSprs[i].CoorX, AniSprs[i].CoorY, FrameIni, Frames, Speed, Mode);
}


// ---------------
// Anim Sprite INI
// ===============

public void AniSprINI()
{
	AniSprC = 0;
	AniSprP = new int[AniSprMAX];
	AniSprs = new AnimSprite[AniSprMAX];
	for (int i=0 ; i<AniSprMAX ; i++) {AniSprP[i]=i;}
}

// ---------------
// Anim Sprite RES
// ===============

public int AniSprRES(int i)
{
	if (i!=-1) {AniSprs[i].Frames=0; AniSprs[i].Pause=0;}
	return -1;
}

// ---------------
// Anim Sprite END
// ===============

public void AniSprEND()
{
	AniSprs = null; AniSprP = null; AniSprC = 0;
}

// ---------------
// Anim Sprite RUN
// ===============

public void AniSprRUN()
{
	for (int i=0 ; i<AniSprC ; i++)
	{
	if ( AniSprs[AniSprP[i]].Play() )
		{
		int t=AniSprP[i]; AniSprs[t] = null;
		AniSprP[i--] = AniSprP[--AniSprC]; AniSprP[AniSprC] = t;
		}
	}
}

// -------------------
// Anim Sprite Ordenar
// ===================
/*
public void AniSprOrdenar()
{
	int Conta=AniSprC-1;

	for (int t=0 ; t<AniSprC ; t++)
	{
		for (int i=0 ; i<Conta ; i++)
		{
			if (AniSprs[AniSprP[i]].Prio < AniSprs[AniSprP[i+1]].Prio)
			{
			int dato=AniSprP[i+1];
			AniSprP[i+1] = AniSprP[i];
			AniSprP[i] = dato;
			}
		}
	Conta--;
	}
}
*/
// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Enemy - Engine
// ====================
// ********************

static final int EnemyCuX = 8;
static final int EnemyCuY = 8;
static final int EnemyCuSizeX = 16;
static final int EnemyCuSizeY = 16;
static final int EnemyCuCentX = 16;
static final int EnemyCuCentY = 16;

int EnemyTime;
int EnemyCnt=0;

Enemy EnemyProximo;
int EnemyDist;

// --------------------
// Enemy ADD
// ====================

public void EnemyADD()
{

	if (EnemyC == EnemyMAX) {return;}
	if (--EnemyTime>0) {return;}

	if (SeguraAnim==-1)
	{
		EnemyTime = 50+RND(50);

		int X = gc.sc.ScrollX + gc.sc.ScrollSizeX;
		int Y = ProtY;

		for (int t=0 ; t<4 ; t++)
		{
			EnemyCnt++;
			switch (EnemyCnt&=0x3)
			{
			case 1:
				X = ProtX;
				Y = gc.sc.ScrollY + gc.sc.ScrollSizeY;
			break;

			case 2:
				X = gc.sc.ScrollX-(EnemyCuCentX*2);
				Y = ProtY;
			break;

			case 3:
				X = ProtX;
				Y = gc.sc.ScrollY-(EnemyCuCentY*2);
			break;
			}


			if ( CheckTile(X+EnemyCuX, Y+EnemyCuY, EnemyCuSizeX-1, EnemyCuSizeY-1, 0x01) == 0 )
			{
				boolean Ok=true;
				for (int i=0 ; i<EnemyC ; i++)
				{
					if ( Colision(X+EnemyCuX, Y+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY, Enemys[EnemyP[i]].CoorX+EnemyCuX, Enemys[EnemyP[i]].CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY ) )
					{
					Ok = false;
					break;
					}
				}

				if (Ok)
				{
				EnemySET(X, Y);
				break;
				}
			}
		}

	} else {

		int X=0;
		switch (RND(2))
		{
		case 0:
			X = 20*8;
		break;		

		case 1:
			X = 2*8;
		break;		
		}

		boolean Ok=true;
		for (int i=0 ; i<EnemyC ; i++)
		{
			if ( Colision(X+EnemyCuX, (3*8)+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY, Enemys[EnemyP[i]].CoorX+EnemyCuX, Enemys[EnemyP[i]].CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY ) )
			{
			Ok = false;
			break;
			}
		}

		if (Ok)
		{
		EnemySET(X, 3*8);
		}

		EnemyTime=3;

	}

}

// --------------------
// Enemy class
// ====================

public class Enemy extends AnimSprite
{
boolean Exit = false;
int SideX;
int SideY;
int Mode;
int Life;
int Time;
int Herido = 0;
int Type;

// ----------------
// Enemy Play
// ================

public boolean Play()
{

	switch (Mode)
	{
	case 0:	CaminarRUN();	break;	// Caminar
	case 1:	TocadoRUN();	break;	// Tocado
	case 80:MorirRUN();		break;	// Morir
	}

	if (Exit) {return true;}

	return super.Play();
}

// <<<<<<<<<<<<<<<<<<<


// -------------------
// Caminar SET
// ===================

public void CaminarSET()
{
	SpriteSET(0, 4, 2, 0x101);
	Mode = 0;
}

// -------------------
// Caminar RUN
// ===================

public void CaminarRUN()
{
	if ( Herido != 0 )
	{
		if ( (Life-=Herido) > 0 )
		{
		TocadoSET();
		} else {
		MorirSET();
		}
	Herido=0;
	return;
	}



	if ( EnemyCheck(CoorX+EnemyCuX, CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY) != 0 )
	{
		int Dist = Math.abs(CoorX-ProtX) + Math.abs(CoorY-ProtY);
		if (EnemyProximo==null || Dist < EnemyDist)
		{
		EnemyDist=Dist;
		EnemyProximo = this;
		}
	}



	SideX = SideY = 0;
	if (CoorX != ProtX) {SideX= CoorX < ProtX? 1:-1;}
	if (CoorY != ProtY) {SideY= CoorY < ProtY? 1:-1;}


	if ( !Colision(CoorX+EnemyCuX, CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY,  ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY) )
	{
		if (SideX!=0 && CheckTile(CoorX+EnemyCuX+SideX, CoorY+EnemyCuY, EnemyCuSizeX-1, EnemyCuSizeY-1, 0x01) == 0) 
		{
			boolean Ok=true;
			for (int i=0 ; i<EnemyC ; i++)
			{
				if (Enemys[EnemyP[i]] == this) {continue;}

				if ( Colision(CoorX+EnemyCuX+SideX, CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY, Enemys[EnemyP[i]].CoorX+EnemyCuX, Enemys[EnemyP[i]].CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY ) )
				{
				Ok = false;
				break;
				}
			}

			if (Ok) {CoorX+=SideX;} else {SideX=0;}
		}

		if (SideY!=0 && CheckTile(CoorX+EnemyCuX, CoorY+EnemyCuY+SideY, EnemyCuSizeX-1, EnemyCuSizeY-1, 0x01) == 0)
		{
			boolean Ok=true;
			for (int i=0 ; i<EnemyC ; i++)
			{
				if (Enemys[EnemyP[i]] == this) {continue;}

				if ( Colision(CoorX+EnemyCuX, CoorY+EnemyCuY+SideY, EnemyCuSizeX, EnemyCuSizeY, Enemys[EnemyP[i]].CoorX+EnemyCuX, Enemys[EnemyP[i]].CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY ) )
				{
				Ok = false;
				break;
				}
			}

			if (Ok) {CoorY+=SideY;} else {SideY=0;}
		}
	} else {
		if (!ProtInmune && ProtMode<80) {JugarEnergy--;}
	}



	if (SideX== 1 && SideY== 0) {FrameBase=(0*16)+(Type*4);} else
	if (SideX== 1 && SideY== 1) {FrameBase=(1*16)+(Type*4);} else
	if (SideX== 0 && SideY== 1) {FrameBase=(2*16)+(Type*4);} else
	if (SideX==-1 && SideY== 1) {FrameBase=(3*16)+(Type*4);} else
	if (SideX==-1 && SideY== 0) {FrameBase=(4*16)+(Type*4);} else
	if (SideX==-1 && SideY==-1) {FrameBase=(5*16)+(Type*4);} else
	if (SideX== 0 && SideY==-1) {FrameBase=(6*16)+(Type*4);} else
	if (SideX== 1 && SideY==-1) {FrameBase=(7*16)+(Type*4);}


}

// <<<<<<<<<<<<<<<<<<<

// -------------------
// Tocado SET
// ===================

public void TocadoSET()
{
	AniSprSET(-1, CoorX, CoorY, 3,2,1, 0x210);

	SpriteSET(0, 4, 0, 0x112);
	Mode = 1;
}

// -------------------
// Tocado RUN
// ===================

public void TocadoRUN()
{
	if (Pause==0) {CaminarSET();}
}

// <<<<<<<<<<<<<<<<<<<

// -------------------
// Morir SET
// ===================

public void MorirSET()
{
	gc.SoundSET(3,1);

	SpriteSET(0, 3, 1, 0x202);
	FrameBase=0;
	Time = 10;
	Mode = 80;
}

// -------------------
// Morir RUN
// ===================

public void MorirRUN()
{
	if (Pause==0 && --Time<0) {Exit=true;}
}

// <<<<<<<<<<<<<<<<<<<

};


// ---------------
// Enemy SET
// ===============

public int EnemySET(int CoorX, int CoorY)
{
	if (EnemyC == EnemyMAX) {return(-1);}

	int i=EnemyP[EnemyC++];

	Enemys[i] = new Enemy();

	Enemys[i].CaminarSET();
	Enemys[i].CoorX = CoorX;
	Enemys[i].CoorY = CoorY;

	Enemys[i].Type = RND(4);
	Enemys[i].Life = RND(5)+1;

	return (i);
}


// ---------------
// Enemy INI
// ===============

static final int EnemyMAX=4;
int EnemyC;
int[] EnemyP;
Enemy[] Enemys;

public void EnemyINI()
{
	EnemyC = 0;
	EnemyP = new int[EnemyMAX];
	Enemys = new Enemy[EnemyMAX];
	for (int i=0 ; i<EnemyMAX ; i++) {EnemyP[i]=i;}
}

// ---------------
// Enemy RES
// ===============

public void EnemyRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=EnemyP[i];
	Enemys[t] = null;
	EnemyP[i--] = EnemyP[--EnemyC];
	EnemyP[EnemyC] = t;
}

// ---------------
// Enemy END
// ===============

public void EnemyEND()
{
	EnemyP = null;
	Enemys = null;
	EnemyC = 0;
}

// ---------------
// Enemy RUN
// ===============

public void EnemyRUN()
{
	for (int i=0 ; i<EnemyC ; i++) {if ( Enemys[EnemyP[i]].Play() ){int t=EnemyP[i];Enemys[t] = null;EnemyP[i--] = EnemyP[--EnemyC];EnemyP[EnemyC] = t;}}
}


// ---------------
// Enemy Check
// ===============

public int EnemyCheck(int X, int Y, int SizeX, int SizeY)
{
	boolean Tocado = false;

	if (ProtDisp)
	{
		switch (ProtSide)
		{
		case 0:
			if ( Colision(ProtX+ProtCuCentX, ProtY+ProtCuY, gc.CanvasSizeX, ProtCuSizeY,  X,Y, SizeX,SizeY) ) {Tocado = true;}
		break;		

		case 1:
			if ( Colision(ProtX+ProtCuCentX, ProtY+ProtCuCentY, gc.CanvasSizeX, gc.CanvasSizeY,  X,Y, SizeX,SizeY) )
			{
			int Desp = (X - ProtX) - (Y - ProtY);
			if (Desp > -15 && Desp < 15) {Tocado = true;}
			}
		break;

		case 2:
			if ( Colision(ProtX+ProtCuX, ProtY+ProtCuCentY, ProtCuSizeX, gc.CanvasSizeY,  X,Y, SizeX,SizeY) ) {Tocado = true;}
		break;		

		case 3:
			if ( Colision(ProtX+ProtCuCentX-gc.CanvasSizeX, ProtY+ProtCuCentY, gc.CanvasSizeX, gc.CanvasSizeY,  X,Y, SizeX,SizeY) )
			{
			int Desp = (ProtX - X) - (Y - ProtY);
			if (Desp > -15 && Desp < 15) {Tocado = true;}
			}
		break;

		case 4:
			if ( Colision(ProtX+ProtCuCentX-gc.CanvasSizeX, ProtY+ProtCuY, gc.CanvasSizeX, ProtCuSizeY,  X,Y, SizeX,SizeY) ) {Tocado = true;}
		break;		

		case 5:
			if ( Colision(ProtX+ProtCuCentX-gc.CanvasSizeX, ProtY+ProtCuCentY-gc.CanvasSizeY, gc.CanvasSizeX, gc.CanvasSizeY,  X,Y, SizeX,SizeY) )
			{
			int Desp = (ProtX - X) - (ProtY - Y);
			if (Desp > -15 && Desp < 15) {Tocado = true;}
			}
		break;

		case 6:
			if ( Colision(ProtX+ProtCuX, ProtY+ProtCuCentY-gc.CanvasSizeY, ProtCuSizeX, gc.CanvasSizeY,  X,Y, SizeX,SizeY) ) {Tocado = true;}
		break;		

		case 7:
			if ( Colision(ProtX+ProtCuCentX, ProtY+ProtCuCentY-gc.CanvasSizeY, gc.CanvasSizeX, gc.CanvasSizeY,  X,Y, SizeX,SizeY) )
			{
			int Desp = (X - ProtX) - (ProtY - Y);
			if (Desp > -15 && Desp < 15) {Tocado = true;}
			}
		break;
		}

	}

	return Tocado? (new int[]{1,2,4,10}[ProtType]):0;
}




// <=- <=- <=- <=- <=-













// ********************
// --------------------
// Santi - Engine
// ====================
// ********************

boolean SantiPaint = false;
int SantiGameStatus;
int SantiType;
int SantiTime;
/*
String[] SantiTexto = new String[]
{
"COLEGA, TIENES QUE ENCONTRAR LA CASA DE AIJON. A VER SI ESPABILAMOS QUE ESTO ESTA LLENO DE ZOMBIS. ZOMBIS CHUNGOS Y CON MALAS PULGAS.",
"YA  TARDABAS TIO, NO VES EL PORTAL QUE TIENES A LA IZQUIERDA? PUES ENTRA HOMBRE, EMPIEZO A PENSAR QUE TE DA CORTE. BUENO ENCUENTRA EL LANZACOHETES QUE ESTA EN EL EDIFICIO. CHAVAL, ES QUE TE LO TENGO QUE DECIR TODO? CUANDO LO CONSIGAS PODRAS ENTRAR EN LA FABRICA,  CARGARTE A TODA LA BANDA DE ZOMBIS Y TAMBIEN A SU JEFE: EL MALVADO ENTRECOT.",
"BUENO, CHAVAL ESTOS TIPOS TE VAN A DESPEDAZAR Y LUEGO SE TE COMERAN. ASI QUE PONTE LAS PILAS Y ENCUENTRA LA FABRICA. SI LAS COSAS SE PONEN CHUNGAS, ABRETE CAMINO CON EL LANZA-COHETES,... A VER SI NOS LO CURRAMOS UN POCO!",
"TIO, YA TE HAS VUELTO A PERDER. PERO MIRA QUE ES FACIL, SOLO TIENES QUE ENCONTRAR LA FABRICA DONDE SE ESCONDE EL ENTRECOT Y CUANDO CONSIGAS ELIMINARLO, SE ACABO EL JUEGO!",
};
*/
// ---------------
// Santi SET
// ===============

public void SantiSET()
{
	int Type=10;

	if (Stage==0)
	{
		switch (JugarLevel)
		{
		case 3:	Type=(JugarState==0? 0:3); break;
		case 10:Type=(JugarState==2? 2:1); break;
		case 8:	Type=(JugarState==0? 0:3); break;
		}
	}


	MenuSET(10+Type);

/*
	SantiType = Type;
	SantiTime = 30;
	SantiPaint = true;

	SantiGameStatus = GameStatus;
	GameStatus = 200;
*/
}

boolean SantiIniFlag;

public void SantiIniSET()
{
	SantiIniFlag = true;
	MenuSET(15);
}


// ---------------
// Santi RES
// ===============

public void SantiRES()
{
	GameStatus = SantiGameStatus;

	SantiPaint = false;
}


// ---------------
// Santi RUN
// ===============

public boolean SantiRUN()
{
	if ( gc.MenuListRUN( (KeybY1!=KeybY2? KeybY1:0) , KeybY1 , (KeybM1>0 && KeybM2!=KeybM1) ) )
	{
		return true;
	}

//	if ( SantiTime==0 && (KeybB1!=0 && KeybB2==0) || (KeybM1!=0 && KeybM2==0) )
//	{
//	return true;
//	}

//	if (SantiTime>0) {SantiTime--;}

	return false;
}

// <=- <=- <=- <=- <=-








// ********************
// --------------------
// Segura - Engine
// ====================
// ********************

int SeguraAnim;
int SeguraMode;
int SeguraType;
int SeguraSide;
int SeguraTime;
int SeguraLife;

// ---------------
// Segura INI
// ===============

public void SeguraINI()
{
	SeguraAnim = -1;
}


// ---------------
// Segura END
// ===============

public void SeguraEND()
{
	SeguraAnim = -1;
}


// ---------------
// Segura SET
// ===============

public void SeguraSET()
{
	SeguraAnim = AniSprSET(-1, 11*8,9*8, 9,1, 2, 0x301);
	SeguraMode=0;
	SeguraType=1;
	SeguraLife = 70;
}

// ---------------
// Segura RUN
// ===============

public void SeguraRUN()
{
	if (SeguraAnim == -1) {return;}


	switch (SeguraMode)
	{
	case 0:
		AniSprs[SeguraAnim].FrameBase = 0;
		AniSprSET(SeguraAnim, 9,1, 2, 0x301);
		SeguraTime=10;
		SeguraMode++;
	break;	

	case 1:
		if (SeguraTime>0) {SeguraTime--;}

		if (SeguraTime==0 && Colision(AniSprs[SeguraAnim].CoorX+8, 0, 16, gc.CanvasSizeY,    ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY) )
		{
			switch (SeguraType)
			{
			case 0:
				SeguraSide=1;
				SeguraType=1;
			break;		

			case 2:
				SeguraSide=-1;
				SeguraType=1;
			break;		

			case 1:
				switch (RND(2))
				{
				case 0:
					SeguraSide=1;
					SeguraType=2;
				break;

				case 1:
					SeguraSide=-1;
					SeguraType=0;
				break;
				}
			break;
			}

			AniSprs[SeguraAnim].FrameBase = SeguraSide==1? 0:16;

			AniSprSET(SeguraAnim, 0,4, 2, 0x301);
			SeguraMode++;
		}
	break;

	case 2:
		AniSprs[SeguraAnim].CoorX += SeguraSide;

		if ((SeguraType==0 && AniSprs[SeguraAnim].CoorX <= 7*8)
		||	(SeguraType==1 && AniSprs[SeguraAnim].CoorX >= 11*8 && AniSprs[SeguraAnim].CoorX <= 12*8)
		||	(SeguraType==2 && AniSprs[SeguraAnim].CoorX >= 15*8))
		{SeguraMode=0;}
	break;

	case 10:
		if (AniSprs[SeguraAnim].Pause==0)
		{
		SeguraAnim = AniSprRES(SeguraAnim);
		SantiSET();
		}		
	break;
	}



	if (SeguraMode < 10 && EnemyProximo == null)
	{
		int Dano = EnemyCheck(AniSprs[SeguraAnim].CoorX+EnemyCuX, AniSprs[SeguraAnim].CoorY+EnemyCuY, EnemyCuSizeX, EnemyCuSizeY);

		if ((SeguraLife-=Dano) < 0)
		{
		AniSprSET(SeguraAnim, AniSprs[SeguraAnim].CoorX, AniSprs[SeguraAnim].CoorY, 3,5, 1, 0x202);
		SeguraMode = 10;
		} else if (Dano>0) {
		AniSprSET(-1, AniSprs[SeguraAnim].CoorX, AniSprs[SeguraAnim].CoorY, 3,2,1, 0x210);
		}
	}




}

// <=- <=- <=- <=- <=-









// ********************
// --------------------
// Menu - Engine
// ====================
// ********************

boolean MenuPaint = false;
int MenuGameStatus;
int MenuExit;

// ---------------
// Menu SET
// ===============

public void MenuSET(int Type)
{
	gc.MenuSET(Type);

	MenuGameStatus = GameStatus;
	GameStatus = 50;

	MenuPaint=true;
	MenuExit=0;
}


// ---------------
// Menu RES
// ===============

public void MenuRES()
{
	GameStatus = MenuGameStatus;

	gc.MenuListEND();

	MenuPaint = false;

	System.gc();
}


// ---------------
// Menu RUN
// ===============

public boolean MenuRUN()
{
	if (gc.MenuType==-1)
	{
		if ((KeybM1!=0 && KeybM2==0) || (KeybB1!=0 && KeybB2==0)) {MenuRES(); MenuSET(0);}
	} else if (gc.MenuType==20)
	{
		if ( gc.MenuListRUN( (KeybY1!=KeybY2? KeybY1:0) , KeybY1 , false ) ) {gc.MenuListEXE();}
	} else {
		if ( gc.MenuListRUN( (KeybY1!=KeybY2? KeybY1:0) , KeybY1 , (KeybM1>0 && KeybM2!=KeybM1) ) ) {gc.MenuListEXE();}
	}

	if (MenuExit!=0) {return true;}

//	if (gc.MenuListUpdate) {gc.MenuListUpdate=false; MenuPaint=true;}

	return false;
}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// Wait - Engine
// ===================
// *******************

long TiempoIni, TiempoFin;
int Retorno;

// -------------------
// Wait INI
// ===================

public void WaitINI(int Tiempo, int Ret)
{
	Retorno=Ret;
	TiempoFin = System.currentTimeMillis() + Tiempo;
}

// -------------------
// Wait SET
// ===================

public void WaitSET()
{
	GameStatus=1000;
}

public void WaitSET(int Tiempo, int Ret)
{
	Retorno=Ret;
	TiempoFin = System.currentTimeMillis() + Tiempo;
	GameStatus=1000;
}

// -------------------
// Wait RUN
// ===================

public void WaitRUN()
{
	if (TiempoFin < System.currentTimeMillis()) {GameStatus=Retorno;}
}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// Prefs - Engine
// ===================
// *******************

byte[] PrefsData = new byte[] {1,1,0};

// -------------------
// Prefs INI
// ===================

public void PrefsINI()
{
	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("Zombies_Prefs", true);

		if (rs.getNumRecords() == 0)
		{
		rs.addRecord(PrefsData, 0, PrefsData.length);
		} else {
		PrefsData=rs.getRecord(1);
		}
		rs.closeRecordStore();
	} catch(Exception e) {}

	GameSound=PrefsData[0]; if (GameSound>1 || GameSound<0) {GameSound=1;}
	GameVibra=PrefsData[1]; if (GameVibra>1 || GameVibra<0) {GameVibra=1;}
}

// -------------------
// Prefs SET
// ===================

public void PrefsSET()
{
	PrefsData[0]=(byte)GameSound;
	PrefsData[1]=(byte)GameVibra;

	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("Zombies_Prefs", true);

		if (rs.getNumRecords() == 0)
		{
		rs.addRecord(PrefsData, 0, PrefsData.length);
		} else {
		rs.setRecord(1, PrefsData, 0, PrefsData.length);
		}
		rs.closeRecordStore();
	} catch(Exception e) {}
}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// Cheat - Engine
// ===================
// *******************

int CheatInmune=0;

int CheatPos_1=0;
boolean Cheat_ON=false;
	
public void CheatRUN(int keycode)
{
	byte[] Cheat_1={7,7,7,7,3,3,4,8,8,7,7,7,2};
	if (Cheat_1[CheatPos_1++] != keycode) {CheatPos_1=0;}
	if (Cheat_1.length==CheatPos_1) {Cheat_ON=true; CheatPos_1=0; ProtInmune=true; JugarLlaves=99; JugarBalas[1]=999; JugarBalas[2]=999; JugarBalas[3]=999;}
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