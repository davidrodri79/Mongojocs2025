package com.mygdx.mongojocs.fbi;


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
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

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
	ProgBarIMP(scr);
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

public void hideNotify() {ga.gamePaused=true;}

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
	textDraw(textoBreak(Str, printerSizeX, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
}

public void textDraw(String Str, int X, int Y, int T, int RGB, int Mode)
{
	textDraw(_textoBreak(Str, T, Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) , ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ))), X, Y, RGB, Mode);
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


		if ((Mode & TEXT_OUTLINE)!=0)
		{
		scr.setColor(0);
		scr.drawString(Str[i],  X-1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y-1+despY , 20);
		scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);
		scr.drawString(Str[i],  X+1+despX, Y+1+despY , 20);
		}

		scr.setColor(RGB);
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
	}

}


// <=- <=- <=- <=- <=-






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

	ga.menuListDraw(scr);

}

// <=- <=- <=- <=- <=-











/* ===================================================================

	SoundINI()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	SoundSET(n� Sonido , Repetir)
	-----------------------------
		Hacemos que suene un sonido (0 a x) y que se repita x veces.
		Repetir == 0: Repeticion infinita

	SoundRES()
	----------
		Paramos el ultimo sonido.

	SoundRUN()
	----------
		Debemos ejecutar este metodo en CADA ciclo para gestionar 'tiempos'

	VibraSET(microsegundos)
	-----------------------
		Hacemos vibrar el mobil x microsegundos

=================================================================== */



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

public void soundInit()
{
	Sonidos = new Player[5];

	Sonidos[0] = SoundCargar("/1/0.MID");
	Sonidos[1] = SoundCargar("/1/1.MID");
	Sonidos[2] = SoundCargar("/1/2.MID");
	Sonidos[3] = SoundCargar("/1/3.MID");
	Sonidos[4] = SoundCargar("/1/4.MID");
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

}



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

	canvasShow=true;
	repaint();
	serviceRepaints();
	//while (ga.Move) {try {Thread.sleep(10);} catch (Exception e) {}}
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

int ProgBarBackColor  = 0xFFFFFF;
int ProgBarFrontColor = 0xAA0000;

public void ProgBarIMP(Graphics Gfx)
{
	if (ProgBar_ON)
	{
	ga.playShow = true;
//	Gfx.fillRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+6, ProgBarSizeY+6);
//	Gfx.setColor(0);
//	Gfx.drawRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+5, ProgBarSizeY+5);
	Gfx.setClip(0,0,canvasWidth,canvasHeight);
	Gfx.setColor(ProgBarBackColor);
	if (ProgBarBackColor!=0x000001) Gfx.fillRect(ProgBarX, ProgBarY, ProgBarSizeX, ProgBarSizeY);
	Gfx.setColor(ProgBarFrontColor);
	Gfx.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);
	}
}




// *************************
// -------------------------
// iMode Microjocs FileSystem v2.0 - Engine - Rev.8 (1.3.2004)
// =========================
// *************************

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
// FS_Notify - Notifica que un Bloque ha sido leido OK, PONER aqu� el "repaint()" para actualizar el 'Slider' de Descarga.
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

public void FS_ErrorDraw(Graphics g, int cSizeX, int cSizeY)
{

}

String[] FS_strError = new String[] {
"- ERROR -",
" ",
"- Si es la primera vez que ejecuta esta aplicaci�n, aseg�rese de tener activado el acceso a la red.",
" ",
"- Posible problema de cobertura GPRS. Int�ntelo de nuevo m�s tarde.",
};

// <=- <=- <=- <=- <=-





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