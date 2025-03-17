package com.mygdx.mongojocs.hellgates;/*===================================================================
  HellGates MIDP2.0 
  -------------------------------------------------------------------
  Programmed by David Rodriguez 2004
  ===================================================================*/

public class enemy extends character {

	public static final int TOAD = 1;
	public static final int SNAKE = 2;
	public static final int DEMON = 3;
		
	public static final int EN_STAND = 0;
	public static final int EN_SHOOT = 1;
	public static final int EN_PAIN = 2;
	public static final int EN_MOVE = 3;
	public static final int EN_JUMP = 4;
	public static final int EN_DEAD = 1000;
	
	public static final int Gravity = 512;
	
	public int state, velX, velY, type, originalX, originalY;
	boolean outOfWorld;

	public enemy()
	{
	
	}
	
	public enemy(int aX, int aY, int aType, level l)
	{
		lev = l; velY = 0; type = aType; setPos(aX,aY); originalX = x; originalY = y;
		outOfWorld = false;
		setState(0);
		
		switch(type)
		{
			case DEMON :
			case TOAD  : collidable = true; break;	
			case SNAKE : break;	
		}
	}
		
	public void setState(int s)
	{
		cnt = 0; state = s;
	}
	
	public void update(player pl, Game ga)
	{
				
		visible = (Math.abs(x - pl.x) < 200 && Math.abs(y - pl.y) < 200);
		if (!visible) return;
		
		cnt++;
		
		damage.unset();
		
		switch(type)
		{		
			case TOAD :
			switch(state)
			{				
				case EN_STAND : 
				look(pl.body);
				if(!atGround()){
					velX = 350;
					setRealPos(realX + velX*(dir),realY,8);		
					setRealPos(realX,realY + velY,8);		
					velY += Gravity; if(velY>2048) velY = 2048;		
				}else {
					walk(0);
					if(cnt >= 30 && Math.abs(pl.x - x) < 100) 
						setState(EN_SHOOT);
				}
				damage.copy(body);
				break;	
				
				case EN_PAIN : 		
				setState(EN_STAND);
				break;
				
				case EN_SHOOT :
				if(cnt == 7) ga.addProjectile(x+12*dir,y-16,projectile.SLIME_BALL,dir);
				if(cnt>=10) setState(EN_STAND);
				damage.copy(body);
				break;
				
				case EN_DEAD : 
				collidable = false;
				setRealPos(realX,realY + velY,24);
				velY+=Gravity;
				break;						
			}
			break;
			
			case SNAKE :
			switch(state)
			{				
				case EN_STAND : 								
				if(Math.abs(pl.x - x) < 40) 
					setState(EN_MOVE);								
				break;	
				
				case EN_MOVE :				
				setRealPos(realX,realY + (cnt<30 ? 2048 : 0));				
				damage.copy(body);
				break;				
				
				case EN_PAIN : 		
				setState(EN_STAND);
				break;
								
				case EN_DEAD : 								
				if(cnt > 10) outOfWorld = true;
				break;						
			}			
			break;
			
			case DEMON :
			switch(state)
			{				
				case EN_STAND : 	
				if(!atGround()) setState(EN_JUMP);
				else{
					look(pl.body);
					if(ga.RND(10) == 0) 
						setState(EN_MOVE);								
				}
				break;	
				
				case EN_MOVE :				
				if(Math.abs(pl.x - x) < 60)
				{
					velY=-3000; velX=750; setState(EN_JUMP);
				}
				else walk(512*dir);				
				damage.copy(body);
				break;				
				
				case EN_JUMP :
				jumpOrFalling(velY > 0);				
				damage.copy(body);
				break;				
				
				case EN_PAIN : 
				if(atGround()) walk(-1000*dir);		
				else {
					velX = -1000;
					jumpOrFalling(velY > 0);	
				}
				if(cnt > 10) setState(EN_STAND);
				break;
				
				case EN_DEAD : 								
				//jumpOrFalling(velY > 0);	
				if(cnt > 10) outOfWorld = true;
				break;						
			}
			break;
			
		}
						
		// Damage from player
				
		if(state!=EN_DEAD && state != EN_PAIN && body.collide(pl.damage))
		{					
			modifyLife(-20);	
			if(life==0) {lev.addItem(body.centerX(),body.centerY()); setState(EN_DEAD);} else setState(EN_PAIN);
		}
	
	}
	
	void jumpOrFalling(boolean checkFall)
	{
		setRealPos(realX + velX*(dir),realY,8);		
		setRealPos(realX,realY + velY,8);					
		velY += Gravity; if(velY>2048) velY = 2048;							
		if(checkFall)
			if(atGround()) {				
				setState(EN_STAND); 
				walk(0);
			}			
	}
	
	public int width()
	{
		switch(type)
		{
			default :
			case TOAD  : return 24;
			case SNAKE : return 16;
			case DEMON : return 32;
		}				
	}
	
	public int height()
	{
		switch(type)
		{
			default :
			case TOAD  : return 24;
			case SNAKE : return (state == EN_STAND ? 10 : (y - originalY)*2);
			case DEMON : return 28;
		}				
	}
	
	public int maximumLife()
	{
		switch(type)
		{
			default :
			case TOAD  : return 25;
			case SNAKE : return 10;
			case DEMON : return 50;
		}		
		
	}
}