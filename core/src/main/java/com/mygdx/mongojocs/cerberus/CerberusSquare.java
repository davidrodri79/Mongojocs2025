package com.mygdx.mongojocs.cerberus;

public class CerberusSquare{

	public int x1, y1, x2, y2;
	
	public CerberusSquare()
	{
		set(1,1,-2,-2);	
	}
	
	public CerberusSquare(int xb, int yb, int xe, int ye)
	{	
		set(xb,yb,xe,ye);
	}
	
	public void set(int xb, int yb, int sx, int sy)
	{
		x1=xb; y1=yb; x2=xb+sx; y2=yb+sy; 
	}
	public void unset()
	{
		set(1,1,-2,-2);	
	}
	
	public boolean collide(CerberusSquare s)
	{
		if(x2>=s.x1 && x1<=s.x2 && y2>=s.y1 && y1<=s.y2)
			return true;
		else return false;	
	}
	
	public int centerx()
	{
		return (x1+x2)/2;
	}

	public int centery()
	{
		return (y1+y2)/2;
	}
	
	public boolean inside(int x, int y)
	{
		if(x>=x1 && x<=x2 && y>=y1 && y<=y2) return true;
		else return false;	
	}
	
}