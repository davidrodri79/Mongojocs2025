package com.mygdx.mongojocs.sekhmet;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class leon extends unit
{
	final static int IZQUIERDA = -2;
   final static int DERECHA = -1;
   
   final static int QUIETO       = 0;
   final static int APATRULLANDO = 1;
   final static int ATACANDO = 2;
   //final static int GIRANDODE = 3;
   //final static int GIRANDOIZ = 4;
   
   int sx=0, sy=0;
	//final int dx = 2;
	//final int dy = 5;
   int dbj;
	boolean saltando = false;
	int estado = APATRULLANDO;
   int lado = IZQUIERDA;
   int anim = 0;
	
	public leon(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j);	 
	   setCollisionRectangle(3*2,0,28,h-2);
      tag = 2+(Game.fase/4);	            
	}


   public void Apatrulla()
   {
      if (anim >= 5) anim = 0;
	   if (lado == DERECHA) ashape = anim;
      else ashape = anim + 5;
   }
      
   public void Moving()
   {
      int pies;
      
      if (sx != 0)//Colisiones laterales
	   {
	      pies = x+(width/2);
	      if (Game.Colisiona(pies-(width/2),y+height, this,true) == 0 && sx < 0) lado = DERECHA;	              
	      if (Game.Colisiona(pies-(width/2),y+height/2, this,true) == 1 && sx < 0) {lado = QUIETO;estado=3;anim=0;}
	      if (Game.Colisiona(pies+(width/2),y+height, this,true) == 0 && sx > 0) lado = IZQUIERDA;	      
	      if (Game.Colisiona(pies+(width/2),y+height/2, this,true) == 1 && sx > 0) {lado = QUIETO;estado=4;anim=0;}	      
	   }
	   
	   if (lado == DERECHA) sx = 1*3;
      else sx = -(1*3);
	   if (lado==QUIETO) sx=0;
	   if (estado == ATACANDO) sx = sx*2;
   }

   public void Muerte()
   {
   	Game.Sonido(2,1);
      //anim = 0;
      sx = 0;
      
   }

   public void Update(Prota prota)
   {
      if (Game.offsetx+x+(40*2) < -(width) || Game.offsety+y < -(height) || Game.offsetx+x > Game.largo+(40*2) || Game.offsety+y > Game.hmap)  visible=false;
	   else 
	   {
	   visible=true;
      
      if (tag> 0 && prota.lanza.timing < 25 && prota.lanza.active)
      {
         if (isCollidingWith(prota.lanza))
         {   
             tag--;
             prota.lanza.Deactivate();
             //if (tag <= 0) {
             	Muerte();if (prota.lanza.lado) lado = IZQUIERDA;else lado = DERECHA;
             	//}
         }
      }
      
      Aterrizage();   
      anim++;
      
      if (tag > 0) 
      {
      	if (isCollidingWith(prota) || prota.isCollidingWith(this)) prota.tag-=4;
      	
         if (estado!= 3 && estado!=4)
         {
            if ((Math.abs(prota.x-x)< (30*2)) && (Math.abs(prota.y-y)< (20))) estado = ATACANDO;         	
            else estado = APATRULLANDO;
         }
         
         if (estado == APATRULLANDO) Apatrulla();
         if (estado == ATACANDO)
         {
            if (prota.x > x-8) 
            {
            	if (sx<0)
            	   {lado = QUIETO;estado=3;anim=0;}
               //lado = DERECHA;
            }   
            else 
            {
            	if (sx>0)
               {lado = QUIETO;estado=4;anim=0;}
               //lado = IZQUIERDA;
            }
            Apatrulla();
            //if (anim >= 6) anim = 0;
	         //if (lado == DERECHA) ashape = anim + 10;
            //else ashape = anim + 16;
         }
         if (estado == 3)
         {
         	if (anim >= 5) {estado = APATRULLANDO;lado=DERECHA;}
         	ashape = 16+anim;
         }
         if (estado == 4)
         {
         	if (anim >= 5) {estado = APATRULLANDO;lado=IZQUIERDA;}
         	ashape = 10+anim;
         }
         
         Moving();     

         
      }
      else 
      {
         if (anim >= 4) anim=3;
         if (lado == DERECHA) ashape = anim+22;
         else ashape = anim + 26;
         
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
	   if ((Game.Colisiona(pies-(2*4),y+height, this,true) == 0) &&
	       (Game.Colisiona(pies+(2*4),y+height, this,true) == 0))
	   {
	      saltando=true;
	      if (dbj>0 && sy < 7) sy+=(1*2);
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
	   gr.clipRect(Game.offsetx+x,Game.offsety+y,width,height);	  
      if (ashape < 22)      
	      gr.drawImage(foo,Game.offsetx+x-offx, Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	         
	   else 
	      gr.drawImage(foo,-(18*2)+Game.offsetx+x-offx, Game.offsety+y - (ashape-22)*height - offy, gr.TOP|gr.LEFT);	         
   }
	
	
}