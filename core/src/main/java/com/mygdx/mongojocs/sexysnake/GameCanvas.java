package com.mygdx.mongojocs.sexysnake;// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================



//#ifdef J2ME

//#elifdef DOJA
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
//#endif

public class GameCanvas extends BiosCanvas
{
	
	Image rabbits;
	Scroll sc;
	int level;
	int lives;
	
	public static final int mapX[] = {25, 25, 25, 25, 25};
	public static final int mapY[] = {27, 27, 27, 27, 27};
	public static final int nRab[] = {15, 12, 10, 8, 5};
	public static final int maxL[] = {30, 30, 40, 50, 55};
	public static final int goal[] = {25, 25, 30, 40, 50};
	//public static final int goal[] = {5, 5, 5, 5, 5};
	
public GameCanvas(Game ga) {super(ga);}

// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//

// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// *******************************************
// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// Terminal: 

// ---------------------------------
//  Bits de Control de cada Terminal
// =================================
final boolean deviceSound = true;		// Terminal con Sonido
final boolean deviceVibra = false;		// Terminal con Vibracion
final boolean deviceLight = true;		// Terminal con opciones de Luz en LCD ON/OFF
final boolean deviceMenus = false;		// Terminal con Texto inferior para menus (CommandListener / softKeyMenu)
// ----------------------------------

// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


// *******************
// -------------------
// canvas - Engine
// ===================
// *******************

// -------------------
// canvas Init
// ===================


public void canvasInit()
{
//#ifdef J2ME

	soundCreate();

//#elifdef DOJA
//#endif

	level = 0;
	lives = 2;
	

	sc = Scroll.instance;
}


// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{



	if (ga.playShow) { ga.playShow=false; playDraw(); }



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

public void playCreate_Gfx()
{
	System.gc();
	rabbits = loadImage("/tilesetnp.png");
	
	sc.ScrollINI(canvasWidth, canvasHeight);
	System.gc();
}

// -------------------
// play Destroy Gfx
// ===================

public void playDestroy_Gfx()
{
	rabbits = null;
	System.gc();
	sc.ScrollEND();
}

// -------------------
// play Init Gfx
// ===================

public void playInit_Gfx()
{
	
	BackgroundManager.start(this, canvasWidth, canvasHeight, mapX[level], mapY[level], rabbits);
	BackgroundManager.addStuff(level);
	RabbitManager.start(nRab[level],1,1, rabbits);
	SnakeManager.start(maxL[level], rabbits, level);
	System.gc();
}

// -------------------
// play Release Gfx
// ===================

public void playRelease_Gfx()
{
	BackgroundManager.release();
	RabbitManager.release();
	SnakeManager.release();
	System.gc();
}

// -------------------
// play Draw Gfx
// ===================

public void playDraw() {
	
	RabbitManager.clearRabbits();
	SnakeManager.clearSnake();
	RabbitManager.logic();
	
	SnakeManager.moveSnake(ga.addX, ga.addY);
	RabbitManager.drawRabbits();
	
	int k = SnakeManager.logic();

	if(k == 0) {	
		soundPlay(SOUND_EAT, 1);
	}
	
	SnakeManager.drawSnake(k);
	RabbitManager.spawnRabbits();		//respawn dead rabbits
	k = SnakeManager.snakeCollision();
	
	if(k == 0xff) {
		soundPlay(SOUND_DIE,1);
		SnakeManager.startSnakePos(level);
		
		lives--;
		if(lives < 0) {
			ga.playExit = 3;
			level = 0;
			lives = 2;
		} else {
			ga.playExit = 2;
		}
	} else {	
		sc.ScrollRUN_Centro_Max(SnakeManager.snakeX * BackgroundManager.TILE_SIZE + BackgroundManager.TILE_SIZE / 2, SnakeManager.snakeY * BackgroundManager.TILE_SIZE + BackgroundManager.TILE_SIZE / 2);
		sc.ScrollIMP(scr);
		
		//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		RabbitManager.drawRabbitsEars(this);
		
		//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
		textDraw(new String[] {"x " + (goal[level] - SnakeManager.rabbitsEaten)}, 20, -8, 0x000000, TEXT_LEFT | TEXT_BOTTOM);
		showImage(scr, rabbits, 9 * 8, 0, 8, 16, 8, canvasHeight - 8 - 16);
		
		if(SnakeManager.combon > 1) {
			if(SnakeManager.comboFTG > 0) {
		//#ifdef J2ME
				scr.setClip(0,0, canvasWidth, canvasHeight);
		//#endif
				int factor = (16 - SnakeManager.comboFTG);
				int kol = (0xff * factor) >> 4;
		//#ifdef J2ME
				scr.setClip(0, 0, canvasWidth, canvasHeight);
		//#endif
				textDraw(new String[] {SnakeManager.combon + "x Combo!"},SnakeManager.comboFTG << 1,0,(kol << 16) + (kol >> 1), TEXT_HCENTER | TEXT_VCENTER);
				SnakeManager.comboFTG--;
			} else {
				SnakeManager.combon = -1;
			}
		}
	}
	
	if(SnakeManager.rabbitsEaten == goal[level]) {
		ga.playExit = 1;
		level++;
	}
}

// <=- <=- <=- <=- <=-




	public static final int SOUND_DIE = 0;
	public static final int SOUND_EAT = 1;
	public static final int SOUND_GIRL = 2;
	public static final int SOUND_MAIN = 3;


// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//
}
