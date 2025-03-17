package com.mygdx.mongojocs.clubfootball2006;
//#ifdef NOSOUNDFX
//#define NOKICKSOUND
//#endif


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
	//#ifdef COINTOSS
	final static int TRUEPREMATCH       = 15;
	//#endif
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
	
	//======================================
    // PLAYER ATRIBUTTES 
    //======================================
    String 		    name;    		
	byte 		    playerSkin;
	byte 		    playerHead;
	byte 		    playerSpeed;
    byte 		    playerKick;
    byte 		    playerPass;
    byte 		    playerTack;
    byte            playerFKick;
    //======================================
	// GRAPHIC VARIABLES
	//======================================
	int			    animSeq, animFrame;	                   
	//======================================
    
    
    soccerPlayer destPass;


/////////////////////////////////////////////////////////////////////////////	
// soccerPlayer soccerPlayer(com.mygdx.mongojocs.sanfermines2006.GameCanvas _gc, int _id, ball _ball)
/////////////////////////////////////////////////////////////////////////////		
public soccerPlayer(GameCanvas _gc, int _id)
{	
//public unit(int _width, int _height, com.mygdx.mongojocs.sanfermines2006.GameCanvas _gc)
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
public void setPlayerSkills(int _skillArray[])
{
	playerSkin = (byte)_skillArray[0];
   	playerHead = (byte)_skillArray[1];
   	if (playerSkin == 1 && playerHead == 3) playerHead = 4;
   	playerSpeed = (byte)_skillArray[2];
   	playerKick = (byte)_skillArray[3];
   	playerPass = (byte)_skillArray[4];
   	playerTack = (byte)_skillArray[5];
    playerFKick = (byte)_skillArray[6];
   	numFaults = 0;
}    


/////////////////////////////////////////////////////////////////////////////	
// activate(int _x , int _y, int _team)
/////////////////////////////////////////////////////////////////////////////	  

public void activate(int _x , int _y, int _team, boolean _set)
{    
    if (_set && state != EJECTED)    
    {state = PREMATCH;formed = false;freeKick = false;}
	if (_team == GameCanvas.TEAMA) {myTeam = GameCanvas.teamA; otherTeam = GameCanvas.teamB;speed = BASERUNSPEED + 32 + (playerSpeed*26);}
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
	/*if (gc.teamDown == team)
	  y = (HGROUNDHEIGHT) - height;
	else    
	  y = (HGROUNDHEIGHT) + height;       	  */
	if (_set
			//#ifndef DOJA
			&& state != EJECTED)
			//#else
			//#endif
	{
	    y = relativeY((GameCanvas.HGROUNDHEIGHT) - height);  
	    x = (id+1) * width;      
	    fx = x << 8;
	    fy = y << 8;        
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
    factor = (factor * 2300) / GROUNDHEIGHT;         

    result += factor;                           
    
    //FACTOR: CERCANIA A LA PELOTA (MAX: 0 ; MIN = -GROUNDHEIGHT*2)
    factor = -distance(ball);
    /*factor = (factor * 1000) / (GROUNDHEIGHT);*/
    if (factor < STEALDISTANCE*3 || ball.theOwner == this)
    {
        factor = (factor * 1100) / (GROUNDHEIGHT);
        result += factor;
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
        factor = (factor * 1000) / (GROUNDHEIGHT);
    else 
        factor = (factor * 3000) / (GROUNDHEIGHT*2);
         
    result += factor;
         
    //FACTOR RANDOM
    //factor = com.mygdx.mongojocs.sanfermines2006.GameCanvas.rnd.nextInt()%300;
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
	//int rand = gc.rnd.nextInt()%3;
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
	//#ifndef NOFAULTBUG
	if (GameCanvas.freezeHumanPlayer > 0 && team == GameCanvas.TEAMA)
	{
		state = FREE;
	}
	//#endif
	int prex = x;
    int prey = y;
    	       	   
    int currentState = state;
   
   //if (this == gc.marca) System.out.println("estado: "+state);
   //System.out.println("gv"+gravityVel);
   //#ifdef HEADKICKS
  /*  if (gravityVel < constants.MAXGRAVITY*6) gravityVel += constants.GRAVITY*6;
   	h -= gravityVel;
    //if (h > 0) h -= 4*256; 
    if (h <= 0) {h = 0; gravityVel = 0;}
    else {sx = 0;sy = 0;}*/
    //#endif	   
    	   
    if (numFaults >= 3)    state = EJECTED;
    
    	   //if (h > 0) System.out.println("HEIGHT: "+(h>>8));
    switch(state)
    {
		case FREE:    	            
            freeKick = false;
    	    goPlace();    	            
            break;
                 
        case GOPLACE:  
            if (ball.theOwner != null) state = FREE;
            else if (ball.takeBall(this)) {GameCanvas.np = id;}
            //#ifndef DOJA
            if (wait < waitReason/4 || distance(ball) < STEALDISTANCE) {tx = ball.x; ty = ball.y;}
            sx = 0; sy = 0;
            if (tx < 8) tx = 8;
            if (tx > GROUNDWIDTH-8) tx = GROUNDWIDTH-8;
            if (tx < x-3) sx = -speed;
            if (tx > x+3) sx = speed;
            if (ty < y-3) sy = -speed;
            if (ty > y+3) sy = speed;    
            if (ball.theOwner == this) {sx = 0;sy = (attackDirection==0?-speed:speed);}
            //#else
            //if (ball.theOwner == this) {sx = 0;sy = (attackDirection==0?-speed:speed);}            
            //#endif
            
            //if (ball.takeBall(this)) {gc.np = id;sx = 0;sy = (attackDirection==0?-speed:speed);}
            //if (wait-- < 0) ;//state = FOLLOW;
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
                    
            if (!ball.takeBall(this)) {
            
                if (ball.h>>8 < BODYH && distance(ball) <= STEALDISTANCE)
                {                    
                    if (//distance(ball) >= BODYRADIUS && 
                    //if(
                    //SE HAN COMENTADO ESTAS 2 LINEAS Y SE HA AGREDADO UNA NUEVA REGLA
                    //////ball.theOwner != null && 
                    //////ball.theOwner.myTeam == otherTeam &&
                    		
                    		ball.sy>>9 != sy>>9 &&
                    GameCanvas.rnd.nextInt()%256 > 196-(playerTack*5))
                        steal();
                //#ifdef HEADKICKS        
                /*} else {
                    
                    if (Math.abs((ball.fx+(ball.sx*4)) -  fx) < 256*TAKEBALLDISTANCE &&
                        Math.abs((ball.fy+(ball.sy*4)) -  fy) < 256*TAKEBALLDISTANCE)                    
                        state = JUMP;*/
                //#endif    
                }
            }        
            break;
        //#ifdef HEADKICKS        
        /*case JUMP:
            sx = 0; sy = 0;
            if (ball.x < x) sx -= BASERUNSPEED;
            if (ball.x > x) sx += BASERUNSPEED;
            if (ball.y < y) sy -= BASERUNSPEED;
            if (ball.y > y) sy += BASERUNSPEED;
            //gc.marca = this;
            if (timing == 1) {gravityVel = -256*18;h = 1;}
            //if (timing < 5) h += 256*12;
            //else h -= 256*4;
            //if (gc.humanSoccerPlayer == this) gc.humanSoccerPlayer = null;
            
            if (ball.takeBall(this))
            {
                
                //ball.theOwner = this;
                if (distanceToGoal() < GROUNDHEIGHT/4)
                    ball.headKick(com.mygdx.mongojocs.sanfermines2006.GameCanvas.pointer, relativeY(GROUNDHEIGHT), (playerKick*90) + (256*10));
                else 
                    ball.headKick(closestPlayer.x, closestPlayer.y, 256*100);                    //512+gc.distance(closestPlayer, this)*12
                    //kick(closestPlayer, 812+gc.distance(closestPlayer, this)*12, 0, false);
                //ball.theOwner = null;
                //state = WAIT;
                //wait = 20;
                //waitReason = W_HEADKICK;
                direction = lookAt(com.mygdx.mongojocs.sanfermines2006.GameCanvas.pointer, relativeY(GROUNDHEIGHT));
                break;
            }
            if (h == 0) state = FREE;
            //state = JUMP;
            break;*/
        //#endif            
        case OWNBALL:      
            if (GameCanvas.firstPass)
            {
                int i = GameCanvas.specialId[team][0];
                //#ifndef NOHEIGHT    
                ball.kick(myTeam[i], 256*5, 10, false);   //myTeam[8]
                //#else
                //#endif
                lookAt(myTeam[i].x, myTeam[i].y);      
                GameCanvas.firstPass = false;                
                break;
            }                   	
            if (ball.theOwner != this) {state = FREE;break;}
            ball.theOwner = this;
                    
            ball.setPosition(x-(cos((direction+2)*32)>>7),y-(sin((direction+2)*32)>>7));
                    
            bestPass = higherStatus(myTeam);
            bestStatus = myStatus;                  
            if (timing%5 == 0)
            {
                 bestStatus = moveStatus();                    
                 if (distanceToGoal() < GROUNDHEIGHT>>2) bestStatus += 1000;     
            }
            //System.out.println(bestStatus+" vs "+bestPass.myStatus);
            
            if (bestStatus < bestPass.myStatus)
            {                     
                direction = lookAt(bestPass.x, bestPass.y);      
                //PRECISION 20-(playerPass*2)
                //#ifndef NOHEIGHT    
                ball.kick(bestPass, 1024+distance(bestPass)*12, playerPass ,false);
                //#else
                //#endif
                myStatus >>= 1;
                //bestPass.wait(6, soccerPlayer.W_KICK);                  
            }
            else                     
            {       
                //ZNR gc.rnd.nextInt()%256 > 160-(playerKick*4) &&  	
                       
                //if ((distanceToGoal() < GROUNDHEIGHT/5 && (minEnemy < STEALDISTANCE || gc.distance(this, otherTeam[10]) < STEALDISTANCE*2)) || distanceToGoal() < GROUNDHEIGHT/8)
                //    ball.kick(gc.pointer, relativeY(GROUNDHEIGHT)/*GROUNDHEIGHT*/, (playerKick*90) + (256*10), 0,false);
                if ((distanceToGoal() < GROUNDHEIGHT/5 && ((minEnemy < STEALDISTANCE && sy != minEnemy) || distance(otherTeam[10]) < STEALDISTANCE)) || distanceToGoal() < GROUNDHEIGHT/10)
                {
                    //ball.kick(gc.pointer, relativeY(GROUNDHEIGHT), (playerKick*196) + (256*8), 0,false);
                    int destX = GameCanvas.HGOALSIZE-GameCanvas.GOALPOSTSIZE-1;
                    if (otherTeam[10].x < HGROUNDWIDTH) destX = HGROUNDWIDTH + destX;
                    else destX = HGROUNDWIDTH - destX;
                    //#ifndef NOHEIGHT
                    ball.kick(destX, relativeY(GROUNDHEIGHT), (playerKick*196) + (256*8), 10,false);
                    //#else
                    //#endif
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
                    //Math.abs(tx - com.mygdx.mongojocs.sanfermines2006.GameCanvas.HGROUNDWIDTH) < width)
                    tx <= GameCanvas.HGROUNDWIDTH+width)
                    tx += width*2;                                
                }
                if (x > tx+2) sx = -BASERUNSPEED*3;
                if (x < tx-2) sx = BASERUNSPEED*3;
                if (y > ty+2) sy = -BASERUNSPEED*3;
                if (y < ty-2) sy = BASERUNSPEED*3;        
                //normalize();
                /*if (com.mygdx.mongojocs.sanfermines2006.GameCanvas.teamGoal == team && (com.mygdx.mongojocs.sanfermines2006.GameCanvas.nfc>>2)%4 == id%4)
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
            if (distance(ball) <= TAKEBALLDISTANCE && timing > 0)
            {
                //if (true)//gc.rnd.nextInt()%256 > 246- 64-(playerTack*6) )
                //{
                    if (ball.theOwner != null && ball.theOwner != this) 
                    {
                    	ball.theOwner.wait(10, W_STEAL);
                    	if (ball.theOwner.direction == direction)     
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
        	//if (ball.y > com.mygdx.mongojocs.sanfermines2006.GameCanvas.GROUNDHEIGHT-com.mygdx.mongojocs.sanfermines2006.GameCanvas.AREAHEIGHT && ball.theOwner != null) fault((soccerPlayer)ball.theOwner);
            if (ball.theOwner == this) ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));                 
            state = STEAL;
            if (--wait <= 0)
            {
                sx = sx>>2;
                sy = sy>>2;
                if (ball.theOwner == this) state = OWNBALL;
                else {wait(STEALFAILEDTICK, W_STEALFAILED);}//state = FREE;                            
            }                        
            break;

                        
        case PREOUT:
            goPlace();
            if (myTeam == GameCanvas.strikeTeam && id == GameCanvas.specialId[team][3]) {
                              
                tx = ball.x <= 0?  ball.x - 4: ball.x + 4;
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
                //#endif    
                    sx = 0;
                    sy = 0;                
                    ball.h = BODYH<<6;
                    fx = tx<<8;fy = ty<<8;
                    state = OUT;
                    GameCanvas.humanSoccerPlayer = this;
                }
            }
            break;
                
        case OUT:            
            direction = lookAt(closestPlayer.x, closestPlayer.y);        
            ball.kickOut(closestPlayer, 512+distance(closestPlayer)*12);                                                     
            GameCanvas.setTeamsState(FREE);                                
            wait(6, W_OUT);
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
                if (x > tx+2) sx = -BASERUNSPEED*2;
                if (x < tx-2) sx = BASERUNSPEED*2;
                if (y > ty+2) sy = -BASERUNSPEED*2;
                if (y < ty-2) sy = BASERUNSPEED*2;
                normalize();  
                
                //#ifndef NOSKIP
                if ((sx == 0 && sy == 0) || (gc.skip))
                //#else
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
                     if (team == GameCanvas.TEAMA)
                     {                                                                                                   
                        GameCanvas.kickPlayer = this;
                        state = KICKBALL;
                     }
                     else state = CORNER;
                     //state = KICKBALL;
                     //gc.humanSoccerPlayer = this;
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
            //System.out.println("chuteeeeee x:"+destX+" y:"+destY);
            wait(8, W_KICK);                                                                                                   
            break; 
        
        
        case PREPENALTY:
            if (myTeam == GameCanvas.strikeTeam && id == GameCanvas.nPP)                        
            {
                tx = HGROUNDWIDTH;
                ty = relativeY(GROUNDHEIGHT - (GROUNDHEIGHT/8) - hheight);
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
                //if (id == 8) System.out.println("formed");
            }
            break;
                  
                        
        case KICKBALL:         
            //System.out.println("CPU kickball");
            ball.theOwner = this;    	
            //com.mygdx.mongojocs.sanfermines2006.GameCanvas.humanSoccerPlayer = null;
            GameCanvas.strikeTeam = myTeam;
            freeKick = true;                                        
            GameCanvas.freeKick = true;
            fx = ball.fx - cos(angleShot);
            fy = ball.fy - sin(angleShot);            
            direction = lookAt(ball.x, ball.y);            
            ball.sx = 0; ball.sy = 0;
            ///com.mygdx.mongojocs.sanfermines2006.GameCanvas.humanSoccerPlayer = null;
            if (--wait < 0)
            {
                //#ifndef NOHEIGHT                
                ball.kick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), (playerKick*90) + 256*18, playerFKick, true);
                //#else
                //#endif
                GameCanvas.freeKick = false;
                //com.mygdx.mongojocs.sanfermines2006.GameCanvas.kickPlayer = null;
                freeKick = false;
            }
            break;
                    
                    
        case PENALTY:         
            //System.out.println("penalty xut");
            formed = false;            
            ball.theOwner = this;
            ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
            //#ifndef NOHEIGHT
            ball.kick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), 2048, playerFKick,false);
            //#else
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
	}
    	   
    	   
    	   
    fx += sx;
    fy += sy;
	x = fx>>8;                            
    y = fy>>8;                            
    
    if (currentState != state) {timing = 0;}
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
           
    animation();
}
	
	
	
	
	
public void manualUpdate(int keyX, int keyY, boolean keyFire)
{	
	//#ifndef NOFAULTBUG
	if (GameCanvas.freezeHumanPlayer > 0)
	{
		if (ball.theOwner == this) ball.theOwner = null;
		timing++;     
	    animation();
	}
	//#endif
    boolean keyShot 	= false;
    boolean keyShotOne 	= false;
    boolean keyShotTwo 	= false;
    int prex = x;
	int prey = y;    	       	   
    int currentState = state;			
    	            
    destPass = null;	       
    
    //#ifndef NOHEIGHT             
    if (gravityVel < constants.MAXGRAVITY*6) gravityVel += constants.GRAVITY*6;
   	h -= gravityVel;    
    if (h < 0) {h = 0; gravityVel = 0;}
    
    
 	if (keyFire) 
 	{
 		if (GameCanvas.keyPresTime == 0) GameCanvas.keyPresTime = GameCanvas.nfc;//System.currentTimeMillis();
 	}
 	else if (GameCanvas.keyPresTime != 0) 
 	{
 		keyShot = true;
 		//if (System.currentTimeMillis() - com.mygdx.mongojocs.sanfermines2006.GameCanvas.keyPresTime > 500) keyShotTwo = true;
        if (GameCanvas.nfc - GameCanvas.keyPresTime >= 6) keyShotTwo = true;
 		else keyShotOne = true;
 		GameCanvas.keyPresTime = 0; 		
    }
    //#else
    //#endif
    
    	
	switch(state)
    {
		case FOLLOW:           
		    if (keyX != 0) {sx = keyX*(speed+PLUSSPEED);if (keyY == 0) sy = 0;}
            if (keyY != 0) {sy = keyY*(speed+PLUSSPEED);if (keyX == 0) sx = 0;}
            if (keyX != 0 && keyY != 0) normalize();                    	               
            if (keyX == 0 && keyY == 0)
            {
            	tx = ball.x; ty = ball.y;
           		sx = 0; sy = 0;
           		if (tx < x-3) sx = -speed;
                if (tx > x+3) sx = speed;
                if (ty < y-3) sy = -speed;
               	if (ty > y+3) sy = speed;      
			}
               //if (gc.distance(this, ball) <= TAKEBALLDISTANCE && ball.theOwner == null) 
            if (!ball.takeBall(this) && ball.h > h+(BODYH<<8))
            {
                //#ifdef HEADKICKS        
                /*if (Math.abs((ball.fx+(ball.sx*4)) -  fx) < 256*TAKEBALLDISTANCE*2 &&
                    Math.abs((ball.fy+(ball.sy*4)) -  fy) < 256*TAKEBALLDISTANCE*2) {
                        state = JUMP;
                        //gc.humanSoccerPlayer = null;
                        //gc.marca = this;
                        //System.out.println("================"+id);
                }*/
                //#endif
            }
            //else{sx = 0;sy = (attackDirection==0?-speed:speed);}
            if (keyShot && distance(ball) <= STEALDISTANCE) steal();
            break;
                    	
        case OWNBALL:
          	if (timing%4 == 0){
                closestUp = closestToAngle(GameCanvas.bestN, true); 
                closestDown = closestToAngle(GameCanvas.bestS, true); 
                closestLeft = closestToAngle(GameCanvas.bestW, false); 
                closestRight = closestToAngle(GameCanvas.bestE, false); 
          	 }// calculatePass();  
           	if (GameCanvas.firstPass)
            {
                int i = GameCanvas.specialId[team][0];
                //#ifndef NOHEIGHT
                ball.kick(myTeam[i], 256*5, 10, false);   //8
                //#else
                //#endif
                //#ifndef DOJA
                lookAt(myTeam[i].x, myTeam[i].y);      
                //#endif
                GameCanvas.firstPass = false;                        	
                break;
            }                
           	if (ball.gravityVel < 0) ball.gravityVel = 0;
            //if (pass == null) pass = closestPlayer;                                                      
            if (keyX != 0) {sx = keyX*(speed+PLUSSPEED);if (keyY == 0) sy = 0;}
            if (keyY != 0) {sy = keyY*(speed+PLUSSPEED);if (keyX == 0) sx = 0;}
            if (keyX != 0 && keyY != 0) normalize();
                    	  
            //if (gc.distance(this, ball) > width) {state = FREE; break;}
            if (ball.theOwner != this) {state = FREE; break;}
            ball.theOwner = this;
            ball.setPosition(x-(cos((direction+2)*32)>>7),y-(sin((direction+2)*32)>>7));
            //bestPass = higherStatus(myTeam);    	   	                                    

            if (GameCanvas.teamDown == team)                                                                                           
                        switch(direction)
                        {
                            case 7:case 0:case 1: pass = closestUp;break;
                            case 2:case 3:pass = closestRight;break;
                            case 4: pass = closestDown;break;
                            case 5:case 6:pass = closestLeft;break;
                        }
                    else                                
                        switch(direction)
                        {
                            case 0: pass = closestUp;break;
                            case 1:case 2:pass = closestRight;break;
                            case 3:case 4:case 5: pass = closestDown;break;                                                                                
                            case 6:case 7:pass = closestLeft;break;
                        }
                        
            if (pass != null) {
                destPass = pass;
            }
            

            if(keyShot)
            {          
                boolean abarraca = false;
                if (direction == attackDirection || direction == (attackDirection+7)%8 || direction == attackDirection+1) abarraca = true;
                // SHOOT!!
                if (abarraca && (distanceToGoal() < (GROUNDHEIGHT/4) || closestUp == null))
                {
                    int destY = relativeY(GROUNDHEIGHT);
                    //#ifndef NOHEIGHT
                    if (relativeY(y) > GROUNDHEIGHT-(GROUNDHEIGHT/14)) destY = relativeY(GROUNDHEIGHT+(GROUNDHEIGHT/32));                    
                    ball.kick(GameCanvas.pointer, destY, (playerKick*128) + (256*8), 10, keyShotTwo);                    
                    //#else
                    //#endif
                    //if (relativeY(y) > GROUNDHEIGHT-(GROUNDHEIGHT/12)) destY = relativeY(GROUNDHEIGHT+HGROUNDHEIGHT);
                    //ball.kick(gc.pointer, relativeY(GROUNDHEIGHT), (playerKick*90) + 256*8, 0, keyShotTwo);                    
                }
                else
                {                            	                                                                                        			                                
                    if (pass == null && !keyShotTwo)
                        ball.kick(direction, 512+(36*12), 10, keyShotTwo);//20-(playerPass*2));
                    else
                    {
                        //#ifndef NOHEIGHT
                        if (keyShotTwo)
                        {
                            ball.kick(direction, (256*6), 10, true);//20-(playerPass*2));
                        }
                        else {
                            ball.kick(pass, 512 + distance(pass) * 12, playerPass, false);//20-(playerPass*2));
                            lookAt(pass.x, pass.y);
                        }
                        //#else
                        //#endif
                    }
                }                                        	
            }
            break;                    
            
		case STEAL:                                
		    //System.out.println("human steal");
		/*if (ball.theOwner != null && ball.theOwner != this ) 
		{   
		    //System.out.println("human falta");
		             //fault(this, (soccerPlayer)ball.theOwner);                    
		
		    
		
		            	break;}*/
			
			
            if (distance(ball) <= TAKEBALLDISTANCE && gc.rnd.nextInt()%256 > 196-(playerTack*6))
            {
                if (ball.theOwner != null && ball.theOwner != this ) 
                {
                	ball.theOwner.wait(11, W_STEAL);
                    if (ball.theOwner.direction == direction)     
                    {
                        fault((soccerPlayer)ball.theOwner);    
                        wait(3,soccerPlayer.W_STEALFAILED);
                        break;
                    }
                    //ball.theOwner.wait(11, W_STEAL);
                    ball.theOwner.sx = (ball.theOwner.sx + sx)>>1;
            		ball.theOwner.sy = (ball.theOwner.sy + sy)>>1;
                    break;
                }
                ball.takeBall(this);
            }
			             
            if (ball.theOwner == this) ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));                 
            state = STEAL;
            if (--wait <= 0)
            {
                if (ball.theOwner == this) state = OWNBALL;
                else {
                	wait(STEALFAILEDTICK, W_STEALFAILED);
                	sx = sx>>2;
                	sy = sy>>2;//state = FOLLOW;
                }
            }
            break;                    
            
        case WAIT:                    
        	if (--wait <= 0) state = FREE;
        	break;      
                            
        /*case CORNER:
                int destX = GROUNDWIDTH/2;
                int destY = ball.y < HGROUNDHEIGHT ? (GROUNDHEIGHT/7):GROUNDHEIGHT-(GROUNDHEIGHT/7);
                direction = lookAt(destX, destY);        
                //ball.kick(destX, destY, GROUNDWIDTH*24, gc.rnd.nextInt()%8, true);    
                gc.setTeamsState(FREE);    
                System.out.println("corner manual");
                state = KICKBALL;
                break;
                */
        
        case KICKBALL:    
            ball.theOwner = this;    	
            //com.mygdx.mongojocs.sanfermines2006.GameCanvas.humanSoccerPlayer = this;
            GameCanvas.strikeTeam = myTeam;
            freeKick = true;
            int fac = 3;
            moving++;
            //int t = angleShot;
            if (keyX != 0)
            {                
                if (gc.teamDown != team) fac = -3;                
                angleShot += keyX*fac; 
                //angleShot = (angleShot+256)%256;
            }
            else moving = 0;
            
            //if (this == gc.gkA && Math.abs(angleShot-(192-(attackDirection*32))) > 32) angleShot = t;
            //else timing = 0;
            fx = ball.fx - cos(angleShot);
            fy = ball.fy - sin(angleShot);
            x = fx>>8;
            y = fy>>8;
            direction = lookAt(ball.x, ball.y);
            	
            if (keyShot || timing > constants.KICKTIMEOUT)
            {                                                 	
                // SHOOT!!
                //#ifndef NOHEIGHT
                //ball.kick(ball.x+cos(angleShot), ball.y+sin(angleShot), 8*256, 20-(playerFKick<<1), keyShotTwo);
                ball.kick(ball.x+cos(angleShot), ball.y+sin(angleShot), 8*256, playerFKick, keyShotTwo);
                //#else
                //#endif
                freeKick = false;
                GameCanvas.freeKick = false;
            }
            sx = 0;sy = 0;
            break;
                
        case PENALTY:
            sx = 0;sy = 0;
            formed = false;            
            ball.theOwner = this;
            ball.setPosition(x-(cos((direction+2)*32)>>8),y-(sin((direction+2)*32)>>8));
            if(keyShot || timing > constants.KICKTIMEOUT)
            {
                
                 for(int i = 0; i < 10;i++)
                 {
                    if (i != id &&  GameCanvas.teamA[i].state >= 0) GameCanvas.teamA[i].state = FREE;
                    if (GameCanvas.teamB[i].state >= 0) GameCanvas.teamB[i].state = FREE;
                 }
                 //#ifndef NOHEIGHT
                 ball.kick(GameCanvas.pointer, relativeY(GROUNDHEIGHT), 2048, playerFKick,false);
                 //#else
                 //#endif
                 GameCanvas.matchState = constants.GAME;
            }
            direction = lookAt(ball.x, ball.y);
            break;
            
   //#ifndef DOJA
        case OUT:
            //direction = lookAt(closestPlayer.x, closestPlayer.y);        
            //ball.theOwner = this;
            //ball.h = BODYH;
            //calculatePass();
            //#ifndef DOJA
            ball.h = BODYH<<7;
            closestUp = closestToAngle(GameCanvas.bestN, true); 
            closestDown = closestToAngle(GameCanvas.bestS, true); 
            closestLeft = closestToAngle(GameCanvas.bestW, false); 
            closestRight = closestToAngle(GameCanvas.bestE, false); 
            if (keyX != 0) {
                GameCanvas.displKickOut = 0;
            }
            
            if (keyY != 0) {                
                GameCanvas.displKickOut = -keyY;            
            }
            
            if (GameCanvas.outSide < HGROUNDWIDTH) {
                
                if (keyY != 0) {                
                    GameCanvas.displKickOut = keyY;            
                }            
                direction = 2+GameCanvas.displKickOut;
                
            } else {
                
                if (keyY != 0) {                
                    GameCanvas.displKickOut = -keyY;            
                }            
                direction = 6+GameCanvas.displKickOut;
                
            }
                        
            switch(direction)
            {
                case 7:case 1:pass = closestUp;ball.y = y-1;break;
                case 2:pass = closestRight;ball.y = y;break;
                case 3:case 5: pass = closestDown;ball.y = y+1;break;                                                                                
                case 6:pass = closestLeft;ball.y = y;break;
            }
            if (pass == null)     pass = closestLeft;           
            destPass = pass;                      
            //#else
            destPass = closestPlayer;          
            //#endif
            
            //#ifndef DOJA
            if(keyShot || timing > constants.KICKTIMEOUT)
            {     
            //#endif       
                //System.out.println ()
                ball.kickOut(destPass, 512+distance(destPass)*18);                                                     
                //ball.kickOut(closestPlayer, 512+(12*24));
                GameCanvas.setTeamsState(FREE);                                
                wait(6, W_OUT);
            //#ifndef DOJA
            }
            //#endif
            break;     
  //#endif DOJA
            
    }
    
    
    
	fx += sx;
    fy += sy;
    x = fx>>8;                            
    y = fy>>8;                            
   
    if (currentState != state) 
    {  
        timing = 0;
        //System.out.println("id:"+id+" from "+currentState+" to "+state);
    }
     
    if (state != WAIT && state != PENALTY && state != KICKBALL)
         direction = calculateDirection(direction, prex, prey, x, y);       
     
    timing++;     
    animation();       	
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
	    		        //    System.out.println("falta warra");
	    		        //    fault(this, ball.theOwner);
	    		        //}
	    		        //else
	    		        //{
	    		        //System.out.println("takeball");
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
            {myTeam = GameCanvas.teamA; otherTeam = GameCanvas.teamB;speed = soccerPlayer.BASERUNSPEED + 32 + (playerSpeed*26);}
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
            System.out.println("at:"+attackDirection);
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
    
    //if (team == 0) System.out.println("T1: "+timing+" - "+state);
    int prex = x;
    int prey = y;
   	int currentState = state;
    	   
	if (timing%6 == 2) updateVars();
   	   
	
   	//#ifndef NOHEIGHT
    if (gravityVel < constants.MAXGRAVITY*6) gravityVel += constants.GRAVITY*6;
    h -= gravityVel;    
    if (h <= 0) {h = 0; gravityVel = 0;}
    //#endif
   	   
    //if (team == com.mygdx.mongojocs.sanfermines2006.GameCanvas.TEAMB) System.out.println("ST: "+state);
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
            
            //if (relativeY(ball.y) < com.mygdx.mongojocs.sanfermines2006.GameCanvas.AREAHEIGHT) System.out.println("true);
            		 
            //if (relativeY(y) < gc.areaHeight-BODYRADIUS && Math.abs(y-ball.y) < GROUNDHEIGHT/6 && Math.abs(HGROUNDWIDTH-x) <= com.mygdx.mongojocs.sanfermines2006.GameCanvas.AREAWIDTH/2 && gc.distance(this, ball) <= INFLUENCE)
            if (relativeY(ball.y) < GameCanvas.AREAHEIGHT && 
            		 ball.x >= HGROUNDWIDTH - GameCanvas.AREAWIDTH/2 &&
                     ball.x <= HGROUNDWIDTH + GameCanvas.AREAWIDTH/2 &&
                distance(ball) <= INFLUENCE && !GameCanvas.freeKick)
    	    //&& gc.distance(this, ball) <= gc.distance(closestEnemyPlayer, ball)
    	        state = FOLLOW;    	            
    	    palomita();       
    	    break;
    	                	             
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
        		   //Math.abs(HGROUNDWIDTH-ball.x) > com.mygdx.mongojocs.sanfermines2006.GameCanvas.AREAWIDTH/2)
        		   
        		   state = FREE;
        		//System.out.println("follow");
        		palomita();                           
            }
            //#ifndef NOHEIGHT
            if (distance(ball) <= TAKEBALLDISTANCE && ball.h > h+BODYH) {gravityVel = -256*12;}
            //#endif
            break;
                    
        case OWNBALL:
            if (timing == 1) wait = 25;
    	    ball.theOwner = this;    	            	
    	    ball.h = h+(11*256);
    	    //gc.humanSoccerPlayer = null;
            sx = 0;sy = 0;
            if (x < AREAIX) sx = 256;
            if (x > AREAFX) sx = -256;    	            
            if (y > AREAY && GameCanvas.teamDown != team) sy = -256;
            if (y < GROUNDHEIGHT - AREAY && GameCanvas.teamDown == team) sy = 256;                  
    	    boolean close = false;
    	    //if (gc.humanSoccerPlayer != null && gc.distance(this, gc.humanSoccerPlayer) < 32) close = true;
    	    if (--wait <= -1 && !close) state = KICKBALL;
    	    direction = attackDirection;    	                	            
    	    ball.setPosition(x-(cos((direction+2)*32)>>9),y-(sin((direction+2)*32)>>9));
    	    angleShot = 192-(attackDirection*32);    	    
            GameCanvas.strikeTeam = myTeam;
    	    break;
    	            	
        case KICKBALL:    
            ball.h = 11*256;
        	ball.theOwner = this;    	
        	direction = attackDirection;
            if (GameCanvas.teamA == myTeam)
            	gc.humanSoccerPlayer = this;    	            		
        	else
        	{    	  
        	    //System.out.println("***************************"+timing);
        	    //gc.freeKick = true;
        	    //if (--wait <= 1)        	    
        	    //{
        	        
                    ball.kick(attackDirection, 7*256, playerFKick, true);
                	sx = 0;sy = 0;	
                    GameCanvas.freeKick = false;
                	freeKick = false;      		
                //}
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
            //#endif
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
                int i = gc.rnd.nextInt()%32365;
                int k = playerTack * 1000;
                if (team == GameCanvas.TEAMB) k -= 5000;                        
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
    if (currentState != state) timing = 0;
    
    //if (team == com.mygdx.mongojocs.sanfermines2006.GameCanvas.TEAMA) System.out.println("PORA:"+state);
    //else System.out.println("PORB:"+state);
    //NEW
    if (state != FREE && state != WAIT && state != OUT && state != OWNBALL && state != KICKBALL && state != PREPENALTY)
        direction = calculateDirection(direction, prex, prey, x, y);       
        
    animation();
    //if (team == 0) System.out.println("T2: "+timing+" - "+state);
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
    	//#ifndef DOJA
    	if (relativeY(ball.y) < relativeY(y)) return;
    	//#endif
    	//if (gc.matchState == gc.GAME && team == gc.TEAMB && gc.rnd.nextInt()%256 < -(playerTack*25)+64) return;
    	//if (gc.matchState == gc.GAME && team == gc.TEAMA && gc.rnd.nextInt()%256 < -playerTack*25) return;    
        
    if (Math.abs(ball.sy) > 256 && Math.abs(y - ball.y) < 24)
    {
        if ((GameCanvas.teamDown == team && ball.sy > 0)
        || (GameCanvas.teamDown != team && ball.sy < 0))
        {
            int i = 0;
            int tbx = ball.fx;
            int tby = ball.fy;
            //System.out.println("calculando");
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
                //System.out.println("palomita");
                state = PALOMITA;
                if (tbx>>8 > x){
                     sx = speed+256;
                     direction = 2;
                }
                else {sx = -speed-256;direction = 6;}
                sy = 0;
                wait = Math.min(20, Math.max(12, i));
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
    
    
    //gc.marca = gc.humanSoccerPlayer;
    
    numFaults++;
    
    GameCanvas.setTeamsState(FREE);
    GameCanvas.strikeTeam = _good.myTeam;       
	//_good.state = KICKBALL;		
    GameCanvas.kickPlayer = _good;
	_good.wait = 20;
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
	    	GameCanvas.freezeHumanPlayer = 40;
	    	if (ball.theOwner == this) ball.theOwner = null;
	    }
	    //#endif
	     //gc.prefault(_good.myTeam, false);
	}
	if (numFaults >= 3) {
        if (GameCanvas.ejected[team] < 4) {
	        
            GameCanvas.eject[team] = id;
	    } else {
	        numFaults = 2;
	    }
	}
	
	_good.state = WOUNDED;		
	_good.sx = 0;_good.sy = 0;	
	_good.timing = 0;
	wait(7, W_STEALFAILED);
	ball.theOwner = null;
	//System.out.println("======================");
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
final static int ANIMOFFY = 8;

public void animation()
{
    
    //#ifdef LATERALVIEW
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
        if (team != GameCanvas.teamDown)
            animSeq += 3;
    }
    else if (state == WOUNDED)
    {
        //#ifdef GFXLITE
        //#else
        animFrame = Math.min((timing-1)/3,1);
        //#endif
        animSeq += (4*ANIMOFFY)-2;
    }
    else if (state == STEAL)
    {
        //#ifdef GFXLITE
        //#else
        animFrame = Math.min((timing)/2,2);
        //#endif
        animSeq += (3*ANIMOFFY)-2;   
    }
    else if (
        //#ifndef DOJA
        state == JUMP || 
        //#endif
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
            //#else
            animFrame = Math.min((timing-1)/3,1);
            //#endif
            animSeq += (4*ANIMOFFY)-2;
        }
        if (waitReason == W_STEALFAILED) 
        {
            //#ifdef GFXLITE
            //#else
            animFrame = 2;
            //#endif
            animSeq += (3*ANIMOFFY)-2;   
        }
        if (waitReason == W_KICK) 
        {
            //#ifdef GFXLITE
            //#else
            animFrame = Math.min(timing,3);
            //#endif
            animSeq += ANIMOFFY;
        }
        //#ifndef DOJA
        if (waitReason == W_HEADKICK) 
        {
            /*//animFrame = 1;
            if (wait > 2)
                animFrame = 0;
            else 
                animFrame = 1;
            animSeq += (4*8)-2;*/
            //#ifdef GFXLITE
            //#else
            animFrame = 1;
            //#endif
            animSeq += (5*ANIMOFFY)-2;

        }
        //#endif DOJA
    }    
    //#ifndef DOJA
    /*else if (state == PREMATCH && com.mygdx.mongojocs.sanfermines2006.GameCanvas.teamGoal == team && !formed &&  (com.mygdx.mongojocs.sanfermines2006.GameCanvas.nfc>>2)%4 == id%4)
    {
        animFrame = 2;
        //#ifdef LATERALVIEW
        animSeq = (2*ANIMOFFY)+3;                
        //#else
        if (sy > 0) animSeq = (2*ANIMOFFY)+3;
        else animSeq = (2*ANIMOFFY);            
        //#endif
    }*/
    //#endif
    else
    {
        if (sx != 0 || sy != 0) 
        {
            
            //#ifdef GFXLITE
            if (!(timing%2 == 0 && animFrame%3 == 0))               
                animFrame = (++animFrame)%6;                
            //#else
            //#endif
        }
        else 
        {
            if (state == KICKBALL && moving > 1) animFrame = (moving/2)%3;
            else animFrame = 2;        
        }
    }
        
}
// animation ////////////////////////////////////////////////////////////////

   

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
    int direction;
    
    static GameCanvas  gc;
    
    soccerPlayer theOwner;
    
    
    
        
    
    int gravityVel;
    int h;
    
    final static int cornerTable[][] = {{16,-16},{128-16,128+16}};








public void activate(int _x , int _y)
{
    //System.out.println("activate");
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
    //System.out.println("setPos");
    x = _x;
    y = _y;
    fx = x<<8;
    fy = y<<8;
}
    
    
    
public int calculateDirection(int def, int prex, int prey, int x, int y)
{
    int dx = x-prex;
    int dy = y-prey;
    if (dx == 0 && dy == 0) return def;     
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
	//#ifndef NOKICKSOUND
	gc.soundPlay(4,1);
	//#endif
	
	//#ifndef DOJA
	long t = System.currentTimeMillis();
    if (t - GameCanvas.lastVibra > 500)
    {
    	//#endif
    	
    	//#ifdef LONGVIBRA
    	//#else
    	gc.vibraInit(100);
    	//#endif
    	
    	//#ifndef DOJA
    	GameCanvas.lastVibra = t;
    }
    //#endif
    if (power > 256*8) power = 256*8;
    //#ifdef DOJA
    //#else
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
    normalize();
    //#endif
    //if (theOwner != null) 
    theOwner.wait(6, soccerPlayer.W_KICK);
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
    sp.state = soccerPlayer.GOPLACE;
    sp.wait = distance(sp)/4;
    sp.waitReason = sp.wait; //AIXO NO ESTA BE
    int dist = distance(sp);
    sp.tx = x+((sx*dist)/1400);
    sp.ty = y+((sy*dist)/1400);
    //System.out.println("kick");
    //#ifdef OFFSIDE
    /*if (sp.relativeY(y) - sp.relativeY(sp.y) < 0)
    {
    	boolean offside = true;
    	int i = 0;
    	while (offside && i < 5)
    	{
    		if (sp.relativeY(sp.otherTeam[i].y) >= sp.relativeY(sp.y))
    		{
    			offside = false;
    		}
    		i++;
    	}
    	if (offside) gc.preoffside(1-sp.team);
    }*/
    //#endif
    //System.out.println("GO player"+sp.id);            
}
    
//#ifndef NOHEIGHT    
public void kick(int _x, int _y, int power, int desp, boolean highKick)
//#else
//#endif
{
	
 	//#ifndef DOJA
 	long t = System.currentTimeMillis();
    if (t - GameCanvas.lastVibra > 500)
    {
    	//#endif
    	
    	//#ifdef LONGVIBRA
    	//#else
    	gc.vibraInit(100);
    	//#endif
    	
    	//#ifndef DOJA
    	GameCanvas.lastVibra = t;
    }
    //#endif
    if (power > 256*8) power = 256*8;
    //#ifndef NOHEIGHT    
    if (highKick) power = (power*5)/6;
    //#endif
    
    
    int snd = 4;
	if (GameCanvas.firstPass) {desp = 10; snd = 5;}
	//#ifndef NOKICKSOUND
	gc.soundPlay(snd,1);
	//#endif
	
    int dx = _x - x;
    int dy = _y - y;
    int angle = atan(dy, dx);
    desp = ((10-desp)*(GameCanvas.rnd.nextInt()%64))>>4;    
    //System.out.println ("desp: "+desp);
    
    angle += desp;
    sx = ((cos(angle)>>2)*power)>>8;
    sy = ((sin(angle)>>2)*power)>>8;       
    if (theOwner != null) theOwner.wait(6, soccerPlayer.W_KICK);
    theOwner = null;        
    h = 0;
    gravityVel = -power;                
    //#ifndef NOHEIGHT    
    if (highKick) 
    {
        gravityVel -= power;                
        power = power/2;
    }
    //#endif
    
	
}
    
//#ifdef HEADKICKS
/*public void headKick(int _x, int _y, int power)
{
    if (power > 256*8) power = 256*8;
    
    int dx = _x - x;
    int dy = _y - y;
    int angle = atan(dy, dx);
    angle += desp;
    sx = ((cos(angle)>>2)*power)>>8;
    sy = ((sin(angle)>>2)*power)>>8;       
    //fy+=16*256;   
    if (theOwner != null) theOwner.wait(6, soccerPlayer.W_HEADKICK);
    theOwner = null;        
    //h = 0;
    //gravityVel = -power;              
    gravityVel = Math.max(0,gravityVel);                        
}*/
//#endif

    
public void kickOut(soccerPlayer sp, int power)
{
    int _x = sp.x;
    int _y = sp.y;
    //#ifndef DOJA
    if (power > (256*6)) power = (256*6);
    //#endif
    int dx = _x - x;
    int dy = _y - y;
    int angle = atan(dy, dx);       
    sx = ((cos(angle)>>2)*power)>>8;
    sy = ((sin(angle)>>2)*power)>>8;       
    theOwner = null;        
    h = 256*BODYH;
    gravityVel = -power;
    
    // GOPLACE
    sp.state = soccerPlayer.GOPLACE;
    sp.wait = distance(sp)/4;
    int dist = distance(sp);
    sp.tx = x+((sx*dist)/1400);
    sp.ty = y+((sy*dist)/1400);
    
}
    
    
    
public void updateBall()
{         
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
    if (Math.abs(sx) < friction && Math.abs(sy) < friction) {sx = 0;sy = 0;}
    //#ifndef DOJA
    direction = calculateDirection(direction, prex, prey, x, y);
    //#endif
    if (gravityVel < constants.MAXGRAVITY) gravityVel += constants.GRAVITY;
    h -= gravityVel;
    if (h < 0) 
    {   
        h = 0;
        //#ifndef DOJA
        gravityVel = -(gravityVel*8)/9;
        //#else
        //#endif
        //System.out.println("G:"+gravityAcc);
        //if (gravityAcc <= 2) gravityAcc = 0;
    }
    //System.out.println("Ball Height: "+(h>>8));
    //h = Math.max(0, h-gravityAcc);
}





public boolean takeBall(soccerPlayer p)
{
    if (theOwner == p) {p.state = soccerPlayer.OWNBALL;return true;}
    if ((distance(p) <= soccerPlayer.TAKEBALLDISTANCE)
    //{if 5
        && (theOwner == null || theOwner.state != soccerPlayer.STEAL))
        { 
    		
    		if (theOwner != null && p.state != soccerPlayer.STEAL && (p.playerTack+(gc.rnd.nextInt()%4)) < theOwner.playerTack+(gc.rnd.nextInt()%4)) return false;
                //#ifndef NOHEIGHT                
                if ((p.h>>8)+p.BODYH > h>>8)
                //#endif
                {
                			//#ifndef DOJA
                        	if (theOwner != null) 
                        	{
                        		//theOwner.state = WAIT;if (theOwner.wait < 4) theOwner.wait= 4;
                        		//System.out.println("robo"+(timing%10));
                        		theOwner.wait(5, WOUNDED);
                        	}
                        	//#endif
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
                            GameCanvas.strikeTeam = p.myTeam;           
                            GameCanvas.lastStrikeTeam = p.myTeam;   
                            GameCanvas.keyPresTime = 0;
                            theOwner = p;                       
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
    
    //#ifdef J2ME
    temp = gc.loadFile("/Trig.bin");
    //#endif
    
    //#ifdef DOJA
    //#endif
    
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
    //System.out.println("angle: " + angle);
    return (angle);    
}
  
	
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
    //final static int WAITOFFSIDE    = 10;
    //#endif
    
    /////////////////////////////////////////////////  
    
    
    //======================================
    //=== PHYSICS ==========================
    //======================================        
    final static int MAXGRAVITY         = 1024;
    final static int GRAVITY            = 256;
    final static int FRICTION_GROUND    = 40;   
    final static int FRICTION_AIR       = 18;   
 
 
    final static int KICKTIMEOUT        = 150;
    
}
