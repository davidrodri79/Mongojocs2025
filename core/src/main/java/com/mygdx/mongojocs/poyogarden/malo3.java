package com.mygdx.mongojocs.poyogarden;

import com.mygdx.mongojocs.midletemu.Image;

class malo3 extends unit
{	 
   
   int sx=0, sy=0;
   int anim = 0;
	
	
	public malo3(Image i, int w, int h, int numf, int g, int j)
	{	
		super(i, w, h, numf,g,j,Game.acor);	  
	   //setCollisionRectangle(0,0,w,h); 		   
		
	}

	public void RandDir()
	{
		ashape = 0;
		int i = Math.abs(Game.rnd.nextInt()%4);
		if (i == 0) {sx = 1;sy = 0;ashape = 2;}
		else if (i == 1) {sx = -1;sy = 0;ashape = 3;}
		else if (i == 2) {sy = 1;sx = 0;ashape = 0;}
		else if (i == 3) {sy = -1;sx = 0;ashape = 1;}
		timing = 0;
		anim = 0;
		desp = 0;
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
   	if (timing == 0) RandDir();	   			
   	timing++;
   	if (sx > 0 && Game.inList(Game.tbcol,Game.GetTile(x+(width),y+20))) sx = 0;
	   if (sx < 0 && Game.inList(Game.tbcol,Game.GetTile(x-3,y+20))) sx = 0;	   
	   if ((sy > 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)-5,y+(height)))) sy = 0;
	   if ((sy < 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)-5,y+8))) sy = 0;
	   if ((sy > 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)+4,y+(height)))) sy = 0;
	   if ((sy < 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)+4,y+8))) sy = 0;
	   int tile = Game.GetTile(x+(width/2),y+16);	   
	   if (Game.inList(Game.AgujeroList, tile) && muerto == 0) muerto = 1;
	   
	   if (freeze == 0 && muerto == 0)
	   {	   
	   	if (isCollidingWith(prota) && Game.poyo.muerto == 0) Game.poyo.muerto = 1;
	   	if (disparando)
		   {
		   	anim++;	   
		   	if (anim == 6) 
		   	{	
		   		switch (ashape-4)
		   		{
		   			case 0: 
		   						Game.AddBala(x,y,0,200,ashape-4);		   						
		   						break;
		   			case 1: 
		   						Game.AddBala(x,y+10,0,-200,ashape-4);
		   						
		   						break;
		   			case 2: 
		   						Game.AddBala(x-4,y+1,200,0,ashape-4);
		   						
		   						break;
		   			case 3: 
		   		 	  			Game.AddBala(x+4,y+1,-200,0,ashape-4);
		   		 	  			
		   						break;
		   		}
		   	}
		   	if (anim == 10) 
		   	{
		   		ashape -= 4;	   		
		   		anim = 0;
		   		desp = 0;
		   		disparando = false;
		   	}	   	
		   	else
		   	{	
		   		desp = fireanim[ashape-4][anim];	
		   	}
		   }
		   else
		   {
	   		if (Game.nfc%4==0) desp = ++desp%4;   	
	   		if (timing%2 == 0)
	   		{
	   			x += sx;
					y += sy;
				}
				if (timing > 60) timing = 0;
				if (timing == 30  && (Math.abs(x - prota.x) < 64)  && (Math.abs(y - prota.y) < 64))	
				{
					disparando = true;
					desp = 0;	
	   			ashape += 4;   	
	   			anim = 0;
				}
	   	
			}
		}
		else if (muerto> 0)
		{
			muerto++;
			ashape = (++ashape)%4;
			if (muerto == 20) Deactivate();			
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