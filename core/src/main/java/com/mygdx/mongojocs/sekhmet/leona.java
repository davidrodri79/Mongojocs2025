package com.mygdx.mongojocs.sekhmet;


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class leona extends unit
{
	//final int IZQUIERDA = -1;
   //final int DERECHA = -2;
   
   
   //final int QUIETO       = 0;
   final int SALTANDO = 1;
   final int ATACANDO = 2;
   final int JODETE = 3;
   final int ATONTADA = 4;
   final int CABREADA = 5;
   //final int MUERTA = 6;
 	int tarenas = 0;
   
   int sx=0;//, sy=0;
	int estado = 0;
   int lado = -1;
   int anim = 0;
   int tipo = 0;
   int contf = 27;
   int timing2 = 0;
   
   unit bola[] ;
   Image nut;
   //Image nut2;
           
   
   
	public leona(Image i, int w, int h, int numf,int g, int j,Image src)
	{	
	   super(i, w, h, numf,g,j);	   
	   setCollisionRectangle(6*2,2*2,12*2,24*2);
      //Activate();
      tag = 70;
      x = 72*2;
      y = 201*2;
      anim=23;
      bola = new unit[4];
      for (int k = 0;k < 4;k++)
      {
	      bola[k] = new unit(i,10,8,5,79,83);	
	      bola[k].setCollisionRectangle(3*2,2*2,4*2,4*2);
	      bola[k].Deactivate();
	   }	 
	   nut = src;	   
	}
            

	
   public void Update(Prota prota)
   {
      sx = 0;      
      if (tag > 0) 
      {
      	if (isCollidingWith(prota)) prota.tag-=10;
      	switch(estado)
      	{
      	  case 0: 
      	  					if (timing > 130-110)
      	  					{
      	  						if (timing%3 == 0)
      	  						{
      	  							if (anim < 7 && anim > 0) anim--;
      	  							if (anim == 19) anim = 6;
      	  							if (anim > 19) anim--;
      	  						}
      	  					}      	  				
      	  					if (timing > 200-110) {timing=0;estado = ATACANDO;anim=0;}
      	  					break; 
           case SALTANDO:            		   
                    if (timing <= 1) 
                    {
                       if (lado == -1) Auto(x,y,-200*2,-320*2);                    	
                       else Auto(x,y,200*2,-400*2);
                    	  anim = 5;
                    }
                    else
                    {
                       int caida = (-320+(6*3*(int)timing))*2;
                       if (caida > 0) anim=4;
                       incy = caida;
                    	  ActAuto(); 
                    	  if (y > 191*2) anim = 3;
                    	  if (y > 201*2) {y = 201*2;anim=0;timing=0;estado=ATACANDO;
                    	  		if (lado == -2) 
                    	  		   {lado = -1;x -= 10*2;if (prota.x > x) estado = JODETE;}
                    	  		else 
                    	  		   {lado = -2;x += 10*2;if (prota.x < x) estado = JODETE;}
                    	  }
                    }
            		  break; 
               case ATACANDO:            		  
                    if (timing >= 18 && timing < 90) 
                    {
                 	  		if (timing == 18)
                    		{
                    			if (tag % 2 == 0)
                 	  			{
                 	  				tipo = 1;	                    
	                    		   bola[0].Activate();
	                    		   bola[0].offx=80*2;
	                    		   if (lado == -2) bola[0].Auto(x+(16*2),y,150*2,0);
	                    		   else bola[0].Auto(x,y,-150*2,0);
	                    		}
	                    		else
	                    		{
	                    			tipo = 2;
	                    			for (int i = 0;i < 4;i++)
                    		   	{
                    		   		int xx, xo;
	                    		   	bola[i].Activate();
	                    		   	bola[i].offx=80*2;
	                    		   	int yy = ((Game.rnd.nextInt()%120)-(Game.rnd.nextInt()%80))*2;
	                    		   	xx= (100 +(Math.abs(Game.rnd.nextInt()%100)))*2;
	                    		   	if (lado == -2)
	                    		   	  xo = x+(16*2);
	                    		   	else
	                    		   	 { xo = x;xx= -xx;}
	                    		   	bola[i].Auto(xo,y+(4*2),xx,yy);
                    		   	}
	                    		}
                    		}	                    		
							  if (tipo == 1)	                    		
							  {
	                    		bola[0].ActAuto();
	                    		bola[0].offx += bola[0].width;                    		
	                    		if (bola[0].offx > 120*2) bola[0].offx=(80+20)*2;
	                    		if (prota.y < bola[0].y) bola[0].incy -= 50*2;
	                    		if (prota.y > bola[0].y) bola[0].incy += 50*2;                    		
	                    		if (bola[0].isCollidingWith(prota) || prota.isCollidingWith(bola[0])) prota.tag-=100;
                    	  }
                    	  else
                    	  {
                    			for (int i = 0;i < 4;i++)	           
                    			{
                    				bola[i].ActAuto();
                    				if (prota.y < bola[i].y) bola[i].incy -= 1*2;
                    				if (prota.y > bola[i].y) bola[i].incy += 1*2;  
                    				if (bola[i].isCollidingWith(prota) || prota.isCollidingWith(bola[i])) prota.tag-=20;
                    				//if (bola[i].isCollidingWith(prota)) prota.tag--;                  		
                    			}
                    	  }	                    	  
                    }
                    else if (timing > 0 && timing%6 == 0)
                    {
                    	  if (anim < 6) anim++;
                    	  else 
                    	  {
                    	  	  for (int i = 0;i < 4;i++) bola[i].Deactivate();	    
                    	  	  if (tag < 25) estado = ATONTADA;
                    	  	  else estado = SALTANDO;
                    	  	  timing=0;
                    	  }
                    }
                    break;
               case JODETE:
                     if (timing <= 1) anim = 14;
               	   else 
               	   {
               	   	if (anim < 14+4) anim++;                    	  
               	   	else {anim = 24;estado = CABREADA;tag += 200;}
                     }
               		break;
               case ATONTADA:
               		if (prota.x > x) {lado = -1;}
               		else {lado = -2;}   
               		if (timing == 1) anim = 14;
               		if (timing == 60) anim = 15;
               		if (timing == 140) anim = 16;
               		if (timing == 146) anim = 17;
               		if (timing == 148) anim = 18;
               		if (timing == 152) {timing = 0;estado = CABREADA;anim = 24;}
               		break;		            		          
               case CABREADA:
               		if (timing % 3 == 0) anim ++;
               		if (anim > 26) anim = 24;
               		if (prota.x > x) {lado = -1;sx=2*2;}
               		else {lado = -2;sx = -2*2;}               		//esta mal pero bueno...
               		break;
               case 6: //MUERTA
               		if (timing % 3 == 0) 
               		{
               			anim ++;               			
               		   if (anim > 28) {anim = 28;sx = 0;tag = 0;Game.Sonido(4,1);}
               		}               		
   						break;
               		
            
     			}     			
     			if (prota.lanza.timing < 25 && prota.lanza.active &&      		
         		isCollidingWith(prota.lanza))
         		{        
         			if (estado != 0 && estado != ATONTADA)
         			{    			
             			tag--;
             			if (estado == CABREADA && tag < 200) estado = 6;
             		}
             		prota.lanza.Deactivate();	       
         		}
     			//}                
      }     
		if (timing % 4 == 0) tarenas = 1-tarenas;      
      x += sx;
	   //y += sy;	      	
	 	timing++;
    
   }
   
   
	public void blit(Graphics gr)
   {
   	
   	for (int i = 0;i < 4;i++)
         bola[i].blit(gr);    
      gr.setClip(0,0,Game.wmap,Game.hmap);   
      ashape=anim;
      int res = 0, r = 0;
      if (ashape < 14)
      {
         gr.clipRect(Game.offsetx+x,Game.offsety+y+(2*2),21*2,29*2);	         
         if (lado == -2) ashape += 7;
      }  
      else 
      {
          gr.clipRect(Game.offsetx+x,Game.offsety+y+(11*2),width,(20+1)*2);	         
          if (lado == -2) ashape += 5;
      }
      if (ashape < 19)      
	      {res = 14;r = 52-63;}	      
	   else if (ashape < 24)      
	      {res = 19;r = 72-63;}	      
	   else if (ashape < 29)     
	      {res = 24;r = 92-63;}	      
	   else {res = 29;r = 92+20-63;}
	   if (ashape >= 14)
	       gr.drawImage(foo,Game.offsetx+x - (ashape-res)*25*2, Game.offsety+y - r*2, gr.TOP|gr.LEFT);	                     
	   else
	   {	
	   	if (lado == -1)   
	   	   gr.drawImage(nut,Game.offsetx+x, (2*2)+Game.offsety+y - ashape*29*2, gr.TOP|gr.LEFT);	                     
	   	else  
	   	   gr.drawImage(nut,Game.offsetx+x-(21*2), (2*2)+Game.offsety+y - (ashape-7)*29*2, gr.TOP|gr.LEFT);	                     	   	
	   }	   
	   if (estado == 6)
	   {
	   	timing2++;     
	   	gr.setClip(0,0,Game.wmap,Game.hmap);  
	   	gr.clipRect(Game.offsetx+(16*2),Game.offsety+(192*2),16*2,(40+1)*2);	         
	   	gr.drawImage(foo,Game.offsetx+(16*2) - (125*2) - (tarenas)*16*2, Game.offsety+(192*2) - 0, gr.TOP|gr.LEFT);	               
	   	gr.setClip(0,0,Game.wmap,Game.hmap);  
	   	gr.clipRect(Game.offsetx+(96*2),Game.offsety+(56*2)+10,(16*2),(40+1)*2);	         
	   	gr.drawImage(foo,Game.offsetx+((96 - 125)*2) - tarenas*16*2, 10+Game.offsety+((56 - 39)*2), gr.TOP|gr.LEFT);	                 
	   	if (timing2 >= contf)	   	
	   	{
	   		timing2 = 0;
	   		contf--;	   		
	   		Game.AddFuego((Game.w/2)+Game.rnd.nextInt()%((Game.w-16)/2),Game.offsety-16);
	   	}
	   	
	   }
	   numframes = estado;	   
   }
	
	
}