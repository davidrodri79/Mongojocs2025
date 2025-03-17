package com.mygdx.mongojocs.qblast;

import java.io.InputStream;
import java.util.Random;


public class QBlastLevel {
	
	// X, Z, Y
	public static final int gridSizes[][]={{7,5,3},{5,6,3},{8,4,4},{5,5,3},{4,8,5},{7,10,3},{5,9,3},{9,4,4},{10,4,4},{6,3,7},{8,3,7},{14,3,6},{10,5,6},{5,7,5},{4,6,5},{9,4,6},{10,3,6},{6,7,5},{10,10,4},{7,5,8},{13,5,3},{15,6,5},{15,5,5},{10,10,4},{17,5,6}};
	
	public static final int existsBytes[]={0x01, 0x02, 0x04, 0x08, 0x0F, 0x10, 0x20, 0x40, 0x80, 0xF0};
	
	public static final int NUMBER_OF_PIECES = 5;

	public int grid[][][];
	
	public int existGrid[][][];
							
	public int levelNum, sizeX, sizeY, sizeZ, begX, begY, begZ, endX, endY, endZ, activeBombs, numEnemies, numLifts, cnt;
	
	public QBlastEnemy en[];
	public QBlastLift li[];
	public QBlastBall pieces[];
	
	public int piecesPos[] = new int[NUMBER_OF_PIECES];
	
	
	Random rnd;

	public QBlastLevel(int level)
	{
		levelNum=level; 
		sizeX=gridSizes[levelNum][0]; sizeZ=gridSizes[levelNum][1]; sizeY=gridSizes[levelNum][2];
		activeBombs=0; cnt=0;
		
		grid= new int[sizeY][sizeZ][sizeX];
		
		existGrid= new int[sizeY][sizeZ][sizeX];
		
		en=new QBlastEnemy[5]; numEnemies=0;
		li=new QBlastLift[10]; numLifts=0;
		pieces=new QBlastBall[NUMBER_OF_PIECES]; for(int i=0; i<5; i++) {pieces[i]=new QBlastBall(); pieces[i].type=5; pieces[i].lev=this;};
				
		//InputStream is = getClass().getResourceAsStream("/"+(levelNum+1)+".map");

		byte is[] = QBlastCanvas.loadFile("/"+(levelNum+1)+".map", 0);
		int ispos=0;
		byte buffer[] = new byte[sizeX];
		
		rnd = new Random();
			
		try	{
			
			for(int j=0; j<sizeY; j++)
			for(int k=0; k<sizeZ; k++){
				
				for(int i=0; i<sizeX; i++) {
					buffer[i] = (byte) is[ispos];
					ispos++;
				}
													
				for(int i=0; i<sizeX; i++){
					
					grid[j][k][i]=(int)buffer[i];
					existGrid[j][k][i]=0;				
					
					switch(grid[j][k][i]){
						
						case 7 : endX=i; endY=j; endZ=k; break;
						case 8 : begX=i; begY=j; begZ=k; break;
						case 10 : 
						en[numEnemies] = new QBlastEnemy(this,i,j,k,0);
						numEnemies++;
						grid[j][k][i]= 0;
						break;
						
						case 9 : 
						en[numEnemies] = new QBlastEnemy(this,i,j,k,1);
						numEnemies++;
						grid[j][k][i]= 0;
						break;						
						
						case 11 : 
						en[numEnemies] = new QBlastEnemy(this,i,j,k,2);
						numEnemies++;
						grid[j][k][i]= 0;
						break;
						
						case 16 :
						li[numLifts] = new QBlastLift(this,i,j,k);
						numLifts++;
						grid[j][k][i]= 17;
						break;
					}
				}
			}
							
			//is.close();
		
		}catch(Exception exception) {}
		
		System.gc();
					
	}
	
	void update()
	{
		cnt++;		
			
	}
		
	public int checkCube(int x, int y, int z)
	{
		//System.out.println("Checking ("+x+","+y+","+z+")");
		if(x>=0 && x<sizeX && y>=0 && y<sizeY && z>=0 && z<sizeZ)
			return grid[y][z][x];
		else return 0;
	}
	
	public boolean solid(int x, int y, int z)
	{
		int c=checkCube(x,y,z);
		return (c>0 && c<7);	
	}
	
	public int nearestGround(int rx, int ry, int rz)
	{
		int x=rx>>10, y=ry>>10, z=rz>>10, c;
		
		c=checkCube(x,y,z);
		
		// Check if I am in a lift
		if(checkExists(x,y,z,4)){
			
			for(int i=0; i<numLifts; i++)
				if(li[i].x==x && li[i].y==y && li[i].z==z)
					return li[i].realy+512;
		}	
		if(c==0 || c==8 || c>=17){
		
			c=checkCube(x,y-1,z);
			if(c==0) return -99999;
			else if(c==1 || c==6) return y<<10;
			else return nearestGround(rx,ry-1024,rz);
			
		}else if(c==2){
			
			return (y<<10)+(1024-(rz%1024));
				
		}else if(c==4){
			
			return (y<<10)+(rz%1024);
			
		}else if(c==5){
			
			return (y<<10)+(1024-(rx%1024));
				
		}else if(c==3){
			
			return (y<<10)+(rx%1024);
							
		}else{
		
			return y<<10;
		}	
		//return -99999;	
	}
	
	public int nearestCube(int rx, int ry, int rz)
	{
		int x=rx>>10, y=ry>>10, z=rz>>10, c;
		
		c=checkCube(x,y,z);
		
		if(c==0){		
			c=checkCube(x,y-1,z);
			if(c==0) return 0;
			else return c;
			
		}else return c;		
	}	
	
	public void setExists(int x, int y, int z, int which)
	{
		if(x>=0 && x<sizeX && y>=0 && y<sizeY && z>=0 && z<sizeZ)
			existGrid[y][z][x] = existGrid[y][z][x] | existsBytes[which];												
	}
	
	public void unsetExists(int x, int y, int z, int which)
	{
		if(x>=0 && x<sizeX && y>=0 && y<sizeY && z>=0 && z<sizeZ)
			existGrid[y][z][x] = existGrid[y][z][x] & ~existsBytes[which];														
	}
	
	public boolean checkExistsAny(int x, int y, int z)
	{
		if(x<0 || x>=sizeX || y<0 || y>=sizeY || z<0 || z>=sizeZ){			
			return false;
		}else return (existGrid[y][z][x] != 0);
	}
		
	
	public boolean checkExists(int x, int y, int z, int which)
	{
		if(x<0 || x>=sizeX || y<0 || y>=sizeY || z<0 || z>=sizeZ){
			if(which<=1) return true;
			else return false;
		}else return (existGrid[y][z][x] & existsBytes[which]) != 0;
	}
	
	public void setPlayerPieces(QBlastPlayer pl)
	{
		for(int i=0; i<pieces.length; i++){
			pieces[i].copy(pl);			
							
			pieces[i].vely+=Math.abs(rnd.nextInt())%400;
			pieces[i].velx+=(Math.abs(rnd.nextInt())%400)-200;
			pieces[i].velz+=(Math.abs(rnd.nextInt())%400)-200;		
			
			piecesPos[i] = 0;
		}
	}

}