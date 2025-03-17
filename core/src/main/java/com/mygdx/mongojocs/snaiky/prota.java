package com.mygdx.mongojocs.snaiky;

import com.mygdx.mongojocs.iapplicationemu.Graphics;
import com.mygdx.mongojocs.iapplicationemu.Image;

public class prota extends unit
{
   
   
    
    prota(Image i[], int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super( i, w, h, numf, _offx, _offy, cor); 
    }
    
    ring cola;
    ring cabeza;
 
    public void init()
    {
        cabeza = new ring(null,null,x/TILED, y/TILED);
        cola = cabeza;
        longitud = 1;
        
        for(int i = 0; i < 8;i++)        
        {
            x += TILED;            
            Cap(x/TILED, y/TILED);
            //longitud++;
        }
        incx = 0;incy = 0;  
        desp = 3;
    }
 
 
    public void Cap(int _x, int _y)
    {
        ring nuevo = new ring(null, cabeza, _x, _y);
        cabeza.prev = nuevo;
        cabeza = nuevo;
        Set(_x*TILED,_y*TILED,  16);
    }
 
    public void Cua()
    {
        cola = cola.prev;
        Set(cola.x*TILED, cola.y*TILED, 0);
    }
 
 
    int longitud;
    int score;
 
    int db = 0;
   
  
    
    
    public boolean Move(int dx, int dy)
    {    
        if (Game.killer > 0) Game.killer--;
        if (dx != 0 && incx == 0) {
                                    if (dx == -1 && incx == 0 && incy == 0) ;
                                    else
                                    {incx = dx*MOV;incy = 0;}}
        if (dy != 0 && incy == 0) {incy = dy*MOV;incx = 0;}
        
        //int cx = x+incx;
        //int cy = y+incy;
        
        
        
        if (y%TILED == 0 && TestCol(x+ cix+incx, x+cfx-1+incx, y+ciy, y+cfy-1, AIR)) x += incx;
        else if (incx != 0){incx = 0;tag = -1;}
        if (x%TILED == 0 && TestCol(x+ cix, x+cfx-1, y+ciy+incy, y+cfy-1+incy, AIR)) y += incy;            
        else if (incy != 0){incy = 0;tag = -1;}
        
        if(incx != 0 || incy != 0)
        for(int i=x/TILED; i < (x+width)/TILED;i++)
            for(int j=y/TILED; j < (y+height)/TILED;j++)     
            {
                if (!Test(i*TILED,j*TILED, 12)) Cua();
                else {longitud++;Game.joc.newItem();score += longitud*10;
                Game.joc.Sonido(3,1,true);}
                Cap(i,j);                
            }
               
        if (incx > 0) desp = 3;
        else if (incx < 0) desp = 1;
        if (incy > 0) desp = 0;
        else if (incy < 0) desp = 2 ;
               
        return true;                
    }
    
        
    
   public void blit(Graphics gr)
   {
      if (!active) return;      
      if (numframes > 0) Game.PutSprite(foo,  x-2, y-2, ((offy+ashape)*numframes)+desp+offx, acor, 1);         
      Erase();      
   }
   
}