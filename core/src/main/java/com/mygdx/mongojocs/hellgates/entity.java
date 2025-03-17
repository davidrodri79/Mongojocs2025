package com.mygdx.mongojocs.hellgates;
/*===================================================================
  HellGates MIDP2.0 
  -------------------------------------------------------------------
  Programmed by David Rodriguez 2004
  ===================================================================*/
  
public class entity {
		
	level lev;
	int realX, realY, x, y, bkRealX, bkRealY, bkX, bkY;
	rect body;
	boolean visible, collidable;
	
	public int width()
	{
		return 24;	
	}
	
	public int height()
	{
		return 32;
	}
	
	public entity()
	{
		body = new rect(); visible = true; collidable = false;
	}	
		
	public void backUpPos()
	{
		bkRealX = realX; bkRealY = realY; bkX = x; bkY = y;		
	}
	
	public void restorePos()
	{
		realX = bkRealX; realY = bkRealY; x = bkX; y = bkY;
		body.set(x - width()/2, y - height(), width(), height());
	}
	
	public boolean setPos(int aX, int aY)
	{		
		backUpPos();
		
		x = aX; y = aY;
		realX = x << 8; realY = y << 8;
				
		body.set(x - width()/2, y - height(), width(), height());
		
		/*if(lev.rectCollide(body)){ restorePos(); return true;}
		else*/ return false;
	}
	
	boolean setRealPos(int aRealX, int aRealY, int precission)
	{
		if(collidable) backUpPos();
		
		realX = aRealX; realY = aRealY;
		x = realX >> 8; y = realY >> 8;
		
		body.set(x - width()/2, y - height(), width(), height());
		
		if(collidable && lev.rectCollide(body,precission)){ restorePos(); return true;}
		else return false;
	}
	
	boolean setRealPos(int aRealX, int aRealY)
	{
		return setRealPos(aRealX,aRealY,24);		
	}
	
}


