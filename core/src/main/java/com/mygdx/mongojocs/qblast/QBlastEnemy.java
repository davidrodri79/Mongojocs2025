package com.mygdx.mongojocs.qblast;

import java.util.Random;


public class QBlastEnemy extends QBlastBall {

	int enemyClass, IADir, cnt; 	 	
	Random rnd;
 	 	
 	public QBlastEnemy(QBlastLevel l, int xx, int yy, int zz, int t)
 	{
 		create(l,xx,yy,zz);	 		
 		type=3; enemyClass=t;
 		IADir=2; cnt=0;
 		rnd = new Random();
 	}
 	
 	public void update()
 	{ 		
 		int dx=x, dz=z;
 		cnt++;
 		 		
 		switch(IADir){
 			
 			/*case 0 : velx=MAX_SPEED/4; velz=0; dx=realx+512; dz=realz; break;
 			case 1 : velz=MAX_SPEED/4; velx=0; dx=realx; dz=realz+512; break;
 			case 2 : velx=-MAX_SPEED/4; velz=0; dx=realx-512; dz=realz; break;
 			case 3 : velz=-MAX_SPEED/4; velx=0; dx=realx; dz=realz-512; break;*/
			case 0 : if(velx<MAX_SPEED/4) velx+=10; velz=velz/2; dx=realx+512; dz=realz; break;
 			case 1 : if(velz<MAX_SPEED/4) velz+=10; velx=velx/2; dx=realx; dz=realz+512; break;
 			case 2 : if(velx>-MAX_SPEED/4) velx-=10; velz=velz/2; dx=realx-512; dz=realz; break;
 			case 3 : if(velz>-MAX_SPEED/4) velz-=10; velx=velx/2; dx=realx; dz=realz-512; break;  			 			
 		}
 		if(lev.solid(dx>>10,realy>>10,dz>>10) || !lev.solid(dx>>10,(realy-1024)>>10,dz>>10)){
 			if(Math.abs(rnd.nextInt())%2==0)
 				IADir++;
 			else IADir--;
 			
 			IADir=(4+(IADir))%4;
 				
 		}
 			
 		//System.out.println("I am ("+x+","+y+","+z+"), check-> ("+dx+","+y+","+dz+")["+lev.solid(dx,y,dz)+" and  ("+dx+","+(y-1)+","+dz+")["+lev.solid(dx,y-1,dz)+" Dir:"+IADir);
 			
		basicUpdate(); 		 			
 	}
 	
 	public void playerInteract(QBlastPlayer pl)
 	{
 		int dx=1, dy=1, dz=1, aux, dist, an;
 		
 		if(pl.state==QBlastPlayer.BROKEN || pl.cnt<15) return; 
		if(enemyClass==2){
			
			dx=realx-pl.realx;
			dy=realy-pl.realy;
			dz=realz-pl.realz;
			dist=SquareRoot.sqrt(dx*dx+dy*dy+dz*dz);
				
			an=Trig.atan(dx,dz);
			if(dist>100 && dist<4096){				
				pl.velx+=((4096-dist)*Trig.sin(an))>>17;
				pl.velz+=((4096-dist)*Trig.cos(an))>>17;
			}
			//System.out.println("Dist:"+dist+", Angle:"+an+"");  		
		}
			 		
 		// Collide 		
 		dx=1; dy=1; dz=1;
 		
 		if(Math.abs(realx-pl.realx)<512 && Math.abs(realy-pl.realy)<512 && Math.abs(realz-pl.realz)<512)
 		{
 			aux=pl.velx; pl.velx=-velx; velx=-aux;
 			aux=pl.vely; pl.vely=-vely; vely=-aux;
 			aux=pl.velz; pl.velz=-velz; velz=-aux;
 			
 			if(pl.realx<realx) dx=-1;
 			if(pl.realy<realy) dy=-1;
 			if(pl.realz<realz) dz=-1;
 			
 			pl.move(pl.realx+128*dx, pl.realy+128*dy, pl.realz+128*dz);
 			move(realx-128*dx, realy-128*dy, realz-128*dz);
 			
 			QBlastCanvas.playSound(3);
 			 			
 			// Black
 			if(pl.invulnerable==0 && pl.state!=QBlastPlayer.BROKEN){
 				if(enemyClass==0){ 		 		
					pl.setState(QBlastPlayer.BROKEN);	
					lev.setPlayerPieces(pl);
					QBlastMain.gameCanvas.vibrate(50,200);
				}
				// Radioactive
				if(enemyClass==1){ 		 		
					pl.radioactive=30;
					QBlastMain.gameCanvas.vibrate(5,2500);
				}
			}
			// Magnetic			
		}
 	}
 	 	 	
 	public void move(int rx, int ry, int rz)
 	{ 		
		basicMove(rx, ry, rz);	
 	}

}