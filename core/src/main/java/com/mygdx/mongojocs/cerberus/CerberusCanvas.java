package com.mygdx.mongojocs.cerberus;

/*
	Canvas for Nokia 60 Series 
*/

import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.util.Random;



class CerberusCanvas extends FullCanvas
{
						
	Font fnts;
	public Control ctrl;	
	Random rnd;
	CerberusMain game;	
	Image Bars, TileSet, BackGround, Sky, Misc, Moon, Rain, Clouds, Player, Armor, Enemy, Paper, SFont, BFont,
		SwampMap, CavesMap, ForestMap, CastleMap, SamuelPic, Title, Grid, Microjocs,
		SwampT, SwampCave, SwampPlants, SwampMisc, SwampEnemy, CavesT, CavesWall, CavesLava, CavesEnemy,
		ForestT, ForestTree, ForestEnemy, Clouds2, CastleT, CastleEnemy;
		
	public static int CANVX, CANVY, BEGINY, SCREENY; 
	private int bgsx, bgsy, bgtx, bgty, bginy, load_percent=0;
	boolean moon_on, clouds_on, sky_on, aux_tm_on, flash_bkg_on, bkg_scroll_on, bkg_on, storm_on, rain_on, thin_clouds_on; 
	byte AuxTileMap[];
	public boolean painting;
	public static final int font_sizex[][]={{4,2,4,4,4,4,4,4,4,4,3,3,3,3,3,4,5,5,5,5,6,5,5,6,7,2,2,6,3,7,6,6,5,6,5,4,5,6,6,8,5,5,5,3,6,3,3,3},
						{7,5,8,8,7,8,7,8,7,7,3,4,4,6,4,7,10,10,9,9,9,9,9,9,8,5,8,9,7,11,9,10,8,10,10,9,10,9,9,13,10,8,9,5,10,4,6,5}};
						
	public static final int HEAD=0;
	public static final int BODY=1;
	public static final int ARMS=2;
	public static final int LEGS=3;
	public static final int CAPE=4;
	public static final int WEAPON=5;
	
	public static final int castle_dat[]={9421,3825,3450,895},
				caves_dat[]={5763,1505,1105,2378,1339},
				forest_dat[]={3736,1158,2949,2378},
				misc_dat[]={772,914,1854,768,167,247,551,1238,2689,481,648},
				player_dat[]={12436,9889},
				swamp_dat[]={1724,5141,1017,843,2797,2808};
	int CheatPos_1=0;
	boolean Cheat_ON=false;
	
	public String introOTA = "024A3A71C1C995CD95B9D1858DA5BDB80400C716A22C24C22C24C22C24C22C24C26C28C26C28C26C28C26C28C22D290498818828C40C40BC0BC09C09C0BC0BC0C308B08C09C0A408C09C0A40B409C0A40B40C40A40B40C40CC0B408C09C0A408C09C0A40B409C0A40B409C0A40B409C08C08B09C0A408C09C0A40B409C0A40B40C40A40B40C40CC0B408C09C0A408C09C0A40B409C0A40B409C0A40B40C40D40E409C08A0000",
				  mainThOTA= "024A3A653585A5B97DD195B5940400A7266184D86986186D06184E05A06106906186986185986106184D86986186D06184E05A06106906186986185986106184D8618618A2884986146186186289A12610618618A22849B6288B126D86986D0A268498613618618628A212618518610A26849861861841B41861B61A6166182000",
				  levelOTA = "024A3A55B195D995B00400172061A6206D06A0A23049A81681840000",
				  itemOTA =  "024A3A51A5D195B404000F26831039049581C80000",
				  armorItOTA="024A3A75A5D195B57D85C9B58591D5C9840400132482B031039049581381580000",
				  bossOTA =  "024A3A75B585D185C88195B995B5A59DBC040017226A856083542B0491820D40AC0000",
				  dieOTA =   "024A3A51B5BDC9D0040015224608390350495811820D4000";
	Music snd[] = new Music[3];
				  				  					
	public CerberusCanvas(CerberusMain main)
	{		
		game=main;	
		
		fnts=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD , Font.SIZE_SMALL);		

		CANVX=getWidth(); CANVY=getHeight(); SCREENY=144;	
		BEGINY=(CANVY-SCREENY)/2;
			
																							
		ctrl=new Control();
		
		try{			
			Microjocs = new Image();
			Microjocs._createImage("/Microjocs.png");
			/*byte buffer[] = multiread("/Misc.dat",misc_dat,5);
			Microjocs = Image.createImage(buffer,0,buffer.length);*/ 					
			
		}catch(Exception err) {}

		snd[0] = LoadMidi("/intro.mid");
		snd[1] = LoadMidi("/main.mid");
		snd[2] = LoadMidi("/level.mid");
	}

	public Music LoadMidi(String Name)
	{

		return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Name));
	}


	public static int canvx()
	{
		return CANVX;
	}
	
	public static int screeny()
	{
		return SCREENY;
	}
	
	public static int sizex(CerberusWorld w)
	{
		return w.sx;
	}
	
	public static int sizey(CerberusWorld w)
	{
		return w.sy;
	}
		
	public void load_intro_gfx()
	{
		byte buffer[];
		load_percent++; repaint();
						
		try{
		
			SamuelPic = Image.createImage("/SamuelPic.png");
			Grid = Image.createImage("/Grid.png");
			Title = Image.createImage("/Title.png");
			SFont = Image.createImage("/SmallFnt.png");
			BFont = Image.createImage("/BigFnt.png");
			Bars = Image.createImage("/Bars.png");
		
		}catch(Exception e) {}
																	
	}
	
	public Image loadImageFromFile(String filename, int lengths[], int id)
	{
		byte buffer[];
		Image im=null;
		
		buffer = multiread(filename,lengths,id);
		/*try{
			im = Image.createImage(buffer,0,buffer.length); 				
		}catch(Exception e){*/
			
				im = Image.createImage(2,2);
			
		//}
		
		buffer = null; System.gc();
		
		return im;
	} 
	
	public void load_all_gfx()
	{
		InputStream is;		
		Graphics g;
		byte buffer[];
		
		repaint(); serviceRepaints();
		
		try {
			//Paper = loadImageFromFile("/Misc.dat",misc_dat,7);
			Paper = Image.createImage("/Paper.png");


			load_percent++;
			repaint();
			serviceRepaints();
		
		/*SwampMap = loadImageFromFile("/Swamp.dat",swamp_dat,2); 						
		CavesMap = loadImageFromFile("/Caves.dat",caves_dat,2); 						
		ForestMap = loadImageFromFile("/Forest.dat",forest_dat,1); 						
		CastleMap = loadImageFromFile("/Castle.dat",castle_dat,1); 						
		Moon = loadImageFromFile("/Misc.dat",misc_dat,6);*/

			SwampMap = Image.createImage("/SwampMap.png");
			CavesMap = Image.createImage("/CavesMap.png");
			ForestMap = Image.createImage("/ForestMap.png");
			CastleMap = Image.createImage("/CastleMap.png");
			Moon = Image.createImage("/Moon.png");

			load_percent++;
			repaint();
			serviceRepaints();

			//Player = loadImageFromFile("/Player.dat",player_dat,1);

			Player = Image.createImage("/Samuel.png");

			buffer = null;
			System.gc();
			load_percent++;
			repaint();
			serviceRepaints();

			//Armor = loadImageFromFile("/Player.dat",player_dat,0);

			Armor = Image.createImage("/Armor.png");

			buffer = null;
			System.gc();
			load_percent++;
			repaint();
			serviceRepaints();
							
		
		/*SwampT = loadImageFromFile("/Swamp.dat",swamp_dat,5); 					
		SwampPlants = loadImageFromFile("/Swamp.dat",swamp_dat,4); 					
		SwampCave = loadImageFromFile("/Swamp.dat",swamp_dat,0); 					
		SwampMisc = loadImageFromFile("/Swamp.dat",swamp_dat,3); 					
		Clouds = loadImageFromFile("/Misc.dat",misc_dat,2); 					
		SwampEnemy = loadImageFromFile("/Swamp.dat",swamp_dat,1); */

			SwampT = Image.createImage("/SwampT.png");
			SwampPlants = Image.createImage("/SwampPlants.png");
			SwampCave = Image.createImage("/SwampCave.png");
			SwampMisc = Image.createImage("/SwampMisc.png");
			Clouds = Image.createImage("/Clouds.png");
			SwampEnemy = Image.createImage("/SwampEnemy.png");

			load_percent++;
			repaint();
			serviceRepaints();
							
		/*CavesT = loadImageFromFile("/Caves.dat",caves_dat,3); 						
		CavesWall = loadImageFromFile("/Caves.dat",caves_dat,4); 						
		//CavesLava = loadImageFromFile("/Caves.dat",caves_dat,1); 						
		CavesEnemy = loadImageFromFile("/Caves.dat",caves_dat,0); */

			CavesT = Image.createImage("/CavesT.png");
			CavesWall = Image.createImage("/CavesWall.png");
			CavesLava = Image.createImage("/CavesLava.png");
			CavesEnemy = Image.createImage("/CavesEnemy.png");

			load_percent++;
			repaint();
			serviceRepaints();
						
		/*ForestT = loadImageFromFile("/Forest.dat",forest_dat,2); 						
		ForestTree = loadImageFromFile("/Forest.dat",forest_dat,3); 						
		ForestEnemy = loadImageFromFile("/Forest.dat",forest_dat,0); 						
		Clouds2 = loadImageFromFile("/Misc.dat",misc_dat,3); */

			ForestT = Image.createImage("/ForestT.png");
			ForestTree = Image.createImage("/ForestTree.png");
			ForestEnemy = Image.createImage("/ForestEnemy.png");
			Clouds2 = Image.createImage("/Clouds2.png");

			load_percent++;
			repaint();
			serviceRepaints();
							
		/*CastleT = loadImageFromFile("/Castle.dat",castle_dat,2); 						
		CastleEnemy = loadImageFromFile("/Castle.dat",castle_dat,0); 						
		//Rain = loadImageFromFile("/Castle.dat",castle_dat,3);*/

			CastleT = Image.createImage("/CastleT.png");
			CastleEnemy = Image.createImage("/CastleEnemy.png");
			Rain = Image.createImage("/Rain.png");

			load_percent++;
			repaint();
			serviceRepaints();

			Thread.sleep(100);

			Gdx.app.postRunnable(new Runnable() {

				public void run() {

					Sky = new Image();
					Sky._createImage(CANVX, SCREENY);
					// Create sky
					Graphics gg = Sky.getGraphics();

					gg.setColor(0, 3, 29);
					gg.fillRect(0, 64, CANVX, SCREENY - 64);

					for (int i = 0; i < CANVX; i += 32) {

						gg.setClip(i, 0, 32, 64);
						Graphics._drawImage(gg, SwampMisc, i, 0, 20);
					}

					gg.setClip(0, 22, 175, SCREENY);
					Graphics._drawImage(gg, Clouds, 0, 22, 20);
				}
			});
				
		int map_offset[]={1088,576,1032,632,810,588,400,448,448};
		AuxTileMap=multiread("/Maps.map",map_offset,8);

									
		System.gc(); load_percent++; repaint(); serviceRepaints();
		
		}catch(Exception e){}
											
	}
			
	public void keyPressed(int keycode)
	{
	
		ctrl.reset();
				
		switch(keycode)
		{
			case Canvas.KEY_NUM1 : ctrl.up=true; ctrl.left=true; break;
			case Canvas.KEY_NUM2 : ctrl.up=true; break;
			case Canvas.KEY_NUM3 : ctrl.up=true; ctrl.right=true; break;
			case Canvas.KEY_NUM4 : ctrl.left=true; break;
			case -7 :
			case Canvas.KEY_NUM5 : ctrl.but1=true; break;
			case Canvas.KEY_NUM6 : ctrl.right=true; break;
			case Canvas.KEY_NUM7 : ctrl.down=true; ctrl.left=true; break;
			case Canvas.KEY_NUM8 : ctrl.down=true; break;
			case Canvas.KEY_NUM9 : ctrl.down=true; ctrl.right=true; break;
			case Canvas.KEY_NUM0 : ctrl.but2=true; break;
			case -6 :
			case Canvas.KEY_STAR : ctrl.but3=true; break;
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
		
		CheatRUN(keycode-'0');
	}
	
	public void keyReleased(int keycode)
	{
		ctrl.reset();
	}
	
	public void play_music(int id, int loop)
	{
		String str=null;

		if(!game.sound) return;

		if(id<3){
			try
			{

				Music mus = snd[id];
				mus.setLooping(loop==0);
				mus.play();
			}
			catch(Exception exception) {exception.printStackTrace();}
		}
	}	
	
	public void play_sound(int id)
	{
		/*try{
			itemSnd.stop();
			armorItSnd.stop();
			if(!game.sound) return;
			if(id==0) itemSnd.play(1);
			if(id==1) armorItSnd.play(1);
		}catch (Exception e) {}*/
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
		String s, rankstr[]={"F","E","D","C","B","A"};
		painting=true;
		int x=0, y=0, sx=0, sy=0, c;
		String def[]=null, id=null;
		Image Im=Player;
		boolean cond=true;
		
		//try{
																
		if(game.cnt>1)						
		switch(game.state) {
			
			case CerberusMain.PRESENTS :
			clear_display(g,0,0,0);
			show_sprite_noclip(g,Microjocs,0,0,101,16,37,95,false);
			break;
			
			case CerberusMain.INTRO :
			clear_display(g,0,0,0);
			g.setClip(20,5,127,112);
			g.drawImage(SamuelPic,20,5,20);			
			if(game.cnt<17) show_fade(g,20,5,150,120,6-((game.cnt-5)/2));
			show_sprite_noclip(g,Title,0,0,113,29,31,-120+(2*game.cnt),false);			
			break;
			
			case CerberusMain.LOAD_GFX :
			clear_display(g,0,0,0);
			g.setClip(20,5,127,112);
			g.drawImage(SamuelPic,20,5,20);
			show_sprite_noclip(g,Title,0,0,113,29,31,80,false);									
			// Loading Bar
			g.setClip(43,148,90,10);
			g.drawImage(Bars,43,148,20);
			g.setClip(43,148,10*load_percent,10);
			g.drawImage(Bars,43,148-10,20);
			g.setClip(0,0,CANVX,CANVY);
			break;

			case CerberusMain.MAIN_MENU :
			clear_display(g,0,0,0);
			g.setClip(20,5,127,112);
			g.drawImage(SamuelPic,20,5,20);
			show_sprite_noclip(g,Title,0,0,113,29,31,80,false);
			show_paper(g,120,0);
			switch(game.menu_opt){
				case 1 : show_font_text_center(g,Text.newgame,158,0); break;
				case 0 : show_font_text_center(g,Text.loadgame,158,0); break;
				case 2 : if(game.sound) show_font_text_center(g,Text.sound_on,158,0);
					 else show_font_text_center(g,Text.sound_off,158,0); break;
				case 3 : show_font_text_center(g,Text.controls,158,0); break;
				case 4 : show_font_text_center(g,Text.credits,158,0); break;
				case 5 : show_font_text_center(g,Text.exit,158,0); break;
			}
			show_sprite_noclip(g,Bars,77,21,13,10,25+(Trig.sin(game.cnt<<3)>>8),158,false); 
			show_sprite_noclip(g,Bars,63,21,13,10,CANVX-48-(Trig.sin(game.cnt<<3)>>8),158,false); 							
			//REMOVE
			//	g.setClip(0,0,CANVX,CANVY);
			//	g.drawImage(Sky,0,0, 20);

			break;
			
			case CerberusMain.STORY :
			clear_display(g,0,0,0);
			
			c=0;
			for(int i=0; i<3; i++) if(game.cleared[i]) c++;
			
			for(int i=0; i<Text.prologue[c].length; i++)
				if(CANVY+15*i-game.cnt<CANVY && CANVY+15*i-game.cnt>80) show_font_text(g,Text.prologue[c][i],20,CANVY+30+15*i-game.cnt,0);				
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(0,0,0);
			g.fillRect(0,0,CANVX,120);
			
			switch(c){
				case 0 :
				g.setClip(20,5,127,112);
				g.drawImage(SamuelPic,20,5,20);
				break;
				
				case 1 :
				show_sprite_noclip(g,CastleEnemy,217,0,48,60,20,33,false);
				show_sprite_noclip(g,CastleEnemy,274,63,49,53,60,40,false);
				show_sprite_noclip(g,CastleEnemy,245,71,27,31,50,16,false);
				show_sprite_noclip(g,CastleEnemy,93,136,28,39,67,10,false);
				show_sprite_noclip(g,CastleEnemy,245,102,30,35,80,14,false);
				show_sprite_noclip(g,CastleEnemy,119,0,31,60,97,33,false);
				break;
				
				case 2 :
				show_sprite(g,Armor,241,170,26,35,31+41,104-50,false); 
				show_sprite(g,Armor,104,177,23,18,36+41,101-50,false); 			
				show_sprite(g,Armor,128,194,13,12,41+41,90-50,false); 			
				show_sprite(g,Armor,128,177,23,16,36+41,108-50,false); 									
				show_sprite(g,Armor,152,176,15,20,40+41,124-50,false); 
				break;				
				
				case 3 :
				show_sprite_noclip(g,CastleMap,0,0,132,124,22,0,false);
				break;
			}			
			if(game.cnt<17) show_fade(g,0,0,CANVX,120,6-((game.cnt-5)/2));			
			break;
						
			case CerberusMain.CHOOSE_WEAPON :
			clear_display(g,0,0,0);			
			show_font_text_center(g,Text.weaponselect,18,0);
			if(game.cnt/2<2) show_paper(g,40,game.cnt/2);
			else{
				show_paper(g,40,2);			
				show_sprite(g,Player,230,192,35,11,-17+CANVX/2,85,false);
				show_sprite(g,Player,216,191,14,13,-7+CANVX/2,105,false);
				show_sprite(g,Player,198,191,17,13,-9+CANVX/2,125,false);	
				show_sprite_noclip(g,Bars,63,21,13,10,35+(Trig.sin(game.cnt<<3)>>8),85+(20*game.weapon),false); 
				show_sprite_noclip(g,Bars,77,21,13,10,CANVX-48-(Trig.sin(game.cnt<<3)>>8),85+(20*game.weapon),false); 										
			}
			show_font_text_center(g,Text.weaponname[game.weapon],188,1);
			break;
			
			case CerberusMain.CHEAT :
			clear_display(g,0,0,0);			
			show_font_text_center(g,Text.cheatask,18,0);
			if(game.cnt/2<2) show_paper(g,40,game.cnt/2);
			else{
				show_paper(g,40,2);			
				show_font_text_center(g,Text.no,80,1);
				show_font_text_center(g,Text.yes,120,1);
				show_sprite_noclip(g,Bars,63,21,13,10,35+(Trig.sin(game.cnt<<3)>>8),80+(40*game.menu_opt),false); 
				show_sprite_noclip(g,Bars,77,21,13,10,CANVX-48-(Trig.sin(game.cnt<<3)>>8),80+(40*game.menu_opt),false); 										
			}			
			break;
						
			case CerberusMain.CHOOSE_LEVEL :
			clear_display(g,0,0,0);
			g.setFont(fnts);
			g.setColor(255,255,255);																					
			show_sprite_noclip(g,CastleMap,0,0,132,124,22,0,false);
			if(!game.cleared[0]) show_sprite_noclip(g,SwampMap,0,0,81,52,45,71,false);
			if(!game.cleared[2]) show_sprite_noclip(g,ForestMap,0,0,54,91,22,31,false);
			if(!game.cleared[1]) show_sprite_noclip(g,CavesMap,0,0,56,71,97,42,false);
			switch(game.level){
				case 0 : x=80; y=73; break;
				case 1 : x=123; y=35; break;
				case 2 : x=47; y=27; break;
				case 3 : x=87; y=4; break;				
			}
			show_sprite_noclip(g,Bars,52,21,10,11,x,y+(Trig.sin(game.cnt<<3)>>8),false); 
			show_paper(g,126,0);
			show_font_text_center(g,Text.leveldesc[game.level],164,0);
			if(game.cnt<6) show_fade(g,0,0,CANVX,CANVY,6-(game.cnt));
			break;
			
			case CerberusMain.LOAD_DATA :
			clear_display(g,0,0,0);
			show_font_text_center(g,Text.leveldesc[game.level],30,0);
			switch(game.level){
			 	case 0 : show_sprite_noclip(g,SwampMap,0,0,81,52,47,78,false); break;
				case 2 : show_sprite_noclip(g,ForestMap,0,0,54,91,61,58,false); break;
				case 1 : show_sprite_noclip(g,CavesMap,0,0,56,71,60,68,false); break;
				case 3 : show_sprite_noclip(g,CastleMap,0,0,132,124,22,42,false); break;
			}
			show_font_text_center(g,Text.loading,170,1);
			break;
			
			case CerberusMain.LEVEL_END :
			case CerberusMain.SUBLEVEL_END :
			case CerberusMain.LOST_LIFE :
			case CerberusMain.GAME_ACTION :
			clear_display(g,0,0,0);
			show_world(g,game.world);
			g.setClip(0,0,CANVX,CANVY);			
			g.setColor(255,255,0);			
			if(game.state==CerberusMain.GAME_ACTION && game.cnt<40)
				show_font_text_center(g,Text.getready,100,1);
				
			if(game.state==CerberusMain.LEVEL_END && game.cnt>15)
				show_font_text_center(g,Text.levelcomplete,100,1);													
				
			if(game.state==CerberusMain.LEVEL_END)
			if(game.cnt>48) show_fade(g,0,BEGINY,CANVX,BEGINY+SCREENY,((game.cnt-48)/2));				
			/*g.setClip(0,0,CANVX,CANVY);
			g.setFont(fnts);
			g.setColor(255,255,255);
			g.drawString("Life:"+game.world.pl.energy, 5, 40, Graphics.LEFT | Graphics.TOP );																
			*/
			show_font_text(g,Text.menutxt,2,CANVY-8,0);
			break;
			
			case CerberusMain.GAME_OVER :
			clear_display(g,0,0,0);			
			show_font_text_center(g,Text.gameover,90,1);					
			show_font_text_center(g,Text.diedtimes+": "+game.dead_times,115,0);					
			if(game.dead_times<666)
				show_font_text_center(g,(666-game.dead_times)+Text.diedleft,130,0);					
			break;
			
			case CerberusMain.LOAD_SAVED :
			case CerberusMain.SAVE_GAME :
			clear_display(g,0,0,0);
			show_paper(g,20,3);
			for(int i=0; i<3; i++){
				g.setClip(0,0,CANVX,CANVY);
				g.setColor(190,91,0);
				g.drawRect(28+(41*i),49,38,100);				
				if(game.sv[i].used){					
					g.setColor(0,128,0);
					if(game.sv[i].levels[0]) g.fillRect(30+(41*i),75,10,6);
					g.setColor(128,0,0);
					if(game.sv[i].levels[1]) g.fillRect(42+(41*i),75,10,6);
					g.setColor(0,0,128);
					if(game.sv[i].levels[2]) g.fillRect(54+(41*i),75,10,6);
					if(game.sv[i].sublevel==1)
						show_font_text(g,Text.levelname[game.sv[i].level],30+(41*i),52,0);
					else show_font_text(g,Text.map,30+(41*i),52,0);
					show_font_text(g,Text.lifes+" "+game.sv[i].lifes,30+(41*i),63,0);					
					if(game.sv[i].cape) show_sprite(g,Armor,241,170,26,35,31+(41*i),104,false); 
					if(game.sv[i].armor) show_sprite(g,Armor,104,177,23,18,36+(41*i),101,false); 			
					if(game.sv[i].helmet) show_sprite(g,Armor,128,194,13,12,41+(41*i),90,false); 			
					if(game.sv[i].gauntlets) show_sprite(g,Armor,128,177,23,16,36+(41*i),108,false); 									
					if(game.sv[i].boots) show_sprite(g,Armor,152,176,15,20,40+(41*i),124,false); 
					
			
				}else{
					show_font_text(g,Text.empty,32+(41*i),52,0);
				}				
			}
			show_sprite_noclip(g,Bars,52,21,10,11,44+(41*game.save_slot),30+(Trig.sin(game.cnt<<3)>>8),false); 
			show_sprite_noclip(g,Bars,41,20,10,14,44+(41*game.save_slot),158-(Trig.sin(game.cnt<<3)>>8),false); 
			
			if(game.state==CerberusMain.SAVE_GAME){
				show_font_text_center(g,Text.save,7,0);
				show_font_text_center(g,Text.save_instr,195,0);
			}else{
				show_font_text_center(g,Text.load,7,0);
				show_font_text_center(g,Text.load_instr,195,0);
			}
				
			break;
			
			case CerberusMain.IN_GAME_MENU :
			clear_display(g,0,0,0);
			if(game.cnt/2<4) 
				show_paper(g,5,game.cnt/2);
			else{
				show_paper(g,5,4);
				
				switch(game.menu_item){
					case 0 :
					Im=Player; cond=true;
					//Weapon info				
					switch(game.weapon){
						case 0 : x=230; y=192; sx=35; sy=11; id=Text.weaponname[game.weapon]; def=Text.weapontext[game.weapon]; break;
						case 1 : x=216; y=191; sx=14; sy=13; id=Text.weaponname[game.weapon]; def=Text.weapontext[game.weapon]; break;
						case 2 : x=198; y=191; sx=17; sy=13; id=Text.weaponname[game.weapon]; def=Text.weapontext[game.weapon]; break;
					}	
					break;		
										
					default :
					Im=Armor;
					switch(game.menu_item-1){
						case 0 : x=128; y=194; sx=13; sy=12; cond=game.world.pl.helmet; break; 	
						case 1 : x=104; y=177; sx=23; sy=18; cond=game.world.pl.armor; break; 	
						case 2 : x=128; y=177; sx=23; sy=16; cond=game.world.pl.gauntlets; break; 	
						case 3 : x=152; y=176; sx=15; sy=20; cond=game.world.pl.boots; break; 	
						case 4 : x=241; y=170; sx=26; sy=37; cond=game.world.pl.cape; break; 	
					}
					if(cond==true){
						id=Text.armorname[game.menu_item-1];
						def=Text.armortext[game.menu_item-1];						
					}else{
						id=Text.unknown;
						def=Text.notavail;
					}
					break;
				}				
				show_font_text_center(g,id,50,1);			
				show_sprite(g,Im,x,y,sx,sy,(CANVX-sx)/2,81-(sy/2),false);											
				show_font_text(g,def[0],25,100,0);
				show_font_text(g,def[1],25,110,0);
				show_font_text(g,def[2],25,120,0);
												
				show_sprite_noclip(g,Bars,41,21,10,13,6,75+(Trig.sin(game.cnt<<3)>>8),false); 
				show_sprite_noclip(g,Bars,52,21,10,11,6,95-(Trig.sin(game.cnt<<3)>>8),false); 
								
				// Menu options
				switch(game.menu_opt){
					case 0 : show_font_text_center(g,Text.contin,160,0); break;
					case 1 : if(game.sound) show_font_text_center(g,Text.sound_on,160,0);
						else show_font_text_center(g,Text.sound_off,160,0); break;
					case 2 : show_font_text_center(g,Text.controls,160,0); break;
					case 3 : show_font_text_center(g,Text.abort,160,0); break;
				}
				show_sprite_noclip(g,Bars,77,21,13,10,20+(Trig.sin(game.cnt<<3)>>8),160,false); 
				show_sprite_noclip(g,Bars,63,21,13,10,CANVX-33-(Trig.sin(game.cnt<<3)>>8),160,false); 
				
			}						
			break;
			
			case CerberusMain.CONTROLS2:
			case CerberusMain.CONTROLS:
			clear_display(g,0,0,0);
			g.setClip(20,5,127,112);
			g.drawImage(SamuelPic,20,5,20);
			for(int i=0; i<7; i++)
				show_font_text(g,Text.controlstext[i],10,110+(10*i),0);							
			show_font_text_center(g,Text.controlsstick,190,0);							
			break;
			
			case CerberusMain.CREDITS:
			clear_display(g,0,0,0);
			if(game.cnt/2<4) 
				show_paper(g,5,game.cnt/2);
			else{
				show_paper(g,5,4);
				show_font_text_center(g,Text.designed,28,0);											
				show_font_text_center(g,Text.david,38,0);
				show_font_text_center(g,Text.coded,53,0);																						
				show_font_text_center(g,Text.david,63,0);
				show_font_text_center(g,Text.graphic,78,0);			
				show_font_text_center(g,Text.elias,88,0);
				show_font_text_center(g,Text.music,103,0);																						
				show_font_text_center(g,Text.jordi,113,0);
				show_font_text_center(g,Text.thanks,128,0);																						
				show_font_text_center(g,Text.juan,138,0);				
				show_font_text_center(g,Text.microjocs,153,0);																						
				show_font_text_center(g,Text.web,168,0);												
			}
			break;									
			
			case CerberusMain.END_GAME :
			clear_display(g,0,0,0);
			if((game.cnt/2)%2==1) g.setColor(255,0,0);
			else g.setColor(255,255,255);
			g.fillRect(0,BEGINY,CANVX,SCREENY);
			show_tilemap(g,game.world);						
			show_CerberusBoss(g,game.world,game.world.boss);				
			show_samuel(g,game.world);	
			if(game.cnt>40){
				for(int i=0; i<CANVX; i+=32)
					show_sprite(g,CavesT,96,96,32,32,i,BEGINY+SCREENY-(2*(game.cnt-40)),false);	
				g.setClip(0,BEGINY,CANVX,SCREENY);
				g.setColor(255,0,0);
				g.fillRect(0,BEGINY+SCREENY+32-(2*(game.cnt-40)),CANVX,CANVY);
			}						
			break;
			
			case CerberusMain.EPILOGUE :
			clear_display(g,0,0,0);
			if(game.cnt<20)
				g.setColor(255/(game.cnt),0,0);
			g.setClip(0,BEGINY,CANVX,SCREENY);
			g.fillRect(0,0,CANVX,CANVY);
			for(int i=0; i<Text.epilogue.length; i++)
				show_font_text(g,Text.epilogue[i],20,CANVY+15*i-game.cnt,0);				
			break;
			
			case CerberusMain.RANK :
			clear_display(g,0,0,0);
			g.setClip(20,5,127,112);
			g.drawImage(SamuelPic,20,5,20);
			show_font_text_center(g,Text.congrats,120,1);				
			show_font_text_center(g,Text.yourrank,150,0);
			x=0;
			if(game.helmet) x++;
			if(game.armor) x++;
			if(game.gauntlets) x++;
			if(game.boots) x++;
			if(game.cape) x++;
			show_font_text_center(g,rankstr[x],170,1);
			break;
		}	
				
		painting=false;
		
		/*}catch (Exception e)
		{
			e.printStackTrace();
			e.toString();
		}*/

	}	
				
	public void clear_display(Graphics g,int r, int gr, int b)
	{
		g.setClip(0,0,CANVX,CANVY);
		g.setColor(r,gr,b);
		g.fillRect(0,0,CANVX,CANVY);		
	}
			
	public static void vibrate(int freq, long time)
	{
		try{
			if(CerberusMain.vibr)
			DeviceControl.startVibra(freq,(int)time);
			
		}catch (java.lang.Exception e) {}
	}
	
	public void show_sprite(Graphics g, Image im, int bx, int by, int sx, int sy, int px, int py, boolean inv)
	{
		int x1, x2, y1, y2;	
		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		
		x1=px; y1=py; x2=px+sx; y2=py+sy;
		
		if(x1<0) x1=0; 
		if(x2>=CANVX) x2=CANVX; 

		if(y1<BEGINY) y1=BEGINY; 
		if(y2>=BEGINY+SCREENY) y2=BEGINY+SCREENY; 
		
		g.setClip(x1,y1,x2-x1,y2-y1);
		if(inv) dg.drawImage(im,px+bx-(im.getWidth()-sx),py-by,20,DirectGraphics.FLIP_HORIZONTAL);				
		else 	g.drawImage(im,px-bx,py-by,20);				
	}									
	
	public void show_sprite_noclip(Graphics g, Image im, int bx, int by, int sx, int sy, int px, int py, boolean inv)
	{
		int x1, x2, y1, y2;	
		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		
		x1=px; y1=py; x2=px+sx; y2=py+sy;
		
		if(x1<0) x1=0; 
		if(x2>=CANVX) x2=CANVX-1; 

		if(y1<0) y1=0; 
		if(y2>=CANVY) y2=CANVY-1; 
		
		g.setClip(x1,y1,x2-x1,y2-y1);
		if(inv) dg.drawImage(im,px+bx-(im.getWidth()-sx),py-by,20,DirectGraphics.FLIP_HORIZONTAL);				
		else 	g.drawImage(im,px-bx,py-by,20);				
	}									
	
	
	public void show_sprite_vflip(Graphics g, Image im, int bx, int by, int sx, int sy, int px, int py, boolean inv)
	{
		int x1, x2, y1, y2;	
		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		
		x1=px; y1=py; x2=px+sx; y2=py+sy;
		
		if(x1<0) x1=0; 
		if(x2>=CANVX) x2=CANVX; 

		if(y1<BEGINY) y1=BEGINY; 
		if(y2>=BEGINY+SCREENY) y2=BEGINY+SCREENY; 
		
		g.setClip(x1,y1,x2-x1,y2-y1);
		if(inv) dg.drawImage(im,px+bx-(im.getWidth()-sx),py+by-(im.getHeight()-sy),20, DirectGraphics.FLIP_VERTICAL | DirectGraphics.FLIP_HORIZONTAL );
		else 	dg.drawImage(im,px-bx,py+by-(im.getHeight()-sy),20,DirectGraphics.FLIP_VERTICAL);				
	}									
	
			
	/*=====================================================================================*/
	
	public void load_world_gfx(int level, int sublevel)
	{
		Graphics g;
		InputStream is;
		
		clouds_on=false; moon_on=false; sky_on=false; aux_tm_on=false; bgty=1; bginy=0; 
		flash_bkg_on=false; bkg_scroll_on=false; bkg_on=true; rain_on=false; storm_on=false;
		thin_clouds_on=false;
		
								
		switch(level){
				
			// Swamp
			case 0 : 
			if(sublevel==0){
				sky_on=true; bgty=1; bginy=56; 
				
			}else{
				aux_tm_on=true; bgty=3;				
			}
			break;
						
			case 1 : 
			bgty=4; 
			if(sublevel==0) flash_bkg_on=true;			
			else bkg_scroll_on=true;
			break;
			
			case 2 : 
			bgty=1; bginy=30;
			if(sublevel==1) {moon_on=true; bkg_on=false; thin_clouds_on=true;}
			break;
			
			case 3 : 
			bkg_on=false;
			storm_on=true;
			if(sublevel==1) {moon_on=true; thin_clouds_on=true;}
			if(sublevel==0) rain_on=true;			
			break;
		}
		
		// Level bitmaps
											
		switch(level){
			case 0 :
			TileSet = SwampT;
			if(sublevel==0) BackGround = SwampPlants;
			else BackGround = SwampCave;
			Misc = SwampMisc;				
			Enemy = SwampEnemy;
			break;
				
			case 1 :
			TileSet = CavesT;
			if(sublevel==0) BackGround = CavesWall;
			else BackGround = CavesLava;
			Enemy = CavesEnemy;
			break;
				
			case 2 :
			TileSet = ForestT;
			BackGround = ForestTree;
			Enemy = ForestEnemy;				
			break;						
				
			case 3 :
			TileSet = CastleT;											
			Enemy = CastleEnemy;
			break;										
		}			
		if(bkg_on){
			bgsx=BackGround.getWidth();
			bgsy=BackGround.getHeight();
			bgtx=(CANVX/bgsx)+2;
		}
						
		System.gc();	
	}
	
	public void destroy_world_gfx()
	{
		TileSet=null;
		Enemy=null;
		BackGround=null;
		Misc=null;						
		System.gc();			
	}
	
	public void show_paper(Graphics g, int iy, int len)
	{
		g.setClip(8,iy,160,40);	
		g.drawImage(Paper,8,iy+0,20);	
		for(int i=0; i<len; i++){
			g.setClip(8,iy+40+(30*i),160,30);	
			g.drawImage(Paper,8,iy+40+(30*i)-26,20);	
		}
		g.setClip(8,iy+40+(30*len),160,40);	
		g.drawImage(Paper,8,iy+40+(30*len)-43,20);			
	}
	
	public void show_font_text(Graphics g, String s, int x, int y, int f)
	{
		char c[];		
		int cc, xx=x;
				
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-'0')%font_sizex[f].length;
			if(f==0){
				
				g.setClip(xx,y,8,8);
				g.drawImage(SFont,xx-(8*(cc%16)),y-(8*(cc/16)),20);			
				if(cc<0) xx+=font_sizex[0][0]+1;
				else xx+=font_sizex[0][cc]+1;
				
			}else{
				g.setClip(xx,y,16,16);
				g.drawImage(BFont,xx-(16*(cc%16)),y-(16*(cc/16)),20);			
				if(cc<0) xx+=font_sizex[1][0]+2;
				else xx+=font_sizex[1][cc]+2;

			}			
		}			
	}
	
	public void show_font_text_center(Graphics g, String s, int y, int f)
	{
		char c[];		
		int sx=0, cc, d=1+f;
		
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-'0')%font_sizex[f].length;
			if(cc<0) sx+=font_sizex[f][0]+d;
			else sx+=font_sizex[f][cc]+d;
		}
		if(f==0)
			show_font_text(g,s,(CANVX-sx)/2,y,f);
		else 	show_font_text(g,s,(CANVX-sx)/2,y,f);
	}
		
	public void show_world(Graphics g, CerberusWorld w)
	{
		int x,y;

		if(flash_bkg_on){
			
			switch((w.cnt/4)%4){
				case 0 : g.setColor(221,73,0); break;
				case 3 :
				case 1 : g.setColor(221,90,0); break;
				case 2 : g.setColor(222,113,0); break;
			}
			g.setClip(0,BEGINY,CANVX,SCREENY);
			g.fillRect(0,BEGINY,CANVX,SCREENY);
		}
		
		// Storm lightning
		if(storm_on){
			switch(w.cnt%200){
				case 0 : g.setColor(255,255,255); break;	
				case 1 : g.setColor(36,2,51); break;	
				case 2 : g.setColor(255,255,255); break;	
				case 3 : g.setColor(200,192,209); break;	
				case 4 : g.setColor(145,128,153); break;	
				case 5 : g.setColor(90,65,101); break;	
				default: g.setColor(36,2,51); break;					
			}	
			g.setClip(0,BEGINY,CANVX,SCREENY);
			g.fillRect(0,BEGINY,CANVX,SCREENY);			
		}
		
		// Rain
		if(rain_on)
			for(int i=0; i<2; i++)
			for(int j=0; j<2; j++){			
				show_sprite(g,Rain,0,0,176,144,(176*i)-(((w.bx/2)+((game.cnt+120)*8))%176),BEGINY-144+(144*j)-(((w.by/2)-((game.cnt+120)*16))%144),false);
			}		
		
		if(sky_on) show_sky(g,w);

		// Moon
		if(moon_on)
			show_sprite(g,Moon,0,0,35,36,130-(w.bx/18),20+BEGINY-(w.by/18),false);
			
		// Thin clouds
		if(thin_clouds_on){
			show_sprite(g,Clouds2,0,0,129,22,CANVX-(((w.bx/12)+(game.cnt/4))%300),BEGINY+SCREENY-(((w.by/12))%200),false);
			show_sprite(g,Clouds2,0,0,129,22,CANVX-((50+(w.bx/12)+(game.cnt/4))%300),BEGINY+SCREENY-((110+(w.by/12))%200),false);			
			show_sprite(g,Clouds2,42,23,87,17,CANVX-((90+(w.bx/12)+(game.cnt/6))%300),BEGINY+SCREENY-((50+(w.by/12))%200),false);
			show_sprite(g,Clouds2,42,23,87,17,CANVX-((160+(w.bx/12)+(game.cnt/6))%300),BEGINY+SCREENY-((20+(w.by/12))%200),false);
			show_sprite(g,Clouds2,0,23,41,13,CANVX-((130+(w.bx/12)+(game.cnt/8))%300),BEGINY+SCREENY-((140+(w.by/12))%200),false);
			
		}
									
		if(bkg_on) show_background(g,w);
		
		if (aux_tm_on) show_aux_tilemap(g,w);
						
		show_tilemap(g,w);
						
		if(w.boss!=null)
		if(!w.boss.out_of_world)
		if((Math.abs(w.pl.x-w.boss.x)<300) && (Math.abs(w.pl.y-w.boss.y)<300))
				show_CerberusBoss(g,w,w.boss);
				
		show_samuel(g,w);				
				
		if(w.en!=null)
		for(int i=0; i<w.en.length; i++)
			if(!w.en[i].out_of_world)
			if((Math.abs(w.pl.x-w.en[i].x)<170) && (Math.abs(w.pl.y-w.en[i].y)<170))
				show_CerberusEnemy(g,w,w.en[i]);
						
				
		for(int i=0; i<w.MAX_ITEM; i++)
			if(w.it[i]!=null) show_CerberusItem(g,w,w.it[i]);
				
				
		show_status_bars(g,w);
			
	}
	
	public void show_status_bars(Graphics g, CerberusWorld w)
	{
		int x, y, x2;
			
		// Samuel's head :)	
		x=35; y=15;
		g.setClip(x,y,13,10);
		g.drawImage(Player,x-308,y-122,20);
		show_font_text(g,""+game.lifes,15,15,1);
		
		// Life bar
		x=50; y=15; x2=(90*w.pl.energy)/w.pl.max_energy();
		g.setClip(x,y,90,10);
		g.drawImage(Bars,x,y,20);
		g.setClip(x,y,x2,10);
		g.drawImage(Bars,x,y-10,20);
		
		
		// Clock
		x=(CANVX-40)/2; y=BEGINY+SCREENY+3;
		g.setClip(x,y,40,26);
		g.drawImage(Bars,x,y-21,20);		
		show_font_text(g,(w.timer/1800)+":"+(((w.timer%1800)/30)/10)+(((w.timer%1800)/30)%10),x+10,y+14,0);
	}
	
	public void show_samuel(Graphics g, CerberusWorld w)
	{	
		int pos=-1, bx, by, x, y, auxx=0;
		boolean inv=false;
		byte frames[][]={
			// 0 Stand (nothing, sword, whip, boomer)
			{CAPE,0,1,-37, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,0,-40, ARMS,22,1,-39},
			{CAPE,0,1,-37, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,0,-40, ARMS,0,0,-45},
			{CAPE,0,1,-37, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,0,-40, ARMS,22,1,-39, WEAPON,7,0,-31},
			{CAPE,0,1,-37, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,0,-40, ARMS,22,1,-39, WEAPON,14,-3,-36},
			// 4 WAlking (sword)
			{CAPE,0,-2,-37, LEGS,1,-2,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,0,2,-45},
			{CAPE,1,-2,-37, LEGS,2,2,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,0,2,-45},
			{CAPE,2,-2,-37, LEGS,3,0,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,0,2,-45},
			{CAPE,1,-2,-37, LEGS,4,1,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,0,2,-45},
			// 8 Walking (whip)
			{CAPE,0,-2,-37, LEGS,1,-2,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,7,2,-31},
			{CAPE,1,-2,-37, LEGS,2,2,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,7,2,-31},
			{CAPE,2,-2,-37, LEGS,3,0,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,7,2,-31},
			{CAPE,1,-2,-37, LEGS,4,1,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,7,2,-31},
			// 12 Walking (boomer)
			{CAPE,0,-2,-37, LEGS,1,-2,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,14,-3,-36},
			{CAPE,1,-2,-37, LEGS,2,2,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,14,-3,-36},
			{CAPE,2,-2,-37, LEGS,3,0,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,14,-3,-36},
			{CAPE,1,-2,-37, LEGS,4,1,-25, HEAD,5,3,-47, BODY,0,2,-40, ARMS,22,3,-39, WEAPON,14,-3,-36},									
			// 16 Jumping (nothing, sword, whip, boomer)
			{LEGS,5,-2,-26, HEAD,5,1,-47, BODY,0,0,-40, ARMS,22,2,-39},			
			{LEGS,5,-2,-26, HEAD,5,1,-47, BODY,0,0,-40, ARMS,0,1,-45},			
			{LEGS,5,-2,-26, HEAD,5,1,-47, BODY,0,0,-40, ARMS,22,2,-39, WEAPON,7,1,-31},			
			{LEGS,5,-2,-26, HEAD,5,1,-47, BODY,0,0,-40, ARMS,22,2,-39, WEAPON,14,-3,-36},
			// 20 Sword slash (1 & 3)
			{CAPE,0,0,-37, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,1,-40, ARMS,2,-1,-45},
			{CAPE,0,0,-37, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,1,-40, ARMS,3,-2,-47},
			{LEGS,6,0,-20, HEAD,7,13,-41, BODY,9,5,-35, ARMS,4,11,-34, CAPE,6,-4,-34},			
			// 23 Sword slash 2
			{LEGS,17,0,-26, HEAD,7,11,-45, BODY,10,2,-38, ARMS,15,10,-49, CAPE,6,-6,-40},						
			// 24 Crouching
			{CAPE,5,-16,-31, LEGS,6,-2,-20, HEAD,5,6,-39, BODY,2,5,-36, ARMS,5,6,-31},
			// 25 Crouch
			{CAPE,4,-14,-22, LEGS,7,-2,-13, HEAD,5,1,-31, BODY,2,-1,-28, ARMS,5,0,-23},
			// 26 Kick
			{CAPE,4,-12,-21, LEGS,8,0,-11, HEAD,5,2,-30, BODY,3,0,-26, ARMS,6,-1,-21},
			{LEGS,9,7,-11, HEAD,8,-2,-36, BODY,4,0,-27, ARMS,7,-4,-22, CAPE,5,-22,-21},
			{LEGS,10,9,-11, HEAD,8,-2,-36, ARMS,8,-5,-23, BODY,5,-8,-27, WEAPON,4,8,-3, CAPE,3,-21,-21},
			// 29 Run
			{LEGS,11,3,-22, HEAD,5,15,-41, BODY,6,2,-37, ARMS,9,8,-35},
			{LEGS,12,3,-23, HEAD,5,19,-41, BODY,7,6,-39, ARMS,10,13,-35},
			{LEGS,13,3,-23, HEAD,6,18,-44, BODY,8,5,-39, ARMS,11,11,-35},
			{LEGS,14,3,-22, HEAD,8,14,-44, BODY,1,1,-36, ARMS,12,8,-34},
			{LEGS,15,3,-23, HEAD,6,19,-43, BODY,8,6,-38, ARMS,11,12,-34},
			{LEGS,16,3,-24, HEAD,5,19,-42, BODY,7,7,-40, ARMS,10,14,-36},
			// 35 Running slash
			{LEGS,0,0,-25, HEAD,5,1,-47, BODY,12,0,-44, ARMS,16,-2,-44},
			{LEGS,1,0,-25, HEAD,5,5,-48, BODY,13,4,-41, ARMS,17,5,-51},
			// 37 Jumping slash
			{LEGS,18,0,-26, HEAD,5,3,-48, BODY,14,1,-42, ARMS,18,6,-47},
			{LEGS,17,0,-26, HEAD,7,12,-46, BODY,10,2,-38, ARMS,15,10,-50},
			// 39 Stand Damage
			{LEGS,19,0,-25, HEAD,1,-9,-45, BODY,15,-4,-37, ARMS,19,0,-39},
			// 40 Crouch Damage
			{LEGS,7,0,-11, HEAD,1,-9,-31, BODY,15,-4,-23, ARMS,19,0,-25},
			// 41 Fall Damage
			{LEGS,5,0,-25, HEAD,1,-9,-45, BODY,15,-4,-37, ARMS,19,0,-39},
			// 42 Dying
			{LEGS,20,0,-25, HEAD,5,-21,-37, BODY,16,-20,-30, ARMS,20,-12,-33},
			{LEGS,21,0,-8, BODY,17,-19,-7,ARMS,21,-27,-6, HEAD,0,-30,-9},
			// 44 Whip Hit
			{CAPE,0,0,-37, WEAPON,8,-6,-52, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,0,-41, ARMS,23,2,-51},
			{CAPE,1,0,-37, LEGS,22,0,-22, HEAD,8,-1,-47, BODY,18,-6,-34, ARMS,24,-2,-46, WEAPON,9,-8,-48},
			{LEGS,22,0,-22, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, WEAPON,10,13,-49, CAPE,6,-12,-39},
			{LEGS,23,0,-22, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, WEAPON,11,59,-37, CAPE,3,-13,-34},			
			{LEGS,23,0,-22, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, WEAPON,13,59,-37, CAPE,3,-13,-34},			
			{LEGS,23,0,-22, HEAD,2,1,-47, BODY,20,0,-37, ARMS,26,4,-36, WEAPON,12,41,-34, CAPE,8,-12,-39},			
			// 50 Jump Hit
			{CAPE,0,0,-37, WEAPON,8,-6,-52, LEGS,3,0,-26, HEAD,5,1,-47, BODY,0,0,-40, ARMS,23,2,-51},
			{CAPE,1,0,-37, LEGS,17,-2,-20, HEAD,8,-1,-47, BODY,18,-6,-34, ARMS,24,-2,-46, WEAPON,9,-8,-48},
			{LEGS,17,-2,-20, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, WEAPON,10,13,-49, CAPE,6,-12,-39},
			{LEGS,17,-2,-20, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, WEAPON,11,59,-37, CAPE,3,-13,-34},			
			{LEGS,17,-2,-20, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, WEAPON,13,59,-37, CAPE,3,-13,-34},			
			{LEGS,17,-2,-20, HEAD,2,1,-47, BODY,20,0,-37, ARMS,26,4,-36, WEAPON,12,41,-34, CAPE,8,-12,-39},			
			// 56 Take item
			{CAPE,5,-16,-31, LEGS,6,-2,-20, HEAD,5,6,-39, BODY,2,5,-36, ARMS,5,6,-31},
			{CAPE,4,-14,-22, LEGS,7,-2,-13, HEAD,5,1,-31, BODY,2,-1,-28, ARMS,27,3,-23},						
			// 58 Boomer launch
			{CAPE,0,0,-37, WEAPON,16,-8,-54, LEGS,0,0,-25, HEAD,5,1,-47, BODY,0,0,-41, ARMS,23,2,-51},
			{CAPE,1,0,-37, LEGS,22,0,-22, HEAD,8,-1,-47, BODY,18,-6,-34, ARMS,24,-2,-46, WEAPON,15,-3,-49},
			{LEGS,22,0,-22, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, CAPE,6,-12,-39},
			{LEGS,23,0,-22, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, CAPE,3,-13,-34},						
			{LEGS,23,0,-22, HEAD,2,1,-47, BODY,20,0,-37, ARMS,26,4,-36, CAPE,8,-12,-39},			
			// 63 Jumping boomer launch
			{CAPE,0,0,-37, WEAPON,16,-8,-54, LEGS,3,0,-26, HEAD,5,1,-47, BODY,0,0,-41, ARMS,23,2,-51},
			{CAPE,1,0,-37, LEGS,17,-2,-20, HEAD,8,-1,-47, BODY,18,-6,-34, ARMS,24,-2,-46, WEAPON,15,-3,-49},
			{LEGS,17,-2,-20, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, CAPE,6,-12,-39},
			{LEGS,17,-2,-20, HEAD,8,1,-50, BODY,19,-4,-40, ARMS,25,7,-36, CAPE,3,-13,-34},						
			{LEGS,17,-2,-20, HEAD,2,1,-47, BODY,20,0,-37, ARMS,26,4,-36, CAPE,8,-12,-39},						
			// 68 Win
			{CAPE,2,0,-37, LEGS,24,0,-25, HEAD,4,0,-50, BODY,21,0,-43, ARMS,28,-3,-58}			
		};			
		
																		
		if(w.pl.dam_time>0 && w.pl.cnt%2==0) return;
		
		if(w.pl.dir==1) inv=false;
		if(w.pl.dir==2) inv=true;
		
		x=w.pl.x; y=w.pl.y;
		bx=w.bx; by=w.by;
		
		switch(w.pl.state){
			
		case CerberusSamuel.PRE_RUN :
		case CerberusSamuel.STAND :
		if(w.pl.weapon==CerberusSamuel.SWORD)
			pos=1;
		else if(w.pl.weapon==CerberusSamuel.WHIP)			
			pos=2;		
		if(w.pl.weapon==CerberusSamuel.BOOMER)			
		if(w.pl.boom_cnt==0)
			pos=3;		
		else pos=0;
		// Thinking about? :O
		if(w.pl.cnt>400){
			show_piece(g,w,Armor,220,176,20,28,x,y,-18,-75,inv); 
			switch(((w.pl.cnt/800)+game.level)%4){
				case 0 : show_piece(g,w,Armor,204,180,13,15,x,y,-19,-73,inv); break;	
				case 1 : show_piece(g,w,Armor,190,179,13,16,x,y,-19,-73,inv); break;	
				case 2 : show_piece(g,w,Armor,175,183,15,12,x,y,-19,-68,inv); break;	
				case 3 : show_piece(g,w,Armor,176,165,13,15,x,y,-19,-73,inv); break;	
			}
		}			
		break;
		
		case CerberusSamuel.JUMP1 :
		case CerberusSamuel.WALKING :
		pos=4+((w.pl.cnt/3)%4);
		if(w.pl.weapon==CerberusSamuel.WHIP) pos+=4;			
		if(w.pl.weapon==CerberusSamuel.BOOMER) pos+=8;			
		show_samuel_cape(g,w,(w.pl.cnt/2)%3,-2,-37,inv);		
		break;		
		
		case CerberusSamuel.RUN_JUMP :
		case CerberusSamuel.JUMP2 :
		show_samuel_cape(g,w,(w.pl.cnt/2)%3,-2,-37,!inv);				
		if(w.pl.weapon==CerberusSamuel.SWORD) pos=17;
		else if(w.pl.weapon==CerberusSamuel.WHIP) pos=18;
		else if(w.pl.boom_cnt==0) pos=19;
		else pos=16;
		break;
		
		case CerberusSamuel.FALL2:
		case CerberusSamuel.FALL :
		if(w.pl.cnt<4) show_samuel_cape(g,w,6+(w.pl.cnt/2)%3,-18,-39,inv);				
		else show_samuel_cape(g,w,3+(w.pl.cnt/2)%3,-18,-39,inv);						
		show_samuel_hairtail(g,w,w.pl.cnt%2,-2,-57,inv);
		if(w.pl.weapon==CerberusSamuel.SWORD) pos=17;
		else if(w.pl.weapon==CerberusSamuel.WHIP) pos=18;
		else if(w.pl.boom_cnt==0) pos=19;
		else pos=16;		
		break;
		
		case CerberusSamuel.SW_SLASH :
		if(w.pl.cnt==0) pos=20;
		else if(w.pl.cnt==1) pos=21;
		else pos=22;
		if(w.pl.cnt==3) show_samuel_weapon(g,w,0,18,-41,inv); 			
		break;
		
		case CerberusSamuel.SW_SLASH2 :		
		if(w.pl.cnt==2) 
		show_samuel_weapon(g,w,2,17,-48,inv); 							
		pos=23;
		break;

		case CerberusSamuel.SW_SLASH3 :				
		if(w.pl.cnt==2) 
		show_samuel_weapon(g,w,6,18,-42,inv); 			
		pos=22;
		break;
				
		case CerberusSamuel.CROUCHING :
		pos=24;
		break;
				
		case CerberusSamuel.JUMP3 :		
		case CerberusSamuel.CROUCH :		
		pos=25;
		break;		
		
		case CerberusSamuel.KICK :
		if(w.pl.cnt/2==0) pos=26;
		else if(w.pl.cnt/2==1 || w.pl.cnt/2==3) pos=27;
		else pos=28;
		if(w.pl.cnt/2==1) show_samuel_weapon(g,w,3,0,-3,inv);		
		break;
				
		case CerberusSamuel.RUNNING :
		pos=29+((w.pl.cnt/2)%6);		
		show_samuel_cape(g,w,3+(w.pl.cnt/2)%3,-4,-36,inv);								
		break;
		
		case CerberusSamuel.RUN_SLASH :
		if(w.pl.cnt/2==0) pos=35;
		else pos=36;
		show_samuel_cape(g,w,6+(w.pl.cnt/2),-12,-40,inv);									
		break;
		
		case CerberusSamuel.JUMP_SLASH :				 		
		if(w.pl.cnt/2==0){
			show_samuel_cape(g,w,7+pos,-12,-40,inv);												
			pos=37;
		}else{
			if(w.pl.cnt==2) 
				show_samuel_weapon(g,w,2,17,-48,inv); 							
			show_samuel_cape(g,w,6+(w.pl.cnt%3),-6,-40,inv);									
			pos=38;			
		}
		break;
		
		case CerberusSamuel.DAMAGE :
		show_samuel_cape(g,w,(w.pl.cnt/2)%3,-6,-37,inv);				
		pos=39;
		switch(w.pl.cnt/2){
			case 0 : show_piece(g,w,Player,268,170,15,16,x,y,-15,-45,inv); break;
			case 1 : show_piece(g,w,Player,284,170,15,16,x,y,-15,-45,inv); break;
			case 2 : show_piece(g,w,Player,300,170,17,16,x,y,-15,-45,inv); break;
		}		
		break;
		
		case CerberusSamuel.CROUCH_DAMAGE :		
		show_samuel_cape(g,w,4,-14,-22,inv);										
		pos=40;
		switch(w.pl.cnt/2){
			case 0 : show_piece(g,w,Player,268,187,17,16,x,y,-15,-45,inv); break;
			case 1 : show_piece(g,w,Player,285,187,15,16,x,y,-15,-45,inv); break;
			case 2 : show_piece(g,w,Player,304,170,17,16,x,y,-15,-45,inv); break;
		}		
		break;
		
		case CerberusSamuel.FALL_DAMAGE :
		if(w.pl.cnt<4) show_samuel_cape(g,w,6+(w.pl.cnt/2)%3,-18,-39,inv);				
		else show_samuel_cape(g,w,3+(w.pl.cnt/2)%3,-18,-39,inv);						
		pos=41;		
		switch(w.pl.cnt/2){
			case 0 : show_piece(g,w,Player,268,170,15,16,x,y,-15,-45,inv); break;
			case 1 : show_piece(g,w,Player,284,170,15,16,x,y,-15,-45,inv); break;
			case 2 : show_piece(g,w,Player,300,170,17,16,x,y,-15,-45,inv); break;
		}
		break;
		
		case CerberusSamuel.DEATH :		
		if(w.pl.cnt<=5) pos=42; else pos=43;
		break;
		
		case CerberusSamuel.WHIP_HIT :
		pos=(w.pl.cnt/2)+44;		
		break;
		
		case CerberusSamuel.JUMP_WHIP :
		pos=(w.pl.cnt/2)+50;		
		break;								
			
		case CerberusSamuel.TAKE_ITEM_SHORT :
		case CerberusSamuel.TAKE_ITEM :
		if(w.pl.cnt<2) pos=56; else pos=57;
		break;
				
		case CerberusSamuel.WON :
		case CerberusSamuel.GOT_ITEM :
		pos=68;
		break;		
		
		case CerberusSamuel.BOOM_LAUNCH :
		pos=(w.pl.cnt/2)+58;		
		break;

		case CerberusSamuel.JUMP_BOOM :
		pos=(w.pl.cnt/2)+63;		
		break;								
				
		}
		
		if(pos>=0) show_samuel_position(g,w,frames[pos],inv);
				
		// Boomerang
		if(w.pl.boom_cnt>0){
			if(w.pl.boom_dir==1) inv=false; else inv=true;
			switch((w.pl.cnt/2)%4){
				case 0 : show_piece(g,w,Player,144,186,7,13,w.pl.boom_x,w.pl.boom_y,0,0,inv); break;	
				case 1 : show_piece(g,w,Player,154,175,13,7,w.pl.boom_x,w.pl.boom_y,0,0,inv); break;	
				case 2 : show_piece(g,w,Player,152,186,7,13,w.pl.boom_x,w.pl.boom_y,0,0,inv); break;	
				case 3 : show_piece(g,w,Player,160,194,13,7,w.pl.boom_x,w.pl.boom_y,0,0,inv); break;	
				
			}			
			
		}
		
		/*if(w.pl.can_damage){
			g.setColor(255,0,0);
			g.setClip(w.pl.damage.x1-w.bx,BEGINY+w.pl.damage.y1-w.by,w.pl.damage.x2-w.pl.damage.x1,w.pl.damage.y2-w.pl.damage.y1);
			g.fillRect(w.pl.damage.x1-w.bx,BEGINY+w.pl.damage.y1-w.by,w.pl.damage.x2-w.pl.damage.x1,w.pl.damage.y2-w.pl.damage.y1);
		}*/
			
				
	}
	
	private void show_samuel_position(Graphics g, CerberusWorld w, byte fr[], boolean inv)
	{
		int id,x,y;
		
		for(int i=0; i<fr.length; i+=4){
			id=fr[i+1];
			x=fr[i+2];
			y=fr[i+3];
			switch(fr[i]){
				case HEAD : show_samuel_head(g,w,id,x,y,inv); break;								
				case BODY : show_samuel_body(g,w,id,x,y,inv); break;								
				case ARMS : show_samuel_arms(g,w,id,x,y,inv); break;								
				case LEGS : show_samuel_legs(g,w,id,x,y,inv); break;								
				case CAPE : show_samuel_cape(g,w,id,x,y,inv); break;								
				case WEAPON : show_samuel_weapon(g,w,id,x,y,inv); break;									
			}		
		}
	}
	
	private void show_samuel_body(Graphics g, CerberusWorld w, int n, int dx, int dy, boolean inv)
	{
		int ix=0, iy=0, sx=0, sy=0;
		Image im;
		if(w.pl.armor) im=Armor; else im=Player;
		switch(n){
			
			case 0 : ix=9; iy=93; sx=15; sy=16; break;			
			case 1 : ix=194; iy=76; sx=27; sy=20; break;			
			case 2 : ix=146; iy=102; sx=15; sy=22; break;			
			case 3 : ix=73; iy=106; sx=12; sy=19; break;			
			case 4 : ix=102; iy=90; sx=14; sy=23; break;			
			case 5 : ix=117; iy=102; sx=28; sy=24; break;			
			case 6 : ix=117; iy=81; sx=27; sy=20; break;			
			case 7 : ix=145; iy=81; sx=23; sy=20; break;			
			case 8 : ix=169; iy=81; sx=24; sy=20; break;
			case 9 : ix=5; iy=110; sx=20; sy=20; break;
			case 10: ix=252; iy=74; sx=25; sy=19; break;
			case 11: ix=86; iy=106; sx=15; sy=20; break;
			case 12: ix=102; iy=114; sx=13; sy=19; break;
			case 13: ix=238; iy=74; sx=13; sy=16; break;
			case 14: ix=281; iy=69; sx=11; sy=16; break;
			case 15: ix=222; iy=74; sx=15; sy=14; break;
			case 16: ix=45; iy=81; sx=18; sy=17; break;
			case 17: ix=278; iy=86; sx=16; sy=9; break;
			case 18: ix=162; iy=102; sx=25; sy=19; break;
			case 19: ix=193; iy=97; sx=27; sy=26; break;
			case 20: ix=222; iy=89; sx=15; sy=22; break;
			case 21: ix=41; iy=101; sx=16; sy=27; break;
						
		}
		if(inv) dx=-dx;
		show_sprite(g,im,ix,iy,sx,sy,w.pl.x+dx-w.bx-(sx/2),BEGINY+w.pl.y+dy-w.by,inv);		
	}
	
	private void show_samuel_legs(Graphics g, CerberusWorld w, int n, int dx, int dy, boolean inv)
	{
		int ix=0, iy=0, sx=0, sy=0;
		Image im;
		if(w.pl.boots) im=Armor; else im=Player;		
		switch(n){
			
			case 0 : ix=170; iy=135; sx=27; sy=25; break;			
			case 1 : ix=1; iy=131; sx=25; sy=25; break;			
			case 2 : ix=27; iy=129; sx=16; sy=25; break;			
			case 3 : ix=44; iy=129; sx=17; sy=25; break;			
			case 4 : ix=62; iy=129; sx=14; sy=25; break;			
			case 5 : ix=238; iy=94; sx=15; sy=26; break;			
			case 6 : ix=286; iy=96; sx=21; sy=20; break;			
			case 7 : ix=288; iy=117; sx=19; sy=13; break;			
			case 8 : ix=288; iy=131; sx=19; sy=12; break;			
			case 9 : ix=238; iy=121; sx=23; sy=10; break;			
			case 10: ix=238; iy=132; sx=39; sy=10; break;						
			case 11: ix=1; iy=157; sx=30; sy=22; break;						
			case 12: ix=32; iy=155; sx=40; sy=23; break;									
			case 13: ix=77; iy=127; sx=24; sy=23; break;									
			case 14: ix=73; iy=155; sx=30; sy=23; break;												
			case 15: ix=104; iy=153; sx=40; sy=23; break;												
			case 16: ix=145; iy=151; sx=22; sy=24; break;															
			case 17: ix=198; iy=124; sx=23; sy=24; break;															
			case 18: ix=222; iy=112; sx=15; sy=25; break;															
			case 19: ix=116; iy=127; sx=26; sy=25; break;																		
			case 20: ix=143; iy=127; sx=26; sy=23; break;																					
			case 21: ix=170; iy=124; sx=25; sy=10; break;																								
			case 22: ix=198; iy=149; sx=21; sy=23; break;																								
			case 23: ix=222; iy=143; sx=25; sy=22; break;																											
			case 24: ix=276; iy=144; sx=28; sy=25; break;																														
		}
		if(inv) dx=-dx;
		show_sprite(g,im,ix,iy,sx,sy,w.pl.x+dx-w.bx-(sx/2),BEGINY+w.pl.y+dy-w.by,inv);		
	}
	
	private void show_samuel_arms(Graphics g, CerberusWorld w, int n, int dx, int dy, boolean inv)
	{
		int ix=0, iy=0, sx=0, sy=0;
		Image im;
		if(w.pl.gauntlets) im=Armor; else im=Player;		
		switch(n){
			
			case 0 : ix=1; iy=1; sx=29; sy=15; break;			
			case 1 : ix=1; iy=24; sx=29; sy=15; break;			
			case 2 : ix=1; iy=40; sx=24; sy=14; break;			
			case 3 : ix=26; iy=41; sx=22; sy=16; break;			
			case 4 : ix=158; iy=14; sx=15; sy=28; break;			
			case 5 : ix=1; iy=55; sx=24; sy=11; break;			
			case 6 : ix=26; iy=81; sx=18; sy=19; break;			
			case 7 : ix=117; iy=59; sx=25; sy=21; break;			
			case 8 : ix=117; iy=36; sx=29; sy=22; break;			
			case 9 : ix=85; iy=1; sx=31; sy=12; break;			
			case 10 : ix=117; iy=1; sx=19; sy=18; break;			
			case 11 : ix=137; iy=1; sx=20; sy=18; break;			
			case 12 : ix=158; iy=1; sx=31; sy=12; break;			
			case 13 : ix=190; iy=1; sx=13; sy=13; break;			
			case 14 : ix=211; iy=1; sx=19; sy=17; break;			
			case 15 : ix=201; iy=19; sx=25; sy=21; break;			
			case 16 : ix=90; iy=14; sx=26; sy=19; break;			
			case 17 : ix=59; iy=14; sx=30; sy=26; break;			
			case 18 : ix=174; iy=15; sx=26; sy=15; break;			
			case 19 : ix=26; iy=58; sx=27; sy=22; break;			
			case 20 : ix=59; iy=1; sx=25; sy=12; break;			
			case 21 : ix=1; iy=17; sx=29; sy=6; break;	
			case 22 : ix=151; iy=65; sx=27; sy=15; break;	
			case 23 : ix=59; iy=41; sx=30; sy=26; break;	
			case 24 : ix=11; iy=67; sx=14; sy=26; break;	
			case 25 : ix=68; iy=73; sx=48; sy=16; break;	
			case 26 : ix=68; iy=90; sx=28; sy=15; break;	
			case 27 : ix=147; iy=43; sx=31; sy=21; break;				
			case 28 : ix=90; iy=34; sx=26; sy=38; break;							
		}
		if(inv) dx=-dx;
		show_sprite(g,im,ix,iy,sx,sy,w.pl.x+dx-w.bx-(sx/2),BEGINY+w.pl.y+dy-w.by,inv);		
	}
	
	private void show_samuel_head(Graphics g, CerberusWorld w, int n, int dx, int dy, boolean inv)
	{
		int ix=0, iy=0, sx=0, sy=0;
		Image im;
		if(w.pl.helmet) im=Armor; else im=Player;		
		switch(n){			
			case 0 : ix=308; iy=69; sx=13; sy=9; break;			
			case 1 : ix=308; iy=79; sx=11; sy=9; break;			
			case 2 : ix=308; iy=89; sx=13; sy=10; break;			
			case 4 : ix=308; iy=111; sx=13; sy=10; break;			
			case 5 : ix=308; iy=122; sx=13; sy=10; break;			
			case 6 : ix=308; iy=133; sx=13; sy=11; break;			
			case 7 : ix=308; iy=145; sx=13; sy=10; break;			
			case 8 : ix=308; iy=156; sx=13; sy=13; break;			
			//case 9 : ix=308; iy=149; sx=11; sy=7; break;			
		}
		if(inv) dx=-dx;
		show_sprite(g,im,ix,iy,sx,sy,w.pl.x+dx-w.bx-(sx/2),BEGINY+w.pl.y+dy-w.by,inv);		
	}
	
	private void show_samuel_weapon(Graphics g, CerberusWorld w, int n, int dx, int dy, boolean inv)
	{
		int ix=0, iy=0, sx=0, sy=0;
		switch(n){			
			case 0 : ix=179; iy=41; sx=27; sy=34; break;			
			case 2 : ix=286; iy=1; sx=35; sy=28; break;			
			case 3 : ix=174; iy=31; sx=23; sy=9; break;			
			case 4 : ix=231; iy=32; sx=43; sy=9; break;						
			case 6 : ix=281; iy=32; sx=40; sy=36; break;	
			case 7 : ix=1; iy=180; sx=16; sy=19; break;	
			case 8 : ix=169; iy=161; sx=11; sy=33; break;	
			case 9 : ix=181; iy=161; sx=16; sy=45; break;	
			case 10: ix=18; iy=180; sx=42; sy=22; break;	
			case 11: ix=61; iy=188; sx=70; sy=9; break;				
			case 12: ix=198; iy=173; sx=47; sy=17; break;				
			case 13: ix=61; iy=178; sx=70; sy=9; break;				
			case 14: ix=134; iy=190; sx=8; sy=13; break;				
			case 15: ix=134; iy=176; sx=8; sy=13; break;				
			case 16: ix=143; iy=176; sx=10; sy=8; break;				
		}
		if(inv) dx=-dx;
		show_sprite(g,Player,ix,iy,sx,sy,w.pl.x+dx-w.bx-(sx/2),BEGINY+w.pl.y+dy-w.by,inv);		
	}	
	
	private void show_samuel_hairtail(Graphics g, CerberusWorld w, int n, int dx, int dy, boolean inv)
	{
		int ix=0, iy=0, sx=0, sy=0;
		switch(n){			
			case 0 : ix=254; iy=170; sx=5; sy=22; break;			
			case 1 : ix=261; iy=170; sx=6; sy=22; break;			
		}
		if(inv) dx=-dx;
		show_sprite(g,Player,ix,iy,sx,sy,w.pl.x+dx-w.bx-(sx/2),BEGINY+w.pl.y+dy-w.by,inv);		
	}	
	
	private void show_samuel_cape(Graphics g, CerberusWorld w, int n, int dx, int dy, boolean inv)
	{
		int ix=0, iy=0, sx=0, sy=0;
		Image im;
		if(!w.pl.cape) return;
		switch(n){			
			case 0 : ix=241; iy=170; sx=26; sy=37; break;			
			case 1 : ix=267; iy=170; sx=26; sy=37; break;						
			case 2 : ix=293; iy=170; sx=26; sy=37; break;						
			case 3 : ix=217; iy=41; sx=32; sy=14; break;									
			case 4 : ix=250; iy=41; sx=32; sy=14; break;									
			case 5 : ix=283; iy=41; sx=32; sy=14; break;									
			case 6 : ix=179; iy=41; sx=37; sy=29; break;												
			case 7 : ix=231; iy=1; sx=37; sy=29; break;															
			case 8 : ix=269; iy=1; sx=37; sy=29; break;															
		}
		if(inv) dx=-dx;
		show_sprite(g,Armor,ix,iy,sx,sy,w.pl.x+dx-w.bx-(sx/2),BEGINY+w.pl.y+dy-w.by,inv);		
	}
	
	public void show_sky(Graphics g, CerberusWorld w)
	{
		g.setColor(128,0,0);
		g.setClip(0,BEGINY,CANVX,SCREENY);	
		for(int i=0; i<2; i++)
			g.drawImage(Sky,(175*i)-((w.bx/8)%175),BEGINY-(w.by/6),20);
	}
	
	public void show_background(Graphics g, CerberusWorld w)
	{
		int offset=0;	
		
		if(bkg_scroll_on) offset=-64+(w.cnt/2)%64;
		g.setClip(0,BEGINY,CANVX,SCREENY);
		for(int j=0; j<bgty; j++)
		for(int i=0; i<bgtx; i++)
			//g.drawImage(BackGround,(bgsx*i)-((w.bx/2)%bgsx),BEGINY+bginy+(bgsy*j)-((w.by/2)%bgsy),20);
			show_sprite(g,BackGround,0,0,bgsx,bgsy,(bgsx*i)-((w.bx/2)%bgsx),BEGINY+bginy+(bgsy*j)-((w.by/2)%bgsy)+offset,false);
			
	}
	
	public void show_tilemap(Graphics g, CerberusWorld w)
	{
		int x, y, t, ii, ei, ij, ej, x1, x2, y1, y2, aux;
		boolean show=true;
		
		ii=w.bx/32;
		ij=w.by/32;
		
		ei=ii+7; if(ei>w.tx) ei=w.tx;
		ej=ij+(SCREENY/32)+2; if(ej>w.ty) ej=w.ty;
		
		for(int i=ii; i<ei; i++)
		for(int j=ij; j<ej; j++)		
		{
			x=-w.bx+(32*i);
			y=-w.by+BEGINY+(32*j);
			
			if(i>=0 && i<w.sx && j>=0 && j<w.sy)
				t=w.TileMap[(j*w.tx)+i];
			else t=0;
			if (t<0) {t+=256;}
			//if(t>2){	
			switch(w.level){
				case 0 : show=(t>2); break;
				case 1 : show=(t!=142); break;
				case 2 : show=(t!=232); break;
				case 3 : show=(t!=136); break;

			}
			
			if(show){
				x1=x; y1=y; x2=32; y2=32;
				if(y1<BEGINY) {y2=y2-(BEGINY-y1); y1=BEGINY; }
				if(y1+y2>=BEGINY+SCREENY) { y2=BEGINY+SCREENY-y1;} 		
				g.setClip(x1,y1,x2,y2);
				g.drawImage(TileSet,x-(t%16)*8,y-(t/16)*8,20);		
				
				if(t==204 && w.level==CerberusWorld.CAVES){
					// Lava animation
					g.setClip(x1,y1,x2,22);
					if((w.cnt/4)%3==1)					
					show_sprite(g,Enemy,117,117,32,23,x,y+1,false);
					if((w.cnt/4)%3==2)
					show_sprite(g,Enemy,117,142,32,23,x,y+1,false);
					
					// Lava bubbles
					aux=((w.cnt/2)+(17*i))%60;
					if(aux<4) 
						show_sprite(g,Enemy,149,66+(9*aux),13,8,x+8,y,false);
					//149,66,13,8
				}					
				
				if(t==128 && w.level==CerberusWorld.CASTLE){
					switch(((w.cnt/2)+x)%4){
						case 0 : show_sprite(g,Enemy,0,86,12,31,x+9,y+1,false); break;	
						case 1 : show_sprite(g,Enemy,12,86,11,31,x+10,y+1,false); break;	
						case 2 : show_sprite(g,Enemy,23,86,12,31,x+9,y+1,false); break;	
						case 3 : show_sprite(g,Enemy,0,117,16,30,x+5,y+2,false); break;	
					}
				}
			}			
		}				
	}
	
	public void show_aux_tilemap(Graphics g, CerberusWorld w)
	{
		int x, y, t, ii, ei, ij, ej, x1, x2, y1, y2, tx=0, ty=0;
		
		ii=w.bx/32;
		ij=w.by/32;
		
		ei=ii+7; if(ei>w.tx) ei=w.tx;
		ej=ij+6; if(ej>w.ty) ej=w.ty;
		
		for(int i=ii; i<ei; i++)
		for(int j=ij; j<ej; j++)		
		{
			x=-w.bx+(32*i);
			y=-w.by+BEGINY+(32*j);
			
			t=AuxTileMap[(j*w.tx)+i];						
			if (t<0) {t+=256;}
			if(t!=0 && t!=20 && t!=22){	
				x1=x; y1=y; x2=32; y2=32;
				if(y1<BEGINY) {y2=y2-(BEGINY-y1); y1=BEGINY; }
				if(y1+y2>=BEGINY+SCREENY) { y2=BEGINY+SCREENY-y1;} 	
				switch(t){
					case 4	: 
					tx=32; ty=31-(w.cnt<<2)%32; 
					g.setClip(x1,y1,x2,y2);
					g.drawImage(SwampMisc,x-tx,y-ty,20);						
					break;
					
					case 8	: 
					tx=32; ty=31-(w.cnt<<2)%32; 
					g.setClip(x1,y1,x2,y2);
					g.drawImage(SwampMisc,x-tx,y-ty,20);											
					g.drawImage(SwampMisc,x-64,y-16-((w.cnt/2)%2)*32,20);											
					break;
				}
				
			}
		}
	}
	
	public void show_CerberusEnemy(Graphics g, CerberusWorld w, CerberusEnemy e)
	{
		
		int x=e.x, y=e.y, cx=e.body.centerx()-w.bx, cy=e.body.centery()+BEGINY-w.by, aux, aux2;
		boolean inv=false;
		
		if(!e.dead || e.cnt%2==0)
		switch(e.type){
						
			case CerberusEnemy.TRITON :			
			switch(e.frame){
				case 1 :
				show_piece(g,w,Enemy,1,1,25,21,x,y,0,-15,inv); break;				
				case 2 :
				show_piece(g,w,Enemy,27,1,24,41,x,y,0,-50,inv); 
				show_piece(g,w,Enemy,52,28,42,27,x,y,0,-10,inv); 
				break;
				case 3 :
				show_piece(g,w,Enemy,52,1,39,26,x,y,0,-80,inv); 
				show_piece(g,w,Enemy,96,26,39,12,x,y,0,-10,inv); 
				break;
				case 4 :
				show_piece(g,w,Enemy,92,1,43,24,x,y,0,-80,inv); 
				show_piece(g,w,Enemy,96,39,39,10,x,y,0,-10,inv); 
				break;
				case 5 :
				show_piece(g,w,Enemy,136,1,17,57,x,y,0,-60,inv); 				
				break;
				case 6 :
				show_piece(g,w,Enemy,1,23,25,29,x,y,0,-20,inv); 				
				break;
				case 7 :
				show_piece(g,w,Enemy,105,50,30,11,x,y,0,-10,inv); 				
				break;
				case 8 :
				show_piece(g,w,Enemy,52,28,42,27,x,y,0,-10,inv); 
				break;
				case 9 :
				show_piece(g,w,Enemy,96,26,39,12,x,y,0,-10,inv); 
				break;
				case 10 :
				show_piece(g,w,Enemy,96,39,39,10,x,y,0,-10,inv); 
				break;																								
			}
			switch(e.dam_time/2){				
				case 0 : show_piece(g,w,Enemy,110,62,10,18,x,cy,0,-10,inv); break;
				case 1 : show_piece(g,w,Enemy,115,81,11,17,x,cy,0,-10,inv); break;
				case 2 : show_piece(g,w,Enemy,112,99,14,15,cx,cy,0,-10,inv); break;				
			}			
			//if(e.frame>0 && e.frame<8) show_sprite(g,Triton,48*(e.frame-1),0,48,91,x,y,false);
			break;
			
			case CerberusEnemy.SCORPION_FLY :			
			if(e.dir==1) inv=true; else inv=false;
			show_piece(g,w,Enemy,34,56,32,18,x,y,0,-9,inv);
			switch(e.frame){				
				case 0 : show_piece(g,w,Enemy,1,53,18,16,x,y,1,-20,inv); break;
				case 3 :
				case 1 : show_piece(g,w,Enemy,27,43,19,7,x,y,2,-8,inv); break;
				case 2 : show_piece(g,w,Enemy,20,53,13,14,x,y,0,-8,inv); break;
			}
			switch(e.dam_time/2){				
				case 0 : show_piece(g,w,Enemy,137,79,8,12,x,y,0,-30,inv); break;
				case 1 : show_piece(g,w,Enemy,146,79,7,18,x,y,0,-30,inv); break;
				case 2 : show_piece(g,w,Enemy,141,98,12,22,x,y,0,-30,inv); break;				
			}
			break;			
						
			case CerberusEnemy.UGLY_BALL :			
			if(e.dir==1) inv=true; else inv=false;			
			switch(e.frame){
				case 0 : show_piece(g,w,Enemy,1,70,32,32,x,y,0,0,inv); break;
				case 1 : show_piece(g,w,Enemy,34,75,32,32,x,y,0,0,inv); break;
				case 2 : show_piece(g,w,Enemy,67,56,31,31,x,y,0,0,inv); break;
			}
			switch(e.dam_time/2){				
				case 0 : show_piece(g,w,Enemy,137,79,8,12,x,y,0,-10,inv); break;
				case 1 : show_piece(g,w,Enemy,146,79,7,18,x,y,0,-10,inv); break;
				case 2 : show_piece(g,w,Enemy,141,98,12,22,x,y,0,-10,inv); break;				
			}
			break;
			
			case CerberusEnemy.TURTLE_GOLEM :
			if(e.dir==1) inv=true; else inv=false;
			if(e.frame==0){
				show_piece(g,w,Enemy,35,66,47,38,x,y,0,-39,inv);
				show_piece(g,w,Enemy,35,143,47,11,x,y,0,-1,inv);
			}else{
				
				show_piece(g,w,Enemy,2,121,12,9,x,y,-17,-36,inv);
				if(e.frame<=8) aux=e.frame-1;
				else aux=8-(e.frame-8);
				for(int i=0; i<aux; i++)
					show_piece(g,w,Enemy,1,114,13,6,x,y,-17-12-(12*i),-35,inv);
				if(aux<7) show_piece(g,w,Enemy,1,131,15,14,x,y,-17-12-2-(12*(aux-1)),-41,inv);
				else show_piece(g,w,Enemy,17,127,17,8,x,y,-17-12-3-(12*(aux-1)),-35,inv);
				
				show_piece(g,w,Enemy,35,105,47,49,x,y,0,-39,inv);				
			}
			switch(e.dam_time/2){				
				case 0 : show_piece(g,w,Enemy,149,102,13,12,x,y,0,-35,inv); break;
				case 1 : show_piece(g,w,Enemy,150,115,12,10,x,y,0,-35,inv); break;
				case 2 : show_piece(g,w,Enemy,150,126,12,9,x,y,0,-35,inv); break;				
			}			
			break;
			
			case CerberusEnemy.INV_AMONITE :
			case CerberusEnemy.AMONITE :
			if(e.bul_on) 
			switch(((e.bulang)/32)%8){
				case 0 : show_piece(g,w,Enemy,27,144,7,4,e.bulx,e.buly,-5,-2,false); break;								
				case 1 : show_piece(g,w,Enemy,19,144,7,7,e.bulx,e.buly,-5,-5,true); break;												
				case 2 : show_piece(g,w,Enemy,30,136,4,7,e.bulx,e.buly,-2,-2,false); break;								
				case 3 : show_piece(g,w,Enemy,19,144,7,7,e.bulx,e.buly,-2,-5,false); break;												
				case 4 : show_piece(g,w,Enemy,27,149,7,4,e.bulx,e.buly,-2,-2,false); break;												
				case 5 : show_piece(g,w,Enemy,22,136,7,7,e.bulx,e.buly,-2,-2,false); break;																
				case 6 : show_piece(g,w,Enemy,17,136,4,7,e.bulx,e.buly,-2,-5,false); break;								
				case 7 : show_piece(g,w,Enemy,22,136,7,7,e.bulx,e.buly,-5,-2,true); break;												
			}
			if(e.dir==1) inv=true; else inv=false;			
			
			if(e.type==CerberusEnemy.AMONITE){
				show_piece(g,w,Enemy,1,66,33,21,x,y,0,-20+(e.frame/2),inv);							
				switch(e.frame){
					case 0 : show_piece(g,w,Enemy,4,88,30,12,x,y,1,-6,inv); break;
					case 3 :
					case 1 : show_piece(g,w,Enemy,15,114,19,12,x,y,1,-6,inv); break;
					case 2 : show_piece(g,w,Enemy,4,101,30,12,x,y,2,-6,inv); break;		
					
				}
			}else{
				show_piece_vflip(g,w,Enemy,1,66,33,21,x,y,0,-1-(e.frame/2),inv);							
				switch(e.frame){
					case 0 : show_piece_vflip(g,w,Enemy,4,88,30,12,x,y,1,-10,inv); break;
					case 3 :
					case 1 : show_piece_vflip(g,w,Enemy,15,114,19,12,x,y,1,-10,inv); break;
					case 2 : show_piece_vflip(g,w,Enemy,4,101,30,12,x,y,2,-10,inv); break;							
				}
			}	
			switch(e.dam_time/2){				
				case 0 : show_piece(g,w,Enemy,77,155,12,16,x,y,0,-25,inv); break;
				case 1 : show_piece(g,w,Enemy,90,155,11,15,x,y,0,-25,inv); break;
				case 2 : show_piece(g,w,Enemy,102,155,14,16,x,y,0,-25,inv); break;
			}												
			break;
			
			case CerberusEnemy.LAVA_ROCK :
			if(e.cnt%64<40){
				aux2=e.cnt%64;
				aux=70*Trig.sin(4*(aux2%32))>>10;
				if(aux2<18) show_piece(g,w,Enemy,83,129,16,25,x,y,0,-aux-10,inv); 
				else if((aux2<32)) show_piece(g,w,Enemy,100,129,16,25,x,y,0,-aux-16,inv); 
				switch(aux2/2){
										
					case 1 : show_piece(g,w,Enemy,83,104,33,24,x,y,0,-13,inv); break;	
					case 16:
					case 0 : 
					case 2 : show_piece(g,w,Enemy,83,83,33,20,x,y,0,-9,inv); break;	
					case 17:
					case 3 : show_piece(g,w,Enemy,83,66,33,16,x,y,0,-5,inv); break;																				
				
				}
			}
			break;
			
			case CerberusEnemy.PLATFORM :
			if(w.level==CerberusWorld.CAVES)
			show_piece(g,w,Enemy,50,155,26,16,x,y,0,-1,inv); 
			else 
			show_piece(g,w,Enemy,48,168,28,8,x,y,0,0,inv); 
			break;
			
			case CerberusEnemy.BIG_PLATFORM :
			if(w.level==CerberusWorld.CAVES)
			show_piece(g,w,Enemy,1,155,48,16,x,y,0,-1,inv); 
			else 
			show_piece(g,w,Enemy,0,168,49,9,x,y,0,0,inv); 			
			break;			
			
			case CerberusEnemy.SKULL_DOG :
			if(e.dir==1) inv=true; else inv=false;	
			if(e.dead) 
				show_piece(g,w,Enemy,62,71,40,10,x,y,0,15,inv);
			else if(e.dam_time<8)
				show_piece(g,w,Enemy,60,81,42,25,x,y-(Trig.sin(e.dam_time*16)>>6),0,-1,inv);
			else
			switch((e.cnt/2)%3){
				case 0 : show_piece(g,w,Enemy,55,107,52,24,x,y,0,-1,inv); break;
				case 2 : show_piece(g,w,Enemy,55,132,52,25,x,y,0,-1,inv); break;
				case 1 : show_piece(g,w,Enemy,59,158,48,24,x,y,0,-1,inv); break;
			}
			switch(e.dam_time/2){
				case 0 : show_piece(g,w,Enemy,27,173,17,15,x,y,0,0,inv); break;				
				case 1 : show_piece(g,w,Enemy,44,176,15,12,x,y,0,0,inv); break;				
			}
			break;			
			
			case CerberusEnemy.GOBLIN :
			if(e.dead){
				show_piece(g,w,Enemy,69,0,31,21,x,y,0,0,inv);
			}else{
				if(e.frame==0)
					show_piece(g,w,Enemy,51,1,17,22,x,y,0,0,inv);
				else if (e.frame<4)
					show_piece(g,w,Enemy,69,0,31,21,x,y,0,0,inv);
				else{
					aux=y+((e.vely*Trig.sin((e.cnt%32)*16))>>10);
					
					if((e.cnt/2)%2==0)
					show_piece(g,w,Enemy,51,24,43,22,x,aux,0,0,inv);
					else show_piece(g,w,Enemy,57,47,37,23,x,aux,0,0,inv);
				}
				switch(e.dam_time/2){
					case 0 : show_piece(g,w,Enemy,27,173,17,15,x,y,0,0,inv); break;				
					case 1 : show_piece(g,w,Enemy,44,176,15,12,x,y,0,0,inv); break;				
				}
				
			}				
			break;		
			
			case CerberusEnemy.EGG :
			switch(e.frame){	
				case 0 : show_piece(g,w,Enemy,96,22,8,10,x,y,0,0,inv); break;
				case 1 : show_piece(g,w,Enemy,96,33,8,10,x,y,0,0,inv); break;
				case 2 : show_piece(g,w,Enemy,96,44,8,10,x,y,0,0,inv); break;
				case 3 : show_piece(g,w,Enemy,0,42,22,15,x,y,0,0,inv); break;
				default : show_piece(g,w,Enemy,0,172,26,16,x,y,0,0,inv); break;				
			}
			break;																																															
			
			case CerberusEnemy.SENTINEL :
			y+=Trig.sin((e.cnt%16)*16)>>8;
			switch(e.frame/2){
				case 0 : show_piece(g,w,Enemy,0,0,37,25,x,y,0,0,inv); break;
				case 3 :
				case 1 : show_piece(g,w,Enemy,38,0,61,25,x,y,0,0,inv); break;
				case 2 : show_piece(g,w,Enemy,20,26,79,25,x,y,0,0,inv); break;
			}
			
			switch((e.cnt/3)%3){
				case 0 : show_piece(g,w,Enemy,0,25,13,6,x,y+25,0,0,inv); break;
				case 1 : show_piece(g,w,Enemy,0,31,13,10,x,y+25,0,0,inv); break;
				case 2 : show_piece(g,w,Enemy,0,43,13,7,x,y+25,0,0,inv); break;
			}						
			if(w.pl.dir==1) {x+=20; inv=false;}
			else {x-=20; inv=true;}
			switch(e.dam_time/2){
				case 0 : show_piece(g,w,Enemy,145,137,14,11,x,y,0,0,inv); break;				
				case 1 : show_piece(g,w,Enemy,145,155,22,22,x,y,0,0,inv); break;								
				case 2 : show_piece(g,w,Enemy,172,136,25,29,x,y,0,0,inv); break;								
			}				
			break;	
			
			case CerberusEnemy.BONE_JAIL :		
			show_piece(g,w,Enemy,0,51,48,10,x,y,0,0,inv);
			switch(e.frame){
				default : show_piece(g,w,Enemy,61,68,29,7,x+10,y+5,0,0,inv); break;
				case 16 :
				case 15 :
				case 11 : show_piece(g,w,Enemy,50,54,40,13,x+4,y-4,0,0,inv); break;
				case 14 :
				case 13 :
				case 12 : show_piece(g,w,Enemy,7,62,39,25,x+4,y-16,0,0,inv); break;
			}
			break;
			
			case CerberusEnemy.ROCK_HEADR :	
			switch(e.cnt%4){
				case 0 : show_piece(g,w,Enemy,46,99,14,8,e.bulx-7,e.buly-4,0,0,false); break;
				case 1 : show_piece(g,w,Enemy,46,115,14,8,e.bulx-7,e.buly-4,0,0,false); break;
				case 2 : show_piece(g,w,Enemy,46,127,16,10,e.bulx-7,e.buly-5,0,0,false); break;
				case 3 : show_piece(g,w,Enemy,46,143,16,10,e.bulx-7,e.buly-5,0,0,false); break;
			}
			if(e.cnt%60<8)	
			show_piece(g,w,Enemy,63,92,30,33,x-7,y-7,0,0,false); 
			else 
			show_piece(g,w,Enemy,63,125,30,33,x-7,y-7,0,0,false); 
			break;			
			
			case CerberusEnemy.ROCK_HEADL :	
			switch(e.cnt%4){
				case 0 : show_piece(g,w,Enemy,46,99,14,8,e.bulx-7,e.buly-4,0,0,true); break;
				case 1 : show_piece(g,w,Enemy,46,115,14,8,e.bulx-7,e.buly-4,0,0,true); break;
				case 2 : show_piece(g,w,Enemy,46,127,16,10,e.bulx-7,e.buly-5,0,0,true); break;
				case 3 : show_piece(g,w,Enemy,46,143,16,10,e.bulx-7,e.buly-5,0,0,true); break;
			}
			if(e.cnt%60<8)	
			show_piece(g,w,Enemy,63,92,30,33,x+15,y-7,0,0,true); 
			else 
			show_piece(g,w,Enemy,63,125,30,33,x+15,y-7,0,0,true); 
			break;						
		}
				
		/*g.setColor(0,255,0);
		g.setClip(e.body.x1-w.bx,BEGINY+e.body.y1-w.by,e.body.x2-e.body.x1,e.body.y2-e.body.y1);
		g.fillRect(e.body.x1-w.bx,BEGINY+e.body.y1-w.by,e.body.x2-e.body.x1,e.body.y2-e.body.y1);							
		
		g.setColor(255,0,0);
		g.setClip(e.damage.x1-w.bx,BEGINY+e.damage.y1-w.by,e.damage.x2-e.damage.x1,e.damage.y2-e.damage.y1);
		g.fillRect(e.damage.x1-w.bx,BEGINY+e.damage.y1-w.by,e.damage.x2-e.damage.x1,e.damage.y2-e.damage.y1);							
		*/
		
	}	
	
	public void show_CerberusBoss(Graphics g, CerberusWorld w, CerberusBoss e)
	{
		
		int x=e.x, y=e.y, cx=e.body.centerx()-w.bx, cy=e.body.centery()+BEGINY-w.by, ix=0, iy=0, sx=0, pos, cnt;
		boolean inv=false;
		
				
		if(!e.dead || e.cnt%2==0)
		switch(e.type){
						
			case CerberusBoss.WIDOW :	
			show_piece(g,w,Enemy,154,1,67,48,x,y,0,0,false);		
			pos=(e.cnt/2)%6;
			switch(pos){
				case 0 : ix=204; iy=50; sx=13; break;	
				case 5 :
				case 1 : ix=204; iy=83; sx=13; break;	
				case 4 :
				case 2 : ix=218; iy=83; sx=13; break;	
				case 3 : ix=232; iy=83; sx=15; break;	
			}
			show_piece(g,w,Enemy,ix,iy,sx,32,x,y+48,0,0,false);		

			if(e.frame==0 || e.claw==2){
				show_piece(g,w,Enemy,154,50,24,15,x,y+35,-14,0,false);								
				show_piece(g,w,Enemy,222,1,18,72,x,y+12,-26,0,false);						
			}else
			switch(e.frame){			
				
				case 5 :
				case 1 :
				show_piece(g,w,Enemy,154,66,24,10,x,y+39,-14,0,false);								
				show_piece(g,w,Enemy,222,1,18,72,x,y+20,-26,0,false);		
				break;
				
				case 4 :
				case 2 :
				show_piece(g,w,Enemy,154,77,24,8,x,y+43,-14,0,false);								
				show_piece(g,w,Enemy,222,1,18,72,x,y+28,-26,0,false);		
				break;
				
				case 3 :
				show_piece(g,w,Enemy,154,86,24,12,x,y+43,-14,0,false);								
				show_piece(g,w,Enemy,222,1,18,48,x,y+36,-26,0,false);		
				show_piece(g,w,Enemy,67,88,18,24,x,y+36+48,-26,0,false);						
				break;				
			}				
			
			if(e.frame==0 || e.claw==1){
				show_piece(g,w,Enemy,179,50,24,15,x,y+35,14,0,false);		
				show_piece(g,w,Enemy,241,1,18,72,x,y+12,26,0,false);		
			}else
			switch(e.frame){			
				
				case 5 :
				case 1 :
				show_piece(g,w,Enemy,178,66,24,10,x,y+39,14,0,false);								
				show_piece(g,w,Enemy,241,1,18,72,x,y+20,26,0,false);		
				break;
				
				case 4 :
				case 2 :
				show_piece(g,w,Enemy,178,77,24,8,x,y+43,14,0,false);								
				show_piece(g,w,Enemy,241,1,18,72,x,y+28,26,0,false);		
				break;
				
				case 3 :
				show_piece(g,w,Enemy,178,86,24,12,x,y+43,14,0,false);								
				show_piece(g,w,Enemy,241,1,18,48,x,y+36,26,0,false);		
				show_piece(g,w,Enemy,86,88,18,24,x,y+36+48,26,0,false);						
				break;				
			}															
			
			switch(e.dam_time/2){				
				case 0 : show_piece(g,w,Enemy,137,79,8,12,x,y+54,0,-10,inv); break;
				case 1 : show_piece(g,w,Enemy,146,79,7,18,x,y+54,0,-10,inv); break;
				case 2 : show_piece(g,w,Enemy,141,98,12,22,x,y+54,0,-10,inv); break;				
			}			
			break;
			
			case CerberusBoss.VIPER :
			for(int i=10; i>=0; i--){
				cnt=(e.cnt*3)-(8*i);
				if(e.dead) cnt=(30*3)-(8*i);
				if(cnt<150){					
					if(e.dir==1) {x=e.x-(60*Trig.cos(cnt)>>10); inv=true;}
					if(e.dir==2) {x=e.x+(60*Trig.cos(cnt)>>10); inv=false;}
					y=e.y-(120*Trig.sin(cnt)>>10);
					pos=(5*cnt)/128;
					switch(pos){
						case 0 : ix=131; sx=31; break;	
						case 1 : ix=99; sx=31; break;	
						case 2 : ix=66; sx=32; break;	
						case 3 : ix=34; sx=31; break;	
						case 4 : ix=1; sx=32; break;	
					}
					if(i==0) iy=34; else iy=1;
					// Body
					if(y<e.y)
						show_piece(g,w,Enemy,ix,iy,sx,31,x,y,0,-15,inv);											
					// Lava splash
					if(y>e.y && y<e.y+12)						
						show_piece(g,w,Enemy,117,88,31,28,x,e.y,0,-14,false);																	
					if(y>=e.y+12 && y<=e.y+24)
						show_piece(g,w,Enemy,117,66,31,21,x,e.y,0,-7,false);											
					// Blood
					if(i==0) 
					switch(e.dam_time/2){				
						case 0 : show_piece(g,w,Enemy,149,102,13,12,x,y,0,-14,inv); break;
						case 1 : show_piece(g,w,Enemy,150,115,12,10,x,y,0,-14,inv); break;
						case 2 : show_piece(g,w,Enemy,150,126,12,9,x,y,0,-14,inv); break;				
					}									
				}
			}						
			break;
			
			case CerberusBoss.BIRD :
			
			y-=(Trig.sin(((e.cnt%8)+4)*32)>>7);
			iy=(Trig.sin((e.cnt%8)*32)>>8);
			
			switch((e.cnt/2)%4){
				case 3 :
				case 2 :
				show_piece(g,w,Enemy,0,56,55,40,x-36,y,0,0,false);
				show_piece(g,w,Enemy,0,56,55,40,x+36,y,0,0,true);
				break;
								
				case 1 :				
				show_piece(g,w,Enemy,0,97,56,36,x-34,y-15,0,0,false);
				show_piece(g,w,Enemy,0,97,56,36,x+34,y-15,0,0,true);
				break;
								
				case 0 :
				show_piece(g,w,Enemy,0,134,47,38,x-32,y-5,0,0,false);
				show_piece(g,w,Enemy,0,134,47,38,x+32,y-5,0,0,true);
				break;
			}
			
			show_piece(g,w,Enemy,38,1,12,53,x-6,y,0,0,false);
			show_piece(g,w,Enemy,38,1,12,53,x+6,y,0,0,true);
			if(e.frame==0 || e.dam_time<10)
				show_piece(g,w,Enemy,0,0,18,26,x,y-12+iy,0,0,false);
			else
				show_piece(g,w,Enemy,19,1,18,26,x,y-12+iy,0,0,false);
				
			if(e.cnt%200<140 && ((e.cnt/2)%4)==0){
				show_piece(g,w,Enemy,2,28,12,10,x-10,y+30-iy,0,0,false);			
				show_piece(g,w,Enemy,2,28,12,10,x+10,y+30-iy,0,0,true);
			}else{
				show_piece(g,w,Enemy,19,28,13,13,x-10,y+30-iy,0,0,false);			
				show_piece(g,w,Enemy,19,28,13,13,x+10,y+30-iy,0,0,true);			
			}
			
			show_piece(g,w,Enemy,96,44,8,10,e.bulx,e.buly,0,0,true);
			
			switch(e.dam_time/2){
				case 0 : show_piece(g,w,Enemy,27,173,17,15,x,y,0,15,inv); break;				
				case 1 : show_piece(g,w,Enemy,44,176,15,12,x,y,0,15,inv); break;				
			}
			break;
			
			case CerberusBoss.CERBERUS :
			
			// Right Arm
			switch(e.rarm){
				case 0 :
				show_piece(g,w,Enemy,262,138,60,46,x-49,y-51,0,0,false);
				show_piece(g,w,Enemy,100,0,19,72,x-29,y-109,0,0,false);
				break;
				
				case 1 :
				show_piece(g,w,Enemy,197,138,65,39,x-18,y-153,0,0,false);
				show_piece(g,w,Enemy,151,0,33,59,x-29,y-147,0,0,false);
				break;
				
				case 2 :				
				show_piece(g,w,Enemy,217,0,48,71,x-43,y-109,0,0,false);
				show_piece(g,w,Enemy,262,138,60,46,x-79,y-50,0,0,false);
				break;
				
			}
				
			// Legs
			switch((e.frame)%6){
				case 0 :
				show_piece(g,w,Enemy,207,72,38,65,x-20,y-57,0,0,false);
				show_piece(g,w,Enemy,100,73,31,62,x+13,y-54,0,0,false);
				break;
				
				case 1 :
				show_piece(g,w,Enemy,131,72,34,58,x-16,y-50,0,0,false);
				show_piece(g,w,Enemy,100,73,31,62,x+13,y-54,0,0,false);
				break;

				case 2 :
				show_piece(g,w,Enemy,100,73,31,62,x-4,y-54,0,0,false);
				show_piece(g,w,Enemy,165,72,42,58,x-1,y-50,0,0,false);				
				break;
				
				case 3 :
				show_piece(g,w,Enemy,100,73,31,62,x-7,y-54,0,0,false);
				show_piece(g,w,Enemy,207,72,38,65,x+1,y-57,0,0,false);
				break;				
				
				case 4 :
				show_piece(g,w,Enemy,131,72,34,58,x-11,y-50,0,0,false);
				show_piece(g,w,Enemy,207,72,38,65,x-1,y-57,0,0,false);
				break;				
				
				case 5 :
				show_piece(g,w,Enemy,165,72,42,58,x-24,y-50,0,0,false);
				show_piece(g,w,Enemy,100,73,31,62,x+13,y-54,0,0,false);
				break;
								
			}
			// Body
			show_piece(g,w,Enemy,274,63,49,75,x,y-102,0,0,false);
			// Left Arm
			switch(e.larm){
				case 0 :
				show_piece(g,w,Enemy,262,138,60,46,x+12,y-51,0,0,false);
				show_piece(g,w,Enemy,119,0,31,70,x+19,y-108,0,0,false);			
				break;
				
				case 1 :
				show_piece(g,w,Enemy,197,138,65,39,x+56,y-144,0,0,false);
				show_piece(g,w,Enemy,184,0,33,61,x+27,y-141,0,0,false);			
				break;
				
				case 2 :				
				show_piece(g,w,Enemy,265,0,57,63,x+5,y-95,0,0,false);			
				show_piece(g,w,Enemy,262,138,60,46,x-35,y-43,0,0,false);
				break;
				
			}
			
			// Head
			for(int i=0; i<3; i++){
				switch(i){
					case 0 : ix=-23; iy=0; break;
					case 1 : ix=-8; iy=-3; break;
					case 2 : ix=+7; iy=-1; break;
				}
				if(e.life<800-(200*i))
					show_piece(g,w,Enemy,121,144,24,32,x+ix,y-113+iy,0,0,false);
				else
				switch(((e.cnt/3)+(3*i))%20){
					default : show_piece(g,w,Enemy,246,71,26,31,x+ix,y-121+iy,0,0,false); break;
					case 2 :
					case 0 : show_piece(g,w,Enemy,245,102,30,35,x+ix,y-125+iy,0,0,false); break;			
					case 1 : show_piece(g,w,Enemy,93,136,28,39,x+ix,y-128+iy,0,0,false); break;
				}			
			}
			// Blood			
			switch(e.dam_time/2){
				case 0 : show_piece(g,w,Enemy,145,137,14,11,x-15,y-120,0,0,inv); break;				
				case 1 : show_piece(g,w,Enemy,145,155,22,22,x-15,y-120,0,0,inv); break;								
				case 2 : show_piece(g,w,Enemy,172,136,25,29,x-15,y-120,0,0,inv); break;								
			}				
						
			//Fireball
			if(e.bul_on){
				if(e.cnt%2==0) show_piece(g,w,Enemy,21,120,21,21,e.bulx-10,e.buly-10,0,0,false);
				else show_piece(g,w,Enemy,21,145,21,21,e.bulx-10,e.buly-10,0,0,false);
			} 
			break;
						
		}
		
		if(e.dead)
		switch(e.type){
						
			case CerberusBoss.WIDOW :											
			for(int i=0; i<10; i++){
				ix=w.rnd.nextInt()%40;
				iy=w.rnd.nextInt()%60;
										
				switch((e.cnt+1)%4){				
					case 0 : show_piece(g,w,Enemy,137,79,8,12,x,y,ix,iy,inv); break;
					case 1 : show_piece(g,w,Enemy,146,79,7,18,x,y,ix,iy,inv); break;
					case 2 : show_piece(g,w,Enemy,141,98,12,22,x,y,ix,iy,inv); break;				
				}										
			}
			break;												
			
			case CerberusBoss.VIPER :
			for(int i=5; i>=0; i--){
				cnt=(30*3)-(8*i);
				if(cnt<150){					
					if(e.dir==1) {x=e.x-(60*Trig.cos(cnt)>>10); inv=true;}
					if(e.dir==2) {x=e.x+(60*Trig.cos(cnt)>>10); inv=false;}
					y=e.y-(120*Trig.sin(cnt)>>10);
					// Blood					
					switch((e.cnt+i)%3){				
						case 0 : show_piece(g,w,Enemy,149,102,13,12,x,y,0,-14,inv); break;
						case 1 : show_piece(g,w,Enemy,150,115,12,10,x,y,0,-14,inv); break;
						case 2 : show_piece(g,w,Enemy,150,126,12,9,x,y,0,-14,inv); break;				
					}									
				}
			}									
			break;
			
			case CerberusBoss.BIRD :
			for(int i=0; i<10; i++){
				ix=w.rnd.nextInt()%60;
				iy=w.rnd.nextInt()%40;
										
				switch((e.cnt+1)%4){				
					case 0 : show_piece(g,w,Enemy,27,173,17,15,x,y,ix,iy,inv); break;				
					case 1 : show_piece(g,w,Enemy,44,176,15,12,x,y,ix,iy,inv); break;						
				}										
			}			
			break;
		}
		
		/*g.setColor(255,0,0);
		g.setClip(e.damage.x1-w.bx,BEGINY+e.damage.y1-w.by,e.damage.x2-e.damage.x1,e.damage.y2-e.damage.y1);
		g.fillRect(e.damage.x1-w.bx,BEGINY+e.damage.y1-w.by,e.damage.x2-e.damage.x1,e.damage.y2-e.damage.y1);							*/
		
				
		/*g.setColor(0,255,0);
		g.setClip(e.body.x1-w.bx,BEGINY+e.body.y1-w.by,e.body.x2-e.body.x1,e.body.y2-e.body.y1);
		g.fillRect(e.body.x1-w.bx,BEGINY+e.body.y1-w.by,e.body.x2-e.body.x1,e.body.y2-e.body.y1);*/							
		
	}		
	
	private void show_piece(Graphics g, CerberusWorld w,  Image im, int ix, int iy, int sx, int sy, int px, int py, int dx, int dy, boolean inv)
	{
		if(inv) dx=-dx;
		show_sprite(g,im,ix,iy,sx,sy,px-w.bx+dx-(sx/2),BEGINY-w.by+py+dy,inv);		
	}	
	private void show_piece_vflip(Graphics g, CerberusWorld w,  Image im, int ix, int iy, int sx, int sy, int px, int py, int dx, int dy, boolean inv)
	{
		if(inv) dx=-dx;
		show_sprite_vflip(g,im,ix,iy,sx,sy,px-w.bx+dx-(sx/2),BEGINY-w.by+py+dy,inv);		
	}	
	
	
	public void show_CerberusItem(Graphics g, CerberusWorld w, CerberusItem it)
	{
		int fr=(it.cnt/2)%6;
		if(fr==4) fr=2;
		if(fr==5) fr=1;
		
		if(!it.out_of_world)		
		switch(it.type){
			
			case CerberusItem.ENERGY : 
			show_sprite(g,Armor,217+(9*it.fading),56,8,15,it.x-w.bx-4,BEGINY+it.y-w.by-15,false); 
			break;	
			
			case CerberusItem.HEART : 
			if(it.fading<4) show_sprite(g,Armor,262+(13*it.fading),56,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			else show_sprite(g,Armor,102,134,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			break;	
			
			case CerberusItem.HIKARU : 
			if(it.fading==0) show_sprite(g,Armor,(13*fr),180,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			else if(it.fading<4) show_sprite(g,Armor,(13*it.fading),193,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			else show_sprite(g,Armor,102,134,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			break;	
			
			case CerberusItem.LIFE : 
			if(it.fading==0) show_sprite(g,Armor,52+(13*fr),180,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			else if(it.fading<4) show_sprite(g,Armor,52+(13*it.fading),193,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			else show_sprite(g,Armor,102,134,12,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			break;	
			
			case CerberusItem.IT_SWORD :
			if(it.fading==0) show_sprite(g,Player,230,192,35,11,it.x-w.bx-17,BEGINY+it.y-w.by-10,false); 
			break;
			
			case CerberusItem.IT_WHIP :
			if(it.fading==0) show_sprite(g,Player,215,191,14,13,it.x-w.bx-7,BEGINY+it.y-w.by-13,false); 
			break;
			
			case CerberusItem.IT_BOOMER :
			if(it.fading==0) show_sprite(g,Player,198,191,17,13,it.x-w.bx-7,BEGINY+it.y-w.by-8,false); 
			break;							
			
			case CerberusItem.HELMET :
			if(it.fading==0) show_sprite(g,Armor,128,194,13,12,it.x-w.bx-6,BEGINY+it.y-w.by-12,false); 
			break;							
			
			case CerberusItem.ARMOR :
			if(it.fading==0) show_sprite(g,Armor,104,177,23,18,it.x-w.bx-12,BEGINY+it.y-w.by-18,false); 
			break;							
			
			case CerberusItem.GAUNTLETS :
			if(it.fading==0) show_sprite(g,Armor,128,177,23,16,it.x-w.bx-12,BEGINY+it.y-w.by-16,false); 
			break;							
			
			case CerberusItem.BOOTS :
			if(it.fading==0) show_sprite(g,Armor,152,176,15,20,it.x-w.bx-12,BEGINY+it.y-w.by-16,false); 
			break;							
			
			case CerberusItem.CAPE :
			if(it.fading==0) show_sprite(g,Armor,241,170,26,37,it.x-w.bx-13,BEGINY+it.y-w.by-37,false); 
			break;															
			
			case CerberusItem.PHONE :
			if(it.fading==0) show_sprite(g,Armor,192,180,10,16,it.x-w.bx-5,BEGINY+it.y-w.by-16,false); 
			break;																		
		}	
	}		
	
	void show_fade(Graphics g, int ix, int iy, int ex, int ey, int l)
	{
		
		g.setColor(0,0,0);
		
		for(int i=ix; i<ex; i+=16)	
		for(int j=iy; j<ey; j+=16){
			
			g.setClip(i,j,19,19);			
			g.drawImage(Grid,i,j,20);
			if(l>0) g.drawImage(Grid,i+2,j,20);
			if(l>1) g.drawImage(Grid,i+1,j+1,20);
			if(l>2) g.drawImage(Grid,i+3,j+1,20);
			if(l>3) g.drawImage(Grid,i+2,j+1,20);
			if(l>4) g.drawImage(Grid,i,j+1,20);
			if(l>5) g.drawImage(Grid,i+1,j+2,20);
			if(l>6) g.fillRect(i,j,16,16);
		}
					
	}
	
	byte[] multiread(String f, int lengths[], int n)
	{
		int startpos = 0;

		for(int i = 0; i < n; i++)
		{
			startpos += lengths[i];
		}

		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+f.substring(1));
		byte[] bytes = file.readBytes();

		byte buffer[]=new byte[lengths[n]];

		for(int j = 0; j < buffer.length; j++)
			buffer[j] = bytes[startpos+j];

		return buffer;
		/*System.gc();
		InputStream is = getClass().getResourceAsStream(f);
		int i=0;
		
		while(i<n){
			try{
				for(int t=0; t<lengths[i]; t++)
					is.read();
						
			}catch(Exception exception) {}
			i++;	
		}
	
		byte buffer[]=new byte[lengths[n]];
	
		try	{
		is.read(buffer, 0, buffer.length);
		is.close();
		}catch(Exception exception) {}
		System.gc();
		
		return buffer;	*/
	}		
	
	// *******************
	// -------------------
	// Cheat - Engine
	// ===================
	// *******************
			
	public void CheatRUN(int keycode)
	{
		byte[] Cheat_1={4,4,2,3,3,3,7,7,7,7};
		if (Cheat_1[CheatPos_1++] != keycode) {CheatPos_1=0;}
		if (Cheat_1.length==CheatPos_1) {Cheat_ON=true; CheatPos_1=0;}
		if(Cheat_ON==true) game.world.pl.dam_time=9999;
	}
		
}
