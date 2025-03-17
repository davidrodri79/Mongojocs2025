package com.mygdx.mongojocs.astro;


// -------------------------------------
// Scroll Class v1.3 - Rev.6 (2.3.2003)
// Version que NO auto-refresca Mapa. Rev.2 (22.5.2003)
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

int Desp=0;

byte[] FaseMap;
int FaseSizeX;
int FaseSizeY;
int FaseX;
int FaseY;

Image FondoImg;
Graphics FondoGfx;
byte[] FondoMap;
int[] FondoEjeX;
int[] FondoEjeY;
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

	FondoSizeX=(SizeX/8)+((SizeX%8==0)?1:2);
	FondoSizeY=(SizeY/8)+((SizeY%8==0)?1:2);
	FondoMap = new byte[FondoSizeX*FondoSizeY];
	FondoEjeX = new int[FondoSizeX];
	FondoEjeY = new int[FondoSizeY];

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			FondoImg=new Image();
			FondoImg._createImage(FondoSizeX*8, FondoSizeY*8);
			FondoGfx=FondoImg.getGraphics();
			FondoGfx.setClip(0,0, 176,208);
			FondoGfx.setColor(0, 255, 0);
			FondoGfx.fillRect(0,0, 176,208);
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
	for (int i=0 ; i<FondoEjeX.length ; i++) {FondoEjeX[i]=-1;}
	for (int i=0 ; i<FondoEjeY.length ; i++) {FondoEjeY[i]=-1;}

	ScrollUpdate_outsideMainThread();


}


// -------------------
// Scroll RUN
// ===================

public void ScrollRUN(int X, int Y)
{
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

	if (X<0) {X=0;} else if (X>=(FaseSizeX*8)-ScrollSizeX) {X=(FaseSizeX*8)-ScrollSizeX-1;}
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

public static boolean waitingForMainThread = false;

	public void ScrollUpdate_outsideMainThread()
	{
		waitingForMainThread = true;

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				ScrollUpdate();
				waitingForMainThread = false;
			}
		});

		while(waitingForMainThread)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

private void ScrollUpdate()
{

			int FondoDir=0;
			int CorX=FaseX+(FondoSizeX-FondoX);
			int CorY=FaseY+(FondoSizeY-FondoY);

			if (CorY< 0) {CorY+=FaseSizeY;}
			if (CorY>=FaseSizeY) {CorY-=FaseSizeY;}

			if (CorX< 0) {CorX+=FaseSizeX;}
			if (CorX>=FaseSizeX) {CorX-=FaseSizeX;}


			int CorX2=CorX;
			int CorY2=CorY;

			for (int y=0 ; y<FondoSizeY ; y++)
			{
				if (y==FondoY) {if ((CorY-=FondoSizeY) < 0) {CorY+=FaseSizeY;}}

				if (FondoEjeY[y] != CorY)
				{
					FondoEjeY[y] = CorY;

					for (int x=0 ; x<FondoSizeX ; x++)
					{
						if (x==FondoX) {if ((CorX-=FondoSizeX) < 0) {CorX+=FaseSizeX;}}

						int FaseDir=(CorY*FaseSizeX)+CorX;

						if (FondoMap[FondoDir++] != FaseMap[FaseDir])
						{
							int TileNum=FaseMap[FaseDir];
							FondoMap[FondoDir-1] = (byte)TileNum;
							if (TileNum < 0 ) {TileNum+=256;}
							// ESTO no está haciendo nada cuando se llama fuera del juego??
							FondoGfx.setClip(x*8 ,y*8,  8,8);
							FondoGfx.drawImage(TilesImg, (x*8)-((TileNum%TilesLineX)*8), (y*8)-((TileNum/TilesLineX)*8), Graphics.TOP|Graphics.LEFT);

						}
						if (++CorX >= FaseSizeX) {CorX-=FaseSizeX;}
					}
				} else {
					FondoDir+=FondoSizeX;
				}

				if (++CorY >= FaseSizeY) {CorY-=FaseSizeY;}
			}



			CorX=CorX2;
			CorY=CorY2;


			for (int x=0 ; x<FondoSizeX ; x++)
			{
				if (x==FondoX) {if ((CorX-=FondoSizeX) < 0) {CorX+=FaseSizeX;}}

				if (FondoEjeX[x] != CorX)
				{
					FondoEjeX[x] = CorX;

					for (int y=0 ; y<FondoSizeY ; y++)
					{
						if (y==FondoY) {if ((CorY-=FondoSizeY) < 0) {CorY+=FaseSizeY;}}

						int FaseDir=(CorY*FaseSizeX)+CorX;
						FondoDir=(y*FondoSizeX)+x;

						if (FondoMap[FondoDir] != FaseMap[FaseDir])
						{
							int TileNum=FaseMap[FaseDir];
							FondoMap[FondoDir] = (byte)TileNum;
							if (TileNum < 0 ) {TileNum+=256;}
							FondoGfx.setClip(x*8 ,y*8,  8,8);
							FondoGfx.drawImage(TilesImg, (x*8)-((TileNum%TilesLineX)*8), (y*8)-((TileNum/TilesLineX)*8), Graphics.TOP|Graphics.LEFT);
						}
						if (++CorY >= FaseSizeY) {CorY-=FaseSizeY;}
					}
				}

				if (++CorX >= FaseSizeX) {CorX-=FaseSizeX;}
			}
}





// -------------------
// Scroll Gfx SET
// ===================

public void ScrollUpdate(int X, int Y)
{
	if (FaseX <= X && FaseX+FondoSizeX > X && FaseY <= Y && FaseY+FondoSizeY > Y)
	{
	FondoEjeX[X%FondoSizeX]=-1;
	FondoEjeY[Y%FondoSizeY]=-1;
	}
}


public void ScrollUpdate(int X, int Y, int SizeX, int SizeY)
{
	if (SizeX <= SizeY)
	{
	for (int x=0 ; x<SizeX ; x++) {ScrollUpdate(X+x,Y);}
	} else {
	for (int y=0 ; y<SizeY ; y++) {ScrollUpdate(X,Y+y);}
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
	int x=ScrollX%8;
	int y=ScrollY%8;

	int sx=((FondoSizeX-FondoX)*8)-x;
	int sy=((FondoSizeY-FondoY)*8)-y;

	int px=ScrollSizeX-sx;
	int py=ScrollSizeY-sy;

	int nx=-(FondoX*8)-x;
	int ny=-(FondoY*8)-y;


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

	//MONGO
	/*Gfx.setClip(0, 0, 176, 208);
	Gfx.drawImage(FondoImg, 0, 0  , Graphics.TOP|Graphics.LEFT);*/
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