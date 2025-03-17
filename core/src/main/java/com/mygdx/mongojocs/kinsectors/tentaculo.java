package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Image;

class tentaculo extends unit
{
	
	public tentaculo(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MTENTACULO);	 		   
	}

   public void ActivateSub()
   {
		tag = 240;
		valia=4622;
		x = Game.largo;
		y = 23;
		setCollisionRectangle(46,51,39,32);
		//timing = Game.rnd.nextInt()%128;   
	}
   
   //int db = 0;
	int numframes = 0;
   
   public void Update(int ax, int by, boolean c)
   {
   	if (tag <= 0) return;
   	timing++;
   	if (estado == 0)
   	{
   		if (x > 90-18-8) x--;
   	}
   	if (estado == 1)
   	{
   		if (x > 75) x--;
   	}
   	if (timing%3 == 0) numframes = (++numframes)%10;
   	if (numframes < 5) desp = numframes;
   	else desp = 4-(numframes-5);
   	desp = desp*96;
   	//ashape = numframes / 5;
   	//desp = (numframes%5)*96;
   	
   	if (numframes == 4)
   	{
   		//DISPARO   	
   		//if (timing % 60 < 30	)
   		//{
   		int angulo = Trig.atan(-28+by-y, -40+ax-x);
   		Game.DisparaR(28+x,40+y,angulo);   	
   		if (timing%3 == 0) 
   		{
   			Game.DisparaR(28+x,40+y,angulo+10);   		
   			Game.DisparaR(28+x,40+y,angulo-10);   				
   		}
   		//}
   	}   	
   }
   
   
   
   //static int fcan = 0;
   /*
   public void Update()
   {
   	timing++;
   	ActAuto();	   	   
	  y += Trig.sin((int)timing<<2)>>7;
	   if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   ashape = Math.abs(timing%2);
	   if (x < -20) Deactivate();	   
   }*/
   
   /*
   public void blit(Graphics gr)
   {
   	Game.blit(0,71,58,56,x+1,y+7,foo);
   	
   	if (tag <= 0)
   	{
   		Game.blit(0,71,58,56,x+1,y+7,foo);
   		return;
   	}
   	//ca�on
   	if (timing < 300 || timing > 350)
   	{
   	   Game.blit(64,128,15,12,x-2,y+29,foo);
   	   if (timing > 250 && timing < 290)
   	      Game.blit(55,128,7,12,x-2-5,y+29,foo);
   	   if (timing > 275 && timing < 300)   	  
   	      Game.blit(48,128,7,12,x-2-5,y+29,foo);       	   
   	}
   	else
   	{
   		if (timing < 305)
   		{
   			Game.blit(64,128,15,12,x+4,y+29,foo);   		
   			Game.blit(48,128,7,12,x+4-5,y+29,foo);       	   
   		}
   		else
   		{
   		   Game.blit(64,128,15,12,x-2,y+29,foo);
   		   Game.blit(0,128,43,12,x-2-41,y+29,foo);
   		   Game.blit(0,128,37,12,x-2-41-37,y+29,foo);
   		   Game.blit(0,128,37,12,x-2-41-37-37,y+29,foo);
   		}
   	}
   	//cuerpo
      Game.blit(0,1,73,70,x,y,foo);
      int canoff = 0, canh = 16, cany = 52;
      switch(ashape)
      {
      	case 0:canoff = 0;canh = 21;cany = 49;break;      	
      	case 1:canoff = 22;canh = 16;cany = 54;break;      	      	
      	case 2:canoff = 39;canh = 16;cany = 55;break;      	
      	case 3:canoff = 56;canh = 16;cany = 56;break;      	
      	case 4:canoff = 73;canh = 16;cany = 57;break;      	
      	case 5:canoff = 90;canh = 16;cany = 57;break;      	
      	case 6:canoff = 107;canh = 21;cany = 58;break;      	
      }
      
      Game.blit(74,canoff,42,canh,x+25,cany,foo);
	}*/
	
	public void Mata()
	{		
		Deactivate();
		Game.addMsg(Lang.con2[13]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x+35, y+35, Game.ESCOMP, 0, 12);	      	   		 
	   Game.AddExplosion(x+45, y+45, Game.ESCOMP, 0, 12);	      	   		 
	   if (estado == 0)
	   {	    
	      Game.malos[2].Activate();
	    	Game.malos[2].estado = 1;
	    	Game.malos[2].y = -16;
	    	Game.malos[2].x = Game.largo-30;
	    	Game.malos[2].numframes = 4;
		}
		else Game.malos[0].numframes = 50;	   		   
	   //Game.faseacabada = true;
	}
   
   /*
   public boolean isC(unit choc)
   {
      if (choc.x + choc.cix > x+cix && choc.x + choc.cix < x+cfx && choc.y + choc.ciy > y+ciy && choc.y + choc.ciy < y+cfy) return true;
      if (choc.x + choc.cfx > x+cix && choc.x + choc.cfx < x+cfx && choc.y + choc.ciy > y+ciy && choc.y + choc.ciy < y+cfy) return true;
      if (choc.x + choc.cix > x+cix && choc.x + choc.cix < x+cfx && choc.y + choc.cfy > y+ciy && choc.y + choc.cfy < y+cfy) return true;
      if (choc.x + choc.cfx > x+cix && choc.x + choc.cfx < x+cfx && choc.y + choc.cfy > y+ciy && choc.y + choc.cfy < y+cfy) return true;
      return false;
   }

   
   public boolean isCollidingWith(unit choc)
   {
   	boolean valor = false;
   	setCollisionRectangle(124,42,38,94);
   	valor = isC(choc) || valor;
   	setCollisionRectangle(99,68,63,69);
   	valor = isC(choc) || valor;
      setCollisionRectangle(52,79,110,58);
   	valor = isC(choc) || valor;
      return valor;
   }*/

	
}