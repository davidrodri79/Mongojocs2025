package com.mygdx.mongojocs.pingpoyo;

public class Player
{
	public static final int IDLE = 0;	
	public static final int CHARGE = 3;
	public static final int RELEASE = 4;
	
	public int x, cnt, state, power, charged, direction, moveTo, offset;

	public Player()
	{
		x=-128; power=0; charged=0; direction=0; moveTo=-128;
		setState(IDLE);
	}
	
	public void setState(int s)
	{
		state = s; cnt = 0;
	}
	
	public void update(Ball b, int keyX, int lastKeyX, int keyY, int keyMenu, int keyMisc)
	{
		cnt++;
		boolean auto=Game.gameAuto;
		
		if(state == RELEASE && offset<12) offset+=4;
		else if(offset>0) offset-=2;
				
		switch(state){
			
			case IDLE :
			// Manual control
			if(b.velz == 0 || !auto){
				if(keyY == 0){
					if(keyX != lastKeyX)		
						if(keyX > 0 && moveTo<128) moveTo=moveTo+128; 
					if(keyX != lastKeyX)		
						if(keyX < 0 && moveTo>-128) moveTo=moveTo-128; 
				}			
			}			
								
			if(keyY != 0 /*|| keyMenu == 2*/){
				if(keyX == 0) direction = 1;
				else if(keyX < 0) direction = 0;
				else if(keyX > 0) direction = 2;				
				setState(CHARGE);
			}
			break;
						
			case CHARGE :
			if(power<42) power++;						
			if(keyY == 0 /*&& keyMenu != 2*/) {
				charged=power; 				
				setState(RELEASE);
				}
			break;
			
			case RELEASE :
			if(power>0) power-=18;
			else {
				power = 0; 
				setState(IDLE);
			}
			break;
		}	
		
		// Automatic control
		if(auto)
		if(b.velz<0)	
			switch(b.to)
			{
				case 0 : moveTo=-128; break;	
				case 1 : moveTo=0; break;	
				case 2 : moveTo=128; break;	
			}
					
		if(x<moveTo) x+=16;
		if(x>moveTo) x-=16;
	}
}