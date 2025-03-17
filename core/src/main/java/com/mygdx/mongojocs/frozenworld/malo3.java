package com.mygdx.mongojocs.frozenworld;
//ARA�A

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class malo3 extends unit
{
    int addy = 2;
    int iniy;
    int moving;
    final int PACIENCIA = 50;
    
    malo3(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
	    setCollisionRectangle(2,6,16,10);	
    }
    
    
    public void Update(unit prota)
    {   
	    if (!active) return;
	    timing++;	    
	    int cx = x;
	    int cy = addy + y;
	       
	    if (moving > PACIENCIA)
	    {		    
		    if (Test(cx+(width>>1), y+ciy, gc.LST_ICE)) 	Set(cx+(width>>1), y+ciy, gc.BEGDESTROY);
		    if (Test(cx+(width>>1), y+cfy, gc.LST_ICE)) 	Set(cx+(width>>1), y+cfy, gc.BEGDESTROY);
		    moving = 0;
	    }
	    if (TestCol(cx+cix, cx+cfx-1,cy+ciy+addy, cy+cfy+addy, gc.LST_AIR))
	    {
		       y += addy;
		       moving = 0;
	    }
	    else 
	    {
		    moving++;		
		    if (moving <= 2)
		    {
			    if (addy < 0) 
			    {
				    if (Test(cx+(width>>1),cy-8 , gc.LST_ICE))
					    Set(cx+(width>>1), cy-8, gc.BEGDESTROY);
				    iniy = Math.min(y, iniy);	    
			    }
			    else 
			    {
				    if (Test(cx+(width>>1), cy+cfy+8, gc.LST_ICE))
					    Set(cx+(width>>1), cy+cfy+8, gc.BEGDESTROY);
			    }
		    }
		    addy = -addy;    
        }
	//GRAFICS
	desp = (timing>>1)%3;
	if (addy > 0) ashape = 1;
	else ashape = 0;
    }
    
    
    public void Activate(byte[] _map, int _x , int _y)
    {
        map = _map;        
        timing = 0;
        active = true;
        ashape=0;
        estado = 0;
        x = _x;
        y = _y;   
	xf = x*100;
	yf = y*100;
	iniy = y;     
	tag = 1;
    }        
    
    
    
    	public boolean canKill(unit prota)
	{
		if ((isCollidingWith(prota) || prota.isCollidingWith(this)) && moving == 0) return true;
		return false;
	} 
    
    
     public void blit(Graphics gr)
     {		
	     if (!active) return;      
	     gc.PutSprite(foo,  -gc.sc.ScrollX+gc.scale(x), -gc.sc.ScrollY+gc.scale(y), ((offy+ashape)*numframes)+desp+offx, acor, gc.NUMTROZOS);
	     gr.setColor(0x6bcef7);
	     gr.setClip(0,0,gc.canvasWidth, gc.canvasHeight);
	     gr.drawLine(-gc.sc.ScrollX+gc.scale(x+9)+1, Math.min(gc.canvasHeight, -gc.sc.ScrollY+gc.scale(y)), -gc.sc.ScrollX+gc.scale(x+9)+1, Math.max(-gc.sc.ScrollY+gc.scale(iniy), 0));      
   }
    
}
