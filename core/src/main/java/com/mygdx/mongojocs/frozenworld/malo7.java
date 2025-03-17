package com.mygdx.mongojocs.frozenworld;

import com.mygdx.mongojocs.midletemu.Image;



//YETI

public class malo7 extends unit
{
    int addx = 1;
    int heading;
    int rebotado = 0;
    int timrebote = 0;
    
    malo7(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
	    setCollisionRectangle(4,6,12,14);	
    }
    
    
    boolean teveo;
    int antestag;
    
    public void Update(unit prota)
    {   
	    if ((-gc.sc.ScrollX+x < -width ||  -gc.sc.ScrollX+x >= gc.canvasWidth ||		     
		   -gc.sc.ScrollY+y < -height || -gc.sc.ScrollY+y >= gc.canvasHeight) && gc.ga.nfc%2 == 1) return; 
		
	    timing++;
	    
		teveo = false;	    
		
	    if (timing > 1 &&  tag != antestag) {if (prota.x < x) addx = -1;else addx = 1;}
	    antestag = tag;
	    int cx = addx + x;	    	    
	    if (prota.y+prota.cfy == y+cfy && (addx < 0 && prota.x < x || addx > 0 && prota.x > x)) {teveo = true;cx +=addx;}
	    int cy = y;
	    int addy=0;
             
	  //ESTOY EN EL AIRE???               
		if (Test(x+cix, y+cfy+2, gc.LST_AIR) &&
		     Test(x+cfx-1, y+cfy+2, gc.LST_AIR))
		{            
			    grounded = false;
			    y += 3;			    
			    desp = 5;
			    return;
		}
		else 
		{
			//TOCO EL SUELO
			y = (((y+cfy+2) / TILED) * TILED)-cfy;
			grounded = true;		
		}                          
               
	       
	    
	    if (heading != 0)
	    {		    
		    	desp = 4;
		    	if (Math.abs(heading) > 8 ) desp = 5;
			if (Math.abs(heading) == 14)
			{
				if (heading < 0) Set(cx+cix+addx, y+8, gc.BEGDESTROY);
				if (heading > 0) Set(cx+cfx+addx, y+8, gc.BEGDESTROY);
				heading = 0;				  
			}			
			if (heading > 0) heading++;
			if (heading < 0) heading--;
	    }
	    else
	    {
		    			boolean ok = false;
				    if (addx < 0 && !Test(cx+cix+addx, y+cfy+8, gc.LST_AIR)) ok = true;
				    if (addx > 0 && !Test(cx+cfx+addx, y+cfy +8, gc.LST_AIR)) ok = true; 
			    
				if (TestCol(cx+cix+addx, cx+cfx-1+addx,cy+ciy, cy+cfy-1, gc.LST_AIR) && ok)
				{				
					x += addx;if (timrebote+4 <= timing)rebotado = 0;
					if (teveo) x+=addx;
					 
				}            
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
				desp =  ((timing>>1)%4);
				
	    }
	    
	if (addx > 0) ashape = 0;
	else ashape = 1;
	if (rebotado > 6 && heading == 0) 
	{
		desp = 4+((timing>>1)%2);
		if (prota.x > x) ashape = 0;
		else ashape = 1;
	}
	 		
    }
    
}
