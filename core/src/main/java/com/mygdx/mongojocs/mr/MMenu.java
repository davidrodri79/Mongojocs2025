package com.mygdx.mongojocs.mr;

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class MMenu
{

	Graphics gr;
    int cvar = 150;
    static int opmenu = 0;
    int intro = -31*20;
    boolean changing = false;
    boolean ending = false;
    int motox = 9;
    int quesitox = 6;
    int nestado;
   
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
	public final static int ID_CARRERARAPIDA = 9;
	public final static int ID_CAMPEONATO = 10;
	public final static int ID_VERSUS = 11;
	public final static int ID_OPCIONES = 12;
	public final static int ID_CREDITS = 13;
	public final static int ID_VIEWRECORDS = 14;
	public final static int ID_VSTIME = 15;
	//public final static int ID_CONFCIRCUIT = 9;
	//public final static int ID_CONFMOTO = 10;
	
	public final static int MAIN = 0;
	public final static int INGAME = 1;
	public final static int CIRCUITED = 2;
	public final static int MOTOED = 3;
	
	/*Form circuitForm;
   TextField vueltaField, stepField;      	
	Graphics cga1;
	ChoiceGroup cg;
	int col[][] = {{200,200,200},{150,150,200},{150,200,150}};
	Command doneCom = new Command("Hecho", Command.SCREEN, 1);*/
	
	public  MMenu()
	{				
		/*circuitForm = new Form(Lang.circuited[0]);
		vueltaField = new TextField(Lang.circuited[1], "", 3, TextField.NUMERIC);
		stepField = new TextField(Lang.circuited[2], "", 2, TextField.NUMERIC);
		circuitForm.append(vueltaField);
		circuitForm.append(stepField);
		cg = new ChoiceGroup("Color carretera", ChoiceGroup.EXCLUSIVE); 		
		cg.append("Gris", null);
		cg.append("Azul", null);
		cg.append("Verde", null);
		circuitForm.append(cg);*/
		/*Image p = Image.createImage(16, 16);
		cga1 = p.getGraphics();
		cga1.setColor(255,0,0);
		cga1.fillRect(0,0,16,16);
		Image d = Image.createImage(p);
		ImageItem i = new ImageItem ("Color", d, ImageItem.LAYOUT_RIGHT , ""); 
		circuitForm.append(i);
		i = new ImageItem ("Color", d, ImageItem.LAYOUT_RIGHT , ""); 
		circuitForm.append(i);
		i = new ImageItem ("Color", d, ImageItem.LAYOUT_RIGHT , ""); 
		circuitForm.append(i);*/
		//circuitForm.addCommand(doneCom);
		//circuitForm.setCommandListener(this);
	}
	int tim;
//String demo[]= {"Movistar Motorage","Demo version 0.4","Proyecto Pecan"};

    public void Wop(int max)
 	{
 	    if (!Game.apretando) return;
 	    if (Game.currentkey==Canvas.UP) wmop--;
	    if (Game.currentkey==Canvas.DOWN) wmop++; 	                   
 	    if (Game.currentkey==Canvas.LEFT) wmop--;
	    if (Game.currentkey==Canvas.RIGHT) wmop++; 
	    if (wmop < 0) wmop = max;
	    if (wmop > max) wmop = 0;	                    
	    Game.currentkey = 0;
 	}
 	
 	Image pi;
    int start = 14;
                    

	public int show(Graphics gr, int currentkey, boolean apretando, int w, int h)
	{
		int estado = Game.estado;
		int prestado =  Game.prestado;
		//gr.setClip(0,0,w,h);
		//gr.setColor(0,0,0);gr.fillRect(0, 0, w, h);
		Game.blit(0,0,176,208,0,0,Game.imenu);              
        tim++;            
		switch( estado)
   	    {
   	        
   	         case 33:
   	                Game.Print(205,255,69,Lang.cilindrada,w/2, 24,Graphics.HCENTER);
   	                for (int i = 0; i < 3;i++)
   	                {
   	                    if (i != wmop) 
   	                        Game.Print(128,200,255,Lang.difficulty[i],w/2,76+i*32,Graphics.HCENTER);
   	                    else Game.Print(105,255,69,Lang.difficulty[i],w/2,76+i*32,Graphics.HCENTER);       	                    
   	                }
   	                Game.Click();
   	                Game.Print(205,255,69,"men�",8, h-16,Graphics.LEFT);
   	                if (apretando && Game.rkey == -6) {estado = 4;Game.SetEstado(4,1);}
   	                if (Game.Rclick()) 
                    {
                        Moto.dificultad = wmop+1;
                        estado = 44;
                        Game.SetEstado(44,1);
                        pi = Game.loadImg("/pi");                       
                    }
                    Wop(2);                    
   	                break;
   	       case 44:
   	                //Game.blit(0,0,176,208,0,0,Game.imenu); 
   	                int ff = (Moto.dificultad*2)+wmop-2;//-1;
   	                //if (wmop == 0) ff = Moto.dificultad;
   	                //else if (Moto.dificultad == 2) ff = 0;   
   	                //if (ff >= 0) 
   	                Game.blit(ff*80,0,80,95,(w/2)-40, 50,pi);
   	                
   	                String pils[] = Game.jugadores[Moto.dificultad-1];
   	                Game.Print(205,255,69,Lang.corredor,w/2,24,Graphics.HCENTER);
   	                
   	                if (wmop == 1 && !Moto.joc.joculto[Moto.dificultad-1])
   	                {
   	                    Game.Print(205,255,69,Lang.soloparaben,w/2,h-32,Graphics.HCENTER);   	                    
   	                    Game.Print(128,128,128,pils[wmop],w/2,158,Graphics.HCENTER);   	                    
   	                }
   	                else 
   	                    Game.Print(128,200,255,pils[wmop],w/2,158,Graphics.HCENTER);
   	                Game.blit(9,88,8,11,w-(w/8)-9+((tim/4)%8),100,Game.wm);//FLECHA
                    Game.blit(0,88,8,11,(w/8)-((tim/4)%8),100,Game.wm);//FLECHA
   	                //System.out.prin
   	                Game.Print(205,255,69,"men�",8, h-16,Graphics.LEFT);
   	                if (apretando && Game.rkey == -6) {estado = 4;Game.SetEstado(4,1);}
   	                Game.Click();
   	                
   	                /*Game.SetPrint(w/2, Game.TITLEY, Graphics.HCENTER);
   	                Game.MultiPrint(Lang.corredor);
   	                Game.SetPrint(w/2, Game.FIRSTLINEY+(Game.LINEHEIGHT*2),Graphics.HCENTER);
   	                if (wmop == 1 && !Moto.joc.joculto[Moto.dificultad-1]) Game.cprint = 0x808080;
   	                Game.MultiPrint(pils[wmop]);*/
                    if (Game.Rclick() && !(wmop == 1 && !Moto.joc.joculto[Moto.dificultad-1])) 
                    {
                        pi = null;                        
                        Game.SetEstado(nestado,1);
                        estado = nestado;
                        Game.moto[0].piloto = pils[wmop];
                        for(int i = 1;i< Game.MAXCORREDORES;i++)       	
   	                        Game.moto[i].piloto = Game.pilotos[Moto.dificultad-1][i];
   	                    if (wmop == 1) Game.moto[5].piloto = pils[0];
                    }
                    Wop(1);                    
   	                break; 
           case 3:
                    //Game.blit(0,0,176,208,0,0,Game.imenu);              
                    for(int i = 0; i < Lang.credits.length-1;i++)
                    {
                        Game.Print(205,255,69,Lang.credits[i],w/2, start+(i*15),Graphics.HCENTER);                        
                    }
                    if (start + Lang.credits.length*15 > h) start -= 1;  	
                    Game.Click();
           			if (Game.Rclick()) {estado=4;Game.currentkey=0;Game.rkey = 0;start = 14;
                    }
           			
           			break;
           			/*
           			for (int i = 0 ;i < Lang.credits.length;i++)
                     {
                     	if (i> 2 && i%3 == 1)gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD , Font.SIZE_SMALL));	
                     	else gr.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN , Font.SIZE_SMALL));	                  
                        if ((cvar+20*i>-16) && (cvar+20*i < h)) 
                           if (Lang.credits[i]!="")  Game.Print(200,255,100,Lang.credits[i],w/2,cvar+20*i,Graphics.HCENTER);
                     }
           			if (cvar > -277)                   
                  {
                     cvar-=2;                     
                  }
                  if (apretando && (Game.rkey ==-6 || Game.rkey == -7)) { estado = 4;cvar=150;Game.currentkey=0;Game.rkey = 0;}  
                  
                  break;   */
           case 4: 
           
                Game.blit(0,0,176,208,0,0,Game.portada);
		        Game.blit(0,0,212,124,0,208-124,Game.portada2);
		        Game.blit(0,0,176,24,0,h-24,Game.portada3);
               	//Game.blit(0,0,120,120,30,20,Game.imenu);
                  //if (Moto.joc.sfx) lll[0][2]= ID_SOUNDON;
                  //else lll[0][2]= ID_SOUNDOFF;
                  //lll[1][2] = lll[0][2];
                  
                  Game.Sonido(0,1);
                  String s = Lang.men[lll[prestado][opmenu]];          	
                  Game.Print(205,255,69,s,w/2, 42+25,Graphics.HCENTER);
                  Game.blit(9,88,8,11,w-(w/8)-9+((tim/4)%8),41+25,Game.wm);//FLECHA
                  Game.blit(0,88,8,11,(w/8)-((tim/4)%8),41+25,Game.wm);//FLECHA

                  if (apretando)
                  {
                  	    //Game.cheat(Game.rkey);
                  	    if (currentkey==Canvas.LEFT) if (opmenu > 0) {opmenu--;}else opmenu = lll[prestado].length-1;//Game.currentkey=0;}
	                    if (currentkey==Canvas.RIGHT) 
	                  	if (opmenu < lll[prestado].length-1) {opmenu++;}else opmenu = 0;//Game.currentkey=0;}
	                    if (currentkey == Canvas.KEY_POUND && prestado==1){ estado=3;}//currentkey = 0;}      	
	                    if (currentkey==Canvas.FIRE || Game.rkey == -7) 
	                    {                     
	                        //if (Moto.rocco) 
	                        //try { Moto.display.vibrate(500);} catch(Exception e){}
	    
	                        switch(lll[prestado][opmenu])
	                        {
	                            case ID_CARRERARAPIDA:
	                                nestado = 7;
	                                Game.ResetRace();
	                                Game.modojuego = Game.SIMPLE;
	                                estado = 33;
	                                break;
	                            case ID_CAMPEONATO:
    	                            nestado = 10;wmop = 0;
    	                            estado = 33;
        	                  	    break;
        	                  	case ID_VERSUS:
        	                  	    estado = 9;
        	                  	    Game.modojuego = Game.VERSUS;
        	                  	    break;
        	                  	case ID_VSTIME:
        	                  	    nestado = 7;
        	                  	    Game.ResetRace();
	                                Game.modojuego = Game.VSTIME;
	                                estado = 33;
        	                  	    break;    
        	                    case ID_RESUME:
        	                        estado = prestado;break;        	                        
        	                    case ID_MENU:
        	                        estado = 4;opmenu = 0;Game.prestado=1;break;
        	                    case ID_QUIT:
        	                        estado=6;break;
        	                    case ID_HELP: 
        	                        estado=5;break;
        	                    case ID_CREDITS: 
        	                         estado=3;break;
        	                    case ID_OPCIONES:
        	                        estado=41;wmop=0;break;
        	                    case ID_VIEWRECORDS:
        	                        estado=42;
	                        }    	                  	 
    	                }
	                    Game.currentkey=0;Game.apretando=false;Game.rkey = 0;
                  }
                  break;
                  
               case 5:
                    //Game.blit(0,0,176,208,0,0,Game.imenu);              
                    int cstart = 28;
                    for(int i = 0; i < Lang.controls.length-1;i++)
                    {
                        Game.Print(205,255,69,Lang.controls[i],w/2, cstart,Graphics.HCENTER);
                        if (Lang.controls[i] == "") cstart += 6;
                        else cstart+=15;
                    }
                    Game.Click();
           			if (Game.Rclick()) {estado=4;Game.currentkey=0;Game.rkey = 0;}
           			
               	    
               	
               	//Game.BlitFondo();
                  /*for (int i = 0 ;i < Lang.controls.length;i++)
                  	Game.Print(100,155,0,Lang.controls[i],w/2, 30+i*32,Graphics.HCENTER);                                                            
                  if (apretando && currentkey != 0) 
                  { 
                  	estado = 3;
                  	Game.currentkey=0;Game.apretando=false;
                  } */ 
                  break;  
               case 7:
                  
                  if (apretando)
                  {
                        if (Game.rkey == -6) {estado = 4;Game.SetEstado(4,1);}
                  	    if (currentkey== Canvas.LEFT) wmop--;
	                    if (currentkey==Canvas.RIGHT) wmop++; 
	                    if (currentkey==Canvas.FIRE || Game.rkey==-7) {estado = 8;Game.circuit.EscogeCircuito(wmop);}
	                    Game.currentkey=0;Game.apretando=false;Game.rkey = 0;
	                    if (wmop < 0) wmop = Lang.circuitos.length-1;
	                    if (wmop >= Lang.circuitos.length) wmop = 0;
	               }   
	               //Game.blit(0,0,176,208,0,0,Game.imenu);
                	Game.blit(0,13,150,75,13,110,Game.wm);
                	
                	Game.blit(9,88,8,11,w-(w/8)-9+((tim/4)%8),70,Game.wm);//FLECHA
                	Game.blit(0,88,8,11,(w/8)-((tim/4)%8),70,Game.wm);//FLECHA
                	Game.blit(19,88,5,5,cirx[wmop]-2,ciry[wmop]-2+90,Game.wm);//PUNTO
                	Game.Print(255,255,0,Lang.circuitos[wmop],w/2, 40,Graphics.HCENTER);                                                            
                	Game.blit(wmop*22,0,22,13,(w/2)-12,70,Game.wm);
                	Game.Print(205,255,69,"men�",8, h-16,Graphics.LEFT);
   	               
                	Game.Click();
                  break;
               case 8: //CUANDO SALE LA PREVIEW DEL CIRCUITO                                 	
                    Game.Sonido(1,1);
                    //Game.blit(0,0,176,208,0,0,Game.imenu);
               
               	    int ol = tim%(176*2);
                    if (ol < 0) ol+=176*2;
                    gr.setColor(Game.circuit.cielo[0], Game.circuit.cielo[1], Game.circuit.cielo[2]); 		
 					gr.fillRect(0,67,176,45);
                    Game.blit(0, 0, 176*2, 45, -(ol)-176, 67, Game.fondo[0]);
				    Game.blit(0, 0, 176*2, 45, -(ol)+176, 67, Game.fondo[0]);
				    ol = (tim*2)%(176*2);
                    if (ol < 0) ol+=176*2;                  
   				    Game.blit(0, 0, 176*2, 45, -(ol), 67, Game.fondo[1]);
   				    Game.blit(0, 0, 176*2, 45, -(ol)+176*2, 67, Game.fondo[1]);
   				   
   				    Game.Print(255,255,0,Game.circuit.name,w/2, 40,Graphics.HCENTER);                                                            
   				    Game.Print(255,255,0,Lang.data[0]+": ",20, 130,Graphics.LEFT);                                                            
   				    Game.blit(25+Game.circuit.clima*16, 88, 15, 15, 89, 128, Game.wm);
   				   
   				    Game.Print(255,255,0,Lang.data[1]+": "+Game.circuit.vuelta*11,20, 150,Graphics.LEFT);                                                            
   				    Game.Print(255,255,0,Lang.data[2]+": "+Game.circuit.vueltas,20, 170,Graphics.LEFT);                                                            
                    Game.Print(255,255,0,Lang.mprev[0],5, h-14,Graphics.LEFT);                                                            
                    Game.Print(255,255,0,Lang.mprev[1],w-5, h-14,Graphics.RIGHT);
   				   
                    if (apretando)
                    {
                        if (currentkey==Canvas.FIRE || Game.rkey==-7) 
                        {
                            estado = -2;                     		
                            int conf[][] = {{250,4,12},{240,5,12},{240,4,14},{225,5,14},{250,5,10},{250,3,14}};
				            Game.moto[0].maxvelocidad = conf[Game.moto[0].conf][0];
                            Game.moto[0].aceleracion = conf[Game.moto[0].conf][1];
                            Game.moto[0].maniobrabilidad = conf[Game.moto[0].conf][2];
                 
                        }
                        if (Game.rkey==-6) estado = 12;         
                        Game.currentkey=0;Game.apretando=false;Game.rkey = 0;
               	    }
					break;   
				case 12:		  //EDITAR MOTO			    
				    //short config[][] = {{10,160,170,100,270,100},{0,180,180,90,270,90},{0,170,170,100,270,90},{10,170,180,90,270,100},,{0,180,180,100,280,80},{0,180,180,80,260,100}};
				    short config[][] = {{45,120,165,120,285,120},{60,105,165,150,315,105},{75,105,180,105,285,150},{90,80,170,140,310,140},{40,120,160,150,310,90},{60,120,180,90,270,150}};
				    //EDITAR MOTO
				
                    Game.Sonido(2,1);				    
				    if (changing)
                    {
                        if (motox > 9)
                        {                            
                            motox-=20;
                            quesitox += 20;
                            if (motox < 9) {motox = 9;changing = false;quesitox = 6;}
                        }
                        if (motox < 9)
                        {
                            motox-=20;
                            quesitox += 20;
                            if (motox <= -164) {motox = 176;quesitox = -164;Game.moto[0].conf = (++Game.moto[0].conf)%6;
                            //System.out.println("Acc"+conf[Game.moto[0].conf][1]);
                            //System.out.println("Man"+conf[Game.moto[0].conf][2]);
                            }
                        }
                    }
                    if (ending)
                    {
                        motox +=30;                    
                        if (motox >= 176)
                        {
                            estado = 8;ending = false;                     		                            
                        }
                    }       
                    
				    
                    //Game.blit(0,0,176,208,0,0,Game.imenu);
                    Game.blit(0,0,164,64,quesitox,30,Game.edit1);
                    Game.blit(0,0,158,86,motox,100,Game.edit2);
                    Game.Print(255,255,0,Lang.medit[0],5, h-14,Graphics.LEFT);                                                            
                    Game.Print(255,255,0,Lang.medit[1],w-5, h-14,Graphics.RIGHT);
                    Game.Print(120,218,255,(config[Game.moto[0].conf][1]*10)/36+"%",quesitox+(146-6), 29,Graphics.LEFT);
                    Game.Print(0,174,239,(config[Game.moto[0].conf][5]*10)/36+"%",quesitox+(146-6), 49,Graphics.LEFT);
                    Game.Print(0,84,166,(config[Game.moto[0].conf][3]*10)/36+"%",quesitox+(146-6), 69,Graphics.LEFT);
                    
                    //QUESO
                    gr.setColor(120,218,255);
                    gr.fillArc(quesitox+(61-6),34,55,55,config[Game.moto[0].conf][0],config[Game.moto[0].conf][1]);
                    gr.fillRect(quesitox+(130-6),30,8,8);
                    gr.setColor(0,84,166);
                    gr.fillArc(quesitox+(61-6),34,55,55,config[Game.moto[0].conf][2],config[Game.moto[0].conf][3]);
                    gr.fillRect(quesitox+(130-6),70,8,8);
                    gr.setColor(0,174,239);				    
                    gr.fillArc(quesitox+(61-6),34,55,55,config[Game.moto[0].conf][4],config[Game.moto[0].conf][5]);
				    gr.fillRect(quesitox+(130-6),50,8,8);
				    if (apretando && changing == false && ending == false)
                    {
                        if (currentkey==Canvas.FIRE || Game.rkey==-7)
                        {                             
                            ending = true;
                            motox += 14;
                        }
                        if (Game.rkey==-6){ changing = true;motox-=10;}
                        Game.currentkey=0;Game.apretando=false;Game.rkey = 0;
               	    }
               	    if (!changing && !ending ) {motox = 9;quesitox = 6;}
				    break;
				case 9:
                  
                  /* if (apretando)
                  {
                  	//if (currentkey==Canvas.LEFT) wmop--;
	                  //if (currentkey==Canvas.RIGHT) wmop++; 
	                  //if (currentkey==Canvas.FIRE || Game.rkey==-7) {estado = 8;Game.circuit.EscogeCircuito(wmop);}
	                  //Game.currentkey=0;Game.apretando=false;Game.rkey = 0;
	                  //if (wmop < 0) wmop = Lang.circuitos.length-1;
	                  //if (wmop >= Lang.circuitos.length) wmop = 0;
	               }   */
	               //Game.blit(0,0,176,208,0,0,Game.imenu);
	               Game.Print(255,255,0,"Espera por favor",w/2, 70,Graphics.HCENTER);                                                            
                	/*Game.blit(0,12,150,76,13,110,Game.wm);                	
                	Game.blit(9,88,8,11,w-(w/8)-9+((tim/4)%8),70,Game.wm);//FLECHA
                	Game.blit(0,88,8,11,(w/8)-((tim/4)%8),70,Game.wm);//FLECHA
                	Game.blit(19,88,5,5,cirx[wmop]-2,ciry[wmop]-2+90,Game.wm);//PUNTO
                	Game.Print(255,255,0,Lang.circuitos[wmop],w/2, 40,Graphics.HCENTER);                                                            
                	Game.blit(wmop*26,0,24,12,(w/2)-12,70,Game.wm);*/
                  break;
               
            	case 40: // PRESENTACION
            	    gr.setClip(0,0,w,h);
		            gr.setColor(0,0,0);gr.fillRect(0, 0, w, h);
		            if (intro >= 0)
		            {
		                Game.blit(0,0,212,124,Math.min(intro-232,0),208-124,Game.portada2);
            	        Game.blit(0,0,212,124,Math.min(intro-222,0),208-124,Game.portada2);
            	        Game.blit(0,0,212,124,Math.min(intro-212,0),208-124,Game.portada2);
            	    }
            	    else if (intro > -21*20)
            	    {
            	        //if (caida < animcm.length)   	        
   	                    //{ 
   	                        //int f = Math.min(caida, animcm.length-1);
   	                        int f = (intro + (21*20)) / 20;
   	                        Game.blit(0,61*Game.animcm[f],30,61,80,120+Game.animcmy[f],Game.caidam);
   	                        Game.blit(0,52*Game.animcp[f],39,52,80,120-Game.animcpy[f],Game.caidap);
       	                //}
            	    }
            	    else
            	    {
            	        Game.moto[0].offy = 0;
            	        Game.moto[0].ashape = 3;
            	        Game.moto[0].inclinacion = 0;
            	        Game.moto[0].x = 95;
            	        Game.moto[0].y = 120;
            	        Game.moto[0].DoBlit(gr);
            	        Game.PutSprite(Game.iroderes, 80, 120+intro%3, Game.moto[0].ashape, Game.roderescor);   
            	    }
            	    intro += 20;
            	    if (intro >= 280)
            	    {            	        
            	        if (intro == 280) {gr.setColor(255,255,255);gr.fillRect(0, 0, w, h);Game.Sonido(0,1);}
            	        else
            	        {
            	            
                	        Game.blit(0,0,176,208,0,0,Game.portada);            	    
                	        Game.blit(0,0,212,124,Math.min(intro-212,0),208-124,Game.portada2);
                	        Game.blit(0,0,176,24,0,h-24,Game.portada3);
                	        //Game.Print(255,255,0,Lang.intro[(intro-240)/50],w/2, 60,Graphics.HCENTER);                                                            
                	        if (apretando)
                                {estado = 4;Game.currentkey=0;Game.apretando=false;Game.rkey = 0;}
                        }
               	    }
            	        
                    /*Game.blit(0,0,176,208,0,0,Game.imenu);
					for(int i = 0;i< demo.length;i++)               	                                 	
   				    Game.Print(255,255,0,demo[i],w/2, 40+i*16,Graphics.HCENTER);                                                            
   				    Game.Print(255,255,0,"Microjocs 2003",w/2, 190,Graphics.HCENTER);       
   					if (apretando)
                    {
                        estado = 4;                     		
                        Game.currentkey=0;Game.apretando=false;Game.rkey = 0;
               	    }*/
               	    break;              
               	case 10:    
               	    int c;
               	    String ww;
               	    if (Moto.gpclimate) ww = Lang.yes;
               	    else ww = Lang.no;
               	    //Game.blit(0,0,176,208,0,0,Game.imenu);
               	    for(int i = 0; i < 3;i++)
               	    {
                        if (wmop == i) c = 0; else c = 255;                                       
                        Game.Print(255,255,c,Lang.newgp[i],w/2, 40+i*20,Graphics.HCENTER);                    
                    }
                    Game.Print(100,255,100, Lang.savedgp[0],w/2, 105,Graphics.HCENTER);                    
                    Game.Print(255,255,0, Lang.savedgp[1]+" - "+Moto.points[0],40, 125,Graphics.LEFT);
                    Game.Print(255,255,0, Lang.savedgp[2]+" - "+Lang.circuitos[Moto.stage],40, 145,Graphics.LEFT);
                    Game.Print(255,255,0, Lang.savedgp[3]+" - "+Lang.difficulty[Moto.gpdificultad-1],40, 165,Graphics.LEFT);//PPP
                    Game.Print(255,255,0, Lang.savedgp[4]+" - "+ww,40, 185,Graphics.LEFT);
              
               	    if (apretando)
                    {
                  	    if (currentkey==Canvas.UP) wmop--;
	                    if (currentkey==Canvas.DOWN) wmop++; 
	                    if (currentkey==Canvas.FIRE || Game.rkey == -7) 
	                    {
	                        switch(wmop)
	                        {
	                            case 0: //REANUDAR GP
	                                    Game.ResetGame();
	                                    Game.modojuego = Game.CAMPEONATO;
	                                    Game.numcarrera = Moto.stage;
	                                    for(int i = 0;i< Game.MAXCORREDORES;i++) Game.moto[i].puntos = Moto.points[i];            
	                                    //PPP
	                                    for(int i = 1;i< Game.MAXCORREDORES;i++)       	
   	                                        Game.moto[i].piloto = Game.pilotos[Moto.gpdificultad-1][i];
   	                                    Game.moto[0].piloto = Moto.gpname;
   	                                    ////////    
	                                    estado = 8;
	                                    Game.circuit.EscogeCircuito(Game.numcarrera);               	    
	                                    break;
	                            case 1: Game.ResetGame();	                                    
        	                  	        Game.modojuego = Game.CAMPEONATO;
        	                  	        Moto.gpclimate = Moto.climate;   
        	                  	        Moto.gpname = Game.moto[0].piloto; //PPP
        	                  	        Moto.gpdificultad = Moto.dificultad;//PPP
        	                  	        Moto.stage = 0;
        	                  	        estado = 8;
        	                  	        Game.circuit.EscogeCircuito(0);               	    
        	                  	        break;
	                            case 2: estado = 4;break;
	                        }
	                    }
	                    Game.currentkey=0;Game.apretando=false;Game.rkey = 0;	                    
	                }   
               	                    
               	    if (wmop < 0) wmop = 2;
	                if (wmop > 2) wmop = 0;
               	    break;
               	    
               	 case 11:    //CONTINUAR GP               	   
               	    //Game.blit(0,0,176,208,0,0,Game.imenu);
               	    for(int i = 0; i < 3;i++)
               	    {
                        if (wmop == i) c = 0; else c = 255;                                       
                        Game.Print(255,255,c,Lang.continuegp[i],w/2, 40+i*20,Graphics.HCENTER);                    
                    }
                    
                 	if (apretando)
                    {
                  	    if (currentkey==Canvas.UP) wmop--;
	                    if (currentkey==Canvas.DOWN) wmop++; 
	                    if (currentkey==Canvas.FIRE || Game.rkey == -7) 
	                    {
	                        switch(wmop)
	                        {
	                            case 0: Game.ResetRace();
	                                    estado = 8;
	                                    Game.circuit.EscogeCircuito(++Game.numcarrera);
	                                    Moto.stage = Game.numcarrera;
	                                    for(int i = 0; i < Game.MAXCORREDORES;i++) Moto.points[i] = Game.moto[i].puntos;
	                                    break;
	                            case 1: Game.ResetRace();
	                                    estado = 8;
	                                    Game.circuit.EscogeCircuito(++Game.numcarrera);
        	                  	        break;
	                            case 2: estado = 4;break;
	                        }
	                    }
	                    Game.currentkey=0;Game.apretando=false;Game.rkey = 0;	                    
	                }   
               	                    
               	    if (wmop < 0) wmop = 2;
	                if (wmop > 2) wmop = 0;
               	    break;         	    
               	case 41: // OPTIONS
               	    String sss;
               	    String vvv;
               	    String www;               	    
               	  
               	    if (Moto.sfx) sss = Lang.men[ID_SOUNDON];
                    else sss = Lang.men[ID_SOUNDOFF];
                    /*if (Moto.rocco) vvv = Lang.men[ID_VIBRAON];
                    else vvv = Lang.men[ID_VIBRAOFF];*/
                    if (Moto.climate) www = Lang.yes;
                    else www = Lang.no;
                    
                    //Game.blit(0,0,176,208,0,0,Game.imenu);
                    if (wmop == 0) c = 0; else c = 255;                                       
                    Game.Print(255,255,c,Lang.smenu,w/2, 40,Graphics.HCENTER);                    
                    if (wmop == 1) c = 0; else c = 255;                   
                    Game.Print(255,255,c,Lang.playername+": "+Moto.jugador,w/2, 60,Graphics.HCENTER);              
                    if (wmop == 2) c = 0; else c = 255;                   
                    Game.Print(255,255,c,Lang.weather+": "+www,w/2, 80,Graphics.HCENTER);                                  
                    if (wmop == 3) c = 0; else c = 255;                                       
                    Game.Print(255,255,c,sss,w/2, 100,Graphics.HCENTER);                                  
                    if (wmop == 4) c = 0; else c = 255;                                       
                    Game.Print(255,255,c,Lang.smenu,w/2, 120,Graphics.HCENTER);
                    
                    if (apretando)
                    {
                  	    if (currentkey==Canvas.UP) wmop--;
	                    if (currentkey==Canvas.DOWN) wmop++; 
	                    if (currentkey==Canvas.FIRE || Game.rkey == -7) 
	                    {
	                        switch(wmop)
	                        {
	                            case 1: Moto.joc.enterName();break;
	                            //case 2: Moto.dificultad++;if (Moto.dificultad > 3) Moto.dificultad = 1;break;
	                            case 2: Moto.climate = !Moto.climate;break;
	                            case 3: Moto.sfx = !Moto.sfx;break;
	                            case 0: 
	                            case 4: estado = 4;break;
	                        }
	                    }
	                    Game.currentkey=0;Game.apretando=false;Game.rkey = 0;	                    
	                }   
	                if (wmop < 0) wmop = 4;
	                if (wmop > 4) wmop = 0;
                    break;
				    
   			}    
   		 if (estado != 4) opmenu = 0;      	
         return  estado;     			
	}
	
	//menu mapa
	int wmop;
 	int cirx[] = {82,89,83,144,83,85,87,85,-5};
 	int ciry[] = {50,51,46,53,42,48,44,44,-5};
 	
 	
   final int lll[][] = {{ID_RESUME , ID_HELP, ID_MENU, ID_QUIT },
					   {ID_CARRERARAPIDA,ID_CAMPEONATO, ID_VSTIME, ID_OPCIONES, ID_HELP, ID_VIEWRECORDS, ID_CREDITS, ID_QUIT }};
  
/*
	public void commandAction(Command c, Displayable d)
	{	   	 	
		VuelcaValores();
		Moto.joc.display.setCurrent(Moto.joc.pantgame);	
		Game.estado = 4;
	
	}

	public void CargaValores()
	{
		vueltaField.setString(""+Game.circuit.vuelta);
		stepField.setString(""+Game.circuit.step);
	}

	public void VuelcaValores()
	{
		Game.circuit.col = col[cg.getSelectedIndex()]; 
	}
*/
	
}