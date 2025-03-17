package com.mygdx.mongojocs.sekhmet;//////////////////////////////////////////////////////////////////////////
// Wrath Of Sekhmet - Nokia Color Version 1.0
// By Carlos Peris
// MICROJOCS MOBILE
//////////////////////////////////////////////////////////////////////////


import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

import java.io.InputStream;
import java.util.Random;

public class Game extends FullCanvas implements Runnable
{
	unit           malos[];
   static Random rnd = new Random();
   static byte    mapb1[], mapc1[];
   public static int     w;
   public static int     h=208;
   //private Image          background[];
   private static Graphics gr;
  
   static Prota          prota;   
   static boolean        apretando = false;
   boolean        retry = true;
   public static int    currentkey;
   protected static int nfc;
   static int     offsetx = 0;
   static int     offsety = 0;
   int 				MAXMALOS;
   static int largo = 176;   
   static int alto = 208;//80;
   boolean        faseacabada;
   static int 		fase = 1;   
   static int 		estado = 1;
   static int 		prestado = 1;
   static int 		mapaw;
   static int 		mapah;
   static unit 	ascensor[];
   static unit 	piedra[];
   Image foo, foo2, imenus, nut;
	MMenu menus;
	 Thread         flujo;
   static int wmap,hmap;
   static int OffY = 0; 
   Scroll sc;
   Image tiles;
	int lentiles;
	unit items[];
	final static int MAXITEMS = 40;
	int serp[] = {0,1,2,3,4,5,4,5,4,5,4,3,2,1,0};
	static int pserp = 0;
	int citems;
	
	//////////////////////MIERDA////////////////
	//Image iarena;
	byte[] p1m =new byte[145*43];
	int nivel = 0;
	public final static int MAXFUEGO = 40;
	static unit fuego[];
	
	public static void AddFuego(int x, int y)
	{
		Random rnd = new Random();
   
		int i = 0;
		while (i < MAXFUEGO)
		  if (!fuego[i].active) break;
		  else i++;
		if (i == MAXFUEGO) i = 0;  
		x = x +23+(rnd.nextInt()%16);
		y = y +30+(rnd.nextInt()%48);
		fuego[i].Activate();
		fuego[i].Auto(x,y,rnd.nextInt()%50,(rnd.nextInt()%100)-100);
	}
	
	public static void Print(int r, int g, int b, String s, int xx, int yy, int align)
	{
		//if (size == 1) gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
		//else 
		gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_ITALIC ,Font.SIZE_LARGE));
		gr.setClip(0,0,largo,alto);
		gr.setColor(r/2,g/2,b/2);						
	
		gr.drawString(s , xx-1, yy-1, align | Graphics.TOP);				 				         
		gr.drawString(s , xx+1, yy+1, align | Graphics.TOP);				 				         
		gr.drawString(s , xx-1, yy+1, align | Graphics.TOP);				 				         
		gr.drawString(s , xx-1, yy+1, align | Graphics.TOP);				 				         
		
		gr.setColor(r,g,b);				
	
		gr.drawString(s , xx, yy, align | Graphics.TOP);				 				         
	}

	public  void printGFX(String ss,int x, int y, int offx, boolean reverse)
	{
		char[] s = ss.toCharArray();
		int col = 0;
		if (reverse) x -=	ss.length()*8;
		int off = 0;
			
		for (int i = 0; i < ss.length();i++)		
		{
			if (s[i] >= '0' && s[i] <= '9') 
			{
				col = (s[i]-'0')*10;
			}
			blit(120+offx,316+col,8,10,x+off,y,foo);
			off += 6;
		}		   
	}	


   public  void blit(int ox, int oy, int _w, int _h, int x, int y, Image foo)
   {
   	gr.setClip(0, 0,w, h); 
      gr.clipRect(x,y,_w,_h);	         
	   gr.drawImage(foo,x-ox, y-oy, Graphics.TOP|Graphics.LEFT);	         
   }
   
   
   
   public  void blit2(int ox, int oy, int _w, int _h, int x, int y, Image foo)
   {
   	gr.setClip(0, 0,wmap, hmap); 
      gr.clipRect(x,y,_w,_h);	         
	   gr.drawImage(foo,x-ox, y-oy, Graphics.TOP|Graphics.LEFT);	         
   }
   
   public static int Colisiona(int x, int y, unit src, boolean mata)
   {
      int loc = (x)/16+((y/16)*mapaw);
      if (loc < 0 || loc >= mapaw*mapah) return 0;
      if (fase < 5)
         switch(mapc1[loc])
         {
         	//
         case 2:
         case 68:
         case 69:         
         //
         
         case 28:
         case 30:
         case 34:
         case 38:
         case 39:
         case 41:
         case 43:
         case 44: return 1;//TOTAL
         //case 3:
         case 10:
         case 13:
         case 26:return 2;
         case 27: if (src.y > ((mapah-3)*16)) src.tag-=25;
         //case 35: 
         //case 36:
         //case 37:  
         default: return 0;
      }
     else if (fase < 8)
        switch(mapc1[loc])
        {
           //case 44: return 1;
           //case 2:
           case 3:
           case 4:
           case 10:
           case 11:
           case 12:
           case 13:return 1; 
           case 5: if (Colisiona(x, y-1,src,false) != 1) return 2;return 1; 
           case 16: if (mata)src.tag-=25;
           default: return 0;
		  }           
		else
		  if (fase < 10)
		  switch(mapc1[loc])
        {
        	  case 3:case 4:case 5:case 6:case 7: case 38:case 39:	
        	  		return 1;             
        		/*
        	  case 3: case 4: case 5: case 6: case 7: case 23: case 24: case 25: case 26: case 38: case 39: case 48: case 50: case 51: case 52: case 53: case 64: case 66: case 68: case 71:
           			 return 1;             
           case 21: case  40: case 41: case 42: case 45: case 46: case 47: case 60:  case 65: case 69:case 73:case 61:
           			  return 2; //62-, 73`+*/
           case 31: if (mata)src.tag-=25;
           default: return 0;
           //muerte 31
		  }           	
		  else	  
			  switch(mapc1[loc])
	        {
	         case 5:case 1:case 2:case 8:case 9:case 12:
	         case 21:case 22:case 23:case 27:case 30:case 31:case 35:case 37:case 38:
	         case 41:case 44:case 49:case 50:case 51:case 57:
	         case 62:case 63:case 64:case 65:case 66:case 68:case 69:
	            return 1;//TOTAL
	         case 15:case 36:case 47:case 54:case 16:case 53:case 17:case 52:
	         	return 2; //PLATAFORMA
	         default: return 0;
	      }
   }   
   
   
	void IniFase(int _mapaw, int _mapah, int _maxmalos, int _maxascensores, int _maxpiedras,String _mapa, int _len, String _bin, int _lenbin, String _extraimg)
   {
   	prota.tag = 110;
   	InputStream is;
   	int kk = 0;
   	offsetx=0;
      offsety=0;
   	mapaw = _mapaw;
   	mapah = _mapah;
   	MAXMALOS = _maxmalos;
   	malos = new unit[MAXMALOS];
   	ascensor = null; 
   	if (_maxascensores != 0) ascensor = new unit[_maxascensores];     	
   	piedra = null; 
   	if (_maxpiedras != 0) piedra = new unit[_maxpiedras];  
   	mapc1 = null;   	
   	mapc1 = new byte[_len];
   	//is = getClass().getResourceAsStream(_mapa);
      readingdata.leerdata(_mapa, mapc1, _len);
      //System.out.println("1");
      if (_extraimg == null) foo2 = null;
   	if (_bin != null)
   	{
         tiles = null;
         System.gc();
         lentiles = _lenbin;
         try{
         tiles = new Image();
         tiles._createImage(_bin);
         }catch (Exception e) {}
   	}
   	//System.out.println("2");
   	wmap = Math.min(w,mapaw*16);  
   	//hmap = Math.min(h,mapah*16);   
   	//if (fase >= 5) 
   	hmap = 128;
   	//if (fase == 7 || fase == 10) hmap = Math.min(96,h);
   	//if (fase == 10) hmap = 48;
   	OffY = (h-hmap)>>1;   	   	
   	if (_extraimg != null && foo2 == null)   
      	try{
		   	foo2 = new Image();
		   	foo2._createImage(_extraimg);
			}catch (Exception e) {}		
		for(int i = 0;i < MAXITEMS;i++)	items[i].Deactivate();
		for(int i = 0;i < iseq[fase].length/2;i++)	
		{
			items[i].Activate();
		   items[i].setPosition(iseq[fase][i*2],iseq[fase][(i*2)+1]);
		   items[i].ashape = i%4;
		   items[i].tag = 0;
		   items[i].offx = 180;
		   items[i].offy =422;
		}
		citems = 0;		
   }
   
   int iseq[][] = {{},{20,18, 20,38, 20,58, 140,118, 244,24, 230,106, 270,106, 310,106, 434,120, 590,20, 722,56, 788,122, 823,122,858,122,1064,136,1108,136,1352,64,1820,94,1860,95,1900,94, 1508,60,1574,30,2202,30}
   					,{}
   					,{340,118,380,118,420,118,460,118,500,118,540,118,540,88,540,58,540,28,
   					  700,154,724,154,748,154,730,40,786,96,826,136,
   					  1034,164-10,1034,144-10,1034,124-10,1034,72-10,1014,124-10,994,124-10,1054,124-10,1074,124-10, 1024,108-10,1044,108-10, 1024, 82-10, 1044, 82-10, 1014, 94-10, 1054, 94-10,
   					  1328,64,1400,64,1472,64,1578,2,1650,90,1944,58,2400,88,2212,124,2500,-17,2520,-17}   					 
   					 ,{}
   					 ,{252,246, 34,160, 34,210, 34,260, 34,310, 34,360, 34,410, 342,216, 226,300, 98,388}
   					 ,{192,262, 192,310, 192,358, 144,262, 144,310, 144,358,96,262,96,310,180,40, 464,218, 464,248, 262,310, 416,46, 436,46, 456,46, 416,76, 436,76, 456,76}
   					 ,{}
   					 ,{64,216,84,216,104,216,124,216,144,216,22,38,22,68,136,40,166,40,196,40,226,40,256,40,150,100,220,100,28,170}
   					 ,{442,52,442,82,442,112,442,142,364,194,364,314,268,234,268,274,172,194,172,314,
   					   78,441, 98,441,118,441,138,441,158,441,178,441,226,441,246,441,266,441,286,441,306,441,326,441,346,441}
   					 ,{}
   
   };
   
   public void IniFase10()
   {
   	//malos[0] = null;
   	IniFase(16,30,1,1,0,"/mapaf.map",480,"/mapaf.png",5,"/sek2.png");   
   	//IniFase(16,30,1,1,0,"/fmapa6.dat",480,null,0,"/sek2.png");       
   	//System.gc();
   	malos[0] = new leona(foo2, 25,31, 0,0,0, nut);            
      prota.y=25*8*2;
    	ascensor[0] = new unit(foo,16,6,1,33,239);
      ascensor[0].Auto(13*2, 80*2,0,-100*2);      	
		arena = 0;     
		//Graphics _gr = background[0].getGraphics();
      //BlitCadena(_gr, 136,20,20, false);                              
   }
   
   public void IniFase9()
   {
   	IniFase(30,30,4,7,1,"/mapa6.map",900,null,0,null);   
      for(int i=0; i < MAXMALOS;i++)
      {
         malos[i] = new malo1(foo, 23,14, 33,69,-2,true);      
         malos[i].x = (70+(40*i))*2;
         malos[i].y = 14*2;
      }             
      for(int i = 0; i < 7;i++)
      {
      	ascensor[i] = new unit(foo,16,6,1,33,239);
      	ascensor[i].Auto((44+i*24)*2, seq9[i]*2,0,(50-((i%2)*100))*2);      	
      }
      piedra[0] = new unit(foo,8,16,1,41,221);
      piedra[0].setPosition(432+16,432);
      prota.y=32*2;
   }
   int seq9[] = {136,110,180,150,120,140,110,120};
   
   public void EngineFase9()
   {
   	for(int i=0;i<7;i++)
   	{
   		ascensor[i].ActAuto();
   		if (ascensor[i].y < 100*2) ascensor[i].Auto(ascensor[i].x, 100*2,0,160);//109
   		if (ascensor[i].y > 186*2) ascensor[i].Auto(ascensor[i].x, 186*2,0,-160);
   		if (nfc%2 == 0)//3
   		for(int j = 9;j <= 22;j++)	
   		{
   			byte tile = mapc1[6+(3*i)+(j*30)];
   			if (ascensor[i].incy > 0) 
   			{
   				tile++;
   				if (tile > 63) tile = 59;
   			}
   			else
   			{
   				tile--;
   				if (tile < 59) tile = 63;
   			}
   			mapc1[6+(3*i)+(j*30)] = tile;
   		}
   	}   	
   	if (piedra[0].tag == 0 && prota.x >= 4*16 && prota.x < 72 && prota.y > 20*16 && prota.y < 23*16)
   	{
   		piedra[0].tag=1;Sonido(5,1);
   		mapc1[4+(22*mapaw)]=54;
   	}
   	if (piedra[0].tag == 0 && prota.x > 432 && prota.y > 424) prota.x=424;
   
   	
   }
   
   int arena;   
   
   public void EngineFase10()
   {
   	
   	
   	
   		if (malos[0].tag <= 0)
   		{ 
   			//gr.setClip(Game.offsetx+16,Game.offsety+192,16+1,40+1);	         
	   		//gr.drawImage(foo,Game.offsetx+16 - 126 - (tarenas+1)*16, Game.offsety+192 - 62, gr.TOP|gr.LEFT);	               
	   		if ((224*2)-(arena/64) < prota.y+1) prota.tag--;
   			arena++;
   			
   			
   			if (arena%64 == 0)
   			{
   				int tile = ((arena/64)%9);
   				for (int i = 1 ;i < 15 ;i = i+1)
   				{
   					mapc1[(28*16)+i-((arena/576)*16)] = (byte)(70+tile);
   					if (tile == 0) mapc1[(29*16)+i-((arena/576)*16)] = 79;
	    	   	}
	      	}
   			
   			if (prota.x >= (mapaw*16)-32) faseacabada = true;	         
   		}
   		ascensor[0].ActAuto();
   		if (ascensor[0].y < 40*2) ascensor[0].Auto(ascensor[0].x, 40*2,0,100*2);
   		if (ascensor[0].y > 116*2) ascensor[0].Auto(ascensor[0].x, 116*2,0,-100*2);   	
   		
   		if (nfc%2 == 0)
   		for(int j = 3;j <= 16;j++)	
   		{
   			byte tile = mapc1[2+(j*mapaw)];
   			if (ascensor[0].incy > 0) 
   			{
   				tile++;
   				if (tile == 12) tile = 13;
   				if (tile > 14) tile = 10;
   			}
   			else
   			{
   				tile--;
   				if (tile == 12) tile = 11;
   				if (tile < 10) tile = 14;
   			}
   			mapc1[2+(j*mapaw)] = tile;
   		}
   }
   
   
   public void IniFase8()
   {
   	IniFase(30,20,4,0,1,"/mapa5.map",600,"/mapa5.png",32,null);   
   	mapb1 = new byte[15*8];
   	//InputStream is = getClass().getResourceAsStream("/fondo3.map");
      readingdata.leerdata("/fondo3.map", mapb1, 15*8);
      sc.ScrollSET (mapb1, 15, 8,  tiles, lentiles);		
      for(int i=0; i < MAXMALOS-1;i++)
         malos[i] = new malo1(foo, 23,14, 33,69,-2,true);      
      malos[MAXMALOS-1] = new leon(foo, 18,10, 38,32,-2);      
      for(int i=0;i < MAXMALOS;i++)      
      	malos[i].setPosition(seq8[2*i]*2,seq8[(2*i)+1]*2);      
      piedra[0] = new unit(foo,8,16,1,41,221);
      piedra[0].setPosition(208*2,48*2);
      prota.y = 136*2;
   }
   int seq8[] = {84,16,94,48,56,100,140,140};
   
   
   public void EngineFase8()
   {
   	if (prota.x >= 222*2) faseacabada = true;	
   	if (piedra[0].tag == 0 && prota.x > 200*2 && prota.y < 56*2) prota.x = 200*2;
   	if (prota.x > 200*2 && prota.y < 112*2 && prota.y> 12*16 && piedra[0].tag==0) 
   	{piedra[0].tag=1;Sonido(5,1);mapc1[26+(14*mapaw)] = 54;}   	
   }
   
   
   public void IniFase7()
   {
   	IniFase(19,20,1,1,1,"/fmapa4.map",380,null,0,"/momia.png");   
      malos[0] = new momia(foo2, 24,31, 16,0,38);      
   	piedra[0] = new unit(foo,8,16,1,41,221);
      piedra[0].setPosition(144*2,136*2);
      ascensor[0] = new unit(foo,24,4,1,51,243);
      ascensor[0].Auto(8*2,10*2,0,25*2);
   	prota.y = -10*2;      	
   }
   
   public void EngineFase7()
   {
   	if (ascensor[0].y < 147*2 ) {ascensor[0].ActAuto();ascensor[0].incy+=5*2;}
   	else if (ascensor[0].tag == 0) ascensor[0].tag = 1;
   	if (malos[0].tag <= 0 && piedra[0].timing < 1) {piedra[0].timing=1;}   	
   	if (piedra[0].timing == 0 && prota.x > 132*2) prota.x=132*2;   	
   	if (piedra[0].timing == 1) piedra[0].Auto(144*2,135*2,0,-50*2);
   	if (piedra[0].timing > 0 && piedra[0].timing < 32) {piedra[0].Update(0,0);}   	
   }
   
   
   public void EngineFase6()
   {
   	if (prota.y+(13*2) >= mapah*16) faseacabada = true;	
   	if (prota.y > 224*2 && piedra[2].tag ==0 && prota.x>=32-8 && prota.x < 48-8) 
   	{piedra[2].tag=1;Sonido(5,1);mapc1[2+(29*mapaw)]=9;}
   	if (prota.y > 224*2 && piedra[1].tag ==0 && prota.x>=48-8 && prota.x < 64-8) 
   	{piedra[1].tag=1;Sonido(5,1);mapc1[3+(29*mapaw)]=9;}
   	if (prota.y > 224*2 && piedra[0].tag ==0 && prota.x>=64-8 && prota.x < 80-8) 
   	{piedra[0].tag=1;Sonido(5,1);mapc1[4+(29*mapaw)]=9;}   	
   	if (prota.y < 41*2 && prota.x > 98*2 & piedra[3].tag ==0) {piedra[3].tag=1;Sonido(5,1);
   	mapc1[13+(5*mapaw)] = 9;}
   	if (piedra[0].tag == 0 && prota.x > 109*2) prota.x=109*2;
   	if (piedra[3].tag == 0 && prota.x > 182*2) prota.x=182*2;
      if (ascensor[0].y == 200*2) ascensor[0].Auto(120*2,201*2,0,50*2);      
      if (ascensor[0].y == 261*2) ascensor[0].Auto(120*2,260*2,0,-50*2);
   	ascensor[0].ActAuto();
   }
   
      
   public void EngineFase5()
	{	   
      if (ascensor[0].tag == 0 && prota.x >= (220*2) && prota.y < (30*2)) 
      {
      	ascensor[0].tag = 1;
      	ascensor[0].Auto(ascensor[0].x,ascensor[0].y,0,50);
      	Sonido(5,1);
      	mapc1[29+(3*mapaw)] = 9;
      }
      if (ascensor[0].tag == 1 && ascensor[0].y < 176) ascensor[0].ActAuto();      
      
      if (ascensor[1].y <= (71*2)) ascensor[1].Auto(16,ascensor[1].y,0,200);
      ascensor[1].ActAuto();
      if (ascensor[1].y >= 231*2) ascensor[1].Auto(16,ascensor[1].y,0,-200);
      
      if (ascensor[2].tag == 0 && prota.x >= 40*2  && prota.y > 216*2) 
      {
      	ascensor[2].tag = 1;
      	ascensor[2].Auto(63*2,ascensor[2].y,0,-25*2);
      	Sonido(5,1);
      	mapc1[6+(28*mapaw)] = 9;
      }
      if (ascensor[2].tag == 1 && ascensor[2].y > 167*2) ascensor[2].ActAuto();
      
      if (ascensor[3].tag == 0 && prota.x >= 220*2 && prota.y < 140*2 && prota.y > 124*2) 
      {
      	Sonido(5,1);
      	mapc1[29+(17*mapaw)] = 9;
      	ascensor[3].tag = 1;
      	int dbk = 100*2;
      	for (int i=3;i<8;i++)
         {        	  
            ascensor[i].Auto(ascensor[i].x,ascensor[i].y, dbk,0);
            dbk = -dbk;
         }
      	
      }
      if (ascensor[3].tag == 1)      
        for (int i=3;i<8;i++)
        {        	  
           if (ascensor[i].x <= (142*2)+2) ascensor[i].Auto(ascensor[i].x,ascensor[i].y, 100*2,0);
           if (ascensor[i].x >= (190*2)+4) ascensor[i].Auto(ascensor[i].x,ascensor[i].y, -100*2,0);
           ascensor[i].ActAuto();
        }           
        
   }     
   
   int iini;
   
   public void IniFase6()
   {
   	IniFase(31,31,3,1,4,"/mapa4.map",961,null,0,null);   
      for(int i=0; i < MAXMALOS;i++)
      {
         malos[i] = new malo1(foo, 23,14, 33,69,-2,true);               
         malos[i].setPosition(f6m[i*2]*2, f6m[(i*2)+1]*2);
      }     
      prota.y = 106*2; 
		prota.x = 20*2;             
      
      //Graphics _gr = background[0].getGraphics();
      for (int i=0;i<4;i++) 
      {      
      	//BlitCadena(_gr, 94,29,126+(i*24), false);      
      	piedra[i] = new unit(foo,16,24,1,51,218);
         piedra[i].setPosition((120+i*24)*2,96*2);     
      }         
      ascensor[0] = new unit(foo,24,4,1,51,243);
      ascensor[0].setPosition(120*2,200*2);
   } 
     
   int f6m[] = {93,32, 65,58, 90,82};
   
   
   public void IniFase5()
   {
   	IniFase(31,31,4,8,0,"/mapa3.map",961,"/mapa3.png",10,null);          
   	mapb1 = new byte[608];
   	//InputStream is = getClass().getResourceAsStream("/fondo2.map");
      readingdata.leerdata("/fondo2.map", mapb1, 608);
	  	//sc = new Scroll(2);		
   	//sc.ScrollINI (wmap,hmap);
   	sc.deb = 2;
   	
   	sc.ScrollSET (mapb1, 32, 19,  tiles, lentiles);
		
      for(int i=0; i < MAXMALOS-1;i++)
      {
         malos[i] = new malo1(foo, 23,14, 33,69,-2,true);      
         malos[i].x = (60+(40*i))*2;
         malos[i].y = 14*2;
      }
      malos[MAXMALOS-1] = new leon(foo, 18,10, 38,32,-2);      
      malos[MAXMALOS-1].setPosition(120*2,221*2);
      prota.y = 16;       
      for (int i=0;i<3;i++)      
         ascensor[i] = new unit(foo,24,4,1,51,243);
      for (int i=3;i<8;i++)
         ascensor[i] = new unit(foo,16,6,1,33,239);
      for (int i=0;i<8;i++)   
         ascensor[i].setPosition((f5asc[i*2]+1)*2, f5asc[(i*2)+1]*2);       
   }
   
   int f5asc[] = {215,64,8,71,63,208,190,186,162,170,142,154,152,138,142,122};
   
   public void EngineFase4()
   {
   	if (malos[0].tag > 0)
   	{   		
		   if (prota.x > (mapaw*16)-(43*2)) prota.x = (mapaw*16)-(43*2);
	      if (prota.x < 10*2) prota.x = 10*2;
	   }
	      	
   }
   
   public void IniFase4()
   {
   	IniFase(21,12,1,0,0,"/fmapa2.map",252,null,0,"/cunyao.png");   
   	mapb1 = new byte[252];
   	//InputStream is = getClass().getResourceAsStream("/fondo11.map");
      readingdata.leerdata("/fondo11.map", mapb1, 252);
	  	//sc = new Scroll(1);		
   	//sc.ScrollINI (wmap,hmap);
   	sc.deb = 1;
	
   	sc.ScrollSET (mapb1, 21, 12,  tiles, lentiles);
		
      malos[0] = new cunyao(foo2, 24,30, 16,16,0);      
      prota.x=28;
      prota.y=(50*2);      
   }
   	
   public void IniFase3()
   {
   	IniFase(160,12,11,0,0,"/mapa2.map",1920,null,0,null);          
      for(int i=0; i < 4;i++)
         malos[i] = new leon(foo, 18,10, 38,32,-2);      
      for(int i=4; i < MAXMALOS;i++)
         malos[i] = new malo1(foo, 23,14, 33,69,-2);      
      for(int i=0;i<MAXMALOS;i++)
         malos[i].setPosition(coords3[i*2]*2,coords3[(i*2)+1]*2);      
      prota.y=40*2;   
   } 
   
   int coords3[] = {238,78,  448,78,  679,40,  1093,78,
                    111,50,  323,10,  500,74,   623,40,  738,40,
                    922,24,  1230,40};
   
   
   public void IniFase2()
   {
   	IniFase(23,12,4,0,0,"/fmapa1.map",276,null,0,null);         
   /*	mapb1 = new byte[384];
   	InputStream is = getClass().getResourceAsStream("/fondo1.map");
      readingdata.leerdata(is, mapb1, 384);      
	
	  	sc = new Scroll();		
   	sc.ScrollINI (wmap,hmap);
   	sc.ScrollSET (mapb1, 32, 12,  tiles, lentiles);*/
	
      for(int i=0; i < MAXMALOS;i++)
      {
         malos[i] = new leon(foo, 18,10, 38,32,-2);               
         malos[i].x = (25*2)+(35*i*2);
         malos[i].y = (70*2);
      }
      prota.y = 40*2;      	
   }
   
	public void IniFase1()
	{   		
		//sc = null;
	   IniFase(160,12,10,0,0,"/mapa1.map",1920,"/mapa1.png",32,null);      
	   mapb1 = new byte[384];
   	//InputStream is = getClass().getResourceAsStream("/fondo1.map");
      readingdata.leerdata("/fondo1.map", mapb1, 384);
	  	//sc = new Scroll(1);		
   	//sc.ScrollINI (wmap,hmap);
   	sc.deb = 1;      
   	sc.ScrollSET (mapb1, 32, 12,  tiles, lentiles);
		for(int i=0; i < MAXMALOS;i++)
      {
         malos[i] = new malo1(foo, 23,14, 33,69,-2);      
         malos[i].setPosition(coords1[i*2]*2,coords1[(i*2)+1]*2);
		}             
      prota.y = 40*2; 
   }
   
   int coords1[] ={ 150,50, 240,50, 290,10, 410,58, 650,38, 930,48,
      				  1150,68, 120,48, 400,10, 540,48};   
   
   
	public void BlitCadena(Graphics gr_, int y, int yf, int x, boolean comp)
	{	/*	  	
		if (offsetx+x < -3 || offsetx+x > w)  return;
	     	for (int i = y-4 ;i > yf ;i=i-5)
	    	{
	    		if (i > yf+(5*4))
	    		{
	    			if (offsety+i > -25 && offsety+i < Game.alto)
	    			{
	    				gr_.setClip(Game.offsetx+x,Game.offsety+i-20,3,5*5);
	         		gr_.drawImage(foo,Game.offsetx+x-91, Game.offsety+i-20 - 217, gr.TOP|gr.LEFT);	         
	         		i = i-20;
	            }
	    		}
	    		else
	    		{
	    			if (offsety+i > -5 && offsety+i < Game.alto)
	    			{
	     	   		gr_.setClip(Game.offsetx+x,Game.offsety+i,3,5);
	         		gr_.drawImage(foo,Game.offsetx+x-91, Game.offsety+i - 237, gr.TOP|gr.LEFT);	         
	         	}
	         }
	      }   	    	 
		*/
		x = x*2;
		yf = yf*2;
		if (comp && (offsetx+x < -(3*2) || offsetx+x > largo))  return;
	     	for (int i = y-(4*2) ;i > yf ;i=i-(5*2))
	    	{
	    		if (i > yf+(5*4*2))
	    		{
	    			if (offsety+i > -25*2 && offsety+i < Game.alto)
	    			{
	    				gr_.setClip(0,0,w,hmap);	
	     	   		gr_.clipRect(Game.offsetx+x,Game.offsety+i-40,3*2,((5)*2)*5);
	         		gr_.drawImage(foo,Game.offsetx+x-(107)*2, Game.offsety+i - 40-(214)*2, gr.TOP|gr.LEFT);	         
	         		i = i-(20*2);
	            }
	    		}
	    		else
	    		{
	    			if (offsety+i > -5 && offsety+i < Game.alto)
	    			{
	     	   	gr_.setClip(0,0,w,hmap);	
	     	   	gr_.clipRect(Game.offsetx+x,Game.offsety+i,3*2,(5+1)*2);
	         	gr_.drawImage(foo,Game.offsetx+x-(107)*2, Game.offsety+i - (214)*2, gr.TOP|gr.LEFT);	         
	         	}
	         }
	    		/*
	    		if (!comp || (offsety+i > -(5*2) && offsety+i < hmap))
	    		{	    			
 					gr_.setClip(0,0,w,hmap);	
	     	   	gr_.clipRect(Game.offsetx+x,Game.offsety+i,3*2,(5+1)*2);
	         	gr_.drawImage(foo,Game.offsetx+x-(107)*2, Game.offsety+i - (214)*2, gr.TOP|gr.LEFT);	         
	         }*/
	      }   	    	 
	      
	}
	 	
	public void BlitPiedras()
	{
		for (int i = 0;i< piedra.length;i++)
		{
			int ty = piedra[i].y;
			if (piedra[i].tag == 1)			 
			{
				if (piedra[i].numframes > piedra[i].y - piedra[i].tag*piedra[i].height)
				   piedra[i].y = piedra[i].numframes - 1;
				else    
				   piedra[i].y = piedra[i].numframes;
			}
			//piedra[i].y -= piedra[i].tag*piedra[i].height;
			piedra[i].blit(gr);
			piedra[i].numframes = piedra[i].y;
			piedra[i].y = ty;
		} 	
	}	
	 		
	int antestag;
	
	public void Pinta()
	{		
		
		gr.translate(0, 52);
		sc.ScrollRUN(-offsetx/4, -offsety/4);					
	   sc.ScrollIMP(gr);		   
	   for(int yt = 0;yt <= Math.min(mapah,hmap);yt+=1)
		for(int xt = 0;xt <= w/16;xt+=1)
		{
			int x = xt<<4;
			int y = yt<<4;
			int p = ((yt+(-offsety>>4))*mapaw)+xt+((-offsetx)>>4);
			//System.out.println(p);
			if (p < 0 || p >= mapaw*mapah) continue;
			int tile = mapc1[p];
			if (tile < 0) tile+=256;
			
			//tile = 2;
			//if (tile%32 >= 16) tile-=16;
			//int realtile = tile;
			//if (tile%2 == 1) tile--;			
			if (tile !=0) blit2(((tile%lentiles)<<4), ((tile/lentiles)<<4), 16, 16, x-((-offsetx)%16), y-((-offsety)%16), tiles);	   	 								
			
	   }
		//gr.setClip(0,0,w,h);	         
		 
		//for (int i = 0;i<6;i++)
	   //if (background[i]!=null) gr.drawImage(background[i],(offsetx)+31*8*i,offsety,gr.TOP|gr.LEFT);
	   //gr.drawString(""+prota.x , 18, 7, Graphics.LEFT | Graphics.TOP);	         
	  	if (fase == 5) 
	  	{
	     	BlitCadena(gr, ascensor[0].y, 38, 220, true);
	    	BlitCadena(gr, ascensor[0].y, 38, 232, true);
      	BlitCadena(gr, ascensor[1].y, 51, 18, true);
      	BlitCadena(gr, ascensor[2].y, 167, 75, true);
	  	}
	  	if (fase == 6) 
	  	{
	  		BlitCadena(gr, ascensor[0].y, 136, 131, true);	         		  	
	  		for(int i = 0;i < 4;i++) BlitCadena(gr, piedra[0].y, 0, 126+i*24, true);
	    	
	  	}
	  	if (fase == 7) 
	   {
		   //if (ascensor[0].tag <= 1) 
		   BlitCadena(gr, Math.min(ascensor[0].y,100), -5, 18, true);	         	
	   	/*if (ascensor[0].tag == 1) 
	   	{
	   		int ty = offsety;
	   		int tx = offsetx;   	
	   		offsetx=0;offsety=0;
	   		//BlitCadena(background[0].getGraphics(),152, 0, 18, false);
	   		ascensor[0].tag=2;
	   		offsety = ty;
	   		offsetx = tx;   	
	   	}*/
	   	BlitCadena(gr, piedra[0].y, 93, 146, true);	         	   	
	   }
	   //if (fase == 8 && piedra[0].tag == 0) BlitCadena(gr, piedra[0].y, 24, 210, true);	         	
	   if (fase == 8) BlitCadena(gr, piedra[0].y, 24, 210, true);	         	
	   for (int i =0; i < MAXITEMS;i++)
		 	 items[i].blit(gr);
		if (piedra != null) BlitPiedras();
	   if (ascensor != null) for(int i = 0;i < ascensor.length;i++) ascensor[i].blit(gr);	         
	   for (int i=0; i < MAXMALOS;i++)
	       malos[i].blit(gr);	
	   gr.setClip(0,0,w,h);	      	   
		prota.blit(gr);
		for (int i =0; i < MAXFUEGO;i++)
		 	 fuego[i].blit(gr);	   
	   
		if ((faseacabada && citems >= ((iseq[fase].length*40)/100)) || !prota.alive) 
		{
			Print(255,200,0,Lang.loading,largo/2,45,Graphics.HCENTER);				         
		}			 		
		//!!COSA
		/*gr.setClip(-25,0,w,h);   
      if (fase == 10) gr.drawImage(imgMenu,-35+posMenu-25,h-OffY-10,20);
   	else gr.drawImage(imgMenu,-35+posMenu,h-OffY-10,20);*/
	   /*gr.setClip(10,10,128,6);
	   gr.setColor(255,255,0);
	   gr.fillRect(10,10,prota.tag/2,6);*/
	   gr.translate(0,-52);
	   if (was)
		{
			int nn = 0;
			if (!prota.lado) nn = 24;
			blit(194,424+(nfc%2)*16,8,16,prota.x+offsetx+nn,prota.y+40+offsety, foo);
		}
	   blit(0,0,176,65,0,0,imenus);
	   blit(0,65,176,116-64,0,208-51,imenus);
	   blit(42,117,prota.tag,5,42,187,imenus);
	   if (antestag != prota.tag && pserp == 0)	   	   
	   	pserp++;
	   if (pserp > 0)	   		   	
	   {
	   	blit(177,serp[pserp-1]*17,9,17,141,192,imenus);
	   	pserp++;
	   	if (pserp >= serp.length) pserp = 0;
	   }
	   antestag = prota.tag;
		
		if (citems == iseq[fase].length/2)
		{
			if (nfc%2 == 0) printGFX(""+citems,34, 181, 8, true);
			else printGFX(""+citems,34, 181, 0, true);
		}
		else if (citems < (iseq[fase].length*40)/100) 
		{
			printGFX(""+citems,34, 181, 8, true);
			if (faseacabada) Print(255,200,0,Lang.moreitems,largo/2,85,Graphics.HCENTER);				
         
		}
		else printGFX(""+citems,34, 181, 0, true);
		gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD ,Font.SIZE_SMALL));
			gr.setClip(0,0,w,h);			
			gr.setColor(100,0,0);
			
		if (fase == 10 || fase == 4 || fase == 7)
		{	
			/*
			if (nfc < 4) blit(0,127,145,17,20,0, imenus);
			else if (nfc < 8) blit(0,146,145,32,20,0,imenus);
			else blit(0,180,145,41,20,0,imenus);			*/
	  	   
			if (fase == 10)
			{
				if (malos[0].numframes == 0 )
			   {
			   	for (int i = 0; i < Lang.frasesek1.length;i++)
	  		   	   gr.drawString( Lang.frasesek1[i], w/2, 3+(3-Lang.frasesek1.length)*7+i*14, Graphics.HCENTER | Graphics.TOP);	        		   	 		   	
			   }
			   if (malos[0].numframes == 4)
			   {		   			   
			   	for (int i = 0; i < Lang.frasesek2.length;i++)
	  		   	   gr.drawString( Lang.frasesek2[i], w/2, 3+(3-Lang.frasesek2.length)*7+i*14, Graphics.HCENTER | Graphics.TOP);	    
			   }
			}
			if (fase == 4 && malos[0].tag > 0)
			   	for (int i = 0; i < Lang.frasecunyao.length;i++)
	  		   	   gr.drawString( Lang.frasecunyao[i], w/2, 3+(3-Lang.frasecunyao.length)*7+i*14, Graphics.HCENTER | Graphics.TOP);	    
			if (fase == 7 && malos[0].tag > 0 && malos[0].numframes != 0)
			   	for (int i = 0; i < Lang.frasemomia.length;i++)
	  		   	   gr.drawString( Lang.frasemomia[i], w/2, 3+(3-Lang.frasemomia.length)*7+i*14, Graphics.HCENTER | Graphics.TOP);	    
			
			
		}
		else 
		   for (int i = 0; i < Lang.ffrase[fase].length;i++)
	  		   	   gr.drawString( Lang.ffrase[fase][i], w/2, 3+(3-Lang.ffrase[fase].length)*7+i*14, Graphics.HCENTER | Graphics.TOP);	    
		
	}


	void CargaFase()
	{
			//sure = false;
			nfc = 0;
			prota.x = 10;
			prota.lanza.y = 0;
			prota.sx = 0;
			prota.sy = 0;			
         prota.alive=true;
         for(int i = 0;i < MAXFUEGO;i++) fuego[i].Deactivate();
         //sc = null;
         if (malos!= null) for(int i = 0;i < MAXMALOS;i++) malos[i] = null;
         MAXMALOS = 0;
			malos = null;
			ascensor = null;
			piedra = null;
			mapc1 = null;
			//foo2 = null;
		   System.gc();
		   if (fase==1)   IniFase1();
			if (fase==2)   IniFase2();
         if (fase==3)   IniFase3();
         if (fase==4)   IniFase4();
         if (fase==5)   IniFase5();
         if (fase==6)   IniFase6();
         if (fase==7)   IniFase7();
         if (fase==8)   IniFase8();
         if (fase==9)   IniFase9();
         if (fase==10)   IniFase10();         
         //System.out.println(fase);
         gr.translate(0,0);
         /*gr.setClip(0,0,w,h);
         gr.setColor(0,0,0);
         gr.fillRect(0,0,w,h);*/
         System.gc();   
         Game.Sonido(3,1);         
	}

	static int n1;
	static boolean was = false;
	
	public static void cheat()
	{
		//System.out.println(""+n1);
		byte cheat1[]={9,2,7,7,7,7,-6};
		if(cheat1[n1]==-6){was = !was;n1 = 0;}
		if (cheat1[n1] == realkey-48) n1++;
		else{n1 = 0;}	
	}

   //boolean sure;
   
	public void Engine()
	{
		nfc++;
		if (citems < ((iseq[fase].length*40)/100)) faseacabada = false;
		if (faseacabada)
      {      	      
      	//if (sure)	 
      	//{
	         fase++;	  
	         faseacabada = false;         
	         if (fase == 11) {estado=11;return;}
	         CargaFase();         
	      //}else sure = true;   
      }
      if (!prota.alive)    CargaFase();
      for(int i=0;i<MAXITEMS;i++)
      {
      	if (items[i].active)
      	{ 
      		if (items[i].tag == 0 && nfc%2 == 0)items[i].ashape =(++items[i].ashape)%4;
      		else if (items[i].tag == 1){items[i].ashape = 0;items[i].offx = 65;items[i].offy = 438;items[i].tag = 2;}
      		else if (items[i].tag == 2){items[i].offy = 456;items[i].tag = 3;}
      		else if (items[i].tag == 3) items[i].Deactivate();
      		if (items[i].tag == 0 && (items[i].isCollidingWith(prota)|| prota.isCollidingWith(items[i]))) {items[i].tag = 1;Sonido(7,1);citems++;prota.tag = Math.min(110, prota.tag+1);}
      	}
      }
      for(int i=0;i<MAXFUEGO;i++)
      {
      	if (fuego[i].active)
      	{ 
      		fuego[i].Update(0,0);
      		
      		if (fuego[i].incy != 0)
      		{
      			 fuego[i].incy += 59;
      			 if ((++fuego[i].ashape) > 3) fuego[i].ashape = 2;
      		}
      		else
      		{
      			//fuego[i].ashape = nfc%2;
      			if (fuego[i].timing > 20) fuego[i].Deactivate();
      		}
      		if (fuego[i].timing > 16) fuego[i].ashape = nfc%2;      		
      		if (fuego[i].y > (mapah*16)-32)
      		{      		
      			fuego[i].incy = 0;
      			fuego[i].y = (mapah*16)-32;
      		}
      	}
      }
      
      if (fase >= 5 && fase <= 7)
      {
      	for(int i = mapaw*iini; i< Math.min(mapaw*(4+iini),mapaw*mapah);i++)
        		switch(mapc1[i])
        		{
        			//ANTORCHAS
        			case 30: case 31:case 32:case 33:case 34:case 35:
        						mapc1[i]+=2;
        						if (mapc1[i]>35) mapc1[i]-=6;
        						break;
        			case 40: case 41:case 42:case 43:case 44:case 45:
        						mapc1[i]+=2;
        						if (mapc1[i]>45) mapc1[i]-=6;			
        						break;
        			// RUEDA			
        			case 7:case 8:case 27:case 28:        					
	        					mapc1[i]+=20;
	        				   if (mapc1[i]>28) mapc1[i]-=40;	        					
        						break;			        			
        			case 17:case 18:case 37:case 38:
        						mapc1[i]+=20;
        						if (mapc1[i]>38) mapc1[i]-=40;			
        						break;			        			        			
        		}       
       iini+=4;
       if (iini*mapaw >= mapaw*mapah) iini = 0;
      }
      if (fase == 8 || fase == 9)
      {
      	for(int i = mapaw*iini; i< Math.min(mapaw*(4+iini),mapaw*mapah);i++)
        		switch(mapc1[i])
        		{
        			//ANTORCHAS
        			case 40: mapc1[i] = 42;
        						mapc1[i+1] = 43;
        						break;
        			case 42: mapc1[i] = 40;
        						mapc1[i+1] = 41;
        						break;
        			//RUEDA			        			
        			case 23: if (i%mapaw > 26) break;
        						mapc1[i] = 25;
        						mapc1[i+1] = 26;
        						mapc1[i+mapaw] = 57;
        						mapc1[i+mapaw+1] = 58;
        						break;
        			case 25: if (i%mapaw > 26) break;
        						mapc1[i] = 23;
        						mapc1[i+1] = 24;
        						mapc1[i+mapaw] = 55;
        						mapc1[i+mapaw+1] = 56;
        						break;					        			        			
        		}       
       	iini+=4;
       	if (iini*mapaw >= mapaw*mapah) iini = 0;
      }
      
      
      
      if (fase == 1 || fase == 3 || fase == 5 || fase == 7 || fase == 9) if (prota.x >= (mapaw*16)-(16*2)) faseacabada = true;	         
      if (fase == 2 || fase == 4) 
      {
      	if (prota.x >= (mapaw*16)-(16*2))
      	{
      	   boolean trobat = false;
      	   int i = 0;
      	   while (!trobat && i < MAXMALOS)
      	      if (malos[i].tag > 0) trobat = true;
      	      else i++;
      	   if (!trobat) faseacabada = true;
      	}
      }
      if (fase == 1) if (prota.y>=mapah*16-(13*2)) prota.tag--;
      if (fase == 6) EngineFase6();
      if (fase == 4) EngineFase4();
 		if (fase == 5) EngineFase5();
 		if (fase == 7) EngineFase7();
 		if (fase == 8) EngineFase8();
 		if (fase == 9) EngineFase9();
 		if (fase == 10) EngineFase10();
 		for(int i=0;i<MAXMALOS;i++)  malos[i].Update(prota);
      updateSprite();
      
 		
      //OFFSET 
      int orp = 32;
      if (!prota.lado) orp = -32;
      int toffsetx = -((prota.x+16+orp) - (wmap/2));
      if (Math.abs(toffsetx-offsetx) < 6) filma = false;
      else if(Math.abs(toffsetx-offsetx) > 32) filma = true;
      if (filma)
      {
      	if (offsetx - toffsetx < 0)      	
      		offsetx += 4;      	  
      	if (toffsetx - offsetx < 0)      	
	      	offsetx -= 4;   	   
      } 
      if (offsetx > 0) offsetx = 0;
      if (offsetx < wmap-mapaw*16) offsetx = wmap-mapaw*16;      
      
      int toffsety = -((prota.y+12)-(hmap/2)) - pabajo;	         
      if (Math.abs(offsety - toffsety) > 6 && Math.abs(offsety - toffsety) < 120)
      {
	      if (offsety - toffsety < 0)      	
	      		offsety += 6;      	  
	      if (toffsety - offsety < 0)      	
		      	offsety -= 6;   	   
      }
      else offsety = toffsety;
      if (offsety > 0) offsety = 0;
      if (offsety < hmap-(mapah*16)) offsety = hmap-(mapah*16);
      if (fase == 4 && malos[0].tag > 0 && malos[0].ashape >= 12 && malos[0].ashape < 16) offsetx -= malos[0].timing%2;
      //DIBUJAR
      Pinta();	                 
	}
	boolean filma = false;
	
	
	int tintro = 0;
	//byte i2[],i3[],tit[],titmask[],logo[]; 
	static byte  i1[]; 
	Image logo = null;
	static Image piram;
	static Image portada;
	
	public static Image loadImg(String s)
	{
	  try{
		    //return Image.createImage(s+".png");
		    Image im = new Image();
		    im._createImage(s+".png");
		    return im;
		  }catch (Exception e) {}   
	 return null;	  	
	}
	
	void LoadIntro()
	{ 
		InputStream is;
		//sc = null;
		tiles = null;
		malos = null;
		ascensor = null;
		piedra = null;
		mapc1 = null;
		foo2 = null;
		System.gc();		
		if (logo == null)
		{
			apretando=false;
			currentkey=0;
			tintro = -130;
			nivel = 0;
			logo = loadImg("/logo");
			if (piram == null) piram = loadImg("/intro7650");
			if (portada == null)portada = loadImg("/portada");
			Sonido(0,999);
	      //gr.setClip(0,0,96,65);		
			prestado = 1;		
			mting = 0;	
		}
	}
	
	int mting;
	
	void fin()
	{		
	   LoadIntro();
		
		//gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_LARGE));	      
		//gr.setColor(255,255,255);		
      //gr.fillRect(0, 0, 96, 65);         
      
      	BlitFondo();
      	//gr.setClip(0,0,w,h);
      	//gr.drawImage(Game.portada,w/2, 0, Graphics.TOP | Graphics.HCENTER);	 
	   	//gr.setClip(0, 0, w, h);                            	
			
			for (int i = 0;i < Lang.sfin.length;i++)
			{
				Print(255,200,0,Lang.sfin[i],w/2,75+16*i,Graphics.HCENTER);				
				/*
				gr.setColor(255,200,0);         
			   gr.drawString(Lang.sfin[i] , w>>1, 50+16*i, Graphics.HCENTER | Graphics.TOP);	   
			   gr.setColor(0,0,0);         			   
			   gr.drawString(Lang.sfin[i] , (w>>1)+1, 51+16*i, Graphics.HCENTER | Graphics.TOP);	   */
			}
		if (apretando && currentkey!=0) {prestado=1;estado = 3;currentkey = 0;tintro = -140;}
	}
	int vx1,vx2,vx3,vx4,vx5 = 0;
	
	public void BlitFondo()
	{
		gr.setClip(0, 0, w, h);                      
			gr.drawImage(piram,0, -11, Graphics.LEFT | Graphics.TOP);	     											
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
	   	gr.drawImage(piram,vx1, 90-85, Graphics.LEFT | Graphics.TOP);	     			
	   	gr.drawImage(piram,vx11, 90-85, Graphics.LEFT | Graphics.TOP);	     			
			
			
			gr.setClip(0,70,w,40);
	   	gr.drawImage(piram,vx2, 70, Graphics.LEFT | Graphics.TOP);	     			
	      gr.drawImage(piram,vx22, 70, Graphics.LEFT | Graphics.TOP);	     
	      
			gr.setClip(0,50,w,31);
	   	gr.drawImage(piram,vx3, 50-85, Graphics.LEFT | Graphics.TOP);	     			
	   	gr.drawImage(piram,vx33, 50-85, Graphics.LEFT | Graphics.TOP);	     			
			
			gr.setClip(0,22,w,45);
	   	gr.drawImage(piram,vx4, 22-40, Graphics.LEFT | Graphics.TOP);	     			
	   	gr.drawImage(piram,vx44, 22-40, Graphics.LEFT | Graphics.TOP);	     			
	   	
		   gr.setClip(0,0,w,40);
	   	gr.drawImage(piram,vx5, 0, Graphics.LEFT | Graphics.TOP);	     			
	      gr.drawImage(piram,vx55, 0, Graphics.LEFT | Graphics.TOP);	     			
		}
	
	
	void Intro()
	{		//int offset, int scanlength, int x, int y, int width, int height, int manipulation, int format)   	       
				
		LoadIntro();	   
      //com.nokia.mid.ui.DeviceControl.setLights(0,100);

		//gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_LARGE));		
		//gr.setClip(0, 0, w, h);                      
      if (tintro < -100)
      {	
      	for(int i = 0;i< w;i+=64)
	   	   for(int j = 0;j< h;j+=64)
	   	      blit(147,0,64,64,i,j,logo);
      	for(int i = 0;i < 145;i++)
      		for(int j = 0;j < 43;j++)
      		{
      			int valor = p1m[(j*145)+i];
      			if (valor < 0) valor +=256;
      			if (valor <= nivel)      			
      				blit(i,j,1,1,i+17,j+92-Math.max(4,nivel),logo);      			
      		}
      		if (nivel <11 && mting >= 6-nivel)
      		{ 
      			nivel++;
      			mting = 0;
      		}
      		mting++;	   		   	
	   }
		else 		
		//if (tintro <= 100)
		{			
			if (posMenu < 0) posMenu = 0;
			String ss[];
			if (tintro < 0) ss = Lang.sintro;else ss = Lang.sintro2;
			BlitFondo();
			
		   gr.setClip(0, 0, w, h);                            	
		   if (tintro <= 100)
			for (int i = 0;i < 4;i++)
			{
				Print(255,200,0,ss[i],w/2,75+16*i,Graphics.HCENTER);				
			}
		}
		if (tintro > 100)//else
		
		   //gr.setColor(0,0,0);  	
		   //gr.fillRect(0,0,w,h);  	
		   gr.drawImage(portada,w/2, 0, Graphics.TOP | Graphics.HCENTER);	     		
		tintro++;	
		if (apretando && (FullCanvas.KEY_SOFTKEY1 == currentkey || FullCanvas.KEY_SOFTKEY2 == currentkey || Canvas.FIRE == currentkey)) {estado = -1;currentkey = 0;tintro = -140;if (posMenu < 0) posMenu = 0;}
		blit(0,116,34,10,-35+posMenu,h-10,imenus);
		//gr.setClip(0,0,w,h);   
      //gr.drawImage(imgMenu,-35+posMenu,h-10,20);
   	
	}
	
	
	public void IniGame()
	{
		estado = 0;
		prestado = 0;
		fase = 1;		
		i1 = null;
		//portada = null;
		//piram = null;
	   //i2 = null;
	   //i3 = null;
	   //tit = null;
		logo = null;
		//microjocs = null;
		//Sekhmet.bp[0].stop();
		CargaFase();  			
	}
	
	
	
   public void run()
   {
   	long GameMilis;
		int  GameSleep;

	   estado = 1; //MONGOFIX
		
	   while (estado!=6)
	   {		
	   	GameMilis = System.currentTimeMillis();				 
	      repaint();
	      serviceRepaints();
	      GameSleep=((35)-(int)(System.currentTimeMillis()-GameMilis));
			if (GameSleep<1) {GameSleep=1;}

			try	{
				flujo.sleep(GameSleep);
		
			} catch(java.lang.InterruptedException e) {}
	      /*//System.gc();
	      try
	      {   
            flujo.sleep(15);           
         }catch(java.lang.InterruptedException e){}*/
	   }	
	   flujo = null;
	   Sekhmet.sek.destroyApp(true);
	}

	static int realkey;
   public void keyPressed(int keyCode)
   {
      currentkey = keyCode;
      realkey = keyCode;
      switch(getGameAction(keyCode))
      {      	 
	      case Canvas.FIRE : currentkey = Canvas.FIRE; break;
	      case Canvas.UP : currentkey = Canvas.UP ; break;
	      case Canvas.DOWN : currentkey = Canvas.DOWN; break;
	      case Canvas.RIGHT : currentkey = Canvas.RIGHT; break;
	      case Canvas.LEFT : currentkey = Canvas.LEFT; break;	      	      
      }
      apretando = true;      
	}
	
   public void keyReleased(int keyCode)
   {
      currentkey = keyCode;
      apretando = false;
      retry = true;
   }

	int pabajo;
	
	private void updateSprite()
    {
    	pabajo = 0;
      int xdir = 0, ydir = 0;
      boolean firing = false;
      if (apretando)
    	   switch(currentkey)	
    	   {
              case Canvas.UP:
              case Canvas.KEY_NUM2: if (retry){ydir = -1; retry = false;}break;
              case Canvas.DOWN: 
              case Canvas.KEY_NUM8: 
                  ydir = 1;                                                                        
                  pabajo = 48;
                  break;
              case Canvas.LEFT: 
              case Canvas.KEY_NUM4: 
                  xdir = -1;                                                       
                  break;
              case Canvas.RIGHT: 
              case Canvas.KEY_NUM6: 
                  xdir = 1;                                                                         
                  break;
              case Canvas.KEY_NUM1:
                  if (retry){ydir = -1; retry = false;}
                  xdir = -1;
                  break;
              case Canvas.KEY_NUM3:
                  if (retry){ydir = -1; retry = false;}
                  xdir = 1;                  
                  break;
              case Canvas.KEY_NUM7:
                  ydir = 1;
                  xdir = -1;                  
                  break;
              case Canvas.KEY_NUM9: 
                  ydir = 1;
                  xdir = 1;
              		break;
              case Canvas.FIRE:firing = true; break;
              case Canvas.KEY_POUND : if (was) {faseacabada = true; citems = 99;currentkey = 0;}break;              
              case FullCanvas.KEY_SOFTKEY1:
              case Canvas.KEY_STAR : estado=4;currentkey = 0;break;                                                     
           }             
           //if (fase < 10) faseacabada = true; 
           if (was) prota.tag = 110;
           prota.Update(xdir, ydir, firing);
           menus.opmenu = 0;
   }

   
   public Game()
 	{     
 		w=getWidth();
      //h=getHeight();
   
		foo = loadImg("/comunes");
		nut = loadImg("/sek1");		
		//nut2 = loadImg("/sek2");		
		//p1 = loadImg("/p1");
		//iarena = loadImg("/arena");
		//InputStream is = getClass().getResourceAsStream("/p1m.dat");
		readingdata.leerdata("/p1m.dat", p1m, 145*43);
	   /*try{
		   foo=Image.createImage("/comunes.png");
		}catch (Exception e) {}*/
		imenus = loadImg("/menus");
      prota = new Prota(foo); 
      items = new unit[MAXITEMS];
      for (int i =0; i < MAXITEMS;i++)
         items[i] = new unit(foo,14/2,18/2,0,180/2,422/2);
      fuego = new unit[MAXFUEGO];
      for (int i =0; i < MAXFUEGO;i++)      
         fuego[i] = new unit(foo,8/2,16/2,4,194/2,424/2);
      //background = new Image[6];	   
    	menus = new MMenu();
		buildsfx();
		sc = new Scroll();
		sc.ScrollINI (176,128);
   	
		//imgMenu = loadImg("/menu");
		flujo=new Thread(this);
	   flujo.start();
  	   System.gc();
  	}

      
   int posMenu = -1;
      
   public void paint(Graphics g)
   {        
   	gr = g;
   	gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_SMALL));
   	if (estado==0) Engine();
	      	else if (estado==1) Intro();
	      	else if (estado==2) IniGame();
	      	//else if (estado==6) { Sekhmet.sek.Save();}
	      		//flujo = null;
	      		
	      	else if (estado==11) {fin();}
	      	else if (estado==7) {prota.alive=false;estado = 0;}	      	
	      	else estado = menus.show(gr,currentkey, apretando,w,h);	      	
  		if (posMenu >= 0) posMenu++;
  		if(posMenu>=35){posMenu=35;}
  		//gr.drawImage(imgMenu,-35+posMenu,h-10,20);
      
 	   //pinta finalmente todo lo que tenia el _buffer despues de DESCARGAR el GOM
	   //g.drawImage (_buffer,0,0,Graphics.TOP|Graphics.LEFT);
   }


	private static byte[] a(String s1)
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

	 static int lastsfx = -1;

    public static void Sonido(int s, int c)
    {
    	//return;
    	
    	//if (c == 0) lastsfx = -1;
    	/*if (Sekhmet.sfx )
    	{
    	   if (lastsfx < 0 
    	   || Sekhmet.bp[lastsfx].getState() == Sound.SOUND_STOPPED) 
    	   {
    	   	try {Sekhmet.bp[s].play(c);	     }
    	 	  catch(Exception e){} 
    	   	lastsfx = s;
    	   }
    	 }    */
    }
   
   
    public void ISound(int k, String s)
    {
    	/*try
      {       
    		byte[] b = a(s);
    		Sekhmet.bp[k] = new Sound(b,1);
    		Sekhmet.bp[k].init(b, 1);           
    	}
       catch(Exception exception) { }	*/
    }
    
    public void buildsfx()
    {
    	/* Sekhmet.bp = new Sound[8];
       ISound(0,"024A3A5941A5CDD185CC040143225A0420A2302103102103302104968108288C0840C40840CC084125204206E0420A31021033021049481081B810828B40840C4084125A0420A2302103102103302104968108288C0840C40840CC084125204206E0420A2D021031021049481081B810828CC0841646042092502102D0210514810819810824940840B4084145204206604209290210310210519810818810824A40840C4084146604206E042092502102D0210514810819810824940840B408414520420660420923021029021051881082C8C08412520420620420A31021037000");
       ISound(1,"024A3A5941A5CDD185CC040005206E0000");
       ISound(2,"024A3A5941A5CDD185CC0400071C6606E000");
       ISound(3,"024A3A5941A5CDD185CC040049204E04206A04204E04206A04204E04206A04206E0420A23021049381081A81081381081A81081181081B8108288C0841262000");
       ISound(4,"024A3A5941A5CDD185CC040021206584185184186584186DA720A24849B8198176192000");
       ISound(5,"024A3A5941A5CDD185CC04000F1C620A2B02B049A80000");
       ISound(6,"024A3A5941A5CDD185CC0400151EA2AC26C22C2682CC49C41A2000");       	
       ISound(7,"024A3A59B58DD1BDB99404000B2A4605606206A000");*/
    }

}//END midlet


