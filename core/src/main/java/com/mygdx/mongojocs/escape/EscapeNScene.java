package com.mygdx.mongojocs.escape;


//import com.nokia.mid.ui.*;
import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;


import java.io.InputStream;
import java.util.Random;


abstract class EscapeNScene {
	

	public static int sizey=12;

	static Image SpriteImg;
	public Image Background, TileImg;	
	byte MapPieces[][];
	byte SegmentMap[];
	byte CheckArray[];
	int SegmentSize[];
	public int SegmentMapX[], nsegs;
	public EscapeNPlayer pl;
	EscapeNEnemy  en[];
	protected int segment_types, nsegsadded=0, nenem;
	public int scex, scey, winx=-36, dummy_limitx1=1000000, dummy_limitx2=1000000, show_index, tileoffset, tx, ty;
	public static Random rnd;
	Graphics bggr;

	
	public int psizex=0;
	
											
	abstract void reset();										
													
	abstract void show(Graphics g);
		
	abstract void update(Control ctrl);
	
	abstract int total_time();
			
	public static void load_sprites()
	{
				
		// Load the sprites only once		
		try {
			SpriteImg = new Image();
			SpriteImg._createImage("/Misc.png");
			//SpriteImg = Image.createImage(8,8); 
			
		}catch(Exception err) {}
	}	
	
	protected void add_piece(int p)
	{
		SegmentMap[nsegs]=(byte)p; nsegs++;	
	}
	
	protected void reset_bkg()
	{
		int i, dx, t;
		i=0; scex=0; show_index=0;
		dx=(scex-SegmentMapX[i])/8;
		
		for(int x=0; x<tx; x++){
			
			// Draw column
			for(int y=0; y<ty; y++){
				t=MapPieces[SegmentMap[i]-1][(SegmentSize[SegmentMap[i]-1]*y)+dx];
				bggr.setClip(x<<3,y<<3,8,8);					
				bggr.drawImage(TileImg,(x<<3)-((t%tileoffset)<<3),(y<<3)-((t/tileoffset)<<3),20);						
				}
			dx++;
			if(dx>=SegmentSize[SegmentMap[i]-1]) {dx=0; i++;}
			if(i>=nsegs) i=nsegs-1;			
		}				
	}
	
	protected void basic_create(int l, int ntiles)
	{
		InputStream is;
		String st;		
				
		// Load tile bitmaps		
		try {
			TileImg = new Image();
			TileImg._createImage("/Stage"+Integer.toString(l)+"Tiles.png");
			//TileImg = Image.createImage(8,8); 
		}catch(Exception err) {}
		
		// Load map pieces		
		
		MapPieces=new byte[segment_types][];
				
		for(int i=0; i<segment_types; i++){			
			MapPieces[i]=new byte[SegmentSize[i]*sizey];
			st="/st"+Integer.toString(l)+"_"+String.valueOf(i+1)+".bin";
			is=getClass().getResourceAsStream(st);
			readingdata.leerdata(st,MapPieces[i],SegmentSize[i]*sizey);
		};		
		
		if(l==0) tileoffset=31; else tileoffset=32;
		
		tx=(EscapeNCanvas.canvasx/8)+1; ty=12;
		
		create_backgrounds();
		
		create_check_array();
						
		// Player
		pl=new EscapeNPlayer();	
	}
					
	public void show_stats(Graphics g, int lifes, int energy, int crono)
	{
		// Remaining lifes				
		int iy=EscapeNCanvas.reliny;
		
		g.setClip(0,iy,9,9);
		g.drawImage(EscapeNCar.SpriteImg,-125,iy-96,20);		
		
		g.setClip(0,iy+5,9,9);
		g.drawImage(EscapeNCar.SpriteImg,-125,iy+5-88,20);		

		g.setClip(0,iy+10,9,9);
		g.drawImage(EscapeNCar.SpriteImg,-125,iy+10-(8*lifes),20);		

		// Bar
		g.setClip(2,iy+15,5,33);
		g.drawImage(EscapeNCar.SpriteImg,-127,iy+15-105,20);		
				
		g.setClip(2,iy+17,4,1+(energy/20));
		g.drawImage(EscapeNCar.SpriteImg,-123,iy+15-105,20);		
				
		// Crono
		g.setClip(0,iy+46,9,9);
		g.drawImage(EscapeNCar.SpriteImg,-125,iy+46-((crono/60)*8),20);		
		
		g.setClip(0,iy+49,9,9);
		g.drawImage(EscapeNCar.SpriteImg,-125,iy+49-80,20);		
		
		g.setClip(0,iy+54,9,9);
		g.drawImage(EscapeNCar.SpriteImg,-125,iy+54-(((crono%60)/10)*8),20);		
		
		g.setClip(0,iy+59,9,9);
		g.drawImage(EscapeNCar.SpriteImg,-125,iy+59-((crono%10)*8),20);				
	}
		
	protected void add_segment(int s)
	{
		s=s-1;
					
		psizex+=SegmentSize[s];

		// Remember the x's where begins each segment				
		nsegsadded++;
		SegmentMapX[nsegsadded]=SegmentMapX[nsegsadded-1]+(SegmentSize[s]*8);				
				
	}
		
	protected void basic_update(Control ctrl)
	{
				
		// Player update
		
		pl.update(ctrl,this);
		
		// Position of the following "camera"
				
		if((pl.velx>=0) && (winx<-36)) winx+=2;
		if((pl.velx<0) && (winx>-76)) winx-=2;
		
		
		if(EscapeNCanvas.canvasy<96){
			scey=pl.y-21-11;
			if(scey<0) scey=0; if (scey>31) scey=31;
		}else scey=-(EscapeNCanvas.canvasy-96)/2;
		
		scex=pl.x+winx;
		if(scex<0) scex=0; if (scex>(psizex*8)-EscapeNCanvas.canvasx) scex=(psizex*8)-EscapeNCanvas.canvasx;
		
		// Enemy update & player collide
		
		for(int i=0; i<nenem; i++){
			en[i].ia_update(pl,this);		
			pl.car_collide(this,en[i]);	
		}
		
		//Collide between enemies
		
		for(int i=0; i<nenem; i++)
			for(int j=0; j<nenem; j++)
				if(i<j)
				if(Math.abs(en[i].x-en[j].x)<150)
					en[i].car_collide(this,en[j]);								
									
	}
	
	protected void create_backgrounds()
	{
		byte t;

		Background=new Image();
		Background._createImage(tx*8,ty*8);
		bggr=Background.getGraphics();
	}
	
	protected void basic_show(Graphics g)
	{	
												
		int i, j=0, x, x2, t, dx=0, inx, offset;				
		
		// The updated column of tiles depends on the direction of the movement
		if(pl.velx+pl.velcx>=0) inx=(tx-2)*8;
		else inx=0;	
						
		if((scex+inx>=SegmentMapX[show_index+1]) && (show_index<nsegs-1)) show_index++;
		if((scex+inx<SegmentMapX[show_index]) && (show_index>0)) show_index--;
		i=show_index;
				
		// Draw only two column of tiles, which are off screen		
		x2=(((scex+inx)/8))%tx;
		
		dx=((scex-SegmentMapX[i]+inx)/8);
		
		if(dx>=SegmentSize[SegmentMap[i]-1]) {dx=0; i++;}
		if(i>=nsegs) i=nsegs-1;
				
		for(x=x2; x<=x2+1; x++)	{
			//System.out.println("i:"+i+"/ "+((SegmentSize[SegmentMap[i]-1]*0)+dx)+" of "+MapPieces[SegmentMap[i]-1].length/12);
			
			for(int y=0; y<ty; y++){
				offset=((SegmentSize[SegmentMap[i]-1]*y)+dx);
				if(offset<0) offset=0;
				t=MapPieces[SegmentMap[i]-1][offset];
				bggr.setClip(x<<3,y<<3,8,8);					
				bggr.drawImage(TileImg,(x<<3)-((t%tileoffset)<<3),(y<<3)-((t/tileoffset)<<3),20);		
			}
			dx++;
			if(dx>=SegmentSize[SegmentMap[i]-1]) {dx=0; i++;}
			if(i>=nsegs) i=nsegs-1;
		}

		g.setClip(0,0,EscapeNCanvas.canvasx,EscapeNCanvas.canvasy);
		g.drawImage(Background,-(scex%(tx<<3)),-scey,20);
		g.drawImage(Background,-(scex%(tx<<3))+(tx<<3),-scey,20);									

								
	}
	
	abstract void create_check_array();
			
	public byte check_ground(int x, int y, int i)
	{
		return check_tile(x>>3,y>>3, i);	
	}
	
	public byte check_tile(int x, int y, int index)
	{
		byte t;
		int i=0, stype;
		
					
		if(y<0) y=0; if(y>=sizey) y=sizey-1;									
		if(x<0) x=0; if(x>=psizex<<3) x=(psizex<<3)-1;
				
		i=index;
		//if((index<0) || (index>=nsegs)) System.out.println("TE SALES!!!");
		
		// Find which segment is		
		if((x<<3<SegmentMapX[i]) && (i>0)) i--; 
		if((x<<3>=SegmentMapX[i+1]) && (i<nsegs-1)) i++;
		
		stype=SegmentMap[i]-1;
				
		// Check the tile inside the map piece
		t=MapPieces[stype][(y*SegmentSize[stype])+x-(SegmentMapX[i]>>3)];
				
		return CheckArray[t];									
	}
	
}