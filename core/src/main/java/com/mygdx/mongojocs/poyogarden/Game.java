package com.mygdx.mongojocs.poyogarden;//////////////////////////////////////////////////////////////////////////
// PoyoP - Nokia Color Version 0.01
// By Carlos Peris
// MICROJOCS MOBILE
// SCROLL 007+SCROLL ZENER
//////////////////////////////////////////////////////////////////////////


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
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
    //MARI
    mariposa mari[] = new mariposa[4];
    //
    
    int sx, sy ;

    static Scroll sc;
    Scores scores;
	public static int MAXTX;
	public static int MAXTY;
	static Image tiles, items, mitems, imarco, ititulo;
    private static Graphics gr;
    private static DirectGraphics dgr;
   
    static boolean        apretando = false;
    public static int    currentkey, rkey;
    static int     offsetx = 0;
    static int     offx = 0;
    static int     offsety = 0;
    //boolean        faseacabada;
    static int 		fase;   
    static int 		estado = 1;
    static int 		prestado = 1;

	public final static short IEXIT 	= 0;	
	public final static short ITIEMPO 	= 1;	
	public final static short IBOTAS 	= 2;	
	public final static short IBOTAS2 	= 3;	
	public final static short I4DIRS 	= 4;	
	public final static short IFREEZE 	= 5;	
	public final static short IVIDA 	= 6;	
	public final static short IPERF 	= 7;	
	public final static short IREGALO 	= 8;	
	public final static short IREVERSE = 9;	
	public final static short IFRUTA1 	= 10;	
	public final static short IFRUTA2 	= 11;	
	public final static short IFRUTA3 	= 12;	
	public final static short IFRUTA4 	= 13;	
	public final static short IFRUTA5 	= 14;	
	public final static short IINMUNIDAD = 15;	
	
	//static Sound bp[];
    MMenu menus;
	Thread         flujo;
	Image TempImg = null;
	//, TempImg2,TempImg3,TempImg4;
	static Image all = null;
	static unit poyo;
	 unit item;
	final static int dx = 4;
	static int nfc = 0;
	int efc;
	static Random rnd = new Random();
	 final static int MAXBOLAS = 8;
	final static int MAXBALAS = 4;
	 unit bolas[];
	static unit balas[];
	static int puntos;	
    static byte map[] = null;
    static byte [] pcor, acor;
    //final static int sw = 176;
    //final static int sh = 208;
    final static int sw = 176;
    final static int sh = 208-16;
    static int cw = sw;
    static int ch = sh;
    //final static int sw = 96;
    //final static int sh = 65;
	
	final static int     w=176;   
    final static int     h=208; 
	
	static int largo = sw,alto = sh;	
	unit malo[];	
	
	 int MAXMALOS;
	
	final static int SIMPLE = 0;
	final static int MULTIPLE = 1;
	final static int HIELO = 1;
	final static int MARTILLO = 2;
	int tipobola;
	int numbola; 		
	static int tiempo;
	int vidas;
	int citem;
	int reverse;
	int initiempo;
    static int lastsfx = -1;
	
	//MOViL DEPENDNT
	final static int MAXESPACIO = 18;
	final static int FRASEY = 16;
	final static int FRASEOFFY = 32;
	
	
		
	public final static short TFREEZE			= 100;		
		
	//TILES
	public final static short QUEMADO			= 2;
	public final static short AGUJERO			= 4;	
	public final static short ARBOL				=166;
	public final static short ARBOL2			=228;
	public final static short PIEDRA			=236;
	public final static short SEMIPIEDRA		=230;
	public final static short HIERBA1			=238;
	public final static short HIERBA8			=14;
	public final static short AGUADE		  	=164;
	public final static short AGUAAB		  	=165;
	public final static short AGUAAR		  	=180;	
	public final static short AGUAIZ		  	=181;
	public final static short TELEPORT			=234;
	public final static short PARALISIS		=232;
	public final static short QUEBRADIZOL		=32;
	public final static short QUEBRADIZOL1	=64;
	public final static short QUEBRADIZOL2	=96;
	public final static short QUEBRADIZO		=34;
	public final static short QUEBRADIZO1		=66;
	public final static short QUEBRADIZO2		=98;
	public final static short QUEBRADIZOR		=36;
	public final static short QUEBRADIZOR1	=68;
	public final static short QUEBRADIZOR2	=100;
	public final static short EXPLOSION		=12;
	public final static short EXPLOSION1		=12+32;
	public final static short EXPLOSION2		=12+64;
	public final static short EXPLOSION3		=12+96;
	public final static short EXPLOSION4		=12+128;
	public final static short HIELOLR			= 224;	
	public final static short HIELOTD			= 226;	
	public final static short BOMBA			   = 172;	
	public final static String ou1 = "4341524C4F535045524953424F4C4541";
	//public final static short HUEVO				= 8;	
	//public final static short HUEVO1			= 40;	
	//public final static short HUEVO2			= 72;	
	public final static short GUARIDA			= 104;	
		
	final static byte faseTX[] = {32, 24, 64, 48, 26, 48, 26, 36, 40, 30, 26, 36, 48, 40, 40};
	final static byte faseTY[] = {26, 32, 26, 26, 26, 26, 26, 36, 26, 30, 48, 26, 26, 26, 26};
	final static byte malos[][] = {{5,1,1,1,3},{1,1,2,1,3},{3,1,4,1,1,1},{5,5,3,5,4,1},{1,4,1,3},{5,1,1,3},{1,1,1,1},{2,1,1,1,3,3,5,5},{4,1,5,4,3,1},{4,1,3,1}, {1,1,2,2,3,3,3,3},{3,5,1},{1,4,5,1},{4,3,4,4,5},{5,1,1,3,4,4}};
	final static short fasetiempo[] = {400,320,600,250,320,320, 350, 340, 250, 200, 280, 550, 200, 300, 500};
	final static short faseorder[] = {11,0,1,4,8,9,3,5,2,6,7,10,13,12,14};
	
	
	int tteleport[] = new int[2];
	int tguarida[] = new int[10];

    static int RefreshList[] = {228,230,166,236};
    final static int QuebradizoList[] = {QUEBRADIZO1, QUEBRADIZO2, QUEBRADIZOL1, QUEBRADIZOL2, QUEBRADIZOR1, QUEBRADIZOR2};
	final static int ExplosionList[] = {EXPLOSION1, EXPLOSION2, EXPLOSION3};
	final static int HieloList[] = {HIELOLR, HIELOTD, 6, 38, 70, 102};
	final static int AgujeroList[] = {4,128,130,132,160,162,192,194};
	final static int HuevoList[] = {8,40,72};
	final static int QuemadoList[] = {2,232,172};
	final static int ItemList[] = {2,14,234,46,78,110,142,174,206,238};
	final static byte finpy[] = {0,0,0,0,0,2,3,3,4,4,4,5,5,5,5,4,4,4,3,3,2,1,0};
    
	
	int tilecounter;
	int inicio[]= new int[2];
	int ttilecounter;
	
    final static int tcol[] = {PIEDRA, SEMIPIEDRA, ARBOL, ARBOL2};
	final static int tbcol[] = {PIEDRA, SEMIPIEDRA, ARBOL, ARBOL2, 4,128,130,132,160,162,192,194};
	int lado = 0;
	int ffra = 0;
	int anim = 0;
	boolean disparando = false;
	final static byte fireanim[][] ={{0,1,2,3,3,2,0,0},
							{0,1,2,3,3,2,0,0},
							{0,1,2,3,3,2,0,0},
							{3,2,1,0,0,1,3,3}		
							};
	int inmunidad;
    static boolean cheat1 = false;
    static int n1;
    
    public static void cheat()
	{
		byte scheat1[]={7,6,6,6,9,9,9,6,6,6,-6};
		if(scheat1[n1]==-6){cheat1 = !cheat1;n1 = 0;}
		if (scheat1[n1] == rkey-48) n1++;
		else{n1 = 0;}	
	}



	public  void printNum(String ss,int x, int y, boolean reverse)
	{
		char[] s = ss.toCharArray();
		int off = 0;
		int col = 0;
		if (reverse) x -=	ss.length()*5;
			
		for (int i = 0; i < ss.length();i++)		
		{				
				col = (s[i]-'0')*8;
				blit(8,col,8,8,x+off,y,mitems);
				off += 5;
		}		   
		ss = ou1;
	}	


    public static void Print(int r, int g, int b, String s, int xx, int yy, int align)
	{
		gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
		gr.setClip(0,0,w,h);
		//gr.setColor(r/2,g/2,b/2);				
		gr.setColor(r,g,b);		
		gr.drawString(s , xx, yy, align | Graphics.TOP);				 				         
		s = ou1;
	}


    public static void PrintS(int r, int g, int b, String s, int xx, int yy, int align)
	{
		gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,Font.SIZE_SMALL));
		gr.setClip(0,0,w,h);
		//gr.setColor(r/2,g/2,b/2);				
		gr.setColor(r,g,b);		
		gr.drawString(s , xx, yy, align | Graphics.TOP);				 				         
		s = ou1;
	}


    public static void PrintEX(int r, int g, int b, String s, int xx, int yy, int align)
    {
        gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
		gr.setClip(0,0,w,h-16);
		gr.setColor(r,g,b);		
		gr.drawString(s , xx, yy, align | Graphics.TOP);
		s = ou1;
    }


	public void Recoge(int j)
	{
	    StopSound();Sonido(4,1,false);
	    puntos += 50;	    
		switch(j)
		{
		 			case IEXIT: finfase();break;
		 			case ITIEMPO: tiempo += 50;break;
		 			case IBOTAS: citem = IBOTAS;break;
		 			case IBOTAS2: citem = IBOTAS2;StopSound();Sonido(9,1,false);break;
		 			case I4DIRS: numbola = MULTIPLE;break;
		 			case IFREEZE: tipobola = HIELO;break;
		 			case IVIDA: vidas++;break;
		 			case IPERF: tipobola = MARTILLO;break;
		 			case IREGALO: int i = Math.abs(rnd.nextInt()%16);Recoge(i);break;
		 			case IREVERSE: reverse = 200;break;
		 			case IFRUTA1: 
		 			case IFRUTA2: 
		 			case IFRUTA3: 
		 			case IFRUTA4: 
		 			case IFRUTA5: puntos += 200;break;
		 			case IINMUNIDAD: inmunidad = 200;break;
		}
		cogido = j;
		SetHelp(-j);
		item.Deactivate();
	}
	
	int cogido;
	
	public void CargaFase(int fa)
	{
	    puntos += 250;
	    int tfase = faseorder[fa];
		item.Deactivate();
		nfc = 0;
		initiempo = fasetiempo[fa];
		tiempo = initiempo;
		ttilecounter = 100;
		tilecounter = 100;
		MAXTX = faseTX[tfase];
		MAXTY = faseTY[tfase];
		sc.FaseMap = null;
		System.gc();
		for(int i = 0;i < 20;i++) helps[i] = false;
		map = ReadByteArray("/mapa"+tfase+".map", MAXTX*MAXTY);		   
			        		    		    
		sc.ScrollSET (map, MAXTX, MAXTY,  tiles, 16);
		

		int initel = 0;
		int inihuev = 0;
		for(int i = 0; i < map.length;i++)
		{			
			int tile = map[i];
			if (tile < 0)  tile += 256;
			if (tile == TELEPORT) {tteleport[initel++] = i;}
			if (tile == 8) {tguarida[inihuev++] = i;}//8 es HUEVO
		}				

		for (int i = 0; i < MAXTX;i++)  
	   	for (int j = 0; j < MAXTY;j++)
	   	{
	   		if (GetTile(i*8,j*8) == HIERBA8)
	   		{	   			
	   			poyo.x = (i-1)*8;
	   			poyo.y = ((j-1)*8)-8;
	   			inicio[0] = poyo.x;
	   			inicio[1] = poyo.y;	   			
	   		}	   	
	   			
	   	}  	   	  	
	    MAXMALOS = malos[tfase].length;	   
     	malo = new unit[MAXMALOS]; 	     	
     	for(int i = 0;i < MAXMALOS;i++)
     	{	
     	    
     		switch(malos[tfase][i])
     		{
	     		case 2: malo[i] = new malo2(all, 16, 24, 20,4,4);break;
	     		case 3: malo[i] = new malo3(all, 16, 24, 20,8,0);break;
	     		case 4: malo[i] = new malo4(all, 16, 24, 20,12,0);break;
	     		case 5: malo[i] = new malo5(all, 16, 24, 20,16,0);break;
	     		default: malo[i] = new malo1(all, 16, 24, 20,4,0);break;
	  		}	  			  	  		 
	  		malo[i].setCollisionRectangle(2,10,12,12);	  			  		   
	  		if (cheat1 == true) {malo[i].offx = 0;poyo.offx = 16;}
	  		else poyo.offx = 0;
	  }			  
	  //System.gc();
	   for(int i = 0; i < 4;i++) mari[i].active = false;
   	   Sonido(3,1,false);	  
	}

			
	public static void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
	{
		if ((DestX       > cw)
		||	(DestX+SizeX < 0)
		||	(DestY       > ch)
		||	(DestY+SizeY < 0))
		{return;}
	
	
		gr.setClip(0, 0, cw, ch);
		gr.clipRect(DestX, DestY, SizeX, SizeY);
	
		switch (Flip)
		{
		case 0:
		//case 1:
		gr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
		return;
	
		case 1:
		dgr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
		return;
		/*	
		case 2:
		dgr.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
		return;
	
		case 3:
		dgr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
		return;*/
		}
	}

// ----------------------------------------------------------

public static void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
{
	Frame*=(Trozos*6);

	for (int i=0 ; i<Trozos ; i++)
	{
	int DestX=Coor[Frame++];
	int DestY=Coor[Frame++];
	int SizeX=Coor[Frame++];
	if (SizeX==0) {return;}
	int SizeY=Coor[Frame++];
	int Flip=0;
	if (SizeX < 0) {SizeX*=-1; Flip|=1;}
	if (SizeY < 0) {SizeY*=-1; Flip|=2;}
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY,  Flip);
	}
}
/*

public static void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor)
{
	Frame*=6;

	int DestX=Coor[Frame++]+X;
	int DestY=Coor[Frame++]+Y;
	int SizeX=Coor[Frame++];
	int SizeY=Coor[Frame++];
	int Flip=0;
	if (SizeX < 0) {SizeX*=-1; Flip|=1;}
	//if (SizeY < 0) {SizeY*=-1; Flip|=2;}
	int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
	int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}

	ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  DestX,DestY,  Flip);

}
*/

/* no seeeeeeeeee			
	public static void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor)
	{
		Frame*=6;
	
		int DestX=Coor[Frame++];
		int DestY=Coor[Frame++];
		int SizeX=Coor[Frame++];
		if (SizeX==0) {return;}
		int SizeY=Coor[Frame++];
		int SourX=Coor[Frame++]; //if (SourX<0) {SourX+=256;}
		int SourY=Coor[Frame++]; //if (SourY<0) {SourY+=256;}
	
		ImageSET(Img,  SourX+128,SourY+128,  SizeX,SizeY,  X+DestX,Y+DestY);
	}


	public static void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
	{	
		gr.setClip(DestX, DestY, SizeX, SizeY);
		gr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
	}
*/		
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
		ffile = ou1;
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
	    Image im = null;
	    try{
		    im = new Image();
		    im._createImage(s+".png");
		  }catch (Exception e) {}   
		System.gc(); 
		s = ou1; 
	    return im;	  	
	}
	
	
	public static void jode(int corx, int cory)
	{
	    corx = corx / 16;
	    cory = (cory - 16) / 16;
	    if (corx >= sc.FaseX && corx < sc.FaseX + sc.FondoSizeX)
	    if (cory >= sc.FaseY && cory < sc.FaseY + sc.FondoSizeY)
	    {
	        int tm = corx % sc.FondoSizeX;
	        tm += (cory % sc.FondoSizeY)* sc.FondoSizeX;
	        sc.FondoMap[tm] = 0;
	         //System.out.println(tm);
	    }	     
	   
	}
		
	
	public static void blit(int ox, int oy, int _w, int _h, int x, int y, Image foo)
    {
   	    gr.setClip(0, 0,cw, ch); 
        gr.clipRect(x,y,_w,_h);	         
	    gr.drawImage(foo,x-ox, y-oy, Graphics.TOP|Graphics.LEFT);	         
    }
	
	
	public void TestEx(int j, int i)
	{
		if (j<0 || i<0 || j >= MAXTX/2 || i >= MAXTY/2) return;
		int tile = GetTile(j*16,i*16);
		if (inList(tcol,tile)) SetTile(j*16,i*16, 1);//QUEMADO ANTES
		else SetTile(j*16,i*16, 0);//AGUJERO ANTES											
		jode(j*16,i*16);
		
		/*for(int k = 0; k < sc.FondoMap.length;k++)
		        sc.FondoMap[k] = 0;*/
		        /*
		if (i > 0)
		{
		    //System.out.println("! "+          ((i+(offsety/16))*sc.FondoSizeX)         );
		    //System.out.println("+ "+          ((j+(offsetx/16)))         );
		    //*!*!*!!****!!!*****!*!*!*!*!
		    
		    //int kk = (((i-1)+(offsety/16))*sc.FondoSizeX) + (((j)+(offsetx/16)));		    
		         sc.FondoMap[kk] = 0;
		}*/
		
	}
	
	
	
	public void Raster(int ini,int fin, int fc)
	{
	    for (int i = ini; i < fin;i++)
				for (int j = 0; j < MAXTX/2;j++)
				{
					int tile = GetTile(j*16,i*16);
					if (tile < 0) tile +=256;
					if (tile%32 == 14 && tile > 14)
					   SetTile(j*16,i*16,  tile-32);
					else if (fc%16 == 0 && inList(QuebradizoList, tile))
					   SetTile(j*16,i*16,  tile+32);   
					else if (fc%16 == 0 && inList(ExplosionList, tile))
					   {SetTile(j*16,i*16,  tile+32);StopSound();Sonido(10,1,false);}
					else if (fc%16 == 0 && tile == EXPLOSION4)   
					{
					    StopSound();
					    Sonido(2,1,true);
						TestEx(j,i);
						TestEx(j-1,i);
						TestEx(j,i-1);
						TestEx(j-1,i-1);
						TestEx(j+1,i);
						TestEx(j,i+1);
						TestEx(j+1,i+1);
						TestEx(j-1,i+1);
						TestEx(j+1,i-1);
						efc = nfc;				
				    }				   
					else if (tile == 0 && nfc >= efc+4)   
					{					
						SetTile(j*16,i*16, AGUJERO);											
					}
					else if (tile == 1 && nfc >= efc+4)   
					{					
						SetTile(j*16,i*16, QUEMADO);											
					}
					else if (fc%16 == 0 && inList(HuevoList, tile))   
					{
						if (rnd.nextInt()%256 > 128) SetTile(j*16,i*16, tile+32);											   
					}					 
					else if (tile == GUARIDA)
						{
							int k = 0;
							while (tguarida[k] != getMapPos(j*16, i*16)) 							
								k++;							
							if (!malo[k].active) 
							{	
								malo[k].Activate();
								malo[k].x = j*16;
								malo[k].y = (i*16)-8;
								//malo[k].setPosition(j*16,(i*16)-8);
							}					
						}
					else if (inList(QuemadoList, tile)) ttilecounter++;
				}
	}
		
	
	public static void PostBlit(int xx, int yy)
	{
	    int xt = xx / 16;
	    int yt = yy / 16;
	    
	    for(int y = yt+1;y <= yt+3;y++)
		for(int x = xt;x <= xt+1;x++)
		{
			int p = (y*2*MAXTX)+(x*2);
			if (p < 0 || p >= MAXTX*MAXTY) continue;
			int tile = map[p];
			if (tile < 0) tile+=256;
			if (tile%32 >= 16) tile-=16;			
			if (tile%2 == 1) tile--;	
				//blit(8*8, 0, 16, 16, (x*16)+offsetx, (y*16)+offsety-16, tiles);	   	 					
				//System.out.println("1");
			switch(tile)
			{
			    //default:
				case 230:
				case 166:
				case 236:
				case 228: 				
				//System.out.println(""+xt+"-"+yt);
				//((tile%16)<<3), ((tile/16)<<3)-16
				
				
				blit((tile%16)<<3, ((tile/16)<<3)-16, 16, 20, (x*16)+offsetx, (y*16)+offsety-16, tiles);	   	 								
				//blit(8*16, 0, 16, 16, (x*16)+offsetx, (y*16)+offsety-16, tiles);	   	 								
				
				//blit(((tile%16)<<3), ((tile/16)<<3)-16, 16, 16+4, x-((-offsetx)%16), y-((-offsety)%16)-16, tiles);	   	 								
						  break;								
			}
	    }
	}
	
	
	int helpmode;
	int helpplace;
	boolean helps[] = new boolean[20];
	
	public void SetHelp(int valor)
	{
	    if (!PoyoP.help || poyo.muerto == 1 || valor == 0) return;
	    int tempplace = (poyo.x/16)+(((poyo.y+8)/16)*256);
	    if (tempplace == helpplace) return;
	    if (valor > 0)
    	    switch(valor)
    	    {
    	        case 12:      helpmode = 16;break;
    	        case 172:     helpmode = 17;break;
    	        case 234:     helpmode = 18;break;
    	        case 232:     helpmode = 19;break;	            	    
    	    }
	    else helpmode = -valor;
	     	
	         
	    if (helpmode != -1) {
	        if (helps[helpmode] == true) {helpmode = -1;return;}
	        else helps[helpmode] = true;
	        currentkey = 0;apretando = false;poyo.tag = 0;
	        helpplace = tempplace; 
	        }
	    
	}
	
	
	
	public void Help()
	{
	    //dgr.setARGBColor(0x70515111); 
	    gr.setClip(0,0,w,h);
	    
	    //gr.fillArc(3,3, w-6, h-6, 0, 360); 
	    //gr.fillRoundRect(3,3, w-6, h-6, 80, 120); 
	    //gr.fillRect(0,0, w, h);
	    //dgr.drawPixels(shelp, true, 0, w, 0, 0, w, h-16, 0, dgr.TYPE_USHORT_4444_ARGB ); 

	    
	    String frase = menus.onhelp[helpmode];
	    int beg = 0;
	    int tbeg;
	    int espacio = 0;
	    int yy = 0;
	    int tt;
	    
	    while(frase.indexOf(" ", espacio) != -1)
	    {
	        //tbeg = beg;
	        if (frase.indexOf(" ", beg) >= 0) espacio = frase.indexOf(" ", beg);
	        else
	        //{
	        //    Print(255,255,255,frase.substring(beg, frase.length()),sw/2,FRASEOFFY+yy,gr.HCENTER);	        
	            break;
	        //}    
	        //else break;
	        while((tt = frase.indexOf(" ", espacio+1)) > 0 && tt-beg < MAXESPACIO) espacio = frase.indexOf(" ", espacio+1);	        
	        if (espacio <= 0 || espacio == beg) espacio = frase.length();
	        Print(0,0,0,frase.substring(beg, espacio),(sw/2)-1,FRASEOFFY+yy-1,gr.HCENTER);	        
	        Print(0,0,0,frase.substring(beg, espacio),(sw/2)+1,FRASEOFFY+yy+1,gr.HCENTER);	        
	        Print(255,225,225,frase.substring(beg, espacio),sw/2,FRASEOFFY+yy,gr.HCENTER);	        
	        beg = espacio+1;
	        yy+= FRASEY;
	    }
	    Print(0,0,0,frase.substring(beg, frase.length()),(sw/2)-1,FRASEOFFY+yy-1,gr.HCENTER);	        
	        Print(0,0,0,frase.substring(beg, frase.length()),(sw/2)+1,FRASEOFFY+yy+1,gr.HCENTER);	        
	        
	     Print(255,225,225,frase.substring(beg, frase.length()),sw/2,FRASEOFFY+yy,gr.HCENTER);	        
	    
	    
	    if (apretando)
	     {helpmode = -1; apretando = false;currentkey = 0; } 
	}
	
	
	public void Engine()	
	{		
	    //try{
	    cw = sw; ch = sh;	    
	    if (helpmode == -1) 
	    {
    		if (tilecounter == 0)		
    			finfase();
    		
    		if (!item.active && nfc%29 == 19)
    		{
    			item.Activate();
    			int i = Math.abs(rnd.nextInt()%16);
    			if (i == IEXIT && Math.abs(rnd.nextInt()%16)<8) i = Math.abs(rnd.nextInt()%16);
    			item.desp = 3+(i/8);
    			item.ashape = i%8;
    			item.tipo = i;
    			int x = Math.abs(rnd.nextInt()%(MAXTX/2));
    			int y = Math.abs(rnd.nextInt()%(MAXTY/2));
    			item.x = x*16;
    			item.y = y*16;
    			while(!inList(ItemList,GetTile(item.x,item.y)))
    			{
    				x = Math.abs(rnd.nextInt()%(MAXTX/2));
    				y = Math.abs(rnd.nextInt()%(MAXTY/2));
    				item.x = x*16;
    				item.y = y*16;
    			}
    			item.tag = x+(y*MAXTX);			
    		}
    		if (item.active)
    		{
    		 	item.timing++;
    		 	if (item.timing > 200) {item.Deactivate();cogido = -1;}
    		 	if (item.isCollidingWith(poyo))		 	
    		 		Recoge(item.tipo);		 				 	
    		} 	
    		nfc++;
    		if (tiempo >= 0 && nfc%10 == 0) {tiempo--;if (tiempo < 30) {StopSound();Sonido(10,1,false);}}
    		if (tiempo < 0 && poyo.muerto == 0) poyo.muerto = 1;
    		
    		
    		
    		
    		switch(nfc%4)
    		{
    		    
    		    case 0: 
    		            tilecounter = ttilecounter;
    		            ttilecounter = 0;    		            
    		            Raster(0,MAXTY/8, nfc);
    		            break;
    		    case 1:Raster(MAXTY/8,MAXTY/4, (nfc/4)*4);break;
    		    case 2:Raster(MAXTY/4,3*(MAXTY/8),(nfc/4)*4);break;
    		    case 3:Raster(3*(MAXTY/8),MAXTY/2,(nfc/4)*4);break;
    		}
    		
    		
    		    			
    									
    		if (vidas > 0) UpdateSprite();
    		else poyo.timing++;
    		for (int i = 0;i < MAXMALOS;i++) if (malo[i].active) malo[i].Update(poyo);
    		//MARI
    		for (int i = 0;i < 4;i++) mari[i].Update(poyo, tilecounter);
    		//
    		
    		UpdateBolas();
    	}	
		if (poyo.timing > 60) {poyo.timing = 0;estado = 1;Game.prestado=1;}
		
		
		//PINTAR
		//System.out.println("2");
		sc.ScrollRUN(-offsetx, -offsety);					
	    sc.ScrollIMP(gr);	
	    //int mi = MAXTX/2;
	   //System.out.println("3");
	    for(int yt = 0;yt <= (sh/8)+1;yt+=2)
		for(int xt = 0;xt <= sw/8;xt+=2)
		{
			int x = xt<<3;
			int y = yt<<3;
			int p = ((yt-(offsety/8))*MAXTX)+xt-((offsetx)/8);
			if (p < 0 || p >= MAXTX*MAXTY) continue;
			int tile = map[p];
			if (tile < 0) tile+=256;
			int realtile = tile;
			tile = -1;	
			switch (realtile)
			{
					case AGUAIZ:
						tile = (136+32*2)-((nfc/4)%3)*32;
		                //blit(((tile%16)<<3), ((tile/16)<<3), 16, 16, x-((-offsetx)%16), y-((-offsety)%16), tiles);	   	 											
						break;
					case AGUADE:
						tile = 136+((nfc/4)%3)*32;
						//blit(((tile%16)<<3), ((tile/16)<<3), 16, 16, x-((-offsetx)%16), y-((-offsety)%16), tiles);	   	 											
						break;	
					case AGUAAR:
						tile = (138+32*2)-((nfc/4)%3)*32;
						//blit(((tile%16)<<3), ((tile/16)<<3), 16, 16, x-((-offsetx)%16), y-((-offsety)%16), tiles);	   	 											
						break;	
					case AGUAAB:
						tile = 138+((nfc/4)%3)*32;
						//blit(((tile%16)<<3), ((tile/16)<<3), 16, 16, x-((-offsetx)%16), y-((-offsety)%16), tiles);	   	 											
						break;		
			}
			if (tile >= 0) blit(((tile%16)<<3), ((tile/16)<<3), 16, 16, x-((-offsetx)%16), y-((-offsety)%16), tiles);	   	 											
			if (realtile == AGUAIZ || realtile == AGUADE)Checktile(p,yt,x,y);				
						
		}
		//System.out.println("4");
		item.blit(gr);	   
		//System.out.println("5");
		
	    if (inmunidad == 0 || nfc%2 == 0) poyo.blit(gr);
	    //System.out.println("6");
	    for(int i = 0;i < MAXMALOS;i++) malo[i].blit(gr);
	    //MARI
	    for(int i = 0;i < 4;i++) mari[i].blit(gr);
	    //
	    
	    //System.out.println("7");
	    for(int i = 0;i < MAXBOLAS;i++) bolas[i].blit(gr);
	    //System.out.println("8");
	    for(int i = 0;i < MAXBALAS;i++) balas[i].blit(gr);
	    //System.out.println("9");
	    
	    /*	    
		for(int yt = 0;yt <= (sh/8)+2;yt+=2)
		for(int xt = 0;xt <= sw/8;xt+=2)
		{
			int x = xt<<3;
			int y = yt<<3;
			int p = ((yt-(offsety/8))*MAXTX)+xt-((offsetx)/8);
			if (p < 0 || p >= MAXTX*MAXTY) continue;
			int tile = map[p];
			if (tile < 0) tile+=256;
			if (tile%32 >= 16) tile-=16;			
			if (tile%2 == 1) tile--;			
			switch(tile)
			{
				case 230:
				case 166:
				case 236:
				case 228: blit(((tile%16)<<3), ((tile/16)<<3)-16, 16, 16+4, x-((-offsetx)%16), y-((-offsety)%16)-16, tiles);	   	 								
						  break;								
			}			
		}*/
		
	    if (item.active)
		{
			blit(0,item.tipo*8,8,8,w-2-8-8,10,mitems);
			if (Math.abs(poyo.x-item.x) > 48)
			{
				if (item.x < poyo.x) blit(8,80+24,8,8,w-2-8-8-8,10,mitems);
				if (item.x > poyo.x) blit(8,80+16,8,8,w-2-8,10,mitems);
			}	
			if (Math.abs(poyo.y-item.y) > 48)
			{
				if (item.y < poyo.y) blit(8,80,8,8,w-2-8-8,2,mitems);
				if (item.y > poyo.y) blit(8,88,8,8,w-2-8-8,2+8+8,mitems);
			}
		}
		
		if (nfc%10 == 1)
		{
    	    //MARCADORES
    	    ch = h;
    	    gr.setClip(0,0,w,h);
    	    gr.setColor(240,160,48);
            gr.fillRect(0,h-15, w, 14); 
            gr.setColor(240,112,0);
            gr.fillRect(0,h-16, w, 1); 
            gr.fillRect(0,h-1, w, 1); 
            blit(8,14*8,8,8,4,h-12,mitems);printNum(""+vidas,10,h-12,false);
	        blit(0,8,8,8,28,h-12,mitems);printNum(""+tiempo,34,h-12,false);
	        blit(8,15*8,8,8,96+50,h-12,mitems);printNum(""+tilecounter,102+50,h-12,false);
	        
    	   /*if (cogido != -1) 
    	   {
    	        gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL));
    	        gr.setClip(0,0,w,h);
    	        gr.setColor(0xe1e114);			
    	        gr.drawString(menus.sitems[cogido] , 4, 4, Graphics.RIGHT | Graphics.TOP);				 				         
    	   }*/
	       if (reverse > 0)
	       { 
	            blit(0,72,8,8,66,h-12,mitems);
	            printNum(""+reverse/10,72, h-12, false);
	       }
	       else if(inmunidad > 0)
	       {
	            blit(0,120,8,8,66,h-12,mitems);
	            printNum(""+inmunidad/10,72, h-12, false);
	       }
	       
	        ch = sh;
        }
		
	   /*blit(8,14*8,8,8,2,2,mitems);printNum(""+vidas,16,2,false);
	   blit(8,15*8,8,8,2,11,mitems);printNum(""+tilecounter,24,11,true);
	   blit(0,8,8,8,2,20,mitems);printNum(""+tiempo,24,20,true);
	    */
	   if (cogido != -1) 
	   {
	        gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL));
	        gr.setClip(0,0,largo,alto);
	        gr.setColor(0xe1e114);			//gr.setColor(225,225,20);				
	        gr.drawString(menus.sitems[cogido] , 4, 4, Graphics.LEFT | Graphics.TOP);				 				         
	   }
	   /*if (reverse > 0) printNum(""+reverse,15, h-10, true);
	   */
	   
	   
	   if (helpmode >= 0) Help();
	   //System.out.println("10");
	   if (vidas == 0) 
	   {
	        PrintEX(255,255,0,"GAME OVER",w/2,(h/2)+h-8-((nfc*6)%(h*2)), Graphics.HCENTER);
	        PrintEX(255,0,0,"GAME OVER",w/2,(h/2)-h-8+((nfc*6)%(h*2)), Graphics.HCENTER);
	        PrintEX(255,150,0,"GAME OVER",w/2,(h/2)-7, Graphics.HCENTER);
	   }
	 /*  }
	 catch (Exception e)
	 {
	    e.printStackTrace();
	    e.toString();
	 } */
	}
	
	
	
	void Checktile(int p, int yt, int x, int y)
	{    
	        if (yt == MAXTY -1) return;
	        p+=MAXTX*2;
			if (p < 0 || p >= MAXTX*MAXTY) return;
			int tile = map[p];
			if (tile < 0) tile+=256;
			if (tile%32 >= 16) tile-=16;			
			if (tile%2 == 1) tile--;			
			switch(tile)
			{
				case 230:
				case 166:
				case 236:
				case 228: blit(((tile%16)<<3), ((tile/16)<<3)-16, 16, 16, x-((-offsetx)%16), y-((-offsety)%16), tiles);	   	 								
						  break;								
			}			
	
	}	
	
	
	void EndGame()
	{
	        //System.gc();
	        //all = null;
		    //tiles = null;
		    ///if (bolas != null)
	        ///    for(int i = 0; i < MAXBOLAS;i++)	    
	        ///        bolas[i] = null;
            bolas = null;	    
		    ///if (malo != null)
	        ///    for(int i = 0; i < malo.length;i++)	    
	        ///        malo[i] = null;
            malo = null;	                
	        ///if (balas != null)
	        ///    for(int i = 0; i < MAXBALAS;i++)	    
	        ///        balas[i] = null;        
	        balas = null;
	        //poyo = null;
	        sc.FaseMap = null;
	        items = null;
		    mitems = null;
    	    item = null;	    	        		    
	        System.gc();
	}
	
	void LoadIntro()
	{ 
	    ch = h;  cw = w;
	    if (TempImg != null) return;		
	    EndGame();
	    if (scores == null) scores = new Scores("poyogardensc");            
	    TempImg = loadImg("/caratula");	    
	    ititulo = loadImg("/intro003");
	    if (scores.isHighScore(puntos))          				
	    {	
			PoyoP.kins.enterHighScore(puntos);         			
			puntos = 0;
			
	    }
	    
        tintro = 0;
        Sonido(8,10,false);	   
	}
	
	
	int tintro = 0;
	void Intro()
	{		
		gr.setClip(0,0,w,h);
		gr.setColor(0x000000);
		gr.fillRect(0,0,w,h);
		gr.translate(24,30);
	    LoadIntro();
	    gr.drawImage(TempImg,0,0, gr.TOP|gr.LEFT);
        tintro++;
        if (tintro > 200) 
        {
            menus.quad();
        	/*gr.setClip(0,0,w,h);
	        gr.setColor(240,160,48);
            gr.fillRoundRect(3,3, w-6, h-6, 32, 32); 
            gr.setColor(240,112,0);
            gr.drawRoundRect(3,3, w-6, h-6, 32,32); */
            //gr.setColor(128,64,0);
            Print(255,238,62, menus.topscores , menus.w/2, menus.BEGFRASEY+ menus.FRASECR, Graphics.HCENTER);
            PutSprite(all, 8, 5, (tintro)%4, acor,3);                   
            PutSprite(all, menus.w-24, 5, 16+(tintro)%4, acor,3);                   
            PutSprite(all, 8, menus.h-29, 8+(tintro)%4, acor,3);                   
            PutSprite(all, menus.w-24, menus.h-29, 84+(tintro)%4, acor,3);                   
            
            //PutPoyo(24, menus.BEGFRASEY+12, tintro);
            //PutPoyo(w-24, menus.BEGFRASEY+12, tintro);
            for(int i = 0; i < 5;i++)            
	            Print(128+i*8,64,0, scores.names[i]+"....."+ scores.values[i] , menus.w/2, menus.BEGFRASEY + menus.FRASECR*(i+2), Graphics.HCENTER);	            
	        
	        
       }       
       gr.translate(-24,-30);     
       blit(0,0,imarco.getWidth(), imarco.getHeight(), (176-imarco.getWidth())/2, 27, imarco);    
       if (tintro <= 200)
            blit(1, 66, 100, 49 ,16+20,7, ititulo); 
       if (tintro == 400) {tintro = 0;  TempImg = null;}
       
       
      //if (tintro < 100) 
         /*gr.drawImage(TempImg,0,0, gr.TOP|gr.LEFT);	         
         gr.drawImage(TempImg2,64,0, gr.TOP|gr.LEFT);	         
         gr.drawImage(TempImg3,0,64, gr.TOP|gr.LEFT);	         
         gr.drawImage(TempImg4,64,64, gr.TOP|gr.LEFT);	         */
         
      /*else
      {
      	gr.setColor(0,0,0);		
         gr.fillRect(0, 0, w, h);               
		   gr.setColor(0,200,0);         
			if (tintro < 600) 			            
	  	      for (int i = 0;i < s.length;i++)
				   gr.drawString(s[i] , 4, 2+11*i, Graphics.LEFT | Graphics.TOP);	         
			else	   
			   for (int i = 0;i < s2.length;i++)
				   gr.drawString(s2[i] , 4, 2+11*i, Graphics.LEFT | Graphics.TOP);	         
		}	*/	
		if (apretando) {estado = -1;currentkey = 0;}
	}
	
	
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
	}
	*/
	public void IniGame()
	{
	    citem = 0;
	    reverse = 0;
	
	    vidas = 5;
	    cogido = -1;
	    scores = null;
	    //System.gc();
		tipobola = SIMPLE;
    	numbola = SIMPLE; 		
	
	    helpmode = -1;
		TempImg = null;
		//TempImg2 = null;
		//TempImg3 = null;
		//TempImg4 = null;
		System.gc();
		puntos = 0;
		//all = loadImg("/all");		
		estado = 0;
		prestado = 0;
		//fase = 0;
		
				
		items = loadImg("/items");
		mitems = loadImg("/mitems");
		//tiles = loadImg("/tiles");
		
		item = new unit(items, 16, 16, 5, 0, 0, pcor);		
		poyo.setCollisionRectangle(2,10,12,12);
	    poyo.Activate();
	    StopSound();
		CargaFase(fase);	
		
		//map = loadRemote("http://kung-foo.dhs.org/zener/mapa1-map.png");  									   
	  
	    bolas = new unit[MAXBOLAS];
	    for(int i = 0; i < MAXBOLAS;i++)
	    {
	         bolas[i] = new unit(items, 16, 16, 5, 2, 0, pcor);
	         bolas[i].setCollisionRectangle(4,4,8,8);
	    }
	    balas = new unit[MAXBALAS];
	    for(int i = 0; i < MAXBALAS;i++)
	    {
	        balas[i] = new unit(all, 16, 24, 20, 8, 4, acor);
	        balas[i].setCollisionRectangle(4,8,8,8);
	    }
	    System.gc(); 	
	   //System.out.println("1");
	}
	
		
	public static void AddBala(int x,int y, int incx, int incy, int as)
	{	      
	      int i = UnitLibre(balas, MAXBALAS);
	 		if (i == -1) return;     
	      balas[i].Auto(x, y, incx, incy);	   
	      balas[i].desp = 2;
	      if (incx < 0) balas[i].desp = 1;
	      balas[i].ashape = as;
	}
	
	
	public void Mata(int j)
	{		
		if (!malo[j].active) return;
		if (malo[j].muerto != 0) return;
		puntos += 100;
		Sonido(1,1,true);
		malo[j].muerto = 1;
		SetTile((tguarida[j]%MAXTX)*8, (tguarida[j]/MAXTX)*8, 8); //8 es HUEVO
	}
	
	
	public void UpdateBolas()   
   {
        for (int i = 0; i < MAXBOLAS; i++)
        {
             if (bolas[i].active)
             {
             	    bolas[i].Update(poyo);
    				bolas[i].ashape = (++bolas[i].ashape)%4;
    				for(int j=0;j < MAXMALOS;j++)
    					if (malo[j].active && malo[j].isCollidingWith(bolas[i])) 
    					{
    						if (tipobola == HIELO) {malo[j].freeze = TFREEZE;StopSound();Sonido(0,1,false);}
    						else Mata(j);
    					}
                 	if (bolas[i].timing > 20) bolas[i].Deactivate();
                 	int tile = GetTile(bolas[i].x+(bolas[i].width)/2,bolas[i].y+bolas[i].height/2);
                 	if (inList(tcol,tile)) 
                 	{
                 		if (tipobola == MARTILLO && tile == SEMIPIEDRA) 
                 		{
                 		    SetTile(bolas[i].x+(bolas[i].width)/2,bolas[i].y+bolas[i].height/2, QUEMADO);
                 		    jode(bolas[i].x+(bolas[i].width)/2,bolas[i].y+bolas[i].height/2);
                 		}
                 		bolas[i].Deactivate();         	
                 	}	
              }         
      }
      for (int i = 0; i < MAXBALAS; i++)
      {
         if (balas[i].active)
         {
         	    balas[i].Update(poyo);
				if (poyo.isCollidingWith(balas[i])) poyo.muerto = 1;							
				if (balas[i].desp == 0 || balas[i].desp == 3) balas[i].Deactivate();         	
         		
         	    if (balas[i].timing >= 20)
         	    {
         		    balas[i].desp = 3;
         		    if (balas[i].incx < 0) balas[i].desp = 0;
         	    } 
         	
         	    if (inList(tcol,GetTile(balas[i].x+(balas[i].width)/2,balas[i].y+16))) 
         	    {
         		    balas[i].desp = 3;
         		    if (balas[i].incx < 0) balas[i].desp = 0;
         		    balas[i].incx = 0;
         		    balas[i].incy = 0;         		
         	    }         	
         }         
      }
      
   }
   
	
    //long fff;	
	
   public void run()
   {
    //try
	//{
	   estado = 1; //MONGOFIX: Peris manta

	   while (estado!=6)
	   {				 
	   	  long tempo = System.currentTimeMillis();
	      repaint();
	      serviceRepaints();
                      //PoyoP.display.callSerially(this); 

	      tempo = 50 - System.currentTimeMillis()+tempo;	      
	      ///fff = tempo;
	        if (tempo <= 0) tempo = 1; 
	      //System.gc();
	      try
	      {
	        //if (tempo >= 40) 
	            //flujo.sleep(1);	      
	        //else
	            flujo.sleep(tempo);           
	            
          }
          catch(java.lang.InterruptedException e) {}                 
       }	
	   flujo = null;
	   PoyoP.kins.destroyApp(true);
	  /*}
	 catch (Exception e)
	 {
	    e.printStackTrace();
	    e.toString();
	 } */
	}

   public void keyPressed(int keyCode)
   {
      rkey = keyCode;
      currentkey = keyCode;
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
      rkey = 0;
   }


	public void finfase()
	{
	    StopSound();Sonido(7,1,false);
		currentkey=0;apretando=false;
		estado = 70;
		puntos += tiempo*10;		
	}
	
	/*
	public void showtime()
	{
		gr.setColor(0,0,0);
		gr.fillRect(0,0,w,h);
		gr.setClip(0,0,w,h);
		gr.setColor(255,255,255);
		gr.drawString("Fase :"+(fase+1), w/2, 60, Graphics.HCENTER | Graphics.TOP);		   
		gr.drawString("Tiempo :"+nfc, w/2, 80, Graphics.HCENTER | Graphics.TOP);		   
		if (apretando)
		{	
			fase++;
			if (fase < 15) {CargaFase(fase);estado = 0;PoyoP.lastfase = Math.max(PoyoP.lastfase, fase);}
			else IniFin();			
		}
		
	}*/

    
    public void IniFin()
    {        
        MAXTX = 26;
		MAXTY = 26;
		sc.FaseMap = null;		
		System.gc();
		map = ReadByteArray("/mapafin.map", MAXTX*MAXTY);		   
		
		sc.ScrollSET (map, MAXTX, MAXTY,  tiles, 16);
		estado = 99;
		
	    nfc = 0;	        
    }


    public void Fin()
    {
        cw = sw;ch = sh;
        nfc++;
        
        if (nfc > 10 &&nfc%2 == 0) 
        {
            for (int i = 0; i < MAXTY/2;i++)
				for (int j = 0; j < MAXTX/2;j++)
				{
					int tile = GetTile(j*16,i*16);
					if (tile < 0) tile +=256;
					if (tile%32 == 14 && tile > 14)
					   SetTile(j*16,i*16,  tile-32);
					   
		        }		  		
		    int j = ((nfc/2) % (MAXTY/2));
        	for (int i = 0; i < MAXTY/2;i++)
        	{
        		    int tile = GetTile(j*16,i*16);
        			if (tile < 0) tile +=256;
        			if (tile%32 == 14 && tile == 14)
        	        SetTile(j*16,i*16,  14+64);
        	}  
		}
		
                 
        sc.ScrollRUN(0, 0);					
	    sc.ScrollIMP(gr);	
	    //APA�O
	    gr.setClip(0,0,w,h);
	    gr.setColor(240,160,48);
        gr.fillRect(0,h-16, w, 16); 
        
	    for(int i = 0;i < w; i=i+16)
	        blit(64,64+(nfc%3)*16,16,16,i,(h/32)*16,tiles);        	    
	    PutMiniPoyos(w/5, h/5, nfc);	    
	    PutMiniPoyos(w-(w/5), 2*h/3, nfc+10);	    
	    PutPoyo(w/2, (h/2)-(h/7), nfc);
	    PutPoyo(w/6, h/3, nfc+5);
	    //PutPoyo(w-w/5, 2*h/5, nfc+8);
	    //PutPoyo(w/3, h-(h/6), nfc+2);
	    //PutPoyo(w/8, 2*h/3, nfc+13);
	    PutPoyo(w-w/3, 4*h/5, nfc+10);
	    //PutPoyo(w-w/4, h/5, nfc+17);
	    blit(48,64,16,32,w-w/5, h/24,tiles);        	    
	    blit(96,96,16,32,w/8,((2*h/3)-5),tiles);        	    
	    if (nfc > 80)
	    {
	        if (nfc < 115*2)	        
	            for (int i = 0;i<menus.fin.length;i++)             
	            {
                  	 Print(155,0,0, menus.fin[i], (w/2)+1, (menus.BEGFRASEY+i*menus.FRASECR)+1, Graphics.HCENTER);	               	              	                                     
                  	 Print(255,255,255, menus.fin[i], w/2, menus.BEGFRASEY+i*menus.FRASECR, Graphics.HCENTER);	               	              	                                     
                }            
            else
            {
                cw = w;ch = h;
                menus.Creditos(gr, w, h);
                cw = sw;ch = sh;
            }                 
	    }
	    
        if (nfc > 120*2 && apretando && (rkey == -6 || rkey == -7 || currentkey == Canvas.FIRE) ) {estado = 1;prestado = 1;apretando=false;}
    }

    
    
    public void PutMiniPoyos(int x, int y, int yy)
    {
        yy = (yy+13)%finpy.length;
        blit(8,14*8,8,8,x-6,y-finpy[yy]-3,mitems);        
        yy = yy%finpy.length;
        blit(8,14*8,8,8,x+7,y-finpy[yy]-1,mitems);
        yy = (yy+13)%finpy.length;
        blit(8,14*8,8,8,x,y-finpy[yy],mitems);
        yy = (yy+13)%finpy.length;        
        blit(8,14*8,8,8,x-11,y-finpy[yy]+1,mitems);
    }
    
    public static void PutPoyo(int x, int y, int yy)
    {
        yy = yy%finpy.length;
        PutSprite(all, x-8, y-16-finpy[yy],  0, acor,3);
    }

	public void Dispara(int lll)
	{	   					
	   		int i = UnitLibre(bolas, MAXBOLAS);		
	   		if (i != -1)
	   		{
	   			switch (lll)
		   		{
		   			case 0: bolas[i].Auto(poyo.x, poyo.y,0,-600);bolas[i].offy = 64/16;break;
		   			case 1: bolas[i].Auto(poyo.x, poyo.y+10,0,600);bolas[i].offy = 64/16;break;
		   			case 2: bolas[i].Auto(poyo.x-4, poyo.y+10,-600,0);bolas[i].offy = 0;break;
		   			case 3: bolas[i].Auto(poyo.x+4, poyo.y+10,600,0);bolas[i].offy = 0;break;
		   		}	   		
		   		switch(tipobola)
		   		{
		   			case SIMPLE: bolas[i].offx = 32/16;break;
		   			case HIELO: bolas[i].offx = 16/16;break;
		   			case MARTILLO: bolas[i].offx = 0;break;
		   		}
		   	}	
	}

////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////
	
	
	public static boolean inList(int[] lista, int valor)
	{
		boolean trobat = false;
		int i = 0;
		while (!trobat && i < lista.length)
		{
			trobat = //trobat || 
			lista[i] == valor;
			i++;
		}
		return trobat;
	}


	
   private void UpdatePoyo(int xdir, int ydir, boolean firing)
   {
   	    if (inmunidad > 0) {poyo.muerto = 0;inmunidad--;}
   	    for(int i = 0;i < MAXMALOS;i++)   	
   		    if (malo[i].isCollidingWith(poyo) && malo[i].freeze > 0) Mata(i);
   	    //int tile = GetTile(poyo.x+(poyo.width/2),poyo.y+20);	   
	    //poyo.tag = tile;	
       	if ((!inList(HieloList, poyo.tag) || citem == IBOTAS || (sx == 0 && sy == 0)) && !disparando)
       	{
    	   	switch(ydir)
    		   {
    		     	case -1: if (sy >= 0) sy = -2;
    		      			else if (sy > -dx && nfc%1==0) sy--;
    		      			lado = 1;	      
    		         		break;
    		      case 0: 
    		      		  if (sy > 0) sy--;
    		              if (sy < 0) sy++;
    		              break;	      
    		      case 1: if (sy <= 0) sy = 2;
    		      			else if (sy < dx && nfc%1==0) sy++;
    		      			lado = 0;
    		         		break;		     
    		   }
    		   switch(xdir)
    		   {
    		   	case -1: if (sx >= 0) sx = -2;
    		      			else if (sx > -dx && nfc%1==0) sx--;
    		      			lado = 3;	      
    		         		break;
    		      case 0: if (sx > 1) sx-=2;
    		      		  else if (sx > 0) sx--;
    		      		  if (sx < -1) sx+=2;
    		              else if (sx < 0) sx++;
    		              break;	      
    		      case 1: if (sx <= 0) sx = 2;
    		      			else if (sx < dx && nfc%1==0) sx++;
    		      			lado = 2;
    		         		break;		     
    		   }	  
    		   if  (nfc%2 == 0 && (sx != 0 || sy != 0)) ffra = (++ffra)%4;	   	   
    		   
    	   }
	   if (disparando)
	   {
	   	anim++;	   
	   	if (anim == 4) 
	   	{	   
	   		if (numbola == SIMPLE) Dispara(lado-4);
	   		else for(int i = 0;i< 4;i++) Dispara(i);	   			   		
	   	}
	   	if (anim == 8) 
	   	{
	   		lado-=4;	   		
	   		ffra = 0;
	   		disparando = false;
	   	}	   	
	   	else
	   	{	ffra = fireanim[lado-4][anim];	}
	   }
	   if (firing && disparando == false)
	   {
	        Sonido(2,1,false);	        
	   	    disparando = true;
	   	    anim = 0;	
	   	    lado+=4;   	
	   	    ffra = 0;
	   }
	   
	   poyo.ashape = lado;
	   poyo.desp = ffra;
				
		if (reverse > 0) { sx = -sx;sy = -sy;	}
	   
		
	   //TODO: HACERLO BIEN
	   if (citem != IBOTAS && nfc%2==0)
	   {
		   if (poyo.tag == AGUADE && sx < 6) sx += 2;
		   else if (poyo.tag == AGUAIZ && sx > -6) sx -= 2;
		   else if (poyo.tag == AGUAAR && sy > -6) sy -= 2;
		   else if (poyo.tag == AGUAAB && sy < 6) sy += 2;
	   }
	   
	   //COLISIONES
	   if (sx > 0 && inList(tcol,GetTile(poyo.x+(poyo.width)+1,poyo.y+20))) sx = 0;
	   if (sx < 0 && inList(tcol,GetTile(poyo.x-3,poyo.y+20))) sx = 0;	   
	   if ((sy > 0) && inList(tcol,GetTile(poyo.x+(poyo.width/2)-5,poyo.y+(poyo.height)))) 
	   {
	   	    sy = 0;
	   	    if (sx == 0 && !inList(tbcol,GetTile(poyo.x+(poyo.width/2)+4,poyo.y+(poyo.height)))) sx = 1;	   	
	   }	   
	   if ((sy > 0) && inList(tcol,GetTile(poyo.x+(poyo.width/2)+4,poyo.y+(poyo.height)))) 
	   {	
	   	    sy = 0;
	   	    if ((sx == 0) && !inList(tbcol,GetTile(poyo.x+(poyo.width/2)-5,poyo.y+(poyo.height)))) sx = -1;
	   }
	   if ((sy < 0) && inList(tcol,GetTile(poyo.x+(poyo.width/2)-5,poyo.y+8)))
	   {
	   	    sy = 0;
	   	    if ((sx == 0) && !inList(tbcol,GetTile(poyo.x+(poyo.width/2)+4,poyo.y+8))) sx = 1;
	   }
	   if ((sy < 0) && inList(tcol,GetTile(poyo.x+(poyo.width/2)+4,poyo.y+8))) 
	   {
	   	    sy = 0;
	   	    if ((sx == 0) && !inList(tbcol,GetTile(poyo.x+(poyo.width/2)-5,poyo.y+8))) sx = -1;
	   }
	   
	   if (citem == IBOTAS2)
	   {
	   	    poyo.x += sx/2;
	   	    poyo.y += sy/2;	   	   		   
	   }
	   
	   poyo.x += sx;
	   poyo.y += sy;	   	   	
	   
	   if (reverse > 0) 
	   {
	    	reverse--;
    	   	sx = -sx;sy = -sy;	
	   }
	   
	   
	   //! NO DEBERIA HACER FALTA
	   if (poyo.x < 0) {poyo.x = 0;sx = 0;}
	   if (poyo.x > (MAXTX-2)<<3) {poyo.x = (MAXTX-2)<<3;sx = 0;}
	   if (poyo.y < -8) {poyo.y = -8;sy = 0;}	   
	   if (poyo.y > ((MAXTY-2)<<3)-8) {poyo.y = ((MAXTY-2)<<3)-8;sy = 0;}
	   
      //TILES ESPECIALES
	   int tile = GetTile(poyo.x+(poyo.width/2),poyo.y+20);	   
	   poyo.tag = tile;
	   SetHelp(tile);
	   if (tile == QUEMADO) SetTile(poyo.x+(poyo.width/2),poyo.y+20, HIERBA1);	   
	   
	   else if (tile == TELEPORT && firing) 
	   {
			if (getMapPos(poyo.x+(poyo.width/2),poyo.y+20) == tteleport[0])	   	
			{
				poyo.x = (tteleport[1]%MAXTX)*8;
				poyo.y = ((tteleport[1]/MAXTX)*8)-8;
			}
			else
			{
				poyo.x = (tteleport[0]%MAXTX)*8;
				poyo.y = ((tteleport[0]/MAXTX)*8)-8;
			}			
			inmunidad = 30;
	   }
	   else if (tile == PARALISIS)
	   {
	   	    SetTile(poyo.x+(poyo.width/2),poyo.y+20, HIERBA1);
	   	    StopSound();Sonido(0,1,true);
	   	    for(int i = 0; i < MAXMALOS;i++)
	   	    if (malo[i].active) malo[i].freeze = TFREEZE;
	   	
	   }
	   else if (tile == BOMBA)
	   {
	        StopSound();Sonido(1,1,true);
	   	    SetTile(poyo.x+(poyo.width/2),poyo.y+20, HIERBA1);
	   	    for(int i = 0; i < MAXMALOS;i++)
	   	        Mata(i);
	    }
	   else if (tile == QUEBRADIZO)
	   {
	   	SetTile(poyo.x+(poyo.width/2),poyo.y+20, QUEBRADIZO1);
	   }
	   else if (tile == QUEBRADIZOL)
	   {
	   	SetTile(poyo.x+(poyo.width/2),poyo.y+20, QUEBRADIZOL1);
	   }
	   else if (tile == QUEBRADIZOR)
	   {
	   	SetTile(poyo.x+(poyo.width/2),poyo.y+20, QUEBRADIZOR1);
	   }
	   else if (tile == EXPLOSION)
	   {
	        StopSound();Sonido(10,1,false);
	   	    SetTile(poyo.x+(poyo.width/2),poyo.y+20, EXPLOSION1);
	   }
	   else if (inList(AgujeroList, tile)) poyo.muerto = 1;
	   //! CALCULO DEL OFFSET: AKI?
      offsetx = -poyo.x+(sw/2)-8;
      offsety = -poyo.y+(sh/2)-16;      	   	   
      if (offsetx < -(MAXTX*8)+sw) offsetx = -(MAXTX*8)+sw;
      if (offsety < -(MAXTY*8)+sh) offsety = -(MAXTY*8)+sh;
      if (offsetx > 0) offsetx = 0;
      if (offsety > 0) offsety = 0;
      
   }
   
   final static String huevo="CarlosPeris";

	public void Muerte()
	{		
	    //poyo.muerto = 0;if (1 == 1) return;
	    if (poyo.muerto == 1) {StopSound();Sonido(6,1,true);}
		poyo.muerto++;
		poyo.ashape = (++poyo.ashape)%4;
		if (poyo.muerto > 24)
		{
		    vidas--;
		    tiempo = initiempo;
			poyo.muerto = 0;
			poyo.x = inicio[0];
			poyo.y = inicio[1];
			inmunidad = 70;
			tipobola = SIMPLE;
			numbola = 0;
			citem = 0;
			reverse = 0;
			{StopSound();Sonido(3,1,false);}
		}
		//IniGame();
	}

	public int getMapPos(int x, int y)
	{
		return ((x/16)*2)+((y/16)*MAXTX*2);
	}


	
	public static int GetTile(int x, int y)
	{
		x = x/16;
		y = y/16;
		int vv = (x+(y*MAXTX))*2;
		if (vv < 0 || vv >= MAXTX*MAXTY) return 0;
		int v = map[vv];
		if (v < 0) v+=256; 
		return v;		
   }

	public static void SetTile(int x, int y, int tile)
	{
		x = x/16;
		y = y/16;
		int vv = (x+(y*MAXTX))*2;
		if (vv < 0 || vv >= MAXTX*MAXTY) return;
		
		//try{
		map[(x+(y*MAXTX))*2] = (byte)tile;
		map[((x+(y*MAXTX))*2)+1] = (byte)(tile+1);
		map[((x+(y*MAXTX))*2)+MAXTX] = (byte)(tile+16);
		map[((x+(y*MAXTX))*2)+MAXTX+1] = (byte)(tile+16+1);		
		//}catch(Exception e){System.out.println("fczfsad");}
   }

	boolean retry = true;

   private void UpdateSprite()
   {
      int xdir = 0, ydir = 0;
      boolean firing = false;
      if (apretando)
    	   switch(currentkey)	
    	   {
              case Canvas.UP:
                    ydir = -1;break;
              case Canvas.DOWN: 
                    ydir = 1; break;
              case Canvas.LEFT: 
                    xdir = -1;break;
              case Canvas.RIGHT: 
                    xdir = 1; break;
              case Canvas.FIRE:if (retry) {firing = true; retry = false;}break;              
              //case Canvas.KEY_STAR : estado=4;currentkey = 0;break;                                  
              //case Canvas.KEY_POUND : tilecounter=0;currentkey = 0;break;                                  
        }             
        if (rkey == -7) if (retry) {firing = true; retry = false;}
        if (rkey == -6) {estado=4;currentkey = 0;rkey = 0;}
        if (poyo.muerto == 0 || inmunidad > 0) UpdatePoyo(xdir, ydir, firing);
        else Muerte();
        menus.opmenu = 0;
   }

   
    public Game()
    {   
        imarco = loadImg("/marco");		 		 		
        all = loadImg("/all");		 		 		
        tiles = loadImg("/tiles");  
 		
 		sc = new Scroll();
		sc.ScrollINI (sw,sh);				
	    acor = ReadByteArray("/all.cor", 960*3);		   
	    pcor = ReadByteArray("/items.cor", 240);		   
	    
        menus = new MMenu(128, 128);//gr);      
        poyo = new unit(all, 16, 24, 20, 0, 0, acor);
		
        //buildsfx();
        SoundINI();
            /*bp = new Sound[11];
            for(int i = 0;i < 11;i++) ISound(i,sss[i]);       
            sss = null;*/
        //MARI
        for(int i = 0; i < 4;i++)
            mari[i] = new mariposa();        
        //  
        shelp = new short[w*(h-16)];
        for(int i = 0; i < shelp.length;i++) shelp[i] = (short)0x70515111;
        
        flujo = new Thread(this);        
	    flujo.start();
  	    //System.gc();
  	}
  	
  	short shelp[];

      
   public void paint (Graphics g)
   {        
   	    gr = g;
   	    menus.gr = g;
   	    dgr = DirectUtils.getDirectGraphics(g);
   	    if (estado==0) Engine();
   	    //printNum(""+fff,2, 46, false);}
	    else if (estado==1) Intro();
	    else if (estado==2) IniGame();
	    else if (estado==71) {CargaFase(fase);estado = 0;PoyoP.lastfase = Math.max(PoyoP.lastfase, fase);PoyoP.kins.PrefsSET();}
	    else if (estado==72) IniFin();			
	    else if (estado==99) Fin();
	    else estado = menus.show(currentkey, apretando); 	   
	    
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
	

    public static void StopSound()
    {
        if (lastsfx < 0) return;
        SoundRES();
        
    }


    public static void Sonido(int s, int c, boolean vib)
    {        
        
    	if (c == 0) c = 10;
    	SoundSET(s,c);
    	
    }
  
   
   
   public static int UnitLibre(unit vec[], int max)
   {
       int i = 0;
       while (i < max) 
          if (!vec[i].active) {vec[i].Activate();return i; }
          else i++;
       return -1;
   }
   

    ////}
// *******************
// -------------------
// Sound - Engine
// ===================
// *******************

static Player[] Sonidos;

static int SoundOld=-1;
static int SoundTime;

// -------------------
// Sound INI
// ===================

public void SoundINI()
{

	try	{ Thread.sleep(20); } catch(java.lang.InterruptedException e) {}

	Sonidos = new Player[11];

	Sonidos[0] = SoundCargar("/fre.mid");	
	Sonidos[1] = SoundCargar("/muertem.mid");
	Sonidos[2] = SoundCargar("/explo.mid");
	Sonidos[3] = SoundCargar("/inifase.mid");
    Sonidos[4] = SoundCargar("/item.mid");	
	Sonidos[6] = SoundCargar("/muerte.mid");
	Sonidos[7] = SoundCargar("/finfase.mid");
	Sonidos[8] = SoundCargar("/menu.mid");
    Sonidos[9] = SoundCargar("/turbo.mid");	
	Sonidos[10] = SoundCargar("/tic.mid");


	try	{ Thread.sleep(20); } catch(java.lang.InterruptedException e) {}

}

public Player SoundCargar(String Nombre)
{

	Player p = null;

	try
	{
	    InputStream stream = getClass().getResourceAsStream(Nombre);
	    //if (stream == null) System.out.println("NUUUUUUUULOOOOOOOO");
		p = Manager.createPlayer( Nombre ,"audio/midi");
		//p = Manager.createPlayer( "resource:"+Nombre);
		p.realize();
		p.prefetch();
		//VolumeControl vc = (VolumeControl) p.getControl("VolumeControl"); if (vc != null) {vc.setLevel(50);}
		//p.setLoopCount(1);
//		p.start();
	}
	catch(Exception ex) {ex.printStackTrace();}

	return p;
}



// -------------------
// Sound SET
// ===================

int stemps;

public static void SoundSET(int Ary, int Loop)
{
    if (Ary == 5) return;
	if (PoyoP.sfx)
	{
		
		//SoundTime = new int[] {300,3000,3000,300,3000}[Ary];
        //int SoundTimeA[] = {300,250,250,400};
		if (Loop==0) {Loop--;}

		if (SoundOld!=-1) {SoundRES();}

		try
		{
		    VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
		    if (Sonidos[Ary] == null) return;
		    //SoundTime = SoundTimeA[Ary];
		    Sonidos[Ary].setLoopCount(Loop);
		    Sonidos[Ary].start();
		}
		catch(Exception exception) {exception.printStackTrace();}

		SoundOld=Ary;
	}
}


// -------------------
// Sound RES
// ===================

public static void SoundRES()
{

	try
	{
	    Sonidos[SoundOld].stop();
	}
	catch(Exception exception) {exception.printStackTrace();}

	SoundOld=-1;

}



// -------------------
// Sound RUN
// ===================
/*
public void SoundRUN()
{
	if (SoundOld!=-1 && --SoundTime==0) {SoundRES();}
}*/


}//END midlet


