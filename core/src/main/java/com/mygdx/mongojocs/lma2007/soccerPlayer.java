package com.mygdx.mongojocs.lma2007;
//#ifndef REM_2DMATCH

//#ifdef NOSOUNDFX
//#define NOKICKSOUND
//#endif

//#define HEADKICKS


class soccerPlayer// extends unit
{
    //======================================
    //=== WAIT REASONS =====================
    //======================================    	
    final static int W_HEADKICK     = -1;
	final static int W_KICK         = 0;
	final static int W_STEAL        = 1;
	final static int W_PENALTYKICK  = 2;
	final static int W_STEALFAILED  = 3;
	final static int W_OUT          = 4;
	final static int W_PASS          = 5;
	final static int STEALFAILEDTICK = 8;
	//======================================
    //======================================
    // PLAYERS CHARACTERISTICS
    //======================================
    final static int BODYRADIUS		    = 8;
	final static int BODYWIDTH        	= 12;
	final static int BODYHEIGHT    	    = 12;	
	final static int BODYH       	  	= 24;
    final static int PLUSSPEED        	= 48;    
    final static int TAKEBALLDISTANCE	= 8;
    final static int BASERUNSPEED		= 356;	
	final static int STEALSPEED         = (256*3)+64;	
	final static int STEALDISTANCE      = 32;	
	final static int FREEKICKDISTANCE   = 48;	
	final static int INFLUENCE      	= 80;		
	//======================================
	// PLAYER STATES
	//======================================
    final static int JUMP               = -4;   
	final static int EJECTED           	= -3;
	final static int GOPLACE        	= -2;		
	final static int WAIT           	= -1;	
	final static int FREE           	= 0;
	final static int OWNBALL        	= 1;
	final static int FOLLOW         	= 2;
	final static int PREMATCH       	= 4;
	final static int STEAL          	= 5;
	final static int PREOUT         	= 6;
	final static int OUT            	= 7;
	final static int PRECORNER      	= 8;
	final static int CORNER         	= 9;		
	final static int PREPENALTY     	= 10;
	final static int PENALTY        	= 11;
	final static int WOUNDED        	= -5;		
	final static int KICKBALL 	  	    = 13;
	final static int PALOMITA 	  	    = 14;
	final static int PREGOALKICK  	    = -6;
	final static int PREGOALKICK2  	    = 15;
	//======================================
	// GROUND SIZES
	//======================================
	final static int GROUNDWIDTH    = GameCanvas.GROUNDWIDTH;
    final static int GROUNDHEIGHT   = GameCanvas.GROUNDHEIGHT;
    final static int HGROUNDWIDTH   = GROUNDWIDTH/2;
    final static int HGROUNDHEIGHT  = GROUNDHEIGHT/2;
    final static int AREAIX 			= (150 - 18);
	final static int AREAFX 			= (150 + 18);
	final static int AREAY  			= 40;	
	//======================================
	// GAME VARIABLES
	//======================================
	soccerPlayer            ball;
	int 			id;	
	int 			speed;                        
    int 			state;
    int 			wait;
    int 			waitReason;
    int 			attackDirection;    	
	int             team;	
	soccerPlayer    myTeam[], otherTeam[];
	soccerPlayer    bestPass;
	soccerPlayer    pass;
	soccerPlayer    closestPlayer, closestUp, closestDown, closestLeft, closestRight;
	soccerPlayer 	closestEnemyPlayer;	
	int             bestStatus;
	boolean         formed;
	int             myStatus;
	int             tx, ty;
	int             formationPosX;
	int             formationPosY;        
    //int             distEnemyPlayer;
	int 			angleShot;
	boolean 		freeKick; 
	int             numFaults;
	int 			moving;
	int 			dontTakeBall;
	boolean 		injured;
	boolean 		stateChanged;
	boolean 		sprint;
	boolean 		lastInMovement;
	//======================================
    // PLAYER ATRIBUTTES 
    //======================================
    String 		    name;    		
	byte 		    playerSpeed;
    byte 		    playerKick;
    byte 		    playerPass;
    byte 		    playerTack;
    //======================================
	// GRAPHIC VARIABLES
	//======================================
	int			    animSeq, animFrame;	  
	int 			pid;
	//======================================
    
    int lastState;
    soccerPlayer destPass;
    
    // David
//#ifndef FEA_CHAPAS	
    AnimatedSprite sprite;
//#endif
    boolean jumpUp;

/////////////////////////////////////////////////////////////////////////////	
// soccerPlayer soccerPlayer(com.mygdx.mongojocs.lma2007.GameCanvas _gc, int _id, ball _ball)
/////////////////////////////////////////////////////////////////////////////		
public soccerPlayer(GameCanvas _gc, int _id)
{	
//public unit(int _width, int _height, com.mygdx.mongojocs.lma2007.GameCanvas _gc)
//{
    /*width = BODYWIDTH;
    height = BODYHEIGHT;
    if (id == -1)
    {
        width = 8;
        height = 8;
        
    }
    hwidth = width/2;
    hheight = height/2;*/
    gc = _gc;    
//}  
    id = _id;
    //ball = _ball;
}	
	

/////////////////////////////////////////////////////////////////////////////	
// setPlayerSkills(int _skillArray[])
/////////////////////////////////////////////////////////////////////////////	
public void setPlayerSkills(String _name, int _skillArray[])//, boolean injured)
{
	//com.mygdx.mongojocs.lma2007.Debug.println("kick: "+_skillArray[2]/4);
	//com.mygdx.mongojocs.lma2007.Debug.println("pass: "+_skillArray[2]/4);
	//com.mygdx.mongojocs.lma2007.Debug.println("speed: "+_skillArray[2]/4);
	//com.mygdx.mongojocs.lma2007.Debug.println("tack: "+_skillArray[2]/4);

	// TODO: 
	// Que pasa si cambia de posicion solo...
	if (!_name.equals(name))
	{
		//System.out.println("NO: "+pid+" - "+name+":"+_name);
		//com.mygdx.mongojocs.lma2007.Debug.println("!!!!!!!1CAMBIO!!!");
		name = _name;
		playerKick = (byte)(_skillArray[0]/4);
		playerPass = (byte)(_skillArray[1]/4);
	   	playerSpeed = (byte)(_skillArray[2]/4);
	   	playerTack = (byte)(_skillArray[3]/4);
	    pid = _skillArray[4];
	   	numFaults = 0;
	   	dontTakeBall = 0;
	   	injured = false;
	   	
	}
	if (injured) speed = soccerPlayer.BASERUNSPEED / 2;
	
}    


/////////////////////////////////////////////////////////////////////////////	
// activate(int _x , int _y, int _team)
/////////////////////////////////////////////////////////////////////////////	  

public void activate(int _x , int _y, int _team, boolean _set)
{    
    if (_set && state != EJECTED)    
    {state = PREMATCH;formed = false;freeKick = false;}
	if (_team == GameCanvas.TEAMA) {myTeam = GameCanvas.teamA; otherTeam = GameCanvas.teamB;speed = BASERUNSPEED + (playerSpeed*26);}
	else {myTeam = GameCanvas.teamB;otherTeam = GameCanvas.teamA;speed = BASERUNSPEED + (playerSpeed*26);}
	
		formationPosX = _x;
		formationPosY = _y;
	
	team = _team;	
	if (GameCanvas.teamDown == team)
    {            
        attackDirection = 0;    
        ty = GROUNDHEIGHT - _y;            
    }
    else
    {
        attackDirection = 4;                
        ty = _y;
    }      
	if (_set
			&& state != EJECTED)
			
	{
	    y = relativeY((GameCanvas.HGROUNDHEIGHT) - height);  
	    x = (id+1) * width;      
	    fx = x << 8;
	    fy = y << 8;     
	    dontTakeBall = 0;
	}
}        
        
    
    
    int minEnemy;
   
/////////////////////////////////////////////////////////////////////////////	
// int status()
/////////////////////////////////////////////////////////////////////////////		    
public int status()
{
    int result = 1000;
    int factor;

    //FACTOR: CERCANIA A LA PORTERIA (MAX: 90*2 = 180; MIN = 0)
    if (GameCanvas.teamDown == team)
        factor = GROUNDHEIGHT - y;
    else
        factor = y;
    
    factor = (factor * (2300+GameCanvas.facCloseToGoal)) / GROUNDHEIGHT;         

    result += factor;                           
    
    //FACTOR: CERCANIA A LA PELOTA (MAX: 0 ; MIN = -GROUNDHEIGHT*2)
    factor = -distance(ball);
    /*factor = (factor * 1000) / (GROUNDHEIGHT);*/
    if (factor < STEALDISTANCE*3 || ball.theOwner == this)
    {
        factor = (factor * 1100) / (GROUNDHEIGHT);
        result += factor;
    }

    
    factor = 0;
    //NUEVO: DISTRIBUCCION
    //MEDIOCAMPO
    if (y > HGROUNDHEIGHT/2 && y < (HGROUNDHEIGHT*3)/2)
    {
    	//A UN LADO
    	if (x < HGROUNDWIDTH/2 || x > (HGROUNDWIDTH*3)/2)
    	{
    		factor += GameCanvas.facCloseToSide;
    	}
    }
    
    //result += factor;
     
    //FACTOR: CERCANIA AL CONTRARIO (MAX: GROUNDHEIGHT*2: MIN = 0)
    minEnemy = distance(otherTeam[0]);
    int min2 = distance(myTeam[GameCanvas.specialId[team][0]]);
    //myTeam[0]
    //, this);
    if (min2 == 0) min2 = GameCanvas.INFINITE;
   
    closestPlayer  = myTeam[GameCanvas.specialId[team][0]];
    //myTeam[0];
                  
    for (int i = 0; i < 10;i++)
    {   
        if (otherTeam[i].state == EJECTED) continue;               	
        int valor = distance(otherTeam[i]);
        //if (valor == 0) {i++;continue;}�1
            
        if (valor > INFLUENCE*2) valor = INFLUENCE*2;
        if (otherTeam[i].sy != sy)  minEnemy = Math.min(valor, minEnemy); //HACKKKKK
        //minEnemy = Math.min(valor, minEnemy);
              
        int valor2 = distance(myTeam[i]);          
        if (valor2 != 0 && valor2 < min2)
        {
            min2 = valor2;
            closestPlayer = myTeam[i];
        }                                                

    }
         
    factor = minEnemy;

    if (state ==  OWNBALL)
        factor = (factor * (1000 + GameCanvas.facCloseToEnemy)) / (GROUNDHEIGHT);
    else 
        factor = (factor * (3000 + GameCanvas.facCloseToEnemy)) / (GROUNDHEIGHT*2);
         
    result += factor;
         
    //FACTOR RANDOM
    //factor = com.mygdx.mongojocs.lma2007.GameCanvas.gfxMatchRnd.nextInt()%300;
    //result += factor;
                  
    return result;                 
}
        
	
public void goPlace()
{			
    if (timing%10 == id)
	//if ((timing&0x03) == id/3)
	{
   	    if (GameCanvas.teamDown == team)
	    {        
	        tx = GameCanvas.teamCenterX[team] - formationPosX;
	        ty = GameCanvas.teamCenterY[team] - ((formationPosY*GameCanvas.teamAtt[team])/100);//  (HGROUNDHEIGHT));                        
        }
	    else
	    {
	        tx = GameCanvas.teamCenterX[team] + formationPosX;
	        ty = GameCanvas.teamCenterY[team] + ((formationPosY*GameCanvas.teamAtt[team])/100);
	    } 
	    //ty = gc.teamCenterY[team] + ((formationPosY*gc.teamAtt[team])/100);
	    
	    if (GameCanvas.freeKick)
	    {
	        int kx = x;
	        x = tx;
	        int i = 0;
	        while (i < 5 && distance(ball) <= FREEKICKDISTANCE)
	        {
	            i++;
	            if (ball.x >= x) x -= STEALDISTANCE;
	            else x += STEALDISTANCE;
	        }	       
	        tx = x;
	        x = kx;
	    }
        tx = Math.max(hwidth, tx);
	    tx = Math.min(GROUNDWIDTH-hwidth, tx);
    }
	//TEORICA POSICION MUY DIFETENTE QUE MI POSICION?
	//int rand = gc.gfxMatchRnd.nextInt()%3;
	if (Math.abs(x-tx) > 4)
	{
		if (x > tx) sx = -speed;
		else if (x < tx) sx = speed;
	}
	else sx = 0;
	if (Math.abs(y-ty) > 4)
	{
		if (y > ty) sy = -speed;
		else if (y < ty) sy = speed;                    
	}              
	else sy = 0;      
	normalize();   
	//#ifndef NOSKIP
	if (gc.skip && sx != 0 && sy != 0 && state != FREE && gc.matchState != constants.WAITFAULT) {
	    fx = (tx<<8)-(sx*4);
	    fy = (ty<<8)-(sy*4);
	}
	//#endif
}
	



/////////////////////////////////////////////////////////////////////////////	
// closestToAngle()
/////////////////////////////////////////////////////////////////////////////	
soccerPlayer closestToAngle(int masterAngle, boolean dirx)
{
	int closestPlayerId = -1;
	int minAngle = 256;
		
	for (int i = 0; i < 10;i++)
    {
     	if (i == id) continue;
     	if (myTeam[i].state == EJECTED) continue;               	
    
     	if (dirx)
     	{
     		if (sx > 0 && myTeam[i].x < x) continue;
     		if (sx < 0 && myTeam[i].x > x) continue;
     	}
     	
     	int cAngle = masterAngle;         	
     	int angle = angle( myTeam[i]);         	
     	if (angle >= 128 && cAngle < 128)
     		cAngle += 256;
     	if (angle < 128 && cAngle >= 128)
     		angle += 256;         		         	         	
     		
     	int diff = Math.abs(cAngle-angle);
     	if (diff > 64 && diff < 192) continue;
     	diff += distance( myTeam[i])>>1;
     	if (diff < minAngle)
     	{
     	    /*if (diff == minAngle && closestPlayerId >= 0) {
     	        if (gc.distance(this, myTeam[i]) > gc.distance(this, myTeam[closestPlayerId])) {
     	            continue;
     	        }
     	    }*/
     		minAngle = diff;
     		closestPlayerId = i;
     	}         	
    }
    
    if (closestPlayerId == -1) return null;
    
    return myTeam[closestPlayerId];
}
	
	
/////////////////////////////////////////////////////////////////////////////	
// calculatePass()
/////////////////////////////////////////////////////////////////////////////	
/*public void calculatePass(){}*/
	
	
/////////////////////////////////////////////////////////////////////////////	
// update CPU()
/////////////////////////////////////////////////////////////////////////////		
public void update()
{	
	//if (injured) com.mygdx.mongojocs.lma2007.Debug.println("vel: "+playerSpeed);
	
	int prex = x;
    int prey = y;
    
    int prefx = fx;
    int prefy = fy;
    
    //int currentState = state;
   
    lastDirection = direction;
    
   //#ifdef HEADKICKS
    if (gravityVel < constants.MAXGRAVITY*6) gravityVel += constants.GRAVITY*6;
   	h -= gravityVel;
    //if (h > 0) h -= 4*256; 
    if (h <= 0) {h = 0; gravityVel = 0;}
    else {sx = 0;sy = 0;}
    //#endif	   
    	   
    if (numFaults >= 3)    state = EJECTED;
 
    if (dontTakeBall > 0) dontTakeBall--;
        
    switch(state)
    {
		case FREE:    	            
            freeKick = false;
		case PREGOALKICK:
    	    goPlace();    	            
            break;
                 
        case GOPLACE:  
            if (ball.theOwner != null) state = FREE;
            else if (ball.takeBall(this)) {GameCanvas.np = id;}
            if (wait < waitReason/4 || distance(ball) < STEALDISTANCE) {tx = ball.x; ty = ball.y;}
            sx = 0; sy = 0;
            if (tx < 8) tx = 8;
            if (tx > GROUNDWIDTH-8) tx = GROUNDWIDTH-8;
            if (tx < x-3) sx = -speed;
            if (tx > x+3) sx = speed;
            if (ty < y-3) sy = -speed;
            if (ty > y+3) sy = speed;    
            if (ball.theOwner == this) {sx = 0;sy = (attackDirection==0?-speed:speed);}
            
            wait--;     	              
            break;
                    
        case FOLLOW:           
            //if (1 == 1) break;
            if (timing%3 == 0)
            {
                sx = 64;sy = 64;
                if (ball.x < x-2) sx = -speed;
                if (ball.x > x+2) sx = speed;
                if (ball.y < y-2) sy = -speed;
                if (ball.y > y+2) sy = speed;
                normalize();
            }
            if (dontTakeBall > 0) break;        
            
            if (!ball.takeBall(this)) {
            
                if (ball.h>>8 < BODYH && distance(ball) <= STEALDISTANCE)
                {                    
                    if (ball.theOwner != null && ball.sy>>9 != sy>>9 &&
                    GameCanvas.gfxMatchRnd.nextInt()%256 > /*com.mygdx.mongojocs.lma2007.GameCanvas.facPress*/+196-(playerTack*5))
                        steal();
                //#ifdef HEADKICKS        
                } 
                else if (ball.h>>7 > BODYH)
                {
                    
                    if (Math.abs((ball.fx+(ball.sx*4)) -  fx) < 256*TAKEBALLDISTANCE &&
                        Math.abs((ball.fy+(ball.sy*4)) -  fy) < 256*TAKEBALLDISTANCE)                    
                        state = JUMP;
                //#endif    
                }
            }        
            break;
        //#ifdef HEADKICKS        
        case JUMP:
            sx = 0; sy = 0;
            if (ball.x < x) sx -= BASERUNSPEED;
            if (ball.x > x) sx += BASERUNSPEED;
            if (ball.y < y) sy -= BASERUNSPEED;
            if (ball.y > y) sy += BASERUNSPEED;
            //gc.marca = this;
            if (timing == 1) {gravityVel = -256*18;h = 1;}
            
            if (ball.takeBall(this))
            {
                //if (ball.y > y+(BODYH/2))
                {	                
	                if (distanceToGoal() < GROUNDHEIGHT/4 || closestPlayer.state == WAIT)
	                    ball.headKick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), (playerKick*90) + (256*10));                    
	                else
	                {
	                    ball.headKick(closestPlayer.x, closestPlayer.y, 256*100);                    //512+gc.distance(closestPlayer, this)*12
	                }
	                
	                direction = lookAt(GameCanvas.pointer, relativeY(GROUNDHEIGHT));
	                break;
                }
                
            }
            if (h == 0) state = FREE;
            //state = JUMP;
            break;
        //#endif            
        case OWNBALL:      
            if (GameCanvas.firstPass)
            {
            	gc.soundPlayEx(GameCanvas.SOUND_WHISTLE);
                int i = GameCanvas.specialId[team][0];
                //#ifndef NOHEIGHT    
                ball.kick(myTeam[i], 256*5, 10, false);   //myTeam[8]
                //#else
                //#endif
                direction = lookAt(myTeam[i].x, myTeam[i].y);      
                GameCanvas.firstPass = false;                
                break;
            }                   	
            if (ball.theOwner != this) {state = FREE;break;}
            ball.theOwner = this;
                    
            ball.setPosition(x-(cos((direction+2)*32)>>7),y-(sin((direction+2)*32)>>7));
                    
            bestPass = higherStatus(myTeam);
            bestStatus = myStatus;                  
            if (timing%3 == 0)
            {
                 bestStatus = moveStatus();                    
                 if (distanceToGoal() < GROUNDHEIGHT>>2) bestStatus += 1000;     
            }
            //com.mygdx.mongojocs.lma2007.Debug.println(bestStatus+" vs "+bestPass.myStatus);
            
            if (timing > 10 && bestStatus < bestPass.myStatus)
            {                     
                direction = lookAt(bestPass.x, bestPass.y);      
                //PRECISION 20-(playerPass*2)
                //#ifndef NOHEIGHT    
                ball.kick(bestPass, 1024+distance(bestPass)*12, playerPass ,gc.gfxMatchRnd.nextInt()%2 == 1);
                //#else
                //#endif
                myStatus >>= 1;
                //bestPass.wait(6, soccerPlayer.W_KICK);                  
            }
            else                     
            {       
                if ( (distanceToGoal() < GROUNDHEIGHT/5 && ((minEnemy < STEALDISTANCE && sy != minEnemy) || distance(otherTeam[10]) < STEALDISTANCE)) || distanceToGoal() < GROUNDHEIGHT/10)
                {
                	//com.mygdx.mongojocs.lma2007.Debug.println("XUTE!!!!!!!!!"+x+" "+(HGROUNDWIDTH+com.mygdx.mongojocs.lma2007.GameCanvas.AREAWIDTH/2)+", "+(HGROUNDWIDTH-com.mygdx.mongojocs.lma2007.GameCanvas.AREAWIDTH/2));
                	if (x < HGROUNDWIDTH - GameCanvas.AREAWIDTH/2 || x > HGROUNDWIDTH + GameCanvas.AREAWIDTH/2) 
                	{
                		//CENTRO
                		ball.kick(HGROUNDWIDTH, y, (playerKick*196) + (256*14), 10,true);	                                    		
                	}
                	else
                	{
	                    int destX = GameCanvas.HGOALSIZE-GameCanvas.GOALPOSTSIZE-4; // ERA 1 EN TMN                    
	                    if (otherTeam[10].x < HGROUNDWIDTH) destX = HGROUNDWIDTH + destX;
	                    else destX = HGROUNDWIDTH - destX;
	                    
	                    //if (gc.gfxMatchRnd.nextInt()%256 > 128)
	                    
	                    if (Math.abs(destX-x) > GameCanvas.AREAWIDTH/2) ball.balleffect = true;
	                    
	                    
	                    if (GameCanvas.noScore[team])
	                    {
	                    	//if (gc.gfxMatchRnd.nextInt() > 0)	                    		
	                    		ball.kick(HGROUNDWIDTH, relativeY(GROUNDHEIGHT), (playerKick*164) + (256*6), 0, false);
	                    	//else
	                    	//	ball.kick(destX, relativeY(GROUNDHEIGHT), (playerKick*196) + (256*15), 10, true);
	                    }
	                    else
	                    	ball.kick(destX, relativeY(GROUNDHEIGHT), (playerKick*196) + (256*8), 10, false);
	                    ball.balleffectx = ball.sx*(gc.gfxMatchRnd.nextInt()%3);
	                    
	                    if (GameCanvas.needToScore[team])
	                    {
	                    	for (int i = 0; i < 10;i++)	                        
	                            otherTeam[i].dontTakeBall = 7;
	                    }
                	}
                }

            }
            break;

                    
        case WAIT:               
            if (wait <= 3 || waitReason < 0) {sx = 0;sy = 0;}//W_STEALFAILED       
            if (--wait <= 0) state = FREE;
            break;
            
            
        case PREMATCH:          
            sx = 0;sy = 0;                                                
            if (x < 0) sx = BASERUNSPEED*3;
            else
            {
                int tty = formationPosY;
                //if (gc.strikeTeam == myTeam) tty = (tty*120)/100;
                if (GameCanvas.teamDown == team)
                {                                            
                    tx = GROUNDWIDTH - formationPosX;            
                    ty = GROUNDHEIGHT - tty;                        
                }
                else
                {
                    tx = formationPosX;
                    ty = tty;
                }
                if (GameCanvas.strikeTeam == myTeam && (id == GameCanvas.specialId[team][0] || id == GameCanvas.specialId[team][1])) //8 y 9
                {
                    ty = GameCanvas.HGROUNDHEIGHT + (team == GameCanvas.teamDown?hheight:-hheight);
                    if (id == GameCanvas.specialId[team][1]) tx = GameCanvas.HGROUNDWIDTH;
                    if (id == GameCanvas.specialId[team][0] && 
                    //Math.abs(tx - com.mygdx.mongojocs.lma2007.GameCanvas.HGROUNDWIDTH) < width)
                    tx <= GameCanvas.HGROUNDWIDTH+width)
                    tx += width*2;                                
                }
                if (x > tx+2) sx = -BASERUNSPEED*3;
                if (x < tx-2) sx = BASERUNSPEED*3;
                if (y > ty+2) sy = -BASERUNSPEED*3;
                if (y < ty-2) sy = BASERUNSPEED*3;        
                //normalize();
                /*if (com.mygdx.mongojocs.lma2007.GameCanvas.teamGoal == team && (com.mygdx.mongojocs.lma2007.GameCanvas.nfc>>2)%4 == id%4)
                {
                    sx = sx>>2;
                    sy = sy>>2;
                }*/
                //#ifndef NOSKIP
                if (gc.skip) {
                    fx = tx<<8;
                    fy = ty<<8;                    
                }
                //#endif
                if (Math.abs(tx - x) < 3 && Math.abs(ty - y) < 3)
                {
                     if (GameCanvas.strikeTeam == myTeam && id == GameCanvas.specialId[team][1]) 
                     {fx = tx<<8;fy = ty<<8;ball.theOwner = this;}
                     //





                     formed = true;

                }
            }
            break;
            
            
        case STEAL:                                 
            if (distance(ball) <= TAKEBALLDISTANCE && timing > 0 
            		&& !GameCanvas.needToScore[1-team])
            {
            	//if (true)//gc.gfxMatchRnd.nextInt()%256 > 246- 64-(playerTack*6) )
                //{
                    if (ball.theOwner != null && ball.theOwner != this) 
                    {                    	                 
                    	ball.theOwner.wait(10, W_STEAL);
                    	//ZNR ZNR ZNR ZNR 
                    	if (ball.theOwner.direction == direction || (GameCanvas.gc.matchSuspendedCount < GameCanvas.gc.league.matchSuspendedCount))     
                    	{                           	                    		
                    		fault((soccerPlayer)ball.theOwner);       
                    		break;                         	
                    	}
                    	else {ball.theOwner = null;ball.takeBall(this);}                		
                		ball.theOwner.sx = (ball.theOwner.sx + sx)>>1;
                		ball.theOwner.sy = (ball.theOwner.sy + sy)>>1;
                		
                    }   
                    else
                    	{ball.theOwner = null;ball.takeBall(this);}
                //}
            }
        	//if (ball.y > com.mygdx.mongojocs.lma2007.GameCanvas.GROUNDHEIGHT-com.mygdx.mongojocs.lma2007.GameCanvas.AREAHEIGHT && ball.theOwner != null) fault((soccerPlayer)ball.theOwner);
            if (ball.theOwner == this) ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));                 
            state = STEAL;
            if (--wait <= 0)
            {            	
                sx = sx>>2;
                sy = sy>>2;
                if (ball.theOwner == this) state = OWNBALL;
                else {wait(STEALFAILEDTICK, W_STEALFAILED);}//state = FREE;
                if (offsidePosition) gc.preoffside(this);
            }                        
            break;

                        
        case PREOUT:
            goPlace();
            if (myTeam == GameCanvas.strikeTeam && id == GameCanvas.specialId[team][3]) {
                              
                tx = ball.x <= 0?  ball.x - 1: ball.x + 1;
                ty = ball.y;
                sx = 0;
                sy = 0;
                if (x > tx+3) sx = -BASERUNSPEED*3;
                if (x < tx-3) sx = BASERUNSPEED*3;
                if (y > ty+3) sy = -BASERUNSPEED*3;
                if (y < ty-3) sy = BASERUNSPEED*3;   
                //#ifndef NOSKIP
                if ((sx == 0 && sy == 0) || gc.skip) {
                //#else
                //if (sx == 0 && sy == 0){
                //#endif    
                    sx = 0;
                    sy = 0;                
                    ball.h = BODYH<<8;
                    fx = tx<<8;fy = ty<<8;
                    state = OUT;
                   
                }
            }
            break;
                
        case OUT:            
            direction = lookAt(closestPlayer.x, closestPlayer.y);       
            ball.theOwner = this;
            if (timing > 2)
            {
            	ball.kickOut(closestPlayer, 512+distance(closestPlayer)*12);                                                     
            	GameCanvas.setTeamsState(FREE);                                
            	wait(6, W_OUT);
            }
            break;     
                        
        case PRECORNER:
            goPlace();
            if (myTeam == GameCanvas.strikeTeam && id == GameCanvas.specialId[team][2]) {
                                      
                tx = ball.x;
                ty = ball.y;
                //#ifndef NOSKIP
                //if (gc.skip) fx = tx<<8;fy = ty<<8;
                //#endif
                sx = 0;
                sy = 0;
                if (x > tx+2) sx = -BASERUNSPEED*3;
                if (x < tx-2) sx = BASERUNSPEED*3;
                if (y > ty+2) sy = -BASERUNSPEED*3;
                if (y < ty-2) sy = BASERUNSPEED*3;
                normalize();  
                
                //#ifndef NOSKIP
                if ((sx == 0 && sy == 0) || (gc.skip))
                //#else
                //if (sx == 0 && sy == 0)
                //#endif                	
                {               
                	 //#ifndef NOSKIP
                	 sx = 0;sy = 0;
                	 //#endif
                     freeKick = true;
                     //x = tx;y = ty;
                     fx = tx<<8;fy = ty<<8;
                     //CORNER;
                     angleShot = soccerPlayer.cornerTable[(GameCanvas.outSide < HGROUNDWIDTH)?0:1][(GameCanvas.outSideV < HGROUNDHEIGHT)?0:1];
                     /*if (team == com.mygdx.mongojocs.lma2007.GameCanvas.TEAMA)
                     {                                                                                                   
                        com.mygdx.mongojocs.lma2007.GameCanvas.kickPlayer = this;
                        state = KICKBALL;
                     }
                     else*/ 
                    	 state = CORNER;
                     //state = KICKBALL;                     
                }
            }
            break;  
        
                        
        case CORNER:
            int destX = HGROUNDWIDTH;
            int destY = ball.y < HGROUNDHEIGHT ? (GROUNDHEIGHT/6):(GROUNDHEIGHT-(GROUNDHEIGHT/6));
            direction = lookAt(destX, destY);
            //ball.theOwner = this;
            GameCanvas.setTeamsState(FREE);        
            //#ifndef NOHEIGHT
            ball.kick(destX, destY, GROUNDWIDTH*24, 6, true);    
            //#else
            //#endif
            freeKick = false;
            //com.mygdx.mongojocs.lma2007.Debug.println("chuteeeeee x:"+destX+" y:"+destY);
            wait(8, W_KICK);                                                                                                   
            break; 
        
        
        case PREPENALTY:
            if (myTeam == GameCanvas.strikeTeam && id == GameCanvas.nPP)                        
            {
                tx = HGROUNDWIDTH;
                ty = relativeY(GROUNDHEIGHT - GameCanvas.PENALTYHEIGHT - hheight);
            }
            else
            {
                tx = relativeX(formationPosX);
                ty = relativeY(formationPosY + AREAY + height);
            }
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
                //if (id == 8) com.mygdx.mongojocs.lma2007.Debug.println("formed");
            }
            break;
                  
                        
        case KICKBALL:         
            //com.mygdx.mongojocs.lma2007.Debug.println("CPU kickball");
            ball.theOwner = this;    	
            GameCanvas.strikeTeam = myTeam;
            freeKick = true;                                        
            GameCanvas.freeKick = true;
            fx = ball.fx - cos(angleShot);
            fy = ball.fy - sin(angleShot);            
            direction = lookAt(ball.x, ball.y);            
            ball.sx = 0; ball.sy = 0;
            
            if (--wait < 0)
            {
                //#ifndef NOHEIGHT                
                ball.kick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), (playerKick*90) + 256*18, playerPass, true);
                //#else
                //ball.kick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), (playerKick*90) + 256*18, playerPass);
                //#endif
                GameCanvas.freeKick = false;
                //com.mygdx.mongojocs.lma2007.GameCanvas.kickPlayer = null;
                freeKick = false;
            }
            break;
                    
                    
        case PENALTY:         
            //com.mygdx.mongojocs.lma2007.Debug.println("penalty xut");
            formed = false;            
            ball.theOwner = this;
            ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
            //#ifndef NOHEIGHT
            ball.kick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), 2048, playerKick,false);
            //#else
            //ball.kick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), 2048, playerKick);
            //#endif
            for(int i = 0; i < 10;i++)
            {
                if (i != id && GameCanvas.teamB[i].state >= 0) GameCanvas.teamB[i].state = FREE;
                if (GameCanvas.teamA[i].state >= 0) GameCanvas.teamA[i].state = FREE;
            }
    	    //ball.kick(gc.pointer, relativeY(GROUNDHEIGHT), 2048, 0,false);
    	    GameCanvas.matchState = constants.GAME;
            direction = 0;
            break;
            
       case EJECTED:
            sy = 0;
            if (y < HGROUNDHEIGHT-BODYRADIUS) sy = BASERUNSPEED*2;
            if (y > HGROUNDHEIGHT+BODYRADIUS) sy = -BASERUNSPEED*2;
            sx = -BASERUNSPEED*2;
            break;
            
       case WOUNDED:
    	    if (GameCanvas.gc.league.userMatch[team].isUserTeam() &&
    	    		!GameCanvas.gc.exhibitionFlag
    	    		&& GameCanvas.gc.matchInjuredCount < GameCanvas.gc.league.matchInjuredCount && GameCanvas.gc.isHalfMatch)
    	    {
    	    	GameCanvas.gc.league.matchInjured[GameCanvas.gc.matchInjuredCount++] = GameCanvas.gc.league.extendedPlayer(pid);    	    	
    	    	if (GameCanvas.gc.league.injurePlayer(pid)) injured = true;
    	    	                                                                                                     
    	    	//com.mygdx.mongojocs.lma2007.Debug.println("LESIONADO:"+name);
    	    	//speed = soccerPlayer.BASERUNSPEED / 2;
    	    	
    	    	GameCanvas.gc.substitution = true;    	    	
    	    }
    	    if (--wait <= 0) state = FREE;
    	    break;       		
	}
    	   
    	   
    //if (injured && timing%10 == 0) com.mygdx.mongojocs.lma2007.Debug.println("LESIONADO:"+name);
    fx += sx;
    fy += sy;
	x = fx>>8;                            
    y = fy>>8;                            
    
    stateChanged = false;
    if (state != lastState) 
    {	
    	timing = 0;
    	stateChanged = true;
    	lastState = state;
    }
    if (state == PREMATCH && formed)
    {
    	direction = 4;
		if (GameCanvas.teamDown == team) direction = 0;//else 
    }
    if (state != WAIT && state != OUT && state != KICKBALL)           
        direction = calculateDirection(direction, prex, prey, x, y);     
    
    if (state == PREPENALTY && formed)
        direction = lookAt(ball.x, ball.y);
    
    timing++;    	
           
    sprint = ((sx >= BASERUNSPEED*2) || (sy >= BASERUNSPEED*2)); 
    animation(prefx != fx || prefy != fy);
}
	
	
	
	
	
	
	
public void steal()
{	    
	int dx = ball.x - x;
	int dy = ball.y - y;
	int angle = atan(dy, dx);	    
	sx = ((cos(angle)>>2)*STEALSPEED)>>8;
	sy = ((sin(angle)>>2)*STEALSPEED)>>8;
	wait = 5;//(playerTack/2)+2;   
	state = STEAL;
}

	
public int distanceToGoal()
{
    if (GameCanvas.teamDown == team)    return y;            
    else                        return GROUNDHEIGHT - y;                    
}
	
	
	
		
	
public int moveStatus()
{
	int movStatus;	
	int tmpx = x;
	int tmpy = y;	
	
	//sx = 0;sy = 0;        
    if (attackDirection == 0)
        sy = -speed;        
    else
        sy = speed;

    
    x+=8;
    movStatus = status();
    if (movStatus > myStatus) {sx = speed;myStatus = movStatus;}
            
    x -= 16;
    movStatus = status();
    if (movStatus > myStatus) {sx = -speed;myStatus = movStatus;}
    
    
    y+= 8;
    movStatus = status();
    if (movStatus > myStatus) {sy = speed;myStatus = movStatus;}
            
    y -= 16;
    movStatus = status();
    if (movStatus > myStatus) {sy = -speed;myStatus = movStatus;}
                 
    x = tmpx;
    y = tmpy;                   

    if (x < STEALDISTANCE) sx += speed;
    if (x > GROUNDWIDTH-STEALDISTANCE) sx -= speed;
    
            
    return movStatus;
}




	
public soccerPlayer higherStatus(soccerPlayer team[])
{
	int thePlayer = -1;
     int maxStatus = -GameCanvas.INFINITE;
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
    
   	return (thePlayer == -1?null:myTeam[thePlayer]);
}
	
	
	
	
/*public boolean takeBall()
{
	if (gc.distance(this, ball) <= TAKEBALLDISTANCE)
	{
		if (ball.theOwner == null || ball.theOwner.state != STEAL)
		{
	    		if (h+BODYH > ball.h>>8)
	    		{
	    		        //if (ball.theOwner != null && ball.theOwner.id == 10 && ball.theOwner.team != team)
	    		        //{ 
	    		        //    com.mygdx.mongojocs.lma2007.Debug.println("falta warra");
	    		        //    fault(this, ball.theOwner);
	    		        //}
	    		        //else
	    		        //{
	    		        //com.mygdx.mongojocs.lma2007.Debug.println("takeball");
    	    	    		state = OWNBALL;
    	    	    		//!!! BALL ACTIVATE
    	    	    		ball.sx = 0;
    	    	    		ball.sy = 0;
    	    	    		ball.h = 0;    	   
    	    	    		ball.fx = x<<8;
    	    	    		ball.fy = y<<8;
    	    	    		gc.strikeTeam = myTeam;    	    
    	    	    		ball.theOwner = this;	    	    		
	    	    		//}
	  		}
		}
	}
	return ball.theOwner == this;
}
	*/

    
	

public void takeBallUnconditional()
{    
    state = OWNBALL;
    ball.sx = 0;
    ball.sy = 0;
    //ball.h = 0;
    ball.fx = x<<8;
    ball.fy = y<<8;
    ball.x = x;
    ball.y = y;
    GameCanvas.lastStrikeTeam = myTeam;         
    GameCanvas.strikeTeam = myTeam;         
    ball.theOwner = this;	   
    wait = 25;
}	    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
   
        
    public void activate(int _team)
    {
        //if (_team == gc.TEAMA) {myTeam = gc.teamA; otherTeam = gc.teamB;}
        //else {myTeam = gc.teamB;otherTeam = gc.teamA;}
        
        if (_team == gc.TEAMA) 
            {myTeam = GameCanvas.teamA; otherTeam = GameCanvas.teamB;speed = soccerPlayer.BASERUNSPEED + (playerSpeed*26);}
        else 
            {myTeam = GameCanvas.teamB;otherTeam = GameCanvas.teamA;speed = soccerPlayer.BASERUNSPEED + (playerSpeed*26);}
     
        team = _team;
        tx = HGROUNDWIDTH;        
        state = FREE;
        if (GameCanvas.teamDown == team)
        {            
            attackDirection = 0;    
            ty = GROUNDHEIGHT - 6;            
        }
        else
        {
            attackDirection = 4;                
            ty = 6;
        }      
        /*if (team == gc.TEAMB)
        {
            com.mygdx.mongojocs.lma2007.Debug.println("at:"+attackDirection);
        }*/
        x = tx;
        y = ty;  
        fx = x << 8;
        fy = y << 8;
        direction = attackDirection;        
    }        
                    
                    
    int minDist = 1000;
                    
    public void updateVars()
    {     
    	tx = HGROUNDWIDTH; 
    	if (GameCanvas.teamDown == team)
        {            
            //attackDirection = 0;    
            ty = GROUNDHEIGHT - 6;            
        }
        else
        {
            //attackDirection = 4;                
            ty = 6;
        }      
    	
    	
    	closestEnemyPlayer = otherTeam[0];         
         
        int min = distance(otherTeam[0]);        
         
         for (int i = 1; i < 10;i++)
         {
            int valor = distance(otherTeam[i]);
            if (valor < min)
            {
                min = valor;
                closestEnemyPlayer = otherTeam[i];
            }
         }
         
    }                    
                    
        
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/////////////////////////////////////////////////////////////////////////////	
// PORTER
/////////////////////////////////////////////////////////////////////////////	

/////////////////////////////////////////////////////////////////////////////	
// update(ball _ball)
/////////////////////////////////////////////////////////////////////////////		
public void update(soccerPlayer _ball)
{	   
    ball = _ball;
    timing++;
    
    lastDirection = direction;
    
    //if (team == 0) com.mygdx.mongojocs.lma2007.Debug.println("T1: "+timing+" - "+state);
    int prex = x;
    int prey = y;
    int prefx = fx;
    int prefy = fy;
    
   	//int currentState = state;
    	   
	if (timing%6 == 2) updateVars();
   	   
	
   	//#ifndef NOHEIGHT
    if (gravityVel < constants.MAXGRAVITY*6) gravityVel += constants.GRAVITY*6;
    h -= gravityVel;    
    if (h <= 0) {h = 0; gravityVel = 0;
    if (state == JUMP) state = FOLLOW;
    }
    //#endif
   	   
    //if (team == com.mygdx.mongojocs.lma2007.GameCanvas.TEAMB) com.mygdx.mongojocs.lma2007.Debug.println("ST: "+state);
   	switch(state)
   	{
		case FREE:    
            freeKick = false;     
            if ((timing & 0x00000001) == 1)        
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
            
            //if (relativeY(ball.y) < com.mygdx.mongojocs.lma2007.GameCanvas.AREAHEIGHT) com.mygdx.mongojocs.lma2007.Debug.println("true);
            		 
            //if (relativeY(y) < gc.areaHeight-BODYRADIUS && Math.abs(y-ball.y) < GROUNDHEIGHT/6 && Math.abs(HGROUNDWIDTH-x) <= com.mygdx.mongojocs.lma2007.GameCanvas.AREAWIDTH/2 && gc.distance(this, ball) <= INFLUENCE)
            if (relativeY(ball.y) < GameCanvas.AREAHEIGHT && 
            		 ball.x >= HGROUNDWIDTH - GameCanvas.AREAWIDTH/2 &&
                     ball.x <= HGROUNDWIDTH + GameCanvas.AREAWIDTH/2 &&
                distance(ball) <= INFLUENCE && !GameCanvas.freeKick)
    	    //&& gc.distance(this, ball) <= gc.distance(closestEnemyPlayer, ball)
    	        state = FOLLOW;    	            
    	    palomita();       
    	    break;
		case JUMP:                	             
        case FOLLOW:
            //if (1 == 1) break;
    	   	sx = 0;sy = 0;    	           
            if (ball.x < x-2) sx = -speed-32;
            if (ball.x > x+2) sx = speed+32;
            if (ball.y < y-2) sy = -speed-32;
            if (ball.y > y+2) sy = speed+32;
            if (ball.h > h+BODYH && relativeY(y) > BODYRADIUS)
            {
                if (y > HGROUNDHEIGHT) sy = speed>>1;
                if (y < HGROUNDHEIGHT) sy = -speed>>1;
            }
            normalize();                                     
            direction = lookAt(ball.x, ball.y);           
            //if (ball.theOwner == null)
            if (!ball.takeBall(this))
            {
        		//if (gc.distanceY(this, ball) > INFLUENCE) state = FREE;         
        		//if (Math.abs(y - ball.y) > INFLUENCE) state = FREE;         
        		
                if (relativeY(ball.y) > GameCanvas.AREAHEIGHT || 
                    distance(ball) > INFLUENCE ||                  
                    ball.x < HGROUNDWIDTH - GameCanvas.AREAWIDTH/2 ||
                    ball.x > HGROUNDWIDTH + GameCanvas.AREAWIDTH/2 ||
                    GameCanvas.freeKick
        		    )
        		   //Math.abs(HGROUNDWIDTH-x) > INFLUENCE || 
        		   //Math.abs(HGROUNDWIDTH-ball.x) > com.mygdx.mongojocs.lma2007.GameCanvas.AREAWIDTH/2)
        		   
                		state = FREE;
        		//com.mygdx.mongojocs.lma2007.Debug.println("follow");
                //#ifndef NOHEIGHT
                if (distance(ball) <= (TAKEBALLDISTANCE*2) && ball.h > h+((2*BODYH)/3)) {gravityVel = -256*12;state = JUMP;}
                else
                //#endif
                
        		palomita();                           
            }
            //anava aki
            break;
                    
        case OWNBALL:
            if (timing == 1) wait = 25;
    	    ball.theOwner = this;    	            	
    	    ball.h = h+(11*256);
    	    sx = 0;sy = 0;
            if (x < AREAIX) sx = 256;
            if (x > AREAFX) sx = -256;    	            
            if (y > AREAY && GameCanvas.teamDown != team) sy = -256;
            if (y < GROUNDHEIGHT - AREAY && GameCanvas.teamDown == team) sy = 256;                  
    	    boolean close = false;
    	    if (--wait <= -1 && !close) state = KICKBALL;
    	    direction = attackDirection;    	                	            
    	    ball.setPosition(x-(cos((direction+2)*32)>>9),y-(sin((direction+2)*32)>>9));
    	    angleShot = 192-(attackDirection*32);    	    
            GameCanvas.strikeTeam = myTeam;
    	    break;
    	 
    	    
        case PREGOALKICK:
        	sx = 0;sy = 0;    	           
            if (ball.x < x-2) sx = -speed-32;
            if (ball.x > x+2) sx = speed+32;
            if (relativeY(5) < y-2) sy = -speed-32;
            if (relativeY(5) > y+2) sy = speed+32;
            if (sy == 0 && sx == 0) state = PREGOALKICK2;
        	break;
        
        case PREGOALKICK2:
        	sy = 0;
            if (ball.y < y-2) sy = -speed-64;
            if (ball.y > y+2) sy = speed+64;
            if (sy == 0) 
            {
            	GameCanvas.setTeamsState(FREE);
            	state = KICKBALL;
            }
        	break;
        	
        case KICKBALL:    
            ball.h = 11*256;
        	ball.theOwner = this;    	
        	direction = attackDirection;
                    
            if (timing == 4)
            { 
            	ball.kick(attackDirection, 10*256, playerPass, true);
            	sx = 0;sy = 0;	
            	GameCanvas.freeKick = false;
            	freeKick = false;
            }
            break;

        case WAIT:                    
            if (--wait <= 0) state = FREE;
            break;     
            
        case PALOMITA:
            //#ifndef NOHEIGHT
            if (ball.h > h+BODYH && timing == 1) {gravityVel = -256*18;}
            h = Math.min(h, (GameCanvas.GOALHEIGHT-(GameCanvas.GOALPOSTSIZE*2))<<8);
            //#else
            h = ball.h;
            //#endif
            if (!GameCanvas.needToScore[1-team])        	
            	ball.takeBall(this);
            //#ifdef NOHEIGHT
            h = 0;
            //#endif
            if (--wait <= 0) state = FREE;
            //h = 0;
            break;
            
        case PREPENALTY:   
            if (GameCanvas.strikeTeam != myTeam)
            {                                    
                attackDirection = lookAt(ball.x, ball.y);
                ty = relativeY(1);
                tx = HGROUNDWIDTH;
            }
            else 
            { 
                state = FREE;
                break;
            }
            sx = 0;sy = 0;
            if (tx < x-1) sx = -soccerPlayer.BASERUNSPEED;
            if (tx > x+1) sx = soccerPlayer.BASERUNSPEED;
            if (ty < y-1) sy = -soccerPlayer.BASERUNSPEED;
            if (ty > y+1) sy = soccerPlayer.BASERUNSPEED;                       
            if (Math.abs(tx - x) < 2 && Math.abs(ty - y) < 2)   
            {
                x = tx;y = ty;                 
                if (GameCanvas.strikeTeam != myTeam) {wait = -10;}
                direction = attackDirection;                        
                if (myTeam != GameCanvas.strikeTeam && ball.sy != 0) state = soccerPlayer.PENALTY;           
            }
            break;
                    
        case PENALTY:
            //if (gc.distance(this, ball) <= 3)  
            ball.takeBall(this); 
            //if (gc.distanceY(this, ball) < 24)
            //{
            sx = 0;sy = 0;
            if (ball.x < x-2) sx = -speed;
            if (ball.x > x+2) sx = speed;
            if (ball.y < y-2) sy = -speed;
            if (ball.y > y+2) sy = speed;   
            normalize();                                     
            //}
            direction = lookAt(ball.x, ball.y);           
            palomita();       
            
            if (timing == 1)
            {                        
                int i = gc.gfxMatchRnd.nextInt()%32365;
                int k = playerTack * 1000;
                //if (team == com.mygdx.mongojocs.lma2007.GameCanvas.TEAMB) k -= 5000;
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
            break;
    }    	   
    fx += sx;
    fy += sy;
    x = fx>>8;                            
    y = fy>>8;
    
    stateChanged = false;
    if (lastState != state) 
    {
    	timing = 0;
    	stateChanged = true;
    	lastState = state;
    	//System.out.println("GKS: "+state);
    }
    
    //if (team == com.mygdx.mongojocs.lma2007.GameCanvas.TEAMA) com.mygdx.mongojocs.lma2007.Debug.println("PORA:"+state);
    //else com.mygdx.mongojocs.lma2007.Debug.println("PORB:"+state);
    //NEW
    if (state != FREE && state != WAIT && state != OUT && state != OWNBALL && state != KICKBALL && state != PREPENALTY)
        direction = calculateDirection(direction, prex, prey, x, y);       
        
    animation(fx != prefx || fy != prefy);
    //if (team == 0) com.mygdx.mongojocs.lma2007.Debug.println("T2: "+timing+" - "+state);
}
// update ///////////////////////////////////////////////////////////////////			
	
	
	
/////////////////////////////////////////////////////////////////////////////	
// palomita()
/////////////////////////////////////////////////////////////////////////////		
public void palomita()
{
	
    if (GameCanvas.teamDown == team && ball.y < GROUNDHEIGHT-GROUNDHEIGHT/4) return;
        if (GameCanvas.teamDown != team && ball.y > GROUNDHEIGHT/4) return;
    	if (ball.x < GROUNDWIDTH/4 || ball.x >= GROUNDWIDTH - GROUNDWIDTH/4) return;
    	if (relativeY(ball.y) < relativeY(y)) return;
    	//if (gc.matchState == gc.GAME && team == gc.TEAMB && gc.gfxMatchRnd.nextInt()%256 < -(playerTack*25)+64) return;
    	//if (gc.matchState == gc.GAME && team == gc.TEAMA && gc.gfxMatchRnd.nextInt()%256 < -playerTack*25) return;    
        
    if (Math.abs(ball.sy) > 256 && Math.abs(y - ball.y) < 24)
    {
        if ((GameCanvas.teamDown == team && ball.sy > 0)
        || (GameCanvas.teamDown != team && ball.sy < 0))
        {
            int i = 0;
            int tbx = ball.fx;
            int tby = ball.fy;
            //com.mygdx.mongojocs.lma2007.Debug.println("calculando");
            while(i < 24)
            {
                tbx += ball.sx;
                tby += ball.sy;
                if ((ball.sy > 0 && tby>>8 >= y)
                || (ball.sy < 0 && tby>>8 <= y))
                    break;	                
                //if (tby < 0 || tby >= GROUNDHEIGHT<<8) break;
                else i++;
            }	            
            if (i == 24) return;
            if (Math.abs(x - (tbx>>8)) >= (i*speed)>>8) // era 8 en FM
            // && Math.abs(x - tbx) > (soccerPlayer.RUNSPEED*3)>>8)
            {
                //com.mygdx.mongojocs.lma2007.Debug.println("palomita");
                state = PALOMITA;
                if (tbx>>8 > x){
                     sx = speed+256;
                     direction = 2;
                }
                else {sx = -speed-256;direction = 6;}
                jumpUp = Math.abs(tbx>>8 - x) < TAKEBALLDISTANCE*2; 
                sy = 0;
                wait = Math.min(18, Math.max(12, i));
            }
        }
    }
}
//palomita //////////////////////////////////////////////////////////////////		
	
	
	
	
/////////////////////////////////////////////////////////////////////////////	
// fault(soccerPlayer _good)
/////////////////////////////////////////////////////////////////////////////	
public void fault(soccerPlayer _good)
{
    if (team == _good.team) return;    
    boolean penalty = false;
    
    
    
    numFaults++;
    if (GameCanvas.gc.league.userMatch[team].isUserTeam())    			   
	{
    	if (GameCanvas.gc.matchSuspendedCount < GameCanvas.gc.league.matchSuspendedCount)
    		numFaults++;
    	else 
    		numFaults--;
	}
	else if (!GameCanvas.gc.league.userMatch[team].isUserTeam())
	{
		numFaults += GameCanvas.gc.league.rand()%2;
	}

    GameCanvas.setTeamsState(FREE);
    GameCanvas.strikeTeam = _good.myTeam;       
	//_good.state = KICKBALL;		
    GameCanvas.kickPlayer = _good;
    _good.wait = 20;
    
    /*    
    //SOLO SI SE LESIONA    
    com.mygdx.mongojocs.lma2007.GameCanvas.kickPlayer = otherTeam[(10+_good.id-1)%10];
    com.mygdx.mongojocs.lma2007.GameCanvas.kickPlayer.state = GOPLACE;
    com.mygdx.mongojocs.lma2007.GameCanvas.kickPlayer.tx = ball.x;
    com.mygdx.mongojocs.lma2007.GameCanvas.kickPlayer.ty = ball.y;
    */
    
    
	
	//_good.sx = _good.sx>>1;
	//_good.sy = _good.sy>>1;
    if (relativeY(_good.y) < GameCanvas.AREAHEIGHT && _good.x > HGROUNDWIDTH - (GameCanvas.AREAWIDTH>>1) && _good.x < HGROUNDWIDTH + (GameCanvas.AREAWIDTH>>1)) penalty = true; 
	
	
    gc.prefault(_good.myTeam, penalty);    
	if (penalty)
	{
	    
        //gc.prefault(_good.myTeam, true);    
        GameCanvas.nPP = _good.id;
	    numFaults++;
	}
	else
	{	    
	    _good.angleShot = 192-(_good.attackDirection*32);
	    //#ifndef NOFAULTBUG
	    if (team == GameCanvas.TEAMA)
	    {
	    	//com.mygdx.mongojocs.lma2007.GameCanvas.freezeHumanPlayer = 40;
	    	if (ball.theOwner == this) ball.theOwner = null;
	    }
	    //#endif
	     //gc.prefault(_good.myTeam, false);
	}
	if (numFaults >= 3) {
        if (GameCanvas.ejected[team] < 3) {
	        
            GameCanvas.eject[team] = id;
	    } else {
	        numFaults = 2;
	    }
	}
	
	_good.state = WOUNDED;		
	_good.sx = 0;_good.sy = 0;	
	_good.timing = 0;
	_good.wait = 6;
	wait(7, W_STEALFAILED);
	ball.theOwner = null;
	//com.mygdx.mongojocs.lma2007.Debug.println("======================");
	//gc.debug = this;
}
// fault ////////////////////////////////////////////////////////////////////



/////////////////////////////////////////////////////////////////////////////	
// wait(int _time, int _reason)
/////////////////////////////////////////////////////////////////////////////	
public void wait(int _time, int _reason)
{
	state = soccerPlayer.WAIT;
   	waitReason = _reason;
   	wait = _time;
}
// wait /////////////////////////////////////////////////////////////////////

	
	
/////////////////////////////////////////////////////////////////////////////	
// animation()
/////////////////////////////////////////////////////////////////////////////	  	
//final static int ANIMOFFY = 8;
//#ifdef FEA_CHAPAS
//#else

public void animation(boolean inMovement)
{
	/*	  
    //#ifdef LATERALVIEW
    //switch((14-direction)%8)   
    animSeq = (14-direction)%8;
    //#else    
    //switch(direction)   
    animSeq = direction;
    //#endif    
    
    if (state == PALOMITA)
    {
        animFrame = timing;
        if (animFrame > 3) animFrame = 3;        
        if (sx > 0) animSeq = (2*ANIMOFFY)+1;  
        else animSeq= (2*ANIMOFFY)+2;        
        if (team != com.mygdx.mongojocs.lma2007.GameCanvas.teamDown)
            animSeq += 3;
    }
    else if (state == WOUNDED)
    {
        //#ifdef GFXLITE
        animFrame = 0;
        //#else
        animFrame = Math.min((timing-1)/3,1);
        //#endif
        animSeq += (4*ANIMOFFY)-2;
    }
    else if (state == STEAL)
    {
        //#ifdef GFXLITE
        animFrame = 0;
        //#else
        animFrame = Math.min((timing)/2,2);
        //#endif
        animSeq += (3*ANIMOFFY)-2;   
    }
    else if (
        state == JUMP || 
        h > 0)
        
    {
        animFrame = 0;
        animSeq += (5*ANIMOFFY)-2;
    }
    else if (state == WAIT)
    {
        if (waitReason == W_STEAL) 
        {
            //#ifdef GFXLITE
            animFrame = 0;
            //#else
            animFrame = Math.min((timing-1)/3,1);
            //#endif
            animSeq += (4*ANIMOFFY)-2;
        }
        if (waitReason == W_STEALFAILED) 
        {
            //#ifdef GFXLITE
            animFrame = 0;
            //#else
            animFrame = 2;
            //#endif
            animSeq += (3*ANIMOFFY)-2;   
        }
        if (waitReason == W_KICK) 
        {
            //#ifdef GFXLITE
            animFrame = Math.min(timing,2);
            //#else
            animFrame = Math.min(timing,3);
            //#endif
            animSeq += ANIMOFFY;
        }
        if (waitReason == W_HEADKICK) 
        {
          
            //#ifdef GFXLITE
            animFrame = 0;
            //#else
            animFrame = 1;
            //#endif
            animSeq += (5*ANIMOFFY)-2;

        }        
    }    
    else
    {
        if (sx != 0 || sy != 0) 
        {
            
            //#ifdef GFXLITE
            if (!(timing%2 == 0 && animFrame%3 == 0))               
                animFrame = (++animFrame)%6;                
            //#else
            if (!(timing%2 == 0 && animFrame%4 == 0))
                animFrame = (++animFrame)%8;
            //#endif
        }
        else 
        {
            if (state == KICKBALL && moving > 1) animFrame = (moving/2)%3;
            else animFrame = 2;        
        }
    }*/
        
    // David
//#ifdef ISOMETRICMATCH   
    
    int directionToSequence[] = {5,6,7,0,1,2,3,4};
    
    if(id < 0) {
    	
    	if(lastDirection != direction) {
    	
    		sprite.setSequence(AnimationConstants.BOLA_ALANTE_B0_S0 + directionToSequence[direction]);
    	}
    	
    	if(speed > 0) {
    		
    		sprite.updateFrameLoop();
    	}
    	        
    } else if(id != 10) {
    	        	    	    
	    if(state == STEAL) {
	    
	    	if(stateChanged) {
	    		
	    		sprite.setSequence(AnimationConstants.SEGAR_ABAJO_B0_S16 + directionToSequence[direction]);
	    	    			    			    				    	
	    	}
	    	    	    		    	    
	    	sprite.updateFrame();
	    	
	    } else if(state == OUT) {
		    
	    	if(stateChanged) {
	    		
	    		sprite.setSequence(ball.x < HGROUNDWIDTH ? AnimationConstants.BANDA_ABAJO_DERECHA_B0_S56 : AnimationConstants.BANDA_ARRIBA_IZQ_B0_S57);
	    	    			    			    				    	
	    	}
	    	    	    		    	    
	    	sprite.updateFrame();    
	    	    	    
	    } else if(state == WOUNDED) {
	    
	    	if(stateChanged) {
	    		
	    		sprite.setSequence(AnimationConstants.CAER_ABAJO_B0_S24 + directionToSequence[direction]);
	    	    			    			    				    	
	    	}
	    	    	    		    	    
	    	sprite.updateFrame();    
	    
	    } else if(state == FREE || state == PREMATCH || state == KICKBALL || state == GOPLACE) {
	    	
	    	if(inMovement) {
	    		
	    		if(stateChanged || lastDirection != direction || !lastInMovement) {
	    	    	
	    	    	sprite.setSequence(AnimationConstants.CORRER_ABAJO_B0_S0 + directionToSequence[direction]);
	    	    		        	    		    			
	    	    }
	    	    	    	    	    	    	
	    	    sprite.updateFrameLoop();
	    	    	
	    	    if(sprint) {
			    		
			    	sprite.updateFrameLoop();
			    }
	    	    	    		
	    	} else {
	    		    		
	    		sprite.setSequence(AnimationConstants.QUIETO_ABAJO_B0_S48 + directionToSequence[direction]);
	    	}
	    		    	    
		} else if(state == WAIT) {
	
	    	if(stateChanged) {
	    		
		    	if(waitReason == W_KICK) {
		    		
		    		sprite.setSequence(AnimationConstants.CHUTAR_ABAJO_B0_S8 + directionToSequence[direction]);
		    	}
		    				    			    			    		
		    	if(waitReason == W_PASS) {
		    			
		    		sprite.setSequence(AnimationConstants.PASAR_ABAJO_B0_S32 + directionToSequence[direction]);
			    			    			
			    }
		    	
		    	if(waitReason == W_HEADKICK) {
	    			
		    		sprite.setSequence(AnimationConstants.CABEZAZO_ABAJO_B0_S40 + directionToSequence[direction]);
			    			    			
			    }	 
		    	
		    	if(waitReason == W_OUT) {
	    			
		    		//sprite.setSequence(com.mygdx.mongojocs.lma2007.AnimationConstants.CABEZAZO_ABAJO_B0_S40 + directionToSequence[direction]);
		    		sprite.updateFrameLoop();
			    }
		    	    				
		    }
	    	
	    	sprite.updateFrame();
		    		    	    	
	    } else {
	        		    		    
		    if(inMovement) {
		    	
		    	if(stateChanged || lastDirection != direction || !lastInMovement) {
			    	
			    	sprite.setSequence(AnimationConstants.CORRER_ABAJO_B0_S0 + directionToSequence[direction]);
			    		        	    		    			
			    }
		    	
		    	sprite.updateFrameLoop();
		    	
		    	if(sprint) {
		    		
		    		sprite.updateFrameLoop();
		    	}
		    	
		    } else {
	    		    		
	    		sprite.setSequence(AnimationConstants.QUIETO_ABAJO_B0_S48 + directionToSequence[direction]);
	    	}
		    
	    }
    
    } else {
    	    	
    	// Goalie
    	
    	if(state == FREE || state == PREMATCH || state == OWNBALL) {
	    	
	    	if(inMovement) {
	    		
	    		if(stateChanged || lastDirection != direction || !lastInMovement) {
	    	    	
	    	    	sprite.setSequence(AnimationConstants.PORTERO_CORRER_ABAJO_B0_S0 + directionToSequence[direction]);
	    	    		        	    		    			
	    	    }
	    	    
	    	    if(inMovement) {
	    	    	
	    	    	sprite.updateFrameLoop();
	    	    }
	    		
	    	} else {
	    		    		
	    		sprite.setSequence(direction < 1 || direction > 4 ? AnimationConstants.QUIETO_ABAJO_B0_S20 : AnimationConstants.QUIETO_ARRIBA_B0_S19);
	    	}
	    	
    	} else if(state == KICKBALL) {
    		
	    	if(stateChanged) {
	    			    			    				    			    		
		    	sprite.setSequence(direction < 1 || direction > 4 ?  AnimationConstants.CHUTAR_ARRIBA_DER_B0_S9 : AnimationConstants.CHUTAR_ABAJO_IZQ_B0_S8);
		    			    				    			    			    				    	    		    		    	    				
		    }
	    	
	    	sprite.updateFrame();	
	    	    		    		    	    
		} else if(state == WAIT) {
	
	    	/*if(stateChanged) {
	    		
		    	if(waitReason == W_KICK) {
		    		
		    		sprite.setSequence(direction < 1 || direction > 4 ?  com.mygdx.mongojocs.lma2007.AnimationConstants.CHUTAR_ARRIBA_DER_B0_S9 : com.mygdx.mongojocs.lma2007.AnimationConstants.CHUTAR_ABAJO_IZQ_B0_S8);
		    	}		    				    			    			    				    	    		    		    	    				
		    }*/
	    	
	    	sprite.updateFrame();
		    		    	    	
	    } else if(state == PALOMITA) {
	
	    	if(stateChanged) {
	    						   
	    		if(team == GameCanvas.teamDown) {
	    			
	    			if(jumpUp) {
	    				
	    				sprite.setSequence(AnimationConstants.PARADA_ARRIBA_B0_S10);
	    				
	    			} else {
	    			
	    				sprite.setSequence(direction < 4 ?  AnimationConstants.PARADA_ABAJO_MEDIA_DER_B0_S16 : AnimationConstants.PARADA_ABAJO_MEDIA_IZQ_B0_S18);
	    			}
	    					    		
	    		} else {
	    			
	    			if(jumpUp) {
	    				
	    				sprite.setSequence(AnimationConstants.PARADA_ARRIBA_B0_S10);
	    				
	    			} else {
	    			
	    				sprite.setSequence(direction < 4 ?  AnimationConstants.PARADA_ARRIBA_MEDIA_DER_B0_S12 : AnimationConstants.PARADA_ARRIBA_MEDIA_IZQ_B0_S14);
	    			}
	    		}	    
	    		
		    }
	    	
	    	if(timing % 2 == 0 || sprite.sequence == AnimationConstants.PARADA_ARRIBA_B0_S10){
	    		
	    		sprite.updateFrame();
	    	}
	    	
	    } else if(state == JUMP) {
	    	
	    	if(stateChanged) {
	    						   	    			    	
	    		sprite.setSequence(AnimationConstants.PARADA_ARRIBA_B0_S10);	    				    			    				    			 			    	    		
		    }
	    	
	    	sprite.updateFrame();	
		    		    	    	
	    } else {
	        
		    if(stateChanged || lastDirection != direction) {
		    	
		    	sprite.setSequence(AnimationConstants.PORTERO_CORRER_ABAJO_B0_S0 + directionToSequence[direction]);
		    		        	    		    			
		    }
		    
		    if(inMovement) {
		    	
		    	sprite.updateFrameLoop();
		    }
		    
	    }
    	
    }
    
    lastInMovement = inMovement;
//#endif        
}
// animation ////////////////////////////////////////////////////////////////

//#endif

/////////////////////////////////////////////////////////////////////////////	
// int relativeY(int _y)
/////////////////////////////////////////////////////////////////////////////	  
public int relativeY(int _y)
{
    if (team == GameCanvas.teamDown) return GROUNDHEIGHT-_y;
    else return _y;
}
// relativeY /////////////////////////////////////////////////////////////////	  



/////////////////////////////////////////////////////////////////////////////	
// int relativeX(int _x)
/////////////////////////////////////////////////////////////////////////////	  
public int relativeX(int _x)
{
    if (team == GameCanvas.teamDown) return GROUNDWIDTH-_x;
    else return _x;
}
// relativeX /////////////////////////////////////////////////////////////////	  















//BALL





	final static int width = 12;
	final static int height = 12;
	final static int hwidth = 6;
	final static int hheight = 6;  
    int x, y, fx, fy, sx, sy;       
    int timing;     
    int direction, lastDirection;
    
    static GameCanvas  gc;
    
    soccerPlayer theOwner;
    
    
    
        
    
    int gravityVel;
    int h;
    
    final static int cornerTable[][] = {{16,-16},{128-16,128+16}};








public void activate(int _x , int _y)
{
    //com.mygdx.mongojocs.lma2007.Debug.println("activate");
    x = _x;
    y = _y;
    fx = _x<<8;
    fy = _y<<8;        
    sx = 0;
    sy = 0;
    h = 0;
    if (theOwner != null) theOwner.state = soccerPlayer.FREE;
    theOwner = null;
}   

  
  
public int lookAt(int lx, int ly)
{
    return calculateDirection(0, x, y, lx, ly);
}
    
        
  
        
public void setPosition(int _x, int _y)
{
    //com.mygdx.mongojocs.lma2007.Debug.println("setPos");
    x = _x;
    y = _y;
    fx = x<<8;
    fy = y<<8;
}
    
    
    
public int calculateDirection(int def, int prex, int prey, int x, int y)
{
    int dx = x-prex;
    int dy = y-prey;
    if (dy == 0) return def;     
    int angle = atan(dy, dx);
    int res = (((angle+16)/32) + 2) % 8;        
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

      
      
public void kick(int dir, int power, int desp, boolean highKick)
{
	
	long t = System.currentTimeMillis();
    
    if (power > 256*8) power = 256*8;
    switch (dir)
    {
        case 0: sx = desp;sy = -power;break;
        case 1: sx = power;sy = -power;break;
        case 2: sx = power;sy = 0;break;
        case 3: sx = power;sy = power;break;
        case 4: sx = desp;sy = power;break;
        case 5: sx = -power;sy = power;break;
        case 6: sx = -power;sy = 0;break;
        case 7: sx = -power;sy = -power;break;          
    }
    
    sx += timing%7;
    sy += timing%5;
    normalize();
    //if (theOwner != null) 
    theOwner.wait(9, soccerPlayer.W_KICK);
    theOwner = null;
    h = 0;
    gravityVel = -power;
    if (highKick) 
    {
        gravityVel -= power;                
        power = power/2;
    }
}
    
//#ifndef NOHEIGHT
public void kick(soccerPlayer sp, int power, int desp, boolean highKick)
//#else
//#endif
{
    //#ifndef NOHEIGHT
    kick(sp.x, sp.y, power, desp, highKick);
    //#else
    //#endif
    
    /*sp.state = soccerPlayer.GOPLACE;
    sp.wait = distance(sp)/4;
    sp.waitReason = sp.wait; //AIXO NO ESTA BE*/
    sp.waitReason = W_PASS;
    
    int dist = distance(sp);
    sp.tx = x+((sx*dist)/1400);
    sp.ty = y+((sy*dist)/1400);
    //com.mygdx.mongojocs.lma2007.Debug.println("kick");
    //#ifdef OFFSIDE
    //com.mygdx.mongojocs.lma2007.Debug.println("mirando offside");
    if (/*state == OWNBALL && */sp.relativeY(y) < sp.relativeY(sp.y))		
    {
    	//com.mygdx.mongojocs.lma2007.Debug.println("########################mirando offside2");
    	boolean offside = true;
    	int i = 0;
    	while (offside && i < 10)
    	{
    		if (sp.relativeY(sp.otherTeam[i].y) >= sp.relativeY(sp.y))
    		{
    			offside = false;
    		}
    		i++;
    	}    	
    	if (offside){ sp.offsidePosition = true;
    		//gc.preoffside(sp);
    	//com.mygdx.mongojocs.lma2007.Debug.println("SERGI ALBERT");
    	}
    }
    //#endif
    //com.mygdx.mongojocs.lma2007.Debug.println("GO player"+sp.id);
}
    

boolean balleffect;
int balleffectx;
//#ifndef NOHEIGHT    
public void kick(int _x, int _y, int power, int desp, boolean highKick)
//#else
//#endif
{
	
 	long t = System.currentTimeMillis();
    if (power > 256*8) power = 256*8;
    //#ifndef NOHEIGHT    
    if (highKick) power = (power*4)/6;
    //#endif
    
    
    int snd = 4;
	if (GameCanvas.firstPass) {desp = 10; snd = 5;}
	
    int dx = _x - x;
    int dy = _y - y;
    int angle = atan(dy, dx);
	desp = ((10-desp)*(GameCanvas.gfxMatchRnd.nextInt()%48))>>5;
	//System.out.println("DESP:"+desp);
    //desp = com.mygdx.mongojocs.lma2007.GameCanvas.gfxMatchRnd.nextInt()%2;
    
    //com.mygdx.mongojocs.lma2007.Debug.println ("desp: "+desp);
    
    angle += desp;
    sx = ((cos(angle)>>2)*power)>>8;
    sy = ((sin(angle)>>2)*power)>>8;       
    if (theOwner != null)
    {
    	pid = theOwner.pid;
    	theOwner.wait(9, soccerPlayer.W_KICK);
    	theOwner.direction = theOwner.lookAt(x+(sx>>7), y+(sy>>7));    	
    }
    theOwner = null;        
    h = 0;
    gravityVel = -power;                
    //#ifndef NOHEIGHT    
    if (highKick) 
    {
        gravityVel -= power;                
        //power = power/2;
    }
    //#endif
    
    
}
    
//#ifdef HEADKICKS
public void headKick(int _x, int _y, int power)
{
    if (power > 256*8) power = 256*8;
    
    int dx = _x - x;
    int dy = _y - y;
    int angle = atan(dy, dx);
    //angle += desp;
    sx = ((cos(angle)>>2)*power)>>8;
    sy = ((sin(angle)>>2)*power)>>8;       
    //fy+=16*256;   
    if (theOwner != null)
    { 
    	theOwner.wait(6, soccerPlayer.W_HEADKICK);
    	theOwner.direction = theOwner.lookAt(x+(sx>>7), y+(sy>>7));
    	pid = theOwner.pid;
    }
    theOwner = null;        
    //h = 0;
    //gravityVel = -power;              
    gravityVel = Math.max(0,gravityVel);                        
}
//#endif

    
public void kickOut(soccerPlayer sp, int power)
{
    int _x = sp.x;
    int _y = sp.y;
    if (power > (256*6)) power = (256*6);
    int dx = _x - x;
    int dy = _y - y;
    int angle = atan(dy, dx);       
    sx = ((cos(angle)>>2)*power)>>8;
    sy = ((sin(angle)>>2)*power)>>8;
    
    theOwner.direction = theOwner.lookAt(sp.x, sp.y);
    theOwner = null;        
    //	theOwner.wait(6, soccerPlayer.W_HEADKICK);
    	
    //	pid = theOwner.pid;
    //}
    h = 255*BODYH;
    gravityVel = -power/2;
    
    // GOPLACE
    sp.state = soccerPlayer.GOPLACE;
    sp.wait = distance(sp)/4;
    int dist = distance(sp);
    sp.tx = x+((sx*dist)/1400);
    sp.ty = y+((sy*dist)/1400);
    
    
}
    
    
    
public void updateBall()
{         
	lastDirection = direction;
    
    timing++;
    int prex = x;
    int prey = y;
    fx += sx;
    fy += sy;
    x = fx >> 8;
    y = fy >> 8;
    int friction = h > 0?constants.FRICTION_AIR:constants.FRICTION_GROUND;
    if (sx > 0) sx -= friction;
    if (sx < 0) sx += friction;
    if (sy > 0) sy -= friction;
    if (sy < 0) sy += friction;
    
    //com.mygdx.mongojocs.lma2007.Debug.println("sx: "+sx + " balleffectx:"+balleffectx);
    if (balleffect) sx -= balleffectx>>5;
    
    if (Math.abs(sx) < friction && Math.abs(sy) < friction) {sx = 0;sy = 0;}
    direction = calculateDirection(direction, prex, prey, x, y);
    if (gravityVel < constants.MAXGRAVITY) gravityVel += constants.GRAVITY;
    h -= gravityVel;
    if (h < 0) 
    {   
    	balleffect = false;
        h = 0;
        gravityVel = -(gravityVel*8)/10; // 8/9
        //com.mygdx.mongojocs.lma2007.Debug.println("G:"+gravityAcc);
        //if (gravityAcc <= 2) gravityAcc = 0;
    }
    
    speed = Math.abs(sx) + Math.abs(sy);// + Math.abs(gravityVel); 
    //com.mygdx.mongojocs.lma2007.Debug.println("Ball Height: "+(h>>8));
    //h = Math.max(0, h-gravityAcc);
    
    animation(true);
}





public boolean takeBall(soccerPlayer p)
{
    if (theOwner == p) {p.state = soccerPlayer.OWNBALL;return true;}
    if ((distance(p) <= soccerPlayer.TAKEBALLDISTANCE)
    //{if 5
        && (theOwner == null || theOwner.state != soccerPlayer.STEAL))
        { 
    		
    		if (theOwner != null && p.state != soccerPlayer.STEAL && (p.playerTack+(gc.gfxMatchRnd.nextInt()%4)) < theOwner.playerTack+(gc.gfxMatchRnd.nextInt()%4)) return false;
                //#ifndef NOHEIGHT                
                if ((p.h>>8)+p.BODYH > h>>8)
                //#endif
                {
                			if (theOwner != null) 
                        	{
                        		//theOwner.state = WAIT;if (theOwner.wait < 4) theOwner.wait= 4;
                        		//com.mygdx.mongojocs.lma2007.Debug.println("robo"+(timing%10));
                        		theOwner.wait(5, WOUNDED);
                        		theOwner.state = WOUNDED;
                        	}
                        	p.state = soccerPlayer.OWNBALL;
                            //!!! BALL ACTIVATE
                            sx = 0;
                            sy = 0;
                            //#ifndef NOHEIGHT
                            if (p.h == 0)
                            //#endif
                            {
                                h = 0;                            
                                fx = x<<8;
                                fy = y<<8;                            
                            }
                            if (offsidePosition) gc.preoffside(p);
                            GameCanvas.strikeTeam = p.myTeam;           
                            GameCanvas.lastStrikeTeam = p.myTeam;   
                            //com.mygdx.mongojocs.lma2007.GameCanvas.keyPresTime = 0;
                            theOwner = p;                                                       
                            GameCanvas.clearOffside();
                        //}
                        return true;
            }
        }
    //}
    return false;
    //return ball.theOwner == p;
}
  
  
  
private static short sintable[];

private static short costable[];

private static short tantable[];
    
public static void initTables(GameCanvas gc)
{       
    byte temp[];//= new byte[384];
    
    temp = gc.loadFile("/Trig.bin");
        
    sintable=new short[64];
    costable=new short[64];
    tantable=new short[64];
    
    
    for(int i=0; i<64; i++){
    
        sintable[i]=(short)(((temp[i*2]&0xFF)<<8) | ((temp[i*2+1]&0xFF)));      
        costable[i]=(short)(((temp[128+i*2]&0xFF)<<8) | ((temp[i*2+(1+128)]&0xFF)));      
        tantable[i]=(short)(((temp[i*2+256]&0xFF)<<8) | ((temp[i*2+(1+256)]&0xFF)));      
    }   
} 
            
static int sin(int a)
{
    int v;
    while(a<0) a+=256;
    a=a%256;
    if((a>>6)%2==0)
        v=sintable[a%64];
    else    v=sintable[63-(a%64)];  
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
    else    v=costable[63-(a%64)];  
    if((a>>6==1) || (a>>6==2)) v=-v;
    return v;
}
    

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

    
    /*static int tan(int a)
    {
        int v;
        while(a<0) a+=256;
        a=a%256;
        if((a>>6)%2==0)
            v=tantable[a%64];
        else    v=tantable[63-(a%64)];  
                
        if((a>>6)%2==1) v=-v;       
        return v;
    }*/



public int distance(soccerPlayer b)
{
    int dx = x - b.x;
    int dy = y - b.y;    
    int angle = atan(dy, dx);
    int deno = sin(angle);
    if (deno == 0) return Math.abs(dx);
    int d = ((dy << 10) / deno );       
    return Math.abs(d);
}


public int angle(soccerPlayer a)
{
    int dx = a.x - x;
    int dy = a.y - y;    
    int angle = ball.atan(dy, dx);
    //com.mygdx.mongojocs.lma2007.Debug.println("angle: " + angle);
    return (angle);    
}
  

boolean offsidePosition;
	
}





interface constants
{
    
    //===============================================
    // MATCH STATES
    //===============================================
    final static int GAME           = 0;
    final static int PENALTIES      = 1;
    final static int ENDGAME        = 2;
    final static int GOAL           = 3;
    final static int WAITCORNER     = 4;
    final static int WAITOUT        = 5;
    final static int WAITGOALKICK   = 6;
    final static int WAITGOAL       = 7;
    final static int WAITFAULT      = 8;
    final static int WAITPENALTY    = 9;
    //#ifdef OFFSIDE
    final static int WAITOFFSIDE    = 10;
    //#endif
    
    /////////////////////////////////////////////////  
    
    
    //======================================
    //=== PHYSICS ==========================
    //======================================        
    final static int MAXGRAVITY         = 1024+512; // 1024 CF
    final static int GRAVITY            = 256+64; // 256 CF
    final static int FRICTION_GROUND    = 40;   
    final static int FRICTION_AIR       = 15;   //18 CF
 
 
    final static int KICKTIMEOUT        = 150;
    
    
}

//#endif
