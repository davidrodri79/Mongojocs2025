package com.mygdx.mongojocs.numa;

/*
	Canvas
*/

//import javax.microedition.lcdui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.util.Random;




public class BeastCanvas
//#ifdef NOKIAUI
	extends FullCanvas

{
						
	Font fnts;
	Random rnd;
	static BeastMain game;
	Image Player, Snake, Dragon, Dino, Scorpion, Plant, Toad, Insect, Mate, Bars, Boss, Extras, Picture;
	
	//#ifdef TILE2FILES
	//#else
	Image Tiles;
	//#endif

	
	//#ifdef GFXBIG
	Image Bkg, Bkg1, Bkg2, Bkg3;
	//#endif
	
	//#ifdef INITIALLOAD
	Image allTiles[] = new Image[3];
	//#endif

	byte PlayerCoo[] = new byte[1008], SnakeCoo[], DinoCoo[], MateCoo[],
		ScorpionCoo[], InsectCoo[], PlantCoo[], ToadCoo[],
		DragonCoo[], BossCoo[], ExtrasCoo[] = new byte[576];
	String texts[][];
	int cooSizes[]={180,48,36,576,48,324,108,1008,60,252,108}, tileSizes[]={4345,4312,4106},
		sprSizes[]={369,2689,592,600,2497,525,1712,981,2809,537,1621,1216};			
		
	public Graphics g;
	DirectGraphics dg;
		
	public static int CANVX, CANVY, BEGINY, SCREENY, ScrollPosX, ScrollPosY, TRANSY = 0, loadProgress = 0; 
				
	public boolean painting, listenerKey = false;	
		
	//#ifdef GFXBIG
	int lastLifes=0, lastPower=0, lastHealth=0, lastGems=0;
	//#endif

	
	public static final int mainMenu[] = {
		BeastMain.PLAY, 
		//#ifdef DEVICESOUND
		BeastMain.SOUND, 
		//#endif
		//#ifdef DEVICEVIBRA
		BeastMain.VIBR, 
		//#endif
		//#ifdef NK-s60
		//BeastMain.KEYCONF,
		//#endif
		BeastMain.HELP, 
		BeastMain.ABOUT, 
		BeastMain.QUIT};
		
	public static final int inGameMenu[] = {
		BeastMain.RESUME, 
		//#ifdef DEVICESOUND
		BeastMain.SOUND, 
		//#endif
		//#ifdef DEVICEVIBRA
		BeastMain.VIBR, 
		//#endif
		//#ifdef NK-s60
		//BeastMain.KEYCONF,
		//#endif
		BeastMain.HELP, 
		BeastMain.ABORT, 
		BeastMain.QUIT};
	
				
	public BeastCanvas(BeastMain main)
	{		
		game=main;	
		
		//#ifdef FONTBOLD
		fnts=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD , Font.SIZE_SMALL);		
		//#else
		//fnts=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN , Font.SIZE_SMALL);
		//#endif

		
		//#ifdef MIDP20
		//setFullScreenMode(true);
		//#endif
			
		CANVX=getWidth();
		CANVY=getHeight();
		

		
		//#elifdef GFXBIG
			BEGINY=43; SCREENY=(CANVY - BEGINY - 20);

		controlReset();
		
						
		//#ifdef CLISTENER
		//TODO ListenerSET(this);
		//#endif
		
		texts = textCreate(LoadFileCheckSize("/Text.txt"));
													
	}
	
	
	public void initialLoad()
	{
		byte buffer[];
		
		loadProgress = 0; loadProgressInc(0);	
	
		//#ifdef GFXBIG
		
			Player = loadImage("/Player.png");						
			Extras = loadImage("/Extras.png");			
			Bars = loadImage("/Bars.png");
			
			loadProgressInc(10);	// 10
						
			PlayerCoo = LoadFile("/Player.coo",PlayerCoo.length);
			ExtrasCoo = LoadFile("/Extras.coo",ExtrasCoo.length);
			
			loadProgressInc(10);	// 20


			Bkg = Image.createImage(96,250);

			loadProgressInc(10);	// 30
			
		//#else
		//#endif
		
		//#ifdef INITIALLOAD
		SnakeCoo = new byte[252];
		DragonCoo = new byte[36];
		DinoCoo = new byte[48];
		ScorpionCoo = new byte[80];
		PlantCoo = new byte[108];
		ToadCoo = new byte[108];					
		InsectCoo = new byte[48];					
		MateCoo = new byte[324];				
		BossCoo = new byte[180];		
				
		PlantCoo = LoadFile("/Plant.coo",PlantCoo.length);
		ScorpionCoo = LoadFile("/Scorpion.coo",ScorpionCoo.length);
		DinoCoo = LoadFile("/Dino.coo",DinoCoo.length);
		DragonCoo = LoadFile("/Dragon.coo",DragonCoo.length);
		ToadCoo = LoadFile("/Toad.coo",ToadCoo.length);
		MateCoo = LoadFile("/Mate.coo",MateCoo.length);
		InsectCoo = LoadFile("/Insect.coo",InsectCoo.length);
		SnakeCoo = LoadFile("/Snake.coo",SnakeCoo.length);		
		BossCoo = LoadFile("/Boss.coo",BossCoo.length);
		
		loadProgressInc(5);	// 35
									
		for(int i = 0; i<3; i++)
			allTiles[i] = loadImage("/Tiles"+(i+1)+".png"); 
			
		loadProgressInc(10);	// 45
																											
		Snake = loadImage("/Snake.png");
		loadProgressInc(3);	// 48
		Dragon = loadImage("/Dragon.png"); 
		loadProgressInc(3);	// 51
		Dino = loadImage("/Dino.png"); 
		loadProgressInc(3);	// 54
		Scorpion = loadImage("/Scorpion.png"); 
		loadProgressInc(3);	// 57
		Plant = loadImage("/Plant.png"); 
		loadProgressInc(3);	// 60
		Toad = loadImage("/Toad.png"); 
		loadProgressInc(3);	// 63
		Insect = loadImage("/Insect.png"); 
		loadProgressInc(3);	// 66
		Mate = loadImage("/Mate.png"); 								
		loadProgressInc(3);	// 69											 
		Boss = loadImage("/Boss.png");				 																							
		loadProgressInc(3);	// 72
		Bkg1 = loadImage("/Bkg1.png");
		loadProgressInc(3);	// 75
		Bkg2 = loadImage("/Bkg2.png");
		loadProgressInc(3);	// 78
		Bkg3 = loadImage("/Bkg3.png");						
		loadProgressInc(3);	// 81
		//#else
		//#endif

		snd = new Music[7];

		for(int i = 0; i < snd.length; i++)
			snd[i] = LoadMidi(i+".mid");

		Picture = loadImage("/Logo.png");
		
		loadProgressInc(9);	// 90

									
		System.gc();		
		
		loadProgressInc(10);	// 100
		loadProgress = 0;
		
	}
			

	

	//#elifdef MIDP20SOUND
	static Music snd[];
	//#endif

	
	static int soundOld = -1;

	//#elifdef MIDP20SOUND
	
		public Music LoadMidi(String Name)
		{
		
			/*javax.microedition.media.Player p = null;
		
			try
			{
				InputStream is = getClass().getResourceAsStream(Name);
				p = Manager.createPlayer( is , "audio/midi");
				p.realize();			
				//#ifndef NOMIDIPREFETCH
				p.prefetch();
				//#endif
			}
			catch(Exception exception) {exception.printStackTrace();}
		
			return p;*/
			return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Name));
		}
	//#endif


	public static void playSound(int Ary, int Loop)
	{

		soundStop();

		//#ifdef J2ME

			//#elifdef MIDP20SOUND
			try{
						
				if(!game.sound) return;								
						
				if(Ary>=0){			
					//#ifdef NOMIDIPREFETCH
					//#endif
					//VolumeControl vc = (VolumeControl) snd[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(100);}
					snd[Ary].setLooping(( Loop > 0 ? false : true));
					snd[Ary].play();
					soundOld = Ary;
				}
				
			}catch(Exception exception) {exception.printStackTrace();}
			//#endif
		
		//#endif
								
	}
		
	public static void soundStop()
	{

		//#elifdef MIDP20SOUND	
		try{
			if(soundOld != -1){
				snd[soundOld].stop();			
			}
		}catch(Exception exception) {exception.printStackTrace();} 							
		//#endif

	}
	
	
	public void scrollInitialize()
	{

	}
	
	public Image loadImage(String file)
	{
		Image im = null;
		try{			
		
			 im = Image.createImage(file);		
							
		}catch(Exception err) {
			//System.out.println ("Error cargando "+file);
		}
		return im;			
	}
	
	public void destroyLogo()
	{
		Picture = null;
		loadPicture();	
	}
	
	public void loadPicture()
	{
					
		if(Picture == null)
			Picture = loadImage("/Picture.png");		
												
	}
	
	public void destroyPicture()
	{
		//#ifndef INITIALLOAD
		//Picture = null; System.gc();
		//#endif
	}
	
	public void loadLevelGfx()
	{		
		loadProgress = 0; loadProgressInc(0);			
			

		//#ifdef GFXBIG
		Tiles = allTiles[game.world.tileSet];
		//#endif


		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Graphics gg = Bkg.getGraphics();

				int sky[][][]={{{25,113,134},{251,212,93}},
						{{158,11,14},{255,247,153}},
						{{13,2,0},{8,76,115}}};
				int caves[][][]={{{147,48,29},{223,188,117}},
						{{190,148,148},{110,67,67}},
						{{51,46,31},{126,113,76}},
						{{46,28,0},{127,105,46}},
						{{0,0,0},{255,0,0}}
				};

				if(game.world.tileSet==0){

					//#ifndef INITIALLOAD
					//Bkg1 = loadImage("/Bkg1.png");
					//#endif

					int r1=sky[game.world.level][0][0], r2=sky[game.world.level][1][0], g1=sky[game.world.level][0][1],
							g2=sky[game.world.level][1][1], b1=sky[game.world.level][0][2], b2=sky[game.world.level][1][2];

					gg.setClip(0,0,288,250);
					gg.setColor(r1,g1,b1);
					gg.fillRect(0,0,288,140);
					for(int i = 0; i<20; i++){
						gg.setColor(((r2*i)+(r1*(19-i)))/19,
								((g2*i)+(g1*(19-i)))/19,
								((b2*i)+(b1*(19-i)))/19);
						gg.fillRect(0,98+2*i,288,2);
					}
					gg.setColor(2,45,42);
					gg.fillRect(0,160,288,200);
					gg.drawImage(Bkg1,0,122,20);
					gg.drawImage(Bkg1,96,122,20);
					gg.drawImage(Bkg1,192,122,20);

					if(game.world.level==2){
						for(int i=0; i<30; i++){
							int x=Math.abs(game.rnd.nextInt())%96, y=Math.abs(game.rnd.nextInt())%110, l=1+Math.abs(game.rnd.nextInt())%5;
							gg.setColor(255/l,255/l,255/l);
							gg.fillRect(x,y,1,1);
							gg.fillRect(96+x,y,1,1);
							gg.fillRect(192+x,y,1,1);
						}
					}
				}



				if(game.world.tileSet==1){

					//#ifndef INITIALLOAD
					//Bkg2 = loadImage("/Bkg2.png");
					//#endif

					int l=game.world.level-3;
					int r1=caves[l][0][0], r2=caves[l][1][0], g1=caves[l][0][1],
							g2=caves[l][1][1], b1=caves[l][0][2], b2=caves[l][1][2];

					gg.setClip(0,0,288,250);

					gg.setColor(r1,g1,b1);
					gg.fillRect(0,0,288,50);

					gg.setColor(r1,g1,b1);
					gg.fillRect(0,0,288,98);
					gg.setColor(r2,g2,b2);
					gg.fillRect(0,198,288,98);

					for(int i = 0; i<50; i++){
						gg.setColor(((r2*i)+(r1*(49-i)))/49,
								((g2*i)+(g1*(49-i)))/49,
								((b2*i)+(b1*(49-i)))/49);
						gg.fillRect(0,50+3*i,288,3);
					}

					gg.drawImage(Bkg2,0,0,20);
					gg.drawImage(Bkg2,96,0,20);
					gg.drawImage(Bkg2,192,0,20);

				}

				if(game.world.tileSet==2){

					//#ifndef INITIALLOAD
					//Bkg3 = loadImage("/Bkg3.png");
					//#endif

					int l=game.world.level-3;
					int r1=caves[l][0][0], r2=caves[l][1][0], g1=caves[l][0][1],
							g2=caves[l][1][1], b1=caves[l][0][2], b2=caves[l][1][2];

					gg.setClip(0,0,288,250);

					gg.setColor(r1,g1,b1);
					gg.fillRect(0,0,288,98);
					gg.setColor(r2,g2,b2);
					gg.fillRect(0,98,288,198);

					for(int i = 0; i<50; i++){
						gg.setColor(((r2*i)+(r1*(49-i)))/49,
								((g2*i)+(g1*(49-i)))/49,
								((b2*i)+(b1*(49-i)))/49);
						gg.fillRect(0,75+2*i,288,4);
					}

					//#ifdef GFXHUGE
					//#else
					gg.drawImage(Bkg3,0,0,20);
					gg.drawImage(Bkg3,96,0,20);
					gg.drawImage(Bkg3,192,0,20);
					//#endif

				}
				//#endif

				gg.setClip(0,0,176,208);
				Bkg.flipVertical();
			}
		});


		loadProgress = 100;	loadProgressInc(0);		
		loadProgress = 0;
	}
	
	//#ifdef GFXBIG
	void showSampleSky()
	{
		
		int r1=13, r2=8, g1=2, g2=76, b1=0, b2=115;
			
		g.setClip(0,0,288,CANVY);
		g.setColor(r1,g1,b1);
		g.fillRect(0,0,288,140);				
		for(int i = 0; i<20; i++){
			g.setColor(((r2*i)+(r1*(19-i)))/19,
						((g2*i)+(g1*(19-i)))/19,
						((b2*i)+(b1*(19-i)))/19);
			g.fillRect(0,98+2*i,288,2);
		}				
		g.setColor(2,45,42);
		g.fillRect(0,138,288,CANVY-138);		
		
		//#ifdef INITIALLOAD
		g.drawImage(Bkg1,0,122,20);
		g.drawImage(Bkg1,96,122,20);
		g.drawImage(Bkg1,192,122,20);		
		//#endif
		
		/*g.drawImage(Bkg1,0,122,20);
		g.drawImage(Bkg1,96,122,20);
		g.drawImage(Bkg1,192,122,20);*/
				
		/*g.setColor(255,255,255);
		for(int i=0; i<30; i++){
			int x=Math.abs(i*173767657)%96, y=Math.abs(i*173767657)%110;
			g.fillRect(x,y,1,1);
			g.fillRect(96+x,y,1,1);			
		}*/
		
		/*g.setClip(0,0,CANVX,CANVY);
		g.clipRect(0,0,CANVX,CANVY);
		g.drawImage(Picture,0,0,20);*/
						
	}
	//#endif	
	
	public void resetScroll()
	{
		ScrollPosX=0; ScrollPosY=0;	
	}
	
	public void destroyLevelGfx()
	{

		System.gc();
		System.gc();				
	}
						
	public void keyPressed(int keycode)
	{
		boolean none = false;
	
		controlReset();
				
		if(!game.keyConf)				
		switch(keycode)
		{
			default : none = true; break;
			case Canvas.KEY_NUM2 : up=true; break;
			case Canvas.KEY_NUM4 : left=true; break;
			case Canvas.KEY_NUM5 : but1=true; break;
			case Canvas.KEY_NUM6 : right=true; break;
			case Canvas.KEY_NUM8 : down=true; break;
			case Canvas.KEY_NUM1 : up=true; left=true; break;
			case Canvas.KEY_NUM7 : down=true; left=true; break;
			case Canvas.KEY_NUM3 : up=true; right=true; break;
			case Canvas.KEY_NUM9 : down=true; right=true; break;
			case Canvas.KEY_NUM0 : but2=true; break;
			case Canvas.KEY_STAR : but3=true; break;
													
			//#ifdef NOKIAUI
			case -7 : but1=true; break;
			case -6 : but2=true; break;
			//#endif
			

		}
		
		/*if(none)
		switch(getGameAction(keycode))
		{
			
			//#ifdef VI-TSM100
			//#else
			case LEFT  : left=true; break;
			case RIGHT : right=true; break;
			case UP    : up=true; break;
			case DOWN  : down=true; break;
			case FIRE  : but1=true; break;
			//#endif
		
			//case GAME_A: but2=true; break;
			//case GAME_B: but3=true; break;
		}	*/
		
		anybut=but1 || but2 || but3 || but4;
		any=up || down || left || right || anybut;							
	}
	
	public void keyReleased(int keycode)
	{
		controlReset();
	}
									
	public void paint(Graphics g2)
	{		

		painting=true;
		String opStr="";
		
		g=g2;
		dg = DirectUtils.getDirectGraphics(g);
		int x, y, min, sec;
		
		try{

			
		//#ifdef FRAMESKIP
		//if(game.cnt % 2 == 0 && game.state != BeastMain.LOADING) return;
		//#endif
																
		if(game.cnt>1)						
		switch(game.state) {
			
			case BeastMain.LOGO :
			clearDisplay(255,255,255);
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Picture,(CANVX-Picture.getWidth())/2,(CANVY-Picture.getHeight())/2,20);
			break;
			
			case BeastMain.TITLE :
			clearDisplay(0,0,0);
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Picture,(CANVX-Picture.getWidth())/2,(Picture.getHeight() < CANVY ? -10 : 0)+(CANVY-Picture.getHeight())/2,20);
			break;
			
			case BeastMain.MAIN_MENU :
			//clearDisplay(0,0,0);
			clearDisplay(0,0,0);
			g.setClip(0,0,CANVX,CANVY);
			g.drawImage(Picture,(CANVX-Picture.getWidth())/2,(Picture.getHeight() < CANVY ? -10 : 0)+(CANVY-Picture.getHeight())/2,20);

			switch(mainMenu[game.option]){
			
				case BeastMain.PLAY :	opStr = texts[TEXT_PLAY][0]; break;
				case BeastMain.SOUND :	opStr = (game.sound ? texts[TEXT_SOUND][1] : texts[TEXT_SOUND][0]); break;
				case BeastMain.VIBR :	opStr = (game.vibr ? texts[TEXT_VIBRA][1] : texts[TEXT_VIBRA][0]); break;
				case BeastMain.KEYCONF : opStr = (game.keyConf ? texts[21][1] : texts[21][0]); break; 
				case BeastMain.ABOUT :	opStr = texts[TEXT_ABOUT][0]; break;
				case BeastMain.HELP :	opStr = texts[TEXT_HELP][0]; break;
				case BeastMain.QUIT :	opStr = texts[TEXT_EXIT][0]; break;
			}
			showFontTextCenter("< "+opStr+" >",CANVY-10-(fnts.getHeight()/2),0);
			break;
			
			case BeastMain.LEVEL_SELECT :
			//#ifdef GFXBIG
			showSampleSky();			
			//#else
			//clearDisplay(0,0,0);
			//#endif
			showFontTextCenter(texts[13][0],CANVY/4,0);
			showFontTextCenter((game.level>0 ? "< " : "")+(game.level+1)+(game.level<game.lastLev ? " >" : ""),2*CANVY/4,0);
			showFontTextCenter(texts[20][0]+(game.pickedGems[game.level])+"/"+game.world.items[game.level].length,3*CANVY/4,0);
			break;
			
			case BeastMain.INITIALLOAD :			
			case BeastMain.LOADING :			
			clearDisplay(0,0,0);
			
			showFontTextCenter(texts[22][0],(CANVY/3),0);			
			
			x = (CANVX - 84)/2; y = 2*CANVY/3;		
			g2.setClip(0,0,CANVX,CANVY);
			g2.setColor(224,249,105);
			g2.drawRect(x,y,83,9);
			g2.fillRect(x+2,y+2,80*loadProgress/100,6);
			
			break;			
			
			case BeastMain.LEVEL_LOAD :
			//#ifdef GFXBIG
			showSampleSky();			
			//#else
			//clearDisplay(0,0,0);
			//#endif
			showFontTextCenter(texts[18][0]+" "+(1+game.level),(CANVY/4),0);
			//#ifdef GFXBIG
			PutSprite(Extras,(CANVX/2)-32,(CANVY/2)-16,24,61+((game.cnt/4)%3),ExtrasCoo,1,false);
			//#else
			//#endif
			showFontText("x"+(BeastWorld.minimum[game.level]),(CANVX/2)-4,(CANVY/2),0);
			showFontTextCenter(texts[14][0],3*(CANVY/4),0);
			break;
			
			
			case BeastMain.EXPLAIN :			
			//#ifndef GFXBIG

			//#ifdef NOEXPLAINSCROLL
				//#else
			int speed=768;

			showFontText(texts[17][game.helpType],CANVX-((game.cnt*speed)>>8),CANVY-17,0);
			//#endif
			g.setColor(79,165,169);
			g.drawRoundRect(0,CANVY-20,CANVX-1,19,6,6);			
			//#else
			showGameAction();
				//#else
				g.setClip(29,0,143,CANVY);			
				showFontTextNoClip(texts[17][game.helpType],CANVX-(game.cnt*3),CANVY-10-(fnts.getHeight()/2),0);						
				//#endif
			//#endif
			break;			
			
						
			case BeastMain.GAME_ACTION :			
			showGameAction();						
			if(game.needMore) showFontTextCenter(texts[15][0],(CANVY/4),0);
			break;
			
			case BeastMain.IN_GAME_MENU :
			//clearDisplay(0,0,0);
			showGameAction();				
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(0,0,0);

			//#else
			g.fillRect(0,CANVY-20,CANVX,20);		
			//#endif
			switch(inGameMenu[game.option]){
			
				case BeastMain.RESUME :	opStr = texts[TEXT_CONTINUE][0]; break;
				case BeastMain.SOUND :	opStr = (game.sound ? texts[TEXT_SOUND][1] : texts[TEXT_SOUND][0]); break;
				case BeastMain.VIBR :	opStr = (game.vibr ? texts[TEXT_VIBRA][1] : texts[TEXT_VIBRA][0]); break;			
				case BeastMain.KEYCONF : opStr = (game.keyConf ? texts[21][1] : texts[21][0]); break;	
				case BeastMain.HELP :	opStr = texts[TEXT_HELP][0]; break;
				case BeastMain.ABORT :	opStr = texts[TEXT_RESTART][0]; break;
				case BeastMain.QUIT :	opStr = texts[TEXT_EXIT][0]; break;
			}

			//#else
			showFontTextCenter("< "+opStr+" >",CANVY-10-(fnts.getHeight()/2),0);
			//#endif
			break;								
			
			case BeastMain.LEVEL_ENDED :
			//#ifdef GFXBIG
			showSampleSky();			
			//#else
			//clearDisplay(0,0,0);
			//#endif
			showFontTextCenter(texts[19][0],CANVY/3,0);
			showFontTextCenter(texts[20][0]+" "+game.pl.gems+"/"+game.world.items[game.level].length,2*CANVY/3,0);
			break;
			
			case BeastMain.GAME_OVER :			
			showGameAction();						
			showFontTextCenter(texts[TEXT_GAMEOVER][0],(CANVY/3),0);
			break;
			
			case BeastMain.CONTROLS :
			case BeastMain.CONTROLS2 :
			//#ifdef GFXBIG
			showSampleSky();			
			//#else
			//clearDisplay(0,0,0);
			//#endif
			int plus=(game.keyConf ? 5 : 0);
			showFontTextCenter(texts[TEXT_HELP_SCROLL][0],CANVY/7,1);
			showFontTextCenter(texts[TEXT_HELP_SCROLL][1+plus],2*CANVY/7,0);
			showFontTextCenter(texts[TEXT_HELP_SCROLL][2+plus],3*CANVY/7,0);
			showFontTextCenter(texts[TEXT_HELP_SCROLL][3+plus],4*CANVY/7,0);
			showFontTextCenter(texts[TEXT_HELP_SCROLL][4+plus],5*CANVY/7,0);
			showFontTextCenter(texts[TEXT_HELP_SCROLL][5+plus],6*CANVY/7,0);
			break;
			
			case BeastMain.CREDITS :
			//#ifdef GFXBIG
			showSampleSky();			
			//#else
			//clearDisplay(0,0,0);
			//#endif
			
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][0],CANVY-2*game.cnt,1);
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][1],CANVY-2*game.cnt+30,1);
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][2],CANVY-2*game.cnt+50,0);
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][3],CANVY-2*game.cnt+80,1);
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][4],CANVY-2*game.cnt+100,0);
			//#ifdef DEVICESOUND 
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][5],CANVY-2*game.cnt+130,1);
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][6],CANVY-2*game.cnt+150,0);
			//#endif
			showFontTextCenter(texts[TEXT_ABOUT_SCROLL][7],CANVY-2*game.cnt+180,1);			
			//#ifndef GFXBIG
			//#endif
			break;			
			
			case BeastMain.GAME_END :
			//#ifdef GFXBIG
			showSampleSky();			
			//#else
			//clearDisplay(0,0,0);
			//#endif
			
			//#else
			showFontTextCenter(texts[16][0],CANVY-game.cnt/2,1);
			showFontTextCenter(texts[16][1],CANVY-game.cnt/2+30,0);
			showFontTextCenter(texts[16][2],CANVY-game.cnt/2+50,0);
			showFontTextCenter(texts[16][3],CANVY-game.cnt/2+70,0);
			showFontTextCenter(texts[16][4],CANVY-game.cnt/2+90,0);
			showFontTextCenter(texts[16][5],CANVY-game.cnt/2+110,0);
			
			showFontTextCenter(texts[20][0]+" "+game.pl.power+"/"+game.TOTALGEMS,CANVY-game.cnt/2+130,0);									
			//#endif

			break;			
						
			
			//case BeastMain.LOST_LIFE :			
			//showGameAction();
			//break;					
		}	
									
		}catch (Exception e)
		{
			e.printStackTrace();
			/*showFontText(e.toString(),0,0,0); */
		}
		
		painting=false;

	}	
	
	public void showGameAction()
	{

			int x, y, sx, sy, bx, by;
			
			//#ifndef GFXBIG
			//clearDisplay(0,0,0);
			//#else
				//#ifdef CLEARDISPLAY
				//clearDisplay(0,0,0);
				//#endif
			//#endif
					
			//g.translate(0,BEGINY); 
			TRANSY = BEGINY;
			
			//#ifdef FRAMESKIP
			//updateScrollPos();
			//#endif
			updateScrollPos();

			
			//#ifdef GFXBIG
			g.setClip(0,TRANSY,CANVX,SCREENY);
			//#ifdef GFXHUGE
			//y = TRANSY;
			//#else
			y = TRANSY+20-(game.world.sizeY*4)-(ScrollPosY/4);
			//#endif

			g.drawImage(Bkg,-((ScrollPosX/4)%96),y,20);
			g.drawImage(Bkg,96-((ScrollPosX/4)%96),y,20);
			g.drawImage(Bkg,192-((ScrollPosX/4)%96),y,20);
			g.drawImage(Bkg,288-((ScrollPosX/4)%96),y,20);
						
			int bi=ScrollPosX/24, bj=ScrollPosY/24;
			
			for(int j=bj; j<bj+(SCREENY/24)+2; j++)
			if(j<game.world.sizeY)								
			for(int i=bi; i<bi+(CANVX/24)+2; i++)
			{
				if(i<game.world.sizeX){								
					x=(i*24)-ScrollPosX;
					y=(j*24)-ScrollPosY;
					bx=x; by=y; sx=24; sy=24;
				
					int t=(j >= 0 && j < game.world.sizeY ? game.world.map[(j*game.world.sizeX)+i] : -1);
					
					if(bx<0) {sx+=bx; bx=0;}
					if(by<0) {sy+=by; by=0;}
					
					if(bx+sx>=CANVX) {sx+=(CANVX-(bx+sx));}
					if(by+sy>=SCREENY) {sy+=(SCREENY-(by+sy));}
				
					g.setClip(bx,by+TRANSY,sx,sy);
					g.clipRect(bx,by+TRANSY,sx,sy);
					if(t < 0)
					{
						//g.setColor(0,0,0);
						//g.fillRect(bx,by+TRANSY,24,24);	
					}
					else if(t != (game.world.tileSet == 0 ? 2 : 1)) 
						g.drawImage(Tiles,x-24*(t%8),y+TRANSY-24*(t/8),20);
				}
				
			}			
			//#endif
			
						
			//showSquare(game.pl.body,255,0,0);			
			showPlayer(game.pl);
			
			
						
			// Items
			for(int i=0; i<game.world.items[game.world.level].length; i++)
				if(!game.world.itemTaken[i]){
					//#ifndef GFXBIG
					//#else
					x = (game.world.items[game.world.level][i][0]*24)-18-ScrollPosX;
					y = (game.world.items[game.world.level][i][1]*24)-24-ScrollPosY;
					if(x>=-36 && x<CANVX)
						PutSprite(Extras,x,y,24,61+((game.cnt/4)%3),ExtrasCoo,1,false);					
					//#endif
			}
								
			for(int i=0; i<game.en.length; i++)
				if(game.en[i]!=null)
					if(!game.en[i].outOfWorld){
						//showSquare(game.en[i].body,0,0,255);
						showEnemy(game.en[i]);
					}
												
			if(game.bo!=null) showBoss(game.bo);
								
			for(int i=0; i<game.world.shots.length; i++){
				
				BeastShot s=game.world.shots[i];
				if(s!=null){
					//showSquare(s.damage,0,255,0);
					int fr;
					//#ifndef GFXBIG ODINNN
					//#else
					switch(s.type){
						case 0 : fr=92; PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);	break;		 
						case 1 : fr=80+(s.cnt%3); PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);	break;
						case 2 : fr=80+(s.cnt%3); PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);
								 fr=92+(s.cnt/10); 
								 PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18+(sin(s.cnt*16)>>7)-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);	
								 PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18-(sin(s.cnt*16)>>7)-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);	
								 break;								 
						case 3 : fr=92+(s.cnt/10); 
								 PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18+(sin(s.cnt*16)>>7)-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);	
								 PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18-(sin(s.cnt*16)>>7)-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);	
								 fr=88+(s.cnt%4); PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-10-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);	
								 break;								 								 
						case 4 : 
								 for(int j=3; j>=0; j--)
								 	PutSprite(Extras,(s.x*3)/2-18-ScrollPosX-j*(s.dir==1 ? -6 : 6),(s.y*3)/2-18-ScrollPosY,36,41+((j+s.cnt)%4),ExtrasCoo,1,s.dir==1);									 
								 fr=36+(s.cnt%4); 
								 PutSprite(Extras,(s.x*3)/2-18-ScrollPosX,(s.y*3)/2-18-ScrollPosY,36,fr,ExtrasCoo,1,s.dir==1);									 
								 break;
								 
						case 10: fr=5; PutSprite(Snake,(s.x*3)/2-16-ScrollPosX,(s.y*3)/2-16-ScrollPosY,32,fr,SnakeCoo,2,s.dir==1); break;
						case 11: fr=9+(s.cnt/4); PutSprite(Toad,(s.x*3)/2-16-ScrollPosX,(s.y*3)/2-16-ScrollPosY,32,fr,ToadCoo,s.dir==1);	break;
						case 12: fr=3; PutSprite(Insect,(s.x*3)/2-16-ScrollPosX,(s.y*3)/2-16-ScrollPosY,32,fr,InsectCoo,s.dir==1);	break;
						case 13: if(s.cnt<3) fr=14; else if(s.cnt<6) fr=15; else fr=16; PutSprite(Mate,(s.x*3)/2-16-ScrollPosX,(s.y*3)/2-16-ScrollPosY,32,fr,MateCoo,2,s.dir==1); break;
						case 14: PutSprite(Boss,(s.x*3)/2-21-ScrollPosX,(s.y*3)/2-21-ScrollPosY,42,27+((s.cnt/4)%2),BossCoo,1,false); break;
					}					
					//#endif
				}
			}
			
			for(int i=0; i<game.world.effects.length; i++){
				
				BeastShot e=game.world.effects[i];								
				if(e!=null){
				//#ifndef GFXBIG
					// #else
					int fr=0, alpha, offs=0;
					switch(e.type){
						case BeastShot.DINO_EAT : fr=72+(e.cnt/3); offs=6; break;						
						case BeastShot.SHOT_SPARK : fr=48+(e.cnt/4); break;											
						case BeastShot.EXPLOSION2 : fr=(e.cnt/2); break;											
						case BeastShot.EXPLOSION : fr=(e.cnt/2); if(fr>=3) fr+=12; break;											
						case BeastShot.TAKE_ITEM : fr=84+(e.cnt/4); offs=14; break;											
						case BeastShot.WASTE : fr=53+(e.cnt/3); break;											
					}
					PutSprite(Extras,(e.x*3)/2-18-ScrollPosX,(e.y*3)/2-36-offs-ScrollPosY,36,fr,ExtrasCoo,1,false);				
				//#endif
				}
			}	

			
			//g.translate(0,-BEGINY);
			TRANSY = 0;
			
			
			//#ifndef GFXBIG
			//#else				
			showBars();
			//#endif
			
						
	}
	
	//#ifdef GFXBIG
	public void showBars()
	{
		int x;
		
		//#ifdef GFXHUGE
		//#else
		
			if(lastLifes!=game.pl.lifes || lastPower!=game.pl.power || lastHealth!=game.pl.life || game.cnt==2 || game.doUpdate)						
			{
				lastLifes=game.pl.lifes;
				lastPower=game.pl.power;
				lastHealth=game.pl.life;
								
				g.setClip(0,0,176,57);
				g.drawImage(Bars,0,-21,20);
																			
				showSprite(Bars,38,80,(130*game.pl.life)/game.pl.maxLife,9,23,4,false);
				
				showSprite(Bars,52,91,(124*game.pl.power)/138,9,37,15,false);
				
				int powerLev = game.pl.power/30; powerLev--; if (powerLev>=3) powerLev=3;
							
				if(powerLev>=0) showSprite(Bars,57+(25*powerLev),103,25,16,57+(25*powerLev),25,false);
				
				if(game.pl.state==BeastPlayer.PAIN) showSprite(Bars,2,109,33,33,2,4,false);
				else showSprite(Bars,2,79,31,27,2,4,false);
			}
			
			g.setClip(150,43,26,10);
			g.drawImage(Bars,0,-21,20);
			
			g.setClip(0,43,41,14);
			g.drawImage(Bars,0,-21,20);
			
			
			
			showSprite(Bars,64,121,(14*game.pl.lifes),27,0,28,false);			
			
			// Inferior
			
			x = (BeastWorld.minimum[game.world.level]-game.pl.gems);
			
			if(x<=0 && game.level!=7)
			if((game.cnt/4)%2==0)
			PutSprite(Extras,CANVX-32,BEGINY,24,65,ExtrasCoo,1,false);
			
						
			if(game.state==BeastMain.EXPLAIN || lastGems!=game.pl.gems || game.cnt==2 || game.doUpdate){
				
				lastGems=game.pl.gems;
				
				g.setClip(0,CANVY-20,176,20);
				g.drawImage(Bars,0,CANVY-20,20);
													
				showFontText(""+game.pl.gems,14,CANVY-10-(fnts.getHeight()/2),0);				
			}
		//#endif
		
	}	
	//#endif
	
	public void updateScrollPos()
	{
			int dx, dy, lookx, looky;
			
			//#ifndef GFXBIG
			//#else
			if(game.pl.dir==0) lookx=(game.pl.x*3)/2+(CANVX/4);
			else lookx=(game.pl.x*3)/2-(CANVX/4);
						
			looky=(game.pl.y*3)/2+(game.pl.vely>>5);
			
			dx=(lookx-(CANVX/2))-ScrollPosX;
			dy=(looky-(CANVY/2))-ScrollPosY;
			if(game.pl.state==BeastPlayer.CROUCH) dy+=50;
			
			ScrollPosX+=dx/4;
			ScrollPosY+=dy/4;

						
			if(ScrollPosX<0) ScrollPosX=0;
			if(ScrollPosY<0) ScrollPosY=0;
			if(ScrollPosX>=game.world.sizeX*24-CANVX) ScrollPosX=game.world.sizeX*24-CANVX-1;
			if(ScrollPosY>=game.world.sizeY*24-SCREENY) ScrollPosY=game.world.sizeY*24-SCREENY-1;					
			//#endif
	}
				
	public void clearDisplay(int r, int gr, int b)
	{
		g.setClip(0,0,CANVX,CANVY);
		g.clipRect(0,0,CANVX,CANVY);
		g.setColor(r,gr,b);
		g.fillRect(0,0,CANVX,CANVY);		
	}
			
	public static void vibrate(int freq, int Time)
	{
		//#ifdef DEVICEVIBRA
			//#ifdef NOKIAUI
			try{
				if(game.vibr)
				DeviceControl.startVibra(freq,Time);
						
			}catch (java.lang.Exception e) {}
			//#endif
		//#endif			
	}
		
	public void showSprite(Image im, int bx, int by, int sx, int sy, int px, int py, boolean inv)
	{
		int x1, x2, y1, y2;	
				
		x1=px; y1=py; x2=px+sx; y2=py+sy;
		
		if(x1>=CANVX || y1>=SCREENY || x2<0 || y2<0 || im==null) return;
					
		if(x1<0) x1=0; 
		if(x2>CANVX) x2=CANVX; 

		if(y1<0) y1=0; 
		if(y2>SCREENY) y2=SCREENY; 
		
		y1+=TRANSY; y2+=TRANSY; py+=TRANSY;
		
		g.setClip(x1,y1,x2-x1,y2-y1);
		g.clipRect(x1,y1,x2-x1,y2-y1);
		//g.clipRect(0,0,CANVX,SCREENY);
		
		if(inv){	 
			//#ifdef GFXFLIP
				//#ifdef NOKIAUI
				DirectGraphics dg = DirectUtils.getDirectGraphics(g);
				dg.drawImage(im,px+bx-(im.getWidth()-sx),py-by,20,DirectGraphics.FLIP_HORIZONTAL);
				//#elifdef MIDP20
				//#else
				//#endif
			//#else
			//#endif
		}else g.drawImage(im,px-bx,py-by,20);				
	}	
		
	public void PutSprite(Image Img,  int X, int Y,  int CuadSize, int Frame, byte[] Coor, boolean inv)
	{		
		PutSprite(Img, X, Y, CuadSize, Frame, Coor, 1, inv);

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
	
					
	public void showFontText(String s, int x, int y, int f)
	{
		if(y>CANVY || y<0) return;	
		g.setFont(fnts);		
		if(f==1) g.setColor(115,101,80);
		else g.setColor(112,125,52);
		g.setClip(0,0,CANVX,CANVY);
		g.drawString(s/*.toUpperCase()*/,x+1,y+1,20);								
		if(f==1) g.setColor(235,202,157);
		else g.setColor(224,249,105);
		g.drawString(s/*.toUpperCase()*/,x,y,20);								
		
	}
	
	public void showFontTextCenter(String s, int y, int f)
	{
		if(y>CANVY || y<0) return;
		g.setFont(fnts);
		if(f==1) g.setColor(115,101,80);
		else g.setColor(112,125,52);
		g.setClip(0,0,CANVX,CANVY);
		g.drawString(s/*.toUpperCase()*/,1 + super.getWidth()/2, y+1, Graphics.HCENTER | Graphics.TOP);
		if(f==1) g.setColor(235,202,157);
		else g.setColor(224,249,105);
		g.drawString(s/*.toUpperCase()*/,super.getWidth()/2, y, Graphics.HCENTER | Graphics.TOP);
	}
	
	//#ifdef GFXBIG
	public void showFontTextNoClip(String s, int x, int y, int f)
	{
		
		g.setFont(fnts);		
		if(f==1) g.setColor(235,202,157);
		else g.setColor(224,249,105);		
		g.drawString(s/*.toUpperCase()*/,x,y,20);								
	} 									
	//#endif
		
	/*public byte[] LoadFile(String name, int size)
	{

		System.gc();
		InputStream is = getClass().getResourceAsStream(name);

		byte[] Dest = new byte[size];

		try {
			for(int i = 0; i<Dest.length; i++)			
				Dest[i] = (byte) is.read();
			is.close();
		} catch(Exception exception) {}
		
		return Dest;
	}*/
	public byte[] LoadFile(String Nombre, int size)
	{
		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
		byte[] bytes = file.readBytes();

		return bytes;
	}
	
	/*=====================================================================================*/	
	
	void showPlayer(BeastPlayer p)
	{
		//#ifndef GFXBIG
		//#else
			int fr=-1, mateFr=-1, yOffset=32;
			boolean inv=(p.dir==1);
			
			if(p.invulnerable==0 || (p.cnt/2)%2==0)
			switch(p.state){
				
				case BeastPlayer.STAND : if(p.cnt<4) fr=7; fr=0; break;
				case BeastPlayer.WALK : 
						if(Math.abs(p.velx)<300) fr=((p.animSpeed>>9)%5)+1;
						else {fr=((p.animSpeed>>10)%4); if(fr==3) fr=1; fr+=8;}
					break;
				case BeastPlayer.JUMP : if(p.vely<-500) fr=18; else fr=19; break;
				case BeastPlayer.FALL : fr=20; break;
				case BeastPlayer.STAND_SHOT : fr=33+(p.cnt/2); break;
				case BeastPlayer.WALK_SHOT : fr=((p.animSpeed>>8)%3)+12; break;
				case BeastPlayer.FALL_SHOT : fr=21; break;			
				case BeastPlayer.RIDE_STAND : fr=36; mateFr=9; yOffset=52; break;
				case BeastPlayer.RIDE_WALK_SHOT :
				case BeastPlayer.RIDE_WALK :  fr=36; mateFr=2+((p.animSpeed>>9)%7); 
					switch(mateFr){
						case 0 : yOffset=48; break;
						case 1 : yOffset=48; break;
						case 2 : yOffset=51; break;
						case 3 : yOffset=51; break;
						case 4 : yOffset=54; break;
						case 5 : yOffset=54; break;
						case 6 : yOffset=51; break;
						case 7 : yOffset=51; break;
						case 8 : yOffset=48; break;					
					}
				break;			
				case BeastPlayer.RIDE_JUMP : fr=36; if(p.vely<-500) mateFr=2; else mateFr=4; yOffset=55; break;
				case BeastPlayer.RIDE_FALL_SHOT :
				case BeastPlayer.RIDE_FALL : fr=36; mateFr=6; yOffset=58; break;
				case BeastPlayer.RIDE_STAND_SHOT : fr=36; mateFr=2+(p.cnt); yOffset=52+(p.cnt/2); break;
				case BeastPlayer.BRAWL : fr=48+(p.cnt/2); break;
				case BeastPlayer.BRAWL2 : fr=27+(p.cnt/2); break;
				case BeastPlayer.CROUCH : fr=(p.cnt<2 ? 37 : 38); break;
				case BeastPlayer.PAIN : fr=51; break;			
				case BeastPlayer.DEAD : if(p.cnt>=21) fr=46; else fr=40+(p.cnt/3); break;			
				case BeastPlayer.MATE_DEAD : fr=20; mateFr=18+(p.cnt/4); yOffset=52-(p.cnt*3); break;
			}
			
			if(mateFr>=0) 
				PutSprite(Mate,(p.x*3)/2-16-ScrollPosX,(p.y*3)/2-32-ScrollPosY,32,mateFr,MateCoo,2,inv);
			if(fr>=0) 
				PutSprite(Player,(p.x*3)/2-16-ScrollPosX,(p.y*3)/2-yOffset-ScrollPosY,32,fr,PlayerCoo,3,inv);
		//#endif		
	}
	
	void showEnemy(BeastEnemy e)
	{
		int fr=0;
		boolean inv=(e.dir==1);
		
		//#ifndef GFXBIG		
		//#else
		switch(e.type){
			
				// SNAKE
				case BeastEnemy.SNAKE :				
				switch(e.state){
										
					case BeastEnemy.STAND_SHOT : fr=((e.cnt/4)%5); break;
					case BeastEnemy.DEAD : fr=(e.cnt/4)+14; break;
					default : fr=7+((e.cnt/3)%6); break;
				}							
				PutSprite(Snake,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-32-ScrollPosY,32,fr,SnakeCoo,2,inv);

				break;			
				
				// DRAGON
				case BeastEnemy.DRAGON :				
				switch(e.state){
															
					case BeastEnemy.DEAD : fr=(e.cnt/5)+3; break;
					default : fr=((e.cnt/4)%4); if(fr==3) fr=1; break;
				}							
				PutSprite(Dragon,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-27-ScrollPosY,32,fr,DragonCoo,inv);

				break;							
				
				// DINO
				case BeastEnemy.DINO :				
				switch(e.state){
															
					default : if(Math.abs(e.velx)<170) fr=((e.cnt/3)%4); 
								else fr=((e.cnt/2)%6); 
					break;
					case BeastEnemy.FALL : fr=4+((e.cnt/3)%2); break;
					case BeastEnemy.DEAD : fr=4+(e.cnt/4); break;
				}							
				PutSprite(Dino,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-32-ScrollPosY,32,fr,DinoCoo,inv);

				break;							
				
				// SCORPION
				case BeastEnemy.SCORPION :				
				switch(e.state){
															
					case BeastEnemy.STAND_SHOT : fr=5+((e.cnt/3)%3); break;															
					case BeastEnemy.DEAD : fr=8+(e.cnt/3); break;															
					default :  fr=((e.cnt/3)%5); 								
					break;					
				}											
				PutSprite(Scorpion,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-32-ScrollPosY,32,fr,ScorpionCoo,inv);


				break;								
				
				// PLANT
				case BeastEnemy.PLANT :				
				switch(e.state){
															
					case BeastEnemy.DEAD : fr=9+(e.cnt/4); break;																														
					case BeastEnemy.STAND_SHOT : fr=1+(e.cnt/2); break;															
					default :  fr=0; 								
					break;					
				}							
				PutSprite(Plant,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-32-ScrollPosY,32,fr,PlantCoo,inv);
				if(fr==4) PutSprite(Plant,(e.x*3)/2-16-ScrollPosX + (inv ? -32 : 32),(e.y*3)/2-32-ScrollPosY,32,8,PlantCoo,inv);


				break;
				
				// TOAD
				case BeastEnemy.TOAD :				
				switch(e.state){

					case BeastEnemy.DEAD : fr=12+(e.cnt/4); break;																																													
					case BeastEnemy.WALK : fr=((e.cnt/4)%6); break;															
					case BeastEnemy.STAND_SHOT : fr=6+((e.cnt/3)%4); if(fr==9) fr=7; break;															
					default :  fr=0; 								
					break;					
				}							
				PutSprite(Toad,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-32-ScrollPosY,32,fr,ToadCoo,inv);

				break;				
				
				// INSECT
				case BeastEnemy.INSECT :				
				switch(e.state){
					
					case BeastEnemy.DEAD : fr=4+(e.cnt/4); break;																																																												
					default :  fr=(e.cnt%3); 								
					break;					
				}							
				PutSprite(Insect,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-32-ScrollPosY,32,fr,InsectCoo,inv);

				break;					
				
				// MATE
				case BeastEnemy.MATE :				
				switch(e.state){

					case BeastEnemy.DEAD : fr=18+(e.cnt/4); break;																																													
					case BeastEnemy.WALK : fr=2+((e.cnt/2)%7); break;															
					case BeastEnemy.STAND_SHOT : fr=9+((e.cnt/6)%5); break;															
					default :  fr=6; 								
					break;					
				}							
				PutSprite(Mate,(e.x*3)/2-16-ScrollPosX,(e.y*3)/2-32-ScrollPosY,32,fr,MateCoo,2,inv);

				break;				
				
				
		}
		//#endif		
			
	}
	
	void showBoss(BeastEnemy b)
	{
		int fr=0;
		boolean inv=false;
		
		//#ifndef GFXBIG				
		//#else
		for(int i=0; i<6; i++){
			fr=2+(((b.snakeCnt/2)+i)%9); if(fr>=10) fr+=0;
			//fr=2;
			if(b.snakeX-16+(12*i)<b.x-16) PutSprite(Boss,(b.snakeX*3)/2-16+(12*i)-ScrollPosX,(b.snakeY*3)/2-(sin((b.snakeCnt*4)+(16*i))>>8)-16-ScrollPosY,42,fr,BossCoo,1,false);
		}		
		
		switch(b.state){
			
			case BeastEnemy.SNAKESHOT :
			case BeastEnemy.SHOT :
			fr=b.cnt/10; if(fr>=2) fr=1;
			fr=21+fr;
			if(b.life<300) fr+=3;
			break;
			
			case BeastEnemy.DEAD :
			fr = 26;
			break;
													
			default : fr=21-b.cnt/10; if(fr<20) fr=20; if(b.life<300) fr+=3; break;
			
		}			
		
		PutSprite(Boss,(b.x*3)/2-16+32-ScrollPosX,(b.y*3)/2-20-ScrollPosY,42,18,BossCoo,1,inv);
		PutSprite(Boss,(b.x*3)/2-16-ScrollPosX,(b.y*3)/2-23-ScrollPosY,42,fr,BossCoo,1,inv);
		
		for(int i=0; i<b.arms.length; i++){
			
			switch((b.angle*i)/21){
				
				case 0  : fr = 2; break;	
				case 1  : fr = 13; break;	
				case 2  : fr = 11; break;	
				case 3  : fr = 10; break;	
				case 4  : fr = 13; break;	
				case 5  : fr = 2; break;	
			}
			
			PutSprite(Boss,(b.arms[i][0]*3)/2-16-ScrollPosX,(b.arms[i][1]*3)/2-16-ScrollPosY,42,fr,BossCoo,1,inv);
		}		
		//#endif
				
	}
	
		
	// Control stuff =====================================================================
	
	public boolean up, down, left, right, but1, but2, but3, but4, any, anybut;	
	
	public void controlReset()
	{
		up=false; down=false; left=false; right=false; 
		but1=false; but2=false; but3=false; but4=false;		
		any=false; anybut=false;
	}	

	
	// -------------------
	// Image SET
	// ===================
	
	/*public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, Graphics Gfx, int DestX, int DestY)
	{
		Gfx.setClip(0,0, ScrollSizeX, ScrollSizeY);
		Gfx.clipRect(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
		
	//	Gfx.setClip(DestX-ScrollX, DestY-ScrollY, SizeX, SizeY);
		Gfx.drawImage(Sour, DestX-SourX-ScrollX, DestY-SourY-ScrollY, Graphics.TOP|Graphics.LEFT);
	}*/
	
	// Trigonometrical stuff
	/*private static short sintable[]={
	0,25,50,75,100,125,150,175,199,224,248,273,297,321,344,368,391,414,437,460,482,504,
	526,547,568,589,609,629,649,668,687,706,724,741,758,775,791,807,822,837,851,865,
	878,890,903,914,925,936,946,955,964,972,979,986,993,999,1004,1008,1012,1016,1019,1021,
	1022,1023};
	
	private static short costable[]={
	1024,1023,1022,1021,1019,1016,1012,1008,1004,999,993,986,979,972,964,955,946,936,
	925,914,903,890,878,865,851,837,822,807,791,775,758,741,724,706,687,668,649,629,
	609,589,568,547,526,504,482,460,437,414,391,368,344,321,297,273,248,224,199,175,
	150,125,100,75,50,25};
	
	private static short tantable[]={
	0,25,50,75,100,126,151,177,203,229,256,283,310,338,366,395,424,453,484,515,547,580,
	613,648,684,721,759,799,840,883,928,974,1024,1075,1129,1187,1247,1312,1380,1453,
	1532,1617,1708,1807,1915,2034,2165,2310,2472,2654,2861,3099,3375,3700,4088,4560,
	5148,5901,6903,8302,10397,13882,20845,30000
	};*/
	
	private static short[] sintable, costable, tantable;
	
	public void initTables()
	{		
		byte temp[]= new byte[384];
		
		temp = LoadFile("/Trig.bin",384);
				
		sintable=new short[64];
		costable=new short[64];
		tantable=new short[64];
		
		for(int i=0; i<64; i++){
		
			sintable[i]=(short)(((temp[i*2]&0xFF)<<8) | ((temp[i*2+1]&0xFF))); 		
			costable[i]=(short)(((temp[128+i*2]&0xFF)<<8) | ((temp[i*2+1+128]&0xFF))); 		
			tantable[i]=(short)(((temp[i*2+256]&0xFF)<<8) | ((temp[i*2+1+256]&0xFF))); 
		}	
								
		temp = null;
		System.gc();	
		
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
	
	
// *******************
// -------------------
// textos - Engine - v1.0 - Rev.2 (5.5.2004)
// ===================
// *******************

	final static int TEXT_PLAY = 0;
	final static int TEXT_CONTINUE = 1;
	final static int TEXT_SOUND = 2;
	final static int TEXT_VIBRA = 3;
	final static int TEXT_MODE = 4;
	final static int TEXT_GAMEOVER = 5;
	final static int TEXT_HELP = 6;
	final static int TEXT_ABOUT = 7;
	final static int TEXT_RESTART = 8;
	final static int TEXT_EXIT = 9;
	final static int TEXT_LOADING = 10;
	final static int TEXT_HELP_SCROLL = 11;
	final static int TEXT_ABOUT_SCROLL = 12;

// -------------------
// textos Create
// ===================

	String[][] textCreate(byte[] textos) {
		int dataPos = 0;
		int dataBak = 0;
		short[] data = new short[1024];

		boolean campo = false;
		boolean subCampo = false;
		boolean primerEnter = true;
		int size = 0;

		for (int i=0 ; i<textos.length ; i++)
		{
			if (campo)
			{
				if (textos[i] == 0x7D)
				{
					subCampo = false;
					campo = true;
				}

				if ((textos[i] < 0x20 && textos[i] >= 0) || textos[i] == 0x7C || textos[i] == 0x7D)	// Buscamos cuando Termina un campo
				{
					data[dataPos+1] = (short) (i - data[dataPos]);

					dataPos+=2;

					campo=false;
				}

			} else {

				if (textos[i] == 0x7D)
				{
					subCampo = false;
					continue;
				}

				if (textos[i] == 0x7B)
				{
					dataBak = dataPos;
					data[dataPos++] = 0;
					campo = false;
					subCampo = true;
					size++;
					continue;
				}

				if (subCampo && textos[i] == 0x0A)
				{
					if (primerEnter)
					{
						primerEnter = false;
					} else {
						data[dataPos++] = (short) i;
						data[dataPos++] = 1;
						if (!subCampo) {size++;} else {data[dataBak]--;}
					}
					continue;
				}

				if (textos[i] >= 0x20)	// Buscamos cuando Empieza un campo nuevo
				{
					campo=true;
					data[dataPos] = (short) i;
					if (!subCampo) {size++;} else {data[dataBak]--;}
					primerEnter = true;
				}
			}
		}

		// MONGOFIX ==========================================
		char textos_char[] = new char[textos.length];
		for(int i = 0; i < textos.length; i++)
			if(textos[i] < 0)
				textos_char[i]=(char)(textos[i]+256);
			else
				textos_char[i]=(char)textos[i];
		//=================================================

		String[][] strings = new String[size][];

		dataPos=0;

		for (int i=0 ; i<size ; i++)
		{
			int num = data[dataPos];

			if (num<0) {num*=-1;dataPos++;} else {num = 1;}

			strings[i] = new String[num];

			char a = 'á';
			for (int t=0 ; t<num; t++) {
				strings[i][t] = new String(textos_char, data[dataPos++], data[dataPos++]);
				if(strings[i][t].length() < 2) strings[i][t] = " ";
			}
		}

		return strings;
	}

	
	public static byte[] LoadFileCheckSize(String Nombre)
	{
		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
		byte[] bytes = file.readBytes();

		return bytes;

		/*System.gc();
	
		InputStream is = game.getClass().getResourceAsStream(Nombre);
	
		//byte[] buffer = null;
		byte[] buffer = new byte[1024];
	
		try
		{
			int Size = 0;
			while (true)
			{
			//int Desp = (int) is.skip(1024);
			int Desp = is.read(buffer, 0, buffer.length);

			if (Desp <= 0) {break;}
			Size += Desp;
			}
	
			is = null; System.gc();
	
			buffer = new byte[Size];
	
			is = game.getClass().getResourceAsStream(Nombre);
			Size = is.read(buffer, 0, buffer.length);
	
			while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}
	
			is.close();
		}
		catch(Exception exception) {}
	
		System.gc();
	
		return buffer;*/
	}
	
	///============
	
	static byte[] multiread(String f, int lengths[], int n)
	{
		/*System.gc();
		InputStream is = game.getClass().getResourceAsStream(f);
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
		for(int j = 0; j < buffer.length; j++)
			buffer[j] = (byte) is.read();
		is.close();
		}catch(Exception exception) {}
		System.gc();
		
		return buffer;	*/

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
	}	
	
	
	void showSquare(Square s, int r, int gg, int b)
	{
		g.setClip(0,0,CANVX,CANVY);
		g.setColor(r,gg,b);
		g.fillRect(s.x1 - ScrollPosX, s.y1 - ScrollPosY, s.x2-s.x1, s.y2 - s.y1);
	}
	
	public void hideNotify()
	{
		game.doUpdate = false;
	}
	
	public void showNotify()
	{
		game.doUpdate = true;
		if(game.state == BeastMain.MAIN_MENU) playSound(0,1);
	}	
	
	public void loadProgressInc(int inc)
	{
		loadProgress+=inc;	
		repaint(); serviceRepaints();
	}
		
//#ifdef CLISTENER

// -----------------------------------
// A�adir a la definicion de la Clase:
// -----------------------------------
// implements CommandListener
// ===================================

//#ifdef ONEKEYLISTENER
//String[] ListenerStr = new String[] { "Menu"};
//#else
String[] ListenerStr = new String[] { "Menu", " "};
//#endif

Command[] ListenerCmd;

// -------------------
// Listener EXE
// ===================

public void ListenerEXE(int CMD)
{	
	controlReset();
	switch (CMD)
	{
	case 0:		// Menu
		but2 = true;
	break;

	case 1:		// Salir
		but1 = true;
	break;
	}
	listenerKey = true;
}


// -------------------
// Listener SET
// ===================

public void ListenerSET(Canvas c)
{
	ListenerCmd = new Command[ListenerStr.length];

	//#ifdef FLIPLISTENERKEYS
	//for (int i = ListenerStr.length-1 ; i>=0 ; i--)
	//#else
	for (int i = 0 ; i<ListenerStr.length ; i++)
	//#endif
	{
	ListenerCmd[i] = new Command(ListenerStr[i], Command.SCREEN, 1);
	//TODO c.addCommand(ListenerCmd[i]);
	}
	//TODO c.setCommandListener(this);
}

// -------------------
// Listener RES
// ===================

public void ListenerRES(Canvas c)
{
	for (int i=0 ; i<ListenerCmd.length ; i++)
	{
	//TODO c.removeCommand(ListenerCmd[i]);
	ListenerCmd[i] = null;
	}
	ListenerCmd = null;
}

// -----------------------
// Listener command Action
// =======================

public void commandAction (Command c, Displayable d)
{
	if (ListenerCmd!=null)
	{
		for (int i=0 ; i<ListenerCmd.length ; i++)
		{
		if (c == ListenerCmd[i]) {ListenerEXE(i); return;}
		}
	}
}

//#endif
	
}
