package com.mygdx.mongojocs.poyoolimpix;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// Adaptacion iMode Rev.2 (1.4.2004)
// ------------------------------------




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

import java.io.InputStream;

//#ifdef DOJA
//#else
public class Bios extends MIDlet implements Runnable, CommandListener
{

GameCanvas gc;
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
//
	//#ifndef NODISPLAY
	Display.getDisplay(this).setCurrent(gc);
	//#endif
	gamePaused=false;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// pauseApp - Cada vez que se PAUSA el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void pauseApp() {/*gamePaused=true;*/ if (gameStatus==60/*GAME_PLAY*/) gameStatus = 24/*GAME_MENU_INIT_SECOND*/;}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// destroyApp - Para DESTRUIR el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	biosDestroy();
	gameExit = true;
	notifyDestroyed();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//#endif
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;
boolean gamePaused = false;
boolean playPaused = false;

long GameMilis, CPU_Milis, PlayMilis, PausedMilis;
int  GameSleep;


public void run()
{
	System.gc();
	biosCreate(); System.gc();
	gc.canvasInit(); System.gc();
	biosInit(); System.gc();
	CPU_Milis = System.currentTimeMillis();
	GameMilis = CPU_Milis;
	PausedMilis = 0;
	
	while (!gameExit)
	{
		if (!gamePaused)
		{
			CPU_Milis = System.currentTimeMillis();
			if (playPaused)
			{
				PausedMilis += CPU_Milis-GameMilis;
			}
			
			GameMilis = CPU_Milis;
			PlayMilis = GameMilis-PausedMilis;

			keyboardTick();
			gc.soundTick();

	//try {
			biosTick();
			//#ifdef MO-C450
			System.gc();
			//#endif
			gc.gameDraw();

	//} catch (Exception e) {System.out.println("*** Exception en la parte L�gica ***"); e.printStackTrace();}

			GameSleep=(SleepTime-(int)(System.currentTimeMillis()-CPU_Milis));
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
		RND_Data[Counter] ^= (Counter+1)*(CPU_Milis);	
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
	gameText = textosCreate( loadFile("/Textos.txt") );
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
	/*savePrefs();
	saveData();*/

	gameDestroy();
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
//	gc.canvasImg = gc.loadImage("/Logo"+cntLogos+".gif");
	gc.canvasImg = gc.FS_LoadImage(2,cntLogos);
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
int menuListDat[][];

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

	if ( f.stringWidth(Texto) <= gc.printerSizeX)
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
//#ifdef DOJA
//#else
	return Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
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
	panelFade = -1;

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
	panelFade = fadeTime*2;
}

public void menuListSet_Screen()
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCREEN;
	panelFade = -1;



	menuListBloIni = 0;
	menuListBloSize = 0;

	int sizeY = 0;

	for (int i=0 ; i<menuListDat.length ; i++)
	{
	if ( (sizeY += menuListDat[i][4]) < gc.printerSizeY) {menuListBloSize++;} else {break;}
	}

}



// -------------------
// menuList Tick
// ===================

public boolean menuListTick(int movY, boolean fire)
{
	if (menuListMode == ML_OPTION)
	{
		if ((panelFade==fadeTime+1)&&(movY ==-1 && menuListPos > 0)) {menuListPos--; menuListShow=true;}
		else
		if ((panelFade==fadeTime+1)&&(movY == 1 && menuListPos < menuListDat.length-1)) {menuListPos++; menuListShow=true;}
		else
		if ((panelFade==fadeTime+1)&&(movY ==-1 && menuListPos == 0)) {menuListPos = menuListDat.length-1; menuListShow=true;}
		else
		if ((panelFade==fadeTime+1)&&(movY == 1 && menuListPos == menuListDat.length-1)) {menuListPos = 0; menuListShow=true;}
		if ((panelFade==fadeTime+1)&&(fire))
		{
		if (++menuListDat[menuListPos][2] == menuListStr[menuListPos].length) {menuListDat[menuListPos][2]=0;}
		menuListCMD=menuListDat[menuListPos][1];
		menuListShow=true;
		if (menuImg == null) playShow = true;		
		if (menuListShow) panelFade = fadeTime;
		return true;
		}
	if (menuListShow) panelFade = fadeTime;
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
			if ( (sizeY += menuListDat[i][4]) < gc.printerSizeY) {menuListBloSize++;} else {break;}
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

public void menuListDraw(Graphics g)
{
	if (!menuList_ON) {return;}
	//if (!menuList_ON || !menuListShow) {return;}

	menuListShow = false;

	//gc.canvasFillInit(0);
	if (menuImg!=null) {gc.canvasImg = menuImg;}
	playShow = true;
//#ifdef DOJA
//#else
		menuListFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
//#endif
		g.setFont(menuListFont);

	switch (menuListMode)
	{
	case ML_OPTION:

		String[] texto = menuListCutText(0, menuListStr[menuListPos][menuListDat[menuListPos][2]]);

		int height = (texto.length * menuListDat[menuListPos][4]);
		//int y = menuListY + (( menuListSizeY / 4) * 3) - (height/2);
		int y = gc.canvasHeight-((2*panelHeight)/3);
		if (y + height > menuListSizeY) {y = menuListY + (menuListSizeY-height);}

		setPanel(-1,menuListStr[menuListPos][menuListDat[menuListPos][2]]);
		/*for (int i=0 ; i<texto.length ; i++)
		{
			menuListTextDraw(g, 0, texto[i], 0,y);

			y += menuListDat[menuListPos][4];
		}*/
	break;

	case ML_SCROLL:
		
		panelInfo = new String[40];
		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			//menuListTextDraw(g, 0, menuListStr[i][0], 0,y);
			if ((y>gc.printerY)&&(y<gc.printerSizeY)) panelInfo[i] = menuListStr[i][0];

			y += menuListDat[i][4];
		}
	break;


	case ML_SCREEN:

		panelInfo = new String[40];
		for (int i=0; i<panelInfo.length; i++) panelInfo[i] = "";
		y = 0;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			y += menuListDat[i][4];
		}

		y = (menuListSizeY - y) / 2;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			//menuListTextDraw(g, 0, menuListStr[i][0], 0,y);
			/*if ((y>menuListY)&&(y<menuListSizeY)) */panelInfo[i-menuListBloIni] = menuListStr[i][0];

			y += menuListDat[i][4];

		}

	break;
	}
}

public void menuListTextDraw(Graphics g, int format, String str, int x, int y)
{
	x = menuListX + (( menuListSizeX - menuListFont.stringWidth(str) ) / 2);
//#ifdef DOJA
//#else
	g.setColor(gc.MenuTextBorderLineColor);
	g.drawString(str, x-1,y-1, 20);
	g.drawString(str, x+1,y-1, 20);
	g.drawString(str, x-1,y+1, 20);
	g.drawString(str, x+1,y+1, 20);

	g.setColor(gc.MenuTextColor);
	g.drawString(str, x,y, 20);
//#endif
}


public String[] menuListCutText(int Dato, String Texto)
{
	String[] str = new String[0];

	int Pos=0, PosIni=0, PosOld=0, Size=0;
//#ifdef DOJA
//#else
	char[] Tex = Texto.toCharArray();

	Font f = menuListGetFont(Dato);

	while ( PosOld < Tex.length )
	{
	Size=0;

	Pos=PosIni;

	while ( Size < (gc.printerSizeX) )
	{
		if ( Pos == Tex.length ) {PosOld=Pos; break;}

		int Dat = Tex[Pos++];
		if (Dat==0x20) {PosOld=Pos-1;}
		Size += f.charWidth((char)Dat);
	}

	if (PosOld-PosIni < 1) { while ( Pos < Tex.length && Tex[Pos] >= 0x30 ) {Pos++;} PosOld=Pos; }
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
// prefs Update - Leemos/Salvamos archivo de preferencias, result: "null" si NO Existe
// ===================

// Leemos cuando le pasamos "null" como parametro.
// Salvamos cuando le pasamos "byte[]" como parametro.
public byte[] updatePrefs(byte[] bufer)
{
	//#ifdef DOJA
	//#else
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
	//#endif
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// inputDialog - Engine
// ===================
// *******************
//#ifdef DOJA
//#else
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
	Display.getDisplay(this).setCurrent(inputForm);		
}

public void commandAction (Command c, Displayable d)
{
	if(c.getLabel()==inputDialogLabel)
	{								
		Display.getDisplay(this).setCurrent(gc);	
		inputDialogNotify(inputField.getString());
	}  	 	
}
//#endif
public void inputDialogNotify(String s){}

// <=- <=- <=- <=- <=-




// **************************************************************************//
// Inicio Clase Game
// **************************************************************************//

public void gameCreate() {}
public void gameDestroy() {}
public void gameTick() {}
public void gameRefresh() {}

public void menuInit(int a) {}
public void menuAction(int a) {}

public void loadPrefs() {}
public void savePrefs() {}
public void saveData() {}

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
	
	//System.out.println(data+" "+readInt(actDataPos+prefsSize));
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
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+3]&(0x000000FF));
	data <<= 8;
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+2]&(0x000000FF));
	data <<= 8;
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+1]&(0x000000FF));
	data <<= 8;
	data|=(prefsData[((prefsSize+(slotSize*actSlot))+actDataPos)+0]&(0x000000FF));
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
	data|=(prefsData[pos+3]&(0x000000FF));
	data <<= 8;
	data|=(prefsData[pos+2]&(0x000000FF));
	data <<= 8;
	data|=(prefsData[pos+1]&(0x000000FF));
	data <<= 8;
	data|=(prefsData[pos]&(0x000000FF));
	return data;
}

public void startData()
{
	actDataPos = 0;
}

public void setData(byte[] Data, int sSlots, int pSize, int sSize) // Array de bytes / Numero de slots / Tama�o de las preferencias / Tama�o de cada Slot
{
	prefsData=Data;
	saveSlots=sSlots;
	prefsSize=pSize;
	slotSize=sSize;
	setDataSlot(0);
}

public void setData(int sSlots, int pSize, int sSize) // Numero de slots / Tama�o de las preferencias / Tama�o de cada Slot
{
	saveSlots=sSlots;
	prefsSize=pSize;
	slotSize=sSize;
	prefsData=new byte[pSize+sSlots*sSize];
	setDataSlot(0);
}

public void setDataSlot(int Slot) // Slot con el que quieres trabajar
{
	actSlot = Slot;
}

int actSlot=0;
int saveSlots;
int prefsSize;
int slotSize;
byte[] prefsData;
int actDataPos=0;
// **************************************************************************//
// Final Clase Game
// **************************************************************************//

/////////////////////////////////// TRIG TRIG TRIG TRIG

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
			
	static int sin(int a)
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

	static int cos(int a)
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
	
	static int tan(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=tantable[a%64];
		else 	v=tantable[63-(a%64)];	
				
		if((a>>6)%2==1) v=-v;		
		return v;
	}
	static int asin(int v)
	{
		int a=0, vabs;
		if(v<0) vabs=-v;
		else vabs=v;
		
		while((sintable[a]<v) && (a<63)) a++;	
		
		if(v>=0) return a;
		else return (256-a)%256;
		
	}
	static int acos(int v)
	{
		int a=0, vabs;
		if(v<0) vabs=-v;
		else vabs=v;
		
		while((costable[a]>v) && (a<63)) a++;	
		
		if(v>=0) return a;
		else return 128-a;
		
	}
	static int atan(int s, int c)
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

// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

//INIT INIT INIT INIT INIT INIT INIT INIT INIT INIT INIT INIT INIT INIT INIT

public void setPanel(int tipo)
{
	setPanel(tipo,panelTitle);
}

public void setPanel(int tipo,String Title)
{
	setPanel(tipo,Title,panelInfo);
}

public void setPanel(int tipo,String Title,String[] Info)
{
	panelTitle = Title;
	panelInfo = Info;
	switch (tipo)
	{
		case 0 : //Entra 1
			panelType = 0;
			panelMove = -4;
			panelInitPos = gc.canvasHeight;
			panelPos = panelInitPos;
			panelMaxPos = gc.canvasHeight-panelHeight;
			panelAct = true;
		break;
		case 1 : //Entran 2
			panelType = 1;
			panelMove = 4;
			panelInitPos = 0-panelHeight;
			panelPos = panelInitPos;
			panelMaxPos = (gc.canvasHeight-(panelHeight+panel2Height))/2;
			panelAct = true;
		break;
		case 2 : //Sale 1
			panelType = 0;
			panelMove = 4;
			panelInitPos = panelPos;
			panelMaxPos = gc.canvasHeight;
			panelAct = false;
		break;
		case 3 : //Salen 2
			panelType = 1;
			panelMove = -4;
			panelInitPos = panelPos;
			panelMaxPos = 0-panelHeight;
			panelAct = false;
		break;
		case 4 : //Entra Centrado
			panelType = 0;
			panelMove = 4;
			panelInitPos = 0-panelHeight;
			panelPos = panelInitPos;
			panelMaxPos = (gc.canvasHeight-panelHeight)/2;
			panelAct = true;
		break;
	}
}


// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------

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
boolean playShow;
int gameStatus;

//#ifdef MI-M341i
//#elifdef NE-N341i
//#elifdef NE-N410i
//#elifdef VI-TSM30i
//#elifdef VI-TSM7i
//#elifdef VI-TSM6
//#elifdef VI-TSM100
//#elifdef MO-V3xx
//#elifdef MO-C65x
//#elifdef MO-C450
//#elifdef SI-x55
//#elifdef SI-C65
//#elifdef SI-CX65
//#elifdef MO-T720
//#elifdef SG-Z105
//#elifdef SE-Z1010
//#elifdef NK-s60
	final static int SleepTime = 50;
	final static int panelWidth = 176;
	final static int panelHeight = 37;
	final static int panel2Height = 171;
//#elifdef NK-s30
//#elifdef NK-s40
//#elifdef SE-T6xx
//#elifdef AL-756
//#elifdef SM-MYVx
//#elifdef SH-GX10
//#elifdef SH-GX15
//#elifdef SH-GX20
//#elifdef SH-GX30
//#elifdef PA-X60
//#endif
int panelType = 0;
int panelMove = 0;
int panelInitPos = 0-panelWidth;
int panelPos = panelInitPos;
int panel2Pos = 500;
int panelMaxPos = 0;
String panelTitle = "";
String[] panelInfo = new String[0];
boolean panelAct = false;
int panelFade = -1;
//#ifdef SI
//#else
int fadeTime = 3;
//#endif

// **************************************************************************//
// Final Clase Bios
// **************************************************************************//
};