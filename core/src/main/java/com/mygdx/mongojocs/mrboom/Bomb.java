package com.mygdx.mongojocs.mrboom;


class Bomb extends SpriteEntity {

	int range;
	int timeOut;
	
	int	posTileX, posTileY;
	
	boolean isExploding;
	
	int maxExplodeRangeUp;
	int maxExplodeRangeDown;
	int maxExplodeRangeLeft;
	int maxExplodeRangeRight;

	Bomb(int x, int y, int r) {
		px = x;
		py = y;
		
		Game theGame = Game.theGame;

		posTileX = (px + theGame.getTileWidth()/2) / theGame.getTileWidth();
		posTileY = (py + theGame.getTileHeight()/2) / theGame.getTileHeight();

		range = r;
		timeOut = 42;		
	}
	
	void draw(GameCanvas gc) {
		Game theGame = Game.theGame;
		
		gc.imageDrawAndInvalidate(gc.tabImgBomb[0+((gc.frameCounter&0x2)>>1)], px, py);
		
		if (isExploding) {

			// draw center
			gc.imageDrawAndInvalidate(gc.tabImgBomb[2], px, py);
			
			// draw up
			for (int i = 0; i < maxExplodeRangeUp; ++i) {		
				int ex = posTileX;
				int ey = posTileY - (i+1);
				
				if (theGame.thePlayfield.isTileBlocking(ex, ey)) 	break;
				if (theGame.thePlayfield.isTileBlocking(ex, ey-1) || i == maxExplodeRangeUp-1)				
					gc.imageDrawAndInvalidate(gc.tabImgBomb[6], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());
				else						
					gc.imageDrawAndInvalidate(gc.tabImgBomb[4], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());

				// check for enemies meanwhile
				for (int j = 0; j < theGame.theEnemies.length; ++j) {
					if (theGame.theEnemies[j] != null) {
						int etx = theGame.theEnemies[j].px;
						int ety = theGame.theEnemies[j].py;
		
						etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
						ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
						
						if (ex == etx && ey == ety) {
							theGame.theEnemies[j].onKilled();
						}
					}
				}										
			}
			
			// draw down
			for (int i = 0; i < maxExplodeRangeDown; ++i) {
				int ex = posTileX;
				int ey = posTileY + (i+1);
				
				if (theGame.thePlayfield.isTileBlocking(ex, ey)) 	break;				
				if (theGame.thePlayfield.isTileBlocking(ex, ey+1) || i == maxExplodeRangeDown-1)				
					gc.imageDrawAndInvalidate(gc.tabImgBomb[8], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());
				else						
					gc.imageDrawAndInvalidate(gc.tabImgBomb[4], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());


				// check for enemies meanwhile
				for (int j = 0; j < theGame.theEnemies.length; ++j) {
					if (theGame.theEnemies[j] != null) {
						int etx = theGame.theEnemies[j].px;
						int ety = theGame.theEnemies[j].py;
		
						etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
						ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
						
						if (ex == etx && ey == ety) {
							theGame.theEnemies[j].onKilled();
						}
					}
				}										
			}
			
			// draw left
			for (int i = 0; i < maxExplodeRangeLeft; ++i) {
				int ex = posTileX - (i+1);
				int ey = posTileY;
				
				if (theGame.thePlayfield.isTileBlocking(ex, ey)) 	break;				
				if (theGame.thePlayfield.isTileBlocking(ex-1, ey) || i == maxExplodeRangeLeft-1)				
					gc.imageDrawAndInvalidate(gc.tabImgBomb[5], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());
				else						
					gc.imageDrawAndInvalidate(gc.tabImgBomb[3], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());						

				// check for enemies meanwhile
				for (int j = 0; j < theGame.theEnemies.length; ++j) {
					if (theGame.theEnemies[j] != null) {
						int etx = theGame.theEnemies[j].px;
						int ety = theGame.theEnemies[j].py;
		
						etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
						ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
						
						if (ex == etx && ey == ety) {
							theGame.theEnemies[j].onKilled();
						}
					}
				}										
			}
			
			// draw right
			for (int i = 0; i < maxExplodeRangeRight; ++i) {
				int ex = posTileX + (i+1);
				int ey = posTileY;
				
				if (theGame.thePlayfield.isTileBlocking(ex, ey)) 	break;				
				if (theGame.thePlayfield.isTileBlocking(ex+1, ey) || i == maxExplodeRangeRight-1)				
					gc.imageDrawAndInvalidate(gc.tabImgBomb[7], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());
				else						
					gc.imageDrawAndInvalidate(gc.tabImgBomb[3], ex*theGame.getTileWidth(), ey*theGame.getTileHeight());						

				// check for enemies meanwhile
				for (int j = 0; j < theGame.theEnemies.length; ++j) {
					if (theGame.theEnemies[j] != null) {
						int etx = theGame.theEnemies[j].px;
						int ety = theGame.theEnemies[j].py;
		
						etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
						ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
						
						if (ex == etx && ey == ety) {
							theGame.theEnemies[j].onKilled();
						}
					}
				}										
			}
		}

	}
	
	void tick() {
		if (--timeOut==0) {
			if (!isExploding)
				//destroy(); 
				explode();
			else
				destroy();
		}
	}
	
	void destroy() {
		for (int i = 0; i < Game.theGame.theBombs.length; ++i) {
			if (Game.theGame.theBombs[i] == this) {
				Game.theGame.theBombs[i] = null;
			}
		}						
	}
	
	// Check if kills player, enemies or items
	void explode() {
		Game theGame = Game.theGame;
		
		if (isExploding) return;
		
		theGame.gc.VibraSET(100);
		theGame.gc.SoundSET(3,1);
		
		// destroy bomb (deferred?!)
		isExploding = true;
		timeOut = 4;
		
		// center
		if (true) {
			// check if there is player (or enemy?)
			int ex = posTileX;
			int ey = posTileY;
			if (theGame.thePlayer.posTileX == ex && theGame.thePlayer.posTileY == ey) {
				// Player dies
				theGame.thePlayer.onKilled();
			}
			for (int j = 0; j < theGame.theEnemies.length; ++j) {
				if (theGame.theEnemies[j] != null) {
					int etx = theGame.theEnemies[j].px;
					int ety = theGame.theEnemies[j].py;
	
					etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
					ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
					
					if (ex == etx && ey == ety) {
						theGame.theEnemies[j].onKilled();
					}
				}
			}
		}
			
		
		// up
		for (int i = 0; i < range; ++i) {
		
			int ex = posTileX;
			int ey = posTileY - (i+1);

			maxExplodeRangeUp = i+1;		
			
			// check if there is tile blocking
			if (theGame.isBlocked(ex, ey)) { //thePlayfield.isTileBlocking(ex, ey)) {
				// if tile is blocking it is breakable?
				if (theGame.thePlayfield.isTileBreakable(ex, ey)) {
					theGame.thePlayfield.setTileAt((byte)0, ex, ey);
				}
				Bomb b = theGame.getBombAt(ex, ey);
				if (b != null) b.explode();
				break;
			}
				
			// check if there is enemy
			for (int j = 0; j < theGame.theEnemies.length; ++j) {
				if (theGame.theEnemies[j] != null) {
					int etx = theGame.theEnemies[j].px;
					int ety = theGame.theEnemies[j].py;
	
					etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
					ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
					
					if (ex == etx && ey == ety) {
						theGame.theEnemies[j].onKilled();
					}
				}
			}
			
			// check if there is player
			if (theGame.thePlayer.posTileX == ex && theGame.thePlayer.posTileY == ey) {
				// Player dies
				theGame.thePlayer.onKilled();
			}
		}
		
		// down
		for (int i = 0; i < range; ++i) {
		
			int ex = posTileX;
			int ey = posTileY + (i+1);

			maxExplodeRangeDown = i+1;		
			
			// check if there is tile blocking
			if (theGame.isBlocked(ex, ey)) { //thePlayfield.isTileBlocking(ex, ey)) {
				// if tile is blocking it is breakable?
				if (theGame.thePlayfield.isTileBreakable(ex, ey)) {
					theGame.thePlayfield.setTileAt((byte)0, ex, ey);
				}
				Bomb b = theGame.getBombAt(ex, ey);
				if (b != null) b.explode();
				break;
			}
				
			// check if there is enemy
			for (int j = 0; j < theGame.theEnemies.length; ++j) {
				if (theGame.theEnemies[j] != null) {
					int etx = theGame.theEnemies[j].px;
					int ety = theGame.theEnemies[j].py;
	
					etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
					ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
					
					if (ex == etx && ey == ety) {
						theGame.theEnemies[j].onKilled();
					}
				}
			}
			
			// check if there is player
			if (theGame.thePlayer.posTileX == ex && theGame.thePlayer.posTileY == ey) {
				// Player dies
				theGame.thePlayer.onKilled();
			}
		}
		
		// left
		for (int i = 0; i < range; i++) {
		
			int ex = posTileX - (i+1);
			int ey = posTileY;

			maxExplodeRangeLeft = i+1;		
			
			// check if there is tile blocking
			if (theGame.isBlocked(ex, ey)) { //thePlayfield.isTileBlocking(ex, ey)) {
				// if tile is blocking it is breakable?
				if (theGame.thePlayfield.isTileBreakable(ex, ey)) {
					theGame.thePlayfield.setTileAt((byte)0, ex, ey);
				}
				Bomb b = theGame.getBombAt(ex, ey);
				if (b != null) b.explode();
				break;
			}
				
			// check if there is enemy
			for (int j = 0; j < theGame.theEnemies.length; ++j) {
				if (theGame.theEnemies[j] != null) {
					int etx = theGame.theEnemies[j].px;
					int ety = theGame.theEnemies[j].py;
	
					etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
					ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
					
					if (ex == etx && ey == ety) {
						theGame.theEnemies[j].onKilled();
					}
				}
			}
			
			// check if there is player
			if (theGame.thePlayer.posTileX == ex && theGame.thePlayer.posTileY == ey) {
				// Player dies
				theGame.thePlayer.onKilled();
			}
		}
		
		// right
		for (int i = 0; i < range; i++) {
		
			int ex = posTileX + (i+1);
			int ey = posTileY;
		
			maxExplodeRangeRight = i+1;		
			
			// check if there is tile blocking
			if (theGame.isBlocked(ex, ey)) { //thePlayfield.isTileBlocking(ex, ey)) {
				// if tile is blocking it is breakable?
				if (theGame.thePlayfield.isTileBreakable(ex, ey)) {
					theGame.thePlayfield.setTileAt((byte)0, ex, ey);
				}
				Bomb b = theGame.getBombAt(ex, ey);
				if (b != null) b.explode();
				break;
			}
			
			// check if there is enemy
			for (int j = 0; j < theGame.theEnemies.length; ++j) {
				if (theGame.theEnemies[j] != null) {
					int etx = theGame.theEnemies[j].px;
					int ety = theGame.theEnemies[j].py;
	
					etx = (etx + theGame.getTileWidth()/2) / theGame.getTileWidth();
					ety = (ety + theGame.getTileHeight()/2) / theGame.getTileHeight();
					
					if (ex == etx && ey == ety) {
						theGame.theEnemies[j].onKilled();
					}
				}
			}
			
			// check if there is player
			if (theGame.thePlayer.posTileX == ex && theGame.thePlayer.posTileY == ey) {
				// Player dies
				theGame.thePlayer.onKilled();
			}
		}
	}
}