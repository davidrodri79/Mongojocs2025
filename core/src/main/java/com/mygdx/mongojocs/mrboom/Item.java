package com.mygdx.mongojocs.mrboom;


class Item extends SpriteEntity {

	int type = ITEM_NUMBOMB;
	
	static final int ITEM_NUMBOMB	= 0;
	static final int ITEM_RANGEBOMB	= 1;
	
	int	posTileX, posTileY;
	
	Item(int x, int y) {
		
		type = Game.rand() & 0x1;
		
		px = x;
		py = y;

		Game theGame = Game.theGame;		

		posTileX = (px + theGame.getTileWidth()/2) / theGame.getTileWidth();
		posTileY = (py + theGame.getTileHeight()/2) / theGame.getTileHeight();
	}	
	
	void draw(GameCanvas gc) {
		gc.imageDrawAndInvalidate(gc.tabImgItem[type], px, py);		
	}
	
	void tick() {
		Player p = Game.theGame.thePlayer;
		
		if (p.posTileX == posTileX && p.posTileY == posTileY) {
			onPicked(p);
			Game.theGame.theItem = null;
		}
	}
	
	void onPicked(Player p) {
		switch (type) {
			case ITEM_NUMBOMB:
				if (p.numBomb < Game.theGame.theBombs.length)
					++p.numBomb;
				break;
			case ITEM_RANGEBOMB:
				if (p.rangeBomb < 4)
					++p.rangeBomb;
				break;
		}
	}
	
}