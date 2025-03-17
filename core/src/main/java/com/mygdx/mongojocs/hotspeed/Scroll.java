package com.mygdx.mongojocs.hotspeed;


// -------------------------------------
// Scroll Class v1.3 - Rev.6 (2.3.2003)
// =====================================
// Version para iMode (13.10.2003)
// -------------------------------------
// Conversion que Pre-calcula GIFs desde Scratchpad - Rev.0 (5.12.2003)
// -=> SOLO para GIFs de 128 colores <=-
// -------------------------------------
// Programado por Juan Antonio G�mez
// Para uso exclusivo de Microjocs S.L.
// -------------------------------------



// *******************
// -------------------
// Scroll - Engine
// ===================
// *******************

import com.mygdx.mongojocs.iapplicationemu.Connector;
import com.mygdx.mongojocs.iapplicationemu.Graphics;
import com.mygdx.mongojocs.iapplicationemu.Image;
import com.mygdx.mongojocs.iapplicationemu.MediaImage;
import com.mygdx.mongojocs.iapplicationemu.MediaManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

public class Scroll
{

private static final int CTE_NUM_PIEZAS = 172;

static final int ScratchPos = 85000;	// Index del bufer dentro del Scratch-pad
static final int FondoMaxX = 112;	// Size MAXIMO de X en pixels por Pantalla
static final int FondoMaxY = 64;	// Size MAXIMO de Y en pixels por Pantalla
//static final int FondoMaxY = 16*8;	// Size MAXIMO de Y en pixels por Pantalla

public byte[] ScrollGifData = new byte[]
{
0x47,0x49,0x46,0x38,0x39,0x61,	// "GIF89a"
0x00,0x00,	// Screen Width
0x00,0x00,	// Screnn Height
(byte)0xF6,	// Control Bits
0x00,		// Background Color
0x00,		// Pixel Ratio
// ------ Paleta ------
0x2C,		// Image Descriptor
0x00,0x00,	// Left
0x00,0x00,	// Top
0x00,0x00,	// Size X
0x00,0x00,	// Size Y
0x00,
// ------ LZW ------
};

byte[] FaseMap;
byte[] FaseXMap;

int FaseSizeX;
int FaseSizeY;
int FaseX;
int FaseY;

Image[] FondoImg;

int ScrollX;
int ScrollY;
int ScrollSizeX;
int ScrollSizeY;


int BloqSizeX;
int BloqSizeY;

int FondoSizeX;
int FondoSizeY;

int NumPiezas;

GameCanvas gc;

// -------------------
// Scroll Constructor
// ===================

public Scroll(GameCanvas gc) { this.gc = gc;}


// -------------------
// Scroll INI
// ===================

public void ScrollINI(int SizeX, int SizeY)
{
	ScrollSizeX=SizeX;
	ScrollSizeY=SizeY;
}


// -------------------
// Scroll SET
// ===================

public void ScrollSET(byte[] Mapa, byte[] XMapa,  int SizeX, int SizeY,  byte[] Tiles, byte[] Paleta)
{
	ScrollX=0;
	ScrollY=0;

	FaseMap=Mapa;
	FaseXMap=XMapa;
	FaseSizeX=SizeX;
	FaseSizeY=SizeY;

	FaseX=0;
	FaseY=0;


	FondoSizeX = FondoMaxX / 8;
	FondoSizeY = FondoMaxY / 8;


	BloqSizeX = FaseSizeX / FondoSizeX;
	BloqSizeY = FaseSizeY / FondoSizeY;

	if (FaseSizeX % FondoSizeX != 0) {BloqSizeX++;}
	if (FaseSizeY % FondoSizeY != 0) {BloqSizeY++;}



	FondoImg = new Image[BloqSizeX * BloqSizeY];


	NumPiezas = XMapa.length;

	
	
	for (int BX=0 ; BX<BloqSizeX ; BX++)
	{
		for (int BY=0 ; BY<BloqSizeY ; BY++)
		{
			int AnchoSizeX = FondoSizeX;
			int AnchoSizeY = FondoSizeY;


			if (BX==BloqSizeX-1) {AnchoSizeX = FaseSizeX-(BX*FondoSizeX);}
			if (BY==BloqSizeY-1) {AnchoSizeY = FaseSizeY-(BY*FondoSizeY);}


			int FondoPixeX = AnchoSizeX * 8;
			int FondoPixeY = AnchoSizeY * 8;


			byte[] Fondo = new byte[(FondoPixeX * FondoPixeY)];


			for (int Y=0 ; Y<AnchoSizeY ; Y++)
			{
				for (int X=0 ; X<AnchoSizeX ; X++)
				{
					int Tile = Mapa[ (BY*FondoSizeY*FaseSizeX) + (BX*FondoSizeX) + (Y*FaseSizeX)+X] & 0xFF;

					for (int y=0 ; y<8 ; y++)
					{
						for (int x=0 ; x<8 ; x++)
						{
						try {
						Fondo[(Y*FondoPixeX*8)+((X)*8)+(y*FondoPixeX)+x] = Tiles[(Tile*8*8)+(y*8)+x];
					} catch (Exception e) {}

					}
					}
				}
			}


			ScrollGifData[6] = (byte)(FondoPixeX & 0xFF); ScrollGifData[7] = (byte)(FondoPixeX >> 8);
			ScrollGifData[8] = (byte)(FondoPixeY & 0xFF); ScrollGifData[9] = (byte)(FondoPixeY >> 8);
			ScrollGifData[18] = ScrollGifData[6]; ScrollGifData[19] = ScrollGifData[7];
			ScrollGifData[20] = ScrollGifData[8]; ScrollGifData[21] = ScrollGifData[9];



			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
			try
			{
				//OutputStream out = Connector.openOutputStream("scratchpad:///0;pos="+ScratchPos);

				DataOutputStream out = new DataOutputStream(bstream);

				out.write(ScrollGifData, 0, 13);
				out.write(Paleta, 0, 128*3);
				out.write(ScrollGifData, 13, 10);

				for (int i=0 ; i<Fondo.length ; i++) {Fondo[i] &= 0x7F;}

				int Size=96;

				out.write( 0x07 );	// Bits (7 = 128 colores)

				int pos=0;
				while (pos+Size < Fondo.length)
				{
				out.write(Size+1);
				out.write( 0x80 );
				out.write(Fondo, pos, Size);
				pos+=Size;
				}

				if (pos<Fondo.length)
				{
				out.write(Fondo.length-pos+1);
				out.write( 0x80 );
				out.write(Fondo, pos, Fondo.length-pos);
				}

				out.write( 0x00 );

				out.write( 0x3B );	// GIF END

				out.close();
			}
			catch (Exception e) {}



			MediaImage mimage = MediaManager.getImage(bstream.toByteArray()/*"scratchpad:///0;pos="+ScratchPos*/);

			try {
				mimage.use();
			} catch (Exception e) {}

			FondoImg[(BY*BloqSizeX)+BX] = mimage.getImage();//Image.createImage("hotspeed/1/31.gif");
					//mimage.getImage();

		/////////////////////////////////////////////
		gc.ProgBarADD();
		}
	}
	//gc.ProgBarEND();
}


// -------------------
// Scroll RUN
// ===================

public void ScrollRUN(int X, int Y)
{

//	while (X<0) {X+=(FaseSizeX*8);}
//	while (Y<0) {Y+=(FaseSizeY*8);}

//	while (X>=FaseSizeX*8) {X-=(FaseSizeX*8);}
//	while (Y>=FaseSizeY*8) {Y-=(FaseSizeY*8);}

	ScrollX=X;
	ScrollY=Y;


	
	
}


public void ScrollRUN_Max(int X, int Y)
{

	if (X<0) {X=0;} else if (X>=(FaseSizeX*8)-ScrollSizeX) {X=(FaseSizeX*8)-ScrollSizeX;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*8)-ScrollSizeY) {Y=(FaseSizeY*8)-ScrollSizeY;}

	ScrollRUN(X,Y);
}


public void ScrollRUN_Centro(int X, int Y)
{
	ScrollRUN(X-(ScrollSizeX/2), Y-(ScrollSizeY/2));
}


public void ScrollRUN_Centro_Max(int X, int Y)
{
	ScrollRUN_Max(X-(ScrollSizeX/2), Y-(ScrollSizeY/2));
}


// -------------------
// Scroll IMP
// ===================

public void ScrollIMP(Graphics Gfx)
{
	int SizeX = FondoSizeX * 8;
	int SizeY = FondoSizeY * 8;

int BY=0;
	try
	{
	
			int i=0;
			for (BY=0 ; BY < CTE_NUM_PIEZAS ; BY++)
			{
				Gfx.drawImage(FondoImg[(FaseXMap[BY%CTE_NUM_PIEZAS]-1)*BloqSizeX], -ScrollX, -ScrollY+(BY*SizeY));
	
			}
	}
	catch (Exception e) { System.out.println("ScrollIMP kaska: BY-> "+BY+" Pieza: "+(FaseXMap[BY]-1)); }
}


// -------------------
// Scroll END
// ===================

public void ScrollEND()
{
	FaseMap=null;
	FondoImg=null;
}

// <=- <=- <=- <=- <=-

};