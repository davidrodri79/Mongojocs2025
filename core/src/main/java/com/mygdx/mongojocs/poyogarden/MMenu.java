package com.mygdx.mongojocs.poyogarden;


import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;

public class MMenu
{

	//Graphics gr;
   int cvar;
   static int opmenu = 0;
   final static int FRASECR = 15;
   final static int BEGFRASEY = 11;
   //final static int BEGFRASEX = 30;
   int tim;
   int come = -32;
   int selfase;
   int preopmenu = -1;
   Graphics gr;
   int w,h;
   

   
                        
   
   
   
	public  MMenu(int w, int h)//)
	{		
	    this.w = w;
	    this.h = h;	    
	}


    public void quad()
    {
        gr.setClip(0,0,w,h);
	    gr.setColor(240,160,48);
        gr.fillRoundRect(0,3, w, h-6, 32, 32); 
        gr.setColor(240,112,0);
        gr.drawRoundRect(0,3, w, h-6, 32,32); 
        gr.setColor(128,64,0);
		
    }
    
	public int show(int currentkey, boolean apretando)
	{	    
	    gr.translate(24,30);
	    Game.ch = h;
	    Game.cw = w;
		int estado = Game.estado;
		int prestado =  Game.prestado;//Game.getPrestado();
		gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_SMALL));
		tim++;
		switch( estado)
   	    {
           case 3:     
                    quad();
                    Creditos(gr, w,h);                    
                    if (apretando && (currentkey== Canvas.FIRE || Game.rkey == -6 || Game.rkey == -7)) { estado = 4;cvar=h/2;Game.currentkey=0;come = -32;}
                    break;   
           case 4:                
                  cvar = h/2;       
                  if (PoyoP.sfx) lll[0][3]= ID_Soundison;
                  else lll[0][3]= ID_Soundisoff;
                  //if (PoyoP.rocco) lll[0][4]= ID_Vibraison;
                  //else lll[0][4]= ID_Vibraisoff;
                  if (PoyoP.help) lll[0][4]= ID_Helpmodeison;
                  else lll[0][4]= ID_Helpmodeisoff;
         
                  lll[1][3] = lll[0][3];
                  lll[1][4] = lll[0][4];
                  //lll[1][5] = lll[0][5];         
                  
                  if (opmenu != preopmenu)
                  {
                    quad();
                    for (int i = 0;i<lll[prestado].length;i++)
                    {
                  	    if (opmenu == i) gr.setColor(255,238,62);
                        else gr.setColor(128,64,0);
                        gr.drawString(smenu[lll[prestado][i]], w/2, 10+BEGFRASEY+i*FRASECR, Graphics.HCENTER | Graphics.TOP);	               	              	                        
                    }
                  }  
                  preopmenu = opmenu;
                  //gr.drawRect(9, 4, w-19, h-9);
                  if (apretando)
                  {
                      Game.cheat();
                  	  if (currentkey==Canvas.UP) if (opmenu > 0) opmenu--; else opmenu = lll[prestado].length-1;//Game.currentkey=0;}
	                  if (currentkey==Canvas.DOWN) 
	                  if (opmenu < lll[prestado].length-1) {opmenu++;} else opmenu = 0;//Game.currentkey=0;}
	                  if (currentkey == Canvas.KEY_POUND && prestado==1){ estado=3;}//currentkey = 0;}      	
	                  if (currentkey==Canvas.FIRE || Game.rkey == -7) 
	                  {               
	                    int pres = estado;      
	                  	if (lll[prestado][opmenu] == ID_NewGame) 
	                     {  
	                        //prestado = 2;
	                        //estado = 2;
	                        estado = 44; 
	                        selfase = PoyoP.lastfase;
	                        /*gr.setColor(255,255,255);gr.fillRect(11, 49, 71, 9);   
	                        gr.setColor(0,0,0);  
	                        gr.drawString("L O A D I N G" , 22, 49, Graphics.LEFT | Graphics.TOP);	    	*/
	                     }                          
	                                                     
	                     if (lll[prestado][opmenu] == ID_Resume) {estado = prestado;} 
	                     if (lll[prestado][opmenu] == ID_MainMenu) {estado = 1;Game.prestado=1;} 
	                     if (lll[prestado][opmenu] == ID_Exit) {estado=6;  }
	                     if (lll[prestado][opmenu] == ID_Help) { estado=5;}                                                          
	                     if (lll[prestado][opmenu] == ID_About) { estado=3;}                                                            
	                     if (lll[prestado][opmenu] == ID_Soundison) { PoyoP.sfx=false;Game.StopSound();preopmenu=-1;}                                                           
	                     if (lll[prestado][opmenu] == ID_Soundisoff) { PoyoP.sfx=true;preopmenu=-1;}                                                           
	                     if (lll[prestado][opmenu] == ID_Vibraison) { PoyoP.rocco=false;preopmenu=-1;}                                                           
	                     if (lll[prestado][opmenu] == ID_Vibraisoff) { PoyoP.rocco=true;preopmenu=-1; }                                                                               
	                     if (lll[prestado][opmenu] == ID_Helpmodeison) { PoyoP.help=false;preopmenu=-1;}                                                           
	                     if (lll[prestado][opmenu] == ID_Helpmodeisoff) { PoyoP.help=true;preopmenu=-1; }                                                                               
	                     if (estado != pres) preopmenu = -1;
	                  }
	                  //if (currentkey==Canvas.KEY_STAR) {estado = prestado;}
	                  Game.currentkey=0;Game.apretando=false;Game.rkey=0;	                  	                  
                  }
                  break;
               case 44:
                    quad();
                     Game.PrintS(128,64,0, lll2[0], w/2, h/2-(2*FRASECR), Graphics.HCENTER);	               	              	                        
                     Game.PrintS(255,238,62, lll2[1]+" < "+(selfase+1)+ " >", w/2, h/2, Graphics.HCENTER);	               	              	            
                     Game.PrintS(128,64,0, lll2[2], w/2, h/2+(2*FRASECR), Graphics.HCENTER);	               	              	                                   
                     Game.PrintS(128,64,0, lll2[3], w/2, h/2+(3*FRASECR), Graphics.HCENTER);	               	              	                                     
                     if (apretando)
                     {
                  	    if (currentkey==Canvas.LEFT) if (selfase > 0) {selfase--;}//Game.currentkey=0;}
	                    if (currentkey==Canvas.RIGHT) if (selfase < PoyoP.lastfase) {selfase++;}
	                    if (currentkey==Canvas.FIRE || Game.rkey == -7)  {estado = 2;Game.fase = selfase;}
	                    Game.currentkey=0;Game.apretando=false;	      
                     }
                    break;   
               case 70:
                    quad();
                    Game.Print(128,64,0, fase[0]+" "+(Game.fase+1), w/2, h/2-(3*FRASECR), Graphics.HCENTER);
                    Game.Print(128,64,0, fase[1]+" "+Game.tiempo , w/2, (h/2)-FRASECR, Graphics.HCENTER);
                    Game.Print(128,64,0, fase[2]+" "+Game.puntos, w/2, h/2+(FRASECR), Graphics.HCENTER);                    
                    //gr.drawString("Fase :"+(Game.fase+1), w/2, 60, Graphics.HCENTER | Graphics.TOP);		   
		            //gr.drawString("Tiempo :"+nfc, w/2, 80, Graphics.HCENTER | Graphics.TOP);		   
		            if (apretando)
		            {	
			            Game.fase++;
			            if (Game.fase < 15) estado = 71;
			            else estado = 72;
		            }
                    break;
               case 5:
                    quad();
                    gr.setClip(12, 12, w-12, h-12);                  
                    for (int i = 0 ;i < controls.length;i++)
                        gr.drawString(controls[i],16, (-(FRASECR*controls.length/2)) + (h/2)+(FRASECR*i), Graphics.LEFT | Graphics.TOP);	               	              	                                  
                    if (apretando && currentkey != 0) 
                    { 
                  	    //estado = 3;
                  	    //if (prestado == 0) 
                  	    estado = 4;
                  	    Game.currentkey=0;Game.apretando=false;
                    }  
                    break;  
               case -1:               
                  estado = 4;
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
                  break;
   			}    
   		 gr.translate(-24,-30);	
         return  estado;     			
	}
	
	
	public void Creditos(Graphics gr, int w,int h)
	{
	    if (w > 128) gr.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
	    if (Game.estado != 3) {tim++;gr.setColor(255,255,255);}
	        
	    if (cvar == h/2) Game.Sonido(8,10,false);	                
      	for (int i = 0 ;i < credits.length;i++)
        {                        
            gr.setClip(12, 12, w-12, h-24);                  	
        
        	if ((cvar+FRASECR*i>12-FRASECR) && (cvar+FRASECR*i < h-(FRASECR))) 
            if (credits[i] != "")                         
                gr.drawString(credits[i],w/2, cvar+FRASECR*i, Graphics.HCENTER | Graphics.TOP);
                //Game.PutPoyo(w/2, (cvar+FRASECR*i)+8, tim);                        
        }
        int kk = 0;    
        for (int i = 12; i < w-28;i += 16) 
        {
            //Game.PutSprite(Game.all, i, h-30, (kk*4)+Math.abs((tim)%4)+ (Math.abs(tim%128)/32)*20, Game.acor,3);    
            kk++;
            if (kk == 5) kk = 21;
            if (kk == 22) kk = 0;
        }
		if (cvar > (-FRASECR*(credits.length-1))+(h/2)) cvar-=1;   
		else
		{
		    if (Game.estado == 3)
		    {
                gr.setColor(255,170,49);
                //gr.setColor(239,154,33);
		        gr.setClip(12, 12, w-12, h-24);                  	
                gr.fillRect(come-150, (h/2)-FRASECR,160,FRASECR);
            }
            come+=2;           			        
		    if (come < w-32) 
		    {   
		        Game.PutSprite(Game.all, come, (h/2)-FRASECR-8, (33*4)+((tim/2)%3), Game.acor,3);                   
		    }
		    else 
		    {           			        
		        come+=4;
		        Game.PutSprite(Game.all, Math.max(w-32, come-w-8), (h/2)-FRASECR-8, (23*4)+((tim)%2), Game.acor,3);                   
		        Game.PutSprite(Game.all, come-w-24, (h/2)-FRASECR-8, (34*4)+((tim)%4), Game.acor,3);                   
		    }
            
		}		
    }
    
    
    int lll[][] = {{ID_Resume, ID_Help,ID_About,-1,-1,ID_MainMenu},
				   {ID_NewGame, ID_Help, ID_About, -1, -1, ID_Exit}};
	
    
	/*
	EXIT 	= 0;	
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
	  */
	
   final static int ID_NewGame      = 0;
   final static int ID_Resume       = 1;
   final static int ID_MainMenu     = 2;
   final static int ID_Exit         = 3;
   final static int ID_Help         = 4;
   final static int ID_About        = 5;
   final static int ID_Soundison    = 6;
   final static int ID_Soundisoff   = 7;
   final static int ID_Vibraison    = 8;
   final static int ID_Vibraisoff   = 9;
   final static int ID_Helpmodeison     = 10;
   final static int ID_Helpmodeisoff    = 11;
	
	String smenu[] = {"Jugar", "Volver", "Ir a Menú", "Salir", "Controles", "Acerca de","Sonido on", "Sonido off", "Vibración on", "Vibración off", "Ayuda on", "Ayuda off"};
    	
	String sitems[] = {"Salida",
                        "Tiempo",
                        "Patines",
                        "Botas aladas",
                        "Huevos",
                        "Congelar",
                        "Vida",
                        "Martillo",
                        "Regalo",
                        "Espejo",
                        "Naranja",
                        "Limón",
                        "Tomate",
                        "Sandía",
                        "Zanahoria",
                        "Inmunidad"                        
                        };
  
  
    String lll2[] = {"Iniciar en","nivel","Usa izquierda","y derecha"};
  
    String onhelp[] = {"Salida",
                        "Tiempo: te da tiempo extra",
                        "Patines: el aceite y el agua no te afectan",
                        "Botas aladas: caminarás más rápido",
                        "Huevos: disparo en 4 direcciones",
                        "Congelar: ¡tus disparos congelan!",
                        "Vida: ¡vida extra!",
                        "Martillo: Tus disparos destruyen rocas",
                        "",
                        "Espejo: ¡el control se invierte!",
                        "Naranja: ¡puntos extra!",
                        "Limón: ¡puntos extra!",
                        "Tomate: ¡puntos extra!",
                        "Sandía: ¡puntos extra!",
                        "Zanahoria: ¡puntos extra!",
                        "Inmunidad: te da inmunidad temporal",
                        "Dinamita: ¡destruye todo lo que está cerca!",
                        "Bomba: elimina a todos los enemigos",
                        "Agujero: con disparos vas a la salida del túnel",
                        "Hielo: congela a los enemigos"
                        };

	/*String lll[][] = {{"Resume", "Help","About","","","","Main Menu"},
							{"New Game", "Help", "About","","","","Exit"},
                     {"Select start","level","use left and right"}};*/
   String credits[] =  { 
   	 					"",   	 						 
                         "Poyo's Garden",
                         "",
                         "",
                         "Gráficos de",
                         "Elias Lozano",
                         "Jordi Palomé",
                         "",
                         "",  
                         "Programado por",
                         "Carlos Peris",                         
                         "",
                         "",
                         "Sonido por",
                         "Jordi Gutierrez",                         
                         "",
                         "",                         
                         "Producido por", 
                         "Microjocs Mobile",
                         "",
                         "",                  
                         "",
                         "",                                
                         "microjocs.com",  
                         "2003"};    
   
   String controls[] = {"2 = Arriba",
                        "8 = Abajo",
                        "4 = Izquierda",
                        "6 = Derecha",                        
                        "5 = Disparar",
                        "* = Menú"};
                        
    String topscores="puntuaciones";
	static String score[] = {"Enhorabuena","Tu puntuación es una de las 5 mejores. Escribe tus iniciales"};
	
	String fin[] = {"Salvaste el"," bosque!","Ahora los poyos","pueden vivir", "sanos y libres."};
	String fase[] = {"Nivel","Tiempo","Puntos"};
}