


// -----------------------------------------------
// The Mute v1.0 Rev.1 ( 6.6.2003) - Final
// The Mute v1.0 Rev.0 (22.5.2003) Canvas
// ===============================================
// Motorola V300
// ------------------------------------


package com.mygdx.mongojocs.themute;



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - GameCanvas 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

public class GameCanvas extends Canvas implements CommandListener
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

long TimeNow, TimeOld;
int TimeDat, TimeCnt;

public void paint (Graphics g)
{       
	LCD_Gfx=g;

	CanvasRUN();

/*
	if (--TimeCnt < 1) {TimeCnt=50; TimeNow=System.currentTimeMillis(); TimeDat=(int)((TimeNow-TimeOld)/10); TimeOld=TimeNow;}
	g.setClip( 0,0, CanvasSizeX, CanvasSizeY ); g.setColor(-1); g.fillRect(0,0,  32,9 ); g.setColor(0);
	g.drawString(Integer.toString(AniSprGrupoC), 0,0, Graphics.LEFT|Graphics.TOP);
//	g.drawString(Integer.toString(TimeDat),  0,0, Graphics.LEFT|Graphics.TOP);
*/
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int KeyType=0, KeyData;

public void keyPressed(int keycode)
{
	KeyData=keycode;
	KeyType=1;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se REPITE una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyRepeated(int keycode)
{
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void keyReleased(int keycode)
{
	KeyData=keycode;
	KeyType=2;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void ImageSET(Image Sour, int Coor[], int Frame, int DestX, int DestY)
{
	Frame<<=2;

	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	int SizeY=Coor[Frame++];

	LCD_Gfx.setClip(0, 0, ga.GameSizeX, ga.GameSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX+scroll.Desp, SizeY+scroll.Desp);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------


public void FillSET(int RGB, int Coor[], int Frame, int DestX, int DestY)
{
	Frame<<=2;

	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	int SizeY=Coor[Frame++];

	LCD_Gfx.setClip(0, 0, ga.GameSizeX, ga.GameSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.setColor(RGB);
	LCD_Gfx.fillRect(DestX, DestY, SizeX, SizeY);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	LCD_Gfx.setClip(0, 0, CanvasSizeX, CanvasSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX+scroll.Desp, SizeY+scroll.Desp);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	LCD_Gfx.setClip(TopX, TopY, FinX+scroll.Desp, FinY+scroll.Desp);
	LCD_Gfx.clipRect(DestX+TopX, DestY+TopY, SizeX+scroll.Desp, SizeY+scroll.Desp);
	LCD_Gfx.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void PutSprite(Image Img, int Width, int X, int Y,  int SizeX, int SizeY,  int Frame)
{
//	LCD_Gfx.setClip(X,Y, SizeX,SizeY);
//	LCD_Gfx.drawImage (Img, X-((Frame%Width)*SizeX), Y-((Frame/Width)*SizeY), Graphics.TOP|Graphics.LEFT);	// Pintamos en el LCD
	scroll.ImageSET(Img, (Frame%Width)*SizeX, (Frame/Width)*SizeY,  SizeX,SizeY,  LCD_Gfx,  X,Y);
}

// ----------------------------------------------------------

public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Bits)
{
	Frame*=(2*6);

	for (int i=0 ; i<2 ; i++)
	{
	int DestX=Coor[Frame++]+X;
	int DestY=Coor[Frame++]+Y;
	int SizeX=Coor[Frame++];
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	scroll.ImageSET2(Img,  SourX+128,SourY+128,  SizeX,SizeY,  LCD_Gfx,  DestX,DestY);

	OcultaTecho(DestX, DestY, SizeX, SizeY, Bits);
	}
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
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	scroll.ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  LCD_Gfx,  X+DestX,Y+DestY);
}

// ----------------------------------------------------------

public void PutSpriteTrozos(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
{
	Frame*=(Trozos*6);

	for (int i=0 ; i<Trozos ; i++)
	{
	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	scroll.ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  LCD_Gfx,  X+DestX,Y+DestY);
	}
}

// ----------------------------------------------------------


public void PutImage(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=(2*6);

	for (int i=0 ; i<2 ; i++)
	{
	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,    X+DestX,Y+DestY);
	}
}

// ----------------------------------------------------------

public void AniSprIMP(AnimSprite[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
	switch (s[i].Bank)
	{
	case 0:
	PutSpriteTrozos(SpritesImg,  s[i].CoorX, s[i].CoorY,  s[i].FrameIni + s[i].FrameAct, SpritesCor, 2);
	break;	

	case 1:
	PutSpriteTrozos(ExtrasImg,  s[i].CoorX, s[i].CoorY,  s[i].FrameIni + s[i].FrameAct, ExtrasCor, 4);
	break;	
	}

	}
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

Scroll scroll;

int CanvasSizeX=getWidth();
int CanvasSizeY=getHeight();
//int CanvasSizeX=176;
//int CanvasSizeY=208;

int CanvasFillRGB;
int CanvasFillPaint;
Image CanvasImg;
Image DiamanImg;
Image IconMenuImg;

// -------------------
// Canvas INI
// ===================

public void CanvasINI()
{
	CanvasImageSET("/Gfx/Loading.png", 0);
	DiamanImg=CanvasImg;

	if (CanvasSizeY >= ga.GameMaxY+10) {IconMenuImg = LoadImage("/Gfx/IconMenu.png");}

	scroll = new Scroll();
}


// -------------------
// Canvas SET
// ===================

public void CanvasSET()
{
	ga.LoadFile(ga.TilDatTab, "/Data/Tiles.bit");

	ga.LoadFile(SpritesCor, "/Gfx/Sprites.cor");

	ga.LoadFile(ExtrasCor, "/Gfx/Extras.cor");

	ListenerSET(this);
}


// -------------------
// Load Image
// ===================

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

	public Image LoadImage_fromMainThread(String FileName)
	{
		System.gc();

		try	{
			Image Img = new Image();
			Img._createImage(FileName);
			System.gc();
			return Img;
		} catch (Exception e) {System.out.println("Error leyendo PNG: "+FileName);}

		return null;
	}



// -------------------
// Canvas Img SET
// ===================

public void CanvasImageSET(String FileName, int RGB)
{
	CanvasFillRGB=RGB; CanvasFillPaint=1;
	CanvasImg = LoadImage_fromMainThread(FileName);
}

// -------------------
// Canvas Img PUT
// ===================

public void CanvasImagePUT(String FileName, int RGB)
{
	CanvasFill(RGB);

	CanvasImg = LoadImage(FileName);

	if (CanvasImg!=null)
	{
	LCD_Gfx.setClip(0, 0, CanvasSizeX, CanvasSizeY);
	ImageSET (CanvasImg, 0,0, CanvasImg.getWidth(), CanvasImg.getHeight(), (CanvasSizeX-CanvasImg.getWidth())/2, (CanvasSizeY-CanvasImg.getHeight())/2);
	CanvasImg=null;
	System.gc();
	}
}


// -------------------
// Canvas Fill
// ===================

public void CanvasFill(int RGB)
{
	CanvasFill ( RGB,  0,0, CanvasSizeX,CanvasSizeY );
}

public void CanvasFill(int RGB, int X, int Y, int SizeX, int SizeY)
{
	LCD_Gfx.setClip (X,Y, SizeX,SizeY );
	LCD_Gfx.setColor(RGB);
	LCD_Gfx.fillRect(X,Y, SizeX,SizeY );
}



// -------------------
// Canvas RUN
// ===================

int IconMenuX;

public void CanvasRUN()
{
	if (CanvasFillPaint!=0) { CanvasFill(CanvasFillRGB); if (CanvasFillPaint > 0) {CanvasFillPaint--;}}

	if (CanvasImg!=null) {
	ImageSET (CanvasImg, 0,0, CanvasImg.getWidth(),CanvasImg.getHeight(), (CanvasSizeX-CanvasImg.getWidth())/2, (CanvasSizeY-CanvasImg.getHeight())/2);
	CanvasImg=null; System.gc(); }

	//TODO LCD_Gfx.translate(ga.GameX, ga.GameY);

	CanvasIMP();

//	LCD_Gfx.translate(-ga.GameX, -ga.GameY);

	if (IconMenuImg!=null)
	{
	LCD_Gfx.setClip(-35+IconMenuX,CanvasSizeY-10-ga.GameY, 35,10);
	LCD_Gfx.drawImage(IconMenuImg,-35+IconMenuX,CanvasSizeY-10-ga.GameY,20);
	IconMenuX++;if(IconMenuX>=35){IconMenuX=35;}
	}

}


// -------------------
// Canvas IMP
// ===================

public void CanvasIMP()
{
	if (IntroPaint!=0) {IntroIMP_Gfx();}

	if (ga.MonitorPaint!=0) { ga.MonitorPaint=0; MonitorIMP_Gfx(); }

	if (ga.JugarPaint!=0) { JugarIMP_Gfx(); if (ga.JugarPaint > 0) {ga.JugarPaint--;}}

	if (ga.Marco_ON!=0) {ga.marco.MarcoIMP(LCD_Gfx);}


	if (ga.PanelPaint!=0)
	{
	ga.PanelPaint=0;

		switch (ga.PanelType)
		{
		case 0:
			StringIMP(Texto.Levels[ga.JugarLevel], 0, 0, -1, 0x11F);
/*
			if (ga.JugarLevel!=0)
			{
			StringIMP(Texto.Lives+ga.JugarVidas,   0,-4, -1, 0x007);
			}
*/
		break;	

		case 1:
			StringIMP(Texto.GameOver,   0,0, -1, 0x01F);
		break;	

		case 2:
			ImageSET (DiamanImg, 0,0, DiamanImg.getWidth(),DiamanImg.getHeight(), (CanvasSizeX-DiamanImg.getWidth())/2, ((CanvasSizeY-DiamanImg.getHeight())/2)-8 );
			StringIMP(Texto.Congra,   0,-2, -1, 0x107);
		break;	

		}
	}

}

// <=- <=- <=- <=- <=-


// *********************
// ---------------------
// String - Engine - Gfx
// =====================*
// ********************

// Modo: XX?
// ---------
// ?0? = PLAIN
// ?1? = BOLD

// 0?? = SMALL
// 1?? = MEDIUM
// 2?? = BIGGER

// Modo: ???X
// ----------
// ??00 = Izquierda
// ??01 = Derecha
// ??1? = Centro Eje X
// 00?? = Arriba
// 01?? = Abajo
// 1??? = Centro Eje Y

// -------------------
// String IMP
// ===================

public void StringIMP(String Str, int X, int Y, int RGB, int Mode)
{
	LCD_Gfx.setFont(Font.getFont(Font.FACE_PROPORTIONAL, ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD), ((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) )) );
	Font f = LCD_Gfx.getFont();

	if ((Mode & 0x002)!=0) {X+=( ga.GameSizeX-f._stringWidth(Str) )/2;}
	else
	if ((Mode & 0x001)!=0) {X+=  ga.GameSizeX-f._stringWidth(Str) ;}

	if ((Mode & 0x008)!=0) {Y+=( ga.GameSizeY-f.getHeight() )/2;}
	else
	if ((Mode & 0x004)!=0) {Y+=  ga.GameSizeY-f.getHeight() ;}

	LCD_Gfx.setClip(0,0, ga.GameSizeX, ga.GameSizeY);
	LCD_Gfx.setColor(RGB);
	LCD_Gfx.drawString(Str,  X, Y,  20);
}

// <=- <=- <=- <=- <=-



// ********************
// --------------------
// Intro - Engine - Gfx
// ====================
// ********************

Image IntroImg;
int IntroFile;
int IntroFin;
int IntroTime;
int IntroX=0;
int IntroPaint;

// -------------------
// Intro INI Gfx
// ===================

public void IntroINI_Gfx(int Type)
{
	IntroX=0;
	IntroPaint=0;

	if (Type==0)
	{
	IntroFile=11;
	IntroFin= 18;
	} else {
	IntroFile=22;
	IntroFin= 23;
	}
	IntroTime=0;
}


// -------------------
// Intro RUN Gfx
// ===================

public boolean IntroRUN_Gfx()
{
	if (--IntroTime < 0)
	{
	if (IntroFile>IntroFin) {return true;}

	IntroImg = LoadImage("/Gfx/"+(IntroFile++)+".png");
	CanvasFillRGB=0; CanvasFillPaint=1;
	CanvasImg=IntroImg;
	IntroTime=60;
	}

	IntroPaint=1;

	return false;
}


// -------------------
// Intro IMP Gfx
// ===================

public void IntroIMP_Gfx()
{
	if (IntroFile==15)
	{
		LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);
		LCD_Gfx.setColor(0);

		int x=IntroX--;
		while (x < CanvasSizeX)
		{
		LCD_Gfx.fillRect(x,0,  24,CanvasSizeY);
		x+=24+16;
		}

	CanvasImg=IntroImg;
	}
}

// <=- <=- <=- <=- <=-





// ********************
// --------------------
// Menu - Engine - Gfx
// ====================
// ********************

Image MenuImg;

// -------------------
// Menu INI Gfx
// ===================

public void MenuINI_Gfx()
{
	MenuImg = LoadImage("/Gfx/Menu.png");
}

// -------------------
// Menu END Gfx
// ===================

public void MenuEND_Gfx()
{
	System.gc();
	MenuImg=null;
	System.gc();
}

// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Jugar - Engine - Gfx
// ====================
// ********************

boolean FlechaFlag;

Image SpritesImg;
Image ExtrasImg;
Image TextosImg;
Image TilesImg;
Image VisionImg;

byte[] SpritesCor = new byte[1536];
byte[] ExtrasCor = new byte[192];

int[] TextosObj = new int[] {
	 0, 0,   38, 7,		// (0) ON
	 0, 7,   38, 7,		// (1) OFF
	40, 0,   54, 7,		// (2) ALARMA
	40, 7,   54, 7,		// (3) CERRADA
	 0,14,   86,54,		// (4) LLAVE
	86,14,    8, 7,		// (5) A
	86,21,    8, 7,		// (6) B
	86,28,    8, 7,		// (7) C
	86,36,    8, 8,		// (8) Marco Superior Izquierda
	86,44,    8, 8,		// (9) Marco Superior Derecho
	86,52,    8, 8,		// (A) Marco Inferior Izquierda
	86,60,    8, 8,		// (B) Marco Inferior Derecho
};

// -------------------
// Jugar INI Gfx
// ===================

public void JugarINI_Gfx()
{
	SpritesImg=	LoadImage("/Gfx/Sprites.png");
	TilesImg=	LoadImage("/Gfx/Tiles.png");
	ExtrasImg=	LoadImage("/Gfx/Extras.png");
	TextosImg=	LoadImage("/Gfx/Textos.png");

	scroll.ScrollINI(ga.GameSizeX, ga.GameSizeY );

	System.gc();
}


// -------------------
// Jugar SET Gfx
// ===================

public void JugarSET_Gfx()
{
	if (ga.JugarLevel==0)
	{
	VisionImg=	LoadImage("/Gfx/Vision.png");
	} else {
	VisionImg=	LoadImage("/Gfx/Vision2.png");
	}

	scroll.ScrollSET(ga.FaseMap, ga.FaseSizeX, ga.FaseSizeY,  TilesImg, 26);
}


// -------------------
// Jugar END Gfx
// ===================

public void JugarEND_Gfx()
{
	System.gc();
	SpritesImg=null;
	ExtrasImg=null;
	TextosImg=null;
	TilesImg=null;
	VisionImg=null;
	scroll.ScrollEND();
	System.gc();
}


// -------------------
// Jugar IMP Gfx
// ===================

public void JugarIMP_Gfx()
{

// Actualizamos Scroll
// -------------------
	int BaseX=0, BaseY=0;

	if (ga.ProtSideY==0)
	{
	BaseX=ga.ProtX+12+( ga.ProtSideX!=1 ? -((ga.GameSizeX/5)) : ((ga.GameSizeX/5)) );
	BaseY=ga.ProtY+12;
	} else {
	BaseX=ga.ProtX+12;
	BaseY=ga.ProtY+12+( ga.ProtSideY!=1 ? -((ga.GameSizeY/5)) : ((ga.GameSizeY/5)) );
	}


	if (ga.ProtMode==4)
	{
	scroll.ScrollRUN(ga.ProtX+12, ga.ProtY+12);
	} else {
	scroll.ScrollRUN(BaseX, BaseY);
	}


	scroll.ScrollIMP(LCD_Gfx);


// Imprimimos Puertas
// -------------------
	AniSprIMP(ga.Puertas, ga.PuertaP, ga.PuertaC);


// Imprimimos Protagonista + Enemigos
// -----------------------------------
	if (ga.FaseType[ga.JugarLevel]==0)
	{
	AniSprIMP(ga.AniSprs, ga.AniSprP, ga.AniSprC);
	} else {

		AniSprGrupoSET( ga.AniSprC + ga.EnemyC );

		AniSprGrupoADD( ga.AniSprs, ga.AniSprP, ga.AniSprC);
		AniSprGrupoADD( ga.Enemys, ga.EnemyP, ga.EnemyC);

// -------------------
// Anim Sprite Ordenar
// ===================
		int Conta=AniSprGrupoC-1;

		for (int t=0 ; t<AniSprGrupoC ; t++)
		{
			for (int i=0 ; i<Conta ; i++)
			{
				if (AniSprGrupos[i].FrameIni>=32 && AniSprGrupos[i].FrameIni<116)
				{
					if (AniSprGrupos[i+1].FrameIni<32 || AniSprGrupos[i+1].FrameIni>=116)
					{
					AnimSprite s=AniSprGrupos[i+1];
					AniSprGrupos[i+1]=AniSprGrupos[i];
					AniSprGrupos[i]=s;
					}
					else if (AniSprGrupos[i].CoorY > AniSprGrupos[i+1].CoorY)
					{
					AnimSprite s=AniSprGrupos[i+1];
					AniSprGrupos[i+1]=AniSprGrupos[i];
					AniSprGrupos[i]=s;
					}
				}
			}
		Conta--;
		}
// =========================

// -------------------
// Anim Sprite Imprimir
// ===================
		for (int i=0 ; i<AniSprGrupoC ; i++)
		{
			if (AniSprGrupos[i].Bank==0)
			{
			int Bits=0x070; if (AniSprGrupos[i].FrameIni>=32 && AniSprGrupos[i].FrameIni<116) {Bits=0x010;}

			PutSprite(SpritesImg,  AniSprGrupos[i].CoorX, AniSprGrupos[i].CoorY,  AniSprGrupos[i].FrameIni + AniSprGrupos[i].FrameAct, SpritesCor, Bits);
			}
		}

	}
// =========================


// Imprimimos Lasers
// -------------------
	AniSprIMP(ga.Lasers, ga.LaserP, ga.LaserC);
// =========================


//	Imprimimos Vision
// -------------------------
	int VisionSizeX=VisionImg.getWidth();
	int VisionSizeY=VisionImg.getHeight();
	int VisionX=ga.ProtX+12-(VisionSizeX/2)-scroll.ScrollX;
	int VisionY=ga.ProtY+12-(VisionSizeY/2)-scroll.ScrollY;

	ImageSET(VisionImg,  0,0,  VisionSizeX,VisionSizeY,  VisionX, VisionY);

	CanvasFill(0, 0,0, CanvasSizeX, VisionY);
	CanvasFill(0, 0,VisionY+VisionSizeY, CanvasSizeX, CanvasSizeY-(VisionY+VisionSizeY) );

	CanvasFill(0, 0,VisionY, VisionX, VisionSizeY);
	CanvasFill(0, VisionX+VisionSizeX,VisionY, CanvasSizeX-(VisionX+VisionSizeX), VisionSizeY );
// =========================


//	Imprimimos Flechas
// -------------------------
/*
	if (FlechaFlag)
	{
		for (int i=0 ; i<ga.FlechaC ; i++)
		{
		PutImage(SpritesImg, ga.Flechas[i][0], ga.Flechas[i][1], ga.Flechas[i][2], SpritesCor);
		}
	FlechaFlag=false;
	} else {
	FlechaFlag=true;
	}
*/
// =========================


// ------------------------------
// Panel
// ------------------------------
	if (ga.JugarPanel_ON != -1 && ga.JugarPanelTime > ga.JugarPanelMedi)
	{
	ImageSET(TextosImg,  TextosObj,  ga.JugarPanel_ON,   0,ga.GameSizeY-7);
	}
// ================================



};





// ------------------------------
// Oculta Sprites Bajo Techo
// ------------------------------

public void OcultaTecho(int X, int Y, int SizeX, int SizeY, int Bits)
{
	SizeX = (((X+SizeX)/8)-(X/8))+1;
	SizeY = (((Y+SizeY)/8)-(Y/8))+1;

	int Dir=((Y/8)*ga.FaseSizeX)+X/8;

	int DirX=((X&-8)-scroll.ScrollX);
	int DirY=((Y&-8)-scroll.ScrollY);

	for (int y=0 ; y<SizeY ; y++)
	{
		for (int x=0 ; x<SizeX ; x++)
		{
			int Tile=ga.FaseMap[Dir+x];
			if ((((ga.TilDatTab[Tile+128] & Bits) != 0) )
			||	( (y>=SizeY-2) && ((ga.TilDatTab[Tile+128] & 0x040) != 0)))
			{
			if (Tile < 0) {Tile+=256;}
			ImageSET(TilesImg,  (Tile%26)*8, (Tile/26)*8,  8,8,  DirX+(x*8), DirY );
			}
		}
		Dir+=ga.FaseSizeX;
		DirY+=8;
	}
};




// -------------------
// AniSprGrupoSET
// ===================

int AniSprGrupoC;
AnimSprite[] AniSprGrupos;

public void AniSprGrupoSET(int MAX)
{
	AniSprGrupoC = 0;
	AniSprGrupos = new AnimSprite[MAX];
}


// -------------------
// AniSprGrupoADD
// ===================

public void AniSprGrupoADD(AnimSprite[] s, int[] P, int C)
{
	for (int i=0 ; i<C ; i++)
	{
		if ((s[P[i]].CoorX+24 > scroll.ScrollX)
		&&	(s[P[i]].CoorX    < scroll.ScrollX+scroll.ScrollSizeX)
		&&	(s[P[i]].CoorY+24 > scroll.ScrollY)
		&&	(s[P[i]].CoorY    < scroll.ScrollY+scroll.ScrollSizeY))
		{
		AniSprGrupos[AniSprGrupoC++] = s[P[i]];
		}
	}
}




// -------------------
// Monitor IMP Gfx
// ===================

public void MonitorIMP_Gfx()
{
	if (ga.Monitor_ON==10)
	{

	CanvasImageSET("/Gfx/21.png",0);

	} else {

	CanvasFill(0);

	int x=(ga.GameSizeX-(TextosObj[(4*4)+2]))/2;
	int y=(ga.GameSizeY-(TextosObj[(4*4)+3]))/2;

	int RGB = new int[] {0xFFFF00,0xFF0000,0x00FF00,0x0000FF} [ga.Monitor_ON & 3];

	FillSET(RGB,  TextosObj,  8,   0,0);
	FillSET(RGB,  TextosObj,  9,   ga.GameSizeX-8,0);
	FillSET(RGB,  TextosObj, 10,   0,ga.GameSizeY-8);
	FillSET(RGB,  TextosObj, 11,   ga.GameSizeX-8,ga.GameSizeY-8);

	FillSET(RGB,  TextosObj,  4,   x,y);
	FillSET(RGB,  TextosObj,  4+ga.Monitor_ON,   x+32,y);


	ImageSET(TextosImg,  TextosObj,  8,   0,0);
	ImageSET(TextosImg,  TextosObj,  9,   ga.GameSizeX-8,0);
	ImageSET(TextosImg,  TextosObj, 10,   0,ga.GameSizeY-8);
	ImageSET(TextosImg,  TextosObj, 11,   ga.GameSizeX-8,ga.GameSizeY-8);

	ImageSET(TextosImg,  TextosObj,  4,   x,y);
	ImageSET(TextosImg,  TextosObj,  4+ga.Monitor_ON,   x+32,y);
	}
}

// <=- <=- <=- <=- <=-




// -------------------
// Sound - Engine
// ===================

public void BorraAlarma()
{
	for (int i=0 ; i<ga.FaseMap.length ; i++)
	{
	if (ga.FaseMap[i]==(byte)0xA1) {ga.FaseMap[i]=(byte)0x97;}
	else
	if (ga.FaseMap[i]==(byte)0xA2) {ga.FaseMap[i]=(byte)0x90;}
	}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
// -------------------
// Version ESPECIAL: Motorola V300 - Rev.0 (03.1.2004)
// ===================
// *******************

Music[] Sonidos;

int SoundOld = -1;
int SoundCache = -1;

// -------------------
// Sound INI
// ===================

public void SoundINI()
{
	Sonidos = new Music[5];

	Sonidos[0] = SoundCargar("/Themute_intro.mid");
	Sonidos[1] = SoundCargar("/Themute_level.mid");
	Sonidos[2] = SoundCargar("/Themute_alarm.mid");
	Sonidos[3] = SoundCargar("/Themute_gameover.mid");
	Sonidos[4] = SoundCargar("/Themute_item.mid");
}

	public Music SoundCargar(String Nombre)
	{
		return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre));
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
			if (SoundCache != Ary)
			{
			//if (SoundCache!=-1) {Sonidos[SoundCache].deallocate();}
			//Sonidos[Ary].prefetch();
			SoundCache = Ary;
			}

			//VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
			Sonidos[Ary].setLooping(Loop==0);

			Sonidos[Ary].play();
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
	if (ga.GameVibra!=0)
	{
		try
		{
			DeviceControl.startVibra(100, Time);
		}
		catch (Exception e) {}
	}
}

// <=- <=- <=- <=- <=-




// **************************
// --------------------------
// Command Listerner - Engine v1.0 - Rev.0 (9.7.2003)
// ==========================
// **************************

// -----------------------------------
// A�adir a la definicion de la Clase:
// -----------------------------------
// implements CommandListener
// ===================================

String[] ListenerStr = new String[] { "Menu","Selec" };

Command[] ListenerCmd;

// -------------------
// Listener EXE
// ===================

public void ListenerEXE(int CMD)
{
/*
	switch (CMD)
	{
	case 0:		// Menu
		ga.KeybB = 10;
	break;

	case 1:		// Aceptar
		ga.KeybB = 5;
	break;
	}
*/
}


// -------------------
// Listener SET
// ===================

public void ListenerSET(Canvas c)
{
	ListenerCmd = new Command[ListenerStr.length];

	for (int i=0 ; i<ListenerStr.length ; i++)
	{
	ListenerCmd[i] = new Command(ListenerStr[i], Command.SCREEN, 1);
	c.addCommand(ListenerCmd[i]);
	}
	c.setCommandListener(this);
}

// -------------------
// Listener RES
// ===================

public void ListenerRES(Canvas c)
{
	for (int i=0 ; i<ListenerCmd.length ; i++)
	{
	c.removeCommand(ListenerCmd[i]);
	ListenerCmd[i] = null;
	}
	ListenerCmd = null;
}

// -----------------------
// Listener command Action
// =======================

public void commandAction (Command c, Displayable d)
{
	if (ListenerCmd!=null)
	{
		for (int i=0 ; i<ListenerCmd.length ; i++)
		{
		if (c == ListenerCmd[i]) {ListenerEXE(i); return;}
		}
	}
}

// <=- <=- <=- <=- <=-




// *******************


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