package com.mygdx.mongojocs.frozenworld;


import com.mygdx.mongojocs.midletemu.Image;

public class prota extends unit
{
    int pipo;
    int sfire[] = {13, 14};
    //int jumpSeq[] = {0,0,3,3,3,2,2,1,1,1,0,-1,-1,-1,-2,-2,-3};
    //int sjump[] = {7,7,7,8,8,8,10,10,10,10,9,9,9,9,9};
    int jumpSeq[] = {0,0,4,4,    3,2,2,          1,0,-1,   -1,-2,-2,-3,-3};
    int sjump[] =      {7,7,8,8,    8,10,10,10,   9,9,9,   9,9};    
    int jump = -1;
    int swalk[] = {1,2,3};
    int anim;
    int lastCell;
    int colorCell;
    int nshots;
    unit shot;
    int jsidex;
    int inmunity;
    
    prota(GameCanvas _gc, Image i, int w, int h, int numf, int _offx, int _offy, byte[] cor)
    {
        super(_gc, i, w, h, numf, _offx, _offy, cor);
        setCollisionRectangle(8,12,8,12);
        sidex = 1;
	//shot = new unit(_gc, i, 8, 10, numf, 0, 0, cor);
	shot = new unit(_gc, i, w, h,numf, 0, 0, cor);
	shot.setCollisionRectangle(8,12,8,8);
    }
    
    boolean jumping;
    int faseacabada;
    
    public boolean Move(int addx, int addy, int befy)
    {   
		//FIN DE FASE???
		 if (this.gc.ga.nbatteryes == 0 && TestCol(x+cix, x+cfx-1, y+ciy, y+cfy-1, gc.REACTOR)) {faseacabada++;inmunity = 10;return true;}
		
		timing++;        
		int fally = 0; 
	
		//ESTOY EN EL AIRE???               
		if (Test(x+cix, y+cfy, gc.LST_AIR) &&
		     Test(x+cfx-1, y+cfy, gc.LST_AIR) || jumping)
		{     
			    if (addx == 0 && jump > 7 && jump < 12) addx = jsidex;
			    grounded = false;
			    if (jump != -1 && jump < jumpSeq.length) 
			    {
			        if (jump == 2) gc.soundPlay(10,1);
					fally = -jumpSeq[jump];					
					if (fally != 0) jumping = false;
					jump++;                           
			    }
			    else {addy = 0;jump = -1;y += 4;}
		}
		else 
		{
			//TOCO EL SUELO
			y = (((y+cfy) / TILED) * TILED)-cfy;
			jump = -1;
			grounded = true;
		}                       
		
	
		// sidex vale 1 o -1
		if (addx != 0) sidex = addx / Math.abs(addx);
		if (addy != 0) sidey = addy / Math.abs(addy);
		//		
		int cx = addx + x;
		int cy = addy + y;        
		
	       
		if (TestCol(cx+cix-addx, cx+cfx-1-addx, cy+ciy, cy+cfy-1, gc.LST_AIR))
		{             
		    if (addy < 0 && jump == -1 && befy == 0) {jump = 0;jumping = true;jsidex = addx;		    
		    if (jsidex != 0) jsidex = jsidex/Math.abs(jsidex);		    
		    }
		    else y += fally;
		}        
		if (TestCol(cx+cix, cx+cfx-1,cy+ciy-addy, cy+cfy-1-addy, gc.LST_AIR))
		    x += addx;                    
	   
			//CABEZAZO  AL HIELO
			int  _dx = cix;
			if (sidex > 0) _dx = cfx-1; 
			if (grounded && befy == 0 && addy < 0 && Test(x+(width>>1), y-8, gc.LST_ICE)) 
			{
				Fire((width>>1) - _dx - sidex*TILED, -1);
				//jump = -1;jumping = false;
				jump = 13;
				return true;
			} 
			//CABEZAZO  A LA PARED	
			 if (!grounded  && !Test(x+(width>>1), y+ciy-1+addy, gc.LST_AIR))
			 {
				if (jump < 13)  jump = 13;			 
			 }
			 //CABEZAZO END
	       
	       
	       //Grafico
		if (grounded && addx == 0) 
		{
			if (desp == 13) desp = 14;
			else desp = 0;
		}	
		if (grounded && addx != 0)
		{
		    anim = anim%swalk.length;
		    desp = swalk[anim];
		    if (timing%2 == 0) anim++;            
		}
		if (!grounded && jump != -1 && jump < sjump.length) 
		    desp = sjump[jump];
		if (!grounded && jump == -1) desp = 9;
		
		if (addx < 0) ashape = 5;
		if (addx > 0) ashape = 0;
		return true;                
    }
    
    public void fireShot()
    {        
	    if (nshots == 0 || shot.active) return;
	    gc.soundPlay(6,1);
	    nshots--;
	    int _x = x;
	    if (sidex == 1) _x += 10;
	    else _x -= 3; 	    		    		    	   
	    shot.Activate(map, _x, y+2);
	    shot.incx = sidex*400;
	    shot.ashape = 8;
	    if (sidex == 1) shot.ashape = 3;	    
	    shot.desp = 0;
    }      
       
       
       
    public boolean FireDown()
    {        
	if (sidex == 1) Fire(-6,1);
        else Fire(7,1);       
        return true;
    }    
        
   public void fireFor()
   {
	   if (sidex > 0)
	   {	   
	   	if (testFire(-sidex*2) != -1 && testFire(-sidex*2)  < testFire(0)) x -= sidex*2;
		else if (testFire(-sidex*4) != -1 && testFire(-sidex*4)  < testFire(0)) x -= sidex*4;				 
	   }
	   else
	   {
		   if (testFire(-sidex*2) != -1 && testFire(-sidex*2) > testFire(0)) x -= sidex*2;
		   else  if (testFire(-sidex*4) != -1 && testFire(-sidex*4) > testFire(0)) x -= sidex*4;
	   }
	   Fire(0,0);	   
   }	
	

    public int testFire(int _dx)	
    {
	    int cx = cix;
	    if (sidex > 0) cx = cfx-1;
	    int p = (x+cx+_dx) / TILED;
	    p += ((y+8)/ TILED) * gc.MAPW;
	    p += sidex;
	    int current = map[p];
            if(gc.inList(gc.AIR,current))
		    return p;
	    else return -1;
    }
	
    public boolean Fire(int _dx, int down)
    {        	    
	    boolean retvalue = false;
	    //if (sidex == 0) return false;
        int cx = cix;
        if (sidex > 0) cx = cfx-1;
        
        int p = (x+cx+_dx) / TILED;
        p += ((y+8+(down*16))/ TILED) * gc.MAPW;
        p += sidex;
         
        int current = map[p];
        if (gc.inList(gc.LST_ICE, current)) {map[p] = (byte)gc.BEGDESTROY;gc.soundPlay(2,1);	}
        else if(gc.inList(gc.AIR,current)) {map[p] = (byte)gc.BEGCREATE; retvalue = true;gc.soundPlay(3,1);	}
       
        desp = 13;
        return retvalue;
    }
       
   
   
    public void myPos()
    {	    
        pipo = (++pipo)%gc.LST_ICE.length;
	if (inmunity > 0) inmunity--;
	if (shot.active)
	{
		if (shot.estado == 0)
		{
			shot.Update(null);			
			shot.desp = 1 + ((timing>>1)%4);
			for(int i = 0; i < gc.ga.malo.length;i++)		
				if (gc.ga.malo[i].active && (shot.isCollidingWith(gc.ga.malo[i]) || gc.ga.malo[i].isCollidingWith(shot)))
				{
					//shot.Deactivate();
					shot.estado = 1;
					shot.timing = 0;
					shot.x = gc.ga.malo[i].x + (gc.ga.malo[i].width>>1);
					shot.y = gc.ga.malo[i].y + (gc.ga.malo[i].height>>1);
					gc.ga.malo[i].tag--;
					gc.soundPlay(8,1);
					if (gc.ga.malo[i].tag <= 0) gc.ga.malo[i].Deactivate();				
				}		
		    if (shot.timing > 40) shot.active = false;
			if (shot.estado == 0) if (!shot.TestCol(shot.x+shot.cix, shot.x+shot.cfx, shot.y+shot.ciy, shot.y+shot.cfy, gc.LST_AIR)) shot.Deactivate();			    
		}
		else
			if (++shot.timing > 10) shot.active = false; 
		
	}
        if (Test(x+(width>>1), y+8, gc.BATTERY)) 
	{
		SetAir(x+(width>>1), y+8);
		gc.ga.nbatteryes--;
		lastCell++;
		colorCell = 0; 
		gc.soundPlay(4,1);	
	}
	if (Test(x+(width>>1), y+8, gc.ENERGY)) 
	{
		if (tag < 3)
		{
			SetAir(x+(width>>1), y+8);
			tag++;
			lastCell++;
			colorCell = 2;
			gc.soundPlay(4,1);	
		}
	}	
	if (Test(x+(width>>1), y+8, gc.LST_GEISER)) 
	{			
		Mata2();				 
	}		
	if (Test(x+(width>>1), y+8, gc.SHOT)) 
	{
		SetAir(x+(width>>1), y+8);
		if (nshots < 9) nshots++;
		lastCell++;
		colorCell = 1;
		gc.soundPlay(4,1);	
	}	
   }
   
   public void Mata()
   {
       if (inmunity > 0 || tag == 0) return;
	    if (gc.ga.cheatActive) return;	   
	   tag--;
	   inmunity = 70;
	   gc.soundPlay(7,1);	
	   gc.vibraInit(300);
   }

    public void Mata2()
   {       
	   if (inmunity > 0 || tag == 0) return;
	   if (gc.ga.cheatActive) return;
	   tag--;
	   if (tag > 0) tag--;
	   inmunity = 70;
	   gc.soundPlay(7,1);
	   gc.vibraInit(300);
   }

   
}