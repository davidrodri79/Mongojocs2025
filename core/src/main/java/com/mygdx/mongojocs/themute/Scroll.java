


// -------------------------------------
// Scroll Class v1.3 - Rev.6 (2.3.2003)
// =====================================
// Programado por Juan Antonio G�mez
// Para uso exclusivo de Microjocs S.L.
// -------------------------------------


// ========================================
//    Version ESPECIAL para Nokia 3410
// ---------------------------------------
// Detecta BUG del drawImage y lo solventa
// ========================================
// ****************************************



package com.mygdx.mongojocs.themute;


import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;


// *******************
// -------------------
// Scroll - Engine
// ===================
// *******************

public class Scroll
{

int Factor=0;

int Desp=0;

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

byte[] ScrollTileTab;

// -------------------
// Scroll Constructor
// ===================

public Scroll()
{
//	Desp=Check3410_Bug();
}



// ------------------------------
// Check Nokia 3410 drawImage BUG
// ==============================

// Retorno: int a Sumar en TODOS los
// setClip (CoorX, CoorY,  SizeX+int , SizeY+int)
// que se hagan para copiar con drawImage

// Requerido:
// import com.nokia.mid.ui.*;			// clase CUSTOM de NOKIA

public int Check3410_Bug()
{
	System.gc();

	try
	{
	Image i1=Image.createImage(8, 2);
	Graphics g1 = i1.getGraphics();

	g1.setClip(0,0, 8,2);
	g1.setColor(-1);
	g1.fillRect(0,0, 8,2);

	Image i2=Image.createImage(8, 2);
	Graphics g2 = i2.getGraphics();

	g2.setClip(0,0, 8,2);
	g2.setColor(0);
	g2.fillRect(0,0, 8,2);

	g1.setClip(0,0, 4,2);
	g1.drawImage(i2, 0,0, 20);

	g1.setClip(0,0, 8,2);

	byte[] test = new byte[2];
	//DirectUtils.getDirectGraphics(g1).getPixels(test,null, 0,1, 3,0, 1,1, 1);

	g2=null;i2=null;g1=null;i1=null;

	System.gc();

	return test[0]!=0?0:1;
	}
	catch (Exception e)
	{
	return 0;
	}
}
// <=- <=- <=- <=- <=-





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

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			FondoImg=new Image();
			FondoImg._createImage((FondoSizeX*8)+2, (FondoSizeY*8)+2);
			FondoGfx=FondoImg.getGraphics();
		}
	});
}


// -------------------
// Scroll SET
// ===================

public void ScrollSET(byte[] Mapa, int SizeX, int SizeY,  Image Img, int LineX)
{
	ScrollX=0;
	ScrollY=0;

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

	ScrollUpdate_outsideMainThread();
}


// -------------------
// Scroll RUN
// ===================

public void ScrollRUN(int X, int Y)
{
	X-=(ScrollSizeX/2);
	Y-=(ScrollSizeY/2);

	if (X<0) {X=0;} else if (X>=(FaseSizeX*8)-ScrollSizeX) {X=(FaseSizeX*8)-ScrollSizeX-1;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*8)-ScrollSizeY) {Y=(FaseSizeY*8)-ScrollSizeY;}

	while (X<0) {X+=(FaseSizeX*8);}
	while (Y<0) {Y+=(FaseSizeY*8);}

	while (X>=FaseSizeX*8) {X-=(FaseSizeX*8);}
	while (Y>=FaseSizeY*8) {Y-=(FaseSizeY*8);}

	if (ScrollX-X < -ScrollSizeX || ScrollX-X > ScrollSizeX) {ScrollX=X;}
	if (ScrollY-Y < -ScrollSizeY || ScrollY-Y > ScrollSizeY) {ScrollY=Y;}

	if (X > ScrollX) {ScrollX+=((X-ScrollX)>>2)+1;}
	if (X < ScrollX) {ScrollX-=((ScrollX-X)>>2)+1;}
	if (Y > ScrollY) {ScrollY+=((Y-ScrollY)>>2)+1;}
	if (Y < ScrollY) {ScrollY-=((ScrollY-Y)>>2)+1;}

//	ScrollX=X;
//	ScrollY=Y;

	FaseX=(ScrollX>>3);
	FaseY=(ScrollY>>(3+Factor));

	FondoX=FaseX%FondoSizeX;
	FondoY=FaseY%FondoSizeY;

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

public void ScrollUpdate()
{

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
						int TileOld=TileNum;
						if (TileNum < 0 ) {TileNum+=256;}

						FondoGfx.setClip(x*8 ,y*8,  8+Desp,8+Desp);


						if (TileNum==0x6D)	// Targeta
						{
						int Til=FaseMap[FaseDir-1]; if (Til < 0 ) {Til+=256;}
						FondoGfx.drawImage(TilesImg, (x*8)-((Til%TilesLineX)*8), (y*8)-((Til/TilesLineX)*8), Graphics.TOP|Graphics.LEFT);
						}

						if ( (ScrollTileTab[TileOld+128] & 0xF0) != 0)
						{
						int Til=0xA3;	// Silla / Mesa / Monitor
						if (TileNum>=0xB4) {Til=0x30;}					// Sofa

						FondoGfx.drawImage(TilesImg, (x*8)-((Til%TilesLineX)*8), (y*8)-((Til/TilesLineX)*8), Graphics.TOP|Graphics.LEFT);
						}

						FondoGfx.drawImage(TilesImg, (x*8)-((TileNum%TilesLineX)*8), (y*8)-((TileNum/TilesLineX)*8), Graphics.TOP|Graphics.LEFT);
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
	int x=ScrollX%8;
	int y=(ScrollY>>Factor)%8;
//	int y=ScrollY%8;

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
		Gfx.setClip( 0,  0, ScrollSizeX+Desp, ScrollSizeY+Desp);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
		} else {
		Gfx.setClip( 0,  0, sx+Desp, ScrollSizeY+Desp);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

		Gfx.setClip(sx,  0, px+Desp, ScrollSizeY+Desp);
		Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);
		}

	} else {

	if (FondoX==0)
	{
	Gfx.setClip( 0,  0, ScrollSizeX+Desp, sy+Desp);
	Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

	Gfx.setClip( 0, sy, ScrollSizeX+Desp, py+Desp);
	Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);

	} else {

	Gfx.setClip( 0,  0, sx+Desp, sy+Desp);
	Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

	Gfx.setClip(sx,  0, px+Desp, sy+Desp);
	Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);


	Gfx.setClip( 0, sy, sx+Desp, py+Desp);
	Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);

	Gfx.setClip(sx, sy, px+Desp, py+Desp);
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
	if ((DestX       > ScrollX+ScrollSizeX)
	||	(DestX+SizeX < ScrollX)
	||	(DestY       > ScrollY+ScrollSizeY)
	||	(DestY+SizeY < ScrollY))
	{return;}

	Gfx.setClip(0,0, ScrollSizeX+Desp, ScrollSizeY+Desp);
	Gfx.clipRect(DestX-ScrollX, DestY-ScrollY, SizeX+Desp, SizeY+Desp);
	
//	Gfx.setClip(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX-ScrollX, DestY-SourY-ScrollY, Graphics.TOP|Graphics.LEFT);
}


public void ImageSET2(Image Sour, int SourX, int SourY, int SizeX, int SizeY, Graphics Gfx, int DestX, int DestY)
{
	Gfx.setClip(0,0, ScrollSizeX+Desp, ScrollSizeY+Desp);
	Gfx.clipRect(DestX-ScrollX, DestY-ScrollY, SizeX+Desp, SizeY+Desp);
	
//	Gfx.setClip(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX-ScrollX, DestY-SourY-ScrollY, Graphics.TOP|Graphics.LEFT);
}


// <=- <=- <=- <=- <=-


};