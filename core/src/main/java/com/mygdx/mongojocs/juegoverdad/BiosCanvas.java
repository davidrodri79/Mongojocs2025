package com.mygdx.mongojocs.juegoverdad;// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// Nokia series 40
// ------------------------------------


//#ifdef J2ME



import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME
  //#ifdef MO-C450
	//#elifdef NOKIAUI
		//#ifdef FULLSCREEN
			public class BiosCanvas extends FullCanvas
		//#else
		//#endif
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


//#ifdef J2ME

	//#ifdef SMALL_FONT
	//#else
		public static Font font = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );
	//#endif

//#else
//#endif



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
//#ifdef SI-x55
//#endif
	
	if (canvasShow)
	{
		canvasShow = false;
		canvasFinished = false;
		scr = g;

//#ifdef DOJA
//#endif
		try {
			canvasDraw();

		} catch (Exception e) {System.out.println("*** Exception en la parte Grafica ***"); e.printStackTrace();}

//#ifdef DOJA
//#endif

		scr = null;
		canvasFinished = true;
	}
//#ifdef SI-x55
//#endif
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#if VI && J2ME
//#endif


public void gameDraw()
{
	canvasShow = true;
	repaint();
//#ifdef J2ME
	serviceRepaints();
	while (canvasFinished == false) { try {Thread.sleep(2);} catch(Exception e) {} }
//#elifdef DOJA
//#endif

//#if VI && J2ME
//#endif
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyMenu, keyX, keyY, keyMisc, keyCode;
int xPressed = 0;
int yPressed = 0;
int fPressed = 0;
int skPressed = 0;

//#ifdef J2ME
public void keyPressed(int keycode)
{
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=keycode;

	switch(getGameAction(keycode))
	{
		case 1:keyY=-1;break;	// Arriba
		case 6:keyY=1;break;	// Abajo
		case 5:keyX=1;break;	// Derecha
		case 2:keyX=-1;break;	// Izquierda
		case 8:keyMenu=2;break;	// Fuego
//#ifdef VI-TSM100
//#endif
//#ifdef VI-TSM100v
//#endif
	}

	switch (keycode)
	{
		
		
//#ifndef NK-3650

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
		keyMisc=5; keyMenu=2;
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

	
	
//#else //NK-3650
//#endif
	
	
	//#ifdef NK
		case -6:	// Nokia - Menu Izquierda
			keyMenu = -1;
		break;
	
		case -7:	// Nokia - Menu Derecha
			keyMenu = 1;
		break;
	//#elifdef SE
	//#elifdef MO-T720
	//#elifdef MO-V3xx
	//#elifdef MO-C650
	//#elifdef SI
	//#elifdef AL-OT756
	//#elifdef LG-U8150
	//#elifdef SG-Z105
	//#endif
	}
	
	xPressed += keyX;
	yPressed += keyY;
	fPressed += ((keyMenu==2)?1:0);
}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void keyReleased(int keycode)
{
	skPressed = (( keyMenu==-1 || keyMenu==1 )?keyMenu:0 );
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

//public void showNotify() {ga.gamePaused=false;}

//public void hideNotify() {ga.gamePaused=true;}

public void showNotify() { 
	ga.gamePaused=false;
	if (ga.biosStatus == 22) {
		ga.menuShow=true;
		ga.menuListShow=true;
		//soundPlay(5, 0);
	}
}

public void hideNotify() {
	ga.gamePaused=true;
	soundStop();
} 

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
	//#ifdef SI
	if (DestX >= canvasWidth || DestY >= canvasHeight || (DestX + SizeX) <= 0 || (DestY + SizeY) <= 0) {return;}
	if (DestX < 0) {SourX+=-DestX; SizeX+=DestX; DestX=0;}
	if (DestY < 0) {SourY+=-DestY; SizeY+=DestY; DestY=0;}
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


public Image[] loadImage(String FileName, int Frames)
{
	Image Img[] = new Image[Frames];
	for (int i=0 ; i<Frames ; i++) {Img[i] = loadImage(FileName+i+".png");}
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
//#ifdef J2ME
	scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif //J2ME

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
	textDraw(_textoBreak(Str, printerSizeX, font ), X, Y, RGB, Mode);
//#elifdef DOJA
//#endif
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode) {
/*
//#ifdef J2ME
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
//#elifdef DOJA
	Font f = Font.getFont(Font.FACE_PROPORTIONAL | ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) | ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
//#endif
*/
	Font f = font;
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
	
	if (texto == null) return new String[0];
	
	while ( posOld < texto.length())
	{
		size = 0;

		pos = posIni;

		while ( size < width )
		{
			if ( pos >= texto.length() ) { posOld = pos; break; }

			int dat = texto.charAt(pos++);

			if ( (dat == '\\') && (pos < texto.length()) && (texto.charAt(pos) == 'n') ) { 
				posOld = pos;	// si encontramos un salto de linea, salimos
				break;
			} 

			if ( dat==0x20 ) { posOld = pos - 1; }
//#ifdef J2ME
			size += font._charWidth((char)dat);
//#elifdef DOJA
//#endif
	    }

		if ( (posOld - posIni) <= 1 ) {
			while ( pos < texto.length() && texto.charAt(pos) >= 0x30 && (!((pos > 0) && (texto.charAt(pos) == 'n') && (texto.charAt(pos - 1) == '\\') || pos == 0) ) ) { pos++; }
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

		if ( ((posEnd - posIni) >= 1) && (posEnd < texto.length()) && (texto.charAt(posEnd) == 'n') && (texto.charAt(posEnd - 1) == '\\') ) {
			str[i] = texto.substring(posIni, posEnd-1);
		} else {
			str[i] = texto.substring(posIni, posEnd);
		}
		
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

int canvasWidth = getWidth();
int canvasHeight = getHeight();

int canvasFillRGB;
boolean canvasFillShow = false;
Image canvasImg;

boolean canvasShow = true;
boolean canvasFinished = true;

// -------------------
// canvas Create
// ===================

public void canvasCreate()
{
//#ifdef J2ME
  	//#ifndef NOKIAUI
	//#endif
//#endif

//#ifdef J2ME
	canvasFillInit(0xffffff);
	canvasImg = new Image();
	canvasImg._createImage("/Logo0.png");
	gameDraw();
//#ifdef SI-C65
//#endif
	//#ifdef MIDP20
	canvasWidth = getWidth();
	canvasHeight = getHeight();
	//#endif
//#endif


//#ifdef MO-C650
//#elifdef MO-V3xx
//#elifdef AL-OT756
//#elifdef SI-CX65
//#elifdef SI-C65
//#elifdef LG-U8150
//#elifdef SE-Z1010
//#elifdef NK-6600
//#elifdef VI-TSM6

//#endif

	canvasTextCreate();
}

// -------------------
// canvas Tick
// ===================

public void canvasDraw()
{
	if (canvasFillShow) { canvasFillShow=false; canvasFill(canvasFillRGB); }

	if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }

// ------------------------

	Draw(scr);	// Imprimimos el juego

// ------------------------

	if (ga.menuShow) { ga.menuShow=false; menuDraw(); }
	
	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }
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
		
		public void soundCreate() {
			Sonidos = new Player[8];
			
			Sonidos[0] = SoundCargar("/6_0_accion.mid");
			Sonidos[1] = SoundCargar("/6_1_prenda.mid");
			Sonidos[2] = SoundCargar("/6_2_pregunta.mid");
			Sonidos[3] = SoundCargar("/6_3_pasar.mid");
			Sonidos[4] = SoundCargar("/6_4_perder.mid");
			Sonidos[5] = SoundCargar("/6_5_main.mid");
			Sonidos[6] = SoundCargar("/6_6_ganar.mid");
			Sonidos[7] = SoundCargar("/6_7_fallo.mid");
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
		
		public void soundPlay(int Ary, int Loop) {
			
			if (ga.gameSound) {
				if (Loop<1) {Loop=-1;}
			
				soundStop();
		
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
			if (ga.gameVibra)
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
	//#elifdef PLAYER_SIEMENS_MIDI_STOP
	//#elifdef PLAYER_TSM6
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
}

