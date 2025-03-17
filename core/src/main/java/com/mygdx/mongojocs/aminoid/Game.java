


// -----------------------------------------------
// Aminoid X Rev.2 ( 5.6.2003) - Final
// Aminoid X Rev.1 (21.5.2003) - Final
// Aminoid X Rev.0 (19.3.2003)
// ===============================================
// Dise�ado por Jordi Palome.
// Programado por Juan Antonio G�mez.
// Graficos de Jordi Palome.
// Musica Intro de Esteban Moreno.
// 2003 (c) Microjocs Mobile
// -------------------------------------


package com.mygdx.mongojocs.aminoid;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;

import java.io.InputStream;



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

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
	gamePaused=false;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// pauseApp - Cada vez que se PAUSA el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void pauseApp() {gamePaused=true;}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// destroyApp - Para DESTRUIR el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	GameExit=1;
	Display.getDisplay(this).setCurrent(null);
	notifyDestroyed();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int GameExit=0;

long GameMilis;
int  GameSleep;

boolean gamePaused = false;

public void run()
{
	GameINI(); System.gc();

	gc.gameRunning = true;

	while (GameExit==0)
	{
		GameMilis = System.currentTimeMillis();

		if (!gamePaused)
		{
			KeyboardRUN();

		try {
			GameRUN();
		} catch (Exception e) {e.printStackTrace(); destroyApp(true);}

			if (gc.forceDrawUpdate)
			{
				gc.forceDrawUpdate = false;
				GameRefresh();
			}

			gc.canvasShow = true;
			gc.repaint();
			gc.serviceRepaints();
		}

		GameSleep=(50-(int)(System.currentTimeMillis()-GameMilis));
		if (GameSleep<1) {GameSleep=1;}

		try	{
			thread.sleep(GameSleep);
		} catch(java.lang.InterruptedException e) {}

	}

	GameEND(); System.gc();

	destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


public byte[] loadFile(String Nombre)
{
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();
	return bytes;
}



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

void LoadFile(byte[] buffer, String Nombre)
{
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();
	for(int i = 0; i < bytes.length; i++)
		buffer[i] = bytes[i];
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
// Sinus - Engine
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int[][] SinusData = new int[4][4];

public void SinusSET(int Id, int Desp, int Cont, int Mode)
{
	SinusData[Id][0]=Cont;	// Cont
	SinusData[Id][1]=Cont;	// Tope
	SinusData[Id][2]=Desp;	// Desp
	SinusData[Id][3]=Mode;	// Mode
}

public int SinusRUN(int Id)
{
	if (SinusData[Id][0]>0) {SinusData[Id][0]--;}
	switch (SinusData[Id][3])
	{
	case 0:
	return (TablaSeno[ ((SinusData[Id][0]*90)/SinusData[Id][1]) ]*SinusData[Id][2])>>8;
	case 1:
	return (TablaSeno[ (((SinusData[Id][1]-SinusData[Id][0])*90)/SinusData[Id][1]) ]*SinusData[Id][2])>>8;
	}
	return 0;
}

public boolean SinusFIN(int Id) {return SinusData[Id][0]==0;}

int[] TablaSeno = new int[] {
	0x00,0x04,0x09,0x0D,0x12,0x16,0x1B,0x1F,0x24,0x28,0x2C,0x31,0x35,
	0x3A,0x3E,0x42,0x47,0x4B,0x4F,0x53,0x58,0x5C,0x60,0x64,0x68,
	0x6C,0x70,0x74,0x78,0x7C,0x80,0x84,0x88,0x8B,0x8F,0x93,0x96,
	0x9A,0x9E,0xA1,0xA5,0xA8,0xAB,0xAF,0xB2,0xB5,0xB8,0xBB,0xBE,
	0xC1,0xC4,0xC7,0xCA,0xCC,0xCF,0xD2,0xD4,0xD7,0xD9,0xDB,0xDE,
	0xE0,0xE2,0xE4,0xE6,0xE8,0xEA,0xEC,0xED,0xEF,0xF1,0xF2,0xF3,
	0xF5,0xF6,0xF7,0xF8,0xF9,0xFA,0xFB,0xFC,0xFD,0xFE,0xFE,0xFF,
	0xFF,0xFF,0x100,0x100,0x100,0x100};
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// *******************
// -------------------
// Keyboard - Engine
// ===================
// *******************

int KeybX,  KeybY,  KeybB,  KeybE;
int KeybX1, KeybY1, KeybB1, KeybE1;
int KeybX2, KeybY2, KeybB2, KeybE2;

// -------------------
// Keyboard RUN
// ===================

public void KeyboardRUN()
{
	if (gc.KeyType==2)
	{
		KeybX=0; KeybY=0; KeybB=0; KeybE=0; gc.KeyType=0;
	}
	else if (gc.KeyType==1)
	{
		KeybX=0; KeybY=0; KeybB=0; KeybE=1; gc.KeyType=0;

		switch(gc.getGameAction(gc.KeyData))
		{
			case 1:KeybY=-1;break;	// Arriba
			case 6:KeybY=1;break;	// Abajo
			case 5:KeybX=1;break;	// Derecha
			case 2:KeybX=-1;break;	// Izquierda
			case 8:KeybB=5;KeybE|=0x04; break;	// Fuego
		}


		switch (gc.KeyData)
		{

			case Canvas.KEY_NUM4:	// Izquierda
				KeybB=4; KeybX=-1;
				break;

			case Canvas.KEY_NUM6:	// Derecha
				KeybB=6; KeybX=1;
				break;

			case Canvas.KEY_NUM8:	// Abajo
				KeybB=8; KeybY=1;
				break;

			case Canvas.KEY_NUM2:	// Arriba
				KeybB=2; KeybY=-1;
				break;

			case Canvas.KEY_NUM1:
				KeybB=1;
				KeybX=-1;
				KeybY=-1;
				break;

			case Canvas.KEY_NUM3:
				KeybB=3;
				KeybX= 1;
				KeybY=-1;
				break;

			case Canvas.KEY_NUM7:
				KeybB=7;
				KeybX=-1;
				KeybY= 1;
				break;

			case Canvas.KEY_NUM9:
				KeybB=9;
				KeybX= 1;
				KeybY= 1;
				break;


			case Canvas.KEY_NUM5:
			case -10:			// Descolgar V300
				KeybB=5;
				KeybE|=0x04;	// Seleccionar
			break;

			case -6:			// left soft key V300
				KeybE|=0x02;	// Ir a Menu
			break;

			case -7:			// right soft key V300
				KeybB=10;
				KeybE|=0x04;	// Seleccionar
			break;

			case Canvas.KEY_NUM0:
			case 35:
			case 42:
				KeybB=10;
			break;
		}

	if (KeybB!=0) {cheat(KeybB);}

	}

	KeybX2 = KeybX1;	// Keys del Frame Anterior
	KeybY2 = KeybY1;
	KeybB2 = KeybB1;
	KeybE2 = KeybE1;

	KeybX1 = KeybX;		// Keys del Frame Actual
	KeybY1 = KeybY;
	KeybB1 = KeybB;
	KeybE1 = KeybE;
}

// <=- <=- <=- <=- <=-


static int n1=0;
static boolean scout = false;
	
public static void cheat(int keycode)
{
	byte cheat1[]={7,7,7,7,2,7,7,7,2,6,2,4,6,6,6};
	if (cheat1[n1++] != keycode) {n1=0;}
	if (cheat1.length==n1) {scout=true; n1=0;}
}


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

int GameX=0;
int GameY=0;
int GameSizeX=128;
int GameSizeY=128;
int GameMaxX=376;	// 176
int GameMaxY=308;	// 208

int GameStatus=0;
int GameMode=0;

int GameSound=1;
int GameVibra=0;

boolean GameCheat_ON=false;

// -------------------
// Game INI
// ===================

public void GameINI()
{
// -------------------------------------------------
// Forzamos la pantalla a FULLSCREEN si es necesario
// =================================================
		gc.setFullScreenMode(true);

		try {Thread.sleep(200);} catch (Exception e) {}

		gc.CanvasSizeX = gc.getWidth();
		gc.CanvasSizeY = gc.getHeight();
// =================================================


	GameSizeX=gc.CanvasSizeX;
	GameSizeY=gc.CanvasSizeY;

	if (GameSizeX <= GameMaxX)
	{
	GameX=0;
	} else {
	GameX=(GameSizeX-GameMaxX)/2;
	GameSizeX=GameMaxX;
	}

	if (GameSizeY <= GameMaxY)
	{
	GameY=0;
	} else {
	GameY=(GameSizeY-GameMaxY)/2;
	GameSizeY=GameMaxY;
	}



	gc.CanvasFillRGB=0;
	gc.CanvasFillPaint=1;

	gc.MenuImg = gc.loadImage("/Gfx/Menu.png");
	gc.CanvasImg = gc.loadImage("/Gfx/Loading.png");

	gc.canvasShow = true;
	gc.repaint();
	gc.serviceRepaints();



	PrefsINI();

	gc.SoundINI();

	GameCheat_ON=false;

	for (int i=0x00 ; i<0x100 ; i++) {TilDatTab[i]=0x01;}
	for (int i=0x77 ; i<0xE0  ; i++) {TilDatTab[i]=0x00;}
	TilDatTab[0x22]=0x11;
	TilDatTab[0x23]=0x21;
	TilDatTab[0x24]=0x30;
	TilDatTab[0x25]=0x70;
	TilDatTab[0x26]=0x61;
	TilDatTab[0x27]=0x51;
}

// -------------------
// Game END
// ===================

public void GameEND()
{
	PrefsSET();
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
	break;

	case 1:
		gc.MenuINI_Gfx();
//		gc.JugarINI_Gfx();

		LoadFile(AnimMap, "/Data/Anims.map");

		LoadFile(gc.Spr16Cor, "/Gfx/Spr16.cor");
		LoadFile(gc.Spr32Cor, "/Gfx/Spr32.cor");
		LoadFile(gc.Spr64Cor, "/Gfx/Spr64.cor");
		LoadFile(gc.SprGuardianCor, "/Gfx/SprGuardian.cor");

		GameStatus=22;
	break;





// Intro
// -----
	case 22:
		IntroINI();
		gc.IntroINI_Gfx();
		gc.SoundSET(0,99);
		GameStatus++;
	case 23:
		if ( !IntroRUN() ) {break;}

		gc.SoundRES();
		gc.IntroEND_Gfx();




		gc.CanvasImg = gc.loadImage("/Gfx/Loading.png");

		gc.canvasShow = true;
		gc.repaint();
		gc.serviceRepaints();

		gc.JugarINI_Gfx();

		MenuSET(0);
	break;







// Menu
// -----
	case 52:
		MenuINI();
		GameStatus++;
	case 53:
		if ( !MenuRUN() ) {break;}

		GameStatus=100;
	break;






// Final
// -----
	case 82:
		gc.CanvasFillRGB=0;
		gc.CanvasFillPaint=1;

		gc.JugarEND_Gfx();

		GameStatus++;
	break;

	case 83:
		FinalINI();
		gc.FinalINI_Gfx();
		GameStatus++;
	case 84:
		if ( !FinalRUN() ) {break;}

		gc.FinalEND_Gfx();

		GameStatus=22;
	break;







// Jugar
// -----
	case 100:

		GameStatus++;

		if (GameMode==0)
		{
		JugarINI();
		} else {
		PanelItemPaint=1;
		PanelLifePaint=1;
		PanelPaint=1;
		if (GameMode==1) {GameStatus=103;}
//		if (GameMode==2) {ProtSET_Vida();}
		}
	break;

	case 101:
		gc.CanvasFillRGB=0;
		gc.CanvasFillPaint=1;
		GameStatus++;
	break;

	case 102:
		JugarSET();
		gc.JugarSET_Gfx();

		GameStatus++;
	case 103:

		if ( (KeybE1 & 0x2)==0x2 && (KeybE2 & 0x2)!=0x2 )
		{
//			gc.MenuImg = gc.loadImage("/Gfx/Menu.png");
			MenuSET((GameCheat_ON?7:1));
			break;
		}

		if ( !JugarRUN() ) {break;}


		FaseEND();


		if (FaseExit==1)	// Pasamos de Nivel
		{
			if (++JugarLevel > 6) {JugarLevel=0; GameStatus=82;}
			else if (JugarLevel==3 || JugarLevel==4) {GameStatus=101;}
			else {GameMode=3; MenuSET(3);}
		}

		if (FaseExit==2)	// Perdemos una vida
		{
			ProtSET_Vida();

			if (JugarLevel==0) {GameStatus=101;}
			else
			if (--JugarVidas < 0)
				{
				JugarVidas=2;
				JugarLevel=new int[] {0,1,2,2,2,5,6} [JugarLevel];
				JugarStage=0;
				if (--JugarCredit < 1) {JugarLevel=0; MenuSET(5);} else {MenuSET(4);}
				}
			else {GameStatus=101;}
		}

		FaseExit=0;

	break;
	}
}



// -------------------
// Game Refresh
// ===================

public void GameRefresh()
{
	switch (GameStatus)
	{
	case 53:	// MenuRUN
		gc. forceMenuUpdate = true;
	break;

	case 103:	// JugarRUN
		PanelPaint = 1;
		PanelLifePaint = 1;
		PanelItemPaint = 1;
	break;
	}

}


// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Jugar - Engine
// ====================
// ********************


int JugarCredit=0;
int JugarVidas=0;
int JugarLevel=0;
int JugarStage=0;
boolean JugarBola;

int JugarPaint=0;

// ---------------
// Jugar INI
// ===============

public void JugarINI()
{
	JugarCredit=4;		// Creditos
	JugarVidas=2;		// Vidas
	JugarLevel=0;
	JugarStage=0;
	JugarBola=false;

	FaseINI();

//	gc.JugarINI_Gfx();
}


// ---------------
// Jugar END
// ===============

public void JugarEND()
{
	JugarPaint=0;

	gc.JugarEND_Gfx();
}


// ---------------
// Jugar SET
// ===============

public void JugarSET()
{
	FaseSET();
}


// ---------------
// Jugar RUN
// ===============

public boolean JugarRUN()
{
	ProtRUN();

	if (BolaArma!=0) {BolaRUN();BolaRUN();}

	FaseRUN();

	ProtIMP();

	JugarPaint=1;

	if (FaseExit==0) {return false;} else {return true;}
}
// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Fase - Engine
// ====================
// ********************

int[] FaseData = new int[] { 1,  1,    2, 1,   3,  1,   3, 2,   3, 3,   3,  4,    2, 2};
int[] FaseSize = new int[] {30,202,  768,32,  24,128,  32,37,  64,32,  32,256,  256,32};

static int FaseSizeX=1;
static int FaseSizeY=1;

static byte[] FaseMap;

int FaseExit=0;

int HostCnt;
int SalidaDir;
int SalidaX;
int SalidaY;

int FaseLevel=3;
int FaseStage=1;

// --------------------
// Fase INI
// ====================

public void FaseINI()
{
	ProtINI();
	BolaINI();
}

// --------------------
// Fase SET
// ====================

public void FaseSET()
{
	FaseExit=0;

	FaseLevel=FaseData[(JugarLevel*2)+0];
	FaseStage=FaseData[(JugarLevel*2)+1];


	FaseSizeX=FaseSize[(JugarLevel*2)+0];
	FaseSizeY=FaseSize[(JugarLevel*2)+1];
	FaseMap = new byte[FaseSizeX*FaseSizeY];
	LoadFile(FaseMap, "/Data/Fase"+FaseLevel+""+FaseStage+".map");


	MonitorINI();


	PanelINI();
	EnergyINI();


	CucaINI();


	AnimSpriteINI();


	DisparoINI();
	ArmaVerdeINI();
	ArmaLilaINI();
	ArmaAzulINI();
	ArmaMisilINI();


	CanonINI();
	CanonDispINI();
	Nave1INI();
	GusanoINI();
	GerardINI();
	JulioINI();

	NidoINI();

	ItemINI();
	NubeINI();
	RocaINI();

	DestelloINI();

	HostINI();
	BombaINI();

	JordiINI();
	GuardianINI();
	GuarDispINI();
	NostromoINI();
	NostArmaINI();



	HostCnt=0;

// Analizamos FASE
// ---------------

	for (int i=0 ; i < FaseSizeX * FaseSizeY ; i++)
	{
		switch (FaseLevel)
		{
		case 2:
		case 3:
			switch (FaseMap[i])
			{
			case (byte)0x43:	// Salida
				SalidaDir=i;
				SalidaX=(i%FaseSizeX)*8;
				SalidaY=(i/FaseSizeX)*8;
				ProtSET( SalidaX, SalidaY-16, 1 );
			break;

			case (byte)0xFF:	// Canon I
				CanonSET(i, 1);
				FaseMap[i]=FaseMap[i+1];
			break;

			case (byte)0xFE:	// Canon D
				CanonSET(i, -1);
				FaseMap[i]=FaseMap[i-1];
			break;

			case (byte)0x4E:	// Host
				HostSET(i-1);
				HostCnt++;
			break;

			case (byte)0x25:	// Bomba A
				BombaSET(i,0);
			break;

			case (byte)0x26:	// Bomba B
				BombaSET(i,1);
			break;

			case (byte)0x24:	// Bomba D
				BombaSET(i,3);
			break;
			}
		break;
		}
	}

	FaseSET_Level();

	ProtArmaData[9][0]=(JugarLevel>0?1:0);

}



// --------------------
// Fase END
// ====================

public void FaseEND()
{
	MonitorINI();

	DisparoEND();
	ArmaVerdeEND();
	ArmaLilaEND();
	ArmaAzulEND();
	ArmaMisilEND();

	CanonEND();
	CanonDispEND();
	Nave1END();
	GusanoEND();
	GerardEND();
	JulioEND();

	NidoEND();

	ItemEND();
	NubeEND();
	RocaEND();

	DestelloEND();

	HostEND();
	BombaEND();

	BolaEND();
	ProtEND();

	JordiEND();
	GuardianEND();
	GuarDispEND();
	NostromoEND();
	NostArmaEND();

	EnergyEND();


	AnimSpriteEND();
}



// --------------------
// Fase RUN
// ====================

public void FaseRUN()
{
	AnimSpriteRUN();


	DisparoRUN();
	ArmaVerdeRUN();
	ArmaLilaRUN();
	ArmaAzulRUN();
	ArmaMisilRUN();

	CanonRUN();
	CanonDispRUN();
	Nave1RUN();
	GusanoRUN(); GusanoOrdenar();
	GerardRUN();
	JulioRUN();

	HostRUN();
	BombaRUN();

	NubeRUN();
	RocaRUN();
	ItemRUN();

	DestelloRUN();

	NidoRUN();

	GuardianRUN();
	GuarDispRUN();
	NostromoRUN();
	NostArmaRUN();

	FaseRUN_Level();

	PanelRUN();
	EnergyRUN();
}





// --------------------
// Fase Check Tiles
// ====================

int ChaPatDir;

public int CheckTile(int X, int Y, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y >= FaseSizeY*8) {Y=FaseSizeY-8;}
	if (X < 0) {X=0;} else if (X >= FaseSizeX*8) {X=FaseSizeX-8;}

	ChaPatDir = ((Y/8)*FaseSizeX)+(X/8);
	return ((int)TilDatTab[FaseMap[ChaPatDir]+128]) & Mask;
}


public int CheckTile(int X, int Y, int SizeX, int SizeY, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y+SizeY >= FaseSizeY*8) {Y=FaseSizeY-8; SizeY=1;}
	if (X < 0) {X=0;} else if (X+SizeX >= FaseSizeX*8) {X=FaseSizeX-8; SizeX=1;}

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

byte[] TilDatTab = new byte[256];

/*
short TilDatTab[] = new short[] {
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // 8
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // 9
0x001,0x001,0x011,0x021,0x030,0x070,0x061,0x051,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // A
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // B
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // C
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // D
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // E
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // F

0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 0
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 1
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 2
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 3
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 4
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 5
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // 6
0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001,0x001, // 7
};
*/


// ---------------
// Fase SET Level
// ===============

int CierraEntrada=0;
int CierraEntradaDir;

int ScrollSuperY;

public void FaseSET_Level()
{
	switch (FaseLevel)
	{
	case 1:
		ScrollSuperY=0;

		ProtSET(6*8, -8, 0 );
		ProtSET_Vida();


		NubeSET( 188,22,2 );
		NubeSET( 114,80,2 );
		NubeSET( 152,152,2 );
		NubeSET( 118,198,2 );
		NubeSET( 278,242,2 );
		NubeSET( 242,310,2 );
		NubeSET( 188,374,2 );
		NubeSET( 46,284,2 );

		NubeSET( 202,0,1 );
		NubeSET( 78,104,1 );
		NubeSET( 2,142,1 );
		NubeSET( 254,178,1 );
		NubeSET( 182,212,1 );
		NubeSET( 32,226,1 );
		NubeSET( 118,300,1 );
		NubeSET( 262,354,1 );

		NubeSET( 18,10,0 );
		NubeSET( 228,56,0 );
		NubeSET( 58,148,0 );
		NubeSET( 172,254,0 );
		NubeSET( 52,346,0 );


		RocaSET( 30,88,0,  0);
		RocaSET( 100,102,1,  0);
		RocaSET( 0,174,2,  0);
		RocaSET( 208,164,2,  0);
		RocaSET( 162,196,1,  0);
		RocaSET( 92,224,3,  0);
		RocaSET( 46,238,2,  0);
		RocaSET( 0,298,2,  0);
		RocaSET( 46,360,2,  0);
		RocaSET( 112,374,6,  0);
		RocaSET( 208,372,2,  0);
		RocaSET( 168,422,2,  0);
		RocaSET( 62,512,0,  0);
		RocaSET( 0,558,2,  0);
		RocaSET( 168,544,2,  0);
		RocaSET( 168,620,2,  0);
		RocaSET( 14,640,2,  0);
		RocaSET( 62,726,4,  80);
		RocaSET( 200,732,2,  0);
		RocaSET( 124,804,0,  0);
		RocaSET( 44,856,2,  0);
		RocaSET( 108,918,2,  0);
		RocaSET( 162,972,6,  0);
		RocaSET( 100,992,6,  0);
		RocaSET( 176,1076,2,  0);
		RocaSET( 98,1116,2,  0);
		RocaSET( 162,1168,6,  0);
		RocaSET( 98,1222,6,  0);
		RocaSET( 0,1278,3,  0);
		RocaSET( 62,1352,2,  0);
		RocaSET( 12,1426,2,  0);
		RocaSET( 154,1424,3,  0);
		int RocaAnim3 = RocaSET( 62,1488,0,  0);
		ItemSET( 0, 0, 9, RocaAnim3);	// Ruedas Alpinas

		MonitorSET(0);
	break;

	case 2:
		NubeSET(   0,112+40, 2);
		NubeSET(  46,104+40, 2);
		NubeSET(  88,116+40, 2);
		NubeSET( 228,103+40, 2);
		NubeSET( 232, 40+40, 2);
		NubeSET( 326, 56+40, 2);

		NubeSET(   0, 90+40, 1);
		NubeSET(  94, 48+40, 1);
		NubeSET(  84,100+40, 1);
		NubeSET( 208, 74+40, 1);
		NubeSET( 290,106+40, 1);

		NubeSET(   0, 38+40, 0);
		NubeSET( 116, 64+40, 0);
		NubeSET( 230,  4+40, 0);

		switch (FaseStage)
		{
		case 1:

			switch (JugarStage)
			{
			case 0:
				ProtSET( 17*8, -8, 0 );
				ProtSET_Vida();
			break;

			case 1:
				ProtSET(426*8, -8, 0 );
				BolaSET(0);
			break;

			case 2:
				ProtSET(621*8, -8, 0 );
				BolaSET(0);
			break;
			}

			JordiSET(  0,  0,     16, 72,    0*8, 12*8);
			JordiSET( 16,  0,    248, 72,   32*8, 12*8);
			JordiSET(  0, 72,    160, 56,  117*8, 14*8);

			JordiSET(  0,128,    304, 80,  317*8, 11*8);
			JordiSET(  0,208,    288, 80,  424*8, 11*8);

			JordiSET(  0,288,    248, 56,  524*8, 14*8);
			JordiSET(160, 72,     80, 56,  563*8, 14*8);
			JordiSET(160, 72,     80, 56,  581*8, 14*8);
			JordiSET(160, 72,     80, 56,  599*8, 14*8);
			JordiSET(  0,344,    264, 56,  618*8, 14*8);


			int RocaAnim=-1;
			RocaSET(  32+16, 68+16, 0,  0);
			RocaSET( 102+16, 22+16, 3,  0);
			RocaSET( 104+16,120+16, 2,  0);
			RocaAnim = RocaSET( 464, 62, 2,  0);
			ItemSET( 462+8,  50-16, 2, RocaAnim);	// Bola Option
			RocaSET( 534, 42, 0,  0);
			RocaSET( 626, 70, 1,  0);
			RocaSET( 700, 96, 2,  0);

			RocaSET( 758,120, 0,  0);
			RocaSET( 858,132, 2,  0);
			RocaAnim = RocaSET(1262, 44, 0,  0);
			ItemSET(1276+8,  44-16, 7, RocaAnim);	// Vida Extra
			RocaSET(1344, 68, 1,  0);
			RocaSET(1406,210, 4,128);
			RocaAnim = RocaSET(1712, 94, 3,  0);
			ItemSET(1726+8,  92-16, 8, RocaAnim);	// Full Energy
			RocaSET(1792,132, 2,  0);
			RocaSET(1848,164, 6,  0);

			RocaSET( (355*8)+ 48, 112, 1,  0);
			RocaSET( (355*8)+128, 198, 4,  18*8);
			RocaSET( (355*8)+192, 128, 0,  0);
			RocaSET( (355*8)+200,  64, 6,  0);
			RocaSET( (355*8)+288,  78, 6,  0);
			RocaSET( (355*8)+350,  56, 6,  0);
			RocaSET( (355*8)+414,  56, 2,  0);
			RocaSET( (355*8)+464,  56+80, 4,  80);

			RocaSET(  5872, 162, 2,  0);
			RocaSET(  5918, 140, 2,  0);
			RocaSET(  5876, 110, 2,  0);
			RocaSET(  5924,  88, 2,  0);


			NidoSET( 64*8,18*8, 1, 8);
			NidoSET( 97*8,23*8, 1, 8);
			NidoSET(131*8,13*8, 1, 4);
			NidoSET(185*8,27*8, 1, 8);

			NidoSET(290*8,24*8, 1, 8);

			NidoSET(682*8,26*8, 1,12);


			Nave1SET(100*8, 8*8, 1);
			Nave1SET(100*8,12*8, 2);
			Nave1SET(190*8,16*8, 3);
			Nave1SET(170*8,20*8, 1);
			Nave1SET(100*8,24*8, 3);

			Nave1SET(250*8,10*8, 2);
			Nave1SET(350*8,14*8, 3);
			Nave1SET(250*8,18*8, 1);
			Nave1SET(250*8,22*8, 3);
			Nave1SET(550*8,26*8, 2);
		break;

		case 2:
			ProtSET(208, 160, 0 );
			BolaSET(0);

			NidoSET( 64*8,23*8, 1, 2);
			NidoSET(142*8,19*8, 1, 6);
			NidoSET(208*8,18*8, 1, 6);

			int RocaAnim2 = RocaSET(252*8, 16*8, 2,  0);
			ItemSET(253*8,  19*8, 8, RocaAnim2);	// Full Energy
		break;
		}
	break;

	case 3:
		BolaSET(0);

		switch (FaseStage)
		{
		case 1:
			ProtSET(10*8,  -16, 0);
			CierraEntrada=64;
			CierraEntradaDir=8;
			
			NidoSET(20*8,91*8, 1, 3);
		break;	

		case 2:
			NidoSET((27*8)+4,3*8, 0, 3);
			NidoSET( 7*8,3*8, 1, 3);
		break;

		case 3:
			Nave1SET(60,4*8, 2);
			Nave1SET(30,6*8, 3);
			Nave1SET(50,8*8, 1);

			EnergySET( GusanoVida * Cuca_C );
		break;

		case 4:
			Nave1SET(60,202*8, 2);
			Nave1SET(30,152*8, 3);
			Nave1SET(50,149*8, 1);
			Nave1SET(50,136*8, 2);
			Nave1SET(50,128*8, 3);
			Nave1SET(50,116*8, 4);
			Nave1SET(50, 39*8, 2);
			Nave1SET(50, 34*8, 3);
			Nave1SET(50, 29*8, 4);

			NidoSET(15*8,204*8, 1, 3);
			NidoSET( 2*8,172*8, 0, 3);
			NidoSET(28*8,172*8, 1, 3);
			NidoSET( 2*8, 46*8, 1, 3);
			NidoSET(14*8,  8*8, 0, 3);
		break;
		}
	break;
	}

}


// ---------------
// Fase RUN Level
// ===============

public void FaseRUN_Level()
{
	switch (FaseLevel)
	{
	case 1:
		if (ScrollSuperY>>1 < gc.ScrollY+gc.ScrollSizeY-80) {ScrollSuperY+=2;}
		else
		if (ProtArmaData[9][0]==1) {FaseExit=1;}
	break;

	case 2:
		switch (FaseStage)
		{
		case 1:
			if (ProtX >= 5920-16 && GuardianFlag==0) {GuardianSET();}
			if (ProtX > 52*8 && ProtX < 54*8) {MonitorSET(1);}	// Consigue la bola para...

			if (ProtX > 423*8 && ProtX < 426*8) {JugarStage=1;}
			if (ProtX > 618*8 && ProtX < 621*8) {JugarStage=2;}
		break;

		case 2:
			if (ProtX >= 230*8 && NostFlag==0) {NostromoSET();}
		break;
		}
	break;

	case 3:
		switch (FaseStage)
		{
		case 1:
			if (CierraEntrada>0 && --CierraEntrada==0) {AnimTileSET(-1, CierraEntradaDir, 13,0, 8,1, 1, 0, 0x00);};
		break;

		case 3:
			CucaRUN();
		break;
		}
	break;
	}
}

// <=- <=- <=- <=- <=-




// -------------------
// Enemy Killed
// ===================

public int EnemyKilled(int X, int Y, int SizeX, int SizeY)
{
	int Power=0;

	SizeX+=X;
	SizeY+=Y;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Disparo Protagonista
// --------------------
	for (int i=0 ; i<DisparoC ; i++)
	{
		int Anim = DisparoP[i];
		if (( Disparos[Anim].CoorX    > SizeX )
		||	( Disparos[Anim].CoorX+16 < X     )
		||	( Disparos[Anim].CoorY    > SizeY )
		||	( Disparos[Anim].CoorY+16 < Y     ))
		{} else {
		DestelloSET(Disparos[Anim].CoorX, Disparos[Anim].CoorY);
		DisparoRES_Pos(i);
		Power+=3;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Disparo Arma Verde
// ------------------
	for (int i=0 ; i<ArmaVerdeC ; i++)
	{
		int Anim = ArmaVerdeP[i];
		if (( ArmaVerdes[Anim].CoorX+4  > SizeX )
		||	( ArmaVerdes[Anim].CoorX+12 < X     )
		||	( ArmaVerdes[Anim].CoorY+4  > SizeY )
		||	( ArmaVerdes[Anim].CoorY+12 < Y     ))
		{} else {
		DestelloSET(ArmaVerdes[Anim].CoorX, ArmaVerdes[Anim].CoorY);
		ArmaVerdeRES_Pos(i);
		Power+=5;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Disparo Arma Lila
// -----------------
	for (int i=0 ; i<ArmaLilaC ; i++)
	{
		int Anim = ArmaLilaP[i];
		if (( ArmaLilas[Anim].CoorX+3  > SizeX )
		||	( ArmaLilas[Anim].CoorX+13 < X     )
		||	( ArmaLilas[Anim].CoorY+3  > SizeY )
		||	( ArmaLilas[Anim].CoorY+13 < Y     ))
		{} else {
		DestelloSET(ArmaLilas[Anim].CoorX, ArmaLilas[Anim].CoorY);
		ArmaLilaRES_Pos(i);
		Power+=((BolaPower==0)?10:20);
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Disparo Arma Azul
// ------------------
	for (int i=0 ; i<ArmaAzulC ; i++)
	{
		int Anim = ArmaAzulP[i];
		if (( ArmaAzuls[Anim].CoorX+4  > SizeX )
		||	( ArmaAzuls[Anim].CoorX+12 < X     )
		||	( ArmaAzuls[Anim].CoorY+4  > SizeY )
		||	( ArmaAzuls[Anim].CoorY+12 < Y     ))
		{} else {
		DestelloSET(ArmaAzuls[Anim].CoorX, ArmaAzuls[Anim].CoorY);
		ArmaAzulRES_Pos(i);
		Power+=((BolaPower==0)?25:50);
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Disparo Arma Misil
// ------------------
	for (int i=0 ; i<ArmaMisilC ; i++)
	{
		int Anim = ArmaMisilP[i];
		if (( ArmaMisils[Anim].CoorX+4  > SizeX )
		||	( ArmaMisils[Anim].CoorX+12 < X     )
		||	( ArmaMisils[Anim].CoorY+4  > SizeY )
		||	( ArmaMisils[Anim].CoorY+12 < Y     ))
		{} else {
		DestelloSET(ArmaMisils[Anim].CoorX, ArmaMisils[Anim].CoorY);
		ArmaMisilRES_Pos(i);
		Power+=((BolaPower==0)?2:4);
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	return Power;
}


// -------------------
// Prot Killed
// ===================

public int ProtKilled(int X, int Y, int SizeX, int SizeY)
{
	int Power=0;

	SizeX+=X;
	SizeY+=Y;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Disparo Canon
// -------------
	for (int i=0 ; i<CanonDispC ; i++)
	{
		int Anim = CanonDispP[i];
		if (( CanonDisps[Anim].CoorX+6  > SizeX )
		||	( CanonDisps[Anim].CoorX+12 < X     )
		||	( CanonDisps[Anim].CoorY+6  > SizeY )
		||	( CanonDisps[Anim].CoorY+12 < Y     ))
		{} else {
		CanonDisps[Anim].Time=0;
		Power+=15;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Nave1
// -------------
	for (int i=0 ; i<Nave1C ; i++)
	{
		int Anim = Nave1P[i];
		if (( Nave1s[Anim].CoorX+6  > SizeX )
		||	( Nave1s[Anim].CoorX+12 < X     )
		||	( Nave1s[Anim].CoorY+6  > SizeY )
		||	( Nave1s[Anim].CoorY+12 < Y     ))
		{} else {
		Nave1s[Anim].Life=0;
		Power+=5;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Gusano
// -------------
	for (int i=0 ; i<GusanoC ; i++)
	{
		int Anim = GusanoP[i];
		if (( Gusanos[Anim].CoorX+6  > SizeX )
		||	( Gusanos[Anim].CoorX+12 < X     )
		||	( Gusanos[Anim].CoorY+6  > SizeY )
		||	( Gusanos[Anim].CoorY+12 < Y     ))
		{} else {
		Power+=20;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Bomba
// -------------
	for (int i=0 ; i<BombaC ; i++)
	{
		int Anim = BombaP[i];
		if (( Bombas[Anim].CoorX+2  > SizeX )
		||	( Bombas[Anim].CoorX+12 < X     )
		||	( Bombas[Anim].CoorY    > SizeY )
		||	( Bombas[Anim].CoorY+12 < Y     ))
		{} else {
		Bombas[Anim].Mode=1;
		Power+=80;

			if (ProtMode==1)
			{
			ProtMode=4;
			ProtSide=0;
			SaltoPos=12;
			HorizPos=2;
			ProtX+=2;
			}
			else if (ProtMode==3)
			{
			ProtMode=5;
			ProtSide=1;
			SaltoPos=12;
			HorizPos=2;
			ProtX-=2;
			}
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Gerard
// -------------
	for (int i=0 ; i<GerardC ; i++)
	{
		int Anim = GerardP[i];
		if (( Gerards[Anim].CoorX+6  > SizeX )
		||	( Gerards[Anim].CoorX+12 < X     )
		||	( Gerards[Anim].CoorY+6  > SizeY )
		||	( Gerards[Anim].CoorY+12 < Y     ))
		{} else {
		Gerards[Anim].Life=0;
		Power+=15;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Julio
// -------------
	for (int i=0 ; i<JulioC ; i++)
	{
		int Anim = JulioP[i];
		if (( Julios[Anim].CoorX+6  > SizeX )
		||	( Julios[Anim].CoorX+12 < X     )
		||	( Julios[Anim].CoorY+6  > SizeY )
		||	( Julios[Anim].CoorY+12 < Y     ))
		{} else {
		Julios[Anim].Life=0;
		Power+=30;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// GuarDisp
// -------------
	for (int i=0 ; i<GuarDispC ; i++)
	{
		int Anim = GuarDispP[i];
		if (( GuarDisps[Anim].CoorX+6  > SizeX )
		||	( GuarDisps[Anim].CoorX+12 < X     )
		||	( GuarDisps[Anim].CoorY+6  > SizeY )
		||	( GuarDisps[Anim].CoorY+12 < Y     ))
		{} else {
		DestelloSET(GuarDisps[Anim].CoorX, GuarDisps[Anim].CoorY);
		GuarDispRES_Pos(i);
		Power+=15;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Canon
// -------------
	for (int i=0 ; i<CanonC ; i++)
	{
		int Anim = CanonP[i];
		if (( Canons[Anim].CoorX+2  > SizeX )
		||	( Canons[Anim].CoorX+6  < X     )
		||	( Canons[Anim].CoorY+2  > SizeY )
		||	( Canons[Anim].CoorY+14 < Y     ))
		{} else {
		Canons[Anim].Life=0;
		Power+=20;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Nostromo Armas
// --------------
	for (int i=0 ; i<NostArmaC ; i++)
	{
		int Anim = NostArmaP[i];
		if (( (NostArmas[Anim].Type!=0) && (NostArmas[Anim].Type!=1)  && (NostArmas[Anim].Type!=4)  && (NostArmas[Anim].Type!=5) )
		||	( NostArmas[Anim].CoorX+18 > SizeX )
		||	( NostArmas[Anim].CoorX+38 < X     )
		||	( NostArmas[Anim].CoorY+2  > SizeY )
		||	( NostArmas[Anim].CoorY+24 < Y     ))
		{} else {
		Power+=70;
		}
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Laser Guardian
// -------------
	if (GuardianFlag==1 && GuardianMode==36 && ProtSprY+8 < GuardianY+22+6 && ProtSprY+24 > GuardianY+22 )
	{
		Power+=50;
	}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	return Power;
}



// *******************
// -------------------
// Prot - Engine
// ===================
// *******************

int ProtX, ProtY;
int ProtSprX, ProtSprY;
int ProtFrame;
int ProtSide;
int ProtMode=4, ProtType, ProtOffset;
int ProtLife=150;
int ProtPower;
int ProtArma;
int ProtInmune=0;

int[] SaltoTab = new int[] {-6,-6,-6,-4,-4,-2,-2,0,-2,0,0,0,2,0,2,2,4,4,6,6};
int SaltoPos=0;

int[] HorizTab = new int[] {6,4,2,2,0};
int HorizPos;

int[][] ProtArmaData = new int[10][2];

int ProtAnim=-1;


// -------------------
// Prot - INI
// ===================

public void ProtINI()
{
	ProtAnim=-1;

	ProtSET_Vida();
}

// -------------------
// Prot - END
// ===================

public void ProtEND()
{
	ProtAnim=-1;
}

// -------------------
// Prot - SET
// ===================

public void ProtSET(int X, int Y, int Type)
{
	ProtX=X;
	ProtY=Y;
	ProtSprX=X;
	ProtSprY=Y;
	ProtSide=0;
	ProtMode=(Type==0?0:10);
	ProtType=0;
	ProtOffset=0;

	HorizPos=HorizTab.length-1;

	ProtAnim = AnimSpriteSET(ProtAnim, ProtX,ProtY, 0, 1, 3, 0x021);
}

public void ProtSET_Vida()
{
	ProtArma=0;
	ProtPower=0;
	BolaArma=0;
	BolaPower=0;

	for (int i=0 ; i<10 ; i++) {ProtArmaData[i][0]=0;}

	ProtLife=150;
}








// -------------------
// Prot - RUN
// ===================

public void ProtRUN()
{

	if (ProtMode!=90 && ProtY+24 > FaseSizeY*8) {if (GuardianFlag!=2) {ProtMorirSET();} else {FaseExit=1;return;}}

	if (FaseLevel==3 && FaseStage==4 && ProtY < -16) {FaseExit=1;return;}


	if (ProtX < 0) {ProtX=0;}
	if (ProtX+24 > FaseSizeX*8) {ProtX=(FaseSizeX*8)-24;}


	if (ProtMode <= 8)
	{
		if (ProtInmune==0 && (ProtLife-=ProtKilled(ProtX+8, ProtY+8, 16,16)) < 1 ) {ProtMorirSET();}

		if (FaseLevel==1 && (ProtSprY+36 < gc.ScrollY || ProtSprY+24 > gc.ScrollY+gc.ScrollSizeY)) {ProtMorirSET(); return;}

		if (KeybB1==5 && KeybB2!=5)
		{
		DisparoSET(ProtSprX,ProtSprY, ProtArma, ProtPower,  0);
		BolaDisp();
		}

		if (BolaArma!=0 && KeybB1==10 && KeybB2!=10) {BolaArmaNext();}

		ProtMoverRUN();
		ProtMoverRUN();
	}


	switch (ProtMode)
	{
	case 10:	// Aparece Protagonista (Destello Azul)
		AnimSpriteSET(ProtAnim, ProtX,ProtY, 344+(ProtSide==0?0:10), 10, 3, 0x012);

		gc.SoundSET(6,1);

		ProtSprX=ProtX;
		ProtSprY=ProtY;
		ProtMode++;
	break;

	case 11:
		if (AnimSprites[ProtAnim].Pause==0) {break;}

		AnimSpriteSET(ProtAnim, ProtX,ProtY, 344+(ProtSide==0?0:10), 2, 1, 0x007);
		AnimSprites[ProtAnim].Repetir=8;

		ProtMode++;
	break;

	case 12:
		if (AnimSprites[ProtAnim].Pause==0) {break;}

		ProtSET(ProtX,ProtY,0);
	break;


	case 20:	// Desaparece Protagonista (Destello Azul)
		AnimSpriteSET(ProtAnim, ProtX,ProtY, 344+(ProtSide==0?0:10), 2, 1, 0x007);

		AnimSprites[ProtAnim].Repetir=8;

		gc.SoundSET(6,1);

		ProtSprX=ProtX;
		ProtSprY=ProtY;
		ProtMode++;
	break;

	case 21:
		if (AnimSprites[ProtAnim].Pause==0) {break;}

		AnimSpriteSET(ProtAnim, ProtX,ProtY, 346+(ProtSide==0?0:10), 8, 3, 0x002);
		ProtMode++;
	break;

	case 22:
		if (AnimSprites[ProtAnim].Pause==0) {break;}

		AnimSprites[ProtAnim].FrameIni=367;
		AnimSprites[ProtAnim].FrameAct=0;
		FaseExit=1;
	break;


	case 90:
		ProtMorirRUN();
	break;
	}








	switch (ProtMode)
	{
	case 0:	// Pisa Suelo

		ProtOffset=(ProtX & 0x06);

		if ( CheckTile(ProtX, ProtY+24, 31, 0, -1) == 0 ) {ProtMode=4;SaltoPos=12;}

		ProtType=1;
		if ( CheckTile(ProtX+32, ProtY+16, 0x70) != 0 ) {break;}
		if ( CheckTile(ProtX+24, ProtY+24, 1) == 0 ) {ProtType=2;}
		if ( CheckTile(ProtX+ 8, ProtY+24, 1) == 0 ) {ProtType=0;}
		if ( CheckTile(ProtX+32, ProtY+16, 1) != 0 ) {ProtType=3;}
	break;

	case 1:	// Pisa Pared Izquierda

		ProtOffset=(ProtY & 0x06);

		ProtType=1;
		if ( CheckTile(ProtX, ProtY+24, 1) == 0 ) {ProtType=2;}
		if ( CheckTile(ProtX, ProtY+ 8, 1) == 0 ) {ProtType=0;}

		if ( CheckTile(ProtX+8, ProtY+32, 1) != 0 ) {ProtType=3;}

	break;

	case 2:	// Agarrado al Techo

		ProtOffset=(ProtX & 0x06);

		ProtType=1;
		if ( CheckTile(ProtX+24, ProtY, 1) == 0 ) {ProtType=2;}
		if ( CheckTile(ProtX+ 8, ProtY, 1) == 0 ) {ProtType=0;}

		if ( CheckTile(ProtX-1, ProtY+16, 1) != 0 ) {ProtType=3;}

	break;

	case 3:	// Pisa Pared Derecha

		ProtOffset=(ProtY & 0x06);

		ProtType=1;
		if ( CheckTile(ProtX+24, ProtY+24, 1) == 0 ) {ProtType=2;}
		if ( CheckTile(ProtX+24, ProtY+ 8, 1) == 0 ) {ProtType=0;}

		if ( CheckTile(ProtX+16, ProtY-1, 1) != 0 ) {ProtType=3;}

	break;

	case 4:	// Cayendo (Derecha)
	case 5:	// Cayendo (Izquierda)

		ProtOffset=(ProtY & 0x06);

		ProtType=1;
   		if ( CheckTile(ProtX+8, ProtY+32, 1) != 0 ) {ProtType=2;}	// Rueda Trasera Toca Suelo?
   		if ( CheckTile(ProtX+8, ProtY+24, 1) != 0 ) {ProtType=3;}	// Rueda Delantera Acoplandose

		if ( CheckTile(ProtX+28, ProtY+8, 1) != 0 ) {ProtType=0;ProtOffset=(ProtX & 0x06);}	// Rueda Trasera Acoplandose
	break;

	case 6:	// Rampa Derecha
	case 7:	// Rampa Izquierda

		ProtOffset=(ProtX & 0x06);

		ProtType=0;

		int Bajos = CheckTile(ProtX+16, ProtY+24, 0x30);
		if ( Bajos != 0 ) {ProtType=(Bajos>>4)-1; break;}
	break;
	}



}



// -------------------
// ProtMover - RUN
// ===================

public void ProtMoverRUN()
{
	switch (ProtMode)
	{
	case 0:	// Modo 0: Pisa Suelo

		if ((FaseLevel==3 && HostCnt<1)
		&&	(ProtSprX    < SalidaX+4)
		&&	(ProtSprX    > SalidaX-4)
		&&	(ProtSprY+16 > SalidaY-8)
		&&	(ProtSprY+16 < SalidaY+8))
		{
			if ( FaseStage==3 && Cuca_C!=-200 )
			{
			MonitorSET(6);	// Debes matar todos los gusanos
			}
			else if ( FaseStage==4 )
			{
			MonitorSET(10);	// Debes dirigirte a la Superficie
			}
			else
			{
			ProtMode=20;
			break;
			}
		}

		if (KeybY1==-1 && KeybY2!=-1)
		{
		if (KeybX1!=0) {ProtSide=(KeybX1==1?0:1);}

		ProtMode=(ProtSide==0?4:5);
		SaltoPos=0;
		HorizPos=0;
		break;
		}

		if ( KeybX1==-1 && ( CheckTile(ProtX,ProtY+16, 0x01) == 0 || CheckTile(ProtX,ProtY+16, 0x70) != 0 ) )
		{
		ProtSide=1;
		ProtX-=2;
		if (CheckTile(ProtX+16,ProtY+16, 0x30) != 0 ) {ProtY-=8;ProtMode=6;break;}
		if (CheckTile(ProtX+16,ProtY+24, 0x70) != 0 ) {ProtMode=7;break;}
		if (ProtModePareISET(-6,-2)) {break;}
		if (ProtModeSueloSET( 0, 0)) {break;}
		if (ProtModePareDSET(-6, 8)) {break;}
		}
		else if (KeybX1==1 && ( CheckTile(ProtX+24,ProtY+16, 0x01) == 0 || CheckTile(ProtX+24,ProtY+16, 0x70) != 0 ) )
		{
		ProtSide=0;
		ProtX+=2;
		if (CheckTile(ProtX+16,ProtY+24, 0x30) != 0 ) {ProtMode=6;break;}
		if (CheckTile(ProtX+16,ProtY+16, 0x70) != 0 ) {ProtY-=8;ProtMode=7;break;}
		if (CheckTile(ProtX+32,ProtY+16, 0x70) != 0 ) {break;}
		if (ProtModePareDSET( 0,-8)) {break;}
		if (ProtModeSueloSET( 0, 0)) {break;}
		if (ProtModePareISET( 8, 8)) {break;}
		}
	break;


	case 1:	// Modo 1: Pisa Pared Izquierda

		if (KeybY1==-1 && KeybY2!=-1 && (CheckTile(ProtX+8,ProtY-8, 15, 0, 0x01) == 0 ) )
		{
		ProtMode=4;
		ProtSide=0;
		SaltoPos=4;
		HorizPos=0;
		ProtX+=2;
		break;
		}
		else if (KeybY1== 1 && KeybY2!= 1)
		{
		ProtMode=4;
		ProtSide=0;
		SaltoPos=12;
		HorizPos=2;
		ProtX+=2;
		break;
		}


		if (KeybX1==-1)
		{
		ProtSide=1;
		ProtY-=2;

		if (ProtModeTechoSET( 2,-6)) {break;}
		if (ProtModePareISET( 0, 0)) {break;}
		if (ProtModeSueloSET(-10,-6)) {break;}
		}
		else if (KeybX1==1)
		{
		ProtSide=0;
		ProtY+=2;

		if (ProtModeSueloSET( 8, 0)) {break;}
		if (ProtModePareISET( 0, 0)) {break;}
		if (ProtModeTechoSET(-10, 8)) {break;}
		}
	break;


	case 2:	// Modo 2: Agarrado al Techo


		if (KeybY1==1)
		{
		if (KeybX1!=0) {ProtSide=(KeybX1==1?0:1);} else {ProtSide=(ProtSide==0?1:0);}
		ProtMode=(ProtSide==0?4:5);
		SaltoPos=12;
		HorizPos=0;
		break;
		}


		if (KeybX1==-1)
		{
		ProtSide=1;
		ProtX+=2;

		if (ProtModePareDSET( 6, 2)) {break;}
		if (ProtModeTechoSET( 0, 0)) {break;}
		if (ProtModePareISET( 8,-10)) {break;}
		}
		else if (KeybX1==1)
		{
		ProtSide=0;
		ProtX-=2;

		if (ProtModePareISET( 6, 8)) {break;}
		if (ProtModeTechoSET( 0, 0)) {break;}
		if (ProtModePareDSET(-6,-10)) {break;}
		}
	break;


	case 3:	// Modo 3: Pisa Pared Derecha

		if (KeybY1==-1 && KeybY2!=-1 && (CheckTile(ProtX+8,ProtY-8, 15, 0, 0x01) == 0 ) )
		{
		ProtMode=5;
		ProtSide=1;
		SaltoPos=4;
		HorizPos=0;
		ProtX-=2;
		break;
		}
		else if (KeybY1== 1 && KeybY2!= 1)
		{
		ProtMode=5;
		ProtSide=1;
		SaltoPos=12;
		HorizPos=2;
		ProtX-=2;
		break;
		}
	
		if (KeybX1==-1)
		{
		ProtSide=1;
		ProtY+=2;

		if (ProtModeSueloSET(-2, 6)) {break;}
		if (ProtModePareDSET( 0, 0)) {break;}
		if (ProtModeTechoSET( 8, 8)) {break;}
		}
		else if (KeybX1==1)
		{
		ProtSide=0;
		ProtY-=2;

		if (ProtModeTechoSET(-8, 0)) {break;}
		if (ProtModePareDSET( 0, 0)) {break;}
		if (ProtModeSueloSET( 8,-6)) {break;}
		}
	break;


	case 4:	// Modo 4: Caida (Derecha)

		ProtY+=SaltoTab[SaltoPos];

		if ( SaltoTab[SaltoPos]<0 && CheckTile(ProtX,ProtY, 31, 0, 0x01) != 0 ) {SaltoPos=10;}

		if (++SaltoPos == SaltoTab.length) {SaltoPos--;}

		ProtX+=HorizTab[HorizPos];
		if (++HorizPos == HorizTab.length)
		{
		HorizPos--;
		if (KeybY1==-1 || KeybX1==1) {ProtX+=2;}
		}

		int Bits4 = CheckTile(ProtX+16,ProtY+24, 0x70);
		if ((Bits4 & 0x40) != 0) {ProtMode=7;ProtY&=-8;break;}
		if ((Bits4 & 0x30) != 0) {ProtMode=6;ProtY&=-8;break;}

		if (ProtModeSueloSET(0, 0)) {break;}
		if (ProtModePareISET(8, 0)) {break;}
		if (ProtModePareDSET(0, 0)) {break;}

		if ( SaltoTab[SaltoPos]>-2 && RocaCheck() ) {ProtMode=8;break;}

	break;


	case 5:	// Modo 5: Caida (Izquierda)

		ProtY+=SaltoTab[SaltoPos];

		if ( SaltoTab[SaltoPos]<0 && CheckTile(ProtX,ProtY, 31, 0, 0x01) != 0 ) {SaltoPos=10;}

		if (++SaltoPos == SaltoTab.length) {SaltoPos--;}

		ProtX-=HorizTab[HorizPos];
		if (++HorizPos == HorizTab.length)
		{
		HorizPos--;
		if (KeybY1==-1 || KeybX1==-1) {ProtX-=2;}
		}

		int Bits5 = CheckTile(ProtX+16,ProtY+24, 0x70);
		if ((Bits5 & 0x40) != 0) {ProtMode=7;ProtY&=-8;break;}
		if ((Bits5 & 0x30) != 0) {ProtMode=6;ProtY&=-8;break;}

		if (ProtModeSueloSET(0, 0)) {break;}
		if (ProtModePareISET(8, 0)) {break;}
		if (ProtModePareDSET(0, 0)) {break;}

		if ( SaltoTab[SaltoPos]>-2 && RocaCheck() ) {ProtMode=8;break;}

	break;

	case 6:	// Modo 6: Rampa Derecha

		if (KeybY1==-1 && KeybY2!=-1)
		{
		if (KeybX1!=0) {ProtSide=(KeybX1==1?0:1);}

		ProtMode=(ProtSide==0?4:5);
		SaltoPos=0;
		HorizPos=HorizTab.length-1;
		break;
		}

		if ( KeybX1==-1 )
		{
		ProtSide=1;
		ProtX-=2;

		if (CheckTile(ProtX+16,ProtY+24, 0x70) == 0 ) {ProtY-=8;}
		if (CheckTile(ProtX+16,ProtY+24, 0x70) != 0 ) {break;}
		ProtY+=8;
//		if (ProtModePareISET(-6,-2)) {break;}
		if (ProtModeSueloSET( 0, 0)) {break;}
//		if (ProtModePareDSET(-6, 8)) {break;}
		}
		else if (KeybX1==1 )
		{
		ProtSide=0;
		ProtX+=2;
		if (CheckTile(ProtX+16,ProtY+24, 0x70) == 0 ) {ProtY+=8;}
		if (CheckTile(ProtX+16,ProtY+24, 0x70) != 0 ) {break;}
//		if (ProtModePareDSET( 0,-8)) {break;}
		if (ProtModeSueloSET( 0, 0)) {break;}
//		if (ProtModePareISET( 8, 8)) {break;}
		}
	break;

	case 7:	// Modo 7: Rampa Izquierda

		if (KeybY1==-1 && KeybY2!=-1)
		{
		if (KeybX1!=0) {ProtSide=(KeybX1==1?0:1);}

		ProtMode=(ProtSide==0?4:5);
		SaltoPos=0;
		HorizPos=HorizTab.length-1;
		break;
		}

		if ( KeybX1==-1 )
		{
		ProtSide=1;
		ProtX-=2;

		if (CheckTile(ProtX+16,ProtY+24, 0x70) == 0 ) {ProtY+=8;}
		if (CheckTile(ProtX+16,ProtY+24, 0x70) != 0 ) {break;}
//		if (ProtModePareISET(-6,-2)) {break;}
		if (ProtModeSueloSET( 0, 0)) {break;}
//		if (ProtModePareDSET(-6, 8)) {break;}

		}
		else if (KeybX1==1 )
		{
		ProtSide=0;
		ProtX+=2;

		if (CheckTile(ProtX+16,ProtY+24, 0x70) == 0 ) {ProtY-=8;}
		if (CheckTile(ProtX+16,ProtY+24, 0x70) != 0 ) {break;}
		ProtY+=8;
//		if (ProtModePareDSET( 0,-8)) {break;}
		if (ProtModeSueloSET( 0, 0)) {break;}
//		if (ProtModePareISET( 8, 8)) {break;}
		}
	break;


	case 8:	// Modo 8: Sobre Asteroide

		if (ProtModeSueloSET( 0, 0)) {break;}

		if (KeybY1==-1 && KeybY2!=-1)
		{
		if (KeybX1!=0) {ProtSide=(KeybX1==1?0:1);}

		ProtMode=(ProtSide==0?4:5);
		SaltoPos=0;
		HorizPos=HorizTab.length-1;
		break;
		}

		if ( KeybX1==-1 )
		{
		ProtSide=1;
		ProtX-=2;
		}
		else if (KeybX1==1 )
		{
		ProtSide=0;
		ProtX+=2;
		}

		if ( !RocaCheck() ) {ProtMode=(ProtSide==0?4:5);SaltoPos=12;HorizPos=0;break;}
	break;
	}

}


int ProtRocaAnim;

public boolean RocaCheck()
{
	for (int i=0 ; i<RocaC ; i++)
	{
		int Anim = RocaP[i];
		if (( Rocas[Anim].CoorX   > ProtX+16 )
		||	( Rocas[Anim].CoorX+RocaSize[Rocas[Anim].Type] < ProtX+16 )
		||	( Rocas[Anim].CoorY-4 > ProtY+24 )
		||	( Rocas[Anim].CoorY+6 < ProtY+16 ))
		{} else {
		ProtRocaAnim=Anim;

		if (Rocas[Anim].CoorX > ProtX+8 ) {ProtOffset=((Rocas[Anim].CoorX-ProtX-2) & 0x06);ProtType=0;return true;}
		if (Rocas[Anim].CoorX + RocaSize[Rocas[Anim].Type] < ProtX+24 ) {ProtOffset=((Rocas[Anim].CoorX + RocaSize[Rocas[Anim].Type] - ProtX) & 0x06);ProtType=2;return true;}

		ProtOffset=(ProtX & 0x06);
		ProtType=1;

		return true;
		}
	}
	return false;
}



// -------------------
// Prot - IMP
// ===================

byte[] ProtPosTab = new byte[] {

//			0				2				4				6

// Mode = 0  (Pisa Suelo)

	 0, 8,0x00,0,	 0, 6,0x00,0,	 0, 4,0x01,0,	 0, 2,0x01,0,	// Rueda Izquierda Cayendo
	 0, 0,0x02,0,	 0, 0,0x03,0,	 0, 0,0x04,0,	 0, 0,0x05,0,	// Ruedas al mismo Nivel
	 0, 2,0x06,0,	 0, 4,0x06,0,	 0, 6,0x07,0,	 0, 8,0x07,0,	// Rueda Derecha Cayendo
	 0,-2,0x01,0,	 0,-4,0x00,0,	 0,-6,0x18,0,	 0,-8,0x19,0,	// Rueda Izquierda Subiendo

// Mode = 1  (Pisa Pared Izquierda)

	-8, 0,0x10,0,	-6, 0,0x10,0,	-4, 0,0x11,0,	-2, 0,0x11,0,	// Rueda Izquierda Cayendo
	 0, 0,0x12,0,	 0, 0,0x13,0,	 0, 0,0x14,0,	 0, 0,0x15,0,	// Ruedas al mismo Nivel
	-2, 0,0x16,0,	-4, 0,0x16,0,	-6, 0,0x17,0,	-8, 0,0x17,0,	// Rueda Derecha Cayendo
	 0, 0,0x11,0,	 2, 0,0x10,0,	 4, 0,0x07,0,	 6, 0,0x06,0,	// Rueda Izquierda Subiendo

// Mode = 2  (Pisa Techo)

	 0,-8,0x08,0,	 0,-6,0x08,0,	 0,-4,0x09,0,	 0,-2,0x09,0,	// Rueda Izquierda Cayendo
	 0, 0,0x0A,0,	 0, 0,0x0B,0,	 0, 0,0x0C,0,	 0, 0,0x0D,0,	// Ruedas al mismo Nivel
	 0,-2,0x0E,0,	 0,-4,0x0E,0,	 0,-6,0x0F,0,	 0,-8,0x0F,0,	// Rueda Derecha Cayendo
	 0, 2,0x0E,0,	 0, 8,0x16,0,	 0, 6,0x17,0,	 0, 4,0x0F,0,	// Rueda Izquierda Subiendo

// Mode = 3  (Pisa Pared Derecha)

	 6, 0,0x18,0,	 4, 0,0x18,0,	 2, 0,0x19,0,	 0, 0,0x19,0,	// Rueda Izquierda Cayendo
	 0, 0,0x1A,0,	 0, 0,0x1B,0,	 0, 0,0x1C,0,	 0, 0,0x1D,0,	// Ruedas al mismo Nivel
	 0, 0,0x1E,0,	 2, 0,0x1E,0,	 4, 0,0x1F,0,	 6, 0,0x1F,0,	// Rueda Derecha Cayendo
	 0, 0,0x1E,0,	-6, 0,0x09,0,	-4, 0,0x08,0,	-2, 0,0x1F,0,	// Rueda Izquierda Subiendo

// Mode = 4  (Caer Derecha)

	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x18,0,	 0, 0,0x19,0,	// Rueda Trasera Acoplandose
	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,	// Moto Cayendo
	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x01,0,	 0, 2,0x02,0,	// Rueda Trasera Toca Suelo
	 0, 0,0x06,0,	-1, 0,0x06,0,	-2, 0,0x07,0,	-3, 0,0x07,0,	// Rueda Delantera Acoplandose

// Mode = 5	(Caer Izquierda)

	 0, 0,0x07,0,	 0, 0,0x07,0,	 0, 0,0x10,0,	 0, 0,0x11,0,	// Rueda Trasera Acoplandose
	 0, 0,0x07,0,	 0, 0,0x07,0,	 0, 0,0x07,0,	 0, 0,0x07,0,	// Moto Cayendo
	 0, 0,0x07,0,	 0, 0,0x07,0,	 0, 0,0x06,0,	 0, 0,0x05,0,	// Rueda Trasera Toca Suelo
	 0, 0,0x01,0,	-1, 0,0x01,0,	-2, 0,0x00,0,	-3, 0,0x00,0,	// Rueda Delantera Acoplandose

// Mode = 6  (Rampa Derecha)

	 0, 1,0x06,0,	 0, 2,0x06,0,	 0, 2,0x06,0,	 0, 3,0x06,0,
	 0, 3,0x06,0,	 0, 4,0x06,0,	 0, 4,0x06,0,	 0, 5,0x06,0,
	 0, 6,0x06,0,	 0, 6,0x06,0,	 0, 7,0x06,0,	 0, 7,0x06,0,
	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,

// Mode = 7  (Rampa Izquierda)

	 0, 3,0x01,0,	 0, 2,0x01,0,	 0, 2,0x01,0,	 0, 1,0x01,0,
	 0, 5,0x01,0,	 0, 4,0x01,0,	 0, 4,0x01,0,	 0, 3,0x01,0,
	 0, 7,0x01,0,	 0, 7,0x01,0,	 0, 6,0x01,0,	 0, 6,0x01,0,
	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,

// Mode = 8  (Sobre Roca)

	 0, 2,0x01,0,	 0, 4,0x01,0,	 0, 6,0x00,0,	 0, 8,0x00,0,
	 0, 0,0x02,0,	 0, 0,0x03,0,	 0, 0,0x04,0,	 0, 0,0x05,0,
	 0, 8,0x07,0,	 0, 6,0x07,0,	 0, 4,0x06,0,	 0, 2,0x06,0,
	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,	 0, 0,0x00,0,
};


public void ProtIMP()
{
	if (ProtMode == 8) {ProtY=Rocas[ProtRocaAnim].CoorY-22;}

	if (ProtMode < 9)
	{
	int Dir=(ProtMode*16*4)+(ProtType*16)+(ProtOffset<<1);
	ProtSprX=ProtX+ProtPosTab[Dir++];
	ProtSprY=ProtY+ProtPosTab[Dir++];
	ProtFrame=ProtPosTab[Dir]+(ProtSide*32);
	AnimSprites[ProtAnim].FrameAct=ProtFrame;
	}

	AnimSprites[ProtAnim].CoorX=ProtSprX;
	AnimSprites[ProtAnim].CoorY=ProtSprY;
}



public boolean ProtModeSueloSET(int X, int Y)
{
	if ( CheckTile(ProtX+X+16, ProtY+Y+24, 1) == 0 ) {return false;}

	ProtMode=0;
	ProtX+=X;
	ProtY+=Y;

	ProtY&=-8;

	return true;
}

public boolean ProtModePareISET(int X, int Y)
{
	if ( CheckTile(ProtX+X, ProtY+Y+16, 1) == 0 ) {return false;}
	ProtMode=1;
	ProtX+=X;
	ProtY+=Y;

	ProtX&=-8;

	return true;
}

public boolean ProtModePareDSET(int X, int Y)
{
	if ( CheckTile(ProtX+X+24, ProtY+Y+16, 1) == 0 ) {return false;}

	ProtMode=3;
//	ProtY+=(6-ProtX&0x7)+Y;
	ProtX+=X;
	ProtY+=Y;

	ProtX&=-8;

	return true;
}

public boolean ProtModeTechoSET(int X, int Y)
{
	if ( CheckTile(ProtX+X+16, ProtY+Y+7, 1) == 0 ) {return false;}

	ProtMode=2;
	ProtX+=X;
	ProtY+=Y;

	ProtY&=-8;

	return true;
}

// *******************
// -------------------
// Prot - Morir - Engine
// ===================
// *******************

int ProtMorAddX;
int ProtMorAddY;

// -------------------
// ProtMorir SET
// ===================

public void ProtMorirSET()
{
	ProtMorMode=0;
	ProtMorCont=0;

	MonitorSET(2);

	gc.SoundSET(3,1);
//	gc.VibraSET(400);

	ProtMode=90;
}


// -------------------
// ProtMorir RUN
// ===================

int ProtMorMode;
int ProtMorCont;

public void ProtMorirRUN()
{

	if (ProtMorMode < 4 && CheckTile(ProtSprX+8,ProtSprY+24, 15,1, 0x01) == 0 ) {ProtSprY+=2;}


	switch (ProtMorMode)
	{
	case 0:
		ProtMorAddX= 1;
		ProtMorAddY=-2;
		DestelloSET(ProtSprX-2, ProtSprY-2, 20,20, 3);
		ProtMorMode++;
	break;

	case 1:
		ProtMorAddX=-2;
		ProtMorAddY=-1;
		DestelloSET(ProtSprX-2, ProtSprY-2, 20,20, 3);
		ProtMorMode++;
	break;

	case 2:
		ProtMorAddX=-1;
		ProtMorAddY= 2;
		DestelloSET(ProtSprX-2, ProtSprY-2, 20,20, 3);
		ProtMorMode++;
	break;

	case 3:
		ProtMorAddX= 2;
		ProtMorAddY=-1;
		DestelloSET(ProtSprX, ProtSprY, 16,16, 3);
		if (++ProtMorCont<8) {ProtMorMode=0;} else {ProtMorMode++;}
	break;

	case 4:
		ProtMorAddX=0;
		ProtMorAddY=0;
		AnimSpriteSET(ProtAnim, ProtSprX, ProtSprY,  0x4F, 8, 2, 0x002);	// Explosion Grande

		AnimSpriteSET(-1, ProtSprX-7, ProtSprY-5,  0x44, 7, 1, 0x000);	// Explosion Peque�a
		AnimSpriteSET(-1, ProtSprX+3, ProtSprY+9,  0x44, 7, 1, 0x000);	// Explosion Peque�a
		AnimSpriteSET(-1, ProtSprX+1, ProtSprY-8,  0x44, 7, 0, 0x000);	// Explosion Peque�a

		ProtMorMode++;
	break;

	case 5:
		if (AnimSprites[ProtAnim].Pause!=-1) {break;}
		ProtMorCont=40;
		ProtMorMode++;
	case 6:
		if (--ProtMorCont > 0) {break;}
		FaseExit=2;
	break;
	}
}

// *******************************





// <=- <=- <=- <=- <=-







// ******************
// ------------------
// Bola - Engine
// ==================
// ******************

int BolaArma=0;

int BolaAnim=-1;

int BolaPower;
int BolaPowerCnt=0;

int[] BolaEjes = new int[] {
	0,-16,	16,32,	32,0,	-16,16,
	16,-16,	0,32,	32,16,	-16,0,
};


int BolaX, BolaY;
int BolaFinX, BolaFinY;
int BolaSumaXcnt, BolaSumaYcnt;
int[] BolaSumaX = new int[] {0,-1,-2,-3,-2,-1,0,1,2,3,2,1};
int[] BolaSumaY = new int[] {0,-1,0,1};

int BolaAzulTime;

// ------------------
// Bola INI
// ==================

public void BolaINI()
{
	BolaAnim=-1;

	BolaArma=0;
	BolaAzulTime=0;
}


// ------------------
// Bola END
// ==================

public void BolaEND()
{
	BolaAnim=-1;
}


// ------------------
// Bola SET
// ==================

public void BolaSET(int Arma)
{
	BolaArma=Arma;

	if (Arma==0 && !JugarBola) {return;}

	if (Arma==0) {BolaArma=4; if (ProtArmaData[7-4][0]==0) {ProtArmaData[7-4][0]=1;} }
	else
	{
	if (ProtArmaData[7-BolaArma][0] < ProtArmaData[7-BolaArma][1]) {ProtArmaData[7-BolaArma][0]++;}
	}

	BolaPower=ProtArmaData[7-BolaArma][0]-1;

	ProtArmaData[2][0]=1;

	JugarBola=true;

	if (BolaAnim==-1)
	{
	BolaX=ProtX+8;
	BolaY=ProtY+8;
	BolaAnim = AnimSpriteSET(-1, BolaX,BolaY, 8, 10, 3, 0x101);
	if (BolaAnim==-1) {BolaArma=0;}
	}
}


// ------------------
// Bola RUN
// ==================

public void BolaRUN()
{
	if (BolaAzulTime>0) {BolaAzulTime--;}

	BolaFinX=ProtSprX+BolaEjes[((ProtFrame/8)*2)  ];
	BolaFinY=ProtSprY+BolaEjes[((ProtFrame/8)*2)+1];


	if (BolaX != BolaFinX) {if (BolaX < BolaFinX) {BolaX+=((BolaFinX-BolaX)/32)+1;} else {BolaX-=((BolaX-BolaFinX)/32)+1;}}
	if (BolaY != BolaFinY) {if (BolaY < BolaFinY) {BolaY+=((BolaFinY-BolaY)/32)+1;} else {BolaY-=((BolaY-BolaFinY)/32)+1;}}


	AnimSprites[BolaAnim].CoorX=BolaX+BolaSumaX[BolaSumaXcnt/8];
	AnimSprites[BolaAnim].CoorY=BolaY+BolaSumaY[BolaSumaYcnt/6];
	if (++BolaSumaXcnt >= BolaSumaX.length*8) {BolaSumaXcnt=0;}
	if (++BolaSumaYcnt >= BolaSumaY.length*6) {BolaSumaYcnt=0;}
}

public void BolaDisp()
{
	switch (BolaArma)
	{
	case 1:
		ArmaMisilSET();
	break;	

	case 2:
		if (BolaAzulTime==0)
		{
		BolaAzulTime=60;
		ArmaAzulSET(-8, 0, 0);
		ArmaAzulSET( 0,-8, 1);
		ArmaAzulSET( 8, 0, 2);
		ArmaAzulSET( 0, 8, 3);
		}
	break;	

	case 3:
		ArmaLilaSET(1);
	break;	

	case 4:
		ArmaVerdeSET();
	break;	
	}
}


public void BolaArmaNext()
{
	for (int i=1 ; i<4 ; i++)
	{
	int Pos=BolaArma+i; if (Pos>4) {Pos-=4;}
	if (ProtArmaData[7-Pos][0] != 0)
		{
		BolaArma=Pos;
		BolaPower=ProtArmaData[7-Pos][0]-1;
		PanelItemSET();
		break;
		}
	}
}

// <=- <=- <=- <=- <=-







// *******************
// -------------------
// Anim Tile - Engine
// ===================
// *******************

static final int AnimSizeX=21;
static final int AnimSizeY=2;
static byte[] AnimMap = new byte[AnimSizeX * AnimSizeY];

// -------------
// Anim Tile SET
// =============

public int AnimTileSET(int a, int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode)
{
	int Sour=(CoorY*AnimSizeX)+CoorX;

		for (int t=0 ; t<SizeY ; t++ )
		{
			for (int i=0 ; i<SizeX ; i++ )
			{
				FaseMap[Dest+i] = AnimMap[Sour+i];
			}
		Sour+=AnimSizeX;
		Dest+=FaseSizeX;
		}

	return 0;
}

// <=- <=- <=- <=- <=-




// *****************************************
// -----------------------------------------
// Anim Sprite - Engine - Rev.1 (15.04.2003)
// =========================================
// *****************************************

// Mode = xxx0 = Anim Auto-Destruct
// Mode = xxx1 = Anim Loop
// Mode = xxx2 = Anim Stop-Fin
// Mode = xxx3 = Anim Stop-Inicio

// Mode = xx1x = Anim-Play hacia ATRAS
// Mode = xx2x = Anim en PAUSA

// Mode = .x.. = Bank - Source (PNGs)
// Mode = x... = Prioridad entre Sprites (0: Inferior, F:Superior)

final int AnimSpriteMAX=128;
int AnimSpriteC;
int[] AnimSpriteP;
AniSpr[] AnimSprites;

// ---------------
// Anim Sprite SET
// ===============

public int AnimSpriteSET(int i, int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	if (i<0)
	{
	if (AnimSpriteC == AnimSpriteMAX) {return(-1);}
	i=AnimSpriteP[AnimSpriteC++];
	AnimSprites[i] = new AniSpr();
	}

	AnimSprites[i].AniSprSET(CoorX, CoorY, FrameIni, Frames, Speed, Mode);

	return (i);
}

// ---------------
// Anim Sprite INI
// ===============

public void AnimSpriteINI()
{
	AnimSpriteC = 0;
	AnimSpriteP = new int[AnimSpriteMAX];
	AnimSprites = new AniSpr[AnimSpriteMAX];
	for (int i=0 ; i<AnimSpriteMAX ; i++) {AnimSpriteP[i]=i;}
}

// ---------------
// Anim Sprite RES
// ===============

public int AnimSpriteRES(int i)
{
	if (i!=-1) {AnimSprites[i].Frames=0; AnimSprites[i].Pause=0;}
	return -1;
}

// ---------------
// Anim Sprite END
// ===============

public void AnimSpriteEND()
{
	AnimSprites = null; AnimSpriteP = null; AnimSpriteC = 0;
}

// ---------------
// Anim Sprite RUN
// ===============

public void AnimSpriteRUN()
{
	for (int i=0 ; i<AnimSpriteC ; i++)
	{
	if ( AnimSprites[AnimSpriteP[i]].Play() )
		{
		int t=AnimSpriteP[i]; AnimSprites[t] = null;
		AnimSpriteP[i--] = AnimSpriteP[--AnimSpriteC]; AnimSpriteP[AnimSpriteC] = t;
		}
	}
}

// -------------------
// Anim Sprite Ordenar
// ===================

public void AnimSpriteOrdenar()
{
	int Conta=AnimSpriteC-1;

	for (int t=0 ; t<AnimSpriteC ; t++)
	{
		for (int i=0 ; i<Conta ; i++)
		{
			if (AnimSprites[AnimSpriteP[i]].Prio < AnimSprites[AnimSpriteP[i+1]].Prio)
			{
			int dato=AnimSpriteP[i+1];
			AnimSpriteP[i+1] = AnimSpriteP[i];
			AnimSpriteP[i] = dato;
			}
		}
	Conta--;
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Disparo - Engine
// ====================
// ********************

int[] Canon = new int[] {
	16, 0,  18, 0,  20, 5,  20, 5,  20, 5,  20, 5,  20,10,  22,14,
	-4, 3,  -5, 4,  -6,10,  -6,10,  -6,10,  -6,10,  -2,15,   2,17,
	17,16,  16,17,  11,20,  11,20,  11,20,  11,20,   6,20,   2,21,
	13,-4,  10,-3,   5,-4,   5,-4,   5,-4,   5,-4,   1,-3,  -1, 0,

	-5,14,  -5,11,  -4, 6,  -4, 6,  -4, 6,  -4, 6,  -2, 2,   1,-2,
	16,17,  20,15,  20,11,  20,11,  20,11,  20,11,  21, 6,  21, 3,
	 3,-2,   7,-4,  11,-3,  11,-3,  11,-3,  11,-3,  16,-3,  19, 1,
	-1,16,   1,17,   6,20,   6,20,   6,20,   6,20,  11,21,  14,21,
};

int[] Direccion = new int[] {

	 0,-8,	// (0) Arriba
	 3,-6,
	 4,-5,
	 5,-4,
	 6,-3,
	 8, 0,	// (5) Derecha
	 6, 3,
	 5, 4,
	 4, 5,
	 3, 6,
	 0, 8,	// (10) Abajo
	-3, 6,
	-4, 5,
	-5, 4,
	-6, 3,
	-8, 0,	// (15) Izquierda
	-6,-3,
	-5,-4,
	-4,-5,
	-3,-6,
};

int[] DispAdd = new int[] {
	 3, 4, 5, 5, 5, 5, 6, 7,
	17,16,15,15,15,15,14,13,
	 8, 9,10,10,10,10,11,12,
	 2, 1, 0, 0, 0, 0,19,18,

	13,14,15,15,15,15,16,17,
	 7, 6, 5, 5, 5, 5, 4, 3,
	18,19, 0, 0, 0, 0, 1, 2,
	12,11,10,10,10,10, 9, 8,
};

int[] DispRotarTab = new int[] { 3,13, 7,17, 1,11, 9,19, 4,14, 6,16, 0,10, 5,15, 2,12, 8,18};
int DispRotarPos=0;


public class Disparo extends AniSpr
{
int Mode;
int OrigX;
int OrigY;
int AddX;
int AddY;
int Arma;
int Power;
int Cont=1;
int Time;

public Disparo() {}

// ----------------
// Disparo Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		CoorX=ProtSprX+DispSumaX[(ProtFrame%64)/8];
		CoorY=ProtSprY+DispSumaY[(ProtFrame%64)/8];
		if (Pause!=-1) {break;}
		OrigX=ProtSprX;
		OrigY=ProtSprY;
		Mode++;
	case 1:
		AniSprSET(OrigX+Canon[ ProtFrame*2], OrigY+Canon[(ProtFrame*2)+1], (Arma*4)+Power, 1, 1, 0x102);
		Time=20;
		Mode++;
	break;

	case 2:
		if (Cont>0 && --Cont==0 && Power>0 ) {DisparoSET(OrigX, OrigY, Arma, Power-1, 1);}

		CoorX+=AddX;
		CoorY+=AddY;

		if (--Time < 0 ) {return (true);}

		if (CheckTile(CoorX+8, CoorY+8, 1) != 0 )
		{
			if (Arma==0 || --Power<0)
			{
			DestelloSET(CoorX,CoorY);
			return true;
			} else {
			FrameIni--;

			CoorX-=AddX;
			CoorY-=AddY;

			AddX=Direccion[ DispAdd[DispRotarTab[DispRotarPos]]*2];
			AddY=Direccion[(DispAdd[DispRotarTab[DispRotarPos]]*2)+1];
			if (++DispRotarPos == DispRotarTab.length) {DispRotarPos=0;}
			}
		}
	break;
	}
	return super.Play();
}

};


// ---------------
// Disparo SET
// ===============

int[] DispSumaX = new int[] {8,-8,0,0,  -8,8,0,0};
int[] DispSumaY = new int[] {0,0,8,-8,  0,0,-8,8};

public int DisparoSET(int CoorX, int CoorY, int Arma, int Power, int Start)
{
	if (DisparoC+Power+1 >= DisparoMAX) {return(-1);}

	int i=DisparoP[DisparoC++];

	Disparos[i] = new Disparo();

	if (Start==0)
	{
	Disparos[i].AniSprSET(ProtSprX+DispSumaX[(ProtFrame%64)/8], ProtSprY+DispSumaY[(ProtFrame%64)/8], (ProtFrame%64)+88+(Arma*64), 2*64, 0, 0x002);
	Disparos[i].Side=64;
	} else {
	Disparos[i].Pause=-1;
	}

	Disparos[i].OrigX=CoorX;
	Disparos[i].OrigY=CoorY;

	Disparos[i].AddX=Direccion[ DispAdd[ProtFrame]*2];
	Disparos[i].AddY=Direccion[(DispAdd[ProtFrame]*2)+1];

	Disparos[i].Arma=Arma;
	Disparos[i].Power=Power;

	return (i);
}


// ---------------
// Disparo INI
// ===============

final int DisparoMAX=12;
int DisparoC;
int[] DisparoP;
Disparo[] Disparos;

public void DisparoINI()
{
	DisparoC = 0;
	DisparoP = new int[DisparoMAX];
	Disparos = new Disparo[DisparoMAX];
	for (int i=0 ; i<DisparoMAX ; i++) {DisparoP[i]=i;}
}

// ---------------
// Disparo RES
// ===============

public void DisparoRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=DisparoP[i];
	Disparos[t] = null;
	DisparoP[i--] = DisparoP[--DisparoC];
	DisparoP[DisparoC] = t;
}

// ---------------
// Disparo END
// ===============

public void DisparoEND()
{
	DisparoP = null;
	Disparos = null;
	DisparoC = 0;
}

// ---------------
// Disparo RUN
// ===============

public void DisparoRUN()
{
	for (int i=0 ; i<DisparoC ; i++)
	{
	if ( Disparos[DisparoP[i]].Play() )
		{
		int t=DisparoP[i];
		Disparos[t] = null;
		DisparoP[i] = DisparoP[--DisparoC];
		DisparoP[DisparoC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-








// ********************
// --------------------
// Canon - Engine
// ====================
// ********************

final int[] CanonOrigX = new int[] {0, 2,4,6, 4, 2, 0};
final int[] CanonOrigY = new int[] {0, 2,4,8,12,14,16};

final int[] CanonGrado = new int[] {3,4,5,6, 6,5,4,3, 2,2,1,0, 0,1,2,3, 4,4,5,6,    6,6,6,6};

public class Canon extends AniSpr
{
int Grado;

int Dest;
int Side=0;
int Type;
int Time=60;
int Life=15;

public Canon() {}

// ----------------
// Canon Play
// ================

public boolean Play()
{
	if ( (Life-=EnemyKilled( CoorX+2, CoorY+3, 4,10 )) < 1 )
	{
	gc.SoundSET(4,1);
	AnimSpriteSET(-1, CoorX-12, CoorY-8,  0x44, 7, 1, 0x000);	// Explosion Peque�a
	return true;
	}

	if (--Time < 0) {Time=1;}

	if ((ProtSprX+16 > CoorX+(Side==1?0:-80))
	&&	(ProtSprX+16 < CoorX+(Side==1?80:8))
	&&	(ProtSprY+16 > CoorY-80)
	&&	(ProtSprY+16 < CoorY+80))
	{
	Grado=Trig_atan(ProtSprY-CoorY+8, ProtSprX-CoorX+8)>>4;

	FrameAct=CanonGrado[Grado];
	Pause=-1;

		if (--Time < 1)
		{
			if ((ProtSprX+16 > CoorX+(Side==1?0:-64))
			&&	(ProtSprX+16 < CoorX+(Side==1?64:8))
			&&	(ProtSprY+16 > CoorY-64)
			&&	(ProtSprY+16 < CoorY+64))
			{
			CanonDispSET( CoorX-4+(CanonOrigX[FrameAct]*Side) , CoorY+CanonOrigY[FrameAct]-8 , 0);
			Time=150;} else {Time=1;}
		}

	} else {
	Pause=0;
	}

	return super.Play();
}


};


// ---------------
// Canon SET
// ===============

public int CanonSET(int Dest, int Side)
{
	if (CanonC == CanonMAX) {return(-1);}

	int i=CanonP[CanonC++];

	Canons[i] = new Canon();

	Canons[i].AniSprSET((Dest%FaseSizeX)*8, (Dest/FaseSizeX)*8, (Side==1)?176:176+7, 7, 30, 0x105);

	Canons[i].Dest=Dest;
	Canons[i].Side=Side;

	return (i);
}


// ---------------
// Canon INI
// ===============

final int CanonMAX=48;
int CanonC;
int[] CanonP;
Canon[] Canons;

public void CanonINI()
{
	CanonC = 0;
	CanonP = new int[CanonMAX];
	Canons = new Canon[CanonMAX];
	for (int i=0 ; i<CanonMAX ; i++) {CanonP[i]=i;}
}

// ---------------
// Canon END
// ===============

public void CanonEND()
{
	CanonP = null;
	Canons = null;
	CanonC = 0;
}

// ---------------
// Canon RUN
// ===============

public void CanonRUN()
{
	for (int i=0 ; i<CanonC ; i++)
	{
	if ( Canons[CanonP[i]].Play() )
		{
		int t=CanonP[i];
		Canons[t] = null;
		CanonP[i] = CanonP[--CanonC];
		CanonP[CanonC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// CanonDisp - Engine
// ====================
// ********************

public class CanonDisp extends AniSpr
{
int OrigX;
int OrigY;
int DespX;
int DespY;
int Total=1;
int Cont=0;
int Time=40;
int Mode=0;

// ----------------
// CanonDisp Play
// ================

public boolean Play()
{
	if ( --Time < 0 || CheckTile(CoorX+8, CoorY+8, 1) != 0 ) {DestelloSET(CoorX, CoorY); return true;}

	switch (Mode)
	{
	case 0:
		CoorX=OrigX+( ((Cont*DespX)/Total) );
		CoorY=OrigY+( ((Cont*DespY)/Total) );
		Cont+=4;
	break;

	case 1:
		CoorX+=DespX;
		CoorY+=DespY;
	break;
	}

	return super.Play();
}

};


// ---------------
// CanonDisp SET
// ===============

public int CanonDispSET(int CoorX, int CoorY, int Type)
{
	if (CanonDispC == CanonDispMAX) {return(-1);}

	int i=CanonDispP[CanonDispC++];

	CanonDisps[i] = new CanonDisp();

	if (Type==0)
	{
	CanonDisps[i].AniSprSET(CoorX,CoorY, 164, 4, 1, 0x101);
	} else {
	CanonDisps[i].AniSprSET(CoorX,CoorY, 168+Type-1, 1, 1, 0x121);
	}

	int DespX=ProtSprX-CoorX+8;
	int DespY=ProtSprY-CoorY+8;

	if (DespX==0 && DespY==0) {return -1;}

	int TotalX=(DespX>0?DespX:DespX*-1);
	int TotalY=(DespY>0?DespY:DespY*-1);

	int Total=(TotalX<TotalY?TotalY:TotalX); if (Total==0) {Total=1;}

	CanonDisps[i].Total=Total;

	CanonDisps[i].OrigX=CoorX;
	CanonDisps[i].OrigY=CoorY;

	CanonDisps[i].DespX=DespX;
	CanonDisps[i].DespY=DespY;

	return (i);
}


public int CanonDispSET(int CoorX, int CoorY, int AddX, int AddY)
{
	int i=CanonDispSET(CoorX, CoorY, 3);

	if (i!=-1)
	{
	CanonDisps[i].DespX=AddX;
	CanonDisps[i].DespY=AddY;
	CanonDisps[i].Mode=1;
	}

	return (i);
}


// ---------------
// CanonDisp INI
// ===============

final int CanonDispMAX=64;
int CanonDispC;
int[] CanonDispP;
CanonDisp[] CanonDisps;

public void CanonDispINI()
{
	CanonDispC = 0;
	CanonDispP = new int[CanonDispMAX];
	CanonDisps = new CanonDisp[CanonDispMAX];
	for (int i=0 ; i<CanonDispMAX ; i++) {CanonDispP[i]=i;}
}

// ---------------
// CanonDisp END
// ===============

public void CanonDispEND()
{
	CanonDispP = null;
	CanonDisps = null;
	CanonDispC = 0;
}

// ---------------
// CanonDisp RUN
// ===============

public void CanonDispRUN()
{
	for (int i=0 ; i<CanonDispC ; i++)
	{
	if ( CanonDisps[CanonDispP[i]].Play() )
		{
		int t=CanonDispP[i];
		CanonDisps[t] = null;
		CanonDispP[i] = CanonDispP[--CanonDispC];
		CanonDispP[CanonDispC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Host - Engine
// ====================
// ********************

public class Host extends AniSpr
{
int Dest;
int Mode=0;
int Time;

// ----------------
// Host Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		if (( CoorX+8  > ProtX+24 )
		||	( CoorX+16 < ProtX+8  )
		||	( CoorY+24 > ProtY+24 )
		||	( CoorY+32 < ProtY+8  ))
		{} else {

		MonitorSET(3);

		if (KeybY==1)
			{
			gc.SoundSET(5,1);
			Pause=0;
			Time=0;
			Mode++;
			}
		}
	break;

	case 1:
		if (++Time < 60 ) {break;}

		AniSprSET(CoorX, CoorY, 30, 9, 2, 0x000);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)

		if (--HostCnt==0)
			{
			AnimTileSET(-1, SalidaDir, 9,1,  4,1, 1, 0, 0);
			if (FaseLevel==3 && FaseStage!=4) {MonitorSET(4);}
			if (FaseLevel==3 && FaseStage==4) {MonitorSET(5); AnimTileSET(-1, 12, 5,0, 8,1, 1, 0, 0x00);}
			}
		Mode++;
	break;
	}

	return super.Play();
}

};


// ---------------
// Host SET
// ===============

public int HostSET(int Dest)
{
	if (HostC == HostMAX) {return(-1);}

	int i=HostP[HostC++];

	Hosts[i] = new Host();

	Hosts[i].AniSprSET((Dest%FaseSizeX)*8, (Dest/FaseSizeX)*8,  30, 2, 2, 0x021);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)

	Hosts[i].Dest=Dest;

	return (i);
}


// ---------------
// Host INI
// ===============

final int HostMAX=64;
int HostC;
int[] HostP;
Host[] Hosts;

public void HostINI()
{
	HostC = 0;
	HostP = new int[HostMAX];
	Hosts = new Host[HostMAX];
	for (int i=0 ; i<HostMAX ; i++) {HostP[i]=i;}
}


// ---------------
// Host END
// ===============

public void HostEND()
{
	HostP = null;
	Hosts = null;
	HostC = 0;
}

// ---------------
// Host RUN
// ===============

public void HostRUN()
{
	for (int i=0 ; i<HostC ; i++)
	{
	if ( Hosts[HostP[i]].Play() )
		{
		int t=HostP[i];
		Hosts[t] = null;
		HostP[i] = HostP[--HostC];
		HostP[HostC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// ArmaVerde - Engine
// ====================
// ********************

public class ArmaVerde extends AniSpr
{
int Time=20;
int AddX;
int AddY;

public ArmaVerde() {}

// ----------------
// ArmaVerde Play
// ================

public boolean Play()
{
	if (--Time < 0 || CheckTile(CoorX+8, CoorY+8, 1) != 0 ) {DestelloSET(CoorX,CoorY);return (true);}

	CoorX+=AddX;
	CoorY+=AddY;

	return super.Play();
}

};


// ---------------
// ArmaVerde SET
// ===============

public void ArmaVerdeSET()
{

	if (BolaPower==0)
	{
		if (BolaPowerCnt==0)
		{
		ArmaVerdeSET(89,  0,-8);
		ArmaVerdeSET(93,  0, 8);
		}

		if (BolaPowerCnt==1)
		{
		ArmaVerdeSET(90,  8,-8);
		ArmaVerdeSET(94, -8, 8);
		}


		if (BolaPowerCnt==2)
		{
		ArmaVerdeSET(91,  8, 0);
		ArmaVerdeSET(95, -8, 0);
		}


		if (BolaPowerCnt==3)
		{
		ArmaVerdeSET(88, -8,-8);
		ArmaVerdeSET(92,  8, 8);
		}
	}
	else if (BolaPower==1)
	{
		if ((BolaPowerCnt&1)==0)
		{
		ArmaVerdeSET(89,  0,-8);
		ArmaVerdeSET(93,  0, 8);
		ArmaVerdeSET(91,  8, 0);
		ArmaVerdeSET(95, -8, 0);
		}

		if ((BolaPowerCnt&1)==1)
		{
		ArmaVerdeSET(90,  8,-8);
		ArmaVerdeSET(94, -8, 8);
		ArmaVerdeSET(88, -8,-8);
		ArmaVerdeSET(92,  8, 8);
		}
	}
	else if (BolaPower>=2)
	{
		ArmaVerdeSET(89,  0,-8);
		ArmaVerdeSET(93,  0, 8);
		ArmaVerdeSET(91,  8, 0);
		ArmaVerdeSET(95, -8, 0);
		ArmaVerdeSET(90,  8,-8);
		ArmaVerdeSET(94, -8, 8);
		ArmaVerdeSET(88, -8,-8);
		ArmaVerdeSET(92,  8, 8);
	}

	if (++BolaPowerCnt >= 4) {BolaPowerCnt=0;}

}



public int ArmaVerdeSET(int FrameIni, int AddX, int AddY)
{
	if (ArmaVerdeC == ArmaVerdeMAX) {return(-1);}

	int i=ArmaVerdeP[ArmaVerdeC++];
	ArmaVerdes[i] = new ArmaVerde();

	ArmaVerdes[i].AniSprSET(AnimSprites[BolaAnim].CoorX, AnimSprites[BolaAnim].CoorY,  FrameIni, 1, 20, 0x101);

	ArmaVerdes[i].AddX=AddX;
	ArmaVerdes[i].AddY=AddY;

	return (i);
}


// ---------------
// ArmaVerde INI
// ===============

final int ArmaVerdeMAX=24;
int ArmaVerdeC;
int[] ArmaVerdeP;
ArmaVerde[] ArmaVerdes;

public void ArmaVerdeINI()
{
	ArmaVerdeC = 0;
	ArmaVerdeP = new int[ArmaVerdeMAX];
	ArmaVerdes = new ArmaVerde[ArmaVerdeMAX];
	for (int i=0 ; i<ArmaVerdeMAX ; i++) {ArmaVerdeP[i]=i;}
}

// ---------------
// ArmaVerde END
// ===============

public void ArmaVerdeEND()
{
	ArmaVerdeP = null;
	ArmaVerdes = null;
	ArmaVerdeC = 0;
}

// ---------------
// ArmaVerde RES
// ===============

public void ArmaVerdeRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=ArmaVerdeP[i];
	ArmaVerdes[t] = null;
	ArmaVerdeP[i--] = ArmaVerdeP[--ArmaVerdeC];
	ArmaVerdeP[ArmaVerdeC] = t;
}

// ---------------
// ArmaVerde RUN
// ===============

public void ArmaVerdeRUN()
{
	for (int i=0 ; i<ArmaVerdeC ; i++)
	{
	if ( ArmaVerdes[ArmaVerdeP[i]].Play() )
		{
		int t=ArmaVerdeP[i];
		ArmaVerdes[t] = null;
		ArmaVerdeP[i] = ArmaVerdeP[--ArmaVerdeC];
		ArmaVerdeP[ArmaVerdeC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-






// ********************
// --------------------
// ArmaLila - Engine
// ====================
// ********************

int ArmaLilaCnt;

public class ArmaLila extends AniSpr
{
int Contador=0;
int Desp=240*2;
int Cnt=0;

// ----------------
// ArmaLila Play
// ================

public boolean Play()
{
	CoorX = AnimSprites[BolaAnim].CoorX + Trig_cos(ArmaLilaCnt+Cnt)/(Desp>>1);
	CoorY = AnimSprites[BolaAnim].CoorY + Trig_sin(ArmaLilaCnt+Cnt)/(Desp>>1);

	if (Desp>30*2) {Desp-=2;}

	return super.Play();
}

};


// ---------------
// ArmaLila SET
// ===============

public int ArmaLilaSET(int Contador)
{
	if (ArmaLilaC == ArmaLilaMAX) {return(-1);}

	int i=ArmaLilaP[ArmaLilaC++];
	ArmaLilas[i] = new ArmaLila();

	ArmaLilas[i].AniSprSET(AnimSprites[BolaAnim].CoorX, AnimSprites[BolaAnim].CoorY,  ((BolaPower>0 && Contador!=0)?18:20), 2, 0, 0x101);

	ArmaLilas[i].Cnt=i*85;

	return (i);
}


// ---------------
// ArmaLila INI
// ===============

final int ArmaLilaMAX=3;
int ArmaLilaC;
int[] ArmaLilaP;
ArmaLila[] ArmaLilas;

public void ArmaLilaINI()
{
	ArmaLilaC = 0;
	ArmaLilaP = new int[ArmaLilaMAX];
	ArmaLilas = new ArmaLila[ArmaLilaMAX];
	for (int i=0 ; i<ArmaLilaMAX ; i++) {ArmaLilaP[i]=i;}
}


// ---------------
// ArmaLila END
// ===============

public void ArmaLilaEND()
{
	ArmaLilas = null;
	ArmaLilaP = null;
	ArmaLilaC = 0;
}

// ---------------
// ArmaLila RES
// ===============

public void ArmaLilaRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=ArmaLilaP[i];
	ArmaLilas[t] = null;
	ArmaLilaP[i--] = ArmaLilaP[--ArmaLilaC];
	ArmaLilaP[ArmaLilaC] = t;
}

// ---------------
// ArmaLila RUN
// ===============

public void ArmaLilaRUN()
{
	ArmaLilaCnt+=8; ArmaLilaCnt&=0xFF;

	for (int i=0 ; i<ArmaLilaC ; i++)
	{
	if ( ArmaLilas[ArmaLilaP[i]].Play() )
		{
		int t=ArmaLilaP[i];
		ArmaLilas[t] = null;
		ArmaLilaP[i] = ArmaLilaP[--ArmaLilaC];
		ArmaLilaP[ArmaLilaC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// ArmaAzul - Engine
// ====================
// ********************

public class ArmaAzul extends AniSpr
{
int AddX;
int AddY;
int Time=80;

public ArmaAzul() {}

// ----------------
// ArmaAzul Play
// ================

public boolean Play()
{
	if (--Time < 0 || CheckTile(CoorX+8, CoorY+8, 1) != 0 ) {DestelloSET(CoorX,CoorY);return (true);}

	CoorX+=AddX;
	CoorY+=AddY;

	return super.Play();
}

};


// ---------------
// ArmaAzul SET
// ===============

public int ArmaAzulSET(int AddX, int AddY, int FrameIni)
{
	if (ArmaAzulC == ArmaAzulMAX) {return(-1);}

	int i=ArmaAzulP[ArmaAzulC++];

	ArmaAzuls[i] = new ArmaAzul();

	ArmaAzuls[i].AniSprSET(AnimSprites[BolaAnim].CoorX, AnimSprites[BolaAnim].CoorY,  96+FrameIni+(BolaPower==0?0:4), 1, 5, 0x102);

	ArmaAzuls[i].AddX=AddX;
	ArmaAzuls[i].AddY=AddY;

	return (i);
}


// ---------------
// ArmaAzul INI
// ===============

final int ArmaAzulMAX=8;
int ArmaAzulC;
int[] ArmaAzulP;
ArmaAzul[] ArmaAzuls;

public void ArmaAzulINI()
{
	ArmaAzulC = 0;
	ArmaAzulP = new int[ArmaAzulMAX];
	ArmaAzuls = new ArmaAzul[ArmaAzulMAX];
	for (int i=0 ; i<ArmaAzulMAX ; i++) {ArmaAzulP[i]=i;}
}

// ---------------
// ArmaAzul RES
// ===============

public void ArmaAzulRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=ArmaAzulP[i];
	ArmaAzuls[t] = null;
	ArmaAzulP[i--] = ArmaAzulP[--ArmaAzulC];
	ArmaAzulP[ArmaAzulC] = t;
}

// ---------------
// ArmaAzul END
// ===============

public void ArmaAzulEND()
{
	ArmaAzulP = null;
	ArmaAzuls = null;
	ArmaAzulC = 0;
}

// ---------------
// ArmaAzul RUN
// ===============

public void ArmaAzulRUN()
{
	for (int i=0 ; i<ArmaAzulC ; i++)
	{
	if ( ArmaAzuls[ArmaAzulP[i]].Play() )
		{
		int t=ArmaAzulP[i];
		ArmaAzuls[t] = null;
		ArmaAzulP[i--] = ArmaAzulP[--ArmaAzulC];
		ArmaAzulP[ArmaAzulC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// ArmaMisil - Engine
// ====================
// ********************

int[] ArmaMisilAddX = new int[] { 4, 3, 3, 2, 2, 1, 1, 0,   0,-1,-1,-2,-2,-3,-3,-4,  -4,-3,-3,-2,-2,-1,-1, 0,    0, 1, 1, 2, 2, 3, 3, 4,   4,4};
int[] ArmaMisilAddY = new int[] { 0, 1, 1, 2, 2, 3, 3, 4,   4, 3, 3, 2, 2, 1, 1, 0,   0,-1,-1,-2,-2,-3,-3,-4,   -4,-3,-3,-2,-2,-1,-1, 0,   0,0};
int[] ArmaMisilFram = new int[] { 3,2,2,1,1,0,0,15,  15,14,14,13,13,12,12,11,  11,10,10,9,9,8,8,7,  7,6,6,5,5,4,4,3,  3,3};

Object MisilEnemy;

public class ArmaMisil extends AniSpr
{
Object Enemy;
int ang;
int Conta=0;
int DestX=30;
int DestY=30;
int AddX;
int AddY;

// ----------------
// ArmaMisil Play
// ================

public boolean Play()
{
	if (CheckTile(CoorX+8, CoorY+8, 1) != 0 ) {DestelloSET(CoorX,CoorY);return true;}

	if (Enemy!=null)
	{
		if (Enemy instanceof Game) {
		DestX=GuardianX+20;
		DestY=GuardianY+28;
		} else if (Enemy instanceof NostArma) {
		DestX=((AniSpr) Enemy).CoorX+12;
		DestY=((AniSpr) Enemy).CoorY+16;
		} else if (Enemy instanceof AniSpr) {
		DestX=((AniSpr) Enemy).CoorX;
		DestY=((AniSpr) Enemy).CoorY;
		} else if (Enemy instanceof Canon) {
		DestX=((Canon) Enemy).CoorX;
		DestY=((Canon) Enemy).CoorY;
		} else if (Enemy instanceof Nave1) {
		DestX=((Nave1) Enemy).CoorX;
		DestY=((Nave1) Enemy).CoorY;
		}
	}

	int DespX=DestX-CoorX;
	int DespY=DestY-CoorY;

	if (DespX > -4 && DespX < 4 && DespY > -4 && DespY < 4) {return true;}

	if (--Conta < 0) {ang = Trig_atan(DespY, DespX);Conta=4;}

//X -COSENO     Y-SENO               LE SUMO LA COORDENADA ACTUAL Y DIVIDO PARA QUE TARDE MAS EN GIRAR

	AddX += ArmaMisilAddX[ang>>3]*3;
	AddY += ArmaMisilAddY[ang>>3]*3;

	CoorX+=AddX >> 1;
	CoorY+=AddY >> 1;

	AddX&=0x01;
	AddY&=0x01;

	FrameIni=ArmaMisilFram[ang>>3]+72;

	return super.Play();
}

};


// ---------------
// ArmaMisil SET
// ===============

public int ArmaMisilSET()
{
	if (ArmaMisilC == ArmaMisilMAX) {return(-1);}

	int Dato=100000, Dato2;

	for (int t=0 ; t<JulioC ; t++)
	{
	int Anim=JulioP[t];
	if ((Julios[Anim].CoorX    > gc.ScrollX+gc.ScrollSizeX)
	||	(Julios[Anim].CoorX+16 < gc.ScrollX)
	||	(Julios[Anim].CoorY    > gc.ScrollY+gc.ScrollSizeY)
	||	(Julios[Anim].CoorY+16 < gc.ScrollY))
	{continue;}
	Dato2=Math.abs(Julios[Anim].CoorX-BolaX) + Math.abs(Julios[Anim].CoorY-BolaY);
	if (Dato2 < Dato) {Dato=Dato2; MisilEnemy=Julios[Anim];}
	}

	for (int t=0 ; t<GerardC ; t++)
	{
	int Anim=GerardP[t];
	if ((Gerards[Anim].CoorX    > gc.ScrollX+gc.ScrollSizeX)
	||	(Gerards[Anim].CoorX+16 < gc.ScrollX)
	||	(Gerards[Anim].CoorY    > gc.ScrollY+gc.ScrollSizeY)
	||	(Gerards[Anim].CoorY+16 < gc.ScrollY))
	{continue;}
	Dato2=Math.abs(Gerards[Anim].CoorX-BolaX) + Math.abs(Gerards[Anim].CoorY-BolaY);
	if (Dato2 < Dato) {Dato=Dato2; MisilEnemy=Gerards[Anim];}
	}

	for (int t=0 ; t<CanonC ; t++)
	{
	int Anim=CanonP[t];
	if ((Canons[Anim].CoorX    > gc.ScrollX+gc.ScrollSizeX)
	||	(Canons[Anim].CoorX+16 < gc.ScrollX)
	||	(Canons[Anim].CoorY    > gc.ScrollY+gc.ScrollSizeY)
	||	(Canons[Anim].CoorY+16 < gc.ScrollY))
	{continue;}
	Dato2=Math.abs(Canons[Anim].CoorX-BolaX) + Math.abs(Canons[Anim].CoorY-BolaY);
	if (Dato2 < Dato) {Dato=Dato2; MisilEnemy=Canons[Anim];}
	}

	for (int t=0 ; t<Nave1C ; t++)
	{
	int Anim=Nave1P[t];
	if ((Nave1s[Anim].CoorX    > gc.ScrollX+gc.ScrollSizeX)
	||	(Nave1s[Anim].CoorX+16 < gc.ScrollX)
	||	(Nave1s[Anim].CoorY    > gc.ScrollY+gc.ScrollSizeY)
	||	(Nave1s[Anim].CoorY+16 < gc.ScrollY))
	{continue;}
	Dato2=Math.abs(Nave1s[Anim].CoorX-BolaX) + Math.abs(Nave1s[Anim].CoorY-BolaY);
	if (Dato2 < Dato) {Dato=Dato2; MisilEnemy=Nave1s[Anim];}
	}

	for (int t=0 ; t<GusanoC ; t++)
	{
	int Anim=GusanoP[t];
	if ((Gusanos[Anim].Pos != 0)
	||	(Gusanos[Anim].CoorX    > gc.ScrollX+gc.ScrollSizeX)
	||	(Gusanos[Anim].CoorX+16 < gc.ScrollX)
	||	(Gusanos[Anim].CoorY    > gc.ScrollY+gc.ScrollSizeY)
	||	(Gusanos[Anim].CoorY+16 < gc.ScrollY))
	{continue;}
	Dato2=Math.abs(Gusanos[Anim].CoorX-BolaX) + Math.abs(Gusanos[Anim].CoorY-BolaY);
	if (Dato2 < Dato) {Dato=Dato2; MisilEnemy=Gusanos[Anim];}
	}

	for (int t=0 ; t<NostArmaC ; t++)
	{
	int Anim=NostArmaP[t];
	if ((NostArmas[Anim].Pos == 0) || (NostArmas[Anim].SalY < 12)
	||	(NostArmas[Anim].CoorX    > gc.ScrollX+gc.ScrollSizeX)
	||	(NostArmas[Anim].CoorX+16 < gc.ScrollX)
	||	(NostArmas[Anim].CoorY    > gc.ScrollY+gc.ScrollSizeY)
	||	(NostArmas[Anim].CoorY+16 < gc.ScrollY))
	{continue;}
	Dato2=Math.abs(NostArmas[Anim].CoorX-BolaX) + Math.abs(NostArmas[Anim].CoorY-BolaY);
	if (Dato2 < Dato) {Dato=Dato2; MisilEnemy=NostArmas[Anim];}
	}

	if (GuardianFlag==1)
	{
	Dato2=Math.abs((GuardianX+20)-BolaX) + Math.abs((GuardianY+28)-BolaY);
	if (Dato2 < Dato) {Dato=Dato2; MisilEnemy=this;}
	}


	if (MisilEnemy==null) {return -1;}

	int i=ArmaMisilP[ArmaMisilC++];

	ArmaMisils[i] = new ArmaMisil();

	ArmaMisils[i].AniSprSET(AnimSprites[BolaAnim].CoorX, AnimSprites[BolaAnim].CoorY,  72, 1, 5, 0x102);

	ArmaMisils[i].Enemy=MisilEnemy;
	MisilEnemy=null;

	return (i);
}


// ---------------
// ArmaMisil INI
// ===============

final int ArmaMisilMAX=4;
int ArmaMisilC;
int[] ArmaMisilP;
ArmaMisil[] ArmaMisils;

public void ArmaMisilINI()
{
	ArmaMisilC = 0;
	ArmaMisilP = new int[ArmaMisilMAX];
	ArmaMisils = new ArmaMisil[ArmaMisilMAX];
	for (int i=0 ; i<ArmaMisilMAX ; i++) {ArmaMisilP[i]=i;}
}

// ---------------
// ArmaMisil RES
// ===============

public void ArmaMisilRES_Pos(int i)
{
	int t=ArmaMisilP[i];
	ArmaMisils[t] = null;
	ArmaMisilP[i--] = ArmaMisilP[--ArmaMisilC];
	ArmaMisilP[ArmaMisilC] = t;
}

// ---------------
// ArmaMisil END
// ===============

public void ArmaMisilEND()
{
	ArmaMisilP = null;
	ArmaMisils = null;
	ArmaMisilC = 0;
}

// ---------------
// ArmaMisil RUN
// ===============

public void ArmaMisilRUN()
{
	for (int i=0 ; i<ArmaMisilC ; i++)
	{
	if ( ArmaMisils[ArmaMisilP[i]].Play() )
		{
		int t=ArmaMisilP[i];
		ArmaMisils[t] = null;
		ArmaMisilP[i--] = ArmaMisilP[--ArmaMisilC];
		ArmaMisilP[ArmaMisilC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Nave1 - Engine
// ====================
// ********************

public class Nave1
{
int CoorX;
int CoorY;
int Anim1;
int Anim2;
int Speed;
int Life=10;

public Nave1() {}

// ----------------
// Nave1 Play
// ================

public boolean Play()
{
	if ( (Life-=EnemyKilled( CoorX+2, CoorY+2, 12,12 )) < 1)
	{
	gc.SoundSET(4,1);
	AnimSpriteSET(Anim1, CoorX-8, CoorY-8,  0x44, 7, 1, 0x000);	// Explosion Peque�a
	AnimSpriteRES(Anim2);
	return true;
	}


	AnimSprites[Anim1].CoorX=CoorX;
	AnimSprites[Anim2].CoorX=CoorX+16;

	CoorX-=Speed; if (CoorX < -32) {CoorX=FaseSizeX*8;}

	return (false);
}

};


// ---------------
// Nave1 SET
// ===============

public int Nave1SET(int CoorX, int CoorY, int Speed)
{
	if (Nave1C == Nave1MAX) {return(-1);}


	int Anim1 = AnimSpriteSET(-1, CoorX, CoorY, 104, 14, 3, 0x101);
	if (Anim1==-1) {return -1;}
	int Anim2 = AnimSpriteSET(-1, CoorX+16, CoorY, 118, 2, 2, 0x101);
	if (Anim2==-1) {AnimSpriteRES(Anim1); return -1;}


	int i=Nave1P[Nave1C++];
	Nave1s[i] = new Nave1();

	Nave1s[i].Anim1=Anim1;
	Nave1s[i].Anim2=Anim2;
	Nave1s[i].CoorX=CoorX;
	Nave1s[i].CoorY=CoorY;
	Nave1s[i].Speed=Speed;

	return (i);
}


// ---------------
// Nave1 INI
// ===============

final int Nave1MAX=12;
int Nave1C;
int[] Nave1P;
Nave1[] Nave1s;

public void Nave1INI()
{
	Nave1C = 0;
	Nave1P = new int[Nave1MAX];
	Nave1s = new Nave1[Nave1MAX];
	for (int i=0 ; i<Nave1MAX ; i++) {Nave1P[i]=i;}
}


// ---------------
// Nave1 END
// ===============

public void Nave1END()
{
	Nave1P = null;
	Nave1s = null;
	Nave1C = 0;
}

// ---------------
// Nave1 RUN
// ===============

public void Nave1RUN()
{
	for (int i=0 ; i<Nave1C ; i++)
	{
	if ( Nave1s[Nave1P[i]].Play() )
		{
		int t=Nave1P[i];
		Nave1s[t] = null;
		Nave1P[i] = Nave1P[--Nave1C];
		Nave1P[Nave1C] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Gusano - Engine
// ====================
// ********************

final int GusanoVida=70;

final int[] GusanoFra = new int[] {30,29,28,27,26,25,24,31};
final int[] GusanoMov = new int[] {0,-1,  1,-1,  1,0,  1,1,  0,1,  -1,1,  -1,0,  -1,-1,};
final int[] GusanoCos = new int[] {0, 0,0, 1,1, 2,2, 3,3, 4,4, 5, 6};

int[] GusanoSalida = new int[] {27,24,  27,8,  27,24};

final int[][] GusanoRuta = {
	{11,2, 8,1, 10,2, 2,4, 24,2, 5,3, 4,2, 11,1, 9,0, 3,7, 9,0, 6,1, 14,0, 4,7, 19,6, 8,5, 29,6, 4,7, 16,0, 8,7, 5,0, 5,7, 15,0, 11,1, 49,2, 13,1},
	{31,4, 7,5, 14,4, 8,3, 14,2, 9,3, 35,2, 10,1, 7,0, 6,7, 10,0, 8,1, 20,0, 14,7, 38,6, 6,7, 9,0, 6,1, 12,2, 9,3, 17,4, 4,5, 21,6, 12,7, 17,0, 11,1, 25,2, 13,3, 28,4, 14,5, 24,6, 8,7, 15,6, 6,5, 13,6, 6,7, 10,0, 11,1, 11,0, 8,7, 26,6, 9,5, 11,4, 9,3, 12,4, 16,5, 26,4, 9,3, 52,2, 14,3, 21,4, 8,5, 13,4, 19,3, 8,2, 8,3, 31,2, 13,1, 24,0, 7,7, 18,0, 6,1, 11,2, 7,3, 19,2, 13,1, 8,2, 15,3, 25,4, 20,3, 35,2, 18,1, 27,0, 11,1, 22,0, 5,1, 19,0, 20,7, 24,6, 4,5, 28,6, 13,7},
	{4,3, 23,2, 6,3, 22,2, 6,1, 20,0, 14,1, 11,0, 5,7, 10,6, 9,7, 15,6, 8,5, 13,6, 6,7, 13,7, 21,0, 17,1, 14,0, 6,1, 19,2, 13,1, 31,2, 5,3, 19,4, 12,5, 28,4, 5,5, 9,6, 6,5, 14,4, 7,3, 19,2, 10,1, 33,2, 18,3, 14,4, 16,5, 20,4, 8,3, 26,2, 9,1, 29,2, 9,3, 24,2, 6,1, 11,0, 7,7, 7,0, 5,1, 34,0, 18,7}
};

int[] GusCaidaTab = new int[] {0,2,0,1,0,3,1,0,2,1,0,3};
int GusCaidaPos=0;


public class Gusano extends AniSpr
{
int Anim;
int OrigX;
int OrigY;
int Ruta=-1;
int RutaCnt=0;
int NextCnt=7;
int Pos;
int Type=0;
int Next=-1;
int Die=-1;
int Mode=0;
int Life=GusanoVida;

public Gusano() {}

// ----------------
// Gusano Play
// ================

public boolean Play()
{
	if (Die > 0 && --Die==0)
	{
	AnimSpriteSET(-1, CoorX-8, CoorY-8,  0x44, 7, 1, 0x000);	// Explosion Peque�a
	if (Next!=-1) {Gusanos[Next].Die=3;}
	return true;
	}

	if (Pos==0 && (Life-=EnemyKilled(CoorX+2, CoorY+2, 12,12 )) < 1 )
	{
	gc.SoundSET(4,1);
	AnimSpriteSET(-1, CoorX-8, CoorY-8,  0x44, 7, 1, 0x000);	// Explosion Peque�a
	Mode=1; int Next2=Next; while (Next2!=-1) {Gusanos[Next2].Mode=1; Next2=Gusanos[Next2].Next;}
	if (Next!=-1) {Gusanos[Next].Die=3;}
	return true;
	}

	switch (Mode)
	{
	case 0:

		if (--RutaCnt < 1)
			{
				if (++Ruta == GusanoRuta[Type].length)
				{
				if (Pos==0) {Cuca_C++;}
				return true;
				}
			RutaCnt=GusanoRuta[Type][Ruta++];
			}

		if (Pos < GusanoCos.length-1 && NextCnt > 0 && --NextCnt==0 ) {Next=GusanoSET(OrigX,OrigY, Type, Pos+1);}

		CoorX+=GusanoMov[ GusanoRuta[Type][Ruta]*2];
		CoorY+=GusanoMov[(GusanoRuta[Type][Ruta]*2)+1];

		if (Pos==0) {FrameIni=GusanoFra[GusanoRuta[Type][Ruta]]+Type*16;}
	break;

	case 1:
		CoorY+=GusCaidaTab[GusCaidaPos];
		if (++GusCaidaPos == GusCaidaTab.length) {GusCaidaPos=0;}
	break;
	}

	return (false);
}

};


// ---------------
// Gusano SET
// ===============

public int GusanoSET(int CoorX, int CoorY, int Type, int Pos)
{
	if (GusanoC == GusanoMAX) {return(-1);}

	int i=GusanoP[GusanoC++];
	Gusanos[i] = new Gusano();

	if (Pos==0)
	{
	CoorX=(GusanoSalida[ Type*2]*8)-5;
	CoorY=(GusanoSalida[(Type*2)+1]*8)-5;
	}

	Gusanos[i].AniSprSET(CoorX, CoorY, 0, 1, 20, 0x101);

	Gusanos[i].Prio=Pos-100;

	if (Pos==0)
	{Gusanos[i].FrameIni=GusanoFra[GusanoRuta[Type][1]]+Type*16;}
	else
	{Gusanos[i].FrameIni=32+GusanoCos[Pos]+Type*16;}

	Gusanos[i].OrigX=CoorX;
	Gusanos[i].OrigY=CoorY;
	Gusanos[i].Type=Type;
	Gusanos[i].Pos=Pos;

	return (i);
}


// ---------------
// Gusano INI
// ===============

final int GusanoMAX=60;
int GusanoC;
int[] GusanoP;
Gusano[] Gusanos;

public void GusanoINI()
{
	GusanoC = 0;
	GusanoP = new int[GusanoMAX];
	Gusanos = new Gusano[GusanoMAX];
	for (int i=0 ; i<GusanoMAX ; i++) {GusanoP[i]=i;}
}


// ---------------
// Gusano END
// ===============

public void GusanoEND()
{
	GusanoP = null;
	Gusanos = null;
	GusanoC = 0;
}

// ---------------
// Gusano RUN
// ===============

public void GusanoRUN()
{
	for (int i=0 ; i<GusanoC ; i++)
	{
	if ( Gusanos[GusanoP[i]].Play() )
		{
		int t=GusanoP[i];
		Gusanos[t] = null;
		GusanoP[i] = GusanoP[--GusanoC];
		GusanoP[GusanoC] = t;
		i--;
		}
	}
}

// -------------------
// Gusano Ordenar
// ===================

public void GusanoOrdenar()
{
	int Conta=GusanoC-1;

	for (int t=0 ; t<GusanoC ; t++)
	{
		for (int i=0 ; i<Conta ; i++)
		{
			if (Gusanos[GusanoP[i]].Prio < Gusanos[GusanoP[i+1]].Prio)
			{
			int dato=GusanoP[i+1];
			GusanoP[i+1] = GusanoP[i];
			GusanoP[i] = dato;
			}
		}
	Conta--;
	}
}

// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Cuca - Engine
// ====================
// ********************

int CucaType=0;
int Cuca_1=-1;
int Cuca_2=-1;
int Cuca_3=-1;
int Cuca_C;

// ---------------
// Cuca INI
// ===============

public void CucaINI()
{
	Cuca_1=-1;
	Cuca_2=-1;
	Cuca_3=-1;
	Cuca_C=4;
}


// ---------------
// Cuca RUN
// ===============

public void CucaRUN()
{
	if (Cuca_C >= 0)
	{
	EnergyAct=Cuca_C*GusanoVida;
	if (Cuca_1!=-1 && Gusanos[Cuca_1]!=null) {EnergyAct+=Gusanos[Cuca_1].Life;}
	if (Cuca_2!=-1 && Gusanos[Cuca_2]!=null) {EnergyAct+=Gusanos[Cuca_2].Life;}
//	if (Cuca_3!=-1 && Gusanos[Cuca_3]!=null) {EnergyAct+=Gusanos[Cuca_3].Life;}

		if (EnergyAct <= 0)
		{
		EnergyRES();
		Cuca_C=-200;
		}
	}


	if (Cuca_C > 0 && (Cuca_1==-1 || Cuca_1!=-1 && Gusanos[Cuca_1]==null))
	{
	Cuca_1=GusanoSET(0,0, CucaType, 0);
	if (++CucaType==3) {CucaType=0;}
	Cuca_C--;
	}

	if (Cuca_C > 0 && (Cuca_2==-1 || Cuca_2!=-1 && Gusanos[Cuca_2]==null))
	{
	Cuca_2=GusanoSET(0,0, CucaType, 0);
	if (++CucaType==3) {CucaType=0;}
	Cuca_C--;
	}
}

// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Bomba - Engine
// ====================
// ********************

public class Bomba
{
int Dest;
int CoorX;
int CoorY;
int Type;
int Mode=0;

public Bomba() {}

// ----------------
// Bomba Play
// ================

public boolean Play()
{
	if (Mode!=0)
	{
	AnimTileSET(-1, Dest, Type,0, 1,2, 1, 0, 0x00);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)
	AnimSpriteSET(-1, CoorX-12, CoorY-8,  0x4F, 7, 1, 0x000);	// Explosion Grande
	return true;
	}

	return (false);
}

};


// ---------------
// Bomba SET
// ===============

public int BombaSET(int Dest, int Type)
{
	if (BombaC == BombaMAX) {return(-1);}

	int i=BombaP[BombaC++];

	Bombas[i] = new Bomba();

	Bombas[i].Dest=Dest;
	Bombas[i].CoorX=(Dest%FaseSizeX)*8;
	Bombas[i].CoorY=(Dest/FaseSizeX)*8;
	Bombas[i].Type=Type;

	return (i);
}


// ---------------
// Bomba INI
// ===============

final int BombaMAX=64;
int BombaC;
int[] BombaP;
Bomba[] Bombas;

public void BombaINI()
{
	BombaC = 0;
	BombaP = new int[BombaMAX];
	Bombas = new Bomba[BombaMAX];
	for (int i=0 ; i<BombaMAX ; i++) {BombaP[i]=i;}
}

// ---------------
// Bomba END
// ===============

public void BombaEND()
{
	BombaP = null;
	Bombas = null;
	BombaC = 0;
}

// ---------------
// Bomba RUN
// ===============

public void BombaRUN()
{
	for (int i=0 ; i<BombaC ; i++)
	{
	if ( Bombas[BombaP[i]].Play() )
		{
		int t=BombaP[i];
		Bombas[t] = null;
		BombaP[i] = BombaP[--BombaC];
		BombaP[BombaC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Nido - Engine
// ====================
// ********************

public class Nido
{
int Max;
int CoorX;
int CoorY;
int Type;
int Cont=0;
int Id;
int Time=200;

public Nido() {}

// ----------------
// Nido Play
// ================

public boolean Play()
{
	if (Time>0 && --Time>0) {return false;}

	if ((CoorX < gc.ScrollX-8)
	||	(CoorX > gc.ScrollX+gc.ScrollSizeX+8)
	||	(CoorY < gc.ScrollY-8)
	||	(CoorY > gc.ScrollY+gc.ScrollSizeY+8))
	{
		if (Cont < Max)
		{
		Time=130;
			if (Type==0)
			{
			if (GerardSET(CoorX,CoorY, Id) != -1) {Cont++;}
			} else {
			if (JulioSET(CoorX,CoorY, Id) != -1) {Cont++;}
			}
		}
	}

	return (false);
}

};


// ---------------
// Nido SET
// ===============

public int NidoSET(int CoorX, int CoorY, int Type, int Max)
{
	if (NidoC == NidoMAX) {return(-1);}

	int i=NidoP[NidoC++];

	Nidos[i] = new Nido();

	Nidos[i].CoorX=CoorX+8;
	Nidos[i].CoorY=CoorY+8;
	Nidos[i].Type=Type;
	Nidos[i].Id=i;
	Nidos[i].Max=Max;

	Nidos[i].Time=(Type==0?250:50);

	return (i);
}


// ---------------
// Nido INI
// ===============

final int NidoMAX=12;
int NidoC;
int[] NidoP;
Nido[] Nidos;

public void NidoINI()
{
	NidoC = 0;
	NidoP = new int[NidoMAX];
	Nidos = new Nido[NidoMAX];
	for (int i=0 ; i<NidoMAX ; i++) {NidoP[i]=i;}
}

// ---------------
// Nido END
// ===============

public void NidoEND()
{
	NidoP = null;
	Nidos = null;
	NidoC = 0;
}

// ---------------
// Nido RUN
// ===============

public void NidoRUN()
{
	for (int i=0 ; i<NidoC ; i++)
	{
	if ( Nidos[NidoP[i]].Play() )
		{
		int t=NidoP[i];
		Nidos[t] = null;
		NidoP[i--] = NidoP[--NidoC];
		NidoP[NidoC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Gerard - Engine
// ====================
// ********************

public class Gerard extends AniSpr
{
int Cnt;
int Nido;
int Life=20;

public Gerard() {}

// ----------------
// Gerard Play
// ================

public boolean Play()
{
	if ( (Life-=EnemyKilled( CoorX+2, CoorY+2, 12,12 )) < 1 )
	{
	gc.SoundSET(4,1);
	Nidos[Nido].Cont--;
	AnimSpriteSET(-1, CoorX-12, CoorY-8,  0x44, 7, 1, 0x000);	// Explosion Peque�a
	ItemSET(CoorX, CoorY);
	return true;
	}

	if (CheckTile(CoorX+2,CoorY+16, 11, 1, 0x01)==0 )
	{
	CoorY++;
	} else {
	if (CheckTile(CoorX+(Side==1?18:-2),CoorY+16,1, 1, 0x01)==0 ) {Side*=-1;}
	if (CheckTile(CoorX+(Side==1?16: 0),CoorY,   1,15, 0x01)!=0 ) {Side*=-1;}
	Cnt++;Cnt&=0x3;
	CoorX+=(Cnt>>1)*Side;
	}

	return super.Play();
}

};


// ---------------
// Gerard SET
// ===============

public int GerardSET(int CoorX, int CoorY, int Nido)
{
	if (GerardC == GerardMAX) {return(-1);}

	int i=GerardP[GerardC++];

	Gerards[i] = new Gerard();

	Gerards[i].AniSprSET(CoorX,CoorY, 120, 4, 3, 0x101);

	Gerards[i].Nido=Nido;

	return (i);
}


// ---------------
// Gerard INI
// ===============

final int GerardMAX=12;
int GerardC;
int[] GerardP;
Gerard[] Gerards;

public void GerardINI()
{
	GerardC = 0;
	GerardP = new int[GerardMAX];
	Gerards = new Gerard[GerardMAX];
	for (int i=0 ; i<GerardMAX ; i++) {GerardP[i]=i;}
}

// ---------------
// Gerard END
// ===============

public void GerardEND()
{
	GerardP = null;
	Gerards = null;
	GerardC = 0;
}

// ---------------
// Gerard RUN
// ===============

public void GerardRUN()
{
	for (int i=0 ; i<GerardC ; i++)
	{
	if ( Gerards[GerardP[i]].Play() )
		{
		int t=GerardP[i];
		Gerards[t] = null;
		GerardP[i--] = GerardP[--GerardC];
		GerardP[GerardC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Julio - Engine
// ====================
// ********************

int[] JulioSumaYI = new int[] {0,0,0,1,1,1,2,2,  2,3,3,3,4,4,4,5,  5,5,6,6,6,7,7,8};
int[] JulioSumaYD = new int[] {2,2,1,1,1,0,0,0,  5,4,4,4,3,3,3,2,  8,7,7,6,6,6,5,5};

public class Julio extends AniSpr
{
int Side=1;
int Nido;
int Cnt;
int Life=15;

public Julio() {}

// ----------------
// Julio Play
// ================

public boolean Play()
{
	if ( (Life-=EnemyKilled( CoorX+2, CoorY+2, 12,12 )) < 1 )
	{
	gc.SoundSET(4,1);
	Nidos[Nido].Cont--;
	AnimSpriteSET(-1, CoorX-12, CoorY-8,  0x44, 7, 1, 0x000);	// Explosion Peque�a
	ItemSET(CoorX, CoorY);
	return true;
	}

	if (CheckTile(CoorX+2,CoorY+16, 11, 1, 0x31) == 0 )
	{
	CoorY+=2;Pause=-1;
	} else {


	Cnt++;Cnt&=0x3;
	CoorX+=(Cnt>>1)*Side;


	if (CoorX < 2 || CoorX+18 > FaseSizeX*8) {Side*=-1;}

	if (CheckTile(CoorX+(Side==1?16:-1),CoorY+16, 1,15, 0x31) == 0) {Side*=-1;}

	if (CheckTile(CoorX+(Side==1?16:-1),CoorY,    1,10, 0x31) == 1) {Side*=-1;}


	if ( CheckTile(CoorX+8,CoorY+8, 0x30) != 0 )
	{
	CoorY-=8;CoorY&=-8;
	}


	int Bit2=CheckTile(CoorX+8, CoorY+16, 0x70);
	int Bits=Bit2&0x30;
	if ( Bits != 0 )
	{
	CoorY=((Bit2&0x40)==0) ? (CoorY&=-8)+JulioSumaYI[(CoorX&0x7)+(((Bits>>4)-1)*8)] : (CoorY&=-8)+JulioSumaYD[(CoorX&0x7)+(((Bits>>4)-1)*8)];
	}


	Pause=0;
	}
	FrameIni=(Side==1?136:124);

	return super.Play();
}

};


// ---------------
// Julio SET
// ===============

public int JulioSET(int CoorX, int CoorY, int Nido)
{
	if (JulioC == JulioMAX) {return(-1);}

	int i=JulioP[JulioC++];

	Julios[i] = new Julio();

	Julios[i].AniSprSET(CoorX,CoorY, 124, 12, 1, 0x101);

	Julios[i].Nido=Nido;

	Julios[i].Side=(ProtX+16<CoorX+8)?-1:1;

	return (i);
}


// ---------------
// Julio INI
// ===============

final int JulioMAX=48;
int JulioC;
int[] JulioP;
Julio[] Julios;

public void JulioINI()
{
	JulioC = 0;
	JulioP = new int[JulioMAX];
	Julios = new Julio[JulioMAX];
	for (int i=0 ; i<JulioMAX ; i++) {JulioP[i]=i;}
}

// ---------------
// Julio END
// ===============

public void JulioEND()
{
	JulioP = null;
	Julios = null;
	JulioC = 0;
}

// ---------------
// Julio RUN
// ===============

public void JulioRUN()
{
	for (int i=0 ; i<JulioC ; i++)
	{
	if ( Julios[JulioP[i]].Play() )
		{
		int t=JulioP[i];
		Julios[t] = null;
		JulioP[i--] = JulioP[--JulioC];
		JulioP[JulioC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Item - Engine
// ====================
// ********************

int[] ItemSelTab = new int[] {0,4,6,1,4,5,1,5,6,3,5,0, 8 ,3,6,1,5,3,6,0,4,5,6,1,6,1,0,3, 8 ,0,5,0,3,4,1,4,3,4,3,6,1,5, 7, 3,6,0,4,5,6,1,6,1,0,3,};
int ItemSelPos;
int ItemCont;

public class Item extends AniSpr
{

int Type;
int Time=200;
int RocaAnim=-1;

public Item() {}

// ----------------
// Item Play
// ================

public boolean Play()
{
	if ( RocaAnim==-1 && Time>0 && --Time==0) {Pause=0;}

	if ( RocaAnim==-1 && CheckTile(CoorX+2,CoorY+16, 11,1, 0x01) == 0 ) {CoorY++;}
	else if (RocaAnim!=-1) {CoorY=Rocas[RocaAnim].CoorY-15;}

	if (( CoorX+ 2 > ProtX+24 )
	||	( CoorX+14 < ProtX+8  )
	||	( CoorY+ 2 > ProtY+24 )
	||	( CoorY+14 < ProtY+8  ))
	{} else {

		gc.SoundSET(2,1);

		PanelItemSET();

		if (Type<9) {MonitorSET(12+Type);}

		switch (Type)
		{
		case 0:
			ProtArma=0;
			if (ProtPower<3) {ProtPower++;}
		break;

		case 1:
			ProtArma=1;
			if (ProtPower<3) {ProtPower++;}
		break;

		case 2:
		case 3:
			BolaSET(4);
		break;

		case 4:
			BolaSET(3);
		break;

		case 5:
			BolaSET(2);
		break;

		case 6:
			BolaSET(1);
		break;

		case 7:
			if (JugarVidas < 9) {JugarVidas++;}
			PanelLifePaint=1;
		break;

		case 8:
			ProtLife+=50; if (ProtLife>999) {ProtLife=999;}
		break;

		case 9:
			ProtArmaData[9][0]=1;
		break;
		}
	return true;
	}
	return super.Play();
}

};


// ---------------
// Item SET
// ===============

public void ItemSET(int CoorX, int CoorY)
{
	if (++ItemCont < 5) {return;}
	ItemCont=0;

	if (BolaArma==0) {while (ItemSelTab[ItemSelPos] > 1) {if (++ItemSelPos >= ItemSelTab.length) {ItemSelPos=0;}}}
	else {for (int i=0 ; i<16 ; i++) {if (ProtArmaData[ItemSelTab[ItemSelPos]][0] < ProtArmaData[ItemSelTab[ItemSelPos]][1]) {break;} if (++ItemSelPos >= ItemSelTab.length) {ItemSelPos=0;} if (i==15) {return;}}}

	ItemSET(CoorX,CoorY, ItemSelTab[ItemSelPos]);
	if (++ItemSelPos >= ItemSelTab.length) {ItemSelPos=0;}
}


public int ItemSET(int CoorX, int CoorY, int Type)
{
	if (ItemC == ItemMAX) {return(-1);}

	int i=ItemP[ItemC++];

	Items[i] = new Item();

	Items[i].AniSprSET(CoorX,CoorY, 148+Type, 2, 0, 0x124);
	Items[i].Side=159-Items[i].FrameIni;
	Items[i].Frames*=Items[i].Side;
	Items[i].Repetir=24;

	Items[i].Type=Type;

	return (i);
}

public int ItemSET(int CoorX, int CoorY, int Type, int RocaAnim)
{
	if (RocaAnim<0) {return -1;}
	CoorX=Rocas[RocaAnim].CoorX + ((RocaSize[Rocas[RocaAnim].Type]-16)/2);
	CoorY=Rocas[RocaAnim].CoorY-15;

	int i=ItemSET(CoorX, CoorY, Type);
	if (i != -1)
	{
	Items[i].RocaAnim=RocaAnim;
	}
	return i;
}


// ---------------
// Item INI
// ===============

final int ItemMAX=12;
int ItemC;
int[] ItemP;
Item[] Items;

public void ItemINI()
{
	ItemCont=0;

	for (int i=0 ; i<10 ; i++)
	{
	ProtArmaData[i][1]=1;
	}
	ProtArmaData[3][1]=3;	// Bola - Arma Verde
	ProtArmaData[4][1]=2;	// Bola - Arma Lila
	ProtArmaData[5][1]=2;	// Bola - Arma Azul
	ProtArmaData[6][1]=2;	// Bola - Arma Misil


	ItemC = 0;
	ItemP = new int[ItemMAX];
	Items = new Item[ItemMAX];
	for (int i=0 ; i<ItemMAX ; i++) {ItemP[i]=i;}
}

// ---------------
// Item END
// ===============

public void ItemEND()
{
	ItemP = null;
	Items = null;
	ItemC = 0;
}

// ---------------
// Item RUN
// ===============

public void ItemRUN()
{
	for (int i=0 ; i<ItemC ; i++)
	{
	if ( Items[ItemP[i]].Play() )
		{
		int t=ItemP[i];
		Items[t] = null;
		ItemP[i--] = ItemP[--ItemC];
		ItemP[ItemC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Destello - Engine
// ====================
// ********************

int[] DesteTabX = new int[] {-2,0,2,1,-1,2,0};
int DestePosX;
int[] DesteTabY = new int[] {-1,1,-2,0,1,0};
int DestePosY;

// ---------------
// Destello SET
// ===============

public void DestelloSET(int CoorX, int CoorY, int SizeX, int SizeY, int Cnt)
{
	while (Cnt-- > 0)
	{
	DestelloSET(CoorX+RND(SizeX),CoorY+RND(SizeY));
	}
}


public int DestelloSET(int CoorX, int CoorY)
{
	if (DestelloC == DestelloMAX) {return(-1);}

	int i=DestelloP[DestelloC++];

	Destellos[i] = new AniSpr();

	Destellos[i].AniSprSET(CoorX+DesteTabX[DestePosX], CoorY+DesteTabY[DestePosY], 160, 4, 1, 0x100);

	if (++DestePosX == DesteTabX.length) {DestePosX=0;}
	if (++DestePosY == DesteTabY.length) {DestePosY=0;}

	return (i);
}


// ---------------
// Destello INI
// ===============

final int DestelloMAX=80;
int DestelloC;
int[] DestelloP;
AniSpr[] Destellos;

public void DestelloINI()
{
	DestelloC = 0;
	DestelloP = new int[DestelloMAX];
	Destellos = new AniSpr[DestelloMAX];
	for (int i=0 ; i<DestelloMAX ; i++) {DestelloP[i]=i;}
}

// ---------------
// Destello END
// ===============

public void DestelloEND()
{
	DestelloP = null;
	Destellos = null;
	DestelloC = 0;
}

// ---------------
// Destello RUN
// ===============

public void DestelloRUN()
{
	for (int i=0 ; i<DestelloC ; i++)
	{
	if ( Destellos[DestelloP[i]].Play() )
		{
		int t=DestelloP[i];
		Destellos[t] = null;
		DestelloP[i--] = DestelloP[--DestelloC];
		DestelloP[DestelloC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Jordi - Engine
// ====================
// ********************

final int JordiMAX=10;
int JordiC=0;
int[][] Jordis;

// ---------------
// Jordi INI
// ===============

public void JordiINI()
{
	JordiC = 0;
	Jordis = new int[JordiMAX][];
}

// ---------------
// Jordi SET
// ===============

public void JordiSET(int CoorX, int CoorY, int SizeX, int SizeY, int DestX, int DestY)
{
	if (JordiC == JordiMAX) {return;}

	Jordis[JordiC] = new int[6];

	Jordis[JordiC][0] = CoorX;
	Jordis[JordiC][1] = CoorY;
	Jordis[JordiC][2] = SizeX;
	Jordis[JordiC][3] = SizeY;
	Jordis[JordiC][4] = DestX;
	Jordis[JordiC][5] = DestY;

	JordiC++;
}

// ---------------
// Jordi END
// ===============

public void JordiEND()
{
	Jordis = null;
	JordiC = 0;
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Nube - Engine
// ====================
// ********************

public class Nube extends AniSpr
{
int Cont=0;
int BaseX;
int AddX;

public Nube() {}

// ----------------
// Nube Play
// ================

public boolean Play()
{
	if (KeybX1==0) {Cont-=AddX/2;}
	if (KeybX1==1) {Cont-=AddX;}

	if ((Cont-=AddX) < 0) {Cont+=12; CoorX--;}
	
	if (CoorX < -128) {CoorX = GameSizeX+128;}

	if (FaseLevel==1)
	{
	CoorY-=AddX;
	if (CoorY < -64) {CoorY+=400;}
	}

	return (false);
}

};


// ---------------
// Nube SET
// ===============

public int NubeSET(int CoorX, int CoorY, int Type)
{
	if (NubeC == NubeMAX) {return(-1);}

	int i=NubeP[NubeC++];

	Nubes[i] = new Nube();

	Nubes[i].AniSprSET(CoorX, CoorY, 8+Type, 1, 10, 0x222);
	Nubes[i].AddX=(3-Type)*2;

	return (i);
}


// ---------------
// Nube INI
// ===============

final int NubeMAX=32;
int NubeC;
int[] NubeP;
Nube[] Nubes;

public void NubeINI()
{
	NubeC = 0;
	NubeP = new int[NubeMAX];
	Nubes = new Nube[NubeMAX];
	for (int i=0 ; i<NubeMAX ; i++) {NubeP[i]=i;}
}


// ---------------
// Nube END
// ===============

public void NubeEND()
{
	NubeP = null;
	Nubes = null;
	NubeC = 0;
}

// ---------------
// Nube RUN
// ===============

public void NubeRUN()
{
	for (int i=0 ; i<NubeC ; i++)
	{
	if ( Nubes[NubeP[i]].Play() )
		{
		int t=NubeP[i];
		Nubes[t] = null;
		NubeP[i--] = NubeP[--NubeC];
		NubeP[NubeC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Roca - Engine
// ====================
// ********************

int[] RocaTab = new int[] {0,1,0,-1};

int[] RocaSize = new int[] {64,46,32,60,54,54,48,48};

int[] RocaCaeTab = new int[] {0,0,0,0,0,1,1,1,2,2,3,4,5};

public class Roca extends AniSpr
{
int Type;
int Pos=0;
int Cont=0;
int Side;
int Desp;
int BaseY;
int ActY;
int RocaCaePos=0;

public Roca() {}

// ----------------
// Roca Play
// ================

public boolean Play(int Anim)
{
	if (Type==6 && RocaCaePos==0 && ProtMode==8 && ProtRocaAnim==Anim) {RocaCaePos++;Pause=0;}
	if (RocaCaePos!=0 && BaseY < FaseSizeY*8)
	{
		if (SpeedCnt==0 && Speed > 0) {Speed--;}
		if (Speed==0)
		{
		BaseY+=RocaCaeTab[RocaCaePos];
		if (++RocaCaePos == RocaCaeTab.length) {RocaCaePos--;}

//		if ( CheckTile(CoorX+16, CoorY+8, 0x01) != 0) {BaseY&=-8;}
		}
	}

	CoorY=BaseY+ActY;

	if (ProtMode==8 && ProtRocaAnim==Anim) {CoorY+=3;}
	else if (Type<4) {CoorY+=RocaTab[Pos]; if (--Cont < 0) {Cont=8; if (++Pos==4) {Pos=0;}}}

	ActY+=Side;

	if (ActY < 0 || ActY > Desp) {Side*=-1;}

	return super.Play();
}

};


// ---------------
// Roca SET
// ===============

public int RocaSET(int CoorX, int CoorY, int Type, int Desp)
{
	if (RocaC == RocaMAX) {return(-1);}

	int i=RocaP[RocaC++];

	Rocas[i] = new Roca();

	if (Type==4)
	{
	Rocas[i].AniSprSET(CoorX, CoorY, Type, 2,  0, 0x201);
	} else if (Type==6) {
	Rocas[i].AniSprSET(CoorX, CoorY, Type, 2,  5, 0x221);
	} else {
	Rocas[i].AniSprSET(CoorX, CoorY, Type, 1, 20, 0x222);
	}

	Rocas[i].Type=Type;
	Rocas[i].Cont=Type*5;
	Rocas[i].BaseY=CoorY-Desp;
	Rocas[i].Desp=Desp;
	Rocas[i].ActY=Desp;

	Rocas[i].Side=(Desp==0?0:-1);

	Rocas[i].Pos= (CoorX>>1)&0x3;
	Rocas[i].Cont=(CoorY>>1)&0x7;

	return (i);
}


// ---------------
// Roca INI
// ===============

final int RocaMAX=36;
int RocaC;
int[] RocaP;
Roca[] Rocas;

public void RocaINI()
{
	RocaC = 0;
	RocaP = new int[RocaMAX];
	Rocas = new Roca[RocaMAX];
	for (int i=0 ; i<RocaMAX ; i++) {RocaP[i]=i;}
}


// ---------------
// Roca END
// ===============

public void RocaEND()
{
	RocaP = null;
	Rocas = null;
	RocaC = 0;
}

// ---------------
// Roca RUN
// ===============

public void RocaRUN()
{
	for (int i=0 ; i<RocaC ; i++)
	{
	if ( Rocas[RocaP[i]].Play(RocaP[i]) )
		{
		int t=RocaP[i];
		Rocas[t] = null;
		RocaP[i--] = RocaP[--RocaC];
		RocaP[RocaC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Guardian - Engine
// ====================
// ********************

final int GuardianVida=300;

int GuardianX;
int GuardianY;
int GuardianPos;
int GuardianCont;
int GuardianAddY;
int GuardianMode;
int GuardianType;
int GuardianFlag=0;
int GuardianTime;
int GuardianTries;
int GuardianLife;
int GuardianMorMode;
int GuardBaseY;
int GuardDesp;
int GuardAnim;
int[] GuardAnims = new int[14];

// ---------------
// Guardian INI
// ===============

public void GuardianINI()
{
	GuardSprINI();

	GuardianPos=0;
	GuardianCont=0;
	GuardianAddY=0;
	GuardianMode=0;
	GuardianType=0;
	GuardianFlag=0;
	GuardianMorMode=0;
	GuardAnim=-1;
	GuardDesp=0;
	for (int i=0 ; i<GuardAnims.length ; i++) {GuardAnims[i]=-1;}
}


// ---------------
// Guardian END
// ===============

public void GuardianEND()
{
	GuardSprEND();
}


// ---------------
// Guardian SET
// ===============

public void GuardianSET()
{
	GuardianFlag=1;

	GuardianLife=GuardianVida;

	GuardianX=(747*8)+4;
	GuardianY=34*8;

	GuardSprSET(0,0);
	GuardSprSET(2,0);
	GuardSprSET(1,0);
	GuardSprSET(3,0);
	GuardSprSET(5,0);

	EnergySET(GuardianVida*3);

	MonitorSET(11);
}


// ---------------
// Guardian RUN
// ===============

int GuardCntY=0, GuardCntSideY=-1,  GuardCntAddY=3;

public void GuardianRUN()
{
	if (GuardianFlag==0) {return;}
	else
	if (GuardianFlag==1)
	{
		if (GuardianType==0) {EnergyAct=GuardianLife+(GuardianVida*2);}
		else
		if (GuardianType==1) {EnergyAct=GuardianLife+GuardianVida;}
		else
		if (GuardianType==2) {EnergyAct=GuardianLife;}


		if (GuardianType==0 && (GuardianLife-=EnemyKilled( GuardianX+4, GuardianY+22, 20,10 )) < 1 )
			{
			gc.SoundSET(4,1);
			AnimSpriteSET(-1, GuardianX-4, GuardianY+11,  0x44, 7, 1, 0x000);	// Explosion Peque�a
			GuardianLife=GuardianVida;
			GuardSprs[3][0]=4;
			GuardBaseY=GuardianY+16;
			GuardianType++;
			GuardianMode=0;
		}

		if (GuardianType==1 && (GuardianLife-=EnemyKilled( GuardianX+20, GuardianY+19, 19,19 )) < 1 )
			{
			gc.SoundSET(4,1);
			GuardAnim=AnimSpriteRES(GuardAnim);
			AnimSpriteSET(-1, GuardianX+12, GuardianY+12,  0x44, 7, 1, 0x000);	// Explosion Peque�a
			GuardianLife=GuardianVida;
			GuardSprs[4][0]=4;
			GuardianType++;
			GuardianMode=0;
			}

		if (GuardianType==2 && GuardSprs[1][1]>6 && (GuardianLife-=EnemyKilled( GuardianX+8, GuardianY+24, 10,10 )) < 1 )
			{
			gc.SoundSET(1,1);
			GuardAnim=AnimSpriteRES(GuardAnim);
			for (int i=0 ; i<GuardAnims.length ; i++) {GuardAnims[i]=AnimSpriteRES(GuardAnims[i]);}
			GuardianMode=90;
			}


		if (GuardianType!=1 && GuardianMode<90) {GuardianAddY=RocaTab[GuardianPos]; if (--GuardianCont < 0) {GuardianCont=6; if (++GuardianPos==4) {GuardianPos=0;}}}


		if (GuardianType==1)
		{
		if (GuardBaseY!=ProtSprY) {GuardBaseY+=(GuardBaseY<ProtSprY?1:-1);}
		GuardianY = (GuardBaseY+16 + (((TablaSeno[GuardCntY]*GuardDesp)>>8)*GuardCntSideY))-28;
		GuardCntY+=GuardCntAddY; if (GuardCntY > 90) {GuardCntY=90; GuardCntAddY*=-1;} else	if (GuardCntY <  0) {GuardCntY= 0; GuardCntAddY*=-1; GuardCntSideY*=-1;if (GuardDesp<40) {GuardDesp+=12;}}
		}
	}


// Mover Guardian
// --------------
	switch (GuardianMode)
	{
	case 0:
		if (GuardianType!=1 && GuardianY != ProtY-12) {GuardianY+=(GuardianY<ProtY-12?1:-1);break;}

		switch (GuardianType)
		{
		case 0:
		GuardianMode=12;
		break;

		case 1:
		GuardianMode=22;
		break;

		case 2:
		GuardianMode=32;
		break;
		}
	break;	

// Dispara Ca�on
// -------------
	case 12:
		if (++GuardianTime < 12) {break;}	// Retardo Inicial antes de disparar

		GuardianTries=3;	// 3 Disparos seguidos
		GuardianMode++;
	break;

	case 13:
		AnimSpriteSET(-1, GuardianX-8, GuardianY+6,  364, 1, 1, 0x000);	// Fuego Ca�on Guardian
		GuardianMode++;
	break;

	case 14:
		GuardianMode++;
	break;

	case 15:
		if (GuardianType!=0) {GuardianMode=0;break;}

		GuarDispSET(GuardianX+10, GuardianY+GuardianAddY+19, 2);
		GuarDispSET(GuardianX+ 5, GuardianY+GuardianAddY+19, 1);
		GuarDispSET(GuardianX+ 0, GuardianY+GuardianAddY+19, 0);

		GuardianTime=0;
		GuardianMode++;
	break;

	case 16:
		if (++GuardianTime < 5) {break;}	// Retardo entre Destellos
		GuardianTime=0;
		GuardianMode++;
	break;

	case 17:
		if (--GuardianTries > 0) {GuardianMode=13;break;}
		GuardianMode++;
	break;

	case 18:
		if (ProtY-12-GuardianY > -6 && ProtY-12-GuardianY < 6) {GuardianMode=12;break;}
		GuardianMode=0;
	break;

// Dispara Centro
// --------------
	case 22:
		GuardAnim = AnimSpriteSET(-1, GuardianX+20,GuardianY+GuardianAddY+20, 168, 3, 6, 0x112);
		if (GuardAnim==-1) {GuardianMode=0;break;}
		GuardianTime=0;
		GuardianMode++;
	break;

	case 23:
		AnimSprites[GuardAnim].CoorY=GuardianY+GuardianAddY+20;

		if (++GuardianTime < 34) {break;}	// Retardo antes de Lanzar Bola Azul

		GuardAnim=AnimSpriteRES(GuardAnim);
		CanonDispSET(GuardianX+20, GuardianY+GuardianAddY+20, 1);
		GuardianTime=0;
		GuardianMode++;
	break;

	case 24:
		CanonDispSET(GuardianX+20, GuardianY+GuardianAddY+20, 2);
		GuardianMode++;
	break;

	case 25:
		CanonDispSET(GuardianX+20, GuardianY+GuardianAddY+20, 3);
		GuardianMode++;
	break;

	case 26:
		if (++GuardianTime < 50) {break;}	// Retardo entre Bolas Azules
		GuardianMode=0;
	break;

// Dispara Ojo
// -----------
	case 32:
		if (++GuardSprs[1][1] != 12) {break;}	// Abriendose
		GuardianTime=0;
		GuardianMode++;
	break;

	case 33:
		GuardAnim = AnimSpriteSET(-1, GuardianX-1,GuardianY+GuardianAddY+21, 171, 2, 3, 0x107);
		if (GuardAnim==-1) {GuardianMode=0;GuardSprs[1][1]--;break;}
		AnimSprites[GuardAnim].Repetir=3;

		GuardianMode++;
	break;

	case 34:
		AnimSprites[GuardAnim].CoorY=GuardianY+GuardianAddY+21;		// Parpadeo Inicio salida Laser
		if (AnimSprites[GuardAnim].Pause==0) {break;}

		AnimSpriteSET(GuardAnim, GuardianX-1,GuardianY+GuardianAddY+21, 173, 1, 3, 0x102);
		GuardianMode++;
	break;

	case 35:
		AnimSprites[GuardAnim].CoorY=GuardianY+GuardianAddY+21;		// Laser Frame 1 ("Punta de laser")
		if (AnimSprites[GuardAnim].Pause==0) {break;}

		GuardAnim=AnimSpriteRES(GuardAnim);

		for (int i=0 ; i<GuardAnims.length ; i++)
		{
		GuardAnims[i] = AnimSpriteSET(GuardAnim, GuardianX-1-(i*16),GuardianY+GuardianAddY+21, 174, 2, 1, 0x107);
		if (GuardAnims[i] != -1) {AnimSprites[GuardAnims[i]].Repetir=15;}
		}

		GuardianMode++;
	break;

	case 36:		// <<==-- CUIDADO, Ajustado para que el laser mate al moto!!!! ***
		int Exit=0;
		for (int i=0 ; i<GuardAnims.length ; i++)
		{
			if (GuardAnims[i] != -1)
			{
			if (AnimSprites[GuardAnims[i]].Pause!=0) {Exit=1;break;}
			AnimSprites[GuardAnims[i]].CoorY=GuardianY+GuardianAddY+21;
			}
		}
		if (Exit==0) {break;}

		for (int i=0 ; i<GuardAnims.length ; i++) {GuardAnims[i]=AnimSpriteRES(GuardAnims[i]);}

		GuardianTime=0;
		GuardianMode++;
	break;

	case 37:
		if (++GuardianTime < 30) {break;}	// Retardo antes de Cerrar
		GuardianMode++;
	break;

	case 38:
		if (--GuardSprs[1][1] != 0) {break;}	// Cerrandose
		GuardianMode=0;
	break;

// Guardian Muere
// --------------
	case 90:
		GuardianFlag=2;
		EnergyRES();

		GuardSprs[1][1]+=1;

		switch (GuardianMorMode)
		{
		case 0:
			DestelloSET(GuardianX+4, GuardianY+4, 32,32, 6);
			GuardianMorMode++;
		break;

		case 1:
			DestelloSET(GuardianX+4, GuardianY+4, 32,32, 6);
			GuardianMorMode++;
		break;

		case 2:
			DestelloSET(GuardianX+4, GuardianY+4, 32,32, 6);
			GuardianMorMode++;
		break;

		case 3:
			DestelloSET(GuardianX+4, GuardianY+4, 32,32, 6);
			GuardianMorMode=0;
		break;
		}

		if (GuardianY < FaseSizeY*8) {GuardianY+=2;break;}

//		FaseExit=1;
		MonitorSET(7);

		GuardianMode++;
	break;
	}

}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// GuardSpr - Engine
// ====================
// ********************

final int GuardSprMAX=6;
int GuardSprC=0;
int[][] GuardSprs;

// ---------------
// GuardSpr INI
// ===============

public void GuardSprINI()
{
	GuardSprC = 0;
	GuardSprs = new int[GuardSprMAX][];
}

// ---------------
// GuardSpr SET
// ===============

public void GuardSprSET(int Frame, int AddY)
{
	if (GuardSprC == GuardSprMAX) {return;}

	GuardSprs[GuardSprC] = new int[2];

	GuardSprs[GuardSprC][0] = Frame;
	GuardSprs[GuardSprC][1] = AddY;

	GuardSprC++;
}

// ---------------
// GuardSpr END
// ===============

public void GuardSprEND()
{
	GuardSprs = null;
	GuardSprC = 0;
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// GuarDisp - Engine
// ====================
// ********************

public class GuarDisp extends AniSpr
{
int Time=25;

// ----------------
// GuarDisp Play
// ================

public boolean Play()
{
	if (--Time < 0) {return true;}

	CoorX-=8;

	return super.Play();
}

};


// ---------------
// GuarDisp SET
// ===============

public int GuarDispSET(int CoorX, int CoorY, int Type)
{
	if (GuarDispC == GuarDispMAX) {return(-1);}

	int i=GuarDispP[GuarDispC++];

	GuarDisps[i] = new GuarDisp();

//	GuarDisps[i].AniSprSET(CoorX,CoorY, 168+Type, 1, 10, 0x122);

	GuarDisps[i].AniSprSET(CoorX,CoorY, 164, 4, 1, 0x101);

	return (i);
}


// ---------------
// GuarDisp INI
// ===============

final int GuarDispMAX=24;
int GuarDispC;
int[] GuarDispP;
GuarDisp[] GuarDisps;

public void GuarDispINI()
{
	GuarDispC = 0;
	GuarDispP = new int[GuarDispMAX];
	GuarDisps = new GuarDisp[GuarDispMAX];
	for (int i=0 ; i<GuarDispMAX ; i++) {GuarDispP[i]=i;}
}

// ---------------
// GuarDisp RES
// ===============

public void GuarDispRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=GuarDispP[i];
	GuarDisps[t] = null;
	GuarDispP[i--] = GuarDispP[--GuarDispC];
	GuarDispP[GuarDispC] = t;
}

// ---------------
// GuarDisp END
// ===============

public void GuarDispEND()
{
	GuarDispP = null;
	GuarDisps = null;
	GuarDispC = 0;
}

// ---------------
// GuarDisp RUN
// ===============

public void GuarDispRUN()
{
	for (int i=0 ; i<GuarDispC ; i++)
	{
	if ( GuarDisps[GuarDispP[i]].Play() )
		{
		int t=GuarDispP[i];
		GuarDisps[t] = null;
		GuarDispP[i--] = GuarDispP[--GuarDispC];
		GuarDispP[GuarDispC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-










// ********************
// --------------------
// Nostromo - Engine
// ====================
// ********************

int NostX;
int NostY;
int NostAddY;
int NostMode;
int NostPos;
int NostCont;
int NostFlag;
int NostContY;
int NostDestX;
int NostSide;
int NostKilled;
int NostLife;
int NostConta;

// ---------------
// Nostromo INI
// ===============

public void NostromoINI()
{
	NostFlag=0;
}


// ---------------
// Nostromo END
// ===============

public void NostromoEND()
{
	NostFlag=0;
}


// ---------------
// Nostromo SET
// ===============

public void NostromoSET()
{
	NostX=ProtX;
	NostY=gc.ScrollY-120;
	NostMode=0;
	NostFlag=1;
	NostDestX=330;
	NostSide=( NostX+NostDestX < ProtSprX+16 ? 1 : -1 );
	NostLife=300;

	NostArmaSET(156,68,1,0x01);
	NostArmaSET(202,68,1,0x02);
	NostArmaSET(247,68,1,0x04);
	NostArmaSET(356,68,1,0x10);
	NostArmaSET(402,68,1,0x20);
	NostArmaSET(447,68,1,0x40);

	NostKilled=0;
	NostConta=50;

	EnergySET(1500);
}


// ---------------
// Nostromo RUN
// ===============

public void NostromoRUN()
{
	if (NostFlag==0) {return;}

	EnergyAct=NostLife;


	if (NostY!=48) {NostAddY=RocaTab[NostPos]; if (--NostCont < 0) {NostCont=16; if (++NostPos==4) {NostPos=0;}}}


	switch (NostMode)
	{
// La nave Baja
// ------------
	case 0:
		NostX+=NostSide;
		if (NostY < 0 && --NostContY < 0) {NostY++; NostContY=1;}
		if (NostY==0) {NostDestX=330; NostMode++;}
	break;

// Vamos a por el Protagonista
// ---------------------------
	case 1:

		if ( (ProtSprX+16)-(NostX+NostDestX) < -40 || (ProtSprX+16)-(NostX+NostDestX) > 40 )	// Fuera del Area?
		{
			if ( (NostKilled & 0x0F) != 7 && (ProtSprX+16) < (NostX+330) ) {NostDestX=230;}
			else
			if ( (NostKilled>>4 != 7) && (ProtSprX+16) > (NostX+330) ) {NostDestX=430;}
			else
			if ( (NostKilled & 0x0F) != 7 ) {NostDestX=230;} else {NostDestX=430;}

		NostSide=( NostX+NostDestX < ProtSprX+16 ? 1 : -1 );
		}


		if ((NostKilled&0x0F)==0x07)
		{
		DestelloSET(NostX+120, NostY+50, 200, 50, 4);
		}
		if ((NostKilled&0xF0)==0x70)
		{
		DestelloSET(NostX+330, NostY+50, 200, 50, 4);
		}

		if (NostKilled==0x77) {NostDestX=330;NostMode++;MonitorSET(8);}

		NostX+=NostSide;
	break;

// La nave se va para arriba
// --------------------------
	case 2:
		if ( (ProtSprX+16)-(NostX+NostDestX) < -200 || (ProtSprX+16)-(NostX+NostDestX) > 200 )	// Fuera del Area?
		{
		NostSide=( NostX+NostDestX < ProtSprX+16 ? 1 : -1 );
		}

		NostX+=NostSide;

		if ((NostKilled&0x0F)==0x07)
		{
		DestelloSET(NostX+120, NostY+50, 200, 50, 3);
		}
		if ((NostKilled&0xF0)==0x70)
		{
		DestelloSET(NostX+330, NostY+50, 200, 50, 3);
		}

		if (NostY+120 > 0 && --NostContY < 0) {NostY--; NostContY=7;}

		if ( (ProtSprY+16)-(NostY+110) > gc.ScrollSizeY-16 ) {ProtMorirSET(); NostMode=-1; break;}

		if ((NostLife-=EnemyKilled( NostX+100, NostY+10, 460, 100 )) < 1) { if (NostX+330 < FaseSizeX*4) {NostSide=1;} else {NostSide=-1;} NostMode++; gc.SoundSET(1,1); EnergyRES();MonitorSET(9);}
	break;

// La nave se estrella
// -------------------
	case 3:
		DestelloSET(NostX+120, NostY+10, 200, 90, 3);
		DestelloSET(NostX+330, NostY+10, 200, 90, 3);

		AnimSpriteSET(-1, NostX+120+RND(388), NostY+32+RND(56),  0x4F, 7, 1, 0x000);	// Explosion Grande

		if (NostY!=48)
		{
		NostX+=NostSide;
		if (NostY < 48 && --NostContY < 0) {NostY++; NostContY=5;}
		} else {
		if (--NostConta < 0) {FaseExit=1;}
		}
	break;
	}

}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// NostArma - Engine
// ====================
// ********************

int[] NostArmaTabX = new int[] {27,30,33,34,34,34,33,30,27,24,21,20,20,20,21,24};
int[] NostArmaTabY = new int[] {24,24,25,28,31,34,37,38,38,38,37,34,31,28,25,24};

public class NostArma extends AniSpr
{
int Mode=0;
int Type;
int AddX;
int AddY;
int SalY=0;
int Time=0;
int DispPos;
int Life=100;
int Pos, Id;

// ----------------
// NostArma Play
// ================

public boolean Play()
{
	if (Type==0) {EnergyAct+=Life;}
	if (Type==1) {EnergyAct+=Life+100;}

	if (SalY > 28 && (Life-=EnemyKilled( CoorX+16, CoorY+10, 24, (Type==0?18:10) )) < 1)
	{
	gc.SoundSET(4,1);
	AnimSpriteSET(-1, CoorX+12, CoorY+12,  0x4F, 7, 1, 0x000);	// Explosion Grande

	NostArmaSET(CoorX,CoorY, Type+4, 0);

	if (Type==0) {NostKilled|=Pos; return true;}

	Time=0;
	SalY=0;
	Life+=100;
	Type=0;
	Mode=5;
	}


	switch (Mode)
	{
	case 0:
		if (NostMode==0 || ++Time<80) {break;}
		Mode++;
	break;

	case 1:
		if ( ProtSprX-CoorX-12 < -80 || ProtSprX-CoorX-12 > 80 ) {break;}	// NO Abrimos Compuerta?
		Pause=0;
		Mode++;
	break;

	case 2:
		if (Pause==0) {break;}

		AniSprSET( (Type==0?9:26), 1, 1, 0x022);

		Mode++;
	break;

	case 3:
		if (++SalY < 38) {break;}
		Mode++;
	break;

	case 4:
		if ( ProtSprX-CoorX-12 > -80 && ProtSprX-CoorX-12 < 80 ) {if (Type==0) {Mode=42;} else {Mode=22;} break;}	// Disparamos?
		if ( ProtSprX-CoorX-12 < -80 || ProtSprX-CoorX-12 > 80 ) {Mode++; break;}	// Cerramos Comp�erta
	break;

	case 5:
		if (--SalY > 0) {break;}

		AniSprSET( 6, 3, 7, 0x012);
		Mode++;
	break;

	case 6:
		if (Pause==0) {break;}

		AniSprSET( 6, 3, 7, 0x022);

		Time=0;
		Mode=0;
	break;


// Arma Triple
// ------------
	case 22:
		NostArmaSET(AddX, CoorY-NostAddY, 3, 0);

		CanonDispSET(CoorX+14-8, CoorY+32-8, -1, 2);
		CanonDispSET(CoorX+27-8, CoorY+38-8,  0, 2);
		CanonDispSET(CoorX+39-8, CoorY+33-8,  1, 2);

		SalY-=3;

		Time=0;
		Mode++;
	break;

	case 23:
		SalY++;
		Mode++;
	break;

	case 24:
		SalY++;
		Mode++;
	break;

	case 25:
		SalY++;
		Mode++;
	break;

	case 26:
		if (++Time<80) {break;}
		Mode=4;
	break;


// Arma Redonda
// ------------
	case 42:
		int Anim = NostArmaSET(AddX, CoorY-NostAddY, 2, 0); if (Anim!=-1) {NostArmas[Anim].Id=Pos;}

		DispPos=0;
		Time=0;
		Mode++;
	case 43:
		if ((Time&1)==0)
		{
		CanonDispSET(CoorX+NostArmaTabX[DispPos]-6, CoorY+NostArmaTabY[DispPos]-12, 0);
		if (++DispPos == NostArmaTabX.length) {DispPos=0;}
		}

		if (++Time<=15*3*2) {break;}
		Time=0;
		Mode++;
	break;

	case 44:
		if (++Time<100) {break;}
		Mode=4;
	break;




// Parpadeo Destello Arma Redonda
// ------------------------------
	case 90:
		if (Bank==0) {Bank=9;} else {Bank=0;}
		if ((NostKilled & Id) != 0) {return true;}
	break;

	case 94:
		CoorY+=RocaCaeTab[DispPos];
		if (++DispPos == RocaCaeTab.length) {DispPos--;}

		if ( CheckTile(CoorX+16,CoorY+18, 23,1, 0x1) != 0) {Mode++;}
	return false;

	case 95:
		AnimSpriteSET(-1, CoorX+12, CoorY+12,  0x4F, 7, 1, 0x000);	// Explosion Peque�a
		Time=1;
		Mode++;
	return false;

	case 96:
		DestelloSET(CoorX+8, CoorY, 24,18, 4);

		if (++Time<10) {return false;}
		AnimSpriteSET(-1, CoorX+12, CoorY+12,  0x4F, 7, 1, 0x000);	// Explosion Grande
	return true;
	}

	CoorX=NostX+AddX;
	CoorY=NostY+NostAddY+AddY+SalY;

	return super.Play();
}

};


// ---------------
// NostArma SET
// ===============

public int NostArmaSET(int CoorX, int CoorY, int Type, int Pos)
{
	if (NostArmaC == NostArmaMAX) {return(-1);}

	int i=NostArmaP[NostArmaC++];

	NostArmas[i] = new NostArma();

	switch (Type)
	{
	case 0:
	case 1: NostArmas[i].AniSprSET(NostX+CoorX, NostY+CoorY,  6, 3,  7, 0x022); break;
	case 2: NostArmas[i].AniSprSET(NostX+CoorX, NostY+CoorY, 10,16,  1, 0x004); NostArmas[i].Mode=90; NostArmas[i].Repetir=3; break;
	case 3: NostArmas[i].AniSprSET(NostX+CoorX, NostY+CoorY, 27, 2,  1, 0x000); NostArmas[i].Mode=92; break;
	case 4: NostArmas[i].AniSprSET(CoorX, CoorY,  9, 1,  2, 0x022); NostArmas[i].Mode=94; break;
	case 5: NostArmas[i].AniSprSET(CoorX, CoorY, 26, 1,  2, 0x022); NostArmas[i].Mode=94; break;
	}
	NostArmas[i].Type=Type;
	NostArmas[i].AddX=CoorX;
	NostArmas[i].AddY=CoorY;
	NostArmas[i].Pos=Pos;

	return (i);
}


// ---------------
// NostArma INI
// ===============

final int NostArmaMAX=16;
int NostArmaC;
int[] NostArmaP;
NostArma[] NostArmas;

public void NostArmaINI()
{
	NostArmaC = 0;
	NostArmaP = new int[NostArmaMAX];
	NostArmas = new NostArma[NostArmaMAX];
	for (int i=0 ; i<NostArmaMAX ; i++) {NostArmaP[i]=i;}
}

// ---------------
// NostArma END
// ===============

public void NostArmaEND()
{
	NostArmaP = null;
	NostArmas = null;
	NostArmaC = 0;
}

// ---------------
// NostArma RUN
// ===============

public void NostArmaRUN()
{
	for (int i=0 ; i<NostArmaC ; i++)
	{
	if ( NostArmas[NostArmaP[i]].Play() )
		{
		int t=NostArmaP[i];
		NostArmas[t] = null;
		NostArmaP[i--] = NostArmaP[--NostArmaC];
		NostArmaP[NostArmaC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-














// ********************
// --------------------
// Panel - Engine
// ====================
// ********************


int PanelLife;

int PanelPaint;
int PanelLifePaint;
int PanelItemPaint;

// ---------------
// Panel INI
// ===============

public void PanelINI()
{
	PanelLife=ProtLife;

	PanelPaint=1;
	PanelLifePaint=1;
	PanelItemPaint=1;
}


// ---------------
// Panel Item SET
// ===============

public void PanelItemSET()
{
	PanelItemPaint=1;
}


// ---------------
// Panel RUN
// ===============

public void PanelRUN()
{

	// Energia
	// -------
	if (ProtLife!=PanelLife)
	{
	PanelLife+=(ProtLife<PanelLife?-1:1);
	PanelLifePaint=1;
	}
	// ========

}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Energy - Engine
// ====================
// ********************

int Energy_ON;
int EnergyY;
int EnergyAct;
int EnergyMax;
int EnergyPos;
int EnergyNow;

// ---------------
// Energy INI
// ===============

public void EnergyINI()
{
	Energy_ON=0;
	EnergyY=0;
}


// ---------------
// Energy END
// ===============

public void EnergyEND()
{
	Energy_ON=0;
}


// ---------------
// Energy SET
// ===============

public void EnergySET(int Max)
{
	Energy_ON=1;
	EnergyMax=Max;
	EnergyAct=Max;
	EnergyPos=133;
	EnergyNow=0;
}


// ---------------
// Energy RES
// ===============

public void EnergyRES()
{
	Energy_ON=2;
	EnergyAct=0;
}


// ---------------
// Energy RUN
// ===============

public void EnergyRUN()
{
	if (Energy_ON==0) {return;}

	if (Energy_ON==1 && EnergyY < 10) {EnergyY++;}
	else
	if (Energy_ON==2 && --EnergyY == 0) {Energy_ON=0;}

	EnergyPos=(EnergyAct*133)/EnergyMax; if (EnergyPos==0 && EnergyAct!=0) {EnergyPos=1;}

	if (EnergyPos!=EnergyNow) {EnergyNow+=(EnergyPos<EnergyNow?-1:1);}
}

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// Intro - Engine
// ===================
// *******************

int IntroPaint;

int IntroPlanetaY;
int IntroPlanetaCnt;
int IntroNodrizaY;
int IntroNodrizaCnt;

int IntroMode;
int IntroX;
int IntroY;
int IntroFr;
int IntroCont;
int[] IntroWait = new int[] {28,2,2,2,2,2,2, 100000,100000};

// -------------------
// Intro INI
// ===================

public void IntroINI()
{
	IntroPlanetaY=-128;
	IntroNodrizaY=GameSizeY;

	IntroX=-50;
	IntroFr=0;
	IntroMode=0;
}

// -------------------
// Intro RUN
// ===================

public boolean IntroRUN()
{
	if (IntroPlanetaY < 0    && --IntroPlanetaCnt < 0) {IntroPlanetaY++; IntroPlanetaCnt=2;}
	if (IntroNodrizaY > -406 && --IntroNodrizaCnt < 0) {IntroNodrizaY--; IntroNodrizaCnt=0;}

	switch (IntroMode)
	{
	case 0:
		if (IntroNodrizaY+203 > 120) {break;}

		IntroCont=IntroWait[IntroFr];
		SinusSET(0, 50, 40, 1);
		SinusSET(1, 80, 40, 1);
		IntroMode++;
	case 1:
		if (!SinusFIN(0))
		{
		IntroX=50+(50-SinusRUN(0));
		IntroY=40+(80-SinusRUN(1));
		if (--IntroCont < 1) {IntroCont=IntroWait[++IntroFr];}
		} else {
		IntroCont=0;
		IntroMode++;
		}
	break;

	case 2:
		if (++IntroCont<40) {break;}

		IntroFr=8;
		IntroCont=0;
		IntroMode++;
	break;

	case 3:
		if (++IntroCont>40) {IntroCont=0;IntroMode++;}
	break;

	case 4:
		if (++IntroCont>20) {IntroCont=0;}
	break;
	}


	if ( (KeybE1 & 0x2)==0x2 && (KeybE2 & 0x2)!=0x2 ) {return true;}	// Boton Menu
	if ( (KeybE1 & 0x4)==0x4 && (KeybE2 & 0x4)!=0x4 ) {return true;}	// Boton Seleccion
	if (KeybB1==5 && KeybB2!=5) {return true;}

	gc.CanvasFillRGB=0;
	gc.CanvasFillPaint=1;
	IntroPaint=1;

	return false;
}

// <=- <=- <=- <=- <=-









// *******************
// -------------------
// Menu - Engine
// ===================
// *******************

int MenuPaint;
int MenuType;

int MenuMode;
int MenuPiezaCont;
int MenuPiezaDesp;

int MenuOptAX;
int MenuOptBX;
int MenuOptPos;
int MenuOptSide;

int MenuAspaDesp;

String[] MenuOpt;
String MenuTxtNew;
String MenuTxtOld;

// -------------------
// Menu INI
// ===================

public void MenuINI()
{
//	Capsula Open
	MenuMode=0;
	MenuTxtOld=" ";
	MenuPiezaCont=0;
	MenuPiezaDesp=0;
	SinusSET(0, 125, 16, 1);

//	Capsula Running
	MenuOptSide=1;
	MenuOptAX=128;
	MenuOptBX=128;

//	Printer Running
	Texto.MenuOptTxt0[1]=Texto.Sound[(GameSound==0)?0:1];
	Texto.MenuOptTxt1[1]=Texto.Sound[(GameSound==0)?0:1];
	Texto.MenuOptTxt3[1]=Texto.Sound[(GameSound==0)?0:1];
	Texto.MenuOptTxt3[5]=Texto.Inmune[(ProtInmune==0)?0:1];

//	gc.MenuINI_Gfx();
//	gc.JugarINI_Gfx();
}

// -------------------
// Menu SET
// ===================

public void MenuSET(int Type)
{
	MenuOptPos=0;

	MenuType=Type;
	switch (Type)
	{
	case 0:	MenuOpt=Texto.MenuOptTxt0; break;
	case 1:	MenuOpt=Texto.MenuOptTxt1; break;
	case 2:	MenuOpt=Texto.MenuOptTxt2; break;
	case 3: MenuOpt = new String[1]; MenuOpt[0]=Texto.MenuOptMision[getLevel(JugarLevel)]; break;
	case 4:	MenuOpt=Texto.MenuOptOver; break;					// Continue 9...
	case 5:	MenuOpt = new String[1]; MenuOpt[0]=" "; break;		// Game Over
	case 6:	MenuOpt=Texto.MenuOptFases; break;
	case 7:	MenuOpt=Texto.MenuOptTxt3; break;
	}

	if (GameStatus!=53) {gc.PrinterINI(gc.MenuImg); GameStatus=52;} else {MenuMode=1;}

	switch (MenuType)
	{
	case 0: gc.PrinterSET(Texto.Intro); break;
	case 1:
	case 7:
	case 3:	gc.PrinterSET(Texto.Fase[getLevel(JugarLevel)]); break;
	case 4:
		int ind=Texto.MenuTxtOver.length-3;
		int fin=Texto.MenuTxtOver[ind].length();
		Texto.MenuTxtOver[ind] = Texto.MenuTxtOver[ind].substring(0,fin-1);
		Texto.MenuTxtOver[ind] = Texto.MenuTxtOver[ind].concat(Integer.toString(JugarCredit));
		gc.PrinterSET(Texto.MenuTxtOver);
		break;
	case 5: gc.PrinterSET(Texto.MenuTxtOve2); break;
	}

	MenuOptSide=1;
}


public int getLevel(int Level)
{
	switch (Level)
	{
	case 0:	return 0;
	case 1:	return 1;
	case 2:
	case 3:
	case 4:	return 2;
	case 5:	return 3;
	case 6:	return 4;
	}
	return 0;
}


public int getStage(int Level)
{
	switch (Level)
	{
	case 0:	return 0;
	case 1:
	case 2:
	case 3:	return 1;
	case 4:
	case 5:
	case 6:	return 2;
	case 7:	return 3;
	case 8:	return 4;
	}
	return 0;
}


// -------------------
// Menu RUN
// ===================

public boolean MenuRUN()
{
	switch (MenuMode)
	{
	case 0:
		if ( !SinusFIN(0) ) {MenuPiezaDesp=SinusRUN(0); break;}
		MenuMode++;
	break;

	case 1:
		if (MenuType==6) {gc.PrinterSET(Texto.Fase[getStage(MenuOptPos)]);}

		MenuTxtNew=MenuOpt[MenuOptPos];

		if (MenuOptSide == 1)
		{
		SinusSET(0,  124, 16, 0);
		SinusSET(1, -124, 16, 1);
		} else {
		SinusSET(0, -124, 16, 0);
		SinusSET(1,  124, 16, 1);
		}
		MenuMode++;
	case 2:
		if ( !SinusFIN(0) ) {MenuOptAX=SinusRUN(0); MenuOptBX=SinusRUN(1); break;}

		MenuTxtOld=MenuTxtNew;

		SinusSET(2, 16, 8, 0);
		MenuMode++;
	break;

	case 3:
		if ( !SinusFIN(2) ) {MenuAspaDesp=16-SinusRUN(2);} else {SinusSET(2, 16, 8, 0);}

		if (MenuType==0 && scout) {scout=false; GameCheat_ON=true; gc.PrinterSET(Texto.Cheat);}	// Truco Empezar Level...

		if (MenuOpt.length > 1)
		{
		if (KeybX1== 1 && KeybX2!= 1) {gc.SoundSET(8,1); if (++MenuOptPos == MenuOpt.length) {MenuOptPos=0;}  MenuMode=1; MenuOptSide=1; return false;}
		if (KeybX1==-1 && KeybX2!=-1) {gc.SoundSET(8,1); if (--MenuOptPos <0 ) {MenuOptPos=MenuOpt.length-1;} MenuMode=1; MenuOptSide=0; return false;}
		}

		if (gc.PrinterMode==0 && (MenuType==4 || MenuType==5)) {MenuSET(0); break;}	// Game Over Contador=0 o Game Over Retardo fin.


		if ((MenuType==1 || MenuType==7) && (KeybE1 & 0x2)==0x2 && (KeybE2 & 0x2)!=0x2 )	// Boton Menu
		{
		GameMode=1;
		return true;
		}


		if ( (KeybE1 & 0x4)==0x4 && (KeybE2 & 0x4)!=0x4 )	// Boton Seleccion
		{

		gc.SoundSET(7,1);

			switch (MenuType)
			{
			case 0:	// Menu Principal

				switch (MenuOptPos)
				{
				case 0:			// Jugar
					GameMode=0;
					if (!GameCheat_ON) {MenuSET((JugarLevel==0)?3:2);} else {MenuSET(6);}
				break;

				case 1:			// Sound ON/OFF
					GameSound=(GameSound==0)?1:0;
					Texto.MenuOptTxt0[1]=Texto.Sound[(GameSound==0)?0:1];
					Texto.MenuOptTxt1[1]=Texto.Sound[(GameSound==0)?0:1];
					Texto.MenuOptTxt3[1]=Texto.Sound[(GameSound==0)?0:1];
					MenuTxtNew=MenuOpt[MenuOptPos];
					MenuMode--;
				break;

				case 2:			// Controls
					gc.PrinterSET(Texto.Controles);
				break;

				case 3:			// Credits
					gc.PrinterSET(Texto.Creditos);
				break;

				case 4:			// Exit Game
					MenuMode++;
				break;
				}
			break;

			case 1:	// Menu Secundario
			case 7:	// Menu Secundario (CHEATS)

				switch (MenuOptPos)
				{
				case 0:			// Continuar
					GameMode=1;
				return true;

				case 1:			// Sound ON/OFF
					GameSound=(GameSound==0)?1:0;
					Texto.MenuOptTxt0[1]=Texto.Sound[(GameSound==0)?0:1];
					Texto.MenuOptTxt1[1]=Texto.Sound[(GameSound==0)?0:1];
					Texto.MenuOptTxt3[1]=Texto.Sound[(GameSound==0)?0:1];
					MenuTxtNew=MenuOpt[MenuOptPos];
					MenuMode--;
				break;

				case 2:			// Controls
					gc.PrinterSET(Texto.Controles);
				break;

				case 3:			// Exit Game
					MenuMode++;
				break;

				case 4:			// Guardar
					PrefsSET();
					FaseEND();
					MenuSET(0);
				break;

				case 5:			// Inmune
					ProtInmune=(ProtInmune==0)?1:0;
					Texto.MenuOptTxt3[5]=Texto.Inmune[(ProtInmune==0)?0:1];
					MenuTxtNew=MenuOpt[MenuOptPos];
					MenuMode--;
				break;

				case 6:			// Full Army
					ProtArma=1;
					ProtPower=3;
					ProtArmaData[3][0]=3;	// Bola - Arma Verde
					ProtArmaData[4][0]=2;	// Bola - Arma Lila
					ProtArmaData[5][0]=2;	// Bola - Arma Azul
					ProtArmaData[6][0]=2;	// Bola - Arma Misil
					BolaSET(4);
					PanelItemSET();
					GameMode=1;
				return true;

				case 7:			// NextLevel
					FaseExit=1;
					GameMode=1;
				return true;
				}
			break;

			case 2:	// Menu Continuar
				switch (MenuOptPos)
				{
				case 0:		// Continuar
					GameMode=2;
					MenuSET(3);
				break;

				case 1:		// Partida Nueva
					GameMode=0;
					JugarLevel=0;
					MenuSET(3);
				break;

				case 2:		// Salir al Menu
					MenuSET(0);
				break;
				}
			break;

			case 3:	// Menu Mostrar Fase (Hemos pasado de Nivel...)
			return true;

			case 4:	// Menu Continue?
				switch (MenuOptPos)
				{
				case 0:			// SI
					GameMode=2;
					MenuSET(3);
				break;

				case 1:			// NO
					MenuSET(0);
				break;
				}
			break;

			case 5:
				MenuSET(0);
			break;

			case 6:	// Menu Seleccionar Fase

				ProtSET_Vida();

				JugarLevel=new int[] {0,1,1,1,2,3,4,5,6} [MenuOptPos];
				JugarStage=(JugarLevel!=1)?0:MenuOptPos-1;

				JugarCredit=4;
				JugarVidas=2;
				JugarBola=true;
				GameMode=2;
			return true;
			}
		}
	break;

	case 4:
		gc.PrinterSET(Texto.MenuPowerOff);
		SinusSET(0, 125, 24, 0);
		MenuMode++;
	case 5:
		if ( !SinusFIN(0) ) {MenuPiezaDesp=SinusRUN(0); break;}
		MenuMode++;
		GameExit=1;		
	break;
	}

	MenuPaint=1;

	return false;
}


// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Monitor - Engine
// ===================
// *******************

int[] MoniData;

// -------------------
// Monitor INI
// ===================

public void MonitorINI()
{
	gc.MonitorINI();
	MoniData = new int[Texto.Info.length];
}

// -------------------
// Monitor SET
// ===================

public void MonitorSET(int data)
{
	if (MoniData[data] == 0) {MoniData[data]=1; gc.MonitorSET(Texto.Info[data]);}
}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// Final - Engine
// ===================
// *******************

int FinalPaint;

// -------------------
// Final INI
// ===================

public void FinalINI()
{
}

// -------------------
// Final RUN
// ===================

public boolean FinalRUN()
{
	FinalPaint=1;

	return gc.FinalMode==3;
}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// Prefs - Engine
// ===================
// *******************

byte[] PrefsData = new byte[] {1,0,0,0,3,2,0,10,  0,0,0,0,0,0,0,0,0,0};

// -------------------
// Prefs INI
// ===================

public void PrefsINI()
{
	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("Aminoid_Prefs", true);

		if (rs.getNumRecords() == 0)
		{
		rs.addRecord(PrefsData, 0, PrefsData.length);
		} else {
		PrefsData=rs.getRecord(1);
		}
		rs.closeRecordStore();
	} catch(Exception e) {}

	GameSound=PrefsData[0];
	GameVibra=PrefsData[1];
	JugarLevel=PrefsData[2]; if (JugarLevel < 0 || JugarLevel > 6) {JugarLevel=0;}
	JugarStage=PrefsData[3]; if (JugarStage < 0 || JugarStage > 2) {JugarStage=0;}
	JugarCredit=PrefsData[4]; if (JugarCredit < 0 || JugarCredit > 9) {JugarCredit=3;}
	JugarVidas=PrefsData[5]; if (JugarVidas < 0 || JugarVidas > 9) {JugarVidas=2;}
	JugarBola=PrefsData[6]!=0;
	ProtLife=PrefsData[7]*10; if (ProtLife < 10 ) {ProtLife=10;}
	for (int i=0 ; i<10 ; i++) {ProtArmaData[i][0]=PrefsData[8+i];}
}

// -------------------
// Prefs SET
// ===================

public void PrefsSET()
{
	PrefsData[0]=(byte)GameSound;
	PrefsData[1]=(byte)GameVibra;
	PrefsData[2]=(byte)JugarLevel;
	PrefsData[3]=(byte)JugarStage;
	PrefsData[4]=(byte)JugarCredit;
	PrefsData[5]=(byte)JugarVidas;
	PrefsData[6]=(byte)((JugarBola)?1:0);
	PrefsData[7]=(byte)(ProtLife/10);
	for (int i=0 ; i<10 ; i++) {PrefsData[8+i]=(byte)ProtArmaData[i][0];}

	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("Aminoid_Prefs", true);

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





// ********************
// --------------------
// Trig - Engine
// Por David Rodriguez
// ====================
// ********************

	private static int sintable[]={
	0,25,50,75,100,125,150,175,199,224,248,273,297,321,344,368,391,414,437,460,482,504,
	526,547,568,589,609,629,649,668,687,706,724,741,758,775,791,807,822,837,851,865,
	878,890,903,914,925,936,946,955,964,972,979,986,993,999,1004,1008,1012,1016,1019,1021,
	1022,1023};
	
	private static int costable[]={
	1024,1023,1022,1021,1019,1016,1012,1008,1004,999,993,986,979,972,964,955,946,936,
	925,914,903,890,878,865,851,837,822,807,791,775,758,741,724,706,687,668,649,629,
	609,589,568,547,526,504,482,460,437,414,391,368,344,321,297,273,248,224,199,175,
	150,125,100,75,50,25};
	
	private static int tantable[]={
	0,25,50,75,100,126,151,177,203,229,256,283,310,338,366,395,424,453,484,515,547,580,
	613,648,684,721,759,799,840,883,928,974,1024,1075,1129,1187,1247,1312,1380,1453,
	1532,1617,1708,1807,1915,2034,2165,2310,2472,2654,2861,3099,3375,3700,4088,4560,
	5148,5901,6903,8302,10397,13882,20845,41719
	};
			
	static int Trig_sin(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=sintable[a%64];
		else 	v=sintable[63-(a%64)];	
		if(a>=128) v=-v;
		return v;
	}

	static int Trig_cos(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=costable[a%64];
		else 	v=costable[63-(a%64)];	
		if((a>>6==1) || (a>>6==2)) v=-v;
		return v;
	}
	
	static int Trig_atan(int s, int c)
	{
		int v,a=0;
		if(c!=0) v=(s<<10)/c;
		else v=99999;
		
		if(v<0) v=-v;
				
		while((tantable[a]<v) && (a<63)) a++;
		
		if(s>=0 && c>=0)
			return a;
		else if(s>=0 && c<0)
			return 128-a;
		else if(s<0 && c<0)
			return 128+a;		
		return (256-a)%256;		
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