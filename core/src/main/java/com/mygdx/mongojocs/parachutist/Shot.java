package com.mygdx.mongojocs.parachutist;

public class Shot
{
	int x, y, realx, realy, velx, vely, cnt;
	boolean out;

	public Shot(int xx, int yy, int angle)
	{
		x = xx; y = yy;
		velx = Trig.cos(angle); vely = -Trig.sin(angle);
		realx = x<<8; realy = y<<8;
		out = false;
		cnt = 0;
	}
	
	public void update()
	{
		cnt++;
		
		if(out) return;
		realx += velx; realy += vely;
		x = realx>>8; y = realy>>8;
				
	}

}