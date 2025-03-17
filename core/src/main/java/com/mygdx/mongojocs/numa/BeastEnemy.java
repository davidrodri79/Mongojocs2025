package com.mygdx.mongojocs.numa;

import java.util.Random;


public class BeastEnemy extends BeastCharacter {

	
	public static final int WALK = 1;	
	public static final int JUMP = 2;	
	public static final int FALL = 3;
	public static final int STAND_SHOT = 4;
	public static final int FLY = 5;
	
	public static final int SNAKE = 1;
	public static final int DRAGON = 2;
	public static final int DINO = 3;
	public static final int SCORPION = 4;
	public static final int PLANT = 5;
	public static final int TOAD = 6;
	public static final int INSECT = 7;
	public static final int MATE = 8;
	public static final int BOSS = 9;
		
	public static final int GRAVITY = 150;
	
	public int type, sizeX, sizeY;
	
	public static final int SHOT = 1;
	public static final int SNAKESHOT = 2;

	public int arms[][];
	public int angle = 8, armDir = 0, armVel = 2, snakeX, snakeY, snakeCnt;
	Square piece;
	Random rnd;
	
	BeastEnemy(int xx, int yy, BeastWorld world, int t)
	{
		w=world;
		move(xx<<7,yy<<7);
		type=t;
		switch(t){
			case BOSS :
			case PLANT : setState(STAND); break;
			case INSECT :
			case DRAGON : setState(FLY); break;
			default :	setState(WALK); break;
		}
		
		switch(t){
			case SNAKE : sizeX=24; sizeY=21; maxLife=70; break;	
			case DRAGON : sizeX=24; sizeY=14; maxLife=20; break;	
			case DINO : sizeX=23; sizeY=15; maxLife=1; break;	
			case SCORPION : sizeX=14; sizeY=11; maxLife=10; break;	
			case PLANT : sizeX=17; sizeY=17; maxLife=40; break;	
			case TOAD : sizeX=24; sizeY=16; maxLife=30; break;	
			case INSECT : sizeX=14; sizeY=19; maxLife=50; break;	
			case MATE : sizeX=23; sizeY=22; maxLife=60; break;				
			case BOSS : rnd=new Random(); 
				 snakeX = w.sizeX*16; snakeY = 20; snakeCnt = 0; life=4250; maxLife=life; piece=new Square();				 
				armDir=0; armVel=2; arms=new int[11][2];
				break;
		}
		
		life=maxLife;
	}
	
	void update(BeastPlayer p)
	{
		if(Math.abs(p.x-x)>150 || Math.abs(p.y-y)>150) return;
		
		cnt++;
		
		body.set(x-sizeX/2,y-sizeY,sizeX,sizeY);
	
		switch(type){
		
			case SNAKE : updateSnake(p); break;
			case DRAGON : updateDragon(p); break;
			case DINO : if(p.state != p.FALL) p.helpRequest=1; updateDino(p); break;
			case SCORPION : updateScorpion(p); break;
			case PLANT : updatePlant(p); break;
			case TOAD : updateToad(p); break;
			case INSECT : updateInsect(p); break;
			case MATE : p.helpRequest=3; updateMate(p); break;
		
		}
		if(state==DEAD && cnt==1) BeastCanvas.playSound(6,1);	
		
		if(vely>1500) vely=1500;
		
		if(state==JUMP || state==FALL || state==FLY || state==DEAD) move(realx+velx,realy+vely);
		else walk(realx+velx,realy+vely);
		
		// Bullet interact
		
		if(state!=DEAD)
		for(int i=0; i<w.shots.length; i++)
			if(w.shots[i]!=null)
			if(!w.shots[i].outOfWorld)
			if(w.shots[i].type<10)														
				if(body.collide(w.shots[i].damage)){						
						modifyLife(w.shots[i].x,w.shots[i].y,-w.shots[i].damAmount);
						switch(w.shots[i].type){
							case 0 : w.addEffect(x,y,BeastShot.SHOT_SPARK); break;
							case 1 : w.addEffect(x,y,BeastShot.SHOT_SPARK); break;
							case 2 : w.addEffect(x,y,BeastShot.EXPLOSION); break;
							case 3 : w.addEffect(x,y,BeastShot.EXPLOSION); break;
							case 4 : w.addEffect(x,y,BeastShot.EXPLOSION2); break;
						}
						w.shots[i].outOfWorld = true;												
					}	
					
		if(y>(w.sizeY+1)*16) outOfWorld = true;					
				
	}
		
	void updateSnake(BeastPlayer p)
	{
		switch(state){
					
			case WALK :			
			if(dir==0) velx=100;			
			if(dir==1) velx=-100;
			if(p.x<x-40) dir=1;
			if(p.x>x+40) dir=0; 
			if(Math.abs(p.y-y)<20) setState(STAND_SHOT);
												
			if(!w.ground(x,y)) {setState(FALL);}
			break;							
						
			case FALL :
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(WALK);}						
			break;
			
			case STAND_SHOT :
			velx=0;	
			if(p.x<x) dir=1;
			if(p.x>=x) dir=0; 
			if(cnt%20==12) w.addShot(realx,realy-2048,dir,0,0, 10);
			if(Math.abs(p.y-y)>=20) setState(WALK);
			break;						
			
			case DEAD :			
			if(cnt>=16) outOfWorld=true; 
			break;
		}	
	}
	
	void updateDragon(BeastPlayer p)
	{
			
		switch(state){
					
			case FLY :			
			vely=BeastCanvas.sin((cnt*8)%256)>>3;
			if(dir==0) velx=150;			
			if(dir==1) velx=-150;
			if(p.x<x-60) dir=1;
			if(p.x>x+60) dir=0;
			
			if(y>p.y-10) vely+=-90;
			else if(y<p.y-30) vely+=90;			
			else vely+=0;				
																	
			break;		
			
			case DEAD :
			if(cnt>=10) outOfWorld = true;
			vely+=30;
			break;									
		}	
	}
	
	void updateDino(BeastPlayer p)
	{	
		int speed;
		switch(state){
					
			case WALK :			
			if(Math.abs(p.x-x)<40) speed=18; else speed=5;
			if(dir==0 && velx<180) velx+=speed;			
			if(dir==1 && velx>-180) velx+=-speed;
			if(x<p.x) dir=1;
			else dir=0; 
			//if(Math.abs(p.y-y)<20) setState(STAND_SHOT);
												
			if(!w.ground(x,y)) {setState(FALL);}
			break;							
			
			case FALL :
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(WALK);}						
			break;
			
			case DEAD :
			if(cnt>=16) outOfWorld = true;			
			break;												
		}	
	}
	
	void updateScorpion(BeastPlayer p)
	{
			
		switch(state){
					
			case WALK :						
			if(dir==0) velx=50;			
			if(dir==1) velx=-50;
			if(p.x<x-60) dir=1;
			if(p.x>x+60) dir=0;
			if(Math.abs(p.x-x)<20 && Math.abs(p.y-y)<24) setState(STAND_SHOT);
												
			if(!w.ground(x,y)) {setState(FALL);}
			break;		
			
			case STAND_SHOT :
			velx=0;	
			if(p.x<x) dir=1;
			if(p.x>=x) dir=0; 			
			if(cnt==16 && Math.abs(p.x-x)<30 && Math.abs(p.y-y)<20 && p.invulnerable==0 && p.state!=BeastPlayer.DEAD) p.damage(body,-15);
			if(Math.abs(p.x-x)>=20) setState(WALK);
			break;														
			
			case FALL :
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(WALK);}						
			break;
			
			case DEAD :
			if(cnt>=6) outOfWorld = true;			
			break;															
		}	
	}
	
	void updatePlant(BeastPlayer p)
	{
		switch(state) {
			
			case STAND :	
			velx=0;	
			if(p.x<x) dir=1;
			if(p.x>=x) dir=0; 	
			if(Math.abs(p.x-x)<50 && Math.abs(p.y-y)<20) setState(STAND_SHOT);
			if(!w.ground(x,y)) {setState(FALL);}
			break;
			
			case FALL :
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(STAND);}						
			break;						
			
			case STAND_SHOT :
			if(cnt==8 && Math.abs(p.x-x)<35 && Math.abs(p.y-y)<20 && p.invulnerable==0 && p.state!=BeastPlayer.DEAD) p.damage(body,-15);
			if(cnt>=12) setState(STAND);
			break;
			
			case DEAD :
			if(cnt>=12) outOfWorld = true;			
			break;																		
		}
	}
		
	void updateToad(BeastPlayer p)
	{
		switch(state) {
			
			case WALK :		
			if(dir==0) velx=90;			
			if(dir==1) velx=-90;
			if(p.x<x) dir=0;
			if(p.x>=x) dir=1;
			if(Math.abs(p.x-x)>45) setState(STAND_SHOT);			
			if(!w.ground(x,y)) {setState(FALL);}
			break;
			
			case FALL :
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(WALK);}						
			break;						
			
			case STAND_SHOT :
			velx=0; 
			if(p.x>x) dir=0;
			if(p.x<=x) dir=1;
			if(cnt==9) w.addShot(realx+(dir==0 ? 1500 : -1500),realy-1500,dir,0,0, 11);
			if(cnt>=12) 
				if(Math.abs(p.x-x)<=45) setState(WALK);
				else setState(STAND_SHOT);
			break;
			
			case DEAD :
			if(cnt>=12) outOfWorld = true;			
			break;																					
		}		
		
	}
	
	void updateInsect(BeastPlayer p)
	{		
		int angle;
		
		switch(state){
					
			case FLY :			
			velx=BeastCanvas.cos((cnt*8)%256)>>3;
			vely=BeastCanvas.sin((cnt*8)%256)>>3;																	
			if(y<p.y-40) vely+=100;
			if(y>p.y) vely-=100;
			if(x<p.x-40) velx+=150;
			if(x>p.x+40) velx-=150;
			if(x<p.x) dir=0; else dir=1;
			if(cnt%50==0) {
								
				angle=BeastCanvas.atan(p.y-y,p.x-x); 
				w.addShot(realx,realy-2048,dir,BeastCanvas.cos(angle)>>2,BeastCanvas.sin(angle)>>2, 12);
			}
			break;											
			
			case DEAD :
			if(cnt>=8) outOfWorld = true;			
			vely+=GRAVITY;
			velx+=(dir==0 ? -GRAVITY/2 : GRAVITY/2);
			break;
		}	
	}
	
	void updateMate(BeastPlayer p)
	{
		switch(state) {
			
			case WALK :		
			if(dir==0) velx=210;			
			if(dir==1) velx=-210;
			if(p.x<x) dir=0;
			if(p.x>=x) dir=1;
			if(Math.abs(p.x-x)>65) setState(STAND_SHOT);			
			if(!w.ground(x,y)) {setState(FALL);}
			break;
			
			case FALL :
			vely+=GRAVITY;
			if(w.ground(x,y)) {vely=0; move(realx,w.yAt(x,y)-2); setState(WALK);}						
			break;						
			
			case STAND_SHOT :
			velx=0; 
			if(p.x>x) dir=0;
			if(p.x<=x) dir=1;
			if(cnt==18) w.addShot(realx+(dir==0 ? 1500 : -1500),realy-1500,dir,0,0, 13);
			if(cnt>=24) 
				if(Math.abs(p.x-x)<=65) setState(WALK);
				else setState(STAND_SHOT);
			break;
			
			case DEAD :
			if(cnt>=12) outOfWorld = true;			
			break;																					
		}			
	}
	
	void updateBoss(BeastPlayer p)
	{
								
		cnt++;		
		snakeCnt++;
		int a = 0;
		
			
		switch(state){
			
			case STAND :			
			if(cnt>100) 
				if(p.x<x-120 && snakeX<-50) setState(SNAKESHOT);
				else setState(SHOT);
			break;
			
			case SHOT :
			if(cnt==2) {
								
				a=BeastCanvas.atan(p.y-y,p.x-x); 
				w.addShot(realx-512,realy,1,BeastCanvas.cos(a)>>2,BeastCanvas.sin(a)>>2, 14);
			}			
			if(cnt>=30) setState(STAND);
			break;
			
			case SNAKESHOT :
			if(cnt==15) {								
				snakeX = (w.sizeX-2)*16; snakeY = y;
			}			
			if(cnt>=100) setState(STAND);
			break;
			
			case DEAD :	
			if(cnt%10 == 0)
			{
				w.addEffect(x+rnd.nextInt()%10,y+rnd.nextInt()%10,BeastShot.EXPLOSION);	
			}				
			if(cnt>=150) outOfWorld = true;					
			break;
		}
		
				
		if(state!=DEAD){
									
			switch(armDir){
				
				case 0 :
				if(p.x>x-70){
					angle=angle+(armVel/2); if(angle>32) {armDir=2; armVel=2+(Math.abs(rnd.nextInt())%3);}
				} else armDir = 1;
				break;
				
				case 1 :
				if(angle>-16) angle=angle-(armVel/2); else if(Math.abs(p.x-x)<70) armDir=0;
				break;
				
				case 2 :
				if(angle>-16) angle=angle-(armVel); else if(Math.abs(p.x-x)<70) armDir=0;
				break;
				
			}
				
			body.set(x-10,y-8,20,18);
		
			arms[0][0]=x+50; arms[0][1]=y+12;
			for(int i=1; i<arms.length; i++){
			
				a += angle;
			
				arms[i][0] = arms[i-1][0] - ((12*BeastCanvas.cos(a))>>10);
				arms[i][1] = arms[i-1][1] - ((12*BeastCanvas.sin(a))>>10);
		
				piece.set(arms[i][0]-6,arms[i][1]-6,12,12);		
				if(armDir==2)
				if(piece.collide(p.body)) 
						p.damage(piece, -3);
				
			}
		
			snakeX--;
			
			piece.set(snakeX,snakeY-6,50,12);		
			if(piece.collide(p.body)) 
						p.damage(piece, -1);
		}
		
		// Bullet interact
		if(state!=DEAD)
		for(int i=0; i<w.shots.length; i++)
			if(w.shots[i]!=null)
			if(!w.shots[i].outOfWorld)
			if(w.shots[i].type<10)														
				if(body.collide(w.shots[i].damage)){						
						modifyLife(w.shots[i].x,w.shots[i].y,-w.shots[i].damAmount);
						w.shots[i].outOfWorld = true;						
						w.addEffect(x,y,BeastShot.SHOT_SPARK); 
					}		
	}


		
}