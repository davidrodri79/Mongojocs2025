package com.mygdx.mongojocs.garbage;
//////////////////////////////////////////////////////////////////////////
// Pacman - Color Version 1.0
// By Carlos Peris
// MICROJOCS MOBILE , 2003
//////////////////////////////////////////////////////////////////////////


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.iapplicationemu.AudioPresenter;
import com.mygdx.mongojocs.iapplicationemu.Canvas;
import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.Font;
import com.mygdx.mongojocs.iapplicationemu.Frame;
import com.mygdx.mongojocs.iapplicationemu.Graphics;
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
import java.io.RandomAccessFile;
import java.util.Random;

public class Game extends Canvas implements Runnable, MediaListener
{
    // *******************
// -------------------
// textos - Engine - v1.0 - Rev.0 (28.01.2004)
// ===================
// *******************

final static int TEXT_PLAY = 0;
final static int TEXT_CONTINUE = 1;
final static int TEXT_SOUND = 2;
final static int TEXT_VIBRA = 3;
final static int TEXT_MODE = 4;
final static int TEXT_GAMEOVER = 5;
final static int TEXT_HELP = 6;
final static int TEXT_ABOUT = 7;
final static int TEXT_RESTART = 8;
final static int TEXT_EXIT = 9;
final static int TEXT_LOADING = 10;
final static int TEXT_HELP_SCROLL = 11;
final static int TEXT_ABOUT_SCROLL = 12;
final static int TEXT_BASURA = 13;
final static int TEXT_FINFASE = 14;
final static int TEXT_ENDGAME = 15;

// -------------------
// textos Create
// ===================

String[][] textosCreate(byte[] textos)
{
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;
	int size = 0;

	for (int i=0 ; i<textos.length ; i++)
	{
		if (campo)
		{
			if (textos[i] == 0x7D)
			{
			subCampo = false;
			campo = true;
			}

			if (textos[i] < 0x20 || textos[i] == 0x7C || textos[i] == 0x7D)	// Buscamos cuando Termina un campo
			{
			data[dataPos+1] = (short) (i - data[dataPos]);

			dataPos+=2;

			campo=false;
			}

		} else {

			if (textos[i] == 0x7D)
			{
			subCampo = false;
			continue;
			}

			if (textos[i] == 0x7B)
			{
			dataBak = dataPos;
			data[dataPos++] = 0;
			campo = false;
			subCampo = true;
			size++;
			continue;
			}

			if (subCampo && textos[i] == 0x0A)
			{
				if (primerEnter)
				{
					primerEnter = false;
				} else {
					data[dataPos++] = (short) i;
					data[dataPos++] = 1;
					if (!subCampo) {size++;} else {data[dataBak]--;}
				}
				continue;
			}

			if (textos[i] >= 0x20)	// Buscamos cuando Empieza un campo nuevo
			{
				campo=true;
				data[dataPos] = (short) i;
				if (!subCampo) {size++;} else {data[dataBak]--;}
				primerEnter = true;
			}
		}
	}

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
	int num = data[dataPos];

	if (num<0) {num*=-1;dataPos++;} else {num = 1;}

	strings[i] = new String[num];

	for (int t=0 ; t<num; t++) {strings[i][t] = new String(textos, data[dataPos++], data[dataPos++]);}
	}

	return strings;
}

// <=- <=- <=- <=- <=-
    
    
    
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public byte[] LoadFile(String Nombre)
{
	FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+Nombre);
	byte[] bytes = file.readBytes();

	return bytes;
	/*
	System.gc();

	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] buffer = null;

	try
	{
		int Size = 0;
		while (true)
		{
		int Desp = (int) is.skip(1024);
		if (Desp <= 0) {break;}
		Size += Desp;
		}

		is = null; System.gc();

		buffer = new byte[Size];

		is = getClass().getResourceAsStream(Nombre);
		Size = is.read(buffer, 0, buffer.length);

		while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}

		is.close();
	}
	catch(Exception exception) {}

	System.gc();

	return buffer;*/
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    
    
    
    
    
    
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////
    // IMODE   

    public Image LoadImage(int Pos)
    {
    	MediaImage mimage = MediaManager.getImage("scratchpad:///0;pos="+Pos);
    
    	try {
    		mimage.use();
    	} catch (Exception e) {/*Handle UI problem here*/}
    
    	return mimage.getImage();
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

	public Image _FS_LoadImage(int Pos, int SubPos)
	{
		Image im = new Image();
		im._createImage(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".gif");
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
			//canvasShow=true; repaint();
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
    pinta = true;
	repaint(); 
	int i = 0;
	while (pinta && i < 5) {i++;try {Thread.sleep(20);} catch (Exception e) {}}
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

    
    
    
    
    
    
    
    
    
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////
    
    
    final static int        GLEFT       =   1;
    final static int        GRIGHT      =   -1;
    final static int        GHCENTER     =   0;
    
    final static int        LINEX       =   4;
    final static int        LINEHEIGHT  =   12;
    final static int        TITLEY      =   4;
    final static int        FIRSTLINEY  =   26;
    public static int       w;
    public static int       h;
    private static          Graphics gr;
    Thread                  flujo;
    static boolean          apretando = false;
    static int              currentkey, rkey;
    static int              nfc = 0;
    final static int              largo = 128;
    final static int              alto  = 128;
    final static String ou1 = "4341524C4F535045524953424F4C4541";
    static Random rnd = new Random();
    boolean                 end = false;
  	static int estado = -1, prestado = 1;
    static MMenu menus;
	boolean paused = false;
	
	
	
	/*public void showNotify()
	{
	    paused = false;
	}
	
	public void hideNotify()
	{
	    paused = true;
	    if (estado == 0) estado = 4;
	}*/
	
	   
	   
   public void stop(){ 	}
   
   
   public static void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
	{
		if ((DestX       >= w)
		||	(DestX+SizeX < 0)
		||	(DestY       >= h)
		||	(DestY+SizeY < 0))
		{return;}
	
		gr.drawImage(Sour, DestX-SourX, DestY-SourY);//, Graphics.TOP|GLEFT);
		
	
		
	}

// ----------------------------------------------------------

public static void PutSprite(Image Img[],  int X, int Y,  int Frame, byte[] Coor, int Trozos)
{
    int f = Frame;
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

	//ImageSET(Img[0],  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY,  Flip);
	ImageSET(Img[f], 0, 0,  SizeX,SizeY,  X+DestX,Y+DestY,  Flip);
	}
}


public static void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
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

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY,  Flip);
	}
}


// ----------------------------------------------------------
 
   
   
    //////////////////////////RUTINAS PRINT/////////////////////////////
    static int xprint, yprint, cprint, bprint, alignprint, startsc;
    
    
    public static void SetPrint(int c, int xx, int yy, int align)
    {
            cprint = c;
            xprint = xx;
            yprint = yy;
            alignprint = align;            
    }
    
    
    public static void SetPrint(int xx, int yy, int align)
    {
            cprint = 0xcdff45;
            xprint = xx;
            yprint = yy;
            alignprint = align; 
    }
    
    
    public static void MultiPrint(String s[])
    {        
            int yy = yprint;
            for(int i = 0; i < s.length;i++)
      	    {
      	        Print(s[i], yy);  	        
      	        yy += LINEHEIGHT;      	    
      	    }
    }
    
    
    
    
    public static void MultiPrintList(String s[])
    {        
            final int SCROLL = 4;
            int yy = yprint;
            for(int i = 0; i < s.length;i++)
      	    {
      	        yy = goodPrint(s[i], yy);  	        
      	        yy += LINEHEIGHT;      	    
      	    }
      	    if (yy >= h) startsc -= SCROLL;
    }
    


	public static int goodPrint(String s, int yy)
	{
	    Font fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
	    byte[] btex = s.getBytes();
	    
	    int inipos = 0;
	    int cutpos = 0;
	    //boolean exit = false;
	    while ( cutpos < btex.length )
	    {
	        int twidth = 0;
	        int pos = inipos;	    
    	    while (twidth < w)
    	    {
    	        if (pos >= btex.length) {cutpos = btex.length;break;}
    	        int ch = btex[pos];
    	        if  (ch == 0x20) cutpos = pos;	        
    	        twidth += fon.stringWidth(new String(  (char)ch+"" ));
    	        pos++;
    	    }
    	    Print(s.substring(inipos, cutpos), yy);
    	    inipos = cutpos + 1;
    	    if  ( cutpos < btex.length ) 
    	    yy += LINEHEIGHT;
    	    
    	}
	    return yy;
	    //String real	    
	    
	}

    
    /*public String[] Desglosa(String in)
    {
        String[] out;
        Font fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
    }*/
    
    
    public static void MultiPrint(String s)
    {
            Print(s, yprint);  	             
    }
    
   
    
	
	public static void Print(String s, int yy)
	{
	    Font Fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
	    int xp = xprint;
	    int yp = yy + Fon.getAscent();
	    switch(alignprint)
	    {
	        case GHCENTER:
	            xp = xp - (Fon._stringWidth(s)/2);
	            break;
	        case GRIGHT:
	            xp = xp - Fon._stringWidth(s);
	            break;
	    }	    
	    if (yp <= -LINEHEIGHT || yp >= h) return;                                	 
		gr.setColor(cprint);		
		gr.drawString(s , xp, yp);//, alignprint | Graphics.TOP);				 				         
	}
	
	
	
	public static void Print(int r, int g, int b, String s, int xx, int yy, int align)
	{	
	    Font Fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
	    int xp = xx;
	    int yp = yy + Fon.getAscent();
	    switch(align)
	    {
	        case GHCENTER:
	            xp = xp - (Fon.stringWidth(s)/2);
	            break;
	        case GRIGHT:
	            xp = xp - Fon.stringWidth(s);
	            break;
	    }
	     
		//i/gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		//i/gr.setClip(0,0,largo,alto);
		
		gr.setColor(gr.getColorOfRGB(r,g,b));		
		gr.drawString(s , xp, yp);//, align | Graphics.TOP);				 				         
	}
	
	public static void Print(String s, int xx, int yy, int align)
	{	 
	    Font Fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
	    int xp = xx;
	    int yp = yy + Fon.getAscent();
	    switch(align)
	    {
	        case GHCENTER:
	            xp = xp - (Fon.stringWidth(s)/2);
	            break;
	        case GRIGHT:
	            xp = xp - Fon.stringWidth(s);
	            break;
	    }
		gr.setColor(cprint);		
		gr.drawString(s , xp, yp);//, align | Graphics.TOP);				 				         
	}
	
	
		
	
	 
    public static void blit(int ox, int oy, int _w, int _h, int x, int y, Image foo)
    {
        if (x <= -_w || x >= w || y <= -_h || y >= w) return;   
   	    gr.drawImage(foo,x-ox, y-oy);//, Graphics.TOP|GLEFT);	         
    }
	
	
	
	
	
	
	public static void Vibra()
	{
	     if (Moto.rocco) joc.VibraSET(100);    	     	        	          	 
	}
	
	
	public Image[] loadMimage(int bank, int nimages)
	{
	    Image img[] = new Image[nimages];
	    for(int i = 0; i < nimages;i++)
	        img[i] = FS_LoadImage(bank, i);
	    return img;
	}
	
	
	
	public byte [] ReadByteArray(String ffile, int bytes)
	{	  
		/*byte [] data=null;
		try
		{	InputStream is = getClass().getResourceAsStream(ffile);
			data = new byte[bytes];
			is.read(data);
			is.close();			
		}catch (Exception e){//System.out.println("File Exception: resource:"+ffile+"!\n"+e);
		}return data;*/
		FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+ffile);
		byte[] b = file.readBytes();

		return b;
	}
	
	
	public static Image loadImg(String s)
	{
	    MediaImage im = MediaManager.getImage("resource://"+s+".gif"); 
	    try{
		    im.use();
		  }catch (Exception e) {}   
	    return im.getImage();	  	
	}
	
	
	
	static int nblocks;
	byte map[] = new byte[31*28];
    static byte backmap[] = new byte[31*28];
    prota pacman; 
    static malo  malo[] = new malo[4];
    static Image img;
    byte sprcor[];
    Image spr[];
    Image tiles[];
    static String[][] txt;
    boolean arrankado = false;
   
   
	public void Inicializacion()
	{
	    if (arrankado) {estado = -1;return;}
	    arrankado = true;
	    //gr.setColor(0xffffff);gr.fillRect(0,0,w,h);
	    //gr.drawImage(img,(w/2)-(img.getWidth()/2), (h/2)-(img.getHeight()/2));//, GHCENTER|Graphics.VCENTER);	         	    
	    //ProgBarINI((3*w)/4, 16, w/8, (3*h)/4);

    System.out.println("INI");

	    ProgBarINI( 94, 6,  (w/2)-47, (h/2)+16 );
	    ProgBarSET(0, 1);
	    
        System.out.println("Create");

	    if (FS_Create("garbage/Data")) Moto.joc.terminate();

    System.out.println("END");
	    ProgBarEND();
	    Moto.joc.PrefsINI();
		
	    tiles = loadMimage(2,17);
	    
	    sprcor = ReadByteArray("/spr.cor", 288);
	    spr = loadMimage(1, 24);
	    
	    pacman = new prota(spr, 10,10,16,0,0,sprcor);
	    //pacman.Activate(map, 4,4);
	    
	    for(int i = 0; i < malo.length;i++)
	    {
	        malo[i] = new malo(spr,10,10,16,8,0,sprcor);
	        //malo[i].Activate(map, 4*16,4*16);
	        //malo[i].tag = 50;
	    }
	    //malo[0].tipo = 1;
	    
	    txt = textosCreate(LoadFile("/textos.txt"));
	    buildsfx();
        
        
	    
	    /*imenu = FS_LoadImage(1,17);//loadImg("/menubase");   
	    motocor0 = FS_LoadFile(14, 0);//ReadByteArray("/moto0.cor", 252);		            	
	    mon = loadMimage(3,30);
	    */
	    estado = -1;    
	}
	
	//int lives;
	static int killer;
	static int difficulty;
	static int stage;
	
	public void Refresh()
	{
	    gr.setColor(0);gr.fillRect(0,0,w,h);        
        for(int i = 0; i < backmap.length;i++) 
	        backmap[i] = -1;	
	}
		
	int dif[] = {1, 2, 3, 4, 5, 6, 7,6, 7, 8, 9};	
		
	public void CargaFase(int _stg)
	{
	    gr.setColor(0);gr.fillRect(0,0,w,h);        
        
	    pacman.tag = 100;      
	    killer = 0;	    
        difficulty = dif[_stg];
        map = null;
        System.gc();
        map = ReadByteArray("/mapa"+_stg+".map", 868);
	    nblocks = 0;
	    for(int i = 0; i < backmap.length;i++) 
	    {
	        if (map[i] == 0) {map[i] = (byte)(12+Math.abs(rnd.nextInt()%4));nblocks++;}
	        backmap[i] = -1;
	    }
	    
	    pacman.Activate(map, 16*5,13*5);
	    
	    for(int i = 0; i < malo.length;i++)
	    {
	        switch(i)
	        {
	            case 0: malo[i].Activate(map, 5,4);break;
	            case 1: malo[i].Activate(map, 5*28,5);break;
	            case 2: malo[i].Activate(map, 5,25*5);break;
	            case 3: malo[i].Activate(map, 5*28,25*5);break;	            
	        }
	        malo[i].tipo = 0;	     
	        malo[i].tag = 50;
	        malo[i].rub = 0;
	    }
	    if (difficulty >= 3) malo[0].tipo = 1;	     
	    if (difficulty >= 5) malo[3].tipo = 1;	     
	    SetEstado(0,0);                  
	}
	
	
	
    public void IniGame()
    {    
        stage = 0;	    	  	    	           
        CargaFase(stage);
    }	
   
   
   
    public static void Print(int c, String s, int xx, int yy, int align)
	{	
	    Font Fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
	    int xp = xx;
	    int yp = yy + Fon.getAscent();
	    switch(align)
	    {
	        case GHCENTER:
	            xp = xp - (Fon.stringWidth(s)/2);
	            break;
	        case GRIGHT:
	            xp = xp - Fon.stringWidth(s);
	            break;
	    }	     	
		gr.setColor(c);		
		gr.drawString(s , xp, yp);
	}
	
   
   
   
   public void Pinta()
   {
        gr.setOrigin((w-(31*5))/2,  (h-(28*5)-16)/2);
        
        
        int j = 0;
        for (int i = 0; i < map.length;i++)
        {
            if (backmap[i] != map[i])
            {
                int tile = map[i];
                switch(tile)
                {
                    case 0: 
                        gr.setColor(0x000000);gr.fillRect((i%31)*5, (i/31)*5,5,5);break;                    
                    case 12: case 13:case 14:case 15:
                        gr.setColor(0x000000);gr.fillRect((i%31)*5, (i/31)*5,5,5);
                        gr.setColor(0xffffff);gr.fillRect(((i%31)*5)+1, ((i/31)*5)+1,3,3);break;
                    
                    default:
                        j++;                                           
                        gr.drawImage(tiles[tile], (i%31)*5, (i/31)*5);// 
                        if (i < 5*31 && j >1 && j%10 == 0) {
                        gr.drawImage(tiles[tile], (i%31)*5, (i/31)*5);j++;}
                        break;
                }        
                backmap[i] = map[i];
            }
        }        
        for(int i = 0; i < malo.length;i++) malo[i].blit(gr);	    
	    pacman.blit(gr);        
	    if (nfc% 10 == 0)	    	        
	    {
	        gr.setColor(0);gr.fillRect(4,28*5, w,12);
	        Print(0xe0e0e0, txt[TEXT_BASURA][0]+": "+engu, w-12, 28*5, GRIGHT);
	        //Print(0xe0e0e0, ""+pacman.tag, w-4, 28*4, GRIGHT);
	        if (pacman.tag > 0)
	        {
	            gr.setColor(0xffffff);gr.drawRect(4,(28*5)+2, 40,8);gr.fillRect(4,(28*5)+2, (pacman.tag*4)/10,8);
	        }    
	    }
   }
   int engu;
   
   
   public void Engine()
   {
   	    nfc++;	
   	    for(int i = 0;i < malo.length;i++) malo[i].Update(pacman);
   	    engu = nblocks;
   	    for(int i = 0;i < malo.length;i++) if (malo[i].active) engu += malo[i].rub;
   	    if (engu == 0) {SetEstado(7, 1);Sonido(1, 1, false);}
   	    if (pacman.tag <= 0) {SetEstado(8, 1);Sonido(3, 1, true);}
   }
   
   

   
   
    boolean pinta;

   public void run()
   {
   	   while (!end)
	   {
	     try{
	        long tempo = System.currentTimeMillis();	      
	     
	        if (!paused)
	        {	            
	            if (estado == 0) 
	            {
	                UpdateSprite();         	       	   	
	                Engine();
	            }
	        }
	        pinta = true;
	        	        
	        if (estado ==  -21) Inicializacion();   		        
	        SoundRUN();                
	        
	        repaint();
	        tempo = 20*5 - System.currentTimeMillis()+tempo;	      
	        if (tempo <= 0) tempo = 1; 
	        
	            try
                {   flujo.sleep(tempo);           
            }catch(java.lang.InterruptedException e){}    
            
        }catch(java.lang.Exception e)
        {
            e.printStackTrace();
	        e.toString();
	    }            
	   }	
	   Moto.joc.destroyApp (true);
	}

   
   public void processEvent(int type, int param)
    {
    	if (type == Display.KEY_RELEASED_EVENT)
    	{
    	    currentkey = 0;
    	    apretando = false;
    		/*KeybB=0;
    		KeybX=0;
    		KeybY=0;*/
    	}
    	else if (type == Display.KEY_PRESSED_EVENT)
    	{
    	    currentkey = param;
    	    apretando = true;
    		/*switch (param)
    		{
    
    		case Display.KEY_SOFT1:
    			KeybB=10;
    		break;
    
    		case Display.KEY_SOFT2:
    			FaseNext();
    		break;
    
    		case Display.KEY_UP:
    			KeybY=-1;
    		break;
    
    		case Display.KEY_DOWN:
    			KeybY=1;
    		break;
    
    		case Display.KEY_LEFT:
    			KeybX=-1;
    		break;
    
    		case Display.KEY_RIGHT:
    			KeybX=1;
    		break;
    
    		case Display.KEY_SELECT:
    			KeybB=5;
    		break;
    
    		}*/
	}
}
   
   
   /*
   public void keyPressed(int keyCode)
   {
          currentkey = keyCode;
          rkey = keyCode;
          switch(getGameAction(keyCode))
          {      	 
    	      case Canvas.FIRE : currentkey = Canvas.FIRE; break;
    	      case Canvas.UP : currentkey = Canvas.UP ; break;
    	      case Canvas.DOWN : currentkey = Canvas.DOWN; break;
    	      case Canvas.RIGHT : currentkey = Canvas.RIGHT; break;
    	      case Canvas.LEFT : currentkey = Canvas.LEFT; break;	      
          }
          apretando = true;      
	}
	
   public void keyReleased(int keyCode)
   {
      currentkey = 0;
      apretando = false;     
   }*/

    private void UpdateSprite()
    {      
      int xdir = 0, ydir = 0;
      if (apretando)
    	 switch(currentkey)	
    	   {
    	        /*case Display.KEY_SELECT:
    	        nblocks = 0;
    	        break;*/
              case Display.KEY_UP:
              case Display.KEY_2: 
              //case Display.KEY_SELECT:
                                    ydir = -1;
                                    break;
              case Display.KEY_DOWN: 
                  ydir = 1;                                     
                  break;
              case Display.KEY_8: 
                  ydir = 1;                                                      
                  break;
              case Display.KEY_LEFT: 
              case Display.KEY_4: 
                  xdir = -1;                                                       
                  break;
              case Display.KEY_RIGHT: 
              case Display.KEY_6: 
                  xdir = 1;                                                                         
                  break;
              case Display.KEY_1:
                  ydir = -1;
                  xdir = -1;
                  break;
              case Display.KEY_3:
                  ydir = -1;
                  xdir = 1;                  
                  break;
              case Display.KEY_7:
                  ydir = 1;
                  xdir = -1;                  
                  break;
              case Display.KEY_9: 
                  ydir = 1;
                  xdir = 1;                 
              		break;
               case Display.KEY_SOFT1:
               case Display.KEY_SOFT2:                
                       estado=4;currentkey=0;break;              
              //case Canvas.KEY_STAR:dengine.resolution=1;break;
              //case Canvas.KEY_POUND:dengine.resolution=2;break;
           }
           
           if (rkey==-6){estado=4;rkey=0;}
           pacman.Move(xdir, ydir);
           
    }
	
	
	static Game joc;
	
    public Game()
 	{ 	
 	    w = getWidth();
 	    h = getHeight();
 	    estado = -22;   
 	    joc = this;        
 	    img = loadImg("/loading");	    
        menus = new MMenu();
        setSoftLabel(Frame.SOFT_KEY_1, "Menu");
	    setSoftLabel(Frame.SOFT_KEY_2, "Menu");
	    flujo=new Thread(this);
	    flujo.start();
	    
	    //gr.setColor(0xffffff);gr.fillRect(0,0,w,h);
	    //gr.drawImage(img,(w/2)-(img.getWidth()/2), (h/2)-(img.getHeight()/2));//, GHCENTER|Graphics.VCENTER);	         
	    
	    System.gc();  	     
  	}
  
  	
  	public static void SetEstado(int est, int pres)
  	{
  		currentkey = 0;
  		apretando = false;
  		estado = est;
  		prestado = pres;
  		rkey = 0;  	
  	}
  
    public static boolean Sclick()
    {
        if (apretando && (currentkey == Display.KEY_SOFT2 || currentkey == Display.KEY_SELECT)) return true;
        return false;
    }
    
    
    public static boolean click()
    {
        if (Sclick() || Cclick()) return true;
        return false;
    }
    
    
    public static boolean Cclick()
    {
        if (apretando && (currentkey == Display.KEY_SOFT1)) return true;
        return false;
    }
  
  
    
    
    int ccolor = 0xffffff;
    
    void Logos()
    {
        nfc++;
        if (nfc == 1) 
        {
           img = loadImg("/saiyu");
        }
        gr.setColor(ccolor);
        gr.fillRect(0,0,w,h);
        gr.drawImage(img,(w/2)-(img.getWidth()/2), (h/2)-(img.getHeight()/2));//, GHCENTER|Graphics.VCENTER);	         
        /*if (nfc == 80) 
        {
            //imenu = FS_LoadImage(1,18);//("/micro");ccolor=0xffffff;}   	
            //ccolor=0xffffff;
        }*/
        if (nfc == 70) 
        {
              img = _FS_LoadImage(0,1);
          
            //imenu = FS_LoadImage(1,17);
            SetEstado(4, 1);            
            Sonido(0, 1, false);
        }
    }
    
    
    public void paint(Graphics g)
    {   
         
        if (!pinta) return;        
        pinta = false;
        g.lock();        
   	    gr = g;   
        
        switch (estado)
   	    {
   	        case -21: break;
   	        case -22:   gr.setColor(0xffffff);gr.fillRect(0,0,w,h);
	                    gr.drawImage(img,(w/2)-(img.getWidth()/2), (h/2)-(img.getHeight()/2));//, GHCENTER|Graphics.VCENTER);	         
	                    estado = -21;
	                    break;
	        //case -23: break;
   	        case -2:    IniGame();break;       		   	           	        
   	        case -1:    Logos();break;
   		    case 0:     Pinta();break;
       		case 6:     end = true;break;       		
       		//case 13:    GameOver();break;
       		default:    estado = menus.show(gr,currentkey, apretando,w,h); 	        	  
   	    }
   	    ProgBarIMP(g);
   	    FS_ErrorDraw(g, w, h);   	    
   	    
   	    g.unlock(true);
    }




    public void Sonido(int s, int c, boolean vibr)
    {    	
        if (!Moto.sfx) return;    	        	        
        if (s == 0) c = 10;
        SoundSET(s, c);
        if (Moto.rocco && vibr) VibraSET(100);
    }
   
    

    public void buildsfx()
    {       
        SoundINI(); 
    }






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

	for (int i=0 ; i<Sound.length ; i++) {Sound[i] = LoadSound(3,i);}

	SoundPlayer.setMediaListener(this);
}

// -------------------
// Sound SET
// ===================

public void SoundSET(int Ary, int Loop)
{
	SoundRES();

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


    

	
}//END midlet




   