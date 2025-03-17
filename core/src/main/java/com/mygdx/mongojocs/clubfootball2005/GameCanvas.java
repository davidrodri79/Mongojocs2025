
package com.mygdx.mongojocs.clubfootball2005;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// Nokia series 40
// ------------------------------------


//#ifdef J2ME

	
//#endif

// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

//#ifdef J2ME
	//#ifdef NOKIAUI
	public class GameCanvas extends FullCanvas
	//#elifdef MOTOROLAUI
	//#else
	//#endif
	
	//#ifdef CLISTENER
	//#endif
//#endif

//#ifdef DOJA
//#endif
{

// ---------------------------------- //
//  Bits de Control de cada Terminal
// ---------------------------------- //
//final boolean deviceSound = true;
//final boolean deviceVibra = true;
// ---------------------------------- //

Game ga;

public boolean doRepaintMenu = false, altShirt = false, paintLock = false;
Font myFont[];

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

// [Frame][Direction]

//#ifndef IMAGE_SHIRTS
//#endif
	
public static int heads[][][]/*{
	{{0,0},{0,0}},
	{{0,0},{0,0}},
	{{0,0},{0,0}},
	{{0,0},{0,0}},
	{{0,-5},{3,-5}},
	{{0,0},{0,0}}
	
}*/;

public static byte customTextWidth[]
   /*{4,2,4,3,3,4,4,4,4,4,1,3,4,2,4,3,
	4,4,4,4,4,4,4,4,4,1,4,4,3,5,4,4,
	4,4,4,4,5,4,5,5,4,5,4,1,5,1,2,1}*/;

public GameCanvas(Game ga)
{
	this.ga = ga;
						
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


public void startupTables()
{
	//Table init
	byte aux[];
	
	
	//#ifndef IMAGE_SHIRTS
	//#endif
		
	//#ifdef J2ME
	aux = ga.loadFile("/heads.dat");
	//#endif
	//#ifdef DOJA
	//#endif
	
	heads = new int[6][2][2];
	
	for(int i = 0; i<6; i++)
	for(int j = 0; j<2; j++)
	for(int k = 0; k<2; k++)
		heads[i][j][k] = aux[4*i + 2*j + k];
		
	//#ifdef J2ME
	aux = ga.loadFile("/font.dat");
	//#endif
	//#ifdef DOJA
	//#endif
	
	customTextWidth = new byte[48];
	
	for(int i = 0; i<48; i++)	
		customTextWidth[i] = aux[i];
		
	aux = null;	

	System.gc();
}


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics scr;

public void paint (Graphics g)
{       
	
	if (canvasShow)
	{
		
		//#ifdef NOKIAUI
		try{			
				DeviceControl.setLights(0,100);
		}catch(java.lang.IllegalArgumentException e) {} 		
		//#endif
		
		//#ifdef SIEMENSUI
		//#endif
		
		//#ifdef SAMSUNGUI
		//#endif
		
		//#ifdef DOJA
		//#endif
				
		canvasShow=false;
		scr = g;
		canvasDraw();
		
		//#ifdef DOJA
		//#endif
		
		scr = null;

	}
		
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw()
{
	soundTick();

	//#ifdef J2ME
	if(forceRefresh) {forceRefresh = false; ga.biosRefresh();} 
	//#endif
	
	canvasShow=true;
	repaint();
	
	//#ifdef J2ME
	serviceRepaints();
	//#endif
	
	//#ifdef DOJA
	//#endif
	
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyMenu, keyX, keyY, keyMisc, keyCode;

//#ifdef J2ME
public void keyPressed(int keycode)
{
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=keycode;
	
	//#ifndef SE	
	switch(getGameAction(keycode))
	{
		//#ifndef NK-s60		
		case 1:keyY=-1;break;	// Arriba
		case 6:keyY=1;break;	// Abajo
		case 5:keyX=1;break;	// Derecha
		case 2:keyX=-1;break;	// Izquierda		
		case 8:keyMenu=2;break;	// Fuego		
		//#endif
			
		//#ifdef VI-TSM100
		//#endif		
	}
	//#endif
				
	switch (keycode)
	{
	//#ifdef NK-s60
	//#else		
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
	//#endif
	
	// -----------------------------------------
	//#ifdef NOKIAUI
	case -6:	// Nokia - Menu Izquierda
		keyMenu = -1;
	break;
	
	case -7:	// Nokia - Menu Derecha
		keyMenu = 1;
	break;
	//#endif
			
	//#ifdef SIEMENSKEYS
	//#endif
		
	//#ifdef MO-C65x
	//#endif
	
	//#ifdef MO-E1000
	//#endif
		
	//#ifdef MO-C450
	//#endif
		
	//#ifdef MO-V3xx
	//#endif
	
	//#ifdef AL-756
	//#endif 
	
	//#ifdef SG-z105
	//#endif
	
	//#ifdef LG-u8150
	//#endif
	
	//#ifdef SE-k500i
	//#endif
	
	//#ifdef SG-X450
	//#endif
	
	//#ifdef SG-E600
	//#endif
	
	//#ifdef SG-D410
	//#endif
	
	}	
		
	//#ifdef SE
	//#endif
						
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	keyMenu = keyX = keyY = keyMisc = keyCode = 0;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean forceRefresh = false;

public void showNotify() {
	ga.gamePaused=false; 
	ga.backFromCall=true;
	forceRefresh = true;	

}

public void hideNotify() {ga.gamePaused=true; soundStop(); }
//#endif


//#ifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void setClip(int x, int y, int sx, int sy)
{
	//#ifdef J2ME 
	scr.setClip(x,y,sx,sy);	
	//#endif
}

public void clipRect(int x, int y, int sx, int sy)
{
	//#ifdef J2ME 
	scr.clipRect(x,y,sx,sy);	
	//#endif
}

public void setColor(int r, int g, int b)
{
	//#ifdef J2ME
	scr.setColor(r,g,b);
	//#endif	
	//#ifdef DOJA
	//#endif	
}

public void showImage(Image Img)
{		
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
	setClip(0,0,canvasWidth,canvasHeight);
}

// ----------------------------------------------------------

/*public void showImage(Image Img, int X, int Y)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}*/

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	if (DestX >= canvasWidth || DestY >= canvasHeight || Sour==null) {return;}

//	setClip(0, 0, canvasWidth, canvasHeight);
//	clipRect(DestX, DestY, SizeX, SizeY);

	if(DestX < 0 ) {SizeX += DestX; SourX -= DestX; DestX = 0;}
	if(DestY < 0 ) {SizeY += DestY; SourY -= DestY; DestY = 0;}
	if(DestX + SizeX >= canvasWidth) SizeX = canvasWidth - DestX;
	
	setClip(DestX, DestY, SizeX, SizeY);
	clipRect(DestX, DestY, SizeX, SizeY);
	
	//#ifdef J2ME
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	//#endif
	
	//#ifdef DOJA
	//#endif

}

// ----------------------------------------------------------
/*
public void showImage(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
		
	//#ifdef J2ME
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	//#endif
	
	//#ifdef DOJA
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY);
	//#endif	
}*/

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	if(Sour == null) return;
	
	setClip(TopX, TopY, FinX, FinY);
	clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	
	//#ifdef J2ME
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
	//#endif
	
	//#ifdef DOJA
	//#endif
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// -------------------
// canvas Fill
// ===================

public void canvasFill(int RGB)
{
	setClip (0,0, canvasWidth,canvasHeight );
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

//#ifdef J2ME
public Image loadImage(String FileName)
{
	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e) {/*System.out.println("Error leyendo PNG: "+FileName);*/}

	System.gc();
	return Img;
}
//#endif

//#ifdef DOJA
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
int canvasTextType;
boolean canvasTextShow = false;



//#ifndef SMALLDISPLAY
int printerX = 10;
int printerSizeX = getWidth() - 20;
//#else
//#endif
int printerY = 0;
int printerSizeY = getHeight();

// -------------------
// canvasText Create
// ===================
/*
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
*/
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
	canvasTextType = 0;
	canvasTextShow = true;
}

// -------------------
// canvasText Draw
// ===================

/*public void canvasTextDraw()
{
	textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
}*/

// <=- <=- <=- <=- <=-



// *********************
// ---------------------
// text - Engine - Gfx
// =====================
// *********************

/*static final int TEXT_PLAIN =	0x000;
static final int TEXT_BOLD =	0x010;

static final int TEXT_SMALL =	0x000;
static final int TEXT_MEDIUM =	0x100;
static final int TEXT_LARGER =	0x200;*/

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

public void textDraw(String Str, int X, int Y, int RGB, int Mode, int type)
{
	
	textDraw(_textoBreak(Str, printerSizeX, myFont[type]), X, Y, RGB, Mode, type);
}

public void textDrawFast(String Str, int X, int Y, int RGB, int Mode, int type)
{
	
	textDraw(new String[] {Str}, X, Y, RGB, Mode, type);		
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode, int type)
{
	
	Font f = myFont[type];	
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
		//scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
		//scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY , 20);
		//#endif
		
		//#ifdef DOJA
		//#endif
		}

		scr.setColor(RGB);
		//#ifdef J2ME
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
		//#endif
		
		//#ifdef DOJA
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

boolean canvasShow = false;

// -------------------
// canvas Create
// ===================
/*
public void canvasCreate()
{
	canvasFillInit(0xffffff);
	//canvasImg = loadImage("/loading.png");

	canvasShow = true;
	repaint();
	
	//#ifdef J2ME
	serviceRepaints();
	//#endif
}*/

// -------------------
// canvas Tick
// ===================

public void canvasDraw()
{
	if (canvasFillShow) { canvasFillShow=false; canvasFill(canvasFillRGB); }

	if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; //canvasTextDraw(); 
		if(canvasTextStr != null) textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode, canvasTextType);
		}

// ------------------------

	Draw(scr);


	if (ga.menuShow) { ga.menuShow=false; 
	//menuDraw(); 
		ga.menuListDraw(scr);
	}



 
}


// <=- <=- <=- <=- <=-




// *******************
// -------------------
// menu - Engine - Gfx
// ===================
// *******************
/*
public void menuDraw()
{

	ga.menuListDraw(scr);

}*/

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

// -------------------
// Sound INI
// ===================

//#ifdef OTASOUND
//#endif

//#ifdef SIEMENSUI
//#elifdef MIDP20SOUND
static Player snd[];
//#endif

//#ifdef VI-TSM6
//#endif

//#ifdef MOTOROLAUI
//#endif

//#ifdef VODAFONEUI
//#endif

//#ifdef SAMSUNGUI
//#endif

//#ifdef DOJA
//#endif

int soundOld = -1;


//#ifdef J2ME
public void soundCreate()
//#endif

//#ifdef DOJA
//#endif
{		
		//#ifdef NOINGAMESOUND
		//#else
		int soundsNumber = 7;
		//#endif
	
		//#ifdef J2ME
			//#ifdef OTASOUND
			//#endif
			
			//#ifdef SIEMENSUI
			//#elifdef MIDP20SOUND						
			snd = new Player[soundsNumber];
			
			for(int i = 0; i<snd.length; i++)
				snd[i] = LoadMidi(i+".mid");
			//#endif
			
			//#ifdef VI-TSM6
			//#endif
			
			//#ifdef MOTOROLAUI

			//#endif		
		//#endif
		
		//#ifdef DOJA
		//#endif		
		
		//#ifdef VODAFONEUI
		//#endif
		
		//#ifdef SAMSUNGUI
		//#endif

}

// -------------------
// Sound SET
// ===================

public void soundPlay(int Ary, int Loop)
{
				
	
	soundStop();
	
	//#ifdef J2ME
		//#ifdef OTASOUND
		//#endif 		
		
		//#ifdef SIEMENSUI
		//#elifdef MIDP20SOUND		
		try{
					
			if(!ga.gameSound) return;								
					
			if(Ary>=0 && Ary<snd.length){			
				//#ifdef NOMIDIPREFETCH
				//#endif
				VolumeControl vc = (VolumeControl) snd[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(100);}
				snd[Ary].setLoopCount(( Loop > 0 ? Loop : 99));									
				snd[Ary].start();
				soundOld = Ary;
			}
				
		}catch(Exception exception) {exception.printStackTrace();} 	
		//#endif
		
		
		//#ifdef VI-TSM6
		//#endif	
		
		//#ifdef MOTOROLAUI
		//#endif	
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	//#ifdef VODAFONEUI
	//#endif
	
	//#ifdef SAMSUNGUI
	//#endif	
		
}

// -------------------
// Sound RES
// ===================

public void soundStop()
{
		
	//#ifdef J2ME
		//#ifdef OTASOUND
		//#endif
	
		//#ifdef SIEMENSUI
		//#elifdef MIDP20SOUND	
		try{
			if(soundOld != -1){
				snd[soundOld].stop();			
			}
		}catch(Exception exception) {exception.printStackTrace();} 							
		//#endif
		
		//#ifdef VI-TSM6
				//#endif
		
		//#ifdef MOTOROLAUI
			//#endif
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	//#ifdef VODAFONEUI
	//#endif
	
	//#ifdef SAMSUNGUI
	//#endif
	
}

// -------------------
// Sound RUN
// ===================

public void soundTick()
{
	
	//#ifdef VODAFONEUI
	//#endif	
	
	//#ifdef DOJA
	//#endif
		
}

// -------------------
// Vibra SET
// ===================

//#ifdef DOJA
//#endif

public void vibraInit(int Time)
{
	//#ifdef DEVICEVIBRA
	
	//#ifdef J2ME
		//#ifdef NOKIAUI
		try{
			if(ga.gameVibra)
			DeviceControl.startVibra(100,Time);	
				
		}catch (java.lang.Exception e) {}
		//#endif
		
		//#ifdef SIEMENSUI
		//#elifdef MIDP20
		//#endif
		
		//#ifdef VI-TSM6
		//#endif
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	//#ifdef VODAFONEUI
	//#endif
	
	
	//#ifdef SAMSUNGUI
	//#endif
		
	//#endif
}

//#ifdef OTASOUND
//#endif

//#ifdef SIEMENSUI
//#elifdef MIDP20SOUND

public Player LoadMidi(String Name)
	{
	
		Player p = null;
	
		try
		{
			InputStream is = getClass().getResourceAsStream(Name);
			p = Manager.createPlayer( Name , "audio/midi");
			p.realize();			
			//#ifndef NOMIDIPREFETCH
			p.prefetch();
			//#endif
		}
		catch(Exception exception) {exception.printStackTrace();}
	
		return p;
	}
//#endif

//#ifdef VI-TSM6
//#endif


//#ifdef MOTOROLAUI
//#endif


//#ifdef SAMSUNGUI
//#endif


//#ifdef DOJA
//#endif

//#ifdef DOJA
//#endif

/*======================================================================
	Flip images
  ======================================================================*/
  
//#ifndef MIDP20FLIP
//#ifdef NOKIAUI
public void showImageFlip(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       > canvasWidth)
	||	(DestX+SizeX < 0)
	||	(DestY       > canvasHeight)
	||	(DestY+SizeY < 0))
	{return;}

	setClip(0, 0, canvasWidth, canvasHeight);
	
	DirectGraphics dg = DirectUtils.getDirectGraphics(scr);

	switch (Flip)
	{
	case 0:
	clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
	clipRect(DestX, DestY, SizeX, SizeY);
	dg.drawImage(Sour, (DestX)-(Sour.getWidth()-SizeX-SourX), DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
	return;

	case 2:
	clipRect(DestX, DestY, SizeX, SizeY);
	dg.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
	clipRect(DestX, DestY, SizeX, SizeY);
	dg.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
	return;

	case 4:
	clipRect(DestX, DestY, SizeY, SizeX);
	dg.drawImage(Sour, (DestX)-SourY, DestY-(Sour.getWidth()-SourX-SizeX), Graphics.LEFT|Graphics.TOP, 90);	// Rotar 90
	return;

	case 5:
	clipRect(DestX, DestY, SizeY, SizeX);
	dg.drawImage(Sour, (DestX)-(Sour.getHeight()-SourY-SizeY), (DestY)-SourX, Graphics.LEFT|Graphics.TOP, 270);	// Rotar 270
	return;
	}
}
//#endif
//#elifdef MIDP20
//#endif


// <=- <=- <=- <=- <=-

//#ifdef CLISTENER
//#endif







// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//
		
	//#ifdef J2ME
	Image grassImg, playerImg, blackImg, headImg, ballImg, flagsImg, clockImg, upGoalImg, downGoalImg, borderHImg, borderVImg, numbersImg, patternImg, cupImg, fntImg, pictureImg;
	//#endif	
	
	//#ifdef DOJA
	//#endif
	
	byte playerCoo[], ledBmp[];	
	
	//#ifdef GFX_BIG
		//#ifdef J2ME
		Image shirtImg, domesticShirtImg, panelImg, bigFlagsImg, fieldImg;
		byte shirtCoo[], domesticShirtCoo[];
		//#endif
		
		//#ifdef DOJA
		//#endif
	//#elifdef USEBIGFLAGS
	//#endif
	
	boolean invLed=false;
	int camX, camY, ledFr=0, fontHeight;
	
	
	public void loadMatchGfx()
	{
		//#ifdef J2ME
			//#ifndef INITIALLOAD
			//#endif
			
		//#endif
		
		//#ifdef DOJA
		//#endif
		
		/*int t1, t2;
		
		if(ga.shirtData[ga.myTeam][0][2] > ga.shirtData[ga.oppTeam][0][0] && ga.shirtData[ga.myTeam][0][2] > ga.shirtData[ga.myTeam][0][1])
		t1 = 0;
		else if(ga.shirtData[ga.myTeam][0][1] > ga.shirtData[ga.myTeam][0][0] && ga.shirtData[ga.myTeam][0][1] > ga.shirtData[ga.myTeam][0][2])
		t1 = 1;
		else t1 = 2;
	
		if(ga.shirtData[ga.oppTeam][1][2] > ga.shirtData[ga.oppTeam][1][0] && ga.shirtData[ga.oppTeam][1][2] > ga.shirtData[ga.oppTeam][1][1])
		t2 = 0;
		else if(ga.shirtData[ga.oppTeam][1][1] > ga.shirtData[ga.oppTeam][1][0] && ga.shirtData[ga.oppTeam][1][1] > ga.shirtData[ga.oppTeam][1][2])
		t2 = 1;
		else t2 = 2;	
	
		altShirt = (t1 == t2);*/
		
		int dif1, dif2;
		
		dif1 = Math.abs((ga.shirtData[ga.myTeam][0][0]) - (ga.shirtData[ga.oppTeam][0][0])) +
			   Math.abs((ga.shirtData[ga.myTeam][0][1]) - (ga.shirtData[ga.oppTeam][0][1])) +
			   Math.abs((ga.shirtData[ga.myTeam][0][2]) - (ga.shirtData[ga.oppTeam][0][2]));
			   
		dif2 = Math.abs((ga.shirtData[ga.myTeam][0][0]) - (ga.shirtData[ga.oppTeam][2][0])) +
			   Math.abs((ga.shirtData[ga.myTeam][0][1]) - (ga.shirtData[ga.oppTeam][2][1])) +
			   Math.abs((ga.shirtData[ga.myTeam][0][2]) - (ga.shirtData[ga.oppTeam][2][2]));
			 
		altShirt = (dif2 > dif1);
					   
		//System.out.println (dif1+" VS "+dif2+(altShirt ? "Elige la segunda camiseta" : "Mantiene la primera"));
						
	}
	
	public void destroyMatchGfx()
	{
		//#ifndef INITIALLOAD
		//#endif
		
		System.gc();		
	}
	
	public void loadCupGfx()
	{
		//#ifdef J2ME
			//#ifndef INITIALLOAD
			//#endif
		//#endif
		
		//#ifdef DOJA
		//#endif
	}

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
//#ifdef DEVICESOUND
final boolean deviceSound = true;
//#else
//#endif

//#ifdef DEVICEVIBRA
final boolean deviceVibra = true;
//#else
//#endif


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
	Image auxImg = loadImage("/loadClock.png");
	canvasFillInit(0);
	canvasImg = auxImg; canvasShow = true;

	gameDraw();
	
		//#ifdef SI-x55
		//#endif

	//#endif
	
		
	//#ifdef MIDP20
		//#ifdef FULLSCREEN
		setFullScreenMode(true);
		//#endif
	//#endif
		
	canvasWidth=getWidth();
	canvasHeight=getHeight();
	
	//#ifdef MO-C65x
	//#endif
	
	//#ifdef MO-C450

	//#endif
	
	//#ifdef MO-V3xx
	//#endif
	
	//#ifdef AL-756
	//#endif
	
	//#ifdef SG-z105
	//#endif
	
	//#ifdef LG-u8150
	//#endif
	
	//#ifdef MO-E1000
	//#endif
	
	//#ifdef SE-k500i
	//#endif
	
	//#ifdef SG-X450
	//#endif
	
	//#ifdef SG-E600
	//#endif
	
	//#ifdef SG-D410
	//#endif
					
		
	//#ifdef J2ME
	printerSizeY = canvasHeight;
	//#endif
	
	myFont = new Font[2];
	
	//#ifdef J2ME
		//#ifdef SINGLEFONT
		//#else
			//#ifdef BIGFONT
			//#else
			myFont[0] = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_SMALL );
			myFont[1] = Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM );	
			//#endif
		//#endif
	//#endif
	
	//#ifdef DOJA
	//#endif
		
	
	//#ifdef J2ME
	fontHeight = myFont[0].getHeight() + 2;
	//#endif
	//#ifdef DOJA
	//#endif
								
	
	//#ifdef J2ME
	flagsImg = loadImage("/flags.png");
	patternImg = loadImage("/pattern.png");
	fntImg = loadImage("/smallfont.png");
					
		//#ifdef GFX_BIG	
		bigFlagsImg = loadImage("/bigFlags.png");
		//#elifdef USEBIGFLAGS
		bigFlagsImg = loadImage("/bigFlags.png");
		//#endif
	//#endif
				
	//#ifdef INITIALLOAD				
		//#ifndef NOGRASSIMAGE
		grassImg = loadImage("/grass.png");
		//#endif
		playerImg = loadImage("/player.png");
		blackImg = loadImage("/blackplayer.png");
		headImg = loadImage("/head.png");
		ballImg = loadImage("/ball.png");
			
		clockImg = loadImage("/clock.png");
		numbersImg = loadImage("/numbers.png");
		upGoalImg = loadImage("/upGoal.png");
		downGoalImg = loadImage("/downGoal.png");
			
		borderHImg = loadImage("/borderh.png");
		borderVImg = loadImage("/borderv.png");
			
		ledBmp = new byte[4050];
		ga.loadFile(ledBmp,"/led.raw");		
	
		//#ifdef GFX_BIG				
		shirtImg = loadImage("/shirts.png");
		domesticShirtImg = loadImage("/domesticShirts.png");
		panelImg = loadImage("/panel.png");
			
		shirtCoo = new byte[5760];
		ga.loadFile(shirtCoo,"/shirt.coo");
		domesticShirtCoo = new byte[5760];
		ga.loadFile(domesticShirtCoo,"/domesticShirt.coo");
		
		//#else
		//#endif
			
		cupImg = loadImage("/cup.png");
			
		pictureImg = loadImage("/Caratula.png");
			
	//#endif
			
	//#ifdef J2ME
	soundCreate();
	//#endif
	
	//#ifdef CLISTENER
	//#endif
	
	//#ifdef DOJA
	//#endif	
	
}


// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{
	int x, y, formation[][];
	String s, txt;
	
	if (ga.playShow){
	
	 	ga.playShow=false; 
	 	
		switch(ga.gameStatus)
			{
				case Game.GAME_NEW_LEAGUE :	
				showPattern();				
				s = (ga.teamNames[ga.myTeam].length() < 14 ? ga.teamNames[ga.myTeam] : ga.abreviate(ga.teamNames[ga.myTeam]));				
				//#ifdef GFX_BIG				
				showBigFlag(ga.myTeam,canvasWidth - 47,15);
				setColor(255,255,255);
				setClip(0,0,canvasWidth,canvasHeight);								
				textDrawFast(s, canvasWidth/8, (canvasHeight/5),(ga.subMenuOption == 0 ? 0xFFFF00 : 0x808080), TEXT_OUTLINE , 1);
				textDrawFast(ga.gameText[33][(ga.wholeLeague ? 1 : 0)], canvasWidth/8, (2*canvasHeight/5), (ga.subMenuOption == 1 ? 0xFFFF00 : 0x808080), TEXT_OUTLINE , 1);
				textDrawFast(ga.gameText[18][ga.gameDuration], canvasWidth/8, (3*canvasHeight/5), (ga.subMenuOption == 2 ? 0xFFFF00 : 0x808080), TEXT_OUTLINE , 1);								
				textDrawFast(ga.gameText[26][0], 5, canvasHeight-fontHeight, 0xFFFF00, TEXT_OUTLINE, 0);								
				textDrawFast(ga.gameText[26][1], canvasWidth-30, canvasHeight-fontHeight, 0xFFFF00, TEXT_OUTLINE, 0);								
				//#else
				//#endif
				break;			
				
				case Game.INGAME_CHANGES :
				case Game.GAME_ALIGNMENT :
				showPattern();				
				//#ifdef GFX_BIG			
				x = (canvasWidth - 160)/2; y = (canvasHeight - 177)/2;
				setClip(0,0,canvasWidth,canvasHeight);	
				
				drawMiniField(canvasWidth/2,y+37,ga.currentPlayer,false);
				
				setClip(0,0,canvasWidth,canvasHeight);				
				setColor(0,128,0);
				scr.fillRect(x+8,y+58,64,90);
				
				if(ga.gameStatus == Game.INGAME_CHANGES)
					textDrawFast("("+ga.changesLeft+")", 4, 4, 0xFFFF00, TEXT_OUTLINE, 0);									
				
				textDraw((ga.subMenuOption == 0 ? "< " : "")+ga.gameText[17][ga.myFormation]+(ga.subMenuOption == 0 ? " >" : ""), 0, y+2, (ga.subMenuOption == 0 ? 0xFFFF00 : 0x808080), TEXT_HCENTER | TEXT_OUTLINE, 0);									
				
				for(int i = 0; i<11; i++){
					
					if(i == ga.currentPlayer)
					{
						setClip(0,0,canvasWidth,canvasHeight);
						setColor(255,255,255);
						scr.drawRect(x+8,y+58+8*i,64,8);
					}
					
					if(ga.chooseNewPlayer)
					if(i == ga.chosenPlayer)
					{
						setClip(0,0,canvasWidth,canvasHeight);
						setColor(255,0,0);
						scr.drawRect(x+8,y+58+8*i,64,8);
					}
					
					int plId = ga.tour.myAlignment[i];
					txt = (plId < 100 ? ga.playerNames[ga.myTeam][plId] : ga.customPlayerNames[plId - 100]);
					customText((txt.length()<=13 ? txt : txt.substring(0,11)),x+10,y+60+8*i);
				}
				
				for(int i = 0; i<ga.tour.teamSize - 11; i++){
					
					int plId = ga.tour.myAlignment[i+11];
					
					if(ga.gameStatus == Game.INGAME_CHANGES)
					if(plId == ga.changesList[0] || plId == ga.changesList[1] || plId == ga.changesList[2])
					{
						setClip(0,0,canvasWidth,canvasHeight);
						setColor(30,30,30);
						scr.fillRect(x+78,y+58+8*i,64,8);						
					}
					
					
					if(ga.chooseNewPlayer)
					if(i == ga.chosenPlayer - 11)
					{
						setClip(0,0,canvasWidth,canvasHeight);
						setColor(255,0,0);
						scr.drawRect(x+78,y+58+8*i,64,8);
					}
																			
					txt = (plId < 100 ? ga.playerNames[ga.myTeam][plId] : ga.customPlayerNames[plId - 100]);
					customText((txt.length()<=13 ? txt : txt.substring(0,11)),x+80,y+60+8*i);										
				}
				
				setClip(0,0,canvasWidth,canvasHeight);	
				
				//#ifndef NOCOMMANDTITLES
				if(ga.chooseNewPlayer)
					textDrawFast(ga.gameText[38][0], 5, canvasHeight-fontHeight, 0xFFFF00, TEXT_OUTLINE, 0);												
					
				else textDrawFast(ga.gameText[26][0], 5, canvasHeight-fontHeight, 0xFFFF00, TEXT_OUTLINE, 0);
				//#endif
				
				//#else
				//#endif								
				break;
				
				case Game.GAME_VERSUS :
				showPattern();				
				//#ifdef GFX_BIG								
				showBigFlag(ga.myTeam,canvasWidth/2 - 52, 3*canvasHeight/5 - 10);
				showBigFlag(ga.oppTeam,canvasWidth/2 + 20, 3*canvasHeight/5 - 10);
				setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[20][0]+" "+(ga.tour.current+1), 0, canvasHeight/6, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 1);
				textDraw(ga.teamNames[ga.myTeam], 0, 2*canvasHeight/5, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 1);
				textDrawFast("VS", 0, -2 + (3*canvasHeight/5), 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 1);
				textDraw(ga.teamNames[ga.oppTeam], 0, 4*canvasHeight/5, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 1);								
				//#else
				// #endif
				break;
								
				/*case Game.GAME_VIEW_TOURNAMENT :				
				showPattern();
				showTournament(ga.tour,true);
				setClip(0,0,canvasWidth,canvasHeight);
				//#ifdef GFX_BIG								
				textDraw(ga.gameText[20][ga.tour.current-1], 0, 10, 0xFFFFFF, TEXT_HCENTER | TEXT_BOLD);								
				//#else				
				textDraw(ga.gameText[20][ga.tour.current-1], 0, 10, 0xFFFFFF, TEXT_HCENTER);								
				//#endif				
				break;
				
				case Game.GAME_CURRENT_TOURNAMENT :				
				showPattern();
				showTournament(ga.tour,false);
				setClip(0,0,canvasWidth,canvasHeight);
				//#ifdef GFX_BIG								
				textDraw(ga.gameText[20][ga.tour.current-1], 0, 10, 0xFFFFFF, TEXT_HCENTER | TEXT_BOLD);								
				//#else				
				textDraw(ga.gameText[20][ga.tour.current-1], 0, 10, 0xFFFFFF, TEXT_HCENTER);								
				//#endif				
				break;*/
				
				case Game.GAME_CHOOSE_STRATEGY :				
				showPattern();								
				//#ifdef GFX_BIG			
				drawMiniField(canvasWidth/2,canvasHeight/2,-1,true);									
				textDrawFast("< "+ga.gameText[17][ga.myFormation]+" >", 0, 6*(canvasHeight/7), 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 1);				
				//#else
				// #endif
				break;
				
				case Game.GAME_PLAY :
				
				playDraw();
				
				break;				
				
				case Game.GAME_HALFTIME_WAIT :				
				//#ifdef REPAINT_BKG
				//#endif												
				int fr[]={-1,0,1,2,3,2,3,2,3,4,-1,-1,-1};
				showLed(fr[ledFr%fr.length],invLed);
				//if(invLed) invLed=false; else invLed=true;
				ledFr++;
				break;
				
				case Game.GAME_GOAL_WAIT :								
				//#ifdef REPAINT_BKG
				//#endif												
				showLed(5+(ledFr%5),invLed);				
				ledFr++;
				break;
				
				/*case Game.GAME_PENALTIES_WAIT :								
				showLed(10+(ledFr > 7 ? 7 : ledFr),invLed);				
				ledFr++;
				break;*/
				
				case Game.GAME_ENDGAME_WAIT :
				//#ifdef REPAINT_BKG
				//#endif												
				showLed(18+(ledFr > 5 ? 5 : ledFr),invLed); 				
				ledFr++;
				break;
				
				case Game.GAME_CUP_WON :
				showPattern();
				showImage(cupImg,0,0,cupImg.getWidth(),cupImg.getHeight(),5+(canvasWidth-cupImg.getWidth())/2,(canvasHeight-cupImg.getHeight())/2);
				setClip(0,0,canvasWidth,canvasHeight);
				//#ifdef GFX_BIG
				textDraw(ga.gameText[22][0], 0, canvasHeight/10, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 0);				
				textDraw(ga.teamNames[ga.tour.rank[0][0]], 0, 7*canvasHeight/10, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 0);				
				textDraw(ga.gameText[22][1], 0, 8*canvasHeight/10, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 0);				
				//#else
				//#endif
				break;
			}
		}

	if(doRepaintMenu){
		switch(ga.gameStatus)
		{
			case Game.GAME_MENU_SECOND :
			playDraw();
			break;
						
			case Game.GAME_SEE_RANK :												
			case Game.GAME_SEE_RESULTS :
			showPattern();				
			break;
			
			case Game.GAME_MENU_LEAGUE :
			showPattern();			
			setClip(0,0,canvasWidth,canvasHeight);			
			//#ifdef GFX_BIG
			textDraw(ga.gameText[35][(ga.tour.isDomestic ? 0 : 1)]+", "+ga.gameText[20][0]+" "+(ga.tour.current+1), 0, canvasHeight/6, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE , 1);																		
			showBigFlag(ga.myTeam,canvasWidth/2 - 16,canvasHeight/2 - 16);
			//#else
			//#endif
			break;

			case Game.GAME_CUSTOMIZE_PLAYER :
				showPattern();
				setClip(0,0,canvasWidth,canvasHeight);
				if(ga.menuType == ga.MENU_CUSTOMIZE_PLAYER){

					//#ifdef J2ME
						//#ifdef NOBLACKMEN
						//#else
					if(ga.menuListPos<3){
						//#endif
					//#endif
					//#ifdef DOJA
					//#endif

								//#ifdef J2ME
								//#ifdef NOGRASSIMAGE
								//#else
								showImage(grassImg,0,16,40,40,canvasWidth/2 - 20, canvasHeight/2 -20);
								//#endif
								//#endif
								//#ifdef DOJA
								//#endif
								camX = - canvasWidth/2; camY = - canvasHeight/2;
								showPlayer((ga.tour.isDomestic ? ga.headLineTeamDom : ga.headLineTeam),ga.dummy,0);
								setClip(0,0,canvasWidth,canvasHeight);
								textDraw(ga.customPlayerNames[ga.currentPlayer], 0, canvasHeight/5, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 0);
							}else{

								//#ifdef SMALLDISPLAY
								//#else

								textDrawFast("("+ga.customPlayerUsedPoints(ga.currentPlayer)+"/30)", 0, (canvasHeight/8),(0xFFFF00), TEXT_HCENTER | TEXT_OUTLINE, 0);

								textDrawFast(ga.gameText[31][3] + (ga.customPlayerData[ga.currentPlayer][2]+1), canvasWidth/10, (2*canvasHeight/8),(0xFFFF00), TEXT_OUTLINE, 0);
								textDrawFast(ga.gameText[31][4] + (ga.customPlayerData[ga.currentPlayer][3]+1), canvasWidth/10, (3*canvasHeight/8),(0xFFFF00), TEXT_OUTLINE, 0);
								textDrawFast(ga.gameText[31][5] + (ga.customPlayerData[ga.currentPlayer][4]+1), canvasWidth/10, (4*canvasHeight/8),(0xFFFF00), TEXT_OUTLINE, 0);
								textDrawFast(ga.gameText[31][6] + (ga.customPlayerData[ga.currentPlayer][5]+1), canvasWidth/10, (5*canvasHeight/8),(0xFFFF00), TEXT_OUTLINE, 0);
								//#endif
							}

						}else
							textDraw(ga.gameText[16][0], 0, canvasHeight/5, 0xFFFF00, TEXT_HCENTER | TEXT_OUTLINE, 0);
						break;
		}
		setClip(0,0,canvasWidth,canvasHeight);
		doRepaintMenu = false;
	}
						
}

// <=- <=- <=- <=- <=-

void drawMiniField(int x, int y, int highlight, boolean big)
{
	int formation[][];
	
	formation = ga.formation[ga.myFormation];				
	setClip(0,0,canvasWidth,canvasHeight);			
		
	if(big){	
		// 72x96
		x-=96/2; y-=72/2;
		setColor(0,200,0);
		scr.fillRect(x,y,96,72);
		setColor(255,255,255);
		scr.drawRect(x,y,96,72);
		scr.drawLine(x+48,y,x+48,y+72);
		//#ifdef J2ME
		scr.drawArc(x+42,y+30,12,12,0,360);
		//#endif
		scr.drawRect(x+96-6,y+30,6,12);
		scr.drawRect(x+96-16,y+18,16,36);
		scr.drawRect(x,y+30,6,12);
		scr.drawRect(x,y+18,16,36);
		setColor(255,0,0);
		for(int i=0; i<formation.length; i++){
			if(highlight-1 == i) setColor(255,255,0);
			else setColor(255,0,0);
			//#ifdef J2ME
			scr.fillArc(x+20+(20*formation[i][0]),y+10+(8*formation[i][1]),4,4,0,360);
			//#endif
			//#ifdef DOJA
			//#endif
		}
		if(highlight == 0) setColor(255,255,0);
		else setColor(255,0,0);
		//#ifdef J2ME
		scr.fillArc(x+4,y+34,4,4,0,360);
		//#endif
		//#ifdef DOJA
		//#endif
		
	}else{
	
		// 36x48											
		x-=48/2; y-=36/2;
		setColor(0,200,0);
		scr.fillRect(x,y,48,36);
		setColor(255,255,255);
		scr.drawRect(x,y,48,36);
		scr.drawRect(x,y+13,6,10);
		scr.drawRect(x+48-6,y+13,6,10);
		scr.drawLine(x+24,y,x+24,y+36);
		//#ifdef J2ME
		scr.drawArc(x+21,y+15,6,6,0,360);	
		//#endif
		for(int i=0; i<formation.length; i++){
			if(highlight-1 == i) setColor(255,255,0);
			else setColor(255,0,0);
			scr.fillRect(x+10+(10*formation[i][0]),y+5+(4*formation[i][1]),2,2);	
		}
		if(highlight == 0) setColor(255,255,0);
		else setColor(255,0,0);
		scr.fillRect(x+2,y+17,2,2);
	}
}








// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************

// -------------------
// play Create Gfx
// ===================

/*public void playCreate_Gfx()
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
}*/

// -------------------
// play Draw Gfx
// ===================

public void playDraw()
{
	//#ifdef SPEEDTEST
    ga.time3 = System.currentTimeMillis();
    //#endif
    	
	int x, y;
			
	camX = (XScale*ga.cameraX)/2 - canvasWidth/2;
	camY = (YScale*ga.cameraY)/2 - canvasHeight/2;
			
	if(camX<-24) camX=-24;
	if(camY<-24) camY=-24;
	if(camX+canvasWidth>(XScale*90)+24) camX=(XScale*90)+24-canvasWidth;
	if(camY+canvasHeight>(XScale*120)+24) camY=(YScale*120)+24-canvasHeight;
				
	showField();
	
	//#ifdef GFX_BIG
	for(int i=0; i<10; i++){
		showPlayerShadow(ga.teamA[i]);
		showPlayerShadow(ga.teamB[i]);
	}
	showPlayerShadow(ga.gkA);
	showPlayerShadow(ga.gkB);	
	//#endif
	
	
	showBall(ga.ball);
	
	for(int i=0; i<10; i++){
		
		showPlayer(ga.myTeam,ga.teamA[i],0);
		showPlayer(ga.oppTeam,ga.teamB[i],(altShirt ? 1 : 0));		
	}		
		
	showPlayer(ga.myTeam,ga.gkA,1);
	
	showPlayer(ga.oppTeam,ga.gkB,(altShirt ? 0 : 1));
	

	//GOALS
	//#ifdef GFX_BIG
	if(YScale*23 - camY > 0)	
		showImage(upGoalImg,0,0,101,36,(XScale*45) - 50 - camX , (YScale*0) - 36 - camY);
	
	if(YScale*97 - camY < canvasHeight)
		showImage(downGoalImg,0,0,101,36,(XScale*45) - 50 - camX , (YScale*120) - 4 - camY);			
	//#else
	// #endif
	
		
	setClip(0,0,canvasWidth,canvasHeight);	
	setColor(255,255,0);
	
	if(ga.teamDown == ga.TEAMA){
		x = (XScale*ga.pointer/2)-camX-5;
		y = (YScale*0/2)-camY;
		if(y>-50) {
			if(y<0) y = 2;
			//#ifdef J2ME
				//#ifdef GFX_BIG
				showImage(numbersImg,36,12,9,10,x,y); 
				//#else
				//#endif
			//#endif
			//#ifdef DOJA
			//#endif
		}
	}else{
		
		x = (XScale*ga.pointer/2)-camX-5;
		y = (YScale*120)-camY;
		if(y<canvasHeight + 50) {
			if(y>canvasHeight) y = canvasHeight - 10;		
			//#ifdef J2ME
				//#ifdef GFX_BIG
				showImage(numbersImg,45,12,9,10,x,y);
				//#else
				//#endif
			//#endif
			//#ifdef DOJA
			//#endif
		}
	}
					
	// SCORES & FLAGS	
	//#ifdef J2ME		
		//#ifdef GFX_BIG
		
			//#ifndef SLOWDEVICE
				showImage(panelImg,0,0,32,21,4,canvasHeight-25);
				showImage(panelImg,32,0,32,21,canvasWidth-36,canvasHeight-25);		
				showFlag(ga.myTeam, 8, canvasHeight - 19);
				showFlag(ga.oppTeam, canvasWidth - 20, canvasHeight - 19);
			//#endif		
				
			showImage(numbersImg,9*(ga.goalsA%10),0,9,12,23,canvasHeight - 20);							
			showImage(numbersImg,9*(ga.goalsB%10),0,9,12,canvasWidth - 32,canvasHeight - 20);	
						
			showImage(clockImg,19*(ga.matchTime/(ga.durationTicks()/7)),0,19,22,5,5);	
			
		//#else			
			
		//#endif
		
	//#endif
	
	
	//#ifdef DOJA
	//#endif

			
	//#ifndef SLOWDEVICE
	
		//#ifdef GFX_BIG		
		x = (canvasWidth - 85)/2;
		setClip(0,0,canvasWidth,canvasHeight);
		setColor(64,64,64);
		scr.fillRect(x,canvasHeight - 17,85,7);
		setColor(128,128,128);
		scr.drawRect(x-1,canvasHeight - 18,86,8);
		
		if (ga.ball.theOwner != null)
			customText(ga.ball.theOwner.name,x+1,canvasHeight - 16);
		//#else
		//#endif
	//#endif
	
	
	//#ifdef SPEEDTEST
    /*ga.time4 = System.currentTimeMillis() - ga.time3;
    scr.setClip(0,0,canvasWidth,canvasHeight);
    textDraw(ga.time2+"/"+ga.time4, 0, 15, 0xFFFFFF, TEXT_HCENTER, 0);*/
    //#endif
   		
}

// <=- <=- <=- <=- <=-

public void PutSprite(Image Img,  int X, int Y,  int CuadSize, int Frame, byte[] Coor, int Trozos, boolean inv)
	{
		Frame*=(Trozos*6);
	
		for (int i=0 ; i<Trozos ; i++)
		{
		int DestX=Coor[Frame++];
		int DestY=Coor[Frame++];
		int SizeX=Coor[Frame++];
		if (SizeX==0) {return;}
		int SizeY=Coor[Frame++];
		int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
		int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}
	
		//ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-sc.ScrollX,Y+DestY-sc.ScrollY);
		if(!inv) showImage(Img,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY);
		else	 showImage(Img,SourX+128,SourY+128,SizeX,SizeY,X+CuadSize-SizeX-DestX,Y+DestY);		
		}
	}


// My functions

public static final int FIELDW = 96;
public static final int FIELDH = 128;

//#ifdef GFX_BIG
public static final int XScale = 6;
public static final int YScale = 6;
//#else
//#endif


void showField()
{
	int x, y;
	
	x = (64+camX)%64;
	y = (64+camY)%64;
	
	
	//#ifdef NOGRASSIMAGE
	//#else
	
		//#ifdef GFX_BIG
		for(int i = 0; i<((canvasWidth-1)/128)+1; i++)
		for(int j = 0; j<(canvasHeight/64)+2; j++)		
		//#else
		//#endif		
			showImage(grassImg,0,0,192,64,(192*i)-x%64,(64*j)-y%64);		
		
	//#endif
		
		
	setClip(0,0,canvasWidth,canvasHeight);
	setColor(255,255,255);
	
	
	//#ifdef J2ME
	scr.translate(-camX,-camY);
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	scr.drawRect((XScale*0),(YScale*0),(XScale*90),(YScale*120));
	
	
	// Field center
	if(YScale*51 - camY < canvasHeight && YScale*69 - camY > 0){
		scr.drawLine((XScale*0),(YScale*60),(XScale*90),(YScale*60));
		//#ifdef J2ME			
		scr.drawArc((XScale*36),(YScale*51),(XScale*18),(YScale*18),0,360);			
		//#endif
		//#ifdef DOJA
		//#endif
		scr.fillRect((XScale*45)-1,(YScale*60)-1,3,3);
		//System.out.println("Center");
	}
	
	// North Area
	if(YScale*23 - camY > 0){
		scr.drawRect((XScale*34),(YScale*0),(XScale*22),(YScale*5));
		scr.drawRect((XScale*25),(YScale*0),(XScale*40),(YScale*20));
	
		scr.fillRect((XScale*45),(YScale*15),1,1);
	
		//#ifdef J2ME	
			//#ifndef NOAREACIRCLES
			//scr.drawArc((XScale*31),(YScale*-4)+1,(XScale*27),(YScale*27),229,80);				
				scr.setClip(XScale*25,YScale*20,XScale*40,YScale*20);
				scr.drawArc((XScale*31),(YScale*-4)+1,(XScale*27),(YScale*27),0,360);
				scr.setClip(0,0,canvasWidth,canvasHeight);				
			//#endif
		//#endif
		//#ifdef DOJA
		//#endif
		
		// Corners
		scr.drawLine((XScale*0),(YScale*2),(XScale*2),(YScale*0));
		scr.drawLine((XScale*88),(YScale*0),(XScale*90),(YScale*2));
		//System.out.println("North");
	}
	
	// South Area
	if(YScale*97 - camY < canvasHeight){
		scr.drawRect((XScale*36),(YScale*115),(XScale*18),(YScale*5));
		scr.drawRect((XScale*25),(YScale*100),(XScale*40),(YScale*20));
	
		scr.fillRect((XScale*45),(YScale*105),1,1);
	
		//#ifdef J2ME
			//#ifndef NOAREACIRCLES				
				scr.setClip(XScale*25,YScale*80,XScale*40,YScale*20);
				scr.drawArc((XScale*31),(YScale*97)-1,(XScale*27),(YScale*27),0,360);
				scr.setClip(0,0,canvasWidth,canvasHeight);				
			//#endif
		//#endif
		//#ifdef DOJA
		//#endif
				
		// Corners
		scr.drawLine((XScale*0),(YScale*118),(XScale*2),(YScale*120));
		scr.drawLine((XScale*88),(YScale*120),(XScale*90),(YScale*118));
		//System.out.println("South");
	}
		
	//#ifdef J2ME
	scr.translate(camX,camY);
	//#endif
	
	//#ifdef DOJA
	//#endif
					
}

void showBall(ball b)
{
	int fr=0;
		
	fr=Math.abs((( b.direction==2 || b.direction==6 ? b.sx : b.sy) * b.timing)/60)%3;
	
	//#ifdef J2ME
		setClip(0,0,canvasWidth,canvasHeight);	
		setColor(80,108,45);
		//#ifdef GFX_BIG
			//#ifndef SLOWDEVICE
			scr.fillArc(b.x*3-camX-3,b.y*3-camY-3,6,6,0,360);
			//#endif
		showImage(ballImg,8*fr,8*b.direction,8,8,b.x*3-camX-4,b.y*3-camY-4-(b.h>>9));
		//#else		
		//#endif
	//#endif
	
	//#ifdef DOJA
	//#endif
			
}

void showPlayer(int team, ball sp, int set)
{
	//#ifdef GFX_BIG
	int x=3*sp.x-camX, y=3*sp.y-camY;
	int x2=x-14, y2=y-14, x3, y3, type=0;	
	//#else
	//#endif
	
	//#ifndef IMAGE_SHIRTS
	//#endif
	int bx, by, sx, sy, fr=1, dir=sp.direction, hx=0, hy=0;
	
			
	setClip(0,0,canvasWidth,canvasHeight);
	
	if(sp == ga.humanSoccerPlayer)			
	{
			//#ifdef J2ME
				setColor(255,255,0);
				//#ifdef GFX_BIG
				scr.drawArc(x2,y2,27,27,0,360);	
				//#else
				//#endif
			//#endif
			
			//#ifdef DOJA
			//#endif
	}
		
	if(ga.humanSoccerPlayer != null)
	if(ga.humanSoccerPlayer.state == soccerPlayer.OWNBALL)
	if(sp == ga.humanSoccerPlayer.pass)
		if(x2>canvasWidth || x2<-8 || y2>canvasHeight || y2<-8)
		{
			x3 = x2; y3 = y2;
			if(x2<0) {x3 = 2; type = 1;}
			if(x2>canvasWidth) {x3 = canvasWidth - 10; type = 3;}
			if(y2<0) {y3 = 2; type = 2;}
			if(y2>canvasHeight) {y3 = canvasHeight - 10; type = 0;}
			
			//#ifdef J2ME
				//#ifdef GFX_BIG
				showImage(numbersImg,9*type,12,9,10,x3,y3);
				//#else
				//#endif						
			//#endif						
			
			//#ifdef DOJA
			//#endif
			
		}else{
		
			//#ifdef J2ME
				//#ifdef GFX_BIG
				showImage(numbersImg,0,12,9,10,x2+4,y2-10);			
				//#else
				//#endif
			//#endif
			
			//#ifdef DOJA
			//#endif
		}		
	
	if(x2>canvasWidth || y2>canvasHeight || x2<-18 || y2<-18) return;
	
	if(sp.state == soccerPlayer.STEAL) fr = 3;
	else if(sp.state == goalKeeper.PALOMITA) fr = 4;
	else if(sp.state == soccerPlayer.WAIT){
		if(sp.waitReason == soccerPlayer.W_STEAL) fr = 4;
		else if(sp.waitReason == soccerPlayer.W_STEALFAILED) fr = 3;
		else if(sp.waitReason == soccerPlayer.W_OUT){fr = 5; showBall(ga.ball);}
		else fr = 5;
	}else if(sp.sx == 0 && sp.sy == 0 ) fr = 1;
	else switch((sp.timing/2)%4)
	{
		case 0 : fr=0; break;
		case 3 :
		case 1 : fr=1; break;
		case 2 : fr=2; break;			
	}
		
	//dir=7; fr=3;
				
	setClip(0,0,canvasWidth,canvasHeight);
	
	//#ifdef IMAGE_SHIRTS
	switch(dir){
			default: hx = heads[fr][0][0];
					 hy = heads[fr][0][1];
					 break;
					 
			case 1 : hx = heads[fr][1][0];
					 hy = heads[fr][1][1];
					 break;					 

			case 2 : hx = -heads[fr][0][1];
					 hy = heads[fr][0][0];
					 break;					 
					 
			case 3 : hx = -heads[fr][1][1];
					 hy = heads[fr][1][0];
					 break;					 					 
					 
			case 4 : hx = -heads[fr][0][0];
					 hy = -heads[fr][0][1];
					 break;					 

			case 5 : hx = -heads[fr][1][0];
					 hy = -heads[fr][1][1];
					 break;			
					 
			case 6 : hx = heads[fr][0][1];
					 hy = -heads[fr][0][0];
					 break;					 		 					 
					 
			case 7 : hx = heads[fr][1][1];
					 hy = -heads[fr][1][0];
					 break;					 		 					 					 			
		}
	
	//#else
	//#endif			
	
	//#ifdef J2ME
		//#ifdef GFX_BIG			
		PutSprite((ga.tour.isDomestic ? domesticShirtImg : shirtImg) ,  x-14, y-14,  27, (dir%2) + (team*2) + (ga.tour.teamsNumber*2*fr) + (ga.tour.teamsNumber*12*set), 2, (ga.tour.isDomestic ? domesticShirtCoo : shirtCoo), (dir%8)/2);		
		int aux[]={0,5,3,4};
		showImageFlip(sp.playerSkin>0 ? blackImg : playerImg, (dir%2)*27,fr*27,27,27,x-14,y-14,aux[(dir%8)/2]);
		showImage(headImg,8*sp.playerHead,0,8,7,x-4+((3*hx)/2),y-3+((3*hy)/2));
		//#else	
		//#endif
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	if(sp.state == soccerPlayer.WAIT && sp.waitReason == soccerPlayer.W_OUT) showBall(ga.ball);
}

void showFlag(int id, int x, int y)
{
	
	/*if(ga.tour.isDomestic)
	{
		if(id == ga.headLineTeamDom) id = ga.headLineTeam; else id = 14;		
	}*/
	
	
	
	id = ga.teamLogos[id];
	
	if(id>=0)
	//#ifdef J2ME		
	showImage(flagsImg,12*(id%4),12*(id/4),12,12,x,y);	
	//#endif
	//#ifdef DOJA
	//#endif
	else{
		setClip(0,0,canvasWidth,canvasHeight);
		scr.setColor(0);
		scr.fillRect(x,y,12,12);
	}
			
}

//#ifdef GFX_BIG
void showPlayerShadow(ball sp)
{	
	int x=3*sp.x-camX, y=3*sp.y-camY;
	setClip(0,0,canvasWidth,canvasHeight);
	setColor(80,108,45);
	//#ifdef J2ME
	//#ifndef SLOWDEVICE
	scr.fillArc(x-8,y-7,16,14,0,360);
	//#endif
	//#endif
	//#ifdef DOJA
	//#endif
}

void showBigFlag(int id, int x, int y)
{
	/*if(ga.tour.isDomestic)
	{
		if(id == ga.headLineTeamDom) id = ga.headLineTeam; else id = 14;		
	}*/	
	//System.out.println (" Want show logo for team "+id);
	
	id = ga.teamLogos[id];
	
	//System.out.println (" Showed: "+id);
	
	if(id>=0)
	//#ifdef J2ME
	showImage(bigFlagsImg,32*(id%4),32*(id/4),32,32,x,y);	
	//#endif
	//#ifdef DOJA
	//#endif
	else{
		setClip(0,0,canvasWidth,canvasHeight);
		scr.setColor(0);
		scr.fillRect(x,y,32,32);
	}	
}

//#ifdef J2ME
public void PutSprite(Image Img,  int X, int Y,  int Rejilla, int Frame, int Trozos, byte[] Coor, int Flip)
{
	
	Frame*=(Trozos*6);
	
	try{
	
	
	for (int i=0 ; i<Trozos ; i++)
	{
		int DestX=Coor[Frame++];
		int DestY=Coor[Frame++];
		int SizeX=Coor[Frame++];
		if (SizeX==0) {return;}
		int SizeY=Coor[Frame++];
		int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
		int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}
	

		switch (Flip)
		{
		case 0:
		showImageFlip(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX, Y+DestY, 0);	// Standard
		break;
	
		case 1:
		showImageFlip(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+(Rejilla-DestY-SizeY), Y+DestX, 5);	// Rotar 270
		break;
	
		case 2:
		showImageFlip(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+(Rejilla-DestX-SizeX), Y+(Rejilla-DestY-SizeY), 3);	// Rotar 180
		break;
	
		case 3:
		showImageFlip(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestY, Y+(Rejilla-DestX-SizeX), 4);	// Rotar 90
		break;
		}
	}
	
	}catch(Exception e) { /*"Frame :"+(Frame/6)+" Array:"+Coor.length);*/}
}
//#endif
//#endif

//#ifdef USEBIGFLAGS
//#endif

boolean ledBmpTestPixel(int offset)
{
	byte pos;
	if((offset/8)>=ledBmp.length) pos = 0;
	else pos = ledBmp[offset/8];
	
	return ((pos>>(7-(offset%8))) & 0x01) != 0;
}

void showLed(int fr, boolean inv)
{
	//#ifdef GFX_BIG	
	int x=1+(canvasWidth-135)/2, y=(canvasHeight-90)/2, offset = 45*30*fr;
	
	//#ifdef J2ME	
	showImage(borderHImg,0,0,176,21,x-22,y-21);
	showImage(borderHImg,0,21,176,23,x-22,y+90);
	showImage(borderVImg,0,0,22,90,x-22,y);
	showImage(borderVImg,22,0,22,90,x+135,y);
	//#endif
	
	//#ifdef DOJA
	//#endif
				
	setClip(0,0,canvasWidth,canvasHeight);		
	setColor(0,0,0);	
	scr.fillRect(x,y,135,90);		
	setColor(171,160,0);		
	
	if(fr<0) return;
		
	for(int j=0; j<30; j++)
	for(int i=0; i<45; i++)
	{
		if(inv ? !ledBmpTestPixel(offset) : ledBmpTestPixel(offset)){
			setColor(171,160,0);		
			scr.fillRect(x+3*i,y+3*j,2,2);	
			/*setColor(255,255,0);			
			scr.fillRect(x+3*i,y+3*j,1,1);*/		
		}
		offset++;
	}	
	
	//#else
	//#endif
}

void showPattern()
{
	if(patternImg != null)
		for(int j = 0; j<canvasHeight; j+=64)
		for(int i = 0; i<canvasWidth; i+=64)
			showImage(patternImg,0,0,64,64,i,j);
	else canvasFill(0x000000);
}


void customText(String s, int x, int y)
{
	
		char c[];		
		int cc, xx=x;
				
		//#ifdef DOJA
		//#else				
		c=s.toUpperCase().toCharArray();			
		//#endif
		
		for(int i=0; i<c.length; i++){
												
			cc=(int)(c[i]-'0')%48;
			if(cc == '.') cc = ']';
			if(c[i] == 209) cc = 12;
							
			//#ifdef J2ME				
			showImage(fntImg,5*(cc%16),5*(cc/16),5,5,xx,y);
			//#endif
			
			//#ifdef DOJA
			//#endif
			
			if(cc<0) xx+=5;
			else xx+=customTextWidth[cc]+1;				
						
		}	
	
}

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