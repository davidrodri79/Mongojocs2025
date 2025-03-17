package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class reina extends unit
{
	
	public reina(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MREINA);	 		   
	}

   public void ActivateSub()
   {
		//tag = 800;
		valia=31232;
		x = Game.largo+18;
		y = 0;
		numframes = 200;
		//setCollisionRectangle(int ox,int oy,int lx,int ly);
		//timing = Game.rnd.nextInt()%128;   
	}
   
   //int db = 0;
	int cony;
   int abeja;
   
   public void Update(int ax, int by, boolean c)
   {
   	if (Game.malos[2].tag > 0 || Game.malos[1].tag > 0) tag = 740;	
   	if (tag <= 0) return;
   	timing++;
   	if (x > 82) x--;
   	if (Game.malos[1].tag <= 0 && x > 14) x--;
   	if (timing %numframes < 8*5)
   	   cony = (timing/8)%5;
   	   //&& Game.rnd.nextInt()%128 > 63   	
   	if (Game.malos[1].tag <= 0)   
   	{
	   	if (timing%numframes >= 35 && timing%numframes <= 40)
	   	{
	   	   cony = 6;   
	   	   if (timing%numframes == 39) 
	   	   {
	   	   	Game.malos[3+abeja].Activate();	
	   	   	abeja = (abeja+1)%5;
	   	   }
	   	}
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
	   Game.puntos += valia; 
	   Game.AddExplosion(x+35, y+65, Game.ESIMPLE, 0, 12);	      	   		 
	   Game.AddExplosion(x+85, y+95, Game.ESIMPLE, 0, 12);	      	   		 
	   Game.AddExplosion(x+65, y+75, Game.ESIMPLE, 0, 12);	      	   		 
	   Game.AddExplosion(x+80, y+45, Game.ESIMPLE, 0, 12);	      	   		 
	   for (int i = 3;i<8;i++)
	      Game.malos[i].Mata();
	   Game.faseacabada = true;
	   Game.addMsg(Lang.con2[10]);
	}
   
   
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
   }

	
	public void blit(Graphics gr)
   {
      if (!visible) return;
      gr.setClip(0, 0,Game.largo, Game.alto); 
	   gr.clipRect(x,Game.offsety+y,width,height);	         
	   gr.drawImage(foo,-desp+x-offx, Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	         
	   if (timing %numframes < 8*5)
   	{
	   	if (cony > 0 && timing%8 < 4)
	   	{
	   		int conyshape = ((cony-1) / 3)*45;
	   		int conydesp = ((cony-1)%3)*47;
	   		Game.blit(conydesp, conyshape, 47, 45, x+115, y+95, Game.imalos);
	   	}
	   	int conyshape = (cony / 3)*45;
   		int conydesp = (cony%3)*47;   	
	   	Game.blit(conydesp, conyshape, 47, 45, x+115, y+95, Game.imalos);
	   	
	   }
   	
   }

	
	
}