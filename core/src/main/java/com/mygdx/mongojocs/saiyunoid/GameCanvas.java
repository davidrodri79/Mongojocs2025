package com.mygdx.mongojocs.saiyunoid;

// -----------------------------------------------
// SAIYU Game Base iMode v1.1 Rev.2 (6.2.2004)
// ===============================================
// Canvas iMode
// ------------------------------------






// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.iapplicationemu.AudioPresenter;
import com.mygdx.mongojocs.iapplicationemu.Canvas;
import com.mygdx.mongojocs.iapplicationemu.Connector;
import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.Font;
import com.mygdx.mongojocs.iapplicationemu.Frame;
import com.mygdx.mongojocs.iapplicationemu.Graphics;
import com.mygdx.mongojocs.iapplicationemu.HttpConnection;
import com.mygdx.mongojocs.iapplicationemu.IApplication;
import com.mygdx.mongojocs.iapplicationemu.Image;
import com.mygdx.mongojocs.iapplicationemu.MediaImage;
import com.mygdx.mongojocs.iapplicationemu.MediaListener;
import com.mygdx.mongojocs.iapplicationemu.MediaManager;
import com.mygdx.mongojocs.iapplicationemu.MediaPresenter;
import com.mygdx.mongojocs.iapplicationemu.MediaSound;
import com.mygdx.mongojocs.iapplicationemu.PhoneSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class GameCanvas extends Canvas implements MediaListener
{

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Variables para customizar el Juego dependiendo del Device
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

final int ChatisMAX=5;
final int FasesMAX=10;

// -------------------------------------------

final int VidasX=19;
final int VidasY=2;
final int VidasAddX=8;

// -------------------------------------------

final int ParedSizeX=10;	// Tablero de X Ladrillos Horizontales
final int ParedSizeY=20;	// Tablero de Y Ladrillos Verticales

// -------------------------------------------

final int PalaJordi = 2;

final int PalaCoorY = 12;

final int PalaSpeed = 6;

// -------------------------------------------

final int BolaSpeed = 4;		// DEBE ser PAR

// -------------------------------------------

final int CapsulaConta = 8;		// Cada x Tocho sale una Capsula

final int CapsulaSpeed = 2;

// -------------------------------------------

int DisparoAdd1 = 3;
int DisparoAdd2 = 13;

final int DisparoSpeed = 6;

// -------------------------------------------

int[] SpritesObj = new int[] {
	11,37,   14, 8,		// (0) Capsula A
	11,41,   14, 8,		// (1) Capsula B
	11,45,   14, 8,		// (2) Capsula C
	11,49,   14, 8,		// (3) Capsula D
	91,55,    2, 4,		// (4) Laser
	93,55,    5, 5,		// (5) Bola
	11,53,    6, 6,		// (6) Pala Inicio
	35,53,    6, 6,		// (7) Pala Final
	17,53,    8, 6,		// (8) Pala Cuerpo
	42,53,   16, 6,		// (9) Pala Disparo
	 0,0,   162,11,		// (A) Marco A
	 0,9,   11,180,		// (B) Marco B
	119,9,  11,180,		// (C) Marco C
	97,55,    6, 3,		// (D) Vida
	11,30,   14, 8,		// (E) Tochos
	11,23,   14, 8,		// (F) Destello
	12,77,   16,16,		//(10) Rejilla

	12,63,  82,13,		//(11) Logo "Sexnoid"
	11,11,   4, 6,		//(12) Font
};

// -------------------------------------------

int[] PalaSizes = new int[4];

int PalaSizeY;

int PalaY;

int BolaSizeX;
int BolaSizeY;

int TochoSizeX;
int TochoSizeY;

int FaseTopX;		// Esquina-Izquierda X del Tablero
int FaseTopY;		// Esquina-Izquierda Y del Tablero

// -------------------------------------------

public int ObjX(int Spr) {return SpritesObj[(Spr*4)+0];}
public int ObjY(int Spr) {return SpritesObj[(Spr*4)+1];}
public int ObjSizeX(int Spr) {return SpritesObj[(Spr*4)+2];}
public int ObjSizeY(int Spr) {return SpritesObj[(Spr*4)+3];}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<






Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga)
{
	this.ga = ga;
	canvasCreate();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void gameDraw()
{
	canvasShow=true;
	repaint();
	//while (canvasShow==true) {try {Thread.sleep(1);} catch(Exception e) {}}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Refresco de la pantalla (repaint)
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics scr;

public void paint(Graphics g)
{       
	SoundRUN();

	if (canvasShow)
	{
	canvasShow=false;

	g.lock();

	scr = g;

	canvasDraw();

	ProgBarIMP(g);
	FS_ErrorDraw(g, canvasWidth, canvasHeight);

	g.unlock(true);
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Gestion de Eventos (Teclado, Timers, etc...)
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int keyX, keyY, keyMisc, keyMenu;

public void processEvent(int type, int param)
{
	if (type == Display.KEY_RELEASED_EVENT)
	{
		keyX=0;
		keyY=0;
		keyMisc=0;
		keyMenu=0;
	}
	else if (type == Display.KEY_PRESSED_EVENT)
	{
		switch (param)
		{
		case Display.KEY_SOFT1:
		case Display.KEY_SOFT2:
			keyMenu=-1;
		return;

		case Display.KEY_UP:	keyY=-1; return;
		case Display.KEY_DOWN:	keyY= 1; return;
		case Display.KEY_LEFT:	keyX=-1; return;
		case Display.KEY_RIGHT: keyX= 1; return;
		case Display.KEY_SELECT:keyMisc= 5; keyMenu= 2; return;

		case Display.KEY_0: keyMisc=10; return;
		case Display.KEY_1: keyMisc=1; keyX=-1; keyY=-1; return;
		case Display.KEY_2: keyMisc=2; keyY=-1; return;
		case Display.KEY_3: keyMisc=3; keyX= 1; keyY=-1; return;
		case Display.KEY_4: keyMisc=4; keyX=-1; return;
		case Display.KEY_5: keyMisc=5; return;
		case Display.KEY_6: keyMisc=6; keyX= 1; return;
		case Display.KEY_7: keyMisc=7; return;
		case Display.KEY_8: keyMisc=8; keyY= 1; return;
		case Display.KEY_9: keyMisc=9; return;
		}
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Rutinas de Impresion en pantalla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void imageDraw(Image Img)
{
	imageDraw(Img, (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ---------------------------------------------------------

public void imageDraw(Image Img, int X, int Y)
{
	scr.drawImage(Img, X,Y);
}

// ---------------------------------------------------------
/*
public void PutSprite(Image Img[],  int X, int Y,  int Frame)
{
	scr.drawImage (Img[Frame], X, Y);

	sc.ScrollUpdate(X,Y, Img[Frame].getWidth(), Img[Frame].getHeight() );
}

// ---------------------------------------------------------

public void PutSprite(Image[] Img,  int X, int Y,  int Frame, byte[] Coor)
{
	int Frame2=Frame;

	Frame*=6;
	X+=Coor[Frame++];
	Y+=Coor[Frame++];

	scr.drawImage (Img[Frame2], X, Y);

	sc.ScrollUpdate(X,Y, Coor[Frame++],Coor[Frame++]);
}
*/
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas de Entrada y Salida de Medios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Image[] loadImage(String FileName, int Frames)
{
	Image Img[] = new Image[Frames];
	for (int i=0 ; i<Frames ; i++) {Img[i] = loadImage(FileName+i+".gif");}
	return Img;
}

// ---------------------------------------------------------

public Image loadImage(String FileName)
{
	MediaImage mimage = MediaManager.getImage("resource://"+FileName);

	try {
		mimage.use();
	} catch (Exception ui) {}

	return mimage.getImage();
}

// ---------------------------------------------------------

public Image loadImage(int Pos)
{
	MediaImage mimage = MediaManager.getImage("scratchpad:///0;pos="+Pos);

	try {
		mimage.use();
	} catch (Exception e) {/*Handle UI problem here*/}

	return mimage.getImage();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas de 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


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

boolean canvasShow=false;
boolean canvasFillDrawPaint=false;
int canvasFillDrawRGB;
Image canvasImg;

// -------------------
// Canvas INI
// ===================

public void canvasCreate()
{
	canvasFillInit(0xffffff);
	canvasImg = loadImage("/Loading.gif");

	canvasShow = true;
	repaint();
}


// -------------------
// Canvas SET
// ===================

public void canvasInit()
{
	ProgBarINI( 94, 6,  (canvasWidth/2)-47, (canvasHeight/2)+16 );

	ProgBarSET(0,6);

	if ( FS_Create("saiyunoid/Data") ) {ga.terminate();}	// Descargamos FileSystem de Internet

	SoundINI(); ProgBarADD();	//	Cargamos e inicializamos TODOS los sonidos


/*
	FontImg = loadImage("/3_Font/", 43); ProgBarADD();
	SprsImg = loadImage("/2_Sprites/", 40); ProgBarADD();
	LogoImg = loadImage("/0_Data/2_Logo.gif"); ProgBarADD();
	MarcImg = loadImage("/0_Data/3_Marcos.gif"); ProgBarADD();
*/
	SprsImg = FS_LoadImage(2); ProgBarADD();
	LogoImg	= FS_LoadImage(0,2); ProgBarADD();
	MarcImg	= FS_LoadImage(0,3); ProgBarADD();

	ga.loadFile(SprsCor, "/Sprites.cor"); ProgBarADD();

	ProgBarEND();


	canvasFillInit(0xFFFFFF);
//	canvasImg = loadImage("/0_Data/1_Caratula.gif");
	canvasImg = FS_LoadImage(0,1);


	System.gc();

	BolaSizeX=ObjSizeX(5);
	BolaSizeY=ObjSizeY(5);

	PalaSizeY=ObjSizeY(8);

	TochoSizeX=ObjSizeX(0xE);
	TochoSizeY=ObjSizeY(0xE);

	ga.ParedPixeX=ParedSizeX*TochoSizeX;
	ga.ParedPixeY=ParedSizeY*TochoSizeY;

	PalaY = (ParedSizeY*TochoSizeY) -PalaSizeY -PalaCoorY;

	PalaSizes[0]=ObjSizeX(6) +  ObjSizeX(8)    + ObjSizeX(7);	// (0) Pala Peque�a
	PalaSizes[1]=ObjSizeX(6) + (ObjSizeX(8)*2) + ObjSizeX(7);	// (1) Pala Mediana
	PalaSizes[2]=ObjSizeX(6) + (ObjSizeX(8)*3) + ObjSizeX(7);	// (2) Pala Grande
	PalaSizes[3]=ObjSizeX(6) + ObjSizeX(9) + ObjSizeX(7);	// (3) Pala Disparo

	FaseTopX=ObjSizeX(0xB);		// Esquina-Izquierda X del Tablero
	FaseTopY=ObjSizeY(0xA);		// Esquina-Izquierda Y del Tablero

	DisparoAdd1+=ObjSizeX(6);
	DisparoAdd2+=ObjSizeX(6);


	setSoftLabel(Frame.SOFT_KEY_1, "Menu");
	setSoftLabel(Frame.SOFT_KEY_2, "Menu");
}

// -------------------
// Canvas Fill
// ===================

public void canvasFillInit(int RGB)
{
	canvasFillDrawRGB = RGB;
	canvasFillDrawPaint = true;
}

public void canvasFillDraw(int RGB)
{
	scr.setColor(RGB & 0xFFFFFF);
	scr.fillRect(0,0, canvasWidth,canvasHeight );
}



String canvasTextStr;
int canvasTextX;
int canvasTextY;
int canvasTextRGB;
int canvasTextMode;
boolean canvasTextShow = false;

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
	Font f = Font.getFont(Font.FACE_PROPORTIONAL | ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) | ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) ));
	scr.setFont(f);

	Y += f.getAscent();

	if ((Mode & TEXT_HCENTER)!=0) {X+=( canvasWidth-f._stringWidth(Str) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {X+=  canvasWidth-f._stringWidth(Str) ;}

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( canvasHeight-f.getHeight() )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  canvasHeight-f.getHeight() ;}

	if ((Mode & TEXT_OUTLINE)!=0)
	{
	scr.setColor(0);
	scr.drawString(Str,  X-1, Y-1);
	scr.drawString(Str,  X+1, Y-1);
	scr.drawString(Str,  X-1, Y+1);
	scr.drawString(Str,  X+1, Y+1);
	}

	scr.setColor(RGB);
	scr.drawString(Str,  X, Y);
}

// <=- <=- <=- <=- <=-



// -------------------
// Canvas IMP
// ===================

public void canvasDraw()
{
	if (canvasFillDrawPaint) { canvasFillDrawPaint=false; canvasFillDraw(canvasFillDrawRGB); }

	if (canvasImg!=null) { imageDraw(canvasImg); canvasImg.dispose(); canvasImg=null; System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }

// -------------------------

	if (ga.jugarShow) { ga.jugarShow=false; jugarDraw_Gfx(); }

	if (ga.ma!=null && ga.ma.Marco_ON!=0) { ga.ma.MarcoIMP(scr); }
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
// Adaptacion: iMode - Rev.0 (28.11.2003)
// ===================
// *******************

AudioPresenter SoundPlayer;
MediaSound[] Sound;
int SoundOld = -1;
int SoundLoop;

// -------------------
// Sound INI
// ===================

public void SoundINI()
{
	SoundPlayer = AudioPresenter.getAudioPresenter();

	Sound = new MediaSound[5];

	for (int i=0 ; i<Sound.length ; i++) {Sound[i] = LoadSound(1,i);}

	SoundPlayer.setMediaListener(this);
}

// -------------------
// Sound SET
// ===================

public void SoundSET(int Ary, int Loop)
{
	SoundRES();

	if (ga.gameSound==0) {return;}

	try
	{
		SoundPlayer.setSound(Sound[Ary]);
		SoundPlayer.play();
		SoundOld=Ary;
		SoundLoop=Loop-1;
	}
	catch(Exception e) {}
}

// -------------------
// Sound RES
// ===================

public void SoundRES()
{
	if (SoundOld != -1)
	{
		try
		{
			SoundPlayer.stop();
			SoundOld = -1;
		}
		catch(Exception e) {}
	}
}

// -------------------
// Sound RUN
// ===================

public void SoundRUN()
{
	if ( Vibra_ON && (System.currentTimeMillis() - VibraTimeIni) > VibraTimeFin)
	{
	Vibra_ON=false;
		try {
		PhoneSystem.setAttribute(PhoneSystem.DEV_VIBRATOR, PhoneSystem.ATTR_VIBRATOR_OFF);
		} catch (Exception e) {}
	}
}

// -------------------
// Vibra SET
// ===================

boolean Vibra_ON;
long VibraTimeIni;
int VibraTimeFin;

public void VibraSET(int Time)
{
	if (ga.gameVibra==0) {return;}

	VibraTimeIni = System.currentTimeMillis();
	VibraTimeFin = Time;
	Vibra_ON=true;

	try
	{
		PhoneSystem.setAttribute(PhoneSystem.DEV_VIBRATOR, PhoneSystem.ATTR_VIBRATOR_ON);
	}
	catch (Exception e) {}
}


// -------------------
// mediaAction para controlar Start, Stop, Complete y as� hacer loops
// ===================

public void mediaAction(MediaPresenter mp, int type, int value)
{
	if (SoundOld!=-1 && mp == SoundPlayer && type == AudioPresenter.AUDIO_COMPLETE)
	{
		if (SoundLoop>0) {SoundLoop--;}

		if (SoundLoop!=0)
		{
			try {
				SoundPlayer.play();
			} catch(Exception e) {}
		}
	}
}


public MediaSound LoadSound(int Pos)
{
	MediaSound msound = MediaManager.getSound("scratchpad:///0;pos="+Pos);

	try {
		msound.use();
	} catch (Exception e) {}

	return msound;
}


public MediaSound LoadSound(int Pos, int SubPos) {
	//Pos = (FS_Data[Pos] / 4) + SubPos;
	//return LoadSound(FS_Data[Pos]);
	// // MONGOFIX

	MediaSound s = new MediaSound();
	s.music = Gdx.audio.newMusic(Gdx.files.internal(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".mid"));
	return s;
}

// <=- <=- <=- <=- <=-









// <=- <=- <=- <=- <=-


// *************************
// -------------------------
// iMode Microjocs FileSystem v2.0 - Engine - Rev.7 (26.1.2004)
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

	public boolean FS_Create(String FileName) {
		FS_Head = FS_LoadData(0, 0x30);

		if (FS_Head == null || FS_Head[0] != 0x4D || FS_Head[1] != 0x49) {
			int Retry = 5;
			while (true) {
				if (Retry-- == 0) {
					FS_ErrorCreate();
					return true;
				}

				byte[] Bufer = FS_DownloadData(FileName + ".mfs");

				if (Bufer == null || Bufer.length != 0x30) {
					FS_Error = 1;
					continue;
				}    // Controlamos que el Size sea correcto.

				int Checksum = Bufer[0x0E];
				Bufer[0x0E] = 0;
				if (Checksum != FS_Checksum(Bufer, 0, Bufer.length)) {
					FS_Error = 2;
					continue;
				}    // Comprobamos Checksum

				FS_SaveData(0, Bufer);
				FS_Head = FS_LoadData(0, 0x30);
				break;
			}
		}

		ProgBarINS(FS_Head[0x20]);    // Agregamos numero de bloques para cargar en el Slider
		FS_Notify();                // Se ha descargado un .mfs Ok (HEAD)

		int BankSize = ((FS_Head[0x22] & 0xFF) << 24) | ((FS_Head[0x23] & 0xFF) << 16) | ((FS_Head[0x24] & 0xFF) << 8) | (FS_Head[0x25] & 0xFF);

		for (int i = 0; i < FS_Head[0x21]; i++) {
			FS_Notify();
		}

		for (int i = FS_Head[0x21]; i < FS_Head[0x20]; i++) {
			int Retry = 5;
			while (true) {
				if (Retry-- == 0) {
					FS_ErrorCreate();
					return true;
				}

				byte[] Bufer = FS_DownloadData(FileName + i + ".mfs");

				if (Bufer == null || Bufer.length != BankSize) {
					FS_Error = 1;
					continue;
				}    // Controlamos que el Size sea correcto.

				if (FS_Head[0x26 + i] != FS_Checksum(Bufer, 0, Bufer.length)) {
					FS_Error = 2;
					continue;
				}    // Comprobamos Checksum

				FS_SaveData(FS_Head.length + (i * BankSize), Bufer);
				FS_SaveData(0x21, new byte[]{(byte) (i + 1)});
				break;
			}
			FS_Notify();    // Se ha descargado un .mfs Ok (DATA)
		}

// Cargamos la "FAT" del MFS
// -------------------------
		int Pos = ((FS_Head[0x14] & 0xFF) << 24) | ((FS_Head[0x15] & 0xFF) << 16) | ((FS_Head[0x16] & 0xFF) << 8) | (FS_Head[0x17] & 0xFF);
		int Size = ((FS_Head[0x18] & 0xFF) << 24) | ((FS_Head[0x19] & 0xFF) << 16) | ((FS_Head[0x1A] & 0xFF) << 8) | (FS_Head[0x1B] & 0xFF);

		FS_Data = new int[(Size / 4)];

		try {

			//InputStream in = Connector.openInputStream("scratchpad:///0;pos=" + Pos);
			// MONGOFIX
			File file = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder, "scratchpad");
			FileInputStream in = new FileInputStream(file);
			in.skip(Pos);

			for (int i = 0; i < FS_Data.length; i++) {
				short s;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] = (s & 0xFF) << 24;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] |= (s & 0xFF) << 16;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] |= (s & 0xFF) << 8;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] |= (s & 0xFF);
			}

			in.close();
		} catch (IOException e) {
		}

		FS_Error = 0;
		return false;
	}


// ---------------
// FS_LoadData
// ===============

	public byte[] CargaFichero(String f)
	{
		FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+f);
		byte[] bytes = file.readBytes();

		return bytes;
	}

	public byte[] FS_LoadData(int Pos, int Size)
	{
	/*
	byte[] Bufer = new byte[Size];

	try {
		InputStream in = Connector.openInputStream("scratchpad:///0;pos="+Pos);
		in.read(Bufer,0,Bufer.length);
		in.close();
	} catch (IOException e) {return null;}

	return Bufer;
*/

	/*File file = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder, "scratchpad");

	if(file.exists())
	{
		try {
			FileInputStream fis = new FileInputStream(file);
			byte b[] = new byte[Size];
			fis.read(b, Pos, Size);
			return b;
		}
		catch(IOException e)
		{
			return null;
		}
	}
	else return null;*/

		RandomAccessFile file;

		try {
			file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder + "/scratchpad", "r");

			byte b[] = new byte[Size];

			try {
				file.seek(Pos);
				file.read(b, 0, Size);
				file.close();
				return b;

			} catch (IOException e2) {
				e2.printStackTrace();
				return null;
			}

		}catch(FileNotFoundException e)
		{
			return null;
		}
	}

// ---------------
// FS_SaveData
// ===============

	public int FS_SaveData(int Pos, byte[] Bufer)
	{
	/*try {
		OutputStream out=Connector.openOutputStream("scratchpad:///0;pos="+Pos);
		out.write(Bufer,0,Bufer.length);
		out.close();
	} catch (Exception e) {return 0;}

	return Bufer.length;*/

		RandomAccessFile file;
		try {
			file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder + "/scratchpad", "rw");

		}catch(FileNotFoundException e)
		{
			File dirs = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder);
			dirs.mkdirs();
			try {
				file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder+ "/scratchpad", "rw");

			} catch (IOException e2) {
				e2.printStackTrace();
				return 0;
			}
		}

		try {
			file.seek(Pos);
			file.write(Bufer);
			return Bufer.length;

		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
/*
	if(!file.())
	{
		File dirs = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder);
		dirs.mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	try {
		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(Bufer, Pos, Bufer.length);
		fos.close();
		return Bufer.length;

	} catch(Exception e)
	{
		return 0;
	}*/
	}

// ---------------
// FS_LoadFile
// ===============

	public byte[] FS_LoadFile(int Pos, int SubPos)
	{
		Pos = (FS_Data[Pos] / 4 ) + SubPos;

		return FS_LoadData(FS_Data[Pos], FS_Data[Pos+1] - FS_Data[Pos]);
	}


// ---------------
// FS_SaveFile
// ===============

	public void FS_SaveFile(int Pos, int SubPos, byte[] Bufer)
	{
		Pos = (FS_Data[Pos] / 4 ) + SubPos;

		FS_SaveData(FS_Data[Pos], Bufer);
	}


// ---------------
// FS_LoadImage
// ===============

	public Image[] FS_LoadImage(int Pos)
	{
		int Ini = FS_Data[Pos];
		int Size = (FS_Data[Pos+1]-Ini)/4;

		Ini/=4;

		Image[] Img = new Image[Size];

		for (int i=0 ; i<Size ; i++) {
			Img[i] = Image.createImage(IApplication.assetsFolder+"/"+Pos+"/"+i+".gif");
			//Img[i] = loadImage(FS_Data[Ini++]);
		}

		return Img;
	}


// ---------------
// FS_LoadImage
// ===============

	public Image FS_LoadImage(int Pos, int SubPos)
	{
		Image im = Image.createImage(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".gif");
		return im;

		//return loadImage(FS_Data[(FS_Data[Pos]/4)+SubPos]);
	}


// ---------------
// FS_DownloadData
// ===============

	public byte[] FS_DownloadData(String Filename)
	{
		FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+Filename);
		byte[] bytes = file.readBytes();

		return bytes;
	/*byte[] dat = null;

	int Retry=5;
	boolean FileOk = false;

	do {
		HttpConnection conn = null;
		try {
			conn=(HttpConnection)(Connector.open(IApplication.getCurrentApp().getSourceURL()+"../mfs_file/" + Filename, (Integer) Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://www.iwapserver.com/microjocs/games/mfs_file/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://mjj.no-ip.biz:8085/microjocs/games/mfs_file/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://www.arrakis.es/%7ejoanant/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open(IApplication.getCurrentApp().getSourceURL() + Filename, Connector.READ));
			conn.setRequestMethod(HttpConnection.GET);
			conn.connect();

			try {
				InputStream in=conn.openInputStream();
				dat = new byte[(int)conn.getLength()];
				in.read(dat);
				in.close();
				FileOk = true;
			} catch (Exception e) {}

		} catch (Exception e) {}

		try {
			if (conn!=null) {conn.close(); conn=null;}
		} catch (Exception e) {}

		if (!FileOk && --Retry==0) {return null;}
	}
	while (!FileOk);

	return dat;*/
	}

// ---------------
// FS_Checksum
// ===============

	public byte FS_Checksum(byte[] Bufer, int Pos, int Size)
	{
		int Checksum=0;
		for (int i=0 ; i<Size ; i++) {Checksum+=Bufer[Pos+i]; Checksum = (Checksum^i)&0xFF;}
		return (byte)(Checksum & 0xFF);
	}

// ---------------
// FS_Notify - Notifica que un Bloque ha sido leido OK, PONER aqu� el "repaint()" para actualizar el 'Slider' de Descarga.
// ===============

	public void FS_Notify()
	{
		ProgBarADD();
	}

// ---------------
// FS_Error Create
// ===============

	public void FS_ErrorCreate()
	{
		FS_ErrorShow = true;

		long time = System.currentTimeMillis();

		while ((System.currentTimeMillis()-time) < 16000 )
		{
			canvasShow=true; repaint();
			try {Thread.sleep(2000);} catch (Exception e) {}
		}
	}

// ---------------
// FS_Error Draw
// ===============

	public void FS_ErrorDraw(Graphics g, int cSizeX, int cSizeY)
	{
		if (FS_ErrorShow)
		{
			g.setColor(0xFFFFFF);
			g.fillRect(0,0, cSizeX, cSizeY);
			g.setColor(0x0);

			Font f = Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
			g.setFont(f);
			int y = f.getAscent();
			int height = f.getHeight();

//	for (int i=0 ; i<FS_strError.length ; i++) {g.drawString(FS_strError[i], 4, y); y += height;}

			for (int i=0 ; i<FS_strError.length ; i++)
			{
				int Pos=0, PosIni=0, PosOld=0, Size=0;

				while ( PosOld < FS_strError[i].length() )
				{
					Size=0;

					Pos=PosIni;

					while ( Size < (cSizeX-6) )
					{
						if ( Pos == FS_strError[i].length() ) {PosOld=Pos; break;}

						int Dat = FS_strError[i].charAt(Pos++);
						if (Dat==0x20) {PosOld=Pos-1;}

						Size += f.stringWidth(new String(new char[] {(char)Dat}));
					}

					if (PosOld-PosIni < 1) { while ( Pos < FS_strError[i].length() && FS_strError[i].charAt(Pos) >= 0x30 ) {Pos++;} PosOld=Pos; }

					g.drawString(FS_strError[i].substring(PosIni,PosOld), 3, y); y += height;

					PosIni=PosOld+1;
				}
			}
		}
	}

	String[] FS_strError = new String[] {
			"- ERROR -",
			" ",
			"- Si es la primera vez que ejecuta esta aplicaci�n, aseg�rese de tener activado el acceso a la red.",
			" ",
			"- Posible problema de cobertura GPRS. Int�ntelo de nuevo m�s tarde.",
	};

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
//	Gfx.fillRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+6, ProgBarSizeY+6);
//	Gfx.setColor(0);
//	Gfx.drawRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+5, ProgBarSizeY+5);
	Gfx.setColor(0xffffff);
	Gfx.fillRect(ProgBarX, ProgBarY, ProgBarSizeX, ProgBarSizeY);
	Gfx.setColor(0x0000AA);
	Gfx.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);
	}
}

// <=- <=- <=- <=- <=-


















public void PutSprite(Image[] Img,  int X, int Y,  int Frame, byte[] Coor, boolean Refresh)
{
	int Frame2 = Frame;

	Frame*=6;
	X+=Coor[Frame++];
	Y+=Coor[Frame++];

	try
	{
		scr.drawImage (Img[Coor[Frame+2]], X, Y);
	} catch (Exception e) {}


	if (Refresh)
	{
	RestData[RestPos][0] = (short) X;
	RestData[RestPos][1] = (short) Y;
	RestData[RestPos][2] = Coor[Frame++];
	RestData[RestPos][3] = Coor[Frame++];
	RestData[RestPos][4] = (short) Frame2;
	RestPos++;
	}

}











// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


int RestPos=0;
short[][] RestData = new short[64][5];


byte[] SprAjust = new byte[] {0,5,10,15,20,21,22,23,24,25,-1,-1,-1,29,30,35,-1,-1,-1};
byte[] SprClear = new byte[] {0,0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1, 0,-1,-1,-1};

public void ImageSET(Image Sour, int Coor[], int Frame, int Add, int DestX, int DestY)
{
	switch (Frame)
	{
	case 0xA:
		ImageSET(MarcImg, DestX, DestY);
	break;	

	case 0xB:
	case 0xC:
	break;

	case 0x11:
		ImageSET(LogoImg, DestX, DestY);
	break;	

	default:
		int Frame2 = SprAjust[Frame];
		if (Frame2<0) {break;}

		PutSprite(SprsImg, DestX, DestY, Frame2+Add, SprsCor, SprClear[Frame]==0);
	break;
	}


/*
	Frame<<=2;

	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	int SizeY=Coor[Frame++];

	SourX+=(Add*SizeX);

	scr.setClip(0, 0, canvasWidth, canvasHeight);
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY);
*/
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int Coor[], int Frame, int Add, int DestX, int DestY,  int TopX, int TopY, int FinX, int FinY)
{
	ImageSET(Sour, Coor, Frame, Add, DestX, DestY);

/*
	Frame<<=2;

	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	int SizeY=Coor[Frame++];

	SourX+=(Add*SizeX);

	scr.setClip(TopX, TopY, FinX, FinY);
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY);
*/
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int DestX, int DestY)
{
	scr.drawImage(Sour, DestX, DestY);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	scr.drawImage(Sour, DestX-SourX, DestY-SourY);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY);
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------








// *******************
// -------------------
// Jugar - Engine - Gfx
// ===================
// *******************

Image SpritesImg;

byte[] SprsCor = new byte[240];

Image[] SprsImg;

Image LogoImg;
Image MarcImg;

boolean JugarTodoPaint = false;

// -------------------
// Jugar INI Gfx
// ===================

public void jugarCreate_Gfx()
{
}


// -------------------
// Jugar END Gfx
// ===================

public void jugarDestroy_Gfx()
{
}


// -------------------
// Jugar SET Gfx
// ===================

public void jugarInit_Gfx()
{
	JugarTodoPaint = true;
}


// -------------------
// Jugar RES Gfx
// ===================

public void jugarRelease_Gfx()
{
}


// ----------------------
// Jugar Tocho Update Gfx
// ======================

public void TochoUpdate_Gfx(int X, int Y, int Tocho)
{
}


// -------------------
// Jugar IMP Gfx
// ===================

public void jugarDraw_Gfx()
{

	if (JugarTodoPaint)
	{
		JugarTodoPaint = false;
		RestPos = 0;

		canvasFillDraw(0);

	// Imprimimos Marco
	// -------------------
		ImageSET(SpritesImg, SpritesObj, 0xA,0,  0,0);
		ImageSET(SpritesImg, SpritesObj, 0xB,0,  0,SpritesObj[(0xA*4)+3]);
		ImageSET(SpritesImg, SpritesObj, 0xC,0,  FaseTopX+ga.ParedPixeX, SpritesObj[(0xA*4)+3]);

	// Imprimimos Vidas
	// --------------------
		for (int i=0 ; i<ga.JugarVidas ; i++)
		{
		ImageSET(SpritesImg, SpritesObj, 0xD,0, VidasX+(i*VidasAddX),VidasY);
		}

	// Imprimimos Tochos
	// -------------------
		for (int y=0 ; y< ParedSizeY ; y++)
		{
			for (int x=0 ; x< ParedSizeX ; x++)
			{
				int Tocho=ga.ParedMapa[(y*ParedSizeX)+x];
				if (Tocho != 0)
				{
				ImageSET(SpritesImg, SpritesObj, 0xE, Tocho-1,  FaseTopX+(x*TochoSizeX), FaseTopY+(y*TochoSizeY));
				}
			}
		}

		RestPos = 0;

	}

	scr.setColor(0);
	for (int i=0 ; i<RestPos ; i++)
	{
	scr.fillRect(RestData[i][0],RestData[i][1],RestData[i][2],RestData[i][3]);
// =--
	int mx = ((RestData[i][0]-FaseTopX)/TochoSizeX);
	int my = ((RestData[i][1]-FaseTopY)/TochoSizeY);

	int dir = (  my * ParedSizeX) + mx;
	int Tocho = ga.ParedMapa[ dir ]; if (Tocho != 0) {ImageSET(SpritesImg, SpritesObj, 0xE, Tocho-1,  FaseTopX+(mx*TochoSizeX), FaseTopY+(my*TochoSizeY));}
// =--
	int dirOld = dir;

	mx = ((RestData[i][0]+BolaSizeX-1-FaseTopX)/TochoSizeX);
	my = ((RestData[i][1]-FaseTopY)/TochoSizeY);
	dir = (  my * ParedSizeX) + mx;
	if (dir != dirOld) {Tocho = ga.ParedMapa[ dir ]; if (Tocho != 0) {ImageSET(SpritesImg, SpritesObj, 0xE, Tocho-1,  FaseTopX+(mx*TochoSizeX), FaseTopY+(my*TochoSizeY));}}
// =--
	dirOld = dir;

	mx = ((RestData[i][0]-FaseTopX)/TochoSizeX);
	my = ((RestData[i][1]+BolaSizeY-1-FaseTopY)/TochoSizeY);
	dir = (  my * ParedSizeX) + mx;
	if (dir != dirOld) {Tocho = ga.ParedMapa[ dir ]; if (Tocho != 0) {ImageSET(SpritesImg, SpritesObj, 0xE, Tocho-1,  FaseTopX+(mx*TochoSizeX), FaseTopY+(my*TochoSizeY));}}
// =--
	dirOld = dir;

	mx = ((RestData[i][0]+BolaSizeX-1-FaseTopX)/TochoSizeX);
	my = ((RestData[i][1]+BolaSizeY-1-FaseTopY)/TochoSizeY);
	dir = (  my * ParedSizeX) + mx;
	if (dir != dirOld) {Tocho = ga.ParedMapa[ dir ]; if (Tocho != 0) {ImageSET(SpritesImg, SpritesObj, 0xE, Tocho-1,  FaseTopX+(mx*TochoSizeX), FaseTopY+(my*TochoSizeY));}}
// =--
	}
	RestPos=0;



// Imprimimos Destellos
// --------------------
	for (int i=0 ; i<ga.DestelloMAX ; i++)
	{
		if (ga.Destellos[i]!=null)
		{
		ImageSET(SpritesImg, SpritesObj, 0xF, ga.Destellos[i][2], FaseTopX+ga.Destellos[i][0], FaseTopY+ga.Destellos[i][1]);
		}
	}


// Imprimimos Capsulas
// -------------------
	for (int i=0 ; i<ga.CapsulaMAX ; i++)
	{
		if (ga.Capsulas[i]!=null)
		{
		ImageSET(SpritesImg, SpritesObj, ga.Capsulas[i][3], ga.Capsulas[i][2], FaseTopX+ga.Capsulas[i][0], FaseTopY+ga.Capsulas[i][1]);
		}
	}


// Imprimimos Disparos
// -------------------
	for (int i=0 ; i<ga.DisparoMAX ; i++)
	{
		if (ga.Disparos[i]!=null)
		{
		if (ga.Disparos[i][2] != 0) {ImageSET(SpritesImg, SpritesObj, 4, 0, FaseTopX+ga.Disparos[i][0]+DisparoAdd1, FaseTopY+ga.Disparos[i][1]);}
		if (ga.Disparos[i][3] != 0) {ImageSET(SpritesImg, SpritesObj, 4, 0, FaseTopX+ga.Disparos[i][0]+DisparoAdd2, FaseTopY+ga.Disparos[i][1]);}
		}
	}


// Imprimimos Bolas
// ----------------
	for (int i=0 ; i<ga.BolaMAX ; i++)
	{
		if (ga.Bolas[i]!=null)
		{
		ImageSET(SpritesImg, SpritesObj, 5, 0,  FaseTopX+ga.Bolas[i][0], FaseTopY+ga.Bolas[i][1]);
		}
	}


// Imprimimos Pala
// ---------------

	SpritesObj[(8*4)+2] = ga.PalaSizeX - (SpritesObj[(6*4)+2] + SpritesObj[(7*4)+2]);
	SpritesObj[(9*4)+2] = SpritesObj[(8*4)+2];


	if (!ga.PalaNormal)
	{
		ImageSET(SpritesImg, SpritesObj,  9, ga.PalaFrame,  -((16-SpritesObj[(8*4)+2])/2) + ( FaseTopX+ga.PalaX+SpritesObj[(6*4)+2]), FaseTopY+PalaY-PalaJordi);
	} else {

		int Size = (SpritesObj[(8*4)+2] / 6) + 1;

		for (int i=0 ; i<Size ; i++)
		{
		ImageSET(SpritesImg, SpritesObj,  8, ga.PalaFrame,  (i*6)+FaseTopX+ga.PalaX+SpritesObj[(6*4)+2], FaseTopY+PalaY-PalaJordi);
		}
	}

	ImageSET(SpritesImg, SpritesObj,  6,0,  FaseTopX+ga.PalaX, FaseTopY+PalaY-PalaJordi);

	ImageSET(SpritesImg, SpritesObj,  7,0,  FaseTopX+ga.PalaX+ga.PalaSizeX-SpritesObj[(7*4)+2], FaseTopY+PalaY-PalaJordi);

};

// <=- <=- <=- <=- <=-




// -------------------
// Back - Create
// ===================

public void BackCreate(int ChatisY, int RejillaSizeY, int Rejilla)
{
}

// -------------------
// Chatis - Create
// ===================

public void ChatisCreate()
{
}

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
} // Cierra la Clase GameCanvas
// **************************************************************************//