package com.mygdx.mongojocs.machinecrisis;

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

	Sound = new MediaSound[3];
	
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
			"- Si es la primera vez que ejecuta esta aplicación, asegúrese de tener activado el acceso a la red.",
			" ",
			"- Posible problema de cobertura GPRS. Inténtelo de nuevo más tarde.",
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

	if ( FS_Create("saiyubot/Data") ) {ga.terminate();}	// Descargamos FileSystem de Internet

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


	/// Texto de "cargando"... solo se ve la primera vez que se juega.
	/// seguramente solo sea necesario en el TSM30...
	if(ga.gameStatus == 100) {
		scr.setColor(0x00FF00);
		scr.drawString(ga.menuText[Game.TEXT_LOADING][0],60,100);	
	}

/*
	if(ga.gameStatus == 51) {
		if(cont6 <= -4) {
			cont6 = 0;
			bDirBrillo = false;
			xBrillo = ga.RND(canvasWidth);
			yBrillo = ga.RND(canvasHeight);
			limBrillo = 5 + ga.RND(35);
		}
		brillo();
	}
*/
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

Image[] protaImg 	= new Image[6];
Image[] piezasImg 	= new Image[30];
Image 	fondoImg, marcadorImg, tuberiasMetaImg, manoOKImg, manoNOKImg;
Image	iconoPiezaImg, iconoRelojImg;
Image[] humoImg		= new Image[3];
Image[] tuberiasImg	= new Image[2];

byte spritesCor[] = new byte[144];

public void jugarCreate_Gfx()

{
	///////////////////// MFS /////////////////////
	
	//animacion prota
	for(int i=0;i<6;i++) protaImg[i] = FS_LoadImage(2,i);
	//animacion de piezas  items
	for(int i=0;i<30;i++) piezasImg[i] = FS_LoadImage(3,i);
	//fondo y animaciones de tuberias y humo
	fondoImg = FS_LoadImage(4,0);
	for(int i=0;i<3;i++) humoImg[i] = FS_LoadImage(4,i+1);
	for(int i=0;i<2;i++) tuberiasImg[i] = FS_LoadImage(4,i+4);
	tuberiasMetaImg = FS_LoadImage(4,7);
	//marcador
	marcadorImg = FS_LoadImage(4,6);
	//mano pieza ok/nok
	manoOKImg = FS_LoadImage(4,8);
	manoNOKImg = FS_LoadImage(4,9);
	//iconos de info de fase completada
	iconoPiezaImg = FS_LoadImage(4,10);
	iconoRelojImg = FS_LoadImage(4,11);
	
	///////////////////// RES /////////////////////
	
	ga.loadFile(spritesCor,"/sprites.cor");

}


// -------------------
// Jugar END Gfx
// ===================

public void jugarDestroy_Gfx() {
	for(int i=0;i<6;protaImg[i++]=null);
	for(int i=0;i<30;piezasImg[i++]=null);
	fondoImg = null;
	spritesCor = null;
	
	System.gc();
}


// -------------------
// Jugar SET Gfx
// ===================

public void jugarInit_Gfx() {}


// -------------------
// Jugar RES Gfx
// ===================

public void jugarRelease_Gfx(){}


// -------------------
// Jugar IMP Gfx
// ===================

boolean bImpFondo 			= false;
boolean bImpMarcador		= false;
boolean bImpTuberias 		= false;
boolean bImpHumo	 		= false;
boolean bImpProta 			= false;
boolean bImpPiezas 			= false;
boolean bImpItems 			= false;
boolean bImpTextosPunt 		= false;
boolean bImpNuevoNivel 		= false;
boolean bImpNivelCompletado = false;
boolean bImpInfoItem 		= false;
boolean bImpTransicion		= false;
boolean bImpManoOK			= false;
boolean bImpManoNOK			= false;
boolean bImpConsejo			= false;

public void noImp() {
	bImpFondo 			= false;	bImpMarcador		= false;
	bImpTuberias 		= false;	bImpHumo	 		= false;
	bImpProta 			= false;	bImpPiezas 			= false;
	bImpItems 			= false;	bImpTextosPunt 		= false;
	bImpNuevoNivel 		= false;	bImpNivelCompletado = false;
	bImpInfoItem 		= false;	bImpTransicion		= false;
	bImpManoOK			= false;	bImpManoNOK			= false;
	bImpConsejo 		= false;
	timerManoOK = timerManoNOK = timerMarcador = 0;
}

public void jugarDraw_Gfx() {


	if(bImpFondo)			{	impFondo();			bImpFondo		= false;}
	if(bImpHumo)			{	impHumo();									}
	if(bImpNivelCompletado) { 	impNivelCompletado(); 						}
	if(bImpTransicion)		{	impTransicion();	return;	/*!*/			}
	if(bImpProta)			{	impProta(); 		bImpProta		= false;}
	if(bImpPiezas)			{	impPiezas(); 		bImpPiezas		= false;}
	if(bImpItems)			{	impItems(); 		bImpItems		= false;}
	if(bImpTuberias)		{	impTuberias();								}
	if(bImpTextosPunt)		{	impTextosPunt(); 	bImpTextosPunt 	= false;}
	if(bImpInfoItem) 		{ 	impInfoItem();								}
	if(bImpMarcador)		{	impMarcador();		bImpMarcador	= false;}
	if(bImpManoOK)			{	impManoOK();								}
	if(bImpManoNOK)			{	impManoNOK();								}
	if(bImpConsejo)			{	impConsejo();								}
	
	// ON SCREEN DEBUGGING (C)
	//scr.setColor(Graphics.getColorOfName(Graphics.WHITE));
	//scr.drawString("Con sleep a "+ga.gameSleep,10,10);
	//scr.drawString("Piezas Objetivo: "+ga.PARAM_PIEZAS_GOAL,10,20);
	//scr.drawString("Nivel: "+ga.NIVEL,10,30);
	//scr.drawString("PROTA: ("+ga.PROTA.x+","+ga.PROTA.y+")",10,30);
	//scr.drawString("SCROLLX: "+ga.scrollX+" ("+canvasWidth/2+")",10,30);
	//scr.drawString("TIEMPO: "+tiempoFase(),10,30);
	
	//MIT - borrar ultima linea de pixels...
	//scr.setColor(Graphics.getColorOfName(Graphics.BLACK));
	//scr.fillRect(0,130,128,1);
}




String tiempoFase() {
	String h, m, s;

	h = Long.toString((ga.TIEMPO_FASE/1000)/3600);
	m = Long.toString((ga.TIEMPO_FASE/1000)/60);
	s = Long.toString((ga.TIEMPO_FASE/1000)%60);
	
	if(h.length()==1) h = "0"+h;
	if(m.length()==1) m = "0"+m;
	if(s.length()==1) s = "0"+s;

	return 	h+":"+m+":"+s;
}


//////////// TEXTO CONSEJO (TITULO) INICIO NIVEL ///////////////////////////////

long timerConsejo = -1;
int contConsejo = -1;
int sizeTexto = 0;
boolean bAux = false;
private void impConsejo() {
	Font f = Font.getFont(Font.FACE_PROPORTIONAL|Font.STYLE_BOLD |Font.SIZE_SMALL);
	scr.setFont(f);

	if(contConsejo < 0 && !bAux) {
		contConsejo = canvasWidth;
		if(ga.NIVEL < 5) sizeTexto = f.stringWidth(ga.menuText[Game.TEXT_TIPS][ga.NIVEL]);
		else sizeTexto = f.stringWidth(ga.menuText[Game.TEXT_NIVEL][0]+" "+Integer.toString(ga.NIVEL+1));
	}	

	if(contConsejo+sizeTexto/2<=canvasWidth/2 && !bAux) {
		timerConsejo = System.currentTimeMillis();
		bAux = true;
	}

	if(timerConsejo < 0) contConsejo -= 4;
	else if(System.currentTimeMillis()-timerConsejo > 2000) timerConsejo = -1;


	if(contConsejo+sizeTexto < 0) {
		bImpConsejo = false;
		contConsejo = -1;	
		bAux = false;
	} else {
		scr.setColor(0);
		scr.fillRect(0,0,canvasWidth,canvasHeight);
		scr.setColor(0xFFFFFF);
		if(ga.NIVEL < 5) scr.drawString(ga.menuText[Game.TEXT_TIPS][ga.NIVEL],contConsejo,canvasHeight/2);
		else scr.drawString(ga.menuText[Game.TEXT_NIVEL][0]+" "+Integer.toString(ga.NIVEL+1),contConsejo,canvasHeight/2);
	}	
	//System.out.println("contConsejo = "+contConsejo);
}

//////////// MANO PIEZA OK /////////////////////////////////////////////////////

static final int TIME_MANO	= 1500;
long timerManoOK = 0;
int contComboOK = 0;

public void putManoOK() {
	if(!bImpManoOK) bImpManoOK = true;
	else {
		contComboOK++;
		timerManoOK = ga.TIEMPO_FASE;
	}	
}

private void impManoOK() {
	
	if(timerManoOK == 0) timerManoOK = ga.TIEMPO_FASE;
	else if(ga.TIEMPO_FASE - timerManoOK > TIME_MANO) {
			contComboOK = 1;
			timerManoOK = 0;
			bImpManoOK = false;
			return;
	}
	/*
	scr.setColor(Graphics.getColorOfName(Graphics.OLIVE));
	scr.fillRect(canvasWidth/2-10,canvasHeight/2-10,20,20);
	*/
	scr.drawImage(manoOKImg,canvasWidth/2-5-55,canvasHeight/2-5-60);
	if(contComboOK > 1) {
		scr.setColor(0);
		scr.drawString("x"+Integer.toString(contComboOK),canvasWidth/2-5-55+5,canvasHeight/2-5-60+35);
	}
}

//////////// MANO PIEZA NOK /////////////////////////////////////////////////////

//static final int TIME_MANO	= 1000;
long timerManoNOK = 0;
int contComboNOK = 0;

public void putManoNOK() {
	if(!bImpManoNOK) bImpManoNOK = true;
	else {
		contComboNOK++;
		timerManoNOK = ga.TIEMPO_FASE;
	}	
	if(ga.gameVibra == 1) VibraSET(69);
}

private void impManoNOK() {
	
	if(timerManoNOK == 0) timerManoNOK = ga.TIEMPO_FASE;
	else if(ga.TIEMPO_FASE - timerManoNOK > TIME_MANO) {
			contComboNOK = 1;
			timerManoNOK = 0;
			bImpManoNOK = false;
			return;
	}
	/*
	scr.setColor(Graphics.getColorOfName(Graphics.OLIVE));
	scr.fillRect(canvasWidth/2-10,canvasHeight/2-10,20,20);
	*/
	scr.drawImage(manoNOKImg,canvasWidth/2-5-19-55,canvasHeight/2-5-60);
	if(contComboNOK > 1) {
		scr.setColor(0);
		scr.drawString("x"+Integer.toString(contComboNOK),canvasWidth/2-5-19-55+5,canvasHeight/2-5-60+35);
	}
}


//////////// TRANSICION ////////////////////////////////////////////////////////
int cont7 = -1;
byte trDxs[] = {0, 1, 2, 3, 4, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
			   7, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
			   0, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
			   5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 5,
			   5, 5, 5, 5, 5, 5, 5, 5, 4, 3, 2, 2, 2, 2, 2, 2, 2, 2,
			   3, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3};
byte trDys[] = {0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,
              11,12,12,12,12,12,12,12,12,11,10, 9, 8, 7, 6, 5, 4, 3, 
               2, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,
              11,11,11,11,11,10, 9, 8, 7, 6, 5, 4, 3, 2, 2, 2, 2, 2,
               3, 4, 5, 6, 7, 8, 9,10,10,10,10, 9, 8, 7, 6, 5, 4, 3,
               3, 3, 4, 5, 6, 7, 8, 9,10,10, 9, 8, 7, 6, 5};

private void impTransicion() {
	if(cont7 < 0) cont7 = 0;
	if(cont7 >= trDys.length-1) {
		cont7 = -1;
		bImpTransicion = false;
		return;
	}
	scr.setColor(0);
	scr.fillRect(trDxs[cont7]*21,trDys[cont7]*14,21,14);
	scr.fillRect(trDxs[cont7+1]*21,trDys[cont7+1]*14,21,14);
	//scr.fillRect(trDxs[trDys.length-cont7]*16,trDys[trDys.length-cont7]*10,16,13);
	//System.out.println(trDxs[cont7]*16+","+trDys[cont7]*10);
	cont7+=2;
	
}


//////////// MARCADOR //////////////////////////////////////////////////////////

static final int TIMER_SALIDA_MARCADOR	= 1500;
long timerMarcador;
int cont5 = 19;
boolean bInOut = false; //false:In;true:Out

boolean bActualizaMarcador = false;

int tamDiv_x100;
int numDiv;
int[] degradado;

public void prepareMarcador() {
	
	tamDiv_x100 = 11100/(2*ga.PARAM_PIEZAS_GOAL);
	numDiv = 2*ga.PARAM_PIEZAS_GOAL;
	degradado = new int[numDiv+1];
	int divs_x100 = 25500/numDiv;
	
	for(int i=0;i<numDiv;i++) {
		degradado[i] = Graphics.getColorOfRGB((i*divs_x100)/100,255-(i*divs_x100)/100,0);
	}
}

private void impMarcador() {
	
	if(bActualizaMarcador) {
		bActualizaMarcador = false;
		if(cont5 > 18) cont5 = 18;
		bInOut = false;
		timerMarcador = /*System.currentTimeMillis()*/ga.TIEMPO_FASE;
	}

	if(cont5 > 18) return;
	
	if(!bInOut) {
		cont5 -=2;
		if(cont5 <= 0) {
			timerMarcador = /*System.currentTimeMillis()*/ga.TIEMPO_FASE;
			bInOut = true;
			cont5 = 0;
		}
	} else {
		if(/*System.currentTimeMillis()*/ga.TIEMPO_FASE-timerMarcador > TIMER_SALIDA_MARCADOR
			&&
		   iTipoItem == -1) {
			cont5 += 1;
			if(cont5 >= 18) cont5 = 19;
		}
	}
	
	if(cont5 >= 0) {
		scr.drawImage(marcadorImg,0,-cont5);
		scr.setColor(0xFFFFFF);
		int tiempo = (int) (ga.PROTA.TIME_EFECTO_ITEM-(ga.TIEMPO_FASE-ga.PROTA.timer));
		if(ga.PROTA.timer==0) tiempo = 0;



		if(tiempo <= 0) {
			scr.drawString("-",21,8-cont5-6);
			scr.drawString("-",32,8-cont5-6);
		} else {
			scr.drawString(Integer.toString(tiempo/1000),21,8-cont5-6);
			scr.drawString(Integer.toString((tiempo/100)%10),32,8-cont5-6);
		}


		if(ga.PARAM_PIEZAS_GOAL==0 && ga.GameTicks%2==0)
			scr.setColor(0xFFFFFF);
		else
			scr.setColor(degradado[ga.PARAM_PIEZAS_GOAL]);
		
		scr.fillRect(47,6-cont5,111-((ga.PARAM_PIEZAS_GOAL*tamDiv_x100)/100),5);
	
/*	
 	static final int IT_SPEED_UP	= 2;
	static final int IT_SPEED_DOWN	= 3;
	static final int IT_STOP		= 4;
	static final int IT_IMAN		= 5;
	static final int IT_PARALIZA	= 6;
*/
		int sprItem = -1;
		switch(iTipoItem) {
			case Item.IT_SPEED_UP: 	sprItem = Item.FB_SPEED_UP; break;
			case Item.IT_SPEED_DOWN:sprItem = Item.FB_SPEED_DOWN; break;
			case Item.IT_STOP: 		sprItem = Item.FB_STOP; break;					
			case Item.IT_IMAN: 		sprItem = Item.FB_IMAN; break;						
			case Item.IT_PARALIZA:	sprItem = Item.FB_PARALIZA; break;
		}
		if(sprItem > 0) scr.drawImage(piezasImg[sprItem],5,-cont5+3);
	
	}
}
//////////// FONDO ESTATICO ////////////////////////////////////////////////////

private void impFondo() {
	scr.drawImage(fondoImg,-ga.scrollX,0);
}

//////////// FONDO DINAMICO ////////////////////////////////////////////////////
int cont1 = 0;
private void impTuberias() {
	int y = 45+50; if(cont1 > 0) y = 34+50;
	
	if(cont1 == 2/*+4*/) { cont1 = 0; bImpTuberias = false; }
	else scr.drawImage(tuberiasImg[/*Math.min(1,*/cont1/*)*/],2-ga.scrollX,y);
	if(ga.GameTicks%3==0) cont1++;
}

int cont4 = 0;
private void impHumo() {
	if(cont4 == 3) { cont4 = 0; bImpHumo = false; }
	else scr.drawImage(humoImg[cont4++],231-ga.scrollX+68,61+50);
}

//////////// TEXTOS DE PUNTUACION //////////////////////////////////////////////
private void impTextosPunt() {
	for(int i=0;i<4;i++) {
		if(ga.TEXTOS[i] != null) {
			scr.setColor(0);
			if(ga.TEXTOS[i].x-ga.scrollX >= canvasWidth-4) {
				scr.drawString(ga.TEXTOS[i].txt,canvasWidth-8+1,ga.TEXTOS[i].y+1);				
				scr.setColor(ga.TEXTOS[i].color);
				scr.drawString(ga.TEXTOS[i].txt,canvasWidth-8,ga.TEXTOS[i].y);
			} else {
				scr.drawString(ga.TEXTOS[i].txt,ga.TEXTOS[i].x-ga.scrollX+1,ga.TEXTOS[i].y+1);				
				scr.setColor(ga.TEXTOS[i].color);
				scr.drawString(ga.TEXTOS[i].txt,ga.TEXTOS[i].x-ga.scrollX,ga.TEXTOS[i].y);
			}
		}	
	}
}

//////////// INFO NIVEL ////////////////////////////////////////////////////////
int cont = -40;
int dxs[] = {0,1,2,3,3,3,3,2,1,0,0,0,1,2,2,1};
int dys[] = {0,0,0,0,1,2,3,3,3,3,2,1,1,1,2,2};

int xReloj = 20;
int xPieza = 20;
int dirReloj = 1;
int dirPieza = 1;
int colorPunt = 0xFFFFFF;

private void impNivelCompletado() {
	
	colorPunt = 0xFFFFFF;
	if(cont < 16) {
		xReloj = 20; dirReloj = 1;
		xPieza = 20; dirPieza = 1;
	} else if(cont == 16) {
		cont5 = 19; //quitar marcador
		bImpTuberias = bImpHumo = bImpTextosPunt = bImpInfoItem = false; //quitarlos...
		bImpFondo = bImpPiezas = bImpItems = false;
		scr.setColor(0);
		scr.fillRect(0,0,canvasWidth,canvasHeight);
		ga.PUNTUACION += 1000;
		colorPunt = Graphics.getColorOfName(Graphics.GREEN);
	} else if(cont > 26) {
		colorPunt = 0xFFFFFF;
		if(ga.TIEMPO_FASE > 0) {
			ga.TIEMPO_FASE -= 1000;
			ga.PUNTUACION--;
			colorPunt = Graphics.getColorOfName(Graphics.RED);
			xReloj += dirReloj*2;
			if(xReloj > 26 || xReloj < 14) dirReloj *= -1;
		} else {
			ga.TIEMPO_FASE = 0;
			xReloj = 20;
			if(ga.PIEZAS_CAIDAS > 0) { 
				xPieza += dirPieza*2; 
				if(xPieza > 26 || xPieza < 14) dirPieza *= -1;
				ga.PIEZAS_CAIDAS--;
				ga.PUNTUACION -= 5;
				colorPunt = Graphics.getColorOfName(Graphics.RED);				
				if(ga.PUNTUACION < 0) ga.PUNTUACION = 0;
			} else {
				xPieza = 20;
			}
		}

		int x;
		if(ga.PROTA.x < /*canvasWidth/2-12*/48 || ga.PROTA.x > 3*canvasWidth/2-12) 
			x = ga.PROTA.x%canvasWidth;
		else 
			x = 48/*canvasWidth/2-12*/;

		scr.fillRect(x-4,ga.PROTA.y-4,24+8,24+8);
	}
	
	if(ga.PIEZAS_CAIDAS	== 0 && ga.TIEMPO_FASE == 0 || keyX != 0 || keyY != 0)  {
		//System.out.println("se akabo...");
		if(ga.PIEZAS_CAIDAS > 0) {
			ga.PUNTUACION -= ga.PIEZAS_CAIDAS*5;
			//colorPunt = Graphics.getColorOfName(Graphics.RED);
			ga.PIEZAS_CAIDAS = 0;
		}
		if(ga.TIEMPO_FASE > 0) {
			ga.PUNTUACION -= ga.TIEMPO_FASE/1000;
			//colorPunt = Graphics.getColorOfName(Graphics.RED);
			ga.TIEMPO_FASE = 0;
		}
		if(cont < 16) ga.PUNTUACION += 1000;
		
		if(ga.PUNTUACION < 0) ga.PUNTUACION = 0;
		cont = -40;
		bImpNivelCompletado = false;
		ga.TIMER = System.currentTimeMillis();
		xPieza = 20;
		colorPunt = 0xFFFFFF;
	}
	scr.setColor(0);
	scr.fillRect(0,0,canvasWidth,canvasHeight);
	
	textDraw(ga.menuText[Game.TEXT_NIVEL][0]+" "+ga.NIVEL+" "+ga.menuText[Game.TEXT_COMPLETADO][0],0,20,0xFFFFFF,TEXT_BOLD|TEXT_LARGER|TEXT_HCENTER|TEXT_OUTLINE);
	
	scr.drawImage(iconoRelojImg,xReloj,80-14);
	textDraw(tiempoFase(),10,70,0xFFFFFF,TEXT_PLAIN|TEXT_MEDIUM|TEXT_HCENTER);
	
	scr.drawImage(iconoPiezaImg,xPieza,120-14);
	textDraw(Integer.toString(ga.PIEZAS_CAIDAS),10,110,0xFFFFFF,TEXT_PLAIN|TEXT_MEDIUM|TEXT_HCENTER);
	
	textDraw(ga.menuText[Game.TEXT_PUNTUACION][0]+": "+ga.PUNTUACION,5,140,colorPunt,TEXT_PLAIN|TEXT_LARGER|TEXT_LEFT|TEXT_OUTLINE);
	
	
	if(ga.PUNTUACION > ga.HISCORE && cont%4==0) {
		textDraw(ga.menuText[Game.TEXT_HISCORE][0]+" "+ga.menuText[Game.TEXT_PUNTUACION][0]+"!!",5,150,
				 Graphics.getColorOfName(Graphics.YELLOW),TEXT_PLAIN|TEXT_LARGER|TEXT_LEFT|TEXT_OUTLINE);
	}
	
	cont++;
}

//////////// INFO ITEM /////////////////////////////////////////////////////////

int cont2 = -1;
int cont3 = canvasHeight;
int iTipoItem = -1;




private void impInfoItem() {
	if(iTipoItem < 0 || cont3 < 0) {
		bImpInfoItem = false;
		cont2 = -50;
		cont3 = canvasHeight;
		return;
	}
	
	switch(iTipoItem) {
		case Item.IT_SPEED_UP:
			scr.setColor(0);
			scr.drawString("+ "+ga.menuText[Game.TEXT_RAPIDO][0],canvasWidth-105+1,canvasHeight/2+1);
			scr.setColor(Graphics.getColorOfName(Graphics.BLUE));
			scr.drawString("+ "+ga.menuText[Game.TEXT_RAPIDO][0],canvasWidth-105,canvasHeight/2);
			break;
		case Item.IT_SPEED_DOWN:
			scr.setColor(0);
			scr.drawString("+ "+ga.menuText[Game.TEXT_LENTO][0],canvasWidth-105+1,canvasHeight/2+1);
			scr.setColor(Graphics.getColorOfName(Graphics.RED));
			scr.drawString("+ "+ga.menuText[Game.TEXT_LENTO][0],canvasWidth-105,canvasHeight/2);
			break;
		case Item.IT_STOP:
			scr.setColor(0);
			scr.drawString(ga.menuText[Game.TEXT_STOP][0],canvasWidth-120+1,canvasHeight/2+1);			
			scr.setColor(Graphics.getColorOfName(Graphics.YELLOW));
			scr.drawString(ga.menuText[Game.TEXT_STOP][0],canvasWidth-120,canvasHeight/2);
			break;
		case Item.IT_IMAN:
			scr.setColor(0);
			scr.drawString(ga.menuText[Game.TEXT_MAGNETIC][0],canvasWidth-105+1,canvasHeight/2+1);
			scr.setColor(Graphics.getColorOfName(Graphics.LIME));
			scr.drawString(ga.menuText[Game.TEXT_MAGNETIC][0],canvasWidth-105,canvasHeight/2);
			break;
		case Item.IT_PARALIZA:
			scr.setColor(0);
			scr.drawString(ga.menuText[Game.TEXT_PARALIZE][0],canvasWidth-105+1,canvasHeight/2+1);
			scr.setColor(Graphics.getColorOfName(Graphics.FUCHSIA));
			scr.drawString(ga.menuText[Game.TEXT_PARALIZE][0],canvasWidth-105,canvasHeight/2);
			break;			
		default:
			System.out.println("GameCanvas->impInfoItem(): Tipo de ITEM incorrecto");
	}

	cont3 -= 4;
}


//////////// PROTA /////////////////////////////////////////////////////////////
private void impProta() {

	int x;
	if(ga.PROTA.x < 162/2-12 || ga.PROTA.x > 3*canvasWidth/2-12 || ga.PROTA.x2==-1 || ga.PROTA.x2==1) 
		x = ga.PROTA.x%canvasWidth;
	else 
		x = 162/2-12;
	if(ga.PROTA.saltando && !ga.PROTA.quietoparao) {
		if(ga.PROTA.direccion) scr.drawImage(protaImg[4],x,ga.PROTA.y);
		else scr.drawImage(protaImg[5],x,ga.PROTA.y);
	} 
	else scr.drawImage(protaImg[ga.PROTA.frame],x,ga.PROTA.y);
}


//////////// PIEZAS ////////////////////////////////////////////////////////////
private boolean enPantalla(int i) { 
	return (ga.PIEZAS[i].x-5-ga.scrollX > 0 && ga.PIEZAS[i].x-5-ga.scrollX < canvasWidth
			&&
			((ga.PIEZAS[i].y+10 > 0 && cont5==19)
			||
			 (ga.PIEZAS[i].y+10 > cont5)));
			 
			
}

private void drawTriangle(int i) {
	int color;
	//int aumento = 0;

	if(ga.PIEZAS[i].dy < 0) 
		color = Graphics.getColorOfName(Graphics.LIME);
	else if(ga.PIEZAS[i].y-5 < canvasHeight/3) 
		color = Graphics.getColorOfName(Graphics.YELLOW);
	else
		color = Graphics.getColorOfName(Graphics.RED);

	
	if(ga.PIEZAS[i].salida) {
		color = Graphics.getColorOfName(ga.RND(15));
		scr.setColor(color);
		//aumento = 4;
		//LCD_Gfx.fillRect(CanvasSizeX-8,ga.PIEZAS[i].y,8,8);
		//return;
	}
	
	int xPoints[] = {0,0,0};
	int yPoints[] = {0,0,0};
	
	if(ga.PIEZAS[i].y +10>cont5 || (ga.PIEZAS[i].y+10>0 && cont5==19)) {
		xPoints[0] = 6;	yPoints[0] = ga.PIEZAS[i].y-5-6;
		xPoints[1] = 0;	yPoints[1] = ga.PIEZAS[i].y-5;
		xPoints[2] = 6;	yPoints[2] = ga.PIEZAS[i].y-5+6;
		if(ga.PIEZAS[i].x-5-ga.scrollX > canvasWidth) {
			xPoints[0] += canvasWidth-8-6;
			xPoints[1] += canvasWidth-8+6;
			xPoints[2] += canvasWidth-8-6;
		}
	} else {
		int xxx = ga.PIEZAS[i].x-ga.scrollX+/*ga.PIEZAS[i].addX()*/+5;
		xPoints[0] = /*ga.PIEZAS[i].x*/xxx-6;		yPoints[0] = 6+(18-cont5);
		xPoints[1] = /*ga.PIEZAS[i].x*/xxx;			yPoints[1] = 0+(18-cont5);
		xPoints[2] = /*ga.PIEZAS[i].x*/xxx+6;		yPoints[2] = 6+(18-cont5);
	}
	

			
	scr.setColor(color);
	scr.fillPolygon(xPoints,yPoints,3);
}

private void impPiezas() {
	//scr.setColor(0xFFFFFF);	
	for(int i=0;i<ga.MAX_NUM_PIEZAS;i++) {
		if(ga.PIEZAS[i] != null) {
			if(!ga.PIEZAS[i].parpadeo || ga.GameTicks%2==0) {
				//System.out.println(ga.PIEZAS[i].frame);
				if(ga.PIEZAS[i].magnetizada) {

					int x;
					if(ga.PROTA.x < 162/2-12 || ga.PROTA.x > 3*canvasWidth/2-12) 
						x = ga.PROTA.x%canvasWidth;
					else 
						x = 162/2-12;
					
					scr.drawImage(piezasImg[ga.PIEZAS[i].frame],
									ga.PIEZAS[i].x2+x+ga.PIEZAS[i].addX()+12,
									ga.PIEZAS[i].y);
				} else if(enPantalla(i)) {
					scr.drawImage(piezasImg[ga.PIEZAS[i].frame],
									ga.PIEZAS[i].x-ga.scrollX+ga.PIEZAS[i].addX(),
									ga.PIEZAS[i].y+ga.PIEZAS[i].addY());
				} else
					drawTriangle(i);
			}
		}
	}


	if(ga.scrollX > 100) {
		scr.drawImage(tuberiasMetaImg,canvasWidth-24+(canvasWidth-ga.scrollX),canvasHeight-61-1);//-1 -> Mit
		//System.out.println("Pintando tuberias meta");
	}

}


//////////// ITEMS ////////////////////////////////////////////////////////////

private void impItems() {
	//scr.setColor(0xFFFFFF);	
	for(int i=0;i<ga.MAX_NUM_ITEMS;i++) {
		if(ga.ITEMS[i] != null) {
			if(ga.ITEMS[i].tipo==Item.IT_BOMBA && ga.ITEMS[i].explotando)
				explosion(	ga.ITEMS[i].x-ga.scrollX+ga.ITEMS[i].addX(),
							ga.ITEMS[i].y+ga.ITEMS[i].addY(),			
							i);
			else if(!ga.ITEMS[i].parpadeo || ga.GameTicks%2==0) 
				
				if(ga.GameTicks%4==0 && ga.ITEMS[i].tipo!=Item.IT_BOMBA)
					scr.drawImage(	piezasImg[24],
									ga.ITEMS[i].x-ga.scrollX+ga.ITEMS[i].addX(),
									ga.ITEMS[i].y+ga.ITEMS[i].addY()	);
				else
					scr.drawImage(	piezasImg[ga.ITEMS[i].frame],
									ga.ITEMS[i].x-ga.scrollX+ga.ITEMS[i].addX(),
									ga.ITEMS[i].y+ga.ITEMS[i].addY()	);
		}
	}
}

//////////// BOMBAS ////////////////////////////////////////////////////////////


private int	exp[] = {-1000,-1000,-1000,-1000,-1000,-1000,-1000,-1000,-1000};
private int	expFrame[] = {0,0,0,0,0,0,0,0,0};

public void resetExplosion() { for(int i=0;i<9;exp[i++]=-1000);}

private void explosion(int x, int y, int i) {

	for(int ii=0;ii<9;ii++) {
		if(exp[ii] == -1000 || expFrame[ii]==2) {
			if(ga.RND(100)>80 || ii==0 || ii==8) {
				if(ii==0) exp[ii] = -ga.PARAM_RADIO_EXPLOSION;
				else if(ii==8) exp[ii] = ga.PARAM_RADIO_EXPLOSION;
				else exp[ii] = -ga.PARAM_RADIO_EXPLOSION+ga.RND(ga.PARAM_RADIO_EXPLOSION)+ga.RND(ga.PARAM_RADIO_EXPLOSION);
				expFrame[ii] = 0;
			}
		} 
		//if(expFrame[ii]==2) {		
		//	exp[ii] = -1000;
		//}
		
		if(exp[ii] != -1000 && expFrame[ii] < 2) 
			scr.drawImage( piezasImg[22+expFrame[ii]],
			               x+exp[ii]+spritesCor[(22+expFrame[ii])*6],
						   y+spritesCor[(22+expFrame[ii]++)*6+1]);

	}
}

private int cont6 = -4;
private int xBrillo = 0;
private int yBrillo = 0;
private int limBrillo = 0;
private boolean bDirBrillo = false;

private void brillo() {
	if(!bDirBrillo) { cont6 += 4;	if(cont6 > limBrillo) bDirBrillo = true;
	} else cont6 -= 4;

	if(cont6 > 0) {

		try {
			scr.setColor(Graphics.getColorOfRGB((255-limBrillo*5)+(cont6-1)*5,
												(255-limBrillo*5)+(cont6-1)*5,
												(255-limBrillo*5)+(cont6-1)*5));
		} catch(Exception e) {
			scr.setColor(0xFFFFFF);
		}
		
		scr.drawLine(xBrillo-cont6/2,yBrillo,xBrillo+cont6/2,yBrillo);
		scr.drawLine(xBrillo,yBrillo-cont6/2,xBrillo,yBrillo+cont6/2);
	}
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