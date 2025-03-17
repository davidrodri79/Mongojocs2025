package com.mygdx.mongojocs.poyogarden;


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
int[] FondoMap;
//byte [] FondoMap2;
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

	FondoSizeX=(SizeX/16)+((SizeX%16==0)?1:2);
	FondoSizeY=(SizeY/16)+((SizeY%16==0)?1:2);
	FondoMap = new int[FondoSizeX*FondoSizeY];
    //FondoMap2 = new byte[FondoSizeX*FondoSizeY];

	FondoImg = new Image();
	FondoImg._createImage(FondoSizeX*16, FondoSizeY*16);
	FondoGfx=FondoImg.getGraphics();
}


// -------------------
// Scroll SET
// ===================

public void ScrollSET(byte[] Mapa, int SizeX, int SizeY,  Image Img, int LineX)
{
	FaseMap=Mapa;
	FaseSizeX=SizeX/2;
	FaseSizeY=SizeY/2;

	TilesImg=Img;
	TilesLineX=LineX/2;

	FaseX=0;
	FaseY=0;

	FondoX=0;
	FondoY=0;

	for (int i=0 ; i<FondoMap.length ; i++) {FondoMap[i]=-1;}
	//for (int i=0 ; i<FondoMap2.length ; i++) {FondoMap[i]=0;}

	ScrollUpdate();
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

/*
public void ScrollRUN_Max(int X, int Y)
{

	if (X<0) {X=0;} else if (X>=(FaseSizeX*16)-ScrollSizeX) {X=(FaseSizeX*16)-ScrollSizeX-1;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*16)-ScrollSizeY) {Y=(FaseSizeY*16)-ScrollSizeY;}

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

*/

/*
public int SetFondo0(int x, int y)
{
    if (x < FaseX || y < FaseY)
        return -1;
    x -= FaseX;     
    y -= FaseY;

    if (FondoSizeX - FondoX < x)
    {    
        x += FondoX;        
    }
    else 
    {
        x += FondoX;        
        x -= FondoSizeX -FondoX;
    }
    
    if (FondoSizeY - FondoY < y)
    {    
        y += FondoY;        
    }
    else 
    {
        y += FondoY;        
        y -= FondoSizeY - FondoY;
    }
    
    int res = x + (y*FondoSizeX);
    if (res < 0 || res >= FondoMap.length) return -2;
    
    return FondoMap[x + (y*FondoSizeX)] = 0;
    
    if (x < FondoX || y < FondoY)
        return -1;
    x -= FondoX;     
    y -= FondoY;
    if (x >= FondoSizeX|| y >= FondoSizeY) 
        return -2;
    return FondoMap[(y*FondoSizeX)+FondoX] = 0;
}
*/
// -------------------
// Scroll Update
// ===================

public void ScrollUpdate()
{
	FondoGfx.setClip(0, 0,  FondoSizeX*16, FondoSizeY*16);

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
				int FaseDir=((CorY*FaseSizeX*4))+(CorX*2); if (++CorX >= FaseSizeX) {CorX-=FaseSizeX;}
                
    			int TileNum = FaseMap[FaseDir];
    			int fistro = (int)(FaseDir^(TileNum<<8));
    			
				if (FondoMap[FondoDir++] != fistro)// || FondoMap2[FondoDir-1] != FaseMap[FaseDir])
				{
				    //System.out.println("*"+fistro);
		    		FondoMap[FondoDir-1] = fistro;
			    	if (TileNum < 0 ) {TileNum+=256;}
				    FondoGfx.setClip(x*16 ,y*16,  16,16);				
			        
				    int tx = TileNum%16;
				    int ty = TileNum / 32;
				    TileNum = (tx/2) + (ty*8);				
								
				    FondoGfx.drawImage(TilesImg, (x*16)-((TileNum%TilesLineX)*16), (y*16)-((TileNum/TilesLineX)*16), Graphics.TOP|Graphics.LEFT);

				    if (CorY < (FaseSizeY)-1)
				    {
				        int TileNum2 = FaseMap[FaseDir+(FaseSizeX*4)];
				        if (TileNum2 < 0 ) {TileNum2+=256;}					        
				        if (Game.inList(Game.RefreshList, TileNum2))
	    			    //switch(TileNum2)
    	    		    {
        				   /*     case 230:
        				        case 166:
        				        case 236:
        				        case 228:*/
        				        //default: 
        				            TileNum2-=32;
        				          int tx2 = TileNum2%16;
    				              int ty2 = TileNum2 / 32;
    				            TileNum2 = (tx2/2) + (ty2*8);		 
        				        FondoGfx.drawImage(TilesImg, (x*16)-(((TileNum2)%TilesLineX)*16), (y*16)-(((TileNum2)/TilesLineX)*16), Graphics.TOP|Graphics.LEFT);
        				       
        						  //break;								
    			        }
				    }
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

/*
public void ScrollIMP(Image Img)
{
	ScrollIMP(Img.getGraphics());
}
*/

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
// Scroll END
// ===================
/*
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
*/
// <=- <=- <=- <=- <=-


};