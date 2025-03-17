package com.mygdx.mongojocs.parachutist;

public class Foe
{
		
	int x, y, realx, realy, speed, cnt, state;
	public boolean out = false;
	
	public static final int FREE_FALL = 0;
	public static final int PARACHUTE_TAKE = 1;
	public static final int PARACHUTE_FALL = 2;
	public static final int SAFE = 3;
	public static final int CRUSHED = 4;
	
	public static int GROUNDY;	

	public Foe(int xx, int yy, int sp)
	{
		x=xx; y=yy;	speed=sp;
		realx=x<<8; realy=y<<8;
		setState(FREE_FALL);
	}
	
	public void setState(int s)
	{
		state=s; cnt=0;	
	}
	
	public void update(GameCanvas gc)
	{					
		cnt++;			
						
		switch(state){
			
			case FREE_FALL :
			realy+=speed;
			if(y>GROUNDY) {gc.SoundSET(1,1); gc.VibraSET(500); setState(CRUSHED);};
			break;
			
			case PARACHUTE_TAKE :
			realy+=speed;
			if(y>GROUNDY) setState(CRUSHED);
			if(cnt>=6) setState(PARACHUTE_FALL);
			break;
			
			case PARACHUTE_FALL :
			realx += Trig.sin(cnt*3)>>2;
			realy+=speed/2;
			if(y>GROUNDY) setState(SAFE);
			break;
			
			case SAFE :
			y = GROUNDY;
			if(cnt>=30) out=true;
			break;
			
			case CRUSHED :
			y = GROUNDY;
			if(cnt>=30) out=true;
			break;
				
		}		
				
		x=realx>>8; y=realy>>8;
	}
	
	public boolean collide(Shot s)
	{		
		return (state!=FREE_FALL ? false : Math.abs(s.x-x)<7 && Math.abs(s.y-y)<10);			
	}
}