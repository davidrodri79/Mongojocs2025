package com.mygdx.mongojocs.hellgates;
/*===================================================================
  HellGates MIDP2.0 
  -------------------------------------------------------------------
  Programmed by David Rodriguez 2004
  ===================================================================*/

public class player extends character {

	public static final int PL_STAND = 0, 
							PL_WALK = 1, 
							PL_JUMP = 2, 
							PL_FALL = 3, 
							PL_STAND_SLASH = 4, 
							PL_STAND_PAIN = 5, 
							PL_CROUCH = 6, 
							PL_CROUCH_PAIN = 7, 
							PL_FALL_PAIN = 8, 
							PL_KICK = 9, 
							PL_RUN = 10, 
							PL_FALL_SLASH = 12, 
							PL_SLIDE = 13, 
							PL_STAND_CHARGE = 15, 
							PL_FALL_CHARGE = 16, 
							PL_STAND_WHIP = 17,
							PL_FALL_WHIP = 18,
							PL_DEAD = 100;
							
	public static final int WE_KATANA = 0,
							WE_WHIP = 1,
							WE_BOOMERANG = 2,
							WE_NUNCHAKU = 3;							
							
	public static final int Gravity = 400;
	
	public int state, lastState, velX, velY, invulCount, dobleKey, charge, swordCombo, consecJumps, weapon,
				attackDir;

	
	public player(int aX, int aY, level l)
	{
		lev = l; velY = 0; velX=0; setPos(aX,aY); invulCount = 0; dobleKey = 0; collidable = true;
		setState(PL_STAND); charge = 0; weapon = WE_WHIP;
	}
		
	public void setState(int s)
	{		
		cnt = 0; lastState = state; state = s; damage.unset();		
	}
	
	public void update(boolean up, boolean down, boolean left, boolean right, boolean fire,
					   boolean lastUp, boolean lastDown, boolean lastLeft, boolean lastRight, boolean lastFire)
	{		
		boolean charging = false;
		
		cnt++;
		if (invulCount > 0) invulCount--;
		if (dobleKey > 0) dobleKey--;
		
		switch(state)
		{
			
			case PL_STAND : 
			if(!atGround())
				setState(PL_FALL);			
			else{
				if(right) {dir = 1; if(dobleKey>0) setState(PL_RUN); else setState(PL_WALK);}
				if(left)  {dir = -1; if(dobleKey>0) setState(PL_RUN); else setState(PL_WALK);}						
				if(up && !lastUp) {velY=-1000; velX=700; setState(PL_JUMP); consecJumps = 0;}
				if(fire && !lastFire) {setState(PL_STAND_CHARGE); swordCombo = 0;}
				if(down) {dobleKey = 6; setState(PL_CROUCH); walk(0);}
			}	
			break;	
			
			case PL_WALK :
			if(!atGround())
				setState(PL_FALL);			
			else{
				dobleKey = 6; 
				if(right) {dir = 1; walk(512);}
				else if(left)  {dir = -1; walk(-512);}						
				else setState(PL_STAND); walk(0);				
			}
			break;
			
			case PL_RUN :
			if(!atGround())
				setState(PL_FALL);			
			else{				
				if(dir>0) walk(912);
				if(dir<0) walk(-912);						
				if(right && dir<0) setState(PL_STAND);				
				if(left && dir>0) setState(PL_STAND);				
				if(up && !lastUp) {velY=-3000; velX=1200; setState(PL_JUMP);  consecJumps = 0;}
				if(fire && !lastFire) {swordCombo = 2; setState(PL_STAND_CHARGE);}
			}
			break;
			
			case PL_SLIDE :
			if(!atGround())
				setState(PL_FALL);			
			else{				
				if(dir>0) walk(1300);
				if(dir<0) walk(-1300);										
				if(up && !lastUp) {setState(PL_STAND); walk(0);}
				if(cnt>=15) {setState(PL_CROUCH); walk(0);}
			}
			break;
			
			case PL_JUMP : 
			if(up && cnt<5) velY=-2000;
			if(fire && !lastFire) setState(PL_FALL_CHARGE);						
			jumpOrFalling(false);						
			if(velY>=0) setState(PL_FALL);
			break;
						
			case PL_FALL :			
			if(fire && !lastFire) setState(PL_FALL_CHARGE);			
			if(up && !lastUp) {
				velY=-1000; velX=700; 
				if(left) dir = -1;
				if(right) dir = 1;
				setState(PL_JUMP); consecJumps++;
			}
			jumpOrFalling(true);
			break;
			
			case PL_FALL_CHARGE :			
			modifyCharge(5); charging = true;
			if(!fire) 
			switch(weapon){
				case WE_KATANA : setState(PL_FALL_SLASH); break;
				case WE_WHIP   : setState(PL_FALL_WHIP); break;
			}
			jumpOrFalling(true);
			break;
			
			case PL_STAND_CHARGE :
			modifyCharge(5); charging = true;
			if(!fire) 
			switch(weapon){
				case WE_KATANA : setState(PL_STAND_SLASH); break;
				case WE_WHIP   : setState(PL_STAND_WHIP); break;
			}
			break;
			
			case PL_STAND_SLASH :
			if(cnt==1) 
				switch(swordCombo)
				{
					case 0 : setDamage(10,-28,34,28); break;
					case 1 : setDamage(10,-32,34,28); break;
					case 2 : setDamage(10,-28,38,34); break;
				}									
			if(cnt==4) damage.unset();
			if(cnt>=4 && fire && !lastFire) {swordCombo = (swordCombo + 1) % 2; setState(PL_STAND_CHARGE);}
			if(cnt>=6) {setState(PL_STAND); walk(0);}
			break;
			
			case PL_STAND_WHIP :
			if(cnt<=2){
				if (cnt == 1) attackDir = 0;
				if (up) attackDir = 16;
				if (down) attackDir = 240;
			}
			if(cnt>2 && cnt<8) setDamage((((cnt-2)*12)*Trig.cos(attackDir))>>10,-32-((((cnt-2)*12)*Trig.sin(attackDir))>>10),34,28);								
			if(cnt==8) damage.unset();			
			if(cnt>=10) {setState(PL_STAND); walk(0);}
			break;
						
			case PL_FALL_SLASH :			
			jumpOrFalling(true);
			if(cnt>=1 && cnt<4) setDamage(10,-28,34,28);						
			if(cnt==4) damage.unset();
			else if(cnt>=5) setState(PL_FALL);						
			break;
			
			case PL_FALL_WHIP :
			jumpOrFalling(true);
			if(cnt<=2){
				if (cnt == 1) attackDir = 0;
				if (up) attackDir = 16;
				if (down) attackDir = 240;
			}
			if(cnt>2 && cnt<8) setDamage((((cnt-2)*12)*Trig.cos(attackDir))>>10,-32-((((cnt-2)*12)*Trig.sin(attackDir))>>10),34,28);								
			if(cnt==8) damage.unset();
			else if(cnt>=10) setState(PL_FALL);						
			break;
						
			case PL_STAND_PAIN :
			walk(-700*dir);
			if(cnt>=10) setState(PL_STAND);
			break;
					
			case PL_CROUCH :
			if(right) dir = 1;
			if(left)  dir = -1;
			if(fire && !lastFire) {setState(PL_KICK);}
			if(down && !lastDown)
				if(dobleKey>0) setState(PL_SLIDE);
			 	else dobleKey = 6;
			if(up && !lastUp) {setState(PL_STAND); walk(0);}
			break;
			
			case PL_KICK :
			if(cnt==3) setDamage(10,-4,24,12);
			if(cnt==6) damage.unset();
			if(cnt>=7) setState(PL_CROUCH);
			break;
			
			case PL_CROUCH_PAIN :
			if(!atGround()){
				setState(PL_FALL_PAIN);
			}else{
				walk(-500*dir);
				if(cnt>=10) setState(PL_CROUCH);			
			}
			break;
			
			case PL_FALL_PAIN :
			velX = -700;
			jumpOrFalling(true);
			break;
			
			case PL_DEAD :
			if(!atGround()){
				setRealPos(realX + velX*(dir),realY,8);		
				setRealPos(realX,realY + velY,8);		
				velY += Gravity; if(velY>2048) velY = 2048;		
			}else walk(0);
			break;
			
		}
		
		byte t = (byte) lev.tileAt(x,y - 2);
		if(t == level.ITEM_TILE)
		{
			// Pick up item
			lev.setTile(x,y - 2,(byte)0);	
		}
		t = (byte) lev.tileAt(x,y - 26);
		if(t == level.ITEM_TILE)
		{
			// Pick up item
			lev.setTile(x,y - 26,(byte)0);	
		}
		
		if(!charging) modifyCharge(-3);
	
	}
	
	void jumpOrFalling(boolean checkFall)
	{
		if(setRealPos(realX + velX*(dir),realY,8)) velX = 0;		
		if(setRealPos(realX,realY + velY,8)) velY = 0;					
		velY += Gravity; if(velY>2048) velY = 2048;							
		if(checkFall)
			if(atGround()) {
				if(state == PL_FALL_CHARGE) setState(PL_STAND_CHARGE); 
				else setState(PL_STAND); 
				walk(0);
			}			
	}
	
	public void damageFromOther(enemy e[])
	{
		if(state != PL_STAND_PAIN && state != PL_DEAD && invulCount == 0)
		for(int i = 0; i<e.length; i++)
		if(body.collide(e[i].damage) && e[i].state < 100)
		{
			look(e[i].damage);			
			modifyLife(-20);			
			switch(state){
				default : setState(PL_STAND_PAIN); break;
				case PL_KICK :
				case PL_CROUCH : setState(PL_CROUCH_PAIN); break;
				case PL_FALL : 
				case PL_JUMP : setState(PL_FALL_PAIN); break;
			}
			if(life == 0) {velY = -2000; setState(PL_DEAD);}
			else {invulCount = 100;}
			return;			
		}	
	}
	
	public void damageFromProjectile(projectile p[])
	{
		if(state != PL_STAND_PAIN && state != PL_DEAD && invulCount == 0)
		for(int i = 0; i<p.length; i++)
		if(p[i] != null)
		if(body.collide(p[i].body))
		{
			look(p[i].body);			
			modifyLife(-p[i].damageAmount());			
			switch(state){
				default : setState(PL_STAND_PAIN); break;
				case PL_KICK :
				case PL_CROUCH : setState(PL_CROUCH_PAIN); break;
				case PL_FALL : 
				case PL_JUMP : setState(PL_FALL_PAIN); break;
			}
			if(life == 0) {velY = -2000; setState(PL_DEAD);}
			else {invulCount = 100;}
			return;			
		}	
	}
	
	public int width()
	{
		switch(state)
		{
			case PL_SLIDE : return 40;
			case PL_DEAD : return 48;
			default : return 20;
		}
	}
	
	public int height()
	{
		switch(state)
		{
			case PL_KICK :
			case PL_CROUCH_PAIN :			
			case PL_CROUCH : return 30;
			case PL_SLIDE : return 22;
			case PL_DEAD : return 22;
			default : return 48;
		}
	}
	
	public int maximumLife()
	{
		return 100;
	}
	
	public int maximumCharge()
	{
		return 25;
	}
	
	void modifyCharge(int d)
	{
		charge += d;
		if(charge<0) charge = 0;
		if(charge>maximumCharge()) charge=maximumCharge();
	}
}