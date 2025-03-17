package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class gusano extends unit
{
	unit trozo[] = new unit[10];
	int ymuerde;
	int ang = 0;
	int dispang = 33;
	int db = 0;
	
	
	public gusano(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MGUSANO);	 	
	   trozo[0] = new unit(i, 72,36,1,7,4,0);
	   trozo[0].setCollisionRectangle(10,19,61,17);	   
	   trozo[1] = new unit(i, 72,36,1,7,40,0);
	   trozo[1].setCollisionRectangle(10,0,61,17);	   
	   trozo[2] = new unit(i, 44,65,1,80,12,0);
	   trozo[3] = new unit(i, 40,70,1,125,9,0);
	   trozo[4] = new unit(i, 40,70,1,125,9,0);
	   trozo[5] = new unit(i, 40,70,1,125,9,0);
	   trozo[6] = new unit(i, 40,70,1,125,9,0);
	   trozo[7] = new unit(i, 40,65,1,166,11,0);
	   trozo[8] = new unit(i, 40,65,1,166,11,0);
	   trozo[9] = new unit(i, 56,65,1,207,10,0);	   
	 	
	}

   public void ActivateSub()
   {
		tag = 700;
		valia=7899;		
		x = Game.largo;
		y = 36;
		for(int i = 0;i < 10;i++) {trozo[i].Activate();trozo[i].x = Game.largo;}
		trozo[0].tag = 0;
		/*
		trozo[0].x = Game.largo;
		trozo[0].y = 0;
	   trozo[1].x = Game.largo;
	   trozo[1].y = 36;
	   trozo[2].x = trozo[0].x+55;
	   trozo[2].y = 4;
	   trozo[3].x = trozo[2].x+27;
	   trozo[3].y = 2;
	   trozo[4].x = trozo[3].x+25;
	   trozo[4].y = 2;
	   trozo[5].x = trozo[4].x+25;
	   trozo[5].y = 2;
	   trozo[6].x = trozo[5].x+25;
	   trozo[6].y = 2;
	   trozo[7].x = trozo[6].x+23;
	   trozo[7].y = 5;
	 	trozo[8].x = trozo[7].x+23;
	 	trozo[8].y = 5;
	 	trozo[9].x = trozo[8].x+23;  
	 	trozo[9].y = 5;*/
	}

	//int cont = 0;
	   
   public void Update(int ax , int by, boolean c)
   {   	
   	timing++;
   
   	trozo[9].x = trozo[8].x+21;  
		trozo[9].y = y+5+(Trig.sin((timing+42)<<3)>>7);
		trozo[7].x = trozo[6].x+21;
		trozo[7].y = y+5+(Trig.sin((timing+30)<<3)>>7);	 		
		trozo[8].x = trozo[7].x+21;
		trozo[8].y = y+5+(Trig.sin((timing+36)<<3)>>7);
		trozo[5].x = trozo[4].x+21;
		trozo[5].y = y+2+(Trig.sin((timing+18)<<3)>>7);
   	trozo[6].x = trozo[5].x+23;
		trozo[6].y = y+2+(Trig.sin((timing+24)<<3)>>7);
		trozo[3].x = trozo[2].x+24;
		trozo[3].y = y+2+(Trig.sin((timing+6)<<3)>>7);
	   trozo[4].x = trozo[3].x+23;
		trozo[4].y = y+2+(Trig.sin((timing+12)<<3)>>7);
		trozo[0].x = x;
		trozo[0].y = y+(Trig.sin(timing<<3)>>7);
		trozo[1].x = x;
		trozo[1].y = y+36+(Trig.sin(timing<<3)>>7);
		trozo[2].x = trozo[0].x+54;
		trozo[2].y = y+4+(Trig.sin(timing<<3)>>7);
   
   	int minx = Game.largo;
		int vivo = 10;
		for (int i = 0;i< 10;i++) if (trozo[i].active)
		{ 
			minx = Math.min(minx, trozo[i].x);
		   vivo = Math.min(i, vivo);
		}
   		   
   	switch (estado)
   	{
   		case 0:
		   		x-=3;
		   		if (ax < x+20)
		   		{
		   			if (by-36 < y) y--;
		   			if (by-36 > y) y++;
		   		}
		   		else estado = 1;	
		   		trozo[0].y-=10;
	   			trozo[1].y+=10;
   				break;
   		case 1:
	   			x-=4;   
	   			if (x < -1400) {x = Game.largo;estado = 0;numframes--;}
	   			if (numframes <= 0) {estado = 2;timing = 0;}
	   			break;
   		case 2:
		   		if (timing >= 100 && timing < 120)
		   		{
		   			if (timing == 100) trozo[0].numframes = 0;   			
		   			if (timing%2 == 0)trozo[0].numframes++;   			
		   		}
		   		if (timing == 140)
		   				ymuerde = by-26;
		   		if (timing >= 160 && timing <= 170)
		   		{
		   			if (y > ymuerde) y-=2;
		   			if (y < ymuerde) y+=2;
		   			x -= 6;
		   			if (timing == 170) trozo[0].numframes = 0;
		   		}
		   		if (timing > 190 && timing < 220) 
		   		{
		   			x+=3;
		   			/*if (timing%3 == 0)
		   			{
		   			if (y < 40) y++;
		   			if (y > 40) y--;
		   			}*/
		   		}
		   		if (timing == 220) timing = 0;
		   		trozo[0].y -= trozo[0].numframes;
		   		trozo[1].y += trozo[0].numframes;
		   		//if (tag <= 0) {tag+=100;trozo[vivo].Deactivate();}
		   		if (minx > 40) x-=2;	
					break;   	
			case 3:
					if (minx > 84) x-=2;
		   		if (minx < 76) x+=2;
		   		if (timing == 100)	trozo[2].estado = 1;
		   		if (timing >100 && timing < 150)
		   		{   			
		   			if (timing%5 == 0)   		
		   			{
		   				Game.Dispara(minx+8,trozo[2].y+25,dispang);				
		   				dispang += 20-(db*40);
		   			}
		   		}
		   		else 
		   		{		   		
		   			if (vivo > 2 && timing % 4 == 0) Game.DisparaR(trozo[vivo].x+10,trozo[vivo].y+25, 128);  
		   		}
		   		if (timing == 150)	{trozo[2].estado = 0;timing = 0;db = 1-db;}	   		
		   		if (timing < 110) y = (y+(30+(Trig.sin((ang++)<<3)>>5)))>>1;	 		   				
	   			break;
   		case 4:
   				x -= 6;
		   		if (x < -400) {x = Game.largo;y = Math.abs(Game.rnd.nextInt()%78);}	   			
		   		ang = 0;
	   			break;
   		case 5:
   				if (trozo[9].estado == 0)
   				{
   					if (timing > 30) {trozo[9].estado = 1;timing = 0;ang = 0;}
   					if (trozo[9].x < 72+64) x+=6;
   					if (trozo[9].x > 78+64) x-=6;
   					if (y < 30) y+=2;
   					if (y > 30) y-=2;   					
   					break;
   				}
   				else
   				{
   					y = ((30+(Trig.sin((ang)<<2)>>5)));	 		   				
   					trozo[9].x = ((75+(Trig.cos((ang)<<2)>>4)));	
   				}	
   				if (timing < 100 || timing > 200)
   				{
   					ang++;
   					if (timing % 8 == 0) Game.DisparaO(trozo[9].x+35,trozo[9].y+26);				
   				}
   				else if (timing < 200)
   				{
   					
   					if (timing % 6 == 0) 		 
   					{
   						Game.Dispara(trozo[9].x+35,trozo[9].y+26,dispang);				
		   				dispang += 20;  				
		   			}
		   		}
		   		if (timing == 250) timing = 0;
   				break;	
   		
   	}   	
   
	
	   if (estado == 0)
	   {
	   	
	   }
	   
   	//for (int i = 0;i < 10; i++) trozo[i].x--;   	
   	
   }
   
   public void blit(Graphics gr)
   {
   	for (int i = 9;i >= 2; i--) 
   	{
   		if (trozo[i].tag > 0 && timing%3 == 0) {trozo[i].offy+=73;}
   		trozo[i].blit(gr);   	
   		if (i == 2 && trozo[2].active && trozo[2].estado == 1) Game.blit(0, 0, 22, 16, trozo[2].x+3, trozo[2].y+24, foo);
   		if (trozo[i].tag > 0 && timing%3 == 0) {trozo[i].offy-=73;trozo[i].tag--;}
   	}
   	for (int i = 1;i >= 0; i--) 
   		if (trozo[i].tag > 0 && timing%3 == 0) {trozo[i].offy+=73;}
   	
   	if (trozo[0].active) Game.blit(7, trozo[0].offy+22, 19, 14, trozo[0].x, trozo[0].y+22, foo);
   	if (trozo[1].active) Game.blit(7, trozo[1].offy, 19, 14, trozo[1].x, trozo[1].y, foo);
   	int boca = -((trozo[0].y-trozo[2].y)>>3);   	
   	if (trozo[0].active) Game.blit(26, trozo[0].offy+17, 18, 19, trozo[0].x+19, trozo[0].y+17+boca, foo);
		if (trozo[1].active) Game.blit(26, trozo[1].offy, 18, 19, trozo[1].x+19, trozo[1].y-boca, foo);
   	boca = -((trozo[0].y-trozo[2].y)>>3)<<1;   	
   	if (trozo[0].active) Game.blit(44, trozo[0].offy, 35, 36, trozo[0].x+19+18, trozo[0].y+boca, foo);
   	if (trozo[1].active) Game.blit(44, trozo[1].offy, 35, 36, trozo[1].x+19+18, trozo[1].y-boca, foo);
   	for (int i = 1;i >= 0; i--) 
   		if (trozo[i].tag > 0 && timing%3 == 0) {trozo[i].offy-=73;trozo[i].tag--;}
   		         		
   	//gr.setClip(0,0,100,100);
   	//gr.drawString("t "+tag , 4, 4, Graphics.LEFT | Graphics.TOP);				 				   	
   }
   
   
	public void Mata()
	{
		int vivo = 10;
   	for (int i = 0;i< 10;i++) if (trozo[i].active)   	
   		   vivo = Math.min(i, vivo);   	
   	trozo[vivo].Deactivate();
   	trozo[1].Deactivate();
   	if (estado < 3) {estado = 3;timing = 0;}
   	if (vivo == 5) estado = 4;
   	if (vivo == 0) tag+=300;   	
   	else if (vivo < 5) tag+= 125;
   	else tag+=70;
   	if (vivo == 8) {tag+=250;estado = 5;timing = 0;}
   	Game.AddExplosion(trozo[vivo].x+15, trozo[vivo].y+30, Game.ESCOMP, 3, 8);	      	   		 
   	
   	Game.puntos += valia/10; 		  
   	if (vivo == 9)
   	{
   		Game.AddExplosion(trozo[vivo].x+35, trozo[vivo].y+35, Game.ESCOMP, 3, 8);	      	   		 
   		//if (tag <= 0) {tag+=100;trozo[vivo].Deactivate();
   		Game.addMsg(Lang.con2[9]);
			Deactivate();
		   Game.puntos += valia; 		  
		   Game.faseacabada = true; 
	   }
	}
	
	public boolean isCollidingWith(unit obj)
	{
		boolean tocado = false;
		for(int i = 0;i < 10;i++)
		{
			if (trozo[i].active && trozo[i].isCollidingWith(obj))
			{	
					if (i != 2 || (i==2 && trozo[i].estado == 1))
					{
						tocado = true;						
						if (i < 2 || (i >= 2 && !trozo[i-1].active))
					        trozo[i].tag = 3;
					   //tocado = false;     
					}				
					//if (i == 2 && trozo[i].estado == 0) tag+=2;
								
			}
			//else trozo[i].tag = 0;
			//tocado = tocado || trozo[i].isColliding
		}
		return tocado;
	}
   
	
}