package com.mygdx.mongojocs.frozenworld;


import com.mygdx.mongojocs.midletemu.Image;

public class malo2 extends unit
{
    int addx = 2;
    int rebotes;
    
    malo2(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
	setCollisionRectangle(4,6,12,14);
    }
    
    
    public void Update(unit prota)
    {   
	    timing++;
	    
	    //GRAFICOS
	desp = (timing>>1)%2;
	if (addx > 0) ashape = 0;
	else ashape = 1; 		
	    
        int cx = addx + x;
        int cy = y;
        int addy=0;
             
	  //ESTOY EN EL AIRE???               
		if (Test(x+cix, y+cfy+2, gc.LST_AIR) &&
		     Test(x+cfx-1, y+cfy+2, gc.LST_AIR))
		{            
			    grounded = false;
			    y += 3;
			    return;
		}
		else 
		{
			//TOCO EL SUELO
			y = (((y+cfy+2) / TILED) * TILED)-cfy;
			grounded = true;		
		}                          
       /*//ESTOY EN EL AIRE???               
        if (Test(x+cix, y+cfy, gc.LST_AIR) &&
            Test(x+cfx-1, y+cfy, gc.LST_AIR))
        {
            grounded = false;
            y += 3;
            return;
        }
        else {y = (y/TILED)*TILED;grounded = true;}                     
          */     
               
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
            rebotes++;
            if (rebotes > 6) {Deactivate();rebotes = 0;}
        }    
	
    }
    
}