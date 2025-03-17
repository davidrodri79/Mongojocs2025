
package com.mygdx.mongojocs.ultranaus;

import java.io.InputStream;
import java.util.Random;

public class UltraRace{

	public static final int DIST_OPPONENT = 32*32;
	public static final int N_ITEMS = 3;	
	public static final int N_ROCKETS = 5;
	public static final int TURBO = 1;
	public static final int ENERGY = 2;
	public static final int SHIELD = 3;
	public static final int ROCKET = 4;
	
	public static final int checkp[][][]={
		
		{{16,16,49,56},{49,16,136,56},{73,50,161,87},{162,48,240,88},{165,88,240,139},{163,134,241,184},{56,137,162,185},{14,136,55,185},{16,54,50,135}},
		{{158,7,234,67},{232,7,419,111},{407,6,473,122},{409,120,474,194},{405,186,474,250},{217,192,408,252},{69,190,217,252},{8,187,70,251},{9,70,66,186},{7,8,73,71},{72,7,162,67}},
		{{7,75,48,170},{7,9,56,87},{48,9,198,81},{192,9,242,80},{190,76,248,101},{186,96,248,153},{110,97,190,155},{59,96,119,155},{61,149,121,174},{60,167,121,226},{119,166,196,228},
			{191,168,248,228},{190,221,247,246},{188,239,249,298},{105,240,188,299},{63,241,120,298},{64,292,120,316},{62,307,121,370},{121,311,192,370},{184,311,249,370},
			{181,400,248,472},{56,400,184,472},{7,400,57,474},{6,280,63,400},{6,200,50,279},{6,150,50,200}},
		{{16,62,57,214},{16,16,65,72},{56,13,196,64},{190,14,250,71},{190,71,250,216},{192,215,258,279},{260,215,322,280},{272,55,322,216},{272,13,328,55},{272,14,327,56},
			{327,15,370,56},{334,58,370,314},{328,312,370,375},{182,318,328,377},{135,318,184,375},{136,117,180,316},{125,70,179,118},{72,70,124,119},{72,118,114,313},
			{64,310,113,375},{15,310,66,375},{16,247,56,310},{16,204,56,237}},
		{{40,115,106,175},{17,16,105,104},{104,15,282,51},{277,16,337,112},{164,71,275,112},{121,72,173,169},{174,123,288,168},{280,130,336,224},{168,184,280,224},
			{120,185,172,280},{172,240,288,281},{283,239,336,316},{282,308,336,368},{88,311,284,370},{16,280,86,368},{17,239,105,280},{40,209,105,240},{41,174,104,207}},
		{{56,71,136,104},{45,1,136,72},{0,0,47,74},{0,75,40,206},{0,204,48,255},{48,208,274,255},{273,203,320,255},{278,65,320,203},{267,1,320,69},{184,1,268,70},{184,72,264,147},
			{158,144,264,193},{56,141,137,194},{55,104,136,130}}			
								
	};
	public static final int sizesx[]={32,60,32,48,44,40};
	public static final int sizesy[]={24,32,60,48,48,32};
	
	public static final int ncheckps[]={9,11,26,23,18,14};

	UltraShip sh[];
	UltraRocket rk[];
	public byte TileMap[], itemx[], itemy[], itemt[];
	public int course, sizex, sizey, nracers, barsy, mess_cnt, ncheckp, rank[];	
	public int iaroute[][], rcheckp[][];
	public int ships[];
	Random rnd;
	String message;

	public UltraRace(int c, int pl)
	{
		InputStream is;
		int aux;
		boolean used;
		
		course=c%6; sizex=sizesx[course]; sizey=sizesy[course]; ncheckp=ncheckps[course];
		nracers=UltranausCanvas.N_RACERS;
		
		rcheckp=new int[ncheckps[course]][4];
		
		for(int i=0; i<ncheckps[course]; i++)
			//  Normal course
			if(c<6) rcheckp[i]=checkp[course][i];
			else    
			// Inverted course
				if (i<ncheckps[course]-1)
				rcheckp[i]=checkp[course][ncheckp-2-i];
				else rcheckp[i]=checkp[course][i];
		
		TileMap=new byte[sizex*sizey];
		rnd=new Random();
		
		//is=getClass().getResourceAsStream("/sc"+(course+1)+".map");
		readingdata.leerdata("/sc"+(course+1)+".map",TileMap,sizex*sizey);
		
		is=null;
		
		rank=new int[nracers]; for(int i=0; i<nracers; i++) rank[i]=i;
		
		iaroute=new int[ncheckp][2];
		
		for(int i=0; i<ncheckp; i++){
			iaroute[i][0]=(rcheckp[i][0]+rcheckp[i][2])/2;
			iaroute[i][1]=(rcheckp[i][1]+rcheckp[i][3])/2;
		}
		
		// Choose random opponents
		ships=new int[nracers];
		ships[0]=pl;
		for(int i=1; i<nracers; i++){			
			do{
				used=false;
				aux=Math.abs(rnd.nextInt())%9;
				for(int j=0; j<i; j++)
					if(ships[j]==aux) used=true;
			}while(used);
			
			ships[i]=aux;													
		}
						
		sh=new UltraShip[nracers];
		
		for(int i=0; i<nracers; i++)
			sh[i]=new UltraShip(nracers+1-i,ships[i],this);
			
		sh[0].player=true;		
			
		itemx=new byte[N_ITEMS];
		itemy=new byte[N_ITEMS];
		itemt=new byte[N_ITEMS];
					
		for(int i=0; i<N_ITEMS; i++)
			itemt[i]=0;
			
		rk=new UltraRocket[N_ROCKETS];
		
		for(int i=0; i<N_ROCKETS; i++)
			rk[i]=new UltraRocket();
			
		
		barsy=9;
			
	}
	
	public void post_message(String s)
	{
		message=s;
		mess_cnt=75; System.gc();
	}

	public void update(Control ctrl, int nplayers)
	{	
		int dx, dy, dist, mindist;
		
		if(nplayers==0) sh[0].player=false;
		
		if(mess_cnt>0) mess_cnt--;
		
		if(nplayers==1)
			sh[0].update(ctrl,this);
		for(int i=nplayers; i<nracers; i++)
			sh[i].ia_update(this);
			
		// Update opponent pointers
		for(int i=0; i<nracers; i++)
			sh[i].opp=null; 
		
		for(int i=0; i<nracers; i++){			
			mindist=9999;
			for(int j=0; j<nracers; j++)
			if(i<j){
						
				dx=Math.abs(sh[i].x-sh[j].x);
				dy=Math.abs(sh[i].y-sh[j].y);
				dist=(dx*dx)+(dy*dy);
				if((dist<mindist) && (dist<DIST_OPPONENT) && (sh[i].energy>0) && (sh[j].energy>0)){			
					mindist=dist;
					sh[i].opp=sh[j];	
					sh[j].opp=sh[i];	
				}			
			}
		}
		
		if((sh[0].opp!=null) && (barsy<16)) barsy+=1;
		if((sh[0].opp==null) && (barsy>7)) barsy-=1;
			
		// Collisions betwen ships
		for(int i=0; i<nracers; i++)
		for(int j=0; j<nracers; j++)
		if(i<j) sh[i].ship_collide(sh[j],this);	
		
		// Update items
		for(int i=0; i<N_ITEMS; i++)
			if(itemt[i]==0){
				
				do{
					itemx[i]=(byte)(2+Math.abs(rnd.nextInt())%(sizex-4));						
					itemy[i]=(byte)(2+Math.abs(rnd.nextInt())%(sizey-4));
										
				}while(TileMap[itemx[i]+(itemy[i]*sizex)]!=19);
							
				itemt[i]=(byte)(1+(Math.abs(rnd.nextInt())%4));
			}
		
		// Update rockets	
		for(int i=0; i<N_ROCKETS; i++)
			if(rk[i].in_use) rk[i].update(this);
			
		//update_rank();
					
	}
	
	public int begin_x(int i)
	{
		return iaroute[ncheckp-1][0]-5+12*(i%2);		
	}
	
	public int begin_y(int i)
	{
		return iaroute[ncheckp-1][1]-8+10*(i/2);		
	}
	
	public boolean collide(int x, int y)
	{
		int subt, subtx, subty;
				
		x=x>>13;
		y=y>>13;
				
		subtx=(((x>>10)%8)/4);
		subty=(((y>>10)%8)/4);
		subt=(subty*2)+subtx;
		
		if(x<0) x=0; if(y<0) y=0;
		if(x>=sizex) x=sizex-1; if(y>=sizey) y=sizey-1;
		
		
		switch(TileMap[(y*sizex)+x]){
				
			case 0 : if(subt!=3) return true; break;
			case 3 : if(subt!=2) return true; break;
			case 22 : return true; 
			case 32 : if(subt!=1) return true; break;
			case 49 : if(subt!=1) return true; break;
			case 52 : return true;			
			case 54 : if(subt!=2) return true; break;
			case 64 : if(subt!=3) return true; break;
			case 113 : return true;			
		}		
		return false;
	}
	
	public boolean inside_checkp(int x, int y, int i)
	{
		if((x>=rcheckp[i][0]) && (y>=rcheckp[i][1]) && (x<=rcheckp[i][2]) && (y<=rcheckp[i][3]))
		return true;
		else return false;		
	}
	
	public int optimal_orientation(UltraShip s)
	{	
		int or, or2, dif, frontx, fronty;
		boolean with_op=false;
		
		or=Trig.atan(s.y-iaroute[s.ianextcp][1],iaroute[s.ianextcp][0]-s.x);
		or2=or;
		
		frontx=s.realx+(Trig.cos(or2)<<3); fronty=s.realy-(Trig.sin(or2)<<3); 
		// Needed for check if ia orientation is ok
		
		// Collides with nearest oppenent ship
		//if(s.opp!=null)
		//with_op=s.opp.colliding_point(frontx,fronty);
		
					
		if((collide(frontx,fronty)) || (with_op)){
			
			//If the suggested orientation leads to a collision, correct it
			
			dif=or2-s.angle;
			if(dif>128) dif-=256; if(dif<-128) dif+=256; 

			if(dif>=0)
			do{
				or2-=8; if(or2<0) or2+=256;
				frontx=s.realx+(Trig.cos(or2)<<1); fronty=s.realy-(Trig.sin(or2)<<1); 
				
			}while(collide(frontx,fronty));
						
			if(dif<0)
			do{
				or2=(or2+8)%256; 
				frontx=s.realx+(Trig.cos(or2)<<1); fronty=s.realy-(Trig.sin(or2)<<1); 
				
			}while(collide(frontx,fronty));			
		}
						
						
		return or2;				
	}
	
	public void launch_rocket(UltraShip s)
	{
		int i=0;
		
		// Search for the first unused rocket slot
		while((i<N_ROCKETS) && (rk[i].in_use)) i++;	
				
		if(i<N_ROCKETS) rk[i].reset(s);		
	}
	
	public void update_rank()
	{
		int aux;		
		for(int j=0; j<nracers; j++)
		for(int i=0; i<nracers-1; i++){
			if(sh[rank[i+1]].wins(sh[rank[i]])){
				aux=rank[i];
				rank[i]=rank[i+1];
				rank[i+1]=aux;		
			}
			sh[rank[i]].rank=i;		
			sh[rank[i+1]].rank=i+1;					
		}				
	}
	
	public void destroy()
	{
		TileMap=null;		
		//rank=null;
		iaroute=null;
		for(int i=0; i<sh.length; i++) sh[i]=null;
		ships=null;
		sh=null;
		itemx=null;
		itemy=null;
		itemt=null;
		for(int i=0; i<rk.length; i++) rk[i]=null;
		rk=null;
		System.gc();
	}
}