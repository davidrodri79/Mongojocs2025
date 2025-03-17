package com.mygdx.mongojocs.camaraoscura;


// ------------------------------------------------------
// Camara Oscura v2.0 Rev.1 (17.7.2003) - Canvas
// ======================================================
// Nokia series 40
// ------------------------------------




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

public class GameCanvas extends FullCanvas
{

// ---------------------------------- //
//  Bits de Control de cada Terminal
// ---------------------------------- //
final boolean DeviceSound = true;
final boolean DeviceVibra = false;

final static int ScWait=0;		// Marco Scroll Wait
final static int ScDesp=1;		// Marco Scroll Despazamiento
// ---------------------------------- //

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

boolean CanvasPaint;

public void paint (Graphics g)
{       
	SoundRUN();

	if (CanvasPaint)
	{
	CanvasPaint = true;

	if (CanvasFillPaint)
	{
	g.setClip(0,0, 176,208);
	g.setColor(0);
	g.fillRect(0,0, 176,208);
	}
	LCD_Gfx=g;
	LCD_dGfx= DirectUtils.getDirectGraphics(g);

	g.translate(0, 8);
	CanvasRUN();
	g.translate(0,-8);
	}

//	if (--TimeCnt < 1) {TimeCnt=50; TimeNow=System.currentTimeMillis(); TimeDat=(int)((TimeNow-TimeOld)/10); TimeOld=TimeNow;}
//	g.setClip( 0,0, CanvasSizeX, CanvasSizeY ); g.setColor(-1); g.fillRect(0,0,  62,12 ); g.setColor(0);
//	g.drawString(""+ga.JaulaY,  0,0, Graphics.LEFT|Graphics.TOP);
//	g.drawString(Integer.toString(ga.FasePortaTab[10][1][0]), 0,0, Graphics.LEFT|Graphics.TOP);

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

public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int Flip=0;
	if (SizeX < 0) {SizeX*=-1; Flip|=1;}
	if (SizeY < 0) {SizeY*=-1; Flip|=2;}
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-sc.ScrollX,Y+DestY-sc.ScrollY, Flip);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       > CanvasSizeX)
	||	(DestX+SizeX < 0)
	||	(DestY       > CanvasSizeY)
	||	(DestY+SizeY < 0))
	{return;}

	LCD_Gfx.setClip(0, 0, ga.GameSizeX, ga.GameSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
		// MONGOFIX +8 en Y. No se porque es necesario esto, solo en Camara Oscura
	LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY+8, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
	return;

	case 2:
	LCD_dGfx.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
	LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
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

//int CanvasSizeX=getWidth();
//int CanvasSizeY=getHeight();
int CanvasSizeX=176;
int CanvasSizeY=192;

boolean CanvasFillPaint=false;
int CanvasFillRGB;
Image CanvasImg;

Scroll sc;

Image GloboImg;
Image FlechitaImg;

// -------------------
// Canvas INI
// ===================

public void CanvasINI()
{
	_CanvasImageSET("/Gfx/Manga.png", 0);

	sc = new Scroll();

	CanvasPaint=true;
	repaint();
	serviceRepaints();
}


// -------------------
// Canvas SET
// ===================

public void CanvasSET()
{
	ga.ProtSpd = 2;
	ga.TileSize = 12;

	SoundINI();

	GloboImg = LoadImage("/Gfx/Globo.png");
	FlechitaImg = LoadImage("/Gfx/Flechita.png");

	ga.LoadFile(ProtaCor, "/Gfx/Prota.cor");

	ga.LoadFile(EnemyCor, "/Gfx/Enemy.cor");

}


// -------------------
// Canvas Img SET
// ===================

public void _CanvasImageSET(String FileName, int RGB)
{
	CanvasFillRGB=RGB; CanvasFillPaint=true;
	CanvasImg = new Image();
	CanvasImg._createImage(FileName);
}

	public void CanvasImageSET(String FileName, int RGB)
	{
		CanvasFillRGB=RGB; CanvasFillPaint=true;
		CanvasImg = Image.createImage(FileName);
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
	if (ga.MenuPaint) { ga.MenuPaint=false; MenuIMP_Gfx(); }

	if (ga.JugarPaint) { ga.JugarPaint=false; JugarIMP_Gfx(); }

	if (ga.GloboPaint) { ga.GloboPaint=false; GloboIMP_Gfx(); }

	if (ga.FlechitaPaint) { ga.FlechitaPaint=false; FlechitaIMP_Gfx(); }

	if (ga.ma != null && ga.ma.Marco_ON != 0) { ga.ma.MarcoIMP(LCD_Gfx); }
}

// <=- <=- <=- <=- <=-








// ********************
// --------------------
// Jugar - Engine - Gfx
// ====================
// ********************

Image TilesImg;
Image ProtaImg;
Image BoteImg;
Image TimeImg;
Image EnemyImg;
Image JaulaImg;

byte[] ProtaCor = new byte[480];
byte[] EnemyCor = new byte[288];

// -------------------
// Jugar INI Gfx
// ===================

public void JugarINI_Gfx()
{
	System.gc();

	ProtaImg=	LoadImage("/Gfx/Prota.png");

	EnemyImg=	LoadImage("/Gfx/Enemy.png");

	BoteImg = LoadImage("/Gfx/Bote.png");

	JaulaImg = LoadImage("/Gfx/Jaula.png");

	TimeImg = LoadImage("/Gfx/Time.png");

	sc.ScrollINI(ga.GameSizeX, ga.GameSizeY );

	System.gc();
};


// -------------------
// Jugar SET Gfx
// ===================

int TileSetBarco=-1;

public void JugarSET_Gfx()
{
	if (TilesImg == null) {TileSetBarco=-1;}

	if (ga.JugarLevel==0 && TileSetBarco!=0)
	{
		sc.TilesImg = null;
		TilesImg = null; System.gc();
		TilesImg = LoadImage("/Gfx/cubierta.png");
		TileSetBarco = 0;
	}

	if (ga.JugarLevel!=0 && TileSetBarco!=1)
	{
		sc.TilesImg = null;
		TilesImg = null; System.gc();
		TilesImg = LoadImage("/Gfx/camarotes.png");
		TileSetBarco = 1;
	}

	sc.ScrollSET(ga.FaseMap, ga.FaseSizeX, ga.FaseSizeY,  TilesImg, 16);
};


// -------------------
// Jugar END Gfx
// ===================

public void JugarEND_Gfx()
{
	System.gc();
	TilesImg=null;
	ProtaImg=null;
	EnemyImg=null;
	BoteImg=null;
	JaulaImg=null;
	TimeImg=null;
	sc.ScrollEND();
	System.gc();
};


// -------------------
// Jugar IMP Gfx
// ===================

public void JugarIMP_Gfx()
{

	if (ga.CamaraProt)
	{
	sc.ScrollRUN_Centro_Max(ga.ProtX+12, ga.ProtY+12);
	} else {
	sc.ScrollRUN_Centro_Max(ga.DemoX+12, ga.DemoY+12);
	}

	sc.ScrollIMP(LCD_Gfx);


// Imprimimos AnimSprites
// ----------------------
	AniSprIMP(ga.AniSprs, ga.AniSprP, ga.AniSprC);


// Imprimimos Enemigos
// ----------------------
	AniSprIMP(ga.Enemigos, ga.EnemigoP, ga.EnemigoC);


// Imprimimos Ratas
// ----------------------
	AniSprIMP(ga.Ratas, ga.RataP, ga.RataC);



// Imprimimos Timer
// ----------------

	if (ga.JugarConta>0)
	{
	LCD_Gfx.setFont( GloboListGetFont() );
	LCD_Gfx.setColor(0);
	ImageSET(TimeImg, 0,0);
	LCD_Gfx.drawString(Integer.toString(ga.JugarConta), 6,3, 20);
	}



// Imprimimos vida
// ---------------

	if (ga.ProtVida>0)
	{
	int max = CanvasSizeX - (CanvasSizeX / 2);
	int size = ga.Regla3(ga.ProtVida, 100, max);

	LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);
	LCD_Gfx.setColor(00);
	LCD_Gfx.fillRect(((CanvasSizeX-size)/2)-1, 5, size+2, 4);
	LCD_Gfx.setColor(0xAA0000);
	LCD_Gfx.fillRect((CanvasSizeX-size)/2, 6, size, 2);
	}


};


// <<<<<<<<<<<<<<<<<

public void AniSprIMP(AnimSprite[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
		switch (s[i].Bank)
		{
		case 0:
		PutSprite(ProtaImg,  s[i].CoorX, s[i].CoorY+4,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, ProtaCor);
		break;	

		case 1:
		PutSprite(EnemyImg,  s[i].CoorX, s[i].CoorY+4,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, EnemyCor);
		break;	

		case 2:
		LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);
		LCD_Gfx.setColor(0);
		LCD_Gfx.fillRect(( 97*ga.TileSize)+3-sc.ScrollX, (14*ga.TileSize)-sc.ScrollY, 2, ga.BoteY+ga.TileSize);
		LCD_Gfx.fillRect((104*ga.TileSize)+3-sc.ScrollX, (14*ga.TileSize)-sc.ScrollY, 2, ga.BoteY+ga.TileSize);
		ImageSET(BoteImg,  s[i].CoorX-sc.ScrollX + ga.BoteX, s[i].CoorY-sc.ScrollY + ga.BoteY );
		break;	

		case 3:
		LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);
		LCD_Gfx.setColor(0);
		LCD_Gfx.fillRect(  s[i].CoorX-sc.ScrollX+18, 0-sc.ScrollY, 2, s[i].CoorY + ga.JaulaY);

//		PutSprite(ProtaImg,  s[i].CoorX+2, s[i].CoorY + ga.JaulaY + 6,  ga.AmigoFr+15, ProtaCor);

		ImageSET(JaulaImg, s[i].CoorX-sc.ScrollX, s[i].CoorY-sc.ScrollY + ga.JaulaY );
		break;	

		}
	}
}


// --------------------
// Jugar - Globo - Gfx
// ====================

public void GloboIMP_Gfx()
{

int SizeX = ga.GloboMaxX;
int SizeY = ga.GloboMaxY;
int X = (CanvasSizeX-SizeX)/2;
//int Y = (CanvasSizeY-SizeY)/2;
int Y = 12;


	if (ga.GloboMarcoPaint)
	{
	ga.GloboMarcoPaint = false;

	LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);

	LCD_Gfx.setColor(0xFFFFFF);
	LCD_Gfx.fillRect(X-6, Y,   SizeX+12, SizeY);
	LCD_Gfx.fillRect(X,   Y-6, SizeX,    SizeY+12);

	ImageSET(GloboImg,  0,0, 6,6, X-6,Y-6);
	ImageSET(GloboImg,  6,0, 6,6, X+SizeX,Y-6);
	ImageSET(GloboImg, 12,0, 6,6, X-6,Y+SizeY);
	ImageSET(GloboImg, 18,0, 6,6, X+SizeX,Y+SizeY);


	LCD_Gfx.setClip(0,0, CanvasSizeX, CanvasSizeY);

	LCD_Gfx.setColor(0);
	LCD_Gfx.drawLine(X,Y-6,  X+SizeX, Y-6);
	LCD_Gfx.drawLine(X,Y+SizeY+5,  X+SizeX, Y+SizeY+5);
	LCD_Gfx.drawLine(X-6,Y,  X-6, Y+SizeY);
	LCD_Gfx.drawLine(X+SizeX+5,Y,  X+SizeX+5, Y+SizeY);

	LCD_Gfx.setColor(0xC0C0D0);
	LCD_Gfx.drawLine(X,Y-5,  X+SizeX, Y-5);
	LCD_Gfx.drawLine(X,Y+SizeY+4,  X+SizeX, Y+SizeY+4);
	LCD_Gfx.drawLine(X-5,Y,  X-5, Y+SizeY);
	LCD_Gfx.drawLine(X+SizeX+4,Y,  X+SizeX+4, Y+SizeY);

	}




	if (ga.GloboList_ON)
	{

		Font f = GloboListGetFont();
		LCD_Gfx.setFont(f);

		LCD_Gfx.setColor(0);

		if (ga.GloboPosY < ga.GloboListStr.length)
		{

		String texto = ga.GloboListStr[ga.GloboPosY][0].substring(0,ga.GloboPosX);

		LCD_Gfx.drawString(texto, X,Y+(ga.GloboPosY*f.getHeight() ), 20);

		ga.GloboPosX++;

		if (ga.GloboPosX > ga.GloboListStr[ga.GloboPosY][0].length() ) {ga.GloboPosX=1; ga.GloboPosY++;}

		}

	}

}

public Font GloboListGetFont()
{
	return Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL );
}



// --------------------
// Jugar - Flechita - Gfx
// ====================

public void FlechitaIMP_Gfx()
{
	ImageSET(FlechitaImg, 40+(ga.ProtSelect*20),132);
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
	System.gc();
	MenuImg = LoadImage("/Gfx/Caratula.png");
	System.gc();
};

// -------------------
// Menu END Gfx
// ===================

public void MenuEND_Gfx()
{
	System.gc();
	MenuImg = null;
	System.gc();
};


// -------------------
// Menu IMP Gfx
// ===================

public void MenuIMP_Gfx()
{
	CanvasFill(0);
	ImageSET(MenuImg);
}




/* ===================================================================

	SoundINI()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	SoundSET(n� Sonido , Repetir)
	-----------------------------
		Hacemos que suene un sonido (0 a x) y que se repita x veces.
		Repetir == 0: Repeticion infinita

	SoundRES()
	----------
		Paramos el ultimo sonido.

	SoundRUN()
	----------
		Debemos ejecutar este metodo en CADA ciclo para gestionar 'tiempos'

	VibraSET(microsegundos)
	-----------------------
		Hacemos vibrar el mobil x microsegundos

=================================================================== */





// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
// -------------------
// Version ESPECIAL: Motorola V300 - Rev.0 (03.1.2004)
// ===================
// *******************

	Player[] Sonidos;

	int SoundOld = -1;
	int SoundCache = -1;

// -------------------
// Sound INI
// ===================

	public void SoundINI()
	{
		Sonidos = new Player[5];

		Sonidos[0] = SoundCargar("main_theme.mid");
		Sonidos[1] = SoundCargar("palanca.mid");
		Sonidos[2] = SoundCargar("muerte_prota.mid");
		Sonidos[3] = SoundCargar("muerte_enemy.mid");
		Sonidos[4] = SoundCargar("item.mid");
	}

	public Player SoundCargar(String Nombre)
	{
		Player p = null;

		try
		{
			InputStream is = getClass().getResourceAsStream(Nombre);
			p = Manager.createPlayer( Nombre , "audio/midi");
			p.realize();
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
				if (SoundCache != Ary)
				{
					if (SoundCache!=-1) {Sonidos[SoundCache].deallocate();}
					Sonidos[Ary].prefetch();
					SoundCache = Ary;
				}

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
	if (ga.GameVibra==0) {return;}

	try
	{
		DeviceControl.startVibra(32,Time);
	}
	catch (Exception e) {}
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