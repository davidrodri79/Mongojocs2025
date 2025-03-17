package com.mygdx.mongojocs.sliddisk;

public class Ball
{
	public int realx, realy, x, y, velx, vely, cnt, speed;
	public boolean allowMove;
	Table tab;

	public Ball(Table t,int level)
	{
		tab=t;
		reset(false,level);
	}
	
	public void reset(boolean up, int level)
	{		
		realx = 64<<8; realy = 64<<8;
		speed = (level < 10 ? 3 : 3+(level-10));  
		setBehav((up ? 32 : 32+128),0);
		cnt = 0; allowMove=true;
		
	}
	
	public void setBehav(int angle, int sp)
	{
		speed += sp;
		velx = (speed*Trig.cos(angle))>>2;		
		vely = (-speed*Trig.sin(angle))>>2;		
	}

	public void update(Pad p1, Pad p2)
	{
		cnt++;
		
		if(!allowMove) return;
		
		if(move(realx+velx, realy+vely, p1, p2))
			move(realx+velx, realy+vely, p1, p2);
				
		if(y>=tab.height && tab.downGoalTimer==0) {p1.score++; tab.downGoalTimer=40; allowMove=false;}
		if(y<0 && tab.upGoalTimer==0) {p2.score++; tab.upGoalTimer=40; allowMove=false;}
					
	}
	
	boolean move(int nx, int ny, Pad p1, Pad p2)
	{
		boolean collided = false;
		
		if(nx>=(tab.right<<8)){ velx=-velx; collided=true;}
		else if(nx<(tab.left<<8)){ velx=-velx; collided=true;}
		
		else if(ny>=tab.height<<8 && (Math.abs((nx>>8)-(tab.width/2))>=16)){
				vely=-vely;	collided=true;			
		}
		else if(ny<0 && (Math.abs((nx>>8)-(tab.width/2))>=16)){
				vely=-vely; collided=true;			
		}		
		
		else if (vely<0 && padCollide(p1,nx,ny)) {
			
			setBehav(192+3*(x-p1.x),(p1.power/8));
			p1.power = 0;
			collided=true;
		}
		else if (vely>0 && padCollide(p2,nx,ny)) {
			
			setBehav(64+3*(p2.x-x),(p2.power/8));
			p2.power = 0;
			collided=true;
			GameCanvas.VibraSET(200);
		}
		else {realx=nx; realy=ny;}
				
		x=realx>>8; y=realy>>8;				
		
		return collided;
	}
	
	boolean padCollide(Pad p, int nx, int ny)
	{
		return (Math.abs((nx>>8)-p.x)<=(Pad.PLENGTH)/2 && Math.abs((ny>>8)-p.y)<=7);
	}
}