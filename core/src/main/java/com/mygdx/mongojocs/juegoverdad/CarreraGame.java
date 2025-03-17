package com.mygdx.mongojocs.juegoverdad;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class CarreraGame {
	
	public int scores;
	public Image sprites[];
	public int gameExit;

	int dif;

	public CarreraGame(Game ga) {
		this.ga = ga;
		this.gc = ga.gc;
	}

	/**
	 *	Inicializa el mini juego del supermercado
	 *	Recibe por par�metros la puntuci�n inicial (si es necesario acumularla),
	 *	la dificultad 'relativa' donde el valor 1024 equivale a la velodidad 
	 *	'normal' y el tiempo para el timeout (0 indica tiempo infinito)
	 */
	public void playInit(int puntos, int dif, int time) {

		scores = puntos;
		this.dif = dif;
		difficulty = ( 40 << 10 ) / (dif + 100) + 3;
		oponente = (ga.RND(difficulty) + 4) >> 1;
		continuar = -1;

		canvasWidth = gc.canvasWidth;
		canvasHeight = gc.canvasHeight;

		gameExit = 0;

		gc.xPressed = 0;
		gc.yPressed = 0;
		gc.fPressed = 0;

		keyTime = 0;

		playerX[0] = playerX[1] = 0;
		playerX2[0] = playerX2[1] = 0;
		playerV[0] = playerV[1] = 0;

		gc.canvasFillInit(0);
		gc.canvasTextInit(ga.gameText[ga.TEXT_LOADING][0], 0, 0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER);
		gc.gameDraw();
		System.gc();

		if (sprites == null) {
//#ifdef J2ME
			sprites = gc.loadImage("/4_car", 11);
//#else
//#endif
			
			fondoWidth = sprites[8].getWidth();
			fondoHeight = sprites[8].getHeight();
			
			System.gc();
		}
		
		MAX_X = time*fondoWidth;

		gc.miniGameScroll(0xFF0000, 0x830000, sprites[9], ga.gameText[ga.TEXT_CARRERA_INSTRUC][0]);

		lastFrameTime = -1;
		currentGameTime = 0;


		selectedPlayer = -2;
		ga.RNDSet();
		
		System.gc();
	}


	public void releaseGame() { sprites = null; System.gc(); }

	private int keyType, keyTime;
	private boolean keyFail;
	private int oponente = 0;
	private long playerV[] = new long[2];
	private long playerX[] = new long[2];
	private long playerX2[] = new long[2];
	private int keyTimeInitial = 64;
	private int keyTimeBlink = 16;
	private int selectedPlayer = -2;


	/**
	 *	Devuelve true si es necesario salir del juego. this.gameExit valdr� el 
	 * c�digo de salida
	 */
	public boolean playTick() {
		long dT = ga.GameMilis - lastFrameTime;
		if (lastFrameTime == -1) {
			lastFrameTime = ga.GameMilis;
		} else {
			if (dT > 1000) {
				dT = 0;
			}
			
			if(dT > 0 && dT < 1000) {
				currentGameTime += dT;
			}
		}

		lastFrameTime = ga.GameMilis;


		int xP = gc.xPressed;
		int yP = gc.yPressed;
		int fP = gc.fPressed;
		gc.xPressed -= xP;
		gc.yPressed -= yP;
		gc.fPressed -= fP;
		
		//Controlamos la selecci�n de jugador
		if (selectedPlayer  < 0) {
			selectedPlayer = ( (selectedPlayer + 8 + xP + yP) % 2 ) - 2;
			
			if (fP != 0) {
				selectedPlayer += 2;
			} else {
				gc.canvasTextInit(ga.gameText[ga.TEXT_CARRERA_SELECT_PLAYER][0], 0, 8, 0xFFFFFF, gc.TEXT_TOP | gc.TEXT_HCENTER);
			}
			
			return false;
		}
		
		if (continuar == -1) {
			
			//  Si no hay ninguna tecla para visualizar hechamos a suerte 
			// si ser� necesario
			if (keyTime > 0) {
				keyTime--;
			} else {
				if (keyTime == 0) {
					keyType = (ga.RND(16)%3 + 1 + keyType)%4;
					keyTime = keyTimeInitial;
					keyFail = false;
				}

				if (keyTime < 0) {
					keyTime++;
				}
			}

			//Actulizamos posiciones de los jugadores segun sus velocidades
			playerX[0] += (int)( ( (long)playerV[0] * fondoWidth * dT ) >> 10 );
			playerX[1] += (int)( ( (long)playerV[1] * fondoWidth * dT ) >> 10 );


			//Comprovamos que el jugador haya presionado la tecla correcta
			boolean ok = false;
			boolean fail = false;

			if (keyTime > 0 && ( xP != 0 || yP != 0 ) ) {
				fail = true;
				switch(keyType) {
					case SPR_FLEIZQ:
						if (xP < 0 && yP == 0) {
							fail = false;
							ok = true;
						}
					break;
					case SPR_FLEDER:
						if (xP > 0 && yP == 0) {
							fail = false;
							ok = true;
						}
					break;
					case SPR_FLEABA:
						if (yP > 0 && xP == 0) {
							fail = false;
							ok = true;
						}
					break;
					case SPR_FLEARR:
						if (yP < 0 && xP == 0) {
							fail = false;
							ok = true;
						}
					break;
				}
			}

			if (fail) {
				keyTime = keyTimeBlink;
				keyFail = true;
				playerV[selectedPlayer] -= 50;
			}
			
			if (ok) {
				keyTime = -6;
				playerV[selectedPlayer] += ((MAX_VEL - playerV[selectedPlayer]) >> 4) * 3;
			}

			playerV[selectedPlayer] -= 3;		
			if (playerV[selectedPlayer] < 0) {
				playerV[selectedPlayer] = 0;
			}


			//  Inteligencia del oponente, simplemente hecha los dados jugamos con la
			// estadistica para hacer que sea mas bueno o mas malo  en la carrera
			boolean imp = false;
			playerV[1 - selectedPlayer] -= 3;		

			if (oponente <= 0) {
				imp = true;
				oponente = ga.RND(difficulty) + 8;
			} else {
				oponente--;
			}

			if (imp) {
				int dir = 1;
				if (ga.RND(100/(difficulty + 1) + 10) == 0) {
					dir = -1;
				}
				playerV[1 - selectedPlayer] += dir * ((MAX_VEL - playerV[1 - selectedPlayer]) >> 4) * 3;
			}

			//Comprovar que nunca vayamos hacia atras
			if (playerV[0] < 0) {
				playerV[0] = 0;
			}

			if (playerV[1] < 0) {
				playerV[1] = 0;
			}

		} else {
			//Actulizamos posiciones de los jugadores segun sus velocidades
			playerX2[0] += (int)( ( (long)playerV[0] * fondoWidth * dT ) >> 10 );
			playerX2[1] += (int)( ( (long)playerV[1] * fondoWidth * dT ) >> 10 );
			keyTime = 0;
		}

		if ( (playerX[0] > (MAX_X << 10)) && (continuar == -1) ) {
			continuar = 100;
		}

		if ( (playerX[1] > (MAX_X << 10)) && ( continuar == -1) ) {
			continuar = 100;
		}

		if ( (continuar != -1) && (continuar > 1)) {
			int y = (canvasHeight>>1) - (fondoHeight>>1);
//#ifndef	SMALL_GFX
			if (selectedPlayer != 0) {
				y -= fondoHeight + BLANCO;
			}
//#endif
			if (playerX[selectedPlayer] > playerX[1 - selectedPlayer]) {
				// has ganado
				gc.canvasTextInit(ga.gameText[ga.TEXT_CARRERA_UWON][0], 0, y, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER);
			} else {
				// has perdido
				gc.canvasTextInit(ga.gameText[ga.TEXT_CARRERA_ULOST][0], 0, y, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER);
			}
		}

		if (continuar > 0) {
			continuar--;
		}

		if ( continuar == 0 ) {
			if (playerX[selectedPlayer] > playerX[1 - selectedPlayer]) {
				// has ganado
				gameExit = 1;
				scores = (int)((((playerX[selectedPlayer] - playerX[1-selectedPlayer])*(dif)) >> 10 ) >> 12);
			} else {
				// has perdido
				gameExit = 3;
			}
			return true;
		}

		return false;
	}

	final static int BLANCO = 2;
	final static int BARRA = 8;
	private int selectBlink = 0; 

	public void playDraw() {
		scr = gc.scr;
//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif

		int VelWidth = canvasWidth - 24 - 2;

		scr.setColor(0xFFFFFF);
		scr.fillRect(0, canvasHeight - fondoHeight - BLANCO, canvasWidth, BLANCO);

		// Dibujar marcadores
		scr.setColor(0);
		scr.fillRect(0, 0, canvasWidth, canvasHeight - fondoHeight*2 - BLANCO);
//#ifndef SMALL_GFX 
		scr.fillRect(0, 0, canvasWidth, canvasHeight - fondoHeight*2 - BLANCO);
//#else
//#endif

		if (selectedPlayer >= 0) {
//#ifndef SMALL_GFX 
			int alty = canvasHeight - (BLANCO + fondoHeight*2) - BARRA - (BARRA>>2)*2;
//#else
//#endif
			gc.textDraw("Vel:", 2, (alty>>2) + (BARRA>>1) - (canvasHeight>>1), 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_LEFT);
			scr.setColor(0x404040);
			scr.fillRect( 24, (alty>>2), VelWidth, BARRA );
			if (selectedPlayer >= 0) {
				scr.setColor(0xD04040);
				scr.fillRect( 24, (alty>>2), (int)((VelWidth * playerV[selectedPlayer]) / MAX_VEL), BARRA );
			}

			scr.setColor( 0xF06EAA );
			scr.fillRect( 24, (alty>>1) + BARRA, (int)( ( VelWidth * playerX[1] ) >> 10 ) / MAX_X, BARRA>>2 );
			scr.setColor( 0x00A651 );
			scr.fillRect( 24, ((alty*3)>>2) + BARRA + (BARRA>>2), (int)( ( VelWidth * playerX[0] ) >> 10 ) / MAX_X, BARRA>>2 );
		}
		
//#ifndef SMALL_GFX 
		// Dibujar fondos
		putSprite(0, 8, -(int)(playerX[0]>>10)%fondoWidth, canvasHeight - fondoHeight);
		putSprite(0, 8, -(int)(playerX[0]>>10)%fondoWidth + fondoWidth, canvasHeight - fondoHeight);
		putSprite(0, 8, -(int)(playerX[0]>>10)%fondoWidth + fondoWidth*2, canvasHeight - fondoHeight);

		putSprite(0, 8, -(int)(playerX[1]>>10)%fondoWidth, canvasHeight - fondoHeight*2 - BLANCO);
		putSprite(0, 8, -(int)(playerX[1]>>10)%fondoWidth + fondoWidth, canvasHeight - fondoHeight*2 - BLANCO);
		putSprite(0, 8, -(int)(playerX[1]>>10)%fondoWidth + fondoWidth*2, canvasHeight - fondoHeight*2 - BLANCO);

		// Dibujar players
		putSprite(0, 4 + (int)((playerX[0] + playerX2[0])>>14)%2, 32 + (int)(playerX2[0]>>10), canvasHeight - 3 );
		putSprite(0, 6 + (int)((playerX[1] + playerX2[1])>>14)%2, 32 + (int)(playerX2[1]>>10), canvasHeight - fondoHeight - BLANCO - 3 );

//#else
//#endif

		// Dibujar tecla
		if ( selectedPlayer >= 0 ) {
			if ( (keyTime > 0) && ( ( (keyTime > keyTimeBlink) || (keyTime & 1) != 0 ) || keyFail ) ) {
				int i8 = fondoHeight >> 1;
//#ifndef SMALL_GFX 
				int y = canvasHeight - selectedPlayer*(fondoHeight+BLANCO) - i8;
//#else
//#endif
				putSprite(0, keyType, canvasWidth - i8, y);
				if (keyFail && ( (keyTime > keyTimeBlink) || (keyTime & 2) != 0 ) ) {
					putSprite(0, 10, canvasWidth - i8, y);
				}
			}
		}

//#ifndef SMALL_GFX 
		if (selectedPlayer < 0) {
			selectBlink++;
			if ((selectBlink & 2) != 0) {
//#ifdef J2ME
				scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif
				int y = canvasHeight - fondoHeight;
				
				if (selectedPlayer == -1) y -= fondoHeight + BLANCO;
				
				scr.setColor(0xFF0000);
				scr.fillRect(0, y, BLANCO, fondoHeight); // |*
				scr.fillRect(BLANCO, y + fondoHeight - BLANCO, canvasWidth, BLANCO); // _
				scr.fillRect(canvasWidth - BLANCO, y, BLANCO, fondoHeight); // *|
				scr.fillRect(0, y, canvasWidth, BLANCO); // ��
			}
		}
//#endif
	}


	private Game ga;
	private GameCanvas gc;
	private Graphics scr;

	private int canvasWidth;
	private int canvasHeight;
	private int fondoWidth;
	private int fondoHeight;


	// Variables espec�ficas de este juego
	private long lastFrameTime;
	private long currentGameTime;

	private int difficulty;
	
	private final static int MAX_VEL = 2000;
	private int MAX_X;
	private int continuar = -1;
	
	private final static int SPR_FLEIZQ = 0;
	private final static int SPR_FLEDER = 1;
	private final static int SPR_FLEABA = 2;
	private final static int SPR_FLEARR = 3;


	private void putSprite(int bank, int spr, int x, int y) {
		switch(bank) {
		case 0:
			if ( spr >= 0 ) {
				if ( spr <= 3 || spr == 10) {
					gc.showImage(sprites[spr], 0, 0, sprites[spr].getWidth(), sprites[spr].getHeight(), x - (sprites[spr].getWidth() >> 1), y - (sprites[spr].getHeight() >> 1) );
				} else {
					if (spr <= 7) {
						gc.showImage(sprites[spr], 0, 0, sprites[spr].getWidth(), sprites[spr].getHeight(), x - (sprites[spr].getWidth() >> 1), y - sprites[spr].getHeight() );
					} else {
						gc.showImage(sprites[spr], 0, 0, sprites[spr].getWidth(), sprites[spr].getHeight(), x, y );
					}
				}
			}
			break;
		}
	}
}

