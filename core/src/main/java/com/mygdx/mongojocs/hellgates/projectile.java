package com.mygdx.mongojocs.hellgates;
/*===================================================================
  HellGates MIDP2.0 
  -------------------------------------------------------------------
  Programmed by David Rodriguez 2004
  ===================================================================*/
  
public class projectile extends entity {

	public static final int SLIME_BALL = 0;

	int cnt, dir, type;

	projectile(int aX, int aY, int aType, int aDir)
	{
		setPos(aX,aY);
		type = aType; dir = aDir;			
	}
	
	void update()
	{
		cnt++;
		setRealPos(realX + velX(), realY + velY(),24);
		
		if(cnt>duration()) visible = false;
	}
	
	int duration()
	{
		switch(type)
		{	
			default :
			case SLIME_BALL : return 20;	
		}
	}
	
	int velX()
	{
		switch(type)
		{	
			default :
			case SLIME_BALL : return 1200*dir;	
		}
	}
	
	int velY()
	{
		switch(type)
		{	
			default :
			case SLIME_BALL : return 200*cnt;	
		}
	}
	
	public int width()
	{
		switch(type)
		{	
			default :
			case SLIME_BALL : return 12;	
		}
	}
	
	public int height()
	{
		switch(type)
		{	
			default :
			case SLIME_BALL : return 12;	
		}
	}
	
	int damageAmount()
	{
		switch(type)
		{	
			default :
			case SLIME_BALL : return 10;	
		}
	}
	
}