package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Image;

class medusa extends unit
{
	
	public medusa(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MMEDUSA);	 	
	   setCollisionRectangle(0,4,w,11);
	}

   public void ActivateSub()
   {
		tag = 10;
		valia=14;
		timing = Game.rnd.nextInt()%8;   
	}
   
   public void Update(int a , int b, boolean c)
   {
   	Update();
   }
   
   public void Update()
   {
   	timing++;
   	int delta = 0;
   	if (numframes == 0 || timing < 32)
   	{
   		delta = Math.abs(Trig.sin((int)timing<<2)>>8);
   		x -= delta;   	
   		ashape = Math.abs(3-delta);
   	}
   	else
   	{
   		if (timing == 32) Auto(x,y,-Math.abs(incy),incy);
   		if (timing < 50) ActAuto();
   		else 
   		{
   			if (timing == 50) {ashape = 2;x-=2;}
   			if (timing == 51) {ashape = 1;x-=4;}
   			if (timing > 51) {ashape = 0;x-=8;}   			
   		}
   		//delta = Math.abs(Trig.sin((int)timing<<2)>>8);
   		//ActAuto();
   	}
	   
	   if (ashape > 3) ashape = 3;
	   if (x < -20) Deactivate();	   
   }

	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[1]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESIMPLE, 0, 5);	      	   		 
	}
   
	
}