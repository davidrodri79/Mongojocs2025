package com.mygdx.mongojocs.hotspeed;

public class Enemigo {

	// distancia a partir de la cual se putea acercandose a la x del prota
	static final int CTE_PROXIMIDAD_MINIMA	= 80;

	// tipos de enemigo
	public static final int ET_NORMAL	=	1;
	public static final int ET_ESPECIAL	=	2;
	public static final int ET_CAMION	=	3;
	public static final int ET_SALIDA	=	4;
	
	static final int FB_NORMAL		=	18;
	static final int FC_NORMAL		=	7-1;
	static final int FB_ESPECIAL	=	36;
	static final int FC_ESPECIAL	=	7-1;
	static final int FB_CAMION		=	0;
	static final int FC_CAMION		=	1-1;
	
	static /*synchronized*/ long timer = System.currentTimeMillis();
	
	
	
	
	GameCanvas m_gameCanvas;
	Game m_game;

	int x0, x, x2, y0, y, y2;
	int dx, olddx, dy;
	int frameBase, frame, frameCount;
	public int tipo;
	boolean d_i, ini_d_i; //false:izq; true:der
	boolean cambiado;
	boolean sacandoHumo, marcando;
	int humoCount, humoCountMax;
	int id;


/******************************************************************************
 *	CONSTRUCTORA
 ******************************************************************************/
	public Enemigo(Game g, GameCanvas gc, int _tipo, int _id) {
		
		/*
		if(_tipo != ET_SALIDA) {
			if(System.currentTimeMillis() - timer < 666) {
				m_game.destroyMe(_id,true);
				return;
			} 
			else timer = System.currentTimeMillis();
		}
		*/		
		
		/*
		if(colision2() && _tipo != ET_SALIDA) {
				System.out.println("Eliminado antes de salir");
				m_game.destroyMe(_id,true);
				return;
		}
		*/
		
		m_gameCanvas = gc;
		m_game = g;
		tipo = _tipo;
		id = _id;
		cambiado = false;
		sacandoHumo = false;
		marcando = false;
		humoCount = 0;
		humoCountMax = 2+m_game.RND(4);
		
		delayColision = 100+m_game.RND(200);
		
		int i;
		//System.out.println("Generado enemigo "+id);

		y0 = m_game.PROTA.realY()-13*10-20-m_game.PROTA.velocidad();

		y = -5*8;

		//for(i=0;i<y0;i+=8);
		//y0 = i;

		if(m_game.RND(100)>50) {
			x0 = 4; //2+m_game.RND(2);
			x = m_game.minX(0) + x0;
			
			ini_d_i = true;
		} else {
			x0 = 20; //10+m_game.RND(10);
			x = m_game.maxX(0) - x0;
			ini_d_i = false;
		}
		
		switch(tipo) {
			case ET_NORMAL:
				frameBase = FB_NORMAL;
				frameCount = FC_NORMAL;
				dy = m_game.RND(8)+8;
				//dy = 2;
				//x0 = x - m_game.minX(0);
				break;
			
			case ET_ESPECIAL:
				frameBase = FB_ESPECIAL;
				frameCount = FC_ESPECIAL;
				dy = m_game.RND(8)+8;
				//for(i=0;i<dy;i+=2);
				//dy = i;
				
				break;
			case ET_CAMION:
				frameBase = FB_CAMION;
				frameCount = 0;
				dy = m_game.RND(4)+4;
				break;
			case ET_SALIDA:
				if(m_game.RND(100)>50) frameBase = FB_NORMAL;
				else frameBase = FB_ESPECIAL;
				//System.out.println(frameBase);
				frameCount = 7-1;
				dy = 1;
				break;
		}
		
		frame = 0;
		
		dx = 0;		
		//x = m_gameCanvas.CanvasSizeX>>1;
		
	}

/******************************************************************************
 *	RESET - Reset...
 ******************************************************************************/
	public void reset() {
		dy = 0;
		x = m_gameCanvas.CanvasSizeX>>1;
		x2 = x;
		frameBase = 0;
		frame = 0;
		frameCount = 0;
	}

/******************************************************************************
 *	SPRITE - Sprite actual
 ******************************************************************************/
	
	public int sprite() { 
		// DEBUG
		//System.out.println("SPRITE PROTA: " + m_gameCanvas.spritesCor[(frameBase+frame)*6+4]);
		return /*m_gameCanvas.spritesCor[(*/frameBase+frame/*)*6+4]*/; 
	}

/******************************************************************************
 *	X/Y/VELOCIDAD
 ******************************************************************************/

	public int X() { return x; }
	public int Y() { return y; }
	public int velocidad() { return dy; }
	public void setXY(int _x, int _y) {
		x = _x;
		y = _y;	
	}

/******************************************************************************
 *	COLISION - Colision con el prota.
 ******************************************************************************/

	private boolean colision() { 
		int k;
		boolean u_d = y < m_game.PROTA.Y();
		d_i = m_game.PROTA.X() > x;
		
		if(tipo == ET_CAMION) if(u_d) k = 18; else k = 38;
		else k = 18;
		
		return(	Math.abs(m_game.PROTA.X()-x)<12 	&&
				Math.abs(m_game.PROTA.Y()-y)<k
				);
	}



////////////////////////////////////////////////////////////////////////////////

	long timerColision = System.currentTimeMillis();

	int delayColision;

	private boolean colision3() { 
		int j;
	
		/*
		if(System.currentTimeMillis()-timerColision < delayColision){
			//System.out.println("No miro colision2...");
			return false;
		}
		timerColision = System.currentTimeMillis();
	*/
		for(int i=0;i<m_game.NUM_ENEMIGOS;i++) {
			//otro activo diferente de este mismo
			if(m_game.ENEMIGOS[i]!=null && m_game.ENEMIGOS[i].id!=id) {
				//si es camion cambia la distancia de colision
				if(m_game.ENEMIGOS[i].tipo == ET_CAMION) j = 50; else j = 20;	
				
				if(Math.abs(m_game.ENEMIGOS[i].y-(y+Math.abs(dy-m_game.ENEMIGOS[i].velocidad()))) <= j
				   &&
				   Math.abs(m_game.ENEMIGOS[i].x-x) <= 18) {
					fxHumo();
					//System.out.println(id+" Cambio!");
					ini_d_i = !ini_d_i;
					if(ini_d_i)	x0 = 2; else x0 = 20;
					//dy = m_game.ENEMIGOS[i].velocidad()-1;
					return true;
					//break;
				}
			}
		}
		return false;
	}




/******************************************************************************
 *	COLISION2 - Posible colision con otros enemigos.
 ******************************************************************************/

	private boolean colision2() { 
		int j;
	
	//try{
	
		for(int i=0;i<m_game.NUM_ENEMIGOS;i++) {
			if(m_game.ENEMIGOS[i]!=null && m_game.ENEMIGOS[i].id!=id) {
				if(m_game.ENEMIGOS[i].tipo == ET_CAMION && tipo != ET_CAMION) 
					j = 50; else j = 30;
				/*
				if(m_game.ENEMIGOS[i].tipo == ET_CAMION && tipo == ET_CAMION) 
					j = 0; else j = 30;
				*/
				
				if(	Math.abs(m_game.ENEMIGOS[i].Y()-y)<j 	&&
					m_game.ENEMIGOS[i].Y()<y 				&&
					Math.abs(m_game.ENEMIGOS[i].X()-x)<18  	&&
					Math.abs(m_game.ENEMIGOS[i].Y()-y)>j-(dy+2)
					
					) {					
					
						fxHumo();
						
						//System.out.println(id+" Cambio!");
						ini_d_i = !ini_d_i;
						if(ini_d_i)	x0 = 4; else x0 = 20;
						dy = m_game.ENEMIGOS[i].velocidad()-1;
						return true;
						//break;
				}
			}
				
				
				
				
				
				
				//if(m_game.ENEMIGOS[i].tipo == ET_CAMION) j = 50; else j = 30;	
				//if((Math.abs(m_game.ENEMIGOS[i].Y()-y) < j) &&
				//	m_game.ENEMIGOS[i].Y() < y && 
				//	m_game.ENEMIGOS[i] != this && 
				   /*(Math.abs(m_game.ENEMIGOS[i].Y()-y) > j-(dy+2)) &&*/
				 //  (m_game.ENEMIGOS[i].Y() < y) && Math.abs(m_game.ENEMIGOS[i].X()-x) < 18 &&
				   //(m_game.ENEMIGOS[i].velocidad() <= dy )) {
					
				//	System.out.println(id+" Cambio!");
				//	ini_d_i = !ini_d_i;
				//	if(ini_d_i)	x0 = 2; else x0 = 20;
				//	dy = m_game.ENEMIGOS[i].velocidad()-1;
				//	break;
				//}	
			//}
		}
		return false;
	
	//}catch(Exception e) {System.out.println("Kagada enb colision2()");}
	
	}


	private void fxHumo() {
		if(!sacandoHumo /*&& m_game.RND(1000)>750*/) sacandoHumo = true;
	}


	
/******************************************************************************
 *	RUN - Gestion de la entrada y movimiento
 ******************************************************************************/

	public void RUN() {
	
		
		if(m_game.GameStatus == Game.EJ_FASE_COMPLETADA) dy += 2;
		else y += m_game.PROTA.velocidad();
		y -= dy;
		if(y > 22*8 || y < -6*8) { //-4*8
			//System.out.println("Destruyendo enemigo "+id);
			m_game.destroyMe(id,true);
			return;
		}
		if(tipo != ET_SALIDA && !sacandoHumo && m_game.RND(1000)>900) sacandoHumo = true;

		
		if(sacandoHumo) {
			if(humoCount++ == humoCountMax) { 
				sacandoHumo = false;
				humoCount = 0;
				humoCountMax = 2+m_game.RND(4);
			} else {
				m_game.generaHumo(x,y/*-(dy-1)*/);
			}
		}	

		switch(tipo) {
			
			case ET_NORMAL:
				if(colision()) {
					m_game.PROTA.colision(Prota.COL_LATERAL, d_i,false);
					y -= 10;
				}
				//colision2();
				if(!colision2()) {
				if(ini_d_i) { 
					if(Math.abs((x-m_game.minX(y-dy))-x0) > 2) {
						if(x - m_game.minX(y-dy) > x0) x-=2; else x+=2;
					}
				} else {
					if(Math.abs((m_game.maxX(y-dy)-x)-x0) > 2) {
						if(m_game.maxX(y-dy) - x  > x0) x+=2; else x-=2;
					}
				}
				}
				break;
			
			case ET_ESPECIAL:
				
				if(Math.abs(y - m_game.PROTA.Y()) < CTE_PROXIMIDAD_MINIMA && !cambiado) {
					ini_d_i = !ini_d_i;
					if(ini_d_i)	x0 = 2; else x0 = 20;
					cambiado = true;
				} else {
					
					if(!colision2()){
				
					if(ini_d_i) { 
						if(Math.abs((x-m_game.minX(y-dy))-x0) > 2) {
							if(x - m_game.minX(y-dy) > x0) x-=2; else x+=2;
						}
					} else {
						if(Math.abs((m_game.maxX(y-dy)-x)-x0) > 2) {
							if(m_game.maxX(y-dy) - x  > x0) x+=2; else x-=2;
						}
					}
					
					}
				}				
				
				if(colision()) {
					m_game.PROTA.colision(Prota.COL_LATERAL, d_i,false);
					y -= 10;
				}
				break;
			
			case ET_CAMION:
				
				if(!colision2()) {
				if(ini_d_i) { 
					if(Math.abs((x-m_game.minX(y-dy))-x0) > 2) {
						if(x - m_game.minX(y-dy) > x0) x-=2; else x+=2;
					}
				} else {
					if(Math.abs((m_game.maxX(y-dy)-x)-x0) > 2) {
						if(m_game.maxX(y-dy) - x  > x0) x+=2; else x-=2;
					}
				}
				}
				if(colision()) {
					m_game.PROTA.colision(Prota.COL_FRONTAL, true,false);
					y -= 10;
				}
				break;
			
			case ET_SALIDA:
				//System.out.println("hola?");
				if(dy++ > 16) dy = 16;
				y -= dy;
				if(y </*= m_game.scroll.ScrollY*/0) {
					System.out.println(y);
					m_game.destroyTraficoSalida(id);
					return;
				}
				break;
			
		}
		
		
		//if(tipo != ET_SALIDA && m_game.RND(1000)>900 && !marcando) {
		//	m_game.generaMarca(x,y,id);
		//	marcando = true;
		//}
		
		
		// BUG - en tsm30 los coches no tienen anim.
		//frame += Math.min(dy>>1,5);
		if(frame >= frameCount) frame = 0;
	}
	
} //CLASS Enemigo

