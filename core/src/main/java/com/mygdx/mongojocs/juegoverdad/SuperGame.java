package com.mygdx.mongojocs.juegoverdad;//#ifdef J2ME
//#endif


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class SuperGame {
	
	public int scores;
	public Image sprites[];
	public int gameExit;

	public SuperGame(Game ga) {
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

		timeOut = 1000 * time;

		canvasWidth = gc.canvasWidth;
		canvasHeight = gc.canvasHeight;


		G = ( canvasHeight * dif * (Game.FRAMETIME + (Game.FRAMETIME >> 1))) >> 9;
		//System.out.println(G);

		dropStatistic = Game.FPS + 5;

		gameExit = 0;

		superLives = 3;
		superCartX = 0;
		superCartY = canvasHeight*3/4;
		superCartProd = -1;

		//scores = 0;
		fallingProdsN = 0;

		spark1Count = 0;
		spark2Count = 0;

		gc.xPressed = 0;
		gc.yPressed = 0;
		gc.fPressed = 0;

		congelar = 0;

		gc.canvasFillInit(0);
		gc.canvasTextInit(ga.gameText[ga.TEXT_LOADING][0], 0, 0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER);
		gc.gameDraw();

		if (sprites == null) {
//#ifdef J2ME
			sprites = gc.loadImage("/3_superSprites", 7);
//#else
//#endif

			SPR_CARTWIDTH = sprites[SPR_CART].getWidth();
			SPR_CARTHEIGHT = sprites[SPR_CART].getHeight();

			SPR_PRODWIDTH = sprites[SPR_PROD1].getWidth();
			SPR_PRODHEIGHT = sprites[SPR_PROD1].getHeight();

			SPR_HEARTWIDTH = sprites[SPR_HEART].getWidth();

			SPR_SPARKHEIGHT = sprites[SPR_SPARK].getHeight();

			System.gc();
		}

		gc.miniGameScroll(0x001970, 0x0039FF, sprites[6], ga.gameText[ga.TEXT_SUPER_INSTRUC][0]);

		lastFrameTime = -1;
		currentGameTime = 0;

		ga.RNDSet();
	}

	public void releaseGame() { sprites = null; System.gc(); }

	/**
	 *	Devuelve true si es necesario salir del juego. this.gameExit valdr� el 
	 * c�digo de salida
	 */
	public boolean playTick() {
		long dT = ga.GameMilis - lastFrameTime;
		if (lastFrameTime == -1) {
			lastFrameTime = ga.GameMilis;
		} else {
			if(dT > 0 && dT < 1000) {
				currentGameTime += dT;
			}
		}
		
//#ifdef SMALL_GFX
//#endif

		lastFrameTime = ga.GameMilis;

		int xP = gc.xPressed;
		gc.xPressed -= xP;		
		int lastSuperCartX = superCartX;
		superCartX += xP;


		if (superCartX >  1)
			superCartX =  1;
		if (superCartX < -1)
			superCartX = -1;


		if ((fallingProdsN < MAX_PRODS) &&
				( ( (fallingProdsN!= 0) && 
					((fallingProdPosY[fallingProdsN - 1] >> 12) > SPR_PRODHEIGHT*3) ) ||
				  ( fallingProdsN == 0 ) ) ) {
			boolean drop = ga.RND(dropStatistic) <= 1;
			if (drop) {
				fallingProdPosX[fallingProdsN] = ga.RND(256)%3 - 1;
				fallingProdType[fallingProdsN] = (6 + fallingProdPosX[fallingProdsN] + 1 + (((ga.RND(256)&1)*2) - 1) ) % 3;
				fallingProdPosY[fallingProdsN] = (-(SPR_PRODHEIGHT >> 1) << 12);
				fallingProdsN++;
			}
		}

		// Soltar el producto
		if (gc.fPressed != 0) {
			if (superCartProd != -1) {
				if (superCartProd != (superCartX + 1)) {
					spark2Count = 8;
					spark2Type = superCartProd;
					spark2Pos = superCartX;

					superLives--;
					if (superLives == 0) {
						congelar = CONGELAR;
						spark2Count = CONGELAR;
					}
				} else {
					scores += 15;
					if (timeOut != 0 ) {
						if ((scores % 100) == 0) {
							G += G >> 4;
						}
					}
				}

				superCartProd = -1;
			}

			gc.fPressed = 0;
		}


		// Gravedad, dejar caer los productos
		for (int count = 0; count < fallingProdsN; count++) {
			fallingProdPosY[count] += (G >> 2);
		}


		// Comprovamos que caigan al suelo
		if (fallingProdsN > 0) {
			if ( (fallingProdPosY[0] >> 12) > (canvasHeight*3/4 + ((SPR_CARTHEIGHT>>1) - (SPR_PRODHEIGHT>>1)))) {

				if ( (fallingProdPosX[0] != superCartX) || (superCartProd != -1) ) {
					spark1Count = 8;
					spark1Type = fallingProdType[0];
					spark1Pos = fallingProdPosX[0];

					gc.vibraInit(200);

					superLives--;
					if (superLives == 0) {
						congelar = CONGELAR;
						spark1Count = CONGELAR;
					}
				} else {
					superCartProd = fallingProdType[0];
				}

				fallingProdsN--;
				for (int c = 0; c < fallingProdsN; c++) {
					fallingProdPosY[c] = fallingProdPosY[c + 1];
					fallingProdPosX[c] = fallingProdPosX[c + 1];
					fallingProdType[c] = fallingProdType[c + 1];
				}
			} else {
				if((fallingProdPosY[0] >> 12) > (canvasHeight*3/4 + ( (SPR_CARTHEIGHT>>1) - (SPR_PRODHEIGHT>>1) - CARTOFFSET ) ) &&
						(fallingProdPosX[0] == superCartX) && (superCartProd == -1) ) {
					superCartProd = fallingProdType[0];

					fallingProdsN--;
					for (int c = 0; c < fallingProdsN; c++) {
						fallingProdPosY[c] = fallingProdPosY[c + 1];
						fallingProdPosX[c] = fallingProdPosX[c + 1];
						fallingProdType[c] = fallingProdType[c + 1];
					}
				} else {
					if((fallingProdPosY[0] >> 12) > (canvasHeight*3/4 - ( (SPR_CARTHEIGHT>>1) + (SPR_PRODHEIGHT>>1) ) ) &&
					(fallingProdPosX[0] == lastSuperCartX) && (superCartProd == -1) && (lastSuperCartX != superCartX) ) {
						superCartProd = fallingProdType[0];
	
						fallingProdsN--;
						for (int c = 0; c < fallingProdsN; c++) {
							fallingProdPosY[c] = fallingProdPosY[c + 1];
							fallingProdPosX[c] = fallingProdPosX[c + 1];
							fallingProdType[c] = fallingProdType[c + 1];
						}
					}
				}
			}
		}


		if (spark1Count > 0)
			spark1Count--;
		if (spark2Count > 0)
			spark2Count--;


		if (congelar == 0 && superLives <= 0) {
			gameExit = 3;
			return true;
		}


		if (congelar > 0) {
			congelar--;
		}

		if (timeOut != 0 && timeOut < currentGameTime ) {
			gameExit = 1;
			return true;
		}

		gc.showBackground = true;

		return false;
	}

	public void playDraw() {
		scr = gc.scr;

		// Dibujar suelo
		scr.setColor(0x808080);
		scr.fillRect(0, canvasHeight*3/4 + (SPR_CARTHEIGHT>>1), canvasWidth, 2);
//#ifndef SMALL_GFX
//#endif

		putSprite(0, SPR_PROD1, -1*(canvasWidth>>2) + (canvasWidth>>1), canvasHeight - (SPR_PRODHEIGHT>>2));
		putSprite(0, SPR_PROD2,  0*(canvasWidth>>2) + (canvasWidth>>1), canvasHeight - (SPR_PRODHEIGHT>>2));
		putSprite(0, SPR_PROD3,  1*(canvasWidth>>2) + (canvasWidth>>1), canvasHeight - (SPR_PRODHEIGHT>>2));


		// Dibujamos los productos que van cayendo
		for (int count = 0; count < fallingProdsN; count++) {
			putSprite(0, SPR_PROD1 + fallingProdType[count],
					  (canvasWidth >> 1) + fallingProdPosX[count] * (canvasWidth >> 2),
					  (fallingProdPosY[count] >> 12));
		}


		// Dibujar carro
		if (superCartProd != -1) {
			putSprite(0, SPR_PROD1 + superCartProd, superCartX*(canvasWidth>>2) + (canvasWidth>>1),
					  superCartY + ((SPR_CARTHEIGHT>>1) - (SPR_PRODHEIGHT>>1)) - CARTOFFSET );
		}
		putSprite(0, SPR_CART, superCartX*(canvasWidth>>2) + (canvasWidth>>1) + CARTOFFSETX, superCartY);


		// Dibujamos las chispas
		if (spark1Count > 0) {
			if ((spark1Count & 2) != 0) {
				putSprite(0, SPR_SPARK,  spark1Pos*(canvasWidth>>2) + (canvasWidth>>1),
						  (canvasHeight*3/4) + ((SPR_CARTHEIGHT>>1) - (SPR_SPARKHEIGHT>>1)));
			}

			putSprite(0, SPR_PROD1 + spark1Type,  spark1Pos*(canvasWidth>>2) + (canvasWidth>>1),
					  (canvasHeight*3/4) + ((SPR_CARTHEIGHT>>1)- (SPR_PRODHEIGHT>>1)));
		}


		if (spark2Count > 0) {
			if ((spark2Count & 2) != 0) {
				putSprite(0, SPR_SPARK,  spark2Pos*(canvasWidth>>2) + (canvasWidth>>1),
						  canvasHeight - SPR_SPARKHEIGHT/2);
			}

			putSprite(	0, SPR_PROD1 + spark2Type,  spark2Pos*(canvasWidth>>2) + (canvasWidth>>1),
						canvasHeight - (SPR_PRODHEIGHT>>2));
		}

		int tmpx = canvasWidth / 12;
		for(int count = 0; count < superLives; count++) {
			putSprite(0, SPR_HEART, tmpx + (tmpx + SPR_HEARTWIDTH/2) * count, tmpx );
		}

//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif //J2ME

		gc.textDraw(Integer.toString(scores), - tmpx + (SPR_HEARTWIDTH>>1), tmpx - (canvasHeight>>1), 0xFFFFFF,
					GameCanvas.TEXT_RIGHT | GameCanvas.TEXT_VCENTER);

		if (timeOut != 0 && ((currentGameTime/200)%5)!=0) {
			gc.textDraw(Integer.toString((int)((timeOut - currentGameTime)/1000)), - tmpx + (SPR_HEARTWIDTH>>1), 
						tmpx - (canvasHeight>>1) + 12, 0xFFFFFF,
						GameCanvas.TEXT_RIGHT | GameCanvas.TEXT_VCENTER);
		}

	}

	
	
	


	private Game ga;
	private GameCanvas gc;
	private Graphics scr;

	private int canvasWidth;
	private int canvasHeight;




	// Variables espec�ficas de este juego

	private int dropStatistic;
	
	private int superLives;
	private int superCartX;
	private int superCartY;
	private int superCartProd;

	private int spark1Pos;
	private int spark1Type;
	private int spark1Count;

	private int spark2Pos;
	private int spark2Type;
	private int spark2Count;



	private int fallingProdType[] = new int[MAX_PRODS];
	private int fallingProdPosX[] = new int[MAX_PRODS];
	private int fallingProdPosY[] = new int[MAX_PRODS];
	private int fallingProdsN;
	private int G;

	private long timeOut = 0;
	
	private long lastFrameTime;
	private long currentGameTime;

	private int congelar;



	private final static int SPR_CART = 0;
	private final static int SPR_PROD1 = 1;
	private final static int SPR_PROD2 = 2;
	private final static int SPR_PROD3 = 3;
	private final static int SPR_HEART = 4;
	private final static int SPR_SPARK = 5;
	private final static int SPR_TITLE = 6;


	private int SPR_CARTWIDTH = 37;
	private int SPR_CARTHEIGHT = 22;
	private int SPR_PRODWIDTH = 24;
	private int SPR_PRODHEIGHT = 26;
	private int SPR_HEARTWIDTH = 14;
	private int SPR_SPARKHEIGHT = 37;


	private final static int MAX_PRODS = 8;
	private final static int CONGELAR = 25;

//#ifdef SMALL_GFX
//#else
	private final static int CARTOFFSETX = 3;
	private final static int CARTOFFSET = 7;
//#endif

	private void putSprite(int bank, int spr, int x, int y) {
		switch(bank) {
		case 0:
			if ( spr >= SPR_CART && spr <= SPR_TITLE) {
				gc.showImage(sprites[spr], 0, 0, sprites[spr].getWidth(), sprites[spr].getHeight(), x - (sprites[spr].getWidth() >> 1), y - (sprites[spr].getHeight() >> 1) );
			}
			break;
		}
	}
}

