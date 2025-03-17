package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.kinsectors.unit;
import com.mygdx.mongojocs.midletemu.Image;

class basico extends unit
{
	
	public basico(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MBASICO);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 9;
		valia=9;
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
	   y += Trig.sin((int)timing<<2)>>7;
	   if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) 
	   {
	   	if (x > 50) Game.Dispara(x,y);
	   	else Game.DisparaO(x,y);
	   }
	   ashape = Math.abs(timing%2);
	   if (x < -20) Deactivate();	   
   }

	public void Mata()
	{
		Game.addMsg(Lang.con2[0]);
		Deactivate();
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESIMPLE, 4, 8);
	      	   		 
	}
   
	
}