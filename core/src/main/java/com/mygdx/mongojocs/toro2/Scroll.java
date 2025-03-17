package com.mygdx.mongojocs.toro2;


// -------------------------------------
// com.mygdx.mongojocs.toro2.Scroll Class v1.3 - Rev.6 (2.3.2003)
// Version que NO auto-refresca Mapa. Rev.2 (22.5.2003)
// =====================================
// Programado por Juan Antonio G�mez
// Para uso exclusivo de Microjocs S.L.
// -------------------------------------


//#ifdef J2ME

//#endif


// *******************
// -------------------
// com.mygdx.mongojocs.toro2.Scroll - Engine
// ===================
// *******************

import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class Scroll
{


//#ifdef S30
//#elifdef S40
//#elifdef S60
	static final int tileSize = 16;
//#elifdef S80
//#endif


//#ifdef J2ME


Image objetosImg;
byte[] objetosCor;
boolean barreraHack = true;

byte[] FaseMap;
byte[] TileComb;
byte[] FaseCol;
int FaseSizeX;
int FaseSizeY;
int FaseX;
int FaseY;

Image FondoImg;
Graphics FondoGfx;
short[] FondoMap;
int[] FondoEjeX;
int[] FondoEjeY;
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

// -------------------
// com.mygdx.mongojocs.toro2.Scroll INI
// ===================

public void ScrollINI(int SizeX, int SizeY)
{
	ScrollX=0;
	ScrollY=0;
	ScrollSizeX=SizeX;
	ScrollSizeY=SizeY;

	FondoSizeX=(SizeX/tileSize)+((SizeX%tileSize==0)?1:2);
	FondoSizeY=(SizeY/tileSize)+((SizeY%tileSize==0)?1:2);
	FondoMap = new short[FondoSizeX*FondoSizeY];
	FondoEjeX = new int[FondoSizeX];
	FondoEjeY = new int[FondoSizeY];

	Gdx.app.postRunnable(new Runnable() {
		@Override
		public void run() {
			FondoImg=new Image();
			FondoImg._createImage(FondoSizeX*tileSize, FondoSizeY*tileSize);
			FondoGfx=FondoImg.getGraphics();
		}
	});

}


// -------------------
// com.mygdx.mongojocs.toro2.Scroll SET
// ===================

public void ScrollSET(byte[] Mapa, byte[] coli, byte[] comb, int SizeX, int SizeY, Image ImgA, Image ImgB, int LineX)
{
	FaseMap=Mapa;
	FaseCol=coli;
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
}


// -------------------
// com.mygdx.mongojocs.toro2.Scroll RUN
// ===================

public void ScrollRUN(int X, int Y)
{
	while (X<0) {X+=(FaseSizeX*tileSize);}
	while (Y<0) {Y+=(FaseSizeY*tileSize);}

	while (X>=FaseSizeX*tileSize) {X-=(FaseSizeX*tileSize);}
	while (Y>=FaseSizeY*tileSize) {Y-=(FaseSizeY*tileSize);}

	ScrollX=X;
	ScrollY=Y;

	FaseX=(X/tileSize);
	FaseY=(Y/tileSize);

	FondoX=(X/tileSize)%FondoSizeX;
	FondoY=(Y/tileSize)%FondoSizeY;

	ScrollUpdate();
}


public void ScrollRUN_Max(int X, int Y)
{

	if (X<0) {X=0;} else if (X>=(FaseSizeX*tileSize)-ScrollSizeX) {X=(FaseSizeX*tileSize)-ScrollSizeX;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*tileSize)-ScrollSizeY) {Y=(FaseSizeY*tileSize)-ScrollSizeY;}

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
// com.mygdx.mongojocs.toro2.Scroll Update
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
			} catch(InterruptedException e)
			{

			}
		}
}

public void ScrollUpdate()
{



//	for (int i=0 ; i<FondoEjeX.length ; i++) {FondoEjeX[i] = -1;}
//	for (int i=0 ; i<FondoEjeY.length ; i++) {FondoEjeY[i] = -1;}


	try {

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

			short data = (short)((FaseCol[FaseDir]<<8) | (FaseMap[FaseDir]&0xff));
			if (FondoMap[FondoDir] != data)
			{
						int TileNum=FaseMap[FaseDir];
						FondoMap[FondoDir] = data;


// Render Tile
// -----------------------------------------------------------------
						TileNum--;
						if (TileNum < 0 ) {TileNum+=256;}
						FondoGfx.setClip(x*tileSize ,y*tileSize,  tileSize,tileSize);

						if (TileNum < 65)
						{
							if (TileNum < 32)
							{
								Graphics._drawImage(FondoGfx, TilesAImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
							} else {
								TileNum -= 32;
								Graphics._drawImage(FondoGfx, TilesBImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
							}

						} else {

							int comTile = (TileNum - 65)*5;
							for (int z=0 ; z<5 ;z++)
							{
								TileNum = TileComb[3+(comTile++)]-1;
								if (TileNum < 0) {continue;}
								if (TileNum < 32)
								{
									FondoGfx._drawImage(FondoGfx, TilesAImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
								} else {
									TileNum -= 32;
									FondoGfx._drawImage(FondoGfx, TilesBImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
								}
							}
						}


				switch (FaseCol[FaseDir])
				{
				case 0x0C:	// Rueda
					spriteDraw(objetosImg, x*tileSize, y*tileSize,    0, objetosCor);
				break;
	
				case 0x0D:	// Bidon
					spriteDraw(objetosImg, x*tileSize, y*tileSize,   27, objetosCor);
				break;
				case 0x2D:	// Bidon
					spriteDraw(objetosImg, x*tileSize, y*tileSize,   22, objetosCor);
				break;

				case 0x0E:	// Caja
					spriteDraw(objetosImg, x*tileSize, y*tileSize,   15, objetosCor);
				break;
				case 0x2E:	// Caja
					spriteDraw(objetosImg, x*tileSize, y*tileSize,   10, objetosCor);
				break;
	
				case 0x0F:	// Barrera

					if (FaseCol[FaseDir-1] != 0x0F || !barreraHack)
					{
						spriteDraw(objetosImg, x*tileSize, y*tileSize, 23, objetosCor);
						barreraHack = true;
					} else {
						spriteDraw(objetosImg, x*tileSize, y*tileSize, 24, objetosCor);
						barreraHack = false;
					}
				break;

				case 0x10:	// Rosa
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 40, objetosCor);
				break;
	
				case 0x11:	// Manzana
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 41, objetosCor);
				break;

				case 0x12:	// Caja de Rosas
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 42, objetosCor);
				break;

				case 0x13:	// Item Special
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 45, objetosCor);
				break;

				case 0x14:	// Bidon chafado
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 26, objetosCor);
				break;
				}

// -----------------------------------------------------------------


			}

		FondoDir++;

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

			short data = (short)((FaseCol[FaseDir]<<8) | (FaseMap[FaseDir]&0xff));
			if (FondoMap[FondoDir] != data)
			{

						int TileNum=FaseMap[FaseDir];
						FondoMap[FondoDir] = data;

// Render Tile
// -----------------------------------------------------------------
						TileNum--;
						if (TileNum < 0 ) {TileNum+=256;}
						FondoGfx.setClip(x*tileSize ,y*tileSize,  tileSize,tileSize);

						if (TileNum < 65)
						{
							if (TileNum < 32)
							{
								FondoGfx._drawImage(FondoGfx, TilesAImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
							} else {
								TileNum -= 32;
								FondoGfx._drawImage(FondoGfx, TilesBImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
							}

						} else {

							int comTile = (TileNum - 65)*5;
							for (int z=0 ; z<5 ;z++)
							{
								TileNum = TileComb[3+(comTile++)]-1;
								if (TileNum < 0) {continue;}
								if (TileNum < 32)
								{
									FondoGfx._drawImage(FondoGfx, TilesAImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
								} else {
									TileNum -= 32;
									FondoGfx._drawImage(FondoGfx, TilesBImg, (x*tileSize)-((TileNum%TilesLineX)*tileSize), (y*tileSize)-((TileNum/TilesLineX)*tileSize), Graphics.TOP|Graphics.LEFT);
								}
							}
						}


				switch (FaseCol[FaseDir])
				{
				case 0x0C:	// Rueda
					spriteDraw(objetosImg, x*tileSize, y*tileSize,  0, objetosCor);
				break;
	
				case 0x0D:	// Bidon
					spriteDraw(objetosImg, x*tileSize, y*tileSize,    27, objetosCor);
				break;
				case 0x2D:	// Bidon
					spriteDraw(objetosImg, x*tileSize, y*tileSize,    22, objetosCor);
				break;

				case 0x0E:	// Caja
					spriteDraw(objetosImg, x*tileSize, y*tileSize,   15, objetosCor);
				break;
				case 0x2E:	// Caja
					spriteDraw(objetosImg, x*tileSize, y*tileSize,   10, objetosCor);
				break;
	
				case 0x0F:	// Barrera

					if (FaseCol[FaseDir-1] != 0x0F || !barreraHack)
					{
						spriteDraw(objetosImg, x*tileSize, y*tileSize, 23, objetosCor);
						barreraHack = true;
					} else {
						spriteDraw(objetosImg, x*tileSize, y*tileSize, 24, objetosCor);
						barreraHack = false;
					}

				break;

				case 0x10:	// Rosa
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 40, objetosCor);
				break;
	
				case 0x11:	// Manzana
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 41, objetosCor);
				break;

				case 0x12:	// Caja de Rosas
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 42, objetosCor);
				break;

				case 0x13:	// Item Special
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 45, objetosCor);
				break;

				case 0x14:	// Bidon chafado
					spriteDraw(objetosImg, x*tileSize, y*tileSize, 26, objetosCor);
				break;
				}


// -----------------------------------------------------------------

			}
		if (++CorY >= FaseSizeY) {CorY-=FaseSizeY;}
		}
	}

	if (++CorX >= FaseSizeX) {CorX-=FaseSizeX;}
	}




	} catch(Exception e)
	{
	//#ifdef com.mygdx.mongojocs.toro2.Debug
		Debug.println(e.toString());
		Debug.println("com.mygdx.mongojocs.toro2.Scroll class Exception");
	//#endif
	}



}





// -------------------
// com.mygdx.mongojocs.toro2.Scroll Gfx SET
// ===================

public void ScrollUpdate(int X, int Y)
{
	if (FaseX <= X && FaseX+FondoSizeX > X && FaseY <= Y && FaseY+FondoSizeY > Y)
	{
	FondoEjeX[X%FondoSizeX]=-1;
//	FondoEjeY[Y%FondoSizeY]=-1;
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
// com.mygdx.mongojocs.toro2.Scroll IMP
// ===================

public void ScrollIMP(Graphics Gfx)
{
	int x=ScrollX%tileSize;
	int y=ScrollY%tileSize;

	int sx=((FondoSizeX-FondoX)*tileSize)-x;
	int sy=((FondoSizeY-FondoY)*tileSize)-y;

	int px=ScrollSizeX-sx;
	int py=ScrollSizeY-sy;

	int nx=-(FondoX*tileSize)-x;
	int ny=-(FondoY*tileSize)-y;


	if (FondoY==0)
	{
		if (FondoX==0)
		{
			Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
			Gfx._drawImage(Gfx, FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
		} else {
			Gfx.setClip( 0,  0, sx, ScrollSizeY);
			Gfx._drawImage(Gfx, FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

			Gfx.setClip(sx,  0, px, ScrollSizeY);
			Gfx._drawImage(Gfx, FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);
		}

	} else {

		if (FondoX==0)
		{
			Gfx.setClip( 0,  0, ScrollSizeX, sy);
			Gfx._drawImage(Gfx, FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
		
			Gfx.setClip( 0, sy, ScrollSizeX, py);
			Gfx._drawImage(Gfx, FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);
		} else {
			Gfx.setClip( 0,  0, sx, sy);
			Gfx._drawImage(Gfx, FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
		
			Gfx.setClip(sx,  0, px, sy);
			Gfx._drawImage(Gfx, FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);
		
		
			Gfx.setClip( 0, sy, sx, py);
			Gfx._drawImage(Gfx, FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);
		
			Gfx.setClip(sx, sy, px, py);
			Gfx._drawImage(Gfx, FondoImg, sx, sy  , Graphics.TOP|Graphics.LEFT);
		}
	}

}


// -------------------
// com.mygdx.mongojocs.toro2.Scroll RES
// ===================

public void ScrollRES()
{
	FaseMap=null;
	FaseCol=null;
	TileComb=null;
}

// -------------------
// com.mygdx.mongojocs.toro2.Scroll END
// ===================

public void ScrollEND()
{
	FaseMap=null;
	FaseCol=null;
	FondoMap=null;
	TileComb=null;
	TilesAImg=null;
	TilesBImg=null;
	FondoGfx=null; FondoImg=null;
}




public void spriteDraw(Image img,  int x, int y,  int frame, byte[] cor)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame  ] + 128;

	FondoGfx.setClip(destX, destY, sizeX, sizeY);
	FondoGfx.drawImage(img, destX-sourX, destY-sourY, 20);
}

// <=- <=- <=- <=- <=-


/*
byte[] objetosCor;
Image[] objetosImg;
int ScrollX, ScrollSizeX=108;
int ScrollY, ScrollSizeY=108;
public void ScrollINI(int SizeX, int SizeY) {}
public void ScrollSET(byte[] Mapa, byte[] coli, byte[] comb, int SizeX, int SizeY, Image ImgA, Image ImgB, int LineX) {}
public void ScrollRUN(int X, int Y) {}
public void ScrollRUN_Max(int X, int Y) {}
public void ScrollRUN_Centro(int X, int Y) {}
public void ScrollRUN_Centro_Max(int X, int Y) {}
public void ScrollUpdate() {}
public void ScrollUpdate(int X, int Y) {}
public void ScrollUpdate(int X, int Y, int SizeX, int SizeY) {}
public void ScrollIMP(Graphics Gfx) {}
public void ScrollRES() {}
public void ScrollEND() {}
*/








//#elifdef DOJA
//#endif




};