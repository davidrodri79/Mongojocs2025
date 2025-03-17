package com.mygdx.mongojocs.kinsectors;


import com.mygdx.mongojocs.midletemu.Image;

class abejorro extends unit
{
	
	public abejorro(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MABEJORRO);	 		   
	}
	
   public void ActivateSub()
   {
		tag = 25;
		valia=77;
		timing = Game.rnd.nextInt()%64;   
		x = 120;
		y = 115;
		incx = -2;
	}
   
   public void Update(int a , int by, boolean c)
   {
   	timing++;
   	x+=incx;
   	if (timing  > 800) Mata();	  
   	y += Trig.sin((int)timing<<3)>>9;
	   x += Trig.cos((int)timing<<3)>>9; 
	   if (by < y && timing%2 == 0) y--;
	   if (by > y && timing%2 == 0) y++;
	   ashape = Math.abs(timing%2);
   	if (incx > 0) ashape += 4;	   
	   if (timing % 80 == 0)
	   {
	   	incx = -incx;
	   	ashape = 3;
	   }
   }
   
	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[3]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESIMPLE, 4, 8);	      	   		 
	}   
	
}