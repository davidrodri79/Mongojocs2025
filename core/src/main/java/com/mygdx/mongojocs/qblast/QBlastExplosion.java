package com.mygdx.mongojocs.qblast;

public class QBlastExplosion {


	public int x, y, z, counter, radius, lenr, lenl, lenu, lend;
	QBlastLevel lev;
	public boolean active=false;
	
	public QBlastExplosion(QBlastLevel l)
	{
		lev=l; active=false;
	}

	public void set(int xx, int yy, int zz, int rad)
	{
		x=xx; y=yy; z=zz; counter=0; active=true; radius=rad;
		lenu=0; lend=0; lenl=0; lenr=0; 
		int i;
		
		// Left range
		lenl=0;
		do
			lenl++;
		while(!lev.solid(xx-lenl,yy,zz) && lenl<radius);
		
		// Right range
		lenr=0;
		do
			lenr++;
		while(!lev.solid(xx+lenr,yy,zz) && lenr<radius);		
		
		// Up range
		lenu=0;
		do
			lenu++;
		while(!lev.solid(xx,yy,zz-lenu) && lenu<radius);			
		
		// Down range
		lend=0;
		do
			lend++;
		while(!lev.solid(xx,yy,zz+lend) && lend<radius);					
		
		//System.out.println(""+lenl+","+lenr+","+lend+","+lenu);
	}

	public void update()
	{		
		for(int i=0; i<=radius; i++){
			lev.unsetExists(x+i,y,z,2);
			lev.unsetExists(x-i,y,z,2);
			lev.unsetExists(x,y,z+i,2);
			lev.unsetExists(x,y,z-i,2);
		}
		counter++;
		if(counter>=18)
			active=false;	
		else {			
			
			for(int i=0; i<=radius; i++){
				if(i<=lenr)
					lev.setExists(x+i,y,z,2);
				if(i<=lenl)
					lev.setExists(x-i,y,z,2);
				if(i<=lend)
					lev.setExists(x,y,z+i,2);
				if(i<=lenu)
					lev.setExists(x,y,z-i,2);
				
				// Destroy gray cubes
				if(counter==4){
					if(lev.checkCube(x,y,z)==6)
						lev.grid[y][z][x]=0;
					if(i<=lenr)
					if(lev.checkCube(x+i,y,z)==6)
						lev.grid[y][z][x+i]=0;
					if(i<=lenl)
					if(lev.checkCube(x-i,y,z)==6)
						lev.grid[y][z][x-i]=0;
					if(i<=lend)
					if(lev.checkCube(x,y,z+i)==6)
						lev.grid[y][z+i][x]=0;
					if(i<=lenu)
					if(lev.checkCube(x,y,z-i)==6)
						lev.grid[y][z-i][x]=0;					
				}
			}
		}
	}
	
	public int burnsAt(int xx, int yy, int zz)
	{
		if(y==yy){
			if(z==zz){
				if(x==xx) return 5;
				else if(xx>=x-radius && xx<x) return 3;
				else if(xx<=x+radius && xx>x) return 1;
				else return 0;
			}else if(x==xx){
				if(zz>=z-radius && zz<z) return 4;
				else if(zz<=z+radius && zz>z) return 2;
				else return 0;
			}else return 0;
		}else return 0;
	}
}

