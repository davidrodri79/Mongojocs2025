package com.mygdx.mongojocs.mrboom;

import com.mygdx.mongojocs.iapplicationemu.Image;

class Playfield {

	static final int numTilesX = 13;
	static final int numTilesY = 13;
	
	static final int tileWidth  = 12;
	static final int tileHeight = 12;
	
	byte[] originalMap;
	byte[] currentMap;

	
	void init(int numLevel) {
		
		Game theGame = Game.theGame;

		originalMap = new byte[numTilesX*numTilesY];
		currentMap  = new byte[numTilesX*numTilesY];
		
		// delete old enemies & bombs
		for (int i = 0; i < theGame.theBombs.length; ++i) {
			theGame.theBombs[i] = null;
		}
		
		for (int i = 0; i < theGame.theEnemies.length; ++i) {
			theGame.theEnemies[i] = null;
		}
		
		theGame.theItem = null;

		
		// load map
		for (int i = 0; i < originalMap.length; i++) {
				originalMap[i] = theGame.gc.tabMaps[numLevel][i];
				currentMap [i] = -1;
				
				if (originalMap[i] == 3) {
					theGame.thePlayer.setPlayerPos((i%numTilesX)*tileWidth, (i/numTilesX)*tileHeight);

					// replace to avoid interference
					originalMap[i] = 0;
				}
				if (originalMap[i] == 4) {
					// create enemy
					theGame.setNewEnemy(new Enemy(Enemy.ENEMY_REBOOT_V, (i%numTilesX)*tileWidth, (i/numTilesX)*tileHeight));
					
					// replace to avoid interference
					originalMap[i] = 0;
				}
				if (originalMap[i] == 5) {
					// create enemy
					theGame.setNewEnemy(new Enemy(Enemy.ENEMY_REBOOT_H, (i%numTilesX)*tileWidth, (i/numTilesX)*tileHeight));
					
					// replace to avoid interference
					originalMap[i] = 0;
				}
				if (originalMap[i] == 6) {
					// create enemy
					theGame.setNewEnemy(new Enemy(Enemy.ENEMY_FOURDIR, (i%numTilesX)*tileWidth, (i/numTilesX)*tileHeight));
					
					// replace to avoid interference
					originalMap[i] = 0;
				}
				if (originalMap[i] == 7) {
					// create enemy
					theGame.setNewEnemy(new Enemy(Enemy.ENEMY_SMART, (i%numTilesX)*tileWidth, (i/numTilesX)*tileHeight));
					
					// replace to avoid interference
					originalMap[i] = 0;
				}
		}
		
		// spawn enemies
		// set player initial pos
	}

	void invalidateAll() {
		for (int i = 0; i < originalMap.length; i++) {
				currentMap [i] = -1;
		}
	}
	
	boolean isTileBlocking(int tx, int ty) {
		return originalMap[tx+ty*numTilesX] != 0;
	}
	
	boolean isTileBreakable(int tx, int ty) {
		return originalMap[tx+ty*numTilesX] == 2 || originalMap[tx+ty*numTilesX] == 8;
	}
	
	boolean isEndLevelBlock(int tx, int ty) {
		return originalMap[tx+ty*numTilesX] == 9;
	}
	
	void setTileAt(byte t, int tx, int ty) {
		
		if (t == 0) {
			if (Game.theGame.theItem==null) {
				int r = Game.rand() % 444;
				if (r > 20 && r < 190) {
					Game.theGame.theItem = new Item(tx*tileWidth, ty*tileHeight);
				}
			}
		}
		// change map 
		if (t==0 && originalMap[tx+ty*numTilesX] == 8) {
			originalMap[tx+ty*numTilesY] = 9;
		}
		else {
			originalMap[tx+ty*numTilesY] = t;
		}
		currentMap [tx+ty*numTilesY] = -1;

		
		// check for winning conditions
	}
	
	void invalidateRect(int px, int py, int w, int h) {
		
		int tileIniX, tileIniY, tileEndX, tileEndY;
		
		tileIniX = px / tileWidth;
		tileIniY = py / tileHeight;
		
		tileEndX = ((px+w) / tileWidth)+1;
		tileEndY = ((py+h) / tileHeight)+1;
		
		for (int y = tileIniY; y < tileEndY; y++) {
			for (int x = tileIniX; x < tileEndX; x++) {
				currentMap[x+y*numTilesX] = -1;
			}
		}
	}

	void refresh(GameCanvas gc) {
		
		int modLevel = Game.theGame.numLevel % 3;
		Image[]tabImgTiles = gc.tabImgTiles;
		
		switch (modLevel) {
			case 0:
				tabImgTiles = gc.tabImgTiles; break;
			case 1:
				tabImgTiles = gc.tabImgTiles2; break;
			case 2:
				tabImgTiles = gc.tabImgTiles3; break;
		}
				
		
		// redraw invalid tiles
		for (int y = 0; y < numTilesY; y++) {
			for (int x = 0; x < numTilesX; x++) {
				int index = x+y*numTilesX;
				if (originalMap[index] != currentMap[index]) {

					byte tileToDraw = originalMap[index];
					
					gc.imageDraw(tabImgTiles[tileToDraw], x*tileWidth, y*tileHeight);
					
					currentMap[index] = tileToDraw;
					
				}
			}
		}
	}
	
	boolean isAtTile() {
		return false;	
	}	
}