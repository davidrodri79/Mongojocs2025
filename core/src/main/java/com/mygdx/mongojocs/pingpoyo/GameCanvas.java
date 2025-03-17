package com.mygdx.mongojocs.pingpoyo;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================



//import javax.microedition.lcdui.*;
//import com.nokia.mid.ui.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class GameCanvas extends BiosCanvas
{
	Image pictureImg, tableImg, facesImg, netImg, ballImg, playerImg, opImg, globeImg, introImg, peopleImg, charsImg[],
			cloudImg, crowImg, window1Img, window2Img, cupImg;
	byte opCoo[];
	boolean doRepaintMenu = false;
	int animFrame = 0;
	Graphics g;
	
	public GameCanvas(Game ga) 
	{		
		super(ga);
								
	}
	
	public void loadInitialGfx()
	{
		try{
			
			pictureImg = Image.createImage("/Picture.png");				
			introImg = Image.createImage("/Intro.png");				
			peopleImg = Image.createImage("/People.png");			
			facesImg = Image.createImage("/Faces.png");						
			tableImg = Image.createImage("/Table.png");			
			netImg = Image.createImage("/Net.png");
			ballImg = Image.createImage("/Ball.png");
			playerImg = Image.createImage("/Player.png");			
			globeImg = Image.createImage("/Globe.png");						
			cloudImg = Image.createImage("/Cloud.png");						
			crowImg = Image.createImage("/Crow.png");						
			window1Img = Image.createImage("/Window1.png");						
			window2Img = Image.createImage("/Window2.png");									
			cupImg = Image.createImage("/Cup.png");									
	
			charsImg = new Image[7];
			
			charsImg[0] = Image.createImage("/Leo.png");
			charsImg[1] = Image.createImage("/Bob.png");
			charsImg[2] = Image.createImage("/Blak.png");			
			charsImg[3] = Image.createImage("/Pigy.png");			
			charsImg[4] = Image.createImage("/Ponk.png");			
			charsImg[5] = Image.createImage("/Robodk.png");			
			charsImg[6] = Image.createImage("/Killdk.png");			
									
		}catch(Exception e){ }		
	}
	
	public void loadPicture() 
	{
							
	}
	
	public void destroyPicture()
	{
		
	}
	
	public void loadIntro()
	{
							
	}
	
	public void destroyOppGfx()
	{
		
	}
	
	public void destroyIntro()
	{
		//introImg = null; System.gc();	
	}
	
	public void loadMatchGfx()
	{
		opCoo = new byte[(ga.round == 6 ? 486 : 216)]; 
		
		switch(ga.round){				
				case 0 : ga.loadFile(opCoo,"/Leo.coo"); break;
				case 1 : ga.loadFile(opCoo,"/Bob.coo"); break;
				case 2 : ga.loadFile(opCoo,"/Blak.coo"); break;
				case 3 : ga.loadFile(opCoo,"/Pigy.coo"); break;
				case 4 : ga.loadFile(opCoo,"/Ponk.coo"); break;
				case 5 : ga.loadFile(opCoo,"/Robodk.coo"); break;
				case 6 : ga.loadFile(opCoo,"/Killdk.coo"); break;
			}					
		opImg = charsImg[ga.round];
	}
	
	public void destroyMatchGfx()
	{
		/*opImg = null; opCoo = null; 
		
		tableImg = null;		
		netImg = null;
		ballImg = null;
		playerImg = null;			
		globeImg = null;			
		System.gc();	*/
	}
	
	// **************************************************************************//
	// Inicio Clase com.mygdx.mongojocs.bravewar.com.mygdx.mongojocs.sanfermines2006.GameCanvas
	// **************************************************************************//
	
	// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	// *******************************************
	// -------------------------------------------
	// Picar el c�digo del juego a partir de AQUI:
	// ===========================================
	// Juego: 
	// Terminal: 
	
	// ---------------------------------
	//  Bits de Control de cada Terminal
	// =================================
	final boolean deviceSound = true;
	final boolean deviceVibra = true;
	final boolean deviceKeyconf = false;
	// ----------------------------------
	
	// ===========================================
	// *******************************************
	// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
	
	
	// *******************
	// -------------------
	// canvas - Engine
	// ===================
	// *******************
	
	// -------------------
	// canvas Init
	// ===================

	String[] introTxt;
	
	public void canvasInit()
	{
		soundCreate();
		//canvasWidth = 128; canvasHeight = 128;
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Graphics._fontGenerate(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL, Color.WHITE);
				introTxt = textoBreak(ga.gameText[23][0], 151, Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
			}
		});
	}

	
	// -------------------
	// Draw
	// ===================
	
	public void Draw(Graphics g2)
	{
		int x, y;
		g = g2;			
					
		if (ga.playShow) { 
			
			ga.playShow=false; //playDraw(); 
			
			switch(ga.gameStatus)
			{
				
				case Game.INTRO :
				int c = (ga.cnt*2)%228;
				canvasFill(0x00000);								
				fireWorks(88,88,32,16,255,255,0,c);
				fireWorks(88,88,130,32,255,0,255,c-44);
				fireWorks(88,88,70,10,0,255,255,c-80);
				fireWorks(88,88,70,10,0,255,0,c-90);
				fireWorks(88,88,88,40,255,255,255,c-100);
				fireWorks(88,88,88,20,0,0,255,c-110);
				fireWorks(88,88,100,32,255,255,255,c-126);
				showImage(introImg,0,0,176,208,0,94);				
				/*g.setClip(0,canvasHeight-36,canvasWidth,36);
				g.setColor(30,104,34);				
				g.fillRoundRect(0,canvasHeight-36,canvasWidth-1,35,8,8);
				g.setColor(189,213,78);
				g.drawRoundRect(0,canvasHeight-36,canvasWidth-1,35,8,8);*/
				g.setClip(12,152,151,46);
				canvasTextCreate(12,152,151,46);
				textDraw(introTxt, 0, 60-(2*ga.cnt/3), 0xFFFFFF, TEXT_BOLD);
				canvasTextCreate();
				break;
				
				case Game.CHOOSE_OPP : 
				canvasFill(0xFFFF00);	
				x=(canvasWidth-160)/2; y=2+(canvasHeight-110)/2; 
				textDraw(ga.gameText[25][0],0,y-20,0xFFFFFF,TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_OUTLINE);				
				for(int i=0; i<7; i++) showFace((i>3 ? 20 : 0)+x+40*(i%4),y+(57*(i/4)),(i<ga.available ? 1+i : 8)); 
				g.setClip(0,0,canvasWidth,canvasHeight); g.setColor(255,0,0); 
				g.drawRect((ga.round>3 ? 20 : 0)+x+40*(ga.round%4),y+(57*(ga.round/4)),40,40);	break;
				
				case Game.VERSUS :
				int offset;				
				if(ga.cnt>=30) {showAnimation(); offset=0;}				
				else {canvasFill(0x00000); offset=(30-ga.cnt)*2;}
				y=(offset*2)+(canvasHeight-40)/2;
				showImage(window1Img,0,0,156,64,8,y-3);
				g.setClip(0,0,canvasWidth,canvasHeight);
				/*g.setColor(30,104,34);				
				g.fillRoundRect(10,y,canvasWidth-20,64,8,8);
				g.setColor(189,213,78);
				g.drawRoundRect(10,y,canvasWidth-20,64,8,8);*/
				
				textDraw((ga.gameMode == ga.TOURNAMENT_MODE ? ga.gameText[24][ga.round] : ga.gameText[20][0]),0,50-offset,0xFFFFFF,TEXT_LARGER | TEXT_BOLD | TEXT_HCENTER | TEXT_SHADED);				
				//textDraw("Vs",0,y+18,0xFFFFFF,TEXT_HCENTER | TEXT_LARGER | TEXT_BOLD/* | TEXT_OUTLINE*/);				
				showFace(20,y+5,0);
				showFace(canvasWidth-63,y+5,1+ga.round);
				break;

				case Game.NEXT_SERVICE:
				case Game.PLAYER_SERVICE :				
				case Game.OPPONENT_SERVICE :				
				case Game.GAME_PLAY :				
				showGameAction();								
				break;
				
				case Game.PLAYER_FIRST :				
				showGameAction();
				boxedText(ga.gameText[14][0]+" "+ga.gameText[13][0],140);								
				break;
				
				case Game.OPPONENT_FIRST :				
				showGameAction();
				boxedText(ga.gameText[14][0]+" "+ga.gameText[13][1+ga.round],140);								
				break;
				
				case Game.FIRST_FAIL :								
				showGameAction();					
				if(ga.cnt>20) boxedText(ga.gameText[15][0],140);
				break;
				
				case Game.OPPONENT_POINT :				
				case Game.PLAYER_POINT :
				showGameAction();
				if(ga.cnt<=30) opponentTalk(ga.round,(ga.gameStatus==Game.PLAYER_POINT ? 4 : 0)+((ga.playerScore+ga.opponentScore)%4),55);										
				if(ga.cnt>30) boxedText(ga.gameText[13][0]+" "+ga.playerScore+" - "+ga.gameText[13][1+ga.round]+" "+ga.opponentScore,140);										
				break;
				
				case Game.YOU_WIN :
				showAnimation();
				showFace((canvasWidth-40)/2,10+(canvasHeight-53)/2,0);
				g.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[16][0], 0, canvasHeight/5, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_OUTLINE);
				textDraw(ticksToTime(ga.ellapsedTicks + ga.lastRoundTime), 0, 4*(canvasHeight/5), 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_OUTLINE);
				break;
				
				case Game.YOU_LOSE :
				showAnimation();
				PutSprite(opImg,(canvasWidth-128)/2,(canvasHeight-64)/2,41,(ga.cnt/2)%2,opCoo,(ga.round==6 ? 9 : 4),false);
				g.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[17][0], 0, canvasHeight/5, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_SHADED);
				if(ga.gameMode == Game.TOURNAMENT_MODE){
					textDraw(ga.gameText[18][0], 0, 4*(canvasHeight/5), 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_SHADED);
					textDraw(ga.gameText[32][0], 10, canvasHeight - 15, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD | TEXT_SHADED);
					textDraw(ga.gameText[32][1], canvasWidth-20, canvasHeight - 15, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD | TEXT_SHADED);
				}
				break;
				
				case Game.VIEW_RECORDS :
				x=(canvasWidth-96)/2; y=2+(canvasHeight-65)/2;
										
				showAnimation();
								
				textDraw(ga.gameText[41][0], 0, 45, 0xFFFF00, TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_OUTLINE);
																				
				for(int i=0; i<3; i++){
					textDraw((i+1)+".", canvasWidth/6, y+25+16*i, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD |TEXT_OUTLINE);
					textDraw(ga.recordNames[i], 6+canvasWidth/4, y+25+16*i, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD |TEXT_OUTLINE);
					textDraw(ticksToTime(ga.recordTimes[i]), canvasWidth/2, y+25+16*i, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD |TEXT_OUTLINE);
				}
					
				break;
								
				case Game.TOURNAMENT_END :				
				
				x=(canvasWidth-96)/2; y=127;
										
				showAnimation();
				showImage(introImg,0,0,176,208,0,94);
				showImage(cupImg,0,0,94,86,41-15,54);				
				
				g.setClip(0,0,canvasWidth,canvasHeight);
												
				textDraw(ga.gameText[26][0], 0, 20, 0xFFFF00, TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_OUTLINE);
				textDraw(ga.gameText[27][0]+" "+ticksToTime(ga.ellapsedTicks+ga.lastRoundTime), 0, 40, 0xFFFF00, TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER | TEXT_OUTLINE);
																								
				for(int i=0; i<3; i++){
					textDraw((i+1)+".", canvasWidth/6, y+25+16*i, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD);
					textDraw(ga.recordNames[i], 6+canvasWidth/4, y+25+16*i, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD);
					textDraw(ticksToTime(ga.recordTimes[i]), canvasWidth/2, y+25+16*i, 0xFFFFFF, TEXT_MEDIUM | TEXT_BOLD);
				}
					
				break;
				
				case Game.SHOW_TIP :
				if(ga.cnt<5)
					showGameAction();
					boxedTip(ga.gameText[31][ga.tipRequest + (ga.gameKeyconf ? 0 : 10)]);
				break;
			}
		
		}
		
		if(doRepaintMenu){
			switch(ga.gameStatus)
			{			
				case Game.GAME_MENU_MAIN :				
				if(pictureImg!=null) showImage(pictureImg,0,0,canvasWidth,canvasHeight,0,0);			
				break;
						
				case Game.GAME_MENU_SECOND :
				ga.updateScore=true;
				showGameAction();
				break;
			}
		}
		
	}
	
	// MY FUNCTIONS
		
	public static final int TABLECENTERX = 88;
	public static final int ORIGINY = 45;
	public static final int SCALEX = 210;
	public static final int SCALEY = 150;
	public static final int PERSPECTIVE = 64;
	public static final int BASEZ = 440;
	public static final int BASEY = 320;
	
	
	public int XYZtoX(int x, int y, int z)	
	{
		
		z+=BASEZ;
		y+=BASEY;
		
		z =(z*PERSPECTIVE)>>8;
		
		z = (z!=0 ? z : 1);
		
		return TABLECENTERX + (SCALEX*(x/4))/z;
	} 
	
	public int XYZtoY(int x, int y, int z)
	{
		
		z+=BASEZ;
		y+=BASEY;
		
		z =(z*PERSPECTIVE)>>8;
		
		z = (z!=0 ? z : 1);
		
		return ORIGINY + (SCALEY*(y/4))/z;
	} 
	
	public void showFace(int x, int y, int id)
	{		
		showImage(facesImg,40*id,0,40,53,x,y);
	}
	
	public void showGameAction()
	{
		int x, y, rgb[][]={{23,131,255},{120,183,255},{189,255,187},{79,164,229},{139,129,255},{255,240,125},{255,227,114}};
		
		// Table		
		
		if (ga.gameStatus == Game.PLAYER_POINT || ga.gameStatus == Game.OPPONENT_POINT)
		{
			 x = (((ga.cnt/3)%2==0) ? 176 : 352);
			 
		} else x = 0;
				
		g.setClip(0,0,canvasWidth,canvasHeight);
		g.setColor(rgb[ga.round][0],rgb[ga.round][1],rgb[ga.round][2]);
		g.fillRect(0,0,canvasWidth,107);
		
		showImage(cloudImg,0,0,88,49,canvasWidth-((animFrame/6)%250),15);
		
		showImage(crowImg,((animFrame/4)%2==0 ? 0 : 14),0,14,12,((animFrame)%400),30);
		
		showImage(peopleImg,x,0,176,79,0,28);		
		showImage(tableImg,0,0,176,153,0,55);
				
		animFrame ++;
		
		showOpponent();
						
		// Invisible table bounds
		/*g.setClip(0,0,176,208);
		g.setColor(255,0,0);
		g.drawLine(XYZtoX(-128,0,128),XYZtoY(-128,0,+128),XYZtoX(-128,0,-128),XYZtoY(-128,0,-128));				
		g.drawLine(XYZtoX(-128,0,-128),XYZtoY(-128,0,-128),XYZtoX(128,0,-128),XYZtoY(128,0,-128));
		g.drawLine(XYZtoX(128,0,-128),XYZtoY(128,0,-128),XYZtoX(128,0,+128),XYZtoY(128,0,+128));
		g.drawLine(XYZtoX(128,0,0),XYZtoY(128,0,0),XYZtoX(-128,0,0),XYZtoY(-128,0,0));
		g.drawLine(XYZtoX(128,0,+128),XYZtoY(128,0,+128),XYZtoX(-128,0,+128),XYZtoY(-128,0,+128));*/
		
		
		showBallShadow();
		
		if(ga.ball.z>0) showBall();
		
		// Net
		x=(canvasWidth-142)/2; y=142;
		g.setClip(x,y,142,14);
		g.drawImage(netImg,x,y,20);				
		
		if(ga.ball.z<=0 && ga.ball.z>=-144) showBall();
		
		showPlayer();
		
		if(ga.ball.z<-144) showBall();
						
		// Indicators
		int green;
		g.setClip(0,0,canvasWidth,canvasHeight);		
		
		g.setColor(255,255,255);		
		g.fillRect(8,56,42,6);
				
		green = 6*ga.pl.power; if(ga.pl.power==42 && ga.cnt%2==0) green = 0;
		if(green<0) green = 0; if(green>255) green = 255; 
		g.setColor(255,green,0);		
		g.fillRect(8,56,ga.pl.power,6);
		
		g.setColor(0,0,0);		
		g.drawRect(8,56,42,6);
		
		g.setColor(255,255,255);		
		g.fillRect(canvasWidth-50,56,42,6);
		
		green = 6*ga.op.power; if(ga.op.power==42 && ga.cnt%2==0) green = 0;
		if(green<0) green = 0; if(green>255) green = 255; 
		g.setColor(255,green,0);		
		g.fillRect(canvasWidth-50+42-ga.op.power,56,ga.op.power,6);		
		
		g.setColor(0,0,0);		
		g.drawRect(canvasWidth-50,56,42,6);
		
		//if(ga.updateScore || ga.gameMode == com.mygdx.mongojocs.sanfermines2006.Game.TUTORIAL_MODE){
			//g.setClip(0,0,176,41);
			//g.drawImage(peopleImg,0,0,20);
			
			if(ga.gameMode != Game.TUTORIAL_MODE){
						
				g.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.playerScore+"/"+ga.opponentScore,0,5,0xFFFFFF,TEXT_HCENTER | TEXT_BOLD | TEXT_LARGER | TEXT_OUTLINE);
			}
		//}
		showFace(2,2,0);		
		showFace(canvasWidth-42,2,1+ga.round);
			
		
	}
	
	public void showBallShadow()
	{		
		int x, y, sw, sh;
				
		// Shadow						
		if(ga.ball.insideTableBounds()){
			
			g.setClip(0,0,canvasWidth,canvasHeight);
			g.setColor(20,20,20);
			x=XYZtoX(ga.ball.x,0,ga.ball.z);
			y=XYZtoY(ga.ball.x,0,ga.ball.z);
						
			if(ga.ball.z<-60){
				sw=8; sh=6;
			}else if (ga.ball.z<32){
				sw=6; sh=4;
			}else{
				sw=4; sh=3;
			}									
						
			g.fillArc(x-sw/2,y-sh/2,sw,sh,0,360);
		}
		
	}
	
	public void showBall()
	{
		int x, y, sy;
		
		if(ga.ball.z>140 || ga.ball.z<-192) return;
		
		x=XYZtoX(ga.ball.x,ga.ball.y,ga.ball.z);
		y=XYZtoY(ga.ball.x,ga.ball.y,ga.ball.z);	
		
		sy = 0;
		
		if(ga.ball.speed >= Ball.MAX_SPEED ) 
		switch(ga.cnt%4)
		{
			case 0 : sy = 16; break;	
			case 3 :
			case 1 : sy = 32; break;	
			case 2 : sy = 48; break;
		}			
							
		// Ball		
		if(ga.ball.z<-60){			
			showImage(ballImg,0,sy,11,11,x-5,y-5);
		}else if (ga.ball.z<32){
			showImage(ballImg,16,sy,8,8,x-4,y-4);		
		}else{
			showImage(ballImg,32,sy,6,6,x-3,y-3);								
		}
								
	}
	
	public void showPlayer()
	{
		int x, y, py, px,  fr;
		
		px = (ga.gameAuto ? (ga.ball.x - ga.pl.x)/8 : 0);
		py = 30 + ((ga.ball.y - 30)/4);
		
		x =	XYZtoX(ga.pl.x,py,-120) + px; y = XYZtoY(ga.pl.x,py,-120) - 20 + (Trig.sin(ga.pl.cnt*6)>>8) - ga.pl.offset;
				
		switch(ga.pl.state){
			default : fr = (ga.pl.x<=0 ? 0 : ga.pl.x / 21 ); break;
			case Player.CHARGE :
			case Player.RELEASE :
			fr = (ga.pl.x<=0 ? 1 + ((ga.pl.power-1)/14) : 5 - ((ga.pl.power-1)/14));
			break;
		}
		
		if(fr == 3)
			if(ga.pl.x==-128 || ga.pl.x==0) x+=6;
			else if(ga.pl.x==128) x-=10;
					
		/*g.setClip(x-8,y-8,16,16);
		g.drawImage(playerImg,x-8-fr*16,y-8,20);*/
		showImage(playerImg,32*fr,0,32,32,x-16,y-16);
	}
	
	public void showOpponent()
	{
		// Opponent
		int fr=0;
		
		if(ga.gameStatus == Game.PLAYER_POINT) fr = 8;
		else if(ga.gameStatus == Game.OPPONENT_POINT) fr = (ga.cnt/2)%2; 			
		else switch(ga.op.state)
		{
			case Opponent.IDLE : 
			fr = 0; 			
			break;
			case Opponent.FIRST : 
			case Opponent.CHARGE : 
				if(ga.op.x == -1) {if(ga.op.cnt<2) fr=4; else if(ga.op.cnt<4) fr=2; else fr=3;}
				if(ga.op.x == 0) {if(ga.op.cnt<2) fr=4; else if(ga.op.cnt<4) fr=4; else fr=5;}
				if(ga.op.x == 1) {if(ga.op.cnt<2) fr=4; else if(ga.op.cnt<4) fr=6; else fr=7;}
			break;
			case Opponent.RELEASE : 
				if(ga.op.x == -1) {if(ga.op.cnt<2) fr=3; else if(ga.op.cnt<6) fr=2; else fr=4;}
				if(ga.op.x == 0) {if(ga.op.cnt<2) fr=5; else if(ga.op.cnt<6) fr=4; else fr=4;}
				if(ga.op.x == 1) {if(ga.op.cnt<2) fr=7; else if(ga.op.cnt<6) fr=6; else fr=4;}
			break;
		}
		
		PutSprite(opImg,24,64,41,fr,opCoo,(ga.op.charId == Opponent.KILLDK ? 9 : 4),false);
	}
	
	public void fireWorks(int x1, int y1, int x2, int y2, int rr, int gg, int bb, int step)
	{
		int s1, s2, xx1, yy1;
		int half=40;
		
		
			
		if(step>=0 && step<half)
		{
			y2+=(step*step)/200;
						
			xx1 = x1 + (step*(x2-x1))/half;
			yy1 = y1 + (step*(y2-y1))/half;
						
			g.setColor(rr,gg,bb);
			g.fillRect(xx1,yy1,1,1);
		
			if(step==half-2)
				g.fillRect(0,0,canvasWidth,canvasHeight);	
		}
							
		if(step>=half && step<half+32){
			
			y2+=(step*step)/200;
			
			int fade = (step-half)*6;
			
			g.setColor((rr-fade > 0 ? rr-fade : 0),(gg-fade > 0 ? gg-fade : 0),(bb-fade > 0 ? bb-fade : 0));
			for(int i=0; i<256; i+=16){
				
				g.fillRect(x2+((((step-half))*Trig.cos(i))>>10),y2+((((step-half))*Trig.sin(i))>>10),1,1);
			}
		}
		
	}
	
	public String ticksToTime(long ticks)
	{
		long min = ticks/1500,
			sec = (ticks/25)%60,
			dec = ((10*ticks)/25)%10;
			
		return min+"' "+(sec<10 ? "0"+sec : ""+sec)+"'' "+dec;
		
	}

// <=- <=- <=- <=- <=-



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
		if(!inv) showImage(Img,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY);
		else	 showImage(Img,SourX+128,SourY+128,SizeX,SizeY,X+CuadSize-SizeX-DestX,Y+DestY);		
		}
	}
	
	public void boxedText(String t, int y)
	{
		g.setClip(0,0,canvasWidth,canvasHeight);
		/*g.setColor(30,104,34);
		g.fillRoundRect(10,y-10,canvasWidth-20,26,8,8);
		g.setColor(189,213,78);
		g.drawRoundRect(10,y-10,canvasWidth-20,26,8,8);*/
		showImage(window2Img,0,0,159,31,7,y-15);
		textDraw(t,0,y-4,0xFFFFFF,TEXT_MEDIUM | TEXT_BOLD | TEXT_HCENTER /* | TEXT_OUTLINE*/);
	}
	
	public void opponentTalk(int op, int t, int y)
	{
		String txt[] = _textoBreak(ga.gameText[33+op][t], canvasWidth-32, Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		
		g.setClip(0,0,canvasWidth,canvasHeight);
		g.setColor(255,255,255);
		g.fillRoundRect(10,y-10,canvasWidth-20,12+(14*txt.length),16,16);
		g.setColor(0,0,0);
		g.drawRoundRect(10,y-10,canvasWidth-20,12+(14*txt.length),16,16);
		//textDraw(ga.gameText[16+op][t],0,y-4,0x000000,TEXT_HCENTER /* | TEXT_OUTLINE*/);
		
		for(int i=0; i<txt.length; i++)
			textDraw(txt[i], 0, y-4+(i*14), 0x000000,TEXT_MEDIUM | TEXT_HCENTER);
			
		showImage(globeImg,0,0,14,14,40,y+1+(14*txt.length));
	}
	
	public void boxedTip(String t)
	{
		String txt[] = textoBreak(t, canvasWidth-16, Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));			
		int sy=10*txt.length+6;
		int y=(canvasHeight-sy)/2;
		
		g.setClip(0,0,canvasWidth,canvasHeight);
		g.setColor(128,0,0);
		g.fillRoundRect(4,y-10,canvasWidth-8,12+(14*txt.length),8,8);
		g.setColor(255,0,0);
		g.drawRoundRect(4,y-10,canvasWidth-8,12+(14*txt.length),8,8);
		//textDraw(ga.gameText[16+op][t],0,y-4,0x000000,TEXT_HCENTER /* | TEXT_OUTLINE*/);
		
		for(int i=0; i<txt.length; i++)
			textDraw(txt[i], 0, y-4+(i*14), 0xFFFFFF,TEXT_HCENTER);
					
	}
	
	public void showAnimation()
	{
					
		int a;
		
		g.setClip(0,0,canvasWidth,canvasHeight);
		g.clipRect(0,0,canvasWidth,canvasHeight);
		g.setColor(255,232,152);
		g.fillRect(0,0,canvasWidth,canvasHeight);
				
		g.setColor(255,180,0);
		
		DirectGraphics dg = DirectGraphics.getDirectGraphics(g); 
				
		for(int i = 0; i<256; i+=32)
		{
			a = i + (ga.cnt*3) % 256;
			
			dg.fillTriangle(88,118,88+(150*Trig.cos(a)>>10),118+(150*Trig.sin(a)>>10),88+(150*Trig.cos(a+16)>>10),118+(150*Trig.sin(a+16)>>10),0xFFFEB400);
		}
		
		
		
		
	}


// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************

// -------------------
// play Create Gfx
// ===================

/*public void playCreate_Gfx()
{
}

// -------------------
// play Destroy Gfx
// ===================

public void playDestroy_Gfx()
{
}

// -------------------
// play Init Gfx
// ===================

public void playInit_Gfx()
{
}

// -------------------
// play Release Gfx
// ===================

public void playRelease_Gfx()
{
}

// -------------------
// play Draw Gfx
// ===================

public void playDraw()
{
	canvasFill(0);

	scr.setColor(0xffffff);
	scr.fillRect(ga.protX,ga.protY, 16,16);


	scr.setColor(0xff0000);
	scr.fillRect(0,canvasHeight/2, 16,16);

	scr.setColor(0x0000ff);
	scr.fillRect(canvasWidth-16,canvasHeight/2, 16,16);


	textDraw(new String[] {"uno","dos","tres"}, 0, 0, 0xff0000, TEXT_HCENTER|TEXT_VCENTER);

}*/

// <=- <=- <=- <=- <=-











// **************************************************************************//
// Final Clase com.mygdx.mongojocs.bravewar.com.mygdx.mongojocs.sanfermines2006.GameCanvas
// **************************************************************************//
};