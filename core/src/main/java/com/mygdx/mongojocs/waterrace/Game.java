package com.mygdx.mongojocs.waterrace;// -----------------------------------------------
// Game_Base v3.0 Rev.4 (2.10.2003)
// ===============================================
// Programado por Juan Antonio G�mez
// ------------------------------------




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextBox;
import com.mygdx.mongojocs.midletemu.TextField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Game extends MIDlet implements Runnable, CommandListener {

	GameCanvas gc;
	Thread thread;

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Game() {
		System.gc();

		gc = new GameCanvas(this);

		System.gc();

		Display.getDisplay(this).setCurrent(gc);

		thread=new Thread(this);
		System.gc();
		thread.start();
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// startApp - Al inicio y cada vez que se hizo pauseApp()
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public void startApp() {
		if(GameStatus == 201) {
			Display.getDisplay(this).setCurrent(nameBox);
		} else {
			Display.getDisplay(this).setCurrent(gc);
		}
		GamePaused = false;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// pauseApp - Cada vez que se PAUSA el MIDlet
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public void pauseApp() {
		GamePaused=true;
		if(GameStatus/100 == 1) {
			PanelSET(1);
			lastPausedTime = currentTimeGame;
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// destroyApp - Para DESTRUIR el MIDlet
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public void destroyApp (boolean b) {
		GameExit=1;
		PrefsSET();
		notifyDestroyed();
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// Regla de 3
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public int Regla3(int x, int min, int max) {
		return (x*max)/min;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// RND - Engine
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	int RND_Cont=0;
	int RND_Seed = 0;
	int[] RND_Data = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};

	public int RND(int Max) {
		RND_Data[RND_Cont%5] ^= RND_Data[++RND_Cont%5];
		if (RND_Cont > 23) {
			RND_Cont=0;
		}
		return ((((RND_Data[RND_Cont%5]>>RND_Cont) ^ RND_Seed) & 0xFF) * Max)>>8;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// run - Thread que creamos para CORRER el MIDlet
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	int GameExit = 0;
	boolean GamePaused;

	long GameMilis, CPU_Milis;
	int  GameSleep;


	public void run() {

		System.gc();
		Textos = textosCreate(LoadFile("/textos.txt"));
		System.gc();
		NameINI();

		GameINI();
		System.gc();
		gc.CanvasSET();
		System.gc();
		GameSET();
		System.gc();


		while (GameExit==0) {
			if (!GamePaused) {
				GameMilis = System.currentTimeMillis();

				KeyboardRUN();

				//	try {
				GameRUN();

				CPU_Milis = System.currentTimeMillis();

				gc.CanvasPaint=true;
				gc.repaint();
				gc.serviceRepaints();

				gc.SoundRUN();

				//	} catch (Exception e) { e.printStackTrace(); e.toString(); }

				GameSleep=(ftime-(int)(System.currentTimeMillis()-GameMilis));
				if (GameSleep<1) {
					GameSleep=1;
				}
			}

			//	System.gc();

			try	{
				thread.sleep(GameSleep);
			} catch(java.lang.InterruptedException e) {}
		}

		GameEND();

		destroyApp(true);
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public byte[] LoadFile(String Nombre) {
		/*System.gc();

		InputStream is = getClass().getResourceAsStream(Nombre);

		byte[] buffer = null;

		try {
			int Size = 0;
			while (true) {
				int Desp = (int) is.skip(1024);
				if (Desp <= 0) {
					break;
				}
				Size += Desp;
			}

			is = null;
			System.gc();

			buffer = new byte[Size];

			is = getClass().getResourceAsStream(Nombre);
			Size = is.read(buffer, 0, buffer.length);

			while (Size < buffer.length) {
				Size += is.read(buffer, Size, buffer.length-Size);
			}

			is.close();
		} catch(Exception exception) {}

		System.gc();

		return buffer;*/

		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
		byte[] bytes = file.readBytes();

		return bytes;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public byte[] LoadFile(String Nombre, int Pos, short[] Size) {
		System.gc();
		InputStream is = getClass().getResourceAsStream(Nombre);

		byte[] Dest = new byte[Size[Pos]];

		try {
			for (int i=0 ; i<Pos ; i++) {
				is.skip(Size[i]);
			}
			is.read(Dest, 0, Dest.length);
			is.close();
		} catch(Exception e) {}
		System.gc();
		return Dest;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public void LoadFile(short[] buffer, String Nombre) {
		System.gc();
		InputStream is = getClass().getResourceAsStream(Nombre);

		try	{
			for (int i=0 ; i<buffer.length ; i++) {
				buffer[i]  = (short) (is.read() << 8);
				buffer[i] |= is.read();
			}
			is.close();
		} catch(Exception exception) {}
		System.gc();
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// Colision
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public boolean Colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2) {
		return !(( y2 > y1+ys1 ) || ( y2+ys2 < y1 ) || ( x2 > x1+xs1 ) ||	( x2+xs2 < x1 ));
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

	private void KeyboardRUN() {
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

	int GameSound = 1;
	int GameVibra = 1;

	// *******************
	// -------------------
	// Game - Engine
	// ===================
	// *******************

	int GameStatus = -5; // -1 = load game

	// -------------------
	// Game INI
	// ===================

	private void GameINI() {
		ma = new Marco(gc.CanvasSizeX, gc.CanvasSizeY);

		for(int count = 0; count < nplayers; count++)
			player[count] = new Moto();

	}

	// -------------------
	// Game SET
	// ===================

private void GameSET() {}


	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// Game END
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



















	private void GameEND() {
		gc.SoundRES();
	}

	// -------------------
	// Game RUN
	// ===================

	private void GameRUN() {

		switch (GameStatus / 100) {
		case 0: // Caratula/Carga del juego (Sprites)
			if(GameStatus == -1) {
				gc.GameLoad();
				PrefsINI();
				setSound(0,0);
			}

			if(GameStatus >= 0) {
				gc.caratulaDraw = true;

				if (
					(KeybM1 != 0 && KeybM1 != KeybM2) ||
					(KeybX1 != 0 && KeybX1 != KeybX2)||
					(KeybY1 != 0 && KeybY1 != KeybY2)||
					(KeybB1 != 0 && KeybB1 != KeybB2) ) {
					GameStatus = 1;
				}

				if(GameStatus == 1) {
					PanelSET(0);
				}

			} else {
				GameStatus++;
			}
			break;

		case 9:	// Menu (900/100)
			gc.menuDraw = true;
			PanelRUN();
			break;

		case 1: // Juego (100/100)
			gc.juegoDraw = true;
			if (KeybM1==-1 && KeybM2==0) {
				PanelSET(1);
				lastPausedTime = currentTimeGame;
				break;
			} else
				JuegoRUN();
			break;

		case 2:	// EnterName
			if(GameStatus == 200) {
				NameSET();
				GameStatus = 201;
			}
			break;

		case 3:	// Ver tiempos
			gc.scoresDraw = true;
			gc.CanvasFillPaint = true;
			gc.CanvasFillRGB = 0x1f1f7f;
			if (
				(KeybM1 != 0 && KeybM1 != KeybM2) ||
				(KeybX1 != 0 && KeybX1 != KeybX2)||
				(KeybY1 != 0 && KeybY1 != KeybY2)||
				(KeybB1 != 0 && KeybB1 != KeybB2) ) {
				gc.caratulaDraw = true;
				gc.scoresDraw = false;
				PanelSET(10);
			}
			break;
		}
	}

	// <=- <=- <=- <=- <=-


	int lastPosition = 0;

	static final int fps = 10;
	static final int ftime = 1000 / fps;


	int nFichas8;

	String banner = null;

	int nplayers = 4;

	Moto player[] = new Moto[nplayers];
	byte[] faseCol;

	int frameCounter;

	long timeGameInit;
	long lastPausedTime;
	long currentTimeGame;
	long currentTimeMilis;


	private void JuegoRUN() {
		switch(GameStatus) {
		case 100:
			JuegoRUN_Load();
			break;

		case 101:
			JuegoRUN_Start();
			break;

		case 102:
			JuegoRUN_Waiting();
			break;

		case 103:
			JuegoRUN_Playing();
			break;

		case 104:
			JuegoRUN_Finished();
			break;

		case 105:
			JuegoRUN_Dead();
			break;

		case 106:
			JuegoRUN_Gameover();
			break;

		case 107:
			JuegoRUN_Continue();
			break;

		case 108:
			JuegoRUN_End();
			break;
			/*
				case 109:
					JuegoRUN_EnterName();
				break;
			*/
		case 110:
			JuegoRUN_ViewScores();
			break;
		}
	}


	private void initPlayers() {
		// inicializar nplayers
		if(nplayers > 4) {
			for(int count = 0; count < nplayers; count++) {
				player[count].init(56 + (count - nplayers / 2)*5, gc.faseStartY - 16 + (count%2)*20, (count+(count/3)), (count != 0), this, gc.currentStage);
			}
		} else {
			for(int count = 0; count < nplayers; count++) {
				player[count].init(56 + (count - nplayers / 2)*10, gc.faseStartY, (count+(count/3)), (count != 0), this, gc.currentStage);
			}
		}
	}


	// 100= Loading
	public void JuegoRUN_Load() {
		gc.juegoDraw = false;

		gc.JuegoLoad();
		GameStatus = 101;
	}


	// 101= Starting
	public void JuegoRUN_Start() {
		frameCounter = 0;
		timeGameInit = 0;
		currentTimeGame = 0;
		lastPausedTime = 0;

		lastPosition = 0;


		currentTimeGame = GameMilis;
		timeGameInit = currentTimeGame;
		GameStatus = 102;

		initPlayers();
	}


	// 102= Waiting
	public void JuegoRUN_Waiting() {
		if(frameCounter == 0) {
			setSound(1,1);
		}

		frameCounter++;

		currentTimeGame = GameMilis;
		timeGameInit = currentTimeGame;

		int tmp = 3 - frameCounter / fps;
		if( tmp > 0 && tmp <= 3)  {
			banner = Integer.toString(tmp);
		}

		if(frameCounter > 3*fps) {
			GameStatus = 103;
			frameCounter = 0;
		}
	}


	// 103= Playing
	public void JuegoRUN_Playing() {
		currentTimeGame = GameMilis;

		int st = 0;

		for(int count = 0; count < nplayers; count++) {
			if ( !player[count].isComputer ) {
				st = player[count].movePlayer(this);
			} else {
				player[count].moveComputer(this);
			}
		}

		switch(st) {
		case 1:  // muerto
			GameStatus = 105;
			break;
		case 2:  // final
			GameStatus = 104;
			break;
		}

		if ((currentTimeGame - timeGameInit) >= 100000) {
			GameStatus = 104;
		}
	}


	// 104= Finished
	public void JuegoRUN_Finished() {
		currentTimeGame = GameMilis;
		frameCounter++;

		if ((currentTimeGame - timeGameInit) >= 100000 && (player[gc.nplayer].pos <= 1)) {
			if ( (frameCounter > 4*fps) && (
						( KeybM1 != 0 && KeybM1 != KeybM2 ) ||
						( KeybX1 != 0 && KeybX1 != KeybX2 ) ||
						( KeybY1 != 0 && KeybY1 != KeybY2 ) ||
						( KeybB1 != 0 && KeybB1 != KeybB2 )
					) ) {
				GameStatus = 101; // start game
			}

			banner = Textos[this.TEXT_TIMEOUT][0];
			return;
		}


		if(frameCounter < 2*fps) {
			banner = Textos[TEXT_FINISH][0];
		} else {
			if(frameCounter < 4*fps) {
				if(player[gc.nplayer].pos == 1) {
					banner = Textos[TEXT_CONGRATULATIONS][0];
				} else {
					banner = Textos[TEXT_SORRY][0];
				}
			}
		}

		if ( (frameCounter > 4*fps) && (
					( KeybM1 != 0 && KeybM1 != KeybM2 ) ||
					( KeybX1 != 0 && KeybX1 != KeybX2 ) ||
					( KeybY1 != 0 && KeybY1 != KeybY2 ) ||
					( KeybB1 != 0 && KeybB1 != KeybB2 )
				) ) {
			frameCounter = 0;
			if(player[gc.nplayer].pos == 1) {
				if(PrefsCompScore(gc.currentLevel + gc.currentStage*5, player[gc.nplayer].ftime, true)) {
					GameStatus = 200;
				} else {
					gc.nextLevel = (gc.currentLevel+1)%5;
					if(gc.nextLevel == 0) {
						gc.nextStage = (gc.currentStage+1)%3;
					}
					GameStatus = 100; // next level
					gc.loadingDraw = true;
				}
			} else {
				GameStatus = 101; // start game
			}
		}

		for(int count = 0; count < nplayers; count++) {
			player[count].moveComputer(this);
		}
	}


	// 105= Dead
	public void JuegoRUN_Dead() {
		frameCounter++;

		currentTimeGame = GameMilis;

		banner = Textos[TEXT_FALL][0];

		if(frameCounter > fps) {
			GameStatus = 107;
			frameCounter = 0;
			player[gc.nplayer].setBlinks(currentTimeGame);
		}

		for(int count = 0; count < nplayers; count++) {
			if ( player[count].isComputer ) {
				player[count].moveComputer(this);
			}
		}
	}


	// 106= Gameover
	public void JuegoRUN_Gameover() {
		frameCounter++;

		banner = Textos[TEXT_TIMEOUT][0];

		if(frameCounter > fps*3) {
			GameStatus = 108;
			frameCounter = 0;
		}
	}


	// 107= Continue
	public void JuegoRUN_Continue() {
		frameCounter++;

		currentTimeGame = GameMilis;
		boolean col = false;

		for(int count = 0; count < nplayers; count++) {
			Moto p = player[count];
			if ( !p.isComputer ) {
				p.stat = 0;
				col = collides(p.x/8 - 4, p.y/8 - 10, 9, 15);

				if(col) {
					if(p.x/4 != 112) {
						p.x -= (p.x > 112*4)? 4: -4;
					}
					p.y += 6;

					p.vy = 0;
					p.vx = 0;

					if(!collides(p.x/8 - 10, p.y/8 - 10, 9, 15)) {
						p.x -= 12;
					} else {
						if(!collides(p.x/8 + 10, p.y/8 - 10, 9, 15))
							p.x += 12;
					}
				}

				if( playerCollides(p, p.x/8, p.y/8) != null ) {
					col = true;
				}

				if(!col) {
					p.movePlayer(this);
				}
			} else {
				p.moveComputer(this);
			}
		}

		if(!col) {
			GameStatus = 103;
			frameCounter = 0;
		}

	}


	// 108= End
	public void JuegoRUN_End() {
		gc.JuegoUnload(); // descargamos
		gc.juegoDraw = false;
		GameStatus = 0;// volvemos al menu principal (esperando)
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// 110= ViewScores
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
















































	public void JuegoRUN_ViewScores() {
		gc.scoresDraw = true;
		if (
			( KeybM1 != 0 && KeybM1 != KeybM2 ) ||
			( KeybX1 != 0 && KeybX1 != KeybX2 ) ||
			( KeybY1 != 0 && KeybY1 != KeybY2 ) ||
			( KeybB1 != 0 && KeybB1 != KeybB2 )
		) {
			gc.nextLevel = (gc.currentLevel+1)%5;
			if(gc.nextLevel == 0) {
				gc.nextStage = (gc.currentStage+1)%3;
			}
			GameStatus = 100;
			gc.loadingDraw = true;
		}
	}

	// <=- <=- <=- <=- <=-


	public void setVibra(int t) {
		if(GameVibra == 1) {
			gc.VibraSET(t);
		}
	}


	public void setSound(int t, int c) {
		if(GameSound == 1) {
			gc.SoundSET(t, c);
		}
	}


	public int tileOf (int x, int y) {
		x >>= 3;
		y >>= 3;

		if(x >= 14 || y >= (nFichas8) || x < 0 || y < 0) {
			return 1;
		}

		return faseCol[y*14 + x];
	}


	public boolean collides(int x, int y) {
		int tile = tileOf(x, y);

		if(tile == 1)
			return true;

		if(tile == 0)
			return false;

		/*  int x8 = x & 7;
		  int y8 = y & 7;
		  
		  switch(tile) {
		    // 45�
		    case 16:
		      if(x8 >= (7 - y8))
		        return true;
		      break;
		    // 135�
		    case 17:
		      if(x8 < y8)
		        return true;
		      break;
		    // 45�
		    case 32:
		      if(x8 >= y8)
		        return true;
		      break;
		    // 45�
		    case 33:
		      if(x8 >= (7 - y8))
		        return true;
		      break;
		*/
		/*
		    // 60�
		    case 2:
		    case 18:
		      if(x8 >= 4)
		        return true;
		      break;
		    case 34:
		    case 50:
		      return true;
		      
		    case 66:
		    case 82:
		      if(x8 >= 4)
		        return true;
		      break;
		      
		    case 3:
		    case 19:
		      if(x8 >= 4)
		        return true;
		      break;

		    case 35:
		    case 51:
		      return true;
		      
		    case 67:
		    case 83:
		      if(x8 >= 4)
		        return true;
		      break;
		*/
		/*
		  }
		*/
		return false;
	}


	public boolean fastCollides(int x, int y) {
		int tile = tileOf(x, y);

		if(tile != 0)
			return true;

		return false;
	}


	public Moto playerCollides(Moto me, int x, int y) {
		for(int count = 0; count < nplayers; count++) {
			if(me != player[count]) {
				if(Colision(x, y, 7, 13, player[count].x>>3, player[count].y>>3, 7, 13) && (player[count].stat < 3))
					return player[count];
			}
		}

		return null;
	}


	public boolean collides(int x, int y, int sx, int sy) {
		boolean col = false;

		sx += x;
		sy += y;

		for(int cx = x + 8; cx <= sx && !col; cx += 8) {
			col |= fastCollides(cx,  y);
		}

		if((sx - x) > 16) {
			int centerx = (x + sx) >> 1;
			for(int cy = y + 8; cy <= sy && !col; cy += 16) {
				col |= fastCollides(centerx, cy);
			}
		}

		for(int cy = y; cy < sy && !col; cy += 8) {
			col |= collides(x,  cy);
			col |= collides(sx, cy);
		}

		return col;
	}



	// *******************
	// -------------------
	// Panel - Engine
	// ===================
	// *******************

	Marco ma;

	int PanelStatus;
	int PanelType;
	int PanelTypeOld;

	// -------------------
	// Panel SET
	// ===================

	public void PanelSET(int Type) {
		String[] text;
		PanelStatus = GameStatus;
		GameStatus = 900;

		PanelTypeOld = PanelType;
		PanelType = Type;

		switch (Type) {
		case 0:
			ma.MarcoINI();
			ma.MarcoADD(0x000,Textos[TEXT_PLAY][0],0);
			ma.MarcoADD(0x000,Textos[TEXT_SELECT_CIRC][0],14);
			//		ma.MarcoADD(0x000,"(D) Introducir nombre",16);
			if (gc.DeviceSound) {
				ma.MarcoADD(0x000,Textos[TEXT_SOUND],10, GameSound);
			}
			if (gc.DeviceVibra) {
				ma.MarcoADD(0x000,Textos[TEXT_VIBRA],11, GameVibra);
			}
			ma.MarcoADD(0x000,Textos[TEXT_VIEWSCORES][0],19);
			ma.MarcoADD(0x000,Textos[TEXT_HELP][0],6);
			ma.MarcoADD(0x000,Textos[TEXT_ABOUT][0],7);
			ma.MarcoADD(0x000,Textos[TEXT_EXIT][0],9);
			ma.MarcoSET_Option();
			break;

		case 1:
			ma.MarcoINI();
			ma.MarcoADD(0x000,Textos[TEXT_CONTINUE][0],1);
			ma.MarcoADD(0x000,Textos[TEXT_RESTART][0],2);
			//		ma.MarcoADD(0x000,"(D) Introducir nombre",16);
			// DEBUG
			//		ma.MarcoADD(0x000,"(D) Siguiente nivel",5);
			//		ma.MarcoADD(0x000,"(D) Siguiente fase",4);
			if (gc.DeviceSound) {
				ma.MarcoADD(0x000,Textos[TEXT_SOUND],10, GameSound);
			}
			if (gc.DeviceVibra) {
				ma.MarcoADD(0x000,Textos[TEXT_VIBRA],11, GameVibra);
			}
			ma.MarcoADD(0x000,Textos[TEXT_HELP][0],6);
			ma.MarcoADD(0x000,Textos[TEXT_BACK][0],8);
			ma.MarcoADD(0x000,Textos[TEXT_EXIT][0],9);
			ma.MarcoSET_Option();
			break;

		case 6:
			ma.MarcoINI();

			text = Textos[TEXT_HELP_SCROLL];

			for(int count = 0; count < text.length; count++) {
				int dato = 0;
				String str = text[count];

				if(str.endsWith(":")) {
					dato = 0x010;
				}

				if(count == 0) {
					dato = 0x111;
				}

				if(str.length() == 0)
					str = " ";

				ma.MarcoFIX(dato, str);
			}
			ma.MarcoSET_Scroll(0);
			System.gc();
			break;

		case 7:
			ma.MarcoINI();
			text = Textos[TEXT_ABOUT_SCROLL];

			for(int count = 0; count < text.length; count++) {
				int dato = 0;
				String str = text[count];

				if(str.endsWith(":")) {
					dato = 0x010;
				}

				if(count == 0) {
					dato = 0x111;
				}

				if(str.length() == 0)
					str = " ";

				ma.MarcoFIX(dato, str);
			}
			ma.MarcoSET_Scroll(0);
			System.gc();
			break;

		case 8:
			ma.MarcoINI();
			ma.MarcoADD(0x000,Textos[TEXT_PLAY][0],15);
			ma.MarcoADD(0x000,Textos[TEXT_DIFFICULTY],12, gc.nextStage);
			ma.MarcoADD(0x000,Textos[TEXT_CIRCUIT],13, gc.nextLevel);
			ma.MarcoADD(0x000,Textos[TEXT_BACK][0],8);
			ma.MarcoSET_Option();
			break;

		case 9:
			ma.MarcoINI();
			ma.MarcoADD(0x000,Textos[TEXT_INTRODUCENAME][0],16);
			ma.MarcoADD(0x000,Textos[TEXT_ACCEPTNAME][0] + " (" + name + ")",17);
			ma.MarcoSET_Option();
			break;

		case 10:
			ma.MarcoINI();
			ma.MarcoADD(0x000,Textos[TEXT_VIEWSCORES][0],18);
			ma.MarcoADD(0x000,Textos[TEXT_DIFFICULTY],12, gc.nextStage);
			ma.MarcoADD(0x000,Textos[TEXT_BACK][0],8);
			ma.MarcoSET_Option();
			break;
		}

	}

	// -------------------
	// Panel END
	// ===================

	public void PanelEND() {
		ma.MarcoEND();
		GameStatus = PanelStatus;
	}


	// -------------------
	// Panel RUN
	// ===================

	public void PanelRUN() {
		int MarcoKey=0;
		if (KeybY1==-1 && KeybY2!=-1) {
			MarcoKey=2;
		}
		if (KeybY1== 1 && KeybY2!= 1) {
			MarcoKey=8;
		}
		if (KeybM1!= 0 && KeybM2== 0) {
			MarcoKey=5;
		}

		int Result = ma.MarcoRUN(MarcoKey, KeybY1);
		PanelEXE(Result);
	}

	// ---------------
	// Panel EXE
	// ===============

	public void PanelEXE(int CMD) {
		switch (CMD) {
		case -2: // Scroll ha llegado al final
			PanelEND();
			if(GameStatus == 0 || GameStatus == 1) {
				gc.caratulaDraw = true;
			} else {
				gc.juegoDraw = true;
			}
			PanelSET(PanelTypeOld);
			break;

		case 0:	// Jugar de 0
			PanelEND();
			RND_Seed = (int)GameMilis;
			gc.nextStage = 0;
			gc.nextLevel = 0;
			GameStatus = 100;
			gc.loadingDraw = true;

			break;

		case 1:	// Continuar
			timeGameInit += GameMilis - lastPausedTime;
			PanelEND();
			break;

		case 2:	// Empezar de nuevo
			PanelEND();
			GameStatus = 101;
			break;

		case 4:	// Siguiente Stage
			PanelEND();
			gc.nextLevel = 0;
			gc.nextStage = (gc.currentStage+1)%3;
			GameStatus = 100;
			gc.loadingDraw = true;
			break;

		case 5:	// Siguiente Nivel
			PanelEND();
			gc.nextLevel = (gc.currentLevel+1)%5;
			if(gc.nextLevel == 0) {
				gc.nextStage = (gc.currentStage+1)%3;
			}
			GameStatus = 100;
			gc.loadingDraw = true;
			break;

		case 6:	// Controles...
		case 7:	// About...
			PanelEND();
			if(GameStatus == 0 || GameStatus == 1) {
				gc.caratulaDraw = true;
			} else {
				gc.juegoDraw = true;
			}
			PanelSET(CMD);
			break;

		case 8:	// Menu
			PanelEND();
			JuegoRUN_End();
			setSound(0,0);
			GameStatus = 1;
			break;

		case 9:	// Exit Game
			PanelEND();
			JuegoRUN_End();
			GameExit = 1;
			break;

		case 10:  // Sound
			GameSound = ma.getOption();
			if(GameSound != 1) {
				gc.SoundRES();
			} else {
				setSound(0, ((PanelType == 0)?0:1));
			}
			break;

		case 11:  // Vibra
			GameVibra = ma.getOption();
			break;

		case 12:  // nextStage
			gc.nextStage = ma.getOption();
			break;

		case 13:  // nextLevel
			gc.nextLevel = ma.getOption();
			break;

		case 14:
			PanelEND();
			gc.caratulaDraw = true;
			PanelSET(8);
			break;

		case 15:	// Jugar
			PanelEND();
			RND_Seed = (int)GameMilis;
			GameStatus = 100;
			gc.loadingDraw = true;
			break;
			/*
				case 16:	// Entrar Nombre (Debug) 
					PanelEND();
			    GameStatus = 200;
			  break;
			*/
		case 17:	// Aceptar el nombre actual
			PanelEND();
			PrefsNewScore();
			GameStatus = 110;
			break;

		case 18:	// Ver tiempos en el stage actual
			PanelEND();
			gc.currentStage = gc.nextStage;
			if(gc.currentStage < 0 || gc.currentStage > 2) {
				gc.currentStage = 0;
			}
			if(gc.currentLevel < 0 || gc.currentLevel > 4) {
				gc.currentLevel = 0;
			}
			GameStatus = 300;
			break;

		case 19:	// Ver tiempos
			PanelEND();
			gc.caratulaDraw = true;
			PanelSET(10);
			break;
		}

	}

	// <=- <=- <=- <=- <=-


	// -------------------
	// Name Engine
	// ===================


	TextBox nameBox;

	String name = "NONAME";
	Command cmdOK;

	public void commandAction(Command c, Displayable d) {
		if(c == cmdOK) {
			name = nameBox.getString();
			PrefsNewScore();

			Display.getDisplay(this).setCurrent(gc);

			GameStatus = 110;
		}
	}

	// -------------------
	// NameINI
	// ===================

	public void NameINI() {
		nameBox = new TextBox(Textos[TEXT_NAME][0], name, 8, TextField.ANY);
		nameBox.setCommandListener(this);

		cmdOK = new Command(Textos[TEXT_OK][0], Command.SCREEN, 1);
		nameBox.addCommand(cmdOK);

		System.gc();
	}

	// -------------------
	// Name SET
	// ===================

	public void NameSET() {
		Display.getDisplay(this).setCurrent(nameBox);
	}



	// -------------------
	// Name RES
	// ===================

	public void NameRES() {
		nameBox = null;
		cmdOK = null;
	}

	// <=- <=- <=- <=- <=-

	// *******************
	// -------------------
	// Prefs - Engine
	// ===================
	// *******************


	String[] scoreData = new String[30];


	// -------------------
	// Prefs INI
	// ===================

	public void PrefsINI() {
		for(int count = 0; count < 15; count++) {
			scoreData[count*2] = name;
			scoreData[count*2+1] = "1000";
		}


		byte data[] = null;

		try {
			RecordStore rs = RecordStore.openRecordStore("Prefs", false);
			data = rs.getRecord(1);
			rs.closeRecordStore();
		} catch (Exception rse) { }

		if(data != null) {
			//		System.out.println("Leyendo prefs");
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(data);

				// Realizar checksum
				byte checksum = 0;
				for(int count = 0; count < data.length; count++) {
					checksum+=data[count];
				}

				/*System.out.println(checksum);*/

				if(checksum != 7) {
					//System.out.println("Error de checksum");
					return;
				}

				// Leemos datos
				DataInputStream is = new DataInputStream(bais);

				is.readByte();

				GameSound = is.readByte();
				GameVibra = is.readByte();

				for(int i = 0; i < 15; i++) {
					scoreData[i*2] = is.readUTF();
					scoreData[i*2+1] = String.valueOf(is.readInt());
				}
				is.close();

			} catch(IOException e) {
				/*
				System.out.println("Error leyendo prefs");
				*/
			}
		}

		data = null;
		System.gc();
	}


	// -------------------
	// Prefs SET
	// ===================

	public void PrefsSET() {
		try {
			RecordStore.deleteRecordStore("Prefs");
		} catch (Exception rse) {}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(400);

			DataOutputStream os = new DataOutputStream(baos);

			os.writeByte(0);

			os.writeByte(GameSound);
			os.writeByte(GameVibra);

			for(int i = 0; i < 15; i++) {
				os.writeUTF(scoreData[i*2]);
				os.writeInt(Integer.parseInt(scoreData[i*2+1]));
			}
			os.close();

			byte[] data = baos.toByteArray();

			// Realizar checksum
			byte checksum = 0;
			for(int count = 0; count < data.length; count++) {
				checksum += data[count];
			}
			checksum = (byte)(7 - checksum);
			data[0] = checksum;

			/* System.out.println((byte)checksum); */

			RecordStore rs = RecordStore.openRecordStore("Prefs", true);
			rs.addRecord(data, 0, data.length);
			rs.closeRecordStore();

			/* System.out.println(data.length + " bytes salvados"); */
		} catch(IOException e) {
			/*
			System.out.println("Error salvando prefs");
			*/
		}
		catch (Exception rse) {
			/*
			System.out.println(rse);
			rse.printStackTrace();
			*/
		}
	}

	// -------------------
	// Prefs NewScore
	// ===================

	/*
	 *   Intenta a�adir un score nuevo a la tabla de scores y la salva de 
	 * forma automatica si se ha superado el ultimo score
	 *
	 * - Parametros: 
	 *    cat: categoria (0 representaria la primer stage/circuito)
	 *    name: nombre del jugador
	 *    score: puntuacion 
	 *    tim: tim (true tiempo, false puntos)
	 *
	 * - Devuelve: verdadero si se ha logrado alcanzar un nuevo record
	 */
	public boolean PrefsNewScore(int cat, String name, int score, boolean Asc) {
		if( PrefsCompScore(cat, score, Asc) ) {
			scoreData[cat*2    ] = name;
			scoreData[cat*2 + 1] = Integer.toString(score/100);

			PrefsSET();

			return true;
		}

		return false;
	}


	public void PrefsNewScore() {
		if(gc.currentStage >= 0 && gc.currentStage <= 2) {
			int cat = gc.currentLevel + gc.currentStage*5;
			PrefsNewScore(cat, name, player[gc.nplayer].ftime, true);
		}
	}


	public boolean PrefsCompScore(int cat, int score, boolean Asc) {
		if( cat >= 0 &&
				cat <= 14 ) {
			if(Asc) { // Ascendente

				if(score < (Integer.parseInt(scoreData[cat*2 + 1])*100)) {
					return true;
				}
			}
		}

		return false;
	}

	// <=- <=- <=- <=- <=-


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	String[][] Textos;


	// *******************
	// -------------------
	// textos - Engine - v1.0 - Rev.0 (28.01.2004)
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
	final static int TEXT_SELECT_CIRC = 13;
	final static int TEXT_VIEWSCORES = 14;
	final static int TEXT_BACK = 15;
	final static int TEXT_PRESSANY = 16;
	final static int TEXT_FALL = 17;
	final static int TEXT_FINISH = 18;
	final static int TEXT_TIMEOUT = 19;
	final static int TEXT_CONGRATULATIONS = 20;
	final static int TEXT_SORRY = 21;
	final static int TEXT_TORESTART = 22;
	final static int TEXT_TONEXT = 23;
	final static int TEXT_TONAME = 24;
	final static int TEXT_NAME = 25;
	final static int TEXT_INTRODUCENAME = 26;
	final static int TEXT_ACCEPTNAME = 27;
	final static int TEXT_PLAYER = 28;
	final static int TEXT_CPU = 29;
	final static int TEXT_OK = 30;
	final static int TEXT_DIFFICULTY = 31;
	final static int TEXT_CIRCUIT = 32;










	// -------------------
	// textos Create
	// ===================

	String[][] textosCreate(byte[] tex) {
		char textos[];
		int dataPos = 0;
		int dataBak = 0;
		short[] data = new short[1024];

		boolean campo = false;
		boolean subCampo = false;
		boolean primerEnter = true;
		int size = 0;

		// MONGOFIX ==========================================
		char tex_char[] = new char[tex.length];
		for(int i = 0; i < tex.length; i++)
			if(tex[i] < 0)
				tex_char[i]=(char)(tex[i]+256);
			else
				tex_char[i]=(char)tex[i];
		//=================================================


		String tmpstr = new String(tex_char);

		textos = tmpstr.toCharArray();

		tmpstr = null;
		System.gc();


		for (int i=0 ; i<textos.length ; i++) {
			if (campo) {
				if (textos[i] == 0x7D) {
					subCampo = false;
					campo = true;
				}

				if (textos[i] < 0x20 || textos[i] == 0x7C || textos[i] == 0x7D)	// Buscamos cuando Termina un campo
				{
					data[dataPos+1] = (short) (i - data[dataPos]);
					dataPos+=2;
					campo=false;
				}

			} else {

				if (textos[i] == 0x7D) {
					subCampo = false;
					continue;
				}

				if (textos[i] == 0x7B) {
					dataBak = dataPos;
					data[dataPos++] = 0;
					campo = false;
					subCampo = true;
					size++;
					continue;
				}

				if (subCampo && textos[i] == 0x0A) {
					if (primerEnter) {
						primerEnter = false;
					} else {
						data[dataPos++] = (short) i;
						data[dataPos++] = 1;
						if (!subCampo) {
							size++;
						} else {
							data[dataBak]--;
						}
					}
					continue;
				}

				if (textos[i] >= 0x20)	// Buscamos cuando Empieza un campo nuevo
				{
					campo=true;
					data[dataPos] = (short) i;
					if (!subCampo) {
						size++;
					} else {
						data[dataBak]--;
					}
					primerEnter = true;
				}
			}
		}

		String[][] strings = new String[size][];

		dataPos=0;

		for (int i=0 ; i<size ; i++) {
			int num = data[dataPos];

			if (num<0) {
				num*=-1;
				dataPos++;
			} else {
				num = 1;
			}

			strings[i] = new String[num];

			for (int t=0 ; t<num; t++) {
				strings[i][t] = new String(textos, data[dataPos++], data[dataPos++]).trim();
			}
		}

		System.gc();
		return strings;
	}

	// <=- <=- <=- <=- <=-


	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// ----------------------------------------------------------------------------

	// *******************
	// -------------------
	// ===================
	// *******************

	// -------------------
	// ===================

	// <=- <=- <=- <=- <=-


	// **************************************************************************//
} 	// Final Clase MIDlet
// **************************************************************************//
