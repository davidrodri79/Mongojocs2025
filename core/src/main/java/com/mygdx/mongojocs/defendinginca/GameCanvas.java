package com.mygdx.mongojocs.defendinginca;

// -----------------------------------------------
// SAIYU Defending INCA v 1.0 (27.2.2004)
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

public void PutSprite(Image Img[],  int X, int Y,  int Frame)
{
	scr.drawImage (Img[Frame], X, Y);

//	sc.ScrollUpdate(X,Y, Img[Frame].getWidth(), Img[Frame].getHeight() );
}

// ---------------------------------------------------------

public void PutSprite(Image[] Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;
	X+=Coor[Frame++];
	Y+=Coor[Frame++];

	scr.drawImage (Img[Coor[Frame+2]], X, Y);

//	sc.ScrollUpdate(X,Y, Coor[Frame++],Coor[Frame++]);
}

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


// -------------------
// Canvas Fill
// ===================

boolean canvasFillShow=false;
int canvasFillRGB;

public void canvasFillInit(int RGB)
{
	canvasFillRGB = RGB;
	canvasFillShow = true;
}

public void canvasFillDraw(int RGB)
{
	scr.setColor(RGB & 0xFFFFFF);
	scr.fillRect(0,0, canvasWidth,canvasHeight );
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




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
static final int TEXT_TOP =	0x000;
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

	Sound = new MediaSound[8];

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

		MediaSound s = new MediaSound();
		s.music = Gdx.audio.newMusic(Gdx.files.internal(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".mid"));
		return s;
	}

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

	public boolean FS_Create(String FileName)
	{
		FS_Head = FS_LoadData(0,0x30);

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
			//InputStream in = Connector.openInputStream("scratchpad:///0;pos="+Pos);
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
		} catch (IOException e){}

		FS_Error=0;
		return false;
	}


// ---------------
// FS_LoadData
// ===============


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
// FS_loadImage
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
// FS_loadImage
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

int ProgBarBackColor  = 0xFFFFFF;
int ProgBarFrontColor = 0x0000AA;

public void ProgBarIMP(Graphics Gfx)
{
	if (ProgBar_ON)
	{
//	Gfx.fillRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+6, ProgBarSizeY+6);
//	Gfx.setColor(0);
//	Gfx.drawRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+5, ProgBarSizeY+5);
	Gfx.setColor(ProgBarBackColor);
	if (ProgBarBackColor!=0x000001) Gfx.fillRect(ProgBarX, ProgBarY, ProgBarSizeX, ProgBarSizeY);
	Gfx.setColor(ProgBarFrontColor);
	Gfx.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);
	}
}

// <=- <=- <=- <=- <=-



// -------------------------------------------------------------------------------------------
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// -------------------------------------------------------------------------------------------





// *******************
// -------------------
// Canvas - Engine
// ===================
// *******************

int canvasWidth=getWidth();
int canvasHeight=getHeight();

boolean canvasShow=false;
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

	ProgBarSET(0,2);

	if ( FS_Create("/defendinginca/Data") ) {ga.terminate();}	// Descargamos FileSystem de Internet

	SoundINI(); ProgBarADD();	//	Cargamos e inicializamos TODOS los sonidos





	ProgBarEND();

	setSoftLabel(Frame.SOFT_KEY_1, "Menu");
	setSoftLabel(Frame.SOFT_KEY_2, "Menu");
}


// -------------------
// Canvas IMP
// ===================

public void canvasDraw()
{
	if (canvasFillShow) { canvasFillShow=false; canvasFillDraw(canvasFillRGB); }

	if (canvasImg!=null) { imageDraw(canvasImg); canvasImg.dispose(); canvasImg=null; System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }

// -------------------------

	if (ga.jugarShow) { ga.jugarShow=false; jugarDraw_Gfx(); }

	if (ga.ma!=null && ga.ma.Marco_ON!=0) { ga.ma.MarcoIMP(scr); }
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Jugar - Engine - Gfx
// ===================
// *******************

// -------------------
// Jugar INI Gfx
// ===================

public void jugarCreate_Gfx()
{
	canvasImg = FS_LoadImage(0,3);

	Enem 		= null;
	eShot 		= null;
	shot 		= null;
	GameOver 	= null;
	Prot 		= null;
	smoke 		= null;
	expl 		= null;
	TileSet 	= null;
	MenuImg 	= null;
	sc = null;
	System.gc();
	
	ProgBarBackColor  = 0x000001;
	ProgBarFrontColor = 0xDF6C06;

	canvasShow = true;
	repaint();
	
	ProgBarINI( 101, 6,  31, 158 );

	ProgBarSET(0,10);

	Enem 		= FS_LoadImage(2);ProgBarADD();
	eShot 		= FS_LoadImage(3);ProgBarADD();
	shot 		= FS_LoadImage(4);ProgBarADD();
	GameOver 	= FS_LoadImage(5);ProgBarADD();
	Prot 		= FS_LoadImage(6);ProgBarADD();
	smoke 		= FS_LoadImage(7);ProgBarADD();
	expl 		= FS_LoadImage(8);ProgBarADD();
	TileSet 	= FS_LoadImage(9);ProgBarADD();
	MenuImg 	= FS_LoadImage(0,4);ProgBarADD();
	
	//ZonaFondo(0,0,160,128,ga.TramosFondo,0);
	sc = new Scroll();
	sc.ScrollINI(160,128,this);
	
	ProgBarEND();
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
	sc.ScrollSET(ga.MapInf, ga.MapSizeX, ga.MapSizeY,  TileSet);
}


// -------------------
// Jugar RES Gfx
// ===================

public void jugarRelease_Gfx()
{
}


// -------------------
// Jugar IMP Gfx
// ===================

Image expl[],smoke[],Prot[],Enem[],eShot[],shot[],GameOver[],TileSet[],MenuImg;
Scroll sc;


boolean Repintar;
int ActFrame=0;
int LastScrollY=0;

public void jugarDraw_Gfx()
{
	int ScrollY = 128*2 - sc.ScrollY;
	if (ScrollY != LastScrollY) sc.ScrollUpdate(0,0,canvasWidth,canvasHeight);
	LastScrollY = ScrollY;
	
	if (ActFrame==5) ActFrame=0; else ActFrame++;
	
	switch (ga.gameStatus)
	{
	    case 210 :
		if ((ga.protWin<ga.enemWin)&&(ga.CPU)) 
		{
			imageDraw(GameOver[0]);
			imageDraw(GameOver[2],(canvasWidth/2)-57+7+ga.timeToChange-84,15);
			imageDraw(GameOver[3],(canvasWidth/2)-57+121+20-ga.timeToChange,15);
		} else 
		{
			imageDraw(GameOver[1]);
			if (ga.protWin<ga.enemWin) textDraw(ga.menuText[17][0],0,73, 0xFFFFFF, 0x111A);
			else textDraw(ga.menuText[16][0],0 , 73, 0xFFFFFF, 0x111A);
			imageDraw(GameOver[2],(canvasWidth/2)-57+7+ga.timeToChange-84,2);
			imageDraw(GameOver[3],(canvasWidth/2)-57+121+20-ga.timeToChange,2);
		}
	    break;
	    default  :
		int ProtS = 0;
		if (ga.protA>(-10)) ProtS = 1;
		if (ga.protA>(20))  ProtS = 2;
		if (ga.protA>(50))  ProtS = 3;
		
		int EnemS = 0;
		if (ga.enemA>(-10)) EnemS = 1;
		if (ga.enemA>(20))  EnemS = 2;
		if (ga.enemA>(50))  EnemS = 3;
		
		//EXPLOSION ScrollUpdate_Now();
		//ZonaFondo(0,0,canvasWidth,DespY,7,((ScrollY<=255)?0:1));
		//ZonaFondo(0,0,160,128,ga.TramosFondo,0);
		//sc.ScrollUpdate_Now();
		sc.ScrollIMP(scr);
		if (ga.turno != 2) 
		{
			scr.drawImage(Prot[ProtS],ga.protX-(ga.protW/2)-sc.ScrollX,ga.protY-(ga.protH/2)-(256-ScrollY)+DespY);
			sc.ScrollUpdate(ga.protX-(ga.protW/2)-sc.ScrollX,ga.protY-(ga.protH/2)-(256-ScrollY)+DespY,ga.protW,ga.protH);
		} else 
		{
			scr.drawImage(Prot[4+ActFrame%2],ga.protX-(ga.protW/2)-sc.ScrollX,ga.protY-(ga.protH/2)-(256-ScrollY)+DespY-ga.Jump);
			sc.ScrollUpdate(ga.protX-(ga.protW/2)-sc.ScrollX,ga.protY-12-(256-ScrollY)-ga.Jump+DespY,ga.protW,24);
		}
		if (ga.turno != -2)
		{
			scr.drawImage(Enem[EnemS],ga.enemX-(ga.enemW/2)-sc.ScrollX,ga.enemY-(ga.enemH/2)-(256-ScrollY)+DespY);
			sc.ScrollUpdate(ga.enemX-(ga.enemW/2)-sc.ScrollX,ga.enemY-(ga.enemH/2)-(256-ScrollY)+DespY,ga.enemW,ga.enemH);
		} else
		{
			scr.drawImage(Enem[4+ActFrame%2],ga.enemX-(ga.enemW/2)-sc.ScrollX,ga.enemY-(ga.enemH/2)-(256-ScrollY)+DespY-ga.Jump);
			sc.ScrollUpdate(ga.enemX-(ga.enemW/2)-sc.ScrollX,ga.enemY-12-(256-ScrollY)-ga.Jump+DespY,ga.enemW,24);
			scr.drawImage(Enem[6+ActFrame%2],ga.enemX-(ga.enemW/2)-sc.ScrollX,ga.enemY-(ga.enemH/2)-(256-ScrollY)+DespY-ga.JumpC);
			sc.ScrollUpdate(ga.enemX-(ga.enemW/2)-sc.ScrollX,ga.enemY-12-(256-ScrollY)-ga.JumpC+DespY,ga.enemW,24);
		}
		
		if (ga.turno == 1)
		{
			scr.setColor(0xFF0000);
			scr.drawLine(ga.protX+(Trig.cos(ga.protA)*30/1024)-3-sc.ScrollX,ga.protY-(Trig.sin(ga.protA)*30/1024)-(256-ScrollY)+DespY,ga.protX+(Trig.cos(ga.protA)*30/1024)+3-sc.ScrollX,ga.protY-(Trig.sin(ga.protA)*30/1024)-(256-ScrollY)+DespY);
			scr.drawLine(ga.protX+(Trig.cos(ga.protA)*30/1024)-sc.ScrollX,ga.protY-(Trig.sin(ga.protA)*30/1024)-3-(256-ScrollY)+DespY,ga.protX+(Trig.cos(ga.protA)*30/1024)-sc.ScrollX,ga.protY-(Trig.sin(ga.protA)*30/1024)+3-(256-ScrollY)+DespY);
			sc.ScrollUpdate(ga.protX+(Trig.cos(ga.protA)*30/1024)-3-sc.ScrollX,ga.protY-(Trig.sin(ga.protA)*30/1024)-3-(256-ScrollY)+DespY,7,7);
		}
		
		if (ga.turno == -1)
		{
			scr.setColor(0xFF0000);
			scr.drawLine(ga.enemX-(Trig.cos(ga.enemA)*30/1024)-3-sc.ScrollX,ga.enemY-(Trig.sin(ga.enemA)*30/1024)-(256-ScrollY)+DespY,ga.enemX-(Trig.cos(ga.enemA)*30/1024)+3-sc.ScrollX,ga.enemY-(Trig.sin(ga.enemA)*30/1024)-(256-ScrollY)+DespY);
			scr.drawLine(ga.enemX-(Trig.cos(ga.enemA)*30/1024)-sc.ScrollX,ga.enemY-(Trig.sin(ga.enemA)*30/1024)-3-(256-ScrollY)+DespY,ga.enemX-(Trig.cos(ga.enemA)*30/1024)-sc.ScrollX,ga.enemY-(Trig.sin(ga.enemA)*30/1024)+3-(256-ScrollY)+DespY);
			sc.ScrollUpdate(ga.enemX-(Trig.cos(ga.enemA)*30/1024)-3-sc.ScrollX,ga.enemY-(Trig.sin(ga.enemA)*30/1024)-3-(256-ScrollY)+DespY,7,7);	
		}
		
		if (ga.shotV)
		{
			if (ga.ultimoTurno==1) scr.drawImage(shot[ActFrame%4],ga.shotX/100-(ga.shotW/2)-sc.ScrollX,(ga.shotY/100-(ga.shotH/2))-sc.ScrollY+DespY);
			else scr.drawImage(eShot[ActFrame%2],ga.shotX/100-(ga.shotW/2)-sc.ScrollX,(ga.shotY/100-(ga.shotH/2))-sc.ScrollY+DespY);
			sc.ScrollUpdate(ga.shotX/100-(ga.shotW/2)-sc.ScrollX,(ga.shotY/100-(ga.shotH/2))-sc.ScrollY+DespY,ga.shotW,ga.shotH);	
		}
		
		if (ga.UsedSmokes > 0)
		{
			for (int Counter=0; Counter<ga.UsedSmokes; Counter++)
			{
				scr.drawImage(smoke[ActFrame],ga.smokeX[Counter]-1-sc.ScrollX,ga.smokeY[Counter]-1-(256-ScrollY)+DespY);
				sc.ScrollUpdate(ga.smokeX[Counter]-1-sc.ScrollX,ga.smokeY[Counter]-1-(256-ScrollY)+DespY,10,10);		
			}
			if (ActFrame>=5)
			{
				ga.ChangeTurn = true;
				ga.UsedSmokes = 0;
			}
		}
		
		if (ga.explV)
		{
			if (ActFrame>=4) 
			{
				ga.explV = false; 
				if (ga.UsedSmokes==0) ga.ChangeTurn = true;
			}
			else
			{
				scr.drawImage(expl[ActFrame],ga.explX-(ga.explGW/2)-sc.ScrollX,ga.explY-(ga.explGH/2)-(256-ScrollY)+DespY);
				sc.ScrollUpdate(ga.explX-(ga.explGW/2)-sc.ScrollX,ga.explY-(ga.explGH/2)-(256-ScrollY)+DespY,ga.explGW,ga.explGH);	
			}
		}
		
		scr.drawImage(MenuImg,0,128);
		
		/*scr.setColor(0xDF6C06);
		for (int Counter=0; Counter<ga.MapHeight.length; Counter++) scr.drawLine(92+Counter,128+8+33-ga.MapHeight[Counter],92+Counter,128+8+35);*/
		scr.setColor(0x608FF0);
		for (int Counter=0; Counter<ga.MapHeight.length; Counter++) scr.drawLine(92+Counter,128+11,92+Counter,128+8+32-ga.MapHeight[Counter]);
		scr.setColor(0xFFFFFF);
		if (ga.shotV)  
		{
			if ((ga.shotY/100)<24)	scr.fillRect(92+(ga.shotX/800),128+11,1,1);
			else scr.fillRect(92+(ga.shotX/800),128+8+(ga.shotY/800),1,1);
		}
		
		scr.setColor(0xFFF200);
		scr.fillRect(91+(ga.protX/8),128+8+(ga.protY/8),3,3);
		scr.fillRect(91+(ga.enemX/8),128+8+(ga.enemY/8),3,3);
		
		if (ga.viento>0) scr.fillRect(49,128+33,ga.viento,2);
		else scr.fillRect(48+ga.viento,128+33,0-ga.viento,2);
		
		scr.setColor(0xFF0000);
		scr.fillRect(15,128+18,(ga.fuerza*2)/3,4);
		
		scr.setColor(0xEEEEEE);
		scr.drawLine(48,128+31,48,128+36);
		
		scr.setColor(0x000000);
		scr.drawRect(0,0,1,128);
		scr.drawRect(160,0,1,128);
		break;
	}
	if (ga.textTime>0)
	{
		if (ga.textTime == 1)
		{
			Repintar = true;
		} else textDraw(ga.text, ga.textX-(canvasWidth/2), ga.textY-(canvasHeight/2)-((ga.gameStatus==102) ? 0 : 0), ga.textC, 0x111A);
	}
	if (Repintar) 
	{
		sc.ScrollUpdate(0,0,canvasWidth,canvasHeight);
		Repintar = false;
	}
}

// <=- <=- <=- <=- <=-


public void ZonaFondo(int X, int Y, int W, int H, int NumTramos, int Tipo)
{
	int ICRed = 0xFF,ICGre = 0xFF,ICBlu = 0xFF;
	int FCRed = 0x0B,FCGre = 0x8F,FCBlu = 0xF0;
	
	/*if (Tipo==0)
	{
		ICRed = 0xFF;ICGre = 0xFF;ICBlu = 0xFF;
		FCRed = 0x6D;FCGre = 0xCF;FCBlu = 0xF6;
	} else
	{
		ICRed = 0x6D;ICGre = 0xCF;ICBlu = 0xF6;
		
	}*/
		
	if (W<0)
	{
		X -= W;
		W *= -1;
	}
	if (H<0)
	{
		Y -= W;
		H *= -1;
	}
	if (X<0) 
	{
		W += X;
		X = 0;
	}
	
	if ((W>0)&&(H>0))
	{
		int ActCol,ActRed,ActGre,ActBlu,Tramo;
		int GradTam;
		
		GradTam = GradTam = ((128*3)/2)/NumTramos;
		for (int Counter = Y; Counter<(Y+H+1); Counter++)
		{
			Tramo = (Counter/GradTam);
			if (Tramo >= NumTramos) Tramo = NumTramos-1;
			ActRed = (((ICRed)*(Tramo))+((FCRed)*((NumTramos)-Tramo)))/(NumTramos);
			ActGre = (((ICGre)*(Tramo))+((FCGre)*((NumTramos)-Tramo)))/(NumTramos);
			ActBlu = (((ICBlu)*(Tramo))+((FCBlu)*((NumTramos)-Tramo)))/(NumTramos);
			Tramo = (Counter/GradTam);
			int Inicio,Final;
			Inicio = (((Tramo*GradTam)>=Y) ? (Tramo*GradTam) : Y);
			Final  = ((((Tramo+1)*GradTam)<=(Y+H)) ? ((Tramo+1)*GradTam) : (Y+H));
			if (Inicio<0) Inicio = 0;
			if (Final<0)  Final = 0;
			if (Inicio>canvasHeight) Inicio = canvasHeight;
			if (Final>canvasHeight)  Final = canvasHeight;
			if (Final-Inicio>0) 
			{
				scr.setColor(scr.getColorOfRGB(ActRed,ActGre,ActBlu));
				scr.fillRect(X,Inicio,W,Final-Inicio);
			}
			Counter = ((Tramo+1)*GradTam);
		}
	}
}

// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------

byte DespY=0;

// **************************************************************************//
} // Cierra la Clase GameCanvas
// **************************************************************************//