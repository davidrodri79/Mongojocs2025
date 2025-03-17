package com.mygdx.mongojocs.ultranaus;

import java.util.Random;

public class UltraShip
{
	public static final int DIST_COLLIDE=3000;
	public static final int SHIELD_TIME=300;
	public static final int TURBO_TIME=300;
	public static final int MAX_ENERGY=199;
	
	public static final int PLAYING=0;
	public static final int SPIN=1;
	public static final int EXPLODE=9;
	public static final int BURNING=10;
	
	public static final int RACE=0;
	public static final int ATTACK=1;

	
	public static final int VELOCITY[]={2,3,2,2,1,3,1,4,2};
	public static final int HANDLING[]={2,1,1,3,1,1,4,1,1};
	public static final int POWER[] =  {2,2,3,1,4,2,1,1,3};
	
	public int cnt, model, realx, realy, x, y, energy, state, frontx, fronty, angle, nextcheckp, ianextcp, shield, turbo, rocket, laps, spin, rank;
	public UltraShip opp;
	private int hndl, eng, pow;
	public int acc, velx, vely, maxvel, behav;
	public boolean player=false, collided=false;
	public int laptime, lastlap, bestlap;
	Random rnd;

	public UltraShip(int i, int m, UltraRace rc)
	{
		model=m; rank=(UltranausCanvas.N_RACERS-1);
		x=rc.begin_x(i); y=rc.begin_y(i); state=PLAYING; energy=MAX_ENERGY;		
		realx=x<<10; realy=y<<10;	
		angle=rc.optimal_orientation(this); velx=0; vely=0; spin=0; acc=0;
		cnt=0; nextcheckp=0; ianextcp=0; shield=0; laps=0;
		opp=null; behav=RACE; 
		hndl=4+HANDLING[m]; eng=3+VELOCITY[m]; pow=POWER[m];
		rnd=new Random(); bestlap=9999999; lastlap=(int)System.currentTimeMillis();
	}
	
	public void set_state(int s)
	{
		cnt=0; state=s;	
		if((s==SPIN) && player) UltranausCanvas.vibrate(50,10);
	}
	
	public void update(Control ctrl, UltraRace rc)
	{
		int dx, dy;
		
		cnt++;
													
		if(state==PLAYING){
			
			if(ctrl.right) angle-=hndl; if(angle<0) angle+=256;
			if(ctrl.left) angle+=hndl; if(angle>=256) angle-=256;
			
			if(cnt%4==0){
				// Acelerate
				if(ctrl.up || UltranausMain.auto){
									
					if(turbo>0){ 
						if(acc<(int)(4*eng/3)) acc++;
					}else if(acc<eng) acc++;
					collided=false;					
				}
													
				// Brake
				if(ctrl.down){		
					if(acc>-eng>>1) acc--;				
					collided=false;
				}
			}
			
			//Launch rocket!
			
			if((ctrl.but1) && (rocket>0)){
				 rc.launch_rocket(this);
				 rocket=0;
			}
		}
		
		if(state==SPIN){
			
			angle-=spin; if(angle<0) angle+=256;
			spin--; if(spin==0) set_state(PLAYING);
		}
			
		if((collided) && (cnt%4==0))
			if(acc>0) acc--;
			else if(acc<0) acc++;			
								
		velx=(acc*Trig.cos(angle));
		vely=(acc*Trig.sin(angle));		
		
		//System.out.println("Trac("+ftrac[0]+","+ftrac[1]+")  Air("+fdrag[0]+","+fdrag[1]+")  Friction("+frr[0]+","+frr[1]+"  Long("+flong[0]+","+flong[1]+")  Acc("+accx+","+accy+") Vel("+velx+","+vely+")");					
													
		move(realx+velx,realy-vely,rc);
				
		// Take items
		for(int i=0; i<UltraRace.N_ITEMS; i++)
			if((x>>3==rc.itemx[i]) && (y>>3==rc.itemy[i])){
				
				switch(rc.itemt[i]){
					
					case UltraRace.TURBO : turbo=TURBO_TIME; if (player) {rc.post_message(Text.turbo); UltranausMain.pant.play_music(2,1); } break;					
					case UltraRace.ENERGY : modify_energy(MAX_ENERGY/6); if (player) { rc.post_message(Text.energy+" "+(energy>>1)); UltranausMain.pant.play_music(3,1); } break;
					case UltraRace.SHIELD : shield=SHIELD_TIME; if (player) { rc.post_message(Text.shield); UltranausMain.pant.play_music(4,1); } break;	
					case UltraRace.ROCKET : rocket=1; if (player) { rc.post_message(Text.rocket); UltranausMain.pant.play_music(4,1); } break;
				}				
				rc.itemt[i]=0;	
			}
		
		if(shield>0) shield--;
		if(turbo>0) turbo--;
		
		// Course and lap count
		if(rc.inside_checkp(x,y,nextcheckp)){
			nextcheckp++;			
			//if (player) System.out.println(nextcheckp);
		}
						
		if(nextcheckp>=rc.ncheckp) {
			nextcheckp=0; laps++;
			laptime=(int)System.currentTimeMillis()-lastlap; 
			lastlap=(int)System.currentTimeMillis();
			if(laptime<bestlap) bestlap=laptime;
			if(player) rc.post_message(UltranausMain.time_str(laptime));			
		}
		
		dx=rc.iaroute[ianextcp][0]-x; dy=rc.iaroute[ianextcp][1]-y;
		if(dx*dx+dy*dy<256) ianextcp++;
		if(ianextcp>=rc.ncheckp) ianextcp=0;
				
		//Manage states
		if((state==EXPLODE) && (cnt>=27)) set_state(BURNING);
												
	}
	
	public void ia_update(UltraRace rc)
	{
		int an, dif, dx, dy;
		Control ctr=new Control();
				
		ctr.reset();
		
		// Changing the behaviour
		
		if(behav==ATTACK)
		if((opp==null) || (energy<MAX_ENERGY>>1) || ((Math.abs(rnd.nextInt())%200==0))) behav=RACE;
		
		if(behav==RACE)
		if((opp!=null) && (Math.abs(rnd.nextInt())%300==0)) behav=ATTACK;
						
		// Attack the opponent
		if(behav==ATTACK)
			an=Trig.atan(y-opp.y,opp.x-x);
		// Follow the race
		else		
		an=rc.optimal_orientation(this);
								
		dif=an-angle; 
		if(dif>128) dif-=256; if(dif<-128) dif+=256; 
		
		// Right direction, accelerate
		if(Math.abs(dif)<8) ctr.up=true;
		// Very bad orientation, better brake!
		else if(Math.abs(an-angle)>26) ctr.down=true;
		
		// Drive
		if(dif>0) ctr.left=true;
		else if(dif<0) ctr.right=true;
		
		// Launch rockets
		if((rocket>0) && (opp!=null) && (Math.abs(rnd.nextInt())%20==0))
			ctr.but1=true;
		
		update(ctr,rc);			
	}
	
	private int decrease_abs(int v, int i)
	{
		if(v>0) {v-=i; if(v<0) v=0;}
		if(v<0) {v+=i; if(v>0) v=0;}
		
		return v;
	}
	
	public void modify_energy(int d)
	{
		if((shield>0) && (d<0)) return;
		energy+=d;
		if(energy<0) energy=0;
		if(energy>MAX_ENERGY) energy=MAX_ENERGY;
		if((energy==0) && (state<EXPLODE))
			set_state(EXPLODE);
	}
	
	private void move(int nx, int ny, UltraRace rc) 		
	{	
		int dx, dy, oldx=realx, oldy=realy;
		
		// Maintain inside bounds
			
		if(nx<0) nx=0; if(nx>rc.sizex<<13) nx=rc.sizex<<13;
		
		if(ny<0) ny=0; if(ny>rc.sizey<<13) ny=rc.sizey<<13;
		
		// Different kinds of collide
		
		 if(rc.collide(nx,realy)){				
			
			realx=realx; realy=ny;
			//acc=decrease_abs(acc,1);
			collided=true;
			modify_energy(-1);
			
		}else if(rc.collide(realx,ny)){				
			
			realx=nx; realy=realy;
			//acc=decrease_abs(acc,1);
			collided=true;
			modify_energy(-1);
						
		}else{
			// Normal movement									
			realx=nx; realy=ny;				
		}
		
		if(rc.collide(realx,realy)){
		
			realx=oldx; realy=oldy; 
			collided=true; acc=-acc>>2;				
			modify_energy(-1);
		}
				
		x=realx>>10; y=realy>>10;							
	}
	
	void ship_collide(UltraShip op, UltraRace rc)
	{
		int dx, dy, temp, dif;
		
		dx=realx-op.realx; dy=realy-op.realy;
		
		if(Math.abs(dx)<DIST_COLLIDE && Math.abs(dy)<DIST_COLLIDE){
			
										
			// Results speed			
			if(dy>=dx){	
				
				//Avoid one ship inside another			
				dif=(DIST_COLLIDE-Math.abs(realy-op.realy))>>1;
							
				if(realy<op.realy){
					move(realx,realy-dif,rc);
					op.move(op.realx,op.realy+dif,rc);
				}else{
					move(realx,realy+dif,rc);
					op.move(op.realx,op.realy-dif,rc);
				}								
				temp=acc;
				acc=op.acc;
				op.acc=temp;				
			}
			if(dx>=dy){	
														
				//Avoid one ship inside another										
				dif=(DIST_COLLIDE-Math.abs(realx-op.realx))>>1;
								
				if(realx<op.realx){
					move(realx-dif,realy,rc);
					op.move(op.realx+dif,op.realy,rc);
				}else{
					move(realx+dif,realy,rc);
					op.move(op.realx-dif,op.realy,rc);
				}																				
				temp=acc;
				acc=op.acc;
				op.acc=temp;				
			}
			
			if(op.shield>0 && state==PLAYING) {modify_energy(-POWER[op.model]*2); spin=60; set_state(SPIN);}
			else modify_energy(-POWER[op.model]);
			if(shield>0 && op.state==PLAYING) {op.modify_energy(-POWER[model]*2); op.spin=60; op.set_state(SPIN);}
			else op.modify_energy(-POWER[model]);
			collided=true;
			op.collided=true;
			
			if(player || op.player){
				 UltranausCanvas.vibrate(50,10); 			
				 if(rc.barsy<16) rc.barsy+=1;
			}
						
		}		
	}
	
	public boolean colliding_point(int xx, int yy)
	{
		int dx, dy;
			
		dx=realx-xx; dy=realy-yy;
			
		if(Math.abs(dx)<DIST_COLLIDE>>1 && Math.abs(dy)<DIST_COLLIDE>>1)
			return true;
		else return false;		
	}
	
	public boolean wins(UltraShip op)
	{
		if(laps>op.laps) return true;
		else if	(laps<op.laps) return false;
		else
			if(nextcheckp>op.nextcheckp) return true;
			else if(nextcheckp<op.nextcheckp) return false;	
			else {
						
				//if (mydyst<opdist) return true;
				return false;						
			}
			
	}
}
