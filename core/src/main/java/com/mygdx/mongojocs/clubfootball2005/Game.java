package com.mygdx.mongojocs.clubfootball2005;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// Adaptacion J2ME Rev.2 (1.4.2004)
// ------------------------------------


//#endif

// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Random;

//#ifdef J2ME
public class Game extends MIDlet implements Runnable, CommandListener
//#endif

//#ifdef DOJA
//#endif

{

static GameCanvas gc;
boolean backFromCall = false;
byte prefsData[];

//#ifdef J2ME
Thread thread;
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()
{	
	System.gc();
	gc = new GameCanvas( this );	
	
	System.gc();
			
	//#ifdef J2ME	
	Display.getDisplay(this).setCurrent(gc);
	
	thread=new Thread(this); System.gc();
	thread.start();
	//#endif
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// startApp - Al inicio y cada vez que se hizo pauseApp()
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME
public void startApp()
{
	Display.getDisplay(this).setCurrent(gc);
	gamePaused=false; //backFromCall = true;
}
//#endif

//#ifdef DOJA
//#endif
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
	gameExit = true;
	savePrefs(); 
	
	//#ifdef J2ME
	notifyDestroyed();
	//#endif
	
	//#ifdef DOJA
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

//#ifdef SPEEDTEST
long time1, time2, time3, time4;
//#endif

String abreviate(String in)
{
	char c[];
	
	c = in.toCharArray();
	
	int i = 0;
	
	while(c[i] != ' ' && i < c.length)
	{
		i++;	
	}
	
	String out = new String(c,0,i);		
		
	return out;
}


public void run()
{
	String temp[][];
	
//	gc.gameDraw();
	rnd = new Random(System.currentTimeMillis());
	gameCreate(); System.gc();
					
	gc.canvasInit(); System.gc();
		
	//#ifdef J2ME
	gameText = textosCreate( loadFile("/Textos.txt") );	
	clubLegalText = textosCreate( loadFile("/clubLegal.txt") );	
	gc.gameDraw();
	//#endif
				
		
	//#ifdef DOJA
	//#endif
			
	//#ifdef J2ME
	inputDialogCreate(gameText[32][0],gameText[32][1],gameText[32][2],10);	
	//#endif
		
	//#ifdef J2ME
	byte aux[] = loadFile("/formation.dat");
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	//#ifdef J2ME
	temp = textosCreate(loadFile("/headline.txt"));		
	//#endif
	
	//#ifdef DOJA
	//#endif
		
	headLineTeam = (byte)Integer.parseInt(temp[0][0]);	
	headLineTeamDom = (byte)Integer.parseInt(temp[0][1]);
	domTeamsNumber = Integer.parseInt(temp[0][2]);
	temp = null; System.gc();
		
	gc.gameDraw();
	
	//System.out.println ("Domestic: "+domTeamsNumber+" Super League:"+teamsNumber);
	
	loadPrefs();
			
	formation = new int[6][10][2];
	
	/*for(int i = 0; i<6; i++)
	for(int j = 0; j<10; j++)
	for(int k = 0; k<2; k++)
		formation[i][j][k] = aux[20*i + 2*j + k];*/
			
	for(int i = 0; i<120; i++)	
		formation[i/20][(i/2)%10][i%2] = aux[i];
		
	aux = null;
	
	gc.startupTables();
	
	gc.gameDraw();
		
	ball.initTables(gc);
		
	gc.gameDraw();
	
	System.gc();
				
	while (!gameExit)
	{
		if (!gamePaused)
		{
			GameMilis = System.currentTimeMillis();

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
			
			//#ifdef CLISTENER
			//#endif
			
			//#ifdef SG-X450
			//#endif


	try {
			//#ifdef SPEEDTEST
			time1 = System.currentTimeMillis();
			//#endif
			
			biosTick();
			
			//#ifdef SPEEDTEST
			time2 = System.currentTimeMillis() - time1;
			//#endif

			gc.gameDraw();

	} catch (Exception e) {e.printStackTrace();e.toString();}

			GameSleep=(80-(int)(System.currentTimeMillis()-GameMilis));
			if (GameSleep<1) {GameSleep=1;}
			try	{Thread.sleep(GameSleep);} catch(Exception e) {}
		} 

	//System.gc();

	}

	gc.soundStop();

	savePrefs();

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
/*static int RND_Cont=0;
static int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};*/

public static int RND(int Max)
{
	/*RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;*/
	if(rnd != null)
	return Math.abs(rnd.nextInt())%Max;	
	else return 0;
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
	/*System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		for(int i = 0; i<buffer.length; i++)
			buffer[i] = (byte)is.read();
		is.close();
	}catch(Exception e) {}
	System.gc();*/
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	for(int i = 0; i < buffer.length && i < bytes.length; i++)
	{
		buffer[i] = bytes[i];
	}
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
// textos - Engine - v1.2 - Rev.4 (28.9.2004)
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

public String[][] textosCreate(byte[] tex)
{
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;

	// MONGOFIX ==========================================
	char tex_char[] = new char[tex.length];
	for(int i = 0; i < tex.length; i++)
		if(tex[i] < 0)
			tex_char[i]=(char)(tex[i]+256);
		else
			tex_char[i]=(char)tex[i];
	//=================================================

	int size = 0;
	String textos = new String(tex_char);

	for (int i=0 ; i<textos.length() ; i++)
	{
		int letra = textos.charAt(i) & 0xff;

		if (campo)		// Estamos buscando el FINAL de un campo?
		{
			if (letra == 0x7D)		// '}' Final del campo?
			{
				subCampo = false;
			}

			if (letra < 0x20 || letra == 0x7C || letra == 0x7D)	// Buscamos cuando Termina un campo
			{
				data[dataPos + 1] = (short) i;				// Nos apuntamos el index final del campo
				dataPos += 2;

				campo = false;		// Pasamos a modo Buscar inicio de un campo
			}

		} else {	// Estamos buscando cuando EMPIEZA un campo.

			if (letra == 0x7D)	// '}'
			{
				subCampo = false;
			}
			else
			if (letra == 0x7B)	// '{'
			{
				dataBak = dataPos;		// Nos apuntamos el index para marcar numero de sub-campos en esta ID
//				data[dataPos++] = 0;	// Ponemos '0' sub-campos para esta ID
				dataPos++;
				subCampo = true;
				size++;					// Incrementamos una ID nueva

				primerEnter = true;		// Activamos ke debemos eskivar el primer Enter ke encontremos
			}
			else
			if (subCampo && letra == 0x0A)	// Campos vacios en los sub-campos?
			{
				if (primerEnter)			// Si es el primer enter lo eskivamos
				{
					primerEnter = false;

				} else {

//					data[dataPos++] = 0;	// Apuntamos index de la linea vacia
					dataPos++;
					data[dataPos++] = -1;	// Tama�o de 'enter'
					data[dataBak]--;	// Incrementamos numero de subcampos (negativo para poder identificarlo luego)
				}
			}
			else
			if (letra >= 0x20)	// Buscamos cuando Empieza un campo nuevo (Caracter ASCII valido)
			{
				campo = true;	// Pasamos a modo campo encontrado, buscar fin del campo.

				data[dataPos] = (short) i;	// Nos apuntamos el index inicial de campo

				if (!subCampo)	// Si estamos en un subCampo (una ID con varios campos):
				{
					size++;		// Incrementamos una ID nueva
				} else {
					data[dataBak]--;	// Incrementamos numero de subcampos (negativo para poder identificarlo luego)
				}

				primerEnter = true;		// Activamos ke debemos eskivar el primer Enter ke encontremos
			}
		}
	}

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
		int numCampos = data[dataPos];

		if (numCampos < 0)
		{
			numCampos *= -1;
			dataPos++;
		} else {
			numCampos = 1;
		}

		strings[i] = new String[numCampos];

		for (int t=0 ; t<numCampos ; t++)
		{
			int posIni = data[dataPos++];
			int posFin = data[dataPos++];
			strings[i][t] = ( posFin<0? " " : textos.substring(posIni, posFin) );
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
boolean gameKeyconf = false;

// -------------------
// bios Create
// ===================

String[][] gameText, clubLegalText;


// -------------------
// bios Init
// ===================


// -------------------
// bios Destroy
// ===================


// -------------------
// bios Tick
// ===================

public void biosTick()
{
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

public void biosRefresh()
{
		
	playShow = true; gc.doRepaintMenu = true;
	
	switch (biosStatus)
	{

// --------------------

// --------------------
// menu Bucle
// --------------------
	case 22:
		menuListUpdate = true;					
	break;
// --------------------
	}
	
	switch(gameStatus)
	{
		case GAME_CUSTOMIZE_PLAYER :	
		case GAME_NEW_LEAGUE:
		case GAME_MENU_MAIN:				
		gc.soundPlay(0,0);		
		break;	
		
		case GAME_SEE_RANK:
		case GAME_CUP_WON:
		case GAME_CHOOSE_STRATEGY:
		case GAME_VERSUS:
		case GAME_ALIGNMENT :
		case GAME_NEW_MATCH:
		case GAME_MENU_LEAGUE:						
		gc.soundPlay(1,0);				
		break;																	
	}			
}


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

	biosStatus = 11;
}


public boolean logosTick()
{
	if ( (System.currentTimeMillis() - timeIniLogos) < timeLogos) {return false;}

	if (cntLogos == numLogos) {return true;}

	gc.canvasFillInit(rgbLogos[cntLogos]);
	
	//#ifdef J2ME
	if(cntLogos == 0) gc.canvasImg = gc.loadImage("/jamdat.png");
	else gc.canvasImg = gc.loadImage("/codemasters.png");
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	cntLogos++;

	timeIniLogos = System.currentTimeMillis();

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
// menu Create
// ===================

public void menuCreate()
{
	menuImg = gc.loadImage("/Caratula.png");
}



// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if ( (menuListMode == ML_SCROLL || menuListMode == ML_SCREEN) && keyMenu == -1 && keyMenu != lastKeyMenu)
	{
	menuAction(-2);
	}
	else
	if ( menuListTick( (keyY!=lastKeyY? keyY:(keyX!=lastKeyX? keyX:0) ), keyMenu!=lastKeyMenu? keyMenu>0:false ) )
	{
	menuAction(menuListCMD);
	}

	if (menuExit) {return true;}
	
	if(menuListUpdate) gc.doRepaintMenu = true;

	menuShow = true;

	return false;
}

// <=- <=- <=- <=- <=-














// *******************
// -------------------
// menuList - Engine - Rev.0 (20.6.2003)
// ===================
// *******************

boolean menuList_ON=false;		// Estado del menuList Engine
boolean menuListUpdate=false;

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
	menuListUpdate=false;

	menuListStr=null;
	menuListDat=null;

	menuListPos=0;
	
	gc.canvasTextStr = null;
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
		//#ifdef GFX_BIG
		return Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
		//#else
		//#endif
	//#endif
	
	//#ifdef DOJA
	//#endif
}








// -------------------
// menuList Set
// ===================

public void menuListSet_Text()
{
	menuList_ON=true;
	menuListUpdate=true;
	menuListMode = ML_TEXT;
}

public void menuListSet_Scroll()
{
	menuListScrY = menuListSizeY;
	menuList_ON=true;
	menuListUpdate=true;
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
	menuListUpdate=true;
	menuListMode = ML_OPTION;
}

public void menuListSet_Screen()
{
	menuList_ON=true;
	menuListUpdate=true;
	menuListMode = ML_SCREEN;



	menuListBloIni = 0;
	menuListBloSize = 0;

	int sizeY = 0;

	for (int i=0 ; i<menuListDat.length ; i++)
	{
	if ( (sizeY += menuListDat[i][4]) < menuListSizeY) {menuListBloSize++;} else {break;}
	}

}



// -------------------
// menuList Tick
// ===================

public boolean menuListTick(int movY, boolean fire)
{
	if (menuListMode == ML_OPTION)
	{
		if (movY ==-1 && menuListPos > 0) {menuListPos--; menuListUpdate=true;}
		else
		if (movY == 1 && menuListPos < menuListDat.length-1) {menuListPos++; menuListUpdate=true;}
		else
		if (movY ==-1 && menuListPos == 0) {menuListPos = menuListDat.length-1; menuListUpdate=true;}
		else
		if (movY == 1 && menuListPos == menuListDat.length-1) {menuListPos = 0; menuListUpdate=true;}

		if (fire)
		{
		if (++menuListDat[menuListPos][2] == menuListStr[menuListPos].length) {menuListDat[menuListPos][2]=0;}
		menuListCMD=menuListDat[menuListPos][1];
		menuListUpdate=true;
		return true;
		}
	}

/*
	if (menuListMode == ML_SCROLL)
	{
		menuListScrY--;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}

		menuListUpdate=true;
	}
*/


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

			menuListUpdate=true;
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

public void menuListDraw(Graphics g)
{

	if (!menuList_ON || !menuListUpdate) {return;}

	menuListUpdate = false;
	
	if (menuImg!=null) {gc.canvasFill(gc.canvasFillRGB); gc.showImage(menuImg);}

		//#ifdef J2ME
			//#ifndef GFX_BIG
			//#else
			menuListFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
			//#endif
		//#endif
		//#ifdef DOJA
		//#endif

		g.setFont(menuListFont);


	switch (menuListMode)
	{
	case ML_OPTION:
	
		if(gc.canvasTextStr != null) gc.textDraw(gc.canvasTextStr, 0, 0, 0xffff00, gc.TEXT_HCENTER | gc.TEXT_VCENTER | gc.TEXT_OUTLINE, 1);

		String[] texto = menuListCutText(0, menuListStr[menuListPos][menuListDat[menuListPos][2]]);

		//#ifndef SMALLDISPLAY
		//#else
		int y = menuListY + menuListSizeY - menuListFont.getHeight() - ((texto.length * menuListDat[menuListPos][4])/2);
		//#endif

		for (int i=0 ; i<texto.length ; i++)
		{
			menuListTextDraw(g, 0, (i==0 ? "< " : "")+texto[i]+(i==texto.length-1 ? " >" :""), 0,y);

			y += menuListDat[menuListPos][4];
		}
	break;


	/*case ML_SCROLL:

		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			if(menuListStr[i][0].length()>1) menuListTextDraw(g, 0, menuListStr[i][0], 0,y);

			y += menuListDat[i][4];
		}
	break;*/


	case ML_SCREEN:

		y = 0;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			y += menuListDat[i][4];
		}

		y = (menuListSizeY - y) / 2;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			if(menuListStr[i][0].length()>1) menuListTextDraw(g, 0, menuListStr[i][0], 0,y);

			y += menuListDat[i][4];

		}


	break;

	}
}






public void menuListTextDraw(Graphics g, int format, String str, int x, int y)
{
	x = menuListX + (( menuListSizeX - menuListFont.stringWidth(str) ) / 2);

	//#ifdef DOJA
	//#endif

	g.setColor(0);
	
	//#ifdef J2ME	
	g.drawString(str, x-1,y-1, 20);
	g.drawString(str, x+1,y-1, 20);
	g.drawString(str, x-1,y+1, 20);
	g.drawString(str, x+1,y+1, 20);
	g.setColor(0xFFFF00);
	g.drawString(str, x,y, 20);
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	
}







public String[] menuListCutText(int Dato, String Texto)
{
	String[] str = new String[0];

	int Pos=0, PosIni=0, PosOld=0, Size=0;
	
	//#ifdef J2ME	
	char[] Tex = Texto.toCharArray();

	Font f = menuListGetFont(Dato);

	while ( PosOld < Tex.length )
	{
	Size=0;

	Pos=PosIni;

	while ( Size < (menuListSizeX) )
	{
		if ( Pos == Tex.length ) {PosOld=Pos; break;}

		int Dat = Tex[Pos++];
		if (Dat==0x20) {PosOld=Pos-1;}
		Size += f.charWidth((char)Dat);
	}

	if (PosOld-PosIni < 1) { while ( Pos < Tex.length && Tex[Pos] >= 0x30 ) {Pos++;} PosOld=Pos; }
	//#endif
	
	
	//#ifdef DOJA
	//#endif


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


//waitStart()
//waitStop();


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
// prefs Update
// ===================

public void updatePrefs(boolean save)
{
	//#ifdef J2ME
	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("prefs", true);

		if (rs.getNumRecords() == 0)
		{
		rs.addRecord(prefsData, 0, prefsData.length);
		} else {
			if (save)
			{
			rs.setRecord(1, prefsData, 0, prefsData.length);
			} else {
			prefsData = rs.getRecord(1);
			}
		}
		rs.closeRecordStore();
	} catch(Exception e) {}
	//#endif
	
	//#ifdef DOJA
	//#endif

}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// inputDialog - Engine
// ===================
// *******************

//#ifdef J2ME
public static Form inputForm;
public static TextField inputField;
public static String inputDialogLabel;
//#endif

//#ifdef DOJA
//InputPanel pan;
//#endif


public void inputDialogCreate(String title, String subtitle, String lab, int numChars)
{
	//#ifdef J2ME
	inputDialogLabel = lab;
	
	Command doneCommand = new Command(lab, Command.OK, 1);
		
	inputForm = new Form(title);
	inputField = new TextField(subtitle, "",numChars,TextField.ANY);
	inputField.setMaxSize(numChars);
	inputForm.append(inputField);   
	inputForm.addCommand(doneCommand);
	inputForm.setCommandListener(this);
	//#endif
	
	//#ifdef DOJA
	//pan = new InputPanel(this,title,subtitle,lab,numChars);
	//#endif
}

public void inputDialogInit()
{
	//#ifdef J2ME
	//Display.getDisplay(this).setCurrent(inputForm);
	//#endif

	inputDialogNotify("MONGO");
	
	//#ifdef DOJA
	//Display.setCurrent(pan);
	//#endif
	
}

public void inputDialogNotify(String in)
{	
	char array[] = in.toCharArray();

	for(int i = 0; i<array.length; i++)
		if(array[i] < 0x20) array[i] = 0x20;
		
	String s = new String(array);

	if(s.length()>0) customPlayerNames[currentPlayer] = s;
	gameStatus = GAME_CUSTOMIZE_PLAYER;	
	menuInit(MENU_CUSTOMIZE_PLAYER); menuListPos = 0;
	gc.soundPlay(0,0);	
}

//#ifdef J2ME
public void commandAction (Command c, Displayable d)
{

	if(c.getLabel()==inputDialogLabel)
	{								
		Display.getDisplay(this).setCurrent(gc);	
		inputDialogNotify(inputField.getString());
	}  	 	
}
//#endif

// <=- <=- <=- <=- <=-




// **************************************************************************//
// Inicio Clase Game
// **************************************************************************//


final static int TEXT_LEVEL = 13;
final static int TEXT_CONGRATULATIONS = 14;


// *******************
// -------------------
// game - Engine
// ===================
// *******************


final static int GAME_LOGOS = 0;
final static int GAME_COPYRIGHT = 1;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_LEAGUE = 11;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
final static int GAME_PLAY = 20;
final static int GAME_NEW_LEAGUE = 40;
final static int GAME_CHOOSE_STRATEGY = 41;
final static int GAME_VIEW_TOURNAMENT = 42;
final static int GAME_NEW_MATCH = 43;
final static int GAME_HALF_TIME = 44;
final static int GAME_VERSUS = 45;
final static int GAME_SEE_RESULTS = 46;
final static int GAME_PROCESS_RESULTS = 47;
final static int GAME_HALFTIME_WAIT = 48;
final static int GAME_CURRENT_TOURNAMENT = 49;
final static int GAME_GOAL_WAIT = 50;
final static int GAME_PENALTIES_WAIT = 51;
final static int GAME_ENDGAME_WAIT = 52;
final static int GAME_CUP_WON = 53;
final static int GAME_ALIGNMENT = 54;
final static int GAME_CUSTOMIZE_PLAYER = 55;
final static int INGAME_CHANGES = 56;
final static int GAME_SEE_RANK = 57;
final static int GAME_IDLE = 100;

public static final int TEAMLIMIT = 30;

int gameStatus = 0, lastStatus, cnt = 0;
int cameraX, cameraY, matchTime, gameDuration = 0, myFormation=0, oppFormation, subMenuOption, teamsNumber, domTeamsNumber, currentPlayer = 0, chosenPlayer = 0, changesLeft;
byte myTeam, oppTeam, oppAlignment[] = new byte[30], headLineTeam, headLineTeamDom;
boolean secondHalf, cheatOn = true, chooseNewPlayer = false, wholeLeague, leagueWon;
tournament tour, dLeague, sLeague;

// DATA BASE
public byte playerData[][][], customPlayerData[][], teamLogos[];
public short shirtData[][][];
public String playerNames[][], teamNames[], customPlayerNames[];

public void createDataBase(int which, int first, int howManyTeams, int t1, int t2)
{
	int pos = 0, size = 0, nplayers;
	
	destroyDataBase();
		
	//try{
	
	
	//#ifdef J2ME
	byte temp[] = loadFile((which==0 ? "/Domestic.bin" : "/Licensed.bin"));
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	//System.out.println ("Array leido: "+temp.length+" bytes");
	
	for(int i = 0; i < howManyTeams; i++)
	{
		size = temp[pos]; pos++;
		
		teamNames[i] = new String(temp,pos,size);							
		
		//#ifdef SMALLDISPLAY													
		//#endif
								
		pos+=size;
		
		teamLogos[i] = temp[pos]; 
		
		//System.out.println ("Team Logo "+i+": "+teamLogos[i]);
		
		pos++;
		
		//System.out.println(teamNames[i]);
			
		for(int j=0; j<4; j++){
			for(int k = 0; k<3; k++){			
				shirtData[first + i][j][k] = (temp[pos] < 0 ? (short)(256+temp[pos]) : temp[pos]); pos++;
			}
			
			//System.out.println("("+shirtData[i][j][0]+","+shirtData[i][j][1]+","+shirtData[i][j][2]+")");
		}	
		
		nplayers = temp[pos]; pos++;
		
		//System.out.println ("Number of players:"+nplayers);
		
		playerNames[first + i] = new String[nplayers];
		
		playerData[first + i] = new byte[nplayers][6];
		
		for(int j = 0; j < nplayers; j++) 			
		{
			size = temp[pos]; pos++;
										
			if(i == t1 || i == t2){
								
				playerNames[i][j] = new String(temp,pos,size);					 	
				
				//#ifdef SMALLDISPLAY																
				//#endif
			}
			
			pos+=size;
			
			//System.out.print(size+"   Player "+j+": "+playerNames[i][j]+"   ");						
					
			for(int k = 0; k < 6; k++){
				playerData[first + i][j][k] = (byte)temp[pos]; pos++;
				//System.out.print(""+playerData[i][j][k]);
			}
			
			//System.out.println ("");
									
		}
			
		System.gc();				
	}
					
	temp = null;
		
	System.gc();
}

public void destroyDataBase()
{
	
	for(int i = 0; i < TEAMLIMIT; i++){
		
		teamNames[i] = null; playerNames[i] = null;
	}
	
	System.gc();
}

// -------------------
// game Create
// ===================

public void gameCreate()
{
			
	teamsNumber = 16;
	
	playerData = new byte[TEAMLIMIT][][];	
	shirtData = new short[TEAMLIMIT][4][3];
	teamNames = new String[TEAMLIMIT];
	playerNames = new String[TEAMLIMIT][];
	teamLogos = new byte[TEAMLIMIT];
			
}

// -------------------
// game Destroy
// ===================

public void displayLoadScreen()
{
	gc.canvasFillInit(0x000000);			
	gc.canvasTextInit(gameText[21][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER);
	gc.gameDraw();	
}

// -------------------
// game Tick
// ===================

public void gameTick()
{	
	lastStatus = gameStatus;
	cnt++;
	
	int max;
		
	switch (gameStatus)
	{
	case GAME_LOGOS:
		logosInit(2, new int[] {0xffffff, 0xffffff}, 3000);
		gameStatus = GAME_COPYRIGHT;
	break;	
	
	case GAME_COPYRIGHT:
		
		gc.canvasFillInit(0xffffff);
		//#ifdef GFX_BIG
		gc.canvasTextInit(gameText[39][0]+gameText[39][1]+gameText[39][2], 0,0, 0x000000, gc.TEXT_VCENTER|gc.TEXT_HCENTER);
		gc.gameDraw();
		waitTime(3000);
		//#else
		//#endif

		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:		
		gc.destroyMatchGfx();
		destroyDataBase();
		gc.canvasFillInit(0);
		//#ifdef INITIALLOAD
		menuImg = gc.pictureImg;
		//#else
		//#endif
		gc.soundPlay(0,0);
		menuInit(MENU_MAIN);		
	break;	
	
	case GAME_MENU_LEAGUE:						
		gc.soundPlay(1,0);
		menuInit(MENU_LEAGUE);		
	break;	
	
	case GAME_NEW_MATCH:
		displayLoadScreen();
		gc.destroyMatchGfx();
		createDataBase((tour.isDomestic ? 0 : 1),0,(tour.isDomestic ? domTeamsNumber : teamsNumber),myTeam,oppTeam);
		gc.loadMatchGfx();
		matchTime = 0; secondHalf = false;
		goalsA = 0; goalsB = 0;				
		gameStatus = GAME_CHOOSE_STRATEGY;
	break;
	
	/*case GAME_CURRENT_TOURNAMENT:
		if(keyX > 0) tourScrollX-=3;
		if(keyX < 0) tourScrollX+=3;
		if(keyY > 0) tourScrollY-=3;
		if(keyY < 0) tourScrollY+=3;
		if(tourScrollX<-128) tourScrollX=-128;
		if(tourScrollX>128) tourScrollX=128;
		if(tourScrollY<-64) tourScrollY=-64;
		if(tourScrollY>64) tourScrollY=64;
		if(keyMenu != 0 && lastKeyMenu == 0)
		{
			gameStatus = GAME_MENU_MAIN;
		}
		playShow = true;
	break;*/
	
	case GAME_GOAL_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=15) {//continueMatch(); 
		matchState = GAME;
    	firstPass = true;
		gameStatus = GAME_PLAY;}		
		playShow = true;		
	break;	
	
	case GAME_HALFTIME_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=15) {gameStatus = GAME_HALF_TIME;}		
		playShow = true;		
	break;
	
	/*case GAME_PENALTIES_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=30) {gameStatus = GAME_PLAY;}		
		playShow = true;		
	break;*/
	
	case GAME_ENDGAME_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=30)
		{
			gc.soundPlay(2,1); 			
			/*if(goalsA == goalsB){
				gc.soundPlay(4,1);
				gameStatus = GAME_PENALTIES_WAIT;
			}else{*/ 																	
			tour.simulate();						
			tour.setGoals(myTeam,(byte)goalsA);
			tour.setGoals(oppTeam,(byte)goalsB);														
			gameStatus = GAME_SEE_RESULTS;
			//}
		}		
		playShow = true;		
	break;
	
	case GAME_HALF_TIME:
		matchTime = 0; secondHalf = true;
		gc.soundPlay(1,0);
		gameStatus = GAME_CHOOSE_STRATEGY;
	break;
	
	case GAME_NEW_LEAGUE:
		
		switch(subMenuOption){
			
			case 2 :
			if(keyX > 0 && lastKeyX == 0)  gameDuration = (gameDuration+1)%3;
			if(keyX < 0 && lastKeyX == 0) gameDuration = (3+gameDuration-1)%3;			
			break;
			
			case 1 :
			if(keyX != 0 && lastKeyX == 0) wholeLeague = (wholeLeague ? false : true);
			break;
			
			case 0 :
			if((!tour.isDomestic && leagueWon) || headLineTeam < 0){
				if(keyX > 0 && lastKeyX == 0) myTeam = (byte)((myTeam+1)%tour.teamsNumber);
				if(keyX < 0 && lastKeyX == 0) myTeam = (byte)((tour.teamsNumber+myTeam-1)%tour.teamsNumber);
			}
			break;
		}
		if(keyY > 0 && lastKeyY == 0) subMenuOption = (subMenuOption+1)%3;
		if(keyY < 0 && lastKeyY == 0) subMenuOption = (3+subMenuOption-1)%3;
		
		if(keyMenu > 0 && lastKeyMenu == 0){
			
			displayLoadScreen();
			
			if(tour.isDomestic) {dLeague = new tournament(domTeamsNumber,true); tour = dLeague;}
			else {sLeague = new tournament(teamsNumber,false); tour = sLeague;}
			
			System.gc();
												
			gameStatus = GAME_MENU_LEAGUE; 			
			tour.myTeam = myTeam; tour.duration = gameDuration; tour.wholeLeague = wholeLeague;
			tour.setBasicAlignment(playerNames[myTeam].length,CUSTOM_PLAYERS);			
			displayLoadScreen();			
			createDataBase((tour.isDomestic ? 0 : 1),0,(tour.isDomestic ? domTeamsNumber : teamsNumber),myTeam,-1);		
		}
		
		if(keyMenu < 0 && lastKeyMenu == 0){
						
			gameStatus = GAME_MENU_MAIN;				
		}		
		playShow = (keyX != lastKeyX || keyY != lastKeyY || keyMenu != lastKeyMenu || cnt == 1);	
	break;
	
	case GAME_ALIGNMENT :
	
	max = tour.teamSize;

	if(!chooseNewPlayer){
		
		//#if GFX_BIG
		
		if(keyY > 0 && lastKeyY == 0) 
		{
			switch(subMenuOption)
			{
				case 0 : subMenuOption = 1; currentPlayer = 0; break;
				case 1 : if(currentPlayer < 10) currentPlayer++; else subMenuOption = 0; break;
			}
		}
		
		if(keyY < 0 && lastKeyY == 0) 
		{
			switch(subMenuOption)
			{
				case 0 : subMenuOption = 1; currentPlayer = 10; break;
				case 1 : if(currentPlayer > 0 ) currentPlayer--; else subMenuOption = 0; break;
			}
		}
						
		//#else
		//#endif
		
		if(keyMenu < 0 && lastKeyMenu == 0){
			gameStatus = GAME_MENU_LEAGUE;				
		}				
	}
	
	switch(subMenuOption){
		
			case 0 :
			if(keyX > 0 && lastKeyX == 0) myFormation = (myFormation+1)%6;
			if(keyX < 0 && lastKeyX == 0) myFormation = (6+myFormation-1)%6;
			break;
						
			case 1 :
			if(chooseNewPlayer){
				
				if((keyX > 0 && lastKeyX == 0) || (keyY > 0 && lastKeyY == 0)) chosenPlayer = (chosenPlayer+1)%max;
				if((keyX < 0 && lastKeyX == 0) || (keyY < 0 && lastKeyY == 0)) chosenPlayer = (max+chosenPlayer-1)%max;	
				
				if(keyMenu < 0 && lastKeyMenu == 0) chooseNewPlayer = false;
				
				if(keyMenu > 0 && lastKeyMenu == 0)
				{
					byte aux = tour.myAlignment[currentPlayer];
					tour.myAlignment[currentPlayer] = tour.myAlignment[chosenPlayer];
					tour.myAlignment[chosenPlayer] = aux;
										
					chooseNewPlayer = false;	
				}
			}else{
			
				if(keyX > 0 && lastKeyX == 0) currentPlayer = (currentPlayer+1)%11;
				if(keyX < 0 && lastKeyX == 0) currentPlayer = (11+currentPlayer-1)%11;	
				
				
				if(keyMenu > 0 && lastKeyMenu == 0)
				{
					chooseNewPlayer = true;
				}
			}		
			break;						
		}
		
		playShow = (keyX != lastKeyX || keyY != lastKeyY || keyMenu != lastKeyMenu);		
		
	break;
	
	
	case INGAME_CHANGES :
	
	subMenuOption = 1;
	max = tour.teamSize;

	if(!chooseNewPlayer){
		
		if(keyMenu < 0 && lastKeyMenu == 0){
			assignPlayerData(); gameStatus = GAME_MENU_SECOND;				
		}				
		
		if(keyY > 0 && lastKeyY == 0) currentPlayer = (currentPlayer+1)%11;
		if(keyY < 0 && lastKeyY == 0) currentPlayer = (11+currentPlayer-1)%11;
		
	}
		
	if(chooseNewPlayer){
				
		if((keyX > 0 && lastKeyX == 0) || (keyY > 0 && lastKeyY == 0)) chosenPlayer++; if(chosenPlayer>=max) chosenPlayer = 11;
		if((keyX < 0 && lastKeyX == 0) || (keyY < 0 && lastKeyY == 0)) chosenPlayer--; if(chosenPlayer < 11) chosenPlayer = max-1;
		
		if(keyMenu < 0 && lastKeyMenu == 0) chooseNewPlayer = false;
			
		byte aux = tour.myAlignment[currentPlayer];				
		
		if(tour.myAlignment[chosenPlayer] != changesList[0] && tour.myAlignment[chosenPlayer] != changesList[1] && chosenPlayer != currentPlayer)
		if(keyMenu > 0 && lastKeyMenu == 0)
		{
												
			changesList[3 - changesLeft] = aux;
			
			tour.myAlignment[currentPlayer] = tour.myAlignment[chosenPlayer];
			tour.myAlignment[chosenPlayer] = aux;
										
			chooseNewPlayer = false; changesLeft--; 
			
		}
	}else{
			
		if(keyX > 0 && lastKeyX == 0) currentPlayer = (currentPlayer+1)%11;
		if(keyX < 0 && lastKeyX == 0) currentPlayer = (11+currentPlayer-1)%11;	
		
				
		if(keyMenu > 0 && lastKeyMenu == 0 && changesLeft > 0)
		{
			chooseNewPlayer = true; chosenPlayer = 11;
		}
	}							
			
	playShow = (keyX != lastKeyX || keyY != lastKeyY || keyMenu != lastKeyMenu);		
		
	break;	
	
	case GAME_CUSTOMIZE_PLAYER :	
		displayLoadScreen();
		gc.destroyMatchGfx();
		gc.loadMatchGfx();	
		dummy.x = 0; dummy.y = 0;
		menuInit(MENU_SELECT_CUSTOM);				
	break;
	
	/*case GAME_VIEW_TOURNAMENT:
		if(tourScrollX>-(130 - gc.canvasWidth/2) && keyX > 0) tourScrollX-=3;
		if(tourScrollX<(130 - gc.canvasWidth/2) && keyX < 0) tourScrollX+=3;
		if(tourScrollY>-(60 - gc.canvasHeight/2) && keyY > 0) tourScrollY-=3;
		if(tourScrollY<(60 - gc.canvasHeight/2) && keyY < 0) tourScrollY+=3;
	
		if(keyMenu != 0 && lastKeyMenu == 0)
		{
			oppTeam = tour.myOpp(myTeam); gameStatus = GAME_VERSUS;
		}
		playShow = true;		
	break;*/
	
	case GAME_VERSUS:
		if(keyMenu != 0 && lastKeyMenu == 0)
			gameStatus = GAME_NEW_MATCH;
		playShow = true;
	break;
	
	case GAME_SEE_RESULTS :
		gc.destroyMatchGfx();
		menuInit(MENU_SEE_RESULTS);				
	break;
	
	case GAME_CUP_WON:
		if(keyMenu != 0 && lastKeyMenu == 0)
		{
			tour.current = 0;			
			if(tour.rank[0][0] == myTeam) leagueWon = true; savePrefs();
			gameStatus = GAME_MENU_MAIN;
		}
		playShow = true;
	break;
	
	case GAME_PROCESS_RESULTS :			
		tour.step();
		savePrefs();				
		menuExit = true;
		if((!tour.wholeLeague && tour.current == tour.teamsNumber - 1) || (tour.wholeLeague && tour.current == (tour.teamsNumber - 1)*2)){
			gc.loadCupGfx();
			gameStatus = GAME_CUP_WON;				
		}
		else gameStatus = GAME_MENU_LEAGUE;				
		
		//playShow = true;
	break;
	
	case GAME_CHOOSE_STRATEGY:
		if((keyX > 0 && lastKeyX == 0) || (keyY > 0 && lastKeyY == 0)) myFormation = (myFormation+1)%6;
		if((keyX < 0 && lastKeyX == 0) || (keyY < 0 && lastKeyY == 0)) myFormation = (6+myFormation-1)%6;
		
		if(keyMenu != 0 && lastKeyMenu == 0){
			playExit=0;					
			oppFormation = RND(6);
			if(secondHalf) halfMatch(myFormation,oppFormation); else initMatch(myFormation,oppFormation);			
			gameStatus = GAME_PLAY;	
			menuExit = true;
			gc.soundStop();						
		}
		playShow = true;			
	break;

	case GAME_PLAY:
		if ((keyMenu == -1 && lastKeyMenu == 0) || backFromCall) {gameStatus = GAME_MENU_SECOND; break;}
		
		matchTime++;
				
		matchTick();
		
		int cx, cy;
		
		if(ball.theOwner == null) { cx = ball.x; cy = ball.y; }
		else { 
			cx = ball.theOwner.x + (ball.cos(32*(ball.theOwner.direction - 2))>>6); 
			cy = ball.theOwner.y + (ball.sin(32*(ball.theOwner.direction - 2))>>6);
		}
		
		cameraX += (cx - cameraX)/4;
		cameraY += (cy - cameraY)/4;

		if(matchState != PENALTIES && matchState < WAITCORNER){
			if(matchTime>durationTicks()) 
				if(secondHalf){ 
													
					/*if(goalsA == goalsB){
						gc.soundPlay(4,1);
						gameStatus = GAME_PENALTIES_WAIT;
					}else{ 																	
						tour.simulate();						
						tour.setGoals(myTeam,goalsA);
						tour.setGoals(oppTeam,goalsB);														
						gameStatus = GAME_ENDGAME_WAIT;
					}*/
					gc.soundPlay(6,1);
					gameStatus = GAME_ENDGAME_WAIT;
				
				}else{gc.soundPlay(6,1); gameStatus = GAME_HALFTIME_WAIT;}
			
			if(matchState == GOAL) {gc.soundPlay(3,1); gc.vibraInit(200); gameStatus = GAME_GOAL_WAIT;}
		}
		/*
		if(cheatOn)
		{
			if(keyMisc ==35 && lastKeyMisc != keyMisc) 
				goalsA++;
			if(keyMisc ==42 && lastKeyMisc != keyMisc) 
				goalsB++;
			if(keyMisc ==10 && lastKeyMisc != keyMisc) 
				{matchTime = 99999; secondHalf = true;}
		}
		*/			
		playShow = true;		
	break;

	case GAME_MENU_SECOND:
		menuInit(MENU_SECOND);		
	break;

	/*case GAME_MENU_GAMEOVER:


		gc.canvasFillInit(0x000000);
		gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
		gc.gameDraw();

		waitTime(3000);

		gameStatus = GAME_MENU_MAIN;
	break;*/
	
	case GAME_SEE_RANK :
		menuInit(MENU_SEE_RANK);		
	break;
			
	}
	
		
	if(lastStatus != gameStatus) {cnt = 0; gc.ledFr = 0;}
	backFromCall = false;

}

int durationTicks()
{
	switch(gameDuration)
	{
		//default : return 5*16;
		default : return 60*16;	
		case 1 : return 150*16;
		case 2 : return 300*16;
	}	
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

void createSaveData()
{

	try{
		
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	    DataOutputStream ostream = new DataOutputStream(bstream);
	
															
		ostream.writeBoolean(gameSound);
		ostream.writeBoolean(gameVibra);
		ostream.writeBoolean(gameKeyconf);		
		ostream.writeBoolean(leagueWon);		
		ostream.writeInt(myFormation);
			
		ostream.writeInt(myTeam);
		dLeague.write(ostream);
		sLeague.write(ostream);
			
		for(int i = 0; i<CUSTOM_PLAYERS; i++){
						
			ostream.writeUTF(customPlayerNames[i]);		
				
			for(int j = 0; j<6; j++)
				ostream.writeByte(customPlayerData[i][j]);	
		}
									
	    ostream.flush();
	    ostream.close();
	                	
    	prefsData = bstream.toByteArray();	
    	
    }catch(Exception e){}
}

public static final int CUSTOM_PLAYERS = 5;

public void loadPrefs()
{
	gameSound = true; gameVibra = false; gameKeyconf = false; leagueWon = false; gameDuration = 1; myFormation = 0; myTeam = 0;
	dLeague = new tournament(domTeamsNumber,true); sLeague = new tournament(teamsNumber,false); tour = dLeague;
	
	customPlayerData = new byte[CUSTOM_PLAYERS][6];
	customPlayerNames = new String[CUSTOM_PLAYERS];
	
	for(int i = 0; i<CUSTOM_PLAYERS; i++){
		
		customPlayerNames[i] = gameText[37][0]+(i+1);
		
		customPlayerData[i][0] = 0;	
		customPlayerData[i][1] = 0;	
		
		for(int j = 2; j<6; j++)
			customPlayerData[i][j] = 4;	
	}
	
	//#ifdef DOJA
	//#endif
	try{
		
		/*System.out.println ("Default options creating...");*/
	
		createSaveData();
		    	    	
    	//#ifdef DOJA
		//#endif
    
    }catch(Exception e){}	
	
	//#ifdef J2ME
	updatePrefs(false);		
	//#endif
		
	try{
	
 		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(prefsData, 0, prefsData.length));
                    		
		gameSound=istream.readBoolean();
		gameVibra=istream.readBoolean();
		gameKeyconf=istream.readBoolean();		
		leagueWon=istream.readBoolean();		
		myFormation=istream.readInt();
		
		myTeam = (byte)istream.readInt();
		dLeague.read(istream);
		sLeague.read(istream);
		
		for(int i = 0; i<CUSTOM_PLAYERS; i++){
					
			customPlayerNames[i] = istream.readUTF();		
			
			for(int j = 0; j<6; j++)
				customPlayerData[i][j] = istream.readByte();	
		}
			  		
	}catch(Exception e){}

	prefsData = null; System.gc();
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	
	createSaveData();
        
	updatePrefs(true);
	
	prefsData = null; System.gc();
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
static final int MENU_SEE_RESULTS = 5;
static final int MENU_LEAGUE = 7;
static final int MENU_SEE_RANK = 8;
static final int MENU_SELECT_CUSTOM = 9;
static final int MENU_CUSTOMIZE_PLAYER = 10;
static final int MENU_CONFIRM_RESTART = 11;
static final int MENU_CONFIRM_EXIT = 12;


static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;
static final int MENU_ACTION_CONFIRM_RESTART = 50;
static final int MENU_ACTION_CONFIRM_EXIT = 51;


int menuType;
int menuTypeBack;

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuTypeBack = menuType;
	menuType = type;	

	menuExit = false;

	menuListInit(10, 0, gc.canvasWidth - 20, gc.canvasHeight);


	switch (type)
	{
	case MENU_MAIN:
		menuListClear();
		if(dLeague.current>0) menuListAdd(0, gameText[19], 11);				
		menuListAdd(0, gameText[0], MENU_ACTION_PLAY);
		if(sLeague.current>0) menuListAdd(0, gameText[14], 18);				
		menuListAdd(0, gameText[13], 17);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		//#ifdef NK-s60
		//#endif
		menuListAdd(0, gameText[16], 20);
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_SECOND:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
		menuListAdd(0, gameText[34], 30);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		//#ifdef NK-s60
		//#endif
		//menuListAdd(0, new String[] {"Cheat Off","Cheat On"}, 13, cheatOn?1:0);
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_CONFIRM_RESTART);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_CONFIRM_EXIT);
		menuListSet_Option();
	break;
		
	case MENU_SCROLL_HELP:
		menuListClear();
		menuListAdd(0, gameText[(gameKeyconf ? 24 : TEXT_HELP_SCROLL)]);
		menuListSet_Screen();
	break;

	case MENU_SCROLL_ABOUT:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListAdd(0, gameText[39]);
		menuListAdd(0, new String[] {" ",clubLegalText[0][0]," "});
		//menuListAdd(0, gameText[41]);
		menuListSet_Screen();
	break;
	
	case MENU_SEE_RESULTS:
		menuListClear();
		menuListAdd(0, gameText[20][0]+" "+(tour.current+1));		
		for(int i = 0; i<tour.league[0].length; i++){				
			menuListAdd(0,"   ");
			menuListAdd(0,teamNames[tour.league[tour.current][i][0]]+" :"+tour.league[tour.current][i][2]+" - "+(teamNames[tour.league[tour.current][i][1]])+" :"+(tour.league[tour.current][i][3]));					
		}
		menuListSet_Screen();
	break;
	
	case MENU_LEAGUE:
		menuListClear();
		menuListAdd(0, gameText[27], 16);
		menuListAdd(0, gameText[28], 19);
		menuListAdd(0, gameText[29], 15);
		menuListAdd(0, gameText[30], MENU_ACTION_RESTART);
		menuListSet_Option();
	break;
	
	case MENU_SELECT_CUSTOM:
		menuListClear();
		for(int i = 0; i< CUSTOM_PLAYERS; i++)
			menuListAdd(0, customPlayerNames[i], 21);		
		menuListAdd(0, gameText[30], MENU_ACTION_RESTART);
		menuListSet_Option();
	break;
	
	case MENU_CUSTOMIZE_PLAYER:
		dummy.playerSkin = customPlayerData[currentPlayer][0];
		dummy.playerHead = customPlayerData[currentPlayer][1];
		menuListClear();		
		//#ifdef J2ME
			menuListAdd(0, gameText[31][0], 23);
			//#ifdef SMALLDISPLAY
			//menuListAdd(0, customPlayerNames[currentPlayer], 23);
			//#else
			//menuListAdd(0, gameText[31][0] + customPlayerNames[currentPlayer], 23);
			//#endif
		//#endif
		
		//#ifdef NOBLACKMEN
		//#else
		for(int i = 0; i<6; i++)			
		//#endif
			menuListAdd(0, gameText[31][i+1] + (customPlayerData[currentPlayer][i]+1), 24+i);
			
		menuListAdd(0, gameText[30], 22);
		menuListSet_Option();
	break;
	
	case MENU_SEE_RANK:
		menuListClear();
		for(int i = 0; i<tour.rank.length; i++){
			menuListAdd(0, (i+1)+": "+teamNames[tour.rank[i][0]]+gameText[36][0]+tour.rank[i][1]+gameText[36][1]+tour.rank[i][2]+gameText[36][2]+tour.rank[i][3]+gameText[36][3]+tour.rank[i][4]+gameText[36][4]+(tour.rank[i][5])+gameText[36][5]+(tour.rank[i][6]));
			if(i != tour.rank.length-1) menuListAdd(0,"   "); 
		}
		menuListSet_Screen();
	break;
	
	case MENU_CONFIRM_EXIT:
	case MENU_CONFIRM_RESTART:		
	menuListClear();
	menuListAdd(0, gameText[40][2], MENU_ACTION_CONTINUE);		
	menuListAdd(0, gameText[40][1], (type == MENU_CONFIRM_RESTART ? MENU_ACTION_RESTART : MENU_ACTION_EXIT_GAME));	
	menuListSet_Option();
	gc.canvasTextStr = gameText[40][0];
	break;
	
	}
	
	biosStatus = 22;
				
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{

	switch (cmd)
	{
	case -3: // Scroll ha sido cortado por usario
	case -2: // Scroll ha llegado al final
		if(menuType == MENU_SEE_RESULTS) {menuExit = true; gc.soundPlay(1,0); gameStatus = GAME_PROCESS_RESULTS;}
		else if(menuType == MENU_SEE_RANK) {menuExit = true; gameStatus = GAME_MENU_LEAGUE;}
		else menuInit(menuTypeBack);
	break;


	case MENU_ACTION_PLAY:		// Jugar de 0, liga domestica		
		displayLoadScreen();
		menuImg = null; System.gc();
		createDataBase(0,0,domTeamsNumber,myTeam,-1);		
		tour = dLeague;		
		menuExit = true;		
		gameStatus = GAME_NEW_LEAGUE; 		
		myTeam = (byte)(headLineTeam > 0 ? headLineTeamDom : 0); subMenuOption = 0; gameDuration = 1; myFormation = 0; wholeLeague = true;
	break;
	
	case 17:		// Jugar de 0, super liga
		displayLoadScreen();
		menuImg = null; System.gc();
		createDataBase(1,0,teamsNumber,myTeam,-1);		
		tour = sLeague;
		menuExit = true;		
		gameStatus = GAME_NEW_LEAGUE; 		
		myTeam = (headLineTeam > 0 ? headLineTeam : 0); subMenuOption = 0; gameDuration = 1; myFormation = 0; wholeLeague = true;
	break;
	
	case MENU_ACTION_CONTINUE:	// Continuar
		gameStatus = GAME_PLAY;	
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_HELP:		// Controles...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// About...

		menuInit(MENU_SCROLL_ABOUT);
	break;


	case MENU_ACTION_CONFIRM_RESTART:
	
		menuInit(MENU_CONFIRM_RESTART);
	break;
	
	case MENU_ACTION_CONFIRM_EXIT:
	
		menuInit(MENU_CONFIRM_EXIT);
	break;

	case MENU_ACTION_RESTART:	// Restart

		playExit = 3;
		menuExit = true;
		gameStatus = GAME_MENU_MAIN;
	break;


	case MENU_ACTION_EXIT_GAME:	// Exit Game

		gameExit = true;
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) {gc.soundPlay(0,0);} else gc.soundPlay(5,1);
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListOpt() != 0; if(gameVibra) gc.vibraInit(200);
	break;
		
	/*case 9 :		
		playExit=0;		
		myFormation = menuListPos; 
		oppFormation = RND(6);
		if(secondHalf) halfMatch(myFormation,oppFormation); else initMatch(myFormation,oppFormation,4-tour.current);
		gameStatus = GAME_PLAY;	
		menuExit = true;
		gc.soundStop();
	break;*/
	
	case 10 :
		gameDuration = (gameDuration+1)%3;
	break;
	
	case 11 :					
		displayLoadScreen();
		menuImg = null; System.gc();
		menuExit = true;		
		tour = dLeague;
		myTeam = tour.myTeam;
		createDataBase(0,0,domTeamsNumber,myTeam,-1);
		gameDuration = tour.duration;		
		gameStatus = GAME_MENU_LEAGUE;	
	break;
	
	case 18 :				
		displayLoadScreen();
		menuImg = null; System.gc();
		menuExit = true;		
		tour = sLeague;
		myTeam = tour.myTeam;
		createDataBase(1,0,teamsNumber,myTeam,-1);
		gameDuration = tour.duration;		
		gameStatus = GAME_MENU_LEAGUE;	
	break;
	
	case 12 :
		menuImg = null;
		menuExit = true;		
		gameStatus = GAME_CURRENT_TOURNAMENT;
	break;
	
	case 13:
		cheatOn = menuListOpt() != 0;
	break;
	
	//#ifdef NK-s60
	//#endif
	
	case 15:
		menuExit = true;
		gameStatus = GAME_SEE_RANK;		
	break;
	
	case 16:
		oppTeam = tour.myOpp(myTeam);
		menuExit = true;		
		gameStatus = GAME_VERSUS;
	break;
	
	case 19 :
		menuExit = true;		
		subMenuOption = 0; currentPlayer = 0;
		gameStatus = GAME_ALIGNMENT;		
	break;
	
	case 20 :
		menuImg = null;
		menuExit = true;		
		gameStatus = GAME_CUSTOMIZE_PLAYER;
	break;
	
	case 21 :
		menuExit = true;
		currentPlayer = menuListPos;		
		menuInit(MENU_CUSTOMIZE_PLAYER);
	break;
	
	case 22 :
		menuInit(MENU_SELECT_CUSTOM);
	break;
	
	//#ifdef J2ME
	case 23 :
		inputDialogInit();		
		gameStatus = GAME_IDLE;			
		//#ifdef LISTENERABORT		
			inputDialogNotify("");					
		//#endif		
	break;
	//#endif
	
	case 24 :
		customPlayerData[currentPlayer][0] = (byte)((customPlayerData[currentPlayer][0] + 1)%2);
		menuInit(MENU_CUSTOMIZE_PLAYER); 
		//#ifdef J2ME
			//#ifdef NOBLACKMEN
			//#else
			menuListPos = 1;
			//#endif
		//#endif
		//#ifdef DOJA
		//#endif
	break;
	
	case 25 :
		customPlayerData[currentPlayer][1] = (byte)((customPlayerData[currentPlayer][1] + 1)%10);
		menuInit(MENU_CUSTOMIZE_PLAYER); 
		//#ifdef J2ME
			//#ifdef NOBLACKMEN
			//#else
			menuListPos = 2;
			//#endif
		//#endif
		//#ifdef DOJA
		//#endif
	break;
	
	/*case 26 :
		customPlayerData[currentPlayer][2] = (byte)((customPlayerData[currentPlayer][2] + 1)%10);
		menuInit(MENU_CUSTOMIZE_PLAYER); 
		//#ifdef J2ME
		menuListPos = 3;
		//#endif
		//#ifdef DOJA
		menuListPos = 2;
		//#endif
	break;
	
	case 27 :
		customPlayerData[currentPlayer][3] = (byte)((customPlayerData[currentPlayer][3] + 1)%10);
		menuInit(MENU_CUSTOMIZE_PLAYER); 
		//#ifdef J2ME
		menuListPos = 4;
		//#endif
		//#ifdef DOJA
		menuListPos = 3;
		//#endif
	break;
	
	case 28 :
		customPlayerData[currentPlayer][4] = (byte)((customPlayerData[currentPlayer][4] + 1)%10);
		menuInit(MENU_CUSTOMIZE_PLAYER); 
		//#ifdef J2ME
		menuListPos = 5;
		//#endif
		//#ifdef DOJA
		menuListPos = 4;
		//#endif
	break;
	
	case 29 :
		customPlayerData[currentPlayer][5] = (byte)((customPlayerData[currentPlayer][5] + 1)%10);
		menuInit(MENU_CUSTOMIZE_PLAYER); 
		//#ifdef J2ME
		menuListPos = 6;
		//#endif
		//#ifdef DOJA
		menuListPos = 5;
		//#endif
	break;*/
	
	case 26 :
	case 27 :
	case 28 :
	case 29 :
		customPlayerData[currentPlayer][cmd-24] = (byte)(customPlayerData[currentPlayer][cmd-24] + 1);
		if(customPlayerData[currentPlayer][cmd-24]>=10 || customPlayerUsedPoints(currentPlayer)>30) customPlayerData[currentPlayer][cmd-24] = 0;
		menuInit(MENU_CUSTOMIZE_PLAYER); 
		//#ifdef J2ME
			//#ifdef NOBLACKMEN
			//#else
			menuListPos = cmd - 23;
			//#endif
		//#endif
		//#ifdef DOJA
		//#endif
	break;
		
	case 30 :
		menuExit = true;		
		gameStatus = INGAME_CHANGES;
	break;
				
	}

}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// play - Engine
// ===================
// *******************

boolean playShow;

int playExit;

/*int protX;
int protY;

// -------------------
// play Create
// ===================

public void playCreate()
{
	gc.canvasFillInit(0);

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
	gc.playInit_Gfx();


	protX = 0;
	protY = 0;

//	gc.canvasTextInit(gameText[13][0]+" 1", 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);

	gc.canvasFillInit(0x000000);
	gc.canvasTextInit("Inicio del nivel este que no vale na que es una chorrada", 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
	gc.gameDraw();

	waitTime(3000);

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
	protX += keyX * 4;
	protY += keyY * 4;


	if ( colision(protX, protY, 16,16,  0,gc.canvasHeight/2, 16,16) ) { playExit = 3; }


	if ( colision(protX, protY, 16,16,  gc.canvasWidth-16,gc.canvasHeight/2, 16,16) ) { playExit = 1; }


	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}*/

// <=- <=- <=- <=- <=-


public int customPlayerUsedPoints(int id)
{
	return customPlayerData[id][2]+customPlayerData[id][3]+customPlayerData[id][4]+customPlayerData[id][5]+4;			
}



// **************************************************************************//
// Final Clase Game Carlos
// **************************************************************************//


int nfc;
static int formation[][][] /*= {
    {{0,0},{0,2},{0,4},{0,6},{1,2},{1,4},{2,0},{2,6},{3,2},{3,4}},
    {{0,0},{0,2},{0,4},{0,6},{1,1},{1,3},{1,5},{2,2},{2,4},{3,3}},
    {{0,1},{0,3},{0,5},{1,2},{1,4},{2,0},{2,3},{2,6},{3,2},{3,4}},
    {{0,0},{0,2},{0,3},{0,4},{0,6},{1,1},{1,3},{1,5},{2,3},{3,3}},
    {{0,1},{0,3},{0,5},{1,2},{1,4},{2,0},{2,6},{3,1},{3,3},{3,5}},
    {{0,2},{0,4},{1,1},{1,3},{1,5},{3,0},{3,2},{3,3},{3,4},{3,6}}}*/;
    
int matchState;
final static int GAME       = 0;
final static int PENALTIES  = 1;
final static int ENDGAME    = 2;
final static int GOAL       = 3;
final static int WAITCORNER = 4;
final static int WAITOUT    = 5;
final static int WAITGOAL    = 6;
int theTeam;
int wait;
soccerPlayer theTeam2[];

final static int groundWidth  = 90*2;//220;
final static int groundHeight = 120*2;//288;
final static int hgroundWidth  = groundWidth/2;
final static int hgroundHeight = groundHeight/2;
final static int INFINITE = 10000;
final static int goalSize = 34;
final static int hgoalSize = goalSize/2;
final static int goalPostSize = 3;
final static int TEAMA = 0;
final static int TEAMB = 1;

soccerPlayer teamA[] = new soccerPlayer[10];
soccerPlayer teamB[] = new soccerPlayer[10];
soccerPlayer dummy = new soccerPlayer(this,1,null);
goalKeeper gkA, gkB;
ball ball;
int teamDown;	
soccerPlayer strikeTeam[];

static Random rnd;
int goalsA, goalsB;
int pointer;



public void goal(int team)
{
    //GOL 
    humanSoccerPlayer = null;
    
    
    if (team == TEAMA)
    {
        goalsA++;
        strikeTeam = teamB;       
        ball.activate(hgroundWidth, hgroundHeight);     
   }
   else
   {
        //GOL 
        goalsB++;
        strikeTeam = teamA;
        ball.activate(hgroundWidth, hgroundHeight);            
   }
   setTeamsState(soccerPlayer.PREMATCH);
   gkA.state = goalKeeper.FREE;
   gkB.state = goalKeeper.FREE;       
  // if (matchState == PENALTIES) nextPenalty();
   if (matchState == GAME) matchState = GOAL;
   
}

/*public void continueMatch()
{
    matchState = GAME;
    firstPass = true;
}*/


int outSide;
int outSideV;

public void out(soccerPlayer[] outTeam)
{    
    humanSoccerPlayer = null;    
    outSide = ball.x < hgroundWidth?0:groundWidth-1;
    if (outTeam == teamA) strikeTeam = teamB;           
    else strikeTeam = teamA;
    if (ball.y < 0) ball.y = 1;
    if (ball.y >= groundHeight)  ball.y = groundHeight-1;
    ball.activate(outSide , ball.y);                    
    setTeamsState(soccerPlayer.PREOUT);        
}

public void corner(soccerPlayer[] cornerTeam)
{    
    humanSoccerPlayer = null;    
    outSide = ball.x < hgroundWidth?0:groundWidth-1;
    outSideV = ball.y < hgroundHeight?0:groundHeight-1;
    ball.activate(outSide , outSideV);                    
    setTeamsState(soccerPlayer.PRECORNER);    
    strikeTeam = cornerTeam;    
}



final static byte bestUp = (byte)192;
final static byte bestDown = (byte)64;
byte bestLeft;
byte bestRight;


public void halfMatch(int formationA, int formationB)
{
    firstPass = true;
    humanSoccerPlayer = null;
    ball.activate(hgroundWidth, hgroundHeight);
    strikeTeam = teamB;
    teamDown = TEAMB;	
    int side = (groundWidth / 8);
    int line = (groundHeight / 10);
    for(int i = 0;i < 10;i++)   {    
        teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA);         
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB);         
    }
    gkA.activate(hgroundWidth, 6, TEAMA, hgoalSize);                    
    gkB.activate(hgroundWidth, 6, TEAMB, hgoalSize);
    gkA.state = goalKeeper.FREE;
    gkB.state = goalKeeper.FREE;
    
    bestLeft = (byte)(96+16);
	 bestRight = (byte)(32-16);
}


boolean firstPass;

String namesTeamA[];
int skillsTeamA[][];

String namesTeamB[];
int skillsTeamB[][];

int changesList[];


public void initMatch(int formationA, int formationB)
{
		
    pointer = groundWidth/2;
    firstPass = true;
    goalsA = 0;
    goalsB = 0;
    changesLeft = 3;
    matchState = GAME;
    teamDown = TEAMA;	
    strikeTeam = teamA;
    
    oppAlignment = new byte [playerNames[oppTeam].length];
    
    for(int i = 0; i<oppAlignment.length; i++)
    	oppAlignment[i] = (byte)i;


	bestLeft = (byte)(160-16);
	bestRight = (byte)(224+16);

            
    /*switch (difficulty)
    {
        default:        
            soccerPlayer.ENEMYSPEED = 256;
            soccerPlayer.CHI = 48;
            break;
        case 1:
            soccerPlayer.ENEMYSPEED = 352;
            soccerPlayer.CHI = 32;            
            break;
        case 2:
            soccerPlayer.ENEMYSPEED = 448;
            soccerPlayer.CHI = 16;            
            break;
        case 4:
            soccerPlayer.ENEMYSPEED = 512;
            soccerPlayer.CHI = 0;
            break;        
    }*/
    


    ball = new ball(8,8);
    ball.init(this);
    ball.activate(hgroundWidth, hgroundHeight);
    
    int side = (groundWidth / 8);
    int line = (groundHeight / 10);
    
    for(int i = 0;i < 10;i++)
    {
        teamA[i] = new soccerPlayer(this, i, ball);                
        teamB[i] = new soccerPlayer(this, i, ball);        
        
    }
    gkA = new goalKeeper(this);                
    gkB = new goalKeeper(this);    
    
    assignPlayerData();
    
    for(int i = 0;i < 10;i++)
    {        
        teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA);             
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB);     
    }
    
    gkA.activate(hgroundWidth, 6, TEAMA, hgoalSize);                       
    gkB.activate(hgroundWidth, 6, TEAMB, hgoalSize);
    
    nfc = 0;
    humanSoccerPlayer = null;
    
    changesList = new int[3]; 
    for(int i = 0; i < 3; i++)
    	changesList[i] = -1;
 
 	  	
}

public void assignPlayerData()
{
	int plId;
	
	namesTeamA = new String[11];
	namesTeamB = new String[11];
    skillsTeamA = new int[11][6];    
    skillsTeamB = new int[11][6];    
    
    for(int i = 0; i<11; i++)
    {
    	plId = tour.myAlignment[i];
    	namesTeamA[i] = (plId < 100 ? playerNames[myTeam][plId] : customPlayerNames[plId - 100]);
    	plId = oppAlignment[i];
    	namesTeamB[i] = playerNames[oppTeam][plId];
    	plId = tour.myAlignment[i];
    	
    	for(int j = 0; j<6; j++)
    	{
    		
    		skillsTeamA[i][j] = (plId < 100 ? playerData[myTeam][plId][j] : customPlayerData[plId - 100][j]) + (j>=2 ? 1 : 0);
    		//plId = oppAlignment[i];
    		skillsTeamB[i][j] = playerData[oppTeam][oppAlignment[i]][j] + (j>=2 ? 1 : 0);		
    	}
    		
    	
    	//for(int j = 0; j<6; j++)
    		
    }
    	
    /*for(int i = 0; i<11; i++)
    {
    	plId = oppAlignment[i];
    	namesTeamB[i] = playerNames[oppTeam][plId];
    }
    	
    for(int i = 0; i<11; i++){
    	plId = tour.myAlignment[i];
    	for(int j = 0; j<6; j++)
    		skillsTeamA[i][j] = (plId < 100 ? playerData[myTeam][plId][j] : customPlayerData[plId - 100][j]) + (j>=2 ? 1 : 0);
    }
    	
    for(int i = 0; i<11; i++){
    	plId = oppAlignment[i];
    	for(int j = 0; j<6; j++)
    		skillsTeamB[i][j] = playerData[oppTeam][plId][j] + (j>=2 ? 1 : 0);		
    }*/
		
	for(int i = 0;i < 10;i++)
    {    
       	teamA[i].name = namesTeamA[i+1];
    	teamA[i].setPlayerSkills(skillsTeamA[i+1]);
    	teamB[i].name = namesTeamB[i+1];
        teamB[i].setPlayerSkills(skillsTeamB[i+1]);     
    }
        
    gkA.name = namesTeamA[0];
    gkA.setPlayerSkills(skillsTeamA[0]);
    
    /*for(int i = 0;i < 10;i++)
    {             
        teamB[i].name = namesTeamB[i+1];
        teamB[i].setPlayerSkills(skillsTeamB[i+1]);     
    }*/
    
    gkB.name = namesTeamB[0];
    gkB.setPlayerSkills(skillsTeamB[0]);
    
    namesTeamA = null;
 	skillsTeamA = null;
 	namesTeamB = null;
 	skillsTeamB = null;
 	
 	System.gc();   	    
}


/*public void removeMatch()
{
    ball = null;
    gkA = null;
    gkB = null;
    for(int i = 0;i < 10;i++)
    {
        teamA[i] = null;
        teamB[i] = null;
    }    
}*/

/*int nPenalty;
int nPP;
int restA, restB;

public void beginPenaltyRound()
{
    matchState = PENALTIES;
    nPenalty = 0;
    restA = 5;
    restB = 5;
    int primens = (rnd.nextInt() & 256) ;
    if (primens >= 128)
    {
        strikeTeam = teamA;
        teamDown = TEAMA;
    }
    else
    {
        strikeTeam = teamB;
        teamDown = TEAMB;
    }
    ball.activate(hgroundWidth, groundHeight/8);    
    setTeamsState(soccerPlayer.PREPENALTY);
    gkA.state = soccerPlayer.PREPENALTY;
    gkB.state = soccerPlayer.PREPENALTY;
    
}


public void nextPenalty()
{
    nPenalty++;    
    nPP = (nPenalty/2) % 10;
    if (restA == 0 && restB == 0) {restA = 1;restB = 1;}
    ball.activate(hgroundWidth, groundHeight/8);          
    setTeamsState(soccerPlayer.PREPENALTY);
    gkA.state = soccerPlayer.PREPENALTY;
    gkB.state = soccerPlayer.PREPENALTY;
    if (teamDown == TEAMA)
    {
        restA--;
        strikeTeam = teamB;
        teamDown = TEAMB;
    }
    else
    {
        restB--;
        strikeTeam = teamA;
        teamDown = TEAMA;
    }
    if (goalsA > goalsB + restB || goalsB > goalsA + restA) matchState = ENDGAME;
    //if () playExit = 3;    
}*/

int preMatchState;


public void precorner(soccerPlayer[] _team)
{    
    humanSoccerPlayer = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = WAITCORNER;
    wait = 5;
    gc.soundPlay(5,1);
}

public void preout(soccerPlayer[] _team)
{
    humanSoccerPlayer  = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = WAITOUT;
    wait = 5;
    gc.soundPlay(5,1);
}

public void pregoal(int _team)
{
    humanSoccerPlayer  = null;
    preMatchState = matchState;
    theTeam = _team;
    matchState = WAITGOAL;
    wait = 5;
}



public void matchTick()
{
    if (matchState == GOAL || matchState == ENDGAME) return;
    if (matchState >= WAITCORNER) 
    {
        humanSoccerPlayer = null;
        wait--;
        ball.update();
        if (matchState == WAITGOAL)
        {
             if (ball.x <= hgroundWidth - (hgoalSize) + goalPostSize
             || ball.x > hgroundWidth + (hgoalSize) - goalPostSize)             
             {
                ball.fx -= ball.sx;
                ball.sx = 0;      
             }
             if (ball.y < -9) ball.y = -9;
             if (ball.y > groundHeight+9) ball.y = groundHeight+9;
        }
        if (wait <= 0)
        {
            int tmp = matchState;
            matchState = preMatchState;
            switch(tmp)
            {
                case WAITCORNER:                        
                        corner(theTeam2);                        
                        break;
                case WAITOUT:
                        out(theTeam2);
                        break;
                case WAITGOAL:
                        goal(theTeam);
                        break;        
            }
        }
        return;
    }
    nfc++;    
    //if (keyMenu != 0 && lastKeyMenu == 0) beginPenaltyRound();//halfMatch(3,4);
    if ((nfc+goalSize/4) % goalSize < hgoalSize) pointer+=2;
    else pointer-=2;
    
    ball.update();    
    updatePlayers();	
    
    if (ball.y < 0)
    { 
        if (ball.x > hgroundWidth - (hgoalSize) && ball.x <= hgroundWidth + (hgoalSize))
        {
            if (ball.x <= hgroundWidth - (hgoalSize) + goalPostSize
             || ball.x > hgroundWidth + (hgoalSize) - goalPostSize)
            {
                ball.fy = 0;
                ball.sy = -(ball.sy*65)/100;                
            }
            else 
                pregoal(teamDown);
        }
        else
        {
            //FUERA DE PORTERIA O CORNER
            if (teamDown == TEAMB) 
            {
                if (strikeTeam == teamA)                            
                    precorner(teamB);        
                else {ball.theOwner =null;gkA.takeBall();}
            }
            else
            { 
                if (strikeTeam == teamB)                            
                    precorner(teamA);                    
                else {ball.theOwner =null;gkB.takeBall(); }
            }            
        }
    }
    else if (ball.y >= groundHeight)
    { 
        if (ball.x > (hgroundWidth) - (hgoalSize) && ball.x <= (hgroundWidth) + (hgoalSize))
        {
            if (ball.x <= hgroundWidth - (hgoalSize) + goalPostSize
             || ball.x > hgroundWidth + (hgoalSize) - goalPostSize)
             {
                ball.fy = (groundHeight-1)<<8;             
                ball.sy = -(ball.sy*65)/100;
             }
             else 
                pregoal(1-teamDown);            
        }
        else 
        {            
            //FUERA DE PORTERIA O CORNER
            if (teamDown == TEAMA) 
            {
                if (strikeTeam == teamA)                            
                    precorner(teamB);                        
                else {ball.theOwner =null;gkA.takeBall(); }
            }
            else
            { 
                if (strikeTeam == teamB)                            
                    precorner(teamA);                    
                else {ball.theOwner =null;gkB.takeBall(); }
            }                        
        }
    }
    else if (ball.x < 0 || ball.x >= groundWidth)     preout(strikeTeam);
    
}	


public void setTeamsState(int st)
{
        for(int i = 0;i < 10;i++)
        {        	
            teamA[i].state = st;//setState(st);                    
            teamB[i].state = st;//setState(st);        
            if (st == soccerPlayer.PREMATCH)
            {
            	 teamA[i].formed = false;        
            	 teamB[i].formed = false;        
            }
        }
}

int nformed = 0;
soccerPlayer humanSoccerPlayer;

/*public void updateSoccerPlayer(int ip)
{
    soccerPlayer pUpdate;
    pUpdate = teamA[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;
    pUpdate = teamB[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;        
}*/



public void updatePlayers()
{
    /*if (matchState == PENALTIES)
    {          
        if (strikeTeam[nPP].formed && (gkA.wait == -10 || gkB.wait == -10))
        {
            strikeTeam[nPP].state = soccerPlayer.PENALTY;
            gkA.timing = 0;gkB.timing = 0;
           //System.out.println("bien");
        }
        if (gkA.state == goalKeeper.OWNBALL || gkB.state == goalKeeper.OWNBALL) nextPenalty();
    }*/
    
    for(int i = 0;i < 10;i++)
    {        
        if (teamA[i].state == soccerPlayer.FOLLOW || teamA[i].state == soccerPlayer.OWNBALL/*|| teamA[i].state == soccerPlayer.PENALTY*/) humanSoccerPlayer = teamA[i];                                        
        else 
           teamA[i].update();        
        teamB[i].update();                
    }
        
    gkA.update(ball);
    gkB.update(ball);    
    
    if (humanSoccerPlayer != null) humanSoccerPlayer.manualUpdate(ball, keyX, keyY, keyMenu == 2 && lastKeyMenu == 0);//

   
    
    
    
    
    int ip = nfc%10;
    if (ip == 0) nformed = 0;
    
    soccerPlayer pUpdate;
    pUpdate = teamA[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;
    pUpdate = teamB[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;        
    
    //updateSoccerPlayer(ip*2);
    //updateSoccerPlayer((ip*2)+1);
        
        
    if (nformed >= 19 && strikeTeam[9].state == soccerPlayer.PREMATCH)
    {
    	 gc.soundPlay(5,1);
         setTeamsState(soccerPlayer.FREE); 
         strikeTeam[9].takeBall();               
         /*for (int i = 0;i < 10;i++)
         {
            pUpdate = teamA[i];
            pUpdate.myStatus = pUpdate.status(pUpdate);    
            pUpdate = teamB[i];
            pUpdate.myStatus = pUpdate.status(pUpdate);              
         } */   
    }
    
    //if (matchState != PENALTIES)
    //{
        if (gkA.state != gkA.OWNBALL && gkB.state != gkB.OWNBALL)
        {
            if (nfc%4 == 0 && (humanSoccerPlayer == null || (keyX == 0 && keyY == 0)))//AKI AKI!!!!
            {
                int fPlayer = closestPlayer(teamA);
                if (fPlayer != -1)
                {
                    if (teamA[fPlayer].state == soccerPlayer.FREE)
                    {
                        for(int i = 0; i < 10;i++)
                            if (teamA[i].state == soccerPlayer.FOLLOW) teamA[i].state = soccerPlayer.FREE;
                        teamA[fPlayer].state = soccerPlayer.FOLLOW;//setState(soccerPlayer.FOLLOW);
                    }
                }
            }
            if (nfc%4 == 2)
            {    
                int fPlayer = closestPlayer(teamB);
                if (fPlayer != -1)
                {
                    if (teamB[fPlayer].state == soccerPlayer.FREE)
                    {
                        for(int i = 0; i < 10;i++)
                            if (teamB[i].state == soccerPlayer.FOLLOW) teamB[i].state = soccerPlayer.FREE;
                        teamB[fPlayer].state = soccerPlayer.FOLLOW;//.setState(soccerPlayer.FOLLOW);
                    }
                }
            }
        }
        else 
        {
            //UN PORTERO TIENE LA PELOTA
            setTeamsState(soccerPlayer.FREE);                       
        }
    //}    
}



public int closestPlayer(soccerPlayer team[])
{
    int thePlayer = -1;
    int minDist = INFINITE;
    for(int i = 0; i < 10;i++)
    {
        if (team[i].state == soccerPlayer.WAIT) continue;
        int d = team[i].isBallClose();
        if (d < minDist)
        {
            minDist = d;
            thePlayer = i;
        }
    }
    return thePlayer;
}



public int distance(ball a, ball b)
{
    int dx = a.x - b.x;
    int dy = a.y - b.y;    
    int angle = ball.atan(dy, dx);
    int deno = ball.sin(angle);
    if (deno == 0) return Math.abs(dx);
	int d = ((dy << 10) / deno );      	
    return Math.abs(d);
}


public int angle(ball a, ball b)
{
    int dx = a.x - b.x;
    int dy = a.y - b.y;    
    int angle = ball.atan(dy, dx);
    return (angle);    
}



/*public int distanceX(ball a, ball b)
{
    return Math.abs(a.x - b.x);
}*/
/*
public int distanceY(ball a, ball b)
{
    return Math.abs(a.y - b.y);
}*/

// -------------------
// prefs Update
// ===================


// **************************************************************************//
// Final Clase Game
// **************************************************************************//



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
};

/*
//#ifdef DOJA
class InputPanel extends Panel {

    TextBox tit, subt, fie;     // single line textbox (textfield)    
    Button  b;
    Game theGame;

    InputPanel(Game game,String title, String subtitle, String lab, int numChars) {
    	
    	theGame = game;
    	
    	this.setSoftKeyListener(new SoftKeyListenerClass());
        this.setSoftLabel(Frame.SOFT_KEY_1, lab); 
    
        fie  = new TextBox("", numChars, 1,
                         TextBox.DISPLAY_ANY);
        tit  = new TextBox(title,title.length(),1,TextBox.DISPLAY_ANY); 
        subt  = new TextBox(subtitle,subtitle.length(),2,TextBox.DISPLAY_ANY);
        
        tit.setEditable(false);
        subt.setEditable(false);
        
        tit.setEnabled(false);
        subt.setEnabled(false);
        
        fie.requestFocus();
                
        this.add(tit);        
        this.add(subt);
        this.add(fie);        
                      
    }
    
  
    class SoftKeyListenerClass implements SoftKeyListener { 
    	
    	public void softKeyPressed(int index) {}
    	
		public void softKeyReleased(int index) {
			
          if (index == Frame.SOFT_KEY_1) {
                                       
             theGame.inputDialogNotify(theGame.pan.fie.getText());
             leave();             
             Display.setCurrent(theGame.gc);
          }
       }
    }
    
    public void leave() {

    }     
} 
//#endif
*/