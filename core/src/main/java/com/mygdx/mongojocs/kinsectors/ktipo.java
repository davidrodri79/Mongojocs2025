package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class ktipo extends unit
{
	int sx, sy ;
	final static public int MAXDISPAROS = 24;
	public static unit shots[];	
	int d_shot = 0;	 
	int ds_shot = 0;	 
	static int inmunidad = 0;
	
	int p_shot = 0;
	int seq_shot[]={0,-20,-40,-20,0,20,40,20};
	final int dx = 4;
	boolean autofire = true;
	int auto = 3;
	int antestag;
	
	//ITEMS
	final static int IESCUDO  	= 0;
	final static int IVIDA  	= 1;
	final static int IPOWERUP  = 2;	
	final static int IBOLA  	= 3;
	final static int IGUSPIRA  = 4;	
	final static int IWAVE 	   = 5;
	final static int ILASER	   = 6;
	final static int IMISIL  	= 7;
	final static int IBOMBA	   = 8;
	
	//DISPAROS
	final static int DNONE 	   = 0;
	final static int DGUSPIRA  = 1;
	final static int DGUSPIRA2 = 2;
	final static int DLASER    = 3;
	final static int DLASER2   = 4;
	final static int DWAVE     = 5;
	final static int DWAVE2    = 6;
	final static int DBOLA     = 7;
	final static int DBOLA2    = 8;
	
	final static int DMISIL    = -1;
	static int tipodisp = DLASER;
	static int subdisp = DNONE;
	int disptag;
	static int cadencia;
	static int vidas;
	final static int energy = 128;
	int sarma = 0;
	
	public ktipo(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,0);	 	   
	   Activate();
	   x = 10;
	   y = 58;
	   shots = new unit[MAXDISPAROS];
	   for (int jj = 0;jj < MAXDISPAROS;jj++)
	      shots[jj] = new unit(i, 8, 8, 8, 32, 40, 0);
	   tor = new unit(i, 21, 24, 0, 0, 0, 0);   
	   tag = energy;
	   setCollisionRectangle(0,3,w,7);
	}

	boolean reviviendo = false;
	public void Revive()
	{		
	 	if (!Game.chani) vidas--;
		Game.addMsg(Lang.livelost);
	   tag = energy;
	   reviviendo = true;
	   inmunidad = 100;
	   x = -16;
	   y = 64;	  
	   //KInsectors.cfase = Game.fase;
	   if (subdisp != DNONE) subdisp = DNONE;
	   else if (tipodisp%2 == 0) tipodisp--;
	}
	int db = 0;
	int turri = 0;
	
	unit tor;
	
	public void Update(int xdir, int ydir, boolean firing)
   {
   	timing++;
   	if (inmunidad > 0) inmunidad--;
   	if (firing && Game.turrican) Upgrade((++turri%9));   	
   	if (firing && sarma >= 256) {tor.Activate();tor.setPosition(x,y);sarma = 0;}
   	if (tor.active) 
   	{
   		tor.Update();
   		
   		tor.x = (x+tor.x+tor.x+tor.x)/4 + (Trig.cos(tor.timing<<4)/48)+2;
   		tor.y = (y+tor.y+tor.y+tor.y)/4 + (Trig.sin(tor.timing<<4)/48)-2;
   		for(int i = 0;i < Game.MAXBOLAS;i++)   		
   				if (tor.isCollidingWith(Game.bolas[i])) Game.bolas[i].Deactivate();   
   				else if (Game.bolas[i].isCollidingWith(tor)) Game.bolas[i].Deactivate();   			
   		for(int i = 0;i < Game.MAXMALOS;i++)
   		{
   			if (Game.malos[i].active && Game.malos[i].tag > 0) 
		   	{
   				if (tor.isCollidingWith(Game.malos[i]) ||
   				    Game.malos[i].isCollidingWith(tor))	Game.malos[i].tag -= 3;
   				if (Game.malos[i].tag <= 0) Game.malos[i].Mata();
   			}
   		}
   		/*switch(timing%4)
   		{
	   		case 0:LanzaDisparo(Game.UnitLibre(shots, MAXDISPAROS), DWAVE2,tor.x,tor.y+8);break;
	   		//case 1:LanzaDisparo(Game.UnitLibre(shots, MAXDISPAROS), DGUSPIRA2,tor.x,tor.y+8);break;
	   		//case 2:LanzaDisparo(Game.UnitLibre(shots, MAXDISPAROS), DBOLA2,tor.x,tor.y+8);break;
	   		//case 3:LanzaDisparo(Game.UnitLibre(shots, MAXDISPAROS), DLASER2,tor.x,tor.y+8);break;
	   	}*/
	   	tor.estado++;
	   	if (tor.estado == 2)	for(int i = 0;i < Game.MAXMALOS;i++)Game.malos[i].tag -= 16;	
   		if (tor.timing > 170)
   		{
   			tor.Deactivate();
   			/*for(int i = 0;i < MAXDISPAROS;i++)
   				shots[i].Deactivate();
   			for(int i = 0;i < Game.MAXMALOS;i++)
   				Game.malos[i].tag -= 16;	*/
   		}
   	} 
   	   	
   	if (tag > 0 && !reviviendo)
   	{
	   	switch(ydir)
		   {
		      case -1: sy = -dx;
		      			ashape = 5;   	
	                  break;
		      case 0: if (sy > 0) sy--;
		              if (sy < 0) sy++;
		              if (ashape == 0) ashape = 7;
		              else if (ashape == 4) ashape = 5;
		              else  ashape = 6;   	
		              break;
		      case 1:  sy = dx;
		   			   ashape = 7;   	
		               break;
		   }
		   switch(xdir)
		   {
		      case -1: sx = -dx;	      
		         break;
		      case 0: if (sx > 0) sx--;
		              if (sx < 0) sx++;
		              break;	      
		      case 1: sx = dx;
		         break;
		   }
		   x += sx;
		   y += sy;
		   	   
		   if (sy == -dx && ashape == 5) ashape = 4 ;
		   if (sy == dx && ashape == 7) ashape = 0; 
		}		
		if (reviviendo)
		{
		  //MONGOFIX Game.Sonido(2,1);
			if (x < 20) x+=dx;
			else {reviviendo = false;
			 if (vidas < 0) {Game.estado = 1;Game.prestado = 1;}
			}
		}
	   else if (x < 0) x = 0;
	   if (!Game.faseacabada && x > 140) x = 140;
	   if (y < 0) y = 0;	   
	   if (Game.fase == 3)
	   {
	   	if (y > 117+16) y = 117+16;
	   }	    
	   else
	      if (y > 117) y = 117;
	   	   
	   if ((d_shot <= 0 && !reviviendo && autofire) || (!autofire && (firing || auto > 0) && d_shot <= 0)) 
	   {
	   	//Game.Sonido(2,1);
	   	if (auto > 0) auto--;	   	
	   	if (firing) auto = 3;
	   	d_shot = cadencia;
	   	int i = Game.UnitLibre(shots, MAXDISPAROS);
	   	LanzaDisparo(i,0,0,0);
	   	db = 1-db;
	   }
	   d_shot--;
	   ds_shot++;
	  
		
	   if (subdisp == DMISIL  && ds_shot%15 == 0 && !reviviendo)	   
	   	LanzaDisparo(Game.UnitLibre(shots, MAXDISPAROS),DMISIL,0,0);
	   
	 
		
	   for (int i = 0;i < MAXDISPAROS;i++)
	      if (shots[i].active) 
	      {
	      	ActualizaDisparo(i);
	      	if (Game.fase == 3)
	      	{
		      	int loc = (shots[i].x+4)/8+(((shots[i].y+4)/8)*(632+256))+(Game.scx/8);
		      	switch(Game.map[loc])
		      	{
		      		case 2:			case 7:			case 17:			case 23:
						case 44:			case 47:			case 49:			case 55:
						case 81:			case 83:			case 87:			case 96:			case 97:
						case 112:		case 113:		case 108:		case 111:  		case 42:
						case 43:			case 58:			case 59:			case 74:
						case 75:			case 90:			case 91:			case 89:			case 88:	
						case 103:		case 104:		case 105:		case 106:		case 107: break;
						default: Game.AddBala(shots[i].x,shots[i].y);
		      	   			shots[i].Deactivate();	      	
									break;
					}
				}
	      	for (int j=0;j < Game.MAXMALOS;j++)
	      	{
	      		if (!shots[i].active) continue;
	      	   if (Game.malos[j].active && Game.malos[j].isCollidingWith(shots[i]))
	      	   { 
	      	   	Game.malos[j].tag -= shots[i].tag;
	      	   	Game.AddBala(shots[i].x,shots[i].y);
	      	   	shots[i].Deactivate();	      	
	      	   	//ESPECIAL BOLA
	      	   	if (shots[i].numframes == DBOLA || shots[i].numframes == DBOLA2)
	      	   	{
	      	   		int val = shots[i].valia;
	      	   		if (val >= 0)
	      	   		{
	      	   			unit sig = shots[val];
	      	   			if (sig.active && (sig.numframes == DBOLA || sig.numframes == DBOLA2))
	      	   			{	      	   			
	      	   				sig.incy = -300+(Game.rnd.nextInt()%2)*600;
	      	   				sig.incx = (sig.incx>>1)+Game.rnd.nextInt()%128;	      	   				
	      	   			}
	      	   			if (sig.valia >= 0)
		      	   		{
		      	   			unit sig2 = shots[sig.valia];
		      	   			if (sig2.active && (sig2.numframes == DBOLA || sig2.numframes == DBOLA2))
		      	   			{
		      	   				sig2.incx = sig.incx;
	      	   				   sig2.incy = -sig.incy;
		      	   				/*sig2.incx = sig.incx;
		      	   				sig2.incy = sig.incy;
		      	   				int t = sig2.offy;
		      	   				sig2.offy = sig.offy;
		      	   				sig.offy = t;
		      	   				if (sig2.incy > 0) sig2.y -=40;
		      	   				else sig2.y += 40;*/
		      	   			}		      	   			
		      	   		}	
	      	   		}	      	   		
	      	   	}
	      	   	//CONT
	      	   	if (sarma < 256) sarma++;	      	   		      	   	
	      	   	if (Game.malos[j].tag <= 0){
	      	   		 Game.malos[j].Mata();
	      	   		 }
	      	   }
	      	 }
	      }	      
   }

	
	public void LanzaDisparo(int i, int ex,int _x, int _y)
	{
		//shots[i].Activate();
		int tx = 0,ty= 0;
		shots[i].numframes = tipodisp;
		if (ex != 0) shots[i].numframes = ex;
		if (_x != 0)
		{
			tx = x;
			ty = y;
			x = _x;
			y = _y;	
		}		
		switch (shots[i].numframes)
		{
			case DGUSPIRA:
			   shots[i].Auto(x+10,y+2,500,seq_shot[++p_shot%seq_shot.length]);			   
			   shots[i].tag = 4;
			   shots[i].ashape = 3;
			   shots[i].offx = 32;
				shots[i].offy = 40;
				break;
			case DGUSPIRA2:
			   shots[i].Auto(x+10,y+2,600,seq_shot[++p_shot%seq_shot.length]);			   
			   shots[i].tag = 5;
			   shots[i].ashape = 3;
			   shots[i].offx = 32;
				shots[i].offy = 40;
				break;				
			case DLASER:
				shots[i].tag = 3;
				shots[i].offx = 16;
				shots[i].offy = 113;
			   shots[i].Auto(x+10,y+2,800,0);
			   break;				
			case DLASER2:
				shots[i].tag = 4;
				shots[i].offx = 16;
				shots[i].offy = 105;
			   shots[i].Auto(x+10,y+2,800,0);
			   break;
			case DWAVE2:
			   int j = Game.UnitLibre(shots, MAXDISPAROS);
			   shots[j].numframes = tipodisp;		
	   		shots[j].tag = 3;
				shots[j].offx = 32;
				shots[j].offy = 24;
				shots[j].estado = 1;
			   shots[j].Auto(x+10,y+2,500,-500);
			   j = Game.UnitLibre(shots, MAXDISPAROS);
			   shots[j].numframes = tipodisp;		
	   		shots[j].tag = 3;
				shots[j].offx = 32;
				shots[j].offy = 24+8;
				shots[j].estado = 1;
			   shots[j].Auto(x+10,y+2,500,500);
			case DWAVE:
				shots[i].tag = 4;
				shots[i].offx = 0;
				shots[i].offy = 176;
			   shots[i].Auto(x+10,y+2,1000,0);
			   break;							   
			case DBOLA2:
		      /*j = Game.UnitLibre(shots, MAXDISPAROS);
			   shots[j].numframes = tipodisp;		
	   		shots[j].tag = 2;
				shots[j].offx = 0;
				shots[j].offy = 128+32;
				shots[j].Auto(x+3,y+2,500,500);			   
				shots[j].estado = 1;
				shots[j].valia = -1;			*/
				j = Game.UnitLibre(shots, MAXDISPAROS);
			   shots[j].numframes = tipodisp;		
	   		shots[j].tag = 2;
				shots[j].offx = 0;
				shots[j].offy = 128+32;
				shots[j].Auto(x+3,y+2,500,-500+db*1000);			   
				shots[j].estado = 1;
				shots[j].valia = -1;			
						   
			case DBOLA:
				shots[i].tag = 2;
				shots[i].offx = 0;
				shots[i].offy = 128;
			   shots[i].Auto(x+10,y+2,800,0);
			   j = Game.UnitLibre(shots, MAXDISPAROS);
			   shots[j].numframes = tipodisp;		
	   		shots[j].tag = 1;
				shots[j].offx = 0;
				shots[j].offy = 128+24;
				shots[j].Auto(x+3,y+2,800,0);			   
				shots[j].estado = 1;
				int k = Game.UnitLibre(shots, MAXDISPAROS);
			   shots[k].numframes = tipodisp;		
	   		shots[k].tag = 1;
				shots[k].offx = 0;
				shots[k].offy = 128+32;
				shots[k].Auto(x-2,y+2,800,0);			   
				shots[k].estado = 1;
				shots[i].valia = j;
				shots[j].valia = k;
				shots[k].valia = -1;
			   break;							   				   
			case DMISIL:
				shots[i].tag = 2;
				shots[i].offx = 24;
				shots[i].offy = 0;
			   shots[i].Auto(x+10,y+2,600,0);
			   shots[i].estado = 0;//angulo
			   BuscaObjetivo(i);
			   if (shots[i].valia == -1) shots[i].Deactivate();
			   break;   
		}
		if (_x != 0)
		{
			x = tx;
			y = ty;			
		}		
		
	}
	
	public void BuscaObjetivo(int i)
	{
		int near = -1;
		int min = 9999;
		for (int j = 0;j < Game.MAXMALOS;j++)				
		if (Game.malos[j].active && Game.malos[j].tag>0)
		{
				int d = Math.abs(shots[i].x-Game.malos[j].x)+Math.abs(shots[i].y-Game.malos[j].y);
				if (d < min) {min = d; near = j;}
		}
		shots[i].valia = near;
	}
	
	public void ActualizaDisparo(int i)
	{
		//numframes es el tipo de disparo
		switch(shots[i].numframes)
		{
			case DGUSPIRA:
			case DGUSPIRA2:			
				if (timing > 0) shots[i].Update(0,0,true);	   
	      	shots[i].incy = shots[i].incy+shots[i].incy/10;
	      	if (shots[i].incy > 0){
	      		 shots[i].ashape--;
	      		 if (shots[i].ashape < 0) shots[i].ashape = 7;
	      	}	      	 
	      	else shots[i].ashape = ++shots[i].ashape%8;	      	
	      	if (shots[i].out) shots[i].Deactivate();	      	
				break;
			case DBOLA:				
			case DBOLA2:				
				if (Game.fase == 1 && (shots[i].y < 8-8 || shots[i].y > 128-8)) shots[i].incy = -shots[i].incy;
				if (Game.fase == 2 && (shots[i].y < 18+8-8 || shots[i].y >128-18-8)) shots[i].incy = -shots[i].incy;
				if (Game.fase == 3 && (shots[i].y < 8-8 || shots[i].y >144-8)) shots[i].incy = -shots[i].incy;
			case DWAVE:				
			case DWAVE2:	
				if (shots[i].estado == 0) shots[i].ashape = shots[i].timing%3;				
			case DLASER:		
			case DLASER2:	
				if (timing > 0) shots[i].Update(0,0,true);	   
				if (shots[i].out) shots[i].Deactivate();	      	
				break;
				
			case DMISIL:	
				if (shots[i].valia != -1)	
				{
					int near = shots[i].valia;
					if (Game.malos[near].tipo != Game.MGUSANO && Game.malos[near].tipo != Game.MREINA)
					{
						unit ma = Game.malos[near];
						int ang = Trig.atan(ma.y-shots[i].y+((ma.cfy-ma.ciy)>>1)+ma.ciy, ma.x-shots[i].x+ma.cix);
						shots[i].incx  = (shots[i].incx+Trig.cos(ang)>>1)/2;					
						shots[i].incy = (shots[i].incy+Trig.sin(ang)>>1)/2;
						shots[i].estado = ang;
						if (!ma.active || ma.tag<=0) BuscaObjetivo(i);	      						
					}						
				}								
				shots[i].Update(0,0,true);	   
				if (shots[i].timing > 100) shots[i].Deactivate();	      	
			
		}
	}

	
	public  void Upgrade(int tip)
	{
		//MONGOFIX Game.Sonido(4,1);
		Game.addMsg(Lang.con1[tip]);		
		if (tip == IVIDA) {vidas++;return;}
		if (tip == IPOWERUP) {tag = energy;return;}		
		if (tip == IBOMBA) {tor.Activate();tor.setPosition(x,y);tor.timing=168;return;}		
		if (tip == IESCUDO) {inmunidad = 100;return;}		
		
		if (tip == tipodisp && tipodisp%2 == 0) return;
		if (tipodisp%2 == 0)		
			switch (tip)
			{
				case IGUSPIRA: tipodisp = DGUSPIRA2;cadencia = 5;break;
				case IWAVE:  	tipodisp = DWAVE2;cadencia = 5;break;				
				case ILASER:  	tipodisp = DLASER2;cadencia = 6;break;											
				case IBOLA:  	tipodisp = DBOLA2;cadencia = 7;break;											
				case IMISIL:   subdisp  = DMISIL;break;						
			}
		else	
			switch (tip)
			{
				case IGUSPIRA: if (tipodisp == DGUSPIRA){ tipodisp = DGUSPIRA2;cadencia = 5;}
									else {tipodisp = DGUSPIRA;cadencia = 6;}
									break;
				case IWAVE:  	if (tipodisp == DWAVE){ tipodisp = DWAVE2;cadencia = 6;}
									else {tipodisp = DWAVE;cadencia = 4;}
									break;				
				case ILASER:  	if (tipodisp == DLASER){ tipodisp = DLASER2;cadencia = 6;}
									else {tipodisp = DLASER;cadencia = 6;}
									break;							
				case IBOLA:  	if (tipodisp == DBOLA){ tipodisp = DBOLA2;cadencia = 7;}
									else {tipodisp = DBOLA;cadencia = 7;}
									break;															
				case IMISIL:  	subdisp = DMISIL;break;						
			}
	}

   public void blit(Graphics gr)
   {
   	if (vidas < 0)       
   	{
      	Game.Print(255,255,140,Lang.gameover,88,58,1);return;
      }
   	Game.blit(desp+offx,  ashape*height + offy, width, height, x, y,foo);
   	//gr.setClip(0, 0,Game.largo, Game.alto); 
	   //gr.clipRect(x,Game.offsety+y,width,height);	         
	   //gr.drawImage(foo,-desp+x-offx, Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	   
	   //      
	   //MOTOR
	   Game.blit(0,112+(timing%2)*8,6,7,x-4,y+2,foo);
	    
	   // ESCUDO 
   	if (inmunidad > 0 || antestag > tag)
   		Game.blit(40,105+(timing%5)*16,16,16,x+1,y-2,foo);
	   antestag = tag;
	   
	   for (int i = 0;i < MAXDISPAROS;i++)
   	{
   		if (!shots[i].active) continue;
			int k = (shots[i].timing-1);   		
   		switch(shots[i].numframes)
   		{
   			case DLASER2:
   					//shots[i].blit(gr);
   					Game.blit(16,121,8,8,shots[i].x+8,shots[i].y,foo);
   					//k = (shots[i].timing-1);
   					//if (shots[i].timing < 3) Game.blit(16,k*14,6,14,x+12,y-1,foo);
   					//break;   					   			
   			case DLASER: case DWAVE: case DWAVE2:
   				
   					if (shots[i].timing < 3) Game.blit(16,k*14,6,14,x+12,y-1,foo);   					
   					//shots[i].blit(gr);   		
   					break;
   			case DMISIL:
   					shots[i].ashape = shots[i].estado>>4;   					
   					//shots[i].blit(gr);
   					break;   					
   			case DBOLA: case DBOLA2:
   					//k = (shots[i].timing-1);
   					if (shots[i].timing < 3) Game.blit(16,29+(k*13),8,13,x+12,y,foo);   					
   					//shots[i].blit(gr);
   					break;
				case DGUSPIRA: case DGUSPIRA2:
						//k = (shots[i].timing-1);
   					if (shots[i].timing < 3) Game.blit(16,58+(k*12),8,12,x+12,y,foo);   					   			
   					break;
   			//default:
   					//k = (shots[i].timing-1);
   					//if (shots[i].timing < 3) Game.blit(16,56+(k*14),7,11,shots[i].x-2,shots[i].y-3,foo);   					
   					//shots[i].blit(gr);
   			//		break;
   		}	
   		shots[i].blit(gr);
   		
   			
			   
		}
   	if (tor.active) 
		{
		   	if (tor.timing < 150 || timing%2 == 0) Game.blitc(10,128+(24*(2-(timing%3))),21,24,tor.x,tor.y,foo);
		   	gr.setClip(0,0,176,128);
		   	if (tor.estado == 1 || tor.estado == 3) {gr.setColor(0,0,0);gr.fillRect(0,0,176,128);}
		   	else if (tor.estado == 2){gr.setColor(255,255,255);gr.fillRect(0,0,176,128);}	   	   
		}
   
      
   }  
     
     

   
	
}