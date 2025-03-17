package com.mygdx.mongojocs.qblast;

public class QBlastBall {

	public int x, y, z, realx, realy, realz, velx, vely, velz, underCube, type;
	public QBlastLevel lev;
	public boolean falling;
	
	public static int MAX_SPEED = 300;

	public QBlastBall()
	{		
	}

	public void create(QBlastLevel l, int xx, int yy, int zz)
	{
		x=xx; y=yy; z=zz;
		realx=x<<10; realy=y<<10; realz=z<<10;
		realx+=512; realy+=512; realz+=512; 
		
		velx=0; vely=0; velz=0;
		lev=l;
	
	}
	
	public void copy(QBlastBall b)
	{
		x=b.x; y=b.y; z=b.z;
		realx=b.realx; realy=b.realy; realz=b.realz;
				
		velx=b.velx; vely=b.vely; velz=b.velz;
		lev=b.lev;
	}
	
	public void update()
	{
		basicUpdate();
	}
	
	public void basicUpdate()
	{
		int yy, c, FRICTION=8;
											
		falling=false;
		
		/*c=lev.checkCube(x,y,z);
		if (!(c==0 || (c>=2 && c<=5) || c>=7)) {	
		
			basicMove(realx,realy-128,realz);						
		}*/		
													
		yy=lev.nearestGround(realx,realy,realz);
		underCube=lev.nearestCube(realx,realy,realz);
		
		// Falling and ground position
		if(yy>realy-128) { velx=-velx/2; vely=-vely/2; velz=-velz/2; }
		else if(yy<realy-512) {vely-=48; falling=true;}
		else {vely=0; realy=yy+512;}
		
		//if(yy<realy) falling=true;
		
		//Gravity at slopes
		switch(underCube)
		{
			case 2 : velz+=28; break;
			case 3 : velx-=28; break;
			case 4 : velz-=28; break;
			case 5 : velx+=28; break;
		}
		
		// Friction
		if(velx>0) if(velx>FRICTION) velx-=FRICTION; else velx=0; if(velx<0) if(velx<-FRICTION) velx+=FRICTION; else velx=0;
		if(velz>0) if(velz>FRICTION) velz-=FRICTION; else velz=0; if(velz<0) if(velz<-FRICTION) velz+=FRICTION; else velz=0;
		
		//Spring
		if(underCube==18 && (lev.cnt%20)<=4) vely+=275;
		
		if(velx>MAX_SPEED) velx=MAX_SPEED;	if(velx<=-MAX_SPEED) velx=-MAX_SPEED;	
		if(velz>MAX_SPEED) velz=MAX_SPEED;	if(velz<=-MAX_SPEED) velz=-MAX_SPEED;	
												
		move(realx+velx, realy+vely, realz+velz);
				
		//System.out.println("My Y:"+realy+", nearest Y:"+yy);
				
	}
	
	public void move(int rx, int ry, int rz)
	{
		basicMove(rx,ry,rz);	
	}
	
	public boolean basicMove(int rx, int ry, int rz)
	{
		int nx=rx, ny=ry, nz=rz, c;
		boolean r;
		
		lev.unsetExists(x,y,z,type);
		
		if(velx>0) nx+=256; if(velx<0) nx-=256;
		if(vely>0) ny+=256; if(vely<0) ny-=256;
		if(velz>0) nz+=256; if(velz<0) nz-=256;
		
		//System.out.println("Im in:"+lev.checkCube(realx>>10,realy>>10,realz>>10)+" trying to go:"+lev.checkCube(nx>>10,ny>>10,nz>>10));
		c=lev.checkCube(nx>>10,ny>>10,nz>>10);
		if(c==0 || (c>=2 && c<=5) || c>=7){			
			realx=rx; realy=ry; realz=rz;			
			r = true;
		}else{
			/*if(velx>=velz && velx>=vely) velx=-velx/2;
			else if(vely>=velz && vely>=velx) vely=-vely/2;
			else if(velz>=velx && velz>=vely) velz=-velz/2;*/
			velx=-velx/2; vely=-vely/2; velz=-velz/2;
			r = false;
			if(type==0) {
				QBlastMain.gameCanvas.vibrate(20,10);
				QBlastCanvas.playSound(2);
			}
		}
		
		// Update integer coordinates
		x=realx>>10; y=realy>>10; z=realz>>10;		
		
		lev.setExists(x,y,z,type);
		
		return r;
	}
	
	public void explosionInteract(QBlastExplosion ex[])
	{
		int boost=300; 
		if(lev.checkExists(x,y,z,2))		
			for(int i=0; i<ex.length; i++)
				if(ex[i].counter==1)
				if(ex[i].active){
				//System.out.println("Burns:"+ex[i].burnsAt(x,y,z));
				switch(ex[i].burnsAt(x,y,z)){
					case 1 : velx+=boost; break;
					case 2 : velz+=boost; break;
					case 3 : velx-=boost; break;
					case 4 : velz-=boost; break;
					case 5 : vely+=boost; move(realx,realy+128,realz); break;
				}
				}
	}
}