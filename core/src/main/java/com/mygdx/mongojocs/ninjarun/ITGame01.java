package com.mygdx.mongojocs.ninjarun;


// -----------------------------------------------
// Ninja RUN v1.1 Rev.1 (6.02.2004)
// ===============================================
// Programado por Oscar Zurriaga Jard�
// ------------------------------------


import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

public class ITGame01 extends MIDlet implements Runnable
{

ITCanvas01 gc;
Thread thread;
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public ITGame01()
{
	System.gc();

	gc = new ITCanvas01(this);
	
	System.gc();
	
	Display.getDisplay(this).setCurrent(gc);

	thread=new Thread(this); System.gc();
	thread.start();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// startApp - Al inicio y cada vez que se hizo pauseApp()
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void startApp()
{
	Display.getDisplay(this).setCurrent(gc);
	GamePaused=false;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// pauseApp - Cada vez que se PAUSA el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void pauseApp() 
{
	GamePaused=true;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// destroyApp - Para DESTRUIR el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	ITU.PrefsSET();
	gc.SoundRES();
	GameExit=1;
	notifyDestroyed();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Regla de 3
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public int Regla3(int x, int min, int max) {return (x*max)/min;}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// RND - Engine
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
int RND_Cont=0;
int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

public int RND(int Max)
{
	RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
	if (RND_Cont > 23) {RND_Cont=0;}
	return (((RND_Data[RND_Cont%5]>>RND_Cont) & 0xFF) * Max)>>8;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int GameExit=0;
boolean GamePaused;

long GameMillis, CPU_Milis;
int  GameSleep;


public void run()
{
	System.gc();
	GameINI(); System.gc();
	gc.CanvasSET(); System.gc();
	GameSET(); System.gc();

	while (GameExit==0)
	{
		if (!GamePaused)
		{
			GameMillis = System.currentTimeMillis();

			KeyboardRUN();

	try {
			GameRUN();

	CPU_Milis = System.currentTimeMillis();

			gc.CanvasPaint=true;
			gc.repaint();
			gc.serviceRepaints();

	} catch (Exception e) {
		e.printStackTrace();
		e.toString();
	}

			GameSleep=(Sleep-(int)(System.currentTimeMillis()-GameMillis));
			if (GameSleep<1) {GameSleep=1;}
		}

	//System.gc();

		try	{
		thread.sleep(GameSleep);
		} catch(java.lang.InterruptedException e) {}
	}

	GameEND();

	destroyApp(true);
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean Colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 > x1+xs1 ) ||	( x2+xs2 < x1 ) ||	( y2 > y1+ys1 ) || ( y2+ys2 < y1 ));
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// *******************
// -------------------
// Keyboard - Engine
// ===================
// *******************

int KeybX1, KeybY1, KeybB1, KeybM1;
int KeybX2, KeybY2, KeybB2, KeybM2;

// -------------------
// Keyboard RUN
// ===================

public void KeyboardRUN()
{
	KeybX2 = KeybX1;	// Keys del Frame Anterior
	KeybY2 = KeybY1;
	KeybB2 = KeybB1;
	KeybM2 = KeybM1;

	KeybX1 = gc.KeybX;	// Keys del Frame Actual
	KeybY1 = gc.KeybY;
	KeybB1 = gc.KeybB;
	KeybM1 = gc.KeybM;
}

// <=- <=- <=- <=- <=-


// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------






// *******************
// -------------------
// Game - Engine
// ===================
// *******************

int GameStatus=0;

// -------------------
// Game INI
// ===================

ITPlayer01 Player,Enemies[],Obstacles[];

int ActualLevel=0;

int TimeToChange,Sleep=40;

int PlayerSelected=0;

int[][] Items; //Type-X-Y

int GameSound=1,GameVibra=1,GameConti=0;

ITMap01 FaseMap;
byte[][] Cord = new byte[2][];
Image[] Img = new Image[2];
Image Scores;
int Combo=0;
int SelectorID,ActualSelectedID,LastLevel=9;

boolean LookColision=true;

String[][] Textos;

ITUtils01 ITU;
boolean Initialized = false, Move=true, LevelFinalized=false;

public void GameINI()
{
	ITU = new ITUtils01(this);
	Textos = ITU.textosCreate(ITU.LoadFile("/Textos.txt"));
	Items = new int[][] {};  
	ITU.PrefsINI();
}

// -------------------
// Game SET
// ===================

public void GameSET()
{
	ITU.ma = new Marco(gc.CanvasSizeX,gc.CanvasSizeY);
}

// -------------------
// Game END
// ===================

public void GameEND()
{
}

// -------------------
// Game RUN
// ===================

public void GameRUN()
{
	switch (GameStatus)
	{
	case 0: //Init
		GameStatus++;
		TimeToChange = 3*(1000/(Sleep));
		Move=true;
		Cord[0] = ITU.LoadFile("/Sel.cor");
		Img[0] = ITU.LoadImage("/Sel.png");
		Cord[1] = ITU.LoadFile("/Prot.cor");
		Img[1] = ITU.LoadImage("/Pjs.png");
		
		gc.BackGround[0] = ITU.LoadImage("/nokiaSeries40.png");
		gc.BackGround[1] = ITU.LoadImage("/caratula_001.png");
		gc.BackGround[2] = ITU.LoadImage("/entrafase.png");
		gc.BackGround[3] = ITU.LoadImage("/ganajuego_000.png");
		gc.BackGround[4] = ITU.LoadImage("/ganajuego_001.png");
		gc.BackGround[5] = ITU.LoadImage("/ganajuego_002.png");
		gc.BackGround[6] = ITU.LoadImage("/ganajuego_003.png");
		gc.BackGround[7] = ITU.LoadImage("/gameover.png");
		
		Scores = ITU.LoadImage("/scores.png");
		gc.TileSet = ITU.LoadImage("/maptiles.png");

		gc.ActBG = 0;
	break;

	case 1: //Logo
		if (TimeToChange>0)
		{
			TimeToChange--;
		} else
		{
			GameStatus++;
			TimeToChange = 5;
			//gc.BackGround = null;
			//System.gc();
			gc.ActBG = 1;
			gc.SoundRES();
			gc.SoundSET(0,0);
		}
	break;
	
	case 2: //Portada
		if (TimeToChange>0)
		{
			TimeToChange--;
		} else
		{
			gc.ActI = 0;
			ITU.AniSprINI();
			
			SelectorID = ITU.AniSprSET(-1, 24+4, 38+32, 0, 3, 2, 0x005);
			ActualSelectedID = ITU.AniSprSET(-1, 24+4, 38+65, 8, 2, 3, 0x005);
			if (KeybM1!=0) 
			{	
				ITU.PanelSET(0);
				TimeToChange = 5;
			}
		}
	break;
	
	case 3: //Historia
		ITU.PanelSET(9);
	break;
	
	case 4:
		if (TimeToChange>0)
		{
			TimeToChange--;
		} else
		{
			if ((KeybX1!=0)&&(KeybX2==0)) PlayerSelected += KeybX1;
			if (PlayerSelected<0) PlayerSelected = 3;
			if (PlayerSelected>3) PlayerSelected = 0;
		
			if (KeybM1>0) 
			{
				GameStatus = 5;
				TimeToChange = 5;
				ActualLevel = GameConti;
				gc.ActBG = 2;
				gc.SoundRES();
				gc.SoundSET(1,1);
			}
		}
	break;
	
	case 5: //LevelLoading
		if (TimeToChange>0)
		{
			TimeToChange--;
		} else
		{
			GameStatus = 6;
			TimeToChange = (1000/(Sleep));
			
			gc.CanvasPaint=true;
			gc.repaint();
			gc.serviceRepaints();	
		}
	break;
	
	
	case 6:
		ITU.AniSprEND();
		InitLevel();
		System.gc();		
		GameStatus = 50;
	break;
	
	case 50: //JuegoRUN
		if ((KeybM1 == -1)&&(KeybM2 != -1)) ITU.PanelSET(1); else JuegoRUN();
	break;
	
	case 70: //LevelWin
		ActualLevel++;
		GameConti = ActualLevel;
		if (GameConti > LastLevel) GameConti = 0;
		GameStatus = 75;
		gc.SoundRES();
		gc.SoundSET(2,1);
		
	case 75: //LevelStart
		if (TimeToChange>0)
		{
			TimeToChange--;
			JuegoRUN();
		} else
		{
			ITU.AniSprEND();
			Items = null;
			System.gc();
			if (ActualLevel > LastLevel) 
			{
				GameStatus = 80; 
			} else 
			{
				gc.ActBG = 2;
				GameStatus = 5;
			}
		}
	break;
	
	case 80: //GameWin
		FaseMap.Destroy();
		FaseMap = null;
		Items = null;
		Obstacles = null;
		gc.ActBG = (byte)(3+Player.Values[6]);
		Player = null;
		System.gc();
		TimeToChange = 2*(1000/(Sleep));
		GameStatus = 85;
		gc.SoundRES();
		gc.SoundSET(0,0);
	break;
	
	case 85:
		if (TimeToChange>0)
		{
			TimeToChange--;
		} else
		{
			GameStatus = 90;
			ITU.ma.BackGround = false;
			ITU.PanelSET(8);
			TimeToChange = 2*(1000/(Sleep));
		}
	break;
	
	case 90:
		if (TimeToChange>0)
		{
			TimeToChange--;
		} else
		{
			ITU.ma.BackGround = true;
			GameStatus = 2;
			TimeToChange = 2*(1000/(Sleep));
			Items = null;
			Obstacles = null;
			Player = null;
			System.gc();
			gc.ActBG = 1;
			gc.SoundRES();
			gc.SoundSET(0,0);
		}
	break;
	
	case 99:
		gc.SoundRES();
		gc.SoundSET(3,1);
		ITU.AniSprEND();
		FaseMap.Destroy();
		FaseMap = null;
		Items = null;
		Obstacles = null;
		Player = null;
		System.gc();
		gc.ActBG = 7;
		GameStatus = 100;
		TimeToChange = 2*(1000/(Sleep));
	break;
	case 100: //GameOver
		if (TimeToChange>0)
		{
			TimeToChange--;
		} else
		{
			TimeToChange = 2*(1000/(Sleep));
			GameStatus = 101;
		}
	break;
	case 101:
		GameStatus = 2;
		TimeToChange = 2*(1000/(Sleep));
		gc.ActBG = 1;
		gc.SoundRES();
		gc.SoundSET(0,0);
	break;
	
	
// ------------------
// Panel Bucle
// ------------------
	case 900:
		ITU.PanelRUN();
	break;
// ===================


	}
}

// <=- <=- <=- <=- <=-

int Paint = 2;


public void JuegoRUN()
{
	Move = true;
		
	if (LevelFinalized==false)
	{
		if (Paint<=0) Paint = 1; else Paint--;
		LookColision = !LookColision;
		for (int Counter=0; Counter<Obstacles.length;Counter++) Obstacles[Counter].Move();
		
		for (int Counter=0; Counter<Enemies.length;Counter++) Enemies[Counter].Move();
	
		Player.Move();
		
		if (Player.Y>=FaseMap.MH) LevelFinalized=true;
		
		int ActualPos=0;
		for (int Counter=0; Counter<Enemies.length;Counter++)
		{
			if (Player.Y < Enemies[Counter].Y) ActualPos++;
			if (Enemies[Counter].Y>=FaseMap.MH) LevelFinalized=true;
		}
		
		if (LevelFinalized)
		{
			TimeToChange = 40;
			gc.TimeToPaintText = 40;
			gc.TextStyle = 0;
		
			if (Player.Values[16] == 0) 
			{
				GameStatus = 70;
				gc.CanvasText = Textos[24][0];
			} else 
			{
				GameStatus = 75;
				gc.CanvasText = Textos[23][0];
			}
		}
		
		Player.Values[16] = ActualPos;
		
		for (int Counter=0; Counter<Enemies.length;Counter++)
		{
			ActualPos=0;
			for (int Counter2=0; Counter2<Enemies.length;Counter2++) if ((Enemies[Counter].Y < Enemies[Counter2].Y)||((Enemies[Counter].Y == Enemies[Counter2].Y)&&(Enemies[Counter].Values[15]>Enemies[Counter2].Values[15]))) ActualPos++;
			if (Enemies[Counter].Y <= Player.Y) ActualPos++;
			Enemies[Counter].Values[16] = ActualPos;
		}
	
		ITU.AniSprs[Player.SpriteID].Prio = 10 - (2 * Player.Values[16]);
		for (int Counter=0; Counter<Enemies.length; Counter++) 
	  	  ITU.AniSprs[Enemies[Counter].SpriteID].Prio = 10 - (2 * Enemies[Counter].Values[16]); 	
	}
}

public boolean In(int X1,int Y1,int X2,int Y2,int X3,int Y3,int X4,int Y4)
{
	return (!((X2<X3)||(X1>X4)||(Y2<Y3)||(Y1>Y4)));
}

public int WhoIn(int W, int X, int Y, int SX, int SY)
{
	if (LevelFinalized) return 100;
	
	SX = (SX*12)/8;
	SY = (SY*12)/8;
	
	//Item Colision
	for (int Counter=0; Counter<Items.length; Counter++)
	{
		if ((Items[Counter][0]!=0)&&(In(X,FaseMap.MH-Y,X+SX,FaseMap.MH-(Y)+SY,Items[Counter][1]+((Items[Counter][0]==1)?5:1),Items[Counter][2]+((Items[Counter][0]==1)?2:3),Items[Counter][1]+((((Items[Counter][0]==1)?17:13))*12)/8,Items[Counter][2]+((((Items[Counter][0]==1)?14:16))*12)/8)))
	  	{ 
	  		int Ret = 50+Items[Counter][0];
	  		if (W==0)
	  		{
	  			if ((Player.Values[0]!=4)&&(Player.Values[0]!=8)) Items[Counter][0] = 0;
	  		} else
	  		{
	  			if ((Enemies[W-1].Values[0]!=4)&&(Enemies[W-1].Values[0]!=8)) Items[Counter][0] = 0;
	  		}
	  		return Ret;
	 	}
	}
	
	if (!LookColision) return 100;
	//Sword Atacks
	if (W>0)
	{
		for (int Counter=Enemies.length; Counter>=0; Counter--)
		{
			if (Counter>0)
			{
				if ((W!=Counter)&&(Enemies[Counter-1].Values[0]==3)&&(Enemies[Counter-1].TimeToChange<=1)&&(In(X,Y,X+SX,Y+SY,Enemies[Counter-1].X-((10)*12)/8,Enemies[Counter-1].Y+2,Enemies[Counter-1].X,Enemies[Counter-1].Y+((15)*12)/8))) return 23;
				if ((W!=Counter)&&(Enemies[Counter-1].Values[0]==2)&&(Enemies[Counter-1].TimeToChange<=1)&&(In(X,Y,X+SX,Y+SY,Enemies[Counter-1].X,Enemies[Counter-1].Y+2,Enemies[Counter-1].X+((10)*12)/8,Enemies[Counter-1].Y+((15)*12)/8))) return 22;
			} else
			{
				if ((Player.Values[0]==3)&&(Player.TimeToChange<=1)&&(In(X,Y,X+SX,Y+SY,Player.X-((10)*12)/8,Player.Y+2,Player.X,Player.Y+((15)*12)/8)))	return 23;
				if ((Player.Values[0]==2)&&(Player.TimeToChange<=1)&&(In(X,Y,X+SX,Y+SY,Player.X,Player.Y+2,Player.X+((10)*12)/8,Player.Y+((15)*12)/8)))	return 22;
			}
		}
	} else
	{
		for (int Counter=0; Counter<Enemies.length+1; Counter++)
		{
			if (Counter>0)
			{
				if ((Enemies[Counter-1].Values[0]==3)&&(Enemies[Counter-1].TimeToChange<=1)&&(In(X,Y,X+SX,Y+SY,Enemies[Counter-1].X-((10)*12)/8,Enemies[Counter-1].Y+2,Enemies[Counter-1].X,Enemies[Counter-1].Y+((15)*12)/8))) return 23;
				if ((Enemies[Counter-1].Values[0]==2)&&(Enemies[Counter-1].TimeToChange<=1)&&(In(X,Y,X+SX,Y+SY,Enemies[Counter-1].X,Enemies[Counter-1].Y+2,Enemies[Counter-1].X+((10)*12)/8,Enemies[Counter-1].Y+((15)*12)/8))) return 22;
			} 
		}
	}
		 
	//Obstacles Colision
	if ((W<10)||(W>13))
	{
		for (int Counter=0; Counter<Obstacles.length; Counter++)
		{
			switch (Obstacles[Counter].Values[6])
			{
				case 12:
					if (In(X,Y,X+SX,Y+SY,Obstacles[Counter].X+((3)*12)/8,Obstacles[Counter].Y+((10)*12)/8,Obstacles[Counter].X+((17)*12)/8,Obstacles[Counter].Y+((20)*12)/8))
					{
						Obstacles[Counter].TimeToChange = 0;
						return (Obstacles[Counter].Values[6]);
					}
				break;
				case 13:
					if (In(X,Y,X+SX,Y+SY,Obstacles[Counter].X,Obstacles[Counter].Y+((5)*12)/8,Obstacles[Counter].X+((20)*12)/8,Obstacles[Counter].Y+((18)*12)/8))
					{
						return (Obstacles[Counter].Values[6]);
					}
				break;
				case 10:
					if (In(X,Y,X+SX,Y+SY,Obstacles[Counter].X,Obstacles[Counter].Y,Obstacles[Counter].X+((20)*12)/8,Obstacles[Counter].Y+((20)*12)/8))
					{
						if (Obstacles[Counter].Values[0]!=0) return (Obstacles[Counter].Values[6]);
					}
				break;
				case 11:
					if (In(X,Y,X+SX,Y+SY,Obstacles[Counter].X,Obstacles[Counter].Y,Obstacles[Counter].X+((20)*12)/8,Obstacles[Counter].Y+((20)*12)/8))
					{
						if (Obstacles[Counter].Values[0]!=0) return (Obstacles[Counter].Values[6]);
					}
				break;
			}
		}
	}

	//Enemies Colision
	if (W>0)
	{
		for (int Counter=Enemies.length; Counter>=0; Counter--)
		{
			/*if (Counter>0)
			{
				if ((W!=Counter)&&(Enemies[Counter-1].Values[0]!=4)&&(In(X,Y,X+SX,Y+SY,Enemies[Counter-1].X-5,Enemies[Counter-1].Y+7,Enemies[Counter-1].X+5,Enemies[Counter-1].Y+15))) return Counter;
			} else
			{*/
				if (((((W==12)&&(Player.TimeToChange<=1)))||(Player.Values[0]!=4))&&(In(X,Y,X+SX,Y+SY,Player.X-((5)*12)/8,Player.Y+((10)*12)/8,Player.X+((5)*12)/8,Player.Y+((15)*12)/8))) return 0;			
			//}
		}
	} else
	{
		for (int Counter=0; Counter<Enemies.length+1; Counter++)
		{
			if (Counter>0)
			{
				if ((((W==12)&&(Enemies[Counter-1].TimeToChange<=1))||(Enemies[Counter-1].Values[0]!=4))&&(In(X,Y,X+SX,Y+SY,Enemies[Counter-1].X-((5)*12)/8,Enemies[Counter-1].Y+((10)*12)/8,Enemies[Counter-1].X+((5)*12)/8,Enemies[Counter-1].Y+((15)*12)/8))) return Counter;
			} 
		}
	}
	return (100);
}

public void InitLevel()
{
	int[] InitValues  = new int[17];
	short[] CTiles,CTiles2;
	byte[] Levels;
	int[][] ObstaclesGeneration;
	int[][] AItems;

	Items = null;
	Obstacles = null;
	Player = null;
	Enemies = null;
	if (FaseMap != null) 
	{
		FaseMap.Destroy();
		FaseMap = null;
	}
	
	System.gc();
	
	CTiles = new short[] {1,6,7,17,22,33,34,37,38,42,49,50,53,54,65,66,78,81,82,85,86,94,95,96,98,101,102,103,114,117};
	CTiles2 = new short[] {68,70,71,108,109,110,111,124,125,126,127};
	
	// ITEM DEFINITION {1,8*(4),64*(5)+8*(1)},{2,8*(6),64*(13)+8*(4)},{1,8*(5),64*(22)+8*(2)}
	// OBSTACLE DEFINITION {0,8*5,64*2,0},{1,890,0},{2,8*5,64*4},{3,890,0}
	Levels = ITU.LoadFile("/stage"+(ActualLevel+1)+".map");
	switch (ActualLevel)
	{
		case 0 :
			Enemies = new ITPlayer01[0];
			AItems = new int[][] {{1,50,1500},{2,50,2450}};  
			ObstaclesGeneration = new int[][] {{0,45,1870,0},{1,2600,0},{2,45,1135},{2,50,4150},{3,600,0}};
		break;
		case 1 :
			Enemies = new ITPlayer01[1];
			AItems = new int[][] {{2,35,1930},{1,50,3150},{2,35,4600}};  
			ObstaclesGeneration = new int[][] {{1,1050,0},{1,2475,0},{2,45,2220},{2,45,3460},{3,420,0}};
		break;
		case 2 :
			Enemies = new ITPlayer01[1];
			AItems = new int[][] {{2,50,420},{1,30,1120},{2,25,2550},{1,35,4770}};  
			ObstaclesGeneration = new int[][] {{2,45,400},{2,8*5,64*4},{2,20,3160},{3,600,0},{3,1650,0},{3,2000,40},{3,2550,0},{3,3000,80},{3,3660,0}};
		break;
		case 3 :
			Enemies = new ITPlayer01[2];
			AItems = new int[][] {{1,25,930},{2,25,1920},{2,30,2760},{1,70,3275}};  
			ObstaclesGeneration = new int[][] {{0,45,270,0},{0,45,2440,45},{0,20,2470,0},{0,45,2550,90},{0,35,4690,0},{0,45,340,20},{0,20,1120,0},{1,550,0},{1,810,0},{2,45,1520},{2,45,1660},{2,45,3440},{3,2240,0},{3,4980,0}};
		break;
		case 4 :
			Enemies = new ITPlayer01[2];
			AItems = new int[][] {{2,30,630},{1,25,1240},{2,25,2370},{1,30,3955}};  
			ObstaclesGeneration = new int[][] {{0,45,240,0},{0,20,350,40},{1,860,0},{2,45,4420},{3,1000,0},{3,1610,0},{3,1650,60}};
		break;
		case 5 :
			Enemies = new ITPlayer01[2];
			AItems = new int[][] {{2,60,560},{1,30,1125},{2,70,1470},{1,25,3000}};  
			ObstaclesGeneration = new int[][] {{0,45,170,0},{0,45,4270,0},{0,20,4315,0},{1,480,0},{1,2020,0},{1,8820,0},{1,4530,0},{2,45,440},{3,820,0},{3,860,30},{3,890,0}};
		break;
		case 6 :
			Enemies = new ITPlayer01[2];
			AItems = new int[][] {{1,36,2420},{2,70,1130}};  
			ObstaclesGeneration = new int[][] {{0,45,450,0},{0,45,480,40},{0,40,2860,0},{1,1000,0},{1,1250,0},{1,1270,30},{1,2020,0},{1,4840,0},{2,35,3800},{2,55,3800},{3,1950,0}};
		break;
		case 7 :
			Enemies = new ITPlayer01[3];
			AItems = new int[][] {{2,36,1800},{1,50,2410}};  
			ObstaclesGeneration = new int[][] {{0,40,3000,0},{0,35,4350,0},{0,55,4370,90},{0,35,4390,45},{1,1000,0},{1,1200,0},{1,1210,10},{1,2720,0},{1,2920,0},{2,35,2360},{2,55,2360},{2,35,2460},{2,55,2460},{2,45,3720},{3,900,0},{3,1700,0},{3,2720,0}};
		break;
		case 8 :
			Enemies = new ITPlayer01[3];
			AItems = new int[][] {{1,50,2225},{1,50,3765}};  
			ObstaclesGeneration = new int[][] {{0,35,350,0},{0,55,370,45},{0,35,390,90},{0,55,2760,0},{1,3295,0},{1,3305,20},{1,3315,40},{1,3680,0},{2,50,3750},{3,1800,0},{3,2180,0},{3,2210,30},{3,2250,60}};
		break;
		case 9 :
			Enemies = new ITPlayer01[3];
			AItems = new int[][] {};  
			ObstaclesGeneration = new int[][] {{0,45,1360,0},{0,45,2650,0},{0,45,2680,80},{1,2025,0},{1,2280,30},{1,2920,95},{1,2930,115},{1,2975,0},{1,2985,25},{1,2995,50},{1,3180,0},{1,4270,0},{2,45,515},{2,50,4950},{2,20,4950},{2,35,4950},{3,900,0},{3,1850,0},{3,1880,25},{3,1910,50},{3,1940,100},{3,3740,75},{3,4250,0}};
		break;
		default :
			Enemies = new ITPlayer01[0];
			AItems = new int[][] {};  
			ObstaclesGeneration = new int[][] {};
		break;
	}
	
	
	
	FaseMap = new ITMap01(this,14,8*Levels.length,"/recta001.map",Levels);
	FaseMap.Img = gc.TileSet;
	FaseMap.InitColision(CTiles);
	FaseMap.InitColision2(CTiles2);
	FaseMap.InitGraph();
	
	for (int Counter=0; Counter<AItems.length; Counter++) AItems[Counter][1] = (12*AItems[Counter][1])/8;
	for (int Counter=0; Counter<AItems.length; Counter++) AItems[Counter][2] = (FaseMap.MH-((AItems[Counter][2]*12)/8));
	Items = AItems;
	AItems = null;
	Levels = null;
	System.gc();

	
	ITU.AniSprINI();

	
	gc.ActI = 1;
	
	InitValues[0] = 0; 			// State
	InitValues[1] = 0;			// Velocity
	InitValues[2] = 100; 			// Life
	InitValues[3] = 0; 			// Jumping Y
	InitValues[4] = 0; 			// Vel Y
	InitValues[5] = 5; 			// Sand Focus
	InitValues[6] = PlayerSelected; 	// PlayerType
	InitValues[7] = 0; 			// 0-Controllable/Type of AI
	InitValues[8] = FaseMap.StartX; 	// Posicion X inicial
	InitValues[9] = FaseMap.StartY; 	// Posicion Y inicial
	InitValues[10] = 25; 			// Atack Damage
	InitValues[11] = 15;			// Max Velocity
	InitValues[12] = 1;			// Acceleration
	InitValues[13] = 0; 			// Direccion (Enemis Only)
	InitValues[14] = 0;			// Last Used State
	InitValues[15] = 0;			// ID
	InitValues[16] = 0;			// Actual Pos
	
	
	Player  = new ITPlayer01(this,InitValues);
	Player.TimeToFall = 3;
	
	int Counter2=0;
	int FileCounter=0;
	int FilaActual=0;
	for (int Counter=0; Counter<Enemies.length;Counter++)
	{
		if ((Counter%3)==0) FileCounter++;
		if ((Counter+Counter2)%4==Player.Values[6]) Counter2++;
		InitValues[2] = 100 + 10*(ActualLevel+1); 			// Life
		InitValues[5] = 3;
		InitValues[6] = (Counter+Counter2)%4;
		InitValues[7] = 1; 
		FilaActual = 0;
		if ((Enemies.length-((FileCounter-1)*3))>1) FilaActual++;
		if ((Enemies.length-((FileCounter-1)*3))>2) FilaActual++;
		InitValues[8] = ((32+(1+(Counter%3))*(56/(2+FilaActual)))*12)/8-10; 
		InitValues[9] = ((Player.Values[9]+(FileCounter*22))*12)/8-10; 
		InitValues[10] = 20-(3*(Counter%3)); 			
		InitValues[11] = 10+(Counter%3);
		InitValues[12] = 2+(Counter%2); 
		InitValues[13] = 0;
		InitValues[14] = 0;
		InitValues[15] = Counter+1;
		InitValues[16] = Counter;
		
		Enemies[Counter] = new ITPlayer01(this,InitValues);
		
		Enemies[Counter].TimeToFall = 3;
	}
	
	Player.Values[16] = Enemies.length;
	
	Obstacles = new ITPlayer01[ObstaclesGeneration.length];
	for (int Counter=0; Counter<Obstacles.length;Counter++)
	{
		InitValues[0] = 0; 			// State
		InitValues[1] = 0;			// Velocity
		InitValues[2] = 100; 			// Life
		InitValues[3] = 0; 			// Jumping Y
		InitValues[4] = 0; 			// Vel Y
		InitValues[5] = 0; 			// Sand Focus
		InitValues[6] = ObstaclesGeneration[Counter][0]+10;// PlayerType
		InitValues[7] = ObstaclesGeneration[Counter][0]+10;// 0-Controllable/Type of AI
		InitValues[13] = 0; 			// Direccion (Enemis Only)
		switch (ObstaclesGeneration[Counter][0])
		{
			case 0 :			
			case 2 :
				InitValues[8] = (ObstaclesGeneration[Counter][1]*12)/8; 	// Posicion X inicial
				InitValues[9] = (ObstaclesGeneration[Counter][2]*12)/8; 	// Posicion Y inicial
			break;
			case 1 :
				InitValues[8] = (-5*12)/8; 	// Posicion X inicial
				InitValues[9] = (ObstaclesGeneration[Counter][1]*12)/8+10; 	// Posicion Y inicial
			break;
			case 3 :
				InitValues[8] = (60*12)/8; 	// Posicion X inicial
				InitValues[9] = (ObstaclesGeneration[Counter][1]*12)/8; 	// Posicion Y inicial
				InitValues[13] = 1; 			// Direccion (Enemis Only)
			break;
		}
		InitValues[10] = 0; 			// Atack Damage
		InitValues[11] = 0; 			// Max Velocity
		InitValues[12] = 0; 			// Acceleration
		InitValues[14] = 0;			// Last Used State
		InitValues[15] = Counter+1+Enemies.length;// ID
		InitValues[16] = 0;			// Actual Pos
		
		Obstacles[Counter] = new ITPlayer01(this,InitValues);
		
		if ((ObstaclesGeneration[Counter][0]==1)||(ObstaclesGeneration[Counter][0]==3))
			 Obstacles[Counter].TimeToChange = ObstaclesGeneration[Counter][2];
		if (ObstaclesGeneration[Counter][0]==0)
			 Obstacles[Counter].TimeToChange = ObstaclesGeneration[Counter][3];
	}	
	
	gc.CanvasText = Textos[16][0];
	gc.TimeToPaintText = 20;
	gc.TextStyle = 0;
	ObstaclesGeneration = null;
	System.gc();
	LevelFinalized = false;
}
// <=- <=- <=- <=- <=-

}