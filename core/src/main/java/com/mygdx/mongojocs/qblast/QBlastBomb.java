
package com.mygdx.mongojocs.qblast;

public class QBlastBomb extends QBlastBall {
 	
 	public boolean active;
 	public int counter;
 	
 	public QBlastBomb(QBlastLevel l)
 	{
 		create(l,0,0,0); active=false;
 		type=1;
 	}
 	
 	public void update()
 	{
 		basicUpdate();
 		counter++; 
 		if(counter>=7*7 || lev.checkExists(x,y,z,2)){
 			 counter=7*7;
 			 active=false; 			 
 			 lev.activeBombs--;
 		}
 	}
 	
 	public boolean explodes()
 	{
 		return counter==7*7;	
 	}
 	
 	public void move(int rx, int ry, int rz)
 	{ 		
		basicMove(rx, ry, rz);	
 	}
 	
	public void set(int xx, int yy, int zz)
	{
		if(basicMove(xx,yy,zz)){
			counter=0; active=true;
			velx=0; vely=0; velz=0;
		}		
		lev.activeBombs++;
	}
 	

}