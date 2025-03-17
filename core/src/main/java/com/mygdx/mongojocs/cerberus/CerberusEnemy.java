package com.mygdx.mongojocs.cerberus;


public class CerberusEnemy {

	public static final int TRITON=0;
	public static final int SCORPION_FLY=1;
	public static final int UGLY_BALL=2;
	public static final int TURTLE_GOLEM=3;
	public static final int AMONITE=4;
	public static final int INV_AMONITE=5;
	public static final int LAVA_ROCK=6;
	public static final int PLATFORM=7;
	public static final int BIG_PLATFORM=8;
	public static final int SKULL_DOG=9;
	public static final int GOBLIN=10;
	public static final int EGG=11;
	public static final int SENTINEL=12;
	public static final int BONE_JAIL=13;
	public static final int ROCK_HEADL=14;
	public static final int ROCK_HEADR=15;

	
	public int x, y, begx, begy, sx, sy, type, frame=0, cnt=0, dir, dam_time, vely, life, dam_amount;
	public int bulx, buly, bulang, bulvel;
	public CerberusSquare body, damage;
	public boolean dead, out_of_world, bul_on;
	
	public CerberusEnemy()
	{}
	
	public CerberusEnemy(int xx, int yy, int t)
	{
		x=xx; y=yy; type=t; dir=2; begx=x; begy=y;
		dead=false; out_of_world=false; dam_time=9999; vely=0; bul_on=false;
		body=new CerberusSquare();
		damage=new CerberusSquare();
		
		switch(type){
			
			case TRITON : dam_amount=10; life=10; break;
			case SCORPION_FLY : dam_amount=12; life=25; break;
			case UGLY_BALL : dam_amount=15; life=150; break;
			case TURTLE_GOLEM : dam_amount=15; life=150; break;
			case INV_AMONITE :
			case AMONITE : dam_amount=5; life=60; break;
			case LAVA_ROCK : dam_amount=15; life=1; break;
			case BIG_PLATFORM :
			case PLATFORM : life=1; break;
			case SKULL_DOG : dam_amount=15; life=40; break;
			case GOBLIN : dam_amount=10; life=10; break;
			case EGG : dam_amount=30; life=1; break;
			case SENTINEL : dam_amount=25; life=300; break;
			case BONE_JAIL : dam_amount=30; life=1; break;
			case ROCK_HEADL :
			case ROCK_HEADR : dam_amount=15; life=1; break;
		}	
		
	}
	
	public void update(CerberusWorld w)
	{
		int aux;
		boolean hold_pl=false;
		
		cnt++;		
		dam_time++;
		if(w.pl.damage.collide(body) && w.pl.can_damage && !dead && dam_time>10)
			damage(w);
			
		// Bullet update
		if(bul_on){
			bulx+=(bulvel*Trig.cos(bulang))>>10;
			buly+=(bulvel*Trig.sin(bulang))>>10;
			if(bulx<w.bx-30 || bulx>w.bx+196 || buly<w.by-30 || buly>w.by+174)
				bul_on=false;				
		}
		
		switch(type){
			
			// SWAMP Enemies
						
			case TRITON : 
			if(dead){
				if(cnt>15) out_of_world=true;	
			}else{
				if(frame==0 && Math.abs(w.pl.x-x)<40 && !dead){
					frame++; cnt=0;
				}
				if(frame>0 && cnt%2==0 && frame<15) frame++;
				if(frame>=15) frame=0;
				if(frame>=15 && dead) out_of_world=true;
				switch(frame){
					case 1 : body.set(x-12,y-25,25,21); break;
					case 2 : body.set(x-12,y-62,24,48); break;
					case 3 : body.set(x-19,y-80,39,26); break;
					case 4 : body.set(x-19,y-80,43,24); break;
					case 5 : body.set(x-8,y-60,17,57); break;
					default : damage.unset(); body.unset(); break;					
				}			
				damage.x1=body.x1; damage.x2=body.x2; damage.y1=body.y1; damage.y2=body.y2;
				w.pl.enemy_damage(this);
			}
			break;			
			
			case SCORPION_FLY : 
			if(!dead){				
				if(dam_time>6){
					if(x>w.pl.x+60) dir=2;
					if(x<w.pl.x-60) dir=1;
					if(dir==2) x-=3;
					if(dir==1) x+=3;
					if(y>w.pl.y-30) y--;
					if(y<w.pl.y-40) y++;	
					frame=(frame+1)%4;
					y+=(Trig.sin((cnt*3)%256)*2)>>10;		
				}else{
					if(dir==2) x+=6;
					if(dir==1) x-=6;
					frame=2;					
				}
				damage.set(x-16,y-9,32,18);
				w.pl.enemy_damage(this);
				body.set(x-16,y-9,32,18);
				
			
				//System.out.println("("+x+","+y+")->"+body.x1+","+body.y1+"->"+body.x2+","+body.y2);											
			}else{
				if(w.pl.x<x) dir=2;
				else dir=2;
				if(dir==2) x-=5;
				if(dir==1) x+=5;
				vely++; y+=vely;
				frame=0;
				if(cnt>=10) out_of_world=true;				
			}
			break;	
			
			case UGLY_BALL :
			if(!dead){	
				frame=(cnt/3)%3;
				if(dam_time<8){				
					if(cnt%2==0) x++;
					else x--;					
				}else{						
					if(cnt%60<30){					
						if(x<w.pl.x) x++;
						if(x>w.pl.x) x--;
						if(y+30<w.pl.y) y++;
						if(y+30>w.pl.y) y--;									
					}					
					damage.set(x-14,y-14,28,28);
					w.pl.enemy_damage(this);
					body.set(x-12,y-12,24,24);
				}
												
			}else{
				vely++; y+=vely;
				if(cnt>=10) out_of_world=true;							
			}
			break;
			
			// CAVES Enemies
						
			case TURTLE_GOLEM : 
			if(!dead){
				if(dam_time<4)
					if(dir==2) x++;
					else x--;
				if(w.pl.x<x-20 && frame==0) dir=2;
				if(w.pl.x>x+20 && frame==0) dir=1;
				
				if(Math.abs(w.pl.x-x)<100 && Math.abs(w.pl.y-y)<50 && frame==0 && cnt%8==0)
					frame=1;
				
				if(frame>=1) frame++;
				if(frame>16) {frame=0; cnt=0;}
				body.set(x-20,y-40,35,50);
				
				if(frame<=8) aux=frame-1;
				else aux=8-(frame-8);	
				
				if(frame>0)
					if(dir==2) damage.set(x-17-12-8-(12*(aux-1)),y-40,20,15);
					else damage.set(x+17+10+(12*(aux-1)),y-40,20,15);					
				else damage.unset();
				w.pl.enemy_damage(this);				
				
			}else if(cnt>=20) out_of_world=true;												
			break;	
			
			case AMONITE :					
			if(!w.solid(w.tile_at(x,y+10))){
				y+=vely/2; vely++;			
			}else vely=0;
			if(dam_time<10 || dead);							
			else{				
				if(cnt%3==0) frame=(frame+1)%4; 
				if((cnt+45)%90==0)
				if(dir==1) dir=2;
				else dir=1;
				
				if(dir==1)					
					{
						x+=2;
						if(w.solid(w.tile_at(x,y-10))) 
							y=w.y_at_tile(x,y-10);
						else if(w.solid(w.tile_at(x,y)))
							 y=w.y_at_tile(x,y);							
						else if(w.solid(w.tile_at(x,y+10)))
							y=w.y_at_tile(x,y+10);							
					}
				if(dir==2)					
					{
						x-=2;
						if(w.solid(w.tile_at(x,y-10))) 
							y=w.y_at_tile(x,y-10);
						else if(w.solid(w.tile_at(x,y)))
							 y=w.y_at_tile(x,y);							
						else if(w.solid(w.tile_at(x,y+10)))
							y=w.y_at_tile(x,y+10);							
					}
				y-=4;
			}
			if(dead && cnt>=10) out_of_world=true;
							
			if(bul_on==false && Math.abs(w.pl.x-x)<60 && Math.abs(w.pl.y-y)<40){
				bulx=x; buly=y; bulvel=4; 	
				bulang=Trig.atan(w.pl.y-20-y,w.pl.x-x);
				bul_on=true;
			}else{
				damage.set(bulx-2,buly-2,4,4);
				w.pl.enemy_damage(this);				
			}
			body.set(x-16,y-15,32,20);
			break;
			
			case INV_AMONITE :					
			if(dam_time<10 || dead){
				type=AMONITE;
			}else{	
				if(cnt%3==0) frame=(frame+1)%4; 			
				if(dir==1)
					if(w.solid(w.tile_at(x+10,y-10)) && !w.solid(w.tile_at(x+10,y+10))) x+=2;
					else dir=2;
				if(dir==2)
					if(w.solid(w.tile_at(x-10,y-10)) && !w.solid(w.tile_at(x-10,y+10))) x-=2;
					else dir=1;
			}
			if(dead && cnt>=10) out_of_world=true;
							
			if(bul_on==false && Math.abs(w.pl.x-x)<60){
				bulx=x; buly=y; bulvel=4; 	
				bulang=Trig.atan(w.pl.y-20-y,w.pl.x-x);
				bul_on=true;
			}else{
				damage.set(bulx-2,buly-2,4,4);
				w.pl.enemy_damage(this);				
			}
			body.set(x-16,y-15,32,20);						
			break;			
			
			case LAVA_ROCK :		
			if(cnt%64<32)			
			damage.set(x-8,y-8-(70*Trig.sin(4*((cnt%64)%32))>>10),16,16);			
			else damage.unset();
			w.pl.enemy_damage(this);				
			break;
			
			case PLATFORM :
			case BIG_PLATFORM :			
			if(((Math.abs(w.pl.x-x)<13 && type==PLATFORM) || (Math.abs(w.pl.x-x)<23 && type==BIG_PLATFORM))
				&& Math.abs(w.pl.y-(y+8))<20 && w.pl.state!=CerberusSamuel.JUMP2){
				w.pl.platform=true;		
				hold_pl=true;		
			}				
			if((cnt%128)<64) x+=2;
			else x-=2;
			if(hold_pl){				
				if((cnt%128)<64) w.pl.move(w,w.pl.realx+200,y*100,1);		
				else w.pl.move(w,w.pl.realx-200,y*100,2);
				/*if((cnt%128)<64) w.pl.x+=2;
				else w.pl.x-=2;
				w.pl.y=y;
				w.pl.realx=w.pl.x*100;
				w.pl.realy=w.pl.y*100;*/												
			}			
			break;
			
			// Forest enemies
			case SKULL_DOG :			
			if(dead){
				if(cnt==20) out_of_world=true;	
								
			}else if(dam_time<8){
				if(w.pl.x<x) dir=2; else dir=1;
				if(dir==1) x-=5;
				if(dir==2) x+=5;				
			}else{
				if(dir==1) x+=5;
				if(dir==2) x-=5;
				//if(x<w.pl.x-80) dir=1;
				//if(x>w.pl.x+80) dir=2;
				if(dir==1 && (w.solid(w.tile_at(x+10,y)) || !w.solid(w.tile_at(x+10,y+32))))
					dir=2;
				if(dir==2 && (w.solid(w.tile_at(x-10,y)) || !w.solid(w.tile_at(x-10,y+32))))
					dir=1;					
				body.set(x-19,y,38,24);
				damage.set(x-19,y,38,16);
				w.pl.enemy_damage(this);	
			}
			break;
			
			case GOBLIN :
			if(dead){								
				y+=cnt; 								
			}else if(frame==0){	
				body.set(x-8,y-11,17,22);			
				if(Math.abs(w.pl.x-x)<40) frame++;				
			}else if (frame<4){
				y+=3;
				frame++;	
				if(frame==4) cnt=31;
			}else{
				if(dir==1) x+=2;				
				if(dir==2) x-=2;
				if(y>w.pl.y-10) y-=2;
				if(y<w.pl.y-60) y+=2;
				if(cnt%32==0){
					if(x>w.pl.x) dir=2;
					else dir=1;				
					vely=Math.abs(CerberusMain.rnd.nextInt())%30;
				}
				body.set(x-15,y+((vely*Trig.sin((cnt%32)*16))>>10),30,21);
				damage.set(x-7,y+((vely*Trig.sin((cnt%32)*16))>>10),15,21);
				w.pl.enemy_damage(this);
			}
			break;
			
			case EGG :
			if(dead){
				if(cnt>15) out_of_world=true;
			}else if(frame==0){
				body.set(x-4,y-5,8,10);
				if(Math.abs(w.pl.x-x)<40 && Math.abs((w.pl.y-20)-y)<40)
					frame++;	
			}else{
				frame++;
				if(frame==4) {damage.set(x-20,y-10,40,20); w.pl.enemy_damage(this);};	
				if(frame>4) {cnt=0; dead=true;}
			}
			break;
			
			case SENTINEL :
			if(dead){
				y+=cnt; 									
				if(cnt>20) out_of_world=true;
			}else
			if(cnt%60<40)				
				switch((cnt/60)%4){
					case 0 : x+=2; break;
					case 1 : y--; break;
					case 2 : x-=2; break;
					case 3 : y++; break;
				}
			if(frame==0 && Math.abs(w.pl.y-30-y)<20 && Math.abs(w.pl.x-x)<60) frame=1;
			if(frame>0) frame++; if(frame>=8) frame=0;
			if(frame==0)
				damage.set(x-15,y,30,25);
			else    damage.set(x-40,y+10,80,12);
			w.pl.enemy_damage(this);
			body.set(x-15,y,30,25);
			break;
			
			case BONE_JAIL :
			if(Math.abs(w.pl.y-y)<20 && Math.abs(w.pl.x-x)<20 && frame==0) frame++;
			else if(frame>0) frame++;
			if(frame>=20) frame=0;
			if(frame==12) damage.set(x-20,y-25,40,25);
			else damage.unset();
			w.pl.enemy_damage(this);			
			break;
			
			case ROCK_HEADR :
			if(cnt%60==3){
				bulx=x; buly=y+14; 	
			} else bulx-=4;
			damage.set(bulx-7,buly-4,14,8);
			w.pl.enemy_damage(this);			
			break;
			
			case ROCK_HEADL :
			if(cnt%60==3){
				bulx=x+22; buly=y+14; 	
			} else bulx+=4;
			damage.set(bulx-7,buly-4,14,8);
			w.pl.enemy_damage(this);			
			break;			
		}	
	}
	
	public void damage(CerberusWorld w)
	{
		life-=w.pl.dam_amount; dam_time=0; w.pl.can_damage=false;
		if(life<=0){ cnt=0; dead=true; w.add_random_item(x,y);}				
	}


}