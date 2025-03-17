package com.mygdx.mongojocs.frozenworld;

//FLECHA


import com.mygdx.mongojocs.midletemu.Image;

public class malo5 extends unit
{
    int liveTim;
    boolean moving;
    
    malo5(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
	setCollisionRectangle(6,6,8,8);
    }
    
    
    public void Update(unit prota)
    {   
	    if (!active) return;
/*	    if ((-gc.sc.ScrollX+x < -width ||  -gc.sc.ScrollX+x >= gc.canvasWidth ||		     
		   -gc.sc.ScrollY+y < -height || -gc.sc.ScrollY+y >= gc.canvasHeight) && gc.ga.nfc%2 == 0) return;*/ 
	    timing++;
	
	    int cx = incx + x;
	    int cy = y;
	    moving = false;   
	       
	    //if (Test(cx+(width>>1), y+8, gc.LST_ICE)) 	Set(cx+(width>>1), y+8, gc.BEGDESTROY);
	    if (TestCol(cx+cix+incx, cx+cfx-1+incx,cy+ciy, cy+cfy-1, gc.LST_AIR))
	    {
		       moving = true;
		       x += incx;
		       liveTim = timing;
	    }
	    else    
	    {
		    if (timing -2 <= liveTim) x += incx;		
		    if (timing > liveTim+15) active = false;
	    }
        
		//GRAFICS
		desp = (timing>>1)%4;
		if (incx > 0) ashape = 0;
		else ashape = 1;
		if (timing > liveTim)    desp = 2;
		
    }
    
    
    	public boolean canKill(unit prota)
	{
		if ((isCollidingWith(prota) || prota.isCollidingWith(this)) && moving) return true;
		return false;
	} 
    
}
