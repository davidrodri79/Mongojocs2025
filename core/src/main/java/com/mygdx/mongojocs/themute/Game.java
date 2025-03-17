


// -----------------------------------------------
// The Mute v1.0 Rev.1 ( 6.6.2003) - Final
// The Mute v1.0 Rev.0 (22.5.2003)
// ===============================================
// Programado por Juan Antonio G�mez
// ------------------------------------


package com.mygdx.mongojocs.themute;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
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
	GameINI(); System.gc();

	while (GameExit==0)
	{
		GameMilis = System.currentTimeMillis();

		KeyboardRUN();

		GameRUN();

		gc.repaint();
		gc.serviceRepaints();

//		System.gc();

		GameSleep=(40-(int)(System.currentTimeMillis()-GameMilis));
		if (GameSleep<1) {GameSleep=1;}

		try	{
		thread.sleep(GameSleep);
//		thread.sleep(1);
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
	for(int i = 0; i < bytes.length; i++)
		buffer[i] = bytes[i];
}


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// RND - Engine - ( Resultado: de 0 a Max-1 )
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
int RND_Cont=(int)(System.currentTimeMillis()%24);
int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

public int RND(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
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


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean Colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	if (( x2     > x1+xs1 )
	||	( x2+xs2 < x1     )
	||	( y2     > y1+ys1 )
	||	( y2+ys2 < y1     ))
	{return false;}
	return true;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// *******************
// -------------------
// Keyboard - Engine
// ===================
// *******************

int KeybX,  KeybY,  KeybB;
int KeybX1, KeybY1, KeybB1, KeybD1;
int KeybX2, KeybY2, KeybB2, KeybD2;

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
			case 6:KeybY=1;break;	// Abajo
			case 5:KeybX=1;break;	// Derecha
			case 2:KeybX=-1;break;	// Izquierda
			case 8:KeybB=5;break;	// Fuego
		}*/

		switch (gc.KeyData)
		{
		case Canvas.KEY_NUM0:
			KeybB=10;
		break;

		case Canvas.KEY_NUM1:
			KeybB=1; KeybX=-1; KeybY=-1;
		break;

		case Canvas.KEY_NUM2:	// Arriba
			KeybB=2; KeybY=-1;
		break;

		case Canvas.KEY_NUM3:
			KeybB=3; KeybX= 1; KeybY=-1;
		break;

		case Canvas.KEY_NUM4:	// Izquierda
			KeybB=4; KeybX=-1;
		break;

		case Canvas.KEY_NUM5:	// Disparo
			KeybB=5;
		break;

		case Canvas.KEY_NUM6:	// Derecha
			KeybB=6; KeybX=1;
		break;

		case Canvas.KEY_NUM7:
			KeybB=7; KeybX=-1; KeybY= 1;
		break;

		case Canvas.KEY_NUM8:	// Abajo
			KeybB=8; KeybY=1;
		break;

		case Canvas.KEY_NUM9:
			KeybB=9; KeybX= 1; KeybY= 1;
		break;

		case 35:		// *
		case 42:		// #
		case -6:		// Menu Izq
			KeybB=-6;
		break;

		case -7:		// Menu Der
			KeybB=-7;
		break;
		}

//	if (KeybB!=0) {CheatRUN(KeybB);}

	}

	KeybX2 = KeybX1;	// Keys del Frame Anterior
	KeybY2 = KeybY1;
	KeybB2 = KeybB1;
	KeybD2 = KeybD1;

	KeybX1 = KeybX;		// Keys del Frame Actual
	KeybY1 = KeybY;
	KeybB1 = KeybB;
	KeybD1 = gc.KeyData;
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
int GameSizeX=128;
int GameSizeY=128;
int GameMaxX=176;	// 176
int GameMaxY=208;	// 208

int GameSound=1;
int GameVibra=1;
int GameConti=0;

int GameStatus=0;

int MonitorPaint;

Marco marco;

// -------------------
// Game INI
// ===================

public void GameINI()
{
	PrefsINI();

	GameSizeX=gc.CanvasSizeX; if (GameSizeX <= GameMaxX) {GameX=0;} else {GameX=(GameSizeX-GameMaxX)/2;GameSizeX=GameMaxX;}
	GameSizeY=gc.CanvasSizeY; if (GameSizeY <= GameMaxY) {GameY=0;} else {GameY=(GameSizeY-GameMaxY)/2;GameSizeY=GameMaxY;}

	marco = new Marco(GameSizeX,GameSizeY);

	gc.SoundINI();

	gc.scroll.ScrollTileTab = TilDatTab;
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
		gc.CanvasSET();
		GameStatus=22;
	break;


// ------------------
// Jugar Bucle
// ------------------
	case 100:
		JugarINI();
		GameStatus++;
	case 101:
		WaitSET(3000, GameStatus+1);

		gc.CanvasFillRGB=0; gc.CanvasFillPaint=1;
		PanelSET(0);

		JugarSET();

		gc.SoundSET(1,1);

		WaitSET();
	break;

	case 102:
		GameStatus++;
	case 103:
		if (KeybB1==-6 && KeybB2!=-6) {MarcoSET(30);}


		if (Monitor_ON!=-1) {GameStatus=162; break;}


		if ( !JugarRUN() ) {break;}


		FaseEND();


		GameStatus=101;

		switch (FaseExit)
		{
		case 1:	// Pasamos de Nivel

			GameConti=JugarLevel+1;

			if (++JugarLevel > 5)
				{
				JugarEND();

				gc.DiamanImg = gc.LoadImage("/Gfx/Loading.png");

				GameConti=0;

				GameStatus=122;
				}
		PrefsSET();

		break;

		case 2:	// Perdemos una Vida
			GameStatus=110;
		break;

		case 3:	// Terminar Juego
			GameStatus=112;
		break;
		}

		FaseExit=0;
	break;

	case 110:
		gc.CanvasFillRGB=0; gc.CanvasFillPaint=1;
		gc.CanvasImg=gc.LoadImage("/Gfx/Lost.png");

		WaitSET(3000, GameStatus+1);
	break;

	case 111:
//		if (JugarLevel == 0 || --JugarVidas > -1)
		{GameStatus=101; break;}

//		GameStatus++;
	case 112:
		gc.CanvasFillRGB=0; gc.CanvasFillPaint=1;

		PanelSET(1);

		gc.SoundSET(3,1);

		GameStatus++;
	break;

	case 113:
		WaitINI(3000, GameStatus+1);

		JugarEND();

		gc.DiamanImg = gc.LoadImage("/Gfx/Loading.png");

		WaitSET();
	break;

	case 114:
		GameStatus=62;
	break;
// ===================


// ------------------
// Monitor Bucle
// ------------------
	case 162:
		MonitorPaint=1;
		if (Monitor_ON==10) {GameStatus++;}
		GameStatus++;
	break;

	case 163:
		Monitor_ON=-1;
		WaitSET(2000, 103);
	break;

	case 164:
		Monitor_ON=-1;
		if (KeybB1!=0 && KeybB2==0) {GameStatus=103;}	// Espero pulsar una tecla
	break;
// ===================



// ------------------
// Intro Bucle
// ------------------
	case 22:
		gc.SoundSET(0,0);

		gc.IntroINI_Gfx(0);

		GameStatus++;
	case 23:
		if ((KeybB1==-6 && KeybB2!=-6) || (KeybB1==-7 && KeybB2!=-7) || (KeybB1==5 && KeybB2!=5) ) {} else if ( !gc.IntroRUN_Gfx() ) {break;}

		gc.IntroPaint=0;
		gc.IntroImg=null;

		GameStatus=62;
	break;
// ===================


// ------------------
// Menu Bucle
// ------------------
	case 62:
		if (gc.SoundOld!=0) {gc.SoundSET(0,0);}

		MenuINI();

		GameStatus++;
	case 63:
		if ( !MenuRUN() ) {break;}

		MenuEND();

		gc.SoundRES();

		GameStatus=100;
	break;
// ===================


// ------------------
// Final Bucle
// ------------------
	case 122:
		gc.IntroINI_Gfx(1);

		gc.SoundSET(0,0);

		GameStatus++;
	case 123:
		if ( !gc.IntroRUN_Gfx() ) {break;}

		gc.IntroPaint=0;
		gc.IntroImg=null;

		GameStatus++;
	break;

	case 124:
		gc.CanvasFillRGB=0; gc.CanvasFillPaint=1;
		PanelSET(2);
		GameStatus++;
	case 125:
		if (KeybB1!=0 && KeybB2==0) {} else {break;}

		gc.SoundRES();

		GameStatus=62;
	break;
// ===================


// ------------------
// Marco Bucle
// ------------------
	case 900:
		MarcoRUN();
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


// -------------------
// Game IMP
// ===================

public void GameIMP()
{
	switch (GameStatus)
	{
	case 103:
		JugarIMP();
	break;
	
	case 63:
		MenuIMP();
	break;
	}
}

// <=- <=- <=- <=- <=-










// ********************
// --------------------
// Menu - Engine
// ====================
// ********************

int MenuMode;
int MenuStart;

// ---------------
// Menu INI
// ===============

public void MenuINI()
{
	gc.MenuINI_Gfx();

	MenuIMP();

	MenuStart=0;
	MenuMode=0;
}


// ---------------
// Menu END
// ===============

public void MenuEND()
{
	gc.MenuEND_Gfx();

	gc.CanvasFillRGB=0; gc.CanvasFillPaint=1;
	gc.CanvasImg=gc.DiamanImg;
	gc.DiamanImg=null;
}


// ---------------
// Menu RUN
// ===============

public boolean MenuRUN()
{
	switch (MenuMode)
	{
	case 0:
		if (KeybB1!=0 && KeybB2==0) {MenuMode++;}	// Espero pulsar una tecla
	break;

	case 1:
		MarcoSET(0);	// Ponemos Marco
		MenuMode++;
	break;

	case 2:
		if (MenuStart!=0) {return true;}
		if (Marco_ON==0) {MenuIMP(); MenuMode=0;}
	break;
	}

	return false;
}


// ---------------
// Menu IMP
// ===============

public void MenuIMP()
{
	gc.CanvasFillRGB=0; gc.CanvasFillPaint=1;
	gc.CanvasImg=gc.MenuImg;
}

// <=- <=- <=- <=- <=-









// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

int JugarPaint;
int JugarLevel;
int JugarVidas;

int JugarLostPaint;

boolean JugarLlave;

boolean JugarFase5_Flag;
boolean JugarLlave_Flag;

// -------------------
// Jugar INI
// ===================

public void JugarINI()
{
	JugarLevel=(MenuStart==1?0:GameConti);

	JugarVidas=3;

	JugarPaint=0;

	JugarFase5_Flag=false;
	JugarLlave_Flag=false;

//	JugarLlave_Flag=true;		// ** QUITAR **

	JugarPanel_ON=-1;
	Monitor_ON=-1;

	gc.JugarINI_Gfx();
	FaseINI();
}


// -------------------
// Jugar SET
// ===================

public void JugarSET()
{
	JugarPanel_ON=-1;
	Monitor_ON=-1;

	FaseSET();

	gc.JugarSET_Gfx();
}


// -------------------
// Jugar END
// ===================

public void JugarEND()
{
	JugarPanel_ON=-1;
	Monitor_ON=-1;

	JugarPaint=0;
	gc.JugarEND_Gfx();
}


// -------------------
// Jugar RUN
// ===================

public boolean JugarRUN()
{
	FaseRUN();


	if (JugarPanelTime > 0 && --JugarPanelTime==0) {if (JugarPanelLoop!=0) {JugarPanel_ON=-1;} else {JugarPanelTime=JugarPanelMedi*2;}}


	if (FaseExit!=0) {return true;}

	JugarPaint=1;

	return false;
}


// -------------------
// Jugar IMP
// ===================

public void JugarIMP()
{
	JugarPaint=1;
}


// -------------------
// Jugar Panel SET
// ===================

int JugarPanel_ON;
int JugarPanelTime;
int JugarPanelMedi;
int JugarPanelLoop;

public void JugarPanelSET(int Type, int Time, int Loop)
{
	if (JugarPanel_ON!=2)
	{
	JugarPanel_ON=Type;
	JugarPanelTime=Time;
	JugarPanelMedi=Time/2;
	JugarPanelLoop=Loop;
	}
}


// -------------------
// Monitor SET
// ===================

int Monitor_ON;

public void MonitorSET(int Type)
{
	Monitor_ON=Type;
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Fase - Engine
// ===================
// *******************


int[] FaseSize = new int[] { 31,31,  75,75,  56,56,  56,56, 32,34,  32,40};

int[] FaseType = new int[] {0,1,1,1,0,1};

int FaseSizeX=1;
int FaseSizeY=1;
byte[] FaseMap;

int FaseExit;

// -------------------
// Fase INI
// ===================

public void FaseINI()
{

//	LoadFile(AnimMap, "/Data/Anims.map");

}


// -------------------
// Fase SET
// ===================

public void FaseSET()
{
	FaseExit=0;

	FaseSizeX=FaseSize[(JugarLevel*2)+0];
	FaseSizeY=FaseSize[(JugarLevel*2)+1];
	FaseMap = new byte[FaseSizeX*FaseSizeY];

	LoadFile(FaseMap, "/Data/Fase"+JugarLevel+".map");
	if (JugarLevel==5) {JugarFase5_Flag=true;}

//	AniTilINI();
	AniSprINI();

	LaserINI();
	EnemyINI();
	PuertaINI();
	ItemINI();

	ProtINI();


	FaseSET_Level();
}


// -------------------
// Fase SET Level
// ===================

public void FaseSET_Level()
{
	switch (JugarLevel)
	{
	case 0:
		for (int i=0 ; i<FaseSizeX*FaseSizeY ; i++)
		{
			if (FaseMap[i]==0x61) {LaserSET((i%FaseSizeX)*8, (i/FaseSizeX)*8, 1);}
			if (FaseMap[i]==0x63) {LaserSET((i%FaseSizeX)*8, (i/FaseSizeX)*8, 0);}
		}

		ProtSET(-32, 6, 1);
	break;

	case 1:

		int Dest=RND(6);
		int x= new int[] {69,66,69, 5,25,16} [Dest];
		int y= new int[] {52,40, 8,55,55, 9} [Dest];
		ItemSET( x*8,  y*8,  0,  1);	// Llave Puerta A
		ItemSET(10*8, 64*8,  1, 10);	// Lector Llave
		ItemSET( 6*8, 63*8,  0, 20, 24,10);	// Zona Salida

		PuertaSET(51*8, 24*8, 1, 5);	// Puerta Entrada
		PuertaSET( 6*8, 63*8, 1, 6);	// Puerta Salida

		PuertaSET(28*8, 31*8, 1, 0);	// Puerta
		PuertaSET( 6*8, 31*8, 1, 0);	// Puerta
		PuertaSET( 6*8, 23*8, 1, 0);	// Puerta
		PuertaSET(50*8, 47*8, 1, 0);	// Puerta
		PuertaSET(36*8, 59*8, 0, 7);	// Puerta Llave A


		EnemySET( 7*8, 13*8, 0);
		EnemySET(60*8, 14*8, 0);
		EnemySET(25*8, 44*8, 0);
		EnemySET( 5*8, 35*8, 0);
		EnemySET(24*8, 66*8, 0);
		EnemySET(57*8, 53*8, 0);
		EnemySET(23*8, 68*8, 5);

		ProtSET(51*8, 21*8, 0);
	break;	

	case 2:

		ItemSET(35*8, 44*8,  0,  1);	// Llave Puerta A
		ItemSET(45*8, 31*8,  0,  2);	// Llave Puerta B
		ItemSET(51*8, 44*8,  1, 10);	// Lector Llave
		ItemSET(48*8, 43*8,  0, 20, 24,10);	// Zona Salida
		ItemSET(20*8, 27*8,  0, 21, 40,8);	// Zona Alarma

		ItemSET(14*8,  5*8,  1, 15);	// Interruptor Electricidad

		PuertaSET( 5*8, 45*8, 1, 5);	// Puerta Entrada
		PuertaSET(47*8, 43*8, 1, 6);	// Puerta Salida

		PuertaSET( 5*8, 22*8, 1, 0);	// Puerta
		PuertaSET( 5*8, 34*8, 1, 0);	// Puerta
		PuertaSET(13*8, 45*8, 1, 0);	// Puerta
		PuertaSET(13*8, 34*8, 1, 0);	// Puerta
		PuertaSET(29*8, 23*8, 1, 0);	// Puerta
		PuertaSET(20*8, 23*8, 0, 7);	// Puerta Llave A
		PuertaSET(20*8, 41*8, 0, 8);	// Puerta Llave B

		EnemySET( 8*8, 27*8, 0);
		EnemySET(38*8, 19*8, 0);
		EnemySET(26*8,  9*8, 0);

		ProtSET( 5*8, 42*8, 0);
	break;	

	case 3:
		ItemSET(14*8, 42*8,  0,  1);	// Llave Tipo A
		ItemSET(22*8, 42*8,  0,  2);	// Llave Tipo B
		ItemSET(30*8, 42*8,  0,  3);	// Llave Tipo C

		ItemSET(38*8, 39*8,  1, 11);	// Switch 1
		ItemSET(40*8, 39*8,  1, 12);	// Switch 2
		ItemSET(42*8, 39*8,  1, 13);	// Switch 3

		PuertaSET( 5*8,  4*8, 1, 5);	// Puerta Entrada

		PuertaSET(37*8, 46*8, 1, 0);	// Puerta
		PuertaSET(13*8, 20*8, 0, 0);	// Puerta
		PuertaSET(13*8, 38*8, 1, 1);	// Puerta Clave 1
		PuertaSET(21*8, 38*8, 1, 2);	// Puerta Clave 2
		PuertaSET(29*8, 38*8, 1, 3);	// Puerta Clave 3
		PuertaSET(21*8, 20*8, 0, 4);	// Puerta Llave A+B+C

		ItemSET((31*8)+4, 24*8,  0, 25, 24,16);	// Zona Pillar Diamante

		ItemSET(12*8,  3*8,  0, 22, 32,8);	// Zona Salida Conductos

		EnemySET(28*8,  6*8, 5);
		EnemySET(48*8, 48*8, 5);

		ProtSET( 5*8,  1*8, 0);
	break;	

	case 4:
		for (int i=0 ; i<FaseSizeX*FaseSizeY ; i++)
		{
			if (FaseMap[i]==0x61) {LaserSET((i%FaseSizeX)*8, (i/FaseSizeX)*8, 1);}
			if (FaseMap[i]==0x63) {LaserSET((i%FaseSizeX)*8, (i/FaseSizeX)*8, 0);}
		}

		ItemSET(25*8, 11*8,  0,  1);	// Llave Puerta A (Fase 4)

		if (!JugarFase5_Flag)
		{
		ProtSET(12, 34*8, 2);
		} else {
		ProtSET(31*8, 14, 4);
		}
	break;

	case 5:
		PuertaSET( 9*8, 18*8, 0, 7);	// Puerta Llave A

		ItemSET(11*8,   7*8,  0, 24, 32,8);	// Zona Salida Conductos Fase 4

		ItemSET( 9*8,  19*8,  0, 23, 32,8);	// Zona Vision Total Terraza

		ItemSET(26*8,  33*8,  0, 26, 16,8);	// Zona Salida Escalera

		ProtSET((11*8)+4, 8*8, 3);
	break;
	}
}

// -------------------
// Fase RUN Level
// ===================

public void FaseRUN_Level()
{
}


// -------------------
// Fase END
// ===================

public void FaseEND()
{
	System.gc();

	FlechaEND();

	FaseMap=null;
	gc.scroll.FaseMap=null;

	LaserEND();
	EnemyEND();
	PuertaEND();
	ItemEND();


	ProtEND();


//	AniTilEND();
	AniSprEND();

	System.gc();
}


// -------------------
// Fase RUN
// ===================

public void FaseRUN()
{
	FlechaINI();

	AniSprRUN();

	LaserRUN();

	EnemyRUN();

	PuertaRUN();

	ItemRUN();

	ProtRUN();

}




// --------------------
// Fase Check Tiles
// ====================

int ChaPatDir;

public int CheckTile(int X, int Y, int Mask)
{
	if (Y < 0) {Y=0;} else if (Y >= FaseSizeY*8) {Y=(FaseSizeY-1)*8;}
	if (X < 0) {X=0;} else if (X >= FaseSizeX*8) {X=(FaseSizeX-1)*8;}

	ChaPatDir = ((Y/8)*FaseSizeX)+(X/8);
	return ((int)TilDatTab[FaseMap[ChaPatDir]+128]) & Mask;
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



byte TilDatTab[] = new byte[] {

// 001 = Obstaculo: Pared o Mesa
// 002 = Obstaculo: Pared
// 004 = Obstaculo: Techo
// 010 = Superponer: Techo (SIEMPRE)
// 020 = Superponer: Mesa  (SOLO Arrastrandote)
// 040 = Superponer: Sobre Piernas

// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x021,0x003,0x003,0x003, // 8
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // 9
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // A
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // B
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // C
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // D
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // E
0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // F
// 0     1     2     3     4     5     6     7     8     9     A     B     C     D     E     F
0x003,0x003,0x003,0x014,0x030,0x030,0x000,0x000,0x003,0x003,0x003,0x021,0x003,0x003,0x000,0x000, // 0
0x000,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x021,0x021,0x003,0x003,0x003,0x003,0x040, // 1
0x040,0x040,0x040,0x040,0x040,0x003,0x003,0x040,0x040,0x003,0x003,0x003,0x003,0x003,0x003,0x003, // 2
0x000,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x003,0x000,0x000, // 3
0x021,0x021,0x003,0x003,0x000,0x003,0x003,0x003,0x003,0x003,0x000,0x003,0x003,0x000,0x000,0x003, // 4
0x003,0x003,0x003,0x003,0x003,0x040,0x040,0x040,0x040,0x021,0x021,0x003,0x003,0x003,0x003,0x003, // 5
0x003,0x000,0x000,0x000,0x021,0x003,0x003,0x003,0x003,0x003,0x021,0x021,0x003,0x000,0x003,0x003, // 6
0x003,0x003,0x021,0x021,0x003,0x003,0x003,0x021,0x000,0x003,0x003,0x021,0x021,0x000,0x000,0x003, // 7
};

// -------------------------


// <=- <=- <=- <=- <=-






// *******************
// -------------------
// Prot - Engine
// ===================
// *******************

int ProtX=72;	// 0
int ProtY=72;	// 6
int ProtMode;
int ProtSideX=0;
int ProtSideY=1;
int ProtAnim=-1;
int ProtSalida;

// -------------------
// Prot INI
// ===================

public void ProtINI()
{
	ProtAnim = AniSprSET(-1, ProtX, ProtY, 0, 1, 2, 0x001);
}


// -------------------
// Prot SET
// ===================

public void ProtSET(int X, int Y, int Type)
{
	ProtX=X;
	ProtY=Y;

	ProtSalida=Type;

	switch (Type)
	{
	case 0:		// Inicio en Ascensor
		ProtSideX=0;
		ProtSideY=1;
		ProtSalidaSET(0);
	break;
	
	case 1:		// Inicio Arrastrando hacia Derecha
		ProtSideX=1;
		ProtSideY=0;
		ProtArrastrarSET();
		ProtSalidaSET(2);
	break;

	case 2:		// Inicio Arrastrando hacia Arriba
		ProtSideX=0;
		ProtSideY=-1;
		ProtArrastrarSET();
		ProtSalidaSET(2);
	break;

	case 3:		// Inicio Arrastrando hacia Abajo
		ProtSideX=0;
		ProtSideY=1;
		ProtArrastrarSET();
		ProtSalidaSET(2);
	break;

	case 4:		// Inicio Arrastrando hacia Izquierda
		ProtSideX=-1;
		ProtSideY=0;
		ProtArrastrarSET();
		ProtSalidaSET(2);
	break;
	}
}


// -------------------
// Prot END
// ===================

public void ProtEND()
{
	ProtAnim=-1;
}


// -------------------
// Prot RUN
// ===================

public void ProtRUN()
{
	ProtMover();

	if (FaseType[JugarLevel]!=0)
	{
		if ((KeybB1==10 && KeybB2!=10) || (KeybB1==-7 && KeybB2!=-7)) {ProtAgacharSET();}	// Caminar <= Agachar => Arrastrar

		if (ProtMode < 10 && KeybB1==5 && KeybB2!=5)
		{
		if (ProtMode==2) {ProtPegarSET();} else {ProtAgacharSET();}
		}
	} else {
	if (ProtX < -16) {ProtX=-16;}
	if (ProtY < -16) {FaseExit=1;}
	if (ProtX > (FaseSizeX*8)-8) {FaseExit=1;}
	if (ProtY > (FaseSizeY*8)-8) {ProtY=(FaseSizeY*8)-8;}
	}

//	ProtObjetos(ProtX+12,ProtY+12);

	AniSprs[ProtAnim].CoorX=ProtX;
	AniSprs[ProtAnim].CoorY=ProtY;
}


// -------------------
// Prot - Mover
// ===================

public void ProtMover()
{
	switch (ProtMode)
	{
	case 0:
		ProtArrastrarRUN();	// Arrastrar
	break;

	case 1:
		ProtPegarRUN();		// Pegar
	break;

	case 2:
		ProtCaminarRUN();	// Caminar
	break;

	case 3:
		ProtAgacharRUN();	// Agachar
	break;

	case 4:
		ProtSalidaRUN();		// Salida del Prot (Fase Completada)
	break;

	case 80:
		ProtMorirRUN();		// Salida del Prot (Fase Completada)
	break;
	}
}


// >>>>>>>>>>>>>>>>>>>>



// -----------------------
// Prot - Agachar - SET
// =======================

int ProtAgacharMode;

public void ProtAgacharSET()
{
	switch (ProtMode)
	{
	case 0:
		int Desp=0; if (ProtSideY==0) {Desp=8;} else if (ProtSideY==1) {Desp=14;} else {Desp=4;}

		if ((CheckTile(ProtX+6, ProtY-Desp+16, 11,5,  0x001) != 0)
		||	(CheckTile(ProtX+6, ProtY-Desp,    11,7,  0x004) != 0))
		{break;}
	case 2:
	case 3:
		AniSprSET(ProtAnim, ProtX, ProtY, ((ProtSideY==0)?(ProtSideX!=1?97:96):(ProtSideY!=1?99:98)), 8,  0, 0x021);
		ProtAgacharMode=ProtMode;
		ProtMode=3;
	break;
	}
}


// -----------------------
// Prot - Agachar - RUN
// =======================

public void ProtAgacharRUN()
{

	switch (ProtAgacharMode)
	{
	case 2:		// Venimos de Caminar
		if ( KeybX1!=0 )
		{
			if (KeybX1 == ProtSideX)
			{
				if ((CheckTile(ProtX+3+(KeybX1*6), ProtY+8+6, 17,11,  0x002) == 0)
				&&	(CheckTile(ProtX+6+(KeybX1*6), ProtY+8,   11, 7,  0x004) == 0))
				{
				ProtSideX=KeybX1; ProtSideY=0;
				ProtX+=(KeybX1*6);
				ProtY+=8;
				ProtArrastrarSET();
				}
			} else {
			ProtSideX=KeybX1; ProtSideY=0;
			AniSprs[ProtAnim].FrameIni=(ProtSideX!=1?97:96);
			}
		}

		if ( KeybY1!=0 )
		{
			if (KeybY1 == ProtSideY)
			{
				int Desp=(KeybY1!=1?2:14);
				if ((CheckTile(ProtX+6, ProtY+Desp+3, 11,17,  0x002) == 0)
				&&	(CheckTile(ProtX+6, ProtY+Desp,   11, 7,  0x004) == 0))
				{
				ProtSideY=KeybY1; ProtSideX=0;
				ProtY+=Desp;
				ProtArrastrarSET();
				}
			} else {
			ProtSideY=KeybY1; ProtSideX=0;
			AniSprs[ProtAnim].FrameIni=(ProtSideY!=1?99:98);
			}
		}
	break;

	case 0:	// Venimos de Arrastrar
		if (ProtSideY==0) {ProtY-=8;} else if (ProtSideY==1) {ProtY-=14;} else {ProtY-=4;}
		ProtX&=-2;
		ProtY&=-2;
		ProtAgacharMode=500;
	break;

	default:
		ProtCaminarSET();
	break;
	}

}

// <<<<<<<<<<<<<<<<<<<<



// -----------------------
// Prot - Pegar - SET
// =======================

public void ProtPegarSET()
{
	AniSprSET(ProtAnim, ProtX, ProtY, ((ProtSideY==0)?(ProtSideX!=1?110:108):(ProtSideY!=1?114:112)), 2,  1, 0x002);

	ProtMode=1;
}


// -----------------------
// Prot - Pegar - RUN
// =======================

public void ProtPegarRUN()
{
	if (AniSprs[ProtAnim].Pause==0) {ProtCaminarSET();}
}

// <<<<<<<<<<<<<<<<<<<<



// -----------------------
// Prot - Arrastrar - SET
// =======================

public void ProtArrastrarSET()
{
	AniSprSET(ProtAnim, ProtX, ProtY, ((ProtSideY==0)?(ProtSideX!=1?8:0):(ProtSideY!=1?24:16)), 8,  0, 0x021);

	ProtMode=0;
}


// -----------------------
// Prot - Arrastrar - RUN
// =======================

public void ProtArrastrarRUN()
{
	if ( KeybX1!=0 )
	{
		if ((CheckTile(ProtX+3+KeybX1, ProtY+6, 17,11,  0x002) == 0)
		&&	(CheckTile(ProtX+6+KeybX1, ProtY,   11, 7,  0x004) == 0))
		{
			ProtSideX=KeybX1; ProtSideY=0;
			AniSprs[ProtAnim].FrameIni=(ProtSideX!=1?8:0);
			ProtX+=KeybX1;
			AniSprs[ProtAnim].Pause=1;
		}
		else if (( ProtSideX==0 )
		&&	(CheckTile(ProtX+6+KeybX1, ProtY+3, 11,17,  0x002) == 0)
		&&	(CheckTile(ProtX+6+KeybX1, ProtY,   11, 7,  0x004) == 0))
		{
		ProtX+=KeybX1;
		AniSprs[ProtAnim].Pause=1;
		}
	}


	if ( KeybY1!=0 )
	{
		if ((CheckTile(ProtX+6, ProtY+3+KeybY1, 11,17,  0x002) == 0)
		&&	(CheckTile(ProtX+6, ProtY+KeybY1,   11, 7,  0x004) == 0))
		{
			ProtSideY=KeybY1; ProtSideX=0;
			AniSprs[ProtAnim].FrameIni=(ProtSideY!=1?24:16);
			ProtY+=KeybY1;
			AniSprs[ProtAnim].Pause=1;
		}
		else if (( ProtSideY==0 )
		&&	(CheckTile(ProtX+3, ProtY+6+KeybY1, 17,11,  0x002) == 0)
		&&	(CheckTile(ProtX+6, ProtY+KeybY1,   11, 7,  0x004) == 0))
		{
		ProtY+=KeybY1;
		AniSprs[ProtAnim].Pause=1;
		}
	}
}

// <<<<<<<<<<<<<<<<<<<<


// -----------------------
// Prot - Caminar - SET
// =======================

public void ProtCaminarSET()
{
	AniSprSET(ProtAnim, ProtX, ProtY, ((ProtSideY==0)?(ProtSideX!=1?40:32):(ProtSideY!=1?56:48)), 8,  1, 0x021);

	ProtMode=2;
}


// -----------------------
// Prot - Caminar - RUN
// =======================

public void ProtCaminarRUN()
{
	if ( KeybX1 == 0 && KeybY1 == 0) {AniSprs[ProtAnim].FrameAct=0; return;}

	if ( KeybX1!=0 )
	{
		if ((CheckTile(ProtX+6+KeybX1, ProtY+16,11,5,  0x001) == 0)
		&&	(CheckTile(ProtX+6+KeybX1, ProtY,   11,7,  0x004) == 0))
		{
		ProtSideX=KeybX1; ProtSideY=0;
		ProtX+=ProtSideX*2;
		AniSprs[ProtAnim].FrameIni=(ProtSideX!=1?40:32);
		AniSprs[ProtAnim].Pause=1;
		}
		else if ( KeybY1 == 0 )
		{
		if ((CheckTile(ProtX+6+KeybX1, ProtY+8+16,11,5,  0x001) == 0)
		&&	(CheckTile(ProtX+6+KeybX1, ProtY+8,   11,7,  0x004) == 0)) {ProtY+=2; AniSprs[ProtAnim].Pause=1;}
		else
		if ((CheckTile(ProtX+6+KeybX1, ProtY-8+16,11,5,  0x001) == 0)
		&&	(CheckTile(ProtX+6+KeybX1, ProtY-8,   11,7,  0x004) == 0)) {ProtY-=2; AniSprs[ProtAnim].Pause=1;}
		}
	}


	if ( KeybY1!=0 )
	{
		if ((CheckTile(ProtX+6, ProtY+16+KeybY1,11,5,  0x001) == 0)
		&&	(CheckTile(ProtX+6, ProtY+KeybY1,   11,7,  0x004) == 0))
		{
		ProtSideY=KeybY1; ProtSideX=0;
		ProtY+=ProtSideY*2;
		AniSprs[ProtAnim].FrameIni=(ProtSideY!=1?56:48);
		AniSprs[ProtAnim].Pause=1;
		}
		else if ( KeybX1 == 0 )
		{
		if ((CheckTile(ProtX+6+8, ProtY+16+KeybY1,11,5,  0x001) == 0)
		&&	(CheckTile(ProtX+6+8, ProtY+KeybY1,   11,7,  0x004) == 0)) {ProtX+=2; AniSprs[ProtAnim].Pause=1;}
		else
		if ((CheckTile(ProtX+6-6, ProtY+16+KeybY1,11,5,  0x001) == 0)
		&&	(CheckTile(ProtX+6-6, ProtY+KeybY1,   11,7,  0x004) == 0)) {ProtX-=2; AniSprs[ProtAnim].Pause=1;}
		}
	}
}

// <<<<<<<<<<<<<<<<<<<<



// -----------------------
// Prot - Salida SET
// =======================

int ProtSalidaMode=0;

public void ProtSalidaSET(int Type)
{
	if (Type==2)
	{
	AniSprs[ProtAnim].Pause=14;
	} else {
	AniSprSET(ProtAnim, ProtX, ProtY, 48+(Type*8), 8, 1, 0x001);
	AniSprs[ProtAnim].Pause=8;
	}

	ProtSalidaMode=Type*10;
	ProtMode=4;
}

// -----------------------
// Prot - Salida RUN
// =======================

public void ProtSalidaRUN()
{
	switch (ProtSalidaMode)
	{
// Entrada en la Fase
// ------------------
	case 0:
		if (AniSprs[ProtAnim].Pause!=0) {break;}
		AniSprs[ProtAnim].Pause=12;
		PuertaEntrada=1;
		ProtSalidaMode++;
	break;

	case 1:
		if (AniSprs[ProtAnim].Pause!=0) {ProtY+=2; break;}

		PuertaEntrada=0;
		ProtSideX=0;
		ProtSideY=1;
		ProtCaminarSET();
	break;

// Salida de la Fase
// -----------------
	case 10:
		if (AniSprs[ProtAnim].Pause!=0) {ProtY-=2; break;}
		AniSprs[ProtAnim].Pause=12;
		PuertaSalida=0;
		ProtSalidaMode++;
	break;

	case 11:
		if (AniSprs[ProtAnim].Pause!=0) {break;}

		FaseExit=1;
	break;

// Entrada de la Fase de Tuberias
// ------------------------------
	case 20:
		if (AniSprs[ProtAnim].Pause!=0)
		{
			switch (ProtSalida)
			{
			case 1: ProtX++; break;
			case 2: ProtY--; break;
			case 3: ProtY++; break;
			case 4: ProtX--; break;
			}
		break;
		}
		ProtArrastrarSET();
	break;
	}
}

// <<<<<<<<<<<<<<<<<<<<


// -----------------------
// Prot - Morir - SET
// =======================

int ProtTime;

public void ProtMorirSET()
{
	if (CheatInmune==0 && ProtMode!=80)
	{
	ProtMode=80;
	ProtTime=0;

	gc.SoundSET(2,8);
	gc.VibraSET(250);
	}
}



// -----------------------
// Prot - Morir - RUN
// =======================

public void ProtMorirRUN()
{
	if ((ProtTime &  0x02) == 0) {AniSprs[ProtAnim].Bank=0;} else {AniSprs[ProtAnim].Bank=10;}

	if (ProtTime++ < 0x20) {return;}

	FaseExit=2;
}

// <<<<<<<<<<<<<<<<<<<<




// -------------------
// Prot Objetos
// ===================

public void ProtObjetos(int X, int Y)
{
	int Objeto=CheckTile(X,Y, 0x0F0) >> 4;

	switch (Objeto)
	{
	case 0:
	return;

	case 0x1:	// Llave
		FaseMap[ChaPatDir  ]=0x30;
		JugarLlave=true;
	break;

	case 0x2:	// Salida
		if (JugarLlave) {JugarLlave=false; ProtSalidaSET(1);}
	break;
	}

}



// <=- <=- <=- <=- <=-














// ********************
// --------------------
// Enemy - Engine
// ====================
// ********************

int[] EnemyRotar = new int[] {
	 1, 0,
	 1,-1,
	 0,-1,
	-1,-1,
	-1, 0,
	-1, 1,
	 0, 1,
	 1, 1,
};

public class Enemy extends AnimSprite
{
int Mode=0;
int SideX=1;
int SideY=0;
int Time=0;

int OrigX;
int OrigY;
int DespX;
int DespY;
int Total;
int NowX;
int NowY;
int DespX2;
int DespY2;
int Total2;
int Cont;
int Cnt=1;
int Stado;

// ----------------
// Enemy Play
// ================

public boolean Play()
{

	if (Mode<40)
	{
	if ( ProtMode!=80 && Colision(CoorX+8, CoorY+16+(SideY*4), 8,6,   ProtX+8,ProtY+16, 8,6) ) {FrameAct=0; Mode=100; ProtMorirSET();}
	else
	if ( ProtMode==1 && Colision(CoorX+8, CoorY+16, 8,6,   ProtX+8+(ProtSideX*6),ProtY+16+(ProtSideY*6), 8+4,6+4) ) {Time=0; Mode=40;}
	}


	if ((Mode<40)
	&&	( !Colision(CoorX+8, CoorY+4, 8,16,   gc.scroll.ScrollX,    gc.scroll.ScrollY,    gc.scroll.ScrollSizeX,    gc.scroll.ScrollSizeY) )
	&&	(  Colision(CoorX+8, CoorY+4, 8,16,   gc.scroll.ScrollX-32, gc.scroll.ScrollY-32, gc.scroll.ScrollSizeX+64, gc.scroll.ScrollSizeY+64) ))
	{
	int X = CoorX+12 - gc.scroll.ScrollX;
	int Y = CoorY+12 - gc.scroll.ScrollY;

	int Frame=120;
	if (X < 3) {X=3;Frame=123;} else if (X > gc.scroll.ScrollSizeX) {X = gc.scroll.ScrollSizeX-4;Frame=121;}
	if (Y < 3) {Y=3;Frame=120;} else if (Y > gc.scroll.ScrollSizeY) {Y = gc.scroll.ScrollSizeY-4;Frame=122;}

	FlechaSET(X-3,Y-3, Frame);
	}



	if (Mode==0 || Mode==1 || Mode==5)
	{
		Stado = Azechar();
		if (Stado==0)
		{
		OrigX=NowX;
		OrigY=NowY;
		DespX=DespX2;
		DespY=DespY2;
		Total=Total2;
		Cont=0;
		Cnt=1;
		Mode=20;
		}
	}


	switch (Mode)
	{

// Patrullando
// -----------
	case 0:
		if (--Time > 0) {break;}
		Cont=RND(8);
		SideX=EnemyRotar[(Cont*2)];
		SideY=EnemyRotar[(Cont*2)+1];

		if ((CheckTile(CoorX+6+SideX, CoorY+SideY+16, 11,5,  0x001) == 0)
		&&	(CheckTile(CoorX+6+SideX, CoorY+SideY,    11,7,  0x004) == 0))
		{Mode++;}
	break;

	case 1:
		if ((CheckTile(CoorX+6+SideX, CoorY+SideY+16, 11,5,  0x001) == 0)
		&&	(CheckTile(CoorX+6+SideX, CoorY+SideY,    11,7,  0x004) == 0))
		{
		CoorX+=SideX;
		CoorY+=SideY;
		Pause=1;
		}
		else
		{
		FrameAct=0;
		SideX*=-1;
		SideY*=-1;
		Time=80;
		Mode=0;
		}
	break;


// Ruta X Fija
// -----------
	case 5:
		if ((CheckTile(CoorX+6+SideX, CoorY+16, 11,5,  0x001) == 0)
		&&	(CheckTile(CoorX+6+SideX, CoorY,    11,7,  0x004) == 0))
		{
		CoorX+=SideX;
		Pause=1;
		} else{
		SideX*=-1;
		}
	break;


// Te Persigue
// -----------
	case 20:

		if (Cnt > 0 && --Cnt<1) {Cnt=8; Stado = Azechar();} else {Stado=3;}

		switch (Stado)
		{
		case 0:

			OrigX=NowX;
			OrigY=NowY;
			DespX=DespX2;
			DespY=DespY2;
			Total=Total2;
			
			Cont=0;

		case 3:

			if (Cont>=Total) {Mode++; break;}

			Cont+=2;

			int NewX=OrigX+( ((Cont*DespX)/Total) );
			int NewY=OrigY+( ((Cont*DespY)/Total) );

			if ((CheckTile(NewX+6, NewY+16, 11,5,  0x001) == 0)
			&&	(CheckTile(NewX+6, NewY,    11,7,  0x004) == 0))
			{
			CoorX=NewX;
			CoorY=NewY;
			} else {
			Mode=0;
			break;
			}

			if (Total-6 == DespX || Total-6 == (DespX*-1))
			{
			SideY=0;
			SideX=(DespX<0?-1:1);
			} else {
			SideX=0;
			SideY=(DespY<0?-1:1);
			}

			Pause=1;

		break;

		default:
			Mode++;
		break;
		}

	break;

	case 21:
		Stado = Azechar();
		if (Stado==0)
		{
		OrigX=NowX;
		OrigY=NowY;
		DespX=DespX2;
		DespY=DespY2;
		Total=Total2;
		Cont=0;
		Cnt=1;
		Mode=20;
		}
		else
		{
		Mode=0;
		}
	break;


// Cae al suelo
// ------------
	case 40:
		this.AniSprSET((SideY!=1?118:116), 2, 2, 0x002);
		Mode++;
	break;

	case 41:
		if (Time++ < 600) {break;}

		this.AniSprSET(64, 8, 1, 0x021);
		Time=0;
		Mode=0;
	break;
	}


	if (Mode < 40)
	{
	if (SideY==0) {FrameIni=(SideX!=1?72:64);} else {FrameIni=(SideY!=1?88:80);}
	}

	return super.Play();
}


public int Azechar()
{
	DespX2=ProtX-CoorX;
	DespY2=ProtY-CoorY;

	if (DespX2==0 && DespY2==0) {return 1;}

	if (JugarPanel_ON!=2 && Mode<20)
	{
	if (DespX2<0 && SideX>0 || DespX2>0 && SideX<0) {return 3;}
	if (DespY2<0 && SideY>0 || DespY2>0 && SideY<0) {return 3;}
	}

	int TotalX=(DespX2>0?DespX2:DespX2*-1);
	int TotalY=(DespY2>0?DespY2:DespY2*-1);

	if (TotalX > gc.scroll.ScrollSizeX+32 || TotalY > gc.scroll.ScrollSizeY+32) {return 2;}

	Total2=(TotalX<TotalY?TotalY:TotalX)+6;

	NowX=CoorX;
	NowY=CoorY;

	for (int Cont=0 ; Cont<Total2 ; Cont+=2)
	{
		int CorX=NowX+( ((Cont*DespX2)/Total2) );
		int CorY=NowY+( ((Cont*DespY2)/Total2) );

		if ((CheckTile(CorX+6, CorY+16, 11,5,  0x001) == 0)
		&&	(CheckTile(CorX+6, CorY,    11,7,  0x004) == 0))
		{} else {return 3;}
	}

	return 0;
}

};


// ---------------
// Enemy SET
// ===============

public int EnemySET(int CoorX, int CoorY, int Mode)
{
	if (EnemyC == EnemyMAX) {return(-1);}

	int i=EnemyP[EnemyC++];

	Enemys[i] = new Enemy();

	Enemys[i].AniSprSET(CoorX, CoorY,  64, 8, 1, 0x021);

	Enemys[i].Mode=Mode;

	return (i);
}


// ---------------
// Enemy INI
// ===============

final int EnemyMAX=12;
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
	for (int i=0 ; i<EnemyC ; i++)
	{
	if ( Enemys[EnemyP[i]].Play() )
		{
		int t=EnemyP[i];
		Enemys[t] = null;
		EnemyP[i--] = EnemyP[--EnemyC];
		EnemyP[EnemyC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-











// ********************
// --------------------
// Laser - Engine
// ====================
// ********************

public class Laser extends AnimSprite
{
int Mode=0;
int Type;
int Time;

// ----------------
// Laser Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		if (--Time>0) {break;}
		Time=16;
		Mode++;
	break;

	case 1:
		if ((Time&1)==0) {Bank=0; Pause=1;} else {Bank=10;}
		if (--Time>0) {break;}
		Time=60+RND(50);
		Bank=0;
		Mode++;
	break;

	case 2:
		Pause=1;
		if (--Time>0) {break;}
		Time=16;
		Mode++;
	break;

	case 3:
		if ((Time&1)==0) {Bank=0;} else {Bank=10;}
		if (--Time>0) {break;}
		Time=36+RND(12);
		Bank=10;
		Mode=0;
	break;
	}


	if ( Mode!=0 && Type==0 && Colision(CoorX+3, CoorY, 1,24,   ProtX+9,ProtY+6, 12,12) ) {ProtMorirSET();}
	else
	if ( Mode!=0 && Type==1 && Colision(CoorX, CoorY+2, 24,1,   ProtX+6,ProtY+7, 12,14) ) {ProtMorirSET();}


	return super.Play();
}

};


// ---------------
// Laser SET
// ===============

public int LaserSET(int CoorX, int CoorY, int Type)
{
	if (LaserC == LaserMAX) {return(-1);}

	int i=LaserP[LaserC++];

	Lasers[i] = new Laser();

	if (Type==0)
	{
	Lasers[i].AniSprSET(CoorX+3, CoorY+2,  100, 4, 1, 0x021);
	} else {
	Lasers[i].AniSprSET(CoorX+1, CoorY+3,  104, 4, 1, 0x021);
	}

	Lasers[i].Type=Type;

	return (i);
}


// ---------------
// Laser INI
// ===============

final int LaserMAX=24;
int LaserC;
int[] LaserP;
Laser[] Lasers;

public void LaserINI()
{
	LaserC = 0;
	LaserP = new int[LaserMAX];
	Lasers = new Laser[LaserMAX];
	for (int i=0 ; i<LaserMAX ; i++) {LaserP[i]=i;}
}

// ---------------
// Laser RES
// ===============

public void LaserRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=LaserP[i];
	Lasers[t] = null;
	LaserP[i--] = LaserP[--LaserC];
	LaserP[LaserC] = t;
}

// ---------------
// Laser END
// ===============

public void LaserEND()
{
	LaserP = null;
	Lasers = null;
	LaserC = 0;
}

// ---------------
// Laser RUN
// ===============

public void LaserRUN()
{
	for (int i=0 ; i<LaserC ; i++)
	{
	if ( Lasers[LaserP[i]].Play() )
		{
		int t=LaserP[i];
		Lasers[t] = null;
		LaserP[i--] = LaserP[--LaserC];
		LaserP[LaserC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-













// ********************
// --------------------
// Puerta - Engine
// ====================
// ********************

int PuertaEntrada;
int PuertaSalida;

public class Puerta extends AnimSprite
{

int Mode;
int Margen;
int Open;
int Flag=0;
byte[] Mapa = new byte[4];

// ----------------
// Puerta Play
// ================

public boolean Play()
{
	switch (Mode)
	{
	case 0:
		if ( Pause!=0 ) {break;}

		int Dest=((CoorY/8)*FaseSizeX)+(CoorX/8);
		FaseMap[Dest+1]=0x2B;
		FaseMap[Dest+2]=0x2B;
		FaseMap[Dest+1+(FaseSizeX*2)]=0x2B;
		FaseMap[Dest+2+(FaseSizeX*2)]=0x2B;
		Mode++;
	case 1:
		if ( !Colision(CoorX-8, CoorY-8, Margen,52,  ProtX+6,ProtY+14, 12,6) ) {Flag=0; break;}

		if (( Open==0 )																// Puerta Standard
		||	( Open==1 && ItemData[11]==1 && ItemData[12]==0 && ItemData[13]==0 )	// Puerta Clave 1 (211)
		||	( Open==2 && ItemData[11]==0 && ItemData[12]==1 && ItemData[13]==0 )	// Puerta Clave 2 (121)
		||	( Open==3 && ItemData[11]==0 && ItemData[12]==1 && ItemData[13]==1 )	// Puerta Clave 3 (122)
		||	( Open==4 && ItemData[ 1]!=0 && ItemData[ 2]!=0 && ItemData[ 3]!=0 )	// Puerta Llave A+B+C
		||	( Open==5 && PuertaEntrada!=0 )	// Puerta Entrada
		||	( Open==6 && PuertaSalida!=0 )	// Puerta Salida
		||	( Open==7 && ItemData[ 1]!=0 )	// Puerta Llave A
		||	( Open==8 && ItemData[ 2]!=0 )	// Puerta Llave B
		||	( Open==7 && JugarLlave_Flag ))	// Puerta Fase 5 (Terraza)
		{
		Side=1; Pause=-1; Mode++;

		Dest=((CoorY/8)*FaseSizeX)+(CoorX/8);
		FaseMap[Dest+1]=Mapa[0];
		FaseMap[Dest+2]=Mapa[1];
		FaseMap[Dest+1+(FaseSizeX*2)]=Mapa[2];
		FaseMap[Dest+2+(FaseSizeX*2)]=Mapa[3];
		}
		else if (Flag==0 && Open!=5 && Open!=6)
		{
		Flag=1;
		JugarPanelSET(3, 120, 1);
		}


	break;

	case 2:
		if (( Pause==0 && (Open<=4 || Open>=7) && !EntrePuertas() )
		||	( Pause==0 && Open==5 && PuertaEntrada==0)	// Puerta Entrada
		||	( Pause==0 && Open==6 && PuertaSalida==0))	// Puerta Salida
		{
		Side=-1; Pause=-1; Mode=0;
		}
	break;
	}

	return super.Play();
}


public boolean EntrePuertas()
{
	if (Colision(CoorX-8, CoorY-8, Margen,52,  ProtX+6,ProtY+14, 12,6)) {return true;}

	for (int i=0 ; i<EnemyC ; i++)
	{
	if ( Colision(CoorX-8, CoorY-8, Margen,52,  Enemys[i].CoorX+6,Enemys[i].CoorY+14, 12,6) ) {return true;}
	}

	return false;
}


};


// ---------------
// Puerta SET
// ===============

public int PuertaSET(int CoorX, int CoorY, int Type, int Open)
{
	if (PuertaC == PuertaMAX) {return(-1);}

	int i=PuertaP[PuertaC++];

	Puertas[i] = new Puerta();

	Puertas[i].AniSprSET(CoorX, CoorY,  Type*4, 4, 1, 0x122);

	Puertas[i].Margen=(Type==0?48:40);
	Puertas[i].Open=Open;

	int Dest=((CoorY/8)*FaseSizeX)+(CoorX/8);
	Puertas[i].Mapa[0]=FaseMap[Dest+1];
	Puertas[i].Mapa[1]=FaseMap[Dest+2];
	Puertas[i].Mapa[2]=FaseMap[Dest+1+(FaseSizeX*2)];
	Puertas[i].Mapa[3]=FaseMap[Dest+2+(FaseSizeX*2)];

	PuertaEntrada=0;
	PuertaSalida=0;

	return (i);
}


// ---------------
// Puerta INI
// ===============

final int PuertaMAX=12;
int PuertaC;
int[] PuertaP;
Puerta[] Puertas;

public void PuertaINI()
{
	PuertaC = 0;
	PuertaP = new int[PuertaMAX];
	Puertas = new Puerta[PuertaMAX];
	for (int i=0 ; i<PuertaMAX ; i++) {PuertaP[i]=i;}
}

// ---------------
// Puerta RES
// ===============

public void PuertaRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=PuertaP[i];
	Puertas[t] = null;
	PuertaP[i--] = PuertaP[--PuertaC];
	PuertaP[PuertaC] = t;
}

// ---------------
// Puerta END
// ===============

public void PuertaEND()
{
	PuertaP = null;
	Puertas = null;
	PuertaC = 0;
}

// ---------------
// Puerta RUN
// ===============

public void PuertaRUN()
{
	for (int i=0 ; i<PuertaC ; i++)
	{
	if ( Puertas[PuertaP[i]].Play() )
		{
		int t=PuertaP[i];
		Puertas[t] = null;
		PuertaP[i--] = PuertaP[--PuertaC];
		PuertaP[PuertaC] = t;
		}
	}
}

// <=- <=- <=- <=- <=-












// *****************************************
// -----------------------------------------
// Anim Sprite - Engine - Rev.2 (22.05.2003)
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

final int AniSprMAX=4;
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

	AniSprs[i].AniSprSET(CoorX, CoorY, FrameIni, Frames, Speed, Mode);

	return (i);
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
// Item - Engine
// ====================
// ********************

int[] ItemData = new int[30];
byte[] ItemMap = new byte[10];

public class Item
{

int CoorX;
int CoorY;
int SizeX;
int SizeY;
int Dest;
int Mode;
int Type;

// ----------------
// Item Play
// ================

public boolean Play()
{
	if ( Mode==0 || (Mode==1 && KeybB1==5 && KeybB2!=5) ) {} else {return false;}

	if ( !Colision(CoorX, CoorY, SizeX,SizeY,  ProtX+9, ProtY+6, 6,14 ) ) {return false;}

	switch (Type)
	{
	case 0:		// Llave Salida
	case 1:		// Llave Tipo A
	case 2:		// Llave Tipo B
	case 3:		// Llave Tipo C
		FaseMap[Dest]=ItemMap[Type];
		ItemData[Type]=1;

		if (JugarLevel==4) {JugarLlave=true;}

		gc.SoundSET(4,1);
		gc.VibraSET(250);

		MonitorSET(Type);
	return true;

	case 10:	// Lector Llave Salida
		ItemData[10]=1; PuertaSalida=1;
	return true;
//		if (ItemData[0]!=0) {ItemData[10]=1; PuertaSalida=1; return true;}

	case 11:	// Switch 1
	case 12:	// Switch 2
	case 13:	// Switch 3
		if (++ItemData[Type]==2) {ItemData[Type]=0;}
		FaseMap[Dest]=(byte)(ItemData[Type]==0?0x60:0x79);

		gc.SoundSET(4,1);
		gc.VibraSET(250);

		JugarPanelSET(ItemData[Type], 120,1);
	break;

	case 15:	// Interruptor Electricidad
		if (ItemData[Type]==0)
		{
		ItemData[Type]=1;

		gc.BorraAlarma();

		FaseMap[Dest]=0x79;

		gc.SoundSET(4,1);
		gc.VibraSET(250);

		JugarPanelSET(ItemData[Type], 120,1);
		}
	break;

	case 20:	// Zona Salida
		if (ItemData[10]!=0) {ProtSalidaSET(1); return true;}
	break;

	case 21:	// Zona Alarma
		if (ItemData[15]==0)
			{
			EnemySET(19*8, 38*8, 0);
			EnemySET(23*8, 38*8, 0);

			gc.SoundSET(2,8);
			gc.VibraSET(250);

			JugarPanelSET(2, 30, 0);	// Alarma ON
			}
	return true;

	case 22:	// Zona Salida a Conductos
		if (ItemData[25]!=0) {FaseExit=1;}
	break;

	case 23:	// Zona Vision Terraza Total
		EnemySET( 3*8, 30*8, 0);
		EnemySET(26*8, 30*8, 0);
		EnemySET(26*8,  5*8, 5);
	return true;

	case 24:	// Zona Salida Conductos Fase 4
		JugarLevel-=2;
		FaseExit=1;
	return true;

	case 25:	// Zona Pillar Diamante
		ItemData[Type]=1;

		FaseMap[Dest+1]=0x19;
		FaseMap[Dest+2]=0x1A;
		FaseMap[Dest+FaseSizeX+1]=0x19;
		FaseMap[Dest+FaseSizeX+2]=0x1A;

		JugarPanelSET(2, 30, 0);	// Alarma ON

		gc.SoundSET(2,8);
		gc.VibraSET(250);

		EnemySET(14*8, 34*8, 5);

		MonitorSET(10);
	return true;

	case 26:	// Zona Salida Escalera
		FaseExit=1;
	return true;
	}

	return false;
}

};


// ---------------
// Item SET
// ===============

public int ItemSET(int CoorX, int CoorY, int Mode, int Type)
{
	return ItemSET(CoorX, CoorY, Mode, Type, 6,10);
}

public int ItemSET(int CoorX, int CoorY, int Mode, int Type, int SizeX, int SizeY)
{
	if (ItemC == ItemMAX) {return(-1);}

	int i=ItemP[ItemC++];

	Items[i] = new Item();

	Items[i].CoorX=CoorX;
	Items[i].CoorY=CoorY;
	Items[i].SizeX=SizeX;
	Items[i].SizeY=SizeY;
	int Dest=((CoorY/8)*FaseSizeX)+(CoorX/8);
	Items[i].Dest=Dest;
	Items[i].Mode=Mode;
	Items[i].Type=Type;

	ItemData[Type]=0;

	switch (Type)
	{
	case 0:		// Llave Salida
	case 1:		// Llave Tipo A
	case 2:		// Llave Tipo B
	case 3:		// Llave Tipo C
		ItemMap[Type]=FaseMap[Dest];
		FaseMap[Dest]=0x6D;
	break;

	case 11:	// Switch 1
	case 12:	// Switch 2
	case 13:	// Switch 3
	case 15:	// Interruptor Electricidad
		ItemData[Type]=0;
		FaseMap [Dest]=(byte)(0x60);
	break;
	}

	return (i);
}


// ---------------
// Item INI
// ===============

final int ItemMAX=16;
int ItemC;
int[] ItemP;
Item[] Items;

public void ItemINI()
{
	ItemC = 0;
	ItemP = new int[ItemMAX];
	Items = new Item[ItemMAX];
	for (int i=0 ; i<ItemMAX ; i++) {ItemP[i]=i;}
}

// ---------------
// Item RES
// ===============

public void ItemRES_Pos(int i)	// i = a la POSICION de 'P' NO al n� de objeto
{
	int t=ItemP[i];
	Items[t] = null;
	ItemP[i--] = ItemP[--ItemC];
	ItemP[ItemC] = t;
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
// Flecha - Engine
// ====================
// ********************

final int FlechaMAX=8;
int FlechaC=0;
int[][] Flechas;

// ---------------
// Flecha INI
// ===============

public void FlechaINI()
{
	FlechaC = 0;
	Flechas = new int[FlechaMAX][];
}

// ---------------
// Flecha SET
// ===============

public void FlechaSET(int CoorX, int CoorY, int Type)
{
	if (FlechaC == FlechaMAX) {return;}

	Flechas[FlechaC] = new int[3];

	Flechas[FlechaC][0] = CoorX;
	Flechas[FlechaC][1] = CoorY;
	Flechas[FlechaC][2] = Type;

	FlechaC++;
}

// ---------------
// Flecha END
// ===============

public void FlechaEND()
{
	Flechas = null;
	FlechaC = 0;
}

// <=- <=- <=- <=- <=-







// *******************
// -------------------
// Panel - Engine
// ===================
// *******************

int PanelPaint=0;
int PanelType;

// -------------------
// Panel SET
// ===================

public void PanelSET(int Type)
{
	PanelPaint=1;
	PanelType=Type;
}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// Marco - Engine
// ===================
// *******************

int Marco_ON;
int MarcoStatus;
int MarcoStatusOld;
int MarGameStatus;
int MarHelpFlag=1;

// -------------------
// Marco SET
// ===================

public void MarcoSET(int Status)
{
	MarcoStatusOld=MarcoStatus;
	MarcoStatus=Status;

	switch (Status)
	{
	case 00:
		if (MarHelpFlag==0)
		{
		MarHelpFlag=1;
		Texto.MenuInfo(marco);
		break;
		}
		MarcoStatus=10;

	case 10:
		marco.MarcoINI();
		if (GameConti!=0)
		{
		marco.MarcoADD(0x011,Texto.Continue,10);
		}
		marco.MarcoADD(0x011,Texto.Start,1);
		marco.MarcoADD(0x011,Texto.Sound,2,GameSound);
		marco.MarcoADD(0x011,Texto.Vibrate,3,GameVibra);
		marco.MarcoADD(0x011,Texto.Controls,5);
		marco.MarcoADD(0x011,Texto.Credits,4);
		marco.MarcoADD(0x011,Texto.ExitGame,9);
		marco.MarcoSET_Option();
	break;

	case 30:
		marco.MarcoINI();
		marco.MarcoADD(0x011,Texto.Continue,0);

		if (Cheat_ON)
		{
		marco.MarcoADD(0x011,Texto.CheatNexLev,60);
		marco.MarcoADD(0x011,Texto.CheatInmune,61,CheatInmune);
		}

		marco.MarcoADD(0x011,Texto.Sound,2,GameSound);
		marco.MarcoADD(0x011,Texto.Vibrate,3,GameVibra);
		marco.MarcoADD(0x011,Texto.Controls,5);
		marco.MarcoADD(0x011,Texto.Restart,6);
		marco.MarcoADD(0x011,Texto.ExitGame,9);

		marco.MarcoSET_Option();
	break;

	case 20:
		Texto.Credits(marco);
	break;

	case 40:
		Texto.Controls(marco);
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
	if (!Cheat_ON && KeybB1!=0 && KeybB1 != KeybB2)
	{
	CheatRUN(KeybB1);

		if (Cheat_ON)
		{
		MarcoEND();
		GameIMP();
		MarcoSET(MarcoStatus);
		}
	}


	switch (MarcoStatus)
	{
	case 00:
		if (KeybB1!=0 && KeybB2==0)
		{
		MarcoEND();
		GameIMP();
		MarcoSET(10);
		return;
		}
	break;

	case 21:
		MarcoEND();
		GameIMP();
		MarcoSET(MarcoStatusOld);
	break;

	case 41:
		MarcoEND();
		GameIMP();
		MarcoSET(MarcoStatusOld);
	break;
	}

	if ( (KeybB1==-6 && KeybB2!=-6) ) {MarcoEND(); return;}

	int MarcoKey=0;
	if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
	if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
	if ( (KeybB1==-7 && KeybB2!=-7) || (KeybB1== 5 && KeybB2!= 5) ) {MarcoKey=5;}

	if (KeybB1!= 0 && KeybB2== 0 && marco.Marco_ON==2) {MarcoEXE(-2); return;}

	MarcoEXE(marco.MarcoRUN(MarcoKey, KeybY1));
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
		GameIMP();
		MarcoStatus++;
	return;

	case 0:	// Continue
		MarcoEND();
	break;

	case 1:	// Start
		GameConti=0;
		MenuStart=1;
		MarcoEND();
	break;

	case 10:	// Continue
		MenuStart=2;
		MarcoEND();
	break;


	case 2:	// Sound ON/OFF
		GameSound=marco.getOption();

		if (MarcoStatus==10)
		{
			if (GameSound==0)
			{
			gc.SoundRES();
			} else {
			gc.SoundSET(0,0);
			}
		}
	break;

	case 3:	// Vibrate ON/OFF
		GameVibra=marco.getOption();
	break;

	case 4:	// Credits
		MarcoEND();
		GameIMP();
		MarcoSET(20);
	break;

	case 5:	// Controls
		MarcoEND();
		GameIMP();
		MarcoSET(40);
	break;

	case 6:	// Restart
		MarcoEND();
		FaseExit=3;
	break;

	case 9:	// Exit Game
		GameExit=1;
	break;




	case 60:	// Next Level
		MarcoEND();
		FaseExit=1;
	break;

	case 61:	// Inmune ON/OFF
		CheatInmune=marco.getOption();
	break;
	}
}


// *************************************************


// -------------------
// Marco RES
// ===================

public void MarcoRES()
{
	marco.MarcoEND();
}

// -------------------
// Marco END
// ===================

public void MarcoEND()
{
	marco.MarcoEND();
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

byte[] PrefsData = new byte[] {1,1,0};

// -------------------
// Prefs INI
// ===================

public void PrefsINI()
{
	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("TheMute_Prefs", true);

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
	GameConti=PrefsData[2]; if (GameConti < 0 || GameConti >= FaseType.length) {GameConti=0;}
}

// -------------------
// Prefs SET
// ===================

public void PrefsSET()
{
	PrefsData[0]=(byte)GameSound;
	PrefsData[1]=(byte)GameVibra;
	PrefsData[2]=(byte)GameConti;

	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("TheMute_Prefs", true);

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
	byte[] Cheat_1={7,7,7,2,3,3,3,9,9,9};
	if (Cheat_1[CheatPos_1++] != keycode) {CheatPos_1=0;}
	if (Cheat_1.length==CheatPos_1) {Cheat_ON=true; CheatPos_1=0;}
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