package com.mygdx.mongojocs.sekhmet;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class unit
{
	protected Image foo;
	
	int numframes, width, height;
	int x, y, incx, incy, xf, yf, offx, offy;
	int cix,ciy,cfx,cfy;
	int tag = 0;
	long  timing = 0;
	//boolean out = false;
	boolean active = false;
	boolean visible = false;
	//int valia = 10;
	int ashape = 0;
  
  // public unit() {}
	
	public unit(Image i, int w, int h, int numf, int _offx, int _offy)
	{	
	   //foo = new Sprite(foopixels,0, w, h, foomask,0, numf);
	   foo = i;  
	   numframes = numf;
	   width = w*2;
	   height = h*2;	
	   offx = _offx*2;
	   offy = _offy*2;
	   //foo.setFrame(0);
	   setCollisionRectangle(0, 0, w*2, h*2); 
	   Activate();
	   //foo.setCollisionRectangle(0, 0, w, h); 
	   //setPosition(-w,-h);
	   //foo.setVisible(false);
	 
	}

   public void Auto(int _x, int _y, int _dx, int _dy)
   {
       x = _x;
       y = _y;
       xf = _x*100;
       yf = _y*100;       
       incx = _dx;
       incy = _dy;
       //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);
       //out = false;  
       ////visible = true;
       //foo.setVisible(true);
   }
   
   public void Deactivate()
   {
      visible = false;
      //foo.setVisible(false);
      active = false;
   }
   
        
   public void Activate()
   {
      timing = 0;
      visible=true;
      //foo.setVisible(true);
      //foo.setFrame(0);
      active = true;
      ashape=0;
   }        
        
	public void ActAuto()
	{
	   xf += incx;
	   yf += incy;
	   x = xf/100;
	   y = yf/100;
	   //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);
	   ////if (x < -width || y < -height || x > Game.largo || y > Game.alto)  {out = true;foo.setVisible(false);}
	   ////else foo.setVisible(true);
	}
	
	/*
	public void Update()
	{	   
	   timing++;
	}*/
	
	
	public void Update(int xx, int yy)
	{	   
	   ActAuto();	   
	   timing++;
	}
	
	
	
	public void setPosition(int foox, int fooy)
	{
	   x = foox;y = fooy;
	   //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);
	   
	}
	
	public void setFrame (int _frame)
	{
	   ashape = _frame;
	   //foo.setFrame(frame);
	}	
   
   public void Update(Prota prota)
   {
      }
   
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
      if (Game.offsetx+x < -width || Game.offsety+y < -height || Game.offsetx+x > Game.largo || Game.offsety+y > Game.hmap)  return;
      gr.setClip(0,0,Game.wmap,Game.hmap);	 
	   gr.clipRect(Game.offsetx+x,Game.offsety+y,width,height);	         	   
	   gr.drawImage(foo,Game.offsetx+x-offx, Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	         
   }

   public boolean isCollidingWith(unit choc)
   {
      if (choc.x + choc.cix > x+cix && choc.x + choc.cix < x+cfx && choc.y + choc.ciy > y+ciy && choc.y + choc.ciy < y+cfy) return true;
      if (choc.x + choc.cfx > x+cix && choc.x + choc.cfx < x+cfx && choc.y + choc.ciy > y+ciy && choc.y + choc.ciy < y+cfy) return true;
      if (choc.x + choc.cix > x+cix && choc.x + choc.cix < x+cfx && choc.y + choc.cfy > y+ciy && choc.y + choc.cfy < y+cfy) return true;
      if (choc.x + choc.cfx > x+cix && choc.x + choc.cfx < x+cfx && choc.y + choc.cfy > y+ciy && choc.y + choc.cfy < y+cfy) return true;
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