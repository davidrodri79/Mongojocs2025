package com.mygdx.mongojocs.qblast;

/*
	Canvas for Sony Ericsson Z1010 
*/


import java.io.InputStream;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;



public class QBlastCanvas extends FullCanvas
{
						
	Font fnts;
	Random rnd;
	static QBlastMain game;	
	Image Tiles, Ball, BallLine, Fnt, BigFnt, Picture, Title, GreatBall, Bar, Faces;
	int bally=140, ballvely=-2;
		
	static int lastSound=-1;
	
	public Graphics g;
	//Runtime rt = Runtime.getRuntime();
		
	public static int CANVX, CANVY; 
	
	public static final int ROTATE_STEP = 1;
		
	public boolean painting;	
	
	boolean Cheat_ON=false;
	int CheatPos_1=0;
	
	
	static Music snd[];

		
	public QBlastCanvas(QBlastMain main)
	{
		game=main;	
		
		fnts=Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN , Font.SIZE_SMALL);	
		
		//setFullScreenMode(true);	

		CANVX=getWidth(); CANVY=getHeight();
	
																										
		controlReset();
							 			
		try{					
			Ball = new Image();
			Ball._createImage("/QBall.png");
			BallLine = new Image();
			BallLine._createImage("/QBallLine.png");
			Fnt = new Image();
			Fnt._createImage("/Font.png");
			BigFnt = new Image();
			BigFnt._createImage("/BigFont.png");
			Picture = new Image();
			Picture._createImage("/Logo.png");
			Faces = new Image();
			Faces._createImage("/Faces.png");
			Title = new Image();
			Title._createImage("/Title.png");
			GreatBall = new Image();
			GreatBall._createImage("/GreatBall.png");
			Bar = new Image();
			Bar._createImage("/Bar.png");
									
		}catch(Exception err) {}
		
		snd = new Music[7];
		
		snd[0] = LoadMidi("intro.mid");
		snd[1] = LoadMidi("level.mid"); 
		snd[2] = LoadMidi("gover.mid"); 
		snd[3] = LoadMidi("bomb.mid"); 
		snd[4] = LoadMidi("item.mid"); 
		snd[5] = LoadMidi("collide.mid"); 
		snd[6] = LoadMidi("enemy.mid");
								
														
	}
	
			
	static byte[] loadFile(String name, int size)
	{
		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+name.substring(1));
		byte[] bytes = file.readBytes();

		return bytes;
	}
	
	public void destroyPicture()
	{
		//Picture = null; System.gc();	
	}
	
	public void destroyLogo()
	{
		Picture = null; System.gc();	
		
		try{
		
			Picture = Image.createImage("/Picture.png"); 							
			
		}catch(Exception err) {}
	}
	
	
	public void loadTiles(int type)
	{
		Tiles = null; System.gc();
		try{					
						
			Tiles = Image.createImage("/QBes"+type+".png"); 						
						
		}catch(Exception err) {}
	}
	
	public void destroyTiles()
	{
		Tiles = null; System.gc();	
	}
	
	public void loadEndPic()
	{
		/*try{		
		
			Picture = Image.createImage("/Faces.png"); 									
			
		}catch(java.io.IOException err) {}*/				
	}
				
	public void keyPressed(int keycode)
	{
	
		controlReset();
		
		/*if(!game.vibr){
			up=(getGameAction(keycode)==UP);
			down=(keycode==KEY_NUM5 || keycode==KEY_NUM6 || getGameAction(keycode)==DOWN);
			left=(keycode==KEY_NUM2 || getGameAction(keycode)==LEFT);
			right=(keycode==KEY_NUM9 || getGameAction(keycode)==RIGHT);
			b
			ut1=(keycode==KEY_POUND || keycode==-7 || getGameAction(keycode)==FIRE);
			but2=(keycode==KEY_NUM1 || keycode==KEY_NUM4);
			but3=(keycode==KEY_NUM0 || keycode==KEY_NUM7);
			but4=(keycode==KEY_STAR || keycode==-6);
		}else{*/
		up=(keycode== Canvas.KEY_NUM2 /*|| getGameAction(keycode)==UP*/);
		down=(keycode==Canvas.KEY_NUM8 /*|| getGameAction(keycode)==DOWN*/);
		left=(keycode==Canvas.KEY_NUM4 /*|| getGameAction(keycode)==LEFT*/);
		right=(keycode==Canvas.KEY_NUM6 /*|| getGameAction(keycode)==RIGHT*/);
		but1=(keycode==Canvas.KEY_NUM5 || keycode==-7 /*|| getGameAction(keycode)==FIRE*/);
		but2=(keycode==Canvas.KEY_NUM1 /*|| keycode==KEY_NUM7*/);
		but3=(keycode==Canvas.KEY_NUM3 /*|| keycode==KEY_NUM9*/);
		but4=(keycode==Canvas.KEY_NUM0 || keycode==-6);
		//}
							
		anybut=but1 || but2 || but3 || but4;
		any=up || down || left || right || anybut;							
	}
	
	public void keyReleased(int keycode)
	{
		controlReset();
	}
	
	public Music LoadMidi(String Name)
	{
	
		/*Player p = null;
	
		try
		{
			InputStream is = getClass().getResourceAsStream(Name);
			p = Manager.createPlayer( is , "audio/midi");
			p.realize();
			p.prefetch();
		}
		catch(Exception exception) {exception.printStackTrace();}*/
	
		return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Name));
	} 
	
	public static void playMusic(int id, int loop)
	{	
		
		if(!game.sound) return;				
		try{
		
			for(int i=0; i<7; i++)
				snd[i].stop();
			
			//VolumeControl vc = (VolumeControl) snd[id].getControl("VolumeControl"); if (vc != null) {vc.setLevel(90);}
			snd[id].setLooping(loop==0);
			snd[id].play();
			
		}catch(Exception exception) {exception.printStackTrace();}
		
	}	
	
	public static void playSound(int id)
	{		
		playMusic(3+id,1);				
	} 	 

								
	public void paint(Graphics g2)
	{		

		painting=true;
		String ops[]=new String[6];
		
		g=g2;
		int x, y, min, sec, radius;
		
		try{
																
		if(game.cnt>1)						
		switch(game.state) {
			
			case QBlastMain.LOGO :
			clear_display(255,255,255);
			showSprite(Picture,0,0,Picture.getWidth(),Picture.getHeight(),(CANVX-Picture.getWidth())/2,(CANVY-Picture.getHeight())/2,false);
			break;			
			
			case QBlastMain.TITLE :
			ballvely-=1;
			bally+=ballvely;
			if(bally<=0) {bally=1; ballvely=-(3*ballvely)/4;}
			
			showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			if(game.cnt>100) showSprite(Title,0,0,CANVX,51,0,105,false);			
			g.setColor(108-(bally/2),0,0); //62,18
			g.setClip(0,0,CANVX,CANVY);
			g.fillArc(56+(bally/4),93+(bally/16),62-(bally/2),18-(bally/8),0,360);
			showSprite(GreatBall,0,0,63,63,56,40-bally,false);
			if((game.cnt/4)%2==0) showFontTextCenter(QBlastText.pushAnyButton,170,1);						
			break;
				
			case QBlastMain.MAIN_MENU :
			//clear_display(54,0,0);
			showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			y=40;
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(152,0,0);
			g.fillRoundRect((CANVX-140)/2,y+2+(20*game.menuOp), 140,20,8,8);
			g.setColor(255,0,0);
			g.drawRoundRect((CANVX-140)/2,y+2+(20*game.menuOp), 140,20,8,8);
			
			ops[0]=QBlastText.play;
			ops[1]=( game.sound ? QBlastText.soundOn : QBlastText.soundOff);
			ops[2]=( game.vibr ? QBlastText.vibrOn : QBlastText.vibrOff);
			ops[3]=QBlastText.help;
			ops[4]=QBlastText.about;
			ops[5]=QBlastText.exit;
			
			for(int i=0; i<6; i++)
				showBigFontTextCenter(ops[i],y+5+(20*i),( i==game.menuOp ? 1 : 0));			
				
			break;
			
			case QBlastMain.INGAME_MENU :
			showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			y=40;
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(152,0,0);
			g.fillRoundRect((CANVX-140)/2,y+2+(20*game.menuOp), 140,20,8,8);
			g.setColor(255,0,0);
			g.drawRoundRect((CANVX-140)/2,y+2+(20*game.menuOp), 140,20,8,8);

			/*g.fillRoundRect((CANVX-80)/2,y+3+(10*game.menuOp), 80,10,8,8);
			g.setColor(255,0,0);
			g.drawRoundRect((CANVX-80)/2,y+3+(10*game.menuOp), 80,10,8,8);*/			
			ops[0]=QBlastText.resume;
			ops[1]=( game.sound ? QBlastText.soundOn : QBlastText.soundOff);
			ops[2]=( game.vibr ? QBlastText.vibrOn : QBlastText.vibrOff);
			ops[3]=QBlastText.help;			
			ops[4]=QBlastText.abort;			
			for(int i=0; i<5; i++)
				showBigFontTextCenter(ops[i],y+5+(20*i),( i==game.menuOp ? 1 : 0));			
			break;			
			
			case QBlastMain.HELP :
			case QBlastMain.HELP2 :
			/*x=(CANVX-96)/2;
			y=(CANVY-65)/2;
			g.setColor(54,0,0);
			g.fillRoundRect((CANVX-96)/2,y,95,64,8,8);
			g.setColor(152,0,0);
			g.drawRoundRect((CANVX-96)/2,y,95,64,8,8);*/
			showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			showBigFontTextCenter(QBlastText.help,25,0);
			showFontText(QBlastText.butConf[0][0]+QBlastText.help1,35,60,1);
			showFontText(QBlastText.butConf[0][1]+QBlastText.help2,35,80,1);
			showFontText(QBlastText.butConf[0][2]+QBlastText.help3,35,100,1);
			showFontText(QBlastText.butConf[0][3]+QBlastText.help4,35,120,1);
			break;
			
			case QBlastMain.ABOUT :			
			y=CANVY+10-(game.cnt*2);			
			showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			showBigFontTextCenter(QBlastText.qblast,y,1);
			showBigFontTextCenter(QBlastText.program,y+40,1);
			showBigFontTextCenter(QBlastText.david,y+70,0);
			showBigFontTextCenter(QBlastText.graphic,y+110,1);
			showBigFontTextCenter(QBlastText.elias,y+140,0);			
			showBigFontTextCenter(QBlastText.music,y+180,1);
			showBigFontTextCenter(QBlastText.jordi,y+210,0);
			showBigFontTextCenter(QBlastText.microjocs,y+250,1);
			showBigFontTextCenter(QBlastText.web,y+290,1);
			break;
			
			case QBlastMain.CHOOSE_LEVEL :
			showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			y=(CANVX-65)/2;
			showBigFontTextCenter(QBlastText.chooseStartLevel,y+10,0);
			min=game.levelTimes[game.leveln]/60;			
			sec=game.levelTimes[game.leveln]%60;			
			/*g.setClip(0,0,CANVX,CANVY);
			g.setColor(152,0,0);			
			g.fillRoundRect((CANVX-96)/2,y+28, 96,10,8,8);
			g.setColor(255,0,0);
			g.drawRoundRect((CANVX-96)/2,y+28, 96,10,8,8);*/						
			if(sec<10)
			showBigFontTextCenter(( game.leveln > 0 ? "< " : "")+QBlastText.level+" "+(game.leveln+1)+"  "+min+":0"+sec+( game.leveln < game.lastLevel ? " >" : ""),y+60,1);			
			else showBigFontTextCenter(( game.leveln > 0 ? "< " : "")+QBlastText.level+" "+(game.leveln+1)+"  "+min+":"+sec+( game.leveln < game.lastLevel ? " >" : ""),y+60,1);			
			
			break;
			
			case QBlastMain.NEXT_LEVEL :
			showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			showBigFontTextCenter(QBlastText.level+" "+(game.leveln+1),(CANVY/2)-30,1);
			showBigFontTextCenter(""+(game.clock/13)+" "+QBlastText.seconds,(CANVY/2)+10,0);
			break;
			
			case QBlastMain.SHOW_TIP :
			//if(game.cnt==2)
			{
				
				x=(CANVX-96)/2;
				y=-10+(CANVY-(10*QBlastText.tips[game.leveln].length))/2;
				x=23; y=CANVY-48;
				clear_display(0,0,0);
				showLevel();
				
				/*g.setClip(0,0,CANVX,CANVY);
				g.setColor(152,0,0);
				g.fillRoundRect(x,y,96,8+8*QBlastText.tips[game.leveln].length,8,8);
				g.setColor(255,0,0);
				g.drawRoundRect(x,y,96,8+8*QBlastText.tips[game.leveln].length,8,8);*/
				for(int i=0; i<QBlastText.tips[game.leveln].length; i++)
					showFontText(QBlastText.tips[game.leveln][i],x+4,y+4+(6*i),1);
			}
			break;
			
			case QBlastMain.GAME_ACTION :
			clear_display(0,0,0);
			showLevel();
			//showFontText(""+game.ori,0,0,1);
			break;
			
			case QBlastMain.LEVEL_COMPLETE :
				clear_display(0,0,0);
				showLevel();
			showFontText(QBlastText.levelComplete,26,CANVY-38,1);
			showFontText((game.clock/13)+" "+QBlastText.extraSeconds,26,CANVY-25,1);
			break;
			
			case QBlastMain.GAME_OVER :
				clear_display(0,0,0);
				showLevel();
			showFontText(QBlastText.outOfTime,26,CANVY-38,1);
			showFontText(QBlastText.gameOver,26,CANVY-25,1);
			break;
			
			case QBlastMain.END_GAME :
			clear_display(0,0,0);
			showSprite(GreatBall,0,0,63,63,56,CANVY-75,false);
			y=CANVY+10-(game.cnt*2);			
			//showSprite(Picture,0,0,CANVX,CANVY,0,0,false);
			/*showBigFontTextCenter(QBlastText.qblast,y,1);
			showBigFontTextCenter(QBlastText.program,y+40,1);
			showBigFontTextCenter(QBlastText.david,y+70,0);
			showBigFontTextCenter(QBlastText.graphic,y+110,1);
			showBigFontTextCenter(QBlastText.elias,y+140,0);			
			showBigFontTextCenter(QBlastText.music,y+180,1);
			showBigFontTextCenter(QBlastText.jordi,y+210,0);
			showBigFontTextCenter(QBlastText.microjocs,y+250,1);
			showBigFontTextCenter(QBlastText.web,y+290,1);*/
						
			g.setClip(0,0,CANVX,CANVY);
			g.setColor(0,0,0);
			//g.fillRect(0,0,CANVX,115);
			showSprite(Faces,0,0,96,65,(CANVX-96)/2,20,false);
			/*showSprite(Ball,0,0,16,16,4,4,false);
			showSprite(Ball,80,32,16,16,CANVX-20,4,false);
			showSprite(Ball,128,32,16,16,4,CANVY-20,false);
			showSprite(Ball,48,32,16,16,CANVX-20,CANVY-20,false);*/
			showFontTextCenter(QBlastText.congrats1+" "+QBlastText.congrats2,95,0);
			//showSprite(Ball,48,32,16,16,CANVX-20,CANVY-20-(Trig.sin((game.cnt*10)%128)>>6),false);
			//showBigFontTextCenter(QBlastText.congrats2,CANVY-30,0);
			break;
			
		}	
				
		painting=false;
		
		
		}catch (Exception e)
		{
			e.printStackTrace();
			e.toString(); 				
		}

	}	
				
	public void clear_display(int r, int gr, int b)
	{
		g.setClip(0,0,CANVX,CANVY);
		g.setColor(r,gr,b);
		g.fillRect(0,0,CANVX,CANVY);		
	}
			
	public static void vibrate(int freq, int ti)
	{
		try{
			if(QBlastMain.vibr)
			DeviceControl.startVibra(freq,(ti < 300 ? 300 : ti));
			
		}catch (java.lang.Exception e) {}
	}
		
	public void showSprite(Image im, int bx, int by, int sx, int sy, int px, int py, boolean inv)
	{
		int x1, x2, y1, y2;	
		
		x1=px; y1=py; x2=px+sx; y2=py+sy;
		
		if(x1<0) x1=0; 
		if(x2>CANVX) x2=CANVX; 

		if(y1<0) y1=0; 
		if(y2>CANVY) y2=CANVY; 
		
		g.setClip(x1,y1,x2-x1,y2-y1);
		if(inv) ;
		else 	g.drawImage(im,px-bx,py-by,20);				
	}									
	
	
						
	int fontSizeX[] ={4,2,4,3,4,4,4,3,4,4,1,1,4,4,4,3,
					  4,4,4,4,4,4,4,4,4,1,3,4,3,5,4,4,
					  4,4,4,4,4,4,5,5,4,5,4,1,0,2,1,1};
	
	public void showFontText(String s, int x, int y, int t)
	{
		char c[];		
		int cc, xx=x, dy=0;
		
		if(y<-8 || y>CANVY) return;
		
		if(t==1) dy+=24;
				
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
				cc=(int)(c[i]-'0');							
				g.setClip(xx,y,8,8);
				if(c[i]!=' ')
					g.drawImage(Fnt,xx-(8*(cc%16)),y-dy-(8*(cc/16)),20);			
				if(cc<0) xx+=5;
				else xx+=fontSizeX[cc]+1;													
			}			
	}
	
	public void showBigFontTextCenter(String s, int y, int t)
	{
		char c[];		
		int sx=0, cc;
		
		if(y<-8 || y>CANVY) return;
		
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-'0');
			if(cc<0) sx+=10;
			else sx+=2*fontSizeX[cc]+2;
		}		
		showBigFontText(s,(CANVX-sx)/2,y,t);		
	}	
	
	public void showBigFontText(String s, int x, int y, int t)
	{
		char c[];		
		int cc, xx=x, dy=0;
		
		if(y<-8 || y>CANVY) return;
		
		if(t==1) dy+=48;
				
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
				cc=(int)(c[i]-'0');							
				g.setClip(xx,y,16,16);
				if(c[i]!=' ')
					g.drawImage(BigFnt,xx-(16*(cc%16)),y-dy-(16*(cc/16)),20);			
				if(cc<0) xx+=10;
				else xx+=2*fontSizeX[cc]+2;													
			}			
	}
	
	public void showFontTextCenter(String s, int y, int t)
	{
		char c[];		
		int sx=0, cc;
		
		if(y<-8 || y>CANVY) return;
		
		c=s.toUpperCase().toCharArray();			
		
		for(int i=0; i<c.length; i++){
			cc=(int)(c[i]-'0');
			if(cc<0) sx+=5;
			else sx+=fontSizeX[cc]+1;
		}		
		showFontText(s,(CANVX-sx)/2,y,t);		
	}	
	
	
	public void CheatRUN(int keycode)
	{
		byte[] Cheat_1={4,4,2,3,3,3,7,7,7,7};
		if (Cheat_1[CheatPos_1++] != keycode) {CheatPos_1=0;}
		if (Cheat_1.length==CheatPos_1) {Cheat_ON=true; CheatPos_1=0;}
		//if(Cheat_ON==true) game.world.pl.dam_time=9999;
	}		
	
	/*=====================================================================================*/	
	
	public void showLevel()
	{
		int x1=0, x2=game.lev.sizeX-1, z1=0, z2=game.lev.sizeZ-1, y1=0, y2=game.lev.sizeY-1;
		int radius=10, aux;
		
		if(game.pl.x<x1) x1=game.pl.x;
		if(game.pl.x>x2) x2=game.pl.x;
		if(game.pl.y<y1) y1=game.pl.y;
		if(game.pl.y>y2) y2=game.pl.y;
		if(game.pl.z<z1) z1=game.pl.z;
		if(game.pl.z>z2) z2=game.pl.z;
		
		if(x1<game.pl.x-radius) x1=game.pl.x-radius;				
		if(x2>game.pl.x+radius) x2=game.pl.x+radius;				
		if(y1<game.pl.y-radius) y1=game.pl.y-10;				
		if(y2>game.pl.y+radius) y2=game.pl.y+10;								
		if(z1<game.pl.z-radius) z1=game.pl.z-radius;				
		if(z2>game.pl.z+radius) z2=game.pl.z+radius;						
		
			
		for(int y=y1; y<=y2; y++)					
		switch(((game.ori+3)%32)/8){
			case 0:
			for(int z=z1; z<=z2; z++)		
			for(int x=x1; x<=x2; x++)		
				drawIsometric(game.lev.checkCube(x,y,z),x,y,z,game.ori);
			break;
			
			case 1:			
			for(int x=x1; x<=x2; x++)			
			for(int z=z2; z>=z1; z--)
				drawIsometric(game.lev.checkCube(x,y,z),x,y,z,game.ori);							
			break;	
			
			case 2:			
			for(int z=z2; z>=z1; z--)			
			for(int x=x2; x>=x1; x--)		
				drawIsometric(game.lev.checkCube(x,y,z),x,y,z,game.ori);							
			break;	
			
			case 3:			
			for(int x=x2; x>=x1; x--)			
			for(int z=z1; z<=z2; z++)		
				drawIsometric(game.lev.checkCube(x,y,z),x,y,z,game.ori);							
			break;	
					
		}
			
		showSprite(Bar,0,0,176,55,0,CANVY-55,false);
		
		g.setClip(0,0,CANVX,CANVY);
		g.setColor(187,204,204);
		x1=12; y1=CANVY-45;
		g.drawLine(x1,y1,x1+(Trig.cos((4*game.clock)%256)>>8),y1-(Trig.sin((4*game.clock)%256)>>8));
						
		aux=((game.clock%(13*60))/13);
				
		if(aux<10) showFontText((game.clock/(13*60))+":0"+((game.clock%(13*60))/13),3,CANVY-36,1);
		else showFontText((game.clock/(13*60))+":"+((game.clock%(13*60))/13),3,CANVY-36,1);
		
		showFontText(""+(game.leveln+1),50,CANVY-53,0);
		
		if(game.pl.invulnerable>0 && (game.pl.invulnerable>30 || (game.pl.invulnerable/2)%2==0))
			showSprite(Ball,144,16,16,16,121,CANVY-53,false);
			
		if(game.pl.maxBombs>1)
			showSprite(Ball,112,16,16,16,121,CANVY-19,false);
			
		if(game.pl.explPower>1){
			showSprite(Ball,96,16,16,16,121,CANVY-19,false);
			showFontText(""+(game.pl.explPower-1),114,CANVY-11,1);
		}
			
			
		//showFontText(""+game.ori,0,0);
		//showFontText("("+game.pl.x+","+game.pl.y+","+game.pl.z+")",0,15);
		
	}	
	
	public void drawIsometric(int piece, int px, int py, int pz, int or)
	{
			//int dx=0, dy=0, fr=0, x, y, z;
			int fr=0, dx, dy, x=0;
			boolean existAnything;
			
			switch(or){
				
				case 0 : dx=16; dy=0; break;
				case 1 : dx=20; dy=1; break;
				case 2 : dx=24; dy=2; break;
				case 3 : dx=25; dy=2; break;
				case 4 : dx=27; dy=3; break;
				case 5 : dx=28; dy=4; break;
				case 6 : dx=30; dy=5; break;
				case 7 : dx=30; dy=6; break;
					
				case 8 : dx=31; dy=8; break;
				case 9 : dx=31; dy=10; break;
				case 10 : dx=30; dy=12; break;
				case 11 : dx=29; dy=13; break;
				case 12 : dx=27; dy=15; break;
				case 13 : dx=25; dy=15; break;
				case 14 : dx=24; dy=15; break;
				case 15 : dx=21; dy=16; break;
					
				case 16 : dx=17; dy=17; break;
				case 17 : dx=13; dy=16; break;
				case 18 : dx=8; dy=15; break;
				case 19 : dx=7; dy=15; break;
				case 20 : dx=6; dy=14; break;
				case 21 : dx=5; dy=13; break;
				case 22 : dx=3; dy=12; break;
				case 23 : dx=2; dy=10; break;
					
				case 24 : dx=1; dy=8; break;
				case 25 : dx=2; dy=7; break;
				case 26 : dx=3; dy=6; break;
				case 27 : dx=4; dy=5; break;
				case 28 : dx=6; dy=4; break;
				case 29 : dx=7; dy=4; break;
				case 30 : dx=8; dy=3; break;
				case 31 : dx=12; dy=2; break;
				default : dx=0; dy=0;
			}			
							
			if(piece>=0 && piece<=18){
			
				switch(piece){
					
					case 1 :							
					fr=8+((or)%16);	
					break;
					
					case 2 :
					fr=24+(or);
					break;
					
					case 3 :
					fr=24+((8+(or))%32);
					break;
					
					case 4 :
					fr=24+((16+(or))%32);
					break;
					
					case 5 :
					fr=24+((24+(or))%32);
					break;
													
					case 6 :
					fr=(or)%8;
					break;			
					
					default :
					fr=-1;
					break;
				}
				
				/*switch(or){
				
					case 0 : dx=16; dy=0; break;
					case 2 : dx=24; dy=2; break;
					case 4 : dx=27; dy=3; break;
					case 6 : dx=30; dy=5; break;
					
					case 8 : dx=31; dy=8; break;
					case 10 : dx=30; dy=12; break;
					case 12 : dx=27; dy=15; break;
					case 14 : dx=24; dy=15; break;
					
					case 16 : dx=17; dy=17; break;
					case 18 : dx=8; dy=15; break;
					case 20 : dx=6; dy=14; break;
					case 22 : dx=3; dy=12; break;
					
					case 24 : dx=1; dy=8; break;
					case 26 : dx=3; dy=6; break;
					case 28 : dx=6; dy=4; break;
					case 30 : dx=8; dy=3; break;
					default : dx=0; dy=0;
				}*/
												
				if(piece!=0 && fr>=0) showSprite(Tiles,32*(fr%8),32*(fr/8),32,32,isometricX(px<<10,py<<10,pz<<10,or)-dx,isometricY(px<<10,py<<10,pz<<10,or)-dy,false);				
												
				// Exit
				if(piece==7)
					//showSprite(Ball,152,32,24,32,isometricX((px<<10)+512,(py<<10),(pz<<10)+512,or)-12,isometricY((px<<10)+512,(py<<10),(pz<<10)+512,or)-11,false);				
					showSprite(Ball,152,32,24,32,isometricX((px<<10),(py<<10),(pz<<10),or)-dx+4,isometricY((px<<10),(py<<10),(pz<<10),or)-dy,false);				
					
				// Trap
				if(piece==18){
					switch(game.lev.cnt%20){						
						case 0 : fr=2; break;						
						case 1 : fr=4; break;
						case 2 : fr=3; break;
						case 3 : fr=2; break;
						case 4 : fr=1; break;
						default: fr=0; break;
					}
					showSprite(Ball,19*fr,48,19,18,isometricX(px<<10,py<<10,pz<<10,or)+6-dx,isometricY(px<<10,py<<10,pz<<10,or)+13-dy,false);				
				}
					
				// Items
				if(piece>=11 && piece<=15){
					switch(piece){
						case 15 : x=96; break;	
						case 14 : x=112; break;	
						case 13 : x=128; break;	
						case 12 : x=144; break;	
						case 11 : x=160; break;	
					}				
					showSprite(Ball,x,16,16,16,isometricX(px<<10,py<<10,pz<<10,or)-dx+8,isometricY(px<<10,py<<10,pz<<10,or)-dy+10,false);									
				}
				
			}
			
			existAnything=game.lev.checkExistsAny(px,py,pz);
			
			if(existAnything){
				
				// Lifts
				if(game.lev.checkExists(px,py,pz,4)){
						
					QBlastLift li;					
					for(int i=0; i<game.lev.numLifts; i++){
						li=game.lev.li[i];
						//System.out.println("LIFT AT "+li.x+","+li.y+","+li.z+" checking "+px+","+py+","+pz);						
						if(li.x==px && li.y==py && li.z==pz){
							fr=8+((or)%16);						
							showSprite(Tiles,32*(fr%8),32*(fr/8),32,32,isometricX(li.realx-512,li.realy-512,li.realz-512,or)-dx,isometricY(li.realx-512,li.realy-512,li.realz-512,or)-dy,false);
						}
					}
				}
																			
				// Enemies
				if(game.lev.checkExists(px,py,pz,3))
				for(int i=0; i<game.lev.numEnemies; i++)
					if(game.lev.en[i].x==px && game.lev.en[i].y==py && game.lev.en[i].z==pz)
						showEnemy(game.lev.en[i],or);	
										
			}
			
			// Player
			if(game.lev.checkExists(px,py,pz,0))
			if(px==game.pl.x && py==game.pl.y && pz==game.pl.z)
				showBall(game.pl,or);				
			
			if(existAnything){						
				
				// Bombs
				if(game.lev.checkExists(px,py,pz,1))
				for(int i=0; i<game.bo.length; i++)
					if(game.bo[i].x==px && game.bo[i].y==py && game.bo[i].z==pz && game.bo[i].active)
						showBomb(game.bo[i],or);	
								
				// Explosions
				if(game.lev.checkExists(px,py,pz,2))		
				for(int i=0; i<game.ex.length; i++)
					if(game.ex[i].active)
					switch(game.ex[i].burnsAt(px,py,pz)){
						case 1 : 
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)-8,isometricY((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)+16,false);					
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10),(py<<10)+512,(pz<<10)+512,or)-8,isometricY((px<<10),(py<<10)+512,(pz<<10)+512,or)+16,false);					
						break;
						
						case 3 : 
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)-8,isometricY((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)+16,false);					
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10)+1024,(py<<10)+512,(pz<<10)+512,or)-8,isometricY((px<<10)+1024,(py<<10)+512,(pz<<10)+512,or)+16,false);					
						break;					
						
						case 4 : 
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)-8,isometricY((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)+16,false);					
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10)+512,(py<<10)+512,(pz<<10)+1024,or)-8,isometricY((px<<10)+512,(py<<10)+512,(pz<<10)+1024,or)+16,false);					
						break;										
						
						case 2 : 
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)-8,isometricY((px<<10)+512,(py<<10)+512,(pz<<10)+512,or)+16,false);					
						showSprite(Ball,80+16*(game.ex[i].counter/3),0,16,16,isometricX((px<<10)+512,(py<<10)+512,(pz<<10),or)-8,isometricY((px<<10)+512,(py<<10)+512,(pz<<10),or)+16,false);					
						break;															
					}
					
				// Ball pieces!!!
				if(game.pl.state==QBlastPlayer.BROKEN)
				if(game.lev.checkExists(px,py,pz,5))		
				for(int i=0; i<game.lev.pieces.length; i++)
					if(game.lev.pieces[i].x==px && game.lev.pieces[i].y==py && game.lev.pieces[i].z==pz)
						showSprite(Ball,176+(16*i),16*((game.lev.piecesPos[i]/25)%4),16,16,isometricX(game.lev.pieces[i].realx,game.lev.pieces[i].realy,game.lev.pieces[i].realz,or)-8,isometricY(game.lev.pieces[i].realx,game.lev.pieces[i].realy,game.lev.pieces[i].realz,or)+8,false);					
			}
								
						
	}
	
	public void showBall(QBlastPlayer pl, int or)
	{		
		int u, v, angle, x, y, fr=0;
		
		x=isometricX(pl.realx,pl.realy,pl.realz,or)-8;
		y=isometricY(pl.realx,pl.realy,pl.realz,or)+8;
		
		if(pl.state==QBlastPlayer.PLAYING){
			angle=pl.rotz-(or*8);
			if(angle<0) angle+=256;
			u=(angle/16)%4;
			v=(pl.rotx/16)%8;		
			if(pl.cnt<15){
				fr=4-(pl.cnt/3);
			}else				
			if(pl.invulnerable>0)
				switch((pl.cnt/3)%4){
					case 0 : fr=0; break;	
					case 1 : fr=1; break; 	
					case 2 : fr=2; break;
					case 3 : fr=1; break;
				}
			else fr=0;
			showSprite(Ball,16*fr,0,15,15,x,y,false);
			
			//System.out.println("INVUL"+pl.invulnerable+", FRAME:"+fr);
			
			if(fr<3) showSprite(BallLine,(15*u),(15*v),15,15,x,y,false);				
		}
		if(pl.state==QBlastPlayer.BROKEN){
			/*if(pl.cnt<7)
			showSprite(Ball,48,0,15,15,x,y,false);
			else
			showSprite(Ball,64,0,15,15,x,y,false);*/
		}
		if(pl.radioactive>0)
			showSprite(Ball,96+16*(pl.cnt%3),48,15,15,x,y,false);			
		
	}
	
	public void showEnemy(QBlastEnemy e, int or)
	{	
		int fr=0;
		switch(e.enemyClass){
				
			case 0 : showSprite(Ball,80,32,16,16,isometricX(e.realx,e.realy,e.realz,or)-8,isometricY(e.realx,e.realy,e.realz,or)+8,false); break;
			case 1 : showSprite(Ball,96+(16*(e.cnt%3)),32,16,16,isometricX(e.realx,e.realy,e.realz,or)-8,isometricY(e.realx,e.realy,e.realz,or)+8,false); break;
			case 2 : if((e.cnt/2)%8<5) fr=(e.cnt/2)%8; 
					 else fr=3-(((e.cnt/2)%8)-5);
					showSprite(Ball,fr*16,32,16,16,isometricX(e.realx,e.realy,e.realz,or)-8,isometricY(e.realx,e.realy,e.realz,or)+8,false); 
			break;
		}
				
	}
	
	public void showBomb(QBlastBomb b, int or)
	{
		showSprite(Ball,16*(b.counter/12),16,16,16,
				isometricX(b.realx,b.realy,b.realz,or)-8,
				isometricY(b.realx,b.realy,b.realz,or)+10,
				false);							
	}
	
	public int isometricX(int px, int py, int pz, int or)
	{
		int x,y,z,dx=0;
		
		x=px-game.pl.realx; y=py-game.pl.realy; z=pz-game.pl.realz;		
		
		switch(or){
				
			case 0  : dx=16*x-16*z; break;
			case 1  : dx=10*x-19*z; break;
			case 2  : dx=6*x-22*z; break;
			case 3  : dx=3*x-21*z; break;
			case 4  : dx=-22*z; break;
			case 5  : dx=-3*x-21*z; break;
			case 6  : dx=-6*x-22*z; break;
			case 7  : dx=-10*x-20*z; break;
				
			case 8  : dx=-16*z-16*x; break;
			case 9  : dx=-10*z-19*x; break;
			case 10 : dx=-6*z-22*x; break;
			case 11 : dx=-3*z-21*x; break;			
			case 12 : dx=-22*x; break;
			case 13 : dx=3*z-21*x; break;
			case 14 : dx=6*z-22*x; break;
			case 15 : dx=10*z-20*x; break;
				
			case 16 : dx=-16*x+16*z; break;
			case 17 : dx=-10*x+19*z; break;
			case 18 : dx=-6*x+22*z; break;
			case 19 : dx=-3*x+21*z; break;
			case 20 : dx=22*z; break;
			case 21 : dx=3*x+21*z; break;
			case 22 : dx=6*x+22*z; break;
			case 23 : dx=10*x+20*z; break;
				
			case 24 : dx=16*z+16*x; break;
			case 25  : dx=10*z+19*x; break;
			case 26 : dx=6*z+22*x; break;
			case 27 : dx=3*z+21*x; break;			
			case 28 : dx=22*x; break;
			case 29 : dx=-3*z+21*x; break;
			case 30 : dx=-6*z+22*x; break;
			case 31 : dx=-10*z+20*x; break;
																								
		}
		if(dx%2==0) dx++;
		dx=dx>>10;
		
		return (CANVX/2)+dx;
	}
	
	public int isometricY(int px, int py, int pz, int or)
	{
		int x,y,z,dy=0;
		
		x=px-game.pl.realx; y=py-game.pl.realy; z=pz-game.pl.realz;		
		
		switch(or){
				
			case 0  : dy=8*x+8*z-16*y; break;
			case 1  : dy=10*x+5*z-16*y; break;
			case 2  : dy=11*x+4*z-16*y; break;
			case 3  : dy=12*x+2*z-16*y; break;
			case 4  : dy=12*x-16*y; break;
			case 5  : dy=+12*x-2*z-16*y; break;
			case 6  : dy=11*x-4*z-16*y; break;
			case 7  : dy=+10*x-5*z-16*y; break;
				
			case 8  : dy=-8*z+8*x-16*y; break;
			case 9  : dy=-10*z+5*x-16*y; break;
			case 10 : dy=-11*z+4*x-16*y; break;
			case 11 : dy=-12*z+2*x-16*y; break;
			case 12 : dy=-12*z-16*y; break;
			case 13 : dy=-12*z-2*x-16*y; break;
			case 14 : dy=-11*z-4*x-16*y; break;
			case 15 : dy=-10*z-5*x-16*y; break;
				
			case 16 : dy=-8*x-8*z-16*y; break;
			case 17 : dy=-10*x-5*z-16*y; break;
			case 18 : dy=-11*x-4*z-16*y; break;
			case 19 : dy=-12*x-2*z-16*y; break;
			case 20 : dy=-12*x-16*y; break;
			case 21 : dy=-12*x+2*z-16*y; break;
			case 22 : dy=-11*x+4*z-16*y; break;
			case 23 : dy=-10*x+5*z-16*y; break;
				
			case 24 : dy=8*z-8*x-16*y; break;
			case 25 : dy=10*z-5*x-16*y; break;
			case 26 : dy=11*z-4*x-16*y; break;
			case 27 : dy=12*z-2*x-16*y; break;
			case 28 : dy=12*z-16*y; break;
			case 29 : dy=12*z+2*x-16*y; break;
			case 30 : dy=11*z+4*x-16*y; break;
			case 31 : dy=10*z+5*x-16*y; break;
																								
		}
		//if(dy%2==0) dy++;
		dy=dy>>10;
		
		return (CANVY-55)/2-10+dy;		
	}	
	
	public void hideNotify()
	{
		game.doUpdate = false;
	}
	
	public void showNotify()
	{
		game.doUpdate = true;
	}
	
	// Control stuff =====================================================================
	
	public boolean up, down, left, right, but1, but2, but3, but4, any, anybut;	
	
	public void controlReset()
	{
		up=false; down=false; left=false; right=false; 
		but1=false; but2=false; but3=false; but4=false;		
		any=false; anybut=false;
	}	
	

}