package com.mygdx.mongojocs.foootballmobile;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Random;

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
final static int GAME_CHOOSE_TEAM = 40;
final static int GAME_CHOOSE_STRATEGY = 41;
final static int GAME_VIEW_TOURNAMENT = 42;
final static int GAME_NEW_MATCH = 43;
final static int GAME_HALF_TIME = 44;
final static int GAME_VERSUS = 45;
final static int GAME_SEE_RESULTS = 46;
final static int GAME_PROCESS_RESULTS = 47;
final static int GAME_HALFTIME_WAIT = 48;
final static int GAME_CURRENT_TOURNAMENT = 49;
final static int GAME_GOAL_WAIT = 50;
final static int GAME_PENALTIES_WAIT = 51;
final static int GAME_ENDGAME_WAIT = 52;
final static int GAME_CUP_WON = 53;


int gameStatus = 0, lastStatus, cnt = 0;
int cameraX, cameraY, matchTime, myTeam, oppTeam, gameDuration = 0, tourScrollX, tourScrollY, myFormation=0, oppFormation;
boolean secondHalf, cheatOn = false;
tournament tour;

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
	lastStatus = gameStatus;
	cnt++;
	
	switch (gameStatus)
	{
	case GAME_LOGOS:
		logosInit(1, new int[] {0xffffff}, 2500);
		gameStatus = GAME_MENU_MAIN;
	break;	


	case GAME_MENU_MAIN:
		/*gc.canvasFillInit(0x000000);
		gc.canvasFill(0x000000);		
		gc.canvasTextInit(gameText[21][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_MEDIUM);
		gc.gameDraw();*/		
		gc.destroyMatchGfx();
		menuImg = gc.pictureImg;		
		gc.soundPlay(0,0);
		menuInit(MENU_MAIN);		
	break;	
	
	case GAME_NEW_MATCH:
		gc.canvasFillInit(0x000000);
		gc.canvasFill(0x000000);		
		gc.canvasTextInit(gameText[21][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_MEDIUM);
		gc.gameDraw();	
		gc.destroyMatchGfx();
		gc.loadMatchGfx();
		matchTime = 0; secondHalf = false;
		goalsA = 0; goalsB = 0;				
		gameStatus = GAME_CHOOSE_STRATEGY;
	break;
	
	case GAME_CURRENT_TOURNAMENT:
		if(keyX > 0) tourScrollX-=3;
		if(keyX < 0) tourScrollX+=3;
		if(keyY > 0) tourScrollY-=3;
		if(keyY < 0) tourScrollY+=3;
		if(tourScrollX<-128) tourScrollX=-128;
		if(tourScrollX>128) tourScrollX=128;
		if(tourScrollY<-64) tourScrollY=-64;
		if(tourScrollY>64) tourScrollY=64;
		if(keyMenu != 0 && lastKeyMenu == 0)
		{
			gameStatus = GAME_MENU_MAIN;
		}
		playShow = true;
	break;
	
	case GAME_GOAL_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=30) {continueMatch(); gameStatus = GAME_PLAY;}		
		playShow = true;		
	break;	
	
	case GAME_HALFTIME_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=30) {gameStatus = GAME_HALF_TIME;}		
		playShow = true;		
	break;
	
	case GAME_PENALTIES_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=30) {gameStatus = GAME_PLAY; beginPenaltyRound();}		
		playShow = true;		
	break;
	
	case GAME_ENDGAME_WAIT:
		if (keyMenu != 0 && lastKeyMenu == 0 && cnt>=30)
		{
			gc.soundPlay(2,1); 			
			if(goalsA == goalsB){
				gc.soundPlay(4,1);
				gameStatus = GAME_PENALTIES_WAIT;
			}else{ 																	
				tour.simulate();						
				tour.setGoals(myTeam,goalsA);
				tour.setGoals(oppTeam,goalsB);														
				gameStatus = GAME_SEE_RESULTS;
			}
		}		
		playShow = true;		
	break;
	
	case GAME_HALF_TIME:
		matchTime = 0; secondHalf = true;
		gc.soundPlay(1,0);
		gameStatus = GAME_CHOOSE_STRATEGY;
	break;
	
	case GAME_CHOOSE_TEAM:
		
		if(keyX > 0 && lastKeyX == 0) myTeam = (4*(myTeam/4)) + ((myTeam+1)%4);
		if(keyX < 0 && lastKeyX == 0) myTeam = (4*(myTeam/4)) + ((4+myTeam-1)%4);
		if(keyY > 0 && lastKeyY == 0) myTeam = (myTeam+4)%16;
		if(keyY < 0 && lastKeyY == 0) myTeam = (16+myTeam-4)%16;
		if(keyMenu != 0 && lastKeyMenu == 0){
			
			tour = new tournament(5);
			gc.soundPlay(1,0);			
			gameStatus = GAME_VIEW_TOURNAMENT; 			
			tourScrollY = - 20*tour.current + gc.canvasHeight/2;
		}
		playShow = true;
	break;
	
	case GAME_VIEW_TOURNAMENT:
		if(tourScrollX>-(130 - gc.canvasWidth/2) && keyX > 0) tourScrollX-=3;
		if(tourScrollX<(130 - gc.canvasWidth/2) && keyX < 0) tourScrollX+=3;
		if(tourScrollY>-(60 - gc.canvasHeight/2) && keyY > 0) tourScrollY-=3;
		if(tourScrollY<(60 - gc.canvasHeight/2) && keyY < 0) tourScrollY+=3;
	
		if(keyMenu != 0 && lastKeyMenu == 0)
		{
			oppTeam = tour.myOpp(myTeam); gameStatus = GAME_VERSUS;
		}
		playShow = true;		
	break;
	
	case GAME_VERSUS:
		if(keyMenu != 0 && lastKeyMenu == 0)
			gameStatus = GAME_NEW_MATCH;
		playShow = true;
	break;
	
	case GAME_SEE_RESULTS :
		menuInit(MENU_SEE_RESULTS);				
	break;
	
	case GAME_CUP_WON:
		if(keyMenu != 0 && lastKeyMenu == 0)
			gameStatus = GAME_MENU_MAIN;
		playShow = true;
	break;
	
	case GAME_PROCESS_RESULTS :
	if(goalsA > goalsB){
			tour.step();					
			if(tour.current>0){
				savePrefs();
				gameStatus = GAME_VIEW_TOURNAMENT;	
			}else{
				tour.current=5;
				savePrefs();
				gc.destroyMatchGfx();
				gc.loadCupGfx();
				gameStatus = GAME_CUP_WON;	
			}			
		}else gameStatus = GAME_MENU_GAMEOVER;
		menuExit = true;
		//playShow = true;
	break;
	
	case GAME_CHOOSE_STRATEGY:
		if(keyX > 0 && lastKeyX == 0) myFormation = (myFormation+1)%6;
		if(keyX < 0 && lastKeyX == 0) myFormation = (6+myFormation-1)%6;
		if(keyY > 0 && lastKeyY == 0) myFormation = (myFormation+1)%6;
		if(keyY < 0 && lastKeyY == 0) myFormation = (6+myFormation-1)%6;
		if(keyMenu != 0 && lastKeyMenu == 0){
			playExit=0;					
			oppFormation = RND(6);
			if(secondHalf) halfMatch(myFormation,oppFormation); else initMatch(myFormation,oppFormation,4-tour.current);			
			gameStatus = GAME_PLAY;	
			menuExit = true;
			gc.soundStop();						
		}
		playShow = true;			
	break;

	case GAME_PLAY:
		if ((keyMenu == -1 && lastKeyMenu == 0) || backFromCall) {gameStatus = GAME_MENU_SECOND; break;}
		
		matchTime++;
				
		matchTick();
		
		int cx, cy;
		
		if(ball.theOwner == null) { cx = ball.x; cy = ball.y; }
		else { 
			cx = ball.theOwner.x + (unit.cos(32*(ball.theOwner.direction - 2))>>6); 
			cy = ball.theOwner.y + (unit.sin(32*(ball.theOwner.direction - 2))>>6);
		}
		
		cameraX += (cx - cameraX)/4;
		cameraY += (cy - cameraY)/4;

		if(matchState != PENALTIES && matchState < WAITCORNER){
			if(matchTime>durationTicks()) 
				if(secondHalf){ 
													
					/*if(goalsA == goalsB){
						gc.soundPlay(4,1);
						gameStatus = GAME_PENALTIES_WAIT;
					}else{ 																	
						tour.simulate();						
						tour.setGoals(myTeam,goalsA);
						tour.setGoals(oppTeam,goalsB);														
						gameStatus = GAME_ENDGAME_WAIT;
					}*/
					gameStatus = GAME_ENDGAME_WAIT;
				
				}else{gc.soundPlay(5,1); gameStatus = GAME_HALFTIME_WAIT;}
			
			if(matchState == GOAL) {gc.soundPlay(6,1); gc.vibraInit(200); gameStatus = GAME_GOAL_WAIT;}
		}
		
		if(cheatOn)
		{
			if(keyMisc ==35 && lastKeyMisc != keyMisc) 
				goalsA++;
			if(keyMisc ==42 && lastKeyMisc != keyMisc) 
				goalsB++;
			if(keyMisc ==10 && lastKeyMisc != keyMisc) 
				matchTime = 99999;
		}
					
		playShow = true;		
	break;

	case GAME_MENU_SECOND:
		menuInit(MENU_SECOND);		
	break;

	case GAME_MENU_GAMEOVER:


		gc.canvasFillInit(0x000000);
		gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE| gc.TEXT_BOLD);
		gc.gameDraw();

		waitTime(3000);

		gameStatus = GAME_MENU_MAIN;
	break;
	}
	
	if(lastStatus != gameStatus) {cnt = 0; gc.ledFr = 0;}
	backFromCall = false;

}

int durationTicks()
{
	switch(gameDuration)
	{
		//default : return 5*16;
		default : return 60*25;	
		case 1 : return 150*16;
		case 2 : return 300*16;
	}	
}

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

public void loadPrefs()
{
	gameSound = true; gameVibra = false; gameDuration = 1; myFormation = 0; myTeam = 0;
	tour = new tournament(5);
	
	try{
	
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
    	DataOutputStream ostream = new DataOutputStream(bstream);
														
		ostream.writeBoolean(gameSound);
		ostream.writeBoolean(gameVibra);
		ostream.writeBoolean(gameKeyconf);
		ostream.writeInt(gameDuration);
		ostream.writeInt(myFormation);
		
		ostream.writeInt(myTeam);
		tour.write(ostream);
								
    	ostream.flush();
    	ostream.close();
                	
    	prefsData = bstream.toByteArray();
    
    }catch(Exception e){}	
		
	updatePrefs(false);
	
	try{
	
 		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(prefsData, 0, prefsData.length));
                    		
		gameSound=istream.readBoolean();
		gameVibra=istream.readBoolean();
		gameKeyconf=istream.readBoolean();
		gameDuration=istream.readInt();
		myFormation=istream.readInt();
		
		myTeam = istream.readInt();
		tour.read(istream);
			  		
	}catch(Exception e){}

}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	try{
	
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
    	DataOutputStream ostream = new DataOutputStream(bstream);				
														
		ostream.writeBoolean(gameSound);
		ostream.writeBoolean(gameVibra);
		ostream.writeBoolean(gameKeyconf);
		ostream.writeInt(gameDuration);
		ostream.writeInt(myFormation);
		
		ostream.writeInt(myTeam);
		tour.write(ostream);

    	ostream.flush();
    	ostream.close();
                	
    	prefsData = bstream.toByteArray();
    
    }catch(Exception e){}

	updatePrefs(true);
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
static final int MENU_SEE_RESULTS = 5;


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
		if(tour.current<4) menuListAdd(0, gameText[19], 11);		
		if(tour.current<4) menuListAdd(0, gameText[15], 12);
		menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		if (gc.deviceKeyconf) {menuListAdd(0, gameText[23], 14, gameKeyconf?1:0);}
		menuListAdd(0, gameText[18], 10, gameDuration);
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
		if (gc.deviceKeyconf) {menuListAdd(0, gameText[23], 14, gameKeyconf?1:0);}
		//menuListAdd(0, new String[] {"Cheat Off","Cheat On"}, 13, cheatOn?1:0);
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;
		
	case MENU_SCROLL_HELP:
		menuListClear();
		menuListAdd(0, gameText[(gameKeyconf ? 24 : TEXT_HELP_SCROLL)]);
		menuListSet_Screen();
	break;

	case MENU_SCROLL_ABOUT:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Screen();
	break;
	
	case MENU_SEE_RESULTS:
		menuListClear();
		menuListAdd(0, gameText[20][tour.current-1]);		
		for(int i = 0; i<tour.evol[tour.current].length; i+=2){
			menuListAdd(0," ");
			menuListAdd(0,gameText[16][tour.evol[tour.current][i]]+"  "+tour.goals[tour.current][i]);			
			menuListAdd(0,gameText[16][tour.evol[tour.current][i+1]]+"  "+tour.goals[tour.current][i+1]);
		}
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
		if(menuType == MENU_SEE_RESULTS) {menuExit = true; gc.soundPlay(1,0); gameStatus = GAME_PROCESS_RESULTS;}
		else menuInit(menuTypeBack);
	break;


	case MENU_ACTION_PLAY:		// Jugar de 0
		menuExit = true;
		menuImg = null;
		gameStatus = GAME_CHOOSE_TEAM; 		
		myTeam = 0;
	break;
	
	case MENU_ACTION_CONTINUE:	// Continuar
		gameStatus = GAME_PLAY;	
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
		
	/*case 9 :		
		playExit=0;		
		myFormation = menuListPos; 
		oppFormation = RND(6);
		if(secondHalf) halfMatch(myFormation,oppFormation); else initMatch(myFormation,oppFormation,4-tour.current);
		gameStatus = GAME_PLAY;	
		menuExit = true;
		gc.soundStop();
	break;*/
	
	case 10 :
		gameDuration = (gameDuration+1)%3;
	break;
	
	case 11 :
		menuImg = null;
		menuExit = true;
		gc.soundPlay(1,0);
		gameStatus = GAME_VIEW_TOURNAMENT;	
	break;
	
	case 12 :
		menuImg = null;
		menuExit = true;		
		gameStatus = GAME_CURRENT_TOURNAMENT;
	break;
	
	case 13:
		cheatOn = menuListOpt() != 0;
	break;
	
	case 14:
		gameKeyconf = menuListOpt() != 0;
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

/*int protX;
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

public boolean playTick()
{
	protX += keyX * 4;
	protY += keyY * 4;


	if ( colision(protX, protY, 16,16,  0,gc.canvasHeight/2, 16,16) ) { playExit = 3; }


	if ( colision(protX, protY, 16,16,  gc.canvasWidth-16,gc.canvasHeight/2, 16,16) ) { playExit = 1; }


	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}*/

// <=- <=- <=- <=- <=-





// **************************************************************************//
// Final Clase Game Carlos
// **************************************************************************//


int nfc;
static int formation[][][] = {
    {{0,0},{0,2},{0,4},{0,6},{1,2},{1,4},{2,0},{2,6},{3,2},{3,4}},
    {{0,0},{0,2},{0,4},{0,6},{1,1},{1,3},{1,5},{2,2},{2,4},{3,3}},
    {{0,1},{0,3},{0,5},{1,2},{1,4},{2,0},{2,3},{2,6},{3,2},{3,4}},
    {{0,0},{0,2},{0,3},{0,4},{0,6},{1,1},{1,3},{1,5},{2,3},{3,3}},
    {{0,1},{0,3},{0,5},{1,2},{1,4},{2,0},{2,6},{3,1},{3,3},{3,5}},
    {{0,2},{0,4},{1,1},{1,3},{1,5},{3,0},{3,2},{3,3},{3,4},{3,6}}};

int matchState;
final static int GAME       = 0;
final static int PENALTIES  = 1;
final static int ENDGAME    = 2;
final static int GOAL       = 3;
final static int WAITCORNER = 4;
final static int WAITOUT    = 5;
final static int WAITGOAL    = 6;
int theTeam;
int wait;
soccerPlayer theTeam2[];

final static int groundWidth  = 90*2;//220;
final static int groundHeight = 120*2;//288;
final static int hgroundWidth  = groundWidth/2;
final static int hgroundHeight = groundHeight/2;
final static int INFINITE = 10000;
final static int goalSize = 34;
final static int hgoalSize = goalSize/2;
final static int goalPostSize = 3;
final static int TEAMA = 0;
final static int TEAMB = 1;

soccerPlayer teamA[] = new soccerPlayer[10];
soccerPlayer teamB[] = new soccerPlayer[10];
goalKeeper gkA, gkB;
ball ball;
int teamDown;	
soccerPlayer strikeTeam[];

Random rnd = new Random();
int goalsA, goalsB;
int pointer;



public void goal(int team)
{
    //GOL 
    humanSoccerPlayer = null;
    
    
    if (team == TEAMA)
    {
        goalsA++;
        strikeTeam = teamB;       
        ball.activate(hgroundWidth, hgroundHeight);     
   }
   else
   {
        //GOL 
        goalsB++;
        strikeTeam = teamA;
        ball.activate(hgroundWidth, hgroundHeight);            
   }
   setTeamsState(soccerPlayer.PREMATCH);
   gkA.state = goalKeeper.FREE;
   gkB.state = goalKeeper.FREE;       
   if (matchState == PENALTIES) nextPenalty();
   if (matchState == GAME) matchState = GOAL;
   
}

public void continueMatch()
{
    matchState = GAME;
    firstPass = true;
}


int outSide;
int outSideV;

public void out(soccerPlayer[] outTeam)
{    
    humanSoccerPlayer = null;    
    outSide = ball.x < hgroundWidth?0:groundWidth-1;
    if (outTeam == teamA) strikeTeam = teamB;           
    else strikeTeam = teamA;
    if (ball.y < 0) ball.y = 1;
    if (ball.y >= groundHeight)  ball.y = groundHeight-1;
    ball.activate(outSide , ball.y);                    
    setTeamsState(soccerPlayer.PREOUT);        
}

public void corner(soccerPlayer[] cornerTeam)
{    
    humanSoccerPlayer = null;    
    outSide = ball.x < hgroundWidth?0:groundWidth-1;
    outSideV = ball.y < hgroundHeight?0:groundHeight-1;
    ball.activate(outSide , outSideV);                    
    setTeamsState(soccerPlayer.PRECORNER);    
    strikeTeam = cornerTeam;    
}




public void halfMatch(int formationA, int formationB)
{
    firstPass = true;
    humanSoccerPlayer = null;
    ball.activate(hgroundWidth, hgroundHeight);
    strikeTeam = teamB;
    teamDown = TEAMB;	
    int side = (groundWidth / 8);
    int line = (groundHeight / 10);
    for(int i = 0;i < 10;i++)    
        teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA);         
    gkA.activate(hgroundWidth, 6, TEAMA, hgoalSize);        
    for(int i = 0;i < 10;i++)    
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB);         
    gkB.activate(hgroundWidth, 6, TEAMB, hgoalSize);
    gkA.state = goalKeeper.FREE;
    gkB.state = goalKeeper.FREE;
}


boolean firstPass;

public void initMatch(int formationA, int formationB, int difficulty)
{
    pointer = groundWidth/2;
    firstPass = true;
    goalsA = 0;
    goalsB = 0;
    matchState = GAME;
    teamDown = TEAMA;	
    strikeTeam = teamA;
    
    switch (difficulty)
    {
        default:        
            soccerPlayer.ENEMYSPEED = 256;
            soccerPlayer.CHI = 48;
            break;
        case 1:
            soccerPlayer.ENEMYSPEED = 352;
            soccerPlayer.CHI = 32;            
            break;
        case 2:
            soccerPlayer.ENEMYSPEED = 448;
            soccerPlayer.CHI = 16;            
            break;
        case 4:
            soccerPlayer.ENEMYSPEED = 512;
            soccerPlayer.CHI = 0;
            break;        
    }
    


    ball = new ball(this);
    ball.activate(hgroundWidth, hgroundHeight);
    
    int side = (groundWidth / 8);
    int line = (groundHeight / 10);
    for(int i = 0;i < 10;i++)
    {
        teamA[i] = new soccerPlayer(this, i, ball);
        teamA[i].activate((formation[formationA][i][1]+1)*side, (formation[formationA][i][0]+1)*line, TEAMA);     
    }
    gkA = new goalKeeper(this);
    gkA.activate(hgroundWidth, 6, TEAMA, hgoalSize);
           
    for(int i = 0;i < 10;i++)
    {
        teamB[i] = new soccerPlayer(this, i, ball);
        teamB[i].activate((formation[formationB][i][1]+1)*side, (formation[formationB][i][0]+1)*line, TEAMB);     
    }
    gkB = new goalKeeper(this);
    gkB.activate(hgroundWidth, 6, TEAMB, hgoalSize);
    nfc = 0;
    humanSoccerPlayer = null;
    
}


public void removeMatch()
{
    ball = null;
    gkA = null;
    gkB = null;
    for(int i = 0;i < 10;i++)
    {
        teamA[i] = null;
        teamB[i] = null;
    }    
}

int nPenalty;
int nPP;
int restA, restB;

public void beginPenaltyRound()
{
    matchState = PENALTIES;
    nPenalty = 0;
    restA = 5;
    restB = 5;
    int primens = (rnd.nextInt() & 256) ;
    if (primens >= 128)
    {
        strikeTeam = teamA;
        teamDown = TEAMA;
    }
    else
    {
        strikeTeam = teamB;
        teamDown = TEAMB;
    }
    ball.activate(hgroundWidth, groundHeight/8);    
    setTeamsState(soccerPlayer.PREPENALTY);
    gkA.state = soccerPlayer.PREPENALTY;
    gkB.state = soccerPlayer.PREPENALTY;
    
}


public void nextPenalty()
{
    nPenalty++;    
    nPP = (nPenalty/2) % 10;
    if (restA == 0 && restB == 0) {restA = 1;restB = 1;}
    ball.activate(hgroundWidth, groundHeight/8);          
    setTeamsState(soccerPlayer.PREPENALTY);
    gkA.state = soccerPlayer.PREPENALTY;
    gkB.state = soccerPlayer.PREPENALTY;
    if (teamDown == TEAMA)
    {
        restA--;
        strikeTeam = teamB;
        teamDown = TEAMB;
    }
    else
    {
        restB--;
        strikeTeam = teamA;
        teamDown = TEAMA;
    }
    if (goalsA > goalsB + restB || goalsB > goalsA + restA) matchState = ENDGAME;
    //if () playExit = 3;    
}

int preMatchState;


public void precorner(soccerPlayer[] _team)
{    
    humanSoccerPlayer = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = WAITCORNER;
    wait = 5;
    gc.soundPlay(4,1);
}

public void preout(soccerPlayer[] _team)
{
    humanSoccerPlayer  = null;
    preMatchState = matchState;
    theTeam2 = _team;
    matchState = WAITOUT;
    wait = 5;
    gc.soundPlay(4,1);
}

public void pregoal(int _team)
{
    humanSoccerPlayer  = null;
    preMatchState = matchState;
    theTeam = _team;
    matchState = WAITGOAL;
    wait = 5;
}



public void matchTick()
{
    if (matchState == GOAL || matchState == ENDGAME) return;
    if (matchState >= WAITCORNER) 
    {
        humanSoccerPlayer = null;
        wait--;
        ball.update();
        if (matchState == WAITGOAL)
        {
             if (ball.x <= hgroundWidth - (hgoalSize) + goalPostSize
             || ball.x > hgroundWidth + (hgoalSize) - goalPostSize)             
             {
                ball.fx -= ball.sx;
                ball.sx = 0;      
             }
             if (ball.y < -9) ball.y = -9;
             if (ball.y > groundHeight+9) ball.y = groundHeight+9;
        }
        if (wait <= 0)
        {
            int tmp = matchState;
            matchState = preMatchState;
            switch(tmp)
            {
                case WAITCORNER:                        
                        corner(theTeam2);                        
                        break;
                case WAITOUT:
                        out(theTeam2);
                        break;
                case WAITGOAL:
                        goal(theTeam);
                        break;        
            }
        }
        return;
    }
    nfc++;    
    //if (keyMenu != 0 && lastKeyMenu == 0) beginPenaltyRound();//halfMatch(3,4);
    if ((nfc+goalSize/4) % goalSize < hgoalSize) pointer+=2;
    else pointer-=2;
    
    ball.update();    
    updatePlayers();	
    
    if (ball.y < 0)
    { 
        if (ball.x > hgroundWidth - (hgoalSize) && ball.x <= hgroundWidth + (hgoalSize))
        {
            if (ball.x <= hgroundWidth - (hgoalSize) + goalPostSize
             || ball.x > hgroundWidth + (hgoalSize) - goalPostSize)
            {
                ball.fy = 0;
                ball.sy = -(ball.sy*65)/100;                
            }
            else 
                pregoal(teamDown);
        }
        else
        {
            //FUERA DE PORTERIA O CORNER
            if (teamDown == TEAMB) 
            {
                if (strikeTeam == teamA)                            
                    precorner(teamB);        
                else {ball.theOwner =null;gkA.takeBall();}
            }
            else
            { 
                if (strikeTeam == teamB)                            
                    precorner(teamA);                    
                else {ball.theOwner =null;gkB.takeBall(); }
            }            
        }
    }
    else if (ball.y >= groundHeight)
    { 
        if (ball.x > (hgroundWidth) - (hgoalSize) && ball.x <= (hgroundWidth) + (hgoalSize))
        {
            if (ball.x <= hgroundWidth - (hgoalSize) + goalPostSize
             || ball.x > hgroundWidth + (hgoalSize) - goalPostSize)
             {
                ball.fy = (groundHeight-1)<<8;             
                ball.sy = -(ball.sy*65)/100;
             }
             else 
                pregoal(1-teamDown);            
        }
        else 
        {            
            //FUERA DE PORTERIA O CORNER
            if (teamDown == TEAMA) 
            {
                if (strikeTeam == teamA)                            
                    precorner(teamB);                        
                else {ball.theOwner =null;gkA.takeBall(); }
            }
            else
            { 
                if (strikeTeam == teamB)                            
                    precorner(teamA);                    
                else {ball.theOwner =null;gkB.takeBall(); }
            }                        
        }
    }
    else if (ball.x < 0 || ball.x >= groundWidth)     preout(strikeTeam);
    
}	


public void setTeamsState(int st)
{
        for(int i = 0;i < 10;i++)
        {
            teamA[i].setState(st);        
            teamB[i].setState(st);                
        }
}

int nformed = 0;
soccerPlayer humanSoccerPlayer;

/*public void updateSoccerPlayer(int ip)
{
    soccerPlayer pUpdate;
    pUpdate = teamA[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;
    pUpdate = teamB[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;        
}*/



public void updatePlayers()
{
    if (matchState == PENALTIES)
    {          
        if (strikeTeam[nPP].formed && (gkA.wait == -10 || gkB.wait == -10))
        {
            strikeTeam[nPP].state = soccerPlayer.PENALTY;
            gkA.timing = 0;gkB.timing = 0;
           //System.out.println("bien");
        }
        if (gkA.state == goalKeeper.OWNBALL || gkB.state == goalKeeper.OWNBALL) nextPenalty();
    }
    
    for(int i = 0;i < 10;i++)
    {        
        if (teamA[i].state == soccerPlayer.FOLLOW || teamA[i].state == soccerPlayer.OWNBALL|| teamA[i].state == soccerPlayer.PENALTY) humanSoccerPlayer = teamA[i];                                        
        else 
           teamA[i].update();        
        teamB[i].update();                
    }
        
    gkA.update(ball);
    gkB.update(ball);    
    
    if (humanSoccerPlayer != null) humanSoccerPlayer.manualUpdate(ball, keyX, keyY, keyMenu == 2 && lastKeyMenu == 0);//

   
    
    
    
    
    int ip = nfc%10;
    if (ip == 0) nformed = 0;
    
    soccerPlayer pUpdate;
    pUpdate = teamA[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;
    pUpdate = teamB[ip];
    pUpdate.myStatus = pUpdate.status(pUpdate);    
    if ((pUpdate.state == soccerPlayer.PREMATCH) && pUpdate.formed) nformed++;        
    
    //updateSoccerPlayer(ip*2);
    //updateSoccerPlayer((ip*2)+1);
        
        
    if (nformed >= 19 && strikeTeam[9].state == soccerPlayer.PREMATCH)
    {
    	 gc.soundPlay(4,1);
         setTeamsState(soccerPlayer.FREE); 
         strikeTeam[9].takeBall();               
         /*for (int i = 0;i < 10;i++)
         {
            pUpdate = teamA[i];
            pUpdate.myStatus = pUpdate.status(pUpdate);    
            pUpdate = teamB[i];
            pUpdate.myStatus = pUpdate.status(pUpdate);              
         } */   
    }
    
    if (matchState != PENALTIES)
    {
        if (gkA.state != gkA.OWNBALL && gkB.state != gkB.OWNBALL)
        {
            if (nfc%2 == 0 && (humanSoccerPlayer == null || (keyX == 0 && keyY == 0)))//AKI AKI!!!!
            {
                int fPlayer = closestPlayer(teamA);
                if (fPlayer != -1)
                {
                    if (teamA[fPlayer].state == soccerPlayer.FREE)
                    {
                        for(int i = 0; i < 10;i++)
                            if (teamA[i].state == soccerPlayer.FOLLOW) teamA[i].state = soccerPlayer.FREE;
                        teamA[fPlayer].setState(soccerPlayer.FOLLOW);
                    }
                }
            }
            if (nfc%2 == 1)
            {    
                int fPlayer = closestPlayer(teamB);
                if (fPlayer != -1)
                {
                    if (teamB[fPlayer].state == soccerPlayer.FREE)
                    {
                        for(int i = 0; i < 10;i++)
                            if (teamB[i].state == soccerPlayer.FOLLOW) teamB[i].state = soccerPlayer.FREE;
                        teamB[fPlayer].setState(soccerPlayer.FOLLOW);
                    }
                }
            }
        }
        else 
        {
            //UN PORTERO TIENE LA PELOTA
            setTeamsState(soccerPlayer.FREE);                       
        }
    }    
}



public int closestPlayer(soccerPlayer team[])
{
    int thePlayer = -1;
    int minDist = INFINITE;
    for(int i = 0; i < 10;i++)
    {
        if (team[i].state == soccerPlayer.WAIT) continue;
        int d = team[i].isBallClose();
        if (d < minDist)
        {
            minDist = d;
            thePlayer = i;
        }
    }
    return thePlayer;
}



public int distance(unit a, unit b)
{
    int dx = a.x - b.x;
    int dy = a.y - b.y;    
    int angle = unit.atan(dy, dx);
    int deno = unit.sin(angle);
    if (deno == 0) return Math.abs(dx);
	int d = ((dy << 10) / deno );      	
    return Math.abs(d);
}



public int distanceX(unit a, unit b)
{
    return Math.abs(a.x - b.x);
}

public int distanceY(unit a, unit b)
{
    return Math.abs(a.y - b.y);
}


};