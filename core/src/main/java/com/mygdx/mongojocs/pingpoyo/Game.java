package com.mygdx.mongojocs.pingpoyo;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================




//import javax.microedition.lcdui.*;
import java.io.*;


public class Game extends Bios
{

// **************************************************************************//
// Inicio Clase com.mygdx.mongojocs.sanfermines2006.Game
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
final static int PLAYER_FIRST = 100;
final static int OPPONENT_FIRST = 101;
final static int FIRST_FAIL = 102;
final static int PLAYER_POINT = 103;
final static int OPPONENT_POINT = 104;
final static int NEXT_SERVICE = 105;
final static int YOU_WIN = 106;
final static int YOU_LOSE = 107;
final static int GAME_INIT = 108;
final static int LOAD_INTRO = 109;
final static int INTRO = 110;
final static int LOAD_MENU_MAIN = 111;
final static int VERSUS = 112;
final static int CHOOSE_OPP = 113;
final static int TOURNAMENT_END = 114;
final static int SHOW_TIP = 115;
final static int VIEW_RECORDS = 116;
final static int PLAYER_SERVICE = 117;
final static int OPPONENT_SERVICE = 118;
final static int IDLE = 200;


int gameStatus = 0;

// -------------------
// game Create
// ===================

public void gameCreate()
{	
	gameText[TEXT_ABOUT_SCROLL][1]=gameText[TEXT_ABOUT_SCROLL][1]+getAppProperty("MIDlet-Version");
	inputDialogCreate(gameText[28][0],gameText[29][0],gameText[30][0],3);
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

public static final int MATCH_POINTS = 11;
public static final int MATCH_NUMBER = 7;

public static final int TOURNAMENT_MODE = 0;
public static final int PRACTICE_MODE = 1;
public static final int TUTORIAL_MODE = 2;

public Ball ball;
public Player pl;
public Opponent op;
public int lastGameStatus, cnt, playerScore, opponentScore, servicesDone, servicesFailed,  round, lastRound, available, gameMode, tipRequest;
public long ellapsedTicks, lastRoundTime, recordTimes[] = new long[3];
public boolean playerTurn, updateScore;
public static boolean  gameAuto=true, doneTips[] = new boolean[10], backFromCall = false;
public String recordNames[] = new String[3];

public void setState(int s)
{
	cnt=0; lastGameStatus=gameStatus; gameStatus=s;	
}

public boolean anyKey()
{
	return (keyMenu != 0 && keyMenu!=lastKeyMenu) || (keyX != 0 && lastKeyX != keyX) || (keyY != 0 && lastKeyY != keyY);	
}

public void gameTick()
{
	if(gameStatus!=GAME_MENU_MAIN && gameStatus!=GAME_MENU_SECOND) cnt++;
	
	updateScore = false;
		
	switch (gameStatus)
	{
	case GAME_LOGOS:
		/*gc.canvasFillInit(0x000000);		
		gc.canvasTextInit(gameText[43][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_MEDIUM);
		gc.gameDraw();*/
		gc.loadInitialGfx();	
		logosInit(1, new int[] {0xffffff}, 2000);		
		setState(LOAD_INTRO);
	break;	
	
	case LOAD_INTRO :
		gc.loadIntro(); gc.soundPlay(1,0); setState(INTRO);
	break;
	
	case INTRO :
		if((keyMenu != 0 && keyMenu != lastKeyMenu) || (cnt>500))
		{			
			setState(LOAD_MENU_MAIN);				
		}
		playShow = true;
	break;
	
	case LOAD_MENU_MAIN :
		gc.canvasFillInit(0x000000);		
		gc.canvasTextInit(gameText[43][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_MEDIUM);
		gc.gameDraw();	
		
		gc.destroyIntro();
		gc.destroyMatchGfx();		
		gc.destroyOppGfx();

//int a=0; while (a==0){}

		gc.loadPicture();
		setState(GAME_MENU_MAIN);
	break;

	case GAME_MENU_MAIN:
		gc.canvasTextShow = false;
		gc.canvasFillShow = false;		
		gc.soundPlay(0,0);
		menuInit(0);			
	break;
	
	case VERSUS :
		if(cnt==1) gc.soundPlay(2,0);
		if(anyKey()){
			gc.soundStop();
			setState(GAME_INIT);
		}
		playShow = true;
	break;
	
	case CHOOSE_OPP :
		if(keyX != 0 && lastKeyX!=keyX)
			round=(available+round+keyX)%available;			
		if(keyMenu > 0 && keyMenu != lastKeyMenu)		
			setState(VERSUS);		
		if(keyMenu < 0 && keyMenu != lastKeyMenu)		
			setState(GAME_MENU_MAIN);					
		playShow = true;
	break;
	
	case GAME_INIT :
	
		gc.canvasFillInit(0x000000);		
		gc.canvasTextInit(gameText[43][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_MEDIUM);
		gc.gameDraw();	
		
		ball = new Ball();
		pl = new Player();
		op = new Opponent(round);		
		playerScore = 0; opponentScore = 0;
		playerTurn = true; servicesDone = 0; servicesFailed = 0;
		ellapsedTicks = 0;		
		gc.destroyPicture();
		//gc.destroyMatchGfx();		
		gc.destroyOppGfx();
		gc.loadMatchGfx();		
		
		setState(NEXT_SERVICE);
	break;
	
	case PLAYER_FIRST :
	if(cnt==1) updateScore = true;
	ball.update();
	pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);
	ball.x = pl.x;
	if(cnt>=20 || anyKey())
		setState(PLAYER_SERVICE);
	playShow = true;
	break;
	
	case OPPONENT_FIRST :
	ball.update();
	pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);
	if(cnt>=20 || anyKey())
		setState(OPPONENT_SERVICE);
	playShow = true;
	break;
	
	case PLAYER_SERVICE :
		if ((keyMenu == -1 && lastKeyMenu == 0) || backFromCall) {setState(GAME_MENU_SECOND); backFromCall = false; break;}				
		ball.update();
		pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);
		op.update(ball,pl);	
		if(pl.state == Player.RELEASE)
		{		
			int or=0;
			if(pl.x == -128) or=0;
			if(pl.x == 0) or=1;
			if(pl.x == 128) or=2;
	
			gc.soundPlay(6,1);
			gc.vibraInit(200);
			
			if(ball.y>-15) {
				ball.setNetPhysics(); 
				servicesFailed++;
				setState(FIRST_FAIL);
			}
			else if(ball.y<-58){
				ball.setTooHighPhysics(); 
				servicesFailed++;
				setState(FIRST_FAIL);
			}
			else {
				ball.setPhysics(or,pl.direction+3,0,true);				
				servicesFailed=0;
				servicesDone++;
				setState(GAME_PLAY);
			}
			
		}else{
			ball.x = pl.x;
		}
		playShow = true;
	break;
	
	case OPPONENT_SERVICE :
		if ((keyMenu == -1 && lastKeyMenu == 0) || backFromCall) {setState(GAME_MENU_SECOND); backFromCall = false; break;}				
		ball.update();
		pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);
		if(cnt>=45) op.update(ball,pl);	
		if(cnt==45){
			 op.x = RND(3)-1;
			 ball.setPhysics(op.x+4,op.x+4,0,true);
			 op.setState(Opponent.FIRST);
		}
		if(op.state==Opponent.RELEASE && op.cnt==1)
		{						
			servicesFailed=0;
			servicesDone++;		
			ball.setPhysics(op.x+4,RND(3),0,true);
			gc.soundPlay(7,1);
			setState(GAME_PLAY);						
		}
		playShow = true;
	break;	


	case GAME_PLAY:		
		ellapsedTicks++;
		if ((keyMenu == -1 && lastKeyMenu == 0) || backFromCall) {setState(GAME_MENU_SECOND); backFromCall = false; break;}				
		ball.update();
		pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);
		op.update(ball,pl);
				
		int or=0;
		
		// Player return
		if(ball.z<-104 && ball.z>-144 && pl.state == Player.RELEASE){
			
			if(pl.x == -128) or=0;
			if(pl.x == 0) or=1;
			if(pl.x == 128) or=2;
			
			if(ball.to == or){
										
					//System.out.println("Desde "+or+" hasta "+(pl.direction+3)+" con fuerza "+(ball.speed+(pl.charged/5)));
					if(pl.charged>=5){
						//System.out.println("Charged:"+pl.charged);
						ball.setMove(or,pl.direction+3,pl.charged/3);
						gc.soundPlay(6,1);
						gc.vibraInit(200);
					}else{
						ball.setNetPhysics(); 
						servicesFailed=2;
						gc.soundPlay(6,1);
						gc.vibraInit(200);
						setState(FIRST_FAIL);
						servicesDone--;
					}
				}
			}
		
		// Opponent return
		if(ball.z>128 && ball.z<144 && op.state == Opponent.RELEASE) 
			if((ball.to==3 && op.x == -1) ||
				(ball.to==5 && op.x == 1) ||
				(ball.to==4 && op.x == 0)){
					
					if(op.x == -1) or=3;
					if(op.x == 0) or=4;
					if(op.x == 1) or=5;
					
					ball.setMove(or,op.direction,op.charged/3);
					gc.soundPlay(7,1);
				}
				
		// My point
		if(ball.z>192)
		{
			playerScore++; updateScore = true;
			setState(PLAYER_POINT);
			gc.soundPlay(3,1);
		}
		playShow = true;				
				
		// Opponent point
		if(ball.z<-192)
		{
			opponentScore++; updateScore = true;
			setState(OPPONENT_POINT);
			gc.soundPlay(3,1);
		}
		playShow = true;
	break;
	
	case FIRST_FAIL :	
		ball.update();
		pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);		
		if(cnt>=60 || (keyMisc == 5 && keyMisc!=lastKeyMisc)){
						
			if(servicesFailed==2){
				
				opponentScore++; updateScore = true; servicesFailed = 0;
				//if(lastGameStatus==PLAYER_FIRST) 
				servicesDone++;
				setState(OPPONENT_POINT);
				
			}else setState(NEXT_SERVICE);
			
		}
		playShow = true;
	break;
	
	case PLAYER_POINT :
	pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);		
	if(cnt>=80 || anyKey()){
			
			op.IAreset();
			op.setState(Opponent.IDLE);
			setState(NEXT_SERVICE);			
		}
		playShow = true;
	break;
	
	case OPPONENT_POINT :
	pl.update(ball,keyX,lastKeyX,keyY,keyMenu,keyMisc);		
	if(cnt>=80 || anyKey()){
			
			op.IAreset();
			op.setState(Opponent.IDLE);
			setState(NEXT_SERVICE);
		}
		playShow = true;
	break;
	
	case NEXT_SERVICE :
		
	if(playerScore>=MATCH_POINTS && playerScore>=opponentScore+2){
		
		// Player Won!	
		gc.destroyMatchGfx();		
		gc.destroyOppGfx();
		setState(YOU_WIN); 
		break;
		
	}else if(opponentScore>=MATCH_POINTS && opponentScore>=playerScore+2){
		
		// Opponent Won!
		gc.destroyMatchGfx();		
		setState(YOU_LOSE); 
		break;
		
	}else if(playerScore>=MATCH_POINTS && opponentScore>=MATCH_POINTS){
		
		if(servicesDone>=1){
			
			playerTurn = !playerTurn; servicesDone = 0;	
		}
				
	}else if(servicesDone>= ((opponentScore>=MATCH_POINTS || playerScore>=MATCH_POINTS) && Math.abs(opponentScore-playerScore)<2? 2 : 5)){
		
		playerTurn = !playerTurn; servicesDone = 0;
	}
	
	// Who's service?
	
	if(playerTurn){
		
		ball.setPhysics(0,0,0,false);
		ball.y = -60; ball.vely = 0;		
		ball.x = pl.x;
		op.setState(Opponent.IDLE);		
		setState(PLAYER_FIRST);			
		
	}else{
		
		op.setState(Opponent.IDLE);		
		setState(OPPONENT_FIRST);		
	}
	break;
	
	case YOU_WIN :		
		if(cnt==1) gc.soundPlay(4,0);
		if(anyKey()) {
			if(gameMode == TOURNAMENT_MODE){
				round++;
				if(round < MATCH_NUMBER){
					lastRound = round;
					if(available < round+1) available = round + 1;
					lastRoundTime += ellapsedTicks;
					savePrefs(); 
					setState(VERSUS);
				}else{
					if(ellapsedTicks<recordTimes[2]){
						inputDialogInit();
						setState(IDLE);
					}else{
						
					 	setState(TOURNAMENT_END);
					}
				}
			}else{
				setState(GAME_MENU_GAMEOVER);
			}
		}
		if(gameStatus != YOU_WIN) gc.soundStop();
		playShow = true;
	break;
	
	case YOU_LOSE :		
		if(cnt==1) gc.soundPlay(5,0);		
		if(gameMode == TOURNAMENT_MODE){
			if(keyMenu==-1 && lastKeyMenu==0) 
				setState(GAME_MENU_GAMEOVER);				
			if(keyMenu==1 && lastKeyMenu==0){
				lastRoundTime += ellapsedTicks; 
				savePrefs();
				setState(GAME_INIT);			
			}
		}else{
			if(anyKey()) 
				setState(GAME_MENU_GAMEOVER);			
		} 
				
		if(gameStatus != YOU_LOSE) gc.soundStop();				
		playShow = true;
	break;
	
	case TOURNAMENT_END :
		if (cnt == 1) gc.soundPlay(1,0);
		if(anyKey()){
			 
			 gc.soundStop();
			 setState(GAME_MENU_GAMEOVER);			 
		}
		playShow = true;
	break;
	
	case SHOW_TIP :
		if(anyKey()) {
			 setState(lastGameStatus);
			 doneTips[tipRequest]=true; tipRequest=-1;
		}
		playShow = true;
	break;
	
	case VIEW_RECORDS :
	if(anyKey()) {
		gameStatus = GAME_MENU_MAIN;
	}
	playShow = true;
	break;
	
	case IDLE :
		playShow = true;
	break;

	/*case GAME_PLAY+1:
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
		break;

		case 2:	// Una vida menos
		break;

		case 3:	// Producir com.mygdx.mongojocs.sanfermines2006.Game Over
			playDestroy();
			gameStatus = GAME_MENU_GAMEOVER;
		break;
		}

		playExit=0;
	break;*/

	case GAME_MENU_SECOND:
		menuInit(1);		
	break;

	case GAME_MENU_GAMEOVER:		
		gc.canvasFillInit(0x000000);		
		gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER | gc.TEXT_BOLD | gc.TEXT_MEDIUM);
		gc.gameDraw();

		waitTime(3000);
		
		savePrefs(); 
		gameStatus = LOAD_MENU_MAIN;
	break;	
	}
	
	// Tutorial Mode
	if(gameMode == TUTORIAL_MODE)
	if(gameStatus >= GAME_PLAY && gameStatus <= NEXT_SERVICE)
	{
		if(cnt>2) doTipRequest(0);
		
		if(gameStatus == FIRST_FAIL && doneTips[1]) doTipRequest(3);
		if(gameStatus == FIRST_FAIL && cnt>40) doTipRequest(1);		
		if(gameStatus == GAME_PLAY && ball.z>0) doTipRequest(2);
		if(gameStatus == GAME_PLAY && ball.velz < 0) doTipRequest(5);
		if(gameStatus == GAME_PLAY && ball.velz < 0 && gameAuto) doTipRequest(4);
		if(gameStatus == PLAYER_POINT) doTipRequest(7);
		if(gameStatus == PLAYER_POINT) doTipRequest(6);
		if(gameStatus == OPPONENT_POINT) doTipRequest(8);
		if(gameStatus == OPPONENT_FIRST) doTipRequest(9);
		
		if(tipRequest >= 0)
			setState(SHOW_TIP);			
	}

}

public void doTipRequest(int id)
{
		if(!doneTips[id]) tipRequest = id;
}

public void inputDialogNotify(String s)
{
	int pos=3;
	
	while(pos>0 && (lastRoundTime + ellapsedTicks)<recordTimes[pos-1]) pos--;
	
	for(int i = 1; i>=pos; i--){
		
		recordTimes[i+1] = recordTimes[i];
		recordNames[i+1] = recordNames[i];
	}
	
	recordTimes[pos] = (lastRoundTime + ellapsedTicks);
	recordNames[pos] = s.toUpperCase();
			
	setState(TOURNAMENT_END);
}

public void gameUpdate()
{
	gc.doRepaintMenu=true;
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
		if(lastRound!=0) menuListAdd(0, gameText[19], 12);
		menuListAdd(0, gameText[TEXT_PLAY], 0);		
		menuListAdd(0, gameText[20], 13);
		menuListAdd(0, gameText[21], 14);
		menuListAdd(0, gameText[40], 16);		
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], 10, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], 11, gameVibra?1:0);}
		if (gc.deviceKeyconf) {menuListAdd(0, gameText[42], 17, gameKeyconf?1:0);}
		menuListAdd(0, gameText[22], 15, gameAuto?1:0);
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
		if (gc.deviceKeyconf) {menuListAdd(0, gameText[42], 17, gameKeyconf?1:0);}
		menuListAdd(0, gameText[22], 15, gameAuto?1:0);
		menuListAdd(0, gameText[TEXT_HELP], 6);
		menuListAdd(0, gameText[TEXT_RESTART], 8);
		menuListAdd(0, gameText[TEXT_EXIT], 9);
		menuListSet_Option();
	break;


	case 6:
		menuListClear();
		for(int i=0; i<19; i++)
			menuListAdd(0, gameText[TEXT_HELP_SCROLL][i + (gameKeyconf ? 0 : 19)]);
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

	case 0:	// Torneo nuevo
		menuExit = true;
		gc.soundStop(); round = 0; lastRound = 0; lastRoundTime = 0; gameMode = TOURNAMENT_MODE;
		setState(VERSUS);
	break;
	case 12:// Continuar Torneo
		menuExit = true;
		gc.soundStop(); round = lastRound; gameMode = TOURNAMENT_MODE;
		setState(VERSUS);
	break;
	case 13:// Entrenamiento
		menuExit = true;
		gc.soundStop(); round = 0; gameMode = PRACTICE_MODE;
		setState(CHOOSE_OPP);
	break;	
	case 14:// Tutorial
		menuExit = true; tipRequest=-1; for(int i=0; i<doneTips.length; i++) doneTips[i] = false;
		gc.soundStop(); round = 0; gameMode = TUTORIAL_MODE;
		setState(GAME_INIT);
	break;	
	case 16:
		menuExit = true;
		gc.soundStop(); 
		setState(VIEW_RECORDS);
	break;
	case 1:	// Continuar
		menuExit = true;
		gameStatus = lastGameStatus;
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
		//playExit = 3;		
		menuExit = true;
		setState(LOAD_MENU_MAIN);				
	break;

	case 9:	// Exit com.mygdx.mongojocs.sanfermines2006.Game
		gameExit = true;
	break;	

	case 10:
		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) {gc.soundPlay(0,0);}
	break;

	case 11:
		gameVibra = menuListOpt() != 0;
	break;
	
	case 17:
		gameKeyconf = menuListOpt() != 0;
	break;	
	
	case 15:
		gameAuto = menuListOpt() != 0;
	break;
	}

}

// <=- <=- <=- <=- <=-

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{
	gameSound = true; gameVibra = false; gameAuto = true; available = 1; lastRound = 0; lastRoundTime = 0;
	recordTimes[0] = 148500; recordTimes[1] = 148500; recordTimes[2] = 148500;
	recordNames[0] = "MIC"; recordNames[1] = "ROJ"; recordNames[2] = "OCS";
	
	try{
	
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
    	DataOutputStream ostream = new DataOutputStream(bstream);				
														
		ostream.writeBoolean(gameSound);
		ostream.writeBoolean(gameVibra);
		ostream.writeBoolean(gameKeyconf);
		ostream.writeBoolean(gameAuto);
		ostream.writeInt(available);
		ostream.writeInt(lastRound);
		ostream.writeLong(lastRoundTime);
		for(int i = 0; i<recordTimes.length; i++)
			ostream.writeLong(recordTimes[i]);
		for(int i = 0; i<recordNames.length; i++)
			ostream.writeUTF(recordNames[i]);			
							
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
		gameAuto=istream.readBoolean();
		available=istream.readInt();
		lastRound=istream.readInt();
		lastRoundTime=istream.readLong();
		for(int i = 0; i<recordTimes.length; i++)
			recordTimes[i]=istream.readLong();
		for(int i = 0; i<recordNames.length; i++)
			recordNames[i]=istream.readUTF();					
			  		
	}catch(Exception e){}

	/*gameSound=prefsData[0]!=0;
	gameVibra=prefsData[1]!=0;*/
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	/*prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);*/
	
	try{
	
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
    	DataOutputStream ostream = new DataOutputStream(bstream);				
														
		ostream.writeBoolean(gameSound);
		ostream.writeBoolean(gameVibra);
		ostream.writeBoolean(gameKeyconf);
		ostream.writeBoolean(gameAuto);
		ostream.writeInt(available);
		ostream.writeInt(lastRound);
		ostream.writeLong(lastRoundTime);
		for(int i = 0; i<recordTimes.length; i++)
			ostream.writeLong(recordTimes[i]);
		for(int i = 0; i<recordNames.length; i++)
			ostream.writeUTF(recordNames[i]);					
							
    	ostream.flush();
    	ostream.close();
                	
    	prefsData = bstream.toByteArray();
    
    }catch(Exception e){}

	updatePrefs(true);
}





// *******************
// -------------------
// play - Engine
// ===================
// *******************

boolean playShow;

/*int playExit;

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

	gc.canvasFill(0x000000);
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
}

// <=- <=- <=- <=- <=-*/





// **************************************************************************//
// Final Clase com.mygdx.mongojocs.sanfermines2006.Game
// **************************************************************************//
};