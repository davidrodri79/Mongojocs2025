package com.mygdx.mongojocs.torapia;


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

Bios ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public BiosCanvas(Bios ga)
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

public void hideNotify() 
{
	ga.gameStatus = ga.GAME_MENU_SECOND;
	ga.gamePaused=true;
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
static final int TEXT_TOP =	0x000;
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
	LoadingImg = new Image();
	LoadingImg._createImage("/Loading.png");
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
	if (ga.biosStatus==22 && ga.menuListUpdate) ga.playShow = true;
	Draw(scr);
//	if (ga.menuShow) { ga.menuShow=false; menuDraw(); }
	scr.setClip(0,0,canvasWidth,canvasHeight);
	ga.menuListDraw(scr);
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
	Sonidos = new Player[9];

	Sonidos[0] = SoundCargar("/1/0.mid");
	Sonidos[1] = SoundCargar("/1/1.mid");
	Sonidos[2] = SoundCargar("/1/2.mid");
	Sonidos[3] = SoundCargar("/1/3.mid");
	Sonidos[4] = SoundCargar("/1/4.mid");
	Sonidos[5] = SoundCargar("/1/5.mid");
	Sonidos[6] = SoundCargar("/1/6.mid");
	Sonidos[7] = SoundCargar("/1/7.mid");
	Sonidos[8] = SoundCargar("/1/8.mid");
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
	Retorno = ga.loadFile("/"+Pos+"/"+SubPos+".dat"); 
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

//public void canvasInit() {}
//public void Draw(Graphics a) {}

// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//

int MaxDir = 22;
int MaxImg = 7;

Image[][] Imagenes = new Image[MaxDir][MaxImg];

public Image[] CargaImagen(int Dir, int Img)
{
	if (Img<0) 
	{
		Img*=-1;
		return FS_LoadImage(Dir,(byte)Img);
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
		FS_LoadImage(Dir,(byte)Img);
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
		Imagenes[Dir][Img] = CargaImagen(Dir,Img)[0];
	}
}

public void CargaI(int Dir, int Img, boolean ProgBarFinalized)
{
	if (Img<0)
	{
		Imagenes[Dir] = CargaImagen(Dir,Img);
	} else
	{
		Imagenes[Dir][Img] = CargaImagen(Dir,Img)[0];
	}
	if (ProgBarFinalized) ProgBarEND(); else ProgBarADD();
}

public void Vacia(int Dir, int Img,boolean GarbageColector)
{
	Imagenes[Dir][Img] = null;
	if (GarbageColector) System.gc();
}

public void Vacia(int Dir,boolean GarbageColector)
{
	Imagenes[Dir] = null;
	Imagenes[Dir] = new Image[MaxImg];
	if (GarbageColector) System.gc();
}

public void Vacia(boolean GarbageColector)
{
	Imagenes = null;
	Imagenes = new Image[MaxDir][MaxImg];
	if (GarbageColector) System.gc();
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

//public GameCanvas(Bios ga) {super(ga);}

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
final boolean deviceSound = true;
final boolean deviceVibra = false;
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
	ProgBarINI( 94, 6,  (canvasWidth/2)-47, (canvasHeight/2)+16 );

	ProgBarSET(0,9+10);
	
	sc = new Scroll();
	sc.ScrollINI(canvasWidth,canvasHeight);
	
	ProgBarADD();
	soundInit();
	
	CargaI(0,-2,false);
	CargaI(3,-2,false);
	
	CargaI(5,-7,false);
	
	CargaEnems();

	CargaI(7,-1,false);
	CargaI(8,-1,false);
	CargaI(9,-6,false);
	CargaI(10,-1,false);
	CargaI(11,-1,true);
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
	sc.ScrollSET(ga.MapInfo, ga.NumTilesX, ga.NumTilesY,  Imagenes[5][(ga.ActualLevel/7)],8);
}

public void CargaEnems()
{
	for (int i=0; i<ga.EnemU.length; i++) 
	{
		//if (ga.EnemU[i])
		//{
			CargaI(12+i,-1,false);
		//}
	}
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
Image LoadingImg;
int ActFrame = 0;
public void playDraw()
{
	ActFrame++;
	if (ActFrame>=4) ActFrame=0;
	switch (ga.gameStatus)
	{
		case 0 :
			scr.setColor(0xFFFFFF);
			scr.setClip(0,0,canvasWidth,canvasHeight);
			scr.fillRect(0,0,canvasWidth,canvasHeight);
			showImage(LoadingImg);
		break;
		case 42 :
			PintaFondo(-1);
			if (ga.timeToChange!=ga.introTimeToChange) PintaCollar((byte)7);
		break;
		
		case 44 :
			PintaFondo(-1);
			if (ga.timeToChange!=ga.introTimeToChange)
			{
				if (ga.timeToChange>(ga.introTimeToChange-20))
				{
					PintaCollar((byte)7);
				} else
				{
					PintaCollar((byte)-1);
				}
			}
		break;
		
		case 10 :
		case 41 :
		case 43 :
		case 45 :
		case 70 :
		case 500 :
			PintaFondo(-1);
		break;
		
		case 85 :
		case 90 :
			PintaFondo(-1);
			textDraw(ga.gameText[ga.TEXT_LOADING][0],5,5,0xFFFFFF,0x210);
			textDraw(ga.gameText[ga.TEXT_LEVEL][0]+(ga.ActualLevel+1),5,35,0xFFFFFF,0x010);
			textDraw(ga.gameText[ga.TEXT_SITIOS][(ga.ActualLevel/7)+1],5,50,0xFFFFFF,0x010);
		break;
		
		case 95 :
		case 100 :
		case 250 :
		case 490 :
			PintaLevel();
			PintaRuta();
			//PintaCol();
			PintaItem();
			PintaPjs();
			PintaToro();
			PintaExplosiones();
			PintaLive();
		break;
		
		case 260 :
			PintaFondo(-1);
			showImage(Imagenes[0][1]);
			//textDraw(ga.gameText[ga.TEXT_SITIOS+1][0],0,0,0xFFFFFF,0x21A);
		break;
		
		case 80 :
		case 310 :
			PintaFondo(-1);
			if (Imagenes[3][0]!=null) PintaCollar((byte)(ga.ActualLevel/7));
		break;
		
		case 400 :
			PintaFondo(-1);
			PintaCollar((byte)8);
		break;
		
		case 610 :
			PintaFondo(-1);
			if (ga.SelectedLevel>0) 
			{
				PintaCollar((byte)(((ga.SelectedLevel-1)/7)+1-Math.abs((ga.sinoidal/2)%2)));
				scr.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[ga.TEXT_SITIOS][((ga.SelectedLevel-1)/7)+1],10,5,0xFFFFFF,0x010);
				textDraw("< "+ga.gameText[ga.TEXT_LEVEL][0]+ga.SelectedLevel+" >",10,canvasHeight-25,0xFFFFFF,0x010);
			} else 
			{
				PintaCollar((byte)0);
				scr.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[ga.TEXT_SITIOS][0],0,0,0xFFFFFF,0x01A);
			}
		break;
		
		default :
			showImage(Imagenes[0][0]);
			//PintaFondo(-1);
		break;
	}

}

public void PintaPjs()
{
	byte[] Orden = new byte[ga.EnemRemain+1];
	byte Pos=0;
	for (int i=0; i<ga.NumTilesY; i++)
	{
		for (int j=0; j<ga.EnemX.length; j++)
		{
			if ((ga.EnemA[j])&&(ga.EnemCY[j]==i))
			{
				Orden[Pos] = (byte)j;
				Pos++;
			}
		}
		if (ga.ProtCY==i)
		{
			Orden[Pos] = -1;
			Pos++;	
		}
	}
	for (int i=0; i<Orden.length; i++)
	{
		if (Orden[i]==-1) PintaProt();
		else PintaEnem(Orden[i]);
	}
}

public void PintaCol()
{
	scr.setClip(0,0,canvasWidth,canvasHeight);
	scr.setColor(0xFF0000);
	for (int i=0; i<ga.Colision.length; i++)
	{
		if (ga.Colision[i]>=4) scr.fillRect((i%ga.NumTilesX)*ga.TileW-sc.ScrollX,(i/ga.NumTilesX)*ga.TileH-sc.ScrollY,ga.TileW,ga.TileH);	
	}	
}

public void PintaEnem(byte i)
{
	if (ga.EnemA[i])
	{
		Par[0] = ga.EnemX[i]-(ga.EnemW[i]/2)-sc.ScrollX;
		Par[1] = ga.EnemY[i]-(ga.EnemH[i]/2)-6-sc.ScrollY;
		Pinta(12+ga.EnemT[i],ga.EnemF[i]*4+(ga.EnemD[i]+1)%4);
	}
}

// <=- <=- <=- <=- <=-

public void PintaFondo(int F)
{
	if (F<0) 
	{
		scr.setColor(0x000000);
		scr.fillRect(0,0,canvasWidth,canvasHeight);
	} else
	{
		
	}
}

int MaxTextos=10;
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
	}	
}

public void PintaTextos()
{
	for (int Counter=0; Counter<MaxTextos; Counter++) 
	{
		if (Textos[Counter]!=null)
		{
			if ((ga.gameStatus!=250)&&(ga.gameStatus!=260)) textDraw(Textos[Counter], TextosX[Counter], TextosY[Counter], TextosC[Counter], 0x1110);
			if (TextosT[Counter]>0) TextosT[Counter]--; else Textos[Counter]=null;
		} else
		{
			TextosT[Counter] = 0;
		}
	}
}

int [] Par = new int[2];
public void Pinta(int Dir, int Img, int[] Parameters)
{
	switch (Dir)
	{
		case 3 :
			if (Img == 0) showImage(Imagenes[Dir][Img], Parameters[0], Parameters[1]);
			else showImage(Imagenes[Dir][1],24*(Img-1),0,24,24,Parameters[0], Parameters[1]);
			//showImage(Imagenes[Dir][Img], Parameters[0], Parameters[1]);
		break;
		
		case 8 :
			showImage(Imagenes[Dir][0],ga.ProtW*(Img%4),ga.ProtH*(Img/4),ga.ProtW,ga.ProtH,Parameters[0], Parameters[1]);	
		break;
		
		case 9 :
			if (Img<=11)
			{
				showImage(Imagenes[Dir][0],ga.ToroW*(Img%4),ga.ToroH*(Img/4),ga.ToroW,ga.ToroH,Parameters[0], Parameters[1]);
			} else
			{
				if (Img<=13) 
				{
					showImage(Imagenes[Dir][1],ga.BandW*(Img-12),0,ga.BandW,ga.BandH,Parameters[0], Parameters[1]);
				} else 
				{
					showImage(Imagenes[Dir][Img-12],Parameters[0], Parameters[1]); 
				}
			}
		break;
		
		case 7 :
		case 11 :
			showImage(Imagenes[Dir][0],36*Img,0,36,36,Parameters[0], Parameters[1]);
		break;
		
		case 12 :
		case 13 :
		case 14 :
		case 15 :
		case 16 :
		case 17 :
		case 18 :
		case 19 :
		case 20 :
		case 21 :
			showImage(Imagenes[Dir][0],ga.ProtW*(Img%4),ga.ProtH*(Img/4),ga.ProtW,ga.ProtH,Parameters[0], Parameters[1]);
		break;
		
		case 10 :
			for (int i=0; i<=Img; i++)
			{
				showImage(Imagenes[Dir][0],Parameters[0]+(24*i), Parameters[1]);
			}
		break;
		
		default :
			if (Imagenes[Dir][Img] == null)System.out.println("Null: "+Dir+","+Img);
			showImage(Imagenes[Dir][Img], Parameters[0], Parameters[1]);
		break;
	}
}

public void Pinta(int Dir, int Img)
{
	Pinta(Dir,Img,Par);
}

public void PintaLevel()
{
	if (ga.ToroM) sc.ScrollRUN_Centro_Max(ga.ToroX,ga.ToroY); else sc.ScrollRUN_Centro_Max(ga.ProtX,ga.ProtY);
	sc.ScrollIMP(scr);
}

public void PintaRuta()
{
	if (ga.Band)
	{
		int CX,CY;
		CX = ga.BandCX;
		CY = ga.BandCY;
		for (int i=0; i<ga.Recorrido.length; i++)
		{
			if (ga.Recorrido[i]==-1)
			{
				i = ga.Recorrido.length;
			} else
			{
				if (ga.Recorrido[i]==0) CX++;
				if (ga.Recorrido[i]==1) CY--;
				if (ga.Recorrido[i]==2) CX--;
				if (ga.Recorrido[i]==3) CY++;
				if (i>=ga.ToroActD) 
				{
					Par[0] = CX*ga.TileW-sc.ScrollX;
					Par[1] = CY*ga.TileH-sc.ScrollY;
					Pinta(9,12);
				}
			}
		}
		Par[0] = ga.BandCX*ga.TileW+(ga.TileW/2)-(ga.BandW/2)-sc.ScrollX;
		Par[1] = ga.BandCY*ga.TileH+(ga.TileH)-3-ga.BandH-sc.ScrollY;
		Pinta(9,13);
	}
}

public void PintaItem()
{
	for (int Counter=0; Counter<ga.ItemV.length; Counter++)
	{
		if (ga.ItemV[Counter]) 
		{
			Par[0] = ga.ItemCX[Counter]*ga.TileW-sc.ScrollX;
			Par[1] = ga.ItemCY[Counter]*ga.TileH-sc.ScrollY;
			Pinta(3,ga.ItemT[Counter]);
		}
	}	
}

public void PintaToro()
{
	if (ga.ToroM)
	{
		if (ga.ToroMT<=ga.ToroV)
		{
			Par[0] = ga.ToroX-(ga.ToroW/2)-sc.ScrollX;
			Par[1] = ga.ToroY-(ga.ToroH)+(ga.TileH/2)-3-sc.ScrollY;
			Pinta(9,ga.ToroF*4+(ga.ToroD+1)%4);
			
			Par[0] = ga.ToroX-(ga.ToroW/2)-sc.ScrollX;
			Par[1] = ga.ToroY-(ga.ToroH/2)+(ga.TileH/2)-3-sc.ScrollY;
			if (ga.ToroD==0) 
			{
				Par[0] -= (ga.ToroW/2)+ga.RND(ga.TileW/2);
				Par[1] -= ga.RND(ga.TileH);
			}
			if (ga.ToroD==1) 
			{
				Par[0] += ga.RND(ga.TileW/2);
				Par[1] += ga.RND(ga.TileH/2);
			}
			if (ga.ToroD==2) 
			{
				Par[0] += (ga.ToroW/2)+ga.RND(ga.TileW/2);
				Par[1] -= ga.RND(ga.TileH/2);
			}
			if (ga.ToroD==3) 
			{
				Par[0] += ga.RND(ga.TileW/2);
				Par[1] -= (ga.TileH/2)+(ga.ToroH/2)+ga.RND(ga.TileH/2);
			}
			Pinta(7,ga.RND(2));
		} else
		{
			Par[0] = ga.ToroX-(132/2)-sc.ScrollX;
			Par[1] = ga.ToroY-(137/2)-sc.ScrollY;
			Pinta(9,17);
			if ((ga.ToroMT-ga.ToroV)==3)
			{
				Par[0] = ga.ToroX-(84/2)-sc.ScrollX;
				Par[1] = ga.ToroY-(135/2)-sc.ScrollY;
			}
			if ((ga.ToroMT-ga.ToroV)==2)
			{
				Par[0] = ga.ToroX-(65/2)-sc.ScrollX;
				Par[1] = ga.ToroY-(99/2)-sc.ScrollY;
			}
			if ((ga.ToroMT-ga.ToroV)==1)
			{
				Par[0] = ga.ToroX-(42/2)-sc.ScrollX;
				Par[1] = ga.ToroY-(65/2)-sc.ScrollY;
			}
			Pinta(9,13+(ga.ToroMT-ga.ToroV));
			Pinta(11,(ga.ToroMT-ga.ToroV));
		}
	}
}

public void PintaProt()
{
	Par[0] = ga.ProtX-(ga.ProtW/2)-sc.ScrollX;
	Par[1] = ga.ProtY-(ga.ProtH/2)-6-sc.ScrollY;
	if ((ga.gameStatus==490)||(ga.ToroM)||((ga.Band)&&(ga.Colision[ga.ProtCX+ga.ProtCY*ga.NumTilesX]==3)))
	  Pinta(8,4*3+(ga.ProtF%2)*4+(ga.ProtD+3)%4); else
	  Pinta(8,ga.ProtF*4+(ga.ProtD+3)%4);
}

public void PintaLive()
{
	Par[0] = 3;
	Par[1] = 3;
	Pinta(10,ga.Lives-1);
}


int CollarW = 135;
int CollarH = 135;
int Diamante1X=13; int Diamante1Y=5;
int Diamante2X=0; int Diamante2Y=43;
int Diamante3X=19; int Diamante3Y=81;
int Diamante4X=102; int Diamante4Y=6;
int Diamante5X=115; int Diamante5Y=43;
int Diamante6X=96; int Diamante6Y=81;
int Diamante7X=55; int Diamante7Y=109;
public void PintaCollar(byte NumDiamantes)
{
	if (NumDiamantes>=0)
	{
		Par[0] = (canvasWidth-CollarW)/2;
		Par[1] = (canvasHeight-CollarH)/2;
		Pinta(3,0);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		Par[0] = Diamante1X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante1Y+(canvasHeight-CollarH)/2;
		Pinta(3,1);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		Par[0] = Diamante2X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante2Y+(canvasHeight-CollarH)/2;
		Pinta(3,2);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		Par[0] = Diamante3X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante3Y+(canvasHeight-CollarH)/2;
		Pinta(3,3);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		Par[0] = Diamante4X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante4Y+(canvasHeight-CollarH)/2;
		Pinta(3,4);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		Par[0] = Diamante5X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante5Y+(canvasHeight-CollarH)/2;
		Pinta(3,5);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		Par[0] = Diamante6X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante6Y+(canvasHeight-CollarH)/2;
		Pinta(3,6);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		Par[0] = Diamante7X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante7Y+(canvasHeight-CollarH)/2;
		Pinta(3,7);
		if (NumDiamantes<=0) return; else NumDiamantes--;
		scr.setColor(0xFFFFFF);
		scr.setClip(0,0,canvasWidth,canvasHeight);
		scr.drawLine(Par[0]-ga.sinoidal*2+5,Par[1]-ga.sinoidal*2+5,Par[0]+ga.sinoidal*2+5,Par[1]+ga.sinoidal*2+5);
		scr.drawLine(Par[0]-ga.sinoidal*2+5,Par[1]+ga.sinoidal*2+5,Par[0]+ga.sinoidal*2+5,Par[1]-ga.sinoidal*2+5);
		scr.drawLine(Par[0]-(5-ga.sinoidal)*2+5,Par[1]+5,Par[0]+(5-ga.sinoidal)*2+5,Par[1]+5);
		scr.drawLine(Par[0]+5,Par[1]-(5-ga.sinoidal)*2+5,Par[0]+5,Par[1]+(5-ga.sinoidal)*2+5);
	} else
	{
		Par[0] = (canvasWidth-CollarW)/2;
		Par[1] = (canvasHeight-CollarH)/2;
		Pinta(3,0);
		Par[0] = Diamante1X+(canvasWidth-CollarW)/2-(ga.introTimeToChange-20-ga.timeToChange)*3;
		Par[1] = Diamante1Y+(canvasHeight-CollarH)/2-(ga.introTimeToChange-20-ga.timeToChange)*2;
		Pinta(3,1);
		Par[0] = Diamante2X+(canvasWidth-CollarW)/2-(ga.introTimeToChange-20-ga.timeToChange)*3;
		Par[1] = Diamante2Y+(canvasHeight-CollarH)/2;
		Pinta(3,2);
		Par[0] = Diamante3X+(canvasWidth-CollarW)/2-(ga.introTimeToChange-20-ga.timeToChange)*3;
		Par[1] = Diamante3Y+(canvasHeight-CollarH)/2+(ga.introTimeToChange-20-ga.timeToChange)*2;
		Pinta(3,3);
		Par[0] = Diamante4X+(canvasWidth-CollarW)/2+(ga.introTimeToChange-20-ga.timeToChange)*3;
		Par[1] = Diamante4Y+(canvasHeight-CollarH)/2-(ga.introTimeToChange-20-ga.timeToChange)*2;
		Pinta(3,4);
		Par[0] = Diamante5X+(canvasWidth-CollarW)/2+(ga.introTimeToChange-20-ga.timeToChange)*3;
		Par[1] = Diamante5Y+(canvasHeight-CollarH)/2;
		Pinta(3,5);
		Par[0] = Diamante6X+(canvasWidth-CollarW)/2+(ga.introTimeToChange-20-ga.timeToChange)*3;
		Par[1] = Diamante6Y+(canvasHeight-CollarH)/2+(ga.introTimeToChange-20-ga.timeToChange)*2;
		Pinta(3,6);
		Par[0] = Diamante7X+(canvasWidth-CollarW)/2;
		Par[1] = Diamante7Y+(canvasHeight-CollarH)/2+(ga.introTimeToChange-20-ga.timeToChange)*3;
		Pinta(3,7);
	}
}

public void PintaExplosiones()
{
	for (int Counter=0; Counter<ga.ExplV.length; Counter++)
	{
		if (ga.ExplV[Counter]) 
		{
			if (ga.ExplF[Counter]<6)
			{
				if (ga.gameStatus!=25) ga.ExplF[Counter]++;
				Par[0] = ga.ExplX[Counter]-(ga.ExplW/2)-sc.ScrollX;
				Par[1] = ga.ExplY[Counter]-(ga.ExplH/2)-sc.ScrollY;
				Pinta(11,(ga.ExplF[Counter]-1)/2);
			} else { ga.ExplV[Counter] = false; }
		}
	}	
}

Scroll sc;
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