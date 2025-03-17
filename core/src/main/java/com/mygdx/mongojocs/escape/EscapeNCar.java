package com.mygdx.mongojocs.escape;


//import com.nokia.mid.ui.*;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;


import java.io.InputStream;



abstract class EscapeNCar {
	
	
	protected static int MIN_X=2000;
	protected static int MIN_Y=1600;
	protected static int MAX_Y=8000;
	protected static int MAX_VEL_X=80000;
	protected static int MAX_VEL_Y=21000;
	 
	static public Image SpriteImg;
	
	public static final int MAX_ENERGY = 619;
	public int energy;	
		
	public int x, y, realx, realy, orix, oriy, velx=0, vely=0, velcx=0, velcy=0, accx=0, accy=0,
			 pos=0, sizex, sizey, oldx, oldy, timer, cx1, cx2, cy1, cy2,
			 map_index=0;
			 
	protected boolean check_limits=true, check_collide=true, allow_vibr=false;
	public static boolean vibr_on=false;
	
	public static void load_sprites()
	{
				
		// Load the sprites only once		
		try {
			SpriteImg = new Image();
			SpriteImg._createImage("/Cars.png");
			
			
		}catch(Exception err) {}
		
		//SpriteImg = Image.createImage(8,8);
	}
	
	public void place_index(EscapeNScene sc)
	{
		map_index=0;	
		
		while((x>=sc.SegmentMapX[map_index+1]) && (map_index<sc.nsegs-1)) map_index++;
	}
	
	abstract void reset(EscapeNScene sc);
	
	abstract void show(EscapeNScene sc, Graphics g);
		
	public void set_pos(int xx, int yy)
	{
		realx=xx*100; realy=yy*100; x=xx; y=yy;			
	}
	
	public void recal_col_rect()
	{
		cx1=realx+200-100*(sizex>>1); cy1=realy+200-100*(sizey>>1); 
		cx2=realx-200+100*(sizex>>1); cy2=realy-200+100*(sizey>>1);		
	}
	
	public void update(Control c, EscapeNScene sc)
	{
		int ct;
		
		if(Math.abs(x-sc.pl.x)>250) return;
		
		timer++;
		
		//modify_energy(timer%2);
														
		// Horizontal movement
		
		accx=0;
		if(c.right==true) accx=1500;
		if(c.left==true) accx=-3000;
		
		velx+=accx; 
		if(check_limits){		
			if(velx>MAX_VEL_X) velx=MAX_VEL_X; 
			if(velx<-MAX_VEL_X>>2) velx=-MAX_VEL_X>>2;
		}
		
		if(c.but1==true) velx=velx*8/10;
		
		if(check_collide)
			ct=sc.check_ground(x,y,map_index);
		else ct=0;
		if(((ct==1) || (ct==3)) && (velx>=MAX_VEL_X>>2)) velx-=3000;
		if(ct==3) modify_energy(-30);
						
						
		// Vertical movement
		
		accy=0;
		if(velx!=0){
			if(c.down==true) accy=9000;
			else if(c.up==true) accy=-9000;
		}
					
		vely+=accy; 
		if(check_limits)
			if(vely>MAX_VEL_Y) vely=MAX_VEL_Y; if(vely<-MAX_VEL_Y) vely=-MAX_VEL_Y;		
		
		if((ct==1) && (vely<=-MAX_VEL_X>>2)) vely+=3000;
		if((ct==1) && (vely>=MAX_VEL_X>>2)) vely-=3000;
				
		if (accy==0) {
			if(vely>0) vely-=1500;
			else if (vely<0) vely+=1500;			
		};
						
		// Actually move														
		move(sc,(velx+velcx)/100,(vely+velcy)/100);
		
		// Velocity resulting of collisions		
		if(velcy!=0) velcy-=velcy/4;
		if(velcx!=0) velcx-=velcx/4;		
																									
		// Position of the car			
				
		if(vely==-MAX_VEL_Y) pos=-2;
		else if(vely<-MAX_VEL_Y>>1) pos=-1;
		else if(vely==MAX_VEL_Y) pos=2;
		else if(vely>MAX_VEL_Y>>1) pos=1;
		else pos=0;
		
		if(velx<0) pos=-pos;
															
	}
	
	protected boolean move(EscapeNScene sc, int ix, int iy)
	{
		int nx, ny, col=0;
		
		// new position

		nx=realx+ix; 
		ny=realy+iy;
		
		if(check_limits){
			if(nx<MIN_X) nx=MIN_X;
			if(ny<MIN_Y) ny=MIN_Y; if(ny>MAX_Y) ny=MAX_Y; 
		}
				
		//Check if new position is posible		
		if(check_collide)
		if(obstacle_collide(sc,nx/100,y))
		     {col=1; velcx=-velx; velx=0;}
		else if(obstacle_collide(sc,x,ny/100))
		     {col=1; velcy=-vely; vely=0;}
		else if(obstacle_collide(sc,nx/100,ny/100))
		     {col=2; velcx=-velx; velx=0; velcy=-vely; vely=0;}
		
		if((col==0) || (!check_limits)){
								
			realx=nx; realy=ny;
		
			x=realx/100; y=realy/100;				
			
			recal_col_rect();
			
			// Update index to segment map
			if((x<sc.SegmentMapX[map_index]) && (map_index>0)) map_index--;
			if((x>=sc.SegmentMapX[map_index+1]) && (map_index<sc.nsegs-1)) map_index++;
		
			return true;
			
		}else{
/*			try{
				if((allow_vibr) && (vibr_on)) DeviceControl.startVibra(50,10);
			} catch(java.lang.Exception e) {}*/
			
			modify_energy(-(Math.abs(velcx)+Math.abs(velcy))/(col*1000));
			return false;
		}	
	}
		
	public void car_collide(EscapeNScene sc, EscapeNCar op)
	{
		int dx=0, dy=0, temp, mov;
		
		if(Math.abs(x-op.x)>50) return;
		
		if(intersec(op)){
			
			if(Math.abs(realx-op.realx)>Math.abs(realy-op.realy))
			{
				if(realx<op.realx) dx=cx2-op.cx1; else dx=-(op.cx2-cx1);
			}else{
				if(realy<op.realy) dy=cy2-op.cy1; else dy=-(op.cy2-cy1);
			}
			
			// Horizontal collide
			if(dx>dy){
								
				velcx=op.velx>>1; 
				op.velcx=velx>>1; 				
												
			// vertical collide
			}else if(dx<dy){
							
				velcy=op.vely>>1; 
				op.velcy=vely>>1; 
				
			}else{
				velcx=op.velx>>1; op.velcx=velx>>1; 			
				velcy=op.vely>>1; op.velcy=vely>>1; 											
			}
			
			//if(allow_vibr) System.out.println("Collide : I("+velcx+","+velcy+"), Other("+op.velcx+","+op.velcy);
			
			// Avoid one car inside another!
																				
			move(sc,-dx>>1,0); op.move(sc,dx>>1,0);				
			move(sc,0,-dy>>1); op.move(sc,0,dy>>1);
											
			modify_energy(-5);
			op.modify_energy(-5);
			
			/*try{
				if((allow_vibr) && (vibr_on)) DeviceControl.startVibra(50,10);			
			} catch(java.lang.Exception e) {}*/
							
		}		
	}
	private boolean intersec(EscapeNCar op)
	{
		if((cx1<op.cx2) && (cx2>op.cx1) && (cy1<op.cy2) && (cy2>op.cy1))
		return true;
		else return false;
	}				
					
	public boolean obstacle_collide(EscapeNScene sc, int nx, int ny)
	{
				
		int x1, x2, y1, y2, i1, i2, j1, j2;
		
		x1=nx-((sizex>>1)-4); x2=nx+((sizex>>1)-4);
		y1=ny-((sizey>>1)-6); y2=ny+((sizey>>1)-6);											
		
		i1=x1>>3; i2=x2>>3;
		j1=y1>>3; j2=y2>>3;
		
		// Find the nearest colliding tile, if exists
		for(int j=j1; j<=j2; j++)
		for(int i=i1; i<=i2; i++)
			if(sc.check_tile(i,j,map_index)==2) return true;
			
		return false;
	}
	
	protected void modify_energy(int m)
	{
		energy+=m;
		
		if(energy>MAX_ENERGY) energy=MAX_ENERGY;
		if(energy<0) energy=0;		
	}
	
	protected void show_dust(EscapeNScene sc, Graphics g)
	{
		int fx, fy, f;
		
		if(velx>=0) {fx=x-25-sc.scex; fy=y-5-sc.scey;}
		else	    {fx=x+10-sc.scex; fy=y-5-sc.scey;}
		f=timer%3;
				
		if((sc.check_ground(x,y,map_index)==1) && ((velx!=0) || (vely!=0))){
			
			g.setClip(fx,fy,10,10);
			g.drawImage(SpriteImg,fx-125,fy-154-(10*f),20);						
		}		
	}
}