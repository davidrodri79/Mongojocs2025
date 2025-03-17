package com.mygdx.mongojocs.garbage;

import com.mygdx.mongojocs.iapplicationemu.Image;

public class prota extends unit
{
   
   
    
    prota(Image i[], int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super( i, w, h, numf, _offx, _offy, cor);
    }
    
    
    
 
    int db = 0;
   
    public boolean Free(int x1, int y1)
    {
        if (Game.killer > 0) return true;
        x += x1;
        y += y1;
        boolean res = true;
        for(int i = 0; i < Game.malo.length;i++)
        {
            if (Game.malo[i].active && (isCollidingWith(Game.malo[i]) || Game.malo[i].isCollidingWith(this))) res = false;
        }
        x -= x1;
        y -= y1;        
        return res;
    }
    
    
    public boolean Move(int dx, int dy)
    {    
        if (Game.killer > 0) Game.killer--;
        if (dx != 0) incx = dx*MOV;
        if (dy != 0) incy = dy*MOV;
        
        //int cx = x+incx;
        //int cy = y+incy;
        
                    
            if (y%5 == 0 && TestCol(x+ cix+incx, x+cfx-1+incx, y+ciy, y+cfy-1, AIR) && Free(incx, 0)) x += incx;
            else incx = 0;
            if (x%5 == 0 && TestCol(x+ cix, x+cfx-1, y+ciy+incy, y+cfy-1+incy, AIR) && Free(0, incy)) y += incy;            
            else incy = 0;
        
        
        for(int i=x/5; i < (x+width)/5;i++)
            for(int j=y/5; j < (y+height)/5;j++)     
            {
                if (Test(i*5,j*5, TRASH)) {Set(i*5,j*5, 0);Game.nblocks--;}
                if (Test(i*5,j*5, BONUS)) {Set(i*5,j*5, 0);Game.killer = 70;Game.joc.Sonido(4, 1, false);}
            }
       
        db = 1 - db;
        if (incx > 0) desp = 4;
        else if (incx < 0) desp = 6;
        if (incy > 0) desp = 0;
        else if (incy < 0) desp = 2;
        
        desp = (desp/2)*2;
        desp += db;
        //System.out.println(""+desp);
                        /*if (TestCol(x+cix, x+cfx-1, y+ciy-incy, y+cfy-1-incy, 0))
        {
            y += incx;
        } */   
   
        return true;                
    }
    
        
    
    public void myPos()
    {
    
    
    }
   
}