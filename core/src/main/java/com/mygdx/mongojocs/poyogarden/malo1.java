package com.mygdx.mongojocs.poyogarden;

import com.mygdx.mongojocs.midletemu.Image;

class malo1 extends unit
{
	    
   int sx=0, sy=0;
   int anim = 0;
	
	
	public malo1(Image i, int w, int h, int numf, int g, int j)
	{	
		super(i, w, h, numf,g,j, Game.acor);	  	
	    RandDir();			
	}


	public void RandDir()
	{
		int i = Math.abs(Game.rnd.nextInt()%4);
		if (i == 0) {sx = 1;sy = 0;ashape = 2;}
		else if (i == 1) {sx = -1;sy = 0;ashape = 3;}
		else if (i == 2) {sy = 1;sx = 0;ashape = 0;}
		else if (i == 3) {sy = -1;sx = 0;ashape = 1;}
		timing = 0;
	}	

	
   public void Update(unit prota)
   {
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
    	   	if (Game.nfc%4==0) anim = ++anim%4;   	
    	   	if (timing%2 == 0)
    	   	{
	   		x += sx;
				y += sy;
			}
			if (timing > 60) RandDir();
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
		desp = anim;
   }
}