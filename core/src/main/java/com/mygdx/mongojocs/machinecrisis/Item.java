package com.mygdx.mongojocs.machinecrisis;

public class Item {

	
	static final int TIME_PARPADEO	= 800;
	static final int TIME_PARADO	= 800;
		
	static final int CTE_GRAVEDAD	= 2;	
	static final int CTE_POS_INI_X1 = 12;
	static final int CTE_POS_INI_X2 = 36;
	static final int CTE_POS_INI_Y 	= 72;	 


	
	static final int IT_BOMBA		= 1;
	static final int IT_SPEED_UP	= 2;
	static final int IT_SPEED_DOWN	= 3;
	static final int IT_STOP		= 4;
	static final int IT_IMAN		= 5;
	static final int IT_PARALIZA	= 6;
	//static final int IT_ =	;
	

	static final int FB_BOMBA		= 20;
	static final int FB_SPEED_UP	= 27;
	static final int FB_SPEED_DOWN	= 26;
	static final int FB_STOP		= 29;
	static final int FB_PARALIZA	= 28;
	static final int FB_IMAN		= 25;


		
	GameCanvas m_gameCanvas;
	Game m_game;

	int x,y,x2,y2,dx,dx2,dy,id;
	int tipo;

	int frame, frameBase, frameCount;
	long timer;
	
	boolean quietoparao, parpadeo, explotando, tocandoProta;

/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Item(Game g, GameCanvas gc, int id, int tipo) {
		m_gameCanvas = gc;
		m_game = g;
		this.tipo = tipo;
		this.id = id;
		quietoparao = parpadeo = explotando = tocandoProta = false;

		if(m_game.RND(100)<50) x = CTE_POS_INI_X1; else x = CTE_POS_INI_X2;
		y = m_gameCanvas.canvasHeight - CTE_POS_INI_Y;
		
		frame = 0;
		
		switch(tipo) {
			case IT_BOMBA:
				frameBase = FB_BOMBA;
				break;
			case IT_STOP:
				frameBase = FB_STOP;
				break;
			case IT_SPEED_UP:
				frameBase = FB_SPEED_UP;
				break;
			case IT_SPEED_DOWN:
				frameBase = FB_SPEED_DOWN;
				break;
			case IT_PARALIZA:
				frameBase = FB_PARALIZA;
				break;
			case IT_IMAN:
				frameBase = FB_IMAN;
				break;
				
			default:
				System.out.println("Tipo de ITEM desconocido!!!");
				break;
		}
		frame = frameBase;
		
		
		x2 = x;
		y2 = y;

		dx = 2+m_game.RND(1);
		dx2 = dx;
		dy = -5-m_game.RND(5);
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int addX() { 
		try { return m_gameCanvas.spritesCor[frame*6]; } 
		catch (Exception e) { return 0;	}
	}
	public int addY() { 
		try { return m_gameCanvas.spritesCor[frame*6+1]; }
		catch (Exception e) { return 0;	}
	}
	public int sizeX() { 
		try { return m_gameCanvas.spritesCor[frame*6+2]; }
		catch (Exception e) { return 0;	}
	}
	public int sizeY() { 
		try { return m_gameCanvas.spritesCor[frame*6+3]; }
		catch (Exception e) { return 0;	}
	}		
	
/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {

		//if(m_game.GameTicks%2 == 0) frame++;
		//if(frame == frameBase+frameCount) frame = frameBase;
		
		if(tipo == IT_BOMBA && !explotando) frame++;
		if(frame == frameBase+2) frame = frameBase;
		
		if(explotando) {
			if(timer == 0) timer = System.currentTimeMillis();
			if(System.currentTimeMillis()-timer > m_game.PARAM_TIME_EXPLOSION) 
				m_game.destroyItem(id);
			if(tocandoProta && m_game.gameStatus!=105) {
				m_game.generarPieza();
				tocandoProta = false;
			}
			return;
		}
		
		if(quietoparao) {
			if(timer == 0) timer = System.currentTimeMillis();
			if(System.currentTimeMillis()-timer > TIME_PARADO) {
				quietoparao = false;
				timer = 0;
				if(tipo != IT_BOMBA) parpadeo = true;
				else {
					explotando = true; 
					timer = 0;
					frame = frameBase; 
					m_gameCanvas.resetExplosion();
					if(m_game.gameVibra == 1) m_gameCanvas.VibraSET(m_game.PARAM_TIME_EXPLOSION);
				}
			}
			return;
		}
		
		if(parpadeo) {
			if(timer == 0) timer = System.currentTimeMillis();
			if(System.currentTimeMillis()-timer > TIME_PARPADEO) {
				if(tipo != IT_BOMBA) m_game.destroyItem(id);
				else { 
					explotando = true; 
					parpadeo = false; 
					timer = 0; 
					frame = frameBase;
					if(m_game.gameVibra == 1) m_gameCanvas.VibraSET(m_game.PARAM_TIME_EXPLOSION);
				}
			}
			return;
		}

		x2 += dx;
		x = x2;
		if(m_game.GameTicks%CTE_GRAVEDAD==0) dy += 1;
		y2 += dy;		
		
		if(y2 >= m_gameCanvas.canvasHeight-sizeY()) { 
			y = y2 = m_gameCanvas.canvasHeight-/*sizeY()*/10;
			quietoparao = true;
		} else {
			y = y2;
		}
	}
		
} //CLASS Item

