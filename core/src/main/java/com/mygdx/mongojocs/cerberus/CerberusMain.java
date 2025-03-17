/*
	"Cerberus Lair" Coded by David Rodriguez 2003
*/

package com.mygdx.mongojocs.cerberus;

import java.util.Random;
import java.io.InputStream;
import java.io.*;


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


public class CerberusMain extends MIDlet implements Runnable, CommandListener
{
		
	static CerberusCanvas pant;
	public static Display display;
	public static Form scoreForm;
	public static TextField scoreField;
	public Thread thread;
	public static String GameDataName="CerberusData";
	
	
	public static final int MAIN_MENU=0;
	public static final int LOAD_DATA=1;
	public static final int GAME_ACTION=2;
	public static final int LOST_LIFE=3;
	public static final int PRESENTS=4;	
	public static final int CREATE_WORLD=5;
	public static final int CHOOSE_WEAPON=6;
	public static final int CHOOSE_LEVEL=7;
	public static final int GAME_OVER=8;
	public static final int SUBLEVEL_END=9;
	public static final int LEVEL_END=10;
	public static final int SAVE_GAME=11;
	public static final int LOAD_SAVED=12;
	public static final int IN_GAME_MENU=13;
	public static final int INTRO=14;
	public static final int CONTROLS=15;
	public static final int CONTROLS2=16;
	public static final int CREDITS=17;
	public static final int LOAD_GFX=18;
	public static final int END_GAME=19;
	public static final int EPILOGUE=20;
	public static final int RANK=21;
	public static final int CHEAT=22;
	public static final int STORY=23;
	public static final int WAIT=99;
	
	
	public static int state, cnt, px=0, level, sublevel, lifes, menu_opt, weapon, save_slot, menu_item=0, dead_times=0;		
	public static boolean vibr=false, sound=true, boss_area, secondhalf;
	public static boolean helmet, armor, gauntlets, boots, cape, cleared[]={false,false,false};
	public static Random rnd;
	public SavedGame sv[];
	
	CerberusWorld world;
	
	public void startApp()
	{
		pant = new CerberusCanvas(this);
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
								
		display.setCurrent(pant);
				
		rnd=new Random();
					
		set_state(PRESENTS);
		
		thread=new Thread(this);
		thread.start();

		System.gc();
				
		sv=new SavedGame[3]; for(int i=0; i<3; i++) sv[i]=new SavedGame();
		save_slot=0;
		
		/*sv[0].used=true; 
		sv[0].helmet=true;
		sv[0].armor=true;
		sv[0].cape=true;
		sv[0].gauntlets=true;
		sv[0].boots=true;
		sv[0].sublevel=1;
		sv[0].level=2;
		sv[0].lifes=3;
		sv[0].levels[0]=true;
		sv[0].levels[1]=true;
		sv[0].levels[2]=true;*/

		load_data();		
	}
	
	public void commandAction (Command c, Displayable d)
	{
	 /*      String label = c.getLabel();
	       if (label == com.mygdx.mongojocs.bravewar.Text.done ) {
	      		display.setCurrent(pant);
	      		pant.ctrl.reset();
	      		//set_state(NEW_RECORD_OK);	      	
	       } 	 	*/
	}	
	
	protected void pauseApp()
	{
		
	}
	
	protected void destroyApp(boolean unconditional)
	{
		/*display.setCurrent(null);
		pant=null; */
		thread = null;
		save_data();		
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

			//try{

			game_update();

			pant.repaint();

			/*}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}

			//System.gc();*/



			if(pant.painting==false)
				pant.serviceRepaints();

			do{
				try{
					Thread.sleep(1);

				}catch(InterruptedException e) {}

			} while(System.currentTimeMillis()<t+50);

	}

	public void runEnd()
	{

	}

	@Override
	public void run()
	{	
		long t;			
														
		while (thread!=null){
			
			t=System.currentTimeMillis();
			
			//try{			
																	
				game_update();
				
				pant.repaint();									 		  			 				 								 																								
							
			/*}
			catch (Exception e)
			{
				e.printStackTrace();
				e.toString();
			}
			
			//System.gc();*/

			
			
			if(pant.painting==false)
				pant.serviceRepaints();																								
			
			do{
				try{
					Thread.sleep(10);
				
				}catch(InterruptedException e) {}
				
			} while(System.currentTimeMillis()<t+50);
									
		}
	}
	
	public void set_state(int s)
	{
		state=s; cnt=0;	menu_opt=0;
	}
	
	private void game_update(){
				
		cnt++;
				
		// update of the game
		
		switch(state) {
			
			case PRESENTS :
			if(cnt>20){
				pant.load_intro_gfx();
				pant.play_music(0,1);
				set_state(INTRO);
				
			}
			break;
			
			case INTRO :
			if(cnt>100 || pant.ctrl.anybut) {pant.ctrl.reset(); set_state(LOAD_GFX);}
			break;
			
			case LOAD_GFX :
			if(cnt>1){
				pant.load_all_gfx();
				set_state(MAIN_MENU);
			}
			break;

			case MAIN_MENU :
			if(pant.ctrl.right) { menu_opt=(menu_opt+1)%6; pant.ctrl.reset();}			
			if(pant.ctrl.left) { menu_opt=(6+menu_opt-1)%6; pant.ctrl.reset();}			
			if(pant.ctrl.anybut){
				pant.ctrl.reset();
				switch(menu_opt){
					
					case 1 : weapon=0; level=0; sublevel=0; lifes=2; helmet=false; armor=false; gauntlets=false; boots=false; cape=false; boss_area=false; cleared[0]=false; cleared[1]=false; cleared[2]=false; 
						 if(dead_times>=666) set_state(CHEAT); else set_state(STORY); break;	
					case 0 : set_state(LOAD_SAVED); break;
					case 2 : if(sound) sound=false; else sound=true; break;
					case 3 : set_state(CONTROLS); break;
					case 4 : set_state(CREDITS); break;
					case 5 : destroyApp(false); 
				}					
			}
			break;
			
			case CHEAT :
			if(pant.ctrl.down) { menu_opt=1; pant.ctrl.reset();}			
			if(pant.ctrl.up) { menu_opt=0; pant.ctrl.reset();}						
			if(pant.ctrl.anybut){
				if(menu_opt==1){
					helmet=true; armor=true; gauntlets=true; boots=true; cape=true;
				}
				pant.ctrl.reset();
				set_state(CHOOSE_WEAPON);
			} 			
			break;
			
			case STORY :
			if(cnt==1) pant.play_music(1,2);
			if(pant.ctrl.anybut || cnt>=400){
				pant.ctrl.reset();
				set_state(CHOOSE_WEAPON);
			} 			
			break;
			
			case CHOOSE_WEAPON :
			if(pant.ctrl.down) { weapon=(weapon+1)%3; pant.ctrl.reset();}			
			if(pant.ctrl.up) { weapon=(3+weapon-1)%3; pant.ctrl.reset();}						
			if(pant.ctrl.anybut){
				pant.ctrl.reset();
				set_state(CHOOSE_LEVEL);
			} 
			break;
			
			case CHOOSE_LEVEL :									
			if(cleared[0] && cleared[1] && cleared[2])
				level=3;
			else{
				if(pant.ctrl.left) { level=(3+level-1)%3; pant.ctrl.reset();}						
				if(pant.ctrl.right) { level=(3+level+1)%3; pant.ctrl.reset();}						
			}
			if(pant.ctrl.anybut) 
				if((level<3 && !cleared[level]) || (level==3  && cleared[0] && cleared[1] && cleared[2])){
				pant.ctrl.reset();
				sublevel=0; 				
				set_state(LOAD_DATA);
			} 
			break;
						
			case LOAD_DATA :
			if(cnt>30){
				pant.load_world_gfx(level, sublevel); secondhalf=false; boss_area=false; pant.ctrl.reset(); set_state(CREATE_WORLD);
			}
			break;
			
			case CREATE_WORLD :
			//world.pl=null; world.en=null; world.boss=null; world.TileMap=null;
			//System.gc(); world=null; System.gc();
			world=new CerberusWorld(level, sublevel, secondhalf, boss_area);
			world.pl.weapon=weapon;
			world.pl.helmet=helmet;
			world.pl.armor=armor;
			world.pl.gauntlets=gauntlets;
			world.pl.boots=boots;
			world.pl.cape=cape;			
			world.pl.energy=world.pl.max_energy();
			
			//pant.set_scroll(world);
			set_state(GAME_ACTION);
			break;
			
			case GAME_ACTION :
			if(cnt==2) pant.play_music(2,1);
			world.update(pant);
			if(sublevel==1 && world.at_boss_area() && !boss_area) {boss_area=true;}
			if(world.at_second_half()) secondhalf=true;
			if(pant.ctrl.but3) {pant.ctrl.reset(); set_state(IN_GAME_MENU);}
			if(world.pl.state==CerberusSamuel.DEATH) {set_state(LOST_LIFE);}
			if(sublevel==0 && world.level_finished() && world.pl.state!=CerberusSamuel.FALL && world.pl.state!=CerberusSamuel.FALL2 && world.pl.state!=CerberusSamuel.JUMP2) 
				{world.pl.set_state(CerberusSamuel.WON); set_state(SUBLEVEL_END);}
			if(sublevel==1 && world.boss.out_of_world  && world.pl.state!=CerberusSamuel.FALL && level!=3) 
				{world.pl.set_state(CerberusSamuel.WON); set_state(LEVEL_END);}			
			if(sublevel==1 && world.boss.dead && level==3) 
				set_state(END_GAME);
			break;
			
			case SUBLEVEL_END :			
			world.update(pant);
			if(cnt>60){
				weapon=world.pl.weapon;				
				helmet=world.pl.helmet;
				armor=world.pl.armor;
				gauntlets=world.pl.gauntlets;
				boots=world.pl.boots;
				cape=world.pl.cape;				
				sublevel=1;
				set_state(SAVE_GAME);
			}
			break;
			
			case LEVEL_END :			
			if(cnt==1) pant.play_music(3,1);
			world.update(pant);
			if(cnt>70){
				weapon=world.pl.weapon;				
				helmet=world.pl.helmet;
				armor=world.pl.armor;
				gauntlets=world.pl.gauntlets;
				boots=world.pl.boots;
				cape=world.pl.cape;								
				sublevel=0;
				boss_area=false;
				cleared[level]=true;
				set_state(SAVE_GAME);
			}
			break;
						
			case LOST_LIFE :	
			if(cnt==2) pant.play_music(4,1);
			world.update(pant);		
			if(cnt>40){
				world=null; System.gc();
				lifes--;
				if(lifes<0) {pant.destroy_world_gfx(); set_state(GAME_OVER); }
				else set_state(CREATE_WORLD);
			}
			break;
			
			case GAME_OVER :
			if(cnt==1) dead_times++;
			if(cnt>90 || pant.ctrl.anybut){
				pant.ctrl.reset();
				set_state(MAIN_MENU);	
			}
			break;
			
			case SAVE_GAME :			
			if(pant.ctrl.right) { save_slot=(save_slot+1)%3; pant.ctrl.reset();}			
			if(pant.ctrl.left) { save_slot=(3+save_slot-1)%3; pant.ctrl.reset();}									
			if(pant.ctrl.but1) {
				pant.ctrl.reset();												
				sv[save_slot].level=level;
				sv[save_slot].sublevel=sublevel;
				sv[save_slot].lifes=lifes;
				sv[save_slot].weapon=weapon;
				sv[save_slot].levels[0]=cleared[0];
				sv[save_slot].levels[1]=cleared[1];
				sv[save_slot].levels[2]=cleared[2];
				sv[save_slot].helmet=helmet;
				sv[save_slot].armor=armor;
				sv[save_slot].gauntlets=gauntlets;
				sv[save_slot].boots=boots;
				sv[save_slot].cape=cape;
				sv[save_slot].used=true;				
				save_data();		
				if(sublevel==0) set_state(STORY);
				else set_state(LOAD_DATA);
			}else if(pant.ctrl.anybut) 
				if(sublevel==0) set_state(STORY);
				else set_state(LOAD_DATA);
			break;
			
			case LOAD_SAVED :
			if(pant.ctrl.right) { save_slot=(save_slot+1)%3; pant.ctrl.reset();}			
			if(pant.ctrl.left) { save_slot=(3+save_slot-1)%3; pant.ctrl.reset();}									
			if(pant.ctrl.but1 && sv[save_slot].used==true) {
				pant.ctrl.reset();						
				level=sv[save_slot].level;
				sublevel=sv[save_slot].sublevel;
				lifes=sv[save_slot].lifes;
				weapon=sv[save_slot].weapon;
				cleared[0]=sv[save_slot].levels[0];
				cleared[1]=sv[save_slot].levels[1];
				cleared[2]=sv[save_slot].levels[2];
				helmet=sv[save_slot].helmet;
				armor=sv[save_slot].armor;
				gauntlets=sv[save_slot].gauntlets;
				boots=sv[save_slot].boots;
				cape=sv[save_slot].cape;				
				if(sublevel==0) set_state(STORY);
				else set_state(LOAD_DATA);
			}else if(pant.ctrl.anybut) {pant.ctrl.reset(); set_state(MAIN_MENU);}
			break;
			
			case IN_GAME_MENU :
			if(pant.ctrl.right) { menu_opt=(menu_opt+1)%4; pant.ctrl.reset();}			
			if(pant.ctrl.left) { menu_opt=(4+menu_opt-1)%4; pant.ctrl.reset();}			
			if(pant.ctrl.down) { menu_item=(menu_item+1)%6; pant.ctrl.reset();}			
			if(pant.ctrl.up) { menu_item=(6+menu_item-1)%6; pant.ctrl.reset();}						
			if(pant.ctrl.anybut){
				pant.ctrl.reset();
				switch(menu_opt){					
					case 0 : set_state(GAME_ACTION); break;						
					case 1 : if(sound) sound=false; else sound=true; break;
					case 2 : set_state(CONTROLS2); break;
					case 3 : pant.destroy_world_gfx(); set_state(MAIN_MENU); break;
				}					
			}			
			break;
			
			case CONTROLS :
			if(pant.ctrl.anybut){
				set_state(MAIN_MENU);
				pant.ctrl.reset();
			}
			break;
			
			case CONTROLS2 :
			if(pant.ctrl.anybut){
				set_state(IN_GAME_MENU);
				pant.ctrl.reset();
			}
			break;			
			
			case CREDITS :
			if(pant.ctrl.anybut){
				set_state(MAIN_MENU);
				pant.ctrl.reset();
			}
			break;						
			
			case END_GAME :
			if(cnt>30) world.update(pant);
			if(cnt>130) set_state(EPILOGUE);
			break;
			
			case EPILOGUE :
			if(cnt>400) set_state(RANK);
			break;
			
			case RANK :
			if(pant.ctrl.anybut) {pant.destroy_world_gfx(); pant.ctrl.reset(); set_state(CREDITS);}
			break;
			
			
		}	
		
	}		
	
	public void load_data()
	{
		byte[] GameData=null;	
		RecordStore rs;
		 					
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
	
			if (rs.getNumRecords() == 0){
				
								
				System.out.println("Warning! Default setting...");
				
				ByteArrayOutputStream bstream = new ByteArrayOutputStream();
                		DataOutputStream ostream = new DataOutputStream(bstream);				
                		
				ostream.writeBoolean(sound);								
				ostream.writeInt(dead_times);								
                						
				for(int j=0; j<3; j++){
			        	ostream.writeBoolean(sv[j].used);
			        	ostream.writeInt(sv[j].level);
			        	ostream.writeInt(sv[j].sublevel);
			        	ostream.writeInt(sv[j].lifes);
			        	ostream.writeInt(sv[j].weapon);
			        	for(int k=0; k<3; k++)
			     			ostream.writeBoolean(sv[j].levels[k]);
			        	ostream.writeBoolean(sv[j].helmet);
			        	ostream.writeBoolean(sv[j].armor);
			        	ostream.writeBoolean(sv[j].gauntlets);
			        	ostream.writeBoolean(sv[j].boots);
			        	ostream.writeBoolean(sv[j].cape);
				}
					
	              		ostream.flush();
	                	ostream.close();
	                					              
	                	GameData = bstream.toByteArray();
	                	rs.addRecord(GameData, 0, GameData.length);									
														                																					
			} else {
				
				System.out.println("Retrieve settings...");
											
				GameData = rs.getRecord(1);
				
				DataInputStream istream = new DataInputStream(new ByteArrayInputStream(GameData, 0, GameData.length));				
				
				sound=istream.readBoolean();								
				dead_times=istream.readInt();								
												                            		
				for(int j=0; j<3; j++){
			        	sv[j].used=istream.readBoolean();
			        	sv[j].level=istream.readInt();
			        	sv[j].sublevel=istream.readInt();
			        	sv[j].lifes=istream.readInt();
			        	sv[j].weapon=istream.readInt();
			        	for(int k=0; k<3; k++)
			     			sv[j].levels[k]=istream.readBoolean();			        	
			        	sv[j].helmet=istream.readBoolean();
			        	sv[j].armor=istream.readBoolean();
			        	sv[j].gauntlets=istream.readBoolean();
			        	sv[j].boots=istream.readBoolean();
			        	sv[j].cape=istream.readBoolean();
				}
				                    		                    		                    												
			}
			rs.closeRecordStore();
			
		} catch(Exception e) {System.out.println("EXCP");}
			
	}
																																																															
	public void save_data()
	{
		byte[] GameData=null;	
		RecordStore rs=null;		
		 					
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
					
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
               		DataOutputStream ostream = new DataOutputStream(bstream);				
								
			ostream.writeBoolean(sound);								
			ostream.writeInt(dead_times);								
												        	                   	        	        	                				
			for(int j=0; j<3; j++){
			        ostream.writeBoolean(sv[j].used);
			        ostream.writeInt(sv[j].level);
			        ostream.writeInt(sv[j].sublevel);
			        ostream.writeInt(sv[j].lifes);
			        ostream.writeInt(sv[j].weapon);
			        for(int k=0; k<3; k++)
			     		ostream.writeBoolean(sv[j].levels[k]);			        
			        ostream.writeBoolean(sv[j].helmet);
			        ostream.writeBoolean(sv[j].armor);
			        ostream.writeBoolean(sv[j].gauntlets);
			        ostream.writeBoolean(sv[j].boots);
			        ostream.writeBoolean(sv[j].cape);
			}
			
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

class SavedGame {
	
	public int level=0, sublevel=0, lifes=0, weapon=0;
	public boolean used=false, helmet=false, armor=false, gauntlets=false, boots=false, cape=false, levels[]={false,false,false};
	
	public SavedGame()
	{
		used=false;	
	}
}