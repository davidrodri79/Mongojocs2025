package com.mygdx.mongojocs.hotspeed;// -----------------------------------------------
// MICRO-ROAD iMode v0.1 Rev.1 (11.12.2003)
// ===============================================
// iMode
// ------------------------------------


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.IApplication;

import java.io.InputStream;

public class Game extends IApplication {

	GameCanvas gc;


	// estados de juego
	public static final int EJ_INICIO 			= 1;
	public static final int EJ_MENU 			= 2;
	public static final int EJ_JUEGO 			= 3;
	public static final int EJ_COLISION 		= 4;
	public static final int EJ_GAMEOVER 		= 5;
	public static final int EJ_PRESENTACION 	= 6;
	public static final int EJ_SELECCION	 	= 7;
	public static final int EJ_FASE_COMPLETADA	= 8;
	public static final int EJ_FASE_INICIO		= 9;
	public static final int EJ_FASE_INICIO_2	= 10;
	public static final int EJ_FASE_INICIO_3	= 11;
	public static final int EJ_SIN_FUEL			= 12;
	public static final int EJ_MENU_1			= 132;
	public static final int EJ_MENU_2			= 13;
	public static final int EJ_CONGRATULATIONS	= 14;
	public static final int EJ_CERDITOS			= 69;
	public static final int EJ_AYUDA			= 666;
	
	public Prota PROTA;
	
	public Scroll scroll;
	Marco marco;
	boolean marcoReady = false;


	public static final int GS_SOUND			= 0;
	public static final int	GS_VIBRATION		= 1;
	public static final int GS_LAST_LEVEL		= 2;
	public static final int GS_DIFICULT			= 3;


	public static final int SFX_MAIN_THEME		= 0;
	public static final int SFX_SIG_NIVEL		= 1;
	public static final int SFX_META			= 2;
	public static final int SFX_LUZ_ROJA		= 3;
	public static final int SFX_LUZ_VERDE		= 4;
	public static final int SFX_COLISION		= 5;
	public static final int SFX_PATINAR			= 6;


byte GAMESETTINGS[]; 

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()
{
	
	System.gc();
	gc = new GameCanvas(this);
	System.gc();
	
	PROTA = new Prota(this,gc);
	marco = new Marco(gc.CanvasSizeX,gc.CanvasSizeY/*130*/);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// start
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void start()
{
	Display.setCurrent(gc);


	for(int i=0;i<NUM_ENEMIGOS;ID_ENEMIGOS[i++]=-1);
	for(int i=0;i<NUM_ITEMS;ID_ITEMS[i++]=-1);

	run();
}

public void _start()
{
	Display.setCurrent(gc);


	for(int i=0;i<NUM_ENEMIGOS;ID_ENEMIGOS[i++]=-1);
	for(int i=0;i<NUM_ITEMS;ID_ITEMS[i++]=-1);

	//run();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	GameExit=1;
	terminate();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int GameExit=0;

long GameMilis;
int  GameSleep;

static long gameCounter = 0;


public void run()
{
	_start();

	System.gc();
	/*GameINI(); System.gc();*/
	GameStatus = EJ_PRESENTACION;
	
	gc.CanvasSET(-1); System.gc();
	GameSET(); System.gc();

	while (GameExit==0)
	{
		GameMilis = System.currentTimeMillis();

		gameCounter++;
		KeyboardRUN();

		GameRUN();

		gc.CanvasPaint=true;
		gc.repaint();

		GameSleep=(60-(int)(System.currentTimeMillis()-GameMilis));
		if (GameSleep<1) {GameSleep=1;}

		
		//while (gc.CanvasPaint==true) {try	{Thread.sleep(1);} catch(java.lang.InterruptedException e) {}}
		
		
		try	{
		Thread.sleep(GameSleep);
		} catch(java.lang.InterruptedException e) {}
		
	}

	GameEND();

	destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

void LoadFile(byte[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
	is.read(buffer, 0, buffer.length);
	is.close();
	}catch(Exception exception) {}
	System.gc();
}

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

// *******************
// -------------------
// Game - Engine
// ===================
// *******************

int GameX;
int GameY;
int GameSizeX;
int GameSizeY;
int GameMaxX=176;	// 176
int GameMaxY=208;	// 208

public int GameStatus=0;

int GameSound = 1;
int GameVibra = 1;
int GameLevel = 0;

int GameMode;

String[][] TEXTO = textosCreate(LoadFile("/Textos.txt"));

// -------------------
// Game INI
// ===================

public void GameINI(int nivel)
{
	GameSizeX=gc.CanvasSizeX; if (GameSizeX <= GameMaxX) {GameX=0;} else {GameX=(GameSizeX-GameMaxX)/2;GameSizeX=GameMaxX;}
	GameSizeY=gc.CanvasSizeY; if (GameSizeY <= GameMaxY) {GameY=0;} else {GameY=(GameSizeY-GameMaxY)/2;GameSizeY=GameMaxY;}

	PROTA.reset();
	destroyAll();
	
	gc.CanvasSET(nivel);
}

// -------------------
// Game END
// ===================

public void GameEND()
{
	gc.FS_SaveFile(0,0,GAMESETTINGS);
}

// -------------------
// Game SET
// ===================

public void GameSET()
{
}



// -------------------
// Game RUN
// ===================

int NIVEL = 1;
int Y;
int COUNT = 52;
int[] oldSettings = {0,0}; //0:sonido; 1:vibreixon (no usado)

boolean sem = false;
int lastGameStatus = -1;

long GameTicks = 0;

public void GameRUN() {
	
	GameTicks++;

	if(gc.bImpTransicion) return;
	
	switch (GameStatus) {
		
		case EJ_PRESENTACION:
			
			if(GAMESETTINGS[GS_SOUND] == 1 && !sem) {
				gc.SoundRES();
				gc.SoundSET(SFX_MAIN_THEME,0);
				sem = true;
			}
			if(KeybB1 != 0 || KeybM1 != 0) {
				GameStatus = EJ_MENU;
				sem = false;
			}
			gc.bImpCaratula = true;
			marcoReady = false;
			break;
	
		case EJ_MENU: 

			if(!marcoReady) {
				marco.MarcoINI();
				marco.MarcoADD(0x011,TEXTO[TEXT_PLAY][0],1);				
				marco.MarcoADD(0x011,TEXTO[TEXT_OPTIONS][0],2);
				marco.MarcoADD(0x011,TEXTO[TEXT_ABOUT][0],3);
				marco.MarcoADD(0x011,TEXTO[TEXT_HELP][0],4);
				marco.MarcoADD(0x011,TEXTO[TEXT_EXIT][0],0);
				marco.MarcoSET_Option();
				marcoReady = true;
				gc.bImpDesplazamiento = false;
				
				gc.bImpCaratula = true;
				
			} else {
				//gc.bImpCaratula = true;
				int MarcoKey=0;
				if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
				if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
				if (KeybB1== 5 && KeybB2!= 5) {MarcoKey=5;}

				switch(marco.MarcoRUN(MarcoKey, gc.KeybY)) {
					case 0:
						GameExit=1;
						marco.MarcoEND();
						break;
					case 1:
						gc.bImpTransicion = true;
						GameStatus = EJ_INICIO;
						marcoReady = false;
						marco.MarcoEND();
						System.gc();
						break;
					case 2: //opciones
						GameStatus = EJ_MENU_1;
						marcoReady = false;
						marco.MarcoEND();
						break;
					case 3:
						GameStatus = EJ_CERDITOS;
						marcoReady = false;
						marco.MarcoEND();
						break;
					case 4:
						GameStatus = EJ_AYUDA;
						marcoReady = false;
						marco.MarcoEND();
						break;

				}
				
				gc.bImpMarco = true;
			}
			break;

	case EJ_MENU_1: 

			if(!marcoReady) {
				marco.MarcoINI();
				marco.MarcoADD(0x011,new String[] {TEXTO[TEXT_SOUND][0],TEXTO[TEXT_SOUND][1]},1, 
													  GAMESETTINGS[GS_SOUND]);
 				marco.MarcoADD(0x011,new String[] {TEXTO[TEXT_VIBRA][0],TEXTO[TEXT_VIBRA][1]},2,
 													  GAMESETTINGS[GS_VIBRATION]);
				marco.MarcoADD(0x011,new String[] {	TEXTO[TEXT_DIFICULTY][0],
													TEXTO[TEXT_DIFICULTY][1],
													TEXTO[TEXT_DIFICULTY][2]},3,
													GAMESETTINGS[GS_DIFICULT]);
				marco.MarcoADD(0x011,TEXTO[TEXT_PREVIOUS][0],0);
				marco.MarcoSET_Option();
				marcoReady = true;
				
				oldSettings[0] = GAMESETTINGS[GS_SOUND];
			
				gc.bImpCaratula = true;
				
			} else {
				int MarcoKey=0;
				if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
				if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
				if (KeybB1== 5 && KeybB2!= 5) {MarcoKey=5;}

				switch(marco.MarcoRUN(MarcoKey, gc.KeybY)) {
					case 0:
						GameStatus = EJ_MENU; //EJ_SELECCION;
						marcoReady = false;
						marco.MarcoEND();
						break;
					case 1: //sonido
						GAMESETTINGS[GS_SOUND] = (byte) marco.getOption();
						if(oldSettings[0] != GAMESETTINGS[GS_SOUND]) {
							oldSettings[0] = GAMESETTINGS[GS_SOUND];
							if(GAMESETTINGS[GS_SOUND] == 0) gc.SoundRES();
							else gc.SoundSET(SFX_MAIN_THEME,0);
						}		
						break;
					case 2: //vibracion
						GAMESETTINGS[GS_VIBRATION] = (byte) marco.getOption();
						break;
					case 3: //dificultad
						GAMESETTINGS[GS_DIFICULT] = (byte) marco.getOption();
						break;
				}
				gc.bImpMarco = true;
				oldSettings[0] = GAMESETTINGS[GS_SOUND];

			}
			break;


		case EJ_MENU_2: 

			if(!marcoReady) {
				marco.MarcoINI();
				marco.MarcoADD(0x011,TEXTO[TEXT_CONTINUE][0],1);
				marco.MarcoADD(0x011,new String[] {TEXTO[TEXT_SOUND][0],TEXTO[TEXT_SOUND][1]},2, 
													  GAMESETTINGS[GS_SOUND]);
 				marco.MarcoADD(0x011,new String[] {TEXTO[TEXT_VIBRA][0],TEXTO[TEXT_VIBRA][1]},3,
 													  GAMESETTINGS[GS_VIBRATION]);
				marco.MarcoADD(0x011,TEXTO[TEXT_RESTART][0],0);
				marco.MarcoADD(0x011,TEXTO[TEXT_EXIT][0],10);
				marco.MarcoSET_Option();
				marcoReady = true;
				oldSettings[0] = GAMESETTINGS[GS_SOUND];
				
			} else {
				int MarcoKey=0;
				if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
				if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
				if (KeybB1== 5 && KeybB2!= 5) {MarcoKey=5;}

				switch(marco.MarcoRUN(MarcoKey, gc.KeybY)) {
					case 0:
						GameStatus = EJ_GAMEOVER;
						marcoReady = false;
						marco.MarcoEND();
						gc.finalizarScroll();
						break;
					case 2: //sound
						GAMESETTINGS[GS_SOUND] = (byte) marco.getOption();
						if(oldSettings[0] != GAMESETTINGS[GS_SOUND]) {
							oldSettings[0] = GAMESETTINGS[GS_SOUND];
							if(GAMESETTINGS[GS_SOUND] == 0) gc.SoundRES();
						}		
						break;
					case 3: //vibracion
						GAMESETTINGS[GS_VIBRATION] = (byte) marco.getOption();
						break;
					case 10:
						GameExit=1;
						marco.MarcoEND();
						break;
					case 1:
						//GameStatus = EJ_JUEGO;
						if(lastGameStatus > 0) GameStatus = lastGameStatus;
						else GameStatus = EJ_JUEGO;

						marcoReady = false;
						marco.MarcoEND();
						break;
				}
				
				gc.bImpMarco = true;
				oldSettings[0] = GAMESETTINGS[GS_SOUND];
			}
			break;

		case EJ_INICIO: 
			//gc.bImpDesplazamiento = true;
			GameINI(NIVEL);
			
			prepararItems();
			
			GameStatus = EJ_FASE_INICIO;
			//Y = (8*(172-/*6*/6))*8; //NEC
			Y = (8*(172-6))*8-50;

			COUNT = 52;
//	ProgBarINI(100,5, /*14*/31,/*100*/150 ,false);			
			//gc.ProgBarINI(5,100, 116,14,true);
			gc.ProgBarINI(5,100, 135,40,true);
			if(GAMESETTINGS[GS_SOUND] == 1) {
				gc.SoundRES();
				gc.SoundSET(SFX_SIG_NIVEL,1);
			}

			break;
		
		
		case EJ_FASE_INICIO:
			
			COUNT -=2;
			if(COUNT<-26) {
				destroyAll();
				GameStatus = EJ_FASE_INICIO_2;
				gc.bImpSlider = true;
				gc.preparaScroll(); //eliminar imagenes de memoria (bug tsm30)
				gc.bImpTransicion = true;
				if(GAMESETTINGS[GS_SOUND] == 1) {
					gc.SoundRES();
				}

			} else gc.bImpCambioFase = true;
			break;
		
		case EJ_FASE_INICIO_2:
			
			generarTraficoSalida();		
			
			gc.bImpScroll = true; 

			prepararTraficoSalida(Y);
			gc.bImpObjetos = true;

			gc.bImpMarcador = true;
			
//			if(Y >= (8*(172-/*2*/4))*8) { //NEC
			if(Y >= (8*(172-2))*8-50) {

				gc.bImpSlider = false;
				gc.bImpSemaforo = true;
				//if(GAMESETTINGS[GS_SOUND] == 1) gc.SoundSET(SFX_LUZ_ROJA,1);
				COUNT -= 2;
				if(COUNT<-81) {
					GameStatus = EJ_FASE_INICIO_3;
					scroll.ScrollRUN(0,(8*(172-2))*8-50);
				}
			} else {
				Y += 8;	
				scroll.ScrollRUN(0,Y);
				if(KeybM1==-1 && KeybM1!=KeybM2) {
					lastGameStatus = EJ_FASE_INICIO_2;
					GameStatus = EJ_MENU_2;
				}
			}
			
			break;
			
		case EJ_FASE_INICIO_3:
			//System.out.println("en fase inicio 3");
			//if(GAMESETTINGS[GS_SOUND] == 1) gc.SoundSET(SFX_LUZ_ROJA,1);
			//generarTraficoSalida();		
			moverTraficoSalida();
			PROTA.RUN();
			
			if(PROTA.realY() < ((172)-8)*64) {				
				GameStatus = EJ_JUEGO;
				destroyTraficoSalida();
				//if(GAMESETTINGS[GS_SOUND] == 1) gc.SoundSET(SFX_LUZ_VERDE,1);
			} else {
				if(KeybM1==-1 && KeybM1!=KeybM2) {
					lastGameStatus = EJ_FASE_INICIO_3;
					GameStatus = EJ_MENU_2;
				}

			}	
		 	gc.bImpObjetos = true;
			gc.bImpMarcador = true;
			gc.bImpScroll = true;
			gc.bImpSemaforo = true;

			break;
					
		case EJ_JUEGO: //System.out.println("ESTADO JUEGO: JUEGO");
			
			PROTA.RUN();
			
			generarTrafico(false); //items
			generarTrafico(true); //enemigos

			
			moverTrafico();
			
			if(KeybM1==-1 && KeybM1!=KeybM2) {
				lastGameStatus = EJ_JUEGO;
				GameStatus = EJ_MENU_2;
			}

			if(PROTA.sinCombustible()) GameStatus = EJ_SIN_FUEL;
			if(PROTA.realY() < 16*8) {
				GameStatus = EJ_FASE_COMPLETADA;
				if(GAMESETTINGS[GS_SOUND] == 1) {
					gc.SoundRES();
					gc.SoundSET(Game.SFX_META,1);
				}
			}
			gc.bImpObjetos = true;
			gc.bImpMarcador = true;
			gc.bImpScroll = true;
			break;
		
		case EJ_SIN_FUEL:
			gc.bImpObjetos = true;
			gc.bImpMarcador = true;
			gc.bImpScroll = true;
			gc.bImpTexto = true;
			
			PROTA.RUN();
			moverTrafico();
			
			//ultima oportunidad...
			if(PROTA.realY() < 16*8) {
				GameStatus = EJ_FASE_COMPLETADA;
				if(GAMESETTINGS[GS_SOUND] == 1) {
					gc.SoundRES();
					gc.SoundSET(Game.SFX_META,1);
				}
			}

			
			if(PROTA.dy == PROTA.CTE_VELOCIDAD_MINIMA) {
				GameStatus = EJ_GAMEOVER;
				gc.finalizarScroll();
			}
		
			break;
			
		case EJ_GAMEOVER: //System.out.println("ESTADO JUEGO: GAMEOVER");
			
			if(KeybB1 != 0 && KeybB2 == 0) {
				GameStatus = EJ_PRESENTACION;
				gc.bImpTransicion = true;
				NIVEL = 1; 
			} else gc.bImpCaratula = true;	
			break;

		case  EJ_FASE_COMPLETADA:
			if(PROTA.realY() > -40) {
				PROTA.RUN();
				moverTrafico();
			
				gc.bImpObjetos = true;
				gc.bImpMarcador = true;
				gc.bImpScroll = true;
				gc.bImpTexto = true;
			
			} else {
				gc.bImpTransicion = true;
		
				GameStatus = EJ_INICIO;
				NIVEL++;
				if(NIVEL > 4) {
					NIVEL = 1; //idem...
					GameStatus = EJ_CONGRATULATIONS;
				}
				gc.finalizarScroll();
			}
			break;
		
		case EJ_CONGRATULATIONS:
	
			if(KeybB1 != 0 && KeybB2 == 0) {
				GameStatus = EJ_PRESENTACION;
				gc.bImpTransicion = true;
				gc.iImpTransicionTipo = RND(1);
			} else gc.bImpCaratula = true;
			
			break;
		
		case EJ_CERDITOS:
			if(!marcoReady) {
				gc.bImpCaratula = true;
				marco.MarcoINI();
				for(int i=0;i<TEXTO[TEXT_ABOUT_SCROLL].length;marco.MarcoFIX(0x011,TEXTO[TEXT_ABOUT_SCROLL][i++]));
				marco.MarcoSET_Scroll(-2);
				marcoReady = true;
			} else {
				int MarcoKey=0;
				if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
				if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
				if (KeybB1== 5 && KeybB2!= 5) {MarcoKey=5;}

				if(marco.MarcoRUN(0, KeybY1) == -2 || MarcoKey==5) {
					GameStatus = EJ_MENU; //EJ_PRESENTACION;
					marcoReady = false;
					marco.MarcoEND();
				}
				
				gc.bImpMarco = true;
			}
			break;

		case EJ_AYUDA:
			if(!marcoReady) {
				gc.bImpCaratula = true;
				marco.MarcoINI();
				for(int i=0;i<TEXTO[11].length;marco.MarcoFIX(0x010,TEXTO[11][i++]));
				marco.MarcoSET_Scroll(-2);
				marcoReady = true;
			} else {
				int MarcoKey=0;
				if (KeybY1==-1 && KeybY2!=-1) {MarcoKey=2;}
				if (KeybY1== 1 && KeybY2!= 1) {MarcoKey=8;}
				if (KeybB1== 5 && KeybB2!= 5) {MarcoKey=5;}
				
				if(marco.MarcoRUN(0, KeybY1) == -2  || MarcoKey==5) {
					GameStatus = EJ_MENU; //EJ_PRESENTACION;
					marcoReady = false;
					marco.MarcoEND();
				}
				
				gc.bImpMarco = true;
			}
			break;


	} //mega-switch(GameStatus)

}


// <=- <=- <=- <=- <=-


/******************************************************************************
 *	GENERACION/DESTRUCCION DE ENEMIGOS
 ******************************************************************************/

public static final int NUM_ENEMIGOS = 4	;
public static final int NUM_ITEMS = 200;

private int idEnemigo = 0;
private int idItem = 0;

public Enemigo ENEMIGOS[] = new Enemigo[NUM_ENEMIGOS];
public Enemigo ENEMIGOS_INI[] = new Enemigo[5];
public int ID_ENEMIGOS[] = new int[NUM_ENEMIGOS];

public Item ITEMS[] = new Item[NUM_ITEMS];
private int ID_ITEMS[] = new int[NUM_ITEMS];


private boolean bSalidaIniciada = false;
private boolean bSalidaFinalidazada = false;



public void generaHumo(int x, int y) {
	int i = buscarSlotObjetoLibre(false);
	
	if(i >= 0) {
		ITEMS[i] = new Item(this,gc,Item.IT_HUMO,idItem);
		ITEMS[i].x = x;
		ITEMS[i].y = y;
		ID_ITEMS[i] = idItem++;
	}
}

public void generaMarca(int x, int y, int padre) {
	int i = buscarSlotObjetoLibre(false);
	
	if(i >= 0) {
		
		//System.out.println("Generando marca en:"+x+","+y);
		
		ITEMS[i] = new Item(this,gc,Item.IT_MARCA,idItem);
		ITEMS[i].x = x;
		ITEMS[i].y = y;
		ITEMS[i].id2 = padre;
		ENEMIGOS[buscarObjeto(padre,true)].marcando = true;
		ID_ITEMS[i] = idItem++;
		
	}
}



private void generarTraficoSalida() {
	
	if(bSalidaIniciada) return;
	for (int i=0;i<5;i++) ENEMIGOS_INI[i] = new Enemigo(this,gc,Enemigo.ET_SALIDA,i);
	bSalidaIniciada = true;
}


// Y de (8*130)*8 a (8*134)*8)

private void prepararTraficoSalida(int Y) {

	int x = 20;
	int y = 20/*+50*/; //NEC


	
	int _NUMPIEZAS = 172;
	
	/*		
	switch(NIVEL) {
		case 1: _NUMPIEZAS = 68; break;
		case 2: _NUMPIEZAS = 88; break;
		case 3: _NUMPIEZAS = 86; break;
		case 4: _NUMPIEZAS = 136; break;
	}
	*/



	for (int i=0;i<5;i++) {
		//ENEMIGOS_INI[i].setXY(x,(8*130)*8-Y+y+(4*64));
		if(ENEMIGOS_INI[i] != null)
			ENEMIGOS_INI[i].setXY(x,(8*(_NUMPIEZAS-/*6*/6))*8-Y+y+(4*64));//NEC
		
		x += 30;
		if(x > 50) {
			x = 20;
			y += 30;
		}
	}

}

private void moverTraficoSalida() { 
	for(int i=0;i<5;i++) if(ENEMIGOS_INI[i] != null) ENEMIGOS_INI[i].RUN();
}

void destroyTraficoSalida(int id) {
	if(id >= 0 && id <5) ENEMIGOS_INI[id] = null;
}
private void destroyTraficoSalida() {

	for(int i=0;i<5;i++) {
		ENEMIGOS_INI[i] = null;
	}
	System.gc();
	bSalidaIniciada	= false;
}

/*private*/ int buscarObjeto(int id, boolean que) {
	
	if(que) {
		for(int i=0;i<NUM_ENEMIGOS;i++) if(ID_ENEMIGOS[i] == id) return i;
		return -1;
	} else {
		for(int i=0;i<NUM_ITEMS;i++) if(ID_ITEMS[i] == id) return i;
		return -1;
	}
}

private int buscarSlotObjetoLibre(boolean que) {
	
	if(que) {
		for(int i=0;i<NUM_ENEMIGOS;i++) if(ID_ENEMIGOS[i] < 0) return i;
		return -1;
	} else {
		for(int i=0;i<NUM_ITEMS;i++) if(ID_ITEMS[i] < 0) return i;
		return -1;
	}
}

public void destroyMe(int id, boolean que) { 
	int i = buscarObjeto(id,que);
	if(i>=0) {
		if(que) {
			ID_ENEMIGOS[i] = -1; 
			ENEMIGOS[i] = null;
		} else {
			ID_ITEMS[i] = -1;
			ITEMS[i] = null;
		}
		System.gc();
	}
	
}
	

private void destroyAll() {
	for(int i=0;i<NUM_ENEMIGOS;i++) {
		ID_ENEMIGOS[i] = -1;
		ENEMIGOS[i] = null;
	}
	for(int i=0;i<NUM_ITEMS;i++) {
		ID_ITEMS[i] = -1;
		ITEMS[i] = null;
	}
	System.gc();
}


static long itemsFuelTimer   = gameCounter/*System.currentTimeMillis()*/;
static long itemsPiedraTimer = gameCounter/*System.currentTimeMillis()*/;
static long itemsAceiteTimer = gameCounter/*System.currentTimeMillis()*/;
static long itemsCharcoTimer = gameCounter/*System.currentTimeMillis()*/;

static long enemigosTimer = gameCounter/*System.currentTimeMillis()*/;



////////////////////////////////////////////////////////////////////////
//  COORDENADAS LIMITE DE APARICION DE ITEMS POR PIEZA (20 piezas)
////////////////////////////////////////////////////////////////////////
//                   1    2   3   4   5  6   7   8   9   10
int itMinX[] = {15	,15	,15	,15	,22	,22	,22	,22	,30	,30,
					 30	,30	,30	,22	,22	,30	,30	,30	,30	,30};
//                  11   12  13  14  15  16  17  18  19  20
int itMaxX[] = {70	,70	,70	,70	,70	,78	,78	,78	,78	,85,
					 85	,85	,70	,70	,70	,70	,70	,70	,70	,70};
/*
static int minY[] = {0	,0	,0	,0	,0	,0	,0	,0	,0	,0,
					 0	,0	,0	,0	,0	,0	,0	,0	,0	,0}

static int maxY[] = {0	,0	,0	,0	,0	,0	,0	,0	,0	,0,
					 0	,0	,0	,0	,0	,0	,0	,0	,0	,0}
*/

// vector de aparicion de items

boolean aparicion[] = new boolean[172]; 


private void prepararItems() {
	int i,j;
		
	if(GAMESETTINGS[GS_DIFICULT] == 0) i = 10;
	else if(GAMESETTINGS[GS_DIFICULT] == 1) i = 30;
	else i = 60;

	System.out.println("`Para esta dificultad sacamos "+i+" items.");
	

	for(j=0;j<172;aparicion[j++]=false);
	for(j=0;j<i;j++) aparicion[RND(172)] = true;

}

private void generarTrafico(boolean que) {
	
	int k;
	int i = buscarSlotObjetoLibre(que);
	if(i >= 0) {
		//////////// COCHES
		if(que) {
			if(GAMESETTINGS[GS_DIFICULT] == 0) k = 50;
			else if(GAMESETTINGS[GS_DIFICULT] == 1) k = 20;
			else k = 10;
	
			if(/*System.currentTimeMillis()*/gameCounter - enemigosTimer < k) return;
			else {
				enemigosTimer = gameCounter; //System.currentTimeMillis();
			}
				
			k = RND(50);
			if(k<25)
				ENEMIGOS[i] = new Enemigo(this,gc,Enemigo.ET_NORMAL,idEnemigo);
			else /*if (k<50)	*/
				ENEMIGOS[i] = new Enemigo(this,gc,Enemigo.ET_ESPECIAL,idEnemigo);
//			else
//				ENEMIGOS[i] = new Enemigo(this,gc,Enemigo./*ET_NORMAL*/ET_CAMION,idEnemigo);
			ID_ENEMIGOS[i] = idEnemigo++;
				//System.out.println("generarTrafico(): enemigo "+ID_ENEMIGOS[i]);
		//////////// ITEMS
		} else {
			int pieza = gc.scroll.ScrollY/64;
			if(aparicion[pieza]) {
				aparicion[pieza] = false;
				k = RND(75);
				if(k<25)
					ITEMS[i] = new Item(this,gc,Item.IT_ACEITE,idItem);
				else if (k<70)	
					ITEMS[i] = new Item(this,gc,Item.IT_CHARCO,idItem);
				else
					ITEMS[i] = new Item(this,gc,Item.IT_PIEDRA,idItem);
				
				ID_ITEMS[i] = idItem++;
			}
			//////////// FUEL
			if(GAMESETTINGS[GS_DIFICULT] == 0) k = RND(2000);
			else if(GAMESETTINGS[GS_DIFICULT] == 1) k = RND(3000);
			else k = RND(4000);
			if(k < 25) {
				ITEMS[i] = new Item(this,gc,Item.IT_BARRIL,idItem);
				ID_ITEMS[i] = idItem++;
			}
		}
	}
}

private void moverTrafico() {
	//System.out.println("moviendo enemigos");
	for(int i=0;i<NUM_ENEMIGOS;i++) {
		if(ENEMIGOS[i] != null) ENEMIGOS[i].RUN();
		//else ENEMIGOS[i] = null;
	}

	//System.out.println("moviendo items");
	for(int i=0;i<NUM_ITEMS;i++) {
		if(ITEMS[i] != null) ITEMS[i].RUN();
		//else ITEMS[i] = null;
	}



	//System.gc();	//optimizar...
}



/******************************************************************************
 *	GESTION DE DUREZAS
 ******************************************************************************/

public static final byte DUR_NO_PISABLE		=	0;
public static final byte DUR_PISABLE_100	=	1;
public static final byte DUR_PISABLE_100_2	=	2;
public static final byte DUR_PISABLE_90_D	=	3;
public static final byte DUR_PISABLE_50_D	=	4;
public static final byte DUR_PISABLE_20_D	=	5;
public static final byte DUR_PISABLE_10_D	=	6;
public static final byte DUR_PISABLE_90_I	=	7;
public static final byte DUR_PISABLE_50_I	=	8;
public static final byte DUR_PISABLE_20_I	= 	9;
public static final byte DUR_PISABLE_10_I	=	10;

//private byte durezaTiles[] = new byte[256]; al gamecanvas

//public boolean esPisable(int x, int y) { return true; }
	
public boolean esPisable(int x, int y) {

	int pieza=-1;
	int y0=-1;

	if(y<0 || x <0) return false;

try {
	pieza = (scroll.ScrollY+y)/(8*8);
	y0 = (gc.scrollXMap[pieza]-1)*(8*8);// + (scroll.ScrollY%(16*8));

	switch (gc.durezaTiles[gc.scrollMap[x/8+((y0+y%64)/8)*14]&0xff]) {

		case DUR_NO_PISABLE: 	return false;
		case DUR_PISABLE_100:	return true;
		case DUR_PISABLE_100_2: return true;
		
		
		case DUR_PISABLE_90_D:	return x%8 > 1;
		case DUR_PISABLE_50_D:	return x%8 > 4;
		case DUR_PISABLE_20_D:	return x%8 > 6;
		case DUR_PISABLE_10_D:	return x%8 > 7;
		case DUR_PISABLE_90_I:	return x%8 < 7;
		case DUR_PISABLE_50_I:	return x%8 < 6;
		case DUR_PISABLE_20_I:	return x%8 < 4;
		case DUR_PISABLE_10_I:	return x%8 < 1;
		
		
		/*
		case DUR_PISABLE_90_D:	return true;
		case DUR_PISABLE_50_D:	return true;
		case DUR_PISABLE_20_D:	return false;
		case DUR_PISABLE_10_D:	return false;
		case DUR_PISABLE_90_I:	return true;
		case DUR_PISABLE_50_I:	return true;
		case DUR_PISABLE_20_I:	return false;
		case DUR_PISABLE_10_I:	return false;
		*/
		
		default:
			System.out.println("HORRENDIDAD ABSOLUTA!");
			return false;
	}

} catch(java.lang.ArrayIndexOutOfBoundsException e) {
	
	System.out.println("PIEZA: "+ pieza);
	//System.out.println("PIEZA REAL: "+ (gc.scrollXMap[pieza]-1));
	System.out.println("Y0: "+ y0);
	System.out.println("PETA: ("+x+","+y+")");
	
	return true;
	
}


}



public int minX(int y) {
	
	if(y<0) y = 0;
	for(int i=0;i<14*8;i++) {
		if(esPisable(i,y) && esPisable(i+16,y)) return i;
	}
	return 0;
}

public int maxX(int y) {

	if(y<0) y = 0;	
	for(int i=14*8-1;i>=0;i--) {
		if(esPisable(i,y) && esPisable(i-16,y)) return i;
	}
	return 0;
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public byte[] LoadFile(String Nombre)
{
	FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+Nombre);
	byte[] bytes = file.readBytes();

	return bytes;
	/*System.gc();

	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] buffer = null;

	try
	{
		int Size = 0;
		while (true)
		{
		int Desp = (int) is.skip(1024);
		if (Desp <= 0) {break;}
		Size += Desp;
		}

		is = null; System.gc();

		buffer = new byte[Size];

		is = getClass().getResourceAsStream(Nombre);
		Size = is.read(buffer, 0, buffer.length);

		while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}

		is.close();
	}
	catch(Exception exception) {}

	System.gc();

	return buffer;*/
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




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
//////////////////////////////////////////EXTRAS//
final static int TEXT_OPTIONS = 13;				//
final static int TEXT_PREVIOUS = 14;
final static int TEXT_DIFICULTY = 15;
final static int TEXT_OUT_OF_FUEL = 16;
final static int TEXT_COMPLETED = 17;			//
//////////////////////////////////////////////////
// -------------------
// textos Create
// ===================

String[][] textosCreate(byte[] textos)
{
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

			if (textos[i] < 0x20 || textos[i] == 0x7C || textos[i] == 0x7D)	// Buscamos cuando Termina un campo
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

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
	int num = data[dataPos];

	if (num<0) {num*=-1;dataPos++;} else {num = 1;}

	strings[i] = new String[num];

	for (int t=0 ; t<num; t++) {strings[i][t] = new String(textos, data[dataPos++], data[dataPos++]);}
	}

	return strings;
}

// <=- <=- <=- <=- <=-


// *******************
// -------------------
// Cheats - Engine
// ===================
// *******************

public void Cheats()
{
}

// <=- <=- <=- <=- <=-



// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------


// **************************************************************************//
} // Final Clase MIDlet
// **************************************************************************//