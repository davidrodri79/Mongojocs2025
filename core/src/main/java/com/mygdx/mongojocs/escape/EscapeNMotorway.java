package com.mygdx.mongojocs.escape;


//import com.nokia.mid.ui.*;
import com.mygdx.mongojocs.midletemu.Graphics;


import java.io.InputStream;
import java.util.Random;



public class EscapeNMotorway extends EscapeNScene {
	
	
	EscapeNMWDummy du[];			
	int Roofx[], RoofType[], nRoofs;
		
	static int DUMMY_CARS=4;
		
								
	public EscapeNMotorway(int dif)
	{
		
		InputStream is;
		String st;
						
		segment_types=10; nsegs=10; 				
										
		SegmentMap=new byte[300];
		SegmentMapX=new int[300];
		SegmentSize=new int[segment_types];
								
		SegmentSize[0]=18;
		SegmentSize[1]=25;
		SegmentSize[2]=25;
		SegmentSize[3]=25;
		SegmentSize[4]=25;
		SegmentSize[5]=23;
		SegmentSize[6]=25;
		SegmentSize[7]=25;
		SegmentSize[8]=22;		
		SegmentSize[9]=18;
															
		basic_create(1,51);
									
		// Positions of roof sprites
		
		Roofx=new int[30];
		RoofType=new int[30];
		nRoofs=0;
													
		map_design(dif);								
				
		// Create full map				
		en=new EscapeNEnemy[30]; nenem=0;
						
		for(int i=0; i<nsegs; i++){
			
			if((i%2==0) && (Math.abs(rnd.nextInt())%3==0) && (SegmentMap[i]==1)){								
				Roofx[nRoofs]=(psizex*8)+1;
				RoofType[nRoofs]=Math.abs(rnd.nextInt())%2;
				nRoofs++;	
				}			
			add_segment(SegmentMap[i]);						
			
			// Enemies
			if((SegmentMap[i]==1) && (i%7==0)) {en[nenem]=new EscapeNEnemy((psizex*8)-30,20+(Math.abs(rnd.nextInt())%56),2); nenem++;}
			if((SegmentMap[i]==10) && (i%4==0)) {en[nenem]=new EscapeNEnemy((psizex*8)-30,40,2); nenem++;}
		}
		
		dummy_limitx1=SegmentMapX[nsegs-8-dif];		
		dummy_limitx2=SegmentMapX[nsegs-5-dif];
																																			
		// Dummy cars
		du=new EscapeNMWDummy[DUMMY_CARS];
		for(int i=0; i<DUMMY_CARS; i++)
			du[i]=new EscapeNMWDummy(50*i,20+(10*i));
											
		reset();

	}
	
	private void map_design(int d)
	{
		int t;
		
		nsegs=0; 
				
		for(int i=0; i<d+1; i++){		
			
			// Road
			for(int j=0; j<10+(4*(d+1)); j++)
				add_piece(1);		
			
			// Paying zone
			add_piece(7);
			add_piece(8);
			add_piece(9);			
		}
		
		for(int i=0; i<10; i++)
			add_piece(1);		
				
		// Highway exit		
		add_piece(2);		
		add_piece(3);
		add_piece(4);
		add_piece(5);
					
		for(int i=0; i<5+d; i++)
			add_piece(10);		
				
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
				//if(i<j)
				if(Math.abs(du[i].x-en[j].x)<150)
					en[j].car_collide(this,du[i]);	
				
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
										
		if((i<nRoofs) && (Roofx[i]-scex>-64) && (Roofx[i]-scex<EscapeNCanvas.canvasx))
			if(RoofType[i]==0){
				
				g.setClip(Roofx[i]-scex,0,56,EscapeNCanvas.canvasy);
				g.drawImage(SpriteImg,Roofx[i]-scex,-scey+1,20);		
				
			}else{
				g.setClip(Roofx[i]-scex,0,56,EscapeNCanvas.canvasy);
				g.drawImage(SpriteImg,Roofx[i]-scex-56,-scey+1,20);		
					
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
		
		CheckArray[5]=1; 
		for(int i=48; i<=51; i++)
			CheckArray[i]=1; 
			
		for(int i=6; i<=9; i++)
			CheckArray[i]=2; 
		CheckArray[10]=2; CheckArray[13]=2; CheckArray[16]=2;  			
		for(int i=36; i<=39; i++)
			CheckArray[i]=2; 
		for(int i=44; i<=47; i++)
			CheckArray[i]=2; 		
		CheckArray[52]=2; 
									
	}
}