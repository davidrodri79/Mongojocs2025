package com.mygdx.mongojocs.mr;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class Moto500 extends unit
{
	final static int DERECHA = -1;
	final static int IZQUIERDA = 1;
	
	Punto pos[] = new Punto[1];
	int proj[] = {0, 0};
	int velocidad;
	int maxvelocidad = 250;	
	int aceleracion = 4;
	int frenada = 10;
	int inclinacion = 0;
	int maniobrabilidad = 12;
	int desplazamiento = 0;
	int vueltas = 1;
	int posicion;
	int puntos;
	int recorrido = 0;
	long racetime = 0;
	long laptime = 0;
	long laptimes[] = new long[10];
	String piloto;
	boolean salido;
	int inmovil;
	int dejavu;
	int conf;
	
	public Moto500(Image i, int w, int h, int numf, int _offx, int _offy, String _p, byte[] _cor)
	{
		super(i, w, h, numf, _offx, _offy, _cor);
		pos[0] = new Punto(0,55,50);				
		piloto = _p;
	}
	
	public void Reset()
	{
	    pos[0] = new Punto(0,55,50);				
		velocidad = 0;
	    maxvelocidad = 250;	
	    aceleracion = 4;
	    frenada = 10;
	    inclinacion = 0;
	    maniobrabilidad = 12;
	    desplazamiento = 0;
	    vueltas = 1;
	    recorrido = 0;
	    racetime = 0;
	    laptime = 0;
	    inmovil = 0;
	    dejavu = 0;
	    salido = false;
	}
	
	public void acelera()
	{
	    velocidad += aceleracion;
		if (velocidad > maxvelocidad) velocidad = maxvelocidad;
	}


	public void frena()
	{
		velocidad -= frenada;
		if (velocidad < 0) velocidad = 0;
	}

	public void UpdateCPU(Circuit circuit, long gap)
	{
	    pos[0].z += velocidad;   	
		recorrido += velocidad;   	
		if (Game.circuit.vuelta <= vueltas)
		{
			laptime  += gap;
			racetime += gap;
		}
		Tramo t = null;
		
		if (pos[0].z >= Game.circuit.vuelta*250 && vueltas < 9) {pos[0].z -= Game.circuit.vuelta*250;laptimes[vueltas-1] = laptime;laptime=0;vueltas++;}
		
		//try{		
		t = circuit.GetTramo((pos[0].z/250)+4);				
		 //}catch(Exception e){System.out.println(e+"kk");}
		if (t.curva < -10) gira(-1);
   	    else if (t.curva > 10) gira(1);
   	    else 
   	    {
   		    if (inclinacion > 5) gira(-1);
   		    if (inclinacion < -5) gira(1);
   	    }   	   	
   	    if (Math.abs(pos[0].z-Game.moto[0].pos[0].z) < 200 && Math.abs(Game.moto[0].proj[0]-proj[0]) < 10)
   	    {
   	        Game.Sonido(6,1);
   	        Game.moto[0].velocidad = Game.moto[0].velocidad-15;
   	        if (Game.moto[0].velocidad < 0) Game.moto[0].velocidad = 0;
   	    }
	}
	
	
	public void UpdateNet(Circuit circuit)
	{
		pos[0].z += velocidad;   	
		recorrido += velocidad;   	
		//laptime  += gap;
		//racetime += gap;
		
		if (pos[0].z >= Game.circuit.vuelta*250) {pos[0].z -= Game.circuit.vuelta*250;laptimes[vueltas-1] = laptime;laptime=0;vueltas++;}
		
		Tramo t = circuit.GetTramo((pos[0].z/250)+1);
		if (t.curva < 10) gira(-1);
   	    else if (t.curva > 10) gira(1);
   	    else 
   	    {
   		    if (inclinacion > 5) gira(-1);
   		    if (inclinacion < -5) gira(1);
   	    }
	}
	


	//TODO: Esto solo sirve para tu moto
	public void Update(Circuit circuit, long gap)
	{
	    salido = false;
		pos[0].z += velocidad;   	
		recorrido += velocidad;   	
		laptime  += gap;
		racetime += gap;
		
		if (pos[0].z >= Game.circuit.vuelta*250) {pos[0].z -= Game.circuit.vuelta*250;laptimes[vueltas-1] = laptime;laptime=0;vueltas++;}
	    if (inmovil > 0) inmovil--;
	
		if (Game.caida > 0) return; 
		
		Tramo actual = Game.dengine.tramo[14];
		if (actual.extra == -2 || actual.extra == -4 || actual.extra == -5)
		{
		    if (Math.abs(actual.jextra[0]-proj[0]) < 30)
		    {
		        if (actual.extra == -5)
		        {
		            if (actual.jextra[0] > proj[0]) desplazamiento = -velocidad/8;
		            else desplazamiento = -velocidad/8;
		        }
		        if (actual.extra == -4) inmovil = 14;
		        if (actual.extra == -2) inmovil = 7;
		        
		    }
		}
		   //Game.caida = 1;
		/*if (Math.abs(pos[0].z-) < 200 && Math.abs(Game.moto[0].proj[0]-proj[0]) < 10)
   	    {
   	        Game.moto[0].velocidad = Game.moto[0].velocidad-15;
   	        if (Game.moto[0].velocidad < 0) Game.moto[0].velocidad = 0;
   	    }*/
		
		
		int fac = 2500;
		if (Game.circuit.clima == 1) fac = 2400;   	
   	    pos[0].x = pos[0].x - (circuit.tramo[circuit.begin].curva*velocidad/fac);   	 		
   	    pos[0].x += inclinacion/7;//;(inclinacion*velocidad)/1500;
   	    pos[0].x += desplazamiento;
   	    if (Game.circuit.clima == Game.circuit.NIEVE) pos[0].x += dejavu/2;
   	    if (Game.circuit.clima == Game.circuit.TORMENTA) pos[0].x += dejavu/3;
   	    if (Game.circuit.clima == Game.circuit.LLUVIA) pos[0].x += dejavu/4;
   	    
   	    if (inclinacion < dejavu) dejavu -= 2;
   	    if (inclinacion > dejavu) dejavu += 2;
   	    
   	    
   	    if (pos[0].z > 150*250 && pos[0].z < 300*250)
   	        pos[0].x += Game.circuit.wind;   	
		
   	
       	if (desplazamiento > 0) desplazamiento--;
    	if (desplazamiento < 0) desplazamiento++;
    		
       	if (pos[0].x < Game.dengine.punt[0].x)
       	{
       	    salido = true;
       	    if  (velocidad > (maxvelocidad/2))   	velocidad--;
       	}
       	 
       		
       	if (pos[0].x < Game.dengine.punt[0].x-30)
       	{ 
       		//if (circuit.GetTramo(circuit.begin+1).extra != 0)
       		if (circuit.GetTramo(circuit.begin+1).extra != 0)
       		{
       		  desplazamiento = Math.max((20*velocidad)/150, 10);
       		  if (velocidad > 180) Game.caida = 1;
       		  if (velocidad > 20) velocidad = (velocidad*80)/100;
       		  
       		  //pos[0].x += 10;
       		}
       	}
       	if (pos[0].x < Game.dengine.punt[0].x-30)   	 
       		pos[0].x = Game.dengine.punt[0].x-30;
       	
       	
       	if (pos[0].x > Game.dengine.punt[1].x)   	
        {   
            salido = true;
       		if  (velocidad > (maxvelocidad/2))   	velocidad--;
       	}	
       	if (pos[0].x > Game.dengine.punt[1].x+30)
       	{ 
       		if (circuit.GetTramo(circuit.begin+1).extra != 0)
       		{       		  
       		  desplazamiento = -Math.max((20*velocidad)/150,10);
       		  if (velocidad > 180) Game.caida = 1;
       		  if (velocidad > 20) velocidad = (velocidad*80)/100;
       		  //pos[0].x -= 10;
       		}
       	}
       	if (pos[0].x > Game.dengine.punt[1].x+30)   	 
       		pos[0].x = Game.dengine.punt[1].x+30;
       	   	
	}

	
	public void gira(int lado)
	{
	    if (inmovil > 0 || velocidad == 0) return;
		inclinacion += lado*maniobrabilidad;
		if (inclinacion > 70) inclinacion = 70;
		if (inclinacion < -70) inclinacion = -70;	
		//if (velocidad <= 15) inclinacion = (inclinacion*90)/100;			
	}


	public void DoBlit(Graphics gr)
	{
		if (inclinacion < 0)
		{
			if (inclinacion < -50) {ashape = 6;}
			else if (inclinacion < -30) {ashape = 5;}
			else if (inclinacion < -10) {ashape = 4;}
			else ashape = 3;
		}
		else
		{
			if (inclinacion > 50) {ashape = 0;}
			else if (inclinacion > 30) {ashape = 1;}
			else if (inclinacion > 10) {ashape = 2;}
			else ashape = 3;
		}	
		if (ashape >= 3) x-=15;
		else x-=40;	
		
		blit(gr);
	}
}