package com.mygdx.mongojocs.machinecrisis;

// -----------------------------------------------
// SAIYU Game Base iMode v1.1 Rev.2 (6.2.2004)
// ===============================================
// Logica
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

public class Game extends IApplication
{

GameCanvas gc;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()
{
	System.gc();
	gc = new GameCanvas(this);
	System.gc();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// start
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void start()
{
	Display.setCurrent(gc);

	run();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	gameExit=true;
	terminate();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// run - Thread que creamos para CORRER el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

boolean gameExit = false;

long gameMillis;
int  gameSleep;

public void run()
{
	Display.setCurrent(gc);

	System.gc();
	gameCreate(); System.gc();
	gc.canvasInit(); System.gc();
	gameInit(); System.gc();

	while (!gameExit)
	{
		gameMillis = System.currentTimeMillis();

		keyboardTick();

		gameTick();

		gc.gameDraw();

		gameSleep=(50-(int)(System.currentTimeMillis()-gameMillis));
		if (gameSleep<1) {gameSleep=1;}
		try	{ Thread.sleep(gameSleep); } catch(Exception e) {}
	}

	gameDestroy();

	destroyApp(true);
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

void loadFile(byte[] buffer, String Nombre)
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

public byte[] loadFile(String Nombre)
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
// colision
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
public boolean colision(int x1, int y1, int xs1, int ys1, int x2, int y2, int xs2, int ys2)
{
	return !(( x2 >= x1+xs1 ) || ( x2+xs2 <= x1 ) || ( y2 >= y1+ys1 ) || ( y2+ys2 <= y1 ));
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// *******************
// -------------------
// Keyboard - Engine
// ===================
// *******************

int keyX, keyY, keyMisc, keyMenu;
int lastKeyX, lastKeyY, lastKeyMisc, lastKeyMenu;

// -------------------
// Keyboard RUN
// ===================

public void keyboardTick()
{
	lastKeyX = keyX;	// Keys del Frame Anterior
	lastKeyY = keyY;
	lastKeyMisc = keyMisc;
	lastKeyMenu = keyMenu;

	keyX = gc.keyX;	// Keys del Frame Actual
	keyY = gc.keyY;
	keyMisc = gc.keyMisc;
	keyMenu = gc.keyMenu;
}

// <=- <=- <=- <=- <=-



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
///////////////////////////////////////////
final static int TEXT_NIVEL		= 13;
final static int TEXT_COMPLETADO= 14;
final static int TEXT_PUNTUACION= 15;
final static int TEXT_RAPIDO 	= 16;
final static int TEXT_LENTO 	= 17;
final static int TEXT_STOP 		= 18;
final static int TEXT_MAGNETIC 	= 19;
final static int TEXT_PARALIZE 	= 20;
final static int TEXT_PUNTOS 	= 21;
final static int TEXT_HISCORE 	= 22;
final static int TEXT_TIPS 		= 23;

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

int gameStatus=0;

int gameSound = 1;
int gameVibra = 1;
int gameLevel = 0;

Marco ma;

String[][] menuText;


// -------------------
// Game INI
// ===================

public void gameCreate()
{
	ma = new Marco(gc.canvasWidth, gc.canvasHeight);
}

// -------------------
// Game SET
// ===================

public void gameInit()
{
	prefsCreate();		// Leemos Archivo Preferencias

	menuText = textosCreate(loadFile("/Textos.txt"));		// Cargamos Textos.

}

// -------------------
// Game END
// ===================

public void gameDestroy()
{
	prefsInit();		// Grabamos Archivo Preferencias
}


// -------------------
// Game RUN
// ===================

	public static final int	MAX_NUM_PIEZAS	= 10;
	public static final int	MAX_NUM_ITEMS	= 5;

	private int buscarSlotLibrePieza() {
		for(int i=0;i<MAX_NUM_PIEZAS;i++) if(PIEZAS[i] == null) return i;
		return -1;
	}

	private int buscarSlotLibreItem() {
		for(int i=0;i<MAX_NUM_ITEMS;i++) if(ITEMS[i] == null) return i;
		return -1;
	}

	public void destroyPieza(int id) { 
		PIEZAS[id] = null; System.gc();
		//CONTADOR_PIEZAS--;
	}
	
	public void destroyItem(int id) { 
		ITEMS[id] = null; System.gc();
		//CONTADOR_PIEZAS--;
	}
	

	private void destroyAll() {
		//System.out.println("eliminando piezas e items antes");
		for(int i=0;i<MAX_NUM_PIEZAS;PIEZAS[i++]=null);
		for(int i=0;i<MAX_NUM_ITEMS;ITEMS[i++]=null);
		//System.out.println("eliminando piezas e items despues");
	}
	

	/*private*/ void generarPieza() {
	
		//System.out.println("generando pieza...");
		if(maquinaParada) return;
		
		int i = buscarSlotLibrePieza();
		if(i < 0) {
			System.out.println("No hay mas slots para piezas!!");
			return;
		} else {
			//System.out.println("ok");
			PIEZAS[i] = new Pieza(this,gc,i);
			gc.bImpTuberias = true;
			//System.out.println("ok?");
		}
	}

	private void generarItem() {

		if(maquinaParada) return;
			
		int i = buscarSlotLibreItem();
		if(i < 0) {
			System.out.println("No hay mas slots para items!!");
			return;
		} else {
			int tipoItem;
			boolean ok = false;
			do {
				tipoItem = 2+RND(5);
				ok = ((tipoItem == 2 || tipoItem == 3) && PARAM_ITEMS_VELOCIDAD == 1)
						||
					 ((tipoItem == 4 || tipoItem == 6) && PARAM_ITEMS_TEMPORALES == 1)
					 	||
					 (tipoItem == 5 && PARAM_ITEMS_MAGNETISMO == 1);
				
			} while(!ok);
			
			ITEMS[i] = new Item(this,gc,i,tipoItem); //[2..6]
			//ITEMS[i] = new Item(this,gc,i,Item.IT_IMAN);
			gc.bImpTuberias = true;
		}
	}
	

	private void generarBomba() {
	
		if(maquinaParada) return;
	
		int i = buscarSlotLibreItem();
		if(i < 0) {
			System.out.println("No hay mas slots para items!!");
			return;
		} else {
			ITEMS[i] = new Item(this,gc,i,Item.IT_BOMBA); 
			gc.bImpTuberias = true;
		}
	}

	private void moverPiezas() {
		for(int i=0;i<MAX_NUM_PIEZAS;i++) if(PIEZAS[i] != null) PIEZAS[i].RUN();
	}

	private void moverItems() {
		for(int i=0;i<MAX_NUM_ITEMS;i++) if(ITEMS[i] != null) ITEMS[i].RUN();
	}



	private boolean allDone() {
		for(int i=0;i<MAX_NUM_ITEMS;i++) if(ITEMS[i] != null) return false;
		for(int i=0;i<MAX_NUM_PIEZAS;i++) 
			if(PIEZAS[i] != null) {
				PIEZAS[i].magnetizada = false; 
				PIEZAS[i].paralizada = false; 
				return false;
			}		
		return true;
	}




public void gameTick() {
	switch (gameStatus)
	{
	case 0:
		gameStatus++;
	break;

	case 1:
		gameStatus=10;
	break;


// ------------------
//  Slide Logos
// ------------------
	case 10:
		gc.canvasFillInit(0xffffff);
		gc.canvasImg = gc.FS_LoadImage(0,1);
		waitInit(3*1000, gameStatus+1);
	break;

	case 11:
		gameStatus = 50;
	break;
// ===================


// ------------------
// Menu Bucle
// ------------------
	case 50:
		gc.canvasFillInit(0);
		gc.canvasImg = gc.FS_LoadImage(0,2);

		gc.SoundSET(0,0);

		gameStatus++;
	break;

	case 51:
		///// repintar caratula para los brillos...
		//gc.canvasImg = gc.FS_LoadImage(0,2);
		
		if ((keyMenu!=0 && keyMenu!=lastKeyMenu) || (keyMisc!=0 && keyMisc!=lastKeyMisc)) {gameStatus++;panelInit(0);}
	break;

	case 52:
		gc.SoundRES();
		gameStatus=100; 
	break;
// ===================


// ------------------
// Jugar Bucle
// ------------------
	case 100:
		jugarCreate();
		gameStatus++;
	case 101:
		gc.SoundRES(); //para sonido de recuento de puntuacion entre fases
		jugarInit();
		gameStatus++;
	case 102:
		TIEMPO_FASE = System.currentTimeMillis()-TIEMPO_INI;		
		//System.out.println(TIEMPO_FASE);
		if(gc.bImpConsejo) { 
			//System.out.println("po-zi");
			jugarShow = true; break; 
		}
		
		cheats();

		if (keyMenu==-1 && keyMenu!=lastKeyMenu) {panelInit(1); break;}

		if ( !jugarTick() ) {break;}

		//jugarRelease();
		if(siguienteNivel) {
			PROTA.x = -24;
			gameStatus +=3; 
			NIVEL++;
			jugarShow = true;
			
			gc.bImpMarcador = true;
			
			gc.bImpPiezas = true;
			gc.bImpProta = true;
			gc.bImpItems = true;
			gc.bImpFondo = true;
			/*
			jugarShow = true;
			gc.bImpMarcador = gc.bActualizaMarcador = true;
			*/
		} else {
			gc.bImpTransicion = true;
			gameStatus++;
		}
	break;

	case 103:
		jugarDestroy();
		jugarShow = true;
		if(!gc.bImpTransicion) gameStatus++;
	break;

	case 104:	// GAME OVER
		gc.noImp();
		panelInit(5);
		waitInit(3*1000, 50);
	break;

	case 105:	// NEXT LEVEL 0
		TIEMPO_FASE = System.currentTimeMillis()-TIEMPO_INI;
		GameTicks++;
		
		jugarShow = true;
		
		gc.bImpMarcador = true;
		
		gc.bImpPiezas = true;
		gc.bImpProta = true;
		gc.bImpItems = true;
		gc.bImpFondo = true;
		moverItems();
		moverPiezas();
		PROTA.RUN();
		/*
		if(PROTA.x < gc.canvasWidth/2) scrollX = 0;
		else if(PROTA.x < 3*gc.canvasWidth/2) scrollX = PROTA.x - gc.canvasWidth/2;
		else scrollX = gc.canvasWidth;
		*/
		
		if(!gc.bImpManoOK && !gc.bImpManoNOK && allDone()) {
			siguienteNivel = false;
			gc.noImp();
			gc.bImpFondo = true;
			gc.bImpTransicion = true;
			gameStatus++;
			gc.SoundSET(1,0);
		}
		break;

	case 106:  	// NEXT LEVEL 1
		jugarShow = true;
		if(!gc.bImpTransicion) {
			gc.bImpNivelCompletado = true;
			TIMER = System.currentTimeMillis();
			gameStatus++;
			PROTA.x2 = -1;
			PROTA.x  = gc.canvasWidth-25; 
		}
		break;
	case 107: 	// NEXT LEVEL 2
		//mientras dure la trasicion...
		//System.out.println("en 105");
		jugarShow = true;
		gc.bImpProta = true;
		if(!gc.bImpNivelCompletado) {
			if(System.currentTimeMillis()-TIMER>5000) gameStatus = 101;
		} else PROTA.fakeRUN();
		//else gc.bImpNivelCompletado = true;
	break;
// ===================


// ------------------
// Wait Bucle
// ------------------
	case 1000:
		waitTick();
	break;
// ==================


// ------------------
// Panel Bucle
// ------------------
	case 900:
		panelTick();
	break;
// ==================
	}

}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Panel - Engine
// ===================
// *******************

int panelType;
int panelTypeOld;
int panelStatus;

// -------------------
// Panel SET
// ===================

public void panelInit(int Type)
{
	TIMER = System.currentTimeMillis();
	
	panelStatus = gameStatus;
	gameStatus = 900;

	panelTypeOld = panelType;
	panelType = Type;

	switch (Type)
	{
	case 0:
		ma.MarcoINI();
		ma.MarcoADD(0x000, menuText[TEXT_PLAY], 0);
		ma.MarcoADD(0x000, menuText[TEXT_SOUND], 2, gameSound);
		ma.MarcoADD(0x000, menuText[TEXT_VIBRA], 3, gameVibra);
		ma.MarcoADD(0x000, menuText[TEXT_HELP], 6);
		ma.MarcoADD(0x000, menuText[TEXT_ABOUT], 7);
		ma.MarcoADD(0x000, menuText[TEXT_EXIT], 9);
		ma.MarcoSET_Option();
	break;	

	case 1:
		ma.MarcoINI();
		ma.MarcoADD(0x000, menuText[TEXT_CONTINUE], 1);
		ma.MarcoADD(0x000, menuText[TEXT_SOUND], 2, gameSound);
		ma.MarcoADD(0x000, menuText[TEXT_VIBRA], 3, gameVibra);
		ma.MarcoADD(0x000, menuText[TEXT_HELP], 6);
		ma.MarcoADD(0x000, menuText[TEXT_RESTART], 8);
		ma.MarcoADD(0x000, menuText[TEXT_EXIT], 9);
		ma.MarcoSET_Option();
	break;	

	case 5:
		ma.MarcoINI();
		ma.MarcoADD(0x111, menuText[TEXT_GAMEOVER]);
		ma.MarcoADD(0x111, PUNTUACION+" "+menuText[TEXT_PUNTOS][0]+".");
		ma.MarcoADD(0x111, "( "+menuText[TEXT_HISCORE][0]+": "+HISCORE+" )");
		ma.MarcoSET_Texto();
	break;

	case 6:
		ma.MarcoINI();
		ma.MarcoADD(0x001, menuText[TEXT_HELP_SCROLL]);
		ma.MarcoSET_Scroll(0);
	break;

	case 7:
		ma.MarcoINI();
		ma.MarcoADD(0x001, menuText[TEXT_ABOUT_SCROLL]);
		ma.MarcoSET_Scroll(0);
	break;
	}
}


// -------------------
// Panel END
// ===================

public void panelDestroy()
{
	TIEMPO_INI += System.currentTimeMillis()-TIMER;
	ma.MarcoEND();
	gameStatus = panelStatus;
}


// -------------------
// Panel RUN
// ===================

public void panelTick()
{
	int marcoKey=0;
	if (keyY==-1 && lastKeyY!=-1) {marcoKey=2;}
	if (keyY== 1 && lastKeyY!= 1) {marcoKey=8;}
	if (keyMenu != 0 && lastKeyMenu== 0) {marcoKey=5;}

	panelAction( ma.MarcoRUN(marcoKey, keyY) );
}

// ---------------
// Panel EXE
// ===============

public void panelAction(int cmd)
{
	switch (cmd)
	{
	case -2: // Scroll ha llegado al final
		panelDestroy();
		gc.canvasFillInit(0);
		panelInit(panelTypeOld);
	break;

	case 0:	// Jugar de 0
		panelDestroy();
		gc.canvasFillInit(0);
	break;

	case 1:	// Continuar
		panelDestroy();
		gc.canvasFillInit(0);
	break;

	case 2:	// Sound ON/OFF
		gameSound = ma.getOption();
		if (gameSound==0) {gc.SoundRES();} else if (panelType==0) {gc.SoundSET(0,0);}
	break;

	case 3:	// Vibra ON/OFF
		gameVibra = ma.getOption();
	break;

	case 6:	// Controles...
	case 7:	// About...
		panelDestroy();
		gc.canvasFillInit(0);
		panelInit(cmd);
	break;

	case 8:	// Restart
		panelDestroy();
		gc.canvasFillInit(0);
		gameStatus++;
	break;

	case 9:	// Exit Game
		gc.canvasFillInit(0);
		gameExit = true;
	break;	
	}

}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Wait - Engine
// ===================
// *******************

long waitTiempoIni, waitTiempoFin;
int waitRetorno;

// -------------------
// Wait INI
// ===================

public void waitCreate(int Tiempo, int Ret)
{
	waitRetorno=Ret;
	waitTiempoFin = System.currentTimeMillis() + Tiempo;
}

// -------------------
// Wait SET
// ===================

public void waitInit()
{
	gameStatus=1000;
}

public void waitInit(int Tiempo, int Ret)
{
	waitRetorno=Ret;
	waitTiempoFin = System.currentTimeMillis() + Tiempo;
	gameStatus=1000;
}

// -------------------
// Wait RUN
// ===================

public void waitTick()
{
	if (waitTiempoFin < System.currentTimeMillis()) {gameStatus=waitRetorno;}
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// Prefs - Engine
// ===================
// *******************

byte[] prefsData = new byte[] {1,1,0};

// -------------------
// Prefs INI
// ===================

public void prefsCreate()
{
	prefsData = gc.FS_LoadFile(0,0);

	gameSound=prefsData[0] & 1;
	gameVibra=prefsData[1] & 1;
	gameLevel=prefsData[2];
}

// -------------------
// Prefs SET
// ===================

public void prefsInit()
{
	prefsData[0]=(byte)gameSound;
	prefsData[1]=(byte)gameVibra;
	prefsData[2]=(byte)gameLevel;

	gc.FS_SaveFile(0,0, prefsData);
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// cheats - Engine
// ===================
// *******************

public void cheats()
{
}

// <=- <=- <=- <=- <=-



// -------------------------------------------------------------------------------------------
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
// -------------------------------------------------------------------------------------------





// *******************
// -------------------
// Jugar - Engine
// ===================
// *******************

public Prota PROTA;
public Pieza[] PIEZAS = new Pieza[MAX_NUM_PIEZAS];
public Item[] ITEMS = new Item[MAX_NUM_ITEMS];

//public int[] ID_PIEZAS = new int[MAX_NUM_PIEZAS];
//int currentPiezasId;


int CONTADOR_PIEZAS = 0;
int NIVEL; 
int PUNTUACION;
long TIEMPO_FASE = 0;
long TIEMPO_INI = 0;
int PIEZAS_CAIDAS = 0;
int HISCORE;



///parametros de cada nivel///////////////
int PARAM_PIEZAS_GOAL;					//
int PARAM_PIEZAS_GOALx2;				//
int PARAM_TIMER_SALIDA_PIEZAS;			//
int PARAM_TIMER_SALIDA_BOMBAS;			//
int PARAM_TIMER_SALIDA_ITEMS;			//
int PARAM_RADIO_EXPLOSION;				//
int PARAM_TIME_EXPLOSION;				//
int PARAM_PENALIZACION;					//
int PARAM_ITEMS_VELOCIDAD;				//
int PARAM_ITEMS_TEMPORALES;				//
int PARAM_ITEMS_MAGNETISMO;				//
//////////////////////////////////////////

long TIMER = System.currentTimeMillis();

boolean maquinaParada = false;

boolean jugarShow = false;

int protX;
int protY;

// -------------------
// Jugar INI
// ===================

boolean firstTime = true;
public void jugarCreate()
{
	jugarShow = false;

	if(firstTime) {
		gc.jugarCreate_Gfx();
		PROTA = new Prota(this,gc);
		firstTime = false;
		byte aux[] = gc.FS_LoadFile(0,3);
		HISCORE = (aux[0]&0xff)<<24 | (aux[1]&0xff)<<16 | (aux[2]&0xff)<<8 | (aux[3]&0xff);
	}
	destroyAll();
	PUNTUACION = 0;
	NIVEL = 0;

}


// -------------------
// Jugar END
// ===================

public void jugarDestroy()
{
	jugarShow = false;
//		public void FS_SaveFile(int Pos, int SubPos, byte[] Bufer)
//		public byte[] FS_LoadFile(int Pos, int SubPos)

	
	if(PUNTUACION > HISCORE) {
		System.out.println("Mejor puntuacion "+PUNTUACION+" ("+HISCORE+")");
		HISCORE = PUNTUACION;
		byte aux[] = { 	(byte)((HISCORE>>24)&0xff),	(byte)((HISCORE>>16)&0xff),
					(byte)((HISCORE>>8)&0xff),	(byte)(HISCORE&0xff)	};
		gc.FS_SaveFile(0,3,aux);
	}
	//gc.jugarDestroy_Gfx();
	//PROTA = null;
	//destroyAll();
	//System.gc();
}


// -------------------
// Jugar SET
// ===================

public void jugarInit()
{
	//System.out.println("en jugarInit() al ppio");
	gc.jugarInit_Gfx(); //no hace nada
	GameTicks = scrollX = 0;
	PROTA.reset();
	destroyAll();
	
	//PARAM_PIEZAS_GOAL = 2+2*NIVEL;
	cargarNivel(NIVEL);
	gc.prepareMarcador();
	gc.bActualizaMarcador = true;
	maquinaParada = false;
	TIEMPO_INI = System.currentTimeMillis();
	PIEZAS_CAIDAS = 0;
	gc.bImpConsejo = true;
	
//	animScrollDone = false;
	//System.out.println("en jugarInit() al final");
}


// -------------------
// Jugar RES
// ===================

public void jugarRelease()
{
	gc.jugarRelease_Gfx();
	destroyAll();
	System.gc();
}


// -------------------
// Jugar RUN
// ===================

long GameTicks = 0;
int scrollX = 0;
boolean siguienteNivel;
//boolean animScrollDone = true;

public boolean jugarTick()
{
	GameTicks++;
	
	jugarShow = true;

	gc.bImpFondo = true;
	gc.bImpMarcador = true;
	gc.bImpProta = true;
	gc.bImpPiezas = true;
	gc.bImpItems = true;
	gc.bImpTextosPunt = true;

/*
	if(scrollX > 0 && !animScrollDone) {
		scrollX-=2;
		if(scrollX <= 0) {
			animScrollDone = true;
			scrollX = 0;
		}	
		return false;
	}
*/

	//System.out.println("en jugarTick()");
	if(siguienteNivel/* && !gc.bImpNivelCompletado*/) return true;
	//System.out.println("seguimos...");
	
	if(!gc.bImpNivelCompletado) {
		moverPiezas();
		moverItems();
		PROTA.RUN();
		for(int i=0;i<8;i++) if(TEXTOS[i] != null) TEXTOS[i].RUN();
	
		if(PROTA.x < gc.canvasWidth/2) scrollX = 0;
		else if(PROTA.x < 3*gc.canvasWidth/2) scrollX = PROTA.x - gc.canvasWidth/2;
		else scrollX = gc.canvasWidth;
		
		// va decrementando el intervalo entre pieza y pieza
		if(GameTicks%100==0) {
			PARAM_TIMER_SALIDA_PIEZAS--;
			System.out.println("Bajando timer piezas a "+PARAM_TIMER_SALIDA_PIEZAS);
		}
		if(PARAM_TIMER_SALIDA_PIEZAS < 30) PARAM_TIMER_SALIDA_PIEZAS = 30;
		
		if(GameTicks%PARAM_TIMER_SALIDA_PIEZAS==0) {
			generarPieza();
		}
				
		if(PARAM_TIMER_SALIDA_ITEMS > 0)
		if(GameTicks%PARAM_TIMER_SALIDA_ITEMS==0) {
			generarItem();
		}

		if(PARAM_TIMER_SALIDA_BOMBAS > 0) {
			// va decrementando el intervalo entre bombas
			if(GameTicks%500==0) PARAM_TIMER_SALIDA_BOMBAS-=5;
			if(PARAM_TIMER_SALIDA_BOMBAS < 50) PARAM_TIMER_SALIDA_BOMBAS = 50;

			if(GameTicks%PARAM_TIMER_SALIDA_BOMBAS==0) {
				generarBomba();
			}
		}
		
		if(PARAM_PIEZAS_GOAL <= 0) {
			siguienteNivel = true;
		}
		if(PARAM_PIEZAS_GOAL >= PARAM_PIEZAS_GOALx2) {
			siguienteNivel = false;		
			return true;
		}	
	}	

	return false;
}

// <=- <=- <=- <=- <=-

Texto TEXTOS[] = {null,null,null,null,null,null,null,null};

public void ponPuntos(int x,int y, int pnts) {
	for(int i=0;i<8;i++) { 
		if(TEXTOS[i] == null) {
			TEXTOS[i] = new Texto(this,x,y,Integer.toString(pnts),i);
			break;
		}
	}
}

public void destroyText(int id) {
	TEXTOS[id] = null;
	System.gc();
}





// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------



private void cargarNivel(int nivel) {

	
	System.out.println("Cargando nivel "+nivel+" ("+nivel%5+")"); 
	//nivel = nivel%5;
	
	if(nivel < 5) {
		PARAM_PIEZAS_GOAL			= NIVELES[nivel*10+0];
		PARAM_PIEZAS_GOALx2			= PARAM_PIEZAS_GOAL*2;
		PARAM_TIMER_SALIDA_PIEZAS	= NIVELES[nivel*10+1];
		PARAM_TIMER_SALIDA_BOMBAS	= NIVELES[nivel*10+2];
		PARAM_TIMER_SALIDA_ITEMS	= NIVELES[nivel*10+3];
		PARAM_RADIO_EXPLOSION		= NIVELES[nivel*10+4];
		PARAM_TIME_EXPLOSION		= NIVELES[nivel*10+5];
		PARAM_PENALIZACION			= NIVELES[nivel*10+6];
		PARAM_ITEMS_VELOCIDAD		= NIVELES[nivel*10+7];
		PARAM_ITEMS_TEMPORALES		= NIVELES[nivel*10+8];
		PARAM_ITEMS_MAGNETISMO		= NIVELES[nivel*10+9];
	} else {
		PARAM_PIEZAS_GOAL			= 6+nivel/5;
		PARAM_PIEZAS_GOALx2			= PARAM_PIEZAS_GOAL*2;
		PARAM_TIMER_SALIDA_PIEZAS	= 60 - nivel/7;
		PARAM_TIMER_SALIDA_BOMBAS	= 400 - nivel/10;
		PARAM_TIMER_SALIDA_ITEMS	= 140 - nivel/6;
		PARAM_RADIO_EXPLOSION		= 10;
		PARAM_TIME_EXPLOSION		= 800;
		PARAM_PENALIZACION			= 1+nivel/10;
		PARAM_ITEMS_VELOCIDAD		= 1;
		PARAM_ITEMS_TEMPORALES		= 1;
		PARAM_ITEMS_MAGNETISMO		= 1;
	}

	System.out.println(	PARAM_PIEZAS_GOAL+","+PARAM_TIMER_SALIDA_PIEZAS+","+
						PARAM_TIMER_SALIDA_BOMBAS+","+PARAM_TIMER_SALIDA_ITEMS+","+
						PARAM_RADIO_EXPLOSION+","+PARAM_TIME_EXPLOSION+","+
						PARAM_PENALIZACION+","+PARAM_ITEMS_VELOCIDAD+","+
						PARAM_ITEMS_TEMPORALES+","+PARAM_ITEMS_MAGNETISMO+".");
}

////							/////
//TIPS[i] == menuText[TEXT_TIPS][i]//
////							/////
/* 
private static String TIPS[] = {
								"Tan solo un par",
								"�� Magnetic !!",
								"�� BOOOOOOM !!",
								"Cuestion de tiempo",
								"Lento o rapido"
};
*/
private static int NIVELES[] = { 	

//	PARAM_PIEZAS_GOAL
//	PARAM_TIMER_SALIDA_PIEZAS , PARAM_TIMER_SALIDA_BOMBAS , PARAM_TIMER_SALIDA_ITEMS
//	PARAM_RADIO_EXPLOSION , PARAM_TIME_EXPLOSION
//	PARAM_PENALIZACION
//	PARAM_ITEMS_VELOCIDAD , PARAM_ITEMS_TEMPORALES , PARAM_ITEMS_MAGNETISMO


///////// "Tan solo un par" ////////////////////////////////////////////////////
	2, 
	60, 	-1, 	-1,
	0, 		0, 
	0, 
	0, 		0, 		0, 

///////// "��Magnetic!!" ///////////////////////////////////////////////////////
	10,
	30, 	-1, 	100,
	0,		0, 
	0,
	0, 		0, 		1,

///////// "��BOOOM!!" //////////////////////////////////////////////////////////
		
	15, 
	100, 	40, 	200,
	10, 	800,
	0,
	0, 		0, 		1,

///////// "TIME crisis" ////////////////////////////////////////////////////////

	6,
	50,		-1,		160,
	0,		0,
	1,
	0,		1,		0,

///////// "Lento o rapido" /////////////////////////////////////////////////////

	6,
	50,		-1,		80,
	0,		0,
	1,
	1,		0,		0,



//	2,
//	50,	540, 150,
//	10,	800,
//	0,
//	1,1, 1,
							
							};




// **************************************************************************//
} // Final Clase MIDlet
// **************************************************************************//