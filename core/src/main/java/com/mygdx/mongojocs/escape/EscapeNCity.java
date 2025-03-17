package com.mygdx.mongojocs.escape;

//import com.nokia.mid.ui.*;
import com.mygdx.mongojocs.midletemu.Graphics;


import java.io.InputStream;
import java.util.Random;



public class EscapeNCity extends EscapeNScene {
	

	byte EasyMap[]={1,2,3,4,5,6,7};	
	EscapeNCDummy du[];		
	int Roofx[], nRoofs, Crossx[], nCross;
	
	
	static int DUMMY_CARS=5;
		
								
	public EscapeNCity(int dif)
	{
		
		InputStream is;
		String st;
					
		segment_types=7; nsegs=7; 				
										
		SegmentMap=new byte[300];
		SegmentMapX=new int[100];
		SegmentSize=new int[segment_types];
		
		
		for(int i=0; i<nsegs; i++)
			SegmentMap[i]=EasyMap[i];			
						
		SegmentSize[0]=25;
		SegmentSize[1]=25;
		SegmentSize[2]=25;
		SegmentSize[3]=25;
		SegmentSize[4]=25;
		SegmentSize[5]=25;
		SegmentSize[6]=30;
																	
		// Load tile bitmaps
		
		basic_create(0,121);
													
		// Positions of roof sprites
		
		Roofx=new int[30];
		nRoofs=0;
		
		Crossx=new int[20];
		nCross=0;
												
		map_design(dif);
		
		en=new EscapeNEnemy[30]; nenem=0;
												
		// Create full map		
		
		for(int i=0; i<nsegs; i++){
			
			// Remember the roofs's x
			if(SegmentMap[i]==7)
				if(i%2==0){								
					Roofx[nRoofs]=(psizex*8)+30;					
					nRoofs++;										
				}			
			
			// Remeber the crosses's x
			if(SegmentMap[i]==2){								
				
				Crossx[nCross]=(psizex*8)+(15*8);					
				nCross++;										
			}			
			if(SegmentMap[i]==4){	
											
				Crossx[nCross]=(psizex*8)+(15*8);					
				nCross++;										
			}			
											
			add_segment(SegmentMap[i]);

			// Enemies			
			if(((SegmentMap[i]==1) || (SegmentMap[i]==3)) && (i%7==0)) {en[nenem]=new EscapeNEnemy((psizex*8)-40,80,2); nenem++;}
			if((SegmentMap[i]==5) && (i%5==0)) {en[nenem]=new EscapeNEnemy((psizex*8)-40,20+40*Math.abs(rnd.nextInt()%2),2); nenem++;}
		}	
																									
		// Dummy cars
		du=new EscapeNCDummy[DUMMY_CARS];
		
		du[0]=new EscapeNCDummy(0,70,0);
		du[1]=new EscapeNCDummy(90,70,0);
		du[2]=new EscapeNCDummy(80,35,1);
		du[3]=new EscapeNCDummy(170,35,1);
		du[4]=new EscapeNCDummy(Crossx[0],40,2);
		
		reset();
						
	}

	private void map_design(int d)
	{
		int t;
		
		nsegs=0; 
												
		add_piece(1);
								
		for(int i=0; i<4*(d+1); i++){
			
			switch(Math.abs(rnd.nextInt())%4){

				default : for(int j=0; j<(8-2*d); j++) add_piece(1); break;
				case 1 : add_piece(2); break;
				case 2 : add_piece(3); add_piece(4); break;								
			}				
			
		}	
		add_piece(5);
		add_piece(6);
		
		for(int i=0; i<6*(d+1); i++)
			add_piece(7);
	}	
	
	public void reset()
	{
		show_index=0;		
		
		reset_bkg();
		
		pl.reset(this);
		
		for(int i=0; i<nenem; i++)
			en[i].reset(this);

		for(int i=0; i<DUMMY_CARS; i++)
			du[i].reset(this);
					
	}
	
	public void update(Control ctrl)
	{
		basic_update(ctrl);
					
		// Collide player-dummies			
		for(int i=0; i<DUMMY_CARS; i++){
			du[i].ia_update(pl,this);		
			pl.car_collide(this,du[i]);	
		}
		
		//Collide dummies-dummies		
		for(int i=0; i<DUMMY_CARS; i++)
			for(int j=0; j<DUMMY_CARS; j++)
				if(i<j)
				if(Math.abs(du[i].x-du[j].x)<150)
					du[i].car_collide(this,du[j]);	
					
		//Collide enemy-dummies
		for(int i=0; i<DUMMY_CARS; i++)
			for(int j=0; j<nenem; j++)				
				if(Math.abs(du[i].x-en[j].x)<150)
					en[j].car_collide(this,du[i]);


		// Place the vertical cars(s) at the nearest cross
		int i=0;
		while((i<nCross) && (Math.abs(scex+90-Crossx[i])>100))
			i++;
			
		if((Math.abs(scex+90-Crossx[i+1])<Math.abs(scex+90-Crossx[i])) && (i<nCross-1))
			i=i+1;
						
		if((i<nCross) && (Crossx[i]-scex>-120) && (Crossx[i]-scex<176)){
			
			for(int j=0; j<DUMMY_CARS; j++)
				if(du[j].mode==2) { 
					du[j].orix=Crossx[i]; 
					du[j].x=du[j].orix;
					du[j].realx=du[j].orix*100;					
					du[j].place_index(this);
				}			
			}		
								
	}
			
	public void show(Graphics g)
	{

		basic_show(g);				
		
		for(int i=0; i<nenem; i++)
			en[i].show(this,g);
			
		for(int i=0; i<DUMMY_CARS; i++)
			du[i].show(this,g);
						
		pl.show(this,g);
		
		// Show the nearest roof		
		
					
		int i=0;
		while((i<nRoofs-1) && (Math.abs(scex+90-Roofx[i+1])<Math.abs(scex+90-Roofx[i])))
			i++;			
						
		if((i<nRoofs) && (Roofx[i]-scex>-120) && (Roofx[i]-scex<EscapeNCanvas.canvasx)){
			
			g.setClip(0,0,EscapeNCanvas.canvasx,EscapeNCanvas.canvasx);
			g.drawImage(SpriteImg,Roofx[i]-scex-1,-scey-96,20);		
		}	
																
	}
	
	public int total_time()
	{
		return (psizex/16);		
	}
		
	public void create_check_array()
	{
					
		CheckArray=new byte[128];
		
		for(int i=0; i<128; i++) CheckArray[i]=0;
		
		//8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,29,30,31,39,40,41,42,43,44,45,
		//46,47,48,49,50,51,52,53,54,56,57,58,60,61,62,73,78,79,80,81,82,83,86,87,89,90,91,92,93,		
		
		//(7-22),28,29,30,(38-53),55,56,57,59,60,61,72,80,81,82,85,86,90,91,98

		for(int i=7; i<=22; i++)
			CheckArray[i]=2; 
			
		for(int i=28; i<=30; i++)
			CheckArray[i]=2; 
			
		for(int i=38; i<=53; i++)
			CheckArray[i]=2; 
			
		for(int i=55; i<=57; i++)
			CheckArray[i]=2; 
						
		for(int i=59; i<=61; i++)
			CheckArray[i]=2; 
			
		CheckArray[72]=2; 					
		
		for(int i=80; i<=82; i++)
			CheckArray[i]=2; 
			
		CheckArray[85]=2; CheckArray[86]=2; 					 								
		
		for(int i=90; i<=91; i++)
			CheckArray[i]=2;
		
		CheckArray[98]=2;
											
	}
}