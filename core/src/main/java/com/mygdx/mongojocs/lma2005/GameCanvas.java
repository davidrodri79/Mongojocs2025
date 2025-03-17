package com.mygdx.mongojocs.lma2005;// -----------------------------------------------
// Microjocs The Love Boat Project Rev.0 (1.3.2004)
// ===============================================
// Massive Client Mobile Source Code
// -----------------------------------------------
// Server Programming by Carlos Carrasco
// Server Programming by Raimon Rafols
// Client Programming by Juan Antonio G�mez G�lvez
// -----------------------------------------------

// (de la linea 3600 en adelante, mas o menos)
// ---------------------------------------------------------------
// Microjocs Football Manager
// ---------------------------------------------------------------
// Strategical Simulation / Pseudo-RPG Football Team Managing Game
// Programming by Carlos Carrasco


//#ifdef PACKAGE

//#endif


//#ifdef J2ME


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

//#ifdef J2ME
	//#ifdef FULLSCREEN
		//#ifdef MO-C450
		//#elifdef MIDP20
		//#elifdef NOKIAUI
			public class GameCanvas extends FullCanvas
		//#else
		//#endif
	//#else
	//#endif
//#elifdef DOJA
//#endif

//#ifdef J2ME
//#ifndef FULLSCREEN
//#endif
//#endif
{
// ---------------------------------
//  Bits de Control de cada Terminal
// =================================
private final boolean deviceSound = true;
private final boolean deviceVibra = true;

private boolean playShow;

private int playExit;

private int playMode = -1;
private int playModeBack = -1;

private int playRunningMode;


private int playX;
private int playY;
private int playWidth;
private int playHeight;

private int playStatus;		// Variable Temporal para hacer SUB-Modos de la playMode.



// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

private Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga)
{
	this.ga = ga;
	//canvasCreate();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

private Graphics scr;

//#ifdef NOKIAUI
private DirectGraphics dscr;
//#endif

private boolean paintFinished = true;
private boolean paintLock = false;

public void paint (Graphics g) {       
//#ifdef SI-x55
//#endif
	paintFinished = false;
	scr = g;
//#ifdef NOKIAUI
	dscr = DirectUtils.getDirectGraphics(g);
//#endif
//#ifdef DOJA
//#endif
	try {
		iformPaint();
		//iformDisplayAll();
	} catch (Exception e) {System.out.println("*** Graphic ***"); e.printStackTrace();}
//#ifdef DOJA
//#endif
	paintFinished = true;

//#ifdef SI-x55
//#endif
	paintLock = false;
}

private boolean screenLost = false;
private boolean animationLost = false;

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw() {
	if (!paintFinished)
		return;
	if (screenLost || animationLost) {
		paintLock = true;
		repaint();
//#ifdef J2ME
		serviceRepaints();
//#endif
		while (paintLock) {
			try { Thread.sleep(2); } catch (Exception e) { }
		}
		screenLost = false;
		animationLost = false;
	}
}


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

private boolean virtualJoy = true;;

private int intKeyMenu, intKeyX, intKeyY, intKeyMisc, intKeyCode;

//#ifdef J2ME

public void keyPressed(int intKeyCode)
{
	intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; this.intKeyCode=intKeyCode;

	if (virtualJoy)
	{
		switch (intKeyCode)
		{
		case Canvas.KEY_NUM1:
			intKeyX=-1; intKeyY=-1;
		break;
	
		case Canvas.KEY_NUM2:	// Arriba
			intKeyY=-1;
		break;
	
		case Canvas.KEY_NUM3:
			intKeyX= 1; intKeyY=-1;
		break;
	
		case Canvas.KEY_NUM4:	// Izquierda
			intKeyX=-1;
		break;
	
		case Canvas.KEY_NUM5:	// Disparo
			intKeyMenu=2;
		break;
	
		case Canvas.KEY_NUM6:	// Derecha
			intKeyX=1;
		break;
	
		case Canvas.KEY_NUM7:
			intKeyX=-1; intKeyY= 1;
		break;
	
		case Canvas.KEY_NUM8:	// Abajo
			intKeyY=1;
		break;
	
		case Canvas.KEY_NUM9:
			intKeyX= 1; intKeyY= 1;
		break;
		}
	}


	switch (intKeyCode)
	{
	case Canvas.KEY_NUM1:
		intKeyMisc=1; //intKeyX=-1; intKeyY=-1;
	return;

	case Canvas.KEY_NUM2:	// Arriba
		intKeyMisc=2; //intKeyY=-1;
	return;

	case Canvas.KEY_NUM3:
		intKeyMisc=3; //intKeyX= 1; intKeyY=-1;
	return;

	case Canvas.KEY_NUM4:	// Izquierda
		intKeyMisc=4; //intKeyX=-1;
	return;

	case Canvas.KEY_NUM5:	// Disparo
		intKeyMisc=5; //intKeyMenu=2;
	return;

	case Canvas.KEY_NUM6:	// Derecha
		intKeyMisc=6; //intKeyX=1;
	return;

	case Canvas.KEY_NUM7:
		intKeyMisc=7; //intKeyX=-1; intKeyY= 1;
	return;

	case Canvas.KEY_NUM8:	// Abajo
		intKeyMisc=8; //intKeyY=1;
	return;

	case Canvas.KEY_NUM9:
		intKeyMisc=9; //intKeyX= 1; intKeyY= 1;
	return;

	case Canvas.KEY_NUM0:
		intKeyMisc=10;
	return;

	case 35:		// *
		intKeyMisc=12;
	return;

	case 42:		// #
		intKeyMisc=11;
	return;
/*
// -----------------------------------------
//#ifdef NOKIAUI
	case -6:	// Nokia - Menu Izquierda
		intKeyMenu = -1;
	return;

	case -7:	// Nokia - Menu Derecha
		intKeyMenu = 1;
	return;
//#endif
// =========================================

// -----------------------------------------
//#ifdef MO-T720
	case -20:	// Motorola T720 - Menu Izquierda
		intKeyMenu=-1;
	return;

	case -21:	// Motorola T720 - Menu Derecha
		intKeyMenu= 1;
	return;
//#endif
// =========================================

// -----------------------------------------
//#ifdef SI-x55
	case -1:	// Siemens - Menu Izquierda
		intKeyMenu=-1;
	return;

	case -4:	// Siemens - Menu Derecha
		intKeyMenu= 1;
	return;
//#endif
// =========================================

// -----------------------------------------
//#ifdef MO-V3xx
	case 21:	// Motorola V3xx - Menu Izquierda
		intKeyMenu=-1;
	return;

	case 22:	// Motorola V3xx - Menu Derecha
		intKeyMenu= 1;
	return;
//#endif
// =========================================

// -----------------------------------------
//#ifdef MO-C65x
	case -21:	// Motorola C650 - Menu Izquierda
		intKeyMenu=-1;
	return;

	case -22:	// Motorola C650 - Menu Derecha
		intKeyMenu= 1;
	return;
//#endif

*/


// =========================================

//#ifdef NK
	case -6:	// Nokia - Menu Izquierda
		intKeyMenu = -1;
	break;

	case -7:	// Nokia - Menu Derecha
		intKeyMenu = 1;
	break;

//#elifdef SE
//#elifdef MO-T720
//#elifdef SI
//#elifdef MO-V3xx
//#elifdef MO-C65x
//#elifdef AL-756
//#elifdef LG-U8150
//#elifdef SG-Z105
//#endif


	}

// Controlamos Game Action

	switch(getGameAction(intKeyCode))
	{
		case 1:intKeyY=-1;return;	// Arriba
		case 6:intKeyY=1;return;	// Abajo
		case 5:intKeyX=1;return;	// Derecha
		case 2:intKeyX=-1;return;	// Izquierda
		case 8:intKeyMenu=2;return;	// Fuego
	}

}

public void keyReleased(int intKeyCode)
{
	intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; intKeyCode=0;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#endif


//#ifdef DOJA
//#endif



//#ifdef J2ME

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void showNotify() {
	gamePaused = false;
	if (gameRunning) {
		screenLost = true;
		animationLost = true;
	}
}

public void hideNotify() {
	gamePaused=true;
	soundStop();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#endif




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


private int convAry2Int(byte[] Bufer, int Pos)
{
	return ((Bufer[Pos++]&0xFF)<<24) | ((Bufer[Pos++]&0xFF)<<16) | ((Bufer[Pos++]&0xFF)<<8) | (Bufer[Pos]&0xFF);
}


private void imageDraw(Image Img)
{
	imageDraw(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ----------------------------------------------------------

private void imageDraw(Image Img, int X, int Y)
{
	imageDraw(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

private void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	if (DestX >= canvasWidth || DestY >= canvasHeight) {return;}

//#ifdef J2ME
	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
//#endif

//#ifdef DOJA
//#endif
}

// ----------------------------------------------------------

private void imageDraw(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
//#ifdef J2ME
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
//#endif

//#ifdef DOJA
//#endif
}

// ----------------------------------------------------------

private void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
//#ifdef J2ME
	scr.setClip(TopX, TopY, FinX, FinY);
	scr.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
//#endif

//#ifdef DOJA
//#endif
}

// ----------------------------------------------------------

// -------------------
// sprite Draw
// ===================
/*
//#ifdef J2ME
public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int flip=0;
	if (sizeX < 0) {sizeX*=-1; flip|=1;}
	if (sizeY < 0) {sizeY*=-1; flip|=2;}
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame++] + 128;

//#ifdef NOKIAUI
	imageDraw(img, sourX, sourY, sizeX, sizeY, destX, destY, flip);
//#else
	scr.setClip (0, 0, playWidth, playHeight-panelHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);
	scr.drawImage(img, destX-sourX, destY-sourY, Graphics.TOP|Graphics.LEFT);
//#endif

	//#ifdef SCRFIX
	ga.ScrollUpdate(destX, destY, sizeX, sizeY);
	//#endif
}
//#endif

//#ifdef DOJA
public void spriteDraw(Image[] img,  int x, int y,  int frame, byte[] cor)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;

	scr.drawImage(img[cor[frame+2]], destX, destY);

	ga.ScrollUpdate(destX, destY, cor[frame++], cor[frame]);
}
//#endif
*/
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


//#ifdef NOKIAUI
private void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       >= canvasWidth)
	||	(DestX+SizeX < 0)
	||	(DestY       >= canvasHeight)
	||	(DestY+SizeY < 0))
	{return;}

	//scr.setClip (0, 0, playWidth, playHeight-panelHeight);
	scr.setClip (0, 0, playWidth, playHeight);
	scr.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
	dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
	return;

	case 2:
	dscr.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
	dscr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
	return;
	}
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<






static private final int COLOR_BACKGROUND = 0x000055;


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas de Entrada y Salida de Medios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


// -------------------
// load Image
// ===================

private Image loadImage(String FileName)
{
//#ifdef J2ME
	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e) {}

	System.gc();
	return Img;
//#endif

//#ifdef DOJA
//#endif
}

// ---------------------------------------------------------

//#ifdef DOJA
//#endif

// ---------------------------------------------------------

//#ifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// ===================





private String[] textBreak(String texto, int width, Font f)
{
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int count = 0;

	while ( posIni < texto.length() )
	{
		size = 0;

		pos = posIni;
		posOld = posIni;

		while ( size < width )
		{
			if ( pos == texto.length()) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat == 0x20 ) { posOld = pos - 1; }

//#ifdef J2ME
			size += f.charWidth((char)dat);
//#endif

//#ifdef DOJA
//#endif
	    }

	    if (posOld == posIni)
		{
			posOld = pos - 1;
		}

		if (posIni < posOld)
		{
		textBreakPos[count++] = (short) posIni;
		textBreakPos[count++] = (short) posOld;
		}

		posIni = posOld;
		while ( posIni < texto.length() && texto.charAt(posIni) == 0x20) {posIni++;}
	}

	String str[] = new String[count/2];

	for (int i=0 ; i<count ; )
	{
		str[i/2] = texto.substring(textBreakPos[i++], textBreakPos[i++]);
	}

	return str;
}


// infame
//private short textBreakPos[] = new short[512];
private short textBreakPos[] = new short[256];


private String[] textBreak(String texto, int width, byte[] f, int letraMin, int letraSpc)
{
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int count = 0;

	while ( posIni < texto.length() )
	{
		size = 0;

		pos = posIni;
		posOld = posIni;

		while ( size < width )
		{
			if ( pos == texto.length() ) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat == 0x20 ) { dat=0x41; posOld = pos - 1; }

			try {
				size += ( f[ ((dat - letraMin) *6) +2 ] + letraSpc );
			} catch(Exception e) {size+=4;}

	    }

	    if (posOld == posIni)
		{ 
			posOld = pos - 1;
		}

		if (posIni < posOld)
		{
		textBreakPos[count++] = (short) posIni;
		textBreakPos[count++] = (short) posOld;
		}

		posIni = posOld;
		while ( posIni < texto.length() && texto.charAt(posIni) == 0x20) {posIni++;}
	}

	String str[] = new String[count/2];

	for (int i=0 ; i<count ; )
	{
		str[i/2] = texto.substring(textBreakPos[i++], textBreakPos[i++]);
	}

	return str;
}











// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------










// *******************
// -------------------
// Canvas - Engine
// ===================
// *******************

private int canvasWidth=getWidth();
private int canvasHeight=getHeight();

private int canvasFillRGB;
private boolean canvasFillShow=false;
private Image canvasImg;

private boolean canvasShow = false;

// -------------------
// canvas Create
// ===================

/*public void canvasCreate()
{
	canvasFillInit(0xffffff);

//#ifdef J2ME
	canvasImg = loadImage("/Microjocs.png");
//#endif

//#ifdef DOJA
	canvasImg = loadImage("/Microjocs.gif");
//#endif

	canvasShow = true;
	repaint();

//#ifdef J2ME
	serviceRepaints();
//#endif
}
*/

// -------------------
// canvas Init
// ===================

private void canvasInit()
{

//#ifdef MIDP20
	//#ifdef FULLSCREEN
		setFullScreenMode(true);
		try {Thread.sleep(1000);} catch (Exception e) {}

		canvasWidth = getWidth();
		canvasHeight = getHeight();
	//#endif
//#endif



//#ifdef DOJA
//#endif

//#ifndef PLAYER_NONE

	//#ifdef PLAYER_OTA
	//#elifdef PLAYER_VODAFONE
	//#else
		soundCreate( new String[] {
			"/title.mid",		// 0 - Musica de la caratula
			"/fxerr.mid",		// 1 - error generico
			"/fxglerr.mid",		// 2 - gol falladao
			"/fxglsco.mid",		// 3 - gol marcado
			"/fxinj.mid",		// 4 - sancion o lesion
			"/fxok.mid"		// 5 - "lo contrario de error generico" :D
		});
	//#endif

//#endif

	//#ifdef LISTENER
//		ListenerSET(0);
	//#endif
}


// <=- <=- <=- <=- <=-



// *******************
// -------------------
// printer - Engine - Rev.0 (3.3.2004)
// ===================
// *******************

static private final int PRINT_LEFT =	0x0000;
static private final int PRINT_RIGHT =	0x0001;
static private final int PRINT_HCENTER=	0x0002;
static private final int PRINT_TOP =	0x0000;
static private final int PRINT_BOTTOM =	0x0004;
static private final int PRINT_VCENTER=	0x0008;

//static final int PRINT_OUTLINE=	0x1000;

static private final int PRINT_MASK =	0x2000;

//static final int PRINT_BREAK=	0x4000;

static private final int PRINT_SHADOW=	0x8000;
static private final int PRINT_QSHADOW=	0x4000;


private int printX;
private int printY;
private int printWidth;
private int printHeight;

private Font printFont;
private Image printFontImg;
private byte[] printFontCor;

private int printFontType;
private int printFontHeight;
private int printAscent;

private int printFont_Min;
private int printFont_Spc;

// -------------------
// print Create
// ===================

private  void printCreate()
{
	printX = 0;
	printY = 0;
	printWidth = canvasWidth;
	printHeight = canvasHeight;
}

private  void printCreate(int x, int y, int width, int height)
{
	printX = x;
	printY = y;
	printWidth = width;
	printHeight = height;
}

private  void printCreateA(int[] a) {
	printCreate(a[0], a[1], a[2], a[3]);
}

// -------------------
// print Destroy
// ===================

private  void printDestroy()
{
	printFont = null;
	printFontImg = null;
	printFontCor = null;
}


// -------------------
// print Init
// ===================

private  void printInit_Font(Font f)
{
	printFont = f;
	printFontHeight = f.getHeight();

//#ifdef DOJA
//#endif

	printFontType = 1;
}


private  void printInit_Font(Image img, byte[] cor, int height, int letraMin, int letraSpc)
{
	printFontImg = img;
	printFontCor = cor;
	printFontHeight = height;
	printFont_Min = letraMin;
	printFont_Spc = letraSpc;

	printFontType = 2;
}


// -------------------
// print Fill
// ===================

private  void printFill(int rgb)
{
//#ifdef J2ME
	scr.setClip(printX, printY, printWidth, printHeight);
//#endif
	scr.setColor(rgb);
	scr.fillRect(printX, printY, printWidth, printHeight);
}


// -------------------
// print Draw
// ===================

private void printDrawAbbrFullWidth(String s) {
	printDrawAbbr(s, 0, 0, PRINT_VCENTER, printWidth);
}

private void printDrawAbbrFullWidthR(String s) {
	printDrawAbbr(s, 0, 0, PRINT_VCENTER|PRINT_RIGHT, printWidth);
}

private void printDrawAbbr(String str, int x, int y, int mode, int avail) {
	// "histeresis"
	avail -= 3;
	if (avail < 1) {
		avail = 1;
	}
	if (printTextWidth(str) <= avail) {
		printDraw(str, x, y, mode);
		return;
	}
	String st = str + "...";
	while (printTextWidth(st) > avail) {
		st = st.substring(0, st.length() - 4) + "...";
	}
	printDraw(st, x, y, mode);
}

private void printDraw(String str, int x, int y, int mode) {

	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - printFontHeight )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - printFontHeight );}


	if ((mode & PRINT_HCENTER)!=0) {x +=( printWidth - printTextWidth(str) )/2;}
	else
	if ((mode & PRINT_RIGHT)!=0)   {x +=( printWidth - printTextWidth(str) );}


	x += printX;
	y += printY + printAscent;


	switch (printFontType)
	{
	case 1:
//#ifdef J2ME
		if ((mode & PRINT_MASK) == 0)
		{
		scr.setClip(0, 0, canvasWidth, canvasHeight);
		} else {
		scr.setClip(printX, printY, printWidth, printHeight);
		}
//#endif

		scr.setFont(printFont);

//		if ( ((mode & PRINT_MASK) != 0) && (y >= printY) && (y + printFontHeight < printY + printHeight) )
//		{

//#ifdef J2ME
		if ((mode & PRINT_SHADOW) != 0) {
			int c = scr.getColor();
			scr.setColor(0);
			scr.drawString(str,  x+1,  y+1,  20);
			scr.setColor(c);
		} else if ((mode & PRINT_QSHADOW) != 0) {
			int c = scr.getColor();
			scr.setColor(0);
			scr.drawString(str,  x+1,  y,  20);
			scr.drawString(str,  x-1,  y,  20);
			scr.drawString(str,  x,  y+1,  20);
			scr.drawString(str,  x,  y-1,  20);
			scr.setColor(c);
		}
		scr.drawString(str,  x,  y,  20);
//#endif

//#ifdef DOJA
//#endif

//		}

	break;

//#ifndef NO_CUSTOM_FONT
	case 2:
		byte[] tex = str.getBytes();

		for (int add=0, i=0 ; i<tex.length ; i++)
		{
			try {
				int letra = tex[i] & 0xff;
				if (letra == '@') {
					// saltar encoding - util para los iconos
					i++;
					letra = tex[i] & 0xff;
				} else { 
					// traduccion a fuente custom con acentos
					letra = font1Trans[letra];
				}

				letra = letra - printFont_Min;
				if (letra == 0xD1 || letra == 0xF1) {letra = 0x5F;}	// es la � o � ???
				if (letra > 0 && letra < printFontCor.length/6)	// Esta dentro de las letras permitidas???
				{
					printSpriteDraw(printFontImg, x+add, y, letra , printFontCor, mode);
				} else {
					letra = 0x21;
				}
				add += printFontCor[(letra * 6) + 2 ] + printFont_Spc;
			} catch (Exception e) { }
		}
	break;
//#endif
	}
}


// -------------------
// print Sprite Draw
// ===================

private void printSpriteDraw(Image img, int x, int y, int frame, byte[] cor, int mode)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame++] + 128;

//#ifdef J2ME

	if (destX >= canvasWidth || destY >= canvasHeight || (destX + sizeX) <= 0 || (destY + sizeY) <= 0) {return;}

	//#ifdef x55
	//#endif

	scr.setClip(destX, destY, sizeX, sizeY);
/*
	if ((mode & PRINT_MASK) == 0)
	{
	scr.setClip(destX, destY, sizeX, sizeY);
	} else {
	scr.setClip(printX, printY, printWidth, printHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);
	}
*/
	scr.drawImage(img, destX-sourX, destY-sourY, 20);
//#endif

//#ifdef DOJA
//	if ((mode & PRINT_MASK) == 0)
//	{
//	scr.setClip(destX, destY, sizeX, sizeY);
//	} else {
//	scr.setClip(printX, printY, printWidth, printHeight);
//	scr.clipRect(destX, destY, sizeX, sizeY);
//	}
//#endif

}


// -------------------
// print Text Width
// ===================

private int printTextWidth(String str)
{
	int size = 0;

	switch (printFontType)
	{
	case 1:
		size = printFont._stringWidth(str);
	break;

//#ifndef NO_CUSTOM_FONT
	case 2:
		byte[] tex = str.getBytes();

		for (int i=0 ; i<tex.length ; i++)
		{
			try {
				int letra = tex[i] & 0xff;
				if (letra == '@') {
					// saltar encoding - util para los iconos
					i++;
					letra = tex[i] & 0xff;
				} else { 
					// traduccion a fuente custom con acentos
					letra = font1Trans[letra];
				}
				letra = letra - printFont_Min;

				if (letra==0) {letra = 0x41 - printFont_Min;}

				size += printFontCor[(letra * 6) + 2 ] + printFont_Spc;
			} catch (Exception e) {}
		}
	break;
//#endif

	}

	return size;
}


// -------------------
// print Text Height
// ===================

private int printTextHeight()
{
	return printFontHeight;
}


// -------------------
// print Sprite Width
// ===================

private int printSpriteWidth(byte[] cor, int frame)
{
	return cor[(frame*6)+2] /*+ (cor[(frame*6)]*2)*/;
}


// -------------------
// print Sprite Height
// ===================

private int printSpriteHeight(byte[] cor, int frame)
{
	return cor[(frame*6)+3] /*+ (cor[(frame*6)+1]*2)*/;
}


// -------------------
// print Draw
// ===================

//#ifdef J2ME
private void printDraw(Image img, byte[] cor, int x, int y, int frame, int mode)
{
	frame*=6;

	int despX=cor[frame++];
	int despY=cor[frame++];
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame++] + 128;


	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - sizeY )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - sizeY );}


	if ((mode & PRINT_HCENTER)!=0) {x +=( printWidth - sizeX )/2;}
	else
	if ((mode & PRINT_RIGHT)!=0)   {x +=( printWidth - sizeX );}


	x += printX + despX;
	y += printY + despY;


	if ((mode & PRINT_MASK) == 0)
	{
	scr.setClip(x, y, sizeX, sizeY);
	} else {
	scr.setClip(printX, printY, printWidth, printHeight);
	scr.clipRect(x, y, sizeX, sizeY);
	}
	scr.drawImage(img, x-sourX, y-sourY, 20);
}
//#endif

//#ifdef DOJA
//#endif




// -------------------
// print Text Break
// ===================

private short[] printTextBreakParts;


private int[] printTextBreak(int[] data, int num)
{
	int[] tempData = new int[num];

	for (int pos=0, i=0 ; i<data.length ; i++)
	{
		for (int t=0 ; t<printTextBreakParts[i] ; t++)
		{
			tempData[pos++] = data[i];
		}
	}

	return tempData;
}


private String[] printTextBreak(String[] str, int width)
{
	printTextBreakParts = new short[str.length];

	String[] tempStr = new String[60];
	int tempNum = 0;

	for (int i=0 ; i<str.length ; i++)
	{
		String[] tempLines = printTextBreak(str[i] , width);
		printTextBreakParts[i] = (short) tempLines.length;

		for (int t=0; t<tempLines.length ; t++)
		{
		tempStr[tempNum++] = tempLines[t];
		}
	}

	String[] finalStr = new String[tempNum];

	for (int i=0 ; i<tempNum ; i++)
	{
	finalStr[i] = tempStr[i];
	}

	return finalStr;
}

private String[] printTextBreakInv(String[] str, int width) {
	printTextBreakParts = new short[str.length];

	String[] tempStr = new String[60];
	int tempNum = 0;

	for (int i=0 ; i<str.length ; i++) {
		String[] tempLines = printTextBreak(str[i] , width);
		printTextBreakParts[i] = (short) tempLines.length;
		for (int t=tempLines.length-1; t>= 0 ; t--) {
			tempStr[tempNum++] = tempLines[t];
		}
	}

	String[] finalStr = new String[tempNum];
	for (int i=0 ; i<tempNum ; i++) {
		finalStr[i] = tempStr[i];
	}

	return finalStr;
}

private String[] printTextBreak(String str, int width)
{
//#ifdef NO_CUSTOM_FONT
//#else
	switch (printFontType)
	{
	case 1:
	return textBreak(str, width, printFont);

	case 2:
	return textBreak(str, width, printFontCor, printFont_Min, printFont_Spc);
	}

	return new String[] {str};
//#endif
}

// <=- <=- <=- <=- <=-





/* ===================================================================

	soundCreate()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	soundPlay(n? Sonido , Repetir)
	-----------------------------
		Hacemos que suene un sonido (0 a x) y que se repita x veces.
		Repetir == 0: Repeticion infinita

	soundStop()
	----------
		Paramos el ultimo sonido.

	soundTick()
	----------
		Debemos ejecutar este metodo en CADA ciclo para gestionar 'tiempos'

	vibraInit(microsegundos)
	-----------------------
		Hacemos vibrar el mobil x microsegundos

=================================================================== */

//#ifdef J2ME

	//#ifdef PLAYER_NONE
	//#elifdef PLAYER_OTA
	//#elifdef PLAYER_MIDP20

		// *******************
		// -------------------
		// Sound - Engine - Rev.0 (28.11.2003)
		// -------------------
		// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
		// ===================
		// *******************
		
		Player[] Sonidos;
		
		int SoundOld = -1;
		
		// -------------------
		// Sound INI
		// ===================
		
		public void soundCreate(String[] sons)
		{	
			Sonidos = new Player[sons.length];

			for (int i=0 ; i<sons.length ; i++)
			{
				Sonidos[i] = SoundCargar(sons[i]);
			}
		}

		public Player SoundCargar(String Nombre)
		{
			Player p = null;
		
			try
			{
				InputStream is = getClass().getResourceAsStream(Nombre);
				p = Manager.createPlayer( Nombre , "audio/midi");
				p.realize();
				p.prefetch();
			}
			catch(Exception exception) {exception.printStackTrace();}
		
			return p;
		}
		
		// -------------------
		// Sound SET
		// ===================
		
		public void soundPlay(int Ary, int Loop)
		{
			soundStop();
		
			//if (ga.gameSound)
			if (gameSound)
			{
				if (Loop<1) {Loop=-1;}

				try
				{
					VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
					Sonidos[Ary].setLoopCount(Loop);
					Sonidos[Ary].start();
				}
				catch(Exception exception) {exception.printStackTrace();}
			
				SoundOld=Ary;
			}
		}
		
		// -------------------
		// Sound RES
		// ===================
		
		public void soundStop()
		{
			if (SoundOld != -1)
			{
				try
				{
					Sonidos[SoundOld].stop();
				}
				catch (Exception e) {}
		
				SoundOld = -1;
			}
		}
		
		// -------------------
		// Sound RUN
		// ===================
		
		public void soundTick()
		{
		}
		
		// -------------------
		// Vibra SET
		// ===================
		
		public void vibraInit(int Time)
		{
			//if (ga.gameVibra)
			if (gameVibra)
			{
				try
				{
					Display.getDisplay(ga).vibrate(Time);
				}
				catch (Exception e) {}
			}
		}
		
		// <=- <=- <=- <=- <=-


	//#elifdef PLAYER_MIDP20_CACHED
	//#elifdef PLAYER_MOTC450
	//#elifdef PLAYER_SIEMENS_MIDI
	//#elifdef PLAYER_VODAFONE
	//#elifdef PLAYER_TSM6
	//#endif

//#elifdef DOJA
//#endif

// <=- <=- <=- <=- <=-






/*
//#ifdef J2ME
//#ifdef FULLSCREEN

public void listenerInit(String strLeft, String strRight) {}

//#else

// **************************
// --------------------------
// Listerner - Engine v1.0 - Rev.0 (9.7.2003)
// ==========================
// **************************

Command[] listenerCmd;

// -------------------
// listener Init
// ===================

public void listenerInit(String strLeft, String strRight)
{
	if (listenerCmd == null)
	{
		listenerCmd = new Command[2];
	} else {
		removeCommand(listenerCmd[1]);
		removeCommand(listenerCmd[0]);
	}

	listenerCmd[0] = new Command(strLeft, Command.SCREEN, 1);
	listenerCmd[1] = new Command(strRight, Command.SCREEN, 1);

	addCommand(listenerCmd[0]);
	addCommand(listenerCmd[1]);

	setCommandListener(this);
}

// -----------------------
// Listener command Action
// =======================

public void commandAction (Command c, Displayable d)
{
	if (listenerCmd!=null)
	{
		if (c == listenerCmd[0])
		{
			keyMenu = -1;
		} else {
			keyMenu =  1;
		}

		ga.limpiaKeyMenu = true;
		return;
	}
}

// <=- <=- <=- <=- <=-

//#endif

//#elifdef DOJA

public void listenerInit(String strLeft, String strRight)
{
	setSoftLabel(Frame.SOFT_KEY_1, strLeft!= " "? strLeft:null);
	setSoftLabel(Frame.SOFT_KEY_2, strRight!= " "? strRight:null);
}
//#endif

*/



// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************


private Image font1Img;
private byte[] font1Cor;
private byte[] font1Trans;


// -------------------
// play Destroy Gfx
// ===================

private void playDestroy_Gfx()
{
	System.gc();

	font1Img = null;
	font1Cor = null;

	System.gc();
}

// <=- <=- <=- <=- <=-









// -------------------
// rect Fill
// ===================


private void rectFill(int RGB, int x, int y, int sizeX, int sizeY) {
//#ifdef J2ME
	scr.setClip(x,y, sizeX, sizeY);
//#endif

	scr.setColor(RGB & 0xffffff);
	scr.fillRect(x,y, sizeX, sizeY);
}

private void rectFillA(int RGB, int alpha, int x, int y, int sizeX, int sizeY) {
//#ifdef J2ME
	scr.setClip(x,y, sizeX, sizeY);
//#endif

//#ifdef NK-s40
//#else
	scr.setColor(RGB & 0xffffff);
//#endif
	scr.fillRect(x,y, sizeX, sizeY);
}

// <=- <=- <=- <=- <=-




//#ifdef DOJA
//#endif




// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------


// INI-INI






//#ifdef J2ME

//#ifdef FULLSCREEN

private void listenerInit(String strLeft, String strRight) {}

//#else
//#endif

//#elifdef DOJA
//#endif





// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;
boolean gamePaused = false;

private long GameMilis, CPU_Milis;
private int  GameSleep;

private boolean gameRunning = false;

public void empieza() {
	System.gc();
	gameCreate(); System.gc();
	canvasInit(); System.gc();
	gameInit(); System.gc();

	gameRunning = true;

	while (!gameExit)
	{
		if (!gamePaused)
		{
			GameMilis = System.currentTimeMillis();

			//#ifdef LISTENER
			intKeyMenu = 0;
			//#endif

			soundTick();

	try {
			gameTick();
			gameDraw();
			soundTick();

	} catch (Exception e) {System.out.println("*** Logic ***"); e.printStackTrace();}

			GameSleep=(50-(int)(System.currentTimeMillis()-GameMilis));
			if (GameSleep<1) {GameSleep=1;}
			try	{Thread.sleep(GameSleep);} catch(Exception e) {}
		}

//	System.gc();

	}

	soundStop();

	gameDestroy();

	ga.destroyApp(true);
}

private final static int GAME_LOGOS = 0;
private final static int GAME_MENU_MAIN = 10;
private final static int GAME_MENU_SECOND = 25;
private final static int GAME_MENU_GAMEOVER = 30;
private final static int GAME_PLAY = 20;




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
	//savePrefs();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

private byte[] loadFile(String Nombre)
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





// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------






























// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

private int sizeAvailPersistent(String name) {
	int r = 0;
//#ifdef J2ME
	RecordStore rs;
	try {
		rs = RecordStore.openRecordStore(name, true);
		r = rs.getSizeAvailable();
		rs.closeRecordStore();
	} catch(Exception e) { }
//#else
//#endif
	return r;
}

private byte[] loadPersistent(String name) {
	byte[] r = null;
//#ifdef J2ME
	RecordStore rs;
	try {
		rs = RecordStore.openRecordStore(name, true);
		if (rs.getNumRecords() == 0) {
			r = null;
		} else {
			r = rs.getRecord(1);
		}
		rs.closeRecordStore();
	} catch(Exception e) { }
//#else
//#endif
	return r;
}

private boolean savePersistent(String name, byte[] data) {
	boolean r = false;
//#ifdef J2ME
	RecordStore rs;
	try {
		rs = RecordStore.openRecordStore(name, true);
		if (rs.getNumRecords() == 0) {
			rs.addRecord(data, 0, data.length);
		} else {
			rs.setRecord(1, data, 0, data.length);
		}
		rs.closeRecordStore();
	} catch(Exception e) {
		r = true;
	}
//#else
//#endif
	return r;
}

// <=- <=- <=- <=- <=-
//#ifdef J2ME

// *******************
// -------------------
// font - Engine
// ===================
// *******************

private static final int FONT_SMALL = 0;
private static final int FONT_MEDIUM = 1;
private static final int FONT_LARGE = 2;
private static final int FONT_CUSTOM1 = 3;

// -------------------
// font
// ===================

private void fontSet(int fontType) {
	switch (fontType) {
	case FONT_SMALL:
		printInit_Font( Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL) );
	break;

	case FONT_MEDIUM:
		printInit_Font( Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_MEDIUM) );
	break;

//#ifdef BIGTEXT
	case FONT_LARGE:
		printInit_Font( Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_LARGE) );
	break;
//#else
//#endif

	case FONT_CUSTOM1:
		printInit_Font(font1Img, font1Cor, 9, 0x20, 1);
	break;
	}
}

// -------------------
// font Height
// ===================

private int fontHeight(int fontType)
{
	fontSet(fontType);
	return printTextHeight();
}

// -------------------
// font Width
// ===================

private int fontWidth(int fontType, String str)
{
	fontSet(fontType);
	return printTextWidth(str);
}

// <=- <=- <=- <=- <=-

//#endif


//#ifdef DOJA
//#endif

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------


// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//


// **************************************************************************//
// **************************************************************************//
// Microjocs Football Manager
// ---------------------------------------------------------------
// Strategical Simulation / Pseudo-RPG Football Team Managing Game
// Programming by Carlos Carrasco
// **************************************************************************//
// **************************************************************************//

// -------------------
// game Init
// ===================

public static String[] loc = null;

//#ifdef SUBSETLANGS
//#elifndef DOJA

//#ifdef LANGES
	private static String[] locales = { "es", "ct", "de", "en", "fr", "it" };
	//#ifdef SMALLTEXT
	//#else
	private static String[] languageItems = { "Espa�ol", "Catal�", "Deutsch", "English", "Fran�ais", "Italiano" };
	//#endif
//#endif

//#ifdef LANGDE
//#endif

//#ifdef LANGFR
//#endif

//#ifdef LANGIT
//#endif

//#ifdef LANGEN
//#endif

//#else

// caso de doja, orden fijo, de momento...
//#endif

private void loadLocale(int l) {
//#ifdef J2ME
	byte[] t = loadFile("/loc" + locales[l]);
//#else
//#endif
	// 232 strings a 24/12/2004
	ByteArrayInputStream bais = new ByteArrayInputStream(t);
	DataInputStream dis = new DataInputStream(bais);
	loc = new String[233];
	try {
		for (int i = 1; i < 233; i++) {
			int ss = dis.readShort();
			byte[] sta = new byte[ss];
			dis.readFully(sta);

			//MONGOFIX=========================================
			char char_sta[] = new char[sta.length];
			for(int j = 0; j < sta.length; j++)
			{
				char_sta[j] = (char)(sta[j] < 0 ? sta[j] + 256 : sta[j]);
			}

			loc[i] = new String(char_sta);
		}
	} catch (IOException e) { }

	for (int i = 0; i < 13; i++) {
		league.trainingsNames[i] = loc[169+i];
	}

	for (int i = 0; i < 4; i++) {
		league.posAbbr[i] = loc[110+i];
	}

	for (int i = 0; i < 4; i++) {
		league.statsAbbr[i] = loc[114+i];
	}

	for (int i = 0; i < 4; i++) {
		league.statsNames[i] = loc[118+i];
	}
}

private boolean gameSound;
private boolean gameVibra;
private int selectedLanguage;

private boolean titleSongPlaying = false;
private void titleSongPlay(boolean doPlay) {
	if (doPlay) {
		if (!titleSongPlaying && gameSound) {
			soundPlay(0,0);
			titleSongPlaying = true;
		}
	} else {
		soundStop();
		titleSongPlaying = false;
	}
}

private void savePrefs() {
	titleSongPlay(gameSound);
	byte[] prefs = { (byte)selectedLanguage, (byte)(gameSound ? 1 : 0), 0, 0, 0, 0, 0, 0 };
	savePersistent("options", prefs);
}

public void gameInit() {
	byte[] prefs = loadPersistent("options");
	if (prefs == null) {
		selectedLanguage = 0;
		gameSound = true;
		savePrefs();
	} else {
		selectedLanguage = prefs[0];
		gameSound = prefs[1] == 1; 
	}

//#ifdef DOJA
//#else
	if (selectedLanguage >= locales.length)
		selectedLanguage = 0;
//#endif

	loadLocale(selectedLanguage);

	System.gc();
	Image spl = null;

//#ifdef J2ME
//#ifdef LOADCUSTOMFONT
	font1Cor = loadFile("/fuenteg.cor");
	font1Trans = loadFile("/trf");
	font1Img = loadImage("/fuenteg.png");
//#endif
	spl = loadImage("/jamdat.png");
	System.gc();
//#else
//#endif
	//league = new League();
	//league.load(plyn, plys, tms);
	//System.gc();

	formSplash(spl);
	currentForm = FORM_SPLASH;
}

// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
// --- IFORM/FORM "engine" como diria juan
// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------


private static final int COLOR_DEFAULT_BG = 0x3f3f6f;
private static final int COLOR_TEXT_FG = 0xffffff;
private static final int COLOR_PICKED_FG = 0xffff00;
private static final int COLOR_INJURED_FG = 0xff0000;
private static final int COLOR_SANCTIONED_FG = 0x00ff00;

private static final int COLOR_BOX_FG = 0x0099dd;

private static final int COLOR_SCROLLBAR_FG = 0x0077aa;
private static final int COLOR_SCROLLBAR_BG = 0x00ddff;

private static final int COLOR_POPUP_GOOD = 0x00B000;
private static final int COLOR_POPUP_BAD = 0xB00000;

private static final int COLOR_WHITE = 0xffffff;

private final static int IFORM_BEHAVIOUR_SINGLE_SELECT_LIST = 1;
private final static int IFORM_BEHAVIOUR_PRESENTATION_LIST = 2;
private final static int IFORM_BEHAVIOUR_ANY_KEY = 3;
private final static int IFORM_BEHAVIOUR_YES_NO = 4;
private final static int IFORM_BEHAVIOUR_SCROLL_TEXT = 5;
private final static int IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO = 6;
private int iformBehaviour = -1;

// datos retenidos de la pantalla actual

/*
Header1
Header2
--------------------------------





--------------------> ListStartY
item 1
item 2
item 3
*/

private Image fieldImage = null;
private Image redBullet = null;
private Image blackBullet = null;
private Image blueBullet = null;
private Image iformBackground = null;
private Image iformTopBackground = null;

private String iformDataHeaderTop = null;
private String iformDataHeaderSmall = null;
private int iformDataHeaderSmallCalcY = 0;
private String iformFocusedText = null;
private int iformFocusAvailWidth = 0;
private int iformFocusY = 0;
private int iformFocusItem = 0;
private String iformDataSoftLeft = null;
private String iformDataSoftRight = null;
private String[][] iformDataListItems = null;
private String[] iformDataListColumnNames = null;
private byte[] iformDataListItemsAttr = null;
private boolean iformFocusDesired;
private boolean iformPaintSoftBack;

private boolean iformModalPopup;
private boolean iformWaitDisplay;
private String iformPopupText;
private int iformPopupColor;

private int iformFontTopHeader;
private int iformFontSmallHeader;
private int iformFont;
private int iformFontSmall;

private int iformScrollTopItem = 0;
private int iformSelectedItem = 0;
private int iformListStartY = -1;			// -1: auto 

//#ifdef SMALLTEXT
//#elifdef BIGTEXT
	//#ifdef DOJA
	//#else
		private int iformFontDefault = FONT_LARGE;
		private int iformFontDefaultSmall = FONT_SMALL;
		private int iformFontDefaultUltraSmall = FONT_CUSTOM1;
		private int iformFontDB = FONT_SMALL;
	//#endif

private int iformHeaderTopMargin = 2;
private int iformHeaderSeparatorMargin = 8;

private int iformListLineSpacing = 5;
private int iformSecondaryColumnWidth = 20;

//#else
//#endif

private void iformPaint() {
	if (screenLost) {
		iformDisplayAll();
	}
	if (animationLost) {
		iformDisplayAnimation();
	}
}

private int iformTickerStepA = 0;
private int iformTickerStepB = 0;

private final static int MASK_PICKED = 1;
private final static int MASK_INJURED = 2;
private final static int MASK_SANCTIONED = 4;

private int lastColor;
private void proxySetColor(int c) {
	lastColor = c;
	scr.setColor(c);
}

private void attributedColor(int i) {
	// atributos del texto
	if (iformDataListItemsAttr != null) {
		if ((iformDataListItemsAttr[i] & MASK_PICKED) != 0) {
			proxySetColor(COLOR_PICKED_FG);
		} else if ((iformDataListItemsAttr[i] & MASK_INJURED) != 0) {
			proxySetColor(COLOR_INJURED_FG);
		} else if ((iformDataListItemsAttr[i] & MASK_SANCTIONED) != 0) {
			proxySetColor(COLOR_SANCTIONED_FG);
		} else {
			proxySetColor(COLOR_TEXT_FG);
		}
	} else {
		proxySetColor(COLOR_TEXT_FG);
	}
}

private Image altTopBackground;
private void iformDisplayHeaderAnimated() {
	boolean altCalc = pickInMatch ||
			currentForm == FORM_MATCH_POST_ROUND_STATS ||
			currentForm == FORM_MATCH_POST_EUROPE_STATS ||
			currentForm == FORM_MATCH_POST_MATCH_STATS;
	Image tb = null;
	if (altCalc) {
		tb = altTopBackground;
	} else {
		tb = iformTopBackground;
	}

	if (tb != null) {
//#ifndef NK-s40
//#else

		int x = 0;
		while (x < canvasWidth) {
			imageDraw(tb, x, 0);
			x += tb.getWidth();
		}
//#endif
	}

	proxySetColor(COLOR_TEXT_FG);
	fontSet(iformFontTopHeader);
	int accY = iformHeaderTopMargin;
//#ifndef TICKERTOP
//#endif

	if (iformTopBackground == null) {
		accY += fontHeight(iformFontTopHeader);
	} else {
		if (altCalc) {
			if (altTopBackground != null) {
				accY = altTopBackground.getHeight() - fontHeight(iformFontSmallHeader) - 1;
			} else {
				accY += fontHeight(iformFontTopHeader);
			}
		} else {
			accY = iformTopBackground.getHeight() - fontHeight(iformFontSmallHeader) - 1;
		}

	}

//#ifdef TICKERTOP
//#else
	if (iformDataHeaderSmall != null) {
		String cStr = iformDataHeaderSmall;
		int font = iformFontSmallHeader;

//#endif
		if (printTextWidth(cStr) > canvasWidth) {
			// subtitulo, con animacion
			fontSet(font);
			int w = printTextWidth(cStr);
			// "real"
			printDraw(cStr, iformTickerStepA*3, accY, PRINT_SHADOW);
			// "extra"
			int ex = w + 20 + iformTickerStepA*3;
			if (ex < canvasWidth) {
				printDraw(cStr, ex, accY, PRINT_SHADOW);
			}
			// swap
			if (ex <= 0) {
				iformTickerStepA = 0;
			}
		} else {

			fontSet(font);
			printDraw(cStr, 0, accY, PRINT_HCENTER|PRINT_SHADOW);
		}
	}
}

private void iformDisplayAnimation() {
	if (iformModalPopup || iformWaitDisplay)
		return;
	if (currentForm == FORM_MATCH_PLAY || currentForm == FORM_MATCH_POST)
		return;
	// setup
	printCreate();
	iformDisplayHeaderAnimated();
	// ticker de focus
	if (iformFocusedText != null && iformFocusDesired) {
		fontSet(iformFont);
		String[] subdiv = null;
		if (iformDataListColumnNames != null) {
			subdiv = new String[iformDataListColumnNames.length];
			if (iformDataListItems[0][iformFocusItem].indexOf("|") == 0) {
				int lasti = 0;
				for (int k = 0; k < subdiv.length; k++) {
					int nexti = iformDataListItems[0][iformFocusItem].indexOf("|", lasti + 1);
					subdiv[k] = iformDataListItems[0][iformFocusItem].substring(lasti+1, nexti);
					lasti = nexti;
				}
			} else {
				for (int k = 0; k < subdiv.length; k++) {
					if (iformDataListItems[k] != null) {
						subdiv[k] = iformDataListItems[k][iformFocusItem];
					} else {
						subdiv[k] = "";
					}
				}
			}
		} else {
			subdiv = new String[1];
			subdiv[0] = iformFocusedText;
		}
		if (printTextWidth(subdiv[0]) > iformFocusAvailWidth) {
			String t = subdiv[0];
//#ifdef DBCUSTOMFONT
//#endif
			// pintada 1 del fondo del focus: todo el ancho
			rectFill(COLOR_BOX_FG, 0, iformFocusY, canvasWidth, fontHeight(iformFont)+1);
			// item animado, con todo el overflow que haga falta
			int w = printTextWidth(t);
			attributedColor(iformFocusItem);
			printDraw(t, iformTickerStepB*3, iformFocusY, PRINT_SHADOW);
			// "extra"
			int ex = w + 20 + iformTickerStepB*3;
			if (ex < canvasWidth) {
				printDraw(t, ex, iformFocusY, PRINT_SHADOW);
			}
			// swap
			if (ex <= 0) {
				iformTickerStepB = 0;
			}
			// pintada 2 del fondo del focus: desde el final del item tickeado
			rectFill(COLOR_BOX_FG, iformFocusAvailWidth, iformFocusY,
				canvasWidth - iformFocusAvailWidth, fontHeight(iformFont)+1);
			attributedColor(iformFocusItem);
			// resto de columnas, asumimos que no hay ticker. si llega a ser necesario MAS tickers
			// entones hay que generalizarlo todo
			if (iformDataListColumnNames != null) {
				// columna a columna
				for (int j = 1; j < iformDataListColumnNames.length; j++) {
					int x = 0;
					if (j > 0) {
						// -5 --> ancho de la scrollbar
						x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 5;
					}
					//printDraw(iformDataListItems[j][iformFocusItem], x, iformFocusY, PRINT_SHADOW);
					printDraw(subdiv[j], x, iformFocusY, PRINT_SHADOW);
				}
			}
			// reparar danyos extras
			displayScrollbar();
			displaySeparators();
		}
	}
}


// se saca esto fuera pada poder pintar la scrollbar dentro de la anim
private int totalHeight;
private int availHeight;
private int sbY;
private void displayScrollbar() {
	// pintar scrollbar si hace falta
	if (availHeight < totalHeight) {
		int sbHeight = (availHeight * availHeight) / totalHeight;
		int slHeight = (availHeight * (fontHeight(iformFont) + iformListLineSpacing)) / totalHeight;
		//rectFill(COLOR_SCROLLBAR_BG, canvasWidth - 4, sbY - 1, 4, availHeight + 2);
		rectFill(COLOR_SCROLLBAR_BG, canvasWidth - 4, sbY - 1, 4, canvasHeight - (sbY - 1));
		rectFill(COLOR_SCROLLBAR_FG, canvasWidth - 3, sbY + slHeight*iformScrollTopItem,
			2, sbHeight);
	}
}

// lo mismo con los separadores de columnas
private int colHeadBot;
private void displaySeparators() {
	if (iformDataListColumnNames != null) {
		for (int j = 0; j < iformDataListColumnNames.length; j++) {
			if (j > 0) {
				// -5 --> ancho de la scrollbar
				// -2 --> ajuste de pixels para no superponer el texto y el separador
				int x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 5 - 2;
				rectFill(COLOR_SCROLLBAR_BG, x, colHeadBot, 1, canvasHeight - colHeadBot - 1);
			}
		}
	}
}

private void iformDisplayAll() {
	// setup
	printCreate();
	iformFocusedText = null;
	iformFocusY = 0;
	iformFocusAvailWidth = canvasWidth;
	// fondo
	/*if (iformTopBackground != null) {
		// hackitithack
		if (!pickInMatch) {
			imageDraw(iformTopBackground, 0, 0);
		}
	}*/
	if (iformBackground != null && iformTopBackground != null) {
		// tilear background
		//int y = iformTopBackground.getHeight();
		int y = 0;
		while (y < canvasHeight) {
			int x = 0;
			while (x < canvasWidth) {
				imageDraw(iformBackground, x, y);
				x += iformBackground.getWidth();
			}
			y += iformBackground.getHeight();
		}
	}
	if (iformBackground != null && iformTopBackground == null) {
		// HACK HACK HACK
		if (iformBehaviour == IFORM_BEHAVIOUR_ANY_KEY) {
			rectFill(COLOR_WHITE, 0, 0, canvasWidth, canvasHeight);
		}
		imageDraw(iformBackground);
	}
	// cabeceras
	iformDisplayHeaderAnimated();
	int accY = 0;
	// AHORA CON TOP BACKGROUND SE FUERZA UN ACCY SIEMPRE
	// y ahora no...
	if (iformTopBackground != null) {
		accY = iformTopBackground.getHeight() + iformHeaderSeparatorMargin;
	}
	if (altTopBackground != null && pickInMatch) {
		accY = altTopBackground.getHeight() + iformHeaderSeparatorMargin;
	}
	int headerY = accY;
	// iformListStartY puede forzar otra posicion para empezar el menu/listado
	int y = iformListStartY;
	if (y == -1) {
		y = accY;
	}
	// --------------------------------------------
	// items
	if (iformDataListItems == null)
		return;
	// header de las columnas
	colHeadBot = 0;
	if (iformDataListColumnNames != null) {
		fontSet(iformFontSmallHeader);
		rectFill(COLOR_SCROLLBAR_BG, 0, y, canvasWidth, 1);
		y += 2;
		for (int j = 0; j < iformDataListColumnNames.length; j++) {
			int x = 0;
			if (j > 0) {
				// -6 --> ancho de la scrollbar
				x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 6;
			}
			printDraw(iformDataListColumnNames[j], x, y, 0);
		}
		y += fontHeight(iformFontSmallHeader) + 1;
		rectFill(COLOR_SCROLLBAR_BG, 0, y, canvasWidth, 1);
		colHeadBot = y;
		y += 1; //iformHeaderSeparatorMargin;
	}
	fontSet(iformFont);
	// logica del scroll
	int minOpt = iformScrollTopItem;
	int maxOpt = minOpt + ((canvasHeight - y) / (fontHeight(iformFont) + iformListLineSpacing)) - 1;
	if (iformSelectedItem < minOpt) {
		iformScrollTopItem = iformSelectedItem;
	} else if (iformSelectedItem > maxOpt) {
		iformScrollTopItem = iformSelectedItem - (maxOpt - minOpt);
	}
	// decidir si hace falta scrollbar, y entonces pintarla o no
	totalHeight = (fontHeight(iformFont) + iformListLineSpacing) * iformDataListItems[0].length;
	availHeight = canvasHeight - y - 4;
	sbY = y;
	// el pintado en si mismo esta despues de los textos, para poder pintar encima
	// iterar items y pintarlos
	proxySetColor(COLOR_TEXT_FG);
	if (iformDataListColumnNames != null) {
		iformFocusAvailWidth = canvasWidth - (iformDataListColumnNames.length-1)*iformSecondaryColumnWidth - 10;
	} else {
		iformFocusAvailWidth = canvasWidth;
	}
	int topi = iformScrollTopItem;
	if (topi < 0) {
		topi = 0;
	}
	for (int i = topi; i < iformDataListItems[0].length; i++) {
		// saltarse items que se salen
		if ((y + fontHeight(iformFont)) >= canvasHeight) {
			break;
		}
		// dibujar una caja en el seleccionado
		if (iformSelectedItem == i && iformFocusDesired) {
			iformFocusY = y;
			iformFocusedText = iformDataListItems[0][i];
			iformFocusItem = i;
			rectFill(COLOR_BOX_FG, 0, y, canvasWidth, fontHeight(iformFont)+1);
			proxySetColor(COLOR_TEXT_FG);
		}

		String[] subdiv = null; 
		if (iformDataListItems[0][i].indexOf("|") == 0) {
			subdiv = new String[iformDataListColumnNames.length];
			int lasti = 0;
			for (int k = 0; k < subdiv.length; k++) {
				int nexti = iformDataListItems[0][i].indexOf("|", lasti + 1);
				subdiv[k] = iformDataListItems[0][i].substring(lasti+1, nexti);
				lasti = nexti;
			}
		}

		if (iformDataListColumnNames != null) {
			// columna a columna
			for (int j = 0; j < iformDataListColumnNames.length; j++) {
				int x = 0;
				if (j > 0) {
					// -5 --> ancho de la scrollbar
					x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 5;
				}
				// atributos del texto
				attributedColor(i);
				// HACK HACK HACK HACK!!!!!
				// pero da igual, la unica columna abreviable es la primera, que ya tiene
				// el avail calculado para el ticker
				String t = "";
				if (subdiv != null) {
					t = subdiv[j];
				} else {
					t = iformDataListItems[j][i];
				}

//#ifdef DBCUSTOMFONT
//#endif
				if (iformSelectedItem == i && iformFocusDesired) {
					printDrawAbbr(t, x, y, PRINT_SHADOW, iformFocusAvailWidth);
				} else {
					printDrawAbbr(t, x, y, 0, iformFocusAvailWidth);
				}
			}
		} else {
			// atributos del texto
			attributedColor(i);
			// 1 columna
			int it = i;
			if (it < 0) {
				it = 0;
			}
			if (it < iformDataListItems[0].length) {
				if (iformSelectedItem == it && iformFocusDesired) {
					printDrawAbbr(iformDataListItems[0][it], 1, y, PRINT_LEFT | PRINT_SHADOW, canvasWidth);
				} else {
					printDrawAbbr(iformDataListItems[0][it], 1, y, PRINT_LEFT, canvasWidth);
				}
			}
		}
		y += fontHeight(iformFont) + iformListLineSpacing;
	}
	// pintar scrollbar si hace falta
	displayScrollbar();
	
	// divisores verticales de columnas
	displaySeparators();

	// graficos custom de la pantalla
	switch (currentForm) {
		case FORM_TACTICS_FORMATION:
		case FORM_TACTICS_POSITION:
			customDrawTacticsFormation(headerY);
		break;
		case FORM_MATCH_PLAY:
		case FORM_MATCH_POST:
			customDrawMatchPlay();
		break;
	}

	// popup informativo
	if (iformModalPopup) {
		//#ifdef SMALLCANVAS
		//#elifdef MEDIUMCANVAS
		//#else
		int x = 10;
		y = x;
		int w = canvasWidth - x*2;
		int h = canvasHeight - y*2;
		printCreate(x + 5, y + 5, w - 10, h - 10);
		int corr = 5;
		//#endif
		fontSet(iformFontDefaultSmall);
		rectFillA(iformPopupColor, 230, x, y, w, h);
		String[] s = printTextBreak(iformPopupText, w - 10);
		int il = fontHeight(iformFontDefaultSmall) + iformListLineSpacing;
		y = (h - s.length*il) / 2;
		proxySetColor(COLOR_TEXT_FG);
		for (int i = 0; i < s.length; i++) {
			printDraw(s[i], 0, y - corr, PRINT_HCENTER|PRINT_SHADOW);
			y += il;
		}
		printCreate();
	}

	// please wait, yer phone thingie si teh suxor
	if (iformWaitDisplay) {
		fontSet(iformFontDefaultSmall);
		int w = printTextWidth(loc[160]) + 6;
		int h = fontHeight(iformFontDefaultSmall) + 4;
		int x = canvasWidth/2 - w/2;
		y = canvasHeight/2 - h/2;
		rectFillA(COLOR_SCROLLBAR_BG, 230, x, y, w, h);
		printCreate(x, y, w, h);
		proxySetColor(COLOR_TEXT_FG);
		printDraw(loc[160], 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
		printCreate();
	}

	// softbuttons
	//#ifdef FULLSCREEN
	if (iformDataSoftLeft != null || iformDataSoftLeft != null) {
		if (iformPaintSoftBack) {
			rectFill(COLOR_SCROLLBAR_BG, 0, canvasHeight - fontHeight(iformFontDefault) - 2,
				canvasWidth, fontHeight(iformFontDefault) + 2);
		}
	}
	if (iformDataSoftLeft != null) {
		/*if (iformPaintSoftBack) {
			rectFillA(COLOR_SCROLLBAR_BG, 128, 0, canvasHeight - fontHeight(iformFontDefault) - 2,
				canvasWidth/2 - 3, fontHeight(iformFontDefault) + 2);
		}*/
		fontSet(iformFontDefault);
		printCreate(0, canvasHeight - fontHeight(iformFontDefault),
			canvasWidth/2 - 3, fontHeight(iformFontDefault));
		proxySetColor(COLOR_TEXT_FG);
		printDraw(iformDataSoftLeft, 0, 0, PRINT_HCENTER|PRINT_QSHADOW);
	}
	if (iformDataSoftRight != null) {
		/*if (iformPaintSoftBack) {
			rectFillA(COLOR_SCROLLBAR_BG, 128, canvasWidth - canvasWidth/2 + 3,
				canvasHeight - fontHeight(iformFontDefault)- 2,
				canvasWidth/2 - 3, fontHeight(iformFontDefault) + 2);
		}*/
		fontSet(iformFontDefault);
		printCreate(canvasWidth - canvasWidth/2 + 3,
			canvasHeight - fontHeight(iformFontDefault),
			canvasWidth/2 - 3, fontHeight(iformFontDefault));
		proxySetColor(COLOR_TEXT_FG);
		printDraw(iformDataSoftRight, 0, 0, PRINT_HCENTER|PRINT_QSHADOW);
	}
	//#else
	//#endif
}

private void customDrawTacticsFormation(int headerY) {
	int topX = (canvasWidth - fieldImage.getWidth())/2;
	int topY = headerY;
	//printCreate(topX, topY, fieldImage.getWidth(), fieldImage.getHeight());
	if (fieldImage != null) {
		imageDraw(fieldImage, topX, topY);
	}
	fontSet(iformFontDefaultUltraSmall);
	proxySetColor(0xffffff);
	for (int i = 1; i < 12; i++) {
		int x = league.formationsXY[focusedFormation][(i-1)*2];
		int y = league.formationsXY[focusedFormation][(i-1)*2+1];
		//#ifdef SMALLCANVAS
		//#endif
		String s = "" + i;
		Image b = blackBullet;
		if (iformDataListItemsAttr != null) {
			if ((iformDataListItemsAttr[i-1] & MASK_INJURED) != 0) {
				b = redBullet;
			}
			if ((iformDataListItemsAttr[i-1] & MASK_SANCTIONED) != 0) {
				b = redBullet;
			}
		}	
		if (focusedPlayer == i-1) {
			b = blueBullet;
		}
		printCreate(topX+x+1, topY+y-1, b.getWidth(), b.getHeight());
		imageDraw(b, topX+x, topY+y);
		//#ifndef SMALLCANVAS
		//#endif
	}
	if (focusedPlayer < 11)
		return;
	String s = loc[6];
	if (focusedPlayer >= 16 && focusedPlayer < 30) {
		s = loc[7];
	}
	printCreate(topX, topY, fieldImage.getWidth(), fieldImage.getHeight());
	fontSet(iformFontDefaultUltraSmall);
	int w = printTextWidth(s);
	int h = fontHeight(iformFontDefaultUltraSmall);
	int x = (fieldImage.getWidth() - w) / 2;
	int y = (fieldImage.getHeight() - h) / 2;
	rectFill(0, topX+x, topY+y, w, h);
	proxySetColor(0xffffff);
	printDraw(s, x, y, 0);
}

//#ifdef MARCADOR96
//#elifdef MARCADOR101
//#elifdef MARCADOR116
//#elifdef MARCADOR120
//#elifdef MARCADOR128
//#elifdef MARCADOR130
//#elifdef MARCADOR132
//#elifdef MARCADOR162
//#elifdef MARCADOR176
private int dmpScoreFont = iformFontDefaultSmall;
private int dmpTimerFont = iformFontDefaultSmall;
private int[] dmpLeftTeamRect = { 4, 3, 67, 13 };
private int[] dmpRightTeamRect = { 105, 3, 67, 13 };
private int[] dmpLeftScoreRect = { 76, 2, 10, 12 };
private int[] dmpRightScoreRect = { 90, 2, 10, 12 };
private int[] dmpTimerRect = { 68, 25, 40, 12 };
//#elifdef MARCADOR240
//#endif

private void customDrawMatchPlay() {
	// rectangulo de eventos calculado
	int evx = 3;
	int evy = 0;
	int evw = canvasWidth - 6;
	int evh = canvasHeight - fontHeight(iformFontDefault) - 3;
	printCreate(evx, evy, evw, evh);
	// eventos
	fontSet(iformFontDB);
	proxySetColor(0xffffff);
	String[] s = printTextBreakInv(mpEventsText, evw);
	int y = evh - fontHeight(iformFontDB);
	for (int i = 0; i < s.length; i++, y -= fontHeight(iformFontDB)) {
		if (s[i] != null)
			printDraw(s[i], 0, y, 0);
	}
	// poner fondo de arriba para clipear el texto
//#ifndef COMPONERMARCADOR
	imageDraw(iformTopBackground, 0, 0);
//#else
//#endif
	// nombres equipos
	fontSet(iformFontDB);
	proxySetColor(0xffffff);
	printCreateA(dmpLeftTeamRect);
	printDrawAbbrFullWidthR(league.journeyMatch[0].name);
	printCreateA(dmpRightTeamRect);
	printDrawAbbrFullWidth(league.journeyMatch[1].name);
	// puntuacion actual
	//scr.setColor(0);
	fontSet(dmpScoreFont);
	printCreateA(dmpLeftScoreRect);
	printDraw("" + mpScoreLeft, 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
	printCreateA(dmpRightScoreRect);
	printDraw("" + mpScoreRight, 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
	// timer
	fontSet(dmpTimerFont);
	printCreateA(dmpTimerRect);
	printDraw(mpTimeText, 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
}

private final static int HIEVENT_IDLE = 1;
private final static int HIEVENT_ANY = 2;
private final static int HIEVENT_YES = 3;
private final static int HIEVENT_NO = 4;
private final static int HIEVENT_FOCUS_N = 100;
private final static int HIEVENT_SELECTED_N = 200;

private int hiEventFocusedItem(int hiEvent) {
	if (hiEvent >= HIEVENT_FOCUS_N && hiEvent < HIEVENT_FOCUS_N + 100) {
		return hiEvent  - HIEVENT_FOCUS_N;
	}
	return -1;
}

private int hiEventSelectedItem(int hiEvent) {
	if (hiEvent >= HIEVENT_SELECTED_N && hiEvent < HIEVENT_SELECTED_N + 100) {
		return hiEvent  - HIEVENT_SELECTED_N;
	}
	return -1;
}

private long nextAnimFrame = -1;
private int formTick() {
	// update animaciones
	// tick pasivo, se detectara en el siguiente tick de juego
	long t = System.currentTimeMillis();
	if (t > nextAnimFrame) {
		nextAnimFrame = t + 150;
		animationLost = true;
		iformTickerStepA--;
		iformTickerStepB--;
	}

	// gestion eventos
	int r = HIEVENT_IDLE;
	if (iformModalPopup) {
		if (intKeyCode != 0) {
			iformModalPopup = false;
			// forzar pintado desde aqui mismo
			screenLost = true;
			r = HIEVENT_YES;
		} else {
			r = HIEVENT_IDLE;
		}
		// eliminar repeticion
		intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; intKeyCode=0;
		return r;
	}

	switch (iformBehaviour) {
		case IFORM_BEHAVIOUR_SINGLE_SELECT_LIST:
		 	if (intKeyMisc == 1) { // arriba 5
				int i = iformSelectedItem;
				i-=5;
				if (i >= 0) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
				} else {
					r = HIEVENT_FOCUS_N + 0;
					iformSelectedItem = 0;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
					//r = HIEVENT_IDLE;
				}
			} else if (intKeyMisc == 7) { // abajo 5
				int i = iformSelectedItem;
				i+=5;
				if (i < iformDataListItems[0].length) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
				} else {
					r = HIEVENT_FOCUS_N + iformDataListItems[0].length - 1;
					iformSelectedItem = iformDataListItems[0].length - 1;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
					//r = HIEVENT_IDLE;
				}
			} else if (intKeyY == 1) { // abajo
				int i = iformSelectedItem;
				i++;
				if (i < iformDataListItems[0].length) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
				} else {
					r = HIEVENT_FOCUS_N + 0;
					iformSelectedItem = 0;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
					//r = HIEVENT_IDLE;
				}
			} else if (intKeyY == -1) { // arriba
				int i = iformSelectedItem;
				i--;
				if (i >= 0) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
				} else {
					r = HIEVENT_FOCUS_N + iformDataListItems[0].length - 1;
					iformSelectedItem = iformDataListItems[0].length - 1;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
					//r = HIEVENT_IDLE;
				}
			} else if (intKeyMenu == 1 || intKeyMenu == 2 || intKeyX == 1) { // izq, fire, derecha: comprobar que funciona bien
				r = HIEVENT_SELECTED_N + iformSelectedItem;
			} else if (intKeyMenu == -1 || intKeyX == -1) {
				// pequenyo hack de usabilidad: izquierda selecciona la ultima opcion de manera automatica
				r = HIEVENT_SELECTED_N + iformDataListItems[0].length - 1;
			}
		break;
		case IFORM_BEHAVIOUR_SCROLL_TEXT:
			r = HIEVENT_IDLE;
			if (intKeyY == 1) { // abajo
				// avanzar 20%
				iformScrollTopItem++;
				if (iformScrollTopItem > iformDataListItems[0].length-2) {
					iformScrollTopItem = iformDataListItems[0].length-2;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
			} else if (intKeyY == -1) { // arriba
				// retroceder 20%
				iformScrollTopItem--;
				if (iformScrollTopItem < 0) {
					iformScrollTopItem = 0;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
			} else if (intKeyMenu == -1 || intKeyX == -1) {
				// pequenyo hack de usabilidad: izquierda selecciona la ultima opcion de manera automatica
				r = HIEVENT_ANY;
			}
		break;
		case IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO:
			r = HIEVENT_IDLE;
			if (intKeyY == 1) { // abajo
				// avanzar 20%
				iformScrollTopItem++;
				if (iformScrollTopItem > iformDataListItems[0].length-2) {
					iformScrollTopItem = iformDataListItems[0].length-2;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
			} else if (intKeyY == -1) { // arriba
				// retroceder 20%
				iformScrollTopItem--;
				if (iformScrollTopItem < 0) {
					iformScrollTopItem = 0;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
			} else if (intKeyMenu == -1 || intKeyX == -1) {
				r = HIEVENT_NO;
			} else if (intKeyMenu == 1 || intKeyX == 1) {
				r = HIEVENT_YES;
			}
		break;
		case IFORM_BEHAVIOUR_ANY_KEY:
			if (intKeyCode != 0) {
				r = HIEVENT_ANY;
			} else {
				r = HIEVENT_IDLE;
			}
		break;
		case IFORM_BEHAVIOUR_YES_NO:
			if (intKeyMenu == 1 || intKeyX == 1) {
				r = HIEVENT_YES;
			} else if (intKeyMenu == -1 || intKeyX == -1) {
				r = HIEVENT_NO;
			} else {
				r = HIEVENT_IDLE;
			}
		break;
	}
	// eliminar repeticion
	intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; intKeyCode=0;
	return r;
}

// alto nivel: un menu simple con columnas y atributos
private void formPlainMenu(String title, String subtitle, String[][] items, String[] labels, byte[] attr) {
	// reset fuentes
	iformFontTopHeader = iformFontDefault;
	iformFontSmallHeader = iformFontDefaultSmall;
	iformFont = iformFontDefault;
	iformFontSmall = iformFontDefaultSmall;
	// reset posiciones
	iformScrollTopItem = 0;
	iformSelectedItem = 0;
	iformListStartY = -1;
	iformFocusDesired = true;
	// fondo
	//iformBackground = defaultBackground;
	//iformTopBackground = defaultTopBackground;
	// cabeceras
	iformDataHeaderTop = title;
	iformDataHeaderSmall = subtitle;
	// items
	iformDataListItems = items;
	iformDataListColumnNames = labels;
	iformDataListItemsAttr = attr;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	// comportamiento interactivo
	iformBehaviour = IFORM_BEHAVIOUR_SINGLE_SELECT_LIST;
	// forzar un paint
	screenLost = true;
}

// alto nivel: un menu simple con columnas
private void formPlainMenu(String title, String subtitle, String[][] items, String[] labels) {
	formPlainMenu(title, subtitle, items, labels, null);
}

// alto nivel: un menu simple
private void formPlainMenu(String title, String subtitle, String[] items) {
	formPlainMenu(title, subtitle, null, null);
	String[][] i = new String[1][];
	i[0] = items;
	iformDataListItems = i;
}

// alto nivel: un listado no interactivo (solo "any key")	
private void formPlainList(String title, String subtitle, String[] items) {
	// reset fuentes
	iformFontTopHeader = iformFontDefault;
	iformFontSmallHeader = iformFontDefaultSmall;
	iformFont = iformFontDefault;
	iformFontSmall = iformFontDefaultSmall;
	// reset posiciones
	iformScrollTopItem = 0;
	iformSelectedItem = 0;
	iformListStartY = -1;
	iformFocusDesired = false;
	// fondo
	//iformBackground = defaultBackground;
	//iformTopBackground = defaultTopBackground;
	// cabeceras
	iformDataHeaderTop = title;
	iformDataHeaderSmall = subtitle;
	// items
	String[][] i = new String[1][];
	i[0] = items;
	iformDataListItems = i;
	iformDataListColumnNames = null;
	iformDataListItemsAttr = null;
	iformPaintSoftBack = true;
	//iformDataSoftLeft = null;
	iformDataSoftLeft = loc[1];
	iformDataSoftRight = null;
	// comportamiento interactivo
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT;
	// forzar un paint
	screenLost = true;
}

// alto nivel: un listado no interactivo (solo "any key")	
private void formPlainListYesNo(String title, String subtitle, String left, String right, String[] items) {
	formPlainList(title, subtitle, items);
	iformDataSoftLeft = left;
	iformDataSoftRight = right;
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO;
}

// alto nivel: listado con pregunta de si, no
private void formYesNoList(String title, String subtitle, String left, String right, String[] items) {
	formYesNoList(title, subtitle, left, right, items, true);
}

// alto nivel: listado con pregunta de si, no
private void formYesNoList(String title, String subtitle, String left, String right, String[] items, boolean paintSoftBack) {
	formPlainList(title, subtitle, items);
	iformBehaviour = IFORM_BEHAVIOUR_YES_NO;
	iformDataSoftLeft = left;
	iformDataSoftRight = right;
	iformPaintSoftBack = paintSoftBack;
}

// alto nivel: listado con pregunta de ok
private void formOKList(String title, String subtitle, String right, String[] items) {
	formPlainList(title, subtitle, items);
	iformBehaviour = IFORM_BEHAVIOUR_YES_NO;
	iformDataSoftLeft = null;
	iformDataSoftRight = right;
	iformPaintSoftBack = true;
}

private void formModalPopup(String txt, int c) {
	iformPopupColor = c;
	iformModalPopup = true;
	iformPopupText = txt;
	screenLost = true;
	// pequenyo hack, para simplificar
	if (c == COLOR_POPUP_BAD) {
		soundPlay(1,1);
	} else if (c == COLOR_POPUP_GOOD) {
		soundPlay(5,1);
	}
}

private void formWait(boolean v) {
	iformWaitDisplay = v;
	screenLost = true;
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// HACK: forzar un repaint!
	gameDraw();
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}

// alto nivel: caratula	
private void formSplash(Image s) {
	// reset fuentes
	iformFontTopHeader = iformFontDefault;
	iformFontSmallHeader = iformFontDefaultSmall;
	iformFont = iformFontDefault;
	iformFontSmall = iformFontDefaultSmall;
	// reset posiciones
	iformScrollTopItem = 0;
	iformSelectedItem = -1;
	iformListStartY = -1;
	iformFocusDesired = false;
	// fondo
	iformBackground = s;
	iformTopBackground = null;
	// cabeceras
	iformDataHeaderTop = null;
	iformDataHeaderSmall = null;
	// items
	iformDataListItems = null;
	iformDataListColumnNames = null;
	iformDataListItemsAttr = null;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	// comportamiento interactivo
	iformBehaviour = IFORM_BEHAVIOUR_ANY_KEY;
	// forzar un paint
	screenLost = true;
}

// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
// --- Logica de interaccion
// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------

private final static int FORM_SPLASH = 1;
private final static int FORM_START_MENU = 2;
private final static int FORM_MAIN_MENU = 3;
private final static int FORM_ABOUT = 4;
private final static int FORM_LEAGUE = 5;
private final static int FORM_CLASSIFICATION = 6;
private final static int FORM_CHOOSE_LEAGUE = 7;
private final static int FORM_CHOOSE_TEAM = 8;
private final static int FORM_MATCH = 9;
//final static int FORM_CHOOSE_TEAM_STATS = 10;
private final static int FORM_TOP_PLAYERS = 11;
private final static int FORM_TRAINING_CHOOSE_TRAINING = 12;
private final static int FORM_TRAINING_CHOOSE_PLAYERS = 13;
private final static int FORM_TRAINING_RESULT = 14;
private final static int FORM_TRANSFERS_MAIN = 15;
//final static int FORM_TRANSFERS_HIRE_SELECT = 16;
private final static int FORM_TRANSFERS_FIRE_SELECT = 17;
private final static int FORM_TACTICS_MAIN = 18;
private final static int FORM_TACTICS_FORMATION = 19;
private final static int FORM_TACTICS_POSITION = 20;
private final static int FORM_SAVE = 21;
private final static int FORM_MATCH_PLAY = 22;
private final static int FORM_MATCH_POST = 23;
private final static int FORM_HELP = 24;
private final static int FORM_END_LEAGUE = 25;
private final static int FORM_HELP_GENERIC = 26;
private final static int FORM_OPTIONS = 27;
private final static int FORM_OPTIONS_LANGUAGE = 28;
private final static int FORM_TRANSFERS_HIRE_SEL_LEAGUE = 29;
private final static int FORM_TRANSFERS_HIRE_SEL_TEAM = 30;
private final static int FORM_TRANSFERS_HIRE_SEL_PLAYER = 31;
private final static int FORM_CLASSIFICATION_EXTRA = 32;
private final static int FORM_SPLASH2 = 33;
private final static int FORM_SPLASH3 = 34;
private final static int FORM_SPLASH_LEGAL = 35;
private final static int FORM_CREDITS = 36;
private final static int FORM_CLASS_CHAMPIONS = 37;
private final static int FORM_TRANSFERS_HIRE_CONFIRM = 38;
private final static int FORM_TACTICS_POSITION_CONFIRM = 39;
private final static int FORM_MATCH_POST_ROUND_STATS = 40;
private final static int FORM_MATCH_POST_EUROPE_STATS = 41;
private final static int FORM_MATCH_POST_MATCH_STATS = 42;

private int currentForm = -1;

private League league = null;

// ----------------------------------------------------------------------------
// game tick - propagar los eventos de alto nivel a sus actions

public void gameTick() {
	int hiEvent = formTick();
	switch (currentForm) {
		case FORM_SPLASH:
			actionSplash(hiEvent);
		break;
		case FORM_START_MENU:
			actionStartMenu(hiEvent);
		break;
		case FORM_CHOOSE_LEAGUE:
			actionChooseLeague(hiEvent);
		break;
		case FORM_CHOOSE_TEAM:
			actionChooseTeam(hiEvent);
		break;
		case FORM_MAIN_MENU:
			actionMainMenu(hiEvent);
		break;
		case FORM_LEAGUE:
			actionLeague(hiEvent);
		break;
		case FORM_ABOUT:
			actionAbout(hiEvent);
		break;
		case FORM_CLASSIFICATION:
			actionClassification(hiEvent);
		break;
		case FORM_CLASSIFICATION_EXTRA:
			actionClassificationExtra(hiEvent);
		break;
		case FORM_MATCH:
			actionMatch(hiEvent);
		break;
		/*case FORM_CHOOSE_TEAM_STATS:
			actionChooseTeamStats(hiEvent);
		break;*/
		case FORM_TOP_PLAYERS:
			actionTopPlayers(hiEvent);
		break;
		case FORM_TRAINING_CHOOSE_TRAINING:
			actionTrainingChooseTraining(hiEvent);
		break;
		case FORM_TRAINING_CHOOSE_PLAYERS:
			actionTrainingChoosePlayers(hiEvent);
		break;
		case FORM_TRAINING_RESULT:
			actionTrainingResult(hiEvent);
		break;
		case FORM_TRANSFERS_MAIN:
			actionTransfersMain(hiEvent);
		break;
		/*case FORM_TRANSFERS_HIRE_SELECT:
			actionTransfersHireSelect(hiEvent);
		break;*/
		case FORM_TRANSFERS_HIRE_SEL_LEAGUE:
			actionTransfersHireSelLeague(hiEvent);
		break;
		case FORM_TRANSFERS_HIRE_SEL_TEAM:
			actionTransfersHireSelTeam(hiEvent);
		break;
		case FORM_TRANSFERS_HIRE_SEL_PLAYER:
			actionTransfersHireSelPlayer(hiEvent);
		break;
		case FORM_TRANSFERS_FIRE_SELECT:
			actionTransfersFireSelect(hiEvent);
		break;
		case FORM_TACTICS_MAIN:
			actionTacticsMain(hiEvent);
		break;
		case FORM_TACTICS_FORMATION:
			actionTacticsFormation(hiEvent);
		break;
		case FORM_TACTICS_POSITION:
			actionTacticsPosition(hiEvent);
		break;
		case FORM_SAVE:
			actionSave(hiEvent);
		break;
		case FORM_MATCH_PLAY:
			actionMatchPlay(hiEvent);
		break;
		case FORM_MATCH_POST:
			actionMatchPost(hiEvent);
		break;
		case FORM_HELP:
			actionHelp(hiEvent);
		break;
		case FORM_HELP_GENERIC:
			actionHelpGeneric(hiEvent);
		break;
		case FORM_END_LEAGUE:
			actionEndLeague(hiEvent);
		break;
		case FORM_OPTIONS:
			actionOptions(hiEvent);
		break;
		case FORM_OPTIONS_LANGUAGE:
			actionOptionsLanguage(hiEvent);
		break;
		case FORM_SPLASH2:
			actionSplash2(hiEvent);
		break;
		case FORM_SPLASH3:
			actionSplash3(hiEvent);
		break;
		case FORM_SPLASH_LEGAL:
			actionSplashLegal(hiEvent);
		break;
		case FORM_CREDITS:
			actionCredits(hiEvent);
		break;
		case FORM_CLASS_CHAMPIONS:
			actionClassChampions(hiEvent);
		break;
		case FORM_TRANSFERS_HIRE_CONFIRM:
			actionTransfersHireConfirm(hiEvent);
		break;
		case FORM_TACTICS_POSITION_CONFIRM:
			actionTacticsPositionConfirm(hiEvent);
		break;
		case FORM_MATCH_POST_ROUND_STATS:
			actionMatchPostRoundStats(hiEvent);
		break;
		case FORM_MATCH_POST_EUROPE_STATS:
			actionMatchPostEuropeStats(hiEvent);
		break;
		case FORM_MATCH_POST_MATCH_STATS:
			actionMatchPostMatchStats(hiEvent);
		break;
	}
}

// ----------------------------------------------------------------------------
// splash

private boolean repaintHack = true;

private long cont1 = -1;
private void actionSplash(int hiEvent) {
	if (cont1 == -1) {
		cont1 = System.currentTimeMillis();
	}
	screenLost = true;
	long d = System.currentTimeMillis() - cont1;
	if (hiEvent == HIEVENT_IDLE && d < 3000)
		return;
	iformBackground = null;
	System.gc();
	Image s = null;
//#ifdef J2ME
	s = loadImage("/codemasters.png");
//#else
//#endif
	formSplash(s);
	cont1 = -1;
	currentForm = FORM_SPLASH2;
}

private void actionSplash2(int hiEvent) {
	if (cont1 == -1) {
		cont1 = System.currentTimeMillis();
	}
	screenLost = true;
	long d = System.currentTimeMillis() - cont1;
	if (hiEvent == HIEVENT_IDLE && d < 3000)
		return;
	iformBackground = null;
	System.gc();
	Image s = null;
//#ifdef J2ME
	s = loadImage("/caratula.png");
//#else
//#endif
	formSplash(s);
	cont1 = -1;
	currentForm = FORM_SPLASH3;
}

private void actionSplash3(int hiEvent) {
	if (cont1 == -1) {
		cont1 = System.currentTimeMillis();
	}
	screenLost = true;
	long d = System.currentTimeMillis() - cont1;
	if (hiEvent == HIEVENT_IDLE && d < 3000)
		return;
	formWait(true);
	iformBackground = null;
	iformTopBackground = null;
	System.gc();
//#ifdef J2ME
	iformBackground = loadImage("/1.png");
	iformTopBackground = loadImage("/menu.png");
	fieldImage = loadImage("/field.png");
	redBullet = loadImage("/rojo.png");
	blackBullet = loadImage("/negro.png");
	blueBullet = loadImage("/azul.png");
	byte[] plyn = loadFile("/plyn");
	byte[] plys = loadFile("/plys");
	byte[] tms = loadFile("/tms");
//#else
//#endif
	league = new League();
	league.load(plyn, plys, tms);
	plyn = null;
	plys = null;
	tms = null;
	System.gc();
	formWait(false);
	//invokeStartMenu();
	invokeSplashLegal();
}

// ----------------------------------------------------------------------------
// splash legal

private void invokeSplashLegal() {
	fontSet(iformFontDefaultSmall);
	String[] s = printTextBreak("no legal text - no legal text - no legal text - no legal text - no legal text - no legal text", canvasWidth - 6);
	formPlainList(loc[203], null, s);
	currentForm = FORM_SPLASH_LEGAL;
	iformFont = iformFontDefaultSmall;
	iformDataSoftLeft = loc[92];
}

private void actionSplashLegal(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeStartMenu();
}

// ----------------------------------------------------------------------------
// start menu

private void invokeStartMenu() {
	String[] startMenuItems = { loc[8], loc[9], loc[10], loc[11], loc[12], loc[196], loc[13] };
	formPlainMenu(loc[14], null, startMenuItems);
	currentForm = FORM_START_MENU;
	// check save game size
	if (sizeAvailPersistent("savegame") < 4000) {
		formModalPopup(loc[15], COLOR_POPUP_BAD);
	}
	titleSongPlay(true);
}

private void actionStartMenu(int hiEvent) {
	byte[] data;
	int selectionTo = hiEventSelectedItem(hiEvent);
	switch (selectionTo) {
		case 0: {
			titleSongPlay(false);
			//#ifndef LOWMEM
			formWait(true);
			league = null;
			System.gc();
			//#ifdef J2ME
			byte[] plyn = loadFile("/plyn");
			byte[] plys = loadFile("/plys");
			byte[] tms = loadFile("/tms");
			//#else
			//#endif
			league = new League();
			league.load(plyn, plys, tms);
			plyn = null;
			plys = null;
			tms = null;
			System.gc();
			formWait(false);
			//#endif
			invokeChooseLeague();
		} break;
		case 1: {
			formWait(true);
			data = loadPersistent("savegame");
			formWait(false);
			if (data == null) {
				formModalPopup(loc[16], COLOR_POPUP_BAD);
				return;
			}
			// version check
			if (data[0] != 7) {
				formModalPopup(loc[17], COLOR_POPUP_BAD);
				return;
			}
			titleSongPlay(false);
			formWait(true);
			//#ifndef LOWMEM
			league = null;
			System.gc();
			//#ifdef J2ME
			byte[] plyn = loadFile("/plyn");
			byte[] plys = loadFile("/plys");
			byte[] tms = loadFile("/tms");
			//#else
			//#endif
			league = new League();
			league.load(plyn, plys, tms);
			plyn = null;
			plys = null;
			tms = null;
			//#endif
			System.gc();
			if (league.loadState(data)) {
				formWait(false);
				formModalPopup("Corrupt save file", COLOR_POPUP_BAD);
				return;
			}
			league.startJourney(false);
			System.gc();
			formWait(false);
			invokeMainMenu();
		} break;
		case 2:
			invokeOptions();
		break;
		case 3:
			invokeHelp(false);
			titleSongPlay(false);
		break;
		case 4:
			invokeAbout();
		break;
		case 5:
			invokeCredits();
		break;
		case 6:
			titleSongPlay(false);
			gameExit = true;
		break;
	}
}

// ----------------------------------------------------------------------------
// options

private void invokeOptions() {
	String[] optionsItems = null;
	if (languageItems.length <= 1) {
		optionsItems = new String[2];
		optionsItems[0] = null;
		optionsItems[1] = loc[1];
	} else {
		optionsItems = new String[3];
		optionsItems[0] = null;
		optionsItems[1] = loc[18];
		optionsItems[2] = loc[1];
	}
	if (gameSound) {
		optionsItems[0] = loc[19] + ": " + loc[20];
	} else {
		optionsItems[0] = loc[19] + ": " + loc[21];
	}
	formPlainMenu(loc[10], null, optionsItems);
	currentForm = FORM_OPTIONS;
}

private void actionOptions(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (languageItems.length <= 1) {
		switch (selectionTo) {
			case 0:
				gameSound = !gameSound;
				savePrefs();
				invokeOptions();
			break;
			case 1:
				invokeStartMenu();
			break;
		}
	} else {
		switch (selectionTo) {
			case 0:
				gameSound = !gameSound;
				savePrefs();
				invokeOptions();
			break;
			case 1:
				invokeOptionsLanguage();
			break;
			case 2:
				invokeStartMenu();
			break;
		}
	}
}

// ----------------------------------------------------------------------------
// options - language


private void invokeOptionsLanguage() {
	String[] l = new String[languageItems.length + 1];
	System.arraycopy(languageItems, 0, l, 0, languageItems.length);
	l[languageItems.length] = loc[1];
	formPlainMenu(loc[18], null, l);
	currentForm = FORM_OPTIONS_LANGUAGE;
}

private void actionOptionsLanguage(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo >= 0 && selectionTo < languageItems.length) {
		selectedLanguage = selectionTo;
		formWait(true);
		savePrefs();
		loadLocale(selectionTo);
		formWait(false);
		invokeOptions();
	} else if (selectionTo  == languageItems.length) {
		invokeOptions();
	}
}

// ----------------------------------------------------------------------------
// help

private boolean inGameHelp;
private void invokeHelp(boolean inGame) {
	inGameHelp = inGame;
	String[] helpMenuItems = { loc[22], loc[134], loc[141], loc[152], loc[154], loc[156], loc[158], loc[1] };
	formPlainMenu(loc[11], null, helpMenuItems);
	currentForm = FORM_HELP;
}

private void actionHelp(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	switch (selectionTo) {
		case 0:
			invokeHelpGeneric(22, 23, 25);
		break;
		case 1:
			invokeHelpGeneric(134, 135, 140);
		break;
		case 2:
			invokeHelpGeneric(141, 142, 151);
		break;
		case 3:
			invokeHelpGeneric(152, 153, 153);
		break;
		case 4:
			invokeHelpGeneric(154, 155, 155);
		break;
		case 5:
			invokeHelpGeneric(156, 157, 157);
		break;
		case 6:
			invokeHelpGeneric(158, 159, 159);
		break;
		case 7:
			if (inGameHelp) {
				invokeMainMenu();
			} else {
				invokeStartMenu();
			}
		break;
	}
}

// ----------------------------------------------------------------------------
// help - generic

private void invokeHelpGeneric(int tn, int from, int to) {
	String[] text = new String[to - from + 1];
	for (int i = 0; i < (to - from + 1); i++) {
		text[i] = loc[from + i];
	}
	fontSet(iformFontDefaultSmall);
	String[] s = printTextBreak(text, canvasWidth - 6);
	formPlainList(loc[tn], null, s);
	currentForm = FORM_HELP_GENERIC;
	iformFont = iformFontDefaultSmall;
}

private void actionHelpGeneric(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeHelp(inGameHelp);
}

// ----------------------------------------------------------------------------
// about

private void invokeAbout() {
	//String[] aboutItems = { loc[26], loc[27], loc[28], loc[29] };
	//formPlainList(loc[14], loc[30], aboutItems);
	String[] aboutItems = printTextBreak(loc[26], canvasWidth - 6);
	formPlainList(loc[14], loc[12], aboutItems);
	currentForm = FORM_ABOUT;
}

private void actionAbout(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeStartMenu();
}

// ----------------------------------------------------------------------------
// credits

private void invokeCredits() {
	String[] gfx = { "Raul Duran" };
	String[] prog = { "Carlos Carrasco" };
	String[] testing = { "Alfred Ferrer", "Abel Andreu", "Toni Gonzalez", "Eduardo Berti",
		"Josep Ruiz", "Xavi Espejo" };
	String[] ack = { "Juan A. Gomez", "Elias Lozano" };

	String[] aboutItems = new String[5 + gfx.length + prog.length + testing.length + ack.length];
	aboutItems[0] = loc[197];
	// gfx
	aboutItems[1] = loc[198];
	System.arraycopy(gfx, 0, aboutItems, 2, gfx.length);
	int i = gfx.length+2;
	// prog
	aboutItems[i] = loc[199];
	System.arraycopy(prog, 0, aboutItems, i+1, prog.length);
	i += prog.length+1;
	// testing
	aboutItems[i] = loc[200];
	System.arraycopy(testing, 0, aboutItems, i+1, testing.length);
	i += testing.length+1;
	// ack
	aboutItems[i] = loc[201];
	System.arraycopy(ack, 0, aboutItems, i+1, ack.length);
	i += ack.length+1;
	fontSet(iformFontDefault);
	String[] s = printTextBreak(aboutItems, canvasWidth - 6);
	formPlainList(loc[14], loc[196], s);
	currentForm = FORM_CREDITS;
}

private void actionCredits(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeStartMenu();
}

// ----------------------------------------------------------------------------
// choose league

private String[] chooseLeagueItems;
private void invokeChooseLeague() {
	chooseLeagueItems = new String[7];
	for (int i = 0; i < 6; i++) {
		chooseLeagueItems[i] = loc[95+i];
	}
	chooseLeagueItems[6] = loc[5];
	formPlainMenu(loc[31], null, chooseLeagueItems);
	currentForm = FORM_CHOOSE_LEAGUE;
}

private void actionChooseLeague(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == chooseLeagueItems.length - 1) {
		invokeStartMenu();
	} else if (selectionTo >= 0) {
		league.playingLeague = selectionTo;
		invokeChooseTeam(selectionTo);
	}
}

// ----------------------------------------------------------------------------
// choose team

private String[] chooseTeamItems;
private void invokeChooseTeam(int selectionTo) {
	String[] chooseTeamLabels = { loc[32], loc[190] };
	int n = League.leagueTeams[selectionTo];
	chooseTeamItems = new String[n + 1];
	for (int i = 0; i < n; i++) {
		chooseTeamItems[i] = league.teams[selectionTo][i].name;
	}
	chooseTeamItems[n] = loc[5];
	String[] chooseTeamCash = new String[n + 1];
	for (int i = 0; i < League.leagueTeams[selectionTo]; i++) {
		chooseTeamCash[i] = league.cashStr(league.teams[selectionTo][i].cash);
	}
	chooseTeamCash[n] = "";
	String[][] its = new String[2][];
	its[0] = chooseTeamItems;
	its[1] = chooseTeamCash;
	formPlainMenu(loc[34], loc[95+selectionTo], its, chooseTeamLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CHOOSE_TEAM;
}

private void actionChooseTeam(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == chooseTeamItems.length - 1) {
		invokeChooseLeague();
	} else if (selectionTo >= 0) {
		league.userTeam = league.teams[league.playingLeague][selectionTo];
		formWait(true);
		league.coachCash = league.userTeam.cash;
		league.startSeason();
		System.gc();
		league.startJourney();
		System.gc();
		formWait(false);
		invokeMainMenu();
	}
}


// ----------------------------------------------------------------------------
// choose team stats
/*
private void invokeChooseTeamStats() {
	String[] statsItems = { league.userTeam.name, "Development DB", "No statistics", "available" };
	formYesNoList(loc[35], loc[36], loc[3], loc[2], statsItems);
	currentForm = FORM_CHOOSE_TEAM_STATS;
}

private void actionChooseTeamStats(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {
		formWait(true);
		league.coachCash = league.userTeam.cash;
		league.startSeason();
		System.gc();
		league.startJourney();
		System.gc();
		formWait(false);
		invokeMainMenu();
	} else if (hiEvent == HIEVENT_NO) {
		invokeChooseTeam(league.playingLeague);
	}
}
*/
// ----------------------------------------------------------------------------
// main menu

private void invokeMainMenu() {
	String[] mainMenuItems = { loc[37], loc[38], loc[39], loc[40], loc[41], loc[11], loc[42] }; 
	formPlainMenu(loc[43], league.getRoundTitle(), mainMenuItems);
	currentForm = FORM_MAIN_MENU;
}

private void actionMainMenu(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	switch (selectionTo) {
		case 0:
			invokeMatch();
		break;
		case 1:
			invokeTacticsMain();
		break;
		case 2:
			invokeLeague();
		break;
		case 3:
			invokeTrainingChooseTraining();
		break;
		case 4:
			if (!hiringCalendarOK()) {
					String sb = loc[132]+  " " + (league.transferRange(0)+1) + "-" + (league.transferRange(0)+5) + ", " +
						loc[132]+  " " + (league.transferRange(1)+1) + "-" + (league.transferRange(1)+5) + ", " +
						loc[132]+  " " + (league.transferRange(2)+1) + "-" + (league.transferRange(2)+5);
					formModalPopup(loc[168] + " " + sb, COLOR_POPUP_GOOD);
			} else {
				invokeTransfersMain();
			}
		break;
		case 5:
			invokeHelp(true);
		break;
		case 6:
			invokeSave();
		break;
	}
}

// ----------------------------------------------------------------------------
// league

private void invokeLeague() {
	String[] leagueItems = { loc[44] + " 1", loc[44] + " 2", loc[45], loc[122], loc[1] };
	formPlainMenu(loc[39], league.getRoundTitle(), leagueItems);
	currentForm = FORM_LEAGUE;
}

private void actionLeague(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == 0) {
		invokeClassification();
	} else if (selectionTo == 1) {
		invokeClassificationExtra();
	} else if (selectionTo == 2) {
		invokeTopPlayers();
	} else if (selectionTo == 3) {
		invokeClassChampions();
	} else if (selectionTo == 4) {
		invokeMainMenu();
	}
}

// ----------------------------------------------------------------------------
// classification

private void invokeClassification() {
	String[] classificationLabels = { loc[46], loc[47], loc[182], loc[183], loc[184] };
	// calcular items
	Team[] teams = league.classificationInfo();
	String[] st = new String[teams.length + 1];
	String[] pt = new String[teams.length + 1];
	String[] ppt = new String[teams.length + 1];
	String[] wt = new String[teams.length + 1];
	String[] dt = new String[teams.length + 1];
	for (int i = 0; i < st.length-1; i++) {
		st[i] = "" + (i+1) + ". " + teams[i].name;
		pt[i] = "" + teams[i].points;
		ppt[i] = "" + teams[i].PJ;
		wt[i] = "" + teams[i].PG;
		dt[i] = "" + teams[i].PE;
		//gp[i] = "" + teams[i].realQuality();
	}
	st[teams.length] = loc[1];
	pt[teams.length] = "";
	ppt[teams.length] = "";
	wt[teams.length] = "";
	dt[teams.length] = "";
	String[][] its = new String[5][];
	its[0] = st;
	its[1] = pt;
	its[2] = ppt;
	its[3] = wt;
	its[4] = dt;
	formPlainMenu(loc[44], league.getRoundTitle(), its, classificationLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CLASSIFICATION;
}

private Team focusedTeam = null;
private void actionClassification(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == league.teams[league.playingLeague].length) {
		invokeLeague();
		return;
	}
	/*int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < league.teams[league.playingLeague].length) {
		focusedTeam = league.teams[league.playingLeague][focusTo];
		screenLost = true;
	} else {
		focusedTeam = null;
		screenLost = true;
	}*/
}

// ----------------------------------------------------------------------------
// classification extra

private void invokeClassificationExtra() {
	String[] classificationLabels = { loc[46], loc[47], loc[185], loc[186], loc[187] };
	// calcular items
	Team[] teams = league.classificationInfo();
	String[] st = new String[teams.length + 1];
	String[] pt = new String[teams.length + 1];
	String[] lt = new String[teams.length + 1];
	String[] gpt = new String[teams.length + 1];
	String[] glt = new String[teams.length + 1];
	for (int i = 0; i < st.length-1; i++) {
		st[i] = "" + (i+1) + ". " + teams[i].name;
		pt[i] = "" + teams[i].points;
		lt[i] = "" + teams[i].PP;
		gpt[i] = "" + teams[i].posGoals;
		glt[i] = "" + teams[i].negGoals;
		//gp[i] = "" + teams[i].realQuality();
	}
	st[teams.length] = loc[1];
	pt[teams.length] = "";
	lt[teams.length] = "";
	gpt[teams.length] = "";
	glt[teams.length] = "";
	String[][] its = new String[5][];
	its[0] = st;
	its[1] = pt;
	its[2] = lt;
	its[3] = gpt;
	its[4] = glt;
	formPlainMenu(loc[44], league.getRoundTitle(), its, classificationLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CLASSIFICATION_EXTRA;
}

private void actionClassificationExtra(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == league.teams[league.playingLeague].length) {
		invokeLeague();
		return;
	}
	/*int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < league.teams[league.playingLeague].length) {
		focusedTeam = league.teams[league.playingLeague][focusTo];
		screenLost = true;
	} else {
		focusedTeam = null;
		screenLost = true;
	}*/
}

// ----------------------------------------------------------------------------
// top players

private int tpbv = 0;
private void invokeTopPlayers() {
	String[] topPlayersLabels = { loc[4], loc[49] };
	// calcular items
	int n = -1;
	int i;
	for (i = 0; i < 20; i++) {
		if (league.top40Players[i] == -1) {
			i++;
			break;
		}
	}
	n = i;
	String[] st = new String[n];
	String[] pt = new String[n];
	for (i = 0; i < st.length-1; i++) {
		if (league.top40Players[i] == -1) {
			i++;
			break;
		}
		st[i] = "" + (i+1) + ". " + league.playerGetName(league.top40Players[i]) + " (" + league.teamForPlayer(league.top40Players[i]).name + ")";
		pt[i] = "" + league.playerGetGoals(league.top40Players[i]);
	}
	st[i] = loc[1];
	tpbv = i;
	pt[i] = "";
	String[][] its = new String[2][];
	its[0] = st;
	its[1] = pt;
	formPlainMenu(loc[45], league.getRoundTitle(), its, topPlayersLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TOP_PLAYERS;
}

private void actionTopPlayers(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tpbv) {
		invokeLeague();
		return;
	}
}

// ----------------------------------------------------------------------------
// europe champions classification

private void invokeClassChampions() {
	fontSet(iformFontDefaultSmall);
	String l = null;
	if (league.champTeams.length >= 2) {
		l = loc[204] + " " + league.getChampJourneyName();
	} else {
		l = "";
	}
	formPlainList(loc[122], l, printTextBreak(league.getChampMatches(), canvasWidth - 6));
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CLASS_CHAMPIONS;
}

private void actionClassChampions(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeLeague();
}

/*
private void invokeClassChampions() {
	String[] classLabels = { "Match", "", "" };
	String[] r = league.getChampMatches();
	int n = r.length + 1;
	String[] st = new String[n];
	int i = 0;
	for (i = 0; i < st.length-1; i++) {
		st[i] = "|" + r[i] + "|?" + "|?|"; 
	}
	st[i] = "|" + loc[1] + "|||";
	tpbv = i;
	String[][] its = new String[3][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	formPlainMenu("Europe Champions", "TO DO", its, classLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CLASS_CHAMPIONS;
}

private void actionClassChampions(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tpbv) {
		invokeLeague();
		return;
	}
}
*/

// ----------------------------------------------------------------------------
// training - choose training

private int tctb = -1;
private void invokeTrainingChooseTraining() {
	String[] st = new String[league.allTrainings.length+1];
	String[] str = new String[league.allTrainings.length+2];
	String[] end = new String[league.allTrainings.length+1];
	String[] spd = new String[league.allTrainings.length+1];
	String[] kik = new String[league.allTrainings.length+1];
	int i, j;
	for (i = 0; i < league.allTrainings.length; i++) {
		st[i] = league.trainingsNames[i];
		j = league.allTrainings[i][1];
		str[i] = j > 0 ? "+" + j : "" + j; 
		j = league.allTrainings[i][2];
		end[i] = j > 0 ? "+" + j : "" + j; 
		j = league.allTrainings[i][3];
		spd[i] = j > 0 ? "+" + j : "" + j; 
		j = league.allTrainings[i][4];
		kik[i] = j > 0 ? "+" + j : "" + j; 
	}
	i = st.length-1;
	st[i] = loc[1];
	str[i] = "";
	end[i] = "";
	spd[i] = "";
	kik[i] = "";
	tctb = i;
	String[][] its = new String[5][];
	its[0] = st;
	its[1] = str;
	its[2] = end;
	its[3] = spd;
	its[4] = kik;
	String[] trainingLabels = { loc[4], league.statsAbbr[0], 
		league.statsAbbr[1], league.statsAbbr[2], league.statsAbbr[3] };
	formPlainMenu(loc[50], league.coachRemainingTrainings + " " + loc[51], its, trainingLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRAINING_CHOOSE_TRAINING;
}

private int selectedTraining = -1;
private void actionTrainingChooseTraining(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tctb) {
		invokeMainMenu();
		return;
	}
	if (selectionTo >= 0) {
		if (league.coachRemainingTrainings <= 0) {
			formModalPopup(loc[52], COLOR_POPUP_BAD);
			return;
		}
		selectedTraining = selectionTo;
		invokeTrainingChoosePlayers();
	}
}

// ----------------------------------------------------------------------------
// training - choose players

private int tcpb = -1;
private int remainingPlayers;
private void invokeTrainingChoosePlayers() {
	// calcular items
	int m = league.userTeam.countPlayers();
	int n = m + 1;
	String[] st = new String[n];
	String[] pos = null;
	String[] str = null;
	String[] end = null;
	String[] spd = null;
	String[] kik = null;
	int i;
	for (i = 0; i < st.length-1; i++) {
		String d = "";
		if (i >= 11 && i <= 15) {
			d = "(S) ";
		} else if (i >= 16) {
			d = "(R) ";
		}
		st[i] = "|" + d + league.playerGetName(league.userTeam.players[i]) + 
			"|" + league.playerGetSpecAbbr(league.userTeam.players[i]) +
			"|" + league.playerGetStat(league.userTeam.players[i], 0) +
			"|" + league.playerGetStat(league.userTeam.players[i], 1) +
			"|" + league.playerGetStat(league.userTeam.players[i], 2) +
			"|" + league.playerGetStat(league.userTeam.players[i], 3) + "|";
	}
	st[i] = "|" + loc[1] + "||||||";
	tcpb = i;
	String[][] its = new String[6][];
	its[0] = st;
	its[1] = pos;
	its[2] = str;
	its[3] = end;
	its[4] = spd;
	its[5] = kik;
	String[] playerListingLabels = { loc[4], loc[33], league.statsAbbr[0], 
		league.statsAbbr[1], league.statsAbbr[2], league.statsAbbr[3] };
	formPlainMenu(loc[53], league.trainingsNames[selectedTraining] + " - " + league.allTrainings[selectedTraining][0] + " " + loc[51],
		its, playerListingLabels);
	setAttToUserTeamPlayerListing();
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRAINING_CHOOSE_PLAYERS;
}

private String[] resultTxt = null;
private void actionTrainingChoosePlayers(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tcpb) {
		invokeTrainingChooseTraining();
		return;
	}
	if (selectionTo >= 0) {
		if (league.playerIsInjured(league.userTeam.players[selectionTo])) {
			formModalPopup(league.playerGetName(league.userTeam.players[selectionTo]) + " " + loc[54], COLOR_POPUP_BAD);
			return;
		}

		// uberhack, este si que mola: cambiar on the fly el array de atributos
		iformDataListItemsAttr[selectionTo] ^= MASK_PICKED;

		int nPicked = 0;
		for (int i = 0; i < iformDataListItemsAttr.length; i++) {
			if ((iformDataListItemsAttr[i] & MASK_PICKED) != 0)
				nPicked++;
		}

		int remaining = league.allTrainings[selectedTraining][0] - nPicked;
		if (remaining < 0) {
			remaining = 0;
		}
		// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
		iformDataHeaderSmall = league.trainingsNames[selectedTraining] + " - " + remaining + " " + loc[51];
		screenLost = true;
		selectingPosition = true;

		if (remaining > 0)
			return;

		String[] t = new String[nPicked];
		for (int i = 0, j = 0; i < iformDataListItemsAttr.length; i++) {
			if ((iformDataListItemsAttr[i] & MASK_PICKED) == 0)
				continue;
			String s = "[" + league.playerGetName(league.userTeam.players[i]) + "] ";

			for (int h = 0; h < league.allTrainings[selectedTraining].length - 1; h++) {
				if (league.allTrainings[selectedTraining][h+1] == 0)
					continue;
				s += league.statsAbbr[h] + " " + league.playerGetStat(league.userTeam.players[i], h) + " -> ";
				league.playerIncStat(league.userTeam.players[i], h,
					league.allTrainings[selectedTraining][h+1]);
				s += league.playerGetStat(league.userTeam.players[i], h) + "   ";
			}
			t[j] = s;
			j++;
		}
		resultTxt = printTextBreak(t, canvasWidth - 6);
		invokeTrainingResult();
		league.coachRemainingTrainings--;
		league.userTeam.markDirtyQuality();
	}
}

// ----------------------------------------------------------------------------
// training - result

private void invokeTrainingResult() {
	formPlainList(loc[55], league.trainingsNames[selectedTraining], resultTxt);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRAINING_RESULT;
}

private void actionTrainingResult(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeMainMenu();
}

// ----------------------------------------------------------------------------
// transfers - main

private void invokeTransfersMain() {
	String[] transfersMainItems = { loc[56], loc[57], loc[1] };
	formPlainMenu(loc[41], loc[205] + ": " + League.cashStr(league.coachCash) + " " + loc[188], transfersMainItems);
	currentForm = FORM_TRANSFERS_MAIN;
}

private boolean hiringCalendarOK() {
	return (league.journeyNumber >= league.transferRange(0) &&
	league.journeyNumber < (league.transferRange(0) + 5)) ||
	// temporada 2
	(league.journeyNumber >= league.transferRange(1) &&
	league.journeyNumber < (league.transferRange(1) + 5)) ||
	// temporada 3
	(league.journeyNumber >= league.transferRange(2) &&
	league.journeyNumber < (league.transferRange(2) + 5));
}

private void actionTransfersMain(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	switch (selectionTo) {
		case 0:
			// condiciones que permiten hire/fire
			 if (league.userTeam.countPlayers() < 30) {
				if (league.availHirePlayersTeams != null) {
					if (league.availHirePlayersTeams.length == 0) {
						formModalPopup(loc[58], COLOR_POPUP_GOOD);
					} else {
						invokeTransfersHireSelLeague();
					}
				} else {
					formModalPopup(loc[59], COLOR_POPUP_BAD);
				}
			} else {
				formModalPopup(loc[60], COLOR_POPUP_BAD);
			}
		break;
		case 1:
			// condiciones que permiten hire/fire
			if (league.coachRemainingFires > 0) {
				if (league.userTeam.countPlayers() > 16) {
					invokeTransfersFireSelect();
				} else {
					formModalPopup(loc[195], COLOR_POPUP_BAD);
				}
			} else {					
				formModalPopup(loc[61], COLOR_POPUP_BAD);
			}
		break;
		case 2:
			invokeMainMenu();
		break;
	}
}

// ----------------------------------------------------------------------------
// transfers - hire - choose league

private void invokeTransfersHireSelLeague() {
	chooseLeagueItems = new String[7];
	for (int i = 0; i < 6; i++) {
		chooseLeagueItems[i] = loc[95+i];
	}
	chooseLeagueItems[6] = loc[5];
	formPlainMenu(loc[31], loc[205] + ": " + League.cashStr(league.coachCash) + " " + loc[188], chooseLeagueItems);
	currentForm = FORM_TRANSFERS_HIRE_SEL_LEAGUE;
}

private int hiringLeague;
private void actionTransfersHireSelLeague(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == 6) {
		invokeTransfersMain();
	} else if (selectionTo >= 0 && selectionTo <= 5) {
		hiringLeague = selectionTo;
		invokeTransfersHireSelTeam(selectionTo);
	}/* else if (selectionTo == 5) {
		hiringLeague = 5;
		hiringTeam = league.extraTeam;
		invokeTransfersHireSelPlayer(hiringTeam);
	}*/
}

// ----------------------------------------------------------------------------
// transfers - hire - choose team

private Team[] teamListing;
private void invokeTransfersHireSelTeam(int selectionTo) {
	String[] chooseTeamLabels = { loc[32], loc[33] };
	int n = League.leagueTeams[selectionTo];
	if (selectionTo == league.playingLeague) {
		n--;
	}
	chooseTeamItems = new String[n + 1];
	teamListing = new Team[n];
	for (int i = 0, j = 0; i < League.leagueTeams[selectionTo]; i++) {
		if (league.teams[selectionTo][i] != league.userTeam) {
			chooseTeamItems[j] = league.teams[selectionTo][i].name;
			teamListing[j] = league.teams[selectionTo][i];
			j++;
		}
	}
	chooseTeamItems[n] = loc[5];
	//formPlainMenu(loc[34], loc[95+selectionTo], its, chooseTeamLabels);
	formPlainMenu(loc[34], loc[205] + ": " + League.cashStr(league.coachCash) + " " + loc[188], chooseTeamItems);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRANSFERS_HIRE_SEL_TEAM;
}

private void actionTransfersHireSelTeam(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == chooseTeamItems.length - 1) {
		invokeTransfersHireSelLeague();
	} else if (selectionTo >= 0) {
		invokeTransfersHireSelPlayer(teamListing[selectionTo]);
		teamListing = null;
	}
}

// ----------------------------------------------------------------------------
// transfers - hire - choose player

private Team hiringTeam;
private void invokeTransfersHireSelPlayer(Team team) {
	hiringTeam = team;
	// calcular items
	int m = hiringTeam.countPlayers();
	int n = m + 1;
	String[] st = new String[n];
	String[] pos = null;//new String[n];
	String[] qa = null;//new String[n];
	String[] val = null;//new String[n];
	int i;
	for (i = 0; i < st.length-1; i++) {
		st[i] = "|" + league.playerGetName(hiringTeam.players[i]) +
		"|" + league.playerGetSpecAbbr(hiringTeam.players[i]) +
		"|" + league.playerBalance(hiringTeam.players[i], -1) +
		"|" + League.cashStr(league.playerValue(hiringTeam.players[i])) + "|";
	}
	st[i] = "|" + loc[1] + "||||";
	//pos[i] = "";
	//qa[i] = "";
	//val[i] = "";
	tcpb = i;
	String[][] its = new String[4][];
	its[0] = st;
	its[1] = pos;
	its[2] = qa;
	its[3] = val;
	String[] playerListingLabels = { loc[4], loc[33], loc[194], loc[190] };
	//formPlainMenu(loc[56], loc[62], its, playerListingLabels);
	formPlainMenu(loc[56], loc[205] + ": " + League.cashStr(league.coachCash) + loc[188], its, playerListingLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRANSFERS_HIRE_SEL_PLAYER;
}

private void actionTransfersHireSelPlayer(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tcpb) {
		//if (hiringLeague == 5) {
		//	invokeTransfersHireSelLeague();
		//} else {
			invokeTransfersHireSelTeam(hiringLeague);
		//}
		return;
	}
	if (selectionTo >= 0) {
		short p = hiringTeam.players[selectionTo];
		Team t = hiringTeam;
		int v = league.playerValue(p);
		if (league.coachCash < v) {
			formModalPopup(loc[191], COLOR_POPUP_BAD);
			return;
		}
		int userQuality = league.userTeam.realQuality();
		int otherQuality = league.playerBalance(p, -1);
		int diff = otherQuality - userQuality;
		if (diff > 15) {
			formModalPopup(loc[192], COLOR_POPUP_BAD);
			return;
		}
		invokeTransfersHireConfirm(p);
		/*t.removePlayer(p);
		league.userTeam.addPlayer(p);
		league.clearHires();
		league.coachCash -= v;
		league.userTeam.markDirtyQuality();
		invokeTransfersMain();
		//if (hiringLeague != 5) {
			formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + t.name, COLOR_POPUP_GOOD);
		//} else {
		//	formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + loc[189], COLOR_POPUP_GOOD);
		//}
		*/
	}
}

// ----------------------------------------------------------------------------
// transfers - confirm hire

private short hiringPlayer;
private void invokeTransfersHireConfirm(short player) {
	hiringPlayer = player;
	fontSet(iformFontDB);
	formYesNoList(loc[56], loc[205] + ": " + League.cashStr(league.coachCash) + loc[188], loc[3], loc[2],
		printTextBreak(loc[206] + " " + league.playerGetName(player) +
			" (" + hiringTeam.name + ") " + " " + loc[207] + " " + 
			League.cashStr(league.playerValue(player)) +
			" " + loc[188], canvasWidth - 6));
	iformFont = iformFontDB;
	currentForm = FORM_TRANSFERS_HIRE_CONFIRM;
}

private void actionTransfersHireConfirm(int hiEvent) {
	if (hiEvent == HIEVENT_NO) {
		invokeTransfersHireSelPlayer(hiringTeam);
		return;
	}
	if (hiEvent == HIEVENT_YES) {
		short p = hiringPlayer;
		Team t = hiringTeam;
		int v = league.playerValue(p);
		t.removePlayer(p);
		league.userTeam.addPlayer(p);
		league.clearHires();
		league.coachCash -= v;
		league.userTeam.markDirtyQuality();
		invokeTransfersMain();
		formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + t.name, COLOR_POPUP_GOOD);
	}
}

// ----------------------------------------------------------------------------
// transfers - fire select

private void invokeTransfersFireSelect() {
	// calcular items
	int m = league.userTeam.countPlayers();
	int n = m + 1;
	String[] st = new String[n];
	String[] pos = null;//new String[n];
	String[] qa = null;//new String[n];
	String[] val = null;//new String[n];
	int i;
	for (i = 0; i < st.length-1; i++) {
		st[i] = "|" + league.playerGetName(league.userTeam.players[i]) +
		"|" + league.playerGetSpecAbbr(league.userTeam.players[i]) +
		"|" + league.playerBalance(league.userTeam.players[i], -1) +
		"|" + League.cashStr(league.playerValue(league.userTeam.players[i])) + "|";
	}
	st[i] = "|" + loc[1] + "||||";
	//pos[i] = "";
	//qa[i] = "";
	//val[i] = "";
	tcpb = i;
	String[][] its = new String[4][];
	its[0] = st;
	its[1] = pos;
	its[2] = qa;
	its[3] = val;
	String[] playerListingLabels = { loc[4], loc[33], loc[194], loc[190] };
	formPlainMenu(loc[57], loc[205] + ": " + League.cashStr(league.coachCash) + " " + loc[188], its, playerListingLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRANSFERS_FIRE_SELECT;
}

private void actionTransfersFireSelect(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tcpb) {
		invokeTransfersMain();
		return;
	}
	if (selectionTo >= 0) {
		short p = league.userTeam.players[selectionTo];
		int otherQuality = league.playerBalance(p, -1);
		Team t = league.userTeam;
		while (t == league.userTeam)
			t = league.vacantTeamQA(otherQuality - 30);
		league.coachCash += league.playerValue(p);
		league.userTeam.removePlayer(p);
		t.addPlayer(p);
		league.userTeam.markDirtyQuality();
		invokeTransfersMain();
		formModalPopup(loc[66] + " " + league.playerGetName(p) +
			". " + loc[67] + " " + t.name , COLOR_POPUP_GOOD);
		league.coachRemainingFires--;
	}
}

// ----------------------------------------------------------------------------
// tactics - main

private void invokeTacticsMain() {
	String[] tacticsMainItems = { null, loc[69], null, loc[1] };
	tacticsMainItems[0] = loc[68] + ": " + league.formationsNames[league.coachFormation];
	String s = loc[162];
	if (league.coachGeneralTactic == 1) {
		s = loc[163];
	}
	if (league.coachGeneralTactic == 2) {
		s = loc[164];
	}
	tacticsMainItems[2] = loc[161] + ": " + s;
	formPlainMenu(loc[38], league.getRoundTitle(), tacticsMainItems);
	currentForm = FORM_TACTICS_MAIN;
}

private void actionTacticsMain(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	switch (selectionTo) {
		case 0:
			invokeTacticsFormation();
		break;
		case 1:
			invokeTacticsPosition(false);
		break;
		case 2:
			league.coachGeneralTactic++;
			if (league.coachGeneralTactic >= 3) {
				league.coachGeneralTactic = 0;
			}
			invokeTacticsMain();
			iformSelectedItem = 2;
		break;
		case 3:
			invokeMainMenu();
		break;
	}
}

// ----------------------------------------------------------------------------
// tactics - formation

private void invokeTacticsFormation() {
	String[] formationItems = new String[league.formationsNames.length + 1];
	System.arraycopy(league.formationsNames, 0, formationItems, 0, league.formationsNames.length);
	formationItems[league.formationsNames.length] = loc[1];
	tcpb = league.formationsNames.length;
	formPlainMenu(loc[68], league.getRoundTitle(), formationItems);
	iformFont = iformFontDefaultSmall;
	iformListStartY = iformTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	focusedFormation = 0;
	focusedPlayer = -1;
	currentForm = FORM_TACTICS_FORMATION;
}

private int focusedFormation;
private int focusedPlayer;
private void actionTacticsFormation(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tcpb) {
		invokeTacticsMain();
		return;
	}
	if (selectionTo >= 0) {
		league.coachFormation = selectionTo;
		invokeTacticsMain();
		return;
	}
	int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < league.formationsNames.length) {
		focusedFormation = focusTo;
		screenLost = true;
	} else if (focusTo == league.formationsNames.length) {
		focusedFormation = league.coachFormation;
		screenLost = true;
	}
}

// ----------------------------------------------------------------------------
// tactics - position

private void invokeTacticsPosition(boolean inGame) {
	iformDataHeaderTop = null;
	iformDataHeaderSmall = null;
	iformDataListItems = null;
	iformDataListColumnNames = null;
	iformDataListItemsAttr = null;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	pickInMatch = inGame;
	System.gc();
	int m = league.userTeam.countPlayers();
	if (pickInMatch) {
		m = m < 16 ? m : 16;
	}
	int n = m + 1;
	String[] st = new String[n];
	String[] pos = null;
	String[] str = null;
	String[] end = null;
	String[] spd = null;
	String[] kik = null;
	int i;
	for (i = 0; i < st.length-1; i++) {
		String d = "";
		if (i >= 11 && i <= 15) {
			d = "(S) ";
		} else if (i >= 16) {
			d = "(R) ";
		}
		st[i] = "|" + d + league.playerGetName(league.userTeam.players[i]) + 
			"|" + league.playerGetSpecAbbr(league.userTeam.players[i]) +
			"|" + league.playerGetStat(league.userTeam.players[i], 0) +
			"|" + league.playerGetStat(league.userTeam.players[i], 1) +
			"|" + league.playerGetStat(league.userTeam.players[i], 2) +
			"|" + league.playerGetStat(league.userTeam.players[i], 3) + "|";
	}
	st[i] = "|" + loc[1] + "||||||";
	tcpb = i;
	String[][] its = new String[6][];
	its[0] = st;
	its[1] = pos;
	its[2] = str;
	its[3] = end;
	its[4] = spd;
	its[5] = kik;
	String[] playerListingLabels = { loc[4], loc[33], league.statsAbbr[0], 
		league.statsAbbr[1], league.statsAbbr[2], league.statsAbbr[3] };

	if (pickInMatch) {
		formPlainMenu(loc[165], remainingMatchChanges + " " + loc[166], its, playerListingLabels);
	} else {
		formPlainMenu(loc[69], loc[70], its, playerListingLabels);
	}
	setAttToUserTeamPlayerListing();
	iformFont = iformFontDefaultSmall;
	if (altTopBackground != null && pickInMatch) {
		iformListStartY = altTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	} else {
		iformListStartY = iformTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	}
	focusedFormation = league.coachFormation;
	focusedPlayer = 0;
	currentForm = FORM_TACTICS_POSITION;
	selectingPosition = false;
}

private void setAttToUserTeamPlayerListing() {
	iformDataListItemsAttr = null;
	byte[] att = new byte[iformDataListItems[0].length];
	for (int i = 0; i < att.length; i++) {
		if (i >= 30)
			break;
		att[i] = league.playerGetTextAtt(league.userTeam.players[i]);
	}
	iformDataListItemsAttr = att;
}

private boolean selectingPosition;
private boolean pickInMatch;
private int pickedPlayer;
private int remainingMatchChanges;
private void actionTacticsPosition(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tcpb) {
		if (pickInMatch) {
			resumeMatch();
			pickInMatch = false;
		} else {
			invokeTacticsMain();
		}
		return;
	}
	int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < tcpb) {
		focusedPlayer = focusTo;
		screenLost = true;
	} else if (focusTo == tcpb) {
		focusedPlayer = -1;
		screenLost = true;
	}
	if (selectionTo >= 0) {
		if (!selectingPosition) {
			// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
			iformDataHeaderSmall = loc[71] + " " + league.playerGetName(league.userTeam.players[selectionTo]);
			// uberhack, este si que mola: cambiar on the fly el array de atributos
			setAttToUserTeamPlayerListing();
			iformDataListItemsAttr[selectionTo] |= MASK_PICKED;
			screenLost = true;
			selectingPosition = true;
			pickedPlayer = selectionTo;
		} else {
			if (pickInMatch) {
				setAttToUserTeamPlayerListing();
				// si la pos destion esta entre los 11 primeros, y la origen no, entonces se descuenta un
				// un swap
				boolean overflow = false;
				boolean benchToPlay = false;
				if ((selectionTo < 11 && pickedPlayer >= 11) || (pickedPlayer < 11 && selectionTo >= 11)) {
					if (remainingMatchChanges > 0) {
						//remainingMatchChanges--;
						benchToPlay = true;
					} else {
						overflow = true;
					}
				}
				int spPlayer = league.playerGetSpec(league.userTeam.players[pickedPlayer]);
				int sp = league.formationsSpec[league.coachFormation][selectionTo];
				int ref = 100;
				if (sp != 64 && sp != 96) {
					ref = league.positionPenalization[spPlayer][sp];
				}
				// el popup que informa de que no se puede hacer el mov tiene mas prioridad
				if (overflow) {
					formModalPopup(loc[167], COLOR_POPUP_BAD);
				}
				if (league.playerIsSanctioned(league.userTeam.players[pickedPlayer])) {
					formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + "is sanctioned and cannot be swaped", COLOR_POPUP_BAD);
					overflow = true;
				}
				if (league.playerIsSanctioned(league.userTeam.players[selectionTo])) {
					formModalPopup(league.playerGetName(league.userTeam.players[selectionTo]) + " " + "is sanctioned and cannot be swaped", COLOR_POPUP_BAD);
					overflow = true;
				}
				// informar sobre cambios que descuentan
				if (benchToPlay && !overflow) {
					selectingPosition = false;
					invokeTacticsPositionConfirm(pickedPlayer, selectionTo, iformScrollTopItem, iformSelectedItem);
					return;
				}
				if (selectionTo < 11 && league.playerIsInjured(league.userTeam.players[pickedPlayer]) && sp != 96) {
					formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + loc[72], COLOR_POPUP_BAD);
				} else if (selectionTo < 11 && league.playerIsSanctioned(league.userTeam.players[pickedPlayer]) && sp != 96) {
					formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + loc[73], COLOR_POPUP_BAD);
				} else if (ref < 100) {
					formModalPopup(loc[74] + " " + ref + "% " + loc[75] + " (" + league.playerGetName(league.userTeam.players[pickedPlayer]) + ")", COLOR_POPUP_BAD);
				}
				if (!overflow) {
					league.userTeam.swapOrdinalToOrdinal(pickedPlayer, selectionTo);
				}
				selectingPosition = false;
				pickedPlayer = -1;
				int t1 = iformScrollTopItem;
				int t2 = iformSelectedItem;
				invokeTacticsPosition(true);
				// hack hack haslkjdlskjd blaeh
				iformScrollTopItem = t1;
				iformSelectedItem = t2;
				focusedPlayer = selectionTo;
				// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
				iformDataHeaderSmall = remainingMatchChanges + " " + loc[166];
			} else {
				// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
				iformDataHeaderSmall = loc[70];
				setAttToUserTeamPlayerListing();
				int spPlayer = league.playerGetSpec(league.userTeam.players[pickedPlayer]);
				int sp = league.formationsSpec[league.coachFormation][selectionTo];
				int ref = 100;
				if (sp != 64 && sp != 96) {
					ref = league.positionPenalization[spPlayer][sp];
				}
				if (league.playerIsInjured(league.userTeam.players[pickedPlayer]) && sp != 96) {
					formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + loc[72], COLOR_POPUP_BAD);
				} else if (league.playerIsSanctioned(league.userTeam.players[pickedPlayer]) && sp != 96) {
					formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + loc[73], COLOR_POPUP_BAD);
				} else if (ref < 100) {
					formModalPopup(loc[74] + " " + ref + "% " + loc[75]+ " (" + league.playerGetName(league.userTeam.players[pickedPlayer]) + ")", COLOR_POPUP_BAD);
				}
				league.userTeam.swapOrdinalToOrdinal(pickedPlayer, selectionTo); 
				selectingPosition = false;
				pickedPlayer = -1;
				int t1 = iformScrollTopItem;
				int t2 = iformSelectedItem;
				invokeTacticsPosition(false);
				// hack hack haslkjdlskjd blaeh
				iformScrollTopItem = t1;
				iformSelectedItem = t2;
				focusedPlayer = selectionTo;
			}
		}
	}
}

// ----------------------------------------------------------------------------
// tactics - position - confirm

private int ordPickedPlayer;
private int ordDestPlayer;
private int savediformScrollTopItem;
private int savediformSelectedItem;
private void invokeTacticsPositionConfirm(int from, int to, int ti, int si) {
	ordPickedPlayer = from;
	ordDestPlayer = to;
	savediformScrollTopItem = ti;
	savediformSelectedItem = si;
	fontSet(iformFontDB);
	formYesNoList(loc[83], loc[208], loc[3], loc[2],
		printTextBreak(loc[209] + " " + league.playerGetName(league.userTeam.players[from]) +
			" " + loc[210] + " " + 
			league.playerGetName(league.userTeam.players[to]), canvasWidth - 6));
	iformFont = iformFontDB;
	currentForm = FORM_TACTICS_POSITION_CONFIRM;
}

private void actionTacticsPositionConfirm(int hiEvent) {
	if (hiEvent == HIEVENT_NO) {
		invokeTacticsPosition(true);
		iformScrollTopItem = savediformScrollTopItem;
		iformSelectedItem = savediformSelectedItem;
		focusedPlayer = ordDestPlayer;
		return;
	} else if (hiEvent == HIEVENT_YES) {
		remainingMatchChanges--;
		league.userTeam.swapOrdinalToOrdinal(ordPickedPlayer, ordDestPlayer); 
		invokeTacticsPosition(true);
		iformScrollTopItem = savediformScrollTopItem;
		iformSelectedItem = savediformSelectedItem;
		focusedPlayer = ordDestPlayer;
		return;
	}
}


// ----------------------------------------------------------------------------
// save before quit

private void invokeSave() {
	String[] saveItems = printTextBreak(loc[76], canvasWidth - 6);
	formYesNoList(loc[77], null, loc[3], loc[2], saveItems);
	currentForm = FORM_SAVE;
}

private void actionSave(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {
		formWait(true);
		byte[] data = league.saveState();
		boolean err = savePersistent("savegame", data);
		formWait(false);
		invokeStartMenu();
		if (err) {
			formModalPopup(loc[78], COLOR_POPUP_BAD);
		}
	} else if (hiEvent == HIEVENT_NO) {
		invokeStartMenu();
	}
}

// ----------------------------------------------------------------------------
// match

private int countDownPlayers(int max) {
	int downPlayers = 0;
	for (int i = 0; i < max; i++) {
		if (league.playerIsSanctioned(league.userTeam.players[i]) || league.playerIsInjured(league.userTeam.players[i]))
			downPlayers++;
	}
	return downPlayers;
}

private void invokeMatch() {
	//String[] matchItems = printTextBreak(league.journeyMatch[0].name + " " + loc[79] + " " +
	//	league.journeyMatch[1].name, canvasWidth - 5);
	int downPlayers = countDownPlayers(11);
	String[] mi = new String[2+downPlayers];
	mi[0] = league.journeyMatch[0].name + " " + loc[79] + " " + league.journeyMatch[1].name;
	if (downPlayers > 0) {
		mi[1] = loc[211] + ":";
		int j = 2;
		for (int i = 0; i < 11; i++) {
			if (league.playerIsSanctioned(league.userTeam.players[i]) || league.playerIsInjured(league.userTeam.players[i])) {
				mi[j] = league.playerGetName(league.userTeam.players[i]);
				j++;
			}
		}
	} else {
		mi[1] = "";
	}
	// hack...
	fontSet(iformFontDB);
	String[] matchItems = printTextBreak(mi, canvasWidth - 6);
	//System.out.println("" + (canvasWidth - 5));
	formPlainListYesNo(loc[80], loc[81], loc[3], loc[2], matchItems);
	currentForm = FORM_MATCH;
	iformFont = iformFontDB;
}

private void actionMatch(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {
		if (countDownPlayers(11) > 0) {
			invokeMainMenu();
			formModalPopup("You can not play because there are injured or sanctioned players in your team.", COLOR_POPUP_BAD);
		} else {
			invokeMatchPlay();
		}
	} else if (hiEvent == HIEVENT_NO) {
		invokeMainMenu();
	}
}

// ----------------------------------------------------------------------------
// match play

private void resumeMatch() {
	currentForm = FORM_MATCH_PLAY;
	String[] matchItems = { };
	formYesNoList("", "", loc[82], loc[83], matchItems, false);
	screenLost = true;
}

private Image numImg;
private void invokeMatchPlay() {
	String[] matchItems = { };
	formWait(true);
	iformBackground = null;
	//iformTopBackground = null;
	System.gc();
//#ifdef J2ME
	iformBackground = loadImage("/4.png");
	altTopBackground = iformTopBackground;
	iformTopBackground = loadImage("/marcador.png");
//#ifdef COMPONERMARCADOR
//#endif
//#else
//#endif
	currentForm = FORM_MATCH_PLAY;

	// juega toda la jornada MENOS el partido del user
	// ESTA LLAMADA ANTES QUE setupPlayerMatch SIEMPRE
	league.playJourney();

	// prepara el partido del player, que se jugara por ticks minuto a minuto
	league.setupPlayerMatch();
	league.gamecanvas = this;

	minute = 0;
	mpTimeText = "" + str60(minute);
	mpEventsText = new String[0];
	mpEventsTextTop = 0;
	topMinute = 90 + (league.rand() % 4); 
	mpScoreLeft = 0;
	mpScoreRight = 0;
	mpPrevTopTrain = league.coachTopTraining;
	mpEventCompletion = null;
	formWait(false);
	formYesNoList("", "", loc[82], loc[83], matchItems, false);
	remainingMatchChanges = 3;
}

private long nextTick = 0;
private int minute = 0;
private int topMinute = 0;
private String mpTimeText;
private String[] mpEventsText = null;
private int mpEventsTextTop = 0;
private String mpEventCompletion;
private int mpTicksToCompletion = 0;
private int mpScoreLeft;
private int mpScoreRight;
private int mpPrevTopTrain;

private String str60(int i) {
	if (i < 10) {
		return "0" + i;
	}
	return "" + i;
}

public int hackSound = -1;
public int hackMins = -1;
public void addMatchEvent(String event, String eventCompletion) {
	String[] matchItems = { };
	int ns = mpEventsText.length + 1;
	if (ns > 10)
		ns = 10;
	String[] s = new String[ns];
	System.arraycopy(mpEventsText, 0, s, 1, s.length - 1);
	// hack, para que no queden los segundos mal...
	if (eventCompletion != null && event != null) {
		hackMins = league.rand() % 30;
		s[0] = "" + str60(minute) + ":" + str60(hackMins) + " - " + event;
	} else {
		s[0] = "" + str60(minute) + ":" + str60(hackMins + 2 + (league.rand() % 2)) + " - " + event;
	}
	mpEventsText = s;
	mpEventCompletion = eventCompletion;
	if (eventCompletion != null) {
		formYesNoList("", "", loc[82], null, matchItems, false);
		mpTicksToCompletion = 6;
	} else {
		mpTicksToCompletion = 0;
		if (hackSound > -1) {
			soundPlay(hackSound, 1);
			hackSound = -1;
		}
	}
}

private void actionMatchPlay(int hiEvent) {
	String[] matchItems = { };
	// caso 1: tick de tiempo, no estamos al final
	if (hiEvent == HIEVENT_IDLE && minute <= topMinute) {
		if (nextTick > GameMilis)
			return;
		if (mpEventCompletion == null) {
			// jugar tick. esto llamara a addMatchEvent si es necesario
			minute++;
			String m = league.tickPlayerMatch(true);
			if (m != null && league.matchPopup) {
				// ha habido un evento que merecia saltar a los swaps
				invokeTacticsPosition(true);
				formModalPopup(m, COLOR_POPUP_GOOD);
			}
		} else {
			mpTicksToCompletion--;
			if (mpTicksToCompletion <= 0) {
				formYesNoList("", "", loc[82], loc[83], matchItems, false);
				addMatchEvent(mpEventCompletion, null);
				mpScoreLeft = league.journeyMatch[0].matchGoals;
				mpScoreRight = league.journeyMatch[1].matchGoals;
			}
		}
		nextTick = GameMilis + 333;
		mpTimeText = "" + str60(minute) + ":00";
		screenLost = true;
		return;
	}
	// caso 2: estamos al final
	if (minute > topMinute) {
		league.postPlayerMatch();
		mpTimeText = "" + str60(topMinute) + ":" + str60(league.rand() % 60);
		invokeMatchPost();
		formModalPopup(league.ppmResultMessage, COLOR_POPUP_GOOD);
		return;
	}
	// caso 3: no estamos al final, ni hay tick de tiempo: pantalla de cambiar jugador o skip 
	if (hiEvent == HIEVENT_YES) {
		// solo dejamos hacer cambios si estamos fuera de una jugada
		if (mpTicksToCompletion <= 0) {
			invokeTacticsPosition(true);
		}
	} else if (hiEvent == HIEVENT_NO) {
		// todo lo que queda de golpe
		while (minute <= topMinute) {
			league.tickPlayerMatch(false);
			minute++;
		}
		mpScoreLeft = league.journeyMatch[0].matchGoals;
		mpScoreRight = league.journeyMatch[1].matchGoals;
		mpTimeText = "" + str60(topMinute) + ":" + str60(league.rand() % 60);
		league.postPlayerMatch();
		mpTicksToCompletion = -1;
		mpEventCompletion = null;
		invokeMatchPost();
		formModalPopup(league.ppmResultMessage, COLOR_POPUP_GOOD);
		return;
	}
}

// ----------------------------------------------------------------------------
// match post

private void invokeMatchPost() {
	String[] it = new String[] { loc[212], loc[213], loc[214], loc[134] };
	tcpb = 3;
	formPlainMenu("", "", it);
	mpEventsText = new String[0];
	currentForm = FORM_MATCH_POST;
}

private void actionMatchPost(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == 0) {
		actionMatchPostMatchStats();
		return;
	} else if (selectionTo == 1) {
		invokeMatchPostRoundStats();
		return;
	} else if (selectionTo == 2) {
		if (league.lastEuropeRound != null) {
			invokeMatchPostEuropeStats();
		} else {
			formModalPopup(loc[215],
				COLOR_POPUP_GOOD);
		}
		return;
	} else if (selectionTo == tcpb) {
		formWait(true);
		altTopBackground = null;
		iformBackground = null;
		iformTopBackground = null;
		//numImg = null;
		System.gc();
//#ifdef J2ME
		iformBackground = loadImage("/1.png");
		iformTopBackground = loadImage("/menu.png");
//#else
//#endif
		formWait(false);
		numImg = null;
		if (league.journeyNumber < league.journeyFinal) {
			league.endJourney();
			league.startJourney();
			System.gc();
			invokeMainMenu();
		} else {
			// es el final de liga
			System.gc();
			invokeEndLeague();
		}
		return;
	}
}

// ----------------------------------------------------------------------------
// match post - round stats

private void invokeMatchPostRoundStats() {
	String[] classLabels = { loc[37], "", "" };
	int n = league.lastLeagueRound.length/2 + 1;
	String[] st = new String[n];
	int i = 0;
	for (i = 0; i < st.length-1; i++) {
		st[i] = "|" + league.lastLeagueRound[i*2].name + " vs. " +
			league.lastLeagueRound[i*2+1].name + 
			"|" + league.lastLeagueResults[i*2] +
			"|" + league.lastLeagueResults[i*2+1] + "|"; 
	}
	st[i] = "|" + loc[1] + "|||";
	tpbv = i;
	String[][] its = new String[3][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	formPlainMenu(loc[213], loc[123] + " " + (league.journeyNumber + 1), its, classLabels);
	iformFont = iformFontDB;
	currentForm = FORM_MATCH_POST_ROUND_STATS;
}

private void actionMatchPostRoundStats(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tpbv) {
		invokeMatchPost();
		return;
	}
}


// ----------------------------------------------------------------------------
// match post - europe stats

private void invokeMatchPostEuropeStats() {
	String[] classLabels = { loc[37], "", "" };
	int n = league.lastEuropeRound.length/2 + 1;
	String[] st = new String[n];
	int i = 0;
	for (i = 0; i < st.length-1; i++) {
		st[i] = "|" + league.lastEuropeRound[i*2].name + " vs. " +
			league.lastEuropeRound[i*2+1].name + 
			"|" + league.lastEuropeResults[i*2] +
			"|" + league.lastEuropeResults[i*2+1] + "|"; 
	}
	st[i] = "|" + loc[1] + "|||";
	tpbv = i;
	String[][] its = new String[3][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	formPlainMenu(loc[214], league.getChampJourneyNameProto(2), its, classLabels);
	iformFont = iformFontDB;
	currentForm = FORM_MATCH_POST_EUROPE_STATS;
}

private void actionMatchPostEuropeStats(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tpbv) {
		invokeMatchPost();
		return;
	}
}


// ----------------------------------------------------------------------------
// match post - match stats

private void actionMatchPostMatchStats() {
	int downPlayers = countDownPlayers(16);
	String[] matchItems = new String[4+downPlayers];
	matchItems[0] = "- " + league.ppmResultMessage;
	league.calcTopTraining();
	if (league.journeyMatch[0] == league.userTeam) {
		int p = league.teams[league.playingLeague].length - league.userTeamPos();
		int val = (p * 35) / league.teams[league.playingLeague].length;
		matchItems[1] = "- " + loc[193] + " " + League.cashStr(val) + " " + loc[188];
		league.coachCash += val;
	} else {
		matchItems[1] = "";
	}
	if (league.journeyNumber == league.journeyFinal) {
		matchItems[2] = "- " + loc[91] + " " + league.userTeamPosStr();
	} else {
		matchItems[2] = "";
	}
	if (downPlayers > 0) {
		matchItems[3] = "- " + loc[216] + ":";
		int j = 4;
		for (int i = 0; i < 16; i++) {
			if (league.playerIsSanctioned(league.userTeam.players[i]) || league.playerIsInjured(league.userTeam.players[i])) {
				matchItems[j] = league.playerGetName(league.userTeam.players[i]);
				j++;
			}
		}
	} else {
		matchItems[3] = "";
	}
	fontSet(iformFontDB);
	String[] mi = printTextBreak(matchItems, canvasWidth - 6);
	formYesNoList(loc[212], "", "", loc[92], mi, false);
	iformFont = iformFontDB;
	currentForm = FORM_MATCH_POST_MATCH_STATS;
}

private void actionMatchPostMatchStats(int hiEvent) {
	if (hiEvent != HIEVENT_IDLE)
		invokeMatchPost();
}

/*
private void invokeMatchPost(int r) {
	altTopBackground = null;
	String[] matchItems = { null, null, null };
	if (r == 0) {
		matchItems[0] = loc[84];
	} else if (r == 1) {
		matchItems[0] = loc[85];
	} else if (r == 2) {
		matchItems[0] = loc[86];
	} else if (r == 3) {
		matchItems[0] = loc[87];
	}
	if (league.ppmFinal == 1) {
		matchItems[1] = loc[88];
	} else if (league.journeyNumber == league.journeyFinal) {
		matchItems[1] = loc[89];
	} else {
		matchItems[1] = "";
	}
	league.calcTopTraining();
	if (league.journeyMatch[0] == league.userTeam) {
		int p = league.teams[league.playingLeague].length - league.userTeamPos();
		int val = (p * 35) / league.teams[league.playingLeague].length;
		matchItems[2] = loc[193] + " " + League.cashStr(val) + " " + loc[188];
		league.coachCash += val;
	} else {
		matchItems[2] = "";
	}
	if (league.journeyNumber == league.journeyFinal) {
		matchItems[2] = loc[91] + " " + league.userTeamPosStr();
	}
	String[] mi = printTextBreak(matchItems, canvasWidth - 6);
	formYesNoList("", "", "", loc[92], mi, false);
	mpEventsText = new String[0];
	currentForm = FORM_MATCH_POST;
}

private void actionMatchPost(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {
		formWait(true);
		iformBackground = null;
		iformTopBackground = null;
		numImg = null;
		System.gc();
//#ifdef J2ME
		iformBackground = loadImage("/1.png");
		iformTopBackground = loadImage("/menu.png");
//#else
		iformBackground = FS_LoadImage(2,0);
		iformTopBackground = FS_LoadImage(2,1);
//#endif
		formWait(false);
		if (league.journeyNumber < league.journeyFinal) {
			league.endJourney();
			league.startJourney();
			System.gc();
			invokeMainMenu();
		} else {
			// es el final de liga
			System.gc();
			invokeEndLeague();
		}
	}
}
*/

// ----------------------------------------------------------------------------
// end of league

private void invokeEndLeague() {
	fontSet(iformFontDefault);
	String[] s = printTextBreak(loc[93], canvasWidth - 6);
	formYesNoList(loc[94], "", loc[3], loc[2], s);
	currentForm = FORM_END_LEAGUE;
}

private void actionEndLeague(int hiEvent) {
	if (hiEvent == HIEVENT_NO) {
		league.startSeason();
		league.startJourney();
		invokeMainMenu();
	} else if (hiEvent == HIEVENT_YES) {
		invokeChooseLeague();
	}
}

};

