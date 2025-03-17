

package com.mygdx.mongojocs.cerberus;

public class CerberusSamuel {

	public static final int STAND = 0;
	public static final int WALKING = 1;
	public static final int JUMP1 = 2;
	public static final int JUMP2 = 3;
	public static final int FALL = 4;
	public static final int SW_SLASH = 5;
	public static final int CROUCHING = 6;
	public static final int CROUCH = 7;
	public static final int KICK = 8;
	public static final int PRE_RUN = 9;
	public static final int RUNNING = 10;
	public static final int RUN_JUMP = 11;
	public static final int SW_SLASH2 = 12;
	public static final int SW_SLASH3 = 13;
	public static final int RUN_SLASH = 14;
	public static final int JUMP_SLASH = 15;
	public static final int FALL2 = 16;
	public static final int DAMAGE = 17;
	public static final int CROUCH_DAMAGE = 18;
	public static final int FALL_DAMAGE = 19;
	public static final int DEATH = 20;
	public static final int WHIP_HIT = 21;
	public static final int JUMP_WHIP = 22;
	public static final int TAKE_ITEM = 23;
	public static final int GOT_ITEM = 24;
	public static final int BOOM_LAUNCH = 25;
	public static final int JUMP_BOOM = 26;
	public static final int JUMP3 = 27;
	public static final int WON = 28;
	public static final int TAKE_ITEM_SHORT = 29;
	
	
	public static final int DAMAGE_RECOVER_TIME = 60;
	
	public static final int SWORD = 0;
	public static final int WHIP = 1;
	public static final int BOOMER = 2;

	public int x, y, weapon, realx, realy, energy, state, dir, jumpdir, cnt, vely, dam_time, dam_amount, boom_cnt, boom_dir, boom_x, boom_y, boom_vx, boom_vy, boom_guide, camy_offset;
	public CerberusSquare body, damage;
	public boolean helmet, gauntlets, boots, armor, cape, can_damage, second_jump;
	public boolean platform;

	public CerberusSamuel(int xx, int yy)
	{
		x=xx; y=yy; realx=xx*100; realy=yy*100; dir=1; vely=0; jumpdir=0;  camy_offset=0;
		body=new CerberusSquare(); damage=new CerberusSquare(); dam_time=0; weapon=SWORD; platform=false;
		boom_cnt=0; second_jump=false;		
		set_state(STAND);
				
		helmet=true; gauntlets=true; boots=true; armor=true; cape=true;
		helmet=false; gauntlets=false; boots=false; armor=false; cape=false;
		
		energy=max_energy();
		
		//dam_time=99999;
	}
	public void set_state(int s)
	{
		state=s; cnt=0;	
		can_damage=false;
		switch(s){
		
			case JUMP3 :
			case JUMP1 :
			jumpdir=0;
			vely=-750;			
			break;
			
			case JUMP2 :				
			break;
			
			case JUMP_WHIP :
			case JUMP_SLASH :			
			case FALL :
			vely=0;
			break;						
			
			case SW_SLASH2 :
			//vely=-750;
			break;									
						
			case FALL_DAMAGE :
			vely=-100;
			break;		
			
			case DEATH : 
			vely=-750; 
			break;
		}
	}
	void update(Control c, CerberusWorld w)
	{
		int auxx=0, auxy;
		int Gravity=200;
		
		if(helmet==true) Gravity=150;
				
		cnt++;
		if (dam_time>0) dam_time--;
		
		if(energy==0 && state!=DEATH) set_state(DEATH);				
				
		switch(state){
		
			case STAND :
			if(c.right) { dir=1; set_state(WALKING); }
			if(c.left) { dir=2; set_state(WALKING); }			
			if(c.up)  {second_jump=false; set_state(JUMP1); }
			if(c.down)  set_state(CROUCHING); 			
			if(c.but1) 
			switch(weapon){
				case SWORD : set_state(SW_SLASH); break;
				case WHIP : set_state(WHIP_HIT); break;
				case BOOMER :  if(boom_cnt==0) {boom_guide=0; set_state(BOOM_LAUNCH); }								
				break;
			}			
			// Fall fom platform			
			if(!at_ground(w,x,y)) {jumpdir=0; set_state(FALL);}
			break;
			
			case WALKING :			
			if(c.right) {
				move(w,realx+350,realy,dir); 									
				dir=1;
			}
			else if(c.left) { 
				move(w,realx-350,realy,dir); 
				dir=2;
			}
			else if(c.up) { second_jump=false; set_state(JUMP1); }
			else if(!c.right && !c.left) {set_state(PRE_RUN);}
			//if(c.but1) set_state(SW_SLASH);
			// Fall fom platform			
			if(!at_ground(w,x,y)) {jumpdir=0; set_state(FALL);}
			break;	
			
			case PRE_RUN :
			if(cnt>2) set_state(STAND);
			if(dir==1 && c.right) set_state(RUNNING);
			if(dir==2 && c.left) set_state(RUNNING);
			break;
			
			case RUNNING :			
			if(dir==1) {
				move(w,realx+550,realy,dir); 									
				dir=1;
			}
			else if(dir==2) { 
				move(w,realx-550,realy,dir); 
				dir=2;
			}
			
			if(c.up) { 
				vely=-1150; 
				if(c.right) jumpdir=1;
				if(c.left) jumpdir=2;
				else jumpdir=dir; 
				set_state(RUN_JUMP); }
			else if(c.right && dir==2) {dir=1; set_state(WALKING);}
			else if(c.left && dir==1) {dir=2; set_state(WALKING);}
			else if(c.down) {set_state(CROUCHING);}
			if(c.but1) 			
			switch(weapon){
				case SWORD : set_state(RUN_SLASH); break;
				case WHIP  : set_state(WHIP_HIT); break;
				case BOOMER  : set_state(BOOM_LAUNCH); break;
			}

			// Fall fom platform
			if(!at_ground(w,x,y))
			 {jumpdir=0; set_state(FALL);}			
			break;	
			
			case JUMP3 :
			case JUMP1 :			
			if(c.left) jumpdir=2;
			if(c.right) jumpdir=1;
			if(c.up) vely-=200;			
			if(cnt==2) {c.up=false; set_state(JUMP2);}
			break;
						
			case JUMP2 :
			
			if(cape==true){
				if(c.left) {dir=2; jumpdir=2; }
				if(c.right) {dir=1; jumpdir=1; }
			}
			
			if(boots==true)
			if(c.up && second_jump==false) {second_jump=true; set_state(JUMP3);}
			
			if(c.but1) 
			switch(weapon){
				case SWORD : set_state(JUMP_SLASH); break;
				case WHIP  : set_state(JUMP_WHIP); break;
				case BOOMER : set_state(JUMP_BOOM); break;
			}
			if(jumpdir==1) move(w,realx+475,realy+vely,jumpdir);
			else if(jumpdir==2) move(w,realx-475,realy+vely,jumpdir);
			else move(w,realx,realy+vely,jumpdir);
			vely+=Gravity; 			
			if(vely>=0) set_state(FALL);
			break;	
			
			case JUMP_SLASH :
			if(cnt==2) set_attack(27,-30,30,25,30);
			if(cnt>=4) set_state(FALL2);
			break;
			
			case RUN_JUMP :
			if(jumpdir==1) move(w,realx+675,realy+vely,jumpdir);
			else if(jumpdir==2) move(w,realx-675,realy+vely,jumpdir);
			else move(w,realx,realy+vely,jumpdir);
			vely+=Gravity; 
			if(vely>=0) set_state(FALL);
			break;
			
			case FALL2:
			case FALL :
			if(c.but1 && state==FALL) 
			switch(weapon){
				case SWORD : set_state(JUMP_SLASH); break;
				case WHIP  : set_state(JUMP_WHIP); break;
				case BOOMER: set_state(JUMP_BOOM); break;
			}	
			if(cape==true){
				if(c.left) {dir=2; jumpdir=2; }
				if(c.right) {dir=1; jumpdir=1; }
			}
									
			if(boots==true)
			if(c.up && second_jump==false) {second_jump=true; set_state(JUMP3);}
					
			if(jumpdir==1) move(w,realx+475,realy+vely,jumpdir);
			else if(jumpdir==2) move(w,realx-475,realy+vely,jumpdir);
			else move(w,realx,realy+vely,jumpdir);
			vely+=Gravity;
			// Stop falling, ground found			
			if(at_ground(w,x,y+2)){
				auxy=w.y_at_tile(x,y+2);
				if(auxy<y+2 || platform) {
					move(w,realx,auxy*100,jumpdir); set_state(STAND); 
				}
			}
			break;		
			
			case SW_SLASH :
			if(cnt==1) c.reset(); 
			if(cnt==2) set_attack(23,-19,30,25,30);
			if(cnt>=5) set_state(STAND);
			if(cnt>=4 && c.but1) {set_state(SW_SLASH2);}
			
			break;
			
			case SW_SLASH2 :
			if(cnt==1) c.reset();
			//if(dir==1) move(w,realx,realy+vely,jumpdir);
			//else if(dir==2) move(w,realx,realy+vely,jumpdir);
			if(cnt==2) set_attack(27,-30,30,25,35);
			//vely+=Gravity; 			
			//if(cnt>=6) set_state(FALL2);
			if(cnt>=6) set_state(STAND);
			if(cnt>=5 && c.but1) {set_state(SW_SLASH3);}			
			break;
			
			case SW_SLASH3 :			
			if(cnt==1) c.reset();
			//if(dir==1) move(w,realx,realy+vely,jumpdir);
			//else if(dir==2) move(w,realx,realy+vely,jumpdir);			
			if(cnt==2) set_attack(23,-19,30,25,40);			
			if(cnt>=6) set_state(STAND);			
			if(cnt>=5 && c.but1) {set_state(SW_SLASH2);}
			/*if(!at_ground(w,x,y+2)){										
				move(w,realx,realy+vely,jumpdir);
				vely+=Gravity;
			}*/
			break;
			
			case RUN_SLASH :			
			if(dir==1) {
				move(w,realx+550,realy,dir); 													
			}
			else if(dir==2) { 
				move(w,realx-550,realy,dir); 				
			}			
			if(cnt>=4) set_state(SW_SLASH2);			
			break;
									
			case CROUCHING :
			if(cnt>=2) {c.reset(); set_state(CROUCH);}
			break;
			
			case CROUCH :
			if(c.right) { dir=1; set_state(WALKING); }
			if(c.left) { dir=2; set_state(WALKING); }			
			if(c.up) { /*second_jump=false;*/ c.reset(); set_state(STAND); }	
			if(c.down) if(camy_offset<40) camy_offset+=4;
			if(c.but1) set_state(KICK);		
			break;		
			
			case KICK :
			if(cnt==3) set_attack(20,-2,19,12,20);			
			if(cnt>=8) set_state(CROUCH);
			break;	
			
			case DAMAGE :			
			if(dir==2) move(w,realx+((6-cnt)*100),realy,jumpdir);
			else if(dir==1) move(w,realx-((6-cnt)*100),realy,jumpdir);			
			if(!at_ground(w,x,y+2)) set_state(FALL_DAMAGE);
			if(cnt>=6) {set_state(STAND); dam_time=DAMAGE_RECOVER_TIME;}			
			break;
			
			case CROUCH_DAMAGE :			
			if(dir==2) move(w,realx+((6-cnt)*75),realy,jumpdir);
			else if(dir==1) move(w,realx-((6-cnt)*75),realy,jumpdir);			
			if(!at_ground(w,x,y+2)) set_state(FALL_DAMAGE);
			if(cnt>=6) {set_state(CROUCH); dam_time=DAMAGE_RECOVER_TIME;}			
			break;
			
			case FALL_DAMAGE :			
			jumpdir=dir;
			if(jumpdir==2) move(w,realx+((6-cnt)*100),realy+vely,jumpdir);
			else if(jumpdir==1) move(w,realx-((6-cnt)*100),realy+vely,jumpdir);
			else move(w,realx,realy+vely,jumpdir);
			vely+=Gravity;
			// Stop falling, ground found			
			if(at_ground(w,x,y+2)){
				auxy=w.y_at_tile(x,y+2);
				if(auxy<y+2 || platform) { move(w,realx,auxy*100,jumpdir); set_state(STAND); dam_time=DAMAGE_RECOVER_TIME;}
			}else if(cnt>=6) {set_state(FALL); dam_time=DAMAGE_RECOVER_TIME;}
					
			break;		
			
			case DEATH :
			if(!at_ground(w,x,y+2) || vely<0){
			//if(true){
				if(dir==2) auxx=500; 
				if(dir==1) auxx=-500;
				move(w,realx+auxx,realy+vely,jumpdir);
				vely+=Gravity;
			}										
			break;			

			case JUMP_WHIP :			
			if(cnt==1) c.reset();
			if(cnt==5) set_attack(70,-32,60,19,27);
			if(cnt>=12) set_state(FALL2);			
			break;
						
			case WHIP_HIT :
			if(cnt==1) c.reset();
			if(cnt==5) set_attack(70,-32,60,19,27);
			if(cnt>=12) set_state(STAND);			
			break;
			
			case TAKE_ITEM :
			if(cnt>=6) {set_state(GOT_ITEM); c.reset();}
			break;			
			
			case TAKE_ITEM_SHORT :
			if(at_ground(w,x,y)){
				auxy=w.y_at_tile(x,y);
				if(auxy<y || platform) 
					move(w,realx,auxy*100,jumpdir);				
			}			
			if(cnt>=6) {set_state(STAND); c.reset();}
			break;			
						
			case GOT_ITEM :
			if(cnt>=6 || c.anybut) set_state(STAND);
			break;
			
			case BOOM_LAUNCH :
			if(cnt==1) c.reset();			
			if(c.up && cnt<=5) {c.reset(); boom_guide=1;}
			if(c.down && cnt<=5) {c.reset(); boom_guide=2;}
			if(cnt==5){
				boom_dir=dir;	
				boom_cnt=1;
				if(boom_guide==1) boom_vy=-10;
				if(boom_guide==2) boom_vy=+10;
				if(dir==1){
					boom_x=x+30; boom_y=y-35; boom_vx=+20; 
				}else{
					boom_x=x-30; boom_y=y-35; boom_vx=-20; 
				}		
				can_damage=true;											
			}
			if(cnt>=10)
				set_state(STAND);		
			break;
			
			case JUMP_BOOM :
			if(cnt==1) c.reset();			
			if(c.up && cnt<=5) {c.reset(); boom_guide=1;}
			if(cnt==5){
				boom_dir=dir;	
				boom_cnt=1;
				if(boom_guide==1) boom_vy=-10;
				if(dir==1){
					boom_x=x+30; boom_y=y-35; boom_vx=+20; 
				}else{
					boom_x=x-30; boom_y=y-35; boom_vx=-20; 
				}		
				can_damage=true;		
			}
			if(cnt>=10)
				set_state(FALL2);		
			break;
			
			case WON :
			break;
																		
		}
		
		mantain_scroll(w);					
				
		if(state==KICK || state==CROUCHING || state==CROUCH)
		body.set(x-9,y-28,18,28);			
		else body.set(x-9,y-44,18,44);
		
		if(y>w.sy+40 && state!=DEATH) set_state(DEATH);
				
		// Lava kills at caves level		
		if(w.level==CerberusWorld.CAVES && 
		(w.tile_at(x,y-10)==60 || w.tile_at(x,y-10)==61 || w.tile_at(x,y-10)==69 || w.tile_at(x,y-10)==70 || w.tile_at(x,y-10)==37 || w.tile_at(x,y-10)==38 || w.tile_at(x,y-10)==39))
			if(dam_time==0) modify_energy(-1000);

		// Spikes kill at castle level			
		if(w.level==CerberusWorld.CASTLE && 
		(w.tile_at(x,y-10)==15 || w.tile_at(x,y-10)==16 || w.tile_at(x,y-10)==6 || w.tile_at(x,y-10)==7))
			if(dam_time==0) modify_energy(-1000);

		if(camy_offset>0 && state!=CROUCH) camy_offset-=4;			
							
		// Boomerang update
		if(boom_cnt>0){
			boom_cnt++;	
			if (boom_cnt>20) boom_cnt=0;
			if(boom_dir==1) boom_vx-=2;
			if(boom_dir==2) boom_vx+=2;			
			boom_x+=boom_vx; 
			if(boom_guide==1) {boom_vy++; boom_y+=boom_vy;}
			if(boom_guide==2) {boom_vy--; boom_y+=boom_vy;}
			damage.set(boom_x-7,boom_y-7,14,14);
			if(gauntlets==true) dam_amount=40;									
			else dam_amount=20;
			if(boom_cnt==10) can_damage=true; 
		}
						
						
	}
	
	void move(CerberusWorld w, int nx, int ny, int d)
	{
		int ix=0, auxy=0, dy=(ny-realy);
		if(dir==1) ix=+1200;
		if(dir==2) ix=-1200;
		
		while (w.solid(w.tile_at(x,y-32)) && w.solid(w.tile_at(x,y-16)) && (y<w.sy+40)){
			realy+=100;			
			x=realx/100; y=realy/100;		
		}	
	
		// Avoid obstacle collide when moving horizontally
		if((!w.solid(w.tile_at((nx+ix)/100,(ny-3200)/100)) || (w.solid(w.tile_at(nx/100,(ny-3200)/100))) && !w.solid(w.tile_at(nx/100,(ny-4800)/100)))
			&& nx<w.sx*100 && nx>0){
				
			realx=nx; x=realx/100; 		
		}
			
		if(vely<0)
			auxy=ny;
		else if(w.solid(w.tile_at(x,(ny/100)-8)))			
			auxy=w.y_at_tile(x,(ny/100)-8)*100;					
		else if(w.solid(w.tile_at(x,(ny/100)-1)))			
			auxy=w.y_at_tile(x,(ny/100)-1)*100;	
		else if(w.solid(w.tile_at(x,(ny/100)+12)))
			auxy=w.y_at_tile(x,(ny/100))*100;
		else	auxy=ny;						
		
		if(auxy>(w.sy+60)*100) auxy=(w.sy+60)*100;
		
		realy=auxy;
										
		x=realx/100; y=realy/100;		
		
		boom_y+=dy/100;
		
		// Mantain world scroll		
			
	}
	
	public void mantain_scroll(CerberusWorld w)
	{
		if(dir==1)
		if(w.bx<x-100) w.bx+=6;
		else if(w.bx<x-60) w.bx+=4;
		else if(w.bx>x-30) w.bx-=4;
		
		if(dir==2)
		if(w.bx>x-76) w.bx-=6;
		else if(w.bx>x-116) w.bx-=4;
		else if(w.bx<x-146) w.bx+=4;
						
		if(w.bx<0) w.bx=0; if(w.bx>=CerberusCanvas.sizex(w)-CerberusCanvas.canvx()) w.bx=CerberusCanvas.sizex(w)-CerberusCanvas.canvx();
		
		if(w.by>y+camy_offset-CerberusCanvas.screeny()+44) w.by=y+camy_offset-CerberusCanvas.screeny()+44;		
		else if(w.by<y+camy_offset-CerberusCanvas.screeny()+24) w.by=y+camy_offset-CerberusCanvas.screeny()+24;
				
		if(w.by<0) w.by=0;
		if(w.by>=CerberusCanvas.sizey(w)-CerberusCanvas.screeny()) w.by=CerberusCanvas.sizey(w)-CerberusCanvas.screeny()-1;		
		//System.out.println("BY:"+w.by+" sizey:"+CerberusCanvas.sizey(w)+" SCREENY:"+CerberusCanvas.screeny());
	}
	
	public boolean at_ground(CerberusWorld w, int xx, int yy)
	{
		if(w.solid(w.tile_at(xx-14,yy)) || w.solid(w.tile_at(xx,yy)) || w.solid(w.tile_at(xx+14,yy)) || platform)
			return true;
		else return false;		
	}
	
	public void enemy_damage(CerberusEnemy e)
	{
		if(state!=WON && state!=DAMAGE && state!=CROUCH_DAMAGE && state!=FALL_DAMAGE && state!=DEATH && dam_time==0)
		if(body.collide(e.damage)){
			
			if(armor) modify_energy(-e.dam_amount/2); else 
			modify_energy(-e.dam_amount);
			
			if(e.x<x) dir=2;
			if(e.x>=x) dir=1;		
			if(state==JUMP2 || state==FALL || state==FALL2 || state==JUMP_SLASH || state==RUN_SLASH)
				set_state(FALL_DAMAGE);	
			else if(state==CROUCH || state==KICK)	
				set_state(CROUCH_DAMAGE);	
			else set_state(DAMAGE);	
		}
	}
	
	public int max_energy()
	{		
		return 70;	
	}
	
	public void modify_energy(int m)
	{
		energy+=m;
		if(energy<0) energy=0;
		if(energy>max_energy()) energy=max_energy();
	}
	
	public void set_attack(int dx, int dy, int sx, int sy, int am)
	{
		if(dir==2) dx=-dx;
		damage.set(x+dx-(sx/2),y+dy-(sy/2),sx,sy);	
		can_damage=true; 		
		if(gauntlets==true) dam_amount=am*2;
		else dam_amount=am;
		
	}
}