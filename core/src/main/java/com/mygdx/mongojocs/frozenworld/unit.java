package com.mygdx.mongojocs.frozenworld;


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class unit
{
    static GameCanvas gc;
	Image foo;
	
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
    byte  acor[];
    static byte map[];
    int sidex, sidey;
    boolean grounded;
    final static int TILED = 16;
    
	public unit(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
	{	
	    gc = _gc;
	    foo = i;  
	    numframes = numf;
	    width = w;
	    height = h;	
	    offx = _offx;
	    offy = _offy;
	    acor = cor;	   
	    setCollisionRectangle(5,3, w-10, h-3); 	
	    //Deactivate();	     	  
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
      int p = (x+(width>>1)) / TILED;
      p += ((y+(height>>1)) / TILED)* gc.MAPW;
      int current = map[p];
      if (gc.inList(gc.AIR, current)) map[p] = (byte)gc.BEGDESTROY;
   }
   
   
    public static void SetAir(int xx, int yy)
    {
        xx = xx / TILED;
        yy = (yy / TILED);
        int p = xx;
        p += yy * gc.MAPW;
        
        map[p] =  gc.back[(xx%8)+((yy%8)*8)];
	
	//(byte)((16+xx%2)+(8*(yy%2)));        
    }
   
    public void Set(int xx, int yy, int tile)
    {
        int p = xx / TILED;
        p += (yy / TILED) * gc.MAPW;
        
        map[p] = (byte)tile;        
    }   
   
   
    public boolean Test(int xx, int yy, int tile)
    {
        int p = xx / TILED;
        p += (yy / TILED) * gc.MAPW;
        
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
	xf = x*100;
	yf = y*100;
	tag = 1;
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
	
	public boolean canKill(unit prota)
	{
		if (isCollidingWith(prota) || prota.isCollidingWith(this)) return true;
		return false;
	}
	
   public void blit(Graphics gr)
   {
      if (!active) return;
      //if (Game.offsetx+x > Game.cw || Game.offsety+y > Game.ch || Game.offsetx+x < -width || Game.offsety+y < -height) return;
      int tx = x;int ty = y;
      //x = (x*3)/2;y = (y*3)/2;
      if (numframes > 0)
         gc.PutSprite(foo,  -gc.sc.ScrollX+gc.scale(x), -gc.sc.ScrollY+gc.scale(y), ((offy+ashape)*numframes)+desp+offx, acor, gc.NUMTROZOS);
      else
      { 
            //gr.setClip(0,0,gc.canvasWidth, gc.canvasHeight);
            //gr.clipRect(-gc.sc.ScrollX+x, -gc.sc.ScrollY+y, width, height);
            gc.showImage(foo, offx, offy, width, height, -gc.sc.ScrollX+x, -gc.sc.ScrollY+y);
      }
      x = tx;y = ty;
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