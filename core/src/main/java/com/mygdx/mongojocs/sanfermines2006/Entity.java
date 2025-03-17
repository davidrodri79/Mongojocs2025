package com.mygdx.mongojocs.sanfermines2006;
//#ifdef DOJA
//#define NOPARTICLES
//#define NONPCS
//#define BUILD_ONE_PLAYER
//#endif


public class Entity {

	
	/*public static final int VECTOR_X = 0;
	public static final int VECTOR_Y = 1;
	public static final int VECTOR_Z = 2;
	
	public static final int BBOX_BOT_X = 0;    
    public static final int BBOX_BOT_Y = 1;    
    public static final int BBOX_BOT_Z = 2;
    public static final int BBOX_SIZE_X = 3;
    public static final int BBOX_SIZE_Y = 4;
    public static final int BBOX_SIZE_Z = 5;*/
	
    public static final int CLASS_NONE = 0;
	public static final int CLASS_PLAYER = 1;
	public static final int CLASS_BULL = 2;
	public static final int CLASS_NPC = 4;
	public static final int CLASS_POOL = 3;
	public static final int CLASS_PARTICLE = 5;
		
	public int rPosX, rPosY, rPosZ;
	public int iBboxBotX, iBboxBotY, iBboxBotZ, iBboxSizeX, iBboxSizeY, iBboxSizeZ;
	public int iMoveAreaBotX, iMoveAreaBotY, iMoveAreaBotZ, iMoveAreaSizeX, iMoveAreaSizeY, iMoveAreaSizeZ;
	public Entity collidable[];
	public int rVelocityX, rVelocityY, rVelocityZ;
	public int rAccelerationX, rAccelerationY, rAccelerationZ;
	
	public int miscData[];
	
	public int entityClass;
	
	public int entitySubclass;
	
	public int entityState;
	
	public boolean mapCollidedFlag;
	
	public int ticks;
	
	public int lifeTime;
	
	public boolean expired;
	
	public Entity link;
	
	public AnimatedSprite sprite;
    
	
    public Entity(int c) {
    	        	  
    	rAccelerationX = 0;
    	rAccelerationY = 0;
    	rAccelerationZ = -128;
    	
    	entityClass = c;
    	
    	collidable = new Entity[0];
    	
    	expired = false;
    	
    	reset();
    	
    }
    
    public void reset() {
    	
    	switch(entityClass) {
    	
    		case CLASS_PLAYER :
    			
    			setBoundingBox(10,10,24);
    			setMoveArea(-STREET_WIDTH/2,-100,0,STREET_WIDTH,10000,1000);
    			rVelocityY = -560;
    			entityState = PLAYER_STATE_WAIT;    			
    			miscData = new int[8];    			
    			miscData[PLAYER_DATA_STAMINA] = PLAYER_MAX_STAMINA;
    			miscData[PLAYER_DATA_TURBOS] = 0;
    			miscData[PLAYER_DATA_ROSE_COUNT] = 0;
    			miscData[PLAYER_DATA_LETTER_FOUND] = 0;
    			   
//#ifdef BUILD_ONE_PLAYER
//#else    			
    			
    			switch (entitySubclass) {
    			
    				case PLAYER_SUBCLASS_VASCO :
					    					
    					miscData[PLAYER_DATA_SPEED_VALUE] = 2;
    					miscData[PLAYER_DATA_RESISTANCE_VALUE] = 2;
    					miscData[PLAYER_DATA_JUMP_VALUE] = 2;
    					miscData[PLAYER_DATA_STRENGTH_VALUE] = 2;
    					break;
    			    					
    				case PLAYER_SUBCLASS_CHICA :
    					    				
    					miscData[PLAYER_DATA_SPEED_VALUE] = 2;
    					miscData[PLAYER_DATA_RESISTANCE_VALUE] = 2;
    					miscData[PLAYER_DATA_JUMP_VALUE] = 3;
    					miscData[PLAYER_DATA_STRENGTH_VALUE] = 0;
    					break;
    					
    				case PLAYER_SUBCLASS_TORERO :
	    				
    					miscData[PLAYER_DATA_SPEED_VALUE] = 2;
    					miscData[PLAYER_DATA_RESISTANCE_VALUE] = 2;
    					miscData[PLAYER_DATA_JUMP_VALUE] = 1;
    					miscData[PLAYER_DATA_STRENGTH_VALUE] = 4;
    					break;
    			}		
//#endif    					
    			
    			break;
    			
//#ifndef NONPCS
    			
    		case CLASS_NPC :
    			
    			setBoundingBox(10,10,24);
    			setMoveArea(-STREET_WIDTH/2,-100,0,STREET_WIDTH,10000,1000);
    			rVelocityY = -560;
    			entityState = NPC_STATE_RUN;
    			miscData = new int[5];    			
    			miscData[NPC_DATA_STAMINA] = NPC_MAX_STAMINA;
    			
    			miscData[NPC_DATA_SPEED_VALUE] = 2;
    			miscData[NPC_DATA_RESISTANCE_VALUE] = 2;
    			miscData[NPC_DATA_JUMP_VALUE] = 2;
    			miscData[NPC_DATA_STRENGTH_VALUE] = 2;
    			break;
    			
//#endif    			
    			
    		case CLASS_BULL :
    			setBoundingBox(14,40,20);
    			setMoveArea(-STREET_WIDTH/2,-1000,0,STREET_WIDTH,10000,1000);
    			rVelocityY = BULL_RUN_SPEED;
    			entityState = BULL_STATE_RUN;
    			miscData = new int[2];    			
    			miscData[BULL_DATA_MAXSPEED] = BULL_RUN_SPEED;
    			miscData[BULL_DATA_SHOWGLOBE] = 0;
    			break;
    			
    		case CLASS_POOL :
    			setBoundingBox(10,10,15);
    			lifeTime = 60;
    			setMoveArea(-STREET_WIDTH/2,-100,0,STREET_WIDTH,10000,1000);
    			break;
    			
//#ifndef NOPARTICLES
    			
    		case CLASS_PARTICLE :
    			//setBoundingBox(5,5,5);
    			setMoveArea(-STREET_WIDTH/2,-100,0,STREET_WIDTH,10000,1000);
    			break;
    			
//#endif    			
    	}
    }
           
    public void setPosition(int x, int y, int z) {
    	
    	rPosX = intToReal(x);
    	rPosY = intToReal(y);
    	rPosZ = intToReal(z);
    }
    
    public void setBoundingBox(int sx, int sy, int sz) {
    	
    	iBboxSizeX = sx;
    	iBboxSizeY = sy;
    	iBboxSizeZ = sz;   
    	
    	updateBoundingBox();
    }
    
    public void setState(int s) {
    	
    	entityState = s;
    	ticks = 0;
    }
    
    public void setMoveArea(int x, int y, int z, int sx, int sy, int sz) {
    	
    	    	    
    	iMoveAreaBotX = x;    
    	iMoveAreaBotY = y;    
    	iMoveAreaBotZ = z;
    	iMoveAreaSizeX = sx;
    	iMoveAreaSizeY = sy;
    	iMoveAreaSizeZ = sz;
    	
    }
    
    public void move() {
    	
    	boolean boundaryCollideX = false;
    	boolean boundaryCollideY = false;
    	boolean boundaryCollideZ = false;
    	boolean mapCollideX = false;
    	boolean mapCollideY = false;
    	boolean mapCollideZ = false;
    	
    	int bakX = rPosX;
    	int bakY = rPosY;
    	int bakZ = rPosZ;
    	
    	byte tile;
        
    	//#ifdef FAST150
    	//#else
    	
    	rPosX += rVelocityX;
    	
    	//#endif
    	
    	updateBoundingBox();
    	
    	boundaryCollideX = iBboxBotX < iMoveAreaBotX ||     	    	   
    	   iBboxBotX + iBboxSizeX >= iMoveAreaBotX + iMoveAreaSizeX;
    	   
    	for(int i = 0; i < collidable.length; i++) {
    		 
    		boundaryCollideX = boundaryCollideX || bboxIntersection(this, collidable[i]); 
    	}
    	   
    	tile = mapTileType(iBboxBotX + iBboxSizeX / 2, iBboxBotY /*+ iBboxSizeY / 2*/);
    	
    	mapCollideX = (tile == TILE_WALL); 
    	
    	
    	if(boundaryCollideX || mapCollideX){
    		    		    		
    		// Restore last position
			
    		rPosX = bakX;
        
        	updateBoundingBox();
    	}
    	
    	//#ifdef FAST150
    	//#else
    	
    	rPosY += rVelocityY;
    	
    	//#endif
    	
    	updateBoundingBox();
    	
    	boundaryCollideY = iBboxBotY < iMoveAreaBotY ||     	    	   
    	   iBboxBotY + iBboxSizeY >= iMoveAreaBotY + iMoveAreaSizeY;
    	
    	tile = mapTileType(iBboxBotX + iBboxSizeX / 2, iBboxBotY /*+ iBboxSizeY / 2*/);   
    	   
    	mapCollideY = (tile == TILE_WALL); // mapCollision(tile);
    	
    	for(int i = 0; i < collidable.length; i++) {
   		 
    		boundaryCollideY = boundaryCollideY || bboxIntersection(this, collidable[i]); 
    	}
    		    		
    	
    	if(boundaryCollideY || mapCollideY) {
    	
    		// Restore last position
			
    		rPosY = bakY;
        
        	updateBoundingBox();
    	}
    	
    	//#ifdef FAST150
    	//#else
    	
    	rPosZ += rVelocityZ;
    	
    	//#endif
    	
    	updateBoundingBox();
    	
    	boundaryCollideZ = iBboxBotZ < iMoveAreaBotZ ||     	    	   
    	   iBboxBotZ + iBboxSizeZ >= iMoveAreaBotZ + iMoveAreaSizeZ;
    	   
    	for(int i = 0; i < collidable.length; i++) {
      		 
       		boundaryCollideZ = boundaryCollideZ || bboxIntersection(this, collidable[i]); 
       	}
    	   
    	mapCollideZ = false;//mapCollision(iBboxBotX + iBboxSizeX / 2, iBboxBotY + iBboxSizeY / 2);
    		    		
    	if(boundaryCollideZ || mapCollideZ) {
    	
    		// Restore last position
			
    		rPosZ = bakZ;
        
        	updateBoundingBox();
    	}
    	
    	//mapCollidedFlag = mapCollideX || mapCollideY || mapCollideZ;
    	mapCollidedFlag = mapCollideY;
    	
    	// Corners
    	
    	if(entityClass == CLASS_PLAYER) {
    		
	    	while (mapCollision(iBboxBotX, iBboxBotY)    		    
	    			&& !mapCollision(iBboxBotX + iBboxSizeX, iBboxBotY)) {
	    			
	    			rPosX += 256;
	    			
	    			updateBoundingBox();
	    			
	    		}    		
	    	    	
	    	while (mapCollision(iBboxBotX + iBboxSizeX, iBboxBotY) 
	    			&& !mapCollision(iBboxBotX, iBboxBotY)) {
	    			
	    			rPosX -= 256;
	    			
	    			updateBoundingBox();
	    			    			
	    		}    		
    	}
    	    	   
    }
    
    public void physicsUpdate() {
    	
    	boolean boundaryCollideX = false;
    	boolean boundaryCollideY = false;
    	boolean boundaryCollideZ = false;
    	boolean mapCollideX = false;
    	boolean mapCollideY = false;
    	boolean mapCollideZ = false;
    	
    	int bakX = rPosX;
    	int bakY = rPosY;
    	int bakZ = rPosZ;
    	
    	//#ifdef FAST150
    	//#else
    	
    	rVelocityX += rAccelerationX;
    	rVelocityY += rAccelerationY;
    	rVelocityZ += rAccelerationZ;
    	
    	//#endif
        	
    	//#ifdef FAST150
    	//#else
    	
    	rPosX += rVelocityX;
    	
    	//#endif
    	
    	updateBoundingBox();
    	
    	boundaryCollideX = iBboxBotX < iMoveAreaBotX ||     	    	   
    	   iBboxBotX + iBboxSizeX >= iMoveAreaBotX + iMoveAreaSizeX;
    	   
   
    	if(boundaryCollideX || mapCollideX){
    		    		    		
    		// Restore last position
			
    		rPosX = bakX;
    		
    		rVelocityX /= -2;
        
        	updateBoundingBox();
    	}
    	
    	//#ifdef FAST150
    	//#else
    	
    	rPosY += rVelocityY;
    	
    	//#endif
    	
    	updateBoundingBox();
    	
    	boundaryCollideY = iBboxBotY < iMoveAreaBotY ||     	    	   
    	   iBboxBotY + iBboxSizeY >= iMoveAreaBotY + iMoveAreaSizeY;
    	   
   	
    	if(boundaryCollideY || mapCollideY) {
    	
    		// Restore last position
			
    		rPosY = bakY;
    		
    		rVelocityY /= -2;
        
        	updateBoundingBox();
    	}
    	    	
    	//#ifdef FAST150
    	//#else
    	
    	rPosZ += rVelocityZ;
    	
    	//#endif
    	
    	updateBoundingBox();
    	
    	boundaryCollideZ = iBboxBotZ < iMoveAreaBotZ ||     	    	   
    	   iBboxBotZ + iBboxSizeZ >= iMoveAreaBotZ + iMoveAreaSizeZ;
    	   
    		    		
    	if(boundaryCollideZ || mapCollideZ) {
    	
    		// Restore last position
			
    		rPosZ = bakZ;
    		
    		rVelocityZ = (3 * rVelocityZ) / 4;;
    		rVelocityX = (3 * rVelocityX) / 4;
    		rVelocityY = (3 * rVelocityY) / 4;
        
        	updateBoundingBox();
    	}
    	
    	mapCollidedFlag = mapCollideX || mapCollideY || mapCollideZ;
   
    }
    
    public void pointPhysicsUpdate() {
    	
    	boolean boundaryCollideX = false;
    	boolean boundaryCollideY = false;
    	boolean boundaryCollideZ = false;
    	
    	
    	int bakX = rPosX;
    	int bakY = rPosY;
    	int bakZ = rPosZ;
    	
    	//#ifdef FAST150
    	
    	rVelocityX += rAccelerationX + (rAccelerationX>>1);
    	rVelocityY += rAccelerationY + (rAccelerationY>>1);
    	rVelocityZ += rAccelerationZ + (rAccelerationZ>>1);
    	
    	//#else
    	//#endif
        	
    	//#ifdef FAST150
    	//#else
    	
    	rPosX += rVelocityX;
    	
    	//#endif
    	    	    
    	boundaryCollideX = realToInt(rPosX) < iMoveAreaBotX ||     	    	   
    		realToInt(rPosX) >= iMoveAreaBotX + iMoveAreaSizeX;
    	   
   
    	if(boundaryCollideX){
    		    		    		
    		// Restore last position
			
    		rPosX = bakX;
    		
    		rVelocityX /= -2;
                
    	}
    	
    	//#ifdef FAST150
    	//#else
    	
    	rPosY += rVelocityY;
    	
    	//#endif
    	    	    
    	boundaryCollideY = realToInt(rPosY) < iMoveAreaBotY ||     	    	   
    		realToInt(rPosY) >= iMoveAreaBotY + iMoveAreaSizeY;
    	   
   	
    	if(boundaryCollideY) {
    	
    		// Restore last position
			
    		rPosY = bakY;
    		
    		rVelocityY /= -2;
                	
    	}
    	
    	
    	//#ifdef FAST150
    	//#else
    	
    	rPosZ += rVelocityZ;
    	
    	//#endif
    	
    	    	
    	boundaryCollideZ = realToInt(rPosZ) < iMoveAreaBotZ ||     	    	   
    		realToInt(rPosZ) >= iMoveAreaBotZ + iMoveAreaSizeZ;
    	   
    		    		
    	if(boundaryCollideZ) {
    	
    		// Restore last position
			
    		rPosZ = bakZ;
    		
    		rVelocityZ = (3 * rVelocityZ) / 4;
    		rVelocityX = (3 * rVelocityX) / 4;
    		rVelocityY = (3 * rVelocityY) / 4;
                	
    	}
    	    	   
    }
    
    public void doUpdate() {
    	
    	switch(entityClass) {
    	
    		case CLASS_PLAYER :
    			playerUpdate();    			
    			break;
    			
    		//#ifndef NONPCS
    			
    		case CLASS_NPC :
    			npcUpdate();    			
    			break;
    			
    		//#endif
    			
    		case CLASS_BULL :
    			bullUpdate();
    			break;
    			
    		case CLASS_POOL :
    			poolUpdate();
    			break;
    			
            //#ifndef NOPARTICLES	
    			
    		case CLASS_PARTICLE :
    			particleUpdate();
    			break;
    			
    		default :
    			break;
    		
    		//#endif
    	}
    	
    }
    
    public void updateBoundingBox() {
    	    	
    	iBboxBotX = realToInt(rPosX);
    	iBboxBotY = realToInt(rPosY);
    	iBboxBotZ = realToInt(rPosZ);    	
    }
    
    /*
     * Player Class - Implementation
     * ============================================================================
     */
    
    /*
     * Subclasses
     */
    
    public static final int PLAYER_SUBCLASS_VASCO = 0;
    public static final int PLAYER_SUBCLASS_CHICA = 1;
    public static final int PLAYER_SUBCLASS_TORERO = 2;
        
    /*
     * States
     */
        
    public static final int PLAYER_MAX_STAMINA = 1000;
    public static final int PLAYER_TURBO_MAX_SPEED = -1250;
    public static final int PLAYER_RIDE_MAX_SPEED = -1250;
    public static final int PLAYER_MAX_TURBOS = 1;
    public static final int PLAYER_RIDE_DURATION_TICKS = 80;
    public static final int PLAYER_TURBO_DURATION_TICKS = 40;
    public static final int PLAYER_STAMINA_UP_RECOVERY = 200;
    public static final int PLAYER_SPRING_IMPULSE = 2048;
    
    /*
     * Data resources
     */
        
    public static final int PLAYER_DATA_SPEED_VALUE = 0;
    public static final int PLAYER_DATA_RESISTANCE_VALUE = 1;
    public static final int PLAYER_DATA_JUMP_VALUE = 2;
    public static final int PLAYER_DATA_STRENGTH_VALUE = 3;
    public static final int PLAYER_DATA_STAMINA = 4;
    public static final int PLAYER_DATA_TURBOS = 5;
    public static final int PLAYER_DATA_ROSE_COUNT = 6;
    public static final int PLAYER_DATA_LETTER_FOUND = 7;
    
    /*
     * Player states
     * -------------
     */
    
    public static final int PLAYER_STATE_RUN = 1;
    public static final int PLAYER_STATE_JUMP = 2;
    public static final int PLAYER_STATE_CROUCH = 3;
    public static final int PLAYER_STATE_TURBO = 4;
    public static final int PLAYER_STATE_HIGH_JUMP = 5;    
    public static final int PLAYER_STATE_RIDING = 6;
    public static final int PLAYER_STATE_HORNED = 7;
    public static final int PLAYER_STATE_WAIT = 8;
    
    
   
    public void playerUpdate() {
    	
    	ticks++;
    	
    	//#ifdef FAST150
    	if(ticks % 3 == 0) {    	
    		ticks++;
    	}
    	//#endif
    	
    	switch(entityState) {
    	
    		case PLAYER_STATE_WAIT:
    			    			
    			sprite.updateFrameLoop();
    			    			
    			break;
    	
    			
    		case PLAYER_STATE_RUN:
    			
    			    			    			    			
	    		if (sprite.sequence != GameCanvas.CORRER_B0_S0) {
	    	    		
	    	    	if(sprite.animFinished) {
	    	    			
	    	    		sprite.setSequence(GameCanvas.CORRER_B0_S0);
	    	    			
	    	    	} else {
	    	    		
	    	    		sprite.updateFrame();
	    	    	}
	    	    		
	    	    } else {
	    	    		    	    		        				
	        		sprite.updateFrameLoop();
	        			
	    	    }
    			    			    		    	    	    			    			    			    			   
    			if(GameCanvas.gc.keyX > 0) {
    	    		
    				rVelocityX = 512;
    				
    	    	} else if(GameCanvas.gc.keyX < 0) {
    	    		
    	    		rVelocityX = -512;
    	    		
    	    	} else {
    	    	
    	    		rVelocityX = 0;
    	    	    			
	    			if(GameCanvas.gc.keyY < 0) {
	    				
	    				rVelocityZ = 512 + 128 * miscData[PLAYER_DATA_JUMP_VALUE];
	    				
	    				setState(PLAYER_STATE_JUMP);
	    				
	    				sprite.setSequence(GameCanvas.SALTAR_B0_S1);
	    				
	    			}
    	    	}
    			
    			/*if(com.mygdx.mongojocs.sanfermines2006.GameCanvas.gc.keyY > 0) {
    				
    				rPosY += 4096;
    				updateBoundingBox();
    				    				    				
    			}*/
    	    	    			    			
    			playerVelocityUpdate();
    			
    			move();
    			
    			//#ifdef FAST150
    			//#else
    			
    			miscData[PLAYER_DATA_STAMINA] -= 5 - miscData[PLAYER_DATA_RESISTANCE_VALUE];
    			
    			//#endif
    			    			    		    			    	    
    	    	if(miscData[PLAYER_DATA_TURBOS] > 0 && GameCanvas.gc.keyMenu > 0 && GameCanvas.gc.lastKeyMenu == 0) {
    	    		
    	    		miscData[PLAYER_DATA_TURBOS]--;
    	    		setState(PLAYER_STATE_TURBO);
    	    		sprite.setSequence(GameCanvas.TURBO_PARTE1_B0_S10);
    	    		rVelocityY = PLAYER_TURBO_MAX_SPEED;
    	    		rVelocityZ = 64;
    	    	}
    	    	    	    	
    			break;
    			
    		case PLAYER_STATE_HIGH_JUMP:
    		case PLAYER_STATE_JUMP:
    			
    			if (entityState == PLAYER_STATE_JUMP || ticks %2 == 0) {
    			
    				sprite.updateFrame();
    			}
    			
    			rVelocityX = 0;
    			
    			//#ifdef FAST150
    			//#else
    			rVelocityZ -= 128;
    			//#endif
    			    			    			    			    		
    			move();
    			
    			if(iBboxBotZ <= 3) {
    				
    				rPosZ = 0;
    				
    				updateBoundingBox();
    				
    				setState(PLAYER_STATE_RUN);
    				
    				sprite.setSequence(GameCanvas.CORRER_B0_S0);
    				    				
    			}
    			      		
    			break;
    			    	    			
    		case PLAYER_STATE_TURBO:
    			
    			if(sprite.sequence == GameCanvas.TURBO_PARTE1_B0_S10) {
    			
    				sprite.updateFrame();
    				
    				if(sprite.animFinished) {
    					
    					sprite.setSequence(GameCanvas.TURBO_PARTE2_B0_S11);
    				}
    				
    			} else {
    				
    				sprite.updateFrameLoop();
    			}
    			
    			
    			rVelocityX = 0;
    			
    			playerVelocityUpdate();
    			
    			move();
    			
    			//#ifndef NOPARTICLES
    			
    			if(ticks % 2 == 0) {
    				
    				GameCanvas.gc.createSmoke(iBboxBotX + iBboxSizeX / 2, iBboxBotY + iBboxSizeY / 2, iBboxBotZ + iBboxSizeZ / 2,PARTICLE_SUBCLASS_SMOKE);
    			}
    			
    			//#endif
    			    			    		
    			if(ticks > PLAYER_TURBO_DURATION_TICKS) {
    				
    				setState(PLAYER_STATE_JUMP);
    				sprite.setSequence(GameCanvas.TURBO_PARTE3_B0_S12);
    				rVelocityZ = 0;
    				
    				//#ifndef NOPARTICLES
    				GameCanvas.gc.createTurboEndingRocket(this);
    				//#endif
    			}
    			break;
    			
    		case PLAYER_STATE_RIDING:
    			
    			sprite.updateFrameLoop();
    			
    			if(GameCanvas.gc.keyX > 0) {
    	    		
    				rVelocityX = 448 + 32*miscData[PLAYER_DATA_SPEED_VALUE];
    				
    	    	} else if(GameCanvas.gc.keyX < 0) {
    	    		
    	    		rVelocityX = -448 - 32*miscData[PLAYER_DATA_SPEED_VALUE];
    	    		
    	    	} else {
    	    	
    	    		rVelocityX = 0;
    	    	}
    			    			    	    	    			    		
    			playerVelocityUpdate();
    			
    			move();
    			
    			//#ifndef NOPARTICLES
    			
    			if(ticks % 2 == 0) {
    				
    				GameCanvas.gc.createSmoke(iBboxBotX + iBboxSizeX / 2, iBboxBotY + iBboxSizeY, iBboxBotZ,PARTICLE_SUBCLASS_SMOKE);
    			}
    			
    			//#endif
    			
    			if(ticks > PLAYER_RIDE_DURATION_TICKS) {
    				
    				rPosZ = intToReal(5);
    				rVelocityZ = 1500;
    				setState(PLAYER_STATE_JUMP);
    				sprite.setSequence(GameCanvas.SALTAR2_B0_S7);
    				
    				//#ifndef NOPARTICLES
    				GameCanvas.gc.createExplosion(iBboxBotX + iBboxSizeX/2, iBboxBotY + iBboxSizeY/2);
    				//#endif
    			}    			
    			break;    			
    	    			
    		case PLAYER_STATE_HORNED:
    			
    			if(ticks % 2 == 0) {
    				
    				if(sprite.sequence != GameCanvas.PISADO_B0_S3 && sprite.animFinished) {
    				
    					sprite.setSequence(GameCanvas.CORNEADO_B0_S2/*com.mygdx.mongojocs.sanfermines2006.GameCanvas.PISADO_B0_S3*/);
    					
    				} else {
    				
    					sprite.updateFrame();
    				}
    			}
    			 			
    			physicsUpdate();
    			
    			break;    			    		
    	}
    	    	
    	    	    	    	    		        
    	if(miscData[PLAYER_DATA_STAMINA] < 0) {
    	
    		miscData[PLAYER_DATA_STAMINA] = 0;
    	}
    	
    	if(miscData[PLAYER_DATA_STAMINA] > PLAYER_MAX_STAMINA) {
        	
    		miscData[PLAYER_DATA_STAMINA] = PLAYER_MAX_STAMINA;
    	}
    	
    	// Obstacles
    	
    	int mapX = iBboxBotX + iBboxSizeX/2;
    	int mapY = iBboxBotY + iBboxSizeY/2;

    	
    	switch(mapTileType(mapX, mapY)) {
    	
    		case TILE_BARRIER:    
    			
    			if(entityState != PLAYER_STATE_TURBO && entityState != PLAYER_STATE_HIGH_JUMP && entityState != PLAYER_STATE_RIDING) {
    				
    				rVelocityX -= ((6 - miscData[PLAYER_DATA_STRENGTH_VALUE]) * rVelocityX) / 18;
    				rVelocityY -= ((6 - miscData[PLAYER_DATA_STRENGTH_VALUE]) * rVelocityY) / 18;
    				    			
    			}
    			
    			if(mapTile(mapX, mapY) == 16) {
    			
    				// Create beer pool
    				
    				if(entityState != PLAYER_STATE_HORNED) {
    				
    					GameCanvas.gc.createPool(mapX, mapY);
    				}
    				
    			}
    			
    			if(iBboxBotZ < TILE_WIDTH*2) {
    				
    				setMapTile(mapX, mapY, TILE_EMPTY);    				
    				GameCanvas.gc.vibraInit(50);
    			}
    		break;
    		
    	    		
    		case TILE_LOW_OBSTACLE:    
    			
    			if(entityState != PLAYER_STATE_TURBO && entityState != PLAYER_STATE_JUMP && 
    			   entityState != PLAYER_STATE_HIGH_JUMP && entityState != PLAYER_STATE_RIDING &&
    			   entityState != PLAYER_STATE_HORNED) {
    				
    				if(rVelocityX < -512) {
    					rVelocityX = -512;
    				}
    				if(rVelocityX > 512) {
    					rVelocityX = 512;
    				}
    				if(rVelocityY < -512) {
    					rVelocityY = -512;
    				}
    				
    				if(sprite.sequence == GameCanvas.CORRER_B0_S0) {
    					
    					sprite.setSequence(GameCanvas.TROPEZAR_B0_S9);
    					
    					GameCanvas.gc.vibraInit(20);
    				}
    				    				
    			}   
    			
    			//#ifndef NOPARTICLES
				
				// Water splash

    			if(entityState != PLAYER_STATE_TURBO && entityState != PLAYER_STATE_JUMP && 
    	    		   entityState != PLAYER_STATE_HIGH_JUMP &&
    	    		   entityState != PLAYER_STATE_HORNED) {
				
    				if(mapTile(mapX, mapY) == 23) {
					
    					GameCanvas.createWaterSplash(iBboxBotX + iBboxSizeX / 2, iBboxBotY + iBboxSizeY / 2);
    				}
    			}				
				//#endif
    		break;
    		
    		case TILE_SPRING:    
    			
    			if(entityState == PLAYER_STATE_RUN || entityState == PLAYER_STATE_CROUCH) {
    				
    				rVelocityZ = PLAYER_SPRING_IMPULSE;
    				
    				setState(PLAYER_STATE_HIGH_JUMP);
    				
    				sprite.setSequence(GameCanvas.SALTAR2_B0_S7);
    				
    				GameCanvas.gc.vibraInit(50);
    			}
    		break;
    		
    			
    		case TILE_STAMINA_UP:
    			if(iBboxBotZ < TILE_WIDTH*2) {
    				miscData[PLAYER_DATA_STAMINA] += PLAYER_STAMINA_UP_RECOVERY;
    				setMapTile(mapX, mapY, TILE_EMPTY);
    			}
    		break;
    		
    		case TILE_TURBO:
    			if(iBboxBotZ < TILE_WIDTH*2) {
    				if(miscData[PLAYER_DATA_TURBOS] < PLAYER_MAX_TURBOS) {
    					miscData[PLAYER_DATA_TURBOS]++;
    					setMapTile(mapX, mapY, TILE_EMPTY);
    				}
    			}
    		break;
    		
    		case TILE_MOTORBIKE:        			
    			if(entityState == PLAYER_STATE_RUN || entityState == PLAYER_STATE_CROUCH) {
    				    				    			
    				setState(PLAYER_STATE_RIDING);
    				sprite.setSequence(GameCanvas.MOTO_B0_S5);
    				rPosZ += intToReal(7);
    				rVelocityY = PLAYER_RIDE_MAX_SPEED;
    				setMapTile(mapX, mapY, TILE_EMPTY);
    			}
    		break;
    		
    		case TILE_ROSE:
    			if(entityState != PLAYER_STATE_JUMP && entityState != PLAYER_STATE_HIGH_JUMP && entityState != PLAYER_STATE_HORNED) {
    				
    				miscData[PLAYER_DATA_ROSE_COUNT]++;
    				setMapTile(mapX, mapY, TILE_EMPTY);
    			}
    		break;
    		
    		case TILE_5_ROSES:
    			if(entityState != PLAYER_STATE_JUMP && entityState != PLAYER_STATE_HIGH_JUMP && entityState != PLAYER_STATE_HORNED) {
    				
    				miscData[PLAYER_DATA_ROSE_COUNT] += 5;
    				setMapTile(mapX, mapY, TILE_EMPTY);
    			}
    		break;
    		
    		//#ifndef BUILD_ONE_PLAYER
    		
    		case TILE_LETTER:
    			if(GameCanvas.gc.ferminesItem[GameCanvas.gc.levelSelected - 1] == 0) {
    				
    				if(entityState != PLAYER_STATE_JUMP && entityState != PLAYER_STATE_HIGH_JUMP && entityState != PLAYER_STATE_HORNED) {
    				    				
    					setMapTile(mapX, mapY, TILE_EMPTY);
    				
    					// Pick up a letter
    				
    					miscData[PLAYER_DATA_LETTER_FOUND] = 1;
    				}
    			}
    		break;
    		
    		//#endif
    	}
    	     	
    }
    
    private void playerVelocityUpdate() {
    	
    	int maximum = -700 - (50 * miscData[PLAYER_DATA_SPEED_VALUE]) - (miscData[PLAYER_DATA_STAMINA] / 3);
    	 
    	
    	if(entityState == PLAYER_STATE_JUMP || entityState == PLAYER_STATE_HIGH_JUMP) {
    		
    		maximum = PLAYER_RIDE_MAX_SPEED;
    	}
    	
    	if(entityState == PLAYER_STATE_RIDING) {
    		
    		maximum = PLAYER_RIDE_MAX_SPEED;
    	}
    	
    	if(entityState == PLAYER_STATE_TURBO) {
    		
    		maximum = PLAYER_TURBO_MAX_SPEED;
    	}
    	
       	if(mapCollidedFlag) {
       		
       		rVelocityZ = 0;
       		
       		if(entityState == PLAYER_STATE_TURBO) {
        		
        		setState(PLAYER_STATE_JUMP);
        		        	
        	}
    	
       	} else {
       		
       		if(rVelocityY > maximum) {
    		
       			//#ifdef FAST150
       			//#else
       			rVelocityY -= 15;
       			//#endif
       		}    	       		
       		
       		if(rVelocityY < maximum) {
   				
   				rVelocityY = maximum;
   			}
       	}
    	    	
    }
    
    public void playerLookBackwards() {
    	    	    	
    	if (sprite.sequence == GameCanvas.CORRER_B0_S0) {
						
			sprite.setSequence(GameCanvas.CORRER_MIRAR_ATRAS_B0_S8);
			
		}	
    	    
    }
    
    /*
     * NPC Class - Implementation
     * ============================================================================
     */
    
    //#ifndef NONPCS
    
    public static final int NPC_MAX_STAMINA = 1000;
        
    /*
     * Data resources
     */
        
    public static final int NPC_DATA_SPEED_VALUE = 0;
    public static final int NPC_DATA_RESISTANCE_VALUE = 1;
    public static final int NPC_DATA_JUMP_VALUE = 2;
    public static final int NPC_DATA_STRENGTH_VALUE = 3;
    public static final int NPC_DATA_STAMINA = 4;

    
    /*
     * Player states
     * -------------
     */
    
    public static final int NPC_STATE_RUN = 1;
    public static final int NPC_STATE_JUMP = 2;
    public static final int NPC_STATE_HORNED = 3;
    
       
    public void npcUpdate() {
    	
    	ticks++;
    	
    	//#ifdef FAST150
    	//#endif

    	    	
    	int AIKeyX = 0, AIKeyY = 0;
    	
    	
    	// Update only if near enough to player
    	
    	//System.out.println("NPC:"+realToInt(rPosY)+" PLAYER:"+realToInt(com.mygdx.mongojocs.sanfermines2006.GameCanvas.gc.runner.rPosY));
		
		if(realToInt(rPosY) < realToInt(GameCanvas.gc.runner.rPosY) - 140) {
			
			return;
		}
    	
    	switch(entityState) {
    	
    		case NPC_STATE_RUN:
    			
    			
    			// AI
    			
    			if(mapCollision(iBboxBotX + iBboxSizeX / 2, iBboxBotY - TILE_WIDTH / 2) &&
    				!mapCollision(iBboxBotX + iBboxSizeX / 2 - TILE_WIDTH, iBboxBotY - TILE_WIDTH / 2)) {
    			
    				AIKeyX = -1;
    				
    			}
    				
    			if(mapCollision(iBboxBotX + iBboxSizeX / 2, iBboxBotY - TILE_WIDTH / 2) &&
        			!mapCollision(iBboxBotX + iBboxSizeX / 2 + TILE_WIDTH, iBboxBotY - TILE_WIDTH / 2)) {
        			
    				AIKeyX = +1;
        				
        		}
    			
    			if(mapTileType(iBboxBotX + iBboxSizeX / 2, iBboxBotY - TILE_WIDTH / 2) == TILE_LOW_OBSTACLE) {
    				
    				AIKeyY = -1;
    			}
    			
    			    			
    			if(ticks % 3 != 0) {
    				
	    			if (sprite.sequence != (entitySubclass == 0 ? GameCanvas.BOOT1_CORRER_B0_S13 : GameCanvas.BOOT2_CORRER_B0_S17)) {
	    	    		
	    	    		if(sprite.animFinished) {
	    	    			
	    	    			sprite.setSequence((entitySubclass == 0 ? GameCanvas.BOOT1_CORRER_B0_S13 : GameCanvas.BOOT2_CORRER_B0_S17));
	    	    			
	    	    		} else {
	    	    		
	    	    			sprite.updateFrame();
	    	    		}
	    	    		
	    	    	} else {
	    	    		    	    		        				
	        			sprite.updateFrameLoop();
	        			
	    	    	}
    			}
    			    		    	    	    			    			    			    			    
    			if(AIKeyX > 0) {
    	    		
    				rVelocityX = 512;
    				
    	    	} else if(AIKeyX < 0) {
    	    		
    	    		rVelocityX = -512;
    	    		
    	    	} else {
    	    	
    	    		rVelocityX = 0;
    	    	}
    			
    			if(AIKeyY < 0) {
    				
    				rVelocityZ = 640 + 64 * miscData[NPC_DATA_JUMP_VALUE];
    				
    				setState(NPC_STATE_JUMP);
    				
    				sprite.setSequence(entitySubclass == 0 ? GameCanvas.BOOT1_SALTAR_B0_S14 : GameCanvas.BOOT2_SALTAR_B0_S18 );
    				
    			}
    	    	    			    			
    			npcVelocityUpdate();
    			
    			move();
    			    			    		    			    	        	    	    	    	    	    
    			break;
    			    	
    		case NPC_STATE_JUMP:
    			
    			sprite.updateFrame();
    			
    			rVelocityX = 0;
    			rVelocityZ -= 128;
    			    			    			    			    		
    			move();
    			
    			if(iBboxBotZ <= 3) {
    				
    				rPosZ = 0;
    				
    				updateBoundingBox();
    				
    				setState(NPC_STATE_RUN);
    				
    				sprite.setSequence((entitySubclass == 0 ? GameCanvas.BOOT1_CORRER_B0_S13 : GameCanvas.BOOT2_CORRER_B0_S17));
    				    				
    			}
    			      		
    			break;
    			
    	       	    			
    		case NPC_STATE_HORNED:
    			
    			if(ticks % 2 == 0) {
    				
    				if(sprite.sequence != (entitySubclass == 0 ? GameCanvas.BOOT1_PISADO_B0_S16 : GameCanvas.BOOT2_PISADO_B0_S20)  && sprite.animFinished) {
    				
    					sprite.setSequence(entitySubclass == 0 ? GameCanvas.BOOT1_PISADO_B0_S16 : GameCanvas.BOOT2_PISADO_B0_S20);
    					
    				} else {
    				
    					sprite.updateFrame();
    				}
    			}
    			 			
    			physicsUpdate();
    			
    			break;    			    		
    	}
    	    	
    	
    	    	
    	miscData[NPC_DATA_STAMINA] -= 5 - miscData[NPC_DATA_RESISTANCE_VALUE];    		
    	
    	
    	if(miscData[NPC_DATA_STAMINA] < 0) {
    	
    		miscData[NPC_DATA_STAMINA] = 0;
    	}
    	
    	if(miscData[NPC_DATA_STAMINA] > NPC_MAX_STAMINA) {
        	
    		miscData[NPC_DATA_STAMINA] = NPC_MAX_STAMINA;
    	}
    	
    	// Obstacles
    	
    	int mapX = iBboxBotX + iBboxSizeX/2;
    	int mapY = iBboxBotY + iBboxSizeY/2;
    	
    	//System.out.println("Pos:"+iBboxBotX+","+iBboxBotY+". Map:"+mapX+","+mapY+".");
    		    	    	
    	switch(mapTileType(mapX, mapY)) {
    	
    		case TILE_BARRIER:    
    			    			    			
    			rVelocityX -= ((6 - miscData[NPC_DATA_STRENGTH_VALUE]) * rVelocityX) / 20;
    			rVelocityY -= ((6 - miscData[NPC_DATA_STRENGTH_VALUE]) * rVelocityY) / 20;
    				    			    			    			
    			if(iBboxBotZ < TILE_WIDTH*2) {
    				setMapTile(mapX, mapY, TILE_EMPTY);
    			}
    		break;
    		    		    		
    		case TILE_LOW_OBSTACLE:    
    			
    			if(entityState != NPC_STATE_JUMP && entityState != NPC_STATE_HORNED) {
    				
    				if(rVelocityX < -512) {
    					rVelocityX = -512;
    				}
    				if(rVelocityX > 512) {
    					rVelocityX = 512;
    				}
    				if(rVelocityY < -512) {
    					rVelocityY = -512;
    				}
    				
    				/*
    				if(sprite.sequence == com.mygdx.mongojocs.sanfermines2006.GameCanvas.BOOT1_CORRER_B0_S13) {
    					
    					sprite.setSequence(com.mygdx.mongojocs.sanfermines2006.GameCanvas.BOOT1_CORRER_B0_S13 );
    				}
    				*/
    				
    				//#ifndef NOPARTICLES
    				
    				// Water splash

        			if(entityState != NPC_STATE_JUMP && entityState != NPC_STATE_HORNED) {
    				
        				if(mapTile(mapX, mapY) == 23) {
    					
        					GameCanvas.createWaterSplash(iBboxBotX + iBboxSizeX / 2, iBboxBotY + iBboxSizeY / 2);
        				}
        			}				
    				//#endif
    			}    			
    		break;
    		    		    		    			    	
    	}
    	    	
    	
    	// Touch the bull
    	
    	link = GameCanvas.gc.bull[ticks % GameCanvas.NUM_BULLS];
		
		if(bboxIntersection(this, link) && entityState != NPC_STATE_HORNED) {
			
			setState(NPC_STATE_HORNED);
			
			if(rVelocityY > -512) {
				
    			sprite.setSequence(entitySubclass == 0 ? GameCanvas.BOOT1_PISADO_B0_S16 : GameCanvas.BOOT2_PISADO_B0_S20);
    									
			} else {
								 			    			
    			rVelocityY = -1024;
    			rVelocityZ = 2048;
    			sprite.setSequence(entitySubclass == 0 ? GameCanvas.BOOT1_CORNEADO_B0_S15 : GameCanvas.BOOT2_CORNEADO_B0_S19);
    		   
			}
			    				
		}   
		
		// Expire
		
		if(realToInt(rPosY) > realToInt(GameCanvas.gc.runner.rPosY) + 160) {
			
			expired = true;
		}
    	    	
    }
    
    private void npcVelocityUpdate() {
    	
    	int maximum = -256 - (25 * miscData[NPC_DATA_SPEED_VALUE]) - (miscData[NPC_DATA_STAMINA] / 3);
    	   
    	    	    	
       	if(mapCollidedFlag) {
       		
       		//rVelocityY = 0;
       		    	
       	} else {
       		
       		if(rVelocityY > maximum) {
    		
       			rVelocityY -= 15;       			       			    		
       		}    	       		
       		
       		if(rVelocityY < maximum) {
   				
   				rVelocityY = maximum;
   			}
       	}
    	    	
    }
    
    //#endif
    
    /*
     * Bull Class - Implementation
     * ============================================================================
     */
    
    public static final int BULL_RUN_SPEED = -1100;
    public static final int BULL_TURN_SPEED = 700;
    public static final int BULL_GLOBE_DURATION = 15;
    
    /*
     * Bull states
     * -------------
     */
    
    public static final int BULL_STATE_RUN = 1;
    public static final int BULL_STATE_TURN_LEFT = 2;
    public static final int BULL_STATE_TURN_RIGHT = 3;
    
    
    /* 
     * Data
     * -------------
     */
    
    public static final int BULL_DATA_MAXSPEED = 0;
    public static final int BULL_DATA_SHOWGLOBE = 1;
    
    
    
    public void bullUpdate() {
    	
    	ticks++;
    	
    	//#ifdef FAST150
    	//#endif

    	
    	if(link.iBboxBotY > iBboxBotY + TILE_WIDTH * 50) {
    	
    		miscData[BULL_DATA_MAXSPEED] = BULL_RUN_SPEED * 2;
    		
    	} else if (miscData[BULL_DATA_MAXSPEED] > BULL_RUN_SPEED) {
    		
    		miscData[BULL_DATA_MAXSPEED] = BULL_RUN_SPEED;
    	}
    	
    	    	
    	if(ticks % 1 == 0) {
		
    		if(sprite.sequence == GameCanvas.CORRER_B0_S0) {
    		
    			sprite.updateFrameLoop();
    			
    		} else {
    			
    			sprite.updateFrame();
    			
    			if(sprite.animFinished) {
    			
    				sprite.setSequence(GameCanvas.CORRER_B0_S0);
    			}
    		}			
		}
    	
    	switch(entityState) {
    	
    		case BULL_STATE_RUN:
    			
    			rVelocityX = 0;
    			
    			if(rVelocityY > miscData[BULL_DATA_MAXSPEED]) {
    				
    				rVelocityY -= 64;
    			}
    			
    			move();
    			    			    			
    			// Follow the player
    			
    			if(link.iBboxBotY > iBboxBotY - 45 && link.iBboxBotY < iBboxBotY) {
    			    				
    				if(link.iBboxBotX > iBboxBotX + iBboxSizeX) {
    						
    					setState(BULL_STATE_TURN_RIGHT);    					
    				}
    				
    				if(link.iBboxBotX + link.iBboxSizeX < iBboxBotX) {
						
						setState(BULL_STATE_TURN_LEFT);    					
    				}
    				
    			} else {
    			
    			// IA
    				
    				int frontx = iBboxBotX + iBboxSizeX / 2;
    				int leftx = frontx - TILE_WIDTH;
    				int rightx = frontx + TILE_WIDTH;
    				int y = iBboxBotY - TILE_WIDTH * 2;
    				    				    				
    				boolean front = mapCollision(frontx, y) || mapTile(frontx, y) == 15;
    				boolean left = mapCollision(leftx, y) || mapTile(leftx, y) == 15;
    				boolean right = mapCollision(rightx, y) || mapTile(rightx, y) == 15;
    						
    			
    				if(front && !left) {
    			
    					setState(BULL_STATE_TURN_LEFT);
    				
    				}
    				
    				if(front && !right) {
        			
        				setState(BULL_STATE_TURN_RIGHT);
        				
        			}
    			}
    			
    			
    			// Avoid walls
    			
    			if(mapCollidedFlag) {
    				
    				if(GameCanvas.gc.rnd(2) == 0) {
    					
    					setState(BULL_STATE_TURN_LEFT);
    					
    				} else {
    					
    					setState(BULL_STATE_TURN_RIGHT);
    				}
    			}
    			    			    		
    			break;
    			
    		case BULL_STATE_TURN_LEFT:
    			
    			rVelocityX = -BULL_TURN_SPEED;    			
    			move();
    			
    			if(ticks >= 10) {
    				
    				setState(BULL_STATE_RUN);
    			}
    			
    			break;
    			
    		case BULL_STATE_TURN_RIGHT:
    			
    			rVelocityX = BULL_TURN_SPEED;    			
    			move();
    			
    			if(ticks >= 10) {
    				
    				setState(BULL_STATE_RUN);
    			}
    			
    			break;    			
    	}
    	
    	//#ifndef REDUCEDANIMATIONS
    	
    	if(iBboxBotY < link.iBboxBotY + link.iBboxSizeY + 30
    			&& iBboxBotY > link.iBboxBotY && sprite.sequence == GameCanvas.CORRER_B0_S0
    			&& link.entityState != PLAYER_STATE_HORNED) {
    		    		    	    	    	
    		link.playerLookBackwards();
    		
    	}
    	
    	//#endif
    	
    	
    	// Cornear
    	
    	if(iBboxBotY < link.iBboxBotY + link.iBboxSizeY + 10
    			&& iBboxBotY > link.iBboxBotY && sprite.sequence == GameCanvas.CORRER_B0_S0
    			&& link.entityState != PLAYER_STATE_HORNED) {
    		    		    	
    		//#ifndef REDUCEDANIMATIONS
    		sprite.setSequence((GameCanvas.gc.rnd(2) == 0 ? GameCanvas.CORNADA2_B0_S2 : GameCanvas.ENVESTIR_B0_S1));
    		//#endif
    		
    		//#ifndef REDUCEDANIMATIONS
    		
    		link.playerLookBackwards();
    		
    		//#endif
    		
    	}
    	
    	// Touch the player
		
    	if(!GameCanvas.gc.protInmune && GameCanvas.gc.playStatus != GameCanvas.PLAY_STATUS_EXITED) {
    		
			if(bboxIntersection(this, link) && link.entityState != PLAYER_STATE_HORNED) {
				
				//#ifndef NOPARTICLES
				
				if(link.entityState == PLAYER_STATE_RIDING) {
				
					GameCanvas.gc.createExplosion(link.iBboxBotX + link.iBboxSizeX/2, link.iBboxBotY + link.iBboxSizeY/2);
		    		
				}
				
				//#endif
				
				link.setState(PLAYER_STATE_HORNED);
				
				if(link.rVelocityY > -512) {
					
	    			link.sprite.setSequence(GameCanvas.PISADO_B0_S3);
	    									
				} else {
									 			    			
	    			link.rVelocityY = -1024;
	    			link.rVelocityZ = 2048;
	    			link.sprite.setSequence(GameCanvas.CORNEADO_B0_S2);
	    			    			    		   
				}
				    		
				GameCanvas.gc.soundPlay(GameCanvas.MUSIC_PLAYER_OVER,1);
				
				GameCanvas.gc.vibraInit(300);
			}
    	}
		    	
    	// Destroy Obstacles
    	    		    	    	
    	if(mapTileType(iBboxBotX, iBboxBotY) == TILE_BARRIER) {
    		
    		setMapTile(iBboxBotX, iBboxBotY, TILE_EMPTY);
    	}
    	
    	if(mapTileType(iBboxBotX + iBboxSizeX, iBboxBotY) == TILE_BARRIER) {
    		
    		setMapTile(iBboxBotX + iBboxSizeX, iBboxBotY, TILE_EMPTY);
    	}
    	
    	if(miscData[BULL_DATA_SHOWGLOBE] > 0) {
    		
    		miscData[BULL_DATA_SHOWGLOBE]--;
    	}
    	    	    	    	       	    	    	
    }
    
    /*
     * Particle Class - Implementation
     * ============================================================================
     */
    
    //#ifndef NOPARTICLES
    
    public static final int PARTICLE_SUBCLASS_WOOD = 0;
    public static final int PARTICLE_SUBCLASS_ITEMFADE = 1;
    public static final int PARTICLE_SUBCLASS_EXPLOSION = 2;
    public static final int PARTICLE_SUBCLASS_CHUPINAZO = 3;
    public static final int PARTICLE_SUBCLASS_TURBO_ROCKET = 4;
    public static final int PARTICLE_SUBCLASS_SMOKE = 5;
    public static final int PARTICLE_SUBCLASS_SMOKE2 = 6;
    public static final int PARTICLE_SUBCLASS_WATERDROP = 7;
    public static final int PARTICLE_SUBCLASS_ROSEPLUS1 = 8;
    public static final int PARTICLE_SUBCLASS_ROSEPLUS5 = 9;
    
    private void particleUpdate() {
    	
    	ticks++;
    	
    	//#ifdef FAST150
    	//#endif

    	
    	pointPhysicsUpdate();
    	    	
    	if(ticks > lifeTime) {
    		
    		expired = true;
    	}
    	
    	if(entitySubclass == PARTICLE_SUBCLASS_CHUPINAZO || entitySubclass == PARTICLE_SUBCLASS_TURBO_ROCKET && ticks%2 == 0) {
    	
    		GameCanvas.gc.createSmoke(realToInt(rPosX),realToInt(rPosY),realToInt(rPosZ),PARTICLE_SUBCLASS_SMOKE2);
    	}
    	
    	//System.out.println("Particle update");
    	
    }
    
    //#endif
    
    
    /*
     * Pool Class - Implementation
     * ============================================================================
     */
           
    private void poolUpdate() {
    	
    	ticks++;
    	
    	//#ifdef FAST150
    	//#endif
    	
    	rPosX -= intToReal(1);
    	
    	//#ifdef FAST150
    	//#else
    	
    	iBboxSizeX += 2;    	
    	iBboxSizeY += 2;
    	
    	//#endif
    	
    	updateBoundingBox();
    	    	    	
    	if(ticks > lifeTime) {
    		
    		expired = true;
    	}
    	
    	// Bulls slide with beer pool
    	
    	for (int i = 0; i < GameCanvas.gc.bull.length; i++) {
    		    		    		
    		if(bboxIntersection(this,GameCanvas.gc.bull[i])) {
    		
    			if(GameCanvas.gc.bull[i].rVelocityY < -512) {
    				
    				GameCanvas.gc.bull[i].rVelocityY = -512;
    				
    				GameCanvas.gc.bull[i].miscData[BULL_DATA_SHOWGLOBE] = BULL_GLOBE_DURATION;
    			}    			
    		}
    	}
    	
    	//System.out.println("Particle update");
    	
    }
        
    /*
     *  Utilities
     *  ===========================================================================
     */
    
    /*
     * Integer / Real utilities
     * ------------------------
     */
    
    public int realToInt(int r) {
    	
    	return r >> 8;
    }
    
    public int intToReal(int i) {
    	
    	return i << 8;
    }
    
    /*
     * Bounding box utilities
     * ----------------------
     */
    
    public static boolean bboxIntersection(Entity a, Entity b) {
    	
	    
        return (a.iBboxBotX < b.iBboxBotX + b.iBboxSizeX &&
                a.iBboxBotX + a.iBboxSizeX > b.iBboxBotX &&
                a.iBboxBotY < b.iBboxBotY + b.iBboxSizeY &&
                a.iBboxBotY + a.iBboxSizeY > b.iBboxBotY &&
                a.iBboxBotZ < b.iBboxBotZ + b.iBboxSizeZ &&
                a.iBboxBotZ + a.iBboxSizeZ > b.iBboxBotZ
                );    
    }
    
    // Variables particulares de Sanfermines 2006
    
    
    public static final int TILE_WIDTH = 16;
    public static final int MAP_WIDTH = 6;
    public static final int STREET_WIDTH = MAP_WIDTH * TILE_WIDTH;
    
    public static final byte TILE_EMPTY = 0;
    public static final byte TILE_WALL = 1;
    public static final byte TILE_BARRIER = 2;
    public static final byte TILE_HIGH_OBSTACLE = 3;
    public static final byte TILE_LOW_OBSTACLE = 4;
    public static final byte TILE_STAMINA_UP = 5;
    public static final byte TILE_TURBO = 6;
    public static final byte TILE_SPRING = 7;
    public static final byte TILE_MOTORBIKE = 8;
    public static final byte TILE_L_DIAG_WALL = 9;
    public static final byte TILE_R_DIAG_WALL = 10;
    public static final byte TILE_ROSE = 11;
    public static final byte TILE_5_ROSES = 12;
    public static final byte TILE_L_DIAG_WALL_INV = 13;
    public static final byte TILE_R_DIAG_WALL_INV = 14;
    public static final byte TILE_LETTER = 15;
    
    
    public static byte levelMap[];
    
    public static byte originalMap[];
    
    public static int mapLength;
    
    public static void createMap(int length) {
    	    
    	levelMap = new byte[length*MAP_WIDTH];
    	
    	for(int i = 0; i < length; i++) {
    		
    		for(int j = 0; j < MAP_WIDTH; j++) {
    			
    			if(GameCanvas.gc.rnd(10) == 0) {
    				
    				levelMap[MAP_WIDTH*i + j] = (byte)(GameCanvas.gc.rnd(6) + 1);
    				
    			} else {
    				
    				levelMap[MAP_WIDTH*i + j] = 0;
    			}
    			
    		}
    	}    	
    }
    
    public static void loadMap(String fileName) {
    	
    
    	levelMap = GameCanvas.gc.loadFile(fileName);
		int faseWidth = levelMap[0] & 0xff;
		int faseHeight = levelMap[1] & 0xff;
		
		mapLength = (levelMap.length - 2) / 6;
		
		System.arraycopy(levelMap, 2, levelMap, 0, faseWidth*mapLength);
		
			
		GameCanvas.gc.totalRoses = 0;
		
		for(int x = 0; x < 6; x++) {
			
			for(int y = 0; y < mapLength; y++) {
			
				if(mapTileType(TILE_WIDTH * x,TILE_WIDTH * y) == TILE_ROSE) {
			
					GameCanvas.gc.totalRoses ++;
				}
			
				if(mapTileType(TILE_WIDTH * x, TILE_WIDTH * y) == TILE_5_ROSES) {
				
					GameCanvas.gc.totalRoses += 5;
				}
							
			}
		}
				    	
    }
    
    public static final int SURVIVAL_PIECES = 20;
    public static final int SURVIVAL_PIECES_LENGTH = 10;
        
    public static void backupMap() {
    
    	originalMap = new byte[levelMap.length];
    	
    	System.arraycopy(levelMap, 0, originalMap, 0, levelMap.length);
    }
    
    public static void createRandomMap() {
    	
    
    	// First piece : Empty
    	
    	for (int x = 0; x < MAP_WIDTH; x++) {
    		
			for (int y = 0; y < SURVIVAL_PIECES_LENGTH; y++) {
												    				    		
				levelMap[MAP_WIDTH * (SURVIVAL_PIECES - 1) * SURVIVAL_PIECES_LENGTH + MAP_WIDTH * y + x] = (byte)0;
				
			}    			
		}
    	
    	randomizeMap(); 	
    }
    
    public static void resetRandomMap() {
    	
    	
    	// From the previous existing map, copy the ending into the begining (loop effect)
    	
    	for (int x = 0; x < MAP_WIDTH; x++) {
    		
			for (int y = 0; y < SURVIVAL_PIECES_LENGTH; y++) {
												    				    		
				levelMap[MAP_WIDTH * (SURVIVAL_PIECES - 1) * SURVIVAL_PIECES_LENGTH + MAP_WIDTH * y + x] = levelMap[0 + MAP_WIDTH * y + x];
				
			}    			
		}
    	
    	randomizeMap();	
    }
    
    private static void randomizeMap() {
    	
    	for(int i = 0; i < SURVIVAL_PIECES - 1; i++) {
        	
    		int piece = GameCanvas.gc.rnd(SURVIVAL_PIECES);
    		
    		    		    		    		
    		for (int x = 0; x < MAP_WIDTH; x++) {
    		
    			for (int y = 0; y < SURVIVAL_PIECES_LENGTH; y++) {
    				
    				//System.out.println(""+(MAP_WIDTH * i * piecesLength + MAP_WIDTH * y + x)+" <- "+(MAP_WIDTH * piece * piecesLength + MAP_WIDTH * y + x));
    				    				    			
    				levelMap[MAP_WIDTH * i * SURVIVAL_PIECES_LENGTH + MAP_WIDTH * y + x] = originalMap[MAP_WIDTH * piece * SURVIVAL_PIECES_LENGTH + MAP_WIDTH * y + x];
    				
    			}    			
    		}
    	}    	
    }
    
    public static byte mapTile(int x, int y) {
    
    	int i = (y/TILE_WIDTH) * MAP_WIDTH + (x + (MAP_WIDTH * TILE_WIDTH / 2)) / TILE_WIDTH;
    	
    	//System.out.println("Asking for:("+(i%6)+","+(i/6)+")");
    	
    	if(i < 0 || i >= levelMap.length) {
    		
    		return 0;
    		
    	} else {
    		
    		return levelMap[i];
    	}
    	
    	
    }
    
    public static byte mapTileType(int x, int y) {
    	
    	switch (mapTile(x,y)) {
    	
    		default: return TILE_EMPTY;
    		case 1 : return TILE_L_DIAG_WALL_INV;
    		case 2 : return TILE_R_DIAG_WALL_INV;
    		case 3 :
    		case 17:
    		case 18:
    		case 19:
    		case 20:
    		case 21:
    		case 22:
    		case 4 : return TILE_WALL;
    		case 5 : return TILE_R_DIAG_WALL;
    		case 6 : return TILE_L_DIAG_WALL;
    		case 16:
    		case 7 : return TILE_BARRIER;
    		case 8 : return TILE_ROSE;
    		case 9 : return TILE_5_ROSES;
    		case 10: return TILE_STAMINA_UP;
    		case 11: return TILE_TURBO;
    		case 12: return TILE_SPRING;
    		case 13: return TILE_MOTORBIKE;
    		case 23:
    		case 24:
    		case 25:
    		case 15: return TILE_LOW_OBSTACLE;
    		case 26: return TILE_LETTER;
    	}
    	
    }
    
    public static void setMapTile(int x, int y, byte value) {
        
    	int i = (y/TILE_WIDTH) * MAP_WIDTH + (x + (MAP_WIDTH * TILE_WIDTH / 2)) / TILE_WIDTH;
    	
    	//System.out.println("Asking for:("+(i%6)+","+(i/6)+")");
    	
    	if(i < 0 || i >= levelMap.length) {
    		
    		    	
    	} else {
    		
    		//#ifndef NOPARTICLES
    		
    		switch(mapTileType(x,y)) {
    		
    		case TILE_BARRIER:    			
    			GameCanvas.createWoodParticles(x,y);
    			break;
    			
    		case TILE_ROSE:    		
    			GameCanvas.createItemfadeEffect(x,y,PARTICLE_SUBCLASS_ROSEPLUS1);
    			break;
    			
    		case TILE_5_ROSES:    		
    			GameCanvas.createItemfadeEffect(x,y,PARTICLE_SUBCLASS_ROSEPLUS5);
    			break;
    			
    		default:    		
    			GameCanvas.createItemfadeEffect(x,y,PARTICLE_SUBCLASS_ITEMFADE);
    			break;
    		}
    		
    		//#endif
    		
    		levelMap[i] = value;
    	}
    	
    	
    }
    
    /*public static boolean mapCollision(byte b) {
    	
    	switch(b) {
    	
    		case TILE_WALL : return true;
    		
    		default : return false;
    	}
    }*/
    
    public static boolean mapCollision(int x, int y) {
    	
    	    	
    	byte tile = mapTileType(x, y);
    	
    	x += STREET_WIDTH / 2;
    	
    	switch(tile) {
    	
    		case TILE_WALL : return true;
    		
    		case TILE_L_DIAG_WALL : return x % TILE_WIDTH < TILE_WIDTH - (y % TILE_WIDTH);
    		
    		case TILE_R_DIAG_WALL : return x % TILE_WIDTH > y % TILE_WIDTH;
    		
    		case TILE_L_DIAG_WALL_INV : return x % TILE_WIDTH > TILE_WIDTH - (y % TILE_WIDTH);
    		
    		case TILE_R_DIAG_WALL_INV : return x % TILE_WIDTH < y % TILE_WIDTH;
    		
    		default : return false;
    	}
    	    
    }
    
               
}