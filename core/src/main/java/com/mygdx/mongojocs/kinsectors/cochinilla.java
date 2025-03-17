package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Image;

class cochinilla extends unit
{
	
	public cochinilla(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MCOCHINILLA);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 30;
		valia=68;
		timing = Game.rnd.nextInt()%128;   
		x = Game.largo;
		if (numframes == -1) y = 110-height;
		else y = 18;
	}
   
   public void Update(int a , int b, boolean c)
   {
   	Update();
   }
   
   
   public void Update()
   {
   	timing++;
   	if (estado == 0)
   	{   		
   		if (x > 0) x-=8;
   		else 
   		{
   			estado = 1;
   			int angulo = (Game.rnd.nextInt()%24);
   			if (numframes == -1) angulo += 224;
   			else angulo += 32;
   			Auto(0,y,Trig.cos(angulo)>>1, Trig.sin(angulo)>>1);
   			//numframes == 1;
   		}
   	}
   	if (estado == 1)
   	{
   		ActAuto();
   		if (y < 18)
   		{
   			y = 18;
   			incy = -incy;
   			//incx = -incx;
   		}
   		if (y > 110-height)
   		{
   			y = 110-height;
   			incy = -incy;
   			//incx = -incx;
   		}
   		if (x < 0 && timing < 300)
   		{
   			x = 0;
   			//incy = -incy;
   			incx = -incx;
   		}
   		if (x > Game.largo-width)
   		{
   			x = Game.largo-width;
   			//incy = -incy;
   			incx = -incx;
   		}
   		if (timing > 300) incx--;
   		if (x < -16) Deactivate();
   	}
   	//ActAuto();	   	   
	   //y += Trig.sin((int)timing<<2)>>7;
	   //if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   ashape = Math.abs(timing%3);
	   if (x < -20) Deactivate();	   
   }

	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[8]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESIMPLE, 8, 10);
	      	   		 
	}
   
	
}