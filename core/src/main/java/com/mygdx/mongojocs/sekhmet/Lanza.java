package com.mygdx.mongojocs.sekhmet;

import com.mygdx.mongojocs.midletemu.Image;

class Lanza extends unit
{
   final static boolean IZQUIERDA = false;
   final static boolean DERECHA = true;
   public boolean lado;
   
	public Lanza(Image i, int w, int h, int numf)
	{	
	   super(i, w, h, numf,50,158);	 
	   setCollisionRectangle(0,0,w,h/2);  
	}

   public boolean ColLaterales()
   {
      if ((lado && Game.Colisiona(x+width,y, this,false) == 1)
      || (!lado && Game.Colisiona(x,y, this,false) == 1))
	   {  
	      //Auto(x,y,0,0);
	      y = (((y+height+(2*2))/16)*16)-height+(1*2);
	      ashape=2;return true;
	   }
	   return false;
	}	   	
   	
   	
   public void Update()
   {
      if (active)
	   {	     
	      timing++;    
	      int l = 1;
	      if (lado == IZQUIERDA) l = -1;
	      if (!ColLaterales())
	      {
	         ActAuto();
	         if (timing == 4) {Auto(x,y, l*1000,0);ashape=ashape+l;}
	         if (timing == 9) {Auto(x,y+(1*2), l*1000,80);ashape=ashape+l;}
	         if (timing == 12) {Auto(x,y, l*840,160);}
	         if (timing == 14) {Auto(x,y+(1*2), l*760,240);ashape=ashape+l;}
	         if (timing == 17) {Auto(x,y, l*640,360);}
	         //if (timing>=25) {Deactivate();}
	      }
	   }
  }
   
   /*
   public void blit(Graphics gr)
   {
      if (!visible) return;
      gr.setClip(Game.offsetx+x,Game.offsety+y,width,height);	         
      gr.drawImage(foo,-50+Game.offsetx+x,-158+Game.offsety+y - ashape*height, gr.TOP|gr.LEFT);	         
	}*/
}