package com.mygdx.mongojocs.frozenworld;

import com.mygdx.mongojocs.midletemu.Image;

public class malo1 extends unit
{
    int addx = 2;
    
    
    malo1(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
	setCollisionRectangle(2,6,14,14);
    }
    
    
    public void Update(unit prota)
    {   
	    if ((-gc.sc.ScrollX+x < -width ||  -gc.sc.ScrollX+x >= gc.canvasWidth ||		     
		   -gc.sc.ScrollY+y < -height || -gc.sc.ScrollY+y >= gc.canvasHeight) && gc.ga.nfc%2 == 0) return; 
		
	    timing++;
	    int cx = addx + x;
	    int cy = y;
	    int addy=0;
               
	    if (Test(cx+(width>>1), y+8, gc.LST_ICE)) 	Set(cx+(width>>1), y+8, gc.BEGDESTROY);
	    if (TestCol(cx+cix+addx, cx+cfx-1+addx,cy+ciy, cy+cfy-1, gc.LST_AIR))
		       x += addx;            
	    else 
	    {
		    if (addx < 0) 
		    {
			    if (Test(cx+cix+addx, y+8, gc.LST_ICE))
				    Set(cx+cix+addx, y+8, gc.BEGDESTROY);
		    }
		    else 
		    {
			    if (Test(cx+cfx+addx, y+8, gc.LST_ICE))
				    Set(cx+cfx+addx, y+8, gc.BEGDESTROY);
		    }
		    addx = -addx;    
        }
	//GRAFICS
	desp = (timing>>1)%5;
	if (addx > 0) ashape = 1;
	else ashape = 0;
    }
    
    
}