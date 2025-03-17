/*
	"QBlast!" Coded by David Rodriguez 2003
*/

package com.mygdx.mongojocs.qblast;

import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
//import com.mygdx.mongojocs.midletemu.Runnable;
//import com.mygdx.mongojocs.midletemu.Thread;
import com.mygdx.mongojocs.midletemu.TextField;


import java.util.Random;
import java.io.InputStream;
import java.io.*;


public class QBlastMain extends MIDlet implements Runnable
{
		
	public static QBlastCanvas gameCanvas;
	public static Display display;
	public static Form scoreForm;
	public static TextField scoreField;
	public Thread thread;
	public static String GameDataName="QBlastData";
	
	
	
	public static final int MAIN_MENU=0;
	public static final int NEXT_LEVEL=1;
	public static final int GAME_ACTION=2;
	public static final int LEVEL_COMPLETE=3;
	public static final int GAME_OVER=4;
	public static final int TITLE=5;
	public static final int CHOOSE_LEVEL=6;
	public static final int INGAME_MENU=7;
	public static final int HELP=8;
	public static final int HELP2=9;
	public static final int ABOUT=10;
	public static final int SHOW_TIP=11;
	public static final int END_GAME=12;
	public static final int LOGO=13;	
	
	
	public static int state, cnt, leveln, clock, lastLevel=0, levelTimes[]=new int[50], boxX1,  boxX2,  boxY1,  boxY2,  boxZ1,  boxZ2;		
	public static boolean vibr=false, sound=true, doUpdate=true;	
	public static Random rnd;
	
	public QBlastLevel lev;
	public QBlastPlayer pl;
	public QBlastBomb bo[];
	public QBlastExplosion ex[];
			
	public int ori=30, rotateCount=0, menuOp=0;
		
		
	public QBlastMain()
	{
		gameCanvas = new QBlastCanvas(this);
		display = Display.getDisplay(this);
		InputStream is;
						
		// Form for the record name entry				
						
		/*Command doneCommand = new Command(com.mygdx.mongojocs.bravewar.Text.done, Command.OK, 1);
		
		scoreForm = new Form(com.mygdx.mongojocs.bravewar.Text.new_record);
        	scoreField = new TextField(com.mygdx.mongojocs.bravewar.Text.type_name, "",3,TextField.ANY);
		scoreField.setMaxSize(3);
        	scoreForm.append(scoreField);   
		scoreForm.addCommand(doneCommand);
        	scoreForm.setCommandListener(this);
    		System.gc();*/
												
		rnd=new Random();

		loadData();		
			
		set_state(LOGO);
		
		thread=new Thread(this);
		thread.start();
		
		display.setCurrent(gameCanvas); doUpdate=true;
				
		System.gc();
			
	}
	
	public void startApp()
	{		
				
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
		doUpdate=false;
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

	}

	public void runTick()
	{
		long t;

			t=System.currentTimeMillis();

			try{

				if(doUpdate)
					game_update();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}

			//System.gc();

			gameCanvas.repaint();

			if(gameCanvas.painting==false)
				gameCanvas.serviceRepaints();

			do{
				try{
					Thread.sleep(1);

				}catch(InterruptedException e) {}

			} while(System.currentTimeMillis()<t+75);

	}

	public void runEnd()
	{

	}

	public void run()
	{	
		long t;			
														
		while (thread!=null){
			
			t=System.currentTimeMillis();
			
			try{			
				
				if(doUpdate)																	
					game_update();
							
			}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}
			
			//System.gc();

			gameCanvas.repaint();									 		  			 				 								 																								
			
			if(gameCanvas.painting==false)
				gameCanvas.serviceRepaints();																								
			
			do{
				try{
					Thread.sleep(1);
				
				}catch(InterruptedException e) {}
				
			} while(System.currentTimeMillis()<t+75);
									
		}
		t = 99;
	}
	
	public void set_state(int s)
	{
		state=s; cnt=0;
	}
	
	private void game_update(){
		
		int VEL_STEP=30;
				
		cnt++;
						
		// update of the game
		
		switch(state) {
			
			case LOGO :			
			if(cnt>40) {gameCanvas.destroyLogo(); set_state(TITLE);}
			break; 			
			
			case TITLE :
			if(cnt==1) QBlastCanvas.playMusic(0,1);
			if(gameCanvas.anybut) {gameCanvas.controlReset(); gameCanvas.destroyPicture(); menuOp=0; set_state(MAIN_MENU);}
			break;
			
			case MAIN_MENU :
			if(gameCanvas.down) {gameCanvas.controlReset(); menuOp++;}
			if(gameCanvas.up) {gameCanvas.controlReset(); menuOp--;}
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				switch(menuOp){
					case 0 : leveln=lastLevel; set_state(CHOOSE_LEVEL); break;
					case 1 : if (sound) {gameCanvas.playSound(-1); sound=false; } else sound=true; break;			
					case 2 : if (vibr) vibr=false; else vibr=true; break;			
					case 3 : set_state(HELP); break;
					case 4 : set_state(ABOUT); break;
					case 5 : destroyApp(false);
				}
			}
			menuOp=(6+menuOp)%6;
			break;
			
			case INGAME_MENU :
			if(gameCanvas.down) {gameCanvas.controlReset(); menuOp++;}
			if(gameCanvas.up) {gameCanvas.controlReset(); menuOp--;}
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				switch(menuOp){					
					case 0 : set_state(GAME_ACTION); break;
					case 1 : if (sound) sound=false; else sound=true; break;								
					case 2 : if (vibr) vibr=false; else vibr=true; break;			
					case 3 : set_state(HELP2); break;
					case 4 : menuOp=0; set_state(MAIN_MENU); break;
				}
			}
			menuOp=(5+menuOp)%5;
			break;			
	
			case ABOUT :		
			case HELP :
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				set_state(MAIN_MENU);
			}			
			break;
			
			case HELP2 :
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				set_state(INGAME_MENU);
			}			
			break;
			
			
			case CHOOSE_LEVEL :
			if(gameCanvas.right && leveln<lastLevel) {gameCanvas.controlReset(); leveln++;}
			if(gameCanvas.left && leveln>0) {gameCanvas.controlReset(); leveln--;}
			if(gameCanvas.but1) {
				clock=levelTimes[leveln]*13; 
				gameCanvas.controlReset(); 
				//leveln=18; clock=120*13; 
				set_state(NEXT_LEVEL);
				}
			break;
			
			case NEXT_LEVEL :
			if(cnt>20){
				lev=null; pl=null; bo=null; ex=null;
				gameCanvas.loadTiles(leveln/5);
						
				System.gc();
			
				lev=new QBlastLevel(leveln);
				pl=new QBlastPlayer(lev,lev.begX,lev.begY,lev.begZ);
		
				bo=new QBlastBomb[3]; for(int i=0; i<bo.length; i++) bo[i]=new QBlastBomb(lev);
				ex=new QBlastExplosion[4]; for(int i=0; i<ex.length; i++) ex[i]=new QBlastExplosion(lev);														
							
				if(QBlastText.tips[leveln]==null)
					set_state(GAME_ACTION);
				else set_state(SHOW_TIP);
			}
			break;
						
			case SHOW_TIP :
			if(gameCanvas.anybut) {
				gameCanvas.controlReset(); 
				set_state(GAME_ACTION);
			}			
			break;
				
				
			case GAME_ACTION :			
			//if(gameCanvas.but4) {gameCanvas.controlReset(); set_state(LEVEL_COMPLETE); break; }
			if(clock>0) clock--;
			else set_state(GAME_OVER);
			
			lev.update();
			
			pl.update();
			pl.explosionInteract(ex);
			
			for(int i=0; i<lev.numLifts; i++){
				lev.li[i].update();				
			}
						
			for(int i=0; i<lev.numEnemies; i++){
				lev.en[i].update();
				lev.en[i].playerInteract(pl);
				lev.en[i].explosionInteract(ex);
			}
			
			for(int i=0; i<bo.length; i++)
				if(bo[i].active){
					 bo[i].update();
					 if (bo[i].explodes()) { setAExplosion(bo[i].x,bo[i].y,bo[i].z);}
				}
							
			for(int i=0; i<ex.length; i++)
				if(ex[i].active) ex[i].update();				
				
			if(pl.state==QBlastPlayer.BROKEN)
			for(int i=0; i<lev.pieces.length; i++){				
				lev.pieces[i].update();
				lev.piecesPos[i]+=(40-pl.cnt);
			}
			
			if(gameCanvas.but2 && rotateCount==0) rotateCount=+4;
			if(gameCanvas.but3 && rotateCount==0) rotateCount=-4; 
			
			if(gameCanvas.but4) {gameCanvas.controlReset(); menuOp=0; set_state(INGAME_MENU);}
			
			if(pl.state==QBlastPlayer.PLAYING && pl.radioactive==0){
			
				if(gameCanvas.but1 && lev.activeBombs<pl.maxBombs) {gameCanvas.controlReset(); setABomb(pl.realx, pl.realy, pl.realz);}
			
				if(rotateCount<0) {gameCanvas.controlReset(); ori=(ori+QBlastCanvas.ROTATE_STEP)%32; rotateCount+=QBlastCanvas.ROTATE_STEP; }
				if(rotateCount>0) {gameCanvas.controlReset(); ori=(32+(ori-QBlastCanvas.ROTATE_STEP))%32; rotateCount-=QBlastCanvas.ROTATE_STEP; }
			
				if(pl.cnt>=15 && !pl.falling)
				switch(ori){				
					case 30 :
					//case 28 :
					case 26 :
					//case 24 :
					if(gameCanvas.right) pl.velx+=VEL_STEP;
					if(gameCanvas.left) pl.velx-=VEL_STEP;
					if(gameCanvas.down) pl.velz+=VEL_STEP;
					if(gameCanvas.up) pl.velz-=VEL_STEP;
					break;
					
					case 22 :
					//case 20 :
					case 18 :
					//case 16 :
					if(gameCanvas.right) pl.velz+=VEL_STEP;
					if(gameCanvas.left) pl.velz-=VEL_STEP;
					if(gameCanvas.down) pl.velx-=VEL_STEP;
					if(gameCanvas.up) pl.velx+=VEL_STEP;
					break;				
					
					case 14 :
					//case 12 :
					case 10 :
					//case 8  :
					if(gameCanvas.right) pl.velx-=VEL_STEP;
					if(gameCanvas.left) pl.velx+=VEL_STEP;
					if(gameCanvas.down) pl.velz-=VEL_STEP;
					if(gameCanvas.up) pl.velz+=VEL_STEP;
					break;				
					
					case 6 :
					//case 4 :
					case 2 :
					//case 0 :
					if(gameCanvas.right) pl.velz-=VEL_STEP;
					if(gameCanvas.left) pl.velz+=VEL_STEP;
					if(gameCanvas.down) pl.velx+=VEL_STEP;
					if(gameCanvas.up) pl.velx-=VEL_STEP;
					break;				
								
				}
			}
						
			if(pl.state==QBlastPlayer.DEAD){
					 pl.create(lev,lev.begX,lev.begY,lev.begZ);
					 pl.setState(QBlastPlayer.PLAYING);
					 pl.invulnerable=70;
				}
				
			if(pl.state==QBlastPlayer.PLAYING && pl.x==lev.endX && pl.y==lev.endY && pl.z==lev.endZ) {gameCanvas.controlReset(); set_state(LEVEL_COMPLETE);};			
			break;				
			
			case LEVEL_COMPLETE :
			if(cnt==1) QBlastCanvas.playMusic(1,1);
			if(gameCanvas.anybut) { 
				gameCanvas.controlReset(); 
				leveln++; 
				if(leveln==25){
					gameCanvas.destroyTiles();
					gameCanvas.loadEndPic();
					set_state(END_GAME);				
				}else{										
					clock+=45*13;
					if(leveln>lastLevel) lastLevel=leveln;
					if(clock/13>levelTimes[leveln]) levelTimes[leveln]=clock/13;
					saveData();
					set_state(NEXT_LEVEL);
				}
			}
			break;
			
			case GAME_OVER :
			if(cnt==1) QBlastCanvas.playMusic(2,1);
			if(gameCanvas.anybut) {gameCanvas.controlReset(); menuOp=0; set_state(MAIN_MENU);}
			break;
			
			case END_GAME :
			if(cnt==1) gameCanvas.playMusic(1,1);
			if(gameCanvas.anybut) {gameCanvas.controlReset(); set_state(ABOUT); gameCanvas.destroyPicture();}
			break;
		}	
		
	}		
	
	public void setABomb(int rx, int ry, int rz)
	{
		int i=0;
		
		while(i<bo.length && bo[i].active)
			i++;
			
		if(i<bo.length)
			bo[i].set(rx, ry, rz);		
	}
	
	public void setAExplosion(int rx, int ry, int rz)
	{
		int i=0;
		
		while(i<ex.length && ex[i].active)
			i++;
			
		if(i<ex.length){
			ex[i].set(rx, ry, rz, pl.explPower);		
			gameCanvas.vibrate(100,50);
			QBlastCanvas.playSound(0);
		}
	}	
	
	public void loadData()
	{
		byte[] GameData=null;	
		RecordStore rs;
		 					
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
	
			if (rs.getNumRecords() == 0){
												
				//System.out.println("Warning! Default setting...");
				
				levelTimes[0]=45;
				for(int i=1; i<levelTimes.length; i++)
					levelTimes[i]=0;
				sound=true; vibr=false; lastLevel=0;
												
				ByteArrayOutputStream bstream = new ByteArrayOutputStream();
                DataOutputStream ostream = new DataOutputStream(bstream);				
								
			    
			    ostream.writeBoolean(sound);
			    ostream.writeInt(lastLevel);
			    for(int i=0; i<levelTimes.length; i++)
			    	ostream.writeInt(levelTimes[i]);
									
	            ostream.flush();
	            ostream.close();
	                					              
	            GameData = bstream.toByteArray();
	            rs.addRecord(GameData, 0, GameData.length);									
														                																					
			} else {
				
				//System.out.println("Retrieve settings...");
											
				GameData = rs.getRecord(1);
								
                DataInputStream istream = new DataInputStream(new ByteArrayInputStream(GameData, 0, GameData.length));				
                    		
			    sound=istream.readBoolean();
			    lastLevel=istream.readInt();
			    for(int i=0; i<levelTimes.length; i++)
			    	levelTimes[i]=istream.readInt();
                    		                    		                    												
			}
			rs.closeRecordStore();
			
		} catch(Exception e) {
			System.out.println("EXCP");
			levelTimes[0]=45;
			for(int i=1; i<levelTimes.length; i++)
				levelTimes[i]=0;
			sound=true; vibr=false; lastLevel=0;
		}
			
	}
																																																															
	public void saveData()
	{
		byte[] GameData=null;	
		RecordStore rs=null;		
		 					
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
					
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
            DataOutputStream ostream = new DataOutputStream(bstream);				
											
			ostream.writeBoolean(sound);
			ostream.writeInt(lastLevel);
			for(int i=0; i<levelTimes.length; i++)
			  	ostream.writeInt(levelTimes[i]);			
			    	
      		ostream.flush();
            ostream.close();
                	
            GameData = bstream.toByteArray();
	                	
            rs.setRecord(1, GameData, 0, GameData.length);						                																								
			
			                        					              			
		} catch(Exception e) {System.out.println("Exception in saving");}
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
	
