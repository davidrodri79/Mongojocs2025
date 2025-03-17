package com.mygdx.mongojocs.lma2007;

public class Team
{
	//#ifndef LESS_PLAYERS
	final static int MAXPLAYERS = 30;
	//#else
	//#endif
	
	final static int KICK = 0;
	final static int PASS = 1;
	final static int SPEED = 2;
	final static int DEFENCE = 3;
	
	public byte teamLeague;
	public byte globalIdx;
	public String name;
//#ifndef REM_TEAMCOLORS
	public int flagColor[] = new int[2];
	public int homeColor[] = new int[4];
	public int awayColor[] = new int[4];
//#endif
	public short[] playerIds = null;
	public byte playerCount;
	public short cash;
	public byte matchGoals;
	
	public byte matchesPlayed;
	public byte matchesWon;
	public byte matchesLost;
	public byte matchesDrew;
	public short goalsScored;
	public short goalsReceived;
	public short points;
	
	byte europeanMatchGoals;
	
	
	public Team() 
	{
		name = null;
		cash = 0;
		playerIds = new short[MAXPLAYERS];		
	}

	
	
	public void load(String _name, int _total, int _base, int _money)
	{
		name = _name;
		cash = (short)_money;
		playerCount = (byte)_total; 
		
		for (int i = 0; i < MAXPLAYERS; i++) 
		{
			if (i < _total) 
				playerIds[i] = (short)(i + _base);
			else 
				playerIds[i] = -1;			
		}
	}
	
	
	
	boolean isUserTeam()
	{
		if (GameCanvas.gc.exhibitionFlag)
			return false;
		else
			return (this == GameCanvas.gc.league.userTeam);
	}
	
	
	// 0 , equipos igual en la table
	// 1 , gana el equipo propio
	// -1, gana el opponente
	public int compareLeagueStatus(Team opp)
	{
		int res = 0;
		int goalBalance = goalsScored - goalsReceived; 
		int goalBalanceOpp = opp.goalsScored - opp.goalsReceived;	
			
		if (points == opp.points) 
		{
			if (goalBalance == goalBalanceOpp) 
			{
				if (goalsScored > opp.goalsScored) res = 1; 									
				else if (opp.goalsScored < goalsScored) res = -1; 				
			} 
			else 
			{
				if (goalBalance > goalBalanceOpp) res = 1;
				else if (goalBalance < goalBalanceOpp) res = -1;				
			}
		} 
		else 
		{
			if (points > opp.points) res = 1;
			else res = -1;			
		}
		
		return res;
	}
	
	
	
	int getQuality() 
	{
			int res;
			int acc = 0;
			int i;
			
			for (i = 0; i < playerCount; i++) 
			{
				if (playerIds[i] == -1) break;
				acc += League.playerGetQuality(playerIds[i]);
			}
			if (i == 0) i = 1; //Porsiaca

			if (isUserTeam()) acc += -10 + (GameCanvas.gc.custSkillNum[0]*i) / 4;
			
			//ZNR
			res = (acc / i);
			if (res == 0) res = 1; //Porsiaca

			return res;
	}
	
	
	int getOffensiveQuality()
	{
		int res;
		int acc = 0;
		int i;
			
		for (i = 0; i < (isUserTeam()?11:playerCount); i++) 
		{
			if (playerIds[i] == -1) break;
			switch (League.playerGetPosition(playerIds[i]))
			{
				case 1: acc += League.playerGetSkill(playerIds[i], SPEED) + League.playerGetSkill(playerIds[i], PASS);
					break;
				case 2: acc += League.playerGetSkill(playerIds[i], SPEED) + League.playerGetSkill(playerIds[i], PASS);
					break;
				case 3: acc += League.playerGetSkill(playerIds[i], SPEED) + League.playerGetSkill(playerIds[i], KICK);
					break;
			}
			//acc += com.mygdx.mongojocs.lma2007.League::playerGetQuality(players[i]);
		}
		if (i == 0) i = 1;

		//ZNR
		res = (2*acc) / i;
		
		if (isUserTeam() && League.playerGetPosition(playerIds[0]) != 0)		
			res = res/2;
		
		if (res == 0) res = 1;

		return res;
	}
	
	

	int getDefensiveQuality()
	{
		int res;
		int acc = 0;
		int i;
			
		for (i = 0; i < (isUserTeam()?11:playerCount); i++) 
		{
			if (playerIds[i] == -1) break;
			switch (League.playerGetPosition(playerIds[i]))
			{
				case 0: acc += League.playerGetSkill(playerIds[i], SPEED) + League.playerGetSkill(playerIds[i], DEFENCE)*2;
					break;
				case 1: acc += League.playerGetSkill(playerIds[i], SPEED) + League.playerGetSkill(playerIds[i], DEFENCE);
					break;
				case 2: acc += League.playerGetSkill(playerIds[i], DEFENCE);
					break;
				case 3: acc += League.playerGetSkill(playerIds[i], DEFENCE);
					break;
			}
			//acc += com.mygdx.mongojocs.lma2007.League::playerGetQuality(players[i]);
		}
		if (i == 0) i = 1;

		//ZNR
		res = 2*acc / i;
		if (res == 0) res = 1;

		return res;
	}
	
	
	public void startSeason() 
	{
		points = 0;
		matchGoals = 0;
		goalsScored = 0;
		goalsReceived = 0;
		matchesPlayed = 0;
		matchesWon = 0;
		matchesLost = 0;
		matchesDrew = 0;
		europeanMatchGoals = 0;
		//#ifndef REM_TOP_SCORERS
		
		for (int i = 0; i < playerCount; i++) 
		{
			//#ifdef DEBUG
			//System.out.println("team startseason: "+i);
			//#endif
			if (playerIds[i] != -1);
				League.playerSetGoals(playerIds[i], 0);
		}
		//#endif
	}

	
	public void addPlayer(int pid)
	{
		//#ifdef DEBUG
		System.out.println("adding playercount "+playerCount);
		System.out.println("adding player "+pid);
		System.out.println("que se llama "+League.playerGetName(pid));		
		//#endif
		if (playerCount < MAXPLAYERS)
		{
			if (isUserTeam())
			{
				if (League.addExtendedPlayer(pid))
				{
					playerIds[playerCount] = (short)pid;
					League.transferRegister((short)pid, globalIdx);
					playerCount++;
				}
			}
			else
			{
				playerIds[playerCount] = (short)pid;
				League.transferRegister((short)pid, globalIdx);
				playerCount++;			
			}
			//#ifdef DEBUG
			System.out.println("playerCount: "+playerCount);
			//#endif
		}
	}
	
	
	
	public void removePlayer(int pid)
	{
		
		//#ifdef DEBUG
		Debug.println("predeleted :"+playerCount);
		//#endif
		for (int i = 0; i < playerIds.length; i++)
		{
			if (playerIds[i] == pid)
			{				
				for(int j = i; j < MAXPLAYERS-1;j++)
				{
					playerIds[j] = playerIds[j+1]; 
				}
				
				playerIds[MAXPLAYERS-1] = -1;
				
				playerCount--;
				
				boolean b = League.delExtendedPlayer(pid);
				
				//#ifdef FEA_FIX_OTHER_TEAMS
				if (i < 11 && !b)
				{
					//FIX EKIPO
					short rep = -1;
					int pos = League.playerGetPosition(pid);
					for(int j = 10; j < MAXPLAYERS;j++)
					{
						if (playerIds[j] != -1 && League.playerGetPosition(playerIds[j]) == pos)
						{
							pos = j;rep = playerIds[pos];
							break;
						}
					}
					//short rep = playerIds[10+pos];
					
					//System.out.println("i: "+i+" rep: "+rep);
					if (rep != -1)
					{
						for(int j = pos; j > i;j--)
						{
							playerIds[j] = playerIds[j-1]; 
						}
						playerIds[i] = rep;
					}
				}
				//#endif
				//#ifdef DEBUG
				Debug.println("deleted :"+playerCount);
				//#endif
				
				
				
				return;
			}
		}
	}
}

