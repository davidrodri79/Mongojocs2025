


// -----------------------------------------------
// Sabotage (Version Color) v1.0 Rev.2 (19.3.2003)
// ===============================================
// Programado por Juan Antonio G�mez.
// Graficos de Jordi Palome.
// Musicas de Esteban.
// Organizacion: Gerard Fernandez
// 2003 (c) Microjocs S.L.
// ------------------------------------


package com.mygdx.mongojocs.sabotage;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
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
	PrefsSET();
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
	GameINI(); System.gc();
//	GameSET(); System.gc();

	gc.ListenerSET(gc);

	while (GameExit==0)
	{
		GameMilis = System.currentTimeMillis();

		KeyboardRUN();

		GameRUN();

		gc.repaint();
		gc.serviceRepaints();

		GameSleep=(40-(int)(System.currentTimeMillis()-GameMilis));
		if (GameSleep<1) {GameSleep=1;}

//	System.gc();

		try	{
		thread.sleep(GameSleep);
		} catch(java.lang.InterruptedException e) {}
	}

//	GameEND(); System.gc();

	destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



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
// Tabla Seno
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
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

int KeybX,  KeybY,  KeybB;
int KeybX1, KeybY1, KeybB1;
int KeybX2, KeybY2, KeybB2;

// -------------------
// Keyboard RUN
// ===================

public void KeyboardRUN()
{
	if (gc.KeyType==2)
	{
		KeybX=0; KeybY=0; KeybB=0; gc.KeyType=0;
	}
	else if (gc.KeyType==1)
	{
		KeybX=0; KeybY=0; KeybB=0; gc.KeyType=0;

		/*switch(gc.getGameAction(gc.KeyData))
		{
			case 1:KeybY=-1;break;	// Arriba
			case 6:KeybY= 1;break;	// Abajo
			case 5:KeybX= 1;break;	// Derecha
			case 2:KeybX=-1;break;	// Izquierda
			case 8:KeybB=5;break;	// Fuego
		}*/


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

			case Canvas.KEY_NUM5:
			case -7:
				KeybB=5;
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

			case Canvas.KEY_NUM0:
			case -6:
				KeybB=10;
				break;
		}
	}

	KeybX2 = KeybX1;	// Keys del Frame Anterior
	KeybY2 = KeybY1;
	KeybB2 = KeybB1;

	KeybX1 = KeybX;		// Keys del Frame Actual
	KeybY1 = KeybY;
	KeybB1 = KeybB;

	KeybB = 0;
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

int GameX=0;
int GameY=0;
int GameSizeX=96;
int GameSizeY=96;
int GameMaxX=176;
int GameMaxY=208;

int GameStatus=0;

int GameSound=1;
int GameVibra=1;

int MenuNow=0;

// -------------------
// Game INI
// ===================

public void GameINI()
{
	PrefsINI();
	gc.SoundINI();

	GameSizeX=gc.getWidth();
	GameSizeY=gc.getHeight();

	if (GameSizeX < 130) {GameMaxY=96;}

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


	Parco(GameSizeX, GameSizeY);
}

// -------------------
// Game END
// ===================
/*
public void GameEND()
{
}
*/
// -------------------
// Game SET
// ===================
/*
public void GameSET()
{
}
*/
// -------------------
// Game RUN
// ===================

int FinalPaint=0;
int FinalExit=0;

int PortadaPaint;

int PaintRGB;
int Paint_ON=0;

public void GameRUN()
{
	switch (GameStatus)
	{
	case 0:
		GameStatus=80;
	break;


	case 80:	// Intro
		PaintRGB=0;
		Paint_ON=1;

		IntroSET();
		GameStatus++;
	break;

	case 81:
		if ( IntroRUN() )
		{
		IntroEND();
		GameStatus=10;
		}
	break;



	case 10:
		gc.PortadaGFX_Load();
		GameStatus++;
	case 11:
		PortadaPaint=1;
		if (MenuNow==1 || KeybB1!=0 && KeybB2==0) {MenuNow=0;MarcoSET(0);}
	break;

	case 12:
		PortadaPaint=0;
		gc.PortadaGFX_Kill();
		GameStatus=20;
	break;


	case 20:
		gc.JugarGFX_Load();

		PaintRGB=0;
		Paint_ON=1;

		JugarINI();		// Empezamos a Jugar desde 0
		FaseINI();
		GameStatus++;
	break;

	case 21:
		JugarSET();		// Jugamos una nueva Vida
		GameStatus++;

	case 22:			// Jugamos una nueva Fase
		FaseSET();
		PanelSET(0);	// Level X
		WaitSET(2*1000, GameStatus+1);
	break;

	case 23:
		PanelRES();

//		Cheats();	// Solo para version DEMO

		JugarRUN();

		if (KeybB1==10 && KeybB2!=10) {MarcoSET(30);}

		if (FaseExit!=0) {GameStatus++;}
	break;

	case 24:
		FaseEND();

		if (FaseExit==1 && ++JugarLevel > 5)
		{
		JugarLevel=1;
		gc.JugarGFX_Kill();
		GameStatus=50;
		break;
		}

		if (FaseExit==2)
		{
			if (JugarVidas==0)
			{
			GameStatus=30;
			break;
			} else {
			JugarVidas--;
			GameStatus=21;
			break;
			}
		}

		GameStatus=22;
	break;


	case 30:
		PanelSET(2);	// Game Over
		WaitSET(3*1000, GameStatus+1);
	break;

	case 31:
		PanelRES();
		GameStatus=10;

		gc.JugarGFX_Kill();
	break;





// Final
// -----
	case 50:
		gc.FinalINI_Gfx();
		gc.SoundSET(1,1);
		FinalExit=0;
		PaintRGB=0;
		Paint_ON=1;
		GameStatus++;
	case 51:
		if (FinalExit==0) {FinalPaint=1;break;}
		gc.SoundRES();
		gc.FinalEND_Gfx();
		GameStatus=10;
	break;





	case 900:
		MarcoRUN();
	break;


	case 1000:
		WaitRUN();
	break;
	}




}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

int JugarVidas;
int JugarLevel;

// -------------------
// Jugar INI
// ===================

public void JugarINI()
{
	JugarLevel=1;
	JugarVidas=3;
}

// -------------------
// Jugar SET
// ===================

public void JugarSET()
{
	ProtStars=3;
}

// -------------------
// Jugar RUN
// ===================

public void JugarRUN()
{
	FaseRUN();
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Fase - Engine
// ====================
// ********************

int FasePaint=0;

int FaseSizeX=31;
int FaseSizeY=31;

int FaseExit;

byte[] FaseMap  = new byte[FaseSizeX*FaseSizeY];

int[] FluorTab = new int[] {400,294,569,280,120,750,200,376,160,483,812,436,275,476};
int FluorPos;

int[] GotaTab = new int[] {1,3,2};
int GotaPos;

// --------------------
// Fase INI
// ====================

public void FaseINI()
{
	LoadFile(AnimMap, "/data/Anims.map");
}

// --------------------
// Fase SET
// ====================

byte[] Fase;

public void FaseSET()
{
	FasePaint=1;

	FaseExit=0;

	LoadFile(FaseMap, "/data/Stage"+JugarLevel+".map");


	AnimSpriteINI();
	AnimTileINI();


	StarINI();
	GotaINI();
	VaporINI();
	PistoleroINI();
	BalaINI();
	LanzadorINI();
	GranadaINI();

	AscensorINI();

	ToroINI();

	FaseEnemySET();


	ProtSET();


// Analizamos FASE
// ---------------
	for (int i=0 ; i < FaseSizeX * FaseSizeY ; i++)
	{
	switch (FaseMap[i])
	{
	case (byte)0x1F:	// Luz Roja
		AnimTileSET(i, 0,4, 1,1, 2, 8, 0x01);
	break;

	case (byte)0x16:	// Fluorescente
		if (FaseMap[i+1]==(byte)0x17)
		{
		AnimTileSET(i, 0,2, 2,2, 4, 0, 0x01, FluorTab[FluorPos++]);
		} else {
		AnimTileSET(i, 0,0, 3,2, 4, 0, 0x01, FluorTab[FluorPos++]);
		}
		if (FluorPos==FluorTab.length) {FluorPos=0;}
	break;

	case (byte)0x23:	// Vapor D
		VaporSET(((i%FaseSizeX)*8)+6, (i/FaseSizeX)*8, 2);
	break;

	case (byte)0x24:	// Vapor I
		VaporSET(((i%FaseSizeX)*8)-14,(i/FaseSizeX)*8, 0);
	break;

	case (byte)0xFF:	// Gota
		FaseMap[i]=0x22;
		GotaSET( (i%FaseSizeX)*8, ((i/FaseSizeX)*8)+4, GotaTab[GotaPos++] );
		if (GotaPos==GotaTab.length) {GotaPos=0;}
	break;
	}
	}
}



public void FaseEnemySET()
{
	switch (JugarLevel)
	{
	case 1:
		PistoleroSET(18*8,26*8, 1);
		PistoleroSET(16*8,20*8,-1);
		PistoleroSET(24*8,14*8, 1);
		PistoleroSET( 6*8,14*8, 1);
		PistoleroSET(17*8, 8*8,-1);

		LanzadorSET(20*8, 8*8,-1);
		LanzadorSET( 1*8, 8*8, 1);
	break;

	case 2:
		PistoleroSET(18*8,26*8,-1);
		PistoleroSET(12*8, 2*8,-1);
		PistoleroSET(18*8, 8*8,-1);
		PistoleroSET(22*8,20*8, 1);

		LanzadorSET(27*8, 2*8,-1);
		LanzadorSET( 6*8,14*8, 1);
	break;

	case 3:
		PistoleroSET(12*8,26*8,-1);
		PistoleroSET(23*8,11*8, 1);

		LanzadorSET(12*8,11*8,-1);
		LanzadorSET( 1*8,18*8, 1);
		LanzadorSET(27*8, 2*8,-1);

		AscensorSET( 6*8, 15*8,  14*8);
	break;

	case 4:
		PistoleroSET(23*8,26*8, 1);
		PistoleroSET(10*8,17*8, 1);

		LanzadorSET( 1*8, 2*8, 1);

		AscensorSET(25*8,  6*8,  23*8);
	break;

	case 5:
		LanzadorSET( 1*8, 7*8, 1);
		LanzadorSET(27*8, 7*8,-1);

		AscensorSET( 6*8,28*8,   0*8);

		ToroSET(20*8, 11*8);
	break;
	}
}


// --------------------
// Fase END
// ====================

public void FaseEND()
{
	FasePaint=0;

	StarEND();
	GotaEND();
	VaporEND();
	PistoleroEND();
	BalaEND();
	LanzadorEND();
	GranadaEND();

	AscensorEND();

	ToroEND();

	ProtEND();

	AnimSpriteEND();
	AnimTileEND();
}


// --------------------
// Fase RUN
// ====================

public void FaseRUN()
{
	AnimSpriteRUN();
	AnimTileRUN();

	StarRUN();
	GotaRUN();
	VaporRUN();
	PistoleroRUN();
	BalaRUN();
	LanzadorRUN();
	GranadaRUN();

	AscensorRUN();

	ToroRUN();

	ProtRUN();
}


// --------------------
// Fase Check Tiles
// ====================

int ChaPatDir;

public int CheckTile(int X, int Y)
{
	if (Y < 0) {Y=0;} else if (Y >= FaseSizeY*8) {Y=(FaseSizeY-1)*8;}
	if (X < 0) {X=0;} else if (X >= FaseSizeX*8) {X=(FaseSizeX-1)*8;}

	ChaPatDir = ((Y/8)*FaseSizeX)+(X/8);
	return (int)TilDatTab[FaseMap[ChaPatDir]];
}


public int CheckTile(int X, int Y, int SizeX, int SizeY, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y+SizeY >= FaseSizeY*8) {Y=(FaseSizeY-1)*8;SizeY=1;}
	if (X < 0) {X=0;} else if (X+SizeX >= FaseSizeX*8) {X=(FaseSizeX-1)*8;SizeX=1;}

int TabXY = ((Y/8)*FaseSizeX)+(X/8);
int TabSizeX = ((X+SizeX)/8)-(X/8);
int TabSizeY = ((Y+SizeY)/8)-(Y/8);

int Dato=0;

	for (; TabSizeY>-1 ; TabSizeY--)
	{
		for (int i=TabSizeX ; i>-1 ; i--)
		{
			Dato |= TilDatTab[FaseMap[TabXY+i]];
		}
	TabXY+=FaseSizeX;
	}

	return (int)(Dato & Mask);
}


final short TilDatTab[] = new short[] {
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F

0x000,0x000,0x000,0x001,0x000,0x002,0x000,0x002,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 0
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 1
0x003,0x003,0x000,0x000,0x000,0x000,0x000,0x002,0x002,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 2
0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 3
0x000,0x010,0x020,0x030,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 4
//0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 5
//0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 6
//0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 7
};

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Prot - Engine
// ===================
// *******************

int ProtX=2*8, ProtY=26*8;
int ProtMode;
int ProtAnim;
int ProtFrame;
int ProtSide=0;
int ProtBaseX;
int ProtBaseY;

int ProtStars;

int[] ProtFrameTab = new int[] {
	 0,12,	// 0 - Caminar
	57,69,	// 1 - Agachar
	24,36,	// 2 - Saltar
	72,84,	// 3 - Escala
	28,40,	// 4 - Caer
	51,63,	// 5 - Sable
	48,60,	// 6 - Star
	29,41,	// 7 - Morir
};


// -------------------
// Prot - SET
// ===================

public void ProtSET()
{
	ProtSableAnim=-1;

	ProtMode=0;
	ProtSide=0;
	ProtAnim = AnimSpriteSET(-1, 30,30, ProtFrameTab[(ProtMode*2)+ProtSide], 12, 1, 0x21);

	switch (JugarLevel)
	{
	case 1:
	ProtX= 2*8;
	ProtY=26*8;
	break;

	case 2:
	ProtX= 8*8;
	ProtY=8*26;
	ProtSide=1;
	AnimSprites[ProtAnim][asFrameIni]=ProtFrameTab[(ProtMode*2)+ProtSide];
	break;

	case 3:
	ProtX= 2*8;
	ProtY= 1*8;
	ProtEscalaSET(0);	// Escala
	break;

	case 4:
	ProtX= 2*8;
	ProtY=26*8;
	break;

	case 5:
	ProtX= 7*8;
	ProtY=24*8;
	break;
	}

	AnimSprites[ProtAnim][asCoorX]=ProtX;
	AnimSprites[ProtAnim][asCoorY]=ProtY;
}


// -------------------
// Prot - END
// ===================

public void ProtEND()
{
}


// -------------------
// Prot - RUN
// ===================

public void ProtRUN()
{
	if (ProtMode!=7 && ProtKilledCheck(ProtX+2, ProtY+((ProtMode!=1)?8:12), 12,((ProtMode!=1)?22:18)) ) {ProtMorirSET();}

	ProtMover();

	ProtObjetos();

	if (ProtX >= FaseSizeX*8 || ProtY <= -24 || ProtY >= FaseSizeY*8) {FaseExit=1;}

	AnimSprites[ProtAnim][asCoorX]=ProtX;
	AnimSprites[ProtAnim][asCoorY]=ProtY;
}


// -------------------
// ProtKilledCheck
// ===================

public boolean ProtKilledCheck(int X, int Y, int SizeX, int SizeY)
{

// Balas
// -----
	for (int i=0 ; i<BalaC ; i++)
	{
	int Anim=Balas[BalaP[i]].Anim;
		if ((AnimSprites[Anim][asCoorX]+ 1 > X+SizeX) ||
			(AnimSprites[Anim][asCoorX]+ 6 < X) ||
			(AnimSprites[Anim][asCoorY]+ 3 > Y+SizeY) ||
			(AnimSprites[Anim][asCoorY]+ 6 < Y))
		{} else {
		Balas[BalaP[i]].Time=0;
		return (true);
		}
	}

// Granadas (Explosion)
// --------------------
	for (int i=0 ; i<GranadaC ; i++)
	{
	if (Granadas[GranadaP[i]].Mode!=1) {continue;}
	int Anim=Granadas[GranadaP[i]].Anim;
		if ((AnimSprites[Anim][asCoorX]+ 4 > X+SizeX) ||
			(AnimSprites[Anim][asCoorX]+12 < X) ||
			(AnimSprites[Anim][asCoorY]+ 4 > Y+SizeY) ||
			(AnimSprites[Anim][asCoorY]+12 < Y))
		{} else {
		return (true);
		}
	}

// Vapor
// -----
	for (int i=0 ; i<VaporC ; i++)
	{
	if (Vapors[VaporP[i]].Anim==-1) {continue;}
	int Anim=Vapors[VaporP[i]].Anim;
		if ((AnimSprites[Anim][asCoorX]+ 2 > X+SizeX) ||
			(AnimSprites[Anim][asCoorX]+14 < X) ||
			(AnimSprites[Anim][asCoorY]+ 2 > Y+SizeY) ||
			(AnimSprites[Anim][asCoorY]+10 < Y))
		{} else {
		return (true);
		}
	}

// Gota
// -----
	for (int i=0 ; i<GotaC ; i++)
	{
	if (Gotas[GotaP[i]].Mode!=1) {continue;}
	int Anim=Gotas[GotaP[i]].Anim;
		if ((AnimSprites[Anim][asCoorX]+ 5 > X+SizeX) ||
			(AnimSprites[Anim][asCoorX]+ 5 < X) ||
			(AnimSprites[Anim][asCoorY]+ 2 > Y+SizeY) ||
			(AnimSprites[Anim][asCoorY]+ 8 < Y))
		{} else {
		return (true);
		}
	}

// Toro
// -----
		if ((ToroAnim==-1) ||
			(AnimSprites[ToroAnim][asCoorX]+ 8 > X+SizeX) ||
			(AnimSprites[ToroAnim][asCoorX]+44 < X) ||
			(AnimSprites[ToroAnim][asCoorY]+32 > Y+SizeY) ||
			(AnimSprites[ToroAnim][asCoorY]+48 < Y))
		{} else {
		return (true);
		}


	return(false);
}

// -------------------
// Prot Objetos
// ===================

public void ProtObjetos()
{
	int Objeto=CheckTile(ProtX+8, ProtY+26) & 0x0F0;

	if (FaseMap[ChaPatDir]==0x44)
	{
	FaseMap[ChaPatDir]=0x48;
	FaseMap[ChaPatDir+1]=0x49;
	FaseMap[ChaPatDir+FaseSizeX]=0x4A;
	FaseMap[ChaPatDir+FaseSizeX+1]=0x4B;

	AnimTileSET(25, 7,4, 5,1, 1, 1, 0x00);
	}
	else if (FaseMap[ChaPatDir]==0x45)
	{
	FaseMap[ChaPatDir-1]=0x48;
	FaseMap[ChaPatDir]=0x49;
	FaseMap[ChaPatDir+FaseSizeX-1]=0x4A;
	FaseMap[ChaPatDir+FaseSizeX]=0x4B;

	AnimTileSET(25, 7,4, 5,1, 1, 1, 0x00);
	}

	if (Objeto==0) {return;}

	FaseMap[ChaPatDir]=0x15;

	if (Objeto==0x010) {ProtStars+=5;if (ProtStars>9) {ProtStars=9;}}
	if (Objeto==0x020) {JugarVidas++;if (JugarVidas>9) {JugarVidas=9;}}

	gc.SoundSET(2,1);
}

// -------------------
// Prot - Mover
// ===================

public void ProtMover()
{
	switch (ProtMode)
	{
	case 0:
		ProtCaminarRUN();	// Caminar
	break;

	case 1:
		ProtAgacharRUN();	// Agachar
	break;

	case 2:
		ProtSaltarRUN();	// Saltar
	break;

	case 3:
		ProtEscalaRUN();	// Escala
	break;

	case 4:
		ProtCaerRUN();		// Caer
	break;

	case 5:
		ProtSableRUN();		// Sable
	break;

	case 6:
		ProtStarRUN();		// Star
	break;

	case 7:
		ProtMorirRUN();		// Morir
	break;
	}

	if ( AscensorCheck() && ProtMode!=2 && CheckTile(ProtX+2+ProtBaseX, ProtBaseY, 11,31, 0x01) == 0 ) {ProtY=ProtBaseY;ProtX+=ProtBaseX;}
}


// -------------------
// Prot Caminar SET
// ===================

public void ProtCaminarSET()
{
	ProtMode=0;
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)+ProtSide], 12, 1, 0x21);
	if (!AscensorCheck() )
	{
	ProtY&=-8;
	}
}

public void ProtCaminarRUN()
{
	if (KeybB1==5 && KeybB2!=5)	// Disparo??
	{
		if (ProtStars!=0)
		{
		ProtStarSET();
		} else {
		ProtSableSET();
		}
	return;
	}


	if ((CheckTile(ProtX+2,ProtY+32, 11,0, 0x01) == 0)	// Suelo??
	&&	(!AscensorCheck() ))							// Sobre Ascensor??
	{
	ProtCaerSET();
	return;
	}

	if (KeybY1==-1 && KeybY2!=-1)	// Saltar??
	{
		if ( ProtEscalaCheck(ProtX, ProtY+16) )	// Escala??
		{
		ProtEscalaSET(0);
		} else {
		ProtSaltarSET();
		}
		return;
	}
	else if (KeybY1==1 && KeybY2!=1)		// Agachar??
	{
		if ( ProtEscalaCheck(ProtX, ProtY+32) )	// Escala??
		{
		ProtEscalaSET(2);
		} else {
		ProtAgacharSET();
		}
		return;
	}


	if (KeybX1==-1 && (CheckTile(ProtX,   ProtY, 0,31, 0x01) == 0))	// Izquierda
		{
		ProtSide=1;
		ProtX-=2;
		AnimSprites[ProtAnim][asFrameIni]=ProtFrameTab[(ProtMode*2)+ProtSide];
		AnimSprites[ProtAnim][asPause]=1;
		}
	else
	if (KeybX1== 1 && (CheckTile(ProtX+14,ProtY, 0,31, 0x01) == 0))	// Derecha
		{
		ProtSide=0;
		ProtX+=2;
		AnimSprites[ProtAnim][asFrameIni]=ProtFrameTab[(ProtMode*2)+ProtSide];
		AnimSprites[ProtAnim][asPause]=1;
		}
}


// -------------------
// Prot Saltar SET
// ===================

int[] ProtSaltoTab = new int[] {-8,-8,-7,-6,-5,-4,-3,-3,-2,-2,-1,-1,-1,0,0,0};
int ProtSaltoPos=0;

public void ProtSaltarSET()
{
	ProtMode=2;
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)+ProtSide], 4, 1, 0x02);
	ProtSaltoPos=0;
}

public void ProtSaltarRUN()
{
	if (KeybB1==5 && KeybB2!=5 && ProtStars!=0)	// Disparo??
	{
	StarSET(ProtX+6-(6*ProtSide),ProtY+12);
	}

	if (KeybX1==-1 && (CheckTile(ProtX,   ProtY, 0,31, 0x01) == 0)) {ProtX-=2;}	// Izquierda
	else
	if (KeybX1== 1 && (CheckTile(ProtX+14,ProtY, 0,31, 0x01) == 0)) {ProtX+=2;}	// Derecha

	ProtY+=ProtSaltoTab[ProtSaltoPos];
	if (++ProtSaltoPos == ProtSaltoTab.length) {ProtCaerSET(); return;}

	if ( (CheckTile(ProtX+2,ProtY, 11,0, 0x01) != 0))	// Techo??
	{
	ProtCaerSET();
	return;
	}

}


// -------------------
// Prot Agachar SET
// ===================

public void ProtAgacharSET()
{
	ProtMode=1;
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)+ProtSide], 3, 0, 0x02);
}

public void ProtAgacharRUN()
{
	if (KeybY1!=1)
	{
	ProtCaminarSET();
	}
}


// -------------------
// Prot Escala SET
// ===================

int ProtEscalaMode;

public void ProtEscalaSET(int Mode)
{
	ProtMode=3;
	if (Mode==0)
	{
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)], 10, 1, 0x21);
	}
	else if (Mode==2)
	{
	AnimSpriteSET(ProtAnim, ProtX,ProtY, 84, 11, 0, 0x12);
	}
	ProtEscalaMode=Mode;
}

public void ProtEscalaRUN()
{
	switch (ProtEscalaMode)
	{
	case 0:

		if (KeybY1==-1)
		{
			if ( ProtEscalaCheck(ProtX, ProtY+10) )	// Escala??
			{
			ProtY-=2;
			AnimSprites[ProtAnim][asPause]=1;
			} else {
				if (FaseMap[ChaPatDir] != 0x03)
				{
				AnimSpriteSET(ProtAnim, ProtX,ProtY, 84, 11, 0, 0x02);
				ProtEscalaMode=1;
				}
			}
		}
		else if (KeybY1==1)
		{
			if ( ProtEscalaCheck(ProtX, ProtY+32) )	// Escala??
			{
			ProtY+=2;
			AnimSprites[ProtAnim][asPause]=1;
			} else {
			ProtCaminarSET();
			}
		}


		if (KeybX1==-1)
		{
			ProtSide=1;
			if ( ProtEscalaCheck(ProtX-2, ProtY+10) )	// Escala??
			{
			ProtX-=2;
			AnimSprites[ProtAnim][asPause]=1;
			} else {
				if (CheckTile(ProtX,   ProtY, 0,31, 0x01) == 0)	// Izquierda
				{
				ProtX-=2;
				ProtCaerSET();
				}
			}
		}
		else if (KeybX1==1)
		{
			ProtSide=0;
			if ( ProtEscalaCheck(ProtX+2, ProtY+10) )	// Escala??
			{
			ProtX+=2;
			AnimSprites[ProtAnim][asPause]=1;
			} else {
				if (CheckTile(ProtX+14,ProtY, 0,31, 0x01) == 0)	// Derecha
				{
				ProtX+=2;
				ProtCaerSET();
				}
			}
		}
	break;

	case 1:

		if (AnimSprites[ProtAnim][asPause]==-1)
		{
		ProtCaminarSET();
		} else {
		ProtY-=2;
			if (KeybY1==1)
			{
			AnimSprites[ProtAnim][asSide]=-1;
			ProtEscalaMode=2;
			}
		}
	break;

	case 2:

		if (AnimSprites[ProtAnim][asPause]==-1)
		{
		ProtEscalaSET(0);
		} else {
		ProtY+=2;
			if (KeybY1==-1)
			{
			AnimSprites[ProtAnim][asSide]=1;
			ProtEscalaMode=1;
			}
		}
	break;
	}
}

public boolean ProtEscalaCheck(int X, int Y)
{
	return ( ((CheckTile(X+4,Y) & 0x02) == 2) && ((CheckTile(X+10,Y) & 0x02) == 2) );	// Escala??
}


// -------------------
// Prot Caer SET
// ===================

int[] ProtCaerTab = new int[] {1,1,1,2,2,3,4,5,6};
int ProtCaerPos=0;

public void ProtCaerSET()
{
	ProtMode=4;
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)+ProtSide], 1, 1, 0x01);
	ProtCaerPos=0;
}

public void ProtCaerRUN()
{
	if (KeybB1==5 && KeybB2!=5 && ProtStars!=0)	// Disparo??
	{
	StarSET(ProtX+6-(6*ProtSide),ProtY+12);
	}

	if (KeybY1==-1 && ProtEscalaCheck(ProtX, ProtY+16) )	// Escala??
	{ProtEscalaSET(0);}
	else
	if (KeybX1==-1 && (CheckTile(ProtX,   ProtY, 0,31, 0x01) == 0)) {ProtX-=2;}	// Izquierda
	else
	if (KeybX1== 1 && (CheckTile(ProtX+14,ProtY, 0,31, 0x01) == 0)) {ProtX+=2;}	// Derecha

	ProtY+=ProtCaerTab[ProtCaerPos];
	if (++ProtCaerPos == ProtCaerTab.length) {ProtCaerPos--;}

	if ((CheckTile(ProtX+2,ProtY+32, 11,0, 0x01) != 0)	// Suelo??
	||	(AscensorCheck() ))								// Sobre Ascensor?
		{
		ProtCaminarSET();
		return;
		}
}


// -------------------
// Prot Sable SET
// ===================

int ProtSableAnim=-1;
int ProtSableCnt;

public void ProtSableSET()
{
	ProtMode=5;
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)+ProtSide], 3, 1, 0x02);
	ProtSableCnt=0;
}

public void ProtSableRUN()
{
	if (ProtSableCnt++ == 1*2)
	{
	ProtSableAnim = AnimSpriteSET(-1, ProtX+16-(32*ProtSide),ProtY, 54+(ProtSide*12), 3, 1, 0x00);
	}
	else if (ProtSableCnt == 4*2)
	{
	ProtSableAnim=-1;
	ProtCaminarSET();
	}
}

public boolean SableCheck(int X, int Y, int SizeX, int SizeY)
{
	if (ProtMode==5 && ProtSableAnim!=-1)
	{
		if ((ProtX+((ProtSide==0)? 8:-12) > X+SizeX) ||
			(ProtX+((ProtSide==0)?28: 12) < X) ||
			(ProtY+ 8 > Y+SizeY) ||
			(ProtY+24 < Y))
		{} else {
		return (true);
		}
	}
	return (false);
}


// -------------------
// Prot Star SET
// ===================

public void ProtStarSET()
{
	ProtMode=6;
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)+ProtSide], 3, 0, 0x12);
}

public void ProtStarRUN()
{
	if (AnimSprites[ProtAnim][asPause]==-1)
	{
	StarSET(ProtX+6-(6*ProtSide),ProtY+12);
	ProtCaminarSET();
	}
}


// -------------------
// Prot Morir SET
// ===================

int MorirTime;

public void ProtMorirSET()
{
	if (CheatInmune!=0) {return;}

	ProtMode=7;
	AnimSpriteSET(ProtAnim, ProtX,ProtY, ProtFrameTab[(ProtMode*2)+ProtSide], 7, 2, 0x02);
	MorirTime=70;

	gc.SoundSET(0,1);
	gc.VibraSET(250);
}

public void ProtMorirRUN()
{
	if (CheckTile(ProtX+2,ProtY+32, 11,0, 0x01) == 0)	// Suelo??
	{
	ProtY+=2;
	}

	if (--MorirTime < 0) {FaseExit=2;}
}

// <=- <=- <=- <=- <=-











// *******************
// -------------------
// Anim Tile - Engine
// ===================
// *******************

final int AnimSizeX=12;
final int AnimSizeY=7;
byte[] AnimMap = new byte[AnimSizeX * AnimSizeY];

int atSour=0;
int atDest=1;
int atSizeX=2;
int atSizeY=3;
int atFrames=4;
int atFrames2=5;
int atFrameAct=6;
int atSpeed=7;
int atSpeedCnt=8;
int atMode=9;
int atSide=10;
int atWait=11;

// --------------
// Anim Tile Play
// ==============

public boolean AnimTilePlay(int at)
{
	if (AnimTiles[at][atSpeedCnt] > 0) {AnimTiles[at][atSpeedCnt]--; return(false);}

	AnimTiles[at][atSpeedCnt]=AnimTiles[at][atSpeed];

	int Source=AnimTiles[at][atSour]+(AnimTiles[at][atSizeX]*AnimTiles[at][atFrameAct]);
	int Destin=AnimTiles[at][atDest];

		for (int t=0 ; t<AnimTiles[at][atSizeY] ; t++ )
		{
			for (int i=0 ; i<AnimTiles[at][atSizeX] ; i++ )
			{
				FaseMap[Destin+i] = AnimMap[Source+i];
			}
		Source+=AnimSizeX;
		Destin+=FaseSizeX;
		}

	switch (AnimTiles[at][atMode])
	{
	case 0:		// Modo Play 1 sola vez
		AnimTiles[at][atFrameAct]++;
		if (--AnimTiles[at][atFrames] == 0) {return(true);}
		break;
	
	case 1:		// Modo Play Loop
		AnimTiles[at][atFrameAct]++;
		if (--AnimTiles[at][atFrames] == 0)
		{
		AnimTiles[at][atFrames]=AnimTiles[at][atFrames2];
		AnimTiles[at][atFrameAct]=0;
		AnimTiles[at][atSpeedCnt]+=AnimTiles[at][atWait];
		}
		break;

	case 2:		// Modo Ping-Pong con Play 1 sola vez
		AnimTiles[at][atFrameAct]+=AnimTiles[at][atSide];
		if (--AnimTiles[at][atFrames] == 0)
		{
			if (AnimTiles[at][atSide] ==  1)
				{
				AnimTiles[at][atFrames]=AnimTiles[at][atFrames2]-1;
				AnimTiles[at][atFrameAct]-=2;
				AnimTiles[at][atSide]=-1;}
			else {return(true);}
		}
		break;

	case 3:		// Modo Ping-Pong con Play Loop
		AnimTiles[at][atFrameAct]+=AnimTiles[at][atSide];
		if (--AnimTiles[at][atFrames] == 0)
		{
			if (AnimTiles[at][atSide] == 1)
				{
				AnimTiles[at][atFrames]=AnimTiles[at][atFrames2]-1;
				AnimTiles[at][atFrameAct]-=2;
				AnimTiles[at][atSide]=-1;
				}
			else
				{
				AnimTiles[at][atFrames]=AnimTiles[at][atFrames2]-1;
				AnimTiles[at][atFrameAct]=1;
				AnimTiles[at][atSide]=1;
				}
		}
		break;
	}
	
	return (false);
}


// -------------
// Anim Tile SET
// =============

public int AnimTileSET(int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode)
{
	if (AnimTileC == AnimTileMAX) {return(-1);}

	int i=AnimTileP[AnimTileC++];

	AnimTiles[i] = new int[12];

	AnimTiles[i][atDest]=Dest;
	AnimTiles[i][atSour]=(CoorY*AnimSizeX)+CoorX;
	AnimTiles[i][atSizeX]=SizeX;
	AnimTiles[i][atSizeY]=SizeY;
	AnimTiles[i][atFrames]=Frames;
	AnimTiles[i][atFrames2]=Frames;
	AnimTiles[i][atFrameAct]=0;
	AnimTiles[i][atSpeed]=Speed;
	AnimTiles[i][atSpeedCnt]=0;
	AnimTiles[i][atMode]=Mode;
	AnimTiles[i][atSide]=1;

	return (i);
}


public int AnimTileSET(int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode, int Wait)
{
	int i=AnimTileSET(Dest, CoorX, CoorY, SizeX, SizeY, Frames, Speed, Mode);
	if (i != -1)
	{
	AnimTiles[i][atWait]=Wait;
	}
	return(i);
}


// -------------
// Anim Tile INI
// =============

final int AnimTileMAX=32;
int AnimTileC;
int[] AnimTileP;
int[][] AnimTiles;

public void AnimTileINI()
{
	AnimTileC = 0;
	AnimTileP = new int[AnimTileMAX];
	AnimTiles = new int[AnimTileMAX][];
	for (int i=0 ; i<AnimTileMAX ; i++) {AnimTileP[i]=i;}
}

// -------------
// Anim Tile END
// =============

public void AnimTileEND()
{
	AnimTiles = null;
	AnimTileP = null;
	AnimTileC = 0;
}

// -------------
// Anim Tile RUN
// =============

public void AnimTileRUN()
{
	for (int i=0 ; i<AnimTileC ; i++)
	{
	if ( AnimTilePlay(AnimTileP[i]) )
		{
		int t=AnimTileP[i];
		AnimTiles[t] = null;
		AnimTileP[i] = AnimTileP[--AnimTileC];
		AnimTileP[AnimTileC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Anim Sprite - Engine
// ====================
// ********************

int asCoorX=0;
int asCoorY=1;
int asFrameIni=2;
int asFrames=3;
int asFrameAct=4;
int asSpeed=5;
int asSpeedCnt=6;
int asMode=7;
int asSide=8;
int asPause=9;
int asBank=10;

// ----------------
// Anim Sprite Play
// ================

public boolean AnimSpritePlay(int as)
{
	if (AnimSprites[as][asPause] != 0) {if (AnimSprites[as][asPause]==-1)	{return(false);} else {AnimSprites[as][asPause]=-1;}}

	if (AnimSprites[as][asFrames] == 0) {return(true);}

	if (AnimSprites[as][asSpeedCnt] > 0) {AnimSprites[as][asSpeedCnt]--; return(false);}

	AnimSprites[as][asSpeedCnt]=AnimSprites[as][asSpeed];

	AnimSprites[as][asFrameAct]+=AnimSprites[as][asSide];

	if (AnimSprites[as][asFrameAct] >= 0 && AnimSprites[as][asFrameAct] < AnimSprites[as][asFrames]) {return(false);}


	switch (AnimSprites[as][asMode] & 0x03)
	{
	case 0:		// Auto-Destruct
		return(true);
	
	case 1:		// Loop
		if (AnimSprites[as][asFrameAct] < 0)
		{
		AnimSprites[as][asFrameAct]=AnimSprites[as][asFrames]-1;
		} else {
		AnimSprites[as][asFrameAct]=0;
		}
	break;

	case 2:		// Stop-Fin
		AnimSprites[as][asFrameAct]-=AnimSprites[as][asSide];
		AnimSprites[as][asPause]=-1;
	break;

	case 3:		// Stop-Ini
		AnimSprites[as][asFrameAct]=0;
		AnimSprites[as][asPause]=-1;
	break;
	}

	return (false);
}


// ---------------
// Anim Sprite SET
// ===============

public int AnimSpriteSET(int i, int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	if (i<0)
	{
	if (AnimSpriteC == AnimSpriteMAX) {return(-1);}
	i=AnimSpriteP[AnimSpriteC++];
	AnimSprites[i] = new int[11];
	}

	AnimSprites[i][asCoorX]=CoorX;
	AnimSprites[i][asCoorY]=CoorY;
	AnimSprites[i][asFrameIni]=FrameIni;
	AnimSprites[i][asFrames]=Frames; AnimSprites[i][asFrameAct]=0;
	AnimSprites[i][asSpeed]=Speed; AnimSprites[i][asSpeedCnt]=Speed;
	AnimSprites[i][asMode]=Mode;
	AnimSprites[i][asPause]=((Mode&0x20)==0)?0:-1;
	AnimSprites[i][asBank]=(Mode>>8) & 0xF;

	if ((Mode&0x10)==0)
	{
	AnimSprites[i][asSide]=1;
	} else {
	AnimSprites[i][asSide]=-1;
	AnimSprites[i][asFrameAct]=Frames-1;
	}

	return (i);
}

// ---------------
// Anim Sprite INI
// ===============

final int AnimSpriteMAX=128;
int AnimSpriteC;
int[] AnimSpriteP;
int[][] AnimSprites;

public void AnimSpriteINI()
{
	AnimSpriteC = 0;
	AnimSpriteP = new int[AnimSpriteMAX];
	AnimSprites = new int[AnimSpriteMAX][];
	for (int i=0 ; i<AnimSpriteMAX ; i++) {AnimSpriteP[i]=i;}
}

// ---------------
// Anim Sprite RES
// ===============

public void AnimSpriteRES(int i)
{
	AnimSprites[i][asFrames]=0;
	AnimSprites[i][asPause]=0;
}

// ---------------
// Anim Sprite END
// ===============

public void AnimSpriteEND()
{
	AnimSprites = null;
	AnimSpriteP = null;
	AnimSpriteC = 0;
}

// ---------------
// Anim Sprite RUN
// ===============

public void AnimSpriteRUN()
{
	for (int i=0 ; i<AnimSpriteC ; i++)
	{
	if ( AnimSpritePlay(AnimSpriteP[i]) )
		{
		int t=AnimSpriteP[i];
		AnimSprites[t] = null;
		AnimSpriteP[i] = AnimSpriteP[--AnimSpriteC];
		AnimSpriteP[AnimSpriteC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Star - Engine
// ====================
// ********************

int[] StarTab = new int[] {0,0,0,0,0,0,0,1,1,1,1,2,2,2,3,4,5};

public class Star
{
int Anim;
int Add;
int Time=30;
int Pos=0;

public Star() {}

// ----------------
// Star Play
// ================

public boolean Play()
{
	if (--Time < 0  || CheckTile(AnimSprites[Anim][asCoorX]+1, AnimSprites[Anim][asCoorY]+1, 5,6, 0x01) != 0)
	{
	AnimSpriteRES(Anim);
	return (true);
	}

	AnimSprites[Anim][asCoorX]+=Add;
	AnimSprites[Anim][asCoorY]+=StarTab[Pos];
	if (++Pos == StarTab.length) {Pos--;}

	return (false);
}

};


// ---------------
// Star SET
// ===============

public int StarSET(int CoorX, int CoorY)
{
	if (StarC == StarMAX) {return(-1);}

	if (ProtStars > 0) {ProtStars--;}

	int Anim = AnimSpriteSET(-1, CoorX,CoorY, 82, 2, 2, 0x01);
	if (Anim==-1) {return (-1);}

	int i=StarP[StarC++];

	Stars[i] = new Star();

	Stars[i].Anim=Anim;
	Stars[i].Add=(ProtSide==0)?6:-6;

	return (i);
}

// ---------------
// Star Check
// ===============

public boolean StarCheck(int X, int Y, int SizeX, int SizeY)
{
	for (int i=0 ; i<StarC ; i++)
	{
	int Anim=Stars[StarP[i]].Anim;
		if ((AnimSprites[Anim][asCoorX]+ 2 > X+SizeX) ||
			(AnimSprites[Anim][asCoorX]+ 6 < X) ||
			(AnimSprites[Anim][asCoorY]+ 2 > Y+SizeY) ||
			(AnimSprites[Anim][asCoorY]+ 6 < Y))
		{} else {
		Stars[StarP[i]].Time=0;
		return (true);
		}
	}
	return(false);
}


// ---------------
// Star INI
// ===============

final int StarMAX=5;
int StarC;
int[] StarP;
Star[] Stars;

public void StarINI()
{
	StarC = 0;
	StarP = new int[StarMAX];
	Stars = new Star[StarMAX];
	for (int i=0 ; i<StarMAX ; i++) {StarP[i]=i;}
}

// ---------------
// Star RES
// ===============
/*
public void StarRES(int i)
{
}
*/
// ---------------
// Star END
// ===============

public void StarEND()
{
	Stars = null;
	StarP = null;
	StarC = 0;
}

// ---------------
// Star RUN
// ===============

public void StarRUN()
{
	for (int i=0 ; i<StarC ; i++)
	{
	if ( Stars[StarP[i]].Play() )
		{
		int t=StarP[i];
		Stars[t] = null;
		StarP[i] = StarP[--StarC];
		StarP[StarC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Gota - Engine
// ====================
// ********************

public class Gota
{
int Anim;
int Mode=0;
int CoorX;
int CoorY;
int Speed;
int Wait=50;

public Gota() {}

// ----------------
// Gota Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		Anim = AnimSpriteSET(-1, CoorX,CoorY, 105, 3, 1, 0x20);
		if (Anim==-1) {Mode=2; break;}
		Mode++;
	break;

	case 1:
		AnimSprites[Anim][asCoorY]+=Speed;
		if ( (CheckTile(AnimSprites[Anim][asCoorX], AnimSprites[Anim][asCoorY]+7) & 0x01) != 0 )
		{
		AnimSprites[Anim][asCoorY]&=-8;
		AnimSprites[Anim][asPause]=0;
		AnimSprites[Anim][asSpeedCnt]=0;
		Mode++;
		}
	break;

	case 2:
		if (--Wait < 0)
		{
		Wait=50;
		Mode=0;
		}
	break;
	}

	return (false);
}

};


// ---------------
// Gota SET
// ===============

public int GotaSET(int X, int Y, int Speed)
{
	if (GotaC == GotaMAX) {return(-1);}

	int i=GotaP[GotaC++];
	Gotas[i] = new Gota();

	Gotas[i].CoorX=X;
	Gotas[i].CoorY=Y;
	Gotas[i].Speed=Speed;

	return (i);
}


// ---------------
// Gota INI
// ===============

final int GotaMAX=8;
int GotaC;
int[] GotaP;
Gota[] Gotas;

public void GotaINI()
{
	GotaC = 0;
	GotaP = new int[GotaMAX];
	Gotas = new Gota[GotaMAX];
	for (int i=0 ; i<GotaMAX ; i++) {GotaP[i]=i;}
}

// ---------------
// Gota RES
// ===============
/*
public void GotaRES(int i)
{
}
*/
// ---------------
// Gota END
// ===============

public void GotaEND()
{
	Gotas = null;
	GotaP = null;
	GotaC = 0;
}

// ---------------
// Gota RUN
// ===============

public void GotaRUN()
{
	for (int i=0 ; i<GotaC ; i++)
	{
	if ( Gotas[GotaP[i]].Play() )
		{
		int t=GotaP[i];
		Gotas[t] = null;
		GotaP[i] = GotaP[--GotaC];
		GotaP[GotaC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Vapor - Engine
// ====================
// ********************

public class Vapor
{
int Anim=-1;
int CoorX;
int CoorY;
int Mode=0;
int Type;
int Wait;

public Vapor() {}

// ----------------
// Vapor Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		Anim = AnimSpriteSET(-1, CoorX,CoorY, 112+Type, 2, 1, 0x01);
		Wait=100;
		Mode++;
	break;	

	case 1:
		if (--Wait < 0)
		{
		Mode=0;
		}
		else if (Wait==40)
		{
		if (Anim!=-1) {AnimSpriteRES(Anim);}
		Anim=-1;
		}
	break;
	}


	return (false);
}

};


// ---------------
// Vapor SET
// ===============

public int VaporSET(int CoorX, int CoorY, int Type)
{
	if (VaporC == VaporMAX) {return(-1);}

	int i=VaporP[VaporC++];

	Vapors[i] = new Vapor();

	Vapors[i].CoorX=CoorX;
	Vapors[i].CoorY=CoorY;
	Vapors[i].Type=Type;

	return (i);
}


// ---------------
// Vapor INI
// ===============

final int VaporMAX=8;
int VaporC;
int[] VaporP;
Vapor[] Vapors;

public void VaporINI()
{
	VaporC = 0;
	VaporP = new int[VaporMAX];
	Vapors = new Vapor[VaporMAX];
	for (int i=0 ; i<VaporMAX ; i++) {VaporP[i]=i;}
}

// ---------------
// Vapor RES
// ===============
/*
public void VaporRES(int i)
{
}
*/
// ---------------
// Vapor END
// ===============

public void VaporEND()
{
	Vapors = null;
	VaporP = null;
	VaporC = 0;
}

// ---------------
// Vapor RUN
// ===============

public void VaporRUN()
{
	for (int i=0 ; i<VaporC ; i++)
	{
	if ( Vapors[VaporP[i]].Play() )
		{
		int t=VaporP[i];
		Vapors[t] = null;
		VaporP[i] = VaporP[--VaporC];
		VaporP[VaporC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Pistolero - Engine
// ====================
// ********************

public class Pistolero
{
int Anim;
int Side;
int Mode=0;
int Cont=100;
int DispCnt=0;

public Pistolero() {}

// ----------------
// Pistolero Play
// ================

public boolean Play()
{
	if (
		StarCheck (AnimSprites[Anim][asCoorX]+((Side==1)?2:10), AnimSprites[Anim][asCoorY]+2, 12, 28)	// Muerte por Star??
	||	SableCheck(AnimSprites[Anim][asCoorX]+((Side==1)?2:10), AnimSprites[Anim][asCoorY]+2, 12, 28)	// Muerte por Sable??
		)
	{
	gc.SoundSET(3,1);
	gc.VibraSET(250);
	AnimSpriteSET(Anim, AnimSprites[Anim][asCoorX],AnimSprites[Anim][asCoorY], ((Side==1)?6:18), 3, 2, 0x100);
	return (true);
	}


	switch (Mode)
	{
	case 0:	// Caminar

		if (--Cont>0 && CheckTile(AnimSprites[Anim][asCoorX]+((Side==1)?24:-1),AnimSprites[Anim][asCoorY], 0,31, 0x01) == 0 )
		{
			AnimSprites[Anim][asCoorX]+=Side;
		} else {
			Side*=-1;Cont=100;
			AnimSprites[Anim][asCoorX]+=((Side==1)?8:-8);
			AnimSprites[Anim][asFrameIni]=((Side==1)?0:12);
		break;
		}

		if (DispCnt!=0) {DispCnt--;} else {DispCnt=25;}	// Intervalo entre disparos

		if (DispCnt==0)
		{
			if ((AnimSprites[Anim][asCoorY]+ 8 < ProtY+24) &&	// Dentro de Zona de tiro??
				(AnimSprites[Anim][asCoorY]+24 > ProtY+ 8) &&
				(AnimSprites[Anim][asCoorX]+12-60 < ProtX+8) &&
				(AnimSprites[Anim][asCoorX]+12+60 > ProtX+8))
			{
				if ((Side== 1 && AnimSprites[Anim][asCoorX]+12 > ProtX+8 ) ||	// Girar para disparar??
					(Side==-1 && AnimSprites[Anim][asCoorX]+12 < ProtX+8 ))
				{
				Side*=-1;Cont=100;
				AnimSprites[Anim][asCoorX]+=((Side==1)?8:-8);
				AnimSprites[Anim][asFrameIni]=((Side==1)?0:12);
				}
			Mode++;
			}
		}
	break;

	case 1:	// Disparar

		AnimSpriteSET(Anim, AnimSprites[Anim][asCoorX],AnimSprites[Anim][asCoorY], ((Side==1)?9:21), 3, 2, 0x102);
		Mode++;
	break;

	case 2:	// Disparo

		if (AnimSprites[Anim][asPause]!=-1) {break;}

		BalaSET(AnimSprites[Anim][asCoorX]+((Side==1)?24:-8), AnimSprites[Anim][asCoorY]+4, Side);
		Mode++;
	break;

	case 3:	// Bajar Brazo

		AnimSpriteSET(Anim, AnimSprites[Anim][asCoorX],AnimSprites[Anim][asCoorY], ((Side==1)?9:21), 3, 2, 0x112);
		Mode++;
	break;

	case 4:	// Espera que brazo baje

		if (AnimSprites[Anim][asPause]!=-1) {break;}

		Cont=100;

			if ((Side== 1 && AnimSprites[Anim][asCoorX]+12+40 > ProtX+8)	// Acercarse o Alejarse???
			||	(Side==-1 && AnimSprites[Anim][asCoorX]+12-40 < ProtX+8))
			{
			Side*=-1;
			AnimSprites[Anim][asCoorX]+=((Side==1)?8:-8);
			AnimSprites[Anim][asFrameIni]=((Side==1)?0:12);
			}

		AnimSpriteSET(Anim, AnimSprites[Anim][asCoorX],AnimSprites[Anim][asCoorY], ((Side==1)?0:12), 6, 2, 0x101);
		Mode=0;
	break;
	}

	return (false);
}

};


// ---------------
// Pistolero SET
// ===============

public int PistoleroSET(int CoorX, int CoorY, int Side)
{
	if (PistoleroC == PistoleroMAX) {return(-1);}

	int Anim = AnimSpriteSET(-1, CoorX,CoorY, ((Side==1)?0:12), 6, 2, 0x101);
	if (Anim==-1) {return(-1);}

	int i=PistoleroP[PistoleroC++];
	Pistoleros[i] = new Pistolero();

	Pistoleros[i].Anim=Anim;
	Pistoleros[i].Side=Side;

	return (i);
}


// ---------------
// Pistolero INI
// ===============

final int PistoleroMAX=8;
int PistoleroC;
int[] PistoleroP;
Pistolero[] Pistoleros;

public void PistoleroINI()
{
	PistoleroC = 0;
	PistoleroP = new int[PistoleroMAX];
	Pistoleros = new Pistolero[PistoleroMAX];
	for (int i=0 ; i<PistoleroMAX ; i++) {PistoleroP[i]=i;}
}

// ---------------
// Pistolero RES
// ===============
/*
public void PistoleroRES(int i)
{
}
*/
// ---------------
// Pistolero END
// ===============

public void PistoleroEND()
{
	Pistoleros = null;
	PistoleroP = null;
	PistoleroC = 0;
}

// ---------------
// Pistolero RUN
// ===============

public void PistoleroRUN()
{
	for (int i=0 ; i<PistoleroC ; i++)
	{
	if ( Pistoleros[PistoleroP[i]].Play() )
		{
		int t=PistoleroP[i];
		Pistoleros[t] = null;
		PistoleroP[i] = PistoleroP[--PistoleroC];
		PistoleroP[PistoleroC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Bala - Engine
// ====================
// ********************

public class Bala
{
int Anim;
int Side;
int Time=50;

public Bala() {}

// ----------------
// Bala Play
// ================

public boolean Play()
{
	if (AnimSprites[Anim][asPause] != -1) {return (false);}

	if (--Time<0 || (CheckTile(AnimSprites[Anim][asCoorX]+((Side==1)?8:-2), AnimSprites[Anim][asCoorY]) & 0x01) != 0 )
	{
	AnimSpriteRES(Anim);
	return (true);
	}

	AnimSprites[Anim][asCoorX]+=Side*4;

	return (false);
}

};


// ---------------
// Bala SET
// ===============

public int BalaSET(int CoorX, int CoorY, int Side)
{
	if (BalaC == BalaMAX) {return(-1);}

	int Anim = AnimSpriteSET(-1, CoorX,CoorY, ((Side==1)?48:52), 4, 0, ((Side==1)?0x102:0x112));
	if (Anim==-1) {return(-1);}

	int i=BalaP[BalaC++];
	Balas[i] = new Bala();

	Balas[i].Anim=Anim;
	Balas[i].Side=Side;

	return (i);
}


// ---------------
// Bala INI
// ===============

final int BalaMAX=12;
int BalaC;
int[] BalaP;
Bala[] Balas;

public void BalaINI()
{
	BalaC = 0;
	BalaP = new int[BalaMAX];
	Balas = new Bala[BalaMAX];
	for (int i=0 ; i<BalaMAX ; i++) {BalaP[i]=i;}
}

// ---------------
// Bala RES
// ===============
/*
public void BalaRES(int i)
{
}
*/
// ---------------
// Bala END
// ===============

public void BalaEND()
{
	Balas = null;
	BalaP = null;
	BalaC = 0;
}

// ---------------
// Bala RUN
// ===============

public void BalaRUN()
{
	for (int i=0 ; i<BalaC ; i++)
	{
	if ( Balas[BalaP[i]].Play() )
		{
		int t=BalaP[i];
		Balas[t] = null;
		BalaP[i] = BalaP[--BalaC];
		BalaP[BalaC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Lanzador - Engine
// ====================
// ********************

public class Lanzador
{
int Anim;
int Side;
int Mode=0;
int Time=60;
int Desp=0;

public Lanzador() {}

// ----------------
// Lanzador Play
// ================

public boolean Play()
{
	if (
		StarCheck (AnimSprites[Anim][asCoorX]+((Side==1)?4:8), AnimSprites[Anim][asCoorY]+2, 12, 28)	// Muerte por Star??
	||	SableCheck(AnimSprites[Anim][asCoorX]+((Side==1)?4:8), AnimSprites[Anim][asCoorY]+2, 12, 28)	// Muerte por Sable??
		)
	{
	gc.SoundSET(3,1);
	gc.VibraSET(250);
	AnimSpriteSET(Anim, AnimSprites[Anim][asCoorX],AnimSprites[Anim][asCoorY], ((Side==1)?33:45), 3, 2, 0x100);
	return (true);
	}

	switch (Mode)
	{
	case 0:	// Esperamos situacion de lanzamiento

		if (Time > 0) {Time--;break;}

		if ((AnimSprites[Anim][asCoorY]+ 0 < ProtY+32)	// Dentro de Zona de tiro??
		&&	(AnimSprites[Anim][asCoorY]+32 > ProtY-48))
		{
			if ((Side== 1 && AnimSprites[Anim][asCoorX]+12+24 < ProtX+8 && AnimSprites[Anim][asCoorX]+12+116 > ProtX+8)
			||	(Side==-1 && AnimSprites[Anim][asCoorX]+12-24 > ProtX+8 && AnimSprites[Anim][asCoorX]+12-116 < ProtX+8))
			{
			Desp=((Side==1)?ProtX+8-AnimSprites[Anim][asCoorX]:AnimSprites[Anim][asCoorX]-ProtX+8); if (Desp<0) {Desp*=-1;}
			AnimSprites[Anim][asPause]=0;
			Mode++;
			Time=32;
			}
		}
	break;

	case 1:	// Lanzamos Granada

		if (AnimSprites[Anim][asFrameAct] != 7) {break;}

		GranadaSET(AnimSprites[Anim][asCoorX]+((Side==1)?16:0), AnimSprites[Anim][asCoorY], Side, Desp+8);
		Mode=0;
	break;
	}

	return (false);
}

};


// ---------------
// Lanzador SET
// ===============

public int LanzadorSET(int CoorX, int CoorY, int Side)
{
	if (LanzadorC == LanzadorMAX) {return(-1);}

	int Anim = AnimSpriteSET(-1, CoorX,CoorY, ((Side==1)?24:36), 9, 1, 0x123);
	if (Anim==-1) {return(-1);}

	int i=LanzadorP[LanzadorC++];
	Lanzadors[i] = new Lanzador();

	Lanzadors[i].Anim=Anim;
	Lanzadors[i].Side=Side;

	return (i);
}


// ---------------
// Lanzador INI
// ===============

final int LanzadorMAX=8;
int LanzadorC;
int[] LanzadorP;
Lanzador[] Lanzadors;

public void LanzadorINI()
{
	LanzadorC = 0;
	LanzadorP = new int[LanzadorMAX];
	Lanzadors = new Lanzador[LanzadorMAX];
	for (int i=0 ; i<LanzadorMAX ; i++) {LanzadorP[i]=i;}
}

// ---------------
// Lanzador RES
// ===============
/*
public void LanzadorRES(int i)
{
}
*/
// ---------------
// Lanzador END
// ===============

public void LanzadorEND()
{
	Lanzadors = null;
	LanzadorP = null;
	LanzadorC = 0;
}

// ---------------
// Lanzador RUN
// ===============

public void LanzadorRUN()
{
	for (int i=0 ; i<LanzadorC ; i++)
	{
	if ( Lanzadors[LanzadorP[i]].Play() )
		{
		int t=LanzadorP[i];
		Lanzadors[t] = null;
		LanzadorP[i] = LanzadorP[--LanzadorC];
		LanzadorP[LanzadorC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Granada - Engine
// ====================
// ********************

int[] GranadaTab = new int[] {-3,-2,-2,-1,-1,-1,0,-1,0,0,0,1,1,1,2,2,3,4,5};

public class Granada
{
int Anim;
int CoorX;
int Side;
int Desp;
int Cont=0;
int Pos=0;
int Mode=0;

public Granada() {}

// ----------------
// Granada Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		if (Cont<=32) {AnimSprites[Anim][asCoorX] = CoorX+((((Desp/32)*Cont++)/256)*Side);}

		AnimSprites[Anim][asCoorY]+=GranadaTab[Pos];
		if (++Pos == GranadaTab.length) {Pos--;}

		if ( CheckTile(AnimSprites[Anim][asCoorX], AnimSprites[Anim][asCoorY]+6, 7,0, 0x01) != 0  )
		{
		AnimSpriteSET(Anim, AnimSprites[Anim][asCoorX]-4, AnimSprites[Anim][asCoorY]-4, 96, 9, 1, 0x02);
		Mode++;
		}
	break;	

	case 1:
		if (AnimSprites[Anim][asPause] == -1)
		{
		AnimSpriteRES(Anim);
		return (true);
		}
	break;
	}

	return (false);
}

};


// ---------------
// Granada SET
// ===============

public int GranadaSET(int CoorX, int CoorY, int Side, int Desp)
{
	if (GranadaC == GranadaMAX) {return(-1);}

	int Anim = AnimSpriteSET(-1, CoorX,CoorY, 108, 4, 1, ((Side==1)?0x01:0x11) );
	if (Anim==-1) {return(-1);}

	int i=GranadaP[GranadaC++];
	Granadas[i] = new Granada();

	Granadas[i].Anim=Anim;
	Granadas[i].CoorX=CoorX;
	Granadas[i].Side=Side;
	Granadas[i].Desp=Desp*256;

	return (i);
}


// ---------------
// Granada INI
// ===============

final int GranadaMAX=8;
int GranadaC;
int[] GranadaP;
Granada[] Granadas;

public void GranadaINI()
{
	GranadaC = 0;
	GranadaP = new int[GranadaMAX];
	Granadas = new Granada[GranadaMAX];
	for (int i=0 ; i<GranadaMAX ; i++) {GranadaP[i]=i;}
}

// ---------------
// Granada RES
// ===============
/*
public void GranadaRES(int i)
{
}
*/
// ---------------
// Granada END
// ===============

public void GranadaEND()
{
	Granadas = null;
	GranadaP = null;
	GranadaC = 0;
}

// ---------------
// Granada RUN
// ===============

public void GranadaRUN()
{
	for (int i=0 ; i<GranadaC ; i++)
	{
	if ( Granadas[GranadaP[i]].Play() )
		{
		int t=GranadaP[i];
		Granadas[t] = null;
		GranadaP[i] = GranadaP[--GranadaC];
		GranadaP[GranadaC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Ascensor - Engine
// ====================
// ********************

int AscenAnim;
int AscenX;
int AscenY;
int AscenAni2;
int AscenSide;
int	AscenDesp;
int	AscenCont;

// ---------------
// Ascensor INI
// ===============

public void AscensorINI()
{
	AscenAnim=-1;
	AscenAni2=-1;
}


// ---------------
// Ascensor SET
// ===============

public void AscensorSET(int X, int Y, int Desp)
{
	AscenX=X;
	AscenY=Y;
	AscenAnim=AnimSpriteSET(-1, AscenX,    AscenY, 56, 1, 1, 0x122);
	AscenAni2=AnimSpriteSET(-1, AscenX+24, AscenY, 57, 1, 1, 0x122);
	AscenDesp=Desp;
	AscenCont=Desp;
	AscenSide=1;
}


// ---------------
// Ascensor END
// ===============

public void AscensorEND()
{
	if (AscenAnim!=-1) {AnimSpriteRES(AscenAnim);}
	if (AscenAni2!=-1) {AnimSpriteRES(AscenAni2);}
	AscenAnim=-1;
	AscenAni2=-1;
}


// ---------------
// Ascensor RUN
// ===============

public void AscensorRUN()
{
	if (AscenAnim==-1) {return;}

	if (--AscenCont < 1)
	{
	AscenCont=AscenDesp; AscenSide*=-1;
	} else {
	AscenY+=AscenSide;
	AnimSprites[AscenAnim][asCoorY]=AscenY;
	AnimSprites[AscenAni2][asCoorY]=AscenY;
	}
}


// ---------------
// Ascensor Check
// ===============

public boolean AscensorCheck()
{
	if (AscenAnim!=-1)
	{
		if ((AscenX+ 0 > ProtX+16) ||
			(AscenX+40 < ProtX) ||
			(AscenY+ 0 > ProtY+36) ||
			(AscenY+ 4 < ProtY+28))
		{} else
		{ProtBaseX=0;ProtBaseY=AscenY-32; return (true);}
	}

	if (ToroAnim!=-1)
	{
		if ((ToroX+20 > ProtX+16) ||
			(ToroX+40 < ProtX) ||
			(ToroY+ 8 > ProtY+36) ||
			(ToroY+12 < ProtY+28))
		{} else
		{ProtBaseX=ToroSide;ProtBaseY=ToroY-24; return (true);}

		if ((ToroX-14 > ProtX+16) ||
			(ToroX- 8 < ProtX) ||
			(ToroY+PalaDesp+17 > ProtY+36) ||
			(ToroY+PalaDesp+20 < ProtY+28))
		{} else
		{ProtBaseX=ToroSide;ProtBaseY=ToroY+PalaDesp-32+18; return (true);}
	}
	return (false);
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Toro - Engine
// ====================
// ********************

int ToroDie;
int ToroMode;
int ToroFrame;
int ToroAnim;
int ToroAni2;
int ToroAni3;
int ToroAni4;
int ToroX;
int ToroY;
int ToroSide, ToroSid1;
int	ToroDesp;
int	ToroCont;

int PalaAnim;
int PalaAni2;
int PalaSide;
int	PalaDesp;

// ---------------
// Toro INI
// ===============

public void ToroINI()
{
	ToroAnim=-1;
	ToroAni2=-1;
	ToroAni3=-1;
	ToroAni4=-1;

	PalaAnim=-1;
	PalaAni2=-1;
}


// ---------------
// Toro SET
// ===============

public void ToroSET(int X, int Y)
{
	ToroDie=30;
	ToroX=X;
	ToroY=Y;
	ToroSide=1;
	ToroMode=0;
	ToroFrame=60;
	ToroCont=230;
	ToroAnim=AnimSpriteSET(-1, ToroX,    ToroY,    ToroFrame,    4, 1, 0x101);
	ToroAni2=AnimSpriteSET(-1, ToroX+24, ToroY,    ToroFrame+1,  4, 1, 0x101);
	ToroAni3=AnimSpriteSET(-1, ToroX,    ToroY+32, ToroFrame+12, 4, 1, 0x101);
	ToroAni4=AnimSpriteSET(-1, ToroX+24, ToroY+32, ToroFrame+13, 4, 1, 0x101);

	PalaDesp=0;
	PalaSide=1;
	PalaAnim=AnimSpriteSET(-1, ToroX-48+8, ToroY, 58, 1, 1, 0x122);
	PalaAni2=AnimSpriteSET(-1, ToroX-24+8, ToroY, 59, 1, 1, 0x122);


	if (ToroAnim==-1 || ToroAni2==-1 || ToroAni3==-1 || ToroAni4==-1 || PalaAnim==-1 || PalaAni2==-1)
	{
	ToroEND();
	return;
	}


	AnimSprites[ToroAnim][asSide]=2;
	AnimSprites[ToroAni2][asSide]=2;
	AnimSprites[ToroAni3][asSide]=2;
	AnimSprites[ToroAni4][asSide]=2;
}


// ---------------
// Toro END
// ===============

public void ToroEND()
{
	if (ToroAnim!=-1) {AnimSpriteRES(ToroAnim);}
	if (ToroAni2!=-1) {AnimSpriteRES(ToroAni2);}
	if (ToroAni3!=-1) {AnimSpriteRES(ToroAni3);}
	if (ToroAni4!=-1) {AnimSpriteRES(ToroAni4);}

	if (PalaAnim!=-1) {AnimSpriteRES(PalaAnim);}
	if (PalaAni2!=-1) {AnimSpriteRES(PalaAni2);}

	ToroINI();
}


// ---------------
// Toro RUN
// ===============

public void ToroRUN()
{
	if (ToroAnim==-1) {return;}


// Toro Muere?
// -----------
	if (
		StarCheck (ToroX, ToroY+8, 48, 32)	// Muerte por Star??
	||	SableCheck(ToroX, ToroY+8, 48, 32)	// Muerte por Sable??
		)
	{
		if (--ToroDie < 0)
		{
		gc.SoundSET(4,1);
		gc.VibraSET(250);
		ToroEND();
		FaseExit=1;
		return;
		}
	}


// Toro Mover
// ----------
	switch (ToroMode)
	{
	case 0:
		ToroX+=ToroSide;
		if (ToroX <= 32 || ToroX >= 186) {ToroSide*=-1; ToroFrame=((ToroSide!=1)?60:64);}

		PalaDesp+=PalaSide;
		if (PalaDesp < 1 || PalaDesp > 23) {PalaSide*=-1;}

	if (--ToroCont > 0) {break;}

	ToroCont=0;

	if (ProtX+8 < ToroX-32 || ProtX+8 > ToroX) {break;}

		ToroSid1=ToroSide; ToroSide=0;
		ToroFrame=68;
			AnimSprites[ToroAnim][asFrameAct]=0;
			AnimSprites[ToroAnim][asPause]=-1;
			AnimSprites[ToroAni2][asFrameAct]=0;
			AnimSprites[ToroAni2][asPause]=-1;
			AnimSprites[ToroAni3][asFrameAct]=0;
			AnimSprites[ToroAni3][asPause]=-1;
			AnimSprites[ToroAni4][asFrameAct]=0;
			AnimSprites[ToroAni4][asPause]=-1;
		ToroCont=6;
		ToroMode++;
	break;

	case 1:
	if (--ToroCont > 0) {break;}

		ToroFrame=70;
		ToroCont=5;
		ToroMode++;
	break;

	case 2:
	if (--ToroCont > 0) {break;}
		BalaSET(ToroX+2, ToroY+9, -1);
		ToroCont=5;
		ToroMode++;
	break;

	case 3:
	if (--ToroCont > 0) {break;}
		BalaSET(ToroX+2, ToroY+9, -1);
		ToroCont=5;
		ToroMode++;
	break;

	case 4:
	if (--ToroCont > 0) {break;}
		BalaSET(ToroX+2, ToroY+9, -1);
		ToroCont=5;
		ToroMode++;
	break;

	case 5:
	if (--ToroCont > 0) {break;}

		ToroSide=ToroSid1;
		ToroFrame=((ToroSide!=1)?60:64);
			AnimSprites[ToroAnim][asPause]=0;
			AnimSprites[ToroAni2][asPause]=0;
			AnimSprites[ToroAni3][asPause]=0;
			AnimSprites[ToroAni4][asPause]=0;
		ToroCont=64;
		ToroMode=0;
	break;
	}


// Toro IMP
// --------
	AnimSprites[ToroAnim][asCoorX]=ToroX;
	AnimSprites[ToroAni2][asCoorX]=ToroX+24;
	AnimSprites[ToroAni3][asCoorX]=ToroX;
	AnimSprites[ToroAni4][asCoorX]=ToroX+24;

	AnimSprites[ToroAnim][asFrameIni]=ToroFrame;
	AnimSprites[ToroAni2][asFrameIni]=ToroFrame+1;
	AnimSprites[ToroAni3][asFrameIni]=ToroFrame+12;
	AnimSprites[ToroAni4][asFrameIni]=ToroFrame+13;


	AnimSprites[PalaAnim][asCoorX]=ToroX-48+8;
	AnimSprites[PalaAni2][asCoorX]=ToroX-24+8;

	AnimSprites[PalaAnim][asCoorY]=ToroY+PalaDesp;
	AnimSprites[PalaAni2][asCoorY]=ToroY+PalaDesp;
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Intro - Engine
// ===================
// *******************

int IntroMode;
int IntroTime;
int IntroPaint=0;
int IntroSumaX;

int SableX, SableY;
int VainaX, VainaY;
int NinjaX, NinjaY, NinjaFrame;
int KanjiX, KanjiY, KanjiDesp, KanjiSeno, KanjiBaseY;
int SabotX, SabotY, SabotDesp, SabotSeno, SabotBaseY;

// -------------------
// Intro SET
// ===================

public void IntroSET()
{
	gc.IntroGFX_Load();

	SableX=GameSizeX-360;
	SableY=(GameSizeY-80)/2;

	VainaX=GameSizeX-86;
	VainaY=SableY+18;

	KanjiX=(GameSizeX-88)/2;
	SabotX=(GameSizeX-83)/2;

	SabotY=GameSizeY+20;SabotBaseY=SabotY;
	SabotDesp=SabotY-((GameSizeY-22)/2);

	KanjiY=-112;KanjiBaseY=KanjiY;
	KanjiDesp=((GameSizeY-112)/2)-KanjiY;


	NinjaX=(GameSizeX-96)/2;
	NinjaY=(GameSizeY-64)/2;


	IntroMode=0;
	IntroSumaX=0;

	PaintRGB=0;
	Paint_ON=1;

	IntroPaint=-1;

	gc.SoundSET(1,1);
}


// -------------------
// Intro END
// ===================

public void IntroEND()
{
	gc.SoundRES();
	IntroPaint=0;
	gc.IntroGFX_Kill();
}


// -------------------
// Intro RUN
// ===================

public boolean IntroRUN()
{
	switch (IntroMode)
	{

// Movimiento del Sable
// --------------------
	case 0:
		IntroTime=96;
		IntroMode++;
	case 1:
		if (--IntroTime > 0)
		{
		SableX--;
		VainaX--;
		break;
		}
		IntroTime=24;
		IntroMode++;
	break;

	case 2:
		if (--IntroTime > 0)
		{
		SableX++;
		VainaX++;
		break;
		}
		IntroTime=16;
		IntroMode++;
	break;

	case 3:
		if (--IntroTime > 0)
		{
		break;
		}
		IntroMode=6;
	break;

	case 6:
		if  (VainaX+86 > 0) {VainaX-=IntroSumaX;}

		if  (SableX < GameSizeX)
		{
		SableX+=IntroSumaX;
		if (++IntroSumaX > 16) {IntroSumaX=16;}
		break;
		}
		IntroMode=10;


// "Animacion" del Ninja
// ---------------------
	case 10:
		NinjaFrame=0;
		IntroMode++;
	case 11:
		IntroTime=12;
		IntroMode++;
	case 12:
		if (--IntroTime > 0)
		{
		if (IntroTime==4 || IntroTime==11) {IntroPaint=1;}
		break;
		}
		IntroMode++;

	case 13:
		NinjaFrame+=3;
		if (NinjaFrame < 3*6)
		{
		IntroMode=11;
		} else {
		IntroMode=20;
		}
	break;


// Movimiento de Logos: Kanji y Sabotage
// -------------------------------------
	case 20:
		IntroTime=16;
		IntroMode++;
	case 21:
		if (--IntroTime > 0)
		{
		break;
		}
		IntroMode++;
	case 22:
		SabotY=SabotBaseY-((TablaSeno[SabotSeno]*SabotDesp)/256);
		KanjiY=KanjiBaseY+((TablaSeno[KanjiSeno]*KanjiDesp)/256);
		SabotSeno+=2;if (SabotSeno > 90) {SabotSeno=90;}
		KanjiSeno+=2;if (KanjiSeno > 90) {KanjiSeno=90;IntroMode++;}
		IntroPaint=1;
		IntroTime=80;
	break;

	case 23:
		if (--IntroTime > 0)
		{
		break;
		}
		return true;
	}

	if (KeybB1==5 && KeybB2!=5) {return true;}
	if (KeybB1==10 && KeybB2!=10) {MenuNow=1;return true;}

	return false;
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Panel - Engine
// ===================
// *******************

int Panel_ON;

// -------------------
// Panel INI
// ===================

public void PanelINI()
{
	Panel_ON=0;
}

// -------------------
// Panel SET
// ===================

public void PanelSET(int Type)
{
	Panel_ON=1;

	ParcoINI();

	switch (Type)
	{
	case 0:
		ParcoADD(0x111,Texto.Level+" "+JugarLevel);
	break;

	case 2:
		ParcoADD(0x111,Texto.GameOver);
	break;
	}

	ParcoSET_Texto();
}


// -------------------
// Panel RES
// ===================

public void PanelRES()
{
	if (Panel_ON!=0)
	{
	ParcoEND();
	Panel_ON=0;
	}
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Marco - Engine
// ===================
// *******************

int Marco_ON;
int MarcoStatus;
int MarcoUpdate;
int MarcoStatusOld;
int MarGameStatus;
int MarHelpFlag=0;

// -------------------
// Marco SET
// ===================

public void MarcoSET(int Status)
{
	MarcoUpdate=1;

	MarcoStatusOld=MarcoStatus;
	MarcoStatus=Status;

	switch (Status)
	{
	case 00:
		if (MarHelpFlag==0 && GameSizeX<160)
		{
		MarHelpFlag=1;
		Texto.MenuInfo(this);
		break;
		}
		MarcoStatus=10;

	case 10:
		ParcoINI();
		ParcoADD(0x011,Texto.Start,1);
		ParcoADD(0x011,Texto.Sound,2,GameSound);
		ParcoADD(0x011,Texto.Vibrate,3,GameVibra);
		ParcoADD(0x011,Texto.Controls,5);
		ParcoADD(0x011,Texto.Credits,4);
		ParcoADD(0x011,Texto.ExitGame,9);
		ParcoSET_Option();
	break;

	case 30:
		ParcoINI();
		ParcoADD(0x011,Texto.Continue,0);
		ParcoADD(0x011,Texto.TryAgain,7);
		ParcoADD(0x011,Texto.Sound,2,GameSound);
		ParcoADD(0x011,Texto.Vibrate,3,GameVibra);
		ParcoADD(0x011,Texto.Controls,5);
		ParcoADD(0x011,Texto.Restart,6);
		ParcoADD(0x011,Texto.ExitGame,9);		
		ParcoSET_Option();
	break;

	case 50:
		ParcoINI();
		ParcoSET_Option();
	break;

	case 20:
		Texto.Credits(this);
	break;

	case 40:
		Texto.Controls(this);
	break;


	default:
	return;
	}
	MarGameStatus=GameStatus;
	GameStatus=900;
	Marco_ON=1;
}

// -------------------
// Marco RUN
// ===================

public void MarcoRUN()
{
	switch (MarcoStatus)
	{
	case 00:
		if (KeybB1!=0 && KeybB2==0)
		{
		MarcoEND();
		MarcoSET(10);
		return;
		}
	break;

	case 21:
		MarcoEND();
		MarcoSET(MarcoStatusOld);
	break;

	case 41:
		MarcoEND();
		MarcoSET(MarcoStatusOld);
	break;
	}

	if (KeybB1==10 && KeybB2!=10) {MarcoEND(); return;}

	int MarcoKey=0;
	if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
	if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
	if (KeybB1== 5 && KeybB2!= 5) {MarcoKey=5;}

	if (KeybB1!= 0 && KeybB2== 0 && Parco_ON==2) {MarcoEXE(-2); return;}

	MarcoEXE(ParcoRUN(MarcoKey));
}

// -------------------
// Marco EXE
// ===================

public void MarcoEXE(int Opcion)
{
	switch (Opcion)
	{
	case -1:
	return;

	case -2:
		MarcoRES();
		MarcoStatus++;
	return;

	case 0:	// Continue
		MarcoEND();
	break;

	case 1:	// Start
		MarcoEND();
		GameStatus=12;
	break;

	case 2:	// Sound ON/OFF
		GameSound=getOption();
//		if (JugarLevelAct==0)
		{
			if (GameSound==0)
			{
//			screen.SoundRES(1);
			} else {
//			screen.SoundSET(1,0);
			}
		}
	break;

	case 3:	// Vibrate ON/OFF
		GameVibra=getOption();
	break;

	case 4:	// Credits
		MarcoEND();
		MarcoSET(20);
	break;

	case 5:	// Controls
		MarcoEND();
		MarcoSET(40);
	break;

	case 6:	// Restard
		MarcoEND();
		JugarVidas=0;
		FaseExit=2;
	break;

	case 7:	// Try Again
		MarcoEND();
		ProtMorirSET();
	break;

	case 9:	// Exit Game
		GameExit=1;
	break;
	}

}

// -------------------
// Marco RES
// ===================

public void MarcoRES()
{
	ParcoEND();
}

// -------------------
// Marco END
// ===================

public void MarcoEND()
{
	ParcoEND();
	GameStatus=MarGameStatus;
	Marco_ON=0;
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

byte[] PrefsData = new byte[] {1,1};

// -------------------
// Prefs INI
// ===================

public void PrefsINI()
{
	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("SabotPrefs", true);

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
		rs = RecordStore.openRecordStore("SabotPrefs", true);

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
// Parco - Engine
// ===================
// *******************

int Parco_ON;		// Estado del Parco Engine (0 = OFF)
int ParcoUpdate=0;	// Obligamos a Actualizar a ParcoIMP

int ParLcdSizeX=96;	// Size X del LCD
int ParLcdSizeY=65;	// Size Y del LCD

int ParMaxSizeY=65-8;	// Size Y m�ximo para el Parco.

int ParcoX;			// Coordenadas y Size del Parco (cuadrado negro)
int ParcoY;
int ParcoSizeX;
int ParcoSizeY;

int ParTexX;		// Coordenadas y Size usables para imprimir los textos.
int ParTexY;
int ParTexSizeX;
int ParTexSizeY;

int TextoSizeX;		// Size m�ximo de los textos a imprimir
int TextoSizeY;

int ParTexScrY;		// Coordenada Y base para imprimir los textos.
int ParTexSpd;		// Velocidad del Scroll.
int ParTexCnt;		// Velocidad del Scroll.
int ParTexPos;		// Posicion actual de la linea de opciones.

int ParMaySizeY;	// Size Y del caracter ">"

String	ParTexStr[][];
int		ParTexDat[][];


// -------------------
// Parco Constructor
// ===================

public void Parco()
{
	Graphics.fontGenerate(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL, Color.BLACK);
	Graphics.fontGenerate(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL, Color.BLACK);
	Graphics.fontGenerate(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, Color.BLACK);
	Font f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
	ParMaySizeY=f.getHeight();
}

public void Parco(int SizeX, int SizeY)
{
	ParLcdSizeX=SizeX;
	ParLcdSizeY=SizeY;

	ParMaxSizeY=ParLcdSizeY-8;

	Font f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
	ParMaySizeY=f.getHeight();
}

// -------------------
// Parco INI
// ===================

public void ParcoINI()
{
	Parco_ON=0;
	ParTexStr=null;
	ParTexDat=null;

	ParcoSizeX=0;
	ParcoSizeY=0;
}


// -------------------
// Parco ADD
// ===================

public void ParcoADD(int Dato, String Texto)
{
	ParcoADD(Dato, new String[] {Texto}, 0, 0);
}

public void ParcoADD(int Dato, String Texto, int Dat1)
{
	ParcoADD(Dato, new String[] {Texto}, Dat1, 0);
}

public void ParcoADD(int Dat0, String[] Texto, int Dat1, int Dat2)
{
	int Lines=(ParTexDat!=null)?ParTexDat.length:0;

	String Str[][] = new String[Lines+1][];
	int Dat[][] = new int[Lines+1][];

	int i=0;

	for (; i<Lines; i++)
	{
	Dat[i] = ParTexDat[i];
	Str[i] = ParTexStr[i];
	}

	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2};

	ParTexStr=Str;
	ParTexDat=Dat;
}



// -------------------
// Parco SET
// ===================

public void ParcoSET(int SizeX, int SizeY)
{
	if (ParcoSizeX==0) {ParcoSizeX=SizeX;}
	if (ParcoSizeY==0) {ParcoSizeY=SizeY;}

	if (ParcoSizeY > ParMaxSizeY) {ParcoSizeY=ParMaxSizeY;}

	ParcoX=(ParLcdSizeX - ParcoSizeX)/2;
	ParcoY=(ParLcdSizeY - ParcoSizeY)/2;

	ParTexX=ParcoX+3;
	ParTexY=ParcoY+3;

	ParTexSizeX=ParcoSizeX-6;
	ParTexSizeY=ParcoSizeY-6;

	if (Parco_ON!=2 && ParTexSizeY > TextoSizeY)
		{
		ParTexY+=((ParTexSizeY-TextoSizeY)/2);
		ParTexSizeY=TextoSizeY;
		}

	ParcoUpdate=1;
}



// -------------------
// Parco SET Texto
// ===================

public void ParcoSET_Texto()
{
	if (ParTexDat == null) {return;}

	Parco_ON=1;

	ParcoCalc(ParTexStr, ParTexDat);

	ParcoSET( TextoSizeX+6 , TextoSizeY+6 );

	ParTexScrY=0;
}



// -------------------
// Parco SET Scroll
// ===================

public void ParcoSET_Scroll(int Speed)
{
	if (ParTexDat == null) {return;}

	Parco_ON=2;

	ParTexSpd=Speed;
	ParTexCnt=ParTexSpd;

	ParcoCalc(ParTexStr, ParTexDat);

	ParcoSET( TextoSizeX+6 , TextoSizeY+6 );

	ParTexScrY=ParTexSizeY;
}



// -------------------
// Parco SET Option
// ===================

public void ParcoSET_Option()
{
	if (ParTexDat == null) {return;}

	Parco_ON=3;

	for (int i=0 ; i<ParTexDat.length ; i++) {ParTexDat[i][0]=0x010;}

	ParcoCalc(ParTexStr, ParTexDat);

	for (int i=0 ; i<ParTexDat.length ; i++) {ParTexDat[i][0]=0x000;}

	TextoSizeX+=10;

	ParcoSET( TextoSizeX+6 , TextoSizeY+6 );

	ParTexX+=10;TextoSizeX-=10;

	ParTexScrY=0;
	ParTexPos=0;
	ParTexDat[ParTexPos][0]=0x010;
}




// -------------------
// Parco Calc
// ===================

public void ParcoCalc(String Str[][], int Dat[][])
{
	TextoSizeX=8;
	TextoSizeY=0;

	for (int i=0 ; i<Str.length ; i++)
	{
	Font f=Font.getFont(Font.FACE_PROPORTIONAL, ((Dat[i][0]&0x0F0)==0)?Font.STYLE_PLAIN:Font.STYLE_BOLD, ((Dat[i][0]&0xF00)==0)?Font.SIZE_SMALL:Font.SIZE_MEDIUM);
	TextoSizeY+=f.getHeight();
	for (int SizeX, t=0 ; t<Str[i].length ; t++)
	{
	SizeX=f.stringWidth(Str[i][t]);
	if ( TextoSizeX < SizeX ) {TextoSizeX=SizeX;}
	}
	}
}



// -------------------
// Parco RUN
// ===================

// Retorno:
//	-1 = Sin novedad.
//	-2 = El Scroll ha llegado al final.
//	 x = Numero asociado a la seleccion en modo "Option".

public int ParcoRUN(int Key)
{
	int Ret=-1;

	switch (Parco_ON)
	{
	case 2:
		if (--ParTexCnt < 0)
		{
		ParTexCnt=ParTexSpd;
		ParcoUpdate=1;
		if (--ParTexScrY < -TextoSizeY) {ParTexScrY++; Ret=-2;}
		}
	break;
	
	case 3:
		switch (Key)
		{
		case 2:
			if (ParTexPos > 0) {ParTexPos--; ParcoUpdate=1;}
		break;

		case 8:
			if (ParTexPos < ParTexDat.length-1) {ParTexPos++; ParcoUpdate=1;}
		break;

		case 5:
			if (++ParTexDat[ParTexPos][2] == ParTexStr[ParTexPos].length) {ParTexDat[ParTexPos][2]=0;}
			Ret=ParTexDat[ParTexPos][1];
			ParcoUpdate=1;
		break;
		}

		for (int i=0 ; i<ParTexDat.length ; i++) {ParTexDat[i][0]=0x000;}
		ParTexDat[ParTexPos][0]=0x010;
	break;

	}

	return(Ret);
}



// -------------------
// Parco IMP
// ===================

public void ParcoIMP_Now(Graphics LCD_Gfx)
{
	ParcoUpdate=1;
	ParcoIMP(LCD_Gfx);
}

public void ParcoIMP(Graphics LCD_Gfx)
{
	if (Parco_ON == 0 ) {return;}

	if (ParcoUpdate == 0 ) {return;} else {ParcoUpdate=0;}

	LCD_Gfx.setClip(ParcoX, ParcoY,  ParcoSizeX, ParcoSizeY);

	LCD_Gfx.setColor(255,255,255);
 	LCD_Gfx.fillRect(ParcoX, ParcoY,  ParcoSizeX, ParcoSizeY);

	LCD_Gfx.setColor(0,0,0);
 	LCD_Gfx.drawRect(ParcoX, ParcoY,  ParcoSizeX-1, ParcoSizeY-1);

	ParcoTextoIMP(LCD_Gfx, ParTexStr, ParTexDat);
}



// -------------------
// Parco Texto IMP
// ===================

public void ParcoTextoIMP(Graphics LCD_Gfx, String Str[][], int Dat[][])
{
	LCD_Gfx.setClip(ParcoX+3, ParcoY+3,  ParcoSizeX-6, ParcoSizeY-6);
	LCD_Gfx.setColor(0);

	if (Parco_ON==3)
	{
	LCD_Gfx.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));

	int BaseY = ((((ParTexSizeY-ParMaySizeY)*256) / (Dat.length-1) ) * ParTexPos) / 256;

	LCD_Gfx.drawString(">",  ParTexX-8, ParTexY+BaseY,  20);

	ParTexScrY=(-ParMaySizeY*ParTexPos)+BaseY;
	}

	int SumaY=ParTexScrY;

	for (int i=0 ; i<Str.length ; i++)
	{
	Font f=Font.getFont(Font.FACE_PROPORTIONAL, ((Dat[i][0]&0x0F0)==0)?Font.STYLE_PLAIN:Font.STYLE_BOLD, ((Dat[i][0]&0xF00)==0)?Font.SIZE_SMALL:Font.SIZE_MEDIUM);
	if (ParTexY+SumaY > -20  &&  SumaY < ParTexSizeY)
	{
	LCD_Gfx.setFont(f);
	int t=Dat[i][2];
	if ((Dat[i][0]&0xF)==0) {LCD_Gfx.drawString(Str[i][t],  ParTexX, ParTexY+SumaY,  20);}
	if ((Dat[i][0]&0xF)==1) {LCD_Gfx.drawString(Str[i][t],  ParTexX+((ParTexSizeX-f._stringWidth(Str[i][0]))/2), ParTexY+SumaY,  20);}
	if ((Dat[i][0]&0xF)==2) {LCD_Gfx.drawString(Str[i][t],  ParTexX+ (ParTexSizeX-f._stringWidth(Str[i][0]))   , ParTexY+SumaY,  20);}
	}
	SumaY+=f.getHeight();
	}
}



// -------------------
// Parco END
// ===================

public void ParcoEND()
{
	ParcoINI();
}



// -------------------
// Parco getOption
// ===================

public int getOption()
{
	return(ParTexDat[ParTexPos][2]);
}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// Cheats - Engine
// ===================
// *******************

int CheatInmune=0;

public void Cheats()
{
	if (KeybB1==9 && KeybB2!=9) {FaseExit=1;}

	if (KeybB1==7 && KeybB2!=7) {CheatInmune=~CheatInmune;}
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