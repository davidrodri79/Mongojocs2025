package com.mygdx.mongojocs.cerberus;


public class CerberusBoss extends CerberusEnemy{

	public static final int WIDOW=100;
	public static final int VIPER=101;
	public static final int BIRD=102;
	public static final int CERBERUS=103;
	
	public int claw, rarm, larm;
	
	public CerberusBoss(int xx, int yy, int t)
	{
		
		x=xx; y=yy; type=t; dir=2; 
		dead=false; out_of_world=false; dam_time=9999; vely=0;
		body=new CerberusSquare();
		damage=new CerberusSquare();
		
		switch(type){
			
			case WIDOW : dam_amount=20; life=320; break;
			case VIPER : dam_amount=15; life=250; break;
			case BIRD : dam_amount=15; life=300; buly=1000; break;
			case CERBERUS : dam_amount=15; life=1000; frame=1; larm=0; rarm=0; break;
		}	
		
	}
	
	public void update(CerberusWorld w)
	{
		int aux, ax, ay;
		
		cnt++;		
		dam_time++;
		if(w.pl.damage.collide(body) && w.pl.can_damage && !dead)
			damage(w);
		
		switch(type){
			
			// SWAMP Enemies
			
			case WIDOW :
			// No attack
			
			body.set(x,y+48,14,20);	
			if(dead){				
				//y-=2; 				
				if(cnt>=90) out_of_world=true;
			}else if(dam_time<10) y-=2;		
			else if(frame==0){
				if(y<0) y++;
				else
				if(Math.abs(w.pl.x-(x-26))<Math.abs(w.pl.x-(x+26))){
					// left claw	
					if(x-39>w.pl.x) x--;
					else if(x-13<w.pl.x) x++;
					else {frame=1; claw=1;} // Pinzazo				
				}else{
					if(x+13>w.pl.x) x--;
					else if(x+39<w.pl.x) x++;
					else {frame=1; claw=2;} // Pinzazo			
				}
			}else{
				if(cnt%2==0) frame++; if(frame==6) frame=0;
				if(frame==3){
					if(claw==1) damage.set(x-35,y+36,18,72);
					else if(claw==2) damage.set(x+17,y+36,18,72);
				}else damage.unset();
			}
			w.pl.enemy_damage(this);
			break;
			
			case VIPER :
			if(dead){
				if(cnt>=90) out_of_world=true;				
			}else{
				if(cnt>80){
					 cnt-=80;
					 if(dir==1) dir=2;
					 else dir=1;
					 if(dir==2) x=w.pl.x+60;
					 else x=w.pl.x-60;
				}
				for(int i=0; i<11; i++){
			        	aux=(cnt*3)-(8*i);
					if(aux<150){					
						if(dir==1) {ax=x-(60*Trig.cos(aux)>>10);}
						else {ax=x+(60*Trig.cos(aux)>>10);}
						ay=y-(120*Trig.sin(aux)>>10);
						if(i==0) body.set(ax-15,ay-15,30,30);
						else{
							damage.set(ax-12,ay-12,24,24);
							w.pl.enemy_damage(this);
						} 
					}	
				}			 			
			}
			break;			
			
			case BIRD :
			if(dead){
				if(cnt%2==0) vely++;
				y+=vely;	
				if(cnt>120) out_of_world=true;			
			}else if(dam_time<15){
				y-=2;
			}else{
				if(cnt%200>140){
					if(x<w.pl.x-4) x+=2;
					if(x>w.pl.x+4) x-=2;				
					y--;
					if(cnt%15==0) {
						frame=4;
						bulx=x; buly=y-10; bulvel=0;
					}
								
				}else if(cnt%80>20){
					if(x<w.pl.x-20) x+=2;
					if(x>w.pl.x+20) x-=2;
					if(y<w.pl.y-80) y+=2;
				}
				if (frame>0) frame--;
				buly+=bulvel; bulvel++;
				if(cnt%200<140 && ((cnt/2)%4)==0)
					damage.set(x-15,y+20,30,15);
				else damage.unset();
				w.pl.enemy_damage(this);
				
				damage.set(bulx-4,buly-4,8,8);
				w.pl.enemy_damage(this);
				
				body.set(x-10,y-28,20,56);
			}			
			break;
			
			case CERBERUS :
			if(cnt%70<11){
				if((cnt/70)%4<2){
					if(cnt%2==0) frame++;
					x-=4;
				}else{
					if(cnt%2==0) frame--; if(frame<0) frame+=6;
					x+=4;				
				}
				
			}
			if((Math.abs(w.pl.x-(x-70))<20 || rarm==2) && cnt%6==0) rarm++;			
			if(rarm>=3) rarm=0;
						
			if((Math.abs(w.pl.x-(x-30))<20 || larm==2) && cnt%6==0) larm++;			
			if(larm>=3) larm=0;
			
			body.set(x-20,y-119,54,20);
			
			if(rarm==2){
				damage.set(x-103,y-63,47,50);				
				w.pl.enemy_damage(this);
			}
			
			if(larm==2){
				damage.set(x-56,y-63,47,50);				
				w.pl.enemy_damage(this);
			}		
			
			if((cnt%60==0 && life>800) || ((cnt+9)%60==0 && life>600) || ((cnt+18)%60==0 && life>400)){
				bulx=x-11; buly=y-118; bulvel=8; 	
				bulang=Trig.atan(w.pl.y-20-(y-118),w.pl.x-(x-11));
				bul_on=true;
			}else if(bul_on==true){
				bulx+=(bulvel*Trig.cos(bulang))>>10;
				buly+=(bulvel*Trig.sin(bulang))>>10;
				damage.set(bulx-2,buly-2,4,4);
				w.pl.enemy_damage(this);								
				if(bulx<w.bx-30 || bulx>w.bx+196 || buly<w.by-30 || buly>w.by+174)
					bul_on=false;			
			}
				
			break;			
		}
			
	}
	
	public void damage(CerberusWorld w)
	{
		life-=w.pl.dam_amount; dam_time=0; w.pl.can_damage=false;
		if(life<=0){ cnt=0; dead=true;}				
	}


}