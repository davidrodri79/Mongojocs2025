
package com.mygdx.mongojocs.escape;


//import com.nokia.mid.ui.*;
import com.mygdx.mongojocs.midletemu.Graphics;


import java.util.Random;




public class EscapeNCDummy extends EscapeNCar {
	
	Control ctrl;
	int orix, oriy, mode, type, maxvel;
	
	public EscapeNCDummy(int ix, int iy, int mo)
	{
				
		orix=ix; oriy=iy; check_limits=false; check_collide=false; mode=mo;
												
		switch(mode){
			
			// My side horizontal dummy
			
			case 0 : sizex=30; sizey=15; break;		
			
			// Opposite side horizontal dummy
			
			case 1 : sizex=30; sizey=15; break;	
			
			// Vertical dummy
			
			case 2 : sizex=16; sizey=30; break;	
									
		}
				
		ctrl=new Control();
																				
	}
	
	public void reset(EscapeNScene sc)
	{
		set_pos(orix,oriy); velx=0; vely=0; velcx=0; velcy=0;	
		type=orix%4;
		maxvel=MAX_VEL_X/2-(sc.rnd.nextInt()%10000); 
		if(type==2) vely=maxvel;
		else velx=maxvel;
		place_index(sc);
	}

	public void show(EscapeNScene sc, Graphics g)
	{
			
		int f=0, fx=x-sc.scex-(sizex>>1), fy=y-sc.scey-(sizey>>1);
		int ix=0, iy=0;
				
		if(Math.abs(sc.scex+90-x)>100)
			return;		
		else{
					
			switch(mode){
				case 0 : ix=30; iy=99; break;
				case 1 : ix=0; iy=113; break;
				case 2 : ix=91; iy=129; break;
			};
						
			g.setClip(fx,fy,sizex,sizey);
			if(mode!=2) g.drawImage(SpriteImg,fx-ix,fy-iy-(sizey*type),20);			
			else g.drawImage(SpriteImg,fx-ix-(sizex*(type/2)),fy-iy-(sizey*(type%2)),20);			
				
			show_dust(sc,g);
		};			
	}	
	
	// Redefined method for vertical moving cars
	
	public void update(Control c, EscapeNScene sc)
	{
		timer++;
		
		oldx=realx; oldy=realy;
				
		if(mode==2){	
			
															
			// Vertical movement
			
			accy=0;
			if(c.up==true) accy=1500;
			if(c.down==true) accy=-3000;
			
			vely+=accy; 
			if(check_limits){		
				if(vely>MAX_VEL_X) vely=MAX_VEL_X; 
				if(vely<-MAX_VEL_X/4) vely=-MAX_VEL_X/4;
			}
			
			if(c.but1==true) vely=vely*8/10;			
													
			if(check_limits)
				if(realy<MIN_X) realy=MIN_X;
			
			// Horizontal movement
			
			accx=0;
			if(vely!=0){
				if(c.right==true) accx=9000;
				else if(c.left==true) accx=-9000;
			}
						
			velx+=accx; 
			if(check_limits)
				if(velx>MAX_VEL_Y) velx=MAX_VEL_Y; if(velx<-MAX_VEL_Y) velx=-MAX_VEL_Y;		
			
			if (accx==0) {
				if(velx>0) velx-=1500;
				else if (velx<0) velx+=1500;			
			}
			
			move(sc,(velx+velcx)/100,(vely+velcy)/100);
						
			// Velocity resulting of collisions		
			if(velcy!=0) velcy-=velcy/4;
			if(velcx!=0) velcx-=velcx/4;		
																																						
		}else super.update(c,sc);
										
	}	
	
	public void ia_update(EscapeNPlayer pl, EscapeNScene sc)
	{
				
					
		// Recycle
		switch(mode){
			case 1:
			case 0:
			
			if((x>sc.scex+220) || (x<sc.scex-50)){			
				type=(type+1)%4;
				if(x>sc.scex+200) realx-=30000; 
				else realx+=30000;
				x=realx/100;		
				maxvel=MAX_VEL_X/2-(sc.rnd.nextInt()%10000);				
				place_index(sc);												
			}
			break;
			
			case 2:
			if(realy>MAX_Y+3000){
				type=(type+1)%4;
				realy-=10000; y=realy/100;				
				maxvel=MAX_VEL_X/2-(sc.rnd.nextInt()%10000);
				place_index(sc);
			}
			break;					
		}
					
		ctrl.reset();
		
		//Simply advance
		
		if(timer%4==0)
		switch(mode){
			
			case 0:		
			if(velx<maxvel) ctrl.right=true;
			if(velx>maxvel) ctrl.left=true;	
			if(y<oriy-5) ctrl.down=true;
			if(y>oriy+5) ctrl.up=true;	
			break;
			
			case 1:		
			if(velx<-maxvel) ctrl.right=true;
			if(velx>-maxvel) ctrl.left=true;	
			if(y<oriy-5) ctrl.down=true;
			if(y>oriy+5) ctrl.up=true;	
			break;
			
			case 2:
			if(x<orix-5) ctrl.right=true;
			if(x>orix+5) ctrl.left=true;
			if(vely<maxvel) ctrl.up=true;
			if(vely>maxvel) ctrl.down=true;
			break;
		}
			
		update(ctrl,sc);										
	}
}