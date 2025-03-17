package com.mygdx.mongojocs.mr;
//////////////////////////////////////////////////////////////////////////
// Movistar MotoGP - Color Version 1.0
// By Carlos Peris
// MICROJOCS MOBILE , 2003
//////////////////////////////////////////////////////////////////////////


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    static Sound bp[];
    public static int       w;
    public static int       h;
    private static Graphics gr;
    Thread                  flujo;
    static boolean          apretando = false;
    static int              currentkey, rkey;
    protected static int    nfc = 0;
    static int              offsetx = 0;
    static int              offsety = 0;
    static int              largo = 176;
    static int              alto = 208;
   
    static Random           rnd = new Random();
    boolean                 end = false;
  	static Dengine dengine;
    static Circuit circuit;
    static Punto camara = new Punto();
	static Image[] cartel;
	static int[] cartelw;
	static int[] cartelh;
	static Moto500 moto[] = new Moto500[8];
    static Image fondo[], mon, imenu, portada, portada2,portada3, wm, caidam, caidap, iroderes, imotoup, ifum, zimzum, edit1, edit2;
    static int estado = -1, prestado = 1;
    static MMenu menus;

	static int esengine = 0;
	
	static int modojuego;
	final static int SIMPLE = 0;
	final static int CAMPEONATO = 1;
	final static int VERSUS = 2;
	final static int VSTIME = 3;
	
	final static int MAXCIRCUITOS = 8;
	final static int MAXCORREDORES = 8;
	static int numcarrera;
	static int puntoscorredor[] = new int[MAXCORREDORES];
	
	//static Sound bp[];	
	final static int puntoscarreta[] = {12,8,5,3,2,1,1,0};
	static int cli;
	static int caida;
	static byte motocor0[], motocor1[], motocor2[], roderescor[];
	//static byte macizacor[] = null;
	long tiempo;
	private static DirectGraphics dgr;
    boolean paused = false;

    public void showNotify()
	{
	    paused = false;
	}
	
	public void hideNotify()
	{
	    paused = true;
	    if (estado == 0) estado = 4;
	}

	   
   public void stop(){ 	}
   
   public byte [] ReadByteArray(String ffile, int bytes)
	{	  
		/*byte [] data=null;
		try
		{	InputStream is = getClass().getResourceAsStream(ffile);
			data = new byte[bytes];
			is.read(data);
			is.close();
			
		}catch (Exception e){//System.out.println("File Exception: resource:"+ffile+"!\n"+e);
		}return data;*/
		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+ffile.substring(1));
		byte[] abytes = file.readBytes();
		return abytes;
	}
	
   public static void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
	{
		if ((DestX       > w)
		||	(DestX+SizeX < 0)
		||	(DestY       > h)
		||	(DestY+SizeY < 0))
		{return;}
	
	
		gr.setClip(0, 0, w, h);
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

// ----------------------------------------------------------
 
   
    public static void Print(int r, int g, int b, String s, int xx, int yy, int align)
	{
		//if (size == 1) gr.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
		//else 
		gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL));
		gr.setClip(0,0,largo,alto);
		gr.setColor(r/2,g/2,b/2);				
		gr.drawString(s , xx-1, yy-1, align | Graphics.TOP);				 				         
		gr.drawString(s , xx+1, yy+1, align | Graphics.TOP);				 				         
		gr.setColor(r,g,b);		
		gr.drawString(s , xx, yy, align | Graphics.TOP);				 				         
	}
	
	
	public static void Click()
	{
	    cli++;
	    if (cli%2 == 0) blit(87,82,13,9,w-15,h-10,mon);
	}
	
	public void PrintTime(long tim, int x, int y)
   	{
   		long amins = (tim/1000);
   		long mins = amins/60;
   		long secs = amins%60;
   		long mils = tim%1000;
   		String smins = ""+mins;
   		String ssecs = ""+secs;
   		String smils = ""+mils;
   		if (mins < 10)  smins = "0"+smins;
   		if (secs < 10)  ssecs = "0"+ssecs;
   		if (mils < 100) smils = "0"+smils;
   		if (mils < 10) smils = "0"+smils;   		   		
   		PrintNumM(smins+":"+ssecs+":"+smils, x, y);
   	}	
   	

	public static void PrintNumM(String s, int x, int y)
	{
		char[] str = s.toCharArray();
		int off = 0;
		int col = 0;
		int ll = 0;
			
		for (int i = 0; i < s.length();i++)		
		{
			if (str[i] >= '0' && str[i] <= '9') 
			{
				ll = 8;
				col = 1+((str[i]-'0')*8);
			}
			else
			{
				ll = 3;			
				col = 82;				
			}
			blit(col,82,ll,11,x+off,y,mon);
			off += ll;
		}
		
	}
	 
	public void PrintNumB(String s, int x, int y)
	{
		char[] str = s.toCharArray();
		int off = 0;
		int col = 0;
		int ll = 0;
			
		for (int i = 0; i < s.length();i++)		
		{
			if (str[i] >= '0' && str[i] <= '9') 
			{
				ll = 12;
				col = 1+((str[i]-'0')*12);
			}			
			blit(col,63,ll,18,x+off,y,mon);
			off += ll;
		}
		
	} 
	public void PrintNumS(String s, int x, int y)
	{
		char[] str = s.toCharArray();
		int off = 0;
		int col = 0;
		int ll = 0;
			
		for (int i = 0; i < s.length();i++)		
		{
			if (str[i] >= '0' && str[i] <= '9') 
			{
				ll = 5;
				col = 1+((str[i]-'0')*5);
			}			
			blit(col,55,ll,7,x+off,y,mon);
			off += ll;
		}
		
	} 
	
	 
    public static void blit(int ox, int oy, int _w, int _h, int x, int y, Image foo)
    {
   	    gr.setClip(0, 0,w, h); 
        gr.clipRect(x,y,_w,_h);	         
	    gr.drawImage(foo,x-ox, y-oy, Graphics.TOP|Graphics.LEFT);	         
    }
	
	public static Image loadImg(String s)
	{
	  try{
	  		Image im = new Image();
		    im._createImage(s+".png");
		    return im;
		  }catch (Exception e) {}   
	 return null;	  	
	}
	
	void Inicializacion1()
	{
		circuit = new Circuit();
       	fondo = new Image[2];
       	motocor0 = ReadByteArray("/moto01.cor", 252*3);		   
       	motocor1 = ReadByteArray("/moto1.cor", 252*3);		   
       	motocor2 = ReadByteArray("/moto2.cor", 252*3);		   
       	roderescor = ReadByteArray("/roderes.cor", 42);		   
       	//maciza = loadImg("/maciza");
       	imotoup = loadImg("/motoup1");
       	ifum = loadImg("/humo");
       	//macizacor = ReadByteArray("/maciza.cor", 54);		   
   	    mon = loadImg("/contadores");   	
   	    cartel = new Image[6];
   	    cartelw = new int[6];
   	    cartelh = new int[6];
       	
       	cartel[0] = loadImg("/cartel001");
       	cartelw[0] = 47;
       	cartelh[0] = 20;
       	cartel[1] = loadImg("/cartel002");
       	cartelw[1] = 14;
       	cartelh[1] = 120;   	
       	cartel[2] = loadImg("/cartel003");
       	cartelw[2] = 23;
       	cartelh[2] = 23;   	   	
       	cartel[3] = loadImg("/cartel004");
       	cartelw[3] = 32;
       	cartelh[3] = 18;   	   	
       	cartel[4] = loadImg("/agua");
       	cartelw[4] = 30;
       	cartelh[4] = 8;   	   	
       	cartel[5] = loadImg("/oil");
       	cartelw[5] = 30;
       	cartelh[5] = 8;   	   	
	    dengine = new Dengine(0,0,176,208);   	
   	    iroderes = loadImg("/roderes");
       	edit1 = loadImg("/edit1");
   	    edit2 = loadImg("/edit2");
   	    Image imoto0 = loadImg("/moto01");
       	Image imoto1 = loadImg("/moto1");
       	Image imoto2 = loadImg("/moto2");
       	
   	    for(int i = 1;i< 8;i++)
       	{
   	    	if (i%2 == 0)
   		    	moto[i] = new Moto500(imoto1, 42, 42, 7, 0, 0,"", motocor1);
   		    else
   			    moto[i] = new Moto500(imoto2, 42, 42, 7, 0, 0,"", motocor2);	
   	    }
       	moto[0] = new Moto500(imoto0, 42, 42, 7, 0, 0,"", motocor0);
       	moto[0].setFrame(3);       	       	
       	
	}
	
	public void Inicializacion()
	{
	    nfc = 0;
		estado = 40;
		caidam = loadImg("/caidam");
       	caidap = loadImg("/caidap");
   	    imenu = loadImg("/menubase");   	
   	    portada = loadImg("/portada1");   	
   	    portada2 = loadImg("/portada2");   	
   	    portada3 = loadImg("/portada3");   	
   	    wm = loadImg("/mapaseleccion");   	    
	}
	
   public void IniGame(int dificultad)
   {
        tiempo = 60;
        SetEstado(0,0);
   	    for(int i = 1;i< 8;i++)
   	    {
       		moto[i].setFrame(3);
       		moto[i].velocidad = 120+(i*12+(dificultad))+(rnd.nextInt()%11);
       		moto[i].pos[0].z = i*(1100+(dificultad*100));
       		moto[i].recorrido = moto[i].pos[0].z;   		
   	    }
   	    //PPP
   	    if (dificultad == 1) moto[0].maxvelocidad -= 20;
   	    if (dificultad == 2) moto[0].maxvelocidad -= 10;   	        
   	    //
   	    motocor0 = ReadByteArray("/moto0"+dificultad+".cor", 252*3);		   
       	Image imoto0 = loadImg("/moto0"+dificultad);       	   	    
       	imotoup = loadImg("/motoup"+dificultad);       	
       	moto[0].cor = motocor0;
       	moto[0].foo = imoto0;
       	 
   	    
   }	
   
   final static String jugadores[][] = {{"Dani Pedrosa","�ngel Nieto"},{"Toni Elias","Fonsi Nieto"},{"Sete Gibernau","Alberto Puig"}};
   
   static String pilotos[][] = 
	{
	    {"","Periggia","Tanaka","Sevilla","Trilla","Pedroti","Dangels","Tegurinni"},
	    {"","Boden","East","Proto","Depuni","Fonsi","Tolfo","Poccinali"},
	    {"","Barroco","Uhkawampa","Caperotte","Chekea","Sis�","Bianchi","Rosseta"}
	};
	
   static int atras = 4;
   static int mivuelta = 1;
   static long racetime = 0;
   static long laptime = 0;
   static long lap = 0;
   static long pos = 0;
   long starttime;
   long startlaptime;
   static int showlap;
   long agap;
   
   static int animcm[] =  {0  ,   1,   0,   1,   2,   3,   4,   4,   5,   6,   6,  7,   1,   2,   3,   4,   4,   5,   6,   6,  7};
   static int animcmy[] = {-16, -16, -16, -16, -25, -40, -50, -55, -47, -37, -26,-16, -16, -20, -30, -36, -40, -36, -28, -23,-16};
   
   static int animcp[] =  {0  ,   0,   0,   1,   1,   1,   1,   1,   2,   2,   2,  3,   3,   3,   4,   5,   6,   6,   6,   5,   6};
   static int animcpy[] = {20 ,  20,  23,  26,  30,  33,  35,  36,  34,  31,  29, 26,  24,  22,  20,  20,  20,  20,  20,  20,  20};
   
   int caidx1;
   int caidx2;
   int mseq[] = {0,1,2,1};
   int croderes;
   int avel;
   
   public void GameOver()
   {
        Pinta();
        Print(255,255,255, "GAME OVER", w/2, h/2, Graphics.HCENTER);
        Click();
        if (Rclick())
            SetEstado(40,1);
   }
   
   
   
   public void Pinta()
   {
            avel = moto[0].velocidad - avel;
   	
   	    dengine.Draw(gr, camara, circuit);	  
   	   	/*
   	    if (esengine == 0)  
   	    {     	
       	    PutSprite(maciza,  moto[0].proj[0]-(nfc*2), 135,  mseq[(nfc/2)%4], macizacor, 3);
       	    if (circuit.clima == circuit.LLUVIA || circuit.clima == circuit.TORMENTA)
       	    {
       	          int num = 15+(rnd.nextInt()%16);
       	          blit(33+(11*(2)),9,11,9,moto[0].proj[0]-(nfc*2)-2+num, 130,ifum);
       	          blit(33+(11*2),9,11,9,moto[0].proj[0]-(nfc*2)+num, 130,ifum);
       	    }
       	}*/
       	
   	    if (caida == 0) 
   	    {
   	        if (moto[0].velocidad < 54 && avel > 2)    	
   	        {        
   	            blit(0, ((moto[0].velocidad/20)%2)*42, 19,42, moto[0].proj[0]-15, moto[0].y, imotoup);   	        
   	            //blit(11*(nfc%6),0,11,9,moto[0].proj[0]-15, moto[0].y+34+((nfc+1)%2),ifum);
   	            //blit(11*((nfc+3)%6),0,11,9,moto[0].proj[0]-8, moto[0].y+34+nfc%2,ifum);
   	        }
   	        else 
   	        {
   	            moto[0].DoBlit(gr);   	            
   	            croderes += moto[0].velocidad;
   	            if (moto[0].vueltas <= circuit.vueltas)
   	            {
           	        if (croderes >= 500)   	        
           	            croderes -= 500;   	               	        
           	        else 
           	        {       	                   	            
           	            if (moto[0].velocidad > 100)
           	                PutSprite(iroderes,  moto[0].x, moto[0].y+((nfc%4)>>1), moto[0].ashape, roderescor);
           	            else PutSprite(iroderes, moto[0].x, moto[0].y, moto[0].ashape, roderescor);   
           	        }
       	        }           	   
       	        
   	        }
   	        if (moto[0].velocidad > 0)
       	        switch (circuit.clima)
       	        {
       	            case Circuit.LLUVIA:
       	            case Circuit.TORMENTA:
       	                blit(33+(11*(nfc%3)),9,11,9,moto[0].proj[0]-20, moto[0].y+33+((nfc+1)%2),ifum);
       	                blit((11*((nfc)%3)),9,11,9,moto[0].proj[0]-2, moto[0].y+33+nfc%2,ifum);
       	                            break;
       	            case Circuit.NIEVE:
       	                blit(33+(11*(nfc%3)),18,11,9,moto[0].proj[0]-20, moto[0].y+33+((nfc+1)%2),ifum);
       	                blit((11*((nfc)%3)),18,11,9,moto[0].proj[0]-2, moto[0].y+33+nfc%2,ifum);
       	                            break;
       	        }   	        
   	        if ((moto[0].salido && moto[0].velocidad > 0) || (moto[0].velocidad < 54 && avel > 3))    	
       	    {
       	            blit(11*(nfc%6),0,11,9,moto[0].proj[0]-15, moto[0].y+34+((nfc+1)%2),ifum);
   	                blit(11*((nfc+3)%6),0,11,9,moto[0].proj[0]-8, moto[0].y+34+nfc%2,ifum);
       	    }     
   	        
   	        
   	        caidx1 = moto[0].proj[0];
   	        caidx2 = moto[0].proj[0];
   	    }
   	    else
   	    {   	        
       	    if (caida < animcm.length)   	        
   	        { 
   	            if (caida == 2) Sonido(5,1);
   	            //int f = Math.min(caida, animcm.length-1);
   	            int f = caida;
   	            blit(0,61*animcm[f],30,61,caidx1,moto[0].y+animcmy[f],caidam);
   	            blit(0,52*animcp[f],39,52,caidx2,moto[0].y-animcpy[f],caidap);
       	    }       	    
       	}
   	
   	
       	gr.setColor(255,255,255);
       	gr.setClip(0,0,w,h);
       	if (esengine == 0)
       	{
       		if (atras == 4)
       		{   		
       			blit(96,49,13,13,(w/2)-26,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)-26,(h/2)+7,mon);
       			blit(96,49,13,13,(w/2)-13,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)-13,(h/2)+7,mon);
       			blit(96,49,13,13,(w/2),(h/2)-6,mon);
       			blit(96,49,13,13,(w/2),(h/2)+7,mon);
       			blit(96,49,13,13,(w/2)+13,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)+13,(h/2)+7,mon);
    			}   		
    			if (atras == 3)
       		{
       			blit(96,49,13,13,(w/2)-26,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)-26,(h/2)+7,mon);
       			blit(96,49,13,13,(w/2)-13,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)-13,(h/2)+7,mon);
       			blit(96,49,13,13,(w/2),(h/2)-6,mon);
       			blit(96,49,13,13,(w/2),(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)+7,mon);
       		}   		
       		if (atras == 2)
       		{
       			blit(96,49,13,13,(w/2)-26,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)-26,(h/2)+7,mon);
       			blit(96,49,13,13,(w/2)-13,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)-13,(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2),(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2),(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)+7,mon);
       		}
       		if (atras == 1)
       		{   		
       			blit(96,49,13,13,(w/2)-26,(h/2)-6,mon);
       			blit(96,49,13,13,(w/2)-26,(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2)-13,(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2)-13,(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2),(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2),(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)+7,mon);
    			}
       		//	Print(255,255,255, ""+atras, w/2, h/2, Graphics.HCENTER);
       	}
       	else
       	{
       		if (nfc < 50)
       		{
       			blit(96+14,49,13,13,(w/2)-26,(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2)-26,(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2)-13,(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2)-13,(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2),(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2),(h/2)+7,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)-6,mon);
       			blit(96+14,49,13,13,(w/2)+13,(h/2)+7,mon);
       		}
       	}
       	//MONITORES
       	blit(0,0,176,48,0,0,mon);   	   	
       	//PrintTime(racetime, 4, 48);
       	PrintNumB(""+moto[0].vueltas,5,10);
       	PrintNumM(""+circuit.vueltas,22,16);
       	//if (modojuego != VSTIME)
       	//{
       	    PrintNumB(""+moto[0].posicion,5,29);
       	    PrintNumM("8",22,36);//!!SE PUEDE OPTIMIZAR ;)
       	//}
       	
       	String s = ""+moto[0].velocidad;
       	if( moto[0].velocidad < 100) s = "0"+s;
       	if (moto[0].velocidad < 10 )s = "0"+s;   	
       	PrintNumS(s,110,14);
       	gr.setColor(40,70,255);
       	gr.setClip(0,0,w,h);
       	gr.drawLine(149,24,149+(Trig.cos(moto[0].velocidad+63)>>6),24+(Trig.sin(moto[0].velocidad+63)>>6));
       	//LINIA DE POSICIONES
       	gr.setColor(255, 255, 255);
       	gr.fillRect(10,6,circuit.vueltas*(circuit.vuelta/20),2);
        for(int i = 1;i < circuit.vueltas;i++) 
       	    gr.drawLine(10+i*(circuit.vuelta/20),5,10+i*(circuit.vuelta/20),9);
       	blit(61,56,4,4,8,5,mon);
       	blit(66,56,4,4,circuit.vueltas*(circuit.vuelta/20)+10,5,mon);
       
        if (modojuego == VSTIME)
            blit(81,56,4,4,moto[0].recorrido/(20*250)+9,5,mon);
        else
        {
           	for(int i = 0;i < MAXCORREDORES;i++)            	
           	    if (moto[i].vueltas <= circuit.vueltas)
           	        switch(i)
           	        {
           	            case 6:blit(71,56,4,4,moto[i].recorrido/(20*250)+9,5,mon);break;
           	            case 7:blit(76,56,4,4,moto[i].recorrido/(20*250)+9,5,mon);break;
           	            case 5:blit(86,56,4,4,moto[i].recorrido/(20*250)+9,5,mon);break;
           	            case 0:blit(81,56,4,4,moto[i].recorrido/(20*250)+9,5,mon);break;       	            
           	        }           	               	
       	}    
       	//
       	if (showlap > 0)
       	{
       		PrintTime(moto[0].laptimes[moto[0].vueltas-2], 5, 48);
       		PrintTime(racetime, 5, 60);
       	}
       	else if (modojuego == VSTIME) PrintNumB(""+tiempo, 5,48);
        	
        	
        blit(87,82,13,9,10,h-10,mon);
        	
       	if (endrace > 6) BanderaCuadros();   	
   	
   	    /*Tramo actual = Game.dengine.tramo[14];
		if (actual.extra == -2 || actual.extra == -4 || actual.extra == -5)
		{
		    System.out.println(actual.jextra[0]+" VS"+moto[0].proj[0]);
		   }*/
		avel = moto[0].velocidad;   
   }
   
   
   static int endrace = 0;
   
   public void Engine()
   {
   	nfc++;	
   	if (esengine > 0) UpdateSprite();         	       	
   	
   	//UPDATEMOTOS   	
   	
   	if (esengine == 0)
   	{
   	    if (atras == 3) Game.Sonido(3,1);
   		if (nfc%20 == 0) atras--;
   		if (atras == 0) {esengine = 1;nfc = 0;starttime = System.currentTimeMillis();startlaptime = starttime;agap = starttime;}
   	}
   	if (esengine > 0)
   	{
   		 long now = System.currentTimeMillis();
   		 if (endrace == 0)
   		 {
   		    racetime = now-starttime;
            laptime = now-startlaptime;
         }
         long gap = now-agap;
         agap = now;
         
         
         switch(modojuego)
         {
                case VERSUS:
         	            moto[0].Update(circuit,gap);
         	            moto[1].UpdateNet(circuit);           
         	            break;                        
         	    case VSTIME:
         	            if (nfc%16 == 0) tiempo--;    
         	            if (tiempo == 0) SetEstado(13,1);
                default:            
                        for(int i = 0;i < 8;i++)
	   		            {
	   			            if (i == 0) Game.moto[i].Update(circuit,gap);
		   		            else Game.moto[i].UpdateCPU(circuit,gap);		   	
	   		            }
         }         
   	}
   	circuit.Traspasa(dengine);
   	   
   	if (mivuelta != moto[0].vueltas) {showlap = 40;mivuelta = moto[0].vueltas;
   	    Sonido(7,1);
   	    if (modojuego == VSTIME) tiempo += 40-Moto.dificultad*5;
   	    }
   		   	
   	//moto[0].Update(circuit);
   	
   	if (caida == 0)
   	{
   	    if (moto[0].pos[0].x-50 > camara.x) camara.x +=12;
   	    if (moto[0].pos[0].x > camara.x) camara.x +=6;
   	    if (moto[0].pos[0].x+50 < camara.x) camara.x -=12;
   	    if (moto[0].pos[0].x < camara.x) camara.x -=6;
   	}
   	if (camara.x > 120) camara.x = 120;
   	if (camara.x < -120) camara.x = -120;
   	
   	if (endrace == 0) circuit.pos = moto[0].pos[0].z-5;   	   	
    
   	//moto[0].y = circuit.tramo[circuit.begin].pos.y-55;
   	camara.y = circuit.tramo[circuit.begin].pos.y-55;
   	camara.z = circuit.tramo[circuit.begin].pos.z;
    
   
   	Pinta();
   	if (endrace == 0) 
   	{
       	circuit.pos = moto[0].pos[0].z-5;   	   	
       	//CALCULA LA POSICION DE LAS MOTOS;
       	if (nfc%4 == 0) 
       	{
       	    int pop = moto[0].posicion;
       	    Ordena(0);
       	    if (nfc > 100 && pop != moto[0].posicion) Sonido(4,1);       	    
       	}
    }   		
   	if (moto[0].vueltas > circuit.vueltas)    	
   	{   		
			endrace++;   	
			if (endrace == 4) quad = 0;
			if (endrace > 30) {estado = 18;currentkey = 0;apretando=false;
			if (circuit.current == 8) estado = 19;
			}			
   	}
   	/*for(int i = 0; i < 256;i+=1)
   	{
   		int x 
   		int f = (circuit.vuelta*i)/256;
   		//if (i%20 == 0) gr.drawString(""+f,w/2+(Trig.cos(i)>>5),h/2+(Trig.sin(i)>>5), Graphics.LEFT | Graphics.TOP);
   		gr.drawRect(w/2+(Trig.cos(i)>>5)+((Trig.cos(i)*circuit.tramo[f].curva)/40000),70+(Trig.sin(i)>>6)+((Trig.sin(i)*circuit.tramo[f].curva)/40000),1,1);
   	}
   	gr.setColor(255,0,0);
   	int f = (circuit.vuelta*circuit.begin)/256;
   	gr.drawRect(w/2+(Trig.cos(circuit.begin)>>5)+((Trig.cos(circuit.begin)*circuit.tramo[f].curva)/40000),70+(Trig.sin(circuit.begin)>>6)+((Trig.sin(circuit.begin)*circuit.tramo[f].curva)/40000),1,1);*/
   	
   	/*for(int i = 0; i < circuit.vuelta;i+=4)
   	{
   		gr.drawRect(i/4,40-circuit.tramo[i].curva/20,1,1);
   	}
   	gr.setColor(255,0,0);
   	gr.drawRect(circuit.begin/4,40-circuit.tramo[circuit.begin].curva/20,1,1);*/
   	///////////////////
   	//gr.drawString(circuit.begin+"/"+circuit.vuelta,0,0, Graphics.LEFT | Graphics.TOP);
   	        
 		if (showlap > 0) showlap--;         
 		//if (nfc%4 == 0) 
 		if (caida > 0)
 		{ 		 
 		    caida++;
 		    caidx1 -= moto[0].desplazamiento/16;
 		    caidx2 += moto[0].desplazamiento/16;
 		    moto[0].velocidad -=10;
 		    if (moto[0].velocidad < 0) moto[0].velocidad = 0;
 		    if (caida > 30)
 		    {
 		        moto[0].pos[0].x = ((circuit.tramo[circuit.begin].pos.x+circuit.tramo[circuit.begin].curva)*2)/3;
 		        caida = 0;
 		        moto[0].inclinacion = 0;
 		        moto[0].desplazamiento = 0;
 		        //if (modojuego == VSTIME && tiempo == 0) SetEstado(13,1);         	    
 		    }
 		}
   }
   
   
   int posiciones[] = new int[MAXCORREDORES];
   
   public void Ordena(int campo)
   {
            int vecpos[] = new int[8];
           	for(int j = 1;j < 9;j++)   	
           	{
           		int max = -1;
           		int imax = -1;
           		for(int i = 0;i < 8;i++)
           		{
           		    int valor;
           		    if (campo == 0) valor = moto[i].recorrido;
           		    else valor = moto[i].puntos;
           			if (valor > max && vecpos[i] == 0)
           			{
           				max = valor;
           				imax = i;   				
           			}
           		}	
           		vecpos[imax] = 1;
           		posiciones[j-1] = imax;
           		moto[imax].posicion = j;   		
           	}
   }

   public void run()
   {
   	//TODO: poner una rutina de sleep decente
	   while (!end)
	   {
	        long ttiempo = System.currentTimeMillis();	        
	        SoundRUN();
	        repaint();
	        serviceRepaints();
	        ttiempo = System.currentTimeMillis()-ttiempo;
	        if (ttiempo >= 35) continue;	      
	        try
	        {   flujo.sleep(35-ttiempo);           
            }catch(java.lang.InterruptedException e){}
            
            //e.printStackTrace();
	        //e.toString();
            
	   }	
	   Moto.joc.destroyApp (true);
	}

   
   public void keyPressed(int keyCode)
   {
      currentkey = keyCode;
      rkey = keyCode;
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

    private void UpdateSprite()
    {      
      int xdir = 0, ydir = 0;
      if (apretando)
    	   switch(currentkey)	
    	   {
              case Canvas.UP:
              case Canvas.KEY_NUM2: 
              case Canvas.FIRE:
                                    ydir = -1;
                                    break;
              case Canvas.DOWN: 
                  ydir = 1;                                     
                  break;
              case Canvas.KEY_NUM8: 
                  ydir = 1;                                                      
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
                  ydir = -1;
                  xdir = -1;
                  break;
              case Canvas.KEY_NUM3:
                  ydir = -1;
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
               case Canvas.KEY_STAR:		
               case Canvas.KEY_POUND:		
               
                       estado=4;currentkey=0;break;              
              //case Canvas.KEY_STAR:dengine.resolution=1;break;
              //case Canvas.KEY_POUND:dengine.resolution=2;break;
           }
           if (rkey==-6){estado=4;rkey=0;}
           if (ydir <0 ) moto[0].acelera();
           if (ydir >0 ) moto[0].frena();
           
           //if (ydir >0 ) circuit.pos+=125;
           //if (ydir <0 ) circuit.pos+=75;
           if (xdir < 0) {moto[0].gira(1);}
           if (xdir > 0) {moto[0].gira(-1);}
           //if (xdir < 0) dengine.gradox+=1;
           //if (xdir > 0) dengine.gradox-=1;           
           //if (xdir > 0) dengine.gradoz+=5;
           //if (xdir < 0) dengine.gradoz-=5;           
           //if (ydir < 0) dengine.gradoy-=20;
    }
	
	
	
    public Game()
 	{
        w = getWidth();
        h = 208;//getHeight();      
        buildsfx();
        menus = new MMenu();
	    flujo=new Thread(this);
	    flujo.start();
  	    System.gc();
  	}
  
  	int quad = 0;
  	
   public void BanderaCuadros()
   {
   	    for (int i = 0; i < quad;i++)
  			for (int j = 0; j < quad;j++)
  			{
  				if (i+j > quad) continue;
  				int colo = ((i+j)%2)*255;
  				gr.setColor(colo,colo,colo);
  				gr.setClip(i*20,j*20,20,20);
  				gr.fillRect(i*20,j*20,20,20);
  			}
        quad++;	   	
        if (moto[0].posicion == 1)
        {
            Print(0x76,0x94,0xff, "La victoria es Azul", w/2, h/2, Graphics.HCENTER);
            /*SetPrint(0x7694ff, w/2, h/2, Graphics.HCENTER);
            MultiPrint(Lang.azul);
            SetPrint(0,0, 0);*/
        }
   }
  
  
  	public static void SetEstado(int est, int pres)
  	{  	    
  		currentkey = 0;
  		apretando = false;
  		estado = est;
  		prestado = pres;
  		rkey = 0;
  		menus.intro = -31*20;
  		menus.opmenu = 0;
  		menus.wmop = 0;
  	}
  
    public static boolean Rclick()
    {
        if (apretando && (rkey == -7 || currentkey == Canvas.FIRE)) return true;
        return false;
    }
    
  
    public void FinishChampionship()
  	{
  	    blit(0,0,176,208,0,0,imenu);	  		  		
  	    Print(255,255,0,Lang.champend,w/2, 30, Graphics.HCENTER);
  	    //Ordena(1);
  	    for(int i = 0;i < 8;i++)
  	    {
  	        int c = 255;
  	        if (posiciones[i] == 0) c = 0;  	        
  	        Print(255,255,c,(i+1)+". "+moto[posiciones[i]].piloto,50, 55+(i*18), Graphics.LEFT);
  	        Print(255,255,c,""+moto[posiciones[i]].puntos,150, 55+(i*18), Graphics.RIGHT);  	        
  	    }
  	    Click();
  		if (Rclick())
  		{
  		    //MIRO SI HA QUEDADO ENTRE LOS 3 PRIMENS
  		    if (posiciones[0] == 0 || posiciones[1] == 0 || posiciones[2] == 0)
            {
                Moto.joc.joculto[Moto.dificultad-1] = true;
                Moto.joc.PrefsSET();
            }          		    
  		    if (numcarrera == MAXCIRCUITOS-1) SetEstado(40,1);  		      		
  		}    
    }

    
  
    public void FinishShowClass()
  	{
        blit(0,0,176,208,0,0,imenu);	  		  		
  	    Print(255,255,0,Lang.champclassification,w/2, 30, Graphics.HCENTER);
  	    Ordena(1);
  	    for(int i = 0;i < 8;i++)
  	    {
  	        int c = 255;
  	        if (posiciones[i] == 0) c = 0;  	        
  	        Print(255,255,c,(i+1)+". "+moto[posiciones[i]].piloto,50, 55+(i*18), Graphics.LEFT);
  	        Print(255,255,c,""+moto[posiciones[i]].puntos,150, 55+(i*18), Graphics.RIGHT);
  	        
  	    }
  	    Click();
  		if (Rclick())
  		{ 
  		    if (numcarrera == MAXCIRCUITOS-1) SetEstado(21,1);
  		    else
  		    {
  		        SetEstado(11,1);
	        }
  		}
    }
    
    public void ViewRecords1()
  	{
        blit(0,0,176,208,0,0,imenu);	  		  		
  	    Print(255,255,0,Lang.best1,w/2, 30, Graphics.HCENTER);
  	    for(int i = 0;i < 8;i++)
  	    {
  	        Print(255,255,255,Lang.circuitos[i],6, 55+(i*18), Graphics.LEFT);
  	        Print(255,255,255,Moto.racenames[i],80, 55+(i*18), Graphics.LEFT);
  	     	PrintTime(Moto.racetimes[i], 108, 55+(i*18));//cambiar	   		   	       
  	    }
  	    Click();
  	    if (Rclick()) SetEstado(43,1);
    }
  
    public void ViewRecords2()
  	{
        blit(0,0,176,208,0,0,imenu);	  		  		
  	    Print(255,255,0,Lang.best2,w/2, 30, Graphics.HCENTER);
  	    for(int i = 0;i < 8;i++)
  	    {
  	        Print(255,255,255,Lang.circuitos[i],6, 55+(i*18), Graphics.LEFT);
  	        Print(255,255,255,Moto.lapnames[i],80, 55+(i*18), Graphics.LEFT);
  	     	PrintTime(Moto.laptimes[i], 108, 55+(i*18));//cambiar	   		   	       
  	    }
  	    Click();
  	    if (Rclick()) SetEstado(4,1);
    }
  
  
    public void FinishShowEnd()
  	{
        blit(0,0,176,208,0,0,imenu);	  		  		
  	    Print(255,255,0,Lang.raceclassification,w/2, 30, Graphics.HCENTER);
  	    for(int i = 0;i < 8;i++)
  	    {
  	        if (posiciones[i] == 0) Print(255,255,0,(i+1)+". "+moto[posiciones[i]].piloto,50, 55+(i*18), Graphics.LEFT);
  	        else Print(255,255,255,(i+1)+". "+moto[posiciones[i]].piloto,50, 55+(i*18), Graphics.LEFT);
  	    }
  	    Click();
  		if (Rclick())
  		{ 
  		    if (modojuego == CAMPEONATO)
  		    {
  		        SetEstado(20,1);
  		        //LE A�ADO LOS PUNTOS DE LA CARRERA
  		        for(int i = 0;i < MAXCORREDORES;i++)
  		            moto[posiciones[i]].puntos += puntoscarreta[i];
  		    }
  		    else
  		        SetEstado(40,1);
  		}
    }
    
    int tim;
    
    public void FinishShowStatus()  	
  	{
  	    tim++;
  	    long bestlap = 3600000-1; 
  	    for(int i = 0; i < circuit.vueltas;i++)  	    
  	        if (moto[0].laptimes[i] < bestlap) bestlap = moto[0].laptimes[i];  	    
  	    
  	    if (racetime < Moto.racetimes[circuit.current]) 
  	    {
  	        Moto.racetimes[circuit.current] = racetime;
  	        Moto.racenames[circuit.current] = Moto.jugador;
  	    }
  	    if (bestlap < Moto.laptimes[circuit.current])
  	    { 
  	        Moto.laptimes[circuit.current] = bestlap;
  	        Moto.lapnames[circuit.current] = Moto.jugador;
  	    }
  	    
        blit(0,0,176,208,0,0,imenu);               
        int ol = tim%(176*2);
        if (ol < 0) ol+=176*2;
        gr.setColor(Game.circuit.cielo[0], Game.circuit.cielo[1], Game.circuit.cielo[2]); 		
 	    gr.fillRect(0,67,176,45);
        blit(0, 0, 176*2, 45, -(ol)-176, 67, Game.fondo[0]);
		blit(0, 0, 176*2, 45, -(ol)+176, 67, Game.fondo[0]);
		ol = (tim*2)%(176*2);
        if (ol < 0) ol+=176*2;                  
   		blit(0, 0, 176*2, 45, -(ol), 67, Game.fondo[1]);
   		blit(0, 0, 176*2, 45, -(ol)+176*2, 67, Game.fondo[1]);
   				   
   		Print(255,255,0,circuit.name,w/2, 40,Graphics.HCENTER);                                                               		
   		Print(255,255,0,moto[0].piloto,14, 120,Graphics.LEFT);                                                            
   		Print(255,255,0,Lang.record,94+20, 120,Graphics.LEFT);                                                            		      		
   		Print(255,255,0,Lang.racetime,w/2, 140,Graphics.HCENTER);                                                            
   		PrintTime(racetime, 14, 140+16);	   		
   		PrintTime(Moto.racetimes[circuit.current], 94, 140+16);//cambiar	   		
   		Print(255,255,0,Lang.laptime,w/2, 170,Graphics.HCENTER);//cambiar                                                 		              
   	    PrintTime(bestlap, 14, 170+16);
        PrintTime(Moto.laptimes[circuit.current], 94, 170+16);   				   
        Click();
        if (Rclick()) 
        {
            if (modojuego == VSTIME) SetEstado(40,1);
            else SetEstado(19,1);
        }    
    }    
        
    public static void ResetGame()
    {
        numcarrera = 0;
        for(int i = 0;i< MAXCORREDORES;i++) moto[i].puntos = 0;            
        ResetRace();
    }
    
    public static void ResetRace()
    {
        endrace = 0;
        nfc = 0;
        for(int i = 0;i< MAXCORREDORES;i++)
            moto[i].Reset();
        atras = 4;
        mivuelta = 1;
        racetime = 0;
        laptime = 0;
        lap = 0;
        pos = 0;
        showlap = 0;                  
        esengine = 0;
        camara.x = -300;        
    }
    
    int ccolor = 0x1C5BA2;
    
    void Logos()
    {
        nfc++;
        if (nfc == 1) {imenu = loadImg("/movi");Sonido(8,10);}
        gr.setClip(0, 0,w, h); 
        gr.setColor(ccolor);
        gr.fillRect(0,0,w,h);
        gr.drawImage(imenu,w/2, h/2, Graphics.HCENTER|Graphics.VCENTER);	         
        if (nfc == 10) {
            Inicializacion1();
            imenu = loadImg("/micro");ccolor=0xffffff;}   	
        if (nfc == 20) estado = -21;
    }
    
    public void paint (Graphics g)
    {         
   	    gr = g;   
   	    dgr = DirectUtils.getDirectGraphics(g);
   	    switch (estado)
   	    {
   	        case -2:    if (modojuego == CAMPEONATO) IniGame(Moto.gpdificultad);
   	                    else IniGame(Moto.dificultad);
   	                    break;       		
   	        case -1:    Logos();break;
   	        case -21:   Inicializacion();break;       		
   		    case 0:     if (!paused) Engine();break;
       		case 6:     end = true;break;
       		//case 10:    NewChampionship();break;
       		//case 11:    ContinueChampionship();break;       		
       		case 13:    GameOver();break;
       		case 18:    FinishShowStatus();break;
       		case 19:    FinishShowEnd();break;
       		case 20:    FinishShowClass();break;
       		case 21:    FinishChampionship();break;       		
       		case 42:    ViewRecords1();break;       		
       		case 43:    ViewRecords2();break;       		
       		default:    estado = menus.show(gr,currentkey, apretando,w,h); 	        	  
   	    }
    }

    
    /*
    public static void Zum(int freq)
    {    	
    	if (Moto.sfx) 
    	{
    	   	        if (bp[0].getState() == Sound.SOUND_PLAYING) bp[0].stop(); 
    	   	        try{
    	   	
    	   	            //bp[0] = new Sound(freq, 100);    
    	   	            bp[0].init(ringfreq[freq]+nfc%4,1000);
    	   	            bp[0].play(10);	      
    	   	        }
    	   	        catch(Exception e){System.out.println("dsds");}    	   	
    	 }       
    }*/
  
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
        if (s <= 2 || s == 8) 
        {
            
            if (s<=2) SoundSET(s,c);
            else SoundSET(3,c);
            return;
        }
        	        	
        //if (c == 0) lastsfx = -1;
    	/*if (Moto.sfx)
    	{
    	    try{
    	   	    if (lastsfx >= 0 && bp[lastsfx].getState() != Sound.SOUND_STOPPED) 
    	   	        if (s != lastsfx) bp[lastsfx].stop(); 
    	   	       
    	   	    
    	   	        if (s == lastsfx && bp[lastsfx].getState() != Sound.SOUND_STOPPED) return;
    	   	        bp[s].play(c);	      
    	   	    }
    	   	catch(Exception e){}
    	   	lastsfx = s;
    	 }    */
    }
   
   
    public void ISound(int k, String s)
    {/*
    	try
      {       
    		byte[] b = a(s);
    		bp[k] = new Sound(b,1);
    		bp[k].init(b, 1);           
    		bp[k].setGain(50);
    	}
       catch(Exception exception) { }	*/
    }
    

    public void buildsfx()
    {    	
       bp = new Sound[9];
       //PRESENTACION
       //ISound(0,"024A3A71C1C995CD95B9D1858DA5BDB80401011E618620620A26C4986188188289B12620620A26C498628B412620A2B04988289B12618620620A26C4986188188289B12618620620618A2D0498828AC12620A26C4966168168288B125985A05A0A22C4968168288B12598A2904968289C125A0A22C4966168168288B12598620620A26C4988188289B12618A2D0498828AC12620A26C49B81B81B81B81B828AA08C08C08C08C08C0C20000");
       //AMBIENTAL
       //ISound(1,"024A3A6585B589A595B9D185B00400711C69AA35049A628C31269AA35049A69A828AA0D309B084126A05A06A0A2AD49A81A6A8D412698A30C49A6A8D41269AA2702A820821049A828B409C0AB126A0A2D000");
       //SETTINGS
       //ISound(2,"024A3A61CD95D1D1A5B99DCC0400471E550A2AD27020C26C2AC498615628AB49C08312558560A270270495428AB49C08309B0AB12618558A2AD27000");
       //EMPEZAR
       ISound(3,"024A3A5D95B5C195E985C804006320A2284918168188288C125A0620A2302D04988288C0B40C40B40C40B408C0C40B408C12620A2D02304988168288C126205A04606205A04605A0620A228000");
       //ADELANTAMIENTO
       ISound(4,"024A3A79859195B185B9D185B5A595B9D1BC040019327204285A04286204206A0428A2D0230000");
       //CAIDA
       ISound(5,"024A3A558D85A5918404002328A2CC2702B023027049C8288C126A07206206A0598000");
       //TOQUE
       ISound(6,"024A3A55D1BDC5D59404000D24A31022C2CC2D0000");
       //VUELTA
       ISound(7,"024A3A59D9D595B1D18404000D22718720720A30C000");
       //MOVISTAR
       //ISound(8,"024A3A6DB5BDD9A5CDD185C97CD0D40400B31E710420720A26C49C61A61C61081C61C61A81C81A61761081C41781C81381C41081C8289B127186987184207187186A07206985D84205D8720A26C21049761C8289B126E07207104206E05E07186E04287184185D87184D85D07185E07204D84E05A0720420A27049C610628AC0AB127184207207206A05D8718000");
       SoundINI();
    }
	
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

	Sonidos = new Player[4];

	Sonidos[0] = SoundCargar("/menu.mid");	
	Sonidos[1] = SoundCargar("/ambiental.mid");
	Sonidos[2] = SoundCargar("/settings.mid");
	Sonidos[3] = SoundCargar("/movi.mid");

	//Sonidos[4] = SoundCargar("under_item.mid");

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
    //if (Ary != 0) return;
	if (Moto.sfx)
	{
		
		if (Ary == SoundOld && SoundTime > 0) return;
		//SoundTime = new int[] {300,3000,3000,300,3000}[Ary];
        int SoundTimeA[] = {300,250,250,400};
		if (Loop==0) {Loop--;}

		if (SoundOld!=-1) {SoundRES();}

		try
		{
		VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
		if (Sonidos[Ary] == null) return;
		SoundTime = SoundTimeA[Ary];
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

public void SoundRUN()
{
	if (SoundOld!=-1 && --SoundTime==0) {SoundRES();}
}

	
	
}//END midlet


   