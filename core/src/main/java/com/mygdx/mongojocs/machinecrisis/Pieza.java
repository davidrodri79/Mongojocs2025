package com.mygdx.mongojocs.machinecrisis;

public class Pieza {

	
	static final int TIME_PARPADEO	= 500;
	static final int TIME_SALIDA	= 100;
	
	/*atic final*/ public int CTE_GRAVEDAD	= 2;	
	static final int CTE_POS_INI_X1 = 10;
	static final int CTE_POS_INI_Y1	= 52+50;	 
	static final int CTE_POS_INI_X2 = 35;
	static final int CTE_POS_INI_Y2	= 56+50;
	
	static final int FC_ALL 		= 4;
	
	static final int FB_TARJETA		= 0;
	static final int FB_CHIP		= 4;
	static final int FB_BOMBILLA	= 8;
	static final int FB_CONDENSADOR	= 12;
	static final int FB_TORNILLO	= 16;
//	static final int FB_BOMBA		= 20;
//	static final int FB_EXPLOSION	= 22;
	

	
	
	GameCanvas m_gameCanvas;
	Game m_game;

	int x,y,x2,y2,dx,dx2,dy,id;//,sizeX,sizeY,addX,addY;
	int tipo;

	int frame, frameBase, frameCount;
	long timerParpadeo, timerSalida;
	
	boolean parpadeo, rebote, salida, magnetizada, magnetizada2, paralizada;

/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Pieza(Game g, GameCanvas gc, int id) {
		m_gameCanvas = gc;
		m_game = g;
		this.tipo = tipo;
		this.id = id;
		salida = parpadeo = magnetizada = magnetizada2 = paralizada = false;

		if(m_game.RND(100)<50) {
			x = CTE_POS_INI_X1; 
			y = /*m_gameCanvas.canvasHeight - */CTE_POS_INI_Y1;
		} else {
			x = CTE_POS_INI_X2;
			y = /*m_gameCanvas.canvasHeight - */CTE_POS_INI_Y2;
		}
		
		frameBase = m_game.RND(5)*4;
		//System.out.println(frameBase);
		frameCount = FC_ALL;
		frame = frameBase;
		
		
		x2 = x;
		y2 = y;
 
		dx = 2+m_game.RND(2);
		dx2 = dx;
		//dy = -5-m_game.RND(5);
		dy = -5-m_game.RND(6);
		/*
		sizeX = m_gameCanvas.spritesCor[frameBase*6+2];
		sizeY = m_gameCanvas.spritesCor[frameBase*6+3];
		addX = m_gameCanvas.spritesCor[frameBase*6];
		addY = m_gameCanvas.spritesCor[frameBase*6+1];
		*/
//		System.out.println("Sprite "+frameBase+" tiene de ancho "+sizeX+" y de alto "+sizeY+".");
//		System.out.println("ADDX: "+addX+" ADDY: "+addY);
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int addX() { return m_gameCanvas.spritesCor[frame*6]; }
	public int addY() { return m_gameCanvas.spritesCor[frame*6+1]; }
	public int sizeX() { return m_gameCanvas.spritesCor[frame*6+2]; }
	public int sizeY() { return m_gameCanvas.spritesCor[frame*6+3]; }		
	
/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {

		if(m_game.GameTicks%2 == 0) frame++;
		if(frame == frameBase+frameCount) frame = frameBase;
		//System.out.println(frame);
		
		if(paralizada && !magnetizada2) return;
		
		if(magnetizada) {
		//	if(m_game.PROTA.x == 213) {
		//		dx = 4;
		//		dy = -10;
		//		magnetizada = false;
		//		System.out.println("La pieza "+id+" se desmagnetiza.");
		//		x2 = m_game.PROTA.x+x2;
		//		y = m_game.PROTA.y-(sizeY()+addY());
		//		rebote = true;
		//		
		//	} else {
		//		x = m_game.PROTA.x+x2;
		//		y = m_game.PROTA.y-(sizeY()+addY());
		//		
		//	}
		//	if(!m_game.PROTA.quietoparao) {
		//		/*if(m_game.keyX < 0) x2 += 6+m_game.PROTA.modifVel;
		//		else x2 -= 6+m_game.PROTA.modifVel;*/
		//	}
			return;
		}
		
		
		
		if(salida) {
			if(timerSalida == 0) timerSalida = System.currentTimeMillis();
			if(System.currentTimeMillis()-timerSalida > TIME_SALIDA) {
				m_game.destroyPieza(id);
				//m_game.ponPuntos(/*m_gameCanvas.canvasWidth-4*/x,y,-1);
				if(m_game.PARAM_PIEZAS_GOAL>0 && !m_game.siguienteNivel) 
					m_game.PARAM_PIEZAS_GOAL--;
				
				m_gameCanvas.bImpHumo = true;
				m_gameCanvas.bActualizaMarcador = true;
				
				//m_gameCanvas.bImpMano = true;
				m_gameCanvas.putManoOK();
			}
			return;
		}
		
		if(parpadeo) {
			if(timerParpadeo == 0) timerParpadeo = System.currentTimeMillis();
			if(System.currentTimeMillis()-timerParpadeo > TIME_PARPADEO) {
				m_game.destroyPieza(id);
				//m_game.ponPuntos(x,y,1);
				// DEBUG invulnerable...
				m_game.PARAM_PIEZAS_GOAL += m_game.PARAM_PENALIZACION;
				if(!m_game.siguienteNivel) {	
				 	m_game.PIEZAS_CAIDAS++;
					m_gameCanvas.bActualizaMarcador = true;
					m_gameCanvas.putManoNOK();
				}
			}
			return;
		}
		
		
		/*if(m_game.GameTicks%CTE_GRAVEDAD==0)*/ x2 += dx;
		/*
		if(x2 < 10/2) { 
			x2 = 10/2; 
			dx = -dx; 
		}
		*/
		//if(x2 > 2*m_gameCanvas.CanvasSizeX-24) x2 = 2*m_gameCanvas.CanvasSizeX-24;

//		if(x2 > 2*m_gameCanvas.canvasWidth+10) { 
		if((x2 > 2*m_gameCanvas.canvasWidth-20
		   &&
		   y > m_gameCanvas.canvasHeight-20) || x2 > 2*m_gameCanvas.canvasWidth) { 
			/*m_game.destroyMe(id);
			m_game.PARAM_PIEZAS_GOAL--;*/
			
			
			salida = true;
			return;
		}
		
		x = x2;
		
		if(m_game.GameTicks%CTE_GRAVEDAD==0) dy += 1;

		y2 += dy;		
		
		if(y2 >= m_gameCanvas.canvasHeight-sizeY()) { 
			y2 = m_gameCanvas.canvasHeight-sizeY();
			parpadeo = true;
		} else {
			y = y2;
		}
	}
		
} //CLASS Pieza

