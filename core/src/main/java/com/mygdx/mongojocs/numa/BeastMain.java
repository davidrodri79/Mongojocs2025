package com.mygdx.mongojocs.numa;/*
	"" Coded by David Rodriguez 2003
*/


import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextField;
//import com.mygdx.mongojocs.midletemu.Runnable;
//import com.mygdx.mongojocs.midletemu.Thread;


/*import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;*/
import java.util.Random;

/*import javax.microedition.rms.*;
import javax.microedition.rms.RecordStore;*/
import java.io.*;



public class BeastMain extends MIDlet implements Runnable, CommandListener
{
		
	public static BeastCanvas gameCanvas;
	public static Display display;
	public static Form scoreForm;
	public static TextField scoreField;
	public Thread thread;
	public static String BeastDataName="Data";
	
	
	public static final int PLAY=0;	
	public static final int SOUND=1;	
	public static final int VIBR=2;	
	public static final int HELP=3;	
	public static final int ABOUT=4;	
	public static final int QUIT=5;
	public static final int RESUME=6;
	public static final int ABORT=7;
	public static final int KEYCONF=8;
	
			
	public static final int MAIN_MENU=0;	
	public static final int GAME_ACTION=1;
	public static final int LEVEL_LOAD=2;
	public static final int LEVEL_ENDED=3;
	public static final int GAME_OVER=4;
	//public static final int LEVEL_RESET=5;
	public static final int IN_GAME_MENU=6;	
	public static final int CONTROLS=7;
	public static final int CONTROLS2=8;
	public static final int EXPLAIN=9;
	public static final int LEVEL_SELECT=10;
	public static final int CREDITS=11;
	public static final int GAME_END=12;
	public static final int TITLE=13;
	public static final int LOADING=14;
	public static final int LOGO=15;
	public static final int INITIALLOAD=16;
	
	public static final int TOTALGEMS = 137;
	
	
	public static int state, cnt, level, option, lastLev, helpType;		
	public static boolean vibr=true, sound=true, keyConf=false, needMore, helpDone[] = new boolean[6], doUpdate = true;	
	public static short pickedGems[] = new short[8];
	public static Random rnd;
		
	public static final int levelEnemies[][]={
	
		{BeastEnemy.DINO, BeastEnemy.PLANT, BeastEnemy.SCORPION},
		{BeastEnemy.DINO, BeastEnemy.TOAD, BeastEnemy.DRAGON},
		{BeastEnemy.DINO, BeastEnemy.MATE, BeastEnemy.DRAGON},
		{BeastEnemy.DINO, BeastEnemy.SCORPION, BeastEnemy.TOAD},
		{BeastEnemy.DINO, BeastEnemy.MATE, BeastEnemy.INSECT},
		{BeastEnemy.DINO,  BeastEnemy.DRAGON, BeastEnemy.SNAKE},
		{BeastEnemy.DINO, BeastEnemy.SNAKE, BeastEnemy.INSECT},
		{BeastEnemy.DINO}		
	};
	
	public BeastWorld world;
	public BeastPlayer pl;
	public BeastEnemy en[], bo;	
	
	public BeastMain()	
	{
		gameCanvas = new BeastCanvas(this);
		gameCanvas.initTables();

		Display.getDisplay(this).setCurrent(gameCanvas);
						
		rnd=new Random();
						
		loadData();		
			
		setState(INITIALLOAD);
		
		thread=new Thread(this);
		thread.start();
				
		System.gc();		
	}
	
				
	public void startApp()
	{						
		display = Display.getDisplay(this);								
		display.setCurrent(gameCanvas);			
	}
	
	public void commandAction (Command c, Displayable d)
	{
	 /*      String label = c.getLabel();
	       if (label == com.mygdx.mongojocs.bravewar.Text.done ) {
	      		display.setCurrent(gameCanvas);
	      		gameCanvas.ctrl.reset();
	      		//set_state(NEW_RECORD_OK);	      	
	       } 	 	*/
	}	
	
	protected void pauseApp()
	{
		
	}
	
	protected void destroyApp(boolean unconditional)
	{
		/*display.setCurrent(null);
		gameCanvas=null; */
		thread = null;
		saveData();
		System.gc();
		this.notifyDestroyed();
	}

	public void runInit()
	{
		doUpdate = true;
	}

	public void runTick()
	{

		long t=System.currentTimeMillis();

		if(doUpdate){
			try{

				gameUpdate();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}

			//System.gc();

			if(gameCanvas.painting==false)
			{
				gameCanvas.repaint();
				gameCanvas.serviceRepaints();
			}

		}

		do{
			try{
				Thread.sleep(1);

			}catch(InterruptedException e) {}

		} while(System.currentTimeMillis()<t+43);
	}

	public void runEnd()
	{

	}
	
	public void run()
	{	
		long t;			
		
		doUpdate = true;	
														
		while (thread!=null){

			t=System.currentTimeMillis();
			
			if(doUpdate){
				try{			
																		
					gameUpdate();
								
				}
				catch (Exception e)
				{
					e.printStackTrace();
					e.toString();
				}
				
				//System.gc();
									
				if(gameCanvas.painting==false)
				{
					gameCanvas.repaint();									 		  			 				 								 																				
					gameCanvas.serviceRepaints();																								
				}
					
			}
			
			do{
				try{
					Thread.sleep(1);
				
				}catch(InterruptedException e) {}
				
			} while(System.currentTimeMillis()<t+43);
									
		}		
	}
	
	public void setState(int s)
	{
		state=s; cnt=0;
	}
	
	private void gameUpdate()
	{
									
		cnt++;
		
		try{
						
		// update of the game
		
		switch(state) {
		
			
			case INITIALLOAD :
			if(cnt==3){			
				gameCanvas.initialLoad();	
				setState(LOGO);
			}
			break;
		
			case LOGO :			
			if(cnt>=90){ 
				setState(MAIN_MENU);
				gameCanvas.destroyLogo(); 			
			}
			break;
								
			case MAIN_MENU :
			if(cnt==1) {gameCanvas.playSound(0,1);}
			if(gameCanvas.left) {gameCanvas.controlReset(); option--; if(option<0) option=gameCanvas.mainMenu.length-1; }
			if(gameCanvas.right) {gameCanvas.controlReset(); option=(option+1)%gameCanvas.mainMenu.length; }
			if(gameCanvas.anybut){
				gameCanvas.controlReset();
				switch(gameCanvas.mainMenu[option]){
				
					case PLAY : gameCanvas.destroyPicture(); level=lastLev; setState((lastLev==0 ? LOADING : LEVEL_SELECT)); break;				
					case SOUND : sound = !sound; if(!sound) gameCanvas.soundStop(); else gameCanvas.playSound(0,1); break;				
					case HELP : setState(CONTROLS); break;				
					case VIBR : vibr = !vibr; if(vibr) {gameCanvas.vibrate(50,100); gameCanvas.playSound(0,1);} break;				
					case KEYCONF : keyConf = !keyConf; break;				
					case ABOUT : setState(CREDITS); break;				
					case QUIT : destroyApp(false); break;				
				}			
			}
			break;
			
			case LEVEL_SELECT :
			if(gameCanvas.left) {gameCanvas.controlReset(); if(level>0) level--;}
			if(gameCanvas.right) {gameCanvas.controlReset(); if(level<lastLev) level++; }
			if(gameCanvas.anybut){
				if(level==0)
					for(int i=0; i<helpDone.length; i++) helpDone[i]=false;
				gameCanvas.controlReset();
				setState(LOADING);
			}
			break;
			
			case LOADING :
			if(cnt==3){
				bo=null;				
				world=new BeastWorld(level);		
				gameCanvas.loadLevelGfx();				
				pl=new BeastPlayer(40,-24,world);		
				for(int i=0; i<level; i++)
					pl.modifyPower(+pickedGems[i]);
				en=new BeastEnemy[2];	
				if(level==7) bo=new BeastEnemy(362,94,world,BeastEnemy.BOSS);
				gameCanvas.resetScroll();						
				setState(LEVEL_LOAD);	
			}					
			break;
						
			case LEVEL_LOAD :					
			//	{gameCanvas.controlReset(); setState(EXPLAIN);}
			if(gameCanvas.anybut) 
				{gameCanvas.controlReset(); gameCanvas.soundStop(); setState(GAME_ACTION);}
			break;
			
			case EXPLAIN :							 			
			if(gameCanvas.but1){
				gameCanvas.controlReset();
				setState(GAME_ACTION);
			}
			break;
						
			case GAME_ACTION :						
			updateGameAction();
			//#ifdef FRAMESKIP
			//updateGameAction();
			//#endif
			if(pl.helpRequest>=0 && !helpDone[pl.helpRequest]) {
					
					gameCanvas.controlReset();
					helpType=pl.helpRequest; helpDone[helpType] = true;
					setState(EXPLAIN); 
			}
			if(gameCanvas.but2) {gameCanvas.controlReset(); setState(IN_GAME_MENU);}
			needMore = false;
			if(pl.outOfLevel && level!=7){				
				if(pl.gems>=world.minimum[level])
					{gameCanvas.controlReset(); setState(LEVEL_ENDED);}	
				else needMore=true;
			}
			if(level==7)
			if(bo.outOfWorld) {
				gameCanvas.destroyLevelGfx();
				setState(GAME_END);
			}
			
			//if(gameCanvas.but2) {gameCanvas.controlReset(); option=0; setState(IN_GAME_MENU);}
			
			if(pl.lifes<0) setState(GAME_OVER);
			break;
			
			case IN_GAME_MENU :
			if(gameCanvas.left) {gameCanvas.controlReset(); option--; if(option<0) option=gameCanvas.inGameMenu.length-1; }
			if(gameCanvas.right) {gameCanvas.controlReset(); option=(option+1)%gameCanvas.inGameMenu.length; }
			if(gameCanvas.but1){
				gameCanvas.controlReset();
				switch(gameCanvas.inGameMenu[option]){
				
					case SOUND : sound = !sound; if(!sound) gameCanvas.soundStop(); break;				
					case VIBR : vibr = !vibr; if(vibr) gameCanvas.vibrate(50,100); break;				
					case HELP : setState(CONTROLS2); break;
					case RESUME : setState(GAME_ACTION); break;				
					case ABORT : gameCanvas.destroyLevelGfx(); gameCanvas.loadPicture(); setState(MAIN_MENU); option=0; break;				
					case KEYCONF : keyConf = !keyConf; break;									
					case QUIT : destroyApp(false); break;				
				}			
			}
			if(gameCanvas.but1){
				gameCanvas.controlReset();
				setState(GAME_ACTION);
			}
			break;			
			
			case GAME_OVER :									
			if(cnt==1) gameCanvas.playSound(2,1);							 						
			if(gameCanvas.anybut) {
				gameCanvas.destroyLevelGfx(); 
				gameCanvas.loadPicture();
				setState(MAIN_MENU); 
				option=0;
				saveData();
				gameCanvas.controlReset(); 
			}
			break;
			
			case LEVEL_ENDED :
			if(cnt==1) gameCanvas.playSound(1,1);							 						
			if(gameCanvas.anybut) {
				world = null;
				gameCanvas.destroyLevelGfx();
				if(pl.gems>pickedGems[level]) pickedGems[level]=(short) pl.gems;
				level++; if(lastLev<level) lastLev=level;				
				saveData();
				gameCanvas.controlReset();
				setState(LOADING);				
			}
			break;
			
			case CONTROLS :									
			if(gameCanvas.anybut) {				
				setState(MAIN_MENU); 
				option=0;
				gameCanvas.controlReset(); 
			}
			break;
			
			case CONTROLS2 :									
			if(gameCanvas.anybut) {				
				setState(IN_GAME_MENU); 
				option=0;
				gameCanvas.controlReset(); 
			}
			break;
			
			case CREDITS :									
			if(gameCanvas.anybut || cnt > 300) {				
				setState(MAIN_MENU); 				
				gameCanvas.controlReset(); 
			}
			break;
			
			case GAME_END :									
			if(gameCanvas.anybut || cnt > 600) {
				if(pl.gems>pickedGems[level]) pickedGems[level]=(short) pl.gems;				
				saveData();
				gameCanvas.loadPicture();				
				setState(CREDITS); 				
				gameCanvas.controlReset(); 
			}
			break;
								
		}	
		
		//#ifdef CLISTENER
		if(gameCanvas.listenerKey){		
			gameCanvas.but1 = false;
			gameCanvas.but2 = false;
			gameCanvas.listenerKey = false;
		}
		//#endif
		
		}catch (Exception e)
		{
			e.printStackTrace();
			e.toString(); 				
		}
		
	}	
	
	public void updateGameAction()
	{
			int distX, distY;	
			
			pl.update(gameCanvas);			 
			pl.nearestEnemy = null; 
			
			for(int i=0; i<en.length; i++)
				if(en[i]!=null){					
					if(en[i].outOfWorld) {en[i]=null;}
					else{
					 	en[i].update(pl);
					 	
					 	// Collide with player
					 	//System.out.println("("+Math.abs(en[i].x-pl.x)+","+Math.abs(en[i].y-pl.y)+")"+en[i].type);
					 	distX = Math.abs(en[i].x-pl.x);
					 	distY = Math.abs(en[i].y-pl.y);
					 	if(en[i].state!=BeastEnemy.DEAD)
					 	if(distX<24 && distY<20)					 	
					 		switch(en[i].type){
					 			case BeastEnemy.MATE :
					 			if(pl.state==BeastPlayer.FALL){
					 		
					 				en[i].outOfWorld = true;	
					 				pl.mateLife=50;
					 				pl.setState(BeastPlayer.RIDE_STAND);
					 			}
					 			break;
					 			
					 			case BeastEnemy.DINO :
					 			if(pl.body.collide(en[i].body))
					 			if(pl.state!=BeastPlayer.DEAD){
					 				en[i].outOfWorld = true;	
					 				pl.modifyLife(en[i].x,en[i].y,+20);
					 				world.addEffect(en[i].x,en[i].y,BeastShot.DINO_EAT);					 				
					 			}
					 			break;
					 								 			
					 			default : 					 							 			
					 			// Jump over the enemies					 			
					 			if(pl.state==BeastPlayer.FALL || pl.state==BeastPlayer.RIDE_FALL){
					 				en[i].modifyLife(pl.x,pl.y,-40);					 			
					 				pl.vely=-BeastPlayer.JUMP_IMPULSE; pl.setState((pl.state==BeastPlayer.FALL ? BeastPlayer.JUMP : BeastPlayer.RIDE_JUMP));
					 				BeastCanvas.vibrate(100,100);
					 			} else
					 				pl.nearestEnemy = en[i];
					 			break;					 							 							 		
					 		} 
					 	
					 	// Enemy too far
					 	//#ifdef GFXHUGE
					 	//if(Math.abs(en[i].x-pl.x)>300 || Math.abs(en[i].y-pl.y)>300) en[i].outOfWorld=true;
					 	//#else
					 	if(Math.abs(en[i].x-pl.x)>200 || Math.abs(en[i].y-pl.y)>200) en[i].outOfWorld=true;
					 	//#endif
					}
					
				}else{
				
					en[i]=new BeastEnemy(pl.x+(Math.abs(rnd.nextInt())%2==0 ? -150 : +150),pl.y+rnd.nextInt()%40,world,levelEnemies[world.level][(Math.abs(rnd.nextInt())%(levelEnemies[world.level].length))]);
				}
				
			if(bo!=null) bo.updateBoss(pl);
			
			world.update();		
	}	
		
	public void loadData()
	{
		byte[] BeastData=null;	
		RecordStore rs;
		 					
		try {
			rs = RecordStore.openRecordStore(BeastDataName, true);
	
			if (rs.getNumRecords() == 0){
												
				//System.out.println("Warning! Default setting...");
								
				sound=true;
				vibr=true;
				lastLev=0;
				for(int i=0; i<helpDone.length; i++) helpDone[i] = false;
				for(int i=0; i<pickedGems.length; i++) pickedGems[i] = 0;
												
				ByteArrayOutputStream bstream = new ByteArrayOutputStream();
                DataOutputStream ostream = new DataOutputStream(bstream);				
								
			    
			    ostream.writeBoolean(sound);
  				ostream.writeBoolean(vibr);
  				ostream.writeBoolean(keyConf);
  				ostream.writeInt(lastLev);
				for(int i=0; i<helpDone.length; i++)
					ostream.writeBoolean(helpDone[i]);
				for(int i=0; i<pickedGems.length; i++)
					ostream.writeShort(pickedGems[i]);
  													
	            ostream.flush();
	            ostream.close();
	                					              
	            BeastData = bstream.toByteArray();
	            rs.addRecord(BeastData, 0, BeastData.length);							
														                																					
			} else {
				
				//System.out.println("Retrieve settings...");
											
				BeastData = rs.getRecord(1);
								
                DataInputStream istream = new DataInputStream(new ByteArrayInputStream(BeastData, 0, BeastData.length));				
                    		
			    sound=istream.readBoolean();
			  	vibr=istream.readBoolean();
			  	keyConf=istream.readBoolean();
			  	lastLev=istream.readInt();
				for(int i=0; i<helpDone.length; i++)
					helpDone[i]=istream.readBoolean();
				for(int i=0; i<pickedGems.length; i++)
					pickedGems[i]=(short) istream.readShort();
			  	
	            		                    		                    												
			}
			rs.closeRecordStore();
			
		} catch(Exception e) {/*System.out.println("EXCP");*/}
			
	}
																																																															
	public void saveData()
	{
		byte[] BeastData=null;	
		RecordStore rs=null;		
		 					
		try {
			rs = RecordStore.openRecordStore(BeastDataName, true);
					
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
            DataOutputStream ostream = new DataOutputStream(bstream);				
											
			ostream.writeBoolean(sound);
			ostream.writeBoolean(vibr);
			ostream.writeBoolean(keyConf);
			ostream.writeInt(lastLev);
			for(int i=0; i<helpDone.length; i++)
				ostream.writeBoolean(helpDone[i]);
			for(int i=0; i<pickedGems.length; i++)
					ostream.writeShort(pickedGems[i]);
							
      		ostream.flush();
            ostream.close();
                	
            BeastData = bstream.toByteArray();
	                	
            rs.setRecord(1, BeastData, 0, BeastData.length);						                																								
			
			                        					              			
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
	
}
	
