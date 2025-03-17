package com.mygdx.mongojocs.sekhmet;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class momia extends unit
{
	//final int IZQUIERDA = -1;
   //final int DERECHA = -2;
   
   //final int QUIETO       = 0;
   //final int ANDANDO = 1;
   //final int ATACANDO = 2;
   final static int MAXVENDAS = 3;
   
   int sx=0, sy=0;
	int estado = 0;
   int lado = -1;
   int anim = 0;
   unit venda[];
   int pupa = -5;
	int timing2;
	
	public momia(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j);	   
	   venda = new unit[MAXVENDAS];
	   for (int k = 0;k < MAXVENDAS;k++)
	   {
	      venda[k] = new unit(i,72,7,8,120,0);	     
	      venda[k].Deactivate();
	   }
	   setCollisionRectangle(6*2,2*2,12*2,28*2);
      Activate();
      tag = 30;
      x = 95*2;
      y = 121*2;
      timing2 = 0;
      //timing=-40;          
	}
          
   public void Muerte()
   {
      Game.Sonido(4,1);	
      timing2 = 0;
      //anim = 0;
      //sx = 0;
   }

	boolean abriendo = false;
	int tapaframe = 0;

   public void Update(Prota prota)
   {
     sx = 0;
     timing2++;
      if (tag > 0) 
      {
      	//if (isCollidingWith(prota)) prota.tag--;
      	switch(estado)
      	{
      	  case 0: visible = false;
      	  					if (prota.x > 80*2) abriendo = true;
      	  					if (abriendo && timing%10 == 0) tapaframe++;
      	  					if (tapaframe == 4) {estado = 1;visible=true;timing=0;}
      	  					break; 
           case 1:
            		  if (timing == 6 ) {anim=1;}
                    if (timing == 12) {anim=2;sx=1*2;}
                    if (timing >= 18) 
                    {
                    	   sx=1;
                    		anim=0;timing=0;   
                    		if (prota.x > x) lado = -2;
            		  		else lado = -1;
            		  		if (x < 8*2)  lado = -2;
            		  	   if (x > 112*2) lado = -1;
            		  	   if (Math.abs(prota.x-x) < 30*2 || timing2 > 300) {timing=0;timing2=0;estado = 2;anim=0;}
                    }
                    if (lado == -1) sx = -sx;
                    ashape = anim;                  
                    
                    break; 
            case 2:            		  
            		  if (timing == 6 ) anim=1;
                    if (timing == 12) anim=2;
                    if (timing == 18) anim=3;
                    if (timing == 24)                    
                    		AddVenda(prota.y, 0);
                    if (timing == 34) anim=2;       
                    if (timing == 44) anim=3;       
                    if (timing == 54) 
                    		AddVenda(prota.y, 1);
                    if (timing == 64) anim=2;       
                    if (timing == 74) anim=3;       
                    if (timing == 84) 
                    		AddVenda(prota.y, 2);                    		
                    /*if (timing == 54) anim=2;       
                    if (timing == 64) anim=3;       
                    if (timing == 74) 
                    		AddVenda(prota, 1);*/
                    if (timing > 120) {anim=0;estado = 1;timing=0;}
                    ashape = anim+3; 
                    break;
     			}
     			if (estado > 0 && prota.lanza.timing < 25 && prota.lanza.active)
      		{
         		if (isCollidingWith(prota.lanza))
         		{  
         			if (pupa == -5) 
         			{
             			tag--;
             			pupa = 7;             		
             			if (tag <= 0) {Muerte();if (prota.lanza.lado) lado = -2;else lado = -1;}
             		}
             		prota.lanza.Deactivate();             		
         		}
     			}          
      		if (pupa > 0) ashape=7;
      		if (pupa > -5) pupa--;
            x += sx;
	         y += sy;	      	   
	      
         //Moving();     			
      }
      else 
      {      	
      	ashape=7;      	
      	if (timing2 < 120)
      	{
      		x+=(timing2%3)-1;
      		int tt = -5;
      		//if (lado == -1) tt = 10; 
      		Game.AddFuego(x+tt,y);      	
	      	Game.AddFuego(x+tt,y); 
	      	Game.AddFuego(x+tt,y);      	
	      	Game.AddFuego(x+tt,y); 
	      }
      	if (timing2 > 100) Deactivate();      	
      }
      for (int i = 0;i < MAXVENDAS;i++)
      	if (venda[i].active)
      	{
      		venda[i].timing++;      		
            if (venda[i].timing > 0) venda[i].x -= venda[i].tag*4*2;
            if (venda[i].timing == 4) {venda[i].ashape=1;venda[i].x += venda[i].tag*4*2;}                    
            if (venda[i].timing == 7) {venda[i].ashape=2;venda[i].x += venda[i].tag*8*2;}                    
            if (venda[i].timing == 11) {venda[i].ashape=3;venda[i].x += venda[i].tag*8*2;}                    
            if (venda[i].timing == 15) {venda[i].ashape=4;venda[i].x += venda[i].tag*8*2;}                    
            venda[i].setCollisionRectangle((146-vend2[venda[i].ashape])/2,1,vend2[venda[i].ashape],8);
            if (venda[i].isCollidingWith(prota) || prota.isCollidingWith(venda[i])) prota.tag-=8;          
         }           
         
      
      //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);	   	   
	 	timing++;
    
   }
   
   //int vend[] = {33,29,23,(146-76)",8};
   int vend2[]= {8,30,60,76,130};
   int seq[] = {5,19,0,10,19,18,5,-1};
   int puntero = 0;
   

	
	private void AddVenda(int _y, int i)
	{
		venda[i].Activate();
      venda[i].setPosition(x-(26*2),Math.max(_y+8, y));
      //y+(seq[puntero]*2));
      venda[i].timing = 0;
      venda[i].ashape = 0;
      if (lado==-2) venda[i].tag = -1;
      else venda[i].tag = 1;
      puntero++;
      //if (seq[puntero] == -1) puntero = 0;				
   }

	public void blit(Graphics gr)
   {   	      
   	numframes = estado;         
   	gr.setClip(0,0,Game.w,Game.hmap);
      gr.clipRect(Game.offsetx+(100*2),Game.offsety+(115*2),(24*2),(38)*2);	   
      gr.drawImage(foo,Game.offsetx+(100*2)-tapaframe*(24*2), Game.offsety+(115*2), gr.TOP|gr.LEFT);	         
      if (!visible) return;
      
      for (int i = 0;i < MAXVENDAS;i++)
         venda[i].blit(gr);      
      if (lado == -2)	  ashape=ashape+8;						  
      gr.setClip(0,0,Game.w,Game.hmap);
      gr.clipRect(Game.offsetx+x,Game.offsety+y,width,height);	         
      if (ashape < 8)      
	      gr.drawImage(foo,Game.offsetx+x-offx - ashape*width, Game.offsety+y  - offy, gr.TOP|gr.LEFT);	         
	   else
	      gr.drawImage(foo,Game.offsetx+x-offx - (ashape-8)*width, Game.offsety+y - (31*2) - offy, gr.TOP|gr.LEFT);	                 
   }
	
	
}