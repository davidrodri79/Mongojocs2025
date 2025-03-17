package com.mygdx.mongojocs.ninjarun;


// ------------------------------------------------------
// Ninja Run Canvas v1.1 Rev.1 (6.02.2004) - Canvas
// ======================================================
// Base
// ------------------------------------




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

public class ITCanvas01 extends FullCanvas
{

ITGame01 ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public ITCanvas01(ITGame01 ga)
{
	this.ga = ga;
	CanvasINI();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics LCD_Gfx;
DirectGraphics LCD_dGfx;


boolean DeviceSound=true,DeviceVibra=true;

long TimeNow, TimeOld;
int TimeDat, TimeCnt;

public void paint (Graphics g)
{       
	if (CanvasPaint)
	{
	CanvasPaint=false;

	if (LCD_Gfx==null) {LCD_Gfx=g; LCD_dGfx= DirectUtils.getDirectGraphics(g);}

	try {

	CanvasRUN();

	} catch (Exception e) {e.printStackTrace();e.toString();}

	TimeNow=System.currentTimeMillis(); TimeDat=(int)(TimeNow-TimeOld); TimeOld=TimeNow;
	if (PaintDebug)
	{
		g.setClip( 0,0, CanvasSizeX, CanvasSizeY ); g.setColor(-1); g.fillRect(0,0,  CanvasSizeX,12 ); g.setColor(0);
		g.drawString(Integer.toString((int)(ga.CPU_Milis-ga.GameMillis)),    0,0, Graphics.LEFT|Graphics.TOP);
		g.drawString(Integer.toString((int)(TimeNow-ga.CPU_Milis)), 40,0, Graphics.LEFT|Graphics.TOP);
		g.drawString(Integer.toString(TimeDat), 80,0, Graphics.LEFT|Graphics.TOP);
	}

//	System.gc(); g.drawString(Integer.toString((int)(Runtime.getRuntime().freeMemory())), 0,0, Graphics.LEFT|Graphics.TOP);

	}

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
		case 1:KeybY=1;break;	// Arriba
		case 6:KeybY=-1;break;	// Abajo
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
		KeybB=1; KeybX=-1; KeybY=1;
	break;

	case Canvas.KEY_NUM2:	// Arriba
		KeybB=2; KeybY=1;
	break;

	case Canvas.KEY_NUM3:
		KeybB=3; KeybX= 1; KeybY=1;
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
		KeybB=7; KeybX=-1; KeybY= -1;
	break;

	case Canvas.KEY_NUM8:	// Abajo
		KeybB=8; KeybY=-1;
	break;

	case Canvas.KEY_NUM9:
		KeybB=9; KeybX= 1; KeybY= -1;
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

// -----------------------------------------
	case -1:	// Siemens - Menu Izquierda
		KeybM=-1;
	break;

	case -4:	// Siemens - Menu Derecha
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
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para paudar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void showNotify() {ga.GamePaused=false;}

public void hideNotify() 
{
	ga.GamePaused=true;
	if (ga.GameStatus==50) ga.ITU.PanelSET(1);
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
	if (DestX >= CanvasSizeX || DestY >= CanvasSizeY) {return;}

//	LCD_Gfx.setClip(0, 0, CanvasSizeX, CanvasSizeY);
//	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);

	LCD_Gfx.setClip(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void ImageSET(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	LCD_Gfx.setClip(TopX, TopY, FinX, FinY);
	LCD_Gfx.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

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

	if (ga.FaseMap!=null && ga.FaseMap.sc != null) ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-ga.FaseMap.sc.ScrollX,Y+DestY-ga.FaseMap.sc.ScrollY, Flip);
	else
	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY, Flip);
}

// ----------------------------------------------------------

public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       > CanvasSizeX)
	||	(DestX+SizeX < 0)
	||	(DestY       > CanvasSizeY)
	||	(DestY+SizeY < 0))
	{return;}

	LCD_Gfx.setClip(0, 0, CanvasSizeX, CanvasSizeY);
	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);

	switch (Flip)
	{
	case 0:
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
	LCD_dGfx.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
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

// -------------------
// Canvas Img[ActI] SET
// ===================

public void CanvasImageSET(String FileName, int RGB)
{
	CanvasFillRGB=RGB; CanvasFillPaint=true;
	CanvasImg = ga.ITU.LoadImage(FileName);
}


// -------------------
// Canvas Img[ActI] PUT
// ===================

public void CanvasImagePUT(String FileName, int RGB)
{
	CanvasFill(RGB);

	CanvasImg = ga.ITU.LoadImage(FileName);

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

int CanvasSizeX=getWidth();
int CanvasSizeY=getHeight();

boolean CanvasFillPaint=false;
int CanvasFillRGB;
Image CanvasImg;
Image[] BackGround = new Image[8];
Image TileSet;
byte ActBG=0,ActI=0;

boolean CanvasPaint=false;
boolean PaintDebug=false;

// -------------------
// Canvas INI
// ===================

public void CanvasINI()
{
	CanvasFillRGB=0;
	CanvasFillPaint=true;
}

// -------------------
// Canvas SET
// ===================

public void CanvasSET()
{
	SoundINI();
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
//	if (ga.MenuPaint) { ga.MenuPaint=false; MenuIMP_Gfx(); }

//	if (ga.JugarPaint) { ga.JugarPaint=false; JugarIMP_Gfx(); }
	Font f;
	switch (ga.GameStatus)
	{
		case 1 :
			LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
			LCD_Gfx.setColor(255,255,255);
			LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
			ImageSET(BackGround[ActBG]);
			
		break;
		case 2 :
			LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
			LCD_Gfx.setColor(0,0,0);
			LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
			ImageSET(BackGround[ActBG]);
		break;
		case 4 :
			LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
			LCD_Gfx.setColor(0,0,0);
			LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
			
			LCD_Gfx.setColor(255,255,255);
			f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
			LCD_Gfx.setFont(f);
			LCD_Gfx.drawString(ga.Textos[14][0], 24+5, 38+15,  20);
			
			f= Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
			LCD_Gfx.setFont(f);
			LCD_Gfx.drawString(ga.Textos[25][ga.PlayerSelected], 24+37, 38+67,  20);
			LCD_Gfx.drawString(ga.Textos[26][ga.PlayerSelected], 24+37, 38+78,  20);
			LCD_Gfx.drawString(ga.Textos[27][ga.PlayerSelected], 24+37, 38+89,  20);
			
			ga.gc.PutSprite(ga.Img[ActI],  24+4, 38+32,  3, ga.Cord[ActI]);
			ga.gc.PutSprite(ga.Img[ActI],  24+34, 38+32,  4, ga.Cord[ActI]);
			ga.gc.PutSprite(ga.Img[ActI],  24+64, 38+32,  5, ga.Cord[ActI]);
			ga.gc.PutSprite(ga.Img[ActI],  24+94, 38+32,  6, ga.Cord[ActI]);
			ga.gc.PutSprite(ga.Img[ActI],  24+4, 38+65,  7, ga.Cord[ActI]);
			ga.ITU.AniSprs[ga.SelectorID].CoorX = (24+4+30*ga.PlayerSelected);
			ga.ITU.AniSprs[ga.ActualSelectedID].FrameIni = (8+2*ga.PlayerSelected);
			ga.ITU.AniSprRUN();
			AniSprIMP(ga.ITU.AniSprs, ga.ITU.AniSprP, ga.ITU.AniSprC);
		break;
		case 5 :
			if (ga.TimeToChange > 0)
			{
				LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY);
				LCD_Gfx.setColor(0,0,0);
				LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY);
				
				LCD_Gfx.setColor(255,255,255);
				f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
				LCD_Gfx.setFont(f);
				LCD_Gfx.drawString(ga.Textos[14][0], 24+5, 38+15,  20);
				
				f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
				LCD_Gfx.setFont(f);
				LCD_Gfx.drawString(ga.Textos[25][ga.PlayerSelected], 24+37, 38+67,  20);
				LCD_Gfx.drawString(ga.Textos[26][ga.PlayerSelected], 24+37, 38+78,  20);
				LCD_Gfx.drawString(ga.Textos[27][ga.PlayerSelected], 24+37, 38+89,  20);
				
				ga.gc.PutSprite(ga.Img[ActI],  24+4, 38+32,  3, ga.Cord[ActI]);
				ga.gc.PutSprite(ga.Img[ActI],  24+34, 38+32,  4, ga.Cord[ActI]);
				ga.gc.PutSprite(ga.Img[ActI],  24+64, 38+32,  5, ga.Cord[ActI]);
				ga.gc.PutSprite(ga.Img[ActI],  24+94, 38+32,  6, ga.Cord[ActI]);
				ga.gc.PutSprite(ga.Img[ActI],  24+4, 38+65,  7, ga.Cord[ActI]);
				
				ga.ITU.AniSprs[ga.SelectorID].Speed = 1;
				ga.ITU.AniSprs[ga.SelectorID].Bank = (ga.ITU.AniSprs[ga.SelectorID].Bank==0) ? 1 : 0;
				
				ga.ITU.AniSprRUN();
				AniSprIMP(ga.ITU.AniSprs, ga.ITU.AniSprP, ga.ITU.AniSprC);
			}
		break;
		case 6:
			LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
			LCD_Gfx.setColor(0,0,0);
			LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
			
			LCD_Gfx.setColor(255,255,255);
			f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
			LCD_Gfx.setFont(f);
			LCD_Gfx.drawString(ga.Textos[10][0], 24+17, 38+15,  20);
			
			f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
			LCD_Gfx.setFont(f);
			LCD_Gfx.drawString(ga.Textos[15][0]+(ga.ActualLevel+1), 24+17, 38+67,  20);
			
			ImageSET(BackGround[ActBG],0,0,51,32,24+17,38+35);
			ImageSET(BackGround[ActBG],51+28*ga.PlayerSelected,0,28,28,24+17,38+83);
		break;
		case 50 :
		if (ga.Move)
		{
			if ((true)||(ga.Paint==0))
			{
				ga.FaseMap.Paint(LCD_Gfx);
				PaintItems();
				ga.Player.Paint();
				for (int Counter=0; Counter<ga.Enemies.length;Counter++) ga.Enemies[Counter].Paint();
				for (int Counter=0; Counter<ga.Obstacles.length;Counter++) ga.Obstacles[Counter].Paint();
				ga.ITU.AniSprRUN();
				OrdenarSprites();
				AniSprIMP(ga.ITU.AniSprs, ga.ITU.AniSprP, ga.ITU.AniSprC);
				PaintScores();
			} else
			{
				ga.Player.Refresh();	
				for (int Counter=0; Counter<ga.Enemies.length;Counter++) ga.Enemies[Counter].Refresh();
				for (int Counter=0; Counter<ga.Obstacles.length;Counter++) ga.Obstacles[Counter].Refresh();
			}
		}
		case 75 :
			PaintText();
			//LCD_Gfx.setClip(0,0,CanvasSizeX,CanvasSizeY);
			//LCD_Gfx.drawString(Integer.toString((int)(ga.Player.X))+","+Integer.toString((int)(ga.Player.Y)),    0,0, Graphics.LEFT|Graphics.TOP);
		break;
		case 85 :
			LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
			LCD_Gfx.setColor(0,0,0);
			LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
			
			ImageSET(BackGround[ActBG]);
			
		break;
		case 100 :
			LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
			LCD_Gfx.setColor(0,0,0);
			LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
			ImageSET(BackGround[ActBG]);
		break;
		case 900 :
			if (ga.ITU.ma.BackGround == false) 
			{
				LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
				LCD_Gfx.setColor(0,0,0);
				LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
			
				ImageSET(BackGround[ActBG]);	
			}
		break;
	}
	
	SoundRUN();
		
	ga.ITU.ma.MarcoIMP(LCD_Gfx);
			
	ga.Move = false;
	
	//ImageSET(ga.Player.Img[ActI], (ActualFrame/2)*24,Direction*24,24,24,  ga.x-sc.ScrollX-12, ga.y-sc.ScrollY-24+(CanvasSizeY/2)-5);
}

public void AniSprIMP(AnimSprite[] s, int[] P, int C)
{
	for (int i, t=0 ; t<C ; t++)
	{
	i=P[t];
		switch (s[i].Bank)
		{
		case 0:
			ga.gc.PutSprite(ga.Img[ActI],  s[i].CoorX, s[i].CoorY,  s[i].FrameIni + s[i].FrameAct + s[i].FrameBase, ga.Cord[ActI]);
		break;	
		}
	}
}

public void PaintScores()
{
	ImageSET(ga.Scores, 25, 0, 25, 208, CanvasSizeX-25, 0);
	ImageSET(ga.Scores, 0, 64+(((100-ga.Player.Values[2])*101)/100), 25 , (((ga.Player.Values[2])*101)/100)+1, CanvasSizeX-25, 102+(((100-ga.Player.Values[2])*101)/100));
	ImageSET(ga.Scores, 0, ga.Player.Values[6]*16, 25, 16, CanvasSizeX-25, 19+(19*ga.Player.Values[16]));
	for (int Counter=0; Counter<ga.Enemies.length; Counter++)
	  ImageSET(ga.Scores, 0, ga.Enemies[Counter].Values[6]*16, 25, 16, CanvasSizeX-25, 19+(19*ga.Enemies[Counter].Values[16]));
	
	LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
	LCD_Gfx.setColor(180,180,180);
	LCD_Gfx.drawLine(5,14,5,114);
	LCD_Gfx.setColor(255,255,255);
	LCD_Gfx.drawLine(6,14,6,114);
	LCD_Gfx.setColor(180,180,180);
	LCD_Gfx.drawLine(7,14,7,114);
	
	ImageSET(ga.Scores, 0, 165, 7, 7, 3, 13);
	ImageSET(ga.Scores, 0, 165, 7, 7, 3, 113);
	
	for (int Counter=0; Counter<ga.Enemies.length; Counter++)
	  ImageSET(ga.Scores, 0, 172+(7*ga.Enemies[Counter].Values[6]), 7, 7, 3, 14+(100*(ga.FaseMap.MH-ga.Enemies[Counter].Y)/ga.FaseMap.MH));
	
	ImageSET(ga.Scores, 0, 172+(7*ga.Player.Values[6]), 7, 7, 3, 14+(100*(ga.FaseMap.MH-ga.Player.Y)/ga.FaseMap.MH));
}

public void PaintItems()
{
	int Act;
	for (int Counter=0; Counter<ga.Items.length; Counter++)
	{
		if (ga.Items[Counter][0]>0) 
		{
			switch(ga.Items[Counter][0])
			{
				case 1 : Act = 49;
				break;
				case 2 : Act = 77;
				break;
				default: Act = 88;
				break;
			}
			ga.gc.PutSprite(ga.Img[ActI],ga.Items[Counter][1],ga.Items[Counter][2],Act, ga.Cord[ActI]);
		}
	}
}

String CanvasText;
int TimeToPaintText,TextStyle;

public void PaintText()
{
	Font f;
	if (TimeToPaintText>0)
	{
		TimeToPaintText--;
		LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY);
		f=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		LCD_Gfx.setFont(f);
		int TextSize = LCD_Gfx.getFont().stringWidth(CanvasText);
		
		LCD_Gfx.setColor(100,100,100);
		LCD_Gfx.drawString(CanvasText, 1+((CanvasSizeX/2)-(TextSize/2)), 1+((CanvasSizeY/2)-10), 20);
		
		if (TextStyle==0);
		{
			LCD_Gfx.setColor(255,255,255);
		}
		if (TextStyle==1)
		{
			LCD_Gfx.setColor(255,((255/4)*(TimeToPaintText%5)),100);
		}
		LCD_Gfx.drawString(CanvasText, ((CanvasSizeX/2)-(TextSize/2)), ((CanvasSizeY/2)-10), 20);
	}
}

public void OrdenarSprites()
{
	ga.ITU.AniSprOrdenar();
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

	Sonidos[0] = SoundCargar("/Mid0.mid");
	Sonidos[1] = SoundCargar("/Mid1.mid");
	Sonidos[2] = SoundCargar("/Mid2.mid");
	Sonidos[3] = SoundCargar("/Mid3.mid");
	Sonidos[4] = SoundCargar("/Mid4.mid");
	Sonidos[5] = SoundCargar("/Mid5.mid");
	Sonidos[6] = SoundCargar("/Mid6.mid");
}

public Player SoundCargar(String Nombre)
{
	Player p = null;

	try
	{
		//InputStream is = getClass().getResourceAsStream(Nombre);
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

	if (ga.GameSound==0) return;
	try
	{
		VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
		Sonidos[Ary].setLoopCount(Loop);
		Sonidos[Ary].start();
	}
	catch(Exception exception) {exception.printStackTrace();}

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


// **************************************************************************//
} // Cierra la Clase ITCanvas01
// **************************************************************************//