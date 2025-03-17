package com.mygdx.mongojocs.frozenworld;


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
final static int TEXT_TIPS = 15;
final static int TEXT_BEGINLEVEL = 16;
final static int TEXT_SCRIPT = 17;
final static int TEXT_RESTARTSTAGE = 18;
final static int TEXT_CONTROL = 21;

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

public void gameTick()
{
	switch (gameStatus)
	{
	case GAME_LOGOS:
		logosInit(1, new int[] {0xffffff}, 2000);
		gameStatus = GAME_MENU_MAIN-2;
	break;	
	case GAME_MENU_MAIN-2:
	    menuImg = gc.loadImage("/Caratula.png");
	    gc.soundPlay(0,0);
		gc.canvasFillShow = true;gc.canvasFillRGB=0x000000;
		gc.canvasImg = menuImg;
		gameStatus++;
	case GAME_MENU_MAIN-1:
		gc.gameDraw();
		//if (keyMenu != 0 && lastKeyMenu == 0)
		if (keyCode != 0 && lastKeyCode == 0)
			gameStatus++;
		break;		
	case GAME_MENU_MAIN:	    
		menuImg = null;System.gc();		
		menuImg = gc.loadImage("/final2.png");
		menuFillShow = true;menuFillRGB=0x000000;
		menuInit(0);
		gameStatus +=3;
	break;

	case GAME_MENU_MAIN+3:
		if (gameStage > 0) menuInit(20);
	 	gameStatus = GAME_PLAY;
		break;

	case GAME_PLAY:
		playCreate();
		gameStatus++;

	case GAME_PLAY+1:
		gc.cargaIntro(stage);	
		gc.soundPlay(1,1);			
		gameStatus = GAME_PLAY+3;		
	break;

	case GAME_PLAY+2:
		if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_SECOND; break;}

		if ( !playTick() ) {break;}

		playRelease();

		gameStatus--;

		switch (playExit)
		{
		case 1:	// Pasar de Nivel
			playShow = false;
			stage++;
			if (stage == 16) gameStatus = GAME_PLAY+50;
			else
			{
				gameStage = (byte)Math.max(stage, gameStage); 
				savePrefs();				
			}
			break;
		case 2:	// Una vida menos
			playShow = false;			
			break;
		case 3:	// Producir Game Over
			playShow = false;
			playDestroy();
			gameStatus = GAME_MENU_GAMEOVER;		
			break;
		}

		playExit=0;
	break;
	
	case GAME_PLAY+3:
		//gc.drawIntro(stage);
		playIntro = true;
		playShow = false;		
		gc.gameDraw();
		if (keyCode != 0 && lastKeyCode == 0) gameStatus++;		
		break;
	
	case GAME_PLAY+4:
		playInit(stage);
		playExit=0;
		gameStatus = GAME_PLAY+2;
		break;
	case GAME_PLAY+50:
		gameStatus++;
		playDestroy();
		gc.imgEnemies = gc.loadImage("/final.png");
		gc.canvasFillInit(0x000000);
		//gc.canvasTextInit(gameText[TEXT_CONGRATULATIONS][0], 0,gc.canvasHeight, 0xFFFFFF, gc.TEXT_BOTTON|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
		gc.canvasImg = gc.imgEnemies;
		gc.canvasShow = true;
		playEnd = true;
		gc.gameDraw();
	case GAME_PLAY+51:					
		if (keyMisc != 0 && lastKeyMisc == 0) 		
		{
			gameStatus = GAME_MENU_GAMEOVER;
			gc.imgEnemies = null;System.gc();
		}		
		break;	
		
	case GAME_MENU_SECOND:
	    gc.lasttip = -1;
		menuImg = null;System.gc();		
	    menuImg = gc.loadImage("/fabrica.png");
	    menuFillShow = true;menuFillRGB=0x000000;	
		menuInit(1);
		gameStatus = GAME_PLAY+2;
		break;

	case GAME_MENU_GAMEOVER:
		playShow = false;
		gc.canvasFillInit(0x000000);
		gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
		gc.gameDraw();

		waitTime(3000);

		gameStatus = GAME_MENU_MAIN;
		gc.soundPlay(0,0);
	break;
	}

}

// <=- <=- <=- <=- <=-




//waitStart()
//waitStop();


public void waitTime(int time)
{
	try {
		Thread.sleep(time);
	} catch (Exception e) {}
}





// *******************
// -------------------
// menu - Engine
// ===================
// *******************

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
	case 0:
		menuListClear();
		menuListAdd(0, gameText[TEXT_PLAY], 0);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], 10, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], 11, gameVibra?1:0);}
		//menuListAdd(0, gameText[TEXT_CONTROL], 12, gameControl?1:0);
		menuListAdd(0, gameText[TEXT_HELP], 6);
		menuListAdd(0, gameText[TEXT_ABOUT], 7);
		menuListAdd(0, gameText[TEXT_EXIT], 9);
		menuListSet_Option();
	break;

	case 1:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], 1);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], 10, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], 11, gameVibra?1:0);}
		//menuListAdd(0, gameText[TEXT_CONTROL], 12, gameControl?1:0);
		menuListAdd(0, gameText[TEXT_HELP], 6);
		menuListAdd(0, gameText[TEXT_RESTARTSTAGE], 50);
		menuListAdd(0, gameText[TEXT_RESTART], 8);
		menuListAdd(0, gameText[TEXT_EXIT], 9);		
		menuListSet_Option();
	break;


	case 6:
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Screen();
	break;


	case 7:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Screen();
	break;


	case 8:
		menuListClear();
		menuListAdd(0, gameText[TEXT_GAMEOVER]);
		menuListSet_Text();
	return;

	case 20:
		menuListClear();		
		for(int i = 0; i <= gameStage; i++)
			menuListAdd(0, gameText[TEXT_LEVEL][0]+"  "+(i+1), 30+i);		
		menuListSet_Option(gameStage);
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

	case 0:	// Jugar de 0
	case 1:	// Continuar
		menuExit = true;
	break;

	case 5:	// Siguiente Nivel
	break;


	case 6:	// Controles...
		menuInit(6);
	break;

	case 7:	// About...
		menuInit(7);
	break;


	case 8:	// Restart
		playExit = 3;
		menuExit = true;
	break;

	case 9:	// Exit Game
		gameExit = true;
	break;	

	case 10:
		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) {gc.soundPlay(0,0);}
	break;

	case 11:
		gameVibra = menuListOpt() != 0;
	break;
	
	case 12:
		gameControl = menuListOpt() != 0;
	break;
	
	case 30:
	case 31:
	case 32:
	case 33:
	case 34:
	case 35:
	case 36:
	case 37:
	case 38:
	case 39:
	case 40:
	case 41:
	case 42:
	case 43:
	case 44:
	case 45:		
			stage = cmd-30;
			menuExit = true;
			break;
	case 50: playExit = 2;
	          prota.nshots = 0;
			  menuExit = true;
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

// -------------------
// play Create
// ===================

public void playCreate()
{
    menuImg = null;System.gc();		
	gc.canvasFillInit(0);

	gc.playCreate_Gfx();
}

// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	stage = 0;
	prota.nshots = 0;
	gc.playDestroy_Gfx();
}

// -------------------
// play Init
// ===================

public void playInit(int Type)
{
	gc.cargaFase(Type);
	gc.playInit_Gfx();
	menuImg = null;		

    gc.canvasImg = gc.loadImage("/fabrica.png");
	gc.canvasFillInit(0x000000);
	//gc.canvasTextInit("Inicio del nivel este que no vale na que es una chorrada", 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
	gc.canvasTextInit(gameText[TEXT_LEVEL][0] + " "+ (stage+1)+": "+ gameText[TEXT_BEGINLEVEL][stage], 0,32, 0xFFFFFF, gc.TEXT_TOP|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
	gc.gameDraw();

	waitTime(4000);

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

boolean freezed, crack;

public boolean playTick()
{
	
	
	 nfc++;
    
    
    /////////////////////////////
    //RASTER
    ///////////////////////////////
    for(int i = 0;i < gc.sc.FondoSizeY;i++)
    for(int j = 0;j < gc.sc.FondoSizeX;j++)
    {  
	    int y = gc.sc.ScrollY/24 + i;        
	    int x = gc.sc.ScrollX/24 + j;
	    if ((y*gc.MAPW)+x >= gc.map.length) break;
	    int tile = gc.map[(y*gc.MAPW) + x];
	    if (tile >= gc.BEGDESTROY && tile < gc.BEGDESTROY+2)
	    {
		    tile++;
		    if (tile >= gc.BEGDESTROY+2) unit.SetAir(x*unit.TILED, y*unit.TILED);
		    else gc.map[(y*gc.MAPW) + x] = (byte)tile;            
	    }
	    if (tile >= gc.BEGCREATE && tile < gc.BEGCREATE+2)
	    {
		    tile++;
		    if (tile >= gc.BEGCREATE+2) gc.sc.FaseMap[(y*gc.MAPW) + x] = (byte)gc.LST_ICE[prota.pipo];    
		    else gc.map[(y*gc.MAPW) + x] = (byte)tile;            
	    }
	    if (nfc%2 == 0 && tile >= gc.BEGGEISER && tile <= gc.BEGGEISER+2)
	    {
		    tile++;
		    if (tile > gc.BEGGEISER+2) tile = gc.BEGGEISER; 
		    gc.map[(y*gc.MAPW) + x] = (byte)tile;
	    }
	     if (prota.faseacabada > 25 && gc.inList(gc.LST_ICE,tile)) gc.map[(y*gc.MAPW) + x] = (byte)gc.BEGDESTROY;	    
    }
    if (prota.faseacabada == 1) gc.soundPlay(5,1);
    //////////////////////////////////////////
  
  	gc.showtip = -1;
	if (gc.tips != null)
	{
		for(int i = 0; i < gc.tips.length;i++)
		{
			    if (gc.tips[i].done) continue;
			    gc.uTip.x = gc.tips[i].x*unit.TILED;
			    gc.uTip.y = gc.tips[i].y*unit.TILED;
			    if (prota.isCollidingWith(gc.uTip))
			    {
				    /*SetPrint(0xffffff, (canvasWidth>>1)+1,TITLEY, GHCENTER);		    
				    goodPrint(tips[i].text, canvasHeight - getTextHeight(tips[i].text) - 3 - LINEHEIGHT);*/
				    gc.showtip = i;			    
			    }
		 }
	}
  
    freezed = !(prota.lastCell == 0 && prota.tag > 0 && gc.showtip == -1); 
    
    if(!freezed)
    {
	      if (stage == 7)
	      {
		    if  (nfc%NEUT == 0)
		    {
			    for(int i = 0;i < gc.MAPW;i++)
				    if (prota.Test( i*unit.TILED, neu*unit.TILED, gc.AIR)) prota.Set(i*unit.TILED, neu*unit.TILED, gc.BEGCREATE);
			    if (neu > 0) neu--;
		    }
		    if (neu*unit.TILED < prota.y + 8) prota.Mata();
	    }
	    if (stage == 14)
	    {
		    	for(int i = 0;i < malo.length;i++)
			{
				  if (!malo[i].active)
				  {
					  malo[i].Activate(gc.sc.FaseMap, (16*unit.TILED) -3 , (5*unit.TILED)  -3);
					  malo[i].incy = 1;				  
				  }
				  if (prota.faseacabada > 0)
				  {					  
					  if (malo[i].x < unit.TILED*14) malo[i].x += unit.TILED>>1;				  
					  if (malo[i].x > unit.TILED*14) malo[i].x -= unit.TILED>>1;
					  if (malo[i].y < unit.TILED*4) malo[i].y += unit.TILED>>1;
					  if (malo[i].y > unit.TILED*4) malo[i].y -= unit.TILED>>1;
				  }
			}			  
	    }
	    if (stage == 15)
	    {
		        crack = true;
		    	int desp[] = {0,1,2,2,3,3,2,2,1};
		    	for(int i = 0;i < malo.length;i++)
			{
				if (malo[i] instanceof malo6)
				{
					malo[i].y = ((4+i)*unit.TILED);
					malo6 m6 = (malo6)malo[i];
					m6.maxmoving = 200;
					if (!m6.bloqueado) {crack = false;
						malo[i].x = (5*unit.TILED) + (desp[(nfc+(i*3))%desp.length]*(unit.TILED/8));
					}
				}					
				//if (m6.moving > 10)  m6.moving = -100;								 
			}
			if (crack) for(int i = 0;i < malo.length;i++) if (malo[i].active) malo[i].active = false;
			//else malo[i].active = true;
	    }
	    	/*if (prota.grounded && prota.jump == -1 && keyY < 0 && lastKeyY < 0)
		{keyX = 0;}*/
	    
	    	if (keyMisc==10 && lastKeyMisc == 0)
		    {
			    prota.fireShot();
		    }
		    if (keyMenu > 0 && lastKeyMenu == 0) 
			prota.fireFor();//prota.Fire(0, 0);
		    else if (keyY > 0 && lastKeyY == 0 && prota.grounded)
		    {			 
			prota.FireDown();
		    }
		    else if (keyY > 0 && lastKeyY > 0 &&  prota.grounded)
		    {
			    if (Math.abs(gc.ty - gc.offsety) < 48) gc.offsety += 2;
		    }
		    else
		    {
			    if (keyX  != 0 && lastKeyX == 0) prota.Move(keyX*1, keyY*4, lastKeyY);
			    else prota.Move(keyX*3, keyY*4, lastKeyY);
		    }			    		    		    
		    prota.myPos();
		    for(int i = 0; i< malo.length;i++) if (malo[i] != null && malo[i].active)
		    {
			    if (malo[i].canKill(prota)) {prota.Mata();}
			    malo[i].Update(prota);			    
		    }
		    if (nbreeds > 0 && nfc%80 == 0) Breeds();
		    if (nspits > 0 && nfc%80 == 40) Spits();
    }		    
    if (gc.showtip != -1)
    {
	  if (keyCode != 0 && lastKeyCode == 0)
	  	  {gc.tips[gc.showtip].done = true;gc.lasttip = -1;} 
    }
    if (prota.tag <= 0)
    {
	    if (prota.tag == 0) {prota.Auto(prota.x, prota.y, -prota.sidex*100, -750);gc.soundPlay(9,1);}
	    prota.ashape = ((prota.ashape/5)*5)+2;
	    prota.desp = 4 + (nfc%2);
	    prota.tag--;
	    prota.inmunity = 0;	    
	    prota.incy += 55;
	    prota.ActAuto();
    }
	playShow = true;

	if (prota.faseacabada > 35) //gc.cargaFase(++stage);
		playExit = 1;
	if (prota.tag == -30 ) {playExit = 2;prota.nshots = 0;}
				
	//if (lastKeyMisc != Canvas.KEY_STAR && keyMisc == Canvas.KEY_STAR) playExit = 1;//
	if (stage == 15 && crack && prota.x < 3*unit.TILED) playExit = 1;
	//c.cargaFase(14);
	
	
	
	
	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}

// <=- <=- <=- <=- <=-


boolean playIntro, playEnd;
prota prota;
unit malo[];// = new unit[10];
int nbatteryes;
int breed[][] = {{-1,-1}, {-1, -1}, {-1, -1}};
int nbreeds;
int spit[][] = {{-1,-1,2}, {-1, -1,2}, {-1, -1,2}, {-1, -1,2}, {-1, -1,2}, {-1, -1,2}};
int nspits;


int stage;
int neu;
final static int NEUT = 38;

final static int enemies[][][] = 
{
	{} ,
	{{5,0,0}},	
	{{4,6,6}, {1,5,1}},
	{{3,11,1},{3,14,3},{3,17,5},{4, 9, 10},{2,0,0},{2,0,0},{5,0,0},{5,0,0}},
	{{2,0,0}, {2,0,0}, {2,0,0}, {2,0,0}},
	{{6,6,11}},	
	{{5,0,0},{4,8,24},{6,11,29},{4,6,7},{5,0,0},{3,14,2},{7,8,9}},
	{{5,0,0},{5,0,0},{5,0,0},{5,0,0},{5,0,0},{5,0,0}},
	{{4,19,14},{7,12,9},{2,0,0},{1,9,5},{6,21,9}},
	{{5,0,0},{5,0,0},{3,10,3},{7,42,1},{7,18,4},{3,45,9},{3,31,8}},
	{{4,8,14},{6,11,6},{6,11,10},{6,6,10},{6,6,6},{6,3,14},{6,9,8}},
	{{7,14,11},{7,8,10},{7,3,6},{7,18,2},{7,9,2},{5,0,0}},
	{{3,21,10},{3,21,18},{3,21,25},{6,17,13},{2,0,0},{2,0,0}},				
	{{2,0,0},{2,0,0},{2,0,0},{1,8,10},{1,6,6}, {1,3,14}},
	{{6,16,5},{6,8,7},{6,14,15},{6,25,12},{6,22,18},{6,19,4}},
	{{6,1,1},{6,1,1},{6,1,1},{6,1,1},{6,1,1},{2,0,0}}
	//{{1,7,3} , {1,12,9}, {2,0,0},{2,0,0},{3, 11,5},{7, 8,10},{5,0,0}}
};



int currentBreed;

public void Breeds()
{
    currentBreed = (++currentBreed)%nbreeds;
    boolean trobat = false;int j = 0;
    while (j < malo.length && !trobat)         
    {            
        if (malo[j] != null && !malo[j].active && malo[j] instanceof malo2) 
        {    
            malo[j].Activate(gc.map, breed[currentBreed][0]*unit.TILED,breed[currentBreed][1]*unit.TILED);
            trobat = true;
        }
        j++;
    }
}

public void Spits()
{
	for(int i = 0; i < nspits;i++) {	
		boolean trobat = false;
		int j = 0;
		while (j < malo.length && !trobat) {         		            
			if (malo[j] != null && !malo[j].active && malo[j] instanceof malo5) {    			 
				malo[j].Activate(gc.map, (spit[i][0]*unit.TILED)-2,(spit[i][1]*unit.TILED)-3);
				malo[j].incx = spit[i][2];
				trobat = true;
			}
			j++;
		}
	}
}



int nfc;
//byte stringCheat[] = {7,6,6,6,9,9,9,6,6,6,-6};
byte stringCheat[] = {2,2,2,7,7,7,9,9,9,7,8,-6};


// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};