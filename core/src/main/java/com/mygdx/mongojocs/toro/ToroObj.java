package com.mygdx.mongojocs.toro;

public class ToroObj
{
	GameCanvas	GC;
	
	int	posX=200, posY=70;
	int	velX, velY;
	int	dir, ang;
	
	int	angPow;
	
	int	tipusToro;


	byte toroXVelTab [] = { -3,-4,-3,-2,-1, 0, 1, 2, 3, 4, 3, 2, 1, 0,-1,-2 };
	byte toroYVelTab [] = { -1, 0, 1, 2, 3, 4, 3, 2, 1, 0,-1,-2,-3,-4,-3,-2 };
		
	ToroObj (GameCanvas g)
	{
		GC = g;
	}
	
	void update ()
	{
		int	vx = GC.protaXPos+8 - posX-16;
		int	vy = GC.protaYPos+10 - posY-16;
		
		int	avx = vx<0?-vx:vx;
		int	avy = vy<0?-vy:vy;
		
		int	adst;
		
		if (avx > avy)
		{
			if (vx > 0)	adst = 4<<1;
			else 		adst = 0<<1;
		}
		else
		{
			if (vy > 0)	adst = 2<<1;
			else		adst = 6<<1;
		}
		
		if (adst > ang)
		{
			if (adst - ang <  16 - adst + ang)	angPow ++;
			else					angPow --;
		}
		else if (adst < ang)
		{
			if (ang - adst < 16 - ang + adst)	angPow --;
			else					angPow ++;
		}
		
		if (angPow > 1)
		{
			ang ++;
			angPow = 0;
		}
		else if (angPow <-1)
		{
			ang --;
			angPow = 0;
		}
		
		if ((tipusToro & 1)!=0)
		{
			// evitar q xoquin
			for (int k = 0; k < GC.numToros; k++)
			{
				if (GC.tabToros [k] == this)	continue;
				// esta proxim?
				int	dtx = posX - GC.tabToros [k].posX;
				int	dty = posY - GC.tabToros [k].posY;
				
				int	dprev = dtx*dtx + dty*dty;
				
				if (dprev < 26*26)
				{
					// s'apropen?
					dtx = posX + velX - GC.tabToros [k].posX - GC.tabToros [k].velX;
					dty = posY + velY - GC.tabToros [k].posY - GC.tabToros [k].velY;
					
					int dpost = dtx*dtx + dty*dty;
					
					if (dpost < dprev)
					{
						dtx = posX + toroXVelTab [(ang>=15?0:ang+1)>>1] - GC.tabToros [k].posX - GC.tabToros [k].velX;
						dty = posY + toroYVelTab [(ang>=15?0:ang+1)>>1] - GC.tabToros [k].posY - GC.tabToros [k].velY;
						
						int 	danginc = dtx*dtx + dty*dty;
						
						dtx = posX + toroXVelTab [(ang<=0?15:ang-1)>>1] - GC.tabToros [k].posX - GC.tabToros [k].velX;
						dty = posY + toroYVelTab [(ang<=0?15:ang-1)>>1] - GC.tabToros [k].posY - GC.tabToros [k].velY;
						
						int	dangdec = dtx*dtx + dty*dty;
						
						if (danginc > dangdec)
						{
							angPow +=2;
						}
						else if (danginc < dangdec)
						{
							angPow -=2;
						}
					}
				}			
			}
		}
		
		
		
		if (ang < 0)		ang = 15;
		else if (ang > 15)	ang = 0;
		
		dir = ang>>1;
	
		velX = toroXVelTab [ang];
		velY = toroYVelTab [ang];

		if ((tipusToro & 8)!=0)
		{
			if (true) //GC.bitSwitch==0)
			{
				int vvx = velX;
				int vvy = velY;

				if (vvx > 0)	vvx--;
				if (vvx < 0)	vvx++;
				if (vvy > 0)	vvy--;
				if (vvy < 0)	vvy++;
				
				posX += vvx>>1;
				posY += vvy>>1;
			}
		}
		
		
		if ((tipusToro & 2)!=0)
		{
			if (GC.bitSwitch==0)
			{
				if (velX > 0)	velX--;
				if (velX < 0)	velX++;
				if (velY > 0)	velY--;
				if (velY < 0)	velY++;
			}
		}
		
		if ((tipusToro & 4)!=0)
		{
			if (GC.bitSwitch!=0)
			{
				if (velX > 0)	velX--;
				if (velX < 0)	velX++;
				if (velY > 0)	velY--;
				if (velY < 0)	velY++;
			}
		}
				
		posX += velX;
		posY += velY;

		int	XDif = 16+posX - 191; //200;
		int	YDif = 16+posY - 131; //138;
		int	Dif2 = XDif*XDif + 4*YDif*YDif;

		if (Dif2 > 145*155+70*70)
		{
			int	nXDif = 16+posX-velX - 191; //200;
			int	nYDif = 16+posY - 131; //138;
			
			if (Dif2 > nXDif*nXDif + 4*nYDif*nYDif)
			{
				posX -= velX;
			}

			nXDif = 16+posX - 191; //200;
			nYDif = 16+posY-velY - 131; //138;

			if (Dif2 > nXDif*nXDif + 4*nYDif*nYDif)
			{
				posY -= velY;
			}
		}
	}
	
}