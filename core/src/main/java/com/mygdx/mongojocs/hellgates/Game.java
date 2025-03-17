package com.mygdx.mongojocs.hellgates;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================




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
final static int TEXT_CONGRATULATIONS = 14;


// *******************
// -------------------
// game - Engine
// ===================
// *******************


final static int GAME_LOGOS = 0;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
final static int GAME_PLAY = 20;
final static int LEVEL_RESTART = 40;
final static int LEVEL_CHOOSE = 41;


int gameStatus = 0;

// -------------------
// game Create
// ===================

public void gameCreate()
{
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
		logosInit(1, new int[] {0xffffff}, 2000);
		gameStatus = GAME_MENU_MAIN;
	break;	


	case GAME_MENU_MAIN:
		menuImg = gc.loadImage("/Caratula.png");
		menuInit(MENU_MAIN);		
	break;
	
	case LEVEL_CHOOSE :
		if(keyX<0 && lastKeyX==0) currentLevel = (10 + currentLevel - 1) % 10;
		if(keyX>0 && lastKeyX==0) currentLevel = (10 + currentLevel + 1) % 10;
		//if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_MAIN; break;}
		if (keyMenu > 0 && lastKeyMenu == 0) {gameStatus = LEVEL_RESTART; break;}
	break;
	
	case LEVEL_RESTART :
		lev = new level(this, currentLevel);
		pl = new player(20,120,lev);
		en = new enemy[10];		
		for(int i = 0; i<en.length; i++)
			en[i] = new enemy(400+200*i,20,enemy.DEMON,lev);		
			//en[i] = new enemy(400+200*i,200,enemy.TOAD,lev);		
		pr = new projectile[10];
		gameStatus = GAME_PLAY;
	break;

	case GAME_PLAY:
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}
		gameUpdate();
		if(pl.state == player.PL_DEAD && pl.cnt >= 30) 
			if(lives > 0) {lives--; gameStatus = LEVEL_RESTART; }
			else gameStatus = GAME_MENU_GAMEOVER;
	break;

	case GAME_MENU_SECOND:
		menuInit(MENU_SECOND);		
	break;

	case GAME_MENU_GAMEOVER:

		gc.canvasFillInit(0x000000);
		gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
		gc.gameDraw();

		waitTime(3000);

		gameStatus = GAME_MENU_MAIN;
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
		menuExit = true;		
		lives = 2; currentLevel = 0; gameStatus = LEVEL_CHOOSE;		
	break;
	
	case MENU_ACTION_CONTINUE:	// Continuar
		menuExit = true;
		gameStatus = GAME_PLAY;
	break;


	case MENU_ACTION_SHOW_HELP:		// Controles...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// About...

		menuInit(MENU_SCROLL_ABOUT);
	break;



	case MENU_ACTION_RESTART:	// Restart

		menuExit = true;		
		gameStatus = GAME_MENU_MAIN;		
	break;


	case MENU_ACTION_EXIT_GAME:	// Exit Game

		gameExit = true;		
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) {gc.soundPlay(0,0);}
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

int protX;
int protY;

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
	gc.playInit_Gfx();


	protX = 0;
	protY = 0;

//	gc.canvasTextInit(gameText[13][0]+" 1", 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);

	gc.canvasFillInit(0x000000);
	gc.canvasTextInit("Inicio del nivel este que no vale na que es una chorrada", 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
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

public boolean gameUpdate()
{
	/*protX += keyX * 4;
	protY += keyY * 4;


	if ( colision(protX, protY, 16,16,  0,gc.canvasHeight/2, 16,16) ) { playExit = 3; }


	if ( colision(protX, protY, 16,16,  gc.canvasWidth-16,gc.canvasHeight/2, 16,16) ) { playExit = 1; }*/


	if (playExit!=0) {return true;}

	playShow = true;
	
	//=============================
	
	pl.update(keyY<0,keyY>0,keyX<0,keyX>0,keyMenu==2,lastKeyY<0,lastKeyY>0,lastKeyX<0,lastKeyX>0,lastKeyMenu==2);
		
	for(int i = 0; i < en.length; i++)
		if(!en[i].outOfWorld) en[i].update(pl,this);
		
	projectileUpdate();
	
	pl.damageFromOther(en);
	pl.damageFromProjectile(pr);
	
	scrollX = pl.x-(gc.canvasWidth/2);
	scrollY = pl.y-48-(gc.canvasHeight/2);
	
	if(scrollX<0) scrollX=0;
	if(scrollY<0) scrollY=0;
	if(scrollX>=(lev.sizeX*24)-gc.canvasWidth) scrollX=(lev.sizeX*24)-gc.canvasWidth;
	if(scrollY>=(lev.sizeY*24)-gc.canvasHeight) scrollY=(lev.sizeY*24)-gc.canvasHeight;

	

	return false;
}

// <=- <=- <=- <=- <=-
// My data

level lev;
player pl;
enemy en[];
projectile pr[];
int scrollX, scrollY, lives, currentLevel;

void projectileUpdate()
{
	for(int i = 0; i<pr.length; i++)		
		if(pr[i] != null){
			pr[i].update();
			if(!pr[i].visible)	pr[i] = null;
		}	
}

void addProjectile(int x, int y, int type, int dir)
{
	int i = 0;
	
	while(i<pr.length && pr[i] != null)
		i++;
		
	if(i<pr.length) pr[i] = new projectile(x,y,type,dir);
}

// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};