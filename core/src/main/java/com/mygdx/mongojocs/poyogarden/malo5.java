package com.mygdx.mongojocs.poyogarden;

import com.mygdx.mongojocs.midletemu.Image;

class malo5 extends unit
{	 
   
   int sx=0, sy=0;
   int anim = 0;
	
	
	public malo5(Image i, int w, int h, int numf, int g, int j)
	{	
		super(i, w, h, numf,g,j,Game.acor);	  
	   //setCollisionRectangle(0,0,w,h); 		   
		
	}

	public void RandDir(unit src)
	{
		ashape = 0;
		if (src.x > x) {sx = 1;ashape = 2;}
		if (src.x < x) {sx = -1;ashape = 3;}
		if (src.y > y) {sy = 1;ashape = 0;}
		if (src.y < y) {sy = -1;ashape = 1;}
		//timing = 0;
		anim = 0;
		//desp = 0;
		disparando = false;
	}	
	
	boolean disparando = false;
	int fireanim[][] ={{0,0,0,0,0,0,1,1,1,0},
							{0,0,0,0,0,0,1,1,1,0},
							{0,0,0,0,0,0,1,1,1,0},
							{3,3,3,3,3,3,2,2,2,3}		
							};	
	
   public void Update(unit prota)
   {
   	    if (timing == 0) disparando = false;
   	    if (!disparando && muerto == 0) RandDir(prota);	   			
   	    timing++;
   	    if (sx > 0 && Game.inList(Game.tcol,Game.GetTile(x+(width),y+20))) sx = 0;
	   if (sx < 0 && Game.inList(Game.tcol,Game.GetTile(x-3,y+20))) sx = 0;	   
	   if ((sy > 0) && Game.inList(Game.tcol,Game.GetTile(x+(width/2)-5,y+(height)))) sy = 0;
	   if ((sy < 0) && Game.inList(Game.tcol,Game.GetTile(x+(width/2)-5,y+8))) sy = 0;
	   if ((sy > 0) && Game.inList(Game.tcol,Game.GetTile(x+(width/2)+4,y+(height)))) sy = 0;
	   if ((sy < 0) && Game.inList(Game.tcol,Game.GetTile(x+(width/2)+4,y+8))) sy = 0;
	   int tile = Game.GetTile(x+(width/2),y+16);	   
	   if (Game.inList(Game.AgujeroList, tile) && muerto == 0) muerto = 1;
	   
	   if (freeze == 0 && muerto == 0)
	   {	   
	   	    if ((isCollidingWith(prota) || prota.isCollidingWith(this))&& Game.poyo.muerto == 0) Game.poyo.muerto = 1;
	   	    if (disparando)
		    {
		   	    anim++;	   		   	
    		   	if (anim == 10) 
    		   	{
    		   		ashape -= 4;	   		
    		   		anim = 0;
    		   		desp = 0;
    		   		disparando = false;
    		   	}	   	
    		   	else
    		   	{	
    		   			if (Game.nfc%2==0) desp = ++desp%4;   
    		   			x += sx;
    		   			y += sy;
    		   	}
		    }
		    else
		    {
    	   		if (Game.nfc%4==0) desp = ++desp%4;   	
    	   		if (timing%4 == 0) x += sx;
				if (timing%4 == 0) y += sy;
				//if (timing > 30) 
				//timing = 0;
				if ((prota.x == x || prota.y == y) && timing %20 == 0) 	
				{
					disparando = true;
					desp = 0;	
	   			    anim = 0;
    	   			if (prota.x == x)
    	   			{
    	   				sx = 0;
    	   				if (prota.y > y) {ashape = 4;sy = 2;}
    	   				else {ashape = 5;sy = -2;}
    	   			} 
    	   			if (prota.y == y)
    	   			{
    	   				sy = 0;
    	   				if (prota.x > x) {ashape = 6;sx = 2;}
    	   				else {ashape = 7;sx = -2;}
    	   			} 
			    }	   	
   		    }
	    }
		else if (muerto > 0)
		{
			muerto++;
			desp = 0;	
			anim = 0;
			ashape = (++ashape)%4;
			if (muerto == 20) Deactivate();			
			//System.out.println(""+muerto);
		}
		else if (freeze > 0) 
		{
			freeze--;			
		}
		
		if (x < 0) x = 0;
	   if (x > (Game.MAXTX-2)<<3) x = (Game.MAXTX-2)<<3;
	   if (y < -8) y = -8;	   
	   if (y > ((Game.MAXTY-2)<<3)-8) y = ((Game.MAXTY-2)<<3)-8;
	   
		
		//desp = anim;
   }
}