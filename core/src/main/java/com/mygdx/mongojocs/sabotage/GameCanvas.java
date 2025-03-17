


// ------------------------
// Sabotage (Version Color)
// ========================
// Motorola V300
// ------------------------


package com.mygdx.mongojocs.sabotage;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;			// clase GENERICA (soportada por TODOS los devices)


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - GameCanvas 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

public class GameCanvas extends Canvas implements CommandListener
{

Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Image PortadaImg;
Image SprProtImg;
Image SprEnemImg;
Image TilesImg;
Image LoadingImg;
Image IconMenuImg;

byte[] SprProtCor = new byte[2880];
byte[] SprEnemCor = new byte[2016];

int IconMenuX=0;

public GameCanvas(Game ga)
{
	this.ga = ga;

	try	{
	System.gc();
	LoadingImg = new Image();
	LoadingImg._createImage("/gfx/Loading.png");
	System.gc();
	if (getHeight()>=ga.GameMaxY+10)
		{
		IconMenuImg = new Image();
		IconMenuImg._createImage("/gfx/IconMenu.png");
		System.gc();
		}
	} catch (Exception e) {}

	ga.LoadFile(SprProtCor,"/gfx/SprProt.cor");
	ga.LoadFile(SprEnemCor,"/gfx/SprEnem.cor");

	System.gc();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics LCD_Gfx;

//long TimeNow, TimeOld;
//int TimeDat, TimeCnt;

public void paint (Graphics g)
{       
	LCD_Gfx=g;

	CanvasRUN();

/*
	if (--TimeCnt < 1)
	{
	TimeCnt=50;
	TimeNow=System.currentTimeMillis();
	TimeDat=(int)((TimeNow-TimeOld)/10);
	TimeOld=TimeNow;
	}

	g.setClip( 0,0, getWidth(), getHeight() );
	g.setColor(-1);
	g.fillRect(0,0,  62,12 );
	g.setColor(0);
	g.drawString(Integer.toString(ga.GameSleep),  0,0, Graphics.LEFT|Graphics.TOP);
	g.drawString(Integer.toString(TimeDat), 30,0, Graphics.LEFT|Graphics.TOP);
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



public void PutSprite(Image Img, int Width, int X, int Y,  int SizeX, int SizeY,  int Frame)
{
	LCD_Gfx.setClip(X,Y, SizeX,SizeY);
	LCD_Gfx.drawImage (Img, X-((Frame%Width)*SizeX), Y-((Frame/Width)*SizeY), Graphics.TOP|Graphics.LEFT);	// Pintamos en el LCD
}



public void PutImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	LCD_Gfx.setClip(0, 0, ga.GameSizeX, ga.GameSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}



public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
{
	if ((Y    < ScrollY + ScrollSizeY)
	&&	(Y+32 > ScrollY)
	&&	(X    < ScrollX + ScrollSizeX)
	&&	(X+24 > ScrollX))
	{
		Frame*=(Trozos*6);

		for (int i=0 ; i<Trozos ; i++)
		{
		int DestX=Coor[Frame++];
		int DestY=Coor[Frame++];
		int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
		int SizeY=Coor[Frame++];
		int SourX=Coor[Frame++];
		int SourY=Coor[Frame++];

		ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  LCD_Gfx, X+DestX,Y+DestY);
		}
	}
}



public void PutSprite2(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=(8*6);

	for (int i=0 ; i<8 ; i++)
	{
	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++]; if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++];
	int SourY=Coor[Frame++];

	PutImage(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY);
	}

}



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

// -------------------
// Canvas INI
// ===================
/*
public void CanvasINI()
{
}
*/
// -------------------
// Canvas END
// ===================
/*
public void CanvasEND()
{
	ScrollEND();
}
*/
// -------------------
// Canvas RUN
// ===================

public void CanvasRUN()
{
	if (ga.GameStatus==0) {return;}


	LCD_Gfx.setClip (0,0, getWidth(),getHeight() );


	if (ga.Paint_ON!=0)
	{
	ga.Paint_ON=0;
	LCD_Gfx.setColor(ga.PaintRGB);
	LCD_Gfx.fillRect(0,0, getWidth(),getHeight() );
	}


	if (LoadingImg!=null)
	{
	LCD_Gfx.setColor(-1);
	LCD_Gfx.fillRect(0,0, getWidth(),getHeight() );
	LCD_Gfx.drawImage (LoadingImg, (getWidth()-LoadingImg.getWidth())/2, (getHeight()-LoadingImg.getHeight())/2, Graphics.TOP|Graphics.LEFT);
	LoadingImg=null;
	}



	LCD_Gfx.translate(ga.GameX, ga.GameY);


	if (ga.Marco_ON==0 || ga.MarcoUpdate!=0)
	{
		ga.MarcoUpdate=0;

		if (ga.IntroPaint!=0) { IntroIMP(); if (ga.IntroPaint > 0) {ga.IntroPaint--;}}


		if (ga.FinalPaint!=0) { FinalIMP(); if (ga.FinalPaint > 0) {ga.FinalPaint--;}}


		if (ga.FasePaint!=0) { FaseIMP(); }


		if (ga.PortadaPaint!=0)
		{
		LCD_Gfx.setClip (0,0, ga.GameSizeX,ga.GameSizeY );
		LCD_Gfx.setColor(0);
		LCD_Gfx.fillRect(0,0, ga.GameSizeX,ga.GameSizeY );
		int x=PortadaImg.getWidth();
		int y=PortadaImg.getHeight();
		PutImage(PortadaImg,  0,0,  x,y,  (ga.GameSizeX-x)/2,(ga.GameSizeY-y)/2);
		}
	}


	if (ga.Parco_ON!=0) {ga.ParcoIMP_Now(LCD_Gfx);}



	LCD_Gfx.translate(-ga.GameX, -ga.GameY);

	if (IconMenuImg!=null)
	{
	LCD_Gfx.setClip(-35+IconMenuX,getHeight()-10, 35,10);
	LCD_Gfx.drawImage(IconMenuImg,-35+IconMenuX,getHeight()-10,20);
	IconMenuX++;if(IconMenuX>=35){IconMenuX=35;}
	}

}

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// Intro GFX
// ===================
// *******************

Image SableImg;
Image VainaImg;
Image KanjiImg;
Image NinjaImg;
byte[] NinjaCor;

// -------------------
// Intro GFX Load
// ===================

public void IntroGFX_Load()
{

	System.gc();
	try	{
	SableImg = Image.createImage("/gfx/Sable.png");
	System.gc();
	VainaImg = Image.createImage("/gfx/Vaina.png");
	System.gc();
	KanjiImg = Image.createImage("/gfx/Kanji.png");
	System.gc();
	NinjaImg = Image.createImage("/gfx/Ninja.png");
	} catch (Exception e) {}
	System.gc();

	NinjaCor = new byte[864];
	ga.LoadFile(NinjaCor,"/gfx/Ninja.cor");
	System.gc();
}

// -------------------
// Intro GFX Kill
// ===================

public void IntroGFX_Kill()
{
	System.gc();
	SableImg=null;
	VainaImg=null;
	KanjiImg=null;
	NinjaImg=null;
	NinjaCor=null;
	System.gc();
}

// -------------------
// Intro IMP
// ===================

public void IntroIMP()
{
	LCD_Gfx.setColor(0);
	LCD_Gfx.fillRect(0,0, ga.GameSizeX,ga.GameSizeY );


	if (ga.IntroMode < 9)
	{
	PutImage(SableImg,   0, 2,  120,25,  ga.SableX+  0, ga.SableY+28);
	PutImage(SableImg,   0,28,  120,25,  ga.SableX+120, ga.SableY+28);
	PutImage(SableImg,   0,54,  120,25,  ga.SableX+240, ga.SableY+28);
	PutImage(SableImg, 120, 0,   10,80,  ga.SableX+360, ga.SableY   );
	PutImage(SableImg, 130, 0,   67,47,  ga.SableX+370, ga.SableY+18);
	PutImage(SableImg, 130,48,   68,26,  ga.SableX+437, ga.SableY+28);

	PutImage(VainaImg,   0,0,  86,47,  ga.VainaX,ga.VainaY);

	int x=ga.VainaX;
	while (x > 0)
	{
	PutImage(VainaImg,  49,7,  29,32,  x-29,ga.VainaY+7);
	x-=29;
	}


	}
	else if (ga.IntroMode < 19)
	{
		if (ga.IntroTime>4)
		{
			int X=0;
			int Y=ga.NinjaY;

			do{
				PutSprite2(NinjaImg, X,  Y,  0,NinjaCor);
				X+=32;
			} while (X < ga.GameSizeX);


			if (ga.NinjaFrame!=0)
			{
			PutSprite2(NinjaImg, ga.NinjaX,   ga.NinjaY, ga.NinjaFrame,  NinjaCor);
			}
			PutSprite2(NinjaImg, ga.NinjaX+32, ga.NinjaY,  ga.NinjaFrame+1, NinjaCor);
			PutSprite2(NinjaImg, ga.NinjaX+64, ga.NinjaY,  ga.NinjaFrame+2, NinjaCor);
		} else {
			if (ga.NinjaFrame < 5*24 )
			{
			LCD_Gfx.setColor(-1);
			LCD_Gfx.fillRect(0, ga.NinjaY+1, ga.GameSizeX, 62 );
			}
		}
	}
	else if (ga.IntroMode < 29)
	{
	PutImage(KanjiImg,   0,  0,  88,112,  ga.KanjiX,ga.KanjiY);
	PutImage(KanjiImg,   0,112,  83, 22,  ga.SabotX,ga.SabotY);
	}

}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Portada GFX
// ===================
// *******************

// -------------------
// Portada GFX Load
// ===================

public void PortadaGFX_Load()
{
	System.gc();
	try	{
	PortadaImg = Image.createImage("/gfx/Portada.png");
	} catch (Exception e) {}
	System.gc();
}


// -------------------
// Portada GFX Kill
// ===================

public void PortadaGFX_Kill()
{
	System.gc();
	PortadaImg=null;
	System.gc();
}

// <=- <=- <=- <=- <=-


// *********************
// ---------------------
// Final - Engine - Gfx
// =====================
// *********************

Image FinalImg;
int FinalX;

// -------------------
// Final INI Gfx
// ===================

public void FinalINI_Gfx()
{
	FinalX=ga.GameSizeX*2;

	System.gc();
	try	{
	FinalImg = Image.createImage("/gfx/Final.png");
	} catch (Exception e) {}
	System.gc();
}

// -------------------
// Final END Gfx
// ===================

public void FinalEND_Gfx()
{
	System.gc();
	FinalImg=null;
	System.gc();
}

// -------------------
// Final IMP Gfx
// ===================

public void FinalIMP()
{
	PutImage(FinalImg, 0,0, FinalImg.getWidth(),62, FinalX/2, (ga.GameSizeY-FinalImg.getHeight())/2 );
	if (--FinalX < -FinalImg.getWidth()*2 ) {ga.FinalExit=1;}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// Jugar GFX
// ===================
// *******************

// -------------------
// Jugar GFX Load
// ===================

public void JugarGFX_Load()
{
	System.gc();
	ScrollINI( ga.GameSizeX, ga.GameSizeY );

	System.gc();
	try	{
	SprProtImg = Image.createImage("/gfx/SprProt.png");
	System.gc();
	SprEnemImg = Image.createImage("/gfx/SprEnem.png");
	System.gc();
	TilesImg = Image.createImage("/gfx/Tiles.png");
	} catch (Exception e) {}
	System.gc();

	ScrollSET(ga.FaseMap, ga.FaseSizeX, ga.FaseSizeY,  TilesImg, 19);
	System.gc();
}

// -------------------
// Jugar GFX Kill
// ===================

public void JugarGFX_Kill()
{
	System.gc();
	SprProtImg=null;
	SprEnemImg=null;
	TilesImg=null;
	System.gc();

	ScrollEND();

	System.gc();
}


// -------------------
// Jugar IMP
// ===================

public void FaseIMP()
{
	ScrollRUN_Centro_Max(ga.ProtX+8, ga.ProtY+10);
	ScrollIMP(LCD_Gfx);


//	Imprimimos Sprites
// -------------------
	for (int i, t=0 ; t<ga.AnimSpriteC ; t++)
	{
	i=ga.AnimSpriteP[t];
		if (ga.AnimSprites[i][ga.asBank]==0)
		{
		PutSprite(SprProtImg,  ga.AnimSprites[i][ga.asCoorX], ga.AnimSprites[i][ga.asCoorY],  ga.AnimSprites[i][ga.asFrameIni]+ga.AnimSprites[i][ga.asFrameAct], SprProtCor,4);
		} else {
		PutSprite(SprEnemImg,  ga.AnimSprites[i][ga.asCoorX], ga.AnimSprites[i][ga.asCoorY],  ga.AnimSprites[i][ga.asFrameIni]+ga.AnimSprites[i][ga.asFrameAct], SprEnemCor,4);
		}
	}

// Marcadores
// ----------
	PutSprite(TilesImg, 19,   0,0,  8,8,  0x35);
	PutSprite(TilesImg, 19,   8,0,  8,8,  0x37 + ga.ProtStars);

	PutSprite(TilesImg, 19,   ga.GameSizeX-16,0,  8,8,  0x36);
	PutSprite(TilesImg, 19,   ga.GameSizeX- 8,0,  8,8,  0x37 + ga.JugarVidas);
}

// <=- <=- <=- <=- <=-





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

	Sonidos[0] = SoundCargar("/Sabotage_muerte1.mid");
	Sonidos[1] = SoundCargar("/Sabotage_intro.mid");
	Sonidos[2] = SoundCargar("/Sabotage_item.mid");
	Sonidos[3] = SoundCargar("/Sabotage_muerte2.mid");
	Sonidos[4] = Sonidos[3];
}

public Player SoundCargar(String Nombre)
{
	Player p = null;

	try
	{
		//InputStream is = getClass().getResourceAsStream(Nombre);
		p = Manager.createPlayer( Nombre , "audio/midi");
		p.realize();
	}
	catch(Exception exception) {exception.printStackTrace();}

	return p;
	//return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre));
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
	if (ga.GameVibra!=0)
	{
		try
		{
			Display.getDisplay(ga).vibrate(Time);
		}
		catch (Exception e) {}
	}
}

// <=- <=- <=- <=- <=-






// -------------------------------------
// Scroll Class v1.2 - Rev.5 (21.2.2003)
// =====================================
// Programado por Juan Antonio G�mez
// Para uso exclusivo de Microjocs S.L.
// -------------------------------------



// *******************
// -------------------
// Scroll - Engine
// ===================
// *******************

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

Image TilesIng;
int TilesLineX;

int ScrollX;
int ScrollY;
int ScrollSizeX;
int ScrollSizeY;

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
			FondoImg._createImage(FondoSizeX*8, FondoSizeY*8);
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

	TilesIng=Img;
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

public void ScrollRUN_Centro_Max(int X, int Y)
{
	X-=(ScrollSizeX/2);
	Y-=(ScrollSizeY/2);

	if (X<0) {X=0;} else if (X>=(FaseSizeX*8)-ScrollSizeX) {X=(FaseSizeX*8)-ScrollSizeX-1;}
	if (Y<0) {Y=0;} else if (Y>=(FaseSizeY*8)-ScrollSizeY) {Y=(FaseSizeY*8)-ScrollSizeY;}

	ScrollX=X;
	ScrollY=Y;

	FaseX=(X/8);
	FaseY=(Y/8);

	FondoX=(X/8)%FondoSizeX;
	FondoY=(Y/8)%FondoSizeY;

	ScrollUpdate();
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
			FondoGfx.setClip(0, 0,  FondoSizeX*8, FondoSizeY*8);

			int FondoDir=0;
			int FaseDir=((FaseY+(FondoSizeY-FondoY))*FaseSizeX)+(FaseX+(FondoSizeX-FondoX));

			for (int y=0 ; y<FondoSizeY ; y++)
			{
				if (y==FondoY) {if ((FaseDir-=(FondoSizeY*FaseSizeX)) < 0) {FaseDir+=(FaseSizeX*FaseSizeY);}}

				for (int x=0, x2=FondoX, i=0 ; i<2; i++)
				{
					for (; x<x2 ; x++)
					{
						if (FondoMap[FondoDir++] != FaseMap[FaseDir++])
						{
							int TileNum=FaseMap[FaseDir-1];
							FondoMap[FondoDir-1] = (byte)TileNum;
							if (TileNum < 0 ) {TileNum+=256;}
							FondoGfx.setClip(x*8 ,y*8,  8,8);
							FondoGfx.drawImage(TilesIng, (x*8)-((TileNum%TilesLineX)*8), (y*8)-((TileNum/TilesLineX)*8), Graphics.TOP|Graphics.LEFT);
						}
					}

					if (i==0)
					{
						if ((FaseDir-=FondoSizeX) < 0) {FaseDir+=(FaseSizeX*FaseSizeY);}
						x2=FondoSizeX;
					} else {
						if ((FaseDir+=FaseSizeX) >= FaseMap.length) {FaseDir-=(FaseSizeX*FaseSizeY);}
					}
				}
			}

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

}


// -------------------
// Scroll END
// ===================

public void ScrollEND()
{
	FaseMap=null;
	FondoMap=null;
	TilesIng=null;
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

String[] ListenerStr = new String[] { "Menu","Aceptar" };

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