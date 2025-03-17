package com.mygdx.mongojocs.sekhmet;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class malo1 extends unit
{
	final static boolean IZQUIERDA = false;
   final static boolean DERECHA = true;
   
   final static int QUIETO       = 0;
   final static int APATRULLANDO = 1;
   final static int ATACANDO = 2;
   
   int sx=0, sy=0;
	//final int dx = 2;
	//final int dy = 5;
   int dbj;
	boolean saltando = false;
	int estado = APATRULLANDO;
   boolean lado = IZQUIERDA;
   int anim = 0;
	
	public malo1(Image i, int w, int h, int numf, int g, int j, boolean l)
	{	
		super(i, w, h, numf,g,j);	  
	   setCollisionRectangle(12,0,8*2,8*2); 
	   tag = 3+(Game.fase/4);	   
	   lado = l;
	}

	public malo1(Image i, int w, int h, int numf,int g, int j)
	{	
	   super(i, w, h, numf,g,j);	  
	   setCollisionRectangle(12,0,8*2,8*2); 
	   tag = 3;	   
	}


   public void Apatrulla()
   {
      if (anim >= 5) anim = 0;
	   if (lado == DERECHA) ashape = anim+5;
      else ashape = anim;
   }
      
   public boolean Condicion(Prota prota)
   {
   	int pies;	
		//comprovamos si esta en el suelo	 
	   pies = x+(width/2);
	   
   	if (estado == ATACANDO &&
   		 Game.Colisiona(pies-(2*2),y+height,this,true) != 0 &&
	       Game.Colisiona(pies+(2*2),y+height,this,true) != 0	&&         
	       Math.abs(prota.y-y) < 40 && saltando == false)
	       {
	       	saltando = true;
	         return true;
	        }
   	return false;	 
   	
   }   
      
   public void Moving(Prota prota)
   {
      int pies;
      
      if (sx != 0)//Colisiones laterales
	   {
	      pies = x+(width/2);
	      if (Game.Colisiona(pies-(4*2),y+height, this,true) == 0 && sx < 0 && !saltando) {lado = DERECHA;if (Condicion(prota)) {sy = -5;lado = IZQUIERDA;}}
	      if (Game.Colisiona(pies+(4*2),y+height, this,true) == 0 && sx > 0 && !saltando) {lado = IZQUIERDA;if (Condicion(prota)) {sy = -5;lado = DERECHA;}}	
	      if (Game.Colisiona(pies-(4*2),y+height/2, this,true) == 1 && sx < 0 && !saltando) {lado = DERECHA;sx = 0;}
	      if (Game.Colisiona(pies+(4*2),y+height/2, this,true) == 1 && sx > 0  && !saltando) {lado = IZQUIERDA;sx = 0;}	      
	   }
	   if (repel == 0)
	   {
	   	if (lado == DERECHA) sx = (1*2);
      	else sx = -(1*2);
	   }
   }

   public void Muerte()
   {
   	Game.Sonido(2,1);
      //anim = 0;
      sx = 0;
   }
	int repel = 0;

   public void Update(Prota prota)
   {
      if (sy == 0 && (Game.offsetx+x+(40*3) < -width || Game.offsety+y+40 < -height || Game.offsetx+x > Game.largo+(40*3) || Game.offsety+y > Game.alto+60))  visible=false;
	   else 
	   {
	   visible=true;
      
      if (tag> 0 && prota.lanza.timing < 25 && prota.lanza.active)
      {
         if (isCollidingWith(prota.lanza))
         {   
             tag--;             
             repel = 4;
             	
             if (prota.lanza.lado == DERECHA)
             {
             	sx = 8;
             	lado = IZQUIERDA;               
             }  
             else
             {
             	sx = -8;
             	lado = DERECHA;             	
             }
             prota.lanza.Deactivate();
             //if (tag <= 0) {
             if (tag <= 0)
             {
             	 Muerte();
             	 lado=prota.lanza.lado;
             }
             	//}
         }
      }
   
      
      Aterrizage();   
      anim++;
      
      if (tag > 0) 
      {
      	if (isCollidingWith(prota)) prota.tag-=3;
         estado = APATRULLANDO;
         if ((Math.abs(prota.x-x)< (20*3)) && (Math.abs(prota.y-y)< (10*3))) estado = ATACANDO;         
         
         if (repel == 0)
         {
         	if (estado == APATRULLANDO) Apatrulla();
	         if (estado == ATACANDO)
	         {
	            if (prota.x > x) lado = DERECHA;
	            else lado = IZQUIERDA;
	            if (anim >= 6) anim = 0;
		         if (lado == DERECHA) ashape = anim + 16;
	            else ashape = anim + 10;
	         }
	        	Moving(prota);     
         }
         else
         {
         	Moving(prota);     
         	repel--;
         	if (sx!=0) sx = sx-(sx/Math.abs(sx));
         	/*if (lado == DERECHA) x-=8;
         	else x+=8;*/
         }	
         
      }
      else 
      {
         if (anim >= 5) {anim=4;sx = 0;}
         if (lado == DERECHA) ashape = anim+22;
         else ashape = anim + 27;        
      }
      x += sx;
	   y += sy;	      	   
	   //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);	   	   
	   }
   }


   public void Aterrizage()
	{
	   int pies;	
		//comprovamos si esta en el suelo	 
	   pies = x+(width/2);
	   if ((Game.Colisiona(pies-(2*2),y+height, this,true) == 0) &&
	       (Game.Colisiona(pies+(2*2),y+height, this,true) == 0))
	   {
	      saltando=true;
	      if (dbj>0 && sy < (7*2)) sy++;
	      dbj++;if (dbj==3) dbj = 0;
	   }	     	   
	   else if (sy > 0)
	   {   
	      sy = 0; 
	      y = (((y+height)/16)*16)-height;
	      saltando=false;
	   }
	   
	}
	
 
	public void blit(Graphics gr)
   {
      if (!visible) return;
      gr.setClip(0,0,Game.wmap,Game.hmap);	 
	   gr.clipRect(Game.offsetx+x,Game.offsety+y,width-1,height);	         	   
	    if (ashape < 17)      
	      gr.drawImage(foo,Game.offsetx+x-offx, Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	         
	   else 
	      gr.drawImage(foo,-(23*2)+Game.offsetx+x-offx, Game.offsety+y - (ashape-17)*height - offy, gr.TOP|gr.LEFT);	         
   }
	
	
}