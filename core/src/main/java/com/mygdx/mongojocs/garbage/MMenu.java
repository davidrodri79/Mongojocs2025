package com.mygdx.mongojocs.garbage;


import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.Graphics;

public class MMenu
{
     final static int        GLEFT       =   1;
    final static int        GRIGHT      =   -1;
    final static int        GHCENTER     =   0;

	//Graphics gr;
    int cvar = 150;
    static int opmenu = 0;
    //int intro;
    int tim;
    int nestado;
   
    public final static int SBEGIN = 24;
    //COMANDOS
    public final static int ID_NEWGAME = 0;
	public final static int ID_HELP = 1;
	public final static int ID_MENU = 2;
	public final static int ID_QUIT = 3;
	public final static int ID_RESUME= 4;	
	public final static int ID_SOUNDON = 5;
	public final static int ID_SOUNDOFF = 6;
	public final static int ID_VIBRAON = 7;
	public final static int ID_VIBRAOFF = 8;
	public final static int ID_CREDITS = 9;
	
	
	final static int MENULINE = 73;
	
	
	/*public void Flechas(int y)
	{
         Game.blit(0,0,8,11,Game.w-(Game.w/9)-9+((tim/4)%6),y,Game.wm[9]);//FLECHA
         Game.blit(0,0,8,11,(Game.w/9)-((tim/4)%6),y,Game.wm[8]);//FLECHA    
         
    }*/             
	
	
	public  MMenu()
	{						
    }


    
    /*publci String[] Desglosa(String in)
    {
        String[] out;
    }*/

	
	public int show(Graphics gr, int currentkey, boolean apretando, int w, int h)
	{
	    int pestado = Game.estado;
		int estado = Game.estado;
		int prestado =  Game.prestado;
		tim++;    
                 //System.out.println(""+estado);
        if (estado!= -21)
        {
            gr.setColor(0x000000);
            gr.fillRect(0,0,w,h);
        }
        else
        {
            gr.setColor(0xffffff);
            gr.fillRect(0,0,w,h);
            
        }
        //gr.drawImage(Game.img,(w/2)-(Game.img.getWidth()/2), (h/2)-(Game.img.getHeight()/2));
                  
		switch(estado)
   	    {
   	    
   	       case 3:
                    Game.SetPrint(w/2, Game.FIRSTLINEY+Game.startsc,GHCENTER);
                    Game.MultiPrintList(Game.txt[Game.TEXT_ABOUT_SCROLL]);                   
                    //if ((Game.FIRSTLINEY + Game.startsc + (Lang.credits.length*Game.LINEHEIGHT) > 0)) //Game.startsc = h;
                    //Game.startsc -= 2;  	
                    if (Game.click()) {estado=4;Game.currentkey=0;Game.rkey = 0;}           			
           			break;
           			           		                                    
           case 4:                                           
           
                  if (Moto.sfx) lll[0][2]= ID_SOUNDON;
                  else lll[0][2]= ID_SOUNDOFF;
                  if (Moto.rocco) lll[0][3]= ID_VIBRAON;
                  else lll[0][3]= ID_VIBRAOFF;
                  lll[1][3] = lll[0][3];
                  lll[1][2] = lll[0][2];
               
                  gr.setColor(0x000000);
                  gr.fillRect(0,0,w,h);
                  if (Game.img != null) Game.blit(0,0,Game.img.getWidth(), Game.img.getHeight(), (w/2)-(Game.img.getWidth()/2), (h/2)-(Game.img.getHeight()/2), Game.img);
        
                  Game.startsc = 0;   
                  int s1=0, s2 = 0;
                  switch(lll[prestado][opmenu])
                  {
                        case ID_NEWGAME: s1 = Game.TEXT_PLAY;break;
                        case ID_RESUME: s1 = Game.TEXT_CONTINUE;break;                        
                        case ID_HELP: s1 = Game.TEXT_HELP;break;
                        case ID_MENU: s1 = Game.TEXT_RESTART;break;
                        case ID_QUIT: s1 = Game.TEXT_EXIT;break;
                        case ID_CREDITS:  s1 = Game.TEXT_ABOUT;break;
                        case ID_SOUNDON: s1 = Game.TEXT_SOUND;s2 = 1;break;
	                    case ID_SOUNDOFF:s1 = Game.TEXT_SOUND;break;
	                    case ID_VIBRAON: s1 = Game.TEXT_VIBRA;s2 = 1;break;
	                    case ID_VIBRAOFF:s1 = Game.TEXT_VIBRA;break;	                        
                  }
                                 
                  String s = Game.txt[s1][s2];//Lang.men[lll[prestado][opmenu]];          	

                  //Flechas(h/2);
                  Game.SetPrint(w/2, MENULINE+((h-128)/2),GHCENTER);        
                  //gr.setColor(0);gr.fillRect(0,h/2, w, 14);
                  Game.MultiPrint(s);
               

                  if (apretando)
                  {
                  	    if (currentkey== Display.KEY_4 || currentkey==Display.KEY_2) if (opmenu > 0) {opmenu--;}else opmenu = lll[prestado].length-1;//Game.currentkey=0;}
	                    if (currentkey==Display.KEY_6  || currentkey==Display.KEY_8)
	                  	if (opmenu < lll[prestado].length-1) {opmenu++;}else opmenu = 0;//Game.currentkey=0;}
	                    if (currentkey == Display.KEY_POUND && prestado==1){ estado=3;}//currentkey = 0;}      	
	                    if (Game.click()) 
	                    {                         
	                        switch(lll[prestado][opmenu])
	                        {
	                            case ID_NEWGAME:
	                                estado = -2;
	                                break;
	                            case ID_RESUME:
        	                        estado = prestado;
        	                        Game.joc.SoundRES();
        	                        Game.joc.Refresh();break;        	                        
        	                    case ID_MENU:
        	                        estado = 4;Game.prestado=1;
        	                        Game.joc.Sonido(0, 1, false);  
        	                        break;
        	                    case ID_QUIT:
        	                        estado=6;break;
        	                    case ID_HELP: 
        	                        estado=5;break;
        	                    case ID_CREDITS: 
        	                         estado=3;break;        	                   
        	                    case ID_SOUNDON:  Game.joc.SoundRES();Moto.sfx=false;break;
        	                    case ID_SOUNDOFF: Moto.sfx=true;Game.joc.Sonido(0, 1, false);break;
        	                    case ID_VIBRAON:  Moto.rocco=false;break;
        	                    case ID_VIBRAOFF: Moto.rocco=true;break;
	                     
	                        }    	                  	 
    	                }
	                    Game.currentkey=0;Game.apretando=false;Game.rkey = 0;
                  }
                  break;
                  
               case 5:
                    Game.SetPrint(w/2, Game.FIRSTLINEY+Game.startsc,GHCENTER);
                    Game.MultiPrintList(Game.txt[Game.TEXT_HELP_SCROLL]);
                    //if (Game.FIRSTLINEY + Game.startsc + (Lang.controls.length*Game.LINEHEIGHT) > h) Game.startsc -= 2;  	
           			if (Game.click()) {estado=4;Game.currentkey=0;Game.rkey = 0;}           			
           			break;
           	   case	7:
           	        if (Game.img != null) Game.blit(0,0,Game.img.getWidth(), Game.img.getHeight(), (w/2)-(Game.img.getWidth()/2), (h/2)-(Game.img.getHeight()/2), Game.img);        
           	        //gr.setColor(0);gr.fillRect(0,h/2, w, 14);
           	        Game.SetPrint(w/2, MENULINE+((h-128)/2),GHCENTER);                         
           	        Game.MultiPrint(Game.txt[Game.TEXT_FINFASE][0]+" "+(Game.stage+1));               
           	   	    if (Game.click()) 
           	   	    {
           	   	        estado=0;Game.currentkey=0;Game.rkey = 0;
           	   	        if (Game.stage < 9) Game.joc.CargaFase(++Game.stage);
           	   	        else  {estado = 9;Game.SetEstado(9,1);}
           	   	    }     
           	   	    break;      			
           	   case	8:
           	        if (Game.img != null) Game.blit(0,0,Game.img.getWidth(), Game.img.getHeight(), (w/2)-(Game.img.getWidth()/2), (h/2)-(Game.img.getHeight()/2), Game.img);        
           	        //gr.setColor(0);gr.fillRect(0,h/2, w, 14);
           	        Game.SetPrint(w/2, MENULINE+((h-128)/2),GHCENTER);        
           	        Game.MultiPrint(Game.txt[Game.TEXT_GAMEOVER][0]);               
           	   	    if (Game.click()) {estado=4;Game.currentkey=0;Game.rkey = 0; 
           	   	    Game.joc.Sonido(0, 1, false);          	   	    
           	   	    }     
           	   	    break;      		    
           	   case 9:
           	        if (Game.img != null) Game.blit(0,0,Game.img.getWidth(), Game.img.getHeight(), (w/2)-(Game.img.getWidth()/2), (h/2)-(Game.img.getHeight()/2), Game.img);        
           	        //gr.setColor(0);gr.fillRect(0,h/2, w, 14);
           	        Game.SetPrint(w/2, MENULINE+((h-128)/2),GHCENTER);                  
           	        Game.MultiPrint(Game.txt[Game.TEXT_ENDGAME][0]);               
           	   	    if (Game.click()) {estado=3;Game.currentkey=0;Game.rkey = 0;           	   	    
           	   	    }                	   	    
           	        break;	    
           			
            }             
         if (estado != pestado)	wmop = 0;
         return  estado;     			
	}
	
	int wmop;
 	
 	 public void Wop(int max)
 	{
 	    if (!Game.apretando) return;
 	    if (Game.currentkey==Display.KEY_2) wmop--;
	    if (Game.currentkey==Display.KEY_8) wmop++;
 	    if (Game.currentkey==Display.KEY_4) wmop--;
	    if (Game.currentkey==Display.KEY_6) wmop++;
	    if (wmop < 0) wmop = max;
	    if (wmop > max) wmop = 0;	                    
	    Game.currentkey = 0;
 	}
 	
   final static int lll[][] = {{ID_RESUME , ID_HELP, 0, 0, ID_MENU, ID_QUIT },
					   {ID_NEWGAME, ID_HELP, 0,0, ID_CREDITS, ID_QUIT }};
  
}