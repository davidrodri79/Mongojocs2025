package com.mygdx.mongojocs.garbage;


import com.mygdx.mongojocs.iapplicationemu.Image;

public class malo extends unit
{
   

    
    malo(Image i[], int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super( i, w, h, numf, _offx, _offy, cor);
        incx = MOV;incy = MOV;
    }
    
    
    
    int db;

    int estado;
    int lastx, lasty;
    int protx, proty;
    int adesp;
    
    
    public void Update(unit prota)
    {                        
        if (!active) return;
        timing++;            
        
        if (prota.x < x) protx = -1*Game.difficulty;
        else protx = 1*Game.difficulty;
        if (prota.y < y) proty = -1*Game.difficulty;
        else proty = 1*Game.difficulty;
        if (Game.killer > 0) {protx = -protx; proty = -proty;}       
            
        if (tipo == 1)
        {
            if (timing%50 == 0) estado = 1-estado;
        }
        
        lastx = incx;
        lasty = incy;
        
        if (isCollidingWith(prota) || prota.isCollidingWith(this))
        {
            if (Game.killer > 0) 
            {
                tag-= 5 ;                
            }
            else
            {
                tag -=5;
                prota.tag-=10;                
            }
            if (tag <= 0) {Deactivate();Game.joc.Sonido(2, 1, true);}
            if (prota.incx != incx) incx = -incx;
            if (prota.incy != incy) incy = -incy;
        }
        
        
        
        
        if (y%5 == 0 && TestCol(x+ cix+incx, x+cfx-1+incx, y+ciy, y+cfy-1, AIR)) x += incx;
        else incx = 0;
        if (x%5 == 0 && TestCol(x+ cix, x+cfx-1, y+ciy+incy, y+cfy-1+incy, AIR)) y += incy;
        else incy = 0;    
        
        if (incx != 0)
        {
            if (TestCol(x+ cix, x+cfx-1, y+ciy+MOV, y+cfy-1+MOV, AIR) && Game.rnd.nextInt()%16 < 0+proty)
            {
                incx = 0;
                incy = MOV;
            }            
            else if (TestCol(x+ cix, x+cfx-1, y+ciy-MOV, y+cfy-1-MOV, AIR) && Game.rnd.nextInt()%16 < 0-proty)
            {
                incx = 0;
                incy = -MOV;
            }
        }        
        else if (incy != 0)
        {
            if (TestCol(x+ cix+MOV, x+cfx-1+MOV, y+ciy, y+cfy-1, AIR) && Game.rnd.nextInt()%16 < 0+protx)
            {                
                incx = MOV;
                incy = 0;
            }            
            if (TestCol(x+ cix-MOV, x+cfx-1-MOV, y+ciy, y+cfy-1, AIR) && Game.rnd.nextInt()%16 < 0-protx)
            {
                incx = -MOV;
                incy = 0;
                
            }
        }
        
        
        if (incx == 0 && incy == 0)
        {           
            int b = Math.abs(Game.rnd.nextInt()%32);
            int a = b;//+Math.abs(lastx*2)-Math.abs(lasty*2);
            if (a < 16)
            {
                if (b % 16 < 8) incx = MOV;
                else incx = -MOV;
            }  
            else
            {
                if (b % 16 < 8) incy = MOV;
                else incy = -MOV;
            }
        }
        
        if (tipo == 1)
        {        
            if (estado == 1)
            {
                 if (rub > 0)
                 {
                    for(int i=x/5; i < (x+width)/5;i++)
                        for(int j=y/5; j < (y+height)/5;j++)     
                        if (Test(i*5,j*5, 0) && Game.rnd.nextInt() > 0) {Set(i*5,j*5, 12+Math.abs(Game.rnd.nextInt()%4));rub--;Game.nblocks++;}
                  }
            }
            else  
            {
                 for(int i=x/5; i < (x+width)/5;i++)
                    for(int j=y/5; j < (y+height)/5;j++)     
                        if (Test(i*5,j*5, TRASH) && Game.rnd.nextInt() > 0) {Set(i*5,j*5, 0);rub++;Game.nblocks--;}
            }
        }
        
        
      
        
        desp = adesp;
        db = 1 - db;
        if (incx > 0) desp = 4;
        else if (incx < 0) desp = 6;
        if (incy > 0) desp = 0;
        else if (incy < 0) desp = 2;
        
        desp = (desp/2)*2;
        desp += db;
        adesp = desp;
        
        if (Game.killer > 0) 
        {
            if (Game.killer > 20 || timing%2 == 0)
                desp += 8;                    
        }    
    }
    
    
    

}