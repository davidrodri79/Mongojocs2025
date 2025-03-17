package com.mygdx.mongojocs.mr;
//////////////////////////////////////////////////////////////////////////
// Movistar MotoGP - Color Version 1.0
// By Carlos Peris
// MICROJOCS MOBILE , 2003
//////////////////////////////////////////////////////////////////////////


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

import java.util.Random;

public class Dengine
{
	int NUMPUNTOS = 32;//puntos a rotar
	int CARPUNTOS = 32;// puntos pertenecientes a la carretera
	int EXTRAPUNTOS = 0;
	
	final int MAXPUNTOS = 128;
	int vx, vy, vw, vh;
 	Punto punt[] = new Punto[MAXPUNTOS];
 	Tramo tramo[] = new Tramo[MAXPUNTOS/2];
 	int proj[] = new int[MAXPUNTOS*2];	
	int gradox = 6350, gradoy= 100, gradoz=0;
	//int cx = 30,cy = 14+50;
	int cx = 0,cy = 14;
	final static int RESOLUTION = 2;
    Circuit c; 		
    Random rnd = new Random();
 		
 	public Dengine(int _vx, int _vy, int _vw, int _vh)
 	{
 			vx = _vx;
 			vy = _vy;
 			vw = _vw;
 			vh = _vh;
 			for(int i = 0; i < MAXPUNTOS;i++) punt[i] = new Punto(0,i,0); 			
 			for(int i = 0; i < MAXPUNTOS/2;i++) tramo[i] = new Tramo();
 			
 	}
 	
 	
 	int sinA;
 	int cosA;
 	int sinB;
 	int cosB;
 	int sinC;
 	int cosC;
 	int abprod;
 	int deprod;
 	int ghprod;
 	int _a, _b,_c,_d,_e,_f, _g,_h,_i;	
 	
 	
 	public void ModifyAxis()
 	{ 	 			 		 		
 		/*sinA = Trig.zsin(gradox);
 		cosA = Trig.zcos(gradox);
 		sinB = Trig.zsin(gradoy);
 		cosB = Trig.zcos(gradoy);
 		sinC = Trig.sin(gradoz);
 		cosC = Trig.cos(gradoz);
 		
 		_a = -((sinA*cosC)>>10)+((((cosA*sinB)>>10)*sinC)>>10);
 		_b = (sinA*sinC)>>10;
 		_b = _b+ (((cosA*sinB)>>10)*cosC>>10);
 		_c = (cosA*cosB)>>10;
 		_d = (cosB*sinC)>>10;
 		_e = (cosB*cosC)>>10;
 		_f = -sinB;
 		_g = (cosA*cosC)>>10; 		
 		_g = _g + (((sinA*sinB)>>10)*sinC)>>10;
 		_h = -(cosA*sinC)>>10;
 		_h = _h + (((sinA*sinB)>>10)*cosC)>>10; 		
 		_i = (sinA*cosB)>>10;
 		abprod = (_a*_b);
 		deprod = (_d*_e);
 		ghprod = (_g*_h);*/
 	}	
 	
 	public void RotateAndProj(Punto cam, Punto src[], int dest[], int NUM)
 	{
 		for (int i = 0;i < NUM;i++)
 		{
 			int x = src[i].x-cam.x;
 			int y = src[i].y-cam.y;
			int z = src[i].z-cam.z;
 			int xyprod = punt[i].x*punt[i].y;
 			int newx = ((-1023+y)*x)  - xyprod;
 			int newy = (y*(1023+x))  - xyprod + (-25*z);
 			int newz = (y*(24+x))  - xyprod + (1022*z);
 			/*int newx = ((((_a)+y)*(_b+x))) - abprod - xyprod + (((_c)*z));
 			int newy = ((((_d)+y)*(_e+x))) - deprod - xyprod + (((_f)*z));
 			int newz = ((((_g)+y)*(_h+x))) - ghprod - xyprod + (((_i)*z));*/
 			newz = newz>>9;
 			//newz += 1000;
 			newz += 1000;
 			
 			if (newz == 0) newz = 1;
 			
 			newx = vx+(newx / newz)+(vw/2)+cx;
 			newy = vy+(newy / newz)+(vh/2)+cy;
 			 			 		
 						
 			dest[(((NUM-i-1)*2))] = newx*vw/176;
 			dest[(((NUM-i-1)*2)+1)] = newy*vh/162;
 			//proj[i*2]= newx;
 			//proj[(i*2)+1]= newy;
 		} 	
 	}
 	
 	public void Clima1(Circuit cc, Graphics gr)
 	{
 	    //LLUVIA1
 		if (cc.clima == cc.LLUVIA || cc.clima == cc.TORMENTA)
 		{     	
     		gr.setColor(150,150,255);
     		for(int i = 0; i < 50;i++)
     		{
     			int x = Math.abs(rnd.nextInt()%vw);
     			int y = Math.abs(rnd.nextInt()%vh);
     			gr.drawLine(x,y,x-2,y+2);
     		}
        }
        else if (cc.clima == cc.VIENTO)
        {
            if (cc.begin > 143 && cc.begin < 293)
            {
                gr.setColor(150,80,55);
     		    for(int i = 0; i < 40;i++)
     		    {
     			    int x = ((-Game.nfc*3)+i*42439)%176;//Math.abs(rnd.nextInt()%vw);
     			    int y = (i*2423)%176;//Math.abs(rnd.nextInt()%vh);
     			    gr.drawLine(x,y,x,y);
     		    }   
            }
        }
        else if (cc.clima == cc.NIEVE)
        {
                gr.setColor(210,210,220);
     		    for(int i = 0; i < 40;i++)
     		    {
     		        int x = (i*2423)%176;//Math.abs(rnd.nextInt()%vh);
     			    int y = ((Game.nfc<<1)+i*42439)%176;//Math.abs(rnd.nextInt()%vw);     			    
     			    gr.drawLine(x,y,x,y);
     		    }               
        }
 	}
 	
 	
 	final int cseq[] = {0,0,0,0,1,1,1,2,2,2,3,3,4,4,5,5};
 	//final int tseq[] = {150,206,300,370};
 	
 	public void Draw(Graphics gr, Punto cam, Circuit cc)
 	{ 	
 		c = cc;
 		ModifyAxis();
 		RotateAndProj(cam, punt, proj, CARPUNTOS); 		
 		gr.setColor(c.cielo[0], c.cielo[1], c.cielo[2]); 		
 		gr.setClip(vx, vy, vw, vh);

		if (gr.fromImage == null)
			Display.fbo.begin();
		else
			gr.fromImage.fbo.begin();

		gr.shapeRenderer.setProjectionMatrix(gr.camera.combined);
		gr.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

 		if (cc.clima == cc.TORMENTA) 
 		{
 		    switch(Game.nfc%400)
 		    {
 		        case 150:case 206:case 300:case 216:case 370: 
 		        case 152:case 208:case 302:case 218:case 372: 
 		            gr.setColor(0,0,0); 
 		            gr.fillRectPrimitive(gr, vx, 0, vw, 147);
 		            break; 
 		        case 151:case 207:case 301:case 217:case 371:  		            		
 		            gr.setColor(255,255,255); 
 		            gr.fillRectPrimitive(gr, vx, 0, vw, 147);
 		            break; 
 		        default:
 		            for (int i = 0; i < 147; i+= 8)
             		{
             		    int desp = ((i-64));
             		    int r = Math.min(Math.max(0, c.cielo[0]+desp), 255);
             		    int g = Math.min(Math.max(0, c.cielo[1]+desp), 255);
             		    int b = Math.min(Math.max(0, c.cielo[2]+desp), 255);
             		    gr.setColor(r, g, b); 		
             		    gr.fillRectPrimitive(gr, vx, vy+i, vw, 8);
             		}       
             		break;
 		    } 		 	
 		}
 		else
 		{
     		for (int i = 0; i < 147; i+= 8)
     		{
     		    int desp = ((i-64));
     		    int r = Math.min(Math.max(0, c.cielo[0]+desp), 255);
     		    int g = Math.min(Math.max(0, c.cielo[1]+desp), 255);
     		    int b = Math.min(Math.max(0, c.cielo[2]+desp), 255);
     		    gr.setColor(r, g, b); 		
     		    gr.fillRectPrimitive(gr, vx, vy+i, vw, 8);
     		}
 		}

 		gr.setColor((cc.ocol[0]*90)/100,(cc.ocol[1]*90)/100,(cc.ocol[2]*90)/100);
 		gr.fillRectPrimitive(gr, vx, vy+147, vw, vh-147);

		gr.shapeRenderer.end();

		if (gr.fromImage == null)
			Display.fbo.end();
		else
			gr.fromImage.fbo.end();
 		//int ofx = (cam.x%176)/10;
 		//TODO: OPTIMIZAR BLIT
 		BlitFondo(gr, vw, vh);
	    
 		
 		
 		//Game.blit(0,0,352,45,0,47,Game.fondo[0]);
 		//Game.blit(0,0,352,45,0,47,Game.fondo[1]);
 		gr.setClip(vx, vy, vw, vh); 		
 		//gr.setColor(255, 255, 255);
 		Clima1(cc, gr);

 		for(int i = 0; i < (NUMPUNTOS*2);i+=4)
 		{
 			Tramo t = tramo[i/4];
 			gr.setClip(vx, vy, vw, vh);
 			//OUTER ROAD
 			if ((Game.circuit.begin+(i/4))%2 == 0)
 			{
	 			gr.setColor(Math.max(Game.circuit.ocol[0]-i,0), Math.max(Game.circuit.ocol[1]-i,0), Math.max(Game.circuit.ocol[2]-i,0)); 		
	 			if (t.extra == -1) gr.setColor(Game.circuit.ocol[0]/2, Game.circuit.ocol[1]/2, Game.circuit.ocol[2]/2); 		
	 		}
	 		else
	 		{
	 			gr.setColor(Math.max((Game.circuit.ocol[0]*90/100)-i,0), Math.max((Game.circuit.ocol[1]*90/100)-i,0), Math.max((Game.circuit.ocol[2]*90/100)-i,0)); 		
	 			if (t.extra == -1) gr.setColor((Game.circuit.ocol[0]*90/100)/2, (Game.circuit.ocol[1]*90/100)/2, (Game.circuit.ocol[2]*90/100)/2); 		
	 		}	
	 	  	int y1 = proj[i+1];
   	   int y3 = proj[i+5];   
   	   gr.fillRect(vx,y1,vw,y3-y1);
   		//INNER ROAD      
 			fillTrap(gr, i, t);
 			if (t.extra > 0)
 			{
 				RotateAndProj(cam, t.pextra, t.jextra,2);
 				int cw = Game.cartelw[t.extra-1];
 				int ch = Game.cartelh[t.extra-1]; 	
 				int doble = 0;
 				if (t.extra == 2 || t.extra == 4) doble = cw;			
 				if (t.clado <= 0) Game.blit(0,cseq[i/4]*ch,cw,ch,t.jextra[0]-cw/2,t.jextra[1]-ch+1,Game.cartel[t.extra-1]);
 				if (t.clado >= 0) Game.blit(doble,cseq[i/4]*ch,cw,ch,t.jextra[2]-cw/2,t.jextra[3]-ch+1,Game.cartel[t.extra-1]);
 			} 			
 			else if (t.extra == -1)
 			{
				if (gr.fromImage == null)
					Display.fbo.begin();
				else
					gr.fromImage.fbo.begin();

				gr.shapeRenderer.setProjectionMatrix(gr.camera.combined);
				gr.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

 				RotateAndProj(cam, t.pextra, t.jextra,3+4);
 				//DELLANTE PUENTE
 				gr.setColor(255,255,255);
 				gr.fillRectPrimitive(gr, vx,t.jextra[1],vw,t.jextra[3]-t.jextra[1]);
 				//ABAJO PUENTE
 				gr.setColor(127,127,127);
 				gr.fillRectPrimitive(gr, vx,t.jextra[3],vw,t.jextra[5]-t.jextra[3]);
 				//COLUMNA +x DELANTE
 				gr.setColor(255,255,255);
 				gr.fillRectPrimitive(gr, t.jextra[8], t.jextra[9], t.jextra[6]-t.jextra[8], t.jextra[7]-t.jextra[9]);
 				//COLUMNA -x DELANTE
 				gr.fillRectPrimitive(gr, t.jextra[12], t.jextra[9], t.jextra[10]-t.jextra[12], t.jextra[7]-t.jextra[9]);

				gr.shapeRenderer.end();

				if (gr.fromImage == null)
					Display.fbo.end();
				else
					gr.fromImage.fbo.end();
 			}
 			else if (t.extra == -2 || t.extra == -4 || t.extra == -5)
 			{
 			    int _src = 4;
 			    if (t.extra == -5) _src = 5;
 				int cw = Game.cartelw[_src];
 				int ch = Game.cartelh[_src]; 	
 				RotateAndProj(cam, t.pextra, t.jextra,1);
 				Game.blit(0,cseq[i/4]*ch,cw,ch,t.jextra[0]-cw/2,t.jextra[1]-ch+1,Game.cartel[_src]);
 			}
 			if(i>=8 && i < 64-1) //!!!!!!!!!!!!
 			{
 			    //MOTOS
	 			for(int k = 1;k < 8;k++)
		 			if ((Game.moto[k].pos[0].z > t.pos.z-250 && Game.moto[k].pos[0].z <= t.pos.z) || Game.moto[k].dejavu == 1)
		 			{
		 			    if (Game.moto[k].dejavu == 1) Game.moto[k].dejavu = 0;
		 			    else {Game.moto[k].dejavu = 1;continue;}
		 			    int tt = Game.moto[k].pos[0].z;
		 			    Game.moto[k].pos[0].z -= Game.circuit.modpos;
		 			    if (Game.moto[k].pos[0].z < Game.circuit.vuelta*125 && Game.moto[0].pos[0].z > Game.circuit.vuelta*125) Game.moto[k].pos[0].z += Game.circuit.vuelta*250;		 			    
		 			    //if (Game.moto[k].pos[0].z >= Game.circuit.vuelta*250) {Game.moto[k].pos[0].z -= Game.circuit.vuelta*250;}
		 				Game.moto[k].pos[0].x = (t.pos.x + t.curva*2)/3;
		 				Game.moto[k].pos[0].y = t.pos.y;
		 				RotateAndProj(cam, Game.moto[k].pos,Game.moto[k].proj ,1);
		 				Game.moto[k].pos[0].z = tt;
		 				Game.moto[k].setPosition(Game.moto[k].proj[0], Game.moto[k].proj[1]-Game.moto[k].height);
		 				Game.moto[k].offy = (5-cseq[(i/4)])*Game.moto[k].height;		 						 				
		 				Game.moto[k].DoBlit(gr);
		 				//if (Game.modojuego != Game.VSTIME)
		 				//{
		 				    Game.blit(87,82,13,9,Game.moto[k].proj[0]-15,Game.moto[k].y+15,Game.mon);
		 				    Game.PrintNumM(""+Game.moto[k].posicion, Game.moto[k].proj[0]-15+2,Game.moto[k].y-11+15);
		 				//}
		 			}
				if (Game.moto[0].pos[0].z > t.pos.z-250 && Game.moto[0].pos[0].z <= t.pos.z)
				{	
				    Game.moto[0].pos[0].y = t.pos.y;		
				    Game.moto[0].offy = (5-cseq[(i/4)])*Game.moto[0].height;		 		 			
					Game.moto[0].setPosition(Game.moto[0].proj[0],Game.moto[0].proj[1]-Game.moto[0].height);					
				}
	 		}	 		
 		}
 		//========================================================================
 		int tt = Game.moto[0].pos[0].z;		
        if (Game.endrace == 1)		
		    Game.moto[0].pos[0].z += Game.circuit.vuelta*250;		 			    		 			    
 		Game.moto[0].pos[0].z -= Game.circuit.modpos;
 		RotateAndProj(cam, Game.moto[0].pos,Game.moto[0].proj ,1); 		
		Game.moto[0].pos[0].z = tt;

 		//Game.moto[0].pos[0].z = Game.circuit.modpos;
 		if (Game.endrace < 4)
 		{
 		    Game.moto[0].setPosition(Game.moto[0].proj[0],vh-45+2); 		   
 		    Game.moto[0].offy = 0;
   	   	}
 
 		  	//LLUVIA2
 		  		gr.setClip(vx, vy, vw, vh); 		
     	
 	    if (cc.clima == cc.LLUVIA  || cc.clima == cc.TORMENTA)
 		{
     		gr.setColor(150,150,255);
     		for(int i = 0; i < 25;i++)
     			{
     				int x = Math.abs(rnd.nextInt()%vw);
     				int y = Math.abs(rnd.nextInt()%vh);
     				gr.drawLine(x,y,x-5,y+5);
     			}
     	}
     	 
        else if (cc.clima == cc.VIENTO)
        {
            if (cc.begin > 150 && cc.begin < 300)
            {
                for(int i = 0; i < 40;i++)
     		    {
     		        int x = Math.abs(rnd.nextInt()%vw);
     			    int y = Math.abs(rnd.nextInt()%vh);
     			    gr.setColor(150,80+((x+y)%70),55);     		         			    
     			    gr.drawLine(x,y,x-2-y%2,y+x%2-y%2);
     		    }   
            }
        }		
        else if (cc.clima == cc.NIEVE)
        {
            gr.setColor(240, 240, 255);
            for(int i = 0; i < 40;i++)
 		    {
 		        int x = Math.abs(rnd.nextInt()%vw);
 			    int y = Math.abs(rnd.nextInt()%vh); 	
 			    int grande = rnd.nextInt()%2;
 			    //int tw = rnd.nextInt()%2;		    
 			    //int th = rnd.nextInt()%2;	
 			    if (grande == 0)	    
 			    { 			        
 			        gr.drawLine(x+1,y,x+1,y+2);
 			        gr.drawLine(x,y+1,x+2,y+1); 			        
 			    }
 			    else
 			    {
 			        gr.fillRect(x,y+1,4,2);
 			        gr.fillRect(x+1,y,2,4);

 			    }
 			    //gr.fillArc(x,y, 3+tw, 3+th, 0, 360); 

 		    }   
            
        }
 		/*for(int i = 0; i < (NUMPUNTOS*2);i+=4)
 		{
 			if (tramo[i/4].extra > 0)
 			{
 				ModifyAxis(cam, tramo[i/4].pextra, tramo[i/4].jextra,2);
 				Game.blit(0,(i/20)*33,50,33,tramo[i/4].jextra[0]-25,tramo[i/4].jextra[1]-32,Game.extra);
 				Game.blit(0,(i/20)*33,50,33,tramo[i/4].jextra[2]-25,tramo[i/4].jextra[3]-32,Game.extra);
 			}
		} */
 	
 	}
 	
 	
 	public void fillTrap(Graphics gr, int i, Tramo src)
   {   	
   	int x1 = proj[i+0];
   	int y1 = proj[i+1];
   	int x2 = proj[i+2];
   	int y2 = proj[i+3];
   	int x3 = proj[i+4];
   	int y3 = proj[i+5];
   	int x4 = proj[i+6];
   	int y4 = proj[i+7];
   	if (y3-y1 == 0) return;
   	boolean norm = (Game.circuit.begin+(i/4))%2 == 0;
   	int incxi = 0;
   	if ((y3-y1) != 0)
   	   incxi = (1000*(x3-x1))/(y3-y1);   	
   	//else return;   
   	int incxd = 0;
   	if ((y4-y2) != 0)
   	   incxd= (1000*(x4-x2))/(y4-y2);
   	//else return;   
   	int xi = x1*1000;
   	int xf = x2*1000;
   	
   	//gr.drawLine(x1,y1, x2, y2);
   	int step = 0;	   
   	
   	/*if (RESOLUTION == 1)	   
			for (int j = y1; j < y3;j++)
	   	{
	   		gr.setColor(src.r, src.g, src.b);
	   		gr.drawLine((xi>>10),(j),(xf>>10)+1,j);	   		
	   		//LADOS
				gr.setColor(src.lr,src.lg,src.lb);				
				gr.drawLine((xi>>10),(j),(xi>>10)+(xf-xi)/20000,j);
				gr.drawLine((xf>>10),(j),(xf>>10)-(xf-xi)/20000,j);
				xi += incxi;
	   		xf += incxd;	   		
	   	}   		
   	else   	   	
   	{*/
   		for (int j = y1; j < y3;j++)
	   	{	   	
	   		if (step%4 == 0)// && y3-j > 3) 
	   		{	   			
	   			//if (src.extra == -1) gr.setColor(src.r/2, src.g/2, src.b/2);	   		
	   			//else gr.setColor(src.r, src.g, src.b);	 
	   			if (norm) gr.setColor(c.col[0]-i, c.col[1]-i, c.col[2]-i);	 
    	   		else gr.setColor((Game.circuit.col[0]*90-i)/100, (Game.circuit.col[1]*90-i)/100, (Game.circuit.col[2]*90-i)/100);	 
    	   			
	   			//if (norm) gr.setColor(c.col[0], c.col[1], c.col[2]);	 
	   			//else gr.setColor((Game.circuit.col[0]*90)/100, (Game.circuit.col[1]*90)/100, (Game.circuit.col[2]*90)/100);	 
	   			if (src.extra == -1) gr.setColor(Game.circuit.col[0]/2, Game.circuit.col[1]/2, Game.circuit.col[2]/2);	   		
	   			if (src.extra == -3) gr.setColor(255, 255, 255);	   		
	   			gr.fillRect((xi>>10),(j),(xf>>10)+1-(xi>>10),4);	   				   			
	   			
	   			if (norm)
	   			{
		   			if (src.extra == -1) gr.setColor(c.ocol1[0]/2,c.ocol1[1]/2,c.ocol1[2]/2);					
		   			else gr.setColor(c.ocol1[0],c.ocol1[1],c.ocol1[2]);					
	   			}
	   			else
	   			{
	   				if (src.extra == -1) gr.setColor(c.ocol2[0]/2,c.ocol2[1]/2,c.ocol2[2]/2);					
		   			else gr.setColor(c.ocol2[0],c.ocol2[1],c.ocol2[2]);					
	   			}
	   		}	
	   		//if (y3-j < 4)	   		
					//gr.drawLine((xi/1000),(j),(xf/1000)+1,j);	   			   		
			
				//LADOS
				gr.fillRect((xi>>10),(j),(xf-xi)/16000,1);
				gr.fillRect((xf>>10)-(xf-xi)/16000,(j),(xf-xi)/16000,1);
				//gr.drawLine((xi>>10),(j),(xi>>10)+(xf-xi)/20000,j);
				//gr.drawLine((xf>>10),(j),(xf>>10)-(xf-xi)/20000,j);
				/*if (step%2 == 0)
				{
					gr.fillRect((xi>>10),(j),(xf-xi)/20000,2);
					gr.fillRect((xf>>10)-(xf-xi)/20000,(j),(xf-xi)/20000,2);
				}*/
				//END LADOS   
				xi += incxi;
	   		xf += incxd;	   		
	   			step++;
	   	}
   	//}
   }




/*
			gr.setColor(src.r, src.g, src.b);
   		if (resolution == 1)
   			gr.drawLine((xi/1000),(j),(xf/1000)+1,j);
   		else	
   			gr.fillRect((xi/1000),(j),(xf/1000)+1-(xi/1000),resolution);
			//LADOS
			gr.setColor(src.lr,src.lg,src.lb);
			
			if (resolution == 1)
   		{
				gr.drawLine((xi/1000),(j),(xi/1000)+(xf-xi)/20000,j);
				gr.drawLine((xf/1000),(j),(xf/1000)-(xf-xi)/20000,j);
			}
			else
			{
				gr.fillRect((xi/1000),(j),(xf-xi)/20000,resolution);
				gr.fillRect((xf/1000)-(xf-xi)/20000,(j),(xf-xi)/20000,resolution);
			}	
			//END LADOS   
			for(int tk = 0;tk < resolution;tk++)
			{
   		xi += incxi;
   		xf += incxd;
   	}*/
   	
   	
   	
   	/*
   		RUTINA WAPA
   		for (int j = y1; j < y3;j++)
	   	{
	   		gr.setColor(src.r, src.g, src.b);
	   		gr.drawLine((xi/1000),(j),(xf/1000)+1,j);	   		
	   		//LADOS
				gr.setColor(src.lr,src.lg,src.lb);				
				gr.drawLine((xi/1000),(j),(xi/1000)+(xf-xi)/20000,j);
				gr.drawLine((xf/1000),(j),(xf/1000)-(xf-xi)/20000,j);
				xi += incxi;
	   		xf += incxd;	   		
	   	}*/
	   	
	   	
	   	int vx1,vx2,vx3,vx4,vx5 = 0;
	
	public void MiniBlit(int _i1, int y, Image i)
	{
	        int i1 = _i1;
	        int i2; 
		    int d;
		    //i1 = -(Game.circuit.ofx0)-176;
		    i2 = i1 + (176*2);
		    i1 = Math.max(0, i1);
		    i2 = Math.min(176, i2);
		    d = -(_i1)+i1;
		    Game.blit(d, 0, i2-i1, 45, i1, y, i);
		    
	}
	
	public int Normaliza(int val)
	{
	    if (val < -176) val += 176*2;
		if (val >= 176) val -= 176*2;
		return val;	   	    
	}
	

	public void BlitFondo(Graphics gr, int w, int h)
	{
		    int fac = 1;
	        int val;						
			boolean doub = Game.circuit.clima == Circuit.VIENTO;										
			
	        if (doub) fac = 2;
	        if (Game.nfc%2 == 0 || doub) vx1--;
			if (vx1 <= -w) vx1 = 0;
			int vx11 = vx1+w;
			if (vx11 > w) vx11 = vx11-w;
			vx2-=fac;
			if (vx2 <= -w) vx2 = 0;
			int vx22 = vx2+w;
			if (vx22 > w) vx22 = vx22-w;
			vx3-=2*fac;
			if (vx3 <= -w) vx3 = 0;
			int vx33 = vx3+w;
			if (vx33 > w) vx33 = vx33-w;
						
						
			gr.setClip(0,18+37+20-12-16,w,19);
			val = Normaliza(-Game.circuit.ofx0 + vx1);				
			gr.drawImage(Game.zimzum,val, 18-12-16, Graphics.LEFT | Graphics.TOP);	     			
	   	    val = Normaliza(-Game.circuit.ofx0 + vx11);				
			gr.drawImage(Game.zimzum,val, 18-12-16, Graphics.LEFT | Graphics.TOP);	     	
			
			
			gr.setClip(0,18+37-12,w,20);
			val = Normaliza(-Game.circuit.ofx0 + vx2);
	   	    gr.drawImage(Game.zimzum,val, 18-12, Graphics.LEFT | Graphics.TOP);	     			
	   	    val = Normaliza(-Game.circuit.ofx0 + vx22);
	   	    gr.drawImage(Game.zimzum,val, 18-12, Graphics.LEFT | Graphics.TOP);	     	
			
			
			gr.setClip(0,18,w,37);
			val = Normaliza(-Game.circuit.ofx0 + vx3);							
	   	    gr.drawImage(Game.zimzum,val, 18, Graphics.LEFT | Graphics.TOP);	     			
	   	    val = Normaliza(-Game.circuit.ofx0 + vx33);							
	   	    gr.drawImage(Game.zimzum,val, 18, Graphics.LEFT | Graphics.TOP);	     			
		
		    
		    MiniBlit(-(Game.circuit.ofx0)-176, 147-45, Game.fondo[0]);
		    MiniBlit(-(Game.circuit.ofx0)+176, 147-45, Game.fondo[0]);
		    MiniBlit(-(Game.circuit.ofx1), 147-45, Game.fondo[1]);
		    MiniBlit(-(Game.circuit.ofx1)+176*2, 147-45, Game.fondo[1]);
		    
		    /*int i1; 
		    int i2; 
		    int d;
		    i1 = -(Game.circuit.ofx0)-176;
		    i2 = i1 + (176*2);
		    i1 = Math.max(0, i1);
		    i2 = Math.min(176, i2);
		    d = -(-(Game.circuit.ofx0)-176)+i1;
		    Game.blit(d, 0, i2-i1, 45, i1, 147-45, Game.fondo[0]);
		    i1 = -(Game.circuit.ofx0)+176;
		    i2 = i1 + (176*2);
		    i1 = Math.max(0, i1);
		    i2 = Math.min(176, i2);		    
		    d = -(-(Game.circuit.ofx0)+176)+i1;
	        Game.blit(d, 0, i2-i1, 45, i1, 147-45, Game.fondo[0]);
	        */
	        //Game.blit(0, 0, 176*2, 45, -(Game.circuit.ofx1), 147-45, Game.fondo[1]);
	        //Game.blit(0, 0, 176*2, 45, -(Game.circuit.ofx1)+176*2, 147-45, Game.fondo[1]);
			
			/*
			gr.setClip(0,70,w,40);
	   	    gr.drawImage(Game.piram,vx2, 70, Graphics.LEFT | Graphics.TOP);	     			
	        gr.drawImage(Game.piram,vx22, 70, Graphics.LEFT | Graphics.TOP);	     
	      
			gr.setClip(0,50,w,31);
	   	    gr.drawImage(Game.piram,vx3, 50-85, Graphics.LEFT | Graphics.TOP);	     			
	   	    gr.drawImage(Game.piram,vx33, 50-85, Graphics.LEFT | Graphics.TOP);	     			
			
			gr.setClip(0,22,w,45);
	   	    gr.drawImage(Game.piram,vx4, 22-40, Graphics.LEFT | Graphics.TOP);	     			
	   	    gr.drawImage(Game.piram,vx44, 22-40, Graphics.LEFT | Graphics.TOP);	     			
	   	
		   gr.setClip(0,0,w,40);
	   	    gr.drawImage(Game.piram,vx5, 0, Graphics.LEFT | Graphics.TOP);	     			
	        gr.drawImage(Game.piram,vx55, 0, Graphics.LEFT | Graphics.TOP);	     			*/
		}
	
}