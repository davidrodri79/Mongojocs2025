package com.mygdx.mongojocs.mr;
//////////////////////////////////////////////////////////////////////////
// Movistar MotoGP - Color Version 1.0
// By Carlos Peris
// MICROJOCS MOBILE , 2003
//////////////////////////////////////////////////////////////////////////


import java.util.Random;

public class Circuit
{
    final static int SOL        = 0;
    final static int LLUVIA     = 1;
    final static int VIENTO     = 2;
    final static int NIEVE      = 3;
    final static int TORMENTA   = 4;
	int vuelta = 500;
	int vueltas = 3;
   int step   = 20;//10;//16//6
   int maxcurva   = 200;//400;//120;//50;
   int mincurva   = 100;//400;//120;//50;
   int facpend   = 70;//10;//40;
   int anchura = 150;
   Tramo tramo[];
   int cielo[] = {0, 168, 255};
	int begin;	
	int modpos;
	int pos=0;
	Random rnd;
	int col[] = {200,200,200};
	int ocol[] = {140,235,99};	
	int ocol1[] = {255,255,255};
	int ocol2[] = {255,0,0};
	String name;
	int atrezzo;
    int current;			
    int clima;
    int wind;
			
	public void Circuit()
	{
		
	}
	
	public void Copy(int[] des, int[] src)
	{
	    if (clima == LLUVIA)
	    {
	    	des[0] = src[0]*70/100;
	        des[1] = src[1]*70/100;
	        des[2] = src[2]*70/100;
	    }
	    else if (clima == NIEVE)
	    {
	        des[0] = (src[0]+src[0]+src[1]+src[2])/4;
	        des[1] = (src[1]+src[0]+src[1]+src[2])/4;
	        des[2] = (src[2]+src[0]+src[1]+src[2])/4;
	        
	    	des[0] = Math.min(des[0]+90,255);
	        des[1] = Math.min(des[1]+90,255);
	        des[2] = Math.min(des[2]+90,255);	        	        
	    }
	    else if (clima == TORMENTA)
	    {
	    	des[0] = src[0]*60/100;
	        des[1] = src[1]*60/100;
	        des[2] = src[2]*60/100;
	    }	    	    
	    else
	    {
	        des[0] = src[0];
	        des[1] = src[1];
	        des[2] = src[2];
	    }
	}
	
	public Tramo GetTramo(int src)
	{
		while (src >= (vuelta)) src -= (vuelta);
		while (src < 0) src+= vuelta;
		Tramo t = null;
		//	try{
		t = tramo[src];
		//}catch(Exception e){System.out.println(e+" src:"+src);}
		return t;
	}
	
	
	
	public void Make(long semilla)	
	{
		int faccurva = maxcurva - mincurva;
	    tramo = new Tramo[vuelta];
		rnd = new Random(semilla);
		
		for(int i = 0; i < vuelta;i++)
		{
			int angulo = (25600*i)/vuelta;
			tramo[i] = new Tramo();
		    tramo[i].SetPos(0,55,i*250);		
			if (i%8 == 0) 
			{
			    tramo[i].extra = atrezzo;//CARTELES
			    tramo[i].clado = 0;
			    //if (i < 50 || i > 750)
			    if (i%16 == 0 || (i < 50 || i > 750))tramo[i].extra = 1;
			}
			//if (i%8 == 4) 
			//else
			//tramo[i].extra = 3;//CARTELES
			if (clima == TORMENTA && i%72 == 0)//CHARCO			
				tramo[i].extra = -2;				
			else if (clima == LLUVIA && i%144 == 0)//CHARCO			
				tramo[i].extra = -2;				
			else if (clima == NIEVE && i%144 == 0)//CHARCO HIELO			
				tramo[i].extra = -4;								
			else if (i%144 == 0)//CHARCO ACEITE			
				tramo[i].extra = -5;				
			
			if (i%40 == 0)//PUENTE			
				tramo[i].extra = -1;				
			
		}
		//tramo[5].SetColor(255,255,255);				
		tramo[0].extra = -3;				
		for(int pos = 40;pos < vuelta-40;pos = pos + step)
        {
      	    tramo[pos+(step/2)].curva = rnd.nextInt()%faccurva;      	      
      	    if (tramo[pos+(step/2)].curva < 0) tramo[pos+(step/2)].curva -= mincurva;
      	    if (tramo[pos+(step/2)].curva > 0) tramo[pos+(step/2)].curva += mincurva;
      	    tramo[pos+(step/2)].altura = rnd.nextInt()%facpend;      	            
        }
        
        
        for(int pos = 40;pos < vuelta-40;pos = pos + step)
        {      	
          	int antcurva = tramo[pos-step+(step/2)].curva;      
          	int curva = tramo[pos+(step/2)].curva;      
          	int factorcurva = ((curva-antcurva)<<10)/step;      	
          	int antpend = tramo[pos-step+(step/2)].altura;      
          	int pend = tramo[pos+(step/2)].altura;      
          	int factorpend = ((pend-antpend)<<10)/step;      	      	
          	for(int npos = 0; npos < step; npos++)
          	{      		
          		tramo[pos+npos-step+(step/2)].curva = antcurva+((factorcurva*npos)>>10);      	
          		tramo[pos+npos-step+(step/2)].altura = antpend+((factorpend*npos)>>10);      			      		   			
          	}           	
        }
        for(int pos = 40;pos < vuelta-40;pos = pos + 1)
        {
      	    tramo[pos].curva = (tramo[pos-1].curva + tramo[pos].curva + tramo[pos+1].curva + tramo[pos+2].curva)>>2;
      	    tramo[pos].altura = (tramo[pos-1].altura + tramo[pos].altura + tramo[pos+1].altura + tramo[pos+2].altura)>>2;      	
        }

		for(int i = 0; i < vuelta;i++)
		{
			if (Math.abs(tramo[i].curva) > 90 && i%2 == 1)
			{
				tramo[i].extra = 4;//CARTELES
				tramo[i].clado = tramo[i].curva;						
			}
		}
		System.gc();
	}
	
	//int src;
	int ofx0, ofx1;
	public void Traspasa(Dengine den)
	{
		if (pos >= (vuelta)*250) pos -= (vuelta)*250;
		if (pos < 0) pos+= vuelta*250;
		begin = pos/250;
		modpos = pos%250;
		den.NUMPUNTOS = den.CARPUNTOS;				
		ofx0 -= (tramo[begin].curva*Game.moto[0].velocidad)/(4500);
		ofx1 -= (tramo[begin].curva*Game.moto[0].velocidad)/(2500);
	    if (ofx0 >= 176*2) ofx0 -= 176*2;
	    if (ofx0 < 0) ofx0 += 176*2;
	    if (ofx1 >= 176*2) ofx1 -= 176*2;
	    if (ofx1 < 0) ofx1 += 176*2;
	    /*while (ofx0 < 0) ofx0 += 176*2;
	    while (ofx1 < 0) ofx1 += 176*2;
		while (ofx0 >= 176*2) ofx0 -= 176*2;
	    while (ofx1 >= 176*2) ofx1 -= 176*2;*/
		
		for(int i = 0;i < den.NUMPUNTOS;i+=2)
		{			
			int src = begin+(i/2);	
			int desfasez = 0;
			//TODO: VAYA MIERDA
			if (src >= vuelta) 
			{
				src = src-vuelta;		
				desfasez = vuelta*250;
			}			
			int ni = (((i/2)+1)*25)-(modpos/10);		
			int ni2 = ni;		
			
			den.punt[i].x = tramo[src].pos.x-anchura;
			den.punt[i].x += ((tramo[src].curva*ni*ni)/50000);			
			den.punt[i].y = tramo[src].pos.y;// + tramo[begin+(i/2)].altura ;
			den.punt[i].y += ((tramo[src].altura*ni2*ni2)/100000);						
			den.punt[i].z = tramo[src].pos.z-modpos+desfasez;
			
			den.punt[i+1].x = tramo[src].pos.x+anchura;			
			den.punt[i+1].x += ((tramo[src].curva*ni*ni)/50000);
			den.punt[i+1].y = tramo[src].pos.y;// + tramo[begin+(i/2)].altura ;
			den.punt[i+1].y += ((tramo[src].altura*ni2*ni2)/100000);					
			den.punt[i+1].z = tramo[src].pos.z-modpos+desfasez;
									
			den.tramo[(den.NUMPUNTOS-i)/2-1].extra = tramo[src].extra;
			den.tramo[(den.NUMPUNTOS-i)/2-1].pextra = tramo[src].pextra;
			den.tramo[(den.NUMPUNTOS-i)/2-1].jextra = tramo[src].jextra;
			den.tramo[(den.NUMPUNTOS-i)/2-1].pos.x = (den.punt[i].x+den.punt[i+1].x)/2;
			den.tramo[(den.NUMPUNTOS-i)/2-1].pos.y = den.punt[i].y;
			den.tramo[(den.NUMPUNTOS-i)/2-1].pos.z = tramo[src].pos.z;
			den.tramo[(den.NUMPUNTOS-i)/2-1].curva = tramo[src].curva;
			den.tramo[(den.NUMPUNTOS-i)/2-1].clado = tramo[src].clado;
			//extra A LOS LADOS
			if (tramo[src].extra > 0)
			{	
				tramo[src].pextra[0].x = den.punt[i].x-70;
				tramo[src].pextra[0].y = den.punt[i].y;
				tramo[src].pextra[0].z = den.punt[i].z;
				tramo[src].pextra[1].x = den.punt[i+1].x+70;
				tramo[src].pextra[1].y = den.punt[i+1].y;
				tramo[src].pextra[1].z = den.punt[i+1].z;
			}
			//EXTRA ENMEDIO
			if (tramo[src].extra == -2 || tramo[src].extra == -4 || tramo[src].extra == -5)
			{	
				tramo[src].pextra[0].x = tramo[src].rand%50;
				tramo[src].pextra[0].y = den.punt[i].y;
				tramo[src].pextra[0].z = den.punt[i].z;
			}	
			//extra ARRIBA
			if (tramo[src].extra == -1)
			{	
				//ABAJO PUENTE
				tramo[src].pextra[1+4].x = 0;
				tramo[src].pextra[1+4].y = -den.punt[i].y;
				tramo[src].pextra[1+4].z = den.punt[i].z-125;
				//ARRIBA PUENTE
				tramo[src].pextra[2+4].x = 0;
				tramo[src].pextra[2+4].y = -den.punt[i+1].y*2;
				tramo[src].pextra[2+4].z = den.punt[i+1].z-125;
				//BAJO PUENTE
				tramo[src].pextra[0+4].x = 0;
				tramo[src].pextra[0+4].y = -den.punt[i+1].y;
				tramo[src].pextra[0+4].z = den.punt[i+1].z+200-125;
				//COLUMNA +x
				////P1
				tramo[src].pextra[0+2].x = den.punt[i+1].x+140;
				tramo[src].pextra[0+2].y = -den.punt[i+1].y-10;
				tramo[src].pextra[0+2].z = den.punt[i+1].z-125;
				////P4
				tramo[src].pextra[1+2].x = den.punt[i+1].x+70;
				tramo[src].pextra[1+2].y = den.punt[i+1].y;
				tramo[src].pextra[1+2].z = den.punt[i+1].z-125;								
				////P5				
				////P6				
				//COLUMNA +x
				////P1
				tramo[src].pextra[0].x = den.punt[i].x-70;
				tramo[src].pextra[0].y = -den.punt[i].y-10;
				tramo[src].pextra[0].z = den.punt[i].z-125;
				////P4
				tramo[src].pextra[1].x = den.punt[i].x-140-50;
				tramo[src].pextra[1].y = den.punt[i].y;
				tramo[src].pextra[1].z = den.punt[i].z-125;								
				
				
			}
		}		
		/*for(int i = 0;i < den.NUMPUNTOS;i+=2)
		{
			if (tramo[begin+(i/2)].extra != 0)
			{	
				den.punt[den.CARPUNTOS+i].x = den.punt[i].x-Math.abs(rnd.nextInt()%150);
				den.punt[den.CARPUNTOS+i].y = den.punt[i].y;
				den.punt[den.CARPUNTOS+i].z = den.punt[i].z;
				//den.NUMPUNTOS++;								
			}
		}*/
	}
	
	final int bskin[] = {0,1,1,3,2,3,2,1};
	final int fskin[] = {0,1,2,3,3,1,2,2};
	final int tiempo[] = {VIENTO,SOL,SOL,VIENTO,LLUVIA,SOL,NIEVE,TORMENTA};
	
	public void EscogeCircuito(int c)
	{			
	    // longitud
	    int clong[] = {451,500,378,444, 467, 444, 382, 497};
	    // num vueltas
	    int cnumv[] = {3,3,4,3,3,4,3,3};	
	    //CARRETERA
	    int cielos[][] = {{0, 168, 255} ,{0, 168, 255} ,{0, 168, 255} ,{0, 168, 255} ,{0, 168, 255} ,{0, 168, 255} ,{0, 168, 255} ,{0, 168, 255} };
		int ccol[][] =   {{160,160,160},{200,200,200},{180,180,220},{210,150,150}, {160,160,160}, {140,140,160}, {150,150,150}, {140,140,160}};
		//CESPED
		int cocol[][] =  {{168,151,121}, {140,235,99}, {120,210,80}, {150,175,49} , {0,  155  ,0}, {120,210, 70}, {110,220,90}, {110,220,90} };	
		//LADOS
		int cocol1[][] = {{255,255,255},{255,255,255},{255,255,0},  {255,255,255}, {200,200,200}, {255,255,  0}, {255,255,255}, {255,255,255} };
		int cocol2[][] = {{0,0,200},    {255,0,0},    {0,0,200},    {255,0,0}    , {255,  0,  0}, {255,  0,  0}, {0,0,0} , {0,0,210}};
		long semilla[] = {17, 121, 443, 47, 7987, 3689, 9745, 957};
		byte atre[] = {3, 2, 2, 3, 2, 2, 2, 2};
		
		boolean r = false;
		current = c;   	    
   	    
		if (c == 8)		
		{ 
		    r = true;
		    rnd = new Random();
		    c = Math.abs(rnd.nextInt()%8);
		}
		Game.fondo[0] = Game.loadImg("/back"+bskin[c]);
		Game.fondo[1] = Game.loadImg("/fore"+fskin[c]);   	
		
		
	    
   	    name = Lang.circuitos[c];
   	    if (r) name = Lang.circuitos[8];
   	    
   	    //if (Moto.climate)
   	    //{ clima = tiempo[c];
   	    clima = SOL;   	    
   	    if (Game.modojuego == Game.CAMPEONATO)
   	    {
   	        if (Moto.gpclimate) clima = tiempo[c];   	       	     
   	    }
   	    else
   	    {
   	        if (Moto.climate) clima = tiempo[c];   	       	     
   	    }
   	    
   	    
   	       	    
   	    if (r && Moto.climate) clima = Math.abs(rnd.nextInt()%5);   	    
   	    
   	    if (clima == LLUVIA || clima == TORMENTA)
   	        Game.zimzum = Game.loadImg("/zimzum2");   	
       	else Game.zimzum = Game.loadImg("/zimzum1");   	
   	    
		Copy(col, ccol[c]);
		Copy(ocol,cocol[c]);
		Copy(ocol1, cocol1[c]);
		Copy(ocol2, cocol2[c]);
		Copy(cielo, cielos[c]);
		if (clima == VIENTO) wind = 2;
		else wind = 0;
		
    	if (r) c = Math.abs(rnd.nextInt()%8);
    	
    	vuelta = clong[c];
		vueltas = cnumv[c];   	
		atrezzo = atre[c];	
		
		if (r) Make(rnd.nextInt());
		else Make(semilla[c]);
		
	}

	
}