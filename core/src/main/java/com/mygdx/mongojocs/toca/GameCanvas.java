package com.mygdx.mongojocs.toca;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// Nokia series 40
// ------------------------------------

//#ifdef J2ME
// BiosCanvas integrada dentro de GameCanvas -------------------------

//public class GameCanvas extends BiosCanvas                          // BiosCanvas integrada dentro de GameCanvas 
// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;

//#ifdef J2ME
  //#ifdef MO-C450
	//#elifdef NOKIAUI
		//#ifdef FULLSCREEN
			public class GameCanvas extends FullCanvas
		//#else
		//#endif
	//#else
	//#endif
//#elifdef DOJA
//#endif
{
// PORTAR DE IMODE A JAVA --------------------------------------------------------------------------------------------------
//#ifdef J2ME
public int getColorOfRGB(int R, int G, int B)
{ //ga.DEBUGPaint("getColorOfRGB");
	int RGB=0;
	RGB += R*0x010000;
	RGB += G*0x0100;
	RGB += B;
	return RGB;
}
//#elifdef DOJA
//#endif

// ---------------------------------- //
//  Bits de Control de cada Terminal
// ---------------------------------- //
//final boolean deviceSound = true;
//final boolean deviceVibra = true;
// ---------------------------------- //

boolean forceRepaint = false;
Game ga;
//#ifdef TOCA
//int[] trackFactor = {256,384,240,216,512,384};
int[] trackFactor = {256,384,235,210,450,384};
//#else
//#endif

//#ifdef EXCEPTION_CONTROL
Exception excp;
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef AUTO_CLEANER
int	lastGC = 0;
//#endif

boolean	forcePauseFirst = true;
boolean forcePause = false;

//#ifdef LISTENER
//#endif

//#ifdef SHOW_VERSION
//#ifndef SG-Z107
//#ifndef SG-Z105
String versionStr = null;
//#endif
//#endif
//#endif

public GameCanvas(Game ga) 
{	
	System.out.println(oMathFP.toFP(1));
	this.ga = ga;
	canvasCreate();
	
	//#ifdef LISTENER
	//#endif
}

//#ifdef LISTENER
//#endif



// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics scr;
//#ifdef TOCA
int scarMenu = 2 * 4;
int scarMDir = 1;
//#endif
int rotAngle = 0;
int arrowPos = 0;
public void paint (Graphics g)
{       
	//#ifdef EXCEPTION_CONTROL
	/*if(excp != null) {
		g.setClip(0,0,canvasWidth,canvasHeight);
		g.setColor(0xff0000);
		g.fillRect(0,0,canvasWidth,canvasHeight);
		g.setColor(0xffffff);
		g.drawString(excp.getMessage(), 10, 10, 20);
		excp = null;
		return;
	}*/
	//#endif

	//if (canvasShow && !painting)
	if(canvasShow) {
	
	painting = true;
	canvasShow=false;
	scr = g;
	
//#ifdef DOJA
//#endif

	try {

	canvasDraw();
	
	} catch (Exception e) {
	//#ifdef EXCEPTION_CONTROL
	//excp = e;
	//#endif
	System.out.println("*** Exception en la parte Grafica ***"); e.printStackTrace();
	}
	
	//#ifdef SHOW_VERSION
	//#ifndef SG-Z107
	//#ifndef SG-Z105
		if(versionStr == null) {
			versionStr = "2022";
		/*ByteArrayOutputStream baos = null;
		try {
			InputStream is = getClass().getResourceAsStream("/build.txt");
			DataInputStream dis = new DataInputStream(is);
			
			baos = new ByteArrayOutputStream();


			int br = -1;
			//skip bytes until we find and =
			while(br != 0x3D) {
				br = dis.readUnsignedByte();
			}


			br = -1;
			while(br != 0x0A) {
				br = dis.readUnsignedByte();
				if(br != 0x0A) {
					br = br - '0';
					System.out.println(br);
					baos.write(br);
				}
			}
			
			int k = 1;
			int total = 0;
			byte[] num = baos.toByteArray();
			for(int i = 0; i < num.length; i++) {
				total += num[num.length - 1 - i] * k;
				k *= 10;
			}
			System.out.println("total: " + total);
			versionStr = String.valueOf(total);
		} catch(Exception e) {}*/
	}
	/*g.setClip(0,0,canvasWidth,canvasHeight);
	g.setFont(Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN, Font.SIZE_SMALL));
	g.setColor(0x000000);
	g.drawString(versionStr, 8, 8, 20);
	g.drawString(String.valueOf(keyMenu), 8, 17, 20);*/
	//#endif
	//#endif
	//#endif
//#ifdef DOJA
//#endif

	painting = false;
	scr = null;
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw()
{
	//if(painting) return;
	refresh();
	/*while (canvasShow==true) {
		try {
			//#ifdef K500
			//#else
			Thread.sleep(1);
			//#endif
		} catch(Exception e) {
		//#ifdef EXCEPTION_CONTROL
		//	excp = e;
		//#endif
		}
	}*/
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyMenu, keyX, keyY, keyMisc, keyCode;

//#ifdef J2ME

public void keyPressed(int keycode)
{
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=keycode;

	switch (keyCode)
	{
	case Canvas.KEY_NUM0:
		keyMisc=10;
	break;

	case Canvas.KEY_NUM1:
		keyMisc=1; //keyX=-1; keyY=-1;
		//#ifdef NK-3650
		//#endif
	break;

	case Canvas.KEY_NUM2:	// Arriba
		keyMisc=2;
		//#ifdef NK-3650
		//#else
		keyY = -1;
		//#endif
	break;

	case Canvas.KEY_NUM3:
		keyMisc=3; //keyX= 1; keyY=-1;
		//#ifdef NK-3650
		//#endif
	break;

	case Canvas.KEY_NUM4:	// Izquierda
		keyMisc=4;
		//#ifndef NK-3650
		//#endif
	break;

	case Canvas.KEY_NUM5:	// Disparo
		keyMisc=5;
		keyMenu=2;
	break;

	case Canvas.KEY_NUM6:	// Derecha
		keyMisc=6;
		//#ifndef NK-3650
		//#endif
	break;

	case Canvas.KEY_NUM7:
		keyMisc=7; //keyX=-1; keyY= 1;
	break;

	case Canvas.KEY_NUM8:	// Abajo
		keyMisc=8;
		//#ifndef NK-3650
		//#endif
	break;

	case Canvas.KEY_NUM9:
		keyMisc=9; //keyX= 1; keyY= 1;
	break;

	case 35:		// *
		keyMisc=35;
		//#ifdef NK-3650
		//#endif
	break;

	case 42:		// #
		keyMisc=42;
	break;

// -----------------------------------------
  	//#ifdef NK
		case -6:	// Nokia - Menu Izquierda
			keyMenu = -1;
		break;
	
		case -7:	// Nokia - Menu Derecha
			keyMenu = 1;
		break;
	//#elifdef SE
	//#elifdef SG
	//#elifdef MO-T720
	//#elifdef SI
	//#elifdef MO-V3xx
	//#elifdef MO-V535
	//#elifdef MO-V3RAZR
	//#elifdef AL-756
	//#elifdef AL-556
	//#elifdef MO-C65x
	//#elifdef SH-GX15
	//#elifdef SM-MYV55
	//#elifdef LG-U8150
	//#endif
	}
	
	if(keyMenu == 0) {
		switch(getGameAction(keycode)){
			case 1:keyY=-1;break;	// Arriba
			case 6:keyY=1;break;	// Abajo
			case 5:keyX=1;break;	// Derecha
			case 2:keyX=-1;break;	// Izquierda
			//#ifndef SI
			case 8:keyMenu=2;break;	// Fuego
			//#else
			//#endif
		}
	}
	
	if(keyMenu == 1) keyMenu = 2;		//zaska :U_
	//fast hack to prevent exit when not desired.
	if(keyMenu == -1 || keyY != 0 || keyX != 0) {
		ga.exitConfirmation = false;
	}
}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void keyReleased(int keycode)
{
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=0;
}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#ifdef DOJA
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME
/*
public synchronized boolean showGame() {
	if(ga.gamePaused) {
		painting = false;
		//enqueueNotify("X");
		switch(ga.gameStatus) {
			case Game.GAME_PLAY + 2:
				//enqueueNotify("1");
				ga.menuInit(ga.MENU_SECOND);
				
				//#ifndef FULL_FEATURED
				if (ga.biosStatus == 0 && !ga.tbOnScreen) {
					ga.menuShow = true;
					ga.menuListShow = true;
				}
				//#endif
				break;
			
			case Game.GAME_MENU_MAIN:
			case Game.GAME_PLAY:
			case Game.GAME_PLAY + 1:
			case Game.GAME_MENU_SECOND:
			case Game.GAME_MENU_GAMEOVER:
			case Game.GAME_MENU_END_TRACK:
				//enqueueNotify("2");
				ga.menuExit = false;
				ga.loading = -1;		//dunno
				ga.menuInit(ga.menuType);
				break;
			
			case Game.GAME_LOGOS:
			case Game.GAME_LEGAL_TEXT:
			case Game.GAME_LEGAL_TEXT2:
			case Game.GAME_COVER:
				//enqueueNotify("3");
				// nothing
				break;
			
			default:
				//enqueueNotify("E");
				break;
		}
		ga.gamePaused = false;
		return true;
	}
	
	return false;
}
*/
public void showNotify() 
{	
	//#ifndef s60
	if(ga != null) {
		ga.currentSound = -1;
		if(ga.gameStatus == (ga.GAME_PLAY + 2) && !ga.menuSecActive) {
			ga.menuInit(ga.MENU_SECOND);
		} else {
			//#ifndef FULL_FEATURED
			//#endif
			//#ifdef SEMI_FULL_FEATURED
			//#endif
			ga.menuExit = false;
			
			//#ifndef MO
			ga.currentSound = -1;
			soundStop();
			ga.playSound(ga.SOUND_MAIN_MENU, 0);
			//#endif
		}
		
		ga.gamePaused = false;
		
		//#ifdef MO
		//#endif
	}
	
	//#ifdef AL
	//#endif
	forceRepaint = true;
	//#else
	//#endif
}

public void hideNotify() {
	//#ifndef s60
	forceRepaint = true;	//force repainting when the applications returns
	if(ga != null) {
		ga.gamePaused = true;
		//#ifndef MO
		ga.currentSound = -1;
		//#endif
	}
	//#ifndef MO
	soundStop();
	//#endif
	//#else
	//#endif
}
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void showTiledImage(Image img, int despx, int despy) {
	int repx, repy;
	int rx, ry;
	
	//#ifdef FULL_FEATURED
	rx = despx % img.getWidth();
	ry = despy % img.getHeight();
	//#else
	//#endif
	
	repx = canvasWidth / img.getWidth() + 2;
	repy = canvasHeight / img.getHeight() + 2;
	
	for(int i = 0; i < repy; i++) {
		for(int j = 0; j < repx; j++) {
			showImage(img, j * img.getWidth() - rx, i * img.getHeight() - ry);
		}
	}
}

//#ifdef J2ME

public void showImage(Image Img)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ----------------------------------------------------------

public void showImageDispl(Image Img, int x, int y)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2 + x, (canvasHeight-Img.getHeight())/2 + y);
}

// ----------------------------------------------------------

public void showImage(Image Img, int X, int Y)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{ 
	//#ifdef SI
	//#else
	if (DestX >= canvasWidth || DestY >= canvasHeight) {return;}
	//#endif

	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void showImage(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	scr.setClip(TopX, TopY, FinX, FinY);
	scr.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}

//#elifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// -------------------
// canvas Fill
// ===================

public void canvasFill(int RGB)
{
	//#ifdef J2ME
	scr.setClip (0,0, canvasWidth,canvasHeight );
	scr.setColor(RGB);
	//#elifdef DOJA
	//#endif
	scr.fillRect(0,0, canvasWidth,canvasHeight );
	
	//#ifdef SI-C65
	//#endif
}


// -------------------
// canvas Fill Init
// ===================

public void canvasFillInit(int RGB)
{
	canvasFillRGB = RGB;
	canvasFillShow = true;
}

// -------------------
// load Image
// ===================

//#ifdef J2ME

public Image loadImage(String FileName)
{
	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e) {System.out.println("Error leyendo PNG: "+FileName);}

	System.gc();
	return Img;
}

//#elifdef DOJA
//#endif

// ===================





// *********************
// ---------------------
// canvasText - Engine - Gfx
// =====================
// *********************

String canvasTextStr;
String canvasTextStrA[];
int canvasTextX;
int canvasTextY;
int canvasTextRGB;
int canvasTextMode;
boolean canvasTextShow = false;

int printerX = 0;
int printerY = 0;
int printerSizeX = getWidth();
int printerSizeY = getHeight();

// -------------------
// canvasText Create
// ===================

public void canvasTextCreate()
{
	printerX = 0;
	printerY = 0;
	printerSizeX = canvasWidth;
	printerSizeY = canvasHeight;
}

public void canvasTextCreate(int x, int y, int sizeX, int sizeY)
{
	printerX = x;
	printerY = y;
	printerSizeX = sizeX;
	printerSizeY = sizeY;
}

// -------------------
// canvasText Init
// ===================

public void canvasTextInit(String str, int x, int y, int rgb, int mode)
{
	canvasTextStrA = null;
	canvasTextStr = str;
	canvasTextX = x;
	canvasTextY = y;
	canvasTextRGB = rgb;
	canvasTextMode = mode;
	canvasTextShow = true;
}

public void canvasTextInit(String str[], int x, int y, int rgb, int mode)
{
	canvasTextStrA = str;
	canvasTextStr = null;
	canvasTextX = x;
	canvasTextY = y;
	canvasTextRGB = rgb;
	canvasTextMode = mode;
	canvasTextShow = true;
}

// -------------------
// canvasText Draw
// ===================
public void canvasTextDraw()
{
	//System.out.println("canvasTextDraw :: drawin' : " + canvasTextStr + " color: " + canvasTextRGB);
	if(canvasTextStrA != null) {
		for(int i = 0; i < canvasTextStrA.length; i++) {
			//hack !!! not useful
			textDraw(canvasTextStrA[i], canvasTextX, canvasTextY + i * 20, canvasTextRGB, canvasTextMode);
		}
	} else {
		textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
	}
}

// <=- <=- <=- <=- <=-



// *********************
// ---------------------
// text - Engine - Gfx
// =====================
// *********************

static final int TEXT_PLAIN =	0x000;
static final int TEXT_BOLD =	0x010;

static final int TEXT_SMALL =	0x000;
static final int TEXT_MEDIUM =	0x100;
static final int TEXT_LARGER =	0x200;

static final int TEXT_LEFT =	0x000;
static final int TEXT_RIGHT =	0x001;
static final int TEXT_HCENTER =	0x002;
static final int TEXT_TOP =		0x000;
static final int TEXT_BOTTON =	0x004;
static final int TEXT_VCENTER =	0x008;

static final int TEXT_OUTLINE =	0x1000;

// -------------------
// text Draw
// ===================

public void textDraw(String Str, int X, int Y, int RGB, int Mode)
{
//#ifdef J2ME
	textDraw(_textoBreak(Str, printerSizeX, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
//#elifdef DOJA
//#endif
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
//#ifdef J2ME
	//#ifdef MENU_SMALL_FONT
	//#else
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
	//#endif
//#elifdef DOJA
//#endif
	scr.setFont(f);

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()*Str.length) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  printerSizeY-(f.getHeight()*Str.length) ;}

	X += printerX;
	Y += printerY;

	for (int i=0 ; i<Str.length ; i++)
	{

	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX +=( printerSizeX-f.stringWidth(Str[i]) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX += printerSizeX-f.stringWidth(Str[i]) ;}


		int despY = i * f.getHeight();


		if ((Mode & TEXT_OUTLINE)!=0)
		{
		scr.setColor(0);
//#ifdef J2ME
		scr.drawString(Str[i],  X-1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY , 20);
//#elifdef DOJA
//#endif
		}

		scr.setColor(RGB);
//#ifdef J2ME
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
//#elifdef DOJA
//#endif
	}

}


// <=- <=- <=- <=- <=-


public void drawStringS(Graphics g, String str, int y) {
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN, Font.SIZE_SMALL);
	int nx = (canvasWidth - f._stringWidth(str)) >> 1;
	int hF = ga.menuListFont.getHeight();
	g.setColor(0);
	g.drawString(str, nx-1,y - 1, 20);
	g.drawString(str, nx+1,y - 1, 20);
	g.drawString(str, nx-1,y + 1, 20);
	g.drawString(str, nx+1,y + 1, 20);
	g.setColor(0xffffff);
	g.drawString(str, nx, y, 20);
	//#ifndef TOCA
	//#endif
	g.setColor(0);
}

public void drawString(Graphics g, String str, int y) {
	int nx = (canvasWidth - ga.menuListFont.stringWidth(str)) >> 1;
	int hF = ga.menuListFont.getHeight();
	g.setColor(0);
	g.drawString(str, nx-1,y - 1, 20);
	g.drawString(str, nx+1,y - 1, 20);
	g.drawString(str, nx-1,y + 1, 20);
	g.drawString(str, nx+1,y + 1, 20);
	g.setColor(0xffffff);
	g.drawString(str, nx, y, 20);
	//#ifndef TOCA
	//#endif
	g.setColor(0);
}


public void drawString(Graphics g, String str, int x, int y, int shadeCol, int mode) {
	int nx = 0;
	switch(mode) {
		case 0:
			nx = x;
			break;
		case 1:
			nx = (canvasWidth - ga.menuListFont._stringWidth(str)) - x;
			break;
	}
	
	g.setColor(ga.menuShadow[shadeCol]);
	g.drawString(str, nx-1,y - 1, 20);
	g.drawString(str, nx+1,y - 1, 20);
	g.drawString(str, nx-1,y + 1, 20);
	g.drawString(str, nx+1,y + 1, 20);
	g.setColor(ga.menuCols[shadeCol]);
	g.drawString(str, nx, y, 20);
	g.setColor(0);
}

public void drawTrack(int[] data, Graphics g, String name, int nTrack) {
	int ox,oy,nx,ny,nz;
	int ox1,oy1,nx1,ny1;
	int rdata[];
	int ytext = 8;
	int scalex,scaley;
	
	//#ifndef FULL_FEATURED
	//#endif
	
	g.setClip(0,0, canvasWidth, canvasHeight);
	
	//#ifdef FULL_FEATURED
	if(ga.loading != 1) {
	//#endif
		drawString(g, name, ytext);
		int hF = ga.menuListFont.getHeight();
		g.drawLine(0, 8 + hF + 2, canvasWidth, 8 + hF + 2);
		//#ifdef TOCA
		g.setColor(0xffffff);
		g.drawLine(0, 8 + hF + 1, canvasWidth, 8 + hF + 1);
		//#endif
		
		rdata = new int[data.length];
		int max = data.length / 3;
		
		int minx = 10000;
		int miny = 10000;
		int maxx = -10000;
		int maxy = -10000;
		for(int i = 0; i < max; i++) {
			int mx = data[i * 3 + 0];
			int my = data[i * 3 + 1];
			
			if(mx < minx) minx = mx;
			if(mx > maxx) maxx = mx;
			
			if(my < miny) miny = my;
			if(my > maxy) maxy = my;
		}
		
		int sizex = (maxx - minx);
		int sizey = (maxy - miny);
		
		int midx = sizex >> 1;
		int midy = sizey >> 1;

		//#ifdef FULL_FEATURED
		int sa = oMathFP.sin(rotAngle);
		int ca = oMathFP.cos(rotAngle);
	
		//int sf = oMathFP.sin(-34314);		//60� en rad o 45?
		//int cf = oMathFP.cos(-34314);		//60� en rad
		
		int sf = oMathFP.sin(-40033);		//70� en rad
		int cf = oMathFP.cos(-40033);		//70� en rad
		
		//int sf = oMathFP.sin(-42893);		//75� en rad
		//int cf = oMathFP.cos(-42893);		//75� en rad
		
		for(int i = 0; i < max; i++) {
			int x = (data[i * 3 + 0] - minx) - midx;
			int y = (data[i * 3 + 1] - miny) - midy;
			
			x = (x * trackFactor[nTrack]) >> 8;
			y = (y * trackFactor[nTrack]) >> 7;
			
			//sintable!!
			
			//rotate
			//x' = x*cos q - y*sin q
			//y' = x*sin q + y*cos q
			nx = oMathFP.toInt(x * ca - y * sa);
			ny = oMathFP.toInt(x * sa + y * ca);
			
			//y' = y*cos q - z*sin q
			//z' = y*sin q + z*cos q
			nz = oMathFP.toInt(ny * sf);
			ny = oMathFP.toInt(ny * cf);
			
			//nx = (512 * nx) / ((nz >> 2) + 580);
			//ny = (512 * ny) / ((nz >> 2) + 580);
			nx = (512 * nx) / ((nz >> 2) + 580);
			ny = (512 * ny) / ((nz >> 2) + 580);
			
			//store
			rdata[i * 3 + 0] = nx;
			rdata[i * 3 + 1] = ny;
			rdata[i * 3 + 2] = data[i * 3 + 2];
		}
		//#else
		//#endif
		
		ox = rdata[0];
		oy = rdata[1];
		
		//precalcular i tal :U_
		
		//int saveCalc[] = new int[20];
		int saveCalc[] = new int[12 * (rdata.length/9)]; 
		g.setColor(0);
		int prevCol = 0;
		for(int j = 1; j >= 0; j--) {
			
			int fpx = 0;
			int fpy = 0;
			int max2 = rdata.length / 9; //12;
			
			for(int i = 0; i < max2; i++) {
				int pos = i * 3 * 3;
				int v1x = rdata[pos + 0 + 0];
				int v2x = rdata[pos + 0 + 3];
				int v3x = rdata[pos + 0 + 6];
				int v4x = rdata[pos + 0 + 9];
				
				int v1y = rdata[pos + 1 + 0];
				int v2y = rdata[pos + 1 + 3];
				int v3y = rdata[pos + 1 + 6];
				int v4y = rdata[pos + 1 + 9];
				
				int t = 0;	//oMathFP.ONE / 10;
				for(int k = 0; k < 6; k++) {
					int fx, fy;
					
					if(j == 1) {
						int ti   = oMathFP.ONE - t;
						
						int t2   = oMathFP.mul(t, t);
						int ti2  = oMathFP.mul(ti, ti);
						int h1   = oMathFP.mul(ti2, ti);
						int h2   = 3 * oMathFP.mul(t, ti2);
						int h3   = 3 * oMathFP.mul(t2, ti);
						int h4   = oMathFP.mul(t2, t);
						
						fx   = h1 * v1x + h2 * v2x + h3 * v3x + h4 * v4x;
						fy   = h1 * v1y + h2 * v2y + h3 * v3y + h4 * v4y;
						//#ifndef TOCA
						//#endif
						saveCalc[i * 12 + k * 2 + 0] = fx;
						saveCalc[i * 12 + k * 2 + 1] = fy;
					} else {
						fx = saveCalc[i * 12 + k * 2 + 0];
						fy = saveCalc[i * 12 + k * 2 + 1];
					}
					
					//j * 2 i j *6  == per tal de fer l'ombra..
					nx = oMathFP.toInt(fx) + j * 2;
					ny = oMathFP.toInt(fy) + j * 6;
					
					nx += canvasWidth / 2;
					ny += canvasHeight /2;
					if(i != 0) {
						g.drawLine(ox, oy, nx, ny);
						g.drawLine(ox, oy + 1, nx, ny + 1);
					} else {
						fpx = nx;
						fpy = ny;
					}
					ox = nx;
					oy = ny;
					t += oMathFP.ONE / 10;
				}
			}
			
			//ajuntar la ultima linea amb la primera, es com si faltes un catxo :? i fa una guarrada
			g.drawLine(ox, oy, fpx, fpy);
			g.drawLine(ox, oy + 1, fpx, fpy + 1);
			g.setColor(0xffffff);
			prevCol = 0xffffff;
		}
		
		rotAngle += 2600;
		rdata = null;
		saveCalc = null;
	//#ifndef FULL_FEATURED
	//#else
	} else {
		drawStringS(g, ga.gameText[ga.TEXT_LOADING][0], canvasHeight - 25);
		ga.loading = 0;
	}
	//#endif
}


public String[] _textoBreak(String texto, int width, Font f)
{
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int[] positions = new int[512];
	int count = 0;

	while ( posOld < texto.length() )
	{
		size = 0;

		pos = posIni;

		while ( size < width )
		{
			if ( pos == texto.length()) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

			if ( dat == '_') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat==0x20 ) { posOld = pos - 1; }
//#ifdef J2ME
			size += f._charWidth((char)dat);
//#elifdef DOJA
//#endif
	    }
    
	    if (posOld - posIni < 1)
		{ 
			while ( pos < texto.length() && texto.charAt(pos) >= 0x30 ) {pos++;}
			posOld = pos;
		}

		posIni = posOld + 1;
		positions[count] = posOld;
		count++;
	}

	String str[] = new String[count];
	posIni = 0;
	int posEnd;

	for(int i = 0; i < count; i++)
	{
		posEnd = positions[i];
		str[i] = texto.substring(posIni, posEnd);
		posIni = posEnd + 1;
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


int canvasWidth=getWidth();
int canvasHeight=getHeight();

int canvasFillRGB;
boolean canvasFillShow=false;
Image canvasImg;

boolean canvasShow=true;
boolean	painting = false;

// -------------------
// canvas Create
// ===================

public void canvasCreate()
{
	//#ifdef EARLY_FULLSCREEN
	//#endif
	
	canvasFillInit(0xffffff);
//#ifdef J2ME
	canvasImg = new Image();
	canvasImg._createImage("/Loading.png");
//#elifdef DOJA
//#endif
	refresh();
}

public void refresh() {
	canvasShow = true;
	
	//#ifndef MO
	//#endif
	
	forceRepaint = false;
	repaint();
	//#ifdef J2ME
	serviceRepaints();
	//#endif
}

// -------------------
// canvas Tick
// ===================

public synchronized void canvasDraw()
{
	boolean somshow = false;
	if(ga.biosStatus != 10000) {
		if (canvasFillShow) { canvasFillShow=false; canvasFill(canvasFillRGB); }
	
		if (canvasImg!=null) {showImage(canvasImg); canvasImg=null; System.gc(); somshow = true;}
	
		if (canvasTextShow) {canvasTextShow=false; canvasTextDraw(); somshow = true;}
		
		Draw(scr);	// Imprimimos el juego
		
		//dibuixar sempre!! :U_ <-- o no..
		if(ga.menuShow && !somshow) {	//dunno!!
			//#ifndef FULL_FEATURED
			//#endif
			menuDraw();
		}
	}
	//#ifdef AUTO_CLEANER
	//#endif
}


// <=- <=- <=- <=- <=-




// *******************
// -------------------
// menu - Engine - Gfx
// ===================
// *******************

public void menuDraw()
{

	ga.menuListDraw(scr);
	
	if(ga.exitConfirmation) {
		int hF = ga.menuListFont.getHeight();
		
		drawString(scr, ga.gameText[ga.TEXT_NO][0], 2, canvasHeight - hF - 1, 5, 0);
		drawString(scr, ga.gameText[ga.TEXT_YES][0], 2, canvasHeight - hF - 1, 5, 1);
	}
	
	if(ga.loading > 0) {
		drawTrack(ga.curvepoints, scr, ga.gameText[ga.TEXT_TRACK_NAME_1 + ga.currentTrack * 2][0], ga.currentTrack);
	}
	if(ga.selectingCar == 1) {
		drawString(scr, ga.gameText[ga.TEXT_SELECT_CAR][0], 8);
		int hF = ga.menuListFont.getHeight();
		//#ifdef TOCA
		drawString(scr, ga.gameText[ga.TEXT_SERIES_1 + ga.menuListPos % 2][0], 0, 8 + hF + 2,scarMenu >> 2, 1);
		
		scr.drawLine(0, 8 + hF + 3 + hF, canvasWidth, 8 + hF + 3 + hF);
		scr.setColor(0xffffff);
		scr.drawLine(0, 8 + hF + 2 + hF, canvasWidth, 8 + hF + 2 + hF);
		/*
		drawString(scr, ga.gameText[ga.TEXT_SERIES_1 + ga.menuListPos % 2][0], 0, 26,scarMenu >> 2, 1);
		
		scr.drawLine(0, 27 + hF, canvasWidth, 27 + hF);
		scr.setColor(0xffffff);
		scr.drawLine(0, 26 + hF, canvasWidth, 26 + hF);
		*/
		//#else
		//#endif
		
		//#ifdef FULL_FEATURED
		arrowPos--;
		if(arrowPos < 0) arrowPos = 20;
		//#ifdef ENGLISH
		//#else
		int xarrow1 = 5 + ((arrowPos) >> 1);
		int xarrow2 = (canvasWidth - 7) - ((arrowPos) >> 1);
		//#endif
		//#else
		//#endif
		
		//#ifdef K500
		//#endif
		
		//#ifdef SM-MYV55
		//#endif
		
		//#ifdef SI-CX65
		//#endif
		
		//#ifdef SI-C65
		//#endif
		
		//#ifdef SH
		//#endif
		
		//#ifdef PA-X60
		//#endif
		
		//#ifdef NK-s40
		//#endif
		
		//#ifdef NK-6600
		xarrow1 = 0;
		xarrow2 = canvasWidth - 7;
		//#endif
		
		//#ifdef AL-756
		//#endif
		
		//#ifdef AL-556
		//#endif
		
		//#ifdef SG-X450
		//#endif
		
		//#ifdef SG-E600
		//#endif
		
		//#ifdef LG-U8150
		//#endif
		
		//#ifdef SE-T610
		//#endif
		
		//#ifdef SE-Z600
		//#endif
		
		//#ifndef AL-756
		//#ifndef AL-556
		//#ifndef SG-X600
		//#ifndef SG-X100
		//#ifndef SG-E820
		//#ifndef NK-s30
		scr.setColor(0);
		scr.drawString("<", xarrow1 + 1, 8 + 1, 20);
		scr.drawString("<", xarrow1 + 1, 8 - 1, 20);
		scr.drawString("<", xarrow1 - 1, 8 - 1, 20);
		scr.drawString("<", xarrow1 - 1, 8 + 1, 20);
		
		
		scr.drawString(">", xarrow2 + 1, 8 + 1, 20);
		scr.drawString(">", xarrow2 + 1, 8 - 1, 20);
		scr.drawString(">", xarrow2 - 1, 8 - 1, 20);
		scr.drawString(">", xarrow2 - 1, 8 + 1, 20);
		
		scr.setColor(0xffffff);
		scr.drawString("<", xarrow1, 8, 20);
		scr.drawString(">", xarrow2, 8, 20);
		//#endif
		//#endif
		//#endif
		//#endif
		//#endif
		//#endif
		
		//#ifdef TOCA
		scarMenu += scarMDir;
		
		if(scarMenu == 2 * 4) scarMDir = 1;
		if(scarMenu == 5 * 4) scarMDir = -1;
		//#endif
	}
}

// <=- <=- <=- <=- <=-









/* ===================================================================

	soundCreate()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	soundPlay(n� Sonido , Repetir)
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
	//#ifdef PLAYER_OFF
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
		
		public void soundCreate()
		{	
			Sonidos = new Player[6];
	
			Sonidos[0] = SoundCargar("/0.mid");//main menu
			Sonidos[1] = SoundCargar("/1.mid");//accelerate
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
			if (Loop<1) {Loop=-1;}
		
			soundStop();
			if (!ga.gameSound) return;
		
			try
			{
				VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
				Sonidos[Ary].setLoopCount(Loop);
				Sonidos[Ary].start();
			}
			catch(Exception exception) {exception.printStackTrace();}
		
			SoundOld=Ary;
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
			//#ifdef VIBRA_ON
			if (ga.gameVibra)
			{
				try
				{
					Display.getDisplay(ga).vibrate(Time);
				}
				catch (Exception e) {}
			}
			//#endif
		}
		//#elifdef PLAYER_MIDP20_LOW_MEMORY
	//#elifdef PLAYER_MIDP20_FORCED
	//#elifdef PLAYER_MIDP20_CACHED
	//#elifdef PLAYER_VODAFONE
	//#elifdef PLAYER_MMF
	//#elifdef PLAYER_MOTC450
	//#elifdef PLAYER_SIEMENS_MIDI
	//#elifdef PLAYER_SIEMENS_MIDI_STOP
	//#elifdef PLAYER_TSM6
	//#endif
//#elifdef DOJA
//#endif

// <=- <=- <=- <=- <=-


//#ifdef DOJA

// <=- <=- <=- <=- <=-
//#elifdef J2ME
// ---------------
// FS_LoadImage
// ===============

public Image FS_LoadImage(int Pos, int SubPos)
{ //ga.DEBUGPaint("FS_LoadImage");
  String FileName = "";  
  Image Img = null;

  //System.out.println("FileName: " + FileName);
  switch (Pos)
  { case 0 : FileName = "/0_Data"; 			      break;
    case 1 : FileName = "/1_Sonido";			    break;
    case 2 : FileName = "/2_Logos";			      break;    
    case 3 : FileName = "/3_Tablero"; 			  break;    
    case 4 : FileName = "/4_NumerosV";		    break;
    case 5 : FileName = "/5_NumerosH";			  break;
    case 6 : FileName = "/6_FichaMarcador";		break;
    case 7 : FileName = "/7_FichasTablero";		break;
    case 8 : FileName = "/8_FichasJugador";		break;
    case 9 : FileName = "/9_Flechas";			    break;
    case 10: FileName = "/10_SombrasFichas";  break;
  }
  //System.out.println("FileName: " + FileName);
  FileName = FileName + "/" + SubPos + ".png";
  //System.out.println("FileName: " + FileName);
  

  try	
  {	Img = Image.createImage(FileName);
  } catch (Exception e) {System.out.println("Error leyendo PNG: "+FileName);}

  
  return (Img);
}

//#endif



// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//

// BiosCanvas integrada en GameCanvas --------------------------------
/*
public void canvasInit() {}
public void Draw(Graphics a) {}
*/
// BiosCanvas integrada en GameCanvas --------------------------------

// **************************************************************************//
// Final Clase GameCanvas
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
// Final Clase BiosCanvas
// **************************************************************************//
//};                                                                        // BiosCanvas integrada dentro de GameCanvas 








// ------------------------------------------------------------------------------------------------------------------------

// public GameCanvas(Game ga) {super(ga);} // BiosCanvas integrada dentro de GameCanvas 

// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//

// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// *******************************************
// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// Terminal: 

// ---------------------------------
//  Bits de Control de cada Terminal
// =================================
final boolean deviceSound = true;		// Terminal con Sonido
//#ifdef VIBRA_ON
  final boolean deviceVibra = true;			// Terminal con Vibracion
//#else	// Terminal sin Vibracion
//#endif
final boolean deviceLight = true;		// Terminal con opciones de Luz en LCD ON/OFF
final boolean deviceMenus = false;		// Terminal con Texto inferior para menus (CommandListener / softKeyMenu)
// ----------------------------------

// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


// *******************
// -------------------
// canvas - Engine
// ===================
// *******************

// -------------------
// canvas Init
// ===================

public void canvasInit()
{
//#ifdef J2ME
  //#ifndef NK
  //#endif
  //canvasFillInit(0);
  
  CanvasSizeX = canvasWidth;
  CanvasSizeY = canvasHeight;
  
  for(int i = 0; i < trackFactor.length; i++) {
	  int factor = canvasWidth + canvasHeight;
	  if(factor > 195 + 200) { factor = 195 + 200; }
	  trackFactor[i] = ((trackFactor[i] * factor) / (176 + 208)) >> 3; 
  }
  gameDraw();
  soundCreate();
  
//#elifdef DOJA
//#endif
}


// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{



	if (ga.playShow) { ga.playShow=false; playDraw(); }



}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************

// -------------------
// play Create Gfx
// ===================

public void playCreate_Gfx()
{
}

// -------------------
// play Destroy Gfx
// ===================

public void playDestroy_Gfx()
{
}

// -------------------
// play Init Gfx
// ===================

public void playInit_Gfx()
{
}

// -------------------
// play Release Gfx
// ===================

public void playRelease_Gfx()
{
}

// -------------------
// play Draw Gfx
// ===================

public void playDraw()
{
	
	/// here we go!!!
	tocaDraw();
}

// <=- <=- <=- <=- <=-


int ScrollX;
int ScrollY;
int CanvasSizeX;// = getWidth();
int CanvasSizeY;// = getHeight();
int ScrollSizeX;// = getWidth();
int ScrollSizeY;// = getHeight();
Graphics LCD_Gfx;

//#ifdef J2ME
//#ifdef NOKIAUI
// ----------------------------------------------------------

DirectGraphics LCD_dGfx = null;

public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int Flip=0;
	if (SizeX < 0) {SizeX*=-1; Flip|=1;}
	if (SizeY < 0) {SizeY*=-1; Flip|=2;}
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-ScrollX,Y+DestY-ScrollY,  Flip);
}

// ----------------------------------------------------------

public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
{
	Frame*=(Trozos*6);

	for (int i=0 ; i<Trozos ; i++)
	{
	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int Flip=0;
	if (SizeX < 0) {SizeX*=-1; Flip|=1;}
	if (SizeY < 0) {SizeY*=-1; Flip|=2;}
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-ScrollX,Y+DestY-ScrollY,  Flip);
	}
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       > CanvasSizeX)
	||	(DestX+SizeX < 0)
	||	(DestY       > CanvasSizeY)
	||	(DestY+SizeY < 0))
	{return;}

	LCD_Gfx.setClip(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
	LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
	return;

	case 2:
	LCD_dGfx.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
	LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip X+Y
	return;
	}
}
//#endif
//#ifdef MIDP20FLIP
//#endif

//#ifndef NOKIAUI

//#endif
//#endif
//#endif



// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//

	TrackGameRenderer trackRender=new TrackGameRenderer();
	
	void tocaDraw() {
		
		LCD_Gfx = scr;
//#ifdef NOKIAUI
		LCD_dGfx = DirectUtils.getDirectGraphics(scr);
		LCD_dGfx.setARGBColor(0xFFFFFFFF);
//#endif
		//scr.setColor(0x00ff00);
		//#ifndef DOJA
		scr.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		//scr.fillRect(0, 0, canvasWidth, canvasHeight);
				
		trackRender.track = ga.track;
		trackRender.gc = this;
		trackRender.render(0, scr, canvasWidth, canvasHeight, ga.myVehicle);
	}


}