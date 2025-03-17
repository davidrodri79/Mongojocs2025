package com.mygdx.mongojocs.sekhmet;


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class Prota extends unit
{
   protected Lanza lanza;	
	final static boolean IZQUIERDA = false;
   final static boolean DERECHA = true;
	int sx, sy;
	//final int dx = 2;
	//final int dy = 5;
	boolean lado = DERECHA;
	//boolean llevalanza = true;
	boolean saltando = false;
	boolean disparando = false;
	boolean alive = true;
	int dbj;
	int anim = 0, animf;
	
	public Prota(Image i)
	{	
	   super(i, 16, 13, 38,0,0);
	   lanza = new Lanza(i, 10,10, 8);	
	   lanza.Deactivate();   
	   setCollisionRectangle(6,5,20,21);
	   //Activate();   	   
	}

/*
   public void Init()
   {
      x = 10;
      y = 40;      
   }*/


   public void Salto(int ydir)
   {
      
      if (ydir==-1)
      {
      //switch(ydir)
	   //{
	      //case -1: 
	      int pies = x+(width/2);
	      if ((Game.Colisiona(pies-(5),y+height, this,false) > 0) ||
	          (Game.Colisiona(pies+(5),y+height, this,false) > 0) || 
	          ColLanza() || 
	          SobrePlataforma() || y >= (Game.mapah*16)-height)		               
	         {sy = -(12);dbj=0;}
	      /*if (Game.ascensor != null)  
	      {	      
	      	for(int i = 0;i < Game.ascensor.length;i++)
	      	{	y+=2;
	            if (isCollidingWith(Game.ascensor[i]))   {sy = -dy;dbj=0;y-=10;}	         
	            y-=2;
	         }
	      }   */
        //          break;	    
	   //} 
	   }
   }  

   public void Desplazamiento(int xdir)
   {
      switch(xdir)
	   {
	      case -1: if (sx == 0) sx = -2;
	      			else sx = -(2*2);	      
	               lado = IZQUIERDA;
	               anim++;if (anim >= 6) anim = 0;
	               break;	               
	      case 0: if (!saltando)
	              {
	                 if (sx > 0) sx-=2;
	                 if (sx < 0) sx+=2;
	              }
	              else if (dbj == 0)
	              {
	                 if (sx > 1*2) sx-=2;
	                 if (sx < -1*2) sx+=2;
	              }
	              break;	      
	      case 1: if (sx == 0) sx = 2;
	      		  else sx = (2*2);
	              lado = DERECHA;
	              anim++;if (anim >= 6) anim = 0;
	              break;
	   }	   
	
   }


   public void ColLaterales()
   {
      int pies;
      if (sx != 0)//Colisiones laterales
	   {
	      pies = x+(width/2);
	      if (Game.Colisiona(pies-(9),y+(8*2), this,false) == 1 && sx < 0) sx = 0;	               
	      if (Game.Colisiona(pies+(9),y+(8*2), this,false) == 1 && sx > 0) sx = 0;	      //era 8 y no 10
	   }
	}	
	
	public boolean ColLanza()
	{
		if (!lanza.active) return false;
	   if ((x > lanza.x-(9*2) && x < lanza.x + (7*2))
	      && (y < lanza.y-(10*2) && y+(8*2) > lanza.y-(10*2)))
	      return true;
	   return false;
	}
	
	
	int asc;
	
	public boolean SobrePlataforma()
	{
		int pies = x+(width/2);
	   boolean sobre = false;
	   if (Game.ascensor !=null)
		   for (int i = 0;i < Game.ascensor.length; i++)
		   {
		   	unit p = Game.ascensor[i];
	   	   if (sy >= 0 && pies >= p.x && pies <= p.x+p.width && Math.abs(y+height - p.y) < (7*2) && y < p.y)
      	   {
      	   	sobre = true;
      	   	y = p.y-height;//+(1*2);
      	   	saltando=false;
      	   	dbj=0;      	   	
      	   	sy=0;
      	   	asc = (p.incx/100);         	   
      	   }
      	}         
      if (Game.piedra !=null && !sobre)
		   for (int i = 0;i < Game.piedra.length; i++)
		   {
		   	unit p = Game.piedra[i];
	   	   if (sy >= 0 && pies >= p.x && pies <= p.x+p.width && Math.abs(y+height - p.numframes) < (7*2) && y < p.numframes)
      	   {
      	   	sobre = true;
      	   	y = p.numframes-height;//+(1*2);
      	   	saltando=false;
      	   	dbj=0;      	   	
      	   	sy=0;      	   	      	   	
      	   }
      	}         	
      return sobre;	               
	}
	   
	public void Aterrizage()
	{
		
		int pies;	
		//comprovamos si esta en el suelo	 
	   pies = x+(width/2);
	   if ((Game.Colisiona(pies-(4),y+height+8, this,false) == 0) &&
	       (Game.Colisiona(pies+(4),y+height+8, this,false) == 0) && !ColLanza() && !SobrePlataforma())
	   {
	      saltando=true;
	      if (dbj>0 && sy < 7) sy+=(1*2);
	      dbj++;if (dbj==4) dbj = 0;
	   }	     	   	   
	   else if (sy >= 0)
	   {   
	      sy = 0; 	      
	      if (ColLanza()) y = lanza.y-(11*2);
	      //else if (SobrePlataforma()) y = Game.ascensor[asc].y-height;
	      else 
	      {
	      	if (!SobrePlataforma()) y = (((y+height+8)/16)*16)-height;	      
	      }
	      saltando=false;
	   }
	   else
	   {
	   	saltando=true;
	      if (dbj>0 && sy < 7) sy+=(1*2);
	      dbj++;if (dbj==4) dbj = 0;
	   }
	   //cabezazo
	   if (sy < 0 &&(Game.Colisiona(pies-(5),y-(3*2), this,false) == 1) || 
	       (Game.Colisiona(pies+(5),y-(3*2), this,false) == 1)
	       //&& (Game.Colisiona(pies+4,y-11) != 1)
	       //&& (Game.Colisiona(pies-5,y-11) != 1)
	       )
	   	sy=(1*2);	   
	   //cabezazo piedra
	    if (Game.piedra !=null)
		   for (int i = 0;i < Game.piedra.length; i++)
		   {
		   	unit p = Game.piedra[i];
	   	   if (sy < 0 && pies >= p.x && pies <= p.x+p.width && Math.abs(y+height - p.numframes-p.height) < (7*2))      	   
      	   	sy=(1*2);	         	   
      	}         		
	   //if (ColLanza() && sy>0) {sy=0;y = lanza.y-10;saltando=false;}
	} 
		
   public void CalculaFrame()
   {
	   if (!disparando)
	   {
	      if (lado == IZQUIERDA) setFrame(anim);
	      else setFrame(6 + anim);
	   }
	   else
	   {
	      if (lado == IZQUIERDA) setFrame(12+animf);
	      else setFrame(18 + animf);
	      if (animf < 5) 
	      {  
	         animf++;
	         if (animf == 2) //LANZAR LA LANZA
	         {
	            lanza.Activate(); 
	            
	            lanza.lado = lado;
	            if (lado==IZQUIERDA) {lanza.Auto(x+4,y+6, -1000,-60);lanza.setFrame(3);}
	            else {lanza.Auto(x+8,y+6, 1000,-60);lanza.setFrame(1);}
	         }	      
	      }
	      else	      
	         disparando = false;	         	      
	   }
	   
	   
	   if (tag<=0)
	   {	
	   	Game.Sonido(6,1);   	
	   	if (tag == 0){tag = -1;animf = 0;}
	      if (lado == IZQUIERDA) setFrame(24+animf);
	      else setFrame(31 + animf);
	      if (animf < 6) animf++;
	      else alive = false;
	   } 
   }		
		
   public void Update(int xdir, int ydir, boolean firing)
   {  
   	//tag = 10;    
      //int pies;
      //SobrePlataforma();
      Game.Colisiona(x+(width/2),y+(8*2), this,true);	               
	      
      asc = 0;
      if (tag>0)
      {		
	      Salto(ydir);
	      Desplazamiento(xdir);
	      ColLaterales();
	   }
	   Aterrizage();
	   
	   if (tag>0) x += sx;
	   
	   
	   int pies = x+(width/2);
	   if (asc < 0 && Game.Colisiona(pies-(9),y+(8*2), this,false) == 1) ;	               
	   else if (asc > 0 && Game.Colisiona(pies+(9),y+(8*2), this,false) == 1 && sx > 0) ;
	   else x += asc;
	   
	   
	   
	   
	   y += sy;	      	   
	   if (x > (Game.mapaw*16)-(12*2)) x = (Game.mapaw*16)-(12*2);
	   if (y > (Game.mapah*16)-(13*2)) y = (Game.mapah*16)-(13*2);
	   if (x < 0) x=0;
	   //if (y < -height) y=-height;
	   //////////////////////
	   
	   //if (y > 12*7) y = 12*7; //mierda para no bajar demasiado de la pantalla

		if (tag>0)	   	   
	   if (firing && (!lanza.active || lanza.timing>=20) && !disparando) 
	   {
	   	Game.Sonido(1,1);
	      disparando = true;
	      animf = 0;
	   }	   	   	   
	   //setPosition(x, y);	   
	   CalculaFrame();
	   lanza.Update();
	   
	   
	   
   }
   
   public void blit(Graphics gr)
   {
      //if (!visible) return;
      gr.clipRect(Game.offsetx+x,Game.offsety+y,width,height);	         
      if (ashape < 19)
         gr.drawImage(foo,Game.offsetx+x,(2*2)+Game.offsety+y - ashape*height, gr.TOP|gr.LEFT);	         
	   else
	      gr.drawImage(foo,Game.offsetx+x-(16*2),(2*2)+Game.offsety+y - (ashape-19)*height, gr.TOP|gr.LEFT);	         
	   lanza.blit(gr);
   }
	
}