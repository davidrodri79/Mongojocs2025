package com.mygdx.mongojocs.sexysnake;// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// Nokia series 40
// ------------------------------------

//#ifdef J2ME

// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

//#ifdef NOKIAUI
public class BiosCanvas extends FullCanvas
//#else
//#endif

//#elifdef DOJA
//#endif
{

// ---------------------------------- //
//  Bits de Control de cada Terminal
// ---------------------------------- //
//final boolean deviceSound = true;
//final boolean deviceVibra = true;
// ---------------------------------- //

Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public BiosCanvas(Game ga)
{
	this.ga = ga;
	canvasCreate();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics scr;

public void paint (Graphics g)
{       
	if (canvasShow)
	{
	canvasShow=false;
	scr = g;

//#ifdef DOJA
//#endif

	try {

	canvasDraw();

	} catch (Exception e) {System.out.println("*** Exception en la parte Grafica ***"); e.printStackTrace();}

//#ifdef DOJA
//#endif

	scr = null;
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw()
{
	canvasShow=true;
	repaint();
//#ifdef J2ME
	serviceRepaints();
//#elifdef DOJA
//#endif
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyMenu, keyX, keyY, keyMisc, keyCode;

//#ifdef J2ME
public void keyPressed(int keycode)
{
	if(ga.gameStatus == (Game.GAME_SHOW_PRON_IMAGE + 1)) {
		ga.gameStatus++;
	}
	
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=keycode;

	switch(getGameAction(keycode))
	{
		case 1:keyY=-1;break;	// Arriba
		case 6:keyY=1;break;	// Abajo
		case 5:keyX=1;break;	// Derecha
		case 2:keyX=-1;break;	// Izquierda
		case 8:keyMenu=2;break;	// Fuego
	}

	switch (keycode)
	{
	case Canvas.KEY_NUM0:
		keyMisc=10;
	break;

	case Canvas.KEY_NUM1:
		keyMisc=1; 
		//keyX=-1; keyY=-1;	//disable diagonals!
//#ifdef NK-3650
//#endif
	break;

	case Canvas.KEY_NUM2:	// Arriba
		keyMisc=2;
//#ifndef NK-3650
//#endif
	break;

	case Canvas.KEY_NUM3:
		keyMisc=3; 
		//keyX=1; keyY=-1;
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
		keyMisc=7; 
		//keyX=-1; keyY= 1;
	break;

	case Canvas.KEY_NUM8:	// Abajo
		keyMisc=8;
//#ifndef NK-3650
//#endif
	break;

	case Canvas.KEY_NUM9:
		keyMisc=9;
		//keyX= 1; keyY= 1;
	break;

	case 35:		// *
		keyMisc=35;
//#ifdef NK-3650
//#endif
	break;

	case 42:		// #
		keyMisc=42;
	break;
	
//#ifdef NOKIA-KEYBOARD
	case -6:	// Nokia - Menu Izquierda
		keyMenu = -1;
		break;

	case -7:	// Nokia - Menu Derecha
		keyMenu = 1;
		break;
//#endif
//#ifdef MO-T720
//#endif
//#ifdef SIEMENS-KEYBOARD
//#endif
//#ifdef MO-V300
//#endif
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

public void showNotify() {ga.gamePaused=false;}

public void hideNotify() {ga.gamePaused=true;}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void showImage(Image Img)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ----------------------------------------------------------

public void showImage(Image Img, int X, int Y)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	if (DestX >= canvasWidth || DestY >= canvasHeight) {return;}

//	scr.setClip(0, 0, canvasWidth, canvasHeight);
//	scr.clipRect(DestX, DestY, SizeX, SizeY);

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
//#endif
	scr.setColor(RGB & 0xffffff);
	scr.fillRect(0,0, canvasWidth,canvasHeight );
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
	canvasTextStr = str;
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
	textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
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
static final int TEXT_BOTTOM =	0x004;
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
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
//#elifdef DOJA
//#endif
	scr.setFont(f);

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()*Str.length) )/2;}
	else
	if ((Mode & TEXT_BOTTOM)!=0) {Y+=  printerSizeY-(f.getHeight()*Str.length) ;}

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

			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

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

// -------------------
// canvas Create
// ===================

public void canvasCreate()
{
	canvasFillInit(0xffffff);
//#ifdef J2ME
	canvasImg = new Image();
	canvasImg._createImage("/Logo0.png");
//#elifdef DOJA
//#endif

	canvasShow = true;
	repaint();
//#ifdef J2ME
	serviceRepaints();
//#endif
}

// -------------------
// canvas Tick
// ===================

public void canvasDraw()
{
	if (canvasFillShow) { canvasFillShow=false; canvasFill(canvasFillRGB); }

	if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }

// ------------------------

	Draw(scr);	// Imprimimos el juego

// ------------------------

	if (ga.menuShow) { ga.menuShow=false; menuDraw(); }
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

//#ifdef NOKIAUI
// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: NOKIAUI - Rev.0 (28.11.2003)
// ===================
// *******************



//#ifdef NOKIAUI
// #endif
//#else

// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: GENERICA - Rev.0 (28.11.2003)
// ===================
// *******************

// -------------------
// Sound Create
// ===================




public void soundCreate()
{
}

// -------------------
// Sound SET
// ===================

public void soundPlay(int Ary, int Loop)
{
}

// -------------------
// Sound RES
// ===================

public void soundStop()
{
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
}

//#endif

//#elifdef DOJA

//#endif

// <=- <=- <=- <=- <=-


//#ifdef DOJA


//#endif



// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//

public void canvasInit() {}
public void Draw(Graphics a) {}

// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//


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
// Final Clase BiosCanvas
// **************************************************************************//
};