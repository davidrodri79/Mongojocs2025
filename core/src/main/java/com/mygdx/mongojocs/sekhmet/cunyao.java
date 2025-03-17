package com.mygdx.mongojocs.sekhmet;


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class cunyao extends unit
{
	final static int IZQUIERDA = -1;
   final static int DERECHA = -2;
   
   final static int QUIETO       = 0;
   final static int SALTANDO = 1;
   final static int ATACANDO = 2;
   final static int MAXPIEDRAS = 4;
   
   int sx=0, sy=0;
	int estado = QUIETO;
   int lado = IZQUIERDA;
   int anim = 0;
   unit piedra[];
	
	public cunyao(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j);	   
	   piedra = new unit[MAXPIEDRAS];
	   for (int k = 0;k < MAXPIEDRAS;k++)
	   {
	      piedra[k] = new unit(i,15,11,8,0,0);	     
	   	piedra[k].setCollisionRectangle(8,2*2,8*2,7*2);   
	   	piedra[k].Deactivate();
	   }    
	   setCollisionRectangle(4,1*2,36,20*2);
      //Activate();
      tag = 40;
      x = (65*2)-2;
      y = 59*2;
      timing=-40;          
	}
       
   public void Moving()
   {/*
      int pies;
      if (sx != 0)//Colisiones laterales
	   {
	      pies = x+(width/2);
	      if (Game.Colisiona(pies-4,y+height) == 0 && sx < 0) lado = DERECHA;	               
	      if (Game.Colisiona(pies+4,y+height) == 0 && sx > 0) lado = IZQUIERDA;	      
	   }*/
	   if (lado == DERECHA) sx = 1*2;
      else sx = -1*2;
	   if (estado!=SALTANDO) sx=0;
   }

   public void Muerte()
   {
   	Game.Sonido(4,1);
      anim = 0;
      sx = 0;
   }

   public void Update(Prota prota)
   {
     if (tag> 0 && prota.lanza.timing < 25 && prota.lanza.active)
      {
         if (isCollidingWith(prota.lanza))
         {   
             tag--;
             prota.lanza.Deactivate();
             if (tag <= 0) {Muerte();if (prota.lanza.lado) lado = DERECHA;else lado = IZQUIERDA;timing = 0;}
         }
      }
      if (tag > 0) 
      {
      	if (isCollidingWith(prota)) prota.tag-=4;
      	switch(estado)
      	{
      	   case QUIETO: 
       	           anim++;      
      	   		  if (prota.x > x) lado = DERECHA;
            		  else lado = IZQUIERDA;
      	           if (anim >= 2) anim = 0;
      	           if (lado == DERECHA) ashape = anim + 6;
                    else ashape = anim;
                    if (timing == 20)
                    	  {estado = SALTANDO;timing = 0;anim=0;}
                    break;
            case SALTANDO:
            		  if (timing == 1)
            		  {
            		  	 if (x < (40*2))  lado = DERECHA;
            		  	 if (x > (90*2)) lado = IZQUIERDA;
            		  }
                    if (timing == 2) anim++;
                    if (timing == 4) anim++;                       
                    if (timing >=4 ) 
                    {
                       int caida = -(128*2)+((6*2)*(int)timing);
                       if (caida > 0) anim=3;
                    	  Auto(x,y,0,caida);ActAuto(); 
                    	  if (y > (59*2)) {y = (59*2);estado=ATACANDO;anim=0;timing=0;}
                    }
                    if (lado == DERECHA)
                    	  ashape=anim+8;
						  else
						  	  ashape=anim+2;
                    break; 
            case ATACANDO:
                    anim++; 
            		  if (prota.x > x) lado = DERECHA;
            		  else lado = IZQUIERDA;            		  
            		  if (anim >= 2) anim = 0;            		  
      	           if (lado == DERECHA) ashape = anim + 14;
                    else ashape = 12+anim;                    
                    if (timing == 10) {Caepiedra(0);}
                    if (timing == 15) {Caepiedra(1);}
                    if (timing == 23) {Caepiedra(2);}
                    if (timing == 35) {Caepiedra(3);}                                         
                    if (timing == 70) {estado=QUIETO;timing=0;}
            		  break;
      
         }
         Moving();     
         
      }
      else 
      {
      	ashape = 12;
      	if (timing < 116)
      	{
      		x+=(timing%3)-1;
      		//int tt = -10;
      		//if (lado == DERECHA) tt = 10; 
      		//if (estado == ATACANDO) tt = 0;     	
	      	Game.AddFuego(x,y);      	
	      	Game.AddFuego(x,y); 
	      	Game.AddFuego(x,y);      	
	      	Game.AddFuego(x,y); 
	      }
      	if (timing > 100) Deactivate();      	
      }
      for (int i = 0;i < MAXPIEDRAS;i++)      
         if (piedra[i].timing <= 30)
         {
      	     piedra[i].ActAuto();
        	     piedra[i].timing++;
              if (piedra[i].timing%10 == 0) piedra[i].ashape++; 
              if (piedra[i].isCollidingWith(prota) || prota.isCollidingWith(piedra[i])) prota.tag-=16;
         }
         else {piedra[i].ashape++;if (piedra[i].ashape == 8) piedra[i].Deactivate();}  
      x += sx;
	   y += sy;	      	   
	   //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);	   	   
	 	timing++;
    
   }

	
	int puntero = 0;
	
	private void Caepiedra(int i)
	{
		int seq[] = {35,60,50,80,23,101,62,85,119,44,75,-1};		
		piedra[i].Auto(seq[puntero]*2,16*2,0,200*2);
		piedra[i].Activate();
		puntero++;
		if (seq[puntero] == -1) puntero = 0;
   }

	public void blit(Graphics gr)
   {
   	for (int i = 0;i < MAXPIEDRAS;i++)
         piedra[i].blit(gr);      
      if (!visible) return;
        gr.setClip(0,0,Game.wmap,Game.hmap);	 
      gr.clipRect(Game.offsetx+x,Game.offsety+y,width,height);	  
             
      if (ashape < 4)      
	      gr.drawImage(foo,Game.offsetx+x-offx - ashape*width, Game.offsety+y  - offy, gr.TOP|gr.LEFT);	         
	   else if (ashape < 8)      
	      gr.drawImage(foo,Game.offsetx+x-offx - (ashape-4)*width, Game.offsety+y - (30*2) - offy, gr.TOP|gr.LEFT);	        
	   else if (ashape < 12)      
	      gr.drawImage(foo,Game.offsetx+x-offx - (ashape-8)*width, Game.offsety+y - (60*2) - offy, gr.TOP|gr.LEFT);	            
	   else
	      gr.drawImage(foo,Game.offsetx+x-offx - (ashape-12)*width, Game.offsety+y - (90*2) - offy, gr.TOP|gr.LEFT);	                 
   }
	
	
}