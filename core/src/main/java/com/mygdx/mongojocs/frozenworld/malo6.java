package com.mygdx.mongojocs.frozenworld;

//MOCO

import com.mygdx.mongojocs.midletemu.Image;

public class malo6 extends unit
{
    int moving;
    int maxmoving = 10;
    boolean bloqueado;
    
    malo6(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
	setCollisionRectangle(2,2,8, 8);
	incy = 1;
    }
    
    
    public void Update(unit prota)
    {   
	    if (!active) return;
	    timing++;	    
	    
	    desp = (timing)%4;
	    x += incx;
	    y += incy;
	    
	    if (timing%2 == 0) moving--;   
	    
	    int cx = x + ((cfx - cix)/2) ;
	    int cy = y + ((cfy - ciy)/2);
	    
	    test:
	    {
		    
		    if (incy > 0)
		{
		    if (!Test(cx+1 ,  cy, gc.LST_AIR))
		    {
			    incx = 1;
			    incy = 0;
			    moving+=2;
			    break test;
		    }
		    if (Test(x-8 ,  cy, gc.LST_AIR))
		    {
			    incx = -1;
			    incy = 0;
			    moving+=2;
			    break test;
		    }
		
		}	    
		if (incy < 0)
		{
		    if (!Test(cx-1 ,  cy, gc.LST_AIR))
		    {
			    incx = -1;
			    incy = 0;
			    moving+=2;
			    break test;
		    }
		    if (Test(x+8 ,  cy, gc.LST_AIR))
		    {
			    incx = 1;
			    incy = 0;
			    moving+=2;
			    break test;
		    }
		}	    	    		    
	    if (incx < 0)
	    {
		     if (!Test(cx ,  cy+1, gc.LST_AIR))
		    {
			    incx = 0;
			    incy = 1;
			    //return;
			    moving+=2;
			    break test;			    
		    }
		    if (Test(cx ,  y-8, gc.LST_AIR))
		    {
			    incx = 0;
			    incy = -1;
			    moving+=2;
			     break test;
		    }		    		
	    }
	    if (incx > 0)
	    {
		    if (!Test(cx ,  cy-1, gc.LST_AIR))
		    {
			    incx = 0;
			    incy = -1;
			    moving+=2;
			    break test;
		    }
		    if (Test(cx ,  y+8, gc.LST_AIR))
		    {
			    incx = 0;
			    incy = 1;
			    moving+=2;
			    break test;
		    }
	    }
	    
		
	        
	
	    }
	    
	    
	    if (moving > maxmoving)
	    {
		    if (Test(cx, y+ciy, gc.LST_ICE)) 	{Set(cx, y+ciy, gc.BEGDESTROY);}
	    	    if (Test(cx, y+cfy, gc.LST_ICE)) 	{Set(cx, y+cfy, gc.BEGDESTROY);}		    
	    }
	    if ( incx < 0 && prota.y > y && Math.abs(prota.x - x) < unit.TILED && Math.abs(prota.y - y) < unit.TILED*3) y += 1;
	    if (Test(cx, cy, gc.LST_AIR)
		    && Test(cx+2, cy-2, gc.LST_AIR)
	    		&& Test(cx-2, cy-2, gc.LST_AIR)
			&& Test(cx+2, cy+2, gc.LST_AIR)
			&& Test(cx-2, cy+2, gc.LST_AIR)
			)
		     	//if (incy < 4) incy++;
		{incy = 2;
		incx = 0;
		//moving = 0;//OLD
		if (moving > 0) moving = 0;//NEW
		y = ((y>>1)<<1)+1;
		bloqueado = false;
		}
	    else bloqueado = true;
	    
	    
    }
    
    
    
}
