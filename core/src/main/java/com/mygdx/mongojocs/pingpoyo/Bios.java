package com.mygdx.mongojocs.pingpoyo;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================



//import javax.microedition.lcdui.*;
//import javax.microedition.midlet.*;

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
//import com.mygdx.mongojocs.midletemu.Runnable;
//import com.mygdx.mongojocs.midletemu.Thread;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;

//import javax.microedition.rms.*;

// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.sanfermines2006.Game
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------


public class Bios extends MIDlet implements Runnable, CommandListener
{

public GameCanvas gc;
Thread thread;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Bios()
{
	System.gc();

	gc = new GameCanvas( (Game)this );

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
	gameExit = true;
	savePrefs();
	notifyDestroyed();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;
boolean gamePaused = false;

long GameMilis, CPU_Milis;
int  GameSleep;


public void runInit()
{
	biosCreate();
	gc.canvasInit();
	biosInit();
	System.gc();
}

public void runTick()
{
	if (!gamePaused)
	{
		GameMilis = System.currentTimeMillis();

		keyboardTick();
		gc.soundTick();

		//try {
		biosTick();

		gc.gameDraw();

		//} catch (Exception e) {e.printStackTrace();e.toString();}

		GameSleep=(40-(int)(System.currentTimeMillis()-GameMilis));
		if (GameSleep<1) {GameSleep=1;}
		try	{Thread.sleep(GameSleep);} catch(Exception e) {}
	}

	if(gameExit)
		runEnd();
}

public void runEnd()
{
	gc.soundStop();

	biosDestroy();

	destroyApp(true);
}

public void run()
{
	
	biosCreate(); 
	gc.canvasInit(); 
	biosInit(); 
	System.gc();

	while (!gameExit)
	{
		if (!gamePaused)
		{
			GameMilis = System.currentTimeMillis();

			keyboardTick();
			gc.soundTick();

	//try {
			biosTick();

			gc.gameDraw();

	//} catch (Exception e) {e.printStackTrace();e.toString();}

			GameSleep=(40-(int)(System.currentTimeMillis()-GameMilis));
			if (GameSleep<1) {GameSleep=1;}
			try	{Thread.sleep(GameSleep);} catch(Exception e) {}
		}

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
static int RND_Cont=0;
static int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

public static int RND(int Max)
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
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	return bytes;

	/*System.gc();

	InputStream is = getClass().getResourceAsStream(Nombre.substring(1));

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
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void loadFile(byte[] buffer, String Nombre)
{
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	for(int i = 0; i < buffer.length; i++)
		buffer[i] = bytes[i];
/*	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		for(int i = 0; i<buffer.length; i++)
			buffer[i] = (byte)is.read();
		is.close();
	}catch(Exception e) {}
	System.gc();*/
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

			if ((textos[i] < 0x20 && textos[i] >= 0) || textos[i] == 0x7C || textos[i] == 0x7D)	// Buscamos cuando Termina un campo
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

	// MONGOFIX ==========================================
	char textos_char[] = new char[textos.length];
	for(int i = 0; i < textos.length; i++)
		if(textos[i] < 0)
			textos_char[i]=(char)(textos[i]+256);
		else
			textos_char[i]=(char)textos[i];
		//=================================================

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
	int num = data[dataPos];

	if (num<0) {num*=-1;dataPos++;} else {num = 1;}

	strings[i] = new String[num];

	char a = 'á';
	for (int t=0 ; t<num; t++) {
		strings[i][t] = new String(textos_char, data[dataPos++], data[dataPos++]);
		if(strings[i][t].length() < 2) strings[i][t] = " ";
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
boolean gameKeyconf = true;

// -------------------
// bios Create
// ===================

String[][] gameText;

public void biosCreate()
{
	gameText = textosCreate( loadFile("/Textos.txt") );
	gameCreate();
}

// -------------------
// bios Init
// ===================

public void biosInit()
{
	loadPrefs();
	gameInit();
}

// -------------------
// bios Destroy
// ===================

public void biosDestroy()
{
	gameDestroy();
	savePrefs();
}

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

	if (cntLogos == numLogos) {
		gc.canvasImg = null;  //MONGOFIX=========
		gc.canvasFillShow = false; //MONGOFIX=========
		return true;
	}

	gc.canvasFillInit(rgbLogos[cntLogos]);
	gc.canvasImg = gc.loadImage("/Logo"+cntLogos+".png");
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
	if ( (menuListMode == ML_SCROLL && menuListMode == ML_SCREEN) && keyMenu != 1 && keyMenu != lastKeyMenu)
	{
	menuAction(-2);
	}
	else
	if ( menuListTick( (keyY!=lastKeyY? keyY:(keyX!=lastKeyX? keyX:0) ), keyMenu!=lastKeyMenu? keyMenu>0:false ) )
	{
	menuAction(menuListCMD);
	}

	if (menuExit) {return true;}

	if(menuListUpdate) gameUpdate();

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
	menuListAdd(0, Texto[i]);
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
	return Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
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


	if (menuListMode == ML_SCROLL)
	{
		menuListScrY--;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}

		menuListUpdate=true;
	}




	if (menuListMode == ML_SCREEN)
	{
		if (fire || movY != 0)
		{
			menuListBloIni += menuListBloSize;
			menuListBloSize = 0;

			int sizeY = 0;

			for (int i=menuListBloIni ; i<menuListDat.length ; i++)
			{
			if ( (sizeY += menuListDat[i][4]) < menuListSizeY) {menuListBloSize++;} else {break;}
			}

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
	if (!menuList_ON) {return;}
//	if (!menuList_ON || !menuListUpdate) {return;}

	menuListUpdate = false;

	if (menuImg!=null) {gc.canvasImg = menuImg;}


		menuListFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );

		g.setFont(menuListFont);


	switch (menuListMode)
	{
	case ML_OPTION:

		String[] texto = menuListCutText(0, menuListStr[menuListPos][menuListDat[menuListPos][2]]);

		int y = menuListY + (( menuListSizeY / 8) * 7) - ((texto.length * menuListDat[menuListPos][4])/2);

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

		y = 10;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			menuListTextDraw(g, 0, menuListStr[i][0], 0,y);

			y += menuListDat[i][4];

		}


	break;

	}
}






public void menuListTextDraw(Graphics g, int format, String str, int x, int y)
{
	x = menuListX + (( menuListSizeX - menuListFont.stringWidth(str) ) / 2);

	g.setColor(0);
	g.drawString(str, x-1,y-1, 20);
	g.drawString(str, x+1,y-1, 20);
	g.drawString(str, x-1,y+1, 20);
	g.drawString(str, x+1,y+1, 20);

	g.setColor(0xFFFFFF);
	g.drawString(str, x,y, 20);
}







public String[] menuListCutText(int Dato, String Texto)
{
	String[] str = new String[0];

	Gdx.app.log("menuListCutText",Texto);

	int Pos=0, PosIni=0, PosOld=0, Size=0;

	// MONGOFIX =============================================================
	//byte[] Texbytes = Texto.getBytes();
	char[] Tex = new char[Texto.length()];
	Texto.getChars(0, Texto.length(), Tex, 0);
	//=======================================================================

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
		Size += f._charWidth((char)Dat);
	}

	if (PosOld-PosIni < 1) { while ( Pos < Tex.length && Tex[Pos] >= 0x30 ) {Pos++;} PosOld=Pos; }



	int Lines=(str!=null)?str.length:0;

	String newStr[] = new String[Lines+1];

	int i=0;

	for (; i<Lines; i++)
	{
	newStr[i] = str[i];
	}

	str = newStr;

	newStr = null;

	try {
		str[i] = Texto.substring(PosIni, PosOld);
	}catch(Exception e)
	{
		e.printStackTrace();
	}

	PosIni=PosOld+1;

	}

	return str;
}












// <=- <=- <=- <=- <=-








// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

byte[] prefsData;// = new byte[] {1,1,0};

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{
	/*updatePrefs(false);

	gameSound=prefsData[0]!=0;
	gameVibra=prefsData[1]!=0;*/
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	/*prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);

	updatePrefs(true);*/
}

// -------------------
// prefs Update
// ===================

public void updatePrefs(boolean save)
{
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
}

// <=- <=- <=- <=- <=-


// *******************
// -------------------
// inputDialog - Engine
// ===================
// *******************

public static Form inputForm;
public static TextField inputField;
public static String inputDialogLabel;

public void inputDialogCreate(String title, String subtitle, String lab, int numChars)
{

	inputDialogLabel = lab;
	
	Command doneCommand = new Command(lab, Command.OK, 1);
		
	inputForm = new Form(title);
	inputField = new TextField(subtitle, "",numChars,TextField.ANY);
	inputField.setMaxSize(numChars);
	inputForm.append(inputField);   
	inputForm.addCommand(doneCommand);
	inputForm.setCommandListener(this);
		
}

public void inputDialogInit()
{
	/*Display.getDisplay(this).setCurrent(inputForm);*/
}

public void commandAction (Command c, Displayable d)
{
	if(c.getLabel()==inputDialogLabel)
	{								
		Display.getDisplay(this).setCurrent(gc);	
		inputDialogNotify(inputField.getString());
	}  	 	
}

public void inputDialogNotify(String s){}


// **************************************************************************//
// Inicio Clase com.mygdx.mongojocs.sanfermines2006.Game
// **************************************************************************//

int gameStatus;

public void gameCreate() {}
public void gameInit() {}
public void gameDestroy() {}
public void gameTick() {}
public void gameUpdate() {}

public void menuInit(int a) {}
public void menuAction(int a) {}



// **************************************************************************//
// Final Clase com.mygdx.mongojocs.sanfermines2006.Game
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