package com.mygdx.mongojocs.rescue;// GameCanvas


import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;

public class GameCanvas
	extends FullCanvas
	implements Runnable
{
	static final int SLOW_DOWN = 20;//80;
	static final int MAX_PER = 40;
	static final int MAX_FIRE = 90;
	static final int MAX_WATER_FIRE = 16;
	static final int MAX_LEVEL = 3;

	static final int STC_PRE_INTRO		= -1;
	static final int STC_INTRO		= 0;
	static final int STC_MENU		= 1;
	static final int STC_CONTROLS		= 2;
	static final int STC_HIGHSCORES		= 3;
	static final int STC_NEW_HIGHSCORE	= 4;
	static final int STC_START_GAME		= 5;
	static final int STC_START_MISSION	= 6;
	static final int STC_INGAME		= 7;
	static final int STC_END_MISSION	= 8;
	static final int STC_END_GAME		= 9;
	static final int STC_GAME_OVER		= 10;
	static final int STC_INGAME_MENU	= 11;

	GameData	GD;
	
	public int w=getWidth();
	public int h=208-54*2; //getHeight();
	
	Thread thread;
	
	int	keyNum;

	static final short	levelHeliPos [] = { 205, 143,
						    140, 2,
						    210,200,	// :P :P
				 		 };


	//level 1 -datapos
	short lev1Tendes[] = { 21,32, 68,87, 41,118, 93,51 };
	short lev1Persones[] = {40,22, 47,31, 22,44, 12,46, 40,64, 109,57,
				84,67, 100,96, 70,115, 138,44, 18,55, 68,68, 62,42, 
				35,85, 18,93 };

	short lev1SouthTendes[] = { 112,230 };

	short lev1SouthPersones[] = { 152,213, 103,208, 147,190, 178,226,
				 	134,233, 139,222 };


	//level 2 -datapos
	short lev2Persones[] = { 133,145, 141,143, 149,141, 151,153, 160,148, 170,165, 181,159,
				199,205, 208,203, 217,214, 204,210, 122,142, 136,137, 64,117,
				71,109, 47,96, 25,91, 14,84, };

	short lev2Fire[] = { 37,99, 57,106, 77,121, 121,131, 181,170, 189,194, 230,195, 215,187,
				170,153 };

	short lev2PersonesRiverSide[] = { 7<<3,5<<3, 9<<3,6<<3, 11<<3,7<<3, 4<<3,7<<3, 12<<3,3<<3, 10<<3,7<<3, 24<<3,3<<3, 27<<3,2<<3 };
	
	// level 3 -datapos
	short lev3FireSpread[] = {152,107, 126,101, 149,61, 89,82, 137,84, 92,98, 160,60, 128,75,
				74,85, 100,99, 123,68, 133,78, 119,97, 133,105, 167,61, 177,57,
				148,100, 153,97, 146,90, 130,67, 77,101, 190,46, 202,44, 67,111,
				58,117 };

	short lev3Persones[] = { 100,111, 118,119, 126,122, 134,118, 141,128, 157,77, 165,80, 170,98,
				176,87, 191,104, 130,135, 188,83, 59,96, 105,129, 82,121, 194,68,
				92,133, 51,138, 75,143, 24,158, 209,77, 115,148 };

	int	helpPage;
	
	int	lastSound=0;
	boolean	bVibra=true, bSound=true, bIngame=false;
	//	
	int	heliDirVel=0;
	int	heliDir;
	int	mapXPos=0, mapYPos=0;

	int	oldHeliXPos, oldHeliYPos;	
	int	heliXPos=208, heliYPos=146;
	int	heliXVel=0, heliYVel=0;
	
	int	fbmX=0, fbmY=0;
	
	int	frameCounter = 0;
	int	bitSwitch = 0;
	int	bitTwoSwitch = 0;
	int	timeMax=90;
	int	timeLeft;
	int	waterLeft=0;
	
	int	alturaHeli=10;
	int	ombraDist=10;
	
	long	timeStart, timeLastFrame, timeCurrent;
	
	Fire	fire[]=new Fire[MAX_FIRE];
	int	numFire=0, numActiveFire=0;
	
	int	bGetWater=0;
	int	bTakeOff=20;
	int	numLevel=1;
	
	int	stateCanvas=STC_PRE_INTRO;

	Fire	waterFire[] = new Fire[MAX_FIRE];
	
	
	int	curMenuPos=-1;
	int	scrollPos=0;
	
	int	numPerSaved;
	int	numPer;
	short	arrayPer[] = new short [MAX_PER*3];
	
	int		curSMO;
	SubMisObj	levSMO [];
	
	int	bWaterDropped = 0;
	int	waterDropPosX = 0;
	int	waterDropPosY = 0;

	int	bestTimes [] = { 599, 599, 599 };

	int	totalTimeMission = -1;

	static final byte	heliDirVelY[] = {  0,-1,-2,-2,   -2,  -2,-2,-1,
						   0, 1, 2, 2,    2,   2, 2, 1 };

	static final byte	heliDirVelX[] = {  2, 2, 2, 1,    0,  -1,-2,-2,
						  -2,-2,-2,-1,    0,   1, 2, 2 };

	
	int	randSeed = 0;
	public int rand ()
	{
		randSeed = randSeed * 214013 + 2531011;
		return randSeed;
	}



	//Sound sound[];

	void	initSound ()
	{
	/*sound=new Sound[9];
	String s1;
	s1 = 
	"024A3A594995CD8DD5940400D126A26C41A6249B104D8698926C41361A6249B104D8698926C41361A628B30C30D31245859882CC491616620B31245859882CC491616620B312458590B2AC49561A620D312558698834C49562C9B12618A22C4916186288B16458A30C38C26C30C4986289B0C312618A26C30C4186249B0C310618926C308000";
	sound[0] = new Sound(a(s1), 1);
	sound[0].init(a(s1), 1);
	// sound[0].setGain(60);//intro.ota
	
	s1 = 
	"024A3A594995CD8DD59404005124830C498810A28BC0850B408510698935021451B810A1A810A20B3125A0428A2F02142D02144136249C085145204284E000";
	sound[1] = new Sound(a(s1), 1);
	sound[1].init(a(s1), 1);
	// sound[1].setGain(60);//inifase.ota
	
	s1 = "024A3A594995CD8DD594040011205208370494820DC000";
	sound[2] = new Sound(a(s1), 1);
	sound[2].init(a(s1), 1);
	// sound[2].setGain(60);//rescate.ota
	
	s1 = "024A3A594995CD8DD59404001326A28C49281081781081980000";
	sound[3] = new Sound(a(s1), 1);
	sound[3].init(a(s1), 1);
	// sound[3].setGain(60);//centra.ota
	s1 = 
	"024A3A594995CD8DD59404006326A34C41A6249B104D8698926C41361A6289B105986D84585986D8458598A2AC4986288B12458618A22C4916186289B104D86184584D86184584D800";
	sound[4] = new Sound(a(s1), 1);
	sound[4].init(a(s1), 1);
	// sound[4].setGain(60);//finfase.ota
	System.gc ();*/
	}

	private static byte[] a(String s1)
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

	public void soundPlay (int s, int n)
	{
		/*if (bSound)
		{
			sound [lastSound].stop ();
			sound [s].play (n);
			lastSound = s;
		}	*/
	}
	
	public void addFire (int posX, int posY)
	{
		if (numFire < MAX_FIRE)
		{
			for (int i = 0; i < numFire; i++)
				if (fire [i].posX == posX && fire [i].posY == posY)
					return;
			fire [numFire++] = new Fire (posX, posY);
		}
	}
	
	public void addPersons (short [] arrayInt, int nper)
	{
		int numMaxPer = (arrayInt.length/2)-1;
		
		if (numPer + nper > MAX_PER)	nper = MAX_PER - numPer;
		if (numMaxPer < nper)		nper = numMaxPer;

		for (int i = 0; i < nper; i++)
		{
			int indx = rand ();
			
			indx = indx < 0 ? -indx : indx;
			
			indx %= numMaxPer;
			
			for (int j = 0; j < numPer; j ++)
			{
				if (arrayPer [j*3  ] == arrayInt [indx*2] &&
					arrayPer [j*3+1] == arrayInt [indx*2+1])
				{
					indx = rand ();
					indx = indx < 0 ? -indx : indx;
					indx %= numMaxPer;
				}
			
			}
			
			arrayPer [(numPer+i)*3  ] = arrayInt [indx*2  ];		// posx
			arrayPer [(numPer+i)*3+1] = arrayInt [indx*2+1];		// posy
			arrayPer [(numPer+i)*3+2] = 23;		// life
		}
		numPer += nper;
	}

	public void addFires (short [] arrayInt, int nfir)
	{
		int numMaxFir = (arrayInt.length/2)-1;
		
		if (numFire + nfir > MAX_PER)	nfir = MAX_FIRE - numFire;
		if (numMaxFir < nfir)		nfir = numMaxFir;

		for (int i = 0; i < nfir; i++)
		{
			int indx = rand ();			
			indx = indx < 0 ? -indx : indx;			
			indx %= numMaxFir;
			
			addFire (arrayInt [indx*2  ], arrayInt [indx*2+1]);
			fire [numFire-1].burnState = (rand () & 3) & i;
			fire [numFire-1].life = rand () & 0x1f;
		}
	}
	
	
	public void stop()
	{ 
	}

	private void resetLevel ()
	{
		for (int i = 0; i < MAX_FIRE; i++)
		{
			if (fire [i]!=null)		fire [i] = null;
			if (waterFire [i]!=null)	waterFire [i] = null;
		}
		bGetWater=0;
		if (numLevel != 2)	bTakeOff=20;
		numFire=0;
		numActiveFire=0;	

		heliDirVel=0;
		heliDir=7;
		mapXPos=0; mapYPos=0;
			
		heliXPos=levelHeliPos [(numLevel<<1)];
		heliYPos=levelHeliPos [(numLevel<<1)+1];
		heliXVel=0; heliYVel=0;
			
		fbmX=0; fbmY=0;
			
		frameCounter = 0;
		bitSwitch = 0;
		bitTwoSwitch = 0;
		waterLeft=0;
		
		if (numLevel != 2)	alturaHeli=10;
		else			alturaHeli=0;
		ombraDist=10;
		
		curSMO = 0;
		
		numPer = 0;
		numPerSaved = 0;
		switch (numLevel)
		{
			case 0:
				addPersons (lev1Persones, 14);
				addFire (12<<3, 9<<3);
				addFire ((2+rand()&0x7)<<3, (2+rand()&0x7)<<3);
				fire [0].burnState = 1;
				timeMax = 20;
								
				levSMO = new SubMisObj [4];
				// Apagar nord
				levSMO [0] = new SubMisObj (200, 0, true);
				// Rescatar nord
				levSMO [1] = new SubMisObj (200, 8, false);
				// Rescatar sud
				levSMO [2] = new SubMisObj (200, 5, false);
				// Rescatar sud
				levSMO [3] = new SubMisObj (200, 0, true);
				
				GD.ShowMessage (TextResources.sMis3Obj0Msg);
				break;
			case 1:
				timeMax = 50;

				addPersons (lev2Persones, 11);
				addFire (lev2Fire [0], lev2Fire [1]);
				addFire (lev2Fire [2], lev2Fire [3]);
				fire [0].burnState = 1;				
				addFires (lev2Fire, 12);
				

				levSMO = new SubMisObj [3];
				// Rescatar persones pont
				levSMO [0] = new SubMisObj (200, 3, false);
				// Apagar foc
				levSMO [1] = new SubMisObj (200, 0, true);
				// Rescatar persones platja
				levSMO [2] = new SubMisObj (200, 5, false);

				GD.ShowMessage (TextResources.sMis1Obj0Msg);
				break;
			case 2:
				timeMax = 70;

				addPersons (lev3Persones, 8);
				addFire (lev3FireSpread [0], lev3FireSpread [1]);
				addFire (lev3FireSpread [2], lev3FireSpread [3]);
				fire [0].burnState = 1;
				addFires (lev3FireSpread, 12);

				levSMO = new SubMisObj [3];
				// Apagar foc
				levSMO [0] = new SubMisObj (200, 0, true);
				// Rescatar persones
				levSMO [1] = new SubMisObj (200, 8, false);
				// Apagar foc totalment
				levSMO [2] = new SubMisObj (200, 0, true);
				GD.ShowMessage (TextResources.sMis2Obj0Msg);
				break;
		}
	}
	

	public void run()
	{
		while (thread!=null)
		{
//		thread.yield();

		GD.gBuffer.setColor (0,0,0);
		GD.gBuffer.setClip (0, 0, w, h);

		GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));

		if (stateCanvas == STC_PRE_INTRO)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.gBuffer.drawImage (GD.imgLogo, (176-90)/2, 15, 20);
				}
			});
			repaint();
			serviceRepaints();

			//if (!GD.bGameDataLoaded)		
			GD.loadGameData ();
			timeStart = System.currentTimeMillis ();
			stateCanvas = STC_INTRO;
		}
		else if (stateCanvas == STC_INTRO)
		{			
			//DeviceControl.setLights(0,100);
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawIntro ((int)(System.currentTimeMillis () - timeStart));
				}
			});

		}
		else if (stateCanvas == STC_MENU)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawFrameRect();
				}});
			
			if (curMenuPos < 0)
			{
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						GD.drawFrameRect ();
						GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
						GD.gBuffer.drawString(TextResources.sFirstHelp,30,5+10,20);
						GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
						GD.gBuffer.drawString(TextResources.sFirstHelp1,30,18+15,20);
						GD.gBuffer.drawString(TextResources.sFirstHelp2,30,28+20,20);
						GD.gBuffer.drawString(TextResources.sFirstHelp3,30,38+25,20);
						GD.gBuffer.drawString(TextResources.sFirstHelp4,30,48+30,20);
					}
				});

				if (keyNum > 0)
				{
					curMenuPos = 0;
					keyNum = -1;
				}
			}
			else
			{
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
					if (curMenuPos==0)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					GD.gBuffer.drawString(TextResources.sMenu1,30,17,20);
					GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
					if (curMenuPos==1)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					if (bSound)	GD.gBuffer.drawString(TextResources.sMenu2On ,30,27+5,20);
					else		GD.gBuffer.drawString(TextResources.sMenu2Off,30,27+5,20);
					GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
					if (curMenuPos==2)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					GD.gBuffer.drawString(TextResources.sMenu3 ,30,37+10,20);
					GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
					if (curMenuPos==3)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					GD.gBuffer.drawString(TextResources.sMenu4,30,47+15,20);
					GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
					if (curMenuPos==4)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					GD.gBuffer.drawString(TextResources.sMenu5,30,57+20,20);
					
					GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					GD.gBuffer.drawString(">",25,17+curMenuPos*15,20);
					}
				});
			}

			bIngame = false;
			
			if (keyNum == 5)
			{
				keyNum = -1;
				switch (curMenuPos)
				{
					case 0:
						stateCanvas = STC_START_GAME;
						GD.imgIntro = null;		// Free some memory
						System.gc ();
						break;
					case 1:
						bSound = bSound ? false : true;
						break;
					case 2:
						stateCanvas = STC_HIGHSCORES;
						keyNum = -1;
						break;
					case 3:
						stateCanvas = STC_CONTROLS;
						keyNum = -1;
						break;
					case 4:
						rescueApp.destroyApp (false);
						break;
				}				
			}
			else if (keyNum == 2)
			{
				if (--curMenuPos < 0)	curMenuPos = 4;
				keyNum = -1;
			}
			else if (keyNum == 8)
			{
				if (++curMenuPos > 4)	curMenuPos = 0;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_INGAME_MENU)
		{
			bIngame = true;
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {


			GD.drawFrameRect ();
/*			GD.gBuffer.drawString(TextResources.sGMenu1,20,7,20);
			if (bSound)	GD.gBuffer.drawString(TextResources.sMenu2On ,20,17,20);
			else		GD.gBuffer.drawString(TextResources.sMenu2Off,20,17,20);
			if (bVibra)	GD.gBuffer.drawString(TextResources.sMenu3On ,20,27,20);
			else		GD.gBuffer.drawString(TextResources.sMenu3Off,20,27,20);
			GD.gBuffer.drawString(TextResources.sMenu4,20,37,20);
			GD.gBuffer.drawString(TextResources.sGMenu5,20,47,20);
*/
		if (curMenuPos==0)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
			GD.gBuffer.drawString(TextResources.sGMenu1,30,17,20);
		GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		if (curMenuPos==1)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
			if (bSound)	GD.gBuffer.drawString(TextResources.sMenu2On ,30,27+5,20);
			else		GD.gBuffer.drawString(TextResources.sMenu2Off,30,27+5,20);
		GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		if (curMenuPos==2)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
			GD.gBuffer.drawString(TextResources.sMenu3,30,37+10,20);
		GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		if (curMenuPos==3)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
			GD.gBuffer.drawString(TextResources.sMenu4,30,47+15,20);
		GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		if (curMenuPos==4)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
			GD.gBuffer.drawString(TextResources.sGMenu5,30,57+20,20);
			
			GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
			GD.gBuffer.drawString(">",25,17+curMenuPos*15,20);
				}
			});
			
			if (keyNum == 5)
			{
				keyNum = -1;
				switch (curMenuPos)
				{
					case 0:
						stateCanvas = STC_INGAME;
						break;
					case 1:
						bSound = bSound ? false : true;
						break;
					case 2:
						stateCanvas = STC_HIGHSCORES;
						keyNum = -1;
						break;
					case 3:
						stateCanvas = STC_CONTROLS;
						keyNum = -1;
						break;
					case 4:
						stateCanvas = STC_MENU;
						keyNum = -1;
						break;
					}				
			}
			else if (keyNum == 2)
			{
				if (--curMenuPos < 0)	curMenuPos = 4;
				keyNum = -1;
			}
			else if (keyNum == 8)
			{
				if (++curMenuPos > 4)	curMenuPos = 0;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_CONTROLS)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {


			GD.drawFrameRect ();
			String []help = TextResources.sHelp [helpPage];
			GD.gBuffer.drawString(help [0],30,7+10,20);
			GD.gBuffer.drawString(help [1],30,17+15,20);
			GD.gBuffer.drawString(help [2],30,27+20,20);
			GD.gBuffer.drawString(help [3],30,37+25,20);
			GD.gBuffer.drawString(help [4],30,47+30,20);
				}
			});
			if (keyNum >= 0)
			{
				if (++helpPage >= TextResources.sHelp.length)
				{
					helpPage = 0;
					if (bIngame)	stateCanvas = STC_INGAME_MENU;
					else		stateCanvas = STC_MENU;
				}
				
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_HIGHSCORES)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawHighScores();
				}});
			
			if (keyNum >= 0)
			{
				stateCanvas = STC_MENU;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_START_GAME)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawFrameRect();
				}});
			stateCanvas = STC_START_MISSION;
		}
		else if (stateCanvas == STC_START_MISSION)
		{
			timeStart = System.currentTimeMillis ();
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawStartMission();
				}});
			
			repaint();
			serviceRepaints();
			
			soundPlay (1,1);

			numLevel %= 3;
			GD.deleteLevel ();
			GD.loadLevel (numLevel);
			
			while (System.currentTimeMillis () - timeStart < 1500) ;

			resetLevel ();

			//try{thread.sleep(1000);}catch(java.lang.InterruptedException e){}
			timeStart = System.currentTimeMillis ();
			timeCurrent = timeStart;

			stateCanvas = STC_INGAME;
		}
		else if (stateCanvas == STC_END_MISSION)
		{
			if (totalTimeMission < 0)
			{
				soundPlay (4,1);
				keyNum = -1;
				totalTimeMission = (int)(System.currentTimeMillis () - timeStart) / 1000;
	
				timeStart = System.currentTimeMillis ();
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						GD.drawEndMission(totalTimeMission);
					}});
				
				repaint();
				serviceRepaints();	
				
				while (System.currentTimeMillis () - timeStart < 1500) ;
			}

			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawEndMission(totalTimeMission);
				}});

			timeStart = System.currentTimeMillis ();
			timeCurrent = timeStart;
			
			if (keyNum >= 0)
			{
				// Save time mission & check besttime
				if (totalTimeMission < bestTimes [numLevel])
				{
					stateCanvas = STC_NEW_HIGHSCORE;
					keyNum = -1;
				}
				else
				{
					totalTimeMission = -1;
					if (numLevel == 0)
					{
							stateCanvas = STC_END_GAME;
					}						
					else
					{
						numLevel ++;
						numLevel %= MAX_LEVEL;
		
						stateCanvas = STC_START_MISSION;
					}
					keyNum = -1;
				}
			}
		}
		else if (stateCanvas == STC_NEW_HIGHSCORE)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawBestTime(totalTimeMission, bestTimes[numLevel]);
				}});
			if (keyNum >= 0)
			{
				// Save time besttime
				bestTimes [numLevel] = totalTimeMission;
				totalTimeMission = -1;
				if (numLevel == 0)
				{
						stateCanvas = STC_END_GAME;
				}						
				else
				{
					numLevel ++;
					numLevel %= MAX_LEVEL;
	
					stateCanvas = STC_START_MISSION;
				}
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_END_GAME)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawEndGame(scrollPos++);
				}});
			if (keyNum >= 0 || scrollPos > 190)
			{
				scrollPos = 0;
				numLevel = 0;
				stateCanvas = STC_HIGHSCORES;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_GAME_OVER)
		{
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					GD.drawFrameRect();
				}});
			GD.gBuffer.drawString (TextResources.sGameOver, w/2, h/2-16, Graphics.TOP|Graphics.HCENTER);
			if (keyNum >= 0)
			{
				stateCanvas = STC_MENU;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_INGAME)
		{	 
			gameLogic ();
		}
		
		repaint();
		serviceRepaints();	
	
		try{thread.sleep(15+SLOW_DOWN);}catch(java.lang.InterruptedException e){}
		}		
	}
	
	void	loadNewSMO ()
	{		
		if (curSMO >= levSMO.length)	return;

		soundPlay (3,1);
		//else if (bVibra)	try{DeviceControl.startVibra(30,20);} catch (java.lang.Exception e) {}

		
		if (levSMO [curSMO].bPutOutFire)
		{
		}
		else
		{
			if (numPer <= numPerSaved)	//numPer = 0;
			{
				numPerSaved = 0;
				numPer = 0;
			}
		}
		
		switch (curSMO*10+numLevel)
		{
			case 10:	
				GD.ShowMessage (TextResources.sMis3Obj1Msg);		
				break;
			case 20:
				addFire (15<<3, 27<<3);	
				addFire (16<<3, 25<<3);	
				addPersons (lev1SouthPersones, 6);
				GD.ShowMessage (TextResources.sMis3Obj2Msg);		
				break;
			case 30:	
				GD.ShowMessage (TextResources.sMis3Obj3Msg);		
				break;
//			case 40:	GD.ShowMessage (TextResources.sMis3ObjMsg [4]);		break;
			case 11:	
				GD.ShowMessage (TextResources.sMis1Obj1Msg);		
				break;
			case 21:
				addPersons (lev2PersonesRiverSide, 6);	
				GD.ShowMessage (TextResources.sMis1Obj2Msg);		
				break;
//			case 31:	GD.ShowMessage (TextResources.sMis1ObjMsg [3]);		break;
			case 12:	
				GD.ShowMessage (TextResources.sMis2Obj1Msg);		
				break;
			case 22:
				// s'haurien "d'amagar" d'alguna forma
				addFires (lev3FireSpread, 7);
				//addPersons (lev3Persones, 4);	
				GD.ShowMessage (TextResources.sMis2Obj2Msg);		
				break;
//			case 32:	
//				//GD.ShowMessage (TextResources.sMis2ObjMsg [3]);
//				break;
//			case 42:	GD.ShowMessage (TextResources.sMis2ObjMsg [4]);		break;
		}		
	}
	

	void	gameLogic ()
	{
	 	if (bitSwitch == 0)
	 	{
 			bitSwitch = 1;
 			if (bitTwoSwitch == 0)		bitTwoSwitch = 1;
 			else				bitTwoSwitch = 0;
 		}
	 	else			bitSwitch = 0;
		 	
		
		timeLastFrame = timeCurrent;
		timeCurrent = System.currentTimeMillis ();
		timeLeft = (int)(timeCurrent-timeStart)/1000;
		
		if (keyNum == 4)
		{
			heliDirVel += 2;
			//keyNum = -5;
		}
		if (keyNum == 6)
		{
			heliDirVel -= 2;
			//keyNum = -5;
		}

		if (heliDirVel > 0)	heliDirVel --;
		if (heliDirVel < 0)	heliDirVel ++;

		if (heliDirVel > 5)	heliDirVel = 5;
		if (heliDirVel < -5)	heliDirVel = -5;
						
		if (heliDirVel > 0)	heliDir ++;
		if (heliDirVel < 0)	heliDir --;

		
		heliDir &= 0x0f;
		
		if (keyNum == 8)
		{
			heliXVel -= heliDirVelX [heliDir]<<2;
			heliYVel -= heliDirVelY [heliDir]<<2;				
		}
		
		if (keyNum == 2)
		{
			heliXVel += heliDirVelX [heliDir]<<1;
			heliYVel += heliDirVelY [heliDir]<<1;
		}
		

		if (true) //bitSwitch==0)
		{
			if (heliXVel > 0)	heliXVel--;
			if (heliXVel < 0)	heliXVel++;
			if (heliYVel > 0)	heliYVel--;
			if (heliYVel < 0)	heliYVel++;
		}

		if (heliXVel > 4<<2)	heliXVel = 4<<2;
		if (heliXVel < -4<<2)	heliXVel = -4<<2;
		if (heliYVel > 4<<2)	heliYVel = 4<<2;
		if (heliYVel < -4<<2)	heliYVel = -4<<2;
		
		oldHeliXPos = heliXPos;
		oldHeliYPos = heliYPos;
		
		heliXPos += heliXVel >> 2;
		heliYPos += heliYVel >> 2;
		
		if (heliXPos < 0)	heliXPos = 0;
		if (heliYPos < -5)	heliYPos = -5;
		if (heliXPos > 240-28)	heliXPos = 240-28;
		if (heliYPos > 240-25)	heliYPos = 240-25;
		
		///////////////////////////////////////////////// edificis mapes
		if (numLevel == 0)
		{
			int dx = (heliXPos+17) - 239;
			int dy = (heliYPos+4+22) - 30;
			dx = dx<0?-dx:dx;
			dy = dy<0?-dy:dy;
			if (dx+2*dy < 48)
			{
				heliXVel = 0;
				heliYVel = 0;
				heliXPos = oldHeliXPos;
				heliYPos = oldHeliYPos;
			}
			if (heliXPos +17 > 204 && heliYPos +4+22 < 30)
			{
				heliXVel = 0;
				heliYVel = 0;
				heliXPos = oldHeliXPos;
				heliYPos = oldHeliYPos;
			}			
		}
		else if (numLevel == 1)
		{
		}
		else if (numLevel == 2)
		{
			int dx = (heliXPos+17) - 23;
			int dy = (heliYPos+4+22) - 57;
			dx = dx<0?-dx:dx;
			dy = dy<0?-dy:dy;
			if (dx+2*dy < 32)
			{
				heliXVel = 0;
				heliYVel = 0;
				heliXPos = oldHeliXPos;
				heliYPos = oldHeliYPos;
			}
			dx = (heliXPos+17) - 105;
			dy = (heliYPos+4+22) - 6;
			dx = dx<0?-dx:dx;
			dy = dy<0?-dy:dy;
			if (dx+2*dy < 56)
			{
				heliXVel = 0;
				heliYVel = 0;
				heliXPos = oldHeliXPos;
				heliYPos = oldHeliYPos;
			}
			if (heliXPos +17 < 66 && heliYPos +4+22 < 56)
			{
				heliXVel = 0;
				heliYVel = 0;
				heliXPos = oldHeliXPos;
				heliYPos = oldHeliYPos;
			}			
		}
		
		
		// fBM motion
		fbmX += (rand () % 3) - 1;
		fbmY += (rand () % 3) - 1;
		
		if (fbmX > 3)		fbmX = 3;
		if (fbmY > 3)		fbmY = 3;
		if (fbmX < -3)		fbmX = -3;
		if (fbmY < -3)		fbmY = -3;
		
		mapXPos = (heliXPos-w/2 +12) + (heliXVel>>1);
		mapYPos = (heliYPos-h/2 +12) + (heliYVel>>1);
				
		if (mapXPos < 0)	mapXPos = 0;
		if (mapYPos < 0)	mapYPos = 0;
		
		if (mapXPos > 240 - w)	mapXPos = 240 - w;
		if (mapYPos > 240 - h)	mapYPos = 240 - h;
		

		if (bWaterDropped > 1)
		{
			waterDropPosX = (waterDropPosX*7+heliXPos) >> 3;
			waterDropPosY = (waterDropPosY*7+heliYPos) >> 3;
		}
		

/////////////////////////////////////////////////////////////////////////// Mapas ///////////
		// water plena = 37
		// hotspots diam. 16 (203, 47), (214, 52)

		if (bTakeOff > 0)
		{
			if (bTakeOff == 1)
			{			
				if (alturaHeli > 0)
				{
					alturaHeli --;
				}
				else	bTakeOff = 0;
			}
			else	bTakeOff --;
		}

	// Gets Water?		
	if (levSMO [curSMO].bPutOutFire)
	{
		if (numLevel == 0)
		{
		// Mapa 0 - GetWater ///////////
			int	dWaterX, dWaterY;
	//		dWaterX = (heliXPos+12) - 203;
	//		dWaterY = (heliYPos+22) - 47;		
			dWaterX = (heliXPos+17) - 203;
			dWaterY = (heliYPos+4+22) - 47;		
			int	d1 = dWaterX*dWaterX+dWaterY*dWaterY;		
			dWaterX = (heliXPos+17) - 214;
			dWaterY = (heliYPos+4+22) - 51;		
			int	d2 = dWaterX*dWaterX+dWaterY*dWaterY;
	
			if (d1 < 8*8 || d2 < 8*8)
			{
				if (keyNum == 5)
				{
					bGetWater = 1;
					keyNum = -1;
				}
				if (bGetWater == 1)
				{
					if (alturaHeli < 7)
					{
						alturaHeli ++;
					}
					else	waterLeft ++;
					
					if (waterLeft > 37) 	waterLeft = 37;
				}
			}
			else
			{
				bGetWater = 0;
				if (alturaHeli > 0) 	alturaHeli --;
				
				if (keyNum == 5)
				if (d1 < 12*12 || d2 < 12*12)
					keyNum = -1;
			}
		}
		else if (numLevel == 1)
		{
		// Mapa 1 - GetWater ///////////
			int	dWaterX, dWaterY;
			dWaterX = (heliXPos+17) - 240;
			dWaterY = (heliYPos+4+22) - 106;		
			dWaterX = dWaterX < 0 ? -dWaterX : dWaterX;
			dWaterY = dWaterY < 0 ? -dWaterY : dWaterY;
			int	d1 = dWaterX+2*dWaterY;		
			dWaterX = (heliXPos+17) - 0;
			dWaterY = (heliYPos+4+22) - 140;		
			dWaterX = dWaterX < 0 ? -dWaterX : dWaterX;
			dWaterY = dWaterY < 0 ? -dWaterY : dWaterY;
			int	d2 = dWaterX+2*dWaterY;		
			dWaterX = (heliXPos+17) - 109;
			dWaterY = (heliYPos+4+22) - 191;		
			dWaterX = dWaterX < 0 ? -dWaterX : dWaterX;
			dWaterY = dWaterY < 0 ? -dWaterY : dWaterY;
			int	d3 = dWaterX+2*dWaterY;		
	
			if (d1 < 128 || d2 < 47 || d3 < 42 ||
			(heliXPos +17 < 43 && heliYPos +4+22 >142) ||
			(heliXPos +17 <124 && heliYPos +4+22 >188) ||
			(heliXPos +17 <201 && heliYPos +4+22 >226))
			{
				if (keyNum == 5)
				{
					bGetWater = 1;
					keyNum = -1;
				}
				if (bGetWater == 1)
				{
					if (alturaHeli < 7)
					{
						alturaHeli ++;
					}
					else	waterLeft ++;
					
					if (waterLeft > 37) 	waterLeft = 37;
				}
			}
			else
			{
				bGetWater = 0;
				if (alturaHeli > 0) 	alturaHeli --;
			}			
		}
		else if (numLevel == 2)
		{
		// Mapa 2 - GetWater ///////////
			int	dWaterX, dWaterY;
			dWaterX = (heliXPos+17) - 240;
			dWaterY = (heliYPos+4+22) - 240;		
			dWaterX = dWaterX < 0 ? -dWaterX : dWaterX;
			dWaterY = dWaterY < 0 ? -dWaterY : dWaterY;
			int	d = dWaterX+2*dWaterY;		
	
			if (d < 140)
			{
				if (keyNum == 5)
				{
					bGetWater = 1;
					keyNum = -1;
				}
				if (bGetWater == 1)
				{
					if (alturaHeli < 7)
					{
						alturaHeli ++;
					}
					else	waterLeft ++;
					
					if (waterLeft > 37) 	waterLeft = 37;
				}
			}
			else
			{
				bGetWater = 0;
				if (alturaHeli > 0) 	alturaHeli --;
			}
		}



	// Drops Water?	
		if (keyNum == 5)
		{	// Drop Water
			//waterLeft -= 9;
			//if (waterLeft < 0) waterLeft = 0;			
			if (waterLeft > 5)
			{
				//if (bVibra)	try{DeviceControl.startVibra(30,20);} catch (java.lang.Exception e) {}

				waterLeft -= 9;
				if (waterLeft < 2) waterLeft = 0;
			
				// sobre quins tiles estem ?  buscar foc
				int  hx = (heliXPos+17);//+3);
				int  hy = (heliYPos+4+12+10);//+3);
				
				bWaterDropped = 1;
				waterDropPosX = heliXPos;//hx;
				waterDropPosY = heliYPos;//hy;

// TODO : s'haura de fer un delay... anar guardant els estats dels focs al cap de 10 frames
//        per tal que no s'apaguin abans que hagi caigut l'aigua				
				for (int i = 0; i < numFire; i++)
				{
					int fx = fire [i].posX;
					int fy = fire [i].posY;
					
					if ((fx-hx)*(fx-hx)+(fy-hy)*(fy-hy) < 2*2<<6)
					{
						if (fire [i].burnState != 4)
						{
							/*
							if (fire [i].burnState == 0)
								fire [i].life += 80;
							else if (fire [i].burnState == 1)
								fire [i].life -= 180;
							else if (fire [i].burnState == 2)
								fire [i].life -= 150;
							else if (fire [i].burnState == 3)
								fire [i].life += 40;
							fire [i].burnState = 3;
							fire [i].life += 80;
							if (fire [i].life > 340)
							{
								fire [i].life = -2-(rand()&0x3f);
								fire [i].burnState = 4;
							}
							*/
							
							waterFire [i] = new Fire (fire [i].posX, fire [i].posY);
							//waterFire [i].posX = fire [i].posX;
							//waterFire [i].posY = fire [i].posY;
							waterFire [i].life = fire [i].life;
							waterFire [i].burnState = 3;
							waterFire [i].quantum = 10;
							if (waterFire [i].burnState == 0)
								waterFire [i].life += 80;
							else if (waterFire [i].burnState == 1)
								waterFire [i].life -= 180;
							else if (waterFire [i].burnState == 2)
								waterFire [i].life -= 150;
							else if (waterFire [i].burnState == 3)
								waterFire [i].life += 40;
							waterFire [i].burnState = 3;
							waterFire [i].life += 80;
							if (waterFire [i].life > 340)
							{
								waterFire [i].life = -2-(rand()&0x3f);
								waterFire [i].burnState = 4;
							}
						}
					}
				}
			}
			keyNum = -1; 	// Un sol cop
		}
		for (int i = 0; i < MAX_FIRE; i++)
		{
			if (waterFire [i] != null)
			{
				waterFire [i].quantum --;
				waterFire [i].life ++;
				if (waterFire [i].quantum <= 0)
				{
					fire [i].life = waterFire [i].life;
					fire [i].burnState = waterFire [i].burnState;					
					waterFire [i] = null;
				}
			}
		}
	}	

		if (!levSMO [curSMO].bPutOutFire && keyNum == 5)
		{
			for (int i = 0; i < numPer; i ++)
			{
				int	hx = (heliXPos+17);//+3);
				int	hy = (heliYPos+4+12+10);//+3);

				int	px = arrayPer [i*3  ];
				int	py = arrayPer [i*3+1];
				
				if ((px-hx)*(px-hx)+(py-hy)*(py-hy) < (2*2<<6)-82)
				{
					arrayPer [i*3] = -2200;
					numPerSaved ++;
					soundPlay (2,1);
					//if (bVibra)	try{DeviceControl.startVibra(30,20);} catch (java.lang.Exception e) {}

				}			
			}
			keyNum = -1;
		}
		

	
	/////////////////////////////////////////////////////////////// Map 0 - Spreads Fire?
	if (numLevel == 0)
	{
		
		if (bitSwitch==1) numActiveFire =0;
		if (bitSwitch==1)
		for (int i = 0; i < numFire; i++)
		{
		 if (fire [i].burnState != 4)	numActiveFire ++;
		 fire [i].life ++;
		 if (numFire < 25)	fire [i].quantum --;
		 if (numFire < 15)	fire [i].quantum --;
		 if (numFire < 5)	fire [i].quantum --;
		 if (fire [i].quantum-- <= 0)
		 {
			// burnState
			//   0 = initFire
			//   1 = heavyFire
			//   2 = smallFire
			//   3 = burntOutFire
			if (fire [i].burnState == 0)
			{
				if (fire [i].life > 140)
				{
					fire [i].life = 0;
					fire [i].burnState = 1;
				}
			}
			else if (fire [i].burnState == 1)
			{
				if (fire [i].life > 140)
				{
					fire [i].life = 0;
					fire [i].burnState = 2;
				}
								
				int r = rand ();
				int pxd = ((r & 0x07)) - 3 -bitTwoSwitch;
				int pyd = ((r & 0x70)>>4) - 3 -bitTwoSwitch;
				
				pxd += fire [i].posX >> 3;
				pyd += fire [i].posY >> 3;
				
				if (pxd > 1 && pxd < 30 && pyd >1 && pyd < 24)
				{
					int	mValue = GD.mapaArray [pxd+pyd*30] < 0 ? 
							256+GD.mapaArray [pxd+pyd*30] 
							: GD.mapaArray [pxd+pyd*30];//GD.map [pxd+pyd*30];
					
					if (mValue == 2+1*25 || mValue == 2+4*25 || mValue == 2+6*25
						|| mValue == 4+6*25 || mValue == 4+8*25)
						{
							// Calculem la probabilitat segons la distancia
							//int fdx = fire [i].posX >> 3;
							//int fdy = fire [i].posY >> 3;
							
							addFire (pxd<<3, pyd<<3);
						}
				}
			}
			else if (fire [i].burnState == 2)
			{
				if (fire [i].life > 240)
				{
					fire [i].life = 0;
					fire [i].burnState = 3;
				}
								
				int r = rand ();
				int pxd = ((r & 0x07)) - 3 -bitTwoSwitch;
				int pyd = ((r & 0x70)>>4) - 3 -bitTwoSwitch;
				
				pxd += fire [i].posX >> 3;
				pyd += fire [i].posY >> 3;
				
				if (pxd > 1 && pxd < 30 && pyd >1 && pyd < 24)
				{
					int	mValue = GD.mapaArray [pxd+pyd*30] < 0 ? 
							256+GD.mapaArray [pxd+pyd*30] 
							: GD.mapaArray [pxd+pyd*30];//GD.map [pxd+pyd*30];

					if (mValue == 2+1*25 || mValue == 2+4*25 || mValue == 2+6*25
						|| mValue == 4+6*25 || mValue == 4+8*25)
						{
							// Calculem la probabilitat segons la distancia
							//int fdx = fire [i].posX >> 3;
							//int fdy = fire [i].posY >> 3;
							
							addFire (pxd<<3, pyd<<3);
						}
				}
				
			}
			else if (fire [i].burnState == 3)
			{
				if (fire [i].life > 340)
				{
					fire [i].life = 0;
					fire [i].burnState = 4;
				}

				int r = rand ();
				int pxd = ((r & 0x07)) - 3 -bitTwoSwitch;
				int pyd = ((r & 0x70)>>4) - 3 -bitTwoSwitch;
				
				pxd += fire [i].posX >> 3;
				pyd += fire [i].posY >> 3;
		
				if ((frameCounter & 0x1) == (rand() & 0x1))
				if (pxd > 1 && pxd < 30 && pyd >1 && pyd < 24)
				{
					int	mValue = GD.mapaArray [pxd+pyd*30] < 0 ? 
							256+GD.mapaArray [pxd+pyd*30] 
							: GD.mapaArray [pxd+pyd*30];//GD.map [pxd+pyd*30];
					
					if (mValue == 2+1*25 || mValue == 2+4*25 || mValue == 2+6*25
						|| mValue == 4+6*25 || mValue == 4+8*25)
						{
							// Calculem la probabilitat segons la distancia
							//int fdx = fire [i].posX >> 3;
							//int fdy = fire [i].posY >> 3;
							
							addFire (pxd<<3, pyd<<3);
						}
				}
			}
		 fire [i].quantum = 12;
		 }				
		}	
	}	// End Map0 - Spreads Fire? //////////////////////////////////////////////////
	else
	{	// Map1 & Map2 - Spread Fire /////////////////////////////////////////////////
		
		if (bitSwitch==1) numActiveFire =0;
		if (bitSwitch==1)
		for (int i = 0; i < numFire; i++)
		{
			 if (fire [i].burnState != 4)	numActiveFire ++;
			 if (levSMO [curSMO].bPutOutFire)
			 {
			 	if ((rand () & 3) == 1)fire [i].life ++;
				if (fire [i].quantum-- <= 0)
				{
					if (fire [i].burnState == 0)
					{
						if (fire [i].life > 140)
						{
							fire [i].life = 0;
							fire [i].burnState = 1;
						}
					}
					else if (fire [i].burnState == 1)
					{
						if (fire [i].life > 140)
						{
							fire [i].life = 0;
							fire [i].burnState = 2;
						}
					}
					else if (fire [i].burnState == 2)
					{
						if (fire [i].life > 240)
						{
							fire [i].life = 0;
							fire [i].burnState = 3;
						}
					}
					else if (fire [i].burnState == 3)
					{
						if (fire [i].life > 340)
						{
							fire [i].life = 0;
							fire [i].burnState = 4;
						}
					}
				fire [i].quantum = 12;
				}
			}
		}
	
	}
	

//////////////////////////////////////////////////////////////////////////////////////////////
	
		if (numFire > MAX_FIRE-4)
		{
			stateCanvas = STC_GAME_OVER;
		}
		

	//// Comprovar levSMO [curSMO]    ////////////////////////////////////////////////////
	
		// es compleixen objectius?
		if (levSMO [curSMO].bPutOutFire)
		{ // toca apagar foc
			if (numActiveFire == 0)
			{
				curSMO ++;
				loadNewSMO ();
			}
		}
		else
		{ // toca rescatar persones
			if (numPer <= numPerSaved)
			{
				//numPerSaved = 0;    ja ho fem a loadNewSMO
				curSMO ++;
				loadNewSMO ();
			}
		}
		
		
	
		if (curSMO >= levSMO.length)
		{
			curSMO = 0;
			keyNum = -1;
			stateCanvas = STC_END_MISSION;
		}
		

//////////////////////////////////////////////////////////////////////////////////////////////

		heliXPos += fbmX;
		heliYPos += fbmY;

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				GD.drawGame ();
			}
		});

		heliXPos -= fbmX;
		heliYPos -= fbmY;
		
		frameCounter ++;
	}
	
	
	public void keyPressed(int keycode)
	{	
//demo
/*

		if (stateCanvas == STC_INGAME && keycode == '#')
		{
			// CHEAT !!!!
			numLevel ++;
			numLevel %= MAX_LEVEL;

			stateCanvas = STC_START_MISSION;
			//stateCanvas = STC_END_GAME;
		}
		else
	*/	if (stateCanvas == STC_INGAME && (keycode == '*' ||keycode == -6))
		{
			stateCanvas = STC_INGAME_MENU;
		}
		else
		if (stateCanvas == STC_INTRO)
		{
			if ((int)(System.currentTimeMillis () - timeStart) > 15000 || keycode == '*'||keycode == -6)
			{
				//sound [0].stop ();
				stateCanvas = STC_MENU;
			}
		}
		else
		if (keycode >= 48 && keycode <= 57)
		{
			// entre 0 i 9
			keyNum = keycode - 48;
		}
		else
		{
			keyNum = -1;
			switch(keycode)
			{
			case -1:keyNum=2;
//demo	stateCanvas = STC_GAME_OVER;keyNum=-1;
			break;	
			case -4:keyNum=6;break;	
			case -3:keyNum=4;break;	
			case -2:keyNum=8;break;	
			case -5:case -7:keyNum=5;break;	
			}
		}

		if (stateCanvas == STC_MENU)
		{
			if (curMenuPos < 0)
			{
				keyNum = -1;
				curMenuPos = 0;
			}
		}		
	}

	public void keyReleased(int keycode)
	{
		keyNum = -1;
	}
	
	rescue	rescueApp;

	public GameCanvas (rescue r)
 	{
	     	//r.PrefsLoad ();
		rescueApp = r;

		bSound = r.PrefsData [0]==1;
		bVibra = r.PrefsData [1]==1;
		bestTimes [0] = r.PrefsData [2]*10+r.PrefsData[3];
		bestTimes [1] = r.PrefsData [4]*10+r.PrefsData[5];
		bestTimes [2] = r.PrefsData [6]*10+r.PrefsData[7];	

		GD = new GameData (this, w, h); //208-54*2);
		initSound ();
					
		thread=new Thread(this);
		soundPlay (0, 8);
		thread.start();
	  	System.gc();
	}


	int posMenu=0;
	public void paint (Graphics g)
	{       
		g.drawImage (GD.screenBuffer,0,54,Graphics.TOP|Graphics.LEFT);
		g.setColor (0, 0, 0);
		g.fillRect (0, 0, w, (getHeight()-h)/2);
		g.fillRect (0, getHeight () - ((getHeight ()-h)/2), w, h);
		if(stateCanvas != STC_PRE_INTRO)
		{
		posMenu++;if(posMenu>=35){posMenu=35;}
		g.drawImage(GD.imgMenu,-35+posMenu,getHeight ()-10,20);
		}
	}
}
