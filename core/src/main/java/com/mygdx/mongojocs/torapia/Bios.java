package com.mygdx.mongojocs.torapia;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;

import java.io.InputStream;

public class Bios extends MIDlet implements Runnable
{

BiosCanvas gc;
Thread thread;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Bios()
{
	System.gc();

	gc = new BiosCanvas( (Bios)this );

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

	//try {
			biosTick();

			gc.gameDraw();

	//} catch (Exception e) {e.printStackTrace();e.toString();}

			GameSleep=(80-(int)(System.currentTimeMillis()-GameMilis));
			if (GameSleep<1) {GameSleep=1;}
			try	{Thread.sleep(GameSleep);} catch(Exception e) {}
		}

	//System.gc();

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
//final static int TEXT_LOADING = 10;
final static int TEXT_HELP_SCROLL = 11;
final static int TEXT_ABOUT_SCROLL = 12;

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

	// MONGOFIX ==========================================
	char tex_char[] = new char[tex.length];
	for(int i = 0; i < tex.length; i++)
		if(tex[i] < 0)
			tex_char[i]=(char)(tex[i]+256);
		else
			tex_char[i]=(char)tex[i];
	//=================================================

	tmpstr = new String(tex_char);
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
	gameText = textosCreate( loadFile("/Textos.txt") );
	Mapas = gc.explode(gc.FS_LoadFile(6,0));
	EMapas = gc.explode(gc.FS_LoadFile(4,0));
	initRND();
}

// -------------------
// bios Init
// ===================

public void initPrefs()
{
	actSlot = 0;
	saveSlots=1;
	prefsSize=2;
	slotSize = (4/*Nivel*/);
	prefsData = new byte[prefsSize+(saveSlots*slotSize)];
	prefsData[0] = 1;
	prefsData[1] = 1;
}

public void biosInit()
{
	initPrefs();
	loadPrefs();
}

// -------------------
// bios Destroy
// ===================

public void biosDestroy()
{
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

	if (cntLogos == numLogos) {return true;}

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
	//menuImg = gc.loadImage("/Caratula.png");
}



// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if (menuListMode == ML_SCROLL && menuListMode == ML_SCREEN && keyMenu == -1 && keyMenu != lastKeyMenu)
	{
	menuAction(-2);
	}
	else
	if ( menuListTick( (keyY!=lastKeyY? keyY:(keyX!=lastKeyX? keyX:0) ), keyMenu!=lastKeyMenu? keyMenu>0:false ) )
	{
	menuAction(menuListCMD);
	}

	if (menuExit) {return true;}

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

boolean playShow;
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
		menuListScrY -= 2+(5*keyY);
		if (menuListScrY>gc.canvasHeight) menuListScrY = gc.canvasHeight;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}

		menuListUpdate=true;
	}




	if (menuListMode == ML_SCREEN)
	{
		if (movY!=0 || fire)
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
//	if (!menuList_ON) {return;}
	if (!menuList_ON || !menuListUpdate) {return;}

//	if (menuImg!=null) gc.canvasImg = menuImg; else {if (menuListUpdate) playShow = true;}
	if ((menuImg!=null)&&((gameStatus==260)||(gameStatus==100)||(gameStatus==40)||(gameStatus==GAME_MENU_MAIN)||(gameStatus==GAME_MENU_SECOND))) {gc.showImage(menuImg);}

	menuListUpdate = false;

		menuListFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );

		g.setFont(menuListFont);


	switch (menuListMode)
	{
	case ML_OPTION:

		String[] texto = menuListCutText(0, menuListStr[menuListPos][menuListDat[menuListPos][2]]);

		int y = menuListY + (( menuListSizeY * 5) / 6) - ((texto.length * menuListDat[menuListPos][4])/2);

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

	int Pos=0, PosIni=0, PosOld=0, Size=0;

	char[] Tex = Texto.toCharArray(); // MONGOFIX

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

byte actDataPos=0;
public void writeByte(byte data)
{
	prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)] = data;
	actDataPos += 1;
}

public void writeBoolean(boolean data)
{
	if (data) prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)] = 1; else prefsData[((2+(slotSize*actSlot))+actDataPos)] = 0;
	actDataPos += 1;
}

public void writeInt(int data)
{
	prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+0] = (byte)((data >> 0) & 0xFF);
	prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+1] = (byte)((data >> 8) & 0xFF);
	prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+2] = (byte)((data >> 16) & 0xFF);
	prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+3] = (byte)((data >> 24) & 0xFF);
	actDataPos += 4;
}

public byte readByte()
{
	actDataPos += 1;
	return prefsData[((prefsSize+(slotSize*actSlot))+actDataPos-1)];
}

public boolean readBoolean()
{
	actDataPos += 1;
	if (prefsData[((prefsSize+(slotSize*actSlot))+actDataPos-1)] == 0) return false; else return true;
}

public int readInt()
{
	int data=0;
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+3]&0xFF);
	data <<= 8;
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+2]&0xFF);
	data <<= 8;
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+1]&0xFF);
	data <<= 8;
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+0]&0xFF);
	actDataPos += 4;
	return data;
}


public void writeByte(byte data, int pos)
{
	prefsData[pos] = data;
}

public void writeBoolean(boolean data, int pos)
{
	if (data) prefsData[pos] = 1; else prefsData[pos] = 0;
}

public void writeInt(int data, int pos)
{
	prefsData[pos+0] = (byte)((data >> 0) & 0xFF);
	prefsData[pos+1] = (byte)((data >> 8) & 0xFF);
	prefsData[pos+2] = (byte)((data >> 16) & 0xFF);
	prefsData[pos+3] = (byte)((data >> 24) & 0xFF);
}

public byte readByte(int pos)
{
	return prefsData[pos];
}

public boolean readBoolean(int pos)
{
	if (prefsData[pos] == 0) return false; else return true;
}

public int readInt(int pos)
{
	int data=0;
	data+=prefsData[pos+3];
	data <<= 8;
	data+=prefsData[pos+2];
	data <<= 8;
	data+=prefsData[pos+1];
	data <<= 8;
	data+=prefsData[pos];
	return data;
}


int actSlot=0;
int saveSlots;
int prefsSize;
int slotSize;
byte[] prefsData;
// <=- <=- <=- <=- <=-


// -------------------
// prefs Update
// ===================
public void loadPrefs()
{
	prefsData[0]=1;
	prefsData[1]=1;
	
	updatePrefs(false);
	
	gameSound=prefsData[0]!=0;
	gameVibra=prefsData[1]!=0;
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);
	
	updatePrefs(true);
}

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






// **************************************************************************//
// Inicio Clase Game
// **************************************************************************//

final static int TEXT_LEVEL = 13;

// *******************
// -------------------
// game - Engine
// ===================
// *******************


final static int GAME_LOGOS = 0;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
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
int sinoidal=3, incSinus=1;
int timeToChange = 0;
int introTimeToChange=50;
public void gameTick()
{
	if (timeToChange>0) timeToChange--;
	if ((sinoidal<=0)||(sinoidal>=5)) incSinus *= -1;
	sinoidal += incSinus;
	switch (gameStatus)
	{
	case GAME_LOGOS:
		logosInit(1, new int[] {0xFFFFFF}, 2000);
		gameStatus = GAME_MENU_MAIN;
		initPrefs();
		loadPrefs();
		readData();
		menuImg = gc.Imagenes[0][0];
	break;

	case GAME_MENU_MAIN:
		gc.soundPlay(0,0);
		menuInit(MENU_MAIN);
		if (Intro) gameStatus = 40; else gameStatus = 60;
	break;
	
	case 60 : //No Intro
		gameStatus = 70;
		ActualLevel = 0;
	break;
	
	case 40 : //Cargar Imagen collar y diamantes.
		Intro = false;
		ActualLevel=0;
		gc.soundPlay(1,1);
		gameStatus = 41;
		
	case 41 : //Pinta Texto Introductorio 1
		playShow = true;
		menuInit(100);
		gameStatus = 42;
		timeToChange = introTimeToChange;
	break;
	
	case 42 : //Pinta Collar con todos los diamantes
		if ((timeToChange==0)||((keyMenu!=0)&&(lastKeyMenu==0))) gameStatus = 43;
		playShow = true;
	break;
	
	case 43 : //Pinta Texto Introductorio 2
		playShow = true;
		menuInit(110);
		gameStatus = 44;
		timeToChange = introTimeToChange;
	break;
	
	case 44 : //Pinta Collar con diamantes dispersandose
		if ((timeToChange==0)||((keyMenu!=0)&&(lastKeyMenu==0))) gameStatus = 45;
		playShow = true;
	break;
	
	case 45 : //Pinta Texto Introductorio 3
		playShow = true;
		menuInit(120);
		gameStatus = 70;
	break;

	case 70 :
		playCreate();
		menuInit(101+(ActualLevel/7));
		gameStatus = 80;
	break;
	
	case 80: //Vacia memoria y prepara loading
		gameStatus = 85;
		playShow = true;
	break;

	case 85: // Loading
		playShow = true;
		initRND();
		gc.soundStop();
		System.gc();
		if (LevelChange) timeToChange = 30; else timeToChange = 0;
		gameStatus = 90;

	case 90: // Level Init
		if (timeToChange==0)
		{
			playInit();
			gc.soundPlay(2,1);
			playExit=0;
			timeToChange = 5;
			gameStatus = 95;
		}
		playShow = true;
	break;	
	
	case 95:
		if (((lastKeyX==0)&&(keyX!=0))||((lastKeyY==0)&&(keyY!=0))||
		    ((lastKeyMisc==0)&&(keyMisc!=0))||((lastKeyMenu==0)&&(keyMenu!=0))) gameStatus = 100;
		playShow = true;
	break;

	case 100: // Play
		testCheat();
		if ((keyMisc==10)&&(cheatActive))
		{
			gameStatus = 490;
			gc.soundPlay(3,1);
		}
	
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}

		if ( !playTick() ) {break;}

		playExit=0;
		playShow = true;
		
		if (timeToChange == 0) gameStatus = 75;
	break;


	case 250 : //Dead se para todo y el personaje empieza a dar vueltas sobre si mismo
		playShow = true;
		Moving = false;
		ToroM = false;
		Band = false;
		LevelChange = false;
		ProtD++;
		ProtD%=4;
		if (timeToChange == 0) 
		{
			if (--Lives<=0) 
			{
				gameStatus = 260;
				playShow = false;
				timeToChange = 50;
			} else 
			{
				if (LevelChange) timeToChange = 30; else timeToChange = 1;
				gameStatus = 90;
				if (EnemRemain==0) 
				{
					gameStatus = 500;
				}
			}
		}
	
	break;	

	case 260 : //Game Over
		playShow = true;
		if (timeToChange==49)
		{
			gc.soundPlay(8,1);
			playExit = 3;
			LevelChange = true;
		} else
		{
			if ((timeToChange == 0)||(((lastKeyX==0)&&(keyX!=0))||((lastKeyY==0)&&(keyY!=0))||
		    		((lastKeyMisc==0)&&(keyMisc!=0))||((lastKeyMenu==0)&&(keyMenu!=0))))
			{
				gameStatus = GAME_MENU_MAIN;
				gc.soundStop();
			}
		}
	break;	
	
	case GAME_MENU_SECOND:
		menuInit(MENU_SECOND);
		gameStatus = 100;
	break;

	case 300 : // Carga Imagen collar y diamantes
		gc.soundPlay(1,1);
		if (ActualLevel==49) gameStatus = 400; else gameStatus = 310;
		timeToChange = introTimeToChange;
	break;
		
	case 310 : //Pinta Collar con diamantes hasta ahora conseguidos
		if ((timeToChange<=0)||((keyMenu!=0)&&(lastKeyMenu==0)))
		{
			menuInit(101+(ActualLevel/7));
			gameStatus = 80;
		}
		playShow = true;
	break;

	case 400 : // Pinta collar completo con destellitos de luz
		if (timeToChange<=0) gameStatus = 450;
		playShow = true;
	break;
	
	case 450 : //Pinta texto final
		playShow = true;
		menuInit(130);
		gameStatus = 460;
	break;
	
	case 460 : //Pinta creditos final
		gc.soundPlay(0,0);
		playShow = true;
		gameStatus = 40;
		menuInit(MENU_SCROLL_ABOUT);
		menuTypeBack = MENU_MAIN;
	break;
	
	case 490 :
		playShow = true;
		Moving = false;
		ToroM = false;
		Band = false;
		ProtF++;
		ProtF%=2;
		ProtD = 3;
		if (timeToChange <= 0) gameStatus = 500;
	break;
	
	case 500 : // Pasa de Nivel
		LevelChange = true;
		ActualLevel++;
		if ((ActualLevel%7)==0) 
		{
			gameStatus = 300; 
		} else gameStatus = 80;
		
		if (ActualLevel<49) MaxScore = ActualLevel;
		timeToChange = 30;
		if (MaxScore>RMaxScore)
		{
			NewRecord = true;
			RMaxScore = MaxScore;
		}
		writeData();
	break;
	
	case 600 : // Preparar Seleccion Nivel
		timeToChange = 0;
		SelectedLevel = RMaxScore+1;
		gameStatus = 610;
	case 610 : // Seleccion Nivel
		playShow = true;
		if ((lastKeyX==0)&&(keyX>0))
		{
			SelectedLevel++;
		}
		if ((lastKeyX==0)&&(keyX<0))
		{
			SelectedLevel--;
		}
		if ((lastKeyY==0)&&(keyY>0))
		{
			if (SelectedLevel>0) SelectedLevel = (((SelectedLevel-1)/7)+1)*7+1; else SelectedLevel = 1;
		}
		if ((lastKeyY==0)&&(keyY<0))
		{
			if ((SelectedLevel-1)%7==0) SelectedLevel = (((SelectedLevel-1)/7)-1)*7+1; else SelectedLevel = ((SelectedLevel-1)/7)*7+1;
		}
		
		if (SelectedLevel<0) SelectedLevel=0;
		if (SelectedLevel>RMaxScore+1) SelectedLevel=RMaxScore+1;
		
		if ((keyMenu==-1)&&(lastKeyMenu==0))
		{
			SelectedLevel = 0;
		}
		
		if ((keyMenu!=0)&&(lastKeyMenu==0))
		{
			if (SelectedLevel>0)
			{
				ActualLevel = (SelectedLevel-1);
				//menuImg = null;
				gameStatus = 70;
			} else
			{
				gameStatus = GAME_MENU_MAIN;
			}
		}
	break;
	}

}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

boolean Record = false;
public void writeData()
{
	actDataPos=0;
	writeInt(RMaxScore);
	updatePrefs(true);
}

public void readData()
{
	updatePrefs(false);
	actDataPos=0;
	RMaxScore 	= readInt();
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


static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_CONTINUE_FROM = 8;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;

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

	menuListInit(0, 0, gc.canvasWidth, gc.canvasHeight);


	switch (type)
	{
	case MENU_MAIN:
		menuListClear();
		if (RMaxScore>0) menuListAdd(0, gameText[TEXT_SCORES][0], MENU_ACTION_CONTINUE_FROM);
		menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_SECOND:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_SCROLL_HELP:
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Scroll();
	break;


	case MENU_SCROLL_ABOUT:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Scroll();
	break;
	
	case 100 :
		menuListClear();
		menuListAdd(0, gameText[TEXT_INTRO1]);
		menuListSet_Scroll();
	break;
	
	case 110 :
		menuListClear();
		menuListAdd(0, gameText[TEXT_INTRO2]);
		menuListSet_Scroll();
	break;
	
	case 120 :
		menuListClear();
		menuListAdd(0, gameText[TEXT_INTRO3]);
		menuListSet_Scroll();
	break;
	
	case 130 :
		menuListClear();
		menuListAdd(0, gameText[TEXT_INTRO4]);
		menuListSet_Scroll();
	break;
	
	case 101 :
	case 102 :
	case 103 :
	case 104 :
	case 105 :
	case 106 :
	case 107 :
		menuListClear();
		menuListAdd(0, gameText[TEXT_FASES][type-101]);
		menuListSet_Scroll();
	break;
	}


	biosStatus = 22;
}

int TEXT_INTRO1 = 18;
int TEXT_INTRO2 = TEXT_INTRO1+1;
int TEXT_INTRO3 = TEXT_INTRO2+1;
int TEXT_INTRO4 = TEXT_INTRO3+1;
int TEXT_SITIOS = TEXT_INTRO4+1;
int TEXT_FASES = TEXT_SITIOS+2;

// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{

	switch (cmd)
	{
	case -3: // Scroll ha sido cortado por usario
	case -2: // Scroll ha llegado al final
		if ((menuType<99)||(menuType>131)) menuInit(menuTypeBack);
		else menuExit = true;
	break;


	case MENU_ACTION_PLAY:		// Jugar de 0
	case MENU_ACTION_CONTINUE:	// Continuar

		menuExit = true;
	break;

	case MENU_ACTION_CONTINUE_FROM:		// Scores...
		gameStatus = 600;
		Record = true;
		menuExit = true;
	break;

	case MENU_ACTION_SHOW_HELP:		// Controles...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// About...

		menuInit(MENU_SCROLL_ABOUT);
	break;



	case MENU_ACTION_RESTART:	// Restart
		gameStatus = 260;
		timeToChange = 50;
		playExit = 3;
		menuExit = true;
	break;


	case MENU_ACTION_EXIT_GAME:	// Exit Game

		gameExit = true;
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) {gc.soundPlay(0,0);}
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListOpt() != 0;
	break;
	}

}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// play - Engine
// ===================
// *******************

int playExit;

// -------------------
// play Create
// ===================

public void playCreate()
{
	gc.canvasFillInit(0);
	LevelChange = true;
	Score = 100;
	ProtV = 4;
	ToroV = 3;
	ToroW = 48;
	ToroH = 48;
	ProtW = 24;
	ProtH = 36;
	TileW = 24;
	TileH = 24;
	ExplW = 32;
	ExplH = 32;
	Lives = 3;
	BandW = 24;
	BandH = 30;
	/*int MaxExpl=5;
	int MaxItem=1;
	ExplX = new int[MaxExpl];
	ExplY = new int[MaxExpl];
	ExplF = new int[MaxExpl];
	ExplV = new boolean[MaxExpl];
	ItemCX = new int[MaxItem];
	ItemCY = new int[MaxItem];
	ItemT = new int[MaxItem];
	ItemV = new boolean[MaxItem];*/
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

byte []Mapas;
byte []EMapas;
public void playInit()
{
	cargaEnem();
	if (LevelChange) 
	{
		MapInfo = AdaptaMapa(Mapas);
		gc.playInit_Gfx();
	}
	sinoidal = 2;
	incSinus = 1;
	ToroMT = 0;
	ProtMT = 0;
	ToroM = false;
	Moving = false;
	Band = false;
	int MaxExpl=5;
	int MaxItem=1;
	ExplX = new int[MaxExpl];
	ExplY = new int[MaxExpl];
	ExplF = new int[MaxExpl];
	ExplV = new boolean[MaxExpl];
	ItemCX = new int[MaxItem];
	ItemCY = new int[MaxItem];
	ItemT = new int[MaxItem];
	ItemV = new boolean[MaxItem];
	gc.gameDraw();
	waitTime(10);
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

byte sCheat[] = {8,6,6,6,7,7,7,6,6,6,-6};
boolean cheatActive = false;
//byte sCheat[];
int iCheat;

public void testCheat()
{		
    if(sCheat[iCheat] == -6){cheatActive = true;iCheat = 0;RMaxScore = 48;}
    if (keyMisc == 0) return;
    //System.out.println(""+iCheat+" - "+keyMisc);		
	if (sCheat[iCheat] == keyMisc && lastKeyMisc == 0) 
	{
		
		iCheat++;
	}
	else if (sCheat[iCheat] != keyMisc && lastKeyMisc == 0) iCheat = 0;
}


public boolean playTick()
{
	timeToChange = 40; 
	MoveProt();
	MoveEnem();
	lookEnemWithToro();
	if (ColisionaEnemigos()) 
	{
		gc.vibraInit(250);
		gc.soundPlay(7,1);
		gameStatus = 250;
	}
			
	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}

public boolean PasoLibre(int CX, int CY, int D)
{
	D %= 4;
	if (
		((D==0)&&(Colision[CX+1+((CY)*NumTilesX)]==0))||
		((D==2)&&(Colision[CX-1+((CY)*NumTilesX)]==0))||
		((D==1)&&(Colision[CX+((CY-1)*NumTilesX)]==0))||
		((D==3)&&(Colision[CX+((CY+1)*NumTilesX)]==0))
	   ) return true; else return false;
}

public boolean PasoLibreE(int CX, int CY, int D)
{
	D %= 4;
	if (
		((D==0)&&(Colision[CX+1+((CY)*NumTilesX)]==0))||
		((D==2)&&(Colision[CX-1+((CY)*NumTilesX)]==0))||
		((D==1)&&(Colision[CX+((CY-1)*NumTilesX)]==0))||
		((D==3)&&(Colision[CX+((CY+1)*NumTilesX)]==0))||
		((D==0)&&(Colision[CX+1+((CY)*NumTilesX)]>=4))||
		((D==2)&&(Colision[CX-1+((CY)*NumTilesX)]>=4))||
		((D==1)&&(Colision[CX+((CY-1)*NumTilesX)]>=4))||
		((D==3)&&(Colision[CX+((CY+1)*NumTilesX)]>=4))
	   ) return true; else return false;
}

public boolean ColisionaEnemigos()
{
	if (Colision[ProtCX+ProtCY*NumTilesX]>=4) return true;
	return false;
}

public void MoveProt()
{
	if (Moving)
	{
		ProtF++;
		ProtF%=3;
		ProtMT--;
		if (ProtD==0) ProtX += (TileW/ProtV);
		if (ProtD==2) ProtX -= (TileW/ProtV);
		if (ProtD==1) ProtY -= (TileH/ProtV);
		if (ProtD==3) ProtY += (TileH/ProtV);
		
		int CX,CY;
		CX = ProtCX;
		CY = ProtCY;
		ProtCX = ProtX/TileW;
		ProtCY = ProtY/TileH;
		if ((Band)&&((CX!=ProtCX)||(CY!=ProtCY)))
		{
			Colision2[ProtCX+ProtCY*NumTilesX]=1;
			AddRecorrido(ProtD);
		}
		
		if (ProtMT <= 0)
		{
			ProtX = ProtCX*TileW+TileW/2;
			ProtY = ProtCY*TileH+TileH/2;
			Moving = false;
		}
		for (int i=0; i<ItemV.length; i++)
		{
			if ((ItemV[i])&&(ProtCX==ItemCX[i])&&(ProtCY==ItemCY[i]))
			{
				ItemV[i] = false;
				if (ItemT[i]==8)
				{
					Lives++;
					gc.soundPlay(4,1);
				} else
				{
					gameStatus = 490;
					gc.soundPlay(3,1);
				}
			}
		}
	}
	
	if (ProtMT==0)
	{
		if (ToroM == true)
		{
			ProtF++;
			ProtF%=2;
			ToroF++;
			ToroF%=3;
			if (ToroMT>ToroV)
			{
				ToroMT--;
				if (ToroMT==ToroV) CreateExpl(ToroX,ToroY);
			}
			if ((ToroMT>0)&&(ToroMT<=ToroV))
			{
				ToroMT--;
				if (ToroD==0) ToroX += (TileW/ToroV);
				if (ToroD==2) ToroX -= (TileW/ToroV);
				if (ToroD==1) ToroY -= (TileH/ToroV);
				if (ToroD==3) ToroY += (TileH/ToroV);
				
				int CX,CY;
				CX = ToroCX;
				CY = ToroCY;
				ToroCX = ToroX/TileW;
				ToroCY = ToroY/TileH;
				
				if (ToroMT <= 0)
				{
					ToroX = ToroCX*TileW+TileW/2;
					ToroY = ToroCY*TileH+TileH/2;
				}
			} 
			if (ToroMT==0)
			{	
				ToroActD++;
				if (Recorrido[ToroActD]==-1)
				{
					Band = false;
					ToroM = false;
					InitRecorrido();
				} else
				{
					ToroD = Recorrido[ToroActD];
					ToroMT = ToroV;
				}
			}
		} else
		{
			if (((keyMisc==5)&&(lastKeyMisc!=5))||((keyMenu>0)&&(lastKeyMenu==0)))
			{
				if (Band)
				{
					gc.soundPlay(5,1);
					ToroCX = BandCX;
					ToroCY = BandCY;
					ToroX = ToroCX*TileW+TileW/2;
					ToroY = ToroCY*TileH+TileH/2;
					ToroD = Recorrido[ToroActD];
					ToroMT = ToroV+3;
					gc.vibraInit(150);
					ToroM = true;
					ProtD += 2;
					ProtD %= 4;
				} else
				{
					gc.soundPlay(4,1);
					InitRecorrido();
					Band = true;
					ToroActD = 0;
					BandCX = ProtCX;
					BandCY = ProtCY;
					Colision[ProtCX+ProtCY*NumTilesX] = 3;
					Colision2[ProtCX+ProtCY*NumTilesX] = 1;
				}
				
			}
			if ((keyY==1)&&((Colision[ProtCX+NumTilesX*(ProtCY+1)]==0)||(Colision[ProtCX+NumTilesX*(ProtCY+1)]>=4))&&(Colision2[ProtCX+NumTilesX*(ProtCY+1)]==0))
			{
				ProtD = 3;
				Moving = true;
				ProtMT = ProtV;
			}
			if ((keyY==-1)&&((Colision[ProtCX+NumTilesX*(ProtCY-1)]==0)||(Colision[ProtCX+NumTilesX*(ProtCY-1)]>=4))&&(Colision2[ProtCX+NumTilesX*(ProtCY-1)]==0))
			{
				ProtD = 1;
				Moving = true;
				ProtMT = ProtV;
			}
			if ((keyX==-1)&&((Colision[ProtCX-1+NumTilesX*(ProtCY)]==0)||(Colision[ProtCX-1+NumTilesX*(ProtCY)]>=4))&&(Colision2[ProtCX-1+NumTilesX*(ProtCY)]==0))
			{
				ProtD = 2;
				Moving = true;
				ProtMT = ProtV;
			}
			if ((keyX==1)&&((Colision[ProtCX+1+NumTilesX*(ProtCY)]==0)||(Colision[ProtCX+1+NumTilesX*(ProtCY)]>=4))&&(Colision2[ProtCX+1+NumTilesX*(ProtCY)]==0))
			{
				ProtD = 0;
				Moving = true;
				ProtMT = ProtV;
			}
		}
	}
}

public void MoveEnem()
{
	for (int i = 0; i<Colision.length; i++)
	{
		if (Colision[i] != 1) Colision[i] = 0;
	}
	for (int i = 0; i<EnemX.length; i++)
	{
		if (EnemA[i])
		{
			if (Colision[EnemCX[i]+EnemCY[i]*NumTilesX]!=1) Colision[EnemCX[i]+EnemCY[i]*NumTilesX] = (byte)(4+i);
			//if ((EnemM[i])&&(EnemMT[i]<EnemV[i])&&((EnemMT[i]>=(EnemV[i]/2)&&((EnemD[i]==1)||(EnemD[i]==2)))||(EnemMT[i]>(EnemV[i]/2)&&((EnemD[i]==0)||(EnemD[i]==3)))))
			switch (EnemD[i])
			{
				case 0:
					if ((Colision[EnemCX[i]+1+EnemCY[i]*NumTilesX]!=1)&&(EnemMT[i]<EnemV[i])&&(((TileW/EnemV[i])*EnemMT[i])>(TileW/2)))
						Colision[EnemCX[i]+1+EnemCY[i]*NumTilesX] = (byte)(4+i);
				break;
				case 1:
					if ((Colision[EnemCX[i]-1+EnemCY[i]*NumTilesX]!=1)&&(EnemMT[i]<EnemV[i])&&(((TileH/EnemV[i])*EnemMT[i])>(TileH/2)))
						Colision[EnemCX[i]+(EnemCY[i]-1)*NumTilesX] = (byte)(4+i);
				break;
				case 2:
					if ((Colision[EnemCX[i]+(EnemCY[i]-1)*NumTilesX]!=1)&&(EnemMT[i]<EnemV[i])&&(((TileW/EnemV[i])*EnemMT[i])>(TileW/2)))
						Colision[EnemCX[i]-1+EnemCY[i]*NumTilesX] = (byte)(4+i);
				break;
				case 3:
					if ((Colision[EnemCX[i]+(EnemCY[i]+1)*NumTilesX]!=1)&&(EnemMT[i]<EnemV[i])&&(((TileH/EnemV[i])*EnemMT[i])>(TileH/2)))
						Colision[EnemCX[i]+(EnemCY[i]+1)*NumTilesX] = (byte)(4+i);
				break;
			}
		}
	}
	if ((Band)&&(BandCX==ProtCX)&&(BandCY==ProtCY)) Colision[BandCX+BandCY*NumTilesX] = 3;
	for (int i = 0; i<EnemX.length; i++)
	{
		if (EnemA[i])
		{
			if ((EnemM[i])&&((EnemT[i]!=6)||(Band)))
			{
				EnemF[i]++;
				EnemF[i]%=3;
				EnemMT[i]--;
				if (EnemD[i]==0) EnemX[i] += (TileW/EnemV[i]);
				if (EnemD[i]==2) EnemX[i] -= (TileW/EnemV[i]);
				if (EnemD[i]==1) EnemY[i] -= (TileH/EnemV[i]);
				if (EnemD[i]==3) EnemY[i] += (TileH/EnemV[i]);
				
				
				//int CX,CY;
				//CX = EnemCX[i];
				//CY = EnemCY[i];
				
				EnemCX[i] = EnemX[i]/TileW;
				EnemCY[i] = EnemY[i]/TileH;
				
				if (EnemA[i])
				{
					if (Colision[EnemCX[i]+EnemCY[i]*NumTilesX]!=1) Colision[EnemCX[i]+EnemCY[i]*NumTilesX] = (byte)(4+i);
/*					if ((EnemM[i])&&(EnemMT[i]<EnemV[i])&&(EnemMT[i]>=(EnemV[i]/2)))
					switch (EnemD[i])
					{
						case 0:
							if (Colision[EnemCX[i]+1+EnemCY[i]*NumTilesX]!=1)
								Colision[EnemCX[i]+1+EnemCY[i]*NumTilesX] = (byte)(4+i);
						break;
						case 1:
							if (Colision[EnemCX[i]-1+EnemCY[i]*NumTilesX]!=1)
								Colision[EnemCX[i]+(EnemCY[i]-1)*NumTilesX] = (byte)(4+i);
						break;
						case 2:
							if (Colision[EnemCX[i]+(EnemCY[i]-1)*NumTilesX]!=1)
								Colision[EnemCX[i]-1+EnemCY[i]*NumTilesX] = (byte)(4+i);
						break;
						case 3:
							if (Colision[EnemCX[i]+(EnemCY[i]+1)*NumTilesX]!=1)
								Colision[EnemCX[i]+(EnemCY[i]+1)*NumTilesX] = (byte)(4+i);
						break;
					}*/
					if (((ToroM==false)||(ToroCX!=BandCX)||(ToroCY!=BandCY))&&(Band)&&(BandCX==EnemCX[i])&&(BandCY==EnemCY[i])) 
					{
						CreateExpl(BandCX*TileW+TileW/2, BandCY*TileH+TileH/2);
						if (ToroM) CreateExpl(ToroX, ToroY);
						Band = false;
						ToroM = false;
						InitRecorrido();
						gc.vibraInit(150);
						gc.soundPlay(4,1);
					}
				}
				if (EnemMT[i] <= 0)
				{
					EnemX[i] = EnemCX[i]*TileW+TileW/2;
					EnemY[i] = EnemCY[i]*TileH+TileH/2;
					EnemM[i] = false;
				}
				
			} 
			if (EnemMT[i]==0)
			{
				switch (EnemT[i])
				{
					case 0:
						if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]+3))) 
						{
							 if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i])))
							 {
							 	if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]+1)))
							 	{
							 		EnemD[i]+=2;
							 	} else EnemD[i]++;
							 }
						} else EnemD[i]+=3;
					break;
					
					case 7:
						if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]+1))) 
						{
							 if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i])))
							 {
							 	if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]+3)))
							 	{
							 		EnemD[i]+=2;
							 	} else EnemD[i]+=3;
							 }
						} else EnemD[i]++;
					break;
					
					case 4:
						if (EnemCY[i]<ProtCY) EnemD[i] = 3; else EnemD[i] = 1;
						if ((Math.abs(EnemCX[i]-ProtCX)>Math.abs(EnemCY[i]-ProtCY))||(!PasoLibre(EnemCX[i],EnemCY[i],EnemD[i])))
						{
							if ((EnemCX[i]<ProtCX)&&(PasoLibre(EnemCX[i],EnemCY[i],0))) EnemD[i] = 0;
							if ((EnemCX[i]>ProtCX)&&(PasoLibre(EnemCX[i],EnemCY[i],2))) EnemD[i] = 2;
						}
					break;
					
					case 6:
						if (Band)
						{
							if (EnemCY[i]<BandCY) EnemD[i] = 3; else EnemD[i] = 1;
							if ((Math.abs(EnemCX[i]-BandCX)>Math.abs(EnemCY[i]-BandCY))||(!PasoLibre(EnemCX[i],EnemCY[i],EnemD[i])))
							{
								if ((EnemCX[i]<BandCX)&&(PasoLibre(EnemCX[i],EnemCY[i],0))) EnemD[i] = 0;
								if ((EnemCX[i]>BandCX)&&(PasoLibre(EnemCX[i],EnemCY[i],2))) EnemD[i] = 2;
							}
						}
					break;
					
					case 9:
						if ((i%2)==0)
						{
							if (ProtD == 0) EnemD[i] = 0;	
							if (ProtD == 2) EnemD[i] = 2;	
							if (ProtD == 1) EnemD[i] = 3;	
							if (ProtD == 3) EnemD[i] = 1;	
						} else
						{
							if (ProtD == 0) EnemD[i] = 2;	
							if (ProtD == 2) EnemD[i] = 0;	
							if (ProtD == 1) EnemD[i] = 1;	
							if (ProtD == 3) EnemD[i] = 3;	
						}
					break;
					
					case 5:
						if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]+3))) 
						{
							 if (!(PasoLibreE(EnemCX[i],EnemCY[i],EnemD[i])))
							 {
							 	if (!(PasoLibreE(EnemCX[i],EnemCY[i],EnemD[i]+1)))
							 	{
							 		EnemD[i]+=2;
							 	} else EnemD[i]++;
							 }
						} else EnemD[i]+=3;
					break;
					
					case 3:
						if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]))) EnemD[i]++;
					break;
					
					case 8:
					case 2:
					case 1:
					default :
						if (!(PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]))) EnemD[i]+=2;
					break;	
				}
				EnemD[i]%=4;
				if (PasoLibre(EnemCX[i],EnemCY[i],EnemD[i]))
				{
					EnemM[i] = true;
					EnemMT[i] = EnemV[i];
				}
			}
		}
	}
}

public void MataEnem(int Enem)
{
	if (EnemA[Enem])
	{
		EnemA[Enem] = false;
		EnemRemain--;
		CreateExpl(EnemX[Enem],EnemY[Enem]);
		gc.soundPlay(6,1);
		//for (int P=0; P<Colision.length; P++) if (Colision[P] == 2) Colision[P] = 0;
		if (EnemRemain==0)
		{
			if (((ActualLevel+1)%7)==0) CreateItem((ActualLevel/7)+1,EnemCX[Enem],EnemCY[Enem]); else 
			{
				gc.soundPlay(3,1);
				gameStatus = 490;
			}
		}
		if ((EnemRemain>0)&&(RND(10)<(3-Lives))) CreateItem(8,EnemCX[Enem],EnemCY[Enem]);
	}
}

public void lookEnemWithToro()
{
	if (ToroM)
	{
		int ToroCX2 = ToroCX;
		int ToroCY2 = ToroCY;			
		switch (ToroD)
		{
			case 0 :
				if ((ToroMT<ToroV)&&(ToroMT>ToroV/2)) ToroCX2++;
			break;
			case 1 :
				if ((ToroMT<ToroV)&&(ToroMT>ToroV/2)) ToroCY2--;
			break;
			case 2 :
				if ((ToroMT<ToroV)&&(ToroMT>ToroV/2)) ToroCX2--;
			break;
			case 3 :
				if ((ToroMT<ToroV)&&(ToroMT>ToroV/2)) ToroCY2++;
			break;
		}	
		int Who=0;
		if (Colision[ToroCX+ToroCY*NumTilesX]>=4) Who = 1;
		if (Colision[ToroCX2+ToroCY2*NumTilesX]>=4) Who = 2;
		if (Who!=0)
		{
			if (Who==1) MataEnem(Colision[ToroCX+ToroCY*NumTilesX]-4);
			if (Who==2) MataEnem(Colision[ToroCX2+ToroCY2*NumTilesX]-4);
		}
	}
}

public void initEnem(int N)
{
	EnemX = null;
	EnemY = null;
	EnemW = null;
	EnemH = null;
	EnemCX = null;
	EnemCY = null;
	EnemD = null;
	EnemF = null;
	EnemP = null;
	EnemT = null;
	EnemV = null;
	EnemMT = null;
	EnemM = null;
	if (LevelChange) EnemA = null;
	System.gc();
	EnemX = new int[N];
	EnemY = new int[N];
	EnemW = new int[N];
	EnemH = new int[N];
	EnemCX = new int[N];
	EnemCY = new int[N];
	EnemD = new int[N];
	EnemF = new int[N];
	EnemP = new int[N];
	EnemT = new int[N];
	EnemV = new int[N];
	EnemMT = new int[N];
	EnemM = new boolean[N];
	if (LevelChange) EnemA = new boolean[N];
	for (int i=0; i<EnemU.length; i++) EnemU[i] = false;
}

public void cargaEnem()
{
	byte[] newEnems;
	newEnems = AdaptaMapa(EMapas);
	Colision = new byte [newEnems.length];
	Colision2 = new byte [newEnems.length];
	int Recuento=-1;
	for (int i=0; i<newEnems.length; i++)
	{
		if (newEnems[i]==1) Colision[i] = 1; else Colision[i] = 0;
		newEnems[i] -= 16;
		if (newEnems[i] >= 0) 
		{
			Recuento++;
		} else
		{
			newEnems[i]=-1;	
		}
	}
	initEnem(Recuento);
	EnemRemain = 0;
	int ActEnem=0;
	for (int i=0; i<newEnems.length; i++)
	{
		if ((newEnems[i]>=0)&&(newEnems[i]<40))
		{
			EnemW[ActEnem] = EW[(newEnems[i]/4)];
			EnemH[ActEnem] = EH[(newEnems[i]/4)];
			EnemV[ActEnem] = EV[(newEnems[i]/4)];
			EnemCX[ActEnem] = (i%NumTilesX);
			EnemCY[ActEnem] = (i/NumTilesX);
			EnemD[ActEnem] = newEnems[i]%4;
			EnemF[ActEnem] = 0;
			EnemP[ActEnem] = 0;
			EnemT[ActEnem] = newEnems[i]/4;
			EnemU[EnemT[ActEnem]] = true;
			EnemX[ActEnem] = ((EnemCX[ActEnem]*TileW)+(TileW/2));
			EnemY[ActEnem] = ((EnemCY[ActEnem]*TileH)+(TileH/2));
			EnemMT[ActEnem] = 0;
			EnemM[ActEnem] = false;
			if (LevelChange) EnemA[ActEnem] = true;
			if (EnemA[ActEnem])
			{
				EnemRemain++;
				//Colision[EnemCX[ActEnem]+EnemCY[ActEnem]*NumTilesX] = 2;
			}
			
			ActEnem++;
		}
		if (newEnems[i]>=40)
		{
			ProtCX = (i%NumTilesX);
			ProtCY = (i/NumTilesX);
			ProtD = newEnems[i]%4;
			ProtX = ((ProtCX*TileW)+(TileW/2));
			ProtY = ((ProtCY*TileH)+(TileH/2));
		}
	}
}


public byte[] AdaptaMapa(byte []Map)
{
	byte []TempInfo = new byte[NumTilesX*NumTilesY];
	for (int i=0; i<TempInfo.length; i++)
	{
		TempInfo[i] = Map[i+(ActualLevel*NumTilesX*NumTilesY)];
	}
	return TempInfo;
}

public void InitRecorrido()
{
	for (int i=0; i<Recorrido.length;i++) Recorrido[i] = -1;
	for (int i=0; i<Colision2.length;i++) Colision2[i] = 0;
}

public void AddRecorrido(int D)
{
	for (int i=0; i<Recorrido.length;i++) 
	  if (Recorrido[i]==-1) 
	  {
	  	Recorrido[i] = D;
	  	return;
	  }
}

public void CreateExpl(int X, int Y)
{
	int Top=0;
	for (int Counter=0; Counter<ExplV.length; Counter++)
	{
		if (ExplV[Counter]==false)
		{
			Top = Counter;
			Counter = ExplV.length;
		} else
		{
			if (ExplF[Counter] > ExplF[Top]) Top = Counter;
		}
	}
	ExplX[Top] = X;
	ExplY[Top] = Y;
	ExplF[Top] = 0;
	ExplV[Top] = true;
}

public void CreateItem(int T, int CX, int CY)
{
	int Top=0;
	for (int Counter=0; Counter<ItemV.length; Counter++)
	{
		if (ItemV[Counter]==false)
		{
			Top = Counter;
			Counter = ItemV.length;
		} else
		{
			if (ItemT[Counter] > ItemT[Top]) Top = Counter;
		}
	}
	ItemCX[Top] = CX;
	ItemCY[Top] = CY;
	ItemT[Top] = T;
	ItemV[Top] = true;
}

// **************************************************************************//
// Final Clase Game
// **************************************************************************//
boolean LevelChange = false;
int ActualLevel=-1;
int Score = 0;
int MaxScore = 0;
boolean NewRecord = false;
int RMaxScore = 0;
int TEXT_SCORES = TEXT_LEVEL+2;
int TEXT_LOADING = TEXT_SCORES+2;

int[] Recorrido = new int[11*11];
int[] EW = new int[] {24,24,24,24,24,24,24,24,24,24};
int[] EH = new int[] {36,36,36,36,36,36,36,36,36,36};
int[] EV = new int[] {6,6,4,6,6,7,7,4,3,4};
int ProtX,ProtY,ProtW,ProtH,ProtCX,ProtCY,ProtD,ProtF,ProtMT,ProtV;
int[] EnemX,EnemY,EnemW,EnemH,EnemCX,EnemCY,EnemD,EnemF,EnemP,EnemT,EnemMT,EnemV;
boolean []EnemM,EnemA;
int TileW,TileH,ExplW,ExplH;
byte Lives;
int NumTilesX = 13;
int NumTilesY = 13;
boolean Moving;
int ToroX,ToroY,ToroCX,ToroCY,ToroF,ToroD,ToroMT,ToroV,BandCX,BandCY,ToroW,ToroH,BandW,BandH,ToroActD;
boolean ToroM,Band;
byte []MapInfo;
byte[] Colision,Colision2;
int []ExplX,ExplY,ExplF;
boolean []ExplV,ItemV;
int EnemRemain=0;
int[] ItemCX,ItemCY,ItemT;
boolean[] EnemU = new boolean[10];
int SelectedLevel=0;
boolean Intro = true;
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