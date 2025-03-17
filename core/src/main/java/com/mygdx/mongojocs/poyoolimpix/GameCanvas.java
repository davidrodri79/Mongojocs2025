package com.mygdx.mongojocs.poyoolimpix;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================


import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

//#else
//#ifdef SE-Z1010
//#else
public class GameCanvas extends FullCanvas
{
//#endif
//#elifdef MO-C450
//#elifdef LISTENER
//#else
//#endif
//#endif


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.2 (1.4.2004)
// ===============================================
// iMode
// ------------------------------------



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------


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

/*public BiosCanvas(Game ga)
{
	this.ga = ga;
	canvasCreate();
}*/
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

	//#ifdef MI
	//#endif
//#ifdef DOJA
//#endif
	scr = g;

	try {

	canvasDraw();

	} catch (Exception e) {System.out.println("*** Exception en la parte Grafica ***"); e.printStackTrace();}

	ProgBarIMP(g);
//#ifdef DOJA
//#else
	scr = null;
//#endif
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw()
{
	canvasShow = true;
	repaint();
//#ifdef DOJA
//#else
	serviceRepaints();
//#endif
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Gestion de Eventos (Teclado, Timers, etc...)
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyX, keyY, keyMisc, keyMenu, keyCode;
//#ifdef DOJA
//#else
public void keyPressed(int keycode)
{
	keyMenu=0; keyX=0; keyY=0; keyMisc=0; keyCode=keycode;

	switch(getGameAction(keycode))
	{
		case 1:keyY=-1;break;	// Arriba
		case 6:keyY=1;break;	// Abajo
		//#ifndef s30
		case 5:keyX=1;break;	// Derecha
		case 2:keyX=-1;break;	// Izquierda
		//#endif 
		case 8:keyMisc=5;keyMenu=2;break;	// Fuego
		//#ifdef VI-TSM100
		//#endif
	}

	switch (keycode)
	{
	//#ifdef NK-3650
	//#else
	case Canvas.KEY_NUM0:
		keyMisc=10;
	break;

	case Canvas.KEY_NUM1:
		keyMisc=1;
	break;

	case Canvas.KEY_NUM2:	// Arriba
		keyMisc=2; keyY=-1;
	break;

	case Canvas.KEY_NUM3:
		keyMisc=3;
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
		keyMisc=7;
	break;

	case Canvas.KEY_NUM8:	// Abajo
		keyMisc=8; keyY=1;
	break;

	case Canvas.KEY_NUM9:
		keyMisc=9;
	break;

	case 35:		// *
		keyMisc=35;
	break;

	case 42:		// #
		keyMisc=42;
	break;
	//#endif

// -----------------------------------------
//#ifdef NK
	case -6:	// Nokia - Menu Izquierda
		keyMenu = -1;
		keyMisc = 5;
	break;

	case -7:	// Nokia - Menu Derecha
		keyMenu = 2;
		keyMisc = 5;
	break;
//#endif
//#ifdef AL
//#endif
//#ifdef SG-Z105
//#endif

// =========================================
//#ifdef MO-T720
// -----------------------------------------
// =========================================
//#endif
//#ifdef MO-C450
// -----------------------------------------
// =========================================
//#endif

// -----------------------------------------
//#ifdef SI
//#endif
// =========================================
//#ifdef MO-V3xx
// -----------------------------------------
// =========================================
//#endif
//#ifdef MO-C65x
// -----------------------------------------
// =========================================
//#endif
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

public void showNotify() 
{/*ga.gamePaused=false;*/
  if (ga.biosStatus == 22)
  { ga.menuShow=true;
    ga.menuListShow=true;
    repaint();
    serviceRepaints();
    soundPlay(0 , 0);
  } else
  {
	  repaint();
	  //#ifndef DOJA
	  serviceRepaints();
	  //#endif
  }
}

public void hideNotify() 
{
  soundStop();/*ga.gamePaused=true;*/ 
  if (ga.gameStatus==60/*GAME_PLAY*/) ga.gameStatus = GAME_MENU_INIT_SECOND;
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
	if ((DestX + SizeX <0) || (DestY + SizeY <0) || DestX >= canvasWidth || DestY >= canvasHeight) {return;}

//	scr.setClip(0, 0, canvasWidth, canvasHeight);
//	scr.clipRect(DestX, DestY, SizeX, SizeY);

	if(DestX < 0 ) {SizeX += DestX; SourX -= DestX; DestX = 0;}
	if(DestY < 0 ) {SizeY += DestY; SourY -= DestY; DestY = 0;}
	if(DestX + SizeX >= canvasWidth) SizeX = canvasWidth - DestX;
	
	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void showImage(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.clipRect(DestX, DestY, SizeX, SizeY);
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

//#endif


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

// -------------------
// canvasText Create
// ===================

public void canvasTextCreate()
{
	printerX = 0;
	printerY = 0;
	printerSizeX = canvasWidth;
	printerSizeY = canvasHeight;
	ga.menuListX = printerX;
	ga.menuListY = printerY;
	ga.menuListSizeX = printerSizeX;
	ga.menuListSizeY = printerSizeY;
}

public void canvasTextCreate(int x, int y, int sizeX, int sizeY)
{
	printerX = x;
	printerY = y;
	printerSizeX = sizeX;
	printerSizeY = sizeY;
	ga.menuListX = printerX;
	ga.menuListY = printerY;
	ga.menuListSizeX = printerSizeX;
	ga.menuListSizeY = printerSizeY;
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

// -------------------
// text Draw
// ===================

public void textDraw(String Str, int X, int Y, int RGB, int Mode)
{
//#ifdef DOJA
//#else
	//#ifdef BIGTEXT
	textDraw(_textoBreak(Str, printerSizeX, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
	//#else
	//#endif
//#endif
}

public void textDraw(String Str, int X, int Y, int W, int RGB, int Mode)
{
//#ifdef DOJA
//#else
	//#ifdef BIGTEXT
	textDraw(_textoBreak(Str, W, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
	//#else
	//#endif
//#endif
}

// -------------------
// text Draw
// ===================

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
//#ifdef DOJA
//#else
	//#ifdef BIGTEXT
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
	//#else
	//#endif
	scr.setFont(f);
//#endif


	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()*Str.length) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  printerSizeY-(f.getHeight()*Str.length) ;}

	X += printerX;
	Y += printerY;

	for (int i=0 ; i<Str.length ; i++)
	{

	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX +=( printerSizeX-f._stringWidth(Str[i]) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX += printerSizeX-f._stringWidth(Str[i]) ;}


		int despY = i * f.getHeight();


		if ((Mode & TEXT_OUTLINE)!=0)
		{
		scr.setColor(MenuTextBorderLineColor);
//#ifdef DOJA
//#else
		scr.drawString(Str[i],  X-1+despX, Y-1+despY,20);
		scr.drawString(Str[i],  X+1+despX, Y-1+despY,20);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY,20);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY,20);
		}

		scr.setColor(RGB);
		scr.drawString(Str[i],  X+despX, Y+despY,20);
	}
//#endif
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
//#ifdef DOJA
//#else
			size += f._charWidth((char)dat);
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

int canvasFillRGB;
boolean canvasFillShow=false;
Image canvasImg;

boolean canvasShow=true;

// -------------------
// canvas Create
// ===================

public void canvasCreate()
{
//#ifdef DOJA
//#else
	canvasFillInit(BackGroundColor);
	//#ifndef SMALLJAR
	canvasImg = new Image();
	canvasImg._createImage("/Loading.png");
	//#endif
	canvasShow = true;
	repaint();
	serviceRepaints();
	
	//#ifdef MO-V3xx
	//#endif
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



// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: iMode - Rev.0 (28.11.2003)
// ===================
// *******************
//#ifdef DOJA
//#elifdef OTASOUND
// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: Nokia OTA - Rev.0 (28.11.2003)
// ===================
// *******************
// <=- <=- <=- <=- <=-
	//#elifdef PLAYER_MIDP20_CACHED
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
		{	Sonidos = new Player[8];

			for (int i=0; i<Sonidos.length; i++) Sonidos[i] = SoundCargar("/1/"+i+".mid");
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
		
			if (ga.gameSound==false) return;
			
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
			//#ifndef SE-T6xx
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
		
		// <=- <=- <=- <=- <=-
	//#elifdef PLAYER_MOTC450
	//#elifdef PLAYER_SIEMENS_MIDI
	//#elifdef PLAYER_SIEMENS_STOP_MIDI
	//#elifdef PLAYER_TSM6
//#elifdef SAMSUNG_PLAYER
//#elifdef API_VODAFONE
//#else
//#endif


// *************************
// -------------------------
// iMode Microjocs FileSystem v2.0 - Engine - Rev.7 (26.1.2004)
// =========================
// *************************

// *** Version Reducida ***

// ------------------------
// HEAD Data Format
// ------------------------
// 00 - (C) - ID - "MICROJOCS_FS"
// 0C - (1) - Version del FileSystem
// 0D - (1) - Tipo de Compresion DATA
// 0E - (1) - Checksum HEAD
// 0F - (1) - Checksum DATA
// 10 - (4) - Size Total Scratch-pad consumido
// 14 - (4) - Size HEAD / Index FAT-Directorios)
// 18 - (4) - Size Total FAT
// 1C - (4) - Size Total DATA (Todos los archivos)

// 20 - (1) - n� de Bloques
// 21 - (1) - n� de Bloques cargados Ok
// 22 - (4) - Size x Bloque
// 26 - (A) - Checksums para 10 Bloques (0 a 9)

// (0x30 = Size del HEAD)
// ========================

// ------------------------
// Errores: SI FS_Create() devuelve TRUE:
// ------------------------
// FS_Error:
// 1: Error de Conexion inicial (No se puede crear una conexion / user DEBE activar las conexiones)
// 2: Error descargando los archivos (Checksum error)
// ========================
//#ifdef DOJA
// #else
int FS_Error;
	boolean FS_ErrorShow = false;

	int[] FS_Data;
	byte[] FS_Head;

// ---------------
// FS_Create
// ===============

	public boolean FS_Create(String FileName)
	{
	/*FS_Head = FS_LoadData(0,0x30);

	if (FS_Head==null || FS_Head[0]!=0x4D || FS_Head[1]!=0x49)
	{
		int Retry = 5;
		while (true)
		{
			if (Retry-- ==0) {FS_ErrorCreate(); return true;}

			byte[] Bufer = FS_DownloadData(FileName+".mfs");

			if (Bufer==null || Bufer.length != 0x30) {FS_Error=1; continue;}	// Controlamos que el Size sea correcto.

			int Checksum = Bufer[0x0E]; Bufer[0x0E]=0;
			if (Checksum != FS_Checksum(Bufer, 0, Bufer.length) ) {FS_Error=2; continue;}	// Comprobamos Checksum

			FS_SaveData(0, Bufer);
			FS_Head = FS_LoadData(0,0x30);
			break;
		}
	}

	ProgBarINS(FS_Head[0x20]);	// Agregamos numero de bloques para cargar en el Slider
	FS_Notify();				// Se ha descargado un .mfs Ok (HEAD)

	int BankSize = ((FS_Head[0x22]&0xFF)<<24) | ((FS_Head[0x23]&0xFF)<<16) | ((FS_Head[0x24]&0xFF)<<8) | (FS_Head[0x25]&0xFF);

	for (int i=0 ; i<FS_Head[0x21] ; i++) {FS_Notify();}

	for (int i=FS_Head[0x21] ; i<FS_Head[0x20] ; i++)
	{
		int Retry = 5;
		while (true)
		{
			if (Retry-- ==0) {FS_ErrorCreate(); return true;}

			byte[] Bufer = FS_DownloadData(FileName+i+".mfs");

			if (Bufer==null || Bufer.length != BankSize) {FS_Error=1; continue;}	// Controlamos que el Size sea correcto.

			if (FS_Head[0x26+i] != FS_Checksum(Bufer, 0, Bufer.length) ) {FS_Error=2; continue;}	// Comprobamos Checksum

			FS_SaveData(FS_Head.length+(i*BankSize), Bufer);
			FS_SaveData(0x21, new byte[] {(byte)(i+1)} );
			break;
		}
	FS_Notify();	// Se ha descargado un .mfs Ok (DATA)
	}

// Cargamos la "FAT" del MFS
// -------------------------
	int Pos  = ((FS_Head[0x14]&0xFF)<<24) | ((FS_Head[0x15]&0xFF)<<16) | ((FS_Head[0x16]&0xFF)<<8) | (FS_Head[0x17]&0xFF);
	int Size = ((FS_Head[0x18]&0xFF)<<24) | ((FS_Head[0x19]&0xFF)<<16) | ((FS_Head[0x1A]&0xFF)<<8) | (FS_Head[0x1B]&0xFF);

	FS_Data = new int[(Size/4)];

	try {
		InputStream in = Connector.openInputStream("scratchpad:///0;pos="+Pos);

		for (int i=0 ; i<FS_Data.length ; i++)
		{
		FS_Data[i]  = (in.read() & 0xFF)<<24;
		FS_Data[i] |= (in.read() & 0xFF)<<16;
		FS_Data[i] |= (in.read() & 0xFF)<<8;
		FS_Data[i] |= (in.read() & 0xFF);
		}

		in.close();
	} catch (IOException e){}

	FS_Error=0;*/
		return false;
	}

// ---------------
// FS_getFirFiles
// ===============

	public int FS_getDirFiles(int dir)
	{
		return 0;
	}

// ---------------
// FS_LoadData
// ===============

	public byte[] FS_LoadData(int Pos, int Size)
	{
		byte []Retorno = new byte[] {};
		return Retorno;
	}

// ---------------
// FS_SaveData
// ===============

	public int FS_SaveData(int Pos, byte[] Bufer)
	{
		int Retorno=0;
		return Retorno;
	}

// ---------------
// FS_LoadFile
// ===============

	public byte[] FS_LoadFile(int Pos, int SubPos)
	{
		byte []Retorno = new byte[] {};
		Retorno = ga.loadFile("/"+Pos+"/"+SubPos+".cor");
		return Retorno;
	}


// ---------------
// FS_SaveFile
// ===============

	public void FS_SaveFile(int Pos, int SubPos, byte[] Bufer)
	{

	}


// ---------------
// FS_LoadImage
// ===============

	public Image[] FS_LoadImage(int Pos, byte Tam)
	{
	/*int Ini = FS_Data[Pos];
	int Size = (FS_Data[Pos+1]-Ini)/4;
	Ini/=4;*/


		Image[] Img = new Image[Tam];

		for (int i=0 ; i<Tam ; i++) {Img[i] = FS_LoadImage(Pos,i);}

		return Img;
	}


// ---------------
// FS_LoadImage
// ===============

	public Image FS_LoadImage(int Pos, int SubPos)
	{
		return loadImage("/"+Pos+"/"+SubPos+".png");
	}


// ---------------
// FS_DownloadData
// ===============

	public byte[] FS_DownloadData(String Filename)
	{
		byte []Retorno = new byte[] {};
		return Retorno;
	}

// ---------------
// FS_Checksum
// ===============

	public byte FS_Checksum(byte[] Bufer, int Pos, int Size)
	{
		byte Retorno = 0;
		return Retorno;
	}

// ---------------
// FS_Notify - Notifica que un Bloque ha sido leido OK, PONER aqu\ED el "repaint()" para actualizar el 'Slider' de Descarga.
// ===============

	public void FS_Notify()
	{

	}

// ---------------
// FS_Error Create
// ===============

	public void FS_ErrorCreate()
	{

	}

// ---------------
// FS_Error Draw
// ===============
//#endif
// <=- <=- <=- <=- <=-




// *******************
// -------------------
// ProgBar - Engine - Rev.2 (20.1.2004)
// ===================
// *******************

int ProgBarX;
int ProgBarY;
int ProgBarSizeX;
int ProgBarSizeY;
int ProgBarPos;
int ProgBarTotal;
boolean ProgBar_ON = false;

// -------------------
// ProgBar INI
// ===================

public void ProgBarINI(int SizeX, int SizeY, int DestX, int DestY)
{
	ProgBarX = DestX;
	ProgBarY = DestY;
	ProgBarSizeX = SizeX;
	ProgBarSizeY = SizeY;
}

// -------------------
// ProgBar END
// ===================

public void ProgBarEND()
{
	ProgBarSET(1,1);
	ProgBar_ON = false;
}

// -------------------
// ProgBar SET
// ===================

public void ProgBarSET(int Pos)
{
	ProgBarSET(Pos, ProgBarTotal);
}

public void ProgBarSET(int Pos, int Total)
{
	ProgBarPos = Pos;
	ProgBarTotal = Total;

	ProgBar_ON = true;

	canvasShow=true; repaint(); //while (canvasShow) {try {Thread.sleep(1);} catch (Exception e) {}}
}

// -------------------
// ProgBar INS
// ===================

public void ProgBarINS(int suma)
{
	ProgBarTotal += suma;
}

// -------------------
// ProgBar ADD
// ===================

public void ProgBarADD()
{
	ProgBarSET(++ProgBarPos);
}

// -------------------
// ProgBar IMP
// ===================

public void ProgBarIMP(Graphics Gfx)
{
	if (ProgBar_ON)
	{
	setC(0,0,canvasWidth,canvasHeight);
	Gfx.setColor(ProgBarBackGroundColor);
	if (ProgBarBackGroundColor!=0x000001) Gfx.fillRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+6, ProgBarSizeY+6);
	Gfx.setColor(ProgBarBorderColor);
	if (ProgBarBackGroundColor!=0x000001) Gfx.drawRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+5, ProgBarSizeY+5);
	Gfx.setColor(ProgBarColor);
	Gfx.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);
	}
}

// <=- <=- <=- <=- <=-








// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//


// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//

public Image[] CargaImagen(int Dir, int Img)
{
	if (Img<0) 
	{
//#ifdef DOJA
//#else
		return FS_LoadImage(Dir,(byte)(0-Img));
//#endif
	} else
	{
		Image[] TempImg= new Image[1];
		TempImg[0] = FS_LoadImage(Dir,Img);
		return TempImg;
	}
}

public Image[] CargaImagen(int Dir, int Img, boolean ProgBarFinalized)
{
	if (Img<0) 
	{
		Img*=-1;
		Image[] TempImg = new Image[Img];
//#ifdef DOJA
//#else
		FS_LoadImage(Dir,(byte)(0-Img));
//#endif
		if (ProgBarFinalized) ProgBarEND(); else ProgBarADD();
		return TempImg;
	} else
	{
		Image[] TempImg= new Image[1];
		TempImg[0] = FS_LoadImage(Dir,Img);
		if (ProgBarFinalized) ProgBarEND(); else ProgBarADD();
		return TempImg;
	}
}

public void CargaI(int Dir, int Img)
{
	if (Img<0)
	{
		Imagenes[Dir] = CargaImagen(Dir,Img);
	} else
	{
		if (Imagenes[Dir][Img]==null) Imagenes[Dir][Img] = CargaImagen(Dir,Img)[0];
	}
}

public void CargaI(int Dir, int Img, boolean ProgBarFinalized)
{
	if (Img<0)
	{
		Imagenes[Dir] = CargaImagen(Dir,Img);
	} else
	{
		if (Imagenes[Dir][Img]==null) Imagenes[Dir][Img] = CargaImagen(Dir,Img)[0];
	}
	if (ProgBarFinalized) ProgBarEND(); else ProgBarADD();
}

public void Vacia(int Dir, int Img,boolean GarbageColector)
{
	//#ifndef ALLINMEM
	//#endif
}

public void Vacia(int Dir,boolean GarbageColector)
{
	//#ifndef ALLINMEM
	//#endif
}

public void Vacia(boolean GarbageColector)
{
	//#ifndef ALLINMEM
	//#endif
}


// ****************
// ----------------
// Explode - Engine
// ================
// ****************

static int A3;
static byte D3;
static byte[] data;

// -----------------------------------------------
// Explode decrunch routine. v1.0 - Rev.0 (24.4.2004)
// Original Imploder / Exploder Authors:
// Albert J. Brouwer
// -----------------------------------------------
// This JAVA support by Juan Antonio G�mez (007!)
// -----------------------------------------------
// IN: byte[] generated by File Imploder program of Amiga Computers
// OUT: uncompresed data or "null" if error.
// -----------------------------------------------

static public byte[] explode(byte[] dataIn)
{
	try
	{

	int D0 = 0;
	int A4 = 0;

// ID = "IMP!" = Imploder

//	dataIn[0] == "IMP!"

// Tama�o del archivo Descomprimido

	A4 = ( (dataIn[5]&0xff)<<16 | (dataIn[6]&0xff)<<8 | (dataIn[7]&0xff) );

// Tama�o del archivo Comprimido (SOLO la parte esencial) (NO el .length del array)

	A3 = ( (dataIn[9]&0xff)<<16 | (dataIn[10]&0xff)<<8 | (dataIn[11]&0xff) );

// Copiamos Bufer de tama�o comprimido (IN) sobre bufer de tama�o Descomprimido (OUT)

	data = new byte[A4]; for (int i=0 ; i<A3 ; i++) {data[i] = dataIn[i];}

// Arreglamos Cabezera del archivo comprimido

	int A0 = (3*4);
	int A2 = A3;
	for (int i=0 ; i<3 ; i++)
	{
	data[--A0] = dataIn[A2+3];
	data[--A0] = dataIn[A2+2];
	data[--A0] = dataIn[A2+1];
	data[--A0] = dataIn[A2+0];
	A2 += 4;
	}

// Tama�o "literal" a copiar Inicialmente.

	int D2 = ((dataIn[A2++]&0xff)<<24 | (dataIn[A2++]&0xff)<<16 | (dataIn[A2++]&0xff)<<8 | (dataIn[A2++]&0xff) );

// Skip para evitar oddAdress en MC68000 (Amiga 500)

	if (dataIn[A2++] >= 0) {A3--;}		// Apa�o por limitaciones del MC68000

// Cargamos primer grupo de bits de control.

	D3 = dataIn[A2++];

// Cargamos Bufer de datos personalizado a la compresion

	byte[] FI_BUFER = new byte[0x1C];
	for (int i=0 ; i<FI_BUFER.length; i++) {FI_BUFER[i] = dataIn[A2++];}

// Destruimos bufer Origen.

	dataIn = null;

// Algoritmo Explode...

	int D4 = 0;

	while (true)
	{
		while (D2-- > 0) {data[--A4] = data[--A3];}

		if (A4 <= 0)
		{
			byte[] dest = data;
			data = null;
			return dest;
		}

		if ( checkBit() )
		{
			if ( checkBit() )
			{
				if ( checkBit() )
				{
					D0 = 3;
					if ( checkBit() )
					{
						if ( checkBit() )
						{
							D4 = data[--A3] & 0xFF;
						} else {
							D4 <<= 1; if ( checkBit() ) {D4++;}
							D4 <<= 1; if ( checkBit() ) {D4++;}
							D4 <<= 1; if ( checkBit() ) {D4++;}
							D4 += 6;
						}
					} else {
						D4 = 5;
					}
				} else {
					D4 = 4;
					D0 = 2;
				}
			} else {
				D4 = 3;
				D0 = 1;
			}
		} else {
			D4 = 2;
			D0 = 0;
		}

		int D5 = 0;
		int D1 = D0;

		if ( checkBit() )
		{
			if ( checkBit() )
			{
				D5 = new byte[] {6,10,10,18} [D0];
				D0 += 8;
			} else {
				D5 = 2;
				D0 += 4;
			}
		}

		D0 = new byte[] {1,1,1,1,2,3,3,4,4,5,7,14} [D0];

		D2 = 0; do {D2 <<= 1; if ( checkBit() ) {D2++;}} while (--D0 > 0);

		D2 += D5;

		A2 = A4 + 1;
		D0 = D1;

		if ( checkBit() )
		{
			D1 <<= 1;

			if ( checkBit() )
			{
				A2 += ( (FI_BUFER[8 + D1]&0xff)<<8 | (FI_BUFER[9 + D1]&0xff) );
				D0 += 8;
			} else {
				A2 += ( (FI_BUFER[0 + D1]&0xff)<<8 | (FI_BUFER[1 + D1]&0xff) );
				D0 += 4;
			}
		}

		D0 = FI_BUFER[0x10 + D0] & 0xFF;

		D5 = 0; do {D5 <<= 1; if ( checkBit() ) {D5++;}} while (--D0 > 0);

		A2 += D5;

		do {data[--A4] = data[--A2];} while (--D4 > 0);
	}

	}
	catch (Exception e)
	{
	data = null;
	return null;
	}
}

// ----------------

static private boolean checkBit()
{
	int carry = D3;							// ADD.B D3,D3

	if ( (D3 <<= 1) == 0 )					// BNE.s Skip
	{
		int carry2 = D3 = data[--A3];		// MOVE.B -(A3),D3
		D3 <<= 1; if (carry < 0) {D3++;}	// ADDX.B D3,D3
		carry = carry2;
	}
										// Skip:
	return carry < 0;						// BCC.s Skip2
}
// ----------------------------------------------------------------------------

	// *******************
// -------------------
// ===================
// *******************
//#ifdef DOJA
//#else

	public void spriteDraw(Image[] img,  int x, int y,  int frame, int C)
	{
		//#ifdef BIGGRAFICS
		spriteDraw(img[0],x,y,frame,Coord[C]);
		//#else
		//#endif
	}

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
		//System.out.println(img);
		showImage(img, sourX, sourY, sizeX, sizeY, destX, destY+DespY);
	}

	public void spriteDraw(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
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

			showImage(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY+DespY);
		}
	}

//#endif
// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------

public GameCanvas(Game ga) 
{ 
 // super(ga);
  this.ga = ga;
  canvasCreate();
  //#ifdef LISTENER
 //#endif
}

//#ifdef LISTENER
//#endif

// **************************************************************************//
// Inicio Clase GameCasfnvas
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
//#ifdef DOJA
//#endif

soundCreate();
//#ifdef ALLINMEM
	ProgBarINI( 94, 6,  (canvasWidth/2)-47, (canvasHeight/2)+16 );

	ProgBarSET(0,18);
	
	CargaI(0,1,false);
	CargaI(3,-3,false);
	CargaI(4,-1,false);
	CargaI(5,-1,false);
	CargaI(6,-4,false);
	CargaI(7,-1,false);
	CargaI(8,-2,false);
	CargaI(9,-9,false);
	CargaI(10,-1,false);
	CargaI(11,-3,false);
	CargaI(12,-1,false);
	CargaI(13,-1,false);
	CargaI(14,-1,false);
	CargaI(15,-1,false);
	CargaI(16,-1,false);
	CargaI(17,-1,false);
	CargaI(18,-1,false);
	CargaI(19,-1,true);
	ProgBarEND();
//#endif
	//#if BIGGRAFICS||DOJA
	Coord = new byte [ga.NumPlayers][];
	//#else
	//#endif
	for (int i=0; i<Coord.length; i++)
	{
		Coord[i] = FS_LoadFile(13,6+i);

		// MONGOFIX====================================================
		for(int j = 0; j < Coord[i].length; j++)
		{
			if(Coord[i][j] < 0) Coord[i][j] += 256;
		}
	}
//#ifdef DOJA
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
	//#ifndef ALLINMEM
	//#endif
	initFrutas();
	NubesPos = ga.RND(256)-128;
	PajaroPos = 0-ga.RND(1000);
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
	System.gc();
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
	if (ga.playPaused==false)
	{
		for (int i=0; i<ga.NumEnems; i++)
		{
			ga.Enems[i][9]++;
			if (ga.Enems[i][7]!=ga.Enems[i][8])
			{
				ga.Enems[i][9] = 0;
				ga.Enems[i][8] = ga.Enems[i][7];
			}
			if (ga.Enems[i][9]>=MaxFrame[ga.Enems[i][5]][ga.Enems[i][7]])
			{
				switch (ga.Enems[i][7])
				{
					case 0:
					case 1:
					case 4:
					case 12:
					case 15:
						ga.Enems[i][9] = 0;
					break;
					
					case 2:
					case 3:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 13:
					case 14:
					case 16:
					case 17:
						ga.Enems[i][9]--;
					break;
				}
			}
		}
		ActFrame++;
		if (ActAnim!=LastAnim)
		{
			ActFrame = 0;
			LastAnim = ActAnim;
			AnimFinalized = false;
		}
		if (ActFrame>=MaxFrame[ga.ActualPlayer][ActAnim])
		{
			switch (ActAnim)
			{
				case 0:
				case 1:
				case 4:
				case 12:
				case 15:
					ActFrame = 0;
				break;
				
				case 2:
				case 3:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 13:
				case 14:
				case 16:
				case 17:
					ActFrame--;
				break;
			}
			AnimFinalized=true;
		}
	}
	
	switch (ga.gameStatus)
	{
		//case GAME_LOGOS :
		//case GAME_MENU_MAIN :
		case GAME_MENU_INIT_SECOND :
		//case GAME_MENU_GAMEOVER :
		//case GAME_NEW_OLIMPIX :
		//case GAME_CONTINUE_OLIMPIX :
		//case GAME_TRAIN :
		//case GAME_PRE_PLAYER_SELECT :
//		//case GAME_PLAYER_SELECT :
		//case GAME_PRE_TEXT_INTRO :
		//case GAME_TEXT_INTRO :
		//case GAME_PRE_EVENT_SELECT :
		//case GAME_EVENT_SELECT :
		//case GAME_PRE_SHOW_HELP :
		//case GAME_SHOW_HELP :
		//case GAME_PRE_SHOW_ABOUT :
		//case GAME_SHOW_ABOUT :
		break;
		
		case GAME_CREATE :
			PintaFondo(0x000000);
			canvasTextCreate();
			textDraw(ga.gameText[ga.TEXT_LOADING][0],3,3,0xFFFFFF,0x200);
			canvasTextCreate((canvasWidth-ga.panelWidth)/2+4,0,ga.panelWidth-8,canvasHeight);
			Par[0] = (canvasWidth-ga.panelWidth)/2;
			Par[1] = (canvasHeight-ga.panelHeight);
			Pinta(9,0);
			switch (ga.ActualEvent)
			{
				case 0 :
				case 3 :
					if (10000>=ga.ProtX)
					{
						ScrollPos = ga.ProtX/10;
						PerspectiveCenter = PlayersWidth+((canvasWidth-(2*PlayersWidth))*(ga.ProtX/10))/1000;
					} else
					{
						ScrollPos = 1000;
					}
				break;	
				case 1 :
					if (ga.Caer==false)
					{
						ScrollPos = ga.ProtX/10;
						if (ga.ProtX<4000) PerspectiveCenter = PlayersWidth+(((canvasWidth/2)-(PlayersWidth))*(ga.ProtX/10))/400; 
						else PerspectiveCenter = (canvasWidth/2);
					}
				break;
				case 2 :
					if (ga.ItemX == 0)
					{
						ScrollPos = ga.ProtX/10;
						PerspectiveCenter = PlayersWidth+((canvasWidth-(2*PlayersWidth))*(ga.ProtX/10))/400;
					} else
					{
						ScrollPos = ga.ItemX/10;
					}
				break;
				case 5 :
					if (ga.gameStatus!=GAME_TRY_END)
					{
						ScrollPos = ga.ItemX/10;
						PerspectiveCenter = (((canvasWidth/2)<(PlayersWidth+ga.ItemX/10))?(canvasWidth/2):(PlayersWidth+ga.ItemX/10));
					}
				break;
				case 4 :
				case 6 :
					ScrollPos = ga.ProtX/10;
					if (ga.ProtX<4000) PerspectiveCenter = PlayersWidth+(((canvasWidth/2)-(PlayersWidth))*(ga.ProtX/10))/400; 
					else PerspectiveCenter = ((((canvasWidth*6)/7)>(canvasWidth/2)+((ga.ProtX-4000)/10))?(canvasWidth/2)+((ga.ProtX-4000)/10):((canvasWidth*6)/7));
				break;	
				default :
					ScrollPos = ga.ProtX/10;
				break;
			}
		break;
		
		case GAME_TRY_END :
		case GAME_PLAY :
			if (++PajaroPos>=(2*canvasWidth)) PajaroPos = 0-ga.RND(1000);
			if ((1000+PajaroPos)%10==0) if (--NubesPos<=(0-canvasWidth)) NubesPos = canvasWidth+ga.RND(100);
	
		case GAME_INIT :
		case GAME_PRE_EVENT_END :
		case GAME_PRE_NEXT :
		case GAME_EVENT_END :
		case GAME_NEXT :
		case GAME_RETURN :
		case GAME_COUNTDOWN :
			DespY = Math.max(0,0-(((ga.ActualEvent==0)||(ga.ActualEvent==3))?(canvasHeight-(ScoresHeight+PistaHeight-((PistaHeight/NumCorrerCarrils)*(NumCorrerCarrils/2)))+(PistaHeight/(NumCorrerCarrils*2))-((ga.ProtY/10)+PlayersHeight)):
	(canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-((ga.ProtY/10)+PlayersHeight))));
			//DespY = Math.max(0,0-(canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-((ga.ProtY/5)+PlayersHeight)));
			switch (ga.ActualEvent)
			{
				case 0 :
				case 3 :
					if (10000>=ga.ProtX)
					{
						ScrollPos = ga.ProtX/10;
						PerspectiveCenter = PlayersWidth+((canvasWidth-(2*PlayersWidth))*(ga.ProtX/10))/1000;
					} else
					{
						ScrollPos = 1000;
					}
				break;
				case 1 :
					if (ga.Caer==false)
					{
						ScrollPos = ga.ProtX/10;
						if (ga.ProtX<4000) PerspectiveCenter = PlayersWidth+(((canvasWidth/2)-(PlayersWidth))*(ga.ProtX/10))/400; 
						else PerspectiveCenter = (canvasWidth/2);
					}
				break;
				case 2 :
					if (ga.ItemX == 0)
					{
						ScrollPos = ga.ProtX/10;
						PerspectiveCenter = PlayersWidth+((canvasWidth-(2*PlayersWidth))*(ga.ProtX/10))/400;
					} else
					{
						ScrollPos = ga.ItemX/10;
					}
				break;
				case 5 :
					if (ga.gameStatus!=GAME_TRY_END)
					{
						ScrollPos = ga.ItemX/10;
						PerspectiveCenter = (((canvasWidth/2)<(PlayersWidth+ga.ItemX/10))?(canvasWidth/2):(PlayersWidth+ga.ItemX/10));
					}
				break;
				case 4 :
				case 6 :
					ScrollPos = ga.ProtX/10;
					if (ga.ProtX<4000) PerspectiveCenter = PlayersWidth+(((canvasWidth/2)-(PlayersWidth))*(ga.ProtX/10))/400; 
					else PerspectiveCenter = ((((canvasWidth*6)/7)>(canvasWidth/2)+((ga.ProtX-4000)/10))?(canvasWidth/2)+((ga.ProtX-4000)/10):((canvasWidth*6)/7));
				break;	
				default :
					ScrollPos = ga.ProtX/10;
				break;
			}
			VallasPos = 0-(ScrollPos/4);
		case GAME_MENU_SECOND :
		case GAME_ANGLE :
			PintaEscenario();
			PintaFlechas();
			PintaScores();
			PintaTextos();
			if (ga.gameStatus == GAME_ANGLE) PintaAngulo();
		break;
		
		case GAME_PRE_END :
		case GAME_END :
			PintaFondo(0x000000);
		break;
		
		case GAME_MENU_INIT_MAIN :
			//#ifndef ALLINMEM
			//#endif
		default :
			PintaFondo(-1);
			PintaTextos();
		break;
	}
	if ((ga.panelMove != 0)||(ga.panelAct)) PintaPanel();
}

// <=- <=- <=- <=- <=-

public void PintaFondo(int F)
{
	if (F>=0) 
	{
		setC(0,0,canvasWidth,canvasHeight);
		scr.setColor(F);
		scr.fillRect(0,0,canvasWidth,canvasHeight);
	} else
	{
		if ((Imagenes[0][F*(-1)]) != null) showImage(Imagenes[0][F*(-1)]); /*else
		showImage(FS_LoadImage(0,F*(-1))); PintaFondo(0xFFFFFF);*/
	}
}

int MaxTextos=1;
String[] Textos = new String[MaxTextos];
int[] TextosX = new int[MaxTextos];
int[] TextosY = new int[MaxTextos];
int[] TextosC = new int[MaxTextos];
int[] TextosT = new int[MaxTextos];

public void InitTexto(String Text, int X, int Y, int C, int T)
{
	for (int Counter=0; Counter<MaxTextos; Counter++)
	{
		if (Textos[Counter]==null)
		{
			Textos[Counter] = Text;
			TextosX[Counter] = X;
			TextosY[Counter] = Y;
			TextosC[Counter] = C;
			TextosT[Counter] = T;
			Counter = MaxTextos;	
		}
		if (Counter==(MaxTextos-1))
		{
			Textos[Counter] = Text;
			TextosX[Counter] = X;
			TextosY[Counter] = Y;
			TextosC[Counter] = C;
			TextosT[Counter] = T;
		}
	}	
}

public void PintaTextos()
{
	canvasTextCreate((canvasWidth-ga.panelWidth)/2+4,0,ga.panelWidth-8,canvasHeight);
	for (int Counter=0; Counter<MaxTextos; Counter++) 
	{
		if (Textos[Counter]!=null)
		{
			MenuTextBorderLineColor=0x000000;
			setC(0,0,canvasWidth,canvasHeight);
			textDraw(Textos[Counter], TextosX[Counter]-(canvasWidth/2), TextosY[Counter]-(canvasHeight/2), TextosC[Counter], 0x121A);
			if (TextosT[Counter]>0) TextosT[Counter]--; else Textos[Counter]=null;
		} else
		{
			TextosT[Counter] = 0;
		}
	}
}

public void Pinta(int Dir, int Img, int[] Parameters)
{
	if ((Dir != 9)&&(Dir != 13)) Parameters[1] += DespY;
	switch (Dir)
	{
		//#ifdef J2ME
		case 3:
			if (Img>=2) showImage(Imagenes[Dir][2],(Img-2)*PajaroWidth,0,PajaroWidth,PajaroHeight,Parameters[0], Parameters[1]);
			else showImage(Imagenes[Dir][Img], Parameters[0], Parameters[1]);
		break;
		case 4:
			showImage(Imagenes[Dir][0],(Img)*PublicoWidth,0,PublicoWidth,PublicoHeight,Parameters[0], Parameters[1]);
		break;
		case 6:
			if (Img>=3) showImage(Imagenes[Dir][3],(Img-3)*FrutasWidth,0,FrutasWidth,FrutasHeight,Parameters[0], Parameters[1]);
			else showImage(Imagenes[Dir][Img], Parameters[0], Parameters[1]);
		break;
		case 7:
			showImage(Imagenes[Dir][0],(Img)*BanderillaTextWidth,0,BanderillaTextWidth,BanderillaTextHeight,Parameters[0], Parameters[1]);
		break;
		case 10:
			showImage(Imagenes[Dir][0],(Img)*HumoWidth,0,HumoWidth,HumoHeight,Parameters[0], Parameters[1]);
		break;
		case 11:
			if (Img>=2) showImage(Imagenes[Dir][2],(Img-2)*MelonWidth,0,MelonWidth,MelonHeight,Parameters[0], Parameters[1]);
			else
			{
				//#ifdef LOWMEM
				//#endif
				showImage(Imagenes[Dir][Img], Parameters[0], Parameters[1]);
			}
		break;
		case 12:
			showImage(Imagenes[Dir][0],(Img)*BanderasWidth,0,BanderasWidth,BanderasHeight,Parameters[0], Parameters[1]);
		break;
		case 13:
			showImage(Imagenes[Dir][0],(Img)*ArrowsWidth,0,ArrowsWidth,ArrowsHeight,Parameters[0], Parameters[1]);
		break;
		//#endif
		
		//#ifdef LOWMEM
		//#endif
		default :
			if (Imagenes[Dir][Img] == null) System.out.println("Null: "+Dir+","+Img);
			showImage(Imagenes[Dir][Img], Parameters[0], Parameters[1]);
		break;
	}
	if ((Dir != 9)&&(Dir != 13)) Parameters[1] -= DespY;
}

public void Pinta(int Dir, int Img)
{
	Pinta(Dir,Img,Par);
}

//CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA CARGA

public void CargaMenu()
{
	//#ifndef ALLINMEM
		//#ifdef SMALLJAR
		//#else
			//#ifdef LOWMEM
			//#else
				//#ifdef MO
				//#else
				CargaI(9,-9);
				//#endif
			//#endif
		//#endif
	//#endif
	//CargaI(9,7);
}

public void DescargaMenu()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaPanel()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaBanderas()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaHumo()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaHumo()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaBanderas()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaMenuImg()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaMenuImg()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaEscena()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaEscena()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaVarios()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaVarios()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaArrows()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaArrows()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaScores()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaScores()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaPlayers()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaPlayers()
{
	//#ifndef ALLINMEM
	//#endif
}

public void CargaFondo()
{
	//#ifndef ALLINMEM
	//#endif
}

public void DescargaFondo()
{
	//#ifndef ALLINMEM
	//#endif
}

String TempString="";
//PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA PINTA
public void PintaPanel()
{
	canvasTextCreate((canvasWidth-ga.panelWidth)/2+4,0,ga.panelWidth-8,canvasHeight);
	boolean PintaFix = false;
	int TextColor = 0xDDDDDD;
	int TempColor = 0x000000;
	Par[0] = (canvasWidth-ga.panelWidth)/2;
	Par[1] = ga.panelPos;
	Pinta(9,0);
	if (ga.panelTitle!=null)
	{
		//#ifdef SI
		//#endif
		if (ga.panelFade>0)
		{
			if ((ga.panelAct==false)&&(ga.panelFade==1)) ga.panelFade++;
			if (ga.panelFade == ga.fadeTime+1) PintaFix = true;
			if ((PintaFix==false)&&(--ga.panelFade==0))
			{
				ga.panelFade = ga.fadeTime*2;
				TempString = ga.panelTitle;
			}
			TextColor = 0x000000;
			TempColor = (((ga.fadeTime-Math.abs(ga.fadeTime+1-ga.panelFade))*0xFF)+((Math.abs(ga.fadeTime+1-ga.panelFade))*0x5A))/ga.fadeTime;
			TextColor |= TempColor;
			TextColor = TextColor<<8;
			TempColor = (((ga.fadeTime-Math.abs(ga.fadeTime+1-ga.panelFade))*0xFF)+((Math.abs(ga.fadeTime+1-ga.panelFade))*0x47))/ga.fadeTime;
			TextColor |= TempColor;
			TextColor = TextColor<<8;
			TempColor = (((ga.fadeTime-Math.abs(ga.fadeTime+1-ga.panelFade))*0xFF)+((Math.abs(ga.fadeTime+1-ga.panelFade))*0x5F))/ga.fadeTime;
			TextColor |= TempColor;
		}
		if (ga.panelType>0) textDraw(ga.panelTitle, 0, (ga.panelPos+(ga.panelHeight/2))-(canvasHeight/2), 0xDDDDDD, 0x20A); else
		textDraw(TempString, 0, (ga.panelPos+(ga.panelHeight/2))-(canvasHeight/2), TextColor, 0x20A);
	}
	if (ga.panelMove>=0) ga.panel2Pos = (ga.panelMaxPos+ga.panelHeight)+((Math.abs(ga.panelMaxPos-ga.panelPos)*(canvasHeight-(ga.panelMaxPos+ga.panelHeight)))/((ga.panelHeight)));
	else ga.panel2Pos = (ga.panelInitPos+ga.panelHeight)+((((Math.abs(ga.panelMaxPos-ga.panelInitPos))-Math.abs(ga.panelMaxPos-ga.panelPos))*(canvasHeight-(ga.panelMaxPos+ga.panelHeight)))/((ga.panelHeight)));
	canvasTextCreate(Par[0]+8,ga.panel2Pos+8,ga.panelWidth-16,ga.panel2Height-8);
	if (ga.panelType>0)
	{
		Par[1] = ga.panel2Pos;
		Pinta(9,1);
		if (PintaMapa)
		{
			PintaMapa(ga.ActualPlayer);
			setC(0,0-DespY,canvasWidth,canvasHeight);
			//#ifdef BIGCANVAS
			textDraw(ga.panelInfo,0-BanderasWidth-4,0-8,0xFFFF88,0x015);
			//#else
			//#endif
		} else
		{
			if (PintaEvent>=0)
			{
				PintaFix = false;
				if (PintaEvent == ga.fadeTime+1) PintaFix = true;
				if ((PintaFix==false)&&(--PintaEvent==0))
				{
					PintaEvent = ga.fadeTime*2;
					TempString = ga.gameText[TEXT_EVENT_INFO][ga.ActualEvent];
				}
				TextColor = 0x000000;
				TempColor = 0x000000;
				TempColor = (((ga.fadeTime-Math.abs(ga.fadeTime+1-PintaEvent))*0xFF)+((Math.abs(ga.fadeTime+1-PintaEvent))*0x5A))/ga.fadeTime;
				TextColor |= TempColor;
				TextColor = TextColor<<8;
				TempColor = (((ga.fadeTime-Math.abs(ga.fadeTime+1-PintaEvent))*0xFF)+((Math.abs(ga.fadeTime+1-PintaEvent))*0x47))/ga.fadeTime;
				TextColor |= TempColor;
				TextColor = TextColor<<8;
				TempColor = (((ga.fadeTime-Math.abs(ga.fadeTime+1-PintaEvent))*0xFF)+((Math.abs(ga.fadeTime+1-PintaEvent))*0x5F))/ga.fadeTime;
				TextColor |= TempColor;
				setC(0,0-DespY,canvasWidth,canvasHeight);
				//#ifdef SMALLCANVAS
				//#else
				textDraw("< "+TempString+" >",0,-4,TextColor,0x00A);
				//#endif
				////System.out.println(ga.MaxScores[ga.ActualEvent]);
				//#ifdef NK
					//#ifndef NK-s30
					//#else
					textDraw((((ga.ActualEvent!=0)&&(ga.ActualEvent!=3))
							?((ga.MaxScores[ga.ActualEvent]>0)?(ga.MaxScores[ga.ActualEvent]/100+"."+(((ga.MaxScores[ga.ActualEvent]%100)<10)?"0":"")+ga.MaxScores[ga.ActualEvent]%100+" m"):"")
							:((ga.MaxScores[ga.ActualEvent]>0)?(((ga.MaxScores[ga.ActualEvent]/1000)+"."+(((ga.MaxScores[ga.ActualEvent]%1000)<100)?"0":"")+(((ga.MaxScores[ga.ActualEvent]%1000)<10)?"0":"")+ga.MaxScores[ga.ActualEvent]%1000+" s")):"")
							),0,7,TextColor,0x00A);
					//#endif
				//#else
				//#endif
				//#ifndef SMALLCANVAS
					PintaSelected(ga.ActualPlayer);
					setC(0,0,canvasWidth,canvasHeight);
					//#ifdef BIGCANVAS
					textDraw(ga.panelInfo,0-BanderasWidth-4,0-8,0xFFFF88,0x015);
					//#else
					//#endif
				//#endif
			} else
			{
				setC(0,0,canvasWidth,canvasHeight);
				textDraw(ga.panelInfo,0,0,0xDDDDDD,0x002);
			}
		}
	}
}

public void PintaMapa(int Map)
{	
	//#ifndef SMALLCANVAS
		Par[0] = (canvasWidth-mapaWidth)/2;
		Par[1] = ga.panel2Pos+2+((ga.panel2Height-(mapaHeight+BanderasHeight))/2);
		Pinta(9,2);
		Par[0] += MapaX[Map];
		Par[1] += MapaY[Map];
		//#ifndef LOWMEM
		Pinta(9,3+Map);
		//#endif
		//#ifdef BIGGRAFICS
		Par[0]+=5;
		Par[1]+=5;
		//#endif
		Par[0]+=5;
		Par[1]+=10+ga.sinoidal;
		Pinta(11,0);
	//#endif
	PintaSelected(Map);
}

public void PintaSelected(int Pj)
{
	
	Par[0] = ((canvasWidth-ga.panelWidth)/2)+8;
	Par[1] = (ga.panel2Pos+ga.panel2Height)-8;
	
	//#ifdef SMALLCANVAS
	//#endif
	
	//#ifndef LOWMEM
	//#ifdef SMALLGRAFICS
	//#endif
	Par[0] -=(PlayersWidth/3);
	Par[1] -=(PlayersHeight);
	PintaPj(Pj, 0, ActFrame);
	//#else
	//#endif
	
	//spriteDraw(Imagenes[14+Pj],  Par[0], Par[1],  0, Coord);
	
	//#ifdef BIGCANVAS
		//#ifndef LOWMEM
		Par[0] = ((canvasWidth-ga.panelWidth)/2)+ga.panelWidth-(BanderasWidth+8);
		Par[1] = (ga.panel2Pos+ga.panel2Height)-(BanderasHeight+8);
		Pinta(12,Pj);
		//#endif
	//#endif
}

//////////////////////////////////////////////

public void PintaEscenario()
{
	/*switch (ga.ActualEvent)
	{
		default :*/
			PintaCielo();
			PintaPublico();
			PintaVallas();
			PintaCesped();
			PintaArena();
			PintaCarriles();
			setC(0,0-DespY,canvasWidth,0);
			scr.setColor(0x58B0FF);
			scr.fillRect(0,0,canvasWidth,DespY);
			for (int i=0; i<NumCorrerCarrils; i++)
			{
				PintaPersonaje((NumCorrerCarrils-1)-i);
				PintaFrutas(i);
			}
			if (Barras) PintaBarras2();
			PintaItem();
		/*break;
	}	*/
}

public void PintaCielo()
{
	setC(0,0,canvasWidth,(canvasHeight-(ScoresHeight+PistaHeight+GrassHeight+WallHeight+PublicoHeight))+DespY);
	scr.setColor(0x58B0FF);
	scr.fillRect(0,0,canvasWidth,(canvasHeight-(ScoresHeight+PistaHeight+GrassHeight+WallHeight+PublicoHeight))+DespY);
	//#if !(SLOW||LOWMEM||SMALLJAR)
	//#endif
	//#if !(LOWMEM&&SMALLJAR)  
	Par[0] = NubesPos;
	Par[1] = canvasHeight-(ScoresHeight+PistaHeight+GrassHeight+WallHeight+PublicoHeight+NubesHeight);
	Pinta(3,1);
	//#endif
	//#if !(SLOW||LOWMEM||SMALLJAR)
	Par[0] = PajaroPos;
	Par[1] = (3*(canvasHeight-(ScoresHeight+PistaHeight+GrassHeight+WallHeight+PublicoHeight+NubesHeight)))/4;
	Pinta(3,2+(1000+PajaroPos)%2);
	//#endif
}

public void PintaVallas()
{
	VallasPos = 0-(ScrollPos/4);
	Par[1] = canvasHeight-(ScoresHeight+PistaHeight+GrassHeight+WallHeight);
	while (VallasPos<(0-VallasWidth*2)) VallasPos += VallasWidth*2;
	for (int i = 0; i<=((canvasWidth/VallasWidth)+1); i++)
	{
		Par[0] = (VallasPos/2)+(i*VallasWidth);
		Pinta(5,0);
	}
}

public void PintaCesped()
{
	setC(0,0,canvasWidth,canvasHeight);
	scr.setColor(0x0EC800);
	scr.fillRect(0,canvasHeight-(ScoresHeight+PistaHeight+GrassHeight)+DespY,canvasWidth,GrassHeight+PistaHeight);
	/*
	switch (ga.ActualEvent)
	{
		default :
			scr.fillRect(0,canvasHeight-(ScoresHeight+PistaHeight+GrassHeight),canvasWidth,GrassHeight);
		break;
		case 2 :
		case 5 :
			scr.fillRect(0,canvasHeight-(ScoresHeight+PistaHeight+GrassHeight),canvasWidth,GrassHeight+CarrilHeight);
			scr.fillRect(0,canvasHeight-(ScoresHeight+PistaHeight-(2*CarrilHeight)),canvasWidth,PistaHeight-(2*CarrilHeight));
		break;
	}*/
}

public void PintaArena()
{
	switch (ga.ActualEvent)
	{
		case 1 :
		case 4 :
		case 6 :
			setC(0,0,canvasWidth,canvasHeight);
			scr.setColor(0xD46943);
			scr.fillRect(0,canvasHeight-(ScoresHeight+PistaHeight)+DespY,canvasWidth,PistaHeight);
		break;
	}
}

public void PintaPublico()
{
	VallasPos = 0-(ScrollPos/4);
	Par[1] = canvasHeight-(ScoresHeight+PistaHeight+GrassHeight+WallHeight+PublicoHeight);
	while (VallasPos<(0-PublicoWidth*2)) VallasPos += PublicoWidth*2;
	for (int i = 0; i<=((canvasWidth/PublicoWidth)+1); i++)
	{
		Par[0] = (VallasPos/2)+i*PublicoWidth;
		Pinta(4,Math.abs(ga.sinoidal/2)%2);
	}
}

public void PintaCarriles()
{
	switch (ga.ActualEvent)
	{
		case 0 :
		case 3 :
			PintaCarril2(100, 1000);
		break;
		case 1 :
		case 4 :
		case 6 :
			PintaCarril(1,100,400);
		break;
		case 2 :
			PintaCarril(1,50,200);
		break;
		case 5 :
			PintaCarril(1,0,5);
		break;
	}	
	PintaExtras();
}

public void PintaExtras()
{
	switch (ga.ActualEvent)
	{
		case 0 :
			PintaBanderillas(0,100,10);
		break;
		case 3 :
			PintaBanderillas(0,100,10);
		break;
		case 5 :
			PintaBanderillas(5,100,5);
			//#if NE||MO||NK
			PintaRallas(5,20,30);
			//#else
			//#endif
		break;
		case 2 :
			PintaBanderillas(200,-50,4);
			PintaBanderillas(200,100,10);
			//#if NE||MO||NK
			PintaRallas(200,20,50);
			PintaRallas(1060,20,7);
			//#else
			//#endif
		break;
		case 4 :
			PintaBanderillas(400,-50,4);
			PintaColchoneta(405);
			PintaBarras(400,ga.SAltura);
		break;
		case 6 :
			PintaBanderillas(400,-50,4);
			PintaColchoneta(405);
			PintaBarras(400,ga.SPertiga);
		break;
		case 1 :
			PintaBanderillas(400,-50,4);
			PintaTierra(400);
			PintaBanderillas(400,25,6);
		break;
	}
	PintaBurbujas();
}

public void PintaCarril(int C, int D, int F)
{
	//int IY = canvasHeight-(ScoresHeight+PistaHeight-(C*CarrilHeight));
	//int FY = IY+CarrilHeight;
	
	int IY = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4;
	int FY = IY+CarrilHeight;
	
	switch (C)
	{
		case 0 :
			FY = IY;
			IY = canvasHeight-(ScoresHeight+PistaHeight);
		break;
		
		case 2 :
			IY = FY;
			FY = canvasHeight-(ScoresHeight);
		break;
	}
	setC(0,0,canvasWidth,canvasHeight);
	scr.setColor(0xD46943);
	switch (ga.ActualEvent)
	{
		case 1 :
		case 2 :
		case 5 :
			if (canvasWidth>(PerspectiveCenter+NX(F,(IY+FY)/2)-ScrollPos))
			{
				if ((PerspectiveCenter+NX(F,(IY*3+FY)/4)-ScrollPos)>0)
					scr.fillRect(0,IY+DespY,PerspectiveCenter+NX(F,(IY*3+FY)/4)-ScrollPos,(FY-IY)/2+1);
				if ((PerspectiveCenter+NX(F,(IY+FY*3)/4)-ScrollPos)>0)
					scr.fillRect(0,FY-(FY-IY)/2+DespY,PerspectiveCenter+NX(F,(IY+FY*3)/4)-ScrollPos,(FY-IY)/2);
				if ((PerspectiveCenter+NX(F,(IY+FY)/2)-ScrollPos)>0)
				{
					scr.setColor(0xFFFFFF);
					scr.drawLine(0,IY+DespY,PerspectiveCenter+NX(F,IY)-ScrollPos,IY+DespY);
					scr.drawLine(0,FY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos,FY+DespY);
				}
				break;
			}
		default :
			scr.fillRect(0,IY+DespY,canvasWidth,FY-IY);
		
			scr.setColor(0xFFFFFF);
			scr.drawLine(0,IY+DespY,canvasWidth,IY+DespY);
			scr.drawLine(0,FY+DespY,canvasWidth,FY+DespY);
		break;	
	}
	scr.setColor(0xFFFFFF);
	if (D>0)
	{
		for (int i=0; i<=(F/D); i++)
		{
			scr.drawLine(PerspectiveCenter+NX((i*D),IY+DespY)-ScrollPos,IY,PerspectiveCenter+NX((i*D),FY)-ScrollPos,FY+DespY);
		}
	}
	//#ifdef BIGGRAFICS
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos-4,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos-4,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos-3,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos-3,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos-2,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos-2,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+3,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+3,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+4,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+4,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+5,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+5,FY+DespY);
	//#endif
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos-1,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos-1,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+0,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+0,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+1,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+1,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+2,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+2,FY+DespY);
}	

public void PintaCarril2(int D, int F)
{
	int IY = canvasHeight-(ScoresHeight+PistaHeight);
	int FY = IY+PistaHeight;
	setC(0,0,canvasWidth,canvasHeight);
	scr.setColor(0xD46943);
	scr.fillRect(0,IY+DespY,canvasWidth,FY-IY);
	scr.setColor(0xFFFFFF);
	for (int i=0; i<=NumCorrerCarrils; i++)
	{
		scr.drawLine(0,IY+i*(PistaHeight/NumCorrerCarrils)+DespY,canvasWidth,IY+i*(PistaHeight/NumCorrerCarrils)+DespY);
	}
	if (D>0)
	{
		for (int i=0; i<=(F/D); i++)
		{
			scr.drawLine(PerspectiveCenter+NX((i*D),IY)-ScrollPos,IY+DespY,PerspectiveCenter+NX((i*D),FY)-ScrollPos,FY+DespY);
		}
	}
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos-1,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos-1,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+0,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+0,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+1,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+1,FY+DespY);
	scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+2,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+2,FY+DespY);
}	

public void PintaRallas(int Init, int Dist, int Num)
{
	int IY = canvasHeight-(ScoresHeight+PistaHeight);
	int FY = canvasHeight-(ScoresHeight);
	int F;
	setC(0,0,canvasWidth,canvasHeight);
	for (int i = 0; i<=Num; i++)
	{
		F = Init+Dist*i;
		if (((F-ScrollPos)>(0-canvasWidth/2))&&((F-ScrollPos)<(0+canvasWidth/2)))
		  scr.drawLine(PerspectiveCenter+NX(F,IY)-ScrollPos+0,IY+DespY,PerspectiveCenter+NX(F,FY)-ScrollPos+0,FY+DespY);
	}
}

public void PintaBanderillas(int Init, int Dist, int Num)
{
	for (int i = 0; i<=Num; i++)
	{
		Par[1] = canvasHeight-(ScoresHeight+PistaHeight+BanderillaHeight);
		Par[0] = PerspectiveCenter+NX(Init+Dist*i,Par[1]+BanderillaHeight)-ScrollPos;
		Pinta(6,0);
		PintaNumBanderillas(Par[0]+3,canvasHeight-(ScoresHeight+PistaHeight+BanderillaTextHeight+1),Math.abs(i*(Dist/10)));
	}
}

public void PintaNumBanderillas(int X, int Y, int Num)
{
	int Cifras = 1;
	while ((Num/(Exp(10,Cifras)))>0) Cifras++;
	Par[0] = X;
	Par[1] = Y;
	while (Cifras>0)
	{
		Cifras--;
		Pinta(7,(Num/Exp(10,Cifras)));
		Num -= ((Num/Exp(10,Cifras))*Exp(10,Cifras));
		Par[0] += BanderillaTextWidth+1;
	}
}

public void PintaNumScores(int X, int Y, int Num)
{
	if (ga.gameStatus==GAME_PLAY)
	if ((ga.ActualEvent==0)||(ga.ActualEvent==3))
	{
		int Cifras = 5;
		if ((Num!=-1)||(ga.incSinus>=0))
		{
			if (Num<0) Num=0;
			Num = ((Num/6000)*10000)+(Num%6000);
			while ((Num/(Exp(10,Cifras)))>0) Cifras++;
			Par[0] = X-(Cifras)*BanderillaTextWidth-2;
			Par[1] = Y;
			while (Cifras>0)
			{
				Cifras--;
				Pinta(7,(Num/Exp(10,Cifras)));
				if (Cifras==4)
				{
					Par[0]++;
					setC(0,canvasHeight-ScoresHeight-DespY,canvasWidth,ScoresHeight);
					scr.setColor(0xFFFFFF);
					//#ifdef VI-TSM100
					//#else
					scr.fillRect(Par[0]+BanderillaTextWidth,Par[1]+BanderillaTextHeight-1+DespY,1,1);
					scr.fillRect(Par[0]+BanderillaTextWidth,Par[1]+BanderillaTextHeight-3+DespY,1,1);
					//#endif
				}
				if (Cifras==2)
				{
					Par[0]++;
					setC(0,canvasHeight-ScoresHeight-DespY,canvasWidth,ScoresHeight);
					scr.setColor(0xFFFFFF);
					//#ifdef VI-TSM100
					//#else
					scr.fillRect(Par[0]+BanderillaTextWidth,Par[1]+BanderillaTextHeight-1+DespY,1,1);
					//#endif
				}
				Num -= ((Num/Exp(10,Cifras))*Exp(10,Cifras));
				Par[0] += BanderillaTextWidth+1;
			}
		}
	} else
	{
		int Cifras = 3;
		if ((Num!=-1)||(ga.incSinus>=0))
		{
			if (Num<0) Num=0;
			while ((Num/(Exp(10,Cifras)))>0) Cifras++;
			Par[0] = X-(Cifras)*BanderillaTextWidth-1;
			Par[1] = Y;
			while (Cifras>0)
			{
				Cifras--;
				Pinta(7,(Num/Exp(10,Cifras)));
				if (Cifras==2)
				{
					Par[0]++;
					setC(0,canvasHeight-ScoresHeight-DespY,canvasWidth,ScoresHeight);
					scr.setColor(0xFFFFFF);
					//#ifdef VI-TSM100
					//#else
					scr.fillRect(Par[0]+BanderillaTextWidth,Par[1]+BanderillaTextHeight-1+DespY,1,1);
					//#endif
				}
				Num -= ((Num/Exp(10,Cifras))*Exp(10,Cifras));
				Par[0] += BanderillaTextWidth+1;
			}
		}
	}
}

public void PintaPersonaje(int W)
{
	if (W<(NumCorrerCarrils/2))
	{
		PintaEnem(W,(NumCorrerCarrils-1)-W);
	}
	if (W==(NumCorrerCarrils/2))
	{
		PintaProt((NumCorrerCarrils-1)-W);
	}
	if (W>(NumCorrerCarrils/2))
	{
		PintaEnem(W-1,(NumCorrerCarrils-1)-W);
	}
}

public void PintaEnem(int W, int P)
{
	if ((ga.ActualEvent==0)||(ga.ActualEvent==3))
	{
		setC(0,0,canvasWidth,canvasHeight);
		Par[1] = canvasHeight-(ScoresHeight+PistaHeight-((PistaHeight/NumCorrerCarrils)*P))+(PistaHeight/(NumCorrerCarrils*2));
		Par[0] = PerspectiveCenter+NX(ga.Enems[W][0]/10,Par[1])-ScrollPos-PlayersWidth;
		scr.setColor(0x333333);
		scr.fillRect(Par[0]+(PlayersWidth/2)+(PlayersWidth/3)-(2*PlayersWidth/5)/((ga.Enems[W][2]/20)+1),Par[1]-1+DespY,(4*PlayersWidth/5)/((ga.Enems[W][2]/20)+1),2);
		Par[1] -= (ga.Enems[W][2]/10)+PlayersHeight;
		//scr.fillRect(Par[0]+(PlayersWidth/3),Par[1],PlayersWidth,PlayersHeight);
		PintaPj(ga.Enems[W][5],ga.Enems[W][7],ga.Enems[W][9]);//ActAnim,ActFrame);
	}
}

public void PintaProt(int P)
{
	setC(0,0,canvasWidth,canvasHeight);
	if ((ga.ActualEvent==0)||(ga.ActualEvent==3)) Par[1] = canvasHeight-(ScoresHeight+PistaHeight-((PistaHeight/NumCorrerCarrils)*P))+(PistaHeight/(NumCorrerCarrils*2));
	else Par[1] = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2);
	Par[0] = PerspectiveCenter+NX(ga.ProtX/10,Par[1])-ScrollPos-PlayersWidth;
	scr.setColor(0x333333);
	if (ga.ActualEvent==6) scr.fillRect(Par[0]+(PlayersWidth/2)-(2*PlayersWidth/4)/((ga.ProtY/20)+1),Par[1]-1+DespY,(3*PlayersWidth/4)/((ga.ProtY/20)+1),2);
	else scr.fillRect(Par[0]+(PlayersWidth/2)+(PlayersWidth/3)-(2*PlayersWidth/4)/((ga.ProtY/20)+1),Par[1]-1+DespY,(3*PlayersWidth/4)/((ga.ProtY/20)+1),2);
	
	Par[1] -= (ga.ProtY/10)+PlayersHeight;
	if (ga.ActualPlayer!=4) PintaPj(ga.ActualPlayer,ActAnim,ActFrame);
	if (ga.ActualEvent == 2)
	{
		if (ga.Caer==false)
		{
			if ((ga.gameStatus == GAME_ANGLE)||(ActAnim==ANIM_LANZA_J))
			{
				PertigaX0 = Par[0]-5;
				PertigaX1 = Par[0]+10;
				PertigaX2 = Par[0]+Pertiga;
				PertigaX3 = Par[0]+Pertiga+1;
				PertigaY0 = Par[1]+PerPos[ga.ActualPlayer][0];
				PertigaY1 = Par[1]+(PerPos[ga.ActualPlayer][0]*2)/3;
				PertigaY2 = Par[1];
				PertigaY3 = Par[1];
				PintaPertiga(PertigaX0+(PlayersWidth/2),PertigaY0,PertigaX1+(PlayersWidth/2),PertigaY1,PertigaX2+(PlayersWidth/2),PertigaY2+((ga.sinoidal-3)*(1+ga.ProtVX))/ga.ProtMaxV,PertigaX3+(PlayersWidth/2),PertigaY3+((ga.sinoidal-3)*(2+ga.ProtVX))/ga.ProtMaxV);
			} else
			{
				PertigaX0 = Par[0]-5;
				PertigaX1 = Par[0]+10;
				PertigaX2 = Par[0]+Pertiga;
				PertigaX3 = Par[0]+Pertiga+1;
				PertigaY0 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PertigaY1 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PertigaY2 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PertigaY3 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PintaPertiga(PertigaX0+(PlayersWidth/2),PertigaY0,PertigaX1+(PlayersWidth/2),PertigaY1,PertigaX2+(PlayersWidth/2),PertigaY2+((ga.sinoidal-3)*(1+ga.ProtVX))/ga.ProtMaxV,PertigaX3+(PlayersWidth/2),PertigaY3+((ga.sinoidal-3)*(2+ga.ProtVX))/ga.ProtMaxV);
			}
		}
	}
	if (ga.ActualEvent == 6)
	{
		if (ga.Caer==false)
		{
			if (ga.ProtY==0)
			{
				
				PertigaX0 = Par[0];
				PertigaX1 = Par[0]+10;
				PertigaX2 = Par[0]+Pertiga;
				PertigaX3 = Par[0]+Pertiga+1;
				PertigaY0 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PertigaY1 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PertigaY2 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PertigaY3 = Par[1]+PerPos[ga.ActualPlayer][(ActFrame+1)%(PerPos[ga.ActualPlayer].length)];
				PintaPertiga(PertigaX0,PertigaY0,PertigaX1,PertigaY1,PertigaX2,PertigaY2+((ga.sinoidal-3)*(1+ga.ProtVX))/ga.ProtMaxV,PertigaX3,PertigaY3+((ga.sinoidal-3)*(2+ga.ProtVX))/ga.ProtMaxV);
			} else
			{
				if ((ga.ProtY<(Pertiga*10))&&(ga.ProtX<4000))
				{
					PertigaX0 = Par[0]+5;
					PertigaX1 = Par[0]+15;//(10*(ga.ProtPX-ga.ProtX))/(ga.ProtY);
					PertigaX2 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
					PertigaX3 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
					PertigaY0 = Par[1]+PerPos[ga.ActualPlayer][0];
					PertigaY1 = Par[1]+PerPos[ga.ActualPlayer][0]+5;
					PertigaY2 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-2;
					PertigaY3 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2);
					PintaPertiga(PertigaX0,PertigaY0,PertigaX1,PertigaY1,PertigaX2,PertigaY2,PertigaX3,PertigaY3);
				} else
				{
					PertigaX0 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
					PertigaX1 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
					PertigaX2 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
					PertigaX3 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
					PertigaY0 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-Pertiga;
					PertigaY1 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-Pertiga+2;
					PertigaY2 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-2;
					PertigaY3 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2);
					PintaPertiga(PertigaX0+((ga.sinoidal-3)*3)/2,PertigaY0,PertigaX1+ga.sinoidal-3,PertigaY1,PertigaX2,PertigaY2,PertigaX3,PertigaY3);
				}
			}
		} /*else
		{
			PertigaX0 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
			PertigaX1 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
			PertigaX2 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
			PertigaX3 = PerspectiveCenter+NX(ga.ProtPX/10,canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2))-ScrollPos-PlayersWidth;
			PertigaY0 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-Pertiga;
			PertigaY1 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-Pertiga+2;
			PertigaY2 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)-2;
			PertigaY3 = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2);
			PintaPertiga(PertigaX0+((ga.sinoidal-3)*3)/2,PertigaY0,PertigaX1+ga.sinoidal-3,PertigaY1,PertigaX2,PertigaY2,PertigaX3,PertigaY3);
		}*/
	}
	if (ga.ActualPlayer==4) PintaPj(ga.ActualPlayer,ActAnim,ActFrame);
}

public void PintaPj(int Pj, int Anim, int State)
{
	int Desp=0;
	if (Pj==3) Desp = 2;
	if ((ga.ActualEvent==6)&&(ga.gameStatus!=GAME_EVENT_SELECT)&&(ga.gameStatus!=GAME_CREATE))
		spriteDraw(Imagenes[14+Pj],  Par[0], Par[1]+Desp, (6*Anim)+State, Pj);
	else spriteDraw(Imagenes[14+Pj],  Par[0]+(PlayersWidth/3), Par[1]+Desp,  (6*Anim)+State, Pj);
}

public void PintaFrutas(int C)
{
	if (ga.ActualEvent == 3)
	{
		int Ys;
		Ys = canvasHeight-(ScoresHeight+PistaHeight-(PistaHeight/NumCorrerCarrils));
		
		for (int i=(6*C); i<(6*(C+1)); i++)
		{
			Par[1] = Ys+((i/6)*(PistaHeight/NumCorrerCarrils));
			Par[0] = PerspectiveCenter+NX(150*(1+(i%6)),Par[1])-(FrutasWidth/2)-ScrollPos;
			Par[1] -= FrutasHeight+2;
			Pinta(6,3+FrutasType[i]);
		}
	}
}

public void PintaBarras(int X, int H)
{
	setC(0,0-DespY,canvasWidth,canvasHeight);
	Barras = true;
	BarrasX = X;
	BarrasH = H;
	int YI = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-6;
	int YF = YI+CarrilHeight+6;
	scr.setColor(0x000000);
	scr.fillRect(PerspectiveCenter+NX(X,YI)-ScrollPos,YI-(H+3)+DespY,3,H+3);
	scr.setColor(0xFFFFFF);
	scr.fillRect(PerspectiveCenter+NX(X,YI)-ScrollPos+1,YI-(H+3)+1+DespY,1,H+1);
	scr.drawLine(PerspectiveCenter+NX(X,YI)-ScrollPos+1,YI-H+DespY,PerspectiveCenter+NX(X,YF)-ScrollPos+1,YF-H-1+DespY);
}

public void PintaBarras2()
{
	setC(0,0-DespY,canvasWidth,canvasHeight);
	int X = BarrasX;
	int H = BarrasH;
	Barras = false;
	int YI = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-6;
	int YF = YI+CarrilHeight+6;
	scr.setColor(0x000000);
	scr.fillRect(PerspectiveCenter+NX(X,YF)-ScrollPos,YF-(H+5)+DespY,3,(H)+5);
	scr.setColor(0xFFFFFF);
	scr.fillRect(PerspectiveCenter+NX(X,YF)-ScrollPos+1,YF-(H+5)+1+DespY,1,(H)+3);
}

public void PintaColchoneta(int X)
{
	int YI = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight));
	Par[1] = YI;
	Par[0] = PerspectiveCenter+NX(X,Par[1])-ScrollPos;
	Par[1] -= ColchonetaHeight;
	Pinta(6,1);
	Par[0] += ColchonetaWidth;
	Pinta(6,1);
	Par[0] += ColchonetaWidth;
	Pinta(6,1);
	//#ifdef SMALLJAR
	//#endif
	Par[1] = YI+(CarrilHeight/2);
	Par[0] = PerspectiveCenter+NX(X,Par[1])-ScrollPos;
	Par[1] -= ColchonetaHeight;
	Pinta(6,1);
	Par[0] += ColchonetaWidth;
	Pinta(6,1);
	Par[0] += ColchonetaWidth;
	Pinta(6,1);
	//#ifdef SMALLJAR
	//#endif
	Par[1] = YI+CarrilHeight;
	Par[0] = PerspectiveCenter+NX(X,Par[1])-ScrollPos;
	Par[1] -= ColchonetaHeight;
	Pinta(6,1);
	Par[0] += ColchonetaWidth;
	Pinta(6,1);
	Par[0] += ColchonetaWidth;
	Pinta(6,1);
	//#ifdef SMALLJAR
	//#endif
}

public void PintaTierra(int X)
{
	int A = canvasHeight-(ScoresHeight+PistaHeight-(((CarrilHeight*3)/2)-(TierraHeight)))-4;
	Par[1] = A+(TierraHeight/2);
	Par[0] = PerspectiveCenter+NX(X,Par[1])-ScrollPos;
	while (Par[0]<(0-TierraWidth)) Par[0] += TierraWidth;
	Par[1] = A;
	for (int i=0; i<=(1+(canvasWidth/TierraWidth)); i++)
	{
		Pinta(6,2);
		Par[0] += TierraWidth;
	}
	Par[1] += (TierraHeight*3)/2;
	Par[0] = PerspectiveCenter+NX(X,Par[1])-ScrollPos;
	while (Par[0]<(0-TierraWidth)) Par[0] += TierraWidth;
	Par[1] -= TierraHeight/2;
	for (int i=0; i<=(1+(canvasWidth/TierraWidth)); i++)
	{
		Pinta(6,2);
		Par[0] += TierraWidth;
	}
	setC(0,0,canvasWidth,canvasHeight);
	scr.setColor(0xFFFFFF);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos-4,A+DespY,canvasWidth,A+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos-4,A+(TierraHeight*2)+DespY,canvasWidth,A+(TierraHeight*2)+DespY);
	
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos-4,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos-4,A+TierraHeight*2+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos-3,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos-3,A+TierraHeight*2+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos-2,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos-2,A+TierraHeight*2+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos-1,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos-1,A+TierraHeight*2+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos+0,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos+0,A+TierraHeight*2+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos+1,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos+1,A+TierraHeight*2+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos+2,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos+2,A+TierraHeight*2+DespY);
	scr.drawLine(PerspectiveCenter+NX(X,A)-ScrollPos+3,A+DespY,PerspectiveCenter+NX(X,A+(TierraHeight*2))-ScrollPos+3,A+TierraHeight*2+DespY);
}

public void PintaScores()
{
	setC(0,0-DespY,canvasWidth,canvasHeight);
	scr.setColor(0x000000);
	scr.fillRect(0,canvasHeight-ScoresHeight,canvasWidth,ScoresHeight);
	Par[0] = 1;
	Par[1] = canvasHeight-ScoresHeight+1-DespY;
	Pinta(8,0);
	if (ga.ProtVX<ga.ProtMaxV)
	{
		//#ifdef VI-TSM100
		//#else
			scr.fillRect(0+3+((VelBarWidth*ga.ProtVX)/ga.ProtMaxV),canvasHeight-ScoresHeight+3,VelBarWidth-((VelBarWidth*ga.ProtVX)/ga.ProtMaxV),VelBarHeight);
		//#endif
	} else
	{
		scr.setColor(0xFFFFFF);
		if ((ga.sinoidal%2)==0) scr.fillRect(0+3,canvasHeight-ScoresHeight+3,VelBarWidth,VelBarHeight);
	}
	if ((ga.gameStatus==GAME_PLAY)&&(ga.ActualTry<3)) PintaNumScores((canvasWidth-5), Par[1], ga.ActScore[ga.ActualTry]/(((ga.ActualEvent==0)||(ga.ActualEvent==3))?10:1));
}
/////////////

public void PintaFlechas()
{
	Par[1] = canvasHeight-(ScoresHeight+YArrows)-DespY;
	Par[0] = (canvasWidth-MarcadorWidth)/2;
	Pinta(8,1);
	for (int i = 0; i<ga.ArrowsV.length; i++)
	{
		if (ga.ArrowsV[i])
		{
			Par[1] = canvasHeight-(ScoresHeight+YArrows)+(MarcadorHeight-ArrowsHeight)/2;
			if (ga.ArrowsA[i])
			{
				Par[0] = (canvasWidth/2)-(ArrowsWidth/2)+((ga.ArrowsO[i])*(ArrowsWidth+1))+((ga.ArrowsO[i]>=1)?(((MarcadorWidth-ArrowsWidth)/2)+1):0);
				Pinta(13,(ga.ArrowsD[i])%5);
			} else
			{
				Par[0] = (canvasWidth-ArrowsWidth)/2;
				if (ga.ArrowsT[i]>0)
				{
					Par[1] += (5-ga.ArrowsT[i])*(YArrows/5);
					ga.ArrowsT[i]--;
					Pinta(13,(ga.ArrowsD[i])%5);
				} else
				{
					Par[1] -= (3+ga.ArrowsT[i])*(YArrows/6);
					ga.ArrowsT[i]++;
					Pinta(13,5);
				}
				if (ga.ArrowsT[i] == 0)
				{
					ga.ArrowsV[i] = false;
				}
			}
		}
	}
}

public void PintaAngulo()
{
	Par[1] = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2);
	Par[0] = PerspectiveCenter+NX(ga.ProtX/10,Par[1])-ScrollPos;
	if ((ga.ActualEvent==2)||(ga.ActualEvent==5)) Par[1] -= (PlayersHeight*2)/3;
	setC(0,0,canvasWidth,canvasHeight);
	scr.setColor(0xFF0000);
	scr.drawLine(Par[0],Par[1]+DespY,Par[0]+(AngleHeight*ga.cos(ga.ActAngle))/1024,Par[1]-(AngleHeight*ga.sin(ga.ActAngle))/1024+DespY);
	Par[1] -= AngleHeight;
	Pinta(11,1);
}

public void PintaItem()
{
	if (ga.ItemX!=0)
	{
		Par[1] = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2);
		Par[0] = PerspectiveCenter+NX(ga.ItemX/10,Par[1])-ScrollPos;
		if (ga.ActualEvent == 5)
		{
			int High = ga.ItemY/200;
			if (High<=0) High = 1;
			scr.setColor(0x222222);
			scr.drawLine(Par[0],Par[1]+DespY,Par[0]+(MelonWidth/High),Par[1]+DespY);
			Par[1] -= (ga.ItemY/10)+MelonHeight;
			Pinta(11,2+((Math.abs(ga.ItemX)/20)%4));
		}
		if (ga.ActualEvent == 2)
		{
			setC(0,0,canvasWidth,canvasHeight);
			int Desviacion=0;
			Desviacion = ga.ItemVY/10;
			if (Desviacion>(Pertiga-5)) Desviacion = Pertiga-5;
			PertigaX0 = Par[0]-(Pertiga-Math.abs(Desviacion))-ga.sinoidal;
			PertigaX1 = PertigaX0+1;
			PertigaX2 = Par[0];
			PertigaX3 = PertigaX2+1;
			scr.setColor(0x222222);
			scr.drawLine(PertigaX0,Par[1]+DespY,PertigaX2,Par[1]+DespY);
			Par[1] -= (ga.ItemY/10);
			PertigaY0 = Par[1]+2*Desviacion+ga.sinoidal;
			PertigaY1 = PertigaY0;
			PertigaY2 = Par[1];
			PertigaY3 = PertigaY2;
			PintaPertiga(PertigaX0,PertigaY0,PertigaX1,PertigaY1,PertigaX2,PertigaY2,PertigaX3,PertigaY3);
		}
		if (Par[1]<0)
		{
			Par[1]=0;
			Pinta(11,0);
		}
		//scr.setColor(0x0000FF);
		//scr.fillRect(Par[0],Par[1],5,5);
	}
}

public void PintaPertiga(int X0, int Y0, int X1, int Y1, int X2, int Y2, int X3, int Y3)
{
	setC(0,0,canvasWidth,canvasHeight);
	scr.setColor(0xDDDDDD);
	Curva(X0,Y0,X1,Y1,X2,Y2,X3,Y3);
	scr.drawLine(X0,Y0+DespY,X1,Y1+DespY);
	scr.drawLine(X1,Y1+DespY,PerX[0],PerY[0]+DespY);
	for (int i = 0; i<(MaxStep-1); i++)
	{
		scr.drawLine(PerX[i],PerY[i]+DespY,PerX[i+1],PerY[i+1]+DespY);
		if (ga.ProtY==0) scr.drawLine(PerX[i],PerY[i]+1+DespY,PerX[i+1],PerY[i+1]+1+DespY);
		else scr.drawLine(PerX[i]+1,PerY[i]+DespY,PerX[i+1]+1,PerY[i+1]+DespY);
	}
	scr.drawLine(X2,Y2+DespY,X3,Y3+DespY);
	scr.drawLine(PerX[MaxStep-1],PerY[MaxStep-1]+DespY,X2,Y2+DespY);
	if (ga.ProtY==0) 
	{
		scr.drawLine(X0,Y0+1+DespY,X1,Y1+1+DespY);
		scr.drawLine(X1,Y1+1+DespY,PerX[0],PerY[0]+1+DespY);
		scr.drawLine(X2,Y2+1+DespY,X3,Y3+1+DespY);
		scr.drawLine(PerX[MaxStep-1],PerY[MaxStep-1]+1+DespY,X2,Y2+1+DespY);
	} else
	{
		scr.drawLine(X0+1,Y0+DespY,X1+1,Y1+DespY);
		scr.drawLine(X1+1,Y1+DespY,PerX[0]+1,PerY[0]+DespY);
		scr.drawLine(X2+1,Y2+DespY,X3+1,Y3+DespY);
		scr.drawLine(PerX[MaxStep-1]+1,PerY[MaxStep-1]+DespY,X2+1,Y2+DespY);
	}
}

/////////////

public int NX(int X, int Y)
{
	return NX(X,Y,ScrollPos);
}

public int NX(int X, int Y, int C)
{
	return NX(X,Y,AcusePerspectivaM,AcusePerspectivaD,canvasHeight-(ScoresHeight+PistaHeight)-100,canvasHeight-(ScoresHeight+PistaHeight-(3*CarrilHeight)),C);
}

public int NX(int X, int Y, int M, int D, int IY, int MY, int C)
{
	int Mul = 1;
	int Div = 1;
	int AX = (X-C);
	int AY = Y-IY;
	int DY = MY-IY;
	
	Mul *= AY*AcusePerspectivaM;
	Div *= DY*AcusePerspectivaD;
	
	return (C+((AX*Mul)/Div));
}

public int Exp(int B, int E)
{
	int Temp = 1;
	for (int i=0; i<E; i++) Temp *= B;
	return Temp;
}

public void Curva(int X0, int Y0, int X1, int Y1, int X2, int Y2, int X3, int Y3)
{
	int PX,PY,QX,QY,RX,RY;
	PX = (X3-X2)-(X0-X1);
	PY = (Y3-Y2)-(Y0-Y1);
	QX = (X0-X1)-PX;
	QY = (Y0-Y1)-PY;
	RX = (X2-X0);
	RY = (Y2-Y0);
	/*
	PX <<= 8;
	PY <<= 8;
	QX <<= 8;
	QY <<= 8;
	RX <<= 8;
	RY <<= 8;
	X1 <<= 8;
	Y1 <<= 8;
	*/
	
	int t = 0;
	for (int i=1; i<MaxStep+1; i++)
	{
		t = (i<<12)/(MaxStep+2);
		
		int t2 = t*t >> 12;
		int t3 = t*t2 >> 12;
		
		PerX[i-1] = ((PX*t3+QX*t2+RX*t)>>12)+X1;
		PerY[i-1] = ((PY*t3+QY*t2+RY*t)>>12)+Y1;
		//PerX[i-1] = (((PX*Exp(i,3)))+((QX*Exp(i,2)))+((PX*i)))+X1;
		//PerY[i-1] = (((PY*Exp(i,3)))+((QY*Exp(i,2)))+((PY*i)))+Y1;
	}
}
/*  function Cubic_Interpolate(v0, v1, v2, v3,x)
	P = (v3 - v2) - (v0 - v1)
	Q = (v0 - v1) - P
	R = v2 - v0
	S = v1

	return Px3 + Qx2 + Rx + S
  end of function
*/
public void PintaBurbujas()
{
	for (int Counter=0; Counter<ga.butterFlyV.length; Counter++)
	{
		if (ga.butterFlyV[Counter]) 
		{
			Par[1] = canvasHeight-(ScoresHeight+PistaHeight-(CarrilHeight))-4 + (CarrilHeight/2)+ga.butterFlyY[Counter]-(ga.butterFlyH/2);
			Par[0] = PerspectiveCenter+NX(ga.butterFlyX[Counter],Par[1])-ScrollPos-(ga.butterFlyH/2)-(PlayersWidth/2);
			Pinta(10,ga.butterFlyF[Counter]/2);
		}
	}	
}

public void initFrutas()
{
	for (int i = 0; i<FrutasType.length; i++) FrutasType[i] = (byte)ga.RND(3);
}

public void setC(int X1, int X2, int X3, int X4)
{
//#ifdef J2ME
	scr.setClip(X1,X2+DespY,X3,X4+DespY);
//#endif
}

// <=- <=- <=- <=- <=-
	
// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//
	static final int TEXT_PLAIN =	0x000;
	static final int TEXT_BOLD =	0x010;
	static final int TEXT_SMALL =	0x000;
	static final int TEXT_MEDIUM =	0x100;
	static final int TEXT_LARGER =	0x200;
	static final int TEXT_LEFT =	0x000;
	static final int TEXT_RIGHT =	0x001;
	static final int TEXT_HCENTER =	0x002;
	static final int TEXT_TOP =	0x000;
	static final int TEXT_BOTTON =	0x004;
	static final int TEXT_VCENTER =	0x008;
	static final int TEXT_OUTLINE =	0x1000;
	
	int printerX = 0;
	int printerY = 0;
	int printerSizeX = getWidth();
	int printerSizeY = getHeight();
	int canvasWidth=   getWidth();
	int canvasHeight=  getHeight();
	
	int ProgBarBackGroundColor=0xFFFFFF;
	int ProgBarColor=0xFF0000;
	int ProgBarBorderColor=0x000000;
	int BackGroundColor=0xFFFFFF;
	int MenuTextBorderLineColor=0x000000;
	int MenuTextColor=0xFFFFFF;
	
	//#ifdef DOJA
	//#elifdef SMALLJAR
	//#else
	int MaxDir = 20;
	int MaxImg = 9;
	//#endif
	
	Image[][] Imagenes = new Image[MaxDir][MaxImg];


	final static int GAME_LOGOS = 0;
	final static int GAME_MENU_INIT_MAIN = 9;
	final static int GAME_MENU_MAIN = 10;
	final static int GAME_MENU_INIT_SECOND = 24;
	final static int GAME_MENU_SECOND = 25;
	final static int GAME_MENU_GAMEOVER = 30;
	final static int GAME_CREATE = 20;
	final static int GAME_INIT = 40;
	final static int GAME_RETURN = 50;
	final static int GAME_COUNTDOWN = 55;
	final static int GAME_PLAY = 60;
	final static int GAME_ANGLE = 61;
	final static int GAME_NEW_OLIMPIX = 100;
	final static int GAME_CONTINUE_OLIMPIX = 110;
	final static int GAME_TRAIN = 200;
	final static int GAME_PRE_PLAYER_SELECT = 400;
	final static int GAME_PLAYER_SELECT = 410;
	final static int GAME_PRE_TEXT_INTRO = 149;
	final static int GAME_TEXT_INTRO = 150;
	final static int GAME_PRE_EVENT_SELECT = 500;
	final static int GAME_EVENT_SELECT = 510;
	final static int GAME_PRE_SHOW_HELP = 600;
	final static int GAME_SHOW_HELP = 610;
	final static int GAME_PRE_SHOW_ABOUT = 601;
	final static int GAME_SHOW_ABOUT = 611;
	final static int GAME_PRE_EVENT_END = 619;
	final static int GAME_EVENT_END = 620;
	final static int GAME_PRE_NEXT = 629;
	final static int GAME_NEXT = 630;
	final static int GAME_TRY_END = 35;
	final static int GAME_PRE_END = 700;
	final static int GAME_END = 710;

	
	final static int TEXT_EVENT_INFO = 25;
	
	//final static String SPDirectory="poyoolimpix";
	//#ifdef SOUND 
	final boolean deviceSound = true;		// Terminal con Sonido
	//#else
	//#endif
	//#ifdef VIBRA 
	final boolean deviceVibra = true;		// Terminal con Vibracion
	//#else
	//#endif
	final boolean deviceLight = true;		// Terminal con opciones de Luz en LCD ON/OFF
	final boolean deviceMenus = false;		// Terminal con Texto inferior para menus (CommandListener / softKeyMenu)
	boolean AnimFinalized = false;
	int ActFrame = 0;
	int ActAnim = 0;
	int LastAnim = ActAnim;
	int [] Par = new int[2];
	
	boolean PintaMapa = false;
	int PintaEvent = -1;
	
	//#ifdef NE-N410i
	//#elifdef BIGCANVAS
	final static int NumCorrerCarrils=5;
	//#elifdef SE-T6xx
	//#else
	//#endif
	
	byte[] FrutasType = new byte[6*NumCorrerCarrils];
	
	//ESCENARIO
	
	//#ifdef BIGGRAFICS
	final static int MarcadorWidth=26;
	final static int MarcadorHeight=26;
	final static int ScoresHeight = 10;
	final static int WallHeight = 20;
	final static int PublicoHeight = 20;
	final static int GrassHeight = 20;
	//#ifdef NE-410i
	//#else
	final static int CarrilHeight = 30;
	final static int PistaHeight = 90;
	//#endif
	final static int NubesHeight = 20;
	final static int PublicoWidth = 173;
	final static int VallasWidth = 173;
	final static int BanderillaHeight = 16;
	final static int BanderillaTextWidth = 5;
	final static int BanderillaTextHeight = 7;
	final static int FrutasHeight = 22;
	final static int FrutasWidth = 22;
	final static int ColchonetaWidth = 85;
	final static int ColchonetaHeight = 22;
	final static int TierraHeight = 14;
	final static int TierraWidth = 88;
	final static int Pertiga = 54;
	final static int AngleWidth = 53;
	final static int AngleHeight = 53;
	final static int YArrows = 40;
	final static int ArrowsWidth = 22;
	final static int ArrowsHeight = 22;
	final static int MelonHeight = 14;
	final static int MelonWidth = 14;
	final static int MaxStep = 5;
	final static int VelBarWidth=66;
	final static int VelBarHeight=4;
	final static int PajaroWidth=14;
	final static int PajaroHeight=14;
	final static int HumoWidth=22;
	final static int HumoHeight=22;
	
	final static int mapaWidth = 135;
	final static int mapaHeight = 108;
	final static int[] MapaX = new int[] {43,91,92,109,53,77};
	final static int[] MapaY = new int[] {40,4,38,32,73,59};
	final static int BanderasWidth = 22;
	final static int BanderasHeight = 16;
	final static int PlayersWidth = 43;
	final static int PlayersHeight = 43;
	
	final static int []NumFramesP = new int[] {30,27,32,38,35,28};
	final static int [][]MaxFrame = new int[][] 
		{
			{3,5,3,2,2,1,3,1,2,4,1,3,4,1,3,5,1,2},
			{3,6,3,2,1,1,4,1,1,6,1,6,4,1,1,6,1,1},
			{3,5,3,2,1,3,3,1,2,5,1,4,4,1,4,5,1,1},
			{3,5,4,2,1,1,2,1,3,3,1,2,4,1,1,5,1,3},
			{2,4,6,3,2,1,3,2,1,6,1,6,4,1,1,4,1,1},
			{2,6,4,2,1,2,4,1,4,4,1,4,4,1,1,5,1,1}
		};
	final static int [][]PerPos = new int [][]
		{
			{29,29,27,30,26,31},
			{15,33,30,27,26,30,30},
			{8,18,14,11,12,18},
			{17,15,15,15,15,15},
			{34,19,19,19,19,19},
			{23,17,17,11,11,12}
		};
		
	final static int AcusePerspectivaM = 14;
	final static int AcusePerspectivaD = 4;
	//#elifdef SMALLGRAFICS

	//#endif
	int TierraPos = 0;
	int NubesPos = 0;
	int PajaroPos = 0;
	int VallasPos = 0;

	int ScrollPos=0;
	int PerspectiveCenter = (canvasWidth*10)/11;
		
	//PLAYERS
	
	int[] PerX = new int[MaxStep];
	int[] PerY = new int[MaxStep];
	int PertigaX0,PertigaY0,PertigaX1,PertigaY1,PertigaX2,PertigaY2,PertigaX3,PertigaY3;
	boolean Barras=false;
	int BarrasX,BarrasH;
	
	byte[][] Coord;
	final static int ANIM_LANZA_J = 17;
	int DespY = 0;
	//#ifdef DOJA
		//#include src-temp/dirjuego.sux
		//#endinclude
	//#endif
};