//#ifdef PACKAGE
package com.mygdx.mongojocs.qblast20;
//#endif

		
public class Ball implements Constant
{	
	public static final int MAXSPEED = 50;
	public static final int BELTSPEED = 3*MAXSPEED/4;
	public static final int MAXYSPEED = 200;
	public static final int FRICTION = 4;
	public static final int SPEEDSTEP = 7;
	public static final int GRAVITY = 12;
	
	public static final int BLACK = 0, ELECTRIC = 1, MAGNETIC = 2, RUBBER = 3, MIRROR = 4;
	
	public static final int PLAY_ST = 0, BROKEN_ST = 1, ELECTRIFIED_ST = 2, REDUCED_ST = 3, CROSS_ST = 4, 
						CROSS_EXIT_ST = 5, REDUCING_ST = 6, EXITING_ST = 7, EXITED_ST = 8;
						
	public static final int XAXIS = 0, YAXIS = 1, ZAXIS = 2;

	int x, y, z, orix, oriy, oriz, realx, realy, realz, velx, vely, velz, groundY, radius, originalRadius, counter, flagYOffset, IAdir, enemyType, state,
		lastx, lasty, lastz, lastvx, lastvy, lastvz, invulnerable, traction, angleu, anglev;
	short collideTimer, badCollide, YCollides;
	GameCanvas lev;
	boolean atGround, lastAtGround, nowCollided, active, collidedWithBall = false, bombAdded = false;
	byte overTile, lastOverTile, type, explInteractAllow[];
	int boundBox[];
	Ball myPlatform;
			
	public Ball()
	{
		groundY = -99999; myPlatform = null; invulnerable = 0; traction = 0; 
		angleu = 0; anglev = 0; collideTimer = 0; active = false; 
		explInteractAllow = new byte[5]; for(int i = 0; i < explInteractAllow.length; i++) explInteractAllow[i] = 0;
		
		boundBox = Box3D(0,0,0,radius - 10,radius - 10,radius - 10);
	}
	
	public void create(GameCanvas l, int xx, int yy, int zz, byte t)
	{
		realCreate(l,(xx<<8)+127,(yy<<8)+127,(zz<<8)+127,t);
	}
	
	public void realCreate(GameCanvas l, int xx, int yy, int zz, byte t)
	{
		counter = 0; lev = l; type = t;
		realx = 0; realy = 0; realz = 0;
		velx = 0; vely = 0; velz = 0; flagYOffset = 0;
		groundY = -99999; myPlatform = null; 
		angleu = 0; anglev = 0; collideTimer = 0; badCollide = 0; YCollides = 0;
		active = true; atGround = true; overTile = 0; traction = 0;
		
		switch(type)
		{
			case PLAYER : radius = 140; l.player = this; state = PLAY_ST; invulnerable = 50; break;
			case BOMB : radius = 70; break;
			case ENEMY : radius = 140; xx++; zz++; break;
			case PLAYER_PIECE : radius = 120; break;
			case LIFT : radius = 128; break;
			case TILE_PIECE : radius = 70;  break;
		}
		
		originalRadius = radius;						
	
		setBoundingBoxSize(radius,radius/2,radius);
		move(xx,yy,zz,0);	
		//#ifdef COLLIDEDEBUG
		Debug.println ("Setting at "+xx+","+yy+","+zz+"; type"+t);		
		//#endif
	}
	
	public void saveOriginalPos()
	{
		orix = x; oriy = y; oriz = z;
	}
	
	public void resetPos()
	{
		realCreate(lev,(orix<<8)+127,(oriy<<8)+127,(oriz<<8)+127,type);
	}
	
	public void moveBoundingBox()
	{		
		boundBox = box3Dset(boundBox,realx,realy + radius - boundBox[7]/2,realz);			
	}
				
	public void setBoundingBoxSize(int sx, int sy, int sz)
	{
										
		boundBox[7] = sy;	
		boundBox[8] = sz;	
		boundBox[6] = sx;	
				
	}
	
	public void cropToMaximumVelocity()
	{
		if(velx>MAXSPEED) velx=MAXSPEED;	if(velx<=-MAXSPEED) velx=-MAXSPEED;	
		if(vely>MAXYSPEED) vely=MAXYSPEED;	if(vely<=-MAXYSPEED) vely=-MAXYSPEED;	
		if(velz>MAXSPEED) velz=MAXSPEED;	if(velz<=-MAXSPEED) velz=-MAXSPEED;				
	}
	
	public void pushToCenter()
	{
		move(realx+(Math.abs((realx%256) - 128) >= 24 ? (realx%256 < 128 ? 24 : -24) : 0),realy,realz,XAXIS);
		move(realx,realy,realz+(Math.abs((realz%256) - 128) >= 16 ? (realz%256 < 128 ? 24 : -24) : 0),ZAXIS);	
	}
	
	public void move(int nx, int ny, int nz, int axis)	
	{
		collidedWithBall = false;
		int aux;
		Ball opp = null, pl = lev.player;
		
		//if(ny < -1024) ny = -10;
		
		lev.unsetFlag(x,(realy+flagYOffset)>>8,z,type);
						
		int bakx = realx, baky = realy, bakz = realz;
			
		realx = nx; realy = ny; realz = nz;
		
		if(realy < groundY + radius) realy = groundY + radius;									
		
		moveBoundingBox();	
					
		nowCollided = lev.boxCollide(this,boundBox); 
				
								
		// Check ring collision
		
		if(type == PLAYER || type == ENEMY)
		if(lev.tile(nx>>8,ny>>8,nz>>8) == RING)
		{	
			int ringAngle = (GameCanvas.ellapsedTicks*2)%128;
			
			//#ifdef COLLIDEDEBUG
			Debug.println ("Ring angle:"+ringAngle);
			//#endif
			
			if(Math.abs(velx) < Math.abs(velz)){
			
				if (ringAngle > 32 && ringAngle <= 96)
					nowCollided = true; 					
			}else{
				if (ringAngle <=32 || ringAngle > 96)
					nowCollided = true; 					
			}
													
		}						
				
		if(y >= 0  && pl.state != BROKEN_ST)		
		if(type == ENEMY)		
		{
			
			if(ballCollide(pl)){
			
				collidedWithBall = true;	
				opp = pl;
				if(pl.state != BROKEN_ST && pl.state < EXITING_ST)
					pl.enemyEffect(this);
			}									
		}
		
		if(type == PLAYER)
		{
			if(GameCanvas.enem != null)
			for(int i = 0; i < GameCanvas.enem.length; i++)
				if(GameCanvas.enem[i].active)
				if(ballCollide(GameCanvas.enem[i])){				
				
					collidedWithBall = true;	
					opp = GameCanvas.enem[i];
					
					enemyEffect(opp);										
				}
		}
		
		if(nowCollided || collidedWithBall)		
		{			
			// Collided			
			int module = 8;
			realx = bakx; realy = baky; realz = bakz; 			 						
			moveBoundingBox();
			if(!collidedWithBall) {
				switch(axis)
				{
					case XAXIS : 
						velx = (Math.abs(velx) < 32 ? -velx : -velx/2); 
						//#ifdef COLLIDEDEBUG
						System.out.println ("# COLLIDE : X Axis collision - v("+velx+","+vely+","+velz+")");
						//#endif
					break;
					case YAXIS : 
						if(vely < 0){ YCollides++; badCollide = 8;} 
						vely = -vely/2; 						
						//#ifdef COLLIDEDEBUG
						System.out.println ("# COLLIDE : Y Axis collision - v("+velx+","+vely+","+velz+") Count("+YCollides+")");
						//#endif
						break;
					case ZAXIS : 
						velz = (Math.abs(velz) < 32 ? -velz : -velz/2); 
						//#ifdef COLLIDEDEBUG
						System.out.println ("# COLLIDE : Z Axis collision - v("+velx+","+vely+","+velz+")");
						//#endif
						break;
				}	
								
				if(type == PLAYER && collideTimer == 0) 
				{
					Game.gc.vibraInit(250);			 
					Game.gc.soundPlay(3,1);					
				}
				
			}else{
				
				// Enemy collided with player
				
				aux = velx; velx = opp.velx; opp.velx = aux;
				aux = vely; vely = opp.vely; opp.vely = aux; 
				aux = velz; velz = opp.velz; opp.velz = aux;
				
				cropToMaximumVelocity();
												
				// Player over the enemy!!!
				if(opp.realy < realy) {
					vely += 64; velx+=(realx > opp.realx ? +64 : -64); velz+=(realz > opp.realz ? +64 : -64);
					cropToMaximumVelocity();
					//#ifdef COLLIDEDEBUG
					System.out.println ("# COLLIDE : "+(this == lev.player ? "Player over an enemy" : "Enemy over a player")+"!!! Jumping");
					//#endif							
				}
								
				//#ifdef COLLIDEDEBUG
				System.out.println ("# COLLIDE : ("+(this == lev.player ? "Player" : "Enemy")+") Enemy collision v("+velx+","+vely+","+velz+"), Opp v("+opp.velx+","+opp.vely+","+opp.velz+")");
				//#endif							
				
				if(ballCollide(opp))
				{
					pl.setState(BROKEN_ST);
					lev.startUpPlayerPieces();
					
					//#ifdef COLLIDEDEBUG
					System.out.println ("# COLLIDE : Player inside the enemy!!!!! What a bizarre thing! Destroying");
					//#endif							
				}
					
			}
			
		} else if(axis == YAXIS) YCollides = 0;
				
		x = realx>>8; y = realy>>8; z = realz>>8;
										
		lev.setFlag(x,(realy+flagYOffset)>>8,z,type);
				
		if(collideTimer == 0 && (nowCollided || collidedWithBall)) collideTimer = 5;
		
	}
	
	public void basicUpdate()
	{
							
		int lvelx, lvely, lvelz, vxpercent, vzpercent, friction;
		
		lvelx = 0; lvely = 0; lvelz = 0;
		vxpercent = 100; vzpercent = 100;

		
		badCollide = 0;
		
		counter++;
		
		lastOverTile = overTile;
		lastAtGround = atGround;
		
		groundY = lev.yAt(radius, realx, realy, realz);
		overTile = lev.nearestTileAt(realx, realy, realz);
				
		lastx = x; lasty = y; lastz = z;  
		
		// Over a lift
			
		if(myPlatform == null)
		{
			if(lev.lifts != null)										
			for(int i = 0; i < lev.lifts.length; i++)				
				if(Math.abs(lev.lifts[i].realx - realx)<128 && Math.abs(lev.lifts[i].realy + lev.lifts[i].radius - realy + radius)<128 && Math.abs(lev.lifts[i].realz - realz) < 128)
				{
					
					myPlatform = lev.lifts[i];
					velx = 0; vely = 0; velz = 0;					
				}					
		}
		else if(Math.abs(myPlatform.realx - realx)<128 && Math.abs(myPlatform.realy + myPlatform.radius - realy + radius)<128 && Math.abs(myPlatform.realz - realz) < 128)
		{
			groundY = myPlatform.realy + myPlatform.radius;
			
			lvelx = myPlatform.velx;					
			lvely = myPlatform.vely;					
			lvelz = myPlatform.velz;						
			
		}else {
			
			// Get free from the platform
			
			velx += myPlatform.velx;
			vely += myPlatform.vely;
			velz += myPlatform.velz;
			myPlatform = null;
		}
		
		if(atGround)
		switch(overTile)
		{			
		
			case RRAMPB : if(velx<0) vxpercent = 80; break;
			case RRAMPD :
			case RRAMPE : if(velx<0) vxpercent = 33; break;
			case LRAMPB : if(velx>0) vxpercent = 80; break;
			case LRAMPD :
			case LRAMPE : if(velx>0) vxpercent = 33; break;
			case DRAMPB : if(velz<0) vzpercent = 80; break;
			case DRAMPD :
			case DRAMPE : if(velz<0) vzpercent = 33; break;
			case URAMPB : if(velz>0) vzpercent = 80; break;
			case URAMPD :
			case URAMPE : if(velz>0) vzpercent = 33; break;
					
			case RBELT : lvelx += BELTSPEED; break;
			case LBELT : lvelx += -BELTSPEED; break;
			case DBELT : lvelz += BELTSPEED; break;
			case UBELT : lvelz += -BELTSPEED; break;			 			
		}
				
		cropToMaximumVelocity();
							
		if(velx + lvelx != 0)
			move(realx + (((velx + lvelx)*vxpercent)/100), realy, realz,XAXIS);			
		if(vely + lvely != 0)
			move(realx, realy + vely + lvely, realz,YAXIS);
		if(velz + lvelz != 0)
			move(realx, realy, realz + (((velz + lvelz)*vzpercent)/100),ZAXIS);
			
			
		/// MAKE A BACK-UP OF THE VELOCITY COMPONENTS
		
		lastvx = velx; lastvy = vely; lastvz = velz;
										
		if(atGround)		
		{
			
			// Gravity at ramps
			
			if(traction == 0){
				switch(overTile)
				{
					case RRAMPB : velx += GRAVITY/2; break;
					case RRAMPD :
					case RRAMPE : velx += (3*GRAVITY)/5; break;
					case LRAMPB : velx -= GRAVITY/2; break;
					case LRAMPD :
					case LRAMPE : velx -= (3*GRAVITY)/5; break;
					case DRAMPB : velz += GRAVITY/2; break;
					case DRAMPD :
					case DRAMPE : velz += (3*GRAVITY)/5; break;
					case URAMPB : velz -= GRAVITY/2; break;
					case URAMPD :
					case URAMPE : velz -= (3*GRAVITY)/5; break;
				}				
			}			
						
			// Friction
			
			friction = (overTile == ICE ? FRICTION/4 : FRICTION);
			if(traction > 0) friction = 3*FRICTION/2;
			
			if(velx>0) if(velx>friction) velx-=friction; else velx=0; if(velx<0) if(velx<-friction) velx+=friction; else velx=0;
			if(velz>0) if(velz>friction) velz-=friction; else velz=0; if(velz<0) if(velz<-friction) velz+=friction; else velz=0;
			
			// Ground attraction						
						
			if(groundY >= realy - radius - 64)
			{
				vely = (groundY + radius) - realy;				
				//#ifdef COLLIDEDEBUG
				if(vely != 0) System.out.println ("# COLLIDE : Ground rolling. Setting Vy="+vely);
				//#endif							
			}
			else
			{ 
				atGround = false;	
				//#ifdef COLLIDEDEBUG
				System.out.println ("# COLLIDE : Leave ground");
				//#endif									
			}
						
			// Leave conveying belts
			if(overTile != lastOverTile || (!atGround && lastAtGround))
				switch(lastOverTile)
				{
					case RBELT : velx += BELTSPEED; break;
					case LBELT : velx += -BELTSPEED; break;
					case DBELT : velz += BELTSPEED; break;
					case UBELT : velz += -BELTSPEED; break;				
				}				
			
			// Spring			
			if(lev.tile(x,y,z) == SPRING && GameCanvas.counter%40 == 2) vely += 128;
			
			// Ring
			if(type == PLAYER)						
			if(lev.tile(x,y,z) == RING){
				
				int ringAngle = (GameCanvas.ellapsedTicks*2)%128;			
																		
				if(Math.abs(velx) >= Math.abs(velz)){
				
					if (ringAngle > 32 && ringAngle <= 96)
						pushToCenter();
				}else{
					if (ringAngle <=32 || ringAngle > 96)
						pushToCenter();
				}
			}
						
			// Broken tiles
		
			if(type == PLAYER)
			if(state != REDUCED_ST)
			if(overTile == BROKEN && atGround)
			{
				lev.addCollapsingTile(x,y,z);
			}
			
			if(overTile == HOLE && radius < 130 && realx%256 > 64 && realx%256 < 192 && realz%256 > 64 && realz%256 < 192) 
				pushToCenter();
											
		}else{
			
			// Touch ground
			
			if(groundY >= realy - radius)
			{
				vely = -vely/3; atGround = true; 
				realy = groundY + radius; 
				
				//#ifdef COLLIDEDEBUG
				System.out.println ("# COLLIDE : Back to ground");
				//#endif							
						
			}
			// Gravity fall	
			else vely -= GRAVITY;			
										
		}
		
		// Avoid secondary effects with collisions
		if(badCollide > 0)
		{
			pushToCenter(); badCollide--;			
			//#ifdef COLLIDEDEBUG
			System.out.println ("# COLLIDE : Repeated collision!!. Pushing. Over tile "+overTile+" ("+( atGround ? "fly" : "ground")+" mode)");
			//#endif								
		}			
		
		
		// Update bounding box size
		if(groundY >= realy - radius - 64){
			
			if(overTile >= RRAMPA && overTile <= URAMPE){
								
				setBoundingBoxSize(radius,radius /4,radius);
			}else					
				setBoundingBoxSize(radius,radius /2,radius);
		}else			
			setBoundingBoxSize(radius,radius*2,radius);
			
						
		moveBoundingBox();
		
		
		if(lev.boxCollide(this,boundBox) || YCollides >= 5)
		{
			//#ifdef COLLIDEDEBUG
			System.out.println ("# COLLIDE : Bounding box conflict! Freeing collision");
			//#endif								
			setBoundingBoxSize(radius,-1000,radius);
			//moveBoundingBox();		
			pushToCenter();
		}
																
	}
	
	public void setState(int s)
	{
		state = s; counter = 0;
	}
	
	public void updatePlayer(GameCanvas ga, int keyX, int keyZ, boolean keyFire)
	{
		
		if(invulnerable > 0) invulnerable--;
		if(traction > 0) traction--;
		if(collideTimer > 0) collideTimer--;
		for(int i = 0; i < explInteractAllow.length; i++)
			if(explInteractAllow[i] > 0) explInteractAllow[i]--;
		bombAdded = false;		
	
		switch(state)
		{
													
			case PLAY_ST :	
			if(atGround)			
			{			
				if(keyX < 0) velx -= SPEEDSTEP;
				if(keyX > 0) velx += SPEEDSTEP;
				if(keyZ < 0) velz -= SPEEDSTEP;
				if(keyZ > 0) velz += SPEEDSTEP;
			}
			
			switch(lev.tile(x,y,z))
			{
				case REDUCER : setState(REDUCING_ST); break;
				case CROSS : setState(CROSS_ST); break;
				case GOAL : setState(EXITING_ST); break;
			}
			
			if(keyFire) 
			if(ga.bombNumber == 0 || !ga.bombLimit) 
				bombAdded = ga.addBomb(realx,realy,realz,false);
			break;
			
			case ELECTRIFIED_ST :
			if(counter >= 60) setState(PLAY_ST);
			break;
			
			case BROKEN_ST :
			counter++;			
			break;
			
			case REDUCING_ST :
			counter++;
			velx=0; vely=0; velz=0; 
			pushToCenter();
			if(counter == 17) ga.soundPlay(9,1); 
			if(counter >= 34) {setState(REDUCED_ST); };
			break;
			
			case REDUCED_ST :	
			radius = originalRadius / 2;
			if(atGround){		
				if(keyX < 0) velx -= SPEEDSTEP;
				if(keyX > 0) velx += SPEEDSTEP;
				if(keyZ < 0) velz -= SPEEDSTEP;
				if(keyZ > 0) velz += SPEEDSTEP;
			}
			
			if(keyFire) 
			if(ga.bombNumber == 0 || !ga.bombLimit) 
				bombAdded = ga.addBomb(realx,realy,realz,false);
			
			if(lev.tile(x,y,z) == GOAL)
			{				
				 setState(EXITING_ST);
			}
			
			if(counter >= 200) {radius = originalRadius; setState(PLAY_ST);}
			break;
			
			case CROSS_ST :
			if(counter == 1){ 
			
				velx = 0; vely = 0; velz = 0; 								
			}
			if(counter > 20){			
				switch((ga.counter/10)%4)
				{
					case 0 : velx = -MAXSPEED; break;
					case 1 : velz = -MAXSPEED; break;
					case 2 : velx = MAXSPEED; break;
					case 3 : velz = MAXSPEED; break;
				}
				ga.vibraInit(250); setState(CROSS_EXIT_ST);
				
			} else pushToCenter();
			
			if(lev.tile(x,y,z) != CROSS) setState(PLAY_ST);
			break;
			
			case CROSS_EXIT_ST :			
			if(counter >= 10) setState(PLAY_ST);
			break;
			
			case EXITING_ST :		
			velx=0; vely=0; velz=0; 
			pushToCenter();
			if(counter == 9) ga.soundPlay(12,1);
			if(counter >= 17) {setState(EXITED_ST); };
			if(lev.tile(x,y,z) != GOAL) setState(PLAY_ST);			
			break;
						
		}
		
		if(state != BROKEN_ST) explosionInteract(ga.expl);											
		
		
		if(state != BROKEN_ST && state != REDUCING_ST)
		{ 
			basicUpdate();
			
			lev.setFlag(x,(realy+flagYOffset)>>8,z,type);
			
			// Take items
			
			byte t = lev.tile(x,y,z);
			
			if(t >= POWER_IT && t <= JEWEL_IT)
			{
				switch(t)
				{
					case POWER_IT : GameCanvas.bombPower++; break;	
					case BOMB_IT : GameCanvas.bombLimit = false; break;	
					case INVUL_IT : invulnerable += 300; break;
					case TIME_IT : GameCanvas.timeTicks+=30*13; break;	
					case GLASSES_IT : GameCanvas.seeHidden = 80; break;	
					case TRACTION_IT : traction += 300; break;	
					case JEWEL_IT : GameCanvas.pickedJewels++; break;	
				}	
				
				switch(t)
				{
					default : ga.soundPlay(10,1); break;	
					case JEWEL_IT : ga.soundPlay(11,1); break;	
				}	
				
				lev.modify(x,y,z,EMPTY);
			}
												
		}
		
		if(atGround && overTile == HIDDEN) lev.modifyGround(realx,realy,realz,HIDDEN,TILE);
												
		// Ball line update
		
		//#ifndef NOBALLLINE
		int angle = GameCanvas.angle, incx = 0, incy = 0;
						
		if(angle<48 || angle>=240)
		{
			incx = lastvx; incy = lastvz;
		}
		if(angle>=48 && angle<112)
		{
			incy = lastvx; incx = -lastvz;
		}
		if(angle>=112 && angle<176)
		{
			incx = -lastvx; incy = -lastvz;
		}
		if(angle>=176 && angle<240)
		{
			incy = -lastvx; incx = lastvz;
		}
										
		anglev = (3328 + anglev + incy*8)%3328;			
		angleu = (2048 + angleu + ((incx*GameCanvas.cos(anglev/13))>>7))%2048;
		//#endif
			
	}
	
	public void updateEnemy(GameCanvas ga, int keyX, int keyZ, boolean keyFire) 
	{
		int dx = 0, dz = 0, dy = 0;
		
		for(int i = 0; i < explInteractAllow.length; i++)
			if(explInteractAllow[i] > 0) explInteractAllow[i]--;
		
		if(enemyType == MIRROR)				
		if(lev.player.state == PLAY_ST)
		{
																
			if(atGround){		
				if(keyX < 0) velx += SPEEDSTEP;
				if(keyX > 0) velx -= SPEEDSTEP;
				if(keyZ < 0) velz += SPEEDSTEP;
				if(keyZ > 0) velz -= SPEEDSTEP;
			}
						
			if(keyFire && lev.player.bombAdded) 
			{
				ga.addBomb(realx,realy,realz,true);				
			}
		}	
		
		explosionInteract(ga.expl);															
				
		basicUpdate();
		
		if(realy < 0 ) {active = false; return;}
					
		if(enemyType != MIRROR)
		{
					
			if(atGround)				
			switch(IAdir){
	 			
	 			case 0 : if(velx < MAXSPEED/4) velx += SPEEDSTEP; velz = velz/2; dx = realx + 128; dz = realz; break;
	 			case 1 : if(velz < MAXSPEED/4) velz += SPEEDSTEP; velx = velx/2; dx = realx; dz = realz + 128; break;
	 			case 2 : if(velx > -MAXSPEED/4) velx -= SPEEDSTEP; velz = velz/2; dx = realx - 128; dz = realz; break;
	 			case 3 : if(velz > -MAXSPEED/4) velz -= SPEEDSTEP; velx = velx/2; dx = realx; dz = realz - 128; break;  			 			
	 		}
	 		
	 		if(lev.checkSolid(this,dx,realy,dz) || !lev.checkSolid(this,dx,(realy-256),dz) || collidedWithBall || counter > 50)
	 		{	 				 			
	 			if(ga.RND(2)==0)
	 			{	 			
	 				IAdir = (4+IAdir+1)%4;
	 				counter = 0;
	 				
	 			}else{
	 			
	 				IAdir = (4+IAdir-1)%4;
	 				counter = 0;
	 			}
	 			 			 				
	 		}
	 		
	 		// Magnetic ball attraction
 		
	 		if(enemyType == MAGNETIC)
	 		{
	 			int an, dist, xpercent, zpercent;
	 			
	 			dx=realx - ga.player.realx;
				dy=0;//realy - ga.player.realy;
				dz=dz=realz - ga.player.realz;
				dist=dx*dx+dy*dy+dz*dz;
				
				xpercent = (dx*dx*100)/dist;
				zpercent = 100 - xpercent;
						
				if(dist>10000 && dist<16777000){				
					ga.player.velx+=((dx > 0 ? 4 : -4)*xpercent)/100;
					ga.player.velz+=((dz > 0 ? 4 : -4)*zpercent)/100;
				}										
	 		}

	 	} 										
				
	}
	
	public void updateBomb(int expl[][])
	{
		if(realy < 0 ) {counter = 100; return;}													
		
		basicUpdate();
		
		// Propagation of explosion	
		
		if(lev.checkFlag(x,y,z,EXPLOSION))
			for(int i = 0; i < expl.length; i++)
				if(expl[i][9] != 0)
					if(explosionBurnsAt(expl[i],x,y,z) != 0)						
							counter = 100;								
	}	
	
	public void updateLift()
	{
		int dx = 0, dy = 0, dz = 0;
		velx = 0; vely = 0; velz = 0;
		
		switch(IAdir)
		{
			case 0 : vely = 16; dy = 128; break;
			case 1 : vely = -16; dy = -128; break;
			case 2 : velx = 8; dx = 128; break;
			case 3 : velx = -8; dx = -128; break;
			case 4 : velz = 8; dz = 128; break;						
			case 5 : velz = -8; dz = -128; break;
			
		}
				
		if(lev.tile((realx+velx+dx)>>8,(realy+vely+dy)>>8,(realz+velz+dz)>>8) != LIFTPATH)
			IAdir = (IAdir+1)%6;		
		else move(realx + velx, realy + vely, realz + velz,-1);
	}
	
	public void explosionInteract(int ex[][])
	{
		int boost=65, burn; 
					
		if(lev.checkFlag(x,y,z,EXPLOSION))		
			for(int i=0; i<ex.length; i++)
			if(explInteractAllow[i] == 0)
			if(ex[i] != null)
			if(ex[i][9] != 0)
			{
				burn = Ball.explosionBurnsAt(ex[i],x,y,z);
				
				if(burn != 0)
				if(explosionFrameAt(ex[i],x,y,z) >= 0 && explosionFrameAt(ex[i],x,y,z) < 5)
				{
					explInteractAllow[i] = 18;
												
					switch(burn){
						case 1 : velx+=boost; break;
						case 2 : velz+=boost; break;
						case 3 : velx-=boost; break;
						case 4 : velz-=boost; break;
						case 5 : vely+=2*boost;  break;
					}
				}
			}			
	}
	
	public boolean ballCollide(Ball b)
	{	
		if(b == null) return false;
		if(!b.active) return false;
			
		int dx = realx - b.realx, dy = realy - b.realy, dz = realz - b.realz, dist = radius/2 + b.radius/2;		
		
		return dx*dx + dy*dy + dz*dz <= dist*dist;
	}
	
	public void enemyEffect(Ball en)
	{
		if(type == PLAYER)
		if(invulnerable == 0)
			switch(en.enemyType)
			{
				case ELECTRIC : if(state == PLAY_ST) {setState(ELECTRIFIED_ST); Game.gc.soundPlay(7,1); Game.gc.vibraInit(2500); } break;
				case MIRROR : 
				case BLACK 	: setState(BROKEN_ST); lev.startUpPlayerPieces(); break;
				case RUBBER : en.velx*=8; en.vely*=8; en.velz*=8; Game.gc.vibraInit(250); break;
			}		
	}
	
	
	/* 3D Box Stuff ===================================================================================================
	 
	 	Box3D b : b = new int[9];
	 		 	
	 	b.botx   : b[0]
	 	b.boty   : b[1]
	 	b.botz   : b[2]
	 	b.topx   : b[3]
	 	b.topy   : b[4]
	 	b.topz   : b[5]
	 	b.sizex  : b[6]
	 	b.sizey  : b[7]
	 	b.sizez  : b[8]
	 	 
	*/
	
	public static int[] Box3D(int x, int y, int z, int sx, int sy, int sz)
	{
		int b[] = new int[9];
		
		b[6] = sx;	
		b[7] = sy;	
		b[8] = sz;	
		
		b = box3Dset(b,x,y,z);
		
		return b;
	}
	
	public static int[] box3Dset(int b[], int x, int y, int z)
	{
		
		b[0] = x - b[6]/2;	b[3] = x + b[6]/2;	
		b[1] = y - b[7]/2;	b[4] = y + b[7]/2;	
		b[2] = z - b[8]/2;	b[5] = z + b[8]/2;	
		
		return b;
	}
	
	
	/* Explosion Stuff =============================================================================================
	 
		Explosion e : e = new int[10];
		
		e.x 		: e[0]
		e.y 		: e[1]
		e.z 		: e[2]
		e.counter 	: e[3]
		e.radius 	: e[4]
		e.lenr 		: e[5]
		e.lenl 		: e[6]
		e.lenu 		: e[7]
		e.lend 		: e[8]
		e.active 	: e[9]
			
	*/ 
	
	public static int[] explosionSet(int e[], GameCanvas l, int xx, int yy, int zz, int rad)
	{
		e[0]=xx; e[1]=yy; e[2]=zz; e[3]=0; e[9]=1; e[4]=rad;	
		int i;		
				
		// Left range
		e[6]=0;
		do
			e[6]++;
		while((l.tile(xx-e[6],yy,zz) <= EMPTY || l.tile(xx-e[6],yy,zz) > SPRING) && e[6]<e[4]);
		
		// Right range
		e[5]=0;
		do
			e[5]++;
		while((l.tile(xx+e[5],yy,zz) <= EMPTY || l.tile(xx+e[5],yy,zz) > SPRING) && e[5]<e[4]);		
		
		// Up range
		e[7]=0;
		do
			e[7]++;
		while((l.tile(xx,yy,zz-e[7]) <= EMPTY || l.tile(xx,yy,zz-e[7]) > SPRING) && e[7]<e[4]);			
		
		// Down range
		e[8]=0;
		do
			e[8]++;
		while((l.tile(xx,yy,zz+e[8]) <= EMPTY || l.tile(xx,yy,zz+e[8]) > SPRING) && e[8]<e[4]);					
		
		
		return e;		
	}

	public static void explosionUpdate(int id)
	{	
		GameCanvas lev = Game.gc;		
		int e[] = GameCanvas.expl[id];
	
		if(e[3] != 0)	
		for(int i=0; i<=e[4]; i++){
			lev.unsetFlag(e[0]+i,e[1],e[2],EXPLOSION);
			lev.unsetFlag(e[0]-i,e[1],e[2],EXPLOSION);
			lev.unsetFlag(e[0],e[1],e[2]+i,EXPLOSION);
			lev.unsetFlag(e[0],e[1],e[2]-i,EXPLOSION);
		}
		
		e[3]++;
		
		if(e[3]>= 18 + 2*e[4])
			e[9]=0;
				
		else {			
			
			for(int i=0; i<=e[4]; i++){
				
				if(i<=e[5])
					lev.setFlag(e[0]+i,e[1],e[2],EXPLOSION);
					
				if(i<=e[6])
					lev.setFlag(e[0]-i,e[1],e[2],EXPLOSION);
					
				if(i<=e[8])
					lev.setFlag(e[0],e[1],e[2]+i,EXPLOSION);
					
				if(i<=e[7])
					lev.setFlag(e[0],e[1],e[2]-i,EXPLOSION);
				
				// Destroy gray cubes
				
					
				if(lev.tile(e[0],e[1],e[2]) == BOX && explosionFrameAt(e,e[0],e[1],e[2]) == 1)
					lev.modify(e[0],e[1],e[2],EMPTY);
						
				if(i <= e[5])
				if(lev.tile(e[0]+i,e[1],e[2]) == BOX && explosionFrameAt(e,e[0]+i,e[1],e[2]) == 1)
					lev.modify(e[0]+i,e[1],e[2], EMPTY);
						
				if(i <= e[6])
				if(lev.tile(e[0]-i,e[1],e[2]) == BOX && explosionFrameAt(e,e[0]-i,e[1],e[2]) == 1)
					lev.modify(e[0]-i,e[1],e[2], EMPTY);
						
				if(i <= e[8])
				if(lev.tile(e[0],e[1],e[2]+i) == BOX && explosionFrameAt(e,e[0],e[1],e[2]+i) == 1)
					lev.modify(e[0],e[1],e[2]+i, EMPTY);
						
				if(i <= e[7])
				if(lev.tile(e[0],e[1],e[2]-i) == BOX && explosionFrameAt(e,e[0],e[1],e[2]-i) == 1)
					lev.modify(e[0],e[1],e[2]-i, EMPTY);
				
			}
		}
	
	}
	
	public static int explosionBurnsAt(int e[], int xx, int yy, int zz)
	{
		if(e[1]==yy){
			if(e[2]==zz){
				if(e[0]==xx) return 5;
				else if(xx>=e[0]-e[4] && xx<e[0]) return 3;
				else if(xx<=e[0]+e[4] && xx>e[0]) return 1;
				else return 0;
			}else if(e[0]==xx){
				if(zz>=e[2]-e[4] && zz<e[2]) return 4;
				else if(zz<=e[2]+e[4] && zz>e[2]) return 2;
				else return 0;
			}else return 0;
		}else return 0;
	}
	
	public static int explosionFrameAt(int e[], int xx, int yy, int zz)
	{
		int dist, fr;
		
		dist = Math.abs(xx - e[0]) + Math.abs(zz - e[2]);
		
		fr = e[3] - (dist*2);
		
		return fr;
	}			
}
	