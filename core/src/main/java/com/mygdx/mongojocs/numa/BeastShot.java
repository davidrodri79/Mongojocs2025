package com.mygdx.mongojocs.numa;

public class BeastShot {
	
	
	public static final int DINO_EAT = 20;
	public static final int SHOT_SPARK = 21;
	public static final int EXPLOSION = 22;
	public static final int TAKE_ITEM = 23;
	public static final int WASTE = 24;
	public static final int EXPLOSION2 = 25;
	

	public int x, y, realx, realy, velx, vely, dir, type, cnt, sizeX, sizeY, damAmount;
	boolean outOfWorld = false;
	Square damage;
	
	int maxCnt;
	
	public BeastShot(int x, int y, int d, int vx, int vy, int t)
	{
		int speed=0;
		
		cnt=0;
		move(x,y);
		type=t; velx=vx; vely=vy;
		dir=d;
		
		switch(type){
			case 0 : speed = 600; damAmount=12; break;
			case 1 : speed = 650; damAmount=23; break;
			case 2 : speed = 700; damAmount=33; break;
			case 3 : speed = 750; damAmount=45; break;
			case 4 : speed = 800; damAmount=60; break;
			
			case 10: speed = 500; damAmount=25; break;
			case 11: speed = 350; damAmount=10; break;
			case 12: speed = 0; damAmount=15; break;
			case 13: speed = 500; damAmount=20; break;
			case 14: speed = 0; damAmount=15; break;
		}
		
		sizeX = 24; sizeY = 17;
		
		if(dir==0)
			velx+=speed;
		else velx-=speed;
		
		damage = new Square();
	}
	
	public BeastShot(int xx, int yy, int t)
	{
		x=xx; y=yy; type=t; cnt=0;
		outOfWorld = false;
		switch(type){
			case DINO_EAT : maxCnt=12; break;
			case SHOT_SPARK : maxCnt=12; break;
			case EXPLOSION : maxCnt=12; break;
			case EXPLOSION2 : maxCnt=18; break;
			case TAKE_ITEM : maxCnt=16; break;
			case WASTE : maxCnt=21; break;
		}
	}
	
	public void update()
	{
		cnt++;
		switch(type)
		{
			case 11 : outOfWorld = outOfWorld || (cnt>=12); break;
			case 14 : outOfWorld = outOfWorld || (cnt>=60); break;
			default : outOfWorld = outOfWorld || (cnt>=30); break;
		}	
		
		
		velx=(velx*99)/100;
		
		move(realx+velx,realy+vely);
		
		switch(type){
			case 10 : damage.set(x-4,y-6,8,8); break;
			case 12 : damage.set(x-4,y+6,8,8); break;
			case 13 : damage.set(x-4,y-10,8,8); break;
			default : damage.set(x-4,y-2,8,8); break;
		}
	}
	
	public void updateEffect()
	{	
		cnt++;
		if(type==TAKE_ITEM) y--;
		if(cnt>=maxCnt) outOfWorld = true;		
	}
	
	public void move(int rx, int ry)
	{
		realx=rx; realy=ry;
		x=realx>>7; y=realy>>7;	
	}
	
	public boolean outOfWorld()
	{
		return outOfWorld;
	}

}