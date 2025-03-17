package com.mygdx.mongojocs.hellgates;
/*===================================================================
  HellGates MIDP2.0 
  -------------------------------------------------------------------
  Programmed by David Rodriguez 2004
  ===================================================================*/

public class rect {
	
	
	int x1, y1, x2, y2;
	
	public rect()
	{
	}	
			
	public rect(int aX, int aY, int aSizeX, int aSizeY)
	{
		set(aX,aY,aSizeX,aSizeY);
	}
	
	public void set(int aX, int aY, int aSizeX, int aSizeY)
	{
		x1 = aX; y1 = aY; x2 = aX + aSizeX; y2 = aY + aSizeY;
	}
	
	public void unset()
	{
		set(1,1,-1,-1);			
	}
	
	public int centerX()
	{
		return (x1+x2)/2;
	}

	public int centerY()
	{
		return (y1+y2)/2;
	}
	
	public boolean inside(int aX, int aY)
	{
		if(aX>=x1 && aX<=x2 && aY>=y1 && aY<=y2) return true;
		else return false;	
	}
	
	public boolean collide(rect r)
	{
		if(x2>=r.x1 && x1<=r.x2 && y2>=r.y1 && y1<=r.y2)
			return true;
		else return false;	
	}
	
	public void copy(rect r)
	{
		x1 = r.x1;	x2 = r.x2;	y1 = r.y1;	y2 = r.y2;	
	}
		
}

