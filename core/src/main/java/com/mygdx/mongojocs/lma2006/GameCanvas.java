package com.mygdx.mongojocs.lma2006;


//define SOUNDTEST
//#ifdef WEBSUBCRIPTION
//#define NOCHAMPIONS
//#endif

//#ifdef J2ME



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;

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
//#ifndef FULLSCREEN
//#endif
//#endif
{

// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
////////////////////////////////////////////////////////////////////////////
// ZNR - Begin
				
int option = 0;
int leftRight = 0;
int suboption = 0;
boolean showMenuBar = false;				
boolean requestMenuBar = false;
//#ifdef LARGEICONS
//#else
final static int MENU_ICONS_HEIGHT = 13;
final static int MENU_ICONS_GFXHEIGHT = 11;
//#endif

//#ifndef FULLSCREEN
//#else
	//#ifdef THINSOFTKEYARROWSHEIGHT
	//#else 
	final static int SOFTKEYARROWSHEIGHT = 10;
	final static int ARROWBOTDISTANCE = 2;
	//#endif
//#endif

final static int TOTAL_TRAINING_TIME = 20*7;

final static int ICON_MATCH = 0;
final static int ICON_TACTICS = 1;
final static int ICON_FORMATION = 2;
final static int ICON_SELECTTEAM = 3;
final static int ICON_STYLE = 4;
final static int ICON_LEAGUE = 5;
final static int ICON_CLASSIFICATION = 6;
//#ifdef LESSICONS
//#else
final static int ICON_TOPSCORERS = 7;
//#endif
final static int ICON_TRAINING = 8;
//#ifdef LESSICONS
//#else
final static int ICON_FINANCESTATS = 9;
//#endif
final static int ICON_TRANSFERS = 10;
final static int ICON_BUY = 11;
final static int ICON_SELL = 12;
final static int ICON_SOUND = 13;
final static int ICON_OPTIONS = 14;
final static int ICON_SAVE = 15;
final static int ICON_EXIT = 16;
final static int ICON_FINANCE = 17;
final static int ICON_HELP = 18;
//#ifdef LESSICONS
//#else
final static int ICON_SPONSOR = 19;
//#endif

final static int MAIN_MENU_ICONS[] = {
	ICON_MATCH,
	ICON_TACTICS,	
	ICON_LEAGUE,
	ICON_TRAINING,
	ICON_TRANSFERS,
//#ifndef NOFINANCES
	ICON_FINANCE,
//#endif	
	ICON_OPTIONS
	/*ICON_HELP,
	ICON_SAVE,
	ICON_EXIT*/
	};

final static int SUBMENU_ICONS[][] = 
{
	{ICON_MATCH},
	{ICON_FORMATION, ICON_SELECTTEAM, ICON_STYLE},
	{ICON_CLASSIFICATION, ICON_CLASSIFICATION 
		//#ifndef NOCHAMPIONS		
		,ICON_CLASSIFICATION
		//#endif
		//#ifndef NOTOPPLAYERS	
		, ICON_TOPSCORERS
		//#endif
	},
	{ICON_TRAINING},
	{ICON_BUY, ICON_SELL},
//#ifndef NOFINANCES
	{ICON_FINANCESTATS, ICON_SPONSOR},
//#endif	
	{ICON_HELP,
		//#ifndef PLAYER_NONE		
		ICON_SOUND,
		//#endif
		ICON_SAVE,
		
		ICON_EXIT}		
};

final static int SUBMENU_COLORS[] = {0xFFF150, 0x709AB7, 0xFC6764, 0x83EC3E, 0xA2A2A2, 0x61F086, 0xF577E1, 0xdF8958};

final static int trainingSchedule[][] = new int[4][7];




/*
public void putColor(int rgb)
{
	//#ifdef J2ME
	scr.setColor(rgb);
	//#elifdef DOJA
	scr.setColor(scr.getColorOfRGB((rgb>>16)&0xff, (rgb>>8)&0xff, (rgb&0xff)));
	//#endif
}
*/

//#ifdef IMENUBAR
//#endif

public void drawMenuBar()
{		
	//#ifdef IMENUBAR
	//#endif
	String s = gameText[161][0] + twoDotString +gameText[162][league.coachGeneralTactic];		
	
	String[] mainMenuItems = { gameText[37][0], gameText[38][0], gameText[39][0], gameText[40][0], gameText[41][0],
			//#ifndef NOFINANCES
			gameText[177][0],
			//#endif
			gameText[10][0]};
	String[][] subMenuItems = { 
			{gameText[37][1]}, 
			{gameText[68][0] + twoDotString + gameText[104][league.coachFormation], gameText[69][0], s}, //LEAGUE 
			{gameText[44][0]+" 1", gameText[44][0]+" 2", gameText[122][0], gameText[45][0]}, 
			{gameText[40][0]}, 
			{gameText[56][0], gameText[57][0]}, //TRANSFER
			//#ifndef NOFINANCES
			{gameText[177][1], gameText[177][2]},
			//#endif
			{gameText[11][0],
				//#ifndef PLAYER_NONE
				gameText[19][0]+spaceString+(gameSound?gameText[20][0]:gameText[21][0]),
				//#endif
				gameText[265][0], gameText[42][0]}};
	
		
	if (showMenuBar)//currentForm == FORM_MAIN_MENU)
	{
		//#ifdef CUSTOMFONTATMENUBAR
		//#else
		fontSet(FONT_SMALL);
		//#endif
		
		//#ifdef J2ME
		scr.setClip(0,0,canvasWidth,canvasHeight);
		//#endif	
		proxySetColor(0xffffff);
		
		//#ifdef TWOBARS
		//#else
		scr.fillRect(0,canvasHeight-(MENU_ICONS_HEIGHT*2)-printFontHeight,canvasWidth, (MENU_ICONS_HEIGHT*2)+printFontHeight);
		//#endif
		//#ifdef TWOBARS
		//#else
			//#ifdef CUSTOMFONTATMENUBAR
			//#else
				//#ifdef EXTRABARHEIGHT
				//#else
				rectFillX(SUBMENU_COLORS[option],	0, canvasHeight-(MENU_ICONS_HEIGHT*2)-printFontHeight, canvasWidth, printFontHeight);
				//#endif
			//#endif		
		//#endif
		
		int x, y;
		//int option = iformSelectedItem;				
		
		
		x = 0;
		//#ifdef TWOBARS
		//#else
			//#ifdef EXTRABARHEIGHT
			//#else
			y =  canvasHeight-(MENU_ICONS_HEIGHT*2)-printFontHeight;
			//#endif
		//#endif
		proxySetColor(0xffffff);
		
		//#ifdef TWOBARS
		//#else
		printDrawAbbr(mainMenuItems[option], x, y, PRINT_QSHADOW,  canvasWidth/2);
			//#ifdef FIXMENUBAR
			//#else
			printDrawAbbr(subMenuItems[option][suboption], 0, y, PRINT_QSHADOW|PRINT_RIGHT,  canvasWidth/2);
			//#endif
		//#endif		
		
		
		int NI = MAIN_MENU_ICONS.length;
		//FIRST BAR
		x = (canvasWidth-(MENU_ICONS_HEIGHT*NI))/2;
		y =  canvasHeight-(MENU_ICONS_HEIGHT*2);
		//#ifdef J2ME
		scr.setClip(0,0,canvasWidth,canvasHeight);
		//#endif
				
		proxySetColor(SUBMENU_COLORS[option]);//0xd0c0ff
		scr.fillRect(x+(option*MENU_ICONS_HEIGHT), y, MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT);
		scr.fillRect(0, y+MENU_ICONS_HEIGHT, canvasWidth, MENU_ICONS_HEIGHT);
		proxySetColor(0xffffff);
		scr.fillRect(x+(NI-SUBMENU_ICONS[option].length+suboption)*MENU_ICONS_HEIGHT, y+MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT);
		
		for(int i = 0; i < MAIN_MENU_ICONS.length;i++)
		{
			//#ifdef J2ME
			imageDraw(menuIcons, (MAIN_MENU_ICONS[i]%10)*MENU_ICONS_GFXHEIGHT, (MAIN_MENU_ICONS[i]/10)*MENU_ICONS_GFXHEIGHT ,MENU_ICONS_GFXHEIGHT, MENU_ICONS_GFXHEIGHT, x+1 , y+1);
			//#elifdef DOJA
			//#endif
			x += MENU_ICONS_HEIGHT;
		}
				
		
		x = ((canvasWidth-(MENU_ICONS_HEIGHT*NI))/2) + (NI-SUBMENU_ICONS[option].length)*MENU_ICONS_HEIGHT;
		y = canvasHeight-(MENU_ICONS_HEIGHT);		
		for(int i = 0; i < SUBMENU_ICONS[option].length;i++)
		{
			//#ifdef J2ME
			imageDraw(menuIcons, (SUBMENU_ICONS[option][i]%10)*MENU_ICONS_GFXHEIGHT, (SUBMENU_ICONS[option][i]/10)*MENU_ICONS_GFXHEIGHT ,MENU_ICONS_GFXHEIGHT, MENU_ICONS_GFXHEIGHT, x+1 , y+1);
			//#elifdef DOJA
			//#endif
			x += MENU_ICONS_HEIGHT;
		}
		
		
		//#ifdef J2ME
		scr.setClip(0,0,canvasWidth,canvasHeight);
		//#endif	
		proxySetColor(0x000000);
		//ZNR CURSOR
		if (cursorY == 0){
			x = ((canvasWidth-(MENU_ICONS_HEIGHT*NI))/2)+(option*MENU_ICONS_HEIGHT);
			y = canvasHeight-(MENU_ICONS_HEIGHT*2);
		} else {
			x = ((canvasWidth-(MENU_ICONS_HEIGHT*NI))/2)+(NI-SUBMENU_ICONS[option].length+suboption)*MENU_ICONS_HEIGHT;
			y = canvasHeight-(MENU_ICONS_HEIGHT);
		}
		
		scr.drawRect(x-2, y-2, MENU_ICONS_HEIGHT+3, MENU_ICONS_HEIGHT+3);
		scr.drawRect(x-1, y-1, MENU_ICONS_HEIGHT+1, MENU_ICONS_HEIGHT+1);
		//scr.fillRect(x+(7-SUBMENU_ICONS[option].length+suboption)*MENU_ICONS_HEIGHT, y+MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT, MENU_ICONS_HEIGHT);
		
		
		//imageDraw(menuIcons, 0 , canvasHeight-(MENU_ICONS_HEIGHT*2));
	}
	
}

// ZNR - End
////////////////////////////////////////////////////////////////////////////

				
				
				
private Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga)
{
	this.ga = ga;
	
//#ifdef MIDP20
	//#ifdef FORCEFULLSCREEN
	//#endif
//#endif
	//canvasCreate();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Funcion que se ejecuta al hacer: 'repaint()'
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

private Graphics scr;

//#ifdef NOKIAUI
private DirectGraphics dscr;
//#endif

private boolean paintFinished = true;
private boolean paintLock = false;

//#ifdef MANUALDOUBLEBUFFER
//#else
public void paint(Graphics g) {
//#endif
	
	
//#ifdef SI
//#endif
	
	paintFinished = false;
	
	scr = g;
//#ifdef NOKIAUI
	dscr = DirectUtils.getDirectGraphics(g);
//#endif
	
//#ifdef DOJA
//#endif

	try {
		//#ifdef DOJA
		//#endif
		//long ms = System.currentTimeMillis();
			
		// iformPaint();
			
		if (screenLost) {
			iformDisplayAll();
		}
		if (animationLost) {
			iformDisplayAnimation();
		}		
			
		//ZNR
		try {
			//#ifdef SG-X450
			//#else
			drawMenuBar();
			//#endif
		} catch (Exception e) {}
		//#ifdef DOJA

		//#endif
		
		/*fontSet(FONT_SMALL);
		printCreate();
		printDraw("" + lastKeyCode, 50, 20, 0);*/

		
		//long ms2 = System.currentTimeMillis() - ms; 
		/*
		fontSet(FONT_SMALL);
		printCreate();
		//printDraw("" + exc1 + ", " + exc2 + ", " + ms2, 0, 0, PRINT_HCENTER|PRINT_VCENTER);
		String[] t = printTextBreak(debugStr, canvasWidth + 1);
		proxySetColor(0x000000);
		for (int i = 0; i < t.length; i++) {
			rectFill(0, 0, 20 + i*9, canvasWidth, 9);
			proxySetColor(0xffffff);
			printDraw(t[i], 0, 20 + i*9, 0);
		}
		*/
	} catch (Exception e) {
		//#ifdef Debug
		Debug.println("*** Graphic ***"); e.printStackTrace();
		//#endif
		//exc2++;
	}

//#ifdef DOJA
//#endif
	paintFinished = true;

//#ifdef Debug
		//if (Debug.enabled) {Debug.debugDraw(this, scr);}
//#endif		

//#ifdef SI
//#endif


	//#ifdef NO_RESUME
		screenLost=true;
	//#endif
		

	paintLock = false;
}

private boolean screenLost = false;
private boolean animationLost = false;

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void gameDraw() {

	repaint();
	serviceRepaints();
	/*
	if (!paintFinished)
		return;
	
	if (screenLost || animationLost) {
		paintLock = true;
		
		//#ifdef MANUALDOUBLEBUFFER
		//#endif
		
		repaint();
//#ifdef J2ME
		serviceRepaints();
//#endif
		
		while (paintLock) {
			try { Thread.sleep(2); } catch (Exception e) { 
				//#ifdef Debug
			 	//Debug.println("paintlock");
				//#endif					
			}
		}
		//#ifdef DOUBLEREPAINT
		//#endif
		screenLost = false;
		animationLost = false;
		
		//#ifdef DOUBLEREPAINT
		//#endif
	}*/
}

//#ifdef DOUBLEREPAINT
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// El MIDlet llama a esta funci�n cuando se PULSA una techa
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//private boolean virtualJoy = true;

private int intKeyMenu, intKeyX, intKeyY, intKeyMisc, intKeyCode;

//#ifdef J2ME

//#ifdef VI-TSM100
/*public void pointerPressed(int x, int y) {
	int ikc = intKeyCode;
	intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; this.intKeyCode=intKeyCode;
	if (x > 116) {
		if (y < 58) {
			intKeyMenu=-1;
		} else {
			intKeyMenu=-1;
		}
	}
}*/
//#endif


public void keyPressed(int intKeyCode)
{
	
	
	int ikc = intKeyCode;
	intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; this.intKeyCode=intKeyCode;
	
	
	/*if (virtualJoy)
	{
		switch (intKeyCode)
		{
		case Canvas.KEY_NUM1:
			intKeyX=-1; intKeyY=-1;
		break;
	
		case Canvas.KEY_NUM2:	// Arriba
			intKeyY=-1;
		break;
	
		case Canvas.KEY_NUM3:
			intKeyX= 1; intKeyY=-1;
		break;
	
		case Canvas.KEY_NUM4:	// Izquierda
			intKeyX=-1;
		break;
	
		case Canvas.KEY_NUM5:	// Disparo
			intKeyMenu=2;
		break;
	
		case Canvas.KEY_NUM6:	// Derecha
			intKeyX=1;
		break;
	
		case Canvas.KEY_NUM7:
			intKeyX=-1; intKeyY= 1;
		break;
	
		case Canvas.KEY_NUM8:	// Abajo
			intKeyY=1;
		break;
	
		case Canvas.KEY_NUM9:
			intKeyX= 1; intKeyY= 1;
		break;
		}
	}
	*/
	switch (intKeyCode)
	{
	//#ifdef NK-3650
	//#else
	/*
	case Canvas.KEY_NUM1:
		intKeyMisc=1; intKeyX=-1; intKeyY=-1;
	return;*/

	case Canvas.KEY_NUM2:	// Arriba
		intKeyMisc=2; intKeyY=-1;
	return;

	/*
	case Canvas.KEY_NUM3:
		intKeyMisc=3; intKeyX= 1; intKeyY=-1;
	return;*/

	case Canvas.KEY_NUM4:	// Izquierda
		intKeyMisc=4; intKeyX=-1;
	return;

	case Canvas.KEY_NUM5:	// Disparo
		intKeyMisc=5; intKeyMenu=2;
	return;

	case Canvas.KEY_NUM6:	// Derecha
		intKeyMisc=6; intKeyX=1;
	return;

	/*
	case Canvas.KEY_NUM7:
		intKeyMisc=7; intKeyX=-1; intKeyY= 1;
	return;*/

	case Canvas.KEY_NUM8:	// Abajo
		intKeyMisc=8; intKeyY=1;
	return;

	/*
	case Canvas.KEY_NUM9:
		intKeyMisc=9; intKeyX= 1; intKeyY= 1;
	return;*/

	case Canvas.KEY_NUM0:
		intKeyMisc=10;
	return;
	//#endif
	/*
	case 35:		// *
		intKeyMisc=12;
	return;

	case 42:		// #
		intKeyMisc=11;
	return;*/

// =========================================

//#ifdef NK
	case -6:	// Nokia - Menu Izquierda
		intKeyMenu = -1;
	break;

	case -7:	// Nokia - Menu Derecha
		intKeyMenu = 1;
	break;

//#elifdef SI-SF65
//#elifdef O2-XM
//#elifdef SH-902
//#elifdef SH-VS3
//#elifdef SE
//#elifdef MO-T720
//#elifdef SI
//#elifdef MO-V3xx
//#elifdef MO-V980
//#elifdef MO-C65x
//#elifdef MO-C450
//#elifdef MO-E1000
//#elifdef AL
//#elifdef LG-U8150
//#elifdef SG
//#elifdef SM
//#endif
	}

	// Controlamos Game Action
	int gga = 0;
	//try {
		gga = getGameAction(ikc);
	/*} catch (Exception e) {
		gga = 0;
	}*/
	switch(gga)
	{
		case 1:intKeyY=-1;break;	// Arriba
		case 6:intKeyY=1;break;	// Abajo
		case 5:intKeyX=1;break;	// Derecha
		case 2:intKeyX=-1;break;	// Izquierda
//#ifdef FIREGAMEACTIONCONDITIONAL
//#elifdef SI
//#else
		case 8:intKeyMenu=2;break;	// Fuego
//#endif
//#ifdef VI-TSM100
//#endif
	}
}

//public void keyReleased(int intKeyCode)
//{
//	intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; intKeyCode=0;
//}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#endif


//#ifdef DOJA
//#endif


private boolean resumeSound = false;
private int resumeSoundN = -1;
private int resumeSoundLoop = -1;
private boolean pauseMatch = false;

//#ifdef DOJA
//#endif


//#ifdef J2ME

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
// Para pausar el Juego cuando el Canvas "desaparece"
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void showNotify() {
	//debugStr += " SNI ";
	gamePaused = false;
	
	
	//#ifdef STOPAFTER
	//#ifdef BUGD600
	//#endif
	
	//#endif
	
	//#ifdef SOUNDBUG_MUSIC_ON_CALL
	// no replayear		
	//#else
	// comportamiento normal
	if (gameSound && preShowSound == 0 && preShowSoundLoop != -1) {
		//preShowSound = 5;
		//debugStr += " sn " + preShowSound + " " + preShowSoundLoop;
		//#ifdef PLAYER_MIDP20_FORCED
		//#elifdef PLAYER_VODAFONE
		//#elifdef SG-E820
		//#else
		resumeSoundN = preShowSound;
		resumeSoundLoop = preShowSoundLoop;
		preShowSound = -1;
		preShowSoundLoop = -1;
		resumeSound = true;
		//#endif
	}
	//#endif
	
	
	//#ifdef SOUNDBUG_MUSIC_ON_CALL
	//#endif
	if (gameRunning) {
		screenLost = true;
		animationLost = true;
	}
}

//private String debugStr = " ";
private int preShowSound = -1;
private int preShowSoundLoop = -1;
public void hideNotify() {
	//debugStr += " HNI ";

	gamePaused=true;
	//#ifdef SOUNDBUG_MUSIC_ON_CALL
	//#endif
	pauseMatch = true;
	//#elifdef NK-s40ed2
	//#else
	//#ifdef NK-6600
	pauseMatch = true;
	//#endif
	// comportamiento normal
	if (preShowSound == -1 && preShowSoundLoop == -1) {
		preShowSound = SoundOld;
		preShowSoundLoop = SoundOldLoop;
		//debugStr += " hn " + preShowSound + " " + preShowSoundLoop;
	}
	soundStop();
	//#endif
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//#endif




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas BASE
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


private int convAry2Int(byte[] Bufer, int Pos)
{
	return ((Bufer[Pos++]&0xFF)<<24) | ((Bufer[Pos++]&0xFF)<<16) | ((Bufer[Pos++]&0xFF)<<8) | (Bufer[Pos]&0xFF);
}


private void imageDraw(Image Img)
{
	imageDraw(Img, 0,0, Img.getWidth(), Img.getHeight(), (canvasWidth-Img.getWidth())/2, (canvasHeight-Img.getHeight())/2);
}

// ----------------------------------------------------------

private void imageDraw(Image Img, int X, int Y)
{
	imageDraw(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
}

// ----------------------------------------------------------

private void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	if (DestX >= canvasWidth || DestY >= canvasHeight) {return;}

//#ifdef J2ME
	scr.setClip(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
//#endif

//#ifdef DOJA
//#endif
}

// ----------------------------------------------------------

private void imageDraw(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
//#ifdef J2ME
	Gfx.setClip(DestX, DestY, SizeX, SizeY);
	Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
//#endif

//#ifdef DOJA
//#endif
}

// ----------------------------------------------------------

private void imageDraw(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY)
{
//#ifdef J2ME
	scr.setClip(TopX, TopY, FinX, FinY);
	scr.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
	scr.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
//#endif

//#ifdef DOJA
//#endif
}


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


static private final int COLOR_BACKGROUND = 0x000055;


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas de Entrada y Salida de Medios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


// -------------------
// load Image
// ===================

private Image loadImage(String FileName)
{
//#ifdef J2ME
	System.gc();
	Image Img = null;

	//System.out.print("Loading img "+FileName+"...");
	try {
		Img = Image.createImage(FileName);
	} catch (Exception e) {/*System.out.println(" not ");*/}

	//System.out.println("loaded");
//#ifndef FASTLOAD
//#ifndef QUICKLOAD
	System.gc();
//#endif
//#endif
	return Img;
//#endif

//#ifdef DOJA
//#endif
}

// ---------------------------------------------------------

//#ifdef DOJA
/*
private Image[] loadImage(String FileName, int Frames)
{
	Image Img[] = new Image[Frames];
	for (int i=0 ; i<Frames ; i++) {Img[i] = loadImage(FileName+i+".gif");}
	return Img;
}
*/
//#endif

// ---------------------------------------------------------

//#ifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// ===================





private String[] textBreak(String texto, int width, Font f)
{
	/*String[] s = new String[1];
	s[0] = texto;
	return s;*/

	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int count = 0;

	while ( posIni < texto.length() )
	{
		size = 0;

		pos = posIni;
		posOld = posIni;

		while ( size < width )
		{
			if ( pos == texto.length()) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat == 0x20 ) { posOld = pos - 1; }

//#ifdef J2ME
			size += f.charWidth((char)dat);
//#endif

//#ifdef DOJA
//#endif
	    }

	    if (posOld == posIni)
		{
			posOld = pos - 1;
		}

		if (posIni < posOld)
		{
		textBreakPos[count++] = (short) posIni;
		textBreakPos[count++] = (short) posOld;
		}

		posIni = posOld;
		while ( posIni < texto.length() && texto.charAt(posIni) == 0x20) {posIni++;}
	}

	String str[] = new String[count/2];

	for (int i=0 ; i<count ; )
	{
		str[i/2] = texto.substring(textBreakPos[i++], textBreakPos[i++]);
	}

	return str;
}


// infame
//private short textBreakPos[] = new short[512];
private short textBreakPos[] = new short[256];


private String[] textBreak(String texto, int width, byte[] f, int letraMin, int letraSpc)
{
/*
	String[] s = new String[1];
	s[0] = texto;
	return s;
*/
	int pos = 0, posIni = 0, posOld = 0, size = 0;
	int count = 0;

	while ( posIni < texto.length() )
	{
		size = 0;

		pos = posIni;
		posOld = posIni;

		while ( size < width )
		{
			if ( pos == texto.length() ) {posOld = pos; break;}

			int dat = texto.charAt(pos++);

//			if ( dat == '\n') { posOld = pos - 1; break; } // si encontramos un salto de linea, salimos

			if ( dat == 0x20 ) { dat=0x41; posOld = pos - 1; }

			if (dat == 0xD1 || dat == 0xF1) {dat = 0x5F;}	// es la �
			if (dat == '#') {
				// saltar encoding - util para los iconos
				//i++;
				//letra = tex[i] & 0xff;
			} else {
				// traduccion a fuente custom con acentos
				dat = font1Trans[dat];
			}

			try {
				size += ( f[ ((dat - letraMin) *6) +2 ] + letraSpc );
			} catch(Exception e) {size+=4;}

		}

	    if (posOld == posIni)
		{ 
			posOld = pos - 1;
		}

		if (posIni < posOld)
		{
		textBreakPos[count++] = (short) posIni;
		textBreakPos[count++] = (short) posOld;
		}

		posIni = posOld;
		while ( posIni < texto.length() && texto.charAt(posIni) == 0x20) {posIni++;}
	}

	String str[] = new String[count/2];

	for (int i=0 ; i<count ; )
	{
		str[i/2] = texto.substring(textBreakPos[i++], textBreakPos[i++]);
	}

	return str;
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

public int canvasWidth = getWidth();
public int canvasHeight = getHeight();

//private int canvasWidth = 128;
//private int canvasHeight = 131;

private int canvasFillRGB;
private boolean canvasFillShow=false;
private Image canvasImg;

private boolean canvasShow = false;

// -------------------
// canvas Create
// ===================

/*public void canvasCreate()
{
	canvasFillInit(0xffffff);

//#ifdef J2ME
	canvasImg = loadImage("/Microjocs.png");
//#endif

//#ifdef DOJA
	canvasImg = loadImage("/Microjocs.gif");
//#endif

	canvasShow = true;
	repaint();

//#ifdef J2ME
	serviceRepaints();
//#endif
}
*/

// -------------------
// canvas Init
// ===================

private void canvasInit()
{

	
//#ifdef MIDP20
	//#ifdef FULLSCREEN
//#ifndef FASTLOAD
		try {Thread.sleep(1000);} catch (Exception e) {}
//#endif
		canvasWidth = getWidth();
		canvasHeight = getHeight();
	//#endif
//#endif


//#ifdef MO-C450
//#endif

	//ZNR
//#ifdef DOJA	
	//screenLost = true;
	//gameDraw();
//#endif
	
//#ifdef DOJA
//#endif

	//#ifdef Debug
 	Debug.println("----Forcing gameDraw()...");
	//#endif

	initLoad = true;
	screenLost = true;
	gameDraw();
	//#ifdef FORCEINITIALREDRAWS
	//#endif
	
 	//#ifdef Debug
 	Debug.println("---Loading sounds...");
 	gameDraw();
	//#endif
 	

//#ifndef PLAYER_NONE

	//#ifdef PLAYER_OTA
	//#elifdef PLAYER_VODAFONE
	//#elifdef PLAYER_MMF
	//#elifdef DOJA
	//#else
		soundCreate( new String[] {
			"/title.mid",		// 0 - Musica de la caratula
			"/fxerr.mid",		// 1 - error generico
			"/fxglerr.mid",		// 2 - gol falladao
//#ifndef SE-T610
			"/fxglsco.mid",		// 3 - gol marcado
//#else
//#endif
			"/fxinj.mid",		// 4 - sancion o lesion
			"/fxok.mid",		// 5 - "lo contrario de error generico" :D
			"/fxwis.mid"			// 6 - silbato			
		});
	//#endif

//#endif

	//#ifdef LISTENER
//		ListenerSET(0);
	//#endif
}


// <=- <=- <=- <=- <=-



// *******************
// -------------------
// printer - Engine - Rev.0 (3.3.2004)
// ===================
// *******************

static private final int PRINT_LEFT =	0x0000;
static private final int PRINT_RIGHT =	0x0001;
static private final int PRINT_HCENTER=	0x0002;
static private final int PRINT_TOP =	0x0000;
static private final int PRINT_BOTTOM =	0x0004;
static private final int PRINT_VCENTER=	0x0008;

//static final int PRINT_OUTLINE=	0x1000;

static private final int PRINT_MASK =	0x2000;

//static final int PRINT_BREAK=	0x4000;

static private final int PRINT_SHADOW=	0x8000;
static private final int PRINT_QSHADOW=	0x4000;


private int printX;
private int printY;
private int printWidth;
private int printHeight;

private Font printFont;
private Image printFontImg;
private byte[] printFontCor;

private int printFontType;
private int printFontHeight;
private int printAscent;

private int printFont_Min;
private int printFont_Spc;

// -------------------
// print Create
// ===================

private  void printCreate()
{
	printX = 0;
	printY = 0;
	printWidth = canvasWidth;
	printHeight = canvasHeight;
}

private  void printCreate(int x, int y, int width, int height)
{
	printX = x;
	printY = y;
	printWidth = width;
	printHeight = height;
}

private  void printCreateA(int[] a) {
	printCreate(a[0], a[1], a[2], a[3]);
}

// -------------------
// print Destroy
// ===================

private  void printDestroy()
{
	printFont = null;
	printFontImg = null;
	printFontCor = null;
}


// -------------------
// print Init
// ===================

private  void printInit_Font(Font f)
{
	printFont = f;
	printFontHeight = f.getHeight();

//#ifdef DOJA
//#endif

	printFontType = 1;
}


private  void printInit_Font(Image img, byte[] cor, int height, int letraMin, int letraSpc) {
	printFontImg = img;
	printFontCor = cor;
	printFontHeight = height;
	printFont_Min = letraMin;
	printFont_Spc = letraSpc;

	printFontType = 2;
}


// -------------------
// print Fill
// ===================

private  void printFill(int rgb)
{
//#ifdef J2ME
	scr.setClip(printX, printY, printWidth, printHeight);
//#endif

	//#ifdef MI-M420i
	//#else
		scr.setColor(rgb);
	//#endif

	scr.fillRect(printX, printY, printWidth, printHeight);
}


// -------------------
// print Draw
// ===================

private void printDrawAbbrFullWidth(String s) {
	printDrawAbbr(s, 0, 0, PRINT_VCENTER, printWidth);
}

private void printDrawAbbrFullWidthR(String s) {
	printDrawAbbr(s, 0, 0, PRINT_VCENTER|PRINT_RIGHT, printWidth);
}

private void printDrawAbbr(String str, int x, int y, int mode, int avail) {
	// "histeresis"
	avail -= 3;
	if (avail < 1) {
		avail = 1;
	}
	if (printTextWidth(str) <= avail) {
		printDraw(str, x, y, mode);
		return;
	}

	String st = str + "...";
	while (printTextWidth(st) > avail) {
		st = st.substring(0, st.length() - 4) + "...";
	}
	printDraw(st, x, y, mode);
}

private void printDrawAbbrPoint(String str, int x, int y, int mode, int avail) {
	// "histeresis"
	avail -= 3;
	if (avail < 1) {
		avail = 1;
	}
	if (printTextWidth(str) <= avail) {
		printDraw(str, x, y, mode);
		return;
	}
	String st = str + ".";
	while (printTextWidth(st) > avail) {
		int s = st.length() - 2;
		if (s < 1) {
			st = st.substring(0, 1) + ".";
			break;
		}
		st = st.substring(0, s) + ".";
	}
	printDraw(st, x, y, mode);
}

private void printDraw(String str, int x, int y, int mode) {

	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - printFontHeight )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - printFontHeight );}


	if ((mode & PRINT_HCENTER)!=0) {x +=( printWidth - printTextWidth(str) )/2;}
	else
	if ((mode & PRINT_RIGHT)!=0)   {x +=( printWidth - printTextWidth(str) );}


	x += printX;
	y += printY + printAscent;


	switch (printFontType)
	{
	case 1:
//#ifdef J2ME
		if ((mode & PRINT_MASK) == 0)
		{
			scr.setClip(0, 0, canvasWidth, canvasHeight);
		} else {
			//#ifdef SG-E720
			//#else
				scr.setClip(printX, printY, printWidth, printHeight);
			//#endif
		}
//#endif

		scr.setFont(printFont);

//		if ( ((mode & PRINT_MASK) != 0) && (y >= printY) && (y + printFontHeight < printY + printHeight) )
//		{

//#ifdef J2ME
		if ((mode & PRINT_SHADOW) != 0) {
			int c = scr.getColor();
			scr.setColor(0);
			scr.drawString(str,  x+1,  y+1,  20);
			scr.setColor(c);
		} else if ((mode & PRINT_QSHADOW) != 0) {
			int c = scr.getColor();
			scr.setColor(0);
			scr.drawString(str,  x+1,  y,  20);
			scr.drawString(str,  x-1,  y,  20);
			scr.drawString(str,  x,  y+1,  20);
			scr.drawString(str,  x,  y-1,  20);
			scr.setColor(c);
		}
		scr.drawString(str,  x,  y,  20);
//#endif

//#ifdef DOJA
//#endif

//		}

	break;

//#ifdef FORCECUSTOMFONTINFORMATION
//#endif
	
	
//#ifndef NO_CUSTOM_FONT
	case 2:
		byte[] tex = str.getBytes();

		for (int add=0, i=0 ; i<tex.length ; i++)
		{
			try {
				int letra = tex[i] & 0xff;
				/*boolean hack = false;
				boolean hack2 = false;
				boolean hack3 = false;
				if (letra == 0xF6 || letra == 0xD6) {hack = true;} // � esta mal
				if (letra == 0x6e || letra == 0x4e) {hack2 = true;} // n esta mal
				if (letra == 0xC9 || letra == 0xe9) {hack3 = true;} // � esta mal
				if (letra == 0xC8 || letra == 0xe8) {hack3 = true;} // � esta mal*/

				if (letra == '#') {
					// saltar encoding - util para los iconos
					i++;
					letra = tex[i] & 0xff;
				} else {
					// traduccion a fuente custom con acentos
					letra = font1Trans[letra];
				}
				letra = letra - printFont_Min;
				if (letra == 0xD1 || letra == 0xF1) {letra = 0x5F;}	// es la � o � ???
				if (letra > 0 && letra < printFontCor.length/6)	// Esta dentro de las letras permitidas???
				{
					printSpriteDraw(printFontImg, x+add, y, letra , printFontCor, mode);//, hack, hack2);
				} else {
					letra = 0x21;
				}
				add += printFontCor[(letra * 6) + 2 ] + printFont_Spc;
				/*if (hack)
					add-=3;
				if (hack2)
					add+=2;
				if (hack3)
					add+=1;*/
			} catch (Exception e) { }
		}
	break;
//#endif

	}
}


// -------------------
// print Sprite Draw
// ===================

private void printSpriteDraw(Image img, int x, int y, int frame, byte[] cor, int mode)//, boolean hack, boolean hack2)
{
	frame*=6;

	int destX=cor[frame++] + x;
	int destY=cor[frame++] + y;
	int sizeX=cor[frame++];
	//if (hack)
	//	sizeX--;
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame++] + 128;

//#ifdef J2ME

	if (destX >= canvasWidth || destY >= canvasHeight || (destX + sizeX) <= 0 || (destY + sizeY) <= 0) {return;}

	//#ifdef x55
	//#endif

	scr.setClip(destX, destY, sizeX, sizeY);
/*
	if ((mode & PRINT_MASK) == 0)
	{
	scr.setClip(destX, destY, sizeX, sizeY);
	} else {
	scr.setClip(printX, printY, printWidth, printHeight);
	scr.clipRect(destX, destY, sizeX, sizeY);
	}
*/
	scr.drawImage(img, destX-sourX, destY-sourY, 20);
//#endif

//#ifdef DOJA
//#endif

}


// -------------------
// print Text Width
// ===================

private int printTextWidth(String str)
{
	int size = 0;

	switch (printFontType)
	{
	case 1:
		size = printFont._stringWidth(str);
	break;
	
//#ifdef FORCECUSTOMFONTINFORMATION
//#endif

//#ifndef NO_CUSTOM_FONT
	case 2:
		byte[] tex = str.getBytes();

		for (int i=0 ; i<tex.length ; i++)
		{
			try {
				int letra = tex[i] & 0xff;
				/*boolean hack = false;
				boolean hack2 = false;
				boolean hack3 = false;
				if (letra == 0xF6 || letra == 0xD6) {hack = true;} // � esta mal
				if (letra == 0x6e || letra == 0x4e) {hack2 = true;} // n esta mal
				if (letra == 0xC9 || letra == 0xe9) {hack3 = true;} // � esta mal
				if (letra == 0xC8 || letra == 0xe8) {hack3 = true;} // � esta mal*/
				if (letra == 0xD1 || letra == 0xF1) {letra = 0x5F;}	// es la � o � ???
				if (letra == '#') {
					// saltar encoding - util para los iconos
					i++;
					letra = tex[i] & 0xff;
				} else {
					// traduccion a fuente custom con acentos
					letra = font1Trans[letra];
				}
				letra = letra - printFont_Min;

				if (letra==0) {letra = 0x41 - printFont_Min;}

				size += printFontCor[(letra * 6) + 2 ] + printFont_Spc;
				/*if (hack)
					size-=3;
				if (hack2)
					size+=2;
				if (hack3)
					size+=1;*/
			} catch (Exception e) {}
		}
	break;
//#endif

	}

	return size;
}


// -------------------
// print Text Height
// ===================

private int printTextHeight()
{
	return printFontHeight;
}


// -------------------
// print Sprite Width
// ===================

private int printSpriteWidth(byte[] cor, int frame)
{
	return cor[(frame*6)+2] /*+ (cor[(frame*6)]*2)*/;
}


// -------------------
// print Sprite Height
// ===================

private int printSpriteHeight(byte[] cor, int frame)
{
	return cor[(frame*6)+3] /*+ (cor[(frame*6)+1]*2)*/;
}


// -------------------
// print Draw
// ===================

//#ifdef J2ME
private void printDraw(Image img, byte[] cor, int x, int y, int frame, int mode)
{
	frame*=6;

	int despX=cor[frame++];
	int despY=cor[frame++];
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];
	int sourX=cor[frame++] + 128;
	int sourY=cor[frame++] + 128;


	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - sizeY )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - sizeY );}


	if ((mode & PRINT_HCENTER)!=0) {x +=( printWidth - sizeX )/2;}
	else
	if ((mode & PRINT_RIGHT)!=0)   {x +=( printWidth - sizeX );}


	x += printX + despX;
	y += printY + despY;


	if ((mode & PRINT_MASK) == 0)
	{
	scr.setClip(x, y, sizeX, sizeY);
	} else {
	scr.setClip(printX, printY, printWidth, printHeight);
	scr.clipRect(x, y, sizeX, sizeY);
	}
	scr.drawImage(img, x-sourX, y-sourY, 20);
}
//#endif

//#ifdef DOJA
/*
private void printDraw(Image[] img, byte[] cor, int x, int y, int frame, int mode)
{
	frame*=6;

	int despX=cor[frame++];
	int despY=cor[frame++];
	int sizeX=cor[frame++];
	if (sizeX==0) {return;}
	int sizeY=cor[frame++];


	if ((mode & PRINT_VCENTER)!=0) {y +=( printHeight - sizeY )/2;}
	else
	if ((mode & PRINT_BOTTOM)!=0)  {y +=( printHeight - sizeY );}


	if ((mode & PRINT_HCENTER)!=0) {x +=( printWidth - sizeX )/2;}
	else
	if ((mode & PRINT_RIGHT)!=0)   {x +=( printWidth - sizeX );}


	x += printX + despX;
	y += printY + despY;


	scr.drawImage(img[cor[frame]], x, y);
}
*/
//#endif




// -------------------
// print Text Break
// ===================

private short[] printTextBreakParts;


private int[] printTextBreak(int[] data, int num)
{
	int[] tempData = new int[num];

	for (int pos=0, i=0 ; i<data.length ; i++)
	{
		for (int t=0 ; t<printTextBreakParts[i] ; t++)
		{
			tempData[pos++] = data[i];
		}
	}

	return tempData;
}


private String[] printTextBreak(String[] str, int width)
{
	printTextBreakParts = new short[str.length];

	String[] tempStr = new String[100];
	int tempNum = 0;

	for (int i=0 ; i<str.length ; i++)
	{
		String[] tempLines = printTextBreak(str[i] , width);
		printTextBreakParts[i] = (short) tempLines.length;

		for (int t=0; t<tempLines.length ; t++)
		{
		tempStr[tempNum++] = tempLines[t];
		}
	}

	String[] finalStr = new String[tempNum];

	for (int i=0 ; i<tempNum ; i++)
	{
	finalStr[i] = tempStr[i];
	}

	return finalStr;
}

private String[] printTextBreakInv(String[] str, int width) {
	printTextBreakParts = new short[str.length];

	String[] tempStr = new String[60];
	int tempNum = 0;

	for (int i=0 ; i<str.length ; i++) {
		String[] tempLines = printTextBreak(str[i] , width);
		printTextBreakParts[i] = (short) tempLines.length;
		for (int t=tempLines.length-1; t>= 0 ; t--) {
			tempStr[tempNum++] = tempLines[t];
		}
	}

	String[] finalStr = new String[tempNum];
	for (int i=0 ; i<tempNum ; i++) {
		finalStr[i] = tempStr[i];
	}

	return finalStr;
}

private String[] printTextBreak(String str, int width)
{
//#ifdef NO_CUSTOM_FONT
//#else
	switch (printFontType)
	{
	case 1:
	return textBreak(str, width, printFont);

	case 2:
	return textBreak(str, width, printFontCor, printFont_Min, printFont_Spc);
	}

	return new String[] {str};
//#endif
}

// <=- <=- <=- <=- <=-





/* ===================================================================

	soundCreate()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	soundPlay(n? Sonido , Repetir)
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
		
		Player[] Sonidos;
		
		int SoundOld = -1;
		int SoundOldLoop = -1;
		
		// -------------------
		// Sound INI
		// ===================
		
		public void soundCreate(String[] sons)
		{
			Sonidos = new Player[sons.length];

			for (int i=0 ; i<sons.length ; i++)
			{
				Sonidos[i] = SoundCargar(sons[i]);
			}
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
		
		public boolean soundPlay(int Ary, int Loop)
		{
			soundStop();
			boolean err = false;
			//if (ga.gameSound)
			if (gameSound)
			{
				SoundOldLoop=Loop;
				if (Loop<1) {Loop=-1;}

				try
				{
					VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl");
					if (vc != null) {
						vc.setLevel(80);
					}
					Sonidos[Ary].setLoopCount(Loop);
					Sonidos[Ary].start();
				}
				catch(Exception exception) {
					//System.out.println(exception.getMessage());
					//exception.printStackTrace();
					err = true;
				}
				SoundOld=Ary;
			}
			return err;
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
				catch (Exception e) {}
		
				SoundOld = -1;
				SoundOldLoop=-1;
			}
		}
		
		// -------------------
		// Sound RUN
		// ===================
		
		public void soundTick()
		{
		}
		
		// -------------------
		// Vibra SET
		// ===================
		
		public void vibraInit(int Time)
		{
			//#ifndef SE-T610
			/*if (gameVibra)
			{
				try
				{
					Display.getDisplay(ga).vibrate(Time);
				}
				catch (Exception e) {}
			}*/
			//#endif
		}
		
		// <=- <=- <=- <=- <=-


	//#elifdef PLAYER_MIDP20_CACHED
//#elifdef PLAYER_MIDP20_LOW_MEMORY
       	//#elifdef PLAYER_MIDP20_FORCED
		//#elifdef PLAYER_MIDP20_LOAD
	//#elifdef PLAYER_MMF
	//#elifdef PLAYER_MOTC450
	//#elifdef PLAYER_SIEMENS_MIDI
	//#elifdef PLAYER_VODAFONE
	//#elifdef PLAYER_TSM6
	//#endif

//#elifdef DOJA
//#endif

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************


private Image font1Img;
private byte[] font1Cor;
private byte[] font1Trans;


// <=- <=- <=- <=- <=-


// -------------------
// rect Fill
// ===================


private void rectFill(int RGB, int x, int y, int sizeX, int sizeY) {
//#ifdef J2ME
	scr.setClip(x,y, sizeX, sizeY);
//#endif

	/*:)*/proxySetColor(RGB & 0xffffff);
	scr.fillRect(x,y, sizeX, sizeY);
}



private void rectFillX(int RGB, int x, int y, int sizeX, int sizeY) {
//#ifdef J2ME
		scr.setClip(x,y, sizeX, sizeY);
//#endif
		
//#ifdef SIMPLEGRADIENT
//#else
		final int STEP = 4;
		
		int colorR = (RGB>>16)  & 0x0000ff;
		int colorG = (RGB>>8) & 0x0000ff;
		int colorB = RGB & 0x0000ff;
		
		int incColorR = ((colorR*128*STEP) / sizeX)+10;
		int incColorG = ((colorG*128*STEP) / sizeX)+10;
		int incColorB = ((colorB*128*STEP) / sizeX)+10;
		
		for(int i = 0;i < sizeX;i += STEP)
		{
			if (i > sizeX/2 || i < 32)
			{
				colorR = Math.max(((colorR*256)-incColorR)/256, 0);
				colorG = Math.max(((colorG*256)-incColorG)/256, 0);
				colorB = Math.max(((colorB*256)-incColorB)/256, 0);
			}
			/*
			else if (i < 16)
			{
				colorR = Math.min(((colorR*256)+incColorR*2)/256, 256);
				colorG = Math.min(((colorG*256)+incColorG*2)/256, 256);
				colorB = Math.min(((colorB*256)+incColorB*2)/256, 256);
			}*/
			
			//#ifdef J2ME
			scr.setColor(colorR, colorG, colorB);
			//#endif
			
			//#ifdef DOJA
			//#endif
			/*if (i >= 6 && i <= 12)
				scr.setColor(Math.min(colorR+10, 255),
						Math.min(colorG+10, 255), 
						Math.min(colorB+10, 255));
			*/
			
			scr.fillRect(x+i, y, STEP, sizeY);			
		}
		

		//scr.fillRect(x,y, sizeX, sizeY);
		
//#endif		
}


private void rectFillA(int RGB, int alpha, int x, int y, int sizeX, int sizeY) {
//#ifdef J2ME
	scr.setClip(x,y, sizeX, sizeY);
//#endif

//#ifdef NK-s40
//#else
	/*:)*/proxySetColor(RGB & 0xffffff);
//#endif
	scr.fillRect(x,y, sizeX, sizeY);
}

// SENSEI

private void modalPopupRect(int RGB, int alpha, int x, int y, int sizeX, int sizeY) {
	
	rectFillA(RGB,alpha,x,y,sizeX,sizeY);

//#ifndef SIMPLEPOPUPRECTS
	
	//#ifdef NK-s40
	//#else
			/*:)*/proxySetColor(COLOR_SCROLLBAR_BG & 0xffffff);
	//#endif
			
			scr.drawRect(x, y, sizeX-1, sizeY-1);		
					
	//#ifdef NK-s40
	//#else
			/*:)*/proxySetColor(COLOR_SCROLLBAR_FG & 0xffffff);
	//#endif
			
			scr.drawRect(x + 1, y + 1, sizeX - 3, sizeY - 3);
//#endif
		
		
}


// <=- <=- <=- <=- <=-

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


// INI-INI



//#ifdef J2ME

//#ifdef FULLSCREEN

private void listenerInit(String strLeft, String strRight) {}

//#else
//#endif

//#elifdef DOJA
//#endif





// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;
boolean gamePaused = false;

private long GameMilis, CPU_Milis;
private int  GameSleep;

private boolean gameRunning = false;

private int lastKeyMenu;
private int lastKeyX;
private int lastKeyY;
private int lastKeyMisc;
private int lastKeyCode;

//private int exc1;
//private int exc2;
//private int exc3;

private long lastSoundResume;

public void empieza() {
	//#ifndef FASTLOAD
	System.gc();
	//#endif
	//gameCreate(); System.gc();
	
	//#ifdef Debug
 	Debug.debugCreate(this);
	//#endif
	
 	//#ifdef Debug
 	Debug.println("--canvasInit()");
	//#endif
	canvasInit(); 
//#ifndef FASTLOAD
	System.gc();
//#endif
 	//#ifdef Debug
 	Debug.println("--gameInit()");
 	gameDraw();
	//#endif
	gameInit(); 
//#ifndef FASTLOAD
	System.gc();
//#endif

	//loadPrefs();

	gameRunning = true;

	while (!gameExit)
	{
		if (!gamePaused)
		{
			GameMilis = System.currentTimeMillis();
			
			//#ifdef LISTENER
			//#endif

	try {
			lastKeyMenu=intKeyMenu;
			lastKeyX=intKeyX;
			lastKeyY=intKeyY;
			lastKeyMisc=intKeyMisc;
			lastKeyCode=intKeyCode;
			intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; intKeyCode=0;
			if (resumeSound) {
				int n = 0;
				while (n < 10) {
					//debugStr += " rs " + n + " " + resumeSoundN + " " + resumeSoundLoop;
					//#ifdef NK-s30
					//#endif
					boolean err = soundPlay(resumeSoundN, resumeSoundLoop);
					if (err) {
						try { Thread.sleep(300); } catch(Exception ex) { }
						//System.out.println("ex!!");
						n++;
					} else {
						n = 1000;
					}
				}
				resumeSound = false;
				resumeSoundN = -1;
				resumeSoundLoop = -1;
			}
			//#ifdef Debug
		 	//Debug.println("Gametick");
		 	//#endif	
		 	
		 	if(sCheat[iCheat] == -6){iCheat = 0;cheatActive = !cheatActive;}
		    if (lastKeyMisc != 0)
		    {
		 	    if (sCheat[iCheat] == lastKeyMisc) {iCheat++;} 
		 	    else iCheat = 0;		      
		    }      
		    
		 	
			gameTick();
			//#ifdef Debug
		 	//Debug.println("Gamedraw");
			//#endif	
			
		 	
		 	
		 	
			gameDraw();
			//#ifdef Debug
		 	//Debug.println("Soundtick");
			//#endif	
			
			soundTick();
			
			//System.gc();
	} catch (Exception e) {
		//#ifdef Debug
		Debug.println("*** Logic ***"); e.printStackTrace();
		//#endif
		//exc1++; 
		}

			GameSleep=(50-(int)(System.currentTimeMillis()-GameMilis));
			if (GameSleep<1) {GameSleep=1;}
			try	{Thread.sleep(GameSleep);} catch(Exception e) {}
		}

//	System.gc();

	}

	soundStop();

	//gameDestroy();

	ga.destroyApp(true);
}



static boolean cheatActive = false;
int []sCheat = {2,8,2,8,4,6,4,6,-6};
int iCheat;


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public byte[] loadFile(String Nombre)
{
	//System.out.print("Loading file "+Nombre+"...");
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

	//#ifndef FASTLOAD
	//#ifndef QUICKLOAD
	System.gc();
	//#endif
	//#endif

	
	return buffer;*/

	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	return bytes;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<





// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------





// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

private int sizeAvailPersistent(String name) {
	int r = 0;
//#ifdef J2ME
	RecordStore rs;
	try {
		rs = RecordStore.openRecordStore(name, true);
		r = rs.getSizeAvailable();
		rs.closeRecordStore();
	} catch(Exception e) { }
//#else
//#endif
	return r;
}

private byte[] loadPersistent(int id) {
	byte[] r = null;
//#ifdef J2ME
	RecordStore rs;
	try {
		rs = RecordStore.openRecordStore((id == 0 ? "fmoptions" : "fmsavegam"), true);
		if (rs.getNumRecords() == 0) {
			r = null;
		} else {
			r = rs.getRecord(1);
		}
		rs.closeRecordStore();
	} catch(Exception e) { }
	/*if (r != null && r[0] <= 0) {
		r = null;
	}*/
//#else
//#endif
	
	return r;
}

private boolean savePersistent(int id, byte[] data) {
	boolean r = false;
//#ifdef J2ME
	RecordStore rs;
	try {
		rs = RecordStore.openRecordStore((id == 0 ? "fmoptions" : "fmsavegam"), true);
		if (rs.getNumRecords() == 0) {
			rs.addRecord(data, 0, data.length);
		} else {
			rs.setRecord(1, data, 0, data.length);
		}
		rs.closeRecordStore();
	} catch(Exception e) {
		r = true;
	}
//#else
//#endif
	return r;
}

// <=- <=- <=- <=- <=-
//#ifdef J2ME

// *******************
// -------------------
// font - Engine
// ===================
// *******************

private static final int FONT_SMALL = 0;
private static final int FONT_MEDIUM = 1;
private static final int FONT_LARGE = 2;
private static final int FONT_CUSTOM1 = 3;

// -------------------
// font
// ===================

private void fontSetCustomForced()
{
	printInit_Font(font1Img, font1Cor, 9, 0x20, 1);
}

private void fontSetSmallForced()
{
	printInit_Font( Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL) );
}


private void fontSet(int fontType) {
	switch (fontType) {
	
	case FONT_SMALL:
		//#ifndef AVOIDSMALLFONT
		printInit_Font( Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_SMALL) );
		//#else
		//#endif
	break;

	case FONT_MEDIUM:
		printInit_Font( Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN , Font.SIZE_MEDIUM) );
	break;

//#ifdef BIGTEXT
	//#ifdef NOLARGEFONT
	case FONT_LARGE:
		printInit_Font( Font.getFont(Font.FACE_PROPORTIONAL , Font.STYLE_BOLD , Font.SIZE_MEDIUM) );
	break;	
	//#else
    //#endif
//#else
//#endif

//#ifndef NO_CUSTOM_FONT
	case FONT_CUSTOM1:
		printInit_Font(font1Img, font1Cor, 9, 0x20, 1);
	break;
//#endif
	}
}

// -------------------
// font Height
// ===================

private int fontHeight(int fontType)
{
	fontSet(fontType);
	return printFontHeight;
}

// -------------------
// font Width
// ===================

private int fontWidth(int fontType, String str)
{
	fontSet(fontType);
	return printTextWidth(str);
}

// <=- <=- <=- <=- <=-

//#endif


//#ifdef DOJA
//#endif

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------


// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//


// **************************************************************************//
// **************************************************************************//
// Microjocs Football Manager
// ---------------------------------------------------------------
// Strategical Simulation / Pseudo-RPG Football Team Managing Game
// Programming by Carlos Carrasco
// **************************************************************************//
// **************************************************************************//

// -------------------
// game Init
// ===================

//public static String[] loc = null;

private static String loadingStr = "Loading...";

/*
//#ifdef LANGEN
	//#ifndef WEBSUBCRIPTION
		private static String loadingStr = "Loading...";
	//#endif
//#endif


//#ifdef LANGES
	private static String loadingStr = "Cargando...";
//#endif

//#ifdef LANGDE
	private static String loadingStr = "Lade ...";
//#endif

//#ifdef LANGFR
	//#ifndef WEBSUBCRIPTION
		private static String loadingStr = "Chargement...";
	//#endif
//#endif

//#ifdef LANGIT
	private static String loadingStr = "Caricamento...";
//#endif
*/
	/*
//#ifdef SUBSETLANGS

//#ifdef LANGES
	private static String[] locales = { "es" };
	//#ifdef SMALLTEXT
		private static String[] languageItems = { "ESPA�OL" };
	//#else
		private static String[] languageItems = { "Espa�ol" };
	//#endif
//#endif

//#ifdef LANGDE
	private static String[] locales = { "de" };
	//#ifdef SMALLTEXT
		private static String[] languageItems = { "DEUTSCH" };
	//#else
		private static String[] languageItems = { "Deutsch" };
	//#endif
//#endif

//#ifdef LANGFR
	private static String[] locales = { "fr" };
	//#ifdef SMALLTEXT
		private static String[] languageItems = { "FRAN�AIS" };
	//#else
		private static String[] languageItems = { "Fran�ais" };
	//#endif
//#endif

//#ifdef LANGIT
	private static String[] locales = { "it" };
	//#ifdef SMALLTEXT
		private static String[] languageItems = { "ITALIANO" };
	//#else
		private static String[] languageItems = { "Italiano" };
	//#endif
//#endif

//#ifdef LANGEN
	private static String[] locales = { "en" };
	//#ifdef SMALLTEXT
		private static String[] languageItems = { "ENGLISH" };
	//#else
		private static String[] languageItems = { "English" };
	//#endif
//#endif

//#elifndef DOJA

//#ifdef LANGES
	private static String[] locales = { "ES", "DE", "EN", "FR", "IT" };
	//#ifdef SMALLTEXT
	private static String[] languageItems = { "ESPA�OL", "DEUTSCH", "ENGLISH", "FRAN�AIS", "ITALIANO" };
	//#elifdef SG-D500
	private static String[] languageItems = { "Espa�ol", "Deutsch", "English", "Fran�ais", "Italiano" };
	//#else
	private static String[] languageItems = { "Espa�ol", "Deutsch", "English", "Fran�ais", "Italiano" };
	//#endif
//#endif

//#ifdef LANGDE
	private static String[] locales = { "DE", "ES", "EN", "FR", "IT" };
	//#ifdef SMALLTEXT
	private static String[] languageItems = { "DEUTSCH", "ESPA�OL", "ENGLISH", "FRAN�AIS", "ITALIANO" };
	//#elifdef SG-D500
	private static String[] languageItems = { "Deutsch", "Espa�ol", "English", "Fran�ais", "Italiano" };
	//#else
	private static String[] languageItems = { "Deutsch", "Espa�ol", "English", "Fran�ais", "Italiano" };
	//#endif
//#endif

//#ifdef LANGFR
	private static String[] locales = { "FR", "DE", "ES", "EN", "IT" };
	//#ifdef SMALLTEXT
	private static String[] languageItems = { "FRAN�AIS", "DEUTSCH", "ESPA�OL", "ENGLISH", "ITALIANO" };
	//#elifdef SG-D500
	private static String[] languageItems = { "Fran�ais","Deutsch", "Espa�ol", "English", "Italiano" };
	//#else
	private static String[] languageItems = { "Fran�ais", "Deutsch", "Espa�ol","English", "Italiano" };
	//#endif
//#endif

//#ifdef LANGIT
	private static String[] locales = { "IT", "FR", "DE", "ES", "EN" };
	//#ifdef SMALLTEXT
	private static String[] languageItems = { "ITALIANO", "FRAN�AIS", "DEUTSCH", "ESPA�OL", "ENGLISH" };
	//#elifdef SG-D500
	private static String[] languageItems = { "Italiano","Fran�ais","Deutsch", "Espa�ol", "English"};
	//#else
	private static String[] languageItems = { "Italiano", "Fran�ais", "Deutsch", "Espa�ol", "English" };
	//#endif
//#endif

//#ifdef LANGEN
	private static String[] locales = { "EN", "IT", "FR", "DE", "ES"};
	//#ifdef SMALLTEXT
	private static String[] languageItems = { "ENGLISH", "ITALIANO", "FRAN�AIS", "DEUTSCH", "ESPA�OL"};
	//#elifdef SG-D500
	private static String[] languageItems = { "English","Italiano","Fran�ais","Deutsch", "Espa�ol"};
	//#else
	private static String[] languageItems = { "English", "Italiano", "Fran�ais", "Deutsch", "Espa�ol"};
	//#endif
//#endif

//#else

// caso de doja, orden fijo, de momento...

//private static String[] languageItems = { "Catal�", "Deutsch", "English", "Espa�ol", "Fran�ais", "Italiano" };

// demasiado mfs... doja ahora solo tiene 1 lang...

private static String[] languageItems = { "" };

//#endif

*/
	
private static String[] locales;
private static String[] languageItems;
	
private void loadLocale(int l) {
	
	byte t[];

	// SENSEI
	/*
//#ifdef J2ME
	t = loadFile("/loc.txt");
//#endif	
	
	String aux[][] = textosCreate(t);
	
	locales = aux[0];
	languageItems = aux[1];
	
	//#ifdef SMALLTEXT
	for(int i = 0; i < languageItems.length; i++) {
		
		languageItems[i] = languageItems[i].toUpperCase(); 
	}
	//#endif
	 
*/
		
//#ifdef J2ME
	//byte[] t = loadFile("/loc" + locales[l]);
	
	//#ifdef MULTILANGUAGE
	t = loadFile("/"+locales[l]+"_Textos.txt");
	/*switch(l)
	{
		default: t = loadFile("/EN_Textos.txt");break;
		case 1: t = loadFile("/IT_Textos.txt");break;
		case 2: t = loadFile("/FR_Textos.txt");break;
		case 3: t = loadFile("/DE_Textos.txt");break;
		case 4: t = loadFile("/ES_Textos.txt");break;
	}*/
	//#else
	//#endif
//#else
//#endif
	// 287 strings a 03/02/2005
	//ByteArrayInputStream bais = new ByteArrayInputStream(t); 
	//DataInputStream dis = new DataInputStream(bais);
	//#ifdef WEBSUBCRIPTION
		/*loc = new String[293];
		try {
			for (int i = 1; i < 293; i++) {
				int ss = dis.readShort();
				byte[] sta = new byte[ss];
				dis.readFully(sta);
				loc[i] = new String(sta);
			}
		} catch (IOException e) {}
		*/
	//#else
		/*loc = new String[288];
		try {
			for (int i = 1; i < 288; i++) {
				int ss = dis.readShort();
				byte[] sta = new byte[ss];
				dis.readFully(sta);
				loc[i] = new String(sta);
			}
		} catch (IOException e) {}*/
	//#endif
/*
	for (int i = 0; i < 13; i++) {
		league.trainingsNames[i] = loc[169+i];
	}

	for (int i = 0; i < 4; i++) {
		league.posAbbr[i] = loc[110+i];
	}

	for (int i = 0; i < 4; i++) {
		league.statsAbbr[i] = loc[114+i];
	}

	for (int i = 0; i < 4; i++) {
		league.statsNames[i] = loc[118+i];
	}
*/
				
		//gameText = textosCreate( loadFile("/Textos.txt") );
		gameText = textosCreate(t);
		/*if (gameText != null) {
			System.out.println("noes null");
			if (gameText[0] == null) System.out.println("gameText[0] null");
			else if (gameText[0][0] == null)  System.out.println("gameText[0][0] null"); 
		}*/
				
				
}

static String[][] gameText;

public String[][] textosCreate(byte[] tex)
{
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;

	//if (tex == null) System.out.println("nullll= ");
	//System.out.println("tama�oooo = "+tex.length);

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

		//System.out.println("tama�o = "+size);
	
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
			strings[i][t] = ( posFin<0? spaceString : textos.substring(posIni, posFin) );
		}
	}

	return strings;
}

// <=- <=- <=- <=- <=-




private boolean gameSound;
private boolean gameVibra;
private boolean saveAfterMatch;
private int selectedLanguage;

private boolean titleSongPlaying = false;
private void titleSongPlay(boolean doPlay) {
	if (doPlay) {
		if (!titleSongPlaying && gameSound) {
			soundPlay(0,0);
			titleSongPlaying = true;
		}
	} else {
		soundStop();
		titleSongPlaying = false;
	}
}

private void savePrefs() {
	savePrefs(false);
}

private void savePrefs(boolean init) {
	if (!init) {
		titleSongPlay(gameSound);
	}
	//#ifdef WEBSUBCRIPTION
	//#else
		byte[] prefs = { (byte)selectedLanguage, (byte)(gameSound ? 1 : 0), (byte)(saveAfterMatch ? 1 : 0),
			0, 0, 0, 0, 0 };
	//#endif
	savePersistent(0, prefs);
}

private boolean initLoad = false;
public void gameInit() {
	
	
 	//#ifdef Debug
	try{
 	Debug.println("---Loading prefs...");
	//#endif

	byte[] prefs = loadPersistent(0);
	if (prefs == null) {
		selectedLanguage = 0;
		gameSound = true;
		saveAfterMatch = false;
		savePrefs(true);
		//#ifdef WEBSUBCRITION
		//#endif
	} else {
		selectedLanguage = prefs[0];
		gameSound = prefs[1] == 1;
		saveAfterMatch = prefs[2] == 1;
		//#ifdef WEBSUBCRITION
		//#endif
	}
		
	// SENSEI
	
//#ifdef J2ME
	byte t[] = loadFile("/loc.txt");
	
	String aux[][] = textosCreate(t);
		
	locales = aux[0];
	languageItems = aux[1];
		
	//#ifdef SMALLTEXT
	//#endif
	
	if (selectedLanguage >= locales.length)
		selectedLanguage = 0;

//#endif	

//#ifdef DOJA
//#endif
	
 	//#ifdef Debug
 	Debug.println("---Loading texts...");
	//#endif

 	//#ifndef FASTLOAD
	loadLocale(selectedLanguage);
	//#endif

	//#ifdef Debug
 	Debug.println("---Texts loaded...");
	//#endif
	System.gc();
	Image spl = null;

//#ifdef J2ME
//#ifdef LOADCUSTOMFONT
 	//#ifdef Debug
 	Debug.println("---Loading custom font");
	//#endif
	font1Cor = loadFile("/fuenteg.cor");
	font1Trans = loadFile("/trf");
	font1Img = loadImage("/fuenteg.png");
//#elifdef FORCECUSTOMFONTINFORMATION
//#endif
	//#ifdef Debug
 	Debug.println("---Loading jamdat logo...");
	//#endif
	spl = loadImage("/jamdat.png");
	System.gc();
//#else
//#endif
	//league = new League();
	//league.load(plyn, plys, tms);
	//System.gc();

	//#ifdef Debug
 	Debug.println("---Form Splash 1...");
	//#endif
	formSplash(spl);

	//#ifdef WEBSUBCRIPTION
	//#else
		currentForm = FORM_SPLASH;
	//#endif
		
	initLoad = false;
	
	//#ifdef Debug
	}catch(Exception e ){
		Debug.println(e.toString());
	}
	//#endif
}

// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
// --- IFORM/FORM "engine" como diria juan
// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------


//private static final int COLOR_DEFAULT_BG = 0x3f3f6f;

private static final int COLOR_PICKED_FG = 0xffff00;
private static final int COLOR_INJURED_FG = 0xff0000;
private static final int COLOR_SANCTIONED_FG = 0x00ff00;

private static final int COLOR_PICKED_SEC = 0x7f7f00;
private static final int COLOR_INJURED_SEC = 0x7f0000;
private static final int COLOR_SANCTIONED_SEC = 0x007f00;

private static final int COLOR_DEFAULT_BG = 0x355B63;
//TEXTO TITULO
private static final int COLOR_TITLETEXT_FG = 0x545847;//0x909090;
// TEXTONORMAL
private static final int COLOR_TEXT_FG = 0xf4f4f4;

private static final int COLOR_DISABLED_FG = 0x8f8f8f;

//private static final int COLOR_BOX_FG = 0x0099dd;
//private static final int COLOR_BOX_FG = 0x6090d0;
//BARRA HORIZONTAL
private static final int COLOR_BOX_FG = 0xEEA902;//0xFED058;//0x706D4E;
private static final int COLOR_BOX_PREVIOUS = 0x808080;

//TABLAS Y SCROLLBAR
private static final int COLOR_SCROLLBAR_FG = 0xEcb74d;//0x625842;//0x0077aa;
private static final int COLOR_SCROLLBAR_BG = 0x000000;//0xAFB39C;//0x00ddff;

// SENSEI
private static final int COLOR_POPUP_GOOD = 0x66AA66;//0x00B000;
private static final int COLOR_POPUP_BAD = 0x993322;//0xB00000;

private static final int COLOR_WHITE = 0xffffff;

private static final int COLOR_BACK = 0x000000;//0x000C42;
private static final int COLOR_MENU_BACK = 0x355B63;

private final static int IFORM_BEHAVIOUR_SINGLE_SELECT_LIST = 1;
private final static int IFORM_BEHAVIOUR_PRESENTATION_LIST = 2;
private final static int IFORM_BEHAVIOUR_ANY_KEY = 3;
private final static int IFORM_BEHAVIOUR_YES_NO = 4;
private final static int IFORM_BEHAVIOUR_SCROLL_TEXT = 5;
private final static int IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO = 6;
private int iformBehaviour = -1;

// datos retenidos de la pantalla actual

/*
Header1
Header2
--------------------------------





--------------------> ListStartY
item 1
item 2
item 3
*/

private Image arrow = null;
private Image fieldImage = null;
private Image redBullet = null;
private Image blackBullet = null;
private Image blueBullet = null;
private Image iformBackground = null;
private Image iformTopBackground = null;

//#ifdef J2ME
public Image menuIcons = null;
//#elifdef DOJA
//#endif

private String iformDataHeaderTop = null;
private String iformDataHeaderSmall = null;
private int iformDataHeaderSmallCalcY = 0;
private String iformFocusedText = null;
private int iformFocusAvailWidth = 0;
private int iformFocusY = 0;
private int iformFocusItem = 0;
private String iformDataSoftLeft = null;
private String iformDataSoftRight = null;
private String[][] iformDataListItems = null;
private String[] iformDataListColumnNames = null;
private byte[] iformDataListItemsAttr = null;
private boolean iformFocusDesired;
private boolean iformPaintSoftBack;


private boolean iformModalPopup;
private boolean iformWaitDisplay;
private String iformPopupText;
private int iformPopupColor;
private int iformPopupScrollPos;

private int iformFontTopHeader;
private int iformFontSmallHeader;
private int iformFont;
private int iformFontSmall;

private int iformScrollTopItem = 0;
private int iformSelectedItem = 0;
private int iformListStartY = -1;			// -1: auto 

//#ifdef SMALLTEXT
//#elifdef BIGTEXT

//#ifdef DOJA
//#else
	
	//#ifdef SG-Z500
	//#elifdef MO-V980
	//#elifdef O2-XM
	//#elifdef SH-902
	//#elifdef SI-SF65
	//#elifdef NK-6101
    //#elifdef SM-MYV76
	//#elifdef NK-N90
	//#else
		//#ifdef MO-E1000
		//#else
			private int iformFontDefault = FONT_LARGE;
		//#endif
		private int iformFontDefaultSmall = FONT_SMALL;
		private int iformFontDefaultUltraSmall = FONT_CUSTOM1;
		private int iformFontDB = FONT_SMALL;
	//#endif

//#endif

//#ifdef MO-V3xx
//#else
private int iformHeaderTopMargin = 2;
//#endif

private int iformHeaderSeparatorMargin = 8;

private int iformListLineSpacing = 5;
//#ifdef EXTRAWIDE_COLUMNS
//#elifdef SP17_COLUMNS
//#elifdef WIDE_COLUMNS
//#else
private int iformSecondaryColumnWidth = 20;
//#endif
	

//#else
//#endif
		
//#endif

/*
private void iformPaint() {
	if (screenLost) {
		iformDisplayAll();		
	}
	if (animationLost) {
		iformDisplayAnimation();
	}
}
*/
private int iformTickerStepA = 0;
private int iformTickerStepB = 0;
private int iformTickerStepC = 0;

private final static int MASK_PICKED = 1;
private final static int MASK_INJURED = 2;
private final static int MASK_SANCTIONED = 4;
private final static int MASK_DISABLED = 8;

private int lastColor;
private void proxySetColor(int c) {
	lastColor = c;
	//dojaSetColor(c);
	
//#ifdef DOJA25
//#else
	//#ifdef MI-M420i
	//#else
		//#ifdef DOJA
		//#else
			scr.setColor(c);
		//#endif
	//#endif
//#endif
 
}

private void dojaSetColor(int c) {
//#ifdef DOJA25
//#else
	//#ifdef MI-M420i
	//#else
		// SENSEI
		//#ifdef DOJA
		//#else
			scr.setColor(c);
		//#endif
	//#endif
//#endif
}

private int attributedSecColor(int i) {
	if (iformDataListItemsAttr != null) {
		if ((iformDataListItemsAttr[i] & MASK_PICKED) != 0) {
			return COLOR_PICKED_SEC;
		} else if ((iformDataListItemsAttr[i] & MASK_INJURED) != 0) {
			return COLOR_INJURED_SEC;
		} else if ((iformDataListItemsAttr[i] & MASK_SANCTIONED) != 0) {
			return COLOR_SANCTIONED_SEC;
		} else {
			return 0;
		}
	} 
	return 0;
}

private void attributedColor(int i) {
	// atributos del texto
	int c = COLOR_TEXT_FG;
	if (iformDataListItemsAttr != null) {
		if ((iformDataListItemsAttr[i] & MASK_PICKED) != 0) {
			c = COLOR_PICKED_FG;
		} else if ((iformDataListItemsAttr[i] & MASK_INJURED) != 0) {
			c = COLOR_INJURED_FG;
		} else if ((iformDataListItemsAttr[i] & MASK_SANCTIONED) != 0) {
			c = COLOR_SANCTIONED_FG;
		} else if ((iformDataListItemsAttr[i] & MASK_DISABLED) != 0) {
			c = COLOR_DISABLED_FG;
		} else {
			c = COLOR_TEXT_FG;
		}
	}
	proxySetColor(c);
}

private Image altTopBackground;
private void iformDisplayHeaderAnimated() {
	boolean altCalc = pickInMatch ||
			currentForm == FORM_MATCH_POST_ROUND_STATS ||
			currentForm == FORM_MATCH_POST_EUROPE_STATS ||
			currentForm == FORM_MATCH_POST_MATCH_STATS;
	Image tb = null;
	if (altCalc) {
		tb = altTopBackground;
	} else {
		tb = iformTopBackground;
	}

	if (tb != null) {
//#ifdef TILEMENUBACK
//#elifdef SOLIDMENUBACK
//#else
		imageDraw(tb, 0, 0);
//#endif
	}

	//ZNR
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif	
	//#ifndef NOCLUBBAR
	int k = 0;
	if (currentForm != FORM_MATCH_PLAY && tb != null)
		//#ifdef THINFLAG
		//#else
			//#ifdef BUGFLAGS
			//#endif
			for(int i = tb.getHeight(); i < tb.getHeight() + 6; i+=2)
			{
				proxySetColor(flagColors[k]);
				if (flagColors[k++] != 0x888888) scr.fillRect(0, i,canvasWidth, 2);
			}			
		//#endif
	//#endif
	
	proxySetColor(COLOR_TITLETEXT_FG);
	fontSet(iformFontTopHeader);
	int accY = iformHeaderTopMargin;
//#ifndef TICKERTOP
/*	if (iformDataHeaderTop != null) {
		printDraw(iformDataHeaderTop, 0, iformHeaderTopMargin, PRINT_HCENTER|PRINT_QSHADOW);
	}*/

	if (iformDataHeaderTop != null) {
		if (printTextWidth(iformDataHeaderTop) > canvasWidth) {
			// titulo, con animacion
			fontSet(iformFontTopHeader);
			int w = printTextWidth(iformDataHeaderTop);
			// "real"
			printDraw(iformDataHeaderTop, iformTickerStepC*3, accY, 0);//ZNR PRINT_QSHADOW
			// "extra"
			int ex = w + 20 + iformTickerStepC*3;
			if (ex < canvasWidth) {
				printDraw(iformDataHeaderTop, ex, accY, 0);//ZNR PRINT_QSHADOW
			}
			// swap
			if (ex <= 0) {
				iformTickerStepC = 0;
			}
		} else {
			printDraw(iformDataHeaderTop, 0, iformHeaderTopMargin, PRINT_HCENTER);//ZNR |PRINT_QSHADOW
		}
	}


//#endif
	if (iformTopBackground == null) {
		accY += fontHeight(iformFontTopHeader);
	} else {
		if (altCalc) {
			if (altTopBackground != null) {
				accY = altTopBackground.getHeight() - fontHeight(iformFontSmallHeader) - 1;
			} else {
				accY += fontHeight(iformFontTopHeader);
			}
		} else {
			accY = iformTopBackground.getHeight() - fontHeight(iformFontSmallHeader) - 1;
		}
	}

//#ifdef TICKERTOP
//#else
	if (iformDataHeaderSmall != null) {
		String cStr = iformDataHeaderSmall;
		int font = iformFontSmallHeader;
//#endif
		if (printTextWidth(cStr) > canvasWidth) {
			// subtitulo, con animacion
			fontSet(font);
			int w = printTextWidth(cStr);
			// "real"

			//#ifdef SG-E720
			//#else
				printDraw(cStr, iformTickerStepA*3, accY, 0); // PRINT_SHADOW
			//#endif
			
			// "extra"
			int ex = w + 20 + iformTickerStepA*3;
			if (ex < canvasWidth) {
				//#ifdef SG-E720
					printDraw(cStr, ex, accY, PRINT_MASK); //ZNR PRINT_SHADOW|
				//#else
					printDraw(cStr, ex, accY, 0);//PRINT_SHADOW
				//#endif
			}
			// swap
			if (ex <= 0) {
				iformTickerStepA = 0;
			}
		} else {
			fontSet(font);
			printDraw(cStr, 0, accY, PRINT_HCENTER); // ZNR PRINT_SHADOW
		}

	}
}

//private int ckk = 0;
private void iformDisplayAnimation() {
	if (iformModalPopup || iformWaitDisplay)
		return;
	if (currentForm == FORM_MATCH_PLAY)// || currentForm == FORM_MATCH_POST)
		return;
	
	//ckk += 30;
	//rectFill(ckk, 0, 0, 20, 20);
	// setup
	printCreate();
	
	if (currentForm != FORM_MATCH_POST)
		iformDisplayHeaderAnimated();
	
	// ticker de focus
	//System.out.println("iformFocusedText: " + iformFocusedText + " iformFocusDesired: " + iformFocusDesired);

	//#ifdef IMENUBAR
	//#endif
	
	
	if (iformFocusedText != null && iformFocusDesired) {
		fontSet(iformFont);
		String[] subdiv = null;
		if (iformDataListColumnNames != null) {
			subdiv = new String[iformDataListColumnNames.length];
			if (iformDataListItems[0][iformFocusItem].indexOf('|') == 0) {
				int lasti = 0;
				for (int k = 0; k < subdiv.length; k++) {
					int nexti = iformDataListItems[0][iformFocusItem].indexOf('|', lasti + 1);
					subdiv[k] = iformDataListItems[0][iformFocusItem].substring(lasti+1, nexti);
					lasti = nexti;
				}
			} else {
				for (int k = 0; k < subdiv.length; k++) {
					if (iformDataListItems[k] != null) {
						subdiv[k] = iformDataListItems[k][iformFocusItem];
					} else {
						subdiv[k] = "";
					}
				}
			}
		} else {
			subdiv = new String[1];
			subdiv[0] = iformFocusedText;
		}
		String t = subdiv[0];
		
		
		
		
//#ifdef DBCUSTOMFONT
//#endif
		int ptwt = printTextWidth(t);
		//System.out.println("" + ptwt + " > " + (iformFocusAvailWidth - 3));
		if (ptwt > (iformFocusAvailWidth - 3)) {
			// pintada 1 del fondo del focus: todo el ancho
			rectFill(COLOR_BOX_FG, 0, iformFocusY, canvasWidth, fontHeight(iformFont)+1);
			// item animado, con todo el overflow que haga falta
			int w = printTextWidth(t);
			attributedColor(iformFocusItem);
			printDraw(t, iformTickerStepB*3, iformFocusY, PRINT_SHADOW);
			// "extra"
			int ex = w + 20 + iformTickerStepB*3;
			if (ex < canvasWidth) {
				printDraw(t, ex, iformFocusY, PRINT_SHADOW);
			}
			// swap
			if (ex <= 0) {
				iformTickerStepB = 0;
			}
			// pintada 2 del fondo del focus: desde el final del item tickeado
			rectFill(COLOR_BOX_FG, iformFocusAvailWidth, iformFocusY,
				canvasWidth - iformFocusAvailWidth, fontHeight(iformFont)+1);
			attributedColor(iformFocusItem);
			// resto de columnas, asumimos que no hay ticker. si llega a ser necesario MAS tickers
			// entones hay que generalizarlo todo
			if (iformDataListColumnNames != null) {
				// columna a columna
				for (int j = 1; j < iformDataListColumnNames.length; j++) {
					int x = 0;
					if (j > 0) {
						// -5 --> ancho de la scrollbar
						x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 5;						
						//#ifdef FIXLISTM1
						x--;
						//#endif 
					}
					
					
					t = subdiv[j];
					//#ifndef DOJA
					int px = x;
					//#endif
					if (t.length() == 1) 
					{
						int size;
						//#ifdef J2ME
						//JUSTIFICAR DERECHA
						//size = (printFont.charWidth('9')*2) - printFont.charWidth(t.charAt(0));
						//CENTRAR
						size = (printFont.charWidth('9')/2);
						//#endif
						//#ifdef DOJA
						//#endif
						x = x + size;
					}
					
					
					
					
					
					//printDraw(iformDataListItems[j][iformFocusItem], x, iformFocusY, PRINT_SHADOW);
					printDraw(subdiv[j], x, iformFocusY, PRINT_SHADOW);
					
					
//					ZNR
					//#ifndef DOJA
					//eliminado por jamdat
					/*if (currentForm == FORM_TRAINING_CHANGE_SCHEDULE &&
							arrow != null && j == 2) 
					{
							int wi = arrow.getWidth()/3;
							int hi = arrow.getHeight();
							imageDraw(arrow, wi, 0, wi, hi, px-wi-1-iformSecondaryColumnWidth, iformFocusY);
							imageDraw(arrow, wi*2, 0, wi, hi, px-1, iformFocusY);
					}*/
					//#endif
					//ZNR END
					
					
				}
			}
			// reparar danyos extras
			displayScrollbar();
			displaySeparators();
			//#ifdef FULLSCREEN
			displayFlechita();
			//#endif
		}
	}
}


// se saca esto fuera pada poder pintar la scrollbar dentro de la anim
private int totalHeight;
private int availHeight;
private int sbY;
private void displayScrollbar() {
	
	// VERSI�N SENSEI: Hasta las narices de parchear!!! Vamos a hacerlo bien de una vez...
	
	if(totalHeight > availHeight) {
		
		int startY = sbY;
		int sizeY = availHeight;
		/*
		//#ifdef FULLSCREEN
		int sizeY = availHeight;canvasHeight - startY - (iformDataSoftLeft != null || iformDataSoftRight != null ? fontHeight(iformFontDefault) + 2 :
			//#ifdef SMALLCANVAS
			SOFTKEYARROWSHEIGHT
			//#else
			SOFTKEYARROWSHEIGHT + 4
			//#endif
			);
		//#else
		int sizeY = canvasHeight - startY;
		//#endif		 
		 */
		
		int itemsThatFit = availHeight / ((fontHeight(iformFont) + iformListLineSpacing));
		int barSizeY = ((sizeY) * itemsThatFit) / iformDataListItems[0].length; 
		
		//int barSizeY = ((sizeY) * availHeight ) / totalHeight;
		int barPosY = ((sizeY) * iformScrollTopItem) / iformDataListItems[0].length;
		
		if(barPosY + barSizeY + 1 > sizeY) barSizeY = sizeY - barPosY - 1;
		
		//#ifdef DebugScrollbar
		System.out.println("###Scrollbar Info:"+sbY);
		System.out.println("Bar start:"+sbY);
		System.out.println("availHeight:"+availHeight+", totalHeight of data"+totalHeight);
		System.out.println("iformScrollTopItem:"+iformScrollTopItem+", length of data"+iformDataListItems[0].length);
		//#endif
		
		//#ifdef J2ME
		scr.setClip(canvasWidth - 4, startY, 4, sizeY);
		//#endif
		rectFill(COLOR_SCROLLBAR_FG, canvasWidth - 4, startY, 4, sizeY);
		rectFill(COLOR_SCROLLBAR_BG, canvasWidth - 3, startY + 1, 2, sizeY - 2);
		rectFill(COLOR_SCROLLBAR_FG, canvasWidth - 3, startY + 1 + barPosY, 2, barSizeY);
	}
	
	
	//rectFill(COLOR_SCROLLBAR_BG, canvasWidth - 4, sbY - 1, 4, canvasHeight - SOFTKEYARROWSHEIGHT - (sbY - 1));
	
	//CCM
	// pintar scrollbar si hace falta
	/*if (availHeight < totalHeight) {
		int sbHeight = (((availHeight * availHeight) << 12) / totalHeight) >> 12;
		int slHeightP = ((((availHeight * (fontHeight(iformFont) + iformListLineSpacing)) << 12)
			/ totalHeight) * iformScrollTopItem) >> 12;
		//rectFill(COLOR_SCROLLBAR_BG, canvasWidth - 4, sbY - 1, 4, availHeight + 2);
		rectFill(COLOR_SCROLLBAR_BG, canvasWidth - 4, sbY - 1, 4, canvasHeight - SOFTKEYARROWSHEIGHT - (sbY - 1));
		rectFill(COLOR_SCROLLBAR_FG, canvasWidth - 3, sbY + slHeightP,
			2, sbHeight + 3 - SOFTKEYARROWSHEIGHT);
	}*/
		
}

//boolean canAccept = true;

//#ifndef DOJA
//#ifdef FULLSCREEN
void displayFlechita() {
	
	if (iformDataSoftLeft == null && iformDataSoftRight == null && arrow != null) {
		int wi = arrow.getWidth()/3;
		int hi = arrow.getHeight();
		//imageDraw(arrow, 4/*canvasWidth - 4 - arrow.getWidth()*/, canvasHeight - 2 - arrow.getHeight());
		
		if (!iformModalPopup && currentForm != FORM_WELCOME && currentForm != FORM_SEASON_GOAL_INFO
				&& currentForm != FORM_GAME_OVER && currentForm != FORM_SPLASH_LEGAL		
				)
		{
			if (requestMenuBar)		
				imageDraw(arrow, 0, 0, wi, hi, 4, canvasHeight - ARROWBOTDISTANCE - hi);					
			else		
				imageDraw(arrow, wi, 0, wi, hi, 4, canvasHeight - ARROWBOTDISTANCE - hi);
		}
		//if (canAccept)
		switch(currentForm)
		{
			case FORM_ABOUT:
			case FORM_HELP_GENERIC:
			case FORM_CREDITS:
			case FORM_CLASS_CHAMPIONS:
			case FORM_TRAINING_CHANGE_SCHEDULE:
			case FORM_MATCH_POST_MATCH_STATS:
			case FORM_MATCH_POST_ROUND_STATS:
			case FORM_MATCH_POST_EUROPE_STATS:
			case FORM_CLASSIFICATION:
			case FORM_CLASSIFICATION_EXTRA:
			case FORM_TOP_PLAYERS:	
			case FORM_FINANCES_GENERAL:
				break;
			default:
				imageDraw(arrow, wi*2, 0, wi, hi,canvasWidth - 4 - wi, canvasHeight - ARROWBOTDISTANCE - hi);
		}
	}
	/*if (iformDataSoftLeft == null && iformDataSoftRight == null) {
		//iformDataSoftRight = loc[92];
		listenerInit(null, loc[92]);
	}*/
}
//#endif
//#endif


// lo mismo con los separadores de columnas
private int colHeadBot;
private void displaySeparators() {
	if (iformDataListColumnNames != null) {
		for (int j = 0; j < iformDataListColumnNames.length; j++) {
			if (j > 0) {
				// -5 --> ancho de la scrollbar
				// -2 --> ajuste de pixels para no superponer el texto y el separador
				int x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 5 - 2;
				rectFill(COLOR_SCROLLBAR_FG, x, colHeadBot, 1, canvasHeight - colHeadBot - 1 - SOFTKEYARROWSHEIGHT);
			}
		}
	}
}

// ZNR 
private void iformDisplayAll() {

	//#ifdef Debug
	Debug.println("iDisplayAll()");
	//#endif
	
	if (initLoad) {
		fontSet(FONT_SMALL);
		rectFill(COLOR_WHITE, 0, 0, canvasWidth, canvasHeight);
		printCreate();
		proxySetColor(0);
	//#ifndef ALWAYS_WIN
		//#ifdef WEBSUBCRIPTION
			//printLoading();
		//#else
			printDraw(loadingStr, 0, 0, PRINT_HCENTER|PRINT_VCENTER);
		//#endif
	//#else
	//#endif
	}
	
	
	// setup
	printCreate();
	iformFocusedText = null;
	iformFocusY = 0;
	iformFocusAvailWidth = canvasWidth;
	// fondo
	/*if (iformTopBackground != null) {
		// hackitithack
		if (!pickInMatch) {
			imageDraw(iformTopBackground, 0, 0);
		}
	}*/
	
	if (iformBackground != null && iformTopBackground != null) {
		// tilear background
		
		//#ifdef SOLIDBACK
		//#else
		//#ifdef TILEDBACKGROUND
		//#else					
		rectFill(COLOR_BACK, 0, 0, canvasWidth, canvasHeight);
		//#ifndef NOBACKGROUNDIMAGE		
		imageDraw(iformBackground, (canvasWidth - iformBackground.getWidth())/2, iformTopBackground.getHeight() + (canvasHeight - iformBackground.getHeight() - iformTopBackground.getHeight())/2);		
		//#endif
		//#endif
		
		//#endif
	}
	if (iformBackground != null && iformTopBackground == null) {
		// HACK HACK HACK
		//#ifdef VI-TSM6
		//#endif		
		if (iformBehaviour == IFORM_BEHAVIOUR_ANY_KEY) {
			rectFill(COLOR_WHITE, 0, 0, canvasWidth, canvasHeight);
		}		
		imageDraw(iformBackground);
		// minihack
		if (currentForm == FORM_SPLASH3 && iformWaitDisplay) {
			fontSet(iformFontDefaultSmall);
			int w = printTextWidth(gameText[TEXT_LOADING][0]) + 6;
			int h = fontHeight(iformFontDefaultSmall) + 4;
			int x = canvasWidth/2 - w/2;
			int yyy = canvasHeight - h - 3;
			// SENSEIHACK
			yyy = canvasHeight/2 - h/2;
			rectFillA(COLOR_SCROLLBAR_BG, 230, x, yyy, w, h);
			printCreate(x, yyy, w, h);
			proxySetColor(COLOR_TEXT_FG);
			printDraw(gameText[TEXT_LOADING][0], 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
			printCreate();
		}

	}
	//#ifdef NOBACKGROUNDIMAGE
	//#endif
	
	// cabeceras
	iformDisplayHeaderAnimated();
	int accY = 0;
	int y = 0;
	// AHORA CON TOP BACKGROUND SE FUERZA UN ACCY SIEMPRE
	// y ahora no...
	if (iformTopBackground != null) {
		accY = iformTopBackground.getHeight() + iformHeaderSeparatorMargin;
	}
	if (altTopBackground != null && pickInMatch) {
		accY = altTopBackground.getHeight() + iformHeaderSeparatorMargin;
	}
	int headerY = accY;
	// iformListStartY puede forzar otra posicion para empezar el menu/listado
	y = iformListStartY;
	if (y == -1) {
		y = accY;
	}
	// --------------------------------------------
	// items
	if (iformDataListItems == null)
		return;
	// header de las columnas
	colHeadBot = 0;

	if (iformDataListColumnNames != null) {
		fontSet(iformFontSmallHeader);
		rectFill(COLOR_SCROLLBAR_FG, 0, y, canvasWidth, 1);
		y += 2;
		for (int j = 0; j < iformDataListColumnNames.length; j++) {
			int x = 0;
			if (j > 0) {
				// -6 --> ancho de la scrollbar
				x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 6;
				//#ifdef FIXLISTM1
				//#endif
			}
			if (j == 0) {
				printDrawAbbrPoint(iformDataListColumnNames[j], x, y, 0,
					canvasWidth - (iformDataListColumnNames.length-1)*iformSecondaryColumnWidth - 6);
			} else {
				printDraw(iformDataListColumnNames[j], x, y, 0);
			}
		}
		y += fontHeight(iformFontSmallHeader) + 1;
		rectFill(COLOR_SCROLLBAR_FG, 0, y, canvasWidth, 1);
		colHeadBot = y;
		y += 1; //iformHeaderSeparatorMargin;
	}
	fontSet(iformFont);
	// logica del scroll
	int minOpt = iformScrollTopItem;
	int maxOpt = minOpt + ((canvasHeight - y - ARROWBOTDISTANCE - SOFTKEYARROWSHEIGHT) / (fontHeight(iformFont) + iformListLineSpacing)) - 1;
	if (iformSelectedItem < minOpt) {
		iformScrollTopItem = iformSelectedItem;
	} else if (iformSelectedItem > maxOpt) {
		iformScrollTopItem = iformSelectedItem - (maxOpt - minOpt);
	}
	// decidir si hace falta scrollbar, y entonces pintarla o no
	totalHeight = (fontHeight(iformFont) + iformListLineSpacing) * 	
		//#ifdef SCROLLBARSLENGTHHACK
		//#else
		(iformDataListItems[0].length);
		//#endif
	
	availHeight = canvasHeight - y;	
	
	//#ifdef FULLSCREEN
	if (iformDataSoftLeft != null || iformDataSoftRight != null) {
		availHeight -= fontHeight(iformFontDefault) + 2;
		fontSet(iformFont);
		
	} else {
		
		 availHeight -= ARROWBOTDISTANCE + SOFTKEYARROWSHEIGHT;
	}	
	//#endif
	
	availHeight -= availHeight % (fontHeight(iformFont) + iformListLineSpacing);
	
	sbY = y;
	// el pintado en si mismo esta despues de los textos, para poder pintar encima
	// iterar items y pintarlos
	proxySetColor(COLOR_TEXT_FG);
	if (iformDataListColumnNames != null) {
		iformFocusAvailWidth = canvasWidth - (iformDataListColumnNames.length-1)*iformSecondaryColumnWidth - 10;
	} else {
		iformFocusAvailWidth = canvasWidth;
	}
	int topi = iformScrollTopItem;
	if (topi < 0) {
		topi = 0;
	}
	
	int oriY = y;
	
	for (int i = topi; i < iformDataListItems[0].length; i++) {
		// saltarse items que se salen
		if ((y + fontHeight(iformFont)) >= oriY + availHeight/*canvasHeight - SOFTKEYARROWSHEIGHT*/) {
			break;
		}
		// dibujar una caja en el seleccionado
		if (iformSelectedItem == i && iformFocusDesired) {
			iformFocusY = y;
			iformFocusedText = iformDataListItems[0][i];
			iformFocusItem = i;
			rectFillX(
					COLOR_BOX_FG,
					0, y, canvasWidth, fontHeight(iformFont)+1);
			proxySetColor(COLOR_TEXT_FG);
		}
		else if (prevOption == i && iformFocusDesired)
		{
			rectFillX(
					COLOR_BOX_PREVIOUS,
					0, y, canvasWidth, fontHeight(iformFont)+1);
			proxySetColor(COLOR_TEXT_FG);
		}
		
		String[] subdiv = null; 
		if (iformDataListItems[0][i].indexOf('|') == 0) {
			subdiv = new String[iformDataListColumnNames.length];
			int lasti = 0;
			for (int k = 0; k < subdiv.length; k++) {
				int nexti = iformDataListItems[0][i].indexOf('|', lasti + 1);
				subdiv[k] = iformDataListItems[0][i].substring(lasti+1, nexti);			
				lasti = nexti;
			}
		}
		if (iformDataListColumnNames != null) {
			// columna a columna
			for (int j = 0; j < iformDataListColumnNames.length; j++) {
				int x = 0;
				if (j > 0) {
					// -5 --> ancho de la scrollbar
					x = canvasWidth - (iformDataListColumnNames.length - j)*iformSecondaryColumnWidth - 5;
					//#ifdef FIXLISTM1
					x--;
					//#endif
				}
				// atributos del texto
				attributedColor(i);
				// HACK HACK HACK HACK!!!!!
				// pero da igual, la unica columna abreviable es la primera, que ya tiene
				// el avail calculado para el ticker
				String t = "";
				if (subdiv != null) {
					t = subdiv[j];
				} else {
					t = iformDataListItems[j][i];
				}
				//ZNR
				
				//#ifndef DOJA
				int px = x;
				//#endif
				if (t.length() == 1) 
				{
					int size;
					//#ifdef J2ME
					//size = (printFont.charWidth('9')*2) - printFont.charWidth(t.charAt(0));
					size = (printFont.charWidth('9')/2);
					//#endif
					//#ifdef DOJA
					//#endif
					x = x + size;
				}
					//t = spaceString +spaceString + t;
//#ifdef DBCUSTOMFONT
//#endif
				if (iformSelectedItem == i && iformFocusDesired) {
					printDrawAbbr(t, x, y, PRINT_SHADOW, iformFocusAvailWidth);
					//ZNR
					//#ifndef DOJA
					/*if (currentForm == FORM_TRAINING_CHANGE_SCHEDULE &&
							arrow != null && j == 2) 
					{
							int wi = arrow.getWidth()/3;
							int hi = arrow.getHeight();
							imageDraw(arrow, wi, 0, wi, hi, px-iformSecondaryColumnWidth-wi-1, y);
							imageDraw(arrow, wi*2, 0, wi, hi, px-1, y);
					}*/
					//#endif
					//ZNR END
				} else {
					printDrawAbbr(t, x, y, 0, iformFocusAvailWidth);
				}		
			}
		} else {
			// atributos del texto
			attributedColor(i);
			// 1 columna
			int it = i;
			if (it < 0) {
				it = 0;
			}
			if (it < iformDataListItems[0].length) {
				if (iformSelectedItem == it && iformFocusDesired) {
					printDrawAbbr(iformDataListItems[0][it], 1, y, PRINT_LEFT | PRINT_SHADOW, canvasWidth);
				} else {
					printDrawAbbr(iformDataListItems[0][it], 1, y, PRINT_LEFT, canvasWidth);
				}
			}
		}
		y += fontHeight(iformFont) + iformListLineSpacing;
	}
	// pintar scrollbar si hace falta
	displayScrollbar();
	
	// divisores verticales de columnas
	displaySeparators();
	// graficos custom de la pantalla
	switch (currentForm) {
		case FORM_TACTICS_FORMATION:
		case FORM_TACTICS_POSITION:
			customDrawTacticsFormation(headerY);
		break;
		case FORM_MATCH_PLAY:
		case FORM_MATCH_POST:
			customDrawMatchPlay();
		break;
	}

	// popup informativo
	if (iformModalPopup) {
		//#ifdef SMALLCANVAS
		//#elifdef MEDIUMCANVAS
		//#else
		int x = 10;
		y = x;
		int w = canvasWidth - x*2;
		int h = canvasHeight - y*2 - fontHeight(iformFontDefault) - 2;
		printCreate(x + 5, y + 5, w - 10, h - 10);
		int corr = 5;
		//#endif
		fontSet(iformFontDefaultSmall);
		modalPopupRect(iformPopupColor, 230, x, y, w, h);
		String[] s = printTextBreak(iformPopupText, w - 10);
		int il = fontHeight(iformFontDefaultSmall) + iformListLineSpacing;
		y = (h - s.length*il) / 2;
		//#ifdef FULLPOPUP
		//#endif
			proxySetColor(COLOR_TEXT_FG);
			for (int i = 0; i < s.length; i++) {
				printDraw(s[i], 0, y - corr, PRINT_HCENTER|PRINT_SHADOW);
				y += il;
			}
		//#ifdef FULLPOPUP
		//#endif
		printCreate();
	}
	// please wait, yer phone thingie si teh suxor
	if (iformWaitDisplay) {
		fontSet(iformFontDefaultSmall);
		int w = printTextWidth(gameText[TEXT_LOADING][0]) + 6;
		int h = fontHeight(iformFontDefaultSmall) + 4;
		int x = canvasWidth/2 - w/2;
		y = canvasHeight/2 - h/2;
		rectFillA(COLOR_SCROLLBAR_BG, 230, x, y, w, h);
		printCreate(x, y, w, h);
		proxySetColor(COLOR_TEXT_FG);
		//#ifdef WAIT_ON_QUIT
		//#else
		printDraw(gameText[TEXT_LOADING][0], 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
		//#endif
		printCreate();
	}
	// softbuttons
	//#ifdef FULLSCREEN
	displayFlechita();
	if (iformDataSoftLeft != null || iformDataSoftRight != null) {
		if (iformPaintSoftBack) {
			rectFill(COLOR_SCROLLBAR_BG, 0, canvasHeight - fontHeight(iformFontDefault) - 2,
				canvasWidth, fontHeight(iformFontDefault) + 2);
		}
	}
	if (iformDataSoftLeft != null) {
		/*if (iformPaintSoftBack) {
			rectFillA(COLOR_SCROLLBAR_BG, 128, 0, canvasHeight - fontHeight(iformFontDefault) - 2,
				canvasWidth/2 - 3, fontHeight(iformFontDefault) + 2);
		}*/
		fontSet(iformFontDefault);
		printCreate(0, canvasHeight - fontHeight(iformFontDefault),
			canvasWidth/2 - 3, fontHeight(iformFontDefault));
		proxySetColor(COLOR_TEXT_FG);
		printDraw(iformDataSoftLeft, 0, 0, PRINT_HCENTER|PRINT_QSHADOW);
	}
	if (iformDataSoftRight != null) {
		/*if (iformPaintSoftBack) {
			rectFillA(COLOR_SCROLLBAR_BG, 128, canvasWidth - canvasWidth/2 + 3,
				canvasHeight - fontHeight(iformFontDefault)- 2,
				canvasWidth/2 - 3, fontHeight(iformFontDefault) + 2);
		}*/
		fontSet(iformFontDefault);
		printCreate(canvasWidth - canvasWidth/2 + 3,
			canvasHeight - fontHeight(iformFontDefault),
			canvasWidth/2 - 3, fontHeight(iformFontDefault));
		proxySetColor(COLOR_TEXT_FG);
		printDraw(iformDataSoftRight, 0, 0, PRINT_HCENTER|PRINT_QSHADOW);
	}
	//#else
	//#endif
}

private void customDrawTacticsFormation(int headerY) {
	int topX = (canvasWidth - fieldImage.getWidth())/2;
	int topY = headerY;
	//printCreate(topX, topY, fieldImage.getWidth(), fieldImage.getHeight());
	if (fieldImage != null) {
		imageDraw(fieldImage, topX, topY);
	}
	
	//#ifdef FORCECUSTOMFONTINFORMATION
	//#elifdef FORCESMALLFONTINFORMATION
	//#else
	fontSet(iformFontDefaultUltraSmall);
	//#endif
	
	proxySetColor(0xffffff);
	for (int i = 1; i < 12; i++) {
		int x = league.formationsXY[focusedFormation][(i-1)*2];

		//#ifdef AJUSTA_FORMACION2
		//#elifdef AJUSTA_FORMACION
		//#else
			int y = league.formationsXY[focusedFormation][(i-1)*2+1];
		//#endif

		//#ifdef SMALLCANVAS
		//#endif
		
		//#ifdef DOUBLESIZEFORMATION
		//#endif
		
		String s = "" + i;
		Image b = blackBullet;
		int c = 0xffffff;
		if (iformDataListItemsAttr != null) {
			if ((iformDataListItemsAttr[i-1] & MASK_INJURED) != 0) {
				b = redBullet;
				c = 0xff0000;
			}
			if ((iformDataListItemsAttr[i-1] & MASK_SANCTIONED) != 0) {
				b = redBullet;
				c = 0xff0000;
			}
		}	
		if (focusedPlayer == i-1) {
			b = blueBullet;
			c = 0xff;
		}
		
		printCreate(topX+x+1, topY+y-1, b.getWidth(), b.getHeight());
		
		//#ifndef DOJA
		//#ifndef NOFORMATIONBUTTONS
		imageDraw(b, topX+x, topY+y);
		//#endif
		//#endif
		
		//#ifdef DOJA
		//#endif
		
		//#ifdef NOFORMATIONBUTTONS
		//#endif
		
		//#ifndef SMALLCANVAS
		//#endif
	}
	if (focusedPlayer < 11)
		return;
	String s = gameText[6][0];
	if (focusedPlayer >= 16 && focusedPlayer < 30) {
		s = gameText[7][0];
	}
	printCreate(topX, topY, fieldImage.getWidth(), fieldImage.getHeight());
	//#ifdef FORCECUSTOMFONTINFORMATION
	//#else
	fontSet(iformFontDefaultUltraSmall);
	//#endif
	int w = printTextWidth(s);
	int h = fontHeight(iformFontDefaultUltraSmall);
	int x = (fieldImage.getWidth() - w) / 2;
	int y = (fieldImage.getHeight() - h) / 2;
	rectFill(0, topX+x-2, topY+y, w+3, h);
	proxySetColor(0xffffff);
	printDraw(s, x, y, 0);
}

//#ifdef MARCADOR96
//#elifdef MARCADOR101
//#elifdef MARCADOR116
//#elifdef MARCADOR120
//#elifdef MARCADOR128
//#elifdef MARCADOR130
//#elifdef MARCADOR132
//#elifdef MARCADOR162
//#elifdef MARCADOR176
private int dmpScoreFont = iformFontDefaultSmall;
private int dmpTimerFont = iformFontDefaultSmall;
private int[] dmpLeftTeamRect = { 4, 3, 67-3, 13 };
private int[] dmpRightTeamRect = { 105+3, 3, 67, 13 };
private int[] dmpLeftScoreRect = { 76, 2, 10, 12 };
private int[] dmpRightScoreRect = { 90, 2, 10, 12 };
private int[] dmpTimerRect = { 68, 25, 40, 12 };
//#elifdef MARCADOR208
//#elifdef MARCADOR240
//#elifdef MARCADOR352
//#endif

private void customDrawMatchPlay() {
	// rectangulo de eventos calculado
	int evx = 3;
	int evy = 0;
	int evw = canvasWidth - 7;
	int evh = canvasHeight - fontHeight(iformFontDefault) - 3;
	printCreate(evx, evy, evw, evh);
	// eventos
	fontSet(iformFontDB);
	proxySetColor(0xffffff);
	String[] s = printTextBreakInv(mpEventsText, evw);
	int y = evh - fontHeight(iformFontDB);
	for (int i = 0; i < s.length; i++, y -= fontHeight(iformFontDB)) {
		if (s[i] != null)
			printDraw(s[i], 0, y, 0);
	}
	// poner fondo de arriba para clipear el texto
//#ifndef COMPONERMARCADOR
	imageDraw(iformTopBackground, 0, 0);
//#else
//#endif
	// nombres equipos
	fontSet(iformFontDB);
	proxySetColor(0xffffff);
	printCreateA(dmpLeftTeamRect);
	printDrawAbbrFullWidthR(league.journeyMatch[0].name);
	printCreateA(dmpRightTeamRect);
	printDrawAbbrFullWidth(league.journeyMatch[1].name);
	
	//ZNR DRAW BANDERAS
	//#ifndef NOFLAGS
	if (currentForm  != FORM_MATCH_POST)
	{
		drawFlag(dmpLeftTeamRect, 0);
		drawFlag(dmpRightTeamRect, 1);
	}
	//#endif
	//ZNR END
	
	// puntuacion actual
	//scr.setColor(0);
	fontSet(dmpScoreFont);
	printCreateA(dmpLeftScoreRect);
	//GOLES DURANTE PARTIDO
	printDraw("" + mpScoreLeft, 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
	printCreateA(dmpRightScoreRect);
	printDraw("" + mpScoreRight, 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
//#ifdef NO_PAINT_TIMER
//#else
	//#ifdef COMPONERMARCADOR_OP_TIMER
	//#else
	// timer
	fontSet(dmpTimerFont);
	printCreateA(dmpTimerRect);
	printDraw(mpTimeText, 0, 0, PRINT_HCENTER|PRINT_VCENTER|PRINT_QSHADOW);
	//#endif
//#endif
}


//#ifndef NOFLAGS
private void drawFlag(int[] dim, int eq)
{
	Team t = league.journeyMatch[eq];
	//#ifdef J2ME
	scr.setClip(0,0,canvasWidth,canvasHeight);
	//#endif
	int []c = new int[3];
	c[0] = c[2] = t.color1;
	c[1] = t.color2;
	int k = 0;
	int x = 2;
	if (eq == 1) x = canvasWidth-(dim[2]/2)-2;
	for(int i = dim[1]+dim[1]+dim[3]+2; k < 3; i += dim[3]/3, k++ )
	{
		proxySetColor(c[k]);
		scr.fillRect(x, i,dim[2]/2, dim[3]/3);		
	}
	proxySetColor(0xffffff);
}


//#endif

private final static int HIEVENT_IDLE = 1;
private final static int HIEVENT_ANY = 2;
private final static int HIEVENT_YES = 3;
private final static int HIEVENT_NO = 4;
private final static int HIEVENT_FOCUS_N = 100;
private final static int HIEVENT_SELECTED_N = 200;

private int hiEventFocusedItem(int hiEvent) {
	if (hiEvent >= HIEVENT_FOCUS_N && hiEvent < HIEVENT_FOCUS_N + 100) {
		return hiEvent  - HIEVENT_FOCUS_N;
	}
	return -1;
}

private int hiEventSelectedItem(int hiEvent) {
	if (hiEvent >= HIEVENT_SELECTED_N && hiEvent < HIEVENT_SELECTED_N + 100) {
		return hiEvent  - HIEVENT_SELECTED_N;
	}
	return -1;
}

private long nextAnimFrame = -1;

//ZNR
private int formTick() {
//#ifndef VI-TSM100
	// update animaciones
	// tick pasivo, se detectara en el siguiente tick de juego
	long t = System.currentTimeMillis();
	if (t > nextAnimFrame) {
		nextAnimFrame = t + 150;
		animationLost = true;
		iformTickerStepA--;
		iformTickerStepB--;
		iformTickerStepC--;
	}
//#else
//#endif

	// gestion eventos
	int r = HIEVENT_IDLE;

/*
	if (iformModalPopup) {
		if (lastKeyCode != 0 || lastKeyMenu!=0) {
			iformModalPopup = false;
			// forzar pintado desde aqui mismo
			screenLost = true;
			r = HIEVENT_YES;
		} else {
			r = HIEVENT_IDLE;
		}
		// eliminar repeticion
		lastKeyMenu=0; lastKeyX=0; lastKeyY=0; lastKeyMisc=0; lastKeyCode=0;
		return r;
	}
*/

	if (iformModalPopup) {

		if (lastKeyY == 1) {// || lastKeyMenu == -1) { // abajo
			iformPopupScrollPos++;
			screenLost = true;
		} else if (lastKeyY == -1) { // arriba
			iformPopupScrollPos--;
			screenLost = true;
		} else if (lastKeyMenu > 0) {// || lastKeyX != 0) {
			// pequenyo hack de usabilidad: izquierda selecciona la ultima opcion de manera automatica
			iformModalPopup = false;			
			if (currentForm == FORM_MAIN_MENU)
				requestMenuBar = true;
			// forzar pintado desde aqui mismo
			screenLost = true;
			r = HIEVENT_YES;
		}
		// eliminar repeticion
		lastKeyMenu=0; lastKeyX=0; lastKeyY=0; lastKeyMisc=0; lastKeyCode=0;
		return r;
	}

	
	
	////////////////////////////////////////////////////////////////////////
	// ZNR
	
	if (showMenuBar) 
	{
			
		if (lastKeyX > 0) 
		{
				if (cursorY == 0) {
					
					int i = option;
					i++;
					if (i < MAIN_MENU_ICONS.length) {
						option = i;
					} else {
						option = 0;
					}
					screenLost = true;
					iformTickerStepB = 0;
					suboption = 0;
				}
				else
				{
					suboption = (suboption + 1) % SUBMENU_ICONS[option].length;
					screenLost = true;
					iformTickerStepB = 0;
				}
		} else if (lastKeyX < 0) {
				if (cursorY == 0) {
					int i = option;//iformSelectedItem;
					i--;
					if (i >= 0) {
						option = i;
					} else {
						option = MAIN_MENU_ICONS.length - 1;
					}	
					screenLost = true;
					iformTickerStepB = 0;
					suboption = 0;
				}
				else
				{
					suboption = (SUBMENU_ICONS[option].length + suboption - 1) % SUBMENU_ICONS[option].length;
					screenLost = true;
					iformTickerStepB = 0;
				}				
		}
		else if (lastKeyY != 0)  {
			cursorY = 1-cursorY;
			screenLost = true;
			iformTickerStepB = 0;		
		}
	}
	else
	{
		leftRight = 0;
		//System.out.println("formtick !showmenubar");
		switch (iformBehaviour) {
		case IFORM_BEHAVIOUR_SINGLE_SELECT_LIST:
			//System.out.println("IFORM_BEHAVIOUR_SINGLE_SELECT_LIST");
			// ZNR - end	
			///////////////////////////////////////////////////////////////////////////
		 	/*if (lastKeyMisc == 1 || lastKeyMisc == 3) { // arriba 5
				int i = iformSelectedItem;
				i-=5;
				if (i >= 0) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
				} else {
					r = HIEVENT_FOCUS_N + 0;
					iformSelectedItem = 0;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
					//r = HIEVENT_IDLE;
				}
			} else if (lastKeyMisc == 7 || lastKeyMisc == 9) { // abajo 5
				int i = iformSelectedItem;
				i+=5;
				if (i < iformDataListItems[0].length) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
				} else {
					r = HIEVENT_FOCUS_N + iformDataListItems[0].length - 1;
					iformSelectedItem = iformDataListItems[0].length - 1;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
					//r = HIEVENT_IDLE;
				}
			} else */if (lastKeyY == 1) { // abajo
				int i = iformSelectedItem;
				i++;
				if (i < iformDataListItems[0].length) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
				} else {
					r = HIEVENT_FOCUS_N + 0;
					iformSelectedItem = 0;
					// forzar pintado desde aqui mismo
					screenLost = true;
					iformTickerStepB = 0;
					//r = HIEVENT_IDLE;
				}
			} else if (lastKeyY == -1) { // arriba
				int i = iformSelectedItem;
				i--;
				if (i >= 0) {
					r = HIEVENT_FOCUS_N + i;
					iformSelectedItem = i;					
				} else {
					r = HIEVENT_FOCUS_N + iformDataListItems[0].length - 1;
					iformSelectedItem = iformDataListItems[0].length - 1;										
				}
				screenLost = true;
				iformTickerStepB = 0;
			} else if (lastKeyMenu == 1 || lastKeyMenu == 2 ) { // izq, fire, derecha: comprobar que funciona bien
				r = HIEVENT_SELECTED_N + iformSelectedItem;
			} else if (lastKeyMenu == -1) 
					//|| lastKeyX == -1) 
			{
				// pequenyo hack de usabilidad: izquierda selecciona la ultima opcion de manera automatica
				if (requestMenuBar)
				{
					showMenuBar = !showMenuBar;
					screenLost = true;
				}
				else					
				{					
					r = HIEVENT_SELECTED_N + iformDataListItems[0].length;// - 1;
				}
			}
			else if (currentForm == FORM_TRAINING_CHANGE_SCHEDULE && lastKeyX != 0)
		 	{
		 		r = HIEVENT_SELECTED_N + iformSelectedItem;
		 		leftRight = lastKeyX;
		 	}
		 	
		break;
		case IFORM_BEHAVIOUR_SCROLL_TEXT:
			//System.out.println("IFORM_BEHAVIOUR_SCROLL_TEXT");
			r = HIEVENT_IDLE;
			if (lastKeyY == 1) { // abajo
				// avanzar 20%
				//iformScrollTopItem++;
				if(totalHeight > availHeight)
				if (iformScrollTopItem < iformDataListItems[0].length - (availHeight / (fontHeight(iformFont) + iformListLineSpacing))) {
					//iformScrollTopItem = iformDataListItems[0].length-2;
					iformScrollTopItem++;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
			} else if (lastKeyY == -1) { // arriba
				// retroceder 20%
				if(totalHeight > availHeight)
					iformScrollTopItem--;
				if (iformScrollTopItem < 0) {
					iformScrollTopItem = 0;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
								
			} else if (lastKeyMenu > 0){// || lastKeyX == -1) {
				// pequenyo hack de usabilidad: izquierda selecciona la ultima opcion de manera automatica
				r = HIEVENT_ANY;
			}
			else  if (lastKeyMenu < 0)
			{
				if (requestMenuBar)
				{
					showMenuBar = !showMenuBar;
					screenLost = true;
				}
			}
			break;
		case IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO:
			r = HIEVENT_IDLE;
			if (lastKeyY == 1) { // abajo
				// avanzar 20%
				if (iformScrollTopItem < iformDataListItems[0].length - (availHeight / (fontHeight(iformFont) + iformListLineSpacing))) {
					//iformScrollTopItem = iformDataListItems[0].length-2;
					iformScrollTopItem++;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
			} else if (lastKeyY == -1) { // arriba
				// retroceder 20%
				if(totalHeight > availHeight)
					iformScrollTopItem--;
				if (iformScrollTopItem < 0) {
					iformScrollTopItem = 0;
				}
				iformSelectedItem = iformScrollTopItem;
				screenLost = true;
				//ZNR QUITAME SI QUE QUEJAN LOS TESTERS			
			} else if (lastKeyMenu == -1){// || lastKeyX == -1) {
				r = HIEVENT_NO;
			} else if (lastKeyMenu == 1){ //|| lastKeyX == 1) {
				r = HIEVENT_YES;
			}			
		break;
		case IFORM_BEHAVIOUR_ANY_KEY:
			if (lastKeyCode != 0) {
				r = HIEVENT_ANY;
			} else {
				r = HIEVENT_IDLE;
			}
		break;
		case IFORM_BEHAVIOUR_YES_NO:
			//ZNR QUITAME SI SE QUEJAN LOS TESTERS
			if (lastKeyMenu == 1) {//|| lastKeyX == 1) {
				r = HIEVENT_YES;
			} else if (lastKeyMenu == -1){// || lastKeyX == -1) {
				r = HIEVENT_NO;
			} else {
				r = HIEVENT_IDLE;
			}
		break;
		}
	}
	// eliminar repeticion
	lastKeyMenu=0; lastKeyX=0; lastKeyY=0; lastKeyMisc=0; lastKeyCode=0;
	return r;
}

// alto nivel: un menu simple con columnas y atributos
private void formPlainMenu(String title, String subtitle, String[][] items, String[] labels, byte[] attr) {
	// reset fuentes
	iformFontTopHeader = iformFontDefault;
	iformFontSmallHeader = iformFontDefaultSmall;
	iformFont = iformFontDefault;
	iformFontSmall = iformFontDefaultSmall;
	// reset posiciones
	iformScrollTopItem = 0;
	iformSelectedItem = 0;
	iformListStartY = -1;
	iformFocusDesired = true;
	// fondo
	//iformBackground = defaultBackground;
	//iformTopBackground = defaultTopBackground;
	// cabeceras
	iformDataHeaderTop = title;
	iformDataHeaderSmall = subtitle;
	// items
	iformDataListItems = items;
	iformDataListColumnNames = labels;
	iformDataListItemsAttr = attr;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	// comportamiento interactivo
	iformBehaviour = IFORM_BEHAVIOUR_SINGLE_SELECT_LIST;
	// forzar un paint
	screenLost = true;
}

// alto nivel: un menu simple con columnas
private void formPlainMenu(String title, String subtitle, String[][] items, String[] labels) {
	formPlainMenu(title, subtitle, items, labels, null);
}

// alto nivel: un menu simple
private void formPlainMenu(String title, String subtitle, String[] items) {
	formPlainMenu(title, subtitle, null, null);
	String[][] i = new String[1][];
	i[0] = items;
	iformDataListItems = i;
}

// alto nivel: un listado no interactivo (solo "any key")	
private void formPlainList(String title, String subtitle, String[] items) {
	// reset fuentes
	iformFontTopHeader = iformFontDefault;
	iformFontSmallHeader = iformFontDefaultSmall;
	iformFont = iformFontDefault;
	iformFontSmall = iformFontDefaultSmall;
	// reset posiciones
	iformScrollTopItem = 0;
	iformSelectedItem = 0;
	iformListStartY = -1;
	iformFocusDesired = false;
	// fondo
	//iformBackground = defaultBackground;
	//iformTopBackground = defaultTopBackground;
	// cabeceras
	iformDataHeaderTop = title;
	iformDataHeaderSmall = subtitle;
	// items
	String[][] i = new String[1][];
	i[0] = items;
	iformDataListItems = i;
	iformDataListColumnNames = null;
	iformDataListItemsAttr = null;
	iformPaintSoftBack = true;
	/*if (currentForm != FORM_MAIN_MENU)
		iformPaintSoftBack = true;
	else 
		iformPaintSoftBack = false;*/
	//iformDataSoftLeft = null;
	
	// SENSEI
	
	/*if (currentForm != FORM_MAIN_MENU)
		iformDataSoftRight = gameText[TEXT_OK][0];
	else*/
	iformDataSoftRight = null;
	iformDataSoftLeft = null;
	// comportamiento interactivo
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT;
	// forzar un paint
	screenLost = true;
}

// alto nivel: un listado no interactivo (solo "any key")	
private void formPlainListYesNo(String title, String subtitle, String left, String right, String[] items) {
	formPlainList(title, subtitle, items);
	iformDataSoftLeft = left;
	iformDataSoftRight = right;
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO;
}

// alto nivel: listado con pregunta de si, no
private void formYesNoList(String title, String subtitle, String left, String right, String[] items) {
	formYesNoList(title, subtitle, left, right, items, true);
}

// alto nivel: listado con pregunta de si, no
private void formYesNoList(String title, String subtitle, String left, String right, String[] items, boolean paintSoftBack) {
	formPlainList(title, subtitle, items);
	iformBehaviour = IFORM_BEHAVIOUR_YES_NO;
	iformDataSoftLeft = left;
	iformDataSoftRight = right;
	iformPaintSoftBack = paintSoftBack;
}

// alto nivel: listado con pregunta de ok
private void formOKList(String title, String subtitle, String right, String[] items) {
	formPlainList(title, subtitle, items);
	iformBehaviour = IFORM_BEHAVIOUR_YES_NO;
	iformDataSoftLeft = null;
	iformDataSoftRight = right;
	iformPaintSoftBack = true;
}

private void formModalPopup(String txt, int c) {
	formModalPopup(txt, c, true);
	
}

private void formModalPopup(String txt, int c, boolean fx) {
	showMenuBar = false;
	requestMenuBar = false;	
	iformPopupScrollPos = 0;
	iformPopupColor = c;
	iformModalPopup = true;
	iformPopupText = txt;
	screenLost = true;
	// pequenyo hack, para simplificar
	if (c == COLOR_POPUP_BAD && fx) {
		soundPlay(1,1);
	} else if (c == COLOR_POPUP_GOOD && fx) {
		soundPlay(5,1);
	}
}

private void formWait(boolean v) {
	iformWaitDisplay = v;
	screenLost = true;
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// HACK: forzar un repaint!
	gameDraw();
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}

// alto nivel: caratula	
private void formSplash(Image s) {
	//#ifdef Debug
 	Debug.println("---Form splash generic invoked...");
	//#endif
	// reset fuentes
	iformFontTopHeader = iformFontDefault;
	iformFontSmallHeader = iformFontDefaultSmall;
	iformFont = iformFontDefault;
	iformFontSmall = iformFontDefaultSmall;
	// reset posiciones
	iformScrollTopItem = 0;
	iformSelectedItem = -1;
	iformListStartY = -1;
	iformFocusDesired = false;
	// fondo
	iformBackground = s;
	iformTopBackground = null;
	// cabeceras
	iformDataHeaderTop = null;
	iformDataHeaderSmall = null;
	// items
	iformDataListItems = null;
	iformDataListColumnNames = null;
	iformDataListItemsAttr = null;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	// comportamiento interactivo
	iformBehaviour = IFORM_BEHAVIOUR_ANY_KEY;
	// forzar un paint
	screenLost = true;
	//#ifdef Debug
 	Debug.println("-FSG invoked end");
	//#endif
	
}

// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
// --- Logica de interaccion
// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------

private final static int FORM_SPLASH = 1;
private final static int FORM_START_MENU = 2;
private final static int FORM_MAIN_MENU = 3;
private final static int FORM_ABOUT = 4;
private final static int FORM_LEAGUE = 5;
private final static int FORM_CLASSIFICATION = 6;
private final static int FORM_CHOOSE_LEAGUE = 7;
private final static int FORM_CHOOSE_TEAM = 8;
private final static int FORM_MATCH = 9;
//final static int FORM_CHOOSE_TEAM_STATS = 10;
private final static int FORM_TOP_PLAYERS = 11;
private final static int FORM_TRAINING_CHOOSE_TRAINING = 12;
private final static int FORM_TRAINING_CHOOSE_PLAYERS = 13;
private final static int FORM_TRAINING_RESULT = 14;
//private final static int FORM_TRANSFERS_MAIN = 15;
//final static int FORM_TRANSFERS_HIRE_SELECT = 16;
private final static int FORM_TRANSFERS_FIRE_SELECT = 17;
private final static int FORM_TACTICS_MAIN = 18;
private final static int FORM_TACTICS_FORMATION = 19;
private final static int FORM_TACTICS_POSITION = 20;
private final static int FORM_SAVE = 21;
private final static int FORM_MATCH_PLAY = 22;
private final static int FORM_MATCH_POST = 23;
private final static int FORM_HELP = 24;
private final static int FORM_END_LEAGUE = 25;
private final static int FORM_HELP_GENERIC = 26;
private final static int FORM_OPTIONS = 27;
private final static int FORM_OPTIONS_LANGUAGE = 28;
private final static int FORM_TRANSFERS_HIRE_SEL_LEAGUE = 29;
private final static int FORM_TRANSFERS_HIRE_SEL_TEAM = 30;
private final static int FORM_TRANSFERS_HIRE_SEL_PLAYER = 31;
private final static int FORM_CLASSIFICATION_EXTRA = 32;
private final static int FORM_SPLASH2 = 33;
private final static int FORM_SPLASH3 = 34;
private final static int FORM_SPLASH_LEGAL = 35;
private final static int FORM_CREDITS = 36;
private final static int FORM_CLASS_CHAMPIONS = 37;
private final static int FORM_TRANSFERS_HIRE_CONFIRM = 38;
private final static int FORM_TACTICS_POSITION_CONFIRM = 39;
private final static int FORM_MATCH_POST_ROUND_STATS = 40;
private final static int FORM_MATCH_POST_EUROPE_STATS = 41;
private final static int FORM_MATCH_POST_MATCH_STATS = 42;
private final static int FORM_CONFIRM_QUIT = 43;
private final static int FORM_SAVE2 = 44;
//private final static int FORM_TRANSFERS_FIRE_CONFIRM = 45;
private final static int FORM_SUBCRIPTION = 146;

private final static int FORM_GENERAL_TACTIC = 46;
private final static int FORM_TRAINING_CHANGE_SCHEDULE = 47;

//SENSEI
private final static int FORM_FINANCES_GENERAL = 50;
private final static int FORM_FINANCES_NEW_SPONSOR = 51;
private final static int FORM_FINANCES_SPONSOR = 52;
private final static int FORM_SEASON_GOAL_INFO = 53;
private final static int FORM_GAME_OVER = 54;
private final static int FORM_WELCOME = 55;
private final static int FORM_TRANSFERS_OFFER = 56;
private final static int FORM_MAIN_MENU_RETURN = 57;

//#ifdef ADVANCEDSEARCH
private final static int FORM_TRANSFERS_HIRE_SEL_SEARCH_MODE = 70;
private final static int FORM_TRANSFERS_HIRE_SEARCH_FILTER = 71;
private final static int FORM_TRANSFERS_HIRE_SEL_PLAYER_FILTERED = 72;
private final static int FORM_TRANSFERS_HIRE_CONFIRM_FILTERED = 73;
//#endif

private final static int FORM_SAVE_OVERWRITE = 74;
boolean backToStart = false;
private int currentForm = -1;

private League league = null;

// ----------------------------------------------------------------------------
// game tick - propagar los eventos de alto nivel a sus actions

//#ifdef WEBSUBCRIPTION
//#endif

public void gameTick() {

	//#ifdef Debug
 	if (lastKeyMisc == 10) {Debug.enabled = !Debug.enabled; /*if (!Debug.enabled) {gameForceRefresh=true;}*/}
	//#endif
	
	if (showMenuBar)
	{

		//boolean barOff = false;
		//System.out.println(lastKeyMenu + " - " + intKeyMenu);
		if (lastKeyMenu < 0 && currentForm != FORM_MAIN_MENU) 
		{			
			showMenuBar = false;
			screenLost = true;
		}
		else if (lastKeyMenu > 0)
		{			
			prevOption = -1;
			actionMainMenu(0);			
			screenLost = true;
		}
		
		lastKeyMenu = 0;//intKeyMenu = 0;lastKeyMisc = 0;
		formTick();
		return;
	}
		
	//#ifdef Debug
 	Debug.println("Form tick");
	//#endif	
	int hiEvent = formTick();
	
	int selectionTo;
	long d;
	Image s;
	
	switch (currentForm) {
		//#ifdef WEBSUBCRIPTION
		//#endif

		case FORM_SPLASH:			
			//#ifdef Debug
		 	Debug.println("-Form splash act");
			//#endif
			//actionSplash(hiEvent);
			//=================================================================
			if (cont1 == -1) {
				cont1 = System.currentTimeMillis();
			}
			screenLost = true;
			
			d = System.currentTimeMillis() - cont1;
			//#ifdef FASTLOAD
			//#else
			if (hiEvent == HIEVENT_IDLE && d < 2000)
			//#endif
				return;
			//#ifdef FASTLOAD
			//#endif
			iformBackground = null;
			System.gc();
			s = null;
			//#ifdef J2ME
			s = loadImage("/codemasters.png");
			//#else
			//#endif
			formSplash(s);
			cont1 = -1;
			//#ifdef Debug
		 	Debug.println("-Go Form splash 2");
			//#endif			
			currentForm = FORM_SPLASH2;			
		break;
		case FORM_START_MENU:
			actionStartMenu(hiEvent);
		break;
		case FORM_CHOOSE_LEAGUE:
			//actionChooseLeague(hiEvent);
			//=================================================================			
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == chooseLeagueItems.length) {
				if (clToEndLeague) {
					invokeEndLeague();
				} else {
					invokeStartMenu();
				}
			} else if (selectionTo >= 0) {
				league.playingLeague = selectionTo;
				invokeChooseTeam(selectionTo);
			}
		break;
		case FORM_CHOOSE_TEAM:
			//actionChooseTeam(hiEvent);
			//=================================================================
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == chooseTeamItems.length) {
				invokeChooseLeague(clToEndLeague);
			} else if (selectionTo >= 0) {
				league.userTeam = league.teams[league.playingLeague][selectionTo];
				formWait(true);
				league.coachCash = league.userTeam.cash;
				league.startSeason();
				System.gc();
				league.startJourney(true);
				System.gc();
				formWait(false);
				if (saveAfterMatch && saveInStart) {
					saveGame();
					saveInStart = false;
				}

				//#ifdef NOWELCOME
				//#else
				invokeWelcome();
				//#endif
			}
		break;
		/*case FORM_MAIN_MENU:
			actionMainMenu(hiEvent);
		break;*/
		/*case FORM_LEAGUE:
			System.out.println("FORM LEAGUE");
			//actionLeague(hiEvent);
			//=================================================================			
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == 0) {
				invokeClassification(false);
			} else if (selectionTo == 1) {
				invokeClassificationExtra(false);
			} else if (selectionTo == 2) {
				
				invokeClassChampions();
			} 		
		break;*/
		case FORM_ABOUT:			
			//actionAbout(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			if (hiEvent == HIEVENT_NO)
				invokeStartMenu();
		break;
		case FORM_CLASSIFICATION:
			//actionClassification(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;
			selectionTo = hiEventSelectedItem(hiEvent);
			//if (selectionTo == league.teams[league.playingLeague].length) {
			if (selectionTo == tcpb) {
				if (classReturnToEndLeague) {
					invokeEndLeague();
				} else {
					// SENSEI
					//invokeLeague();			
					requestMenuBar = true;
					showMenuBar = true;
					//canAccept = true;
				}
				return;
			}
			/*int focusTo = hiEventFocusedItem(hiEvent);
			if (focusTo >= 0 && focusTo < league.teams[league.playingLeague].length) {
				focusedTeam = league.teams[league.playingLeague][focusTo];
				screenLost = true;
			} else {
				focusedTeam = null;
				screenLost = true;
			}*/
		break;
		case FORM_CLASSIFICATION_EXTRA:
			//actionClassificationExtra(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			selectionTo = hiEventSelectedItem(hiEvent);
			//if (selectionTo == league.teams[league.playingLeague].length) {
			if (selectionTo == tcpb) {
				if (classReturnToEndLeague) {
					invokeEndLeague();
				} else {
					// SENSEI
					//invokeLeague();
					//canAccept = true;
					requestMenuBar = true;
					showMenuBar = true;
				}
				return;
			}
			/*int focusTo = hiEventFocusedItem(hiEvent);
			if (focusTo >= 0 && focusTo < league.teams[league.playingLeague].length) {
				focusedTeam = league.teams[league.playingLeague][focusTo];
				screenLost = true;
			} else {
				focusedTeam = null;
				screenLost = true;
			}*/
		break;
		case FORM_MATCH:
			//actionMatch(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_YES) {
				if (league.countDownPlayers(11) > 0) {
					//invokeMainMenu();
					invokeTacticsPosition(false);
					formModalPopup(gameText[240][0], COLOR_POPUP_BAD);
				} else {
					invokeMatchPlay();
				}
			} else if (hiEvent == HIEVENT_NO) {
				invokeMainMenu();
			}
		break;
		/*case FORM_CHOOSE_TEAM_STATS:
			actionChooseTeamStats(hiEvent);
		break;*/
		case FORM_TOP_PLAYERS:
			//actionTopPlayers(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == tpbv) {
			//if (selectionTo >= 0) {
				if (classReturnToEndLeague) {
					invokeEndLeague();
				} else {
					requestMenuBar = true;
					showMenuBar = true;
					//invokeMainMenu();//League(); ZNR
				}
				return;
			}
		break;
		case FORM_TRAINING_CHOOSE_TRAINING:
			//actionTrainingChooseTraining(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			
			//System.out.println("actionTrainingChooseTraining");
			selectionTo = hiEventSelectedItem(hiEvent);
			
			if (selectionTo >= 0) 
			{
				invokeTrainingChangeSchedule(selectionTo, 0);
			}
			
			
			/*if (selectionTo == tctb) {
				invokeMainMenu();
				return;
			}*/
			/*if (selectionTo >= 0) {
				if (league.coachRemainingTrainings <= 0) {
					formModalPopup(loc[52], COLOR_POPUP_BAD);
					return;
				}
				selectedTraining = selectionTo;
				invokeTrainingChoosePlayers();
			}*/
		break;
		
		case FORM_TRAINING_CHANGE_SCHEDULE:
			//actionTrainingChangeSchedule(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			
			//System.out.println("actionTrainingChangeSchedule");
			
			selectionTo = hiEventSelectedItem(hiEvent);
			
			if (selectionTo == 0) return;
			if (leftRight < 0)
			{
				//System.out.println("ddd1: "+selectionTo);		
				if (trainingSchedule[_line][selectionTo] > 0 && trainingSchedule[_line][0] < 99)
				{
					trainingSchedule[_line][selectionTo]--;
					trainingSchedule[_line][0]++;
					invokeTrainingChangeSchedule(_line, selectionTo);
				}
				
			}
			else if (leftRight > 0)
			{
				//System.out.println("ddd2: "+selectionTo);
				if (trainingSchedule[_line][0] > 0 && trainingSchedule[_line][selectionTo] < 99)
		        {
							trainingSchedule[_line][selectionTo]++;
							trainingSchedule[_line][0]--;
							invokeTrainingChangeSchedule(_line, selectionTo);
		        }
				//trainingSchedule[_line][selectionTo]++;
				
			}
			/*int selectionTo = hiEventSelectedItem(hiEvent);
			
			if (selectionTo >= 0) 
			{
				invokeTrainingChangeSchedule(selectionTo);
			}
			*/
			
			/*if (selectionTo == tctb) {
				invokeMainMenu();
				return;
			}*/
			/*if (selectionTo >= 0) {
				if (league.coachRemainingTrainings <= 0) {
					formModalPopup(loc[52], COLOR_POPUP_BAD);
					return;
				}
				selectedTraining = selectionTo;
				invokeTrainingChoosePlayers();
			}*/
		break;
		
		/*case FORM_TRAINING_CHOOSE_PLAYERS:
			actionTrainingChoosePlayers(hiEvent);
		break;
		case FORM_TRAINING_RESULT:
			actionTrainingResult(hiEvent);
		break;
		case FORM_TRANSFERS_MAIN:
			actionTransfersMain(hiEvent);
		break;*/
		case FORM_GENERAL_TACTIC:
			//actionGeneralTactic(hiEvent);
			//=================================================================
			//	ZNR
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo >= 0) {
				league.coachGeneralTactic = selectionTo;
				prevOption = league.coachGeneralTactic;
				showMenuBar = true;
				//prevOption = -1;
				screenLost = true;
			}
			/*int focusTo = hiEventFocusedItem(hiEvent);
			if (focusTo >= 0 && focusTo < league.formationsNames.length) {
				focusedFormation = focusTo;
				System.out.println("opppp 1");
				screenLost = true;
			} else if (focusTo == league.formationsNames.length) {
				focusedFormation = league.coachFormation;
				screenLost = true;
				System.out.println("opppp 1");
			}*/
			//System.out.println(lastKeyMenu+" - "+intKeyMenu);
			//if (lastKeyMenu > 0) showMenuBar = true;
		break;
		/*case FORM_TRANSFERS_HIRE_SELECT:
			actionTransfersHireSelect(hiEvent);
		break;*/
		case FORM_TRANSFERS_HIRE_SEL_LEAGUE:
			//actionTransfersHireSelLeague(hiEvent);
			//=================================================================
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == 6) {
				//#ifdef ADVANCEDSEARCH
				invokeTransfersHireSelSearchMode();
				//#else
				//#endif
				//invokeTransfersMain();
			} else if (selectionTo >= 0 && selectionTo <= 5) {
				hiringLeague = selectionTo;
				invokeTransfersHireSelTeam(selectionTo);
			}/* else if (selectionTo == 5) {
				hiringLeague = 5;
				hiringTeam = league.extraTeam;
				invokeTransfersHireSelPlayer(hiringTeam);
			}*/
		break;
		case FORM_TRANSFERS_HIRE_SEL_TEAM:
			//actionTransfersHireSelTeam(hiEvent);
			//=================================================================
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == chooseTeamItems.length) {
				invokeTransfersHireSelLeague();
			} else if (selectionTo >= 0) {
				invokeTransfersHireSelPlayer(teamListing[selectionTo]);
				teamListing = null;
			}
		break;
		case FORM_TRANSFERS_HIRE_SEL_PLAYER:
			//System.out.println("IDLE");
			//actionTransfersHireSelPlayer(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			selectionTo = hiEventSelectedItem(hiEvent);
			//System.out.println("SELECTION"+selectionTo);
			if (selectionTo == tcpb) {
				//if (hiringLeague == 5) {
				//	invokeTransfersHireSelLeague();
				//} else {
					invokeTransfersHireSelTeam(hiringLeague);
				//}
				return;
			}
			if (selectionTo >= 0) {
				short p = hiringTeam.players[selectionTo];
				Team t = hiringTeam;
				int v = league.playerValue(p);
				if (t.countPlayers() <= 16) {
					formModalPopup(gameText[238][0], COLOR_POPUP_BAD);
					requestMenuBar = true;
					return;
				}
				if (league.coachCash < v) {
					formModalPopup(gameText[191][0], COLOR_POPUP_BAD);
					requestMenuBar = true;
					return;
				}
				int userQuality = league.userTeam.realQuality();
				//int otherQuality = league.playerBalance(p, -1);
				int otherQuality = t.realQuality();
				int diff = otherQuality - userQuality;
				if (diff > 7) {
					formModalPopup(gameText[192][0], COLOR_POPUP_BAD);
					requestMenuBar = true;
					return;
				}
				invokeTransfersHireConfirm(p);
				/*t.removePlayer(p);
				league.userTeam.addPlayer(p);
				league.clearHires();
				league.coachCash -= v;
				league.userTeam.markDirtyQuality();
				invokeTransfersMain();
				//if (hiringLeague != 5) {
					formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + t.name, COLOR_POPUP_GOOD);
				//} else {
				//	formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + loc[189], COLOR_POPUP_GOOD);
				//}
				*/
			}
		break;
		case FORM_TRANSFERS_FIRE_SELECT:
			//actionTransfersFireSelect(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			selectionTo = hiEventSelectedItem(hiEvent);
			/*if (selectionTo == tcpb) {
				invokeTransfersMain();
				return;
			}*/
			if (selectionTo >= 0) 
			{
				short p = league.userTeam.players[selectionTo];
			
				
				
				if (triedToSell[selectionTo] == 0)
				{
					triedToSell[selectionTo] = 1;
					
					//triedToSell[player] = league.offertedPlayerValue(player);
				}
				else
				{
					triedToSell[selectionTo] = 0;
					//formModalPopup("No vendiendo: "+ league.playerGetName(p), COLOR_POPUP_BAD);
				}
				//#ifdef DOJA
				//#else
				int sel = triedToSell[selectionTo];
				if (gameText[61].length <= 2)
					formModalPopup(gameText[61][sel] + league.playerGetName(p), COLOR_POPUP_BAD);
				else
					formModalPopup(gameText[61][sel*2] + league.playerGetName(p)+gameText[61][(sel*2)+1], COLOR_POPUP_BAD);
				//#endif
				
				invokeTransfersFireSelect();
				requestMenuBar = true;
				
				//invokeTransfersFireConfirm(p);
				
				
				/*int otherQuality = league.playerBalance(p, -1);
				Team t = league.userTeam;
				while (t == league.userTeam)
					t = league.vacantTeamQA(otherQuality - 30);
				league.coachCash += league.playerValue(p);
				league.userTeam.removePlayer(p);
				t.addPlayer(p);
				league.userTeam.markDirtyQuality();
				invokeTransfersMain();
				formModalPopup(loc[66] + " " + league.playerGetName(p) +
					". " + loc[67] + " " + t.name , COLOR_POPUP_GOOD);
				league.coachRemainingFires--;*/
			}
		break;
		/*case FORM_TACTICS_MAIN:
			actionTacticsMain(hiEvent);
		break;*/
		case FORM_TACTICS_FORMATION:
			//actionTacticsFormation(hiEvent);
			//=================================================================			
			//	ZNR
			selectionTo = hiEventSelectedItem(hiEvent);
			/*if (selectionTo == tcpb) {
				invokeTacticsMain();
				return;
			}*/
			if (selectionTo >= 0) {
				league.coachFormation = selectionTo;
				showMenuBar = true;	
				prevOption = league.coachFormation;
				screenLost = true;
				/*invokeTacticsMain();
				return;*/		
			}
			int focusTo = hiEventFocusedItem(hiEvent);
			if (focusTo >= 0 && focusTo < gameText[104].length){//league.formationsNames.length) {
				focusedFormation = focusTo;
				screenLost = true;
			} else if (focusTo == gameText[104].length){//league.formationsNames.length) {
				focusedFormation = league.coachFormation;
				screenLost = true;
			}
			//System.out.println(lastKeyMenu+" - "+intKeyMenu);
			//if (lastKeyMenu > 0) showMenuBar = true;
		break;
		case FORM_TACTICS_POSITION:
			actionTacticsPosition(hiEvent);
		break;
		//#ifndef DOJA
		case FORM_SAVE_OVERWRITE:
			if (hiEvent == HIEVENT_YES) {
				boolean err = saveGame();
				if (backToStart) invokeStartMenu();
				else invokeMainMenu();
				if (err) formModalPopup(gameText[78][0], COLOR_POPUP_BAD);				
			} else if (hiEvent == HIEVENT_NO) {
				if (backToStart) invokeStartMenu();
				else invokeMainMenu();
			}
			break;
		//#endif
		case FORM_SAVE:
			//actionSave(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_YES) {
				//SOLO SI NO HAY ANTERIOR
				//#ifndef DOJA
				if (!savedGame)
				//#endif	
				{
					boolean err = saveGame();
					invokeStartMenu();
					if (err) formModalPopup(gameText[78][0], COLOR_POPUP_BAD);
					//#ifndef DOJA
					else savedGame = true;
					//#endif
				}
				//#ifndef DOJA
				else invokeSaveOverwrite(true);
				//#endif
			} else if (hiEvent == HIEVENT_NO) {
				invokeStartMenu();
			}
		break;
		case FORM_MATCH_PLAY:
			actionMatchPlay(hiEvent);
		break;
		case FORM_MATCH_POST:
			actionMatchPost(hiEvent);
		break;
		case FORM_HELP:
			//actionHelp(hiEvent);
			//=================================================================		
			selectionTo = hiEventSelectedItem(hiEvent);
			switch (selectionTo) {
				case 0:
					invokeHelpGeneric(22, 23);
				break;
				case 1:
					invokeHelpGeneric(134, 135);
				break;
				case 2:
					invokeHelpGeneric(141, 142);
				break;
				case 3:
					invokeHelpGeneric(152, 153);
				break;
				case 4:
					invokeHelpGeneric(154, 155);
				break;
				case 5:
					invokeHelpGeneric(156, 157);
				break;
				case 6:
					invokeHelpGeneric(158, 159);
				break;
				//#ifndef NOFINANCES
				case 7:
					invokeHelpGeneric(177, 136);
					break;
				case 8:
					if (inGameHelp) {
						invokeMainMenu();
					} else {
						invokeStartMenu();
					}
				break;
				//#else
				//#endif
			}
			/*selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == 7)
			{
				if (inGameHelp) {
					invokeMainMenu();
				} else {
					invokeStartMenu();
				}
			}
			else invokeHelpGeneric(helpGen[selectionTo][0], helpGen[selectionTo][1], helpGen[selectionTo][2]);
			*/
		break;
		case FORM_HELP_GENERIC:
			//actionHelpGeneric(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			//System.out.println("marca warra"+hiEvent);
			if(hiEvent == HIEVENT_NO) 
				invokeHelp(inGameHelp);
		break;
		case FORM_END_LEAGUE:
			//actionEndLeague(hiEvent);
			//=================================================================
			selectionTo = hiEventSelectedItem(hiEvent);
						
			if (selectionTo == 0) {
				league.startSeason();
				league.startJourney(true);
				if (saveAfterMatch) {
					saveGame();
				}
				
				// SENSEI	
				//#ifndef NOSEASONGOAL
				invokeSeasonGoalInfo();
				//#else
				//#endif
						
			} else if (selectionTo == 1) {
				if (saveAfterMatch) {
					saveInStart = true;
				}
				invokeChooseLeague(true);
			} else if (selectionTo == 2) {				
				invokeClassification(true);
			} else if (selectionTo == 3) {				
				invokeClassificationExtra(true);
			} 
			//#ifndef NOTOPPLAYERS
			else if (selectionTo == 4) {
				invokeTopPlayers(true);
			}else if (selectionTo == 5) {
				invokeStartMenu();
			}
			//#else
			//#endif
		break;
		case FORM_OPTIONS:
			actionOptions(hiEvent);
		break;
		//#ifdef MULTILANGUAGE
		case FORM_OPTIONS_LANGUAGE:
			//actionOptionsLanguage(hiEvent);
			//=================================================================
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo >= 0 && selectionTo < languageItems.length) {
				selectedLanguage = selectionTo;
				formWait(true);
				savePrefs(false);
				loadLocale(selectionTo);
				formWait(false);
				invokeOptions();
			} else if (selectionTo  == languageItems.length) {
				invokeOptions();
			}
		break;
		//#endif
		case FORM_SPLASH2:
			//#ifdef Debug
		 	Debug.println("-Form splash 2");
			//#endif						
			//actionSplash2(hiEvent);
			//=================================================================			
			if (cont1 == -1) {
				cont1 = System.currentTimeMillis();
			}
			screenLost = true;
			d = System.currentTimeMillis() - cont1;
			//#ifdef FASTLOAD
			//#else
			if (hiEvent == HIEVENT_IDLE && d < 2000)
			//#endif
				return;
			iformBackground = null;
			System.gc();
			s = null;
			//#ifdef J2ME
			s = loadImage("/caratula.png");
			//#else
			//#endif
			formSplash(s);
			titleSongPlay(true);
			cont1 = -1;
			currentForm = FORM_SPLASH3;
		break;
		case FORM_SPLASH3:
			//#ifdef Debug
		 	Debug.println("Form splash 3");
			//#endif						
			//actionSplash3(hiEvent);
			//=================================================================			
			if (cont1 == -1) {
				cont1 = System.currentTimeMillis();
			}
			screenLost = true;
			//#ifndef FASTLOAD
			d = System.currentTimeMillis() - cont1;			
			if (hiEvent == HIEVENT_IDLE && d < 2000)
				return;
			//#endif
			formWait(true);
			
			iformBackground = null;
			iformTopBackground = null;
			//#ifndef FASTLOAD
			System.gc();
			//#endif
			
			//#ifdef Debug
		 	/*Debug.println("---Loading league...");
		 	gameDraw();
		 	try{
		 		Thread.sleep(5000);
		 	}catch(Exception e){}*/
			//#endif
			//#ifdef J2ME
			   //#ifndef FASTLOAD
			byte[] plyn = loadFile("/plyn");
			byte[] plys = loadFile("/plys");
			byte[] tms = loadFile("/tms");
			byte[] cons = loadFile("/const");
			   //#endif
			//#else
			//#endif

			//#ifndef SOUNDTEST
			//System.out.println("Event 1");
			league = new League(gameText);
			//System.out.println("Event 2");
			//#ifndef FASTLOAD
			league.load(cons, plyn, plys, tms);
			//#endif
			//System.out.println("Event 3");
			//#endif
			
			//#ifdef Debug
		 	Debug.println("---END Loading league...");
		 	gameDraw();
		 	//#endif
		 	   //#ifndef FASTLOAD
			plyn = null;
			plys = null;
			tms = null;
			cons = null;
			System.gc();
			   //#endif
			//#ifdef J2ME
			
				//#ifndef NOBACKGROUNDIMAGE
				iformBackground = loadImage("/bg1.png");
				//#else
			    //#endif
			
				iformTopBackground = loadImage("/menu.png");
				fieldImage = loadImage("/field.png");
				redBullet = loadImage("/rojo.png");
				blackBullet = loadImage("/negro.png");
				blueBullet = loadImage("/azul.png");
				arrow = loadImage("/arrow.png");
				menuIcons = loadImage("/iconos.png");			
			
				//#ifdef BUG_IMAGE_LEAK
				//#endif

			//#else
			//#endif
			//#ifndef FASTLOAD	
			System.gc();
			//#endif
			//invokeStartMenu();
			invokeSplashLegal();
			formWait(false);			
		break;
		case FORM_SPLASH_LEGAL:
			//actionSplashLegal(hiEvent);
			//=================================================================
			/*
			//#ifdef SOUNDTEST						
				if (hiEvent == HIEVENT_IDLE)
					return;
				if (playSound%2 == 0) soundPlay(playSound/2, 1);
				else soundStop();
				playSound++;
				//#ifdef PLAYER_OTA
				playSound = playSound%(sound.length*2);
				//#else
				playSound = playSound%(Sonidos.length*2);
				//#endif				
			//#else
			*/
				//#ifdef SOUNDBUG_NGAGE
	            //#endif
	            //#ifdef FASTLOAD
	            //#else	            	
			if (hiEvent == HIEVENT_IDLE && System.currentTimeMillis()-splashLegalStart < 2000)
				//#endif
				return;
			
			intKeyMenu=0; intKeyX=0; intKeyY=0; intKeyMisc=0; intKeyCode=0;
			
			invokeStartMenu();
			//#endif
		break;
		case FORM_CREDITS:
			//actionCredits(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			if (hiEvent == HIEVENT_NO)
				invokeStartMenu();
		break;
		case FORM_CLASS_CHAMPIONS:
			//actionClassChampions(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			//showMenuBar = true;
			if (hiEvent == HIEVENT_NO)
				invokeMainMenu();
		break;
		case FORM_TRANSFERS_HIRE_CONFIRM:
			//actionTransfersHireConfirm(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_NO) {
				invokeTransfersHireSelPlayer(hiringTeam);
				return;
			}
			if (hiEvent == HIEVENT_YES) {
				short p = hiringPlayer;
				Team t = hiringTeam;
				int v = league.playerValue(p);
				t.removePlayer(p);
				league.userTeam.addPlayer(p);
				league.availHires--;
				league.coachCash -= v;
				
				// SENSEI
				//#ifndef NOFINANCES
				league.journeyExpenses += v;
				//#endif
				
				league.userTeam.markDirtyQuality();
				//invokeTransfersMain();
				invokeMainMenuReturn();
				formModalPopup(gameText[63][0] + spaceString + league.playerGetName(p) + spaceString + gameText[64][0] + spaceString + t.name, COLOR_POPUP_GOOD);
			}
		break;
		/*case FORM_TRANSFERS_FIRE_CONFIRM:
			actionTransfersFireConfirm(hiEvent);
		break;*/
		case FORM_TACTICS_POSITION_CONFIRM:
			//actionTacticsPositionConfirm(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_NO) {
				invokeTacticsPosition(true);
				iformScrollTopItem = savediformScrollTopItem;
				iformSelectedItem = savediformSelectedItem;
				focusedPlayer = ordDestPlayer;
				return;
			} else if (hiEvent == HIEVENT_YES) {
				remainingMatchChanges--;
				league.userTeam.swapOrdinalToOrdinal(ordPickedPlayer, ordDestPlayer); 
				invokeTacticsPosition(true);
				iformScrollTopItem = savediformScrollTopItem;
				iformSelectedItem = savediformSelectedItem;
				focusedPlayer = ordDestPlayer;
				return;
			}
		break;
		case FORM_MATCH_POST_ROUND_STATS:
			//actionMatchPostRoundStats(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == tpbv) {
				invokeMatchPost();
				return;
			}
		break;
		case FORM_MATCH_POST_EUROPE_STATS:
			//actionMatchPostEuropeStats(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			selectionTo = hiEventSelectedItem(hiEvent);
			if (selectionTo == tpbv) {
				invokeMatchPost();
				return;
			}
		break;
		case FORM_MATCH_POST_MATCH_STATS:
			//actionMatchPostMatchStats(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;
			if (hiEvent == HIEVENT_NO)
				invokeMatchPost();
		break;
		case FORM_CONFIRM_QUIT:
			//actionConfirmQuit(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_YES) {		
				titleSongPlay(false);
				//#ifdef WAIT_ON_QUIT
				//#endif
				gameExit = true;
			} else if (hiEvent == HIEVENT_NO) {
				//requestMenuBar = false;
				invokeStartMenu();
			}
		break;
		case FORM_SAVE2:
			//actionSave2(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_YES) {
				//#ifndef DOJA
				if (!savedGame)
				//#endif
				{				
					boolean err = saveGame();
					invokeMainMenu();
					if (err) formModalPopup(gameText[78][0], COLOR_POPUP_BAD);
					//#ifndef DOJA
					else savedGame = true;
					//#endif
				}
				//#ifndef DOJA
				else invokeSaveOverwrite(false);
				//#endif
			} else if (hiEvent == HIEVENT_NO) {
				invokeMainMenu();
			}
		break;
		
//#ifndef NOFINANCES		
		//SENSEI
		
		/*
		case FORM_FINANCES_GENERAL:
			//actionFinancesGeneral(hiEvent);
			//=================================================================
			if (hiEvent == HIEVENT_IDLE)
				return;	
			//requestMenuBar = true;
			showMenuBar = true;
			invokeMainMenu();
		break;
		*/
		
		case FORM_FINANCES_NEW_SPONSOR:
			//actionFinancesNewSponsor(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;	
			if (hiEvent == HIEVENT_YES) {
				
				// Se contrata el sponsor
				//#ifndef NOFINANCES
				league.haveSponsor = true;
				//#endif
						
				//invokeFinancesSponsor();
				invokeMainMenuReturn();
				formModalPopup(gameText[TEXT_SENSEI + 1][4], COLOR_POPUP_GOOD);		
				return;
					
			} else if (hiEvent == HIEVENT_NO) {
					
				// Cancela		
				invokeMainMenu();
			}
		break;
		
		case FORM_FINANCES_SPONSOR:
			//actionFinancesSponsor(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;	
			if (hiEvent == HIEVENT_YES) {
				
				// Se cambia de sponsor
				league.haveSponsor = false;
							
				//invokeFinancesNewSponsor();
				invokeMainMenuReturn();
				formModalPopup(gameText[TEXT_SENSEI + 1][8], COLOR_POPUP_GOOD);
				return;
					
			} else if (hiEvent == HIEVENT_NO) {
					
				// Cancela		
				invokeMainMenu();
			}
		break;
//#endif		
		
		case FORM_TRANSFERS_OFFER:
			actionTransfersOffer(hiEvent);
		break;
		
		//#ifndef NOSEASONGOAL
		case FORM_SEASON_GOAL_INFO:
			//actionSeasonGoalInfo(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;
			invokeMainMenu();
		break;
		//#endif
		
//#ifndef NOFINANCES		
//#ifndef NOSEASONGOAL		
		//#define GAMEOVERSCREEN
		/*case FORM_GAME_OVER:
			//actionGameOver(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;
			invokeStartMenu();
		break;*/
//#endif
//#endif
		
//#ifdef NOSEASONGOAL
	//#ifndef NOFINANCES
		//#define GAMEOVERSCREEN
	//#endif
//#endif
//#ifdef NOFINANCES
		//#ifndef NOSEASONGOAL
			//#define GAMEOVERSCREEN
		//#endif
//#endif

		
//#ifdef GAMEOVERSCREEN
		case FORM_GAME_OVER:
			//actionGameOver(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;
			invokeStartMenu();
		break;		
//#endif
		
//#ifndef NOWELCOME		
		case FORM_WELCOME:
			//actionWelcome(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;
			invokeSeasonGoalInfo();
		break;
//#endif		
		
		case FORM_MAIN_MENU_RETURN:
			//actionMainMenuReturn(hiEvent);
			//=================================================================			
			if (hiEvent == HIEVENT_IDLE)
				return;
			invokeMainMenu();
		break;
		
		//#ifdef ADVANCEDSEARCH
		case FORM_TRANSFERS_HIRE_SEL_SEARCH_MODE:
			actionTransfersHireSelSearchMode(hiEvent);
		break;
		
		case FORM_TRANSFERS_HIRE_SEARCH_FILTER:
			actionTransfersHireSearchFilter(hiEvent);
		break;
		
		case FORM_TRANSFERS_HIRE_SEL_PLAYER_FILTERED:
			actionTransfersHireSelPlayerFiltered(hiEvent);
		break;
		
		case FORM_TRANSFERS_HIRE_CONFIRM_FILTERED:
			actionTransfersHireConfirmFiltered(hiEvent);
		break;
		//#endif	
	}
	//System.out.println("gameTick n");
}

// ----------------------------------------------------------------------------
// splash

private boolean repaintHack = true;

private long cont1 = -1;
private void actionSplash(int hiEvent) {
	if (cont1 == -1) {
		cont1 = System.currentTimeMillis();
	}
	screenLost = true;
	long d = System.currentTimeMillis() - cont1;
	if (hiEvent == HIEVENT_IDLE && d < 2000)
		return;
	iformBackground = null;
	System.gc();
	Image s = null;
//#ifdef J2ME
	s = loadImage("/codemasters.png");
//#else
//#endif
	formSplash(s);
	cont1 = -1;
	currentForm = FORM_SPLASH2;
}

private void actionSplash2(int hiEvent) {
	if (cont1 == -1) {
		cont1 = System.currentTimeMillis();
	}
	screenLost = true;
	long d = System.currentTimeMillis() - cont1;
	if (hiEvent == HIEVENT_IDLE && d < 2000)
		return;
	iformBackground = null;
	System.gc();
	Image s = null;
//#ifdef J2ME
	s = loadImage("/caratula.png");
//#else
//#endif
	formSplash(s);
	titleSongPlay(true);
	cont1 = -1;
	currentForm = FORM_SPLASH3;
}

//#ifdef BUG_IMAGE_LEAK
//#endif


/*
private void actionSplash3(int hiEvent) {
	if (cont1 == -1) {
		cont1 = System.currentTimeMillis();
	}
	screenLost = true;
	long d = System.currentTimeMillis() - cont1;
	if (hiEvent == HIEVENT_IDLE && d < 3000)
		return;
	formWait(true);
	iformBackground = null;
	iformTopBackground = null;
	System.gc();

//#ifdef J2ME
	byte[] plyn = loadFile("/plyn");
	byte[] plys = loadFile("/plys");
	byte[] tms = loadFile("/tms");
	byte[] cons = loadFile("/const");
//#else
	byte[] plyn = FS_LoadFile(1,0);
	byte[] plys = FS_LoadFile(1,1);
	byte[] tms = FS_LoadFile(1,2);
	byte[] cons = FS_LoadFile(1,4);
//#endif

	//#ifndef SOUNDTEST
	//System.out.println("Event 1");
	league = new League(gameText);
	//System.out.println("Event 2");
	league.load(cons, plyn, plys, tms);
	//System.out.println("Event 3");
	//#endif
	plyn = null;
	plys = null;
	tms = null;
	cons = null;
	System.gc();

//#ifdef J2ME
	//iformBackground = loadImage("/1.png");
	System.out.println("rama 0");
	//#ifndef NOBACKGROUNDIMAGE
	iformBackground = loadImage("/bg1.png");
	//#endif
	//#ifdef FAKETOPBACKGROUND
	System.out.println("rama 1");
	iformTopBackground = loadImage("/menu.png");
	//#else
	System.out.println("rama 2");
	iformTopBackground = menuIcons;
	//#endif
	fieldImage = loadImage("/field.png");
	redBullet = loadImage("/rojo.png");
	blackBullet = loadImage("/negro.png");
	blueBullet = loadImage("/azul.png");
	arrow = loadImage("/arrow.png");
	menuIcons = loadImage("/iconos.png");
	

//#ifdef BUG_IMAGE_LEAK
	imgBug1 = loadImage("/bg3.png");
	imgBug2 = loadImage("/marcador.png");
	imgBug3 = iformBackground;//loadImage("/bg1.png");
	imgBug4 = loadImage("/menu.png");
	imgBug5 = loadImage("/bg2.png");
//#endif

//#else
	iformBackground = FS_LoadImage(2,0);
	iformTopBackground = FS_LoadImage(2,1);
	fieldImage = FS_LoadImage(3,0);
	redBullet = FS_LoadImage(3,3);
	blackBullet = FS_LoadImage(3,2);
	blueBullet = FS_LoadImage(3,1);
	menuIcons = FS_LoadImage(6);

//#endif
	
	System.gc();
	//invokeStartMenu();
	invokeSplashLegal();
	formWait(false);
}//Albert
*/

// ----------------------------------------------------------------------------
// splash legal


long splashLegalStart;

private void invokeSplashLegal() {
	fontSet(iformFontDefaultSmall);
	String[] s = printTextBreak(gameText[233][0], canvasWidth - 8);
	formPlainList(gameText[203][0], null, s);
	currentForm = FORM_SPLASH_LEGAL;
	iformFont = iformFontDefaultSmall;
	//iformDataSoftLeft = loc[92];
	//#ifdef SOUNDTEST
	//#endif
	splashLegalStart = System.currentTimeMillis();
}



//#ifdef SOUNDTEST
//#else
private void actionSplashLegal(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE && System.currentTimeMillis()-splashLegalStart < 2000)
		return;
	invokeStartMenu();
}
//#endif

//#ifndef DOJA
boolean savedGame; 
//#endif

// ----------------------------------------------------------------------------
// start menu

private void invokeStartMenu() {
	//#ifndef NOCLUBBAR
	flagColors[0] = flagColors[1] = flagColors[2] = 0x888888;
	//#endif
	
	String[] startMenuItems;
	String[] startMenuItemsContinue = { gameText[8][0], gameText[9][0], gameText[10][0], gameText[11][0], gameText[12][0], gameText[196][0], gameText[13][0] };
	//#ifndef DOJA
	String[] startMenuItemsNoContinue = { gameText[8][0],  gameText[10][0], gameText[11][0], gameText[12][0], gameText[196][0], gameText[13][0] };
	byte[] data = loadPersistent(1);
	if (data == null)
	{
		savedGame = false;
		startMenuItems = startMenuItemsNoContinue;
	}
	else 
	{
		savedGame = true;
	//#endif
	startMenuItems = startMenuItemsContinue;
	//#ifndef DOJA
	}
	//#endif
	formPlainMenu(gameText[14][0], null, startMenuItems);
	currentForm = FORM_START_MENU;
//#ifndef NOCHECKSAVESIZE
	// check save game size
	if (sizeAvailPersistent("fmsavegame") < 3000) {
		formModalPopup(gameText[15][0], COLOR_POPUP_BAD, false);
	}
//#endif
	titleSongPlay(true);
	requestMenuBar = false;
	showMenuBar = false;
	// ZNR TODO: quiza es para todos los moviles con listener
	//#ifdef DOJA
	//listenerInit(gameText[TEXT_QUIT][0], gameText[TEXT_OK][0]);
	//#endif
	//#ifdef BACKCOLOR
	//#endif
}

private void actionStartMenu(int hiEvent) {
	byte[] data;
	int selectionTo = hiEventSelectedItem(hiEvent);
	//#ifndef DOJA
	if (!savedGame && selectionTo > 0) selectionTo++;
	//#endif
	
	switch (selectionTo) {
		//NEW GAME
		case 0: {			
			titleSongPlay(false);
			//#ifndef LOWMEM
				formWait(true);
				league = null;
				System.gc();
				//#ifdef J2ME
				byte[] plyn = loadFile("/plyn");
				byte[] plys = loadFile("/plys");
				byte[] tms = loadFile("/tms");
				byte[] cons = loadFile("/const");
				//#else
				//#endif
				league = new League(gameText);
				league.load(cons, plyn, plys, tms);
				plyn = null;
				plys = null;
				tms = null;
				System.gc();
				formWait(false);
			//#else
			//#endif
			invokeChooseLeague(false);
			
			for(int i = 0; i < trainingSchedule.length; i++)
			{
				int percTotal = 0;
				for(int j = 0; j < trainingSchedule[i].length; j++)
				{
					int perc = TOTAL_TRAINING_TIME / trainingSchedule[i].length;
					percTotal += perc;
					trainingSchedule[i][j] = perc;
				}
				trainingSchedule[i][0] += TOTAL_TRAINING_TIME-percTotal;
			}
			
		} break;
		case 1: {
			formWait(true);			
			data = loadPersistent(1);
			formWait(false);
			if (data == null) {
				formModalPopup(gameText[16][0], COLOR_POPUP_BAD, false);
				return;
			}
			// version check
			/*if (data[0] != 14) {
				System.out.println("ver: "+data[0]);
				formModalPopup(gameText[17][0], COLOR_POPUP_BAD, false);
				return;
			}*/
			titleSongPlay(false);
			formWait(true);
			//#ifndef LOWMEM
			league = null;
			System.gc();
			//#ifdef J2ME
			byte[] plyn = loadFile("/plyn");
			byte[] plys = loadFile("/plys");
			byte[] tms = loadFile("/tms");
			byte[] cons = loadFile("/const");
			//#else
			//#endif
			league = new League(gameText);
			league.load(cons, plyn, plys, tms);
			plyn = null;
			plys = null;
			tms = null;
			//#endif
			System.gc();
			if (league.loadState(data)) {
				formWait(false);
				formModalPopup(gameText[234][0], COLOR_POPUP_BAD, false);
				titleSongPlay(true);
				return;
			}
			league.startJourney(false);
			System.gc();
			loadFormBackground();
			formWait(false);
			invokeMainMenu();
		} break;
		case 2:
			invokeOptions();
			break;
		case 3:
			invokeHelp(false);
			//titleSongPlay(false);
			break;
		case 4:
			invokeAbout();
			break;
		case 5:
			//invokeCredits();
			//========================================
			fontSet(iformFontDefaultSmall);
			String[] s = printTextBreak(gameText[TEXT_CREDITS], canvasWidth - 8);
			formPlainList(gameText[14][0], gameText[196][0], s);
			currentForm = FORM_CREDITS;
			iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO;
			iformFont = iformFontDefaultSmall;
			break;
		case 6:
		case 7:
			invokeConfirmQuit();
			//titleSongPlay(false);
			//gameExit = true;
			break;
	}
}

// ----------------------------------------------------------------------------
// confirm quir

private void invokeConfirmQuit() {
	// hack...
	fontSet(iformFontDefault);
	String[] its = printTextBreak(gameText[235][0], canvasWidth - 8);
	formPlainListYesNo(gameText[14][0], null, gameText[3][0], gameText[2][0], its);
	currentForm = FORM_CONFIRM_QUIT;
	showMenuBar = false;
}

private void actionConfirmQuit(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {		
		titleSongPlay(false);
		//#ifdef WAIT_ON_QUIT
		//#endif
		gameExit = true;
	} else if (hiEvent == HIEVENT_NO) {
		//requestMenuBar = false;
		invokeStartMenu();
	}
}

// ----------------------------------------------------------------------------
// options

//#ifndef PLAYER_NONE
private void invokeOptions() {
	String[] optionsItems = null;
	//#ifndef MULTILANGUAGE
	//#else
	//} else {
		optionsItems = new String[3];
		optionsItems[0] = null;
		optionsItems[1] = null;
		optionsItems[2] = gameText[18][0];
		//optionsItems[3] = gameText[TEXT_BACK][0];
	//}
	//#endif
	if (gameSound) {
		optionsItems[0] = gameText[19][0] + twoDotString + gameText[20][0];
	} else {
		optionsItems[0] = gameText[19][0] + twoDotString + gameText[21][0];
	}
	if (saveAfterMatch) {
		optionsItems[1] = gameText[263][0] + twoDotString + gameText[20][0];
	} else {
		optionsItems[1] = gameText[263][0] + twoDotString + gameText[21][0];
	}
	formPlainMenu(gameText[10][0], null, optionsItems);
	//#ifdef BIGGEROPTIONSFONT
	//#else
	iformFont = iformFontDefaultSmall;
	//#endif
	currentForm = FORM_OPTIONS;
}

private void actionOptions(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	//if (languageItems.length <= 1) {
	//#ifndef MULTILANGUAGE
	//#else
		switch (selectionTo) {
			case 0:
				gameSound = !gameSound;
				//#ifdef SH-GX15
				//#endif
				savePrefs(false);
				//#ifdef SH-GX15
				//#endif
				invokeOptions();
			break;
			case 1:
				saveAfterMatch = !saveAfterMatch;
				//#ifdef SH-GX15
				//#endif
				savePrefs(false);
				//#ifdef SH-GX15
				//#endif
				invokeOptions();
				iformSelectedItem = 1;
			break;
			case 2:
				invokeOptionsLanguage();
			break;
			case 3:
				invokeStartMenu();
			break;
		}
	//}
	//#endif
}

//#else
//#endif

// ----------------------------------------------------------------------------
// options - language

//#ifdef MULTILANGUAGE
private void invokeOptionsLanguage() {
	/*String[] l = new String[languageItems.length + 1];
	System.arraycopy(languageItems, 0, l, 0, languageItems.length);
	l[languageItems.length] = gameText[TEXT_BACK][0];*/
	formPlainMenu(gameText[18][0], null, languageItems);
	currentForm = FORM_OPTIONS_LANGUAGE;
}

private void actionOptionsLanguage(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo >= 0 && selectionTo < languageItems.length) {
		selectedLanguage = selectionTo;
		formWait(true);
		savePrefs(false);
		loadLocale(selectionTo);
		formWait(false);
		invokeOptions();
	} else if (selectionTo  == languageItems.length) {
		invokeOptions();
	}
}
//#endif

// ----------------------------------------------------------------------------
// help

private boolean inGameHelp;
private void invokeHelp(boolean inGame) {
	inGameHelp = inGame;
	String[] helpMenuItems = { gameText[22][0], gameText[134][0], gameText[141][0], gameText[152][0], gameText[154][0], gameText[156][0], gameText[158][0],
			//#ifndef NOFINANCES
			gameText[177][0]
			//#endif
			              };
	//String[] helpMenuItems2 = { gameText[22][0], gameText[134][0], gameText[141][0], gameText[152][0], gameText[154][0], gameText[156][0], gameText[158][0], gameText[177][0], gameText[TEXT_BACK][0] };
	//String[] helpMenuItems;
	/*if (inGameHelp)
		helpMenuItems = helpMenuItems1;	
	else
		helpMenuItems = helpMenuItems2;*/
	requestMenuBar = inGameHelp;
	formPlainMenu(gameText[11][0], null, helpMenuItems);
	currentForm = FORM_HELP;
	showMenuBar = false;	
}


/*final static int helpGen[][] = {
	{22, 23, 25},
	{134, 135, 140},
	{141, 142, 151},
	{152, 153, 153},
	{154, 155, 155},
	{156, 157, 157},
	{158, 159, 159}};
*/
/*
private void actionHelp(int hiEvent) {
	System.out.println("dasdasdasds 22222222");
	int selectionTo = hiEventSelectedItem(hiEvent);
	//
	if (selectionTo == 7)
	{
		if (inGameHelp) {
			invokeMainMenu();
		} else {
			invokeStartMenu();
		}
	}
	else invokeHelpGeneric(helpGen[selectionTo][0], helpGen[selectionTo][1], helpGen[selectionTo][2]);
	//
	switch (selectionTo) {
		case 0:
			invokeHelpGeneric(22, 23);
		break;
		case 1:
			invokeHelpGeneric(134, 135);
		break;
		case 2:
			invokeHelpGeneric(141, 142);
		break;
		case 3:
			invokeHelpGeneric(152, 153);
		break;
		case 4:
			invokeHelpGeneric(154, 155);
		break;
		case 5:
			invokeHelpGeneric(156, 157);
		break;
		case 6:
			invokeHelpGeneric(158, 159);
		break;
		case 7:
			if (inGameHelp) {
				invokeMainMenu();
			} else {
				invokeStartMenu();
			}
		break;
	}
}
*/
// ----------------------------------------------------------------------------
// help - generic

private void invokeHelpGeneric(int tn, int from) {
	int count = gameText[from].length;
	//System.out.println("from:"+from+" count:"+count);
	String[] text = new String[(count*2)];
	for (int i = 0; i < (count); i++) {
		text[i*2] = gameText[from][i];
		text[i*2+1] = spaceString;
		//System.out.println(text[i*2]+text[i*2+1]);
	}
	fontSet(iformFontDefaultSmall);
	String[] s = printTextBreak(text, canvasWidth - 8);
	formPlainList(gameText[tn][0], null, s);
	currentForm = FORM_HELP_GENERIC;
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO;
	iformFont = iformFontDefaultSmall;
	requestMenuBar = false;
}

/*
private void actionHelpGeneric(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeHelp(inGameHelp);
}
*/
// ----------------------------------------------------------------------------
// about

private void invokeAbout() {
	fontSet(iformFontDefaultSmall);

//#ifdef J2ME
	//#ifdef FAKEVERSION
	//#else
	String[] ptb = { gameText[264][0] + spaceString + ga.getAppProperty("MIDlet-Version"), spaceString,
	//#endif
	gameText[259][0], spaceString, gameText[259][1], gameText[259][2], spaceString, gameText[26][0] };
//#else
//#endif
	String[] aboutItems = printTextBreak(ptb, canvasWidth - 8);
	formPlainList(gameText[14][0], gameText[12][0], aboutItems);
	currentForm = FORM_ABOUT;
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO;
	iformFont = iformFontDefaultSmall;
}
/*
private void actionAbout(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeStartMenu();
}*/

// ----------------------------------------------------------------------------
// credits

//private void invokeCredits() {
	/*String[] jditems = new String[8];
	//jditems[0] = loc[259];
	int lasti = 0;
	for (int k = 0; k < 8; k++) {
		int nexti = loc[260].indexOf('|', lasti + 1);
		jditems[k] = loc[260].substring(lasti+1, nexti);
		lasti = nexti;
	}

	String[] cmitems = new String[9];
	lasti = 0;
	for (int k = 0; k < 9; k++) {
		int nexti = loc[261].indexOf('|', lasti + 1);
		cmitems[k] = loc[261].substring(lasti+1, nexti);
		lasti = nexti;
	}

//#ifdef J2ME
	//#ifdef SG-D500
	String[] gfx = { "Ra�l Dur�n" };
	String[] prog = { "Carlos Carrasco" };
	String[] testing = { "Alfred Ferrer", "Abel Andreu", "Toni Gonz�lez", "Eduardo Berti",
		"Josep Ruiz", "Xavi Espejo", "Joaqu�n Hurtado", "Jordi Balfeg�", "Juan Moll�",
		"Arnau Castellv�", "Albert Sans" };
	String[] snd = { "Jordi Guti�rrez" };
	String[] ack = { "Juan A. G�mez", "El�as Lozano", "Raimon R�fols", "Itthi Yossundara" };

	//#else
	String[] gfx = { "Ra�l Dur�n" };
	String[] prog = { "Carlos Carrasco" };
	String[] testing = { "Alfred Ferrer", "Abel Andreu", "Toni Gonz�lez", "Eduardo Berti",
		"Josep Ruiz", "Xavi Espejo", "Joaqu�n Hurtado", "Jordi Balfeg�", "Juan Moll�",
		"Arnau Castellv�", "Albert Sans" };
	String[] snd = { "Jordi Guti�rrez" };
	String[] ack = { "Juan A. G�mez", "El�as Lozano", "Raimon R�fols", "Itthi Yossundara" };

	//#endif
//#else
	String[] gfx = { loc[271] };
	String[] prog = { loc[272] };
	String[] testing = { loc[273], loc[274], loc[275], loc[276],
		loc[277], loc[278], loc[279], loc[280], loc[281],
		"Arnau Castellv�" };
	String[] snd = { loc[287] };
	String[] ack = { loc[282], loc[283], loc[284], loc[285] };
//#endif

	String[] aboutItems = new String[6*2 + 1 + gfx.length + prog.length + testing.length + ack.length + snd.length +
		jditems.length + cmitems.length];
	aboutItems[0] = loc[197];
	aboutItems[1] = " ";

	// gfx
	aboutItems[2] = loc[198];
	System.arraycopy(gfx, 0, aboutItems, 3, gfx.length);
	int i = gfx.length+3;
	aboutItems[i] = " ";
	i++;

	// prog
	aboutItems[i] = loc[199];
	System.arraycopy(prog, 0, aboutItems, i+1, prog.length);
	i += prog.length+1;
	aboutItems[i] = " ";
	i++;

	// testing
	aboutItems[i] = loc[200];
	System.arraycopy(testing, 0, aboutItems, i+1, testing.length);
	i += testing.length+1;
	aboutItems[i] = " ";
	i++;

	// snd
	aboutItems[i] = loc[286];
	System.arraycopy(snd, 0, aboutItems, i+1, snd.length);
	i += snd.length+1;
	aboutItems[i] = " ";
	i++;

	// ack
	aboutItems[i] = loc[201];
	System.arraycopy(ack, 0, aboutItems, i+1, ack.length);
	i += ack.length+1;
	aboutItems[i] = " ";
	i++;

	// jd
	System.arraycopy(jditems, 0, aboutItems, i, jditems.length);
	i += jditems.length;
	aboutItems[i] = " ";
	i++;

	// cm
	System.arraycopy(cmitems, 0, aboutItems, i, cmitems.length);
	//i += cmitems.length;
	//aboutItems[i] = " ";
	//i++;
*/	
	
	//fontSet(iformFontDefaultSmall);
	//String[] s = printTextBreak(gameText[TEXT_CREDITS], canvasWidth - 8);
	//formPlainList(gameText[14][0], gameText[196][0], s);
	//currentForm = FORM_CREDITS;
	//iformFont = iformFontDefaultSmall;
	
//}
/*
private void actionCredits(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeStartMenu();
}*/

// ----------------------------------------------------------------------------
// choose league

private String[] chooseLeagueItems;
private boolean clToEndLeague = false;
private void invokeChooseLeague(boolean b) {
	clToEndLeague = b;
	chooseLeagueItems = new String[6];
	for (int i = 0; i < 6; i++) {
		chooseLeagueItems[i] = gameText[95+i][0];
	}
	//chooseLeagueItems[6] = gameText[TEXT_BACK][0];
	formPlainMenu(gameText[31][0], null, chooseLeagueItems);
	currentForm = FORM_CHOOSE_LEAGUE;
}

/*
private void actionChooseLeague(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == chooseLeagueItems.length - 1) {
		if (clToEndLeague) {
			invokeEndLeague();
		} else {
			invokeStartMenu();
		}
	} else if (selectionTo >= 0) {
		league.playingLeague = selectionTo;
		invokeChooseTeam(selectionTo);
	}
}
*/

// ----------------------------------------------------------------------------
// choose team
//#ifndef NOCLUBBAR
final static int[][] leagueColors = {
		{0xffffff,0xff0000, 0xffffff}, //UK
		{0x071D67,0xffffff,0x071D67}, //SCOT
		{0x000000,0xff0000,0xffff00}, //GERMANY
		{0x008800,0xffffff,0xCC1325}, //ITALY
		{0x0015E0,0xffffff,0xff0000}, //FRANCE
		{0xff0000,0xffff00,0xff0000}, //SPAIN
};
//#endif

private String[] chooseTeamItems;
private void invokeChooseTeam(int selectionTo) {
	String[] chooseTeamLabels = { gameText[32][0], gameText[190][0] };
	int n = League.leagueTeams[selectionTo];	
	//#ifndef NOCLUBBAR	
	flagColors[0] = leagueColors[selectionTo][0];
	flagColors[1] = leagueColors[selectionTo][1];
	flagColors[2] = leagueColors[selectionTo][2];	
	//#endif
	chooseTeamItems = new String[n];
	for (int i = 0; i < n; i++) {
		chooseTeamItems[i] = league.teams[selectionTo][i].name;
	}
	//chooseTeamItems[n] = gameText[TEXT_BACK][0];
	String[] chooseTeamCash = new String[n];
	for (int i = 0; i < n; i++) {
		chooseTeamCash[i] = league.cashStr(league.teams[selectionTo][i].cash);
	}
	//chooseTeamCash[n] = "";
	String[][] its = new String[2][];
	its[0] = chooseTeamItems;
	its[1] = chooseTeamCash;
	formPlainMenu(gameText[34][0], gameText[95+selectionTo][0], its, chooseTeamLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CHOOSE_TEAM;
}


private void actionChooseTeam(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == chooseTeamItems.length - 1) {
		invokeChooseLeague(clToEndLeague);
	} else if (selectionTo >= 0) {
		league.userTeam = league.teams[league.playingLeague][selectionTo];
		formWait(true);
		//System.out.println("Peta 1");
		league.coachCash = league.userTeam.cash;
		//System.out.println("Peta 2");
		league.startSeason();
		//System.out.println("Peta 3");
		System.gc();
		//System.out.println("Peta 4");
		league.startJourney(true);
		//System.out.println("Peta 5");
		System.gc();
		formWait(false);
		if (saveAfterMatch && saveInStart) {
			saveGame();
			saveInStart = false;
		}
		//#ifdef NOWELCOME
		//#else
		invokeWelcome();
		//#endif
	}
}


// ----------------------------------------------------------------------------
// main menu

//#ifndef NOCALENDAR
private String getCurrentDate(String format, int week)
{
	String dat[] = league.getCurrentDate(week, League.season);
	
	String ret = new String();
	
	for(int i = 0; i < format.length();i++) {
		switch(format.charAt(i))
		{
			case 'Y': ret = ret+dat[0];break;
			case 'M': ret = ret+dat[1];break;
			case 'D': ret = ret+dat[2];break;
			case 'W': ret = ret+dat[3];break;
			case '*': 
				if (dat[2] == "1" || dat[2] == "21" || dat[2] == "31")
					ret = ret+"st";
				if (dat[2] == "2" || dat[2] == "22")
					ret = ret+"nd";
				if (dat[2] == "3" || dat[2] == "23")
					ret = ret+"rd";
				else
					ret = ret+"th";
				break;
			default: ret = ret+format.charAt(i);break;
		}
	}
	return ret;
}
//#endif

static int sellPlayerPointer = 0;
static int tries;

private void invokeMainMenu() {
	//ZNR
	//#ifndef NOCLUBBAR
	flagColors[0] = flagColors[2] = league.userTeam.color1;
	flagColors[1] = league.userTeam.color2;
	//#endif
	
	//System.out.println("START invokeMainMenu");
	//String[] mainMenuItems = { loc[37], loc[38], loc[39], loc[40], loc[41], loc[11], loc[265], loc[42] };
	//#ifdef NOCALENDAR
	//#else
	String tmp = getCurrentDate(gameText[TEXT_DATEFORMAT][0],league.journeyNumber+
			//#ifndef NOPRESEASON
			league.preSeasonMatchesPlayed+
			//#endif
			1);
	//#endif
	
	//#ifdef EXTRAINFO
		String playing = gameText[122][0];
		if (!league.champJourney) {
			//#ifndef NOPRESEASON		
			if (league.preSeasonPlaying) playing = gameText[39][1];
			else 
			//#endif
				playing = gameText[39][0];
		}
		String[] mainMenuItems = { tmp , gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + spaceString + gameText[TEXT_CURRENCY][0], playing }, its;
	//#else
	//#endif
	
	//#ifdef ALWAYS_WIN
	//mainMenuItems[0] += " - CHEATING";
	//#endif
	
	//#ifdef NO_CUSTOM_FONT
	//#else
	iformFont = FONT_CUSTOM1;
	//#endif	
	fontSet(iformFont);			
	its = printTextBreak(mainMenuItems,canvasWidth-8);	
	currentForm = FORM_MAIN_MENU;
	formPlainList(gameText[134][0], league.getRoundTitle(), its);	
	//formPlainMenu(loc[43], league.getRoundTitle(), mainMenuItems);

	
	showMenuBar = true;
	requestMenuBar = true;
	formWait(false);
	while (tries > 0){
		if (sellPlayerPointer >= 30) sellPlayerPointer -= 30;
		if (triedToSell[sellPlayerPointer] == 1
				&& league.userTeam.countPlayers() > 16
				//#ifndef DOJA
				//#ifndef NK-s40
				&& league.rand() % 128 >= 74
				//#endif
				//#endif
				//ZNR MARK 3
		)	{
			invokeTransfersOffer((short)sellPlayerPointer++);
			tries = 0;
			break;
		}		
		sellPlayerPointer++;
		tries--;
	}
	//System.out.println("END invokeMainMenu");	
}

private void actionMainMenu(int hiEvent) {
		
	//ZNR
	
	boolean hackMenuBar = true;
	
	int selectionTo = option;//hiEventSelectedItem(hiEvent);
	switch (MAIN_MENU_ICONS[selectionTo]) {
		case ICON_MATCH:
			invokeMatch();
		break;
		case ICON_TACTICS:
			//ZNR
			//==================================================================
			selectionTo = hiEvent;//hiEventSelectedItem(hiEvent);
			switch (suboption) {
				case 0:
					invokeTacticsFormation();
				break;
				case 1:
					invokeTacticsPosition(false);
				break;
				case 2:
					invokeGeneralTactic();
				break;
				/*case 3:
					invokeMainMenu();
				break;*/
			}
		break;
		case ICON_LEAGUE:
			//ZNR
			if (suboption == 0)
			{
				//canAccept = false;
				invokeClassification(false);
			}
			else if (suboption == 1)
			{
				//canAccept = false;
				invokeClassificationExtra(false);
			}
			//#ifndef NOCHAMPIONS
			else if (suboption == 2)			
				invokeClassChampions();
			//#endif
			//#ifndef NOTOPPLAYERS
			else if (suboption == 3)
			{
				//#ifndef NOPRESEASON	
				/*
				if (league.preSeasonPlaying)
				{
					formModalPopup(gameText[TEXT_SENSEI+4][4], COLOR_POPUP_GOOD);
					invokeMainMenu();						
				}
				else*/	
				//#endif
				invokeTopPlayers(false);
			}
			//#endif
			break;			
		case ICON_TRAINING:
			/*if (league.coachRemainingTrainings <= 0) {
				formModalPopup(gameText[52][0], COLOR_POPUP_BAD);
			} else {
				if (suboption == 0)					
					invokeTrainingChooseTraining(0);
				else
					invokeTrainingChooseTraining(1);
			}*/
			//invokeTrainingChooseTraining();//suboption);
			//===============================================
			formPlainMenu(gameText[107][0], gameText[108][0], gameText[105]);
			iformFont = iformFontDefaultSmall;
			currentForm = FORM_TRAINING_CHOOSE_TRAINING;
			requestMenuBar = true;
		break;
		case ICON_TRANSFERS:
			/*if (!hiringCalendarOK()) {
					String sb = gameText[269][0]+  spaceString + (league.transferRange(0)+1) + "-" + (league.transferRange(0)+5) + ", " +
					gameText[269][0]+  spaceString + (league.transferRange(1)+1) + "-" + (league.transferRange(1)+5) + ", " +
					gameText[269][0]+  spaceString + (league.transferRange(2)+1) + "-" + (league.transferRange(2)+5);
					formModalPopup(gameText[168][0] + spaceString + sb, COLOR_POPUP_GOOD);
			} else {*/
				//invokeTransfersMain();
				if (suboption == 0)
				{
					//actionTransfersBuy();
					//=======================================================
					//condiciones que permiten hire/fire
					if (league.userTeam.countPlayers() < 30) {
						if (league.availHires <= 0) {
							formModalPopup(gameText[59][0], COLOR_POPUP_BAD);
							invokeMainMenuReturn();
						} else {
							
							//#ifdef ADVANCEDSEARCH			
							invokeTransfersHireSelSearchMode();
							//#else
							invokeTransfersHireSelLeague();			
							//#endif
							
						}
					} else {
						formModalPopup(gameText[60][0], COLOR_POPUP_BAD);
						invokeMainMenuReturn();
					}

				}
				else
				{
					//actionTransfersSell();
					//////////////////////////////
					//if (league.userTeam.countPlayers() > 16) {
						invokeTransfersFireSelect();
					/*} else {
						formModalPopup(gameText[195][0], COLOR_POPUP_BAD);
					}*/
				}
			//}
		break;
		
//#ifndef NOFINANCES		
		case ICON_FINANCE:
			//SENSEI
			if (suboption == 0)
				invokeFinancesGeneral();
			else if (suboption == 1)
			{
				if(league.haveSponsor) invokeFinancesSponsor();			
				else 				   invokeFinancesNewSponsor();
			}
			break;
//#endif			
			
		case ICON_OPTIONS:
			if (suboption == 0)
				invokeHelp(true);
			//#ifndef PLAYER_NONE
			else if (suboption == 1)			
			{
				gameSound = !gameSound;
				if (gameSound) soundPlay(5, 1);
				
				hackMenuBar = false;				
			}
			else if (suboption == 2)
				invokeSave2();
			else
				invokeSave();
			//#else
			//#endif
			break;
				
	}
	
	if (selectionTo >= 0
		
			&& hackMenuBar
		
			) showMenuBar = false;
	//if (lastKeyMenu < 0) showMenuBar = true;
}

// ----------------------------------------------------------------------------
// league

//ZNR
/*
private void invokeLeague() {
	String[] leagueItems = { gameText[44][0] + " 1", gameText[44][0] + " 2", 
			//loc[45], 
			gameText[122][0]
			//, loc[1] 
			};
	formPlainMenu(gameText[39][0], league.getRoundTitle(), leagueItems);
	currentForm = FORM_LEAGUE;
	requestMenuBar = true;
}
*/
/*
private void actionLeague(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == 0) {
		invokeClassification(false);
	} else if (selectionTo == 1) {
		invokeClassificationExtra(false);
	} else if (selectionTo == 2) {
		/*invokeTopPlayers(false);
	} else if (selectionTo == 3) {
		invokeClassChampions();
	}
}
*/
// ----------------------------------------------------------------------------
// classification

private boolean classReturnToEndLeague = false;
private void invokeClassification(boolean b) {
	classReturnToEndLeague = b;
	//#ifdef LOWMEM
	//#endif
	
	String[] classificationLabels = { gameText[46][0], gameText[47][0], gameText[182][0], gameText[183][0], gameText[184][0] };
	// calcular items
	Team[] teams = league.classificationInfo();
	String[] st = new String[teams.length];
	String[] pt = new String[teams.length];
	String[] ppt = new String[teams.length];
	String[] wt = new String[teams.length];
	String[] dt = new String[teams.length];
	for (int i = 0; i < st.length; i++) {
		st[i] =  (i+1) + ". " + teams[i].name;
		pt[i] = "" + teams[i].points;
		ppt[i] = "" + teams[i].PJ;
		wt[i] = "" + teams[i].PG;
		dt[i] = "" + teams[i].PE;
		//gp[i] = "" + teams[i].realQuality();
	}
	/*st[teams.length] = b?gameText[TEXT_BACK][0]:gameText[TEXT_MENU][0];
	pt[teams.length] = "";
	ppt[teams.length] = "";
	wt[teams.length] = "";
	dt[teams.length] = "";*/
	String[][] its = new String[5][];
	its[0] = st;
	its[1] = pt;
	its[2] = ppt;
	its[3] = wt;
	its[4] = dt;
	tcpb = teams.length;
	if (classReturnToEndLeague) {
		formPlainMenu(gameText[44][0], gameText[94][0], its, classificationLabels);
	} else {
		formPlainMenu(gameText[44][0], league.getRoundTitle(), its, classificationLabels);
	}

	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CLASSIFICATION;
	
}

private Team focusedTeam = null;
private void actionClassification(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	//if (selectionTo == league.teams[league.playingLeague].length) {
	if (selectionTo >= 0) {
		if (classReturnToEndLeague) {
			invokeEndLeague();
		} else {
			// SENSEI
			//invokeLeague();			
			requestMenuBar = true;
			showMenuBar = true;
			//canAccept = true;
		}
		//ZNR return;
	}
	/*int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < league.teams[league.playingLeague].length) {
		focusedTeam = league.teams[league.playingLeague][focusTo];
		screenLost = true;
	} else {
		focusedTeam = null;
		screenLost = true;
	}*/
}

// ----------------------------------------------------------------------------
// classification extra

private void invokeClassificationExtra(boolean b) {
	classReturnToEndLeague = b;
	//#ifdef LOWMEM
	System.gc();
	//#endif
	String[] classificationLabels = { gameText[46][0], gameText[47][0], gameText[185][0], gameText[186][0], gameText[187][0] };
	// calcular items
	Team[] teams = league.classificationInfo();
	String[] st = new String[teams.length];
	String[] pt = new String[teams.length];
	String[] lt = new String[teams.length];
	String[] gpt = new String[teams.length];
	String[] glt = new String[teams.length];
	for (int i = 0; i < st.length; i++) {
		st[i] = "" + (i+1) + ". " + teams[i].name;
		pt[i] = "" + teams[i].points;
		lt[i] = "" + teams[i].PP;
		gpt[i] = "" + teams[i].posGoals;
		glt[i] = "" + teams[i].negGoals;
		//gp[i] = "" + teams[i].realQuality();
	}
	/*st[teams.length] = b?gameText[TEXT_BACK][0]:gameText[TEXT_MENU][0];
	//st[teams.length] = gameText[TEXT_BACK][0];
	pt[teams.length] = "";
	lt[teams.length] = "";
	gpt[teams.length] = "";
	glt[teams.length] = "";*/
	String[][] its = new String[5][];
	its[0] = st;
	its[1] = pt;
	its[2] = lt;
	its[3] = gpt;
	its[4] = glt;
	tcpb = teams.length;
	if (classReturnToEndLeague) {
		formPlainMenu(gameText[44][0], gameText[94][0], its, classificationLabels);
	} else {
		formPlainMenu(gameText[44][0], league.getRoundTitle(), its, classificationLabels);
	}
	
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CLASSIFICATION_EXTRA;
	
}

/*
private void actionClassificationExtra(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	//if (selectionTo == league.teams[league.playingLeague].length) {
	if (selectionTo >= 0) {
		if (classReturnToEndLeague) {
			invokeEndLeague();
		} else {
			// SENSEI
			//invokeLeague();			
			requestMenuBar = true;
			showMenuBar = true;
			canAccept = true;
		}
		return;
	}
}
*/
// ----------------------------------------------------------------------------
// top players

private int tpbv = 0;

//#ifndef NOTOPPLAYERS
private void invokeTopPlayers(boolean b) {
	//#ifndef NOFWAIT
	formWait(true);
	//#endif
	classReturnToEndLeague = b;
	String[] topPlayersLabels = { gameText[4][0], gameText[49][0] };
	// calcular items
	int i;
	for (i = 0; i < 20; i++) {
		if (league.top40Players[i] == -1) {
			break;
		}
	}
	int n = i;
	String[] st = new String[n];
	String[] pt = new String[n];
	for (i = 0; i < st.length; i++) {
		if (league.top40Players[i] == -1) {
			i++;
			break;
		}
		st[i] = "" + (i+1) + ". " + league.playerGetName(league.top40Players[i]) + " (" + league.teamForPlayer(league.top40Players[i]).name + ")";
		pt[i] = "" + league.playerGetGoals(league.top40Players[i]);
	}
	//st[i] = b?gameText[TEXT_BACK][0]:gameText[TEXT_MENU][0];
	//st[i] = gameText[TEXT_BACK][0];
	tpbv = i;
	//pt[i] = "";
	String[][] its = new String[2][];
	its[0] = st;
	its[1] = pt;

	
	if (i == 0)
	{
		String []tst = {gameText[TEXT_SENSEI+4][4]};
		String []tpt = {""};
		its[0] = tst;	
		its[1] = tpt;
		tpbv = 1;
	}
	
	
	if (classReturnToEndLeague) {
		formPlainMenu(gameText[45][0], gameText[94][0], its, topPlayersLabels);
	} else {
		formPlainMenu(gameText[45][0], league.getRoundTitle(), its, topPlayersLabels);
	}
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TOP_PLAYERS;
	//#ifndef NOFWAIT
	formWait(false);
	//#endif
	requestMenuBar = !classReturnToEndLeague;
}

/*
private void actionTopPlayers(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tpbv) {
	//if (selectionTo >= 0) {
		if (classReturnToEndLeague) {
			invokeEndLeague();
		} else {
			requestMenuBar = true;
			showMenuBar = true;
			//invokeMainMenu();//League(); ZNR
		}
		//return;
	}
}
*/
//#endif

// ----------------------------------------------------------------------------
// europe champions classification

//#ifndef NOCHAMPIONS
private void invokeClassChampions() {
	fontSet(iformFontDefaultSmall);
	String l = null;
	if (league.champTeams.length >= 2) {
		l = gameText[204][0] + spaceString + league.getChampJourneyNameProto(1);
	} else {
		l = "";
	}
	formPlainList(gameText[122][0], l, printTextBreak(league.getChampMatches(), canvasWidth - 8));
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_CLASS_CHAMPIONS;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO;
	requestMenuBar = true;
}
//#endif

/*
private void actionClassChampions(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	showMenuBar = true;
	//invokeLeague();
}
*/
// ----------------------------------------------------------------------------
// training - choose training

private int tcpb = -1;

private int tctb = -1;

private int trainingTable;
/*private void invokeTrainingChooseTraining(int _tt) {
	trainingTable = _tt;
		int numRows = 4;
	
	String[] line = {gameText[110][0],gameText[111][0],gameText[112][0],gameText[113][0]};//{"GK", "DEF","MID","ATT"};
	String[] timeRest = new String[numRows];
	String[] timeP1 = new String[numRows];
	String[] timeP2 = new String[numRows];
	String[] timeP3 = new String[numRows];
	String[] timeP4 = new String[numRows];
	String[] timeP5 = new String[numRows];
	String[] timeP6 = new String[numRows];
	
	
	for (int i = 0; i < numRows; i++) {		
		timeRest[i] = ""+trainingSchedule[i][0];
        timeP1[i] = ""+trainingSchedule[i][1];
        timeP2[i] = ""+trainingSchedule[i][2];
        timeP3[i] = ""+trainingSchedule[i][3];
        timeP4[i] = ""+trainingSchedule[i][4];
        timeP5[i] = ""+trainingSchedule[i][5];
        timeP6[i] = ""+trainingSchedule[i][6];        
	}
	
	String[][] its = new String[5][];
	its[0] = line;
	String titles[];
	if (trainingTable == 0)
	{
		its[1] = timeRest;
		its[2] = timeP1;
		its[3] = timeP2;
		its[4] = timeP3;
		titles = gameText[108];//new String []{"Line", "Rest", "ST", "BW", "5A"};
	}else{
		its[1] = timeRest;
		its[2] = timeP4;
		its[3] = timeP5;
		its[4] = timeP6;
		titles = gameText[109];//new String []{"Line", "Rest", "CR", "AD", "OO"};
	}
	
	
	formPlainMenu(gameText[107][0], "", its, titles);
	
	//System.out.println("5");
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRAINING_CHOOSE_TRAINING;
	requestMenuBar = true;
}*/

/*
private void invokeTrainingChooseTraining() {
	formPlainMenu(gameText[107][0], gameText[108][0], gameText[105]);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRAINING_CHOOSE_TRAINING;
	requestMenuBar = true;
}
*/

private int selectedTraining = -1;
/*
private void actionTrainingChooseTraining(int hiEvent) {	
	if (hiEvent == HIEVENT_IDLE)
		return;
	
	//System.out.println("actionTrainingChooseTraining");
	int selectionTo = hiEventSelectedItem(hiEvent);
	
	if (selectionTo >= 0) 
	{
		invokeTrainingChangeSchedule(selectionTo, 0);
	}
	
	
}
*/

private void actionTrainingChangeSchedule(int hiEvent) {	
	if (hiEvent == HIEVENT_IDLE)
		return;
	
	//System.out.println("actionTrainingChangeSchedule");
	
	int selectionTo = hiEventSelectedItem(hiEvent);
	
	if (selectionTo == 0) return;
	if (leftRight < 0)
	{
		//System.out.println("ddd1: "+selectionTo);		
		if (trainingSchedule[_line][selectionTo] > 0 && trainingSchedule[_line][0] < 99)
		{
			trainingSchedule[_line][selectionTo]--;
			trainingSchedule[_line][0]++;
			invokeTrainingChangeSchedule(_line, selectionTo);
		}
		
	}
	else if (leftRight > 0)
	{
		//System.out.println("ddd2: "+selectionTo);
		if (trainingSchedule[_line][0] > 0 && trainingSchedule[_line][selectionTo] < 99)
        {
					trainingSchedule[_line][selectionTo]++;
					trainingSchedule[_line][0]--;
					invokeTrainingChangeSchedule(_line, selectionTo);
        }
		//trainingSchedule[_line][selectionTo]++;
		
	}
	/*int selectionTo = hiEventSelectedItem(hiEvent);
	
	if (selectionTo >= 0) 
	{
		invokeTrainingChangeSchedule(selectionTo);
	}
	*/
	
	/*if (selectionTo == tctb) {
		invokeMainMenu();
		return;
	}*/
	/*if (selectionTo >= 0) {
		if (league.coachRemainingTrainings <= 0) {
			formModalPopup(loc[52], COLOR_POPUP_BAD);
			return;
		}
		selectedTraining = selectionTo;
		invokeTrainingChoosePlayers();
	}*/
}

int _line;
private void invokeTrainingChangeSchedule(int line, int op)
{
	prevOption = 0;
	_line = line;
	int trnLen = 7;
	String[] st = new String[trnLen];
	String[] str = new String[trnLen];
	String[] end = new String[trnLen];
	String[] spd = new String[trnLen];
	String[] kik = new String[trnLen];
	String[] port = new String[trnLen];
	
	int i, j;
	for (i = 0; i < trnLen; i++) {
		/*switch(i)
		{
			case 0: st[i] = "Rest";
				break;
			case 1: st[i] = "Stamina";
				break;
			case 2: st[i] = "Box work";
				break;
			case 3: st[i] = "5-a-side";
				break;
			case 4: st[i] = "Crossing";
				break;
			case 5: st[i] = "Advanced";
				break;
			case 6: st[i] = "One-on-one";
				break;
			
		}*/
	
		/*if (i == 0)
			st[i] = "Rest";
		else
			st[i] = "P"+(i);//loc[169+i];*/
		
		port[i] = ""+trainingSchedule[line][i];		
		j = league.allTrainings[i][1];
		str[i] = j > 0 ? "+" + j : "0"; 
		j = league.allTrainings[i][2];
		end[i] = j > 0 ? "+" + j : "0"; 
		j = league.allTrainings[i][3];
		spd[i] = j > 0 ? "+" + j : "0"; 
		j = league.allTrainings[i][4];
		kik[i] = j > 0 ? "+" + j : "0";
		if (j == 0) str[i] = end[i] = spd[i] = kik[i] = "-";							
	}
	st = gameText[106];
	
	String[][] its = new String[6][];
	its[0] = st;
	its[1] = port;
	its[2] = str;
	its[3] = end;
	its[4] = spd;
	its[5] = kik;
	String[] trainingLabels = { gameText[4][0], gameText[107][3], gameText[114+0][0], gameText[114+1][0], gameText[114+2][0], gameText[114+3][0] };

	formPlainMenu(gameText[107][1], gameText[105][line]+twoDotString+gameText[107][2], its, trainingLabels);
	
	iformSelectedItem = op;
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRAINING_CHANGE_SCHEDULE;
	requestMenuBar = true;
}





// ----------------------------------------------------------------------------
// training - choose players

/*
private int remainingPlayers;
private void invokeTrainingChoosePlayers() {
	
	//System.out.println("invokeTrainingChoosePlayers");
	
	// calcular items
	int m = league.userTeam.countPlayers();
	int n = m ;
	

	String[] st = buildSTPlayers(n, true);
	////tcpb = i;
	//tcpb = st.length-1;

	String[][] its = new String[6][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	its[3] = null;
	its[4] = null;
	its[5] = null;
	String[] playerListingLabels = { gameText[4][0], gameText[33][0], gameText[114+0][0], gameText[114+1][0], gameText[114+2][0], gameText[114+3][0] }; 
	formPlainMenu(gameText[53][0], gameText[169 + selectedTraining][0] + " - " + league.allTrainings[selectedTraining][0] + spaceString + gameText[51][0],
		its, playerListingLabels);
	setAttToUserTeamPlayerListing();
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRAINING_CHOOSE_PLAYERS;
}*/

private String[] res_st = null;
/*
private void actionTrainingChoosePlayers(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	//if (selectionTo == tcpb) {
	//	invokeTrainingChooseTraining();
	//	return;
	//}
	if (selectionTo >= 0) {
		if (league.playerIsInjured(league.userTeam.players[selectionTo])) {
			formModalPopup(league.playerGetName(league.userTeam.players[selectionTo]) + spaceString + gameText[54], COLOR_POPUP_BAD);
			return;
		}

		// uberhack, este si que mola: cambiar on the fly el array de atributos
		iformDataListItemsAttr[selectionTo] ^= MASK_PICKED;

		int nPicked = 0;
		for (int i = 0; i < iformDataListItemsAttr.length; i++) {
			if ((iformDataListItemsAttr[i] & MASK_PICKED) != 0)
				nPicked++;
		}

		int remaining = league.allTrainings[selectedTraining][0] - nPicked;
		if (remaining < 0) {
			remaining = 0;
		}
		// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
		iformDataHeaderSmall = gameText[169 + selectedTraining][0] + " - " + remaining + spaceString + gameText[51][0];
		screenLost = true;
		selectingPosition = true;

		if (remaining > 0)
			return;

		// se han seleccionado todos, construir el array para la pantalla siguiente
		// (aplicando los entrenamientos)
		int m = nPicked*2 + 1;
		int n = m + 1;
		res_st = new String[m];
		int j = 0;
		for (int i = 0; i < iformDataListItemsAttr.length; i++) {
			if ((iformDataListItemsAttr[i] & MASK_PICKED) == 0)
				continue;
			res_st[j] = barString + gameText[236][0] + spaceString + buildPlayerItem(i);
			for (int h = 0; h < league.allTrainings[selectedTraining].length - 1; h++) {
				league.playerIncStat(league.userTeam.players[i], h,
					league.allTrainings[selectedTraining][h+1]);
			}

			res_st[j+1] =barString + gameText[237][0] + spaceString + buildPlayerItem(i);
			j += 2;
		}
		res_st[j] = barString + gameText[TEXT_BACK][0] + "||||||";
		tcpb = j;

		invokeTrainingResult();
		league.coachRemainingTrainings--;
		league.userTeam.markDirtyQuality();
	}
}*/

// ----------------------------------------------------------------------------
// training - result
/*
private void invokeTrainingResult() {
	String[][] its = new String[6][];
	its[0] = res_st;
	its[1] = null;
	its[2] = null;
	its[3] = null;
	its[4] = null;
	its[5] = null;

	String[] playerListingLabels = { gameText[4][0], gameText[33][0], gameText[114+0][0], 
			gameText[114+1][0], gameText[114+2][0], gameText[114+3][0] };
	formPlainMenu(gameText[55][0], gameText[169 + selectedTraining][0],
		its, playerListingLabels);
	iformDataListItemsAttr = null;
	byte[] att = new byte[iformDataListItems[0].length];
	for (int i = 0; i < att.length; i++) {
		att[i] = (byte)((i & 1) == 1 ? MASK_PICKED : 0);
	}
	iformDataListItemsAttr = att;

	iformFont = iformFontDB;
	currentForm = FORM_TRAINING_RESULT;
}
*/
/*
private void actionTrainingResult(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	//if (selectionTo == tcpb) {
	if (selectionTo >= 0) {
		res_st = null;
		if (league.coachRemainingTrainings <= 0) {
			invokeMainMenu();
		} else {
			invokeTrainingChooseTraining(0);
		}
	}
}
*/
// ----------------------------------------------------------------------------
// transfers - main

/*
private void invokeTransfersMain() {
	String[] transfersMainItems = { loc[56], loc[57], loc[1] };
	formPlainMenu(loc[41], loc[205] + ": " + League.cashStr(league.coachCash) + " " + loc[188], transfersMainItems);
	currentForm = FORM_TRANSFERS_MAIN;
}
*/
/*
private boolean hiringCalendarOK() {
	return (league.journeyNumber >= league.transferRange(0) &&
	league.journeyNumber < (league.transferRange(0) + 5)) ||
	// temporada 2
	(league.journeyNumber >= league.transferRange(1) &&
	league.journeyNumber < (league.transferRange(1) + 5)) ||
	// temporada 3
	(league.journeyNumber >= league.transferRange(2) &&
	league.journeyNumber < (league.transferRange(2) + 5));
}
*/
/*
private void actionTransfersBuy()
{
	// condiciones que permiten hire/fire
	if (league.userTeam.countPlayers() < 30) {
		if (league.availHires <= 0) {
			formModalPopup(gameText[59][0], COLOR_POPUP_BAD);
			invokeMainMenuReturn();
		} else {
			
//#ifdef ADVANCEDSEARCH			
			invokeTransfersHireSelSearchMode();
//#else
			invokeTransfersHireSelLeague();			
//#endif
			
		}
	} else {
		formModalPopup(gameText[60][0], COLOR_POPUP_BAD);
		invokeMainMenuReturn();
	}
}
*/
private void actionTransfersSell()
{
	// condiciones que permiten hire/fire
	//if (league.coachRemainingFires > 0) {
		if (league.userTeam.countPlayers() > 16) {
			invokeTransfersFireSelect();
		} else {
			formModalPopup(gameText[195][0], COLOR_POPUP_BAD);
		}
	/*} else {					
		formModalPopup(loc[61], COLOR_POPUP_BAD);
	}*/

}

//ZNR

//private void actionTransfersMain(int hiEvent) {
/*	int selectionTo = hiEventSelectedItem(hiEvent);
	switch (selectionTo) {
		case 0:
			// condiciones que permiten hire/fire
			if (league.userTeam.countPlayers() < 30) {
				if (league.availHires <= 0) {
					formModalPopup(loc[59], COLOR_POPUP_BAD);
				} else {
					invokeTransfersHireSelLeague();
				}
			} else {
				formModalPopup(loc[60], COLOR_POPUP_BAD);
			}
		break;
		case 1:
			// condiciones que permiten hire/fire
			if (league.coachRemainingFires > 0) {
				if (league.userTeam.countPlayers() > 16) {
					invokeTransfersFireSelect();
				} else {
					formModalPopup(loc[195], COLOR_POPUP_BAD);
				}
			} else {					
				formModalPopup(loc[61], COLOR_POPUP_BAD);
			}
		break;
		case 2:
			invokeMainMenu();
		break;
	}
	*/
//}

// ----------------------------------------------------------------------------
// transfers - hire - choose league

private void invokeTransfersHireSelLeague() {
	chooseLeagueItems = new String[6];
	for (int i = 0; i < 6; i++) {
		chooseLeagueItems[i] = gameText[95+i][0];
	}
	//chooseLeagueItems[6] = gameText[TEXT_BACK][0];
	formPlainMenu(gameText[31][0], gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + spaceString + gameText[188][0], chooseLeagueItems);
	currentForm = FORM_TRANSFERS_HIRE_SEL_LEAGUE;
}

private int hiringLeague;
/*
private void actionTransfersHireSelLeague(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == 6) {
fsdfsdfsdfd
		//invokeTransfersMain();
	} else if (selectionTo >= 0 && selectionTo <= 5) {
		hiringLeague = selectionTo;
		invokeTransfersHireSelTeam(selectionTo);
	}// else if (selectionTo == 5) {
	//	hiringLeague = 5;
	//	hiringTeam = league.extraTeam;
	//	invokeTransfersHireSelPlayer(hiringTeam);
	//}
}
*/
// ----------------------------------------------------------------------------
// transfers - hire - choose team

private Team[] teamListing;
private void invokeTransfersHireSelTeam(int selectionTo) {
	String[] chooseTeamLabels = { gameText[32][0], gameText[33][0] };
	int n = League.leagueTeams[selectionTo];
	if (selectionTo == league.playingLeague) {
		n--;
	}
	chooseTeamItems = new String[n];
	teamListing = new Team[n];
	for (int i = 0, j = 0; i < League.leagueTeams[selectionTo]; i++) {
		if (league.teams[selectionTo][i] != league.userTeam) {
			chooseTeamItems[j] = league.teams[selectionTo][i].name;
			teamListing[j] = league.teams[selectionTo][i];
			j++;
		}
	}
	//chooseTeamItems[n] = gameText[TEXT_BACK][0];
	//formPlainMenu(loc[34], loc[95+selectionTo], its, chooseTeamLabels);
	formPlainMenu(gameText[34][0], gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + spaceString + gameText[188][0], chooseTeamItems);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRANSFERS_HIRE_SEL_TEAM;
}

/*
private void actionTransfersHireSelTeam(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == chooseTeamItems.length - 1) {
		invokeTransfersHireSelLeague();
	} else if (selectionTo >= 0) {
		invokeTransfersHireSelPlayer(teamListing[selectionTo]);
		teamListing = null;
	}
}
*/
// ----------------------------------------------------------------------------
// transfers - hire - choose player

private Team hiringTeam;
private void invokeTransfersHireSelPlayer(Team team) {
	hiringTeam = team;
	// calcular items
	int m = hiringTeam.countPlayers();
	int n = m ;
	String[] st = new String[n];
	//String[] pos = null;//new String[n];
	//String[] qa = null;//new String[n];
	//String[] val = null;//new String[n];
	int i;
	for (i = 0; i < st.length; i++) {
		st[i] = barString + league.playerGetName(hiringTeam.players[i]) +
		barString + league.playerGetSpecAbbr(hiringTeam.players[i]) +
		barString + league.playerBalance(hiringTeam.players[i], -1) +
		barString + League.cashStr(league.playerValue(hiringTeam.players[i])) + barString;
	}
	//st[i] = barString + gameText[TEXT_BACK][0] + "||||";
	//pos[i] = "";
	//qa[i] = "";
	//val[i] = "";
	tcpb = i;
	String[][] its = new String[4][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	its[3] = null;
	String[] playerListingLabels = { gameText[4][0], gameText[33][0], gameText[194][0], gameText[190][0] };
	//formPlainMenu(loc[56], loc[62], its, playerListingLabels);
	formPlainMenu(gameText[56][0], gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + gameText[188][0], its, playerListingLabels);
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRANSFERS_HIRE_SEL_PLAYER;
}
/*
private void actionTransfersHireSelPlayer(int hiEvent) {
	System.out.println("IDLE");
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	System.out.println("ENTROOOO");
	if (selectionTo == tcpb) {
		//if (hiringLeague == 5) {
		//	invokeTransfersHireSelLeague();
		//} else {
			invokeTransfersHireSelTeam(hiringLeague);
		//}
		return;
	}
	if (selectionTo >= 0) {
		short p = hiringTeam.players[selectionTo];
		Team t = hiringTeam;
		int v = league.playerValue(p);
		if (t.countPlayers() <= 16) {
			formModalPopup(gameText[238][0], COLOR_POPUP_BAD);
			return;
		}
		if (league.coachCash < v) {
			formModalPopup(gameText[191][0], COLOR_POPUP_BAD);
			return;
		}
		int userQuality = league.userTeam.realQuality();
		//int otherQuality = league.playerBalance(p, -1);
		int otherQuality = t.realQuality();
		int diff = otherQuality - userQuality;
		if (diff > 7) {
			formModalPopup(gameText[192][0], COLOR_POPUP_BAD);
			return;
		}
		invokeTransfersHireConfirm(p);

	}
}*/

// ----------------------------------------------------------------------------
// transfers - confirm hire

private short hiringPlayer;
private void invokeTransfersHireConfirm(short player) {
	hiringPlayer = player;
	fontSet(iformFontDB);
	//formYesNoList(
	//#ifdef DOJA
	//#else
	String sadd = "";
	if (gameText[207].length > 1) sadd = gameText[207][1];		
		
		formPlainListYesNo(gameText[56][0], gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + gameText[188][0], gameText[3][0], gameText[2][0],
				printTextBreak(gameText[206][0] + spaceString + league.playerGetName(player) +
					" (" + hiringTeam.name + ") " + spaceString + gameText[207][0] + spaceString + 
					League.cashStr(league.playerValue(player)) +
					spaceString + gameText[188][0]+sadd+gameText[262][1], canvasWidth - 8));
	//#endif
	iformFont = iformFontDB;
	currentForm = FORM_TRANSFERS_HIRE_CONFIRM;
}


private void actionTransfersHireConfirm(int hiEvent) {
	if (hiEvent == HIEVENT_NO) {
		invokeTransfersHireSelPlayer(hiringTeam);
		return;
	}
	if (hiEvent == HIEVENT_YES) {
		short p = hiringPlayer;
		Team t = hiringTeam;
		int v = league.playerValue(p);
		t.removePlayer(p);
		league.userTeam.addPlayer(p);
		league.availHires--;
		league.coachCash -= v;
		
		// SENSEI
		//#ifndef NOFINANCES
		league.journeyExpenses += v;
		//#endif
		
		league.userTeam.markDirtyQuality();
		//invokeTransfersMain();
		invokeMainMenuReturn();
		formModalPopup(gameText[63][0] + spaceString + league.playerGetName(p) + spaceString + gameText[64][0] + spaceString + t.name, COLOR_POPUP_GOOD);
	}
}

// ----------------------------------------------------------------------------
// transfers - fire select

private void invokeTransfersFireSelect() {
	// calcular items
	int m = league.userTeam.countPlayers();
	//   int n = m + 1;
	String[] st = new String[m];
	
	//String[] pos = null;//new String[n];
	//String[] qa = null;//new String[n];
	//String[] val = null;//new String[n];
	int i;
	//   for (i = 0; i < st.length-1; i++) {
	for (i = 0; i < st.length; i++) {
		st[i] = barString + league.playerGetName(league.userTeam.players[i]) +
		barString + league.playerGetSpecAbbr(league.userTeam.players[i]) +
		barString + league.playerBalance(league.userTeam.players[i], -1) +
		barString + League.cashStr(league.playerValue(league.userTeam.players[i])) + barString;
	}
	//st[i] = "|" + loc[1] + "||||";
	//   st[i] = barString + gameText[TEXT_BACK][0] + "||||";
	
	
	
	
	//pos[i] = "";
	//qa[i] = "";
	//val[i] = "";
	tcpb = i;
	String[][] its = new String[4][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	its[3] = null;
	String[] playerListingLabels = { gameText[4][0], gameText[33][0], gameText[194][0], gameText[190][0] };
	formPlainMenu(gameText[57][0], gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + spaceString + gameText[188][0], its, playerListingLabels);
	
	iformDataListItemsAttr = null;
	//   byte[] att = new byte[st.length-1];
	byte[] att = new byte[st.length];
	for (int j = 0; j < att.length; j++) {
		if (j >= 30)
			break;
		att[j] = (byte)(triedToSell[j]*4);
		
	}
	iformDataListItemsAttr = att;
	
	iformFont = iformFontDefaultSmall;
	currentForm = FORM_TRANSFERS_FIRE_SELECT;
	requestMenuBar = true;
}

private void actionTransfersFireSelect(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	/*if (selectionTo == tcpb) {
		invokeTransfersMain();
		return;
	}*/
	if (selectionTo >= 0) 
	{
		short p = league.userTeam.players[selectionTo];
	
		
		
		if (triedToSell[selectionTo] == 0)
		{
			triedToSell[selectionTo] = 1;
			
			//triedToSell[player] = league.offertedPlayerValue(player);
		}
		else
		{
			triedToSell[selectionTo] = 0;
			//formModalPopup("No vendiendo: "+ league.playerGetName(p), COLOR_POPUP_BAD);
		}
		formModalPopup(gameText[61][triedToSell[selectionTo]] + league.playerGetName(p), COLOR_POPUP_BAD);
		
		invokeTransfersFireSelect();
		requestMenuBar = true;
		
		//invokeTransfersFireConfirm(p);
		
		
		/*int otherQuality = league.playerBalance(p, -1);
		Team t = league.userTeam;
		while (t == league.userTeam)
			t = league.vacantTeamQA(otherQuality - 30);
		league.coachCash += league.playerValue(p);
		league.userTeam.removePlayer(p);
		t.addPlayer(p);
		league.userTeam.markDirtyQuality();
		invokeTransfersMain();
		formModalPopup(loc[66] + " " + league.playerGetName(p) +
			". " + loc[67] + " " + t.name , COLOR_POPUP_GOOD);
		league.coachRemainingFires--;*/
	}
}


// ----------------------------------------------------------------------------
// transfers - confirm fire

private short firingPlayer;
static int triedToSell[] = new int[30];

/*
private void invokeTransfersFireConfirm(short player) {
	
	firingPlayer = player;
	fontSet(iformFontDB);
	formPlainListYesNo(		gameText[57][0], 
							gameText[205][0] + ": " + League.cashStr(league.coachCash) + " " + gameText[188][0], 
							gameText[3][0], 
							gameText[2][0],
							
							printTextBreak(gameText[262][0] + " " + league.playerGetName(player) + " " + gameText[207][0] + " " +
										League.cashStr(triedToSell[player]) + " " + gameText[188][0],
										canvasWidth - 8));
	
	iformFont = iformFontDB;
	currentForm = FORM_TRANSFERS_FIRE_CONFIRM;
}


private void actionTransfersFireConfirm(int hiEvent) {
	if (hiEvent == HIEVENT_NO) {
		invokeTransfersFireSelect();
		return;
	}
	if (hiEvent == HIEVENT_YES) {
		short p = firingPlayer;
		int otherQuality = league.playerBalance(p, -1);
		Team t = league.userTeam;
		while (t == league.userTeam)
			t = league.vacantTeamQA(otherQuality - 30);
		league.coachCash += league.offertedPlayerValue;

		
		// SENSEI
		league.journeyIncome += league.offertedPlayerValue;
		
		league.userTeam.removePlayer(p);
		t.addPlayer(p);
		league.userTeam.markDirtyQuality();
		////t.markDirtyQuality();
		//invokeTransfersMain();
		invokeMainMenu();
		formModalPopup(gameText[66][0] + " " + league.playerGetName(p) +
			". " + gameText[67][0] + " " + t.name , COLOR_POPUP_GOOD);
		league.coachRemainingFires--;

		// poner a 0 los goles y reordenar top players, siempre y cueando el equipo sea
		// de otra liga
		if (league.userTeam.loadLeague != t.loadLeague) {
			league.playerSetGoals(p, 0);
			league.bubbleSortTop40();
		}
	
	}
}
*/

// ----------------------------------------------------------------------------
// tactics - main
/*
private void invokeTacticsMain() {
	String[] tacticsMainItems = { null, loc[69], null, loc[1] };
	tacticsMainItems[0] = loc[68] + ": " + league.formationsNames[league.coachFormation];
	String s = loc[162];
	if (league.coachGeneralTactic == 1) {
		s = loc[163];
	}
	if (league.coachGeneralTactic == 2) {
		s = loc[164];
	}
	tacticsMainItems[2] = loc[161] + ": " + s;
	//ZNR
	// TO DO: lo de arriba es mas que inutil
	//formPlainMenu(loc[38], league.getRoundTitle(), tacticsMainItems);
	//currentForm = FORM_TACTICS_MAIN;
}*/

private void actionTacticsMain(int hiEvent) {
	int selectionTo = hiEvent;//hiEventSelectedItem(hiEvent);
	switch (selectionTo) {
		case 0:
			invokeTacticsFormation();
		break;
		case 1:
			invokeTacticsPosition(false);
		break;
		case 2:
			invokeGeneralTactic();
			/*league.coachGeneralTactic++;
			if (league.coachGeneralTactic >= 3) {
				league.coachGeneralTactic = 0;
			}
			invokeTacticsMain();
			iformSelectedItem = 2;*/
		break;
		case 3:
			invokeMainMenu();
		break;
	}
}

// ----------------------------------------------------------------------------
// tactics - formation

private void invokeTacticsFormation() {
	//String[] formationItems = new String[league.formationsNames.length];//+1
	//System.arraycopy(league.formationsNames, 0, formationItems, 0, league.formationsNames.length);
	////formationItems[league.formationsNames.length] = loc[1];
	tcpb = gameText[104].length;//league.formationsNames.length;
	formPlainMenu(gameText[68][0], league.getRoundTitle(), gameText[104]);
	iformFont = iformFontDefaultSmall;
	iformListStartY = iformTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	focusedFormation = 0;
	focusedPlayer = -1;
	currentForm = FORM_TACTICS_FORMATION;
	prevOption = league.coachFormation;
}

private int focusedFormation;
private int focusedPlayer;
private void actionTacticsFormation(int hiEvent) {
	//ZNR
	int selectionTo = hiEventSelectedItem(hiEvent);
	/*if (selectionTo == tcpb) {
		invokeTacticsMain();
		return;
	}*/
	if (selectionTo >= 0) {
		league.coachFormation = selectionTo;
		showMenuBar = true;	
		prevOption = league.coachFormation;
		screenLost = true;
		/*invokeTacticsMain();
		return;*/		
	}
	int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < gameText[104].length){//league.formationsNames.length) {
		focusedFormation = focusTo;
		screenLost = true;
	} else if (focusTo == gameText[104].length){//league.formationsNames.length) {
		focusedFormation = league.coachFormation;
		screenLost = true;
	}
	//System.out.println(lastKeyMenu+" - "+intKeyMenu);
	//if (lastKeyMenu > 0) showMenuBar = true;
}


private void actionGeneralTactic(int hiEvent) {
	//ZNR
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo >= 0) {
		league.coachGeneralTactic = selectionTo;
		prevOption = league.coachGeneralTactic;
		showMenuBar = true;
		//prevOption = -1;
		screenLost = true;
	}
	/*int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < league.formationsNames.length) {
		focusedFormation = focusTo;
		System.out.println("opppp 1");
		screenLost = true;
	} else if (focusTo == league.formationsNames.length) {
		focusedFormation = league.coachFormation;
		screenLost = true;
		System.out.println("opppp 1");
	}*/
	//System.out.println(lastKeyMenu+" - "+intKeyMenu);
	//if (lastKeyMenu > 0) showMenuBar = true;
}



// ----------------------------------------------------------------------------
// tactics - position

private String buildPlayerItem(int i) {
	return league.playerGetName(league.userTeam.players[i]) + spaceString + //loc[236] +
		barString + league.playerGetSpecAbbr(league.userTeam.players[i]) +
		barString + league.playerGetStat(league.userTeam.players[i], 0) +
		barString + league.playerGetStat(league.userTeam.players[i], 1) +
		barString + league.playerGetStat(league.userTeam.players[i], 2) +
		barString + league.playerGetStat(league.userTeam.players[i], 3) + barString;
}

private String[] buildSTPlayers(int n, boolean posFlags) {
	
	String[] st = new String[n];
	
	int i;
	for (i = 0; i < st.length; i++) {
		String d = barString;
		if (posFlags) {
			if (i >= 11 && i <= 15) {
				d = "|(S) ";
			} else if (i >= 16) {
				d = "|(R) ";
			}
		}
		st[i] = d + buildPlayerItem(i);		
	}
	
	
	/*
	String[] st = new String[n + 1];
	int i;
	for (i = 0; i < st.length - 1; i++) {
		String d = barString;
		if (posFlags) {
			if (i >= 11 && i <= 15) {
				d = "|(S) ";
			} else if (i >= 16) {
				d = "|(R) ";
			}
		}
		st[i] = d + buildPlayerItem(i);		
	}
	st[i] = barString + gameText[TEXT_BACK][0] + "||||||";
	*/
	return st;
}

int prevOption = -1;

private void invokeGeneralTactic() {
	
	//mpScoreLeft = league.journeyMatch[0].matchGoals;
	//mpScoreRight = league.journeyMatch[1].matchGoals;
	//String[] it = new String[] { loc[162], loc[163], loc[164] };
	tcpb = 3;
	fontSet(iformFontDefault);
	formPlainMenu(gameText[0][0], null, gameText[162]);
	iformFont = iformFontDefault;
	mpEventsText = new String[0];
	currentForm = FORM_GENERAL_TACTIC;
	
	prevOption = league.coachGeneralTactic;
	
	/*iformDataHeaderTop = null;
	iformDataHeaderSmall = null;
	iformDataListItems = null;
	iformDataListColumnNames = null;
	iformDataListItemsAttr = null;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	pickInMatch = inGame;
	System.gc();
	int m = league.userTeam.countPlayers();
	if (pickInMatch) {
		m = m < 16 ? m : 16;
	}
	int n = m + 1;
	String[] st = buildSTPlayers(n, true);
	//tcpb = i;
	tcpb = st.length-1;
	String[][] its = new String[6][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	its[3] = null;
	its[4] = null;
	its[5] = null;

	String[] playerListingLabels = { loc[4], loc[33], loc[114+0], loc[114+1], loc[114+2], loc[114+3] };

	if (pickInMatch) {
		formPlainMenu(loc[165], remainingMatchChanges + " " + loc[166], its, playerListingLabels);
	} else {
		formPlainMenu(loc[69], loc[70], its, playerListingLabels);
	}
	if (inGame) {
		setAttToUserTeamPlayerListing(16);
	} else {
		setAttToUserTeamPlayerListing();
	}
	iformFont = iformFontDefaultSmall;
	if (altTopBackground != null && pickInMatch) {
		iformListStartY = altTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	} else {
		iformListStartY = iformTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	}
	focusedFormation = league.coachFormation;
	focusedPlayer = 0;
	currentForm = FORM_TACTICS_POSITION;
	selectingPosition = false;*/
}






private void invokeTacticsPosition(boolean inGame) {
	iformDataHeaderTop = null;
	iformDataHeaderSmall = null;
	iformDataListItems = null;
	iformDataListColumnNames = null;
	iformDataListItemsAttr = null;
	iformDataSoftLeft = null;
	iformDataSoftRight = null;
	pickInMatch = inGame;
	System.gc();
	int m = league.userTeam.countPlayers();
	if (pickInMatch) {
		m = m < 16 ? m : 16;
	}
	int n = m ;//+ 1;
	/*String[] st = new String[n];
	int i;
	for (i = 0; i < st.length-1; i++) {
		String d = "";
		if (i >= 11 && i <= 15) {
			d = "(S) ";
		} else if (i >= 16) {
			d = "(R) ";
		}
		st[i] = "|" + d + league.playerGetName(league.userTeam.players[i]) + 
			"|" + league.playerGetSpecAbbr(league.userTeam.players[i]) +
			"|" + league.playerGetStat(league.userTeam.players[i], 0) +
			"|" + league.playerGetStat(league.userTeam.players[i], 1) +
			"|" + league.playerGetStat(league.userTeam.players[i], 2) +
			"|" + league.playerGetStat(league.userTeam.players[i], 3) + "|";
	}
	st[i] = "|" + loc[1] + "||||||";*/
	String[] st = buildSTPlayers(n, true);
	//tcpb = i;
	//if (!inGame) 
		tcpb = st.length;
	//else tcpb = -2;//st.length
	String[][] its = new String[6][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	its[3] = null;
	its[4] = null;
	its[5] = null;

	String[] playerListingLabels = { gameText[4][0], gameText[33][0], gameText[114+0][0], gameText[114+1][0], gameText[114+2][0], gameText[114+3][0] };

	if (pickInMatch) {
		formPlainMenu(gameText[165][0], remainingMatchChanges + spaceString + gameText[166][0], its, playerListingLabels);
	} else {
		formPlainMenu(gameText[69][0], gameText[70][0], its, playerListingLabels);
	}
	if (inGame) {
		setAttToUserTeamPlayerListing(16);
	} else {
		requestMenuBar = true;
		setAttToUserTeamPlayerListing();
	}
	iformFont = iformFontDefaultSmall;
	if (altTopBackground != null && pickInMatch) {
		iformListStartY = altTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	} else {
		iformListStartY = iformTopBackground.getHeight() + fieldImage.getHeight() + 3 + iformHeaderSeparatorMargin;
	}
	focusedFormation = league.coachFormation;
	focusedPlayer = 0;
	currentForm = FORM_TACTICS_POSITION;
	selectingPosition = false;
}

private void setAttToUserTeamPlayerListing() {
	setAttToUserTeamPlayerListing(30);
}

private void setAttToUserTeamPlayerListing(int t) {
	iformDataListItemsAttr = null;
	byte[] att = new byte[iformDataListItems[0].length];
	for (int i = 0; i < att.length; i++) {
		if (i >= 30 || i >= t)
			break;
		att[i] = league.playerGetTextAtt(league.userTeam.players[i]);
	}
	iformDataListItemsAttr = att;
}

private boolean selectingPosition;
private boolean pickInMatch;
private int pickedPlayer;
private int remainingMatchChanges;
private void actionTacticsPosition(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	//int selectionTo = tcpb;
	//SUPER MEGA HACK VERGONZOSO
	/*if (hiEvent != -222)
		selectionTo = hiEventSelectedItem(hiEvent);*/
	//ZNR
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tcpb) {
		if (pickInMatch) {
			pauseMatch = false;
			resumeMatch();
			pickInMatch = false;
		} else {
			//invokeTacticsMain();
			showMenuBar = true;
			screenLost = true;
		}
		return;
	}
	int focusTo = hiEventFocusedItem(hiEvent);
	if (focusTo >= 0 && focusTo < tcpb) {
		focusedPlayer = focusTo;
		screenLost = true;
	} else if (focusTo == tcpb) {
		focusedPlayer = -1;
		screenLost = true;
	}
	if (selectionTo >= 0) {
		if (!selectingPosition) {
			// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
			iformDataHeaderSmall = gameText[71][0] + spaceString + league.playerGetName(league.userTeam.players[selectionTo]);
			// uberhack, este si que mola: cambiar on the fly el array de atributos
			//setAttToUserTeamPlayerListing();
			if (pickInMatch) {
				setAttToUserTeamPlayerListing(16);
			} else {
				setAttToUserTeamPlayerListing();
			}
			iformDataListItemsAttr[selectionTo] |= MASK_PICKED;
			screenLost = true;
			selectingPosition = true;
			pickedPlayer = selectionTo;
		} else {
			if (pickInMatch) {
				//setAttToUserTeamPlayerListing();
				setAttToUserTeamPlayerListing(16);
				int playerFrom = league.userTeam.players[pickedPlayer];
				int playerTo = league.userTeam.players[selectionTo];
				String playerFromName = league.playerGetName(playerFrom);
				String playerToName = league.playerGetName(playerTo);

				// calcular si es un cambio y si aun quedan cambios
				boolean overflow = false;
				boolean benchToPlay = false;
				if ((selectionTo < 11 && pickedPlayer >= 11) || (pickedPlayer < 11 && selectionTo >= 11)) {
					if (remainingMatchChanges > 0) {
						benchToPlay = true;
					} else {
						overflow = true;
					}
				}

				String playerFromPenal = null;
				String playerToPenal = null;

				//#ifndef REMOVE_FEATURES
				// calcular penalizacion
				int spPlayer1 = league.playerGetSpec(playerFrom);
				int spCoach1 = league.formationsSpec[league.coachFormation][selectionTo];
				int penal1 = 100;
				if (spCoach1 != 64 && spCoach1 != 96) {
					penal1 = league.positionPenalization[spPlayer1][spCoach1];
				}

				int spPlayer2 = league.playerGetSpec(playerTo);
				int spCoach2 = league.formationsSpec[league.coachFormation][pickedPlayer];
				int penal2 = 100;
				if (spCoach2 != 64 && spCoach2 != 96) {
					penal2 = league.positionPenalization[spPlayer2][spCoach2];
				}

				// calcular penalizaciones por posicion
				playerFromPenal = null;
				if (penal1 < 100) {
					playerFromPenal = gameText[74][0] + spaceString + penal1 + "% " + gameText[75][0] + " (" + playerFromName + ")";
				}
				if (penal2 < 100) {
					playerFromPenal = gameText[74][0] + spaceString + penal2 + "% " + gameText[75][0] + " (" + playerToName + ")";
				}
				if (selectionTo < 11 && league.playerIsInjured(playerFrom) && spPlayer1 != 96) {
					playerFromPenal = playerFromName + spaceString + gameText[72][0];
				}
				if (selectionTo < 11 && league.playerIsSanctioned(playerFrom) && spPlayer1 != 96) {
					playerFromPenal = playerFromName + spaceString + gameText[73][0];
				}
				playerToPenal = null;
				if (selectionTo < 11 && league.playerIsInjured(playerTo) && spPlayer1 != 96) {
					playerToPenal = playerToName + spaceString + gameText[72][0];
				}
				if (selectionTo < 11 && league.playerIsSanctioned(playerTo) && spPlayer1 != 96) {
					playerToPenal = playerToName + spaceString + gameText[73][0];
				}
				//#endif

				// calcular si se quiere cambiar un sancionado
				boolean sanctionedToBench = false;
				if (league.playerIsSanctioned(playerFrom) || league.playerIsSanctioned(playerTo)) {
					// caso 1: al banquillo
					if (benchToPlay) {
						sanctionedToBench = true;
						//formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + "is sanctioned and cannot be swaped", COLOR_POPUP_BAD);
					}
				}

				/*boolean overflow
				boolean benchToPlay
				String playerFromPenal
				String playerToPenal
				boolean sanctionedToBench*/
				// prioridad 1: demasiados cambios
				if (overflow) {
					formModalPopup(gameText[167][0], COLOR_POPUP_BAD);
				}
				// prioridad 2: sancionados al banquillo
				else if (sanctionedToBench) {
					if (league.playerIsSanctioned(playerFrom)) {
						formModalPopup(playerFromName + spaceString + gameText[239][0], COLOR_POPUP_BAD);
					} else {
						formModalPopup(playerToName + spaceString + gameText[239][0], COLOR_POPUP_BAD);
					}
				}
				// prioridad 3: cambio de banquillo
				else if (benchToPlay) {
					selectingPosition = false;
					invokeTacticsPositionConfirm(pickedPlayer, selectionTo, iformScrollTopItem, iformSelectedItem);
					return;
				}
				// prioridad 4: cambio normal 
				else {
					league.userTeam.swapOrdinalToOrdinal(pickedPlayer, selectionTo);
					if (playerFromPenal != null) {
						formModalPopup(playerFromPenal, COLOR_POPUP_BAD);
					} else if (playerToPenal != null) {
						formModalPopup(playerToPenal, COLOR_POPUP_BAD);
					}
				}
				selectingPosition = false;
				pickedPlayer = -1;
				int t1 = iformScrollTopItem;
				int t2 = iformSelectedItem;
				invokeTacticsPosition(true);
				// hack hack haslkjdlskjd blaeh
				iformScrollTopItem = t1;
				iformSelectedItem = t2;
				focusedPlayer = selectionTo;
				// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
				iformDataHeaderSmall = remainingMatchChanges + spaceString + gameText[166][0];
			} else {
				/*
				// HACK HACK HACK HACK, pero lo simplifica todo MUCHO
				iformDataHeaderSmall = loc[70];
				setAttToUserTeamPlayerListing();
				int spPlayer = league.playerGetSpec(league.userTeam.players[pickedPlayer]);
				int sp = league.formationsSpec[league.coachFormation][selectionTo];
				int ref = 100;
				if (sp != 64 && sp != 96) {
					ref = league.positionPenalization[spPlayer][sp];
				}
				if (league.playerIsInjured(league.userTeam.players[pickedPlayer]) && sp != 96) {
					formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + loc[72], COLOR_POPUP_BAD);
				} else if (league.playerIsSanctioned(league.userTeam.players[pickedPlayer]) && sp != 96) {
					formModalPopup(league.playerGetName(league.userTeam.players[pickedPlayer]) + " " + loc[73], COLOR_POPUP_BAD);
				} else if (ref < 100) {
					formModalPopup(loc[74] + " " + ref + "% " + loc[75]+ " (" + league.playerGetName(league.userTeam.players[pickedPlayer]) + ")", COLOR_POPUP_BAD);
				}
				league.userTeam.swapOrdinalToOrdinal(pickedPlayer, selectionTo); 
				selectingPosition = false;
				pickedPlayer = -1;
				int t1 = iformScrollTopItem;
				int t2 = iformSelectedItem;
				invokeTacticsPosition(false);
				// hack hack haslkjdlskjd blaeh
				iformScrollTopItem = t1;
				iformSelectedItem = t2;
				focusedPlayer = selectionTo;
				*/
				iformDataHeaderSmall = gameText[70][0];
				String playerFromPenal = null;
				String playerToPenal = null;
				setAttToUserTeamPlayerListing();
//#ifndef REMOVE_FEATURES
				int playerFrom = league.userTeam.players[pickedPlayer];
				int playerTo = league.userTeam.players[selectionTo];
				String playerFromName = league.playerGetName(playerFrom);
				String playerToName = league.playerGetName(playerTo);

				// calcular penalizacion
				int spPlayer1 = league.playerGetSpec(playerFrom);
				int spCoach1 = league.formationsSpec[league.coachFormation][selectionTo];
				int penal1 = 100;
				if (spCoach1 != 64 && spCoach1 != 96) {
					penal1 = league.positionPenalization[spPlayer1][spCoach1];
				}

				int spPlayer2 = league.playerGetSpec(playerTo);
				int spCoach2 = league.formationsSpec[league.coachFormation][pickedPlayer];
				int penal2 = 100;
				if (spCoach2 != 64 && spCoach2 != 96) {
					penal2 = league.positionPenalization[spPlayer2][spCoach2];
				}

				// calcular penalizaciones por posicion
				playerFromPenal = null;
				if (penal1 < 100) {
					playerFromPenal = gameText[74][0] + spaceString + penal1 + "% " + gameText[75][0] + " (" + playerFromName + ")";
				}
				if (penal2 < 100) {
					playerFromPenal = gameText[74][0] + spaceString + penal2 + "% " + gameText[75][0] + " (" + playerToName + ")";
				}
				if (league.playerIsInjured(playerFrom) && spPlayer1 != 96) {
					playerFromPenal = playerFromName + spaceString + gameText[72][0];
				}
				if (league.playerIsSanctioned(playerFrom) && spPlayer1 != 96) {
					playerFromPenal = playerFromName + spaceString + gameText[73][0];
				}
				playerToPenal = null;
				if (league.playerIsInjured(playerTo) && spPlayer1 != 96) {
					playerToPenal = playerToName + spaceString + gameText[72][0];
				}
				if (league.playerIsSanctioned(playerTo) && spPlayer1 != 96) {
					playerToPenal = playerToName + spaceString + gameText[73][0];
				}
//#endif
				// prioridad 4: cambio normal 
				league.userTeam.swapOrdinalToOrdinal(pickedPlayer, selectionTo);
				if (playerFromPenal != null) {
					formModalPopup(playerFromPenal, COLOR_POPUP_BAD);
				} else if (playerToPenal != null) {
					formModalPopup(playerToPenal, COLOR_POPUP_BAD);
				}
				selectingPosition = false;
				pickedPlayer = -1;
				int t1 = iformScrollTopItem;
				int t2 = iformSelectedItem;
				invokeTacticsPosition(false);
				// hack hack haslkjdlskjd blaeh
				iformScrollTopItem = t1;
				iformSelectedItem = t2;
				focusedPlayer = selectionTo;
			}
		}
	}
}

// ----------------------------------------------------------------------------
// tactics - position - confirm

private int ordPickedPlayer;
private int ordDestPlayer;
private int savediformScrollTopItem;
private int savediformSelectedItem;
private void invokeTacticsPositionConfirm(int from, int to, int ti, int si) {
	ordPickedPlayer = from;
	ordDestPlayer = to;
	savediformScrollTopItem = ti;
	savediformSelectedItem = si;
	fontSet(iformFontDB);
	formYesNoList(gameText[83][0], gameText[208][0], gameText[3][0], gameText[2][0],
		printTextBreak(gameText[209][0] + spaceString + league.playerGetName(league.userTeam.players[from]) +
			spaceString + gameText[209][1] + spaceString + 
			league.playerGetName(league.userTeam.players[to])+ "?", canvasWidth - 8));
	iformFont = iformFontDB;
	currentForm = FORM_TACTICS_POSITION_CONFIRM;
}

private void actionTacticsPositionConfirm(int hiEvent) {
	if (hiEvent == HIEVENT_NO) {
		invokeTacticsPosition(true);
		iformScrollTopItem = savediformScrollTopItem;
		iformSelectedItem = savediformSelectedItem;
		focusedPlayer = ordDestPlayer;
		return;
	} else if (hiEvent == HIEVENT_YES) {
		remainingMatchChanges--;
		league.userTeam.swapOrdinalToOrdinal(ordPickedPlayer, ordDestPlayer); 
		invokeTacticsPosition(true);
		iformScrollTopItem = savediformScrollTopItem;
		iformSelectedItem = savediformSelectedItem;
		focusedPlayer = ordDestPlayer;
		return;
	}
}


// ----------------------------------------------------------------------------
// save before quit

private boolean saveGame() {
	formWait(true);
	byte[] data = league.saveState();
	boolean err = savePersistent(1, data);
	formWait(false);
	return err;
}


private void invokeSaveOverwrite(boolean _backToStart) {
	fontSet(iformFontDefault);
	String[] saveItems = printTextBreak(gameText[76][1], canvasWidth - 8);
	formPlainListYesNo(gameText[77][0], null, gameText[3][0], gameText[2][0], saveItems);
	currentForm = FORM_SAVE_OVERWRITE;
	showMenuBar = false;
	backToStart = _backToStart;
}

private void invokeSave() {
	fontSet(iformFontDefault);
	String[] saveItems = printTextBreak(gameText[76][0], canvasWidth - 8);
	//formYesNoList(loc[77], null, loc[3], loc[2], saveItems);
	formPlainListYesNo(gameText[77][0], null, gameText[3][0], gameText[2][0], saveItems);
	currentForm = FORM_SAVE;
	showMenuBar = false;
}
/*
private void actionSave(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {
		boolean err = saveGame();
		invokeStartMenu();
		if (err) {
			formModalPopup(gameText[78][0], COLOR_POPUP_BAD);
		}
	} else if (hiEvent == HIEVENT_NO) {
		invokeStartMenu();
	}
}
*/
// ----------------------------------------------------------------------------
// just save

private void invokeSave2() {
	fontSet(iformFontDefault);
	String[] saveItems = printTextBreak(gameText[266][0], canvasWidth - 8);
	formYesNoList(gameText[265][0], null, gameText[3][0], gameText[2][0], saveItems);
	currentForm = FORM_SAVE2;
	showMenuBar = false;
}

private void actionSave2(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {
		boolean err = saveGame();
		invokeMainMenu();
		if (err) {
			formModalPopup(gameText[78][0], COLOR_POPUP_BAD);
		}
	} else if (hiEvent == HIEVENT_NO) {
		invokeMainMenu();
	}
}

// ----------------------------------------------------------------------------
// match

private void invokeMatch() {
	//String[] matchItems = printTextBreak(league.journeyMatch[0].name + " " + loc[79] + " " +
	//	league.journeyMatch[1].name, canvasWidth - 5);
	int downPlayers = league.countDownPlayers(11);
	String[] mi = new String[2+downPlayers];
	mi[0] = league.journeyMatch[0].name + spaceString + gameText[79][0] + spaceString + league.journeyMatch[1].name;
	if (downPlayers > 0) {
		mi[1] = gameText[211][0] + ":";
		int j = 2;
		for (int i = 0; i < 11; i++) {
			if (league.playerIsSanctioned(league.userTeam.players[i]) || league.playerIsInjured(league.userTeam.players[i])) {
				mi[j] = league.playerGetName(league.userTeam.players[i]);
				j++;
			}
		}
	} else {
		mi[1] = "";
	}
	
	// hack...
	fontSet(iformFontDB);
	String[] matchItems = printTextBreak(mi, canvasWidth - 8);
	
	//for(int i = 0; i < matchItems.length; i++) 
	//System.out.println("Linea "+i+":"+matchItems[i]);
	
	//System.out.println("" + (canvasWidth - 5));
	formPlainListYesNo(gameText[80][0], gameText[81][0], gameText[3][0], gameText[2][0], matchItems);
	
	currentForm = FORM_MATCH;
	iformFont = iformFontDB;
	showMenuBar = false;
	requestMenuBar = false;
}

private void actionMatch(int hiEvent) {
	if (hiEvent == HIEVENT_YES) {
		if (league.countDownPlayers(11) > 0) {
			//invokeMainMenu();
			invokeTacticsPosition(false);
			formModalPopup(gameText[240][0], COLOR_POPUP_BAD);
		} else {
			invokeMatchPlay();
		}
	} else if (hiEvent == HIEVENT_NO) {
		invokeMainMenu();
	}
}

// ----------------------------------------------------------------------------
// match play

private void resumeMatch() {
	currentForm = FORM_MATCH_PLAY;
	String[] matchItems = { };
	formYesNoList("", "", gameText[82][0], gameText[83][0], matchItems, false);
	screenLost = true;
}

//private Image numImg;
private void invokeMatchPlay() {
	pauseMatch = false;
	String[] matchItems = { };
	formWait(true);
	iformBackground = null;
	//iformTopBackground = null;
	System.gc();
//#ifdef J2ME
//#ifdef BUG_IMAGE_LEAK
//#else
//#ifndef REDUCEIMAGES
	iformBackground = loadImage("/bg3.png");
	//#ifdef NOBACKGROUNDIMAGE
	//#endif
//#else
//#endif
	altTopBackground = iformTopBackground;
	iformTopBackground = loadImage("/marcador.png");
//#endif
//#ifdef COMPONERMARCADOR 
	//numImg = loadImage("/marcador2.png");
//#endif
//#else
//#endif
		
	currentForm = FORM_MATCH_PLAY;

	// juega toda la jornada MENOS el partido del user
	// ESTA LLAMADA ANTES QUE setupPlayerMatch SIEMPRE
	//SENSEI
	
	//#ifndef NOPRESEASON
	if(!league.preSeasonPlaying)
	//#endif
		league.playJourney();

	// prepara el partido del player, que se jugara por ticks minuto a minuto
	league.setupPlayerMatch();
	league.gamecanvas = this;

	minute = 0;
	mpTimeText = "" + str60(minute);
	mpEventsText = new String[0];
	mpEventsTextTop = 0;
	topMinute = 90 + (league.rand() % 4); 
	mpScoreLeft = 0;
	mpScoreRight = 0;
	mpPrevTopTrain = league.coachTopTraining;
	mpEventCompletion = null;
	formYesNoList("", "", gameText[82][0], gameText[83][0], matchItems, false);
	formWait(false);
	remainingMatchChanges = 3;
}

private long nextTick = 0;
public static int minute = 0;
private int topMinute = 0;
private String mpTimeText;
private String[] mpEventsText = null;
private int mpEventsTextTop = 0;
private String mpEventCompletion;
private int mpTicksToCompletion = 0;
private int mpScoreLeft;
private int mpScoreRight;
private int mpPrevTopTrain;

private String str60(int i) {
	if (i < 10) {
		return "0" + i;
	}
	return "" + i;
}

public int hackSound = -1;
public int hackMins = -1;
public void addMatchEvent(String event, String eventCompletion) {
	String[] matchItems = { };
	int ns = mpEventsText.length + 1;
	if (ns > 10)
		ns = 10;
	String[] s = new String[ns];
	System.arraycopy(mpEventsText, 0, s, 1, s.length - 1);
	// hack, para que no queden los segundos mal...
	if (eventCompletion != null && event != null) {
		hackMins = 5;
		s[0] = "" + str60(minute) + ":" + str60(hackMins) + " - " + event;
	} else {
		s[0] = "" + str60(minute) + ":" + str60(hackMins + 2 + (league.rand() % 2)) + " - " + event;
	}
	mpEventsText = s;
	mpEventCompletion = eventCompletion;
	if (eventCompletion != null) {
		formYesNoList("", "", gameText[82][0], null, matchItems, false);
		mpTicksToCompletion = 6;
	} else {
		mpTicksToCompletion = 0;
		if (hackSound > -1) {
			soundPlay(hackSound, 1);
			hackSound = -1;
		}
	}
}

private void actionMatchPlay(int hiEvent) {
	String[] matchItems = { };
	// caso 1: tick de tiempo, no estamos al final
	if (hiEvent == HIEVENT_IDLE && minute < topMinute) {
		
		if (minute == 45) soundPlay(6,1);
		if (minute == topMinute-2) soundPlay(6,1);
		
		// caso 1.1!!! multitasking-only
		//#ifdef MULTITASKING
		if (pauseMatch) {
			invokeTacticsPosition(true);
			formModalPopup(gameText[270][0], COLOR_POPUP_GOOD);
			pauseMatch = false;
			return;
		}
		//#endif
		if (nextTick > GameMilis)
			return;
		if (mpEventCompletion == null) {
			// jugar tick. esto llamara a addMatchEvent si es necesario
			minute++;
			String m = league.tickPlayerMatch(true);
			if (m != null && league.matchPopup) {
				// ha habido un evento que merecia saltar a los swaps
				invokeTacticsPosition(true);
				formModalPopup(m, COLOR_POPUP_GOOD);
			}
		} else {
			mpTicksToCompletion--;
			if (mpTicksToCompletion <= 0) {
				formYesNoList("", "", gameText[82][0], gameText[83][0], matchItems, false);
				addMatchEvent(mpEventCompletion, null);
				mpScoreLeft = league.journeyMatch[0].matchGoals;
				mpScoreRight = league.journeyMatch[1].matchGoals;
				minute++;
				if (minute >= topMinute)
					minute = topMinute;
			}
		}
		nextTick = GameMilis + 333;
		mpTimeText = "" + str60(minute) + ":00";
		//#ifdef SLOW_MATCH
		//#else
		screenLost = true;
		//#endif
		return;
	}
	// caso 2: estamos al final
	if (minute >= topMinute) {
		mpScoreLeft = league.journeyMatch[0].matchGoals;
		mpScoreRight = league.journeyMatch[1].matchGoals;
		mpTimeText = "" + str60(topMinute) + ":" + str60(league.rand() % 60);
		league.postPlayerMatch();
		invokeMatchPost();
		formModalPopup(league.ppmResultMessage, COLOR_POPUP_GOOD);
		return;
	}
	// caso 3: no estamos al final, ni hay tick de tiempo: pantalla de cambiar jugador o skip 
	if (hiEvent == HIEVENT_YES) {
		// solo dejamos hacer cambios si estamos fuera de una jugada
		if (mpTicksToCompletion <= 0) {
			invokeTacticsPosition(true);
		}
	} else if (hiEvent == HIEVENT_NO) {
		// todo lo que queda de golpe
		while (minute <= topMinute) {
			league.tickPlayerMatch(false);
			minute++;			
		}
		mpScoreLeft = league.journeyMatch[0].matchGoals;
		mpScoreRight = league.journeyMatch[1].matchGoals;
		mpTimeText = "" + str60(topMinute) + ":" + str60(league.rand() % 60);
		league.postPlayerMatch();
		mpTicksToCompletion = -1;
		mpEventCompletion = null;
		invokeMatchPost();
		formModalPopup(league.ppmResultMessage, COLOR_POPUP_GOOD);
		return;
	}
}

// ----------------------------------------------------------------------------
// match post

private void invokeMatchPost() {
	mpScoreLeft = league.journeyMatch[0].matchGoals;
	mpScoreRight = league.journeyMatch[1].matchGoals;
	
	//#ifndef NOCHAMPIONS
	String[] it = new String[] { gameText[212][0], gameText[213][0], gameText[214][0], gameText[134][0] };
	tcpb = 3;
	//#else
	tcpb = 2;	
	//#endif
	
	fontSet(iformFontDefault);
	formPlainMenu("", "", it);
	
	//#ifdef FULLSCREEN
	/*requestMenuBar = true;
	iformDataSoftLeft = null; 
	iformDataSoftRight = null;*/
	//#endif
	iformFont = iformFontDefault;
	mpEventsText = new String[0];
	currentForm = FORM_MATCH_POST;
}

private void actionMatchPost(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == 0) {
		invokeMatchPostMatchStats();
		return;
	} else if (selectionTo == 1) {
		//invokeMatchPostRoundStats();	
		//#ifdef NOPRESEASON
		invokeMatchPostRoundStats();
		//#else
		if (!league.preSeasonPlaying) {
			invokeMatchPostRoundStats();
		} else {
			formModalPopup(gameText[TEXT_SENSEI+4][4], COLOR_POPUP_GOOD);
		}
		//#endif		
		
		return;
    //#ifndef NOCHAMPIONS	
	} else if (selectionTo == 2) {
		if (league.lastEuropeRound != null) {
			invokeMatchPostEuropeStats();
		} else {
			formModalPopup(gameText[215][0], COLOR_POPUP_GOOD);
		}
		return;
	//#endif		
	} else if (selectionTo >= tcpb) 
	{
		formWait(true);
		altTopBackground = null;
		//numImg = null;
					
		// SENSEIHACK
						
		if (league.journeyNumber < league.journeyFinal) {
			league.endJourney();
			league.startJourney(true);
			System.gc();
			
			//#ifndef NOFINANCES
			if(league.coachCash < 0) {
				
				invokeNoCashGameOver();
						
			}else{
			//#endif
							
				loadFormBackground();
				invokeMainMenu();
				System.gc();
				if (saveAfterMatch) {
					saveGame();
				}	
				
			//#ifndef NOFINANCES
			}
			//#endif
			
			formWait(false);
			
		} else {
			// es el final de liga
			loadFormBackground();
			System.gc();
			
			formWait(false);
			
			// SENSEI
			//#ifndef NOSEASONGOAL
			if(!league.seasonGoalAcomplished()) {
				invokeGoalFailedGameOver();
				//formModalPopup(gameText[91][0] + spaceString, COLOR_POPUP_GOOD);
			} else {
				invokeEndLeague();							
				formModalPopup(gameText[91][0] + spaceString + league.userTeamPosStr() + gameText[TEXT_SENSEI+3][3], COLOR_POPUP_GOOD);				
			}
			//#else
			invokeEndLeague();							
			formModalPopup(gameText[91][0] + spaceString + league.userTeamPosStr(), COLOR_POPUP_GOOD);			
			//#endif
		}
		return;
	}
}

// ----------------------------------------------------------------------------
// match post - round stats

private void invokeMatchPostRoundStats() {
	String[] classLabels = { gameText[37][0], "", "" };
	int n = league.lastLeagueRound.length/2;
	String[] st = new String[n];
	int i = 0;
	for (i = 0; i < st.length; i++) {
		st[i] = barString + league.lastLeagueRound[i*2].name + " vs. " +
			league.lastLeagueRound[i*2+1].name + 
			barString + league.lastLeagueResults[i*2] +
			barString + league.lastLeagueResults[i*2+1] + barString; 
	}
	//ZNR MARK 1
	//st[i] = barString + gameText[TEXT_BACK][0] + "|||";
	tpbv = i;
	String[][] its = new String[3][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	formPlainMenu(gameText[213][0], gameText[123][0] + spaceString + (league.journeyNumber + 1), its, classLabels);
	iformFont = iformFontDB;	
	currentForm = FORM_MATCH_POST_ROUND_STATS;
}

/*
private void actionMatchPostRoundStats(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tpbv) {
		invokeMatchPost();
		return;
	}
}
*/

// ----------------------------------------------------------------------------
// match post - europe stats

//#ifndef NOCHAMPIONS
private void invokeMatchPostEuropeStats() {
	String[] classLabels = { gameText[37][0], "", "" };
	int n = league.lastEuropeRound.length/2;
	String[] st = new String[n];
	int i = 0;
	for (i = 0; i < st.length; i++) {
		int r1 = league.lastEuropeResults[i*2];
		int r2 = league.lastEuropeResults[i*2+1];
		st[i] = barString + league.lastEuropeRound[i*2].name + " vs. " +
			league.lastEuropeRound[i*2+1].name;
		if (r1 != r2) {
			st[i] += barString + r1 + barString + r2 + barString;
		} else {
			if (league.lastEuropeVictories[i*2] == 1) {
				st[i] += "|*" + r1 + barString + r2 + barString;
			} else if (league.lastEuropeVictories[(i*2)+1] == 1) {
				st[i] += barString + r1 + "|*" + r2 + barString;
			}
			else
				st[i] += barString + r1 + barString + r2 + barString;
		}
	}
	//st[i] = barString + gameText[TEXT_BACK][0] + "|||";
	tpbv = i;
	String[][] its = new String[3][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	formPlainMenu(gameText[214][0], league.getChampJourneyNameProto(((league.champRound%2) == 0 || league.champTeams.length == 2)?2:1), its, classLabels);
	iformFont = iformFontDB;	
	currentForm = FORM_MATCH_POST_EUROPE_STATS;
	//ZNR MARK 2
}


private void actionMatchPostEuropeStats(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tpbv) {
		invokeMatchPost();
		return;
	}
}
//#endif

// ----------------------------------------------------------------------------
// match post - match stats

private void invokeMatchPostMatchStats() {
	//int downPlayers = countDownPlayers(16);
	int downPlayers = league.lastMatchDownPlayers.length;
	String[] matchItems = new String[4+downPlayers];
	matchItems[0] = "- " + league.ppmResultMessage;
	league.calcTopTraining();
	if (league.journeyMatch[0] == league.userTeam) {
		int p = league.teams[league.playingLeague].length - league.userTeamPos();
		int val = (p * 35) / league.teams[league.playingLeague].length;
		matchItems[1] = "- " + gameText[193][0] + spaceString + League.cashStr(val) + spaceString + gameText[188][0];
		//league.coachCash += val;
	} else {
		matchItems[1] = "";
	}
	if (league.journeyNumber == league.journeyFinal) {
		matchItems[2] = "- " + gameText[91][0] + spaceString + league.userTeamPosStr();
	} else {
		matchItems[2] = "";
	}
	if (downPlayers > 0) {
		matchItems[3] = "- " + gameText[216][0] + ":";
		int j = 4;
		/*for (int i = 0; i < 16; i++) {
			if (league.playerIsSanctioned(league.userTeam.players[i]) || league.playerIsInjured(league.userTeam.players[i])) {
				matchItems[j] = league.playerGetName(league.userTeam.players[i]);
				j++;
			}
		}*/
		for (int i = 0; i < league.lastMatchDownPlayers.length; i++, j++) {
			int w = (league.playerDowntime(league.lastMatchDownPlayers[i]) - 1);
			if (w <= 1) {
				matchItems[j] = league.playerGetName(league.lastMatchDownPlayers[i]) +
					" (" + w + spaceString + gameText[258][0] + ")";
			} else {
				matchItems[j] = league.playerGetName(league.lastMatchDownPlayers[i]) +
					" (" + w + spaceString + gameText[132][0] + ")";
			}
		}
	} else {
		matchItems[3] = "";
	}
	fontSet(iformFontDB);
	String[] mi = printTextBreak(matchItems, canvasWidth - 8);
	//formYesNoList(loc[212], "", "", loc[92], mi, false);
	//formPlainListYesNo(gameText[212][0], "", "", gameText[92][0], mi);
	formPlainList(gameText[212][0], null, mi);
	iformBehaviour = IFORM_BEHAVIOUR_SCROLL_TEXT_YES_NO;
	//iformPaintSoftBack = false;
	iformFont = iformFontDB;
	currentForm = FORM_MATCH_POST_MATCH_STATS;
}

private void actionMatchPostMatchStats(int hiEvent) {
	if (hiEvent != HIEVENT_IDLE)
		invokeMatchPost();
}

// ----------------------------------------------------------------------------
// end of league

private void invokeEndLeague() {
	String[] its = { gameText[267][0] + spaceString + league.userTeam.name, gameText[268][0],
			gameText[44][0] + " 1", gameText[44][0] + " 2",
			//#ifndef NOTOPPLAYERS
			gameText[45][0],
			//#endif
			gameText[TEXT_MENU][0]
			}; 
	
	loadFormBackground();
	
	formPlainMenu(gameText[94][0], gameText[91][0] + spaceString + league.userTeamPosStr(), its);
	//iformDataSoftLeft = null;
	requestMenuBar = false;
	currentForm = FORM_END_LEAGUE;
}

private boolean saveInStart;
private void actionEndLeague(int hiEvent) {
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == 0) {
		league.startSeason();
		league.startJourney(true);
		if (saveAfterMatch) {
			saveGame();
		}
		
		// SENSEI
		//invokeEndLeague();							
		//#ifndef NOSEASONGOAL
		invokeSeasonGoalInfo();
		//#else
		//#endif
				
	} else if (selectionTo == 1) {
		if (saveAfterMatch) {
			saveInStart = true;
		}
		invokeChooseLeague(true);
	} else if (selectionTo == 2) {
		invokeClassification(true);
	} else if (selectionTo == 3) {
		invokeClassificationExtra(true);
	}
	//#ifndef NOTOPPLAYERS
	else if (selectionTo == 4) {
		invokeTopPlayers(true);
	}else if (selectionTo == 5) {
		invokeStartMenu();
	}
	//#else
	//#endif
}

//----------------------------------------------------------------------------
//SENSEI

public static final String spaceString = " ";
public static final String barString = "|";
public static final String twoDotString = ": ";

public void loadFormBackground() {
	
	iformBackground = null;
	iformTopBackground = null;
	//numImg = null;
	System.gc();
//#ifdef J2ME
//#ifdef BUG_IMAGE_LEAK
//#else
	//iformBackground = loadImage("/1.png");		
	//#ifndef NOBACKGROUNDIMAGE
	if(league.champJourney)
		iformBackground = loadImage("/bg2.png");
	else
		iformBackground = loadImage("/bg1.png");
	//#else
	//#endif
	iformTopBackground = loadImage("/menu.png");
	
//#endif
//#else
//#endif
	
	
}


//Finances

//#ifndef NOFINANCES
private void invokeFinancesGeneral() {
	
		
	String[] its = {gameText[TEXT_SENSEI][2]+League.cashStr(league.coachCash) + spaceString + gameText[188][0],
					gameText[TEXT_SENSEI][3],
					gameText[TEXT_SENSEI][4]+League.cashStr(league.haveSponsor ? league.sponsorBenefit : 0) + spaceString + gameText[188][0],
					gameText[TEXT_SENSEI][5]+League.cashStr(league.journeyIncome) + spaceString + gameText[188][0],
					gameText[TEXT_SENSEI][6],
					gameText[TEXT_SENSEI][7]+League.cashStr(league.teamPlayerWage()) + spaceString + gameText[188][0],
					gameText[TEXT_SENSEI][8]+League.cashStr(league.journeyExpenses) + spaceString + gameText[188][0]};
		
	//#ifdef FIXPLAINLIST
	//#endif
	
	String[] mi = printTextBreak(its, canvasWidth - 8);
		
	currentForm = FORM_FINANCES_GENERAL;	
	formPlainList(gameText[TEXT_SENSEI][0],gameText[TEXT_SENSEI][1],mi);
	iformFont = iformFontDefaultSmall;
	requestMenuBar = true;

}

private void actionFinancesGeneral(int hiEvent) {
	
	if (hiEvent == HIEVENT_IDLE)
		return;	
	//requestMenuBar = true;
	//showMenuBar = true;
	invokeMainMenu();
}

private void invokeFinancesNewSponsor() {
			
	String its[] = {gameText[TEXT_SENSEI + 1][1]+league.cashStr(league.sponsorBenefit)+gameText[188][0]+gameText[TEXT_SENSEI + 1][2]};
	//#ifdef FIXPLAINLIST
	//#endif
	String[] mi = printTextBreak(its, canvasWidth - 8);	
	currentForm = FORM_FINANCES_NEW_SPONSOR;
//#ifdef SHORTSPONSOR
	//#else
	formPlainListYesNo(gameText[TEXT_SENSEI + 1][0], gameText[TEXT_SENSEI + 1][3], gameText[3][0], gameText[2][0], mi);
	//#endif
	iformFont = iformFontDefaultSmall;
	showMenuBar = false;
	iformModalPopup = false;
		
}

private void actionFinancesNewSponsor(int hiEvent) {
	
	if (hiEvent == HIEVENT_IDLE)
		return;	
	if (hiEvent == HIEVENT_YES) {
		
		// Se contrata el sponsor		
		league.haveSponsor = true;
				
		//invokeFinancesSponsor();
		invokeMainMenuReturn();
		formModalPopup(gameText[TEXT_SENSEI + 1][4], COLOR_POPUP_GOOD);		
		return;
			
	} else if (hiEvent == HIEVENT_NO) {
			
		// Cancela		
		invokeMainMenu();
	}
}

private void invokeFinancesSponsor() {
	
	String its[] = {gameText[TEXT_SENSEI + 1][5]};
	//#ifdef FIXPLAINLIST
	//#endif
		
	String[] mi = printTextBreak(its, canvasWidth - 8);
	currentForm = FORM_FINANCES_SPONSOR;
	//#ifdef SHORTSPONSOR
	//#else
	formPlainListYesNo(gameText[TEXT_SENSEI + 1][0], gameText[TEXT_SENSEI + 1][6]+League.cashStr(league.sponsorBenefit)+spaceString+gameText[188][0]+gameText[TEXT_SENSEI + 1][7], gameText[3][0], gameText[2][0], mi);
	//#endif
	
	iformFont = iformFontDefaultSmall;
	showMenuBar = false;
	iformModalPopup = false;
		
}

private void actionFinancesSponsor(int hiEvent) {
	
	if (hiEvent == HIEVENT_IDLE)
		return;	
	if (hiEvent == HIEVENT_YES) {
		
		// Se cambia de sponsor
		league.haveSponsor = false;
					
		//invokeFinancesNewSponsor();
		invokeMainMenuReturn();
		formModalPopup(gameText[TEXT_SENSEI + 1][8], COLOR_POPUP_GOOD);
		return;
			
	} else if (hiEvent == HIEVENT_NO) {
			
		// Cancela		
		invokeMainMenu();
	}
}
//#endif

private void invokeTransfersOffer(short player)
{
	firingPlayer = player;
	short p = league.userTeam.players[player];
	fontSet(iformFontDB);
	//#ifdef DOJA
	//#else
	String sadd = "";
	if (gameText[262].length > 2) sadd = gameText[262][2];
	formPlainListYesNo(		gameText[57][0], 
			gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + spaceString + gameText[188][0], 
			gameText[3][0], 
			gameText[2][0],				
			printTextBreak(gameText[262][0] + spaceString + league.playerGetName(p) + sadd + spaceString + gameText[207][0] + spaceString +
						League.cashStr(league.offertedPlayerValue(p)) + spaceString + gameText[188][0]+gameText[262][1],
						canvasWidth - 8));

	//#endif
	iformFont = iformFontDB;
	
	/*String its[] = {"Tienes una oferta de XXX millones por el player paco. La aceptas?"};
	fontSet(FONT_LARGE);
	String[] mi = printTextBreak(its, canvasWidth - 8);	
	formPlainListYesNo("Transfer", "Tienes una oferta", "NO", "YES", mi);*/
	currentForm = FORM_TRANSFERS_OFFER;//FORM_FINANCES_SPONSOR;
	showMenuBar = false;
	iformModalPopup = false;	
}


private void actionTransfersOffer(int hiEvent) {
	
	if (hiEvent == HIEVENT_IDLE)
		return;	
		
	if (hiEvent == HIEVENT_NO) {
			//No vendes		
		invokeMainMenu();
		
		return;
	}
	if (hiEvent == HIEVENT_YES) {
			
		/*
		if (league.userTeam.countPlayers() <= 16) {
			formModalPopup(gameText[195][0], COLOR_POPUP_BAD);
			invokeMainMenu();
			return;
		}
		*/
		short p = league.userTeam.players[firingPlayer];
		int otherQuality = league.playerBalance(p, -1);
		Team t = league.userTeam;
		while (t == league.userTeam)
			t = league.vacantTeamQA(otherQuality - 30);
		league.coachCash += league.offertedPlayerValue;

		
		// SENSEI
		//#ifndef NOFINANCES
		league.journeyIncome += league.offertedPlayerValue;
		//#endif
		
		league.userTeam.removePlayer(p);
		t.addPlayer(p);
		league.userTeam.markDirtyQuality();
		////t.markDirtyQuality();
		//invokeTransfersMain();				
		formModalPopup(gameText[66][0] + spaceString + league.playerGetName(p) +
			". " + gameText[67][0] + spaceString + t.name , COLOR_POPUP_GOOD);		
		//league.coachRemainingFires--;
		
		
		// poner a 0 los goles y reordenar top players, siempre y cueando el equipo sea
		// de otra liga
		if (league.userTeam.loadLeague != t.loadLeague) {
			league.playerSetGoals(p, 0);
			league.bubbleSortTop40();
		}
		invokeMainMenuReturn();
		
	}
		
	/*
	if (hiEvent == HIEVENT_NO) {
		//No vendes		
		invokeMainMenu();
		//invokeTransfersHireSelPlayer(hiringTeam);
		return;
	}
	if (hiEvent == HIEVENT_YES) {
		short p = hiringPlayer;
		Team t = hiringTeam;
		int v = league.playerValue(p);
		t.removePlayer(p);
		league.userTeam.addPlayer(p);
		league.availHires--;
		league.coachCash -= v;
		
		// SENSEI
		league.journeyExpenses -= v;
		
		league.userTeam.markDirtyQuality();
		//invokeTransfersMain();
		invokeMainMenu();
		formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + t.name, COLOR_POPUP_GOOD);
	}*/
	/*if (hiEvent == HIEVENT_YES) {		
		invokeMainMenu();
		formModalPopup("Te deshaces del jugador", COLOR_POPUP_GOOD);
		return;			
	} else if (hiEvent == HIEVENT_NO) {			
		// No vendes		
		invokeMainMenu();
	}*/
}

//#ifndef NOSEASONGOAL

private void invokeSeasonGoalInfo() {
		
	fontSet(iformFontDefaultSmall);
	String[] target = {
			gameText[TEXT_SENSEI + 2][1],
			gameText[TEXT_SENSEI + 2][2],
			gameText[TEXT_SENSEI + 2][3],
			gameText[TEXT_SENSEI + 2][4]
			};
	fontSet(FONT_SMALL);
	//#ifdef FIXPLAINLIST
	//#endif
	String[] s = printTextBreak(target[league.seasonGoal - 1], canvasWidth - 8);
	currentForm = FORM_SEASON_GOAL_INFO;
	formPlainList(gameText[TEXT_SENSEI + 2][0], null, s);	
	iformFont = iformFontDefaultSmall;	
}

/*
private void actionSeasonGoalInfo(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeMainMenu();
}
*/


private void invokeGoalFailedGameOver() {
	
	String its[] = {gameText[91][0] + spaceString +league.userTeamPosStr(),gameText[TEXT_SENSEI + 3][1]};
	fontSet(FONT_SMALL);
	String[] mi = printTextBreak(its, canvasWidth - 8);
	currentForm = FORM_GAME_OVER;
	formPlainList(gameText[TEXT_SENSEI + 3][0], null, mi);
	iformFont = iformFontDefaultSmall;
	showMenuBar = false;
	loadFormBackground();	
}
//#endif

//#ifndef NOFINANCES
private void invokeNoCashGameOver() {
	
	String its[] = {gameText[TEXT_SENSEI + 3][2]};
	fontSet(FONT_SMALL);
	String[] mi = printTextBreak(its, canvasWidth - 8);
	currentForm = FORM_GAME_OVER;
	formPlainList(gameText[TEXT_SENSEI + 3][0], null, mi);
	iformFont = iformFontDefaultSmall;
	showMenuBar = false;
	loadFormBackground();	
}
//#endif

//#ifndef NOFINANCES
//#ifndef NOSEASONGOAL
private void actionGameOver(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeStartMenu();
}
//#endif
//#endif


//#ifndef NOWELCOME

private void invokeWelcome() {
	
	String its[] = {gameText[TEXT_SENSEI + 4][1]+league.userTeam.name+gameText[TEXT_SENSEI + 4][2]};
	fontSet(FONT_SMALL);
	//#ifdef FIXPLAINLIST
	//#endif
	String[] mi = printTextBreak(its, canvasWidth - 8);
	currentForm = FORM_WELCOME;
	formPlainList(gameText[TEXT_SENSEI + 4][0], null, mi);	
	iformFont = iformFontDefaultSmall;
	showMenuBar = false;
		
}



private void actionWelcome(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeSeasonGoalInfo();
}

//#endif

private void invokeMainMenuReturn() {
	
	iformDataSoftLeft = null;
	iformDataSoftRight = gameText[TEXT_OK][0]; 
	currentForm = FORM_MAIN_MENU_RETURN;
	showMenuBar = false;
		
}
/*
private void actionMainMenuReturn(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	invokeMainMenu();
}
*/
//#ifdef ADVANCEDSEARCH

private void invokeTransfersHireSelSearchMode() {
		
	String its[] = { gameText[TEXT_SENSEI + 5][2], gameText[TEXT_SENSEI + 5][3]/*, gameText[TEXT_SENSEI + 5][4] */};
		
	//formPlainMenu(loc[34], loc[95+selectionTo], its, chooseTeamLabels);
	requestMenuBar = true;
	currentForm = FORM_TRANSFERS_HIRE_SEL_SEARCH_MODE;
	formPlainMenu(gameText[TEXT_SENSEI + 5][0], gameText[TEXT_SENSEI + 5][1], its);
	iformFont = iformFontDefaultSmall;
}

private void actionTransfersHireSelSearchMode(int hiEvent) {
	
	int selectionTo = hiEventSelectedItem(hiEvent);
	/*if (selectionTo == 2) {
		invokeMainMenu();
	} else*/
	if (selectionTo == 0) {
		invokeTransfersHireSelLeague();		
	} else if (selectionTo == 1) {
		invokeTransfersHireSearchFilter(0);		
	} 
}

public static final int FILTER_MINIMUM_COST = 50;
int filterLeague = 0, filterSpec = 0, filterMaxCost = FILTER_MINIMUM_COST;
short filterPlayerList[];


private void invokeTransfersHireSearchFilter(int sel) {
	
	//filterMaxCost = league.coachCash;
	 //filterMaxCost = FILTER_MINIMUM_COST;
	 
	String its[] = { gameText[TEXT_SENSEI + 6][3] + (filterLeague == 6 ? gameText[TEXT_SENSEI + 6][2] : gameText[95 + filterLeague][0]), 
					 gameText[TEXT_SENSEI + 6][4] + (filterSpec == 4 ? gameText[TEXT_SENSEI + 6][2] : gameText[110 + filterSpec][0]),
					 gameText[TEXT_SENSEI + 6][5] + league.cashStr(filterMaxCost)+gameText[188][0], 
					 gameText[TEXT_SENSEI + 6][6]/*, 
					 gameText[TEXT_SENSEI + 6][7] */};
		
	//formPlainMenu(loc[34], loc[95+selectionTo], its, chooseTeamLabels);
	requestMenuBar = true;
	currentForm = FORM_TRANSFERS_HIRE_SEARCH_FILTER;
	formPlainMenu(gameText[TEXT_SENSEI + 6][0], gameText[TEXT_SENSEI + 6][1], its);
	iformFont = iformFontDefaultSmall;	
	iformSelectedItem = sel;
}

private void actionTransfersHireSearchFilter(int hiEvent) {
	
	int selectionTo = hiEventSelectedItem(hiEvent);
	
	if (hiEvent == HIEVENT_IDLE)
		return;	
	
	//if(false)
	{
		switch(selectionTo) {
	
			case 0 : filterLeague = (filterLeague + 1) % 7; break;
			case 1 : filterSpec = (filterSpec + 1) % 5; break;
			case 2 : filterMaxCost = filterMaxCost += 5; if (filterMaxCost > league.coachCash) filterMaxCost = FILTER_MINIMUM_COST; break;
			case 3 : invokeTransfersHireSelPlayerFiltered(); break;
			//case 4 : invokeTransfersHireSelSearchMode(); break;	
		}
	
		if(selectionTo >= 0 && selectionTo < 3) {		
			invokeTransfersHireSearchFilter(selectionTo);
		}
	}

}

private void invokeTransfersHireSelPlayerFiltered() {

	String[] st;
	int playerCount = 0, index;
	
	formWait(true);
	
	// calcular items
		
	for(int i = 0; i < league.teams.length; i++)
	{		
		if(i == filterLeague || filterLeague == 6) {
			
			for(int j = 0; j < league.teams[i].length; j++) {
				
				Team t = league.teams[i][j];
				
				if(t != league.userTeam) {
				
					for(int k = 0; k < t.countPlayers(); k++) {
						
						int currentPlayer = t.players[k];
						
						if(
							(league.playerGetSpec(currentPlayer) == filterSpec || filterSpec == 4) &&
							(league.playerValue(currentPlayer) <= filterMaxCost)
							) {
							playerCount++;
						}
					}		
				}
			}
		}	
	}
	
	//System.out.println("Number of players found:"+playerCount);
	
	if(playerCount == 0) {
		formWait(false);	
		formModalPopup(gameText[TEXT_SENSEI + 6][8], COLOR_POPUP_BAD);
		return;		
	}
	else if (playerCount >= 95) playerCount = 95;
	
	st = new String[playerCount];
	filterPlayerList = new short[playerCount+2];
	index = 0;
	
	for(int i = 0; i < league.teams.length; i++)
	{		
		if(i == filterLeague || filterLeague == 6) {
			
			for(int j = 0; j < league.teams[i].length; j++) {
				
				Team t = league.teams[i][j];
				
				if(t != league.userTeam) {
				
					for(int k = 0; k < t.countPlayers(); k++) {
						
						short currentPlayer = t.players[k];
						
						if(
							(league.playerGetSpec(currentPlayer) == filterSpec || filterSpec == 4) &&
							(league.playerValue(currentPlayer) <= filterMaxCost)
							) 
						{
							if (index < playerCount)
							{
								st[index] = barString + league.playerGetName(currentPlayer) +
								barString + league.playerGetSpecAbbr(currentPlayer) +
								barString + league.playerBalance(currentPlayer, -1) +
								barString + League.cashStr(league.playerValue(currentPlayer)) + barString;
													
								filterPlayerList[index] = currentPlayer;
							
								//System.out.println(st[index]);
							
								index++;
							}
						}
					}		
				}
			}
		}	
	}
	
	formWait(false);
		
	//st[index] = barString + gameText[TEXT_BACK][0] + barString+barString+barString+barString;
	
	tcpb = index;
	
	String[][] its = new String[4][];
	its[0] = st;
	its[1] = null;
	its[2] = null;
	its[3] = null;
	String[] playerListingLabels = { gameText[4][0], gameText[33][0], gameText[194][0], gameText[190][0] };
	//formPlainMenu(loc[56], loc[62], its, playerListingLabels);
	requestMenuBar = true;
	currentForm = FORM_TRANSFERS_HIRE_SEL_PLAYER_FILTERED;
	
	formPlainMenu(gameText[56][0], gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + gameText[188][0], its, playerListingLabels);
	iformFont = iformFontDefaultSmall;
}

private void actionTransfersHireSelPlayerFiltered(int hiEvent) {
	if (hiEvent == HIEVENT_IDLE)
		return;
	int selectionTo = hiEventSelectedItem(hiEvent);
	if (selectionTo == tcpb)
	{
		invokeTransfersHireSearchFilter(0);
		//invokeMainMenu();
		return;
	}
	/*if (selectionTo == tcpb) {
		//if (hiringLeague == 5) {
		//	invokeTransfersHireSelLeague();
		//} else {
			invokeTransfersHireSelTeam(hiringLeague);
		//}
		return;
	}*/
		
	if (selectionTo >= 0) {
		short p = filterPlayerList[selectionTo];
		Team t = null;
		
		// Busca el equipo que tiene al jugador p
		for(int i = 0; i < league.teams.length; i++)
			for(int j = 0; j < league.teams[i].length; j++) {
			
				if(league.teams[i][j].hasPlayer(p)) {
					
					hiringTeam = league.teams[i][j];
					t = hiringTeam;
					break;
				}
			}
				
		int v = league.playerValue(p);
		if (t.countPlayers() <= 16) {
			formModalPopup(gameText[238][0], COLOR_POPUP_BAD);
			requestMenuBar = true;
			return;
		}
		if (league.coachCash < v) {
			formModalPopup(gameText[191][0], COLOR_POPUP_BAD);
			requestMenuBar = true;
			return;
		}
		int userQuality = league.userTeam.realQuality();
		//int otherQuality = league.playerBalance(p, -1);
		int otherQuality = t.realQuality();
		int diff = otherQuality - userQuality;
		if (diff > 7) {
			formModalPopup(gameText[192][0], COLOR_POPUP_BAD);
			requestMenuBar = true;
			return;
		}
		
		invokeTransfersHireConfirmFiltered(p);
		/*t.removePlayer(p);
		league.userTeam.addPlayer(p);
		league.clearHires();
		league.coachCash -= v;
		league.userTeam.markDirtyQuality();
		invokeTransfersMain();
		//if (hiringLeague != 5) {
			formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + t.name, COLOR_POPUP_GOOD);
		//} else {
		//	formModalPopup(loc[63] + " " + league.playerGetName(p) + " " + loc[64] + " " + loc[189], COLOR_POPUP_GOOD);
		//}
		*/
	}
}


private void invokeTransfersHireConfirmFiltered(short player) {
	hiringPlayer = player;
	fontSet(iformFontDB);	
	currentForm = FORM_TRANSFERS_HIRE_CONFIRM_FILTERED;
	
	//#ifdef DOJA
	//#else
	String sadd = "";
	if (gameText[207].length > 1) sadd = gameText[207][1];
	formPlainListYesNo(gameText[56][0], gameText[205][0] + twoDotString + League.cashStr(league.coachCash) + gameText[188][0], gameText[3][0], gameText[2][0],
			printTextBreak(gameText[206][0] + spaceString + league.playerGetName(player) +
				" (" + hiringTeam.name + ") " + spaceString + gameText[207][0] + spaceString + 
				League.cashStr(league.playerValue(player)) +
				spaceString + gameText[188][0]+sadd+gameText[262][1], canvasWidth - 8));
		
	//#endif
	iformFont = iformFontDB;	
}


private void actionTransfersHireConfirmFiltered(int hiEvent) {
	if (hiEvent == HIEVENT_NO) {
		invokeTransfersHireSelPlayerFiltered();
		return;
	}
	if (hiEvent == HIEVENT_YES) {
		short p = hiringPlayer;
		Team t = hiringTeam;
		int v = league.playerValue(p);
		t.removePlayer(p);
		league.userTeam.addPlayer(p);
		league.availHires--;
		league.coachCash -= v;
		
		// SENSEI
		//#ifndef NOFINANCES
		league.journeyExpenses += v;
		//#endif
		
		league.userTeam.markDirtyQuality();
		//invokeTransfersMain();
		invokeMainMenuReturn();
		formModalPopup(gameText[63][0] + spaceString + league.playerGetName(p) + spaceString + gameText[64][0] + spaceString + t.name, COLOR_POPUP_GOOD);
	}
}

//#endif


//#ifdef WEBSUBCRIPTION
//#endif
//<=- <=- <=- <=- <=-



//#ifndef NOCLUBBAR
int flagColors[] = {0x888888, 0x888888, 0x888888};//new int[3]
//#endif
final static int TEXT_DATEFORMAT = 52;
final static int TEXT_MONTH = 178;
final static int TEXT_WEEKDAY = 179;
final static int TEXT_CREDITS = 260;
final static int TEXT_LOADING = 160;
final static int TEXT_OK = 92;
final static int TEXT_BACK = 1;
final static int TEXT_MENU = 43;
final static int TEXT_QUIT = 42;
final static int TEXT_CURRENCY = 188;
// SENSEI
final static int TEXT_SENSEI = 293;

int cursorY;
int BACKBGCOLOR = 0x000050;
//#ifdef BACKCOLOR
//#endif










//#ifdef PLAYER_MO-E1000
//#endif

}


