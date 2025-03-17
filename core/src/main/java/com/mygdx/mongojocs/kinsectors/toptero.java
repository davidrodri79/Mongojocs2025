package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.kinsectors.unit;
import com.mygdx.mongojocs.midletemu.Image;

class toptero extends unit
{
	
	public toptero(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MTOPTERO);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 13;
		valia=13;
		timing = Game.rnd.nextInt()%128;   
	}
   
   public void Update(int a , int b, boolean c)
   {
   	timing++;
   	ActAuto();	   	   
	   y += Trig.sin((int)timing<<3)>>7;
	   x += Trig.cos((int)timing<<3)>>6;
	   //if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   ashape = Math.abs(timing%4);
	   if (x < -50) Deactivate();	   
   	//Update();
   }
   
   /*
   public void Update()
   {
   	timing++;
   	ActAuto();	   	   
	   y += Trig.sin((int)timing<<3)>>7;
	   x += Trig.cos((int)timing<<3)>>6;
	   //if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   ashape = Math.abs(timing%4);
	   if (x < -50) Deactivate();	   
   }*/

	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[14]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESIMPLE, 8, 11);	      	   		 
	}
   
	
}