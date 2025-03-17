package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Image;

class cabezon extends unit
{
	
	public cabezon(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MCABEZON);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 8;
		valia=13;
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
	   //y += Trig.sin((int)timing<<2)>>7;
	   y += Game.rnd.nextInt()%2;
	   if (timing % 12 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   ashape = Math.abs(timing%2);
	   if (x < -30) Deactivate();	   
   }

	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[4]);
	   Game.puntos += valia; 
	 Game.AddExplosion(x, y, Game.ESMEKKA, 2, 4);	      	 
	      	   		 
	}
   
	
}