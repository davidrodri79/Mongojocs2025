package com.mygdx.mongojocs.unadezombis;


// ------------------------------------------------------
// Una de Zombis v1.0 Rev.0 (26.11.2003) - Canvas
// ======================================================
// Nokia series 60
// ------------------------------------




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

public class GameCanvas extends FullCanvas
{

Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga)
{
	this.ga = ga;
	System.gc();
	CanvasINI();
	System.gc();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics LCD_Gfx;
DirectGraphics LCD_dGfx;

long TimeNow, TimeOld;
int TimeDat, TimeCnt;

public void paint (Graphics g)
{       
	LCD_Gfx=g;
	LCD_dGfx= DirectUtils.getDirectGraphics(g);


//	try {
		CanvasRUN();
//	} catch (Exception e) {e.printStackTrace();e.toString();}



//	if (--TimeCnt < 1) {TimeCnt=50; TimeNow=System.currentTimeMillis(); TimeDat=(int)((TimeNow-TimeOld)/10); TimeOld=TimeNow;}

//	if (ga.ProtAnim!=-1)
	{
//	g.setClip( 0,0, CanvasSizeX, CanvasSizeY ); g.setColor(-1); g.fillRect(0,0,  62,12 ); g.setColor(0xff0000);
//	g.drawString(Integer.toString(ga.JugarBalas[ga.ProtType]),   0,0, Graphics.LEFT|Graphics.TOP);
//	g.drawString(Integer.toString(gera1),  20,0, Graphics.LEFT|Graphics.TOP);
//	g.drawString(Integer.toString(gera2),  40,0, Graphics.LEFT|Graphics.TOP);
	}
//	g.drawString(Integer.toString(TimeDat), 30,0, Graphics.LEFT|Graphics.TOP);


}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int KeybX, KeybY, KeybB, KeybM;

public void keyPressed(int keycode)
{
	KeybX=0; KeybY=0; KeybB=0; KeybM=0;

	switch(getGameAction(keycode))
	{
		case 1:KeybY=-1;break;	// Arriba
		case 6:KeybY=1;break;	// Abajo
		case 5:KeybX=1;break;	// Derecha
		case 2:KeybX=-1;break;	// Izquierda
		case 8:KeybM=2;break;	// Fuego
	}

	switch (keycode)
	{
	case Canvas.KEY_NUM0:
		KeybB=10;
	break;

	case Canvas.KEY_NUM1:
		KeybB=1; KeybX=-1; KeybY=-1;
	break;

	case Canvas.KEY_NUM2:	// Arriba
		KeybB=2; KeybY=-1;
	break;

	case Canvas.KEY_NUM3:
		KeybB=3; KeybX= 1; KeybY=-1;
	break;

	case Canvas.KEY_NUM4:	// Izquierda
		KeybB=4; KeybX=-1;
	break;

	case Canvas.KEY_NUM5:	// Disparo
		KeybB=5;
		KeybM=2;
	break;

	case Canvas.KEY_NUM6:	// Derecha
		KeybB=6; KeybX=1;
	break;

	case Canvas.KEY_NUM7:
		KeybB=7; KeybX=-1; KeybY= 1;
	break;

	case Canvas.KEY_NUM8:	// Abajo
		KeybB=8; KeybY=1;
	break;

	case Canvas.KEY_NUM9:
		KeybB=9; KeybX= 1; KeybY= 1;
	break;

	case 35:		// *
		KeybB=35;
	break;

	case 42:		// #
		KeybB=42;
	break;

// -----------------------------------------
	case -6:	// Nokia - Menu Izquierda
		KeybM=-1;
	break;

	case -7:	// Nokia - Menu Derecha
		KeybM= 1;
	break;
// =========================================
/*
// -----------------------------------------
	case -20:	// Motorola T720 - Menu Izquierda
		KeybM=-1;
	break;

	case -21:	// Motorola T720 - Menu Derecha
		KeybM= 1;
	break;
// =========================================
*/
	}

}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	KeybX=0; KeybY=0; KeybB=0; KeybM=0;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void ImageSET(Image Img)
{
	ImageSET(Img, 0,0, Img.getWidth(), Img.getHeight(), (CanvasSizeX-Img.getWidth())/2, (CanvasSizeY-Img.getHeight())/2);
}

// ----------------------------------------------------------

public void ImageSET(Image Img, int X, int Y)
{
	ImageSET(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	LCD_Gfx.setClip(0, 0, CanvasSizeX, CanvasSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	LCD_Gfx.setClip(TopX, TopY, FinX, FinY);
	LCD_Gfx.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void ImageSET(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX, Y+DestY);
}

// ----------------------------------------------------------

public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-sc.ScrollX, Y+DestY-sc.ScrollY, 0);	// Standard
}

// ----------------------------------------------------------


public void PutSprite(Image Img,  int X, int Y,  int Frame, int Base, int Rejilla, byte[] Coor)
{
	int Flip=0; if (Frame >= Base) {Flip = Frame / Base; Frame %= Base;}

	Frame*=6;

	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];

	switch (Flip)
	{
	case 0:
	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-sc.ScrollX, Y+DestY-sc.ScrollY, 0);	// Standard
	break;

	case 1:
	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+(Rejilla-DestY-SizeY)-sc.ScrollX, Y+DestX-sc.ScrollY, 5);	// Rotar 270
	break;

	case 2:
	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+(Rejilla-DestX-SizeX)-sc.ScrollX, Y+(Rejilla-DestY-SizeY)-sc.ScrollY, 3);	// Rotar 180
	break;

	case 3:
	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestY-sc.ScrollX, Y+(Rejilla-DestX-SizeX)-sc.ScrollY, 4);	// Rotar 90
	break;
	}
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       > sc.ScrollSizeX)
	||	(DestX+SizeX < 0)
	||	(DestY       > sc.ScrollSizeY)
	||	(DestY+SizeY < 0))
	{return;}

	LCD_Gfx.setClip(0, 0, sc.ScrollSizeX, sc.ScrollSizeY);

	switch (Flip)
	{
	case 0:
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_dGfx.drawImage(Sour, (DestX)-(Sour.getWidth()-SizeX-SourX), DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
	return;

	case 2:
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_dGfx.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
	return;

	case 4:
	LCD_Gfx.clipRect(DestX, DestY, SizeY, SizeX);
	LCD_dGfx.drawImage(Sour, (DestX)-SourY, DestY-(Sour.getWidth()-SourX-SizeX), Graphics.LEFT|Graphics.TOP, 90);	// Rotar 90
	return;

	case 5:
	LCD_Gfx.clipRect(DestX, DestY, SizeY, SizeX);
	LCD_dGfx.drawImage(Sour, (DestX)-(Sour.getHeight()-SourY-SizeY), (DestY)-SourX, Graphics.LEFT|Graphics.TOP, 270);	// Rotar 270
	return;
	}
}

// ----------------------------------------------------------


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Load Image
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Image LoadImage(String FileName)
{
	System.gc();

	try	{
		Image Img = Image.createImage(FileName);
		System.gc();
		return Img;
	} catch (Exception e) {System.out.println("Error leyendo PNG: "+FileName);}

	return null;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------







// *******************
// -------------------
// Canvas - Engine
// ===================
// *******************

int CanvasSizeX=getWidth();
int CanvasSizeY=getHeight();

boolean CanvasFillPaint=false;
int CanvasFillRGB;
Image CanvasImg;

Scroll sc;

// -------------------
// Canvas INI
// ===================

public void CanvasINI()
{
	_CanvasImageSET("/Gfx/Manga.png", 0);

	sc = new Scroll();
}


// -------------------
// Canvas SET
// ===================

public void CanvasSET()
{
	ga.LoadFile(ga.TilDatTab0, "/Gfx/Tiles0.chk");
	ga.LoadFile(ga.TilDatTab1, "/Gfx/Tiles1.chk");
	ga.LoadFile(ga.TilDatTab2, "/Gfx/Tiles2.chk");


	ga.LoadFile(ProtaCor, "/Gfx/Prota.cor");

	ga.LoadFile(EnemyCor, "/Gfx/Enemy.cor");

	ga.LoadFile(ExtrasCor, "/Gfx/Extras.cor");

	ga.LoadFile(DispCor, "/Gfx/Disp.cor");

	ga.LoadFile(ItemsCor, "/Gfx/Items.cor");

	ga.LoadFile(SeguraCor, "/Gfx/Segura.cor");


	ga.LoadFile(FontCor, "/Gfx/Font.cor");

	FontImg=	LoadImage("/Gfx/Font.png");

	SoundINI();
}


// -------------------
// Canvas Img SET
// ===================

public void CanvasImageSET(String FileName, int RGB)
{
	CanvasFillRGB=RGB; CanvasFillPaint=true;
	CanvasImg = LoadImage(FileName);
}

	public void _CanvasImageSET(String FileName, int RGB)
	{
		CanvasFillRGB=RGB; CanvasFillPaint=true;
		CanvasImg = new Image();
		CanvasImg._createImage(FileName);
	}


// -------------------
// Canvas Img PUT
// ===================

public void CanvasImagePUT(String FileName, int RGB)
{
	CanvasFill(RGB);

	CanvasImg = LoadImage(FileName);

	if (CanvasImg!=null) {ImageSET(CanvasImg); CanvasImg=null; System.gc();}
}


// -------------------
// Canvas Fill
// ===================

public void CanvasFill(int RGB)
{
	LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
	LCD_Gfx.setColor(RGB);
	LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
}



// -------------------
// Canvas RUN
// ===================

public void CanvasRUN()
{
	if (CanvasFillPaint) { CanvasFillPaint=false; CanvasFill(CanvasFillRGB); }

	if (CanvasImg!=null) { ImageSET(CanvasImg); CanvasImg=null; System.gc(); }

	CanvasIMP();
}


// -------------------
// Canvas IMP
// ===================

public void CanvasIMP()
{
	if (ga.JugarPaint) { ga.JugarPaint=false; JugarIMP_Gfx(); }

	if (ga.MenuPaint) { ga.MenuPaint=false; MenuIMP_Gfx(); }

	if (MenuListUpdate) { MenuListUpdate=false; MenuListIMP_Gfx(); if (MenuType>=10) {ga.SantiPaint=true;}}

	if (ga.SantiPaint) { ga.SantiPaint=false; SantiIMP_Gfx(); }
}

// <=- <=- <=- <=- <=-








// ********************
// --------------------
// Jugar - Engine - Gfx
// ====================
// ********************

Image TilesImg;

Image ProtaImg;
Image EnemyImg;
Image ExtrasImg;
Image DispImg;

Image ItemsImg;
Image PanelImg;

Image FontImg;

Image SantiImg;
Image SeguraImg;

byte[] ProtaCor = new byte[288];
byte[] EnemyCor = new byte[192];
byte[] ExtrasCor = new byte[144];
byte[] DispCor = new byte[384];
byte[] ItemsCor = new byte[66];

byte[] FontCor = new byte[384];

byte[] SeguraCor = new byte[24];

int Stage;

boolean PanelPaint;
int Energy;
int Llaves;
int ProtType;
int Balas;

// -------------------
// Jugar INI Gfx
// ===================

public void JugarINI_Gfx()
{
	System.gc();

	ProtaImg=	LoadImage("/Gfx/Prota.png");
	EnemyImg=	LoadImage("/Gfx/Enemy.png");
	ExtrasImg=	LoadImage("/Gfx/Extras.png");
	DispImg=	LoadImage("/Gfx/Disp.png");
	ItemsImg=	LoadImage("/Gfx/Items.png");

	PanelImg=	LoadImage("/Gfx/Panel.png");

	SantiImg=	LoadImage("/Gfx/Santi.png");

	SeguraImg=	LoadImage("/Gfx/Segura.png");

	sc.ScrollINI(ga.GameSizeX, ga.GameSizeY-32);

	System.gc();
};


// -------------------
// Jugar SET Gfx
// ===================

public void JugarSET_Gfx()
{
	if (TilesImg==null || Stage != ga.Stage)
	{
	Stage = ga.Stage;
	TilesImg=null;
	TilesImg=	LoadImage("/Gfx/Tiles"+ga.Stage+".png");
	}

	sc.ScrollSET(ga.FaseMap, ga.FaseSizeX, ga.FaseSizeY,  TilesImg, 16);

	PanelPaint = true;
};


// -------------------
// Jugar RES Gfx
// ===================

public void JugarRES_Gfx()
{
	System.gc();
	TilesImg = null;
	sc.TilesImg = null;
	System.gc();
}

// -------------------
// Jugar END Gfx
// ===================

public void JugarEND_Gfx()
{
	System.gc();
	ProtaImg=null;
	EnemyImg=null;
	ExtrasImg=null;
	ItemsImg=null;
	PanelImg=null;
	SantiImg=null;
	sc.ScrollEND();
	System.gc();
};


// -------------------
// Jugar IMP Gfx
// ===================

public void JugarIMP_Gfx()
{

	sc.ScrollRUN_Centro_Max(ga.ProtX+12, ga.ProtY+12);

	sc.ScrollIMP(LCD_Gfx);



//	LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);
//	LCD_Gfx.setColor(-1);
//	LCD_Gfx.drawRect(ga.ProtX-sc.ScrollX,ga.ProtY-sc.ScrollY, 31,31);



// Imprimimos Enemy
// ----------------------
	AniSprIMP(ga.Enemys, ga.EnemyP, ga.EnemyC);


// Imprimimos AnimSprites
// ----------------------
	AniSprIMP(ga.AniSprs, ga.AniSprP, ga.AniSprC);





// Imprimimos Marcadores
// ----------------------

	if ( Energy != ga.JugarEnergy || Llaves != ga.JugarLlaves || ProtType != ga.ProtType || ga.JugarBalas[ga.ProtType] != Balas )
	{
		Energy = ga.JugarEnergy;
		Llaves = ga.JugarLlaves;
		ProtType = ga.ProtType;
		Balas = ga.JugarBalas[ga.ProtType];
		PanelPaint = true;
	}

	if (PanelPaint)
	{
	PanelPaint = false;

		int PanelX=0;
		int PanelY=CanvasSizeY-32;

		ImageSET(PanelImg, PanelX, PanelY);		// Panel de Fondo

		PrinterINI(0, PanelY, 128, 16);

		PrinterSET_Font(FontImg, 0,0,  7,7,  FontCor);

		PrinterSET_Spaces(-1,1);

		ImageSET(ItemsImg, PanelX+  4,PanelY+2, 0, ItemsCor);				// Vida
		PrinterIMP(Integer.toString(ga.JugarEnergy), PanelX+20,5, 0x00);

		ImageSET(ItemsImg, PanelX+  4,PanelY+18, 1, ItemsCor);				// Llaves
		PrinterIMP(Integer.toString(ga.JugarLlaves), PanelX+20,21, 0x00);

		ImageSET(ItemsImg, PanelX+142,PanelY+1, 2, ItemsCor);	// Arma
		ImageSET(ItemsImg, PanelX+142,PanelY+11, 3, ItemsCor);	// Arma
		ImageSET(ItemsImg, PanelX+142,PanelY+20, 4, ItemsCor);	// Arma

		PrinterIMP(Integer.toString(ga.JugarBalas[1]), PanelX+158,4, 0x00);
		PrinterIMP(Integer.toString(ga.JugarBalas[2]), PanelX+158,14, 0x00);
		PrinterIMP(Integer.toString(ga.JugarBalas[3]), PanelX+158,23, 0x00);

		if (ga.ProtType!=0)
		{
		ImageSET(ItemsImg, PanelX+128,PanelY+((ga.ProtType-1)*10), 5, ItemsCor);	// Arma
		}

		PrinterEND();
	}

}


// <<<<<<<<<<<<<<<<<

public void AniSprIMP(AnimSprite[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
	int Frame = s[i].FrameIni + s[i].FrameAct + s[i].FrameBase;
		switch (s[i].Bank)
		{
		case 0:
		PutSprite(ProtaImg,  s[i].CoorX, s[i].CoorY,  Frame, 48, 32, ProtaCor);
		break;	

		case 1:
		PutSprite(EnemyImg,  s[i].CoorX, s[i].CoorY,  Frame, 32, 32, EnemyCor);
		break;	

		case 2:
		PutSprite(ExtrasImg,  s[i].CoorX, s[i].CoorY,  Frame, ExtrasCor);
		break;	

		case 3:
		PutSprite(SeguraImg,  s[i].CoorX, s[i].CoorY,  Frame, 8, 32, SeguraCor);
		break;	

		case 7:
		PutSprite(DispImg,  s[i].CoorX, s[i].CoorY,  Frame/6, DispCor);
		break;	
		}
	}
}

// <=- <=- <=- <=- <=-



// -------------------
// Santi IMP Gfx
// ===================


public void SantiIMP_Gfx()
{
/*
	BocataIMP(CanvasSizeX - (CanvasSizeX/4), CanvasSizeY - (CanvasSizeY/2), 4);

	PrinterINI(SantiX-(SantiMarcX/2), SantiY-(SantiMarcY/2), SantiSizeX+SantiMarcX, SantiSizeY+SantiMarcY);

	PrinterSET_Font(FontImg, 0,0,  7,7,  FontCor);

	PrinterSET_Spaces(1,1);




	PrinterFill(-1);

	PrinterIMP_MenuList(MenuListStr, MenuListDat);

//	PrinterIMP(ga.SantiTexto[ga.SantiType]);



	PrinterEND();
*/

	ImageSET(SantiImg, CanvasSizeX-SantiImg.getWidth(), CanvasSizeY-SantiImg.getHeight() );

}

// --------------------------

int SantiX = (CanvasSizeX - 80)/2;
int SantiY = (CanvasSizeY - 40)/4;
int SantiSizeX = 80;
int SantiSizeY = 40;
static final int SantiMarcX = 12;
static final int SantiMarcY = 12;


public void BocataIMP(int SizeX, int SizeY, int AjusteY)
{
	SantiSizeX = SizeX;
	SantiSizeY = SizeY;

	SantiX = (CanvasSizeX - SantiSizeX)/2;
	SantiY = (CanvasSizeY - SantiSizeY)/AjusteY;

	LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);

	LCD_Gfx.setColor(0xFFFFFF);
	LCD_Gfx.fillRect((SantiX-SantiMarcX), SantiY, SantiSizeX+(SantiMarcX*2), SantiSizeY);
	LCD_Gfx.fillRect(SantiX, (SantiY-SantiMarcY), SantiSizeX, SantiSizeY+(SantiMarcY*2));

	LCD_Gfx.setColor(0xA3A3A3);
	LCD_Gfx.drawRect((SantiX-SantiMarcX), (SantiY), 0, SantiSizeY);
	LCD_Gfx.drawRect((SantiX+SantiSizeX+SantiMarcX-1), (SantiY), 0, SantiSizeY);
	LCD_Gfx.drawRect((SantiX), (SantiY-SantiMarcY), SantiSizeX, 0);
	LCD_Gfx.drawRect((SantiX), (SantiY+SantiSizeY+SantiMarcY-1), SantiSizeX, 0);


	ImageSET(ItemsImg, (SantiX-SantiMarcX), (SantiY-SantiMarcY), 8, ItemsCor);
	ImageSET(ItemsImg, (SantiX+SantiSizeX), (SantiY-SantiMarcY), 9, ItemsCor);
	ImageSET(ItemsImg, (SantiX-SantiMarcX), (SantiY+SantiSizeY), 6, ItemsCor);
	ImageSET(ItemsImg, (SantiX+SantiSizeX), (SantiY+SantiSizeY), 7, ItemsCor);

}



// <=- <=- <=- <=- <=-







// ********************
// --------------------
// Menu - Engine
// ====================
// ********************

int MenuType, MenuTypeOld;

// ---------------
// Menu SET
// ===============

public void MenuSET(int Type)
{
	if (Type<=0 && SoundOld!=0) {SoundSET(0,99);}

	MenuTypeOld = MenuType;
	MenuType = Type;

	switch (Type)
	{
	case 0:
		MenuListINI();
		MenuListADD(0,"JUGAR",0);
		MenuListADD(0,new String[] {"SONIDO OFF","SONIDO ON"},10,ga.GameSound);
//		MenuListADD(0,new String[] {"VIBRACION OFF","VIBRACION ON"},11,ga.GameVibra);
		MenuListADD(0,"CONTROLES",6);
		MenuListADD(0,"ACERCA DE...",7);
		MenuListADD(0,"SALIR",9);
		MenuListSET_Option();
	break;	

	case 1:
		MenuListINI();
		MenuListADD(0,"CONTINUAR",1);
		MenuListADD(0,new String[] {"SONIDO OFF","SONIDO ON"},10,ga.GameSound);
//		MenuListADD(0,new String[] {"VIBRACION OFF","VIBRACION ON"},11,ga.GameVibra);
		MenuListADD(0,"CONTROLES",6);
		MenuListADD(0,"TERMINAR",8);
		MenuListADD(0,"SALIR",9);
		MenuListSET_Option();
	break;	


	case 6:		// Controles
		MenuListINI();
		MenuListADD(2,"CONTROLES");
		MenuListADD(0," ");
		MenuListADD(0,"TECLA DE");
		MenuListADD(0,"CONTROL IZQUIERDO:");
		MenuListADD(0,"- IR AL MENU");
		MenuListADD(0," ");
		MenuListADD(0,"TECLA DE");
		MenuListADD(0,"CONTROL DERECHO:");
		MenuListADD(0,"- SELECCIONAR OPCION");
		MenuListADD(0," ");
		MenuListADD(0,"TECLAS 2 Y");
		MenuListADD(0,"DESP. ARRIBA:");
		MenuListADD(0,"- CAMINAR ARRIBA");
		MenuListADD(0," ");
		MenuListADD(0,"TECLAS 8 Y");
		MenuListADD(0,"DESP. ABAJO:");
		MenuListADD(0,"- CAMINAR ABAJO");
		MenuListADD(0," ");
		MenuListADD(0,"TECLAS 4 Y");
		MenuListADD(0,"DESP. IZQUIERDA:");
		MenuListADD(0,"- CAMINAR IZQUIERDA");
		MenuListADD(0," ");
		MenuListADD(0,"TECLAS 6 Y");
		MenuListADD(0,"DESP. DERECHA:");
		MenuListADD(0,"- CAMINAR DERECHA");
		MenuListADD(0," ");
		MenuListADD(0,"TECLAS 5 Y");
		MenuListADD(0,"CONTROL DERECHO:");
		MenuListADD(0,"- DISPARAR");
		MenuListADD(0," ");
		MenuListADD(0,"TECLA 0:");
		MenuListADD(0,"- CAMBIAR ARMA");
		MenuListSET_Scroll();
	break;	





	case 7:		// Creditos
		MenuListINI();
		MenuListADD(2,"UNA DE ZOMBIS");
		MenuListADD(0," ");

		MenuListADD(2, "ES UNA PELICULA DE LAMATA. UNA PRODUCCION DE AMIGUETES ENTERTAINMENT DISTRIBUIDA POR MANGA FILMS.", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListADD(0," ");
		MenuListADD(0," ");

		MenuListADD(2,"DESARROLLADO POR");
		MenuListADD(2,"PEBEGATSIX");
		MenuListADD(2,"MICROJOCS MOBILE");
		MenuListADD(2,"2003");
		MenuListADD(0," ");
		MenuListADD(2,"WWW.MICROJOCS.COM");
		MenuListADD(0," ");
		MenuListADD(0," ");

		MenuListADD(0,"PRODUCTORES:");
		MenuListADD(1,"GERARD FERNANDEZ");
		MenuListADD(1,"PERE TORRENTS");
		MenuListADD(0," ");
		MenuListADD(0,"GRAFICOS:");
		MenuListADD(1,"RAUL DURAN");
		MenuListADD(1,"JORDI PALOME");
		MenuListADD(0," ");
		MenuListADD(0,"SONIDO:");
		MenuListADD(1,"JORDI GUTIERREZ");
		MenuListADD(0," ");
		MenuListADD(0,"PROGRAMACION:");
		MenuListADD(1,"JUAN ANTONIO GOMEZ");
		MenuListSET_Scroll();
	break;	


	case 8:		// Game Over
		MenuListINI();
		MenuListADD(2,"FIN DEL JUEGO");
		MenuListSET_Text(3*1000);
	break;




	case 10:		// Santi
		MenuListINI();
		MenuListADD(2, "COLEGA, TIENES QUE ENCONTRAR LA CASA DE AIJON. A VER SI ESPABILAMOS QUE ESTO ESTA LLENO DE ZOMBIS. ZOMBIS CHUNGOS Y CON MALAS PULGAS.", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListSET_Scroll();
	break;	

	case 11:		// Santi
		MenuListINI();
		MenuListADD(2, "YA  TARDABAS TIO, NO VES EL PORTAL QUE TIENES A LA IZQUIERDA? PUES ENTRA HOMBRE, EMPIEZO A PENSAR QUE TE DA CORTE. BUENO ENCUENTRA EL LANZACOHETES QUE ESTA EN EL EDIFICIO. CHAVAL, ES QUE TE LO TENGO QUE DECIR TODO? CUANDO LO CONSIGAS PODRAS ENTRAR EN LA FABRICA,  CARGARTE A TODA LA BANDA DE ZOMBIS Y TAMBIEN A SU JEFE: EL MALVADO ENTRECOT.", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListSET_Scroll();
	break;	

	case 12:		// Santi
		MenuListINI();
		MenuListADD(2, "BUENO, CHAVAL ESTOS TIPOS TE VAN A DESPEDAZAR Y LUEGO SE TE COMERAN. ASI QUE PONTE LAS PILAS Y ENCUENTRA LA FABRICA. SI LAS COSAS SE PONEN CHUNGAS, ABRETE CAMINO CON EL LANZA-COHETES,... A VER SI NOS LO CURRAMOS UN POCO!", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListSET_Scroll();
	break;	

	case 13:		// Santi
		MenuListINI();
		MenuListADD(2, "TIO, YA TE HAS VUELTO A PERDER. PERO MIRA QUE ES FACIL, SOLO TIENES QUE ENCONTRAR LA FABRICA DONDE SE ESCONDE EL ENTRECOT Y CUANDO CONSIGAS ELIMINARLO, SE ACABO EL JUEGO!", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListSET_Scroll();
	break;	

	case 15:		// Santi
		MenuListINI();
		MenuListADD(2, "SI QUIERES ACABAR CONMIGO, TENDRAS QUE SEGUIR MIS INSTRUCCIONES. TE IRE LLAMANDO, BUSCAME EN LAS CABINAS...", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListSET_Scroll();
	break;	


	case 20:		// Final
		MenuListINI();
		MenuListADD(2, "MUY BIEN CAMPEON!!!", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListADD(2, "TE HAS CARGADO AL ENTRECOT.", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListADD(0," ");
		MenuListADD(2, "ESTO ES EL FINAL DEL JUEGO.", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListADD(0," ");
		MenuListADD(2, "... Y DE VERDAD ...", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListADD(0," ");
		MenuListADD(2, "YO LO QUE NO ENTIENDO ES COMO HAY GENTE QUE PIERDE EL TIEMPO CON ESTAS CHORRADAS...", FontCor, 1, SantiSizeX+SantiMarcX);
		MenuListSET_Scroll();
	break;	
	}

	ga.MenuPaint = true;
}

// -------------------
// Menu IMP Gfx
// ===================


public void MenuIMP_Gfx()
{
	if (MenuType!=-1)
	{
	BocataIMP(CanvasSizeX - (CanvasSizeX/2), CanvasSizeY - ((CanvasSizeY/3)*2), 2);
	MenuListUpdate = true;
	}
}

// <=- <=- <=- <=- <=-



// -------------------
// Menu IMP Gfx
// ===================

public void MenuListIMP_Gfx()
{
	PrinterINI(SantiX-(SantiMarcX/2), SantiY-(SantiMarcY/2), SantiSizeX+(SantiMarcX), SantiSizeY+SantiMarcY);

	PrinterSET_Font(FontImg, 0,0,  7,7,  FontCor);

	PrinterSET_Spaces(1,1);

	PrinterFill(-1);

	PrinterIMP_MenuList(MenuListStr, MenuListDat);

	PrinterEND();
}

// <=- <=- <=- <=- <=-






// ************************************
// ------------------------------------
// Printer - Engine - Rev.1 (2.7.2003)
// ====================================
// ************************************

// Modo: ???X
// ----------
// ??00 = Izquierda
// ??01 = Derecha
// ??1? = Centro Eje X
// 00?? = Arriba
// 01?? = Abajo
// 1??? = Centro Eje Y

// ========================

int PrinX;
int PrinY;
int PrinSizeX;
int PrinSizeY;

int PrinFontX;
int PrinFontY;
int PrinFontSizeX;
int PrinFontSizeY;
Image PrinFontImg;
byte[] PrintFontCor;

int PrinFontSpcX=1;
int PrinFontSpcY=1;
int PrinFontCurX;
int PrinFontCurY;

// -------------------
// Printer INI
// ===================

public void PrinterINI(int X, int Y, int SizeX, int SizeY)
{
	PrinX=X;
	PrinY=Y;
	PrinSizeX=SizeX;
	PrinSizeY=SizeY;

	PrinFontCurX=0;
	PrinFontCurY=0;
}

// -------------------
// Printer END
// ===================

public void PrinterEND()
{
	PrinFontImg=null;
	PrintFontCor=null;
}

// -------------------
// Printer SET
// ===================

public void PrinterSET_Font(Image Img, int X, int Y, int SizeX, int SizeY, byte[] Cor)
{
	PrinFontImg=Img;
	PrinFontX=X;
	PrinFontY=Y;
	PrinFontSizeX=SizeX;
	PrinFontSizeY=SizeY;
	PrintFontCor=Cor;

	PrinFontCurX=0;
	PrinFontCurY=0;

	PrinFontSpcX=1;
	PrinFontSpcY=1;
}

public void PrinterSET_Spaces(int X, int Y)
{
	PrinFontSpcX=X;
	PrinFontSpcY=Y;
}

// -------------------
// Printer Size Calc
// ===================

public int PrinterSizeCalc(byte[] Texto)
{
	int Size=0;	for (int i=0 ; i<Texto.length ; i++) {int c=Texto[i]-0x20; Size+=PrintFontCor[((c<=0?2:c)*6)+2]+PrinFontSpcX;}
	return Size-PrinFontSpcX;
}


public int PrinterSizeX_Max(String[][] Str)
{
	int MaxSize=0;

	for (int i=0 ; i<Str.length ; i++)
	{
	int Size = PrinterSizeCalc( Str[i][0].getBytes() );
	if (MaxSize < Size) {MaxSize = Size;}
	}

	return MaxSize;
}


// -------------------
// Printer Fill
// ===================

public void PrinterFill(int RGB)
{
	LCD_Gfx.setClip (PrinX, PrinY,  PrinSizeX+1, PrinSizeY);
	LCD_Gfx.setColor(RGB & 0xFFFFFF);
	LCD_Gfx.fillRect(PrinX, PrinY,  PrinSizeX+1, PrinSizeY);
}

public void PrinterFill(Image Img)
{
	ImageSET(Img,   PrinX, PrinY,  PrinSizeX, PrinSizeY,   PrinX, PrinY);
}

// -------------------
// Printer IMP
// ===================

public void PrinterIMP(String TextoStr, int Mode)
{
	PrinFontCurX=0;
	PrinterIMP(TextoStr, PrinFontCurX, PrinFontCurY, Mode);
}

public void PrinterIMP(String TextoStr, int DestX, int DestY, int Mode)
{
	byte[] Texto = TextoStr.getBytes();


	if ((Mode & 0x002)!=0) {DestX+=( PrinSizeX-PrinterSizeCalc(Texto) )/2;}
	else
	if ((Mode & 0x001)!=0) {DestX+=  PrinSizeX-PrinterSizeCalc(Texto) ;}

	if ((Mode & 0x008)!=0) {DestY+=( PrinSizeY-PrinFontSizeY )/2;}
	else
	if ((Mode & 0x004)!=0) {DestY+=  PrinSizeY-PrinFontSizeY ;}


	for (int i=0 ; i<Texto.length ; i++)
	{
	int c=Texto[i]-0x20; if (c<=0) {DestX+=PrintFontCor[((2*6)+2)]+PrinFontSpcX; continue;}

	int Dir = c*6;

	int x=PrintFontCor[Dir+4]+128;
	int y=PrintFontCor[Dir+5]+128;

	ImageSET(PrinFontImg,  PrinFontX+x, PrinFontY+y,   PrintFontCor[Dir+2], PrintFontCor[Dir+3],   PrinX+DestX+PrintFontCor[Dir+0], PrinY+DestY+PrintFontCor[Dir+1]);

	DestX+=PrintFontCor[Dir+2]+PrinFontSpcX;
	}

	PrinFontCurX=DestX;
	PrinFontCurY+=PrinFontSizeY+PrinFontSpcY;	// Enter!!!
}

// --------------------
// Printer IMP MenuList
// ====================

public void PrinterIMP_MenuList(String[][] Str, int[][] Dat)
{
	PrinFontCurY = (PrinSizeY-((PrinFontSizeY+PrinFontSpcY) * Dat.length))/2 ;

	int X = -1000;
	int Y = 0;

	int IniX = 0; if (MenuListMode==3) {IniX = PrinFontSizeX;}

	if (MenuListMode==2)
	{
		PrinFontCurY = PrinSizeY-MenuListScrY;
	}


	for (int i=0 ; i<Str.length ; i++)
	{
	if (MenuListMode==3 && i == MenuListPos ) {X = PrinFontCurX; Y = PrinFontCurY;}

		if (MenuListMode!=2 || PrinFontCurY >= 0 && PrinFontCurY+PrinFontSizeY <= PrinSizeY)
		{
		PrinterIMP(Str[i][Dat[i][2]], IniX, PrinFontCurY, Dat[i][0]);
		} else {
		PrinFontCurY+=PrinFontSizeY+PrinFontSpcY;	// Enter!!!
		}
	}

	if (X!=-1000) {PrinterIMP(">", 0,Y, 0x0);}
}



public void PrinterIMP(String Str)
{

	MenuListINI();
	MenuListADD(0, Str, PrintFontCor, PrinFontSpcX, SantiSizeX+SantiMarcX);
	PrinterIMP(MenuListStr);
	MenuListEND();
}


public void PrinterIMP(String[][] Str)
{
	for (int i=0 ; i<Str.length ; i++)
	{
	PrinterIMP(Str[i][0], 0x00);
	}
}

// <=- <=- <=- <=- <=-








// *******************
// -------------------
// MenuList - Engine - Rev.0 (20.6.2003)
// ===================
// *******************

boolean MenuList_ON=false;		// Estado del MenuList Engine
boolean MenuListUpdate=false;

int MenuListMode;

int MenuListScrY;
int MenuListWait;

int MenuListPos;
int MenuListCMD;

String	MenuListStr[][];
int		MenuListDat[][];

long MenuListTimeIni;
int MenuListTimeWait;

// -------------------
// MenuList INI
// ===================

public void MenuListINI()
{
	MenuList_ON=false;
	MenuListUpdate=false;

	MenuListStr=null;
	MenuListDat=null;

	MenuListPos=0;
}

// -------------------
// MenuList INI
// ===================

public void MenuListEND()
{
	MenuListINI();
}

// -------------------
// MenuList ADD
// ===================

public void MenuListADD(int Dato, String Texto)
{
	MenuListADD(Dato, new String[] {Texto}, 0, 0);
}

public void MenuListADD(int Dato, String Texto, int Dat1)
{
	MenuListADD(Dato, new String[] {Texto}, Dat1, 0);
}

public void MenuListADD(int Dat0, String[] Texto, int Dat1, int Dat2)
{
	int Lines=(MenuListDat!=null)?MenuListDat.length:0;

	String Str[][] = new String[Lines+1][];
	int Dat[][] = new int[Lines+1][];

	int i=0;

	for (; i<Lines; i++)
	{
	Dat[i] = MenuListDat[i];
	Str[i] = MenuListStr[i];
	}

	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2};

	MenuListStr=Str;
	MenuListDat=Dat;
}





public void MenuListADD(int Dato, String Texto, byte[] FontSizeX, int SpcX, int MaxSizeX)
{
	int Pos=0, PosIni=0, PosOld=0, Size=0;

	byte[] Tex = Texto.getBytes();


	while ( PosOld < Tex.length )
	{
	Size=0;

	Pos=PosIni;

//	while ( Pos < Tex.length && Tex[Pos] < 0x30 ) {Pos++;}

	while ( Size < MaxSizeX )
	{
	if ( Pos == Tex.length ) {PosOld=Pos; break;}

	int Dat=Tex[Pos++]-0x20;
	if ( Dat <= 0) {PosOld=Pos-1; Dat=2;}
	Size+=FontSizeX[(Dat*6)+2]+SpcX;
	}

	if (PosOld-PosIni < 1) { while ( Pos < Tex.length && Tex[Pos] >= 0x20 ) {Pos++;} PosOld=Pos; }

	MenuListADD(Dato, new String[] {Texto.substring(PosIni,PosOld)}, 0, 0);

	PosIni=PosOld+1;

	}

}



// -------------------
// MenuList SET
// ===================

public void MenuListSET_Text()
{
	MenuListSET_Text(0);
}

public void MenuListSET_Text(int Time)
{
	MenuList_ON=true;
	MenuListUpdate=true;

	MenuListTimeWait = Time;
	MenuListTimeIni = System.currentTimeMillis();

	MenuListMode=1;
}

public void MenuListSET_Scroll()
{
	MenuList_ON=true;
	MenuListUpdate=true;
	MenuListMode=2;
	MenuListScrY=0;
}

public void MenuListSET_Option()
{
	MenuListSET_Option(0);
}

public void MenuListSET_Option(int Pos)
{
	MenuListPos=Pos;
	MenuList_ON=true;
	MenuListUpdate=true;
	MenuListMode=3;
}

// -------------------
// MenuList RUN
// ===================

public boolean MenuListRUN(int MovY, int KeyY, boolean Fire)
{
	if (MenuListMode==1)
	{
	if ( MenuListTimeWait>0 && (System.currentTimeMillis() - MenuListTimeIni) >= MenuListTimeWait ) {MenuListCMD = -4; return true;}
	}

	if (MenuListMode==2)
	{
		if (Fire || MenuListScrY > PrinSizeY+(MenuListDat.length*(PrinFontSizeY+PrinFontSpcY)) ) {MenuListCMD = -3; return true;}

		if (KeyY>=0 && --MenuListWait < 0)
		{
		MenuListWait = (PrinFontSizeY+PrinFontSpcY) * 2;
		MenuListScrY+= (PrinFontSizeY+PrinFontSpcY) * (KeyY+1);
		MenuListUpdate=true;
		}
	}

	if (MenuListMode==3)
	{
		if (MovY ==-1 && MenuListPos > 0) {MenuListPos--; MenuListUpdate=true;}
		else
		if (MovY == 1 && MenuListPos < MenuListDat.length-1) {MenuListPos++; MenuListUpdate=true;}

		if (Fire)
		{
		if (++MenuListDat[MenuListPos][2] == MenuListStr[MenuListPos].length) {MenuListDat[MenuListPos][2]=0;}
		MenuListCMD=MenuListDat[MenuListPos][1];
		MenuListUpdate=true;
		return true;
		}
	}

	return false;
}


// -------------------
// MenuList OPT
// ===================

public int MenuListOPT()
{
	return(MenuListDat[MenuListPos][2]);
}


// -------------------
// MenuList EXE
// ===================

public void MenuListEXE()
{
	switch (MenuListCMD)
	{
	case -4:
		MenuListEND();
		MenuSET(0);
	break;

	case -3:
		if (MenuType<10)
		{
		MenuListEND();
		MenuSET(MenuTypeOld);
		}
		else if (MenuType==20)
		{
		ga.MenuRES();
		ga.FaseExit=3;
		} else {
		PanelPaint=true;
		ga.MenuRES();
		}
	break;

	case 0:	// Jugar de 0
	case 1:	// Continuar
		ga.MenuExit=1;
	break;

	case 5:	// Siguiente Nivel
//		FaseExit=1;
//		GameStatus=102;
	break;


	case 6:	// Controles...
		MenuListEND();
		MenuSET(6);
	break;

	case 7:	// About...
		MenuListEND();
		MenuSET(7);
	break;


	case 8:	// Restart
		ga.MenuExit=1;
		ga.FaseExit=3;
	break;

	case 9:	// Exit Game
		ga.GameExit=1;
	break;	

	case 10:
		ga.GameSound = MenuListOPT();
		if (MenuType==0)
		{
		if (ga.GameSound!=0) {SoundSET(0,99);} else {SoundRES();}
		}
	break;

	case 11:
		ga.GameVibra = MenuListOPT();
	break;
	}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
// ===================
// *******************

Player[] Sonidos;

int SoundOld = -1;

// -------------------
// Sound INI
// ===================

public void SoundINI()
{
	Sonidos = new Player[7];

	Sonidos[0] = SoundCargar("/zombie_menu.mid");
	Sonidos[1] = SoundCargar("/astro_explosion.mid");
	Sonidos[2] = SoundCargar("/zombie_item.mid");
	Sonidos[3] = SoundCargar("/zombie_muerte_enemigo.mid");
	Sonidos[4] = SoundCargar("/zombie_muerte_protagonista.mid");
	Sonidos[5] = SoundCargar("/zombie_puerta.mid");
	Sonidos[6] = SoundCargar("/zombie_seleccion_arma.mid");
}

public Player SoundCargar(String Nombre)
{
	Player p = null;

	try
	{
		InputStream is = getClass().getResourceAsStream(Nombre);
		p = Manager.createPlayer( Nombre , "audio/midi");
		p.realize();
		p.prefetch();
	}
	catch(Exception exception) {exception.printStackTrace();}

	return p;
}

// -------------------
// Sound SET
// ===================

public void SoundSET(int Ary, int Loop)
{
	if (Loop<1) {Loop=-1;}

	SoundRES();

	if (ga.GameSound!=0)
	{
		try
		{
			VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
			Sonidos[Ary].setLoopCount(Loop);
			Sonidos[Ary].start();
		}
		catch(Exception exception) {exception.printStackTrace();}
	}

	SoundOld=Ary;
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
			Sonidos[SoundOld].stop();
		}
		catch (Exception e) {}

		SoundOld = -1;
	}
}

// -------------------
// Sound RUN
// ===================

public void SoundRUN()
{
}

// -------------------
// Vibra SET
// ===================

public void VibraSET(int Time)
{
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