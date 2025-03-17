package com.mygdx.mongojocs.kinsectors;


import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;

public class MMenu
{

	Graphics gr;
   int cvar = 40;
   static int opmenu = 0;
   
	public  MMenu()//Graphics _gr)
	{		
	}

	int timing = 0;
	int ff = 0;
	int ppp = 0;
	int credits = 0;
	
	public int show(Graphics gr, int currentkey, boolean apretando, int w, int h)
	{
		
	   
		Game.offsety = 0;
		int estado = Game.estado;
		int prestado =  Game.prestado;//Game.getPrestado();
		Game.offsety = 0;
	
		if (timing % 2 == 0) Game.actCon();
      if (timing % 40 == 0 && estado != 9) Game.addMsg(Lang.credits[(++credits)%Lang.credits.length]);
                
		gr.translate(0,28);
		gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		//Game.Quadri();		
		Game.blit(0,0,176,128,0,0,Game.ifons);
		
		//gr.setClip(0,0,w,h);
		timing++;
		Game.blit(0,0,67,67,-68+ppp,30,Game.intro1);//PLANETA
      Game.blit(0,0,67,67,176-ppp,30,Game.intro1);//PLANETA
      switch(estado)
   	{
           /*case 3: 
           			if (cvar > -126)                   
                  {
                  	gr.setColor(255,255,255);
                     gr.fillRect(0, 0, w, h);
                     gr.setColor(0,0,0);                  
                     for (int i = 0 ;i < credits.length;i++)
                     {
                     	//if (i> 2 && i%3 == 1)gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD , Font.SIZE_SMALL));	
                     	//else gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_SMALL));	                  
                        if ((cvar+11*i>-8) && (cvar+11*i < 65)) 
                           if (credits[i]!="") gr.drawString(credits[i],0, cvar+11*i, Graphics.LEFT | Graphics.TOP);	               	              	               
                     }
                     cvar--;                     
                  }
                  if (apretando && currentkey!=0) { estado = 4;cvar=40;Game.currentkey=0;}  
                  break;   */
           case 4: 
           			if (ppp < 34) ppp++;
               	if (timing == 200)	estado = 7;
                  if (KInsectors.sfx) lli[0][2]= 3;
                  else lli[0][2]= 4;                 
                  lli[1][3] = lli[0][2];                                   
                  Game.blit(11*((timing>>2)%2),68,11,18,20,55,Game.intro1);//FLECHITAS
                  Game.blit(11*((timing>>2)%2),68,11,18,w-32,55,Game.intro1);//FLECHITAS
                  //int d = 2;
                  for (int ang = 0+ff;ang < 256+ff;ang = ang + 51)                  
                  	Game.blitc(69,13*lli[prestado][((ff+25)/51)]+7,104,13/2,-30+(Trig.cos(ang)>>3)-10,64+(Trig.sin(ang)>>3)-6+6,Game.intro1);                  	                  
                  for (int ang = 0+ff;ang < 256+ff;ang = ang + 51)                  
                  	Game.blitc(69,13*lli[prestado][((ff+25)/51)],104,13/2,50+176-(Trig.cos(ang)>>3)-10,(64-(Trig.sin(ang)>>3)-6),Game.intro1);                  	                  
                  if (ff > opmenu*51) ff-=2;	
                  if (ff-60 > opmenu*51) ff-=4;	                  
                  if (ff < opmenu*51) ff+=2;
                  if (ff+60 < opmenu*51) ff+=4;
                  
                  
                  if (Math.abs(ff-(opmenu*51)) < 2) ff = opmenu*51;
                  //for (int i = 0;i<lll[prestado].length;i++)
                  //{
                  //	Game.blitc(69,i*13,104,13,w>>1,10+(i*13),Game.intro1);                  	
                  //}
                  //gr.drawRect(9, 4, w-19, h-9);
                  if (apretando)
                  {
                  	timing = 0;
                  	Game.cheat(Game.realkey);
                  	if (currentkey== Canvas.UP) if (opmenu > 0) {opmenu--;}else {opmenu = 4;}//Game.currentkey=0;}
	                  if (currentkey==Canvas.DOWN) 
	                  	if (opmenu < 4) {opmenu++;}else opmenu = 0;//Game.currentkey=0;}
	                  if (currentkey == Canvas.KEY_POUND && prestado==1){ estado=3;}//currentkey = 0;}      	
	                  if (currentkey==Canvas.FIRE || currentkey==-7) 
	                  {                     
	                  	if (lll[prestado][opmenu] == "NewGame") 
	                     {  estado = 2;
	                        Game.prestado=2;
	                     	Game.lifeframe = 1;	      
	                     	Game.one = false;                  
	                     }                                                           
	                     if (lll[prestado][opmenu] == "Resume") 	                     
	                     	   {estado = prestado;Game.abrir = true;Game.lifeframe = 0;Game.rush=true;
	                     	   Game.joc.soundStop();
	                     	   Game.one = false;
	                     	   } 
								if (lll[prestado][opmenu] == "StartF") 	                     	                     	   								
	                     		{estado = 9;opmenu = 1;Game.currentkey = 0;}
	                     if (lll[prestado][opmenu] == "Menu") {estado = 1;Game.prestado=1;} 
	                     if (lll[prestado][opmenu] == "Exit") {estado=6;}
	                     if (lll[prestado][opmenu] == "Help") { estado=5;}                                                           
	                     if (lll[prestado][opmenu] == "Sound") { KInsectors.sfx = !KInsectors.sfx;
	                     							             if (!KInsectors.sfx) Game.joc.soundStop();
	                     							             else Game.joc.soundPlay(0,0);
	                     }//KInsectors.bp[0].stop();}                                                           
	                  }
	                  //if (currentkey==Canvas.KEY_STAR) {estado = prestado;}
	                  Game.currentkey=0;Game.apretando=false;	                  	                  
                  }
                  break;
                  
               case 5:               
                   for (int i = 0 ;i < Lang.controls.length;i++)
                   {
                   	//Game.Print(0, 0, 0, Lang.controls[i], 87, 15+(i*14), 1);                     
                     Game.Print(255, 255, 240, Lang.controls[i], 88, 16+(i*14), 1);                     
                   }
                  if (apretando && currentkey != 0) 
                  { 
                  	estado = 4;timing = 0;
                  	Game.currentkey=0;Game.apretando=false;
                  }  
                  break;  
               case 7:                	
               	if (ppp > 0) ppp--;
               	//for(int i = 0;i < 3;i++)
      				//for(int j = 0;j < 3;j++)            		
						// 	Game.blit(173,((i+j)%4)*4,5,5,((j*367)+(i*57))%176,((j*34)+(i*243))%128,Game.intro1);      		      				      		
				      Game.blit(0,93+13,138,36,0,128-36+ppp,Game.intro1);//PLANETA GORDO     		                              						
				      //gr.setColor(80,0,0);
				      //gr.setClip(0,0,176,128);
				      //gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
				      //gr.drawString(Lang.topscores , 176/2, 13-ppp, Graphics.HCENTER | Graphics.TOP);				 				         
				      //Game.Print(0,0,0,Lang.topscores, 87, 12-ppp, 1);
				      Game.Print(255,255,180,Lang.topscores, 88, 13-ppp, 1);
				      //gr.setColor(130,0,0);
				      for(int i = 0;i < 3;i++)
				      {
				      	//Game.Print(0,0,0,KInsectors.sc.names[i]+"....."+KInsectors.sc.values[i], -1+((ppp<<2)+88)-((i%2)*8*ppp), -1+32+i*20, 1);
				      	Game.Print(255,255,255,KInsectors.sc.names[i]+"....."+KInsectors.sc.values[i], ((ppp<<2)+88)-((i%2)*8*ppp), 32+i*20, 1);
				      }
				      //gr.drawString(KInsectors.sc.names[0]+"....."+KInsectors.sc.values[0] , (ppp<<2)+(176/2), 32, Graphics.HCENTER | Graphics.TOP);
	      			//gr.drawString(KInsectors.sc.names[1]+"....."+KInsectors.sc.values[1], -(ppp<<2)+(176/2), 52, Graphics.HCENTER | Graphics.TOP);
	      			//gr.drawString(KInsectors.sc.names[2]+"....."+KInsectors.sc.values[2], (ppp<<2)+(176/2), 72, Graphics.HCENTER | Graphics.TOP);
      				//Game.printGFX("M�ximas puntuaciones",10, 20-ppp, false);
      				if (apretando) {estado = 4;Game.currentkey = 0;timing = 0;}
              		break;
              	case 9: 
              		if (timing % 60 == 1) Game.addMsg(Lang.startfrom);
              		for (int i = 1 ; i <= KInsectors.cfase;i++)
              		{
              		//for (int i = 1 ; i <= 3;i++)              		
              			Game.blit((i-1)*32, 0,32,32,72,-24+(i*36), Game.ifases);              		
              			Game.Print(255,255,255,  Lang.stages[i], 88, -3+(i*36),0); 
              		}
              		if (timing%2 == 0) Game.blit(24, 69,36,36,70,-26+(opmenu*36), Game.intro1);              			
               	if (apretando)
                  {
                  	if (currentkey==Canvas.UP && opmenu > 1) {opmenu--;}
	                  if (currentkey==Canvas.DOWN && opmenu < KInsectors.cfase) {opmenu++;}
	                  if (currentkey==Canvas.FIRE || currentkey == -7) 
	                  {
	                  	estado = 2;
	                     Game.prestado=2;
	                     Game.lifeframe = opmenu;	                        
	                  }
	                  Game.currentkey=0;	 
	               }
	               
               	
               //case -1:               
                //estado = 4;
                  /*gr.setColor(255,255,255);gr.fillRect(10, 5, w-20, h-10);         
                  gr.setColor(0,0,0);gr.drawRect(9, 4, w-19, h-9);    
                  gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));              
                  for (int i = 0;i<lll[2].length;i++)
                  {
                  	gr.drawString(lll[2][i], 11, 5+i*11, Graphics.LEFT | Graphics.TOP);	               	              	   
                  	gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
                  }
                  if (apretando)
                  {
                  	if (currentkey == Canvas.KEY_STAR) estado = 4;  
                     if (currentkey == Canvas.KEY_POUND) estado=3;
                     Game.currentkey=0;Game.apretando=false;
                  }*/
                  //break;
   			}    
         return  estado;     			
	}
	
	  


	String lll[][] = {{"Resume", "Help","Sound","Menu","Exit"},
							{"NewGame", "StartF","Help","Sound","Exit"}};//,   
                     //{" Menu Controls", "* = menu","2 = up pointer", "8 = down pointer", "5 = select opction"}};
	int lli[][] = {{6,2,3,7,5},
					   {0,1,2,3,5}};//,
   
                     
  

	
}