package com.mygdx.mongojocs.sekhmet;

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;

public class MMenu
{

	int cvar = 40+170;
   static int opmenu = 0;
   
	public  MMenu()//Graphics _gr)
	{
	
	}


	public int show(Graphics gr, int currentkey, boolean apretando, int w, int h)
	{
		int estado = Game.estado;
		int prestado =  Game.prestado;//Game.getPrestado();
		gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
		BlitFondo(gr,w,h);
      gr.setClip(0,0,w,h);		     			
		switch( estado)
   	{
           case 3: 
                  if (cvar > -158) cvar--;                                        
               	//gr.setClip(0,0,w,h);
               	for (int i = 0 ;i < Lang.credits.length;i++)
                  {
                  	if ((i> 2 && i%3 == 1) || i == 1)Game.Print(255,200,0, Lang.credits[i],20, cvar+14*i, Graphics.LEFT);
                  	else Game.Print(205,180,100, Lang.credits[i],20, cvar+14*i, Graphics.LEFT);
                  	//if ((i> 2 && i%3 == 1) || i == 1)
                  	/*
                  	if ((i> 2 && i%3 == 1) || i == 1) {gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD , Font.SIZE_MEDIUM));gr.setColor(145,32,0);	}
                  	else {gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_MEDIUM));	 gr.setColor(80,0,0);   }
                     if ((cvar+14*i>-14) && (cvar+14*i < h)) 
                        if (Lang.credits[i]!="") gr.drawString(Lang.credits[i],5, cvar+14*i, Graphics.LEFT | Graphics.TOP);	               	              	               */
                  }
                  if (apretando && (currentkey == Canvas.FIRE || currentkey == FullCanvas.KEY_SOFTKEY1 || currentkey==FullCanvas.KEY_SOFTKEY2)) { estado = 4;cvar=40+170;Game.currentkey=0;}
                  break;   
           case 4: 
           			//BlitFondo(gr,w,h);
                  gr.setClip(0,0,w,h);
                  gr.drawImage(Game.portada,w/2, 0, Graphics.TOP | Graphics.HCENTER);	 
                  if (Sekhmet.sfx) Lang.lll[0][2]= Lang.soundon;
                  else Lang.lll[0][2]= Lang.soundoff;
                  Lang.lll[1][2] = Lang.lll[0][2];
                  for (int i = 0;i<Lang.lll[prestado].length;i++)
                  {
                  	if (opmenu == i) Game.Print(255,200,0, Lang.lll[prestado][i], w/2, 108+i*16, Graphics.HCENTER);
                  	else Game.Print(200,150,0, Lang.lll[prestado][i], w/2, 108+i*16, Graphics.HCENTER);
                  	/*
                  	gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_MEDIUM));	                  
                  	String c;
                     if (opmenu == i) {c = "> ";gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD , Font.SIZE_MEDIUM));	                  }
                     else c = "  ";
                     gr.drawString(c+Lang.lll[prestado][i], w/2, 98+i*15, Graphics.HCENTER | Graphics.TOP);	               	              	   */
                  }
                  if (apretando)
                  {
                  	Game.cheat();
                  
                  	if (currentkey==Canvas.UP) if (opmenu > 0) {opmenu--;}//Game.currentkey=0;}
	                  if (currentkey==Canvas.DOWN) 
	                  	if (opmenu < Lang.lll[prestado].length-1) {opmenu++;}//Game.currentkey=0;}
	                  if (currentkey == Canvas.KEY_POUND && prestado==1){ estado=3;}//currentkey = 0;}      	
	                  if (currentkey==Canvas.FIRE || currentkey== FullCanvas.KEY_SOFTKEY2)
	                  {                     
	                     if (Lang.lll[prestado][opmenu] == "Jugar") 
	                     {  estado = 2;
	                  		Game.Print(255,200,0, Lang.loading, w/2, 180, Graphics.HCENTER);   
	               			/*gr.setColor(247,214,132);gr.fillRect(20, 45, w-40, 90);         
                  			gr.setColor(145,32,0);gr.drawRect(20, 45, w-40, 90);    
                  			gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD , Font.SIZE_MEDIUM));
                           gr.drawString(Lang.loading , 42, 85, Graphics.LEFT | Graphics.TOP);	    	*/
	                     }                                                           
	                     if (Lang.lll[prestado][opmenu] == "Volver") {estado = prestado;gr.setClip(0,0,w,h);
         					gr.setColor(0,0,0);
         					gr.fillRect(0,0,w,h);} 
	                     if (Lang.lll[prestado][opmenu] == "Ir a Men�") {estado = 1;Game.prestado=1;} 
	                     if (Lang.lll[prestado][opmenu] == "Salir") {estado=6;  }
	                     if (Lang.lll[prestado][opmenu] == "Ayuda") { estado=5;}                                                           
	                     if (Lang.lll[prestado][opmenu] == "Sonido en on") { Sekhmet.sfx=false; /*Sekhmet.bp[0].stop();*/}
	                     if (Lang.lll[prestado][opmenu] == "Sonido en off") { Sekhmet.sfx=true;}                                                           
	                  }
	                  Game.currentkey=0;Game.apretando=false;	                  	               
                  }
                  break;
                  
               case 5:
                  //gr.setColor(247,214,132);gr.fillRect(0, 0, w, h);
                  gr.setColor(145,32,0);
                  for (int i = 0 ;i < Lang.controls.length;i++)
                  	Game.Print(255,200,0, Lang.controls[i], w/2, 8+i*16, Graphics.HCENTER);
                     //gr.drawString(Lang.controls[i],20, 55+15*i, Graphics.LEFT | Graphics.TOP);	               	              	                                  
                  if (apretando && currentkey != 0) 
                  { 
                  	estado = 3;
                  	if (prestado == 0) estado = 4;
                  	Game.currentkey=0;Game.apretando=false;
                  }  
                  break;  
               case -1:               
                  estado = 4;
                  /*
						gr.setColor(247,214,132);gr.fillRect(20, 60, w-40, 70);         
                  gr.setColor(145,32,0);gr.drawRect(20, 60, w-40, 70);    
                  gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));              
                  for (int i = 0;i<Lang.lll[2].length;i++)
                  {
                  	gr.drawString(Lang.lll[2][i], 26, 68+i*14, Graphics.LEFT | Graphics.TOP);	               	              	   
                  	gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
                  }
                  if (apretando)
                  {
                  	if (currentkey == Canvas.KEY_STAR) estado = 4;  
                  	if (currentkey == FullCanvas.KEY_SOFTKEY1 ) estado = 4;                    	
                     if (currentkey == Canvas.KEY_POUND) estado=3;
                     
                     Game.currentkey=0;Game.apretando=false;
                  }*/
                  break;
   			}    
         return  estado;     
			
	}
	
	  int vx1,vx2,vx3,vx4,vx5 = 0;
	
	public void BlitFondo(Graphics gr, int w, int h)
	{
		gr.setClip(0, 0, w, h);                      
			gr.drawImage(Game.piram,0, -11, Graphics.LEFT | Graphics.TOP);	     											
			vx1--;
			if (vx1 <= -w) vx1 = 0;
			int vx11 = vx1+w;
			if (vx11 > w) vx11 = vx11-w;
			vx2-=2;
			if (vx2 <= -w) vx2 = 0;
			int vx22 = vx2+w;
			if (vx22 > w) vx22 = vx22-w;
			vx3-=3;
			if (vx3 <= -w) vx3 = 0;
			int vx33 = vx3+w;
			if (vx33 > w) vx33 = vx33-w;
			vx4-=5;
			if (vx4 <= -w) vx4 = 0;
			int vx44 = vx4+w;
			if (vx44 > w) vx44 = vx44-w;
			vx5-=8;
			if (vx5 <= -w) vx5 = 0;
			int vx55 = vx5+w;
			if (vx55 > w) vx55 = vx55-w;
			
			gr.setClip(0,90,w,31);
	   	gr.drawImage(Game.piram,vx1, 90-85, Graphics.LEFT | Graphics.TOP);	     			
	   	gr.drawImage(Game.piram,vx11, 90-85, Graphics.LEFT | Graphics.TOP);	     			
			
			
			gr.setClip(0,70,w,40);
	   	gr.drawImage(Game.piram,vx2, 70, Graphics.LEFT | Graphics.TOP);	     			
	      gr.drawImage(Game.piram,vx22, 70, Graphics.LEFT | Graphics.TOP);	     
	      
			gr.setClip(0,50,w,31);
	   	gr.drawImage(Game.piram,vx3, 50-85, Graphics.LEFT | Graphics.TOP);	     			
	   	gr.drawImage(Game.piram,vx33, 50-85, Graphics.LEFT | Graphics.TOP);	     			
			
			gr.setClip(0,22,w,45);
	   	gr.drawImage(Game.piram,vx4, 22-40, Graphics.LEFT | Graphics.TOP);	     			
	   	gr.drawImage(Game.piram,vx44, 22-40, Graphics.LEFT | Graphics.TOP);	     			
	   	
		   gr.setClip(0,0,w,40);
	   	gr.drawImage(Game.piram,vx5, 0, Graphics.LEFT | Graphics.TOP);	     			
	      gr.drawImage(Game.piram,vx55, 0, Graphics.LEFT | Graphics.TOP);	     			
		}
	


	
   

	
}