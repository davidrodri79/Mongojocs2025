package com.mygdx.mongojocs.bravewar;/*========================================================================
	BraveWar

========================================================================*/



import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
//import com.mygdx.mongojocs.midletemu.Runnable;
//import com.mygdx.mongojocs.midletemu.Thread;
import com.mygdx.mongojocs.midletemu.TextField;

import java.io.*;


public class GameMidletLogic extends MIDlet implements /*LoginListener,*/ Runnable, CommandListener
{

	public static final int MAIN_MENU = 0;
	public static final int LOGING_IN = 1;
	public static final int WORLDMAP_NAVIGATE = 2;
	public static final int WORLDMAP_MYREGION_MENU = 3;
	public static final int WORLDMAP_OTHERREGION_MENU = 4;
	public static final int WORLDMAP_EMPTY_MENU = 5;
	public static final int WORLDMAP_DESTSELECT = 6;
	public static final int WORLDMAP_ATTACKSELECT = 7;
	public static final int WHAT_BUY_SELECT = 8;
	public static final int TROOPS_BUY_SELECT = 9;
	public static final int TROOPS_BUY_DETAILS = 10;
	public static final int FOOD_BUY_DETAILS = 11;
	public static final int FUEL_BUY_DETAILS = 12;
	public static final int MOVE_TROOPS_DETAILS = 13;
	public static final int LOBBY_WAIT_GAME_INIT = 14;
	public static final int REGION_INFO = 15;
	public static final int BATTLE = 16;
	public static final int BATTLE_ACTION_SELECT = 17;
	public static final int BATTLE_ACTION_SELECT_SIMPLE = 18;
	public static final int BATTLE_DESTSELECT = 19;
	public static final int BATTLE_TROOPMOVE = 20;
	public static final int BATTLE_MOVE_DETAILS = 21;
	public static final int BATTLE_ATTACKSELECT_SHORT = 22;
	public static final int BATTLE_ATTACKSELECT_LONG = 23;
	public static final int BATTLE_INFANTRY_ATTACK = 24;
	public static final int BATTLE_ARCHER_ATTACK = 25;
	public static final int BATTLE_CAVALRY_ATTACK = 26;
	public static final int BATTLE_TANK_ATTACK = 27;
	public static final int NOT_ENOUGH_POINTS = 28;
	public static final int PROCESS_NEXT_BATTLEACTION = 29;
	public static final int BATTLE_WAIT_RESPONSE = 30;	
	public static final int BATTLE_WON = 31;	
	public static final int BATTLE_LOST = 32;	
	public static final int BATTLE_REFUSED = 33;	
	public static final int NOT_ENOUGH_RESOURCES = 34;	
	public static final int CONFIRM_RETREAT = 35;	
	public static final int OPPONENT_RETREATED = 36;			
	public static final int BATTLE_REQUEST = 38;	
	public static final int FLAG_EDIT = 39;	
	public static final int BATTLE_MOVE_CAM_FOLLOW = 40;
	public static final int BATTLE_ATTACK_CAM_FOLLOW = 41;	
	public static final int MOVE_TROOPS_TYPE_DETAILS = 42;	
	public static final int CREDITS = 43;		
	public static final int GAME_ENDED = 44;	
	public static final int OPTIONS = 45;	
	public static final int CONFIRM_EXIT = 46;	
	public static final int LOBBY_CHOOSE_GAME = 47;	
	public static final int LOBBY_CREATE_GAME = 48;	
	public static final int LOBBY_GAME_DETAILS = 49;	
	public static final int CREDITS2 = 50;	
	public static final int CHAT_SELECT = 51;	
	public static final int CLEAR_FLAG_CONFIRM = 52;	
	public static final int LOGIN_FAILED = 53;	
	public static final int CANNOT_RETREAT = 54;
	public static final int GAME_END_RESULTS = 55;		
	public static final int SURVIVOR = 56;		
	public static final int GAME_STARTING = 57;		
	public static final int CLOSE_CONNECTION_WAIT = 58;
	public static final int OLD_VERSION = 59;
	public static final int TOO_OLD_VERSION = 60;
	public static final int CONFIRM_RESET = 61;
	public static final int WAIT_RESPONSE = 99;	
	
	
	public static final int MYREGION = 0;
	public static final int OTHERREGION = 1;
	public static final int EMPTY = 2;
	public static final int BATTLETILE = 3;
	public static final int BATTLETILESIMPLE = 4;
	
	public static final int FREEMOVE = 0;
	public static final int CONQUERMOVE = 1;
	public static final int ESCAPEMOVE = 2;
	
	
	public static final int moveActionPoints[]={4,2,1,20};
	public static final int attackActionPoints[]={4,1,3,40};
	
	public static final int BATTLE_ROUND_TIME = 450;
	

	public Thread th;
	int state, lastState, cnt, mapScrollX, mapScrollY, opCursor, 
		destRegion, destCurX, destCurY, menuOp=0, subMenuOp=0, troopType=0, 
		buyAmount, leftArrowFlash=0, rightArrowFlash=0, moveTroopsAmount[]={0,0,0,0},
	    troopsAtRegion[]={0,0,0,0}, alreadyHave=0, price=0, regionType=0,
	    batCurX, batCurZ, movingOffsetX, movingOffsetZ, movingOr, movingType, movingSide, shotAngle,
	    shotPower=1024, minPower, maxPower, actionPoints, nextBattleAction, battleTimer=0, moneyTimer=0,
	    batOpponentId, targetCurX, targetCurY, refusingReason, chatTimer=0, currentColor=1, idWinner, 
	    numPlayers, chatRec=0, recIndex[], lastBCX, lastBCZ,
	    startMoney, startTroops, startTime, currentGamesNumber=0, currentGame, waitingPlayersNumber,
	    resIdWinner, resScoreWinner, resPlIds[], resGloScores[], resRegScores[], resMonScores[], resArmScores[],
	    moveCase;	
	public long initialMillis, totalTime;	     	 
	boolean IAttack=false;
	
	// PlayerInfo
	//public Player playerInfo[];
	int idPlayers[], waitingPlayersIds[];
	String strPlayerNames[];
	byte playerFlags[][];
	
	public WarField warField;
	public Round round;
	public Camera cam;
	public String chatString, myName="BRAVE", gameName = "WAR", waitingPlayers[];
	public byte myFlag[];
	public boolean selfFlag=false, sound=true;
	
	public static GameCanvas gameCanvas; 	
	public static boolean doUpdate = true; 	

	public static Form chatForm, inputNameForm, inputGameNameForm, lobbyChatForm;
	public static TextField chatField, inputNameField, inputGameNameField, lobbyChatField;
	public static final String GameDataName = "BraveWarData";
	public static String versionStr; 
	
		
	class lobbyGameInfo {
	
		long appId;
		String gameName;
		int numPlayers, initialMoney, initialArmy, playTime;
	}
	
	public lobbyGameInfo currentGames[];
			
    public GameMidletLogic()
    {	
    	
		System.gc();
		versionStr = " - 1." + (0x00FF & GameCanvas.versionHandsetJar);
		//System.out.println("INICIALIZANDO...Memoria "+freeMemory());	

    	loadData();
    	// Chat stuff
    	
		Command chatDone = new Command(Text.chatSend, Command.OK, 1);
		
		chatForm = new Form(Text.chatWindow);
        	chatField = new TextField("","",20,TextField.ANY);
		chatField.setMaxSize(30);
        	chatForm.append(chatField);   
		chatForm.addCommand(chatDone);
        	chatForm.setCommandListener(this);    	
        	
		Command inputNameDone = new Command(Text.inputNameOk, Command.OK, 1);
		
		inputNameForm = new Form(Text.inputNameWindow);
        	inputNameField = new TextField(Text.inputNameField,myName,7,TextField.ANY);
		inputNameField.setMaxSize(7);
        	inputNameForm.append(inputNameField);   
		inputNameForm.addCommand(inputNameDone);
        	inputNameForm.setCommandListener(this);    	
        	
		Command inputGameNameDone = new Command(Text.inputGameNameOk, Command.OK, 1);
		
		inputGameNameForm = new Form(Text.inputGameNameWindow);
        	inputGameNameField = new TextField(Text.inputGameNameField,gameName,10,TextField.ANY);
		inputGameNameField.setMaxSize(10);
        	inputGameNameForm.append(inputGameNameField);   
		inputGameNameForm.addCommand(inputGameNameDone);
        	inputGameNameForm.setCommandListener(this);    	
        	
		Command lobbyChatDone = new Command(Text.lobbyChatSend, Command.OK, 1);
		
		lobbyChatForm = new Form(Text.chatWindow);
        	lobbyChatField = new TextField("","",30,TextField.ANY);
		lobbyChatField.setMaxSize(30);
        	lobbyChatForm.append(lobbyChatField);   
		lobbyChatForm.addCommand(lobbyChatDone);
        	lobbyChatForm.setCommandListener(this);    	
        	
        	
        	            	    	    	
    	GameMidletLogic.theGameMidlet = this;
        gameCanvas=new GameCanvas(this);
        
        display = Display.getDisplay (this);
        
        gameCanvas.initTables();
        
                       
		set_state(MAIN_MENU);              
		gameCanvas.playMusic(0,1);
                
        th=new Thread(this);
		th.start();
		
		//map = new com.mygdx.mongojocs.bravewar.Map(2,2);
		//mapCurX=2; mapCurY=2;
				
		currentGames = new lobbyGameInfo[20];
		waitingPlayers = new String[7];
		waitingPlayersIds = new int[7]; for(int i=0; i<7; i++) waitingPlayersIds[i]=-1;
		   		
		   		      		   		
    }
    
    public void startApp()
    {
		display.setCurrent (gameCanvas); 
		doUpdate = true;
    }
    
	public void commandAction (Command c, Displayable d)
	{
	       String label = c.getLabel(), text;
	       if (label == Text.chatSend ) {	       	
	       		if(chatRec==0)
					sendMessageTo(-1,myName+":"+checkString(chatField.getString()));
				else sendMessageTo(recIndex[chatRec-1],myName+":"+checkString(chatField.getString()));
				chatTimer=300; chatString=myName+":"+checkString(chatField.getString());
				chatField.setString("");
	      		display.setCurrent(gameCanvas);
	      		gameCanvas.controlReset();
	      		set_state(WORLDMAP_NAVIGATE);	      	
	       } 	 	
	       
	       if (label == Text.inputNameOk ) {	       	
				myName = checkString(inputNameField.getString());
	      		display.setCurrent(gameCanvas);
	      		gameCanvas.controlReset();
	      		saveData();
	      		set_state(MAIN_MENU);	      	
	       } 	 		       
	       
	       if (label == Text.inputGameNameOk ) {	       	
				gameName = checkString(inputGameNameField.getString());
	      		display.setCurrent(gameCanvas);
	      		gameCanvas.controlReset();
	      		set_state(LOBBY_CREATE_GAME);	      	
	       } 	 		       	       
	       
	       if (label == Text.lobbyChatSend ) {	       	
	       		
				sendGameLobbyMessage (myName+":"+checkString(lobbyChatField.getString()));
				lobbyChatField.setString("");				
	      		display.setCurrent(gameCanvas);
	      		gameCanvas.controlReset();
	      		set_state(LOBBY_WAIT_GAME_INIT);	      	
	       } 	 	
	       
	}	    
    
    public void pauseApp() 
    {
    	doUpdate = false;
    }
    
    public void destroyApp(boolean unconditional)
    {
    	saveData();
        notifyDestroyed();
    }

    public void runInit()
	{
	}

	public void runTick()
	{
		long t;

		//while (th!=null){


			t=System.currentTimeMillis();

			try{

				if (doUpdate) game_update();

				connector.readAndDispatch();
				//System.gc();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}

			do{
				try{
					Thread.sleep(1);

				}catch(InterruptedException e) {}

			} while(System.currentTimeMillis()<t+66);

		//}
	}

	public void runEnd()
	{

	}
    
    public void run()
	{	
		long t;			
														
		while (th!=null){
			
						
			t=System.currentTimeMillis();
			
			try{			
																	
				if (doUpdate) game_update();				

				connector.readAndDispatch();			
				//System.gc();				
			}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}
													
			do{
				try{
					Thread.sleep(1);
				
				}catch(InterruptedException e) {}
				
			} while(System.currentTimeMillis()<t+66);
									
		}		
	}
	
	
	public void set_state(int s)
	{
		state=s; cnt=0;
	}
	
	public void game_update()
	{
		int dx=0, dz=0, or=0, pts;
		cnt++;	
		boolean doPaint=false;
		
		if(chatTimer>0) chatTimer--;
					
		switch(state){
			
			case MAIN_MENU :
			if(gameCanvas.left) {gameCanvas.controlReset(); menuOp=(7+(menuOp-1))%7;}
			if(gameCanvas.right) {gameCanvas.controlReset(); menuOp=(menuOp+1)%7;}			
			if(gameCanvas.accept) {
				gameCanvas.controlReset();
				
				switch(menuOp){
					case 0 : 										
					idPlayers = new int[7]; for(int i=0; i<7; i++) idPlayers[i]=0;
					strPlayerNames = new String[7]; for(int i=0; i<7; i++) strPlayerNames[i]="BRAVE";
					playerFlags = new byte[7][];
						
					currentGamesNumber=0;
					
					set_state(CLOSE_CONNECTION_WAIT);				
					break;
										
					case 1 :
					if(sound==false) sound=true; else sound=false;
					break;
					
					case 2 :
					if(selfFlag==false) selfFlag=true; else selfFlag=false;
					break;
					
					case 3 : 
					gameCanvas.createFlag(0,myFlag);
					set_state(FLAG_EDIT);
					break;
					
					case 4 :
					display.setCurrent(inputNameForm); set_state(99);		
					break;			
					
					case 5 :
					set_state(CREDITS);
					break;
					
					case 6 :
					destroyApp(false);
					break;
				}
			}			
			doPaint=true;
			break;
			
			case CLOSE_CONNECTION_WAIT :
			// TO CHECK
			//if (threadCommListener!=null) while (threadCommListener.bDisconnectWhenPossible);
			set_state(LOGING_IN); 
			try {						
					//Conf.setMIDlet (this);
	   				connector.doLogin(this); //, this, null);
	   		} catch (Exception e)	{ e.printStackTrace (); }								
			break;
			
			case LOGING_IN :
				isConnected = true;
			if (isConnected)	{
														
				set_state(LOBBY_CHOOSE_GAME);			
			}else
			if(gameCanvas.men) {
				gameCanvas.controlReset(); 				
				set_state(MAIN_MENU);
			}						
			
			doPaint=true;
			break;
			
			case LOGIN_FAILED :
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				set_state(MAIN_MENU);
			}			
			doPaint=true;
			break;
			
			case LOBBY_CHOOSE_GAME :
			if(gameCanvas.men) {sendStartUpQuit(); isConnected=false; isGameRunning=false; set_state(MAIN_MENU);}
			if(gameCanvas.up) {gameCanvas.controlReset(); if(opCursor>0) opCursor--;}
			if(gameCanvas.down) {gameCanvas.controlReset(); if(opCursor<currentGamesNumber) opCursor++;}						
			if(gameCanvas.accept) {
				gameCanvas.controlReset();
				switch(opCursor){
					case 0 : 
					numPlayers=2; startMoney=0; startTroops=0; startTime=1; opCursor=0;
					set_state(LOBBY_CREATE_GAME);
					break;
					default :								
					currentGame=opCursor-1; set_state(LOBBY_GAME_DETAILS);
					break;					
				}							
			}	
			if(opCursor>currentGamesNumber) opCursor=currentGamesNumber;									
			doPaint=true;			
			break;
			
			case LOBBY_CREATE_GAME :
			if(gameCanvas.any) doPaint=true;			
			if(gameCanvas.up) {gameCanvas.controlReset(); opCursor=(6+(opCursor-1))%6;}
			if(gameCanvas.down) {gameCanvas.controlReset(); opCursor=(opCursor+1)%6;}						
			if(gameCanvas.down) {gameCanvas.controlReset(); set_state(LOBBY_CHOOSE_GAME);}
			if(gameCanvas.accept){		
				gameCanvas.controlReset();
				switch(opCursor){				
					case 0 : display.setCurrent(inputGameNameForm); set_state(99); break;
					case 5 : //set_state(WAIT_RESPONSE); HACK
					         set_state(GAME_STARTING);
							 waitingPlayersNumber=0;				
							 sendStartUpCreateGameLobby (gameName, numPlayers, startMoney, startTroops, startTime); 							 
					break;
				}				
			}
			if(gameCanvas.right) {
					gameCanvas.controlReset();
					switch(opCursor){
						case 1 : if(numPlayers<6) numPlayers++; if(numPlayers==3) numPlayers++; break;
						case 2 : if(startMoney<2) startMoney++; break;
						case 3 : if(startTroops<2) startTroops++; break;
						case 4 : if(startTime<4) startTime++; break;
					}								
			}
			if(gameCanvas.left) {
					gameCanvas.controlReset();
					switch(opCursor){
						case 1 : if(numPlayers>2) numPlayers--; if(numPlayers==3) numPlayers--; break;
						case 2 : if(startMoney>0) startMoney--; break;
						case 3 : if(startTroops>0) startTroops--; break;
						case 4 : if(startTime>0) startTime--; break;
					}								
			}	
			if(gameCanvas.men) {
				gameCanvas.controlReset(); 				
				set_state(LOBBY_CHOOSE_GAME);
			}						
			break;
			
			case LOBBY_GAME_DETAILS :
			if(gameCanvas.any) doPaint=true;			
			if(gameCanvas.accept){				
				gameCanvas.controlReset();
				sendStartUpJoinGameLobby (currentGames[currentGame].appId);		
				waitingPlayersNumber=0;
				for(int i=0; i<7; i++) waitingPlayersIds[i]=-1;
				numPlayers=currentGames[currentGame].numPlayers;
				set_state(LOBBY_WAIT_GAME_INIT);
			}
			if(gameCanvas.men){				
				gameCanvas.controlReset();
				set_state(LOBBY_CHOOSE_GAME);
			}
			doPaint=true;			
			break;
			
			case LOBBY_WAIT_GAME_INIT :			
			if(gameCanvas.men) {gameCanvas.controlReset(); 
				currentGames = new lobbyGameInfo[20];
				currentGamesNumber=0; opCursor=0; 
				sendGameLobbyQuit ();
				set_state(LOBBY_CHOOSE_GAME);				
			}
			if(gameCanvas.but3) {
				gameCanvas.controlReset(); 
				display.setCurrent(lobbyChatForm); set_state(99);
			}
			doPaint=true;
			break;
			
			case GAME_STARTING :
			if (isGameRunning){					
			
					myMoney=initialMoneyValue; myFood=50; myFuel=0;
			
					gameCanvas.destroyPicture();
					gameCanvas.createWorldTileMap(map);					
					gameCanvas.loadMapGfx();
					set_state(WORLDMAP_NAVIGATE);
					initialMillis = System.currentTimeMillis();
					switch(startTime){
						case 0 : totalTime = 5; break;
						case 1 : totalTime = 15; break;
						case 2 : totalTime = 30; break;
						case 3 : totalTime = 45; break;
						case 4 : totalTime = 60; break;
					}					
					
					recIndex=new int[7];
					for(int i=0; i<numPlayers-1; i++)
						if(i<myPlayerId-1) recIndex[i]=i+1;
						else recIndex[i]=i+2;								
						
			}
			doPaint=true;
			break;			
																										
			case WORLDMAP_NAVIGATE :			
			if(gameCanvas.up && mapCurY>0) {gameCanvas.controlReset(); mapCurY--;}
			if(gameCanvas.down && mapCurY<map.hexaHeight-1) {gameCanvas.controlReset(); mapCurY++;}
			if(gameCanvas.left && mapCurX>0) {gameCanvas.controlReset(); mapCurX--;}
			if(gameCanvas.right && mapCurX<map.hexaWidth-1) {gameCanvas.controlReset(); mapCurX++;}						
			if(gameCanvas.accept || gameCanvas.men){
				opCursor=0;
				gameCanvas.controlReset();
				if(map.hexaMap[mapCurY][mapCurX]<1 || !isMapUpdated) {regionType=EMPTY; set_state(WORLDMAP_EMPTY_MENU);}
				else if(map.getRegionByPos(mapCurX,mapCurY).idPlayer==myPlayerId)
					{regionType=MYREGION; set_state(WORLDMAP_MYREGION_MENU); }
				else {regionType=OTHERREGION; set_state(WORLDMAP_OTHERREGION_MENU);}
			}				
			doPaint=true;
			break;	
			
			case WORLDMAP_MYREGION_MENU :
			if(gameCanvas.left) {doPaint=true; gameCanvas.controlReset(); opCursor=(6+(opCursor-1))%6;}
			if(gameCanvas.right) {doPaint=true; gameCanvas.controlReset(); opCursor=(opCursor+1)%6;}
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(WORLDMAP_NAVIGATE);}
			if(gameCanvas.accept){
				doPaint=true;
				gameCanvas.controlReset();
				switch(opCursor){
					case 0 : destRegion=0; moveCase=FREEMOVE; set_state(WORLDMAP_DESTSELECT); break;		
					case 1 : set_state(WORLDMAP_ATTACKSELECT); break;							
					case 2 : set_state(WHAT_BUY_SELECT); break;						
					case 3 : set_state(REGION_INFO); break;						
					case 4 : set_state(CHAT_SELECT); break;
					case 5 : set_state(OPTIONS); break;
				}			
			}
			if(cnt<6) doPaint=true;
			break;
			
			case WORLDMAP_OTHERREGION_MENU :
			if(gameCanvas.left) {doPaint=true; gameCanvas.controlReset(); opCursor=(3+(opCursor-1))%3;}
			if(gameCanvas.right) {doPaint=true; gameCanvas.controlReset(); opCursor=(opCursor+1)%3;}
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(WORLDMAP_NAVIGATE);}
			if(gameCanvas.accept){
				doPaint=true;
				gameCanvas.controlReset();
				switch(opCursor){
					case 0 : set_state(REGION_INFO); break;		
					case 1 : set_state(CHAT_SELECT); break;
					case 2 : set_state(OPTIONS); break;
				}			
			}
			if(cnt<6) doPaint=true;
			break;
			
			case WORLDMAP_EMPTY_MENU :
			if(gameCanvas.left) {doPaint=true; gameCanvas.controlReset(); opCursor=(2+(opCursor-1))%2;}
			if(gameCanvas.right) {doPaint=true; gameCanvas.controlReset(); opCursor=(opCursor+1)%2;}
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(WORLDMAP_NAVIGATE);}
			if(gameCanvas.accept){
				doPaint=true;
				gameCanvas.controlReset(); 
				switch(opCursor){
					case 0 : set_state(CHAT_SELECT); break;
					case 1 : set_state(OPTIONS); break;
				}			
			}
			if(cnt<6) doPaint=true;
			break;
						
			case WORLDMAP_ATTACKSELECT :
			if(gameCanvas.right) {gameCanvas.controlReset(); destRegion=(6+(destRegion-1))%6;}
			if(gameCanvas.left) {gameCanvas.controlReset(); destRegion=(destRegion+1)%6;}
			if(gameCanvas.down) {gameCanvas.controlReset(); destRegion=(6+(destRegion-1))%6;}
			if(gameCanvas.up) {gameCanvas.controlReset(); destRegion=(destRegion+1)%6;}
			if(gameCanvas.men) {
				gameCanvas.controlReset(); 
				switch(regionType){
					case MYREGION : set_state(WORLDMAP_MYREGION_MENU); break;
					case OTHERREGION : set_state(WORLDMAP_OTHERREGION_MENU); break;
					case EMPTY : set_state(WORLDMAP_EMPTY_MENU); break;
				}
			}
			if(destCurX>=0 && destCurX<map.hexaWidth && destCurY>=0 && destCurY<map.hexaHeight)
			if(gameCanvas.accept && map.hexaMap[destCurY][destCurX]>0 && map.getRegionByPos(destCurX,destCurY).idPlayer!=myPlayerId
				&& map.getRegionByPos(destCurX,destCurY).idPlayer>=0) {
				gameCanvas.controlReset();
				set_state(99);
				sendAttackFirst (map.getRegionIdByPos (mapCurX,mapCurY), map.getRegionIdByPos (destCurX, destCurY));												
				//setUpBattle(0,1,0,0,0,0,0,0,0,0);
			}			
			//if(gameCanvas.accept) {gameCanvas.controlReset(); set_state(WORLDMAP_NAVIGATE);}						
			setDestinationXY(destRegion);			
			targetCurX=destCurX; targetCurY=destCurY;
			doPaint=true;
			break;
						
			case WORLDMAP_DESTSELECT :
			if(gameCanvas.right) {gameCanvas.controlReset(); destRegion=(6+(destRegion-1))%6;}
			if(gameCanvas.left) {gameCanvas.controlReset(); destRegion=(destRegion+1)%6;}
			if(gameCanvas.down) {gameCanvas.controlReset(); destRegion=(6+(destRegion-1))%6;}
			if(gameCanvas.up) {gameCanvas.controlReset(); destRegion=(destRegion+1)%6;}
			//if(state==WORLDMAP_DESTSELECT)
			if(gameCanvas.men) {
				gameCanvas.controlReset(); 
				switch(regionType){
					case MYREGION : set_state(WORLDMAP_MYREGION_MENU); break;
					case OTHERREGION : set_state(WORLDMAP_OTHERREGION_MENU); break;
					case EMPTY : set_state(WORLDMAP_EMPTY_MENU); break;
				}
			}			
			if(destCurX>=0 && destCurX<map.hexaWidth && destCurY>=0 && destCurY<map.hexaHeight)
			if(gameCanvas.accept && map.hexaMap[destCurY][destCurX]>0 && (map.getRegionByPos(destCurX,destCurY).idPlayer<=0 || map.getRegionByPos(destCurX,destCurY).idPlayer==myPlayerId)) {
					//System.out.println("MAP:"+map.hexaMap[destCurY][destCurX]+" "+map.getRegionByPos(destCurX,destCurY).idPlayer);
					gameCanvas.controlReset(); 
					if(moveCase!=ESCAPEMOVE){
						for(int i=0; i<4; i++) moveTroopsAmount[i]=0;					
						troopsAtRegion[0]=map.getRegionByPos(mapCurX,mapCurY).numUnitsInfantry;
						troopsAtRegion[1]=map.getRegionByPos(mapCurX,mapCurY).numUnitsArchers;
						troopsAtRegion[2]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCavalry;
						troopsAtRegion[3]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCatapults;						
						troopType=0;
						set_state(MOVE_TROOPS_DETAILS);					
					}else {				
						isMapUpdated=false;															
						sendPlaceTroops (destCurX+destCurY*map.hexaWidth,
							 troopsAtRegion[3], troopsAtRegion[1], troopsAtRegion[2], troopsAtRegion[0]);							 												
						for(int i=0; i<4; i++)
							moveTroopsAmount[i]=troopsAtRegion[i];
						myFood-=foodNeeded();
						myFuel-=fuelNeeded();
						set_state(WORLDMAP_NAVIGATE);												
					}
			}						
			setDestinationXY(destRegion);			
			doPaint=true;
			break;
									
			case WHAT_BUY_SELECT :
			if(gameCanvas.up) {doPaint=true; gameCanvas.controlReset(); menuOp=(3+(menuOp-1))%3;}
			if(gameCanvas.down) {doPaint=true; gameCanvas.controlReset(); menuOp=(menuOp+1)%3;}			
				if(gameCanvas.men) {
				gameCanvas.controlReset(); 
				switch(regionType){
					case MYREGION : set_state(WORLDMAP_MYREGION_MENU); break;
					case OTHERREGION : set_state(WORLDMAP_OTHERREGION_MENU); break;
					case EMPTY : set_state(WORLDMAP_EMPTY_MENU); break;
				}
			}
			if(gameCanvas.accept) {				
				gameCanvas.controlReset(); 
				buyAmount=0;
				if(menuOp==0) {set_state(TROOPS_BUY_SELECT); troopType=0;}
				if(menuOp==1) {alreadyHave=myFood; price=10; set_state(FOOD_BUY_DETAILS);}				
				if(menuOp==2) {alreadyHave=myFuel; price=50; set_state(FUEL_BUY_DETAILS);}				
			}						
			break;
			
			case TROOPS_BUY_SELECT :			
			if(gameCanvas.left) {doPaint=true; gameCanvas.controlReset(); troopType=(4+(troopType-1))%4;}
			if(gameCanvas.right) {doPaint=true; gameCanvas.controlReset(); troopType=(troopType+1)%4;}			
			if(gameCanvas.accept) {
				doPaint=true;
				gameCanvas.controlReset(); 
				buyAmount=0; 
				switch(troopType){
					case 0 : alreadyHave=map.getRegionByPos(mapCurX,mapCurY).numUnitsInfantry; price=50; break;	
					case 1 : alreadyHave=map.getRegionByPos(mapCurX,mapCurY).numUnitsArchers; price=100; break;	
					case 2 : alreadyHave=map.getRegionByPos(mapCurX,mapCurY).numUnitsCavalry; price=250; break;	
					case 3 : alreadyHave=map.getRegionByPos(mapCurX,mapCurY).numUnitsCatapults; price=1000; break;	
				}				
				set_state(TROOPS_BUY_DETAILS);
			}							
			if(gameCanvas.men) {gameCanvas.controlReset(); buyAmount=0; set_state(WHAT_BUY_SELECT);}						
			break;			
			
			case TROOPS_BUY_DETAILS :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.left) { gameCanvas.controlReset(); leftArrowFlash=3; buyAmount--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); rightArrowFlash=3; 
										if((buyAmount+1)*price<=myMoney) buyAmount++; }
			if(gameCanvas.down) {gameCanvas.controlReset(); leftArrowFlash=3; buyAmount-=5;}
			if(gameCanvas.up) {gameCanvas.controlReset(); rightArrowFlash=3; 
										if((buyAmount+5)*price<=myMoney) buyAmount+=5;}
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(TROOPS_BUY_SELECT);}									
			if(gameCanvas.accept) {
				for(int i=0; i<4; i++)
					troopsAtRegion[i]=0;
				troopsAtRegion[troopType]=buyAmount;
				gameCanvas.controlReset(); 
				isMapUpdated=false;
				sendBuyTroops (mapCurX+(mapCurY*map.hexaWidth),troopsAtRegion[3],troopsAtRegion[1],troopsAtRegion[2],troopsAtRegion[0]);
				myMoney-=buyAmount*price;
				sendUpdateMyValues();
				set_state(WORLDMAP_NAVIGATE);				
			}
			if(buyAmount<0) buyAmount=0;
			if(leftArrowFlash>0) leftArrowFlash--;
			if(rightArrowFlash>0) rightArrowFlash--;			
			break;
			
			case FUEL_BUY_DETAILS :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.left) {gameCanvas.controlReset(); leftArrowFlash=3; buyAmount--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); rightArrowFlash=3; 
										if((buyAmount+1)*price<=myMoney) buyAmount++; }
			if(gameCanvas.down) {gameCanvas.controlReset(); leftArrowFlash=3; buyAmount-=5;}
			if(gameCanvas.up) {gameCanvas.controlReset(); rightArrowFlash=3; 
										if((buyAmount+5)*price<=myMoney) buyAmount+=5;}
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(WHAT_BUY_SELECT);}						
			if(gameCanvas.accept) {
				gameCanvas.controlReset(); 
				myFuel+=buyAmount;
				myMoney-=buyAmount*price;
				sendUpdateMyValues();				
				set_state(WORLDMAP_NAVIGATE);
				}
			if(buyAmount<0) buyAmount=0;
			if(leftArrowFlash>0) leftArrowFlash--;
			if(rightArrowFlash>0) rightArrowFlash--;						
			break;
						
			
			case FOOD_BUY_DETAILS :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.left) {gameCanvas.controlReset(); leftArrowFlash=3; buyAmount--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); rightArrowFlash=3; 
										if((buyAmount+1)*price<=myMoney) buyAmount++; }
			if(gameCanvas.down) {gameCanvas.controlReset(); leftArrowFlash=3; buyAmount-=5;}
			if(gameCanvas.up) {gameCanvas.controlReset(); rightArrowFlash=3; 
										if((buyAmount+5)*price<=myMoney) buyAmount+=5;}
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(WHAT_BUY_SELECT);}						
			if(gameCanvas.accept) {
				gameCanvas.controlReset(); 
				myFood+=buyAmount;
				myMoney-=buyAmount*price;
				sendUpdateMyValues();				
				set_state(WORLDMAP_NAVIGATE);
				}
			if(buyAmount<0) buyAmount=0;
			if(leftArrowFlash>0) leftArrowFlash--;
			if(rightArrowFlash>0) rightArrowFlash--;						
			break;
			
			
			case MOVE_TROOPS_DETAILS :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.up) {gameCanvas.controlReset(); troopType=(6+(troopType-2))%6;}
			if(gameCanvas.down) {gameCanvas.controlReset(); troopType=(troopType+2)%6;}						
			if(gameCanvas.left) {gameCanvas.controlReset(); troopType=(6+(troopType-1))%6;}
			if(gameCanvas.right) {gameCanvas.controlReset(); troopType=(troopType+1)%6;}									
			
			if(gameCanvas.men) {
				gameCanvas.controlReset(); 
				if(moveCase==CONQUERMOVE)					
				sendMoveTroops (mapCurX+mapCurY*map.hexaWidth, destCurX+destCurY*map.hexaWidth,0, 0, 0, 0);							 			
				set_state(WORLDMAP_DESTSELECT);
			}									
			if(gameCanvas.accept){
				
				gameCanvas.controlReset(); 					
				
				if(troopType<4) set_state(MOVE_TROOPS_TYPE_DETAILS);
				else if(troopType==4){
						if(moveCase==CONQUERMOVE)					
						sendMoveTroops (mapCurX+mapCurY*map.hexaWidth, destCurX+destCurY*map.hexaWidth,0, 0, 0, 0);							 			
						set_state(WORLDMAP_DESTSELECT);
						
				}else if (troopType==5){
					if(myFood>=foodNeeded() && myFuel>=fuelNeeded())
					if(moveTroopsAmount[0]>0 || moveTroopsAmount[1]>0 || moveTroopsAmount[2]>0 || moveTroopsAmount[3]>0){
					
													
						myFood-=foodNeeded();
						myFuel-=fuelNeeded();
						isMapUpdated=false;
						sendMoveTroops (mapCurX+mapCurY*map.hexaWidth, destCurX+destCurY*map.hexaWidth,
							 moveTroopsAmount[3], moveTroopsAmount[1], moveTroopsAmount[2], moveTroopsAmount[0]);							 
						
						sendUpdateMyValues();											 
						set_state(WORLDMAP_NAVIGATE);							
					}
				}
			}			
			break;
			
			case MOVE_TROOPS_TYPE_DETAILS :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.left) {gameCanvas.controlReset(); leftArrowFlash=3; if(moveTroopsAmount[troopType]>0) moveTroopsAmount[troopType]--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); rightArrowFlash=3; if(moveTroopsAmount[troopType]<troopsAtRegion[troopType]) moveTroopsAmount[troopType]++;}															
			if(gameCanvas.down) {gameCanvas.controlReset(); leftArrowFlash=3; if(moveTroopsAmount[troopType]>=5) moveTroopsAmount[troopType]-=5;}
			if(gameCanvas.up) {gameCanvas.controlReset(); rightArrowFlash=3; if(moveTroopsAmount[troopType]<=troopsAtRegion[troopType]-5) moveTroopsAmount[troopType]+=5;}																		
			if(leftArrowFlash>0) leftArrowFlash--;
			if(rightArrowFlash>0) rightArrowFlash--;									
			if(gameCanvas.men || gameCanvas.accept) {
				gameCanvas.controlReset(); 
				set_state(MOVE_TROOPS_DETAILS);
			}
			break;
			
			case REGION_INFO :
			if(gameCanvas.accept || gameCanvas.men) {
				gameCanvas.controlReset(); 
				switch(regionType){
					case MYREGION : set_state(WORLDMAP_MYREGION_MENU); break;
					case OTHERREGION : set_state(WORLDMAP_OTHERREGION_MENU); break;
					case EMPTY : set_state(WORLDMAP_EMPTY_MENU); break;
				}
			}						
			break;
			
			case CHAT_SELECT :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.up) {doPaint=true; gameCanvas.controlReset(); chatRec=((numPlayers)+(chatRec-1))%(numPlayers);}
			if(gameCanvas.down) {doPaint=true; gameCanvas.controlReset(); chatRec=(chatRec+1)%(numPlayers);}			
			if(gameCanvas.men) {
				gameCanvas.controlReset(); 
				switch(regionType){
					case MYREGION : set_state(WORLDMAP_MYREGION_MENU); break;
					case OTHERREGION : set_state(WORLDMAP_OTHERREGION_MENU); break;
					case EMPTY : set_state(WORLDMAP_EMPTY_MENU); break;
				}
			}						
			if(gameCanvas.accept) {
				gameCanvas.controlReset();
				display.setCurrent(chatForm); set_state(99);
			}
			break;
			
			case BATTLE :
			doPaint=true;
			processBattleTime();
			
			if(gameCanvas.up) {gameCanvas.controlReset(); if(batCurZ<warField.sz-2) batCurZ++;}
			if(gameCanvas.down) {gameCanvas.controlReset(); if(batCurZ>0) batCurZ--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); if(batCurX<warField.sx-2) batCurX++;}
			if(gameCanvas.left) {gameCanvas.controlReset(); if(batCurX>0) batCurX--;}
			
			battleCamFollow(batCurX,batCurZ,200);			
			
			//cam.recalculateMatrix();
			
			/*if(gameCanvas.men) {
				gameCanvas.controlReset();
				set_state(WORLDMAP_NAVIGATE);
				warField=null; cam=null; System.gc();
				gameCanvas.destroyWarGfx();
				}*/
			if(gameCanvas.accept) {				
				gameCanvas.controlReset();
				opCursor=0;
				if(warField.tiles[batCurX][batCurZ].UnitSide==0 && round.notUsedTile(batCurX,batCurZ))
					set_state(BATTLE_ACTION_SELECT);
				else 				
					set_state(BATTLE_ACTION_SELECT_SIMPLE);
			}
			break;
			
			case BATTLE_DESTSELECT :
			doPaint=true;
			processBattleTime();
			if(gameCanvas.right || gameCanvas.down) {gameCanvas.controlReset(); destRegion=(8+(destRegion-1))%8;}
			if(gameCanvas.left || gameCanvas.up) {gameCanvas.controlReset(); destRegion=(destRegion+1)%8;}			
			setBattleDestinationXY(destRegion);
			
			if(gameCanvas.men) {
				gameCanvas.controlReset();
				set_state(BATTLE);				
			}			
			if(gameCanvas.accept && 
			((warField.tiles[destCurX][destCurY].UnitType==-1) ||
			(warField.tiles[destCurX][destCurY].UnitType==warField.tiles[batCurX][batCurZ].UnitType && warField.tiles[destCurX][destCurY].UnitSide==0))) {
				gameCanvas.controlReset();
				moveTroopsAmount[0]=warField.tiles[batCurX][batCurZ].UnitAmount;
				set_state(BATTLE_MOVE_DETAILS);								
			}			
			break;
			
			case BATTLE_ACTION_SELECT :
			if(gameCanvas.any) doPaint=true;
			processBattleTime();
			if(gameCanvas.left) {gameCanvas.controlReset(); opCursor=(4+(opCursor-1))%4;}
			if(gameCanvas.right) {gameCanvas.controlReset(); opCursor=(opCursor+1)%4;}
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(BATTLE);}
			if(gameCanvas.accept){
				gameCanvas.controlReset(); 
				switch(opCursor){
					case 0 : destRegion=0; setBattleDestinationXY(0); set_state(BATTLE_DESTSELECT); break;		
					case 1 : 								
							 switch(warField.tiles[batCurX][batCurZ].UnitType){							 
							 	case 0 : set_state(BATTLE_ATTACKSELECT_SHORT); break;
							 	case 1 : minPower=2; maxPower=4; set_state(BATTLE_ATTACKSELECT_LONG); break;
							 	case 2 : minPower=1; maxPower=3; set_state(BATTLE_ATTACKSELECT_LONG); break;
							 	case 3 : minPower=4; maxPower=11; set_state(BATTLE_ATTACKSELECT_LONG); break;							 	
							}
							shotPower=minPower<<10;
					 		break;
					case 2 : set_state(BATTLE_WAIT_RESPONSE); round.send(); break;
					case 3 : set_state(CONFIRM_RETREAT); break;
				}					
			}
			if(cnt<6) doPaint=true;	
			break;
			
			case BATTLE_ACTION_SELECT_SIMPLE :
			if(gameCanvas.any) doPaint=true;
			processBattleTime();
			if(gameCanvas.left) {gameCanvas.controlReset(); opCursor=(2+(opCursor-1))%2;}
			if(gameCanvas.right) {gameCanvas.controlReset(); opCursor=(opCursor+1)%2;}			
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(BATTLE);}
			if(gameCanvas.accept){
				gameCanvas.controlReset(); 
				switch(opCursor){					
					case 0 : 
					set_state(BATTLE_WAIT_RESPONSE);
					round.send();				
					break;					
					case 1 : set_state(CONFIRM_RETREAT); 
					break;
				}				
			}			
			if(cnt<6) doPaint=true;
			break;
			
			case BATTLE_WAIT_RESPONSE :		
			doPaint=true;	
			if(gameCanvas.up) {gameCanvas.controlReset(); if(batCurZ<warField.sz-2) batCurZ++;}
			if(gameCanvas.down) {gameCanvas.controlReset(); if(batCurZ>0) batCurZ--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); if(batCurX<warField.sx-2) batCurX++;}
			if(gameCanvas.left) {gameCanvas.controlReset(); if(batCurX>0) batCurX--;}
			
			battleCamFollow(batCurX,batCurZ,200);			
			lastBCX=batCurX; lastBCZ=batCurZ;
			
			//cam.recalculateMatrix();			
			break;
						
			case PROCESS_NEXT_BATTLEACTION :						
			if(nextBattleAction<round.nActions){
				switch(round.ac[nextBattleAction].type){
					case Round.ATTACK :
					batCurX=round.ac[nextBattleAction].xori;
					batCurZ=round.ac[nextBattleAction].zori;
					destCurX=round.ac[nextBattleAction].xdes;
					destCurY=round.ac[nextBattleAction].zdes;
					movingOr=angleDirection(batCurX,batCurZ,destCurX,destCurY);
					if(warField.tiles[batCurX][batCurZ].UnitSide!=-1)
						set_state(BATTLE_ATTACK_CAM_FOLLOW);					
					else set_state(PROCESS_NEXT_BATTLEACTION);
					break;					
					
					case Round.MOVE :
					batCurX=round.ac[nextBattleAction].xori;
					batCurZ=round.ac[nextBattleAction].zori;
					destCurX=round.ac[nextBattleAction].xdes;
					destCurY=round.ac[nextBattleAction].zdes;
					moveTroopsAmount[0]=round.ac[nextBattleAction].amount;
					if(moveTroopsAmount[0]>warField.tiles[batCurX][batCurZ].UnitAmount)
						moveTroopsAmount[0]=warField.tiles[batCurX][batCurZ].UnitAmount;
					
					dx=destCurX-batCurX;
					dz=destCurY-batCurZ;
					
					movingOffsetX=-dx*1024;									
					movingOffsetZ=-dz*1024;									
					movingType=warField.tiles[batCurX][batCurZ].UnitType;				
					movingSide=warField.tiles[batCurX][batCurZ].UnitSide;
					movingOr=angleDirection(batCurX,batCurZ,destCurX,destCurY);
					
					if(warField.tiles[batCurX][batCurZ].UnitSide!=-1 && 
						(warField.tiles[destCurX][destCurY].UnitSide==-1 || 
						(warField.tiles[destCurX][destCurY].UnitSide==warField.tiles[batCurX][batCurZ].UnitSide && warField.tiles[destCurX][destCurY].UnitType==warField.tiles[batCurX][batCurZ].UnitType))){
							warField.leaveTroops(batCurX,batCurZ,moveTroopsAmount[0]);			
						set_state(BATTLE_MOVE_CAM_FOLLOW);					
					}else set_state(PROCESS_NEXT_BATTLEACTION);
					
				}
				troopType=warField.tiles[batCurX][batCurZ].UnitType;				
				nextBattleAction++;
								
			}else{
				round=null; System.gc();
				// Check if battle is over
				int res=warField.battleStatus();
				if(res==WarField.NOT_OVER){
					round=new Round(myPlayerId); actionPoints=250; nextBattleAction=0; battleTimer=0;
					batCurX=lastBCX; batCurZ=lastBCZ;
					set_state(BATTLE);
				}else{
					if(res==WarField.WON)
						{set_state(BATTLE_WON); gameCanvas.playMusic(5,1);}
					else						
						{set_state(BATTLE_LOST); gameCanvas.playMusic(6,1);}															
				}
			}
			break;
			
			case BATTLE_MOVE_CAM_FOLLOW :			
			doPaint=true;
			battleCamFollow(destCurX,destCurY,200);									
			if(cam.z>=((destCurY-4)<<10)+200 && cam.z<=((destCurY-3)<<10)-200 && cam.x>=((destCurX-2)<<10)+200 && cam.x<=((destCurX-1)<<10)-200)
				set_state(BATTLE_TROOPMOVE);
			break;
			
			case BATTLE_ATTACK_CAM_FOLLOW :	
			if(gameCanvas.any) doPaint=true;					
			battleCamFollow(batCurX,batCurZ,200);												
			if(cam.z>=((batCurZ-4)<<10)+200 && cam.z<=((batCurZ-3)<<10)-200 && cam.x>=((batCurX-2)<<10)+200 && cam.x<=((batCurX-1)<<10)-200)
				switch(warField.tiles[batCurX][batCurZ].UnitType){
					case 0 : set_state(BATTLE_INFANTRY_ATTACK); break;
					case 1 : set_state(BATTLE_CAVALRY_ATTACK); break;
					case 2 : set_state(BATTLE_ARCHER_ATTACK); break;
					case 3 : set_state(BATTLE_TANK_ATTACK); break;						
				}
			break;

			
			case BATTLE_TROOPMOVE :
			doPaint=true;
			
			battleCamFollow(destCurX,destCurY,200);												
								
			dx=destCurX-batCurX;
			dz=destCurY-batCurZ;
			
			movingOffsetX+=dx*64;
			movingOffsetZ+=dz*64;
			//movingOr=or;
			if(cnt>16) {				
				batCurX=destCurX; batCurZ=destCurY; 
				//warField.tiles[destCurX][destCurY].UnitOr=0;
				warField.putTroops(batCurX,batCurZ,moveTroopsAmount[0],movingType,movingSide);
				set_state(PROCESS_NEXT_BATTLEACTION);
			}
			break;
			
			case BATTLE_MOVE_DETAILS :		
			if(gameCanvas.any) doPaint=true;
			processBattleTime();
			if(gameCanvas.left) {gameCanvas.controlReset(); leftArrowFlash=3; moveTroopsAmount[0]--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); rightArrowFlash=3; moveTroopsAmount[0]++;}
			if(gameCanvas.down) {gameCanvas.controlReset(); leftArrowFlash=3; moveTroopsAmount[0]-=5;}
			if(gameCanvas.up) {gameCanvas.controlReset(); rightArrowFlash=3; moveTroopsAmount[0]+=5;}
			if(moveTroopsAmount[0]<1) moveTroopsAmount[0]=1;
			if(moveTroopsAmount[0]>warField.tiles[batCurX][batCurZ].UnitAmount) moveTroopsAmount[0]=warField.tiles[batCurX][batCurZ].UnitAmount;
			if(leftArrowFlash>0) leftArrowFlash--;
			if(rightArrowFlash>0) rightArrowFlash--;			
						
			if(gameCanvas.men) {gameCanvas.controlReset(); set_state(BATTLE_DESTSELECT);}									
			if(gameCanvas.accept) {
				
				gameCanvas.controlReset();
				pts=moveActionPoints[warField.tiles[batCurX][batCurZ].UnitType]*warField.tiles[batCurX][batCurZ].UnitAmount;
				if(actionPoints>=pts){
					actionPoints-=pts;
					round.addMoveAction(battleTimer,batCurX,batCurZ,destCurX,destCurY,moveTroopsAmount[0]);
					set_state(BATTLE);																				
				}else{
					set_state(NOT_ENOUGH_POINTS);
				}
			}
			break;
			
			case BATTLE_ATTACKSELECT_SHORT :
			doPaint=true;
			processBattleTime();			
			if(gameCanvas.right) {gameCanvas.controlReset(); destRegion=(8+(destRegion-1))%8;}
			if(gameCanvas.left) {gameCanvas.controlReset(); destRegion=(destRegion+1)%8;}
			if(gameCanvas.down) {gameCanvas.controlReset(); destRegion=(8+(destRegion-1))%8;}
			if(gameCanvas.up) {gameCanvas.controlReset(); destRegion=(destRegion+1)%8;}			
			
			setBattleDestinationXY(destRegion);
			
			if(gameCanvas.men) {
				gameCanvas.controlReset();
				set_state(BATTLE);				
			}			
			if(gameCanvas.accept && 
			warField.tiles[destCurX][destCurY].UnitSide!=0) {
				gameCanvas.controlReset();
				pts=attackActionPoints[warField.tiles[batCurX][batCurZ].UnitType]*warField.tiles[batCurX][batCurZ].UnitAmount;
				if(actionPoints>=pts){
					actionPoints-=pts;
					round.addAttackAction(battleTimer,batCurX,batCurZ,destCurX,destCurY);
					set_state(BATTLE);																				
				}else{
					set_state(NOT_ENOUGH_POINTS);
				}
			}			
			break;			
			
			case BATTLE_ATTACKSELECT_LONG :
			doPaint=true;
			processBattleTime();
			if(gameCanvas.left) {shotAngle=(shotAngle+2)%256;}
			if(gameCanvas.right) {shotAngle=(256+shotAngle-2)%256;}
			if(gameCanvas.down) {shotPower-=250; if(shotPower<(minPower<<10)+256) shotPower=(minPower<<10)+256;}
			if(gameCanvas.up) {shotPower+=250; if(shotPower>(maxPower<<10)+256) shotPower=(maxPower<<10)+256;}			
								
			setLongRangeDestinationXY();			
			battleCamFollow(destCurX,destCurY,300);
			
			if(gameCanvas.men) {
				gameCanvas.controlReset();
				set_state(BATTLE);				
			}			
			if(gameCanvas.accept && 
			warField.tiles[destCurX][destCurY].UnitSide!=0) {
				gameCanvas.controlReset();
				
				pts=attackActionPoints[warField.tiles[batCurX][batCurZ].UnitType]*warField.tiles[batCurX][batCurZ].UnitAmount;
				if(actionPoints>=pts){
					actionPoints-=pts;
					round.addAttackAction(battleTimer,batCurX,batCurZ,destCurX,destCurY);
					set_state(BATTLE);																				
				}else{
					set_state(NOT_ENOUGH_POINTS);
				}
			}			
	
			break;			
			
			case NOT_ENOUGH_POINTS :
			doPaint=true;
			processBattleTime();			
			if(gameCanvas.accept){
					gameCanvas.controlReset();
					set_state(BATTLE);
			}
			break;			
						
			case BATTLE_INFANTRY_ATTACK :
			doPaint=true;
			if(cnt<15) battleCamFollow(batCurX,batCurZ,200);
			else battleCamFollow(destCurX,destCurY,200);
			if(cnt==20) warField.attack(batCurX,batCurZ,destCurX,destCurY);
			if(cnt>=35) set_state(PROCESS_NEXT_BATTLEACTION);
			break;
			
			case BATTLE_CAVALRY_ATTACK :
			case BATTLE_TANK_ATTACK :
			case BATTLE_ARCHER_ATTACK :
			doPaint=true;
			if(cnt<15) battleCamFollow(batCurX,batCurZ,400);
			else battleCamFollow(destCurX,destCurY,400);
			if(cnt==20) {warField.attack(batCurX,batCurZ,destCurX,destCurY); gameCanvas.playSound(4);}
			if(cnt>=35) set_state(PROCESS_NEXT_BATTLEACTION);
			break;			
			
			case BATTLE_WON :
			if(gameCanvas.accept){
				gameCanvas.controlReset();				
				processBattleResults(true);					
				if(IAttack){
					destCurX=targetCurX; destCurY=targetCurY; 					
					moveTroopsAmount[0]=0;
					moveTroopsAmount[1]=0;
					moveTroopsAmount[2]=0;
					moveTroopsAmount[3]=0;
					troopType=0;
					lastState=state;
					moveCase=CONQUERMOVE;
					set_state(MOVE_TROOPS_DETAILS);
					
				}	
				else set_state(WORLDMAP_NAVIGATE);								
				shutDownBattle();
			}			
			break;
			
			case BATTLE_LOST :
			if(gameCanvas.accept){
				gameCanvas.controlReset();				
				processBattleResults(false);				
				shutDownBattle();				
				set_state(WORLDMAP_NAVIGATE);
			}
			break;			
						
			case BATTLE_REFUSED :			
			if(gameCanvas.accept){
				gameCanvas.controlReset();
				if(refusingReason==2){
					destCurX=targetCurX; destCurY=targetCurY; 					
					troopsAtRegion[0]=map.getRegionByPos(mapCurX,mapCurY).numUnitsInfantry;
					troopsAtRegion[1]=map.getRegionByPos(mapCurX,mapCurY).numUnitsArchers;
					troopsAtRegion[2]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCavalry;
					troopsAtRegion[3]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCatapults;
					moveTroopsAmount[0]=0;
					moveTroopsAmount[1]=0;
					moveTroopsAmount[2]=0;
					moveTroopsAmount[3]=0;
					troopType=0;
					lastState=state;
					moveCase=CONQUERMOVE;
					set_state(MOVE_TROOPS_DETAILS);					
				}else					
				set_state(WORLDMAP_NAVIGATE);				
			}
			break;						
			
			case NOT_ENOUGH_RESOURCES :
			if(gameCanvas.accept){
				gameCanvas.controlReset();
				set_state(MOVE_TROOPS_DETAILS);				
			}
			break;		
			
			case CANNOT_RETREAT :
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				set_state(BATTLE);
			}			
			doPaint=true;
			break;										
			
			case CONFIRM_RETREAT :
			if(gameCanvas.accept){
				
				gameCanvas.controlReset();
				
				moveTroopsAmount[0]=0;
				moveTroopsAmount[1]=0;
				moveTroopsAmount[2]=0;
				moveTroopsAmount[3]=0;
				
				for(int x=0; x<warField.sx; x++)
				for(int z=0; z<warField.sz; z++)				
					if(warField.tiles[x][z].UnitSide==0)
						moveTroopsAmount[warField.tiles[x][z].UnitType]+=warField.tiles[x][z].UnitAmount;
													
				/*moveTroopsAmount[0]=map.getRegionByPos(mapCurX,mapCurY).numUnitsInfantry;					
				moveTroopsAmount[1]=map.getRegionByPos(mapCurX,mapCurY).numUnitsArchers;					
				moveTroopsAmount[2]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCavalry;					
				moveTroopsAmount[3]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCatapults;*/					
					
				if((myFood>=foodNeeded() && myFuel>=fuelNeeded()) || IAttack){
															
					processBattleResults(false);				
					/*warField=null; cam=null; System.gc();
					gameCanvas.destroyWarGfx();	*/
					shutDownBattle();
					set_state(WAIT_RESPONSE);
					
				}else{
					set_state(CANNOT_RETREAT);
				}
			}
			if(gameCanvas.men){
				gameCanvas.controlReset();
				set_state(BATTLE);
			}
			break;
			
			case OPPONENT_RETREATED :
			if(gameCanvas.accept){	
				gameCanvas.controlReset();		
				processBattleResults(true);						
				shutDownBattle();
				if(IAttack){
					destCurX=targetCurX; destCurY=targetCurY; 					
					moveTroopsAmount[0]=0;
					moveTroopsAmount[1]=0;
					moveTroopsAmount[2]=0;
					moveTroopsAmount[3]=0;
					troopType=0;
					lastState=state;
					moveCase=CONQUERMOVE;
					set_state(MOVE_TROOPS_DETAILS);				
				}else
					set_state(WORLDMAP_NAVIGATE);				
			}
			break;
			
			case BATTLE_REQUEST :
			if(gameCanvas.accept){				
				set_state(99);
				sendAttackAccept(true);
			}
			if(gameCanvas.men){
				sendAttackAccept(false);
				gameCanvas.controlReset();
				troopsAtRegion[0]=map.getRegionByPos(mapCurX,mapCurY).numUnitsInfantry;
				troopsAtRegion[1]=map.getRegionByPos(mapCurX,mapCurY).numUnitsArchers;
				troopsAtRegion[2]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCavalry;
				troopsAtRegion[3]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCatapults;										
				moveCase=ESCAPEMOVE;
				set_state(WORLDMAP_DESTSELECT);
			}			
			break;
			
			case FLAG_EDIT :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.left) {gameCanvas.controlReset(); if(mapCurX>0) mapCurX--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); if(mapCurX<10) mapCurX++;}
			if(gameCanvas.up) {gameCanvas.controlReset(); if(mapCurY>0) mapCurY--;}
			if(gameCanvas.down) {gameCanvas.controlReset(); if(mapCurY<10) mapCurY++;}	
			if(gameCanvas.men) {saveData(); gameCanvas.controlReset(); set_state(MAIN_MENU);}	
			if(gameCanvas.but1) {
				gameCanvas.controlReset(); 
				myFlag[mapCurY*11+mapCurX]=(byte)currentColor;				
				gameCanvas.createFlag(0,myFlag);
			}	
			if(gameCanvas.but3) {
				gameCanvas.controlReset(); 						
				currentColor=(currentColor+1)%16;
			}
			if(gameCanvas.but4) {
				gameCanvas.controlReset(); 						
				set_state(CLEAR_FLAG_CONFIRM);
			}
			break;
			
			case CLEAR_FLAG_CONFIRM :
			if(gameCanvas.accept || gameCanvas.men){							
				if(gameCanvas.accept){
					for(int i=0; i<121; i++) myFlag[i]=0; 
					gameCanvas.createFlag(0,myFlag);
				}
				gameCanvas.controlReset();			
				set_state(FLAG_EDIT);
			}
			break;
			
			
			case CREDITS :
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				set_state(MAIN_MENU);
			}			
			doPaint=true;
			break;
			
			case CREDITS2 :
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				set_state(OPTIONS);
			}			
			break;			
			
			case OPTIONS :
			if(gameCanvas.any) doPaint=true;
			if(gameCanvas.up) {gameCanvas.controlReset(); subMenuOp=(3+(subMenuOp-1))%3;}
			if(gameCanvas.down) {gameCanvas.controlReset(); subMenuOp=(subMenuOp+1)%3;}
			if(gameCanvas.men) {
				gameCanvas.controlReset(); 
				switch(regionType){
					case MYREGION : set_state(WORLDMAP_MYREGION_MENU); break;
					case OTHERREGION : set_state(WORLDMAP_OTHERREGION_MENU); break;
					case EMPTY : set_state(WORLDMAP_EMPTY_MENU); break;
				}				
			}
			if(gameCanvas.accept){
				gameCanvas.controlReset();
				switch(subMenuOp){
					case 0 : if(sound==false) sound=true; else sound=false; break;
					case 1 : set_state(CREDITS2); break;
					case 2 : set_state(CONFIRM_EXIT); break;
				}
			}
			subMenuOp=(3+subMenuOp)%3;
			break;			
			
			case CONFIRM_EXIT :
			if(gameCanvas.accept){
				gameCanvas.controlReset();
				sendQuit(); isConnected=false; isGameRunning=false;
				gameCanvas.destroyMapGfx();
				gameCanvas.loadPicture();
				set_state(MAIN_MENU);
			}
			if(gameCanvas.men){
				gameCanvas.controlReset();
				set_state(OPTIONS);
			}
			break;
						
			case GAME_ENDED :
			if(gameCanvas.anybut){
				gameCanvas.controlReset();	
				mapScrollY=0;			
				set_state(GAME_END_RESULTS);
			}			
			doPaint=true;
			break;
			
			case SURVIVOR :
			if(gameCanvas.anybut){
				gameCanvas.controlReset();	
				sendQuit(); isConnected=false; isGameRunning=false;
				gameCanvas.destroyMapGfx();
				gameCanvas.loadPicture();
				set_state(MAIN_MENU);
			}			
			doPaint=true;
			break;
														
			case GAME_END_RESULTS :
			if(gameCanvas.up && mapScrollY>0) mapScrollY-=2;
			if(gameCanvas.down && mapScrollY<30*numPlayers) mapScrollY+=2;
			if(gameCanvas.anybut){
				gameCanvas.controlReset();
				sendQuit(); isConnected=false; isGameRunning=false;
				gameCanvas.destroyMapGfx();
				gameCanvas.loadPicture();
				set_state(MAIN_MENU);
			}			
			doPaint=true;
			break;
			
			case OLD_VERSION :
			if(gameCanvas.anybut){
				gameCanvas.controlReset();					
				set_state(LOBBY_WAIT_GAME_INIT);
			}			
			doPaint=true;
			break;
			
			case TOO_OLD_VERSION :
			if(gameCanvas.anybut){
				gameCanvas.controlReset();					
				sendQuit(); isConnected=false; isGameRunning=false;				
				set_state(MAIN_MENU);
			}			
			doPaint=true;
			break;			
			
			case CONFIRM_RESET :
			if(gameCanvas.accept){
				gameCanvas.controlReset();
				sendQuit(); isConnected=false; isGameRunning=false;
				gameCanvas.destroyMapGfx();
				gameCanvas.loadPicture();
				set_state(MAIN_MENU);
			}
			if(gameCanvas.men){
				gameCanvas.controlReset();
				set_state(WAIT_RESPONSE);
			}
			break;
			
			case WAIT_RESPONSE :
			if(gameCanvas.men){
				gameCanvas.controlReset();
				set_state(CONFIRM_RESET);
			}
			break;
			
			
		}		
		
		if(isGameRunning){
			moneyTimer++;
			if(moneyTimer>=900){
				myMoney+=map.gainPerRegions(myPlayerId);
				moneyTimer=0;
				sendUpdateMyValues();				
			}
			
			long elapsed = (System.currentTimeMillis()-initialMillis),
			 	 elapsedSeconds = elapsed / 1000,
				 elapsedMinutes = elapsed / 60000;
			
			if(elapsedSeconds % 60 < 1 && totalTime - elapsedMinutes != 0 && (totalTime - elapsedMinutes == 1 || (elapsedMinutes) % 5 == 0))
			{
				chatTimer=300; chatString=Text.minutesLeft+(totalTime - elapsedMinutes);
			}
		}
		
				
		if(doPaint || cnt==1){
			gameCanvas.allowPaint=true;
			gameCanvas.repaint();
			gameCanvas.serviceRepaints();																													
		}
	}
	
	void processBattleTime()
	{
		battleTimer++;
		if(battleTimer>=BATTLE_ROUND_TIME){					
				set_state(BATTLE_WAIT_RESPONSE);
				round.send();				
		}		
	}
	
	void battleCamFollow(int xx, int zz, int d)
	{
			if(cam.z<((zz-4)<<10)+200) cam.z+=d;
			if(cam.z>((zz-3)<<10)-200) cam.z-=d;
			if(cam.x<((xx-2)<<10)+200) cam.x+=d;
			if(cam.x>((xx-1)<<10)-200) cam.x-=d;
			cam.recalculateMatrix();			
	}
	
	void setDestinationXY(int dest)
	{
		switch(dest){
				case 0 : destCurX=mapCurX+1; destCurY=mapCurY; break;
				case 1 : if(mapCurY%2==1) destCurX=mapCurX+1;
										  else destCurX=mapCurX;
						 destCurY=mapCurY-1;
						 break;								
				case 2 : if(mapCurY%2==1) destCurX=mapCurX;
										  else destCurX=mapCurX-1;
						 destCurY=mapCurY-1;
						 break;														
				case 3 : destCurX=mapCurX-1; destCurY=mapCurY; break;													
				case 4 : if(mapCurY%2==1) destCurX=mapCurX;
										  else destCurX=mapCurX-1;
						 destCurY=mapCurY+1;
						 break;																						
				case 5 : if(mapCurY%2==1) destCurX=mapCurX+1;
										  else destCurX=mapCurX;
						 destCurY=mapCurY+1;
						 break;								
			}						
	}
	
	void setBattleDestinationXY(int dest)
	{
		switch(dest){
				case 0 : destCurX=batCurX+1; destCurY=batCurZ; break;
				case 1 : destCurX=batCurX+1; destCurY=batCurZ+1; break;
				case 2 : destCurX=batCurX; destCurY=batCurZ+1; break;
				case 3 : destCurX=batCurX-1; destCurY=batCurZ+1; break;
				case 4 : destCurX=batCurX-1; destCurY=batCurZ; break;
				case 5 : destCurX=batCurX-1; destCurY=batCurZ-1; break;
				case 6 : destCurX=batCurX; destCurY=batCurZ-1; break;
				case 7 : destCurX=batCurX+1; destCurY=batCurZ-1; break;				
			}						
	}
	
	void setLongRangeDestinationXY()
	{
		destCurX=batCurX+((GameCanvas.cos(shotAngle)*shotPower+512)>>20);	
		destCurY=batCurZ+((GameCanvas.sin(shotAngle)*shotPower+512)>>20);			
	}
	
	int angleDirection(int orix, int oriz, int desx, int desz)
	{
		int angle = GameCanvas.atan(desz-oriz, desx-orix);
		//System.out.println(""+angle);
		return angle/2;
	}
	
	public int foodNeeded()
	{
		return moveTroopsAmount[0]+moveTroopsAmount[1]*5+moveTroopsAmount[2]*10;
	}	
		
	public int fuelNeeded()
	{
		return moveTroopsAmount[3];
	}	
	
	/*===================================================================================*/
	public void setUpBattle(int idRegionAttacker, int idRegionDefender,
								int numAttCatapults, int numAttArchers, int numAttCavalary, int numAttInfantry,
								int numDefCatapults, int numDefArchers, int numDefCavalary, int numDefInfantry)
	{		

		set_state(99);		
		gameCanvas.destroyMapGfx();
				
		batCurX=3; batCurZ=3;
		cam=new Camera((batCurX-1)<<10,2000,(batCurZ-3)<<10,16,8,0);		
		cam.recalculateMatrix();					
								
		warField=new WarField(12,7);		
		if(map.getRegionById(idRegionAttacker).idPlayer==myPlayerId){
			IAttack=true;
			batOpponentId=map.getRegionById(idRegionDefender).idPlayer;
			warField.placeTroops(numAttInfantry,numAttArchers,numAttCavalary,numAttCatapults,numDefInfantry,numDefArchers,numDefCavalary,numDefCatapults);
		}else{
			IAttack=false;
			batOpponentId=map.getRegionById(idRegionAttacker).idPlayer;			
			warField.placeTroops(numDefInfantry,numDefArchers,numDefCavalary,numDefCatapults,numAttInfantry,numAttArchers,numAttCavalary,numAttCatapults);
		}
		
		round=new Round(myPlayerId); actionPoints=250; nextBattleAction=0; battleTimer=0;				
		System.gc();
		
		gameCanvas.loadWarGfx();
		set_state(BATTLE);
				
	}
	
	public void shutDownBattle()
	{
		warField=null; cam=null; round=null; System.gc();
		gameCanvas.destroyWarGfx();				
		//set_state(WORLDMAP_NAVIGATE);		
	}
	
	public void displayBattle(Round r)
	{
		round=null; System.gc();
		round=r; round.sortByTime(); r.reverseOpponentActions(warField);
		gameCanvas.playMusic(2,1);
		nextBattleAction=0; set_state(PROCESS_NEXT_BATTLEACTION);		
	}
	
	public void processBattleResults(boolean winner)
	{
		int winUnits[]={0,0,0,0}, losUnits[]={0,0,0,0}, idPlayerWins, idPlayerLoses;
		
		for(int i=0; i<warField.sx; i++)
		for(int j=0; j<warField.sz; j++)
			
			switch(warField.tiles[i][j].UnitSide){
				
				case 0 :
				if(winner) winUnits[warField.tiles[i][j].UnitType]+=warField.tiles[i][j].UnitAmount;	
				else	   losUnits[warField.tiles[i][j].UnitType]+=warField.tiles[i][j].UnitAmount;	
				break;
				
				case 1 :
				if(!winner) winUnits[warField.tiles[i][j].UnitType]+=warField.tiles[i][j].UnitAmount;	
				else	   losUnits[warField.tiles[i][j].UnitType]+=warField.tiles[i][j].UnitAmount;	
				break;								
			}			
						
		if(winner) {idPlayerWins=myPlayerId; idPlayerLoses=batOpponentId; 
					for(int i=0; i<4; i++)
						troopsAtRegion[i]=winUnits[i];
					}
		else {idPlayerWins=batOpponentId; idPlayerLoses=myPlayerId; 
					for(int i=0; i<4; i++)
						troopsAtRegion[i]=losUnits[i];		
					}
		//System.out.println("Winner:"+winUnits[0]+","+winUnits[1]+","+winUnits[2]+","+winUnits[3]+" Loser:"+losUnits[0]+","+losUnits[1]+","+losUnits[2]+","+losUnits[3]+" MY UNITS:"+troopsAtRegion[0]+","+troopsAtRegion[1]+","+troopsAtRegion[2]+","+troopsAtRegion[3]);
		
		sendBattleResults (idPlayerWins,idPlayerLoses,winUnits[3],winUnits[1],winUnits[2],winUnits[0],losUnits[3],losUnits[1],losUnits[2],losUnits[0]);		
	}	

/*	
	public void notifyAttackRefused(int reason)
	{		
		refusingReason=reason;
		set_state(BATTLE_REFUSED);			
	}
*/	
/*
	public void notifyEndBattle ()
	{
		set_state(OPPONENT_RETREATED);	
	}
*/

/*	
	public void notifyAttackQuery (int idRegionAttacker, int idRegionDefender)
	{
		batOpponentId=map.getRegionById(idRegionAttacker).idPlayer;
		mapCurX=idRegionDefender%map.hexaWidth;
		mapCurY=idRegionDefender/map.hexaWidth;
		
		moveTroopsAmount[0]=map.getRegionByPos(mapCurX,mapCurY).numUnitsInfantry;					
		moveTroopsAmount[1]=map.getRegionByPos(mapCurX,mapCurY).numUnitsArchers;					
		moveTroopsAmount[2]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCavalry;					
		moveTroopsAmount[3]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCatapults;					
		
		if(myFood>=foodNeeded() && myFuel>=fuelNeeded())
			set_state(BATTLE_REQUEST);	
		else {
			sendAttackAccept(true);
			set_state(99);
		}
	}
*/
/*	
	public void postChatMessage (int idPlayer, String message)
	{
		chatTimer=300; chatString=message;
	}
*/	
	public void loadData()
	{
		byte[] GameData=null;	
		RecordStore rs;
		 		
		myFlag=new byte[121];
			
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
	
			if (rs.getNumRecords() == 0){
												
				//System.out.println("Warning! Default setting...");
				
				for(int i=0; i<121; i++) myFlag[i]=0;
				
				ByteArrayOutputStream bstream = new ByteArrayOutputStream();
                DataOutputStream ostream = new DataOutputStream(bstream);				
								
			    //ostream.writeBoolean(sound);
			    ostream.write(myFlag,0,121);
			    ostream.writeUTF(myName);
			    ostream.writeBoolean(sound);
			    ostream.writeBoolean(selfFlag);
									
	            ostream.flush();
	            ostream.close();
	                					              
	            GameData = bstream.toByteArray();
	            rs.addRecord(GameData, 0, GameData.length);									
														                																					
			} else {
				
				//System.out.println("Retrieve settings...");
											
				GameData = rs.getRecord(1);
								
                DataInputStream istream = new DataInputStream(new ByteArrayInputStream(GameData, 0, GameData.length));				
                    		
			    //sound=istream.readBoolean();
			    istream.read(myFlag,0,121);	
			    myName=istream.readUTF();	
				sound=istream.readBoolean();
			    selfFlag=istream.readBoolean();			    
                    		                    		                    												
			}
			rs.closeRecordStore();
			
		} catch(Exception e) {/*System.out.println("EXCP");*/}
			
	}
																																																															
	public void saveData()
	{
		byte[] GameData=null;	
		RecordStore rs=null;		
		 					
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
					
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
            DataOutputStream ostream = new DataOutputStream(bstream);				
											
				ostream.write(myFlag,0,121);	
				ostream.writeUTF(myName);				
		        ostream.writeBoolean(sound);
		        ostream.writeBoolean(selfFlag);
			
      			ostream.flush();
              	ostream.close();
                	
              	GameData = bstream.toByteArray();
	                	
               	rs.setRecord(1, GameData, 0, GameData.length);						                																								
			
			                        					              			
		} catch(Exception e) {/*System.out.println("Exception in saving");*/}
		finally {
                    if (rs != null) {
                        try {
                            rs.closeRecordStore();
                        } catch(Exception e) {
                        }
                    }
                }
			
	}
	
	public void notifyPlayerInfo (int idPlayer, String playerName, byte [] arrayFlag)
	{
		
		idPlayers[idPlayer-1]=idPlayer;
		strPlayerNames[idPlayer-1]=playerName;		
		if(arrayFlag!=null)
			gameCanvas.createFlag(idPlayer-1, arrayFlag);					
		playerFlags[idPlayer-1]=arrayFlag;
								
	}

/*	
	public void notifyEndOfGame (int idPlayerWon)
	{
		idWinner=idPlayerWon;
		set_state(SURVIVOR);	
	}	
*/

	public void notifyTimeEndOfGame (int idWin, int scoreWin,
									 int numCurPlayers,
									 int[] playersId, int[] gloScores, int[] regScores, int[] monScores, int[] armScores)
	{

		set_state(99);
		
		resIdWinner=idWin;
		resScoreWinner=scoreWin;
		numPlayers=numCurPlayers;
		resPlIds=playersId;
		resGloScores=gloScores;
		resRegScores=regScores;
		resMonScores=monScores;
		resArmScores=armScores;
						
		if(warField!=null) shutDownBattle();		
		set_state(GAME_ENDED); gameCanvas.playMusic(5,1);		
		if(!gameCanvas.isShown())
			display.setCurrent(gameCanvas);
	}
	
	
/*	public void notifyLoginFailed ()
	{
		set_state(LOGIN_FAILED);	
	}
*/
/*	
	public void notifyGameLobbyMessage (String msg)
	{
		chatTimer=300; chatString=msg;
	}	
	
	public void notifyGameLobbyMovingToGame ()
	{
		set_state(GAME_STARTING);
	}	
*/	
	public void notifyGameLobbyPlayerInfo (int idPlayer, String playerNameAndInfo)
	{
		int i=0;
		
		while(waitingPlayersIds[i]!=-1 && waitingPlayersIds[i]!=idPlayer && i<waitingPlayers.length)
			i++;
		
		if(i<waitingPlayers.length && waitingPlayersIds[i]!=idPlayer){
			waitingPlayers[i] = playerNameAndInfo;
			waitingPlayersIds[i] = idPlayer;		
			waitingPlayersNumber++;	
		}
	}	

	public void notifyGameLobbyPlayerHasQuit (int idPlayer)
	{
		int i=0;
		
		while(i<waitingPlayers.length && waitingPlayersIds[i]!=idPlayer)
			i++;
		
		if(i<waitingPlayers.length){
			
			for(int j=i; j<waitingPlayersIds.length-1; j++){
				waitingPlayersIds[j] = waitingPlayersIds[j+1];
				waitingPlayers[j] = waitingPlayers[j+1];
			}
			waitingPlayersIds[waitingPlayersIds.length-1]=-1;
			System.gc();
			waitingPlayersNumber--;	
		}
		
	}	
	
/*		
	public void notifyStartUpLobbyCreateGameAck ()
	{
		set_state(LOBBY_WAIT_GAME_INIT);
	}	

	public void notifyStartUpGameDeleted (long appId)
	{
		int i=0;
		
		while(currentGames[i]!=null && currentGames[i].appId!=appId && i<currentGames.length)
			i++;
			
		if(i<currentGames.length && currentGames[i]!=null){
			currentGames[i]=null;
			for(int j=i; j<currentGamesNumber-1; j++)
				currentGames[j]=currentGames[j+1];
			System.gc();
			currentGamesNumber--;
		}		
	}	
*/	
	
	public void notifyStartUpGameInfo (long appId, String gameName, int numPlayers, int initialMoney, int initialArmy, int playTime)
	{
		int i=0;
		
		while(currentGames[i]!=null && i<currentGames.length)
			i++;
		
		// Found empty space for a game
		if(i<currentGames.length && currentGames[i]==null){
			currentGames[i] = new lobbyGameInfo();
			currentGames[i].appId = appId;
			currentGames[i].gameName = gameName;
			currentGames[i].numPlayers = numPlayers;
			currentGames[i].initialMoney = initialMoney; 
			currentGames[i].initialArmy = initialArmy;
			currentGames[i].playTime = playTime;
			currentGamesNumber++;
		}		
	}	
	
	
	public long freeMemory()
	{
		Runtime runtime = Runtime.getRuntime();
    	runtime.gc();

		//free = runtime.freeMemory();
		//total = runtime.totalMemory();
		return runtime.freeMemory();
	}


	private void notifyRetreatConfirmation (boolean isAck)
	{
		// isAck == true  => es pot retirar 
		
		// isAck == false => no es pot retirar
		//					   si esta en batalla continuar
		//					   si no esta en batalla, significa que no has pogut moure on volies (et contesta al placeTroops)
		
		if(!isAck) set_state(CANNOT_RETREAT);
		else{
		
			if(IAttack) set_state(WORLDMAP_NAVIGATE);
			else {
				moveCase=ESCAPEMOVE;
				set_state(WORLDMAP_DESTSELECT);
			}
		}
		
	}



	String	lobby_arl="BWNL";
	
	static GameMidletLogic	theGameMidlet;

	//////////////////////////////////////

	// incoming messages
	static final byte 		MT_BUY_FOOD_ACK			= 1;
	static final byte 		MT_BUY_TROOPS_ACK		= 2;
	static final byte 		MT_MOVE_TROOPS_ACK		= 3;
	static final byte 		MT_MESSAGE_NOTIFY		= 4;
	static final byte 		MT_EXIT_PLAYER_NOTIFY	= 5;
	static final byte 		MT_ATTACK_QUERY			= 6;
	static final byte 		MT_ATTACK_ACK			= 7;
	static final byte 		MT_REGION_UPDATE		= 8;
	static final byte 		MT_END_OF_PERIOD		= 9;
	static final byte 		MT_BATTLE_END_TURN		= 10;

	static final byte		MT_PLAYER_INFO			= 11;
	static final byte		MT_ATTACK_NACK			= 12;
	static final byte		MT_END_BATTLE_NOTIFY	= 13;

	static final byte		MT_ALL_PLAYERS_INGAME 	= 99;
	static final byte 		MT_WELCOME				= 100;
	static final byte		MT_SEND_HEXAMAP			= 101;
	static final byte		MT_END_OF_GAME			= 102;

	static final byte		MT_TIME_END_OF_GAME		= 103;
	static final byte		MT_RETREAT_ACK			= 104;

//	static final byte		MT_LOBBY_WAITING_LIST_UPDATE	= 103;
//	static final byte		MT_LOBBY_END_LOBBY				= 104;


		static final byte	MT_GAMELOBBY_QUIT_GAME_LOBBY	= 121;
		static final byte	MT_GAMELOBBY_QUIT				= 122;
		static final byte	MT_GAMELOBBY_SEND_MESSAGE		= 123;

		static final byte	MT_STARTUPLOBBY_JOIN_GAME		= 124;
		static final byte	MT_STARTUPLOBBY_CREATE_GAME		= 125;
		static final byte	MT_STARTUPLOBBY_QUIT			= 126;


	// outgoing messages
	static final byte		MT_BUY_FOOD				= -1;
	static final byte		MT_BUY_TROOPS			= -2;
	static final byte		MT_MOVE_TROOPS			= -3;
	static final byte		MT_MESSAGE				= -4;
	static final byte		MT_QUIT					= -5;
	static final byte		MT_ATTACK_FIRST			= -6;
	static final byte		MT_ATTACK_ACCEPT		= -7;
	static final byte		MT_BATTLE_ACTION		= -8;
	static final byte		MT_BATTLE_RESULT		= -9;

	static final byte		MT_REFRESH_MAP			= -10;
	static final byte		MT_UPDATE_MY_VALUES		= -11;
	static final byte		MT_MY_PLAYER_INFO	    = -12;

	static final byte		MT_PLACE_TROOPS			= -66;

/*
	static final byte		MT_LOBBY_JOIN_WAITING_LIST	= -20;
	static final byte		MT_LOBBY_QUIT_WAITING_LIST	= -21;
	static final byte		MT_LOBBY_QUIT_APPLICATION	= -22;
*/


	static final byte		MT_GAMELOBBY_MESSAGE_NOTIFY			= -13;
	static final byte		MT_GAMELOBBY_PLAYER_INFO_NOTIFY		= -14;
	static final byte		MT_GAMELOBBY_MOVING_TO_GAME_NOTIFY	= -15;
	static final byte		MT_GAMELOBBY_PLAYER_QUIT_NOTIFY		= -20;

	static final byte		MT_STARTUPLOBBY_CREATE_GAME_ACK		= -16;
	static final byte		MT_STARTUPLOBBY_DELETE_GAME_NOTIFY	= -17;
	static final byte		MT_STARTUPLOBBY_GAME_INFO_NOTIFY	= -18;
	static final byte		MT_STARTUPLOBBY_LOBBY_FULL_NOTIFY	= -19;
	static final byte  		MT_GAMELOBBY_WORKING_VERSIONS  		= -22;


	static final byte		MT_WHOAMI							= -27;
 

	///////////////////////////////////////////////////////////// App Id	
	static final byte		APP_ID_STARTUPLOBBY 	= 0;
	static final byte		APP_ID_GAMELOBBY 		= 1;
	static final byte		APP_ID_GAMEAPP	 		= 2;
	////////////////////////////////////////////////////////////////////


   ///////////////////////////////////////
   // associations
	
	final byte	NUM_MAP_COLS = 14;
	final byte	NUM_MAP_ROWS = 14;

	private Connector connector = new Connector();
    //public com.mygdx.mongojocs.bravewar.HTTPThreadListener	threadCommListener;
    public Map 					map = new Map (NUM_MAP_COLS, NUM_MAP_ROWS);
    //public Battle				battle; 


	int		myPlayerId, initialMoneyValue;
	int		mapCurX, mapCurY;

	int		myMoney, myFood, myFuel;


	public Display  display;
    

	boolean		isConnected = false;
	boolean		isGameRunning = false;
	boolean		isBattleAccepted = false;
	boolean		isMapUpdated = false;

//	Vector		playersWaitingList = new Vector ();

	/////////////////////////////////////
	// operations
	

//////////////////////////////////////////////////////////////////////////////////
    // Pecan LoginListener handler

/*
    public void onLoginFailed()
    {
    	//System.out.println ("LoginFailed");
    	//notifyLoginFailed();
		set_state(LOGIN_FAILED);
    }
        
    // Pecan LoginListener handler
    public void onLoginSuccessful(com.mygdx.mongojocs.bravewar.Connector connector, Message[] availablePlayers, java.lang.Object context)
    {
		//System.out.println ("Login successful.");
/ *
        Message joinMessage = new Message();
        joinMessage.ARL=lobby_arl;
        joinMessage.params=new Hashtable ();
        try {
            //connector.sendData(joinMessage);
	    	threadCommListener = new com.mygdx.mongojocs.bravewar.HTTPThreadListener (gameMidlet, this, connector);

            threadCommListener.start ();
            }
        catch(Exception e)
            {
            e.printStackTrace ();
            }
            
* /
        try {
	        // We should start the lobby, and wait until the game starts...
	        Message msg = null;
	        Message connect = new Message();
	        connect.ARL = Conf.getAppProperty("ARL");
	        connect.params = new Hashtable();
	        msg = connector.sendData(connect);
	        
	        Hashtable htMsg = msg.params;
	        // for (Enumeration e=htMsg.elements (); e.hasMoreElements ();)
	        //	System.out.println (e.nextElement ());
	        	
	        	
	        String	strCommBuf = (String) htMsg.get ("commBuffer");
	        
	        //if (strCommBuf!=null)
	        //System.out.println ("commBuffer is : "+strCommBuf);
	
			threadCommListener = new com.mygdx.mongojocs.bravewar.HTTPThreadListener (this, connector);
			threadCommListener.sendToParse (msg);
			threadCommListener.start ();
			
			isConnected = true;
			
			//System.out.println ("Connected");
			
			//sendJoinWaitingList (2);

			//gameCanvas.joiningGame ();
			
			//display.setCurrent (gameCanvas);
    	}
        catch(Exception e)
            {
            e.printStackTrace ();
            }

	}
*/
//////////////////////////////////////////////////////////////////////////////////

	private int readShort (byte [] data, int pos)
	{
		short	numShort;
		
		int		numData0 = data [0] < 0 ? 256+data [0] : data [0];
		int		numData1 = data [1] < 0 ? 256+data [1] : data [1];
		
		numShort   = (short) (numData1 << 8);
		numShort  |= numData0;
		
		return numShort;
	}

	private void writeShort (int num, byte [] data, int pos)
	{
		short	numShort = (short)num;
		
		data [0] = (byte)((numShort & 0xff));
		data [1] = (byte)((numShort & 0xff00) >> 8);
	}
	

	public void appendShort (StringBuffer strBuf, short num)
	{
		short	r = num;
		byte	t;
		
		t = (byte) ((r & 0xf000) >> 12);
		strBuf.append ((char)(t > 9 ? 'a'-10+t : '0'+t));
		t = (byte) ((r & 0xf00) >> 8);
		strBuf.append ((char)(t > 9 ? 'a'-10+t : '0'+t));
		t = (byte) ((r & 0xf0) >> 4);
		strBuf.append ((char)(t > 9 ? 'a'-10+t : '0'+t));
		t = (byte) (r & 0xf);
		strBuf.append ((char)(t > 9 ? 'a'-10+t : '0'+t));
	}

	public void appendByte (StringBuffer strBuf, byte num)
	{
		byte	r = num;
		byte	t;
		
		t = (byte) ((r & 0xf0) >> 4);
		strBuf.append ((char)(t > 9 ? 'a'-10+t : '0'+t));
		t = (byte) (r & 0xf);
		strBuf.append ((char)(t > 9 ? 'a'-10+t : '0'+t));
	}

	public byte readByte (StringBuffer strBuf, int pos)
	{
		char	t;
		byte	ln, hn;
		
		t = strBuf.charAt (pos);
		hn = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');

		t = strBuf.charAt (pos+1);
		ln = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		
		return (byte) ((hn<<4)+ln);
	}

	public short readShort (StringBuffer strBuf, int pos)
	{
		char	t;
		byte	lln, lhn, hln, hhn;
		
		t = strBuf.charAt (pos);
		hhn = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		t = strBuf.charAt (pos+1);
		hln = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		t = strBuf.charAt (pos+2);
		lhn = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		t = strBuf.charAt (pos+3);
		lln = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		
		return (short) ((hhn<<12)+(hln<<8)+(lhn<<4)+(lln));
	}

	public byte readByte (String strBuf, int pos)
	{
		char	t;
		byte	ln, hn;
		
		t = strBuf.charAt (pos);
		hn = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');

		t = strBuf.charAt (pos+1);
		ln = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		
		return (byte) ((hn<<4)+ln);
	}

	public short readShort (String strBuf, int pos)
	{
		char	t;
		byte	lln, lhn, hln, hhn;
		
		t = strBuf.charAt (pos);
		hhn = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		t = strBuf.charAt (pos+1);
		hln = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		t = strBuf.charAt (pos+2);
		lhn = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		t = strBuf.charAt (pos+3);
		lln = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
		
		return (short) ((hhn<<12)+(hln<<8)+(lhn<<4)+(lln));
	}

	public static void appendLong(StringBuffer strBuf, long num) {
		byte	t;
		long	shift = 60;
		for (int i = 0; i < 16; i++) {
			t = (byte)((num >> shift) & 0xf);
			strBuf.append((char)(t > 9 ? 'a'-10+t : '0'+t));
			shift -= 4;
		}
	}

	public static long readLong(String strBuf, int pos) {
		char	t;
		byte	nibble;
		int	shift = 60;
		long	ret = 0;

		for (int i = 0; i < 16; i++) {
			t = strBuf.charAt(pos+i);
			nibble = (byte)(t >= 'a' && t <= 'f' ? t-'a'+10 : t-'0');
			ret += (long)(nibble) << shift;
			shift -= 4;
		}

		return ret;
	}


	int		timeOutWrongState = 0;

/**
 * Does ...
 * 
 * @param type ...
 * @param data ...
 * 
 */
	public void parseMessage(byte type, String strMsg) {

		switch (type)
		{
			case MT_WHOAMI:
				{
				byte appId = readByte(strMsg, 0);		// AppId
				//readByte(strMsg, 2);		// CommBuffer that is sending the server
				//readByte(strMsg, 4);		// lasCommBuffer received by the server
				
				switch (appId)
				 {
					case APP_ID_STARTUPLOBBY:
						// Everything is ok.. do nothing
						timeOutWrongState = 0;
						break;
					case APP_ID_GAMELOBBY:
						if ((state==WAIT_RESPONSE && display.getCurrent() /***/==gameCanvas) || state==LOBBY_CHOOSE_GAME)
						{
							if (++timeOutWrongState > 3)
							{
								waitingPlayersNumber=0;
								numPlayers=0;
								set_state(LOBBY_WAIT_GAME_INIT);	
							}
						}
						break;
					case APP_ID_GAMEAPP:
						// Shutdown connection
						if (isGameRunning==false /**&& !connector.threadCommListener.bDisconnectWhenPossible*/)
						{
							// notificar que torni a intentar
							sendQuit();
							set_state(LOGIN_FAILED);
						}
						break;
				 }
				}
				break;
			case MT_BUY_FOOD_ACK:
				//System.out.println (" MT_BUY_FOOD parsed!"+readShort (strMsg, 0)+" units");
				map.buyFood (readShort (strMsg, 0));
				break;
//			case MT_BUY_TROOPS_ACK:
				//System.out.println (" MT_BUY_TROOPS parsed!");
				//map.buyTroops (	readShort (strMsg, 0*4),		// idRegion
				//				readShort (strMsg, 1*4),		// unitsCat
				//				readShort (strMsg, 2*4),		// unitsArc
				//				readShort (strMsg, 3*4),		// unitsCav
				//				readShort (strMsg, 4*4));		// unitsInf
//				break;
			case MT_MOVE_TROOPS_ACK:
				//System.out.println (" MT_MOVE_TROOPS parsed!");
				//map.moveTroops (readShort (strMsg, 0*4),
				//				readShort (strMsg, 1*4),
				//				readShort (strMsg, 2*4),
				//				readShort (strMsg, 3*4),
				//				readShort (strMsg, 4*4),
				//				readShort (strMsg, 5*4));
				break;
			case MT_MESSAGE_NOTIFY:
				//System.out.println (" MT_MESSAGE_NOTIFY parsed!");
				//postChatMessage(readShort (strMsg, 0*4), strMsg.substring (4));
				chatTimer=300; chatString=strMsg.substring (4); gameCanvas.playSound(3);

				//showMessage (data);
				break;
			case MT_EXIT_PLAYER_NOTIFY:
				//System.out.println (" MT_EXIT_PLAYER_NOTIFY parsed! PlayerId:"+readShort (strMsg, 0));
				//map.deletePlayer (readShort (strMsg, 0*2));
				break;
			case MT_ATTACK_QUERY:
				//System.out.println (" MT_ATTACK_QUERY parsed!");
				//notifyAttackQuery (	readShort (strMsg, 0*4),	// idRegionAttacker
				//					readShort (strMsg, 1*4)		// idRegionDefender
				//					);
				
		//	public void notifyAttackQuery (int idRegionAttacker, int idRegionDefender)
				batOpponentId=map.getRegionById(readShort (strMsg, 0*4)).idPlayer;
				mapCurX=readShort (strMsg, 1*4)%map.hexaWidth;
				mapCurY=readShort (strMsg, 1*4)/map.hexaWidth;
				
				moveTroopsAmount[0]=map.getRegionByPos(mapCurX,mapCurY).numUnitsInfantry;
				moveTroopsAmount[1]=map.getRegionByPos(mapCurX,mapCurY).numUnitsArchers;
				moveTroopsAmount[2]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCavalry;
				moveTroopsAmount[3]=map.getRegionByPos(mapCurX,mapCurY).numUnitsCatapults;
				
				if(myFood>=foodNeeded() && myFuel>=fuelNeeded()){
					if(!gameCanvas.isShown())
						display.setCurrent(gameCanvas);					
					gameCanvas.playSound(1);
					set_state(BATTLE_REQUEST);
				}else {
					sendAttackAccept(true);
					set_state(99);
				}				
				break;
			case MT_ATTACK_ACK:
				/*System.out.println (" MT_ATTACK_ACK parsed! "+
				
				readShort (strMsg, 0*4)+",	// idRegionAttacker"
									+readShort (strMsg, 1*4)+",	// idRegionDefender"
									+readShort (strMsg, 2*4)+",	// Attacker.numCatapults"
									+readShort (strMsg, 3*4)+",	// Attacker.numArchers"
									+readShort (strMsg, 4*4)+",	// Attacker.numCavalry"
									+readShort (strMsg, 5*4)+",	// Attacker.numInfantry"
									+readShort (strMsg, 6*4)+",	// Defender.numCatapults"
									+readShort (strMsg, 7*4)+",	// Defender.numArchers"
									+readShort (strMsg, 8*4)+",	// Defender.numCavalry"
									+readShort (strMsg, 9*4)+");	// Defender.numInfantry"
				);*/
				/*
		appendShort (playerToSend.commStringBuffer, (short) idRegionAttacker);
		appendShort (playerToSend.commStringBuffer, (short) idRegionDefender);
		appendShort (playerToSend.commStringBuffer, (short) regAttacker.numCatapults);
		appendShort (playerToSend.commStringBuffer, (short) regAttacker.numArchers);
		appendShort (playerToSend.commStringBuffer, (short) regAttacker.numCavalry);
		appendShort (playerToSend.commStringBuffer, (short) regAttacker.numInfantry);
		appendShort (playerToSend.commStringBuffer, (short) regDefender.numCatapults);
		appendShort (playerToSend.commStringBuffer, (short) regDefender.numArchers);
		appendShort (playerToSend.commStringBuffer, (short) regDefender.numCavalry);
		appendShort (playerToSend.commStringBuffer, (short) regDefender.numInfantry);
				*/
				try {
				if(!gameCanvas.isShown())
					display.setCurrent(gameCanvas);
				
				setUpBattle(		readShort (strMsg, 0*4),	// idRegionAttacker
									readShort (strMsg, 1*4),	// idRegionDefender
									readShort (strMsg, 2*4),	// Attacker.numCatapults
									readShort (strMsg, 3*4),	// Attacker.numArchers
									readShort (strMsg, 4*4),	// Attacker.numCavalry
									readShort (strMsg, 5*4),	// Attacker.numInfantry
									readShort (strMsg, 6*4),	// Defender.numCatapults
									readShort (strMsg, 7*4),	// Defender.numArchers
									readShort (strMsg, 8*4),	// Defender.numCavalry
									readShort (strMsg, 9*4));	// Defender.numInfantry
				}
				catch (Exception e)
				{
					e.printStackTrace();
					e.toString();
				}
				
				break;
			case MT_ATTACK_NACK:
				//System.out.println (" MT_ATTACK_NACK parsed!");
				
				//notifyAttackRefused (readByte (strMsg, 0));
			//	public void notifyAttackRefused(int reason)
					refusingReason=readByte (strMsg, 0);
					if(state==WAIT_RESPONSE) set_state(BATTLE_REFUSED);			
				
				break;
			case MT_REGION_UPDATE:
				//System.out.println (" MT_REGION_UPDATE parsed!");
				map.updateRegion (	readShort (strMsg, 0*4),	// idRegion
									readShort (strMsg, 1*4),	// idPlayer
									readShort (strMsg, 2*4),	// unitsCat
									readShort (strMsg, 3*4),	// unitsArc
									readShort (strMsg, 4*4),	// unitsCav
									readShort (strMsg, 5*4));	// unitsInf
				//gameCanvas.repaint ();
				isMapUpdated=true;
				break;
			case MT_END_OF_PERIOD:
				//System.out.println (" MT_END_OF_PERIOD parsed!");
				/*
				showMessage ("End of Month, food eaten, money raised, men lost");
				map.updateFood ();
				map.updateMoney ();	
				*/
				break;
			case MT_BATTLE_END_TURN:
				//System.out.println (" MT_BATTLE_END_TURN parsed!");
				/*
				Vector	vecBattleActions = parseBattleActions (data);
				sendEventsToDisplay (vecBattleActions);
				*/
				
				parseEndOfRound (strMsg);
				
				break;
			case MT_WELCOME:
				//gameCanvas.menuState = -1;
				//gameCanvas.buildMenu (gameCanvas.menuState);
				//System.out.println (" MT_WELCOME parsed!");
				
				myPlayerId = readShort (strMsg, 0*4);
				int	iistmp = readShort (strMsg, 1*4);
				initialMoneyValue = readShort (strMsg, 2*4); 
				switch(initialMoneyValue){
					case 0 : initialMoneyValue=100; break;
					case 1 : initialMoneyValue=1000; break;
					case 2 : initialMoneyValue=10000; break;
				}
				mapCurX = iistmp % map.hexaWidth;
				mapCurY = iistmp / map.hexaWidth;
				
				//System.out.println (" myPlayerId = "+myPlayerId+" idReg:"+iistmp+" ("+mapCurX+","+mapCurY+")");
				
//				sendRequestRefreshMap ();
				//gameCanvas.repaint ();
				//gameCanvas.serviceRepaints ();
				isGameRunning = true;
				break;
				
			case MT_SEND_HEXAMAP:

				int	hexaMapWidth 	= readByte (strMsg, 0);
				int	hexaMapHeight	= readByte (strMsg, 2);
				
				//System.out.println (" MT_SEND_HEXAMAP received!! :P W:"+hexaMapWidth+" H:"+hexaMapHeight);
				
				map.hexaWidth	= hexaMapWidth;
				map.hexaHeight	= hexaMapHeight;
				
				map.hexaMap = new byte [hexaMapWidth][hexaMapHeight];
				
				for (int xt = 0; xt < hexaMapWidth; xt ++)
					for (int yt = 0; yt < hexaMapHeight; yt ++)
					{
						byte bhm = readByte (strMsg, 4+(xt+yt*hexaMapWidth)*2);
						
						map.hexaMap [xt][yt] = bhm;
					}
					
				map.init (map.hexaWidth, map.hexaHeight);
								
				break;
			case MT_END_BATTLE_NOTIFY:
				//System.out.println (" MT_END_BATTLE_NOTIFY Parsed!");				
//				notifyEndBattle ();
		//	public void notifyEndBattle ()
				set_state(OPPONENT_RETREATED);	gameCanvas.playMusic(5,1);
				
				break;
			case MT_PLAYER_INFO:
				{
					byte [] arrayFlag=null;
					int	playerId = readShort (strMsg, 0);
					
					int	playerNameSize = readByte (strMsg, 4);
					
					String playerName = strMsg.substring (4+2, 4+2+playerNameSize);
					
					if (4+2+playerNameSize < strMsg.length ())
					{
						//System.out.println ("  User has flag!");
						
						arrayFlag = new byte [121];
						
						for (int i = 0; i < 121; i++)
						{
							arrayFlag [i] = readByte (strMsg, 4+2+playerNameSize+i*2);	
						}
					}

					//System.out.println (" MT_PLAYER_INFO Parsed!  Player ("+playerId+"): "+playerName);

					notifyPlayerInfo (playerId, playerName, arrayFlag);
				}
				break;
			case MT_END_OF_GAME:
				//System.out.println (" MT_END_OF_GAME Parsed!");
				
//				notifyEndOfGame (readShort (strMsg, 0));
			//	public void notifyEndOfGame (int idPlayerWon)
					idWinner=readShort (strMsg, 0);					
					if(warField!=null) shutDownBattle();		
					set_state(SURVIVOR); gameCanvas.playMusic(5,1);
					if(!gameCanvas.isShown())
						display.setCurrent(gameCanvas);
				
				break;
			case MT_TIME_END_OF_GAME:
				{
					int		idWin		= readShort (strMsg, 0);
					int		scoreWin	= readShort (strMsg, 4);
					
					int		numCurPlayers = readShort (strMsg, 2*4);
					
					int		playersId [] = new int [numCurPlayers];
					int		gloScores [] = new int [numCurPlayers];
					int		regScores [] = new int [numCurPlayers];
					int		monScores [] = new int [numCurPlayers];
					int		armScores [] = new int [numCurPlayers];
					
					int		iMsgPos = 3*4;
					for (int i = 0; i < numCurPlayers; i++)
					{
						playersId [i] = readShort (strMsg, iMsgPos);	iMsgPos += 4;
						gloScores [i] = readShort (strMsg, iMsgPos);	iMsgPos += 4;
						regScores [i] = readShort (strMsg, iMsgPos);	iMsgPos += 4;
						monScores [i] = readShort (strMsg, iMsgPos);	iMsgPos += 4;
						armScores [i] = readShort (strMsg, iMsgPos);	iMsgPos += 4;												
					}
					notifyTimeEndOfGame (idWin, scoreWin, numCurPlayers, playersId, gloScores, regScores, monScores, armScores);
				}
				break;
			case MT_RETREAT_ACK :
			
//				notifyRetreatConfirmation (readByte (strMsg, 0) == 1);
			//	private void notifyRetreatConfirmation (boolean isAck)
					// isAck == true  => es pot retirar 
					
					// isAck == false => no es pot retirar
					//					   si esta en batalla continuar
					//					   si no esta en batalla, significa que no has pogut moure on volies (et contesta al placeTroops)
					
					if(readByte(strMsg, 0) != 1) set_state(CANNOT_RETREAT);
					else{
					
						if(IAttack) set_state(WORLDMAP_NAVIGATE);
						else {
							moveCase=ESCAPEMOVE;
							set_state(WORLDMAP_DESTSELECT);
						}
					}

				break;
/*
			case MT_LOBBY_WAITING_LIST_UPDATE:
			{
				//System.out.println (" MT_LOBBY_WAITING_LIST_UPDATE Parsed!");
				int indxPlayer = readByte (strMsg, 0);
				
				//System.out.println ("  "+indxPlayer+" : "+strMsg.substring (2));
				
				if (indxPlayer==0)
				{
					synchronized (playersWaitingList) {
						playersWaitingList.removeAllElements ();
					}
				}
				
				playersWaitingList.insertElementAt (strMsg.substring (2), indxPlayer);
			}	
				break;
*/
//			case MT_LOBBY_END_LOBBY:
//				//System.out.println (" MT_LOBBY_END_LOBBY Parsed!");
//				break;
			case MT_ALL_PLAYERS_INGAME:
				//System.out.println (" MT_ALL_PLAYERS_INGAME Parsed!");				
				sendPlayerInfo (((GameMidletLogic)this).myName, ((GameMidletLogic)this).selfFlag?((GameMidletLogic)this).myFlag:null);
				break;				

			case MT_GAMELOBBY_WORKING_VERSIONS:
			    {
			     int workState=2;
			     for (int i = 0; i < strMsg.length()/4; i++)
			     {
			      short wv = readShort (strMsg, i*4);
			      //System.out.println("version "+wv+",");
			
			      if (wv == gameCanvas.versionHandsetJar)
			      {
			       workState = 0;  break;
			      }
			
			      if (wv == (gameCanvas.versionHandsetJar|0x4000))
			      {
			      	//System.out.println ("si valgo!!");
			       workState = 1;  break;
			      }
			     }
			     notifyWorksWithCurrentVersion (workState); // 0-ok  1-hay nueva ver 2-falla
			    }
			    break;
								
			case MT_GAMELOBBY_MESSAGE_NOTIFY:
				//System.out.println ("GameLobby_Message Notify: "+strMsg.substring (0));
//				notifyGameLobbyMessage (strMsg.substring (0));
		//	public void notifyGameLobbyMessage (String msg)
				chatTimer=300; chatString=strMsg.substring (0); gameCanvas.playSound(3);
							
				break;
			case MT_GAMELOBBY_PLAYER_INFO_NOTIFY:
				//System.out.println ("GameLobby_PlayerInfo Notify: "+readShort (strMsg, 0)+" name:"+strMsg.substring (4));
				if (state==WAIT_RESPONSE)		set_state(LOBBY_WAIT_GAME_INIT);
				notifyGameLobbyPlayerInfo (readShort (strMsg, 0), strMsg.substring (4));
				break;
			case MT_GAMELOBBY_PLAYER_QUIT_NOTIFY:
				//System.out.println ("GameLobby_PlayerHasQuit Notify: "+readShort (strMsg, 0));
				if (state==WAIT_RESPONSE)		set_state(LOBBY_WAIT_GAME_INIT);
				notifyGameLobbyPlayerHasQuit (readShort (strMsg, 0));
				break;
			case MT_GAMELOBBY_MOVING_TO_GAME_NOTIFY:
				//System.out.println ("GameLobby_MovingToGame Notify");
				//notifyGameLobbyMovingToGame ();
				if(!gameCanvas.isShown())
					display.setCurrent(gameCanvas);
				set_state(GAME_STARTING);
				break;
			case MT_STARTUPLOBBY_CREATE_GAME_ACK:
				//System.out.println ("StartUpLobby_CreateGameAck Notify");
				//notifyStartUpLobbyCreateGameAck ();
				set_state(LOBBY_WAIT_GAME_INIT);		
				break;	
			case MT_STARTUPLOBBY_DELETE_GAME_NOTIFY:
			{
				//System.out.println ("StartUpLobby_DeleteGame Notify");
				long appId = readLong (strMsg, 0);
				//notifyStartUpGameDeleted (appId);
			//	public void notifyStartUpGameDeleted (long appId)
					int i=0;
					
					while(currentGames[i]!=null && currentGames[i].appId!=appId && i<currentGames.length)
						i++;
						
					if(i<currentGames.length && currentGames[i]!=null){
						currentGames[i]=null;
						for(int j=i; j<currentGamesNumber-1; j++)
							currentGames[j]=currentGames[j+1];
						System.gc();
						currentGamesNumber--;
					}		
			}
				break;
			case MT_STARTUPLOBBY_GAME_INFO_NOTIFY:
				{
				int numPlayers		= readShort (strMsg, 0);
				int initialMoney	= readShort (strMsg, 4);
				int initialArmy		= readShort (strMsg, 4*2);
				int playTime		= readShort (strMsg, 4*3);
				long appId			= readLong (strMsg, 4*4);
				
				String	gameName	= strMsg.substring (4*4+16);
				
				//System.out.println ("StartUpLobby_GameInfo Notify:"+gameName+" "+numPlayers);
				notifyStartUpGameInfo (appId, gameName, numPlayers, initialMoney, initialArmy, playTime);
				}
				break;
			case MT_STARTUPLOBBY_LOBBY_FULL_NOTIFY:
				//System.out.println ("StartUpLobby_LobbyFull Notify");

				break;	
									
			default:
				//System.out.println (" Incoming message unknown!");
				break;
		}
		
	} // end parseMessage



	void		parseEndOfRound (String	strMsg)
	{
		Round		r = new Round (myPlayerId);
		
		int	numActions = readByte (strMsg, 0*2);
		
		r.nActions = numActions;
		
		int		iPos = 2;
		
		for (int i = 0; i < numActions; i++)
		{
			BattleAction		b =	new BattleAction(0,0,0,0,0,0,0,0);
			b.timer = readShort (strMsg, iPos);	iPos +=4;
			b.type = readByte (strMsg, iPos);	iPos +=2;
			b.xori = readByte (strMsg, iPos);	iPos +=2;
			b.zori = readByte (strMsg, iPos);	iPos +=2;
			b.xdes = readByte (strMsg, iPos);	iPos +=2;
			b.zdes = readByte (strMsg, iPos);	iPos +=2;
			b.amount = readByte (strMsg, iPos);	iPos +=2;
			b.who = readByte (strMsg, iPos);	iPos +=2;
			
			/*System.out.println ("ActionParsed: t:"+b.type+
				" xo:"+b.xori+" zo:"+b.zori+
				" xd:"+b.xdes+" zd:"+b.zdes+
				" a:"+b.amount+" w:"+b.who+" t:"+b.timer);*/
				
			r.ac [i] = b;
		}
		displayBattle (r);
	}
	
	
	// Outgoing Messages	////////////////////////////////////////////
	/*
		MT_BUY_FOOD
		MT_BUY_TROOPS
		MT_MOVE_TROOPS
		MT_MESSAGE
		MT_QUIT
		MT_ATTACK_FIRST
		MT_ATTACK_ACCEPT
		MT_BATTLE_ACTION
		MT_BATTLE_RESULT	
	*/
	
	void	sendBuyFood (int units)
	{
		//System.out.println ("sendBuyFood");

		StringBuffer	sbMsg = new StringBuffer ();
		
		appendByte (sbMsg, MT_BUY_FOOD);
		appendShort (sbMsg, (short)units);
		
		connector.sendMessage (sbMsg);
		
	}

/*
MT_BUY_TROOPS
	idRegion: int
	unitsCat: int
	unitsArc: int
	unitsCab: int
	unitsInf: int

 Reply imm MT_BUY_TROOPS_ACK

*/	
	void	sendBuyTroops (int idRegion, int unitsCat, int unitsArc, int unitsCab, int unitsInf)
	{
			//System.out.println ("sendBuyTroops : "+unitsCat+","+unitsArc+","+unitsCab+","+unitsInf);
		StringBuffer	sbMsg = new StringBuffer ();
		
		appendByte (sbMsg, MT_BUY_TROOPS);
		appendShort (sbMsg, (short) idRegion);
		appendShort (sbMsg, (short) unitsCat);
		appendShort (sbMsg, (short) unitsArc);
		appendShort (sbMsg, (short) unitsCab);
		appendShort (sbMsg, (short) unitsInf);
		
		connector.sendMessage (sbMsg);		
	}

/*
MT_MOVE_TROOPS
	idRegionSrc: int
	idRegionDst: int
	unitsCat: int
	unitsArc: int
	unitsCab: int
	unitsInf: int

 Reply imm MT_MOVE_TROOPS_ACK.	
*/	
	void	sendMoveTroops (int idRegionSrc, int idRegionDst, int unitsCat, int unitsArc, int unitsCab, int unitsInf)
	{
		isMapUpdated=false;
		//System.out.println ("sendMoveTroops : "+unitsCat+","+unitsArc+","+unitsCab+","+unitsInf);
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_MOVE_TROOPS);
		appendShort (sbMsg, (short) idRegionSrc);
		appendShort (sbMsg, (short) idRegionDst);
		appendShort (sbMsg, (short) unitsCat);
		appendShort (sbMsg, (short) unitsArc);
		appendShort (sbMsg, (short) unitsCab);
		appendShort (sbMsg, (short) unitsInf);

		connector.sendMessage (sbMsg);
		
	}


	void	sendPlaceTroops (int idRegionDst, int unitsCat, int unitsArc, int unitsCab, int unitsInf)
	{
		isMapUpdated=false;
		//System.out.println ("sendMoveTroops : "+unitsCat+","+unitsArc+","+unitsCab+","+unitsInf);
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_PLACE_TROOPS);
		appendShort (sbMsg, (short) 0);
		appendShort (sbMsg, (short) idRegionDst);
		appendShort (sbMsg, (short) unitsCat);
		appendShort (sbMsg, (short) unitsArc);
		appendShort (sbMsg, (short) unitsCab);
		appendShort (sbMsg, (short) unitsInf);

		connector.sendMessage (sbMsg);
		
	}

	
/*
MT_MESSAGE
	idPlayerDst: int 			(-1 to All)
	message: byte[]
	
 Sends MT_MESSAGE_NOTIFY to idPlayerDst.	
*/
	void	sendMessageTo (int idPlayerToSend /* -1 to All */, String strMsgToSend)		// to add parameters
	{
		//System.out.println ("sendMessageTo "+idPlayerToSend+": "+strMsgToSend);		
		StringBuffer 	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_MESSAGE);

		appendShort (sbMsg, (short) idPlayerToSend);
		sbMsg.append (strMsgToSend);

		connector.sendMessage (sbMsg);
	}
	
/*
MT_QUIT
	none
*/
	void	sendQuit ()
	{
		//System.out.println ("sendQuit");
		StringBuffer 	sbMsg = new StringBuffer ();
		
		appendByte (sbMsg, MT_QUIT);
	
		connector.sendMessage (sbMsg);
		
		connector.threadCommListener.bDisconnectWhenPossible=true;
	}

/*
MT_ATTACK_FIRST
 sent from attacker

	idRegionAttacker: int
	idRegionDefender: int
*/	
	void	sendAttackFirst (int idRegionAttacker, int idRegionDefender)
	{
		//System.out.println ("sendAttackFirst");
		StringBuffer	sbMsg = new StringBuffer ();
	
		appendByte (sbMsg, MT_ATTACK_FIRST);
		appendShort (sbMsg, (short) idRegionAttacker);
		appendShort (sbMsg, (short) idRegionDefender);
		
		connector.sendMessage (sbMsg);		
	}

	void	sendRound (Round r)
	{
		//System.out.println ("sendRound");
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_BATTLE_ACTION);
		appendByte (sbMsg, (byte) r.nActions);
		
		for (int i = 0; i < r.nActions; i++)
		{
			BattleAction		b = r.ac [i];
			appendShort (sbMsg, (short) b.timer);
			appendByte (sbMsg, (byte) b.type);
			appendByte (sbMsg, (byte) b.xori);
			appendByte (sbMsg, (byte) b.zori);
			appendByte (sbMsg, (byte) b.xdes);
			appendByte (sbMsg, (byte) b.zdes);
			appendByte (sbMsg, (byte) b.amount);
			appendByte (sbMsg, (byte) b.who);
			
			/*System.out.println ("ActionSent: t:"+b.type+
				" xo:"+b.xori+" zo:"+b.zori+
				" xd:"+b.xdes+" zd:"+b.zdes+
				" a:"+b.amount+" w:"+b.who+" t:"+b.timer);*/

		}
		
		connector.sendMessage (sbMsg);
	}

/*
MT_ATTACK_ACCEPT
 sent from defender

	idRegionAttacker: int
	idRegionDefender: int
	
 Replies MT_ATTACK_ACK to Attacker & Defender.
*/
	void	sendAttackAccept (int idRegionAttacker, int idRegionDefender)
	{
		//System.out.println ("sendAttackAccept");
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_ATTACK_ACCEPT);
		appendShort (sbMsg, (short) idRegionAttacker);
		appendShort (sbMsg, (short) idRegionDefender);

		connector.sendMessage (sbMsg);
	}
	
	void	sendRequestRefreshMap ()
	{
		//System.out.println ("sendRequestRefreshMap");
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_REFRESH_MAP);

		connector.sendMessage (sbMsg);		
	}
/*
MT_BATTLE_ACTION
	msTime: int
	actionType: int
	actionData: byte[]

	actionType=BA_MOVE
		actionData:
			unitType: int
			moveToX: int
			moveToY: int
	
	actionType=BA_ATTACK
		actionData:
			unitType: int
			attackToX: int
			attackToY: int
			
	actionType=BA_END_TURN			// No more action points or pass turn.
		actionData: none
		
	actionType=BA_RETIRE
		actionData: none
*/
	void	sendBattleAction (BattleAction batAction)		// define BattleAction
	{
		//System.out.println ("sendBattleAction");
		/*		
		msgData [0] = MT_BATTLE_ACTION;

		// todo serialize BattleAction
		
		threadCommListener.sendMessage (msgData);
		*/

	}

/*
MT_BATTLE_RESULT
	idPlayerWins: int
	idPlayerLoses: int
	unitsCatWinner: int
	unitsArcWinner: int
	unitsCabWinner: int
	unitsInfWinner: int
	unitsCatLoser: int
	unitsArcLoser: int
	unitsCabLoser: int
	unitsInfLoser: int
*/
	void	sendBattleResults (int idPlayerWins, int idPlayerLoses, 
								int unitsCatWinner, int unitsArcWinner, int unitsCabWinner, int unitsInfWinner,
								int unitsCatLoser,  int unitsArcLoser,  int unitsCabLoser,  int unitsInfLoser)
	{
		//System.out.println ("sendBattleResults");
		StringBuffer	sbMsg = new StringBuffer ();
	
		appendByte (sbMsg, MT_BATTLE_RESULT);
		appendShort (sbMsg, (short) idPlayerWins);
		appendShort (sbMsg, (short) idPlayerLoses);
		appendShort (sbMsg, (short) unitsCatWinner);
		appendShort (sbMsg, (short) unitsArcWinner);
		appendShort (sbMsg, (short) unitsCabWinner);
		appendShort (sbMsg, (short) unitsInfWinner);
		appendShort (sbMsg, (short) unitsCatLoser);
		appendShort (sbMsg, (short) unitsArcLoser);
		appendShort (sbMsg, (short) unitsCabLoser);
  		appendShort (sbMsg, (short) unitsInfLoser);
		
		connector.sendMessage (sbMsg);
	}
	
	void	sendUpdateMyValues ()
	{
		//System.out.println ("sendUpdateMyValues");
		StringBuffer	sbMsg = new StringBuffer ();
		
		appendByte (sbMsg, MT_UPDATE_MY_VALUES);
		appendShort (sbMsg, (short) myMoney);
		appendShort (sbMsg, (short) myFood);
		appendShort (sbMsg, (short) myFuel);

		connector.sendMessage (sbMsg);
	}

	void	sendAttackAccept (boolean acceptsOrNot)
	{
		//System.out.println ("sendAttackAccept: "+(acceptsOrNot?"Accepts":"Retires"));
		StringBuffer	sbMsg = new StringBuffer ();
		
		appendByte (sbMsg, MT_ATTACK_ACCEPT);
		appendByte (sbMsg, (byte) (acceptsOrNot?0:1));

		connector.sendMessage (sbMsg);
	}
	
	
	void	sendPlayerInfo (String playerName, byte [] arrayFlag)
	{
		StringBuffer	sbMsg = new StringBuffer ();
		
		//System.out.println (" sendPlayerInfo! "+playerName);
		
		appendByte (sbMsg, MT_MY_PLAYER_INFO);
		appendByte (sbMsg, (byte) (playerName.length ()));
		sbMsg.append (playerName);
		
		if (arrayFlag!=null)
		{
			for (int i = 0; i < 121; i++)
			{
				appendByte (sbMsg, arrayFlag [i]);
			}
		}
		connector.sendMessage (sbMsg);		
	}
	
	/////////////////////// Statup lobby msgs
	
	void sendStartUpCreateGameLobby (String gameName, int numPlayers, int initialMoney, int initialArmy, int playTime)
	{
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_STARTUPLOBBY_CREATE_GAME);
		appendShort (sbMsg, (short) numPlayers);
		appendShort (sbMsg, (short) initialMoney);
		appendShort (sbMsg, (short) initialArmy);
		appendShort (sbMsg, (short) playTime);
		sbMsg.append (gameName);
		
		connector.sendMessage (sbMsg);		
	}
	
	void sendStartUpJoinGameLobby (long appId)
	{
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_STARTUPLOBBY_JOIN_GAME);
		appendLong (sbMsg, appId);
		
		connector.sendMessage (sbMsg);		
	}

	void sendStartUpQuit ()
	{
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_STARTUPLOBBY_QUIT);
		
		connector.sendMessage (sbMsg);
		
		connector.threadCommListener.bDisconnectWhenPossible=true;
	}
	
	
	////////////////////// com.mygdx.mongojocs.sanfermines2006.Game lobby msgs

	void sendGameLobbyQuit ()
	{
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_GAMELOBBY_QUIT_GAME_LOBBY);
		
		connector.sendMessage (sbMsg);		
	}

	void sendGameLobbyMessage (String strMsg)
	{
		StringBuffer	sbMsg = new StringBuffer ();

		appendByte (sbMsg, MT_GAMELOBBY_SEND_MESSAGE);
		sbMsg.append (strMsg);

		connector.sendMessage (sbMsg);		
	}
	
	void notifyWorksWithCurrentVersion (int workState)
	{
		if(workState==1) set_state(OLD_VERSION);
		if(workState==2) set_state(TOO_OLD_VERSION);
	}

	public static String checkString(String s)
	{
		char c[];
		
		c = s.toUpperCase().toCharArray();
		
		for(int i=0; i<c.length; i++)
			if(c[i]<'!' || c[i]>'Z') c[i]=' ';
		
		return new String(c);
	}
	
}
