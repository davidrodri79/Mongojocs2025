package com.mygdx.mongojocs.fbi;


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

int timeToChange = 0;
public void gameTick()
{
	if (timeToChange>0) timeToChange--;
	switch (gameStatus)
	{
	case GAME_LOGOS:
		gc.ProgBarEND();
		logosInit(2, new int[] {0x000000,0x000000}, 1000);
		gc.soundInit();
		gameStatus = GAME_MENU_MAIN;
		initPrefs();
		loadPrefs();
		readData();
	break;

	case GAME_MENU_MAIN:
//		menuImg = gc.loadImage("/0_Data/1_Caratula.gif");
		menuImg = gc.FS_LoadImage(0,1);
		gc.soundPlay(0,0);
		menuInit(MENU_MAIN);
		gameStatus = 45;
	break;

	case 45:
		gameStatus = 50;
		playShow = true;
	break;

	case 50: // Loading
		playShow = true;
		initRND();
		playCreate();
		gc.loadMenu();
		gc.loadGame();
		gc.soundStop();
		gameStatus = 75;

	case 75: // Level Init
		playInit();
		playExit=0;
		timeToChange = 50;
		gameStatus = 100;
		playShow = true;

	case 100: // Play
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}

		if ( !playTick() ) {break;}

		playExit=0;
		
		if (timeToChange == 0) gameStatus = 75;
	break;

	case 250 : //Game Over Cardenas LLorando sobre fondo GAME OVER
		if ((keyMisc!=0)&&(timeToChange>3)) timeToChange-=3;
		State = 7;
		playShow = true;
		if (timeToChange == 0) 
		{
			gameStatus = 260;
			timeToChange = 2*((gc.canvasWidth/2)+16);
		}
	
	break;	

	case 260 : //Game Over APARECE FREAKY
		if ((keyMisc!=0)&&(timeToChange>3)) timeToChange-=3;
		playShow = true;
		if (timeToChange == 0) 
		{
			gameStatus = 270;
			timeToChange = 80;
		}
	
	break;	
	
	case 270 : //Game Over FREAKY DICE FRASE
		if ((keyMisc!=0)&&(timeToChange>3)) timeToChange-=3;
		playShow = true;
		if (timeToChange == 0) 
		{
			gameStatus = 280;
			timeToChange = 100;
		}
	
	break;	
	
	case 280 : //Game Over APARECEN EL RESTO DE FREAKYS
		if ((keyMisc!=0)&&(timeToChange>3)) timeToChange-=3;
		playShow = true;
		if (timeToChange == 0) 
		{
			timeToChange = 100;
			gameStatus = 290;	
			gc.soundPlay(2,0);
		}
	
	break;	
	
	case 290 : //Game Over SE RIEN DE TI
		if ((keyMisc!=0)&&(timeToChange>3)) timeToChange-=3;
		playShow = true;
		if (timeToChange == 0) 
		{
			gameStatus = 500;
			gc.soundStop();
		}
	
	break;	
	
	case GAME_MENU_SECOND:
		menuInit(MENU_SECOND);
		gameStatus = 100;
	break;

	case 500 : // Preparar Scores
		gameStatus = 550;
		gc.loadScores();
		gc.ScoreScroll=0;
		timeToChange = 30;
		if ((MaxScore>RMaxScore)&&(Record==false))
		{
			NewRecord = true;
			RMaxScore = MaxScore;
			for (int Counter=0; Counter<=MaxEnem; Counter++)
			{
				RRecuento[Counter] = Recuento[Counter];	
			}
			writeData();
		}
	
	case 550 : // Principio Scores
		playShow = true;
		if ((keyMenu!=0)&&(lastKeyMenu==0))
		{
			gameStatus = GAME_MENU_MAIN;
			NewRecord = false;
			Record = false;
			MaxScore = 100;
			for (int Counter=0; Counter<=MaxEnem; Counter++)
			{
				Recuento[Counter] = 0;	
			}
		}
		if (timeToChange == 0) 
		{
			timeToChange = 25+(65*(MaxEnem+2))+10-gc.canvasHeight;
			gameStatus = 560;
		}
	break;
	
	case 560 : // Scores con Scroll
	
		if (timeToChange == 0)
		{
			gameStatus = 570;
			timeToChange = 60;
		}
		if (keyMenu!=0)
		{
			gameStatus = GAME_MENU_MAIN;
			NewRecord = false;
			Record = false;
			MaxScore = 100;
			for (int Counter=0; Counter<=MaxEnem; Counter++)
			{
				Recuento[Counter] = 0;	
			}
		}
		playShow = true;
	
	break;
	
	case 570 : // Final Scores
	
		playShow = true;
		if ((keyMenu!=0)||(timeToChange == 0))
		{
			gameStatus = GAME_MENU_MAIN;
			NewRecord = false;
			Record = false;
			MaxScore = 100;
			for (int Counter=0; Counter<=MaxEnem; Counter++)
			{
				Recuento[Counter] = 0;	
			}
			gc.loadMenu();
		}
	
	break;
	}

}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

boolean Record = false;
//byte[] prefsData;

// -------------------
// prefs INI
// ===================

public void initPrefs()
{
	actSlot = 0;
	saveSlots=1;
	prefsSize=2;
	slotSize = (4/*Puntos*/+4*(MaxEnem+1)/*Cantidad de Freaks y chicas abducidos*/);
	prefsData = new byte[prefsSize+(saveSlots*slotSize)];
}

public void writeData()
{
	actDataPos=0;
	writeInt(RMaxScore);
	for (int Counter=0; Counter<=MaxEnem; Counter++)
	{
		writeInt(RRecuento[Counter]);
	}
	updatePrefs(true);
}

public void readData()
{
	updatePrefs(false);
	actDataPos=0;
	RMaxScore 	= readInt();
	for (int Counter=0; Counter<=MaxEnem; Counter++)
	{
		RRecuento[Counter] = readInt();
	}
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
static final int MENU_ACTION_SHOW_SCORES = 8;
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
		menuListAdd(0, gameText[TEXT_SCORES][0], MENU_ACTION_SHOW_SCORES);
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

	case MENU_ACTION_SHOW_SCORES:		// Scores...
		gameStatus = 500;
		Record = true;
		menuExit = true;
	break;

	case MENU_ACTION_SHOW_HELP:		// Controles...

		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// About...

		menuInit(MENU_SCROLL_ABOUT);
	break;



	case MENU_ACTION_RESTART:	// Restart
		gameStatus = 500;
		playExit = 3;
		menuExit = true;
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

int playExit;

// -------------------
// play Create
// ===================

public void playCreate()
{
	gc.canvasFillInit(0);
	ActualLevel=-1;
	Score = 100;
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

//	gc.canvasTextInit(gameText[13][0]+" 1", 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);

	LevelChange = true;
	sinoidal = 2;
	incSinus = 1;
	
	int MaxButterFly = 3;
	butterFlyX   = new int[MaxButterFly];
	butterFlyY   = new int[MaxButterFly];
	butterFlyDX  = 2;
	butterFlyDY  = -3;
	butterFlyW   = 8;
	butterFlyH   = 8;
	butterFlyD   = new int[MaxButterFly];
	butterFlyV   = new boolean[MaxButterFly];
	
	for (int Counter = 0; Counter<butterFlyV.length; Counter++)
	{
		butterFlyX[Counter] = 0;
		butterFlyY[Counter] = 0;
		butterFlyD[Counter] = (Counter%2);
		if (butterFlyD[Counter]==0) butterFlyD[Counter]=-1;
		butterFlyV[Counter] = false;
	}
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
	if ((sinoidal==0)||(sinoidal==5)) incSinus *= -1;
	sinoidal += incSinus;
	if (LevelChange == false)
	{
		timeToChange = 50; 
		moverBurbujas();
		gestionaPuertas();
		cambiarNivel();
	} else
	{
		moverBurbujas();
		State = 5;
		if (timeToChange <= 0)
		{
			ActualLevel++;
			State = 0;
			LevelChange = false;
			gc.InitTexto(gameText[TEXT_LEVEL][0]+" "+(int)(ActualLevel+1),(gc.canvasWidth/4),(gc.canvasHeight/2)-10,0xFFFFFF,20);
		}
	}
	if (timeToEnem>0) timeToEnem--; else if (LevelChange==false) initEnem();
			
	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}

int State=0;
boolean LevelChange = false;
int ActualLevel=-1;
int MaxTime=40;
int MaxTimeToEnem=30;
int timeToEnem=10;
int[] PuertaS = new int[3];
int[] PuertaT = new int[3];
boolean[] Used = new boolean[MaxEnem];

public void moverBurbujas()
{
	for (int Counter=0; Counter<butterFlyV.length; Counter++)
	{
		if (butterFlyV[Counter]) 
		{
			butterFlyX[Counter] += butterFlyDX*butterFlyD[Counter];
			butterFlyY[Counter] += butterFlyDY;
			if ((0>(butterFlyY[Counter]+(butterFlyH/2)))||(0>(butterFlyX[Counter]+(butterFlyW/2)))||(gc.canvasWidth<(butterFlyX[Counter]-(butterFlyW/2)))) butterFlyV[Counter] = false;
		}
	}	
}

public void cambiarNivel()
{
	int Sumatorio = 0;
	for (int Counter=0; Counter<ActualLevel+1; Counter++) Sumatorio += 75*(Counter+1);
	
	if (Score>=(100+Sumatorio)) 
	{
		LevelChange = true;
		initDoors();
		gc.ActCom = RND(gameText[TEXT_LEVEL+1].length);
		gc.soundPlay(1,1);
	}
		
	if (Score<=0) 
	{
		gameStatus = 250;
		gc.loadDead();
		timeToChange = 20;
		initDoors();
		gc.ActFre = RND(MaxEnem);
		gc.ActCom = RND(gameText[TEXT_LEVEL+2+gc.ActFre].length);
	}
}

public void initDoors()
{
	for (int Counter=0; Counter<PuertaS.length; Counter++) PuertaS[Counter] = 0;
	for (int Counter=0; Counter<PuertaT.length; Counter++) PuertaT[Counter] = 0;
	for (int Counter=0; Counter<Used.length; Counter++) Used[Counter] = false;
}

public void initEnem()
{
	int[] PuertasL = new int[3];
	int Libres=0;
	if (PuertaS[0] == 0)
	{
		PuertasL[Libres] = 0;
		Libres++;	
	}
	if (PuertaS[1] == 0)
	{
		PuertasL[Libres] = 1;
		Libres++;	
	}
	if (PuertaS[2] == 0)
	{
		PuertasL[Libres] = 2;
		Libres++;	
	}
	if (Libres>0)
	{
		int ActP = PuertasL[RND(Libres)];
		PuertaS[ActP] = RND(MaxEnem+(ActualLevel/2))-(ActualLevel/2);
		if (PuertaS[ActP]<0) PuertaS[ActP] = -1; else PuertaS[ActP]++;
		while ((PuertaS[ActP]>=0)&&(Used[PuertaS[ActP]-1]))
		{
			PuertaS[ActP] = RND(MaxEnem+(ActualLevel/2))-(ActualLevel/2);
			if (PuertaS[ActP]<0) PuertaS[ActP] = -1; else PuertaS[ActP]++;
		}
		if (PuertaS[ActP]>0) Used[PuertaS[ActP]-1] = true;
		PuertaT[ActP] = RND(10)+MaxTime/(ActualLevel+1);
		if (PuertaT[ActP]<1) PuertaT[ActP] = 1;
		timeToEnem = RND(10)+MaxTimeToEnem/(ActualLevel+1);
	}
}


int Score = 100;
int MaxScore = 100;
int[] Recuento = new int[MaxEnem+1]; //0 - Tias, 1... Freaks
boolean NewRecord = false;
int RMaxScore = 100;
int[] RRecuento = new int[MaxEnem+1]; //0 - Tias, 1... Freaks
public void gestionaPuertas()
{
	State = 0;
	int KeyPre=0;
	if ((keyMisc!=0)||(keyY!=0)) KeyPre=2;
	if (keyX<0) KeyPre=1;
	if (keyX>0) KeyPre=3;
	for (int Counter=0; Counter<PuertaT.length; Counter++)
	{
		if ((PuertaS[Counter]!=0)&&(KeyPre==(Counter+1)))
		{
			State = 2+Counter;	
		}
		if (PuertaS[Counter]>=100)
		{
			PuertaS[Counter]++;
		}
		if (PuertaS[Counter]>105) 
		{
			butterFlyCreate(gc.PuertaX[Counter],gc.PuertaY[Counter]);
			PuertaS[Counter] = 0;
			PuertaT[Counter] = 0;
		} 
		if (PuertaT[Counter]>0) 
		{
			PuertaT[Counter]--;
			if (KeyPre==(Counter+1))
			{
				if ((PuertaS[Counter]>0)&&(PuertaS[Counter]<100))
				{
					gc.InitTexto("+"+((10)*(ActualLevel+1)),gc.PuertaX[Counter]-10,gc.PuertaY[Counter],0x00FF00,20);
					Score += (10)*(ActualLevel+1);
					Recuento[PuertaS[Counter]]++;
					Used[PuertaS[Counter]-1]=false;
					PuertaS[Counter] = 100;
					PuertaT[Counter] = 6;
					gc.soundPlay(3,1);
				}
				if ((PuertaS[Counter]<0)&&(PuertaS[Counter]<100))
				{
					gc.InitTexto("-"+((20)*(ActualLevel+1)),gc.PuertaX[Counter]-10,gc.PuertaY[Counter],0xFF0000,20);
					Score -= (20)*(ActualLevel+1);
					Recuento[0]++;
					PuertaS[Counter] = 100;
					PuertaT[Counter] = 6;
					gc.soundPlay(4,1);
					gc.vibraInit(100);
				}
			}
		} else 
		{
			if ((PuertaS[Counter]>0)&&(PuertaS[Counter]<100))
			{
				gc.InitTexto("-"+((5)*(ActualLevel+1)),gc.PuertaX[Counter]-5,gc.PuertaY[Counter],0xFFFF00,20);
				Score -= (5)*(ActualLevel+1);
				Used[PuertaS[Counter]-1]=false;
			}
			PuertaS[Counter] = 0;
		}
		
	}
	if (Score>MaxScore) MaxScore = Score;
}
// <=- <=- <=- <=- <=-
int butterFlyX[];
int butterFlyY[];
int butterFlyDX;
int butterFlyDY;
int butterFlyW;
int butterFlyH;
int butterFlyD[];
boolean[] butterFlyV;
int sinoidal,incSinus;
public void butterFlyCreate(int BX, int BY)
{
	int Top=0;
	for (int Counter=0; Counter<butterFlyV.length; Counter++)
	{
		if (butterFlyV[Counter]==false)
		{
			Top = Counter;
			Counter = butterFlyV.length;
		} else
		{
			if (butterFlyY[Counter] < butterFlyY[Top]) Top = Counter;
		}
	}
	butterFlyX[Top] = BX;
	butterFlyY[Top] = BY;
	butterFlyD[Top] *= -1;//(BX>(gc.canvasWidth/2)) ? 1 : -1;
	butterFlyV[Top] = true;
}


int TEXT_SCORES = TEXT_LEVEL+MaxEnem+3;
int TEXT_LOADING = TEXT_SCORES+2;

// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};