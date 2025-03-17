package com.mygdx.mongojocs.sliddisk;


public class Pad {
	
	public static final int PLENGTH = 16;
	public static final int SPEED = 4;
	public static final int MAXPOWER = 30;
	
	public int x, y, power, score, shouldDo, lastShould;
	public boolean behave;
	Table tab;

	public Pad(Table t, boolean up)
	{
		tab = t;
		x = t.width/2; y = (up ? 10 : t.height-10);
		score = 0;
		behave = true;
	}

	public void update(int yAxis, boolean butt)
	{
		if(butt)
		{
			if(power<MAXPOWER) power++;	
		}else		
		switch(yAxis){
			
			case -1 : if(x > tab.left+(PLENGTH/2)) x-=SPEED; break;
			case 1 : if(x < tab.right-(PLENGTH/2)) x+=SPEED; break;
		}
	}
	
	public void IAupdate(Ball b, int level)
	{
		
		if(Math.abs(b.y-y)>(tab.height*3)/4) shouldDo = 2;
		else if(b.x<x) shouldDo = -1;
		else if(b.x>x) shouldDo = 1;
		else shouldDo = 0;
					
		if(lastShould!=shouldDo) behave=false;	
				
		if(behave){
			switch(shouldDo){
				case 2 : update(0,true); break;	
				case -1: update(-1,false); break;
				case 1 : update(1,false); break;
			}						
		}else{
			if(level>11) behave = true;
			else if(Game.RND(10-level)==0) behave = true;
				
		}
		
		lastShould = shouldDo;
	}
}