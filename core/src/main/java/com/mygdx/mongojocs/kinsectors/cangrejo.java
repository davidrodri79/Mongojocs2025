package com.mygdx.mongojocs.kinsectors;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class cangrejo extends unit
{

	int p1_cel = 0;
	int p2_cel = 8;
	int p_pata0 = 0;
	
	int seq_cel[] = {0,1,2,3,4,5,6,7,8,7,6,5,4,3,2,1};	
	int seq_pata [][] = {{0,0,0,0,0,0,1,1,1,2,2,2,2,2,2,2,2,2,2,2,1,1},
								{1,0,0,0,0,0,0,1,1,1,2,2,2,2,2,2,2,2,2,2,2,1},
								{3,3,2,2,2,2,2,2,2,2,3,3,3,4,4,4,4,4,4,4,4,4},
								{4,3,3,2,2,2,2,2,2,2,2,3,3,3,4,4,4,4,4,4,4,4},
								{4,4,4,4,4,4,4,5,5,6,6,6,6,6,6,6,6,6,5,5,4,4},
								{6,6,6,6,5,5,4,4,4,4,4,4,4,4,5,5,5,6,6,6,6,6}
								};
	
	
	
	public cangrejo(Image i, int w, int h, int numf, int g, int j)
	{	
	   super(i, w, h, numf,g,j,Game.MCEFALOPODO);	 	   
	}


   public void ActivateSub()
   {
		tag = 85;
		valia = 233;		
		estado = 0;
		Auto(Game.largo+60,80,-134,0);
	}

   
   public void Update(int xx, int yy, boolean b)
   {
   	timing++;
   	//ActAuto();
   	if (estado == 0)
   	{	
   		x-=2;
   		if (p_pata0 >= 5 && p_pata0 <= 10) x--;//Auto(x-1,y,-50,0);
   		if (x < 50)
   		{
   			if (y <= 0 && x > 42 && Game.rnd.nextInt()%128 > 90) estado = 2;
   			else estado = 1;
   		}
   		
   		if (x < -100) Deactivate();
   	}
   	if (estado == 1)
   	{
   		if (y > 0)
   		{
   			x+=2;
   			y-=2;
   		}
   		else estado = 0;	
   	}
   	if (estado == 2)
   	{
   		if (y < 80)
   		{
   			x+=2;
   			y+=2;
   		}
   		else estado = 0;	
   	}
   	
   	/*
   	estado--;
      if (estado < 0) 
      {
      	ActAuto();	   	   
      	if (x < 30) {Auto(x,y,150,-200);estado = 100;}
	  		if (y < 0) {Auto(x,y,50,150);estado = 100;}
	  		if (y > 70) {Auto(x,y,-200,-100);estado = 100;}
		}	*/
		if (seq_cel[p1_cel%16] == 0) Game.DisparaR(x-10,y-5, 128);
		if (seq_cel[p2_cel%16] == 0) Game.DisparaR(x-10,y+35, 128);
	   if (timing %32 == 8) Game.Dispara(x+50,y+32);
	   if (timing %32 == 24) Game.Dispara(x+50,y-20);
   }

	public void blitPatas()
	{
		
		
		switch (seq_pata[0][p_pata0])
		{
			case 0: Game.blit(45,48,10,14,x+22,y-5,foo);break; //0
			case 1: Game.blit(57,46,6,16,x+26,y-7,foo);break; // 1
			case 2: Game.blit(67,45,4,17,x+28,y-8,foo);break; // 2
		}
		switch (seq_pata[1][p_pata0])
		{
			case 0: Game.blit(45+15,48,10,14,x+22,y-5+40,foo);break; //0
			case 1: Game.blit(57+17,46,6,16,x+26,y-7+40,foo);break; // 1
			case 2: Game.blit(67+18,45,4,17,x+28,y-8+40,foo);break; // 2
		}		
		switch (seq_pata[2][p_pata0])
		{
			case 2: Game.blit(67,45,4,17,x+28+7-1,y-8+2,foo);break; 
			case 3: Game.blit(76,45,4,17,x+28+7,y-8+2,foo);break; 
			case 4: Game.blit(84,45,4,17,x+28+7,y-8+2,foo);break; 
		}		
		switch (seq_pata[3][p_pata0])
		{
			case 2: Game.blit(67,45+18,4,17,x+28+7-1,y-8+2+36,foo);break; 
			case 3: Game.blit(76,45+18,4,17,x+28+7,y-8+2+36,foo);break; 
			case 4: Game.blit(84,45+18,4,17,x+28+7,y-8+2+36,foo);break; 
		}		
		switch (seq_pata[4][p_pata0])
		{
			case 4: Game.blit(84,45,4,17,x+28+11,y-8+4,foo);break; 
			case 5: Game.blit(92,46,8,16,x+28+11,y-8+4+1,foo);break; 
			case 6: Game.blit(100,48,12,14,x+28+11,y-8+4+3,foo);break; 			
		}		
		switch (seq_pata[5][p_pata0])
		{
			case 4: Game.blit(84,45+18,4,17,x+28+11,y-8+4+30,foo);break; 
			case 5: Game.blit(92,46+17,8,16,x+28+11,y-8+4+1+30,foo);break; 
			case 6: Game.blit(100,48+15,12,14,x+28+11,y-8+4+3+30,foo);break; 			
		}		
	
	
		p_pata0++;
		if (estado > 0) p_pata0++;
		if (p_pata0 >= seq_pata[0].length) p_pata0 = 0;
	}
	

	public void Mata()
	{
		Deactivate();
		Game.addMsg(Lang.con2[5]);
	   Game.puntos += valia; 
	   Game.AddExplosion(x, y, Game.ESCOMP, 2, 7);	      	   		 
	}

   public void blit(Graphics gr)
   {
   	if (!active) return;
   	if (timing>=64) timing -= 64; 							  	   
		int ang = timing<<3; 		 		
   	for(int j = 7;j >= 0;j--)
 		{ 					 							   							  			  	  
		   int ex = (Math.abs(4-j));					  	   
 		   ex = ex*ex;
 			Game.blit(102,79+(j/2)*9,8,8,(Trig.sin(ang%128)>>6)+x+41-(Math.abs(Trig.sin(ang)*ex>>10))+Math.abs(Trig.cos(ang)/200)*j,y+16+(Trig.sin(ang)>>8)*j,foo); 							  	   
 		}
 		int j = 8;
 		int ex = (Math.abs(4-j));					  	   
 		ex = ex*ex;
 		Game.blit(130,1+(16*(4+(Trig.sin(ang)>>8))),18,16,(Trig.sin(ang%128)>>6)+x+41-(Math.abs(Trig.sin(ang)*ex>>10))+Math.abs(Trig.cos(ang)/200)*j,y+16+(Trig.sin(ang)>>8)*j,foo); 							  	   
 		//gr.setClip(0,0,200,200);
 		//gr.drawString("Score "+(4+(Trig.sin(ang)>>8)) , 4, 4, Graphics.LEFT | Graphics.TOP);			
	     //				     blit(87,0,10,10,malos[i].x+37,malos[i].y+15,TempImg);
 		  //               malos[i].blit(gr);
 		blitPatas();
 		Game.blit(85,2,44,41,x,y,foo);
 		Game.blit(44,3,40,19,x-20+seq_cel[++p1_cel%16],y-7,foo);
		Game.blit(44,24,40,19,x-20+seq_cel[++p2_cel%16],y+30,foo);
   	
   	
   }
   
	

	
}