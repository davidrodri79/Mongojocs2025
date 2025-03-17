package com.mygdx.mongojocs.numa;

public class BeastCharacter {
	

	public static final int STAND = 0;
	public static final int DEAD = 9999;

	public int cnt, state, x, y, realx, realy, velx, vely, animSpeed, life, maxLife, dir;
	boolean outOfWorld, checkBounds=false;
	public BeastWorld w;
	Square body;

	public BeastCharacter()
	{
		setState(STAND); velx=0; vely=0; outOfWorld=false; body=new Square();
	}
	
	public void setState(int s)
	{
		cnt=0; animSpeed=0; state=s;
	}
	
	public void move(int rx, int ry)
	{		
		if(checkBounds){
			if(rx<0) rx=0; 
			if(rx>(((w.sizeX+2)*16)<<7)) rx=((w.sizeX+2)*16)<<7;
			if(ry>(((w.sizeY+2)*16)<<7)) ry=((w.sizeY+2)*16)<<7;
		}
		realx=rx; realy=ry;
		x=realx>>7; y=realy>>7;
	}
	
	public void walk(int rx, int ry)
	{
		int nx=rx>>7, ny=ry>>7, inc;				
			
	
		if(w.ground(nx,ny-4)) move(rx,w.avgYAt(nx,ny-4));							
		else if(w.ground(nx,ny)) move(rx,w.avgYAt(nx,ny));		
		else if(w.ground(nx,ny+6)) move(rx,w.avgYAt(nx,ny+6));		
		else move(rx,ry);
		
			
	}	
	
	void modifyLife(int xx, int yy, int am)
	{
		if(xx<x) {dir=1; velx+=200;}
		else {dir=0; velx-=200;}
		life+=am; 
		if(life>maxLife) life=maxLife;
		if(life<=0) {life=0; setState(DEAD);}
	}
			
}