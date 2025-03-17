package com.mygdx.mongojocs.nopasseu;


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
byte[] TileComb;
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

Image TilesAImg;
Image TilesBImg;
int TilesLineX;

int ScrollX;
int ScrollY;
int ScrollSizeX;
int ScrollSizeY;

boolean firstTime;

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
	ScrollSizeX=SizeX;
	ScrollSizeY=SizeY;

	FondoSizeX=(SizeX/16)+((SizeX%16==0)?1:2);
	FondoSizeY=(SizeY/16)+((SizeY%16==0)?1:2);
	FondoMap = new byte[FondoSizeX*FondoSizeY];

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			FondoImg=new Image();
			FondoImg._createImage(FondoSizeX*16, FondoSizeY*16);
			FondoGfx=FondoImg.getGraphics();
		}
	});

}


// -------------------
// Scroll SET
// ===================

public void ScrollSET(byte[] Mapa, byte[] comb, int SizeX, int SizeY, Image ImgA, Image ImgB, int LineX)
{
	ScrollX=0;
	ScrollY=0;

	FaseMap=Mapa;
	TileComb=comb;

	FaseSizeX=SizeX;
	FaseSizeY=SizeY;

	TilesAImg=ImgA;
	TilesBImg=ImgB;
	TilesLineX=LineX;

	FaseX=0;
	FaseY=0;

	FondoX=0;
	FondoY=0;

	for (int i=0 ; i<FondoMap.length ; i++) {FondoMap[i]=-1;}

	ScrollUpdate_outsideMainThread();

	firstTime = true;
}


// -------------------
// Scroll RUN
// ===================

public void ScrollRUN(int X, int Y)
{
	while (X<0) {X+=(FaseSizeX*16);}
	while (Y<0) {Y+=(FaseSizeY*16);}

	while (X>=FaseSizeX*16) {X-=(FaseSizeX*16);}
	while (Y>=FaseSizeY*16) {Y-=(FaseSizeY*16);}

	ScrollX=X;
	ScrollY=Y;

	FaseX=(X/16);
	FaseY=(Y/16);

	FondoX=(X/16)%FondoSizeX;
	FondoY=(Y/16)%FondoSizeY;

	ScrollUpdate();
}


public void ScrollRUN_Max(int X, int Y)
{

	if (X<0) {X=0;} else if (X>=(FaseSizeX*16)-ScrollSizeX) {X=(FaseSizeX*16)-ScrollSizeX;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*16)-ScrollSizeY) {Y=(FaseSizeY*16)-ScrollSizeY;}

	ScrollRUN(X,Y);
}


public void ScrollRUN_Centro(int X, int Y)
{
	ScrollRUN(X-(ScrollSizeX/2), Y-(ScrollSizeY/2));
}


public void ScrollRUN_Centro_Max(int X, int Y)
{
//	ScrollRUN_Max(X-(ScrollSizeX/2), Y-(ScrollSizeY/2));

/*
	Y = Y-(ScrollSizeY/2);
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*16)-ScrollSizeY) {Y=(FaseSizeY*16)-ScrollSizeY;}
	ScrollY = Y;
*/

	X-=(ScrollSizeX/2);
	Y-=(ScrollSizeY/2);

	if (X<0) {X=0;} else if (X>=(FaseSizeX*16)-ScrollSizeX) {X=(FaseSizeX*16)-ScrollSizeX;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*16)-ScrollSizeY) {Y=(FaseSizeY*16)-ScrollSizeY;}

	while (X<0) {X+=(FaseSizeX*16);}
	while (Y<0) {Y+=(FaseSizeY*16);}

	while (X>=FaseSizeX*16) {X-=(FaseSizeX*16);}
	while (Y>=FaseSizeY*16) {Y-=(FaseSizeY*16);}

	if (firstTime || ScrollX-X < -ScrollSizeX || ScrollX-X > ScrollSizeX) {ScrollX=X;}
	if (firstTime || ScrollY-Y < -ScrollSizeY || ScrollY-Y > ScrollSizeY) {ScrollY=Y;}

	firstTime = false;

	if (X > ScrollX) {ScrollX+=((X-ScrollX)>>3)+1;}
	if (X < ScrollX) {ScrollX-=((ScrollX-X)>>3)+1;}
	if (Y > ScrollY) {ScrollY+=((Y-ScrollY)>>1)+1;}
	if (Y < ScrollY) {ScrollY-=((ScrollY-Y)>>1)+1;}


	FaseX=(ScrollX>>4);
	FaseY=(ScrollY>>4);

	FondoX=FaseX%FondoSizeX;
	FondoY=FaseY%FondoSizeY;

	ScrollUpdate();
}



// -------------------
// Scroll Update
// ===================

	public static boolean mainThreadWaiting = false;

	public void ScrollUpdate()
	{
				try {


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

								if (FaseDir >= 0 && FaseDir < FaseMap.length)
								{
									if (FondoMap[FondoDir++] != FaseMap[FaseDir])
									{
										int TileNum=FaseMap[FaseDir];
										FondoMap[FondoDir-1] = (byte)TileNum;

										TileNum--;
										if (TileNum < 0 ) {TileNum+=256;}
										FondoGfx.setClip(x*16 ,y*16,  16,16);

										if (TileNum < 71)
										{
											if (TileNum < 30)
											{
												FondoGfx.drawImage(TilesAImg, (x*16)-((TileNum%TilesLineX)*16), (y*16)-((TileNum/TilesLineX)*16), Graphics.TOP|Graphics.LEFT);
											} else {
												TileNum -= 30;
												FondoGfx.drawImage(TilesBImg, (x*16)-((TileNum%TilesLineX)*16), (y*16)-((TileNum/TilesLineX)*16), Graphics.TOP|Graphics.LEFT);
											}

										} else {

											int comTile = (TileNum - 71)*5;
											for (int z=0 ; z<5 ;z++)
											{
												TileNum = TileComb[3+(comTile++)]-1;
												if (TileNum < 0) {continue;}
												if (TileNum < 30)
												{
													FondoGfx.drawImage(TilesAImg, (x*16)-((TileNum%TilesLineX)*16), (y*16)-((TileNum/TilesLineX)*16), Graphics.TOP|Graphics.LEFT);
												} else {
													TileNum -= 30;
													FondoGfx.drawImage(TilesBImg, (x*16)-((TileNum%TilesLineX)*16), (y*16)-((TileNum/TilesLineX)*16), Graphics.TOP|Graphics.LEFT);
												}
											}

										}
									}
								} else {
									FondoDir++;
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


				} catch (Exception e) {ScrollX=0; ScrollY=0;}

	}

public void ScrollUpdate_outsideMainThread()
{
	mainThreadWaiting = true;

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {

			ScrollUpdate();
			mainThreadWaiting = false;

		}
	});

	while (mainThreadWaiting)
	{
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	int x=ScrollX%16;
	int y=ScrollY%16;

	int sx=((FondoSizeX-FondoX)*16)-x;
	int sy=((FondoSizeY-FondoY)*16)-y;

	int px=ScrollSizeX-sx;
	int py=ScrollSizeY-sy;

	int nx=-(FondoX*16)-x;
	int ny=-(FondoY*16)-y;


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
// Scroll RES
// ===================

public void ScrollRES()
{
	FaseMap=null;
	TileComb=null;
}


// -------------------
// Scroll END
// ===================

public void ScrollEND()
{
	FaseMap=null;
	FondoMap=null;
	TilesAImg=null;
	TilesBImg=null;
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