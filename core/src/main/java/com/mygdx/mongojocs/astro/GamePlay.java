package com.mygdx.mongojocs.astro;

// --------------------------
// Astro 3003 (Color version)
// ==========================

// Programacion:   Juan Antonio Gomez
// Graficos:       Jordi Palomer
// Sonido:         Esteban Moreno
// Organizacion:   Gerard Fernandez

// 2003 (c) Microjocs S.L.  All Rights Reserved.


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.astro.Game;
import com.mygdx.mongojocs.astro.Marco;
import com.mygdx.mongojocs.astro.Screen;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;

import java.io.InputStream;

public class GamePlay extends Canvas implements Runnable, CommandListener
{

Thread thread;

Game game;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GamePlay(Game game)
{
	this.game = game;

	System.gc();

	GameINI();	System.gc();

	GameSET();	System.gc();

	thread=new Thread(this);
	thread.start();

	System.gc();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Comienza Hilo en multitarea al hacer 'thread.start()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void run()
{
	screen.DeviceINI();
	GameRUN();
	screen.DeviceEND();
//	GameEND();
	game.destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Se DETIENE el Hilo al hacer 'thread.stop()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void stop() {}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// con la ventaja que YA tenemos un Objeto Gr�fico Creado 'g'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

long TimeNow, TimeOld;
int TimeDat, TimeCnt;

public void paint (Graphics g)
{       
	if (LCD_Fill!=0)
	{
	LCD_Fill=0;
//	g.setColor(LCD_FillRGB);
//	g.fillRect(0,0,getWidth(),getHeight());
	}


	screen.paint(g);


	if (marco.Marco_ON!=0) {marco.MarcoIMP(g);}

//	TimeNow=System.currentTimeMillis(); TimeDat=(int)(TimeNow-TimeOld); TimeOld=TimeNow;
//	g.setClip( 0,0, 101, 80 ); g.setColor(-1); g.fillRect(0,0,  101,12 ); g.setColor(0);
//	g.drawString(Integer.toString((int)(TimeDat)),    0,0, Graphics.LEFT|Graphics.TOP);

}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int KeybX, KeybY, KeybB;

public void keyPressed(int keycode)
{
	KeybX=0; KeybY=0; KeybB=0;

	/*switch(getGameAction(keycode))
	{
		case 1:KeybY=-1;KeybX=0;break;	// Arriba
		case 6:KeybY=1;KeybX=0;break;	// Abajo
		case 5:KeybX=1;KeybY=0;break;	// Derecha
		case 2:KeybX=-1;KeybY=0;break;	// Izquierda
		case 8:KeybB=5;break;			// Fuego
	}*/

	switch (keycode)
	{

		case KEY_NUM4:	// Izquierda
			KeybB=4; KeybX=-1;KeybY=0;
			break;

		case KEY_NUM6:	// Derecha
			KeybB=6; KeybX=1;KeybY=0;
			break;

		case KEY_NUM8:	// Abajo
			KeybB=8; KeybY=1;KeybX=0;
			break;

		case KEY_NUM2:	// Arriba
			KeybB=2; KeybY=-1;KeybX=0;
			break;

		case KEY_NUM1:
			KeybB=1;
			KeybX=-1;
			KeybY=-1;
			break;

		case KEY_NUM3:
			KeybB=3;
			KeybX= 1;
			KeybY=-1;
			break;

		case -7:	// Motorola V300 - Menu Derecha
		case KEY_NUM5:
			KeybB=5;
			break;

		case KEY_NUM7:
			KeybB=7;
			break;

		case KEY_NUM9:
			KeybB=9;
			break;

		case KEY_NUM0:
		case -6:	// Motorola V300 - Menu Izquierda
		case 42:	// *
		case 35:	// #
			KeybB=10;
			break;
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyRepeated(int keycode)
{
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	KeybX=0; KeybY=0; KeybB=0;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Keyboard - Engine - RUN
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int KeybX1, KeybY1, KeybB1;
int KeybX2, KeybY2, KeybB2;

public void KeyboardRUN()
{
	KeybX2 = KeybX1;	// Keys del Frame Anterior
	KeybY2 = KeybY1;
	KeybB2 = KeybB1;

	KeybX1 = KeybX;		// Keys del Frame Actual
	KeybY1 = KeybY;
	KeybB1 = KeybB;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

void leerdata(String f, byte[] buffer, int size)
{
	/*try
	{
		is.read(buffer, 0, size);
		is.close();
	}catch(Exception exception) {}*/

	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+f.substring(1));
	byte[] bytes = file.readBytes();

	for(int i = 0; i < size; i++)
		buffer[i] = bytes[i];

}


void leerdata(InputStream is, byte[] buffer, int size, int noclose)
{
	try
	{
		is.read(buffer, 0, size);
	}catch(Exception exception) {}
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Copia Imagenes
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void ImageCopy(Image Sour, int SourX, int SourY, Image Dest, int DestX, int DestY, int SizeX, int SizeY)
{
	Graphics DestGfx = Dest.getGraphics();
	DestGfx.setClip(DestX, DestY, SizeX, SizeY);
	DestGfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
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


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<





//                 =================================

//                       EMPIEZA LA ACCION !!!

//                 =================================




// *******************
// -------------------
// Game - Engine
// ===================
// *******************

int GameStatus=0;
int GameSound=1;
int GameVibra=1;
int GameExit=0;

int GameX;
int GameY;
int GameSizeX;
int GameSizeY;
final int GameMaxX=176;	// 176
final int GameMaxY=208;	// 144

int LCD_Fill=0;
int LCD_FillRGB;

Screen screen;

Marco marco;

Image LoadImg;

// -------------------
// Game INI
// ===================

public void GameINI()
{
	PrefsINI();
	GameSound=PrefsData[0];
	GameVibra=PrefsData[1];
	JugarLevelUlt=(PrefsData[2]<11 && PrefsData[2]>0)?PrefsData[2]:1;

	GameSizeX=getWidth();
	GameSizeY=getHeight();

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

	screen = new Screen(this);

	marco = new Marco(GameSizeX, GameSizeY);

	System.gc();	// Recolector de BASURA (FORZAMOS a liberar memoria que ya no se usa)
}

// -------------------
// Game SET
// ===================

public void GameSET()
{
}

// -------------------
// Game RUN
// ===================

public void GameRUN()
{
  while (GameExit == 0)
  {

	KeyboardRUN();
	
	switch (GameStatus)
	{
	case 00:
		WaitINI(3*1000, 50);

		GameLoadFiles();

		screen.SoundINI();

		GameStatus++;
		WaitSET();

		ListenerSET(this);

//		GameStatus=50;
	break;



	case 50:
		JugarINI();
		WaitINI(0, 52);
		GameStatus++;
	break;

	case 51:
		JugarSET();

		LCD_FillRGB=0;
		LCD_Fill=1;

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.gc();
		WaitSET();
	break;

	case 52:
		if (JugarLevelAct!=0)
		{
		JugarRUN();
		JugarIMP();
		PanelSET(0);	// Level x
		WaitSET(2*1000, GameStatus+1);
		} else {
		GameStatus++;
		}
	break;

	case 53:
		PanelRES();
		if (JugarLevelAct == 0) { screen.SoundSET(1,0); }

		GameStatus++;
	break;

	case 54:
//		Cheats();		// ** Para quitar en FINAL version ** //

		JugarRUN();
		JugarIMP();

		if (KeybB1!= 0 && KeybB2== 0 && JugarLevelAct==0 && CoheMode==2) {MarcoSET(0); break;}
		if (KeybB1==10 && KeybB2!=10 && JugarLevelAct!=0) {MarcoSET(30); break;}

		if (FaseExit!=0) {GameStatus++;}
	break;

	case 55:
		JugarEND();

		WaitINI(0, 52);

		if (FaseExit==1)
			{
			if (--JugarVidas < 0) { PanelSET(2); WaitSET(3*1000, 60); break;}
			}
		if (FaseExit==2)
			{
			PanelSET((JugarLevelNow==0)?3:1); if (JugarLevelNow!=0) {WaitINI(4*1000, 52);}

			if (JugarLevelNow!=0) {screen.SoundSET(3,1);} else {screen.SoundRES();}

			if (++JugarLevelAct > 10) {JugarFinal=1; JugarLevelAct=0;}
			}
		GameStatus=51;
	break;


	case 60:
		PanelRES();
		GameStatus=50;
	break;


	case 900:
		MarcoRUN();
	break;


	case 1000:
		WaitRUN();
	break;

	} // switch



	repaint();
	serviceRepaints();


	try	{
	thread.sleep(50);	// Paramos esta Tarea/Hilo/thread x nanosegundos!!!
	} catch(java.lang.InterruptedException e){}

  } // while
}

// -------------------
// Game END
// ===================

public void GameEND()
{
	PrefsData[0]=(byte)GameSound;
	PrefsData[1]=(byte)GameVibra;
	PrefsData[2]=(byte)JugarLevelUlt;
	PrefsSET();
}


// -------------------
// Game LoadFiles
// ===================

byte MapAnim[] = new byte[16*16];	// Patones que forman la Pantalla

public void GameLoadFiles()
{

	try	{
	LoadImg = Image.createImage("/gfx/Loading.png");
	} catch (Exception e) {}

	while (LoadImg!=null)
	{
	repaint();
	serviceRepaints();
	}


	InputStream is;

	//is = getClass().getResourceAsStream("/gfx/Anims.map");
	leerdata("/gfx/Anims.map",MapAnim,16*16);


	screen.loadFiles();


	System.gc();
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

int JugarVidas;
int JugarFinal;
int JugarStart;
int JugarInmune;
int JugarLevelAct;
int JugarLevelNow;
int JugarLevelUlt;

int JugarShow=0;

// -------------------
// Jugar INI
// ===================

public void JugarINI()
{
	JugarLevelAct=0;
	JugarLevelNow=-1;
	JugarFinal=0;
	JugarStart=0;
}

// -------------------
// Jugar SET
// ===================

public void JugarSET()
{
	JugarVidas=2;

	if (JugarLevelAct > JugarLevelUlt) {JugarLevelUlt = JugarLevelAct;}

	AnimTileINI();
	AnimSpriteINI();

	FaseINI();
	FaseSET();

	screen.setTileMap();

	screen.setSprites();

	screen.setGOM();
}


// -------------------
// Jugar RUN
// ===================

public void JugarRUN()
{
	FaseRUN();

	AnimTileRUN();

	AnimSpriteRUN();

	FaseRUN();

	AnimTileRUN();

	AnimSpriteRUN();

	if (JugarInmune > 0) {JugarInmune--;}
}

// -------------------
// Jugar IMP
// ===================

int JugarUpdate=0;

public void JugarIMP()
{

	JugarUpdate=1;

//	screen.setScroll();
//	screen.runSprites();
//	screen.runGOM();
}

// -------------------
// Jugar END
// ===================

public void JugarEND()
{
	FaseEND();

	AnimTileEND();
	AnimSpriteEND();
}

// <=- <=- <=- <=- <=-



// *************
// -------------
// Fase - Engine
// =============
// *************

int FaseX,FaseY;	// Coordenada X,Y para el scroll del fondo

final int FaseTilMaxX = 64;		// Size X MAXIMO de la Fase
final int FaseTilMaxY = 32;		// Size Y MAXIMO de la Fase

byte ChaPat[] = new byte[FaseTilMaxX*FaseTilMaxY];	// Patones que forman la Pantalla
byte FasePat[];

int FaseTilSizeX = 64;
int FaseTilSizeY = 12;

int FaseTilSizeTab[] = new int[] {
	32,24,	// Level o - Intro
	24,24,	// Level 1
	24,32,	// Level 2
	64,12,	// Level 3
	24,32,	// Level 4
	13,64,	// Level 5
	40,24,	// Level 6
	24,40,	// Level 7
	40,16,	// Level 8
	40,24,	// Level 9
	12,64,	// Level 10
};

String FaseTilFileTab[] = new String[] {
	"0",	// Level 0 - Intro
	"1",	// Level 1
	"2",	// Level 2
	"3",	// Level 3
	"4",	// Level 4
	"5",	// Level 5
	"6",	// Level 6
	"7",	// Level 7
	"8",	// Level 8
	"9",	// Level 9
	"10",	// Level 10
};

int FaseExit;
int FaseMineral;
int FasePuertaDir;

int LavaCnt;
final int[] LavaWait = new int[] {20,30,15,38,22,17,40,36,15,45,14,37,28,19,42,24};

// ----------
// Fase INI
// ==========

public void FaseINI()
{
	FaseExit=0;
	FaseMineral=0;
	FasePuertaDir=0;

	AutoMovINI();		// Movimiento Automatico Protagonista
	CoheteINI();

	Enemy1INI();
	Enemy2INI();
	HuevoINI();
	BolaINI();
	AscensorINI();

	ScrollINI();

	if (JugarLevelNow!=JugarLevelAct)
	{
	JugarLevelNow=JugarLevelAct;

	FaseTilSizeX=FaseTilSizeTab[(JugarLevelAct*2)+0];		// Fase Size X
	FaseTilSizeY=FaseTilSizeTab[(JugarLevelAct*2)+1];		// Fase Size Y

// Leemos FASE del archivo
// -----------------------
	FasePat = new byte[FaseTilSizeX * FaseTilSizeY];

	//InputStream is;
	//is = getClass().getResourceAsStream("/gfx/stage"+FaseTilFileTab[JugarLevelAct]+".map");
	leerdata("/gfx/stage"+FaseTilFileTab[JugarLevelAct]+".map", FasePat, FaseTilSizeX * FaseTilSizeY);

	for(int i = 0; i < FasePat.length; i++)
		if(FasePat[i] < 0 ) FasePat[i] += 256;

	if (JugarLevelAct==0)
	{
		for (int i=0; i<6*32 ; i++)
		{
		FasePat[i+18*32]=FasePat[i+12*32];
		}

		for (int i=0; i<9*32 ; i++)
		{
		FasePat[i+9*32]=FasePat[i+0*32];
		}
	}


	int GameTilX=(GameSizeX/8)+1;
	int GameTilY=(GameSizeY/8)+1;

	if ( GameTilX > FaseTilSizeX  ||  GameTilY > FaseTilSizeY )
	{

	int Dir=0, SizeX, SizeY;
	if (GameTilX > FaseTilSizeX)
		{SizeX=GameTilX; Dir+=(GameTilX-FaseTilSizeX)/2;} else
		{SizeX=FaseTilSizeX;}
	if (GameTilY > FaseTilSizeY)
		{SizeY=GameTilY; Dir+=((GameTilY-FaseTilSizeY)/2)*SizeX;} else
		{SizeY=FaseTilSizeY;}

	byte[] buffer = new byte[ SizeX * SizeY ];

	for (int i=0 ; i<buffer.length ; i++) {buffer[i]=1;}

	for (int y=0 ; y<FaseTilSizeY ; y++)
	{
		for (int x=0 ; x<FaseTilSizeX ; x++)
		{
		buffer[Dir+(y*SizeX)+x] = FasePat[(y*FaseTilSizeX)+x];
		}
	}
	FasePat=buffer;
	FaseTilSizeX=SizeX;
	FaseTilSizeY=SizeY;

	}


	}
}

// ----------
// Fase SET
// ==========

public void FaseSET()
{
	ProtSET();		// Ponemos al Protagonista

	if (JugarLevelAct == 0) { CoheteSET(); JugarShow=0; } else {JugarShow=1;}

// Inicializamos FASE
// ------------------
	for (int i=0 ; i < FaseTilMaxX * FaseTilMaxY ; i++) {ChaPat[i] = (i<FasePat.length)?FasePat[i]:2;}

// Analizamos FASE
// ---------------
for (int i=0 ; i < FaseTilSizeX * FaseTilSizeY ; i++)
{
	switch (ChaPat[i])
	{
	case (byte)0xFF:	// Player
		ProtSprY = ((i / FaseTilSizeX)*8)-8;
		ProtSprX = (i % FaseTilSizeX)*8;
		if (ChaPat[i+1] == 0x43) {ChaPat[i] = 0x42;} else {ChaPat[i] = 0;}
		break;

	case (byte)0x50:	// Puerta (Salida / Unica direccion)
		if (ChaPat[i + FaseTilSizeX] == 0x26)
		{
			if (ChaPat[i + FaseTilSizeX - 1] == 0x28)	// Puerta Apertura Izquierda
			{
			}
			else if (ChaPat[i + FaseTilSizeX + 2] == 0x28)	// Puerta Apertura Derecha
			{
			}
			else {FasePuertaDir = i + FaseTilSizeX;}	// Puerta de Salida
		}
		break;

	case (byte)0x2C:	// Mineral
		FaseMineral++;
		break;	

	case (byte)0x74:	// Premsa
		AnimTileSET(i ,0 ,7 ,2 ,3 ,8 ,1 ,1, 40);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)
		break;	
	
	case (byte)0x37:	// Huevo
		HuevoSET(i, ((i%FaseTilSizeX)*8)+3, ((i/FaseTilSizeX)*8)-2 );
		break;	

	case (byte)0x7D:	// Lava
		LavaCnt&=0x0F;
		AnimTileSET(i ,0 ,13  ,2 ,2  ,5  ,2 ,1 ,LavaWait[LavaCnt]*6, LavaWait[LavaCnt++]*6);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)
		break;	

	case (byte)0xFE:	// Enemy #1
		Enemy1SET( (i%FaseTilSizeX)*8, ((i/FaseTilSizeX)*8) );
		ChaPat[i] = 0;
		break;	

	case (byte)0xFD:	// Enemy #2
		Enemy2SET( (i%FaseTilSizeX)*8, ((i/FaseTilSizeX)*8) );
		ChaPat[i] = 0;
		break;	

	case (byte)0xFC:	// Ascensor
		AscensorSET( (i%FaseTilSizeX)*8, ((i/FaseTilSizeX)*8) );
		ChaPat[i] = 0;
		break;	
	}
}

}

// ----------
// Fase RUN
// ==========

public void FaseRUN()
{
	if (Prot_ON != 0) {ProtRUN();}

	if (Dinamita_ON != 0) {DinamitaRUN();}

	if (Disco_ON != 0) {DiscoRUN();}

	if (Cohe_ON!=0) {CoheteRUN();}

	Enemy1RUN();
	Enemy2RUN();
	HuevoRUN();
	BolaRUN();
	AscensorRUN();

	ScrollRUN();
}

// ----------
// Fase END
// ==========

public void FaseEND()
{
	if (Disco_ON != 0) {DiscoEND();}

	Enemy1END();
	Enemy2END();
	HuevoEND();
	BolaEND();
	AscensorEND();
}

// ----------
// Fase Next
// ==========

public void FaseNext()
{
	FaseExit=2;
}

// <=- <=- <=- <=- <=-


// *******************
// -------------------
// Scroll - Engine
// ===================
// *******************

int LCDSizeX;	// 96
int LCDSizeY;	// 65
int LCDMidX;
int LCDMidY;

// -------------------
// Scroll INI
// ===================

public void ScrollINI()
{
	LCDSizeX=GameSizeX;		// 96
	LCDSizeY=GameSizeY;		// 65
	LCDMidX=(LCDSizeX/2)-8;
	LCDMidY=(LCDSizeY/2)-12;
}

// -------------------
// Scroll RUN
// ===================

public void ScrollRUN()
{

// Ajustamos Scroll
// ----------------
	FaseX=ProtSprX-LCDMidX;
	FaseY=ProtSprY-LCDMidY;

	if (FaseX > (FaseTilSizeX*8)-LCDSizeX) {FaseX=(FaseTilSizeX*8)-LCDSizeX;}
	if (FaseX < 0) {FaseX=0;}

	if (FaseY > (FaseTilSizeY*8)-LCDSizeY) {FaseY=(FaseTilSizeY*8)-LCDSizeY;}
	if (FaseY < 0) {FaseY=0;} 
}

// <=- <=- <=- <=- <=-

































// ----------------------------------------------------------------------------

// *********************
// ---------------------
// Prot - Engine
// =====================
// *********************

int Prot_ON;
int ProtSprFrame;		// Numero de Frame del Sprite del Protagonista a mostrar
int ProtSprX,ProtSprY;	// Coordenada del Sprite
int ProtSumaY;

int ProtFraSide;
int ProtFraMode;
int ProtFraType;
int ProtFraTime;
int ProtFraCnt;
int ProtFraCaer;
int ProtFraAscensor;
int ProtVolarCnt;
final int ProtVolarSuma[] = {-1,0,1,0};
final int ProtSprVolar[] = {0x0A,0x0C};
final int ProtSprCaminar[] = {0,2,4,6};
final int ProtSprEscalera[] = {122,123,124,125,126,127};
final int ProtSprMuerte[] = {128,130,132, 134,136, 134,136, 134,136, 134,136, 138};
final int ProtSprAccion[] = {0x0A,0x0C};
final int ProtFraAccion[] = {12,8,8,12*2,8};
final int ProtFraSalto[] = {6,4,4,2,2,0,0,-2,-2,-4,-4,-6};


// ------------------
// Prot SET
// ==================

public void ProtSET()
{
	ProtSumaY=0;
	ProtFraSide=0;
	ProtFraMode=0;
	ProtFraType=0;
	ProtFraTime=0;
	ProtFraCnt=0;
	ProtSprFrame=0;				// Numero de Frame del Sprite del Protagonista a mostrar
	ProtSprX=40; ProtSprY=8;	// Coordenada del Sprite
	ProtVolarCnt=0;

	ProtFraAscensor=0;

	Prot_ON=(JugarLevelAct!=0)?1:0;
}

// ------------------
// Prot RUN
// ==================

public void ProtRUN()
{
	ProtMover();

	ProtSprFrame = 0;
	ProtFraCnt&=0x3FF;

	switch (ProtFraMode)
	{
	case 0:		// Caminar
		ProtSprFrame=(ProtFraType*16)+ProtFraSide+ProtSprCaminar[(ProtFraCnt>>1)&3];
		if (ProtFraCaer==1)
		{
//		ProtSprFrame=(ProtFraType*16)+ProtFraSide+14;
		ProtSprFrame=(ProtFraType*16)+ProtFraSide+8;
		}
		break;

	case 1:		// Salto
		ProtSprFrame=(ProtFraType*16)+ProtFraSide+8;
		break;

	case 2:		// Escalera
		while (ProtFraCnt>=6) {ProtFraCnt-=6;}
		ProtSprFrame=ProtSprEscalera[(ProtFraCnt)];
		break;

	case 3:		// Volar
		ProtSprFrame=ProtFraSide+ProtSprVolar[(ProtFraCnt>>1)&1];
		break;

	case 4:		// Accion
		ProtSprFrame=(ProtFraType*16)+ProtFraSide+ProtSprAccion[(ProtFraCnt>>2)&1];
		break;

	case 5:		// Suicidio
		ProtSprFrame=80+(ProtFraCnt&-2)+ProtFraSide;
		break;

	case 6:		// Muerte
		ProtSprFrame=ProtSprMuerte[(ProtFraCnt>>1)]+ProtFraSide;
		break;
	}
}

// ------------------
// Prot Mover
// ==================

public void ProtMover()
{
	if ((ProtSprX == FaseTilSizeX*8) | (ProtSprX <= -16) | (ProtSprY <= -20)) {FaseNext(); return; }	// Next Level????

	ProtCheck();

	if (AutoMov_ON!=0) {AutoMovRUN();}

	if (ProtFraMode == 5) { Suicidio(); return; }

	if (ProtFraMode == 4) { Accion(); return; }

	if (ProtFraMode == 3) { Volar(); return; }

	if (ProtFraMode == 2) { Escalera(); return; }

	if (ProtFraMode == 1) { if (Salto()) {return;} }

	ProtFraCaer=0;
	if (ProtFraAscensor == 0)
	{
		if (CheckTile(ProtSprX+2,ProtSprY+24,11,0,0x07) == 0)	// Hay Suelo ???
			{
			ProtSprY+=2; ProtFraCaer=1;
			if ((ProtFraType==4) && (KeybB1 == 5) && (KeybB2 != 5)) { AccionSet(); }
			return;
			}
	}
	ProtFraAscensor=0;


	if (ProtFraMode == 6) { Muerte(); return;}

	if ((KeybB1 == 5) && (KeybB2 != 5)) { AccionSet(); }

	if (ProtFraMode == 0)
		{
		PuenteRUN();
		PuertaRUN();
		Caminar();
		}
}
// ==================================


// -----------------------
// PROTAGONISTA Accion SET
// =======================

public void AccionSet() {

	switch (ProtFraType)
	{
	case 1:		// Disco
		ProtFraMode=4;ProtFraTime=ProtFraAccion[ProtFraType];ProtFraCnt=0;
		break;

	case 2:		// Dinamita
		ProtFraMode=4;ProtFraTime=ProtFraAccion[ProtFraType];ProtFraCnt=0;
		break;

	case 3:		// Taladro

	screen.VibraSET(200);

	if ( (CheckTile(ProtSprX+18-(ProtFraSide*22),ProtSprY+8) & 0x002) == 2)
		{AnimTileSET(ChaPatDir, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	if ( (CheckTile(ProtSprX+18-(ProtFraSide*22),ProtSprY+16) & 0x002) == 2)
		{AnimTileSET(ChaPatDir, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	if ( (CheckTile(ProtSprX+26-(ProtFraSide*38),ProtSprY+8) & 0x002) == 2)
		{AnimTileSET(ChaPatDir, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	if ( (CheckTile(ProtSprX+26-(ProtFraSide*38),ProtSprY+16) & 0x002) == 2)
		{AnimTileSET(ChaPatDir, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

		ProtFraMode=4;ProtFraTime=ProtFraAccion[ProtFraType];ProtFraCnt=0;
		break;

	case 4:		// Volar
		ProtFraMode=4;ProtFraTime=ProtFraAccion[ProtFraType];ProtFraCnt=0;
		break;
	}
}
// ==================================


// -----------------------
// PROTAGONISTA Accion RUN
// =======================

public void Accion() {

	switch (ProtFraType)
	{
	case 1:		// Disco
		ProtFraCnt+=1;
		break;

	case 2:		// Dinamita
		ProtFraCnt+=1;
		break;

	case 3:		// Taladro
		ProtFraCnt+=2;
		break;

	case 4:		// Volar
		ProtFraCnt+=1;
		break;

	default:
		return;
	}

	ProtFraTime--; if (ProtFraTime > 0) {return;}

// -----------------------
// PROTAGONISTA Accion END
// =======================

	switch (ProtFraType)
	{

	case 1:		// Disco
		DiscoSET();
		break;

	case 2:		// Dinamita
		DinamitaSET();
		break;	

	case 4:		// Volar
		ProtFraMode=3; ProtFraCnt=0; ProtVolarCnt=40*2; return;
	
	}

	ProtFraMode=0; ProtFraType=0;
}
// ==================================


// --------------------
// PROTAGONISTA Caminar
// ====================

public void Caminar() {

	if (KeybX1 == -1) {
		ProtFraSide=1;
		if (CheckTile(ProtSprX-1,ProtSprY+8,0,15,0x01) == 0) ProtSprX-=2;ProtFraCnt++;
	} else if (KeybX1 == 1) {
		ProtFraSide=0;
		if (CheckTile(ProtSprX+16,ProtSprY+8,0,15,0x01) == 0) ProtSprX+=2;ProtFraCnt++;
}

// Controlamos si hay ESCALERA tras pulsar ARRIBA / ABAJO ( Sino SALTA )
// ---------------------------------------------------------------------

	if (KeybY1 == 1) {
		if	((CheckTile(ProtSprX+02,ProtSprY+24) & 0x04) != 0 &&
			 (CheckTile(ProtSprX+13,ProtSprY+24) & 0x04) != 0)
				{ProtFraMode=2; ProtFraCnt=0;}
	} else if (KeybY1 == -1) {
		if	((CheckTile(ProtSprX+02,ProtSprY+22) & 0x04) != 0 &&
			 (CheckTile(ProtSprX+13,ProtSprY+22) & 0x04) != 0)
				{ProtFraMode=2; ProtFraCnt=0;}
		else
			{ProtFraMode=1; ProtFraTime=12; ProtFraCnt=0;}
	}
}
// ==================================


// ------------------
// PROTAGONISTA Volar
// ==================

public void Volar()
{

	ProtSumaY=ProtVolarSuma[ProtFraCnt>>1 & 3];

	if (KeybX1 == -1) {
		ProtFraSide=1;
		if (CheckTile(ProtSprX-1,ProtSprY+10,0,10,0x01) == 0) ProtSprX-=2;
	} else if (KeybX1 == 1) {
		ProtFraSide=0;
		if (CheckTile(ProtSprX+16,ProtSprY+10,0,10,0x01) == 0) ProtSprX+=2;
	}
	if (KeybY1 == -1)	{
			if (CheckTile(ProtSprX+2,ProtSprY+4,11,0,0x01) == 0) {ProtSprY-=2;}
	} else if (KeybY1 == 1)	{
			if (CheckTile(ProtSprX+2,ProtSprY+24,11,0,0x01) == 0) {ProtSprY+=2;}
			else {ProtFraMode=0; ProtFraType=0; ProtSumaY=0;ProtSprY&=0xFFFFF8;ProtVolarCnt=0;return;}
	}

	ProtFraCnt++;

	if (--ProtVolarCnt == 0)
	{ProtFraMode=0; ProtFraType=0; ProtSumaY=0;ProtSprY&=0xFFFFFC;}

}
// ==================================


// ---------------------
// PROTAGONISTA Escalera
// =====================

public void Escalera() {

	if (KeybY1==-1) {
		if (CheckTile(ProtSprX,ProtSprY+22,15,0,0x04) != 0)
			{ProtSprY-=2;ProtFraCnt++;}
		else
			{ProtFraMode=0;KeybY=0;}
	} else if (KeybY1 == 1) {
		if (CheckTile(ProtSprX,ProtSprY+24,15,0,0x04) != 0)
			{ProtSprY+=2;ProtFraCnt++;}
		else
			{ProtFraMode=0;}
	}


	if (KeybX1 == -1 || KeybX1 == 1) {
		ProtFraMode=0;
	}

}
// ==================================


// ------------------------
// Prot - Suicidio - Engine
// ========================

int SuiciMode;
int SuiciCnt;

public void SuicidioSET()
{
	screen.VibraSET(200);
	ProtFraMode=5;ProtFraCnt=0;SuiciMode=0;	// Modo Suicidio
}

public void Suicidio() {

	switch (SuiciMode)
	{
	case 0:
		if (++ProtFraCnt < 21*2) {return;}
		else {ProtFraCnt--;SuiciMode++;SuiciCnt=32;}
		break;
	case 1:
		if (--SuiciCnt > 0) {return;}
		else {FaseExit=3;}
		break;
	}

}

// ==================================


// ----------------------
// Prot - Muerte - Engine
// ======================

int MuerteMode;
int MuerteCnt;

public void MuerteSET()
{
	ProtFraMode=6;ProtFraCnt=0;MuerteMode=0;
	screen.SoundSET(0,1);
}

public void Muerte()
{

	switch (MuerteMode)
	{
	case 0:
		if (++ProtFraCnt < 12*2) {return;}
		else {ProtFraCnt--;MuerteMode++;MuerteCnt=32;}
		break;
	case 1:
		if (--MuerteCnt > 0) {return;}
		else {FaseExit=3;}
		break;
	}

}

// ==================================


// ------------------
// PROTAGONISTA Salto
// ==================

public boolean Salto() {

	int Dato=ProtFraSalto[ProtFraTime-1];

	if (Dato < 0)
	{
		if (CheckTile(ProtSprX+2,ProtSprY+14+Dato,11,0,0x01) == 0) {ProtSprY+=Dato;}
		else {ProtFraTime=6;}
	}
	else if (Dato > 0)
	{
		if (CheckTile(ProtSprX,ProtSprY+23+Dato,15,0,0x01) == 0) {ProtSprY+=Dato;}
		else {ProtFraTime=1;}
	}

	if (CheckTile(ProtSprX+16-(ProtFraSide*17),ProtSprY+14,0,9,0x01) == 0) {ProtSprX+=2-(ProtFraSide*4);}

	ProtFraCnt+=1;

	if (--ProtFraTime < 1) {ProtFraMode=0;return(false);}

	return (true);
}
// ==================================


// ----------------------------------
// PROTAGONISTA Check Sprites
// ==================================

public boolean ProtCheck(int X, int Y, int SizeX, int SizeY)
{
	if ((ProtSprX+ 2 > X+SizeX) ||
		(ProtSprX+13 < X) ||
		(ProtSprY+10 > Y+SizeY) ||
		(ProtSprY+21 < Y))
	{return (false);}
	return(true);
}

public boolean ProtCheck2(int X, int Y, int SizeX, int SizeY)
{
	if (JugarInmune!=0 || !ProtCheck(X,Y,SizeX,SizeY) ) {return false;}

	if (--JugarVidas < 0) {JugarVidas=0; return true;}

	JugarInmune=25;

	return false;
}

// ----------------------------------
// PROTAGONISTA Check Fondo
// ==================================

public void ProtCheck()
{
	CheckFondo(ProtSprX+ 4,ProtSprY+10);
	CheckFondo(ProtSprX+11,ProtSprY+10);
	CheckFondo(ProtSprX+ 4,ProtSprY+21);
	CheckFondo(ProtSprX+11,ProtSprY+21);

// Chequeda si tocamos la explosion de la dinamita
// ------------------------------------
	if ((Dinamita_ON < (16*2)-(6*2)) && (Dinamita_ON > 4))
	{
		if (ProtCheck(DinamitaX+4, DinamitaY+4,  8,8))
		{if (ProtFraMode!=6) {MuerteSET();}}
	}
}

// ==================================

int TileBitsID;

public void CheckFondo(int X, int Y)
{
	TileBitsID=CheckTile(X,Y);

	if (ChaPat[ChaPatDir]==1 && ProtFraMode!=6) {FaseNext(); return;}

	if ((TileBitsID & 0x10F8) != 0)
		{if ((ProtFraMode == 0) || (ProtFraMode == 1) || (ProtFraMode == 3)) {ObjetoSET();}}

	if (((TileBitsID & 0x0800) != 0) && (ProtFraMode!=6)) {MuerteSET();}
}

// ------------------------
// PROTAGONISTA Objeto SET
// ========================

public void ObjetoSET() {

	if ((ProtFraType != 0) && ((TileBitsID & 0x1008) == 0)) {return;}

	if ((TileBitsID & 0x300) == 0x100) {ChaPatDir--;}
	if ((TileBitsID & 0x300) == 0x200) {ChaPatDir-=FaseTilSizeX;}
	if ((TileBitsID & 0x300) == 0x300) {ChaPatDir-=(FaseTilSizeX+1);}

	ChaPat[ChaPatDir] = 0;
	ChaPat[ChaPatDir+1] = 0;
	ChaPat[ChaPatDir+FaseTilSizeX] = 0;
	ChaPat[ChaPatDir+FaseTilSizeX+1] = 0;

	screen.scroll.ScrollUpdate((ChaPatDir%FaseTilSizeX),(ChaPatDir/FaseTilSizeX));
	screen.scroll.ScrollUpdate((ChaPatDir%FaseTilSizeX)+1,(ChaPatDir/FaseTilSizeX)+1);

	if ((TileBitsID & 0x10F8) == 0x008) // Mineral
		{if ((--FaseMineral == 0) && (FasePuertaDir!=0)) {AnimTileSET( FasePuertaDir, 0, 2, 2, 4, 4, 4, 0);}}	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)

	if ((TileBitsID & 0x10F8) == 0x1000) {if (JugarVidas<9) {JugarVidas++;}} // Vida Extra

	if ((TileBitsID & 0x10F8) == 0x010) {ProtFraType=1;} // Disco
	if ((TileBitsID & 0x10F8) == 0x040) {ProtFraType=2;} // Explosivo
	if ((TileBitsID & 0x10F8) == 0x020) {ProtFraType=3;} // Taladro
	if ((TileBitsID & 0x10F8) == 0x080) {ProtFraType=4;ProtVolarCnt=40*2;} // Elevador

	screen.SoundSET(2,1);
}


// ----------------------------------------------------------------------------


// *****************
// -----------------
// Dinamita - Engine
// =================
// *****************

int DinamitaDir;
int Dinamita_ON;
int DinamitaX;
int DinamitaY;

// --------------
// Dinamita - SET
// ==============

public void DinamitaSET()
	{
	DinamitaX=ProtSprX+5-(ProtFraSide*11);
	DinamitaY=ProtSprY;
	AnimSpriteSET(DinamitaX, DinamitaY+8, 0, 6, 2, 0);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)

	DinamitaDir = (((ProtSprY+28)/8)*FaseTilSizeX)+((8+ProtSprX+5-(ProtFraSide*11))/8);

	Dinamita_ON=16*2;
	}


// --------------
// Dinamita - RUN
// ==============

public void DinamitaRUN()
{

	if (Dinamita_ON == (16*2)-(6*2) )
	{
	AnimSpriteSET(DinamitaX, DinamitaY+16, 6, 10, 2, 0);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
//	screen.SoundSET(4,1);
	screen.VibraSET(200);
	}


	if (--Dinamita_ON == (16*2)-(9*2) )
	{

	if ( ((TilDatTab[ChaPat[DinamitaDir-1]+128]) & 0x002) == 2)
		{AnimTileSET(DinamitaDir-1, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	if ( ((TilDatTab[ChaPat[DinamitaDir+0]+128]) & 0x002) == 2)
		{AnimTileSET(DinamitaDir+0, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	if ( ((TilDatTab[ChaPat[DinamitaDir+1]+128]) & 0x002) == 2)
		{AnimTileSET(DinamitaDir+1, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	if ( ((TilDatTab[ChaPat[DinamitaDir-FaseTilSizeX-1]+128]) & 0x002) == 2)
		{AnimTileSET(DinamitaDir-FaseTilSizeX-1, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	if ( ((TilDatTab[ChaPat[DinamitaDir-FaseTilSizeX+1]+128]) & 0x002) == 2)
		{AnimTileSET(DinamitaDir-FaseTilSizeX+1, 0, 0, 1, 1, 6, 3, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}

	}
}



// ----------------------------------------------------------------------------


// *****************
// -----------------
// Disco - Engine
// =================
// *****************

int DiscoSide;
int Disco_ON;
int DiscoSpr=-1;

// --------------
// Disco - SET
// ==============

public void DiscoSET()
	{
	if (DiscoSpr != -1)	{DiscoEND();}

	DiscoSpr = AnimSpriteSET(ProtSprX+5-(ProtFraSide*11), ProtSprY+14, 16, 4, 1, 1);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
	if (DiscoSpr==-1) {return;}

	DiscoSide=(ProtFraSide==0)?4:-4;
	
	Disco_ON=200;
	}

// --------------
// Disco - RUN
// ==============

public void DiscoRUN()
	{
	if (--Disco_ON == 0) { DiscoEND(); return; }

	if (CheckTile(AnimSprites[DiscoSpr].CoorX+3, AnimSprites[DiscoSpr].CoorY-2, 10, 0, 0x003) != 0) { DiscoEND(); return; }

	AnimSprites[DiscoSpr].CoorX+=DiscoSide;
	}

// --------------
// Disco - END
// ==============

public void DiscoEND()
{
	AnimSpriteRES(DiscoSpr);
	DiscoSpr=-1;
	Disco_ON=0;
}

// ----------------------------------------------------------------------------


// ----------------------------------------------------------------------------


// ********************
// --------------------
// Check Tiles - Engine
// ====================
// ********************

int MapAnimSizeX = 16;

int ChaPatDir;

public int CheckTile(int X, int Y)
{
	if (Y < 0) {Y=0;}
	if (X < 0) {X=0;}
	else if (X >= (FaseTilSizeX*8)-16 && ProtSprX >= (FaseTilSizeX*8)-16) {X=(FaseTilSizeX*8)-8;}

	ChaPatDir = ((Y/8)*FaseTilSizeX)+(X/8);
	return (int)TilDatTab[ChaPat[ChaPatDir]+128];
}


public int CheckTile(int X, int Y, int SizeX, int SizeY, int Mask)
{
	if (Y < 0) {Y=0;}
	if (X < 0) {X=0;}
	else if (X >= (FaseTilSizeX*8)-16 && ProtSprX >= (FaseTilSizeX*8)-16) {X=(FaseTilSizeX*8)-8;}

int TabXY = ((Y/8)*FaseTilSizeX)+(X/8);
int TabSizeX = ((X+SizeX)/8)-(X/8);
int TabSizeY = ((Y+SizeY)/8)-(Y/8);

int Dato=0;

	for (; TabSizeY>-1 ; TabSizeY--)
	{
		for (int i=TabSizeX ; i>-1 ; i--)
		{
			Dato |= TilDatTab[ChaPat[TabXY+i]+128];
		}
	TabXY+=FaseTilSizeX;
	}

	return (int)(Dato & Mask);
}


// ----------------------------------------------------------------------------


final short TilDatTab[] = new short[] {
// 0     1      2      3     4     5     6     7     8     9     A     B     C     D     E     F
0x000,0x001,0x0001,0x0001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x800,0x800, // 8
0x800,0x800,0x0000,0x0000,0x000,0x000,0x001,0x001,0x001,0x000,0x000,0x800,0x800,0x800,0x800,0x001, // 9
0x000,0x000,0x1000,0x1100,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // A
0x000,0x000,0x0000,0x0000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // B
0x800,0x800,0x1200,0x1300,0x000,0x000,0x000,0x004,0x004,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // C
0x000,0x000,0x0000,0x0000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // D
0x000,0x000,0x0000,0x0000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // E
0x000,0x000,0x0000,0x0000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // F

0x000,0x000,0x0001,0x0001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000, // 0
0x000,0x000,0x0000,0x0000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001,0x001,0x001,0x001,0x003, // 1
0x000,0x000,0x0004,0x0004,0x000,0x000,0x001,0x001,0x000,0x000,0x020,0x120,0x008,0x108,0x080,0x180, // 2
0x001,0x001,0x0040,0x0140,0x010,0x110,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001, // 3
0x000,0x000,0x0004,0x0004,0x004,0x004,0x000,0x000,0x000,0x000,0x220,0x320,0x208,0x308,0x280,0x380, // 4
0x001,0x001,0x0240,0x0340,0x210,0x310,0x001,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x000,0x001, // 5
0x002,0x002,0x0001,0x0001,0x001,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x800,0x800, // 6
0x800,0x800,0x0800,0x0800,0x000,0x000,0x001,0x001,0x001,0x001,0x001,0x000,0x000,0x000,0x000,0x001, // 7
};


// ----------------------------------------------------------------------------


// *******************
// -------------------
// Puerta - Engine
// ===================
// *******************

// -------------------
// Puerta RUN
// ===================

public void PuertaRUN()
{
	CheckTile(ProtSprX+12-(8*ProtFraSide),ProtSprY+20);
	if ( ChaPat[ChaPatDir] == (byte)0xB2)	// Sobre Interruptor Puerta?
	{
	ChaPat[ChaPatDir] = 0;
		if (ChaPat[ChaPatDir+1] == 0x26)
		{
		AnimTileSET( ChaPatDir-(3*FaseTilSizeX)+1, 0, 2, 2, 4, 4, 4, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}
		else if (ChaPat[ChaPatDir-1] == 0x27)
		{
		AnimTileSET( ChaPatDir-(3*FaseTilSizeX)-2, 0, 2, 2, 4, 4, 4, 0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed)
		}
	}
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Puente - Engine
// ===================
// *******************

// -------------------
// Puente RUN
// ===================

public void PuenteRUN()
{
	CheckTile(ProtSprX+2,ProtSprY+24);
	if ( ChaPat[ChaPatDir] == 0x3F)	// Sobre Puente?
	{
		ChaPat[ChaPatDir]=(byte)0xA4;
		AnimTileSET(ChaPatDir ,1 ,1 ,1 ,1 ,5 ,2 ,0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)
	}

	CheckTile(ProtSprX+8,ProtSprY+24);
	if ( ChaPat[ChaPatDir] == 0x3F)	// Sobre Puente?
	{
		ChaPat[ChaPatDir]=(byte)0xA4;
		AnimTileSET(ChaPatDir ,1 ,1 ,1 ,1 ,5 ,2 ,0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)
	}

	CheckTile(ProtSprX+13,ProtSprY+24);
	if ( ChaPat[ChaPatDir] == 0x3F)	// Sobre Puente?
	{
		ChaPat[ChaPatDir]=(byte)0xA4;
		AnimTileSET(ChaPatDir ,1 ,1 ,1 ,1 ,5 ,2 ,0);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)
	}
}

// <=- <=- <=- <=- <=-



// -----------------------------------------------
// Sub-Clase para gestion de Animaciones con Tiles
// ===============================================

// *******************
// -------------------
// Anim Tile - Engine
// ===================
// *******************

public class AnimTile
{
int Sour;
int Dest;
int SizeX;
int SizeY;
int Frames, Frames2;
int FrameAct=0;
int Speed;
int SpeedCnt=0;
int Mode;
int Side=1;
int Wait=0;

public AnimTile() {}

// ---------------
// Anim Tiles Play
// ===============

public boolean Play()
{
	if (SpeedCnt > 0) {SpeedCnt--; return(false);}

	SpeedCnt=Speed;

	int Source=Sour+(SizeX*FrameAct);
	int Destin=Dest;

		for (int t=0 ; t<SizeY ; t++ )
		{
			for (int i=0 ; i<SizeX ; i++ )
			{
				ChaPat[Destin+i] = MapAnim[Source+i];
			}
		Source+=MapAnimSizeX;
		Destin+=FaseTilSizeX;
		}

	screen.scroll.ScrollUpdate(Dest%FaseTilSizeX, Dest/FaseTilSizeX, SizeX, SizeY);

	switch (Mode)
	{
	case 0:		// Modo Play 1 sola vez
		FrameAct++;
		if (--Frames == 0) {return(true);}
		break;
	
	case 1:		// Modo Play Loop
		FrameAct++;
		if (--Frames == 0)
		{
		Frames=Frames2; FrameAct=0; SpeedCnt+=Wait;
		}
		break;

	case 2:		// Modo Ping-Pong con Play 1 sola vez
		FrameAct+=Side;
		if (--Frames == 0)
		{
			if (Side ==  1) {Frames=Frames2-1; FrameAct-=2; Side=-1;}
			else {return(true);}
		}
		break;

	case 3:		// Modo Ping-Pong con Play Loop
		FrameAct+=Side;
		if (--Frames == 0)
		{
			if (Side == 1) {Frames=Frames2-1; FrameAct-=2; Side=-1;}
			else {Frames=Frames2-1; FrameAct=1; Side=1;}
		}
		break;
	}
	
	return (false);
}

};


// --------------
// Anim Tiles SET
// ==============

public int AnimTileSET(int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode)
{
	if (AnimTileC == AnimTileMAX) {return(-1);}

	int i=AnimTileP[AnimTileC++];

	AnimTiles[i] = new AnimTile();

	AnimTiles[i].Dest=Dest;
	AnimTiles[i].Sour=(CoorY*MapAnimSizeX)+CoorX;
	AnimTiles[i].SizeX=SizeX;
	AnimTiles[i].SizeY=SizeY;
	AnimTiles[i].Frames=Frames;
	AnimTiles[i].Frames2=Frames;
	AnimTiles[i].FrameAct=0;
	AnimTiles[i].Speed=Speed;
	AnimTiles[i].SpeedCnt=0;
	AnimTiles[i].Mode=Mode;

	return (i);
}


public int AnimTileSET(int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode, int Wait)
{
	int i=AnimTileSET(Dest, CoorX, CoorY, SizeX, SizeY, Frames, Speed, Mode);
	if (i != -1)
	{
	AnimTiles[i].Wait=Wait;
	}
	return(i);
}


public int AnimTileSET(int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode, int Wait, int WaitIni)
{
	int i=AnimTileSET(Dest, CoorX, CoorY, SizeX, SizeY, Frames, Speed, Mode);
	if (i != -1)
	{
	AnimTiles[i].Wait=Wait;
	AnimTiles[i].SpeedCnt=WaitIni;
	}
	return(i);
}


// --------------
// Anim Tiles INI
// ==============

final int AnimTileMAX=32;
int AnimTileC;
int[] AnimTileP;
AnimTile[] AnimTiles;

public void AnimTileINI()
{
	AnimTileC = 0;
	AnimTileP = new int[AnimTileMAX];
	AnimTiles = new AnimTile[AnimTileMAX];
	for (int i=0 ; i<AnimTileMAX ; i++) {AnimTileP[i]=i;}
}

// --------------
// Anim Tiles END
// ==============

public void AnimTileEND()
{
	for (int i=0 ; i<AnimTileMAX ; i++)
	{
	AnimTiles[i] = null;
	}
	AnimTileC = 0;
}

// --------------
// Anim Tiles RUN
// ==============

public void AnimTileRUN()
{
	for (int i=0 ; i<AnimTileC ; i++)
	{
	if ( AnimTiles[AnimTileP[i]].Play() )
		{
		int t=AnimTileP[i];
		AnimTiles[t] = null;
		AnimTileP[i] = AnimTileP[--AnimTileC];
		AnimTileP[AnimTileC] = t;
		i--;
		}
	}
}



// ----------------------------------------------------------------------------





// -------------------------------------------------
// Sub-Clase para gestion de Animaciones con Sprites
// =================================================


// *********************
// ---------------------
// Anim Sprites - Engine
// =====================
// *********************

public class AnimSprite
{
int CoorX;
int CoorY;
int FrameIni;
int Frames, Frames2, FrameAct;
int Speed, SpeedCnt;
int Mode;
int Side=1;
int Pause=0;

public AnimSprite() {}

// -----------------
// Anim Sprites Play
// =================

public boolean Play()
{
	if (Pause != 0) {return(false);}

	if (Frames == 0) {return(true);}

	if (SpeedCnt > 0) {SpeedCnt--; return(false);}

	SpeedCnt=Speed;

	switch (Mode)
	{
	case -1:
	return(true);

	case 0:		// Modo Play 1 sola vez
		FrameAct++;
		if (--Frames == 0) {return(true);}
		break;
	
	case 1:		// Modo Play Loop
		FrameAct++;
		if (--Frames == 0) {Frames=FrameAct; FrameAct=0;}
		break;

	case 3:		// Modo Ping-Pong con Play Loop
		FrameAct+=Side;
		if (--Frames == 0)
		{
			if (Side == 1) {Frames=Frames2-1; FrameAct-=2; Side=-1;}
			else {Frames=Frames2-1; FrameAct=1; Side=1;}
		}
		break;
	}

	return (false);
}

};


// ----------------
// Anim Sprites SET
// ================

public int AnimSpriteSET(int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	if (AnimSpriteC == AnimSpriteMAX) {return(-1);}

	int i=AnimSpriteP[AnimSpriteC++];

	AnimSprites[i] = new AnimSprite();

	AnimSprites[i].CoorX=CoorX;
	AnimSprites[i].CoorY=CoorY;
	AnimSprites[i].FrameIni=FrameIni;
	AnimSprites[i].Frames=Frames; AnimSprites[i].Frames2=Frames; AnimSprites[i].FrameAct=0;
	AnimSprites[i].Speed=Speed; AnimSprites[i].SpeedCnt=Speed;
	AnimSprites[i].Mode=Mode;

	return (i);
}


// ----------------
// Anim Sprites INI
// ================

final int AnimSpriteMAX=12;
int AnimSpriteC;
int[] AnimSpriteP;
AnimSprite[] AnimSprites;

public void AnimSpriteINI()
{
	AnimSpriteC = 0;
	AnimSpriteP = new int[AnimSpriteMAX];
	AnimSprites = new AnimSprite[AnimSpriteMAX];
	for (int i=0 ; i<AnimSpriteMAX ; i++) {AnimSpriteP[i]=i;}
}

// ----------------
// Anim Sprites RES
// ================

public void AnimSpriteRES(int i)
{
	AnimSprites[i].Mode=-1;
	AnimSprites[i].Pause=0;
}

// ----------------
// Anim Sprites END
// ================

public void AnimSpriteEND()
{
	for (int i=0 ; i<AnimSpriteMAX ; i++)
	{
	AnimSprites[i] = null;
	}
	AnimSpriteC = 0;
}

// ----------------
// Anim Sprites RUN
// ================

public void AnimSpriteRUN()
{
	for (int i=0 ; i<AnimSpriteC ; i++)
	{
	if ( AnimSprites[AnimSpriteP[i]].Play() )
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



// *******************
// -------------------
// Enemy1 - Engine
// ===================
// *******************

public class Enemy1
{
int Anim;
int Side;

public boolean Play()
{

// Chequeda si tocamos al Jugador
// ------------------------------------
	if (ProtCheck2(AnimSprites[Anim].CoorX+4, AnimSprites[Anim].CoorY+4,  8,8))
	{if (ProtFraMode!=6) {MuerteSET();}}


// Chequeda si la explosion de la dinamita toca al Enemigo
// ------------------------------------
	if ((Dinamita_ON < (16*2)-(6*2)) && (Dinamita_ON > 4))
	{
		if ((DinamitaY+13 <= AnimSprites[Anim].CoorY+2) ||
			(DinamitaY+2  >= AnimSprites[Anim].CoorY+13) ||

			(DinamitaX+13 <= AnimSprites[Anim].CoorX+2) ||
			(DinamitaX+2  >= AnimSprites[Anim].CoorX+13))
		{} else {
			AnimSpriteSET(AnimSprites[Anim].CoorX,AnimSprites[Anim].CoorY, 38, 4, 3, 0);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
			AnimSpriteRES(Anim);
			return(true);
			}
	}

// Chequeda si el Disco toca al Enemigo
// ------------------------------------
	if (Disco_ON != 0)
	{
		if ((AnimSprites[DiscoSpr].CoorY+6 <= AnimSprites[Anim].CoorY+2) ||
			(AnimSprites[DiscoSpr].CoorY+2 >= AnimSprites[Anim].CoorY+13) ||

			(AnimSprites[DiscoSpr].CoorX+9  <= AnimSprites[Anim].CoorX+2) ||
			(AnimSprites[DiscoSpr].CoorX+7  >= AnimSprites[Anim].CoorX+13))
		{} else {
			screen.VibraSET(200);
			AnimSpriteSET(AnimSprites[Anim].CoorX,AnimSprites[Anim].CoorY, 38, 4, 3, 0);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
			AnimSpriteRES(Anim);
			return(true);
			}
	}
	

// Desplaza al enemigo horizontalmente y mira suelo
// ------------------------------------------------
	if ((CheckTile(AnimSprites[Anim].CoorX, AnimSprites[Anim].CoorY+16,  15, 0, 0x07) == 0))
		{AnimSprites[Anim].CoorY++;}
	else {
	
	if (Side==0)
	{
		if ((CheckTile(AnimSprites[Anim].CoorX-1, AnimSprites[Anim].CoorY   , 0, 15, 0x03) == 0) &&
			(CheckTile(AnimSprites[Anim].CoorX-1, AnimSprites[Anim].CoorY+16, 0,  0, 0x07) != 0))
			{AnimSprites[Anim].CoorX--;}
		else {Side=1;}
	} else if (Side == 1)
	{
		if ((CheckTile(AnimSprites[Anim].CoorX+16, AnimSprites[Anim].CoorY   , 0, 15, 0x03) == 0) &&
			(CheckTile(AnimSprites[Anim].CoorX+16, AnimSprites[Anim].CoorY+16, 0,  0, 0x07) != 0))
			{AnimSprites[Anim].CoorX++;}
		else {Side=0;}
	}
	}

	return(false);
}

};

// ----------------
// Enemy1 SET
// ================

public int Enemy1SET(int CoorX, int CoorY)
{
	if (Enemy1C == Enemy1MAX) {return(-1);}

	int Anim = AnimSpriteSET( CoorX, CoorY, 24, 4, 2, 1);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
	if (Anim == -1) {return(-1);}

	int i=Enemy1P[Enemy1C++];

	Enemy1s[i] = new Enemy1();

	Enemy1s[i].Anim=Anim;
	Enemy1s[i].Side=0;
	return (i);
}


// ----------------
// Enemy1 INI
// ================

final int Enemy1MAX=12;
int Enemy1C;
int[] Enemy1P;
Enemy1[] Enemy1s;

public void Enemy1INI()
{
	Enemy1C = 0;
	Enemy1P = new int[Enemy1MAX];
	Enemy1s = new Enemy1[Enemy1MAX];
	for (int i=0 ; i<Enemy1MAX ; i++) {Enemy1P[i]=i;}
}

// ----------------
// Enemy1 END
// ================

public void Enemy1END()
{
	for (int i=0 ; i<Enemy1MAX ; i++)
	{
	Enemy1s[i] = null;
	}
	Enemy1C = 0;
}

// ----------------
// Enemy1 RUN
// ================

public void Enemy1RUN()
{
	for (int i=0 ; i<Enemy1C ; i++)
	{
	if ( Enemy1s[Enemy1P[i]].Play() )
		{
		int t=Enemy1P[i];
		Enemy1s[t] = null;
		Enemy1P[i] = Enemy1P[--Enemy1C];
		Enemy1P[Enemy1C] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-


// *******************
// -------------------
// Enemy2 - Engine
// ===================
// *******************

final int Enemy2SumaY[] = new int[] {-1,0,1,0,1,0,-1,0};

public class Enemy2
{
int Anim;
int Side;
int SumaY=0;
int Modo=0;
int Contador=100;
int BolaCnt=100;

public boolean Play()
{

// Chequeda si tocamos al Jugador
// ------------------------------------
	if (ProtCheck2(AnimSprites[Anim].CoorX+4, AnimSprites[Anim].CoorY+4,  8,8))
	{if (ProtFraMode!=6) {MuerteSET();}}

// Chequeda si la explosion de la dinamita toca al Enemigo
// ------------------------------------
	if ((Dinamita_ON < (16*2)-(6*2)) && (Dinamita_ON > 4))
	{
		if ((DinamitaY+13 <= AnimSprites[Anim].CoorY+2) ||
			(DinamitaY+2  >= AnimSprites[Anim].CoorY+13) ||

			(DinamitaX+13 <= AnimSprites[Anim].CoorX+2) ||
			(DinamitaX+2  >= AnimSprites[Anim].CoorX+13))
		{} else {
			AnimSpriteSET(AnimSprites[Anim].CoorX,AnimSprites[Anim].CoorY, 34, 4, 3, 0);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
			AnimSpriteRES(Anim);
			return(true);
			}
	}

// Chequeda si el Disco toca al Enemigo
// ------------------------------------
	if (Disco_ON != 0)
	{
		if ((AnimSprites[DiscoSpr].CoorY+6 <= AnimSprites[Anim].CoorY+2) ||
			(AnimSprites[DiscoSpr].CoorY+2 >= AnimSprites[Anim].CoorY+13) ||

			(AnimSprites[DiscoSpr].CoorX+9  <= AnimSprites[Anim].CoorX+2) ||
			(AnimSprites[DiscoSpr].CoorX+7  >= AnimSprites[Anim].CoorX+13))
		{} else {
			screen.VibraSET(200);
			AnimSpriteSET(AnimSprites[Anim].CoorX,AnimSprites[Anim].CoorY, 34, 4, 3, 0);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
			AnimSpriteRES(Anim);
			return(true);
			}
	}
	
	
	switch (Modo)
	{
	case 0:		// Enemigo elevandose...
		if (AnimSprites[Anim].FrameAct == 3)
		{
		AnimSprites[Anim].Pause = 1;
		Contador=300; Modo++;
		}
		break;	
	
	case 1:		// Desplaza al enemigo horizontalmente

		if (--BolaCnt < 0)
		{
			if ((AnimSprites[Anim].CoorX-16 < ProtSprX+16) &&
				(AnimSprites[Anim].CoorX+32 > ProtSprX))
			{
			BolaSET(AnimSprites[Anim].CoorX, AnimSprites[Anim].CoorY-6);
			}
		BolaCnt=50;
		}


		if (Side==0)
		{
			if	(CheckTile(AnimSprites[Anim].CoorX-1, AnimSprites[Anim].CoorY+8, 0, 6, 0x03) == 0)
				{AnimSprites[Anim].CoorX--;}
			else {Side=1;}
		} else if (Side == 1)
		{
			if	(CheckTile(AnimSprites[Anim].CoorX+16, AnimSprites[Anim].CoorY+8, 0, 6, 0x03) == 0)
				{AnimSprites[Anim].CoorX++;}
			else {Side=0;}
		}

		AnimSprites[Anim].CoorY += Enemy2SumaY[SumaY++&7];

		if (--Contador < 0)
		{
		Contador=0;
		if (((CheckTile(AnimSprites[Anim].CoorX+ 2, AnimSprites[Anim].CoorY+18) & 0x07) != 0) &&
			((CheckTile(AnimSprites[Anim].CoorX+13, AnimSprites[Anim].CoorY+18) & 0x07) != 0))
			{
			AnimSprites[Anim].Pause=0;
			Modo++;
			}
		}
		break;

	case 2:		// Enemigo Aterrizando...
		if (AnimSprites[Anim].FrameAct == 0)
		{
		AnimSprites[Anim].Pause = 1;
		Contador=100; Modo++;
		}
		break;	

	case 3:
		if (--Contador < 0)
		{
		AnimSprites[Anim].Pause = 0;
		Modo=0;
		}
		break;
	}

	return(false);
}

};

// ----------------
// Enemy 2 SET
// ================

public int Enemy2SET(int CoorX, int CoorY)
{
	if (Enemy2C == Enemy2MAX) {return(-1);}

	int Anim = AnimSpriteSET( CoorX, CoorY, 20, 4, 2, 3);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
	if (Anim == -1) {return(-1);}

	int i=Enemy2P[Enemy2C++];

	Enemy2s[i] = new Enemy2();
		
	Enemy2s[i].Anim=Anim;
	Enemy2s[i].Side=0;
	return (i);
}


// ----------------
// Enemy2 INI
// ================

final int Enemy2MAX=12;
int Enemy2C;
int[] Enemy2P;
Enemy2[] Enemy2s;

public void Enemy2INI()
{
	Enemy2C = 0;
	Enemy2P = new int[Enemy2MAX];
	Enemy2s = new Enemy2[Enemy2MAX];
	for (int i=0 ; i<Enemy2MAX ; i++) {Enemy2P[i]=i;}
}

// ----------------
// Enemy2 END
// ================

public void Enemy2END()
{
	for (int i=0 ; i<Enemy2MAX ; i++)
	{
	Enemy2s[i] = null;
	}
	Enemy2C = 0;
}

// ----------------
// Enemy2 RUN
// ================

public void Enemy2RUN()
{
	for (int i=0 ; i<Enemy2C ; i++)
	{
	if ( Enemy2s[Enemy2P[i]].Play() )
		{
		int t=Enemy2P[i];
		Enemy2s[t] = null;
		Enemy2P[i] = Enemy2P[--Enemy2C];
		Enemy2P[Enemy2C] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Huevo - Engine
// ===================
// *******************

public class Huevo
{
int Dest;
int CoorX;
int CoorY;
int Contador;
int Modo=0;

public boolean Play()
{
	switch (Modo)
	{
	case 0:		// Si el player est� cerca el huevo se abre.
		if ( (CoorX-3-16 < ProtSprX+16) && (CoorX-3+32 > ProtSprX) )
		{
		AnimTileSET(Dest ,0 ,10 ,2 ,2 ,4 ,2 ,2);	// (Dest, SourX, SourY, SizeX, SizeY, Frames, Speed, Modo)
		Contador=8; Modo++;
		}
		break;

	case 1:		// Cuando el huevo est� abierto lanzamos Bola
		if (--Contador == 0)
		{
		BolaSET( CoorX, CoorY );
		Contador=100; Modo++;
		}
		break;

	case 2:		// Esperamos un tiempo para proximo ataque.
		if (--Contador == 0) {Modo=0;}
		break;
	}

	return(false);
}

};

// ----------------
// Huevo SET
// ================

public int HuevoSET(int Dest, int CoorX, int CoorY)
{
	if (HuevoC == HuevoMAX) {return(-1);}

	int i=HuevoP[HuevoC++];

	Huevos[i] = new Huevo();

	Huevos[i].Dest=Dest;
	Huevos[i].CoorX=CoorX;
	Huevos[i].CoorY=CoorY;
	return (i);
}


// ----------------
// Huevo INI
// ================

final int HuevoMAX=12;
int HuevoC;
int[] HuevoP;
Huevo[] Huevos;

public void HuevoINI()
{
	HuevoC = 0;
	HuevoP = new int[HuevoMAX];
	Huevos = new Huevo[HuevoMAX];
	for (int i=0 ; i<HuevoMAX ; i++) {HuevoP[i]=i;}
}

// ----------------
// Huevo END
// ================

public void HuevoEND()
{
	for (int i=0 ; i<HuevoMAX ; i++)
	{
	Huevos[i] = null;
	}
	HuevoC = 0;
}

// ----------------
// Huevo RUN
// ================

public void HuevoRUN()
{
	for (int i=0 ; i<HuevoC ; i++)
	{
	if ( Huevos[HuevoP[i]].Play() )
		{
		int t=HuevoP[i];
		Huevos[t] = null;
		HuevoP[i] = HuevoP[--HuevoC];
		HuevoP[HuevoC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// Bola - Engine
// ===================
// *******************

int BolaDespY[] = new int[] {3,2,2,1,1,0,0,0,-1,-1,-2,-2,-3};

public class Bola
{
int Anim;
int Side;
int DespYcnt;
int Modo;

public boolean Play()
{

	if (ProtCheck2(AnimSprites[Anim].CoorX+3, AnimSprites[Anim].CoorY+9,  5,5))
	{if (ProtFraMode!=6) {MuerteSET();}}


	switch (Modo)
	{
	case 0:
		AnimSprites[Anim].CoorY+=BolaDespY[DespYcnt];
		if (--DespYcnt < 0) {Modo=1;};

		if (Side==0)
		{
			if (CheckTile(AnimSprites[Anim].CoorX+3-1, AnimSprites[Anim].CoorY+9, 0, 5, 0x03) == 0)
				{AnimSprites[Anim].CoorX-=2;}
		} else if (Side == 1)
		{
			if (CheckTile(AnimSprites[Anim].CoorX+8+1, AnimSprites[Anim].CoorY+9, 0, 5, 0x03) == 0)
				{AnimSprites[Anim].CoorX+=2;}
		}

		break;

	case 1:
		if (CheckTile(AnimSprites[Anim].CoorX+4, AnimSprites[Anim].CoorY+14+3, 4, 0, 0x07) == 0)
			{AnimSprites[Anim].CoorY+=3;}
		else {
			int CoorX = AnimSprites[Anim].CoorX;
			int CoorY = AnimSprites[Anim].CoorY;
			AnimSpriteRES(Anim);
			AnimSpriteSET( CoorX, CoorY, 28, 4, 2, 0);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
			return(true);
		}
		break;
	}
	
	return(false);
}

};

// ----------------
// Bola SET
// ================

public int BolaSET(int CoorX, int CoorY)
{
	if (BolaC == BolaMAX) {return(-1);}

	int Anim = AnimSpriteSET( CoorX, CoorY, 28, 1, 2, 1);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
	if (Anim == -1) {return(-1);}

	int i=BolaP[BolaC++];

	Bolas[i] = new Bola();

	Bolas[i].Anim=Anim;
	Bolas[i].Side=(ProtSprX+8 < CoorX+5) ? 0 : 1;
	Bolas[i].DespYcnt=BolaDespY.length-1;
	Bolas[i].Modo=0;
	return (i);
}


// ----------------
// Bola INI
// ================

final int BolaMAX=12;
int BolaC;
int[] BolaP;
Bola[] Bolas;

public void BolaINI()
{
	BolaC = 0;
	BolaP = new int[BolaMAX];
	Bolas = new Bola[BolaMAX];
	for (int i=0 ; i<BolaMAX ; i++) {BolaP[i]=i;}
}

// ----------------
// Bola END
// ================

public void BolaEND()
{
	for (int i=0 ; i<BolaMAX ; i++)
	{
	Bolas[i] = null;
	}
	BolaC = 0;
}

// ----------------
// Bola RUN
// ================

public void BolaRUN()
{
	for (int i=0 ; i<BolaC ; i++)
	{
	if ( Bolas[BolaP[i]].Play() )
		{
		int t=BolaP[i];
		Bolas[t] = null;
		BolaP[i] = BolaP[--BolaC];
		BolaP[BolaC] = t;
		i--;
		}
	}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Ascensor - Engine
// ===================
// *******************

public class Ascensor
{
int Anim1;
int Anim2;
int Side=-1;
int Old=0;

public boolean Play()
{

		if (Side==1)
		{
			if (CheckTile(AnimSprites[Anim1].CoorX, AnimSprites[Anim1].CoorY+16, 31, 0, 0xFFF) == 0)
				{AnimSprites[Anim1].CoorY+=2;AnimSprites[Anim2].CoorY+=2;}
			else {Side=-1;}
		} else if (Side == -1)
		{
			if (CheckTile(AnimSprites[Anim1].CoorX, AnimSprites[Anim1].CoorY-24, 31, 0, 0xFFF) == 0)
				{AnimSprites[Anim1].CoorY-=2;AnimSprites[Anim2].CoorY-=2;}
			else {Side=1;}
		}



		if ((ProtFraMode != 3) &&
			(ProtSprX+6  >= AnimSprites[Anim1].CoorX) &&
		 	(ProtSprX+10 <= AnimSprites[Anim1].CoorX+32) &&
		 	(ProtSprY+24 >= AnimSprites[Anim1].CoorY-2) &&
		 	(ProtSprY+24 <= AnimSprites[Anim1].CoorY+8))
		{
		ProtSprY=AnimSprites[Anim1].CoorY-24;
		ProtFraAscensor=1;
		if (Old==0) {ProtFraMode=0;}
		Old=1;
		}
		else {Old=0;}


	return(false);
}

};

// ----------------
// Ascensor SET
// ================

public int AscensorSET(int CoorX, int CoorY)
{
	if (AscensorC == AscensorMAX) {return(-1);}

	int Anim1 = AnimSpriteSET( CoorX, CoorY, 32, 1, 2, 1);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
	if (Anim1 == -1) {return(-1);}
	int Anim2 = AnimSpriteSET( CoorX+16, CoorY, 33, 1, 2, 1);	// (CoorX, CoorY, FrameIni, Frames, Speed, Modo)
	if (Anim2 == -1) {return(-1);}

	int i=AscensorP[AscensorC++];

	Ascensors[i] = new Ascensor();
		
	Ascensors[i].Anim1=Anim1;
	Ascensors[i].Anim2=Anim2;
	return (i);
}


// ----------------
// Ascensor INI
// ================

final int AscensorMAX=12;
int AscensorC;
int[] AscensorP;
Ascensor[] Ascensors;

public void AscensorINI()
{
	AscensorC = 0;
	AscensorP = new int[AscensorMAX];
	Ascensors = new Ascensor[AscensorMAX];
	for (int i=0 ; i<AscensorMAX ; i++) {AscensorP[i]=i;}
}

// ----------------
// Ascensor END
// ================

public void AscensorEND()
{
	for (int i=0 ; i<AscensorMAX ; i++)
	{
	Ascensors[i] = null;
	}
	AscensorC = 0;
}

// ----------------
// Ascensor RUN
// ================

public void AscensorRUN()
{
	for (int i=0 ; i<AscensorC ; i++)
	{
	if ( Ascensors[AscensorP[i]].Play() )
		{
		int t=AscensorP[i];
		Ascensors[t] = null;
		AscensorP[i] = AscensorP[--AscensorC];
		AscensorP[AscensorC] = t;
		i--;
		}
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
// Cohete - Engine
// ===================
// *******************

int Cohe_ON, CoheCnt;
int CoheX, CoheY, CoheFocCnt;
int CohePortaY;
int CoheFrame, CoheFocFrame, CohePortaFrame;
int CoheSin;
int CoheMode;
int CoheFaseY=-48;

// -------------------
// Cohete INI
// ===================

public void CoheteINI()
{
	Cohe_ON=0;
	CoheFrame=-1;
	CoheFocFrame=-1;
	CohePortaFrame=-1;
}

// -------------------
// Cohete SET
// ===================

public void CoheteSET()
{
	if (JugarFinal == 0)
	{
	CoheMode=0;
	CoheX=10;
	CoheY=70;
	} else {
	CoheMode=20;
	CoheX=10;
	CoheY=23+48;
	}

	Cohe_ON=1;
}

// -------------------
// Cohete RUN
// ===================

public void CoheteRUN()
{
	if ((KeybB1 != 0) && (KeybB2 == 0) && (CoheMode >= 10 && CoheMode <= 14)) { AutoMov_ON=0; FaseExit=2; }

	switch (CoheMode)
	{

// Subimos Portada Astro 3003 hacia arriba (Portada)
// -------------------------------------------------
	case 0:
		JugarShow=0;
		ProtSET();
		CohePortaFrame=0;
		CohePortaY=128;
		CoheSin=0;
		CoheFocCnt=0;
		CoheMode++;
	case 1:
		int PortaSizeY=screen.SprPortaImg.getHeight();
		CohePortaY=PortaSizeY-((TablaSeno[CoheSin]*PortaSizeY)/256)+((GameSizeY-PortaSizeY)/2);

//		ProtSprY-=2; if (ProtSprY < LCDMidY) {ProtSprY+=72;}

		CoheSin+=3; if (CoheSin>90) {CoheMode++; JugarStart=0;}
	break;

	case 2:
//		ProtSprY-=2; if (ProtSprY < LCDMidY) {ProtSprY+=72;}

//		if ((KeybB1 == 5) & (KeybB2 != 5)) {CohePortaFrame=-1; CoheSin=0; CoheMode=10; }
		if (JugarStart!=0) {CohePortaFrame=-1; CoheSin=0; CoheMode=10; }
	break;


// La nave aterriza en el planeta (Inicio)
// ---------------------------------------
	case 10:
		JugarShow=1;
		CoheY=-87-73+((TablaSeno[CoheSin]*(183+48))/256);
		CoheFocFrame=(CoheSin>45)?(((CoheSin-45)*5)/46)+CoheFocCnt:-1;
		if (++CoheFocCnt == 2) {CoheFocCnt=0;}

		ProtSprY=LCDMidY+((TablaSeno[CoheSin]*(52+70-LCDMidY))/256);
		CoheFrame=0;
		if (++CoheSin>90) {CoheSin=90; CoheFocFrame=5; CoheMode++;}
	break;

	case 11:
		if (CoheFocFrame-- == 0) {CoheMode++; WaitSET(1000, GameStatus);}
	break;

	case 12:
		CoheFrame=1;
		CoheMode++;
		WaitSET(500, GameStatus);
	break;

	case 13:
		Prot_ON=1;
		AutoMovSET(AutoMovStart);
		CoheMode++;
	break;


// La nave despega  del planeta (Final)
// ------------------------------------
	case 20:
		JugarShow=1;
		ProtSET();
		ProtSprX=28*8;
		ProtSprY=21*8;
		ProtFraSide=1;
		Prot_ON=1;

		CoheFrame=1;
		CoheSin=0;
		CoheFocFrame=-1;
		CoheFocCnt=0;
		CoheMode++;

		AutoMovSET(AutoMovEnd);
	break;

	case 22:
		ProtSprX=40;
		CoheMode++;
		WaitSET(700, GameStatus);
	break;

	case 23:
		CoheFrame=0;
		CoheMode++;
		WaitSET(1000, GameStatus);
	break;

	case 24:
		if (++CoheFocFrame == 5) {CoheFocCnt=0;CoheSin=90;CoheMode++;}
	break;

	case 25:
		CoheFocFrame=4+CoheFocCnt;
		if (++CoheFocCnt == 2) {CoheFocCnt=0;}

		CoheY=-87-73+((TablaSeno[CoheSin]*(183+48))/256);
		if (--CoheSin < 0) {CoheCnt=20; CoheMode++;}
	break;

	case 26:
//		CoheY-=1;
		CoheFocFrame=4+CoheFocCnt;
		if (++CoheFocCnt == 2) {CoheFocCnt=0;}

//		ProtSprY-=2; if (ProtSprY < LCDMidY) {ProtSprY+=72;CoheY+=72;}

		if (--CoheCnt < 0) {CoheMode++;}
	break;

	case 27:
//		ProtSprY-=2; if (ProtSprY < LCDMidY) {ProtSprY+=72;}
		CoheMode=0;
	break;
	}
}

// <=- <=- <=- <=- <=-


// *******************
// -------------------
// AutoMov - Engine
// ===================
// *******************

int AutoMov_ON=0;
int AutoMovAry, AutoMovCnt;
int AutoMovSeq[];
int AutoMovStart[] = new int[] {1,92,  2,1,  1,30,  0,0};
int AutoMovEnd[] = new int[] {2,13,  3,68,  2,1,  3,16,  2,1,  3,6,  4,0};

// -------------------
// AutoMov INI
// ===================

public void AutoMovINI()
{
	AutoMov_ON=0;
}

// -------------------
// AutoMov SET
// ===================

public void AutoMovSET(int[] Sequencia)
{
	AutoMovSeq=Sequencia;
	AutoMovCnt=AutoMovSeq[1];
	AutoMovAry=0;
	AutoMov_ON=1;
}

// -------------------
// AutoMov RUN
// ===================

public void AutoMovRUN()
{

	KeybX1=0;KeybX2=0;KeybX=0;
	KeybY1=0;KeybY2=0;KeybY=0;

	if (--AutoMovCnt < 0) {AutoMovAry+=2; AutoMovCnt=AutoMovSeq[AutoMovAry+1];}

	switch (AutoMovSeq[AutoMovAry])
	{
	case 0:
		AutoMov_ON=0;
	break;

	case 1:
		KeybX1=1;
	break;

	case 2:
		KeybY1=-1;
	break;

	case 3:
		KeybX1=-1;
	break;

	case 4:
		AutoMov_ON=0;
		Prot_ON=0;
		CoheMode=22;
	break;
	}


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

	switch (Type)
	{
	case 0:
		marco.MarcoINI();
		marco.MarcoADD(0x111,Texto.Level+" "+FaseTilFileTab[JugarLevelAct]);
		marco.MarcoSET_Texto();
	break;

	case 1:
		marco.MarcoINI();
		marco.MarcoADD(0x111,Texto.Level);
		marco.MarcoADD(0x111,Texto.Complet);
		marco.MarcoSET_Texto();
	break;

	case 2:
		marco.MarcoINI();
		marco.MarcoADD(0x111,Texto.GameOver);
		marco.MarcoSET_Texto();
	break;

	case 3:
		marco.MarcoINI();
		marco.MarcoADD(0x111,Texto.Loading);
		marco.MarcoSET_Texto();
	break;
	}

}


// -------------------
// Panel RES
// ===================

public void PanelRES()
{
	marco.MarcoEND();
	Panel_ON=0;
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
int MarHelpFlag=1;		// =1 para que no se muestre el Menu Controls.

// -------------------
// Marco SET
// ===================

public void MarcoSET(int Status)
{
	MarcoStatusOld=MarcoStatus;
	MarcoStatus=Status;

	MarGameStatus=GameStatus;

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
		marco.MarcoADD(0x011,Texto.Start,1);
		if (JugarLevelUlt > 1)
		{
		marco.MarcoADD(0x011,Texto.PlayLev,15);
		}
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
		marco.MarcoADD(0x011,Texto.TryAgain,7);
		marco.MarcoADD(0x011,Texto.Sound,2,GameSound);
		marco.MarcoADD(0x011,Texto.Vibrate,3,GameVibra);
		marco.MarcoADD(0x011,Texto.Controls,5);
		marco.MarcoADD(0x011,Texto.Restart,6);
		marco.MarcoADD(0x011,Texto.ExitGame,9);
		marco.MarcoSET_Option();
	break;

	case 50:
		marco.MarcoINI();
		for (int i=JugarLevelUlt ; i>0 ; i--)
		{
		marco.MarcoADD(0x011,Texto.Level+" "+i,16);
		}
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
		JugarIMP();
		MarcoSET(10);
		return;
		}
	break;

	case 21:
		MarcoEND();
		JugarIMP();
		MarcoSET(MarcoStatusOld);
	break;

	case 41:
		MarcoEND();
		JugarIMP();
		MarcoSET(MarcoStatusOld);
	break;
	}

	if (KeybB1==10 && KeybB2!=10) {MarcoEND(); return;}

	int MarcoKey=0;
	if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
	if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
	if (KeybB1== 5 && KeybB2!= 5) {MarcoKey=5;}

	if (KeybB1!= 0 && KeybB2== 0 && marco.Marco_ON==2) {MarcoEXE(-2); return;}

	MarcoEXE(marco.MarcoRUN(MarcoKey,KeybY1));
}

// -------------------
// Marco EXE
// ===================

public void MarcoEXE(int Opcion)
{
	if (Opcion==-2)
	{
		MarcoRES();
		JugarIMP();
		MarcoStatus++;
		return;
	}

	switch (Opcion)
	{
	case 0:	// Continue
		MarcoEND();
	break;

	case 1:	// Start
		MarcoEND();
		JugarStart=1;
	break;

	case 15:// Continuar Level...
		MarcoEND();
		JugarIMP();
		MarcoSET(50);
	break;

	case 16:// Level x
		MarcoEND();
		JugarLevelAct=JugarLevelUlt-marco.MarTexPos-1;
		FaseNext();
	break;

	case 2:	// Sound ON/OFF
		GameSound=marco.getOption();
		if (JugarLevelAct==0)
		{
			if (GameSound==0)
			{
			screen.SoundRES();
			} else {
			screen.SoundSET(1,0);
			}
		}
	break;

	case 3:	// Vibrate ON/OFF
		GameVibra=marco.getOption();
	break;

	case 4:	// Credits
		MarcoEND();
		JugarIMP();
		MarcoSET(20);
	break;

	case 5:	// Controls
		MarcoEND();
		JugarIMP();
		MarcoSET(40);
	break;

	case 6:	// Restard
		MarcoEND();
		JugarVidas=0;
		FaseExit=1;
	break;


	case 7:	// Suicide
		MarcoEND();
		SuicidioSET();
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
		rs = RecordStore.openRecordStore("AstroPrefs", true);

		if (rs.getNumRecords() == 0)
		{
		rs.addRecord(PrefsData, 0, PrefsData.length);
		} else {
		PrefsData=rs.getRecord(1);
		}
		rs.closeRecordStore();
	} catch(Exception e) {}
}

// -------------------
// Prefs SET
// ===================

public void PrefsSET()
{
	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("AstroPrefs", true);

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
// Cheats - Engine
// ===================

public void Cheats()
{
	if (KeybB1 == KeybB2) {return;}

	switch (KeybB1)
	{
	case 7:
		if (++ProtFraType > 4) {ProtFraType=0;}
	break;

	case 9:
		FaseNext();
	break;
	}
}

// <=- <=- <=- <=- <=-



// **************************
// --------------------------
// Command Listerner - Engine v1.0 - Rev.0 (9.7.2003)
// ==========================
// **************************

// -----------------------------------
// A�adir a la definicion de la Clase:
// -----------------------------------
// implements CommandListener
// ===================================

String[] ListenerStr = new String[] { "Menu","Selec" };

Command[] ListenerCmd;

// -------------------
// Listener EXE
// ===================

public void ListenerEXE(int CMD)
{
}


// -------------------
// Listener SET
// ===================

public void ListenerSET(Canvas c)
{
	ListenerCmd = new Command[ListenerStr.length];

	for (int i=0 ; i<ListenerStr.length ; i++)
	{
	ListenerCmd[i] = new Command(ListenerStr[i], Command.SCREEN, 1);
	c.addCommand(ListenerCmd[i]);
	}
	c.setCommandListener(this);
}

// -----------------------
// Listener command Action
// =======================

public void commandAction (Command c, Displayable d)
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
} // Cierra la Clase
// **************************************************************************//