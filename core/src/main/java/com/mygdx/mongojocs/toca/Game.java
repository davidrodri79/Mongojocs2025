package com.mygdx.mongojocs.toca;

// -----------------------------------------------
// Clase Bios integrada en la Game ----------------------------------------------------------------------------------
// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// Adaptacion J2ME Rev.2 (1.4.2004)
// ------------------------------------


//#ifdef J2ME


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextBox;
import com.mygdx.mongojocs.midletemu.TextField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Random;


//#endif


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME
public class Game extends MIDlet implements Runnable, CommandListener       // Clase Bios integrada en la Game
//#elifdef DOJA
//#endif
{

	//#ifdef TIME_DEFAULT
	//#elifdef TIME_SEMI_SLOW
	//#elifdef TIME_SLOW
	public int MILLIS_PER_SECOND = 149;
	//#elifdef TIME_VERY_SLOW
	//#elifdef TIME_AEUSSERST_SLOW
	//#elifdef TIME_UBER_SLOW
	//#elifdef TIME_FAST
	//#elifdef TIME_VERY_FAST
	//#endif
	
int oldVel = 0;
int currentSound = -1;
GameCanvas gc;
int mdisp = 0;
int loading = 0;
String playerName = "Player 1";
TextBox tbPlayerName = null;
boolean tbOnScreen = false;
boolean exitConfirmation = false;
int oldbs;
String saveGames[];
byte[] savePos;
byte[] saveCar;
byte saveSlot;
int menux = 0, menuy = 0;
int currentTrack;
int currentTrackHistory;
int nOppo = 0;
int nPhant = 0;
int gameOver = 0;
int startUsid;
long lastSound = -200;
int selectingCar = 0;
int selectedCar = 0;
int historyMode = 0;
int lapCounter = 0;
int lastLPusid = 0;
long lapTime = 0;
long bestTime = 0;
long lastLapTime = 0;
long finishTime = 0;
long timePause = -1;
long timeDiff = 0;
long totalTimeDiff = 0;
long menuStart = -1;
int lastmenupos = -1;
//Integer tDifMutex = new Integer(0);
int endPosition = 0;
int bestTimeBlink = 0;
int position = 0;
int ttplayers = 0;
//#ifdef PRELOAD_CARS
Image carImages[] = null;
//#endif
boolean menuSecActive = false;

//#ifdef MO
//long lastFrameTime = System.currentTimeMillis();
//#endif


long gameTickCounter = 0;

//#ifndef TEXT_SELECT_CAR
Image slCar;
//#endif
//int[] displFL = { 6, -452, 9, 9, -232, -374};
//int[] startU = {20 << 7, 456 << 7, 21 << 7, 344 << 7,235 << 7, 379 << 7};

//int[] displFL = {     4,       4,      8,      9,     4,      6};
//int[] startU = {42 << 7, 71 << 7, 6 << 7, 4 << 7,1 << 7, 3 << 7};		//formula ford
//Vallelunga,Oulton Park,Manthorp Park,Pikes Peak,Catalunya,Road America

//#ifdef TOCA

//A1-Ring, Catalunya, Nurburgring, Kyalami, Road America, Manthorp Park
int[] displFL = {     6,      4,       9,      6,     6,      6};
int[] startU = {20 << 7, 1 << 7, 21 << 7, 6 << 7,3 << 7, 6 << 7};

//#else
//#endif


int[] menuSelCols = {0x3a7998, 0x588e81, 0x84ab5f, 0xb5cc39, 0xe1eb18, 0xffff00};
int[] menuCols = {0x3a7998, 0x588da8, 0x84acbe, 0xb5cdd8, 0xe1ebef, 0xffffff};
int[] menuShadow = {0x3a7998, 0x316781, 0x244c5e, 0x162e39, 0x091316, 0x000000};
int[] mainMenuShadow = {0xe0e0e0, 0xb3b3b3, 0x7f7f7f, 0x4c4c4c, 0x1e1e1e, 0x000000}; 
	
int menuSel = 0;
int menuIt = 0;
int vu;
int aftahRank = 0;
int mainMenu = 0;
//#ifdef ONLY_TWO_LAPS
//#else
int totalLaps = 3;
//#endif
int ttpl[] = new int[4];
FwdPhantomVehicle phantom = null;
long lastPhantomTick = -1;
int currPlayer = 0;
int playerColors[] = {0xff0000, 0x00ff00, 0x0000ff, 0x000000};
int showingResults = 0;
int curvepoints[];
int nextTrack = 0;
//#ifdef SEMI_FULL_FEATURED
boolean hangScreen = true;
//#endif

//#ifdef J2ME
Thread thread;

int SOUND_MAIN_MENU = 0;
int SOUND_ACCELERATE = 1;
int SOUND_BRAKE = 2;
int SOUND_ENGINE_LOOP = 3;
int SOUND_ENGINE_STOP = 4;
int SOUND_INGAME = 1;
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public Game()                                                               // Clase Bios integrada en la Game
{
	System.gc();

	gc = new GameCanvas( (Game)this );
	Display.getDisplay(this).setCurrent(gc);
	System.gc();
	

	gameInit();
	//saveSlots();
	
	thread=new Thread(this); System.gc();
	thread.start();
}

//#elifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


//#ifdef DOJA
//#endif


public void resetGameSlots(byte[] data) {
	for(int j = 0; j < 4; j ++) {
		int k = j * 30;
		data[k + 0] = 5;
		data[k + 1] = '-';
		data[k + 2]=  '-';
		data[k + 3] = '-';
		data[k + 4] = '-';
		data[k + 5] = '-';
		
		for(int i = 6; i < 30; i++) {
			data[k + i] = (byte) j;
		}
	}
}
/*
public void resetGameData() {
	for(int i = 0; i < saveGames.length; i++) {
		saveGames[i] = "empty";
		savePos[i] = 0;
	}
	saveSlots();
	gameSlotsInit();
}
*/
public void gameSlotsInit() {
	RecordStore rs;
	boolean created = false;
	byte[] data = new byte[30 * 4];
	
	resetGameSlots(data);
	
	try {
		rs = RecordStore.openRecordStore("tocadata", true);
		
		int k = rs.getNumRecords();
		//crear els records que toca en el toca :D
		if(k == 0) {
			rs.addRecord(data, 0, data.length);
		}
		
		saveGames = new String[4];
		savePos = new byte[4];
		saveCar = new byte[4];
		
		data = rs.getRecord(1);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
		
		for(int i = 0; i < 4; i++) {
			int len = dis.readByte();
			byte[] tmp = new byte[len];
			dis.readFully(tmp, 0, len);
			saveGames[i] = new String(tmp);
			savePos[i] = dis.readByte();
			saveCar[i] = dis.readByte();
			//System.out.println(i + " :: " + saveGames[i] + " :: " + savePos[i] + " :: " + saveCar[i]);
			
			tmp = new byte[30];
			int kl = 30 - (len + 2 + 1 + 1 + 1) + 2;
			dis.readFully(tmp, 0, kl);
		}
		
		saveSlot = 0;
		
		rs.closeRecordStore();
	} catch(Exception e) {
		//#ifdef EXCEPTION_CONTROL
		//gc.excp = e;
		//#endif
		System.out.println("exception " +e + " " + e.getMessage());
	}
}

//#ifdef DOJA
//#elifdef J2ME
public void gameInit() {
	gameSlotsInit();
}	
//#endif

//#ifdef DOJA
//#elifdef J2ME
public void saveSlots() {
	try {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		for(int i = 0; i < 4; i++) {
			int len = saveGames[i].length();
			dos.writeByte(len);
			dos.write(saveGames[i].getBytes());
			dos.writeByte(savePos[i]);
			dos.writeByte(saveCar[i]);
			
			int kl = 30 - (len + 2 + 1 + 1 + 1) + 2;
			for(int j = 0; j < kl; j++) {
				dos.writeByte(0);
			}
		}
		
		RecordStore rs = RecordStore.openRecordStore("tocadata",false);
		rs.setRecord(1, baos.toByteArray(), 0, baos.toByteArray().length);
		rs.closeRecordStore();
	} catch(Exception e) {
		//#ifdef EXCEPTION_CONTROL
		//gc.excp = e;
		//#endif
		System.out.println("exception " + e + " :: " + e.getMessage());
	}
}
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// startApp - Al inicio y cada vez que se hizo pauseApp()
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void startApp()
{
	if(tbOnScreen) {
		Display.getDisplay(this).setCurrent(tbPlayerName);
	} else {
		if(gc != null) {
			Display.getDisplay(this).setCurrent(gc);
			gc.forceRepaint = true;
		}
	}
	gamePaused = false;
}

//#elifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// pauseApp - Cada vez que se PAUSA el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//#ifdef J2ME
public void pauseApp() {
	gamePaused = true;
	notifyPaused();
}
//#endif
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// destroyApp - Para DESTRUIR el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	
	saveSlots();
	savePrefs();
	gameExit = true;
	
	Display.getDisplay(this).setCurrent(null);
	
	//#ifdef J2ME
	notifyDestroyed();
	//#elifdef DOJA
	//#endif
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;
boolean gamePaused = false;

long GameMilis, CPU_Milis;
int  GameSleep;

//#ifdef NK-s40
	int framesToLight = 0;
//#endif

public void run()
{
	System.gc();
	biosCreate(); System.gc();
	gc.canvasInit(); System.gc();
	biosInit(); System.gc();

	while (!gameExit)
	{
		if (!gamePaused)
		{
			GameMilis = System.currentTimeMillis();

			keyboardTick();
			gc.soundTick();

			try {
				biosTick();
				gc.gameDraw();
			} catch (Exception e) {
				System.out.println("*** Exception en la parte L�gica ***"); e.printStackTrace();
				//#ifdef EXCEPTION_CONTROL
				//gc.excp = e;
				//#endif
			}
			
			//#ifdef NK-s40
			if (--framesToLight < 0) {
				DeviceControl.setLights(0, 100);
				framesToLight = 5;
			}
			//#endif
			
			if(gc.forceRepaint) {
				gc.refresh();
			}
		}
		//#ifndef SEMI_FULL_FEATURED
		arrowPos++;
		if(arrowPos > 5) arrowPos = 0;
		//#else
		arrowPos = 5;
		//#endif

		//#ifdef MO
		//#else
		//#ifndef Z1010
		GameSleep=(60-(int)(System.currentTimeMillis()-GameMilis));
		//#else
		//#endif
		if (GameSleep<1) {GameSleep=1;}
		//#endif
		try	{Thread.sleep(GameSleep);} catch(Exception e) {}
		//	System.gc();
	}

	gc.soundStop();
	biosDestroy();
	destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Regla de 3
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public int regla3(int x, int min, int max) {return (x*max)/min;}
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
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 >= x1+xs1 ) || ( x2+xs2 <= x1 ) || ( y2 >= y1+ys1 ) || ( y2+ys2 <= y1 ));
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public byte[] loadFile(String Nombre)
{
	/*System.gc();

	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] buffer = new byte[1024];

	try
	{
		int Size = 0;

		while (true)
		{
		int Desp = is.read(buffer, 0, buffer.length);
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
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	return bytes;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void loadFile(byte[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		is.read(buffer, 0, buffer.length);
		is.close();
	}catch(Exception e) {}
	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public byte[] loadFile(String Nombre, int Pos, short[] Size)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] Dest = new byte[Size[Pos]];

	try {
		for (int i=0 ; i<Pos ; i++) {is.skip(Size[i]);}
		is.read(Dest, 0, Dest.length);
		is.close();
	} catch(Exception e) {}
	System.gc();
	return Dest;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void loadFile(short[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		for (int i=0 ; i<buffer.length ; i++)
		{
		buffer[i]  = (short) (is.read() << 8);
		buffer[i] |= is.read();
		}
	is.close();
	}catch(Exception exception) {}
	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// *******************
// -------------------
// textos - Engine - v1.0 - Rev.2 (5.5.2004)
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
final static int TEXT_TIMEATTACK	= 13;
final static int TEXT_REMOTE_BEST	= 14;
final static int TEXT_LOCAL_BEST	= 15;
final static int TEXT_2_PLAYERS		= 16;
final static int TEXT_3_PLAYERS		= 17;
final static int TEXT_4_PLAYERS 	= 18;
final static int TEXT_TRACK_NAME_1	= 19;
final static int TEXT_TRACK_PRE_1	= 20;
final static int TEXT_TRACK_NAME_2	= 21;
final static int TEXT_TRACK_PRE_2	= 22;
final static int TEXT_TRACK_NAME_3	= 23;
final static int TEXT_TRACK_PRE_3	= 24;
final static int TEXT_TRACK_NAME_4	= 25;
final static int TEXT_TRACK_PRE_4	= 26;
final static int TEXT_TRACK_NAME_5	= 27;
final static int TEXT_TRACK_PRE_5	= 28;
final static int TEXT_TRACK_NAME_6	= 29;
final static int TEXT_TRACK_PRE_6	= 30;
final static int TEXT_WIN_TRACK		= 31;
final static int TEXT_ALMOST_WIN_TRACK = 32;
final static int TEXT_LOSE_TRACK	= 33;
final static int TEXT_CRASHED_TRACK	= 34;
final static int TEXT_CAR_1_NOT_USED	= 35;
final static int TEXT_CAR_2_NOT_USED	= 36;
final static int TEXT_RACER00		= 37;
final static int TEXT_RACER01		= 38;
final static int TEXT_RACER02		= 39;
final static int TEXT_RACER03		= 40;
final static int TEXT_RACER04		= 41;
final static int TEXT_RACER05		= 42;
final static int TEXT_RACER06		= 43;
final static int TEXT_RACER07		= 44;
final static int TEXT_RACER08		= 45;
final static int TEXT_RACER09		= 46;
final static int TEXT_BREAK_BEFORE_0	= 48;
final static int TEXT_BREAK_BEFORE_1	= 49;
final static int TEXT_BREAK_BEFORE_2	= 50;
final static int TEXT_BREAK_BEFORE_3	= 51;
final static int TEXT_BREAK_BEFORE_4	= 52;
final static int TEXT_RESET_SLOT 	= 53;
final static int TEXT_PLAYER_NAME	= 54;
final static int TEXT_ENTER_NAME	= 55;
final static int TEXT_CAR_1		= 56;
final static int TEXT_CAR_2		= 57;
final static int TEXT_CAR_3		= 58;
final static int TEXT_SHORT_LEGAL_LINES = 59;
final static int TEXT_SELECT_CAR	= 60;
final static int TEXT_EXIT_CONFIRMATION = 61;
final static int TEXT_YES 		= 62;
final static int TEXT_NO 		= 63;
//#ifdef TOCA
final static int TEXT_SERIES_1		= 64;
final static int TEXT_SERIES_2		= 65;
final static int TEXT_CHAMPION		= 66;
//#ifdef LEGAL_TWO_SCREENS
//#endif
//#else
//#ifdef LEGAL_TWO_SCREENS
//#endif
//#endif

// -------------------
// textos Create
// ===================

String[][] textosCreate(byte[] tex) {
	char textos[];
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;
	int size = 0;

	String tmpstr = null;

	tmpstr = new String(tex);
	textos = tmpstr.toCharArray();
	tmpstr = null;
	System.gc();


	for (int i=0 ; i<textos.length ; i++) {
		if (campo) {
			if (textos[i] == 0x7D) {
				subCampo = false;
				campo = true;
			}

			if ((textos[i] < 0x20 && textos[i] >= 0) || textos[i] == 0x7C || textos[i] == 0x7D)	// Buscamos cuando Termina un campo
			{
				data[dataPos+1] = (short) (i - data[dataPos]);

				dataPos+=2;

				campo=false;
			}

		} else {

			if (textos[i] == 0x7D) {
				subCampo = false;
				continue;
			}

			if (textos[i] == 0x7B) {
				dataBak = dataPos;
				data[dataPos++] = 0;
				campo = false;
				subCampo = true;
				size++;
				continue;
			}

			if (subCampo && textos[i] == 0x0A) {
				if (primerEnter) {
					primerEnter = false;
				} else {
					data[dataPos++] = (short) i;
					data[dataPos++] = 1;
					if (!subCampo) {
						size++;
					} else {
						data[dataBak]--;
					}
				}
				continue;
			}

			if (textos[i] >= 0x20)	// Buscamos cuando Empieza un campo nuevo
			{
				campo=true;
				data[dataPos] = (short) i;
				if (!subCampo) {
					size++;
				} else {
					data[dataBak]--;
				}
				primerEnter = true;
			}
		}
	}

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++) {
		int num = data[dataPos];

		if (num<0) {
			num*=-1;
			dataPos++;
		} else {
			num = 1;
		}

		strings[i] = new String[num];

		for (int t=0 ; t<num; t++)
		{
		strings[i][t] = new String(textos, data[dataPos++], data[dataPos++]);
		if (strings[i][t].length() < 2) {strings[i][t] = " ";}
		}
	}

	return strings;
}

// <=- <=- <=- <=- <=-



// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// *******************
// -------------------
// keyboard - Engine
// ===================
// *******************

int keyMenu, keyX, keyY, keyMisc, keyCode;
int lastKeyMenu, lastKeyX, lastKeyY, lastKeyMisc, lastKeyCode;

// -------------------
// keyboard Tick
// ===================

public void keyboardTick()
{
	lastKeyMenu = keyMenu;	// Keys del Frame Anterior
	lastKeyX = keyX;
	lastKeyY = keyY;
	lastKeyMisc = keyMisc;
	lastKeyCode = keyCode;

	keyMenu = gc.keyMenu;	// Keys del Frame Actual
	keyX = gc.keyX;
	keyY = gc.keyY;
	keyMisc = gc.keyMisc;
	keyCode = gc.keyCode;
	
	//#ifdef LISTENER
	//#endif

}

// <=- <=- <=- <=- <=-


// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------









// *******************
// -------------------
// bios - Engine
// ===================
// *******************

int biosStatus=0;

boolean gameSound = true;
boolean gameVibra = true;

// -------------------
// bios Create
// ===================

String[][] gameText;

public void biosCreate()
{
	//#ifdef GERMAN
	//#elifdef ENGLISH
	//#else
	gameText = textosCreate( loadFile("/Textos.txt") );
	//#endif

	gameCreate();
}

// -------------------
// bios Init
// ===================

public void biosInit()
{
	loadPrefs();
}

// -------------------
// bios Destroy
// ===================

public void biosDestroy()
{
	saveSlots();
	savePrefs();

	gameDestroy();
}

// -------------------
// bios Tick
// ===================

public void biosTick()
{
	
	//#ifdef MO
	//lastFrameTime = System.currentTimeMillis();
	//#endif
	switch (biosStatus)
	{
// --------------------
// game Bucle
// --------------------
	case 0:
		gameTick();
	return;
// --------------------

// --------------------
// logos Bucle
// --------------------
	case 11:
		if ( logosTick() ) {biosStatus = 0;}
	return;
// --------------------

// --------------------
// menu Bucle
// --------------------
	case 22:
		if ( menuTick() ) {biosStatus = 0;}
	return;
// --------------------
	}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// logos - Engine
// ===================
// *******************

int numLogos = 0;
int cntLogos = 0;
int[] rgbLogos;
int timeLogos;
long timeIniLogos;

public void logosInit(int num, int[] rgb, int time)
{
	numLogos = num;
	cntLogos = 0;
	rgbLogos = rgb;
	timeLogos = time;

	timeIniLogos = System.currentTimeMillis() - timeLogos;

	menuShow = false;
	biosStatus = 11;
}


public boolean logosTick()
{
	if ( (System.currentTimeMillis() - timeIniLogos) < timeLogos) {return false;}

	if (cntLogos == numLogos) {menuShow = true; return true;}

	gc.canvasFillInit(rgbLogos[cntLogos]);

//#ifdef J2ME
	gc.canvasImg = gc.loadImage("/Logo"+cntLogos+".png");
//#elifdef DOJA
//#endif

	cntLogos++;

	timeIniLogos = System.currentTimeMillis();

	gc.forcePause = false;
	//System.out.println("drawin' logos");
	return false;
}

// <=- <=- <=- <=- <=-















// *******************
// -------------------
// menu - Engine
// ===================
// *******************

boolean menuShow;
boolean menuExit;

Image menuImg;

// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if(menuIt < 100) menuIt++;
	//#ifdef MENU_COOL
	menuScr++;
	//#endif
	if ( (menuListMode == ML_SCROLL || menuListMode == ML_SCREEN) && (keyMenu == -1 || keyMenu == 2) && keyMenu != lastKeyMenu)
	{
		menuAction(-2);
	}
	else
	if ( menuListTick( (keyY!=lastKeyY? keyY:(keyX!=lastKeyX? keyX:0) ), keyMenu!=lastKeyMenu? keyMenu>0:false ) )
	{
	menuAction(menuListCMD);
	}
	gc.forcePause = false;
	if (menuExit) {return true;}
	//menuShow = true;
	//System.out.println("drawin menu");
	return false;
}

// <=- <=- <=- <=- <=-














// *******************
// -------------------
// menuList - Engine - Rev.0 (20.6.2003)
// ===================
// *******************

boolean menuList_ON=false;		// Estado del menuList Engine
boolean menuListShow=false;

int menuListMode;

int menuListPos;
int menuListCMD;

int menuListX;
int menuListY;
int menuListSizeX;
int menuListSizeY;

int menuListScrY;
int menuListScrSizeY;

int menuListBloIni;
int menuListBloSize;

Font menuListFont;

String	menuListStr[][];
int		menuListDat[][];

static final int ML_TEXT = 1;
static final int ML_SCROLL = 2;
static final int ML_OPTION = 3;
static final int ML_SCREEN = 4;

// -------------------
// menuList Init
// ===================

public void menuListInit(int x, int y, int sizeX, int sizeY)
{
	menuListX = x;
	menuListY = y;
	menuListSizeX = sizeX;
	menuListSizeY = sizeY;
}

// -------------------
// menuList Clear
// ===================

public void menuListClear()
{
	menuList_ON=false;
	menuListShow=false;

	menuListStr=null;
	menuListDat=null;

	menuListPos=0;
}

// -------------------
// menuList Add
// ===================

public void menuListAdd(int Dato, String Texto)
{
	Font f = menuListGetFont(Dato);

	if ( f.stringWidth(Texto) <= menuListSizeX)
	{
	menuListAdd(Dato, new String[] {Texto}, 0, 0);
	} else {

	String[] Textos = menuListCutText(Dato, Texto);

		for (int i=0 ; i<Textos.length ; i++)
		{
		menuListAdd(Dato, new String[] {Textos[i]}, 0, 0);
		}
	}
}


public void menuListAdd(int Dato, String[] Texto)
{
	for (int i=0 ; i<Texto.length ; i++)
	{
	menuListAdd(Dato, Texto[i]);
	}
}


public void menuListAdd(int Dato, String Texto, int Dat1)
{
	menuListAdd(Dato, new String[] {Texto}, Dat1, 0);
}

public void menuListAdd(int Dato, String[] Texto, int Dat1)
{
	menuListAdd(Dato, new String[] {Texto[0]}, Dat1, 0);
}

public void menuListAdd(int Dat0, String[] Texto, int Dat1, int Dat2)
{
	int Lines=(menuListDat!=null)?menuListDat.length:0;

	String Str[][] = new String[Lines+1][];
	int Dat[][] = new int[Lines+1][];

	int i=0;

	for (; i<Lines; i++)
	{
	Dat[i] = menuListDat[i];
	Str[i] = menuListStr[i];
	}

	Font f = menuListGetFont(Dat0);

	int TextoSizeX = 8;
	for (int SizeX, t=0 ; t<Texto.length ; t++) {SizeX=f.stringWidth(Texto[t]); if ( TextoSizeX < SizeX ) {TextoSizeX=SizeX;}}


	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2, TextoSizeX, f.getHeight() };

	menuListStr=Str;
	menuListDat=Dat;
}












public Font menuListGetFont(int Bits)
{
//#ifdef J2ME
	//#ifdef MENU_SMALL_FONT
	//#else
	return Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
	//#endif
//#elifdef DOJA
//#endif
}


// -------------------
// menuList Set
// ===================

public void menuListSet_Text()
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_TEXT;
}

public void menuListSet_Scroll()
{
	menuListScrY = menuListSizeY;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCROLL;


	menuListScrSizeY = 0; for (int i=0 ; i<menuListDat.length ; i++) {menuListScrSizeY += menuListDat[i][4];}

}

public void menuListSet_Option()
{
	menuListSet_Option(0);
}

public void menuListSet_Option(int Pos)
{
	menuListPos=Pos;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_OPTION;
}

//#ifdef NK-s40
//#else
public void menuListSet_Screen()
//#endif
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCREEN;



	menuListBloIni = 0;
	menuListBloSize = 0;

	int sizeY = 0;

	for (int i=0 ; i<menuListDat.length ; i++)
	{
	if ( (sizeY += menuListDat[i][4]) < menuListSizeY) {menuListBloSize++;} else {break;}
	}
	
	//#ifdef NK-s40
	//menuListBloSize += hack;
	//#endif

}



// -------------------
// menuList Tick
// ===================

public boolean menuListTick(int movY, boolean fire)
{
	if (menuListMode == ML_OPTION)
	{
		if (movY ==-1 && menuListPos > 0) {menuListPos--; menuShow = true;menuListShow=true;}
		else
		if (movY == 1 && menuListPos < menuListDat.length-1) {menuListPos++; menuListShow=true;menuShow = true;}
		else
		if (movY ==-1 && menuListPos == 0) {menuListPos = menuListDat.length-1; menuListShow=true;menuShow = true;}
		else
		if (movY == 1 && menuListPos == menuListDat.length-1) {menuListPos = 0; menuListShow=true;menuShow = true;}

		if (fire)
		{
		if (++menuListDat[menuListPos][2] == menuListStr[menuListPos].length) {menuListDat[menuListPos][2]=0;}
		menuListCMD=menuListDat[menuListPos][1];
		menuListShow=true;
		menuShow = true;
		return true;
		}
	}


	if (menuListMode == ML_SCROLL)
	{
		menuListScrY--;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}

		menuListShow=true;
	}




	if (menuListMode == ML_SCREEN)
	{
		if (fire || movY != 0)
		{
			menuListBloIni += menuListBloSize;
			menuListBloSize = 0;

			int sizeY = 0;

			while (menuListBloIni < menuListDat.length && menuListStr[menuListBloIni][0] == " ") {menuListBloIni++;}

			for (int i=menuListBloIni ; i<menuListDat.length ; i++)
			{
			if ( (sizeY += menuListDat[i][4]) < menuListSizeY) {menuListBloSize++;} else {break;}
			}

			int fin = (menuListBloIni + menuListBloSize)-1;

			while (fin > menuListBloIni && menuListStr[fin--][0] == " ") {menuListBloSize--;}


			if (menuListBloSize == 0) {menuListCMD=-2; return true;}

			menuListShow=true;
		}
	}


	return false;
}


// -------------------
// menuList Opt
// ===================

public int menuListOpt()
{
	return(menuListDat[menuListPos][2]);
}


// -------------------
// menuList Draw
// ===================
/*
public void menuListDraw(Graphics g)
{
	//if (!menuList_ON) {return;}
	if (!menuList_ON || !menuListShow) {return;}

	menuListShow = false;

	if (menuImg!=null) {gc.showImage(menuImg);}

//#ifdef J2ME
  //#ifdef NK-s40
    menuListFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL );
	//#else
		menuListFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
  //#endif
//#elifdef DOJA
		menuListFont = Font.getFont( Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_MEDIUM );
//#endif

		g.setFont(menuListFont);


	switch (menuListMode)
	{
	case ML_OPTION:

		String[] texto = menuListCutText(0, menuListStr[menuListPos][menuListDat[menuListPos][2]]);

		int height = (texto.length * menuListDat[menuListPos][4]);
    //int y = menuListY + (( menuListSizeY / 4) * 3) - (height/2);
		int y = menuListY + (( menuListSizeY / 6) * 5);
		if (y + height > menuListSizeY) {y = menuListY + (menuListSizeY-height);}

		for (int i=0 ; i<texto.length ; i++)
		{
			menuListTextDraw(g, 0, texto[i], 0,y);

			y += menuListDat[menuListPos][4];
		}
	break;


	case ML_SCROLL:

		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			menuListTextDraw(g, 0, menuListStr[i][0], 0,y);

			y += menuListDat[i][4];
		}
	break;


	case ML_SCREEN:

		y = 0;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			y += menuListDat[i][4];
		}

		y = (menuListSizeY - y) / 2;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			menuListTextDraw(g, 0, menuListStr[i][0], 0,y);

			y += menuListDat[i][4];

		}


	break;

	}
}
*/
public void menuListDraw(Graphics g)
{
	if(menuExit) {return;}
	//if (!menuList_ON || !menuListShow) {return;}

	//menuListShow = false;

	g.setClip(0,0,gc.canvasWidth,gc.canvasHeight);
	
	if(menuType == MENU_MAIN) {
		if(menuBackground != null) {
			gc.canvasFill(0xffffff);
			gc.showImage(menuBackground);
		}
		
		if(arrow != null) gc.showImage(arrow, gc.canvasWidth - arrow.getWidth() - 2, gc.canvasHeight - arrow.getHeight() - 2 - (5 - arrowPos));
	} else {
		//#ifndef NO_MENU_BACKGROUND
		//#else
		gc.canvasFill(0x3a7998);
		//#endif
	}
	
	
	
	//#ifndef SEMI_FULL_FEATURED
	mdisp++;
	if(mdisp > 15) mdisp = 0;
	//#endif

	menuListFont = menuListGetFont(0);
	g.setFont(menuListFont);
	
	switch (menuListMode)
	{
	case ML_OPTION:
		int ht = (menuListStr.length * menuListFont.getHeight() );
		int addy = (gc.canvasHeight - ht) / (menuListStr.length + 1);		//toma hack
		int y = addy;
		//int y = menuListY + ((menuListSizeY - height) / 2);

		if(selectingCar == 1) {
			//#ifndef TEXT_SELECT_CAR
			//#ifdef PRELOAD_CARS
			if(carImages == null) {
				carImages = new Image[menuListStr.length];
				for(int i = 0; i < carImages.length; i++) {
					try {
						int p = i + 1;
						carImages[i] = new Image();
						carImages[i]._createImage("/car_" + p + ".png");
					} catch(Exception e) {}
				}
			}
			//#endif
			//#endif
			
			if(menuListPos != lastmenupos) {
				//#ifndef TEXT_SELECT_CAR
				slCar = null;
				//#endif
				lastmenupos = menuListPos;
			}
			//#ifndef TEXT_SELECT_CAR
				//#ifndef PRELOAD_CARS
				//#else
				slCar = carImages[menuListPos];
				//#endif
			
			if (slCar != null)
				gc.showImageDispl(slCar,0,5);
				//gc.showImage(slCar);
			menuListTextDraw(g, 0, menuListStr[menuListPos][menuListDat[menuListPos][2]], 0, gc.canvasHeight - menuListFont.getHeight() - 8);
			//#else
			//#endif
		} else {
			//#ifndef TEXT_SELECT_CAR
			if(slCar != null){ 
				slCar = null;
				//#ifdef PRELOAD_CARS
				//carImages = null;			//avoid reloading them!! they are always in memory!!!
				//#endif
				System.gc();
			}
			//#endif
			
			if(menuType == MENU_MAIN) {
				if(menuListPos != lastmenupos) {
					menuIt = 0;
					lastmenupos = menuListPos;
				}
				String str = menuListStr[menuListPos][menuListDat[menuListPos][2]];
				str = "> " + str + " <";
				y = gc.canvasHeight - ((gc.canvasHeight / 3) >> 1)  - (menuListFont.getHeight() >> 1);
				//#ifdef MENU_COOL
				if(menuScroll0 != null) {
					gc.showImage(menuScroll0, (menuScr * 4) % gc.canvasWidth, y - (menuScroll0.getHeight() >> 1));
					gc.showImage(menuScroll0, (menuScr * 4) % gc.canvasWidth - gc.canvasWidth, y - (menuScroll0.getHeight() >> 1));
					gc.showImage(menuScroll1, (gc.canvasWidth / 7 + (menuScr * 3)) % gc.canvasWidth, y);
					gc.showImage(menuScroll1, (gc.canvasWidth / 7 + (menuScr * 3)) % gc.canvasWidth - gc.canvasWidth, y);
					gc.showImage(menuScroll2, (gc.canvasWidth / 3 + (menuScr * 2)) % gc.canvasWidth, y + (menuScroll2.getHeight() >> 1));
					gc.showImage(menuScroll2, (gc.canvasWidth / 3 + (menuScr * 2)) % gc.canvasWidth - gc.canvasWidth, y + (menuScroll2.getHeight() >> 1));
				}
				//#endif
				menuListTextDraw(g, 0, str, 0, y, false);
			} else {
				for (int i=0 ; i<menuListStr.length ; i++)
				{
					String str = menuListStr[i][menuListDat[i][2]];
		
					if (i == menuListPos) {str = "> " + str + " <";}
		
					menuListTextDraw(g, 0, str, 0, y, i == menuListPos);
		
					//System.out.println("drawing text at: " + y);
					y += addy;
					y += menuListFont.getHeight();
				}
			}
			
			
			//#ifdef SEMI_FULL_FEATURED
			//#endif
		}

	break;

	case ML_SCROLL:

		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			menuListTextDraw(g, 0, menuListStr[i][0], 0,y);

			y += menuListDat[i][4];
		}
	break;


	case ML_SCREEN:

		y = 0;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			y += menuListDat[i][4];
		}

		y = (menuListSizeY - y) / 2;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			menuListTextDraw(g, 0, menuListStr[i][0], 0,y);
			y += menuListDat[i][4];

		}
		
		//#ifdef SEMI_FULL_FEATURED
		//#endif


	break;

	}
	//#ifdef MO
  	//playShow = true;
	//#endif
}






public void menuListTextDraw(Graphics g, int format, String str, int x, int y) {
	menuListTextDraw(g,format,str,x,y,false);
}

public void menuListTextDraw(Graphics g, int format, String str, int x, int y, boolean selected)
{
	x = menuListX + (( menuListSizeX - menuListFont.stringWidth(str) ) / 2);
//#ifdef J2ME
g.setClip(0,0, gc.canvasWidth, gc.canvasHeight);

//#ifdef FULL_FEATURED
	//#ifdef NO_MENU_SCROLL
		g.setColor(menuShadow[5]);
		g.drawString(str, x-1,y-1, 20);
		g.drawString(str, x+1,y-1, 20);
		g.drawString(str, x-1,y+1, 20);
		g.drawString(str, x+1,y+1, 20);
	//#else
		//#ifndef SEMI_FULL_FEATURED
			if(menuIt > 4) {
				menuIt = 5;
			}
			
			if(menuType == MENU_MAIN) {
				g.setColor(mainMenuShadow[menuIt]);
				g.drawString(str, x-1, y-1 + (5 - menuIt) * 2, 20);
				g.drawString(str, x+1, y-1 + (5 - menuIt) * 2, 20);
				g.drawString(str, x-1, y+1 + (5 - menuIt) * 2, 20);
				g.drawString(str, x+1, y+1 + (5 - menuIt) * 2, 20);
			} else {
				g.setColor(menuShadow[menuIt]);
				g.drawString(str, x-1- (5 - menuIt) * 2,y-1, 20);
				g.drawString(str, x+1- (5 - menuIt) * 2,y-1, 20);
				g.drawString(str, x-1- (5 - menuIt) * 2,y+1, 20);
				g.drawString(str, x+1- (5 - menuIt) * 2,y+1, 20);
			}
		//#else
		//#endif
	//#endif
//#else
//#endif
	

	if(selected) {
		//#ifdef FULL_FEATURED
			//#ifdef NO_MENU_SCROLL
			//#else
				//#ifndef SEMI_FULL_FEATURED
				g.setColor(menuSelCols[menuIt]);
				//#else
				//#endif
			//#endif
		//#else
		//#endif
	} else {
		//#ifdef FULL_FEATURED
			//#ifdef NO_MENU_SCROLL
			//#else
				//#ifndef SEMI_FULL_FEATURED
				if(menuType == MENU_MAIN) {
					g.setColor(menuCols[5]);
				} else{ 
					g.setColor(menuCols[menuIt]);
				}
				//#else
				//#endif
			//#endif
		//#else
		//#endif
	}
	
	//#ifdef FULL_FEATURED
		//#ifdef NO_MENU_SCROLL
		//#else
			//#ifndef SEMI_FULL_FEATURED
			if(menuType == MENU_MAIN) {
				g.drawString(str, x,y + (5 - menuIt) * 2, 20);
			} else {
				g.drawString(str, x - (5 - menuIt) * 2,y, 20);
			}
			//#else
			//#endif
		//#endif
	//#else
	//#endif

//#elifdef DOJA
//#endif

}







public String[] menuListCutText(int Dato, String Texto)
{
	String[] str = new String[0];

	int Pos=0, PosIni=0, PosOld=0, Size=0;

	Font f = menuListGetFont(Dato);

	while ( PosOld < Texto.length() )
	{
	Size=0;

	Pos=PosIni;

	while ( Size < (menuListSizeX) )
	{
		if ( Pos == Texto.length() ) {PosOld=Pos; break;}

		int Dat = Texto.charAt(Pos++);
		if (Dat==0x20) {PosOld=Pos-1;}

		Size += f.charWidth((char)Dat);
	}

	if (PosOld-PosIni < 1) { while ( Pos < Texto.length() && Texto.charAt(Pos) >= 0x30 ) {Pos++;} PosOld=Pos; }



	int Lines=(str!=null)?str.length:0;

	String newStr[] = new String[Lines+1];

	int i=0;

	for (; i<Lines; i++)
	{
	newStr[i] = str[i];
	}

	str = newStr;

	newStr = null;

	str[i] = Texto.substring(PosIni,PosOld);

	PosIni=PosOld+1;

	}

	return str;
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// wait - Engine
// ===================
// *******************

long waitMillis;

// -------------------
// wait Start - Guardamos Tiempo ACTUAL para posteriores usos...
// ===================

public void waitStart()
{
	waitMillis = System.currentTimeMillis();
}

// -------------------
// wait Stop - Hacemos una espera tomando como tiempo inicial cuando se llamo a "waitStart()"
// ===================

public void waitStop(int time)
{
	int wait = time - ((int)(System.currentTimeMillis() - waitMillis));
	if (wait > 0) {waitTime(wait);}
}

// -------------------
// wait Finished - Devuelve "true" si ha pasado el tiempo especificado desde que se llamo a "waitStart()"
// ===================

public boolean waitFinished(int time)
{
	return (time - ((int)(System.currentTimeMillis() - waitMillis))) <= 0;
}

// -------------------
// wait Time - Hacemos una espera del tiempo especificado
// ===================

public void waitTime(int time)
{
	try {
		Thread.sleep(time);
	} catch (Exception e) {}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

// -------------------
// prefs Update - Leemos/Salvamos archivo de preferencias, resultado: byte[] que grabamos la ultima vez o "null" si NO Existe
// ===================

// Leemos cuando le pasamos "null" como paremetro.
// Salvamos cuando le pasamos "byte[]" como parametro.

public byte[] updatePrefs(byte[] bufer)
{
//#ifdef J2ME

	RecordStore rs;

	try {

		rs = RecordStore.openRecordStore("prefs", true);

		if (bufer != null && bufer.length > 0)
		{
			if (rs.getNumRecords() == 0)
			{
				rs.addRecord(bufer, 0, bufer.length);
			} else {
				rs.setRecord(1, bufer, 0, bufer.length);
			}
		} else {
			if (rs.getNumRecords() == 0)
			{
			bufer = null;
			} else {
			bufer = rs.getRecord(1);
			}
		}

		rs.closeRecordStore();

	} catch(Exception e) {return null;}

	return bufer;

//#elifdef DOJA
//#endif
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// inputDialog - Engine
// ===================
// *******************

//#ifdef J2ME

public void inputDialogInit() {
	oldbs = biosStatus;
	tbOnScreen = true;
	tbPlayerName = null;
	System.gc();
	
	playerName = gameText[TEXT_PLAYER_NAME][0];	//"Player";
	currentTrack = 0;
	currentTrackHistory = 0;
	//#ifdef SHORT_TEXT_BOX
	//#else
	tbPlayerName = new TextBox(gameText[TEXT_ENTER_NAME][0], playerName, 15, TextField.ANY);
	//#endif
	tbPlayerName.addCommand(new Command(gameText[TEXT_CONTINUE][0], Command.OK, 1));
	tbPlayerName.setCommandListener(this);
	
	biosStatus = 10000;	// hack
	//Display.getDisplay(this).setCurrent(tbPlayerName);

	//MONGOFIX
	commandAction(null, null);
}

public void commandAction (Command c, Displayable d) {
	tbOnScreen = false;
	menuInit(MENU_SELECT_CAR);
	//System.out.println("aftah selecting car!!");
	
	playerName = tbPlayerName.getString().replace('\n', ' ');
	saveGames[saveSlot] = playerName;
	savePos[saveSlot] = (byte) currentTrackHistory;
	saveSlots();
		
	biosStatus = oldbs;
	Display.getDisplay(this).setCurrent(gc);
}

//public void inputDialogNotify(String s){}

//#elifdef DOJA

//#endif

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
// Final Clase Bios
// **************************************************************************//
//}; // Clase Bios integrada en la Game

// -----------------------------------------------------------------------------------------------------------------------

// **************************************************************************//
// Inicio Clase Game
// **************************************************************************//

// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// ===========================================


final static int TEXT_LEVEL = 13;
final static int TEXT_CONGRATULATIONS     = 14;


// *******************
// -------------------
// game - Engine
// ===================
// *******************


final static int GAME_LOGOS = 0;
final static int GAME_LEGAL_TEXT = 1;
final static int GAME_LEGAL_TEXT2 = 2;
final static int GAME_LEGAL_TEXT3 = 3;
final static int GAME_COVER = 5;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
final static int GAME_MENU_END_TRACK = 40;
final static int GAME_PLAY = 20;


int gameStatus = 0;

// -------------------
// game Create
// ===================

public void gameCreate()
{
}

// -------------------
// game Destroy
// ===================

public void gameDestroy()
{
}

// -------------------
// game Tick
// ===================

public void gameTick()
{
	//#ifdef s60
	/*if(gc != null) {
		if(gc.forcePause) {
			if((gameStatus == GAME_PLAY || gameStatus == GAME_PLAY + 1 || gameStatus == GAME_PLAY + 2)) {
				gameStatus = GAME_MENU_SECOND;
			}
			
			if(gameStatus > GAME_COVER) {
				gameSound = false;
				currentSound = -1;
				gc.soundStop();
				//TODO: hackejar el main menu pq surti be :U_
			}
			
			gc.forcePause = false;
		}
	}*/
	//#endif
	
	switch (gameStatus)
	{
	case GAME_LOGOS:
		logosInit(2, new int[] {0xffffff,0xffffff,0xffffff}, 2000);
		gameStatus = GAME_LEGAL_TEXT;
	break;	
	
	case GAME_LEGAL_TEXT:
		gc.canvasFillInit(0xffffff);
		//gc.canvasTextInit(gameText[TEXT_SHORT_LEGAL_LINES], 0,10, 0x000000, gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_VCENTER);
		//gc.canvasTextInit(gameText[TEXT_SHORT_LEGAL_LINES], 0,0, 0x000000, gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_VCENTER);
		gc.canvasTextInit(gameText[TEXT_SHORT_LEGAL_LINES][0], 0,0, 0x000000, gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_VCENTER);
		gc.gameDraw();
		
		waitTime(4000);
		//#ifdef LEGAL_TWO_SCREENS
		gameStatus = GAME_LEGAL_TEXT2;
		//#else
		gameStatus = GAME_COVER;
		//#endif
	break;
	
	//#ifdef LEGAL_TWO_SCREENS
	//#endif
	//#ifdef LEGAL_THREE_SCREENS
	//#endif
	
	case GAME_COVER:
		gc.canvasFillInit(rgbLogos[cntLogos]);
		gc.canvasImg = gc.loadImage("/Logo2.png");
		gc.gameDraw();
		
		waitTime(timeLogos);
		gameStatus = GAME_MENU_MAIN;
	break;
	
	case GAME_MENU_MAIN:
//#ifdef J2ME
//#ifndef NO_MENU_BACKGROUND
		menuImg = gc.loadImage("/background.png");
//#endif
//#elifdef DOJA
//#endif
		menuInit(MENU_MAIN);
		gameStatus = GAME_PLAY;
	break;


	case GAME_PLAY:
		playCreate();
		gameStatus++;

	case GAME_PLAY+1:
		playInit();
		playExit=0;
		gameStatus++;
	break;

	case GAME_PLAY+2:
		if (keyMenu == -1 && lastKeyMenu != -1) {
			gameStatus = GAME_MENU_SECOND; 
			break;
		}

		if ( !playTick() ) {break;}

		playRelease();

		gameStatus--;

		switch (playExit)
		{
		case 1:	// Pasar de Nivel
		break;

		case 2:	// Una vida menos
		break;

		case 3:	// Producir Game Over

			playDestroy();

			gameStatus = GAME_MENU_GAMEOVER;
		break;
		}

		playExit=0;
	break;

	case GAME_MENU_SECOND:
		menuInit(MENU_SECOND);
		gameStatus = GAME_PLAY+2;
	break;

	case GAME_MENU_GAMEOVER:
		playShow = false;
		
		menuInit(MENU_GAMEOVER);
		gameStatus = GAME_PLAY;
		//gc.canvasFillInit(0x000000);
		//gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
		//gc.gameDraw();

		//waitTime(3000);

		//gameStatus = GAME_MENU_MAIN;
	break;
	
	case GAME_MENU_END_TRACK:
		if(myVehicle.hasCrashed) {
			endPosition = 4;
			menuInit(MENU_SHOW_RESULTS);
		} else {
			menuInit(MENU_RANKING);
		}
	break;
	}
}

// -------------------
// game Refresh
// ===================

/*public void gameRefresh()
{

	switch (gameStatus)
	{
	}

}*/

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

byte[] prefsData;

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{

// Cargamos un byte[] con las ultimas prefs grabadas

	prefsData = updatePrefs(null);	// Recuperamos byte[] almacenado la ultima vez

// Si es null es que nunca se han grabado prefs, por lo cual INICUALIZAMOS las prefs del juego

	if (prefsData == null) {
		prefsData = new byte[] {1,1,0};		// Inicializamos preferencias ya que NO estaban grabadas
	}

// Actualizamos las variables del juego segun las prefs leidas / inicializadas

	gameSound=prefsData[0]!=0;
	gameVibra=prefsData[1]!=0;
	//#ifndef VIBRA_ON
  gameVibra = false;
	//#endif
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{

// Ponemos las varibles del juego a salvar como prefs.

	prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);

// Almacenamos las prefs

	updatePrefs(prefsData);		// Almacenamos byte[]
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// menu - Engine
// ===================
// *******************

static final int MENU_MAIN = 0;
static final int MENU_SECOND = 1;
static final int MENU_SCROLL_HELP = 2;
static final int MENU_SCROLL_ABOUT = 3;
static final int MENU_TIMEATTACK   = 4;
static final int MENU_SELECT_SLOT  = 5;
static final int MENU_SELECT_CAR   = 6;
static final int MENU_SELECT_TRACK = 7;
static final int MENU_NEXT_TRACK   = 8;
static final int MENU_RANKING 	   = 9;
static final int MENU_SHOW_RESULTS = 10;
static final int MENU_BREAK_BEFORE_NEXT_TRACK = 11;
static final int MENU_EDIT_SLOT = 12;
static final int MENU_PRE_NEXT_TRACK = 13;
static final int MENU_SCROLL_HELP_IN_GAME = 14;
static final int MENU_GAMEOVER = 15;
static final int MENU_EXIT_CONFIRMATION = 16;

static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;
static final int MENU_ACTION_PLAY_LOCAL 	=  8;
static final int MENU_ACTION_PLAY_REMOTE 	=  9;
static final int MENU_ACTION_PLAY_REMOTE_DEBUG  = 10;
static final int MENU_ACTION_REMOTE_BEST 	= 11;
static final int MENU_ACTION_LOCAL_BEST		= 12;
static final int MENU_ACTION_2_PLAYERS		= 13;
static final int MENU_ACTION_3_PLAYERS		= 14;
static final int MENU_ACTION_4_PLAYERS		= 15;
static final int MENU_ACTION_TIMEATTACK		= 16;
static final int MENU_ACTION_TRACK1		= 17;
static final int MENU_ACTION_TRACK2		= 18;
static final int MENU_ACTION_TRACK3		= 19;
static final int MENU_ACTION_TRACK4		= 20;
static final int MENU_ACTION_TRACK5		= 21;
static final int MENU_ACTION_TRACK6		= 22;
static final int MENU_ACTION_CAR1		= 23;
static final int MENU_ACTION_CAR2		= 24;
static final int MENU_ACTION_SELECT_SLOT	= 25;
static final int MENU_ACTION_SLOT_CHOOSED0	= 26;
static final int MENU_ACTION_SLOT_CHOOSED1	= 27;
static final int MENU_ACTION_SLOT_CHOOSED2	= 28;
static final int MENU_ACTION_SLOT_CHOOSED3	= 29;
static final int MENU_ACTION_PLAY_HISTORY 	= 30;
static final int MENU_ACTION_MAIN_MENU 		= 31;
static final int MENU_ACTION_NEXT_TRACK		= 32;
static final int MENU_ACTION_RESET_SLOT		= 33;
static final int MENU_ACTION_SHOW_IN_GAME_HELP  = 34;
static final int MENU_ACTION_CAR3		= 35;
static final int MENU_ACTION_CAR4		= 36;
static final int MENU_ACTION_CAR5		= 37;
static final int MENU_ACTION_CAR6		= 38;
static final int MENU_ACTION_EXIT_CONFIRMATION  = 39;

int menuType;
int menuTypeBack;
Image menuBackground = null;
Image arrow = null;

int arrowPos = 0;

//#ifdef MENU_COOL
Image menuScroll0 = null;
Image menuScroll1 = null;
Image menuScroll2 = null;
int menuScr = 0;
//#endif


public String tstamp2String(long time) {
	if(time == 0) {
		return "--:--:---";
	}
	
	int mins = (int) (time / 60000);
	int secs = (int) (time /  1000) % 60;
	int millis = (int) (time %  1000);
	
	boolean mins0 = mins < 10;
	boolean secs0 = secs < 10;
	boolean millis0 = millis < 10;
	boolean millis00 = millis < 100;
	
	StringBuffer str = new StringBuffer();
	if(mins0) str.append("0");
	str.append(mins);
	str.append(":");
	if(secs0) str.append("0");
	str.append(secs);
	str.append(":");
	if(millis00) str.append("0");
	if(millis0) str.append("0");
	str.append(millis);
	
	return str.toString();
}

// -------------------
// menu Init
// ==================

public void menuInit(int type)
{
	exitConfirmation = false;
	if(type == MENU_SECOND) {
		menuSecActive = true;
	} else {
		menuSecActive = false;
	}
	//Z1010 - hack here
	menuBackground = null;
	System.gc();
	
	if(type == MENU_MAIN) {
		currentSound = -1;
		lastSound = -100;
	}
	lastmenupos = -1;		//vigilar amb el select car!! potser no rula ara
	playSound(SOUND_MAIN_MENU, 0);
	
	menuTypeBack = menuType;
	menuType = type;
	
	//#ifndef FULL_FEATURED
	menuShow = true;
	//#endif
	//#ifdef SEMI_FULL_FEATURED
	menuShow = true;
	//#endif
	
	menuExit = false;

	
	menuListInit(0, 0, gc.canvasWidth, gc.canvasHeight);

	switch (type)
	{
	case MENU_MAIN:
		try {
			menuBackground = Image.createImage("/Logo2.png");
			//#ifdef MENU_COOL
			if(menuScroll0 == null) {
				menuScroll0 = Image.createImage("/scroll0.png");
				menuScroll1 = Image.createImage("/scroll1.png");
				menuScroll2 = Image.createImage("/scroll2.png");
			}
			//#endif
			if(arrow == null) {
				arrow = Image.createImage("/arrow.png");
			}

		} catch (Exception e) {
			menuBackground = null;
		}
		
		menuListClear();
 		menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY_HISTORY);
 		menuListAdd(0, gameText[TEXT_TIMEATTACK], MENU_ACTION_TIMEATTACK);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_CONFIRMATION);
		menuListSet_Option();
	break;

	case MENU_SECOND:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_IN_GAME_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		//menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;


	case MENU_GAMEOVER:
		releaseTrack();
		gameOver = 1;
		menuListClear();
		menuListAdd(0, gameText[TEXT_GAMEOVER]);
		//#ifdef SEMI_FULL_FEATURED
		hangScreen = true;
		//#endif
		menuListSet_Screen();
		break;
		
	case MENU_EXIT_CONFIRMATION:
		menuListClear();
		mainMenu = 1;
		exitConfirmation = true;
		menuListAdd(0, gameText[TEXT_EXIT_CONFIRMATION]);
		menuListSet_Screen();
	break;
	case MENU_SCROLL_HELP:
		mainMenu = 1;
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		//#ifdef SEMI_FULL_FEATURED
		hangScreen = false;
		//#endif
		menuListSet_Screen();
	break;
	
	case MENU_SCROLL_HELP_IN_GAME:
		mainMenu = 2;
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		//#ifdef SEMI_FULL_FEATURED
		hangScreen = false;
		//#endif
		menuListSet_Screen();
	break;


	case MENU_SCROLL_ABOUT:
		mainMenu = 1;
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		//#ifdef SEMI_FULL_FEATURED
		hangScreen = false;
		//#endif
		menuListSet_Screen();
	break;

	
	case MENU_TIMEATTACK:
		menuListClear();
 		//menuListAdd(0, gameText[TEXT_REMOTE_BEST], MENU_ACTION_REMOTE_BEST);
 		menuListAdd(0, gameText[TEXT_LOCAL_BEST], MENU_ACTION_LOCAL_BEST);
 		menuListAdd(0, gameText[TEXT_2_PLAYERS], MENU_ACTION_2_PLAYERS);
 		menuListAdd(0, gameText[TEXT_3_PLAYERS], MENU_ACTION_3_PLAYERS);
 		menuListAdd(0, gameText[TEXT_4_PLAYERS], MENU_ACTION_4_PLAYERS);
		menuListSet_Option();
	break;
	
	case MENU_SELECT_SLOT:
		menuListClear();
		
		for(int i = 0; i < 4; i ++) {
			menuListAdd(0, " - " + saveGames[i] + " -", MENU_ACTION_SLOT_CHOOSED0 + i);
		}
		
		//menuListAdd(0, gameText[TEXT_RESET_SLOTS], MENU_ACTION_RESET_SLOTS);
		menuListSet_Option();
	break;
	
	case MENU_SELECT_CAR:
		//change!!
		selectingCar = 1;
		lastmenupos = -1;
		menuListClear();
 		
		//#ifndef TOCA
		//#else
		menuListAdd(0, gameText[TEXT_CAR_1], MENU_ACTION_CAR1);
 		menuListAdd(0, gameText[TEXT_CAR_2], MENU_ACTION_CAR2);
		//#ifdef TWO_COLORS
		menuListAdd(0, gameText[TEXT_CAR_1], MENU_ACTION_CAR3);
		menuListAdd(0, gameText[TEXT_CAR_2], MENU_ACTION_CAR4);
		//#endif
		//#endif
		menuListSet_Option();
	break;
	
	case MENU_EDIT_SLOT:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_NEXT_TRACK);
		menuListAdd(0, gameText[TEXT_RESET_SLOT], MENU_ACTION_RESET_SLOT);
		menuListSet_Option();
		break;
		
	case MENU_SELECT_TRACK:
		menuListClear();
		
		menuListAdd(0, gameText[TEXT_TRACK_NAME_1], MENU_ACTION_TRACK1);
		menuListAdd(0, gameText[TEXT_TRACK_NAME_2], MENU_ACTION_TRACK2);
		menuListAdd(0, gameText[TEXT_TRACK_NAME_3], MENU_ACTION_TRACK3);
		//#ifndef THREE_TRACKS
		menuListAdd(0, gameText[TEXT_TRACK_NAME_4], MENU_ACTION_TRACK4);
		menuListAdd(0, gameText[TEXT_TRACK_NAME_5], MENU_ACTION_TRACK5);
		menuListAdd(0, gameText[TEXT_TRACK_NAME_6], MENU_ACTION_TRACK6);
		//#endif
		
		menuListSet_Option();
	break;
	
	case MENU_NEXT_TRACK:
		menuListClear();
		int idText = 0;
		switch (currentTrack) {
			case 0:	idText = TEXT_TRACK_PRE_1; break;
			case 1:	idText = TEXT_TRACK_PRE_2; break;
			case 2:	idText = TEXT_TRACK_PRE_3; break;
			case 3:	idText = TEXT_TRACK_PRE_4; break;
			case 4:	idText = TEXT_TRACK_PRE_5; break;
			case 5:	idText = TEXT_TRACK_PRE_6; break;
		}
		
		String txt = null;
		int zeroIndex = gameText[idText][0].indexOf("0");
		if(zeroIndex >= 0) {
			StringBuffer sb = new StringBuffer();
			sb.append(gameText[idText][0].substring(0, zeroIndex));
			sb.append(playerName);
			sb.append(gameText[idText][0].substring(zeroIndex + 1));
			txt = sb.toString();
		} else {
			txt = gameText[idText][0];
		}
		
		menuListAdd(0, txt);
		nextTrack = 1;
		//#ifdef SEMI_FULL_FEATURED
		//#endif
		menuListSet_Screen();
	break;
	
	case MENU_BREAK_BEFORE_NEXT_TRACK:
		menuListClear();
		if(position == 1) {
			menuListAdd(0, gameText[TEXT_BREAK_BEFORE_0], MENU_ACTION_NEXT_TRACK); //continue
		} else {
			menuListAdd(0, gameText[TEXT_BREAK_BEFORE_4], MENU_ACTION_NEXT_TRACK); //replay
		}
		//menuListAdd(0, gameText[TEXT_BREAK_BEFORE_1], MENU_ACTION_NEXT_TRACK); //upload score // TODO
		menuListAdd(0, gameText[TEXT_BREAK_BEFORE_2], MENU_ACTION_MAIN_MENU); //main menu
		//menuListAdd(0, gameText[TEXT_BREAK_BEFORE_3], MENU_ACTION_EXIT_GAME); //exit
		menuListSet_Option();
	break;
	
	case MENU_SHOW_RESULTS:
		System.out.println("showing results!!");
		showingResults = 1;
		releaseTrack();
		
		menuListClear();
		if(myVehicle.hasCrashed) {
			menuListAdd(0, gameText[TEXT_CRASHED_TRACK]);
		} else {
			switch(endPosition) {
				case 1:
					//#ifndef THREE_TRACKS 
					if(currentTrack == 5) {
					//#else
					//#endif
						menuListAdd(0, gameText[TEXT_CHAMPION]);
					} else {
						menuListAdd(0, gameText[TEXT_WIN_TRACK]);
					}
					break;
				case 2:
					menuListAdd(0, gameText[TEXT_ALMOST_WIN_TRACK]);
					break;
				case 3:
				case 4:
					menuListAdd(0, gameText[TEXT_LOSE_TRACK]);
					break;
				case -1:
					menuListAdd(0, gameText[TEXT_CRASHED_TRACK]);
					break;
			}
		}
		//#ifdef SEMI_FULL_FEATURED
		//#endif
		menuListSet_Screen();
	break;
	
	case MENU_PRE_NEXT_TRACK:
		nextTrack = 0;
		loadCurve(getTrackName(currentTrack));
		
		menuListClear();
		//System.out.println(currentTrack + " :: " + gameText[TEXT_TRACK_NAME_1 + currentTrack]);
		menuListAdd(0, " ");	//empty string :U_
		
		//drawcurve!!
		//System.out.println("Inside pre next track");
		//#ifdef FULL_FEATURED
		loading = 2;
		//#else
		//#endif
		//#ifdef SEMI_FULL_FEATURED
		//#endif
		menuListSet_Screen();
		break;
		
	case MENU_RANKING:
		//gc.soundPlay(0,0);
		
		int[] list = new int[nOppo];
		
		for(int i = 0; i < list.length; i++) {
			boolean isOk = false;
			
			while(!isOk) {
				int c = RND(5);
				
				isOk = true;
				for(int j = 0; j < i && isOk; j++) {
					if(list[j] == c){
						isOk = false;
					}
				}
				list[i] = c;
			}
		}
		menuListClear();
		
		aftahRank = 1;
		boolean playerWr = false;
		int pPos = endPosition - 1;
		int lastT = 0;
		
		//hack of the death reloaded -- hack car positions
		vehiclesByDepth[pPos].usid = myVehicle.usid;
		
		for(int i = 0; i < vehiclesByDepth.length; i++) {
			if(i < pPos) {
				if(vehiclesByDepth[i].usid == vehiclesByDepth[pPos].usid) {
					for(int j = 0; j <= i; j++) {
						vehiclesByDepth[j].usid += 2;
					}
				}
			}
			
			if(i > pPos) {
				if(vehiclesByDepth[i].usid == vehiclesByDepth[pPos].usid) {
					for(int j = i; j < vehiclesByDepth.length; j++) {
						vehiclesByDepth[j].usid -= 2;
					}
				}
			}
		}
		
		int lastUsid = vehiclesByDepth[0].usid;
		
		for(int i = 1; i < vehiclesByDepth.length; i++) {
			int u = vehiclesByDepth[i].usid;
			
			if(lastUsid - u < 1) {
				u = lastUsid - 1;
			}
			
			vehiclesByDepth[i].usid = u;
			lastUsid = u;
		}
		
		//ranking
		for(int i = 0; i < vehiclesByDepth.length; i++) {
			if(i == pPos) {
				//#ifndef SG-E600
				menuListAdd(0, String.valueOf(endPosition) + " - " + playerName);
				menuListAdd(0, tstamp2String(finishTime));
				//#else
				//#endif
				playerWr = true;
			} else {
				int k = i;
				if(playerWr) k--;
				int index = list[k];
				//seleccionar els pilots catxondos si es a partir del 3er circuit
				if(currentTrack > 2) {
					index += 5;
				}
				//#ifndef SG-E600
				menuListAdd(0,String.valueOf(i + 1) + " - " + String.valueOf(gameText[TEXT_RACER00 + index][0]));
				//#endif
				
				if(i < pPos) {
					int udif = vehiclesByDepth[i].usid - vehiclesByDepth[pPos].usid;
					lastT = (int) finishTime - udif * 350 - (RND(35) + 1);
				}
				
				if(i > pPos) {
					int udif = vehiclesByDepth[pPos].usid - vehiclesByDepth[i].usid;
					lastT = (int) (finishTime + udif * 350 + (RND(35) + 1));
				}
				//#ifndef SG-E600
				menuListAdd(0, tstamp2String(lastT));
				//#else
				//#endif
			}
		}
		//#ifdef SEMI_FULL_FEATURED
		//#endif
		
		//#ifdef NK-s40
		//#else
		menuListSet_Screen();
		//#endif
	}
	biosStatus = 22;
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{
	
	keyMenu = 0;
	gc.keyMenu = 0;
	
	menuSecActive = false;
	menuIt = 0;
	menuSel = 0;
	//System.out.println("action " + cmd + "::"+aftahRank+","+showingResults+":"+gameStatus+"pos:"+endPosition);
	switch (cmd)
	{
	case -3: // Scroll ha sido cortado por usario
	case -2: // Scroll ha llegado al final
		if(exitConfirmation) {
			gameExit = true;
			break;
		}

		if(gameOver == 1) {
			gameOver = 0;
			menuInit(MENU_MAIN);
			break;
		}
		
		if(mainMenu == 1) {
			mainMenu = 0;
			menuInit(MENU_MAIN);
			break;
		}
		
		if(mainMenu == 2) {
			mainMenu = 0;
			menuInit(MENU_SECOND);
			break;
		}
		
		if(aftahRank != 0) {
			aftahRank = 0;
			menuInit(MENU_SHOW_RESULTS);
			break;
		}
		
		if(showingResults != 0) {
			showingResults = 0;
			
			//System.out.println("endpos :: " + endPosition);
			if(endPosition == 1) {
				currentTrack++;
				//#ifndef THREE_TRACKS
				if(currentTrack == 6) { 
					currentTrack = 5;
				//#else
				//#endif
					gameStatus = GAME_PLAY;
					menuInit(MENU_MAIN);
					break;
				}
				savePos[saveSlot] = (byte) currentTrack;
				saveSlots();
			}
			gameStatus = GAME_PLAY;
			menuInit(MENU_BREAK_BEFORE_NEXT_TRACK);
			break;
		}
		
		if(nextTrack == 1) {
			nextTrack = 0;
			//System.out.println("changin' to pre_next_track");
			menuInit(MENU_PRE_NEXT_TRACK);
			break;
		}
		
		if(loading == 2) {
			loading = 1;
			menuExit = true;		//humm xungo
			playShow = false;		//hack
			break;
		}
		// HACK!!!!! fall through
		
		//menuInit(menuTypeBack);
		//menuInit(MENU_ACTION_PLAY);
	//break;

	case MENU_ACTION_PLAY:		// Jugar de 0
	case MENU_ACTION_CONTINUE:	// Continuar
		loading = 0;
		menuExit = true;
		currentSound = -1;
		break;
	
	case MENU_ACTION_PLAY_HISTORY:
		nOppo = 3;
		nPhant = 0;
		historyMode = 1;
		ttplayers = 0;
		currPlayer = 0;
		phantom = null;
		menuInit(MENU_SELECT_SLOT);
	break;
	
	case MENU_ACTION_TIMEATTACK:
		historyMode = 0;
		menuInit(MENU_TIMEATTACK);
	break;
	
	case MENU_ACTION_SELECT_SLOT:
		menuInit(MENU_SELECT_SLOT);
		break;

	case MENU_ACTION_SHOW_HELP:		// Controles...
		menuInit(MENU_SCROLL_HELP);
	break;
	
	case MENU_ACTION_SHOW_IN_GAME_HELP:
		menuInit(MENU_SCROLL_HELP_IN_GAME);
	break;
	/*
	case MENU_ACTION_RESET_SLOTS:
		resetGameData();
		menuInit(MENU_SELECT_SLOT);
		break;
		*/
	case MENU_ACTION_MAIN_MENU:
		menuInit(MENU_MAIN);
		break;
		
	case MENU_ACTION_SLOT_CHOOSED0:
		saveSlot = 0;
		playerName = saveGames[0];
		currentTrack = savePos[0];
		selectedCar = saveCar[0];
		
		if(playerName.compareTo("-----") == 0) {
			inputDialogInit();
		} else {
			menuInit(MENU_EDIT_SLOT);
		}
	break;
	
	case MENU_ACTION_SLOT_CHOOSED1:
		saveSlot = 1;
		playerName = saveGames[1];
		currentTrack = savePos[1];
		selectedCar = saveCar[1];
		if(playerName.compareTo("-----") == 0) {
			inputDialogInit();
		} else {
			menuInit(MENU_EDIT_SLOT);
		}
	break;
	
	case MENU_ACTION_SLOT_CHOOSED2:
		saveSlot = 2;
		playerName = saveGames[2];
		currentTrack = savePos[2];
		selectedCar = saveCar[2];
		if(playerName.compareTo("-----") == 0) {
			inputDialogInit();
		} else {
			menuInit(MENU_EDIT_SLOT);
		}
	break;
	
	case MENU_ACTION_SLOT_CHOOSED3:
		saveSlot = 3;
		playerName = saveGames[3];
		currentTrack = savePos[3];
		selectedCar = saveCar[3];
		if(playerName.compareTo("-----") == 0) {
			inputDialogInit();
		} else {
			menuInit(MENU_EDIT_SLOT);
		}
	break;

	case MENU_ACTION_SHOW_ABOUT:	// About...
		menuInit(MENU_SCROLL_ABOUT);
	break;



	case MENU_ACTION_RESTART:	// Restart
		menuExit = true;
		playExit = 3;
	break;

	case MENU_ACTION_EXIT_CONFIRMATION:
		menuInit(MENU_EXIT_CONFIRMATION);
	break;

	case MENU_ACTION_EXIT_GAME:	// Exit Game
		gameExit = true;
	break;	


	case MENU_ACTION_SOUND_CHG:
		gameSound = menuListOpt() != 0;
		if(!gameSound) {
			currentSound = -1;
			gc.soundStop();
		} else {
			playSound(SOUND_MAIN_MENU, 0);
		}
		//if (!gameSound) {gc.soundStop();} else gc.soundPlay(0,0);
	break;


	case MENU_ACTION_VIBRA_CHG:
		gameVibra = menuListOpt() != 0;
		if(gameVibra) {
			gc.vibraInit(250);
		}
		//#ifdef SG-X450
		currentSound = -1;
		playSound(SOUND_MAIN_MENU, 0);
		//#endif
	break;
	
	case MENU_ACTION_REMOTE_BEST:
		nOppo = 0;
		nPhant = 1;
		ttplayers = 1;
		ttpl[0] = 0;
		currPlayer = 0;
		menuInit(MENU_SELECT_CAR);
	break;
	case MENU_ACTION_LOCAL_BEST:		//1 player
		nOppo = 0;
		nPhant = 1;
		ttplayers = 1;
		ttpl[0] = 0;
		currPlayer = 0;
		menuInit(MENU_SELECT_CAR);

	break;
	case MENU_ACTION_2_PLAYERS:
		nOppo = 0;
		nPhant = 1;
		ttplayers = 2;
		ttpl[0] = 0;
		ttpl[1] = 0;
		currPlayer = 0;
		menuInit(MENU_SELECT_CAR);
	
	break;
	case MENU_ACTION_3_PLAYERS:
		nOppo = 0;
		nPhant = 1;
		ttplayers = 3;
		ttpl[0] = 0;
		ttpl[1] = 0;
		ttpl[2] = 0;
		currPlayer = 0;
		menuInit(MENU_SELECT_CAR);
	
	break;
	case MENU_ACTION_4_PLAYERS:
		nOppo = 0;
		nPhant = 1;
		ttplayers = 4;
		ttpl[0] = 0;
		ttpl[1] = 0;
		ttpl[2] = 0;
		ttpl[3] = 0;
		currPlayer = 0;
		menuInit(MENU_SELECT_CAR);
	
	break;
	
	case MENU_ACTION_CAR1:
		selectingCar = 0;
		selectedCar = 0;
		if(historyMode == 1) {
			saveCar[saveSlot] = (byte) selectedCar;
			menuInit(MENU_NEXT_TRACK);
		} else {
			menuInit(MENU_SELECT_TRACK);
		}
	break;
	case MENU_ACTION_CAR2:
		selectingCar = 0;
		selectedCar = 1;
		if(historyMode == 1) {
			saveCar[saveSlot] = (byte) selectedCar;
			menuInit(MENU_NEXT_TRACK);
		} else {
			menuInit(MENU_SELECT_TRACK);
		}
	break;
	case MENU_ACTION_CAR3:
		selectingCar = 0;
		selectedCar = 2;
		if(historyMode == 1) {
			saveCar[saveSlot] = (byte) selectedCar;
			menuInit(MENU_NEXT_TRACK);
		} else {
			menuInit(MENU_SELECT_TRACK);
		}
	break;
	case MENU_ACTION_CAR4:
		selectingCar = 0;
		selectedCar = 3;
		if(historyMode == 1) {
			saveCar[saveSlot] = (byte) selectedCar;
			menuInit(MENU_NEXT_TRACK);
		} else {
			menuInit(MENU_SELECT_TRACK);
		}
	break;
	case MENU_ACTION_CAR5:
		selectingCar = 0;
		selectedCar = 4;
		if(historyMode == 1) {
			saveCar[saveSlot] = (byte) selectedCar;
			menuInit(MENU_NEXT_TRACK);
		} else {
			menuInit(MENU_SELECT_TRACK);
		}
	break;
	case MENU_ACTION_CAR6:
		selectingCar = 0;
		selectedCar = 5;
		if(historyMode == 1) {
			saveCar[saveSlot] = (byte) selectedCar;
			menuInit(MENU_NEXT_TRACK);
		} else {
			menuInit(MENU_SELECT_TRACK);
		}
	break;
	
	case MENU_ACTION_TRACK1:
		currentTrack = 0;
		//startUsid = startU[currentTrack];
		
		//menuInit(MENU_NEXT_TRACK);
		
		switch(historyMode) {
			case 0:
				menuInit(MENU_PRE_NEXT_TRACK);
				break;
			case 1:
				menuInit(MENU_NEXT_TRACK);
				break;
		}
		
	break;
	
	case MENU_ACTION_TRACK2:
		currentTrack = 1;
		switch(historyMode) {
			case 0:
				menuInit(MENU_PRE_NEXT_TRACK);
				break;
			case 1:
				menuInit(MENU_NEXT_TRACK);
				break;
		}
	break;
	
	case MENU_ACTION_TRACK3:
		currentTrack = 2;
		menuInit(MENU_NEXT_TRACK);
		switch(historyMode) {
			case 0:
				menuInit(MENU_PRE_NEXT_TRACK);
				break;
			case 1:
				menuInit(MENU_NEXT_TRACK);
				break;
		}
	break;
	
	case MENU_ACTION_TRACK4:
		currentTrack = 3;
		menuInit(MENU_NEXT_TRACK);
		switch(historyMode) {
			case 0:
				menuInit(MENU_PRE_NEXT_TRACK);
				break;
			case 1:
				menuInit(MENU_NEXT_TRACK);
				break;
		}
	break;
	
	case MENU_ACTION_TRACK5:
		currentTrack = 4;
		menuInit(MENU_NEXT_TRACK);
		switch(historyMode) {
			case 0:
				menuInit(MENU_PRE_NEXT_TRACK);
				break;
			case 1:
				menuInit(MENU_NEXT_TRACK);
				break;
		}
	break;
	
	case MENU_ACTION_TRACK6:
		currentTrack = 5;
		switch(historyMode) {
			case 0:
				menuInit(MENU_PRE_NEXT_TRACK);
				break;
			case 1:
				menuInit(MENU_NEXT_TRACK);
				break;
		}
	break;
	
	case MENU_ACTION_NEXT_TRACK:
		menuInit(MENU_NEXT_TRACK);
	break;
	
	case MENU_ACTION_RESET_SLOT:
		saveGames[saveSlot] = "-----";
		savePos[saveSlot] = 0;
		currentTrack = 0;
		currentTrackHistory = 0;
		menuInit(MENU_SELECT_SLOT);
		//inputDialogInit();
	break;
	}
}
// <=- <=- <=- <=- <=-






// *******************
// -------------------
// play - Engine
// ===================
// *******************
// -------------------

boolean playShow;

int playExit;

// -------------------
// play Create
// ===================

public void playCreate()
{
	gc.playCreate_Gfx();
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	gc.playDestroy_Gfx();
}

// -------------------
// play Init
// ===================

public void playInit()
{
	tocaInit();
	gc.playInit_Gfx();
}

// -------------------
// play Release
// ===================

public void playRelease()
{
	gc.playRelease_Gfx();
}

// -------------------
// play Tick
// ===================

public boolean playTick()
{
	if (playExit!=0) {return true;}

	playShow = true;
	
	tocaTick();

	return false;
}

// <=- <=- <=- <=- <=-




// ------------------------------------------------------------------------------------------------------------------------
// **************************************************************************//
// Final Clase Game
// **************************************************************************//

	String trackLoaded;
	
	//#ifdef J2ME
	void tocaInitTrack(String trackfile) {
	//#elifdef DOJA
	//#endif
		
		System.out.println("Loading track");
		// cache? not atm
		track = null;
		System.gc();
		
		// cache?
		loadTrack(trackfile);
		
		// preprocess
		for (int i = 0; i < track.trackSectionsMap.length; ++i) {
			TrackSection ts = track.trackSectionsMap[i];
			
			if (ts!=null) {
				for (int j = 0; j < ts.patchList.length; j+= Track.INTS_PER_PATCH) {				
						int type = ts.patchList[j+7];
						
						if ((type & Track.LEFT_BIT)!=0) {
							ts.patchList[j] = ts.patchList[j+2] = -1200;
						}
	
						if ((type & Track.RIGHT_BIT)!=0) {
							ts.patchList[j+1] = ts.patchList[j+3] = 1200;
						}
					
						ts.patchList[j] -= 200;
						ts.patchList[j+1] -= 200;
						ts.patchList[j+2] -= 200;
						ts.patchList[j+3] -= 200;
	
						ts.patchList[j] <<= 8;
						ts.patchList[j+1] <<= 8;
						ts.patchList[j+2] <<= 8;
						ts.patchList[j+3] <<= 8;
	
						ts.patchList[j] /= 6;
						ts.patchList[j+1] /= 6;
						ts.patchList[j+2] /= 6;
						ts.patchList[j+3] /= 6;
	
						
						ts.patchList[j+4] <<=16;
						ts.patchList[j+4] /= 140;
						
						ts.patchList[j+5] <<=16;
						ts.patchList[j+5] /=140;
				}
	
				for (int j = 0; j < ts.lineList.length; j+= Track.INTS_PER_LINE) {
						ts.lineList[j] -= 200;
						ts.lineList[j+2] -= 200;
						ts.lineList[j] <<= 8;
						ts.lineList[j+2] <<= 8;
						ts.lineList[j] /= 6;
						ts.lineList[j+2] /= 6;
											
						ts.lineList[j+1] <<= 16;
						ts.lineList[j+3] <<= 16;
						ts.lineList[j+1] /= 140;
						ts.lineList[j+3] /= 140;
				}
			}
		}

		for (int i = 0; i < track.verticalTrackSectionsMap.length; ++i) {
			TrackSection ts = track.verticalTrackSectionsMap[i];
			
			if (ts != null) {				
				// VERTICAL //
				for (int j = 0; j < ts.patchList.length; j+= Track.INTS_PER_PATCH) {				
					ts.patchList[j] -= 200;
					ts.patchList[j+1] -= 200;
					ts.patchList[j+2] -= 200;
					ts.patchList[j+3] -= 200;
	
					ts.patchList[j] <<= 8;
					ts.patchList[j+1] <<= 8;
					ts.patchList[j+2] <<= 8;
					ts.patchList[j+3] <<= 8;
	
					ts.patchList[j] /= 6;
					ts.patchList[j+1] /= 6;
					ts.patchList[j+2] /= 6;
					ts.patchList[j+3] /= 6;
					
					ts.patchList[j+4] -= 140;
					ts.patchList[j+5] -= 140;
					ts.patchList[j+4] >>= 2;					
					ts.patchList[j+5] >>= 2;
				}
	
				for (int j = 0; j < ts.lineList.length; j+= Track.INTS_PER_LINE) {
					ts.lineList[j] -= 200;
					ts.lineList[j+2] -= 200;
					ts.lineList[j] <<= 8;
					ts.lineList[j+2] <<= 8;
					ts.lineList[j] /= 6;
					ts.lineList[j+2] /= 6;
										
					ts.lineList[j+1] -= 140;
					ts.lineList[j+3] -= 140;
					ts.lineList[j+1] >>= 2;
					ts.lineList[j+3] >>= 2;
				}
			}
		}
	}

	//#ifdef J2ME	
	public void loadCurve(String trackName) {
		System.out.println("pre-loading :: " + trackName);
		
		curvepoints = null;
		System.gc();
		try {
			InputStream is = getClass().getResourceAsStream(trackName);
			com.mygdx.mongojocs.midletemu.DataInputStream dis = new com.mygdx.mongojocs.midletemu.DataInputStream (trackName);
			
			int nvert = dis.readInt();
			curvepoints = new int[nvert * 3];
			for (int i = 0; i < nvert; ++i) {
				curvepoints[i * 3 + 0] = dis.readInt();
				curvepoints[i * 3 + 1] = dis.readInt();
				curvepoints[i * 3 + 2] = dis.readByte();
			}
			
			is.close();
			dis = null;
			is = null;
		} catch(Exception e) {System.out.println("exception == " + e.getMessage());}
		System.gc();
	}
	
	public void loadTrack(String trackName) {	
		track = new Track();
		try {
			//InputStream is = getClass().getResourceAsStream(trackName);
			//DataInputStream dis = new DataInputStream(is);
			com.mygdx.mongojocs.midletemu.DataInputStream dis = new com.mygdx.mongojocs.midletemu.DataInputStream(trackName);
	
			//reset();
			//track.reset();
			// read track
			int nvert            = dis.readInt();
			//fis.read();
			for (int i = 0; i < nvert; ++i) {
				int px    = dis.readInt();
				int py    = dis.readInt();
				byte alt  = dis.readByte();
				track.addPoint(px, py, alt);
			}

			// Load TrackSections
			int maxTrackSects = dis.readByte();
			int numTrackSects = dis.readByte();
			
			track.trackSectionsMap = new TrackSection[maxTrackSects+numTrackSects]; // hack
			
			for (int i = 0; i < numTrackSects; ++i) {
				
				int intKey = dis.readByte();
				
				int[] pl = new int[dis.readShort()];
				for (int k = 0; k < pl.length; ++k)		pl[k] = dis.readInt();
				int[] ll = new int[dis.readShort()];
				for (int k = 0; k < ll.length; ++k)		ll[k] = dis.readInt();
				
				TrackSection ts = new TrackSection(pl, ll);
				track.trackSectionsMap[intKey] = ts;
			}
			
			// load section assigs
			int numSec = dis.readInt();
			track.typeSectionArray = new byte[numSec];
			for (int i = 0; i < track.typeSectionArray.length; ++i) {
				track.typeSectionArray[i] = dis.readByte();
			}
			
			int ko = dis.readInt();		// -> Num Cubes!!!!!!!
			System.out.println("----" + ko);
			for(;ko > 0; ko--) {
				dis.readInt();
			}
			
			// Load VertTrackSections
			int maxVertTrackSects = dis.readByte();
			int numVertTrackSects = dis.readByte();

			//System.out.println(" maxVertTrackSects "+maxVertTrackSects+" numVertTrackSects "+numVertTrackSects+"    (mem "+Runtime.getRuntime().freeMemory()+")");
			
			track.verticalTrackSectionsMap = null;
			System.gc();
			track.verticalTrackSectionsMap = new TrackSection[maxVertTrackSects+numVertTrackSects]; // hack
			
			for (int i = 0; i < numVertTrackSects; ++i) {
				
				int intKey = dis.readByte();
				
				int[] pl = new int[dis.readShort()];
				for (int k = 0; k < pl.length; ++k)		pl[k] = dis.readInt();
				int[] ll = new int[dis.readShort()];
				for (int k = 0; k < ll.length; ++k)		ll[k] = dis.readInt();
				
				TrackSection ts = new TrackSection(pl, ll);
				track.verticalTrackSectionsMap[intKey] = ts;
			}
			
			// load vertical section assigs
			int numVertSec = dis.readInt();
			track.verticalTypeSectionArray = new byte[numVertSec];
			for (int i = 0; i < track.verticalTypeSectionArray.length; ++i) {
				track.verticalTypeSectionArray[i] = dis.readByte();
			}
						
			dis.close();
		} catch (Exception e) {	e.printStackTrace(); }
		
		track.calculateLongitudes();
		track.update();
		System.out.println("Updated track.. all ok");				
	}
	//#elifdef DOJA
	//#endif
	
	
	
/*	
	void tocaTick() {
		
		if (keyY < 0)	Vehicle.accStatus = 1;
		else			Vehicle.accStatus = 0;
		
		Vehicle.wheelStatus = keyX; 
		
		Vehicle.tick();
	}
*/
/////////////////////////////////////////////////////////////////////////////////

	public int msLogic, msRender;
	
	public static Game instance;
	
	private long timeStart;
	private Thread myThread;
	
	FwdVehicle		myVehicle;
	FwdAIVehicle	oppoVehicles[];
	
	
	FwdVehicle[]	vehiclesByDepth;
	
	public Random rand = new Random();
	
	boolean bs=false;
	
	boolean canRender = false;
	
	Track		track;
	
	int[] depthLevels = {
//		0, 15, 29, 42, 55, 66, 74, 79, 85  -desaparecen en el horizonte muy rapido (near-ok)
		//0, 15, 29, 42, 55, 68, 81, 92, 102
		 //0, 15, 30, 45, 60, 75, 90, 105, 120
		 7, 21-5, 36-5, 51-5, 66-5, 82-5, 98-5, 114-5, 129-5
	};
	
	
	int fov=40, pitch=0, height=40;
	
	int raceTime=0;
	int endRace = 0;
	int endRaceTime = 0;

	//long lastTimeFrame = 0;
	
	int currentTrackNum;
	
	int[] incAiSpeed = new int[] { 0, 0, 1, 1, 2, 2, 3, 3, 4 };	
	
	void tocaInit() {
		instance = this;
		//loadCollisionRects();
		//gc.soundPlay(1, 0);
		tocaStartRace(currentTrack);
	}
	
	private String getTrackName(int trackNum) {
		String tmp = "";
		
		//#ifdef TOCA
		switch(trackNum) {
			case 0:
				tmp = "/A1-ring.trk";
				break;
			case 1:
				tmp = "/Catalunya.trk";
				break;
			case 2:
				tmp = "/Nurburgring.trk";
				break;
			case 3:
				tmp = "/Kyalami.trk";
				break;
			case 4:
				tmp = "/Road_America.trk";
				break;
			case 5:
				tmp = "/manthorp_park.trk";
				break;
		}
		//#else
		//#endif
		
		return tmp;
	}
	
	final static int NUM_HORIZON_IMAGES = 2;
	final static int NUM_SKIES_IMAGES = 1;
	
	void tocaStartRace(int trackNum) {
		//System.out.println("starting: " + trackNum);
		
		curvepoints = null;
		System.gc();

		//#ifdef J2ME
		tocaInitTrack(getTrackName(trackNum));
		//#elifdef DOJA
		//#endif
		
		currentTrackNum = trackNum;
		
		//#ifndef NK-s40
		//#ifndef MO-V3xx
		//#ifndef MO-V535
		//#ifndef SI-CX65
		//#ifndef SI-C65
		//#ifndef SG-D410
		//#ifndef SG-X450
		//#ifndef SG-E600
		try {
			System.gc();
			gc.trackRender.horizonImage	= Image.createImage("/horizon_"+((trackNum % NUM_HORIZON_IMAGES)+1)+".png");
			System.gc();
			gc.trackRender.skyImage = Image.createImage("/sky_"+((trackNum % NUM_SKIES_IMAGES)+1)+".png");
			System.gc();
		}
		catch (Exception e) {
		}
		//#endif
		//#endif
		//#endif
		//#endif
		//#endif
		//#endif
		//#endif
		//#endif
		
		
		oppoVehicles = new FwdAIVehicle[nOppo];
		
		for(int i = 0; i < nOppo; i++) {
			int ct = 0;
			oppoVehicles[i] = new FwdAIVehicle(null, (i + 1)<<7, 150 + i * 8);
				
			//#ifdef TOCA
			ct = (selectedCar + 2) % 4;
			//#else

			//#endif
			
			oppoVehicles[i].carType = ct;
		}
		
		myVehicle = new FwdVehicle(null);
		myVehicle.track = track;
		
		vehiclesByDepth = new FwdVehicle[oppoVehicles.length+1];
		sortVehicles();
		
		// Set vehicle's tracks
		for (int i = 0; i < oppoVehicles.length; ++i) {
			oppoVehicles[i].track = track;
		}	
		if(nPhant != 0) {
			phantom = new FwdPhantomVehicle(track);
			//#ifdef TOCA
			phantom.carType = (selectedCar + 2) % 4;
			//#else
			//#endif
			
			lastPhantomTick = 0;
		}
		bestTime = 0;
		startRace();
	}
	
	public void startRace() {
		if(historyMode == 1) {
			currentTrackHistory = currentTrack;
		}
		startUsid = startU[currentTrack];
		lapTime = 0;
		raceTime = -4000;
		
		bestTimeBlink = 0;
		lastLapTime = 0;
		endRace = 0;
		endRaceTime = 0;
		myVehicle.vel = 0;
		myVehicle.ang = 0;
		myVehicle.carType = selectedCar;
		aftahRank = 0;
		oldVel = 0;
		timePause = -1;
		/*
		synchronized(tDifMutex) {
			menuStart = -1;
			timeDiff = 0;
		}
		*/
		myVehicle = new FwdVehicle(null);
		myVehicle.track = track;
		myVehicle.subUsid = startUsid;	//startUsid; //:U_
		myVehicle.latDisplace = -1024*3;	// ????
		lapCounter = 0;
		lastLPusid = startUsid - 1;
		myVehicle.updateDirection();
		myVehicle.fpAng = myVehicle.trackAng;
		myVehicle.fpViewAng = myVehicle.trackAng;
		
		myVehicle.damage = 0;
		myVehicle.hasCrashed = false;
		
		if(phantom != null) {
			phantom.resetTracking();
		}		
		
		
		for (int i = 0; i < oppoVehicles.length; ++i) {
			oppoVehicles[i].vel = 0;
			oppoVehicles[i].ang = 0;
			oppoVehicles[i].subUsid = startUsid + (i<<7) + 358;///(i << (7 + 1));
			
			if ((i & 1) == 1)	oppoVehicles[i].latDisplace = -1024*3;
			else			oppoVehicles[i].latDisplace = 1024*3;
			
			oppoVehicles[i].updateDirection();
			oppoVehicles[i].fpAng = oppoVehicles[i].trackAng;
			oppoVehicles[i].fpViewAng = oppoVehicles[i].trackAng;
		}
		
		//lastTimeFrame = System.currentTimeMillis();
		gameTickCounter = 0;
	}
	
	public void sortVehicles() {
		if(endRace != 1) {
			vehiclesByDepth[0] = myVehicle;
			for (int i = 0; i < oppoVehicles.length; ++i) {
				vehiclesByDepth[i+1] = oppoVehicles[i];
			}
			
			for (int i = 0; i < vehiclesByDepth.length; ++i) {
				FwdVehicle cv = vehiclesByDepth[i];
				FwdVehicle cm = cv;
				int im = i;
				for (int j = i+1; j < vehiclesByDepth.length; ++j) {
					// busca el min
					if (cm.subUsid < vehiclesByDepth[j].subUsid) {
						cm = vehiclesByDepth[j];
						im = j;
					}
				}
				vehiclesByDepth[i] = cm;
				vehiclesByDepth[im] = cv;
			}
		}
	}
	
	boolean myVehicleHasCollidedIndeed = false;

	public void calculateCollisions() {
		bs=!bs;
		for (int i = 0; i < vehiclesByDepth.length-1; ++i) {
			if (vehiclesByDepth[i].subUsid - vehiclesByDepth[i+1].subUsid < 256) {
				vehiclesByDepth[i].hintAccelerate = true;
			}

			if (i == 0 && vehiclesByDepth[i].subUsid - vehiclesByDepth[i+1].subUsid < 640) {
				vehiclesByDepth[i].hintAcc2 = true;
			}
			
			if (vehiclesByDepth[i].subUsid - myVehicle.subUsid > 1408) {
				vehiclesByDepth[i].hintBrake = true;
			}
		}
		
		int oxi = myVehicle.curColRectXi;
		int oyi = myVehicle.curColRectYi;
		int oxf = myVehicle.curColRectXf;
		int oyf = myVehicle.curColRectYf;
		
		int ocx = oxi+oxf >> 1;
		int ocy = oyi+oyf >> 1;
		
		myVehicle.isColliding = false;
		for (int i = 0; i < vehiclesByDepth.length; ++i) {

			if (myVehicle == vehiclesByDepth[i])
				continue;
//#ifdef SI-CX65
//#endif			
			vehiclesByDepth[i].isColliding = false;			
//#ifdef SI-CX65
//#endif			
			
			int axi = vehiclesByDepth[i].curColRectXi;
			int axf = vehiclesByDepth[i].curColRectXf;

			if (axi == 0 && axf == 0)
				continue;
			
			int ayi = vehiclesByDepth[i].curColRectYi;
			int ayf = vehiclesByDepth[i].curColRectYf;
			
			boolean isLeftInside = (oxi >= axi && oxi <= axf);
			boolean isRightInside = (oxf >= axi && oxf <= axf); 
			boolean isTopInside = (oyi >= ayi && oyi <= ayf);
			boolean isBottomInside = (oyf >= ayi && oyf <= ayf);
			
			if ((isLeftInside || isRightInside) && (isTopInside || isBottomInside)) {
				// collides
				vehiclesByDepth[i].isColliding = true;
				vehiclesByDepth[i].collideVehicle = myVehicle;
				myVehicle.isColliding = true;
				
				myVehicleHasCollidedIndeed = true;
				
				myVehicle.collideVehicle = vehiclesByDepth[i];				
				
				int acx = axi+axf >> 1;
				int acy = ayi+ayf >> 1;
				
				int dx = acx-ocx;
				int dy = acy-ocy;
				
				if (dx*dx >>1 > dy*dy) {
					// left or right 2 3
					if (dx < 0)		myVehicle.hitPosition = 2;
					else			myVehicle.hitPosition = 3;
				}
				else {
					// front or back  0 1
					if (dy < 0)		myVehicle.hitPosition = 0;
					else			myVehicle.hitPosition = 1;
				}
			}
		}
	}
	
	void releaseTrack() {
		System.out.println("before: " + Runtime.getRuntime().freeMemory());
		//#ifdef NK-s40
		//#endif
		//#ifdef LG-U8150
		//#endif
		track = null;
		curvepoints = null;
		System.gc();
		System.out.println("aftah: " + Runtime.getRuntime().freeMemory());
		
	}

	public void resetCollisions() {
		myVehicle.isColliding = false;		
		for (int i = 0; i < vehiclesByDepth.length; ++i) {
			vehiclesByDepth[i].isColliding = false;
			vehiclesByDepth[i].isColBack = false;			
		}
	}

	//public int getTimeDiff(long currentTimeFrame) {
	public long getTimeDiff() {
		/*
		long tlapsed = (currentTimeFrame - lastTimeFrame);
		synchronized(tDifMutex) {
			if(timeDiff != 0) {
				tlapsed -= timeDiff;
				if(tlapsed < 0) {
					timeDiff = -tlapsed;
					if(timeDiff < 0) timeDiff = 0;
					tlapsed = 0;
				} else {
					timeDiff = 0;
				}
			}
		}
		return (int) tlapsed;
		*/
		return MILLIS_PER_SECOND;	//gameTickCounter * 10;	//10 fps :U_
	}
	
	void tocaTick() {
		gameTickCounter++;
		oldVel = myVehicle.vel;
		if(endRaceTime > 2500) {
			if(ttplayers == 0) {
				gameStatus = GAME_MENU_END_TRACK;
				return;
			} else {
				currPlayer = (currPlayer + 1) % ttplayers;
				startRace();
			}
		}
		
		//long msStart = System.currentTimeMillis();		
		long currentTimeFrame = System.currentTimeMillis(); 
		long deltaTimeFrame = getTimeDiff();
		lapTime += deltaTimeFrame;
		if(endRace == 1) {
			endRaceTime += deltaTimeFrame;
		}
		//#ifdef LOWFREQ_SAMPLING
		//#else
		if((nPhant != 0) && ((currentTimeFrame - lastPhantomTick) > 1000)) {
		//#endif
			lastPhantomTick = currentTimeFrame;
			phantom.addKey((int)gameTickCounter, myVehicle.getX(), myVehicle.getY(), myVehicle.getZ(), myVehicle.subUsid, myVehicle.getLateralDisplace(), myVehicle.ang);
			//System.out.println("phantom: adding key!");
		}
		
		//#ifdef CHEATS
		/*if(keyMisc == 35) {
			finishTime = raceTime - 4000;
			endPosition = 1;
			endRace = 1;
			lapCounter = totalLaps;
		}*/
		//#endif
		
		// perform all game logic
		vu = myVehicle.usid % (myVehicle.track.vertexSectionArray.length / 3);
		int su = startUsid >> 7;
		if((lastLPusid < su + displFL[currentTrack]) && (vu == su + displFL[currentTrack])) {
			lapCounter++;
			if(lapCounter > 1) lastLapTime = lapTime;
			if(((lapTime < bestTime) || (bestTime == 0)) && lapCounter != 1) {
				bestTime = lapTime;
				bestTimeBlink = 64;
				if(nPhant != 0) {
					gameTickCounter = 0;
					phantom.addKey((int)gameTickCounter, myVehicle.getX(), myVehicle.getY(), myVehicle.getZ(), myVehicle.usid, myVehicle.getLateralDisplace(), myVehicle.ang);
					phantom.setBestTrack();
				//	System.out.println("setting new best track");
				}
			} else {
				if(nPhant != 0) {
					gameTickCounter = 0;
					phantom.resetTracking();
				//	System.out.println("reseting phantom");
				}
			}
			
			if(ttplayers > 1) {
				if(((lapTime < ttpl[currPlayer]) || (ttpl[currPlayer] == 0)) && lapCounter != 1) {
					ttpl[currPlayer] = (int) lapTime;
				}

				//if(lapCounter > 1 && ((lapCounter % 2) == 1)) currPlayer = (currPlayer + 1) % ttplayers;
				if(lapCounter > 1) {
					lapCounter = 0;
					endRace = 1;
					gameTickCounter = 0;
					//startRace();
				}
			}
			
			//#ifdef ONLY_TWO_LAPS
			//#else
			if(lapCounter > 3 && historyMode == 1) {
			//#endif
				finishTime = raceTime - 4000;
				endPosition = position;
				endRace = 1;
				lapCounter = totalLaps;
			}
			
			lapTime = 0;
			if(endRace == 1) {
				lapTime = finishTime;
			}
			
			lastLPusid = vu;
			if(nPhant != 0) {
				lastPhantomTick = currentTimeFrame;
				phantom.addKey((int)gameTickCounter, myVehicle.getX(), myVehicle.getY(), myVehicle.getZ(), myVehicle.usid, myVehicle.getLateralDisplace(), myVehicle.ang);
			}
		} else {
			if((lastLPusid < vu - 1) || (lastLPusid > vu + 1)) {
				lastLPusid = vu - 1;
			}
		}
		
		if(nPhant != 0) {
			if (ttplayers > 1) {
				if(lapCounter != 0) {
					phantom.setCurrentTime((int) gameTickCounter);		/// TO FIX - HAURIA DE SER QUAN COMEN�A REALMENT LA VOLTA !�?
				}
			} else {
				if(lapCounter != 0) {
					phantom.setCurrentTime((int) gameTickCounter);
				}
			}
		}
		
		raceTime += deltaTimeFrame;
		
		if ((raceTime > 0) && (endRace == 0)) {
			try {
				myVehicle.track = track;
				
				if(keyY == -1) {
					myVehicle.status = 1;
				}
				
				if(keyY == 1) {
					myVehicle.status = -1;
				}
				
				if(keyY == 0 && myVehicle.status < 0) {
					myVehicle.status = 0;
				}
				
				for (int i = 0; i < oppoVehicles.length; ++i) {
					oppoVehicles[i].track = track;
					oppoVehicles[i].tickAI((int)deltaTimeFrame, false);
				}
				myVehicle.tick((int)deltaTimeFrame, keyX, true);

				sortVehicles();
				
				if (raceTime > 1000)	calculateCollisions();
				else					resetCollisions();

			} catch (Exception e) {
				//#ifdef EXCEPTION_CONTROL
				//gc.excp = e;
				//#endif
			}
		}
		else {
			try {
				myVehicle.tick((int)deltaTimeFrame, 0, false);
				for (int i = 0; i < oppoVehicles.length; ++i) {
					oppoVehicles[i].track = track;
					oppoVehicles[i].tick((int)deltaTimeFrame, 0, false);
				}
				sortVehicles();
				resetCollisions();
			} catch (Exception e) { }
		}
		
		playSound(SOUND_INGAME, 0);
		
		//lastTimeFrame = currentTimeFrame;
		//msLogic = (int) (System.currentTimeMillis() - msStart);
	}
	
	public void playSound(int sound, int loop) {
		if(gc == null) return;
		
		if(currentSound != sound) {
			if(gameSound) {
				gc.soundPlay(sound, loop);
				currentSound = sound;
			}
		}
	}
	/*
	public Image createImageFromPalette(String filename, String palFile) {
		byte[] ti = loadFile(filename);
		byte[] pal = loadFile(palFile);
		
		Image img = null;
		int pos = (pal[0] << 24) + (pal[1] << 16) + (pal[2] << 8) + pal[3];
		//System.out.println("writting at position : " + pos);
		System.arraycopy(pal, 4, ti, pos, pal.length - 4);
		try {
			img = Image.createImage(ti, 0, ti.length);
			//System.out.println("Creating image : " + filename + " from palette: " + palFile);
		} catch (Exception e) {
			System.out.println("exception while creating image from palette: " + e.getMessage() + " from " + filename + " : " + palFile);
		}
		pal = null;
		ti = null;
		System.gc();
		
		return img;
	}
	*/
};
