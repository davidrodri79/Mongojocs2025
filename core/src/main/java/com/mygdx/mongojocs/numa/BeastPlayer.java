package com.mygdx.mongojocs.numa;

import java.util.Random;


public class BeastPlayer extends BeastCharacter {

	
	public static final int WALK = 1;
	public static final int JUMP = 2;
	public static final int FALL = 3;
	public static final int STAND_SHOT = 4;
	public static final int WALK_SHOT = 5;
	public static final int FALL_SHOT = 6;		
	public static final int PAIN = 7;
	public static final int BRAWL = 8;
	public static final int BRAWL2 = 9;
	public static final int CROUCH = 10;	
	public static final int PIT = 9998;	
	public static final int RIDE_STAND = 100;
	public static final int RIDE_WALK = 101;
	public static final int RIDE_JUMP = 102;
	public static final int RIDE_FALL = 103;
	public static final int RIDE_STAND_SHOT = 104;
	public static final int RIDE_WALK_SHOT = 105;
	public static final int RIDE_FALL_SHOT = 106;
	public static final int MATE_DEAD = 107;
		
	public static final int RUN_MAX_SPEED = 500;
	public static final int JUMP_IMPULSE = 1300;
	public static final int RIDE_RUN_MAX_SPEED = 550;
	public static final int RIDE_JUMP_IMPULSE = 1000;
	public static final int GRAVITY = 150;
	
	public int invulnerable, power, mateLife, lastGX, lastGY, lifes, gems, helpRequest;
	Random rnd;
	BeastCharacter nearestEnemy;
	boolean outOfLevel;
	
	
	BeastPlayer(int xx, int yy, BeastWorld world)
	{
		w=world;
		move(xx<<7,yy<<7);
		lifes=3;
		
		rnd = new Random(); nearestEnemy = null; invulnerable = 0; power = 0; maxLife=75; life=maxLife; gems=0;
		helpRequest = -1;
		checkBounds = true;
	}
	
	void reset()
	{		
		lifes--;
		if(lifes>0){ 			
			move(lastGX,lastGY); 
			nearestEnemy = null; invulnerable = 100; life=maxLife; 
			setState(STAND);
		}
	}
	
	void update(BeastCanvas c)
	{
		cnt++;
		if(invulnerable>0) invulnerable--;
		
		outOfLevel = x>=w.sizeX*16 && y<(w.sizeY+1)*16;
		//if(w.power>0) modifyPower(w.power); w.power=0;
	
		switch(state){
		
			case STAND :
			vely=0;
			if(c.right) {dir=0; setState(WALK);}
			if(c.left) {dir=1; setState(WALK);}
			if(c.up) {
				
				if(c.right) velx+=RUN_MAX_SPEED;
				if(c.left) velx-=RUN_MAX_SPEED;
				
				c.controlReset(); 
				vely=-JUMP_IMPULSE; 
				setState(JUMP);
				}
			if(c.down) {setState(CROUCH); c.controlReset();}
			if(c.but1) {
				if(nearestEnemy == null)
					setState(STAND_SHOT);	
				else if(Math.abs(rnd.nextInt())%2==0) setState(BRAWL); else setState(BRAWL2);
			}
			if(!w.ground(x,y)) {setState(FALL);}
			if(velx>40) velx-=40; else if(velx>0) velx=0;
			if(velx<-40) velx+=40; else if(velx<0) velx=0;
			break;	
			
			case WALK :
			animSpeed+=Math.abs(velx);
			if(c.up) {				
				if(c.right) velx=RUN_MAX_SPEED;
				if(c.left) velx=-RUN_MAX_SPEED;
				
				c.controlReset(); 
				vely=-JUMP_IMPULSE; 
				setState(JUMP);
				}
			if(c.down) {setState(CROUCH); c.controlReset();}
			if(dir==0){
				if(c.right)
					if(velx<RUN_MAX_SPEED) velx+=80;
				if(c.left) {dir=1; velx=0;}
			}
			if(dir==1){
				if(c.left)
					if(velx>-RUN_MAX_SPEED) velx-=80;
				if(c.right) {dir=0; velx=0;}
			}						
			if(c.but1) {
				if(nearestEnemy == null)
					setState(WALK_SHOT);	
				else if(Math.abs(rnd.nextInt())%2==0) setState(BRAWL); else setState(BRAWL2);
			}
			if(!w.ground(x,y)) {setState(FALL);}
			
			// Friction
			if(velx>0){
				if(velx>=40) velx-=40;	
				else velx=0;
			}else if(velx<0){
				if(velx<=-40) velx+=40;	
				else velx=0;
			}else setState(STAND);
			break;							
			
			case JUMP :
			vely+=GRAVITY;				
			if(c.right){
				dir=0;
				if(velx<RUN_MAX_SPEED) velx+=60;
			}
			if(c.left){
				dir=1;
				if(velx>-RUN_MAX_SPEED) velx-=60;
			}				
			if(vely>0) setState(FALL);
			if(c.but1) {setState(FALL_SHOT);}
			break;
			
			case FALL :
			vely+=GRAVITY;
			if(c.right){
				dir=0;
				if(velx<RUN_MAX_SPEED) velx+=60;
			}
			if(c.left){
				dir=1;
				if(velx>-RUN_MAX_SPEED) velx-=60;
			}							
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(WALK);}			
			if(c.but1) {setState(FALL_SHOT);}
			break;
			
			case STAND_SHOT :
			if(cnt==2)
				 fire();			
			if(cnt>=4) setState(STAND);
			break;
			
			case WALK_SHOT :
			animSpeed+=Math.abs(velx);
			if(cnt==2) fire();
			if(cnt>=4) setState(WALK);
			if(velx>0){
				if(velx>=40) velx-=40;	
				else velx=0;
			}else if(velx<0){
				if(velx<=-40) velx+=40;	
				else velx=0;
			}else setState(STAND);			
			break;
			
			case FALL_SHOT :			
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(WALK);}			
			if(cnt==1) fire();
			if(cnt>=3) setState(FALL);			
			break;
			
			case PAIN :
			vely=0;
			if(velx>40) velx-=40; else if(velx>0) velx=0;
			if(velx<-40) velx+=40; else if(velx<0) velx=0;					
			if(cnt>=4) {setState(STAND); invulnerable=100; };
			break;
			
			case RIDE_STAND :			
			if(c.right) {dir=0; setState(RIDE_WALK);}
			if(c.left) {dir=1; setState(RIDE_WALK);}
			if(c.up) {
				
				if(c.right) velx+=RIDE_RUN_MAX_SPEED;
				if(c.left) velx-=RIDE_RUN_MAX_SPEED;
				
				c.controlReset(); 
				vely=-RIDE_JUMP_IMPULSE; 
				setState(RIDE_JUMP);
				}
			if(c.but1) {setState(RIDE_STAND_SHOT);}
			if(!w.ground(x,y)) {setState(RIDE_FALL);}
			if(velx>40) velx-=40; else if(velx>0) velx=0;
			if(velx<-40) velx+=40; else if(velx<0) velx=0;
			break;	
			
			case RIDE_WALK :
			animSpeed+=Math.abs(velx);
			if(c.up) {				
				if(c.right) velx=RIDE_RUN_MAX_SPEED;
				if(c.left) velx=-RIDE_RUN_MAX_SPEED;
				
				c.controlReset(); 
				vely=-RIDE_JUMP_IMPULSE; 
				setState(RIDE_JUMP);
				}
			if(dir==0){
				if(c.right)
					if(velx<RIDE_RUN_MAX_SPEED) velx+=80;
				if(c.left) {dir=1; velx=0;}
			}
			if(dir==1){
				if(c.left)
					if(velx>-RIDE_RUN_MAX_SPEED) velx-=80;
				if(c.right) {dir=0; velx=0;}
			}						
			if(c.but1) {setState(RIDE_WALK_SHOT);}
			if(!w.ground(x,y)) {setState(RIDE_FALL);}
			// Friction
			if(velx>0){
				if(velx>=40) velx-=40;	
				else velx=0;
			}else if(velx<0){
				if(velx<=-40) velx+=40;	
				else velx=0;
			}else setState(RIDE_STAND);
			break;
			
						
			case RIDE_JUMP :
			vely+=GRAVITY;				
			if(c.right){
				dir=0;
				if(velx<RIDE_RUN_MAX_SPEED) velx+=50;
			}
			if(c.left){
				dir=1;
				if(velx>-RIDE_RUN_MAX_SPEED) velx-=50;
			}				
			if(vely>0) setState(RIDE_FALL);
			if(c.but1) {setState(RIDE_FALL_SHOT);}
			if(c.up) { setState(MATE_DEAD);}
			break;										
			
			case RIDE_FALL :
			vely+=GRAVITY;
			if(c.right){
				dir=0;
				if(velx<RIDE_RUN_MAX_SPEED) velx+=50;
			}
			if(c.left){
				dir=1;
				if(velx>-RIDE_RUN_MAX_SPEED) velx-=50;
			}										
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(RIDE_WALK);}			
			if(c.but1) {setState(RIDE_FALL_SHOT);}
			if(c.down) { setState(MATE_DEAD);}
			break;
			
			case RIDE_STAND_SHOT :
			if(cnt==2) fire();
			if(cnt>=4) setState(RIDE_STAND);
			break;
			
			case RIDE_WALK_SHOT :
			animSpeed+=Math.abs(velx);
			if(cnt==3) fire();
			if(cnt>=6) setState(RIDE_WALK);
			break;			
			
			case RIDE_FALL_SHOT :			
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(RIDE_WALK);}			
			if(cnt==4) fire();
			if(cnt>=6) setState(RIDE_FALL);									
			
			
			case BRAWL :
			if(cnt==6){
				if(nearestEnemy!=null){
					nearestEnemy.modifyLife(x,y,-40);
					w.addEffect(nearestEnemy.x,nearestEnemy.y,BeastShot.SHOT_SPARK);
				}
			}
			if(cnt>=8) setState(STAND);
			break;
			
			case BRAWL2 :			
			if(cnt==6){
				if(nearestEnemy!=null){
					nearestEnemy.modifyLife(x,y,-40);
					w.addEffect(nearestEnemy.x,nearestEnemy.y,BeastShot.SHOT_SPARK);
				}
			}
			if(cnt>=10) setState(STAND);
			break;
			
			case CROUCH :
			if(velx>40) velx-=40; else if(velx>0) velx=0;
			if(velx<-40) velx+=40; else if(velx<0) velx=0;			
			if(c.up) setState(STAND);
			if(c.right){
				 if(dir!=0) dir=0; else setState(WALK);
				 c.controlReset();
			}
			if(c.left){
				 if(dir!=1) dir=1; else setState(WALK); 
				 c.controlReset();
			}
			break;
			
			case PIT :
			case DEAD :
			if(cnt==1) BeastCanvas.playSound(4,1);								
			vely=0;
			if(velx>40) velx-=40; else if(velx>0) velx=0;
			if(velx<-40) velx+=40; else if(velx<0) velx=0;
			if(cnt>=40) reset();
			
			if(!w.ground(x,y)) vely+=GRAVITY;			
			else{vely=0; move(realx,w.yAt(x,y)-2);}
			//if(c.but1) {modifyLife(0,0,100); setState(STAND);}
			break;
			
			case MATE_DEAD :
			if(cnt>=12) setState(STAND);
			break;
			
		}
		
		if(y>(w.sizeY+1)*16 && state!=DEAD && state!=PIT) setState(PIT);
		
		if(vely>1500) vely=1500;
		
		if(state==JUMP || state==FALL ||state==RIDE_JUMP) move(realx+velx,realy+vely);
		else if(state != PIT) walk(realx+velx,realy+vely);
		
		
		if(state>=RIDE_STAND) body.set(x-12,y-36,24,36);
		else if(state==CROUCH) body.set(x-12,y-12,24,12);
		else			  body.set(x-10,y-24,20,24);
		
		
		if(state!=DEAD  && state!=PAIN && state!=MATE_DEAD && invulnerable==0){
			
			// Bullet interact
			for(int i=0; i<w.shots.length; i++)
				if(w.shots[i]!=null)
				if(!w.shots[i].outOfWorld)
				if(w.shots[i].type>=10)														
					if(body.collide(w.shots[i].damage)){												
							w.shots[i].outOfWorld = true;							
							damage(w.shots[i].damage, -w.shots[i].damAmount);
						}		
				
			// Enemy contact Damage						
			if(nearestEnemy!=null)
				if(body.collide(nearestEnemy.body)) {
						damage(nearestEnemy.body, -20);
				}
				
			/*if(w.spikes(x,y-16)){
				helpRequest = 4;
				damage(body,-10);
				vely=-JUMP_IMPULSE/2;	
			}*/				
		}
		
		if(state!=DEAD  && state!=PAIN && state!=MATE_DEAD){
			// Background interact
			if(w.waste(x,y-16)){
				
				helpRequest = 2;
				
				velx=+1024;					
				w.addEffect(14+(x/16)*16,16+((y-16)/16)*16,BeastShot.WASTE);				
				if(state==STAND) setState(WALK);				
				if(state==RIDE_STAND) setState(RIDE_WALK);				
			}			
			if(w.waste(x,y)){
				
				helpRequest = 2;
				
				velx=+1024;					
				w.addEffect(14+(x/16)*16,16+(y/16)*16,BeastShot.WASTE);				
				if(state==STAND) setState(WALK);				
				if(state==RIDE_STAND) setState(RIDE_WALK);				
			}
						
		}
		
		// Take Items
		for(int i = 0; i<w.items[w.level].length; i++)
			if(!w.itemTaken[i])			
				if(Math.abs(w.items[w.level][i][0]*16 - x)<12 && Math.abs(w.items[w.level][i][1]*16 - (y-16))<12)
				{
					int yy=(w.tileSet == 2 ? 16 : 30);					
					w.addEffect(w.items[w.level][i][0]*16, w.items[w.level][i][1]*16+yy, BeastShot.TAKE_ITEM);
					w.itemTaken[i] = true;
					modifyPower(+1);	
					gems++;
					helpRequest = 0;
					BeastCanvas.vibrate(50,100);	
					BeastCanvas.playSound(3,1);					
				}
		
		if(w.ground(x,y)){
			lastGX = realx;	
			lastGY = realy;	
		}
					
	}
	
	void damage(Square sq, int am)
	{
		helpRequest = 5;
		if(state<RIDE_STAND){
			setState(PAIN);
			modifyLife(sq.centerx(),sq.centery(),am);			
		}else if(state<1000){
			mateLife+=am; 
			if(mateLife<=0) setState(MATE_DEAD); else invulnerable=100;
		}			
		BeastCanvas.vibrate(100,150);
	}	
	
	void modifyPower(int am)
	{
				
		power+=am; if(power<0) power=0;
		if(power>=138) power=138;
	}		

	public void fire()
	{
		int xx=realx+(dir==0 ? 1024 : -1024), yy=realy-(state<RIDE_STAND ? 1736 : 3000);

		int powerLev = power/30; if (powerLev>=4) powerLev=4;
				
		w.addShot(xx,yy,dir,0,0,powerLev);
		BeastCanvas.playSound(5,1);											
	}

}
