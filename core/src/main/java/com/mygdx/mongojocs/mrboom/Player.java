package com.mygdx.mongojocs.mrboom;


class Player extends SpriteEntity {
	
	// atributes

	int numLifes=5;
	
	int dir, moveDir=-1;
	int numBomb=1;
	int	rangeBomb=1;
	int destPosX, destPosY;
	
	int	posTileX, posTileY;
	int destPosTileX, destPosTileY;
	
	//int lifes;
	int score;
	
	int	frames;
	
	boolean isKilled=false;
	int framesSinceKilled;
	
	Player() {
		reset();
	}
	
	void reset() {
		numLifes = 5;
		dir = 0; moveDir=-1;
		numBomb = 1;
		rangeBomb = 1;
		destPosX = 0; destPosY = 0;
		posTileX = 0; posTileY = 0;
		
		score = 0;
		
		frames = 0;
		
		isKilled=false;
		framesSinceKilled = 0;
	}
	
	void setPlayerPos(int x, int y) {
		px = x;
		py = y;
	}
	
	// actions

	void draw(GameCanvas gc) {
		if (!isKilled) {
			gc.imageDrawAndInvalidate(gc.tabImgPlayer[(dir<<1)+(moveDir!=-1?gc.frameCounter&1:0)], px, py);
		} else {
			if (framesSinceKilled < 6) {
				gc.imageDrawAndInvalidate(gc.tabImgPlayer[8], px, py);
			}
			else if (framesSinceKilled < 20) {
				gc.imageDrawAndInvalidate(gc.tabImgPlayer[9], px, py);
			}
		}
	}
	
	void tick(Game theGame) {
		
		if (isKilled) {
			framesSinceKilled++;
			if (framesSinceKilled > 24) {
				framesSinceKilled = 0;
				isKilled = false;
				
				reloadLevel();				
			}
			return;
		}
		
		switch (moveDir) {
			case 0:
				py -= 2;
				break;
			case 1:
				py += 2;
				break;
			case 2:
				px -= 2;
				break;
			case 3:
				px += 2;
				break;
		}
		
		if (px < 0)		px = 0;
		if (py < 0)		py = 0;
		
		posTileX = (px + theGame.getTileWidth()/2) / theGame.getTileWidth();
		posTileY = (py + theGame.getTileHeight()/2) / theGame.getTileHeight();
		
		if (px == destPosX && py == destPosY) {
			moveDir = -1;
		}
		
		if (px > (theGame.thePlayfield.numTilesX-1)*theGame.getTileWidth())	
			px = (theGame.thePlayfield.numTilesX-1)*theGame.getTileWidth();
		if (py > (theGame.thePlayfield.numTilesY-1)*theGame.getTileHeight())	
			py = (theGame.thePlayfield.numTilesY-1)*theGame.getTileHeight();

		if (moveDir < 0) {
			if (Game.theGame.keyX < 0) {
				dir     = 2;
				if (!theGame.isBlocked(posTileX-1, posTileY)) {
					moveDir = 2;
					
					destPosX = px - theGame.getTileWidth();
					destPosY = py;
				}
				if (theGame.isEndLevelBlock(posTileX-1, posTileY)) {
					nextLevel();
				}
			}
			if (Game.theGame.keyX > 0) {
				dir     = 3;
				if (!theGame.isBlocked(posTileX+1, posTileY)) {
					moveDir = 3;
		
					destPosX = px + theGame.getTileWidth();
					destPosY = py;
				}
				if (theGame.isEndLevelBlock(posTileX+1, posTileY)) {
					nextLevel();
				}
			}
			if (Game.theGame.keyY < 0) {
				dir     = 0;
				if (!theGame.isBlocked(posTileX, posTileY-1)) {
					moveDir = 0;
		
					destPosX = px;
					destPosY = py - theGame.getTileHeight();
				}
				if (theGame.isEndLevelBlock(posTileX, posTileY-1)) {
					nextLevel();
				}
			}
			if (Game.theGame.keyY > 0) {
				dir     = 1;
				if (!theGame.isBlocked(posTileX, posTileY+1)) {
					moveDir = 1;
		
					destPosX = px;
					destPosY = py + theGame.getTileHeight();
				}
				if (theGame.isEndLevelBlock(posTileX, posTileY+1)) {
					nextLevel();
				}
			}
		}

		
		// if fire and there are enough bombs, bomb!
		
		int numBombsDeployed = 0;
		for (int i = 0; i < theGame.theBombs.length; i++) {
			if (theGame.theBombs[i] != null) {
				numBombsDeployed ++;
				if (theGame.theBombs[i].posTileX == posTileX && theGame.theBombs[i].posTileY == posTileY)
					numBombsDeployed = 333;
			}
		}
		
		if (theGame.keyMisc == 5 && theGame.lastKeyMisc!=5) {
			if (numBomb > numBombsDeployed) {
				bomb();
			}
			else {
				// Do something otherwise?
			}
		}
	}
	
	void bomb() {
		
		// spawn bomb (already did checks)
		
		Game theGame = Game.theGame;
		
		for (int i = 0; i < theGame.theBombs.length; i++) {
			if (theGame.theBombs[i] == null) {
				theGame.theBombs[i] = new Bomb(posTileX*theGame.getTileWidth(), 
						posTileY*theGame.getTileHeight(), 
						rangeBomb);
				break;
			}
		}
	}
	
	
	// events
	
	void onKilled() {
		if (!isKilled) {		
			isKilled = true;
			//System.out.println("Player killed - life "+numLifes);
			
			Game.theGame.gc.VibraSET(250);
			Game.theGame.gc.SoundSET(3,1);
			
			if (--numLifes <= 0) {
				Game.theGame.gameStatus = 103;
			}
		}
	}

	void reloadLevel() {
		Game theGame = Game.theGame;
		

		// resetPlayer		
		dir = 0; moveDir=-1;
		numBomb = 1;
		rangeBomb = 1;
		destPosX = 0; destPosY = 0;
		posTileX = 0; posTileY = 0;		
		frames = 0;
		
		isKilled=false;
		framesSinceKilled = 0;
		
		moveDir = -1;
		
		// init level
		theGame.thePlayfield.init(theGame.numLevel);
			
		numBomb = 1;
		rangeBomb = 1;

		theGame.panelInit(9);
		theGame.waitInit(3*1000, 1002);
	}
	
	void nextLevel() {
		Game theGame = Game.theGame;

		moveDir = -1;
		
		// init next level
		if (theGame.numLevel<=13) {
			theGame.thePlayfield.init(++theGame.numLevel);
			
			numBomb = 1;
			rangeBomb = 1;

			theGame.panelInit(8);
			theGame.waitInit(3*1000, 1002);
		}
		else {
			// final del juego
			theGame.panelInit(10);
			theGame.waitInit(3*1000, 51);
		}
	}
}

