package com.mygdx.mongojocs.mrboom;


import com.mygdx.mongojocs.iapplicationemu.Image;

class Enemy extends SpriteEntity {

	final static int ENEMY_REBOOT_V = 0;
	final static int ENEMY_REBOOT_H = 1;
	final static int ENEMY_FOURDIR  = 2;
	final static int ENEMY_SMART    = 3;
	
	final static int MOVE_INC = 2;
	
	int type;
	int	dir, lastDir;
	
	int moveDir;

	int	posTileX, posTileY;
	
	int destPosX, destPosY;

	Enemy(int t, int x, int y) {
		type = t;
		px = x;
		py = y;
		
		destPosX = x;
		destPosY = y;
		
		if (t == ENEMY_REBOOT_H) {
			moveDir = 2;
			dir = moveDir;
		}

		Game theGame = Game.theGame;

		posTileX = (px + theGame.getTileWidth()/2) / theGame.getTileWidth();
		posTileY = (py + theGame.getTileHeight()/2) / theGame.getTileHeight();
		
		boolean isCol=false;
		switch (moveDir) {
			case 0: isCol = theGame.isBlocked(posTileX, posTileY-1); break; 
			case 1: isCol = theGame.isBlocked(posTileX, posTileY+1); break; 
			case 2: isCol = theGame.isBlocked(posTileX-1, posTileY); break; 
			case 3: isCol = theGame.isBlocked(posTileX+1, posTileY); break;	
		}
		if (isCol) moveDir = -1;						
		
		recalculateDestPos();
	}
	
	// actions

	void tick() {
		
		Game theGame = Game.theGame;

		switch (moveDir)
		{
			case 0:
				py -= MOVE_INC;
				break;
			case 1:		
				py += MOVE_INC;
				break;
			case 2:
				px -= MOVE_INC;
				break;
			case 3:
				px += MOVE_INC;
				break;
		}
		
		if (px < 0)		px = 0;
		if (py < 0)		py = 0;
		
		posTileX = (px + theGame.getTileWidth()/2) / theGame.getTileWidth();
		posTileY = (py + theGame.getTileHeight()/2) / theGame.getTileHeight();
		
		if (px == destPosX && py == destPosY) {
			
			lastDir = moveDir;
			
			// check if it is possible to keep going
			boolean isColliding=false;
			switch (moveDir) {
				case 0: isColliding = theGame.isBlocked(posTileX, posTileY-1); break; 
				case 1: isColliding = theGame.isBlocked(posTileX, posTileY+1); break; 
				case 2: isColliding = theGame.isBlocked(posTileX-1, posTileY); break; 
				case 3: isColliding = theGame.isBlocked(posTileX+1, posTileY); break;	
			}
			
			if (type == ENEMY_FOURDIR) {
				// if possible change dir randomly
				
				// get all possible choices including the last one, ignoring turning around
				boolean[] canMoveDir = new boolean[4];
				
				for (int j = 0; j < 4; ++j) {
					boolean isCol = false;
					switch (j) {
						case 0: isCol = theGame.isBlocked(posTileX, posTileY-1); break; 
						case 1: isCol = theGame.isBlocked(posTileX, posTileY+1); break; 
						case 2: isCol = theGame.isBlocked(posTileX-1, posTileY); break; 
						case 3: isCol = theGame.isBlocked(posTileX+1, posTileY); break;	
					}
					canMoveDir[j] = !isCol;
				}
				
				// forbid turn around
				switch (moveDir) {
					case 0:	canMoveDir[1] = false; break;
					case 1: canMoveDir[0] = false; break;
					case 2: canMoveDir[3] = false; break;
					case 3: canMoveDir[2] = false; break;
				}

				// if possible, move
				boolean canMove = canMoveDir[0] | canMoveDir[1] | canMoveDir[2] | canMoveDir[3];

				if (canMove) {
					int f = Game.rand()/5 & 0x3;
					while (!canMoveDir[f]) {
						f ++;
						f &= 3;
					}
					moveDir = f;
				}
				
			}
			else if (type == ENEMY_SMART) {
				// if possible move towards the player
				
				// get all possible choices including the last one
				boolean[] canMoveDir = new boolean[4];
				
				for (int j = 0; j < 4; ++j) {
					boolean isCol = false;
					switch (j) {
						case 0: isCol = theGame.isBlocked(posTileX, posTileY-1); break; 
						case 1: isCol = theGame.isBlocked(posTileX, posTileY+1); break; 
						case 2: isCol = theGame.isBlocked(posTileX-1, posTileY); break; 
						case 3: isCol = theGame.isBlocked(posTileX+1, posTileY); break;	
					}
					canMoveDir[j] = !isCol;
				}
				
				// forbid turn around
				switch (moveDir) {
					case 0:	canMoveDir[1] = false; break;
					case 1: canMoveDir[0] = false; break;
					case 2: canMoveDir[3] = false; break;
					case 3: canMoveDir[2] = false; break;
				}

				// if possible, move
				boolean canMove = canMoveDir[0] | canMoveDir[1] | canMoveDir[2] | canMoveDir[3];

				if (canMove) {
					// calculate max dist
					int distX = (posTileX - theGame.thePlayer.posTileX); 
					int distY = (posTileY - theGame.thePlayer.posTileY);
					
					if (distX*distX > distY*distY) {
						// try in x axis first
						if (distX > 0 && canMoveDir[2])			moveDir = 2;
						else if (distX < 0 && canMoveDir[3])	moveDir = 3;
						else if (distY > 0 && canMoveDir[0])	moveDir = 0;
						else if (distY < 0 && canMoveDir[1])	moveDir = 1;
					} else {
						// try in y axis first
						if (distY > 0 && canMoveDir[0])			moveDir = 0;
						else if (distY < 0 && canMoveDir[1])	moveDir = 1;
						else if (distX > 0 && canMoveDir[2])	moveDir = 2;
						else if (distX < 0 && canMoveDir[3])	moveDir = 3;
					}					
				}
			}
				
			if (isColliding && lastDir==moveDir) {
				// choose a new direction (reboot)
				switch (moveDir) {
					case 0:	moveDir = 1; break;
					case 1: moveDir = 0; break;
					case 2: moveDir = 3; break;
					case 3: moveDir = 2; break;
				}
				
				if (moveDir<0) {
					if (type == ENEMY_REBOOT_H) {
						//if (theGame.isBlocked(posTileX, posTile))
						moveDir = 2;					
					}
					else if (type == ENEMY_REBOOT_V) {
						moveDir = 0;
					}
				}

				boolean isCol=false;
				switch (moveDir) {
					case 0: isCol = theGame.isBlocked(posTileX, posTileY-1); break; 
					case 1: isCol = theGame.isBlocked(posTileX, posTileY+1); break; 
					case 2: isCol = theGame.isBlocked(posTileX-1, posTileY); break; 
					case 3: isCol = theGame.isBlocked(posTileX+1, posTileY); break;	
				}
				if (isCol) moveDir = -1;						
			} 
			if (!isColliding && (type==ENEMY_REBOOT_H || type==ENEMY_REBOOT_V)) {
				if (moveDir < 0) {
					if (type == ENEMY_REBOOT_H) {
						//if (theGame.isBlocked(posTileX, posTile))
						moveDir = 3;					
					}
					else if (type == ENEMY_REBOOT_V) {
						moveDir = 1;
					}
					boolean isCol=false;
					switch (moveDir) {
						case 0: isCol = theGame.isBlocked(posTileX, posTileY-1); break; 
						case 1: isCol = theGame.isBlocked(posTileX, posTileY+1); break; 
						case 2: isCol = theGame.isBlocked(posTileX-1, posTileY); break; 
						case 3: isCol = theGame.isBlocked(posTileX+1, posTileY); break;	
					}
					if (isCol) moveDir = -1;
				}
				if (moveDir < 0) {
					if (type == ENEMY_REBOOT_H) {
						//if (theGame.isBlocked(posTileX, posTile))
						moveDir = 2;					
					}
					else if (type == ENEMY_REBOOT_V) {
						moveDir = 0;
					}
					boolean isCol=false;
					switch (moveDir) {
						case 0: isCol = theGame.isBlocked(posTileX, posTileY-1); break; 
						case 1: isCol = theGame.isBlocked(posTileX, posTileY+1); break; 
						case 2: isCol = theGame.isBlocked(posTileX-1, posTileY); break; 
						case 3: isCol = theGame.isBlocked(posTileX+1, posTileY); break;	
					}
					if (isCol) moveDir = -1;
				}
			}
			if (moveDir >= 0)
				dir = moveDir;
			
			// recalculate destPosX & destPosY				
			recalculateDestPos();
		}
		
		// check if kills player
		if (theGame.thePlayer.posTileX == posTileX && theGame.thePlayer.posTileY == posTileY) {
				// Player dies
				theGame.thePlayer.onKilled();
		}
	}
	
	void recalculateDestPos() {
		Game theGame = Game.theGame;
		
		switch (moveDir) {
			case 0:	destPosY -= theGame.getTileHeight(); break;
			case 1:	destPosY += theGame.getTileHeight(); break;
			case 2:	destPosX -= theGame.getTileWidth(); break;
			case 3:	destPosX += theGame.getTileWidth(); break;
		}
	}
	
	void draw(GameCanvas gc) {
		
		Image[] tabImgEnemy = gc.tabImgRebootEnemy;
				
		switch (type) {
			case ENEMY_REBOOT_H:
			case ENEMY_REBOOT_V:
				tabImgEnemy = gc.tabImgRebootEnemy;
				break;
			case ENEMY_FOURDIR:
				tabImgEnemy = gc.tabImgFourDirEnemy;
				break;
			case ENEMY_SMART:
				tabImgEnemy = gc.tabImgSmartEnemy;
				break;
		}
		
		gc.imageDrawAndInvalidate(tabImgEnemy[(dir<<1)+(gc.frameCounter&1)], px, py);
	}
	
	// event

	void onKilled() {
		
		// destroy enemy
		for (int i = 0; i < Game.theGame.theEnemies.length; ++i) {
			if (Game.theGame.theEnemies[i] == this) {
				Game.theGame.theEnemies[i] = null;
			}
		}
	}
}