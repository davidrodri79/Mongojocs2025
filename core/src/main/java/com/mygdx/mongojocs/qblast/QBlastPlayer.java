package com.mygdx.mongojocs.qblast;

public class QBlastPlayer extends QBlastBall {
	
	public static final int PLAYING = 0;
	public static final int BROKEN = 1;
	public static final int DEAD = 2;

 	public int rotx=0, rotz=0, explPower, maxBombs, state, cnt, radioactive, invulnerable;
 	
 	public QBlastPlayer(QBlastLevel l, int xx, int yy, int zz)
 	{
 		create(l,xx,yy,zz);	 		
 		type=0; explPower=1; maxBombs=1;
 		state=PLAYING; radioactive=0; invulnerable=0;  		
 	}
 	
 	public void setState(int s)
 	{
 		state=s; cnt=0;	
 	}
 	
 	public void update()
 	{
 		int c;
 		
 		cnt++;
 		if(radioactive>0) radioactive--;
 		if(invulnerable>0) invulnerable--;
 		
 		basicUpdate();
 		
 		switch(state)
 		{
 			case PLAYING :
 			 		
 			// Take items
 			c=lev.checkCube(x,y,z);
 			if(c>=11 && c<=15){
 				switch(c){
 					case 12 : invulnerable=300; break;
 					case 13 : QBlastMain.clock+=13*45; break;
 					case 14 : maxBombs=4; break;
 					case 15 : explPower++; break;	
 				}
 				lev.grid[y][z][x]=0;
 				QBlastCanvas.playSound(1);
 			}
 			if(y<=-5) setState(DEAD);
 			break;
 			
 			case BROKEN :
 			if(cnt>=40) setState(DEAD);
 			break;
 			
 			case DEAD :
 			break;
 		}
 	}
 	
 	public void move(int rx, int ry, int rz)
 	{ 		
		rotx+=(rz-realz)/8; if(rotx>=256) rotx-=256; if(rotx<0) rotx+=256;
		rotz+=(rx-realx)/8; if(rotz>=256) rotz-=256; if(rotz<0) rotz+=256; 		
		basicMove(rx, ry, rz);	
 	}

}