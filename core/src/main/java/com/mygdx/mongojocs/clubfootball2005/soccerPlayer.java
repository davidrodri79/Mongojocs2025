package com.mygdx.mongojocs.clubfootball2005;


class soccerPlayer extends ball
{
	
	ball ball;
	int speed;                        
    //static int ENEMYSPEED;	
	
    
	//======================================
	final static int FREE           = 0;
	final static int OWNBALL        = 1;
	final static int FOLLOW         = 2;
	final static int WAIT           = 3;
	final static int PREMATCH       = 4;
	final static int STEAL          = 5;
	final static int PREOUT         = 6;
	final static int OUT            = 7;
	final static int PRECORNER      = 8;
	final static int CORNER         = 9;
	//final static int PREPENALTY     = 20;
	//final static int PENALTY        = 21;
	
	//======================================
	final static int BASERUNSPEED           = 256;
	//final static int BASERUNSPEEDWITHBALL   = (256-16)*2;
	int runSpeed           = 256*2;
	int runSpeedWithBall   = 256*2;
	final static int STEALSPEED         = 512+256;	
	//======================================
	//static int CHI;	
	//======================================	
	final static int influence      = 48;//64	
	int             team;
	int             id;
	soccerPlayer    bestPass;
	soccerPlayer    closestPlayer, closestUp, closestDown, closestLeft, closestRight;
	int             bestStatus;
	boolean         formed;
	int             myStatus;
	int             tmx, tmy;	
	int             tx, ty;
	int             formationPosX;
    int             formationPosY;        
    int             distEnemyPlayer;
	soccerPlayer    pass;                    
                        
		
	public soccerPlayer(Game _ga, int i, ball _ball)
	{	
	    super(12,12);
	    id = i;
	    ga = _ga;	        	  
	    ball = _ball;
	}
   
   
   
        
    public void activate(int _x , int _y, int _team)
    {
        //setState(PREMATCH);         
        state = PREMATCH;formed = false;
        if (_team == ga.TEAMA) {myTeam = ga.teamA; otherTeam = ga.teamB;speed = BASERUNSPEED + 32 + (playerSpeed*26);}
        else {myTeam = ga.teamB;otherTeam = ga.teamA;speed = BASERUNSPEED + (playerSpeed*26);}
       formationPosX = _x;
        formationPosY = _y;
        team = _team;
        if (ga.teamDown == team)
            y = (ga.hgroundHeight) - height;
        else    
            y = (ga.hgroundHeight) + height;       
            
        x = (id+1) * width;      
        fx = x << 8;
        fy = y << 8;        
    }        
        
    
   
    
    public int status(soccerPlayer player)
    {
    	//#ifdef FASTENGINE
    	//#else
    	
    	
         int result = 1000;
         int factor;
         //FACTOR: CERCANIA A LA PORTERIA (MAX: 90*2 = 180; MIN = 0)
         if (ga.teamDown == team)
         {
            factor = ga.groundHeight - y;
         }
         else
         {
            factor = y;
         }
         factor = (factor * 3000) / ga.groundHeight;         
         result += factor;                           
         
         //FACTOR: CERCANIA A LA PELOTA (MAX: 0 ; MIN = -ga.groundHeight*2)
         factor = -ga.distance(this, ball);
         factor = (factor * 1000) / (ga.groundHeight);
         result += factor;
         
         
         //FACTOR: CERCANIA AL CONTRARIO (MAX: ga.groundHeight*2: MIN = 0)
         int min = ga.distance(otherTeam[0], this);
         int min2 = ga.distance(myTeam[0], this);
         if (min2 == 0) min2 = ga.INFINITE;
         int min3 = ga.angle(this, myTeam[0]);         
   
         closestPlayer = myTeam[0];
         closestUp = null;
         closestDown = null;
         closestLeft = null;
         closestRight = null;
         
         byte minUp = (byte)min3;
         byte minDown = (byte)min3;
         byte minLeft = (byte)min3;
         byte minRight = (byte)min3;
         for (int i = 0; i < 10;i++)
         {
         	 //System.out.println ("*********");
            int valor = ga.distance(otherTeam[i], this);
            //if (valor == 0) {i++;continue;}
            
            if (valor > influence*2) valor = influence*2;
            min = Math.min(valor, min);
           
           
                int valor2 = ga.distance(myTeam[i], this);          
                if (valor2 != 0 && valor2 < min2)
                {
                    min2 = valor2;
                    closestPlayer = myTeam[i];
                }
                
                byte angle = (byte)ga.angle( myTeam[i], this);
               
                
                if (myTeam == ga.teamA && myTeam[i] != this)
                {
                    //if (myTeam[i].y < y-12) //ARRIBA
                    if (valor < 72)
                    {
                    		//if (this == ga.humanSoccerPlayer && i == 5) System.out.println (" angle5 "+angle );
                    		if (Math.abs(angle - ga.bestUp) < Math.abs(minUp - ga.bestUp))
                        {
                            minUp = angle;
                            closestUp = myTeam[i];
                        }
                    //}
                    //if (myTeam[i].y > y+12) //ABAJO
                    //{
                        if (Math.abs(angle - ga.bestDown) < Math.abs(minDown - ga.bestDown))
                        {
                            minDown = angle;
                            closestDown = myTeam[i];
                        }
                    //}
                    //if (myTeam[i].x < x-12) //IZQ
                    //{
                        if (Math.abs(angle - ga.bestLeft) < Math.abs(minLeft - ga.bestLeft))
                        {
                            minLeft = angle;//a.distanceY(this, myTeam[i]);
                            closestLeft = myTeam[i];
                        }
                    //}
                    //if (myTeam[i].x > x+12) //DERECHA
                    //{
                        if (Math.abs(angle - ga.bestRight) < Math.abs(minRight - ga.bestRight))
                        {
                            minRight = angle;
                            closestRight = myTeam[i];
                        }
                    }
                    if (closestUp == myTeam[0]) closestUp = closestDown;
                }
            //}
         }
         factor = min;
         if (state ==  OWNBALL)
            factor = (factor * 1000) / (ga.groundHeight);
         else 
            factor = (factor * 3000) / (ga.groundHeight*2);
         
         result += factor;
         //System.out.println(""+factor);
        
         
         //FACTOR RANDOM
         factor = ga.rnd.nextInt()%300;
         result += factor;
         //System.out.println("*"+result);
         
         
         //if (this == ga.humanSoccerPlayer) System.out.println (" minup "+minUp );
         return result;
         
         //#endif
    }
        
	
	public void goPlace()
	{
			//#ifdef FASTENGINE

			//#else
			
	        if (timing%4 == id/3)
	        {
                int kk;
                int at = 0;
                if (myTeam == ga.strikeTeam) at = 72;
                int target = ball.y;
                if (ball.theOwner != null && ball.theOwner instanceof goalKeeper) target = ga.hgroundWidth;
                if (ga.teamDown == team)
                {        
    	            tx = ga.groundWidth - formationPosX + ((ball.x-ga.hgroundWidth)>>2);            
                    kk = ((ga.groundHeight-target)*3)/2;                        
                    if (kk == 0) kk = 1;
                    if (kk > ga.hgroundHeight) kk = ga.hgroundHeight;
                    if (kk < 50) kk = 50;
                    ty = ga.groundHeight - ((((formationPosY*(160+at))/100) *  kk) /  (ga.hgroundHeight));                        
                }
                else
                {
                    tx = formationPosX+ ((ball.x-ga.hgroundWidth)>>2);            
                    kk = (target*3)/2;
                    if (kk == 0) kk = 1;
                    if (kk > ga.hgroundHeight) kk = ga.hgroundHeight;
                    if (kk < 50) kk = 50;
                    ty = ((((formationPosY*(160+at))/100) *  kk) /  (ga.hgroundHeight));
                }     
                
                tx = Math.max(hwidth, tx);
                tx = Math.min(ga.groundWidth-hwidth, tx);
            }
            //TEORICA POSICION MUY DIFETENTE QUE MI POSICION?
            int rand = ga.rnd.nextInt()%3;
            if (Math.abs(x-tx) > 4+(rand))
            {
                if (x > tx) sx = -speed;
                else if (x < tx) sx = speed;
            }
            else sx = 0;
            if (Math.abs(y-ty) > 4+(rand))
            {
                if (y > ty) sy = -speed;
                else if (y < ty) sy = speed;                    
            }              
            else sy = 0;      
            normalize();
            //#endif
	}
	
	public void update()
	{	   
	       //if (ball == null) ball = _ball;
    	   
    	   int prex = x;
    	   int prey = y;
    	   
    	   //myStatus = status(this);
    	   //if (myTeam == ga.teamA) System.out.println(myStatus);
    	   
    	   int currentState = state;
    	   //if (ga.teamDown == team) return;
    	   switch(state)
    	   {
    	        case FREE:    	            
    	            goPlace();    	            
                    break;
                    
                case FOLLOW:           
                    //#ifdef FASTENGINE
                    //#else
                    if (timing%3 == 0)
                    //#endif
                    {
                        sx = 0;sy = 0;
                        if (ball.x < x-2) sx = -speed;
                        if (ball.x > x+2) sx = speed;
                        if (ball.y < y-2) sy = -speed;
                        if (ball.y > y+2) sy = speed;      
                        normalize();                                                   
                    }
                    //#ifdef FASTENGINE
                    //#endif
                    
                    if (ga.distance(this, ball) <= 4 && ball.theOwner == null) takeBall();
                    else if (   ga.distance(this, ball) <= width*2 &&  
                                ball.theOwner != null && 
                                ball.theOwner.myTeam == otherTeam && 
                                ga.rnd.nextInt()%256 > 128-(playerTack*4))  
                                    steal();
                    //#ifdef FASTENGINE
                    //#endif                    
                    break;
                    
                case OWNBALL:        
                    if (ga.firstPass)
                    {
                        ball.kick(myTeam[8], 256*5, 0);   
                        lookAt(myTeam[8].x, myTeam[8].y);      
                        ga.firstPass = false;
                        /*ball.kick(closestPlayer, 512+256, 0);   
                        ga.firstPass = false;
                        setState(FREE);*/
                        //System.out.println("pase");
                        break;
                    }                   	
                    if (ga.distance(this, ball) > width) {state = FREE;break;}
                    ball.theOwner = this;
                    ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
                    bestPass = higherStatus(myTeam);
                    bestStatus = myStatus;                  
                    if (timing%4 == 0) bestStatus = moveStatus();                    
                    //System.out.println(bestStatus+" vs "+bestPass.myStatus);
                    if (distanceToGoal() < ga.groundHeight/4) bestStatus += 200;
                        if (bestStatus < bestPass.myStatus)
                        {                        
                             direction = lookAt(bestPass.x, bestPass.y);      
                             ball.kick(bestPass, 256+ga.distance(bestPass, this)*18, 20-(playerPass*2));                             
                             bestPass.wait(6, soccerPlayer.W_KICK);
                             //wait(6, W_KICK);
                             //state = WAIT;
                             //wait = 6;
                        }
                        else                     
                        {        	       
                            if (ga.rnd.nextInt()%256 > 128-(playerKick*4) && distanceToGoal() < ga.groundHeight/4)
                            {
                                if (ga.teamDown == team)                  
                                {           
                                    ball.kick(ga.pointer, 0, (playerKick*90) + 256*3, 0);                            
                                 	direction = 0;
                                }
                                else          
                                {                                                        
                                    ball.kick(ga.pointer, ga.groundHeight, (playerKick*90) + 256*3, 0);                            
                                    direction = 4;
                                }
                                //wait(6, W_KICK);    
                                //state = WAIT;
                                //wait = 6;                                              
                            }                           
                        }
                    
                    //ball.kick(8, 0, 256*16, 0);                                        
                    break;
                    
                case WAIT:               
                    if (wait <= 3) {sx = 0;sy = 0;}//W_STEALFAILED       
                    if (--wait <= 0) state = FREE;
                    break;
                    
                case PREMATCH:          
                        sx = 0;sy = 0;                                                
                        if (x < 0) sx = BASERUNSPEED*3;
                        else
                        {
                            int tty = formationPosY;
                            //if (ga.strikeTeam == myTeam) tty = (tty*120)/100;
                            if (ga.teamDown == team)
                            {                                            
                                tx = ga.groundWidth - formationPosX;            
                                ty = ga.groundHeight - tty;                        
                            }
                            else
                            {
                                tx = formationPosX;
                                ty = tty;
                            }
                            if (ga.strikeTeam == myTeam && (id == 8 || id == 9))
                            {
                                ty = ga.hgroundHeight;
                                if (id == 9) tx = ga.hgroundWidth;
                                if (id == 8 && Math.abs(tx - ga.hgroundWidth) < width) tx += width*2;                                
                            }
                            if (x > tx+2) sx = -BASERUNSPEED*3;
                            if (x < tx-2) sx = BASERUNSPEED*3;
                            if (y > ty+2) sy = -BASERUNSPEED*3;
                            if (y < ty-2) sy = BASERUNSPEED*3;        
                            //normalize();
                            if (Math.abs(tx - x) < 3 && Math.abs(ty - y) < 3)
                            {
                                 formed = true;                                 
                            }
                        }
                        break;
               case STEAL:                                 
                        if (ga.distance(this, ball) <= width && timing > 0)
                        {
                            if (ga.rnd.nextInt()%256 > 246- 64-(playerTack*6) )
                            {
                                if (ball.theOwner != null && ball.theOwner != this) 
                                {
                                    ball.theOwner.wait(10, W_STEAL);
                                    ball.theOwner.sx = 0;
                                    ball.theOwner.sy = 0;
                                }
                                takeBall();
                            }
                        }
                        if (ball.theOwner == this) ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));                 
                        state = STEAL;
                        if (--wait <= 0)
                        {
                            if (ball.theOwner == this) state = OWNBALL;
                            else {wait(7, W_STEALFAILED);}//state = FREE;                            
                        }                        
                        break;
               case PREOUT:
                        goPlace();
                        if (myTeam == ga.strikeTeam && id == 2)
                        {              
                            tx = ball.x <= 0?  ball.x - 4: ball.x + 4;
                            ty = ball.y;
                            sx = 0;
                            sy = 0;
                            if (x > tx+3) sx = -BASERUNSPEED*3;
                            if (x < tx-3) sx = BASERUNSPEED*3;
                            if (y > ty+3) sy = -BASERUNSPEED*3;
                            if (y < ty-3) sy = BASERUNSPEED*3;   
                            if (sx == 0 && sy == 0)
                            {
                                 x = tx;y = ty;
                                 state = OUT;
                                 ga.humanSoccerPlayer = this;
                            }
                        }
                        break;
                case OUT:
                        direction = lookAt(closestPlayer.x, closestPlayer.y);        
                        ball.kickOut(closestPlayer, 256+ga.distance(closestPlayer, this)*18);                                                     
                        ga.setTeamsState(FREE);                                
                        wait(6, W_OUT);
                        break;     
                case PRECORNER:
                        goPlace();
                        if (myTeam == ga.strikeTeam && id == 5)
                        {                                                     
                            tx = ball.x;
                            ty = ball.y;
                            sx = 0;
                            sy = 0;
                            if (x > tx+2) sx = -BASERUNSPEED*2;
                            if (x < tx-2) sx = BASERUNSPEED*2;
                            if (y > ty+2) sy = -BASERUNSPEED*2;
                            if (y < ty-2) sy = BASERUNSPEED*2;
                            normalize();  
                            if (sx == 0 && sy == 0)
                            {
                                 x = tx;y = ty;
                                 state = CORNER;
                                 ga.humanSoccerPlayer = this;
                            }
                        }
                        break;  
                case CORNER:
                        int destX = ga.groundWidth/2;
                        int destY = ball.y < ga.hgroundHeight ? (ga.groundHeight/7):ga.groundHeight-(ga.groundHeight/7);
                        direction = lookAt(destX, destY);        
                        ball.kick(destX, destY, ga.groundWidth*24, ga.rnd.nextInt()%8);    
                        ga.setTeamsState(FREE);    
                        //wait(6, W_KICK);                        
                        //state = WAIT;
                        //wait = 6;                                                                                   
                        break; 
                /*case PREPENALTY:
                        tx = ga.hgroundWidth;
                        ty = ga.hgroundHeight;                
                        if (myTeam == ga.strikeTeam && id == ga.nPP)                        
                            ty = (ga.groundHeight/8);                           
                        sx = 0;
                        sy = 0;
                        if (x > tx+1) sx = -BASERUNSPEED*2;
                        if (x < tx-1) sx = BASERUNSPEED*2;
                        if (y > ty+1) sy = -BASERUNSPEED*2;
                        if (y < ty-1) sy = BASERUNSPEED*2;                                                   
                        if (Math.abs(tx - x) <= 2 && Math.abs(ty - y) <= 2)
                        { 
                            formed = true;
                            x = tx;
                            y = ty;
                            sx = 0;
                            sy = 0;
                            //if (id == 8) System.out.println("formed");
                        }
                        break;
                        
                case PENALTY:         
                    formed = false;            
                    ball.theOwner = this;
                    ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
                    ball.kick(ga.pointer, 0, (playerKick*90) + 256*3, 0);                          
                    direction = 0;
                    break;*/
    	   }
    	   
    	   
    	   fx += sx;
           fy += sy;
           x = fx>>8;                            
    	   y = fy>>8;                            
           if (currentState != state) timing = 0;
           if (state == PREMATCH && formed)
           {
                if (ga.teamDown == team) direction = 0;else direction = 4;
           }
           if (state != WAIT && state != OUT)
           {              
               direction = calculateDirection(direction, prex, prey, x, y);       
           }
            //  direction = lookAt(ball.x, ball.y);       
           timing++;
    	   
	}
	
	
	
	
    public void manualUpdate(ball _ball, int keyX, int keyY, boolean keyShot)
	{
	    /*int prex = x;
	    int prey = y;
	    setPosition(x + keyX, y + keyY);	
	    if (ga.distance(this, ball) > width) {state = FREE;return;}
        ball.theOwner = this;
        ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
        direction = calculateDirection(direction, prex, prey, x, y);              */
        
 		if (ball == null) ball = _ball;
    	   
    	   int prex = x;
    	   int prey = y;
    	   
    	   //myStatus = status(this);
    	   //if (myTeam == ga.teamA) System.out.println(myStatus);
    	   
    	   int currentState = state;		
		
		   switch(state)
    	   {
				case FOLLOW:           
                    sx = 0;sy = 0;
                    sx = keyX*(speed);
                    sy = keyY*(speed);  
                    normalize();                                      
                    if (ga.distance(this, ball) <= 8 && ball.theOwner == null) takeBall();
                    if (keyShot) steal();
                    break;
                case OWNBALL:                    
                    if (ga.firstPass)
                    {
                    	Game.gc.vibraInit(50);
                        ball.kick(myTeam[8], 256*5, 0);   
                        lookAt(myTeam[8].x, myTeam[8].y);      
                        ga.firstPass = false;
                        //System.out.println("pase");
                        break;
                    }                         
                                if (ga.teamDown == team)                    	
                                {   
                                
                                    switch(direction)
                                    {
                                        case 7:case 0:case 1: pass = closestUp;break;
                                        case 2:case 3:pass = closestRight;break;
                                        case 4: pass = closestDown;break;
                                        case 5:case 6:pass = closestLeft;break;
                                    }                                                                    
                                }
                                else
                                {
                                    switch(direction)
                                    {
                                        case 0: pass = closestUp;break;
                                        case 1:case 2:pass = closestRight;break;
                                        case 3:case 4:case 5: pass = closestDown;break;                                                                                
                                        case 6:case 7:pass = closestLeft;break;
                                    }
                                    //if (direction >= 3 && direction <= 5) pass = closestUp;
                                    //else pass = closestDown;
                                }
                            if (pass == null) pass = closestPlayer;
                            
                	  sx = keyX*(speed+32);
                    sy = keyY*(speed+32);                    
                    normalize();
                    if (ga.distance(this, ball) > width) {state = FREE; break;}
                    ball.theOwner = this;
                    ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
                    //bestPass = higherStatus(myTeam);    	   	                                    
                
                                       
                        if(keyShot)
                        {
                         
                        	
                        	// SHOOT!!
                        	if (distanceToGoal() < ga.groundHeight/4)
                            {
                                if (ga.teamDown == team) 
                                {                                    
                                    Game.gc.vibraInit(50);
                                    ball.kick(ga.pointer, 0, (playerKick*90) + 256*3, 0);
                                }
                                else 
                                {
                                	Game.gc.vibraInit(100);
                                    ball.kick(ga.pointer, ga.groundHeight, (playerKick*90) + 256*3, 0);
                                    
                                }
                                
                            }
                            else
                            {
                            	Game.gc.vibraInit(50);
                                lookAt(pass.x, pass.y);      
                             	ball.kick(pass, 512+ga.distance(pass, this)*18, 20-(playerPass*2));
                        	}                                        	
                        }
                    
                    break;                    
				case STEAL:                                
                        if (ga.distance(this, ball) <= hwidth && ga.rnd.nextInt()%256 > 196-(playerTack*6))
                        {
                            if (ball.theOwner != null && ball.theOwner != this ) 
                            {
                                ball.theOwner.wait(11, W_STEAL);
                                ball.theOwner.sx = 0;
                                ball.theOwner.sy = 0;
                            }
                            takeBall();
                            Game.gc.vibraInit(75);
                        }
                        if (ball.theOwner == this) ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));                 
                        state = STEAL;
                        if (--wait <= 0)
                        {
                            if (ball.theOwner == this) state = OWNBALL;
                            else state = FOLLOW;                            
                        }
                        break;                    
				case WAIT:                    
                    if (--wait <= 0) state = FREE;
                    break;      
                /*case PENALTY:         
                    formed = false;            
                    ball.theOwner = this;
                    ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
                    if(keyShot){
                    	 ball.kick(ga.pointer, 0, 2048, 0);                                                                
                    	 Game.gc.vibraInit(50);
                    }
                    direction = 0;                   
                    break;*/
    	   }
		   fx += sx;
         fy += sy;
         x = fx>>8;                            
    	   y = fy>>8;                            
   
         if (currentState != state) timing = 0;
         
         if (state != WAIT)                       
             direction = calculateDirection(direction, prex, prey, x, y);       
         
         timing++;            	
	}
	
	
	
	
	
	

	
/*	public void setState(int st)
	{
	    switch (st)
	    {
	        //case PREPENALTY:
	        case PREMATCH:
	            formed = false;
	            break;	        
	    }
	    state = st;	    
	}
*/	
	
	
	public void steal()
	{	    
	    int dx = ball.x - x;
	    int dy = ball.y - y;
	    int angle = atan(dy, dx);	    
	    sx = ((cos(angle)>>2)*STEALSPEED)>>8;
	    sy = ((sin(angle)>>2)*STEALSPEED)>>8;	
	    wait = 4;   
	    state = STEAL;
	    //setState(STEAL);
    }

	
	
	public int distanceToGoal()
	{
	    if (ga.teamDown == team)    return y;            
        else      return ga.groundHeight - y;                    
	}
	
	
	
		
	
	public int moveStatus()
	{
	        sx = 0;sy = 0;
	        int tmpx = x;
	        int tmpy = y;	        
            x++;
             if (status(this) > myStatus) sx = speed-32;
            x--;
            if (status(this) > myStatus) sx = -speed-32;
            y++;
            if (status(this) > myStatus) sy = speed-32;
            y--;
            if (status(this) > myStatus) sy = -speed-32;
            int ret = status(this);
            int x = tmpx;
            int y = tmpy;                   
            return ret;
	}
	
	public soccerPlayer higherStatus(soccerPlayer team[])
	{
	    int thePlayer = -1;
        int maxStatus = -ga.INFINITE;
        int i = 0;
        while (i < 10)
        {
            if (team[i].state == soccerPlayer.FREE && team[i].myStatus > maxStatus)
            {
                    maxStatus = team[i].myStatus;
                    thePlayer = i;                
            }
            i++;
        }
        return myTeam[thePlayer];
    }
	
	
	
	
	public void takeBall()
	{
	    if (ball.theOwner == null || ball.theOwner.state != STEAL)
	    {
    	    state = OWNBALL;
    	    ball.sx = 0;
    	    ball.sy = 0;
    	    ball.h = 0;    	   
    	    ball.fx = x<<8;
    	    ball.fy = y<<8;
    	    ga.strikeTeam = myTeam;    	    
    	    ball.theOwner = this;
    	}
	}
	
	
	
	
      public int isBallClose()
    {
        if (ga.distance(this,ball) < influence) return ga.distance(this, ball);       
        return ga.INFINITE;
    }
	
	
	
   


}