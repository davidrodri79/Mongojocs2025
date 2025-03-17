package com.mygdx.mongojocs.hellgates;
/*===================================================================
  HellGates MIDP2.0 
  -------------------------------------------------------------------
  Programmed by David Rodriguez 2004
  ===================================================================*/
  
public class level {
	
	
	public static final byte EMPTY_TILE = 0, FULL_TILE = 1, MIDDLE_TILE = 2, 
							UP45_TILE = 3, UP30_TILE_LOW = 4, UP30_TILE_HIGH = 5,
					  		DOWN45_TILE = 6, DOWN30_TILE_LOW = 7, DOWN30_TILE_HIGH = 8, ITEM_TILE = 9, MIDDLE_TILE_HIGH = 10; 
	
			
	byte map[], tileCategory[];
	int sizeX, sizeY;
	int mapSizesX[] = {120,144,160,80,180};
	int mapSizesY[] = {8,24,13,80,18};

	public level(Bios bios, int which)
	{
	
		sizeX = mapSizesX[which]; sizeY = mapSizesY[which];
		map = bios.loadFile("/"+which+".map");
		
		tileCategory = new byte[256];
		
		for(int i = 0; i<256; i++)
			tileCategory[i] = EMPTY_TILE;
		
		tileCategory[1] = FULL_TILE;
		tileCategory[2] = UP45_TILE;
		tileCategory[3] = DOWN45_TILE;
		tileCategory[4] = UP30_TILE_LOW;
		tileCategory[5] = UP30_TILE_HIGH;
		tileCategory[6] = DOWN30_TILE_HIGH;
		tileCategory[7] = DOWN30_TILE_LOW;
		tileCategory[8] = MIDDLE_TILE;
		tileCategory[12] = FULL_TILE;
		tileCategory[13] = MIDDLE_TILE_HIGH;
		tileCategory[64] = ITEM_TILE;
		
	}

	int tileAt(int x, int y)
	{
		int mx=(x/24), my=(y/24);
	
		if(mx<0) mx=0; if(mx>=sizeX) mx=sizeX-1; 
		if(my<0) my=0; if(my>=sizeY) my=sizeY-1;
	
		return tileCategory[map[(my*sizeX)+mx]];

	}

	int yAtTile(int x, int y)
	{
		int mx=(x/24), my=(y/24);
	
		if(mx<0) mx=0; if(mx>=sizeX) mx=sizeX-1; 
		if(my<0) my=0; if(my>=sizeY) my=sizeY-1;
		
		switch(tileCategory[map[(my*sizeX)+mx]])
		{
			default : 
			case EMPTY_TILE : return 9999;
			
			case MIDDLE_TILE_HIGH :
			
			case FULL_TILE : return ((int)(y/24))*24;
			
			case MIDDLE_TILE : return (((int)(y/24))*24)+(24/2);
			
			case UP45_TILE : return (((int)(y/24))*24) + 24 - (x%24);
			
			case DOWN45_TILE : return (((int)(y/24))*24) + (x%24);
			
			case DOWN30_TILE_HIGH : return (((int)(y/24))*24) + ((x%24)/2);
			
			case DOWN30_TILE_LOW : return (((int)(y/24))*24) + (24/2) + ((x%24)/2);
			
			case UP30_TILE_HIGH : return (((int)(y/24))*24) + (24/2) - ((x%24)/2);
			
			case UP30_TILE_LOW : return (((int)(y/24))*24) + 24 - ((x%24)/2);
					
		}
	}
	
	int nearestDiv(int n, int d)
	{
		return  n - (n%d);		
	}

	boolean rectCollide(rect r, int p)
	{
		if(r.x1 < 0 || r.y1 < -50 || r.x2 >= sizeX*24) return true;
		
		for(int x = nearestDiv(r.x1,p); x <= nearestDiv(r.x2,p); x+=p)
			for(int y = nearestDiv(r.y1,p); y <= nearestDiv(r.y2,p) - 2; y+=p)
			{
				if(tileAt(x,y) == FULL_TILE) return true;				
				if(tileAt(x,y) == MIDDLE_TILE && (y%24)>=12) return true;				
				if(tileAt(x,y) == MIDDLE_TILE_HIGH && (y%24)<12) return true;				
				if(tileAt(x,y) == UP30_TILE_HIGH && (y%24)>=12) return true;	
				if(tileAt(x,y) == DOWN30_TILE_LOW && (y%24)>=12) return true;				
			}	
		return false;
	}
	
	void addItem(int x, int y)
	{
		if(tileAt(x,y)==0)
			setTile(x,y,(byte)64);								
	}
	
	void setTile(int x, int y, byte v)
	{
		int mx=(x/24), my=(y/24);
	
		if(mx<0) mx=0; if(mx>=sizeX) mx=sizeX-1; 
		if(my<0) my=0; if(my>=sizeY) my=sizeY-1;
	
		map[(my*sizeX)+mx] = v;			
	}


}