package com.mygdx.mongojocs.frozenworld;

//HOMBRE DEL HIELO

import com.mygdx.mongojocs.midletemu.Image;

public class malo4 extends unit
{
    int addx = 1;
    boolean morire;
    int heading;
    int rebotado = 0;
    int timrebote = 0;
    
    malo4(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
	setCollisionRectangle(4,6,12,14);
	morire = false;
    }
    
    
    public void Update(unit prota)
    {   
	     if ((-gc.sc.ScrollX+x < -width ||  -gc.sc.ScrollX+x >= gc.canvasWidth ||		     
		   -gc.sc.ScrollY+y < -height || -gc.sc.ScrollY+y >= gc.canvasHeight) && gc.ga.nfc%2 == 0) return; 
		     
	      
	    timing++;	    		
	    
        int cx = addx + x;
        int cy = y;
        int addy=0;
             
	  //ESTOY EN EL AIRE???               
		if (Test(x+cix, y+cfy+2, gc.LST_AIR) &&
		     Test(x+cfx-1, y+cfy+2, gc.LST_AIR))
		{            
			    grounded = false;
			    y += 3;
			    morire  = true;
			    desp = 0;
			    return;
		}
		else 
		{
			//TOCO EL SUELO
			y = (((y+cfy+2) / TILED) * TILED)-cfy;
			grounded = true;		
		}                          
               
	       if (morire) {Deactivate();}
	    
	    if (heading != 0)
	    {
		    
		    	desp = 5;
		    	if (Math.abs(heading) > 4 ) desp = 6;
			if (Math.abs(heading) == 8)
			{
				if (heading < 0 && Test(cx+cix+addx, y+8, gc.LST_ICE)) Set(cx+cix+addx, y+8, gc.BEGDESTROY);
				if (heading > 0 && Test(cx+cfx+addx, y+8, gc.LST_ICE)) Set(cx+cfx+addx, y+8, gc.BEGDESTROY);
				heading = 0;				  
			}			
			if (heading > 0) heading++;
			if (heading < 0) heading--;
	    }
	    else
	    {
		    			boolean ok = false;
					
					//ICE NO ES; ES !air!"!!!!!!!!
				    if (addx < 0 && !Test(cx+cix+addx, y+cfy+8, gc.LST_AIR)) ok = true;
				    if (addx > 0 && !Test(cx+cfx+addx, y+cfy +8, gc.LST_AIR)) ok = true; 
			    
				if (TestCol(cx+cix+addx, cx+cfx-1+addx,cy+ciy, cy+cfy-1, gc.LST_AIR) && ok)
				{x += addx;if (timrebote+4 <= timing)rebotado = 0;}            
				else 
				{
				    if (addx < 0) 
				    {
					if (Test(cx+cix+addx, y+8, gc.LST_ICE)) heading = -1;
					    //Set(cx+cix+addx, y+8, gc.BEGDESTROY);
				    }
				    else 
				    {
					if (Test(cx+cfx+addx, y+8, gc.LST_ICE)) heading = 1;
					    //Set(cx+cfx+addx, y+8, gc.BEGDESTROY);
				    }
				    if (heading == 0) 
				    {
					    addx = -addx;
					    if (timrebote+4 > timing) rebotado++;	
					    timrebote = timing;
				    }   
				}    
				//GRAFICOS
				desp = 1 + ((timing>>1)%4);
				//if (morire) desp = 0;
	    }
	    
	    if (heading == 0 && Math.abs(prota.y - y) < 12)
	    {
		    if (prota.x < x && Math.abs(prota.x - x) < 12) heading = -1;
		    if (prota.x > x && Math.abs(prota.x - x) < 12) heading = 1;
	    }
	if (rebotado > 6 && heading == 0) desp = 5;
	if (addx > 0) ashape = 0;
	else ashape = 1; 		
    }
    
}
