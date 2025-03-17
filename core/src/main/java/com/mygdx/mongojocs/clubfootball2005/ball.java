package com.mygdx.mongojocs.clubfootball2005;

class ball
{	
	public int lookAt(int lx, int ly)
	{
	    return calculateDirection(0, x, y, lx, ly);
	}
	
	
		byte playerSkin;
	byte playerHead;
	byte playerSpeed;
    byte playerKick;
    byte playerPass;
    byte playerTack;
    String name;

    
    //======================================
	final static int W_KICK         = 0;
	final static int W_STEAL        = 1;
	final static int W_PENALTYKICK  = 2;
	final static int W_STEALFAILED  = 3;
	final static int W_OUT          = 4;
	//======================================
	
	
    int width;
	int height;
	int hwidth;
	int hheight;
  
    int x, y, fx, fy, sx, sy;
    //int cix,ciy,cfx,cfy;
    int timing;	
	int state;
	int direction;
	int wait;
	int waitReason;
	Game ga;
	soccerPlayer myTeam[], otherTeam[];
  
    public void wait(int _time, int reason)
	{
	    state = soccerPlayer.WAIT;
	    waitReason = reason;
	    wait = _time;
    }
  
    public ball(int _width, int _height)
    {
        width = _width;
        height = _height;
        hwidth = width/2;
	    hheight = height/2;
    }
  
    public void setPosition(int _x, int _y)
    {
        x = _x;
        y = _y;
        fx = x<<8;
        fy = y<<8;
    }
    
	public void setPlayerSkills(int _a[])
    {
    	playerSkin = (byte)_a[0];
        playerHead = (byte)_a[1];
        playerSpeed = (byte)_a[2];
        playerKick = (byte)_a[3];
        playerPass = (byte)_a[4];
        playerTack = (byte)_a[5];
    }    
    
    public int calculateDirection(int def, int prex, int prey, int x, int y)
	{
	    int dx = x-prex;
	    int dy = y-prey;
	    if (dx == 0 && dy == 0) return def;	    
	    int angle = atan(dy, dx);
	    int res = (((angle+16)/32) + 2) % 8;
	    //int res = (((angle+16) >> 5) + 2) & 0x0111;//% 8;
	    return res; 
	}
	
	public void normalize()
	{
	    if (sx != 0 && sy != 0)
	    {
	        sx = (7*sx)/10;
	        sy = (7*sy)/10;
	    }
	}
	  
  
  
  /*
   public boolean isCollidingWith(unit choc)
   {
   	    if (choc.x + choc.cix >= x+cix && choc.x + choc.cix <= x+cfx && choc.y + choc.ciy >= y+ciy && choc.y + choc.ciy <= y+cfy) return true;
        if (choc.x + choc.cfx >= x+cix && choc.x + choc.cfx <= x+cfx && choc.y + choc.ciy >= y+ciy && choc.y + choc.ciy <= y+cfy) return true;
        if (choc.x + choc.cix >= x+cix && choc.x + choc.cix <= x+cfx && choc.y + choc.cfy >= y+ciy && choc.y + choc.cfy <= y+cfy) return true;
        if (choc.x + choc.cfx >= x+cix && choc.x + choc.cfx <= x+cfx && choc.y + choc.cfy >= y+ciy && choc.y + choc.cfy <= y+cfy) return true;      
        return false;
   }


   public void setCollisionRectangle(int ox,int oy,int lx,int ly)
   {
      cix = ox;
      ciy = oy;
      cfx = ox+lx;
      cfy = oy+ly;
   }*/

	private static short sintable[]/*={
	0,25,50,75,100,125,150,175,199,224,248,273,297,321,344,368,391,414,437,460,482,504,
	526,547,568,589,609,629,649,668,687,706,724,741,758,775,791,807,822,837,851,865,
	878,890,903,914,925,936,946,955,964,972,979,986,993,999,1004,1008,1012,1016,1019,1021,
	1022,1023}*/;
	
	private static short costable[]/*={
	1024,1023,1022,1021,1019,1016,1012,1008,1004,999,993,986,979,972,964,955,946,936,
	925,914,903,890,878,865,851,837,822,807,791,775,758,741,724,706,687,668,649,629,
	609,589,568,547,526,504,482,460,437,414,391,368,344,321,297,273,248,224,199,175,
	150,125,100,75,50,25}*/;
	
	private static short tantable[]/*={
	0,25,50,75,100,126,151,177,203,229,256,283,310,338,366,395,424,453,484,515,547,580,
	613,648,684,721,759,799,840,883,928,974,1024,1075,1129,1187,1247,1312,1380,1453,
	1532,1617,1708,1807,1915,2034,2165,2310,2472,2654,2861,3099,3375,3700,4088,4560,
	5148,5901,6903,8302,10397,13882,20845,41719
	}*/;
	
	public static void initTables(GameCanvas gc)
	{		
		byte temp[]= new byte[384];
		
		//#ifdef J2ME
		gc.ga.loadFile(temp,"/Trig.bin");
		//#endif
				
		//#ifdef DOJA
		//#endif
				
		sintable=new short[64];
		costable=new short[64];
		tantable=new short[64];
		
		for(int i=0; i<64; i++){
		
			sintable[i]=(short)(((temp[i*2]&0xFF)<<8) | ((temp[i*2+1]&0xFF))); 		
			costable[i]=(short)(((temp[128+i*2]&0xFF)<<8) | ((temp[i*2+1+128]&0xFF))); 		
			tantable[i]=(short)(((temp[i*2+256]&0xFF)<<8) | ((temp[i*2+1+256]&0xFF))); 		
		}	
							
	} 	
			
	static int sin(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=sintable[a%64];
		else 	v=sintable[63-(a%64)];	
		if(a>=128) v=-v;
		return v;
	}

	static int cos(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=costable[a%64];
		else 	v=costable[63-(a%64)];	
		if((a>>6==1) || (a>>6==2)) v=-v;
		return v;
	}
	
	/*static int tan(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=tantable[a%64];
		else 	v=tantable[63-(a%64)];	
				
		if((a>>6)%2==1) v=-v;		
		return v;
	}*/

	
	static int atan(int s, int c)
	{
		int v,a=0;
		if(c!=0) v=(s<<10)/c;
		else v=99999;
		
		if(v<0) v=-v;
				
		while((tantable[a]<v) && (a<63)) a++;
		
		if(s>=0 && c>=0)
			return a;
		else if(s>=0 && c<0)
			return 128-a;
		else if(s<0 && c<0)
			return 128+a;		
		return (256-a)%256;		
	}	

	
	
	
	
	ball theOwner;
	int h;
	int gravity;
	
	public void init(Game _ga)
	{	
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
	    Game.gc.soundPlay(4,1);
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
	    
	    Game.gc.soundPlay(4,1);
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