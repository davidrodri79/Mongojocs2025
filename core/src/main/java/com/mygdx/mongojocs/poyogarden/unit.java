package com.mygdx.mongojocs.poyogarden;


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class unit
{
	Image foo;
	
	int numframes, width, height;
	int x, y, incx, incy, xf, yf, offx, offy;
	int cix,ciy,cfx,cfy;
	int tag;
	//int energy = 1;
	int timing;
	//boolean out;
	boolean active;
	//boolean visible;
	//int valia;
	int ashape;
	int tipo;
	int estado;
	//int nido;
	int desp;
   int freeze;
   int muerto;
   byte  acor[];
   
	
	public unit(Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
	{	
	   foo = i;  
	   numframes = numf;
	   width = w;
	   height = h;	
	   offx = _offx;
	   offy = _offy;
	   //tipo = tip;
	   acor = cor;
	   setCollisionRectangle(0, 0, w, h); 
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
      freeze = 0;
      muerto = 0;
      timing = 0;
   }
   
        
   public void Activate()
   {
      timing = 0;
      //visible=true;
      active = true;
      ashape=0;
      //out = false;
      estado = 0;
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
	
	
	/*
	public void setPosition(int foox, int fooy)
	{
	   x = foox;y = fooy;	   	   
	}
	
	public void setFrame (int _frame)
	{
	   ashape = _frame;
	   //foo.setFrame(frame);
	}	
   
   public void Update(Prota prota)
   {
      }
   */
   /*
   public void blit(Graphics gr, int _x, int _y)
   {
      x = _x; y = _y;
      gr.setClip(_x,_y,width+1,height+1);	         
	   gr.drawImage(foo,_x,_y - ashape*height, gr.TOP|gr.LEFT);	         
   }*/

/*
   public void blit(Graphics gr)
   {
      if (!visible) return;
      gr.setClip(Game.offsetx+x,Game.offsety+y,width,height);	         
	   gr.drawImage(foo,Game.offsetx+x,Game.offsety+y - ashape*height, gr.TOP|gr.LEFT);	         
   }
*/

	public void blit(Graphics gr)
   {
      if (!active) return;
      if (Game.offsetx+x > Game.cw || Game.offsety+y > Game.ch || Game.offsetx+x < -width || Game.offsety+y < -height) return;
      if (freeze > 0 && timing%2 == 0) return;

      if (numframes != 5)
         Game.PutSprite(foo,  Game.offsetx+x,Game.offsety+y, ((offy+ashape)*numframes)+desp+offx, acor,3);
      else
      {
           Game.PutSprite(foo,  Game.offsetx+x,Game.offsety+y, ((offy+ashape)*numframes)+desp+offx, acor,1);
      }
      //if (Game.offsetx+x < -width || Game.offsety+y < -height || Game.offsetx+x > Game.largo || Game.offsety+y > Game.alto)  return;
      	/*gr.setClip(0, 0,Game.largo, Game.alto); 
	   	gr.clipRect(Game.offsetx+x,Game.offsety+y,width,height);	         
	   //gr.drawImage(foo,x-offx, -Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	         
	   	gr.drawImage(foo,-(desp*width)+Game.offsetx+x-offx, Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	         
	   }*/
	   Game.PostBlit(x, y);
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