package com.mygdx.mongojocs.poyogarden;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

import java.util.Random;

public class Intro extends FullCanvas implements Runnable
{
    Sound musica;
    Image ipantalla, ibosque, iextra, ifuego, imarco;
    Thread flujo;
    final int sw = 128;
    final int sh = 128;
    int w, h;
    Random rnd;
    boolean exit = false;
    int tim = 0;
    Graphics gr;
    
    int y = -256+65;
    int offx = 17;
    int noffy = 24;
    int offy = noffy;
    int sep;
    int caida;
    //int desp;
    
    private byte[] a(String s1)
    {
        byte abyte0[] = new byte[s1.length() / 2];
        for(int j1 = 0; j1 < s1.length(); j1 += 2)
        {
            char c1 = s1.charAt(j1 + 1);
            int i1;
            if(c1 >= '0' && c1 <= '9')
                i1 = c1 - 48;
            else
                i1 = (c1 - 65) + 10;
            c1 = s1.charAt(j1);
            if(c1 >= '0' && c1 <= '9')
                i1 += (c1 - 48) * 16;
            else
                i1 += ((c1 - 65) + 10) * 16;
            abyte0[j1 / 2] = (byte)i1;
        }

        return abyte0;
    }

    
    public Intro()
    {
        //LoadGFX();
        imarco = loadImg("/marco");
        ipantalla = loadImg("/intro001");
        ibosque = loadImg("/intro002");
        ifuego = loadImg("/intro003");
        iextra = loadImg("/intro004");    
        try
        {       
    		byte[] b = a("024A3A65C1BDE5BDCDC1B1A5D0040045204586184586184586986984D84186984D86984D87187184184586184586184586986984D84186984D86984D87186A0618420000");
    		//musica = new Sound(b,1);
    		//musica.init(b, 1);
    		/*if (PoyoP.sfx )
    		    try{
    	   	        //musica.play(5);
    	   	    }catch(Exception e){}*/
    	}
        catch(Exception exception) { }	
        
        rnd = new Random();
        flujo = new Thread(this);
	    flujo.start();
  	    System.gc();        
    }


    public Image loadImg(String s)
	{
	  try{
	      Image im = new Image();
	      im._createImage(s+".png");
	      return im;
	  }catch (Exception e) {}
	 return null;	  	
	}
	

    public void blit(int ox, int oy, int _w, int _h, int x, int y, Image foo)
    {
   	        gr.setClip(0, 0,w, h); 
            gr.clipRect(x,y,_w,_h);	         
	        gr.drawImage(foo,x-ox, y-oy, Graphics.TOP|Graphics.LEFT);	         
    }

/*
    public void LoadGFX()
    {
        ipantalla = loadImg("/intro001");
        ibosque = loadImg("/intro002");
        ifuego = loadImg("/intro003");
        iextra = loadImg("/intro004");    
    }


    public void UnloadGFX()
    {
        iextra = null;       
        ifuego = null;        
        ibosque = null;
        ipantalla = null;        
        rnd = null;
        flujo = null;		   
	    gr = null;
        System.gc();
    }*/

    

    public void keyPressed(int keyCode)
    {
        if (keyCode == -6 || keyCode == -7 || keyCode == Canvas.KEY_NUM5) exit = true;
	}
	


    
    public void run()
    {
	   while (!exit)
	   {				 
	    
          tim++;	       
	   	  long tempo = System.currentTimeMillis();
	   	  repaint();
	      serviceRepaints();
	      tempo = 40-System.currentTimeMillis()+tempo;
	      if (tempo <= 0) tempo = 1;	      
	      try	      	    
	      {    	      
	            flujo.sleep(tempo);                     
	      }      
          catch(java.lang.InterruptedException e) {}
        
          if (tim > 600) break;
       
        }
    	
    	//musica.stop();
	   //UnloadGFX();
	   iextra = null;       
        ifuego = null;        
        ibosque = null;
        ipantalla = null;        
        rnd = null;
        flujo = null;		   
	    gr = null;
        System.gc();


	   PoyoP.NewGame();  	  
	   //System.gc();
	}


    
    public void paint (Graphics g)
    {        
   	    gr = g;
   	    
   	    w = sw;h = sh;
   	    gr.setClip(0,0,176,208);
   	    gr.setColor(0x000000);
   	    gr.fillRect(0,0,176,208);
   	    gr.translate((176-128)/2, 30);
   	    gr.setColor(0xffffff);   	    
        gr.fillRect(0,0,128,128);
   	    
   	    DeviceControl.setLights(0,100);
   	    gr.setClip(0,0,sw,sh);
   	    gr.setColor(21, 165, 221);
   	    gr.fillRect(offx,offy+y-32,96,128+32);
   	    blit(0,0,96,128,offx,offy+y+128, ibosque);        
   	    
   	    //MISIL
   	    blit(50, 6, 9, 15,offx+42,offy+y+6+caida, iextra);        
   	    //ROBOPOYO
   	    blit(0, 0, 45, 29,offx+25,offy+y+5, iextra);        
   	    
   	    blit(0, 29, 72, 26,offx-30,offy+y+100, iextra);        
   	    blit(0, 29, 72, 26,offx+40,offy+y+70, iextra);        
   	    blit(0, 29, 72, 26,offx+20,offy+y+36, iextra);        
   	    blit(0, 29, 72, 26,offx-10-sep,offy+y+2, iextra);        
   	    blit(0, 29, 72, 26,offx+30+sep,offy+y+5, iextra);        
   	       	    
        if (tim > 50 && tim < 256 ) y++;
        if (tim > 240 && tim %2 == 0) sep++;
        if (tim > 320 && tim < 423) {caida+=2;y-=2;}
        if (tim > 422 && tim < 435) {caida+=2;}
        if (tim == 435)
        {
            gr.setClip(0,0,sw,sh);
   	        gr.setColor(0,0,0);
   	        gr.fillRect(offx,offy,96,65);   	    
        }
        if (tim == 436)
        {
            gr.setClip(0,0,sw,sh);
   	        gr.setColor(255,255,255);
   	        gr.fillRect(offx,offy,96,65);   	    
        }
        if (tim == 437)
        {
            gr.setClip(0,0,sw,sh);
   	        gr.setColor(255,200,200);
   	        gr.fillRect(offx,offy,96,65);   	    
        }
        if (tim == 438)
        {
            gr.setClip(0,0,sw,sh);
   	        gr.setColor(255,155,155);
   	        gr.fillRect(offx,offy,96,65);   	    
        }        
        if (tim > 438)
        {
            blit(0, 0, 96, 65,offx,offy, ifuego); 
            blit(96, 32*(tim%2), 32, 32 ,offx+5,offy, ifuego); 
            blit(96, 32*((tim+1)%2), 32, 32 ,offx+32,offy+19, ifuego); 
            blit(96, 32*(tim%2), 32, 32 ,offx+62,offy+5, ifuego); 
        }
        if (tim > 438 && tim < 450)
        {
            offy = noffy + rnd.nextInt()%20;
        }
        else offy = noffy;
        
        if (tim%47 >= 44 || tim > 437 && tim < 460) 
        {        
            gr.setClip(0,0,sw,sh);
            
            for(int i = 0; i < 60;i++)
            {
                int c = 127+((i%2)*127);
                 gr.setColor(c,c,c);
                int n = rnd.nextInt();
                int xx = Math.abs(n%34243);                
                int yy = Math.abs(n%434);
                xx = offx+(xx%96);
                yy = offy+(yy%65);
                gr.drawLine(xx,yy,xx,yy);
            }
            
        /*    gr.setColor(200,250,200);
            gr.drawLine(offx, offy+((tim*858)%85), offx+96, offy+((tim*858)%85));*/
        }
      
        blit(0,0,sw,sh,0,0,ipantalla);   	  
        
        
      
        gr.translate(-(176-128)/2, -30);
        h = 208; w = 176;   	    
        
        
        /*gr.setClip(0,0,176,208);
   	    gr.setColor(0xff0000);   	    
   	    for(int i = 0; i < 4; i++)
   	        gr.drawRoundRect(((176-128)/2)-2+i, 30-2+i, 128, 128, 16,16);*/
   	    blit(0,0,imarco.getWidth(), imarco.getHeight(), (176-imarco.getWidth())/2, 27, imarco);    
   	    if (tim > 490)
        {
            blit(1, 66, 100, 49 ,16+20,7, ifuego); 
        }
        
        
        
    }






}