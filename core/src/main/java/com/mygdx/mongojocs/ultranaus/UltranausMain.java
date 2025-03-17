/*
	"Ultranaus" Coded by David Rodriguez 2003
*/

package com.mygdx.mongojocs.ultranaus;

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

import java.util.Random;
import java.io.*;

//import com.nokia.mid.ui.*;


public class UltranausMain extends MIDlet implements Runnable, CommandListener
{
		
	public static UltranausCanvas pant;
	public static Display display;
	public static Form scoreForm;
	public static TextField scoreField;
	public Thread thread;
	
	public static final int N_COURSES=12;
	public static final int N_LAPS=3;	
	public static final int N_CREDITS=3;
	
	public static final int MAIN_TITLE=0;
	public static final int LOAD_DATA=1;
	public static final int GET_READY=2;
	public static final int COUNTDOWN=3;
	public static final int GAME_ACTION=4;
	public static final int MAIN_MENU=5;
	public static final int CHOOSE_SHIP=6;
	public static final int IN_GAME_MENU=7;
	public static final int INTRO=8;
	public static final int LOAD_INTRO=9;
	public static final int RACE_OVER=10;
	public static final int RESULTS=11;
	public static final int CRASHED=12;
	public static final int ASKCONT=13;
	public static final int GAME_OVER=14;
	public static final int SELECT_COURSE=15;	
	public static final int NEW_RECORD=16;	
	public static final int NEW_RECORD_OK=17;	
	public static final int RECORDS=18;
	public static final int CREDITS=19;
	public static final int ENDING=20;
	public static final int HOW_TO_PLAY=21;
	public static final int HOW_TO_PLAY2=22;
	public static final int WAIT=99;
	
	public static final int ARCADE=0;
	public static final int DEMO=1;
	public static final int PRACTICE=2;
	
	public static final String GameDataName="UltranausData";
	
		
	public int state, cnt, opt, opty, cury, pl_ship=0, course, last_rank, credits, game_mode, highest_course=0, help_frame, sh_offset=0;	
	public UltraRace rc;
	public static boolean auto=true, sound=true;
	public static int best_lap;	
	public static String r_names[];
	public static int r_times[];
	public static byte r_ships[];
	Random rnd;
	
	public void startApp()
	{
		pant = new UltranausCanvas(this);
		display = Display.getDisplay(this);
						
		System.gc();						
						
		// Form for the record name entry				
						
		Command doneCommand = new Command(Text.done, Command.OK, 1);
		
		scoreForm = new Form(Text.new_record);
        	scoreField = new TextField(Text.type_name, "",3,TextField.ANY);
		scoreField.setMaxSize(3);
        	scoreForm.append(scoreField);   
		scoreForm.addCommand(doneCommand);
        	scoreForm.setCommandListener(this);
    		System.gc();
								
		display.setCurrent(pant);
		
		rc=null;
		
		rnd=new Random();
								
		load_data();
				
		set_state(LOAD_INTRO);
		
		thread=new Thread(this);
		thread.start();
				
		System.gc();
										
	}
	
	public void commandAction (Command c, Displayable d)
	{
	       String label = c.getLabel();
	       if (label == Text.done ) {
			char ch[]=new char[3];
			scoreField.getChars(ch);
			r_names[course]=new String(ch).toUpperCase(); 	      	
			r_times[course]=best_lap;
			r_ships[course]=(byte)pl_ship;
	      		display.setCurrent(pant);
	      		pant.ctrl.reset();
	      		set_state(NEW_RECORD_OK);	      	
	       } 	 	
	}	
	
	protected void pauseApp()
	{
		
	}
	
	protected void destroyApp(boolean unconditional)
	{		
		/*display.setCurrent(null);
		pant=null; */
		thread=null;
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

		game_update();

		pant.repaint();

		pant.serviceRepaints();

		do{
			try{
				Thread.sleep(1);

			}catch(InterruptedException e) {}

		}while(System.currentTimeMillis()<t+50);
	}

	public void runEnd()
	{


	}
	
	public void run()
	{
		long t;

		while (thread!=null){

			t=System.currentTimeMillis();

			game_update();

			pant.repaint();

			pant.serviceRepaints();

			do{
				try{
					Thread.sleep(1);

				}catch(InterruptedException e) {}

			}while(System.currentTimeMillis()<t+50);

		}
	}
	
	public void set_state(int s)
	{
		state=s; cnt=0;	opt=0; opty=0; sh_offset=0;
	}
	
	private void game_update(){
				
		cnt++;
				
		// update of the game
		
		switch(state) {
			
			case LOAD_INTRO :
			if(cnt==1) pant.load_loading_gfx();
			if(cnt>2){
				 pant.destroy_loading_gfx();
				 pant.load_intro_gfx();
				 pant.load_logo_gfx();
				 pant.load_select_gfx();
				 set_state(INTRO);
				 
				 //pant.destroy_intro_gfx();
				 //set_state(MAIN_MENU);
			}							
			break;
			
			case INTRO :
			if(cnt==1) pant.play_music(0,0);			
			if((cnt>512) || (pant.ctrl.anybut)){
				pant.destroy_intro_gfx();
				pant.ctrl.reset();
				set_state(MAIN_MENU);				
				pant.destroy_select_gfx();
				pant.stop_music();
			}				
			break;
			
			case MAIN_MENU :
			if(cury<119+(10*opt)) cury+=2;
			if(cury>119+(10*opt)) cury-=2;
			if(cnt==1){ 
				if(!pant.loaded_logo_gfx()) pant.load_logo_gfx(); cury=119+(10*opt);
			}
			if(opty>0){
				opty-=2;
			}else if(opty<0){				
				opty+=2;
			}else{				
				if(pant.ctrl.up) { opt--; if(opt<0) opt=6; opty=+16; pant.ctrl.reset();}
				if(pant.ctrl.down) { opt=(opt+1)%7; opty=-16; pant.ctrl.reset();}
				if(cnt==300){ game_mode=DEMO; course=Math.abs(rnd.nextInt()%(highest_course+1)); pl_ship=Math.abs(rnd.nextInt()%2); pant.ctrl.reset(); set_state(LOAD_DATA); }				
				if(pant.ctrl.anybut){				
					switch(opt){					
						case 0 : set_state(CHOOSE_SHIP); pant.destroy_logo_gfx(); game_mode=ARCADE; credits=N_CREDITS; course=0; pant.ctrl.reset(); break;	
						case 1 : set_state(SELECT_COURSE); pant.destroy_logo_gfx(); game_mode=PRACTICE; credits=0; course=0; pant.ctrl.reset(); break;	
						case 3 : if(auto) auto=false; else auto=true; pant.ctrl.reset(); break;	
						case 2 : if(sound) sound=false; else sound=true; pant.ctrl.reset(); break;	
						case 4 : set_state(HOW_TO_PLAY); help_frame=0; pant.ctrl.reset(); break;
						case 5 : set_state(CREDITS); pant.ctrl.reset(); break;							
						case 6 : destroyApp(false); break;
					}						
				}				
			}
			break;
			
			case IN_GAME_MENU :
			if(cury<109+(10*opt)) cury+=2;
			if(cury>109+(10*opt)) cury-=2;			
			if(cnt==1) cury=109+(10*opt);
			if(opty>0){
				opty-=2;
			}else if(opty<0){				
				opty+=2;
			}else{
				if(pant.ctrl.up) { opt--; if(opt<0) opt=4; opty=+16; pant.ctrl.reset();}
				if(pant.ctrl.down) { opt=(opt+1)%5; opty=-16; pant.ctrl.reset();}				
				if(pant.ctrl.anybut){				
					switch(opt){					
						case 0 : set_state(GAME_ACTION); break;							
						case 1 : if(sound) sound=false; else sound=true; pant.ctrl.reset(); break;	
						case 2 : if(auto) auto=false; else auto=true; pant.ctrl.reset(); break;	
						case 3 : help_frame=0; set_state(HOW_TO_PLAY2); pant.ctrl.reset(); break;
						case 4 : best_lap=rc.sh[0].bestlap; /*rc.destroy();*/ rc=null; System.gc(); pant.destroy_race_gfx(); pant.ctrl.reset(); 
							 if(game_mode==PRACTICE){																								 	
								if(best_lap<r_times[course]) set_state(NEW_RECORD);
								else set_state(NEW_RECORD_OK);
							 }else
							 	set_state(MAIN_MENU);																		
						break;
					}						
				}
				
			}
			break;
									
			case CHOOSE_SHIP :
			if (cnt==1) pant.load_select_gfx();
			//if(pant.ctrl.up) pl_ship-=3;
			//if(pant.ctrl.down) pl_ship+=3;
			if(sh_offset>0) sh_offset-=15;
			if(sh_offset<0) sh_offset+=15;			
			if(pant.ctrl.left && sh_offset==0) {sh_offset=-150; pl_ship--; cnt=2;}
			if(pant.ctrl.right && sh_offset==0) {sh_offset=150; pl_ship++; cnt=2;}
			if(pant.ctrl.but1) { pant.destroy_select_gfx(); set_state(LOAD_DATA);}
			if(pant.ctrl.but3) { pant.destroy_select_gfx(); set_state(MAIN_MENU);}
			if(pant.ctrl.any) pant.ctrl.reset();	
			if(pl_ship>=9) pl_ship-=9;		
			if(pl_ship<0) pl_ship+=9;		
			break;
			
			case LOAD_DATA :
			if(cnt==1) pant.load_loading_gfx();
			if(cnt>2){				
				pant.destroy_loading_gfx();				
				rc=new UltraRace(course,pl_ship);				
				pant.load_race_gfx(rc);				
				if(game_mode==DEMO) set_state(GAME_ACTION);										
				else set_state(COUNTDOWN);										
			}
			break;
			
			case COUNTDOWN :
			if(cnt==1) pant.play_music(1,1);
			if(cnt>=100){
				 for(int i=0; i<UltranausCanvas.N_RACERS; i++)
				 	rc.sh[i].lastlap=(int)System.currentTimeMillis();
				 set_state(GAME_ACTION);
			}
			break;
				
			case GAME_ACTION :					
			switch(game_mode){
				case ARCADE :
				rc.update(pant.ctrl,1);
				rc.update_rank(); 
				if(pant.ctrl.but3) {set_state(IN_GAME_MENU); pant.ctrl.reset();}
				if((rc.sh[0].laps>=N_LAPS) || (rc.sh[0].energy==0)) {set_state(RACE_OVER); pant.ctrl.reset();}
				break;
				
				case DEMO :
				rc.update(pant.ctrl,0);
				rc.update_rank(); 
				if((pant.ctrl.anybut) || (rc.sh[0].energy==0) || (cnt>=600)){
					 set_state(RECORDS); pant.ctrl.reset();
					 rc.destroy(); rc=null; pant.destroy_race_gfx(); System.gc(); 
				}
				break;
				
				case PRACTICE :
				rc.update(pant.ctrl,1);
				rc.update_rank(); 
				if(pant.ctrl.but3) {set_state(IN_GAME_MENU); pant.ctrl.reset();}
				if(rc.sh[0].energy==0) {set_state(RACE_OVER); }					
				break;				
			}
			break;
			
			case RACE_OVER :
			rc.update(pant.ctrl,0);
			if(cnt>=99) {				
				pant.ctrl.reset(); 
				last_rank=rc.sh[0].rank+1;
				best_lap=rc.sh[0].bestlap;
				if(game_mode==DEMO)
					if(best_lap<r_times[course]) set_state(NEW_RECORD);
					else set_state(NEW_RECORD_OK);				
				else{
					if(rc.sh[0].energy==0 && rc.sh[0].laps<N_LAPS) set_state(CRASHED);
					else set_state(RESULTS);
					/*rc.destroy();*/ pant.destroy_race_gfx(); System.gc(); 
				}
			}
			break;	
			
			case RESULTS :			
			if (cnt==1) pant.load_results_gfx();
			if (cnt==1) pant.play_music(5,1);
			if(pant.ctrl.anybut) {
				if(best_lap<r_times[course]) set_state(NEW_RECORD);
				else set_state(NEW_RECORD_OK);
				rc.destroy();  rc=null; System.gc();
				pant.destroy_results_gfx(); pant.ctrl.reset();  				
				pant.stop_music();
			}							
			break;
			
			case NEW_RECORD :
			//Display.getDisplay(this).setCurrent(scoreForm);
			set_state(WAIT);
			break;
			
			case WAIT :
				commandAction(new Command(Text.done,1,1), null);
			break;
			
			case NEW_RECORD_OK :
			if((last_rank==1 && game_mode!=PRACTICE) || course==N_COURSES-1){
				if(course==N_COURSES-1) set_state(ENDING);
				else set_state(LOAD_DATA);
				if(course<N_COURSES-1) course++; if(highest_course<course && highest_course<N_COURSES-1) highest_course=course;
				pant.destroy_results_gfx(); pant.ctrl.reset(); 
			}else {
				if(credits>0) set_state(ASKCONT);					
				else set_state(GAME_OVER);
				
			}							
			break;

			
			case CRASHED :
			if (cnt==1) pant.load_crashed_gfx();
			if (cnt==1) pant.play_music(6,1);
			if(pant.ctrl.anybut) {				 
				if(credits>0) set_state(ASKCONT); else set_state(GAME_OVER);
				rc=null; pant.destroy_crashed_gfx(); pant.ctrl.reset(); 
				pant.stop_music();
			}
			break;
			
			case ASKCONT :
			if (cnt==1) pant.load_results_gfx();
			if(pant.ctrl.but1) {set_state(CHOOSE_SHIP); credits--; if(course<N_COURSES-1) course++; pant.destroy_results_gfx(); pant.ctrl.reset();}
			if (pant.ctrl.but3 || cnt>=297) {set_state(GAME_OVER); pant.destroy_results_gfx(); pant.ctrl.reset(); }
			break; 																	
			
			case GAME_OVER :
			if (cnt==1){save_data(); pant.load_gameover_gfx();}
			if((pant.ctrl.anybut) || (cnt>=150)){
				set_state(RECORDS);				
				pant.destroy_gameover_gfx(); pant.ctrl.reset();
			} 
			break;
			
			case SELECT_COURSE :
			if (cnt==1) pant.load_selectcourse_gfx();
			if(pant.ctrl.left && course>0) course--;
			if(pant.ctrl.right && course<highest_course) course++;
			if(pant.ctrl.but1) { pant.destroy_select_gfx(); set_state(CHOOSE_SHIP);}
			if(pant.ctrl.but3) { pant.destroy_select_gfx(); set_state(MAIN_MENU);}
			if(pant.ctrl.any) pant.ctrl.reset();				
			break;
			
			case RECORDS :
			if((pant.ctrl.anybut) || (cnt>=400)){
				pant.ctrl.reset(); set_state(MAIN_MENU);		
			}
			break;		
			
			case CREDITS :						
			if((pant.ctrl.anybut) || (cnt>=300)){
				set_state(MAIN_MENU);				
				pant.ctrl.reset();
			} 
			break;
			
			case ENDING :				
			if (cnt==1) pant.load_ending_gfx();		
			if((pant.ctrl.anybut) || (cnt>=250)){
				set_state(CREDITS);				
				pant.ctrl.reset();
				pant.destroy_ending_gfx();		
			} 
			break;	
			
			case HOW_TO_PLAY :
			case HOW_TO_PLAY2 :
			if (cnt==1) { pant.load_misc_gfx();}
			if(pant.ctrl.but1){
				help_frame++;
				pant.ctrl.reset();
				if(help_frame==2){										
					if(state==HOW_TO_PLAY){/*pant.destroy_misc_gfx();*/ set_state(MAIN_MENU);}
					else set_state(IN_GAME_MENU);
					
				}
			if(pant.ctrl.but3) { pant.destroy_select_gfx(); set_state(MAIN_MENU);}				
			}
			break;									
		}	
		
	}
	
	public static String time_str(long t)
	{		
		String min=""+(t/60000),
		     sec=""+((t%60000)/1000),
		     cent=""+((t%1000)/10);
		    
		if((t%60000)/1000<10) sec="0"+sec;
		if((t%1000)/10<10) cent="0"+cent;
		
		return ""+min+"M "+sec+"S "+cent;	
	}
	
	public void load_data()
	{
		byte[] GameData=null;	
		RecordStore rs;
		 		
		r_names=new String[N_COURSES];
		r_times=new int[N_COURSES];
		r_ships=new byte[N_COURSES];		
			
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
	
			if (rs.getNumRecords() == 0){
				
								
				System.out.println("Warning! Default setting...");
				for(int i=0; i<N_COURSES; i++){
					r_names[i]="M J";
					r_times[i]=120000;
					r_ships[i]=(byte)(i%9);
				}
				
				ByteArrayOutputStream bstream = new ByteArrayOutputStream();
                		DataOutputStream ostream = new DataOutputStream(bstream);				
								
			        ostream.writeBoolean(sound);
					ostream.writeBoolean(auto);
	        	    ostream.writeInt(highest_course);
	        	    ostream.writeInt(pl_ship);
	        	        	                		                				
				for(int j=0; j<N_COURSES; j++){	
								
					ostream.writeUTF(r_names[j]);			                    
					ostream.writeInt(r_times[j]);			                    
					ostream.writeByte(r_ships[j]);			                    			
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
					auto=istream.readBoolean();
		            highest_course=istream.readInt();
		            pl_ship=istream.readInt();
		                		                		                				
				for(int j=0; j<N_COURSES; j++){	
								
					r_names[j]=istream.readUTF();			                    
					r_times[j]=istream.readInt();			                    
					r_ships[j]=istream.readByte();			                    			
				}		                    		                    		                    												
			}
			rs.closeRecordStore();
			
		} catch(Exception e) {System.out.println("EXCP");}
			
	}
																																																															
	public void save_data()
	{
		byte[] GameData=null;	
		RecordStore rs=null;		
		 		
		/*r_names=new String[N_COURSES];
		r_times=new int[N_COURSES];
		r_ships=new byte[N_COURSES];*/		
			
		try {
			rs = RecordStore.openRecordStore(GameDataName, true);
					
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
               		DataOutputStream ostream = new DataOutputStream(bstream);				
												
		        ostream.writeBoolean(sound);
				ostream.writeBoolean(auto);
        	    ostream.writeInt(highest_course);
        	    ostream.writeInt(pl_ship);
        	                   	        	        	                				
			for(int j=0; j<N_COURSES; j++){	
											
				ostream.writeUTF(r_names[j]);			                    
				ostream.writeInt(r_times[j]);			                    
				ostream.writeByte(r_ships[j]);			                    							
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