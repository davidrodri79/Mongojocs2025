package com.mygdx.mongojocs.poyogarden;

import com.mygdx.mongojocs.midletemu.Image;

class malo2 extends unit
{
	
  
   
   int sx=0, sy=0;
   int anim = 0;
	
	
	public malo2(Image i, int w, int h, int numf, int g, int j)
	{	
		super(i, w, h, numf,g,j,Game.acor);	  
	   //setCollisionRectangle(0,0,w,h); 	
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
	
	int lista[] = {14,46,78,110,142,174,206,238};
	
   public void Update(unit prota)
   {
   	timing++;
   	if (sx > 0 && Game.inList(Game.tbcol,Game.GetTile(x+(width),y+20))) sx = 0;
	   if (sx < 0 && Game.inList(Game.tbcol,Game.GetTile(x-3,y+20))) sx = 0;	   
	   if ((sy > 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)-5,y+(height)))) sy = 0;
	   if ((sy < 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)-5,y+8))) sy = 0;
	   if ((sy > 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)+4,y+(height)))) sy = 0;
	   if ((sy < 0) && Game.inList(Game.tbcol,Game.GetTile(x+(width/2)+4,y+8))) sy = 0;
	   int tilel = Game.GetTile(x+(width/2),y+16);	   
	   if (Game.inList(Game.AgujeroList, tilel) && muerto == 0) muerto = 1;
	   
	   if (freeze == 0 && muerto == 0)
	   {	  
	   	int tile = Game.GetTile(x+(width/2),y+16);	   	   
	   	if (Game.inList(lista,tile)) 
	   	{
	   		tile+=32;
	   		if (tile > 256) tile = 2;
	   		Game.SetTile(x+(width/2),y+16,tile); 
	   	}
	   	if (isCollidingWith(prota) && Game.poyo.muerto == 0) Game.poyo.muerto = 1;
	   	if (Game.nfc%2==0) anim = ++anim%4;   	
	   	if (timing%3 == 0)
	   	{
	   		x += sx;
				y += sy;
			}
			if (timing > 5) RandDir();
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