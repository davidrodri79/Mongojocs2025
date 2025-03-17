package com.mygdx.mongojocs.frozenworld;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================
// Nokia series 40
// ------------------------------------



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

public class BiosCanvas extends FullCanvas
{

// ---------------------------------- //
//  Bits de Control de cada Terminal
// ---------------------------------- //
//final boolean deviceSound = true;
//final boolean deviceVibra = true;
// ---------------------------------- //
DirectGraphics dgr;
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
	scr = g;		
	dgr = DirectUtils.getDirectGraphics(scr);
		
	if (canvasShow)
	{
		canvasShow=false;
		canvasDraw();
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw()
{
	canvasShow=true;
	repaint();
	serviceRepaints();
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyMenu, keyX, keyY, keyMisc, keyCode;

public void keyPressed(int keycode)
{
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=keycode;


    if (ga.gameControl)
    {
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
        		keyMisc=1; keyX=-1; keyY=-1;
        	break;
        
        	case Canvas.KEY_NUM2:	// Arriba
        		keyMisc=2; keyY=-1;
        	break;
        
        	case Canvas.KEY_NUM3:
        		keyMisc=3; keyX= 1; keyY=-1;
        	break;
        
        	case Canvas.KEY_NUM4:	// Izquierda
        		keyMisc=4; keyX=-1;
        	break;
        
        	case Canvas.KEY_NUM5:	// Disparo
        		keyMisc=5;
        		keyMenu=2;
        	break;
        
        	case Canvas.KEY_NUM6:	// Derecha
        		keyMisc=6; keyX=1;
        	break;
        
        	case Canvas.KEY_NUM7:
        		keyMisc=7; keyX=-1; keyY= 1;
        	break;
        
        	case Canvas.KEY_NUM8:	// Abajo
        		keyMisc=8; keyY=1;
        	break;
        
        	case Canvas.KEY_NUM9:
        		keyMisc=9; keyX= 1; keyY= 1;
        	break;
        
        	case 35:		// *
        		keyMisc=35;
        	break;
        
        	case 42:		// #
        		keyMisc=42;
        	    break;
            // -----------------------------------------
	        case -6:	// Nokia - Menu Izquierda
		        keyMenu = -1;
	            break;

           	case -7:	// Nokia - Menu Derecha
		        keyMenu = 1;
	            break;        	
        }
    }
    else
    {
        
        switch (keycode)
    	{
        
            case Canvas.KEY_NUM1:
		        keyMisc=1; keyX=-1; keyY=-1;
        	    return;
        
        	case Canvas.KEY_NUM0:
        		keyX= 1; keyY=-1;
        	    return;
        
        	case Canvas.KEY_NUM2:	// Izquierda
        		keyMisc=2; keyX=-1;
        	    return;
        
        	case Canvas.KEY_NUM5:	// Abajo
        		keyMisc=5; keyY= 1;
        	    return;
        	
        	case Canvas.KEY_NUM6:	// Abajo
        		keyMisc=6; keyY= 1;
        	    return;
        
        
        	case Canvas.KEY_NUM4:	// Disparo
        		keyMisc=4;
        		keyMenu=2;
        	    return;
        	case Canvas.KEY_NUM7:	// Disparo
        		keyMisc=7;
        		keyMenu=2;
        	    return;
        
        	case Canvas.KEY_NUM9:	// Derecha
        		keyMisc=9; keyX=1;
        	    return;
        
        	case Canvas.KEY_NUM3:
        		keyMisc=3; keyX=-1; keyY= 1;
        	    return;
        
        	case Canvas.KEY_NUM8:
        		keyMisc=8; keyX= 1; keyY= 1;
        	    return;
                
        	case 35:		// *
        		//keyMisc=35;
        		keyMisc=10;
        	    return;
        
        	case 42:		// #
        		//keyMisc=42;
        		keyMisc=10;
        	    return;
      // -----------------------------------------
    	    case -6:	// Nokia - Menu Izquierda
    		    keyMenu = -1;
    	        return;
        
    	    case -7:	// Nokia - Menu Derecha
        		keyMenu = 1;
    	        return;
        }
    
    	switch(getGameAction(keycode))
    	{
    		case 1:keyY=-1;break;	// Arriba
    		case 6:keyY=1;break;	// Abajo
    		case 5:keyX=1;break;	// Derecha
    		case 2:keyX=-1;break;	// Izquierda
    		case 8:keyMenu=2;break;	// Fuego
    	}
    
        
    }
// =========================================
/*
// -----------------------------------------
	case -20:	// Motorola T720 - Menu Izquierda
		keyMenu=-1;
	break;

	case -21:	// Motorola T720 - Menu Derecha
		keyMenu= 1;
	break;
// =========================================

// -----------------------------------------
	case -1:	// Siemens - Menu Izquierda
		keyMenu=-1;
	break;

	case -4:	// Siemens - Menu Derecha
		keyMenu= 1;
	break;
// =========================================

// -----------------------------------------
	case 21:	// Motorola V300 - Menu Izquierda
		KeybM=-1;
	break;

	case 22:	// Motorola V300 - Menu Derecha
		KeybM= 1;
	break;
// =========================================
*/
//	}

}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=0;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void showNotify() {ga.gamePaused=false;}

public void hideNotify() {ga.gamePaused=true;
if (ga.gameStatus == ga.GAME_PLAY+2) ga.gameStatus = ga.GAME_MENU_SECOND;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

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

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// -------------------
// canvas Fill
// ===================

public void canvasFill(int RGB)
{
	scr.setClip (0,0, canvasWidth,canvasHeight );
	scr.setColor(RGB);
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
static final int TEXT_BOTTON =	0x004;
static final int TEXT_VCENTER =	0x008;

static final int TEXT_OUTLINE =	0x1000;

// -------------------
// text Draw
// ===================

public void textDraw(String Str, int X, int Y, int RGB, int Mode)
{
	textDraw(_textoBreak(Str, printerSizeX, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
	scr.setFont(f);    
	scr.setClip(0,0,canvasWidth,canvasHeight);
	

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
		scr.drawString(Str[i],  X-1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY , 20);
		}

		scr.setColor(RGB);
		//scr.setClip(0, 0, canvasWidth, canvasHeight); //ZNR
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
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
				size += f._charWidth((char)dat);
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



public String[] textoBreak(String texto, int width, Font f)
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
			size += f.charWidth((char)dat);
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
	canvasFillInit(0x000000);
	canvasImg = new Image();
	canvasImg._createImage("/Loading.png");

	canvasShow = true;
	repaint();
	serviceRepaints();
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

	if (ga.menuShow) { ga.menuShow=false; menuDraw(); }

	Draw(scr);
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

// *******************
// -------------------
// Sound - Engine
// ===================
// *******************

static Player[] Sonidos;

static int SoundOld=-1;
static int SoundTime;


public void soundCreate()
{

	//try	{ Thread.sleep(20); } catch(java.lang.InterruptedException e) {}

	Sonidos = new Player[11];
	scr.setClip(0, 0, canvasWidth, canvasHeight);
	scr.setColor(0xff8000);	    
	scr.drawRoundRect(20-2,140-2, (11*12)+4, 20+4,8, 8);
	repaint();
    for(int i = 0; i < Sonidos.length;i++)
    {
	    Sonidos[i] = SoundCargar("/"+i+".mid");	
	    scr.setClip(0, 0, canvasWidth, canvasHeight);
	    scr.setColor(0xff8000);	    	
	    scr.fillRoundRect(20,140, ((i+1)*12), 20, 8, 8);
	    repaint();
	}
	//try	{ Thread.sleep(20); } catch(java.lang.InterruptedException e) {}

}

public Player SoundCargar(String Nombre)
{


	Player p = null;

	try
	{
	   //InputStream stream = getClass().getResourceAsStream(Nombre);
		p = Manager.createPlayer( Nombre ,"audio/midi");
		p.realize();
		p.prefetch();	
	}
	catch(Exception ex) {ex.printStackTrace();}

	return p;
}



// -------------------
// Sound SET
// ===================

int stemps;

public void soundPlay(int Ary, int Loop)
{
        if (!ga.gameSound) {return;}
    	
		if (Loop==0) {Loop--;}

		if (SoundOld!=-1) {soundStop();}

		try
		{
    		VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
    		if (Sonidos[Ary] == null) return;
    		Sonidos[Ary].setLoopCount(Loop);
    		Sonidos[Ary].start();
		}
		catch(Exception exception) {exception.printStackTrace();}

		SoundOld=Ary;
	
}


void vibraInit(int microsegundos)
{
}

// -------------------
// Sound RES
// ===================

public void soundStop()
{

	try
	{
	Sonidos[SoundOld].stop();
	}
	catch(Exception exception) {exception.printStackTrace();}

	SoundOld=-1;

}



// -------------------
// Sound RUN
// ===================

public void soundTick()
{
	//if (SoundOld!=-1 && --SoundTime==0) {SoundRES();}
}








// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//

public void canvasInit() {}
public void Draw(Graphics a) {}

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
};