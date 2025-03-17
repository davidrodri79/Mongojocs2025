package com.mygdx.mongojocs.hellgates;
/*===================================================================
  HellGates MIDP2.0 
  -------------------------------------------------------------------
  Programmed by David Rodriguez 2004
  ===================================================================*/
  
public class character extends entity{

	int cnt, dir, life;
	rect damage;

	public character()
	{
		dir = 1;
		damage = new rect(); damage.unset(); life=maximumLife();
	}
	
	public character(int aX, int aY, level l)
	{
		lev = l; 
		setPos(aX,aY);
	}
		

	void walk(int aRXOffset)
	{
		int newRX = realX + aRXOffset, newRY, newY;
	
		//THIS WORKS!
		/*newRY = (iLev->yAtTile((newRX>>8),iY))<<8;
		
		// Not a too high step
		if(newRY > iRealY - 1024){
		
			// Do Fall
			if(newRY > iRealY + 512)
				newRY = iRealY;
			
			setRealPos(newRX,newRY);
		}*/
		
				
		/*if(w.ground(nx,ny-4)) move(rx,w.avgYAt(nx,ny-4));							
			else if(w.ground(nx,ny)) move(rx,w.avgYAt(nx,ny));		
			else if(w.ground(nx,ny+6)) move(rx,w.avgYAt(nx,ny+6));		
			else move(rx,ry);
		*/
			
		if(lev.tileAt((newRX>>8),y-4) != level.EMPTY_TILE) newRY = (lev.yAtTile((newRX>>8),y-4))<<8;
		else if(lev.tileAt((newRX>>8),y) != level.EMPTY_TILE) newRY = (lev.yAtTile((newRX>>8),y))<<8;
		else if(lev.tileAt((newRX>>8),y+6) != level.EMPTY_TILE) newRY = (lev.yAtTile((newRX>>8),y+6))<<8;
		else newRY = realY;
	
		//if(newRY >= iRealY - 2048){				
					
			if(newRY > realY + 1024) newRY = realY;
					
			setRealPos(newRX,newRY,24);
		//}
			
	}

	boolean atGround()
	{	
		return (lev.yAtTile(x,y) <= y);	
	}
	
	void setDamage(int px, int py, int sx, int sy)
	{
		damage.set(x + dir*px - sx/2 ,y + py -sy/2 ,sx,sy);									
	}
	
	void modifyLife(int d)
	{
		life += d;
		if(life<0) life = 0;
		if(life>maximumLife()) life=maximumLife();
	}
	
	int maximumLife()
	{
		return 33;	
	}
	
	void look(rect r)
	{
		if(r.centerX()<x) dir=-1; else dir=1;
	}
}
