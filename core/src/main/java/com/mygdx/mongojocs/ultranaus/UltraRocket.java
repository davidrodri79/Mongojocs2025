package com.mygdx.mongojocs.ultranaus;

import java.util.Random;

public class UltraRocket
{
	public static final int FLYING=0;
	public static final int EXPLODE=1;
	
	public static final int DIST_COLLIDE=4096;
	
	public int cnt, realx, realy, x, y, state, frontx, fronty, angle;
	public UltraShip opp;	
	private int acc, velx, vely;	
	boolean in_use;
	Random rnd;
	
	public UltraRocket()
	{
		in_use=false; 
	}
	
	public void reset(UltraShip s)
	{
		in_use=true;
		
		realx=s.realx+(Trig.cos(s.angle)<<3); realy=s.realy-(Trig.sin(s.angle)<<3); 
		angle=s.angle;
		opp=s.opp; 
		velx=s.velx; vely=s.vely; acc=20;
		x=realx>>10; y=realy>>10;
		state=FLYING;
	}
	
	public void update(UltraRace rc)
	{
		int an, dif;
		cnt++;
		
		if(state==FLYING){				
			
			velx+=acc*(Trig.cos(angle)*acc)>>10;			
			vely+=acc*(Trig.sin(angle)*acc)>>10;
		
			move(realx+velx,realy-vely,rc);
			
			if(opp!=null){
				an=Trig.atan(y-opp.y,opp.x-x);				
				dif=an-angle; 
				if(dif>128) dif-=256; if(dif<-128) dif+=256; 
				if(dif<0) angle=(angle-12); if(angle<0) angle+=256;
				if(dif>0) angle=(angle+12)%256;
			}
		}
		
		if(state==EXPLODE){				
			
			if(cnt>=9) in_use=false;
		}
		
	}
	
	private void move(int nx, int ny, UltraRace rc) 		
	{	
		int i, dx, dy;
		
		// Maintain inside bounds
			
		if(nx<0) in_use=false; if(nx>rc.sizex<<13) in_use=false;
		
		if(ny<0) in_use=false; if(ny>rc.sizey<<13) in_use=false;
		
		// Collide with obstacles
		if(rc.collide(nx,ny)){
			
			cnt=0; state=EXPLODE;
			
		}else{				
			// Normal movement									
			realx=nx;			
			realy=ny;		
		}		
		
		// Collide with ships
		i=0;
		while(i<rc.nracers){
		
			dx=Math.abs(realx-rc.sh[i].realx); dy=Math.abs(realy-rc.sh[i].realy);
			
			if(dx<DIST_COLLIDE && dy<DIST_COLLIDE){
			
				cnt=0; state=EXPLODE;
				
				rc.sh[i].set_state(UltraShip.SPIN); rc.sh[i].spin=60;
				rc.sh[i].modify_energy(UltraShip.MAX_ENERGY/8);
									
				i=rc.nracers;	
			}			
			i++;	
		}
		x=realx>>10; y=realy>>10;							
	}	
}