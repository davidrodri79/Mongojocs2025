package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Image;

class rotator extends unit
{
	
	public rotator(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MROTATOR);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 7;
		valia=15;
		//timing = Game.rnd.nextInt()%128;   
	}
   
   public void Update(int a , int b, boolean c)
   {
   	timing++;
   	ActAuto();	   	   
	   //if (y >= b-15 && y <= b+-15 && Game.rnd.nextInt()%16 > 6) 
	   if (tag < 7) {incy = 0;incx-=200;}
	   //if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   ashape = (++ashape)%7;
	   if (x < -20) Deactivate();	   
   }
   
   
   
	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[11]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESIMPLE, 4, 8);
	      	   		 
	}
   
	
}