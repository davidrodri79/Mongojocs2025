package com.mygdx.mongojocs.escape;

//import com.nokia.mid.ui.*;
import com.mygdx.mongojocs.midletemu.Graphics;


import java.io.InputStream;
import java.util.Random;


public class EscapeNDesert extends EscapeNScene {
	
								
	public EscapeNDesert(int dif)
	{
											
		segment_types=10;	
		
		nsegs=10; 		
				
		SegmentMap=new byte[300];
		SegmentMapX=new int[300];
		SegmentSize=new int[segment_types];
																															
		// Load the segments
		
		SegmentSize[0]=20;
		SegmentSize[1]=30;		
		SegmentSize[2]=30;
		SegmentSize[3]=30;
		SegmentSize[4]=30;
		SegmentSize[5]=30;
		SegmentSize[6]=15;
		SegmentSize[7]=30;
		SegmentSize[8]=10;
		SegmentSize[9]=20;
												
		basic_create(2,75);								

		
		// Dessign the random map!
		
		map_design(dif);
				
		// Create the map
			
		nenem=0;			
		
		// Enemies
		en=new EscapeNEnemy[30];
										
		for(int i=0; i<nsegs; i++){
			add_segment(SegmentMap[i]);
			if((SegmentMap[i]==1) && (i%2==0)) {en[nenem]=new EscapeNEnemy((psizex*8)-40,20,0); nenem++;}
			if((SegmentMap[i]==9) && (i%4==0)) {en[nenem]=new EscapeNEnemy((psizex*8)-20,10,1); nenem++;}
			if((SegmentMap[i]==7) && (i%4==0)) {en[nenem]=new EscapeNEnemy((psizex*8)-20,80,1); nenem++;}
		}
		
		
		reset();
																														
	}
	
	private void map_design(int d)
	{
		int t;
		
		nsegs=0; 
						
		add_piece(1);		
		
		for(int i=0; i<3+(d+1)*2; i++){
			
			t=Math.abs(rnd.nextInt())%2;
						
			if(t==0){
				// Bridge, corners				
								
				add_piece(1);
				add_piece(2);
				add_piece(3);
				add_piece(4);
								
			}else{
				// Two way bifurcation
				
					
				t=Math.abs(rnd.nextInt())%(5*(d+1));
										
				add_piece(5);	
				for(int k=0; k<(4*(d+1))-t; k++)
					add_piece(9);	
				add_piece(6);	
				for(int k=0; k<t; k++)
					add_piece(7);											
				add_piece(8);							
			}			
		}
		// End, Mexico's announce
		add_piece(10);		
				
	}
	
	public void reset()
	{
		show_index=0;
		
		reset_bkg();
		
		pl.reset(this);
		
		for(int i=0; i<nenem; i++)
			en[i].reset(this);					
	}	
	
	public void update(Control ctrl)
	{
		basic_update(ctrl);
	}
			
	public void show(Graphics g)
	{
		
		basic_show(g);		
		
		for(int i=0; i<nenem; i++)
			en[i].show(this,g);
			
		pl.show(this,g);
				
	}
	
	public int total_time()
	{
		return (psizex/12);		
	}
		
	public void create_check_array()
	{
					
		CheckArray=new byte[128];
		
		for(int i=0; i<128; i++) CheckArray[i]=1;
		
 
		//	Colisio: 3,22,23,32,33,34,35,70,71,72,73
		//	Cami:(9<>21)(24<>31)(41<>63
		//	Aigua2,65,66,67,69,   )per laigua haig de fer un frame del coche enfonsat.		
		
		CheckArray[3]=2; 
		CheckArray[22]=2;
		CheckArray[23]=2; 		
		for(int i=32; i<=35; i++)
			CheckArray[i]=2; 
		for(int i=70; i<=73; i++)
			CheckArray[i]=2; 
			
		for(int i=9; i<=21; i++)
			CheckArray[i]=0; 
		for(int i=24; i<=31; i++)
			CheckArray[i]=0; 
		for(int i=41; i<=63; i++)
			CheckArray[i]=0; 
			
		for(int i=65; i<=67; i++)
			CheckArray[i]=3; 		
		CheckArray[69]=3; 
			
																
	}
}