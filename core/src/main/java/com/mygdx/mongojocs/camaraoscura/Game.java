package com.mygdx.mongojocs.camaraoscura;


// -----------------------------------------------
// Camara Oscura v2.0 Rev.1 (17.7.2003)
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
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Image;
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

	try
	{
		GameRUN();
	}
	catch (Exception e)
	{
	e.printStackTrace();
	e.toString();
	}

		gc.CanvasPaint=true;
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
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	for(int i = 0; i < buffer.length; i++)
		buffer[i] = bytes[i];
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Regla de 3
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public int Regla3(int x, int min, int max) {return (x*max)/min;}
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
// Colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean Colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 > x1+xs1 ) ||	( x2+xs2 < x1 ) ||	( y2 > y1+ys1 ) ||	( y2+ys2 < y1 ));
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
int GameStatusBack=0;

Marco ma;

int GameSound=1;
int GameVibra=1;

// -------------------
// Game INI
// ===================

public void GameINI()
{
	GameSizeX=gc.CanvasSizeX; if (GameSizeX <= GameMaxX) {GameX=0;} else {GameX=(GameSizeX-GameMaxX)/2;GameSizeX=GameMaxX;}
	GameSizeY=gc.CanvasSizeY; if (GameSizeY <= GameMaxY) {GameY=0;} else {GameY=(GameSizeY-GameMaxY)/2;GameSizeY=GameMaxY;}

	ma = new Marco(gc.CanvasSizeX, gc.CanvasSizeY);

	PrefsINI();
}

// -------------------
// Game END
// ===================

public void GameEND()
{
	PrefsSET();
}

// -------------------
// Game SET
// ===================

public void GameSET()
{
}


// -------------------
// Game IMP
// ===================

public void GameIMP()
{
	GameIMP(GameStatus);
}

public void GameIMP(int Status)
{
	switch (Status)
	{
	case 52:
		MenuPaint = true;
	break;

	case 102:
		JugarPaint = true;
	break;

	case 150:
		GloboPos--;
		GloboNext = true;
	break;

	case 172:
		gc.CanvasImg = InicioImg;
	break;
	}
}


// -------------------
// Game RUN
// ===================

public void GameRUN()
{
	switch (GameStatus)
	{
	case 0:
		GameStatus++;
//		GameStatus=100;
	break;

	case 1:
		gc.CanvasImageSET("/Gfx/Manga.png", 0);
		WaitSET(2500, GameStatus+1);
	break;

	case 2:
		gc.CanvasImageSET("/Gfx/Producido.png", 0);
		WaitSET(2500, GameStatus+1);
	break;

	case 3:
		GameStatus=50;
	break;






// ------------------
// Menu Bucle
// ------------------
	case 50:
		MenuINI();
		GameStatus++;
	case 51:
		MenuSET();

		gc.SoundSET(0,0);

		GameStatus++;
	case 52:
		if ( !MenuRUN() ) {break;}

		gc.SoundRES();

		MenuEND();

		GameStatus=170;
	break;
// ===================






// ------------------
// Jugar Bucle
// ------------------
	case 100:
		JugarINI();
		GameStatus++;
	case 101:
		JugarSET();
		GameStatus++;
	case 102:

//		Cheats();

		if (KeybM1==-1 && KeybM1!=KeybM2) {PanelSET(1); break;}

		if ( !JugarRUN() ) {break;}

		FaseEND();


		GameStatus=101;

		switch (FaseExit)
		{
		case 1:	// Pasamos por una Puerta
			JugarLevel = JugarLevelNext;
			GameStatus=101;
		break;

		case 2:	// Perdemos una Vida
		break;

		case 3:	// Terminar Juego
			JugarEND();
			GameStatus = 250;
		break;

		case 4:	// Final del Juego
			JugarEND();
			GameStatus = 200;
		break;
}

		FaseExit=0;
	break;
// ===================

// ------------------
// Globo Bucle
// ------------------
	case 150:
		if (KeybM1==-1 && KeybM1!=KeybM2) {PanelSET(1); break;}

		if ( FaseExit!=0 || GloboRUN() ) {GameStatus = GameStatusBack;}
	break;
// ===================


// ------------------
// Inicio Bucle
// ------------------
	case 170:
		gc.CanvasFillRGB=0;
		gc.CanvasFillPaint = true;
		InicioImg = gc.LoadImage("/Gfx/Inicio.png");
		gc.CanvasImg = InicioImg;

		WaitSET(3*1000, GameStatus+1);
	break;

	case 171:
		GameStatus++;
		GloboSET(Texto.Intro1);
	break;

	case 172:
		gc.CanvasImg = InicioImg;
		FlechitaPaint = true;
		GameStatus++;
	break;

	case 173:
		if (KeybX1!=0 && KeybX1!=KeybX2) {ProtSelect++; ProtSelect &= 0x1; GameStatus--; break;}
		if (KeybM1!=0 && KeybM1!=KeybM2) {gc.CanvasImg = InicioImg; GameStatus++;}
	break;

	case 174:
		InicioImg = null;
		System.gc();

		PanelSET(80);
		gc.CanvasPaint=true; gc.repaint(); gc.serviceRepaints();
		PanelEND();

		GameStatus = 100;
	break;
// ===================




// ------------------
// Over Bucle
// ------------------
	case 250:
		gc.CanvasFillRGB=0;
		gc.CanvasFillPaint = true;
		gc.CanvasImg = gc.LoadImage("/Gfx/Over_ahogo.png");

		WaitSET(5*1000, GameStatus+1);
	break;

	case 251:
		GameStatus = 50;
	break;
// ===================



// ------------------
// Final Bucle
// ------------------
	case 200:
		gc.CanvasFillRGB=0;
		gc.CanvasFillPaint = true;
		gc.CanvasImg = gc.LoadImage("/Gfx/Final.png");

		WaitSET(5*1000, GameStatus+1);
	break;

	case 201:
		GameStatus = 50;
	break;
// ===================



// ------------------
// Panel Bucle
// ------------------
	case 900:
		PanelRUN();
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


Image InicioImg;
boolean FlechitaPaint = false;
byte ProtSelect=0;



// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

int AmigoFr = 0;
int ProtaFr = 20;

boolean JugarPaint=false;
int JugarLevel, JugarLevelNext;
int JugarLlave;
boolean JugarBotiquin;
boolean JugarCurado;
boolean JugarRadio;
boolean JugarRadioOff;
boolean JugarGPS;
boolean JugarBote;
boolean JugarBote2;
boolean JugarNota;

boolean MsgPorta;
boolean MsgTelefono;
boolean MsgGPS;
boolean MsgPalanca;
boolean MsgCorrer;
boolean MsgGrua;

boolean MsgJaula;

boolean MsgBote;

boolean JugarCuchillo;

int JugarConta, JugarCnt;

int BoteX, BoteY, BoteCnt;

boolean BoteWait;
boolean BoteAgua;

int JaulaY, JaulaCnt, JaulaSide;

boolean CamaraProt;

int CapitanAnim;

int JaulaAnim;

boolean JaulaOk;
boolean JaulaSuelo;
boolean AmigoLibre;

boolean JugarEnemyBotiquin;

int ProtVida;

boolean AmigoCapturado;

// -------------------
// Jugar INI
// ===================

public void JugarINI()
{
	if (ProtSelect==0)
	{
	AmigoFr = 0;
	ProtaFr = 20;
	} else {
	AmigoFr = 20;
	ProtaFr = 0;
	}

	JugarLevel=3;
	ProtNewX=-1;

	ProtVida=100;

	JugarLlave=0x01;

	JugarConta = 0; JugarCnt = 0;

	JugarBotiquin = false;
	JugarCurado = false;
	JugarRadio = false;
	JugarRadioOff = false;
	JugarGPS = false;
	JugarBote = false;
	JugarBote2 = false;
	JugarNota = false;

	AmigoDead = false;
	AmigoDeadJaula = false;

	AmigoCapturado = false;

	JaulaOk = false;
	JaulaSuelo = false;
	AmigoLibre = false;

	BoteX = 0;
	BoteY = 0;

	BoteWait = false;
	BoteAgua = false;

	CamaraProt = true;

	MsgPorta = false;
	MsgTelefono = false;
	MsgGPS = false;
	MsgPalanca = false;
	MsgCorrer = false;
	MsgGrua = false;

	MsgJaula = false;

	MsgBote = false;

	JugarCuchillo = false;

	JugarEnemyBotiquin = false;

	JaulaY=8*TileSize;
	JaulaSide=0;

	JugarPaint=false;

	FaseINI();

	gc.JugarINI_Gfx();
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

public void JugarEND()
{
	JugarPaint=false;

	gc.JugarEND_Gfx();
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

int TileSize=8;

static int FaseSizeX=1;
static int FaseSizeY=1;
static byte[] FaseMap;

int FaseExit;

int[][] FaseInfoTab = new int[][]
{{
	112,32,	// Fase Size X,Y	(Fase 0)
},{
	64,16,	// Fase Size X,Y	(Fase 1)
},{
	32,16,	// Fase Size X,Y	(Fase 2)
},{
	32,16,	// Fase Size X,Y	(Fase 3)
},{
	48,16,	// Fase Size X,Y	(Fase 4)
},{
	16,16,	// Fase Size X,Y	(Fase 5) - No existe
},{
	64,16,	// Fase Size X,Y	(Fase 6)
},{
	16,32,	// Fase Size X,Y	(Fase 7)
},{
	16,16,	// Fase Size X,Y	(Fase 8)
},{
	32,16,	// Fase Size X,Y	(Fase 9)
},{
	16,16,	// Fase Size X,Y	(Fase 10)
},{
	16,16,	// Fase Size X,Y	(Fase 11)
},{
	16,16,	// Fase Size X,Y	(Fase 12)
},{
	32,16,	// Fase Size X,Y	(Fase 13)
},{
	16,16,	// Fase Size X,Y	(Fase 14)
},{
	16,16,	// Fase Size X,Y	(Fase 15)
}};



int[][][] FasePortaTab = new int[][][]
{
// -----------------------
// Fase 0
// =======================
{{
	0,		// ID (0: Puerta)
	90,11,	// Puerta X,Y	(Puerta 0 - H - 8)
	8,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x20,	// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	87,18,	// Puerta X,Y	(Puerta 1 - G - 14)
	14,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x04,	// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	98,24,	// Puerta X,Y	(Puerta 2 - 6 - 5)
	5,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x01,	// n� de Llave que necesitas
}},
// -----------------------
// Fase 1
// =======================
{{
	0,		// ID (0: Puerta)
	60,2,	// Puerta X,Y	(Puerta 0)
	4,		// Fase a la que vas
	1,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
}},
// -----------------------
// Fase 2
// =======================
{{
	0,		// ID (0: Puerta)
	4,3,	// Puerta X,Y	(Puerta 0 - 2)
	4,		// Fase a la que vas
	0,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	29,1,	// Puerta X,Y	(Puerta 1 - 3)
	4,		// Fase a la que vas
	7,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
}},
// -----------------------
// Fase 3
// =======================
{{
	0,		// ID (0: Puerta)
	28,3,	// Puerta X,Y	(Puerta 0 - 1)
	5,		// Fase a la que vas
	7,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
}},
// -----------------------
// Fase 4
// =======================
{{
	0,		// ID (0: Puerta)
	1,11,	// Puerta X,Y	(Puerta 0 - 2)
	2,		// Fase a la que vas
	0,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	1,1,	// Puerta X,Y	(Puerta 1 - 7)
	1,		// Fase a la que vas
	0,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	44,1,	// Puerta X,Y	(Puerta 2 - 5)
	5,		// Fase a la que vas
	1,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	10,6,	// Puerta X,Y	(Puerta 3 - A)
	9,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x01,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	13,6,	// Puerta X,Y	(Puerta 4 - B)
	10,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x01,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	26,6,	// Puerta X,Y	(Puerta 5 - C)
	7,		// Fase a la que vas
	1,		// Puerta en la que apareces
	0x10,	// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	44,7,	// Puerta X,Y	(Puerta 6 - 4)
	5,		// Fase a la que vas
	3,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	21,11,	// Puerta X,Y	(Puerta 7 - 3)
	2,		// Fase a la que vas
	1,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
}},
// -----------------------
// Fase 5
// =======================
{{
	0,		// ID (0: Puerta)
	13,1,	// Puerta X,Y	(Puerta 0 - 6)
	0,		// Fase a la que vas
	2,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	1,6,	// Puerta X,Y	(Puerta 1 - 5)
	4,		// Fase a la que vas
	2,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	13,6,	// Puerta X,Y	(Puerta 2 - F)
	13,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x02,	// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	1,11,	// Puerta X,Y	(Puerta 3 - 4)
	4,		// Fase a la que vas
	6,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	3,11,	// Puerta X,Y	(Puerta 4 - D)
	11,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x08,	// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	5,11,	// Puerta X,Y	(Puerta 5 - E)
	12,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x08,	// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	8,11,	// Puerta X,Y	(Puerta 6 - I)
	15,		// Fase a la que vas
	0,		// Puerta en la que apareces
	0x08,	// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	13,11,	// Puerta X,Y	(Puerta 7 - 1)
	3,		// Fase a la que vas
	0,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
}},
// -----------------------
// Fase 6
// =======================
{{
}},
// -----------------------
// Fase 7
// =======================
{{
	0,		// ID (0: Puerta)
	13,2,	// Puerta X,Y	(Puerta 0 - J)
	8,		// Fase a la que vas
	1,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	12,24,	// Puerta X,Y	(Puerta 1 - C)
	4,		// Fase a la que vas
	5,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
}},
// -----------------------
// Fase 8
// =======================
{{
	0,		// ID (0: Puerta)
	2,4,	// Puerta X,Y	(Puerta 0 - H - 0)
	0,		// Fase a la que vas
	0,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	0,		// ID (0: Puerta)
	12,10,	// Puerta X,Y	(Puerta 1 - J)
	7,		// Fase a la que vas
	0,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
}},
// -----------------------
// Fase 9
// =======================
{{
	0,		// ID (0: Puerta)
	1,7,	// Puerta X,Y	(Puerta 0 - A)
	4,		// Fase a la que vas
	3,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	2,		// ID (2: Llave)
	14,9,	// Tile X,Y
	0x02,	// Tipo de Llave
}},
// -----------------------
// Fase 10
// =======================
{{
	0,		// ID (0: Puerta)
	1,6,	// Puerta X,Y	(Puerta 0 - B)
	4,		// Fase a la que vas
	4,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	1,		// ID (1: Botiquin)
	12,7,	// Tile X,Y
	0,		// ON / OFF (0=Hay Objeto)
}},
// -----------------------
// Fase 11
// =======================
{{
	0,		// ID (0: Puerta)
	2,7,	// Puerta X,Y	(Puerta 0 - D)
	5,		// Fase a la que vas
	4,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	1,		// ID (1: Botiquin)
	12,8,	// Tile X,Y
	0,		// ON / OFF (0=Hay Objeto)
}},
// -----------------------
// Fase 12
// =======================
{{
	0,		// ID (0: Puerta)
	1,7,	// Puerta X,Y	(Puerta 0 - E)
	5,		// Fase a la que vas
	5,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	1,		// ID (1: Botiquin)
	10,8,	// Tile X,Y
	0,		// ON / OFF (0=Hay Objeto)
}},
// -----------------------
// Fase 13
// =======================
{{
	0,		// ID (0: Puerta)
	3,6,	// Puerta X,Y	(Puerta 0 - F)
	5,		// Fase a la que vas
	2,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	2,		// ID (2: Llave)
	17,8,	// Tile X,Y
	0x04,	// Tipo de Llave
},{
	3,		// ID (3: Cuchillo)
	18,8,	// Tile X,Y
	0,		// ON / OFF (0=Hay Objeto)
}},
// -----------------------
// Fase 14
// =======================
{{
	0,		// ID (0: Puerta)
	2,5,	// Puerta X,Y	(Puerta 0 - G - 0)
	0,		// Fase a la que vas
	1,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	2,		// ID (2: Llave)
	14,9,	// Tile X,Y
	0x08,	// Tipo de Llave
}},
// -----------------------
// Fase 15
// =======================
{{
	0,		// ID (0: Puerta)
	1,7,	// Puerta X,Y	(Puerta 0 - I)
	5,		// Fase a la que vas
	6,		// Puerta en la que apareces
	-1,		// n� de Llave que necesitas
},{
	2,		// ID (2: Llave)
	9,8,	// Tile X,Y
	0x10,	// Tipo de Llave
}},
// -----------------------
};


// -------------------
// Fase INI
// ===================

public void FaseINI()
{
	for (int f=0 ; f<FasePortaTab.length ; f++)
	{
		for (int i=0 ; i<FasePortaTab[f].length ; i++)
		{
			if (FasePortaTab[f][i].length >0 && FasePortaTab[f][i][0] < 0) {FasePortaTab[f][i][0] *= -1;}
		}
	}
}


// -------------------
// Fase END
// ===================

public void FaseEND()
{
	FaseMap = null;
	gc.sc.FaseMap = null;

	ProtEND();

	AmigoEND();

	RatoneraEND();
	RataEND();
	BichaEND();
	EnemigoEND();

	AniSprEND();
	AniTilEND();

	System.gc();
}


// -------------------
// Fase Item Update
// ===================

public void FaseItemUpdate()
{
	for (int i=0 ; i<FasePortaTab[JugarLevel].length ; i++)
	{
		switch (FasePortaTab[JugarLevel][i][0] * -1)
		{
		case 3:	// Cuchillo
			FaseMap[(((FasePortaTab[JugarLevel][i][2]+1) * FaseSizeX) + FasePortaTab[JugarLevel][i][1])] = 0x25;
			JugarCuchillo = true;

		case 2:	// Llave
		case 1:	// Botiquin
			FaseMap[((FasePortaTab[JugarLevel][i][2] * FaseSizeX) + FasePortaTab[JugarLevel][i][1])] = 0x25;

			gc.SoundSET(4,1);
		break;
		}
	}
}



// -------------------
// Fase SET
// ===================

public void FaseSET()
{
	FaseExit=0;

	FaseSizeX=FaseInfoTab[JugarLevel][0];
	FaseSizeY=FaseInfoTab[JugarLevel][1];
	FaseMap = new byte[FaseSizeX*FaseSizeY];


	if (JugarLevel==0)
	{
	AnimTile.AnimSizeX=15;
	AnimTile.AnimSizeY=6;
	LoadFile(AnimTile.AnimMap, "/Data/Anim0.map");
	} else {
	AnimTile.AnimSizeX=15;
	AnimTile.AnimSizeY=6;
	LoadFile(AnimTile.AnimMap, "/Data/Anim1.map");
	}


	LoadFile(FaseMap, "/Data/Fase"+JugarLevel+".map");


	FaseItemUpdate();


	TilDatTab = TilDatTab1;
	if (JugarLevel==0) {TilDatTab = TilDatTab0;}


	AniSprINI();
	AniTilINI();

	AmigoINI();

	ProtINI();


	ProtSET();

	RatoneraINI();
	RataINI();
	BichaINI();
	EnemigoINI();


// Analizamos Fase

	if (JugarLevel!=0)
	{
		for (int i=0 ; i<FaseMap.length ; i++)
		{
			switch (FaseMap[i])
			{
			case (byte)0x08:
			case (byte)0x61:
				AniTilSET(-1, i-1, 0,2, 4,4, 2,4, 0x001);	// Ventilador
			break;

			case (byte)0xA4:
			case (byte)0xA5:
				if (JugarLevel!=3)
				{
				BichaSET( (i%FaseSizeX)*TileSize , ((i/FaseSizeX)-2)*TileSize );
				}
			break;
			}
		}
	}



// Agragamos salida Ratas

	for (int i=0 ; i<FasePortaTab[JugarLevel].length ; i++)
	{
		if ( FasePortaTab[JugarLevel][i][0]==0 && FasePortaTab[JugarLevel][i][5]<0 )
		{
		RatoneraSET(FasePortaTab[JugarLevel][i][1]*TileSize, (FasePortaTab[JugarLevel][i][2]+1)*TileSize);
		}
	}




	FaseSET_Level();
}


// -------------------
// Fase SET Level
// ===================

public void FaseSET_Level()
{
	AmigoFlag = true;

	CapitanAnim = -1;

	switch (JugarLevel)
	{
	case 0:

		if (AmigoCapturado)
		{
			if (!AmigoLibre)
			{
			AmigoSET(1);
			} else {
			AmigoSET(0);
			}
		}

//	AmigoLibre = true;
//	AmigoSET(0);


		JaulaAnim = AniSprSET(-1, 24*TileSize,  3*TileSize, 0,1,1, 0x302);	// Jaula


		AniSprSET(-1, 96*TileSize, 14*TileSize, 0,1,1, 0x202);	// Bote


		if (AmigoCapturado && !AmigoLibre)
		{
		CapitanAnim = AniSprSET(-1, 19*TileSize, 10*TileSize, 13,2,8, 0x121);	// Capitan
		AniSprs[CapitanAnim].FrameBase = 24;
		}
	break;

	case 3:
		if (JugarConta>0 || AmigoDead || !JugarBote)
		{
		AmigoSET(2);
		}
	break;	

	case 4:
		if (JugarBotiquin && !JugarEnemyBotiquin)
		{
		JugarEnemyBotiquin = true;
		EnemigoSET(2*TileSize, 7*TileSize);
		}
	break;
	}


}


// -------------------
// Fase RUN
// ===================

public void FaseRUN()
{


// Bajamos la barca
// ----------------
	if (JugarBote)
	{
		if (!BoteWait && BoteY < 15*TileSize)
		{
		if (--BoteCnt < 0) {BoteCnt=4; BoteY++;}
		} else {
		BoteWait = true;
		}
	}

	if (JugarBote2)
	{
		if (!BoteAgua && BoteY < ((16*TileSize)+(TileSize/2)) )
		{
		if (--BoteCnt < 0) {BoteCnt=4; BoteY++;}
		} else {
		BoteAgua = true;
		}
	}
// -----------------



// Movemos la Jaula
// ----------------
	if (JaulaOk && JaulaY == 21*TileSize)
	{
	JaulaSide=0;
	JaulaSuelo = true;
	} else {
		if (JaulaSide!=0)
		{
		if (--JaulaCnt < 0) {JaulaCnt=6; JaulaY += JaulaSide;}

		if (JaulaY < 0) {JaulaY=0; JaulaSide=0;}
		else if (JaulaY > 32*TileSize) {JaulaY=32*TileSize; JaulaSide=0;}
		}
	}
// -----------------


	ProtRUN();

	AmigoRUN();

	RatoneraRUN();
	RataRUN();
	BichaRUN();
	EnemigoRUN();

	AniSprRUN();
	AniTilRUN();

	FaseRUN_Level();
}


// -------------------
// Fase RUN Level
// ===================

public void FaseRUN_Level()
{

	switch (JugarLevel)
	{
	case 0:

		if (!MsgJaula && AmigoCapturado && Colision(ProtX+ProtCentX, ProtY+ProtCuY, 2, ProtCuSizeY, 35*TileSize, 26*TileSize, TileSize, TileSize) )
		{
		MsgJaula = true;
		ProtDemoSET(1);
		JaulaY = 8*TileSize;
		JaulaSide = 0;
		JaulaOk = false;
		break;
		}


		if (!MsgGrua && Colision(ProtX+ProtCuX+(ProtCuSizeX/2), ProtY+ProtCuY, 2, ProtCuSizeY, 18*TileSize, 11*TileSize, 4, 4) )
		{
		MsgGrua = true;
		GloboSET(Texto.MsgGrua);
		}


		if ( !MsgBote && BoteWait && Colision(ProtX+ProtCentX, ProtY+ProtCuY, 4, ProtCuSizeY, 101*TileSize, 26*TileSize, 3*TileSize, 4) )
		{
		MsgBote = true;
		GloboSET(Texto.BoteWait);
		break;
		}



		if ( AmigoFlag && JaulaSuelo && !AmigoLibre && Colision(ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY, 25*TileSize, 25*TileSize,  2*TileSize, 3*TileSize ))
		{
		AmigoFlag = false;
			if (AmigoDeadJaula)
			{
			GloboSET(Texto.AmigoMuere);
			} else {
			GloboSET(Texto.JaulaCerrada);
			}
		break;
		}



	break;


	case 3:
		if (AmigoFlag && Colision(ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY, 1*TileSize, 8*TileSize, 2*TileSize, 3*TileSize) )
		{
		AmigoFlag = false;


	if (AmigoDead)
	{
		GloboSET(Texto.AmigoMuere);
		break;
	}


	if (AmigoCapturado && !JugarNota)
	{
		JugarNota = true;
		GloboSET(Texto.Charla7);	// Nota de captura
		break;
	}


	if (!JugarBotiquin)
	{
		GloboSET(Texto.Charla1);	// Sin Botiquin
	}
	else if (!JugarCurado)
	{
		JugarCurado = true;
		JugarConta = 0;
		GloboSET(Texto.Charla2);	// Con Botiquin / Sin GPS
	}
	else if (!JugarRadio)
	{
		GloboSET(Texto.Charla3);	// Sin Encontrar Radio
	}
	else if (!JugarGPS)
	{
		GloboSET(Texto.Charla4);	// Sin GPS
	}
	else if (!JugarRadioOff)
	{
		GloboSET(Texto.Charla5);	// Con GPS / Sin hablar Radio
	}
	else if (!JugarBote)
	{
		GloboSET(Texto.Charla6);	// La barca NO esta bajando
	}


		}
		else if (!AmigoFlag && !Colision(ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY, 1*TileSize, 8*TileSize, 2*TileSize, 3*TileSize) )
		{
		AmigoFlag = true;
		}


		if (!MsgPorta && Colision(ProtX+ProtCuX+(ProtCuSizeX/2), ProtY+ProtCuY, 2, ProtCuSizeY, 29*TileSize, 5*TileSize, 4, 4) )
		{
		MsgPorta = true;
		GloboSET(Texto.MsgPorta);
		}

		if (!MsgCorrer && Colision(ProtX+ProtCuX+(ProtCuSizeX/2), ProtY+ProtCuY, 2, ProtCuSizeY, 11*TileSize, 13*TileSize, 4, 4) )
		{
		MsgCorrer = true;
		if (ProtMode!=4) {GloboSET(Texto.MsgCorrer);}
		}
	break;


	case 14:
		if (!MsgTelefono && Colision(ProtX+ProtCuX+(ProtCuSizeX/2), ProtY+ProtCuY, 2, ProtCuSizeY, 13*TileSize, 9*TileSize, 4, 4) )
		{
		MsgTelefono = true;
		GloboSET(Texto.MsgTelefono);
		}
	break;

	case 15:
		if (!MsgGPS && Colision(ProtX+ProtCuX+(ProtCuSizeX/2), ProtY+ProtCuY, 2, ProtCuSizeY, 8*TileSize, 9*TileSize, 4, 4) )
		{
		MsgGPS = true;
		GloboSET(Texto.MsgGPS);
		}
	break;

	case 8:
		if (!MsgPalanca && Colision(ProtX+ProtCuX+(ProtCuSizeX/2), ProtY+ProtCuY, 2, ProtCuSizeY, 6*TileSize, 6*TileSize, 4, 4) )
		{
		MsgPalanca = true;
		GloboSET(Texto.MsgPalanca);
		}
	break;
	}


}




// --------------------
// Fase Check Tiles
// ====================

int FaseChkDir;

public int CheckTile(int X, int Y, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y >= FaseSizeY*TileSize) {Y=(FaseSizeY-1)*TileSize;}
	if (X < 0) {X=0;} else if (X >= FaseSizeX*TileSize) {X=(FaseSizeX-1)*TileSize;}

	FaseChkDir = ((Y/TileSize)*FaseSizeX)+(X/TileSize);
	return ((int)TilDatTab[FaseMap[FaseChkDir]+128]) & Mask;
}


public int CheckTile(int X, int Y, int SizeX, int SizeY, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y+SizeY >= FaseSizeY*TileSize) {Y=(FaseSizeY-1)*TileSize; SizeY=1;}
	if (X < 0) {X=0;} else if (X+SizeX >= FaseSizeX*TileSize) {X=(FaseSizeX-1)*TileSize; SizeX=1;}

int TabXY = ((Y/TileSize)*FaseSizeX)+(X/TileSize);
int TabSizeX = ((X+SizeX)/TileSize)-(X/TileSize);
int TabSizeY = ((Y+SizeY)/TileSize)-(Y/TileSize);

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


short[] TilDatTab;

final short TilDatTab0[] = new short[] {
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x004,0x004,0x005,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 8
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 9
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // A
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // B
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // C
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // D
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // E
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // F
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x000,0x001,0x000,0x000,0x000,0x004,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 0
0x000,0x001,0x001,0x000,0x000,0x000,0x004,0x001,0x001,0x001,0x005,0x000,0x000,0x000,0x000,0x000, // 1
0x000,0x000,0x000,0x000,0x000,0x002,0x002,0x002,0x005,0x001,0x001,0x001,0x008,0x008,0x001,0x000, // 2
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x004,0x000,0x000,0x000,0x008,0x008,0x000,0x005, // 3
0x000,0x000,0x000,0x000,0x004,0x000,0x000,0x000,0x004,0x000,0x000,0x000,0x008,0x008,0x004,0x000, // 4
0x000,0x000,0x000,0x001,0x001,0x002,0x002,0x002,0x001,0x000,0x000,0x000,0x000,0x000,0x004,0x000, // 5
0x000,0x000,0x4000,0x4100,0x4200,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 6
0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x002,0x000,0x000,0x000,0x000,0x000,0x001,0x000, // 7
};


final short TilDatTab1[] = new short[] {
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x2000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x080,0x040,0x040,0x000,0x000,0x000, // 8
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 9
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // A
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // B
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // C
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // D
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // E
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // F
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x000,0x001,0x000,0x005,0x001,0x001,0x000,0x000,0x000,0x000,0x008,0x008,0x008,0x008,0x000, // 0
0x001,0x001,0x000,0x001,0x004,0x000,0x000,0x000,0x000,0x000,0x000,0x008,0x008,0x008,0x008,0x000, // 1
0x000,0x000,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x108,0x108,0x008,0x008,0x001, // 2
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x108,0x108,0x008,0x008,0x001, // 3
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001, // 4
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 5
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x000,0x000,0x000, // 6
0x2000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x080,0x040,0x040,0x000,0x000,0x000, // 7
};

// <=- <=- <=- <=- <=-








// *******************
// -------------------
// Prot - Engine
// ===================
// *******************

static final int ProtCuX =  10;
static final int ProtCuY =  14;
static final int ProtCuSizeX = 12;
static final int ProtCuSizeY = 22;

static final int ProtCentX =  16;
static final int ProtCentY =  16;

int ProtSpd;	// Se carga desde la Canvas

int ProtX;
int ProtY;
int ProtMode;
int ProtType;
int ProtSide;

int ProtNewX;
int ProtNewY;

int ProtInmune;

int ProtAnim=-1;
 
int ProtCorrerCnt;

boolean AmigoFlag;

// -------------------
// Prot INI
// ===================

public void ProtINI()
{
	ProtAnim = AniSprSET(-1, 20,20, 1,6,1, 0x001);
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
	ProtSide = ((FaseSizeX*TileSize)/2) < ProtNewX? -1:1;

	if (ProtNewX==-1)
	{
	ProtX=3*TileSize;
	ProtY=8*TileSize;
	ProtSide = -1;
	ProtCaminarSET();
	ProtDemoSET(0);
	} else {
	ProtX = ProtNewX;
	ProtY = ProtNewY;
	ProtCaminarSET();
	}

}

// -------------------
// Prot RUN
// ===================

public void ProtRUN()
{
	switch (ProtMode)
	{
	case 0:	ProtCaminarRUN();	break;	// Caminar
	case 1:	ProtSaltarRUN();	break;	// Saltar
	case 2:	ProtCaerRUN();		break;	// Caer
	case 3:	ProtEscaleraRUN();	break;	// Escalera
	case 4:	ProtCorrerRUN();	break;	// Correr
	case 5:	ProtAccionRUN();	break;	// Accion
	case 6:	ProtPuertaRUN();	break;	// Abrimos Puerta
	case 7:	ProtDemoRUN();		break;	// Modos DEMO
	case 80:ProtMorirRUN();		break;	// Morir
	}



	if (ProtMode!=7)
	{
	if (ProtX+ProtCuX < 0) {ProtX=0-ProtCuX;} else if (ProtX+ProtCuX+ProtCuSizeX > FaseSizeX*TileSize) {ProtX = (FaseSizeX*TileSize)-(ProtCuX+ProtCuSizeX);}
	}



	if (ProtMode<80)
	{
		ProtFaseCheck();
	}



	AniSprs[ProtAnim].CoorX=ProtX;
	AniSprs[ProtAnim].CoorY=ProtY;
	AniSprs[ProtAnim].FrameBase = (ProtSide==1? 0:40) + ProtaFr;
}

// <<<<<<<<<<<<<<<<<<<

// -------------------
// ProtFaseCheck
// ===================

public void ProtFaseCheck()
{
// ---------------------------
// Chequeamos Objetos
// ===========================

	for (int i=0 ; i<FasePortaTab[JugarLevel].length ; i++)
	{
//		Botiquin
//		---------
		if ( FasePortaTab[JugarLevel][i][0]==1 && Colision(FasePortaTab[JugarLevel][i][1]*TileSize, FasePortaTab[JugarLevel][i][2]*TileSize, 1*TileSize, 1*TileSize,   ProtX+ProtCuX,ProtY+ProtCuY, ProtCuSizeX,ProtCuSizeY) )
		{
		FasePortaTab[JugarLevel][i][0] *= -1;
		FaseItemUpdate();

			if (!JugarBotiquin)
			{
			JugarBotiquin = true;
			GloboSET(Texto.Botiquin);
			} else {
			ProtVida += 30; if (ProtVida>100) {ProtVida=100;}
			}
		}

//		Llave
//		---------
		if ( FasePortaTab[JugarLevel][i][0]==2 && Colision(FasePortaTab[JugarLevel][i][1]*TileSize, FasePortaTab[JugarLevel][i][2]*TileSize, 1*TileSize, 1*TileSize,   ProtX+ProtCuX,ProtY+ProtCuY, ProtCuSizeX,ProtCuSizeY) )
		{
		FasePortaTab[JugarLevel][i][0] *= -1;
		FaseItemUpdate();

		JugarLlave |= FasePortaTab[JugarLevel][i][3];
		}

//		Cuchillo
//		---------
		if ( FasePortaTab[JugarLevel][i][0]==3 && Colision(FasePortaTab[JugarLevel][i][1]*TileSize, FasePortaTab[JugarLevel][i][2]*TileSize, 1*TileSize, 2*TileSize,   ProtX+ProtCuX,ProtY+ProtCuY, ProtCuSizeX,ProtCuSizeY) )
		{
		FasePortaTab[JugarLevel][i][0] *= -1;
		FaseItemUpdate();
		}
	}

// ---------------------------

}

// <<<<<<<<<<<<<<<<<<<

// -------------------
// ProtCaminarSET
// ===================

public void ProtCaminarSET()
{
	ProtMode = 0;
	AniSprSET(ProtAnim, 0,6,1, 0x021);

	ProtCorrerCnt = 10;
}

// -------------------
// ProtCaminarRUN
// ===================

public void ProtCaminarRUN()
{
	if (KeybY1==1 && KeybY1!=KeybY2 && ProtPuertaSET() ) {return;}


	if (JugarCuchillo && KeybM1>0 && KeybM1!=KeybM2) {ProtAccionSET(); return;}


	if (KeybB1==10 && KeybB1!=KeybB2) {ProtCorrerSET(); return;}


	if (KeybX1!=0 && KeybX2==0)
	{
	if (KeybX1!=ProtSide || ProtCorrerCnt > 10) {ProtCorrerCnt=0;} else {ProtCorrerSET(); return;}
	}
	if (ProtCorrerCnt < 20) {ProtCorrerCnt++;}



	if (KeybY1== 1)
	{
		if (( CheckTile(ProtX+ ProtCentX-4, ProtY+(ProtCuY+ProtCuSizeY), 1,0, 0x04) != 0 )
		&&	( CheckTile(ProtX+ ProtCentX+3, ProtY+(ProtCuY+ProtCuSizeY), 1,0, 0x04) != 0 ))
		{
//		if ( CheckTile(ProtX+ ProtCuX, ProtY+(ProtCuY+ProtCuSizeY), 0x04) == 0 ) {ProtX+=TileSize;}
//		ProtX &= TileSize*-1;
		ProtEscaleraSET(); return;
		}
	}
	else if (KeybY1==-1)
	{
		if (( CheckTile(ProtX+ ProtCentX-1, ProtY+(ProtCuY+ProtCuSizeY-2), 1,0, 0x04) != 0 )
		&&	( CheckTile(ProtX+ ProtCentX-4, ProtY,                         1,0, 0x04) != 0 )
		&&	( CheckTile(ProtX+ ProtCentX+3, ProtY,                         1,0, 0x04) != 0 ))
		{
//		if ( CheckTile(ProtX+ ProtCuX, ProtY+(ProtCuY+ProtCuSizeY-2), 0x04) == 0 ) {ProtX+=TileSize;}
//		ProtX &= TileSize*-1;
		ProtEscaleraSET(); return;
		}
	}


	if (KeybY1==-1 && KeybY2==0) {ProtSaltarSET(); return;}


	if (KeybX1!=0)
	{
	ProtSide = KeybX1;
		if ( CheckTile(ProtX+ProtCuX+(KeybX1*ProtSpd), ProtY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
		{
		ProtX+=KeybX1*ProtSpd;
		AniSprs[ProtAnim].FrameIni = 1;
		AniSprs[ProtAnim].Pause = 1;
		}
	} else {
		AniSprs[ProtAnim].FrameIni = 0;
		AniSprs[ProtAnim].FrameAct = 0;
	}


	if ( CheckTile(ProtX+ProtCuX, ProtY+(ProtCuY+ProtCuSizeY), ProtCuSizeX-1,0, 0x03) == 0 )
	{
	ProtCaerSET(); return;
	}

}

// <<<<<<<<<<<<<<<<<<<


// -------------------
// ProtCorrerSET
// ===================

public void ProtCorrerSET()
{
	ProtMode = 4;
	AniSprSET(ProtAnim, 10,3,1, 0x001);
}

// -------------------
// ProtCorrerRUN
// ===================

public void ProtCorrerRUN()
{
	if (KeybM1>0 && KeybM1!=KeybM2) {ProtAccionSET(); return;}


	if (KeybY1==-1 && KeybY2==0) {ProtSaltarSET(); return;}


	if (KeybX1!=0 && KeybX1!=ProtSide)
	{
		ProtCaminarSET(); return;
	} else {
		if ( CheckTile(ProtX+ProtCuX+(ProtSide*ProtSpd*2), ProtY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
		{
		ProtX+=ProtSide*ProtSpd*2;
		} else {
		ProtCaminarSET(); return;
		}
	}


	if ( CheckTile(ProtX+ProtCuX, ProtY+(ProtCuY+ProtCuSizeY), ProtCuSizeX-1,0, 0x03) == 0 )
	{
	ProtCaerSET(); return;
	}

}

// <<<<<<<<<<<<<<<<<<<

int ProtSaltarPos;
int[] ProtSaltarTab = new int[] {-8,-6,-4,-2,-2,0,0};

// -------------------
// ProtSaltarSET
// ===================

public void ProtSaltarSET()
{
	ProtMode = 1;
	AniSprSET(ProtAnim, 11, 1,0, 0x022);

	ProtSaltarPos = 0;
}

// -------------------
// ProtSaltarRUN
// ===================

public void ProtSaltarRUN()
{
	if ( CheckTile(ProtX+ProtCuX+(ProtSide*ProtSpd), ProtY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
	{
	ProtX+=ProtSide*ProtSpd;
	}

	if ( CheckTile(ProtX+ProtCuX, ProtY+ProtCuY+ProtSaltarTab[ProtSaltarPos], ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
	{
		ProtY += ProtSaltarTab[ProtSaltarPos++];
		if (ProtSaltarPos==ProtSaltarTab.length)
		{
		ProtCaerSET(true); return;
		}
	} else {
		ProtCaerSET(true); return;
	}
}

// <<<<<<<<<<<<<<<<<<<


// -------------------
// ProtCaerSET
// ===================

byte[] ProtCaerTab = new byte[] {1,2,3,4,5,6};
int ProtCaerPos;
int ProtCaerDist;
boolean ProtCaerDano;

int ProtCaerDesp;

public void ProtCaerSET()
{
	ProtCaerSET(false);
}

public void ProtCaerSET(boolean Desp)
{
	ProtMode = 2;
	AniSprSET(ProtAnim, 2, 1,0, 0x022);

	ProtCaerDesp = Desp? ProtSaltarTab.length : 0;

	ProtCaerPos = 0;
	ProtCaerDist = 0;
	ProtCaerDano = false;
}

// -------------------
// ProtCaerRUN
// ===================

public void ProtCaerRUN()
{
	if (ProtCaerDano)
	{
	if (AniSprs[ProtAnim].Pause==0) {ProtCaminarSET();}
	return;
	}

	int Desp = ProtCaerTab[ProtCaerPos]; if (++ProtCaerPos == ProtCaerTab.length) {ProtCaerPos--;}

	if ( CheckTile(ProtX+ProtCuX, ProtY+(ProtCuY+ProtCuSizeY+Desp), ProtCuSizeX-1,0, 0x03) != 0 )
//	if ( CheckTile(ProtX+ProtCuX, ProtY+(ProtCuY+ProtSpd), ProtCuSizeX-1,ProtCuSizeY-1, 0x03) != 0 )
	{
//	ProtY = (ProtY+Desp) & -8; 
	ProtY = (((ProtY+(ProtCuY+ProtCuSizeY)+Desp) / TileSize) * TileSize) -(ProtCuY+ProtCuSizeY);

	if (ProtCaerDist > 10*TileSize) {ProtMorirSET(200); return;}
	else
		if (ProtCaerDist > 6*TileSize)
		{
		ProtCaerDano = true;
		AniSprSET(ProtAnim, 18, 1,4, 0x002);
		ProtMorirSET(5);
		return;
		}
	ProtCaminarSET();
	return;
	} else {
	ProtY+=Desp;
	ProtCaerDist+=Desp;
	}

	if (ProtCaerDesp>0)
	{
		ProtCaerDesp--;

		if ( CheckTile(ProtX+ProtCuX+(ProtSide*ProtSpd), ProtY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
		{
		ProtX+=(ProtSide*ProtSpd);
		}
	}
}

// <<<<<<<<<<<<<<<<<<<


// -------------------
// ProtEscaleraSET
// ===================

public void ProtEscaleraSET()
{
	ProtMode = 3;
	AniSprSET(ProtAnim, 7, 3,2, 0x021);
}

// -------------------
// ProtEscaleraRUN
// ===================
 
public void ProtEscaleraRUN()
{
	if (KeybX1!=0)
	{
	ProtSide=KeybX1;
		if ( CheckTile(ProtX+ProtCuX+KeybX1, ProtY+ProtCuY+(KeybY1*ProtSpd), ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
		{
		ProtX += (KeybX1*ProtSpd);
		AniSprs[ProtAnim].Pause = 1;

			if (( CheckTile(ProtX+ ProtCentX-4, ProtY+(ProtCuY+ProtCuSizeY), 0x04) == 0 )
			||	( CheckTile(ProtX+ ProtCentX+3, ProtY+(ProtCuY+ProtCuSizeY), 0x04) == 0 ))
			{
			ProtCaminarSET();
			}
		}
	}

	if (KeybY1==1)
	{
	ProtY+=ProtSpd;
	AniSprs[ProtAnim].Pause = 1;
		if ( CheckTile(ProtX+ProtCuX, ProtY+(ProtCuY+ProtCuSizeY), ProtCuSizeX-1,0, 0x04) == 0 )
		{
		ProtCaerSET(); return;
		}
	}
	else if (KeybY1==-1)
	{
		if (( CheckTile(ProtX+ProtCuX, ProtY+(ProtCuY+ProtCuSizeY-ProtSpd), ProtCuSizeX-1,0, 0x04) == 0 )
		||	( CheckTile(ProtX+ProtCuX, ProtY+(ProtCuY+ProtCuSizeY)        , ProtCuSizeX-1,0, 0x05) == 5 )
		&&	( CheckTile(ProtX+ProtCuX, ProtY                              , ProtCuSizeX-1,0, 0x04) == 0 ))
		{
//		ProtCaminarSET(); return;
//		ProtY = (ProtY-ProtSpd) & -8;
		ProtY = (((ProtY+(ProtCuY+ProtCuSizeY)-ProtSpd) / TileSize) * TileSize) -(ProtCuY+ProtCuSizeY);
		ProtCaminarSET(); return;
		}
	ProtY-=ProtSpd;
	AniSprs[ProtAnim].Pause = 1;
	}
}

// <<<<<<<<<<<<<<<<<<<

// -------------------
// ProtAccionSET
// ===================

public void ProtAccionSET()
{
	ProtMode = 5;
	AniSprSET(ProtAnim, 16,2,2, 0x002);
}

// -------------------
// ProtAccionRUN
// ===================

public void ProtAccionRUN()
{
	if (AniSprs[ProtAnim].Pause==0)
	{
		if (JugarLevel==0 && !AmigoDeadJaula && JaulaSuelo && !AmigoLibre)
		{
			if ( Colision(ProtX, ProtY, TileSize, TileSize, 22*TileSize, 25*TileSize,  2*TileSize, TileSize ))
			{
				AmigoLibre = true;
				GloboSET(Texto.JaulaAbierta);
				AmigoSET(0);
			}
		}

	ProtCaminarSET();
	}
}

// <<<<<<<<<<<<<<<<<<<

int PuertaAnim;

// -------------------
// ProtPuertaSET
// ===================

public boolean ProtPuertaSET()
{
	boolean PortaBote = false;

	int Dat = CheckTile(ProtX+ProtCentX, ProtY+ProtCentY, -1);

	if ( (Dat & 0x08) != 0 )	// Sobre Puerta???
	{
		for (int i=0 ; i<FasePortaTab[JugarLevel].length ; i++)
		{
			if ( FasePortaTab[JugarLevel][i][0]==0 && Colision(FasePortaTab[JugarLevel][i][1]*TileSize, FasePortaTab[JugarLevel][i][2]*TileSize, 2*TileSize, 4*TileSize,   ProtX+ProtCentX,ProtY+ProtCentY, 1,1) )
			{

			if (FasePortaTab[JugarLevel][i][5] == 0x20) {PortaBote = true;}

			if ( (FasePortaTab[JugarLevel][i][5] & JugarLlave ) == 0 ) {continue;}

			if (FasePortaTab[JugarLevel][i][5]!=-1)
			{
			int Dest = (FasePortaTab[JugarLevel][i][2] * FaseSizeX) + FasePortaTab[JugarLevel][i][1];
			PuertaAnim = AniTilSET(-1, Dest,  9,2, 2,4, 3,2, 0x002);
			} else {
			PuertaAnim = -1;
			}

			int Puerta=FasePortaTab[JugarLevel][i][4];
			JugarLevelNext=FasePortaTab[JugarLevel][i][3];
			ProtNewX=((FasePortaTab[JugarLevelNext][Puerta][1]+1)*TileSize)-ProtCentX;
			ProtNewY=(FasePortaTab[JugarLevelNext][Puerta][2]+1)*TileSize;

			ProtMode = 6;

			return true;
			}
		}

	if (!JugarRadioOff || !PortaBote) {GloboSET(Texto.PortaTancada);} else {GloboSET(Texto.PortaCabina);}

	}

	else if ( (Dat & 0x40) != 0 )	// Sobre Radio???
	{
		JugarRadio = true;
		if (!JugarGPS)
		{
		GloboSET(Texto.Radio);
		} else {
			if (!JugarRadioOff)
			{
			GloboSET(Texto.Radio2);
			JugarRadioOff = true;
			} else {
			GloboSET(Texto.Radio3);
			}
		}
	}

	else if ( (Dat & 0x80) != 0 )	// Sobre GPS???
	{
	GloboSET(Texto.GPS);
	JugarGPS = true;
	}

	else if ( (Dat & 0x2000) != 0 )	// Sobre Palancas Bote???
	{
	JugarBote = true;

	AniTilSET(-1, (5*FaseSizeX)+5, 8,0, 1,2, 3,1, 0x000);
	AniTilSET(-1, (5*FaseSizeX)+6, 8,0, 1,2, 3,1, 0x000);

	gc.SoundSET(1,1);

	if (!AmigoDead) {JugarCurado = true; JugarConta = 0; AmigoCapturado = true;}
	}

	else if ( AmigoCapturado && (Dat & 0x4000) != 0 )	// Sobre Palancas Jaula???
	{
		switch ( (Dat>>8) & 0x3)
		{
		case 0:
			GloboSET(Texto.JaulaSube);
			JaulaSide = -1;
		break;

		case 1:
			JaulaSide = 0;

			if (!JaulaOk)
			{
				if (JaulaY < 21*TileSize)
				{
				GloboSET(Texto.JaulaOk);
				JaulaOk = true;
				} else {
				GloboSET(Texto.JaulaMal);
				}
			}
		break;

		case 2:
			GloboSET(Texto.JaulaBaja);
			JaulaSide = 1;
		break;
		}
	}



	if ( BoteWait && Colision(ProtX+ProtCentX, ProtY+ProtCuY, 4, ProtCuSizeY, 101*TileSize, 26*TileSize, 3*TileSize, 4) )
	{
	ProtDemoSET(2);
	}


	return false;
}


// -------------------
// ProtPuertaRUN
// ===================

public void ProtPuertaRUN()
{
	if (PuertaAnim==-1 || AniTils[PuertaAnim].Pause==0)
	{
	FaseExit=1;
	}
}

// <<<<<<<<<<<<<<<<<<<

int ProtMorirTime;

// -------------------
// ProtMorirSET
// ===================

public void ProtMorirSET(int Rest)
{
	if (ProtMode!=80)
	{
	if ( (ProtVida-=Rest) > 0 ) {gc.VibraSET(200); return;}
	ProtVida=0;
	ProtMode = 80;
	AniSprSET(ProtAnim, 13,2,4, 0x002);
	ProtMorirTime=0;

	gc.VibraSET(500);
	gc.SoundSET(2,1);
	}
}

// -------------------
// ProtMorirRUN
// ===================

public void ProtMorirRUN()
{
	if (++ProtMorirTime > 60)
	{
	FaseExit = 3;
	}
}

// <<<<<<<<<<<<<<<<<<<

int ProtDemoMode;
int ProtDemoTime;

int DemoX;
int DemoY;
int DemoDestX;
int DemoDestY;

int BoteAjuste;

int BoteNavPos;
byte[] BoteNavTab = new byte[] {-1,0,0,0, 1,0,0,0, 1,0,0,0, -1,0,0,0};

// -------------------
// ProtDemoSET
// ===================

public void ProtDemoSET(int Type)
{
	ProtMode = 7;

	ProtDemoMode = Type*10;

	ProtDemoTime = 0;
}

// -------------------
// ProtDemoRUN
// ===================

public void ProtDemoRUN()
{

	switch (ProtDemoMode)
	{
	case 0:
		AniSprSET(AmigoAnim, AmigoFr+0,1, 1, 0x021);
		if (++ProtDemoTime>20)
		{
		GloboSET(Texto.Inicio1);
		ProtDemoTime = 0;
		ProtDemoMode++;
		}
	break;

	case 1:
		if (++ProtDemoTime>20)
		{
		ProtDemoTime = 0;
		ProtDemoMode++;

		AniSprSET(-1, 2*TileSize,8*TileSize, 21+24, 3, 2, 0x100);
		}
	break;

	case 2:
		AniSprSET(AmigoAnim, AmigoFr+15,1, 1, 0x021);
		ProtDemoTime = 0;
		ProtDemoMode++;
	break;

	case 3:
		if (++ProtDemoTime>10)
		{
		GloboSET(Texto.Inicio2);
		ProtCaminarSET();
		JugarConta = 99;
		}
	break;

// -----

	case 10:
		CamaraProt = false;

		DemoX = ProtX;
		DemoY = ProtY;
		DemoDestX = 22*TileSize;
		DemoDestY = 11*TileSize;

		ProtCaminarSET(); ProtMode = 7;

		ProtDemoMode++;
	break;

	case 11:
		boolean ok = true;
		if (DemoX != DemoDestX) {DemoX += DemoX<DemoDestX? 1:-1; ok=false;}
		if (DemoY != DemoDestY) {DemoY += DemoY<DemoDestY? 1:-1; ok=false;}

		if (ok)
		{
		GloboSET(Texto.Jaula1);
		JaulaSide = 1;
		ProtDemoTime = 0;
		ProtDemoMode++;
		}
	break;

	case 12:
		AniSprs[CapitanAnim].Pause = 2;
		ProtDemoMode++;
	break;

	case 13:
		if (++ProtDemoTime>25)
		{
		GloboSET(Texto.Jaula2);

		DemoDestX = ProtX;
		DemoDestY = ProtY;

//		AniSprs[CapitanAnim].FrameAct = 0;

		ProtDemoMode++;
		}
	break;

	case 14:
		ok = true;
		if (DemoX != DemoDestX) {DemoX += DemoX<DemoDestX? 1:-1; ok=false;}
		if (DemoY != DemoDestY) {DemoY += DemoY<DemoDestY? 1:-1; ok=false;}

		if (ok)
		{
		CamaraProt = true;
		CapitanAnim = AniSprRES(CapitanAnim);
		ProtCaminarSET();

		EnemigoSET(gc.sc.ScrollX-ProtCentX, 25*TileSize);
		EnemigoSET(gc.sc.ScrollX+gc.sc.ScrollSizeX+ProtCentX, 25*TileSize);
		}
	break;

// -----

	case 20:
		if (AmigoLibre) {AmigoAlBote = true;}

		int DestX = 103*TileSize;
		int DespX = TileSize;

		if (DestX-DespX > ProtX || DestX+DespX < ProtX)
		{
		int Desp = DestX < ProtX ? -1:1;

			if ( CheckTile(ProtX+ProtCuX+(Desp*ProtSpd), ProtY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			ProtX+=Desp*ProtSpd;
			ProtSide = Desp;
			AniSprs[ProtAnim].FrameIni = 1;
			AniSprs[ProtAnim].Pause = 1;
			}
		} else {
			ProtSide=-1;

			AniSprSET(ProtAnim, 2, 1,2, 0x022);
			ProtCaerPos = 0;
			ProtDemoTime = 0;
			ProtDemoMode++;
		}
	break;

	case 21:
		int Desp = ProtCaerTab[ProtCaerPos]; if (++ProtCaerPos == ProtCaerTab.length) {ProtCaerPos--;}

		if ( (ProtDemoTime+=Desp) >= 3*TileSize )
		{
		ProtDemoMode++;
		BoteAjuste = ProtY - BoteY;
		}

		ProtY+=Desp;
	break;

	case 22:
		if (!AmigoLibre || AmigoEnBote) {ProtDemoMode++; JugarBote2 = true;}
	break;

	case 23:
		ProtY = BoteY + BoteAjuste;

		if (BoteAgua)
		{
		BoteNavPos = 0;
		ProtDemoTime = 0;
		ProtDemoMode++;
		}
	break;

	case 24:
		BoteY += BoteNavTab[BoteNavPos]; if (++BoteNavPos == BoteNavTab.length) {BoteNavPos=0;}

		ProtY = BoteY + BoteAjuste;

		if (ProtDemoTime > 15)
		{
		BoteX--;
		ProtX--;
		} else {
		ProtDemoTime++;
		}

		if (ProtX < -10*TileSize)
		{
		ProtDemoMode++;
		}
	break;

	case 25:
		FaseExit = 4;
		ProtDemoMode++;
	break;
	}

}

// <<<<<<<<<<<<<<<<<<<

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
// Anim Tile - Engine
// ====================
// ********************

final int AniTilMAX=12;
int AniTilC;
int[] AniTilP;
AnimTile[] AniTils;

// ---------------
// Anim Tile SET
// ===============

public int AniTilSET(int i, int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode)
{
	if (i<0)
	{
	if (AniTilC == AniTilMAX) {return(-1);}
	i=AniTilP[AniTilC++];
	AniTils[i] = new AnimTile();
	}

	AniTils[i].AniTilSET(Dest, CoorX, CoorY, SizeX, SizeY, Frames, Speed, Mode);

	return (i);
}

// ---------------
// Anim Tile INI
// ===============

public void AniTilINI()
{
	AniTilC = 0;
	AniTilP = new int[AniTilMAX];
	AniTils = new AnimTile[AniTilMAX];
	for (int i=0 ; i<AniTilMAX ; i++) {AniTilP[i]=i;}
}

// ---------------
// Anim Tile RES
// ===============

public int AniTilRES(int i)
{
	if (i!=-1) {AniTils[i].Frames=0; AniTils[i].Pause=0;}
	return -1;
}

// ---------------
// Anim Tile END
// ===============

public void AniTilEND()
{
	AniTils = null; AniTilP = null; AniTilC = 0;
}

// ---------------
// Anim Tile RUN
// ===============

public void AniTilRUN()
{
	for (int i=0 ; i<AniTilC ; i++)
	{
	if ( AniTils[AniTilP[i]].Play() )
		{
		int t=AniTilP[i]; AniTils[t] = null;
		AniTilP[i--] = AniTilP[--AniTilC]; AniTilP[AniTilC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Enemigo - Engine
// ====================
// ********************

final static int EnemigoSpd=3;

public class Enemigo extends AnimSprite
{
int Mode = 0;
int Side = 1;
int Esperas = 5;

// ----------------
// Enemigo Play
// ================

public boolean Play()
{

// Acuchillamos a enemigo???

	if (Mode < 80 && ProtMode == 5 && AniSprs[ProtAnim].FrameAct==1)
	{
		if ( Colision(CoorX+ProtCuX, CoorY+ProtCuY, ProtCuSizeX, ProtCuSizeY,   ProtX+ProtCuX+(Side*TileSize), ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY) )
		{
		Mode=80;
		}
	}


	switch (Mode)
	{
	case 0:
		SpriteSET(0, 1, 40, 0x102);
		Side*=-1;
		Mode++;
	break;

	case 1:

// Controlamos si estamos a la vista del enemigo

		if ( Colision(CoorX+ProtCentX-(gc.sc.ScrollSizeX/2), CoorY+ProtCentY-TileSize, gc.sc.ScrollSizeX, TileSize*2,  ProtX+ProtCentX-2, ProtY+ProtCentY-2, 4,4) )
		{
		Side = (ProtX < CoorX)? -1:1;
		SpriteSET(1, 3, 1, 0x101);
		Mode = 10;
		break;
		}

// Esperamos mientras miramos a ambos lados

		if (Pause!=0) {break;}

		if (--Esperas > 0)
		{
		Mode=0;
		} else {
		SpriteSET(1, 3, 1, 0x101);
		Mode=10;
		}
	break;

	case 10:

// Chequeamos si tocamos al Prota.

		if ( ProtMode<80 && Colision(CoorX+ProtCuX+(Side*TileSize), CoorY+ProtCuY, ProtCuSizeX, ProtCuSizeY,   ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY) )
		{
		SpriteSET(8, 2, 2, 0x102);
		Mode=20;
		break;
		}

// Corremos

		if ( CheckTile(CoorX+ProtCuX+(Side*EnemigoSpd), CoorY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
		{
		CoorX+=Side*EnemigoSpd;
		} else {
		Esperas = 5;
		Mode = 0;
		}
	break;

	case 20:
		if (Pause!=0) {break;}

		if ( Colision(CoorX+ProtCuX+(Side*TileSize), CoorY+ProtCuY, ProtCuSizeX, ProtCuSizeY,   ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY) )
		{
		ProtMorirSET(15);
		}

		SpriteSET(0, 1, 10, 0x102);
		Mode++;
	break;

	case 21:
		if (Pause==0) {Mode=0;}
	break;

	case 80:
		SpriteSET(10, 2, 2, 0x102);
		gc.SoundSET(3,1);
		Mode++;
	break;
	}

	FrameBase = Side==1? 0:24;

	return super.Play();
}

};


// ---------------
// Enemigo SET
// ===============

public int EnemigoSET(int CoorX, int CoorY)
{
	if (EnemigoC == EnemigoMAX) {return(-1);}

	int i=EnemigoP[EnemigoC++];

	Enemigos[i] = new Enemigo();

	Enemigos[i].SpriteSET(CoorX, CoorY,  1, 3, 1, 0x101);

	return (i);
}


// ---------------
// Enemigo INI
// ===============

final int EnemigoMAX=8;
int EnemigoC;
int[] EnemigoP;
Enemigo[] Enemigos;

public void EnemigoINI()
{
	EnemigoC = 0;
	EnemigoP = new int[EnemigoMAX];
	Enemigos = new Enemigo[EnemigoMAX];
	for (int i=0 ; i<EnemigoMAX ; i++) {EnemigoP[i]=i;}
}

// ---------------
// Enemigo RES
// ===============

public void EnemigoRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=EnemigoP[i];
	Enemigos[t] = null;
	EnemigoP[i--] = EnemigoP[--EnemigoC];
	EnemigoP[EnemigoC] = t;
}

// ---------------
// Enemigo END
// ===============

public void EnemigoEND()
{
	EnemigoP = null;
	Enemigos = null;
	EnemigoC = 0;
}

// ---------------
// Enemigo RUN
// ===============

public void EnemigoRUN()
{
	for (int i=0 ; i<EnemigoC ; i++)
	{
	if ( Enemigos[EnemigoP[i]].Play() )
		{
		int t=EnemigoP[i];
		Enemigos[t] = null;
		EnemigoP[i--] = EnemigoP[--EnemigoC];
		EnemigoP[EnemigoC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Ratonera - Engine
// ====================
// ********************

final int RatoneraMAX=8;
int[][] Ratoneras;
int[] RatoneraP;
int RatoneraC=0;


// ---------------
// Ratonera SET
// ===============

public int RatoneraSET(int CoorX, int CoorY)
{
	if (RatoneraC == RatoneraMAX) {return(-1);}

	int ID = RatoneraP[RatoneraC++];
	Ratoneras[ID] = new int[3];

	Ratoneras[ID][0] = CoorX;
	Ratoneras[ID][1] = CoorY;
	Ratoneras[ID][2] = 50+RND(100);

	return (ID);
}


// ---------------
// Ratonera Play
// ===============

public boolean RatoneraPlay(int[] Data)
{
	if (--Data[2] < 0)
	{
	Data[2] = 100+RND(150);
	RataSET(Data[0],Data[1]);
	}

	return false;
}



// ---------------
// Ratonera INI
// ===============

public void RatoneraINI()
{
	RatoneraC = 0;
	Ratoneras = new int[RatoneraMAX][];
	RatoneraP = new int[RatoneraMAX];
	for (int i=0 ; i<RatoneraMAX ; i++) {RatoneraP[i]=i;}
}

// ---------------
// Ratonera END
// ===============

public void RatoneraEND()
{
	RatoneraP = null; Ratoneras = null; RatoneraC = 0;
}

// ---------------
// Ratonera RUN
// ===============

public void RatoneraRUN()
{
	for (int i=0 ; i<RatoneraC ; i++)
	{
	if ( RatoneraPlay(Ratoneras[RatoneraP[i]]) ) {int t=RatoneraP[i]; Ratoneras[t] = null; RatoneraP[i--] = RatoneraP[--RatoneraC]; RatoneraP[RatoneraC] = t;}
	}
}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Rata - Engine
// ====================
// ********************

final static int RataSpd = 2;

public class Rata extends AnimSprite
{
int Mode=0;
int Side = -1;
int Time = 0;
boolean Gira = false;

// ----------------
// Rata Play
// ================

public boolean Play()
{

	switch (Mode)
	{
	case 0:
		if (( CheckTile(CoorX+ProtCuX+(Side*RataSpd), CoorY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
		&&	( CheckTile(CoorX+ProtCentX+(Side*ProtCentX), CoorY+ProtCuY+ProtCuSizeY, 0x01) != 0 ))
		{
		CoorX+=Side*RataSpd;
		} else {
		Side*=-1;
		Gira = true;
		}

		if ( Time==0 && Colision(ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX, ProtCuSizeY,   CoorX+ProtCentX-2+(Side*(TileSize/2)), CoorY+(TileSize*2), 4,TileSize ) )
		{
		ProtMorirSET(5);
		SpriteSET(23, 1, 5, 0x102);
		Mode++;
		}

		if (Time>0) {Time--;}

		if ( Gira && CheckTile(CoorX+ProtCentX, CoorY+ProtCentY, 0x108) == 0x108 ) {if (RND(3)==1) {return true;} else {Gira=false;}}
	break;	

	case 1:
		if (Pause==0)
		{
		SpriteSET(21, 2, 2, 0x101);
		Time = 50;
		Mode=0;
		}
	break;
	}

	FrameBase = Side==1? 0:24;

	return super.Play();
}

};


// ---------------
// Rata SET
// ===============

public int RataSET(int CoorX, int CoorY)
{
	if ( Colision(ProtX+ProtCentX-(4*TileSize), ProtY-TileSize, 11*TileSize, 4*TileSize, CoorX+ProtCentX, CoorY+ProtCentY, 2,2 ) )
	{
	return -1;
	}


	if (RataC == RataMAX) {return(-1);}

	int i=RataP[RataC++];

	Ratas[i] = new Rata();

	Ratas[i].SpriteSET(CoorX, CoorY,  21, 2, 2, 0x101);

	return (i);
}


// ---------------
// Rata INI
// ===============

final int RataMAX=5;
int RataC;
int[] RataP;
Rata[] Ratas;

public void RataINI()
{
	RataC = 0;
	RataP = new int[RataMAX];
	Ratas = new Rata[RataMAX];
	for (int i=0 ; i<RataMAX ; i++) {RataP[i]=i;}
}

// ---------------
// Rata RES
// ===============

public void RataRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=RataP[i];
	Ratas[t] = null;
	RataP[i--] = RataP[--RataC];
	RataP[RataC] = t;
}

// ---------------
// Rata END
// ===============

public void RataEND()
{
	RataP = null;
	Ratas = null;
	RataC = 0;
}

// ---------------
// Rata RUN
// ===============

public void RataRUN()
{
	for (int i=0 ; i<RataC ; i++)
	{
	if ( Ratas[RataP[i]].Play() )
		{
		int t=RataP[i];
		Ratas[t] = null;
		RataP[i--] = RataP[--RataC];
		RataP[RataC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Bicha - Engine
// ====================
// ********************

public class Bicha
{
int Mode=0;
int Anim;
int CoorX;
int CoorY;
int Time;

// ----------------
// Bicha Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		if ( Colision(CoorX-(TileSize*2), CoorY, TileSize*5, TileSize*3,  ProtX+ProtCentX, ProtY+ProtCuY, 2,ProtCuSizeY) )
		{
		int X = ((ProtX+ProtCentX) > CoorX+(TileSize/2)) ? 0:-2;
		Anim = AniSprSET(-1, CoorX+(TileSize*X),CoorY,  16, 5, 0, 0x102);
		if (Anim!=-1) {Mode++;}
		AniSprs[Anim].FrameBase = (X!=0? 0:24);
		}
	break;	

	case 1:
		if (AniSprs[Anim].Pause==0)
		{
		if ( Colision(ProtX+ProtCuX, ProtY+ProtCuY, ProtCuSizeX,ProtCuSizeY,  AniSprs[Anim].CoorX+TileSize, CoorY+TileSize, TileSize,TileSize ) ) {ProtMorirSET(10);}

		AniSprSET(Anim,  16, 5, 0, 0x112);
		Mode++;
		}
	break;

	case 2:
		if (AniSprs[Anim].Pause==0)
		{
		Anim = AniSprRES(Anim);		
		Time=0;
		Mode++;
		}
	break;

	case 3:
		if (++Time>50) {Mode=0;}
	break;
	}

	return false;
}

};


// ---------------
// Bicha SET
// ===============

public int BichaSET(int CoorX, int CoorY)
{
	if (BichaC == BichaMAX) {return(-1);}

	int i=BichaP[BichaC++];

	Bichas[i] = new Bicha();

	Bichas[i].CoorX = CoorX;
	Bichas[i].CoorY = CoorY;

	return (i);
}


// ---------------
// Bicha INI
// ===============

final int BichaMAX=8;
int BichaC;
int[] BichaP;
Bicha[] Bichas;

public void BichaINI()
{
	BichaC = 0;
	BichaP = new int[BichaMAX];
	Bichas = new Bicha[BichaMAX];
	for (int i=0 ; i<BichaMAX ; i++) {BichaP[i]=i;}
}

// ---------------
// Bicha RES
// ===============

public void BichaRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=BichaP[i];
	Bichas[t] = null;
	BichaP[i--] = BichaP[--BichaC];
	BichaP[BichaC] = t;
}

// ---------------
// Bicha END
// ===============

public void BichaEND()
{
	BichaP = null;
	Bichas = null;
	BichaC = 0;
}

// ---------------
// Bicha RUN
// ===============

public void BichaRUN()
{
	for (int i=0 ; i<BichaC ; i++)
	{
	if ( Bichas[BichaP[i]].Play() )
		{
		int t=BichaP[i];
		Bichas[t] = null;
		BichaP[i--] = BichaP[--BichaC];
		BichaP[BichaC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// Globo - Engine
// ===================
// *******************

boolean GloboPaint = false;
boolean GloboMarcoPaint = false;

boolean GloboNext;

int GloboPos=0;
String[] GloboTxt;

// -------------------
// Globo SET
// ===================

public void GloboSET(String[] Texto)
{
	GloboNext = false;

	GameStatusBack = GameStatus;
	GameStatus=150;

	GloboPos = 0;
	GloboTxt = Texto;

	GloboSET(Texto[0]);
}

public void GloboSET(String Texto)
{
	GloboNext = false;

	GloboListINI();
	GloboListADD(Texto, 0);
	GloboListSET();
	GloboMarcoPaint = true;
}

// -------------------
// Globo RUN
// ===================

public boolean GloboRUN()
{
	if ( GloboNext || (KeybM1!=0 && KeybM1!=KeybM2) || (KeybB1!=0 && KeybB1!=KeybB2) )
	{
	if (++GloboPos == GloboTxt.length) {return true;} else {GameIMP(GameStatusBack); GloboSET(GloboTxt[GloboPos]);}
	}

	GloboPaint = true;

	return false;
}

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// GloboList - Engine - Rev.0 (20.6.2003)
// ===================
// *******************

boolean GloboList_ON=false;		// Estado del GloboList Engine
boolean GloboListUpdate=false;

int GloboListPos;
int GloboListCMD;

String	GloboListStr[][];
int		GloboListDat[][];

long GloboListTimeIni;
int GloboListTimeWait;

int GloboPosY = 0;
int GloboPosX = 1;

int GloboMaxX;
int GloboMaxY;

// -------------------
// GloboList INI
// ===================

public void GloboListINI()
{
	GloboPosY = 0;
	GloboPosX = 1;

	GloboList_ON=false;

	GloboListStr=null;
	GloboListDat=null;

	GloboListPos=0;
}

// -------------------
// GloboList INI
// ===================

public void GloboListEND()
{
	GloboListINI();
}

// -------------------
// GloboList ADD
// ===================

public void GloboListADD(int Dato, String Texto)
{
	GloboListADD(Dato, new String[] {Texto}, 0, 0);
}

public void GloboListADD(int Dato, String Texto, int Dat1)
{
	GloboListADD(Dato, new String[] {Texto}, Dat1, 0);
}

public void GloboListADD(int Dat0, String[] Texto, int Dat1, int Dat2)
{
	int Lines=(GloboListDat!=null)?GloboListDat.length:0;

	String Str[][] = new String[Lines+1][];
	int Dat[][] = new int[Lines+1][];

	int i=0;

	for (; i<Lines; i++)
	{
	Dat[i] = GloboListDat[i];
	Str[i] = GloboListStr[i];
	}

	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2};

	GloboListStr=Str;
	GloboListDat=Dat;
}

public void GloboListADD(String Texto, int Dato)
{
	int Pos=0, PosIni=0, PosOld=0, Size=0;

	char[] Tex = Texto.toCharArray();

	Font f=gc.GloboListGetFont();

	while ( PosOld < Tex.length )
	{
	Size=0;

	Pos=PosIni;

	while ( Size < (gc.CanvasSizeX-16) )
	{
		if ( Pos == Tex.length ) {PosOld=Pos; break;}

		int Dat = Tex[Pos++];
		if (Dat==0x20) {PosOld=Pos-1;}
		Size += f.charWidth((char)Dat);
	}

	if (PosOld-PosIni < 1) { while ( Pos < Tex.length && Tex[Pos] >= 0x30 ) {Pos++;} PosOld=Pos; }

	GloboListADD(Dato, Texto.substring(PosIni,PosOld));

	PosIni=PosOld+1;

	}
}


// -------------------
// GloboList SET
// ===================

public void GloboListSET()
{
	Font f = gc.GloboListGetFont();

	GloboMaxX = 16;
	GloboMaxY = GloboListStr.length * f.getHeight();

	for (int i=0 ; i<GloboListStr.length ; i++)
	{
	int SizeX = f.stringWidth(GloboListStr[i][0]);
	if ( GloboMaxX < SizeX ) {GloboMaxX = SizeX;}
	}

	GloboList_ON = true;
}




// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Amigo - Engine
// ====================
// ********************

int AmigoAnim;
int AmigoType;

int AmigoCaerPos;
int AmigoDemoTime;
int AmigoBoteAjusteX;
int AmigoBoteAjusteY;

boolean AmigoDead;
boolean AmigoDeadJaula;

boolean AmigoAlBote;
boolean AmigoEnBote;

// ---------------
// Amigo INI
// ===============

public void AmigoINI()
{
	AmigoAnim = -1;
	AmigoAlBote = false;
	AmigoEnBote = false;
}


// ---------------
// Amigo END
// ===============

public void AmigoEND()
{
	AmigoAnim = -1;
}


// ---------------
// Amigo SET
// ===============

public void AmigoSET(int Type)
{
	AmigoType = Type;

	switch (Type)
	{
	case 0:
		AmigoAnim = AniSprSET(AmigoAnim, ProtX+(TileSize*3), 25*TileSize, AmigoFr,6,1, 0x021);	// Amigo
		AniSprs[AmigoAnim].FrameBase = 40;
	break;

	case 1:
		AmigoAnim = AniSprSET(-1, 0, 0, AmigoFr+15,1,1, 0x022);	// Amigo
	break;	

	case 2:
		AmigoAnim = AniSprSET(-1, 1*TileSize, 8*TileSize, AmigoDead?AmigoFr+14:AmigoFr+15,1,2, 0x021);
	break;
	}


}

// ---------------
// Amigo RUN
// ===============

public void AmigoRUN()
{
// Contador de Tiempo para regresar con el botiquin
	if (JugarConta > 0) {if (--JugarCnt < 0) {JugarCnt=20; if (--JugarConta==0)	{AmigoDead=true;}}}

	if (AmigoAnim == -1) {return;}

	switch (AmigoType)
	{
	case 0:

		int DestX = ProtX;
		int DespX = 3*TileSize;

		if (AmigoAlBote)
		{
		DestX = 99*TileSize;
		DespX = TileSize;
		}


		if (DestX-DespX > AniSprs[AmigoAnim].CoorX || DestX+DespX < AniSprs[AmigoAnim].CoorX)
		{
		int Desp = DestX < AniSprs[AmigoAnim].CoorX ? -1:1;

			if ( CheckTile(AniSprs[AmigoAnim].CoorX+ProtCuX+(Desp*ProtSpd), AniSprs[AmigoAnim].CoorY+ProtCuY, ProtCuSizeX-1,ProtCuSizeY-1, 0x01) == 0 )
			{
			AniSprs[AmigoAnim].CoorX+=Desp*ProtSpd;
			AniSprs[AmigoAnim].FrameIni = AmigoFr+1;
			AniSprs[AmigoAnim].Pause = 1;
			AniSprs[AmigoAnim].FrameBase = 20*((Desp*-1)+1);
			}
		} else {
			AniSprs[AmigoAnim].FrameIni = AmigoFr+0;
			AniSprs[AmigoAnim].FrameAct = 0;

			AniSprs[AmigoAnim].FrameBase = ProtX < AniSprs[AmigoAnim].CoorX? 40:0;

			if (AmigoAlBote) {AmigoType = 10;}
		}
	break;

	case 1:
		AniSprs[AmigoAnim].CoorX = AniSprs[JaulaAnim].CoorX + 2;
		AniSprs[AmigoAnim].CoorY = AniSprs[JaulaAnim].CoorY + JaulaY + 6;

		if (!AmigoDeadJaula && JaulaY > 29*TileSize) {AmigoDeadJaula = true;}

		AniSprs[AmigoAnim].FrameIni = AmigoFr+ (AmigoDeadJaula? 14:15);
	break;

	case 2:
		if (JugarLevel==3 && AmigoDead)
		{
		AniSprs[AmigoAnim].FrameIni = AmigoFr+14;
		}
	break;



	case 10:
		AniSprSET(AmigoAnim, AmigoFr+2, 1,0, 0x022);	// Amigo
		AmigoCaerPos=0;
		AmigoDemoTime=0;
		AmigoType++;
	break;

	case 11:
		int Desp = ProtCaerTab[AmigoCaerPos]; if (++AmigoCaerPos == ProtCaerTab.length) {AmigoCaerPos--;}

		if ( (AmigoDemoTime+=Desp) >= 3*TileSize )
		{
		AmigoType++;
		AmigoBoteAjusteX = AniSprs[AmigoAnim].CoorX - BoteX;
		AmigoBoteAjusteY = AniSprs[AmigoAnim].CoorY - BoteY;
		AmigoEnBote = true;
		}

		AniSprs[AmigoAnim].CoorY+=Desp;
	break;

	case 12:
		AniSprs[AmigoAnim].CoorX = BoteX + AmigoBoteAjusteX;
		AniSprs[AmigoAnim].CoorY = BoteY + AmigoBoteAjusteY;
	break;
	}

}

// <=- <=- <=- <=- <=-











// ********************
// --------------------
// Menu - Engine
// ====================
// ********************

boolean MenuPaint=false;

int MenuExit=0;
int MenuMode;

// ---------------
// Menu INI
// ===============

public void MenuINI()
{
	MenuExit=0;
	gc.MenuINI_Gfx();
}


// ---------------
// Menu END
// ===============

public void MenuEND()
{
	gc.MenuEND_Gfx();
	MenuPaint=false;
}


// ---------------
// Menu SET
// ===============

public void MenuSET()
{
	MenuExit=0;
	MenuMode=0;

	MenuPaint=true;
}


// ---------------
// Menu RUN
// ===============

public boolean MenuRUN()
{
	switch (MenuMode)
	{
	case 0:
		if ( (KeybM1!=0 && KeybM2==0) || (KeybB1!=0 && KeybB2==0) )
		{
		PanelSET(0);
		MenuMode++;
		}
	break;
	}


	if (MenuExit!=0) {return true;}

	MenuPaint=true;

	return false;
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Panel - Engine
// ===================
// *******************

int PanelStatus;
int PanelType;
int PanelTypeOld;

// -------------------
// Panel SET
// ===================

public void PanelSET(int Type)
{
	PanelStatus = GameStatus;
	GameStatus = 900;

	PanelTypeOld = PanelType;
	PanelType = Type;

	switch (Type)
	{
	case 0:
		ma.MarcoINI();
		ma.MarcoADD(0x000,Texto.Start,0);
		if (gc.DeviceSound) {ma.MarcoADD(0x000,Texto.Sound,10, GameSound);}
		if (gc.DeviceVibra) {ma.MarcoADD(0x000,Texto.Vibra,11, GameVibra);}
		ma.MarcoADD(0x000,Texto.Controls,6);
		ma.MarcoADD(0x000,Texto.Credits,7);
		ma.MarcoADD(0x000,Texto.ExitGame,9);
		ma.MarcoSET_Option();
	break;

	case 1:
		ma.MarcoINI();
		ma.MarcoADD(0x000,Texto.Continue,1);
		if (gc.DeviceSound) {ma.MarcoADD(0x000,Texto.Sound,10, GameSound);}
		if (gc.DeviceVibra) {ma.MarcoADD(0x000,Texto.Vibra,11, GameVibra);}
		ma.MarcoADD(0x000,Texto.Controls,6);
		ma.MarcoADD(0x000,Texto.Restart,8);
		ma.MarcoADD(0x000,Texto.ExitGame,9);
		ma.MarcoSET_Option();
	break;

	case 6:
		ma.MarcoINI();
		Texto.sc_Controles(ma);
		ma.MarcoSET_Scroll(gc.ScWait,gc.ScDesp);
	break;

	case 7:
		ma.MarcoINI();
		Texto.sc_Credits(ma);
		ma.MarcoSET_Scroll(gc.ScWait,gc.ScDesp);
	break;

	case 80:
		ma.MarcoINI();
		ma.MarcoADD(0x011,Texto.Loading);
		ma.MarcoSET_Texto();
	break;
	}

}

// -------------------
// Panel END
// ===================

public void PanelEND()
{
	ma.MarcoEND();
	GameStatus = PanelStatus;
}


// -------------------
// Panel RUN
// ===================

public void PanelRUN()
{
	int MarcoKey=0;
	if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
	if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
	if (KeybM1!= 0 && KeybM2== 0) {MarcoKey=5;}

	int Result = ma.MarcoRUN(MarcoKey, KeybY1);
	PanelEXE(Result);
}

// ---------------
// Panel EXE
// ===============

public void PanelEXE(int CMD)
{
	switch (CMD)
	{
	case -2: // Scroll ha llegado al final
		PanelEND();
		GameIMP();
		PanelSET(PanelTypeOld);
	break;

	case 0:	// Jugar de 0
		PanelEND();
		MenuExit = 1;
	break;

	case 1:	// Continuar
		PanelEND();
		GameIMP();
	break;

	case 5:	// Siguiente Nivel
	break;

	case 6:	// Controles...
	case 7:	// About...
		PanelEND();
		GameIMP();
		PanelSET(CMD);
	break;

	case 8:	// Restart
		PanelEND();
		GameIMP();
		FaseExit = 3;
	break;

	case 9:	// Exit Game
		GameExit = 1;
	break;	

	case 10:
		GameSound = ma.getOption();
		if (PanelType==0) {if (GameSound!=0) {gc.SoundSET(0,0);} else {gc.SoundRES();}}
	break;

	case 11:
		GameVibra = ma.getOption();
	break;
	}

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
		rs = RecordStore.openRecordStore("Camara_Prefs", true);

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
		rs = RecordStore.openRecordStore("Camara_Prefs", true);

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






// -------------------
// Cheats
// ===================

public void Cheats()
{

	if (KeybB1 == 35 && KeybB1!=KeybB2) {JugarLlave=0x7F; JugarCuchillo = true;}

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