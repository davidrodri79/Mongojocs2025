
package com.mygdx.mongojocs.escape;


//import com.nokia.mid.ui.*;
import com.mygdx.mongojocs.midletemu.Graphics;





public class EscapeNPlayer extends EscapeNCar {
	

	
	public EscapeNPlayer()
	{
			
		sizex=30; sizey=22; orix=-30; oriy=40; velx=MAX_VEL_X/2;
		allow_vibr=true;													
	}
	
	public void reset(EscapeNScene sc)
	{
		set_pos(orix,oriy); velx=MAX_VEL_X/2; vely=0; velcx=0; velcy=0;	
		energy=MAX_ENERGY;
		place_index(sc);
	}
	
	public void show(EscapeNScene sc, Graphics g)
	{
		int f=0, fx=x-sc.scex-(sizex>>1), fy=y-sc.scey-(sizey>>1);
								
		if(Math.abs(sc.scex+90-x)>100)
			return;
		else{
		
			switch(pos){			
				case -2 : f=3; break;
				case -1 : f=4; break;
				case 0  : f=0; break;
				case 1  : f=1; break;
				case 2  : f=2; break;			
			};
			
			g.setClip(fx,fy,sizex,sizey);
			g.drawImage(SpriteImg,fx,fy+2-(sizey*f),20);
									
			show_dust(sc,g);
					
		};			
	}	
}