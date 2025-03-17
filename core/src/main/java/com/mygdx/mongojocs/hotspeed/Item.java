package com.mygdx.mongojocs.hotspeed;

public class Item {

	// tipos de item
	public static final int IT_BARRIL	=	1;
	public static final int IT_PIEDRA	=	2;
	public static final int IT_ACEITE	=	3;
	public static final int IT_CHARCO	=	4;
	public static final int IT_HUMO		= 	5;
	public static final int IT_MARCA	=	6;
	
	static final int FB_BARRIL		=	75;
	static final int FB_PIEDRA		=	72;
	static final int FB_ACEITE		=	73;
	static final int FB_CHARCO		=	74;
	static final int FB_HUMO		=	63;
	
	static final int FC_HUMO		= 5-1;
	

	GameCanvas m_gameCanvas;
	Game m_game;

	int x,x2,y;
	int frameBase, frame, frameCount;
	public int tipo;
	boolean d_i; //false:izq;true:der
	int id, id2; //id2: "padre" de la IT_MARCA


/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Item(Game g, GameCanvas gc, int _tipo, int _id) {
		m_gameCanvas = gc;
		m_game = g;
		tipo = _tipo;
		id = _id;

		y = -g.PROTA.velocidad()-36; //16
		//x = m_game.minX(0)+m_game.RND(m_game.maxX(0)-m_game.minX(0));
		x = x2 = (m_game.minX(0)+4)+m_game.RND(m_game.maxX(0)-m_game.minX(0)-8);

		
		frame = 0;

		switch(tipo) {
			case IT_BARRIL:
				frameBase = FB_BARRIL;
				frameCount = 0;
				
				//x = m_gameCanvas.CanvasSizeX>>1;
				break;
			
			case IT_PIEDRA:
				frameBase = FB_PIEDRA;
				frameCount = 0;
				//System.out.println("Piedra...");
				//x = m_gameCanvas.CanvasSizeX>>2;
				break;

			case IT_ACEITE:
				frameBase = FB_ACEITE;
				frameCount = 0;
				
				//x = m_gameCanvas.CanvasSizeX>>3;
				break;

			case IT_CHARCO:
				frameBase = FB_CHARCO;
				frameCount = 0;
				
				//x = m_gameCanvas.CanvasSizeX>>3;
				break;
				
			case IT_HUMO:
				frameBase = FB_HUMO;
				frameCount = FC_HUMO;
				frame = 0;//m_game.RND(2);
					
			case IT_MARCA:
				frameCount = 5+m_game.RND(5); //longitud marca
				break;

		}
		

		//frameCount = 0;
	}

/******************************************************************************
 *	RESET - Reset...
 ******************************************************************************/
	public void reset() {
		x = m_gameCanvas.CanvasSizeX>>1;
		frameBase = 0;
		frame = 0;
		frameCount = 0;
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int sprite() { return frameBase+frame; }


/******************************************************************************
 *	X/Y/
 ******************************************************************************/

	public int X() { return x; }
	public int Y() { return y; }


/******************************************************************************
 *	COLISION - Colision con el prota.
 ******************************************************************************/
	private boolean colision() { 
		d_i = m_game.PROTA.X() > x;
		return(	Math.abs(m_game.PROTA.X()-x)<10 	&&
				Math.abs(m_game.PROTA.Y()-y)<20
				);
	}

	private boolean colision2() { 
		d_i = m_game.PROTA.X() > x;
		return(Math.abs(m_game.PROTA.X()-(x+5))<10&&Math.abs(m_game.PROTA.Y()-(y+5))<10);
	}
	
/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {
				
		if(m_game.GameStatus != Game.EJ_FASE_COMPLETADA && tipo != IT_BARRIL)
			y += m_game.PROTA.velocidad();
		
		if(y >= 24*8) { 
			m_game.destroyMe(id, false);
			return;	
		}
		
		switch(tipo) {
			
			case IT_BARRIL:
				if(colision()) {
					m_game.PROTA.addCombustible(50);
					m_gameCanvas.bImpTextoFuel = true;
					m_game.destroyMe(id, false);
				} else {
					
					y += m_game.PROTA.velocidad()/4;
					x2 += m_game.RND(3)-1;
					if(m_game.esPisable(x2,y)) x = x2;
				}
				break;
				
			case IT_PIEDRA:
				//System.out.println("Y efectivamente soy piedra...");
				if(colision()) {
					m_game.PROTA.colision(Prota.COL_FRONTAL,true,true);
					//m_game.destroyMe(id, false);
				}
				break;
			case IT_ACEITE:
				if(colision()) {
					m_game.PROTA.colision(Prota.COL_LATERAL,d_i,false);	
					 //m_game.destroyMe(id, false);
				}
				break;
			case IT_CHARCO:
				if(colision()) {
					//m_game.PROTA.dy = Prota.CTE_VELOCIDAD_MINIMA;
					m_game.PROTA.frenada = true;
				}
				break;
			case IT_HUMO:
				if(frame++ == FC_HUMO) m_game.destroyMe(id,false);
				break;
			
			case IT_MARCA:
				if(frame++ == frameCount) m_game.destroyMe(id,false);
				break;

		}
	}
	
} //CLASS Item