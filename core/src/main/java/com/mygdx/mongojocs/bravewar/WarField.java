package com.mygdx.mongojocs.bravewar;

import com.mygdx.mongojocs.bravewar.Camera;

import java.util.Random;


class WarFieldTile {
		
	int groundy, UnitType, UnitOr, UnitSide, UnitAmount, lightFactor1, lightFactor2;
	
	public WarFieldTile(){
		
		 groundy=0; clear();
	}
	
	public void clear()
	{
			UnitType=-1; UnitOr=0; UnitSide=-1; UnitAmount=0;		
	}
		
}

public class WarField {

	
	public static final int attackPower[]={5,3,9,30};
	public static final int resistance[]={1,3,5,30};
	
	public static final int NOT_OVER = 0;
	public static final int WON = 1;
	public static final int LOST = 2;

	public int sx, sz; 
	public byte decor[][][];
	public WarFieldTile tiles[][];
	Point3D pts[][];
	Point3D pts2d[][];

	public WarField(int x, int z)
	{					
		sx=x; sz=z;
		tiles=new WarFieldTile[sx][sz];
		
		for(int i=0; i<sx; i++)
		for(int j=0; j<sz; j++)
				tiles[i][j]=new WarFieldTile();
				
		decor=new byte[sx][sz][3];
				
		Random rnd=new Random(100);
		
		for(int i=0; i<sx; i++)
		for(int j=0; j<sz; j++){
			tiles[i][j].groundy=(Math.abs(rnd.nextInt())%10)*30;		
			
			if(Math.abs(rnd.nextInt())%5==0){
				decor[i][j][0]=(byte)(1+(Math.abs(rnd.nextInt())%10));
				decor[i][j][1]=(byte)(rnd.nextInt()%256);
				decor[i][j][2]=(byte)(rnd.nextInt()%256);
			}
			else decor[i][j][0]=0;
		}
		
		createPoints();		
	}
	
	void createPoints()
	{
		Point3D n, light=new Point3D(1024,4096,-4096);
		int f;
		
		light.unit();
		
		pts=new Point3D[sx][sz];
		pts2d=new Point3D[sx][sz];
		for(int i=0; i<sx; i++)
		for(int j=0; j<sz; j++){
			pts[i][j] = new Point3D(i<<10,tiles[i][j].groundy,j<<10);
		}
		
		// Calculate normals
		for(int i=0; i<sx-1; i++)
		for(int j=0; j<sz-1; j++){
						
			n=Point3D.normalVector(pts[i][j],pts[i][j+1],pts[i+1][j+1]);
			f=(int)light.scalarProduct(n);
			
			if(f<0) f=0;						
			f=(int)(1024-((1024-f)/2));
			
			tiles[i][j].lightFactor1=f;
			
			n=Point3D.normalVector(pts[i+1][j+1],pts[i+1][j],pts[i][j]);
			f=(int)light.scalarProduct(n);
			
			if(f<0) f=0;						
			f=(int)(1024-((1024-f)/2));
			
			tiles[i][j].lightFactor2=f;
			
			//System.out.println("LightFactors:"+f);			
		}
		
		System.gc();

	}
	
	public void transformPoints(Camera cam, int x1, int x2)
	{
		//int xmin=x1, xmax=x2;
		x2++;
		if(x1<0) x1=0; if(x1>=sx) x1=sx-1;
		if(x2<0) x2=0; if(x2>=sx) x2=sx-1;
		
		for(int i=x1; i<=x2; i++)
		for(int j=0; j<sz; j++){
			pts2d[i][j] = pts[i][j].transform2D(cam);
		}		
		
		//System.gc();
	}
	
	public Point3D tileMiddle2D(int x, int z)
	{
		int xx, yy;
		xx=(pts2d[x][z].x+pts2d[x+1][z].x+pts2d[x+1][z+1].x+pts2d[x][z+1].x)/4;
		yy=(pts2d[x][z].y+pts2d[x+1][z].y+pts2d[x+1][z+1].y+pts2d[x][z+1].y)/4;
		return new Point3D(xx,yy);
	}
	
	public int heightAt(int x, int z)
	{
		int i, j, y;	
		Point3D p1, p2, p3, norm;
		long a, b, c, d;
		
		i=x/1024;
		j=z/1024;
		
		if(x%1024>z%1024){
			
			p1=new Point3D((i+1)<<10,tiles[i+1][j+1].groundy,(j+1)<<10);
			p2=new Point3D((i+1)<<10,tiles[i+1][j].groundy,(j)<<10);
			p3=new Point3D((i)<<10,tiles[i][j].groundy,(j)<<10);
			
		}else{
		
			p1=new Point3D((i+1)<<10,tiles[i+1][j+1].groundy,(j+1)<<10);
			p2=new Point3D((i)<<10,tiles[i][j].groundy,(j)<<10);
			p3=new Point3D((i)<<10,tiles[i][j+1].groundy,(j+1)<<10);							
		}
		
		norm=Point3D.normalVector(p1,p2,p3);
		
		a=norm.x; b=norm.y; c=norm.z;
		
		d=-a*(i<<10)-b*tiles[i][j].groundy-c*(j<<10);
				
		y=(int)((-a*x-c*z-d)/b);
												
		return y;
	}
	
	public void leaveTroops(int x1, int z1, int am)
	{
		tiles[x1][z1].UnitAmount-=am;
		if(tiles[x1][z1].UnitAmount==0)
			tiles[x1][z1].clear();			
				
	}
	
	public void putTroops(int x2, int z2, int am, int type, int side)
	{
			
		tiles[x2][z2].UnitAmount+=am;
		tiles[x2][z2].UnitType=type;
		tiles[x2][z2].UnitSide=side;		
		if(side==0) tiles[x2][z2].UnitOr=0;
		else tiles[x2][z2].UnitOr=64;		
	}	
	
	public void placeTroops(int myInf, int myArc, int myCha, int myTan, int opInf, int opArc, int opCha, int opTan)
	{	
		if(myInf>120) myInf=120;
		if(myArc>90) myInf=90;
		if(myCha>60) myCha=60;
		if(myTan>18) myTan=18;
		if(opInf>120) opInf=120;
		if(opArc>90) opArc=90;
		if(opCha>60) opCha=60;
		if(opTan>18) opTan=19;
											
		placeAtRow(myInf,20,0,0,3,0,false);										
		placeAtRow(myArc,15,1,0,2,0,false);
		placeAtRow(myCha,10,2,0,1,0,false);
		placeAtRow(myTan,3,3,0,0,0,false);
		
		placeAtRow(opInf,20,0,1,sx-5,64,true);										
		placeAtRow(opArc,15,1,1,sx-4,64,true);
		placeAtRow(opCha,10,2,1,sx-3,64,true);
		placeAtRow(opTan,3,3,1,sx-2,64,true);		
	}
	
	public void placeAtRow(int amount, int gro, int type, int side, int row, int or, boolean inv)
	{
		int z, nTiles;
		if(amount<=0) return;
		nTiles=(amount/gro)+1;
		for(int i=0; i<nTiles; i++){
				z=(sz/2)-(nTiles/2)+i;
				
				if(inv) z=sz-2-z;
				
				if(i==nTiles-1){
					if(amount%gro!=0){
						tiles[row][z].UnitAmount=amount%gro;
						tiles[row][z].UnitType=type;
						tiles[row][z].UnitSide=side;
						tiles[row][z].UnitOr=or;
					}						
				}else{
					tiles[row][z].UnitAmount=gro;				
					tiles[row][z].UnitType=type;
					tiles[row][z].UnitSide=side;
					tiles[row][z].UnitOr=or;					
				}
		}										
		
	}
		
	public void attack(int x1, int z1, int x2, int z2)
	{
		int dam1, dam2;
		
		if(tiles[x2][z2].UnitSide<0) return;
		// Infantry battle
		if(tiles[x1][z1].UnitType==0){
			
			dam1=(tiles[x2][z2].UnitAmount*attackPower[tiles[x2][z2].UnitType])/(resistance[tiles[x1][z1].UnitType]*3);			
			dam2=(tiles[x1][z1].UnitAmount*attackPower[tiles[x1][z1].UnitType])/(resistance[tiles[x2][z2].UnitType]*3);
			
			//System.out.println("DAM1: "+dam1+", DAM2:"+dam2);
			
			if(dam2>=dam1){
				tiles[x2][z2].UnitAmount=0;
				tiles[x1][z1].UnitAmount-=dam1; if(tiles[x1][z1].UnitAmount<0) tiles[x1][z1].UnitAmount=1;  
			}else{
				tiles[x1][z1].UnitAmount=0;
				tiles[x2][z2].UnitAmount-=dam2; if(tiles[x2][z2].UnitAmount<0) tiles[x2][z2].UnitAmount=1;  			
			}
						
			if(tiles[x1][z1].UnitAmount<=0) tiles[x1][z1].clear();
			if(tiles[x2][z2].UnitAmount<=0) tiles[x2][z2].clear();
								
		// Distance battle
		}else{
		
			dam1=tiles[x1][z1].UnitAmount*attackPower[tiles[x1][z1].UnitType];			
			tiles[x2][z2].UnitAmount-=dam1/resistance[tiles[x2][z2].UnitType];		
			if(tiles[x2][z2].UnitAmount<=0) tiles[x2][z2].clear();
		}
					
	}
	
	public int battleStatus()
	{
		int myTiles=0, opTiles=0;	
		
		for(int i=0; i<sx; i++)
		for(int j=0; j<sz; j++)
			if(tiles[i][j].UnitSide==0) myTiles++;
			else if(tiles[i][j].UnitSide==1) opTiles++;
			
		
		if(myTiles==0 && opTiles>0) return LOST;
		else if(opTiles==0 && myTiles>0) return WON;	
		return NOT_OVER;
			
	}
	
}