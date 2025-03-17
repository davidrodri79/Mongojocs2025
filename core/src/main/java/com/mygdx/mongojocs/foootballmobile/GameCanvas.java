package com.mygdx.mongojocs.foootballmobile;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================


import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class GameCanvas extends BiosCanvas
{
	static final byte playerInfo[][][]=
	{
		{{ 0, 2}, { 0, 3}, { 0, 0}, { 0, 3}, { 0, 2}, { 0, 0}, { 0, 0}, { 0, 3}, { 0, 3}, { 0, 3}, { 0, 0}},
		{{ 0, 1}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 2}, { 0, 0}, { 0, 0}, { 0, 0}}, 
		{{ 0, 3}, { 0, 3}, { 0, 3}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 1}, { 0, 3}},
		{{ 0, 0}, { 0, 2}, { 0, 3}, { 0, 0}, { 0, 1}, { 0, 2}, { 0, 3}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 3}},
		{{ 0, 2}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 3}, { 0, 0}, { 0, 0}, { 0, 3}, { 0, 0}, { 0, 0}, { 0, 3}},  		
		{{ 0, 1}, { 0, 0}, { 0, 2}, { 1, 4}, { 1, 4}, { 1, 4}, { 0, 0}, { 0, 1}, { 0, 0}, { 1, 4}, { 1, 4}},
		{{ 0, 0}, { 0, 1}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 1}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}},
		{{ 0, 3}, { 0, 1}, { 0, 3}, { 1, 0}, { 1, 0}, { 0, 0}, { 0, 0}, { 0, 3}, { 0, 0}, { 0, 0}, { 1, 0}},
		{{ 0, 0}, { 1, 0}, { 0, 0}, { 1, 4}, { 0, 3}, { 0, 2}, { 1, 4}, { 0, 3}, { 0, 5}, { 1, 4}, { 0, 3}},
		{{ 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 3}, { 0, 0}, { 0, 0}},
		{{ 0, 0}, { 0, 0}, { 0, 0}, { 0, 2}, { 0, 0}, { 0, 3}, { 0, 0}, { 0, 0}, { 0, 2}, { 0, 3}, { 0, 0}},
		{{ 0, 0}, { 0, 0}, { 0, 0}, { 1, 4}, { 1, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 1, 4}, { 0, 0}},
		{{ 0, 0}, { 0, 0}, { 0, 0}, { 0, 2}, { 0, 0}, { 0, 0}, { 0, 2}, { 0, 2}, { 0, 3}, { 0, 0}, { 0, 1}},
		{{ 0, 0}, { 0, 3}, { 0, 0}, { 0, 1}, { 0, 0}, { 0, 0}, { 0, 3}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 3}},
		{{ 0, 2}, { 0, 3}, { 0, 5}, { 0, 1}, { 0, 2}, { 0, 0}, { 0, 0}, { 0, 2}, { 0, 3}, { 0, 3}, { 0, 0}},
		{{ 0, 0}, { 0, 5}, { 0, 0}, { 0, 3}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 0}, { 0, 3}}
	};
	
	static final short shirtInfo[][][]=
	{					
 		{{ 255, 255, 255}, { 75, 75, 75}, { 0, 0, 0}, { 154, 154, 154}},
   		{{ 255, 255, 255}, { 0, 54, 99}, { 237, 28, 36}, { 255, 255, 255}},
		{{ 255, 255, 255}, { 229, 205, 11}, { 0, 114, 188}, { 255, 255, 255}},
		{{ 237, 28, 36}, { 0, 114, 188}, { 255, 255, 255}, { 198, 156, 109}},
		{{ 237, 28, 36}, { 0, 114, 188}, { 255, 255, 255}, { 229, 205, 11}},
		{{ 0, 114, 188}, { 0, 0, 0}, { 255, 255, 255}, { 154, 154, 154}},
		{{ 0, 114, 188}, { 75, 75, 75}, { 255, 255, 255}, { 229, 205, 11}},
		{{ 255, 138, 0}, { 154, 154, 154}, { 255, 255, 255}, { 0, 0, 0}},
		{{ 255, 255, 255}, { 0, 114, 188}, { 237, 28, 36}, { 0, 0, 0}},
		{{ 0, 114, 188}, { 0, 54, 99}, { 255, 255, 255}, { 172, 211, 115}},
		{{ 237, 28, 36}, { 172, 211, 115}, { 255, 255, 255}, { 0, 94, 32}},
		{{ 237, 28, 36}, { 0, 0, 0}, { 255, 255, 255}, { 154, 154, 154}},
		{{ 237, 28, 36}, { 0, 54, 99}, { 255, 255, 255}, { 255, 255, 255}},
		{{ 255, 255, 255}, { 154, 154, 154}, { 0, 114, 188}, { 0, 0, 0}},
		{{ 229, 205, 11}, { 255, 255, 255}, { 109, 207, 246}, { 0, 54, 99}},
		{{ 237, 28, 36}, { 0, 54, 99}, { 255, 255, 255}, { 255, 255, 255}}		
	};
		
	Image grassImg, playerImg, blackImg, headImg, ballImg, flagsImg, clockImg, upGoalImg,
		  downGoalImg, borderHImg, borderVImg, numbersImg, patternImg, cupImg, shirtImg, panelImg,
		  bigFlagsImg, fieldImg, pictureImg;
	Graphics fieldGr;
	byte shirtCoo[], ledBmp[];
	boolean invLed=false;
	int camX, camY, ledFr=0;
	
	public GameCanvas(Game ga) {
		
		super(ga);
			
		flagsImg = loadImage("/flags.png");
		bigFlagsImg = loadImage("/bigFlags.png");
		patternImg = loadImage("/pattern.png");
		
		grassImg = loadImage("/grass.png");
		playerImg = loadImage("/player.png");
		blackImg = loadImage("/blackplayer.png");
				
		shirtImg = loadImage("/shirts.png");
		
		headImg = loadImage("/head.png");
		ballImg = loadImage("/ball.png");
		
		clockImg = loadImage("/clock.png");
		numbersImg = loadImage("/numbers.png");
		upGoalImg = loadImage("/upGoal.png");
		downGoalImg = loadImage("/downGoal.png");
		
		borderHImg = loadImage("/borderh.png");
		borderVImg = loadImage("/borderv.png");
		
		panelImg = loadImage("/panel.png");
				
		shirtCoo = new byte[9216];
		ga.loadFile(shirtCoo,"/shirt.coo");				
		ledBmp = new byte[4050];
		ga.loadFile(ledBmp,"/led.raw");		
		
		cupImg = loadImage("/cup.png");
		
		pictureImg = loadImage("/Caratula.png");		
		
		fieldImg = new Image();
		fieldImg._createImage(XScale*90 + 1,YScale*120 + 1);
		fieldGr = fieldImg.getGraphics();
	}
	
	public void loadMatchGfx()
	{
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				createField();
			}
		});
		
	}
	
	public void destroyMatchGfx()
	{

	}
	
	public void loadCupGfx()
	{
	
	}

// **************************************************************************//
// Inicio Clase GameCanvas
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
final boolean deviceVibra = false;
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

public void canvasInit()
{
	soundCreate();
}


// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{
	int x, y, formation[][];
	
	if (ga.playShow){
	
	 	ga.playShow=false; 
	 	
		switch(ga.gameStatus)
			{
				case Game.GAME_CHOOSE_TEAM :	
				showPattern();				
				showImage(bigFlagsImg,0,0,95,65,(canvasWidth-95)/2,(canvasHeight-63)/4);
				scr.setColor(255,255,255);
				scr.setClip(0,0,canvasWidth,canvasHeight);
				scr.drawRect((canvasWidth-95)/2 + 26*(ga.myTeam%4) - 2,(canvasHeight-63)/4 + 17*(ga.myTeam/4) - 2,21,17);				
				textDraw(ga.gameText[16][ga.myTeam], 0, 6*(canvasHeight/7), 0xFFFFFF, TEXT_HCENTER | TEXT_OUTLINE | TEXT_MEDIUM | TEXT_BOLD);
				break;				
				
				case Game.GAME_VERSUS :
				showPattern();				
				showBigFlag(ga.myTeam,canvasWidth/2 - 38, 3*canvasHeight/5 -4);
				showBigFlag(ga.oppTeam,canvasWidth/2 + 20, 3*canvasHeight/5 -4);
				scr.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[20][ga.tour.current-1], 0, canvasHeight/6, 0xFFFFFF, TEXT_MEDIUM | TEXT_HCENTER | TEXT_OUTLINE | TEXT_BOLD);
				textDraw(ga.gameText[16][ga.myTeam], 0, 2*canvasHeight/5, 0xFFFFFF, TEXT_HCENTER | TEXT_OUTLINE | TEXT_BOLD);
				textDraw("VS", 0, -2 + (3*canvasHeight/5), 0xFFFFFF, TEXT_MEDIUM | TEXT_HCENTER | TEXT_OUTLINE | TEXT_BOLD);
				textDraw(ga.gameText[16][ga.oppTeam], 0, 4*canvasHeight/5, 0xFFFFFF, TEXT_HCENTER | TEXT_OUTLINE | TEXT_BOLD);
				break;
								
				case Game.GAME_VIEW_TOURNAMENT :				
				showPattern();
				showTournament(ga.tour,true);
				scr.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[20][ga.tour.current-1], 0, 10, 0xFFFFFF, TEXT_HCENTER | TEXT_BOLD);								
				break;
				
				case Game.GAME_CURRENT_TOURNAMENT :				
				showPattern();
				showTournament(ga.tour,false);
				scr.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[20][ga.tour.current-1], 0, 10, 0xFFFFFF, TEXT_HCENTER | TEXT_BOLD);				
				break;
				
				case Game.GAME_CHOOSE_STRATEGY :
				formation = ga.formation[ga.myFormation];			
				showPattern();				
				scr.setClip(0,0,canvasWidth,canvasHeight);
				x=(canvasWidth-72)/2; y=(canvasHeight-96)/2;
				scr.setColor(0,200,0);
				scr.fillRect(x,y,72,96);
				scr.setColor(255,255,255);
				scr.drawRect(x,y,72,96);
				scr.drawLine(x,y+48,x+72,y+48);
				scr.drawArc(x+30,y+42,12,12,0,360);
				scr.drawRect(x+30,y+96-6,12,6);
				scr.drawRect(x+18,y+96-16,36,16);
				scr.drawRect(x+30,y,12,6);
				scr.drawRect(x+18,y,36,16);
				scr.setColor(255,0,0);
				for(int i=0; i<formation.length; i++)
					scr.fillArc(x+10+(8*formation[i][1]),y+78-(20*formation[i][0]),4,4,0,360);
				scr.fillArc(x+34,y+92,4,4,0,360);
				textDraw("< "+ga.gameText[17][ga.myFormation]+" >", 0, 6*(canvasHeight/7), 0xFFFFFF, TEXT_HCENTER | TEXT_OUTLINE | TEXT_MEDIUM | TEXT_BOLD);				
				break;
				
				case Game.GAME_PLAY :
				playDraw();
				break;				
				
				case Game.GAME_HALFTIME_WAIT :				
				int fr[]={-1,0,1,2,3,2,3,2,3,4,-1,-1,-1};
				showLed(fr[ledFr%fr.length],invLed);
				//if(invLed) invLed=false; else invLed=true;
				ledFr++;
				break;
				
				case Game.GAME_GOAL_WAIT :								
				showLed(5+(ledFr%5),invLed);				
				ledFr++;
				break;
				
				case Game.GAME_PENALTIES_WAIT :								
				showLed(10+(ledFr > 7 ? 7 : ledFr),invLed);				
				ledFr++;
				break;
				
				case Game.GAME_ENDGAME_WAIT :								
				showLed(18+(ledFr > 5 ? 5 : ledFr),invLed); 				
				ledFr++;
				break;
				
				case Game.GAME_CUP_WON :
				showPattern();
				showImage(cupImg,0,0,cupImg.getWidth(),cupImg.getHeight(),5+(canvasWidth-cupImg.getWidth())/2,(canvasHeight-cupImg.getHeight())/2);
				scr.setClip(0,0,canvasWidth,canvasHeight);
				textDraw(ga.gameText[22][0], 0, canvasHeight/10, 0xFFFFFF, TEXT_HCENTER | TEXT_OUTLINE | TEXT_BOLD);				
				textDraw(ga.gameText[16][ga.myTeam], 0, 8*canvasHeight/10, 0xFFFFFF, TEXT_HCENTER | TEXT_OUTLINE | TEXT_BOLD);				
				textDraw(ga.gameText[22][1], 0, 9*canvasHeight/10, 0xFFFFFF, TEXT_HCENTER | TEXT_OUTLINE | TEXT_BOLD);				
				break;
			}
		}

	if(doRepaintMenu){
		switch(ga.gameStatus)
		{
			case Game.GAME_MENU_SECOND :
			playDraw();
			break;
												
			case Game.GAME_SEE_RESULTS :
			showPattern();				
			break;
		}
		scr.setClip(0,0,canvasWidth,canvasHeight);
		doRepaintMenu = false;
	}
}

// <=- <=- <=- <=- <=-


// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************

// -------------------
// play Create Gfx
// ===================

public void playCreate_Gfx()
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
    //long dmarca = System.currentTimeMillis();
	boolean altShirt = shirtInfo[ga.myTeam][0][0] == shirtInfo[ga.oppTeam][0][0] &&
					shirtInfo[ga.myTeam][0][1] == shirtInfo[ga.oppTeam][0][1] &&
					shirtInfo[ga.myTeam][0][2] == shirtInfo[ga.oppTeam][0][2];
	int x, y;
	
	
	camX = (XScale*ga.cameraX)/2 - canvasWidth/2;
	camY = (YScale*ga.cameraY)/2 - canvasHeight/2;
			
	if(camX<-24) camX=-24;
	if(camY<-24) camY=-24;
	if(camX+canvasWidth>(XScale*90)+24) camX=(XScale*90)+24-canvasWidth;
	if(camY+canvasHeight>(XScale*120)+24) camY=(YScale*120)+24-canvasHeight;
		
	showField();
		
	for(int i=0; i<10; i++){
		showPlayerShadow(ga.teamA[i]);
		showPlayerShadow(ga.teamB[i]);
	}
	showPlayerShadow(ga.gkA);
	showPlayerShadow(ga.gkB);
	
	showBall(ga.ball);
	
	for(int i=0; i<10; i++){
		
		showPlayer(ga.myTeam,ga.teamA[i],playerInfo[ga.myTeam][i+1][1],playerInfo[ga.myTeam][i+1][0]>0,0);
		showPlayer(ga.oppTeam,ga.teamB[i],playerInfo[ga.oppTeam][i+1][1],playerInfo[ga.oppTeam][i+1][0]>0,(altShirt ? 2 : 0));		
	}		
		
	showPlayer(ga.myTeam,ga.gkA,playerInfo[ga.myTeam][0][1],playerInfo[ga.myTeam][0][0]>0,1);
	
	showPlayer(ga.oppTeam,ga.gkB,playerInfo[ga.oppTeam][0][1],playerInfo[ga.oppTeam][0][0]>0,(altShirt ? 3 : 1));
	
	if(YScale*23 - camY > 0)	
		showImage(upGoalImg,0,0,101,36,(XScale*45) - 50 - camX , (YScale*0) - 36 - camY);
	
	if(YScale*97 - camY < canvasHeight)
		showImage(downGoalImg,0,0,101,36,(XScale*45) - 50 - camX , (YScale*120) - 4 - camY);
		
	scr.setClip(0,0,canvasWidth,canvasHeight);	
	scr.setColor(255,255,0);
	
	if(ga.teamDown == ga.TEAMA){
		x = (XScale*ga.pointer/2)-camX-5;
		y = (YScale*0/2)-camY;
		if(y>-50) {
			if(y<0) y = 2;
			showImage(numbersImg,36,12,9,10,x,y);
		}
	}else{
		
		x = (XScale*ga.pointer/2)-camX-5;
		y = (YScale*120)-camY;
		if(y<canvasHeight + 50) {
			if(y>canvasHeight) y = canvasHeight - 10;		
			showImage(numbersImg,45,12,9,10,x,y);
		}
	}
	
	showImage(panelImg,0,0,32,21,4,canvasHeight-25);
	showImage(panelImg,32,0,32,21,canvasWidth-36,canvasHeight-25);
			
	if(ga.goalsA < 10)
	{
		showImage(numbersImg,9*ga.goalsA,0,9,12,23,canvasHeight - 20);	
	}
	
	if(ga.goalsB < 10)
	{
		showImage(numbersImg,9*ga.goalsB,0,9,12,canvasWidth - 32,canvasHeight - 20);	
	}
	
	showFlag(ga.myTeam, 8, canvasHeight - 18);
	showFlag(ga.oppTeam, canvasWidth - 20, canvasHeight - 18);
	
	showImage(clockImg,19*(ga.matchTime/(ga.durationTicks()/7)),0,19,22,5,5);
	
	if(ga.matchState == ga.PENALTIES){
		for(int i=0; i<ga.restA; i++)
			showImage(ballImg,0,0,6,6,2,canvasHeight - 19 - 7*i);
		for(int i=0; i<ga.restB; i++)
			showImage(ballImg,0,0,6,6,canvasWidth-8,canvasHeight - 19 - 7*i);			
	}
	//dmarca = System.currentTimeMillis()-dmarca;
	scr.setClip(0,0,128,128);
	//canvasTextCreate();
	
	//textDraw(ga.marca+" vs "+dmarca, 0, 0, 0xFFFFFF, TEXT_VCENTER|TEXT_HCENTER|TEXT_OUTLINE);
	//System.out.println("fsdafsd");
    //textDraw(ga.marca+" vs "+dmarca, 0,  20, 0xFFFFFF, TEXT_TOP| TEXT_HCENTER);

	/* MONGOTEST: Asi es como rota las imagenes DirectGraphics de Nokia :S
	scr.setClip(0, 0, 176, 208);
	scr.setColor(255,0,0);
	DirectUtils.getDirectGraphics(scr).drawImage(panelImg, 50,50, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_90);
	scr.drawRect(50,50,panelImg.getWidth(), panelImg.getHeight());// Flip X
	DirectUtils.getDirectGraphics(scr).drawImage(panelImg, 50,100, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_180);
	scr.drawRect(50,100,panelImg.getWidth(), panelImg.getHeight());// Flip X
	DirectUtils.getDirectGraphics(scr).drawImage(panelImg, 50,150, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_270);
	scr.drawRect(50,150,panelImg.getWidth(), panelImg.getHeight());// Flip X*/

}

// <=- <=- <=- <=- <=-


public void PutSprite(Image Img,  int X, int Y,  int Rejilla, int Frame, int Trozos, byte[] Coor, int Flip)
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
	

		switch (Flip)
		{
		case 0:
		ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX, Y+DestY, 0);	// Standard
		break;
	
		case 1:
		ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+(Rejilla-DestY-SizeY), Y+DestX, 5);	// Rotar 270
		break;
	
		case 2:
		ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+(Rejilla-DestX-SizeX), Y+(Rejilla-DestY-SizeY), 3);	// Rotar 180
		break;
	
		case 3:
		ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestY, Y+(Rejilla-DestX-SizeX), 4);	// Rotar 90
		break;
		}
	}
}

/*public void PutSprite(Image Img,  int X, int Y,  int CuadSize, int Frame, byte[] Coor, int Trozos, int rot)
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
	
		
		showImage(Img,SourX+128,SourY+128,SizeX,SizeY,X+DestX,Y+DestY,rot);		
		}
	}*/
	
public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
{
	if ((DestX       > canvasWidth)
	||	(DestX+SizeX < 0)
	||	(DestY       > canvasHeight)
	||	(DestY+SizeY < 0))
	{return;}

	scr.setClip(0, 0, canvasWidth, canvasHeight);
	
	DirectGraphics dg = DirectUtils.getDirectGraphics(scr);

	switch (Flip)
	{
	case 0:
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	return;

	case 1:
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	dg.drawImage(Sour, (DestX)-(Sour.getWidth()-SizeX-SourX), DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
	return;

	case 2:
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	dg.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
	return;

	case 3:
	scr.clipRect(DestX, DestY, SizeX, SizeY);
	dg.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
	return;

	case 4:
	scr.clipRect(DestX, DestY, SizeY, SizeX);
	dg.drawImage(Sour, (DestX)-SourY, DestY-(Sour.getWidth()-SourX-SizeX), Graphics.LEFT|Graphics.TOP, 90);	// Rotar 90
	return;

	case 5:
	scr.clipRect(DestX, DestY, SizeY, SizeX);
	dg.drawImage(Sour, (DestX)-(Sour.getHeight()-SourY-SizeY), (DestY)-SourX, Graphics.LEFT|Graphics.TOP, 270);	// Rotar 270
	return;
	}
}



// My functions

public static final int FIELDW = 96;
public static final int FIELDH = 128;

public static final int XScale = 6;
public static final int YScale = 6;

void createField()
{
	int x, y;
		
	fieldGr.setClip(0,0,fieldImg.getWidth(),fieldImg.getHeight());
		
	for(int i = 0; i<(fieldImg.getWidth()/192) +1; i++)
	for(int j = 0; j<(fieldImg.getHeight()/64) +1; j++)
		fieldGr.drawImage(grassImg,(192*i),(64*j),20);
				
	
	fieldGr.setColor(255,255,255);
	
		
	fieldGr.drawRect((XScale*0),(YScale*0),(XScale*90),(YScale*120));
	
	
	// Field center
	
	fieldGr.drawLine((XScale*0),(YScale*60),(XScale*90),(YScale*60));
	fieldGr.drawArc((XScale*36),(YScale*51),(XScale*18),(YScale*18),0,360);
	fieldGr.fillRect((XScale*45)-1,(YScale*60)-1,3,3);
	
	
	
	// North Area
	
	fieldGr.drawRect((XScale*34),(YScale*0),(XScale*22),(YScale*5));
	fieldGr.drawRect((XScale*25),(YScale*0),(XScale*40),(YScale*20));
	
	fieldGr.fillRect((XScale*45),(YScale*15),2,2);

	fieldGr.setClip(0,(YScale*20),fieldImg.getWidth(),fieldImg.getHeight());
	fieldGr.drawArc((XScale*31),(YScale*-4)+1,(XScale*27),(YScale*27),229,80);
	fieldGr.setClip(0,0,fieldImg.getWidth(),fieldImg.getHeight());
		
	// Corners
	fieldGr.drawLine((XScale*0),(YScale*2),(XScale*2),(YScale*0));
	fieldGr.drawLine((XScale*88),(YScale*0),(XScale*90),(YScale*2));
		
	// South Area
	
	fieldGr.drawRect((XScale*36),(YScale*115),(XScale*18),(YScale*5));
	fieldGr.drawRect((XScale*25),(YScale*100),(XScale*40),(YScale*20));
	
	fieldGr.fillRect((XScale*45),(YScale*105),2,2);

	fieldGr.setClip(0,0,fieldImg.getWidth(),(YScale*100));
	fieldGr.drawArc((XScale*31),(YScale*97)-1,(XScale*27),(YScale*27),49,80);
	fieldGr.setClip(0,(YScale*20),fieldImg.getWidth(),fieldImg.getHeight());
		
	// Corners
	fieldGr.drawLine((XScale*0),(YScale*118),(XScale*2),(YScale*120));
	fieldGr.drawLine((XScale*88),(YScale*120),(XScale*90),(YScale*118));
		
}


void showField()
{	
	int x, y;
	
	x = (64+camX)%64;
	y = (64+camY)%64;
	
	for(int i = 0; i<((canvasWidth-1)/128)+1; i++)
	for(int j = 0; j<(canvasHeight/64)+2; j++)
		showImage(grassImg,0,0,192,64,(192*i)-x%64,(64*j)-y%64);	
		
	showImage(fieldImg,camX,camY,canvasWidth,canvasHeight,0,0);
		
	/*scr.setClip(0,0,canvasWidth,canvasHeight);
	scr.setColor(255,255,255);
	
	scr.translate(-camX,-camY);
	
	scr.drawRect((XScale*0),(YScale*0),(XScale*90),(YScale*120));
	
	
	// Field center
	if(YScale*51 - camY < canvasHeight && YScale*69 - camY > 0){
		scr.drawLine((XScale*0),(YScale*60),(XScale*90),(YScale*60));
		scr.drawArc((XScale*36),(YScale*51),(XScale*18),(YScale*18),0,360);
		scr.fillRect((XScale*45)-1,(YScale*60)-1,3,3);
		//System.out.println("Center");
	}
	
	// North Area
	if(YScale*23 - camY > 0){
		scr.drawRect((XScale*34),(YScale*0),(XScale*22),(YScale*5));
		scr.drawRect((XScale*25),(YScale*0),(XScale*40),(YScale*20));
	
		scr.fillRect((XScale*45),(YScale*15),2,2);
	
		scr.drawArc((XScale*31),(YScale*-4)+1,(XScale*27),(YScale*27),229,80);
		
		// Corners
		scr.drawLine((XScale*0),(YScale*2),(XScale*2),(YScale*0));
		scr.drawLine((XScale*88),(YScale*0),(XScale*90),(YScale*2));
		//System.out.println("North");
	}
	
	// South Area
	if(YScale*97 - camY < canvasHeight){
		scr.drawRect((XScale*36),(YScale*115),(XScale*18),(YScale*5));
		scr.drawRect((XScale*25),(YScale*100),(XScale*40),(YScale*20));
	
		scr.fillRect((XScale*45),(YScale*105),2,2);
	
		scr.drawArc((XScale*31),(YScale*97)-1,(XScale*27),(YScale*27),49,80);
		
		// Corners
		scr.drawLine((XScale*0),(YScale*118),(XScale*2),(YScale*120));
		scr.drawLine((XScale*88),(YScale*120),(XScale*90),(YScale*118));
		//System.out.println("South");
	}
		
		
	scr.translate(camX,camY);*/
					
}

void showBall(ball b)
{
	int fr=0;
		
	fr=Math.abs((( b.direction==2 || b.direction==6 ? b.sx : b.sy) * b.timing)/60)%3;
	
	scr.setClip(0,0,canvasWidth,canvasHeight);	
	scr.setColor(80,108,45);
	scr.fillArc(b.x*3-camX-3,b.y*3-camY-3,6,6,0,360);
	showImage(ballImg,8*fr,8*b.direction,8,8,b.x*3-camX-4,b.y*3-camY-4-(b.h>>9));
}

// [Frame][Direction]

/*public static final int shirts[][][]={
		{{5,8,4,4,8,9,4,4},{6,7,4,4,8,9,4,4}},
		{{5,8,4,4,9,8,4,4},{6,7,4,4,8,9,4,4}},
		{{6,8,4,4,9,7,4,4},{6,7,3,4,8,9,4,4}},
		{{6,6,5,4,8,7,5,4},{6,6,4,4,8,8,4,5}},
		{{6,6,6,3,7,9,4,1},{8,4,5,4,7,6,4,3}},
		{{5,8,4,4,8,10,4,4},{6,7,4,4,7,10,4,4}}
	};*/
	
public static final int heads[][][]={
	{{0,0},{0,0}},
	{{0,0},{0,0}},
	{{0,0},{0,0}},
	{{0,0},{0,0}},
	{{0,-5},{3,-5}},
	{{0,0},{0,0}}
	
};

void showPlayerShadow(unit sp)
{
	int x=3*sp.x-camX, y=3*sp.y-camY;
	scr.setClip(0,0,canvasWidth,canvasHeight);
	scr.setColor(80,108,45);
	scr.fillArc(x-8,y-7,16,14,0,360);
}

void showPlayer(int team, unit sp, short head, boolean black, int set)
{
	int x=3*sp.x-camX, y=3*sp.y-camY;
	int x2=x-14, y2=y-14, x3, y3, type=0;
	int bx, by, sx, sy, fr=1, dir=sp.direction, hx=0, hy=0;
	
	
	
	scr.setClip(0,0,canvasWidth,canvasHeight);
	
	if(sp == ga.humanSoccerPlayer)			
	{
			scr.setColor(255,255,0);
			scr.drawArc(x2,y2,27,27,0,360);	
	}
		
	if(ga.humanSoccerPlayer != null)
	if(ga.humanSoccerPlayer.state == soccerPlayer.OWNBALL)
	if(sp == ga.humanSoccerPlayer.pass)
		if(x2>canvasWidth || x2<-8 || y2>canvasHeight || y2<-8)
		{
			x3 = x2; y3 = y2;
			if(x2<0) {x3 = 2; type = 1;}
			if(x2>canvasWidth) {x3 = canvasWidth - 10; type = 3;}
			if(y2<0) {y3 = 2; type = 2;}
			if(y2>canvasHeight) {y3 = canvasHeight - 10; type = 0;}
			
			showImage(numbersImg,9*type,12,9,10,x3,y3);
		}else{
			//scr.setColor(255,255,0);
			//scr.fillRect(x2+4,y2-6,10,4);	
			showImage(numbersImg,0,12,9,10,x2+4,y2-10);
		}		
	
	if(x2>canvasWidth || y2>canvasHeight || x2<-18 || y2<-18) return;
	
	if(sp.state == soccerPlayer.STEAL) fr = 3;
	else if(sp.state == goalKeeper.PALOMITA) fr = 4;
	else if(sp.state == soccerPlayer.WAIT){
		if(sp.waitReason == soccerPlayer.W_STEAL) fr = 4;
		else if(sp.waitReason == soccerPlayer.W_STEALFAILED) fr = 3;
		else if(sp.waitReason == soccerPlayer.W_OUT){fr = 5; showBall(ga.ball);}
		else fr = 5;
	}else if(sp.sx == 0 && sp.sy == 0 ) fr = 1;
	else switch((sp.timing/2)%4)
	{
		case 0 : fr=0; break;
		case 3 :
		case 1 : fr=1; break;
		case 2 : fr=2; break;			
	}
		
	//dir=7; fr=3;
				
	//scr.setClip(0,0,canvasWidth,canvasHeight);
	//for(int i=0; i<shirts[fr][dir%2].length; i+=4){
		
		/*if((i/4)%2==0) scr.setColor(255,0,0);
		else */
		//scr.setColor(r,g,b);
		
		switch(dir){
			default: /*bx = shirts[fr][0][i];
					 by = shirts[fr][0][i+1];
					 sx = shirts[fr][0][i+2];
					 sy = shirts[fr][0][i+3];*/
					 hx = heads[fr][0][0];
					 hy = heads[fr][0][1];
					 break;
					 
			case 1 : /*bx = shirts[fr][1][i];
					 by = shirts[fr][1][i+1];
					 sx = shirts[fr][1][i+2];
					 sy = shirts[fr][1][i+3];*/
					 hx = heads[fr][1][0];
					 hy = heads[fr][1][1];
					 break;					 

			case 2 : /*bx = 18-shirts[fr][0][i+1]-shirts[fr][0][i+3];
					 by = shirts[fr][0][i];
					 sx = shirts[fr][0][i+3];
					 sy = shirts[fr][0][i+2];*/
					 hx = -heads[fr][0][1];
					 hy = heads[fr][0][0];
					 break;					 
					 
			case 3 : /*bx = 18-shirts[fr][1][i+1]-shirts[fr][0][i+3];
					 by = shirts[fr][1][i];
					 sx = shirts[fr][1][i+3];
					 sy = shirts[fr][1][i+2];*/
					 hx = -heads[fr][1][1];
					 hy = heads[fr][1][0];
					 break;					 					 
					 
			case 4 : /*bx = 18-shirts[fr][0][i]-shirts[fr][0][i+2];
					 by = 18-shirts[fr][0][i+1]-shirts[fr][0][i+3];
					 sx = shirts[fr][0][i+2];
					 sy = shirts[fr][0][i+3];*/
					 hx = -heads[fr][0][0];
					 hy = -heads[fr][0][1];
					 break;					 

			case 5 : /*bx = 18-shirts[fr][1][i]-shirts[fr][1][i+2];
					 by = 18-shirts[fr][1][i+1]-shirts[fr][1][i+3];
					 sx = shirts[fr][1][i+2];
					 sy = shirts[fr][1][i+3];*/
					 hx = -heads[fr][1][0];
					 hy = -heads[fr][1][1];
					 break;			
					 
			case 6 : /*bx = shirts[fr][0][i+1];
					 by = 18-shirts[fr][0][i]-shirts[fr][0][i+2];
					 sx = shirts[fr][0][i+3];
					 sy = shirts[fr][0][i+2];*/
					 hx = heads[fr][0][1];
					 hy = -heads[fr][0][0];
					 break;					 		 					 
					 
			case 7 : /*bx = shirts[fr][1][i+1];
					 by = 18-shirts[fr][1][i]-shirts[fr][1][i+2];
					 sx = shirts[fr][1][i+3];
					 sy = shirts[fr][1][i+2];*/
					 hx = heads[fr][1][1];
					 hy = -heads[fr][1][0];
					 break;					 		 					 					 
			
		}
						
	//}
	
		
	//showImage(shirtImg[team],(dir%2)*27,fr*27 + 162*set,27,27,x-14,y-14,(dir%8)/2);
			
	PutSprite(shirtImg,  x-14, y-14,  27, (dir%2) + (team*2) + (32*fr) + (192*set), 2, shirtCoo, (dir%8)/2);		
	//showImage(black ? blackImg : playerImg,(dir%2)*27,fr*27,27,27,x-14,y-14,(dir%8)/2);
	int aux[]={0,5,3,4};
	ImageSET(black ? blackImg : playerImg, (dir%2)*27,fr*27,27,27,x-14,y-14,aux[(dir%8)/2]);
	showImage(headImg,8*head,0,8,7,x-4+((3*hx)/2),y-3+((3*hy)/2));
	
	if(sp.state == soccerPlayer.WAIT && sp.waitReason == soccerPlayer.W_OUT) showBall(ga.ball);
		
	if(sp.state == soccerPlayer.STEAL)
		if(ga.RND(5)==0) 
			fieldErode(x+camX,y+camY,6,10);
	
	if(sp.state == goalKeeper.PALOMITA)
		fieldErode(x+camX,y+camY,4,10);
	
}

void fieldErode(int x, int y, int radius, int press)
{
	int offx, offy;
		
	fieldGr.setClip(0,0,fieldImg.getWidth(),fieldImg.getHeight());
	
	
	for(int i = 0; i<press; i++){
		
		int c = ga.RND(4)+1;
		
		fieldGr.setColor(125-10*c,73-5*c,0);
	
		offx = ga.RND(radius*2);
		offy = ga.RND(radius*2);
		fieldGr.fillRect(x - radius + offx,y - radius + offy,1,1);
	}
	
}

void showImage(Image im, int bx, int by, int sx, int sy, int px, int py, int rot)
{
	int x1, x2, y1, y2;
	
	DirectGraphics dg = DirectUtils.getDirectGraphics(scr);

	x1=px; y1=py; x2=px+sx; y2=py+sy;
		
	if(x1<0) x1=0; 
	if(x2>canvasWidth) x2=canvasWidth; 

	if(y1<0) y1=0; 
	if(y2>canvasHeight) y2=canvasHeight; 
		
	scr.setClip(x1,y1,x2-x1,y2-y1);
	scr.clipRect(x1,y1,x2-x1,y2-y1);
		
	switch(rot){	
		case 0 : dg.drawImage(im,px-bx,py-by,20,0); break;
		case 1 : dg.drawImage(im,px-(im.getHeight()-by-sy),py-bx,20,DirectGraphics.ROTATE_270); break;
		case 2 : dg.drawImage(im,px+bx-im.getWidth()+sx,py+by-im.getHeight()+sy,20,DirectGraphics.ROTATE_180); break;
		case 3 : dg.drawImage(im,px-by,py-bx-sx,20,DirectGraphics.ROTATE_90); break;		
	}
	
	
}

void showFlag(int id, int x, int y)
{
	if(id>=0)
	showImage(flagsImg,17*(id%4),11*(id/4),12,9,x,y);	
	else{
		scr.setClip(0,0,canvasWidth,canvasHeight);
		scr.setColor(0);
		scr.fillRect(x,y,12,9);
	}
	
}

void showBigFlag(int id, int x, int y)
{
	if(id>=0)
	showImage(bigFlagsImg,26*(id%4),17*(id/4),18,14,x,y);	
	else{
		scr.setClip(0,0,canvasWidth,canvasHeight);
		scr.setColor(0);
		scr.fillRect(x,y,18,14);
	}
	
}


void showTournament(tournament t, boolean fixScroll)
{
	int x, y, inc;
	
	
	for(int j= 0; j<t.levels; j++)
	{
		inc = 15;
		
		for(int k=0; k<(t.levels-1-j); k++)
			inc = inc*2;
		
		for(int i = 0; i<t.evol[j].length; i++)
		{
						
			x = ga.tourScrollX + (canvasWidth/2) -  (inc * (t.evol[j].length/2)) + inc/2 + i*inc;
			if(j == 0) x = ga.tourScrollX + (canvasWidth/2);
			y = /*ga.tourScrollY + */(canvasHeight/2) - 35 + 20*j;
			
					
			if(t.evol[j][i] == ga.myTeam) {
				scr.setClip(0,0,canvasWidth,canvasHeight); 
				scr.setColor(255,255,255); 
				scr.fillRect(x-1,y-1,14,11);				
				
			}
			showFlag(t.evol[j][i],x,y);
			
			if(j<t.levels-1)
			{
				scr.setClip(0,0,canvasWidth,canvasHeight);
				
				if(j>=t.current) scr.setColor(255,255,0);
				else scr.setColor(0,0,0);
				scr.drawLine(x+6,y+9,x+6,y+13);					
				
				if(t.evol[j][i] == t.evol[j+1][i*2] && j>=t.current) scr.setColor(255,255,0);
				else scr.setColor(0,0,0);
				scr.drawLine(x+6-inc/4,y+14,x+5,y+14);									
				scr.drawLine(x+6-inc/4,y+14,x+6-inc/4,y+20);					
				
				if(t.evol[j][i] == t.evol[j+1][i*2 + 1]  && j>=t.current) scr.setColor(255,255,0);
				else scr.setColor(0,0,0);
				scr.drawLine(x+7,y+14,x+6+inc/4,y+14);		
				scr.drawLine(x+6+inc/4,y+14,x+6+inc/4,y+20);					
			}
				
		}		
	}
	
	scr.setClip(0,0,canvasWidth,canvasHeight);
	if (ga.tourScrollX<(130 - canvasWidth/2)) textDraw("<", 5, canvasHeight/4, 0xFFFFFF, TEXT_BOLD);								
	if (ga.tourScrollX>-(130 - canvasWidth/2)) textDraw(">", canvasWidth-10, canvasHeight/4, 0xFFFFFF, TEXT_BOLD);								
}

boolean ledBmpTestPixel(int offset)
{
	byte pos;
	if((offset/8)>=ledBmp.length) pos = 0;
	else pos = ledBmp[offset/8];
	
	return ((pos>>(7-(offset%8))) & 0x01) != 0;
}

void showLed(int fr, boolean inv)
{
	int x=(canvasWidth-135)/2, y=(canvasHeight-90)/2, offset = 45*30*fr;
	
		
	showImage(borderHImg,0,0,176,21,x-22,y-21);
	showImage(borderHImg,0,21,176,23,x-22,y+90);
	showImage(borderVImg,0,0,22,90,x-22,y);
	showImage(borderVImg,22,0,22,90,x+135,y);
				
	scr.setClip(0,0,canvasWidth,canvasHeight);		
	scr.setColor(0,0,0);	
	scr.fillRect(x,y,135,90);		
	scr.setColor(171,160,0);		
	
	if(fr<0) return;
		
	for(int j=0; j<30; j++)
	for(int i=0; i<45; i++)
	{
		if(inv ? !ledBmpTestPixel(offset) : ledBmpTestPixel(offset))
			scr.fillRect(x+3*i,y+3*j,2,2);		
		offset++;
	}	
}

void showPattern()
{
	for(int j = 0; j<canvasHeight; j+=64)
	for(int i = 0; i<canvasWidth; i+=64)
		showImage(patternImg,0,0,64,64,i,j);
}


// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//
};