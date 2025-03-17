package com.mygdx.mongojocs.clubfootball2005;


class goalKeeper extends ball
{
	
	static int influence = 32;		
	int tx, ty;
	int team;
	final static int FREE    = 0;
	final static int OWNBALL = 1;
	final static int FOLLOW  = 2;
	final static int WAIT    = 3;
	final static int KICKBALL = 4;
	final static int PALOMITA = 50;
	ball ball;
	
	final static int AREAIX = 90 - 18;
	final static int AREAFX = 90 + 18;
	final static int AREAY  =  10;
	soccerPlayer closestEnemyPlayer;
	int wait;
    //soccerPlayer myTeam[], otherTeam[];
    int attackDirection;
    int speed;
    	
	
	public goalKeeper(Game _ga)
	{	
	    super(12,12);
	    ga = _ga;	        	  
	    state = FREE;
	}
   
   
        
    public void activate(int _x , int _y, int _team, int goalSize)
    {
        if (_team == ga.TEAMA) {myTeam = ga.teamA; otherTeam = ga.teamB;}
        else {myTeam = ga.teamB;otherTeam = ga.teamA;}
        if (_team == ga.TEAMA) {myTeam = ga.teamA; otherTeam = ga.teamB;speed = soccerPlayer.BASERUNSPEED + 32 + (playerSpeed*26);}
        else {myTeam = ga.teamB;otherTeam = ga.teamA;speed = soccerPlayer.BASERUNSPEED + (playerSpeed*26);}
     
        team = _team;
        tx = _x;        
        if (ga.teamDown == team)
        {            
            attackDirection = 0;    
            ty = ga.groundHeight - _y;            
        }
        else
        {
            attackDirection = 4;                
            ty = _y;
        }      
        x = tx;
        y = ty;  
        fx = x << 8;
        fy = y << 8;
        direction = attackDirection;
    }        
                    
                    
    int minDist = 1000;
                    
    public void updateVars()
    {     
    	
    	//#ifdef FASTENGINE
 		int st = 6;
        if (timing%24 == 3) {st = 1;closestEnemyPlayer = otherTeam[0];minDist = ga.distance(otherTeam[0], this);}    
 		//int min = ga.distance(otherTeam[0], this);

         for (int i = st; i < Math.min(10, st+4);i++)
         {           
            int valor = ga.distance(otherTeam[i], this);
            if (valor < minDist)
            {
                minDist = valor;
                closestEnemyPlayer = otherTeam[i];
            }
         }         
    
    	//#else         
        closestEnemyPlayer = otherTeam[0];         
         
        int min = ga.distance(otherTeam[0], this);        
         
         for (int i = 1; i < 10;i++)
         {
            int valor = ga.distance(otherTeam[i], this);
            if (valor < min)
            {
                min = valor;
                closestEnemyPlayer = otherTeam[i];
            }
         }
         //#endif
         
    }                    
                    
        
	
	
	public void update(ball _ball)
	{	   
	       ball = _ball;
    	   timing++;
    	   
    	   int prex = x;
    	   int prey = y;
    	   
    	   //#ifdef FASTENGINE
    	   //#else
    	   if (timing%6 == 2) updateVars();
    	   //#endif
    	   
    	   int currentState = state;
    	   
    	   switch(state)
    	   {
    	        case FREE:    	  
    	        	//#ifdef FASTENGINE
    	            //#else
    	            if (timing%2 == 0)        
    	            //#endif
    	            {
    	            	
        	            sx = 0;sy = 0;
        	            int addx = 0;
        	            if (ball.x > x+20) addx = 10;
        	            if (ball.x < x-20) addx = -10;
        	            if (tx+addx < x-2) sx = -speed;
                     	if (tx+addx > x+2) sx = speed;
                     	if (ty < y-2) sy = -speed;
                     	if (ty > y+2) sy = speed;
                     	direction = lookAt(ball.x, ball.y);
                     
                    }                    
    	            if (Math.abs(ty - y) < 24 && Math.abs(y-ball.y) < ga.groundHeight/6 && Math.abs(ga.hgroundWidth-x) <= 38 && ga.distance(this, ball) <= influence)
    	            //&& ga.distance(this, ball) <= ga.distance(closestEnemyPlayer, ball)
    	                state = FOLLOW;    	            
    	            palomita();       
    	            break;
    	                	             
    	        case FOLLOW:
    	        	sx = 0;sy = 0;    	           
    	            if (ball.x < x-2) sx = -speed-32;
                    if (ball.x > x+2) sx = speed+32;
                    if (ball.y < y-2) sy = -speed-32;
                    if (ball.y > y+2) sy = speed+32;
                    normalize();                                     
                    direction = lookAt(ball.x, ball.y);           
                    if (ga.distance(this, ball) <= 3) takeBall();                        
                    else
                    {
                    		//if (ga.distanceY(this, ball) > influence) state = FREE;         
                    		if (Math.abs(y - ball.y) > influence) state = FREE;         
                    		if (Math.abs(ty - y) > 24 || Math.abs(ga.hgroundWidth-x) > influence || Math.abs(ga.hgroundWidth-x) > 38) state = FREE;         
                    		palomita();                           
                    }
                    break;
                    
    	        case OWNBALL:
    	            ga.humanSoccerPlayer = null;
    	            ball.theOwner = this;
    	            sx = 0;sy = 0;
    	            if (x < AREAIX) sx = 256;
    	            if (x > AREAFX) sx = -256;
    	            if (y > AREAY && ga.teamDown != team) sy = -256;
    	            if (y < ga.groundHeight - AREAY && ga.teamDown == team) sy = 256;
    	            if (--wait <= 0) state = KICKBALL;
    	            direction = attackDirection;
    	            ball.setPosition(x-(cos((direction+2)*32)>>9),y-(sin((direction+2)*32)>>9));
    	            break;
    	        case KICKBALL:    
    	            ball.kick(attackDirection, 8*256, ga.rnd.nextInt()%512);
    	            sx = 0;sy = 0;
    	            //state = WAIT;
    	            //wait = 6;
    	            break;
    	        case WAIT:                    
                    if (--wait <= 0) state = FREE;
                    break;     
                case PALOMITA:
                    if (ga.distance(this, ball) <= 3) takeBall();
                    //direction = lookAt(ball.x, ball.y);    
                    //if (direction >= 4 ) direction = 6;
                    //else direction = 2;                    
                    if (--wait <= 0) state = FREE;
                    break;    
/*               case soccerPlayer.PREPENALTY:   
                    int defendDirection;
                    if (ga.strikeTeam != myTeam)
                    {                                    
                        defendDirection = 0;
                        attackDirection = 4;                
                        ty = 1;
                        tx = ga.hgroundWidth;
                    }
                    else { tx = ga.groundWidth-width;
                        ty = ga.hgroundHeight/2;      
                         defendDirection = 4; }
                    sx = 0;sy = 0;
    	            if (tx < x-1) sx = -soccerPlayer.BASERUNSPEED;
                    if (tx > x+1) sx = soccerPlayer.BASERUNSPEED;
                    if (ty < y-1) sy = -soccerPlayer.BASERUNSPEED;
                    if (ty > y+1) sy = soccerPlayer.BASERUNSPEED;   
                     normalize();                                                             	                  
                    direction = defendDirection;
                    if (Math.abs(tx - x) < 2 && Math.abs(ty - y) < 2)   
                    {
                        x = tx;y = ty;                 
                        if (ga.strikeTeam != myTeam) {wait = -10;}
                        direction = attackDirection;
                        if (myTeam != ga.strikeTeam && ball.sy != 0) state = soccerPlayer.PENALTY;
                        if (myTeam != ga.strikeTeam && ball.sy != 0) state = soccerPlayer.PENALTY;           
                    }
                    break;
               case soccerPlayer.PENALTY:
                    if (ga.distance(this, ball) <= 3)  takeBall(); 
                    if (ga.distanceY(this, ball) < 24)
                    {
                        sx = 0;sy = 0;
        	            if (ball.x < x-2) sx = -speed;
                        if (ball.x > x+2) sx = speed;
                        if (ball.y < y-2) sy = -speed;
                        if (ball.y > y+2) sy = speed;   
                        normalize();                                     
                    }
                    direction = lookAt(ball.x, ball.y);           
                    palomita();       
                    
                    if (timing == 1)
                    {                        
                        int i = ga.rnd.nextInt()%32365;
                        int k = playerTack * 1000;
                        if (team == ga.TEAMB) k -= 5000;                        
                        if (i > k)
                        {
                            state = PALOMITA;
        	                if (i%256 > 0)
        	                {
        	                     sx = soccerPlayer.BASERUNSPEED+256;
        	                     direction = 2;
        	                }
        	                else {sx = -soccerPlayer.BASERUNSPEED-256;direction = 6;}
        	                sy = 0;
        	                wait = Math.min(8, Math.max(4, i%8));
    	                }
	                }                    
                    break;*/
    	   }

    	   
    	   fx += sx;
           fy += sy;
           x = fx>>8;                            
    	   y = fy>>8;                            
           if (currentState != state) timing = 0;
           //System.out.println(""+state);
          
	}
	
	
	
	/*public int lookAt(int lx, int ly)
	{
	    return calculateDirection(0, x, y, lx, ly);
	}*/
	
	public void palomita()
	{
	   if (ga.teamDown == team && ball.y < ga.groundHeight-ga.groundHeight/4) return;
	    if (ga.teamDown != team && ball.y > ga.groundHeight/4) return;
	    if (ball.x < ga.groundWidth/4 || ball.x >= ga.groundWidth - ga.groundWidth/4) return;
	    //if (ga.matchState == ga.GAME && team == ga.TEAMB && ga.rnd.nextInt()%256 < -128-(soccerPlayer.CHI*2)) return;
	    if (ga.matchState == ga.GAME && team == ga.TEAMB && ga.rnd.nextInt()%256 < -(playerTack*25)+64) return;
	    if (ga.matchState == ga.GAME && team == ga.TEAMA && ga.rnd.nextInt()%256 < -playerTack*25) return;    
	        
	    if (Math.abs(ball.sy) > 256 && Math.abs(y - ball.y) < 16)
	    {
	        if ((ga.teamDown == team && ball.sy > 0)
	        || (ga.teamDown != team && ball.sy < 0))
	        {
	            int i = 0;
	            int tbx = ball.fx;
	            int tby = ball.fy;
	            //System.out.println("calculando");
	            while(i < 16)
	            {
	                tbx += ball.sx;
	                tby += ball.sy;
	                if ((ball.sy > 0 && tby>>8 >= y)
	                || (ball.sy < 0 && tby>>8 <= y))
	                    break;	                
	                //if (tby < 0 || tby >= ga.groundHeight<<8) break;
	                else i++;
	            }	            
	            if (i == 16) return;
	            if (Math.abs(x - (tbx>>8)) >= (i*speed)>>8)
	            // && Math.abs(x - tbx) > (soccerPlayer.RUNSPEED*3)>>8)
	            {
	                //System.out.println("palomita");
	                state = PALOMITA;
	                if (tbx>>8 > x){
	                     sx = soccerPlayer.BASERUNSPEED+256;
	                     direction = 2;
	                }
	                else {sx = -soccerPlayer.BASERUNSPEED-256;direction = 6;}
	                sy = 0;
	                wait = Math.min(8, Math.max(4, i));
	            }
	        }
	        
	    }

	}
	

			
	public void takeBall()
	{
		//if (ga.rnd.nextInt()%256 < playerTack*30) return;
	    ball.sx = 0;
	    ball.sy = 0;
	    ball.fx = x<<8;
	    ball.fy = y<<8;
	    ball.h = 128;
	    ga.strikeTeam = myTeam;
	    wait = 30;
	    if (ball.theOwner != null) ball.theOwner.state = soccerPlayer.FREE;
	    ball.theOwner = this;
	    state = OWNBALL;	    
	}
	
	/*
	public void followBall()
	{
	    state = FOLLOW;
	}*/
	
		
    public int isBallClose()
    {
        if (ga.distance(this,ball) < influence) return ga.distance(this, ball);       
        return ga.INFINITE;
    }
	
	
	
   


}