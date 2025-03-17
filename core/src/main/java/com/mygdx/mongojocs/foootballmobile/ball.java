package com.mygdx.mongojocs.foootballmobile;

class ball extends unit
{	
	
	
	unit theOwner;
	int h;
	int gravity;
	
	public ball(Game _ga)
	{	
	    super(8,8);
	    ga = _ga;	   
	}
	
	
	      
    public void activate(int _x , int _y)
    {
        x = _x;
        y = _y;
        fx = _x<<8;
        fy = _y<<8;        
        sx = 0;
        sy = 0;
        h = 0;
        //state = FREE;
        if (theOwner != null) theOwner.state = soccerPlayer.FREE;
        theOwner = null;
    }        
	
	
	
	public void kick(int dir, int power, int desp)
	{
	    if (power > 256*5) power = 256*5;
	    
	    switch (dir)
	    {
	        case 0: sx = desp;sy = -power;break;
	        case 1: sx = power/2;sy = -power/2;break;
	        case 2: sx = power;sy = 0;break;
	        case 3: sx = power/2;sy = power/2;break;
	        case 4: sx = desp;sy = power;break;
	        case 5: sx = -power/2;sy = power/2;break;
	        case 6: sx = -power;sy = 0;break;
	        case 7: sx = -power/2;sy = -power/2;break;
	        
	    }
	    if (theOwner != null) theOwner.wait(6, soccerPlayer.W_KICK);
	    theOwner = null;
	    h = 0;
	    gravity = -power;
	}
	
	public void kick(soccerPlayer sp, int power, int desp)
	{
	    kick(sp.x, sp.y, power, desp);
	    Game.gc.soundPlay(3,1);
	    /*//System.out.println("pow"+power);
	    if (power > 256*6) power = 256*6;
	    int dx = sp.x - x;
	    int dy = sp.y - y;
	    int angle = atan(dy, dx);
	    angle += desp;
	    sx = ((cos(angle)>>2)*power)>>8;
	    sy = ((sin(angle)>>2)*power)>>8;	    	    */
	}
	
	public void kick(int _x, int _y, int power, int desp)
	{
	    //System.out.println("pow"+power);
	    if (power > 256*6) power = 256*6;
	    int dx = _x - x;
	    int dy = _y - y;
	    int angle = atan(dy, dx);
	    angle += desp;
	    sx = ((cos(angle)>>2)*power)>>8;
	    sy = ((sin(angle)>>2)*power)>>8;	   
	    if (theOwner != null) theOwner.wait(6, soccerPlayer.W_KICK);
	    theOwner = null; 	    
	    h = 0;
	    gravity = -power;
	    
	    Game.gc.soundPlay(3,1);
	}
	
	public void kickOut(soccerPlayer sp, int power)
	{
	    int _x = sp.x;
	    int _y = sp.y;
	    if (power > 256*6) power = 256*6;
	    int dx = _x - x;
	    int dy = _y - y;
	    int angle = atan(dy, dx);	    
	    sx = ((cos(angle)>>2)*power)>>8;
	    sy = ((sin(angle)>>2)*power)>>8;	   
	    theOwner = null; 	    
	    h = 256*3;
	    gravity = -power;
	}
	
	final int FRICTION = 36;
	
	
	public void update()
	{	   
    	   timing++;
    	   int prex = x;
    	   int prey = y;
           fx += sx;
           fy += sy;
           x = fx >> 8;
           y = fy >> 8;
           if (sx > 0) sx -= FRICTION;
           if (sx < 0) sx += FRICTION;
           if (sy > 0) sy -= FRICTION;
           if (sy < 0) sy += FRICTION;
           if (Math.abs(sx) < FRICTION && Math.abs(sy) < FRICTION) {sx = 0;sy = 0;}
           direction = calculateDirection(direction, prex, prey, x, y);    
           if (gravity < 1024) gravity += 256;
           h = Math.max(0, h-gravity);                          
	}
	
	
  
   
}