
package com.mygdx.mongojocs.cerberus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.io.InputStream;
import java.util.Random;

public class CerberusWorld {
	
	public static final int SWAMP = 0;
	public static final int CAVES = 1;
	public static final int FOREST = 2;
	public static final int CASTLE = 3;
	
	public static final int TRITON=CerberusEnemy.TRITON;
	public static final int SCORPION_FLY=CerberusEnemy.SCORPION_FLY;
	public static final int UGLY_BALL=CerberusEnemy.UGLY_BALL;
	public static final int TURTLE_GOLEM=CerberusEnemy.TURTLE_GOLEM;
	public static final int AMONITE=CerberusEnemy.AMONITE;
	public static final int INV_AMONITE=CerberusEnemy.INV_AMONITE;	
	public static final int LAVA_ROCK=CerberusEnemy.LAVA_ROCK;
	public static final int PLATFORM=CerberusEnemy.PLATFORM;
	public static final int BIG_PLATFORM=CerberusEnemy.BIG_PLATFORM;	
	public static final int SKULL_DOG=CerberusEnemy.SKULL_DOG;	
	public static final int GOBLIN=CerberusEnemy.GOBLIN;	
	public static final int EGG=CerberusEnemy.EGG;	
	public static final int SENTINEL=CerberusEnemy.SENTINEL;	
	public static final int BONE_JAIL=CerberusEnemy.BONE_JAIL;	
	public static final int ROCK_HEADR=CerberusEnemy.ROCK_HEADR;	
	public static final int ROCK_HEADL=CerberusEnemy.ROCK_HEADL;	
		
		
	public static final int MAX_ITEM = 20;
	
	public int level, sublevel, cnt, tx, ty, sx, sy, bx, by;
	public byte TileMap[];
	public CerberusEnemy en[];
	public CerberusBoss boss;
	public CerberusItem it[];
	public CerberusSamuel pl;
	public static final int level_en[][][][]={
			// Swamp A
			{{{27,10,SCORPION_FLY},{41,19,TRITON},{56,4,SCORPION_FLY},{77,19,TRITON},{77,10,SCORPION_FLY},{95,19,TRITON},{106,12,SCORPION_FLY},{119,7,SCORPION_FLY},{147,7,SCORPION_FLY},{165,5,SCORPION_FLY}, {211,19,TRITON},{222,19,TRITON},{233,19,TRITON},{242,19,TRITON},{260,3,SCORPION_FLY},{286,9,SCORPION_FLY},{296,7,SCORPION_FLY},{218,6,SCORPION_FLY},{180,9,UGLY_BALL},{325,7,UGLY_BALL}},
			// Swamp B
			{{45,63,TRITON},{21,54,SCORPION_FLY},{59,54,SCORPION_FLY},{70,63,TRITON},{96,54,SCORPION_FLY},{187,46,UGLY_BALL},{94,38,UGLY_BALL},{78,38,UGLY_BALL},{48,38,SCORPION_FLY},{27,37,UGLY_BALL}, {20,27,UGLY_BALL},{42,26,SCORPION_FLY},{60,28,UGLY_BALL},{78,21,UGLY_BALL},{95,7,UGLY_BALL},{73,5,SCORPION_FLY},{78,5,SCORPION_FLY},{27,15,UGLY_BALL},{36,16,UGLY_BALL},{45,16,UGLY_BALL}}
			},
			// Caves A
			{{{30,19,TURTLE_GOLEM},{18,43,TURTLE_GOLEM},{46,57,INV_AMONITE},{14,69,INV_AMONITE},{31,79,AMONITE},{81,83,TURTLE_GOLEM},{89,83,TURTLE_GOLEM}, {187,23,TURTLE_GOLEM},{150,65,INV_AMONITE},{148,53,INV_AMONITE},{133,59,TURTLE_GOLEM},{69,27,TURTLE_GOLEM},{71,39,AMONITE},{101,11,TURTLE_GOLEM},{120,15,AMONITE},{128,83,AMONITE},{136,4,INV_AMONITE},{163,43,TURTLE_GOLEM}},
			// Caves B
			{{40,13,LAVA_ROCK},{32,10,BIG_PLATFORM},{84,13,LAVA_ROCK},{76,10,PLATFORM},{108,21,LAVA_ROCK},{115,21,LAVA_ROCK},{104,18,BIG_PLATFORM},{127,21,LAVA_ROCK},{135,21,LAVA_ROCK},{124,18,PLATFORM}, {152,21,BIG_PLATFORM},{154,16,BIG_PLATFORM},{157,12,PLATFORM},{183,29,LAVA_ROCK},{194,21,PLATFORM},{197,16,PLATFORM},{252,6,BIG_PLATFORM},{268,6,BIG_PLATFORM},{73,11,TURTLE_GOLEM},{169,0,INV_AMONITE},{213,7,TURTLE_GOLEM},{266,1,INV_AMONITE}}
			},
			// Forest A
			{{{27,29,SKULL_DOG},{17,21,GOBLIN},{30,17,GOBLIN},{61,19,GOBLIN},{49,9,GOBLIN},{65,25,SKULL_DOG},{183,15,EGG},{122,29,SKULL_DOG},{132,29,SKULL_DOG},{98,9,GOBLIN},{122,9,GOBLIN},{136,5,GOBLIN},{144,9,GOBLIN},{165,8,GOBLIN},{160,23,GOBLIN}, {220,13,GOBLIN},{237,7,GOBLIN},{261,13,GOBLIN},{262,9,GOBLIN},{278,19,GOBLIN},{326,15,GOBLIN},{330,15,GOBLIN},{350,19,GOBLIN},{204,25,SKULL_DOG},{357,25,SKULL_DOG},{207,27,EGG},{254,23,EGG}},
			// Forest B
			{{21,71,GOBLIN},{22,67,GOBLIN},{20,69,EGG},{38,63,GOBLIN},{20,69,EGG},{51,63,EGG},{97,68,EGG},{20,69,EGG},{100,55,EGG},{53,49,GOBLIN},{9,47,GOBLIN},{102,57,GOBLIN},{22,45,GOBLIN}, {8,29,GOBLIN},{52,39,GOBLIN},{37,35,GOBLIN},{68,45,GOBLIN},{102,23,GOBLIN},{73,13,GOBLIN},{79,13,GOBLIN},{34,44,EGG},{31,30,EGG},{62,31,EGG},{78,36,EGG},{70,19,EGG},{104,15,EGG},{46,15,EGG}}
			},									
			// Castle A
			{{{44,59,BONE_JAIL},{60,59,BONE_JAIL},{140,19,BONE_JAIL},{176,35,BONE_JAIL},{176,51,BONE_JAIL},{8,27,BONE_JAIL},{124,19,BONE_JAIL},{104,62,BIG_PLATFORM},{52,29,BIG_PLATFORM},{57,23,BIG_PLATFORM},{9,23,PLATFORM},{9,18,BIG_PLATFORM},{195,59,PLATFORM},{197,48,PLATFORM},{195,59,PLATFORM},{203,41,PLATFORM},{195,59,PLATFORM},{160,63,PLATFORM},{129,53,PLATFORM},{143,41,ROCK_HEADR},{156,29,ROCK_HEADL},{192,30,ROCK_HEADL},{13,55,SENTINEL},{70,53,SENTINEL},{75,37,SENTINEL},{23,26,SENTINEL},{150,14,SENTINEL},{233,25,SENTINEL}},
			// Castle B
			{{8,165,PLATFORM},{22,134,PLATFORM},{36,116,PLATFORM},{17,91,PLATFORM},{17,85,PLATFORM},{17,79,PLATFORM},{17,70,PLATFORM},{17,65,PLATFORM},{17,60,PLATFORM},{17,55,PLATFORM},{4,161,ROCK_HEADL},{4,161,ROCK_HEADL},{4,117,ROCK_HEADL},{16,86,ROCK_HEADL},{16,66,ROCK_HEADL},{16,58,ROCK_HEADL},{31,66,ROCK_HEADR},{31,58,ROCK_HEADR},{31,86,ROCK_HEADR},{43,129,ROCK_HEADR},{32,175,BONE_JAIL},{24,123,BONE_JAIL},{32,175,BONE_JAIL},{32,139,BONE_JAIL},{24,95,BONE_JAIL},{9,185,SENTINEL},{19,111,SENTINEL}}
			}												
		};
	public static final int level_it[][][][]={
			// Swamp A
			{{{151,11,CerberusItem.ENERGY},{246,7,CerberusItem.ENERGY}},
			// Swamp B
			{{31,1,CerberusItem.HEART},{38,2,CerberusItem.IT_SWORD},{109,22,CerberusItem.BOOTS},{81,21,CerberusItem.ENERGY},{5,34,CerberusItem.ENERGY}}},
			// Caves A
			{{{35,19,CerberusItem.HIKARU},{6,75,CerberusItem.ARMOR},{35,36,CerberusItem.ENERGY},{147,89,CerberusItem.HEART}},
			// Caves B
			{{254,11,CerberusItem.GAUNTLETS},{214,6,CerberusItem.ENERGY},{196,8,CerberusItem.HIKARU},{121,19,CerberusItem.ENERGY},{299,6,CerberusItem.IT_WHIP}}},
			// Forest A
			{{{9,4,CerberusItem.CAPE},{42,1,CerberusItem.ENERGY},{98,6,CerberusItem.ENERGY},{178,14,CerberusItem.HEART},{42,1,CerberusItem.ENERGY},{183,2,CerberusItem.HIKARU},{237,10,CerberusItem.ENERGY}},
			// Forest B
			{{104,37,CerberusItem.HELMET},{5,26,CerberusItem.IT_BOOMER},{65,43,CerberusItem.ENERGY},{106,4,CerberusItem.HEART},{8,52,CerberusItem.ENERGY}}},									
			// Castle A
			{{{9,8,CerberusItem.HEART},{96,26,CerberusItem.ENERGY},{158,62,CerberusItem.HIKARU},{197,18,CerberusItem.LIFE}},
			// Castle B
			{{21,35,CerberusItem.IT_BOOMER},{24,35,CerberusItem.IT_WHIP},{27,35,CerberusItem.IT_SWORD},{21,35,CerberusItem.IT_BOOMER},{17,31,CerberusItem.HEART},{6,130,CerberusItem.ENERGY},{29,75,CerberusItem.ENERGY}}}								
		};
	public static final int shalf_first_en[][]={
		{10,10},
		{7,10},
		{15,13},
		{0,0}
	};
	public static final int level_end[][]={{316,0,321,11},{165,30,173,46},{354,16,360,30},{246,12,256,30}};
	public static final  int boss_area[][]={{-1,0,20,17},{288,13,316,32},{-1,0,32,23},{4,4,48,25}};
	public static final  int second_halves[][][]={{{141,0,319,19},{0,0,42,41}},{{100,0,171,95},{140,1,315,31}},{{180,0,359,35},{0,0,111,44}},{{0,0,0,0},{0,0,0,0}}};
	public int timer;
	
	public Random rnd;

	public CerberusWorld(int l, int sl, boolean secondhalf, boolean boss_area)
	{
		InputStream is;
		String filename="";
		int filepos=0, fileoffset[]={};
		
		level=l; sublevel=sl;
			
		switch(level){
			case SWAMP : 
			if(sublevel==0){
				tx=80; ty=5; filepos=6; 
			}
			if(sublevel==1){
				tx=28; ty=16; filepos=7; 
			}
			break;			
			
			case CAVES : 
			if(sublevel==0){
				tx=43; ty=24; filepos=2; 
			}
			if(sublevel==1){
				tx=79; ty=8; filepos=3; 
			}	
			break;		
			
			case FOREST :
			if(sublevel==0){
				tx=90; ty=9; filepos=4; 
			}
			if(sublevel==1){
				tx=28; ty=21; filepos=5; 
			}				
			break;
			
			case CASTLE :
			if(sublevel==0){
				tx=64; ty=17; filepos=0; 
			}
			if(sublevel==1){
				tx=12; ty=48; filepos=1; 
			}				
			break;
						
		}
		
		sx=tx*32; sy=ty*32;
		
		int map_offset[]={1088,576,1032,632,810,588,400,448,448};
		TileMap=multiread("/Maps.map",map_offset,filepos);
					
		bx=0; by=0; cnt=0;
		
		// Create the enemies	
		//if(false){
		if(!boss_area){			
			en=new CerberusEnemy[level_en[level][sublevel].length];
			for(int i=0; i<level_en[level][sublevel].length; i++)
				en[i]=new CerberusEnemy(8*level_en[level][sublevel][i][0],8*level_en[level][sublevel][i][1],level_en[level][sublevel][i][2]);			
			if(secondhalf)
			for(int i=0; i<shalf_first_en[level][sublevel]; i++)
				en[i].out_of_world=true;
		}else {en=null;}
		
		// Create items		
		it=new CerberusItem[MAX_ITEM];
		for(int i=0; i<MAX_ITEM; i++)
			it[i]=null;
			
		for(int i=0; i<level_it[level][sublevel].length; i++)
			it[i]=new CerberusItem(8*level_it[level][sublevel][i][0],8*level_it[level][sublevel][i][1],level_it[level][sublevel][i][2],false);			
			
									
		// Create player
		
		switch(level){
			
			case SWAMP : 
			if(sublevel==0){
				if(secondhalf)
				pl=new CerberusSamuel(157*8,8*8);
				else
				pl=new CerberusSamuel(20,130);
			}else 
				if(boss_area)
				pl=new CerberusSamuel(60,60);
				else 
				if(secondhalf)
				pl=new CerberusSamuel(7*8,33*8);				
				else pl=new CerberusSamuel(20,470);				
			break;
		
			case CAVES : 		
			if(sublevel==0){
				if(secondhalf)
				pl=new CerberusSamuel(113*8,84*8);
				else
				pl=new CerberusSamuel(24,80);
			}else 
				if(boss_area)
				pl=new CerberusSamuel(292*8,12*8);
				else 
				if(secondhalf)
				pl=new CerberusSamuel(148*8,19*8);				
				else pl=new CerberusSamuel(24,80);				
			break;
			
			case FOREST :
			if(sublevel==0){
				if(secondhalf)
				pl=new CerberusSamuel(174*8,14*8);
				else				
				pl=new CerberusSamuel(3*8,27*8);
			}else 
				if(boss_area)
				pl=new CerberusSamuel(4*8,3*8);
				else if(secondhalf) pl=new CerberusSamuel(95*8,46*8);				
				else pl=new CerberusSamuel(2*8,74*8);				
			break;
			
			case CASTLE :
			if(sublevel==0)
				pl=new CerberusSamuel(4*8,56*8);
			else 
				if(boss_area)
				pl=new CerberusSamuel(7*8,19*8);
				else pl=new CerberusSamuel(3*8,184*8);				
			break;
			
		}
				
		bx=pl.x-88; by=pl.y-120;
		
		if(level==0 && sublevel==1) boss=new CerberusBoss(100,-66,CerberusBoss.WIDOW);
		else if(level==1 && sublevel==1) boss=new CerberusBoss(302*8,29*8,CerberusBoss.VIPER);
		else if(level==2 && sublevel==1) boss=new CerberusBoss(100,-30,CerberusBoss.BIRD);
		else if(level==3 && sublevel==1) boss=new CerberusBoss(37*8,23*8,CerberusBoss.CERBERUS);
		else boss=null;
		
		rnd=new Random(System.currentTimeMillis());
		
		timer=30*60*3;
				
	}
	
	public void update(CerberusCanvas c)
	{
		cnt++; 
		
		if (timer>0) timer--; else pl.modify_energy(-1000);
		
		pl.update(c.ctrl,this);
		
		pl.platform=false;
		
		if(en!=null)
		for(int i=0; i<en.length; i++)
			if(!en[i].out_of_world)
			if((Math.abs(pl.x-en[i].x)<300) && (Math.abs(pl.y-en[i].y)<160))
				en[i].update(this);		
				
		if(boss!=null) 
		if(!boss.out_of_world && at_boss_area())
			boss.update(this);
				
		for(int i=0; i<it.length; i++)
			if(it[i]!=null) 
				if(it[i].out_of_world) it[i]=null; 
				else if(pl.body.inside(it[i].x-8,it[i].y-16) && it[i].fading==0)
				{
					// Take item
					it[i].fading=1;	
					if(it[i].type>=CerberusItem.HELMET)
						{pl.set_state(CerberusSamuel.TAKE_ITEM); c.play_sound(0);}
					else {pl.set_state(CerberusSamuel.TAKE_ITEM_SHORT); c.play_sound(1);}
					switch(it[i].type){
						case CerberusItem.ENERGY : pl.modify_energy(10); break;	
						case CerberusItem.HEART  : pl.modify_energy(200); break;	
						case CerberusItem.HIKARU : pl.dam_time=300; break;	
						case CerberusItem.LIFE : CerberusMain.lifes++; break;	
						case CerberusItem.IT_SWORD : pl.weapon=CerberusSamuel.SWORD; CerberusMain.weapon=pl.weapon; break;	
						case CerberusItem.IT_WHIP : pl.weapon=CerberusSamuel.WHIP; CerberusMain.weapon=pl.weapon; break;							
						case CerberusItem.IT_BOOMER : pl.weapon=CerberusSamuel.BOOMER; CerberusMain.weapon=pl.weapon; break;							
						case CerberusItem.HELMET : pl.helmet=true; CerberusMain.helmet=true; break;							
						case CerberusItem.ARMOR : pl.armor=true; CerberusMain.armor=true; break;							
						case CerberusItem.GAUNTLETS : pl.gauntlets=true; CerberusMain.gauntlets=true; break;							
						case CerberusItem.BOOTS : pl.boots=true; CerberusMain.boots=true; break;							
						case CerberusItem.CAPE : pl.cape=true; CerberusMain.cape=true; break;													
					}
					
				} else it[i].update(this);
		
	}
	
	public int tile_at(int x, int y)
	{
		int mx, my, t, x2, y2, t2;
		
		mx=x/32; my=y/32;
		
		if(mx<0) mx=0;
		if(mx>=tx) mx=tx-1;
		if(my<0) my=0;
		if(my>=ty) my=ty-1;
		
		
		// Obtain tile in a 8x8 gride
						
		t=(int)TileMap[(tx*my)+mx];
		
		if(t<0) t+=256;
						
		// Transform to a tile of a 16x16 gride
		x2=(t%16)/2;
		y2=(t/16)/2;
		
		t2=(y2*9)+x2;
		
		t2+=(x/16)%2;
		t2+=9*((y/16)%2);						
		return t2;			
	}
	
	public boolean solid(int t)
	{
		switch(level){
			
			case SWAMP :
			if(
				((t>=12) && (t<=15)) ||			
				((t>=19) && (t<=26)) ||
				((t>=27) && (t<=35)) ||
				(t==36) || (t==40) ||	
				//((t>=46) && (t<=49)) ||	
				((t>=51) && (t<=53)) ||	
				(t==57) || (t==63) || (t==66) || (t==68) || (t==70) || (t==71) ||
				(t==70) || (t==75) || (t==77) || (t==79) || (t==80))
				
			return true;
			else return false;
									
			case CAVES :
			if(
				((t>=3) && (t<=4)) ||
				((t>=11) && (t<=14)) ||				
				((t>=21) && (t<=22)) ||
				((t>=37) && (t<=39)) ||
				((t>=43) && (t<=44)) ||
				((t>=52) && (t<=53)) ||
				((t>=54) && (t<=55)) ||
				((t>=58) && (t<=62)) ||
				((t>=69) && (t<=71)) 
			)
			return false;
			else return true;						
			
			case FOREST :
			if(
				((t>=4) && (t<=6)) ||
				((t>=10) && (t<=12)) ||
				((t>=16) && (t<=17)) ||
				(t==18) ||
				((t>=25) && (t<=26)) ||
				((t>=27) && (t<=28)) ||
				((t>=31) && (t<=32)) ||
				((t>=38) && (t<=39)) ||
				((t>=54) && (t<=57)) ||
				(t==62) ||
				((t>=63) && (t<=66)) ||
				((t>=69) && (t<=71)) ||
				((t>=72) && (t<=75)) ||
				((t>=78) && (t<=80))
			)
			return true;
			else return false;						
			
			case CASTLE :
			if(
				(t==0) || (t==1) ||
				(t==4) || (t==5) ||
				(t==9) || (t==10) ||
				(t==13) || (t==14) ||
				(t==22) || (t==23) ||
				(t==31) || (t==32) ||
				(t==36) || (t==37) ||
				(t==42) || (t==43) ||
				(t==45) || (t==46) ||
				(t==51) || (t==52) ||
				((t>=56) && (t<=59)) ||
				((t>=63) && (t<=70)) 				
			)return true;
			else return false;
			
		}
		return false;
	}
	
	public int y_at_tile(int x, int y)
	{
		int ix, t, iy, px, py=0;
		ix=x/16; iy=y/16;
		px=x%16;
		
		t=tile_at(x,y);
		
		if(solid(t)){
	
			switch(level){
				
				case SWAMP:
			 	switch(t){
			
					case 12 : py=px/2; break;
					case 13 : py=8+px/2; break;
					case 14 : py=15-px/2; break;
					case 15 : py=7-px/2; break;
					case 19 : py=px; break;
					case 26 : py=15-px; break;
					default : return iy*16;
				}				
				break;					
				
				case CAVES:
			 	switch(t){
			
					case 63 :
					case 56 : py=px/2; break;
					case 64 :
					case 57 : py=8+px/2; break;
					case 67 :
					case 41 : py=15-px/2; break;
					case 68 :
					case 42 : py=7-px/2; break;
					case 31 :
					case 23 : py=px; break;
					case 30 :
					case 20 : py=15-px; break;
					default : return iy*16;
				}				
				break;		
				
				case FOREST:
			 	switch(t){
						
					case 69 :
					case 62 :
					case 31 :		
					case 16 : py=px/2; break;					
					case 70 :
					case 32 :
					case 17 : py=8+px/2; break;					
					case 26 :
					case 38 :					
					case 27 : py=15-px/2; break;					
					case 39 :
					case 28 : py=7-px/2; break;					
					case 10 :
					case 18 : py=px; break;					
					//case 26 : py=15-px; break;
					default : return iy*16;
				}				
				break;	
				
				case CASTLE :
			 	switch(t){
					
					case 56 :
					case 63 : py=px/2; break;					
					case 57 :
					case 64 : py=8+px/2; break;					
					case 69 :
					case 58 : py=15-px/2; break;					
					case 59 :
					case 70 : py=7-px/2; break;					
					default : return iy*16;
				}				
				break;	
				
															
			}
			return (iy*16)+16-py;																	
		}else{
			return (iy+1)*16;
		}		
	}
	
	public void add_item(int x, int y, int t)
	{
		int i=0;
		
		while(it[i]!=null && i<MAX_ITEM) i++;	
		
		if(i<MAX_ITEM) it[i]=new CerberusItem(x,y,t,false);
		
	}
	
	public boolean probabilistic(int max)
	{
		if (Math.abs(rnd.nextInt())%max==0) return true;
		else return false;
	}
	
	public void add_random_item(int x, int y)
	{
		int i=0, t=0;
		
		if(probabilistic(3)){
		// Do create a item
			if(probabilistic(1000))
				t=CerberusItem.PHONE;
			else if(probabilistic(2))
				t=CerberusItem.ENERGY;
			else 		
				// Life		
				if(probabilistic(3)) t=CerberusItem.LIFE;
				// Other items
				else if(probabilistic(2)){
					
					if(probabilistic(2))
						t=CerberusItem.HIKARU;
					else  	t=CerberusItem.HEART;										
				}else{
					// A Weapon				
					t=CerberusItem.IT_SWORD+(Math.abs(rnd.nextInt()%3));				
				}				
		}else return;
															
		while(it[i]!=null && i<MAX_ITEM) i++;	
		
		if(i<MAX_ITEM) it[i]=new CerberusItem(x,y,t,true);
		
	}	
	
	public boolean level_finished()
	{
		if(pl.x>level_end[level][0]*8 && pl.y>level_end[level][1]*8 
			&& pl.x<level_end[level][2]*8 && pl.y<level_end[level][3]*8)
			return true;
		else return false;				
	}
	
	public boolean at_boss_area()
	{
		if(pl.x>boss_area[level][0]*8 && pl.y>boss_area[level][1]*8 
			&& pl.x<boss_area[level][2]*8 && pl.y<boss_area[level][3]*8)
			return true;
		else return false;				
	}	
	
	public boolean at_second_half()
	{
		if(pl.x>second_halves[level][sublevel][0]*8 && pl.y>second_halves[level][sublevel][1]*8 
			&& pl.x<second_halves[level][sublevel][2]*8 && pl.y<second_halves[level][sublevel][3]*8)
			return true;
		else return false;				
	}	

	
	void leerdata(InputStream is,byte[] buffer,int size)
	{
		try{
		is.read(buffer, 0,size);is.close();
		}catch(Exception exception){}
	
	}
	
	byte[] multiread(String f, int lengths[], int n)
	{
		int startpos = 0;

		for(int i = 0; i < n; i++)
		{
			startpos += lengths[i];
		}

		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+f.substring(1));
		byte[] bytes = file.readBytes();

		byte buffer[]=new byte[lengths[n]];

		for(int j = 0; j < buffer.length; j++)
			buffer[j] = bytes[startpos+j];

		return buffer;
		/*System.gc();
		InputStream is = getClass().getResourceAsStream(f);
		int i=0;
		
		while(i<n){
			try{
				for(int t=0; t<lengths[i]; t++)
					is.read();
						
			}catch(Exception exception) {}
			i++;	
		}
	
		byte buffer[]=new byte[lengths[n]];
	
		try	{
		is.read(buffer, 0, buffer.length);
		is.close();
		}catch(Exception exception) {}
		System.gc();
		
		return buffer;	*/
	}	
}