package com.mygdx.mongojocs.qblast;

public class QBlastLift {

	public int x, y, z, realx, realy, realz, dir;
	QBlastLevel lev;
		
	public QBlastLift(QBlastLevel l, int xx, int yy, int zz)
	{
		lev=l; dir=0; 
		x=xx; y=yy; z=zz;
		realx=(xx<<10)+512; realy=(yy<<10)+512; realz=(zz<<10)+512;		
	}
	
	public void update()
	{
		int SPEED=48;
		
		lev.unsetExists(x,y,z,4);
		
		switch(dir){
			case 0 : if(lev.checkCube(realx>>10,(realy+512+SPEED)>>10,realz>>10)!=17) dir=1; else realy+=SPEED; break;			
			case 1 : if(lev.checkCube(realx>>10,(realy-512+SPEED)>>10,realz>>10)!=17) dir=0; else realy-=SPEED; break;			
		}
		//realy+=16;
						
		// Update integer coordinates
		x=realx>>10; y=realy>>10; z=realz>>10;		
		//System.out.println("("+realx+","+realy+","+realz+")=["+lev.checkCube(realx>>10,(realy-16)>>10,realz>>10)+"]");
		lev.setExists(x,y,z,4);
	}
}