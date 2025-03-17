	

// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.3 (1.4.2004)
// ===============================================
// Representacion Grafica
// ------------------------------------


//#ifdef PACKAGE
package com.mygdx.mongojocs.qblast20;
//#endif

//#define NOCHEATT

//#ifdef J2ME


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
//import com.mygdx.mongojocs.midletemu.Runnable;
//import com.mygdx.mongojocs.midletemu.Thread;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Image;

import java.io.*;
import java.util.Date;
import java.util.Random;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.sanfermines2006.Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME

	//#ifdef FULLSCREEN
		//#ifdef MO-C450
		//#elifdef MIDP20
		//#elifdef NOKIAUI
			public class GameCanvas extends FullCanvas
		//#else
		//#endif
	//#else
	//#endif	
	
//#elifdef DOJA
//#endif

//#ifdef J2ME
 implements Runnable, CommandListener, Constant
//#endif
{
	
static Game ga;

//#ifdef J2ME
byte tileCoo[][], shadowCoo[], ballCoo[], lineCoo[], gadgetCoo[], spikesCoo[];
Image tilesImg[], ballImg, shadowImg, lineImg, gadgetImg, spikesImg, pictureImg, buttonImg, timerFnt;
//#ifdef IMAGEBAR
Image barsImg;
//#endif
//#ifdef SCREENFRAME
//#endif
//#endif

//#ifdef DOJA
//#endif

static int loadPercent;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga) 
{
	this.ga = ga;

	 {
			//#ifdef FORCEFULLSCREEN
			//#endif

			//#ifdef J2ME
			menuListFont = Font.getFont( Font.FACE_PROPORTIONAL ,

					//#ifdef BOLDMENUFONT
					//#else
					Font.STYLE_PLAIN ,
					//#endif
					//#ifdef LARGEMENUFONT
					//#else
					Font.SIZE_SMALL );
			//#endif
			//#elifdef DOJA
			//#endif
			fontHeight = menuListFont.getHeight();

			//#ifdef SG-Z105
			//#endif

			//#ifdef J2ME
			buttonImg = new Image();
			buttonImg._createImage("/menu.png");

			//#ifdef NOPHONEFONTATBAR
			//timerFnt = new Image();
			//timerFnt._createImage("/numbers.png");
			//#endif
			//#endif

			frustum = new int[9];

			//#ifdef J2ME
			thread=new Thread(this); System.gc();
			thread.start();
			//#endif
		}
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics scr;

public void paint (Graphics g)
{       
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	long millis = System.currentTimeMillis();
	//#endif
	
	
	synchronized (this)
	{
		if (canvasShow)
		{
			canvasShow=false;
		
			scr = g;
		
		//#ifdef DOJA
		//#endif
		
			try {
				canvasDraw();
			} catch (Exception e) {
				
				//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
				System.out.println("*** Exception en la parte Grafica ***"); e.printStackTrace();
				//#endif
			}
			
		
		//#ifdef DOJA
		//#endif
		
		//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
 		if (Debug.enabled) {Debug.debugDraw(this, scr);}
		//#endif
		
			scr = null;
		}
	}
	
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	//com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug.println("Repaint:"+(System.currentTimeMillis() - millis));
	//#endif
		
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw()
{

	canvasShow = true;
	repaint();

//#ifdef J2ME
	serviceRepaints();
//#elifdef DOJA
//#endif
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int intKeyMenu, intKeyX, intKeyY, intKeyMisc, intKeyCode, intKeyR;

//#ifdef J2ME

public void keyPressed(int keycode)
{
	intKeyMenu = 0; intKeyMisc = 0; intKeyR = 0;
	
	switch (keycode)
	{
	//#ifndef NK-3650
	case Canvas.KEY_NUM0:
		intKeyMisc=10; intKeyR=1;
		//intKeyMenu= 1;
	return;

	case Canvas.KEY_NUM1:
		intKeyMisc=1; intKeyR=-1;
	return;

	case Canvas.KEY_NUM2:	// Arriba
		intKeyMisc=2; intKeyY=-1; intKeyX=0;
	return;

	case Canvas.KEY_NUM3:
		intKeyMisc=3; intKeyR=1;
	return;

	case Canvas.KEY_NUM4:	// Izquierda
		intKeyMisc=4; intKeyX=-1; intKeyY=0;
	return;

	case Canvas.KEY_NUM5:	// Disparo
		intKeyMisc=5;
		intKeyMenu=2;
	return;

	case Canvas.KEY_NUM6:	// Derecha
		intKeyMisc=6; intKeyX=1; intKeyY=0;
	return;

	case Canvas.KEY_NUM7:
		intKeyMisc=7; intKeyR=-1;
	return;

	case Canvas.KEY_NUM8:	// Abajo
		intKeyMisc=8; intKeyY=1; intKeyX=0;
	return;

	case Canvas.KEY_NUM9:
		intKeyMisc=9; intKeyR=1;
	return;

	case 35:		// *
		intKeyMisc=11;
	return;

	case 42:		// #
		intKeyMisc=12;
	return;
	
	//#else
	

	//#endif

// -----------------------------------------

//#ifdef NK
	case -6:	// Nokia - Menu Izquierda
		intKeyMenu = -1;
	return;

	case -7:	// Nokia - Menu Derecha
		intKeyMenu = 1;
	return;

//#elifdef SE
//#elifdef SG-D500
//#elifdef MO-T720
//#elifdef SI
//#elifdef MO-V3xx
//#elifdef MO-V9xx
//#elifdef MO-C65x
//#elifdef AL-OT556
//#elifdef AL-OT756
//#elifdef LG-U8150
//#elifdef S
//#elifdef PA-x500
//#elifdef SD
//#endif
	}
			
	/*switch(getGameAction(keycode))
	{			
		//#ifdef VI-TSM100
		case 1: intKeyY=-1; intKeyX=0; intKeyMisc = 2; break;	// Arriba
		case 6: intKeyY=1; intKeyX=0; intKeyMisc = 8; break;	// Abajo
		case 5: intKeyX=1; intKeyY=0; intKeyMisc = 6; break;	// Derecha
		case 2: intKeyX=-1; intKeyY=0; intKeyMisc = 4; break;	// Izquierda
		case 8: intKeyMenu=2; intKeyMisc = 5; break;	// Fuego
		case 9  : intKeyMenu = -1; intKeyMisc = 1; break;		
		case 10 : intKeyMenu = 1; intKeyMisc = 3; break;		
		case 11 : intKeyR = -1; intKeyMisc = 7; break;		
		case 12 : intKeyR = 1;  intKeyMisc = 9; break;
		//#else
		case 1:intKeyY=-1; intKeyX=0; break;	// Arriba
		case 6:intKeyY=1; intKeyX=0; break;	// Abajo
		case 5:intKeyX=1; intKeyY=0;break;	// Derecha
		case 2:intKeyX=-1; intKeyY=0; break;	// Izquierda
			//#ifndef SI-x55
			case 8:intKeyMenu=2;break;	// Fuego
			//#endif		
		//#endif
	}*/
	
	//System.out.println ("Key Menu:"+intKeyMenu+" Key Misc:"+intKeyMisc);

}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void keyReleased(int keycode)
{
	intKeyMenu = 0; intKeyMisc = 0; intKeyR = 0;
	
	switch (keycode)
	{
	//#ifndef NK-3650

	case Canvas.KEY_NUM2:	// Arriba
		intKeyY=0;
	return;

	case Canvas.KEY_NUM3:
		intKeyX=0; intKeyY=0;
	return;

	case Canvas.KEY_NUM4:	// Izquierda
		intKeyX=0;
	return;

	case Canvas.KEY_NUM5:	// Disparo		
		intKeyMenu=0;
	return;

	case Canvas.KEY_NUM6:	// Derecha
		intKeyX=0;
	return;

	case Canvas.KEY_NUM8:	// Abajo
		intKeyY=0;
	return;
	
	//#else

	//#endif
	}
			
	/*switch(getGameAction(keycode))
	{
		case 1:intKeyY=0;break;	// Arriba
		case 6:intKeyY=0;break;	// Abajo
		case 5:intKeyX=0;break;	// Derecha
		case 2:intKeyX=0;break;	// Izquierda
		//#ifndef SI-x55
		case 8:intKeyMenu=0;break;	// Fuego
		//#endif		
		//#ifdef VI-TSM100
		case 9  : intKeyMenu = 0; break;		
		case 10 : intKeyMenu = 0; break;		
		case 11 : intKeyR = 0; break;		
		case 12 : intKeyR = 0; break;
		//#endif
	}*/
}

//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#ifdef DOJA
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME


public void showNotify()
{	
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("--showNotify()");
	//#endif
	
	gamePaused = false;

	gameForceRefresh = true;
}

public void hideNotify()
{
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("--hideNotify()");
	//#endif
	
	gamePaused = true;
	
	gameSound = false;
	soundStop();
		
}

//#endif


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void showImage(Image Img)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ----------------------------------------------------------

public void showImage(Image Img, int X, int Y)
{
	showImage(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{ 
			
	if (DestX < 0) {SourX-=DestX; SizeX+=DestX; DestX=0;}
	if (DestY < 0) {SourY-=DestY; SizeY+=DestY; DestY=0;}
	
	if (DestY + SizeY >= canvasHeight) SizeY -= DestY + SizeY - canvasHeight;
	if (DestX + SizeX >= canvasWidth) SizeX -= DestX + SizeX - canvasWidth;
	
	if (DestX >= canvasWidth || DestY >= canvasHeight || (DestX + SizeX) <= 0 || (DestY + SizeY) <= 0) {return;}

	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

//

public void showSpriteImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{ 
		
	if (DestX < sprMinX) {SourX-=DestX - sprMinX; SizeX+=DestX - sprMinX; DestX=sprMinX;}
	if (DestY < sprMinY) {SourY-=DestY - sprMinY; SizeY+=DestY - sprMinY; DestY=sprMinY;}
	
	if (DestY + SizeY >= sprMaxY) SizeY -= DestY + SizeY - sprMaxY;
	if (DestX + SizeX >= sprMaxX) SizeX -= DestX + SizeX - sprMaxX;
	
	if (DestX >= sprMaxX || DestY >= sprMaxY || (DestX + SizeX) <= sprMinX || (DestY + SizeY) <= sprMinY) {return;}

	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void showImage(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

// ----------------------------------------------------------

public void showImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
	scr.setClip(TopX, TopY, FinX, FinY);
	scr.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
}

//#elifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// -------------------
// canvas Fill Task
// ===================

public void canvasFillTask(int RGB)
{
	canvasFillRGB = RGB;
	canvasFillShow = true;
}

// -------------------
// canvas Fill Draw
// ===================

public void canvasFillDraw(int RGB)
{
//#ifdef J2ME
	scr.setClip (0,0, canvasWidth,canvasHeight);
	scr.setColor(RGB);
//#elifdef DOJA
//#endif
	scr.fillRect(0,0, canvasWidth,canvasHeight);
}

//#ifdef J2ME
public void spriteCanvasFillDraw(int RGB)
{
	scr.setClip (sprMinX,sprMinY, sprMaxX - sprMinX, sprMaxY - sprMinY);
	scr.setColor(RGB);
	scr.fillRect(0,0, canvasWidth,canvasHeight);
}
//#endif



// -------------------
// load Image
// ===================

//#ifdef J2ME

public Image loadImage(String FileName)
{
	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e) {
		//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		System.out.println("Error leyendo PNG: "+FileName);
		//#endif
	}	

	System.gc();
	return Img;
}

//#elifdef DOJA
//#endif

// ===================





// *********************
// ---------------------
// canvasText - Engine - Gfx
// =====================
// *********************

String canvasTextStr;
int canvasTextX;
int canvasTextY;
int canvasTextRGB;
int canvasTextMode;
boolean canvasTextShow = false;

int printerX = 0;
int printerY = 0;
int printerSizeX = getWidth();
int printerSizeY = getHeight();

// -------------------
// canvasText Create
// ===================

/*public void canvasTextCreate()
{
	printerX = 0;
	printerY = 0;
	printerSizeX = canvasWidth;
	printerSizeY = canvasHeight;
}*/

public void canvasTextCreate(int x, int y, int sizeX, int sizeY)
{
	printerX = x;
	printerY = y;
	printerSizeX = sizeX;
	printerSizeY = sizeY;
}

// -------------------
// canvasText Init
// ===================

public void canvasTextTask(String str, int x, int y, int rgb, int mode)
{
	canvasTextStr = str;
	canvasTextX = x;
	canvasTextY = y;
	canvasTextRGB = rgb;
	canvasTextMode = mode;
	canvasTextShow = true;
}

// -------------------
// canvasText Draw
// ===================

public void canvasTextDraw()
{
	textDraw(canvasTextStr, canvasTextX, canvasTextY, canvasTextRGB, canvasTextMode);
}

// <=- <=- <=- <=- <=-



// *********************
// ---------------------
// text - Engine - Gfx
// =====================
// *********************

// -------------------
// text Draw
// ===================


public void textDraw(String Str, int X, int Y, int RGB, int Mode)
{	
//#ifdef J2ME
	textDraw(textBreak(Str, ((Mode & TEXT_BOXED)!=0 ? printerSizeX - 16 : printerSizeX), Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) ,
			//#ifdef AVOIDSMALLFONT
			//#else
			((Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE) )
			//#endif
			)), X, Y, RGB, Mode);
//#elifdef DOJA
//#endif
}

// -------------------
// text Draw
// ===================

public void setColor(int r, int g, int b)
{
	//#ifdef J2ME
	scr.setColor(r,g,b);
	//#elifdef DOJA
	//#endif
}

public void drawTextBox(int bx, int by, int bw, int bh)
{
	
	//#ifdef J2ME
	scr.setClip(bx,by,bw,bh);
	//#endif
		
	scr.setColor(RGB_BOX);
	scr.fillRect(bx,by,bw,bh);
	
	scr.setColor(RGB_SHADE);
	scr.drawLine(bx+1,by+1,bx+bw-2,by+1);
	scr.drawLine(bx+1,by+1,bx+1,by+bh-2);
	
	scr.setColor(RGB_DARK);
	scr.drawLine(bx+bw-2,by+1,bx+bw-2,by+bh-2);
	scr.drawLine(bx+1,by+bh-2,bx+bw-2,by+bh-2);
}

public void textDraw(String[] Str, int X, int Y, int RGB, int Mode)
{
	int bx, by, bw, bh;
	
//#ifdef J2ME
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) ,			
			//#ifdef AVOIDSMALLFONT
			//#else
			(Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE)
			//#endif
			);
//#elifdef DOJA
//#endif

	scr.setFont(f);

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()*Str.length) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  printerSizeY-(f.getHeight()*Str.length) ;}
	else Y -= ((Str.length - 1)*f.getHeight())/2;

	X += printerX;
	Y += printerY;
	
		
	if ((Mode & TEXT_BOXED)!=0)
	{		
		bx = X - 4;
		by = Y - 4;
		bh = f.getHeight()*Str.length + 8;
		//#ifdef DOJA
		//#endif
		bw = 0;
		
		for(int i = 0; i<Str.length; i++)
		{
			if(f.stringWidth(Str[i]) > bw) {
				bw = f.stringWidth(Str[i]) + 8;
				if((Mode & TEXT_HCENTER)!=0) bx = X - 4 + ( printerSizeX-f.stringWidth(Str[i]) )/2;
				if((Mode & TEXT_RIGHT)!=0) bx = X - 4 + ( printerSizeX-f.stringWidth(Str[i]) );
			}
		}
		
		drawTextBox(bx,by,bw,bh);
				
	}

	for (int i=0 ; i<Str.length ; i++)
	{

	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX +=( printerSizeX-f.stringWidth(Str[i]) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX += printerSizeX-f.stringWidth(Str[i]) ;}


		int despY = i * f.getHeight();

//#ifndef MONOCHROME			
			if ((Mode & TEXT_OUTLINE)!=0)
			{
			scr.setColor(RGB_SHADE);
	//#ifdef J2ME
			scr.drawString(Str[i],  X-1+despX, Y+1+despY , 20);		
	//#elifdef DOJA
	//#endif
			}
//#endif	
	

		scr.setColor(RGB);
//#ifdef J2ME
		scr.drawString(Str[i],  X+despX, Y+despY , 20);
//#elifdef DOJA
//#endif
	}

}


public void textDrawNoCut(String Str, int X, int Y, int RGB, int Mode)
{
	int bx, by, bw, bh;
	
//#ifdef J2ME
	Font f = Font.getFont(Font.FACE_PROPORTIONAL , ((Mode&0x0F0)==0?Font.STYLE_PLAIN:Font.STYLE_BOLD) ,			
			//#ifdef AVOIDSMALLFONT
			//#else
			(Mode&0xF00)==0?Font.SIZE_SMALL : ((Mode&0xF00)==0x100?Font.SIZE_MEDIUM:Font.SIZE_LARGE)
			//#endif
			);
//#elifdef DOJA
//#endif
	scr.setFont(f);

//#ifdef DOJA
//#endif

	if ((Mode & TEXT_VCENTER)!=0) {Y+=( printerSizeY-(f.getHeight()) )/2;}
	else
	if ((Mode & TEXT_BOTTON)!=0) {Y+=  printerSizeY-(f.getHeight()) ;}

	X += printerX;
	Y += printerY;
	
	if ((Mode & TEXT_BOXED)!=0)
	{		
		bx = X - 4;
		by = Y - 4;
		bh = f.getHeight() + 8;
		//#ifdef DOJA
		//#endif
		bw = 0;
		
		if(f.stringWidth(Str) > bw) {
				bw = f.stringWidth(Str) + 8;
				if((Mode & TEXT_HCENTER)!=0) bx = X - 4 + ( printerSizeX-f.stringWidth(Str) )/2;
		}
				
		drawTextBox(bx,by,bw,bh);
				
	}
	
	int despX = 0;

	if ((Mode & TEXT_HCENTER)!=0) {despX +=( printerSizeX-f.stringWidth(Str) )/2;}
	else
	if ((Mode & TEXT_RIGHT)!=0) {despX += printerSizeX-f.stringWidth(Str) ;}


	int despY = 0;

	//#ifndef MONOCHROME			
	if ((Mode & TEXT_OUTLINE)!=0)
	{
		scr.setColor(RGB_SHADE);
		//#ifdef J2ME
		scr.drawString(Str,  X-1+despX, Y+1+despY , 20);		
		//#elifdef DOJA
		//#endif
	}
	//#endif	
	
	scr.setColor(RGB);
	//#ifdef J2ME
	scr.drawString(Str,  X+despX, Y+despY , 20);
	//#elifdef DOJA
	//#endif
	

}


// <=- <=- <=- <=- <=-





// -------------------
// text Draw
// ===================

public String[] textBreak(String texto, int width, Font f)
{
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int[] positions = new int[512];
	int count = 0;

	while ( posOld < texto.length() )
	{
		size = 0;

		pos = posIni;

		while ( size < width )
		{
			if ( pos == texto.length()) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//juan			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat==0x20 ) { posOld = pos - 1; }
//#ifdef J2ME
			size += f._charWidth((char)dat);
//#elifdef DOJA
//#endif
	    }
    
	    if (posOld - posIni < 1)
		{ 
			while ( pos < texto.length() && texto.charAt(pos) >= 0x30 ) {pos++;}
			posOld = pos;
		}

		posIni = posOld + 1;
		positions[count] = posOld;
		count++;
	}

	String str[] = new String[count];
	posIni = 0;
	int posEnd;

	for(int i = 0; i < count; i++)
	{
		posEnd = positions[i];
		str[i] = texto.substring(posIni, posEnd);
		posIni = posEnd + 1;
	}

	return str;
}

// <=- <=- <=- <=- <=-





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

int canvasWidth = getWidth();
int canvasHeight = getHeight();

int sprMinX, sprMinY, sprMaxX, sprMaxY;

int canvasFillRGB;
boolean canvasFillShow = false;
Image canvasImg;

boolean canvasShow = true;
boolean menuBarShow = false;

// -------------------
// canvas Create
// ===================


public void canvasCreate()
{
			
	//#ifdef SCREENFRAME
	//#else
	
	sprMinX = 0; sprMinY = 0; sprMaxX = canvasWidth;
	
		//#ifdef IMAGEBAR
			//#ifdef IMAGEBARBIG
			//#else
			sprMaxY = canvasHeight - 41;
			//#endif		
		//#else
		//#endif
		
	//#endif
					
	canvasTextCreate(0,0,canvasWidth,canvasHeight);
	
	loadPercent = 0; canvasShow = true; gameDraw();
	
	//#ifdef INITIALLOAD
	initialLoadPicture();
	//#endif
	
	increaseLoadPercent(10);
	
	//#ifdef INITIALLOAD
	loadGameGfx();
	//#else
	//#endif
	
	//#ifdef DOJA
	//#endif
	
	
}

// -------------------
// canvas Tick
// ===================

public void canvasDraw()
{
	if (canvasFillShow) { canvasFillShow=false; canvasFillDraw(canvasFillRGB); }

	if (canvasImg!=null) { showImage(canvasImg); canvasImg=null; System.gc(); }

	if (canvasTextShow) { canvasTextShow=false; canvasTextDraw(); }


	playDraw();		// Imprimimos el juego


	if (menuShow) { menuShow=false; menuListDraw(scr); }
}


// <=- <=- <=- <=- <=-




// *******************
// -------------------
// menu - Engine - Gfx
// ===================
// *******************

/*public void menuDraw()
{

	menuListDraw(scr);

}*/

// <=- <=- <=- <=- <=-









// *******************
// -------------------
// ant Sound - Engine Rev.10 (25.2.2005)
// ===================
// *******************

/* ===================================================================

	soundCreate()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	soundPlay(n� Sonido , Repetir)
	-----------------------------
		Hacemos que suene un sonido (0 a x) y que se repita x veces.
		Repetir == 0: Repeticion infinita

	soundStop()
	----------
		Paramos el ultimo sonido.

	soundTick()
	----------
		Debemos ejecutar este metodo en CADA ciclo para gestionar 'tiempos'

	vibraInit(microsegundos)
	-----------------------
		Hacemos vibrar el mobil x microsegundos

=================================================================== */

//#ifdef J2ME

	//#ifdef PLAYER_NONE
	//#elifdef PLAYER_OTA
	//#elifdef PLAYER_MIDP20

		// *******************
		// -------------------
		// Sound - Engine - Rev.0 (28.11.2003)
		// -------------------
		// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
		// ===================
		// *******************

		Music[] Sonidos;

		int SoundOld = -1;
		boolean needRestart = false;
		
		// -------------------
		// Sound INI
		// ===================
		
		public void soundCreate(String[] sons)
		{	
			Sonidos = new Music[sons.length];

			for (int i=0 ; i<sons.length ; i++)
			{
				Sonidos[i] = SoundCargar(sons[i]);
			}
		}

		public Music SoundCargar(String Nombre)
		{
			return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre));
		}
		
		// -------------------
		// Sound SET
		// ===================
		
		public void soundPlay(int Ary, int Loop)
		{
			soundStop();

			if (gameSound)
			{
				if (Loop<1) {Loop=-1;}

				try
				{
					Sonidos[Ary].setLooping(Loop==0);
					//VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
					Sonidos[Ary].play();
				}
				catch(Exception e)
				{
				//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
					Debug.println(e.toString());
				//#endif

					needRestart = true;					
				}

				SoundOld = Ary;
			}
		}
		
		// -------------------
		// Sound RES
		// ===================
		
		public void soundStop()
		{
			if (SoundOld != -1)
			{
				try
				{
					Sonidos[SoundOld].stop();
				}
				catch (Exception e)
				{
				//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
					Debug.println(e.toString());
				//#endif
				}
		
				SoundOld = -1;
			}
		}
		
		// -------------------
		// Sound RUN
		// ===================
		
		public void soundTick()
		{
			if(needRestart)
			{
				try
				{
					if(SoundOld >= 0) Sonidos[SoundOld].play();
				}
				catch(Exception e2) {}
				
				needRestart = false;
			}
		}
		
		// -------------------
		// Vibra SET
		// ===================
		
		public void vibraInit(int Time)
		{
		//#ifdef VIBRATION
			if (gameVibra)
			{
				try
				{
					DeviceControl.startVibra(100,Time);
				}
				catch (Exception e) {}
			}
		//#endif
		}

		// <=- <=- <=- <=- <=-


	//#elifdef PLAYER_MIDP20_CACHED
	//#elifdef PLAYER_MIDP20_FORCED
	//#elifdef PLAYER_MIDP20_SIMPLE
	//#elifdef PLAYER_MIDP20_LOAD
	//#elifdef PLAYER_MIDP20_TICK
	//#elifdef PLAYER_SI_CX65
	//#elifdef PLAYER_MOTC450
	//#elifdef PLAYER_SIEMENS_MIDI
	//#elifdef PLAYER_TSM6
	//#elifdef PLAYER_SAMSUNG
	//#elifdef PLAYER_MO-E1000
	//#elifdef PLAYER_SHARP
	//#endif

//#elifdef DOJA
//#endif

// <=- <=- <=- <=- <=- Ant Sound Engine






//#ifdef J2ME

// **************************
// --------------------------
// Listerner - Engine v1.0 - Rev.0 (9.7.2003)
// ==========================
// **************************

Command[] listenerCmd;

// -------------------
// listener Init
// ===================

public void listenerInit(String strLeft, String strRight)
{
	if (listenerCmd == null)
	{
		listenerCmd = new Command[2];
	} else {
		/*removeCommand(listenerCmd[1]);
		removeCommand(listenerCmd[0]);*/
	}
	
							
	//#ifdef SM-myv55
	//#else
	listenerCmd[0] = new Command(strLeft, Command.SCREEN, 1);
	listenerCmd[1] = new Command(strRight, Command.SCREEN, 1);
	//#endif
		
	//#ifndef FLIPLISTENERKEYS			
	/*addCommand(listenerCmd[0]);
	addCommand(listenerCmd[1]);*/
	//#else		

	//#endif
						
	//setCommandListener(this);
	
}

// -----------------------
// Listener command Action
// =======================

public void commandAction (Command c, Displayable d)
{
		
	if (listenerCmd!=null)
	{
		if (c == listenerCmd[0])
		{			
			intKeyMenu = -1;
		} else {			
			intKeyMenu =  1;
		}

		limpiaKeyMenu = true;
		return;
	}
}

// <=- <=- <=- <=- <=-

//#elifdef DOJA
//#endif








//#ifdef DOJA
//#endif

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
// Final Clase BiosCanvas
// **************************************************************************//



// **************************************************************************//
// Inicio Clase com.mygdx.mongojocs.sanfermines2006.GameCanvas
// **************************************************************************//

// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// *******************************************
// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// Terminal: 
// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


// *******************
// -------------------
// canvas - Engine
// ===================
// *******************

// -------------------
// canvas Init :: Llamamos a este metodo JUSTO antes de empezar el bucle maestro del midlet.
// ===================

public void canvasInit()
{

// --------------------------------
// Forzamos a FULL CANVAS para J2ME / MIDP 2.0
// ================================
//#ifdef J2ME
	//#ifdef FULLSCREEN
		//#ifdef MIDP20
			//setFullScreenMode(true);
			try {Thread.sleep(300);} catch (Exception e) {}
			canvasWidth = getWidth();
			canvasHeight = getHeight();
		//#endif
	//#endif
//#endif
// ================================


// --------------------------------
// Pintamos TODO el canvas de color NEGRO de forma INMEDIATA
// ================================
	//#ifdef J2ME
	Image clock = loadImage("/clock.png");	
	canvasImg = clock;
	//#endif
	canvasFillTask(RGB_BKG);		// Color NEGRO
	gameDraw();						// FORZAMOS una llamada a 'canvasDraw()' AHORA MISMO.
// ================================


// --------------------------------
// Descargamos el MFS de internet, gestionado por una barra de progreso
// ================================
//#ifdef DOJA
//#endif
// ================================


// --------------------------------
// Cargamos e inicializamos TODOS los sonidos del juego
// ================================

	//#ifdef FORCESTARTUPREPAINT
	canvasImg = clock; canvasFillTask(RGB_BKG); gameDraw();
	//#endif
	
//#ifndef PLAYER_NONE

	//#ifdef J2ME
		//#ifdef PLAYER_OTA
		//#elifdef PLAYER_SHARP
		//#elifdef PLAYER_SAMSUNG
		//#else
	
			soundCreate(new String[] {
					"/0.mid",
					"/1.mid",
					"/2.mid",
					"/3.mid",
					"/4.mid",
					"/5.mid",
					"/6.mid",
					"/7.mid",
					"/8.mid",
					"/9.mid",
					"/10.mid",
					"/11.mid",
					"/12.mid"
			});
	
		//#endif
		
	//#elifdef DOJA
	//#endif
	
	//#ifdef FORCESTARTUPREPAINT
	//#endif
	
//#endif

// ================================

}


// -------------------
// Draw :: Llamamos a este metodo para renderizar en pantalla, el objeto 'scr' es nuestro GRAPHICS.
// ===================

public void Draw()
{

// --------------------------------
// Renderizamos PLAY
// ================================

	//if (playShow) { playShow=false; playDraw(); }
					
	playDraw();
					
// ================================

}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************

// -------------------
// play Create Gfx
// ===================

public void loadGameGfx()
{

	//#ifdef J2ME			
	ballCoo = loadFile("/ball.coo");
	ballImg = loadImage("/ball.png");
	
	increaseLoadPercent(10);
	
	//#ifndef NOBALLLINE	
	lineCoo = loadFile("/line.coo");
	lineImg = loadImage("/line.png");	
	//#endif
	
	increaseLoadPercent(10);
	
	//#ifndef NOBALLSPIKES
	spikesCoo = loadFile("/spikes.coo");
	spikesImg = loadImage("/spikes.png");
	//#endif
	
	increaseLoadPercent(10);
	
	gadgetCoo = loadFile("/gadget.coo");
	gadgetImg = loadImage("/gadget.png");
	
	increaseLoadPercent(10);
			
	tileCoo = new byte[4][];
	tilesImg = new Image[4];
			
	for(int i = 0; i < tilesImg.length; i++)
	{	
		tileCoo[i] = loadFile("/tiles"+(i+1)+".coo");
		tilesImg[i] = loadImage("/tiles"+(i+1)+".png");
		increaseLoadPercent(10);
	}
					
	//#ifndef NOSHADOWS					
	shadowCoo = loadFile("/ballShadow.coo");
	shadowImg = loadImage("/ballShadow.png");
	//#endif
	
	increaseLoadPercent(10);
	
	//#ifdef IMAGEBAR
	barsImg = loadImage("/bars.png");
	//#endif
	
	//#ifdef SCREENFRAME
	//#endif
	
	//#elifdef DOJA
	//#endif
}

public void destroyGameGfx()
{
	ballCoo = null;
	ballImg = null;
	lineCoo = null;
	lineImg = null;
	spikesCoo = null;
	spikesImg = null;
	gadgetCoo = null;
	gadgetImg = null;
	tileCoo = null;
	tilesImg = null;
	shadowCoo = null;
	shadowImg = null;
	//#ifdef IMAGEBAR
	barsImg = null;
	//#endif
	//#ifdef SCREENFRAME
	//#endif
	System.gc();
	
}

/*public void playCreate_Gfx()
{
	//#ifndef INITIALLOAD
	loadGameGfx();
	//#else
	increaseLoadPercent(90);
	//#endif
}*/

// -------------------
// play Destroy Gfx
// ===================

/*public void playDestroy_Gfx()
{
	//#ifndef INITIALLOAD
	destroyGameGfx();	
	//#endif
}*/

// -------------------
// play Init Gfx
// ===================

/*public void playInit_Gfx()
{
}*/

// -------------------
// play Release Gfx
// ===================

/*public void playRelease_Gfx()
{
}*/

// -------------------
// play Draw Gfx
// ===================

//#ifdef INITIALLOAD
public void initialLoadPicture()
{	
		//#ifdef J2ME
		pictureImg = loadImage("/Caratula.png");
		//#elifdef DOJA
		//#endif
}
//#endif

public void loadPicture()
{
	//#ifndef INITIALLOAD
		//#ifdef J2ME
		pictureImg = loadImage("/Caratula.png");
		//#elifdef DOJA
		//#endif
	//#endif
}

public void destroyPicture()
{
	//#ifndef INITIALLOAD
	pictureImg = null;	
	System.gc();
	//#endif
}

public String timeString(int t)
{
	int min = 0, sec = 0, dec = 0;
	
	if(t<0) return "-'--\"-";
	else{
		
		min = t/(60*13);
		sec = (t/13)%60;
		dec = ((t%13)*10)/13;
		
		return min + "'" + (sec < 10 ? "0"+sec : ""+sec) + "\"" + dec;
	}		
}

/*public void resetLoadPercent()
{
	loadPercent = 0; canvasShow = true; gameDraw();
}*/

public void increaseLoadPercent(int d)
{
	loadPercent += d; if(loadPercent > 100) loadPercent = 100;
	canvasShow = true; gameDraw();
	//#ifdef J2ME
	serviceRepaints();
	//#endif
}

	public void standardBar(int textLines, boolean levelInfo, boolean menu, boolean ok, boolean loadBar)
	{
		int min, sec, x, y, sx;
		//#ifdef BARVERSIONA
		int margin = 3;
		//#else
		//#endif


		y = canvasHeight - fontHeight*textLines - (margin*2);

		//#ifndef SCREENFRAME
		//if(sprMaxY <= y && levelInfo) y = sprMaxY;
		//#endif

		min = timeTicks/(60*13); sec = (timeTicks/13)%60;


		//#ifdef BARVERSIONA
		drawTextBox(0,y, canvasWidth, canvasHeight - y);
		//#elifdef BARVERSIONB
		//#elifdef BARVERSIONC
		//#endif


		if(levelInfo)
		{
			//#ifdef BARVERSIONC
			//#else
				//#ifdef NOPHONEFONTATBAR
				//#else
					textDrawNoCut(gameText[13][0]+" "+(currentLevel+1),canvasWidth/8,y + margin,RGB_DARK,TEXT_HCENTER);
					textDrawNoCut(min+":"+(sec < 10 ? "0"+sec : ""+sec),-margin,y + margin,((timeTicks/7)%2 == 0 || timeTicks > 30*13 ? RGB_LIGHT : RGB_RED),TEXT_RIGHT);
				//#endif
			//#endif
		}

		if(loadBar)
		{
			scr.setColor(RGB_NOTAVAILABLE);
			scr.fillRect(margin + 2, y + margin + 2, canvasWidth - margin*2 - 4, fontHeight*textLines - 4);
			scr.setColor(RGB_BOX);
			scr.fillRect(margin + 3, y + margin + 3, (((canvasWidth - margin*2 - 6)*loadPercent)/100), fontHeight*textLines - 6);

		}

		drawBoxButtons(!loadBar,menu,ok,(menu ? 1 : 0),2,0,y,canvasWidth,canvasHeight-y);
	}

public void drawBoxButtons(boolean left, boolean leftAtBottom, boolean right, int sprLeft, int sprRight, int bx, int by, int bw, int bh)
{
	//#ifdef VERSIONA	
	if(left){				
		if(leftAtBottom) showImage(buttonImg,9*sprLeft,0,9,13,bx+2,by + bh -14);	
		else showImage(buttonImg,9*sprLeft,0,9,13,bx+2,by - 3);		
	}
	
	if(right) showImage(buttonImg,9*sprRight,0,9,13,bx + bw - 15, by + bh - 11);
	
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif	
}

//#ifdef IMAGEBAR
//#ifdef IMAGEBARBIG
//#else
public void imageBar()
{
	int min, sec, clock, baseY = canvasHeight - 41;
	min = timeTicks/(60*13); sec = (timeTicks/13)%60;
	String s = min+":"+(sec < 10 ? "0"+sec : ""+sec);
	Ball pl = player;
	
	//#ifdef NK
	int despY = 2;
	//#else
	//#endif
	
	showImage(barsImg,0,65,176,19,0,0);
	showImage(barsImg,138,84,38,24,138,19);
	
	showImage(barsImg,0,24,176,41,0,baseY);
	showImage(barsImg,111,0,65,24,176-65,baseY - 24);
	
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif
	
	textDrawNoCut(gameText[13][0]+" "+(currentLevel+1),10,9 + despY - fontHeight/2,RGB_LIGHT,0);
	
	textDrawNoCut(s, 132 - menuListFont.stringWidth(s), baseY + 8 + despY - fontHeight/2,((timeTicks/7)%2 == 0 || timeTicks > 30*13 ? RGB_LIGHT : RGB_RED),0);
	
	clock = 360 - (360*timeTicks/packTimes[currentLevel][2]);
	scr.setColor(0xb00000);	
	if(clock>=15) scr.fillArc(142,baseY - 15,25,25,90,-(15*(clock/15)));
		
	if (pl.invulnerable > 0)
		if(!blinking(pl.invulnerable)) 
			showBallSprite(119,baseY + 28,68,false);
	if (pl.traction > 0)
		if(!blinking(pl.traction)) 
			showBallSprite(95,baseY + 28,66,false);
	if(bombPower > 1){
	
		showBallSprite(73,baseY + 26,67,false);		
		showImage(barsImg,8*(bombPower - 2),0,9,7,74,baseY + 30);
	}
		
	if (!bombLimit) 
		showBallSprite(50,baseY + 26,70,false);
	if (seeHidden > 0) 
		if(!blinking(seeHidden)) 
			showBallSprite(138,baseY + 29,71,false); 
	if (pickedJewels > 0) 
		showBallSprite(17,baseY + 16,72,false);
	
	
}
//#endif
//#endif

void menuSquare(int x, int y, int color)
{
		
	scr.setColor(color);

	//#ifdef FORCESQUARE
	//#else
		
		//#ifdef SQUAREUP
		y -= 2;
		//#elifdef SQUAREDOWN
		//#endif
	
	scr.fillRect(x, y+2, 9, fontHeight-4);
	//#endif
	
}

void menuJewel(int x, int y)
{		
	//#ifdef VERSIONA
	int grid = 24;
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif
	
	//#ifdef J2ME	
	y += fontHeight/2 - grid/2;
	//#elifdef DOJA
	//#endif
	
	PutSprite(ballImg,x,y,grid,72,ballCoo,1,false);													
}

public void playDraw()
{
	//if(angle % 16 != 0) return;
			
	int topColors[] = {RGB_GOLD,RGB_SILVER,RGB_BRONZE,RGB_NOTAVAILABLE};
	int y, i, fitOptions, firstOption = 0;
	
	//if(gameStatus != lastGameStatus)
		playShow = true;
		
	switch(gameStatus){
		
	case GAME_MENU_PACK_SELECT :
		
		if(playShow){
					
		canvasFillDraw(RGB_BKG);		
		
		//#ifdef MENUVERSIONA
		textDraw(gameText[19][0],canvasWidth/6,canvasHeight/10,RGB_LIGHT, 0);	
			
		y = 3*canvasHeight/10;
			
			
		//#ifndef NONETWORK														
		for(i = 0; i < inJarPacks + dlPacks + 1; i++){			
		//#else
		//#endif
						
			//#ifndef NONETWORK																				
			if(i == inJarPacks + dlPacks)
				textDraw(networkText[0][2],canvasWidth/6 + 12,y,RGB_LIGHT, 0);	
			else{						
				menuSquare(canvasWidth/6, y, RGB_LIGHT);
				if(i < inJarPacks)
					textDraw(defaultPak[i],canvasWidth/6 + 12,y, RGB_LIGHT, 0);
				else
					if(dlPacksIds[i - inJarPacks] == storedPackId)					
						textDraw(dlPacksNames[i - inJarPacks], canvasWidth/6 + 12,y,RGB_LIGHT, 0);
					else 
						textDraw(dlPacksNames[i - inJarPacks]+" "+networkText[3][3], canvasWidth/6 + 12,y,RGB_NOTAVAILABLE, 0);						 					 
			}
						
			//#else
			//#endif			
			
			y += fontHeight;
		}
		
		textDraw(">",10,3*canvasHeight/10 + fontHeight*currentPack,RGB_LIGHT, TEXT_LEFT);	
		textDraw("<",-10,3*canvasHeight/10 + fontHeight*currentPack,RGB_LIGHT, TEXT_RIGHT);	
		
		textDraw(gameText[21][1],0, 8*canvasHeight/10, RGB_BOX, TEXT_HCENTER);												
		
		//#elifdef MENUVERSIONB
		//#elifdef MENUVERSIONC
		//#endif
				
		standardBar(1,false,true,true,false);			
		playShow = false;
		}
	break;
	
	case GAME_LOADING_INITIAL :
	case GAME_LOADING_PACK :
	case GAME_PLAY + 1 :
		canvasFillDraw(RGB_BKG);
		//#ifdef MENUVERSIONA
		textDraw(gameText[10][0]+loadPercent+"%",0, 8*canvasHeight/10, RGB_SHADE, TEXT_HCENTER);												
		//#elifdef MENUVERSIONB
		//#elifdef MENUVERSIONC
		//#endif
		standardBar(1,false,false,false,true);		
	break;
	
	case GAME_MENU_LEVEL_SELECT :
		if(playShow){
		
		canvasFillDraw(RGB_BKG);		
		
		//#ifdef MENUVERSIONA
		textDraw(gameText[22][0],canvasWidth/6,canvasHeight/10,RGB_LIGHT, 0);	
			
		y = 3*canvasHeight/10;
		scr.setColor(RGB_LIGHT);
		
		fitOptions = (4*canvasHeight/10) / (fontHeight); 
		
		if(currentLevel > fitOptions) firstOption = currentLevel - fitOptions;
		
		i = firstOption;
											
		while(
			//#ifndef NONETWORK
			i < 24 
			//#else
			//#endif
			&& y < 7*canvasHeight/10){			
									
			switch(i){
				case 20 : textDrawNoCut(gameText[23][2],canvasWidth/6 + 12,y,(topTimes[19][1]<3 ? RGB_LIGHT : RGB_NOTAVAILABLE), 0); break;				
				//#ifndef NONETWORK
				case 21 : textDrawNoCut(networkText[0][0],canvasWidth/6 + 12,y,RGB_LIGHT, 0); break;
				case 22 : textDrawNoCut(networkText[0][3],canvasWidth/6 + 12,y,RGB_LIGHT, 0); break;
				case 23 : textDrawNoCut(gameText[8][0],canvasWidth/6 + 12,y,RGB_LIGHT, 0); break;
				//#else
				//#endif
				default :
					boolean available = (i == 0 ? true : topTimes[i-1][1] < 3);
								
					menuSquare(canvasWidth/6, y, (available ? topColors[topTimes[i][1]] : RGB_NOTAVAILABLE));				
					
					if (available) textDrawNoCut(gameText[16][0]+" "+(i+1),canvasWidth/6 + 12,y,RGB_LIGHT, 0);
					else textDrawNoCut(gameText[16][0]+" "+(i+1)+" "+gameText[22][1],canvasWidth/6 + 12,y,RGB_NOTAVAILABLE, 0);
					
					if(topTimes[i][2] > 0) menuJewel(canvasWidth - 40,y);				
					//#ifdef J2ME						
					scr.setClip(0,0,canvasWidth,canvasHeight);			
					//#endif
				break;
			}
						
			y += fontHeight;
			i++;
		}
		
		textDraw(">",10,3*canvasHeight/10 + fontHeight*(currentLevel - firstOption),RGB_LIGHT, TEXT_LEFT);	
		textDraw("<",-10,3*canvasHeight/10 + fontHeight*(currentLevel - firstOption),RGB_LIGHT, TEXT_RIGHT);	
		
		textDraw(gameText[21][1],0, 8*canvasHeight/10, RGB_BOX, TEXT_HCENTER);												
		
		//#elifdef MENUVERSIONB
		//#elifdef MENUVERSIONC
		//#endif
				
		standardBar(1,false,true,true,false);			
		playShow = false;
		}
	break;
					
	case GAME_PLAY+2 :	
		
		if(playShow){
		
		canvasFillDraw(RGB_BKG);		
					
		//#ifdef MENUVERSIONA
		textDraw(gameText[16][0]+" "+(currentLevel+1),canvasWidth/6,canvasHeight/10,RGB_LIGHT, 0);
		
		if(topTimes[currentLevel][1] > 2)
		{
						
			menuSquare(canvasWidth/6, 2*canvasHeight/10, topColors[2]);
			
			textDraw(gameText[5][0],canvasWidth/6 + 12,2*canvasHeight/10,RGB_LIGHT, 0);		
			textDraw(timeString(packTimes[currentLevel][2]),canvasWidth/6 + 12,2*canvasHeight/10 + fontHeight,topColors[2], 0);				
									
		}else{
						
			menuSquare(canvasWidth/6, 2*canvasHeight/10, RGB_BOX);
						
			textDraw(gameText[5][0],canvasWidth/6 + 12,2*canvasHeight/10,RGB_LIGHT, 0);	
										
			for(i = 0; i < 3; i++){
							
				textDraw(timeString(packTimes[currentLevel][i]),canvasWidth/6 + 12,2*canvasHeight/10 + fontHeight*(i+1),topColors[i], 0);				
			}
									
			menuSquare(canvasWidth/6, 6*canvasHeight/10, topColors[topTimes[currentLevel][1]]);
			
			textDraw(gameText[14][0],canvasWidth/6 + 12,6*canvasHeight/10,RGB_LIGHT, 0);		
			textDraw(timeString(topTimes[currentLevel][0]),canvasWidth/6 + 12,6*canvasHeight/10 + fontHeight,topColors[topTimes[currentLevel][1]], 0);				
																								
		}
		
								
		textDraw(gameText[21][0],0, 8*canvasHeight/10, RGB_BOX, TEXT_HCENTER);
		
		if(topTimes[currentLevel][2] > 0) menuJewel(canvasWidth - 30,canvasHeight/10);										
		
		standardBar(1,false,true,true,false);	
				
		//#elifdef MENUVERSIONB
		//#elifdef MENUVERSIONC
		//#endif
		
		playShow = false;
		}
	break;
	
	
	case GAME_SHOW_TIP :	
	case GAME_PLAY+3 :		
	if((menuType == -1)|| menuListShow){
							
		//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		long debugMilis;
	
		debugMilis = System.currentTimeMillis();
		//#endif
		
		//#ifdef J2ME								
		spriteCanvasFillDraw(RGB_BKG);		
		//#else
		//#endif
		showLevel();	
				
		//#ifdef J2ME	
			//#ifdef IMAGEBAR
			imageBar();
			//#elifdef BARVERSIONC
			//#else
			//#endif
		//#elifdef DOJA
		//#endif						
		
		//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
		//com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug.println("Render:"+(System.currentTimeMillis() - debugMilis)+" ms");
		//#endif	
	
	}
	break;
	
	case GAME_MENU_NEXT_ASK:
	case GAME_LEVEL_COMPLETE+1:
	
		if(playShow || menuShow)
		if(
			//#ifndef NONETWORK
			(menuType != MENU_NEXT_ASK && menuType != MENU_NETWORK_RECORDS)
			//#else
			//#endif
			|| menuListShow){
			
		canvasFillDraw(RGB_BKG);		
		
		//#ifdef MENUVERSIONA
		textDraw(gameText[16][0]+" "+(currentLevel+1)+" "+gameText[16][1],canvasWidth/6,canvasHeight/10,RGB_LIGHT, 0);		
		menuSquare(canvasWidth/6, 3*canvasHeight/10, topColors[myClass]);
		
		textDraw(gameText[5][0],canvasWidth/6 + 12,3*canvasHeight/10,RGB_LIGHT, 0);		
		textDraw(timeString(myTime),canvasWidth/6 + 12,3*canvasHeight/10 + fontHeight,topColors[myClass], 0);				
		
		textDraw(gameText[20][myClass],canvasWidth/6 + 12,3*canvasHeight/10 + fontHeight*2,topColors[myClass], 0);				
						
		if(pickedJewels > 0)
		{
			textDraw(gameText[14][1],canvasWidth/6 + 12,6*canvasHeight/10,RGB_LIGHT, 0);				
			textDraw(packJewels+gameText[14][3],canvasWidth/6 + 12,6*canvasHeight/10 + fontHeight,RGB_BOX, 0);				
		}		
		else textDraw(packJewels+gameText[14][3],canvasWidth/6 + 12,6*canvasHeight/10,RGB_BOX, 0);				
		
		if(gameStatus != GAME_MENU_NEXT_ASK)
			textDraw(gameText[21][0],0, 8*canvasHeight/10, RGB_BOX, TEXT_HCENTER);				
				
		menuJewel(canvasWidth/6 - 10,6*canvasHeight/10);
		
		//#elifdef MENUVERSIONB
		//#elifdef MENUVERSIONC
		//#endif
				
		standardBar(1,false,true,true,false);				
		
		playShow = false;
		}				
	break;
	
	case GAME_PACK_COMPLETE:
	
		if(playShow)
		{		
		canvasFillDraw(RGB_BKG);		
		
		int totalClass = (packTime < totalTimes[0] ? 0 : (packTime < totalTimes[1] ? 1 : 2));
		
		//#ifdef MENUVERSIONA
		textDraw(gameText[16][4],canvasWidth/6,canvasHeight/10,RGB_LIGHT, 0);
		textDraw(gameText[16][5],canvasWidth/6,canvasHeight/10 + fontHeight,RGB_LIGHT, 0);		
		menuSquare(canvasWidth/6, 3*canvasHeight/10, topColors[totalClass]);
		textDraw(gameText[5][0],canvasWidth/6 + 12,3*canvasHeight/10,RGB_LIGHT, 0);		
		textDraw(timeString(packTime),canvasWidth/6 + 12,3*canvasHeight/10 + fontHeight,topColors[totalClass], 0);				
		textDraw(gameText[20][totalClass],canvasWidth/6 + 12,3*canvasHeight/10 + fontHeight*2,topColors[totalClass], 0);				
										
		textDraw(packJewels+gameText[14][3],canvasWidth/6 + 12,6*canvasHeight/10,RGB_BOX, 0);				
		if(packJewels >= 20) textDraw(gameText[16][6],canvasWidth/6 + 12,6*canvasHeight/10 + fontHeight,RGB_LIGHT, 0);				
		
		textDraw(gameText[21][0],0, 8*canvasHeight/10, RGB_BOX, TEXT_HCENTER);				
				
		menuJewel(canvasWidth/6 - 10,6*canvasHeight/10);
		
		//#elifdef MENUVERSIONB
		//#elifdef MENUVERSIONC
		//#endif
				
		standardBar(1,false,true,true,false);
		
		playShow = false;
		}
						
	break;
	
	case GAME_TIME_OVER +1:
	case GAME_TIME_OVER:
		if(playShow || gameStatus == GAME_TIME_OVER)
		{		
			
			//#ifdef J2ME
			spriteCanvasFillDraw(RGB_BKG);
			//#elifdef DOJA
			//#endif
			
			showLevel();	
			//#ifdef IMAGEBAR
			imageBar();
			//#else
			//#endif
			textDraw(gameText[17][0],0,2*canvasHeight/7,RGB_LIGHT, TEXT_HCENTER | TEXT_BOXED | TEXT_OUTLINE);
			//#ifndef MENUVERSIONC
			textDraw(gameText[17][1],0,4*canvasHeight/7,RGB_LIGHT, TEXT_HCENTER | TEXT_BOXED | TEXT_OUTLINE);
			//#endif
		}
	break;
	
	case GAME_MENU_RETRY_ASK:
		if(menuListShow){
			//#ifdef J2ME
			spriteCanvasFillDraw(RGB_BKG);
			//#elifdef DOJA
			//#endif			
			showLevel();
			//#ifdef IMAGEBAR
			imageBar();
			//#else
			//#endif		
			//#ifdef J2ME	
			scr.setClip(0,0,canvasWidth,canvasHeight);						   	
			//#endif
			textDraw(gameText[18][0],0,canvasHeight/2,RGB_LIGHT, TEXT_HCENTER | TEXT_BOXED | TEXT_OUTLINE);		
		}		
	break;
	
	//#ifndef NONETWORK	
	case GAME_MENU_PACK_DOWNLOAD_SELECT :
	if(menuListShow){
		canvasFillDraw(RGB_BKG);			
		textDraw(networkText[3][0],0,0,RGB_LIGHT, TEXT_HCENTER | TEXT_VCENTER | TEXT_BOXED | TEXT_OUTLINE);		
	}
	break;
	
	case GAME_MENU_INPUT_RECORD_NAME +1 :
	case GAME_MENU_SEE_NETWORK_LEVEL_RECORDS :
	case GAME_MENU_SEE_NETWORK_PACK_RECORDS :
	case GAME_MENU_PACK_GET_LIST :
	case GAME_MENU_INPUT_DOWNLOAD_CODE+1:
	case GAME_MENU_DOWNLOAD_PACK:
		if(playShow){		
		canvasFillDraw(RGB_BKG);
		standardBar(1,false,true,false,false);							
		textDraw(networkText[5][0],0,0,RGB_LIGHT, TEXT_HCENTER | TEXT_VCENTER | TEXT_BOXED | TEXT_OUTLINE);		
		playShow = false;		
		}
	break;	
	
	case GAME_MENU_DOWNLOAD_CODE_ERROR:
		if(playShow){		
			canvasFillDraw(RGB_BKG);
			standardBar(1,false,false,true,false);							
			textDraw(networkText[4][1],0,0,RGB_LIGHT, TEXT_HCENTER | TEXT_VCENTER | TEXT_BOXED | TEXT_OUTLINE);		
			playShow = false;
		}
	break;
	//#endif
	}
	
	
	if(loadingShow){
	
		textDraw(gameText[10][0],0,0,RGB_LIGHT, TEXT_HCENTER | TEXT_VCENTER | TEXT_BOXED | TEXT_OUTLINE);
		loadingShow = false;
	}
		
}

//#ifdef VERSIONA
	//#ifdef SMALLFRUSTUM
	//#else
	public static final int HORIZONTAL_RADIUS = 8;
	public static final int VERTICAL_RADIUS = 12;
	//#endif
//#elifdef VERSIONB
//#elifdef VERSIONC
//#endif


int frustum[];

void setFrustum(int x, int y, int z)
{
	frustum[0] = x - HORIZONTAL_RADIUS;
	frustum[3] = x + HORIZONTAL_RADIUS;
	frustum[1] = y - VERTICAL_RADIUS;
	frustum[4] = y + VERTICAL_RADIUS;
	frustum[2] = z - HORIZONTAL_RADIUS;
	frustum[5] = z + HORIZONTAL_RADIUS;
}

void enlargeFrustum(int x, int y, int z)
{
	if(frustum[0] > x) frustum[0] = x;
	if(frustum[3] < x) frustum[3] = x;
	if(frustum[1] > y) frustum[1] = y;
	if(frustum[4] < y) frustum[4] = y;
	if(frustum[2] > z) frustum[2] = z;
	if(frustum[5] < z) frustum[5] = z;
}

void enlargeFrustumForExplosion(int e[])
{
	if(frustum[0] > e[0] - e[6]) frustum[0] = e[0] - e[6];
	if(frustum[3] < e[0] + e[5]) frustum[3] = e[0] + e[5];	
	if(frustum[2] > e[2] - e[7]) frustum[2] = e[2] - e[7];
	if(frustum[5] < e[2] + e[8]) frustum[5] = e[2] + e[8];
}

void cropFrustum(int x1, int y1, int z1, int x2, int y2, int z2)
{
	if(frustum[0] < x1) frustum[0] = x1;
	if(frustum[3] >= x2) frustum[3] = x2 - 1;
	if(frustum[1] < y1) frustum[1] = y1;
	if(frustum[4] >= y2) frustum[4] = y2 - 1;
	if(frustum[2] < z1) frustum[2] = z1;
	if(frustum[5] >= z2) frustum[5] = z2 - 1;
		
}

void showLevel()
{
	int xinc = 1, zinc = 1, step=SUMMARY, aux, x, y, frame = 0;
	byte theTile;
	Ball pl = player, b;
	//Level lev = lev;
	
	//#ifdef SCREENFRAME
	//#endif
	
	//#ifdef SHOWSUMMARYAREAS
	//int checkedAreas = 0;
	//#endif
	
	// Construct the viewport's 3D Frustum
	
	//setFrustum(pl.x, pl.y, pl.z);	
	frustum[0] = pl.x - HORIZONTAL_RADIUS;
	frustum[3] = pl.x + HORIZONTAL_RADIUS;
	frustum[1] = pl.y - VERTICAL_RADIUS;
	frustum[4] = pl.y + VERTICAL_RADIUS;
	frustum[2] = pl.z - HORIZONTAL_RADIUS;
	frustum[5] = pl.z + HORIZONTAL_RADIUS;
	
	cropFrustum(MARGIN, MARGIN, MARGIN, sizeX - MARGIN, sizeY - MARGIN, sizeZ - MARGIN);
		
	enlargeFrustum(pl.x,pl.y,pl.z);
	
	//#ifndef NOADAPTFRUSTUM
	if(enem != null)
	for(int i = 0; i < enem.length; i++)		
		if(enem[i].active)
		enlargeFrustum(enem[i].x,enem[i].y,enem[i].z);
		
	if(pl.state == Ball.BROKEN_ST)
	for(int i = 0; i < playerPieces.length; i++)
		enlargeFrustum(playerPieces[i].x,playerPieces[i].y,playerPieces[i].z);
			
	//#ifndef NOTILEPIECES
	for(int i = 0; i < tilePieces.length; i++)
		enlargeFrustum(tilePieces[i].x,tilePieces[i].y,tilePieces[i].z);
	//#endif
			
	for(int i = 0; i < bomb.length; i++)
		if(bomb[i].active)
			enlargeFrustum(bomb[i].x,bomb[i].y,bomb[i].z);
		
	for(int i = 0; i < expl.length; i++)
		if(expl[i][9] != 0)
			enlargeFrustumForExplosion(expl[i]);
	
	cropFrustum(pl.x - HORIZONTAL_RADIUS, pl.y - VERTICAL_RADIUS, pl.z - HORIZONTAL_RADIUS, pl.x + HORIZONTAL_RADIUS, pl.y + VERTICAL_RADIUS, pl.z + HORIZONTAL_RADIUS);
	//#endif
	
	cropFrustum(0, 0, 0, sizeX, sizeY, sizeZ);
	
		
	// Flip the frustum if needed, depending on the camera's angle
						
	if(angle > 0 && angle <= 64)	
	{			
	}	
	else if(angle > 64 && angle <= 128)	
	{
		zinc = -1;		
		aux = frustum[2]; frustum[2] = frustum[5]; frustum[5] = aux;	
	}
	
	else if(angle > 128 && angle <= 192)	
	{
		zinc = -1;		
		aux = frustum[2]; frustum[2] = frustum[5]; frustum[5] = aux;
		xinc = -1;		
		aux = frustum[0]; frustum[0] = frustum[3]; frustum[3] = aux;	
	}	
	else if(angle > 192 || angle == 0)	
	{
		xinc = -1;		
		aux = frustum[0]; frustum[0] = frustum[3]; frustum[3] = aux;	
	}
	
	// Stablish iteration bounds for representation
	
	int minx, maxx, minz, maxz;
	
	minz = (zinc > 0 ? (frustum[2]/step)*step  : (frustum[2]/step)*step);
	minx = (xinc > 0 ? (frustum[0]/step)*step  : (frustum[0]/step)*step);
	
	maxz = (zinc > 0 ? (frustum[5]/step)*step  : (frustum[5]/step)*step);
	maxx = (xinc > 0 ? (frustum[3]/step)*step  : (frustum[3]/step)*step);
		
	
	if(pl.y < 0)
		showPlayer(pl);
		
	
	//#ifndef IGNORESUMMARY
	
	minz = (zinc > 0 ? (frustum[2]/step)*step : (frustum[2]/step)*step);
	minx = (xinc > 0 ? (frustum[0]/step)*step : (frustum[0]/step)*step);
	
	maxz = (zinc > 0 ? (frustum[5]/step)*step : (frustum[5]/step)*step);
	maxx = (xinc > 0 ? (frustum[3]/step)*step : (frustum[3]/step)*step);
		
	// First level check, skip summary matrix empty areas
	
	for(int j2 = frustum[1]; j2 <= frustum[4]; j2+=SUMMARYY)
	
	for(int k2 = minz; (zinc > 0 ? k2 <= maxz : k2 >= maxz); k2+=zinc*step)
	for(int i2 = minx; (xinc > 0 ? i2 <= maxx : i2 >= maxx); i2+=xinc*step)
	
	if(checkAnyFlagSummary(i2,j2,k2))
			
	// Second check, show summary matrix not-empty areas
			
	for(int j = j2;  j < j2 + SUMMARYY; j++)			
	for(int k = (zinc > 0 ? k2 : k2 + step - 1); (zinc > 0 ? k < k2 + step: k >= k2); k+=zinc)
	for(int i = (xinc > 0 ? i2 : i2 + step - 1); (xinc > 0 ? i < i2 + step: i >= i2); i+=xinc)
	
	//#else
	
	minz = (zinc > 0 ? frustum[2] : frustum[2]);
	minx = (xinc > 0 ? frustum[0] : frustum[0]);
	
	maxz = (zinc > 0 ? frustum[5] : frustum[5]);
	maxx = (xinc > 0 ? frustum[3] : frustum[3]);
		
	
	for(int j = frustum[1]; j <= frustum[4]; j++)
	
	for(int k = minz; (zinc > 0 ? k <= maxz : k >= maxz); k+=zinc)
	for(int i = minx; (xinc > 0 ? i <= maxx : i >= maxx); i+=xinc)
		
	//#endif
	{
		//if(checkAnyFlag(i,j,k))
		if(flagMatrix[j][i][k] != 0)
		{	
		
			theTile = tile(i,j,k);
			
			
			// If its only a block cell
			//if(checkIsUniqueFlag(i,j,k,BLOCK)){
			if(flagMatrix[j][i][k] == existFlags[BLOCK]){
												
				// Maze blocks, back piece
						
				cube3Dback(i,j,k,theTile);
				
				// Maze blocks, front piece
								
				if(theTile == CROSS || theTile >= RBELT)
					cube3Dfront(i,j,k,theTile);
				
			
			// If there's something else than blocks			
			}else{
				
				// Maze blocks, back piece
				
				if(checkFlag(i,j,k,BLOCK))				
					cube3Dback(i,j,k,theTile);
									
				// Player
				
				if(checkFlag(i,j,k,PLAYER))
				if(pl.x == i && pl.y == j && pl.z == k)
				//if(pl.state != Ball.BROKEN_ST && pl.state != Ball.EXITED_ST && !(pl.state == Ball.EXITING_ST && pl.counter >= 11))
				{
					showPlayer(pl);
				}
							
				// Enemies
				
				if(checkFlag(i,j,k,ENEMY))
					if(enem != null)
					for(int ii = 0; ii < enem.length; ii++)
					{
						
						b = enem[ii];
									
						if(b.active)
						//==================
						if(Math.abs(b.y - b.y) <= 3)
						//==================
						if(b.x == i && b.y == j && b.z == k){
							
							//#ifndef NOSHADOWS
							showShadow(b);
							//#endif
							
							int banim = b.counter%6;
																		
							switch(b.enemyType)
							{
								case Ball.BLACK : showBallSprite(b,48+banim); break;
								case Ball.ELECTRIC : showBallSprite(b,30+banim); 
													 showBallSprite(b,36+banim); break;
								case Ball.MAGNETIC : showBallSprite(b,42+banim); break;
								case Ball.RUBBER : showBallSprite(b,60+banim); break;
								case Ball.MIRROR : 
									frame = 0;					
									if(pl.collideTimer > 0) frame += (pl.collideTimer);
									showBallSprite(b,54+frame); 
									//#ifndef NOBALLLINE							
									//#ifdef VERSIONA
									PutSprite(lineImg,isoX(b.realx,b.realy,b.realz)-8,
													  isoY(b.realx,b.realy,b.realz)-8,16,(pl.anglev>>8)*8 + (pl.angleu>>8),lineCoo,1,true);
									//#elifdef VERSIONB
									//#elifdef VERSIONC
									//#endif
									//#endif											
								break;
							}															
						}
					}
					
							
				// Bombs
				
				if(checkFlag(i,j,k,BOMB))
				
					for(int ii = 0; ii < bomb.length; ii++)
					{
					
					b = bomb[ii];
					
					if(b.active)
														
						if(b.x == i && b.y == j && b.z == k){				
						
							//showShadow(bomb[ii]);					
							
							showBallSprite(b,18+(b.counter/10));						
																									
							//showBox(bomb[ii].boundBox);
						}
					}		
				
				
				
				// Explosions
				
				if(checkFlag(i,j,k,EXPLOSION))
				{
					for(int ii = 0; ii < expl.length; ii++)
					if(expl[ii][9] != 0)
					{
						frame = Ball.explosionFrameAt(expl[ii],i,j,k)/3; if (frame >= 6 || frame < 0) frame = -1000;					
						int frame2 = (Ball.explosionFrameAt(expl[ii],i,j,k)+1)/3; if (frame2 >= 6 || frame2 < 0) frame2 = -1000;
						int rx=i<<8, ry = j<<8, rz = k<<8;
						
						switch(Ball.explosionBurnsAt(expl[ii],i,j,k))
						{
							
							case 5 : 					
							showBallSprite(isoX((rx)+128,(ry)+128,(rz)+128),isoY((rx)+128,(ry)+128,(rz)+128),24+frame,true);
							break;
							
							case 2 :					
							showBallSprite(isoX((rx)+128,(ry)+128,(rz)+64),isoY((rx)+128,(ry)+128,(rz)+64),24+frame2,true);
							showBallSprite(isoX((rx)+128,(ry)+128,(rz)+192),isoY((rx)+128,(ry)+128,(rz)+192),24+frame,true);
							break;
							
							case 4 : 					
							showBallSprite(isoX((rx)+128,(ry)+128,(rz)+64),isoY((rx)+128,(ry)+128,(rz)+64),24+frame,true);
							showBallSprite(isoX((rx)+128,(ry)+128,(rz)+192),isoY((rx)+128,(ry)+128,(rz)+192),24+frame2,true);
							break;
							
							case 1 :					
							showBallSprite(isoX((rx)+64,(ry)+128,(rz)+128),isoY((rx)+64,(ry)+128,(rz)+128),24+frame2,true);
							showBallSprite(isoX((rx)+192,(ry)+128,(rz)+128),isoY((rx)+192,(ry)+128,(rz)+128),24+frame,true);
							break;
							
							case 3 : 					
							showBallSprite(isoX((rx)+64,(ry)+128,(rz)+128),isoY((rx)+64,(ry)+128,(rz)+128),24+frame,true);
							showBallSprite(isoX((rx)+192,(ry)+128,(rz)+128),isoY((rx)+192,(ry)+128,(rz)+128),24+frame2,true);
							break;						
						}
					}
				}
				
				// Player pieces
				
				if(pl.state == Ball.BROKEN_ST)			
				if(checkFlag(i,j,k,PLAYER_PIECE))
				
					for(int ii = 0; ii < playerPieces.length; ii++)				
					{
						b = playerPieces[ii];
					
						if(b.x == i && b.y == j && b.z == k){
						
							showBallSprite(b,10+(((b.counter+ii)/2)%6));
																															
						}
					}
				
							
				// Tile pieces
				
				//#ifndef NOTILEPIECES									
				if(checkFlag(i,j,k,TILE_PIECE))
				
					for(int ii = 0; ii < tilePieces.length; ii++)	
					{
						b = tilePieces[ii];
					
						if(b.active)							
						if(b.x == i && b.y == j && b.z == k){
																															
							drawGadgetSprite(32 + (ii%5), isoX(b.realx,b.realy,b.realz),isoY(b.realx,b.realy,b.realz), 0);					
						}			
					}
				//#endif
							
				// Lifts
				
				if(checkFlag(i,j,k,LIFT))
				{
					Ball lift;
									
					if(lifts != null)								
					for(int ii = 0; ii < lifts.length; ii++)				
					if(lifts[ii].x == i && lifts[ii].y == j && lifts[ii].z == k)
					{
						lift = lifts[ii];
														
						int angle2 = (256 + angle - 32)%256;
						int column = (angle2>>3)%32;
						
						drawCubeSprite(column,isoX(lift.realx,lift.realy - 128,lift.realz),isoY(lift.realx,lift.realy - 128,lift.realz),0);					
					}
				}
			
						
			// Maze blocks, front piece
		
			if(checkFlag(i,j,k,BLOCK)) 
			if(theTile == CROSS || theTile >= RBELT)
				cube3Dfront(i,j,k,theTile);
			}
			
		}
		
		//#ifdef SHOWSUMMARYAREAS
		/*scr.setClip(0,0,canvasWidth,canvasHeight);
		rect3D(i2<<8,j2<<8,k2<<8,
			   (i2+step)<<8,j2<<8,k2<<8,
			   (i2+SUMMARY)<<8,j2<<8,(k2+SUMMARY)<<8,
			   i2<<8,j2<<8,(k2+SUMMARY)<<8,
			   255,0,0);
			   
		checkedAreas++;*/
		//#endif			  
	}
		
	if(pl.y >= sizeY)
		showPlayer(pl);
				
	//#ifndef NOITEMNOTIFY
	
	// Temporal items indicator	
	
	//#ifdef VERSIONA
	x = 10; xinc = 16; y = 10;
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif
		
	if (pl.invulnerable > 0) { if(!blinking(pl.invulnerable)) showBallSprite(x,y,68,true); x+=xinc; }
	if (pl.traction > 0) { if(!blinking(pl.traction)) showBallSprite(x,y,66,true); x+=16; }
	for(int i = 0; i < bombPower - 1; i++) { showBallSprite(x,y,67,true); x+=xinc; }
	if (!bombLimit) { showBallSprite(x,y,70,true); x+=xinc; }
	if (seeHidden > 0) { if(!blinking(seeHidden)) showBallSprite(x,y,71,true); x+=xinc; }
	if (pickedJewels > 0) { showBallSprite(x,y,72,true); x+=xinc; }
	//#endif

	//#ifdef SHOWSUMMARYAREAS
	//System.out.println ("Checked areas:"+checkedAreas+", checked tiles:"+(checkedAreas*SUMMARY*SUMMARY*SUMMARYY));
	//#endif
	
}

boolean blinking(int cnt)
{
	//#ifdef FRAMESKIP
	//#else
	return (cnt % 2 == 1 && cnt <= 20);
	//#endif	
}


void showPlayer(Ball pl)
{
	int frame, x, y;
	
	if(pl.state == Ball.BROKEN_ST || pl.state == Ball.EXITED_ST || (pl.state == Ball.EXITING_ST && pl.counter >= 11))
		return;
	
	x = isoX(pl.realx,pl.realy,pl.realz);
	y = isoY(pl.realx,pl.realy,pl.realz);
		
	//#ifndef NOSHADOWS
	showShadow(pl);
	//#endif
								 				
	if((pl.state == Ball.REDUCING_ST && pl.counter >= 22 && pl.counter%2 == 0) || (pl.state == Ball.REDUCED_ST && (pl.counter%2 == 0 || pl.counter < 185)))
		frame = 6;					
	else 
		frame = 0;
		
	if(pl.state == Ball.ELECTRIFIED_ST && pl.counter % 4 < 2)
		frame +=30;
					
	if(pl.collideTimer > 0)
		frame += (pl.collideTimer);
					
	showBallSprite(pl,frame);
			
	//#ifndef NOBALLLINE			
		if(frame < 6) 
		//#ifdef VERSIONA
		PutSprite(lineImg,x-8,y-8,16,(pl.anglev>>8)*8 + (pl.angleu>>8),lineCoo,1,true);
		//#elifdef VERSIONB
		//#elifdef VERSIONC
		//#endif
	//#endif
	
	//#ifndef NOBALLSPIKES			
		if(frame < 6 && pl.traction > 0) 
		//#ifdef VERSIONA
		PutSprite(spikesImg,x-12,y-12,24,(pl.anglev>>9)*4 + (pl.angleu>>9),spikesCoo,1,true);
		//#elifdef VERSIONB
		//#elifdef VERSIONC
		//#endif
	//#endif
				
	if(pl.state == Ball.ELECTRIFIED_ST && pl.counter % 4 < 2)
		showBallSprite(pl,36+pl.counter%6);
					
	if(pl.invulnerable > 0 && (pl.invulnerable % 2 == 0 || pl.invulnerable > 20))
		showBallSprite(pl,(frame < 6 ? 73 : 74));
						
	//#ifdef COLLIDEDEBUG																						
	//scr.setClip(0,0,canvasWidth,canvasHeight);
	//showBox(pl.boundBox);
	//#endif
}

//#ifdef NK
DirectGraphics dg;
//#endif


// <=- <=- <=- <=- <=-

static int angle = 16;

static final int SIDE = 21;
static final int SIDE2 = SIDE/2;

public int isoX(int x, int y, int z)
{
	/*x -= 128; z -= 128;
	
	return (canvasWidth/2) + ((SIDE*x*Trig.cos(angle))>>18) - ((SIDE*z*Trig.sin(angle))>>18);*/
	
	int dx = 0, dz = 0;
	
	//x -= 512; z -= 512;
	x -= player.realx; y -= player.realy; z -= player.realz; 
	
	//#ifdef VERSIONA	
	switch(angle)
	{		 		 	 
		 case 16 : dx = 26; dz = -11; break;
		 case 24 : dx = 23; dz = -15; break;
		 case 32 : dx = 20; dz = -20; break;
		 case 40 : dx = 15; dz = -23; break;
		 case 48 : dx = 11; dz = -26; break;
		 case 56 : dx = 6; dz = -28; break;
		 case 64 : dx = 0; dz = -29; break;
		 case 72 : dx = -6; dz = -28; break;
		 
		 case 80 : dx = -11; dz = -26; break;
		 case 88 : dx = -15; dz = -23; break;
		 case 96 : dx = -20; dz = -20; break;
		 case 104: dx = -23; dz = -15; break;
		 case 112: dx = -26; dz = -11; break;
		 case 120: dx = -28; dz = -6; break;
		 case 128: dx = -29; dz = 0; break;
		 case 136: dx = -28; dz = 6; break;
		 
		 case 144: dx = -26; dz = 11; break;
		 case 152: dx = -23; dz = 15; break;
		 case 160: dx = -20; dz = 20; break;
		 case 168: dx = -15; dz = 23; break;
		 case 176: dx = -11; dz = 26; break;
		 case 184: dx = -6; dz = 28; break;
		 case 192: dx = 0; dz = 29; break;
		 case 200: dx = 6; dz = 28; break;
		 
		 case 208: dx = 11; dz = 26; break;
		 case 216: dx = 15; dz = 23; break;
		 case 224: dx = 20; dz = 20; break;
		 case 232: dx = 23; dz = 15; break;
		 case 240: dx = 26; dz = 11; break;
		 case 248: dx = 28; dz = 6; break;
		 case 0  : dx = 29; dz = 0; break;
		 case 8  : dx = 28; dz = -6; break;
	}	
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif
	
	return (canvasWidth>>1) + ((dx*x)>>8) + ((dz*z)>>8);
}


public int isoY(int x, int y, int z)
{
	/*x -= 128; z -= 128;
	
	return (canvasHeight/2)	+ ((SIDE2*x*Trig.sin(angle))>>18) + ((SIDE2*z*Trig.cos(angle))>>18) - (((SIDE2-2)*y)>>8);*/
	
	int dx = 0, dz = 0, dy, cy;
	
	//x -= 512; z -= 512;	
	x -= player.realx; y -= player.realy; z -= player.realz; 
	
	//#ifdef VERSIONA	
	dy = -12;
	
	switch(angle)
	{		 		 
		 case 16 : dx = 6; dz = 12; break;
		 case 24 : dx = 8; dz = 12; break;
		 case 32 : dx = 10; dz = 10; break;
		 case 40 : dx = 12; dz = 8; break;
		 case 48 : dx = 13; dz = 6; break;
		 case 56 : dx = 14; dz = 3; break;
		 case 64 : dx = 14; dz = 0; break;
		 case 72 : dx = 14; dz = -3; break;
		 
		 case 80 : dx = 13; dz = -6; break;
		 case 88 : dx = 12; dz = -8; break;
		 case 96 : dx = 10; dz = -10; break;
		 case 104: dx = 8; dz = -12; break;
		 case 112: dx = 6; dz = -13; break;
		 case 120: dx = 3; dz = -14; break;
		 case 128: dx = 0; dz = -14; break;
		 case 136: dx = -3; dz = -13; break;
		 
		 case 144: dx = -6; dz = -12; break;
		 case 152: dx = -8; dz = -12; break;
		 case 160: dx = -10; dz = -10; break;
		 case 168: dx = -12; dz = -8; break;
		 case 176: dx = -13; dz = -6; break;
		 case 184: dx = -14; dz = -3; break;
		 case 192: dx = -14; dz = 0; break;
		 case 200: dx = -14; dz = 3; break;
		 
		 case 208: dx = -13; dz = 6; break;
		 case 216: dx = -12; dz = 8; break;
		 case 224: dx = -10; dz = 10; break;
		 case 232: dx = -8; dz = 12; break;
		 case 240: dx = -6; dz = 13; break;
		 case 248: dx = -3; dz = 14; break;
		 case 0  : dx = 0; dz = 14; break;
		 case 8  : dx = 3; dz = 13; break;
		 		 
	}
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif
		
	return ((sprMinY + sprMaxY)>>1) + ((dx*x)>>8) + ((dz*z)>>8) + ((dy*y)>>8);
}


public void triangle(int x1, int y1, int x2, int y2, int x3, int y3, int r, int g, int b)
{
	//#ifdef NK
			
	dg.fillTriangle(x1,y1,x2,y2,x3,y3,(((0xFF<<24) | (r<<16)) | (g<<8)) | b);
	
	//#elifdef MIDP20
	//#endif	
}

public void rect3D(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3, int x4, int y4, int z4, int r, int g, int b)
{			 
	setColor(r,g,b);
			 
	scr.drawLine(isoX(x1,y1,z1),isoY(x1,y1,z1),isoX(x2,y2,z2),isoY(x2,y2,z2));				 			 
	scr.drawLine(isoX(x2,y2,z2),isoY(x2,y2,z2),isoX(x3,y3,z3),isoY(x3,y3,z3));				 			 
	scr.drawLine(isoX(x3,y3,z3),isoY(x3,y3,z3),isoX(x4,y4,z4),isoY(x4,y4,z4));				 			 
	scr.drawLine(isoX(x4,y4,z4),isoY(x4,y4,z4),isoX(x1,y1,z1),isoY(x1,y1,z1));				 			 
}

public void showBox(int b[])
{	
	rect3D(b[3],b[4],b[5],
		   b[3] - b[6],b[4],b[5],
		   b[3] - b[6],b[4],b[5] - b[8],
		   b[3],b[4],b[5] - b[8],
		   255, 0 , 0);
		   
	rect3D(b[3],b[1],b[5],
		   b[3] - b[6],b[1],b[5],
		   b[3] - b[6],b[1],b[5] - b[8],
		   b[3],b[1],b[5] - b[8],
		   255, 0 , 0);		   
		   
	rect3D(b[0],b[1],b[2],
		   b[0] + b[6],b[1],b[2],
		   b[0] + b[6],b[1] + b[7],b[2],
		   b[0],b[1] + b[7],b[2],		   
		   255, 0 , 0);		   		   
		   
	rect3D(b[0],b[1],b[2] + b[8],
		   b[0] + b[6],b[1],b[2] + b[8],
		   b[0] + b[6],b[1] + b[7],b[2] + b[8],
		   b[0],b[1] + b[7],b[2] + b[8],		   
		   255, 0 , 0);		   		   		   
}

public void drawCubeSprite(int spriteId, int X, int Y, int yOffset)
{
	int imId = (spriteId/128), line = (spriteId%128), grid, Frame; 
	if(imId == 4) {imId = 3; line += 128; }
	
	//#ifdef VERSIONA
	grid = 40;		
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif	
	
	X-=grid/2; Y += yOffset - grid/2;
	
	//#ifdef J2ME
	Frame=line*6;
	
	int DestX=tileCoo[imId][Frame++];
	int DestY=tileCoo[imId][Frame++];
	int SizeX=tileCoo[imId][Frame++];
	if (SizeX==0) {return;}
	int SizeY=tileCoo[imId][Frame++];
	int SourX=tileCoo[imId][Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=tileCoo[imId][Frame++]; //if (SourY<0) {SourY+=256;}
			
	showSpriteImage(tilesImg[imId],SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY);		
	//#endif
	
	//#ifdef DOJA
	//#endif
	
}

public void drawGadgetSprite(int spriteId, int x, int y, int yOffset)
{
	//#ifdef VERSIONA	
	PutSprite(gadgetImg,x-20,y-20+yOffset,40,spriteId,gadgetCoo,1,true);
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif	
}

public void cube3Dback(int x, int y, int z, int t)
{
	x = x<<8; y = y<<8; z = z<<8;
	
	int angle2 = (256 + angle - 32)%256, line = -1,  columnOffset = 0, column, xx, yy, spriteId, yOffset = 0, screenx, screeny;
	
	switch(t)
	{				
		case RBELT  :
		case LBELT  :
		case UBELT  :
		case DBELT  :
		case TILE	  : line = 0; break;
		case HIDDEN : if(seeHidden > 0) line = 0; break;				
		case ICE    : line = 1; break;
		case BROKEN : line = 2; break;
		case BOX    : line = 3; break;
		case CROSS  : line = 5; break;
		case RRAMPA : line = 6; break;
		case RRAMPB : line = 7; break;
		case RRAMPC : line = 8; break;
		case RRAMPD : line = 9; break;				
		case RRAMPE : line = 10; break;				
		case DRAMPA : line = 6; columnOffset = 8; break;
		case DRAMPB : line = 7; columnOffset = 8; break;
		case DRAMPC : line = 8; columnOffset = 8; break;
		case DRAMPD : line = 9; columnOffset = 8; break;				
		case DRAMPE : line = 10; columnOffset = 8; break;				
		case LRAMPA : line = 6; columnOffset = 16; break;
		case LRAMPB : line = 7; columnOffset = 16; break;
		case LRAMPC : line = 8; columnOffset = 16; break;
		case LRAMPD : line = 9; columnOffset = 16; break;				
		case LRAMPE : line = 10; columnOffset = 16; break;				
		case URAMPA : line = 6; columnOffset = 24; break;
		case URAMPB : line = 7; columnOffset = 24; break;
		case URAMPC : line = 8; columnOffset = 24; break;
		case URAMPD : line = 9; columnOffset = 24; break;				
		case URAMPE : line = 10; columnOffset = 24; break;				
		case HOLE   : line = 11; break;
		case RING   : line = 14; columnOffset = (((ellapsedTicks)>>2) + 8) % 32; 
		//#ifdef VERSIONA
		yOffset= -2; 
		//#elifdef VERSIONB
		//#elifdef VERSIONC
		//#endif
		break;		
	}
	
	//#ifdef VERSIONA
	column = (columnOffset + (angle2>>3))%32;
	//#else
	//#endif	
	
	spriteId = (32*line) + column;
	
	screenx = isoX(x+128,y,z+128); screeny = isoY(x+128,y,z+128);
	
	if(line >= 0) 					
		drawCubeSprite(spriteId, screenx, screeny, yOffset);
		
		
	switch(t)
	{		
		case SPRING	  : 
		drawGadgetSprite((counter%40 < 8 ? 8 + counter%40 : 8), screenx, screeny, 0);					
		break;	
		
		case GOAL 	:
		drawGadgetSprite(((counter/2)%8) + 16, screenx, screeny, 0);			
		break;						
	}
	
}

public void cube3Dfront(int x, int y, int z, int t)
{
	x = x<<8; y = y<<8; z = z<<8;
	
	int angle2 = (256 + angle - 32)%256, line = -1,  columnOffset = 0, column, xx, yy, spriteId, yOffset = 0;
	int ix = isoX(x+128,y+128,z+128), iy = isoY(x+128,y+128,z+128);
	
	switch(t)
	{		
		case CROSS  : line = 4; 
		//#ifdef VERSIONA
		yOffset= -8; 
		//#elifdef VERSIONB
		//#elifdef VERSIONC
		//#endif
		break;
		case HOLE   : line = 12; break;
		case RING   : line = 15; columnOffset = (((ellapsedTicks)>>2) + 8) % 32; 		
		//#ifdef VERSIONA
		yOffset= -2; 
		//#elifdef VERSIONB
		//#elifdef VERSIONC
		//#endif
		break;			
	}
	
	//#ifdef VERSIONA
	column = (columnOffset + (angle2>>3))%32;
	//#else
	//#endif	
	
	spriteId = (32*line) + column;
	
	if(line >= 0) 				
		drawCubeSprite(spriteId, isoX(x+128,y,z+128), isoY(x+128,y,z+128), yOffset);	
		
	//#ifndef NOSHADOWS		
	switch(t)
	{		
		case POWER_IT :		
		case BOMB_IT :		
		case INVUL_IT :		
		case TIME_IT :		
		case TRACTION_IT :		
		case GLASSES_IT :		
		case JEWEL_IT :
		showShadow(120,x+128,yAt(120,x+128,y+128,z+128), z+128);				
		break;
	}
	//#endif
	
	// Primitive graphics	
	switch(t)
	{		
		
		case RBELT :
		line = 13;
		columnOffset = 16;
		xx = x + 95 + (counter*16)%256;
		column = (columnOffset + (angle2>>3))%32;
		spriteId = (32*line) + column;
		drawCubeSprite(spriteId, isoX(xx,y,z+128), isoY(xx,y,z+128), 0);					
		break;
		
		case LBELT :
		line = 13;
		columnOffset = 0;
		xx = x + 160 - (counter*16)%256;
		column = (columnOffset + (angle2>>3))%32;
		spriteId = (32*line) + column;
		drawCubeSprite(spriteId, isoX(xx,y,z+128), isoY(xx,y,z+128), 0);					
		break;
		
		case DBELT :
		line = 13;
		columnOffset = 24;
		yy = z + 95 + (counter*16)%256;
		column = (columnOffset + (angle2>>3))%32;
		spriteId = (32*line) + column;
		drawCubeSprite(spriteId, isoX(x+128,y,yy), isoY(x+128,y,yy), 0);					
		break;
		
		case UBELT :
		line = 13;
		columnOffset = 8;
		yy = z + 160 - (counter*16)%256;
		column = (columnOffset + (angle2>>3))%32;
		spriteId = (32*line) + column;
		drawCubeSprite(spriteId, isoX(x+128,y,yy), isoY(x+128,y,yy), 0);					
		break;
		
		case CROSS :
		line = 16;
		int dir = (counter/10)%4;
		columnOffset = 8*dir;		
		column = (columnOffset + (angle2>>3) + 8)%32;		
		spriteId = (32*line) + column;
		//#ifdef VERSIONA
		drawCubeSprite(spriteId, isoX(x+128,y,z+128), isoY(x+128,y,z+128), -10);			
		//#elifdef VERSIONB
		//#elifdef VERSIONC
		//#endif
		break;
		
		case REDUCER_MA :		
		drawGadgetSprite((player.state == Ball.REDUCING_ST && player.counter >= 10 ? (player.counter - 10) / 3 : 0), isoX(x+128,y,z+128), isoY(x+128,y,z+128), 0);					
		break;
		
		case GOAL	:
		if(player.state == Ball.EXITING_ST)
			drawGadgetSprite((player.counter*2 >= 10 && player.counter*2 < 34 ? (player.counter*2 - 10) / 3 : 0)+24, isoX(x+128,y,z+128), isoY(x+128,y,z+128), -6);					
		break;
				
		case POWER_IT :		
		showBallSprite(ix,iy,67,true);
		break;		
		case BOMB_IT :		
		showBallSprite(ix,iy,70,true);
		break;		
		case INVUL_IT :		
		showBallSprite(ix,iy,68,true);
		break;		
		case TIME_IT :		
		showBallSprite(ix,iy,69,true);
		break;		
		case TRACTION_IT :		
		showBallSprite(ix,iy,66,true);
		break;
		case GLASSES_IT :		
		showBallSprite(ix,iy,71,true);
		break;				
		case JEWEL_IT :		
		showBallSprite(ix,iy,72,true);
		break;
		
	}	
	
}

public void showBallSprite(int X, int Y, int Frame, boolean clip)
{	
	int grid;
		
	//#ifdef VERSIONA
	grid = 24;	
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif
	
	X-=grid/2; Y-=grid/2;
	
	if(Frame < 0) return;
	
	//#ifdef J2ME
	Frame*=6;
	
	int DestX=ballCoo[Frame++];
	int DestY=ballCoo[Frame++];
	int SizeX=ballCoo[Frame++];
	if (SizeX==0) {return;}
	int SizeY=ballCoo[Frame++];
	int SourX=ballCoo[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=ballCoo[Frame++]; //if (SourY<0) {SourY+=256;}
	
	if(clip)		
		showSpriteImage(ballImg,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY);		
	else
		showImage(ballImg,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY);		
	//#endif
	
	//#ifdef DOJA
	//#endif
		
}

public void showBallSprite(Ball b, int frame)
{	
	showBallSprite(isoX(b.realx,b.realy,b.realz),isoY(b.realx,b.realy,b.realz),frame, true);	
}

//#ifndef NOSHADOWS

public void showShadow(Ball b)
{
	showShadow(b.radius,b.realx,b.groundY,b.realz);
}

public void showShadow(int radius, int rx, int ry, int rz)
{
	int x, y, sx, sy, dist, line, column, columnOffset = 0;
	
					
	x = isoX(rx,ry,rz);
	y = isoY(rx,ry,rz);
		
	switch(nearestTileAt(rx,ry,rz))
	{		
		default			  : line = -1;
		case TILE	  : 
		case ICE    : 
		case BROKEN : 
		case BOX    : 
		case CROSS  : line = 0; break;		
		case RRAMPB : line = 1; break;		
		case RRAMPD : line = 2; break;				
		case RRAMPE : line = 2; break;						
		case DRAMPB : line = 1; columnOffset = 8; break;		
		case DRAMPD : line = 2; columnOffset = 8; break;				
		case DRAMPE : line = 2; columnOffset = 8; break;						
		case LRAMPB : line = 1; columnOffset = 16; break;		
		case LRAMPD : line = 2; columnOffset = 16; break;				
		case LRAMPE : line = 2; columnOffset = 16; break;						
		case URAMPB : line = 1; columnOffset = 24; break;		
		case URAMPD : line = 2; columnOffset = 24; break;				
		case URAMPE : line = 2; columnOffset = 24; break;				
	}
	
	int angle2 = (256 + angle - 32)%256;
	
	column = (columnOffset + 2*(angle2>>4))%32;	
	
	if(line >= 0) 
	//#ifdef VERSIONA
		PutSprite(shadowImg,x-8,y-8,16,(32*line) + column,shadowCoo,1,true);
	//#elifdef VERSIONB
	//#elifdef VERSIONC
	//#endif
}
//#endif

//#ifdef J2ME

public void PutSprite(Image Img,  int X, int Y,  int CuadSize, int Frame, byte[] Coor, int Trozos, boolean clip)
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
			
		if(clip) showSpriteImage(Img,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY);		
		else	 showImage(Img,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY);		
	}
} 
//#elifdef DOJA
//#endif


// Trigonometrical stuff

	private static short sintable[];
	
	//private static short costable[];
	
	//private static short tantable[];
	
	public static void initTables()
	{		
		byte temp[]= new byte[384];
		
		//#ifdef J2ME
		Game.gc.loadFile(temp,"/Trig.bin");
		//#endif
				
		//#ifdef DOJA
		//#endif
				
		sintable=new short[64];
		//costable=new short[64];
		//tantable=new short[64];
		
		for(int i=0; i<64; i++){
		
			sintable[i]=(short)(((temp[i*2]&0xFF)<<8) | ((temp[i*2+1]&0xFF))); 		
			//costable[i]=(short)(((temp[128+i*2]&0xFF)<<8) | ((temp[i*2+1+128]&0xFF))); 		
			//tantable[i]=(short)(((temp[i*2+256]&0xFF)<<8) | ((temp[i*2+1+256]&0xFF))); 		
		}	
							
	} 	
			
	static int sin(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=sintable[a%64];
		else 	v=sintable[63-(a%64)];	
		if(a>=128) v=-v;
		return v;
	}

	static int cos(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=sintable[63-(a%64)];
		else 	v=sintable[a%64];	
		if((a>>6==1) || (a>>6==2)) v=-v;
		return v;
	}
	
	/*static int tan(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=tantable[a%64];
		else 	v=tantable[63-(a%64)];	
				
		if((a>>6)%2==1) v=-v;		
		return v;
	}

	
	static int atan(int s, int c)
	{
		int v,a=0;
		if(c!=0) v=(s<<10)/c;
		else v=99999;
		
		if(v<0) v=-v;
				
		while(tantable[a] <v && (a<63)) a++;
		
		if(s>=0 && c>=0)
			return a;
		else if(s>=0 && c<0)
			return 128-a;
		else if(s<0 && c<0)
			return 128+a;		
		return (256-a)%256;		
	}*/

// **************************************************************************//
// Final Clase com.mygdx.mongojocs.sanfermines2006.GameCanvas
// **************************************************************************//


 // com.mygdx.mongojocs.sanfermines2006.Game 8<===============================================================================
 
 
 //#ifdef J2ME
Thread thread;
//#endif

//#ifdef EXTRAPAKS
static final String defaultPak[] = new String[] {"original","ice","mirror"};
static final int defaultPakIds[] = new int[] {0,1,2};
//#else
//static final String defaultPak[] = new String[] {"original"};
//static final int defaultPakIds[] = new int[] {0};
//#endif

static final int inJarPacks = defaultPak.length;

 
 // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean limpiaKeyMenu = false;
int keyMenu, keyX, keyY, keyR, keyMisc, keyCode;
int lastKeyMenu, lastKeyX, lastKeyY, lastKeyR, lastKeyMisc, lastKeyCode;

boolean gameExit = false;
boolean gamePaused = false;
boolean gameForceRefresh = false;

static long userID;

long gameMilis, CPU_Milis, debugMilis;
int gameSleep;
int mainSleep = 75;

public void runInit()
{

	System.gc();

	canvasInit(); System.gc();
	biosInit(); System.gc();
	//#ifndef NOBALLLINE
	initTables();
	//#endif

	// --------------------------------
	// Inicializamos com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug Engine
	// ================================
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.debugCreate(this);
	//#endif
	// ================================


	gameStatus = GAME_LOADING_INITIAL;


	canvasCreate();
	System.gc();

	//#ifdef USELISTENER
	//listenerInit(gameText[15][3],gameText[15][2]);
	listenerInit(gameText[15][0],gameText[15][0]);
	//#endif

	gameStatus = GAME_LOGOS;
}

public void runTick()
{
	if (!gamePaused)
	{
		gameMilis = System.currentTimeMillis();

		lastKeyMenu = keyMenu;	// Keys del Frame Anterior
		lastKeyX = keyX;
		lastKeyY = keyY;
		lastKeyR = keyR;
		lastKeyMisc = keyMisc;
		lastKeyCode = keyCode;

		keyMenu = intKeyMenu;	// Keys del Frame Actual
		keyX = intKeyX;
		keyY = intKeyY;
		keyR = intKeyR;
		keyMisc = intKeyMisc;
		keyCode = intKeyCode;

		//#ifndef NOCHEAT
		if ((keyMisc!=lastKeyMisc) && (keyMisc!=0)){

			//#ifdef VI-TSM100
			//byte[] Cheat_1= {2,2,7,7,7,6,6,6,6,6,9,9,9,9,3,3};
			//#else
			//byte[] Cheat_1= {2,2,7,7,7,6,6,6,6,6,9,9,9,9,3,3};
			//#endif
			if (Cheat_1[CheatPos_1++] != intKeyMisc) {CheatPos_1=0;}
			if (Cheat_1.length==CheatPos_1) {

				// Activate the cheats

				CheatPos_1=0;/*Cheat*/

				vibraInit(500);

				//cheatActive = false;

				if(topTimes != null)
				{
					for(int i = 0; i < 20; i++){

						if(topTimes[i][1] > 2) topTimes[i][1] = 2;
						if(topTimes[i][0] < 0) topTimes[i][0] = 4680;
					}

					saveTopTimes((currentPack < inJarPacks ? defaultPak[currentPack] : dlPacksNames[currentPack - inJarPacks]));

					recalcPackResults();
				}

			}
		}

			/*if(cheatActive) {

				vibraInit(500);

				cheatActive = false;

				if(topTimes != null)
				{
					for(int i = 0; i < 20; i++){

						if(topTimes[i][1] > 2) topTimes[i][1] = 2;
						if(topTimes[i][0] < 0) topTimes[i][0] = 4680;
					}

					saveTopTimes((currentPack < inJarPacks ? defaultPak[currentPack] : dlPacksNames[currentPack - inJarPacks]));
				}

			}		*/
		//#endif

		if (limpiaKeyMenu) {limpiaKeyMenu=false; intKeyMenu = 0;}

		try {
			biosTick();
			if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}

			gameDraw();

			soundTick();
		} catch (Exception e) {
			//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
			System.out.println("*** Exception en la parte L�gica ***"); e.printStackTrace();
			//#endif
		}
	}

	gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
	try	{Thread.sleep( gameSleep < 1? 1:gameSleep );} catch(Exception e) {}

	if(gameExit) {
		soundStop();

		//biosDestroy();
		savePrefs();
		rmsDestroy();

		ga.destroyApp(true);
	}

//	System.gc();
}

public void runEnd()
{
	/*soundStop();

	//biosDestroy();
	savePrefs();
	rmsDestroy();

	ga.destroyApp(true);*/
}

public void run()
{
			
	System.gc();
		
	canvasInit(); System.gc();
	biosInit(); System.gc();
	//#ifndef NOBALLLINE
	initTables();
	//#endif
	
	// --------------------------------
	// Inicializamos com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug Engine
	// ================================
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
 	Debug.debugCreate(this);
	//#endif
	// ================================
	
	
	gameStatus = GAME_LOADING_INITIAL;
	
	
	canvasCreate();
	System.gc();
	
	//#ifdef USELISTENER
	//listenerInit(gameText[15][3],gameText[15][2]);
	listenerInit(gameText[15][0],gameText[15][0]);	
	//#endif
			
	gameStatus = GAME_LOGOS;


	while (!gameExit)
	{
		if (!gamePaused)
		{
			gameMilis = System.currentTimeMillis();

			lastKeyMenu = keyMenu;	// Keys del Frame Anterior
			lastKeyX = keyX;
			lastKeyY = keyY;
			lastKeyR = keyR;
			lastKeyMisc = keyMisc;
			lastKeyCode = keyCode;

			keyMenu = intKeyMenu;	// Keys del Frame Actual
			keyX = intKeyX;
			keyY = intKeyY;
			keyR = intKeyR;
			keyMisc = intKeyMisc;
			keyCode = intKeyCode;
			
			//#ifndef NOCHEAT			
			if ((keyMisc!=lastKeyMisc) && (keyMisc!=0)){
											
					//#ifdef VI-TSM100	
					//byte[] Cheat_1= {2,2,7,7,7,6,6,6,6,6,9,9,9,9,3,3};
					//#else
					//byte[] Cheat_1= {2,2,7,7,7,6,6,6,6,6,9,9,9,9,3,3};
					//#endif
					if (Cheat_1[CheatPos_1++] != intKeyMisc) {CheatPos_1=0;}
					if (Cheat_1.length==CheatPos_1) {
					
						// Activate the cheats
						
						CheatPos_1=0;/*Cheat*/
						
						vibraInit(500);
						
						//cheatActive = false;
						
						if(topTimes != null)
						{				
							for(int i = 0; i < 20; i++){
							
								if(topTimes[i][1] > 2) topTimes[i][1] = 2;
								if(topTimes[i][0] < 0) topTimes[i][0] = 4680;
							}
							
							saveTopTimes((currentPack < inJarPacks ? defaultPak[currentPack] : dlPacksNames[currentPack - inJarPacks]));
							
							recalcPackResults();
						}			

					}
			}
			
			/*if(cheatActive) {
				
				vibraInit(500);
				
				cheatActive = false;
				
				if(topTimes != null)
				{				
					for(int i = 0; i < 20; i++){
					
						if(topTimes[i][1] > 2) topTimes[i][1] = 2;
						if(topTimes[i][0] < 0) topTimes[i][0] = 4680;
					}
					
					saveTopTimes((currentPack < inJarPacks ? defaultPak[currentPack] : dlPacksNames[currentPack - inJarPacks]));
				}			
				
			}		*/	
			//#endif

			if (limpiaKeyMenu) {limpiaKeyMenu=false; intKeyMenu = 0;}

			try {
				biosTick();				
				if (gameForceRefresh) {gameForceRefresh = false; biosRefresh();}
								
				gameDraw();
												
				soundTick();
			} catch (Exception e) {
				//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
				System.out.println("*** Exception en la parte L�gica ***"); e.printStackTrace();
				//#endif
			}
		}

		gameSleep = (mainSleep - (int)(System.currentTimeMillis() - gameMilis));
		try	{Thread.sleep( gameSleep < 1? 1:gameSleep );} catch(Exception e) {}

//	System.gc();

	}

	soundStop();
	
	//biosDestroy();
	savePrefs();
	rmsDestroy();
	
	ga.destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Regla de 3
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public int regla3(int x, int min, int max) {return (x*max)/min;}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// RND - Engine
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
static int RND_Cont = (int)(System.currentTimeMillis()>>8 & 0x0F);
static int[] RND_Data = new int[5], RND_Original = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

public static int RND(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
}

public static void randomize(long seed)
{
	for(int i = 0; i < 5; i++) RND_Data[i] = RND_Original[i];
	RND_Cont = (int)(seed>>8 & 0x0F);	
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 >= x1+xs1 ) || ( x2+xs2 <= x1 ) || ( y2 >= y1+ys1 ) || ( y2+ys2 <= y1 ));
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public byte[] loadFile(String Nombre)
{
	/*System.gc();

	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] buffer = new byte[1024];

	try
	{
		int Size = 0;

		while (true)
		{
		int Desp = is.read(buffer, 0, buffer.length);
		if (Desp <= 0) {break;}
		Size += Desp;
		}

		is = null; System.gc();

		buffer = new byte[Size];

		is = getClass().getResourceAsStream(Nombre);
		Size = is.read(buffer, 0, buffer.length);

		while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}

		is.close();
	}
	catch(Exception exception) {}

	System.gc();*/
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	return bytes;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void loadFile(byte[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		is.read(buffer, 0, buffer.length);
		is.close();
	}catch(Exception e) {}
	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

/*public byte[] loadFile(String Nombre, int Pos, short[] Size)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] Dest = new byte[Size[Pos]];

	try {
		for (int i=0 ; i<Pos ; i++) {is.skip(Size[i]);}
		is.read(Dest, 0, Dest.length);
		is.close();
	} catch(Exception e) {}
	System.gc();

	return Dest;
}*/

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void loadFile(short[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		for (int i=0 ; i<buffer.length ; i++)
		{
		buffer[i]  = (short) (is.read() << 8);
		buffer[i] |= is.read();
		}
	is.close();
	} catch(Exception exception) {}

	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// *******************
// -------------------
// textos - Engine - v1.2 - Rev.4 (28.9.2004)
// ===================
// *******************

final static int TEXT_PLAY = 0;
final static int TEXT_CONTINUE = 1;
final static int TEXT_SOUND = 2;
final static int TEXT_VIBRA = 3;
final static int TEXT_MODE = 4;
final static int TEXT_GAMEOVER = 5;
final static int TEXT_HELP = 6;
final static int TEXT_ABOUT = 7;
final static int TEXT_RESTART = 8;
final static int TEXT_EXIT = 9;
final static int TEXT_LOADING = 10;
final static int TEXT_HELP_SCROLL = 11;
final static int TEXT_ABOUT_SCROLL = 12;
final static int TEXT_LEVEL = 13;
final static int TEXT_CONGRATULATIONS = 14;
final static int TEXT_SOFTKEYS = 15;



// -------------------
// textos Create
// ===================

public String[][] textosCreate(byte[] tex)
{
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;


	// MONGOFIX ==========================================
	char tex_char[] = new char[tex.length];
	for(int i = 0; i < tex.length; i++)
		if(tex[i] < 0)
			tex_char[i]=(char)(tex[i]+256);
		else
			tex_char[i]=(char)tex[i];
	//=================================================

	int size = 0;
	String textos = new String(tex_char);

	for (int i=0 ; i<textos.length() ; i++)
	{
		int letra = textos.charAt(i) & 0xff;

		if (campo)		// Estamos buscando el FINAL de un campo?
		{
			if (letra == 0x7D)		// '}' Final del campo?
			{
				subCampo = false;
			}

			if (letra < 0x20 || letra == 0x7C || letra == 0x7D)	// Buscamos cuando Termina un campo
			{
				data[dataPos + 1] = (short) i;				// Nos apuntamos el index final del campo
				dataPos += 2;

				campo = false;		// Pasamos a modo Buscar inicio de un campo
			}

		} else {	// Estamos buscando cuando EMPIEZA un campo.

			if (letra == 0x7D)	// '}'
			{
				subCampo = false;
			}
			else
			if (letra == 0x7B)	// '{'
			{
				dataBak = dataPos;		// Nos apuntamos el index para marcar numero de sub-campos en esta ID
//				data[dataPos++] = 0;	// Ponemos '0' sub-campos para esta ID
				dataPos++;
				subCampo = true;
				size++;					// Incrementamos una ID nueva

				primerEnter = true;		// Activamos ke debemos eskivar el primer Enter ke encontremos
			}
			else
			if (subCampo && letra == 0x0A)	// Campos vacios en los sub-campos?
			{
				if (primerEnter)			// Si es el primer enter lo eskivamos
				{
					primerEnter = false;

				} else {

//					data[dataPos++] = 0;	// Apuntamos index de la linea vacia
					dataPos++;
					data[dataPos++] = -1;	// Tama�o de 'enter'
					data[dataBak]--;	// Incrementamos numero de subcampos (negativo para poder identificarlo luego)
				}
			}
			else
			if (letra >= 0x20)	// Buscamos cuando Empieza un campo nuevo (Caracter ASCII valido)
			{
				campo = true;	// Pasamos a modo campo encontrado, buscar fin del campo.

				data[dataPos] = (short) i;	// Nos apuntamos el index inicial de campo

				if (!subCampo)	// Si estamos en un subCampo (una ID con varios campos):
				{
					size++;		// Incrementamos una ID nueva
				} else {
					data[dataBak]--;	// Incrementamos numero de subcampos (negativo para poder identificarlo luego)
				}

				primerEnter = true;		// Activamos ke debemos eskivar el primer Enter ke encontremos
			}
		}
	}

	// MONGOFIX ==========================================
	/*char textos_char[] = new char[textos.length];
	for(int i = 0; i < textos.length; i++)
		if(textos[i] < 0)
			textos_char[i]=(char)(textos[i]+256);
		else
			textos_char[i]=(char)textos[i];*/
	//=================================================

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
		int numCampos = data[dataPos];

		if (numCampos < 0)
		{
			numCampos *= -1;
			dataPos++;
		} else {
			numCampos = 1;
		}

		strings[i] = new String[numCampos];

		for (int t=0 ; t<numCampos ; t++)
		{
			int posIni = data[dataPos++];
			int posFin = data[dataPos++];
			strings[i][t] = ( posFin<0? " " : textos.substring(posIni, posFin) );
		}
	}

	return strings;
}

// <=- <=- <=- <=- <=-




// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------









// *******************
// -------------------
// bios - Engine
// ===================
// *******************

int biosStatus = 0;

boolean gameSound = true;
boolean gameVibra = true;

//#ifdef MENUPERIODICREFRESH
int refreshCnt = 0;
//#endif

// -------------------
// bios Create
// ===================

String[][] gameText;

//#ifndef NONETWORK
String[][] networkText;
//#endif

/*public void biosCreate()
{	
	gameCreate();
}*/

// -------------------
// bios Init
// ===================

public void biosInit()
{
	//#ifdef J2ME
	gameText = textosCreate( loadFile("/Textos.txt") );
			
	String versionStr = "MONGOJOCS";//TODO ga.getAppProperty("MIDlet-Version");
	gameText[TEXT_ABOUT_SCROLL][1] = (versionStr == null ? " " : gameText[TEXT_ABOUT_SCROLL][1]+versionStr);		
		
	//#ifndef NONETWORK
	networkText = textosCreate( loadFile("/Network.txt") );
	//#endif
		
	//#elifdef DOJA
	//#endif
	
	//#ifndef NONETWORK
	rmsCreate(10240);
	//#else
	//#endif
	loadPrefs();
}

// -------------------
// bios Destroy
// ===================

/*public void biosDestroy()
{
	savePrefs();
	rmsDestroy();
	//gameDestroy();
}*/

// -------------------
// bios Tick
// ===================

public void biosTick()
{
	
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
 	if (keyMisc == 11 && lastKeyMisc == 0) {Debug.enabled = !Debug.enabled; /*if (!com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug.enabled) {gameForceRefresh=true;}*/}
	//#endif
	
	switch (biosStatus)
	{
// --------------------
// game Bucle
// --------------------
	case BIOS_GAME:
		gameTick();
		//#ifdef FRAMESKIP
		//#endif
	return;
// --------------------

// --------------------
// logos Bucle
// --------------------
	case BIOS_LOGOS:
		if ( logosTick() ) {biosStatus = BIOS_GAME;}
	return;
// --------------------

// --------------------
// menu Bucle
// --------------------
	case BIOS_MENU:
	
		//#ifdef MENUPERIODICREFRESH
		refreshCnt++;
		if(refreshCnt % 13 == 0) {menuShow = true; playShow = true;}
		//#endif
		
		if ( menuTick() ) {biosStatus = BIOS_GAME;}
	return;
// --------------------
	}
}

// -------------------
// bios Refresh
// ===================

public void biosRefresh()
{
		
	switch (biosStatus)
	{
// --------------------
// game Refresh
// --------------------
	case BIOS_GAME:
		gameRefresh();
	return;
// --------------------

// --------------------
// logos Refresh
// --------------------
	case BIOS_LOGOS:
	return;
// --------------------

// --------------------
// menu Refresh
// --------------------
	case BIOS_MENU:
		menuInit(menuType);
		menuShow=true;		
	return;
// --------------------
	}
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// logos - Engine
// ===================
// *******************

int numLogos = 0;
int cntLogos = 0;
String[] nameLogos;
int[] rgbLogos;
int timeLogos;
long timeIniLogos;

/*public void logosInit(String[] fileNames, int[] rgb, int time)
{
	nameLogos = fileNames;
	numLogos = rgb.length;
	cntLogos = 0;
	rgbLogos = rgb;
	timeLogos = time;

	timeIniLogos = System.currentTimeMillis() - timeLogos;

	biosStatus = BIOS_LOGOS;
}*/


public boolean logosTick()
{

	if ( (System.currentTimeMillis() - timeIniLogos) < timeLogos) {return false;}

	if (cntLogos == numLogos) {canvasImg = null; /*MONGOFIX*/ return true;}

	canvasFillTask(rgbLogos[cntLogos]);

//#ifdef J2ME
	canvasImg = loadImage(nameLogos[cntLogos]);
//#elifdef DOJA
//#endif

	cntLogos++;

	timeIniLogos = System.currentTimeMillis();

	return false;
}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// softKey - Engine
// ===================
// *******************

// -------------------
// softKey Tick
// ===================

public void softKeyInit()
{
}

public void softKeyTick()
{
}

public void softKeyDraw()
{
}

// <=- <=- <=- <=- <=-









// *******************
// -------------------
// menu - Engine
// ===================
// *******************

boolean menuShow;
boolean menuExit;

Image menuImg;

// -------------------
// menu Tick
// ===================

public boolean menuTick()
{
	if ( (/*menuListMode == ML_SCROLL || */menuListMode == ML_SCREEN) && keyMenu == -1 && keyMenu != lastKeyMenu)
	{
	menuAction(-2);
	}
	else
	if ( menuListTick( (keyY!=lastKeyY? keyY:(keyX!=lastKeyX? keyX:0) ), keyMenu!=lastKeyMenu? keyMenu>0:false ) )
	{
	menuAction(menuListCMD);
	}

	if (menuExit) {menuType = -1; return true;}

	menuShow = true; menuBarShow = true;

	return false;
}

// <=- <=- <=- <=- <=-














// *******************
// -------------------
// menuList - Engine - Rev.0 (20.6.2003)
// ===================
// *******************

boolean menuList_ON=false;		// Estado del menuList Engine
boolean menuListShow=false;

int menuListMode;

int menuListPos;
int menuListCMD;

int menuListX;
int menuListY;
int menuListSizeX;
int menuListSizeY;

int menuListScrY;
int menuListScrSizeY;

int menuListBloIni;
int menuListBloSize;

Font menuListFont;

int fontHeight;

String	menuListStr[][];
int		menuListDat[][];

// -------------------
// menuList Init
// ===================

public void menuListInit(int x, int y, int sizeX, int sizeY)
{
	menuListX = x;
	menuListY = y;
	menuListSizeX = sizeX;
	menuListSizeY = sizeY;
}

// -------------------
// menuList Clear
// ===================

public void menuListClear()
{
	menuList_ON=false;
	menuListShow=false;

	menuListStr=null;
	menuListDat=null;

	menuListPos=0;
}

// -------------------
// menuList Add
// ===================

public void menuListAdd(int Dato, String Texto)
{
	Font f = menuListGetFont(Dato);

	if ( f.stringWidth(Texto) <= menuListSizeX)
	{
	menuListAdd(Dato, new String[] {Texto}, 0, 0);
	} else {

	String[] Textos = textBreak(Texto,menuListSizeX,f); //menuListCutText(Dato, Texto);

		for (int i=0 ; i<Textos.length ; i++)
		{
		menuListAdd(Dato, new String[] {Textos[i]}, 0, 0);
		}
	}
}


public void menuListAdd(int Dato, String[] Texto)
{
	for (int i=0 ; i<Texto.length ; i++)
	{
	menuListAdd(Dato, Texto[i]);
	}
}


public void menuListAdd(int Dato, String Texto, int Dat1)
{
	menuListAdd(Dato, new String[] {Texto}, Dat1, 0);
}

public void menuListAdd(int Dato, String[] Texto, int Dat1)
{
	menuListAdd(Dato, new String[] {Texto[0]}, Dat1, 0);
}

public void menuListAdd(int Dat0, String[] Texto, int Dat1, int Dat2)
{
	int Lines=(menuListDat!=null)?menuListDat.length:0;

	String Str[][] = new String[Lines+1][];
	int Dat[][] = new int[Lines+1][];

	int i=0;

	for (; i<Lines; i++)
	{
	Dat[i] = menuListDat[i];
	Str[i] = menuListStr[i];
	}

	Font f = menuListGetFont(Dat0);

	int TextoSizeX = 8;
	for (int SizeX, t=0 ; t<Texto.length ; t++) {SizeX=f.stringWidth(Texto[t]); if ( TextoSizeX < SizeX ) {TextoSizeX=SizeX;}}


	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2, TextoSizeX, f.getHeight() };
	
	//#ifdef SG-Z105
	//#endif

	menuListStr=Str;
	menuListDat=Dat;
}


// -------------------
// menuList Set
// ===================

public void menuListSet_Text()
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_TEXT;
}

/*public void menuListSet_Scroll()
{
	menuListScrY = menuListSizeY;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCROLL;


	menuListScrSizeY = 0; for (int i=0 ; i<menuListDat.length ; i++) {menuListScrSizeY += menuListDat[i][4];}

}

public void menuListSet_Option()
{
	menuListSet_Option(0);
}*/

public void menuListSet_Option()
{
	menuListPos=0;
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_OPTION;
}

public void menuListSet_Screen()
{
	menuList_ON=true;
	menuListShow=true;
	menuListMode = ML_SCREEN;



	menuListBloIni = 0;
	menuListBloSize = 0;

	int sizeY = 10;

	for (int i=0 ; i<menuListDat.length ; i++)
	{
	if ( (sizeY += menuListDat[i][4]) < menuListSizeY) {menuListBloSize++;} else {break;}
	}

}



// -------------------
// menuList Tick
// ===================

public boolean menuListTick(int movY, boolean fire)
{
	if (menuListMode == ML_OPTION)
	{
		if (movY ==-1 && menuListPos > 0) {menuListPos--; menuListShow=true;}
		else
		if (movY == 1 && menuListPos < menuListDat.length-1) {menuListPos++; menuListShow=true;}
		else
		if (movY ==-1 && menuListPos == 0) {menuListPos = menuListDat.length-1; menuListShow=true;}
		else
		if (movY == 1 && menuListPos == menuListDat.length-1) {menuListPos = 0; menuListShow=true;}

		if (fire)
		{
		if (++menuListDat[menuListPos][2] == menuListStr[menuListPos].length) {menuListDat[menuListPos][2]=0;}
		menuListCMD=menuListDat[menuListPos][1];
		menuListShow=true;
		return true;
		}
	}


	/*if (menuListMode == ML_SCROLL)
	{
		menuListScrY--;

		if (menuListScrY < -menuListScrSizeY) {menuListCMD=-2; return true;}
		if (fire) {menuListCMD=-3; return true;}

		menuListShow=true;
	}*/




	if (menuListMode == ML_SCREEN)
	{
		if (fire || movY != 0)
		{
			menuListBloIni += menuListBloSize;
			menuListBloSize = 0;

			int sizeY = 0;

			while (menuListBloIni < menuListDat.length && menuListStr[menuListBloIni][0] == " ") {menuListBloIni++;}

			for (int i=menuListBloIni ; i<menuListDat.length ; i++)
			{
			if ( (sizeY += menuListDat[i][4]) < menuListSizeY) {menuListBloSize++;} else {break;}
			}

			int fin = (menuListBloIni + menuListBloSize)-1;

			while (fin > menuListBloIni && menuListStr[fin--][0] == " ") {menuListBloSize--;}


			if (menuListBloSize == 0) {menuListCMD=-2; return true;}

			menuListShow=true;
		}
	}


	return false;
}


// -------------------
// menuList Opt
// ===================

/*public int menuListOpt()
{
	return(menuListDat[menuListPos][2]);
}*/


// -------------------
// menuList Draw
// ===================

public void menuListDraw(Graphics g)
{
	if (!menuList_ON || !menuListShow) {return;}

	menuListShow = false;

	if (menuImg!=null) {showImage(menuImg);}
	
	
	if(menuType == MENU_QUIT_ASK || menuType == MENU_RESTART_ASK) 
		textDraw(gameText[18][1],0,0,RGB_LIGHT, TEXT_HCENTER | TEXT_VCENTER | TEXT_BOXED | TEXT_OUTLINE);		

//#ifdef J2ME
  
  	if(menuListFont == null)
	menuListFont = Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , 
	
	//#ifdef LARGEMENUFONT
	//#else
	Font.SIZE_SMALL 
	//#endif
	);
  
//#elifdef DOJA
//#endif

	fontHeight = menuListFont.getHeight();
	
	//#ifdef SG-Z105
	//#endif

	g.setFont(menuListFont);
	
	//#ifdef SAMSUNGSTRINGDRAW
	//#endif

	switch (menuListMode)
	{
	case ML_OPTION:
		
		String[] texto =		
		//#ifdef NOARROWSATMENU
		//#else
		textBreak(menuListStr[menuListPos][menuListDat[menuListPos][2]],menuListSizeX- 10 - menuListFont.stringWidth(">  <"),menuListFont); 
		//#endif
		
		//#ifdef MENUVERSIONA
		int margin = 10;
		//#else
		//#endif

		int height = (texto.length * menuListDat[menuListPos][4]);
    	//int y = menuListY + (( menuListSizeY / 4) * 3) - (height/2);
		//int y = menuListY +  menuListSizeY - menuListFont.getHeight() - margin;
		int y = canvasHeight - fontHeight*texto.length;
				
		//#ifdef MENUVERSIONC
		//#else
		y -= fontHeight/2;
		standardBar(texto.length + 1, false, false, true, false);
		//#endif
				
		//#ifdef J2ME
		g.setClip(0,0,canvasWidth, canvasHeight);
		//#endif
		
		for (int i=0 ; i<texto.length ; i++)
		{
			//#ifdef NOARROWSATMENU
			//#else
			menuListTextDraw(g, 0, (i == 0 ? "> " : "")+texto[i]+(i == texto.length - 1 ? " <" : ""), 0,y,TEXT_HCENTER | TEXT_OUTLINE);
			//#endif

			y += menuListDat[menuListPos][4];
		}
	break;


	/*case ML_SCROLL:

		y = menuListScrY;

		for (int i=0 ; i<menuListStr.length ; i++)
		{
			menuListTextDraw(g, 0, menuListStr[i][0], 0,y,TEXT_HCENTER | TEXT_BOXED);

			y += menuListDat[i][4];
		}
	break;*/


	case ML_SCREEN:

		y = 0;

		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			y += menuListDat[i][4];
		}

		y = (menuListSizeY - y) / 2;
		
		int bw = 0, bh = 0;
		
		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			//if(menuListFont.stringWidth(menuListStr[i][0]) > bw) bw = menuListFont.stringWidth(menuListStr[i][0]);
			bw = menuListSizeX;
			bh = fontHeight * menuListBloSize + 8;			 
		}
		bw += 8; bh += 8;
		
		drawTextBox((canvasWidth-bw)/2,y - 4,bw,bh);				
		drawBoxButtons(true,true,true,3,(menuListBloIni+menuListBloSize < menuListStr.length ? 4 : 2),2+(canvasWidth-bw)/2,y - 2,bw-4,bh-4);
		
		//#ifdef J2ME
		scr.setClip(0,0,canvasWidth,canvasHeight);
		//#endif
								
		for (int i=menuListBloIni ; i<menuListBloIni+menuListBloSize ; i++)
		{
			menuListTextDraw(g, 0, menuListStr[i][0], 0,y, TEXT_OUTLINE | TEXT_HCENTER);

			y += menuListDat[i][4];
					
		}						
		
	break;
	}
}






public void menuListTextDraw(Graphics g, int format, String str, int x, int y, int mode)
{
	x = menuListX + (( menuListSizeX - menuListFont.stringWidth(str) ) / 2);
	x = 0;

	textDrawNoCut(str, x, y, 0xFFFFFF, mode);

}






/*
public String[] menuListCutText(int Dato, String Texto)
{
	String[] str = new String[0];

	int Pos=0, PosIni=0, PosOld=0, Size=0;

	Font f = menuListGetFont(Dato);

	while ( PosOld < Texto.length() )
	{
	Size=0;

	Pos=PosIni;

	while ( Size < (menuListSizeX) )
	{
		if ( Pos == Texto.length() ) {PosOld=Pos; break;}

		int Dat = Texto.charAt(Pos++);
		if (Dat==0x20) {PosOld=Pos-1;}

		Size += f.stringWidth(new String(new char[] {(char)Dat}));
	}

	if (PosOld-PosIni < 1) { while ( Pos < Texto.length() && Texto.charAt(Pos) >= 0x30 ) {Pos++;} PosOld=Pos; }



	int Lines=(str!=null)?str.length:0;

	String newStr[] = new String[Lines+1];

	int i=0;

	for (; i<Lines; i++)
	{
	newStr[i] = str[i];
	}

	str = newStr;

	newStr = null;

	str[i] = Texto.substring(PosIni,PosOld);

	PosIni=PosOld+1;

	}

	return str;
}
*/
// <=- <=- <=- <=- <=-




// *******************
// -------------------
// wait - Engine
// ===================
// *******************

/*long waitMillis;

// -------------------
// wait Start - Guardamos Tiempo ACTUAL para posteriores usos...
// ===================

public void waitStart()
{
	waitMillis = System.currentTimeMillis();
}

// -------------------
// wait Stop - Hacemos una espera tomando como tiempo inicial cuando se llamo a "waitStart()"
// ===================

public void waitStop(int time)
{
	int wait = time - ((int)(System.currentTimeMillis() - waitMillis));
	if (wait > 0) {waitTime(wait);}
}

// -------------------
// wait Finished - Devuelve "true" si ha pasado el tiempo especificado desde que se llamo a "waitStart()"
// ===================

public boolean waitFinished(int time)
{
	return (time - ((int)(System.currentTimeMillis() - waitMillis))) <= 0;
}

// -------------------
// wait Time - Hacemos una espera del tiempo especificado
// ===================

public void waitTime(int time)
{
	try {
		Thread.sleep(time);
	} catch (Exception e) {}
}
*/
// <=- <=- <=- <=- <=-




// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

// -------------------
// prefs Update - Leemos/Salvamos archivo de preferencias, resultado: byte[] que grabamos la ultima vez o "null" si NO Existe
// ===================

// Leemos cuando le pasamos "null" como paremetro.
// Salvamos cuando le pasamos "byte[]" como parametro.

public byte[] updatePrefs(byte[] bufer)
{
//#ifdef J2ME

	RecordStore rs;

	try {

		rs = RecordStore.openRecordStore("prefs", true);

		if (bufer != null && bufer.length > 0)
		{
			if (rs.getNumRecords() == 0)
			{
				rs.addRecord(bufer, 0, bufer.length);
			} else {
				rs.setRecord(1, bufer, 0, bufer.length);
			}
		} else {
			if (rs.getNumRecords() == 0)
			{
			bufer = null;
			} else {
			bufer = rs.getRecord(1);
			}
		}

		rs.closeRecordStore();

	} catch(Exception e) {return null;}

	return bufer;

//#elifdef DOJA
//#endif
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
// Final Clase Bios
// **************************************************************************//




// **************************************************************************//
// Inicio Clase com.mygdx.mongojocs.sanfermines2006.Game
// **************************************************************************//

// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// ===========================================



// Variables staticas para identificar los textos, libres desde el 16...
final static int TEXT_ = 16;







public Font menuListGetFont(int Bits)
{
//#ifdef J2ME
	return Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , 
	//#ifdef LARGEMENUFONT
	//#else
	Font.SIZE_SMALL 
	//#endif
	);
//#elifdef DOJA
//#endif
}







// *******************
// -------------------
// game - Engine
// ===================
// *******************


final static int GAME_LOGOS = 0;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_PACK_SELECT = 15;
final static int GAME_MENU_LEVEL_SELECT = 130;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
final static int GAME_PLAY = 20;
final static int GAME_LEVEL_COMPLETE = 100;
final static int GAME_TIME_OVER = 110;
final static int GAME_MENU_RETRY_ASK = 120;
final static int GAME_MENU_NEXT_ASK = 180;
final static int GAME_SHOW_TIP = 140;
final static int GAME_LOADING_PACK = 150;
final static int GAME_LOADING_LEVEL = 160;
final static int GAME_LOADING_INITIAL = 170;
final static int GAME_PACK_COMPLETE = 190;

//#ifndef NONETWORK
final static int GAME_MENU_NETWORK_ERROR = 200;
final static int GAME_MENU_INPUT_RECORD_NAME = 210;
final static int GAME_MENU_PACK_GET_LIST = 220;
final static int GAME_MENU_PACK_DOWNLOAD_SELECT = 230;
final static int GAME_MENU_PACK_DOWNLOAD_INSTRUCTIONS = 240;
final static int GAME_MENU_DOWNLOAD_ENTER_CODE  = 250;
final static int GAME_MENU_INPUT_DOWNLOAD_CODE = 260;
final static int GAME_MENU_SEE_NETWORK_LEVEL_RECORDS = 270;
final static int GAME_MENU_SEE_NETWORK_PACK_RECORDS = 310;
final static int GAME_MENU_DOWNLOAD_PACK = 280;
final static int GAME_MENU_DOWNLOAD_CODE_ERROR = 290;
final static int GAME_MENU_NETWORK_OK = 300;
//#endif


int gameStatus = 0, lastGameStatus = 0;

// -------------------
// game Create
// ===================

/*public void gameCreate()
{
}*/

// -------------------
// game Destroy
// ===================

/*public void gameDestroy()
{
}*/

// -------------------
// game Tick
// ===================

public void gameTick()
{
	
	boolean anyKey = (keyMenu != 0 && keyMenu != lastKeyMenu) || (keyX != 0 && keyX != lastKeyX) 
				  || (keyY != 0 && keyY != lastKeyY) || (keyR != 0 && keyR != lastKeyR) || 
				  (keyMisc != 0 && keyMisc != lastKeyMisc);
				  
	lastGameStatus = gameStatus;
		
	switch (gameStatus)
	{
	case GAME_LOGOS:
			
		//#ifdef J2ME
		nameLogos = new String[] {"/Microjocs.png"};
		//#elifdef DOJA
		//#endif
		numLogos = 1;
		cntLogos = 0;
		rgbLogos = new int[] {0xffffff};
		timeLogos = 2000;
	
		timeIniLogos = System.currentTimeMillis() - timeLogos;
	
		biosStatus = BIOS_LOGOS;
		
		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:
		playRelease();
		playDestroy();
		loadPicture();
		menuImg = pictureImg;
		soundPlay(0,0);
		menuInit(MENU_MAIN);		
	break;
		
	case GAME_MENU_PACK_SELECT:		
		//#ifndef NONETWORK
			if(keyMenu > 0 && keyMenu != lastKeyMenu)
			{	
				if(currentPack == inJarPacks + dlPacks)	
					// Download levels
					gameStatus = GAME_MENU_PACK_GET_LIST;
					
				else
				{	
					// Play the pack that is already downloaded or included originally in JAR			
					if(currentPack < inJarPacks || dlPacksIds[currentPack - inJarPacks] == storedPackId)
						gameStatus = GAME_LOADING_PACK;	
						
					else
					{
						requestedPackId = dlPacksIds[currentPack - inJarPacks];					
						gameStatus = GAME_MENU_DOWNLOAD_PACK;
					}
				}
			}			
			if(keyMenu < 0 && keyMenu != lastKeyMenu)
				gameStatus = GAME_MENU_MAIN;
			//#ifdef MENUVERSIONA
			if(keyY > 0 && keyY != lastKeyY)
				currentPack = (currentPack + 1)%(inJarPacks + dlPacks + 1);
			if(keyY < 0 && keyY != lastKeyY)
				currentPack = (inJarPacks + dlPacks + currentPack)%(inJarPacks + dlPacks + 1);
			//#else
			//#endif
		//#else
		//#endif
		//if(keyMenu != 0 || keyY != 0 || keyX != 0) playShow = true;
		if(keyY != lastKeyY && keyY != 0 || keyX != lastKeyX && keyX != 0) playShow = true;
		
	break;	
	
	case GAME_LOADING_PACK :
	//#ifdef NOSOUNDWHILELOAD
	//#endif
	playCreate();
	currentLevel = 0;
	
	playShow = true;
			
	if(topTimes[0][1] == 3) gameStatus = GAME_PLAY;			
	else gameStatus = GAME_MENU_LEVEL_SELECT;
	break;
	
	case GAME_MENU_LEVEL_SELECT:
		packJewels = 0;
		for(int i = 0; i < 20; i++)	
			packJewels += topTimes[i][2];
			
		//#ifndef NONETWORK
				
			if(keyMenu > 0 && keyMenu != lastKeyMenu)
			{
			
				if(currentLevel == 23)
				{							
					gameStatus = GAME_MENU_PACK_SELECT;
				}
				else if(currentLevel == 22)
				{							
					gameStatus = GAME_MENU_SEE_NETWORK_PACK_RECORDS;
				}				
				else if(currentLevel == 21)
				{							
					// Send records																
					
					gameStatus = GAME_MENU_INPUT_RECORD_NAME;
					
					ga.inputDialogCreate(networkText[0][0], networkText[2][0], networkText[2][1], 10);
					ga.inputDialogInit();
					
				}
				
				else if(currentLevel == 20)
				{
					if(topTimes[19][1] < 3)	gameStatus = GAME_PACK_COMPLETE;
				}
				
				else if (currentLevel == 0 ? true : topTimes[currentLevel-1][1] < 3)								
					gameStatus = GAME_PLAY;
			}			
			
			if(keyMenu < 0 && keyMenu != lastKeyMenu)			
				gameStatus = GAME_MENU_PACK_SELECT; 
			
			//#ifdef MENUVERSIONA
			if(keyY > 0 && keyY != lastKeyY)
				currentLevel = (currentLevel + 1)%24;
			if(keyY < 0 && keyY != lastKeyY)
				currentLevel = (24 + currentLevel - 1)%24;
			//#else
			//#endif
					
		//#else
		//#endif
		if(keyY != lastKeyY && keyY != 0 || keyX != lastKeyX && keyX != 0) playShow = true;
	break;


	case GAME_PLAY:				
		gameStatus++;

	case GAME_PLAY+1:
		//#ifdef NOSOUNDWHILELOAD
		//#endif
		playRelease();
		playInit();
		soundStop();		
		playExit=0;
		gameStatus++;
	break;
	
	case GAME_PLAY+2:
		if(anyKey)
		{
			if(keyMenu < 0)
			{				
				gameStatus = GAME_MENU_LEVEL_SELECT;
			}
			else
			{				 
				 gameStatus = GAME_SHOW_TIP;				 
			}
		}		
	break;
	
	case GAME_SHOW_TIP :		
		menuInit(MENU_SHOW_TIP);		
	break;
		
	case GAME_PLAY+3:
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}
				
		if ( !playTick() ) {break;}
						
		gameStatus--;

		switch (playExit)
		{
		case 1:	// Pasar de Nivel			
			gameStatus = GAME_LEVEL_COMPLETE;
		break;

//		case 2:	// Una vida menos
//		break;

		case 3:	// Producir com.mygdx.mongojocs.sanfermines2006.Game Over
			gameStatus = GAME_TIME_OVER;
		break;
		}

		playExit=0;
	break;

	case GAME_MENU_SECOND:

		menuInit(MENU_SECOND);
		gameStatus = GAME_PLAY+3; menuBarShow = true;
	break;
	
	case GAME_LEVEL_COMPLETE:
		soundPlay(1,1);
		myTime = (short)ellapsedTicks;						
		
		if(myTime <= packTimes[currentLevel][0]) myClass = 0;
		else if(myTime <= packTimes[currentLevel][1]) myClass =  1;
		else myClass =  2;
		
		boolean needSave = false;
		
		if(!cheatUsed)						
		{
			if(myTime < topTimes[currentLevel][0] || topTimes[currentLevel][1] == 3)
			{
				topTimes[currentLevel][0] = myTime;
				topTimes[currentLevel][1] = myClass;
				needSave = true;
			}
			
			if(pickedJewels > topTimes[currentLevel][2])
			{
				topTimes[currentLevel][2] = pickedJewels;
				needSave = true;
			}						
		}	
		
		recalcPackResults();
		
		loadingShow = true; gameDraw();
		
		if(needSave) saveTopTimes((currentPack < inJarPacks ? defaultPak[currentPack] : dlPacksNames[currentPack - inJarPacks]));
		
		gameStatus++;		
	break;
	
	case GAME_LEVEL_COMPLETE +1:
	
		if(anyKey)
		{			
			gameStatus = GAME_MENU_NEXT_ASK;			
		}			
	break;
	
	case GAME_PACK_COMPLETE:
				
		if(anyKey)
		{			
			gameStatus = GAME_MENU_LEVEL_SELECT;			
		}			
	break;
	
	case GAME_TIME_OVER:
		soundPlay(2,1);
		playShow = true; menuShow = true; gameDraw();
		gameStatus++;		
	break;
	
	case GAME_TIME_OVER +1:
	
		menuShow = true;
		
		//#ifdef MENUPERIODICREFRESH
		refreshCnt++;
		if(refreshCnt % 13 == 0) {playShow = true;}
		//#endif		
		
		if(anyKey)
		{
			gameStatus = GAME_MENU_RETRY_ASK;
		}			
	break;
	
	case GAME_MENU_RETRY_ASK:
		menuInit(MENU_RETRY_ASK);
	break;
	
	case GAME_MENU_NEXT_ASK:
		menuInit(MENU_NEXT_ASK);
	break;
	
	//#ifndef NONETWORK	
	case GAME_MENU_NETWORK_ERROR:
		menuInit(MENU_NETWORK_ERROR);		
	break;
	
	//case GAME_MENU_INPUT_RECORD_NAME:
	//break;
	
	case GAME_MENU_INPUT_RECORD_NAME +1:			
						
		// Send the records		
		networkCommand(MCCONTROLLER_SCORES,MCCLIENT_REPORT_SCORE,MCSERVER_SCORE_OK);
	break;
	
	case GAME_MENU_SEE_NETWORK_PACK_RECORDS :
	case GAME_MENU_SEE_NETWORK_LEVEL_RECORDS :
				
		networkCommand(MCCONTROLLER_SCORES,MCCLIENT_GET_SCORES,MCSERVER_SCORES);
	break;
		
	case GAME_MENU_PACK_GET_LIST :
				
		networkCommand(MCCLIENT_GET_PACK_LIST,MCCLIENT_GET_PACK_LIST,MCSERVER_PACK_LIST);
	break;
	
	case GAME_MENU_PACK_DOWNLOAD_SELECT :
		menuInit(MENU_PACK_DOWNLOAD_SELECT);
	break;
	
	case GAME_MENU_INPUT_DOWNLOAD_CODE:
	break;
	
	case GAME_MENU_INPUT_DOWNLOAD_CODE +1:
			
		// Send the code		
		networkCommand(MCCONTROLLER_PACKS,MCCLIENT_GET_PACK,MCSERVER_PACK);
	break;
	
	case GAME_MENU_DOWNLOAD_PACK:
	
		// Send the code		
		networkCommand(MCCONTROLLER_PACKS,MCCLIENT_GET_PACK_ALREADY_PAID,MCSERVER_PACK);
	break;
	
	case GAME_MENU_DOWNLOAD_CODE_ERROR:	
	if(anyKey)
	{
		gameStatus = GAME_MENU_MAIN;
	}	
	break;
	//#endif

	}
}

// -------------------
// game Refresh
// ===================

public void gameRefresh()
{

	switch (gameStatus)
	{
	case GAME_MENU_MAIN :
	soundPlay(0,0);
	break;
		
	case GAME_PLAY+3:
	gameStatus = GAME_MENU_SECOND;
	break;
	}
	
	playShow = true;

}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

byte[] prefsData;

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{

// Cargamos un byte[] con las ultimas prefs grabadas

	prefsData = updatePrefs(null);	// Recuperamos byte[] almacenado la ultima vez

// Si es null es que nunca se han grabado prefs, por lo cual INICIALIZAMOS las prefs del juego

	if (prefsData == null)
	{
		try{
			
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	    	DataOutputStream ostream = new DataOutputStream(bstream);
	    		    					
			ostream.writeLong(0);
															
			ostream.writeBoolean(true);
			ostream.writeBoolean(true);
					
			ostream.writeInt(0);					
			ostream.writeInt(0);					

			//#ifndef STORETIMESONPREFS
	    	//ostream.flush();
	    	//ostream.close();
	    
	    	//prefsData = bstream.toByteArray();
	    	//#endif
	    	
	    	for(int i = 0; i < 20; i++)
	    	{
	    		topTimes[i][0] = -1;
	    		topTimes[i][1] = 3;
	    		topTimes[i][2] = 0;
	    		
	    	}
						
			//#ifdef STORETIMESONPREFS
			for(int i = 0; i < 60; i++)
				ostream.writeShort(topTimes[i/3][i%3]);
			//#else
			//for(int i = 0; i < defaultPak.length; i++)
	    	//	saveTopTimes(defaultPak[i]);
			//#endif
			
			//#ifdef STORETIMESONPREFS
	    	ostream.flush();
	    	ostream.close();
	    
	    	prefsData = bstream.toByteArray();
	    	//#endif
			
								    	    		    
    	}catch (Exception e){  }

	}

// Actualizamos las variables del juego segun las prefs leidas / inicializadas
	
	try
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(prefsData,0,prefsData.length));
		
		userID = dis.readLong();
		
		gameSound = dis.readBoolean();
		gameVibra = dis.readBoolean();
		
		dlPacks = dis.readInt();
		storedPackId = dis.readInt();		
		
		dlPacksIds = new int[dlPacks];		
		dlPacksNames = new String[dlPacks];
						
		for(int i = 0; i < dlPacks; i++)
		{
			dlPacksIds[i] = dis.readInt();		
			dlPacksNames[i] = dis.readUTF();
		}
		
		//#ifdef STORETIMESONPREFS
		topTimes = new short[20][3];
		
		for(int i = 0; i < 60; i++)			
			topTimes[i/3][i%3] = dis.readShort();
		//#endif
				
												
	}catch (Exception e){  }
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{

	// Ponemos las varibles del juego a salvar como prefs.
	
	try
	{

		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	    DataOutputStream ostream = new DataOutputStream(bstream);				
										
		ostream.writeLong(userID);
															
		ostream.writeBoolean(gameSound);
		ostream.writeBoolean(gameVibra);
				
		ostream.writeInt(dlPacks);
		ostream.writeInt(storedPackId);
		
		for(int i = 0; i < dlPacks; i++){
			ostream.writeInt(dlPacksIds[i]);
			ostream.writeUTF(dlPacksNames[i]);
		}
		
		//#ifdef STORETIMESONPREFS
		for(int i = 0; i < 60; i++)
			ostream.writeShort(topTimes[i/3][i%3]);
		//#endif
								
	    ostream.flush();
	    ostream.close();
	    
	    prefsData = bstream.toByteArray();
	    
	}catch (Exception e){  }	

// Almacenamos las prefs

	updatePrefs(prefsData);		// Almacenamos byte[]
}

// <=- <=- <=- <=- <=-

public void startupPackTimes(byte[] packBuffer)
{
	totalTimes[0] = totalTimes[1] = totalTimes[2] = 0;
	
	try{
	
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packBuffer,0,packBuffer.length));
		
		dis.readUTF();
		dis.readByte();
			
		for(int i = 0; i < 60; i++)				
		{							
			packTimes[i/3][i%3] = dis.readShort();					
			totalTimes[i%3] += packTimes[i/3][i%3];
		}
		
	}catch (Exception e){  }	
}

void recalcPackResults()
{
	packJewels = 0; packTime = 0;
	
	for(int i = 0; i < 20; i++){
	
		packJewels += topTimes[i][2];
		packTime += topTimes[i][0];		
	}
}


// *******************
// -------------------
// menu - Engine
// ===================
// *******************

int menuType = -1;
int menuTypeBack;

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	if(menuType != type) menuTypeBack = menuType;
	menuType = type;

	menuExit = false;

	//#ifdef MENUVERSIONC
	//#else
	menuListInit(10, 0, canvasWidth - 20, canvasHeight - 12);
	//#endif

	switch (type)
	{
	case MENU_MAIN:
		menuListClear();
 		menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY); 			
	//#ifndef PLAYER_NONE
		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_QUIT_CONFIRM);
		menuListSet_Option();
	break;
	
	case MENU_SHOW_TIP:
		loadingShow = true; gameDraw(); loadingShow = true;
		menuListClear();
		menuListAdd(0, (currentPack == 0 && gameText[25][currentLevel].length() > 2 ? gameText[25][currentLevel] : gameText[21][0]));
		menuListSet_Screen();
	break;

	case MENU_SECOND:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
		menuListAdd(0, gameText[4][0], MENU_ACTION_RETRY_CONFIRM);
		menuListAdd(0, gameText[4][1], MENU_ACTION_BACK_TO_LEVEL_SELECT);
		if(packJewels >= 20) menuListAdd(0, gameText[24][0], MENU_ACTION_CHEAT_MENU);
	//#ifndef PLAYER_NONE
		menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);			
		menuListAdd(0, gameText[TEXT_RESTART][0].toUpperCase(), MENU_ACTION_RESTART_CONFIRM);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_QUIT_CONFIRM);
		menuListSet_Option();
	break;


	case MENU_SCROLL_HELP:
		loadingShow = true; gameDraw();
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Screen();
	break;


	case MENU_SCROLL_ABOUT:
		loadingShow = true; gameDraw();
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Screen();		
	break;
			
	case MENU_RETRY_ASK:
		menuListClear();
		menuListAdd(0, gameText[18][2], MENU_ACTION_RETRY_CONFIRM);
		menuListAdd(0, gameText[18][3], MENU_ACTION_BACK_TO_LEVEL_SELECT);
		menuListSet_Option();		
	break;
	
	case MENU_NEXT_ASK:
		menuListClear();
		if(topTimes[19][1] < 3) menuListAdd(0,gameText[23][2].toUpperCase(), MENU_ACTION_PACK_RESULTS);
		if(currentLevel < 19) menuListAdd(0, gameText[23][0], MENU_ACTION_NEXT_LEVEL);		
		menuListAdd(0, gameText[23][1], MENU_ACTION_RETRY_CONFIRM);
		//#ifndef NONETWORK
		menuListAdd(0, networkText[0][1], MENU_ACTION_RETRIEVE_NETWORK_RECORDS);
		//#endif		
		menuListAdd(0, gameText[8][0].toUpperCase(), MENU_ACTION_RESTART);
		menuListSet_Option();		
	break;
	
	case MENU_RESTART_ASK:
		menuListClear();
		menuListAdd(0, gameText[18][3], -2);
		menuListAdd(0, gameText[18][2], MENU_ACTION_RESTART);
		menuListSet_Option();		
	break;
	
	case MENU_QUIT_ASK:
		menuListClear();
		menuListAdd(0, gameText[18][3], -2);
		menuListAdd(0, gameText[18][2], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();		
	break;
	
	//#ifndef NONETWORK
	case MENU_NETWORK_ERROR:
	loadingShow = true; gameDraw();
	menuListClear();
	menuListAdd(0, networkText[1]);
	menuListSet_Screen();
	break;
	
	case GAME_MENU_PACK_GET_LIST:
	loadingShow = true; gameDraw();
	menuListClear();
	menuListAdd(0, networkText[5]);
	menuListSet_Screen();
	break;
	
	case MENU_NETWORK_RECORDS:
	loadingShow = true; gameDraw();
	menuListClear();	
	if(bestPlayers.length < 1) 
		menuListAdd(0, networkText[4][0]);
	else
	{	
		for(int i = 0; i < bestPlayers.length; i++)
		{	
			menuListAdd(0, (i+1)+"-"+bestPlayers[i]+": "+timeString(bestTimes[i]));
			menuListAdd(0, gameText[15][0]);
			//menuListAdd(0, timeString(bestTimes[i]));			
		}
						
		menuListAdd(0, (playerOwnTime != 0 ?
							networkText[0][4]+" "+timeString(playerOwnTime) :
							networkText[6][0])
		);
		
		//menuListAdd(0, timeString(playerOwnTime));
	}
	menuListSet_Screen();	
	break;
	
	case MENU_PACK_DOWNLOAD_SELECT:
	menuListClear();
	for(int i = 0; i < serverPacks.length; i++)
		menuListAdd(0, serverPacks[i]+(packPaid[i] ? networkText[3][2] : ""), 
					(packPaid[i] ? MENU_ACTION_FORCE_PACK_REDOWNLOAD : MENU_ACTION_PACK_DOWNLOAD_INSTRUCTIONS));
	menuListAdd(0, networkText[3][1], MENU_ACTION_DOWNLOAD_ENTER_CODE);
	menuListAdd(0, gameText[TEXT_RESTART][0].toUpperCase(), MENU_ACTION_RESTART);
	menuListSet_Option();
	break;
	
	case MENU_PACK_DOWNLOAD_INSTRUCTIONS:
	loadingShow = true; gameDraw();
	menuListClear();
	menuListAdd(0, serverInfoText);	
	menuListSet_Screen();
	break;
	//#endif
	
	case MENU_CHEATS :
	menuListClear();
	for(int i = 0; i < 8; i++)
		menuListAdd(0, gameText[24][i + 1], 15 + i);
	/*menuListAdd(0, gameText[24][1], 15);
	menuListAdd(0, gameText[24][2], 16);
	menuListAdd(0, gameText[24][3], 17);
	menuListAdd(0, gameText[24][4], 18);
	menuListAdd(0, gameText[24][5], 19);
	menuListAdd(0, gameText[24][6], 20);
	menuListAdd(0, gameText[24][7], 21);
	menuListAdd(0, gameText[24][8], 22);*/
	menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
	menuListSet_Option();
	break;

	}

	biosStatus = BIOS_MENU;
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{

	switch (cmd)
	{
	case -3: // Scroll o Screen ha sido cortado por usuario
	case -2: // Scroll o Screen ha llegado al final
		if(menuType == MENU_SHOW_TIP)
		{
				menuExit = true;
				gameStatus = GAME_PLAY+3; menuBarShow = true;
				menuType = -1;
		}
		//#ifndef NONETWORK
		else if(menuType == MENU_NETWORK_ERROR)
		{
			menuExit = true;					
			gameStatus = GAME_MENU_MAIN;
		}else if(menuType == MENU_NETWORK_RECORDS)
		{			
			bestPlayers = null;
			bestTimes = null;	
			menuExit = true;
			gameStatus = (gameStatus == GAME_MENU_SEE_NETWORK_LEVEL_RECORDS ? GAME_MENU_NEXT_ASK : GAME_MENU_LEVEL_SELECT);
		}
		//#endif
		else menuInit(menuTypeBack);
	break;


	case MENU_ACTION_PLAY:		// Jugar de 0			
		menuImg = null;	
		//destroyPicture();
		//#ifndef INITIALLOAD
		pictureImg = null;			
		//#endif
		currentPack = 0;		
		menuExit = true;
	  	playShow = true;	
		
		//#ifndef NONETWORK
		gameStatus = GAME_MENU_PACK_SELECT;		
		//#else
		//if(dlPacks + inJarPacks == 1) gameStatus = GAME_LOADING_PACK;
		//else gameStatus = GAME_MENU_PACK_SELECT;
		//#endif
		
	break;
	
	case MENU_ACTION_CONTINUE:	// Continuar		
		menuExit = true; 
		menuBarShow = true;
	break;


	case MENU_ACTION_SHOW_HELP:		// Mostramos Instrucciones...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// Mostramos About...

		menuInit(MENU_SCROLL_ABOUT);
	break;



	case MENU_ACTION_RESTART:	// Provocamos GAME OVER desde menu
		
		//#ifndef NONETWORK
		serverInfoText = null; serverPacks = null; downloadCode = null;		
		serverPacksIds = null; packPaid = null;				
		//#endif
		
		//listenerInit(gameText[TEXT_SOFTKEYS][1],gameText[TEXT_SOFTKEYS][4]);	// " "," "
		playExit = 3;		// flag para SALIDA de juego tipo GAME OVER
		menuExit = true;
		gameStatus = GAME_MENU_MAIN;		
	break;
	
	case MENU_ACTION_BACK_TO_LEVEL_SELECT:
		menuExit = true;
		gameStatus = GAME_MENU_LEVEL_SELECT;
	break;

	case MENU_ACTION_EXIT_GAME:	// Exit com.mygdx.mongojocs.sanfermines2006.Game

		//listenerInit(gameText[TEXT_SOFTKEYS][1],gameText[TEXT_SOFTKEYS][4]);	// " "," "
		gameExit = true;
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListDat[menuListPos][2] != 0;
		if (!gameSound) soundStop(); else {
			if (menuType == 0) soundPlay(0,0);
			else soundPlay(1,1);
		}
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListDat[menuListPos][2] != 0; if(gameVibra) vibraInit(250);
	break;
		
	case MENU_ACTION_RETRY_CONFIRM:
		menuExit = true;				
		gameStatus = GAME_PLAY;
	break;
	
	case MENU_ACTION_NEXT_LEVEL:
		menuExit = true; currentLevel = (currentLevel+1)%20; gameStatus = GAME_PLAY +1;
	break;
	
	case MENU_ACTION_PACK_RESULTS:
		menuExit = true; gameStatus = GAME_PACK_COMPLETE;
	break;
	
	case MENU_ACTION_CHEAT_MENU:
		menuInit(MENU_CHEATS);
	break;
	
	case 15:
		player.invulnerable += 300; 
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case 16:
		if(bombPower<5) bombPower++;
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case 17:
		timeTicks+=30*13;
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case 18:
		player.traction+=300;
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case 19:
		bombLimit = false;
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case 20:
		seeHidden += 100;
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case 21:
		player.setState(Ball.REDUCED_ST);
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case 22:
		enem = null; System.gc();
		cheatUsed = true; menuExit = true; menuBarShow = true;
	break;
	
	case MENU_ACTION_QUIT_CONFIRM :
		menuInit(MENU_QUIT_ASK);
	break;
	
	case MENU_ACTION_RESTART_CONFIRM :
		menuInit(MENU_RESTART_ASK);
	break;
	
	//#ifndef NONETWORK
	case MENU_ACTION_RETRIEVE_NETWORK_RECORDS:		
		menuExit = true;																	
		gameStatus = GAME_MENU_SEE_NETWORK_LEVEL_RECORDS;				
	break;
	
	case MENU_ACTION_PACK_DOWNLOAD_INSTRUCTIONS: 	
		menuInit(MENU_PACK_DOWNLOAD_INSTRUCTIONS);
	break;
	
	case MENU_ACTION_DOWNLOAD_ENTER_CODE:
		// Send records				
		menuExit = true;																	
		gameStatus = GAME_MENU_INPUT_DOWNLOAD_CODE;
					
		ga.inputDialogCreate(networkText[0][2], networkText[2][2], networkText[2][1], 10);
		ga.inputDialogInit();
	break;
	
	case MENU_ACTION_FORCE_PACK_REDOWNLOAD:		
		requestedPackId = serverPacksIds[menuListPos];	
		menuExit = true;
		gameStatus = GAME_MENU_DOWNLOAD_PACK;
	break;
	//#endif
	}

}

// <=- <=- <=- <=- <=-



//static Level lev;
static Ball player, bomb[], enem[], playerPieces[];
static int expl[][];
static int angleOffset = 0, counter = 0, bombPower = 1, bombNumber = 0, explNumber = 0, collapsingTilesNumber = 0, seeHidden = 0, currentLevel, timeTicks, currentPack, dlPacks, ellapsedTicks, packTime;
static boolean bombLimit = true, bombAdded = false, cheatUsed;;
static short packTimes[][] = new short[20][3], topTimes[][] = new short[20][3], myTime, myClass, pickedJewels, packJewels, totalTimes[] = new short[3];
static String dlPacksNames[];
static byte packBuffer[];
static int dlPacksIds[], storedPackId;




// *******************
// -------------------
// play - Engine
// ===================
// *******************
// -------------------

boolean playShow, loadingShow = false;

int playExit;

// -------------------
// play Create
// ===================

public void loadPackBuffer()
{
	if(currentPack < inJarPacks)
	{
		// Play original levels from QBlast 2.0
		
		//#ifdef J2ME
		packBuffer = loadFile("/"+defaultPak[currentPack]+".pak");		 
		//#elifdef DOJA
		//#endif
		
		startupPackTimes(packBuffer);	
		loadTopTimes(defaultPak[currentPack]);
	}
	else
	{
		// Load levels downloaded and saved to RMS		
		//#ifndef NONETWORK
		packBuffer = rmsLoadFile("stored.pak");		 
		startupPackTimes(packBuffer);	
		loadTopTimes(dlPacksNames[currentPack-inJarPacks]);		
		//#endif
	}
}

public void playCreate()
{
	loadPercent = 0; canvasShow = true; gameDraw();
		
	//#ifndef INITIALLOAD
	//#else
	increaseLoadPercent(90);
	//#endif
	
	loadPackBuffer();
	
	//#ifdef LOWMEMORY
	//#endif
	
	recalcPackResults();
		
	increaseLoadPercent(10);
	
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	//#ifndef INITIALLOAD
	//#endif
				
	packBuffer = null; System.gc();	
}

// -------------------
// play Init
// ===================

public void playInit()
{			

	loadPercent = 0; canvasShow = true; gameDraw();
	
	//#ifdef LOWMEMORY
	//#endif

	levelLoad(packBuffer,currentLevel);
	
	//#ifdef LOWMEMORY
	//#endif
	
	increaseLoadPercent(33);
	
	processLevel();			
	
	increaseLoadPercent(33);
	
	randomize(currentLevel);
	
	increaseLoadPercent(34);
			
	bombPower = 1; bombNumber = 0; explNumber = 0; collapsingTilesNumber = 0; seeHidden = 0; pickedJewels = 0;
	bombLimit = true; cheatUsed = false;
	
	timeTicks = packTimes[currentLevel][2];
	ellapsedTicks = 0; counter = 0;
			
}

// -------------------
// play Release
// ===================

public void playRelease()
{
	matrix = null; flagMatrix = null; collapsingTiles = null;		
	lifts = null; enem = null;
	//#ifndef NOTILEPIECES			
	tilePieces = null; 
	//#endif
		
	//#ifndef IGNORESUMMARY
	summaryMatrix = null; 
	//#endif
}

// -------------------
// play Tick
// ===================

//#ifdef FRAMESKIP
//#endif

public boolean playTick()
{
		
	if (playExit!=0) {return true;}
	
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	long debugMilis = System.currentTimeMillis();
	//#endif
									
	counter++;
	
	if(player.state < Ball.EXITING_ST)
	{	
		timeTicks--;	
		ellapsedTicks++;
	}
	
	if(seeHidden > 0) seeHidden--;

	
	//#ifdef ROUGHROTATE
	//#else
	
		if((keyR > 0) && angleOffset == 0) angleOffset = 32;
		if((keyR < 0) && angleOffset == 0) angleOffset = -32;


	//#ifdef FRAMESKIP
	//#endif
	{
			//#ifdef VERSIONA
			if(angleOffset > 0) {angle = (256 + angle + 8) % 256; angleOffset-=8;};
			if(angleOffset < 0) {angle = (256 + angle - 8) % 256; angleOffset+=8;};
			//#else
			if(angleOffset > 0) {angle = (256 + angle + 16) % 256; angleOffset-=16;};
			if(angleOffset < 0) {angle = (256 + angle - 16) % 256; angleOffset+=16;};
			//#endif
			
			//#ifdef FRAMESKIP
			//#endif
	}
	//#ifdef FRAMESKIP
	//#endif
	
		
	//#endif
	
	int keyx = 0, keyz = 0;
	
	if(keyR == 0){
						
		if(angle<48 || angle>=240)
		{
			keyx = keyX; keyz = keyY;
		}
		if(angle>=48 && angle<112)
		{
			keyx = keyY; keyz = -keyX;
		}
		if(angle>=112 && angle<176)
		{
			keyx = -keyX; keyz = -keyY;
		}
		if(angle>=176 && angle<240)
		{
			keyx = -keyY; keyz = keyX;
		}
										
	}
	
	//levelUpdate();
	
	// Lifts
		
	if(lifts != null)		
	for(int i = 0; i < lifts.length; i++)	
		lifts[i].updateLift();
	
	// Pieces of broken tiles
			
	//#ifndef NOTILEPIECES				
	for(int i = 0; i < tilePieces.length; i++)
	if(tilePieces[i].active)
	{	
		tilePieces[i].basicUpdate();
		if(tilePieces[i].counter > 20) tilePieces[i].active = false;
	}
	//#endif
		
	// About-to-collapse tiles
				
	if(collapsingTilesNumber > 0)				
	for(int i = 0; i < collapsingTiles.length; i++)
		if(collapsingTiles[i][3] > 0)
		{
			collapsingTiles[i][3]--;
			if(collapsingTiles[i][3] == 0){
				
				// Make the tile collapse
				
				int cx = (collapsingTiles[i][0]<<8)+128,
					cy = (collapsingTiles[i][1]<<8)+128,
					cz = (collapsingTiles[i][2]<<8)+128; 
				//collapse((collapsingTiles[i][0]<<8)+128,(collapsingTiles[i][1]<<8)+128,(collapsingTiles[i][2]<<8)+128);				
				modifyGround(cx,cy,cz,BROKEN,EMPTY);
				
				soundPlay(8,1);
					
				//#ifndef NOTILEPIECES							
				for(int j = 0; j < 4; j++)			
					addTilePiece((cx>>8),(cy>>8)-1,(cz>>8));						
				//#endif
				
				collapsingTilesNumber--;
			}
		}
		
						
	player.updatePlayer(this,keyx,keyz,(keyMenu ==2 && lastKeyMenu != keyMenu && ellapsedTicks >= 4));
		
	if(enem != null)		
	for(int i = 0; i < enem.length; i++)		
		if(enem[i].active)
			enem[i].updateEnemy(this,keyx,keyz,(keyMenu ==2 && lastKeyMenu != keyMenu && ellapsedTicks >= 4));
	
			
	//if(player.realx%256 + enem[0].realx%256 != 255 || player.realz%256 + enem[0].realz%256 != 255) System.out.println("Desfasados!!!");
							
	if(player.state == Ball.BROKEN_ST)
	for(int i = 0; i < playerPieces.length; i++)
		playerPieces[i].basicUpdate();
		

	if(bombNumber > 0)					
	for(int i = 0; i < bomb.length; i++)
		if(bomb[i].active)
		{
			bomb[i].updateBomb(expl);
			if(bomb[i].counter >= 60)
			{
				addExplosion(bomb[i].x, bomb[i].y, bomb[i].z);
				bomb[i].active = false; bombNumber--;
			}			
		}
	
	if(explNumber > 0)				
	for(int i = 0; i < expl.length; i++)		
		if(expl[i][9] != 0){		
			Ball.explosionUpdate(i);		
			if(expl[i][9] == 0) explNumber--;
		}
								
	if(player.y < -5 || (player.state == Ball.BROKEN_ST && player.counter >= 40)) {
		
		// Restart player
				
		player.create(this,startx,starty,startz,PLAYER);
			
		if(player.x == 0 && player.y == 0 && player.z == 0)
			player.create(this,startx,starty + 2,startz,PLAYER);
		
		// Restart mirror balls
		
		if(enem != null)
		for(int i = 0; i < enem.length; i++)		
			if(enem[i].enemyType == Ball.MIRROR)
				enem[i].resetPos();
	}
	
	if(player.state == Ball.EXITED_ST) playExit = 1;
		
	if(timeTicks <= 0) playExit = 3;
	
	playShow = true;
	
	//#ifdef FRAMESKIP
	//#else
	if (timeTicks % 13 == 0  || (timeTicks <= 30*13 && timeTicks % 13 == 7)) menuBarShow = true;
	//#endif
	
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	//com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug.println("Update:"+(System.currentTimeMillis() - debugMilis)+"ms");
	//#endif
		
	return false;
}

void processLevel()
{
	int enemPos = 0, liftPos = 0, aux;
			
	bomb = new Ball[5];
	for(int i = 0; i < bomb.length; i++)	
		bomb[i] = new Ball();
	expl = new int[5][];
	for(int i = 0; i < expl.length; i++)
	{	
		expl[i] = new int[10]; expl[i][3] = 0; expl[i][9] = 0;
	}
	//#ifndef FEWPIECES
	playerPieces = new Ball[5];	
	//#else
	//#endif
	for(int i = 0; i < playerPieces.length; i++)	
		playerPieces[i] = new Ball();		
	
	aux = count(BLACK_EN) + count(ELECTRIC_EN) + count(MAGNETIC_EN) + count(RUBBER_EN) + count(MIRROR_EN);
	if(aux > 0) enem = new Ball[aux];
	
	aux = count(LIFT_EN);
	if(aux > 0) lifts = new Ball[aux];
	
	aux = count(BROKEN);
	
	//#ifndef NOTILEPIECES			
		//#ifndef FEWPIECES	
		tilePieces = new Ball[(aux > 0 ? 12 : 0)];
		//#else
		//#endif
		for(int i = 0; i < tilePieces.length; i++)
			tilePieces[i] = new Ball();
	//#endif
	
	collapsingTiles = new byte[aux][4];
			
	for(int i = 0; i < collapsingTiles.length; i++)
		collapsingTiles[i][3] = 0;
		
	
	player = new Ball();	
	
	//System.out.println ("Enemy Count : "+enem.length);
	
	for(int j = 0; j < sizeY; j++)
	for(int i = 0; i < sizeX; i++)
	for(int k = 0; k < sizeZ; k++)
	{
		switch(tile(i,j,k))
		{
			case STARTPOS : 
			startx = i; starty = j; startz = k; 			
			modify(i,j,k,EMPTY); 
			break;
			
			case LIFT_EN :
			lifts[liftPos] = new Ball();
			lifts[liftPos].create(this,i,j,k,LIFT);
			modify(i,j,k,LIFTPATH);
			liftPos++;
			break;
			
			case BLACK_EN :
			case ELECTRIC_EN :
			case MAGNETIC_EN :
			case MIRROR_EN :
			case RUBBER_EN :
			enem[enemPos] = new Ball();			
			enem[enemPos].enemyType = (short)(Ball.BLACK + (tile(i,j,k) - BLACK_EN));
			enem[enemPos].create(this,i,j,k,ENEMY);
			if(tile(i,j,k) == MIRROR_EN)
				enem[enemPos].saveOriginalPos();
			modify(i,j,k,EMPTY);
			enemPos++;
			break;
			//default : System.out.println ("("+i+","+j+","+k+") : "+lev.tile(i,j,k));
		}		
	}
		
	player.create(this,startx,starty,startz,PLAYER);
}


void loadTopTimes(String pack)
{
	
	//#ifdef STORETIMESONPREFS
	loadPrefs();
	//#else
	/*byte buffer[] = rmsLoadFile(pack+".rec");
	
	try
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer,0,buffer.length));
						
		for(int i = 0; i < 60; i++)							
			topTimes[i/3][i%3] = dis.readShort();
		
		// Avoid corrupt scores
		for(int i = 0; i < 20; i++)
			if(topTimes[i][0] == 0) 
			{
				topTimes[i][0] = packTimes[i][2]; 
				topTimes[i][1] = 2;
				topTimes[i][2] = 0;
			} 
			
											
	}catch (Exception e){  }*/
	//#endif
}

void saveTopTimes(String pack)
{
	
	//#ifdef STORETIMESONPREFS
	savePrefs();
	//#else
	/*byte buffer[];
		
	try
	{
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	    DataOutputStream ostream = new DataOutputStream(bstream);				
															
		
		for(int i = 0; i < 60; i++)			
			ostream.writeShort(topTimes[i/3][i%3]);					
															
	    buffer = bstream.toByteArray();
	    
	    rmsSaveFile(pack+".rec",buffer);
	    
	}catch (Exception e){ }*/
	//#endif
}


boolean addBomb(int x, int y, int z, boolean mirror)
{
	int i = 0;
		
	while(i < bomb.length && bomb[i].active)
		i++;
		
	if(i < bomb.length)
	{		
		bomb[i].realCreate(this,x,y,z,BOMB);			
		bomb[i].velx = (mirror ? -player.velx : player.velx);
		bomb[i].vely = player.vely;
		bomb[i].velz = (mirror ? -player.velz : player.velz);
		
		bombNumber++;
		
		soundPlay(4,1);
		
		return true;		
	}
	
	return false;
}

void addExplosion(int x, int y, int z)
{
	int i = 0;
	while(i < expl.length && expl[i][9] != 0)
		i++;
		
	if(i < expl.length)
	{		
		expl[i] = Ball.explosionSet(expl[i],this,x,y,z,bombPower);
		
		explNumber++;
		
		vibraInit(500);
		soundPlay(5,1);					
	}
}

void startUpPlayerPieces()
{
	for(int i = 0; i < playerPieces.length; i++){
		
		Ball p = playerPieces[i];
	
		p.create(this,player.x,player.y,player.z,PLAYER_PIECE);
		p.velx = player.velx + Math.abs(RND(100)) - 50;
		p.vely = player.vely + Math.abs(RND(100));
		p.velz = player.velz + Math.abs(RND(100)) - 50;
		
	}
	
	soundPlay(6,1); vibraInit(1000);
}


/* Level Stuff ==================================================================================================
	 			
*/ 
	
	public static final int SUMMARY = 2;
	public static final int SUMMARYY = 1;
	
	//#ifdef LOWMEMORY
	//#else
	public static final int MARGIN = 8;
	//#endif
	
	public static final byte existFlags[] = { (byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x10};
		
	int sizeX, sizeY, sizeZ, startx, starty, startz;
	
	byte matrix[][][], flagMatrix[][][], collapsingTiles[][];
	//#ifndef IGNORESUMMARY
	byte summaryMatrix[][][];
	//#endif
	short times[] = new short[3];
	
	Ball lifts[];
	//#ifndef NOTILEPIECES			
	Ball tilePieces[];
	//#endif
	
	
	/*public void newLevel(int sx, int sy, int sz)
	{
		sizeX = sx + MARGIN*2; sizeY = sy + MARGIN*2; sizeZ = sz + MARGIN*2;
		
		matrix = new byte[sizeY][sizeX][sizeZ];	
		
		flagMatrix = new byte[sizeY][sizeX][sizeZ];	
		
		//#ifndef IGNORESUMMARY
		summaryMatrix = new byte[(sizeY/SUMMARYY)+1][(sizeX/SUMMARY)+1][(sizeZ/SUMMARY)+1];	
		//#endif
		
		for(int j = 0; j < flagMatrix.length; j++)		
		for(int i = 0; i < flagMatrix[j].length; i++)		
		for(int k = 0; k < flagMatrix[j][i].length; k++)	
			flagMatrix[j][i][k] = 0; 
			
		//#ifndef IGNORESUMMARY			
		for(int j = 0; j < summaryMatrix.length; j++)		
		for(int i = 0; i < summaryMatrix[j].length; i++)		
		for(int k = 0; k < summaryMatrix[j][i].length; k++)	
			summaryMatrix[j][i][k] = 0; 
		//#endif
							
	}*/
	
		
	byte tile(int x, int y, int z)		
	{
		
		if(x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ)
			return 0;
		else return matrix[y][x][z];
		
	}
	
	void modify(int x, int y, int z, byte v)		
	{
		
		if(x < 0 || x >= sizeX || y < 0 || y >= sizeY || z < 0 || z >= sizeZ)
			return;
		else matrix[y][x][z] = v;
		
	}
	
	int yAt(int radius, int rx, int ry, int rz)
	{
		int x = rx>>8, y = ry>>8, z = rz>>8, cy = y<<8;		
				
		if(y < 0) return -99999;
						
		switch(tile(x,y,z))
		{
			
			case HIDDEN :
			case RBELT	:
			case LBELT	:
			case UBELT	:
			case DBELT	:
			case BOX	:
			case ICE	:
			case BROKEN	:
			case TILE 	: if(ry%256 >= 128) return (y+1) << 8; else return yAt(radius,rx,ry - 128, rz);
			case RRAMPB	: return cy + 256 - (rx%256);
			case RRAMPD	: return cy + 511 - (2*(rx%256));
			case RRAMPE	: return cy + 256 - (2*(rx%256));
			case LRAMPB	: return cy + (rx%256);
			case LRAMPD	: return cy + (2*(rx%256));
			case LRAMPE	: return cy + (2*(rx%256)) - 256;
			case DRAMPB	: return cy + 256 - (rz%256);
			case DRAMPD	: return cy + 511 - (2*(rz%256));
			case DRAMPE	: return cy + 256 - (2*(rz%256));
			case URAMPB	: return cy + (rz%256);
			case URAMPD	: return cy + (2*(rz%256));
			case URAMPE	: return cy + (2*(rz%256)) - 256;
			
			
			case LIFTPATH :
				if(checkFlag(x,y,z,LIFT))
				{
					if(lifts != null)										
					for(int i = 0; i < lifts.length; i++)					
					if(lifts[i].x == x && lifts[i].y == y && lifts[i].z == z)
					{							
						return lifts[i].realy + lifts[i].radius;											
					}					
				} 				
				return yAt(radius,rx,ry - 128, rz);
				
			case HOLE   :
				if(radius < 130){					
					if (ry%256 >= 128 && (rx%256 < 104 || rz%256 < 104 || rx%256 >= 152 || rz%256 >= 152)) 
						return (y+1) << 8;
					else return yAt(radius,rx,ry - 128, rz);
				}else if(ry%256 >= 128) return (y+1) << 8; 
					  else return yAt(radius,rx,ry - 128, rz);
			default 	: return yAt(radius,rx,ry - 128, rz);
		}
	}
	
	byte nearestTileAt(int rx, int ry, int rz)
	{
		int x = rx>>8, y = ry>>8, z = rz>>8;		
				
		if(y < 0) return EMPTY;
						
		switch(tile(x,y,z))
		{
			default 	: return tile(x,y,z);
			case GOAL	:
			case EMPTY 	: return nearestTileAt(rx,ry - 256, rz);
		}
	}
	
	void modifyGround(int rx, int ry, int rz, byte or, byte de)
	{
		int x = rx>>8, y = ry>>8, z = rz>>8;		
				
		if(y < 0) return;
						
		if(tile(x,y,z) == or) modify(x,y,z,de);
		else modifyGround(rx,ry-256,rz,or,de);		
	}
	
	/*void levelUpdate()
	{		
		// Lifts
		
		if(lifts != null)
		for(int i = 0; i < lifts.length; i++)		
			lifts[i].updateLift();
	
		// Pieces of broken tiles
		
		//#ifndef NOTILEPIECES	
		for(int i = 0; i < tilePieces.length; i++)
		if(tilePieces[i].active)
		{	
			tilePieces[i].basicUpdate();
			//#ifdef FEWPIECES
			if(tilePieces[i].counter > 6) tilePieces[i].active = false;
			//#else
			if(tilePieces[i].counter > 20) tilePieces[i].active = false;			
			//#endif
		}
		//#endif
		
		// About-to-collapse tiles
				
		for(int i = 0; i < collapsingTiles.length; i++)
			if(collapsingTiles[i][3] > 0)
			{
				collapsingTiles[i][3]--;
				if(collapsingTiles[i][3] == 0)
					collapse((collapsingTiles[i][0]<<8)+128,(collapsingTiles[i][1]<<8)+128,(collapsingTiles[i][2]<<8)+128);
			}
		
	}*/
	
	/*void collapse(int rx, int ry, int rz)
	{
				
		modifyGround(rx,ry,rz,BROKEN,EMPTY);
		
		soundPlay(8,1);
			
		//#ifndef NOTILEPIECES							
		for(int i = 0; i < 4; i++)			
			addTilePiece((rx>>8),(ry>>8)-1,(rz>>8));		
		//#endif									
	}*/
	
	boolean checkSolid(Ball b, int x, int y, int z)
	{
		int grY = 0, modx = x%256, mody = y%256, modz = z%256;
						
		switch(tile(x>>8,y>>8,z>>8))
		{			
			case EMPTY : return false;
			case BOX : return true;			
			case HIDDEN:
			case RBELT :
			case LBELT :
			case UBELT :
			case DBELT :
			case ICE :
			case BROKEN :
			case TILE : return (mody) >= 128;
			case HOLE  :
				if(b.radius < 130)
					return (mody >= 128 && (modx < 64 || modz < 64 || modx >= 192 || modz >= 192));
				else return (mody) >= 128;
			case RRAMPB	: grY = 256 - 64 - (modx); 
						  return (mody) < grY && (mody) > grY - 128;
			case RRAMPD	: grY = 512 - 96 - (2*(modx)); 
						  return (mody) < grY && (mody) > grY - 64;
			case RRAMPE	: grY = 256 - 96 - (2*(modx)); 
						  return (mody) < grY && (mody) > grY - 64;
			case LRAMPB	: grY = (modx) - 64; 
						  return (mody) < grY && (mody) > grY - 128;
			case LRAMPD	: grY = (2*(modx)) - 96; 
						  return (mody) < grY && (mody) > grY - 64;
			case LRAMPE	: grY = (2*(modx))  - 96 - 256; 
						  return (mody) < grY && (mody) > grY - 64;
			case DRAMPB	: grY = 256 - 64 - (modz); 
						  return (mody) < grY && (mody) > grY - 128;
			case DRAMPD	: grY = 512  - 96 - (2*(modz)); 
						  return (mody) < grY && (mody) > grY - 64;
			case DRAMPE	: grY = 256 - 96 - (2*(modz)); 
						  return (mody) < grY && (mody) > grY - 64;
			case URAMPB	: grY = (modz) - 64; 
						  return (mody) < grY && (mody) > grY - 128;
			case URAMPD	: grY = (2*(modz)) - 96; 
						  return (mody) < grY && (mody) > grY - 64;
			case URAMPE	: grY = (2*(modz)) - 96 - 256; 
						  return (mody) < grY && (mody) > grY - 64;
			
			/*case LRAMPA : return (mody >= 128 && modx < 64);
			case RRAMPA : return (mody >= 128 && modx >= 192);
			case URAMPA : return (mody >= 128 && modz < 64);
			case DRAMPA : return (mody >= 128 && modz >= 192);*/
							
		}
		
		return false;		
		
	}
	
	boolean boxCollide(Ball ba, int b[])
	{
			
		/*if(ba.type != PLAYER && ba.enemyType != Ball.MIRROR)
			return checkSolid(ba,(b[0]+b[3])>>1,(b[1]+b[4])>>1,(b[2]+b[5])>>1);*/

		//int vprecission = (b[7]> 70 ? 1 : 0);
		int hprecission = (b[5]> 128 ? 1 : 0);
		
		for(int y = b[1]; y <= b[4]; y += b[7]>>(b[7]> 70 ? 1 : 0))
		for(int x = b[0]; x <= b[3]; x += b[6]>>hprecission)
		for(int z = b[2]; z <= b[5]; z += b[8]>>hprecission)
			if(checkSolid(ba,x,y,z)) return true;
		return false;
											
	}
	
	void setFlag(int x, int y, int z, int fl)
	{
		if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && z >= 0 && z < sizeZ)
		{
							
			flagMatrix[y][x][z] = (byte)(flagMatrix[y][x][z] | existFlags[fl]);
			
			//#ifndef IGNORESUMMARY
			summaryMatrix[y/SUMMARYY][x/SUMMARY][z/SUMMARY] = (byte)(summaryMatrix[y/SUMMARYY][x/SUMMARY][z/SUMMARY]  | existFlags[fl%7]);
			//#endif
		}
	}
	
	void unsetFlag(int x, int y, int z, int fl)
	{
		if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && z >= 0 && z < sizeZ)
		{
							
			flagMatrix[y][x][z] = (byte)(flagMatrix[y][x][z] & ~existFlags[fl]);		
			
			//#ifndef IGNORESUMMARY
			summaryMatrix[y/SUMMARYY][x/SUMMARY][z/SUMMARY] = (byte)(summaryMatrix[y/SUMMARYY][x/SUMMARY][z/SUMMARY] & ~existFlags[fl%7]);
			//#endif
		}
	}
	
	boolean checkFlag(int x, int y, int z, int fl)
	{
		if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && z >= 0 && z < sizeZ)
					
			return (flagMatrix[y][x][z] & existFlags[fl]) != 0;				
			
		else return false;	
				
	}
	
	/*boolean checkIsUniqueFlag(int x, int y, int z, int fl)
	{
		if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && z >= 0 && z < sizeZ)
					
			return (flagMatrix[y][x][z] == existFlags[fl]);				
			
		else return false;	
				
	}
	
	boolean checkAnyFlag(int x, int y, int z)
	{
		if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && z >= 0 && z < sizeZ)
					
			return flagMatrix[y][x][z] != 0;				
			
		else return false;
	}*/
	
	//#ifndef IGNORESUMMARY
	boolean checkAnyFlagSummary(int x, int y, int z)
	{
		if(x >= 0 && x < sizeX && y >= 0 && y < sizeY && z >= 0 && z < sizeZ)
					
			return summaryMatrix[y/SUMMARYY][x/SUMMARY][z/SUMMARY] > 0;				
			
		else return false;
	}
	//#endif
	
	public void levelLoad(byte[] buffer, int index)
	{
		
		int pos = 0, sx, sy, sz;
				
		try
		{
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer,0,buffer.length));
			
			dis.readUTF();
			dis.readByte();
			
			// Skip the time table
			
			for(int j = 0; j < 60; j++)			
				dis.readShort();
						
			// Skip first [index] levels
			
			for(int l = 0; l < index; l++)
			{
				sx = (int)dis.readByte();
				sy = (int)dis.readByte();
				sz = (int)dis.readByte();
				
				for(int i = 0; i < sx*sy*sz; i++)
					dis.readByte();
					
				for(int i = 0; i < 3; i++)
					dis.readShort();
			}
			
			// Load the level
			
			sx = (int)dis.readByte();
			sy = (int)dis.readByte();
			sz = (int)dis.readByte();
			
			// Create the new level
			
			//newLevel(sx,sy,sz);
			sizeX = sx + MARGIN*2; sizeY = sy + MARGIN*2; sizeZ = sz + MARGIN*2;
		
			matrix = new byte[sizeY][sizeX][sizeZ];	
			
			flagMatrix = new byte[sizeY][sizeX][sizeZ];	
			
			//#ifndef IGNORESUMMARY
			summaryMatrix = new byte[(sizeY/SUMMARYY)+1][(sizeX/SUMMARY)+1][(sizeZ/SUMMARY)+1];	
			//#endif
			
			for(int j = 0; j < flagMatrix.length; j++)		
			for(int i = 0; i < flagMatrix[j].length; i++)		
			for(int k = 0; k < flagMatrix[j][i].length; k++)	
				flagMatrix[j][i][k] = 0; 
				
			//#ifndef IGNORESUMMARY			
			for(int j = 0; j < summaryMatrix.length; j++)		
			for(int i = 0; i < summaryMatrix[j].length; i++)		
			for(int k = 0; k < summaryMatrix[j][i].length; k++)	
				summaryMatrix[j][i][k] = 0; 
			//#endif
			
									
			for(int j = 0; j < sy; j++)
			for(int i = 0; i < sx; i++)
			for(int k = 0; k < sz; k++){
			
				matrix[MARGIN + j][MARGIN + i][MARGIN + k] = dis.readByte();
				if(matrix[MARGIN + j][MARGIN + i][MARGIN + k] > EMPTY && matrix[MARGIN + j][MARGIN + i][MARGIN + k]<LIFTPATH) setFlag(MARGIN + i,MARGIN + j,MARGIN + k,BLOCK);				
				pos++;
			}
												
			
		}catch (Exception e){  }
		
	}

	
	public int count(byte b)
	{
		int sum = 0;
		
		for(int j = 0; j < sizeY; j++)		
		for(int i = 0; i < sizeX; i++)		
		for(int k = 0; k < sizeZ; k++)	
			if(matrix[j][i][k] == b) sum++; 
			
		return sum;
	}
	
	/*public int enemyCount()
	{
		return count(BLACK_EN) + count(ELECTRIC_EN) + count(MAGNETIC_EN) + count(RUBBER_EN) + count(MIRROR_EN);				
	}*/
	
	public void addCollapsingTile(int x, int y, int z)
	{
		
		for(int i = 0; i < collapsingTiles.length; i++)
			if(collapsingTiles[i][3] > 0)
			{			
				if(collapsingTiles[i][0] == x && collapsingTiles[i][1] == y && collapsingTiles[i][2] == z)
					return;
			}
			
		for(int i = 0; i < collapsingTiles.length; i++)			
		if(collapsingTiles[i][3] == 0)
		{					
				
			collapsingTiles[i][0] = (byte)x;
			collapsingTiles[i][1] = (byte)y;
			collapsingTiles[i][2] = (byte)z;
			collapsingTiles[i][3] = (byte)10;							
				
			collapsingTilesNumber++;				
			return;
		}						
	}
	
	//#ifndef NOTILEPIECES			
	public void addTilePiece(int x, int y, int z)
	{
		int i = 0;
						
		while(i < tilePieces.length && tilePieces[i].active)
			i++;
			
		if(i < tilePieces.length)
		{		
			tilePieces[i].create(this,x,y,z,TILE_PIECE);			
			tilePieces[i].velx += RND(40) - 20;
			tilePieces[i].velz += RND(40) - 20;			
		}
	}
	//#endif


// *******************
// -------------------
// rms File System - Engine v1.1 - Rev.5 (7.02.2005)
// ===================
// *******************

//#ifndef STORETIMESONPREFS
	
/* ==========================================================================================

	rmsCreate(int tama�oNecesario)
	-----------
		Inicializamos TODO lo referente al rms Engine para poder trabajar con el.
		A partir de aqui YA podemos usar la gestion de archivos del rms (Load/Save/Delete)
		Si nunca se almacen� nada, se formatea automaticamente el rms.


	rmsDestroy()
	------------
		Liberamos toda la memoria usada por el rms - Engine
		A partir de aqui NO podemos usar la gestion de archivos del rms (Load/Save/Delete)


	rmsFormat(int tama�oNecesario)
	-----------
		Formateamos el RMS, borrando TODO su contenido.


	rmsGetDir()
	-----------
		Recuperamos el nombre de todos los archivos almacenados.
		Resultado: "String[]" con los nombres de archivos.


	rmsCheckFile(String FileName)
	----------------------------
		Comprobamos si un fichero Existe.
		Resultado: "int = tama�o archivo" si el archivo NO existe su tama�o sera "-1"


	rmsLoadFile(String FileName)
	----------------------------
		Recuperamos un byte[] con al archivo solicitado.
		Resultado: "byte[] = null" si el archivo NO existe.


	rmsSaveFile(String FileName, byte[] data)
	-----------------------------------------
		Almacenamos un byte[] con el nombre de archivo indicado.
		Resultado: "boolean = true" ERROR, no se pudo almacenar el archivo.


	rmsDeleteFile(String FileName)
	------------------------------
		Eliminamos el archivo indicado.
		Resultado: "boolean = true" ERROR, el archivo no existe.

========================================================================================== */

// >>>>>>>>>>>>
//#ifdef J2ME
// >>>>>>>>>>>>

private int rmsFiles;
private short[] rmsFileInfo;
private String[] rmsFileName;

private byte[] rmsSector;

private int rmsSectorSize = 1*1024;
private int rmsStoreSize;
private int rmsStoreUsed;
private int rmsFileIndex;

// -------------------
// rms Create
// ===================

public void rmsCreate(int store)
{
	byte[] fat = rmsGetFila(0, 1);

	if (fat == null || fat[0] != 'r' || fat[1] != 'm' || fat[2] != 's')
	{
		rmsFormat(store);
	}

	byte[] in = rmsGetFila(0, 2);

	int pos = 0;

	rmsFiles = ((in[pos++]<<8) | (in[pos++] & 0xff));

	rmsFileInfo = new short[rmsFiles];

	for (int i=0 ; i<rmsFileInfo.length ; i++)
	{
		rmsFileInfo[i] = (short)((in[pos++]&0xff)<<8 | (in[pos++]&0xff));
	}

	rmsFileName = new String[rmsFiles];

	for (int i=0 ; i<rmsFiles ; i++)
	{
		int size = in[pos++];
		rmsFileName[i] = new String(in, pos, size);
		pos += size;
	}

	rmsSector = new byte[((in[pos++]<<8) | (in[pos++] & 0xff))];
	System.arraycopy(in, pos, rmsSector, 0, rmsSector.length);

	rmsStoreSize = (rmsSector.length * rmsSectorSize)>>1;

	rmsStoreUsed = 0;
	for (int i=0 ; i<rmsFiles ; i++)
	{
		rmsStoreUsed += rmsFileInfo[i];
	}
}


// -------------------
// rms Destroy
// ===================

public void rmsDestroy()
{
	rmsFileInfo = null;
	rmsFileName = null;
}


// -------------------
// rms Format
// ===================

public int rmsFormat(int store)
{
	rmsStoreSize = store;

	for (int t=0 ; t<2 ; t++)	// Hacemos "dos pasadas" por bug en motorola V525
	{
		String[] names = RecordStore.listRecordStores();

		if (names != null)
		{
			for (int i=0 ; i<names.length ; i++)
			{
				if ( names[i].startsWith("rmsFS") )
				{
					try
					{
						RecordStore.deleteRecordStore( names[i] );
					}
					catch(Exception e)
					{
					//#ifdef DebugConsole
						e.printStackTrace(); e.toString();
					//#endif
					}
				}
			}
		}
	}




	rmsSetFila(0, -1, new byte[4], false);		// Reservamos 1Kb pa la FAT
	rmsSetFila(0, -1, new byte[1024], false);	// Reservamos 1Kb pa la FAT


	byte[] sector = new byte[(rmsStoreSize / rmsSectorSize)*2];


	byte[] sectorData = new byte[rmsSectorSize];

	int pack = 1;
	int filas = 0;
	int size = 0;
	int secPos = 0;

	while (size < rmsStoreSize)
	{
		int filaId = rmsSetFila(pack, -1, sectorData, false);

//#ifdef DebugConsole
	System.out.println ("Fila ID: "+filaId);
//#endif

		if ( filaId < 0 )
		{
			if (filas == 0) {break;} else {pack++; filas = 0;}
		} else {

			sector[secPos++] = (byte)pack;
			sector[secPos++] = (byte)filaId;

			filas++;
			size += sectorData.length;
		}
	}


	rmsSector = new byte[secPos];
	System.arraycopy(sector, 0, rmsSector, 0, secPos);


//#ifdef DebugConsole
	System.out.println ("Formateado a: " + size + " / " + (size/1024) + "Kb");
//#endif



	rmsFiles = 0;
	rmsFileInfo = new short[0];
	rmsFileName = new String[0];
	rmsStoreUsed = 0;


	rmsSetFila(0, 1, new byte[] {'r','m','s','!'}, false);	// Guardamos rmsID
	rmsSetFila(0, 2, rmsFatGenerator(rmsFileName, rmsFileInfo), false);			// Guardamos rmsFAT

	rmsStoreSize = size;

	return size;
}


// -------------------
// rms Save File
// ===================

public boolean rmsSaveFile(String fileName, byte[] data)
{
	int found = rmsFindFile(fileName);

	if (found != -1) {rmsDeleteFile(fileName);}

	rmsSaveSegment(rmsStoreUsed, data);

	rmsStoreUsed += data.length;

	return rmsUpdateFat( 1, 0, fileName, (short)data.length);
}


// -------------------
// rms Load File
// ===================

public byte[] rmsLoadFile(String fileName)
{
	int found = rmsFindFile(fileName);

	if (found == -1) {return null;}

	return rmsLoadSegment(rmsFileIndex, rmsFileInfo[found]);
}


// -------------------
// rms Delete File
// ===================

public boolean rmsDeleteFile(String fileName)
{
	int found = rmsFindFile(fileName);

	if (found == -1) {return true;}

	rmsStoreUsed -= rmsFileInfo[found];

	int indexNew = rmsFileIndex;
	int indexOld = rmsFileIndex + rmsFileInfo[found];

	for (int i=found+1 ; i<rmsFiles ; i++)
	{
		rmsSaveSegment( indexNew, rmsLoadSegment(indexOld, rmsFileInfo[i]) );
		indexNew += rmsFileInfo[i];
		indexOld += rmsFileInfo[i];
	}

	return rmsUpdateFat(-1, found, null, (short)0);
}


// -------------------
// rms Get Dir
// ===================

public String[] rmsGetDir()
{
	return rmsFileName;
}


// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


// -------------------
// rms Find File
// ===================

private int rmsFindFile(String fileName)
{
	rmsFileIndex = 0;
	for (int i=0 ; i<rmsFileName.length ; i++) {if (fileName.equals(rmsFileName[i])) {return i;} else {rmsFileIndex += rmsFileInfo[i];}}
	return -1;
}


// -------------------
// rms Save Segment
// ===================

private void rmsSaveSegment(int index, byte[] data)
{
	int posData = 0;
	int sizeData = data.length;

	while (sizeData > 0)
	{
		int sector = index / rmsSectorSize;

	//System.out.println ("Sector: "+sector);

		byte[] bufer = rmsGetFila(rmsSector[sector<<1], rmsSector[(sector<<1)+1]);

	//System.out.println ("Sector: "+rmsSector[sector<<1]+","+rmsSector[(sector<<1)+1]);

		int pos = index - (rmsSectorSize * sector);

		int size = rmsSectorSize - pos;

		if (size > sizeData) {size = sizeData;}

	//System.out.println ("Size: "+size);

		System.arraycopy(data, posData, bufer, pos, size);

		rmsSetFila(rmsSector[sector<<1], rmsSector[(sector<<1)+1], bufer, false);

		posData += size;

		sizeData -= size;

		index += size;
	}
}


// -------------------
// rms Load Segment
// ===================

private byte[] rmsLoadSegment(int index, int sizeData)
{
	byte[] data = new byte[sizeData];

	int posData = 0;

	while (sizeData > 0)
	{
		int sector = index / rmsSectorSize;

		byte[] bufer = rmsGetFila(rmsSector[sector<<1], rmsSector[(sector<<1)+1]);

		int pos = index - (rmsSectorSize * sector);

		int size = rmsSectorSize - pos;

		if (size > sizeData) {size = sizeData;}

		System.arraycopy(bufer, pos, data, posData, size);

		posData += size;

		sizeData -= size;

		index += size;
	}

	return data;
}


// -------------------
// rms Update Fat
// ===================

private boolean rmsUpdateFat(int suma, int pos, String newName, short newData)
{
	rmsFiles += suma;

	String[] tempName = new String[rmsFiles];
	short[] tempInfo = new short[rmsFiles];

	int size = (suma>0? rmsFileName.length : tempName.length);

	System.arraycopy(rmsFileName, 0, tempName, 0 , size);
	System.arraycopy(rmsFileInfo, 0, tempInfo, 0 , size);

	if (suma > 0)
	{
		tempName[rmsFiles-1] = newName;
		tempInfo[rmsFiles-1] = newData;
	} else {
		if (pos < tempName.length)
		{
//			tempName[pos] = rmsFileName[rmsFileName.length-1];
//			tempInfo[pos] = rmsFileInfo[rmsFileInfo.length-1];

			System.arraycopy(rmsFileName, pos+1, tempName, pos , tempName.length-pos);
			System.arraycopy(rmsFileInfo, pos+1, tempInfo, pos , tempInfo.length-pos);
		}
	}

// Almacenamos la nueva FAT
	if ( rmsSetFila(0, 2, rmsFatGenerator(tempName, tempInfo), false ) == -1 )
	{
	// Error almacenando FAT, restauramos la vieja
	//#ifdef DebugConsole
		System.out.println ("*** Error: No se ha podido actualizar la FAT ("+suma+") ***");
	//#endif

//		rmsSetFila(0, rmsFileInfo[pos]&0xff, null, true);
//		rmsSetFila(0, 2, rmsFatGenerator(rmsFileName, rmsFileInfo), false );
	}

// La FAT se ha guardado CORRECTAMENTE, actualizamos FAT en memoria
	rmsFileInfo = tempInfo;
	rmsFileName = tempName;

	return false;
}


// -------------------
// rms Fat Generator
// ===================

// 00 (2) Numero de archivos almacenados
// 02 (n) FAT
//  n (1) tama�o del filename
//  n+1 (fn) filename

private byte[] rmsFatGenerator(String [] in, short[] inf)
{
	int size = 2 + inf.length*2 + in.length + 2 + rmsSector.length;
	for (int i=0 ; i<in.length ; i++) {size += in[i].length();}

	int pos = 0;
//	byte[] out = new byte[size];
	byte[] out = new byte[1024];

	out[pos++] = (byte)(rmsFiles>>8 & 0xff);
	out[pos++] = (byte)(rmsFiles    & 0xff);

	for (int i=0 ; i<inf.length ; i++)
	{
		out[pos++] = (byte)((inf[i]>>8) & 0xff);
		out[pos++] = (byte)((inf[i]   ) & 0xff);
	}

	for (int i=0 ; i<in.length ; i++)
	{
		out[pos++] = (byte)in[i].length();

		for (int t=0 ; t<in[i].length() ; t++)
		{
			out[pos++] = (byte)in[i].charAt(t);
		}
	}

	out[pos++] = (byte)(rmsSector.length>>8 & 0xff);
	out[pos++] = (byte)(rmsSector.length    & 0xff);

	System.arraycopy(rmsSector, 0, out, pos, rmsSector.length);

	return out;
}


// -------------------
// rms Get Fila
// ===================

private byte[] rmsGetFila(int pack, int fila)
{
	RecordStore rs = null;

	byte[] bufer = null;

	try
	{
		rs = RecordStore.openRecordStore("rmsFS"+pack, false);

		bufer = rs.getRecord(fila);
	}
	catch(Exception e)
	{
	//#ifdef DebugConsole
		e.printStackTrace(); e.toString();
	//#endif
	}
	finally {
		try {
			rs.closeRecordStore();
		} catch (Exception e) {}
	}

	return bufer;
}


// -------------------
// rms Set Fila
// ===================

private int rmsSetFila(int pack, int fila, byte[] data, boolean delete)
{
	RecordStore rs = null;

	int result = -1;

	try
	{
		rs = RecordStore.openRecordStore("rmsFS"+pack, data!=null);

		if (data != null)
		{
			if (fila < 0)
			{
				fila = rs.addRecord(data, 0, data.length);
			} else {
				rs.setRecord(fila, data, 0, data.length);
			}

			result = fila;
		}
		else if (delete)
		{
			rs.deleteRecord(fila);
		}
		else
		{
			int size = rs.getRecordSize(fila);
			result = size;
		}
	}
	catch(Exception e)
	{
	//#ifdef DebugConsole
		e.printStackTrace(); e.toString();
	//#endif
	}
	finally {
		try {
			rs.closeRecordStore();
		} catch (Exception e) {}
	}

	return result;
}

// <<<<<<<<<<<<
// <<<<<<<<<<<<

// >>>>>>>>>>>>
//#elifdef DOJA

// <<<<<<<<<<<<
//#endif
// <<<<<<<<<<<<


//#else
//#endif

// <=- <=- <=- <=- <=-

//#ifndef NOCHEAT

int CheatPos_1=0;
boolean cheatActive=false;

//#ifdef VI-TSM100
//#else
public static final byte[] Cheat_1= {2,2,7,7,7,6,6,6,6,6,9,9,9,9,3,3};
//#endif
	
/*public void CheatRUN(int keycode)
{
	//#ifdef VI-TSM100	
	byte[] Cheat_1= {2,2,7,7,7,6,6,6,6,6,9,9,9,9,3,3};
	//#else
	byte[] Cheat_1= {2,2,7,7,7,6,6,6,6,6,9,9,9,9,3,3};
	//#endif
	if (Cheat_1[CheatPos_1++] != keycode) {CheatPos_1=0;}
	if (Cheat_1.length==CheatPos_1) {cheatActive = true; CheatPos_1=0;}
}*/
//#endif




// *******************
// -------------------
// Network stuff
// ===================
// *******************

//#ifndef NONETWORK

//#ifdef NETWORKDEBUG
private void reportError(int answerID, DataInputStream dis) {

	try{
	
		if (answerID == MCSERVER_ERROR_GENERIC) {			
			String txt = dis.readUTF();			
			Debug.println("Error generico: " + txt);
			System.out.println (txt);
		} else if (answerID == MCSERVER_ERROR_NO_SUBSCRIPTION) {
			Debug.println("Error de pago/suscripcion");
		} else if (answerID == MCSERVER_ERROR_OLD_VERSION) {
			Debug.println("Version antigua del juego");
		} else if (answerID == MCSERVER_ERROR_INVALID_PACK) {
			Debug.println("Pack invalido");
		} else {
			Debug.println("Error de conexion/protocolo");
		}
	} catch (Exception e){}
	
}
//#endif

String bestPlayers[], serverPacks[], serverInfoText, myRecordName, downloadCode;
int bestTimes[], playerOwnTime;
int serverPacksIds[], requestedPackId;
boolean packPaid[];

public void networkCommand(int controller, int command, int expectedResponse)
{
		
	byte[] resp = null;
	boolean isNewPack = false, timeOut = false;
	
	
	// Generate the random unique userID for network
	
	if(userID == 0){
		
		Random r = new Random(System.currentTimeMillis());
		
		//#ifdef J2ME
		Date d = new Date();
		long hipart = d.getTime();		
		long lopart = r.nextInt();						
		//#elifdef DOJA
		//#endif					
		userID = Math.abs(((hipart & 0xffffffff) << 32) | (lopart & 0xffffffff));
		savePrefs();	
	}
			
	try {
		
		
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
		DataOutputStream ostream = new DataOutputStream(bstream);	
		
		// Construct command data
		switch(command)
		{		
			case MCCLIENT_GET_SCORES :
			ostream.writeInt((currentPack < inJarPacks ? defaultPakIds[currentPack] : dlPacksIds[currentPack - inJarPacks]));		
			ostream.writeInt(gameStatus == GAME_MENU_SEE_NETWORK_PACK_RECORDS ? -1 : currentLevel + 1);
			ostream.writeInt(5);
			break;				
			
			case MCCLIENT_GET_PACK_LIST:
			break;	
			
			case MCCLIENT_REPORT_SCORE:
			ostream.writeInt((currentPack < inJarPacks ? defaultPakIds[currentPack] : dlPacksIds[currentPack - inJarPacks]));
			ostream.writeUTF(myRecordName);
			ostream.writeInt(20);
			for(int i = 0; i < 20; i++)
				ostream.writeInt(topTimes[i][1] > 2 ? packTimes[i][2] : topTimes[i][0]);
			
			//#ifdef NETWORKDEBUG
		
			Debug.println("Uploading times...");
			Debug.println("Pack Id :"+(currentPack < inJarPacks ? defaultPakIds[currentPack] : dlPacksIds[currentPack - inJarPacks]));
			//for(int i = 0; i < 20; i++)
				//	com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug.println("Level "+i+", time "+(topTimes[i][1] > 2 ? packTimes[i][2] : topTimes[i][0]));
			
			//#endif
			break;
			
			case MCCLIENT_GET_PACK:
			ostream.writeUTF(downloadCode);
			isNewPack = true;
			break;
			
			case MCCLIENT_GET_PACK_ALREADY_PAID:
			ostream.writeInt(requestedPackId);
			//#ifdef NETWORKDEBUG			
			Debug.println("Request for pack "+requestedPackId);			
			//#endif NETWORKDEBUG
			isNewPack = false;
			break;
		}			
		
		ga.controllerID = controller;
		ga.methodID = command;
		ga.methodArgs = bstream.toByteArray();							
		
														
	} catch (IOException e) {
			
		//#ifdef NETWORKDEBUG
		Debug.println("IOE: " + e.getMessage());
		//#endif
		gameStatus = GAME_MENU_NETWORK_ERROR; return;
			
	}
								
	/*ga.invokeServer(MCGAME_QBLAST2, controller,
		command, bstream.toByteArray());					*/
		
	//ga.run();
		
	//if(!ga.answerIsOk) return false;		
		
	ga.clientThread = new Thread(ga);
	ga.clientThread.start();
	ga.threadBlocked = true;
	
	//#ifdef NETWORKDEBUG
	Debug.println("Deberia iniciar el thread de Network");
	//#endif NETWORKDEBUG
	
		
	long startingMillis = System.currentTimeMillis();
		
	do{
		//#ifndef NOREPAINTWHILEIDLE
		if(!gamePaused)
		{
			playShow = true; gameDraw();
		}
		//#endif
		
		timeOut = System.currentTimeMillis() > startingMillis + NETWORK_TIMEOUT;
						
		if(intKeyMenu < 0)
		{
			gameStatus = GAME_MENU_MAIN; intKeyMenu = 0;
			ga.clientThread = null; System.gc();
			return;	
		}
		
	}while (ga.threadBlocked && !timeOut);
						
	if(timeOut) resp = null;
	else resp = ga.serverResponse;
	
	ga.serverResponse = null; 
	System.gc();
	
	//#ifdef NETWORKDEBUG
	Debug.println("Deberia haber terminado el thread de Network");
	//#endif NETWORKDEBUG
			
	if (resp == null || !ga.answerIsOk) {
			
		//#ifdef NETWORKDEBUG
		Debug.println("null resp");
		//#endif
		gameStatus = GAME_MENU_NETWORK_ERROR;
		return;
	}
	
	ga.clientThread = null;
		
	// Process server response	
		
	ByteArrayInputStream bais = new ByteArrayInputStream(resp);
	DataInputStream dis = new DataInputStream(bais);
	
	try {
		
		int answerID = dis.readInt();
		if (answerID != expectedResponse/*MCSERVER_SCORES*/) {
			
			//#ifdef NETWORKDEBUG
			reportError(answerID, dis);
			//#endif
			
			if(answerID == MCSERVER_ERROR_NO_SUBSCRIPTION) 
				gameStatus = GAME_MENU_DOWNLOAD_CODE_ERROR;
			else gameStatus = GAME_MENU_NETWORK_ERROR;
			
			return;
		}
		
		// Process the expected response
		
		switch(answerID)
		{
			case MCSERVER_SCORES :				
			dis.readInt(); //Pack Id
			dis.readInt(); //Level Id
			int numPlayers = dis.readInt(); //Number of players
		
			bestPlayers = new String[numPlayers];
			bestTimes = new int[numPlayers];
		
			for(int i = 0; i < numPlayers; i++)
				bestPlayers[i] = dis.readUTF();
				
			for(int i = 0; i < numPlayers; i++)
				bestTimes[i] = dis.readInt();
			playerOwnTime = dis.readInt();
			
			//#ifdef NETWORKDEBUG
			Debug.println("Received "+numPlayers+" top players");
			//#endif
			menuInit(MENU_NETWORK_RECORDS); 
			gameStatus = GAME_LEVEL_COMPLETE;
			return;
			
			case MCSERVER_PACK_LIST:			
			serverInfoText = dis.readUTF();//readSuxString(dis);
			
			//#ifdef NETWORKDEBUG
			Debug.println("server says: " + serverInfoText);
			//#endif
			
			int n = dis.readInt();
			
			serverPacks = new String[n];
			serverPacksIds = new int[n];
			packPaid = new boolean[n];
			
			//#ifdef NETWORKDEBUG
			Debug.println("" + n + " packs");
			//#endif
			
			for (int i = 0; i < n; i++) {
				serverPacksIds[i] = dis.readInt();
				serverPacks[i] = dis.readUTF();//readSuxString(dis);							
				packPaid[i] = (dis.readByte() == '1');
				
				//#ifdef NETWORKDEBUG
				Debug.println("" + serverPacksIds[i] + ": " + serverPacks[i] + (packPaid[i] ? " (Already paid)" : ""));
				//#endif
			}
			gameStatus = GAME_MENU_PACK_DOWNLOAD_SELECT;			
			return;
			
			case MCSERVER_SCORE_OK:
			gameStatus = GAME_MENU_LEVEL_SELECT;
			return;			
			
			case MCSERVER_PACK:			
			int receivedId = dis.readInt();
			int packSize = dis.readInt();
			String packName = dis.readUTF();
			
			//#ifdef NETWORKDEBUG			
			Debug.println(packName+" Id:"+receivedId+", size "+packSize+" bytes");			
			//#endif NETWORKDEBUG
			
			byte receivedData[] = new byte[packSize];
			
			for(int i = 0; i < packSize; i++)
				receivedData[i] = dis.readByte();
							
			rmsSaveFile("stored.pak",receivedData);	    	
			storedPackId = receivedId;				
			
			//#ifdef NETWORKDEBUG
			Debug.println("Pack stored!");			
			//#endif NETWORKDEBUG
			
			if(isNewPack)
			{
				storeNewPackInfo(packName,receivedId);
			}				
								
			savePrefs();
			gameStatus = GAME_MENU_PACK_SELECT;
			return;
		}
		
	} catch (IOException e) {
			
			//#ifdef NETWORKDEBUG
			Debug.println("Error de protocolo");
			//#endif NETWORKDEBUG
			gameStatus = GAME_MENU_NETWORK_ERROR;
			return;
	}
	
}

void storeNewPackInfo(String packName, int receivedId)
{
	boolean packPreviouslyDownloaded = false;
	
	for(int i = 0; i < dlPacks; i++)
		if (dlPacksIds[i] == receivedId) packPreviouslyDownloaded = true;
	
	if(!packPreviouslyDownloaded)
	{
		dlPacks++;
		
		String tempNames[] = new String[dlPacks];
		int tempIds[] = new int[dlPacks];

		for(int i = 0; i < dlPacks - 1; i++)
		{
			tempNames[i] = dlPacksNames[i];
			tempIds[i] = dlPacksIds[i];
		}

		tempNames[dlPacks - 1] = packName;
		tempIds[dlPacks - 1] = receivedId;

		dlPacksNames = tempNames;
		dlPacksIds = tempIds;
		
		for(int i = 0; i < 20; i++)
		{
				topTimes[i][0] = -1;
				topTimes[i][1] = 3;
				topTimes[i][2] = 0;	    		
		}
			
		saveTopTimes(packName);
		
		//#ifdef NETWORKDEBUG
		Debug.println("New pack added to pack list");			
		Debug.println("Blank scores created");			
		//#endif NETWORKDEBUG
	}
}


//#endif
 	
};