package com.mygdx.mongojocs.kinsectors;//////////////////////////////////////////////////////////////////////////
// KInsectors - Nokia Color Version 0.01
// By Carlos Peris
// MICROJOCS MOBILE
//////////////////////////////////////////////////////////////////////////


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.Manager;
import com.mygdx.mongojocs.midletemu.Player;
import com.mygdx.mongojocs.midletemu.VolumeControl;

import java.io.InputStream;
import java.util.Random;

public class Game extends FullCanvas implements Runnable
{
	static byte map[] = null;
	
   public int     w=getWidth();
   //public final int GAMEW = 176;
   //public int     GAMEWIDTH=Math.min(getWidth(),GAMEW);
   public int     h=getHeight();
   private static Graphics gr;
   static boolean        apretando = false;
   public static int    currentkey;
   //static int     offsetx = 0;
   //static int     offx = 0;
   static int     offsety = 0;
   static boolean        faseacabada;
   static int 		fase = 1;   
   static int 		estado = -101;
   static int 		prestado = 1;
   static int largo = 176,alto = 128;
   static int speed;
   MMenu menus;
	Thread         flujo;
	static Image ifons, TempImg = null, all, raices, gusano, imalos, iexplo = null, imenu = null, ifont = null, intro1 =null,intro2=null, ifases = null, tiles;
	static ktipo ktype;
	static int nfc;
	static Random rnd;
	static int MAXBOLAS = 16;
	static unit bolas[];
	static unit item;
	static int puntos;	
	static int MAXEXPLOSIONES = 18;
   static unit explos[];
   static int offraices;
	static unit malos[];
	static int MAXMALOS;
	static int MAXELEMENTS = 5;
	static unit elements[];
	
	
	////////////////////////////////////////////////////////////////////
	// 	MALOS
	////////////////////////////////////////////////////////////////////
	public final static int MBASICO 		= 1;
	public final static int MROTATOR 	= 2;
	public final static int MMEDUSA 		= 3;	
	public final static int MCEFALOPODO = 4;
	public final static int MGUSANO 		= 5;
   public final static int MBOLA 		= 6;
	public final static int MSMEDUSA 	= 7;
	public final static int MSROTATOR 	= 8;
	public final static int MCANYON 		= 9;
	public final static int MTOPTERO 	= 10;
	public final static int MCABEZON 	= 11;
	public final static int MROVELLON 	= 12;
	public final static int MCOCHINILLA	= 13;
	public final static int MCANON	   = 14;
	public final static int MSHADOW	   = 15;
	public final static int MREINA	   = 16;
	public final static int MTENTACULO  = 17;
	public final static int MABEJORRO   = 18;
	
	public final static int XSPEED	   = -1;
	public final static int AITEM	      = -2;
	
	//EXPLOSIONES
	public final static int ESIMPLE 		= 0;
	public final static int ESCOMP 		= 1;
	public final static int ESMEKKA 		= 2;
		
		
	static String consmsg = "                        ";	
	static int conssub;
				
		
	public static void addMsg(String s)
	{
		conssub = consmsg.length()+3;
		consmsg += "..."+s;
	}	
		
	public static void actCon()	
	{
		if (conssub > 0)
		{
			consmsg = consmsg.substring(1);
			conssub--;          
		}
		if (conssub > 16)
		{
			consmsg = consmsg.substring(1);
			conssub--;          
		}
	}
	
	public static void Print(int r, int g, int b, String s, int xx, int yy, int size)
	{
		if (size == 1) gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
		else gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
		gr.setClip(0,0,176,128);
		gr.setColor(0,0,0);				
		gr.drawString(s , xx-1, yy-1, Graphics.HCENTER | Graphics.TOP);				 				         
		gr.drawString(s , xx+1, yy+1, Graphics.HCENTER | Graphics.TOP);				 				         
		gr.setColor(r,g,b);		
		gr.drawString(s , xx, yy, Graphics.HCENTER | Graphics.TOP);				 				         
	}

		
	public byte [] ReadByteArray(String ffile, int bytes)
	{	  
		/*byte [] data=null;
		try
		{
		    int nr=0, cr=0;
		    InputStream is = getClass().getResourceAsStream(ffile);
			data = new byte[bytes];
			cr = is.read(data);
			nr += cr;
			while(nr < bytes)
			{
			    cr = is.read(data, nr, bytes-nr);
			    nr += cr;
			}			
			is.close();
		}		
		catch (Exception e)
		{
			//System.out.println("File Exception: resource:"+ffile+"!\n"+e);
		}
		return data;*/

		//MONGOFIX
		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+ffile.substring(1));
		byte[] data = file.readBytes();

		byte data2[] = new byte[bytes];
		for(int i = 0; i < bytes; i++)
			data2[i] = data[i];

		return data2;
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
	
	public static void blit(int ox, int oy, int w, int h, int x, int y, Image foo)
   {
   	gr.setClip(0, 0,largo, 128); 
      gr.clipRect(x,Game.offsety+y,w,h);	         
	   gr.drawImage(foo,x-ox, Game.offsety+y-oy, Graphics.TOP|Graphics.LEFT);	         
   }
	
	public static void blitg(int ox, int oy, int w, int h, int x, int y, Image foo, Graphics gr_)
	{
		gr_.setClip(0, 0,largo, 128); 
      gr_.clipRect(x,Game.offsety+y,w,h);	         
	   gr_.drawImage(foo,x-ox, Game.offsety+y-oy, Graphics.TOP|Graphics.LEFT);	            
	}
   
	
	public static void blitc(int ox, int oy, int _w, int _h, int x, int y, Image foo)
   {
   	gr.setClip(0, 0,largo, 128); 
      gr.clipRect(x-(_w>>1),y,_w,_h);	         
	   gr.drawImage(foo,x-ox-(_w>>1), y-oy, Graphics.TOP|Graphics.LEFT);	         
   }
	
	
	
	public static void blit2(int ox, int oy, int w, int h, int x, int y, Image foo)
   {
   	gr.setClip(0,0,176,208);		
   	gr.clipRect(x,y,w,h);	        
   	gr.drawImage(foo,x-ox, y-oy, Graphics.TOP|Graphics.LEFT);	         
   }
	
	
	public static void printGFX(String ss,int x, int y, boolean reverse)
	{
		char[] s = ss.toUpperCase().toCharArray();
		int off = 0;
		int linea = 0;
		int col = 0;
		int ll = 0;
		if (reverse) x -=	ss.length()*5;
			
		for (int i = 0; i < ss.length();i++)		
		{
			if (s[i] == ' ') {off +=3;continue;}
			if (s[i] >= 'A' && s[i] <= 'Z') 
			{
				ll = 5;
				linea = 0;
				col = (s[i]-'A')*5;
				if (s[i] > 'M') col++;
				if (s[i] > 'W') {col++;}				
				if (s[i] == 'I') ll = 2;
				if (s[i] == 'W') ll = 7;
				if (s[i] == 'M') ll = 6;
				//off += 5;
			}
			else if (s[i] >= '0' && s[i] <= '9') 
			{
				ll = 5;
				linea = 1;
				col = (s[i]-'0')*5;
			}
			else
			{
				ll = 5;			
				linea = 2;			
				if (s[i] == '�') {col = 45;ll= 6;}
				else col = 0;
			}
			blit2(col,linea*6,ll,6,x+off,y,ifont);
			off += ll;
		}
		   
	}	
	
	
	public void AddWaveBasico(int y)
	{
			int k = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MBASICO)
				{
					malos[i].Activate();
			      malos[i].Auto(largo+k,y+(rnd.nextInt()%32),-200,0);
			      k += 21;
				}
	}		
	
	public void AddWaveMedusa(int y)
	{
			int k = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MMEDUSA)
				{
					malos[i].Activate();					
			      malos[i].Auto(largo+k,y+(rnd.nextInt()%32),0,0);
			      malos[i].numframes = 0;			      			     
			      k += 30;
				}
	}		
	
	int medusa_seq[] = {0,100,-100,200,-200,300,-300,0};
	
	public void AddWaveSMedusa()
	{
			int k = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MMEDUSA)
				{
					malos[i].Activate();					
			      malos[i].Auto(largo,56,-150,medusa_seq[k]);
					malos[i].numframes = 1;			      			     
					malos[i].timing = 10;			      			       
					k++;
				}
	}		
	
	/*
	public void AddCefalopodo()
	{
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MCEFALOPODO)
				{
					malos[i].Activate();					
				}
	}		
	
	
	public void AddGusano()
	{
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MGUSANO)
				{
					malos[i].Activate();					
				}
	}	*/	
	
	public void AddMalo(int g)
	{
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == g)
				{
					malos[i].Activate();					
				}
	}		
	
	
	public void AddWaveRotator()
	{
			int k = 0;
			int dk = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MROTATOR)
				{
					malos[i].Activate();					
			      malos[i].Auto((largo-50)+dk,-16+((i%2)*(alto+16)),-200,150-((i%2)*300));
			      //malos[i].estado = k;			      			     
			      k++;
			      if (k%2 == 0) dk += 48;
				}
	}		
	
	public void AddWaveSRotator()
	{
			//int k = 0;
			int dk = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MROTATOR)
				{
					malos[i].Activate();					
			      malos[i].Auto((largo-50)+dk,-16+((i%2)*(alto+16)),-200,150-((i%2)*300));
			      //malos[i].estado = k;			      			     			      
			      dk += 30;
				}
	}		
	
	
	public void AddWaveToptero()
	{
			int k = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MTOPTERO)
				{
					malos[i].Activate();
			      malos[i].Auto(largo+k,57+(rnd.nextInt()%44),-100,0);
			      k += 7;
				}
	}		
	
	public void AddWaveCabezon()
	{
			int k = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MCABEZON)
				{
					malos[i].Activate();
			      malos[i].Auto(largo+k,60+(rnd.nextInt()%42),-250,0);
			      k += 30;
				}
	}		
	
	
	public void AddCochinilla(int param)
	{
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MCOCHINILLA)
				{
					malos[i].numframes = param;
					malos[i].Activate();					
					return;					
				}
	}		
	
	
	public void AddWaveRovellon()
	{
			int k = 0;
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MROVELLON)
				{
					malos[i].Activate();
			      malos[i].Auto(186+(rnd.nextInt()%82),128+k, -150,-125);			      
			      k += 12;
				}
	}		
	
	public void AddCanyon(int param)
	{
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MCANYON)
				{
					malos[i].Activate();					
					malos[i].estado = param;
					if (param == 1)					
						malos[i].Auto(Game.largo,128-24-18,-200,0);					
					else
					   malos[i].Auto(Game.largo,18,-200,0);					
					return;					
				}
	}
	
	/*public void AddCanon()
	{
			for (int i = 0;i<MAXMALOS;i++)
				if (!malos[i].active && malos[i].tipo == MCANON)				
					malos[i].Activate();									
	}	*/	
	
	//int nmalos = 0;
	
	
	public void AddWave(int tipo, int param)
	{
		switch(tipo)
		{
			case MBASICO: 		AddWaveBasico(param);break;						
			case MMEDUSA: 		AddWaveMedusa(param);break;						
			case MSMEDUSA: 	AddWaveSMedusa();break;									
			//case MCEFALOPODO: AddCefalopodo();break;						
			case MROTATOR: 	AddWaveRotator();break;									
			case MSROTATOR: 	AddWaveSRotator();break;									
			//case MGUSANO:   	AddGusano();break;
			case MTOPTERO:   	AddWaveToptero();break;
			case MCABEZON:   	AddWaveCabezon();break;
			case MCOCHINILLA: AddCochinilla(param);break;
			case MROVELLON:   AddWaveRovellon();break;
			case MCANYON:     AddCanyon(param);break;
			case XSPEED:      speed = param;break;
			//case MCANON:      AddCanon();break;
			case MREINA:      malos[0].Activate();
									malos[1].Activate();
									//malos[1].estado = 0;
									//malos[2].Activate();
									break;
			case AITEM:      AddItem(largo-1,80,param);break;
			default:AddMalo(tipo);
		}
	}
	
	public void Pinta1()
	{
		gr.translate(0,28);
		//SCROLL
	   sc.ScrollRUN(scx, 0);				
	   sc.ScrollIMP(gr);	
	   //RAICES
	   //gr.setClip(0, 0,largo, alto); 
	   blit(0, 0, 250, 36, offraices, 0, raices);
	   blit(0, 0, 250, 36, offraices+250, 0, raices);
	   blit(0, 38, 250, 50, offraices, 128-50, raices);
	   blit(0, 38, 250, 50, offraices+250, 128-50, raices);	   
	   if (offraices <= -250) offraices = 0;
	   offraices-=2;
	   for (int i = 0;i < MAXMALOS;i++) 		
 			if (malos[i].active)	malos[i].blit(gr);					      			
		
		
	}
	
	int vx1, vx11,vx2,vx22,vx3,vx33,vx4,vx44;
	public void Pinta2()
	{		if (speed < 3)  gr.translate(0,28);
			else   gr.translate(0,27+(nfc%3));
			//gr.setClip(0, 0,largo, alto); 
			int vx11 = 0,vx22= 0,vx33 = 0,vx44 = 0;
		for(int i = 0;i< speed;i++)
		{
			vx1-=4;
			if (vx1 <= -w) vx1 = 0;
			vx11 = vx1+w;
			if (vx11 > w) vx11 = vx11-w;
			vx2-=2;
			if (vx2 <= -w) vx2 = 0;
			vx22 = vx2+w;
			if (vx22 > w) vx22 = vx22-w;
			vx3-=1;
			if (vx3 <= -w) vx3 = 0;
			vx33 = vx3+w;
			if (vx33 > w) vx33 = vx33-w;
			if (nfc%2 == 0) vx4--;
			if (vx4 <= -w) vx4 = 0;
			vx44 = vx4+w;
			if (vx44 > w) vx44 = vx44-w;
		}	
			
		//gr.drawImage(Game.piram,vx1, 90-85, Graphics.LEFT | Graphics.TOP);	     			
	   //gr.drawImage(Game.piram,vx11, 90-85, Graphics.LEFT | Graphics.TOP);	     			
		
	   
	   blit(0, 48, 176, 33, vx4, 48, raices);	   
	   blit(0, 48, 176, 33, vx44, 48, raices);	   
	   blit(0, 19+14, 176, 15, vx3, 19+14, raices);	   
		blit(0, 19+14, 176, 15, vx33, 19+14, raices);	   
		blit(0, 81, 176, 15, vx3, 128-19-14-15, raices);	   
		blit(0, 81, 176, 15, vx33, 128-19-14-15, raices);	   	   
	   blit(0, 19, 176, 14, vx2, 19, raices);	   
		blit(0, 19, 176, 14, vx22, 19, raices);	   
		blit(0, 96, 176, 14, vx2, 128-19-14, raices);	   
		blit(0, 96, 176, 14, vx22, 128-19-14, raices);	   
		
		PintaRailes(offraices);
		///////////////
		if (nfc > 210 && nfc < 210+432) offraices+=2;
		if (nfc > 1325 && nfc < 1325+236) offraices+=2;		
		for (int i = 0;i < speed;i++)				
			if (nfc > 2700)offraices+=2;
		//if (nfc > 2700 && nfc < 2700+260) offraices+=2;
		if (offraices>>3 >= 233) offraices -=8;
		
		//if (offraices >= (llin-23)<<3) offraices = 0;
		for (int i = 0;i < 3;i++) 		
 			if (malos[i].active)	malos[i].blit(gr);					      			
		
		blit(0, 0, 176, 19, vx1, 0, raices);	   
		blit(0, 0, 176, 19, vx11, 0, raices);	   
		blit(0, 110, 176, 19, vx1, 128-19, raices);	   
		blit(0, 110, 176, 19, vx11, 128-19, raices);	   
	   
	   for (int i = 3;i < MAXMALOS;i++) 		
 			if (malos[i].active)	malos[i].blit(gr);					      			
		
	   //if (offraices <= -250) offraices = 0;
	   //offraices-=2;
	   //sc.ScrollRUN(scx, 0);				
	   //sc.ScrollIMP(gr);	
	   
	}
	
	static int llin = 256;				
		
	
	public void PintaRailes(int pos)
	{
		for(int yt = 0;yt < 16;yt+=2)
		for(int xt = 0;xt < 24;xt+=2)
		{
			int x = xt<<3;
			int y = yt<<3;
			int tile = map[(pos>>3)+(yt*llin)+xt];
			if (tile == 1 || tile == 6|| tile == 7) tile = 0;
			if (tile == 3 || tile == 8|| tile == 9) tile = 2;
			if (tile == 5 || tile == 10|| tile == 11) tile = 4;
			if (tile == 13 || tile == 18|| tile == 19) tile = 12;
			if (tile == 15 || tile == 20|| tile == 21) tile = 14;			
			if (tile == 17 || tile == 22|| tile == 23) tile = 16;
			if (tile == 25 || tile == 30|| tile == 31) tile = 24;
			if (tile == 27 || tile == 32|| tile == 33) tile = 26;
			if (tile == 29 || tile == 34|| tile == 35) tile = 28;
			if (tile !=14) blit(218+((tile%6)<<3), (tile/6)<<3, 16, 16, x-(pos%16), y, imalos);	   				
		}
	}
	
	public void Pinta3()
	{
		gr.translate(0,28);
		//SCROLL
		offsety = (ktype.y+8)-(128/2);	         
	   if (offsety > 144-128) offsety = 144-128;
	   if (offsety < 0) offsety = 0;
	               
	   sc.ScrollRUN(scx, offsety);				
	   offsety = -offsety;
	   sc.ScrollIMP(gr);	
	   //RAICES
	   for (int i = 0;i < MAXMALOS;i++) 		
 			if (malos[i].active)	malos[i].blit(gr);					      			
	}
	
	
	public void EngineFase1()
	{
		if (off_fase1[punt_fase] == nfc-suma)		
		{
			suma += off_fase1[punt_fase];
			AddWave(tip_fase1[punt_fase], par_fase1[punt_fase]);
			punt_fase++;
		}
		scx++;
		//if (scx == 85*8 ) scx = 8;
	}   
	   
	   
	int vagon = 0;   
	int vagdelay = 0;
	public void EngineFase2()
	{
		if (vagdelay > 0) vagdelay--;
		if (vagdelay <= 0)
		{
			int tile = map[(offraices>>3)+23];
			if (tile == 16 || tile == 22)
			{
				malos[vagon].Activate();
				malos[vagon].Auto(Game.largo+8,-7,-200,200);
				vagdelay = 8;
			}			
			for (int i = 0;i < 3;i++)		
		      if (!malos[i].active) vagon = i;		
			tile = map[(offraices>>3)+23+(llin*15)];
			if (tile == 18 || tile == 12)
			{
				malos[vagon].Activate();
				malos[vagon].Auto(Game.largo+8,128+7,-200,-200);
				vagdelay = 8;
			}						
		}
		for (int i = 0;i < 3;i++)		
		   if (!malos[i].active) vagon = i;
		
		if (off_fase2[punt_fase] == nfc-suma)		
		{
			suma += off_fase2[punt_fase];
			AddWave(tip_fase2[punt_fase], par_fase2[punt_fase]);
			punt_fase++;
		}
		if ((ktype.y > 128-18-8 || ktype.y < 18) && ktype.inmunidad <= 0) ktype.tag -= 9;
		
		
		/*malos[0].active = true;
		malos[0].x = 40;
		malos[0].y = 40;
		*/
		/*malos[MAXMALOS-1].ashape = fistro;
		if (currentkey == Canvas.FIRE && dfistro == 0)
		{
			fistro++;
			fistro = fistro%7; 
			dfistro = 10;
		}
		if (dfistro > 0) dfistro--;*/
	}   
	//int fistro = 0;
	//int dfistro = 0;
	
	public void BlitExplosiones()
	{
		for (int i = 0;i < MAXEXPLOSIONES;i++) 		
		{
			if (explos[i].active && explos[i].estado == ESMEKKA)
			{
				switch(explos[i].ashape)
				{
					case 1: blit(65+32, 1, 30, 28, explos[i].x-6, explos[i].y-6, iexplo);break;	   			
					case 0:
					case 2: blit(68+32, 30, 24, 23, explos[i].x-3, explos[i].y-3, iexplo);break;	   			
					case 3: blit(70+32, 54, 20, 19, explos[i].x-1, explos[i].y-1, iexplo);break;	   			
					case 4: blit(74+32, 74, 11, 10, explos[i].x+4, explos[i].y+4, iexplo);break;	   			
				}
			}
			else
		   explos[i].blit(gr);
		}
	}	
	
	
	public void EngineFase3()
	{
		if (off_fase3[punt_fase] == nfc-suma)		
		{
			suma += off_fase3[punt_fase];
			AddWave(tip_fase3[punt_fase], par_fase3[punt_fase]);
			punt_fase++;
		}
		
		if (malos[1].tag > 0) 
		{
			if (scx < ((632+256)*8)-(22*8)-70) scx+=speed;
		}
		else if (scx < ((632+256)*8)-(22*8)) scx+=speed;
		
		
		int loc = (ktype.x+4)/8+(((ktype.y+4)/8)*(632+256))+(scx/8);
		if ( ktype.inmunidad <= 0)
		switch(map[loc])
		{
			case 2:
			case 7:
			case 17:
			case 23:
			case 44:
			case 47:
			case 49:
			case 55:
			case 81:
			case 83:
			case 87:
			case 96:
			case 97:
			case 112:
			case 113:
			case 108:
			case 111: ktype.tag -= 4;break;
			case 42:
			case 43:
			case 58:
			case 59:
			case 74:
			case 75:
			case 90:
			case 91:
			case 89:
			case 88:
			case 103:
			case 104:
			case 105:
			case 106:
			case 107: break;
			default: ktype.tag -= 10;break;									//soundPlay(4,1);
		}
	
		AddItem(largo-1, 80,nfc%9);
      //if (loc < 0 || loc >= mapaw*mapah) return 0;
      
	} 
	 
	   
	static int scx = 0;   
	int suma = 0;
	
	public void Engine()
	{
		if (nfc == 1) soundPlay(1,1);
		nfc++;		
		//offx++;
		//if (offx > 1500) offx = 0;
	
	if (faseacabada)
		{
			ktype.inmunidad = 20;
			ktype.ashape = (nfc)%8;
			ktype.x += 5;
			abrir = false;
			if (lifeframe < 34) lifeframe = 34;
			if (ktype.x > Game.largo && lifeframe > 70)
			{
				soundPlay(2,1);			
				lifeframe = 0;
				if (fase == 3) Inifinal();
				if (fase == 2) Inifase3();
				if (fase == 1) Inifase2();				
			}
		}
		
		
		UpdateSprite();
		
		
		
		for (int i = 0;i < MAXMALOS;i++)
		   if (malos[i].active) 
		   {
		   malos[i].Update(ktype.x,ktype.y,true);
		   if (malos[i].isCollidingWith(ktype) && ktype.inmunidad <= 0) {ktype.tag-=11;}//soundPlay(4,1);		   
		   }
		//if (offx == 150)	AddItem(largo-1,80,3);
		/*if (offx == 50)	AddItem(largo-1,80,ktype.IBOLA);		
		if (offx == 250)	AddItem(largo-1,80,ktype.IBOLA);
		if (offx == 450)	AddItem(largo-1,80,ktype.IBOLA);
		if (offx == 750)	AddItem(largo-1,80,ktype.IWAVE);
		if (offx == 950)	AddItem(largo-1,80,ktype.IWAVE);
		*/
		//if (offx == 60)	AddItem(largo-1,80,2);
		
		switch(fase)
		{
			case 1: EngineFase1();break;
			case 2: EngineFase2();break;
			case 3: EngineFase3();break;
		}
		
	
   	
	   UpdateItem();
		UpdateBolas();	
		UpdateExplosiones();	
			
			
		//Antes estaba dfaseacabada aki
						
	   //PINTAR	   	
	  
      gr.setColor(0,0, 0);
      gr.fillRect(112,128+28+17,192,40);      
      int green = 255;
      int t = ktype.tag;
      green = ((t*3)>>1)+8;
      if (green > 255) green = 255;
      if (green < 0) green = 0;
      gr.setColor(255,green, 0);
      gr.fillRect(112,17+128+28,ktype.tag>>1,15);//((ktype.tag*64)/ktipo.energy),15);                  
      green = 124+(ktype.sarma>>1);
      if (green > 255) green = 255;
      if (green < 0) green = 0;          
      gr.setColor(green,green, 0);
      if (ktype.sarma >= 256 && nfc%2 == 0) gr.setColor(255,255,255);
      gr.fillRect(118,33+128+28,ktype.sarma>>2,15);
                  
                  
      //MENUS
      gr.setClip(0, 0,176, 28); 
      gr.setColor(203, 10, 0); 
      gr.fillRect(6,5,138,14);
      printGFX(consmsg.substring(0,Math.min(23,consmsg.length())),10,7,false);
      
      gr.setClip(0, 0,176, 28);
      gr.drawImage(imenu,0, 0, Graphics.TOP|Graphics.LEFT);	         	   
	   gr.setClip(0, 128+28,176, 53); 
      gr.drawImage(imenu,0, 128+28-29, Graphics.TOP|Graphics.LEFT);
      //if (nfc%2 == 0) 
      actCon();	
	   printGFX(""+puntos,38,177,true);
	   printGFX(""+ktype.vidas,38,184,true);
	   switch(ktype.tipodisp)
	   {	   	
	   	case ktipo.DWAVE: 
	   	case ktipo.DWAVE2:blit2(40,184,8,8,84,195,all);break;
	   	case ktipo.DLASER: 
	   	case ktipo.DLASER2:blit2(48,184,8,8,84,195,all);break;
	   	case ktipo.DGUSPIRA: 
	   	case ktipo.DGUSPIRA2:blit2(48,192,8,8,84,195,all);break;
	   	default:blit2(40,192,8,8,84,195,all);break;
	   }
	   
	   //printGFX("HOLAAAB",10,200);
	   //printGFX("HOLAAAC",10,170);
	   
	   switch (fase)
	   {
	   	case 1: Pinta1();break;
	   	case 2: Pinta2();break;
	   	case 3: Pinta3();break;
	   }
      
	   ktype.blit(gr);
		
		for (int i = 0;i < MAXBOLAS;i++) 		
		   bolas[i].blit(gr);		   
		   
		BlitExplosiones();   
		
	   for (int i = 0;i < MAXELEMENTS;i++) 		
		   elements[i].blit(gr);
	   item.blit(gr);
	   if (ktype.tag <= 0)  
	   {
	   	//ktype.tag = 0;
	   	AddExplosion(ktype.x, ktype.y, ESMEKKA, 3, 4);	     
			ktype.Revive();	   
	   }
	   if (nfc < 50) Print(255,255,255,  Lang.stages[fase], 88, 60,1); 
	      
	   if (!faseacabada && nfc>26 && !rush) 
	      lifeframe = 0;
	   if (faseacabada && ktype.x < 128) lifeframe = 0;   
	   Quadri();
	   
	   //Print(255,255,255,  ""+nmalos, 88, 60,1); 
	   if (rush == true && !faseacabada) lifeframe += 4;
	   if (rush && lifeframe > 26 && !abrir){estado=4;currentkey = 0;rush = false;soundPlay(0,0);}
	   if (rush && lifeframe > 32 && abrir){rush = false;}
	   //if (faseacabada) Quadri();
	   //gr.setClip(0,0,128,128);
	   //gr.drawString("Score "+ktype.tag , 4, 4, Graphics.LEFT | Graphics.TOP);				 				   
      
      //Print(255,255,255,  ""+malos[0].x, 10, 10,1); 
	}
	
	//FASES
	int punt_fase = 0;
	int off_fase1[] = {100    , 150		, 130		, 150		  , 10          , 90 	 , 120		  , 50          ,150    , 150	    , 20         ,60     , 120     , 20            ,10     ,100        , 100		,200        , 150         , 50       ,20         ,   50            , 50       ,150		    , 100	      , 20           ,110	    , 90	     , 130	   , 40          ,70	    , 120	 , 50              ,250 	 , 120      , 200           , 400     			, -1};
	int tip_fase1[] = {MBASICO, MBASICO , MMEDUSA, MSROTATOR, AITEM       , MBASICO, MCEFALOPODO, AITEM       ,MBASICO, MSMEDUSA, AITEM      ,MBASICO, MROTATOR, AITEM         ,MBASICO,MCEFALOPODO, MMEDUSA ,MCEFALOPODO, AITEM       , MROTATOR ,MCEFALOPODO,   AITEM         , MSROTATOR,MCEFALOPODO , MCEFALOPODO , AITEM        ,MSMEDUSA, MROTATOR, MGUSANO , AITEM       ,MMEDUSA, MBASICO, AITEM           ,MROTATOR, MSROTATOR , AITEM         , AITEM};
	int par_fase1[] = {40     , 70		, 60		, 0		  , ktype.ILASER, 70     , 0		     , ktype.IMISIL,40     , 0       , ktype.IBOLA,70     , 0       , ktype.IGUSPIRA,30     ,0          , 70		,0          , ktype.IBOMBA, 0        ,0		    ,   ktype.IPOWERUP, 0        ,0 			 , 0		      , ktype.IESCUDO,0	      , 0       , 0       , ktype.IMISIL,30     , 70     , ktype.IGUSPIRA  ,0 		 , 0        , ktype.IGUSPIRA, ktype.IVIDA};

	int off_fase2[] = {50          ,50      , 200        ,20      ,40         ,220     , 160     , 40          ,80         , 140    ,20      , 20     , 80       , 70            ,60      , 300        , 10         , 150         ,50       , 130     , 10          ,40     ,  80     , 10          ,120      , 60      , 60     , 20         ,40     , 60     , 60       , 20          ,80      , 100           ,200     , 120    ,60     ,60     ,20         ,40     ,60     ,60     ,60     ,130    ,10    ,20         ,10    ,200        ,200        ,200           ,200      ,200        , -1};
	int tip_fase2[] = {AITEM       ,MCABEZON, AITEM      ,MCABEZON,AITEM      ,MTOPTERO, MCABEZON, AITEM       ,MCOCHINILLA, MCANYON, MCANYON, MCANYON, MROVELLON, AITEM         ,MTOPTERO, MCOCHINILLA, MCOCHINILLA, AITEM       ,MROVELLON, MTOPTERO, AITEM       ,MCANYON, MCABEZON, AITEM       ,MTOPTERO , MCANYON , MCANYON, AITEM      ,MCANYON, MCANYON, MROVELLON, AITEM       ,MCABEZON, AITEM         ,MTOPTERO, MCANYON,MCANYON,MCANYON,MCOCHINILLA,MCANYON,MCANYON,MCANYON,MCANYON,MCANON ,XSPEED,AITEM      ,XSPEED,AITEM      ,AITEM      ,AITEM         ,MTOPTERO ,AITEM};
	int par_fase2[] = {ktype.IMISIL,0       , ktype.IWAVE,0       ,ktype.IWAVE,0       , 0       , ktype.IBOLA ,-1         , 1      , 2      , 1      , 0        , ktype.IPOWERUP,0       , -1         , 1          , ktype.IBOMBA,0        , 0       , ktype.ILASER,1      , 0       , ktype.ILASER,0        , 1       , 2      , ktype.IWAVE,1      , 2      , 0        , ktype.IMISIL,0       , ktype.IPOWERUP,0       , 1      , 2     ,1      ,1          ,2      ,1      ,2      ,1      ,0      ,2     ,ktype.IVIDA,3     ,ktype.IWAVE,ktype.IWAVE,ktype.IPOWERUP,0        ,ktype.IWAVE};

	int off_fase3[] = {30    , 30    , 30    , 30    , 1272   , 9    , 9     , 10    , 1    ,   -1};
   int tip_fase3[] = {XSPEED, XSPEED, XSPEED, XSPEED, XSPEED, XSPEED, XSPEED, XSPEED, MREINA};
   int par_fase3[] = {2     , 3     , 4     , 5     , 4     , 3     , 2     , 1     , 0};
	

	int cvar;
	public void Inifinal()
	{		
		estado = -99;
		nfc = 0;
		abrir = true;
		cvar = 148;
	}

	public void fin()
	{
		nfc++;
			gr.translate(0,28);
		if ( nfc>26 && !rush) 
	      lifeframe = 0;
	   //if (faseacabada && ktype.x < 128) lifeframe = 0;   
	   UpdateExplosiones();
	   if (nfc == 80) Game.AddExplosion(75, 55, ESMEKKA, 2 , 1);	   
	   if (nfc == 100) Game.AddExplosion(85, 65, ESMEKKA, 2 , 1);	   
	   if (nfc == 120) Game.AddExplosion(60, 50, ESMEKKA, 2 , 1);	   
	   if (nfc >= 150 && nfc <= 175)  Game.AddExplosion(75, 55, ESMEKKA, 5 , 25);	   
	   if (nfc == 181)
	   {
	   	lifeframe = 0;
	   	Game.AddExplosion(75, 60, Game.ESIMPLE, 1, 4);	      	  
	      Game.AddExplosion(75, 60, Game.ESIMPLE, 10, 13);	      	
	      Game.AddExplosion(75, 60, Game.ESIMPLE, 1, 4);	      	   
	      Game.AddExplosion(75, 60, Game.ESIMPLE, 10, 13);	      		      
	   }
   	
	   
		gr.setColor(0,0,0);
		gr.fillRect(0,0,176,128);
		for(int i = 0;i < 5;i++)
      	for(int j = 0;j < 5;j++)            		
      		blit(173,((i+j)%4)*4,5,5,((j*364)+(i*67))%176,((j*54)+(i*243))%128,intro1);      		      				
      if (nfc < 170) blit(0,0,67,67,40,30,intro1);//PLANETA
   	BlitExplosiones();   	
   	
   	if (nfc > 180) cvar--;                   
      
      for (int i = 0 ;i < Lang.finals.length;i++)      Print(255,255,200,Lang.finals[i], 88, cvar+(i*16),0);
      for (int i = 0 ;i < Lang.credits.length;i++)      Print(255,255,200,Lang.credits[i], 88, 150+cvar+(i*16),0);
      
      if (nfc > 700 && nfc%30 < 15)      Print(255,255,255,"INSERT COIN", 88, 58,1);
      
      Quadri();	   
      
	   if (rush == true) lifeframe += 4;
	   if (rush && lifeframe > 40 && !abrir){estado = 1;prestado = 1;currentkey = 0;rush = false;}
	   if (rush && lifeframe > 32 && abrir){rush = false;}	   
		if (nfc == 1000 || (nfc > 700 && apretando)) {rush = true;abrir = false;lifeframe = 0;}
		//if (nfc >= 1030) {estado = 1;prestado = 1;}
	
      		
	}

	public void Inifase1()
	{
		//for (int i = 0;i < MAXMALOS;i++) malos[i].Deactivate();
		fase = 1;	
		
		Inifase();
		//abrir = true;
		rush = false;
		//faseacabada = false;
		//scx = 0;
		//punt_fase = 0;
		//nfc = 0;
		////quadrifirst = true;
		//byte map[] = ReadByteArray("/mapa1.map", 1344);		   
		//byte tempmap[] = ReadByteArray("/mapa1.map", 1472);		   		
		//map = new byte[1472+(26*16)];
		map = ReadByteArray("/mapa1.map", 1472);		   		
		//map = loadRemote("http://kung-foo.dhs.org/zener/mapa1-map.png");  				
		//for (int i = 0;i<1;i++) map[400+i] = tmap[i];
		//int k = 0;
		/*
		for (int i = 0;i<16;i++)
		   for (int j = 0;j<107;j++)
		   {
		   	 if (j < 84)
		   	 {
		   	 	map[(i*108)+j] = tempmap[(i*84)+j];
		   	   //map[(i*84)+25+j] = tempmap[(i*25)+j];
		   	 }
		   	 else
		   	 {
		   	 	 //map[(i*84)+j] = tempmap[(i*25)+j];
		   	    map[(i*108)+j] = tempmap[(i*84)+j-84];
		   	 }		   	 
		   }
		for (int i = 0;i<16;i++)
		   for (int j = 0;j<26+92;j++)
		   {
		   	 if (j < 92)
		   	 {
		   	 	map[(i*118)+j] = tempmap[(i*92)+j];//108 y 84
		   	   //map[(i*84)+25+j] = tempmap[(i*25)+j];
		   	 }
		   	 else
		   	 {
		   	 	 //map[(i*84)+j] = tempmap[(i*25)+j];
		   	    map[(i*118)+j] = tempmap[(i*92)+j-92];
		   	 }		   	 
		   }
		   */		
		tiles = loadImg("/tile1");
		sc = new Scroll();
		sc.ScrollINI (largo, 128);
		sc.ScrollSET (map, 92, 16,  tiles, 22);//108
	
	   raices = loadImg("/raices");
	   imalos = loadImg("/malos-raices");	   
	   gusano = loadImg("/gusano2");
	   
	   MAXMALOS = 1+1+8*3;   
	   malos = new unit[MAXMALOS];
	   int cmalos = 0;
	   malos[cmalos++] = new cangrejo(imalos, 44, 41, 1, 0, 0);
	   for (int i = cmalos;i < cmalos+8;i++)
	      malos[i] = new basico(imalos, 20, 14, 2, 17, 80);
	   cmalos += 8;   
	   for (int i = cmalos;i < cmalos+8;i++)
	      malos[i] = new medusa(imalos, 27, 20, 4, 17, 0);
	   cmalos += 8;      
	   for (int i = cmalos;i < cmalos+8;i++)
	      malos[i] = new rotator(imalos,16 , 16, 7, 0, -2);
	   cmalos += 8;      
	   malos[cmalos++] = new gusano(gusano, 44, 41, 2, 0, 0);
	   //Sonido(1,15);
	}

	public void Inifase()
	{
		Game.addMsg(Lang.stages[fase]);
		for (int i = 0;i < MAXBOLAS;i++) bolas[i].Deactivate();
		for (int i = 0;i < MAXMALOS;i++) malos[i].Deactivate();
		for (int i = 0;i < ktipo.MAXDISPAROS;i++) ktype.shots[i].Deactivate();
		KInsectors.cfase = Math.max(KInsectors.cfase, fase);
		
		faseacabada = false;
		abrir = true;
		scx = 0;
		ktype.x = 10;
		ktype.y = 58;
		offraices = 0;
		punt_fase = 0;
		nfc = 0;
		suma = 0;		
		speed = 1;   	
		item.Deactivate();
	}


	public void Inifase2()
	{
		fase = 2;

		Inifase();
		//faseacabada = false;
		//abrir = true;
		//scx = 0;
		//ktype.x = 10;
		//offraices = 0;
		//punt_fase = 0;
		//nfc = 0;
		MAXMALOS = 3+8+6+12+2+1+2;
		//suma = 0;
		
		map = ReadByteArray("/mapa2.map", 4096);		   
		//tiles = loadImg("/railes");
		tiles = null;
		sc = null;
		//sc = new Scroll();
		//sc.ScrollINI (largo, 128);
		//sc.ScrollSET (map, 256, 16,  tiles, 6);
	
	   imalos = loadImg("/malos-nivel2");
	   gusano = loadImg("/canon");
	   raices = loadImg("/nivel2");
	   
	   malos = new unit[MAXMALOS];
	   for (int i = 0;i < 3;i++)	   
	      malos[i] = new canyon(imalos, 16, 16, 0, 0, 0);
	   for (int i = 3;i < 3+8;i++)	   
	      malos[i] = new toptero(imalos, 24, 19, 4, 175, 0);
	   for (int i = 3+8;i < 3+8+6;i++)	   
	      malos[i] = new cabezon(imalos, 29, 16, 2, 129, 48);
	   for (int i = 3+8+6;i < 3+8+6+12;i++)	   
	      malos[i] = new rovellon(imalos, 17, 16, 5, 201,0);
      for (int i = 3+8+6+12;i < 3+8+6+12+2;i++)	   
	      malos[i] = new cochinilla(imalos, 16, 16, 3, 158, 0);	 	   
	   malos[3+8+6+12+2] = new canon(gusano, 73, 70, 0, 0, 0);	 	   
	   malos[3+8+6+12+2+1] = new shadow(imalos, 16, 16, 3, 158, 48);	 	   
	   malos[3+8+6+12+2+2] = new shadow(imalos, 16, 16, 3, 158, 48);	 	   
	}
	
	
	
	
	public void Inifase3()
	{
		//faseacabada = false;
		fase = 3;
		Inifase();
		
		//abrir = true;
		//scx = 0;
		//ktype.x = 10;
		//offraices = 0;
		//punt_fase = 0;
		//nfc = 0;
		MAXMALOS = 3+5;
		//suma = 0;
		final int MAPAL = 632+256;
		//System.out.println("1");
		
		map = new byte[MAPAL*18];
		//System.out.println("2");
		
		map = ReadByteArray("/mapa3.map", 15984);		   
		//speed = 0;
		/*
		byte tempmap[] = ReadByteArray("/3001.map", 2880);		   
		for (int i = 0;i<18;i++)
		   for (int j = 0;j<160;j++)		   
		  	 	map[(i*MAPAL)+j] = tempmap[(i*160)+j];		   	   		   	 		  		
		tempmap = ReadByteArray("/3002.map", 2880);		   
		for (int i = 0;i<18;i++)
		   for (int j = 0;j<160;j++)		   
		  	 	map[(i*MAPAL)+j+160] = tempmap[(i*160)+j];		   	   		   	 		  		
		tempmap = ReadByteArray("/3003.map", 5040);		   
		for (int i = 0;i<18;i++)
		   for (int j = 0;j<280;j++)		   
		  	 	map[(i*MAPAL)+j+320] = tempmap[(i*280)+j];		  		  	 			  	 	
		tempmap = ReadByteArray("/3004.map", 4608);		   
		for (int i = 0;i<18;i++)
		   for (int j = 0;j<256;j++)		   
		  	 	map[(i*MAPAL)+j+600] = tempmap[(i*256)+j];		  		  	 			  	 	
		tempmap = ReadByteArray("/3final.map", 576);		   
		for (int i = 0;i<18;i++)
		   for (int j = 0;j<32;j++)		   
		  	 	map[(i*MAPAL)+j+600+256] = tempmap[(i*32)+j];		  
		  	 	
		  	 	//MARCA
		RecordStore rs; 
		 
		 try { 
		 rs = RecordStore.openRecordStore("fistro", true); 
		 
		 if (rs.getNumRecords() == 0) 
		 { 
		    rs.addRecord(map, 0, map.length); 
		 } else { 
		    rs.setRecord(1, map, 0, map.length); 
		 } 
		 rs.closeRecordStore(); 
		} catch(Exception e) {}   	 	
		*/
		tiles = loadImg("/techdungeon");
		//sc = null;
		
		sc = new Scroll();
		sc.ScrollINI (largo, 128);
		sc.ScrollSET (map, MAPAL, 18,  tiles, 16);
	
		raices = loadImg("/pinzas");
		gusano = loadImg("/reina");	   
	   imalos = loadImg("/reina2");
		
		
	   malos = new unit[MAXMALOS];
	   malos[0] = new reina(gusano, 162, 143, 1, 0, 0);
	   malos[1] = new tentaculo(raices, 96, 100, 1, 0, 0);
	   malos[2] = new tentaculo(raices, 96, 100, 1, 0, 0);	   
	   for(int i = 3; i < MAXMALOS;i++)	   
	   	malos[i] = new abejorro(imalos, 21, 14, 6, 141, 0);	   
	
		
	}
	
	static boolean abrir = false, rush;
	
	
	void LoadIntro()
	{ 
		if (TempImg == null)
		{
			menus.opmenu = 0;
	      TempImg = loadImg("/caratula");
	      tintro = 0;
	      scx = 0;			
			tiles = loadImg("/techdungeon");
			intro1 = loadImg("/intro1");
			intro2 = loadImg("/intro2");
			map = ReadByteArray("/intro.map", 2304);		   
			sc = new Scroll();
			sc.ScrollINI (largo, 128);
			sc.ScrollSET (map, 128, 18,  tiles, 16);				      
	      rush = false;
	      faseacabada = true;
	      abrir = false;
	      lifeframe = 0;
	      soundPlay(0,0);
	      one = true;
	      //Sonido(0,15);
	      lifeframe = 0;
			for(int i = 0;i < 12;i++)
	      for(int j = 0;j < 9;j++)	      
	      	qmat[i][j] = 4;	         		      
	   
	   }
	   //estado = 2;
	}
	
	int tintro = 0;
	static boolean one;
	void Intro()
	{		
		
		/*
		String s[] = {"2003: A probe of alien origin was",
						  "reported entering the solar",
						  "system. The probe carried a",
						  "message of peace.",
						  "",
						  "2028: For 25 years we attempted",
						  "to establish communications with",
						  "the alien world but with no",
						  "success.",
						  "Then the aliens come to Earth.",
						  "Our starships tried a first",
						  "contact and were destroyed."
						};		
		String s2[] = {
						  "We had no choice but to respond",
						  "with force.  The war began.",
						  "",
						  "2032: We suffered severe",
						  "setbacks. Our defeat is imminent",
						  "Our last hope is to strike at the",
						  "heart of the aliens power,",
						  "the Queen of Insectors.",
						  "We have a prototype fighter to",
						  "penetrate the alien defenses",
						  "and destroy the Queen."
						  };
		*/				
		if (puntos > 0)
		{
			if (KInsectors.sc.isHighScore(puntos)) {
				KInsectors.kins.enterHighScore(puntos);
			}
			puntos = 0;			
		}	   
		
	   LoadIntro();	   	   
	   tintro++;  
	       
	   
	   gr.setColor(0,0,0);		
      gr.fillRect(0, 0, w, h);          		
	   gr.translate(0,28);		            
	   
		if (tintro < 100)      
      {
      	//blitc(0,0,160,100,88,14,TempImg);
      	//gr.drawImage(TempImg,88, 14, gr.TOP|gr.HCENTER);	         
      	gr.drawImage(TempImg,88, 18, gr.TOP|gr.HCENTER);	         
      	gr.setClip(0, -28,176, 28-(100-tintro)); 
      	gr.drawImage(imenu,0, -28-(100-tintro), Graphics.TOP|Graphics.LEFT);	         	   
	   	gr.setClip(0, 128+(100-tintro),176, 53+(100-tintro)); 
      	gr.drawImage(imenu,0, 128-29+(100-tintro), Graphics.TOP|Graphics.LEFT);	         	   
      }
      else
      {
      	//		sc.ScrollRUN(scx-100, 0);				
      	//	sc.ScrollIMP(gr);	
      
      	gr.setClip(0, -28,176, 28); 
      	gr.drawImage(imenu,0, -28, Graphics.TOP|Graphics.LEFT);	         	   
	   	gr.setClip(0, 128,176, 53); 
      	gr.drawImage(imenu,0, 128-29, Graphics.TOP|Graphics.LEFT);	         	   		   
      	
      	
      	if (tintro > 180 && tintro < 500)
      	{
      		//MINISEQ1      		
      		for(int i = 0;i < 5;i++)
      		for(int j = 0;j < 5;j++)            		
      			blit(173,((i+j)%4)*4,5,5,((j*364)+(i*67))%176,((j*54)+(i*243))%128,intro1);      		      				
      		blit(0,0,67,67,14+(tintro>>6),10,intro1);//PLANETA
      		switch (tintro>>2)
      		{
      			case 110:blit(0,0,56,66,110,128-66,intro2);break;
      			case 111:blit(56,0,59,97,105,128-97,intro2);break;      		
      			case 112:blit(115,0,84,112,85,128-112,intro2);break;      		
      			case 113:blit(199,0,111,112,61,128-112,intro2);break;      		
      			case 114:blit(407,0,61,112,70,128-113,intro2);break;
      			case 115:
      			case 116:
      			case 117:
      			case 118:
      			case 119:	blit(9*((tintro>>2)-115),66,9,9,64,16,intro2);break;      		
      		}      		
      		//gr.setColor(255,200,0);
      		//gr.setClip(0,0,176,128);
      		//gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
      		if (tintro > 200 && tintro < 320)
      		{
      			Print(255,255,100,Lang.intro1[0],88,86,0);
      			Print(255,220,40,Lang.intro1[1],88,100,0);
      			Print(255,200,0,Lang.intro1[2],88,114,0);
      			//gr.drawString(Lang.intro1[0] , w/2, 86, Graphics.HCENTER | Graphics.TOP);				 				          
      			//gr.drawString(Lang.intro1[1] , w/2, 100, Graphics.HCENTER | Graphics.TOP);				 				        
      			//gr.drawString(Lang.intro1[2] , w/2, 114, Graphics.HCENTER | Graphics.TOP);				 				          
      		}
      		if (tintro > 320 && tintro < 440)
      		{
      			Print(255,255,100,Lang.intro1[3],88,86,0);
      			Print(255,220,40,Lang.intro1[4],88,100,0);
      			Print(255,200,0,Lang.intro1[5],88,114,0);      			
      			//gr.drawString(Lang.intro1[3] , w/2, 86, Graphics.HCENTER | Graphics.TOP);				 				          
      			//gr.drawString(Lang.intro1[4] , w/2, 100, Graphics.HCENTER | Graphics.TOP);				 				        
      			//gr.drawString(Lang.intro1[5] , w/2, 114, Graphics.HCENTER | Graphics.TOP);				 				          
      		}
      	//if (tintro%2 == 0)scx++;	
      	}      	   
      	else if (tintro > 600 && tintro < 950)
      	{
      		//MINISEQ2
      		for(int i = 0;i < 3;i++)
      		for(int j = 0;j < 3;j++)            		
      			blit(173,((i+j)%4)*4,5,5,((j*367)+(i*57))%176,((j*34)+(i*243))%128,intro1);      		      				
      		
      		blit(0,93+13,138,36,0,128-22-(tintro>>6),intro1);//PLANETA
      		switch (tintro>>2)
      		{
      			case 216:blit(468,72,30,39,146,10,intro2);break;
      			case 217:blit(468,17,80,94,96,10,intro2);break;
      			case 222:
      			case 223:
      			case 224:
      			case 225:
      			case 226:blit(9*((tintro>>2)-222),66,9,9,92,101,intro2);break;      	      			
      		}
      		if (tintro >= 872 && tintro < 872+23)
      		{
      			int t = (tintro - 872)<<2;
      			blit(468,17+t,80,94-t,96,10+t,intro2);
      		}
      		//gr.setColor(255,200,0);
      		//gr.setClip(0,0,176,128);
      		//gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
      		if (tintro > 620 && tintro < 740)
      		{
      			Print(255,255,100,Lang.intro2[0],88,4,0);
      			Print(255,220,40,Lang.intro2[1],88,18,0);
      			Print(255,200,0,Lang.intro2[2],88,32,0);      			
      			//gr.drawString(Lang.intro2[0] , w/2, 4, Graphics.HCENTER | Graphics.TOP);				 				          
      			//gr.drawString(Lang.intro2[1] , w/2, 18, Graphics.HCENTER | Graphics.TOP);				 				        
      			//gr.drawString(Lang.intro2[2] , w/2, 32, Graphics.HCENTER | Graphics.TOP);				 				          
      		}
      		if (tintro > 740 && tintro < 860)
      		{
      			Print(255,255,100,Lang.intro2[3],88,4,0);
      			Print(255,220,40,Lang.intro2[4],88,18,0);
      			Print(255,200,0,Lang.intro2[5],88,32,0);      			
      			//gr.drawString(Lang.intro2[3] , w/2, 4, Graphics.HCENTER | Graphics.TOP);				 				          
      			//gr.drawString(Lang.intro2[4] , w/2, 18, Graphics.HCENTER | Graphics.TOP);				 				        
      			//gr.drawString(Lang.intro2[5] , w/2, 32, Graphics.HCENTER | Graphics.TOP);				 				          
      		}      			
      		//if (tintro%2 == 0)scx++;
      	}
      	else
      	{      		
      		sc.ScrollRUN(scx-100, 0);				
      		sc.ScrollIMP(gr);	
      		gr.setColor(0,0,0);
      		gr.setClip(0,0,176,128);
      		scx+=2;
		   }
      	if (tintro < 142)
      	{      		
      		gr.fillRect(0,0,176,64-((tintro-110)*2));
      		gr.fillRect(0,64+((tintro-110)*2),176,64-((tintro-110)*2));      		
      		blitc(0,0,160,46,88,64-46-Math.max((tintro-110)*2,0),TempImg);
      		blitc(0,46,160,54,88,64+Math.max((tintro-110)*2,0),TempImg);
      		//blitc(0,0,160,50,88,64-50-((tintro-10)*2),TempImg);
      		//blitc(0,50,160,50,88,64+((tintro-110)*2),TempImg);
      	}
      	/*else
		   {	
		   	nfc++;
		   	sc.ScrollRUN(scx-64, 0);				
		   	sc.ScrollIMP(gr);	
		   }*/
		   //ktype.ashape = (nfc)%8;
		   //ktype.x = 60;
		   //ktype.y = 60;
		   //ktype.blit(gr);		   
		}   		   
		if (tintro > 1130 || (apretando && (currentkey == -6 || currentkey == Canvas.KEY_STAR)))
			rush = true;		
		if (rush)
		{
		//	abrir = false;
			Quadri();
		   if (lifeframe == 26) {estado = 4;currentkey = 0;menus.timing = 0;}
		}
		
	}
	
	Scroll sc;
	/*
	public Image loadImage(String uurl)
	{
		try{		
		   //Connector conn = (Connector)Connection.open(uurl);
		   DataInputStream dis = Connector.openDataInputStream(uurl);
		   int len = dis.available(); 
		   System.out.println(len);
		   byte value[] = new byte[len];
		   dis.read(value);
		   Image png = Image.createImage(value, 0, len);          
		   return png;		   
		}
		catch(Exception e){}
		return null;
	}*/
	
	/*
	public byte[] loadRemote(String uurl)
	{
		try{
		   String connectString = uurl;
		   ContentConnection hc = null;
		   DataInputStream dis = null;		   
		   hc = (ContentConnection)Connector.open(connectString, Connector.READ, false);		   
		   dis = hc.openDataInputStream();
		   int len = (int)hc.getLength();		   
		   byte value[] = new byte[len];
		   int i = dis.read(value);
		   ByteArrayOutputStream ba = new ByteArrayOutputStream();
		   int off = 0;
		   while (i != -1)
		   {
		       ba.write(value, 0, i);
		       off += i;
		       i = dis.read(value);             
		   }
		   dis.close();				
		   //Image png = Image.createImage(ba.toByteArray(), 0, ba.toByteArray().length);          		   
		   //return png;		   
		   return ba.toByteArray();
		}
		catch(Exception e){}
		return null;
	}*/
	
	static Game joc;	
		
	public void IniGame()
	{
		TempImg = null;
		System.gc();
		estado = 0;
		prestado = 0;
		ktype.vidas = 3;
		ktype.tag = ktype.energy;
		ktype.sarma = 0;
		ktype.cadencia = 6;
		ktype.tipodisp = ktipo.DLASER;
		ktype.subdisp = ktipo.DNONE;
		puntos = 0;		
		//if (lifeframe == 0)
//	   	Inifase1();
	   //else	   
	   	switch (lifeframe)
	   	{	   		
	   		case 2:Inifase2();break;
	   		case 3:Inifase3();break;
	   		default:Inifase1();break;
	   	}	   	
	   //Sonido(1,20);
		//raices = loadRemote("http://kung-foo.dhs.org/zener/mapa1-map.png");  				
		//if (raices == null) System.out.println("es nulo :(");
	}
	
	 public void UpdateExplosiones()   
    {
    	//System.out.println("0");
      
    	for (int i = 0; i < MAXEXPLOSIONES; i++)
         if (explos[i].active)
         {
         	explos[i].incy-=1;
         	explos[i].Update(0,0,true);
         	if (explos[i].timing%2 == 0)         	
         	   if (explos[i].ashape++ == 7) 
         	   {	if (i%2 == 0 || explos[i].timing > 20)
         	   	  explos[i].Deactivate();         	
         	   	else 
         	   	  explos[i].ashape = 0;
         	   }
         	//System.out.println("1");
         	
         	if (explos[i].estado == ESMEKKA)
         	{
         		explos[i].ashape++;
         		//try {					   	
         		if (explos[i].ashape == 3) AddExplosion(explos[i].x+(rnd.nextInt()%explos[i].tag), explos[i].y+(rnd.nextInt()%explos[i].tag), ESMEKKA,explos[i].numframes-1 , explos[i].tag);	      	   		          		
	   			//}catch(Exception e){System.out.println(e);}   
         		if (explos[i].ashape == 5) explos[i].Deactivate();         		
         	} 
      
      
         }
      for (int i = 0; i < MAXELEMENTS; i++)
         if (elements[i].active) 
         {
         	elements[i].Update();
         	elements[i].ashape++;
				if (elements[i].ashape == elements[i].numframes) elements[i].Deactivate();
         }
         //System.out.println("2");
    }
	
	
	public static void AddExplosion(int x, int y, int tipo,int ci, int cf)
	{
		if (ktype.tag > 0) joc.soundPlay(5,1);
		switch (tipo)
		{			
			case ESCOMP:
				for (int j=0;j<12;j++)
				{
					int i = UnitLibre(explos, MAXEXPLOSIONES);
					explos[i].Auto(x+rnd.nextInt()%20,y+rnd.nextInt()%20,rnd.nextInt()%200,rnd.nextInt()%400);
					explos[i].desp = (ci+(j%(cf-ci)))*8;
					//explos[i].width = 8;
					//explos[i].height = 8;
					explos[i].estado = tipo;
				}
				break;
			default:			   
				for (int j=0;j<6;j++)
				{
					int i = UnitLibre(explos, MAXEXPLOSIONES);
					explos[i].Auto(x,y,rnd.nextInt()%200,rnd.nextInt()%400);
					explos[i].desp = (ci+(j%(cf-ci)))*8;
					explos[i].estado = tipo;
					//explos[i].width = 8;
					//explos[i].height = 8;
				}
				break;			
			case ESMEKKA:
				if (ci < 0) return;
				int i = UnitLibre(explos, MAXEXPLOSIONES);
				explos[i].Auto(x,y,0,0);	
				explos[i].estado = tipo;	
				explos[i].numframes = ci;
				explos[i].tag = cf;
				
				//explos[i].width = 8;
				//explos[i].height = 8;	
				break;	
		}		
	}

	public static void AddBala(int x, int y)
	{
		boolean trobat = false;
		int i = 0;
		while (i < MAXELEMENTS && !trobat) 
		{
			if (!elements[i].active)
			{
				trobat = true;
				elements[i].Activate();
				elements[i].setPosition(x,y);
			}
			i++;
		}
	}
	
	
	public void UpdateItem()
	{
		if (!item.active) return;
		item.Update(0,0,true);
		item.y += Trig.sin((int)item.timing<<1)>>5;
		if (item.x < -8) item.Deactivate();	
		if (ktype.isCollidingWith(item))
		{
			ktype.Upgrade(item.ashape);
			item.Deactivate();
		}   
	}
	
	public static void AddItem(int x, int y, int tipo)
	{
		if (item.active) return;
		item.Activate();
		item.ashape = tipo;
		item.Auto(x,y,-125,0);		
	}
	
	public static void Dispara(int xx,int yy)
	{
	      int dy;
	      int i = UnitLibre(bolas, MAXBOLAS);
	      if (yy > ktype.y) dy = -250;else dy = 250;   	   
	      bolas[i].Auto(xx, yy + 8, -400, dy);	   
	}
	
	public static void Dispara(int xx,int yy, int ang)
	{
	      int dx, dy;
	      dx = Trig.cos(ang)>>2;
	      dy = Trig.sin(ang)>>2;
	      int i = UnitLibre(bolas, MAXBOLAS);
	      bolas[i].Auto(xx, yy + 4, dx, dy);	   
	}

	public static void DisparaR(int xx,int yy, int ang)
	{
	      int dx, dy;
	      dx = Trig.cos(ang)>>1;
	      dy = Trig.sin(ang)>>1;
	      int i = UnitLibre(bolas, MAXBOLAS);
	      bolas[i].Auto(xx, yy + 4, dx, dy);	   
	}


	public static void DisparaO(int xx,int yy)
	{
	      int dy;
	      int dx;
	      int i = UnitLibre(bolas, MAXBOLAS);
	      if (yy > ktype.y) dy = -150;else dy = 150;   	   
	      if (xx > ktype.x) dx = -150;else dx = 150;   	   
	      bolas[i].Auto(xx, yy + 8, dx, dy);	   
	}
	
	
	
	public void UpdateBolas()   
   {
      for (int i = 0; i < MAXBOLAS; i++)
      {
         if (bolas[i].active)
         {
         	 //inmunidad == 0 &&
             //if ( bolas[i].isCollidingWith(ktype)) ktype.tag -= 12; 
             if (ktype.isCollidingWith(bolas[i])&& ktype.inmunidad <= 0) {ktype.tag -= 11; }//soundPlay(4,1);
             //{Muerte();bola[i].Deactivate();return;}
   	       //bolas[i].Update(ktype.x, ktype.y);   	         
   	       bolas[i].Update(0,0,true);   	         
   	       bolas[i].active = !bolas[i].out;   
         }
         //if (special > 0 && bola[i].isCollidingWith(bomba)) bola[i].Deactivate();	          
      }
   }
   
	
   public void run()
   {
   	long GameMilis;
		int  GameSleep;

		estado = -101; //MONGOFIX

	   while (estado!=6)
	   {				 	      	
	   	GameMilis = System.currentTimeMillis();			
		
	   	repaint();
			serviceRepaints();
			GameSleep=((30)-(int)(System.currentTimeMillis()-GameMilis));
			if (GameSleep<1) {GameSleep=1;}

			try	{
				flujo.sleep(GameSleep);
		
		} catch(java.lang.InterruptedException e) {}

	      	
	      	/*
	      		      try
	      {

	      	repaint();
	         serviceRepaints();
	      
	      	if (fase == 3 && scx < 830*8)
	      	{
	      		//flujo.sleep(1);           
	      	}
	      	else{	      
	      	  if (nfc%2 == 0) flujo.sleep(15);           
	      	  else flujo.sleep(16);           
	      	}
	      	//flujo.sleep(10);           
         }catch(java.lang.InterruptedException e){}
         //System.gc();
         */
	   }	
	   flujo = null;
	   KInsectors.kins.destroyApp(true);
	}

   static int realkey = 0;
   
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
   }

	
	static int n1, n2, n3;
	static boolean ender = false;
	static boolean turrican = false;
	static boolean chani = false;
	
	public static void cheat(int keycode)
	{
		byte cheat1[]={3,3,6,6,3,3,3,7,7,7,-6};
		if(cheat1[n1]==-6){ender = true;n1 = 0;}
		if (cheat1[n1] == keycode-48) n1++;
		else{n1 = 0;}
		
		byte cheat2[]={8,8,8,7,7,7,7,7,7,4,4,4,2,2,2,2,6,6,-6};
		if(cheat2[n2]==-6){turrican = true;n2 = 0;}
		if (cheat2[n2] == keycode-48) n2++;
		else{n2 = 0;}
		
		byte cheat3[]={2,2,2,4,4,2,6,6,4,4,4,-6};
		if(cheat3[n3]==-6){chani = true;n3 = 0;}
		if (cheat3[n3] == keycode-48) n3++;
		else{n3 = 0;}
	}

	
   
   private void UpdateSprite()
   {
      int xdir = 0, ydir = 0;
      boolean firing = false;
      if (apretando)
    	   switch(currentkey)	
    	   {
              case Canvas.UP:
              case Canvas.KEY_NUM2: ydir = -1;break;
              case Canvas.DOWN: 
              case Canvas.KEY_NUM8: ydir = 1; break;
              case Canvas.LEFT: 
              case Canvas.KEY_NUM4: xdir = -1;break;
              case Canvas.RIGHT: 
              case Canvas.KEY_NUM6: xdir = 1; break;
              case Canvas.KEY_NUM1: ydir = -1; xdir = -1;break;
              case Canvas.KEY_NUM3: ydir = -1; xdir = 1; break;
              case Canvas.KEY_NUM7: ydir = 1;  xdir = -1;break;
              case Canvas.KEY_NUM9: ydir = 1;  xdir = 1; break;
              case Canvas.FIRE:firing = true; break;
              case Canvas.KEY_NUM0 : {ktype.autofire = !ktype.autofire; currentkey = 0;}break;
              case Canvas.KEY_POUND:if (nfc > 30 && !faseacabada && ender) faseacabada = true; currentkey = 0;lifeframe = 0;break;              
              case -6:
              case Canvas.KEY_STAR : if (!rush && !faseacabada) {rush = true;abrir = false;lifeframe = 0;}break;                                  
           }                      
           ktype.Update(xdir, ydir, firing);
           menus.opmenu = 0;
   }


	public void Init()
	{
		rnd = new Random();
      menus = new MMenu();//gr);      
		soundCreate();
		
	   all = loadImg("/nave");
	   iexplo = loadImg("/explosiones");
	   imenu = loadImg("/menu");   
	   //iquad = loadImg("/quad");
	   ifont = loadImg("/font");
	   ifons = new Image();
	   ifons._createImage(176, 128); //MONGOFIX
	   Graphics gg = ifons.getGraphics();
	   for(int i = 0;i < 12;i++)
	      for(int j = 0;j < 9;j++)	      
	      	blitg(40, 0, 16, 25, -8*(j%2)+i*16, -12+j*18, all, gg);	   	         
		ifases = new Image();
		ifases._createImage(96, 32);
		gg = ifases.getGraphics();	      	
	   tiles = loadImg("/raices");
	   blitg(74, 2, 32, 32, 0, 0, tiles, gg);	   	         
	   tiles = loadImg("/nivel2");
	   blitg(39, 10, 32, 32, 32, 0, tiles, gg);	   	         
	   tiles = loadImg("/techdungeon");
	   blitg(32, 16, 32, 32, 64, 0, tiles, gg);	   	         
	   //iselect = loadImg("/select");
	   
	   //for (int i = 10;i < 15;i++)   
	   //   malos[i] = new unit(gusano, 45, 50, 1, 0, 0, 5);
	   //BOLAS
	   bolas = new unit[MAXBOLAS];
	   for (int i = 0;i< MAXBOLAS;i++)
	      bolas[i] = new unit(all,4,4,1,8,122,0);
	   //Explosiones
	   explos = new unit[MAXEXPLOSIONES];
	   for (int i = 0;i< MAXEXPLOSIONES;i++)
	      explos[i] = new unit(iexplo,8,8,8,0,0,0);
	   elements = new unit[MAXELEMENTS];
	   for (int i = 0;i< MAXELEMENTS;i++)
	      elements[i] = new unit(all,8,8,3,17,80,0);
	   ktype = new ktipo(all, 16, 14, 0, 0, 0);	   
	   item = new unit(all,8,8,12,32,104,0);//12 items	   
		estado = 1;   	
	}   
   
   public Game()
 	{     
 	    joc = this;
 		//try{
		//   foo=Image.createImage("/coma5.png");
		//   }catch (Exception e) {}
		
	   	
       flujo = new Thread(this);
	   flujo.start();
  	   System.gc();
  	}

	static int lifeframe = 0;
	static int qmat[][] = new int[12][9];	
	
	
	public static void Quadri()
	{		
		for(int i = 0;i < 12;i++)
	      for(int j = 0;j < 9;j++)
	      {
	      	if (qmat[i][j] < 4) 
	         	blit(40, qmat[i][j]*25, 16, 25, -8*(j%2)+i*16, -12+j*18, all);	   	         
	      }
	   //quadrifirst = false;   
	   for(int i = 0;i < 12;i++)
	      for(int j = 0;j < 9;j++)
	         if ((Math.abs(i-6) < lifeframe/2) && (Math.abs(j-4) < lifeframe/2))	         
	         {
	            if (abrir)	            
	               {if (qmat[i][j] < 4) qmat[i][j]+=1;}
	            else
	               if (qmat[i][j] > 0) qmat[i][j]-=1;	            	         	            
	         }
	   lifeframe += 1;
	   /*
	   if (lifeframe > 34)
	   {
	   	estado = 0;
	   	lifeframe = 0;
	   	for(int i = 0;i < 25;i++)
	      	for(int j = 0;j < 25;j++)
	   			qmat[i][j] = 0;
	   }*/
	}
	
	/*
   public void IniLife()   
   {   	
   	gr.translate(0,28);
		//SCROLL
	   sc.ScrollRUN(scx, 0);				
	   sc.ScrollIMP(gr);	
	   //RAICES
	   gr.setClip(0, 0,largo, alto); 
	   blit(0, 0, 250, 36, offraices, 0, raices);
	   blit(0, 0, 250, 36, offraices+250, 0, raices);
	   blit(0, 38, 250, 50, offraices, 128-50, raices);
	   blit(0, 38, 250, 50, offraices+250, 128-50, raices);	   
	   quadrifirst = true;
	   Quadri();	   	   	
   }*/

   //static boolean quadrifirst;  
      
   public void paint (Graphics g)
   {        
   	gr = g;
   	//gr.setColor(0,0,0);		
      //gr.fillRect(0, 0, w, h);           
   	//gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_SMALL));
   	if (estado==0) Engine();
	   else if (estado==1) Intro();
	   else if (estado==-101){
	   	TempImg = loadImg("/caratula");
	   	gr.setColor(0,0,0);		
	   	gr.setClip(0, 0, w, 208);          		
      	gr.fillRect(0, 0, w, 208);          		
	   	gr.translate(0,28);		            	   
			gr.drawImage(TempImg,88, 18, gr.TOP|gr.HCENTER);	                 
			TempImg = null;
			estado = -102; 	   	
	   }
	   else if (estado==-102) Init();
	   else if (estado==2) IniGame();//{gr.translate(0,28);Quadri();IniGame();}
	   else if (estado==-99) fin();
	   //else if (estado==-100) IniLife();
	   //else if (estado==11) {fin();}
	   else 
	   {
	   	gr.setClip(0, 0,176, 28); 
      	gr.setColor(203, 10, 0); 
      	gr.fillRect(6,5,138,14);
	  		Game.printGFX(Game.consmsg.substring(0,Math.min(23,Game.consmsg.length())),10,7,false);	
	   	gr.setClip(0, 0,176, 28);
      	gr.drawImage(imenu,0, 0, Graphics.TOP|Graphics.LEFT);	         	   
	   	gr.setClip(0, 128+28,176, 53); 
      	gr.drawImage(imenu,0, 128+28-29, Graphics.TOP|Graphics.LEFT);
	   	estado = menus.show(gr,currentkey, apretando,w,h); 	   	   	   	
	   }
   }


	
	
  
   
   public static int UnitLibre(unit vec[], int max)
   {
       int i = 0;
       while (i < max) 
          if (!vec[i].active) {vec[i].Activate();return i; }
          else i++;
       return 0;
   }
   
   

// *******************
// -------------------
// Sound - Engine
// ===================
// *******************

static Player[] Sonidos;

static int SoundOld=-1;
static int SoundTime;


public void soundCreate()
{

	
	Sonidos = new Player[7];
	Sonidos[0] = SoundCargar("/maintheme.mid");	
	Sonidos[1] = SoundCargar("/startlevel.mid");	
	Sonidos[2] = SoundCargar("/passlevel.mid");	
	Sonidos[3] = SoundCargar("/powerup.mid");	
	Sonidos[4] = SoundCargar("/toque.mid");	
	Sonidos[5] = SoundCargar("/explosion.mid");	
	Sonidos[6] = SoundCargar("/lose.mid");	
	

}

public Player SoundCargar(String Nombre)
{


	Player p = null;

	try
	{
	    InputStream stream = getClass().getResourceAsStream(Nombre);
		p = Manager.createPlayer( Nombre ,"audio/midi");
		p.realize();
		p.prefetch();	
	}
	catch(Exception ex) {ex.printStackTrace();}

	return p;
}



// -------------------
// Sound SET
// ===================

int stemps;

public void soundPlay(int Ary, int Loop)
{
        if (!KInsectors.sfx) {return;}
    	
		if (Loop==0) {Loop--;}

		if (SoundOld!=-1) {soundStop();}

		try
		{
    		VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
    		if (Sonidos[Ary] == null) return;
    		Sonidos[Ary].setLoopCount(Loop);
    		Sonidos[Ary].start();
		}
		catch(Exception exception) {exception.printStackTrace();}

		SoundOld=Ary;
	
}



// -------------------
// Sound RES
// ===================

public void soundStop()
{

    if (SoundOld == -1) return;
	try
	{
	Sonidos[SoundOld].stop();
	}
	catch(Exception exception) {exception.printStackTrace();}

	SoundOld=-1;

}






}//END midlet


