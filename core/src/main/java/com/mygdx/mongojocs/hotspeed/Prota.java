package com.mygdx.mongojocs.hotspeed;

public class Prota {

	static final int CTE_Y							=	180 - (15*3);
	static final int CTE_PERDIDA_FUEL_POR_ACCIDENTE = 	20;
	static final int CTE_FUEL_INICIAL 				= 	500;
	static final int CTE_VELOCIDAD_MAXIMA			= 	18;
	static final int CTE_VELOCIDAD_MINIMA			= 	2;
		
	static final int FB_NORMAL 				= 0;
	static final int FC_NORMAL				= 7-1;
	
	static final int FB_COL_LATERAL_INI_I 	= 10;
	static final int FB_COL_LATERAL_INI_D 	= 16;
	
	static final int FB_COL_LATERAL 	= 9;
	static final int FC_COL_LATERAL 	= 8-1;

	static final int FB_COL_FRONTAL 	= 54;
	static final int FC_COL_FRONTAL 	= 15-1;

	public static final int COL_FRONTAL	= 1;
	public static final int COL_LATERAL	= 2;
	public static final int COL_LATERAL_INI_D	= 3;
	public static final int COL_LATERAL_INI_I	= 4;
	
	GameCanvas m_gameCanvas;
	Game m_game;

	int x, x2;
	int y, y2;
	int dy, dy2;
	int fuel;
	int frameBase, frame, frameCount, frameLast;
	boolean colisionando, explotando, invulnerable, frenada, frenada2;
	long timerInvulnerable = System.currentTimeMillis();
	boolean col_d_i; //false:izq;true:der
	int tipoColision;
	int contDerrape;
	long timerFrenada;
	
/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Prota(Game g, GameCanvas gc) {
		m_gameCanvas = gc;
		m_game = g;
	}

/******************************************************************************
 *	RESET - Reset...
 ******************************************************************************/
	public void reset() {
		dy = 0;
		fuel = CTE_FUEL_INICIAL;
		x2 = x = 5*8+10;
		y2 = y = ((8*172)-3)*8;

		frameBase = FB_NORMAL;
		frame = 0;
		frameCount = FC_NORMAL;
		colisionando = false;
		explotando = false;
		invulnerable = false;
		frenada = frenada2 = false;
		tipoColision = 0;
		contDerrape = 0;
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int sprite() { return frameBase+frame; }
	public int spriteLast() { return frameLast;} //para explosion...
	
/******************************************************************************
 *	X/Y/VELOCIDAD/COMBUSTIBLE
 ******************************************************************************/

	public int X() { return x; }
	public int Y() { return CTE_Y+(dy*1-4); }
	public int realY() { return y+(dy*1-4); }
	public int combustible() { return fuel; }
	public void addCombustible(int kk) {
		if((fuel+=kk) > CTE_FUEL_INICIAL) fuel = CTE_FUEL_INICIAL;
	}
	public int velocidad() { if(explotando) return 0; else return dy; }

	
/******************************************************************************
 *	SINCOMBUSTIBLE - Si se ha quedado sin...
 ******************************************************************************/
	public boolean sinCombustible() { return fuel==0; }

/******************************************************************************
 *	COLISION - Perdida de fuel, animacion...
 ******************************************************************************/

	public void colision(int tipo, boolean d_i, boolean force) {

		if(invulnerable) return;
		
		if((colisionando && !force) || explotando ||
			m_game.GameStatus == m_game.EJ_FASE_COMPLETADA) return;
		

		colisionando = true;

		col_d_i = d_i;
		dy = CTE_VELOCIDAD_MINIMA;
				
		switch(tipo) {
			case COL_FRONTAL:
				if(m_game.GAMESETTINGS[Game.GS_VIBRATION] == 1) 
					m_gameCanvas.VibraSET(800);
				if(m_game.GAMESETTINGS[Game.GS_SOUND] == 1) 
					m_gameCanvas.SoundSET(Game.SFX_COLISION,1);
			
				explotando = true;
				fuel -= 35;
				tipoColision = COL_FRONTAL;
				frameLast = sprite();
				frameBase = FB_COL_FRONTAL;
				frameCount = FC_COL_FRONTAL;
				frame = 0;
				dy = 0;

				break;
			case COL_LATERAL:
				if(m_game.GAMESETTINGS[Game.GS_VIBRATION] == 1) 
					m_gameCanvas.VibraSET(400);

				contDerrape = 0;

				if(col_d_i) {
					tipoColision 	= COL_LATERAL_INI_D;
					frameBase 		= FB_COL_LATERAL_INI_D;
				} else {
					tipoColision 	= COL_LATERAL_INI_I;
					frameBase 		= FB_COL_LATERAL_INI_I;
				}
				frame = 0;
				break;		
		}	
	}

	public void setInvulnerable() {
		invulnerable = true;
		timerInvulnerable = System.currentTimeMillis();
	}


/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {

		if(m_game.GameStatus == m_game.EJ_FASE_COMPLETADA) {
			y-=CTE_VELOCIDAD_MAXIMA;
			frame = 0;
			frameBase = FB_NORMAL;
			return;
		}

		if(!frenada2) {
			if(frenada) {
				timerFrenada = System.currentTimeMillis();
				frenada2 = true;
			}
		}

		if(invulnerable) 
			if((System.currentTimeMillis() - timerInvulnerable) > 1000)
				invulnerable = false;

		
		if(colisionando) {
			switch(tipoColision) {
			
				case COL_FRONTAL:
					if(frame++ >= frameCount) {
						colisionando = false;
						explotando = false;
						setInvulnerable();
						x2=m_game.minX(Y())+(m_game.maxX(Y())-m_game.minX(Y()))/2;
						x2-=10;

						frameBase = FB_NORMAL;
						frame = 0;
						frameCount = FC_NORMAL;
					}
					break;
					
				case COL_LATERAL_INI_D:
				case COL_LATERAL_INI_I:
					if(contDerrape++ == 4) {
						tipoColision = COL_LATERAL;
						frame = 0;
						frameBase = FB_COL_LATERAL; 
					} else {
						if(col_d_i) {
							if(m_game.KeybX1 > 0) { 
								colisionando = false;
								frameBase = FB_NORMAL;
								frame = 0;
								frameCount = FC_NORMAL;
							} else x2 += 4; 
						} else {
							if(m_game.KeybX1 < 0) {
								colisionando = false;
								frameBase = FB_NORMAL;
								frame = 0;
								frameCount = FC_NORMAL;
							} else x2 -= 4;
						}
					}
					break;
					
				case COL_LATERAL:
					if(frame++ >= frameCount) frame = 0;
					if(col_d_i) x2+=4; else x2-=4;
					break;
			}
		} else {
		
			if(m_game.GameStatus != Game.EJ_SIN_FUEL && !frenada) {
				if(m_gameCanvas.KeybX > 0 ) { x2 += 6; }
				if(m_gameCanvas.KeybX < 0 ) { x2 -= 6; }
				if(m_gameCanvas.KeybY > 0) { dy -= 2; }
				if(m_gameCanvas.KeybY < 0 || m_gameCanvas.KeybB != 0) { dy += 2; }
			}

			if(frenada) {
				if(System.currentTimeMillis() - timerFrenada > 500) 
					frenada = frenada2 = false;
				else
					dy -= 2;
			}	
						
			frame += Math.min(dy>>1,5);
			
			// DEBUG - no decrementar combustible	
			fuel -= 1;
			
			if(fuel < 0) fuel = 0;
			if(y < 16*8) y = Y();
			if(frame >= frameCount) frame = 0;


		}

		dy2++;
		dy -= dy2%2;
		if(dy < CTE_VELOCIDAD_MINIMA) dy = CTE_VELOCIDAD_MINIMA;
		if(dy > CTE_VELOCIDAD_MAXIMA) dy = CTE_VELOCIDAD_MAXIMA;		

		if(	!colisionando || 
			(colisionando && tipoColision==COL_LATERAL) ||
			(colisionando && tipoColision==COL_LATERAL_INI_D) ||
			(colisionando && tipoColision==COL_LATERAL_INI_I) ) 	y2 -= dy;

		if(y2 < 8) y2 = y = ((8*172)-3)*8;	
		if(m_game.esPisable(x2+10,Y())) { x = x2; y = y2; } 
		else { x2 = x; colision(COL_FRONTAL,true,true); }
	}
	
} //CLASS Prota

