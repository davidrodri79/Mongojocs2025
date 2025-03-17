package com.mygdx.mongojocs.kinsectors;


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

class unit
{
	protected Image foo;
	
	int numframes, width, height;
	int x, y, incx, incy, xf, yf, offx, offy;
	int cix,ciy,cfx,cfy;
	int tag;
	int  timing = 0;
	boolean out;
	boolean active;
	boolean visible;
	int valia;
	int ashape = 0;
	int tipo;
	int estado;
	int desp = 0;
  
  // public unit() {}
	
	public unit(Image i, int w, int h, int numf, int _offx, int _offy, int tip)
	{	
	   foo = i;  
	   numframes = numf;
	   width = w;
	   height = h;	
	   offx = _offx;
	   offy = _offy;
	   tipo = tip;
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
      visible = false;
      active = false;
   }
   
        
   public void Activate()
   {
      timing = 0;
      visible=true;
      active = true;
      ashape=0;
      out = false;
      estado = 0;
      ActivateSub();      
   }        
   
   public void ActivateSub()
   {   /*
		switch(tipo)
	    {
	   	case 1: tag = 80;valia=200;break;
	   	case 2: tag = 2;valia=10;timing = Game.rnd.nextInt()%128;break;
	   	case 5: tag = 200;valia=1000;break;
	    } */         
   } 
        
	public void ActAuto()
	{
	   xf += incx;
	   yf += incy;
	   x = xf/100;
	   y = yf/100;
	   //foo.setPosition(-Game.offsetx+x, -Game.offsety+y);
	   if (x < -width || y < -height || x > Game.largo || y > 144)  {out = true;}
	   ////else foo.setVisible(true);
	}
	
	public void Mata(){}
	
	public void Update()
	{
		timing++;		
	}
	
	public void Update(int xx, int yy, boolean b)
	{	   
	   timing++;
	   if (b) ActAuto();
	   /*
	   switch(tipo)
	   {
	      case 1: 	estado--;
	               if (estado < 0) 
	               {ActAuto();	   	   
	               if (x < 30) {Auto(x,y,150,-200);estado = 100;}
	      		  	if (y < 0) {Auto(x,y,50,150);estado = 100;}
	      		  	if (y > 70) {Auto(x,y,-200,-100);estado = 100;}
	      			}	
	      		   if (timing %32 == 8) Game.Dispara(x+50,y+32);
	      		   if (timing %32 == 24) Game.Dispara(x+50,y-20);
	              break;
	   	case 2: ActAuto();	   	   
	   	        y += Trig.sin((int)timing<<2)>>7;
	   	        if (timing % 20 == 0 && Game.rnd.nextInt()%16 > 8) Game.Dispara(x,y);
	   	        ashape = timing%2;
	   	        if (x < -20) Deactivate();
	   	        break;
	      case 5: if (x > 26 || timing > 500) ActAuto();	 
	              if (timing == 500) Auto(x,y,134,0);     
	              if (timing >= 500 && x > Game.largo) Deactivate();     
	              break;
	      default: ActAuto();	 	        
	   }*/
	}
	
	
	
	public void setPosition(int foox, int fooy)
	{
	   x = foox;y = fooy;	   	   
	}
	/*
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
      if (!visible) return;
      Game.blit(desp+offx,  ashape*height + offy, width, height, x, y,foo);
      //gr.setClip(0, 0,Game.largo, Game.alto); 
	   //gr.clipRect(x,Game.offsety+y,width,height);	         
	   //gr.drawImage(foo,-desp+x-offx, Game.offsety+y - ashape*height - offy, gr.TOP|gr.LEFT);	         
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