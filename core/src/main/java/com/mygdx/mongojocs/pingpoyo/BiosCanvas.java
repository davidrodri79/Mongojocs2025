package com.mygdx.mongojocs.pingpoyo;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================
// Nokia series 40
// ------------------------------------


//import javax.microedition.lcdui.*;

//import com.nokia.mid.ui.*;		// clase CUSTOM de NOKIA
//import com.nokia.mid.sound.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

//import javax.microedition.media.*;
//import javax.microedition.media.control.*;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.sanfermines2006.Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

public class BiosCanvas extends FullCanvas
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

	switch(keycode)
	{
		case -3  : keyX=-1; break;
		case -4  : keyX=1; break;
		case -1  : keyY=-1; break;
		case -2  : keyY=1; break;
		case -5  : keyMenu=2; break; 
	}

	switch (keycode)
	{
	case Canvas.KEY_NUM0:
		keyMisc=10; if(!ga.gameKeyconf) {keyX=1; keyY=-1;}
	break;

	case Canvas.KEY_NUM1:
		keyMisc=1; if(!ga.gameKeyconf) {keyX=-1; keyY=-1;} else {keyX=-1; keyY=-1;}
	break;

	case Canvas.KEY_NUM2:	// Arriba
		keyMisc=2; if(!ga.gameKeyconf) {keyX=-1;} else {keyY=-1;} 
	break;

	case Canvas.KEY_NUM3:
		keyMisc=3; if(!ga.gameKeyconf){keyX=-1;} else {keyX= 1; keyY=-1;}
	break;

	case Canvas.KEY_NUM4:	// Izquierda
		keyMisc=4; if(!ga.gameKeyconf) {keyX=-1; keyY=1;} else {keyX=-1;}
	break;

	case Canvas.KEY_NUM5:	// Disparo
		keyMisc=5; if(!ga.gameKeyconf) {keyY=1;} else {keyMenu=2;}
	break;

	case Canvas.KEY_NUM6:	// Derecha
		keyMisc=6; if(!ga.gameKeyconf) {keyY=1;} else {keyX=1;}
	break;

	case Canvas.KEY_NUM7:
		keyMisc=7; if(!ga.gameKeyconf) {keyY=1; keyX=1;} else {keyX=-1; keyY= 1;}
	break;

	case Canvas.KEY_NUM8:	// Abajo
		keyMisc=8; if(!ga.gameKeyconf) {keyX=1;} else {keyY=1;}
	break;

	case Canvas.KEY_NUM9:
		keyMisc=9; if(!ga.gameKeyconf) {keyX=1;} else {keyX= 1; keyY= 1;}
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
	}

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

public void hideNotify() {ga.gamePaused=true; ga.backFromCall=true;}

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

	//try	{
		Img = Image.createImage(FileName);
	//} catch (java.io.IOException e) {System.out.println("Error leyendo PNG: "+FileName);}

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
static final int TEXT_SHADED =	0x2000;

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

		//if((Y+despY < printerY + printerSizeX) && (Y+despY+f.getHeight() > printerY)){

			if ((Mode & TEXT_OUTLINE)!=0)
			{
			scr.setColor(0);
			scr.drawString(Str[i],  X-1+despX, Y-1+despY , 20);
			scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
			scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
			scr.drawString(Str[i],  X+1+despX, Y+1+despY , 20);
			}else if ((Mode & TEXT_SHADED)!=0){
			scr.setColor(0);
			scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
			scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);		
			}
	
			scr.setColor(RGB);
			scr.drawString(Str[i],  X+despX, Y+despY , 20);
		//}
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
	canvasFillInit(0xffffff);

	canvasImg = new Image();
	canvasImg._createImage("/Clock.png");

	canvasShow = true;
	repaint();
	serviceRepaints();
}

// -------------------
// canvas Tick
// ===================

public void canvasDraw()
{
	if (canvasFillShow) { /*canvasFillShow=false; MONGOFIX*/ canvasFill(canvasFillRGB); }

	if (canvasImg!=null) { showImage(canvasImg); /*canvasImg=null; MONGOFIX*/ System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }

// ------------------------

	Draw(scr);

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
	scr.setClip(0,0,176,208);
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
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: GENERICA - Rev.0 (28.11.2003)
// ===================
// *******************

//static javax.microedition.media.Player snd[];
	Music snd[];


// -------------------
// Sound INI
// ===================

public void soundCreate()
{
		snd = new Music[8];
		
		for(int i = 0; i<snd.length; i++)
			snd[i] = LoadMidi(i+".mid");
		
}

// -------------------
// Sound SET
// ===================

public void soundPlay(int Ary, int Loop)
{
	try{
		
			soundStop();
				
			if(!ga.gameSound) return;				
			
			if(Ary>=0){
				/*VolumeControl vc = (VolumeControl) snd[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(90);}
				snd[Ary].setLoopCount((Loop > 0 ? Loop : 99));			
				snd[Ary].start();*/
				snd[Ary].setLooping( Loop == 0);
				snd[Ary].play();
			}
			
		}catch(Exception exception) {exception.printStackTrace();} 

}

// -------------------
// Sound RES
// ===================

public void soundStop()
{
	try{
		
		for(int i=0; i<snd.length; i++)
			snd[i].stop();
									
	}catch(Exception exception) {exception.printStackTrace();}
}

// -------------------
// Sound RUN
// ===================

public void soundTick()
{
}

/*public javax.microedition.media.Player LoadMidi(String Name)
	{

		javax.microedition.media.Player p = null;
	
		try
		{
			InputStream is = getClass().getResourceAsStream(Name);
			p = Manager.createPlayer( is , "audio/midi");
			p.realize();
			p.prefetch();
		}
		catch(Exception exception) {exception.printStackTrace();}
	
		return p;
	} */


	public Music LoadMidi(String Name)
	{
		return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Name));
	}


public void vibraInit(int Time)
{

	try{
		if(ga.gameVibra)
			DeviceControl.startVibra(100,Time);
			
	}catch (java.lang.Exception e) {}

}


// <=- <=- <=- <=- <=-


// **************************************************************************//
// Inicio Clase com.mygdx.mongojocs.bravewar.com.mygdx.mongojocs.sanfermines2006.GameCanvas
// **************************************************************************//

public void canvasInit() {}
public void Draw(Graphics a) {}

// **************************************************************************//
// Final Clase com.mygdx.mongojocs.bravewar.com.mygdx.mongojocs.sanfermines2006.GameCanvas
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