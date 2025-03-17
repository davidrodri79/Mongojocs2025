package com.mygdx.mongojocs.rescue;// GameData	- rescue_bwn

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.io.InputStream;

public class GameData
{
//	TiledBackground tiledBackA, tiledBackB;
//	GraphicObjectManager GOM;
	
	Image imgLogo;
	Image imgIntro,imgMenu;

	public static final int NOKIA_BUGCLIP = 0;

	GameCanvas	GC;

	//
	Image	imgBar;
	Image 	screenBuffer;
	Graphics gBuffer;

	int	w, h;
	
	Image	imgHeliF0, imgHeliF1, imgHeliF2, imgHeliF3;
	
	Image	imgFoc1, imgFoc2, imgFoc3, imgSmallFoc, imgFiFoc, imgFum;
	Image	imgAigua;
	Image	imgShadow;	
	Image	imgShad0, imgShad1, imgShad2, imgShad3;
	Image	imgSprites;
	Image	imgGente;
	Image	imgJefe;
	
	// depenents del nivell
	Image	imgTendes;
	
	Image	imgTiles;

	byte	heliArray [];

	byte	tilesArray [];	
	byte 	mapaArray [], mapaArray2 [];

	Image	imgBack=null, imgBack2=null;


	static void readData(String is, byte[] buffer, int size)
	{
		/*try{
		is.read(buffer, 0,size);
		is.close();
		}catch(Exception exception) {} //{ System.err.println ("readData error");}	*/

		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+is.substring(1));
		byte[] bytes = file.readBytes();

		for(int i = 0; i < size; i++)
		{
			buffer[i] = bytes[i];
		}
	}
/*	
	int		numFiles;
	StringBuffer 	arrayFileNames[];
	int		arrayFilePos[];
	boolean	bIndexLoaded = false;
	void readIndex ()
	{
		bIndexLoaded = true;
		InputStream is = getClass().getResourceAsStream("/mp.dat");
		//DataInputStream di;// = new DataInputStream (is);
		
		try{
		numFiles = is.read ();
		
		arrayFileNames = new StringBuffer [numFiles];
		arrayFilePos = new int [numFiles];
		
		for (int i = 0; i < numFiles; i++)
		{
			arrayFileNames [i] = new StringBuffer ();
			char	c = (char)is.read ();
			while (c != 0)
			{
				arrayFileNames [i].append (c);
			
				c = (char)is.read ();	
			};
			int d0 = is.read ();
			int d1 = is.read ();
			int d2 = is.read ();
			int d3 = is.read ();
			
			arrayFilePos [i] =  d0 + (d1<<8) + (d2<<16) + (d3<<24);
			//System.out.println (arrayFileNames [i].toString() +"    "+arrayFilePos [i]);
		}
		
		//is.read(buffer, 0,size);
		is.close();
		}catch(Exception exception) {} //{ System.err.println ("readData error");}	
	}
	

	void readData(String st, byte[] buffer)
	{
		int filePos=0, fileLen=0;
		
		if (!bIndexLoaded)		readIndex ();
		
		InputStream is = getClass().getResourceAsStream("/mp.dat");
		//DataInputStream di;// = new DataInputStream (is);
		
		for (int i = 0; i < numFiles; i++)
		{
			if (st.equals (arrayFileNames [i].toString ()))
			{
				filePos = arrayFilePos [i];
				fileLen = arrayFilePos [i+1] - filePos;
				break;
			}	
		}
		
		if (filePos > 0)
		try{
		is.skip (filePos);
		is.read(buffer, 0, fileLen);
		is.close();
		}catch(Exception exception) {} //{ System.err.println ("readData error");}	
	}

	Image readImage(String st)
	{
		byte buffer[];
		Image img=null;
		int filePos=0, fileLen=0;
		
		if (!bIndexLoaded)		readIndex ();
		
		InputStream is = getClass().getResourceAsStream("/mp.dat");
		//DataInputStream di;// = new DataInputStream (is);
		
		for (int i = 0; i < numFiles; i++)
		{
			if (st.equals (arrayFileNames [i].toString ()))
			{
				filePos = arrayFilePos [i];
				fileLen = arrayFilePos [i+1] - filePos;
				break;
			}	
		}
		
		if (filePos > 0)
		{
			buffer = new byte [fileLen];
			
			try{
			is.skip (filePos);
			is.read(buffer, 0, fileLen);
			is.close();
			}catch(Exception exception) {} //{ System.err.println ("readData error");}	
		
			img = Image.createImage (buffer, 0, fileLen);
		}
		return img;
	}
*/

	String	msgToShow [];
	int	curMessagePage = 0;
	int	msgYPos = 22;
	boolean	bShowingMessage = false;
	long	timeStartShowMessage=0;
	
	void	ShowMessage (String arrayString [])
	{
		msgToShow = arrayString;
		bShowingMessage = true;
		timeStartShowMessage = System.currentTimeMillis ();
	}
	
	
	void	drawJefeBar ()
	{
		if (msgToShow!=null)
		if (bShowingMessage)
		{
			int	timeElapsed = (int)(System.currentTimeMillis () - timeStartShowMessage);
			
			curMessagePage = (timeElapsed-2000) / 2000;
			curMessagePage = curMessagePage < 0 ? 0 : curMessagePage;
			if (curMessagePage == 0)
			{
				if (msgYPos > 0)	msgYPos --;
			}
			else if (curMessagePage >= msgToShow.length)
			{
				if (msgYPos > 21)	bShowingMessage = false;
				else			msgYPos ++;
				curMessagePage	= msgToShow.length - 1;
				if (curMessagePage < 0)	curMessagePage = 0;
			}
			drawJefe (msgYPos);
			gBuffer.setColor (0,0,0);
			gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
			gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
			gBuffer.drawString (msgToShow [curMessagePage], 24, 2 - msgYPos, 20);		
		}
	}

	
	void 	deleteLevel ()
	{
		imgBack = null;
		imgBack2 = null;
		tilesArray = null;
		System.gc ();
	}

	boolean gettingGraphics = false;

	static Graphics gb;
	void	loadLevel (int level)
	{
		InputStream is;
		
		switch (level)
		{
			case 0:		
				//tilesArray = new byte [8*138*4];
				//is = getClass().getResourceAsStream("/tmapa1.bit");
				//GameData.readData (is, tilesArray, 8*138);
	
				imgTiles = null;
				System.gc ();
				
//				imgTiles = Image.createImage ("m1tiles.png");

			        try
			        {
					imgTiles = Image.createImage ("/m1tiles.png");
			        }
			        catch (Exception e)	{}//{System.err.println ("ioerror2");}
	
				mapaArray = new byte [30*30];
				//is = getClass().getResourceAsStream("/mapa1.map");
				GameData.readData ("/mapa1.map", mapaArray, 30*30);
				
				//readData ("mapa1.map", mapaArray);

				imgBack = Image.createImage (240, 240);
				gettingGraphics = true;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						gb = imgBack.getGraphics ();
						gb.setColor (255, 255, 255);
						gb.fillRect (0, 0, 240, 192);
						gb.setColor (0, 0, 0);

						for (int j = 0; j < 30; j++)
							for (int i = 0; i < 30; i++)
								drawTileCol (gb, imgTiles, mapaArray [i+j*30] < 0 ? 256+mapaArray [i+j*30] : mapaArray [i+j*30],
										i<<3, j<<3);
						gettingGraphics = false;
					}
				});
				while(gettingGraphics)
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				break;
			case 1:
				//tilesArray = new byte [8*122];
				//is = getClass().getResourceAsStream("/mapa2f1.gfx");
				//GameData.readData (is, tilesArray, 8*122);

				imgTiles = null;
				System.gc ();
//				imgTiles = Image.createImage ("m2tiles.png");
			        try
			        {
					imgTiles = Image.createImage ("/m2tiles.png");
			        }
			        catch (Exception e)	{}//{System.err.println ("ioerror2");}
				
				
				mapaArray = new byte [30*30];
				//is = getClass().getResourceAsStream("/mapa2f1.map");
				GameData.readData ("/mapa2f1.map", mapaArray, 30*30);

//				readData ("mapa2f1.map", mapaArray);

				mapaArray2 = new byte [30*30];
				//is = getClass().getResourceAsStream("/mapa2f2.map");
				GameData.readData ("/mapa2f2.map", mapaArray2, 30*30);

//				readData ("mapa2f2.map", mapaArray2);
		
				imgBack = Image.createImage (240, 240);
				// MONGOFIX
				gettingGraphics = true;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						gb = imgBack.getGraphics ();
						gb.setColor (255, 255, 255);
						gb.fillRect (0, 0, 240, 240);
						gb.setColor (0, 0, 0);

						for (int j = 0; j < 30; j++)
							for (int i = 0; i < 30; i++)
								drawTileCol (gb, imgTiles, mapaArray [i+j*30] < 0 ? 256+mapaArray [i+j*30] : mapaArray [i+j*30],
										i<<3, j<<3);
						gettingGraphics = false;
					}
				});
				while(gettingGraphics)
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				imgBack2 = Image.createImage (240, 240);
				// MONGOFIX
				gettingGraphics = true;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						gb = imgBack2.getGraphics ();
						gb.setColor (255, 255, 255);
						gb.fillRect (0, 0, 240, 240);
						gb.setColor (0, 0, 0);

						for (int j = 0; j < 30; j++)
							for (int i = 0; i < 30; i++)
								drawTileCol (gb, imgTiles, mapaArray2 [i+j*30] < 0 ? 256+mapaArray2 [i+j*30] : mapaArray2 [i+j*30],
										i<<3, j<<3);
						gettingGraphics = false;
					}
				});
				while(gettingGraphics)
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				break;
			case 2:
				//tilesArray = new byte [8*200];
				//is = getClass().getResourceAsStream("/mapa3f1.gfx");
				//GameData.readData (is, tilesArray, 8*167);
				
				imgTiles = null;
				System.gc ();
//				imgTiles = Image.createImage ("m3tiles.png");
			        try
			        {
					imgTiles = Image.createImage ("/m3tiles.png");
			        }
			        catch (Exception e)	{}//{System.err.println ("ioerror2");}

				
				mapaArray = new byte [30*30];
				//is = getClass().getResourceAsStream("/mapa3f1.map");
				GameData.readData ("/mapa3f1.map", mapaArray, 30*30);

//				readData ("mapa3f1.map", mapaArray);

				mapaArray2 = new byte [30*30];
				//is = getClass().getResourceAsStream("/mapa3f2.map");
				GameData.readData ("/mapa3f2.map", mapaArray2, 30*30);

//				readData ("mapa3f2.map", mapaArray2);
		
				imgBack = Image.createImage (240, 240);
				gettingGraphics = true;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						gb = imgBack.getGraphics ();
						gb.setColor (255, 255, 255);
						gb.fillRect (0, 0, 240, 240);
						gb.setColor (0, 0, 0);

						for (int j = 0; j < 30; j++)
							for (int i = 0; i < 30; i++)
								drawTileCol (gb, imgTiles, mapaArray [i+j*30] < 0 ? 256+mapaArray [i+j*30] : mapaArray [i+j*30],
										i<<3, j<<3);
						gettingGraphics = false;
					}
				});
				while(gettingGraphics)
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}


				imgBack2 = Image.createImage (240, 240);
				gettingGraphics = true;
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						gb = imgBack2.getGraphics ();
						gb.setColor (255, 255, 255);
						gb.fillRect (0, 0, 240, 240);
						gb.setColor (0, 0, 0);

						for (int j = 0; j < 30; j++)
							for (int i = 0; i < 30; i++)
								drawTileCol (gb, imgTiles, mapaArray2 [i+j*30] < 0 ? 256+mapaArray2 [i+j*30] : mapaArray2 [i+j*30],
										i<<3, j<<3);
						gettingGraphics = false;
					}
				});
				while(gettingGraphics)
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				break;
		}
	}
	
	GameData (GameCanvas gameCanvas, int wd, int ht)
	{
		GC = gameCanvas;
		w = wd;	h = ht;
		
		//readIndex ();
		
		int	wb = ((w >> 3) + 1) << 3;
		int	hb = ((h >> 3) + 1) << 3;
		
		screenBuffer = new Image();
		screenBuffer._createImage (wb,hb); //104,80);
	 	gBuffer = screenBuffer.getGraphics();

	        try
	        {
			imgLogo = new Image();
			imgLogo._createImage ("/logo.png");
			imgMenu = new Image();
			imgMenu._createImage ("/menu.png");
	        }
	        catch (Exception e)	{}//{System.err.println ("ioerror2");}

//		heliArray = new byte [3168];
//		is = getClass().getResourceAsStream("/h.bit");
//		GameData.readData (is, heliArray, 3168);
	}

	boolean		bGameDataLoaded=false;
	void	loadGameData ()
	{
		bGameDataLoaded = true;

	        try
	        {
			imgIntro = Image.createImage ("/intro7650.png"); 
	        }
	        catch (Exception e)	{System.err.println ("loadGameData1");}
	        try
	        {
			imgBar = Image.createImage("/menus.png"); //tile_2.png");
			imgHeliF0 = Image.createImage ("/hc0.png");
			imgHeliF1 = Image.createImage ("/hc1.png");
			imgHeliF2 = Image.createImage ("/hc2.png");
			imgHeliF3 = Image.createImage ("/hc3.png");
			imgShad0 = Image.createImage ("/oc0.png");
			imgShad1 = Image.createImage ("/oc1.png");
			imgShad2 = Image.createImage ("/oc2.png");
			imgShad3 = Image.createImage ("/oc3.png");
			imgAigua = Image.createImage ("/aigua.png");			
			imgFoc1 = Image.createImage ("/foc.png");
			imgFoc2 = Image.createImage ("/foc2.png");
			imgFoc3 = Image.createImage ("/foc3.png");
			imgSmallFoc = Image.createImage ("/sfoc.png");
			imgFiFoc = Image.createImage ("/fifoc.png");
			imgFum = Image.createImage ("/fum.png");
			//imgIntro = Image.createImage ("/intro.png");
			imgJefe = Image.createImage ("/jefesradio.png");
//			imgGente = Image.createImage ("/gente.png");
			imgSprites = Image.createImage ("/sprites.png");		
	        }
	        catch (Exception e)	{System.err.println ("loadGameData6");}

/*
			imgIntro = readImage ("intro7650.png"); 

			imgBar = readImage("menus.png"); //tile_2.png");
			imgHeliF0 = readImage ("hc0.png");
			imgHeliF1 = readImage ("hc1.png");
			imgHeliF2 = readImage ("hc2.png");
			imgHeliF3 = readImage ("hc3.png");
			imgShad0 = readImage ("oc0.png");
			imgShad1 = readImage ("oc1.png");
			imgShad2 = readImage ("oc2.png");
			imgShad3 = readImage ("oc3.png");
			imgAigua = readImage ("aigua.png");			
			imgFoc1 = readImage ("foc.png");
			imgFoc2 = readImage ("foc2.png");
			imgFoc3 = readImage ("foc3.png");
			imgSmallFoc = readImage ("sfoc.png");
			imgFiFoc = readImage ("fifoc.png");
			imgFum = readImage ("fum.png");
			//imgIntro = readImage ("intro.png");			
			imgJefe = readImage ("jefesradio.png");
			imgGente = readImage ("gente.png");
			
			imgSprites = readImage ("sprites.png");		
*/
	}
	

	void	drawFrameRect ()
	{
		gBuffer.setColor(255,225,205);
	 	gBuffer.fillRect(5,5,w-10,h-10);
	 	gBuffer.setColor(0,0,0);
	 	gBuffer.drawRect(4,4,w-9,h-9);
	 	
	}
	
	void	drawIntro (int msTime)
	{		
		if (msTime < 5000)
		{
			gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgIntro, 0, -148+(msTime-5000)/450+0, 20);
			
			gBuffer.setColor(255,225,205);
			gBuffer.fillRect (0,75,w,h);
			gBuffer.setColor (0, 0, 0);
			gBuffer.drawRect (0,74,GC.w-1,GC.h-45);
			
			if (msTime < 2500)
			{
				gBuffer.drawString(TextResources.sIntro1,5,76,20);
				gBuffer.drawString(TextResources.sIntro2,65,88,20);
			}
			else if (msTime < 5000)
			{
				gBuffer.drawString(TextResources.sIntro3,5,76,20);
				gBuffer.drawString(TextResources.sIntro4,55,88,20);
			}
			
		}
		else if (msTime < 10000)
		{
			gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgIntro, 0, -248+(-5000+msTime-5000)/450+0, 20);
			
			gBuffer.setColor(255,225,205);
			gBuffer.fillRect (0,75,w,h);
			gBuffer.setColor (0, 0, 0);
			gBuffer.drawRect (0,74,GC.w-1,GC.h-45);
			
			if (msTime < 7500)
			{
				gBuffer.drawString(TextResources.sIntro5,5,76,20);
				gBuffer.drawString(TextResources.sIntro6,65,88,20);
			}
			else if (msTime <10000)
			{
				gBuffer.drawString(TextResources.sIntro7,5,76,20);
				gBuffer.drawString(TextResources.sIntro8,65,88,20);
			}
		}
		else if (msTime < 23000)
		{
			gBuffer.setColor(255,225,205);
			gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
			gBuffer.fillRect (0, 0, w, h);
			
				gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
				gBuffer.drawImage (imgIntro, 0, -48, 20);		// fondo
			
			int	ix = (15000-msTime)/50;
			int	iy = (15000-msTime)/120;
			
			ix = ix < 0 ? 0 : ix;
			iy = iy < 0 ? 0 : iy;
			
			ix += 35-22 + ((GC.rand () & 0x0f) >>3);
			iy += -10 + ((GC.rand () & 0x0f) >>3);

			gBuffer.setClip (0, -iy, ix+95+NOKIA_BUGCLIP, 44+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgIntro, ix, -iy, 20);

			int	ixt = -(15000-msTime)/40;
			
			if (ixt > w-81-40)
			{
				ixt = w-81-40;
			
				gBuffer.setClip (ixt, h-58, 81+NOKIA_BUGCLIP, 42+NOKIA_BUGCLIP);
				gBuffer.drawImage (imgIntro, ixt-95, h-58-0, 20);

				int	ipx = 44+30+9;
				int	ipy = 27+19;
	
				gBuffer.setClip (ipx, ipy, 42+NOKIA_BUGCLIP, 5+NOKIA_BUGCLIP);
				gBuffer.drawImage (imgIntro, ipx-111, ipy-42, 20);//1, 20);
			}
			else
			{
				gBuffer.setClip (ixt, h-58, 81+NOKIA_BUGCLIP, 42+NOKIA_BUGCLIP);
				gBuffer.drawImage (imgIntro, ixt-95, h-58-0, 20);
			}

		}
		else	GC.stateCanvas = GC.STC_MENU;
	}
	

	void	drawStartMission ()
	{
		
		drawFrameRect ();
		gBuffer.setColor (0, 0, 0);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
		gBuffer.drawString(TextResources.sMission+((int)(GC.numLevel==0?3:GC.numLevel)),60,45,20);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	}

/*	void	drawEndMission ()
	{
		
		gBuffer.setColor (255,255,255);
		gBuffer.fillRect (0,45,w,h);
		gBuffer.setColor (0, 0, 0);
		gBuffer.drawRect (0,44,GC.w-1,GC.h-45);
		
		
	//	gBuffer.drawString ("Mission "+numLevel, w/2, h/2-16, Graphics.TOP|Graphics.HCENTER);		
		
		gBuffer.setColor (255, 255, 255);
		gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
		gBuffer.fillRect (0, 0, w, h);

		gBuffer.setColor (0, 0, 0);
		gBuffer.drawString("Mission "+((int)(GC.numLevel==0?3:GC.numLevel)),50,16,20);
		gBuffer.drawString("Completed",50,26,20);
	}
*/
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
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
		gBuffer.drawString(TextResources.sMission+((int)(GC.numLevel==0?3:GC.numLevel))+TextResources.sCompleted,30,36,20);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));

		gBuffer.drawString(TextResources.sYourTime+getTimeString (timeMission),47,64,20);
	}
	
	void	drawBestTime (int newtime, int oldtime)
	{
		drawFrameRect ();
		gBuffer.setColor (0, 0, 0);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
		gBuffer.drawString(TextResources.sBestTime,44,16+5,20);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
				
		gBuffer.drawString(TextResources.sYourTime+getTimeString (newtime),45,30+15,20);
		gBuffer.drawString(TextResources.sLastTime+getTimeString (oldtime),37,40+20,20);
	}
	
	void	drawHighScores ()
	{
		drawFrameRect ();
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
		gBuffer.drawString (TextResources.sBestTimes,32,12+5,20);
		gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		gBuffer.drawString (TextResources.sMission1+getTimeString (GC.bestTimes [1]), 25, 26+10, 20);
		gBuffer.drawString (TextResources.sMission2+getTimeString (GC.bestTimes [2]), 25, 36+15, 20);
		gBuffer.drawString (TextResources.sMission3+getTimeString (GC.bestTimes [0]), 25, 46+20, 20);
	}
	
	void	drawEndGame (int spos)
	{
		drawFrameRect ();
		// scroller
		int a=30;
	 	gBuffer.setClip(5+a,10,120,83);
	 	
		gBuffer.drawString(TextResources.sScroll1,12+a,60-spos,20);
		gBuffer.drawString(TextResources.sScroll2,12+a,80-spos,20);
		gBuffer.drawString(TextResources.sScroll3,12+a,100-spos,20);

		gBuffer.drawString(TextResources.sScroll4,12+a,127-spos,20);
		gBuffer.drawString(TextResources.sScroll5,12+a,147-spos,20);

		gBuffer.drawString(TextResources.sScroll6,12+a,167-spos,20);
		gBuffer.drawString(TextResources.sScroll7,12+a,187-spos,20);
	}

	
	void	drawGame ()
	{
	//		tiledBackA.setPositionInMap(mapXPos, mapYPos);//cambiamos posiciones del tile
	//		tiledBackB.setPositionInMap(mapXPos, mapYPos);//cambiamos posiciones del tile
	
	// 	gBuffer = screenBuffer.getGraphics();
		gBuffer.setColor (255, 255, 255);		
		gBuffer.setClip (0, 0, screenBuffer.getWidth ()+NOKIA_BUGCLIP, screenBuffer.getHeight()+NOKIA_BUGCLIP);

		if (imgBack2!=null && GC.bitTwoSwitch==1)
			gBuffer.drawImage (imgBack2, -GC.mapXPos, -GC.mapYPos, 20);
		else
			gBuffer.drawImage (imgBack, -GC.mapXPos, -GC.mapYPos, 20);
		
		
		// dibuixa foc i altres		
		for (int i = 0; i < GC.numFire; i++)
		{
			int	fpx = GC.fire [i].posX;
			int	fpy = GC.fire [i].posY;
			switch (GC.fire [i].burnState)
			{
			case 0:
			//gBuffer.setClip (fpx-GC.mapXPos-4, fpy-GC.mapYPos-14, 9+NOKIA_BUGCLIP, 14+NOKIA_BUGCLIP);
			//gBuffer.drawImage (imgSmallFoc, fpx-GC.mapXPos-4, fpy-GC.mapYPos-GC.bitSwitch*14-14, 20);
			drawFire (fpx-GC.mapXPos, fpy-GC.mapYPos, i+GC.frameCounter, 0);
				break;
			case 1:
			//gBuffer.setClip (fpx-GC.mapXPos-6, fpy-GC.mapYPos-18 +3, 12+NOKIA_BUGCLIP, 18+NOKIA_BUGCLIP);
			//gBuffer.drawImage (imgFoc1, fpx-GC.mapXPos-6, fpy-GC.mapYPos-GC.bitSwitch*18-18 +3, 20);
			drawFire (fpx-GC.mapXPos, fpy-GC.mapYPos, i+GC.frameCounter, 1);
				break;
			case 2:
			//gBuffer.setClip (fpx-GC.mapXPos-6, fpy-GC.mapYPos-18 +4, 12+NOKIA_BUGCLIP, 18+NOKIA_BUGCLIP);
			//gBuffer.drawImage (imgFoc2, fpx-GC.mapXPos-6, fpy-GC.mapYPos-GC.bitSwitch*18-18 +4, 20);
			drawFire (fpx-GC.mapXPos, fpy-GC.mapYPos, i+GC.frameCounter, 1);
				break;
			case 3:
			//gBuffer.setClip (fpx-GC.mapXPos-7, fpy-GC.mapYPos-12 +4, 14+NOKIA_BUGCLIP, 12+NOKIA_BUGCLIP);
			//gBuffer.drawImage (imgFoc3, fpx-GC.mapXPos-7, fpy-GC.mapYPos-GC.bitSwitch*12-12 +4, 20);
			drawFire (fpx-GC.mapXPos, fpy-GC.mapYPos, i+GC.frameCounter, 3);
				break;
			case 4:
			//gBuffer.setClip (fpx-GC.mapXPos-6, fpy-GC.mapYPos-4, 12+NOKIA_BUGCLIP, 8+NOKIA_BUGCLIP);
			//gBuffer.drawImage (imgFiFoc, fpx-GC.mapXPos-6, fpy-GC.mapYPos-4, 20);
			drawFire (fpx-GC.mapXPos, fpy-GC.mapYPos, 0, 2);
			
			int	fumFrame = 0;
			int	fiLife = GC.fire [i].life;
			
			if (fiLife >= 0 && fiLife < 64*2)
			{
				fpy -= (fiLife % 64) >> 1;
				fumFrame = (fiLife % 64) / 9;

				int 	fumY = 0;
				int	fumH = 0;
				switch (fumFrame)
				{
					case 0:
						fumY =35;	fumH = 4;	break;
					case 1:
						fumY =28;	fumH = 6;	break;
					case 2:
						fumY =20;	fumH = 7;	break;
					case 3:
						fumY =10;	fumH = 9;	break;
					case 4:
						fumY =0;	fumH = 9;	break;
				}
				if (fumFrame <=4)
				{
					gBuffer.setClip (fpx-GC.mapXPos-7, fpy-GC.mapYPos-4, 14+NOKIA_BUGCLIP, fumH+NOKIA_BUGCLIP);
					gBuffer.drawImage (imgFum, fpx-GC.mapXPos-7, fpy-GC.mapYPos-fumY-4, 20);
				}			
			}

				break;
			}
				
		}
		
		
		drawTendes (1 + GC.bitSwitch, 100, 80);
		drawTendes (0, 200, 200);

		for (int m = 0; m < GC.numPer; m++)
			if (GC.arrayPer [(m*3)+2] > 0)
			{
				drawPerson (GC.arrayPer [(m*3)], GC.arrayPer [(m*3)+1], GC.frameCounter+m, m);
			}
			

		// dibuixa sprites      /////////////////////////////////////////////////////////
		
		drawHelicopter (GC.heliDir, 10, GC.heliXPos-GC.mapXPos, GC.heliYPos-GC.mapYPos);
	
		// dibuixa barra	/////////////////////////////////////////////////////////
		gBuffer.setClip (1, h - 7, 1+128+93-93+NOKIA_BUGCLIP, 6+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgBar, 1, h-1 - 7, Graphics.TOP|Graphics.LEFT);
			
		int 	timeShow = GC.timeLeft >= 600 ? 599 : GC.timeLeft; 
			
		int	minLeft = timeShow / 60;
		int	secLeft = timeShow % 60;
	
		drawSmallDigit (minLeft, 1+17, h-7);
		drawSmallDigit (secLeft / 10, 1+17+4+2, h-7);	
		drawSmallDigit (secLeft % 10, 1+17+4+4+2, h-7);	
		// :
		gBuffer.setClip (1+17+4, h - 7, 2+NOKIA_BUGCLIP, 6+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgBar, 1+17+4 , h - 7 -6-1, Graphics.TOP|Graphics.LEFT);
		
/*		gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
		gBuffer.setColor (255, 255, 255);
		gBuffer.drawLine (92-37+GC.waterLeft, h - 7 + 2, 92, h - 7 + 2);
		gBuffer.drawLine (92-37+GC.waterLeft, h - 7 + 3, 92, h - 7 + 3);
*/	
	
			
		if (GC.levSMO [GC.curSMO].bPutOutFire)
		{	// WATER Bar
			gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
			gBuffer.setColor (25, 25, 25);
			gBuffer.drawLine (128-(73-(73*GC.waterLeft)/37), h - 7 + 1, 128-92+92, h - 7 + 1);
			gBuffer.drawLine (128-(73-(73*GC.waterLeft)/37), h - 7 + 2, 128, h - 7 + 2);
			gBuffer.drawLine (128-(73-(73*GC.waterLeft)/37), h - 7 + 3, 128, h - 7 + 3);
			gBuffer.drawLine (128-(73-(73*GC.waterLeft)/37), h - 7 + 4, 128, h - 7 + 4);
		}
		else
		{	// SAVED Bar
			// numPerSaved of numPer
			// 92-37 = 55
		gBuffer.setClip (1+32, h - 7, 1+128-32+NOKIA_BUGCLIP, 6+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgBar, 1, h-1 - 7-6, Graphics.TOP|Graphics.LEFT);

			gBuffer.setClip (0, 0, w+NOKIA_BUGCLIP, h+NOKIA_BUGCLIP);
			gBuffer.setColor (25, 25, 25);
			gBuffer.drawLine (55, h - 7 + 1,128, h - 7 + 1);
			gBuffer.drawLine (55, h - 7 + 2,128, h - 7 + 2);
			gBuffer.drawLine (55, h - 7 + 3,128, h - 7 + 3);
			gBuffer.drawLine (55, h - 7 + 4,128, h - 7 + 4);


			int	iDiv = (73-GC.numPer)/(GC.numPer-1);
			
			gBuffer.setColor (240,110,10);			
			for (int i = 0; i < GC.numPerSaved; i++)
			{
			gBuffer.drawLine (128-73+(iDiv+1)*i, h - 7 + 1, 128-73+(iDiv+1)*i+iDiv-1, h - 7 + 1);
			gBuffer.drawLine (128-73+(iDiv+1)*i, h - 7 + 2, 128-73+(iDiv+1)*i+iDiv-1, h - 7 + 2);
			gBuffer.drawLine (128-73+(iDiv+1)*i, h - 7 + 3, 128-73+(iDiv+1)*i+iDiv-1, h - 7 + 3);
			gBuffer.drawLine (128-73+(iDiv+1)*i, h - 7 + 4, 128-73+(iDiv+1)*i+iDiv-1, h - 7 + 4);
			}

		}

		//if (GC.timeLeft == 0)	drawMisionComplete (GC.numLevel);
		
		drawJefeBar ();
	}

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

	private void drawTileCol (Graphics g, Image iTiles, int mapArray, int px, int py)
	{
		g.setClip (px, py, 8, 8);
		g.drawImage (iTiles, px-((mapArray%25)<<3), py-((mapArray/25)<<3), 20);
	}

/*	
	private void drawTile (Graphics g, byte tilesArray[], int mapArray, int px, int py)
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
	private void drawTendes (int n, int x, int y)
	{
		int	iy=0, ih=0;
		switch (n)
		{
			case 0:
				iy = 0; ih = 14;
				break;
			case 1:
				iy = 14; ih = 18;
				break;		
			case 2:
				iy = 32; ih = 16;
				break;
			case 3:
				iy = 48; ih = 12;
				break;
		}		
//		gBuffer.setClip (x-GC.mapXPos-8, y-GC.mapYPos-ih, 17+NOKIA_BUGCLIP, ih+NOKIA_BUGCLIP);
//		gBuffer.drawImage (imgTendes, x-GC.mapXPos-8, y-GC.mapYPos-iy-ih, 20);
	}
	


	private void drawHelicopter (int d, int h, int x, int y)
	{
		d %= 16;
		
		//gBuffer.setClip (x, y+GC.ombraDist+ h, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		//gBuffer.drawImage (imgShadow, x, y+GC.ombraDist + h -d*34, Graphics.TOP|Graphics.LEFT);

		if (d < 4)
		{
		gBuffer.setClip (x, y+GC.ombraDist+ h, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgShad0, x, y+GC.ombraDist + h -(d)*34, Graphics.TOP|Graphics.LEFT);
		}
		else if (d < 8)
		{
		gBuffer.setClip (x, y+GC.ombraDist+ h, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgShad1, x, y+GC.ombraDist + h -(d-4)*34, Graphics.TOP|Graphics.LEFT);
		}
		else if (d < 12)
		{
		gBuffer.setClip (x, y+GC.ombraDist+ h, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgShad2, x, y+GC.ombraDist + h -(d-8)*34, Graphics.TOP|Graphics.LEFT);
		}
		else if (d < 16)
		{
		gBuffer.setClip (x, y+GC.ombraDist+ h, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgShad3, x, y+GC.ombraDist + h -(d-12)*34, Graphics.TOP|Graphics.LEFT);
		}
		
		// aqui en mig em de dibuixar l'aigua q cau
		if (GC.bWaterDropped > 0)
		{
//			gBuffer.setClip (x+9, 2+y+GC.bWaterDropped, 15+NOKIA_BUGCLIP, 15+NOKIA_BUGCLIP);
//			gBuffer.drawImage (imgAigua, x+9, 2+y+GC.bWaterDropped -(GC.bWaterDropped/4)*15, 20);
//			gBuffer.setClip (GC.waterDropPosX-GC.mapXPos+9, 2+GC.waterDropPosY-GC.mapYPos+GC.bWaterDropped, 15+NOKIA_BUGCLIP, 15+NOKIA_BUGCLIP);
//			gBuffer.drawImage (imgAigua, GC.waterDropPosX-GC.mapXPos+9, 2+GC.waterDropPosY-GC.mapYPos+GC.bWaterDropped -(GC.bWaterDropped/4)*15, 20);

			int	hy = 0;
			int	py = 0;
			switch (GC.bWaterDropped/4)
			{
				case 0:
					py = 0; hy = 6;	break;
				case 1:
					py = 7; hy = 9;	break;
				case 2:
					py =16; hy = 8;	break;
				}
				
			gBuffer.setClip (GC.waterDropPosX-GC.mapXPos+9, 2+GC.waterDropPosY-GC.mapYPos+GC.bWaterDropped*2, 9+NOKIA_BUGCLIP, hy+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgAigua, GC.waterDropPosX-GC.mapXPos+9, 2+GC.waterDropPosY-GC.mapYPos+GC.bWaterDropped*2 -py, 20);
			
			if (GC.bWaterDropped++ > 10)
				GC.bWaterDropped = 0;
		}
		
//		gBuffer.setClip (x, y+GC.alturaHeli, 24, 15);
//		gBuffer.drawImage (imgHeli [d], x, y+GC.alturaHeli - GC.bitSwitch*15, Graphics.TOP|Graphics.LEFT);	
//		gBuffer.setClip (x, y+GC.alturaHeli, 34+NOKIA_BUGCLIP, 22+NOKIA_BUGCLIP);
//		gBuffer.drawImage (imgHeli [d], x- GC.bitSwitch*34, y+GC.alturaHeli, Graphics.TOP|Graphics.LEFT);

/*
		if (d < 8)
		{
		gBuffer.setClip (x, y+GC.alturaHeli, 34+NOKIA_BUGCLIP, 22+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgHeliF0, x- GC.bitSwitch*34, y+GC.alturaHeli -d*22, Graphics.TOP|Graphics.LEFT);
		}
		else	
		{
		gBuffer.setClip (x, y+GC.alturaHeli, 34+NOKIA_BUGCLIP, 22+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgHeliF1, x- GC.bitSwitch*34, y+GC.alturaHeli -(d-8)*22, Graphics.TOP|Graphics.LEFT);
		}		
*/

		if (d < 4)
		{
		gBuffer.setClip (x, y+GC.alturaHeli, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgHeliF0, x- GC.bitSwitch*34, y+GC.alturaHeli -(d)*34, Graphics.TOP|Graphics.LEFT);
		}
		else if (d < 8)
		{
		gBuffer.setClip (x, y+GC.alturaHeli, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgHeliF1, x- GC.bitSwitch*34, y+GC.alturaHeli -(d-4)*34, Graphics.TOP|Graphics.LEFT);
		}
		else if (d < 12)
		{
		gBuffer.setClip (x, y+GC.alturaHeli, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgHeliF2, x- GC.bitSwitch*34, y+GC.alturaHeli -(d-8)*34, Graphics.TOP|Graphics.LEFT);
		}
		else if (d < 16)
		{
		gBuffer.setClip (x, y+GC.alturaHeli, 34+NOKIA_BUGCLIP, 34+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgHeliF3, x- GC.bitSwitch*34, y+GC.alturaHeli -(d-12)*34, Graphics.TOP|Graphics.LEFT);
		}

	}
	

	private void drawSmallDigit (int n, int x, int y)
	{
		if (n > 9)	n = 0;
		gBuffer.setClip (x, y, 3+NOKIA_BUGCLIP, 6+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgBar, x -2-3*n, y -6-1, Graphics.TOP|Graphics.LEFT);
	}
	
	private void drawMisionComplete (int n)
	{
		gBuffer.setClip (16, 7+h/2, 63+NOKIA_BUGCLIP, 21-12+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgBar, 16, 7+h/2 -12, Graphics.TOP|Graphics.LEFT);
		gBuffer.setClip (16+25, 7+h/2, 5+NOKIA_BUGCLIP, 9+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgBar, 16+25 -5*n, 7+h/2 -12-(21-12), Graphics.TOP|Graphics.LEFT);
	}
	
	private void drawJefe (int yPos)
	{
		//int yPos = 0;
		
		gBuffer.setClip (0, 0, 152+NOKIA_BUGCLIP, 21+11+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgJefe, 0, -17-yPos, 20);
		
		gBuffer.setClip (3, 2+2-yPos, 15+NOKIA_BUGCLIP, 17+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgJefe, 3, 2+2-yPos, 20);		
	}
	
	
	private void drawPerson (int x, int y, int numFrame, int t)
	{
		numFrame >>= 1;
		numFrame %= 8;
		
		x-=GC.mapXPos+5;
		y-=GC.mapYPos+5;
	
		t %= 4;
		if (t==1)	t = 52;
		if (t==2)	t = 111;
		if (t==3)	t = 0;
		
		if ( numFrame >= 5)
		{
		//gBuffer.setClip (x-5, y, 11+NOKIA_BUGCLIP, 9+NOKIA_BUGCLIP);
		//gBuffer.drawImage (imgGente, x-5, y-9*(8-numFrame), Graphics.TOP|Graphics.LEFT);
		drawPersonRealFrame (x, y, 8-numFrame, t);
		}
		else
		{
		//gBuffer.setClip (x-5, y, 11+NOKIA_BUGCLIP, 9+NOKIA_BUGCLIP);
		//gBuffer.drawImage (imgGente, x-5, y-9*numFrame, Graphics.TOP|Graphics.LEFT);
		drawPersonRealFrame (x, y, numFrame, t);
		}
	}	

	private void drawPersonRealFrame (int x, int y, int rframe, int offs)
	{
		int pw=0, pp=0, po=0;
		
		switch (rframe)
		{
			case 0:
				po =-2;	pp = 2;	pw = 9;	break;
			case 1:
				po =-1;	pp =13;	pw = 7;	break;
			case 2:
				po =-1;	pp =22;	pw = 7;	break;
			case 3:
				po = 0;	pp =31;	pw = 6;	break;
			case 4:
				po = 1;	pp =38;	pw = 5;	break;
			}
		gBuffer.setClip (x+po, y, pw+NOKIA_BUGCLIP, 11+NOKIA_BUGCLIP);
		gBuffer.drawImage (imgSprites, x+po-pp-offs, y-46, Graphics.TOP|Graphics.LEFT);	
	}
	
	private void drawFire (int x, int y, int n, int t)
	{
		int fw=0, fp=0, fo=0;

		switch (t)
		{
			case 0:
				n %= 4;
				switch (n)
				{
					case 0:
						fo =-4;	fp = 0;	fw = 7;	break;
					case 1:
						fo =-4;	fp = 7;	fw = 8;	break;
					case 2:
						fo =-4;	fp =15;	fw = 9;	break;
					case 3:
						fo =-4;	fp =24;	fw = 7;	break;					
				}
				
			gBuffer.setClip (x+fo, y-15, fw+NOKIA_BUGCLIP, 16+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgSprites, x+fo-fp, y-15, 20);
			
			break;
			case 1:
				n %= 3;
				switch (n)
				{
					case 0:
						fo =-7;	fp =33;	fw =15;	break;
					case 1:
						fo =-7;	fp =48;	fw =15;	break;
					case 2:
						fo =-7;	fp =64;	fw =15;	break;
				}
				
			gBuffer.setClip (x+fo, y-21, fw+NOKIA_BUGCLIP, 24+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgSprites, x+fo-fp, y-21, 20);
			break;
			case 2:
			gBuffer.setClip (x-9, y-15, 20+NOKIA_BUGCLIP, 24+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgSprites, x-9-82, y-15, 20);
			break;
			
			case 3:
				n %= 2;
				switch (n)
				{
					case 0:
						fo =-7;	fp =107;fw =16;	break;
					case 1:
						fo =-7;	fp =123;fw =16;	break;
				}
			gBuffer.setClip (x+fo, y-14, fw+NOKIA_BUGCLIP, 16+NOKIA_BUGCLIP);
			gBuffer.drawImage (imgSprites, x+fo-fp, y-14-8, 20);
			break;
		}
	}
	

}
