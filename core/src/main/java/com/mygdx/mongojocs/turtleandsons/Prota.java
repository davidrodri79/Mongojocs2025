package com.mygdx.mongojocs.turtleandsons;

public class Prota {

	static final int	FB_UP		= 30;
	static final int	FB_DOWN		= 21;
	static final int	FB_LEFT		= 24;
	static final int	FB_RIGHT	= 27;

	static final int	FC_ALL		= 3;

	static final int 	X = 34;
	static final int 	Y = 36;

	GameCanvas m_gameCanvas;
	Game m_game;

	int frame, frameBase, frameCount;
	int x, y;
	int posOstia;
	boolean moviendose;

/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Prota(Game g, GameCanvas gc) {
		m_gameCanvas = gc;
		m_game = g;
		moviendose = false;
	}

/******************************************************************************
 *	RESET - Reset...
 ******************************************************************************/
	public void reset() {
		frameBase = FB_DOWN;
		frame = 0;
		frameCount = FC_ALL;
		x = X;
		y = Y;
		posOstia = -1;
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int sprite() { 
		return frameBase+frame;
	}


/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {
		
		if(!moviendose) {
			moviendose = true;
			if(--frame < 0) { frame = 0; x = X; y = Y; }
			
			if(x==X && y==Y) {
				if(m_gameCanvas.keyY<0 || m_gameCanvas.keyMisc==2) {
					frameBase = FB_UP;
					posOstia = 0;
					y = Y-16;
				} else if(m_gameCanvas.keyX<0 || m_gameCanvas.keyMisc==4) {
					frameBase = FB_LEFT;
					x = X-20;
					y = Y-8;
					posOstia = 1;
					
				} else if(m_gameCanvas.keyX>0 || m_gameCanvas.keyMisc==6) {
					frameBase = FB_RIGHT;
					x = X+8;
					y = Y-13;
					posOstia = 2;
				} else if(m_gameCanvas.keyY>0 || m_gameCanvas.keyMisc==8) {
					frameBase = FB_DOWN;
					y = Y+8;
					posOstia = 3;
				} 
				else moviendose = false;
			} 
			else moviendose = false;
		} else {
			if(++frame == 2) {
				m_game.mazazoEnPos(posOstia);
				moviendose = false;
			}
		}

	}
	
} //CLASS Prota

