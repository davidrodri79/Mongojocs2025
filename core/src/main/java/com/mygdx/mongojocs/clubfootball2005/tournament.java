package com.mygdx.mongojocs.clubfootball2005;

import java.io.*;


public class tournament
{
	
	public int current, teamsNumber, duration, teamSize;
	// games[teamA,teamB,goalsA,goalsB]
	byte league[][][], 
		//rank[teamId,points,victories,loses,draws,plusGoals,minusGoals]		
		myTeam, myAlignment[] = new byte[30];
	short rank[][];
	boolean wholeLeague, isDomestic;
			
	public tournament(int nt, boolean domestic)
	{
		teamsNumber = nt;
		isDomestic = domestic;
		byte teamA, teamB, i1, i2;
		short aux;
		
		// Change the order of the teams, a different league every time you play
		
		rank = new short[teamsNumber][7];
		for(byte i = 0; i < teamsNumber; i++)
		{
			rank[i][0] = (short)(i);
			
			for(byte j = 1; j < 5; j++)
				rank[i][j] = 0;
							
		}
		
		for(int i = 0; i<teamsNumber*teamsNumber; i++)
		{
			i1 = (byte)Game.RND(teamsNumber);
			i2 = (byte)Game.RND(teamsNumber);
			
			aux = rank[i1][0];
			rank[i1][0] = rank[i2][0];
			rank[i2][0] = aux;
		}
		
		// Startup League
						
		league = new byte[2*(teamsNumber-1)][teamsNumber/2][4];	
		
		for(int i = 0; i < league.length; i++)
		for(int j = 0; j < league[i].length; j++){
			league[i][j][0] = -1;
			league[i][j][1] = -1;
			league[i][j][2] = -1;
			league[i][j][3] = -1;
		}
		
		// First half: Home Play
		
		// First day
		
		for(int i = 0; i < league[0].length; i++)
		{
			league[1][i][0] = (byte)(i);
			league[1][i][1] = (byte)(teamsNumber - 1 - i);						
			
			league[0][i][1] = (byte)(teamsNumber/2 - 1 - i);
		}
		
		league[0][0][0] = (byte)(teamsNumber - 1);
		
		for(int i = 1; i<league[0].length; i++)
		{
			league[0][i][0] = (byte)(league[0].length - 1 + i);
		}
		
		// Rotate
		
		for(int i = 2; i <= league.length/2; i++)
			if(i%2 == 1){
								
				for(int j = 0; j < league[i].length - 1; j++)
					league[i][j][0] = league[i-2][j+1][0];
					
				league[i][league[i].length - 1][0] = league[i-2][league[i].length - 1][1];
					
				for(int j = 2; j < league[i].length; j++)
					league[i][j][1] = league[i-2][j-1][1];
					
				league[i][0][1] = league[i-2][0][1];
				league[i][1][1] = league[i-2][0][0];
				
			}else{
			
				league[i][0][0] = league[i-2][0][0];
				league[i][league[i].length - 1][0] = league[i-2][league[i].length - 1][1];
				league[i][0][1] = league[i-2][1][0];
			
				for(int j = 1; j < league[i].length - 1; j++)
					league[i][j][0] = league[i-2][j+1][0];
					
				for(int j = 1; j < league[i].length; j++)
					league[i][j][1] = league[i-2][j-1][1];
									
			}
		
		
		// Second half: Visitor play
		
		for(int i = 0; i < teamsNumber - 1; i++)
		for(int j = 0; j < teamsNumber/2; j++){
		
			league[teamsNumber -1 + i][j][0] = league[i][j][1];
			league[teamsNumber -1 + i][j][1] = league[i][j][0];
		}
		
		// Mix teams
		
		for(int i = 0; i < league.length; i++)
		for(int j = 0; j < league[i].length; j++){
		
			league[i][j][0] = (byte)rank[league[i][j][0]][0];
			league[i][j][1] = (byte)rank[league[i][j][1]][0];
			
		}		
		
		current = 0;
					
		/*for(int i = 0; i < 2*(teamsNumber - 1); i++){
			
			System.out.print("Jornada "+i+": ");
			
			for(int j = 0; j < teamsNumber/2; j++){
				System.out.print((league[i][j][0]+0)+"VS"+(league[i][j][1]+0)+" ");
			}
			System.out.println();
		}*/
			
	}
	
	public void setBasicAlignment(int originals, int customs)
	{
		teamSize = originals + customs;
		
		for(int i = 0; i<originals; i++)
			myAlignment[i] = (byte)i;
			
		for(int i = 0; i<customs; i++)
			myAlignment[originals + i] = (byte)(100 + i);
				
	}
	
	public void simulate()
	{
		for(int i = 0; i < league[current].length; i++){
			league[current][i][2] = (byte)Game.RND(5);
			league[current][i][3] = (byte)Game.RND(5);									
		}		
	}
	
	public void step()
	{
		byte teamA, teamB, posA, posB, goalsA, goalsB;
		
		for(int i = 0; i < league[current].length; i++){
			
			teamA = league[current][i][0];
			teamB = league[current][i][1];
			goalsA = league[current][i][2];
			goalsB = league[current][i][3];
			
			posA = findPos(teamA);
			posB = findPos(teamB);
			
			rank[posA][5] += goalsA; 
			rank[posA][6] += goalsB; 
			rank[posB][5] += goalsB; 
			rank[posB][6] += goalsA; 
			
			if(goalsA > goalsB)
			{
				// Team A wins
				rank[posA][1] += 3; 
				rank[posA][2] += 1; 
												
				rank[posB][3] += 1; 
				
								
			}else if(goalsA < goalsB)
			{
				// Team B wins
				rank[posB][1] += 3; 
				rank[posB][2] += 1; 
								
				rank[posA][3] += 1; 
				
			}else
			{
				// Draw
				rank[posA][1] += 1;
				rank[posA][4] += 1; 
								
				rank[posB][1] += 1;
				rank[posB][4] += 1; 
			}
		}		
		
		sortRank();
		
		current++;
		
		/*System.out.println("0 played against "+myOpp((byte)0));
		
		System.out.println("Jornada "+current);
		for(int i = 0; i<rank.length; i++)
			System.out.println( "      "+i+"� : Team "+rank[i][0]+", "+rank[i][1]+" points");
		*/
	}
	
	public byte myOpp(byte team)
	{
		int i = 0;
		
		while(league[current][i][0] != team && league[current][i][1] != team && i < league[current].length) i++;
			
		if(i < league[current].length){ 
			if(league[current][i][0] == team) return league[current][i][1];
			else if(league[current][i][1] == team) return league[current][i][0];
		}
		
		return -1;
	}
	
	public void setGoals(byte team, byte goal)
	{
		for(int i = 0; i < league[current].length; i++)
		{
			if(league[current][i][0] == team) {league[current][i][2] = goal; return;}
			if(league[current][i][1] == team) {league[current][i][3] = goal; return;}			
		}
	}
	
	public byte findPos(int team)
	{
		byte i = 0;
		
		while(rank[i][0] != team && i < rank.length) i++;
			
		if(i < rank.length) return i;
		return -1;
	}
	
	public void sortRank()
	{
		short aux[];
		
		for(int j = 0; j < teamsNumber; j++)
			for(int i = 0; i < rank.length - 1; i++)
			
				if(rank[i][1] < rank[i+1][1] || 
					(rank[i][1] == rank[i+1][1] && rank[i][5]-rank[i][6] < rank[i+1][5]-rank[i+1][6]) ||
					(rank[i][1] == rank[i+1][1] && rank[i][5]-rank[i][6] == rank[i+1][5]-rank[i+1][6] && rank[i][5] < rank[i+1][5]))		
				{
					// Swap		
					aux = rank[i];
					rank[i] = rank[i+1];
					rank[i+1] = aux;					
				}
							
	}
		
	public void write(DataOutputStream ostream)
	{
		try{
		
			ostream.writeInt(teamsNumber);
			ostream.writeInt(current);			
			ostream.writeInt(duration);
			ostream.writeByte(myTeam);
			ostream.writeInt(teamSize);
			ostream.writeBoolean(wholeLeague);
			ostream.writeBoolean(isDomestic);
			
			for(int i = 0; i<myAlignment.length; i++)			
				ostream.writeByte(myAlignment[i]);
			
			for(int i = 0; i<teamsNumber; i++)
			for(int j = 0; j<7; j++)
				ostream.writeShort(rank[i][j]);
											
			for(int i = 0; i<2*(teamsNumber-1); i++)
			for(int j = 0; j<teamsNumber/2; j++)
			for(int k = 0; k<4; k++)
				ostream.writeByte(league[i][j][k]);
						
		}catch(Exception e){/*System.out.println("Error en la escriptura");*/}

	}
	
	public void read(DataInputStream istream)
	{
		try{
		
			teamsNumber = istream.readInt();
			current = istream.readInt();			
			duration = istream.readInt();
			myTeam = istream.readByte();
			teamSize = istream.readInt();
			wholeLeague = istream.readBoolean();
			isDomestic = istream.readBoolean();
			
			for(int i = 0; i<myAlignment.length; i++)			
				myAlignment[i] = istream.readByte();
			
			for(int i = 0; i<teamsNumber; i++)
			for(int j = 0; j<7; j++)
				rank[i][j] = istream.readShort();
											
			for(int i = 0; i<2*(teamsNumber-1); i++)
			for(int j = 0; j<teamsNumber/2; j++)
			for(int k = 0; k<4; k++)
				league[i][j][k] = istream.readByte();
						
		}catch(Exception e){/*System.out.println("Error en la lectura");*/}
	}

}