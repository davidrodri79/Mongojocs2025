package com.mygdx.mongojocs.frozenworld;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class GameCanvas extends BiosCanvas
{
public GameCanvas(Game ga) {super(ga);}

// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//

// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// *******************************************
// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// Terminal: 

// ---------------------------------
//  Bits de Control de cada Terminal
// =================================
final boolean deviceSound = true;
final boolean deviceVibra = false;
// ----------------------------------

// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


// *******************
// -------------------
// canvas - Engine
// ===================
// *******************

// -------------------
// canvas Init
// ===================

public void canvasInit()
{
   
	soundCreate();	
	canvasTextCreate(8, 4, canvasWidth-16, canvasHeight-8);
	sc = new Scroll();
	sc.ScrollINI(canvasWidth, canvasHeight);
	ga.prota = new prota(this, loadImage("/sprite.png"), 24, 24, 6, 0, 0, ga.loadFile("/sprite.cor"));
	imgNumbers =  loadImage("/numeros.png");
    back = ga.loadFile("/back.map");
    ga.sCheat = ga.stringCheat;
}


// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{

    
	if (ga.playIntro) { ga.playIntro=false; drawIntro(); }
	if (ga.playShow) { ga.playShow=false; playDraw(); }
	if (ga.playEnd) { ga.playEnd=false; ztextdraw(ga.gameText[ga.TEXT_CONGRATULATIONS][0], 0,1, 0xffffff, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE); }


}

// <=- <=- <=- <=- <=-










// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************

// -------------------
// play Create Gfx
// ===================

public void playCreate_Gfx()
{
}

// -------------------
// play Destroy Gfx
// ===================

public void playDestroy_Gfx()
{
    imgEnemies = null;	
    corEnemies = null;
	ga.malo = null;
	tileset = null;
	tips = null;
	sc.FaseMap=null;
	//sc.FondoMap=null;
	sc.TilesImg=null;
	//sc.FondoGfx=null; 
	//sc.FondoImg=null;
	System.gc();
}

// -------------------
// play Init Gfx
// ===================

public void playInit_Gfx()
{
}

// -------------------
// play Release Gfx
// ===================

public void playRelease_Gfx()
{
}

// -------------------
// play Draw Gfx
// ===================


public void playDraw()
{	
		if (showtip != -1 && lasttip == showtip) return;
		
	  tx = scale(ga.prota.x +(ga.prota.width>>1)) + (ga.prota.sidex*(canvasWidth>>3));
	  ty = scale(ga.prota.y + (ga.prota.height>>1)) - (canvasHeight>>4) ;
	  if  (!ga.freezed)
	  {
		  if (offsetx < tx) offsetx += scale(3)+1;
		  if (offsetx > tx) offsetx -= scale(3)+1;
		  if (!ga.prota.grounded || keyY <= 0)
		  {
			if (offsety < ty) offsety += scale(4)+1;//ZNR
			if (offsety > ty) offsety -= scale(4)+1;
		  }	
	  }
	  if (Math.abs(offsetx-tx) > canvasWidth>>1 || Math.abs(offsetx-tx) < scale(3)+1) offsetx = tx;
	  if (Math.abs(offsety-ty) > scale(64)  || (Math.abs(offsety-ty) < scale(4)+1 && keyY<=0)) offsety = ty;

	  	int ex = 0, ey = 0;
	    if (ga.prota.faseacabada > 20) 
	    {
	        //TERREMOTO
	        ex = (ga.nfc*13)%3;
	        ey = (ga.nfc*7)%4;
	        if (ga.prota.faseacabada == 21) vibraInit(1500);
	   }
	    sc.ScrollRUN_Centro_Max(offsetx+scale(ex), offsety+scale(ey));

	    sc.ScrollIMP(scr);
	    for(int i = 0; i < ga.malo.length;i++) if (ga.malo[i] != null && ga.malo[i].active) ga.malo[i].blit(scr);	    
	    drawMonitor();	    
	    ga.prota.blit(scr);
	    if (ga.prota.shot.active)
	    {
		    if (ga.prota.shot.estado == 0)
		    {			  
			    ga.prota.shot.blit(scr);
			    //scr.fillArc(-sc.ScrollX+ga.prota.shot.x , -sc.ScrollY+ga.prota.shot.y-((ga.prota.shot.timing%5)/2), 10 ,5+(ga.prota.shot.timing%5), 0, 360);
		   }	    
		    else
		    {
			    scr.setClip(0,0,canvasWidth, canvasHeight);	
			    //scr.setColor(255,(ga.prota.shot.timing*25)%255,0);
			    scr.setColor((ga.prota.shot.timing*25)%255,(ga.prota.shot.timing*25)%255,255);		    		    
					    
			    for(int i = -1;i < 2;i++)
				    for(int j = -1;j < 2;j++)
				    {
					    int step = ga.prota.shot.timing*3;
					    int radius = 12 - (ga.prota.shot.timing);
					    int len = 200;
					    if (Math.abs(i) == 1 && Math.abs(j) == 1) len = 144;
					    step = (step*len)/ 200;					     
					    scr.fillArc(-sc.ScrollX+scale(ga.prota.shot.x+(step*i) -(radius/2)), -sc.ScrollY+scale(ga.prota.shot.y+(step*j)-(radius/2)), scale(radius) ,scale(radius), 0, 360);
				    }
		    }
	    }
	    if (ga.prota.inmunity > 0)  PutSprite(ga.prota.foo,  -sc.ScrollX+scale(ga.prota.x), -sc.ScrollY+scale(ga.prota.y),  (24)+(ga.nfc%6), ga.prota.acor, NUMTROZOS);
	    
	    scr.setClip(0,0,canvasWidth, canvasHeight);
	    scr.setColor(0);	    
	    if(ga.prota.lastCell > 0)
	    {
		    ga.prota.lastCell++;
		     int step = ga.prota.lastCell ;
		     int x =  -sc.ScrollX + scale(ga.prota.x + (ga.prota.width>>1));
		     int y =  -sc.ScrollY + scale(ga.prota.y + (ga.prota.height>>1));			    
		     scr.setClip(0,0,canvasWidth, canvasHeight);
		     if (step > 8) step = 15 - ga.prota.lastCell; 		    			     
		     for(int i = 0; i < 5;i++)
		     {		
			     int istep = (5-i)*step;
			     switch(ga.prota.colorCell)
			     {
				     case 0:scr.setColor(i*50,255,i*50);break;
				     //case 1:scr.setColor(255,255,i*50);break;
				     case 1:scr.setColor(i*50,255,255);break;
				     case 2:scr.setColor(255,i*50,i*50);break;
			     }
			     istep = scale(istep);
			    scr.fillRect(x - istep, y-istep, 3, 3);
			    scr.fillRect(x - istep, y+istep, 3, 3);
			    scr.fillRect(x + istep, y+istep, 3, 3);
			    scr.fillRect(x + istep, y-istep, 3, 3);
			    //scr.fillArc(x - istep, y-istep, istep*2, istep*2, 0, 360); 
			    //scr.fillRect(x-(step*(10-i)), 0, 2*(step*(10-i)), canvasHeight);
		     }
		     if (ga.prota.lastCell > 15) ga.prota.lastCell = 0;		    		     
	    }
	    if (ga.stage == 15) PutSprite(imgEnemies,  -sc.ScrollX+scale(2*unit.TILED), -sc.ScrollY+scale((8*unit.TILED)-4),  (14*7)+3, corEnemies, NUMTROZOS);
	    drawEndStage();	    	    
	    drawTips();
}

// <=- <=- <=- <=- <=-



/*public void drawShield(int f, int c)
{
		int ab[] = {0,17,28,46,64,72,83,100};
		int as[] = {17,25,35,48,52,65,75,83};
		  scr.setClip(0,0,canvasWidth, canvasHeight);
		    scr.setColor(c);
		    int yb=(ab[f]*ga.prota.height)/100; 
		    int ys=(as[f]*ga.prota.height)/100;
		    ys += ga.prota.y-sc.ScrollY;
		    yb += ga.prota.y-sc.ScrollY;
		    int x = ga.prota.x-sc.ScrollX;
		    int y = ga.prota.y-sc.ScrollY;
		    scr.drawLine(x ,  y+(ga.prota.height/2), x+(ga.prota.width/8), ys);
		    scr.drawLine(x+(ga.prota.width/8), ys, x+ga.prota.width-(ga.prota.width/2), yb);
		    scr.drawLine(x+(ga.prota.width/2), yb, x+ga.prota.width-(ga.prota.width/8), ys);
		    scr.drawLine(x+ga.prota.width-(ga.prota.width/8), ys, x+ga.prota.width ,  y+(ga.prota.height/2));
	    }
*/	    

int lasttag;
	    
public void drawMonitor()
{
	//int l = canvasWidth /  8;
	//int gap = canvasWidth /  16;
	//int h = canvasHeight /  20;
	//int y = (canvasHeight>>5)+5;
	int y = (canvasHeight>>5);

	//ENERGY
	showImage(tileset, scale(unit.TILED)*7,scale(unit.TILED)*5, scale(unit.TILED),scale(unit.TILED), scale(32), y);
	scr.setClip(0,0,canvasWidth, canvasHeight);
	y = y - 10 + unit.TILED;
	for (int i = 0; i < ga.prota.tag;i++)
	{
		if (lasttag > ga.prota.tag*100)
		{lasttag -= 10;if (ga.nfc%2 == 0) break;}
		if (lasttag < ga.prota.tag*100)
			lasttag = ga.prota.tag*100;
		int x = scale(32+12 + (i*6));
		scr.setColor(0xffffff);
		scr.fillRect(x, y , scale(6), scale(6));
		scr.setColor(0xffc060);		
		scr.drawRect(x, y, scale(6), scale(6));
		
	}
	y = (canvasHeight>>5);
	/*for (int i = -1; i < ga.prota.tag-1;i++)
	{
		int x = (canvasWidth>>1) + (i*(l+gap)) - (l>>1);
		scr.setColor(0x0000ff);
		scr.fillRect(x, y , l, h);
		scr.setColor(0xffffff);
		scr.drawRect(x, y, l, h);
	}*/
	
	//CELULAS	
	if (ga.nbatteryes == 0)
	{
		if (ga.nfc%6 < 3) showImage(tileset, scale(unit.TILED)*2,scale(unit.TILED)*5, scale(unit.TILED),scale(unit.TILED), scale(2), y);//drawString("exit", 1,1, 0);
	}
	else {
		showImage(tileset, scale(unit.TILED)*4,scale(unit.TILED)*5, scale(unit.TILED),scale(unit.TILED), 0, y);
		showImage(imgNumbers, 0, 0, 9,12, scale(2+(unit.TILED/2))+2,y+scale(unit.TILED-12));
		showImage(imgNumbers, 9+(9*ga.nbatteryes), 0, 9,12, scale(2+(unit.TILED/2)+9)-1,y+scale(unit.TILED-12));
	}
	//BALAS
	if (ga.prota.nshots > 0)
	{
	    showImage(tileset, scale(unit.TILED)*6,scale(unit.TILED)*5, scale(unit.TILED),scale(unit.TILED), canvasWidth-scale(30), y);		
		showImage(imgNumbers, 0, 0, 9,12, canvasWidth-scale(30)+scale(unit.TILED/2)+4  ,y+scale(unit.TILED-12));
		showImage(imgNumbers, 9+(9*ga.prota.nshots), 0, 9,12, canvasWidth-scale(30)+scale((unit.TILED/2)+9),y+scale(unit.TILED-12));
		
	}
	
	
}







// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//













Scroll sc;
//final static int LST_DOUBLE[] = {3,4,5,8,9,10,11,12,13,34,35,36,37,44,45, 46};

final static int LST_SIMPLE[] = {16,17,18,19,20,21,  24,25,26,27,28,29,   32,33,   40, 41};
final static int LST_SOLID[] = {6,7,14,15,22,23,30,31, 8,9,10};
final static int AIR[] =  {16,17,18,19,20,21,24,25,26,27,28,29,32,33,40,41};
final static int LST_AIR[] = {16,17,18,19,20,21,24,25,26,27,28,29,32,33,34,35,36,40,41,42,43,44, 3,4,5, 37, 45, 11, 12, 13, 46, 47};
final static int LST_ICE[] = {0,1,2};
final static int REACTOR[] = {35};
final static int BATTERY = 44;
final static int SHOT = 46;
final static int ENERGY = 47;
final static int BREED[] = {42, 43};
final static int SPIT[] = {37, 45};
final static int BEGDESTROY = 3;
final static int BEGCREATE = 8;
final static int BEGGEISER = 11;
final static int LST_GEISER[] = {11,12,13};


int tx,ty;


	//////////////////////////RUTINAS PRINT/////////////////////////////
    /*static int xprint, yprint, cprint, bprint, alignprint, startsc;
    final static int        GLEFT       =   Graphics.LEFT;
    final static int        GRIGHT      =    Graphics.RIGHT;
    final static int        GHCENTER     =   Graphics.HCENTER;
    
    final static int        LINEX       =   4;
    final static int        LINEHEIGHT  =   12;
    final static int        TITLEY      =   4;
    final static int        FIRSTLINEY  =   26;
    final int SCROLL = 4;
    
    public Font SetPrintEnv()
    {
	    scr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	    scr.setClip(0,0,canvasWidth, canvasHeight);
	    return scr.getFont();
    }
    
    public void SetPrint(int c, int xx, int yy, int align)
    {
            cprint = c;
            xprint = xx;
            yprint = yy;
            alignprint = align;            
    }
    
    
    public void SetPrint(int xx, int yy, int align)
    {
            cprint = 0xcdff45;
            xprint = xx;
            yprint = yy;
            alignprint = align; 
    }
    
    
    public void MultiPrint(String s[])
    {        
            int yy = yprint;
            for(int i = 0; i < s.length;i++)
      	    {
      	        Print(s[i], yy);  	        
      	        yy += LINEHEIGHT;      	    
      	    }
    }
    
    
    
    
    public void MultiPrintList(String s[])
    {        
            final int SCROLL = 4;
            int yy = yprint;
            for(int i = 0; i < s.length;i++)
      	    {
      	        yy = goodPrint(s[i], yy);  	        
      	        yy += LINEHEIGHT;
      	    
      	    }
      	    if (yy >= canvasHeight) startsc -= SCROLL;
    }
    


	public  int goodPrint(String s, int yy)
	{
		Font fon = SetPrintEnv();
		//Font fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
		byte[] btex = s.getBytes();
	    
		int inipos = 0;
		int cutpos = 0;
		while ( cutpos < btex.length )
		{
			int twidth = 0;
			int pos = inipos;	    
			while (twidth < canvasWidth)
			{
				if (pos >= btex.length) {cutpos = btex.length;break;}
				int ch = btex[pos];
				if  (ch == 0x20) cutpos = pos;	        
				//twidth += fon.stringWidth(new String(  (char)ch+"" ));
				twidth += fon.charWidth( (char)ch );
				pos++;
				}
			Print(s.substring(inipos, cutpos), yy);
			inipos = cutpos + 1;
			if  ( cutpos < btex.length ) 
			yy += LINEHEIGHT;    	    
		}
		return yy;
	}

	
	public int getTextHeight(String s)
	{
		int height = 0;
		Font fon = SetPrintEnv();
		//Font fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
		byte[] btex = s.getBytes();
	    
		int inipos = 0;
		int cutpos = 0;
		while ( cutpos < btex.length )
		{
			int twidth = 0;
			int pos = inipos;	    
			while (twidth < canvasWidth)
			{
				if (pos >= btex.length) {cutpos = btex.length;break;}
				int ch = btex[pos];
				if  (ch == 0x20) cutpos = pos;	        
				//twidth += fon.stringWidth(new String(  (char)ch+"" ));
				twidth += fon.charWidth( (char)ch );
				pos++;
			}
			inipos = cutpos + 1;
			if  ( cutpos < btex.length ) 
			height += LINEHEIGHT;    	    
		}
		return height;
	}

	
    
    public  void MultiPrint(String s)
    {
            Print(s, yprint);  	             
    }
    
   
    
	
	public  void Print(String s, int yy)
	{
		Font fon = SetPrintEnv();
		//Font Fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
		int xp = xprint;
		int yp = yy;
		    //int yp = yy + Fon.getAscent();
		    //switch(alignprint)
		    //{
			//case GHCENTER:
			    //xp = xp - (Fon.stringWidth(s)/2);
			    //break;
			//case GRIGHT:
			    //xp = xp - Fon.stringWidth(s);
			    //break;
		    //}	
		    if (yp <= -LINEHEIGHT || yp >= canvasHeight) return;

		    scr.setColor(0x000000);		
		    scr.drawString(s , xp-1, yp-1, alignprint | Graphics.TOP);                                	 
		    scr.setColor(cprint);		
		    //gr.drawString(s , xp, yp);//, alignprint | Graphics.TOP);
		    scr.drawString(s , xp, yp, alignprint | Graphics.TOP);				 				         
	}
	
	
	
	public  void Print(int c, String s, int xx, int yy, int align)
	{	
		Font fon = SetPrintEnv();
		//Font Fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
		int xp = xx;
		int yp = yy;
		//int yp = yy + Fon.getAscent();
		//switch(align)
		  //  {
			//case GHCENTER:
			 //   xp = xp - (Fon.stringWidth(s)/2);
			   // break;
			//case GRIGHT:
			  //  xp = xp - Fon.stringWidth(s);
//			    break;
	//	    }
		scr.setColor(c);		
		scr.drawString(s , xp, yp, align | Graphics.TOP);				 				         
	}*/
	
	/*public static void Print(String s, int xx, int yy, int align)
	{	 
	    Font Fon = Font.getFont(Font.FACE_SYSTEM | Font.STYLE_PLAIN);
	    int xp = xx;
	    int yp = yy + Fon.getAscent();
	    switch(align)
	    {
	        case GHCENTER:
	            xp = xp - (Fon.stringWidth(s)/2);
	            break;
	        case GRIGHT:
	            xp = xp - Fon.stringWidth(s);
	            break;
	    }
		gr.setColor(cprint);		
		gr.drawString(s , xp, yp);//, align | Graphics.TOP);				 				         
	}*/
	



	public void ImageSET(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY, int Flip)
	{
		if ((DestX       > canvasWidth)
		||	(DestX+SizeX < 0)
		||	(DestY       > canvasHeight)
		||	(DestY+SizeY < 0))
		{return;}
	
	
		scr.setClip(0, 0, canvasWidth, canvasHeight);
		scr.clipRect(DestX, DestY, SizeX, SizeY);
	
		switch (Flip)
		{
    		case 0:
    		//case 1:
    		scr.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
    		return;
    	
    		case 1:
    		dgr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, DestY-SourY, Graphics.LEFT|Graphics.TOP, 0x2000);		// Flip X
    		return;
    			
    		/*case 2:
    		dgr.drawImage(Sour, DestX-SourX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 0x4000);	// Flip Y
    		return;
    	
    		case 3:
    		dgr.drawImage(Sour, (DestX+SourX)-Sour.getWidth()+SizeX, (DestY+SourY)-Sour.getHeight()+SizeY, Graphics.LEFT|Graphics.TOP, 180);	// Flip XY
    		return;*/
		}
	}

// ----------------------------------------------------------

public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor, int Trozos)
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


public void PutSprite(Image Img,  int X, int Y,  int Frame, byte[] Coor)
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



static byte map[], back[];


public static boolean inList(int[] lista, int valor)
{
		boolean trobat = false;
		int i = 0;
		while (!trobat && i < lista.length)
		{
			trobat = (lista[i] == valor);
			i++;
		}
		return trobat;
}


public static byte getBack(int x, int y)
{
       //return (byte)((16+x%2)+(8*(y%2)));
       return   back[(x%8)+((y%8)*8)];        
}

public static byte getBack(int p)
{
    return back[p%back.length];//getBack(p%16, p/16);
}



int  stageWidth[] = {64, 32, 32, 48, 32, 42,  32,   32, 64, 96, 32,48,50,48,58,26};
int  stageHeight[] = {32, 32, 48, 48, 32, 42, 64,   96, 34, 26, 32,26,64,38,44,26};
//int startX[] = {3, 3,2, 2, 3, 3,  6,   8, 29, 3, 1,       20,3,14, 3};
//int startY[] = {12, 2,7, 4, 12, 4,  3,  42, 3, 11, 2, 11,1,15, 20};
int startX[] = {3, 3,2, 2, 3, 3,  6,   8, 29, 3, 2,       20,3,14, 3, 10};
int startY[] = {12, 2,7, 4, 12, 4,  3,  42, 3, 11, 3, 11,1,15, 20, 8};


int MAPW;
Image tileset, imgEnemies, imgNumbers;
byte[] corEnemies;

void ztextdraw(String s, int x, int y,int c,int a)
{
	scr.setClip(0,0,canvasWidth,canvasHeight);
	textDraw(s,x,y, c, a);
}

void zztextdraw(int color)
{
    if (canvasHeight > 65)
	{
		scr.setColor(0x000000);
		scr.setClip(0,0,canvasWidth,canvasHeight);
		scr.fillRect(0,0,canvasWidth, canvasHeight/6);
		scr.fillRect(0,canvasHeight- (canvasHeight/6),canvasWidth, canvasHeight/6);	
	}
	ztextdraw(ga.gameText[ga.TEXT_SCRIPT][ga.stage], 0,1, color, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
}




public void drawIntro()
{	
    ga.nfc++;
	int x = (6*unit.TILED)-8;
	int y = (10*unit.TILED)-8;
	offsetx = 8*unit.TILED;
	offsety = 9*unit.TILED;		
	sc.ScrollRUN_Centro_Max(scale(offsetx), scale(offsety));
	sc.ScrollIMP(scr);
	
	scr.setClip(0,0,canvasWidth,canvasHeight);
	switch (ga.stage)
	{
		case 0:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y),  0, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+(ga.nfc>>1)%2, corEnemies, NUMTROZOS);						
			//zztextdraw(ga.gameText[ga.TEXT_GAMEOVER][stage], 0,1, 0xeeFFee, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xeeFFee);
			break;
		case 1:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+3, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xFFb0b0, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xFFb0b0);
			break;
		case 2:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+3, corEnemies, NUMTROZOS);
			zztextdraw(0xFFb0b0);
			break;
		case 3:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);
			zztextdraw(0xc0ffff);
			break;	
		case 4:	
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y),  0, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(3*unit.TILED)), -sc.ScrollY+scale(y+4),  (2*7)+(ga.nfc)%2, corEnemies, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+(ga.nfc>>1)%2, corEnemies, NUMTROZOS);			
			zztextdraw(0xeeFFee);
			break;
		case 5:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+3, corEnemies, NUMTROZOS);
			zztextdraw(0xffffff);
			break;
		case 6:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xc0ffff, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xc0ffff);
			break;	
		case 7:	
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y),  0, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+(ga.nfc>>1)%2, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xeeFFee, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xeeFFee);
			break;
		case 8:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x-(1*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(1*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);			
			//ztextdraw("", 0,1, 0xc0ffff, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xc0ffff);
			break;	
		case 9:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+3, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xFFb0b0, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xFFb0b0);
			break;
		case 10:	
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y),  0, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+(ga.nfc>>1)%2, corEnemies, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(5*unit.TILED)), -sc.ScrollY+scale(y+4+12),  (10*7)+(ga.nfc)%4, corEnemies, NUMTROZOS);			
			//ztextdraw("", 0,1, 0xeeFFee, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xeeFFee);
			break;	
		case 11:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(-1*unit.TILED)), -sc.ScrollY+scale(y),  0, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(1*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(2*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(3*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+2, corEnemies, NUMTROZOS);	
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(5*unit.TILED))+2, -sc.ScrollY+scale(y+4),  (13*7)+4+(ga.nfc)%2, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xc0ffff, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xc0ffff);
			break;	
		case 12:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+3, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xFFffff, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xffffff);
			break;
		case 13:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4-(2*unit.TILED)),  (14*7)+2, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xc0ffff, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xc0ffff);
			break;
		case 14:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(2*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(1*unit.TILED)), -sc.ScrollY+scale(y+4),  (14*7)+3, corEnemies, NUMTROZOS);
			//ztextdraw("", 0,1, 0xFFb0b0, TEXT_TOP|TEXT_HCENTER | TEXT_OUTLINE);
			zztextdraw(0xFFb0b0);
			break;
		case 15:
			PutSprite(ga.prota.foo,  -sc.ScrollX+scale(x+(4*unit.TILED)), -sc.ScrollY+scale(y),  5*6, ga.prota.acor, NUMTROZOS);
			PutSprite(imgEnemies,  -sc.ScrollX+scale(x), -sc.ScrollY+scale(y+4),  (14*7)+3, corEnemies, NUMTROZOS);
			for(int i = 0;i<5;i++)				
				PutSprite(imgEnemies,  -sc.ScrollX+scale(x+(2*unit.TILED)), -sc.ScrollY+scale(y+8-(unit.TILED*i)),  (10*7)+(ga.nfc)%4, corEnemies, NUMTROZOS);
			zztextdraw(0xFFb0b0);
			break;
			
	}
	clickAdvice();
}	    



public void cargaIntro(int stage)
{
	ga.menuImg = null;
	ga.malo = null;
	map = null;System.gc();		
	ga.nfc = 0;	
	map = ga.loadFile("/intro"+stage+".map");
	for(int i = 0; i < map.length;i++)
	{
		if (map[i] == 0x10) map[i] = getBack(i%unit.TILED, i/unit.TILED);
		if (inList(LST_ICE, map[i])) 	map[i] =  (byte)Math.abs(((i*233)  - (i%5)) %3);
	}
	if (tileset == null) tileset = loadImage("/tileset.png");
	sc.ScrollSET(map, 16, 16, tileset, 8);
	if (imgEnemies == null)
	{
		imgEnemies = loadImage("/enemics.png");
		corEnemies = ga.loadFile("/enemics.cor");
	}	
}

public int scale(int a)
{
    return (a*3)/2;
}


public void cargaFase(int stage)
{	
	map = null;System.gc();
	ga.nfc = 0;
	ga.stage = stage;
	int SIZEX = stageWidth[stage] / 2;
	int SIZEY = stageHeight[stage] / 2;
	ga.neu = SIZEY-1;	
	MAPW = SIZEX;
	//map = ga.loadFile("mapa"+stage+".map", SIZEX*SIZEY);
	map = ga.loadFile("/mapa"+stage+".map");
	ga.nbatteryes = 0;    
	ga.nbreeds = 0;
	ga.nspits = 0;
	for(int i = 0; i < map.length;i++)
	{
		if (map[i] == 0x10) map[i] = getBack(i%SIZEX, i/SIZEX);
		if (map[i] == BATTERY) ga.nbatteryes++;
		if (inList(BREED, map[i])) 
		{
			ga.breed[ga.nbreeds][0] = i % SIZEX;
			ga.breed[ga.nbreeds][1] = i / SIZEX;
			ga.nbreeds++;
		}
		if (inList(SPIT, map[i])) 
		{
			ga.spit[ga.nspits][0] = i % SIZEX;
			ga.spit[ga.nspits][1] = i / SIZEX;
			if (map[i] == 37) ga.spit[ga.nspits][2] = 2;
			else ga.spit[ga.nspits][2] = -2;
			ga.nspits++;
		}
		if (inList(LST_ICE, map[i])) 	map[i] =  (byte)Math.abs(((i*233)  - (i%5)) %3);
	}
	if (tileset == null) tileset = loadImage("/tileset.png");
	sc.ScrollSET(map, SIZEX, SIZEY, tileset, 8);		
	
	ga.prota.faseacabada = 0;
	ga.prota.Activate(map, (startX[stage]*unit.TILED)-8, (startY[stage]*unit.TILED)-8);
	ga.prota.lastCell = 0;
	ga.prota.tag = 3;
	ga.prota.inmunity = 20;
	ga.prota.sidex = 1;
	
	offsetx = scale(ga.prota.x +(ga.prota.width>>1)) + (ga.prota.sidex*(canvasWidth>>3));
	offsety = scale(ga.prota.y + (ga.prota.height>>1)) - (canvasHeight>>3) ;
	//sc.ScrollRUN_Centro_Max(offsetx, offsety);
	
	
	if (imgEnemies == null)
	{
		imgEnemies = loadImage("/enemics.png");
		//corEnemies = ga.loadFile("enemics.cor", 588);
		corEnemies = ga.loadFile("/enemics.cor");
	}
	ga.malo = null;
	System.gc();
	ga.malo = new unit[ga.enemies[stage].length]; 	     	
 	for(int i = 0;i < ga.malo.length;i++)
 	{	   	    
     		switch(ga.enemies[stage][i][0])
     		{
	     		case 2: ga.malo[i] = new malo2(this,  imgEnemies, 20, 20, 7,0,2,corEnemies);break;
	     		case 3: ga.malo[i] = new malo3(this,  imgEnemies, 20, 20, 7,0,4,corEnemies);break;
			    case 4: ga.malo[i] = new malo4(this,  imgEnemies, 20, 20, 7,0,6,corEnemies);break;
			    case 5: ga.malo[i] = new malo5(this,  imgEnemies, 20, 20, 7,0,8,corEnemies);break;
			    case 6: ga.malo[i] = new malo6(this,  imgEnemies, 20, 20, 7,0,10,corEnemies);break;
			    case 7: ga.malo[i] = new malo7(this,  imgEnemies, 20, 20, 7,0,12,corEnemies);break;			
	     		default: ga.malo[i] = new malo1(this,  imgEnemies, 20, 20, 7,0,0, corEnemies);break;
	  	    }
		    switch(ga.enemies[stage][i][0])
     		{
			    case 5:
			    case 2:break;
			    case 6:ga.malo[i].Activate(map, (ga.enemies[stage][i][1]*unit.TILED) -3 , (ga.enemies[stage][i][2]*unit.TILED)  -3);break; 
			    default: ga.malo[i].Activate(map, (ga.enemies[stage][i][1]*unit.TILED) - 2 , (ga.enemies[stage][i][2]*unit.TILED) - 4);break;
		    }		
		    if (ga.enemies[stage][i][0] == 7) ga.malo[i].tag = 2;
	}
	iniTips(stage);
	if (stage == 15) ga.prota.nshots = 0;	  				  	  		 	  	
}

public class tip
{
	int x, y;
	boolean done;
	String text;
}
tip tips[];

void iniTips(int stage)
{
	showtip = -1;lasttip = -1;
	switch(stage)
	{
		case 0:
			tips = new tip[9];
			for (int i = 0; i < tips.length; i++) tips[i] = new tip();
			tips[0].x 		= 5;
			tips[0].y 		= 12;
			tips[0].text 	= ga.gameText[ga.TEXT_TIPS][0];
		
			tips[1].x 		= 12;
			tips[1].y 		= 12;
			tips[1].text 	= ga.gameText[ga.TEXT_TIPS][1];
			
			tips[2].x 		= 16;
			tips[2].y 		= 12;
			tips[2].text 	= ga.gameText[ga.TEXT_TIPS][2];
			
			tips[3].x 		= 27;
			tips[3].y 		= 10;
			tips[3].text 	= ga.gameText[ga.TEXT_TIPS][3];
			
			tips[4].x 		= 20;
			tips[4].y 		= 4;
			tips[4].text 	= ga.gameText[ga.TEXT_TIPS][4];
			
			tips[5].x 		= 10;
			tips[5].y 		= 12;
			tips[5].text 	= ga.gameText[ga.TEXT_TIPS][5];
			
			tips[6].x 		= 2;
			tips[6].y 		= 8;
			tips[6].text 	= ga.gameText[ga.TEXT_TIPS][6];
			
			tips[7].x 		= 3;
			tips[7].y 		= 12;
			tips[7].text 	= ga.gameText[ga.TEXT_TIPS][7];
			
			tips[8].x 		= 3;
			tips[8].y 		= 4;
			tips[8].text 	= ga.gameText[ga.TEXT_TIPS][8];
			break;
		case 1:
			tips = new tip[4];
			for (int i = 0; i < tips.length; i++) tips[i] = new tip();
			tips[0].x 		= 7;
			tips[0].y 		= 5;
			tips[0].text 	= ga.gameText[ga.TEXT_TIPS][9];
			tips[1].x 		= 11;
			tips[1].y 		= 14;
			tips[1].text 	= ga.gameText[ga.TEXT_TIPS][10];;
			tips[2].x 		= 9;
			tips[2].y 		= 9;
			tips[2].text 	= ga.gameText[ga.TEXT_TIPS][11];;
			tips[3].x 		= 6;
			tips[3].y 		= 11;
			tips[3].text 	= ga.gameText[ga.TEXT_TIPS][12];;																			
			break;	
		case 2:
			tips = new tip[1];
			for (int i = 0; i < tips.length; i++) tips[i] = new tip();
		
			tips[0].x 		= 2;
			tips[0].y 		= 7;
			tips[0].text 	= ga.gameText[ga.TEXT_TIPS][13];;
			
			/*tips[1].x 		= 3;
			tips[1].y 		= 10;
			tips[1].text 	= "Los reactores necesitan c�lulas energ�ticas para activarse";
			
			tips[2].x 		= 2;
			tips[2].y 		= 1;
			tips[2].text 	= "Recogiendo estas c�lulas el reactor podr� activarse";*/
			break;
		case 6:
			tips = new tip[1];
			for (int i = 0; i < tips.length; i++) tips[i] = new tip();
			tips[0].x 		= 10;
			tips[0].y 		= 7;
			tips[0].text 	= ga.gameText[ga.TEXT_TIPS][14];
			break;
		case 15:
			tips = new tip[1];
			for (int i = 0; i < tips.length; i++) tips[i] = new tip();
			tips[0].x 		= 10;
			tips[0].y 		= 8;
			tips[0].text 	= ga.gameText[ga.TEXT_TIPS][15];
			break;			
		default: tips = null;break;	
	}
}	


unit uTip = new unit(this,null, unit.TILED, unit.TILED, 0,0,0, null);

int offsetx, offsety;


public void drawEndStage()
{
	if (ga.prota.faseacabada > 0)
	    {
	         int step = ga.prota.faseacabada;
		     int tope = 10;
		     if (step > 10) tope =10 - (step-10);
		     int x =  -sc.ScrollX + scale(ga.prota.x) + (ga.prota.width>>1);			    
		     scr.setClip(0,0,canvasWidth, canvasHeight);
		     for(int i = 0; i < tope;i++)
		     {			  
			    scr.setColor(i*25,255,i*25);
			    scr.fillRect(x-(step*(10-i)), 0, 2*(step*(10-i)), canvasHeight);
		     }		  
	    }
	
}


public void clickAdvice()
{
	showImage(imgNumbers, 1, 5, 7, 4, canvasWidth-8 ,canvasHeight-5);	
}

int showtip;
int lasttip;

public void drawTips()
{
	/*showtip = -1;
	if (tips == null) return;
	for(int i = 0; i < tips.length;i++)
	{
	            if (tips[i].done) continue;
		    uTip.x = tips[i].x*16;
		    uTip.y = tips[i].y*16;
		    if (ga.prota.isCollidingWith(uTip))
		    {*/
			      if (showtip != -1 && lasttip != showtip)
			      {
			        
				      //SetPrint(0xffffff, (canvasWidth>>1)+1,TITLEY, GHCENTER);		    
				      //goodPrint(tips[showtip].text, canvasHeight - getTextHeight(tips[showtip].text) - 3 - LINEHEIGHT);
				      scr.setClip(0,0,canvasWidth,canvasHeight);
				      textDraw(tips[showtip].text, 0,0, 0xFFFFFF, TEXT_BOTTON|TEXT_HCENTER | TEXT_OUTLINE);
				      clickAdvice();
				      lasttip = showtip;
			      }			      			    
		    /*}
	 }*/
}

final static int NUMTROZOS = 2;



};