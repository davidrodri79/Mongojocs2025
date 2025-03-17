package com.mygdx.mongojocs.waterrace;// ------------------------------------------------------
// Game Base v3.0 Rev.4 (2.10.2003) - Canvas
// ======================================================
// Base
// ------------------------------------




import java.io.InputStream;



import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

public class GameCanvas extends FullCanvas
			//public class GameCanvas extends Canvas
{

	static final boolean DeviceSound = true;
	static final boolean DeviceVibra = false;

	Game ga;

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	//  Constructor
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public GameCanvas(Game ga) {
		this.ga = ga;
		//setFullScreenMode(true);
		CanvasINI();
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	protected void sizeChanged(int w, int h) {
		CanvasSizeX = w;
		CanvasSizeY = h;
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// Funcion que se ejecuta al hacer: 'repaint()'
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	Graphics LCD_Gfx;

	long TimeNow, TimeOld;
	int TimeDat, TimeCnt;

	public void paint (Graphics g) {
		if (CanvasPaint) {
			CanvasPaint=false;

			LCD_Gfx = g;

			CanvasRUN();

			/*
			TimeNow=System.currentTimeMillis(); TimeDat=(int)(TimeNow-TimeOld); TimeOld=TimeNow;
			g.setClip( 0, 0, CanvasSizeX, CanvasSizeY ); g.setColor(-1);// g.fillRect(0,0,  CanvasSizeX,12 ); g.setColor(0);
			g.drawString(Integer.toString((int)(ga.CPU_Milis-ga.GameMilis)),    0,110, Graphics.LEFT|Graphics.TOP);
			g.drawString(Integer.toString((int)(TimeNow-ga.CPU_Milis)), 40,110, Graphics.LEFT|Graphics.TOP);
			g.drawString(Integer.toString(TimeDat), 80,110, Graphics.LEFT|Graphics.TOP);
			*/

			/*
			g.setClip( 0,0, CanvasSizeX, CanvasSizeY );
			g.drawString(Integer.toString((int)(CanvasSizeY)),    0,110, Graphics.LEFT|Graphics.TOP);
			*/
			//    System.gc(); g.drawString(Integer.toString((int)(Runtime.getRuntime().freeMemory())), 0,110, Graphics.LEFT|Graphics.TOP);

			LCD_Gfx=null;
		}

	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>-s
	// El MIDlet llama a esta funci�n cuando se PULSA una techa
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.















































	int KeybX, KeybY, KeybB, KeybM;

	public void keyPressed(int keycode) {
		KeybX=0;
		KeybY=0;
		KeybB=0;
		KeybM=0;

		switch(getGameAction(keycode)) {
		case 1:
			KeybY=-1;
			break;// Arriba
		case 6:
			KeybY=1;
			break;	// Abajo
		case 5:
			KeybX=1;
			break;	// Derecha
		case 2:
			KeybX=-1;
			break;// Izquierda
		case 8:
			KeybM=2;
			break;	// Fuego
		}

		switch (keycode) {
		case Canvas.KEY_NUM0:
			KeybB=10;
			break;

		case Canvas.KEY_NUM1:
			KeybB=1;
			KeybX=-1;
			KeybY=-1;
			break;

		case Canvas.KEY_NUM2:	// Arriba
			KeybB=2;
			KeybY=-1;
			break;

		case Canvas.KEY_NUM3:
			KeybB=3;
			KeybX= 1;
			KeybY=-1;
			break;

		case Canvas.KEY_NUM4:	// Izquierda
			KeybB=4;
			KeybX=-1;
			break;

		case Canvas.KEY_NUM5:	// Disparo
			KeybB=5;
			KeybM=2;
			break;

		case Canvas.KEY_NUM6:	// Derecha
			KeybB=6;
			KeybX=1;
			break;

		case Canvas.KEY_NUM7:
			KeybB=7;
			KeybX=-1;
			KeybY= 1;
			break;

		case Canvas.KEY_NUM8:	// Abajo
			KeybB=8;
			KeybY=1;
			break;

		case Canvas.KEY_NUM9:
			KeybB=9;
			KeybX= 1;
			KeybY= 1;
			break;

		case 35:		// *
			KeybB=35;
			break;

		case 42:		// #
			KeybB=42;
			break;

			// -----------------------------------------
		case -6:	// Nokia - Menu Izquierda
			KeybM=-1;
			break;

		case -7:	// Nokia - Menu Derecha
			KeybM= 1;
			break;
			// =========================================

			/*
			// -----------------------------------------
			case 22:			// Motorola V300 Menu Derecha
				KeybM= 1;
				break;

			case 21:			// Motorola V300 Menu Izquierda
				KeybM= -1;
				break;
				// =========================================
			*/
			/*
			// -----------------------------------------
				case -20:	// Motorola T720 - Menu Izquierda
					KeybM=-1;
				break;

				case -21:	// Motorola T720 - Menu Derecha
					KeybM= 1;
				break;
			// =========================================

			// -----------------------------------------
				case -1:	// Siemens - Menu Izquierda
					KeybM=-1;
				break;

				case -4:	// Siemens - Menu Derecha
					KeybM= 1;
				break;
			// =========================================
			*/
		}

	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// El MIDlet llama a esta funci�n cuando se SUELTA una tecla
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	public void keyReleased(int keycode) {
		KeybX=0;
		KeybY=0;
		KeybB=0;
		KeybM=0;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// Metodos a Sobre-Cargar en la Clase que extiende de CANVAS
	// Para paudar el Juego cuando el Canvas "desaparece"
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public void showNotify() {
		ga.GamePaused=false;
	}

	public void hideNotify() {
		ga.GamePaused=true;

		if(ga.GameStatus/100 == 1) {
			ga.PanelSET(1);
			ga.lastPausedTime = ga.currentTimeGame;
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// Rutinas BASE
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public void ImageSET(Image Img) {
		ImageSET(Img, 0,0, Img.getWidth(), Img.getHeight(), (CanvasSizeX-Img.getWidth())/2, (CanvasSizeY-Img.getHeight())/2);
	}

	// ----------------------------------------------------------

	public void ImageSET(Image Img, int X, int Y) {
		ImageSET(Img, 0,0, Img.getWidth(), Img.getHeight(), X, Y);
	}

	// ----------------------------------------------------------

	public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY) {
		if (DestX >= CanvasSizeX || DestY >= CanvasSizeY) {
			return;
		}

		//	LCD_Gfx.setClip(0, 0, CanvasSizeX, CanvasSizeY);
		//	LCD_Gfx.clipRect(DestX, DestY, SizeX, SizeY);

		LCD_Gfx.setClip(DestX, DestY, SizeX, SizeY);
		LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	}

	// ----------------------------------------------------------

	public void ImageSET(Graphics Gfx, Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY) {
		Gfx.setClip(DestX, DestY, SizeX, SizeY);
		Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	}

	// ----------------------------------------------------------

	public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int TopX, int TopY, int FinX, int FinY) {
		LCD_Gfx.setClip(TopX, TopY, FinX, FinY);
		LCD_Gfx.clipRect(DestX+TopX, DestY+TopY, SizeX, SizeY);
		LCD_Gfx.drawImage(Sour, (DestX+TopX)-SourX, (DestY+TopY)-SourY, Graphics.TOP|Graphics.LEFT);
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	// -------------------
	// Canvas Img SET
	// ===================

	public void CanvasImageSET(String FileName, int RGB) {
		CanvasFillRGB = RGB;
		CanvasFillPaint = true;
		CanvasImg = LoadImage(FileName);
	}


	// -------------------
	// Canvas Img PUT
	// ===================

	public void CanvasImagePUT(String FileName, int RGB) {
		CanvasFill(RGB);

		CanvasImg = LoadImage(FileName);

		if (CanvasImg!=null) {
			ImageSET(CanvasImg);
			CanvasImg=null;
			System.gc();
		}
	}


	// -------------------
	// Canvas Fill
	// ===================

	public void CanvasFill(int RGB) {
		LCD_Gfx.setClip (0,0, CanvasSizeX,CanvasSizeY );
		LCD_Gfx.setColor(RGB);
		LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
	}


	// -------------------
	// Load Image
	// ===================

	public Image LoadImage(String FileName) {
		System.gc();
		Image Img = null;

		try	{
			Img = Image.createImage(FileName);
		} catch (Exception e) {
			System.out.println("Error leyendo PNG: "+FileName);
		}

		System.gc();
		return Img;
	}

	// ===================




	// ---------------------------------------------------------
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	// ---------------------------------------------------------


	// *******************
	// -------------------
	// Canvas - Engine
	// ===================
	// *******************

	int CanvasSizeX = getWidth();
	int CanvasSizeY = getHeight();

	boolean CanvasFillPaint = false;
	int CanvasFillRGB;
	Image CanvasImg;
	Scroll sc;

	boolean CanvasPaint = false;
	boolean juegoDraw = false;
	boolean menuDraw = false;
	boolean caratulaDraw = false;
	boolean scoresDraw = false;
	boolean loadingDraw = false;


	byte MAPAFICHAS[][] = new byte[][] {
							  {0, 1, 2, 1, 1, 2, 2, 1, 7, 8, 8, 9, 9, 8, 9, 10, 12, 3, 4, 4, 5, 4, 5, 5, 5, 6, 7, 10, 11, 11, 12, 1, 2, 13, 14, 11, 14, 11, 11, 15, 6, 7, 10, 15, 6, 1, 2, 2, 1, 1, 2, 2, 0},
							  {0, 1, 2, 3, 4, 5, 6, 1, 2, 7, 10, 12, 1, 1, 3, 6, 3, 6, 13, 14, 11, 15, 6, 1, 2, 1, 2, 1, 2, 1, 1, 1, 13, 11, 14, 11, 14, 11, 14, 12, 1, 2, 1, 2, 1, 2, 1, 13, 15, 6, 7, 10, 12, 2, 1, 2, 0},
							  {0, 1, 2, 2, 2, 1, 1, 13, 11, 14, 11, 14, 14, 14, 11, 14, 11, 15, 6, 7, 10, 12, 3, 6, 7, 8, 8, 9, 9, 10, 11, 12, 13, 12, 13, 15, 6, 7, 10, 15, 4, 5, 4, 5, 6, 1, 2, 1, 2, 13, 11, 14, 11, 14, 11, 12, 13, 12, 3, 6, 13, 15, 6, 7, 10, 15, 6, 2, 1, 0},
							  {0, 1, 2, 3, 4, 5, 6, 1, 3, 6, 3, 6, 7, 10, 12, 3, 4, 5, 6, 7, 10, 15, 6, 3, 6, 3, 6, 7, 10, 11, 14, 11, 14, 11, 11, 12, 1, 2, 13, 14, 15, 6, 3, 6, 7, 10, 12, 13, 15, 6, 13, 14, 11, 14, 12, 3, 4, 5, 6, 1, 2, 1, 0} ,
							  {0, 13, 14, 11, 12, 13, 15, 6, 3, 6, 3, 6, 3, 6, 7, 10, 12, 3, 6, 13, 14, 11, 12, 13, 15, 6, 3, 6, 3, 6, 3, 6, 7, 10, 12, 3, 6, 7, 10, 1, 3, 6, 1, 2, 1, 1, 2, 0}
						  };


	Image spritesImg;
	byte spritesCor[];
	Image tilesImg;
	Image menuImg;
	Image menuSprImg;

	Image caratula;

	byte[] faseMap;
	byte[] faseMapFichas;
	int faseStartY;
	int nFichas;
	int scrollXS;

	Font font;


	// -------------------
	// Canvas INI
	// ===================

	public void CanvasINI() {
		sc = new Scroll();
		System.gc();
	}


	// -------------------
	// Canvas SET
	// ===================

	public void CanvasSET() {
		CanvasImageSET("/logo.png", -1);
		SoundINI();
		font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
	}


	// -------------------
	// Canvas RUN
	// ===================

	public void CanvasRUN() {
		LCD_Gfx.setFont(font);

		if (CanvasFillPaint) {
			CanvasFillPaint=false;
			CanvasFill(CanvasFillRGB);
		}

		if (CanvasImg!=null) {
			ImageSET(CanvasImg);
			CanvasImg=null;
			System.gc();
		}

		CanvasDRAW();
	}


	// -------------------
	// Canvas DRAW
	// ===================

	public void CanvasDRAW() {

		if(ga.GameExit == 1)
			return;

		if(caratulaDraw) {
			CanvasFillPaint = false;
			//caratulaLoad(); MONGOFIX
			caratula = new Image();
			caratula._createImage("/caratula.png");
			CanvasFill(0);
			ImageSET(caratula);

			caratulaDraw = false;
		}

		if(ga.GameStatus == 0) {
			if(((ga.GameMilis/300)%5) > 0) {
				drawStringShad(ga.Textos[ga.TEXT_PRESSANY][0], (CanvasSizeX>>1), CanvasSizeY - 2, -1, Graphics.BOTTOM | Graphics.HCENTER, CanvasSizeX);
			}
		}

		if(juegoDraw) {
			JuegoDRAW();
			juegoDraw = false;
		}

		if(scoresDraw) {
			drawScores();
			scoresDraw = false;
		}

		if(menuDraw) {
			ga.ma.MarcoIMP(LCD_Gfx);
			menuDraw = false;
		}

		if(loadingDraw) {
			CanvasFill(0);
			drawStringShad(ga.Textos[ga.TEXT_LOADING][0], (CanvasSizeX>>1), (CanvasSizeY>>1), -1, Graphics.VCENTER|Graphics.HCENTER, CanvasSizeX);
			loadingDraw = false;
		}
	}



	// -------------------
	// GameLoad
	// ===================
	public void GameLoad() {
		caratula = null;
		System.gc();

		menuImg = LoadImage("/menu_juego.png");
		System.gc();
		menuSprImg = LoadImage("/menu_juego_sprites.png");
		System.gc();

		spritesImg = LoadImage("/sprites.png");
		System.gc();
		spritesCor = ga.LoadFile("/sprites.cor");
		System.gc();

		caratulaLoad();
	}



	public void putSprite(int spr, int x, int y) {
		/*
		 - ADD X , ADD Y,
		 - Size X , Size Y,
		 - Coor X , Coor Y
		*/
		spr *= 6;

		if(spr >= spritesCor.length) {
			return;
		}

		int addx = spritesCor[spr++];
		int addy = spritesCor[spr++];
		int sx = spritesCor[spr++];
		int sy = spritesCor[spr++];
		int coorx = (int)spritesCor[spr++] + 128;
		int coory = (int)spritesCor[spr++] + 128;


		ImageSET(spritesImg, coorx, coory, sx, sy, x + addx, y + addy);
	}



	int nextLevel = 0;
	int nextStage = 0;

	int currentStage = -1;
	int currentLevel = -1;

	String stages[] = {"playa", "rio", "canal"};

	public void caratulaLoad() {
		if(caratula == null) {
			caratula = LoadImage("/caratula.png");
		}
	}


	public void buildMap(int nFichas, int level, byte[] lev, byte[] map, byte[] mapFichas) {
		int id = 0;
		for(int f = 0; f < nFichas; f++) {
			int io = (lev[f])*112;
			for(int c = 0; c < 112; c++, io++, id++) {
				map[id] = mapFichas[io];
			}
		}
	}


	private void LoadMAP(int stage, int level) {
		unloadMAP();

		nFichas = MAPAFICHAS[level].length;
		ga.nFichas8 = nFichas * 8;

		// Cargamos colisiones
		ga.faseCol = new byte[112*nFichas];
		System.gc();
		byte[] faseColFichas = ga.LoadFile("/col_" + stages[stage] + ".map");
		System.gc();
		buildMap(nFichas, level, MAPAFICHAS[level], ga.faseCol, faseColFichas);
		faseColFichas = null;
		System.gc();

		// Cargamos Nivel
		faseMap = new byte[112*nFichas];
		System.gc();
		faseMapFichas = ga.LoadFile("/mapa_" + stages[stage] + ".map");
		System.gc();
		buildMap(nFichas, level, MAPAFICHAS[level], faseMap, faseMapFichas);
		faseMapFichas = null;
		System.gc();


		faseStartY = ((nFichas<<3) - 2)<<3;


		// Cargamos Stage
		tilesImg = null;
		System.gc();
		tilesImg = LoadImage("/" + stages[stage] + ".png");
		System.gc();


		sc.ScrollINI(CanvasSizeX - 24, CanvasSizeY);
		sc.ScrollSET(faseMap, 14, nFichas<<3, tilesImg, 16);
		System.gc();

		currentStage = stage;
		currentLevel = level;
	}


	public void unloadMAP() {
		caratula = null;
		faseMap = null;
		faseMapFichas = null;
		tilesImg = null;
		ga.faseCol = null;
		sc.ScrollEND();
		System.gc();

		currentStage = -1;
		currentLevel = -1;
	}


	int nplayer = 0;


	public void JuegoLoad() {
		LoadMAP(nextStage, nextLevel); // nivel, fase
	}


	public void JuegoUnload() {
		unloadMAP();
	}



	// -------------------
	// sortPlayers
	// ===================
	private void sortPlayers() {
		// ordenar y actualizar posiciones de los jugadores
		for(int cont = 0; cont < (ga.nplayers - 1); cont++) {
			if(ga.player[cont].y > ga.player[cont + 1].y && ga.player[cont].pos == 0) {
				Moto tmp = ga.player[cont + 1];
				ga.player[cont + 1] = ga.player[cont];
				ga.player[cont] = tmp;
			}
		}

		//  nplayer = 0; // ver al ganador

		for(int count = 0; count < ga.nplayers; count++) {
			if ( !ga.player[count].isComputer ) {
				nplayer = count;
				break;
			}
		}
	}


	// -------------------
	// drawScores
	// ===================
	public void drawScores() {
		drawStringShad(ga.Textos[ga.TEXT_DIFFICULTY][currentStage], (CanvasSizeX>>1), 20, -1, Graphics.TOP|Graphics.HCENTER, CanvasSizeX);

		for(int count = 0; count < 10; count += 2) {
			int y = 35 + (count * font.getHeight() >> 1);
			int sc = Integer.parseInt(ga.scoreData[count + currentStage*10 + 1]);
			String str = Integer.toString(sc/10) + "." + Integer.toString(sc%10);

			drawStringShad( Integer.toString(currentStage + 1) + "/" + Integer.toString(count/2 + 1), 5*CanvasSizeX/128, y, -1, Graphics.TOP|Graphics.LEFT, CanvasSizeX);
			drawStringShad(ga.scoreData[count + currentStage*10    ], 30*CanvasSizeX/128, y, -1, Graphics.TOP|Graphics.LEFT, CanvasSizeX);
			drawStringShad(str, 110*CanvasSizeX/128, y, -1, Graphics.TOP|Graphics.RIGHT, CanvasSizeX);
		}
	}



	// -------------------
	// drawPlayers
	// ===================
	private void drawPlayers() {
		// Dibujar eliminados
		for(int cont = 0; cont < ga.nplayers; cont++) {
			if(ga.player[cont].stat == 3)
				ga.player[cont].draw(this);
		}

		// Dibujar corredores (no eliminados)
		for(int cont = 0; cont < ga.nplayers; cont++) {
			if(ga.player[cont].stat != 3) {
				ga.player[cont].draw(this);
			}
		}
	}



	// -------------------
	// Jugar DRAW
	// ===================
	public void JuegoDRAW() {

		if(ga.GameStatus == 100 || ga.GameStatus == 101) { // loading
			return;
		}

		sortPlayers();

		sc.ScrollRUN_Centro_Max(ga.player[nplayer].x * 3 / 16, ga.player[nplayer].y * 3 / 16);
		sc.ScrollIMP(LCD_Gfx);

		drawPlayers();

		JuegoDRAW_Menu();

		LCD_Gfx.setClip( 0,0, CanvasSizeX, CanvasSizeY );
		LCD_Gfx.setColor(-1);

		if(ga.banner != null) {
			drawStringShad(ga.banner, CanvasSizeX/2,(CanvasSizeY - 12)/2, -1, Graphics.VCENTER|Graphics.HCENTER, CanvasSizeX);
			ga.banner = null;
		}


		if(ga.GameStatus == 104 && ga.frameCounter > ga.fps*4) {
			int cpos = 0;
			for(int cont = 0; cont < ga.nplayers; cont++) {
				if(ga.player[cont].pos != 0) {
					if(ga.player[cont].isComputer) {
						drawStringShad(ga.Textos[ga.TEXT_CPU][0] + " " + ga.player[cont].color, 10*CanvasSizeX/128,30+cpos, -1, Graphics.LEFT|Graphics.TOP, CanvasSizeX);
					} else {
						drawStringShad(ga.Textos[ga.TEXT_PLAYER][0], 10*CanvasSizeX/128,30+cpos, -1, Graphics.LEFT|Graphics.TOP, CanvasSizeX);
					}

					drawStringShad(Integer.toString(ga.player[cont].ftime/1000) + "." + Integer.toString((ga.player[cont].ftime/100)%10), 110*CanvasSizeX/128, 30+cpos, -1, Graphics.RIGHT|Graphics.TOP, CanvasSizeX);

					cpos += font.getHeight();
				}
			}

			if(((System.currentTimeMillis()/200)&1) == 0) {
				if(ga.player[nplayer].pos == 1) {
					if(ga.PrefsCompScore(currentLevel + currentStage*5, ga.player[nplayer].ftime, true)) {
						drawStringShad(ga.Textos[ga.TEXT_TONAME][0], (CanvasSizeX>>1), CanvasSizeY - 2, -1, Graphics.HCENTER|Graphics.BOTTOM, CanvasSizeX);
					} else {
						drawStringShad(ga.Textos[ga.TEXT_TONEXT][0], (CanvasSizeX>>1), CanvasSizeY - 2, 0x00FF00, Graphics.HCENTER|Graphics.BOTTOM, CanvasSizeX);
					}
				} else {
					drawStringShad(ga.Textos[ga.TEXT_TORESTART][0], (CanvasSizeX>>1), CanvasSizeY - 2, 0xFFFF00, Graphics.HCENTER|Graphics.BOTTOM, CanvasSizeX);
				}
			}
		}

		if(ga.GameStatus == 110) {
			drawStringShad(ga.Textos[ga.TEXT_TONEXT][0], (CanvasSizeX>>1), CanvasSizeY - 2, 0x00FF00, Graphics.HCENTER|Graphics.BOTTOM, CanvasSizeX);
		}

		/*
		  String s = Integer.toString((int)(currentStage)) + "/" +
		             Integer.toString((int)(currentLevel));
		  if(nplayer >= 0 && nplayer < ga.nplayers && ga.player[nplayer].y > 0) {
		    int pficha = ga.player[nplayer].y/(64*8);
		    s += " - " + Integer.toString(pficha); 
		    
		    if(MAPAFICHAS[currentLevel].length > pficha && pficha >= 0) {
		      s += " - " + Integer.toString(MAPAFICHAS[currentLevel][pficha]);
		    }
		  }
		  
		  drawStringShad(s, 2, 2, -1, Graphics.LEFT|Graphics.TOP, CanvasSizeX - 24);
		*/
	}


	// -------------------
	// JuegoDRAW_Menu()
	// ===================

	private void JuegoDRAW_Menu() {
		int mx = CanvasSizeX - 24;

		ImageSET(menuImg, mx,0);

		// Draw vel
		int alt = -ga.player[nplayer].vy * 63 / 24;
		LCD_Gfx.setColor(0x011c35);
		LCD_Gfx.fillRect(mx+6, 140,  14, 63 - alt);

		// numeros
		int val = (int)((ga.currentTimeGame - ga.timeGameInit) / 100);
		if(ga.player[nplayer].pos != 0) {
			val = ga.player[nplayer].ftime / 100;
		}

		if(val < 1000 && val >= 0) {
			int pos = 0;
			while(val > 0 || (val == 0 && pos == 0)) {
				int d = val % 10;

				ImageSET(menuSprImg, d*5, 0, 4, 7, mx + 16 - (pos*5), 126);

				pos++;
				val /= 10;
			}
		}

		drawStringShad(Integer.toString(nplayer + 1), mx + 6, 108, -1, Graphics.LEFT|Graphics.BOTTOM, 12);

		// ponemos posiciones en el ranking
		for(int cont = ga.nplayers - 1; cont >= 0; cont--) {
			if(ga.player[cont].y > 0) {
				// 18,5 1,84
				ImageSET(menuSprImg, 0, 7 + (ga.player[cont].color*3), 4, 3, mx + 16, 5 + ((ga.player[cont].y * 108 / nFichas) >> 9) );
			}
		}
	}


	// <=- <=- <=- <=- <=-



	/* ===================================================================
	 
		SoundINI()
		----------
			Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)
	 
		SoundSET(n� Sonido , Repetir)
		-----------------------------
			Hacemos que suene un sonido (0 a x) y que se repita x veces.
			Repetir == 0: Repeticion infinita
	 
		SoundRES()
		----------
			Paramos el ultimo sonido.
	 
		SoundRUN()
		----------
			Debemos ejecutar este metodo en CADA ciclo para gestionar 'tiempos'
	 
		VibraSET(microsegundos)
		-----------------------
			Hacemos vibrar el mobil x microsegundos
	 
	=================================================================== */



	// *******************
	// -------------------
	// Sound - Engine - Rev.0 (28.11.2003)
	// -------------------
	// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
	// ===================
	// *******************

	Player[] Sonidos;

	int SoundOld = -1;

	// -------------------
	// Sound INI
	// ===================

	public void SoundINI() {
		Sonidos = new Player[3];

		/*
		Sonidos[0] = SoundCargar("/Astro_muerte.mid");
		Sonidos[1] = SoundCargar("/Astro_intro.mid");
		Sonidos[2] = SoundCargar("/Astro_item.mid");
		Sonidos[3] = SoundCargar("/Astro_level.mid");
		Sonidos[4] = SoundCargar("/Astro_explosion.mid");
		*/

		Sonidos[0] = SoundCargar("/water_race.mid");
		Sonidos[1] = SoundCargar("/water_salida.mid");
		Sonidos[2] = SoundCargar("/water_golpe.mid");
	}

	public Player SoundCargar(String Nombre) {
		Player p = null;

		try {
			//InputStream is = getClass().getResourceAsStream(Nombre);
			p = Manager.createPlayer( Nombre , "audio/midi");
			p.realize();
			p.prefetch();
		} catch(Exception exception) {
			exception.printStackTrace();
		}

		return p;
	}

	// -------------------
	// Sound SET
	// ===================

	public void SoundSET(int Ary, int Loop) {
		if(Ary > 2) {
			Ary = 2;
		}

		Loop = 1;

		if (Loop<1) {
			Loop=-1;
		}

		SoundRES();

		try {
			VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl");
			if (vc != null) {
				vc.setLevel(80);
			}
			Sonidos[Ary].setLoopCount(Loop);
			Sonidos[Ary].start();
		} catch(Exception exception) {
			exception.printStackTrace();
		}

		SoundOld=Ary;
	}

	// -------------------
	// Sound RES
	// ===================

	public void SoundRES() {
		if (SoundOld != -1) {
			try {
				Sonidos[SoundOld].stop();
			} catch (Exception e) {}

			SoundOld = -1;
		}
	}

	// -------------------
	// Sound RUN
	// ===================

	public void SoundRUN() {}

	// -------------------
	// Vibra SET
	// ===================






















	public void VibraSET(int Time) {
		if (ga.GameVibra!=0) {
			try {
				Display.getDisplay(ga).vibrate(Time);
			} catch (Exception e) {}
		}
	}

	// <=- <=- <=- <=- <=-


	// -------------------
	// StringFIX
	// ===================
	/*
	  *  Corta la cadena por los espacios antes de final de linea o por donde 
	  * encuentre el caracter \n
	  */
	public String[] _StringFIX(String texto, int width, Font f) {
		int pos = 0, posIni = 0, posOld = 0, size = 0;
		int[] positions = new int[512];
		int count = 0;


		while ( posOld < texto.length() ) {
			size = 0;

			pos = posIni;

			while ( size < width ) {
				if ( pos == texto.length()) {
					posOld = pos;
					break;
				}

				int dat = texto.charAt(pos++);

				if ( dat == '\n') {
					posOld = pos - 1;
					break;
				} // si encontramos un salto de linea, salimos

				if ( dat==0x20 ) {
					posOld = pos - 1;
				}
				size += f._charWidth((char)dat);
			}

			if (posOld - posIni < 1) {
				while ( pos < texto.length() && texto.charAt(pos) >= 0x30 ) {
					pos++;
				}

				posOld = pos;
			}

			posIni = posOld + 1;

			positions[count] = posOld;
			count++;
		}

		String str[] = new String[count];
		posIni = 0;
		int posEnd;

		for(int i = 0; i < count; i++) {
			posEnd = positions[i];
			str[i] = texto.substring(posIni, posEnd);
			posIni = posEnd + 1;
		}

		return str;
	}


	// -------------------
	// drawStringShad
	// ===================
	/*
	 *  Dibuja la cadena en las coordenadas x,y usando el anchor (utilizando 
	 * incluso VCENTER ) y color.
	 * Corta la cadena por los espacios antes de final de linea o por donde 
	 * encuentre el caracter \n
	 */
	private void drawStringShad(String str, int x, int y, int color, int anchor, int width) {
		// primero partimos la cadena
		Font f = LCD_Gfx.getFont();
		int height = f.getHeight();
		String strings[] = _StringFIX(str, width, f);

		if((anchor&Graphics.VCENTER) != 0) {
			y -= strings.length*height / 2;
			anchor ^= Graphics.VCENTER;
			anchor |= Graphics.TOP;
		} else {
			if((anchor&Graphics.BOTTOM) != 0) {
				y -= (strings.length - 1)*height;
			}
		}

		LCD_Gfx.setClip( 0,0, CanvasSizeX, CanvasSizeY );

		for(int count = 0; count < strings.length; count++) {
			LCD_Gfx.setColor((color>>2)&0x3F3F3F);
			LCD_Gfx.drawString(strings[count], x - 1, y - 1, anchor);

			LCD_Gfx.setColor(color);
			LCD_Gfx.drawString(strings[count], x, y, anchor);

			y += height;
		}
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
} 	// Cierra la Clase GameCanvas
// **************************************************************************//