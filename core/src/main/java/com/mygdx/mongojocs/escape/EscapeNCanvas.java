package com.mygdx.mongojocs.escape;




import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import java.io.InputStream;
import java.util.Random;
//import com.mygdx.mongojocs.midletemu.Runnable;
//import com.mygdx.mongojocs.midletemu.Thread;




//class EscapeNCanvas extends Canvas implements Runnable
class EscapeNCanvas extends FullCanvas implements Runnable
{
	
	public Thread thread;

	Image Picture, Titles, Sky;
			
	public static final int MAIN_TITLE=0;
	public static final int LOAD_DATA=1;
	public static final int GET_READY=2;
	public static final int GAME_ACTION=3;
	public static final int LOST_LIFE=4;
	public static final int GAME_OVER=5;
	public static final int LEVEL_END=6;
	public static final int RESULTS=7;
	public static final int RECORDS=8;
	public static final int NEW_RECORD=9;
	public static final int COMPLETED=10;
	public static final int NEW_DIF=11;
	public static final int CONGRATULATIONS=12;
	public static final int MAIN_MENU=13;
	public static final int HELP=14;
	public static final int CREDITS=15;
	public static final int NEW_RECORD_CONFIRM=16;
	public static final int WAIT=17;
	public static final int IN_GAME_MENU=18;
	public static final int HELP_IN_GAME=19;
	public static final int CONTROLS=20;
			
	Font fntg, fntb, fntp;
	EscapeNScene  sc;	
	Control ctrl;
	Scores sco;
	int level, lifes, crono, score, dif, state, cnt, opt;
	static int canvasx, canvasy, relinx, reliny;
	boolean snd_on=true;
	String dif_str[]={"Normal","Hard","Extreme"};
	
	String intro_mus = "024A3A6995CD8D85C195919598C80401091CB26822826822826449322C9C144E09270593A10A138249C1646092705968289C124E0B2D42142D0210270210310518824C4166284286209310596824C416628428A310596A10A24C416628428A31049881082C8C144609230591A10A118248C146E092305948288C12460B2942142902102302102D0516824B4165A84285A092D0594824B4165A8428A2D0594A10A24B4165A8428A2D04968000";			 
	String level_mus = "024A3A6995CD8D85C195919598C80400AB205A04E05A04E05A04E06204E06204E06204E06A04E06A04E0A2304968288C125A0A2304968289C12620A2704988289C12620A2704988289C126205A04E05A04E05A04E06A05A06A05A06A05A0A2304988288C126207206207206207206206A05A06A05A06A05A06A05A06A05A0000";
	String crush_mus = "024A3A6995CD8D85C195919598C80400471CB2705138249C164E84284E092705918249C166209310598824C4165A0A2D0596828B4164D000";
	String win_mus   = "024A3A6995CD8D85C195919598C804007F1CA2704938289C124E0A2704938289C124E0A2D0496828B4125A0A310498828C412620460A2304918288C12460A2302704938289C124E0A2704938289C124E0A2704938000";
	
	//Sound mus=null;
		
						
	public EscapeNCanvas()
	{
		
		canvasx=getWidth(); canvasy=getHeight();
		relinx=(canvasx-96)/2; reliny=(canvasy-65)/2;
								
		fntp=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN , Font.SIZE_SMALL);		
		fntb=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN , Font.SIZE_MEDIUM);		
		fntg=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD , Font.SIZE_MEDIUM);		
				
		EscapeNCar.load_sprites();
		EscapeNScene.load_sprites();
		
		// Load the titles image		
				
		try {
			Titles = new Image();
			Titles._createImage("/Titles.png");
			
		}catch(Exception err) {}
		
		try {
			Sky = new Image();
			Sky._createImage("/Sky.png");
			
		}catch(Exception err) {}
		
		
		//Titles=Image.createImage(8,8);
		
		// Create random seed
		
		EscapeNScene.rnd=new Random(1234567);
																			
		ctrl=new Control();
		
		sco=new Scores("Escape");
		
								
		set_state(CONTROLS);
																
		thread=new Thread(this);
		thread.start();
				
		System.gc();
		
	}
	
	private static byte[] convertOTA(String s1)
	{
	        byte abyte0[] = new byte[s1.length() / 2];
	        for(int j1 = 0; j1 < s1.length(); j1 += 2)
	        {
	            char c1 = s1.charAt(j1 + 1);
	            int i1;
	            if(c1 >= '0' && c1 <= '9')
	                i1 = c1 - 48;
	            else
	                i1 = (c1 - 65) + 10;
	            c1 = s1.charAt(j1);
	            if(c1 >= '0' && c1 <= '9')
	                i1 += (c1 - 48) * 16;
	            else
	                i1 += ((c1 - 65) + 10) * 16;
	            abyte0[j1 / 2] = (byte)i1;
	        }
	
	        return abyte0;
	}	
	
	
	public void keyPressed(int keycode)
	{
	
	//	ctrl.reset();
	//	if(state==WAIT) return;
		
		switch(keycode)
		{
			case KEY_NUM1 : ctrl.up=true; ctrl.left=true; break;
			case KEY_NUM2 : ctrl.up=true; break;
			case KEY_NUM3 : ctrl.up=true; ctrl.right=true; break;
			case KEY_NUM4 : ctrl.left=true; break;			
			case KEY_NUM5 : ctrl.but1=true; break;
			case KEY_NUM6 : ctrl.right=true; break;			
			case KEY_NUM7 : ctrl.down=true; ctrl.left=true; break;			
			case KEY_NUM8 : ctrl.down=true; break; 
			case KEY_NUM9 : ctrl.down=true; ctrl.right=true; break;
			case KEY_NUM0 : ctrl.but2=true; break;
			case -6 :
			case KEY_STAR : ctrl.but3=true; break;
		}
		
		/*switch(getGameAction(keycode))
		{
			case LEFT  : ctrl.left=true; break;
			case RIGHT : ctrl.right=true; break;
			case UP    : ctrl.up=true; break;
			case DOWN  : ctrl.down=true; break;
			case FIRE  : ctrl.but1=true; break;
			//case GAME_A: ctrl.but2=true; break;
			//case GAME_B: ctrl.but3=true; break;
		}*/
		
		ctrl.anybut=ctrl.but1 || ctrl.but2 || ctrl.but3 || ctrl.but4;
		ctrl.any=ctrl.up || ctrl.down || ctrl.left || ctrl.right || ctrl.anybut;		
	}
	
	public void keyReleased(int keycode)
	{
		ctrl.reset();
	}

	public void runTick()
	{
		long t;

		//while (thread!=null){

			t=System.currentTimeMillis();

			game_update();

			repaint();

			serviceRepaints();

			try{
				while(System.currentTimeMillis()<t+40) Thread.sleep(1);

			}catch(InterruptedException e) {}

		//}
	}

	public void runEnd() {

	}

	public void run()
	{	
		long t;

		while (thread!=null){
			
			t=System.currentTimeMillis();
											 		 		
 			game_update();
 				 								 										
			repaint();
				
			serviceRepaints();
																				
			try{			
				while(System.currentTimeMillis()<t+40) java.lang.Thread.sleep(1);
				
			}catch(InterruptedException e) {}
																			
		}
	}

	public void runInit() {

	}

	public void paint(Graphics g)
	{					
		int x=0, y=0, xx=0;
													
		if(cnt>1)						
		switch(state) {
			
			case MAIN_TITLE :
			clear_display(g,0,0,0);			
			show_sky(g);	
			if(cnt<176/3) x=176-(cnt*3);
			else x=0;			
			g.setClip(x,0,176,canvasy);
			g.drawImage(Titles,x-208,reliny,20);					
			break;
			
			case MAIN_MENU :			
			show_sky(g);
			g.setColor(255,255,255);
			g.setFont(fntp);				
			g.drawString("Start the game", relinx+15, reliny+4 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("Sound is", relinx+15, reliny+16 , Graphics.LEFT | Graphics.TOP );
			if(snd_on) 
				g.drawString("On", relinx+60, reliny+16 , Graphics.LEFT | Graphics.TOP );
			else 	g.drawString("Off", relinx+60, reliny+16 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("How to play", relinx+15, reliny+28 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("Credits", relinx+15, reliny+40 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("Exit", relinx+15, reliny+52 , Graphics.LEFT | Graphics.TOP );			
			g.setFont(fntb);	
			g.setColor(255,216,0);			
			g.drawString(">", relinx+5, reliny+4+(12*opt) , Graphics.LEFT | Graphics.TOP );			
			break;
			
			case IN_GAME_MENU :			
			sc.show(g);
			if(canvasy>96){
				g.setClip(0,0,canvasx,canvasy);
				g.setColor(0,0,0);
				g.fillRect(0,0,canvasx,(canvasy-96)/2);	
				g.fillRect(0,canvasy-((canvasy-96)/2),canvasx,(canvasy-96)/2);
			}									
			g.setClip(0,0,canvasx,canvasy);
			draw_options_square(g);			
			g.setColor(10,0,0);							
			g.setFont(fntg);				
			g.drawString("Resume", relinx+10, reliny-5 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("Sound is", relinx+10, reliny+10 , Graphics.LEFT | Graphics.TOP );
			if(snd_on) 
				g.drawString("On", relinx+74, reliny+10 , Graphics.LEFT | Graphics.TOP );
			else 	g.drawString("Off", relinx+74, reliny+10 , Graphics.LEFT | Graphics.TOP );						
			g.drawString("How to play", relinx+10, reliny+25 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("Back to title", relinx+10, reliny+40 , Graphics.LEFT | Graphics.TOP );						
			g.setColor(255,0,0);			
			g.drawString(">", relinx, reliny-5+(15*opt) , Graphics.LEFT | Graphics.TOP );			
			break;
			
			case HELP_IN_GAME :
			case HELP :								
			clear_display(g,0,0,0);									
			draw_options_square(g);			
			g.setColor(200,0,0);									
			g.setFont(fntg);				
			g.drawString("How to play", super.getWidth() / 2, reliny-10 , Graphics.HCENTER | Graphics.TOP );			
			g.setColor(0,0,0);						
			g.drawString("2,8:Drive", relinx-6, reliny+7 , Graphics.LEFT | Graphics.TOP );
			g.drawString("6/5:Accel/Brake", relinx-6, reliny+22 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("4:Backwards", relinx-6, reliny+37 , Graphics.LEFT | Graphics.TOP );
			g.drawString("*:Menu", relinx-6, reliny+52 , Graphics.LEFT | Graphics.TOP );			
			break;
			
			case CONTROLS :
			clear_display(g,0,0,0);									
			draw_options_square(g);			
			g.setColor(200,0,0);									
			g.setFont(fntg);				
			g.drawString("Controls", super.getWidth() / 2, reliny-10 , Graphics.HCENTER | Graphics.TOP );			
			g.setColor(0,0,0);
			g.drawString("2 : Up", relinx, reliny+7 , Graphics.LEFT | Graphics.TOP );
			g.drawString("8 : Down", relinx, reliny+22 , Graphics.LEFT | Graphics.TOP );			
			g.drawString("5 : Accept", relinx, reliny+37 , Graphics.LEFT | Graphics.TOP );
			g.drawString("* : Menu", relinx, reliny+52 , Graphics.LEFT | Graphics.TOP );
			break;
			
			
			case CREDITS :
			g.setFont(fntp);	
			clear_display(g,170,80,20);			
			g.setColor(255,216,0);
			g.setClip(relinx+25,5,46,7);
			g.drawImage(Titles,relinx+25-34,5-32,20);				
			g.setClip(0,20,canvasx,canvasy-relinx-20);
			g.setFont(fntg);	
			g.drawString("ESCAPE", super.getWidth()/2, 170-cnt , Graphics.HCENTER | Graphics.TOP );			
			g.drawString("Coded by", super.getWidth()/2, 220-cnt , Graphics.HCENTER | Graphics.TOP );																
			g.drawString("Graphics by", super.getWidth()/2, 300-cnt , Graphics.HCENTER | Graphics.TOP );																
			g.drawString("Microjocs Mobile", super.getWidth()/2, 380-cnt , Graphics.HCENTER | Graphics.TOP );
			g.drawString("2003", super.getWidth()/2, 410-cnt , Graphics.HCENTER | Graphics.TOP );			
			g.setColor(255,255,255);
			//g.setFont(fntb);				
			g.drawString("David Rodríguez", super.getWidth()/2, 250-cnt , Graphics.HCENTER | Graphics.TOP );
			g.drawString("Elías Lozano", super.getWidth()/2, 330-cnt , Graphics.HCENTER | Graphics.TOP );
			g.drawString("www.microjocs.com", super.getWidth()/2, 460-cnt , Graphics.HCENTER | Graphics.TOP );																			
			g.drawString("Thanks for playing!", super.getWidth()/2, 510-cnt , Graphics.HCENTER | Graphics.TOP );			
			break;
									
			case LOAD_DATA :			
			/*clear_display(g,255);			
			g.setClip(25,20,46,7);
			g.drawImage(Titles,25-34,20-32,20);																	
			g.setClip(31,40,34,7);
			g.drawImage(Titles,31,40-32,20);*/
			break;
				
			case GAME_ACTION :
			sc.show(g);
			sc.show_stats(g,lifes,sc.pl.energy,crono);
			if(canvasy>96){
				g.setClip(0,0,canvasx,canvasy);
				g.setColor(0,0,0);
				g.fillRect(0,0,canvasx,(canvasy-96)/2);	
				g.fillRect(0,canvasy-((canvasy-96)/2),canvasx,(canvasy-96)/2);
			}			
			break;
			
			case GET_READY :
			show_sky(g);
			show_pic(g);
			switch(level){
				case 0 : x=80; y=6; break;	
				case 1 : x=147; y=6; break;	
				case 2 : x=83; y=36; break;	
			}
			if(cnt<66/3) xx=176-cnt*3;
			else xx=110;
			g.setClip(xx,reliny+35,60,24);
			g.drawImage(Titles,xx-x,reliny+35-y,20);
			break;
			
			case LOST_LIFE :			
			g.setClip(relinx+16,reliny+24,64,17);
			if(crono==0)				
				g.drawImage(Titles,relinx+16,reliny+24-16,20);								
			else
				g.drawImage(Titles,relinx+16,reliny+24,20);												
			break;
			
			case GAME_OVER :			
			show_sky(g);			
			show_pic(g);
			if(cnt<76/3) xx=-60+cnt*3;
			else xx=16;
			g.setClip(xx,reliny+5,60,24);
			g.drawImage(Titles,xx-143,reliny+5-36,20);			
			break;						
						
			case RESULTS :			
			show_sky(g);
			g.setColor(255,216,0);															
			g.setFont(fntb);	
			g.drawString("Well done!", super.getWidth() / 2, reliny+5 , Graphics.HCENTER | Graphics.TOP );
			g.setColor(255,255,255);
			g.setFont(fntp);	
			g.drawString("Time Bonus:", super.getWidth() / 2, reliny+20 , Graphics.HCENTER | Graphics.TOP );
			g.drawString(Integer.toString(crono)+" X 5="+Integer.toString(crono*5), super.getWidth() / 2, reliny+35 , Graphics.HCENTER | Graphics.TOP );										
			g.drawString("Your Score:"+Integer.toString(score+(crono*5)), super.getWidth() / 2, reliny+50 , Graphics.HCENTER | Graphics.TOP );			
			break;
			
			case RECORDS :
			show_sky(g);
			g.setColor(255,216,0);									
			g.setFont(fntp);	
			g.drawString("Top Drivers", super.getWidth() / 2, reliny+5 , Graphics.HCENTER | Graphics.TOP );
			g.setColor(255,255,255);
			g.setFont(fntb);	
			g.drawString(sco.names[0]+" - "+sco.values[0], super.getWidth() / 2, reliny+20 , Graphics.HCENTER | Graphics.TOP );			
			g.drawString(sco.names[1]+" - "+sco.values[1], super.getWidth() / 2, reliny+35 , Graphics.HCENTER | Graphics.TOP );			
			g.drawString(sco.names[2]+" - "+sco.values[2], super.getWidth() / 2, reliny+50 , Graphics.HCENTER | Graphics.TOP );			
			break;			
			
			case COMPLETED :
			show_pic(g);
			break;
			
			case NEW_DIF :
			show_sky(g);
			g.setColor(255,216,0);									
			g.setFont(fntb);	
			g.drawString("Excellent!", super.getWidth() / 2, reliny+5 , Graphics.HCENTER | Graphics.TOP );			
			g.drawString(dif_str[dif]+" Mode", super.getWidth() / 2, reliny+35 , Graphics.HCENTER | Graphics.TOP );						
			g.setColor(255,255,255);
			g.setFont(fntp);	
			g.drawString("Now get ready for", super.getWidth() / 2, reliny+20 , Graphics.HCENTER | Graphics.TOP );						
			g.drawString("Keep on going!", super.getWidth() / 2, reliny+50 , Graphics.HCENTER | Graphics.TOP );						
			break;
			
			case CONGRATULATIONS :
			clear_display(g,170,80,20);			
			g.setColor(255,216,0);									
			g.setFont(fntg);	
			g.drawString("CONGRATULATIONS", super.getWidth() / 2, reliny-10 , Graphics.HCENTER | Graphics.TOP );
			g.setColor(255,255,255);			
			g.drawString("You are the", super.getWidth() / 2, reliny+10 , Graphics.HCENTER | Graphics.TOP );						
			g.drawString("greatest", super.getWidth() / 2, reliny+30 , Graphics.HCENTER | Graphics.TOP );						
			g.drawString("driver ever!", super.getWidth() / 2, reliny+50 , Graphics.HCENTER | Graphics.TOP );						
			break;
		}
										
	}
	
	public void set_state(int s)
	{
		state=s; cnt=0;	opt=0;
		//if(mus!=null) mus.stop(); mus=null;
		Picture=null; System.gc();
	}
	
	private void game_update()
	{
				
		cnt++;
		
		// update of the game
		
		switch(state) {
			
			case MAIN_TITLE :
			if(cnt==1) {
				/*mus = new Sound(convertOTA(intro_mus), 1);
				mus.init(convertOTA(intro_mus), 1);				
				if(snd_on) mus.play(1);*/
				load_pic("/Titles.png",96,70); 					
				}
			if(ctrl.any){ 				
				ctrl.reset();
				set_state(MAIN_MENU);
				}			
			if(cnt>=400) set_state(RECORDS);
			break;
			
			case MAIN_MENU :
			if(ctrl.up)
				if(opt>0) {opt--; ctrl.reset();}
			if(ctrl.down)
				if(opt<4) {opt++; ctrl.reset();}
			if(ctrl.anybut){
				ctrl.reset();
				switch(opt){				
				case 0 : level=0; lifes=3; score=0; dif=0; set_state(GET_READY); break;
				case 1 : if(snd_on) snd_on=false;
					 else snd_on=true;
					 break;					 
				case 2 : set_state(HELP); break;
				case 3 : set_state(CREDITS); break;
				case 4 : { thread= null; EscapeNMain.gameMidl.destroyApp(false); }break;
				}
			}
			if(cnt>=400) set_state(MAIN_TITLE);				
			break;
			
			case IN_GAME_MENU :
			if(ctrl.up)
				if(opt>0) {opt--; ctrl.reset();}
			if(ctrl.down)
				if(opt<3) {opt++; ctrl.reset();}
			if(ctrl.anybut){
				ctrl.reset();
				switch(opt){				
				case 0 : set_state(GAME_ACTION); break;
				case 1 : if(snd_on) snd_on=false;
					 else snd_on=true;
					 cnt=0;
					 break;					 
				case 2 : set_state(HELP_IN_GAME); break;
				case 3 : sc=null; System.gc(); set_state(MAIN_TITLE); break;					
				}					
			}	
			break;
						
			case HELP :
			if(ctrl.any){ 
				set_state(MAIN_MENU);			
				ctrl.reset();
			}
			
			break;
			
			case HELP_IN_GAME :
			if(ctrl.any){				
				set_state(IN_GAME_MENU);			
				ctrl.reset();
				}
			
			break;			
			
			case CREDITS :
			if(cnt==1){
			}
			if((ctrl.any) || (cnt>550)){
				set_state(MAIN_MENU);						
				ctrl.reset();
				}
			break;
						
			case LOAD_DATA : 
			if(cnt==1){
				/*sc=null; 
				repaint();
				serviceRepaints();
				System.gc();*/			
					
				//Loading data
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						switch(level){
							case 0 : sc=new EscapeNCity(dif); break;
							case 1 : sc=new EscapeNMotorway(dif); break;
							case 2 : sc=new EscapeNDesert(dif); break;
						}
						System.gc();
						crono=sc.total_time();
						set_state(GAME_ACTION);
					}
				});
			}
			break;
			
			case GAME_ACTION :					 
			sc.update(ctrl);
			if(cnt%33==0) crono--;	
			if(sc.pl.x>sc.psizex*8)				
				set_state(RESULTS);	
			if(ctrl.but3) {ctrl.reset(); set_state(IN_GAME_MENU);}
//truc
	//		if(ctrl.but2)	set_state(RESULTS);				

			if((sc.pl.energy==0) || (crono==0))
				set_state(LOST_LIFE);			
			break;
			
			case GET_READY :
			if(cnt==1){
				/*mus = new Sound(convertOTA(level_mus), 1);
				mus.init(convertOTA(level_mus), 1);
				if(snd_on) mus.play(1);*/
				//inicio fase				
				load_pic("/Level"+(level+1)+".png",96,65);								
				
			}				 			
			if(ctrl.any){
				set_state(LOAD_DATA);
				ctrl.reset();
				}
			break;
			
			case LOST_LIFE :
			if(cnt==1){
				sc=null; System.gc(); 								
				/*mus = new Sound(convertOTA(crush_mus), 1);
				mus.init(convertOTA(crush_mus), 1);				
				if(snd_on) mus.play(1);*/
			}				 			
			if((ctrl.any) && (cnt>20)) {				
				lifes--;
				ctrl.reset();
				if(lifes>0) {set_state(GET_READY);}
				else if(sco.isHighScore(score))
					set_state(NEW_RECORD);
					else set_state(GAME_OVER);				
			}
			break;
			
			case GAME_OVER :
			if(cnt==1) load_pic("/Arrested.png",96,65);
			if(ctrl.any){
				set_state(RECORDS);
				ctrl.reset();
			}
			break;
											
			case RESULTS :
			if(cnt==1){
				sc=null; System.gc();				
				/*mus = new Sound(convertOTA(win_mus), 1);
				mus.init(convertOTA(win_mus), 1);				
				if(snd_on) mus.play(1);*/
			}				 						
			if((ctrl.any) && (cnt>=60)){
				score+=crono*5;
				lifes++;	
				level++; 
				ctrl.reset();
				if(level>2) {level-=3; dif++; set_state(COMPLETED);}				 							 
				else set_state(GET_READY);
			}
			break;
			
			case COMPLETED :
			if(cnt==1) load_pic("/Sunset.png",96,65);
			if(ctrl.any){
				ctrl.reset();
				if(dif==3) set_state(CONGRATULATIONS);
				else set_state(NEW_DIF);			
			}
			break;
			
			case NEW_DIF :
			if(ctrl.any){
				ctrl.reset();
				set_state(GET_READY);			
				}
			break;
			
			case CONGRATULATIONS :
			if(ctrl.any) {
				ctrl.reset();
				if(sco.isHighScore(score))
					set_state(NEW_RECORD);
					else set_state(MAIN_TITLE);				
			}
			break;
			
			case CONTROLS :			
			if(ctrl.any){
				ctrl.reset();
				set_state(MAIN_TITLE);
			}
			break;
						
			case RECORDS :			
			if((ctrl.any) || (cnt>=400)) {
				ctrl.reset();
				set_state(MAIN_TITLE);
			}
			break;
			
			case NEW_RECORD :
			// Retrieve the name data			
			//EscapeNMain.display.setCurrent(EscapeNMain.scoreForm);
			set_state(WAIT);
			break;
			
			case WAIT :
			// Do nothing
				set_state(NEW_RECORD_CONFIRM);
			break;
									
			case NEW_RECORD_CONFIRM:
			char c[]=new char[3];
			EscapeNMain.scoreField.getChars(c);
			String name=new String(c).toUpperCase(); 
			sco.addHighScore(score,name); 
			System.gc();
			set_state(RECORDS);
			break;
																			
		}																																		
	}
		
	private void clear_display(Graphics gr, int r, int g, int b)
	{
		gr.setColor(r,g,b);
		gr.fillRect(0,0,canvasx,canvasy);		
	}
	
	private void load_pic(String n, int sx, int sy)
	{		
		InputStream is;
				
		Picture=null; System.gc();
			
		try {
			Picture=Image.createImage(n); 
			
		}catch(Exception err) {}
							
	}

	private void show_sky(Graphics g)
	{			
		int x=(cnt/2)%224;
		g.setClip(0,0,canvasx,canvasy);
		g.setColor(0,0,0);
		g.fillRect(0,0,canvasx,canvasy);
		g.setClip(0,0,canvasx,reliny+65);
		g.drawImage(Sky,-x,reliny,20);
		g.drawImage(Sky,-x+224,reliny,20);			
	}
	
	private void draw_options_square(Graphics g)
	{		
		g.setColor(252,138,0);
		g.drawRect(relinx-13,reliny-16,96+25,65+25);									
		g.setColor(255,78,0);
		g.drawRect(relinx-12,reliny-15,96+23,65+23);						
		g.setColor(252,138,0);
		g.drawRect(relinx-11,reliny-14,96+21,65+21);			
		g.setColor(252,230,0);
		g.fillRect(relinx-10,reliny-13,96+20,65+20);	
	}
	
	private void show_pic(Graphics g)
	{			
		g.setClip(0,0,canvasx,canvasy);
		//g.setColor(0,0,0);
		//g.fillRect(0,0,canvasx,canvasy);
		g.drawImage(Picture,0,reliny,20);			
	}
	
	
}