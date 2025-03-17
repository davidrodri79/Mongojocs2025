


// ------------------------
// Aminoid X   * Canvas *
// ========================
// MIDP 2.0 Standard
// ------------------------


package com.mygdx.mongojocs.aminoid;


import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - GameCanvas 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

public class GameCanvas extends Canvas
{

Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga)
{
	this.ga = ga;

	CanvasFillRGB=0;
	CanvasFillPaint=0;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics LCD_Gfx;

boolean canvasShow = false;

public void paint (Graphics g)
{       
	if (canvasShow)
	{
		canvasShow = false;

		LCD_Gfx=g;
		CanvasRUN();
	}
/*
	System.gc();
	g.setClip(0,0, CanvasSizeX, CanvasSizeY);
	g.setColor(0);
	g.fillRect(0,0, 200,12);
	g.setColor(0xffffff);
	g.drawString(""+Runtime.getRuntime().freeMemory(), 0,0, 20);
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
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameRunning = false;
boolean forceDrawUpdate = false;

public void showNotify()
{
	ga.gamePaused=false;

	ga.KeybX = ga.KeybY = ga.KeybB = ga.KeybE = 0;

	if (gameRunning) {forceDrawUpdate = true;}
}

public void hideNotify()
{
	ga.KeybX = ga.KeybY = ga.KeybB = ga.KeybE = 0;

	ga.gamePaused=true;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	LCD_Gfx.setClip(DestX, DestY, SizeX, SizeY);

	DirectGraphics dg = DirectGraphics.getDirectGraphics(graphics);
	switch (Flip)
	{
	case 0:
		LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	break;

		case 1:
			dg.drawImage(Sour, DestX-Sour.getWidth()+SourX+SizeX, DestY-SourY, 20, DirectGraphics.FLIP_HORIZONTAL);
			break;

		case 2:
			dg.drawImage(Sour, DestX-SourX, DestY-Sour.getHeight()+SourY+SizeY, 20, DirectGraphics.FLIP_VERTICAL);
			break;

		case 3:
			dg.drawImage(Sour, DestX-Sour.getWidth()+SourX+SizeX, DestY-Sour.getHeight()+SourY+SizeY, 20, DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.FLIP_VERTICAL);
			break;
	}
}


/*
public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	LCD_Gfx.setClip(0, 0, ScrollSizeX, ScrollSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
		LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	break;

	case 1:
		LCD_Gfx.drawRegion(Sour,SourX,SourY,SizeX,SizeY, Sprite.TRANS_MIRROR, DestX, DestY, 20);	// Flip X
	break;

	case 2:
		LCD_Gfx.drawRegion(Sour,SourX,SourY,SizeX,SizeY, Sprite.TRANS_MIRROR_ROT180, DestX, DestY, 20);	// Flip Y
	break;

	case 3:
		LCD_Gfx.drawRegion(Sour,SourX,SourY,SizeX,SizeY, Sprite.TRANS_ROT180, DestX, DestY, 20);	// Flip XY
	break;
	}
}
*/


public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	LCD_Gfx.setClip(0, 0, ga.GameSizeX, ga.GameSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}


public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	LCD_Gfx.setClip(TopX, TopY, FinX, FinY);
	LCD_Gfx.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}



public void PutSprite2(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int flip=0;
	if (SizeX < 0) {SizeX*=-1; flip|=1;}
	if (SizeY < 0) {SizeY*=-1; flip|=2;}
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame  ]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY, flip);
}



public void PutSprite(Image Sour,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++] + X - ScrollX;
	int DestY=Coor[Frame++] + Y - ScrollY;
	int SizeX=Coor[Frame++];
	int SizeY=Coor[Frame++];
	int flip=0;
	if (SizeX < 0) {SizeX*=-1; flip|=1;}
	if (SizeY < 0) {SizeY*=-1; flip|=2;}

	if (SizeX==0 || DestX >= ScrollSizeX || DestY >= ScrollSizeY || (DestX + SizeX) <= 0 || (DestY + SizeY) <= 0) {return;}

	int SourX=Coor[Frame++] + 128;
	int SourY=Coor[Frame  ] + 128;

	LCD_Gfx.setClip(0, 0, ScrollSizeX, ScrollSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);

	DirectGraphics dg = DirectGraphics.getDirectGraphics(graphics);
	switch (flip)
	{
	case 0:
		LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	break;

		case 1:
			dg.drawImage(Sour, DestX-Sour.getWidth()+SourX+SizeX, DestY-SourY, 20, DirectGraphics.FLIP_HORIZONTAL);
			break;

		case 2:
			dg.drawImage(Sour, DestX-SourX, DestY-Sour.getHeight()+SourY+SizeY, 20, DirectGraphics.FLIP_VERTICAL);
			break;

		case 3:
			dg.drawImage(Sour, DestX-Sour.getWidth()+SourX+SizeX, DestY-Sour.getHeight()+SourY+SizeY, 20, DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.FLIP_VERTICAL);
			break;
	}

}



public Image loadImage(String FileName)
{
	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e) {}

	System.gc();
	return Img;
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

int CanvasSizeX=getWidth();
int CanvasSizeY=getHeight();

int CanvasFillRGB;
int CanvasFillPaint;
Image CanvasImg;


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

	if (CanvasFillPaint!=0) { CanvasFill(CanvasFillRGB); if (CanvasFillPaint > 0) {CanvasFillPaint--;}}

	if (CanvasImg!=null)
	{
	ImageSET (MenuImg,  0,0,  176,208,  0,0,    0,0, CanvasSizeX,CanvasSizeY);
	ImageSET (CanvasImg, 0,0, CanvasImg.getWidth(),CanvasImg.getHeight(), (CanvasSizeX-CanvasImg.getWidth())/2, (CanvasSizeY-CanvasImg.getHeight())/2,  0,0, CanvasSizeX,CanvasSizeY);
	CanvasImg=null; System.gc();
	}


	if (ga.IntroPaint!=0) { IntroIMP_Gfx(); if (ga.IntroPaint > 0) {ga.IntroPaint--;}}

	if (ga.MenuPaint!=0) { MenuIMP_Gfx(); if (ga.MenuPaint > 0) {ga.MenuPaint--;}}

	if (ga.JugarPaint!=0) { JugarIMP_Gfx(); if (ga.JugarPaint > 0) {ga.JugarPaint--;}}

	if (ga.FinalPaint!=0) { FinalIMP_Gfx(); if (ga.FinalPaint > 0) {ga.FinalPaint--;}}

}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Jugar - Engine - Gfx
// ====================
// ********************

Image NostromoImg;
Image TilesImg;
Image Spr16Img;
Image Spr32Img;
Image Spr64Img;
Image SprGuardianImg;
Image PanelImg;
Image ColinasImg;
byte[] Spr16Cor = new byte[1152];
byte[] Spr32Cor = new byte[2208];
byte[] Spr64Cor = new byte[72];
byte[] SprGuardianCor = new byte[252];

// -------------------
// Jugar INI Gfx
// ===================

public void JugarINI_Gfx()
{
//	MenuImg = null;

	scroll_ScrollINI( ga.GameSizeX, ga.GameSizeY-40 );

	Spr32Img = loadImage("/Gfx/Spr32.png");				// 23980
	Spr64Img = loadImage("/Gfx/Spr64.png");				// 22834
	NostromoImg = loadImage("/Gfx/Nostromo.png");		// 19800
	TilesImg = loadImage("/Gfx/Tiles.png");				// 16384
	SprGuardianImg = loadImage("/Gfx/SprGuardian.png");	// 16137
	Spr16Img = loadImage("/Gfx/Spr16.png");				// 15210
	PanelImg = loadImage("/Gfx/Panel.png");				// 11264
//	ColinasImg = loadImage("/Gfx/Colinas.png");			// 

	System.gc();
}


// -------------------
// Jugar SET Gfx
// ===================

public void JugarSET_Gfx()
{
	System.gc();
	scroll_ScrollSET(ga.FaseMap, ga.FaseSizeX, ga.FaseSizeY,  TilesImg, 16);
	System.gc();
}


// -------------------
// Jugar END Gfx
// ===================

public void JugarEND_Gfx()
{
	System.gc();
	NostromoImg = null;
	TilesImg = null;
	Spr16Img = null;
	Spr32Img = null;
	Spr64Img = null;
	SprGuardianImg = null;
	PanelImg = null;
	ColinasImg = null;
	scroll_ScrollEND();
	System.gc();
}

// <=- <=- <=- <=- <=-


public void AniSprIMP(AniSpr[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
	PutSprite(Spr16Img,  s[i].CoorX, s[i].CoorY,  s[i].FrameIni + s[i].FrameAct, Spr16Cor);
	}
}


public void AniSprIMP_56(AniSpr[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
		switch (s[i].Bank)
		{
		case 0:
		PutSprite(SprGuardianImg,  s[i].CoorX, s[i].CoorY,  s[i].FrameIni + s[i].FrameAct, SprGuardianCor);
		break;	
		}
	}
}



public void AniSprIMP_64(AniSpr[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
	PutSprite(Spr64Img,  s[i].CoorX, s[i].CoorY,  s[i].FrameIni + s[i].FrameAct, Spr64Cor);
	}
}



// -------------------
// Jugar IMP Gfx
// ===================

public void JugarIMP_Gfx()
{

	if (ga.FaseLevel==1)
	{
	scroll_Factor=1;
	scroll_ScrollRUN_Centro_Max(ga.ProtSprX+16, ga.ScrollSuperY>>1);
	} else {
	scroll_Factor=0;
		if (ga.ProtMode==90)
		{
		scroll_ScrollRUN_Centro_Max(ga.ProtSprX+16+ga.ProtMorAddX, ga.ProtSprY+16+ga.ProtMorAddY);
		} else {

			if (ga.GuardianFlag==1)
			{
			scroll_ScrollRUN_Centro_Max((ga.ProtSprX+16)+((ga.GuardianX+28)-(ga.ProtSprX+16))/2  , ga.ProtSprY+16);
			}
			else if (ga.NostFlag==1)
			{
				if (ga.NostMode!=2)
				{
				scroll_ScrollRUN_Centro_Max((ga.ProtSprX+16) , ga.ProtSprY+64-(ScrollSizeY/2) );
				} else {
				scroll_ScrollRUN_Centro_Max((ga.ProtSprX+16) , (ga.ProtSprY+16)+((ga.NostY+110)-(ga.ProtSprY+16))/2 );
				}
			}
			else
			{
			scroll_ScrollRUN_Centro_Max(ga.ProtSprX+16, ga.ProtSprY+16);
			}
		}
	}

	scroll_ScrollIMP(LCD_Gfx);



//	Imprimimos Nubes
// -------------------------
	if (ga.FaseLevel==1)
	{
		for (int i, t=0 ; t<ga.NubeC ; t++)
		{
		i=ga.NubeP[t];
		PutSprite(Spr64Img,  ga.Nubes[i].CoorX+ScrollX, ga.Nubes[i].CoorY+ScrollY,  ga.Nubes[i].FrameIni + ga.Nubes[i].FrameAct, Spr64Cor);
		}
/*
	} else {
		for (int i, t=0 ; t<ga.NubeC ; t++)
		{
		i=ga.NubeP[t];
		PutSprite(Spr64Img,  ga.Nubes[i].CoorX+ScrollX, ga.Nubes[i].CoorY,  ga.Nubes[i].FrameIni + ga.Nubes[i].FrameAct, Spr64Cor);
		}
*/
	}
// =========================


//	Imprimimos Jordis
// -------------------------
/*
	for (int i=0 ; i<ga.JordiC ; i++)
	{
	scroll_ImageSET(ColinasImg, ga.Jordis[i][0], ga.Jordis[i][1], ga.Jordis[i][2], ga.Jordis[i][3], LCD_Gfx, ga.Jordis[i][4], ga.Jordis[i][5]);
	}
*/
// =========================


//	Imprimimos Rocas
// -------------------------
	AniSprIMP_64(ga.Rocas, ga.RocaP, ga.RocaC);
// =========================


//	Imprimimos Guardian
// -------------------------
	for (int i=0 ; i<ga.GuardSprC ; i++)
	{
	PutSprite(SprGuardianImg,  ga.GuardianX, ga.GuardianY+ga.GuardianAddY+ga.GuardSprs[i][1],  ga.GuardSprs[i][0], SprGuardianCor);
	}
// =========================



//	Imprimimos Nostromo
// -------------------------
	if (ga.NostFlag!=0)
	{
//	Imprimimos Nostromo - Armas
// -------------------------
	AniSprIMP_56(ga.NostArmas, ga.NostArmaP, ga.NostArmaC);
// =========================
//	scroll_ImageSET(NostromoImg,   0,0,  330, NostromoImg.getHeight(),  LCD_Gfx,  ga.NostX,     ga.NostY+ga.NostAddY);
//	scroll_ImageSET(NostromoImg, 130,0,  330, NostromoImg.getHeight(),  LCD_Gfx,  ga.NostX+330, ga.NostY+ga.NostAddY);

	ImageSET(NostromoImg,  0,0,  330, 60,  ga.NostX-ScrollX,     ga.NostY+ga.NostAddY-ScrollY,    0);
	ImageSET(NostromoImg,  0,0,  330, 60,  ga.NostX+330-ScrollX, ga.NostY+ga.NostAddY-ScrollY,    1);
	ImageSET(NostromoImg,  0,0,  330, 60,  ga.NostX-ScrollX,     ga.NostY+ga.NostAddY+60-ScrollY, 2);
	ImageSET(NostromoImg,  0,0,  330, 60,  ga.NostX+330-ScrollX, ga.NostY+ga.NostAddY+60-ScrollY, 3);
	}
// =========================


//	Imprimimos Host
// -------------------------
	AniSprIMP_56(ga.Hosts, ga.HostP, ga.HostC);
// =========================


//	Imprimimos AnimSprites
// -------------------------
	for (int i, t=0 ; t<ga.AnimSpriteC ; t++)
	{
	i=ga.AnimSpriteP[t];
		switch (ga.AnimSprites[i].Bank)
		{
		case 0:
		PutSprite(Spr32Img,  ga.AnimSprites[i].CoorX, ga.AnimSprites[i].CoorY,  ga.AnimSprites[i].FrameIni+ga.AnimSprites[i].FrameAct, Spr32Cor);
		break;
		
		case 1:
		PutSprite(Spr16Img,  ga.AnimSprites[i].CoorX, ga.AnimSprites[i].CoorY,  ga.AnimSprites[i].FrameIni+ga.AnimSprites[i].FrameAct, Spr16Cor);
		break;

		case 2:
		PutSprite(Spr64Img,  ga.AnimSprites[i].CoorX, ga.AnimSprites[i].CoorY,  ga.AnimSprites[i].FrameIni+ga.AnimSprites[i].FrameAct, Spr64Cor);
		break;
		}
	}
// =========================



//	Imprimimos Canon (SOLO para Fase 2)
// ------------------------------------
	AniSprIMP(ga.Canons, ga.CanonP, ga.CanonC);
// =========================


//	Imprimimos CanonDisp
// -------------------------
	AniSprIMP(ga.CanonDisps, ga.CanonDispP, ga.CanonDispC);
// =========================

//	Imprimimos ArmaVerde
// -------------------------
	AniSprIMP(ga.ArmaVerdes, ga.ArmaVerdeP, ga.ArmaVerdeC);
// =========================

//	Imprimimos ArmaAzul
// -------------------------
	AniSprIMP(ga.ArmaAzuls, ga.ArmaAzulP, ga.ArmaAzulC);
// =========================

//	Imprimimos ArmaAzul
// -------------------------
	AniSprIMP(ga.ArmaLilas, ga.ArmaLilaP, ga.ArmaLilaC);
// =========================

//	Imprimimos ArmaMisil
// -------------------------
	AniSprIMP(ga.ArmaMisils, ga.ArmaMisilP, ga.ArmaMisilC);
// =========================

//	Imprimimos Gusano
// -------------------------
	AniSprIMP(ga.Gusanos, ga.GusanoP, ga.GusanoC);
// =========================

//	Imprimimos Gerard
// -------------------------
	AniSprIMP(ga.Gerards, ga.GerardP, ga.GerardC);
// =========================

//	Imprimimos Julio
// -------------------------
	AniSprIMP(ga.Julios, ga.JulioP, ga.JulioC);
// =========================

//	Imprimimos Items
// -------------------------
	AniSprIMP(ga.Items, ga.ItemP, ga.ItemC);
// =========================

//	Imprimimos Guardian-Disparo
// -------------------------
	AniSprIMP(ga.GuarDisps, ga.GuarDispP, ga.GuarDispC);
// =========================



//	Imprimimos Disparos Protagonista
// -------------------------
	for (int i, t=0 ; t<ga.DisparoC ; t++)
	{
	i=ga.DisparoP[t];
		if (ga.Disparos[i].Bank==0)
		{
		PutSprite(Spr32Img,  ga.Disparos[i].CoorX, ga.Disparos[i].CoorY,  ga.Disparos[i].FrameIni+ga.Disparos[i].FrameAct, Spr32Cor);
		} else {
		PutSprite(Spr16Img,  ga.Disparos[i].CoorX, ga.Disparos[i].CoorY,  ga.Disparos[i].FrameIni+ga.Disparos[i].FrameAct, Spr16Cor);
		}
	}
// =========================

//	Imprimimos Destellos
// -------------------------
	AniSprIMP(ga.Destellos, ga.DestelloP, ga.DestelloC);
// =========================



//	Imprimimos Energy - Enemigos
// -------------------------
	if (ga.Energy_ON!=0)
	{
	ImageSET(PanelImg, 0,50,  176, ga.EnergyY<8?ga.EnergyY:8,   0,ga.GameSizeY-40 - ga.EnergyY);
	ImageSET(PanelImg, 0,58,  ga.EnergyNow, ga.EnergyY<8?ga.EnergyY:8,  33,ga.GameSizeY-39 - ga.EnergyY);
	}
// =========================


//	Imprimimos Panel
// -------------------------
	if (ga.PanelPaint!=0)
	{
	ga.PanelPaint=0;
	ImageSET(PanelImg, 0,0, 176,40, 0,ga.GameSizeY-40);
	}
// =========================


//	Panel Energia
// -------------------------
	if (ga.PanelLifePaint!=0 && ga.JugarLevel!=0 )
	{
	ga.PanelLifePaint=0;
	int Pos1=(ga.PanelLife/100)%10;
	int Pos2=(ga.PanelLife/10)%10;
	int Pos3= ga.PanelLife%10;
	ImageSET(PanelImg, 1+(Pos1*5),42, 4,7, 36,ga.GameSizeY-40+11);
	ImageSET(PanelImg, 1+(Pos2*5),42, 4,7, 41,ga.GameSizeY-40+11);
	ImageSET(PanelImg, 1+(Pos3*5),42, 4,7, 46,ga.GameSizeY-40+11);

	ImageSET(PanelImg, 1+(ga.JugarVidas*5),42, 4,7, 41,ga.GameSizeY-40+19);	// Vidas
	}
// =========================

//	Panel Items
// -------------------------
	if (ga.PanelItemPaint!=0)
	{
	ga.PanelItemPaint=0;

	if (ga.ProtArmaData[9][0]!=0)	{PutSprite2(Spr16Img,  71,ga.GameSizeY-40+15, 157, Spr16Cor);}
	else							{ImageSET(PanelImg, 71, 15, 16,16, 71,ga.GameSizeY-40+15);}

	if (ga.ProtArmaData[6][0]!=0)	{PutSprite2(Spr16Img,  88,ga.GameSizeY-40+15, 154, Spr16Cor);}
	else							{ImageSET(PanelImg, 88, 15, 16,16, 88,ga.GameSizeY-40+15);}

	if (ga.ProtArmaData[5][0]!=0)	{PutSprite2(Spr16Img, 105,ga.GameSizeY-40+15, 153, Spr16Cor);}
	else							{ImageSET(PanelImg,105, 15, 16,16,105,ga.GameSizeY-40+15);}

	if (ga.ProtArmaData[4][0]!=0)	{PutSprite2(Spr16Img, 122,ga.GameSizeY-40+15, 152, Spr16Cor);}
	else							{ImageSET(PanelImg,122, 15, 16,16,122,ga.GameSizeY-40+15);}

	if (ga.ProtArmaData[3][0]!=0)	{PutSprite2(Spr16Img, 139,ga.GameSizeY-40+15, 151, Spr16Cor);}
	else							{ImageSET(PanelImg,139, 15, 16,16,139,ga.GameSizeY-40+15);}

	if (ga.ProtArmaData[2][0]!=0)	{PutSprite2(Spr16Img, 156,ga.GameSizeY-40+15, 150, Spr16Cor);}
	else							{ImageSET(PanelImg,156, 15, 16,16,156,ga.GameSizeY-40+15);}

	if (ga.BolaArma!=0)
		{
		ImageSET(PanelImg, 90,12, 64,3, 90, ga.GameSizeY-40+12);
		ImageSET(PanelImg, 51,42,  5,3, (94-17)+(ga.BolaArma*17), ga.GameSizeY-40+12);
		}
	}
// =========================


	if (MonitorMode!=0) {MonitorRUN();}

}

// <=- <=- <=- <=- <=-




// ********************
// --------------------
// Intro - Engine - Gfx
// ====================
// *********************

Image IntroImg;
Image NodrizaImg;

// -------------------
// Intro INI Gfx
// ===================

public void IntroINI_Gfx()
{
	IntroImg = loadImage("/Gfx/Intro.png");
	NodrizaImg = loadImage("/Gfx/Nodriza.png");
}

// -------------------
// Intro END Gfx
// ===================

public void IntroEND_Gfx()
{
	System.gc();
	IntroImg = null;
	NodrizaImg = null;
	System.gc();
}

// -------------------
// Intro IMP Gfx
// ===================

public void IntroIMP_Gfx()
{

	ImageSET(IntroImg, 0,0, 128,128, 0,ga.IntroPlanetaY);		// Planeta

	ImageSET(IntroImg, ((ga.IntroFr)*14),189, 14,14, ga.IntroX,ga.IntroY);	// Nave Peque�a

	ImageSET(NodrizaImg, 0,  0, 72, 206, 80,ga.IntroNodrizaY,0);	// Nave Nodriza
	ImageSET(NodrizaImg, 0,  0, 72, 206,152,ga.IntroNodrizaY,1);	// Nave Nodriza Fliped X
	ImageSET(NodrizaImg, 0, 38, 72, 168, 80,ga.IntroNodrizaY+206,2);	// Nave Nodriza Fliped Y
	ImageSET(NodrizaImg, 0, 38, 72, 168,152,ga.IntroNodrizaY+206,3);	// Nave Nodriza Fliped XY
	ImageSET(NodrizaImg, 0,206, 72,  31, 80,ga.IntroNodrizaY+374,0);	// Nave Nodriza
	ImageSET(NodrizaImg, 0,206, 72,  31,152,ga.IntroNodrizaY+374,1);	// Nave Nodriza Fliped X

	if (ga.IntroFr==8) {ImageSET(IntroImg, 0,128, 122,61, 27,32);}	// Logo Aminoid



	switch (ga.IntroMode)
	{
	case 4:
		if (ga.IntroCont<10)
		{
		TextoImp(Texto.PressFire, 0,130, 0x011, 1);
		}
		TextoImp(Texto.Firma, 0,180, 0x010, 1);
	break;
	}


}

// <=- <=- <=- <=- <=-






// ********************
// --------------------
// Final - Engine - Gfx
// ====================
// *********************

int FinalMode;
int FinalCnt;
Image BackImg;

// -------------------
// Final INI Gfx
// ===================

public void FinalINI_Gfx()
{
	IntroImg = loadImage("/Gfx/Intro.png");

	BackImg = Image.createImage(CanvasSizeX, CanvasSizeY);
	Graphics BackGfx = BackImg.getGraphics();
	BackGfx.setClip(0,0, CanvasSizeX,CanvasSizeY);
	BackGfx.setColor(0);
	BackGfx.fillRect(0,0, CanvasSizeX,CanvasSizeY);
	BackGfx.setClip(0,0, 128,128);
	BackGfx.drawImage(IntroImg,  0,0,  Graphics.TOP|Graphics.LEFT);

	PrinterINI(BackImg);
	PrinterSET(Texto.Final);

	CanvasFillRGB=0;
	CanvasFillPaint=1;

	FinalCnt=100;
	FinalMode=0;
}

// -------------------
// Final END Gfx
// ===================

public void FinalEND_Gfx()
{
	System.gc();
	IntroImg=null;
	BackImg=null;
	System.gc();
}

// -------------------
// Final IMP Gfx
// ===================

public void FinalIMP_Gfx()
{
	switch (FinalMode)
	{
	case 0:
		ImageSET(IntroImg, 0,0, 128,128, 0,0);
		FinalMode++;
	break;

	case 1:
		if (PrinterMode!=0) {PrinterRUN();}

		if (PrinterMode==10 && FinalCnt>0 && --FinalCnt==0) {PrinterINI(null); FinalMode++;}

		if (PrinterMode==10 && (ga.KeybE1 & 0x4)==0x4 && (ga.KeybE2 & 0x4)!=0x4 )	// Boton Seleccion
		{
		FinalMode++;
		}
	break;	

	case 2:
		CanvasFill(0);

		if ( (ga.KeybE1 & 0x4)==0x4 && (ga.KeybE2 & 0x4)!=0x4 )	// Boton Seleccion
		{
		FinalMode++; break;
		}

		ImageSET(IntroImg,   0,0, 128,128,  0, 0);
		ImageSET(IntroImg, 128,0,  78,170, 50,16);
	break;
	}
}

// <=- <=- <=- <=- <=-








// ********************
// --------------------
// Menu - Engine - Gfx
// ====================
// *********************

Image MenuImg;
Image FontImg;

// -------------------
// Menu SET Gfx
// ===================

public void MenuINI_Gfx()
{
	FontImg = loadImage("/Gfx/Font.png");
}

// -------------------
// Menu END Gfx
// ===================

public void MenuEND_Gfx()
{
	System.gc();
	MenuImg = null;
	FontImg = null;
	System.gc();
}

// -------------------
// Menu IMP Gfx
// ===================

int MenuPaintBit=0;
boolean forceMenuUpdate = false;

public void MenuIMP_Gfx()
{
	switch (ga.MenuMode)
	{
	case 0:
	case 5:
		MenuPaintBit=0x03;
	break;

	case 2:
		MenuPaintBit=0x04;
	break;

	case 3:
		MenuPaintBit=0x08;
	break;
	}

	if (forceMenuUpdate)
	{
		forceMenuUpdate = false;
		MenuPaintBit = 0x0F;
	}

	if ((MenuPaintBit & 0x01) != 0) {ImageSET(MenuImg, 0,0, 176,208, 0,0);}	// Fondo

	if ((MenuPaintBit & 0x06) != 0)
		{
		ImageSET(MenuImg,   0,209, 23,41,  6,156);					// Pieza Izquierda
		ImageSET(MenuImg,  23,212, ga.MenuPiezaDesp,35, 29,159);	// Pieza Central
		ImageSET(MenuImg, 151,209, 24,41, 22+ga.MenuPiezaDesp,156);	// Pieza Derecha
		}

	if ((MenuPaintBit & 0x04) != 0)
		{
		TextoImp(ga.MenuTxtNew, ga.MenuOptAX+2,2, 0x211, 1);
		TextoImp(ga.MenuTxtOld, ga.MenuOptBX+2,2, 0x211, 1);
		}

	if ((MenuPaintBit & 0x08) != 0)
		{
			if (ga.MenuOpt.length >1 )
			{
			ImageSET(MenuImg,   0,209, 23,41,  6,156);					// Pieza Izquierda
			ImageSET(MenuImg,  23,212, 10,27, 29,159);	// Pieza Central
			ImageSET(MenuImg,  23+112,212, 16,27, 23+112,159);	// Pieza Central

			TextoImp("<",   6-ga.MenuAspaDesp,2, 0x201, 1);
			TextoImp(">", 112+ga.MenuAspaDesp,2, 0x201, 1);
			}
		}
	MenuPaintBit=0;


	if (PrinterMode!=0 && ga.MenuMode!=0) {PrinterRUN();}

}




// -------------------
// Monitor - Engine
// ===================

int MonitorMode;
int MonitorCont;
int MonitorLine;
int MonitorBaseY;
String[] MonitorTexto;

// -------------------
// Monitor INI
// ===================

public void MonitorINI()
{
	MonitorMode=0;
}

// -------------------
// Monitor SET
// ===================

public void MonitorSET(String[] Texto)
{
	MonitorTexto=Texto;
	MonitorCont=1;
	MonitorLine=0;

	MonitorBaseY=(ga.Energy_ON==0)?ga.GameSizeY-60:ga.GameSizeY-70;

	MonitorMode=1;
}

// -------------------
// Monitor RUN
// ===================

public void MonitorRUN()
{
	switch (MonitorMode)
	{
	case 1:
		int CoorY=MonitorBaseY;
		for (int i=0 ; i<MonitorLine ; i++)
		{
		CoorY+=TextoImp(MonitorTexto[i], 3, CoorY , 0x000, 1);
		}

		TextoImp(MonitorTexto[MonitorLine].substring(0,MonitorCont), 3, CoorY, 0x000, PrinterCnt);

		if (PrinterCnt==0 && MonitorCont++ == MonitorTexto[MonitorLine].length())
		{
		MonitorCont=1;

			if (++MonitorLine == MonitorTexto.length)
			{
			MonitorMode++;
			break;
			}
		}
		PrinterCnt++;PrinterCnt&=1;
	break;

	case 2:
		CoorY=MonitorBaseY;
		for (int i=0 ; i<MonitorTexto.length ; i++)
		{
		CoorY+=TextoImp(MonitorTexto[i], 3, CoorY , 0x000, 1);
		}

		if (++PrinterCnt>60) {MonitorMode=0;}
	break;
	}
}





// -------------------
// Printer - Engine
// ===================

int PrinterMode;
int PrinterCont;
int PrinterLine;
int PrinterBaseY;
int PrinterCnt;
String[] PrinterTexto;
Image PrinterImg;

// -------------------
// Printer INI
// ===================

public void PrinterINI(Image Img)
{
	PrinterImg=Img;
	PrinterMode=0;
}

// -------------------
// Printer SET
// ===================

public void PrinterSET(String[] Texto)
{
	PrinterTexto=Texto;
	PrinterCont=1;
	PrinterLine=0;

	PrinterBaseY=30;
	PrinterCnt=0;

	PrinterMode=1;
}

// -------------------
// Printer RUN
// ===================

public void PrinterRUN()
{
	switch (PrinterMode)
	{
	case 1:
		ImageSET(PrinterImg, 4,17, 168,136, 0,0, 4,17, 168, 136);	// Fondo
		PrinterMode++;
	break;

	case 2:
		int CoorY=PrinterBaseY;
		for (int i=0 ; i<PrinterLine ; i++)
		{
		CoorY+=TextoImp(PrinterTexto[i], 2, -64 , 0x100, 1);
		}

		ImageSET(PrinterImg, CursorX+4,CursorY+17, 8,16, CursorX,CursorY, 4,17, 168, 136);

		TextoImp(PrinterTexto[PrinterLine].substring(0,PrinterCont), 2, CoorY, 0x100, PrinterCnt);

		if (PrinterCnt==1 && PrinterCont++ == PrinterTexto[PrinterLine].length())
		{
		PrinterCont=1;

			if (++PrinterLine == PrinterTexto.length)
			{
			if (ga.MenuType==4)
			{
			PrinterCnt=99*3;
			PrinterMode=11;
			} else if (ga.MenuType==5)
			{
			PrinterCnt=80;
			PrinterMode=12;		// Game Over
			} else {
			PrinterMode=10;
			}
			break;
			}

			if (CoorY > 120-20)
			{
			PrinterCont=4;
			PrinterMode++;
			}
		}
		PrinterCnt++;PrinterCnt&=1;
	break;

	case 3:
		ImageSET(PrinterImg, 4,17, 168,136, 0,0, 4,17, 168, 136);	// Fondo

		PrinterBaseY-=5;
		CoorY=PrinterBaseY;
		for (int i=0 ; i<PrinterLine ; i++)
		{
		CoorY+=TextoImp(PrinterTexto[i], 2,CoorY , 0x100, 1);
		}
		if (--PrinterCont < 1) {PrinterMode=2;}
	break;

	case 10:
		if (PrinterCnt<10)
		{
		ImageSET(PrinterImg, CursorX+4,CursorY+17, 8,16, CursorX,CursorY, 4,17, 168, 136);
		} else {
		ImageSET(FontImg, 13*8,0,  8,CursorMode, CursorX,CursorY, 4,17, 168, 136);
		}
		if (++PrinterCnt>20) {PrinterCnt=0;}
	break;

	case 11:
		ImageSET(PrinterImg, CursorX+4,CursorY+17, 8,16, CursorX,CursorY, 4,17, 168, 136);
		TextoImp(Integer.toString(PrinterCnt/30), CursorX, CursorY, 0x101, 1);

		if (--PrinterCnt < 1) {PrinterMode=0;}
	break;

	case 12:
		if (--PrinterCnt < 1) {PrinterMode=0;}
	break;

	}
}


// -------------------
// Texto IMP Gfx
// ===================

int CursorX;
int CursorY;
int CursorMode;

int FontSourY;
int[] FontSizeX;
int[] FontSiz1X = new int[] {8,5,8,8,8,8,8,8,8,8,  8,3,8,8,8,8,8,  8,8,8,8,8,8,8,8,2,5,8,6,8,8,8,8,8,8,8,8,8,8,8,8,8,8, 4,5};
int[] FontSiz2X = new int[] {7,3,7,7,7,7,7,6,7,7,  7,3,3,7,3,7,8,  7,7,7,7,7,7,7,7,1,6,7,5,7,7,7,7,7,7,7,7,7,7,7,7,7,7, 3,4};

public int TextoImp(String TextoStr, int DestX, int DestY, int Modo, int Cur)
{
	if (Cur==0) {TextoStr = TextoStr.concat("=");}
 
	byte[] Texto = TextoStr.getBytes();

	if (Texto.length>0)
	{
	int ini=0;
		switch (Texto[0])
		{
		case  97: Modo&=0xF00;ini++;break;				// "a"
		case  98: Modo&=0xF0F;Modo|=0x00F;ini++;break;	// "b"
		case  99: Modo&=0xF00;Modo|=0x010;ini++;break;	// "c"
		case 100: Modo|=0x01F;ini++;break;				// "d"
		}
	if (ini!=0) {Texto = TextoStr.substring(1).getBytes();}
	}

	if (DestY < -15) {return (Modo & 0x00F)==0?10:20;}

	if ((Modo & 0x00F)==0)
	{
	FontSourY=16;
	FontSizeX=FontSiz2X;
	} else {
	FontSourY=0;
	FontSizeX=FontSiz1X;
	}

	int AddX=0;
	if ((Modo & 0x0F0)==0x10)
	{
	for (int i=0 ; i<Texto.length ; i++) {int c=Texto[i]-0x30; if (c>=0 && c<FontSizeX.length) {AddX+=FontSizeX[c]+1;} else {AddX+=4;} }
	DestX+=((Modo & 0xF00)==0) ? (ga.GameSizeX-AddX)/2 : (124-AddX)/2 ;
	}

	for (int i=0 ; i<Texto.length ; i++)
	{
	int c=Texto[i]-0x30;
	if (c<0) {DestX+=4; continue;}

	if (c<0 || c >= FontSizeX.length) {continue;}

	if (c==13 && (Modo & 0xF00)==0x100) {CursorX=DestX; CursorY=DestY; CursorMode=(Modo & 0x00F)==0?8:16;
//	SoundSET(9,1);
	}

	if ((Modo & 0xF00)==0)
	{
	ImageSET(FontImg, c*8, FontSourY, FontSizeX[c], 16, DestX,DestY);
	} else if ((Modo & 0xF00)==0x100) {
	ImageSET(FontImg, c*8, FontSourY, FontSizeX[c], 16, DestX,DestY,   4,17, 168, 136);
	} else {
	ImageSET(FontImg, c*8, FontSourY, FontSizeX[c], 16, DestX,DestY,  25,167, ga.MenuPiezaDesp, 20);
	}

	DestX+=FontSizeX[c]+1;
	}

	return (Modo & 0x00F)==0?10:20;
}


// <=- <=- <=- <=- <=-








// *******************
// -------------------
// Scroll - Engine
// ===================
// *******************

// Scroll Class v1.3 - Rev.6 (2.3.2003)

int scroll_Factor=0;

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

Image scroll_TilesImg;
int TilesLineX;

int ScrollX;
int ScrollY;
int ScrollSizeX;
int ScrollSizeY;


// -------------------
// Scroll INI
// ===================

public void scroll_ScrollINI(int SizeX, int SizeY)
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

public void scroll_ScrollSET(byte[] Mapa, int SizeX, int SizeY,  Image Img, int LineX)
{
	ScrollX=0;
	ScrollY=0;

	FaseMap=Mapa;
	FaseSizeX=SizeX;
	FaseSizeY=SizeY;

	scroll_TilesImg=Img;
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

public void scroll_ScrollRUN_Centro_Max(int X, int Y)
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
	FaseY=(ScrollY>>(3+scroll_Factor));

	FondoX=FaseX%FondoSizeX;
	FondoY=FaseY%FondoSizeY;

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
							FondoGfx.setClip(x*8 ,y*8,  8,8);

//				FondoGfx.setColor(255, 171-((((80*256)/32)*(CorY))/256) ,0);
							FondoGfx.setColor(255, CorY<22 ? (31-CorY)*8 : (31-22)*8 ,0);
							FondoGfx.fillRect(x*8,y*8,8,8);

							FondoGfx.drawImage(scroll_TilesImg, (x*8)-((TileNum%TilesLineX)*8), (y*8)-((TileNum/TilesLineX)*8), Graphics.TOP|Graphics.LEFT);
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

public void scroll_ScrollIMP(Graphics Gfx)
{
	int x=ScrollX%8;
	int y=(ScrollY>>scroll_Factor)%8;

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
		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect( 0,  0, sx, ScrollSizeY);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect(sx,  0, px, ScrollSizeY);
		Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);
		}

	} else {

		if (FondoX==0)
		{
		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect( 0,  0, ScrollSizeX, sy);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect( 0, sy, ScrollSizeX, py);
		Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);

		} else {

		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect( 0,  0, sx, sy);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);

		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect(sx,  0, px, sy);
		Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);


		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect( 0, sy, sx, py);
		Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);

		Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect(sx, sy, px, py);
		Gfx.drawImage(FondoImg, sx, sy  , Graphics.TOP|Graphics.LEFT);
		}
	}

}


// -------------------
// Scroll END
// ===================

public void scroll_ScrollEND()
{
	FaseMap=null;
	FondoMap=null;
	scroll_TilesImg=null;
	FondoGfx=null; FondoImg=null;
}


// -------------------
// Image SET
// ===================

public void scroll_ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, Graphics Gfx, int DestX, int DestY)
{
	if ((DestX       > ScrollX+ScrollSizeX)
	||	(DestX+SizeX < ScrollX)
	||	(DestY       > ScrollY+ScrollSizeY)
	||	(DestY+SizeY < ScrollY))
	{return;}

	Gfx.setClip(0,0, ScrollSizeX, ScrollSizeY);
	Gfx.clipRect(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	
//	Gfx.setClip(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX-ScrollX, DestY-SourY-ScrollY, Graphics.TOP|Graphics.LEFT);
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
// Version ESPECIAL: Motorola V300 - Rev.1 (6.5.2004)
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
	Sonidos = new Player[9];

	Sonidos[0] = SoundCargar("/aminoid_intro.mid");
	Sonidos[1] = SoundCargar("/aminoid_ship_dead.mid");
	Sonidos[2] = SoundCargar("/aminoid_item.mid");
	Sonidos[3] = SoundCargar("/aminoid_prota_dead.mid");
	Sonidos[4] = SoundCargar("/aminoid_enemy_dead.mid");
	Sonidos[5] = SoundCargar("/aminoid_rehen.mid");
	Sonidos[6] = SoundCargar("/aminoid_teleport.mid");
	Sonidos[7] = SoundCargar("/aminoid_menu_select.mid");
	Sonidos[8] = SoundCargar("/aminoid_menu_move.mid");
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
}

// -------------------
// Sound SET
// ===================

public void SoundSET(int Ary, int Loop)
{
	if (Ary == 7) {Ary = 8;}		// Chapucilla para V300

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