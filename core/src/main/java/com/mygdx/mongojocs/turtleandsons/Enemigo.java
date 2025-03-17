package com.mygdx.mongojocs.turtleandsons;

public class Enemigo {
	

	static final int 	ET_TORTUGA	= 	0;
	static final int 	ET_CANGREJO	=	1;


	static final int	POS_0_X		= 55;
	static final int	POS_0_Y		= 16;
	
	static final int	POS_1_X		= 7;
	static final int	POS_1_Y		= 47; 
	
	static final int	POS_2_X		= 92;
	static final int	POS_2_Y		= 38;
	
	static final int	POS_3_X		= 55;
	static final int	POS_3_Y		= 82;


	static final int	FB_CANGREJO = 1;
	static final int	FC_CANGREJO = 5-1;
	static final int	FB_CANGREJO_2 = 6;
	static final int	FC_CANGREJO_2 = 3-1;


	static final int	FB_TORTUGA	= 9;
	static final int 	FC_TORTUGA	= 4-1;

	static final int	FB_TORTUGA_2_I	= 15;
	static final int	FB_TORTUGA_2_D	= 19;
	static final int 	FC_TORTUGA_2_ID	= 2-1;


	static final int	FB_TORTUGA_I	= 13;
	static final int	FB_TORTUGA_D	= 17;
	static final int 	FC_TORTUGA_ID	= 2-1;


	GameCanvas m_gameCanvas;
	Game m_game;

	int x, y;
	int frame, frameBase, frameCount, id, tipo, pos;
	boolean moviendose, largandose, esperando, ostiado, ostiado2;
	long timerEspera;

/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Enemigo(Game g, GameCanvas gc, int tipo, int id, int pos) {
		m_gameCanvas = gc;
		m_game = g;
		moviendose = true;
		largandose = false;
		esperando = false;
		ostiado = false;
		ostiado2 = false;
		this.tipo = tipo;
		this.id = id;
		this.pos = pos;
		switch(pos) {
			case 0:	x = POS_0_X; y = POS_0_Y; break;
			case 1:	x = POS_1_X; y = POS_1_Y; break;
			case 2:	x = POS_2_X; y = POS_2_Y; break;
			case 3:	x = POS_3_X; y = POS_3_Y; break;
		}
		if(tipo == ET_TORTUGA) {
			frameCount = FC_TORTUGA;
			frameBase = FB_TORTUGA;
			x += 2;
			y -= 2;
		} else {
			frameCount = FC_CANGREJO;
			frameBase = FB_CANGREJO;
		}
		
		timerEspera = System.currentTimeMillis();
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int sprite() { 
		return frameBase+frame;
	}



	public void ostia(){
		if(tipo == ET_CANGREJO || !largandose) ostiado = true;
	}


/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {

		if(ostiado) {
			if(!ostiado2) {
				timerEspera = System.currentTimeMillis();
				ostiado2 = true;
				frame = 0;
				if(tipo == ET_CANGREJO) {
					m_game.PUNTUACION += 4+m_game.NIVEL;
					m_game.ponPuntos(x+4,y,4+m_game.NIVEL);
					
					frameCount = FC_CANGREJO_2;
					frameBase = FB_CANGREJO_2;
				} else {
					m_gameCanvas.VibraSET(500);
					m_game.PUNTUACION -= 4+m_game.NIVEL;
					m_game.ponPuntos(x+4,y,-(4+m_game.NIVEL));
					
					frameCount = FC_TORTUGA_2_ID;
					if(pos == 1 || pos==3) frameBase = FB_TORTUGA_2_I;
					else frameBase = FB_TORTUGA_2_D;
				}
			} else {
				if(++frame == frameCount) frame = 0;
				if(System.currentTimeMillis() - timerEspera > m_game.PARAM_DELAY_ESPERA)
					m_game.destroyMe(id);
			}
			return;
		}

		if(largandose) {
			if(++frame == frameCount) frame = 0;
			switch(pos) {
				case 0:	x += 4; break;
				case 1:	x -= 4; break;
				case 2:	x += 4; break;
				case 3:	x -= 4; break;
			}
			if(x < -17-5 || x > -17+162+5) {
				m_game.destroyMe(id);
				m_game.PUNTUACION++;
				if(x<-17-5) m_game.ponPuntos(-17+10,y,1);
				else m_game.ponPuntos(-17+162-10,y,1);

			}
		
		} else {
		
			if(!moviendose) {
				if(--frame == 0) {
					if(tipo == ET_CANGREJO) {
						m_game.PUNTUACION-=4+m_game.NIVEL;
						m_game.ponPuntos(x+4,y,-(4+m_game.NIVEL));
						m_game.destroyMe(id);
					} else {
						largandose = true;
						frame = 0;
						frameCount = FC_TORTUGA_ID;
						if(pos==1  || pos==3) frameBase = FB_TORTUGA_I;
						else frameBase = FB_TORTUGA_D;
					}
				}
			} else {
				if(!esperando) {
					if(++frame == frameCount) {
						esperando = true;
						timerEspera = System.currentTimeMillis();
					}
				} else {
					if(System.currentTimeMillis()-timerEspera > m_game.PARAM_DELAY_ESPERA)
						moviendose = false;
				}
			}

		}
	}
	
} //CLASS Prota

