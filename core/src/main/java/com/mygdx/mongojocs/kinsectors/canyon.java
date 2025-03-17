package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class canyon extends unit
{
	
	public canyon(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MCANYON);	 	
	   
	}

   public void ActivateSub()
   {
		tag = 25;
		valia=53;
		timing = 0;   
	}
		
	public boolean CorrectTile(int mt)
	{
		return (mt >= 0 && mt < Game.llin<<4);	   
	}
   
   public void Update(int ax , int by, boolean c)
   {
   	timing++;   	
   	ActAuto();	
   	if (estado == 2) plus = 8;
   	else plus = 0;
   	if (estado == 0)
   	{
   	
   	int tb = ((Game.offraices+x)>>3) + (((y+8)>>3)*Game.llin);   	
   	if (incy != 0)
   	{
   		int mt = tb + 2;
   		if (CorrectTile(mt) && (Game.map[mt] == 2 || Game.map[mt] == 3))
   		{
   			incy = 0;incx = -25;
   		}
   	}
   	else if (incx > -200)
   	{
   		int mt = tb - Game.llin ;
   		if (CorrectTile(mt) && Game.map[mt] == 18)
   		{
   			incx = -200;incy = -200;
   		}
   		mt = tb + 2*Game.llin ;
   		if (CorrectTile(mt) && Game.map[mt] == 16)
   		{
   			incx = -200;incy = 200;
   		}
   	}
   	}
   	
   	if (tag > 0)
   	{
			//CANYON   	
   		int angulo = Trig.atan(by-y, ax-x);
   		ashape = (((angulo*1000)+5333)/10666)+6;
   		ashape = ashape%24;
   		//DISPARO
   		if (timing%16 == 0 && Game.rnd.nextInt()%100 >= 16) Game.Dispara(5+x+(Trig.cos(angulo)/90),plus+3+y+(Trig.sin(angulo)/90),angulo);
   	}
   	if ((incy > 0 && y > 120) 
   	||(incy < 0 && y < -7)
   	||(estado > 0 && x < -24))
   		Deactivate();
   	//if (incy < 0 && y < -7) Deactivate();
   	//if (estado > 0 && x < -24) Deactivate();
   }
   
   /*
   public void Update()
   {
   	timing++;
   	ActAuto();	   	   
	   y += Trig.sin((int)timing<<2)>>7;
	   if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   ashape = Math.abs(timing%2);
	   if (x < -20) Deactivate();	   
   }
	*/

	int plus = 0;
   	
	public void blit(Graphics gr)
   {
   	if (!active) return;
   	if (estado < 2)
         Game.blit(130,0,26,24,x-6,y,foo);	      
      else      
         Game.blit(130,24,26,24,x-6,y,foo);	         
      //CANYON
      if (tag <= 0) return;
      y+=plus;
      switch(ashape)
      {
      	case 0: Game.blit(10,1,10,19,x+2,y-6,foo);break;
      	case 1: Game.blit(30,1,10,19,x+2,y-6,foo);break;
      	case 2: Game.blit(50,2,14,18,x+2,y-5,foo);break;
      	case 3: Game.blit(70,4,16,16,x+2,y-3,foo);break;
      	case 4: Game.blit(90,7,17,13,x+2,y,foo);break;
      	case 5: Game.blit(110,10,19,10,x+2,y+3,foo);break;
      	case 6: Game.blit(10,60,18,10,x+3,y+3,foo);break;
      	case 7: Game.blit(30,60,19,10,x+3,y+3,foo);break;
      	case 8: Game.blit(50,60,17,13,x+3,y+3,foo);break;
      	case 9: Game.blit(70,60,16,16,x+3,y+3,foo);break;
      	case 10: Game.blit(90,60,14,18,x+3,y+3,foo);break;
      	case 11: Game.blit(110,60,10,19,x+3,y+3,foo);break;      	
      	case 12: Game.blit(10,20,10,19,x+2,y+3,foo);break;      	
      	case 13: Game.blit(30,20,10,19,x+2,y+3,foo);break;
      	case 14: Game.blit(46,20,14,18,x-2,y+3,foo);break;
      	case 15: Game.blit(64,20,16,16,x-4,y+3,foo);break;
      	case 16: Game.blit(83,20,17,13,x-5,y+3,foo);break;
      	case 17: Game.blit(101,20,19,10,x-6,y+3,foo);break;      	
      	case 18: Game.blit(2,50,18,10,x-6,y+3,foo);break;
      	case 19: Game.blit(21,50,19,10,x-7,y+3,foo);break;
      	case 20: Game.blit(43,47,17,13,x-6,y,foo);break;
      	case 21: Game.blit(64,44,16,16,x-4,y-3,foo);break;
      	case 22: Game.blit(86,42,14,18,x-1,y-5,foo);break;
      	case 23: Game.blit(110,41,10,19,x+2,y-6,foo);break;      
      }
      y-=plus;
   }

	public void Mata()
	{
		//Deactivate();
	   Game.puntos += valia; 
	   Game.addMsg(Lang.con2[7]);
	   Game.AddExplosion(x, y, Game.ESMEKKA, 2, 4);	      	   		 
	}
	
	public boolean isCollidingWith(unit choc)
   {
   	if (tag <= 0) return false;
      if (choc.x + choc.cix > x+cix && choc.x + choc.cix < x+cfx && choc.y + choc.ciy > y+ciy && choc.y + choc.ciy < y+cfy) return true;
      if (choc.x + choc.cfx > x+cix && choc.x + choc.cfx < x+cfx && choc.y + choc.ciy > y+ciy && choc.y + choc.ciy < y+cfy) return true;
      if (choc.x + choc.cix > x+cix && choc.x + choc.cix < x+cfx && choc.y + choc.cfy > y+ciy && choc.y + choc.cfy < y+cfy) return true;
      if (choc.x + choc.cfx > x+cix && choc.x + choc.cfx < x+cfx && choc.y + choc.cfy > y+ciy && choc.y + choc.cfy < y+cfy) return true;
      return false;
   }

   
	
}