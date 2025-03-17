package com.mygdx.mongojocs.bravewar;/*

	BraveWar Canvas for Nokia 6600
	
	Microjocs 2003

*/

/**
 * 
 */


import java.io.InputStream;
import java.io.IOException;
import java.util.*;




import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;



class lobbyGameInfo {
	
		long appId;
		String gameName;
		int numPlayers, initialMoney, initialArmy, playTime;
}

public class GameCanvas extends FullCanvas
{	
	public static short versionHandsetJar = 0x0400; 
    public GameMidletLogic 			gameMidlet; 
    	
    Image WorldMapTiles, FontImg, BigFontImg, GUIImg, IconsImg, BoxImg, HumansImg, OrcsImg, TanksImg, Explos, Misc, UserFlag[], Picture;
    public static int CANVX, CANVY, mapScrollX=0, mapScrollY=0, barsY=-20;        
    public byte[] HumansCor= new byte[1080], OrcsCor= new byte[360], TanksCor= new byte[120], FontCor= new byte[768], ExplCor= new byte[78], GUICor= new byte[90], tileMap;    
    public static final int flagColors[][]={{25,59,63},{255,0,0},{155,0,0},{247,148,29},{255,255,255},{0,185,255},{0,118,163},{204,110,0},{193,193,193},{255,255,0},{171,160,0},{240,110,170},{129,129,129},{0,255,0},{25,123,48},{184,5,148}};
    //public Scroll worldScroll;
    public Graphics g;
    public boolean allowPaint;

	//Sound snd[]; 
	Music snd[];

	GameCanvas (GameMidletLogic mid)
	{
		CANVX=getWidth(); CANVY=getHeight(); //CANVX=128; CANVY=128;
				
		gameMidlet = mid;
   		
   		try{
   			FontImg = new Image();
   			FontImg._createImage("/Font.png");
   			BigFontImg = new Image();
   			BigFontImg._createImage("/BigFnt.png");
   			GUIImg = new Image();
			GUIImg._createImage("/GUI.png");
   			IconsImg = new Image();
   			IconsImg._createImage("/Icons.png");
   			BoxImg = new Image();
   			BoxImg._createImage("/Box.png");
   			
   		}catch(Exception e) {}
   		
		//loadPicture();   		   		   		   		
		Picture = new Image();
   		Picture._createImage("/Picture.png");
   		   		   		   		
		HumansCor = LoadFile(3);
								
		OrcsCor = LoadFile(4);
						
		TanksCor = LoadFile(5);
				
		FontCor = LoadFile(1);
				
		ExplCor = LoadFile(0);
				
		GUICor = LoadFile(2); 		
		HumansImg = new Image();
		HumansImg._createImage("/Humans.png");
		OrcsImg = new Image();
		OrcsImg._createImage("/Orcs.png");
		TanksImg = new Image();
		TanksImg._createImage("/Tanks.png");
		Explos = new Image();
		Explos._createImage("/Explosions.png");
		Misc = new Image();
		Misc._createImage("/Misc.png");
				
		UserFlag=new Image[6];
		for(int i=0; i<6; i++) {
			UserFlag[i] = new Image();
			UserFlag[i]._createImage(11, 11);
		}
			
													   		   		   		
   		Point3D.constantInit(CANVX,CANVY);
   		Point3D.SCALE=12*128/176;
   		   		
   		// Midi
   		
		snd = new Music[7];
   		
   		snd[0] = LoadMidi("intro.mid");		
   		snd[1] = LoadMidi("alert.mid");		
   		snd[2] = LoadMidi("turn.mid");		
   		snd[3] = LoadMidi("chat.mid");		
   		snd[4] = LoadMidi("expl.mid");		
   		snd[5] = LoadMidi("win.mid");		
   		snd[6] = LoadMidi("lose.mid");		
   		   		   		
	}
	
	/*public Music LoadMidi(String Name)
	{
	
		Player p = null;
	
		try
		{
			InputStream is = getClass().getResourceAsStream(Name);
			p = Manager.createPlayer( is , "audio/midi");
			p.realize();
			p.prefetch();
		}
		catch(Exception exception) {exception.printStackTrace();}
	
		return p;
	}*/

	public Music LoadMidi(String Name)
	{
		return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Name));
	}
		
	public void playMusic(int id, int loop)
	{	
		
		if(!gameMidlet.sound) return;				
		try{
		
			for(int i=0; i<7; i++)
				snd[i].stop();
			
			//VolumeControl vc = (VolumeControl) snd[id].getControl("VolumeControl"); if (vc != null) {vc.setLevel(90);}
			snd[id].setLooping(loop==0);
			snd[id].play();
			
		}catch(Exception exception) {exception.printStackTrace();}
		
	}	
	
	public void playSound(int id)
	{		
		playMusic(id,1);				
	} 	

	
	public void showNotify()
	{
		gameMidlet.doUpdate = true;
	}
	
	public void hideNotify()
	{
		gameMidlet.doUpdate = false;
	}		
	
	public void destroyMapGfx()
	{
		/*WorldMapTiles = null;
		System.gc();
		ScrollEND();
		System.gc();
		//worldScroll = null;
		System.gc();		
		IconsImg=null;
		System.gc();	*/
		
	}
	
	public void loadWarGfx()
	{		
		/*HumansImg = LoadImage("/Humans.png");
				
		OrcsImg = LoadImage("/Orcs.png");
		//OrcsImg = Image.createImage(2,2);
				
		TanksImg = LoadImage("/Tanks.png");
		//TanksImg = Image.createImage(2,2);
						
		Explos = LoadImage("/Explosions.png");
		
		Misc = LoadImage("/Misc.png");*/		
						
	}
	
	public void loadPicture()
	{
		//Picture = LoadImage("/Picture.png");	
	}
	
	public void destroyPicture()
	{
		//Picture=null;		
		//System.gc();
	}
	
	public void destroyWarGfx()
	{		
		/*HumansImg=null;
		OrcsImg=null;
		TanksImg=null;
		Explos=null;
		Misc=null;
		System.gc();

		loadMapGfx();*/				
	}
	
	public boolean areMapGfxLoaded()
	{
		return IconsImg!=null;	
	}
	
	public void loadMapGfx()
	{	
					
		WorldMapTiles = LoadImage("/WorldTiles.png");		
				
		ScrollEND();						
		createScroll();		
		
		IconsImg = LoadImage("/Icons.png");
	}
	
	public void createScroll()
	{
		int sx=2*gameMidlet.map.hexaWidth+10, sy=4*gameMidlet.map.hexaHeight+13;
		
		//worldScroll = new Scroll();
		ScrollINI(CANVX,CANVY);						
		ScrollSET(tileMap, sx, sy, WorldMapTiles, 4);				
	}

	public void joiningGame()
	{
	}

	
	public void keyPressed(int keycode)
	{
		controlReset();
				
		switch(keycode)
		{
			case KEY_NUM1 : up=true; left=true; break;
			case KEY_NUM2 : up=true; break;
			case KEY_NUM3 : up=true; right=true; break;
			case KEY_NUM4 : left=true; break;			
			case -7 :// accept=true; break;
			case KEY_NUM5 : but1=true; break;
			case KEY_NUM6 : right=true; break;			
			case KEY_NUM7 : down=true; left=true; break;			
			case KEY_NUM8 : down=true; break; 
			case KEY_NUM9 : down=true; right=true; break;
			case KEY_NUM0 : //but2=true; break;
			case -6 : but2=true; break;
			case KEY_STAR : but3=true; break;
			case KEY_POUND : but4=true; break;
		}
		
		/*switch(getGameAction(keycode))
		{
			case LEFT  : left=true; break;
			case RIGHT : right=true; break;
			case UP    : up=true; break;
			case DOWN  : down=true; break;
			case FIRE  : but1=true; break;
			//case GAME_A: but2=true; break;
			//case GAME_B: but3=true; break;
		}	*/
		
		anybut=but1 || but2 || but3 || but4 || accept;
		any=up || down || left || right || anybut;							
		accept=but1; men=but2;
						
	} 

	public void keyReleased(int keycode)
	{
		controlReset();		
	}

	// Canvas paint handler
    public void paint(Graphics gg)
    {
    	if(!allowPaint) return;
    	allowPaint=false;
    	
    	String txt=null, ops[];
    	int x, y, sx;
    	
		g=gg;
		
		try{
			
			DeviceControl.setLights(0,100);
			
		}catch(java.lang.IllegalArgumentException e) {}
			        
	    try{
	        
		switch(gameMidlet.state){        	
			
			case GameMidletLogic.MAIN_MENU :			
			clearDisplay(0,0,0);
			showSprite(Picture,0,0,Picture.getWidth(),Picture.getHeight(),0,0,false);			
			switch(gameMidlet.menuOp){
				case 0 : txt=Text.connect; break;	
				case 1 : if(gameMidlet.sound) txt=Text.soundOnOff[0]; else txt=Text.soundOnOff[1]; break;	
				case 2 : if(gameMidlet.selfFlag) txt=Text.flagOnOff[1]; else txt=Text.flagOnOff[0]; break;	
				case 3 : txt=Text.flagEdit; break;
				case 4 : txt=Text.nameEdit; break;
				case 5 : txt=Text.credits; break;
				case 6 : txt=Text.exit; break;
			}
			showBigFontTextCenter("< "+txt+" >",CANVY-20,0);			
			break;
			
			case GameMidletLogic.LOGING_IN :
			clearDisplay(0,0,0);
			showBigFontTextCenter(Text.loging,CANVY/2-5,0);			
			break;
			
			case GameMidletLogic.LOGIN_FAILED :
			clearDisplay(0,0,0);
			drawAcceptTextBox(g,Text.loginFailed);
			break;						
			
			case GameMidletLogic.LOBBY_CHOOSE_GAME :
			int begOp=0, numOps;
			numOps=(CANVY-38)/8; if(numOps>gameMidlet.currentGamesNumber+1) numOps=gameMidlet.currentGamesNumber+1;
			if(gameMidlet.opCursor>=numOps) begOp=gameMidlet.opCursor-numOps+1;
			//System.out.println(gameMidlet.opCursor+""+numOps+" "+begOp);
			clearDisplay(0,0,0);
			showBigFontText(Text.bravewarGames,5,8,0);
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(255,255,255);
			g.drawRect(2,25,CANVX-15,CANVY-38);			
			g.setColor(119,146,142);
			g.fillRect(4,28+8*(gameMidlet.opCursor-begOp),CANVX-18,9);
			
			g.setColor(255,255,255);
			g.setClip(0,0,CANVX,CANVY);
			if(begOp>0) showFontText("-",CANVX-10,25,0);
			if(begOp+numOps<gameMidlet.currentGamesNumber+1) showFontText("+",CANVX-10,CANVY-20,0); 			
			
								
			for(int i=0; i<numOps; i++){				
				if(i+begOp==0) showFontText(Text.createGame,6,29,0);						
				else{
					showFontText(gameMidlet.currentGames[i+begOp-1].gameName,6,29+(8*i),0);			
					showFontText("("+gameMidlet.currentGames[i+begOp-1].numPlayers+")",CANVX-30,29+(8*i),0);
				}
			}
						
			showFontText(Text.menBack,5,CANVY-9,0);	
			showFontText(Text.accSelect,CANVX-40,CANVY-9,0);	
			break;		
			
			case GameMidletLogic.LOBBY_CREATE_GAME :							
			
			clearDisplay(0,0,0);
			showBigFontText(Text.newGame,5,8,0);
			
			String createOps[]={Text.name+": "+gameMidlet.gameName,
						  Text.players+": "+gameMidlet.numPlayers,
						  Text.money+": "+Text.moneyCat[gameMidlet.startMoney],
						  Text.troops+": "+Text.troopCat[gameMidlet.startTroops],
						  Text.tim+": "+Text.timeCat[gameMidlet.startTime],
						  Text.goCreate};
						  						  			
			drawGUIBoxMenu(createOps, gameMidlet.opCursor, 20, 30);			

						
			break;
			
			case GameMidletLogic.LOBBY_GAME_DETAILS :
			String detailsOps[]={Text.players+": "+gameMidlet.currentGames[gameMidlet.currentGame].numPlayers,
						  Text.money+": "+Text.moneyCat[gameMidlet.currentGames[gameMidlet.currentGame].initialMoney],
						  Text.troops+": "+Text.troopCat[gameMidlet.currentGames[gameMidlet.currentGame].initialArmy],
						  Text.tim+": "+Text.timeCat[gameMidlet.currentGames[gameMidlet.currentGame].playTime],
						  };
						  			
			clearDisplay(0,0,0);						  						  									  						
			showBigFontText(Text.joinTo+gameMidlet.currentGames[gameMidlet.currentGame].gameName,5,8,0);			
			drawGUIBoxMenu(detailsOps, -1, 20, 30);			
			//showFontText(com.mygdx.mongojocs.bravewar.Text.joinOrBack,5,CANVY-9,0);
			showFontText(Text.menBack,5,CANVY-9,0);	
			showFontText(Text.accJoin,CANVX-40,CANVY-9,0);	
			break;
			
			case GameMidletLogic.LOBBY_WAIT_GAME_INIT :							
			clearDisplay(0,0,0);
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(255,255,255);
			g.drawRect(2,25,CANVX-5,CANVY-50);			
			g.drawLine(0,CANVY-12,CANVX,CANVY-12);			
			showBigFontText(Text.waitGameInit+": "+gameMidlet.waitingPlayersNumber+"/"+gameMidlet.numPlayers,5,8,0);			
			for(int i=0; i<gameMidlet.waitingPlayersNumber; i++)
				showFontText(""+gameMidlet.waitingPlayers[i], 6, 29+8*(i),0);			
			//if(gameMidlet.chatTimer>0) showFontText(gameMidlet.chatString,5,CANVY-18,0);
			//showFontText(com.mygdx.mongojocs.bravewar.Text.press0ToAbort,5,CANVY-20,0);
			showFontText(Text.menBack,5,CANVY-19,0);	
			showFontText(Text.starChat,CANVX-40,CANVY-19,0);				
			break;
			
			case GameMidletLogic.GAME_STARTING :
			clearDisplay(0,0,0);
			showBigFontTextCenter(Text.gameStarting,CANVY/2-5,0);			
			break;			
															
			case GameMidletLogic.WORLDMAP_NAVIGATE :			
			showWorldMap(g);
			showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
			drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
			drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
			drawCommandBars(g,gameMidlet.regionType,true);			
			if(!gameMidlet.isMapUpdated) showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,3);
			mantainScroll(gameMidlet.mapCurX,gameMidlet.mapCurY);
			drawGUIStringBoxRight(Text.empty,CANVY-20-(2*barsY));
			if(barsY>-20) barsY-=4;					
			break;
			
			case GameMidletLogic.WORLDMAP_MYREGION_MENU :			
			showWorldMap(g);
			showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
			drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
			drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
			drawCommandBars(g,gameMidlet.regionType,true);			
			switch(gameMidlet.opCursor)
			{
				case 0 : txt=Text.move; break;	
				case 1 : txt=Text.attack; break;					
				case 2 : txt=Text.buy; break;	
				case 3 : txt=Text.info; break;	
				case 4 : txt=Text.talk; break;	
				case 5 : txt=Text.options; break;	
			}
			drawGUIStringBoxRight(txt,CANVY-20-(2*barsY));
			if(barsY<0) barsY+=4;					
			//mantainScroll();
			break;		
			
			case GameMidletLogic.WORLDMAP_OTHERREGION_MENU :			
			showWorldMap(g);
			showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);			
			drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
			drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
			drawCommandBars(g,gameMidlet.regionType,true);			
			switch(gameMidlet.opCursor)
			{
				case 0 : txt=Text.info; break;	
				case 1 : txt=Text.talk; break;					
				case 2 : txt=Text.options; break;	
				default: txt=Text.empty;
			}
			drawGUIStringBoxRight(txt,CANVY-20-(2*barsY));
			if(barsY<0) barsY+=4;					
			//mantainScroll();
			break;		
			
			case GameMidletLogic.WORLDMAP_EMPTY_MENU :			
			showWorldMap(g);
			showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
			drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
			drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
			drawCommandBars(g,gameMidlet.regionType,true);			
			switch(gameMidlet.opCursor)
			{				
				case 0 : txt=Text.talk; break;					
				case 1 : txt=Text.options; break;	
			}
			drawGUIStringBoxRight(txt,CANVY-20-(2*barsY));
			if(barsY<0) barsY+=4;					
			//mantainScroll();
			break;		
									
			case GameMidletLogic.WORLDMAP_DESTSELECT :			
			if(barsY<0) barsY+=4;					
			showWorldMap(g);
			showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
			drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
			drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
			drawCommandBars(g,gameMidlet.regionType,true);			
			if(gameMidlet.cnt%2==0) showRegionCursor(g,gameMidlet.destCurX,gameMidlet.destCurY,0);
			if(gameMidlet.destCurX>=0 && gameMidlet.destCurX<gameMidlet.map.hexaWidth && gameMidlet.destCurY>=0 && gameMidlet.destCurY<gameMidlet.map.hexaHeight)
			if(gameMidlet.map.hexaMap[gameMidlet.destCurY][gameMidlet.destCurX]>0 &&
			(gameMidlet.map.getRegionByPos(gameMidlet.destCurX,gameMidlet.destCurY).idPlayer<=0 || 
				gameMidlet.map.getRegionByPos(gameMidlet.destCurX,gameMidlet.destCurY).idPlayer==gameMidlet.myPlayerId))
				showRegionCursor(g,gameMidlet.destCurX,gameMidlet.destCurY,1);
			drawGUIStringBoxRight(Text.chooseRegion,CANVY-20-(2*barsY));
			mantainScroll(gameMidlet.destCurX,gameMidlet.destCurY);
			break;		
			
			
			case GameMidletLogic.WORLDMAP_ATTACKSELECT :			
			if(barsY<0) barsY+=4;					
			showWorldMap(g);
			showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
			drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
			drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
			drawCommandBars(g,gameMidlet.regionType,true);			
			if(gameMidlet.cnt%2==0) showRegionCursor(g,gameMidlet.destCurX,gameMidlet.destCurY,0);
			
			if(gameMidlet.destCurX>=0 && gameMidlet.destCurX<gameMidlet.map.hexaWidth && gameMidlet.destCurY>=0 && gameMidlet.destCurY<gameMidlet.map.hexaHeight)
			if(gameMidlet.map.hexaMap[gameMidlet.destCurY][gameMidlet.destCurX]>0 &&
			gameMidlet.map.getRegionByPos(gameMidlet.destCurX,gameMidlet.destCurY).idPlayer>0 && 
				gameMidlet.map.getRegionByPos(gameMidlet.destCurX,gameMidlet.destCurY).idPlayer!=gameMidlet.myPlayerId)			
				showRegionCursor(g,gameMidlet.destCurX,gameMidlet.destCurY,2);							
			drawGUIStringBoxRight(Text.chooseRegion,CANVY-20-(2*barsY));
			mantainScroll(gameMidlet.destCurX,gameMidlet.destCurY);
			break;		
			
			case GameMidletLogic.WHAT_BUY_SELECT :
			if(gameMidlet.cnt==0){
				showWorldMap(g);
				drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
				drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
				drawCommandBars(g,gameMidlet.regionType,true);
				showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
				drawGUIStringBoxRight(Text.whatToBuy,CANVY-20-(2*barsY));
			}
			drawGUIBoxMenu(Text.buyMenu,gameMidlet.menuOp,30,40);
			break;
			
			case GameMidletLogic.TROOPS_BUY_SELECT :
			if(gameMidlet.cnt==0){
				showWorldMap(g);
				drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
				drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
				drawCommandBars(g,gameMidlet.regionType,true);
				showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);				
				drawGUIBoxMenu(Text.buyMenu,gameMidlet.menuOp,10,30);
			}
			drawGUIStringBoxRight(Text.troopType[gameMidlet.troopType],CANVY-20-(2*barsY));
			drawSelectTroopTypeBar(g,true);
			break;						

			case GameMidletLogic.FUEL_BUY_DETAILS :			
			case GameMidletLogic.FOOD_BUY_DETAILS :
			case GameMidletLogic.TROOPS_BUY_DETAILS :
			if(gameMidlet.cnt==0){
				showWorldMap(g);
				showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
				drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
				drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
				drawCommandBars(g,gameMidlet.regionType,true);				
				drawGUIStringBoxRight(Text.howManyUnits,CANVY-20-(2*barsY));
				drawGUIBoxMenu(Text.buyMenu,gameMidlet.menuOp,10,30);				
			}
			if(gameMidlet.state==GameMidletLogic.TROOPS_BUY_DETAILS){
				drawSelectTroopTypeBar(g,false);
				drawBuyDetails(g,gameMidlet.troopType);
			}
			if(gameMidlet.state==GameMidletLogic.FOOD_BUY_DETAILS)
				drawBuyDetails(g,4);
			if(gameMidlet.state==GameMidletLogic.FUEL_BUY_DETAILS)
				drawBuyDetails(g,5);				
			break;									
						
			case GameMidletLogic.MOVE_TROOPS_DETAILS :
			if(gameMidlet.cnt==0){
				showWorldMap(g);
				drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
				drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);
				drawCommandBars(g,gameMidlet.regionType,true);
				showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
				showRegionCursor(g,gameMidlet.destCurX,gameMidlet.destCurY,0);
				showRegionCursor(g,gameMidlet.destCurX,gameMidlet.destCurY,1);
				drawGUIStringBoxRight(Text.howManyTroops,CANVY-20-(2*barsY));			
				drawMoveDetails(g,true);
			}else			
			drawMoveDetails(g,false);
			break;
			
			case GameMidletLogic.MOVE_TROOPS_TYPE_DETAILS :
			drawTypeMoveDetails(g);
			break;
			
			case GameMidletLogic.REGION_INFO :			
			showWorldMap(g);
			showRegionCursor(g,gameMidlet.mapCurX,gameMidlet.mapCurY,0);
			drawPlayerLogo(g,gameMidlet.myPlayerId,0,0);
			drawNameAndMoney(g,gameMidlet.myPlayerId,0,0);			
			drawCommandBars(g,gameMidlet.regionType,true);			
			showRegionInfo(g);
			//drawGUIStringBoxRight(txt,CANVY-20-(2*barsY));
			if(barsY<0) barsY+=4;					
			//mantainScroll();
			break;		
			
			case GameMidletLogic.CHAT_SELECT :
			x=(CANVX-96)/2; y=(CANVY-65)/2;
			drawGUIBox(x,y,96,65);
			showFontText(Text.sendToWho,x+5,y+5,0);
			showFontText(Text.everyone,x+10,y+15,0);
			for(int i=0; i<gameMidlet.numPlayers-1; i++)
				showFontText(gameMidlet.strPlayerNames[gameMidlet.recIndex[i]-1],x+10,y+22+(7*i),0);						
			PutSprite(GUIImg,x+45,y+17+(7*gameMidlet.chatRec),18,6,GUICor,false);
			
			if(gameMidlet.chatRec!=0) drawPlayerLogo(g,gameMidlet.idPlayers[gameMidlet.recIndex[gameMidlet.chatRec-1]-1],x+65,y+18);
			break;
			
			case GameMidletLogic.BATTLE_MOVE_CAM_FOLLOW :		
			case GameMidletLogic.BATTLE_TROOPMOVE :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,false);					
			//drawCommandBars(g,gameMidlet.BATTLETILE,true);
			drawGUIStringBoxRight(Text.movement/*+com.mygdx.mongojocs.bravewar.Text.troopType[0]*/,CANVY-20);
			//if(barsY>-20) barsY-=2;					
			break;
			
			case GameMidletLogic.BATTLE :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,true);		
			showBattleTimer();
			drawCommandBars(g,gameMidlet.BATTLETILE,true);
			drawGUIStringBoxRight(Text.empty,CANVY-20-(2*barsY));
			if(barsY>-20) barsY-=4;					
			break;
			
			case GameMidletLogic.BATTLE_ACTION_SELECT :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,true);
			showBattleTimer();
			drawCommandBars(g,gameMidlet.BATTLETILE,true);					
			switch(gameMidlet.opCursor){
				case 0 : txt=Text.move; break;
				case 1 : txt=Text.attack; break;
				case 2 : txt=Text.roundConfirm; break;
				case 3 : txt=Text.retreat; break;
				default: txt=Text.empty; break;
			}
			drawGUIStringBoxRight(txt,CANVY-20-(2*barsY));
			if(barsY<0) barsY+=4;					
			break;
			
			case GameMidletLogic.BATTLE_ACTION_SELECT_SIMPLE :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,true);
			showBattleTimer();
			drawCommandBars(g,gameMidlet.BATTLETILESIMPLE,true);			
			switch(gameMidlet.opCursor){
				case 0 : txt=Text.roundConfirm; break;
				case 1 : txt=Text.retreat; break;
				default: txt=Text.empty; break;
			}			
			drawGUIStringBoxRight(txt,CANVY-20-(2*barsY));		
			if(barsY<0) barsY+=4;					
			break;
						
			case GameMidletLogic.BATTLE_DESTSELECT :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,1,true);		
			showBattleTimer();
			drawCommandBars(g,gameMidlet.BATTLETILE,true);
			drawGUIStringBoxRight(Text.tileSelect,CANVY-20-(2*barsY));		
			if(barsY>-20) barsY-=4;					
			break;
			
			case GameMidletLogic.BATTLE_ATTACKSELECT_SHORT :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,2,true);		
			showBattleTimer();
			drawCommandBars(g,gameMidlet.BATTLETILE,true);
			drawGUIStringBoxRight(Text.oppSelect,CANVY-20-(2*barsY));		
			if(barsY>-20) barsY-=4;					
			break;
						
			case GameMidletLogic.BATTLE_MOVE_DETAILS :
			/*clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,1);
			showBattleTimer();
			drawCommandBars(g,gameMidlet.BATTLETILE,true);*/
			drawBattleMoveDetails(g,0);
			break;					
			
			case GameMidletLogic.BATTLE_ATTACKSELECT_LONG :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,2,true);	
			showBattleTimer();
			showAttackParabole();	
			drawCommandBars(g,gameMidlet.BATTLETILE,true);
			drawGUIStringBoxRight(Text.oppSelect,CANVY-20-(2*barsY));		
			if(barsY>-20) barsY-=4;					
			break;			
	
			case GameMidletLogic.BATTLE_ATTACK_CAM_FOLLOW :		
			case GameMidletLogic.BATTLE_INFANTRY_ATTACK :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,false);				
			drawGUIStringBoxRight(Text.attackfrom/*+com.mygdx.mongojocs.bravewar.Text.troopType[gameMidlet.troopType]*/,CANVY-20);
			break;						
			
			case GameMidletLogic.BATTLE_ARCHER_ATTACK :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,false);				
			if(gameMidlet.cnt>=20 && gameMidlet.cnt<27)
				showExplosion(gameMidlet.warField,gameMidlet.destCurX, gameMidlet.destCurY, 0, (gameMidlet.cnt-20)/2);
			drawGUIStringBoxRight(Text.attackfrom/*+com.mygdx.mongojocs.bravewar.Text.troopType[gameMidlet.troopType]*/,CANVY-20);
			break;									
						
			case GameMidletLogic.BATTLE_CAVALRY_ATTACK :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,false);		
			if(gameMidlet.cnt>=20 && gameMidlet.cnt<27)		
				showExplosion(gameMidlet.warField,gameMidlet.destCurX, gameMidlet.destCurY, 1, (gameMidlet.cnt-20)/2);
			drawGUIStringBoxRight(Text.attackfrom/*+com.mygdx.mongojocs.bravewar.Text.troopType[gameMidlet.troopType]*/,CANVY-20);
			break;									
						
			case GameMidletLogic.BATTLE_TANK_ATTACK :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,false);		
			if(gameMidlet.cnt>=20 && gameMidlet.cnt<27)		
				showExplosion(gameMidlet.warField,gameMidlet.destCurX, gameMidlet.destCurY, 2, (gameMidlet.cnt-20)/2);
			drawGUIStringBoxRight(Text.attackfrom/*+com.mygdx.mongojocs.bravewar.Text.troopType[gameMidlet.troopType]*/,CANVY-20);
			break;									
			
			case GameMidletLogic.NOT_ENOUGH_POINTS :
			//clearDisplay(0,0,0);			
			//showWarField(gameMidlet.warField,0);							
			drawGUIBoxMenu(Text.notEnoughPoints,0,20,0);					
			break;
			
			case GameMidletLogic.BATTLE_WAIT_RESPONSE :
			clearDisplay(0,0,0);			
			showWarField(gameMidlet.warField,0,false);					
			//drawGUIStringBoxRight(com.mygdx.mongojocs.bravewar.Text.waitOpponent,CANVY-20);
			x=(CANVX-24)/2; y=(CANVY-32)/2;
			//drawGUIBox(x,y,24,32);			
			//showSprite(GUIImg,239,2,8,14,x+8,y+9,false);	
			//PutSprite(GUIImg,x+8,y+9,18,14,GUICor,false);				
			drawGUIStringBoxRight(Text.oppWait,CANVY-20);					
			break;			
			
			case GameMidletLogic.BATTLE_WON :
			drawAcceptTextBox(g,Text.battleWon);
			break;
			
			case GameMidletLogic.BATTLE_LOST :
			drawAcceptTextBox(g,Text.battleLost);
			break;
			
			case GameMidletLogic.BATTLE_REFUSED :
			drawAcceptTextBox(g,Text.reason[gameMidlet.refusingReason]);
			break;			
			
			case GameMidletLogic.NOT_ENOUGH_RESOURCES :
			drawAcceptTextBox2(g,Text.need,Text.buyMenu[1]+" "+gameMidlet.foodNeeded(),Text.buyMenu[2]+" "+gameMidlet.fuelNeeded());
			break;						
			
			case GameMidletLogic.CONFIRM_RETREAT :
			drawQuestionTextBox(g,Text.confirmRetreat);
			break;
			
			case GameMidletLogic.OPPONENT_RETREATED :
			drawAcceptTextBox(g,Text.opponentRetreated);
			break;						
			
			case GameMidletLogic.BATTLE_REQUEST :		
			drawBattleRequestBox(g);	
			break;

			case GameMidletLogic.CLOSE_CONNECTION_WAIT :								
			case GameMidletLogic.WAIT_RESPONSE :		
			drawGUIBigStringBox(Text.wait,(CANVX-90)/2,(CANVY-40)/2);	
			break;			
			
			case GameMidletLogic.FLAG_EDIT :
			clearDisplay(0,0,0);
			drawFlagEditor(g);
			break;
			
			case GameMidletLogic.CLEAR_FLAG_CONFIRM :
			drawQuestionTextBox(g,Text.clearFlagConfirm);
			break;			
			
			case GameMidletLogic.CREDITS :
			case GameMidletLogic.CREDITS2 :
			clearDisplay(0,0,0);
			x=(CANVX-96)/2; y=15;
			showBigFontTextCenter(Text.bravewar+GameMidletLogic.versionStr,y+10,0);
			showBigFontTextCenter(Text.programming,y+35,0);
			showBigFontTextCenter(Text.david,y+55,0);
			showBigFontTextCenter(Text.pau,y+70,0);
			showBigFontTextCenter(Text.graphi,y+95,0);
			showBigFontTextCenter(Text.jordi,y+115,0);
			showBigFontTextCenter(Text.music,y+140,0);
			showBigFontTextCenter(Text.jordi2,y+160,0);			
			showBigFontTextCenter(Text.microjocs,y+185,0);									
			break;
			
			case GameMidletLogic.GAME_ENDED :
			clearDisplay(0,0,0);
			x=(CANVX-96)/2; y=(CANVY-65)/2;
			y-=20;
			drawPlayerLogo(g,gameMidlet.resIdWinner,x+37,y+5);
			showBigFontTextCenter(Text.timeOver,y+40,0);
			showBigFontTextCenter(gameMidlet.strPlayerNames[gameMidlet.resIdWinner-1]+" "+Text.winsTheGame,y+60,0);			
			break;
			
			case GameMidletLogic.SURVIVOR :
			clearDisplay(0,0,0);
			x=(CANVX-96)/2; y=(CANVY-65)/2;
			y-=20;
			drawPlayerLogo(g,gameMidlet.idWinner,x+37,y+5);
			showBigFontTextCenter(Text.campaignOver,y+40,0);
			showBigFontTextCenter(gameMidlet.strPlayerNames[gameMidlet.idWinner-1]+" "+Text.survivor,y+60,0);			
			break;
			
			
			case GameMidletLogic.GAME_END_RESULTS :
			clearDisplay(0,0,0);			
			x=(CANVX-96)/2; y=30-gameMidlet.mapScrollY;
			for(int i=0; i<gameMidlet.numPlayers; i++)
			{
				sx=0;
				drawPlayerLogo(g,gameMidlet.resPlIds[i],x,y+(30*i));				
				showFontText(gameMidlet.strPlayerNames[gameMidlet.resPlIds[i]-1],x+25,y+5+30*i,0);
				g.setClip(0,0,CANVX,CANVY);
				g.setColor(56,112,120);
				g.fillRect(x+25+sx,y+15+30*i,(gameMidlet.resRegScores[i]*60)/gameMidlet.resScoreWinner,5);
				sx+=(gameMidlet.resRegScores[i]*60)/gameMidlet.resScoreWinner;
				g.setColor(248,240,104);
				g.fillRect(x+25+sx,y+15+30*i,(gameMidlet.resMonScores[i]*60)/gameMidlet.resScoreWinner,5);
				sx+=(gameMidlet.resMonScores[i]*60)/gameMidlet.resScoreWinner;
				g.setColor(208,0,8);
				g.fillRect(x+25+sx,y+15+30*i,(gameMidlet.resArmScores[i]*60)/gameMidlet.resScoreWinner,5);
				sx+=(gameMidlet.resArmScores[i]*60)/gameMidlet.resScoreWinner;
			}			
			g.setColor(0,0,0);	
			g.fillRect(0,0,CANVX,25);
			g.fillRect(0,CANVY-25,CANVX,25);
			showBigFontText(Text.scores,8,5,0);			
			showFontText(Text.pushAnyBut,8,CANVY-9,0);			
			break;
						
			case GameMidletLogic.OPTIONS :
			String ingameOps[]={"",Text.credits,Text.abort};
			if(gameMidlet.sound) ingameOps[0]=Text.soundOnOff[1];
			else ingameOps[0]=Text.soundOnOff[0];
			drawGUIBoxMenu(ingameOps, gameMidlet.subMenuOp, 30, 40);			
			break;
			
			case GameMidletLogic.CONFIRM_EXIT :
			drawQuestionTextBox(g,Text.confirmAbort);
			break;
			
			case GameMidletLogic.CANNOT_RETREAT :			
			drawAcceptTextBox(g,Text.cannotRetreat);
			break;								    				
				
			case GameMidletLogic.OLD_VERSION :
			clearDisplay(0,0,0);
			drawAcceptTextBox(g,Text.oldVersion);
			break;
			
			case GameMidletLogic.TOO_OLD_VERSION :
			clearDisplay(0,0,0);
			drawAcceptTextBox(g,Text.tooOldVersion);
			break;
			
			case GameMidletLogic.CONFIRM_RESET :
			drawQuestionTextBox(g,Text.confirmReset);
			break;					
		}
		
		if(gameMidlet.chatTimer>0) showFontText(gameMidlet.chatString,5,CANVY-28-barsY,0);
		
		//System.gc();
		//showFontTextCenter(""+gameMidlet.freeMemory(),0,0);			
		
			}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}
		/*g.setClip(0,0,CANVX,CANVY);
		g.setColor(255,255,255);
		g.drawString(Long.toString(gameMidlet.freeMemory()),20,0,20);*/
		        	                	
	}
	
	void drawFlagEditor(Graphics g)
	{
		int size=(CANVX/13)-2;
		int x=0, y;	
		
		if(CANVY>CANVX) y=(CANVY-CANVX)/2;
		else y=0;
		
		
		g.setClip(0,0,CANVX,CANVY);
		g.setColor(25,59,63);
		g.fillRect(x+0,y+0,11*size,11*size);
		g.setColor(0,0,0);
		for(int i=0; i<11; i++){			
			g.drawLine(x,y+size+(size*i),x+(size*11),y+size+(size*i));
			g.drawLine(x+size+(size*i),y,x+size+(size*i),y+(size*11));
		}
		for(int i=0; i<121; i++)
			if(gameMidlet.myFlag[i]!=0){
				g.setColor(flagColors[gameMidlet.myFlag[i]][0],flagColors[gameMidlet.myFlag[i]][1],flagColors[gameMidlet.myFlag[i]][2]);						
				g.fillRect(x+1+size*(i%11),y+1+size*(i/11),size-1,size-1);
			}	
		
		g.setColor(255,255,255);
		g.drawRect(x+size*gameMidlet.mapCurX,y+size*gameMidlet.mapCurY,size,size);
		
		for(int i=0; i<16; i++){
			g.setColor(flagColors[i][0],flagColors[i][1],flagColors[i][2]);
			g.fillRect(CANVX-3-(size*4)+size*(i%4),y+5+(i/4)*size,size,size);						
		}
		g.setColor(255,255,255);
		g.drawRect(CANVX-3-(size*4)+size*(gameMidlet.currentColor%4),y+5+(gameMidlet.currentColor/4)*size,size,size);						
				
		g.setClip(CANVX-35,CANVY-130,21,24);
		g.drawImage(IconsImg,CANVX-35,CANVY-130,20);
		g.setClip(CANVX-30,CANVY-124,11,12);
		g.drawImage(UserFlag[0],CANVX-30,CANVY-124,20);
		
		showFontText(Text.flagEditHelp,5,CANVY-9,0);
	}
	
	void createFlag(int id, byte[] data)
	{
		//TODO no funciona en multithread
		Graphics g = UserFlag[id].getGraphics();
		g.setClip(0,0,11,11);
		for(int i=0; i<121; i++){
			g.setColor(flagColors[data[i]][0],flagColors[data[i]][1],flagColors[data[i]][2]);
			g.fillRect(i%11,i/11,1,1);
		}
	}
	
	void drawPlayerLogo(Graphics g, int plId, int x, int y)
	{
		int id1, id2;
		if(plId<=0 || IconsImg==null) return;
		
		showSprite(IconsImg,0,0,21,24,x,y,false);
		
		if(plId>=8){
			// War flag
			id1=plId & 0x07;
			id2=(plId>>3) & 0x07;
			
			switch((gameMidlet.cnt/4) % 4){
				
				// Player 1	
				case 0 :
				if(gameMidlet.playerFlags[id1-1]==null){
					
					showSprite(IconsImg,22+11*(id1-1),0,11,11,x+5,y+6,false);				
				}else{				
					showSprite(UserFlag[id1-1],0,0,11,11,x+5,y+6,false);
				}				
				break;
				// Player 2
				case 2 :							
				if(gameMidlet.playerFlags[id2-1]==null){
					
					showSprite(IconsImg,22+11*(id2-1),0,11,11,x+5,y+6,false);				
				}else{				
					showSprite(UserFlag[id2-1],0,0,11,11,x+5,y+6,false);
				}		
				break;		
				default :
				PutSprite(GUIImg,x+3,y+5,18,7,GUICor,false);	
				break;
			}
						
		}else{		
			// Normal Flag
			if(gameMidlet.playerFlags[plId-1]==null){
				
				showSprite(IconsImg,22+11*(plId-1),0,11,11,x+5,y+6,false);				
			}else{				
				showSprite(UserFlag[plId-1],0,0,11,11,x+5,y+6,false);
			}
		}
	}
	void drawNameAndMoney(Graphics g, int plId, int x, int y)
	{
		showBigFontText(gameMidlet.myName,x,y+25,0);			
		showFontText(gameMidlet.myMoney+"$",x,y+37,0);
	}
	
	void drawCommandBars(Graphics g, int type, boolean cur)
	{			
			int x=0, myregion[]={1,0,2,4,5,3},
				otherregion[]={4,5,3},
				empty[]={5,3},
				battletile[]={1,0,12,13},
				battlesimple[]={12,13},
				arr[]=null;
			
			switch(type){
				case GameMidletLogic.MYREGION : arr=myregion; break;
				case GameMidletLogic.OTHERREGION : arr=otherregion; break;
				case GameMidletLogic.EMPTY : arr=empty; break;
				case GameMidletLogic.BATTLETILE : arr=battletile; break;
				case GameMidletLogic.BATTLETILESIMPLE : arr=battlesimple; break;
			}				
			
			x=CANVX-(arr.length*18);
			while(x+9+(18*gameMidlet.opCursor)<0) x+=18;
			
			for(int i=0; i<arr.length; i++){
				
				PutSprite(GUIImg,x+18*i,barsY,18,(i%2==0 ? 10 : 11),GUICor,false);				
				PutSprite(GUIImg,x+18*i,barsY,18,arr[i],GUICor,false);				
			}
				
			PutSprite(GUIImg,x+9+(18*gameMidlet.opCursor),barsY+9,18,6,GUICor,false);
			
	}
	
	void drawSelectTroopTypeBar(Graphics g, boolean cur)
	{
			int x=(CANVX-96)/2, y=(CANVY-32)/2;
			drawGUIBox(x,y,96,32);
			for(int i=0; i<4; i++)
				showSprite(IconsImg,18*i,24,18,18,x+5+(20*i),y+7,false);
			if(cur)
				//showSprite(GUIImg,108,0,10,10,x+15+20*gameMidlet.troopType,y+17,false);		
				PutSprite(GUIImg,x+15+20*gameMidlet.troopType,y+17,18,6,GUICor,false);									
	}
	
	void drawBuyDetails(Graphics g, int it)
	{
			int x=(CANVX-96)/2, y=(CANVY-65)/2;
			drawGUIBox(x,y,96,65);					
			showSprite(IconsImg,18*it,24,18,18,x+5,y+7,false);
			showFontText(gameMidlet.price+"$",x+5,y+27,1);
			showFontText(Text.total+":"+(gameMidlet.alreadyHave+gameMidlet.buyAmount),x+41,y+6,1);
			if(gameMidlet.leftArrowFlash==0) showSprite(IconsImg,69,15,8,7,x+29,y+18,false);
			showSprite(IconsImg,22,13,45,9,x+39,y+17,false);
			if(gameMidlet.rightArrowFlash==0) showSprite(IconsImg,80,15,8,7,x+85,y+18,false);
			showFontText(""+gameMidlet.buyAmount,x+40,y+18,0);
			showSprite(IconsImg,22,13,45,9,x+39,y+32,false);
			showSprite(IconsImg,89,1,16,17,x+26,y+27,false);
			showFontText(""+(gameMidlet.buyAmount*gameMidlet.price),x+40,y+33,0);
						
			PutSprite(GUIImg,x+5,y+44,18,11,GUICor,false);									
			PutSprite(GUIImg,x+5,y+44,18,9,GUICor,false);									
			
			PutSprite(GUIImg,x+71,y+44,18,10,GUICor,false);									
			PutSprite(GUIImg,x+71,y+44,18,12,GUICor,false);									
			
						
	}
	void drawMoveDetails(Graphics g, boolean all)
	{
			int x=(CANVX-96)/2, y=(CANVY-65)/2, am=0;
			Region reg=gameMidlet.map.getRegionByPos(gameMidlet.mapCurX,gameMidlet.mapCurY);			
			
			drawGUIBox(x,y,96,65);											
			for(int i=0; i<4; i++){					
				
				am=gameMidlet.moveTroopsAmount[i];
				showSprite(IconsImg,18*i,24,18,18,x+5+(45*(i%2)),y+4+(20*(i/2)),false);					
				showFontText(""+am+"/"+gameMidlet.troopsAtRegion[i],x+25+(45*(i%2)),y+12+(20*(i/2)),1);
			}	
							
			PutSprite(GUIImg,x+5,y+44,18,11,GUICor,false);									
			PutSprite(GUIImg,x+5,y+44,18,9,GUICor,false);									
			
			if(gameMidlet.myFood>=gameMidlet.foodNeeded() && gameMidlet.myFuel>=gameMidlet.fuelNeeded()){
				PutSprite(GUIImg,x+71,y+44,18,10,GUICor,false);									
				PutSprite(GUIImg,x+71,y+44,18,12,GUICor,false);									
			}else{
				showFontText(Text.need,x+26,y+42,1);
				showFontText(Text.buyMenu[1]+" "+gameMidlet.foodNeeded(),x+26,y+49,1);
				showFontText(Text.buyMenu[2]+" "+gameMidlet.fuelNeeded(),x+26,y+56,1);
			}

			if(gameMidlet.troopType<4)
				//showSprite(GUIImg,108,0,10,10,x+14+(45*(gameMidlet.troopType%2)),y+14+(20*(gameMidlet.troopType/2)),false);					
				PutSprite(GUIImg,x+14+(45*(gameMidlet.troopType%2)),y+14+(20*(gameMidlet.troopType/2)),18,6,GUICor,false);									
			else if(gameMidlet.troopType==4) PutSprite(GUIImg,x+14,y+53,18,6,GUICor,false);									
			//showSprite(GUIImg,108,0,10,10,x+14,y+53,false);					
			else if(gameMidlet.troopType==5) PutSprite(GUIImg,x+80,y+53,18,6,GUICor,false);									
			//showSprite(GUIImg,108,0,10,10,x+80,y+53,false);					
			
						
	}
	
	void drawTypeMoveDetails(Graphics g)
	{
			int x=(CANVX-96)/2, y=(CANVY-65)/2, it, max;
			
			it=gameMidlet.troopType;
			max=10;
			
			drawGUIBox(x,y,96,65);					
			showSprite(IconsImg,18*it,24,18,18,x+5,y+7,false);
			//showFontText(gameMidlet.price+"$",x+5,y+27,1);
			showFontText(Text.leftShort+":"+(gameMidlet.troopsAtRegion[gameMidlet.troopType]-gameMidlet.moveTroopsAmount[it]),x+31,y+16,1);
			if(gameMidlet.leftArrowFlash==0) showSprite(IconsImg,69,15,8,7,x+24,y+29,false);
			showSprite(IconsImg,22,13,45,9,x+34,y+28,false);
			if(gameMidlet.rightArrowFlash==0) showSprite(IconsImg,80,15,8,7,x+80,y+29,false);
			showFontText(""+gameMidlet.moveTroopsAmount[it],x+35,y+29,0);
			/*showSprite(IconsImg,133,30,45,9,x+39,y+32,false);
			showSprite(IconsImg,116,25,16,17,x+26,y+27,false);
			showFontText(""+(gameMidlet.buyAmount*gameMidlet.price),x+40,y+33,0);*/
			
			PutSprite(GUIImg,x+5,y+44,18,11,GUICor,false);									
			PutSprite(GUIImg,x+5,y+44,18,9,GUICor,false);									
			
			PutSprite(GUIImg,x+71,y+44,18,10,GUICor,false);									
			PutSprite(GUIImg,x+71,y+44,18,12,GUICor,false);							
	}	
	
	void showRegionInfo(Graphics g)
	{						
			int x=(CANVX-96)/2, y=(CANVY-65)/2, am=0, id1, id2;
			
			Region reg=gameMidlet.map.getRegionByPos(gameMidlet.mapCurX,gameMidlet.mapCurY);
			
			drawGUIBox(x,y,96,65);		
			if(reg.idPlayer>=8){		
				
				id1=(int)(reg.idPlayer & 0x07);
				id2=(int)((reg.idPlayer>>3) & 0x07);
				
				showFontText(Text.regionInWar,x+10,y+4,0);			
				drawPlayerLogo(g,id1,x+5,y+20);
				drawPlayerLogo(g,id2,x+70,y+20);
				
				showFontTextCenter(gameMidlet.strPlayerNames[id1-1],y+19,0);
				showFontTextCenter(Text.vs,y+27,0);
				showFontTextCenter(gameMidlet.strPlayerNames[id2-1],y+35,0);
				
			}else if(reg.idPlayer>0){
				//drawPlayerLogo(g,reg.idPlayer,x+37,y+20);
				//showSprite(GUIImg,22*reg.idPlayer,0,21,24,x+5,y+5,false);		
				showFontText(gameMidlet.strPlayerNames[reg.idPlayer-1],x+10,y+4,0);
				for(int i=0; i<4; i++){
					switch(i){
						case 0 : am=reg.numUnitsInfantry; break;
						case 1 : am=reg.numUnitsArchers; break;				
						case 2 : am=reg.numUnitsCavalry; break;	
						case 3 : am=reg.numUnitsCatapults; break;	
					
					}
					showSprite(IconsImg,18*i,24,18,18,x+5+(45*(i/2)),y+13+(20*(i%2)),false);					
					showFontText(""+am,x+25+(45*(i/2)),y+21+(20*(i%2)),1);
				}
			}else{
				showFontText(Text.emptyRegion,x+10,y+4,0);
			}
			
			showFontText(Text.regionType[gameMidlet.map.hexaMap[gameMidlet.mapCurY][gameMidlet.mapCurX]-1],x+10,y+53,1);
								
			//showSprite(GUIImg,162,25,18,18,x+76,y+90,false);
			//showSprite(GUIImg,90,44,18,18,x+76,y+90,false);												
	}
	
	void drawBattleMoveDetails(Graphics g, int it)
	{
			int x=(CANVX-96)/2, y=(CANVY-64)/2;
			drawGUIBox(x,y,96,64);					
			//showSprite(IconsImg,18*gameMidlet.warField.tiles[gameMidlet.batCurX][gameMidlet.batCurZ].UnitType,24,18,18,x+5,y+7,false);
			
			showFontText(Text.left+":"+(gameMidlet.warField.tiles[gameMidlet.batCurX][gameMidlet.batCurZ].UnitAmount-gameMidlet.moveTroopsAmount[0]),x+10,y+10,1);
			//if(gameMidlet.leftArrowFlash==0) showSprite(IconsImg,108,26,8,7,x+30,y+25,false);
			//showSprite(IconsImg,133,30,45,9,x+40,y+24,false);
			g.setColor(0,0,0);
			g.setClip(0,0,CANVX,CANVY);
			g.fillRect(x+40,y+24,45,9);
			if(gameMidlet.leftArrowFlash==0) showFontText("<",x+30,y+25,1);
			if(gameMidlet.rightArrowFlash==0) showFontText(">",x+87,y+25,1);
			//if(gameMidlet.rightArrowFlash==0) showSprite(IconsImg,108,33,8,7,x+86,y+25,false);
			showFontText(""+gameMidlet.moveTroopsAmount[0],x+41,y+25,0);
			
			/*if(gameMidlet.leftArrowFlash==0) showSprite(IconsImg,108,26,8,7,x+29,y+18,false);
			showSprite(IconsImg,133,30,45,9,x+39,y+17,false);
			if(gameMidlet.rightArrowFlash==0) showSprite(IconsImg,108,33,8,7,x+85,y+18,false);*/
			
							
			/*showSprite(GUIImg,180,0,18,18,x+5,y+40,false);
			showSprite(GUIImg,144,0,18,18,x+5,y+40,false);
			
			showSprite(GUIImg,162,0,18,18,x+69,y+40,false);
			showSprite(GUIImg,198,0,18,18,x+69,y+40,false);*/		
			
			PutSprite(GUIImg,x+5,y+40,18,11,GUICor,false);									
			PutSprite(GUIImg,x+5,y+40,18,9,GUICor,false);									
			
			PutSprite(GUIImg,x+69,y+40,18,10,GUICor,false);									
			PutSprite(GUIImg,x+69,y+40,18,12,GUICor,false);									
	}
	
	void drawAcceptTextBox(Graphics g, String txt)
	{
		int x=(CANVX-96)/2, y=(CANVY-48)/2;
		drawGUIBox(x,y,96,48);					
		showFontTextCenter(txt,y+10,0);
		PutSprite(GUIImg,x+69,y+26,18,10,GUICor,false);									
		PutSprite(GUIImg,x+69,y+26,18,12,GUICor,false);									

	}
	
	void drawAcceptTextBox(Graphics g, String txt[])
	{
		int sy=32+8*txt.length;
		int x=(CANVX-96)/2, y=(CANVY-sy)/2;
		drawGUIBox(x,y,96,sy);					
		for(int i=0; i<txt.length; i++)			
			showFontText(txt[i],x+10,y+10+8*i,0);
		PutSprite(GUIImg,x+69,y+sy-22,18,10,GUICor,false);									
		PutSprite(GUIImg,x+69,y+sy-22,18,12,GUICor,false);									

	}
	 		
	void drawAcceptTextBox2(Graphics g, String t1, String t2, String t3) 
	{
		int x=(CANVX-96)/2, y=(CANVY-64)/2;
		drawGUIBox(x,y,96,64);					
		showFontTextCenter(t1,y+10,0);
		showFontTextCenter(t2,y+20,0);
		showFontTextCenter(t3,y+30,0);
		PutSprite(GUIImg,x+69,y+42,18,10,GUICor,false);									
		PutSprite(GUIImg,x+69,y+42,18,12,GUICor,false);									
				
	}
	
	void drawQuestionTextBox(Graphics g, String txt)
	{
		int x=(CANVX-112)/2, y=(CANVY-48)/2;
		drawGUIBox(x,y,112,48);					
		showFontTextCenter(txt,y+10,0);
				
		PutSprite(GUIImg,x+5,y+26,18,11,GUICor,false);									
		PutSprite(GUIImg,x+5,y+26,18,9,GUICor,false);									
			
		PutSprite(GUIImg,x+89,y+26,18,10,GUICor,false);									
		PutSprite(GUIImg,x+89,y+26,18,12,GUICor,false);											
	}

	void drawBattleRequestBox(Graphics g)
	{
		int x=(CANVX-112)/2, y=(CANVY-64)/2;
		drawGUIBox(x,y,112,64);	
		drawPlayerLogo(g,gameMidlet.batOpponentId,x+1,y+1);				
		showFontTextCenter(Text.underAttack,y+20,0);
		showFontTextCenter(Text.fightOrRetreat,y+30,0);
				
		PutSprite(GUIImg,x+5,y+40,18,11,GUICor,false);									
		PutSprite(GUIImg,x+5,y+40,18,9,GUICor,false);									
			
		PutSprite(GUIImg,x+89,y+40,18,10,GUICor,false);									
		PutSprite(GUIImg,x+89,y+40,18,12,GUICor,false);											

	}
			
		
	void showWorldMap(Graphics g2)
	{						
		
		int x, y, pid;
		Map wm=gameMidlet.map;
		
		//ScrollRUN(mapScrollX,mapScrollY);
		//ScrollIMP(g2);
		
		clearDisplay(0,0,0);
		
		for(int i=gameMidlet.mapCurX-5; i<gameMidlet.mapCurX+5; i++)
		for(int j=gameMidlet.mapCurY-5; j<gameMidlet.mapCurY+5; j++){
		
			y=36*j;
			if(j%2==0){			
				x=40*i; 
			}else{
				x=20+(40*i);
			}
			
			if(i>=0 && j>=0 && i<wm.hexaWidth && j<wm.hexaHeight){
				if(wm.checkHexaMap(i,j)>0)
					showSprite(WorldMapTiles,41*(wm.checkHexaMap(i,j)-1),0,40,45,x-mapScrollX,y-mapScrollY,false);
				pid=wm.getRegionByPos(i,j).idPlayer;
				if(pid>0)					
					drawPlayerLogo(g2,pid,x-mapScrollX+10,y-mapScrollY+10);
			}			
		}

														
	}	
	void putTile(byte tm[],int x, int y, int v, int sx)
	{
		if(x>=0 && y>=0) tm[y*sx+x]=(byte)v;	
	}
	
	void createWorldTileMap(Map wm)
	{
		int x, y, sx, sy;
		int pattern[][]={{3,4,15,16,26,27},{5,6,17,18,28,29},{7,4,15,16,26,30},{8,9,15,21,31,32},{10,11,22,23,33,34},{12,13,24,25,35,36}};
		sx=2*wm.hexaWidth+10;
		sy=4*wm.hexaHeight+13;
   		tileMap = new byte[sx*sy];
   		for(int i=0; i<sx*sy; i++)
   			tileMap[i]=2;
   		
   				
		for(int i=-1; i<=wm.hexaWidth; i++)
		for(int j=-1; j<=wm.hexaHeight; j++){
			y=1+4*j;
			if(j%2==0){
				x=2*i; 
			}else{
				x=2*i+1;
			}		
							
				if(j%2==1 && j>0){
					if(wm.checkHexaMap(i,j-1)==0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x,y-1,2,sx);
					if(wm.checkHexaMap(i,j-1)!=0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x,y-1,41,sx);
					if(wm.checkHexaMap(i,j-1)==0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x,y-1,0,sx);						
					if(wm.checkHexaMap(i,j-1)!=0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x,y-1,38,sx);												
						
					if(wm.checkHexaMap(i+1,j-1)==0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x+1,y-1,2,sx);
					if(wm.checkHexaMap(i+1,j-1)!=0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x+1,y-1,37,sx);
					if(wm.checkHexaMap(i+1,j-1)==0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x+1,y-1,1,sx);						
					if(wm.checkHexaMap(i+1,j-1)!=0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x+1,y-1,39,sx);																		
				}else{
					if(wm.checkHexaMap(i-1,j-1)==0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x,y-1,2,sx);
					if(wm.checkHexaMap(i-1,j-1)!=0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x,y-1,41,sx);
					if(wm.checkHexaMap(i-1,j-1)==0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x,y-1,0,sx);						
					if(wm.checkHexaMap(i-1,j-1)!=0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x,y-1,38,sx);												
						
					if(wm.checkHexaMap(i,j-1)==0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x+1,y-1,2,sx);
					if(wm.checkHexaMap(i,j-1)!=0 && wm.checkHexaMap(i,j)==0)	
						putTile(tileMap,x+1,y-1,37,sx);
					if(wm.checkHexaMap(i,j-1)==0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x+1,y-1,1,sx);						
					if(wm.checkHexaMap(i,j-1)!=0 && wm.checkHexaMap(i,j)!=0)	
						putTile(tileMap,x+1,y-1,39,sx);																						
				}
			
			if(wm.checkHexaMap(i,j)!=0){			
				putTile(tileMap,x,y,pattern[wm.checkHexaMap(i,j)-1][0],sx);
				putTile(tileMap,x+1,y,pattern[wm.checkHexaMap(i,j)-1][1],sx);
				putTile(tileMap,x,y+1,pattern[wm.checkHexaMap(i,j)-1][2],sx);
				putTile(tileMap,x+1,y+1,pattern[wm.checkHexaMap(i,j)-1][3],sx);
				putTile(tileMap,x,y+2,pattern[wm.checkHexaMap(i,j)-1][4],sx);
				putTile(tileMap,x+1,y+2,pattern[wm.checkHexaMap(i,j)-1][5],sx);
				if(wm.checkHexaMap(i+1,j)==0){
					putTile(tileMap,x+2,y,14,sx);
					putTile(tileMap,x+2,y+1,14,sx);
					putTile(tileMap,x+2,y+2,14,sx);
				}
			}
		}		
	}
	
	void mantainScroll(int cx, int cy)
	{
		// Mantain window scroll
		int x,y;
		
		y=36*cy;
		if(cy%2==0){			
			x=40*cx; 
		}else{
			x=20+(40*cx);
		}
				
		if(mapScrollX<x-(CANVX/2)+23) mapScrollX+=6;
		if(mapScrollX>x-(CANVX/2)+23) mapScrollX-=6;
		
		if(mapScrollY<y-(CANVY/2)+22) mapScrollY+=6;
		if(mapScrollY>y-(CANVY/2)+22) mapScrollY-=6;
			
	}
	
	void showRegionCursor(Graphics g, int cx, int cy, int type)
	{
		int x=0, y=0;
		// Selected region cursor
		
		y=36*cy;
		if(cy%2==0){			
			x=40*cx; 
		}else{
			x=20+(40*cx);
		}		
		x-=mapScrollX; y-=mapScrollY;
		
		switch(type){
			case 0 :/*g.setColor(255,0,0);
					g.setClip(0,0,CANVX,CANVY); 
					g.drawLine(x+0,y+6,x+12,y+0);
					g.drawLine(x+13,y+0,x+25,y+6);
					g.drawLine(x+0,y+7,x+0,y+24);
					g.drawLine(x+26,y+6,x+26,y+24);
					g.drawLine(x+0,y+24,x+12,y+30);
					g.drawLine(x+13,y+30,x+25,y+24);*/
					showSprite(WorldMapTiles,246,0,40,45,x,y,false);					
					break;
			case 1 : PutSprite(GUIImg,x+15,y+17,18,8,GUICor,false);														
					break;
			case 2 : PutSprite(GUIImg,x+15,y+17,18,7,GUICor,false);														
					break;
			case 3 : PutSprite(GUIImg,x+12,y+13,18,14,GUICor,false);														
					break;
		}							
	}		
	
	public void showWarField(WarField wf, int destCur, boolean prev)
	{
		
		Point3D pp1, pp2, pp3, pp4, p2d;
		//painting=true;
		Point3D p3d, n, n2, light=new Point3D(1024,4096,-4096);
		long factor=1024;
		int anim=0, num=1, aux=0, or;
		int xx, yy, zz;
		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		
		light.unit();
		//System.out.println("("+light.x+","+light.y+","+light.z+")");
		
		wf.transformPoints(gameMidlet.cam,gameMidlet.cam.x>>10,(gameMidlet.cam.x>>10)+7);
				
		for(int j=wf.sz-2; j>=0; j--)
		for(int i=wf.sx-2; i>=0; i--)
		
			if(i>=(gameMidlet.cam.x>>10) && i<=(gameMidlet.cam.x>>10)+7)
			//if(true)
			{	
			
			g.setClip(0,0,CANVX,CANVY);
			g.clipRect(0,0,CANVX,CANVY);
					
			pp1=wf.pts2d[i][j];
			pp2=wf.pts2d[i][j+1];
			pp3=wf.pts2d[i+1][j+1];
			pp4=wf.pts2d[i+1][j];
			
			/*n=com.mygdx.mongojocs.bravewar.Point3D.normalVector(p1,p2,p3);
			factor=light.scalarProduct(n);*/
			//if(factor<0) factor=0;			
			
			//factor=1024-((1024-factor)/2);
			//System.out.println("("+n.x+","+n.y+","+n.z+"), Factor:"+factor);
			if(pp1!=null && pp2!=null && pp3!=null)
			if(pp1.ok && pp2.ok && pp3.ok && factor>=0) 
			//if(insideCanvas(pp1) || insideCanvas(pp2) || insideCanvas(pp3))
			dg.fillTriangle(pp1.x,pp1.y,pp2.x,pp2.y,pp3.x,pp3.y,factorColor(55,111,117,wf.tiles[i][j].lightFactor1));						
			
			/*n=com.mygdx.mongojocs.bravewar.Point3D.normalVector(p3,p4,p1);
			factor=light.scalarProduct(n);
			factor=1024-((1024-factor)/2);*/
			//if(factor<0) factor=0;			
			if(pp3!=null && pp4!=null && pp1!=null)
			//if(insideCanvas(pp3) || insideCanvas(pp4) || insideCanvas(pp1))
			if(pp3.ok && pp4.ok && pp1.ok && factor>=0) 
			dg.fillTriangle(pp3.x,pp3.y,pp4.x,pp4.y,pp1.x,pp1.y,factorColor(55,111,117,wf.tiles[i][j].lightFactor2));																					
					
		}	

		// Action Cursor
				
		if(gameMidlet.batCurX>=0 && gameMidlet.batCurZ>=0 && gameMidlet.batCurX<wf.sx-1 && gameMidlet.batCurZ<wf.sz-1){
			
			pp1=wf.pts2d[gameMidlet.batCurX][gameMidlet.batCurZ];
			pp2=wf.pts2d[gameMidlet.batCurX][gameMidlet.batCurZ+1];
			pp3=wf.pts2d[gameMidlet.batCurX+1][gameMidlet.batCurZ+1];
			pp4=wf.pts2d[gameMidlet.batCurX+1][gameMidlet.batCurZ];
			
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(255,0,0);
			if(pp1!=null && pp2!=null && pp3!=null && pp4!=null){
				g.drawLine(pp1.x,pp1.y,pp2.x,pp2.y);
				g.drawLine(pp2.x,pp2.y,pp3.x,pp3.y);
				g.drawLine(pp3.x,pp3.y,pp4.x,pp4.y);
				g.drawLine(pp4.x,pp4.y,pp1.x,pp1.y);
			}
		}
		
		// Number of units
						
		// Destination cursor
		if(destCur>0 && gameMidlet.cnt%2==0 && gameMidlet.destCurX>=0 && gameMidlet.destCurX<wf.sx-1 && gameMidlet.destCurY>=0 && gameMidlet.destCurY<wf.sz-1){
					
			pp1=wf.pts2d[gameMidlet.destCurX][gameMidlet.destCurY];
			pp2=wf.pts2d[gameMidlet.destCurX][gameMidlet.destCurY+1];
			pp3=wf.pts2d[gameMidlet.destCurX+1][gameMidlet.destCurY+1];
			pp4=wf.pts2d[gameMidlet.destCurX+1][gameMidlet.destCurY];			
		
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(255,0,0);
			g.drawLine(pp1.x,pp1.y,pp2.x,pp2.y);
			g.drawLine(pp2.x,pp2.y,pp3.x,pp3.y);
			g.drawLine(pp3.x,pp3.y,pp4.x,pp4.y);
			g.drawLine(pp4.x,pp4.y,pp1.x,pp1.y);
						
			if(gameMidlet.destCurX>=0 && gameMidlet.destCurY>=0 && gameMidlet.destCurX<wf.sx-1 && gameMidlet.destCurY<wf.sz-1){						
				if(destCur==1)
				if (wf.tiles[gameMidlet.destCurX][gameMidlet.destCurY].UnitSide<0 || (wf.tiles[gameMidlet.destCurX][gameMidlet.destCurY].UnitSide==0 && wf.tiles[gameMidlet.destCurX][gameMidlet.destCurY].UnitType==wf.tiles[gameMidlet.batCurX][gameMidlet.batCurZ].UnitType)){
					//p1=new com.mygdx.mongojocs.bravewar.Point3D((gameMidlet.destCurX<<10)+512,wf.heightAt((gameMidlet.destCurX<<10)+512,(gameMidlet.destCurY<<10)+512),(gameMidlet.destCurY<<10)+512);
					pp1=wf.tileMiddle2D(gameMidlet.destCurX,gameMidlet.destCurY);
					//pp1=p1.transform2D(gameMidlet.cam);
					//showSprite(GUIImg,133,0,12,12,pp1.x-6,pp1.y-12,false);
					PutSprite(GUIImg,pp1.x-6,pp1.y-12,18,8,GUICor,false);									
				}
				
				if(destCur==2)
				if (wf.tiles[gameMidlet.destCurX][gameMidlet.destCurY].UnitSide!=0){
					//p1=new com.mygdx.mongojocs.bravewar.Point3D((gameMidlet.destCurX<<10)+512,wf.heightAt((gameMidlet.destCurX<<10)+512,(gameMidlet.destCurY<<10)+512)+200,(gameMidlet.destCurY<<10)+512);
					pp1=wf.tileMiddle2D(gameMidlet.destCurX,gameMidlet.destCurY);
					//p1.transform2D(gameMidlet.cam);
					//showSprite(GUIImg,120,0,12,12,pp1.x-6,pp1.y-12,false);
					PutSprite(GUIImg,pp1.x-6,pp1.y-12,18,7,GUICor,false);									
				}			
			}
		}
				
		// Troops, decorates		
		for(int j=wf.sz-2; j>=0; j--)
		for(int i=wf.sx-2; i>=0; i--)
		//if(i>=0 && j>=0 && j<wf.sz-1 && i<wf.sx-1)
		if(i>=(gameMidlet.cam.x>>10) && i<=(gameMidlet.cam.x>>10)+5){	
			
			if(wf.decor[i][j][0]!=0){				
				showDecorate(wf,i,j,wf.decor[i][j][0]);
			}
			
			if(wf.tiles[i][j].UnitType>=0){
				anim=0;
																
				if(wf.tiles[i][j].UnitAmount>50) num=3;
				else if(wf.tiles[i][j].UnitAmount>10) num=2;
				else num=1;
												
				for(int k=0; k<num; k++){				
										
					xx=1024*i+512;
					zz=1024*j+768;
						
					//yy=wf.heightAt(xx,zz);
								
					or=wf.tiles[i][j].UnitOr;
					
					//System.out.println("Get p2d at "+i+","+j);
					
					p2d=wf.tileMiddle2D(i,j);
					
					switch(num){
						case 2 : if(k==1) p2d.x-=4; if(k==0) p2d.x+=4; aux=0; break;
						case 3 : if(k==2) {p2d.x-=3; p2d.y-=4;}; if(k==0) {p2d.x+=3; p2d.y-=4;} aux=1; break;
					}
										
					// Fighting troop
					if(gameMidlet.state>=GameMidletLogic.BATTLE_INFANTRY_ATTACK && gameMidlet.state<=GameMidletLogic.BATTLE_TANK_ATTACK
					 && i==gameMidlet.batCurX && j==gameMidlet.batCurZ){
						anim=2;
						or=gameMidlet.movingOr;
					}
				
					showUnit(p2d,
								wf.tiles[i][j].UnitType,or,wf.tiles[i][j].UnitSide==1,anim,
								(gameMidlet.batCurX==i && gameMidlet.batCurZ==j && k==aux) || (gameMidlet.destCurX==i && gameMidlet.destCurY==j && k==aux),
								wf.tiles[i][j].UnitAmount);						
						
				}
			}
						
		}
			
		if(prev) showBattleActionsPreview(wf,gameMidlet.nextBattleAction);
		
		// Moving troops
					
		if(gameMidlet.state==GameMidletLogic.BATTLE_TROOPMOVE || gameMidlet.state==GameMidletLogic.BATTLE_MOVE_CAM_FOLLOW/*&& i==gameMidlet.destCurX && j==gameMidlet.destCurY*/){
				
			if(gameMidlet.moveTroopsAmount[0]>50) num=3;
			else if(gameMidlet.moveTroopsAmount[0]>10) num=2;
			else num=1;
					
			for(int k=0; k<num; k++){
														
				xx=(1024*gameMidlet.destCurX)+512+gameMidlet.movingOffsetX;
				zz=(1024*gameMidlet.destCurY)+768+gameMidlet.movingOffsetZ;
						
				switch(num){
					case 2 : if(k==1) zz-=100; if(k==0) zz+=100; aux=0; break;
					case 3 : if(k==2) {xx-=100; zz-=200;}; if(k==0) {xx-=100; zz+=200;} aux=1; break;
				}
						
				yy=wf.heightAt(xx,zz);
								
				p3d=new Point3D(xx,yy,zz);
				p2d=p3d.transform2D(gameMidlet.cam);
			
				//p2d=gameMidlet.warField.tileMiddle2D(x,z);
				
							
				showUnit(p2d,
					gameMidlet.movingType,
					gameMidlet.movingOr,
					gameMidlet.movingSide==1,
					1,k==0,
					gameMidlet.moveTroopsAmount[0]);								
								
				}
			}										
	}
	
	public void showBattleTimer()
	{
			PutSprite(GUIImg,10,2,18,14,GUICor,false);									
			showFontText(""+(gameMidlet.BATTLE_ROUND_TIME-gameMidlet.battleTimer)/15,24,6,0);												
			
			g.setClip(0,0,CANVX,CANVY);
			
			if(gameMidlet.actionPoints<50) g.setColor(255,0,0);
			else if(gameMidlet.actionPoints<100) g.setColor(255,255,0);
			else g.setColor(0,255,0);
			g.fillRect(2,18,gameMidlet.actionPoints/6,3);		
	}
	
	public void showUnit(Point3D pos, int type, int unitOri, boolean op, int anim, boolean showAmount, int amount)
	{
			Point3D p3d;
			Point3D p2d;
			
			int fr=0, ori=unitOri+gameMidlet.cam.roty;
			boolean inv=false;
			
			if(ori<0) ori+=256;
			ori=ori%256;
			
			ori=ori+8;
			ori=ori%256;
			//if(ori<0) ori+=256;
						
			switch((ori/16)%8){
				case 0 : fr=2; inv=true; break;				
				case 1 : fr=1; inv=true; break;
				case 2 : fr=0; inv=false; break;
				case 3 : fr=1; inv=false; break;
				case 4 : fr=2; inv=false; break;
				case 5 : fr=3; inv=false; break;
				case 6 : fr=4; inv=false; break;
				case 7 : fr=3; inv=true; break;
				
			}
			
			// Opponent: Other color			
			if(op){
				switch(type){
					case 0 : fr+=25; break;
					case 1 : fr+=20; break;
					case 2 : fr+=15; break;
					case 3 : fr+=10; break;
				}
				
			}
			
			// Animate
			// 0 : No animate
			// 1 : Walking animation
			// 2 : Attack animation
			switch(anim){
				case 1 :				
					switch(type){
						case 1 :
						case 0 :
						if((gameMidlet.cnt/2)%2==0) fr+=5;
						else fr+=10;
						break;
						case 2 :
						if((gameMidlet.cnt/2)%2==0) fr+=5;						
						break;	
					}
				break;			
				
				case 2 :
					switch(type){					
						case 0 :
						if((gameMidlet.cnt/2)%2==0) fr+=15;
						else fr+=20;
						break;						
						case 1 :
						if((gameMidlet.cnt/2)%2==0) fr+=15;						
						break;												
						case 2 :
						if((gameMidlet.cnt/2)%2==0) fr+=10;						
						break;												
						case 3 :
						if((gameMidlet.cnt/2)%2==0) fr+=5;												
						break;
																	
					}				
				break;						
			}
						
			switch(type){
				case 0 : PutSprite(HumansImg,pos.x-4,pos.y-12,12,fr,HumansCor,2,inv); break;
				case 1 : PutSprite(HumansImg,pos.x-4,pos.y-12,12,50+fr,HumansCor,2,inv); break;
				case 2 : if((gameMidlet.cnt/2)%2==0 && anim==2)
						 	PutSprite(OrcsImg,pos.x-10,pos.y-17,21,fr-10,OrcsCor,2,inv); 
						 PutSprite(OrcsImg,pos.x-10,pos.y-17,21,fr,OrcsCor,2,inv); 
				break;
				case 3 : if((gameMidlet.cnt/2)%2==0 && anim==2)
						 	PutSprite(TanksImg,pos.x-10,pos.y-17,26,fr-5,TanksCor,inv); 
						 PutSprite(TanksImg,pos.x-13,pos.y-16,26,fr,TanksCor,inv); 
				break;
			}
			
			if(showAmount)
				showFontText(""+amount,pos.x-6,pos.y-20,0);				
												
	}
	
	public void showExplosion(WarField wf, int x, int z, int type, int fr)
	{
		Point3D p1;
		Point3D p2d1;
		int xx=(x<<10)+512, zz=(z<<10)+512;
		
		p1=new Point3D(xx,wf.heightAt(xx,zz)+200,zz);
		
		p2d1=p1.transform2D(gameMidlet.cam);
				
		switch(type){			
			case 0 : PutSprite(Explos,p2d1.x-6,p2d1.y-6,12,fr,ExplCor,false); break;
			case 1 : PutSprite(Explos,p2d1.x-6,p2d1.y-6,12,5+fr,ExplCor,false); break;
			case 2 : PutSprite(Explos,p2d1.x-10,p2d1.y-9,12,fr,ExplCor,false); 
					 PutSprite(Explos,p2d1.x-2,p2d1.y-6,12,fr,ExplCor,false);
					 PutSprite(Explos,p2d1.x-6,p2d1.y-2,12,fr,ExplCor,false);
			break;
		}
	}
	
	public void showDecorate(WarField wf, int xx, int zz, int t)
	{
		//com.mygdx.mongojocs.bravewar.Point3D p1;
		Point3D p2d1;
		//int xx=(x<<10), zz=(z<<10);
		
		//p1=new com.mygdx.mongojocs.bravewar.Point3D(xx,wf.heightAt(xx,zz),zz);
		
		p2d1=wf.pts2d[xx][zz];//p1.transform2D(gameMidlet.cam);
		//p2d1=wf.tileMiddle2D(0,0);
		
		switch(t){
			case 1 : showSprite(Misc,0,0,22,20,p2d1.x-11,p2d1.y-16,false); break;
			case 2 : showSprite(Misc,22,0,16,21,p2d1.x-9,p2d1.y-18,false); break;
			case 3 : showSprite(Misc,38,0,12,16,p2d1.x-5,p2d1.y-13,false); break;
			case 4 : showSprite(Misc,50,0,5,8,p2d1.x-2,p2d1.y-7,false); break;
			case 5 : showSprite(Misc,55+(14*(gameMidlet.cnt%2)),0,14,13,p2d1.x-7,p2d1.y-9,false); break;
			case 6 : showSprite(Misc,83,0,10,9,p2d1.x-5,p2d1.y-7,false); break;
			case 7 : showSprite(Misc,55,15,5,6,p2d1.x-2,p2d1.y-5,false); break;
			case 8 : showSprite(Misc,62,15,10,7,p2d1.x-5,p2d1.y-5,false); break;
			case 9 : showSprite(Misc,74,13,12,10,p2d1.x-7,p2d1.y-8,false); break;
			case 10: showSprite(Misc,87,14,5,7,p2d1.x-1,p2d1.y-7,false); break;
			
		}
	}
	
	public void showAttackParabole()	
	{
		Point3D p1, p2;
		Point3D p2d1, p2d2;
		
		int inc=gameMidlet.shotPower/8;
		
		for(int i=0; i<8; i++){
			p1=new Point3D((gameMidlet.batCurX<<10)+512+((cos(gameMidlet.shotAngle)*i*inc)>>10),200+sin(16*i),(gameMidlet.batCurZ<<10)+512+((sin(gameMidlet.shotAngle)*inc*i)>>10));
			p2=new Point3D((gameMidlet.batCurX<<10)+512+((cos(gameMidlet.shotAngle)*(i+1)*inc)>>10),
							200+sin(16*(i+1)),
							(gameMidlet.batCurZ<<10)+512+((sin(gameMidlet.shotAngle)*inc*(i+1))>>10));
			
			p2d1=p1.transform2D(gameMidlet.cam);
			p2d2=p2.transform2D(gameMidlet.cam);
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(255,0,0);
			g.drawLine(p2d1.x,p2d1.y,p2d2.x,p2d2.y);			
		}
		
	}
	
	public void showBattleActionsPreview(WarField wf, int ac1)
	{
		BattleAction ba;
		//com.mygdx.mongojocs.bravewar.Point3D p1, p2;
		Point3D p2d1, p2d2;
		//int xx, zz;
		
		for(int i=0; i<gameMidlet.round.nActions; i++)
		if(i>=ac1)
		{
			ba=gameMidlet.round.ac[i];
			
			/*xx=(ba.xori<<10)+512;
			zz=(ba.zori<<10)+512;
			p1=new com.mygdx.mongojocs.bravewar.Point3D(xx,wf.heightAt(xx,zz),zz);
						
			xx=(ba.xdes<<10)+512;
			zz=(ba.zdes<<10)+512;
			p2=new com.mygdx.mongojocs.bravewar.Point3D(xx,wf.heightAt(xx,zz),zz);*/
			
			//p2d1=p1.transform2D(gameMidlet.cam);
			//p2d2=p2.transform2D(gameMidlet.cam);
			
			p2d1=wf.tileMiddle2D(ba.xori,ba.zori);
			p2d2=wf.tileMiddle2D(ba.xdes,ba.zdes);
						
			if(ba.type==Round.MOVE) g.setColor(0,0,255);
			else g.setColor(255,255,0);
			g.setClip(0,0,CANVX,CANVY);
			g.drawLine(p2d1.x,p2d1.y,p2d2.x,p2d2.y);
			g.drawArc(p2d2.x-5,p2d2.y-5,10,10,0,360);
			
		}	
		
	}
	
	public int factorColor(int r, int g, int b, long factor)
	{
		return (int)(((((factor*r)/1024) & 0xFF)<<16) 
			| ((((factor*g)/1024) & 0xFF)<<8) 
			| (((factor*b)/1024) & 0xFF) 
			| 0xFF000000);
	}
			
	int font_sizex[]={4,1,3,5,4,4,4,1,2,2,3,5,2,3,1,3,
					  4,2,4,4,4,4,4,4,4,4,1,2,3,3,3,4,
					  4,4,4,4,4,4,4,4,4,1,3,4,4,5,4,4,
					  4,4,4,4,3,4,5,5,4,4,4,2,3,2,3,4};
		
	public void showFontText(String s, int x, int y, int f)
	{
		char c[];		
		int cc, xx=x;
				
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-0x20);
										
			if(cc<0 || cc>=font_sizex.length) xx+=font_sizex[0];
			else{
			
				//PutSprite( FontImg, xx+1, y, 8, cc + (f==1 ? 64 : 0), FontCor, false);
				showSprite(FontImg, (cc%16)*6, (f==1 ? 24 : 0) + (cc/16)*6, 6, 6, xx+1, y, false);
				xx+=font_sizex[cc]+1;
			} 
						
		}			
	}
		
	public void showFontTextCenter(String s, int y, int f)
	{
		char c[];		
		int sx=0, cc;
		
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-'!'-1);
			if(cc<0 || cc>=font_sizex.length) sx+=font_sizex[0];
			else sx+=font_sizex[cc]+1;
			//sx+=5;
		}

		showFontText(s,(CANVX-sx)/2,y,f);

	}	
	
	int big_font_sizex[]={8,3,6,10,10,10,10,3,4,4,6,7,5,8,3,10,
					  	  10,6,10,10,10,10,10,10,10,10,3,4,5,6,5,10,
					  	  10,10,10,10,10,10,10,10,10,3,5,8,8,10,10,10,
					  	  10,10,10,10,10,10,10,10,10,9,10,6,6,7,10,10};
		
	
	public void showBigFontText(String s, int x, int y, int f)
	{
		char c[];		
		int cc, xx=x;
				
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-0x20);
										
			if(cc<0 || cc>=font_sizex.length) xx+=big_font_sizex[0];
			else{
			
				//PutSprite( FontImg, xx+1, y, 8, cc + (f==1 ? 64 : 0), FontCor, false);
				showSprite(BigFontImg, (cc%16)*10, (f==1 ? 40 : 0) + (cc/16)*10, 10, 10, xx+1, y, false);
				xx+=big_font_sizex[cc]+1;
			} 
						
		}			
	}
		
	public void showBigFontTextCenter(String s, int y, int f)
	{
		char c[];		
		int sx=0, cc;
		
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-0x20);
			if(cc<0 || cc>=font_sizex.length) sx+=big_font_sizex[0];
			else sx+=big_font_sizex[cc]+1;
			//sx+=5;
		}

		showBigFontText(s,(CANVX-sx)/2,y,f);

	}	
	
	
	void drawGUIBox(int px, int py, int sx, int sy)
	{
		int ssx=sx/16, ssy=sy/16, x=px, y=py, ix=0, iy=0;
		
		g.setColor(0x779988);
		g.setClip(0,0,CANVX,CANVY);
		g.fillRect(px+16,py+16,sx-32,sy-32);
			
		for(int j=0; j<ssy; j++){			
			for(int i=0; i<ssx; i++){
												
				if(j==0 && i==0) {ix=0; iy=0;}
				else if(j==0 && i==ssx-1) {ix=16; iy=0;}
				else if(j==ssy-1 && i==0) {ix=0; iy=16;}
				else if(j==ssy-1 && i==ssx-1) {ix=16; iy=16;}
				else if(j==0) {ix=8; iy=0;}
				else if(i==0) {ix=0; iy=8;}
				else if(i==ssx-1) {ix=16; iy=8;}
				else if(j==ssy-1) {ix=8; iy=16;}
				else {ix=-1; iy=-1;}
									
				if(ix!=-1 && iy!=-1){																			
					g.setClip(x,y,16,16);
					g.drawImage(BoxImg,x-ix,y-iy,20);
				}

				x+=16;
			}
			x=px; y+=16;
		}		
				
	}
	
	void drawGUIStringBox(String s, int px, int py)
	{
		int maxl=s.length();
				
		drawGUIBox(px,py-4,6*(maxl+1),32);
				
		showFontText(s,px+8,py+8,0);			
	}
	
	void drawGUIBigStringBox(String s, int px, int py)
	{
		int maxl=s.length();
				
		drawGUIBox(px,py-4,10*(maxl+1),32);
				
		showBigFontText(s,px+8,py+8,0);			
	}
		
	
	void drawGUIStringBoxRight(String s, int py)
	{
		int maxl=s.length(), l=(6*(maxl+1));
		
		l-=(l%8);
				
		drawGUIBox(CANVX-l,py+4,l+30,32);
				
		showFontText(s,CANVX-l+8,py+10,0);			
	}
	
	void drawGUITextBox(String[] ops, int px, int py)
	{
		int maxl=0;
	
		for(int i=0; i<ops.length; i++)
			if(ops[i].length()>maxl) maxl=ops[i].length();
			
		drawGUIBox(px,py,6*(maxl+2),8*(ops.length+2));
		
		for(int i=0; i<ops.length; i++)
				showFontText(ops[i],px+8,py+8+(8*i),0);			
	}
	
	
	void drawGUIBoxMenu(String[] ops, int selop, int px, int py)
	{
		int maxl=0;
	
		for(int i=0; i<ops.length; i++)
			if(ops[i].length()>maxl) maxl=ops[i].length();
			
		drawGUIBox(px,py,6*(maxl+2),8*(ops.length+2+(ops.length%2)));
		
		for(int i=0; i<ops.length; i++)
			if(i==selop)
				showFontText(ops[i],px+8,py+8+(8*i),0);	
			else 
				showFontText(ops[i],px+8,py+8+(8*i),1);	
		
	}
	
	void drawBigGUIBoxMenu(String[] ops, int selop, int px, int py)
	{
		int maxl=0;
	
		for(int i=0; i<ops.length; i++)
			if(ops[i].length()>maxl) maxl=ops[i].length();
			
		drawGUIBox(px,py,10*(maxl+3),14*(ops.length+2));
		
		for(int i=0; i<ops.length; i++)
			if(i==selop)
				showBigFontText(ops[i],px+12,py+14+(14*i),0);	
			else 
				showBigFontText(ops[i],px+12,py+14+(14*i),1);	
		
	}
	
	
	public void PutSprite(Image Img,  int X, int Y,  int CuadSize, int Frame, byte[] Coor, boolean inv)
	{		
		PutSprite(Img, X, Y, CuadSize, Frame, Coor, 1, inv);
		/*Frame*=6;
	
		int DestX=Coor[Frame++];
		int DestY=Coor[Frame++];
		int SizeX=Coor[Frame++];
		if (SizeX==0) {return;}
		int SizeY=Coor[Frame++];
		int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
		int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}
			
		if(!inv) showSprite(Img,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY,false);
		else	 showSprite(Img,SourX+128,SourY+128,SizeX,SizeY,X+CuadSize-SizeX-DestX,Y+DestY,true);*/
	}
	

	public void PutSprite(Image Img,  int X, int Y,  int CuadSize, int Frame, byte[] Coor, int Trozos, boolean inv)
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
	
		//ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX-sc.ScrollX,Y+DestY-sc.ScrollY);
		if(!inv) showSprite(Img,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY,false);
		else	 showSprite(Img,SourX+128,SourY+128,SizeX,SizeY,X+CuadSize-SizeX-DestX,Y+DestY,true);		
		}
	}
			
	public void showSprite(Image im, int bx, int by, int sx, int sy, int px, int py, boolean inv)
	{
		int x1, x2, y1, y2;	
		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		
		x1=px; y1=py; x2=px+sx; y2=py+sy;
		
		if(x1>CANVX || x2<0 || y1>CANVY || y2<0) return;
		
		if(x1<0) x1=0; 
		if(x2>=CANVX) x2=CANVX-1; 

		if(y1<0) y1=0; 
		if(y2>CANVY) y2=CANVY; 
		
		g.setClip(x1,y1,x2-x1,y2-y1);
		g.clipRect(x1,y1,x2-x1,y2-y1);
		if(inv) dg.drawImage(im,px+bx-(im.getWidth()-sx),py-by,20,DirectGraphics.FLIP_HORIZONTAL);			
		else 	g.drawImage(im,px-bx,py-by,20);				
	}									
	
	
	void clearDisplay(int r, int gg, int b)
	{
		g.setColor(r,gg,b);
       	g.setClip(0,0,CANVX,CANVY);
       	g.fillRect(0,0,CANVX,CANVY);			
	}
	
	//=============================================================================
	
	void LoadFile(byte[] buffer, String Nombre)
	{
		System.gc();
		InputStream is = getClass().getResourceAsStream(Nombre);
	
		try	{
		is.read(buffer, 0, buffer.length);
		is.close();
		}catch(Exception exception) {}
		System.gc();
	}
	
	public Image LoadImage(String FileName)
	{
		System.gc();
	
		try	{
			Image Img = Image.createImage(FileName);
			System.gc();
			return Img;
		} catch (Exception e) {System.out.println("Error leyendo PNG: "+FileName);}
	
		return null;
	}	
	
	// Control stuff
	
	public boolean up, down, left, right, but1, but2, but3, but4, any, anybut, men, accept;	
	
	public void controlReset()
	{
		up=false; down=false; left=false; right=false; 
		but1=false; but2=false; but3=false; but4=false;		
		any=false; anybut=false; men=false; accept=false;
	}	
	
	public boolean insideCanvas(Point3D p)
	{
		if(p.x>=0 && p.x<CANVX && p.y>=0 && p.y<CANVY) return true;
		else return false;		
	}
	
	
	// Scroll Stuff =======================================================
	
	byte[] FaseMap;
	int FaseSizeX;
	int FaseSizeY;
	int FaseX;
	int FaseY;
	
	Image FondoImg;
	Graphics FondoGfx;
	byte[] FondoMap;
	int FondoSizeX;
	int FondoSizeY;
	int FondoX;
	int FondoY;
	
	Image TilesImg;
	int TilesLineX;
	
	int ScrollX;
	int ScrollY;
	int ScrollSizeX;
	int ScrollSizeY;
	
	
	// -------------------
	// Scroll INI
	// ===================
	
	public void ScrollINI(int SizeX, int SizeY)
	{
		ScrollX=0;
		ScrollY=0;
		ScrollSizeX=SizeX;
		ScrollSizeY=SizeY;
	
		FondoSizeX=(SizeX/13)+((SizeX%13==0)?1:2);
		FondoSizeY=(SizeY/6)+((SizeY%6==0)?1:2);
		FondoMap = new byte[FondoSizeX*FondoSizeY];
	
		FondoImg=Image.createImage(FondoSizeX*13, FondoSizeY*6);
		FondoGfx=FondoImg.getGraphics();
	}
	
	
	// -------------------
	// Scroll SET
	// ===================
	
	public void ScrollSET(byte[] Mapa, int SizeX, int SizeY,  Image Img, int LineX)
	{
		FaseMap=Mapa;
		FaseSizeX=SizeX;
		FaseSizeY=SizeY;
	
		TilesImg=Img;
		TilesLineX=LineX;
	
		FaseX=0;
		FaseY=0;
	
		FondoX=0;
		FondoY=0;
	
		for (int i=0 ; i<FondoMap.length ; i++) {FondoMap[i]=-1;}
	
		ScrollUpdate();
	}
	
	
	// -------------------
	// Scroll RUN
	// ===================
	
	public void ScrollRUN(int X, int Y)
	{
		while (X<0) {X+=(FaseSizeX*13);}
		while (Y<0) {Y+=(FaseSizeY*6);}
	
		while (X>=FaseSizeX*13) {X-=(FaseSizeX*13);}
		while (Y>=FaseSizeY*6) {Y-=(FaseSizeY*6);}
	
		ScrollX=X;
		ScrollY=Y;
	
		FaseX=(X/13);
		FaseY=(Y/6);
	
		FondoX=FaseX%FondoSizeX;
		FondoY=FaseY%FondoSizeY;
	
		ScrollUpdate();
	}
		
	// -------------------
	// Scroll Update
	// ===================
	
	public void ScrollUpdate()
	{
		int FondoDir=0;
		int CorX=FaseX+(FondoSizeX-FondoX);
		int CorY=FaseY+(FondoSizeY-FondoY);
	
		if (CorY< 0) {CorY+=FaseSizeY;}
		if (CorY>=FaseSizeY) {CorY-=FaseSizeY;}
	
		if (CorX< 0) {CorX+=FaseSizeX;}
		if (CorX>=FaseSizeX) {CorX-=FaseSizeX;}
	
		for (int y=0 ; y<FondoSizeY ; y++)
		{
		if (y==FondoY) {if ((CorY-=FondoSizeY) < 0) {CorY+=FaseSizeY;}}
	
			for (int x=0, x2=FondoX, i=0 ; i<2; i++)
			{
				for (; x<x2 ; x++)
				{
					int FaseDir=(CorY*FaseSizeX)+CorX; if (++CorX >= FaseSizeX) {CorX-=FaseSizeX;}
	
					if (FondoMap[FondoDir++] != FaseMap[FaseDir])
					{
					int TileNum=FaseMap[FaseDir];
					FondoMap[FondoDir-1] = (byte)TileNum;
					if (TileNum < 0 ) {TileNum+=256;}
					FondoGfx.setClip(x*13 ,y*6,  13,6);
					FondoGfx.drawImage(TilesImg, (x*13)-((TileNum%TilesLineX)*13), (y*6)-((TileNum/TilesLineX)*6), Graphics.TOP|Graphics.LEFT);
					}
				}
	
				if (i==0)
				{
				if ((CorX-=FondoSizeX) < 0) {CorX+=FaseSizeX;}
				x2=FondoSizeX;
				} else {
				if (++CorY >= FaseSizeY) {CorY-=FaseSizeY;}
				}
			}
		}
	}
	
	public void ScrollIMP(Graphics Gfx)
	{
		int x=ScrollX%13;
		int y=ScrollY%6;
	
		int sx=((FondoSizeX-FondoX)*13)-x;
		int sy=((FondoSizeY-FondoY)*6)-y;
	
		int px=ScrollSizeX-sx;
		int py=ScrollSizeY-sy;
	
		int nx=-(FondoX*13)-x;
		int ny=-(FondoY*6)-y;
	
	
		if (FondoY==0)
		{
			if (FondoX==0)
			{
			Gfx.setClip( 0,  0, ScrollSizeX, ScrollSizeY);
			Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
			} else {
			Gfx.setClip( 0,  0, sx, ScrollSizeY);
			Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
	
			Gfx.setClip(sx,  0, px, ScrollSizeY);
			Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);
			}
	
		} else {
	
		if (FondoX==0)
		{
		Gfx.setClip( 0,  0, ScrollSizeX, sy);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
	
		Gfx.setClip( 0, sy, ScrollSizeX, py);
		Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);
	
		} else {
	
		Gfx.setClip( 0,  0, sx, sy);
		Gfx.drawImage(FondoImg, nx, ny, Graphics.TOP|Graphics.LEFT);
	
		Gfx.setClip(sx,  0, px, sy);
		Gfx.drawImage(FondoImg, sx, ny, Graphics.TOP|Graphics.LEFT);
	
	
		Gfx.setClip( 0, sy, sx, py);
		Gfx.drawImage(FondoImg, nx, sy  , Graphics.TOP|Graphics.LEFT);
	
		Gfx.setClip(sx, sy, px, py);
		Gfx.drawImage(FondoImg, sx, sy  , Graphics.TOP|Graphics.LEFT);
		}
		}
	
	}
	
	
	// -------------------
	// Scroll END
	// ===================
	
	public void ScrollEND()
	{
		FaseMap=null;
		FondoMap=null;
		TilesImg=null;
		FondoGfx=null; FondoImg=null;
	}
	
	// Trig functions
	
	private static short sintable[];/*={
	0,25,50,75,100,125,150,175,199,224,248,273,297,321,344,368,391,414,437,460,482,504,
	526,547,568,589,609,629,649,668,687,706,724,741,758,775,791,807,822,837,851,865,
	878,890,903,914,925,936,946,955,964,972,979,986,993,999,1004,1008,1012,1016,1019,1021,
	1022,1023};*/
	
	private static short costable[];/*={
	1024,1023,1022,1021,1019,1016,1012,1008,1004,999,993,986,979,972,964,955,946,936,
	925,914,903,890,878,865,851,837,822,807,791,775,758,741,724,706,687,668,649,629,
	609,589,568,547,526,504,482,460,437,414,391,368,344,321,297,273,248,224,199,175,
	150,125,100,75,50,25};*/
	
	private static short tantable[];/*={
	0,25,50,75,100,126,151,177,203,229,256,283,310,338,366,395,424,453,484,515,547,580,
	613,648,684,721,759,799,840,883,928,974,1024,1075,1129,1187,1247,1312,1380,1453,
	1532,1617,1708,1807,1915,2034,2165,2310,2472,2654,2861,3099,3375,3700,4088,4560,
	5148,5901,6903,8302,10397,13882,20845,41719
	};
	*/
	
	public void initTables()
	{		
		byte temp[]= new byte[384];
		
		temp = LoadFile(6);
				
		sintable=new short[64];
		for(int i=0; i<64; i++)
			sintable[i]=(short)(((temp[i*2]&0xFF)<<8) | ((temp[i*2+1]&0xFF))); 		
			
		costable=new short[64];
		for(int i=0; i<64; i++)
			costable[i]=(short)(((temp[128+i*2]&0xFF)<<8) | ((temp[i*2+1+128]&0xFF))); 		
			
		tantable=new short[64];
		for(int i=0; i<64; i++)
			tantable[i]=(short)(((temp[i*2+256]&0xFF)<<8) | ((temp[i*2+1+256]&0xFF))); 		
		
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
			v=costable[a%64];
		else 	v=costable[63-(a%64)];	
		if((a>>6==1) || (a>>6==2)) v=-v;
		return v;
	}
	
	static int tan(int a)
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
	/*static int asin(int v)
	{
		int a=0, vabs;
		if(v<0) vabs=-v;
		else vabs=v;
		
		while((sintable[a]<v) && (a<63)) a++;	
		
		if(v>=0) return a;
		else return (256-a)%256;
		
	}
	static int acos(int v)
	{
		int a=0, vabs;
		if(v<0) vabs=-v;
		else vabs=v;
		
		while((costable[a]>v) && (a<63)) a++;	
		
		if(v>=0) return a;
		else return 128-a;
		
	}*/
	static int atan(int s, int c)
	{
		int v,a=0;
		if(c!=0) v=(s<<10)/c;
		else v=99999;
		
		if(v<0) v=-v;
				
		while((tantable[a]<v) && (a<63)) a++;
		
		if(s>=0 && c>=0)
			return a;
		else if(s>=0 && c<0)
			return 128-a;
		else if(s<0 && c<0)
			return 128+a;		
		return (256-a)%256;		
	}		
	
	short[] SizeArc = new short[] {78,768,90,1080,360,120,384}; 
	
	public byte[] LoadFile(int Pos)
	{
		System.gc();
		InputStream is = getClass().getResourceAsStream("/data.arc");

		byte[] Dest = new byte[SizeArc[Pos]];

		try {
			for (int i=0 ; i<Pos ; i++) {is.skip(SizeArc[i]);}
							
			for (int i=0 ; i<Dest.length ; i++)
				Dest[i] = (byte) is.read();
			
			is.close();
		} catch(Exception exception) {}
		System.gc();
		return Dest;
	}
	 	
	
	public byte[][] LoadFile(String Nombre)
	{
		System.gc();
		InputStream is = getClass().getResourceAsStream(Nombre);
	
		byte[][] Bufer = null;
	
		try	{
			int Size = (is.read() << 8) | is.read();
	
			Bufer = new byte[Size][];
	
			for (int Base=0 ; Base<Bufer.length ; Base++)
			{
				Size = (is.read() << 8) | is.read();
	
				Bufer[Base] = new byte[Size];
	
				for(int i=0; i<Size; i++)
					Bufer[Base][i] = (byte) is.read();
					//is.read(Bufer[Base], 0, Size);
			}
	
			is.close();
	
		} catch(Exception exception) {}
		System.gc();
	
		return Bufer;
	} 	
			
} // end com.mygdx.mongojocs.bravewar.com.mygdx.mongojocs.sanfermines2006.GameCanvas



