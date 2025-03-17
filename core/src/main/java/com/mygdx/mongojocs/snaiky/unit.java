package com.mygdx.mongojocs.snaiky;


import com.mygdx.mongojocs.iapplicationemu.Graphics;
import com.mygdx.mongojocs.iapplicationemu.Image;

class unit
{
	
	int numframes, width, height;
	int x, y, incx, incy, xf, yf, offx, offy;
	int cix,ciy,cfx,cfy;
	int tag;
	int timing;
	boolean active;
	int ashape;
	int tipo;
	int estado;
	int desp;    
    byte  acor[], map[];
    int sidex, sidey;
    boolean grounded;
    final int TILED = 5;
    final int MAPW = 31;
	Image foo[];
	int AIR[] = {0, 12};
	int TRASH[] = {12, 13, 14 , 15};
	int BONUS = 16;
	int rub;
	final int MOV = 5;
	
	public unit(Image _i[], int w, int h, int numf, int _offx, int _offy, byte[] cor)
	{	
	    foo = _i;
	    numframes = numf;
    	width = w;
    	height = h;	
    	offx = _offx;
    	offy = _offy;
    	acor = cor;	   
    	setCollisionRectangle(0,0,w,h); 
    	Deactivate();	 
    	  
	}

   public void Auto(int _x, int _y, int _dx, int _dy)
   {
   	//Activate();
       x = _x;
       y = _y;
       xf = _x*100;
       yf = _y*100;       
       incx = _dx;
       incy = _dy;       
       //out = false;  
       //visible = true;       
   }
   
   public void Deactivate()
   {
      //visible = false;
      active = false;
      timing = 0;
   }
   
   public void Erase()
    {
         for(int i=(x-TILED)/TILED; i <= (x+width)/TILED;i++)
            for(int j=(y-TILED)/TILED; j <= (y+height)/TILED;j++)
                Game.backmap[(j*MAPW)+ i] = -1;
    
    }
    
   
   
    public void Set(int xx, int yy, int tile)
    {
        int p = xx / TILED;
        p += (yy / TILED) * MAPW;
        
        map[p] = (byte)tile;        
    }   
   
   
    public boolean Test(int xx, int yy, int tile)
    {
        int p = xx / TILED;
        p += (yy / TILED) * MAPW;
        
        int next = map[p];
        return next == tile;        
    }


    public boolean Test(int xx, int yy, int[] lst)
    {
        boolean any = false;
        for(int i = 0;i < lst.length;i++)
            if (Test(xx, yy, (int)lst[i])) return true;
        return false;    
    }


   
   
    public boolean TestCol(int _xi, int _xf, int _yi, int _yf, int tile[])
    {
        if (Test(_xi, _yi, tile) &&
            Test(_xi, _yf, tile) &&
            Test(_xf, _yi, tile) &&
            Test(_xf, _yf, tile))                                  
            return true;
        return false;    
    }
   
    /*public boolean TestCol(int _xi, int _xf, int _yi, int _yf, int tile)
    {
        if (Test(_xi, _yi, tile) &&
            Test(_xi, _yf, tile) &&
            Test(_xf, _yi, tile) &&
            Test(_xf, _yf, tile))                                  
            return true;
        return false;    
    }*/
   
   
   
        
    public void Activate(byte[] _map, int _x , int _y)
    {
        map = _map;        
        timing = 0;
        //visible=true;
        active = true;
        ashape=0;
        //out = false;
        estado = 0;
        x = _x;
        y = _y;        
        incx = 0;incy = 0;
    }        
        
        
	public void ActAuto()
	{
	   xf += incx;
	   yf += incy;
	   x = xf/100;
	   y = yf/100;
	   //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);
	   //if (x < -width || y < -height || x > Game.largo || y > Game.alto)  {out = true;}
	   ////else foo.setVisible(true);
	}
	
	
	public void Update(unit prota)
	{	   
	   timing++;
	   ActAuto();	 	        
	}
	
	
	
   public void blit(Graphics gr)
   {
      if (!active) return;
      //if (Game.offsetx+x > Game.cw || Game.offsety+y > Game.ch || Game.offsetx+x < -width || Game.offsety+y < -height) return;

        //gr.setColor(0x00ff00);
        //gr.fillRect(x,y,width,height);      
      if (numframes > 0)
         Game.PutSprite(foo,  x,y, ((offy+ashape)*numframes)+desp+offx, acor, 1);
      /*else
      { 
            //gr.setClip(0,0,gc.canvasWidth, gc.canvasHeight);
            //gr.clipRect(-gc.sc.ScrollX+x, -gc.sc.ScrollY+y, width, height);
            
      } */     
      Erase();
   }

   public boolean isCollidingWith(unit choc)
   {
   	    if (choc.x + choc.cix >= x+cix && choc.x + choc.cix <= x+cfx && choc.y + choc.ciy >= y+ciy && choc.y + choc.ciy <= y+cfy) return true;
        if (choc.x + choc.cfx >= x+cix && choc.x + choc.cfx <= x+cfx && choc.y + choc.ciy >= y+ciy && choc.y + choc.ciy <= y+cfy) return true;
        if (choc.x + choc.cix >= x+cix && choc.x + choc.cix <= x+cfx && choc.y + choc.cfy >= y+ciy && choc.y + choc.cfy <= y+cfy) return true;
        if (choc.x + choc.cfx >= x+cix && choc.x + choc.cfx <= x+cfx && choc.y + choc.cfy >= y+ciy && choc.y + choc.cfy <= y+cfy) return true;      
        return false;
   }


   public void setCollisionRectangle(int ox,int oy,int lx,int ly)
   {
      cix = ox;
      ciy = oy;
      cfx = ox+lx;
      cfy = oy+ly;
   }

	
	


}