package com.mygdx.mongojocs.sexysnake;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================



//#ifdef J2ME


import com.mygdx.mongojocs.midletemu.Image;

public class Game extends Bios
{

// **************************************************************************//
// Inicio Clase Game
// **************************************************************************//

// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// ===========================================


final static int TEXT_LEVEL = 13;
final static int TEXT_LIVES = 14;
final static int TEXT_GOAL = 15;
final static int TEXT_KILLED = 16;

Image pronImage = null;

// *******************
// -------------------
// game - Engine
// ===================
// *******************


final static int GAME_LOGOS = 0;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
final static int GAME_SHOW_PRON_IMAGE = 50;
final static int GAME_PLAY = 20;

int textStatus = 0;
int gameStatus = 0;

// -------------------
// game Create
// ===================

public void gameCreate() {
}

// -------------------
// game Destroy
// ===================

public void gameDestroy()
{
}

// -------------------
// game Tick
// ===================

public void gameTick()
{
	switch (gameStatus)
	{
	case GAME_LOGOS:
		logosInit(1, new int[] {0xffffff,0xffffff}, 1000);
		gameStatus = GAME_MENU_MAIN;
		gc.soundPlay(gc.SOUND_MAIN, 0);
	break;	


	case GAME_MENU_MAIN:
		menuImg = gc.loadImage("/Caratula.png");
		menuInit(MENU_MAIN);
		gameStatus = GAME_PLAY;
	break;


	case GAME_PLAY:
		gc.soundStop();
		playCreate();
		gameStatus++;

	case GAME_PLAY+1:
		playInit();
		playExit=0;
		gameStatus++;
	break;

	case GAME_PLAY+2:
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}

		if ( !playTick() ) {break;}

		playRelease();

		gameStatus--;

		switch (playExit)
		{
		case 1:	// Pasar de Nivel
			playDestroy();
			textStatus = 1;
			gameStatus = GAME_SHOW_PRON_IMAGE;
		break;

		case 2:	// Una vida menos
			textStatus = 2;
		break;

		case 3:	// Producir Game Over
			playDestroy();
			textStatus = 1;
			gameStatus = GAME_MENU_GAMEOVER;
			break;
		}

		playExit=0;
	break;

	case GAME_MENU_SECOND:
		menuInit(MENU_SECOND);
		gameStatus = GAME_PLAY+2;
	break;

	case GAME_MENU_GAMEOVER:
		gc.canvasFillInit(0x000000);
		gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
		gc.gameDraw();

		waitTime(3000);
		
		gc.soundPlay(gc.SOUND_MAIN, 0);
		gameStatus = GAME_MENU_MAIN;
	break;
	
	case GAME_SHOW_PRON_IMAGE:
		System.gc();
		gc.canvasImg = gc.loadImage("/" + (gc.level) + "_.png");
		gc.soundPlay(gc.SOUND_GIRL, 1);
		gameStatus++;
		break;
		
	case GAME_SHOW_PRON_IMAGE + 1:
		gc.gameDraw();
	break;
	
	case GAME_SHOW_PRON_IMAGE + 2:
		gc.canvasImg = null;
		System.gc();
		
		gameStatus = GAME_PLAY + 1;
		
		gc.soundStop();
		
	//#ifdef NK-s40
	//#else
		if(gc.level == 5) {
	//#endif
			gc.level = 0;
			gc.lives = 2;		//fixes a bug after ending the game.
			gameStatus = GAME_LOGOS;
		} else {
			playCreate();
		}
		break;
	}

}

// -------------------
// game Refresh
// ===================

public void gameRefresh()
{
/*
	switch (gameStatus)
	{
	}
*/
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
	prefsData = updatePrefs(null);	// Recuperamos byte[] almacenado la ultima vez

	if (prefsData == null)
	{
		prefsData = new byte[] {1,1,0};		// Inicializamos preferencias ya que NO estaban grabadas
	}

	gameSound=prefsData[0]!=0;
	gameVibra=prefsData[1]!=0;
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);

	updatePrefs(prefsData);		// Almacenamos byte[]
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// menu - Engine
// ===================
// *******************

static final int MENU_MAIN = 0;
static final int MENU_SECOND = 1;
static final int MENU_SCROLL_HELP = 2;
static final int MENU_SCROLL_ABOUT = 3;


static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;

int menuType;
int menuTypeBack;

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuTypeBack = menuType;
	menuType = type;

	menuExit = false;

	menuListInit(0, 0, gc.canvasWidth, gc.canvasHeight);


	switch (type)
	{
	case MENU_MAIN:
		menuListClear();
		menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_SECOND:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;


	case MENU_SCROLL_HELP:
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Screen();
	break;


	case MENU_SCROLL_ABOUT:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Screen();
	break;
	}


	biosStatus = 22;
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{

	switch (cmd)
	{
	case -3: // Scroll ha sido cortado por usario
	case -2: // Scroll ha llegado al final
		menuInit(menuTypeBack);
	break;


	case MENU_ACTION_PLAY:		// Jugar de 0
	case MENU_ACTION_CONTINUE:	// Continuar

		menuExit = true;
	break;


	case MENU_ACTION_SHOW_HELP:		// Controles...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// About...

		menuInit(MENU_SCROLL_ABOUT);
	break;



	case MENU_ACTION_RESTART:	// Restart

		playExit = 3;
		
		gc.lives = 2;
		gc.level = 0;
		
		menuExit = true;
	break;


	case MENU_ACTION_EXIT_GAME:	// Exit Game

		gameExit = true;
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) {gc.soundPlay(gc.SOUND_MAIN,1);} // ???
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListOpt() != 0;
	break;
	}

}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// play - Engine
// ===================
// *******************

boolean playShow;

int playExit;

//int protX;
//int protY;
int addX,addY;

// -------------------
// play Create
// ===================

public void playCreate()
{
	gc.canvasFillInit(0);

	gc.playCreate_Gfx();
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	gc.playDestroy_Gfx();
}

// -------------------
// play Init
// ===================

public void playInit()
{
	String str;
	gc.playInit_Gfx();
	
	addX = 0;
	addY = 0;
	
	gc.canvasFillInit(0x000000);
	
	str = gameText[TEXT_LEVEL][0] + " " + (gc.level + 1) + " " + gameText[TEXT_LIVES][0] + " " + (gc.lives + 1) + "\n" + gameText[TEXT_GOAL][0] + " " + gc.goal[gc.level];
	
	if(textStatus == 2) {
		str = gameText[TEXT_KILLED][0] + "\n" + str;
	}
	
	gc.canvasTextInit(str, 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
	gc.gameDraw();
	
	waitTime(3000);
}

// -------------------
// play Release
// ===================

public void playRelease()
{
	gc.playRelease_Gfx();
}

// -------------------
// play Tick
// ===================

public boolean playTick()
{
	addX = keyX;
	addY = keyY;
	/*
	if(keyX > 0) {
		addX = 1;
	} else {
		addX = 0;
	}
	protX += keyX * 4;
	protY += keyY * 4;
*/
/*
	if ( colision(protX, protY, 16,16,  0,gc.canvasHeight/2, 16,16) ) { playExit = 3; }


	if ( colision(protX, protY, 16,16,  gc.canvasWidth-16,gc.canvasHeight/2, 16,16) ) { playExit = 1; }
*/

	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}

// <=- <=- <=- <=- <=-





// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};