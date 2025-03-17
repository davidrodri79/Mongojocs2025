package com.mygdx.mongojocs.machinecrisis;

public class Prota {

	static final int CTE_VELOCIDAD_DISPARO			= 	10;
	static final int CTE_VEL_SPEED_UP				=   +4;
	static final int CTE_VEL_SPEED_DOWN				=   -4;

	static final int CTE_MIN_X						= 	48;
	static final int CTE_SIZE_Y						= 	24+4;


	static final int TIME_EFECTO_ITEM				= 7000;

		
	GameCanvas m_gameCanvas;
	Game m_game;

	int x,x2,y,dy;
	int frame, frameBase;

	boolean saltando, direccion, quietoparao;
	long timer = 0;

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
		x = x2 = 162/2-12;
		y = m_gameCanvas.canvasHeight-CTE_SIZE_Y;
		frame = frameBase = 0;
		frame = 0;
		saltando = false;
		
		//variables sobre efectos de items
		timer = 0;
		modifVel = 0;
		bMagnetico = false;

		itemActivo = -1;
		m_gameCanvas.iTipoItem = -1;
		
		dy = -10; //para los saltos del final de nivel
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int sprite() { 
	// DEBUG
	//System.out.println("SPRITE PROTA: " + m_gameCanvas.spritesCor[(frameBase+frame)*6+4]);
 	return frameBase+frame; //m_gameCanvas.spritesCor[(frameBase+frame)*6+4]; 
	}

/******************************************************************************
 *	EFECTO DE ITEM - SPEED UP/DOWN, IMAN...
 ******************************************************************************/

/*	
 	static final int IT_BOMBA		= 1;
	static final int IT_SPEED_UP	= 2;
	static final int IT_SPEED_DOWN	= 3;
	static final int IT_STOP		= 4;
	static final int IT_IMAN		= 5;
*/


	int modifVel = 0;
	boolean bMagnetico = false;
		
	int itemActivo = -1;

	public void aplicarItem(int tipoItem) {
		
		if(itemActivo >= 0) pararItem();
		itemActivo = tipoItem;
		timer = /*System.currentTimeMillis()*/m_game.TIEMPO_FASE;
	
		switch(tipoItem) {
		
			case Item.IT_SPEED_UP:
				modifVel = CTE_VEL_SPEED_UP;
				break;
			
			case Item.IT_SPEED_DOWN:
				modifVel = CTE_VEL_SPEED_DOWN;
				break;
				
			case Item.IT_IMAN:
				bMagnetico = true;
				break;
		
			case Item.IT_STOP:
				m_game.maquinaParada = true;
				break;
			
			case Item.IT_PARALIZA:
				for(int i=0;i<m_game.MAX_NUM_PIEZAS;i++)
					if(m_game.PIEZAS[i] != null) 
						m_game.PIEZAS[i].paralizada = true;
				
				break;
		
		}
		
	}

	private void pararItem() {

		switch(itemActivo) {
		
			case Item.IT_SPEED_UP:
			case Item.IT_SPEED_DOWN:
				modifVel = 0;
				break;

			case Item.IT_IMAN:
				bMagnetico = false;
				break;
			
			case Item.IT_STOP:
				m_game.maquinaParada = false;
				break;
				
			case Item.IT_PARALIZA:
				for(int i=0;i<m_game.MAX_NUM_PIEZAS;i++)
					if(m_game.PIEZAS[i] != null) 
						m_game.PIEZAS[i].paralizada = false;
				
				break;
		}	
		
		itemActivo = -1;
		timer = 0;
		m_gameCanvas.iTipoItem = -1;
	}


	private void checkItem() {
		for(int i=0;i<m_game.MAX_NUM_ITEMS;i++) {
			if(m_game.ITEMS[i] != null) {
				int xP = m_game.ITEMS[i].x2+5+15;

				if( Math.abs(xP-(x+12)) <= 5+12
					&&
					m_game.ITEMS[i].y == m_gameCanvas.canvasHeight-/*sizeY()*/10
					)
				{
					if(m_game.ITEMS[i].tipo == Item.IT_BOMBA) {
						if(Math.abs(xP-(x+12)) <= 5+12+m_game.PARAM_RADIO_EXPLOSION/2) 
							m_game.ITEMS[i].tocandoProta = true;
					} else {
						aplicarItem(m_game.ITEMS[i].tipo);
						m_gameCanvas.iTipoItem = m_game.ITEMS[i].tipo;
						m_gameCanvas.bImpInfoItem = true;
						m_gameCanvas.cont2 = xP-30;
						m_gameCanvas.bActualizaMarcador = true;
						m_game.destroyItem(i);
					}								
					break;
				}
			}		
			
		}
	}

/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	
	public void fakeRUN() {
	
		//m_game.scrollX = 0;
		//x = m_gameCanvas.canvasWidth + m_gameCanvas.canvasWidth/2;
		saltando = false;
		y = m_gameCanvas.canvasHeight-CTE_SIZE_Y;
		x += x2;
		if(x < m_gameCanvas.canvasWidth/2+24 || x > m_gameCanvas.canvasWidth-24) x2 = -x2;

		frame++;	
		if(frame < 0) frame = 3;
		frame %= 4;
	}

	public void RUN() {


		if(m_game.gameStatus == 105) {
			frame = 0;
			x = x2;
			/*ga.PROTA.saltando && !ga.PROTA.quietoparao) {
		if(ga.PROTA.direccion*/
			saltando = true; quietoparao = false;
			if(m_game.RND(10) > 5) direccion = true; else direccion = false;
			
			
			dy++;
			y += dy;
			if(y >= m_gameCanvas.canvasHeight-CTE_SIZE_Y) {
				dy = -dy;
				y = m_gameCanvas.canvasHeight-CTE_SIZE_Y;
			}
			
			//System.out.println("Prota: "+x2+" "+y);
			return;
		}


		checkItem();

		if(timer > 0) if(m_game.TIEMPO_FASE - timer > TIME_EFECTO_ITEM) pararItem();
		
		quietoparao = false;
		direccion = false; //izq:false|der:true

		saltando = false;
		
		if(m_game.gameStatus != 105) {
			if(m_game.keyY < 0 && !saltando) {
				saltando = true;
			}
			
			if(m_game.keyX > 0) { x2 += 6+modifVel; frame++; direccion = true;}
			else if(m_game.keyX < 0) { x2 -= 6+modifVel; frame++;}
			else { frame = 0; quietoparao = true; }
		}		
		
		if(saltando && !quietoparao) {
			if(direccion) {
				int i,j;
				for(i=0,j=-1;i<m_game.MAX_NUM_PIEZAS;i++) {
					if(m_game.PIEZAS[i] != null) {
						if(m_game.PIEZAS[i].x+10 - x <= 12) {
							if(j>=0) if(m_game.PIEZAS[i].x-x < m_game.PIEZAS[j].x-x) j = i;
							else j = i;
						}
					}
				}
				if(j<0) x2 += 12+modifVel; else x2 = m_game.PIEZAS[j].x;  
			} else {
				int i,j;
				for(i=0,j=-1;i<m_game.MAX_NUM_PIEZAS;i++) {
					if(m_game.PIEZAS[i] != null) {
						if(x - m_game.PIEZAS[i].x-10 <= 12) {
							if(j>=0) if(x-m_game.PIEZAS[i].x < x-m_game.PIEZAS[j].x) j = i;
							else j = i;
						}
					}
				}
				
				if(j<0) x2 -= 12+modifVel; else x2 = m_game.PIEZAS[j].x; 
			}
//			saltando = false;	
		}
		
		
		if(frame < 0) frame = 3;
		frame %= 4;
		
		if(x2 < CTE_MIN_X) x2 = CTE_MIN_X; 
		if(x2 >= 2*m_gameCanvas.canvasWidth-(24+24)) x2 = 2*m_gameCanvas.canvasWidth-(24+24);
		x = x2;
	
		
		int xProta;

		if(x < 162/2-12 || x > 3*m_gameCanvas.canvasWidth/2-12 || x2==-1 || x2==1) 
			xProta = x%m_gameCanvas.canvasWidth;
		else 
			xProta = 162/2-12;

		
		int xPieza;
	

		boolean golpe = false;	
		for(int i=0;i<m_game.MAX_NUM_PIEZAS;i++) {
			if(m_game.PIEZAS[i] != null) {
 				xPieza = m_game.PIEZAS[i].x-m_game.scrollX+m_game.PIEZAS[i].addX();

				if( m_game.PIEZAS[i].y+m_game.PIEZAS[i].addY()+m_game.PIEZAS[i].sizeY() >= y
					&&
					m_game.PIEZAS[i].y+m_game.PIEZAS[i].addY()+m_game.PIEZAS[i].sizeY() <= y+12
					&&
					Math.abs((xPieza+5)-(xProta+12)) < 5+12+2
					&&
					!m_game.PIEZAS[i].parpadeo 
					&&
					!m_game.PIEZAS[i].magnetizada 
					&&
					!m_game.PIEZAS[i].rebote
				  	&&
				  	m_game.gameStatus != 105
				  ) {
					
						golpe = true;
					
						m_game.PIEZAS[i].dy *= -1; 
						m_game.PIEZAS[i].rebote = true;
						//m_game.PIEZAS[i].y = m_gameCanvas.CanvasSizeY-(25+11+4+1);
						m_game.PIEZAS[i].y = y-m_game.PIEZAS[i].sizeY()-m_game.PIEZAS[i].addY();
						if(!quietoparao || bMagnetico) {
							if(direccion) {
								if(saltando) m_game.PIEZAS[i].dx += 4; 
								else m_game.PIEZAS[i].dx += 2; 
							}
							else m_game.PIEZAS[i].dx = 0;
							
							if(bMagnetico && !m_game.PIEZAS[i].magnetizada) {
								m_game.PIEZAS[i].dx = 0;
								m_game.PIEZAS[i].dy = 0;
								m_game.PIEZAS[i].magnetizada = true;
								m_game.PIEZAS[i].magnetizada2 = true;
								m_game.PIEZAS[i].x2 = m_game.PIEZAS[i].x - x;
								
								//System.out.println("Pieza "+i+" magnetizada");
							}
						}
							 
				} else if(m_game.PIEZAS[i].rebote) m_game.PIEZAS[i].rebote = false;
			
				if(m_game.PIEZAS[i].magnetizada) {
					m_game.PIEZAS[i].x = x + m_game.PIEZAS[i].x2;
					m_game.PIEZAS[i].y = y-m_game.PIEZAS[i].sizeY()+m_game.PIEZAS[i].addY();
					if(x2 == 2*m_gameCanvas.canvasWidth-(24+24)) {
						m_game.PIEZAS[i].dx = 2;
						m_game.PIEZAS[i].x2 = m_game.PIEZAS[i].x;
						m_game.PIEZAS[i].dy = -5-m_game.RND(4);
						m_game.PIEZAS[i].magnetizada = false;
						m_game.PIEZAS[i].magnetizada2 = false;
						m_game.PIEZAS[i].paralizada = false;
						m_game.PIEZAS[i].rebote = true;
						//System.out.println("La pieza "+i+" se desmagnetiza.");
					}
				}
			}
		} //for
		if(golpe) m_gameCanvas.SoundSET(2,1);

	} //run	
} //CLASS Prota