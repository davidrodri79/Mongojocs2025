package com.mygdx.mongojocs.frozenworld;


// -------------------------------------
// Scroll Class v1.3 - Rev.6 (2.3.2003)
// =====================================
// Programado por Juan Antonio G�mez
// Para uso exclusivo de Microjocs S.L.
// -------------------------------------





// *******************
// -------------------
// Scroll - Engine
// ===================
// *******************

import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class Scroll
{

byte[] FaseMap;
int FaseSizeX;
int FaseSizeY;
int FaseX;
int FaseY;

Image FondoImg;
Graphics FondoGfx;
byte[] FondoMap;
int FondoSizeX;
int FondoSizeY;
int FondoX;
int FondoY;

Image TilesImg;

int TilesLineX;

int ScrollX;
int ScrollY;
int ScrollSizeX;
int ScrollSizeY;
//int deb = 1;

// -------------------
// Scroll Constructor
// ===================

public Scroll()
{
	
}


// -------------------
// Scroll INI
// ===================

public void ScrollINI(int SizeX, int SizeY)
{
	ScrollX=0;
	ScrollY=0;
	ScrollSizeX=SizeX;
	ScrollSizeY=SizeY;

	FondoSizeX=(SizeX/24)+((SizeX%24==0)?1:2);
	FondoSizeY=(SizeY/24)+((SizeY%24==0)?1:2);
	FondoMap = new byte[FondoSizeX*FondoSizeY];

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			FondoImg=new Image();
			FondoImg._createImage(FondoSizeX*24, FondoSizeY*24);
			FondoGfx=FondoImg.getGraphics();
		}
	});

}


// -------------------
// Scroll SET
// ===================

public void ScrollSET(byte[] Mapa, int SizeX, int SizeY,  Image Img, int LineX)
{
	FaseMap=Mapa;
	FaseSizeX=SizeX;
	FaseSizeY=SizeY;

	TilesImg=Img;
	TilesLineX=LineX;

	FaseX=0;
	FaseY=0;

	FondoX=0;
	FondoY=0;

	for (int i=0 ; i<FondoMap.length ; i++) {FondoMap[i]=-1;}

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			ScrollUpdate();
		}
	});
}


// -------------------
// Scroll RUN
// ===================

public void ScrollRUN(int X, int Y)
{
	while (X<0) {X+=(FaseSizeX*24);}
	while (Y<0) {Y+=(FaseSizeY*24);}

	while (X>=FaseSizeX*24) {X-=(FaseSizeX*24);}
	while (Y>=FaseSizeY*24) {Y-=(FaseSizeY*24);}

	ScrollX=X;
	ScrollY=Y;

	FaseX=(X/24);
	FaseY=(Y/24);

	FondoX=(X/24)%FondoSizeX;
	FondoY=(Y/24)%FondoSizeY;

	ScrollUpdate();
}


public void ScrollRUN_Max(int X, int Y)
{   
	if (X<0) {X=0;} else if (X>=(FaseSizeX*24)-ScrollSizeX) {X=(FaseSizeX*24)-ScrollSizeX-1;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*24)-ScrollSizeY) {Y=(FaseSizeY*24)-ScrollSizeY;}

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
	FondoGfx.setClip(0, 0,  FondoSizeX*24, FondoSizeY*24);

	int FondoDir=0;
	int CorX=FaseX+(FondoSizeX-FondoX);
	int CorY=FaseY+(FondoSizeY-FondoY);

	if (CorY< 0) {CorY+=FaseSizeY;}
	if (CorY>=FaseSizeY) {CorY-=FaseSizeY;}

	if (CorX< 0) {CorX+=FaseSizeX;}
	if (CorX>=FaseSizeX) {CorX-=FaseSizeX;}

	for (int y=0 ; y<FondoSizeY ; y++)
	{
	if (y==FondoY) {if ((CorY-=FondoSizeY) < 0) {CorY+=FaseSizeY;}}

		for (int x=0, x2=FondoX, i=0 ; i<2; i++)
		{
			for (; x<x2 ; x++)
			{
				int FaseDir=(CorY*FaseSizeX)+CorX; if (++CorX >= FaseSizeX) {CorX-=FaseSizeX;}

				if (FondoMap[FondoDir++] != FaseMap[FaseDir])
				{
    				int TileNum=FaseMap[FaseDir];
    				FondoMap[FondoDir-1] = (byte)TileNum;
    				if (TileNum < 0 ) {TileNum+=256;}
    				
    				
    				FondoGfx.setClip(x*24 ,y*24, 24,24);									
    				if (!GameCanvas.inList(GameCanvas.LST_SIMPLE, TileNum)) 
    				{
    				    int TileNum2 = GameCanvas.getBack(FaseDir);
    				    FondoGfx.drawImage(TilesImg, (x*24)-((TileNum2%TilesLineX)*24), (y*24)-((TileNum2/TilesLineX)*24), Graphics.TOP|Graphics.LEFT);
    				}
    				FondoGfx.drawImage(TilesImg, (x*24)-((TileNum%TilesLineX)*24), (y*24)-((TileNum/TilesLineX)*24), Graphics.TOP|Graphics.LEFT);
				}
			}

			if (i==0)
			{
			if ((CorX-=FondoSizeX) < 0) {CorX+=FaseSizeX;}
			x2=FondoSizeX;
			} else {
			if (++CorY >= FaseSizeY) {CorY-=FaseSizeY;}
			}
		}
	}
}


// -------------------
// Scroll IMP
// ===================

public void ScrollIMP(Image Img)
{
	ScrollIMP(Img.getGraphics());
}

public void ScrollIMP(Graphics Gfx)
{
	int x=ScrollX%24;
	int y=ScrollY%24;

	int sx=((FondoSizeX-FondoX)*24)-x;
	int sy=((FondoSizeY-FondoY)*24)-y;//+Game.OffY;

	int px=ScrollSizeX-sx;
	int py=ScrollSizeY-sy;

	int nx=-(FondoX*24)-x;
	int ny=-(FondoY*24)-y;//+Game.OffY;


	if (FondoY==0)
	{
		if (FondoX==0)
		{
		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
		} else {
		Gfx.setClip( 0,  0, sx, ScrollSizeY);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

		Gfx.setClip(sx,  0, px, ScrollSizeY);
		Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);
		}

	} else {

	if (FondoX==0)
	{
	Gfx.setClip( 0,  0, ScrollSizeX, sy);
	Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

	Gfx.setClip( 0, sy, ScrollSizeX, py);
	Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);

	} else {

	Gfx.setClip( 0,  0, sx, sy);
	Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

	Gfx.setClip(sx,  0, px, sy);
	Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);


	Gfx.setClip( 0, sy, sx, py);
	Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);

	Gfx.setClip(sx, sy, px, py);
	Gfx.drawImage(FondoImg, sx, sy  , Graphics.TOP|Graphics.LEFT);
	}
	}

}


// -------------------
// Scroll END
// ===================

public void ScrollEND()
{
	FaseMap=null;
	FondoMap=null;
	TilesImg=null;
	FondoGfx=null; FondoImg=null;
}


// -------------------
// Image SET
// ===================

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, Graphics Gfx, int DestX, int DestY)
{
	Gfx.setClip(0,0, ScrollSizeX, ScrollSizeY);
	Gfx.clipRect(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	
//	Gfx.setClip(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX-ScrollX, DestY-SourY-ScrollY, Graphics.TOP|Graphics.LEFT);
}

// <=- <=- <=- <=- <=-


};