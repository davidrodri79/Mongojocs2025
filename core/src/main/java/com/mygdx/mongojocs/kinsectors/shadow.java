package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Image;

class shadow extends unit
{
	
	public shadow(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MSHADOW);	 		   
	}

	//boolean suicida;

   public void ActivateSub()
   {
		tag = 52;
		valia=111;
		//timing = Game.rnd.nextInt()%128;   
		timing = 0;
		if (Game.rnd.nextInt()%128 > 100) estado = 3;
		//estado = 3;
		//else suicida = false;   
		//suicida = true;
	}
   
   public void Update(int a , int b, boolean c)
   {
   	if (estado == 3)
   	{
   		ActAuto();
   		if (a < x) incx-=5;
   		if (a > x) incx+=5;
   		if (b < y) incy-=5;
   		if (b > y) incy+=5;
   		if (timing > 100) incx-=30;
   	}
   	Update();
   }
   
   
   public void Update()
   {
   	timing++;
   	if (estado == 0)
   	{   		   		
   		if (x > 0) ActAuto();
   		else 
   		{
   			estado = 1;
   			int angulo = (Game.rnd.nextInt()%48);
   			angulo += 64;
   			Auto(0,y,(Trig.cos(angulo)), (Trig.sin(angulo)));   			
   		}
   	}
   	if (estado == 1)
   	{
   		ActAuto();
   		if (y < 18)
   		{
   			y = 18;
   			incy = -incy;   			
   		}
   		if (y > 110-height)
   		{
   			y = 110-height;
   			incy = -incy;   			
   		}
   		if (x < 0 && timing < 100)
   		{
   			x = 0;   			
   			incx = -incx;
   		}
   		if (x > Game.largo-width)
   		{
   			x = Game.largo-width;
   			incx = -incx;
   		}
   		if (timing > 150) incx--;
   		if (x < -16) Deactivate();
   	}
   	 if (x < -20 && timing >= 100) Deactivate();	   
   }

	public void Mata()
	{
		Game.addMsg(Lang.con2[12]);
		Deactivate();
	   Game.puntos += valia; 
	   Game.AddExplosion(x+8, y+8, Game.ESMEKKA, 1, 2);
	      	   		 
	}
   
	
}