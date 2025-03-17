package com.mygdx.mongojocs.ultranaus;

/*
	Canvas for Nokia 60 series
*/


import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

import java.util.Random;



class UltranausCanvas extends FullCanvas
{
	
	public static final int N_RACERS = 6;
	
	public static final String player_names[]={"snid","atte","moir","silk","snudde","noid","annan","ittoe","hadde"},
				   player_teams[]={"richah","ottoy","richah","aspow","fazek","fazek","ottoy","aspow","aspow"},
				   player_ages[]={"28","19","cyborg","39","cyborg","46","17","cyborg","unknown"};				  
					
	Font fnts, fntb;
	public Control ctrl;	
	Random rnd;
	static UltranausMain game;
	
	Image Picture, ShipImg, MiscImg=null, TileSet, LogoImg, SelImg, LoadImg, IntroImg1, IntroImg2=null, ResultImg, GOverImg, CourImg, EndImg,
		City, WindowTile, Border, Faces, BigShip1, BigShip2, BigShip3, BigShip4, Fonts, Stats, Console, Wallpaper, Arrows;
				
	public int CANVX, CANVY, begx, begy; 
	public int Desp, bgx, bgy, loadpr=0;
		
	/*String  intro_mus="024A3A65D5B1D1C985B985D5CC0400A71C558A38C495828D312560A30C495828BB12560A3102B0495628E312560A34C495828C312560A2EC495828C40AC124D8A34C493828C3124E0A2CC493828AB124E0A2D0270493628D3124E0A30C493828B3124E0A2AC493828B4000",
		level_mus="024A3A65D5B1D1C985B985D5CC0400271C5604205586A0420620A2B0210495628AC125604205605E0000",
		item1_mus="024A3A65D5B1D1C985B985D5CC04000F1CA390495828AC0000",
		item2_mus="024A3A65D5B1D1C985B985D5CC04000D1CA2B03104958000",
		item3_mus="024A3A65D5B1D1C985B985D5CC04000B1C6A0A31023000",
		win_mus=  "024A3A65D5B1D1C985B985D5CC0400271C558A2AC495828AC126206906A0A22C26C49C69869A2800",
		lose_mus= "024A3A65D5B1D1C985B985D5CC0400231C558A2AC495828AC126206906A061855859A61A54A000";   */
		
	//Sound  mus, item1_snd, item2_snd, item3_snd, snd=null;
	
	byte maps[][]=new byte [6][];  
												
	public UltranausCanvas(UltranausMain main)
	{
		game=main;	
				
		fnts=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN , Font.SIZE_SMALL);		
		fntb=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN , Font.SIZE_SMALL);		
		
		CANVX=getWidth(); CANVY=getHeight();	
		begx=(CANVX-96)/2; begy=(CANVY-65)/2;
														
		try {
			Fonts = new Image();
			Fonts._createImage("/Fonts.png");
			
		}catch(Exception err) {}
									
		LogoImg=null;
						
		ctrl=new Control();

		/*
		item1_snd= new Sound(convertOTA(item1_mus), Sound.FORMAT_TONE);
		item1_snd.init(convertOTA(item1_mus),1);
		item2_snd= new Sound(convertOTA(item2_mus), Sound.FORMAT_TONE);
		item2_snd.init(convertOTA(item2_mus),1);
		item3_snd= new Sound(convertOTA(item3_mus), Sound.FORMAT_TONE);
		item3_snd.init(convertOTA(item3_mus),1);
		 */
	}
			
	public void keyPressed(int keycode)
	{
	
		ctrl.reset();
				
		switch(keycode)
		{
			case KEY_NUM1 : ctrl.up=true; ctrl.left=true; break;
			case KEY_NUM2 : ctrl.up=true; break;
			case KEY_NUM3 : ctrl.up=true; ctrl.right=true; break;
			case KEY_NUM4 : ctrl.left=true; break;
			case -7 :
			case KEY_NUM5 : ctrl.but1=true; break;
			case KEY_NUM6 : ctrl.right=true; break;
			case KEY_NUM7 : ctrl.down=true; ctrl.left=true; break;
			case KEY_NUM8 : ctrl.down=true; break; 
			case KEY_NUM9 : ctrl.down=true; ctrl.right=true; break;
			case KEY_NUM0 : ctrl.but2=true; break;
			case -6 :
			case KEY_STAR :	ctrl.but3=true; break;
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
		}		*/
		
		ctrl.anybut=ctrl.but1 || ctrl.but2 || ctrl.but3 || ctrl.but4;
		ctrl.any=ctrl.up || ctrl.down || ctrl.left || ctrl.right || ctrl.anybut;							
	}
	
	public void keyReleased(int keycode)
	{
		ctrl.reset();
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
								
	public void paint(Graphics g)
	{

		int x,y,sx,sy,ix,iy,p;
		Image aux;
		String s;
		DirectGraphics dg= DirectUtils.getDirectGraphics(g);
														
		if(game.cnt>1)						
		switch(game.state) {
			
			case UltranausMain.LOAD_DATA :
			clear_display(g,0,0,0);			
			break;
			
			case UltranausMain.LOAD_INTRO :			
			clear_display(g,0,0,0);			
			show_font_text_center(g,Text.loading,85,1,99);			
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(0,255,255);
			g.drawRect(46,118,84,7);
			g.fillRect(48,120,10*loadpr,4);
			break;
			
			case UltranausMain.INTRO :
			//DeviceControl.setLights(0,100);
			if(game.cnt==0) clear_display(g,0,0,0);			
			if(game.cnt<44){						
				p=game.cnt;
				g.drawImage(City,0,0,20);
				g.setColor(0,0,0);
				g.fillRect(0,0,p*4,50);								
				g.fillRect(CANVX-p*4,CANVY-50,p*4,50);												
			}
			
			if(game.cnt>=44 && game.cnt<120){						
				p=game.cnt-44;
				g.setClip(0,0,CANVX,CANVY);
				g.drawImage(City,0,0,20);				
				g.setColor(0,0,0);
				g.fillRect(0,0,CANVX,50);								
				g.fillRect(0,CANVY-50,CANVX,50);												
				g.setClip(83,93,176,65);
				if(p<30 && (p/10)%2==0) g.drawImage(IntroImg1,83,93,20);
				else g.drawImage(IntroImg1,83,93-66,20);
				show_font_text_center(g,Text.intro1,178,0,p/2);								
			}
			
			if(game.cnt>=120 && game.cnt<200){						
				p=game.cnt-120;
				g.setClip(0,0,CANVX,CANVY);
				g.drawImage(City,0,0,20);				
				g.setColor(0,0,0);
				g.fillRect(0,0,CANVX,50);								
				g.fillRect(0,CANVY-50,CANVX,50);												
				g.setClip(83,93,192,130);
				g.drawImage(IntroImg1,83,93-132,20);
				show_font_text_center(g,Text.intro2,20,0,p/2);								
			}
						
			if(game.cnt>200 && game.cnt<300){						
				p=game.cnt-200;				
				g.setClip(0,0,CANVX,CANVY);
				g.drawImage(City,0,0,20);				
				g.setColor(0,0,0);
				g.setClip(42,CANVY-400+p*2,96,200);				
				g.drawImage(IntroImg1,42-97,CANVY-400+p*2,20);								
				g.setClip(0,0,CANVX,CANVY);
				g.fillRect(0,0,CANVX,50-(p*3));								
				g.fillRect(0,CANVY-50+(p*3),CANVX,50);																
				show_font_text_center(g,Text.intro3,178,0,p/2);								
			}
			
			if(game.cnt>=300 && game.cnt<380){						
				p=game.cnt-300;
				g.setClip(0,0,CANVX,CANVY);
				g.drawImage(City,0,0,20);								
				if(p<40 && p%2==0){
					g.setClip(42,CANVY-200,96,200);				
					g.drawImage(IntroImg1,42-97,CANVY-200,20);
				}				
				g.setClip(47,71,82,66);				
				g.drawImage(BigShip2,47-83,71,20);
				
				show_font_text_center(g,Text.intro4,20,0,p/2);								
			}
			
			if(game.cnt>=380 && game.cnt<=512){						
				p=game.cnt-380;
				g.setClip(0,0,CANVX,CANVY);
				g.drawImage(City,0,0,20);	
				ix=p*5;							
				iy=p*4;											
				g.setClip(47-ix,71-iy,82,66);				
				g.drawImage(BigShip2,47-83-ix,71-iy,20);				
				show_font_text_center(g,Text.intro5,104,0,p/2);								
				for(int i=0; i<4; i++)
					if(p>10*i){
						g.setClip(13+(40*i),20,27,46);
						g.drawImage(Faces,13+(40*i)-28*i,20,20);
					}
				if(p>40)
				for(int i=4; i<8; i++){
					if(p>10*i){
						ix=i; if(i>5) ix++;
						g.setClip(13+(40*(i-4)),150,27,46);
						g.drawImage(Faces,13+(40*(i-4))-28*ix,150,20);						
					}
				}
				
				if(p>80){
					g.setClip(0,0,CANVX,(p-80)*2);
					g.drawImage(Wallpaper,0,-104+(p-80)*2,20);
					g.setClip(0,CANVY-(p-80)*2,CANVX,CANVY);
					g.drawImage(Wallpaper,0,CANVY-(p-80)*2-104,20);										
				}				
			}

			// REMOVE
				//g.setClip(0,0,CANVX,CANVY);
				//g.drawImage(City,-176,0,20);
													
			break;
			
			case UltranausMain.MAIN_MENU :			
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Wallpaper,0,0,20);								
			show_font_text(g,Text.arcade,10,120,0,99);				
			show_font_text(g,Text.practice,10,130,0,99);				
			if(game.sound) show_font_text(g,Text.snd_on,10,140,0,99);				
			else show_font_text(g,Text.snd_off,10,140,0,99);				
			if(game.auto) show_font_text(g,Text.auto_on,10,150,0,99);				
			else show_font_text(g,Text.auto_off,10,150,0,99);							
			show_font_text(g,Text.how_to_play,10,160,0,99);				
			show_font_text(g,Text.credits,10,170,0,99);				
			show_font_text(g,Text.exit,10,180,0,99);				
			g.setClip(8,game.cury,138,11);
			g.drawImage(MiscImg,8-192,game.cury-120,20);			
			break;
			
			case UltranausMain.IN_GAME_MENU :									
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Wallpaper,0,-16,20);					
			show_bars(g,game.rc);
			
			show_font_text(g,Text.resume,10,110,0,99);							
			if(game.sound) show_font_text(g,Text.snd_on,10,120,0,99);				
			else show_font_text(g,Text.snd_off,10,120,0,99);				
			if(game.auto) show_font_text(g,Text.auto_on,10,130,0,99);				
			else show_font_text(g,Text.auto_off,10,130,0,99);										
			show_font_text(g,Text.how_to_play,10,140,0,99);							
			show_font_text(g,Text.abort,10,150,0,99);				
			g.setClip(8,game.cury,138,11);
			g.drawImage(MiscImg,8-192,game.cury-120,20);
								
			break;
			
			
			case UltranausMain.CHOOSE_SHIP :
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Console,0,0,20);
			show_font_text(g,player_names[game.pl_ship],74,29,0,game.cnt/2);	
			show_font_text(g,player_ages[game.pl_ship],69,37,0,game.cnt/2);	
			show_font_text(g,player_teams[game.pl_ship],41,56,1,game.cnt/2);	
			
			show_font_text(g,""+(UltraShip.VELOCITY[game.pl_ship]*100)/6,124,79,0,99);	
			show_font_text(g,""+(UltraShip.POWER[game.pl_ship]*100)/6,124,87,0,99);	
			show_font_text(g,""+(UltraShip.HANDLING[game.pl_ship]*100)/6,124,95,0,99);	
			g.setClip((CANVX-170)/2,30,170,73);
			g.drawImage(Stats,(CANVX-170)/2,30,20);
			g.setClip(10,31,27,46);
			g.drawImage(Faces,10-(28*game.pl_ship),31,20);
			
			check_draw(g,ShipImg,(game.pl_ship*19),(((game.cnt/6)%12)*19),18,19,150,80);
			
			y=Trig.sin(game.cnt<<3)>>8;
			for(int i=-1; i<=1; i++){
				switch((game.pl_ship+i+9)%9)
				{
					case 0 : aux=BigShip4; ix=0; break;	
					case 1 : aux=BigShip2; ix=0; break;	
					case 2 : aux=BigShip4; ix=83; break;	
					case 3 : aux=BigShip1; ix=0; break;	
					case 4 : aux=BigShip3; ix=0; break;	
					case 5 : aux=BigShip3; ix=83; break;	
					case 6 : aux=BigShip2; ix=83; break;	
					case 7 : aux=BigShip1; ix=83; break;	
					case 8 : aux=BigShip1; ix=166; break;	
					default: aux=null; ix=0; break;
				}			
				check_draw(g,aux,ix,0,83,66,game.sh_offset+(150*i)+(CANVX-83)/2,124+y);
			}
			
			x=Trig.sin(game.cnt<<4)>>9;
			check_draw(g,Fonts,151,49,8,16,20+x,150);
			check_draw(g,Fonts,161,49,8,16,CANVX-28-x,150);
							
			break;
															
			case UltranausMain.COUNTDOWN :			
			show_race(g,game.rc);
			if(game.cnt>=75)
				show_countdown(g,1);			
			else if(game.cnt>=50)
				show_countdown(g,2);			
			else if(game.cnt>=25)
				show_countdown(g,3);			
			break;
			
			case UltranausMain.GAME_ACTION :			
			show_race(g,game.rc);
			//Messages
			if(game.rc.mess_cnt>0){
				show_font_text(g,game.rc.message,114,172,0,99);	
				
			}
			if((game.game_mode==UltranausMain.DEMO) && ((game.cnt/8)%2==0)){
				show_font_text_center(g,Text.any_button,150,0,99);	
			}
						
			if(game.cnt<30)			
				show_countdown(g,0);
			if((game.rc.sh[0].energy<UltraShip.MAX_ENERGY/3) && (game.rc.sh[0].energy>0))
			if(((game.cnt>>2)%2)==0)
				show_countdown(g,4);
			break;
					
			case UltranausMain.RACE_OVER :			
			show_race(g,game.rc);
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(0,0,0);
			g.setFont(fnts);
			if(game.rc.sh[0].energy==0)	
			show_font_text_center(g,Text.wiped,150,1,game.cnt/3);	
			else 
			show_font_text_center(g,Text.finish,150,1,game.cnt/3);	
			
			break;			
			
			case UltranausMain.CRASHED :				
			break;

			case UltranausMain.RESULTS :			
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Console,0,0,20);					
			show_font_text_center(g,""+Text.raceover,20,1,99);
			g.setClip(50,45,27,46);
			g.drawImage(Faces,50-28*(game.pl_ship),45,20);
			show_font_text(g,""+(game.last_rank),100,60,1,99);
			switch(game.last_rank){
				case 1 : show_font_text(g,""+Text.r1,115,60,0,99); break;
				case 2 : show_font_text(g,""+Text.r2,115,60,0,99); break;
				case 3 : show_font_text(g,""+Text.r3,115,60,0,99); break;
				default: show_font_text(g,""+Text.r4,115,60,0,99); break;
			}				
			show_font_text_center(g,Text.bestlap+"  "+game.time_str(game.best_lap),100,0,game.cnt/2);
			for(int i=0; i<game.rc.nracers; i++)
				show_font_text(g,""+(i+1)+" "+player_names[game.rc.sh[game.rc.rank[i]].model],50,120+(10*i),0,(game.cnt/2)-(2*i));
			show_font_text_center(g,""+Text.any_button,185,0,99);								
			
			break;
			
			case UltranausMain.ASKCONT :			
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Console,0,0,20);			
					
			show_font_text_center(g,Text.conti,70,1,99);						
			show_font_text_center(g,""+Integer.toString(9-(game.cnt/33)),95,1,99);				
			show_font_text_center(g,Text.credits+" "+game.credits,150,0,99);				
			break;
			
			case UltranausMain.GAME_OVER :			
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Console,0,0,20);		
			show_font_text_center(g,"game",74,1,game.cnt/4);				
			show_font_text_center(g,"over",96,1,(game.cnt-10)/4);				
			show_font_text_center(g,Text.microjocs,135,0,99);				
			break;						
			
			case UltranausMain.SELECT_COURSE :
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Console,0,0,20);	
			show_font_text_center(g,Text.practice,20,1,game.cnt/2);											
			g.setClip(0,0,CANVX,CANVY);
			ix=(CANVX-(UltraRace.sizesx[game.course%6]*2))/2; 
			iy=(CANVY-(UltraRace.sizesy[game.course%6]*2))/2;
			for(int j=0; j<UltraRace.sizesy[game.course%6]/2; j++)			
			for(int i=0; i<UltraRace.sizesx[game.course%6]/2; i++){
			
				x=ix+4*i; y=iy+4*j;
				
				if(maps[game.course%6][2*((j*UltraRace.sizesx[game.course%6])+i)]!=113)
				g.setColor(0,255,255);				
				else				
				g.setColor(0,74,128);				
				g.fillRect(x,y,3,3);
								
			}			
			x=Trig.sin(game.cnt<<4)>>9;
			if(game.course>0) check_draw(g,Fonts,151,49,8,16,15+x,100);
			if(game.course<game.highest_course) check_draw(g,Fonts,161,49,8,16,CANVX-23-x,100);
			
			if(game.course<9) show_font_text_center(g,Text.stage+" 00"+(game.course+1),180,0,99);														
			else show_font_text_center(g,Text.stage+" 0"+(game.course+1),180,0,99);		
			
			if(game.course>=6) show_font_text(g,"inv",144,56,0,99);																										
						
			break;		
			
			case UltranausMain.RECORDS :
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Console,0,0,20);					
			show_font_text_center(g,Text.best_times,20,1,99);							
			
			iy=3*(int)(game.cnt/100);
			
			for(int i=0; i<3; i++){
				g.setClip(20,45+50*i,27,46);
				g.drawImage(Faces,20-28*(game.r_ships[iy+i]),45+50*i,20);
				if(iy+i+1<10) show_font_text(g,"S00"+(iy+i+1)+"  "+game.time_str(game.r_times[iy+i]),60,50+50*i,0,game.cnt%100);
				else 	      show_font_text(g,"S0"+(iy+i+1)+"  "+game.time_str(game.r_times[iy+i]),60,50+50*i,0,game.cnt%100);
				show_font_text(g,game.r_names[iy+i],60,65+50*i,1,99);
			}																						 								 	 	
			break;	
			
			case UltranausMain.CREDITS :
			clear_display(g,0,0,0);
			g.drawImage(City,0,0,20);
			
			show_font_text_center(g,Text.ultranaus,20,1,99);							
			show_font_text_center(g,Text.coded_by,60,0,99);							
			show_font_text_center(g,Text.david,70,0,99);							
			show_font_text_center(g,Text.graphics_by,95,0,99);						
			show_font_text_center(g,Text.jordi,105,0,99);								
			show_font_text_center(g,Text.microjocs,130,0,99);								
			show_font_text_center(g,Text.web,155,0,99);								
			show_font_text_center(g,Text.thanks,180,0,99);								
									
			break;				
			
			case UltranausMain.ENDING :
			if(game.cnt<50){
				g.drawImage(City,0,0,20);
				if(game.cnt==10) clear_display(g,255,255,255);				
				if(game.cnt>10){
					g.setClip(-10,30,196,152);
					g.drawImage(EndImg,-10-56,30,20);
				}
				if(game.cnt<10) y=CANVY-(10*game.cnt);
				else y=CANVY-100;			
				g.setClip((CANVX-56)/2,y,56,109);
				g.drawImage(EndImg,(CANVX-56)/2,y,20);
				g.setClip(0,0,CANVX,CANVY);
			}else{
				g.setClip(0,0,CANVX,CANVY);
				g.drawImage(Console,0,0,20);					
				g.setClip(74,30,27,46);
				g.drawImage(Faces,74-(28*game.pl_ship),30,20);
				show_font_text_center(g,Text.end1,100,1,game.cnt/2);													
				show_font_text_center(g,Text.end2,130,0,game.cnt/2);													
				show_font_text_center(g,Text.end3,145,0,game.cnt/2);													
				show_font_text_center(g,Text.end4,160,0,game.cnt/2);													
				
			}
			break;
			
			case UltranausMain.HOW_TO_PLAY :
			case UltranausMain.HOW_TO_PLAY2 :
					
			clear_display(g,0,0,0);									
			g.setClip(begx,begy,96,65);
			g.setColor(255,255,255);
			
			switch(game.help_frame){
 				
 					case 0 :
					g.setClip(0,0,CANVX,CANVY);
					g.drawImage(Console,0,0,20);					
					show_font_text_center(g,Text.controls,20,1,game.cnt/2);
					show_font_text(g,Text.cont1,15,90,0,99);
					show_font_text(g,Text.cont2,15,105,0,99);
					show_font_text(g,Text.cont3,15,120,0,99);
					show_font_text(g,Text.cont4,15,135,0,99);													 					
					show_font_text(g,Text.cont5,15,150,0,99);													 					
					show_font_text_center(g,Text.cont6,180,0,game.cnt/2);
					g.setClip(79,50,19,19);
					g.drawImage(ShipImg,79-(19*game.pl_ship),50-210,20);										 										
 					break;	
										
					case 1 :
					g.setClip(0,0,CANVX,CANVY);
					g.drawImage(Console,0,0,20);					
					
					for(int i=0; i<4; i++){
						g.setColor(182,203,221);						
						g.setClip(0,0,CANVX,CANVY);
						g.fillRect(28,38+40*i,17,17);
						g.setClip(30,40+(40*i),13,13);
						g.drawImage(MiscImg,30-193-(16*i),40+(40*i)-49,20);					
					}					
					show_font_text(g,Text.turbo,50,38,0,game.cnt/2);													 										
					show_font_text(g,Text.item1,50,54,0,game.cnt/2);													 										
					
					show_font_text(g,Text.energy,50,78,0,game.cnt/2);													 										
					show_font_text(g,Text.item2,50,94,0,game.cnt/2);													 										
					
					show_font_text(g,Text.shield,50,118,0,game.cnt/2);													 										
					show_font_text(g,Text.item3,50,134,0,game.cnt/2);													 										
					
					show_font_text(g,Text.rocket,50,158,0,game.cnt/2);													 										
					show_font_text(g,Text.item4,50,174,0,game.cnt/2);													 										
										
					break;					
 			}						
			break;
							
		}				
	}		
		
	public void clear_display(Graphics g,int r, int gr, int b)
	{
		g.setClip(0,0,CANVX,CANVY);
		g.setColor(r,gr,b);
		g.fillRect(0,0,CANVX,CANVY);		
	}
		
	public void create_backgrounds(byte[] TileMap, Image TileSet, int sx, int sy)
	{

	}
	
	public static void vibrate(int freq, long time)
	{
	}
	
	public void play_music(int i, int loop)
	{		
		/*String song="";
		
		try{
		
		if(snd!=null) snd.stop();
				
		if(i<2 || i>4){
			
			switch(i){
				case 0 : song=intro_mus; break;	
				case 1 : song=level_mus; break;	
				case 5 : song=win_mus; break;	
				case 6 : song=lose_mus; break;	
			}
			
			mus = new Sound(convertOTA(song), Sound.FORMAT_TONE);
			mus.init(convertOTA(song), 1);				
			if(game.sound && game.game_mode!=UltranausMain.DEMO) mus.play(loop);		
		} else {
			switch(i){
				case 2 : snd=item1_snd; break;	
				case 3 : snd=item2_snd; break;	
				case 4 : snd=item3_snd; break;					
			}						
			if(game.sound && game.game_mode!=UltranausMain.DEMO) snd.play(loop); 			
		}
		
	}catch(Exception e){}*/
			
	}
	
	public void stop_music()
	{
		/*try{
		if(game.sound) mus.stop();				
		}catch(Exception e){}*/
	}
	
		
	/*=====================================================================================*/
			
	public void load_race_gfx(UltraRace rc)
	{
		InputStream is;		
		int ntiles=100;
																																
		System.gc();
	}
	
	public void load_preview_maps()
	{
		InputStream is;
		
		for(int i=0; i<6; i++){
			
			maps[i]=new byte[UltraRace.sizesx[i]*UltraRace.sizesy[i]];
		
			//is=getClass().getResourceAsStream("/sc"+(i+1)+".map");
			readingdata.leerdata("/sc"+(i+1)+".map" ,maps[i],UltraRace.sizesx[i]*UltraRace.sizesy[i]);
		}
	
	}
		
	public void create_city_gfx()
	{
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Image cityTiles=null;
				InputStream is;
				int k,t,t1,t2;
				byte b[]=new byte[116*60*2];

				try{
					cityTiles = new Image();
					cityTiles._createImage("/City.png");

				}catch(Exception err) {}

				//is=getClass().getResourceAsStream("/City.map");
				readingdata.leerdata("/City.map",b,116*60*2);

				//City=Image.createImage(928,480);
				City=new Image();
				City._createImage(728,380);

				Graphics g=City.getGraphics();

				for(int j=0; j<60; j++)
					for(int i=0; i<116; i++){

						k=(j*116)+i;
						t1=b[2*k]; if(t1<0) t1+=256;
						t2=b[(2*k)+1]; if(t2<0) t2+=256;
						t=(t1<<8)+t2;
						g.setClip(i*8,j*8,8,8);
						Graphics._drawImage(g, cityTiles,(i*8)-((t%25)*8),(j*8)-((t/25)*8),20);
					}
				b=null; cityTiles=null; g=null; System.gc();
			}
		});

	}
	
	public void destroy_race_gfx()
	{		
		System.gc();
	}
		
	public void load_misc_gfx()
	{
		System.gc();		
		try{
			MiscImg = Image.createImage("/Misc.png"); 
			
			
		}catch(Exception e) {};
				
		System.gc();
	}
	
	public void destroy_misc_gfx()
	{		
		MiscImg=null; System.gc();
	}		
	
	
	public void load_logo_gfx()
	{
		System.gc();
	}
	
	public void destroy_logo_gfx()
	{				
	}		
	
	public boolean loaded_logo_gfx()
	{		
		return true;
	}
		
	public void load_select_gfx()
	{
		System.gc();
	}
	
	public void destroy_select_gfx()
	{				
	}	
	
	public void load_loading_gfx()
	{
		System.gc();
	}
	
	public void destroy_loading_gfx()
	{		
		LoadImg=null; System.gc();
	}	
	
	public void load_intro_gfx()
	{	
		create_city_gfx();				
		System.gc();					
		loadpr++; repaint();	
		try {	
			TileSet = Image.createImage("/Tiles.png");
		}catch(Exception err) {}
		System.gc();											
		loadpr++; repaint();																			
		try {
			Wallpaper = Image.createImage("/Wallpaper.png"); 			
		}catch(Exception err) {}
		System.gc();																		
		loadpr++; repaint();
		try {
			BigShip1 = Image.createImage("/BigShip1.png"); 
		}catch(Exception err) {}
		System.gc();	
		loadpr++; repaint();																	
		try {
			BigShip2 = Image.createImage("/BigShip2.png"); 
		}catch(Exception err) {}
		System.gc();	
		loadpr++; repaint();																	
		try {
			BigShip3 = Image.createImage("/BigShip3.png"); 
		}catch(Exception err) {}
		System.gc();	
		loadpr++; repaint();																	
		try {
			BigShip4 = Image.createImage("/BigShip4.png"); 
		}catch(Exception err) {}
		System.gc();																		
		loadpr++; repaint();																	
		try {		
			Faces = Image.createImage("/Faces.png"); 			
		}catch(Exception err) {}
		System.gc();																				
		try {					
			IntroImg1 = Image.createImage("/Intro1.png"); 		
		}catch(Exception err) {}
		System.gc();
		loadpr++; repaint();																	
						
	}
	
	public void destroy_intro_gfx()
	{		
		IntroImg1=null;  System.gc(); System.gc();
		
		load_misc_gfx();				
		create_console();		
		try {	
			ShipImg = Image.createImage("/Ships.png"); 			
		}catch(Exception err) {}
		System.gc();								
		try {	
			Stats = Image.createImage("/Stats.png"); 	
		}catch(Exception err) {}
		System.gc();								
		try {			
			Arrows = Image.createImage("/Arrows.png"); 															
		}catch(Exception err) {}
		System.gc();
				
		load_preview_maps();	
		System.gc();
		
	}	
	
	public void load_crashed_gfx()
	{
		System.gc();		
	}
	
	public void destroy_crashed_gfx()
	{		
		System.gc();
	}	
	
	public void load_results_gfx()
	{
		System.gc();
	}
	
	public void destroy_results_gfx()
	{		
		SelImg=null; ResultImg=null; System.gc();
	}	
	
	public void load_gameover_gfx()
	{
		System.gc();
	}
	
	public void destroy_gameover_gfx()
	{		

	}		
	
	public void load_selectcourse_gfx()
	{
		System.gc();

	}
	
	public void destroy_selectcourse_gfx()
	{		
	}		
		
	public void load_ending_gfx()
	{
		System.gc();
		try {
			EndImg = Image.createImage("/Ending.png"); 			
						
		}catch(Exception err) {}
		System.gc();
	}
	
	public void destroy_ending_gfx()
	{		
		EndImg=null; System.gc();
	}			
	
	public void create_console()
	{
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				try {
					WindowTile = new Image();
					WindowTile._createImage("/WindowTile.png");
					Border = new Image();
					Border._createImage("/Border.png");

				} catch (Exception err) {}

				Console=new Image();
				Console._createImage(CANVX,CANVY);
				Graphics g=Console.getGraphics();
				g.setClip(0,0,CANVX,CANVY);
				for(int i=0; i<=CANVX/24; i++)
					for(int j=0; j<=CANVY/24; j++)
						g._drawImage(g, WindowTile,i*24,j*24,20);
				g.setClip(0,0,CANVX,10);
				g._drawImage(g, Border,0,0,20);
				g.setClip(0,CANVY-10,CANVX,10);
				g._drawImage(g, Border,0,CANVY-10-14,20);
				for(int j=10; j<=CANVY-10; j+=4){
					g.setClip(0,j,CANVX,4);
					g._drawImage(g, Border,0,j-10,20);
				}
				g=null;
				WindowTile=null;
				Border=null;
				System.gc();
			}
		});
	}
	
	public void show_font_text(Graphics g, String s, int px, int py, int fnt, int max)
	{
		char c[];
		int i, let=0, x=0, begy=0, sy=0;
		int begx[][]={{0,7,13,20,26,33,39,46,52,57,63,70,76,83,89,96,102,109,115,122,128,135,141,148,154,160,166,0,7,11,18,24,31,37,44,50,57,63},
			      {0,20,39,58,78,97,116,135,154,159,177,197,213,239,259,0,20,39,59,79,98,117,136,154,180,199,218,239,1,17,24,40,55,71,86,103,116,132,146}};
		
				
		c=s.toLowerCase().toCharArray();
		
		i=0;
		while(i<c.length && i<max){
			
			switch(fnt){
				case 0 :
				sy=8;
				if(c[i]>='a' && c[i]<='z'){
					let=(int)(c[i]-'a'); begy=74;
				}
				
				else if(c[i]>='0' && c[i]<='9'){
					let=(int)(c[i]-'0'+('z'-'a')+2); begy=83;
				}else let=9999;
				break;
				
				case 1 :
				sy=15;
				if(c[i]>='a' && c[i]<='n'){
					let=(int)(c[i]-'a'); begy=93;
				}else
				
				if(c[i]>='o' && c[i]<='z'){
					let=(int)(c[i]-'a'+1); begy=108;
				}else
								
				if(c[i]>='0' && c[i]<='9'){
					let=(int)(c[i]-'0'+('z'-'a')+3); begy=125;
				}else let=9999;
				break;
			}				
			begy-=75;
			if(let!=9999){
				g.setClip(px+x,py,begx[fnt][let+1]-begx[fnt][let],sy);
				g.drawImage(Fonts,px+x-begx[fnt][let],py-begy,20);				
				x+=begx[fnt][let+1]-begx[fnt][let];
			} else x+=6;
			i++;			
		}
				
	}
	
	void show_font_text_center(Graphics g, String s, int py, int fnt, int max)
	{
		char c[];
		int sx=0, i=0, let=0;
		int begx[][]={{0,7,13,20,26,33,39,46,52,57,63,70,76,83,89,96,102,109,115,122,128,135,141,148,154,160,0,7,11,18,24,31,37,44,50,57,63},
			      {0,20,39,58,78,97,116,135,154,159,177,197,213,239,259,0,20,39,59,79,98,117,136,154,180,200,219,1,17,24,40,55,71,86,103,116,132,146}};
				
		c=s.toLowerCase().toCharArray();
		
		while(i<max && i<c.length){
			
			switch(fnt){
				case 0 :				
				if(c[i]>='a' && c[i]<='y') let=(int)(c[i]-'a');							
				if(c[i]>='0' && c[i]<='9') let=(int)(c[i]-'0'+('y'-'a')+2);				
				break;
				
				case 1 :				
				if(c[i]>='a' && c[i]<='n') let=(int)(c[i]-'a'); 								
				if(c[i]>='o' && c[i]<='y') let=(int)(c[i]-'a'+1); 												
				if(c[i]>='0' && c[i]<='9') let=(int)(c[i]-'0'+('y'-'a')+3);								
				break;
			}				
			
			sx+=begx[fnt][let+1]-begx[fnt][let];
			i++;
		}
		show_font_text(g,s,(CANVX-sx)/2,py,fnt,max);			
	}
				
	public void show_race(Graphics g, UltraRace r)
	{
		int scx, scy, px, py, ix=0, sx=0;
						
		// Mantain window		
		scx=(((36*r.sh[0].realx)-(36*r.sh[0].realy))>>13)-(CANVX/2);
		scy=(((12*r.sh[0].realx)+(12*r.sh[0].realy))>>13)-(CANVY/2);
									
	
		// Background
		clear_display(g,0,0,0);
		//g.drawImage(City,-300-scx/4,-scy/4,20);
		
		check_draw(g,City,0,0,728,480,-600-scx/4,100-scy/4);
		check_draw(g,City,0,0,728,480,-600+728-scx/4,100-scy/4);
		
		
		show_course(g,r,scx,scy);
		
		
			
		// Items
		for(int i=0; i<UltraRace.N_ITEMS; i++)
			if(r.itemt[i]!=0){
				
				px=(((36*r.itemx[i])-(36*r.itemy[i]))>>0)-scx-7;
				py=(((12*r.itemx[i])+(12*r.itemy[i]))>>0)-scy+13;
								
				check_draw(g,MiscImg,257,56,13,8,px,py+10);
																
				check_draw(g,MiscImg,193+(16*(r.itemt[i]-1)),49,13,13,px,py-4-(Trig.sin(game.cnt*8)>>8));
				
				switch(r.itemt[i]-1){
					case 0 : ix=192; sx=28; break;
					case 1 : ix=221; sx=33; break;
					case 2 : ix=255; sx=28; break;
					case 3 : ix=284; sx=25; break;	
				}
				check_draw(g,MiscImg,ix,32,sx,10,px+10,py-20);
			}
		
		// Ships
		if(game.state!=UltranausMain.COUNTDOWN || game.cnt>60 || (game.cnt/4)%2==0) 
			show_ship(g,r.sh[0],scx,scy);	
		for(int i=1; i<r.nracers; i++)
			show_ship(g,r.sh[i],scx,scy);	
		for(int i=0; i<r.nracers; i++)
			show_ship_explosions(g,r.sh[i],scx,scy);	
			
		// Rockets
		for(int i=0; i<UltraRace.N_ROCKETS; i++)
			if(r.rk[i].in_use) show_rocket(g,r.rk[i],scx,scy);
									
		show_bars(g,r);					
	}
	
	void show_bars(Graphics g, UltraRace r)
	{
		int ix, iy, sx, v;
		
		// Bars
		g.setClip(0,0,CANVX,40);
		g.drawImage(MiscImg,0,-219,20);
		g.setClip(0,CANVY-40,CANVX,40);
		g.drawImage(MiscImg,-177,CANVY-40-219,20);
		// Me
		g.setClip(0,0,27,40);
		g.drawImage(Faces,-28*r.sh[0].model,0,20);
		show_font_text(g,""+(r.sh[0].energy/2),28,30,0,99);	
		show_font_text(g,"P"+(r.sh[0].rank+1),29,6,1,99);	
		// Opponent
		if(r.sh[0].opp!=null){
			g.setClip(CANVX-27,0,27,40);
			g.drawImage(Faces,CANVX-26-(28*r.sh[0].opp.model)-1,0,20);
			show_font_text(g,""+(r.sh[0].opp.energy>>1),128,30,0,99);	
			show_font_text(g,"P"+(r.sh[0].opp.rank+1),135,7,0,99);	
		}
		v=r.sh[0].laps;		
		show_font_text(g,Integer.toString(v),87,177,1,99);			
		if(game.game_mode!=UltranausMain.PRACTICE) show_font_text(g,""+(UltranausMain.N_LAPS),105,184,0,99);	

		// Taken items
		
		if(r.sh[0].turbo>0){			
			g.setClip(120,195,13,13);
			g.drawImage(MiscImg,120-193,195-49,20);
		}

		if((game.rc.sh[0].energy<UltraShip.MAX_ENERGY/3) && (game.rc.sh[0].energy>0))
		if(((game.cnt>>2)%2)==0){			
	
			g.setClip(134,195,13,13);
			g.drawImage(MiscImg,134-209,195-49,20);
			
		}
			
		if(r.sh[0].shield>0){			
			g.setClip(148,195,13,13);
			g.drawImage(MiscImg,148-225,195-49,20);
			
		}
			
		if(r.sh[0].rocket>0){			
			g.setClip(162,195,13,13);
			g.drawImage(MiscImg,162-241,195-49,20);
		}			
			
		// Speed 
				
		v=(Math.abs(r.sh[0].velx)*Math.abs(r.sh[0].velx)+Math.abs(r.sh[0].vely)*Math.abs(r.sh[0].vely))/200000;
						
		g.setClip(26,193,9,11);
		g.drawImage(MiscImg,26-193-(9*((v/100)%10)),193-65,20);
		g.setClip(34,193,9,11);
		g.drawImage(MiscImg,34-193-(9*((v/10)%10)),193-65,20);
		g.setClip(42,193,9,11);
		g.drawImage(MiscImg,42-193-(9*(v%10)),193-65,20);
		
		switch(Math.abs(r.sh[0].acc*2)){
			case 0 : ix=0; iy=160; sx=15; break;	
			case 1 : ix=16; iy=160; sx=16; break;	
			case 2 : ix=33; iy=160; sx=16; break;	
			case 3 : ix=50; iy=160; sx=19; break;	
			case 4 : ix=70; iy=160; sx=21; break;	
			case 5 : ix=92; iy=160; sx=23; break;	
			case 6 : ix=116; iy=160; sx=33; break;	
			case 7 : ix=150; iy=160; sx=37; break;	
			case 8 : ix=188; iy=160; sx=41; break;	
			case 9 : ix=230; iy=160; sx=49; break;	
			case 10 : ix=280; iy=160; sx=54; break;	
			case 11 : ix=335; iy=160; sx=59; break;	
			case 12 : ix=0; iy=189; sx=65; break;	
			case 13 : ix=66; iy=189; sx=72; break;	
			default : ix=139; iy=189; sx=77; break;	
		}
		
		g.setClip(5,172,sx,29);
		g.drawImage(MiscImg,5-ix,172-iy,20);
		
	}
	
	public void show_course(Graphics g, UltraRace r, int scx, int scy)
	{
		int px, py, spx, spy, t2, rad=5, sy, arrow=-1;
		byte t;
				
		int mi=(r.sh[0].x)/8, mj=(r.sh[0].y)/8;
						
		for(int i=mi-rad; i<mi+rad; i++)
		for(int j=mj-rad; j<mj+rad; j++)			
			if(i>=0 && i<r.sizex && j>=0 && j<r.sizey){
			
				px=(36*i)-(36*(j+1))-scx;
				py=(12*i)+(12*(j+1))-scy;
				
				t=r.TileMap[i+(r.sizex*j)];			
				
				t2=t-((t/16)*9);
				if(t>=80) t2=10;
				spx=(t2%7)*72;
				spy=(t2/7)*40;
									
						
				if(t!=113){										
					if(t2==3 || t2==4 || t2==6 || t2==6 || t2==11 || t2==13 || t2==19 || t2==20 || t2==22 || t2==23 || t2==24 || t2==25 || t2==30) sy=40;
					else sy=23;					
					check_draw(g,TileSet,spx,spy,72,sy,px,py);
					// Ground arrows
					if(game.course<6)
					switch(t){
						case 80 : arrow=0; break;
						case 81 : arrow=1; break;
						case 82 : arrow=2; break;
						case 83 : arrow=3; break;
						case 96 : arrow=4; break;
						case 97 : arrow=5; break;
						case 98 : arrow=6; break;
						case 99 : arrow=7; break;												
					}	
					if(game.course>=6)							
					switch(t){
						case 84 : arrow=0; break;
						case 85 : arrow=1; break;
						case 86 : arrow=2; break;
						case 87 : arrow=3; break;
						case 100 : arrow=4; break;
						case 101 : arrow=5; break;
						case 102 : arrow=6; break;
						case 103 : arrow=7; break;												
					}								
				}
			
		}	
			
		// Arrow
		if((game.cnt/3)%2==0 && game.state!=UltranausMain.COUNTDOWN)
		switch(arrow){
			case 0 : check_draw(g,Arrows,25,0,32,12,72,50); break;
			case 1 : check_draw(g,Arrows,58,0,24,12,76,50); break;
			case 2 : check_draw(g,Arrows,0,0,24,12,76,50); break;
			case 3 : check_draw(g,Arrows,83,0,32,12,72,50); break;
			case 4 : check_draw(g,Arrows,0,13,32,12,72,50); break;
			case 5 : check_draw(g,Arrows,33,13,24,12,76,50); break;
			case 6 : check_draw(g,Arrows,58,13,24,12,76,50); break;
			case 7 : check_draw(g,Arrows,83,13,32,12,72,50); break;						
		}					
		
	}	
	
	public void check_draw(Graphics g, Image im, int ix, int iy, int sx, int sy, int px, int py)
	{
		if(px<CANVX && py<CANVY-40 && px+sx>=0 && py+sy>=40){
			g.setClip(px,py,sx,sy);
			g.drawImage(im,px-ix,py-iy,20);	
		}
		
	}
	
	public void show_number(Graphics g, int x, int y, int n)
	{
		g.setClip(x,y,7,5);
		g.drawImage(MiscImg,x-32-(((n/10)%10)*8),y-90,20);	
		g.setClip(x+5,y,7,5);
		g.drawImage(MiscImg,x+5-32-((n%10)*8),y-90,20);	
	}
	
	public void show_ship(Graphics g, UltraShip s, int scx, int scy)
	{
		int px, py, pos, pos2, an, ix=0, sx=0;
		
		px=(((36*s.realx)-(36*s.realy))>>13)-scx;
		py=(((12*s.realx)+(12*s.realy))>>13)-scy;
		
			
		// Which pos (orientation) of the ship
		
		an=(s.angle+32+11)%256; 
		pos=(10*an)/213;

		// Show shade		
		check_draw(g,ShipImg,171,(pos*19),19,19,px,py+4);
		
		// Show ship		
		check_draw(g,ShipImg,(s.model*19),(pos*19),18,19,px,py);
						
		// Show shield
		if(s.shield>0)
		if((s.shield>16) || ((s.shield<=16) && ((s.shield>>1)%2==0))){			
			switch(s.shield%5){
				case 0 : ix=2; break;	
				case 1 : ix=35; break;	
				case 2 : ix=64; break;	
				case 3 : ix=94; break;	
				case 4 : ix=128; break;	
			}
			check_draw(g,MiscImg,ix,97,30,30,px-5,py-5);
		}
				
		switch((8-pos)%8){
			case 0 : px-=8; break;
			case 1 : px-=6; py-=6; break;
			case 2 : py-=8; break;
			case 3 : px+=6; py-=6; break;
			case 4 : px+=8; break;
			case 5 : px+=6; py+=6; break;
			case 6 : py+=8; break;
			case 7 : px-=6; py+=6; break;
		}
		if(!s.collided && s.acc>0){
		}

	}	

	public void show_ship_explosions(Graphics g, UltraShip s, int scx, int scy)
	{
		int px, py, pos, y;
		
		px=(((36*s.realx)-(36*s.realy))>>13)-scx;
		py=(((12*s.realx)+(12*s.realy))>>13)-scy;
						
		// Show explosion
		if(s.state==UltraShip.EXPLODE){
			check_draw(g,MiscImg,5+(32*(s.cnt%6)),36,22,22,px-1,py-1);
		}
		
		// Show explosion
		if(s.state==UltraShip.BURNING)
		
			for(int i=0; i<6; i++){
				
				// Flying balls of smoke
				y=(s.cnt-(i<<3))%48;
				pos=(Trig.sin((s.cnt-(i<<3))<<3)>>8);
				if(y>0)
				if(y<42){
					check_draw(g,MiscImg,5,70,22,22,px-1+pos,py-1-y);
				}else{
					check_draw(g,MiscImg,5+(32*(y-42)),70,22,22,px-1,py-1-48);
				}				
			}		
	}	
	
	public void show_rocket(Graphics g, UltraRocket r, int scx, int scy)
	{
		
		int px, py, an, pos;		
		
		px=(((36*r.realx)-(36*r.realy))>>13)-scx;
		py=(((12*r.realx)+(12*r.realy))>>13)-scy;
				
		if(r.state==UltraRocket.FLYING){
		
			an=(r.angle+32+11)%256; 
			pos=(10*an)/213;
			
			check_draw(g,MiscImg,16*pos,16,16,16,px,py+10);
			
			check_draw(g,MiscImg,16*pos,0,16,16,px,py);
		}
		
		if(r.state==UltraRocket.EXPLODE){
			
			check_draw(g,MiscImg,5+(32*(r.cnt%6)),36,22,22,px-1,py-1);
		}
	}

	public void show_countdown(Graphics g, int i)
	{
		switch(i){
			
			case 3 :
			g.setClip((CANVX-32)>>1,(CANVY-22)>>2,32,22);
			g.drawImage(MiscImg,(CANVX-32)>>1,((CANVY-22)>>2)-136,20);
			break;
			
			case 2 :
			g.setClip((CANVX-32)>>1,(CANVY-22)>>2,32,22);
			g.drawImage(MiscImg,((CANVX-32)>>1)-32,((CANVY-22)>>2)-136,20);
			break;
			
			case 1 :
			g.setClip((CANVX-12)>>1,(CANVY-22)>>2,12,22);
			g.drawImage(MiscImg,((CANVX-12)>>1)-66,((CANVY-22)>>2)-136,20);
			break;
			
			case 0 :
			g.setClip((CANVX-74)>>1,(CANVY-22)>>2,72,22);
			g.drawImage(MiscImg,((CANVX-74)>>1)-80,((CANVY-22)>>2)-136,20);
			break;
			
			case 4 :
			g.setClip((CANVX-112)>>1,148,112,16);
			g.drawImage(MiscImg,((CANVX-112)>>1)-160,148-142,20);
			break;
			
			
		}		
	}
	
	void show_big_number(Graphics g, int x, int y, int n)
	{
		int ix=0, sx=1;	
		
		switch(n){
			
			case 1 : ix=46; sx=9; break;
			case 2 : ix=57; sx=23; break;
			case 3 : ix=82; sx=23; break;
			case 4 : ix=107; sx=23; break;
			case 5 : ix=132; sx=23; break;
			case 6 : ix=157; sx=23; break;
			case 7 : ix=182; sx=18; break;
			case 8 : ix=202; sx=23; break;
			case 9 : ix=227; sx=23; break;			
		}
		
		g.setClip(x-(sx/2),y,sx,23);
		g.drawImage(ResultImg,x-(sx/2)-ix,y-2,20);		
	}
		
}