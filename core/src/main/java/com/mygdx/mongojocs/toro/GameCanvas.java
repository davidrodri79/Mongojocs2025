package com.mygdx.mongojocs.toro;// GameCanvas

/*
	TODO : 
		
	
	Punt mig 191,131  -> altura 77 anch 183 //7
	
	
	Textos fixejar
	
	Scroll final setClip fixejar
	

*/


import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;

public class GameCanvas
	extends FullCanvas
	implements Runnable
{
	boolean bExitMidletApp = false;
	static final int SLOW_DOWN = 65;//75;//80;

	static final int STC_LOADING		=-1;
	static final int STC_INTRO		= 0;
	static final int STC_MENU		= 1;
	static final int STC_CONTROLS		= 2;
	static final int STC_START_GAME		= 5;
	static final int STC_START_MISSION	= 6;
	static final int STC_INGAME		= 7;
	static final int STC_END_MISSION	= 8;
	static final int STC_END_GAME		= 9;
	static final int STC_GAME_OVER		= 10;
	static final int STC_INGAME_MENU	= 11;
	static final int STC_RETRY_MISSION	= 12;
	static final int STC_PRE_START_MISSION	= 21;

	GameData	GD;
	
	public int w=getWidth();
	public int h=getHeight();
	
	Thread thread;
	
	int	keyNum;


	int	helpPage;
	
	boolean	bVibra=false, bSound=true, bIngame=false;

	///////////////////////////////////////////////////////////////////////////	
	
	static final int MAX_ROSES 	= 10;
	static final int MAX_TORO	= 10;
	
	int	protaDir=0;
	int	mapXPos=0, mapYPos=0;

	int	oldProtaXPos, oldProtaYPos;	
	int	protaXPos=208, protaYPos=146;
	int	protaXVel=0, protaYVel=0;
	
	int	protaDamage;
	
	int	protaState;	// 0-run  2-dead 3-win
	int	countProta;

	int		numToros;
	ToroObj		tabToros [] = new ToroObj [MAX_TORO];	
	
	ToroObj		toro = new ToroObj(this);
	
	int	numRosesCollected=0;

	int	numRoses=0;
	short	tabRoses [] = new short[MAX_ROSES*2];
		
	///////////////////////////////////////////////////////////////////////////
	int	frameCounter = 0;
	int	bitSwitch = 0;
	int	bitTwoSwitch = 0;
	
	long	timeStart, timeLastFrame, timeCurrent, timeElapsed;
	
	
	int	numLevel=0;
	
	int	stateCanvas=STC_LOADING;

	int	curMenuPos=0;
	int	scrollPos=0;
	

	int	randSeed = 0;
	public int rand ()
	{
		randSeed = randSeed * 214013 + 2531011;
		return randSeed;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/// Sound
	//
	//Sound sound[];

	void	initSound ()
	{
	/*sound=new Sound[6];
	String s1,s2,s3,s4,s5,s6;
	
	// toro_intro
	s1 =
	"024A3A69D1BDC9BD7DA5B9D1C9BC0400AD18A2AC2702B02C92B027022C49C61A61981A81C49C8288C09B0AB08B126A0720A22C26C49C6288B12698718558560560698A22C49C41861C61A41661A61961561861661561581581A6288B12710618718690598698658558618598558560560698A22C49C41861C61A41661A61961561861661520000";    
	// toro_inilevel
	s2 =
	"024A3A51D1BDC9BC04003D1C5585605605605605605605605605605605605605585985A05A05A05A05A05A05A05A05A05A0620620598552000";
	// toro_winlevel
	s3 = "024A3A51D1BDC9BC04000F1C560558A2302AC22C00";
	// toro_rosa
	s4 = "024A3A51D1BDC9BC04000B1C4606A0A23000";
	// toro_ole
	s5 = "024A3A51D1BDC9BC0400091C45AA22C000";
	// toro_dead
	s6 = "024A3A51D1BDC9BC0400111CA22C4988158188116800";


	sound[0] = new Sound(a(s1), 1);		// 0-intro
	sound[0].init(a(s1), 1);
	sound[1] = new Sound(a(s2), 1);		// 1-inilevel
	sound[1].init(a(s2), 1);
	sound[2] = new Sound(a(s3), 1);		// 2-winlevel
	sound[2].init(a(s3), 1);
	sound[3] = new Sound(a(s4), 1);		// 3-rosa
	sound[3].init(a(s4), 1);
	sound[4] = new Sound(a(s5), 1);		// 4-ole
	sound[4].init(a(s5), 1);
	sound[5] = new Sound(a(s6), 1);		// 5-dead
	sound[5].init(a(s6), 1);

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

	int	lastSound=0;
	void playSound (int s, int t)
	{			
		/*if (bSound)
		{
			sound [lastSound].stop ();
			sound [s].play (t);
			lastSound = s;
		}*/
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	public void stop()
	{
	}

	void	addRose ()
	{
		int	rx, ry;
		
		rx = rand () & 0x1ff;
		ry = rand () & 0xff;
		
		int	XDif = 8+rx - 191; //200;
		int	YDif = 5+ry - 131; //138;
		
		int	Dif2 = XDif*XDif + 4*YDif*YDif;
		
		while (Dif2 > 160*160+60*60)
		{
			rx = rand () & 0x1ff;
			ry = rand () & 0xff;
			
			XDif = 8+rx - 191; //200;
			YDif = 5+ry - 131; //138;
			
			Dif2 = XDif*XDif + 4*YDif*YDif;			
		}
		
		tabRoses [(numRoses<<1)  ] = ((short)rx);
		tabRoses [(numRoses<<1)+1] = ((short)ry);
		numRoses++;
	}
	

	private void resetLevel ()
	{
		mapXPos = 191;
		mapYPos = 131;
		protaXPos=208;
		protaYPos=146;
		
		numRoses = 0;
		numRosesCollected = 0;
		
		protaState = 0;
		countProta = 0;
		
		for (int k = 0; k < 10; k++) addRose ();

/*
		numToros = 0;
		for (int i = 0; i < 3; i++)
		{
			tabToros [numToros++] = new ToroObj (this);
			
			tabToros [numToros-1].posY += rand () & 0xf;
			tabToros [numToros-1].posX += rand () & 0xf;
			tabToros [numToros-1].ang = rand () & 0xf;
			
			tabToros [numToros-1].tipusToro = i | 2;
		}
*/

		switch (numLevel)
		{
			case 0:
				numToros = 2;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 2;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1|2;
				break;
			case 1:
				numToros = 2;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 2;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;
				break;
			case 2:
				numToros = 2;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 0;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;
				break;
			case 3:
				numToros = 2;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 0;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1|8|2;
				break;
			case 4:
				numToros = 3;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 2;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1|2;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1|2;
				break;
			case 5:
				numToros = 3;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 2;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1|2;
				break;
			case 6:
				numToros = 3;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 2;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1;
				break;
			case 7:
				numToros = 3;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 0;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1|8;
				break;
			case 8:
				numToros = 4;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 2;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1|2;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1;

				tabToros [3] = new ToroObj (this);
				tabToros [3].posY += rand () & 0xf;
				tabToros [3].posX += rand () & 0xf;
				tabToros [3].ang = rand () & 0xf;
				tabToros [3].tipusToro = 1|2|4;
				break;
			case 9:
				numToros = 4;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 2;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1;

				tabToros [3] = new ToroObj (this);
				tabToros [3].posY += rand () & 0xf;
				tabToros [3].posX += rand () & 0xf;
				tabToros [3].ang = rand () & 0xf;
				tabToros [3].tipusToro = 1|2|4;
				break;
			case 10:
				numToros = 4;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 0;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1;

				tabToros [3] = new ToroObj (this);
				tabToros [3].posY += rand () & 0xf;
				tabToros [3].posX += rand () & 0xf;
				tabToros [3].ang = rand () & 0xf;
				tabToros [3].tipusToro = 1|2|4;
				break;
			case 11:
				numToros = 4;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 0;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1;

				tabToros [3] = new ToroObj (this);
				tabToros [3].posY += rand () & 0xf;
				tabToros [3].posX += rand () & 0xf;
				tabToros [3].ang = rand () & 0xf;
				tabToros [3].tipusToro = 1|2;
				break;
			case 12:
				numToros = 4;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 0;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1|8;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1;

				tabToros [3] = new ToroObj (this);
				tabToros [3].posY += rand () & 0xf;
				tabToros [3].posX += rand () & 0xf;
				tabToros [3].ang = rand () & 0xf;
				tabToros [3].tipusToro = 1|2;
				break;
			case 13:
				numToros = 4;

				tabToros [0] = new ToroObj (this);
				tabToros [0].posY += rand () & 0xf;
				tabToros [0].posX += rand () & 0xf;
				tabToros [0].ang = rand () & 0xf;
				tabToros [0].tipusToro = 8;
				
				tabToros [1] = new ToroObj (this);
				tabToros [1].posY += rand () & 0xf;
				tabToros [1].posX += rand () & 0xf;
				tabToros [1].ang = rand () & 0xf;
				tabToros [1].tipusToro = 1|8;

				tabToros [2] = new ToroObj (this);
				tabToros [2].posY += rand () & 0x1f;
				tabToros [2].posX += rand () & 0x1f;
				tabToros [2].ang = rand () & 0xf;
				tabToros [2].tipusToro = 1;

				tabToros [3] = new ToroObj (this);
				tabToros [3].posY += rand () & 0xf;
				tabToros [3].posX += rand () & 0xf;
				tabToros [3].ang = rand () & 0xf;
				tabToros [3].tipusToro = 1|2;
				break;
			case 14:
				break;
		}
					

		timeStart = System.currentTimeMillis ();
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/// Thread.run
	//
	public void run()
	{
		while (thread!=null)
		{
		thread.yield();		
		
		if (stateCanvas == STC_START_MISSION)
		{//kit
			playSound ( 1, 1);
			timeStart = System.currentTimeMillis ();
			

			if (!GD.bDataLoaded)	GD.loadGameData ();

			GD.deleteLevel ();
			GD.loadLevel (numLevel);
			
			
			while (System.currentTimeMillis () - timeStart < 1200) ;

			resetLevel ();

			//try{thread.sleep(1000);}catch(java.lang.InterruptedException e){}
			timeStart = System.currentTimeMillis ();
			timeCurrent = timeStart;

			stateCanvas = STC_INGAME;
		}

		if (bExitMidletApp)	{MidletApp.destroyApp (false); thread = null; break; }

		repaint();
		serviceRepaints();	
	
		try{thread.sleep(1+SLOW_DOWN);}catch(java.lang.InterruptedException e){}
		}		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/// gameLogic
	//
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
		timeElapsed = (timeCurrent - timeStart)/1000;
		
		if (keyNum == 4)
		{
			protaDir --;
			if (protaDir < 0)	protaDir = 7;
		}
		if (keyNum == 6)
		{
			protaDir ++;
			if (protaDir >= 8)	protaDir = 0;
		}

		//toro.update ();
		
		for (int i = 0; i < numToros; i++)
		{
			tabToros [i].update ();
			
			final int  tabToroDespX [] = {  5,  7, 16, 24, 27, 26, 15, 32 };
			final int  tabToroDespY [] = { 13, 22, 20, 22, 12,  5,  4,  6 };
			final int  tabToroRadi2 [] = {169,196,196,196,169,169,196,196 };
			
			int	toroDespX = tabToroDespX [tabToros [i].dir];
			int	toroDespY = tabToroDespY [tabToros [i].dir];
			int	toroRad	  = tabToroRadi2 [tabToros [i].dir];

			//int	tdx = tabToros[i].posX+16 - protaXPos-7;
			//int	tdy = tabToros[i].posY+16 - protaYPos-10;
			int	tdx = tabToros[i].posX+toroDespX - protaXPos-7;
			int	tdy = tabToros[i].posY+toroDespY - protaYPos-10;
			
			if (tdx*tdx+tdy*tdy < toroRad && protaState == 0)
			{
				protaDamage ++;
				protaDamage ++;
				protaDamage ++;
				
				if (protaDamage > (60-5)/2)
				{
					protaState = 2;	// dead
					countProta = 0; // framecount
				}
				
				//if (bVibra)	try{DeviceControl.startVibra(30,20);} catch (java.lang.Exception e) {}			
			}
		}
		
		
		for (int i = 0; i < numRoses; i++)
		{
			if (tabRoses [i<<1] > 0)
			{
				int	drx = tabRoses [(i<<1)  ] - protaXPos;
				int	dry = tabRoses [(i<<1)+1] - protaYPos-7;
				
				if (drx*drx + dry*dry < 14*14)
				{
					numRosesCollected ++;
					int	temp = numRoses;
					//numRoses = i;
					//addRose ();
					//numRoses = temp;
					
					tabRoses [(i<<1)] = -1;
					
					//kit
					if (numRosesCollected==10)	playSound (2,1);
					else				playSound (3,1);
					
					/*if (bVibra)	try{DeviceControl.startVibra(30,20);} catch (java.lang.Exception e) {}
					*/
				}
			}	
		}
		

		byte protaXVelTab [] = { -2,-4,-2, 0, 2, 4, 2, 0 };
		byte protaYVelTab [] = {  2, 0,-2,-4,-2, 0, 2, 4 };
		
		protaXVel = protaXVelTab [protaDir];
		protaYVel = protaYVelTab [protaDir];
		
		if (protaState == 0)
		{
			protaXPos += protaXVel;
			protaYPos += protaYVel;
		}
		
		
		int	protaXDif = 8+protaXPos - 191; //200;
		int	protaYDif = 10+protaYPos - 131; //138;
		
		int	protaDif2 = protaXDif*protaXDif + 4*protaYDif*protaYDif;
		
		if (protaDif2 > 160*160+72*72)
		{
			int	pnXDif = 8+protaXPos-protaXVel - 191; //200;
			int	pnYDif = 10+protaYPos - 131; //138;
			
			if (protaDif2 > pnXDif*pnXDif + 4*pnYDif*pnYDif)
			{
				protaXPos = oldProtaXPos;
			}
	
			pnXDif = 8+protaXPos - 191; //200;
			pnYDif = 10+protaYPos-protaYVel - 131; //138;
			
			if (protaDif2 > pnXDif*pnXDif + 4*pnYDif*pnYDif)
			{
				protaYPos = oldProtaYPos;
			}
		}
		
		oldProtaXPos = protaXPos;
		oldProtaYPos = protaYPos;
				
		mapXPos = (protaXPos-w/2 + 8) + (protaXVel>>1);
		mapYPos = (protaYPos-h/2 +10) + (protaYVel>>1);
		
		if (mapXPos < 0)	mapXPos = 0;
		if (mapYPos < 0)	mapYPos = 0;
		
		if (mapXPos > 384 - w)	mapXPos = 384 - w;
		if (mapYPos > 232 - h)	mapYPos = 232 - h;
	
	
		GD.drawGame ();
		
		if (numRosesCollected == 10)
		{
			protaState = 3;		// guanya
			
			//if (bSound)	sound [2].play (1);

			//stateCanvas = STC_END_MISSION;
		}
		
		if (protaState != 0)
		{
			countProta ++;		// perd
			
			if (protaState == 2)
			{	//kit
				if (countProta==2 && bSound)	playSound (5,1);

				if (countProta > 10)
				{
					stateCanvas = STC_RETRY_MISSION;
					protaDamage = 0;
				}
				
			}
			
			if (protaState == 3)
			{
				if (countProta > 14)
				{
					if (numLevel == 13)	stateCanvas = STC_END_GAME;
					else			stateCanvas = STC_END_MISSION;
				}
				
			}
			
		}	

		frameCounter ++;
	}
	
	
	public void keyPressed(int keycode)
	{	
		if (keycode==-1)	keycode = '2';
		if (keycode==-2)	keycode = '8';
		if (keycode==-3)	keycode = '4';
		if (keycode==-4)	keycode = '6';
		if (keycode==-6)	keycode = '*';
		if (keycode==-7)	keycode = '5';
		if (keycode==-5)	keycode = '5';
/*
		if (stateCanvas == STC_INGAME && keycode == '#')
		{
			// CHEAT !!!!
			//numLevel ++;
			//numLevel %= MAX_LEVEL;

			//stateCanvas = STC_PRE_START_MISSION;
			stateCanvas = STC_END_MISSION;
		}
		else
*/		if (stateCanvas == STC_INGAME && keycode == '*')
		{
			stateCanvas = STC_INGAME_MENU;
		}

		if (stateCanvas == STC_INTRO)
		{
			//sound [0].stop ();
			stateCanvas = STC_MENU;
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
			if (stateCanvas == STC_MENU)
			{
				keyNum = 15;
			}
		}
	}

	public void keyReleased(int keycode)
	{
		keyNum = -1;
	}
	
	Toro	MidletApp;
	
	public GameCanvas (Toro r)
 	{
		MidletApp = r;
		GD = new GameData (this, w, h);
		initSound ();
			
		timeStart = System.currentTimeMillis ();

		for (int i = 0; i < ((timeStart / 10) & 0x1f); i++)	rand ();
		thread=new Thread(this);
		
		thread.start();
	  	System.gc();
	}

	public void paint (Graphics g)
	{       
		/*
		g.drawImage (GD.screenBuffer,0,0,Graphics.TOP|Graphics.LEFT);
		*/
			//		stateCanvas = STC_END_GAME;
	
		GD.gBuffer = g;
		
		GD.gBuffer.setColor (0,0,0);
		GD.gBuffer.setClip (0, 0, w+1, h+1);

		GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));

		if (stateCanvas == STC_LOADING)
		{
			timeStart = System.currentTimeMillis ();
/*			GD.gBuffer.drawImage (GD.imgMicrojocs, 0, 0, 20);
			//repaint();
			//serviceRepaints();
						
			while (System.currentTimeMillis () - timeStart < 1500);
			GD.imgMicrojocs = null;
*/			System.gc ();
			stateCanvas = STC_INTRO;
			//kit
			playSound (0,2);
		}
		else if (stateCanvas == STC_INTRO)
		{			
			GD.drawIntro ((int)(System.currentTimeMillis () - timeStart));
		}
		else if (stateCanvas == STC_MENU)
		{
			if (curMenuPos < 0)
			{
				GD.drawFrameRect ();
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
				GD.gBuffer.drawString(TextResources.sFirstHelp,25,5+10+3+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
				GD.gBuffer.drawString(TextResources.sFirstHelp1,35,18+15+13+40,20);
				GD.gBuffer.drawString(TextResources.sFirstHelp2,35,28+20+13+40,20);
				GD.gBuffer.drawString(TextResources.sFirstHelp3,35,38+25+13+40,20);
				GD.gBuffer.drawString(TextResources.sFirstHelp4,35,48+30+13+40,20);
				if (keyNum > 0)
				{//kit
					//if (bSound) sound [0].stop ();
					curMenuPos = 0;
					keyNum = -1;
				}
			}
			else
			{
			GD.drawFrameRect ();
				if (curMenuPos==0)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
				GD.gBuffer.drawString(TextResources.sMenu1,27,17+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				if (curMenuPos==1)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
				if (bSound)	GD.gBuffer.drawString(TextResources.sMenu2On ,27,27+5+6+40,20);
				else		GD.gBuffer.drawString(TextResources.sMenu2Off,27,27+5+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
	/*			if (curMenuPos==2)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					if (bVibra)	GD.gBuffer.drawString(TextResources.sMenu3On ,27,37+10+6+40,20);
					else		GD.gBuffer.drawString(TextResources.sMenu3Off,27,37+10+6+40,20);
	*/			GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				if (curMenuPos==2)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					GD.gBuffer.drawString(TextResources.sMenu4,27,37+10+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				if (curMenuPos==3)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					GD.gBuffer.drawString(TextResources.sMenu5,27,47+15+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
				GD.gBuffer.drawString(">",22,17+curMenuPos*15+6+40,20);
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
	//				case 2:
	//					bVibra = bVibra ? false : true;
	//					break;
					case 2:
						stateCanvas = STC_CONTROLS;
						keyNum = -1;
						break;
					case 3:
						bExitMidletApp = true;
						break;
					}				
			}
			else if (keyNum == 2)
			{
				if (--curMenuPos < 0)	curMenuPos = 3;
				keyNum = -1;
			}
			else if (keyNum == 8)
			{
				if (++curMenuPos > 3)	curMenuPos = 0;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_INGAME_MENU)
		{
			bIngame = true;
			GD.drawFrameRect ();

				if (curMenuPos==0)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
				GD.gBuffer.drawString(TextResources.sGMenu1,27,17+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				if (curMenuPos==1)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
				if (bSound)	GD.gBuffer.drawString(TextResources.sMenu2On ,27,27+5+6+40,20);
				else		GD.gBuffer.drawString(TextResources.sMenu2Off,27,27+5+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
	/*			if (curMenuPos==2)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					if (bVibra)	GD.gBuffer.drawString(TextResources.sMenu3On ,27,37+10+6+40,20);
					else		GD.gBuffer.drawString(TextResources.sMenu3Off,27,37+10+6+40,20);
	*/			GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				if (curMenuPos==2)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					GD.gBuffer.drawString(TextResources.sMenu4,27,37+10+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				if (curMenuPos==3)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					GD.gBuffer.drawString(TextResources.sMenu5,27,47+15+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
				
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
				GD.gBuffer.drawString(">",22,17+curMenuPos*15+6+40,20);

/*				if (curMenuPos==0)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
				GD.gBuffer.drawString(TextResources.sMenu1,27,17+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
				if (curMenuPos==1)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
				if (bSound)	GD.gBuffer.drawString(TextResources.sMenu2On ,27,27+5+6+40,20);
				else		GD.gBuffer.drawString(TextResources.sMenu2Off,27,27+5+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
				if (curMenuPos==2)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					if (bVibra)	GD.gBuffer.drawString(TextResources.sMenu3On ,27,37+10+6+40,20);
					else		GD.gBuffer.drawString(TextResources.sMenu3Off,27,37+10+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
				if (curMenuPos==3)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					GD.gBuffer.drawString(TextResources.sMenu4,27,47+15+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
				if (curMenuPos==4)	GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
					GD.gBuffer.drawString(TextResources.sGMenu5,27,57+20+6+40,20);
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
				
				GD.gBuffer.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
				GD.gBuffer.drawString(">",22,17+curMenuPos*15+6+40,20);
*/			
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
				///	case 3:
				//		bVibra = bVibra ? false : true;
				//		break;
					case 2:
						stateCanvas = STC_CONTROLS;
						keyNum = -1;
						break;
					case 3:	bExitMidletApp = true;
						//stateCanvas = STC_MENU;
						//keyNum = -1;
						break;
					}				
			}
			else if (keyNum == 2)
			{
				if (--curMenuPos < 0)	curMenuPos = 3;
				keyNum = -1;
			}
			else if (keyNum == 8)
			{
				if (++curMenuPos > 3)	curMenuPos = 0;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_CONTROLS)
		{
			GD.drawFrameRect ();
			String []help = TextResources.sHelp [helpPage];
			GD.gBuffer.drawString(help [0],20,7+10+5+3+40,20);
			GD.gBuffer.drawString(help [1],20,17+15+5+6+40,20);
			GD.gBuffer.drawString(help [2],20,27+20+5+9+40,20);
			GD.gBuffer.drawString(help [3],20,37+25+5+12+40,20);
			GD.gBuffer.drawString(help [4],20,47+30+5+15+40,20);
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
		else if (stateCanvas == STC_START_GAME)
		{
			GD.drawFrameRect ();
			stateCanvas = STC_PRE_START_MISSION;
		}
		else if (stateCanvas == STC_PRE_START_MISSION)
		{
			timeStart = System.currentTimeMillis ();
			GD.drawStartMission ();
			//if (bSound)	sound [1].play (1);
			
			stateCanvas = STC_START_MISSION;
		}
		else if (stateCanvas == STC_END_MISSION)
		{
			//GD.drawEndMission (0); //totalTimeMission);
			if (numLevel == 13)
			{
				stateCanvas = STC_END_GAME;
				timeStart = System.currentTimeMillis ();
				timeCurrent = timeStart;
			}
			else
			{
				timeStart = System.currentTimeMillis ();
				timeCurrent = timeStart;
				
				protaDamage >>=1;	// recuperarse			
				numLevel ++;
				stateCanvas = STC_PRE_START_MISSION;
			}
/*			timeStart = System.currentTimeMillis ();
			timeCurrent = timeStart;
			
			protaDamage >>=1;	// recuperarse			
			numLevel ++;
			stateCanvas = STC_PRE_START_MISSION;
*/		}
		else if (stateCanvas == STC_END_GAME)
		{
			GD.drawEndGame (scrollPos++);
			if (keyNum >= 0 || scrollPos > 160)
			{
				scrollPos = 0;
				numLevel = 0;
				stateCanvas = STC_MENU;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_GAME_OVER)
		{
			GD.drawFrameRect ();
			GD.gBuffer.drawString (TextResources.sGameOver, w/2, h/2-16+40, Graphics.TOP|Graphics.HCENTER);
			if (keyNum >= 0)
			{
				stateCanvas = STC_MENU;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_RETRY_MISSION)
		{
			GD.drawFrameRect ();
			GD.gBuffer.drawString(TextResources.sRetryMenu1,27,37+40,20);
			GD.gBuffer.drawString(TextResources.sRetryMenu2,27,57+40,20);

			GD.gBuffer.drawString (">", 22, 37+20*curMenuPos+40, 20);
			
			if (keyNum == 5)
			{
				keyNum = -1;
				switch (curMenuPos)
				{
					case 0:
						stateCanvas = STC_PRE_START_MISSION;
						break;
					case 1:
						stateCanvas = STC_MENU;
						break;
					}				
			}
			else if (keyNum == 2)
			{
				if (--curMenuPos < 0)	curMenuPos = 1;
				keyNum = -1;
			}
			else if (keyNum == 8)
			{
				if (++curMenuPos > 1)	curMenuPos = 0;
				keyNum = -1;
			}
		}
		else if (stateCanvas == STC_INGAME)
		{	 
			gameLogic ();
		}
		g.drawImage(GD.imgMicrojocs,0,208-10,20);
		//GD.gBuffer = null;
	}
}
