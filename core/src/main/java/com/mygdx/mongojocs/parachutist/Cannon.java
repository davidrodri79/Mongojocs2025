package com.mygdx.mongojocs.parachutist;

public class Cannon
{
	int x, y, angle, shotTime;

	public Cannon(int xx, int yy)
	{
		x=xx; y=yy; angle=64;
		shotTime = 0;
	}

	public void update(int keyX)
	{
		if(shotTime>0) shotTime--;
		if(keyX<0 && angle<128) angle+=3;
		if(keyX>0 && angle>0) angle-=3;
	
	}
}