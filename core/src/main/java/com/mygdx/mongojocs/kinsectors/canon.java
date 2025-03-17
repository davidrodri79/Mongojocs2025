package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class canon extends unit
{
	
	public canon(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MCANON);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 1100;
		valia=13245;
		x = Game.largo;
		y = 29;
		//timing = Game.rnd.nextInt()%128;   
	}
   
   int db = 0;
	
   
   public void Update(int ax, int by, boolean c)
   {
   	if (tag <= 0) return;
   	timing++;
   	if (x > 103) x--;
   	int angulo = Trig.atan(by-66, ax-154);
   	if (angulo >= 158) ashape = 0;
   	else if (angulo <= 96) ashape = 6;
   	else ashape = 6-((angulo-96)/9);   	
   	if (timing % 140 == 0)
   	{
   		int dx, dy;
   		db = 1-db;
	      dx = Trig.cos(angulo)/3;
	      dy = Trig.sin(angulo)/3;	      
   		Game.malos[32+db].Activate();
   		Game.malos[32+db].Auto(x+25,55,dx,dy);		
   	}
   	if (timing > 305 && timing < 350)
	   {
	   	if (Game.ktype.y+4 > y+31 && Game.ktype.y+4 <= y+31+8 && Game.ktype.inmunidad <= 0) Game.ktype.tag -= 50;
	   }
	   if (timing == 350) timing = 0;
   
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
   
   public void blit(Graphics gr)
   {
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
	}
	
	public void Mata()
	{		
		//Deactivate();
		Game.speed = 2;
		Game.addMsg(Lang.con2[6]);
		Game.puntos += valia; 
	   Game.AddExplosion(x+35, y+35, Game.ESMEKKA, 6, 25);	      	   		 
	   Game.faseacabada = true;
	}
   
	
}