package com.mygdx.mongojocs.pingpoyo;

public class Opponent
{
	public static final int IDLE = 0;
	public static final int CHARGE = 1;
	public static final int RELEASE = 2;
	public static final int FIRST = 3;
	
	public static final int LEO = 0;
	public static final int BOB = 1;
	public static final int BLAK = 2;
	public static final int PIGY = 3;
	public static final int PONK = 4;
	public static final int ROBDK = 5;
	public static final int KILLDK = 6;

	int x, state, power, charged, cnt, direction, charId, lastPosition, lastDirection, respondTime, ballSequence[];

	public Opponent(int id)
	{
		charId = id; x = 0; lastPosition = 0; lastDirection = 1; respondTime = 0; setState(IDLE);		
		ballSequence = new int[10];
	}
	
	public void setState(int s)
	{
		cnt=0; state = s;
	}
	
	private void addBallSeq(int s)
	{
		for(int i = ballSequence.length-1; i>=1; i--)
		
			ballSequence[i] = ballSequence[i-1];
			
		ballSequence[0] = s;
			
		/*for(int i=0; i<ballSequence.length; i++)
			System.out.print(ballSequence[i]+",");
		System.out.println();*/
				
	}
	
	private boolean repeatedSeq(int i1, int i2)
	{
		boolean rep=ballSequence[i1]!=0;
		
		for(int i=i1; i<i2; i++)
			rep = rep & (ballSequence[i]==ballSequence[i+1]);
		
		return rep;
	}
	
	// IA ==========================================================
	
	public void IAreset()
	{
		respondTime = 0; lastPosition = 0; lastDirection = 1;	
		for(int i = 0; i<ballSequence.length; i++)
			ballSequence[i] = 0;
	}
	
	private boolean respond(Ball b)
	{
		int reaction;
				
		switch(charId){
			default : reaction = 8; break;
			case BOB : reaction = 5; break;
			case BLAK : reaction = 6; break;
			case PIGY : reaction = 4; break;
			case PONK : reaction = 4; break;
			case ROBDK : reaction = 3; break;
			case KILLDK : reaction = 2; break;
		}
		
		return (Game.RND(reaction)==0);
	}
	
	private boolean respondDistance(Ball b)
	{
		int distance;
		
		switch(charId){
			default : distance = 16; break;
			case BOB : distance = 20; break;
			case BLAK : distance = 14; break;
			case PIGY : distance = 12; break;
			case PONK : distance = 10; break;
			case ROBDK : distance = 8; break;
			case KILLDK : distance = 6; break;
		}
		
		return (b.z > distance);
	}
	
	private int respondLimit()
	{
		switch(charId){
			default : return 4;
			case BOB : return 6;
			case BLAK : return 8; 
			case PIGY : return 10; 
			case PONK : return 14; 
			case ROBDK : return 18; 
			case KILLDK : return 20;
		}
	}
	
	private int choosenPosition(Ball b)
	{
		int x, intelligence, onFail;
		boolean mustFail; 
				
		switch(charId){
			default : mustFail=Game.RND(3)==0 && ((b.from==0 && b.to==5) || (b.from==2 && b.to==3)); intelligence = 5; onFail=0; break;
			case BOB : mustFail=(ballSequence[0]==5 && ballSequence[1]==5/* && ballSequence[2]==5*/); intelligence = 4; onFail=-1; break;
			case BLAK : mustFail=Game.RND(6)==0 && (ballSequence[0]==5 || ballSequence[0]==3) && b.speed==b.MAX_SPEED; intelligence = 6; onFail=0; break;
			case PIGY : mustFail=repeatedSeq(1,4) && ballSequence[0]!=ballSequence[1]; intelligence = 8; onFail=ballSequence[1]-4; break;
			case PONK : mustFail=repeatedSeq(1,4) && ballSequence[0]!=ballSequence[1] && b.speed==Ball.MAX_SPEED; intelligence = 10; onFail=ballSequence[1]-4; break;
			case ROBDK : mustFail=Game.RND(3)==0 && repeatedSeq(1,3) && ballSequence[0]!=ballSequence[1] && b.speed==Ball.MAX_SPEED; intelligence = 12; onFail=ballSequence[1]-4; break;
			case KILLDK : mustFail=((lastDirection==0 && b.to==5) || (lastDirection==2 && b.to==3) || (lastDirection==1 && b.to!=4))&& b.speed==Ball.MAX_SPEED && Game.RND(2)==0; 
						  intelligence = 20; onFail=lastPosition; break;
					
		}
		
		if(mustFail || respondTime>respondLimit() || (Game.RND(intelligence)==0)){
			
			// Force (fail)	
			x = onFail;				
							
		}else{
			// Right response
			if(b.to==3) x = -1;
			else if(b.to==5) x = 1;
			else x = 0;
		}		
		return x;					
	}
	
	private int choosenDirection(Ball b, Player p)
	{
		int x, seq[] = {0,1,2,1};
				
		switch(charId){
			
			default : if(Game.RND(10)==0) x = Game.RND(3); else x = lastDirection; break;
			case BOB : if(respondTime%4==0) x = Game.RND(3); else x = lastDirection; break;
			case BLAK : if(Game.RND(8)==0) x = Game.RND(3); else x = lastDirection; break;
			case PIGY : x = Game.RND(3); break;
			case PONK : x = Game.RND(3); break;
			case ROBDK : x = seq[respondTime%4]; break;
			case KILLDK : if(p.moveTo == -128) x=2; 
						  else if(p.moveTo == 0) x=1;
						  else x=0; 
						  break;
		}
					
		return x;						
	}
	
	private int chargeFirstTime()
	{
		switch(charId){
			default : return 42;	
		}
	}
	
	private int responsePower()
	{
		switch(charId){
			default : return 0;	
			case BOB : return 10;
			case BLAK : return (direction != lastDirection ? 42 : 0);
			case PIGY : return 0;	
			case PONK : return (Game.RND(3)==0 ? 42 : 0);
			case ROBDK : return 42;
			case KILLDK : return 42;
		}
	}
	
	//==============================================================

	public void update(Ball b, Player p)
	{
		int aux;
		
		cnt++;
		
		switch(state){
		
			case IDLE :		
			if(respondDistance(b) && b.velz>0 && respond(b)){
								
				addBallSeq(b.to);
				x = choosenPosition(b);
				lastPosition = x;
				
				setState(CHARGE);
				power = responsePower();
			}
			break;
			
			case CHARGE :
			//if(power<42) power++;									
			if(b.z>118) {
				charged=power; 				
				setState(RELEASE);				
				direction = choosenDirection(b,p);
				lastDirection = direction;
				respondTime++;
				
				//System.out.println("Chosen Dir:"+direction+"   Last Dir:"+lastDirection+"   respondTime:"+respondTime);
			}
			break;
			
			case FIRST :
			if(power<42) power++;									
			if(power>=chargeFirstTime()) {
				charged=power; 				
				setState(RELEASE);				
				direction = choosenDirection(b,p);
				lastDirection = direction;
			}
			break;
			
			
			case RELEASE :
			if(power>0) power-=15;
			if(cnt>10){
				 power = 0; 
				 setState(IDLE);
			}
			break;
		}
			 
	}

}