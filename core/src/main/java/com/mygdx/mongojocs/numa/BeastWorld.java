package com.mygdx.mongojocs.numa;

public class BeastWorld {

	public byte map[];
	public int sizeX, sizeY, level, tileSet;
	public BeastShot shots[];
	public BeastShot effects[];
	public boolean itemTaken[] = new boolean[20];
	
	public static final short levelXSizes[]={75,125,200,125,250,250,250,25};
	public static final short levelYSizes[]={16,16,8,16,8,8,8,8};
	
	public static final short items[][][]={
		{{5,5},{8,14},{15,8},{24,14},{31,3},{13,14},{40,14},{60,3},{58,5},{57,8},{55,14},{73,7},{73,13},{53,8}},
		{{0,6},{1,6},{7,3},{12,7},{1,14},{21,2},{23,14},{33,2},{55,5},{103,13},{88,3},{81,9},{71,14},{92,14},{98,11},{64,14},{66,3},{75,9},{119,14},{113,12}},
		{{5,6},{15,6},{22,7},{34,1},{47,3},{53,6},{58,5},{70,7},{71,1},{81,1},{95,6},{103,2},{120,2},{143,4},{163,2},{161,6},{168,5},{173,5},{191,3},{189,6}},
		{{2,8},{16,8},{13,14},{15,14},{30,5},{39,3},{52,5},{47,9},{62,6},{75,4},{71,8},{85,9},{88,3},{66,14},{94,7},{79,12},{91,14},{95,4},{108,6},{118,14}},
		{{5,6},{21,2},{46,1},{76,1},{58,3},{41,6},{103,2},{117,4},{121,2},{125,5},{150,3},{166,6},{183,2},{220,1},{235,3},{216,5},{221,5},{236,6},{144,6},{190,4}},
		{{1,4},{6,2},{1,7},{2,6},{19,6},{24,6},{36,3},{61,5},{59,0},{76,1},{86,1},{103,6},{143,4},{167,3},{195,1},{213,1},{225,1},{197,5},{132,3},{230,5}},
		{{2,3},{24,3},{33,1},{53,2},{69,1},{72,6},{78,6},{79,6},{90,2},{105,1},{121,0},{132,6},{159,1},{172,6},{194,3},{214,3},{228,6},{229,2},{239,1},{231,6}},
		{{5,6},{13,3},{14,5}}
	};
	
	public static final byte minimum[] = {7,10,11,13,14,15,16,0};
	
	int mapSizes[]={1200,2000,1600,2000,2000,2000,2000,200};

	BeastWorld(int l)
	{
		//InputStream is = getClass().getResourceAsStream("/com.mygdx.mongojocs.bravewar.Map"+(l+1)+".map");
					
		level=l;  sizeX=levelXSizes[level]; sizeY=levelYSizes[level];
								
		//map = new byte[sizeX*sizeY];
		
		map = BeastCanvas.multiread("/Maps.dat",mapSizes,level);
		
		if(level<3) tileSet=0;
		else if(level<5) tileSet=1;
		else tileSet=2;
					
		/*try{
			for(int i=0; i<map.length; i++)
				map[i] = (byte) is.read();
				
			is.close();
			
		}catch(java.io.IOException err) {}*/
		
		for(int i=0; i<map.length; i++)
			if(map[i]<0) map[i]+=128;
			
		shots=new BeastShot[6]; 
		effects=new BeastShot[4];
				
		for(int i=0; i<itemTaken.length; i++) itemTaken[i] = false;
	}
	
	public void update()
	{
		for(int i=0; i<shots.length; i++)
			if(shots[i]!=null){
				shots[i].update();
				for(int j=0; j<shots.length; j++){
					
					if(i<j)
					if(shots[j]!=null)
					if(shots[i].damage.collide(shots[j].damage))
					{
						addEffect((shots[i].x+shots[j].x)/2,10+(shots[i].y+shots[j].y)/2,BeastShot.EXPLOSION);
						shots[i].outOfWorld = true;		
						shots[j].outOfWorld = true;								
					}
					
				}	
				if(shots[i].outOfWorld()) shots[i]=null;				
			}
			
		for(int i=0; i<effects.length; i++)
			if(effects[i]!=null){
				effects[i].updateEffect();	
				if(effects[i].outOfWorld) effects[i]=null;				
			}			
	}
	
	public boolean ground(int x, int y)
	{
		
		int t=checkTile(x,y);
				
		switch(tileSet){
			
			default :
		
			switch(t){
												
				case 18 :
				case 21 :
				case 30 :
				//case 49 :
				case 50 :
				//case 56 :				
				case 1 :
				case 3 :
				case 4 :
				case 5 :
				case 6 :			
				case 9 :
				case 10 :
				case 12 :				
				case 13 :
				case 22 :
				case 23 :
				case 26 :
				case 28 :
				case 29 :
				case 34 :
				case 55 :
				case 54 :				
				case 53 :
				case 59 :
				case 60 :
				//case 47 :
				//case 57 :			
				case 58 :
				case 63 : 
				case 14 :
				case 15 :
				//case 46 :				
				case 62 : 
				//case 45 :
				case 61 : return true;
				// Total collision								
				default : return false;				
				
				
			}
			
			case 1 :			
			switch(t){
			
				case 55 :
				case 54 :
				case 52 :
				case 51 :
				case 50 :
				case 49 :
				case 47 :
				case 46 :
				case 43 :
				case 42 :
				case 36 :
				case 35 :
				case 34 :
				case 33 :
				case 27 :
				case 26 :
				case 17 :
				case 16 :
				case 3 :
				case 2 : return true;
				default : return false;	
			}								
			
			case 2 :			
			switch(t){
			
				case 63 :
				case 62 :
				case 61 :
				case 60 :
				case 58 :
				//case 59 :
				case 57 :
				case 54 :
				case 53 :
				case 52 :
				case 51 :
				case 50 :
				case 46 :
				case 45 :
				case 44 :
				case 43 :
				//case 39 :
				case 38 :
				case 31 :
				case 30 :
				case 29 :
				case 28 :
				case 27 :
				case 22 :
				case 21 :
				case 20 :
				case 19 :
				case 18 :
				case 17 :
				case 16 :
				case 15 :
				case 14 : 
				case 13 :				
				case 10 :
				case 9 :
				case 8 :
				case 7 :
				case 6 :
				case 5 :
				case 4 :
				case 3 :
				case 2 : return true;
				default : return false;	
			}											
		}
	}
	
	public int avgYAt(int xx, int yy)
	{
		if(ground(xx-8,yy) && ground(xx+8,yy))
			return (yAt(xx-8,yy) + yAt(xx+8,yy))/2;
		else return yAt(xx,yy);
	}
	
	public int yAt(int xx, int yy)
	{
		int x=xx/16; 
		int y=yy/16;
		
		if(x<0) x=0; if(x>=sizeX) x=sizeX-1;
		if(y<0) y=0; if(y>=sizeY) y=sizeY-1;
		
			
		switch(tileSet){
			
			default :		
		
			switch(map[(sizeX*y)+x]){
				
				case 47 :
				case 57 :			
				case 58 :
				case 63 : return ((y<<4)+(xx%16))<<7;
				case 14 :
				case 15 :
				case 46 :
								
				case 62 : return ((y<<4)+4)<<7;						
				case 45 :
				case 61 : return ((y<<4)+(16-(xx%16)))<<7;
				// Total collision
				
				case 18 :
				case 21 :
				case 30 :
				case 49 :
				case 50 :
				case 56 : return (y<<4)<<7;
				
				
				default : return ((y<<4)+10)<<7;
			}		
			
			case 1 :
			
			switch(map[(sizeX*y)+x]){
				
				case 49 :
				case 46 :
				case 42 :
				case 26 : return ((y<<4)+(16-(xx%16)))<<7;
				//case 62 : return ((y<<4)+4)<<7;					
				case 55 :
				case 53 :
				case 43 :					
				case 27 : return ((y<<4)+(xx%16))<<7;
				case 54 :
				case 47 :
				case 35 :
				case 34 :
				case 50 :
				case 51 :
				default : return ((y<<4)+2)<<7;
			}					
			
			case 2 :
			
			switch(map[(sizeX*y)+x]){
				
				case 43 :
				case 53 :
				case 50 :
				case 16 :
				case 8 :
				case 2 : return ((y<<4)+(16-(xx%16)))<<7;				
				
				case 62 :
				case 52 :
				case 17 :
				case 14 :				
				case 7 : return ((y<<4)+(xx%16))<<7;
				
				case 46 :
				case 57 :
				case 51 :
				case 39 :
				case 22 : return ((y<<4)+4)<<7;
				//case 58 : return ((y<<4)+4)<<7;
						
				case 54 :
				case 58 :
				case 61 :
				case 60 :								
				case 21 :
				case 20 :		
				case 19 :
				case 18 :
				case 9  :
				case 13 : return (y<<4)<<7;
				
				default : return ((y<<4)+12)<<7;
			}								
			
		}
	}
	
	public boolean waste(int xx, int yy)
	{
				
		int t = checkTile(xx,yy);
			
		switch(tileSet){
			
			default :		
			return t==57;
			
			case 1 :		
			return t==30;
			
			case 2 :		
			return t==45;
			
		}		
						
	}
	
	public boolean spikes(int xx, int yy)
	{
				
		int t = checkTile(xx,yy);
			
		switch(tileSet){
			
			default :		
			return false;
			
			case 1 :		
			return t==25 || t==28;
			
			case 2 :		
			return t==43 || t==44;
			
		}	
	}
	
	public byte checkTile(int xx, int yy)
	{
		int x=xx/16; 
		int y=yy/16;
		
		if(x<0) x=0; if(x>=sizeX) x=sizeX-1;
		if(y<0) y=0; if(y>=sizeY) y=sizeY-1;
		
		return map[(sizeX*y)+x];		
	}
	
	public void addShot(int x, int y, int d, int vx, int vy, int t)
	{
		int i=0;
		while(i<shots.length && shots[i]!=null)
			i++;
			
		if(i<shots.length)
			shots[i]=new BeastShot(x,y,d,vx,vy,t);			
	}
	
	public void addEffect(int x, int y, int t)
	{
		int i=0;
		while(i<effects.length && effects[i]!=null)
			i++;
			
		if(i<effects.length)
			effects[i]=new BeastShot(x,y,t);			
	}	

}