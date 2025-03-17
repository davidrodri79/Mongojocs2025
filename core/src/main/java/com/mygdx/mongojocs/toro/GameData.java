package com.mygdx.mongojocs.toro;// GameData	- Toro7650	176x208

/*
	Diferencies de canvas amb el 7210 80x48
	
*/


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.io.IOException;
import java.io.InputStream;

public class GameData
{
	GameCanvas	GC;

	Image	imgMicrojocs;
//	Image 	screenBuffer;
	Graphics gBuffer;

	int	w, h;
	
	boolean	bDataLoaded=false;

	byte	corToro [] = new byte [576];
	byte	corProta [] = new byte [576];
	Image imgIntro, imgProta, imgPng, imgToro; //, imgToro2;
	
	Image	imgProtaGuanya, imgProtaPerd;
	Image	imgPlaza;
	
	byte	mapArray[];
		
	//Scroll		scrollMap= new Scroll ();

	ToroObj		tabTorosOrd [] = new ToroObj[10];
	int GusanoP []= new int [10];

/*
	void	readData (String file, byte [] buffer)
	{
		InputStream is;
		is = getClass().getResourceAsStream(file);
		try {
			is.read(buffer, 0, buffer.length);
			is.close();
		}catch(Exception exception) {}
	}
*/

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

void LoadFile(byte[] buffer, String Nombre)
{
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	for(int i = 0; i < buffer.length; i++)
		buffer[i] = bytes[i];
	/*System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
	is.read(buffer, 0, buffer.length);
	is.close();
	}catch(Exception exception) {}
	System.gc();*/
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	static void readData(InputStream is,byte[] buffer,int size)
	{
		System.gc ();
		try{
		is.read(buffer, 0,size);
		is.close();
		}catch(Exception exception) {} //{ System.err.println ("readData error");}	
		System.gc ();
	}
			
	void 	deleteLevel ()
	{
		System.gc ();
	}
	
	void	loadLevel (int level)
	{

	}
	
	void 	loadGameData ()
	{
		if (bDataLoaded)	return;
		
		imgIntro = null;
		//imgMicrojocs = null;
		System.gc ();

	        //try
	        {
			imgToro		= Image.createImage ("/ctoro.png"); ///toro.png");
		System.gc ();
			imgPlaza	= Image.createImage ("/plazaCOLOR.png");
		System.gc ();
//			imgToro2	= Image.createImage ("/toro2.png");
	        }
	        //catch (IOException e)	{System.err.println ("ioerror2");}

		System.gc ();
		// Altres
	        //try
	        {
			imgProta	= Image.createImage ("/cprota.png"); //personatge.png");
		System.gc ();
			imgPng		= Image.createImage ("/png.png");
		System.gc ();
			
			imgProtaGuanya	= Image.createImage ("/personatge-guanya.png");
		System.gc ();
			imgProtaPerd	= Image.createImage ("/personatge-pillat-pel-toro.png");
		System.gc ();
			
		        bDataLoaded = true;
	        }
	        //catch (IOException e)	{System.err.println ("ioerror");}
	        

		LoadFile (corProta, "/prota.cor");		
		LoadFile (corToro, "/toro.cor");		


		InputStream is;
		
		byte mapTemp [] = new byte [24*29];
		is = getClass().getResourceAsStream("/mapahalf.map");
		GameData.readData (is, mapTemp, 48*29/2);
		/*
		mapArray = new byte [48*29*2];
		
		int	sp = 0;
		for (int y = 0; y < 29; y++)
		{
			int yp = y * 48;
			
			for (int x = 0; x < 24; x++)
			{
				mapArray [x+yp]		= mapTemp [sp];
				mapArray [48-1-x+yp]	= mapTemp [sp];
				sp ++;
			}
		}
		*/
		is = null;
	
		System.gc ();
		
		//scrollMap.ScrollINI (w, h);
		//scrollMap.ScrollSET (mapArray, 48, 29, imgTiles, 200/8);
	}
	
	
	GameData (GameCanvas gameCanvas, int wd, int ht)
	{
		GC = gameCanvas;
		w = wd;	h = ht;
		
		int	wb = ((w >> 3) + 1) << 3;
		int	hb = ((h >> 3) + 1) << 3;
		
		//screenBuffer = Image.createImage (wb,hb); //104,80);
	 	//gBuffer = screenBuffer.getGraphics();


		// Altres
	        //try
	        {
	       	imgMicrojocs = new Image();
	       	imgMicrojocs._createImage ("/menu.png");
			imgIntro = new Image();
			imgIntro._createImage ("/caratula.png");
	        }
	        //catch (IOException e)	{}//{System.err.println ("ioerror2");}

	}

/// PutSprite ////////////////////////////////////////////////////////////	
public void PutSpriteSinTrocear(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY);
}


public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
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

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY);
	}
}


public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
	gBuffer.setClip(0, 0, w, h);
	gBuffer.clipRect(DestX, DestY, SizeX, SizeY);
	gBuffer.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}

//////////////////////////////////////////////////////////////////////////	


	void	drawFrameRect ()
	{
		gBuffer.setColor(255,225,205);
	 	gBuffer.fillRect(5,5+40,w-10,h-10-80);
	 	gBuffer.setColor(0,0,0);
	 	gBuffer.drawRect(4,4+40,w-9,h-9-80);
	 	
	}
	
	
	void	drawIntro (int msTime)
	{
		if (msTime < 12500)
		{
			gBuffer.setClip (0, 0, w, h);
			gBuffer.setColor (0, 0, 0);
			gBuffer.fillRect (0, 0, w, h);
			gBuffer.drawImage (imgIntro, 0, 32, 20);
		}
		else	GC.stateCanvas = GC.STC_MENU;
	}
	

	void	drawStartMission ()
	{
		drawFrameRect ();
		gBuffer.setColor (0, 0, 0);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
		gBuffer.drawString(TextResources.sMission+((int)(GC.numLevel+1)),38+25,38+40,20);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	}


	String	getTimeString (int timeMission)
	{
		String	sMin, sSec;
		int	iMin, iSec;
		
		iMin = timeMission / 60;
		iSec = timeMission % 60;
		if (iMin > 9)	sMin = ""+(iMin);
		else		sMin = "0"+(iMin);
		
		if (iSec > 9)	sSec = ""+(iSec);
		else		sSec = "0"+(iSec);
		
		return 	sMin+":"+sSec;
	}


	void	drawEndMission (int timeMission)
	{
		drawFrameRect ();
		gBuffer.setColor (0, 0, 0);
		gBuffer.drawString(TextResources.sMission+((int)(GC.numLevel))+TextResources.sCompleted,8,26+40,20);

		//gBuffer.drawString(TextResources.sYourTime+getTimeString (timeMission),15,36,20);
	}
	/*
	void	drawBestTime (int newtime, int oldtime)
	{
		drawFrameRect ();
		gBuffer.setColor (0, 0, 0);
		gBuffer.drawString(TextResources.sBestTime,19,16,20);
				
		gBuffer.drawString(TextResources.sYourTime+getTimeString (newtime),12,30,20);
		gBuffer.drawString(TextResources.sLastTime+getTimeString (oldtime),12,45,20);
	}
	*/	
	void	drawEndGame (int spos)
	{
		drawFrameRect ();
		// scroller
	 	gBuffer.setClip(5,5,86,55);
	 	gBuffer.setClip(5,5+40,w-10,h-10-80);
	 	int a=30;int b=10;
		gBuffer.drawString(TextResources.sScroll1,12+a,60-spos+40+b*0,20);
		gBuffer.drawString(TextResources.sScroll2,12+a,70-spos+40+b*1,20);
		gBuffer.drawString(TextResources.sScroll3,12+a,80-spos+40+b*2,20);

		gBuffer.drawString(TextResources.sScroll4,12+a,117-spos+40+b*3,20);
		gBuffer.drawString(TextResources.sScroll5,12+a,127-spos+40+b*4,20);

		gBuffer.drawString(TextResources.sScroll6,12+a,147-spos+40+b*5,20);
		gBuffer.drawString(TextResources.sScroll7,12+a,157-spos+40+b*6,20);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/// drawGame
	//
	void	drawGame ()
	{	
		gBuffer.setColor (255, 255, 255);		
		gBuffer.setClip (0, 0, w, h);

	 	gBuffer.fillRect(0,0,w,h);
		/*
		if (imgBack2!=null && GC.bitTwoSwitch==1)
			gBuffer.drawImage (imgBack2, -GC.mapXPos, -GC.mapYPos, 20);
		else
			gBuffer.drawImage (imgBack, -GC.mapXPos, -GC.mapYPos, 20);
		*/
		
		/*
		gBuffer.drawImage (imgMap00, -GC.mapXPos, -GC.mapYPos, 20);
		gBuffer.drawImage (imgMap10, 200-GC.mapXPos, -GC.mapYPos, 20);
		gBuffer.drawImage (imgMap01, -GC.mapXPos, 128-GC.mapYPos, 20);
		gBuffer.drawImage (imgMap11, 200-GC.mapXPos, 128-GC.mapYPos, 20);
		*/
		
		//scrollMap.ScrollRUN_Max (GC.mapXPos, GC.mapYPos);
		//scrollMap.ScrollIMP (gBuffer);		

		gBuffer.drawImage (imgPlaza, -GC.mapXPos, -GC.mapYPos, 20);
		
		drawDoor (0);

		// dibuixa sprites      /////////////////////////////////////////////////////////

		// roses
		for (int i = 0; i < GC.numRoses; i++)
		{
			if (GC.tabRoses [(i<<1)] > 0)
			{
				drawRose (GC.tabRoses [(i<<1)]-GC.mapXPos, GC.tabRoses [(i<<1)+1]-GC.mapYPos, i & 0x3);
			}
		}	
		
		if (GC.protaState == 0)
		{
			drawProta (GC.protaXPos-GC.mapXPos,GC.protaYPos-GC.mapYPos, GC.protaDir, (GC.frameCounter % 4)+1);
		}
		else if (GC.protaState == 2)
		{
			drawProtaPerd (GC.protaXPos-GC.mapXPos,GC.protaYPos-GC.mapYPos, GC.countProta);
		}
		else if (GC.protaState == 3)
		{
			drawProtaGuanya (GC.protaXPos-GC.mapXPos,GC.protaYPos-GC.mapYPos, GC.countProta);
		}
		
		
	
		for (int t=0; t < GC.numToros; t++)		GusanoP [t] = t;

		int Conta=GC.numToros-1;
		for (int t=0 ; t < GC.numToros ; t++)
		{
			for (int i=0 ; i < Conta ; i++)
			{
				if (GC.tabToros[GusanoP[i]].posY > GC.tabToros[GusanoP[i+1]].posY)
				{
				int dato	= GusanoP[i+1];
				GusanoP[i+1] 	= GusanoP[i];
				GusanoP[i] 	= dato;
				}
			}
			Conta--;
		}


		for (int i = 0; i < GC.numToros; i++)
		{
			ToroObj	to = GC.tabToros [GusanoP [i]]; //tabTorosOrd [i];
	
			drawToro (to.posX-GC.mapXPos, to.posY-GC.mapYPos, to.dir, (GC.frameCounter+ 1) % 3);
		}
		/*
		for (int i = 0; i < GC.numToros; i++)
		{
			ToroObj	to = GC.tabToros [i];
	
			drawToro (to.posX-GC.mapXPos, to.posY-GC.mapYPos, to.dir, (GC.frameCounter+ 1) % 3);		
		}
		*/
		
		// dibuixa barres ///////////////////////////////////////////////////////////////
		
		/*
		for (int y = GC.protaDamage*3+5; y < 120; y+=3)
		{
			gBuffer.setClip (0,0,w,h);
			gBuffer.setColor (180,0,0);
			gBuffer.drawLine (122, y, 124, y);
			gBuffer.drawLine (122, y+1, 124, y+1);
			//gBuffer.setColor (255,255,255);
			//gBuffer.drawLine (93, y+1, 94, y+1);
		}
		*/
		
		
		gBuffer.setClip (0,0,w,h);
		gBuffer.setColor (180,0,0);
		gBuffer.fillRect (w-4,21+3*(GC.protaDamage*2+4), 2, 3*(60-(GC.protaDamage*2+4)));
				
		gBuffer.setClip (4, h-15, 9,11);
		gBuffer.drawImage (imgPng, 5 -41, h-15 -40, 20);
		
		if (GC.numRosesCollected < 10)
		{
			drawNumber (14, h-14, GC.numRosesCollected);
		}
		else
		{
			drawNumber (14, h-14, GC.numRosesCollected/10);
			drawNumber (14+6, h-14, GC.numRosesCollected%10);
		}
		

		int	timeToCount = ((int)GC.timeElapsed);
		
		if (timeToCount > 599)	timeToCount = 599;
		
		int	min = timeToCount / 60;
		int	sec = timeToCount % 60;
		drawNumber (w-14, h-14, sec % 10);
		drawNumber (w-14-6, h-14, (sec/10)%10);
		drawNumber (w-14-5-5, h-14, 10);	
		drawNumber (w-14-6-5-5, h-14, min%10);
	}
	
	// S'obren les portes i surten els toros :P
	void	drawIntroDay ()
	{
		/*
		gBuffer.drawImage (imgMap00, -GC.mapXPos, -GC.mapYPos, 20);
		gBuffer.drawImage (imgMap10, 200-GC.mapXPos, -GC.mapYPos, 20);
		gBuffer.drawImage (imgMap01, -GC.mapXPos, 128-GC.mapYPos, 20);
		gBuffer.drawImage (imgMap11, 200-GC.mapXPos, 128-GC.mapYPos, 20);
		
		*/
		//scrollMap.ScrollIMP (gBuffer);		
		//scrollMap.ScrollRUN_Max (GC.mapXPos, GC.mapYPos);

		
		drawDoor (GC.frameCounter % 3);

		// dibuixa sprites      /////////////////////////////////////////////////////////		
		
		if (GC.protaState == 0)
		{
			drawProta (GC.protaXPos-GC.mapXPos,GC.protaYPos-GC.mapYPos, GC.protaDir, (GC.frameCounter % 4));
		}
					
		//drawToro (GC.toro.posX-GC.mapXPos,GC.toro.posY-GC.mapYPos, GC.toro.dir, (GC.frameCounter % 3));
		
		for (int i = 0; i < GC.numToros; i++)
		{
			ToroObj	to = GC.tabToros [i];
	
			drawToro (to.posX-GC.mapXPos, to.posY-GC.mapYPos, to.dir, (GC.frameCounter+ 1) % 3);		
		}
	}
	
	
	void	drawNumber (int xpos, int ypos, int n)
	{
		gBuffer.setClip (xpos, ypos, 6,8);
		gBuffer.drawImage (imgPng, xpos -39-6*n, ypos -51, 20);		
	}
	
	
	void	drawProta (int xpos, int ypos, int dir, int n)
	{
		gBuffer.setClip (xpos, ypos, 16, 20);

		dir &= 0x7;
		n &= 0x3;

//		DirectGraphics dg = DirectUtils.getDirectGraphics(gBuffer);

//		dg.drawImage (imgProta, xpos, ypos, 20, 0x4000 | 0x2000);
//		PutSprite (imgProta, xpos, ypos, dir+n*8, corProta, 3);
		PutSprite (imgProta, xpos, ypos, dir+(n*8), corProta, 3);

//		gBuffer.drawImage (imgProta, xpos-dir*16, ypos-n*20+20, 20);
	}
	
	void	drawProtaGuanya (int xpos, int ypos, int n)
	{
		int	pc=0, pw=0;
		int	rc, rw;
		
		if (n > 4)
		{
			n = (n-1) % 4;
			n ++;
		}
		switch (n)
		{
			case 0:
				pc = 6;
				pw = 6;
				break;
			case 1:
				pc = 21;
				pw = 8;
				break;
			case 2:
				pc = 37;
				pw = 7;
				break;
			case 3:
				pc = 52;
				pw = 8;
				break;
			case 4:
				pc = 66;
				pw = 7;
				break;
		}
		
		rc = pc - pw + 1;
		rw = 2*pw - 1;
		if (n==4) rw ++;
		gBuffer.setClip (xpos, ypos-4, rw, 24);
		gBuffer.drawImage (imgProtaGuanya, xpos-rc, ypos-4, 20);
	}
	
	void	drawProtaPerd (int xpos, int ypos, int n)
	{
		if (n > 4)	n = 4;
		gBuffer.setClip (xpos, ypos-1, 20, 21);
		gBuffer.drawImage (imgProtaPerd, xpos-n*20, ypos-1, 20);			
	}
	
	
	void	drawToro (int xpos, int ypos, int dir, int n)
	{
/*		if (dir <= 5)
		{
			gBuffer.setClip (xpos, ypos, 32, 32);
			gBuffer.drawImage (imgToro, xpos, ypos, 20);
			//gBuffer.drawImage (imgToro, xpos-dir*32, ypos-n*32, 20);
		}
		else
		{
			gBuffer.setClip (xpos, ypos, 32, 32);
			gBuffer.drawImage (imgToro2, xpos, ypos, 20);
			//gBuffer.drawImage (imgToro2, xpos-(dir-6)*32, ypos-n*32, 20);
		}
*/
		gBuffer.setClip (xpos, ypos, 32, 32);
		PutSprite (imgToro, xpos, ypos, dir+n*8, corToro, 4 );
	}
	
	void	drawRose (int xpos, int ypos, int f)
	{
		int	yp=0;
		gBuffer.setClip (xpos, ypos, 14, 10);
		switch (f)
		{
			case 0:	yp = 0; break;
			case 1: yp = 9; break;
			case 2: yp =19; break;
			case 3: yp =29; break;			
		}
		gBuffer.drawImage (imgPng, xpos-38, ypos-yp, 20);		
	}
	
	void	drawOle (int f)
	{
		gBuffer.setClip (17, 10-21, 100, 12);
		switch (f)
		{
			case 0:
				///gBuffer.drawImage (imgPng, 17, 10 , 20);
				break;
			case 1:
				///gBuffer.drawImage (imgPng, 17, 10 -12, 20);
				break;
			case 2:
				///gBuffer.drawImage (imgPng, 17, 10 -24, 20);
				break;
		}
	}
	

	// at (184, 60)
	void	drawDoor (int f)
	{
		int xpos = 182-GC.mapXPos, ypos = 60-GC.mapYPos;
		switch (f)
		{
			case 0:
				gBuffer.setClip (xpos, ypos-21, 37, 21);
				///gBuffer.drawImage (imgPng, xpos, ypos-21, 20);
				break;
			case 1:
				gBuffer.setClip (xpos, ypos-19, 37, 19);
				///gBuffer.drawImage (imgPng, xpos, ypos-19 -21, 20);
				break;
			case 2:
				gBuffer.setClip (xpos, ypos-18, 37, 18);
				///gBuffer.drawImage (imgPng, xpos, ypos-18 -40, 20);
				break;
		}
	}		
	

/*
	private void drawTile (Graphics g, byte tilesArray[], int mapArray, int px, int py)
	{
		int	mapi = mapArray - 3;
		if (mapi < 0)
		{
			if (mapi == -1)
			for (int y = 0; y < 8; y++)
				g.drawLine (px, py+y, px+7, py+y);	
			else if (mapi == -2)
			{
				g.setColor (255,255,255);
				for (int y = 0; y < 8; y++)
					g.drawLine (px, py+y, px+7, py+y);		
			}				
			g.setColor (0,0,0);
		}
		else
		{		
			for (int y = 0; y < 8; y++)
			{
				int	pw = tilesArray [mapi*8+y];
				if (pw == -1)
					g.drawLine (px, py+y, px+7, py+y);
				else
				for (int x = 0; x < 8; x++)
				{
					if ((pw & 0x80) != 0)
						g.drawLine (px+x, py+y, px+x, py+y);
					
					pw <<= 1;
				}
			}
		}
	}
*/
/*	private void drawTile (Graphics g, byte tilesArray[], int mapArray, int px, int py)
	{
		DirectGraphics dg = DirectUtils.getDirectGraphics (g);
		
		int	mapi = mapArray - 3;
		if (mapi < 0)
		{
			if (mapi == -1)
			{
				g.setColor (0,0,0);		
				g.fillRect (px, py, px+7, py+7);
			}
			else if (mapi == -2)
			{
				g.setColor (255,255,255);
				g.fillRect (px, py, px+7, py+7);
			}				
			g.setColor (0,0,0);
		}
		else
		{
		dg.drawPixels (tilesArray, (byte[])null,
				mapi*8*8, 8,
				px, py,
				8, 8,
				0, DirectGraphics.TYPE_BYTE_1_GRAY);
		}
	}
*/	
}
