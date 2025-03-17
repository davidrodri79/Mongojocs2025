
package com.mygdx.mongojocs.escape;


//import com.nokia.mid.ui.*;

import com.mygdx.mongojocs.midletemu.Graphics;

import java.util.Random;




public class EscapeNMWDummy extends EscapeNCar {
	
	Control ctrl;
	int orix, oriy, type, maxvel;
		
	public EscapeNMWDummy(int ix, int iy)
	{
				
		orix=ix; oriy=iy; check_limits=false; check_collide=false;
								
		sizex=30; sizey=15; 
				
		ctrl=new Control();
																		
	}
	
	public void reset(EscapeNScene sc)
	{
		set_pos(orix,oriy); velx=0; vely=0; velcx=0; velcy=0;	
		type=Math.abs(sc.rnd.nextInt())%5;
		maxvel=MAX_VEL_X-15000-(sc.rnd.nextInt()%30000); velx=maxvel;
		
		place_index(sc);
	}

	public void show(EscapeNScene sc, Graphics g)
	{
		
		int f=0, fx=x-sc.scex-(sizex>>1), fy=y-sc.scey-(sizey>>1);
						
		if(Math.abs(sc.scex+90-x)>100);
			
		else{			
			g.setClip(fx,fy,sizex,sizey);
			g.drawImage(SpriteImg,fx-30,fy-99-(15*type),20);			
		}				
	}	
	
	public void ia_update(EscapeNPlayer pl, EscapeNScene sc)
	{
				
		//Simply advance
		
				
		// Recycle
		if((x<sc.scex-50) || (x>sc.scex+210)){
			
				type=(type+1)%5;
				if(x>sc.scex+150) realx-=30000; 
				else realx+=30000;
				x=realx/100;		
				realy=3000+Math.abs(sc.rnd.nextInt())%4000;
				if(realx/100>sc.dummy_limitx2) realy=12000;
				maxvel=MAX_VEL_X-15000-(Math.abs(sc.rnd.nextInt())%30000);				
				place_index(sc);			
			}
					
		ctrl.reset();
		
	
		if((x<sc.dummy_limitx2) && (timer%4==0)){
			if(velx<maxvel) ctrl.right=true;
			if(velx>maxvel) ctrl.left=true;
		
			if(x>sc.dummy_limitx1) ctrl.down=true;
		}
			
		update(ctrl,sc);						
				
	}
}