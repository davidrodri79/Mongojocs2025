
package com.mygdx.mongojocs.escape;


//import com.nokia.mid.ui.*;
import com.mygdx.mongojocs.midletemu.Graphics;



public class EscapeNEnemy extends EscapeNCar {
	
	Control ctrl;
	int type;
	
	public EscapeNEnemy(int ix, int iy, int t)
	{
		
		orix=ix; oriy=iy; timer=0; type=t;
		
		switch(type){
						
			case 0 : sizex=30; sizey=18; break;
			case 1 : sizex=30; sizey=24; break;
			case 2 : sizex=28; sizey=18; break;
		};
								
		ctrl=new Control();
										
	}
	
	public void reset(EscapeNScene sc)
	{
		set_pos(orix,oriy); velx=0; vely=0; velcx=0; velcy=0; place_index(sc);
	}

	public void show(EscapeNScene sc, Graphics g)
	{
		int f=0, fx=x-sc.scex-(sizex>>1), fy=y-sc.scey-(sizey>>1);
		int ix=0, iy=0;
				
		if(Math.abs(sc.scex+90-x)>100)
			return;		
		else{
			if(type==1)
				switch(pos){			
					case -2 : f=3; break;
					case -1 : f=4; break;
					case 0  : f=0; break;
					case 1  : f=1; break;
					case 2  : f=2; break;			
			}else			
				switch(pos){			
					case -2 : f=1; break;
					case -1 : f=2; break;
					case 0  : f=0; break;
					case 1  : f=3; break;
					case 2  : f=4; break;			
			};
			
			switch(type){
				case 0 : ix=31; iy=0; break;
				case 1 : ix=93; iy=0; break;
				case 2 : ix=62; iy=0; break;
			};
			
			if((timer%8>=4) && (type==2)) f+=5;
			
			g.setClip(fx,fy,sizex,sizey);
			g.drawImage(SpriteImg,fx-ix,fy-iy-(sizey*f),20);			
				
			show_dust(sc,g);
		};
		
	}	
	
	public void ia_update(EscapeNCar pl, EscapeNScene sc)
	{
				
		//Pursue the player
		
		if(Math.abs(orix-pl.x)>1250) return;
		
		ctrl.reset();
		
		if(Math.abs(pl.velx)+Math.abs(pl.vely)>7500){
		
			if(pl.x>x) ctrl.right=true;		
			if(pl.x<x-20) 
				if(pl.velx>0) ctrl.but1=true;
				else ctrl.left=true;
			
			if((Math.abs(pl.x-x)<20) && (pl.velcy<10))
				if(pl.y>y) ctrl.down=true;
				else if(pl.y<y) ctrl.up=true;
		}
			
		update(ctrl,sc);						
				
	}
}