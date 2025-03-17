package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Image;

class rovellon extends unit
{
	
	public rovellon(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MROVELLON);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 5;
		valia=8;
		timing = Game.rnd.nextInt()%128;   
	}
   
   public void Update(int a , int b, boolean c)
   {
   	Update();
   }
   
   
   public void Update()
   {
   	timing++;
   	ActAuto();	   	   
   	ashape = Math.abs(timing%5);
   	if (y < 0) Deactivate();	   
   }

	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[2]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESIMPLE, 9, 12);
	      	   		 
	}
   
	
}