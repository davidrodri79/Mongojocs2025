package com.mygdx.mongojocs.defendinginca;


// -------------------------------------
// Scroll Class v1.3 - Rev.6 (2.3.2003)
// =====================================
// Version para iMode (13.10.2003)
// -------------------------------------
// Programado por Juan Antonio G�mez
// Para uso exclusivo de Microjocs S.L.
// -------------------------------------


// *******************
// -------------------
// Scroll - Engine
// ===================
// *******************

import com.mygdx.mongojocs.iapplicationemu.Graphics;
import com.mygdx.mongojocs.iapplicationemu.Image;

public class Scroll
{

byte[] FaseMap;
int FaseSizeX;
int FaseSizeY;
int FaseX;
int FaseY;

byte[] FondoMap;
int FondoSizeX;
int FondoSizeY;
int FondoX;
int FondoY;

Image TilesImg[];

int ScrollX;
int ScrollY;
int ScrollSizeX;
int ScrollSizeY;


// -------------------
// Scroll Constructor
// ===================

public Scroll()
{
}

GameCanvas gc;

// -------------------
// Scroll INI
// ===================

public void ScrollINI(int SizeX, int SizeY, GameCanvas GamCan)
{
	ScrollX=0;
	ScrollY=0;
	ScrollSizeX=SizeX;
	ScrollSizeY=SizeY;
	
	gc = GamCan;

	FondoSizeX=(SizeX/8);
	FondoSizeY=(SizeY/8);
	FondoMap = new byte[FondoSizeX*FondoSizeY];
}


// -------------------
// Scroll SET
// ===================

public void ScrollSET(byte[] Mapa, int SizeX, int SizeY,  Image[] Img)
{
	FaseMap=Mapa;
	FaseSizeX=SizeX;
	FaseSizeY=SizeY;

	TilesImg=Img;

	FaseX=0;
	FaseY=0;

	FondoX=0;
	FondoY=0;

	for (int i=0 ; i<FondoMap.length ; i++) {FondoMap[i]=-1;}

	ScrollUpdate();
}


// -------------------
// Scroll RUN
// ===================

public void ScrollRUN(int X, int Y)
{
	X&=-8;
	Y&=-8;

	while (X<0) {X+=(FaseSizeX*8);}
	while (Y<0) {Y+=(FaseSizeY*8);}

	while (X>=FaseSizeX*8) {X-=(FaseSizeX*8);}
	while (Y>=FaseSizeY*8) {Y-=(FaseSizeY*8);}

	ScrollX=X;
	ScrollY=Y;

	FaseX=(X/8);
	FaseY=(Y/8);

	FondoX=(X/8)%FondoSizeX;
	FondoY=(Y/8)%FondoSizeY;

	ScrollUpdate();
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
// Scroll Update
// ===================

public void ScrollUpdate()
{
}

public void ScrollUpdate_Now()
{
	if (FondoMap!=null)
	{
	for (int i=0 ; i<FondoMap.length ; i++) {FondoMap[i]=-1;}
	}
}


public void ScrollUpdate(int X, int Y, int SizeX, int SizeY)
{
	Y = Y - gc.DespY;
	SizeX = ((X+SizeX+7)/8)-(X/=8);
	SizeY = ((Y+SizeY+7)/8)-(Y/=8);
	
	for (int y=0 ; y<SizeY ; y++)
	{
		if ( Y+y >=0 && Y+y <FondoSizeY )
		{
			for (int x=0 ; x<SizeX ; x++)
			{
			if ( X+x >=0 && X+x <FondoSizeX ) {FondoMap[((Y+y)*FondoSizeX)+X+x]=-1;}
			}
		}
	}
}


// -------------------
// Scroll IMP
// ===================

public void ScrollIMP(Graphics Gfx)
{
//	Gfx.setColor(0xffffff);
//	Gfx.fillRect(0,0, FondoSizeX*8,FondoSizeY*8);
	int FaseDir = ((ScrollY/8)*FaseSizeX) + (ScrollX/8);

	int FondoDir=0;


	//try {
		for (int y=0 ; y<FondoSizeY ; y++)
		{
			for (int x=0 ; x<FondoSizeX ; x++)
			{
				if ( FaseMap[FaseDir+x] != FondoMap[FondoDir++] )
				{
				//FondoDir++;
					gc.ZonaFondo(x*8,(y*8)+gc.DespY,8,8,gc.ga.TramosFondo,((ScrollY!=0)?0:1));	
					int Tile = FaseMap[FaseDir+x];
					if (Tile>0)
					{
					Gfx.drawImage(TilesImg[Tile & 0xFF], x*8,gc.DespY+(y*8));
					}
				FondoMap[FondoDir-1] = (byte) Tile;
				}
				
			}
		FaseDir+=FaseSizeX;
		}

	//} catch (Exception e) {}


}


// -------------------
// Scroll END
// ===================

public void ScrollEND()
{
	FaseMap=null;
	FondoMap=null;
	TilesImg=null;
}


// -------------------
// Image SET
// ===================

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, Graphics Gfx, int DestX, int DestY)
{
/*
	Gfx.setClip(0,0, ScrollSizeX, ScrollSizeY);
	Gfx.clipRect(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	
//	Gfx.setClip(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX-ScrollX, DestY-SourY-ScrollY, Graphics.TOP|Graphics.LEFT);
*/
}

// <=- <=- <=- <=- <=-


};