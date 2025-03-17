package com.mygdx.mongojocs.lma2007;

import java.io.*;
import java.util.Random;


public class League implements HandsetConstants

{
	GameCanvas gc;
	
/////////////////////////////////////////////////////////////////////////////////
//	 constantes del juego

	public static int[] leagueTeams = null;
	public static int[] positionGoalProbability = null;
	public static int[][] allTrainings = null;
	
	//final static int NUMLEAGUES = 6;
	//#ifdef FAKE_DB
	//#else
	final static int TUPLELENGTH = 3;
	final static int PLY_POSITION = 1;
	final static int PLY_QUALITY = 2;	
	//#endif
	
	Team[][] leagueJourneys;
	String [][]gameText;
	byte[] menuText;
	Team [][]teams;
	//int []numLeagueTeams;
	int userLeague;
	Team userTeam;
	byte playerSeasonGoal;
	int userTeamLeagueHistorial[] = new int[40];
	static int transfersRecord[][] = {};
	
	// PLAYER ARRAYS
	//#ifndef FAKE_DB
	static public byte[] rawPlayerNames;
	//#endif
	//#ifndef CALCULATE_OFFSET
	static public short[] rawPlayerNameOffsets;
	//#endif
	
	static public byte[] rawPlayerStats;
	//#ifndef REM_TOP_SCORERS
	static public byte[] rawPlayerGoals;
	//#endif
	//
	int currentJourney;
	int lastJourneyType;
	int currentWeek;
	int currentYear;
	final static int FIRSTWEEKDAY = 4;
	final static int FIRSTWEEKMONTH = 8;
	final static int FIRSTWEEKYEAR = 2006;
	//final static int firstJourneyMonth;
	
	

	//#ifndef REM_TOP11
	public short[] topPlayers = new short[11];
	//#endif
	//#ifndef REM_TOP_SCORERS
	public short[] topScorers = new short[40];
	//#endif
	
	
	public League(GameCanvas _gc, String[][] _gt) 
	{
		gc = _gc;
		gameText = _gt;		
	}
	


	
String[] getMenuText(int id)
{
	return gc.getMenuText(id);
}



	
	public void init()
	{
		
		gc.initMessaging();
//#ifdef DEBUG_PC_USED_MEM	
		gc.showMemUsed("despu�s crear mensajes");
//#endif		
		//com.mygdx.mongojocs.lma2007.Debug.println("league.load");
		//#ifndef FAKE_DB
		rawPlayerNames = null;
		//#endif
		rawPlayerStats = null;
		teams = null;
		System.gc();

		byte[] cons = gc.loadFile("/const");
			
			
		//com.mygdx.mongojocs.lma2007.Debug.println("load 1");
		// cargar constantes
		ByteArrayInputStream cais = new ByteArrayInputStream(cons); 
		DataInputStream cis = new DataInputStream(cais);

		try {
			//com.mygdx.mongojocs.lma2007.Debug.println("leagueTeams");
			leagueTeams = new int[6];
			for (int i = 0; i < 6; i++) {
				leagueTeams[i] = cis.readByte();
				//com.mygdx.mongojocs.lma2007.Debug.println("" +leagueTeams[i]);
			}
		
			//com.mygdx.mongojocs.lma2007.Debug.println("positionGoalProbability");
			positionGoalProbability = new int[4];
			for (int i = 0; i < 4; i++) {
				positionGoalProbability[i] = cis.readByte();
				//com.mygdx.mongojocs.lma2007.Debug.println("" +positionGoalProbability[i]);
			}

			//com.mygdx.mongojocs.lma2007.Debug.println("allTrainings");
			allTrainings = new int[7][];
			for (int j = 0; j < 7; j++) {
				allTrainings[j] = new int[5];
				for (int i = 0; i < 5; i++) {
					allTrainings[j][i] = cis.readByte();
					//com.mygdx.mongojocs.lma2007.Debug.println("" +allTrainings[j][i]);
				}
			}

			} catch (IOException e) {}
		
			cons = null;
						
			//#ifndef FAKE_DB
			rawPlayerNames = gc.loadFile("/plyn");
			//#endif
			rawPlayerStats = gc.loadFile("/plys");		

//#ifdef DEBUG_PC_USED_MEM	
			gc.showMemUsed("despu�s cargar players");
//#endif				
			
			byte[] tms = gc.loadFile("/tms");
			
			
			
			//com.mygdx.mongojocs.lma2007.Debug.println("load 9");
			// init estatico de ligas
			teams = new Team[League.leagueTeams.length][];
			int[] teamsIdx = new int[League.leagueTeams.length];
			for (int i = 0; i < League.leagueTeams.length; i++) 
			{
				teams[i] = new Team[League.leagueTeams[i]];
				teamsIdx[i] = 0;
			}
			int globalidx = 0;
			
			
			Team tmpTeams[] = null;;
			int totalTeams = 0;
			
			ByteArrayInputStream bais = new ByteArrayInputStream(tms); 
			DataInputStream dis = new DataInputStream(bais);
			int base = 0;
			try {
				
				totalTeams = dis.readUnsignedByte();
				tmpTeams = new Team[totalTeams]; 
				
				for (int i = 0; i < totalTeams; i++) {
					// nombre
					int ss = dis.readUnsignedByte();
					byte[] namea = new byte[ss];
					dis.readFully(namea);
					String namet = new String(namea);
					// total players
					int total = dis.readUnsignedByte();
					// league id
					int league = dis.readUnsignedByte();
					// pasta
					int money = dis.readUnsignedByte();
					Team t = new Team();
					tmpTeams[i] = t;
					
					t.load(namet, total, base, money);					
					
					t.teamLeague = (byte)league;
					t.globalIdx = (byte)globalidx;
					
					teams[league][teamsIdx[league]] = t;
					teamsIdx[league]++;
					
					base += total;
					globalidx++;
					//com.mygdx.mongojocs.lma2007.Debug.println("No se calculan stats 4");
				}
			} catch (IOException e) { }

			
			//#ifndef CALCULATE_OFFSET
			// construir offsets de nombres
			rawPlayerNameOffsets = new short[base];
			short accSize = 0;
			for (int i = 0; i < base; i++) {
				rawPlayerNameOffsets[i] = accSize;
				accSize += rawPlayerStats[i*TUPLELENGTH]; //ZNR
			}			
			//#endif
			
			// allocar goles
			//#ifndef REM_TOP_SCORERS
			rawPlayerGoals = new byte[base];
			//#endif
			
			
			// ordenar equipos por calidad(dinero)
			for (int i = 0; i < teams.length; i++) 
				sortTeams(teams[i], false);
			
			tms = null;
			//#ifndef REM_TEAMCOLORS
			byte[] cols = gc.loadFile("/tcol");
			ByteArrayInputStream tcais = new ByteArrayInputStream(cols); 
			dis = new DataInputStream(tcais);
						
			//int r, g, b;
			int col;
			try{

				dis.readByte();
				
				for(int i = 0; i < totalTeams; i++)
				{
					Team t = tmpTeams[i];
					
					
					col = dis.readInt();
					t.homeColor[0] = col;
					col = dis.readInt();
					t.homeColor[1] = col;
					col = dis.readInt();
					t.homeColor[2] = col;
					col = dis.readInt();
					t.homeColor[3] = col;
					
					col = dis.readInt();
					t.flagColor[0] = col;
					
					
					
					col = dis.readInt();
					t.awayColor[0] = col;
					col = dis.readInt();
					t.awayColor[1] = col;
					col = dis.readInt();
					t.awayColor[2] = col;
					col = dis.readInt();
					t.awayColor[3] = col;
					
					col = dis.readInt();
					t.flagColor[1] = col;
					
				}
			} catch (IOException e) {
				
				//#ifdef DEBUG
				Debug.println("*** ERROR LEYENDO COLORES");
				//#endif
				
			}
			
			//#endif
		}
	
	
	
	
	
//#ifndef REM_TOP_SCORERS
	static public int playerGetGoals(int i) 
	{
		return rawPlayerGoals[i];
	}

	static public void playerSetGoals(int i, int g) 
	{
		rawPlayerGoals[i] = (byte)g;
	}

	
	public void recordGoal(int p) 
	{
		//com.mygdx.mongojocs.lma2007.Debug.println("Registrando gol");
		
		playerSetGoals(p, playerGetGoals(p)+1);
		
		
		// si ya estaba el tio en el top 40 se reordena y punto
		for (int i = 0; i < 40; i++) {
			if (topScorers[i] == p) 
			{
				sortTopScorers();
				return;
			}
		}
		// sino se intenta insertar al final
		if (topScorers[39] == -1) {
			topScorers[39] = (short)p;
			sortTopScorers();
		} else {
			if (playerGetGoals(topScorers[39]) < playerGetGoals(p)) {
				topScorers[39] = (short)p;
				sortTopScorers();
			}
		}
		
	}
//#endif	

//#ifndef REM_TOP_SCORERS
	private void sortTopScorers() 
	{
		
		for (int i = 0; i < topScorers.length; i++) 
		{
			for (int j = i; j < topScorers.length; j++) 
			{
				int aig = topScorers[i] == -1 ? -9999 : playerGetGoals(topScorers[i]);
				int ajg = topScorers[j] == -1 ? -9999 : playerGetGoals(topScorers[j]);
				if (aig < ajg) 
				{
					short t = topScorers[j];
					topScorers[j] = topScorers[i];
					topScorers[i] = t;
				}
			}
		}
		
		/*
		for(int i = 0; i < topScorers.length;i++)
		{
			if (topScorers[i] != -1)
			{
				com.mygdx.mongojocs.lma2007.Debug.println(playerGetName(topScorers[i])+": "+playerGetGoals(topScorers[i]));
			}
		}*/
	}
//#endif	
	
	
	
	
	
	private void shuffleTeams(Team[] t) 
	{
		for (int i = 0; i < t.length; i++) 
		{
			int t2i = rand() % t.length;
			Team t2t = t[t2i];
			t[t2i] = t[i];
			t[i] = t2t;
		}
	}

	
	public void reset()
	{
		init();
		season = 0;
		transfersRecord = new int[][] {};
	}
	
	
	public void startSeason() 
	{
//#ifdef DEBUG
		Debug.println("*** startSeason *** startSeason *** startSeason *** startSeason *** startSeason ");
//#endif
		currentJourney = 0;
		currentWeek = 0;
		champEnded = false;
		
		season++;
		
		initChampions();
				
		//Pongo los stats de equipo a 0
		for(int i = 0; i < teams[userLeague].length; i++)
		{
			teams[userLeague][i].startSeason();			
		}
		
		
		shuffleTeams(teams[userLeague]);
		
		// LEAGUE
		int halfLeagueMatches = teams[userLeague].length-1;
		int totalTeams = teams[userLeague].length;
		int halfTeams = teams[userLeague].length/2;
		
		
		Team[] left = new Team[halfTeams];
		
		for (int i = 0; i < halfTeams; i++) {
			left[i] = teams[userLeague][i];
		}
		
		
		Team[] right = new Team[halfTeams];
		
		for (int i = halfTeams, j = right.length-1; i < totalTeams; i++, j--) {
			right[j] = teams[userLeague][i];
		}

		
		leagueJourneys = new Team[halfLeagueMatches*2][];
		int top = (teams[userLeague].length/2)-1;
		
		int phase = 0;
		
		for (int i = 0; i < halfLeagueMatches; i++) 
		{
			Team first = right[0];
			
			Team[] nr = new Team[right.length];
			System.arraycopy(right, 1, nr, 0, right.length - 1);
			right = nr;

			right[top] = left[top];
			left[top] = null;

			Team leftFirst = left[0];
			Team[] nl = new Team[left.length];
			System.arraycopy(left, 0, nl, 1, left.length - 1);
			nl[0] = leftFirst;
			left = nl;

			left[1] = first;

			leagueJourneys[i] = new Team[teams[userLeague].length];
			leagueJourneys[i+halfLeagueMatches] = new Team[teams[userLeague].length];
			
			for (int j = 0; j < left.length; j++) {
				if (phase == 0) {
					leagueJourneys[i][j*2] = left[j];
					leagueJourneys[i][j*2+1] = right[j];
					leagueJourneys[i+halfLeagueMatches][j*2] = right[j];
					leagueJourneys[i+halfLeagueMatches][j*2+1] = left[j];
				} else {
					leagueJourneys[i][j*2] = right[j];
					leagueJourneys[i][j*2+1] = left[j];
					leagueJourneys[i+halfLeagueMatches][j*2] = left[j];
					leagueJourneys[i+halfLeagueMatches][j*2+1] = right[j];
				}
			}
			phase = 1 - phase;
			/*if (phase == 0) {
				phase = 1;
			} else {
				phase = 0;
			}*/
		}
				
		
		if (!playingChamp)
		{
			//el equipo no esta en europa
			currentJourney++;			
		}
		
		//#ifndef REM_TOP_SCORERS
		for (int i = 0; i < topScorers.length; i++) 		
			topScorers[i] = -1;
		//#endif		
		//#ifndef REM_TOP11
		calculateTopPlayers();
		//#endif
		//#ifndef REM_TOP_SCORERS
		for (int i = 0; i < rawPlayerGoals.length; i++) 		
			rawPlayerGoals[i] = 0;
		//#endif
	}

	
	static public void playerSetName(int i, String newname)
	{
		int ex = extendedPlayer(i);
		
		if (ex == -1) return;
		
		userPlayerNames[ex] = newname;	
	}
	
	
	
	//#ifdef FAKE_DB

	//#endif
	
	
	
	
	static public String playerGetName(int i) 
	{
		int ex = extendedPlayer(i);
		
		if (ex == -1)
		{
			int len = rawPlayerStats[i*TUPLELENGTH];
			//#ifndef CALCULATE_OFFSET
			//#else
				//#ifdef FAKE_DB
				//#else
				int off = 0;
				
				for (int j = 0; j < i; j++) 
				{
					off += rawPlayerStats[j*TUPLELENGTH];
				}
				return new String(rawPlayerNames, off, len);
				//#endif
			//#endif
		}
		else
		{
			return userPlayerNames[ex];
		}
	}

	
	static public int playerGetPosition(int i) 
	{
		return rawPlayerStats[i*TUPLELENGTH+PLY_POSITION];
	}
	
	
	static public int playerGetQuality(int i) 
	{
		int ex = extendedPlayer(i);
		
		if (ex != -1)
		{
			int t = 0;
			for(int j = 0; j < 4;j++)
				t += playerGetSkill(i, j);
			return t;
		}
		else
			return rawPlayerStats[(i*TUPLELENGTH)+PLY_QUALITY];
		
	}

	
	final static int facTable[][] = {
		{10,10,20,60},
		{10,20,20,50},
		{20,45,25,10},
		{30,20,45,05}	
	};
	
	
	static public int playerGetSkill(int i, int s) 
	{		
		int ex = extendedPlayer(i);
		
		if (ex == -1)
		{
			int playerPosition = rawPlayerStats[i*TUPLELENGTH+PLY_POSITION];
			return (rawPlayerStats[i*TUPLELENGTH+PLY_QUALITY]*facTable[playerPosition][s])/100;
		}
		else
		{
			return userPlayerStats[ex][s+1];
		}		
	}
	
	
	static public int playerGetGameSkill(int i, int s) 
	{		
		int ex = extendedPlayer(i);
		
		int playerPosition = rawPlayerStats[i*TUPLELENGTH+PLY_POSITION];
		
		if (ex == -1)
		{
			
			return (rawPlayerStats[i*TUPLELENGTH+PLY_QUALITY]*facTable[playerPosition][s])/100;
		}
		else
		{
			//int playerPosition = rawPlayerStats[i*TUPLELENGTH+PLY_POSITION];
			int stat = userPlayerStats[ex][s+1];
			
			if (playerPosition != 0 && ex == 0) stat = stat/2;
			//if (playerPosition = 0 && ex == 0) stat = stat/2;
			
			return stat;
		}		
	}
	
	
	static public void playerSetSkill(int i, int s, int val) 
	{		
		int ex = extendedPlayer(i);
		
		if (ex != -1)
			userPlayerStats[ex][s+1] = val;
				
	}
	
	
	static public void playerIncSkill(int i, int s, int inc) 
	{
		int ex = extendedPlayer(i);
	
		if (ex != -1)
		{
			int value = userPlayerStats[ex][s+1] + inc;
			
			// CONSTRAINT
			if (value < 1) value = 1;
			
			// CONSTRAINT		
			int playerPosition = rawPlayerStats[i*TUPLELENGTH+PLY_POSITION];
			int originalStat =  (rawPlayerStats[i*TUPLELENGTH+PLY_QUALITY]*facTable[playerPosition][s])/100;
			
			//com.mygdx.mongojocs.lma2007.Debug.println("pgskill:"+value);
			//com.mygdx.mongojocs.lma2007.Debug.println("originalStat+10:"+(originalStat + 10));
			
			if (value > originalStat + 3)	value = originalStat + 3;
			
			
			//if (value > 100) value = 100;
			
			userPlayerStats[ex][s+1] = value;
		}
		// el caso contrario no deberia pasas NUNCA
		//#ifdef DEBUG
		else Debug.println (" -+- Esto NO DEBERIA pasar nunca Exception: playerIncSkill()");
		//#endif
	}
		
	
	
	final static int LEAGUEJOURNEY = 0;
	final static int CHAMPIONSJOURNEY = 1;
	int journeyType;
	
	public void startJourney()
	{			
		if (currentJourney%2 == 1)
			journeyType = LEAGUEJOURNEY;
		else
			journeyType = CHAMPIONSJOURNEY;
		
		currentWeek = currentJourney / 2;
		
		sortLeague();				
		
		countedGoals = new int[2];
		
		if (journeyType == LEAGUEJOURNEY)
		{
			if (currentWeek == leagueJourneys.length)
			{
				//System.out.println("Detecto fin 1");
				gc.deleteMessages();
				gc.addMessage(0, teams[userLeague][0].name+getMenuText(MENTXT_ENDLEAGUE)[0], getMenuText(MENTXT_LEAGUEWINNER)[0]+ teams[userLeague][0].name+getMenuText(MENTXT_LEAGUEWINNER)[1]+ teams[userLeague][0].points+getMenuText(MENTXT_LEAGUEWINNER)[2]);
				if (teams[userLeague][0] == userTeam) 
					gc.addMessage(0, getMenuText(MENTXT_WEARETHECHAMPIONS)[0], getMenuText(MENTXT_WEARETHECHAMPIONS)[1]);
				return;
			}
			journeyMatches = leagueJourneys[currentWeek];
		}
		else
		{
			// CHAMPIONSJOURNEY
			journeyMatches = champTeams;
			//#ifdef DEBUG
			Debug.println("JORNADA DE CHAMPIONS Champions 1");
			for (int i = 0; i < champTeams.length/2; i++)
			{
				Debug.println(champTeams[i].name+" VS "+champTeams[i+1].name);
			}
			//#endif
		}
		
		
		//
		//com.mygdx.mongojocs.lma2007.Debug.println("paso1");
		if (journeyType == CHAMPIONSJOURNEY && (!playingChamp || (playingChamp && !isChampJourney(currentJourney))))
		{			
			if (isChampJourney(currentJourney)/*journeyType == CHAMPIONSJOURNEY*/) preMatch();
				
			//el equipo no esta en europa
			currentJourney++;
			training();
			//startJourney();
			
				// ZNR PROVA OPTIMITZACIO 1
				//#ifdef DEBUG
				if (currentJourney%2 != 1) Debug.println("!!!ESTO NO DEBERIA PASAR NUNCA!!!");
				//#endif
					journeyType = LEAGUEJOURNEY;
				/*else
					journeyType = CHAMPIONSJOURNEY;
				*/
				currentWeek = currentJourney / 2;
				
				//#ifndef FEA_FAST_CALCS
				sortLeague();		
				//#endif
				
				// ZNR PROVA OPTIMITZACIO 1
				//if (journeyType == LEAGUEJOURNEY)
				//{
				if (currentWeek == leagueJourneys.length)
				{
					//System.out.println("Detecto fin 2");
					gc.deleteMessages();
					gc.addMessage(0, teams[userLeague][0].name+getMenuText(MENTXT_ENDLEAGUE)[0], getMenuText(MENTXT_LEAGUEWINNER)[0]+ teams[userLeague][0].name+getMenuText(MENTXT_LEAGUEWINNER)[1]+ teams[userLeague][0].points+getMenuText(MENTXT_LEAGUEWINNER)[2]);
					if (teams[userLeague][0] == userTeam) 
						gc.addMessage(0, getMenuText(MENTXT_WEARETHECHAMPIONS)[0], getMenuText(MENTXT_WEARETHECHAMPIONS)[1]);
					return;
				}
				journeyMatches = leagueJourneys[currentWeek];
		}
		
		if (byeChamp)
		{
			playingChamp = false;
			byeChamp = false;
		}
		
		
		
		
	}
	
	
	//boolean preSeasonPlaying = false;
	public Team[] journeyMatches; // PARTIDOS DE LA JORNADA
	public Team[] lastEuropeanMatches;
	public byte[] lastEuropeanResults;
	//public byte[] lastEuropeVictories;
	public Team[] lastLeagueMatches;
	public byte[] lastLeagueResults;
	Team userMatch[] = new Team[2];
	
	
	public void preMatch()
	{
		if (currentWeek >= leagueJourneys.length) return;
				
		if (journeyType == LEAGUEJOURNEY)
		{
			//com.mygdx.mongojocs.lma2007.Debug.println("RAMA 1");
			journeyMatches = leagueJourneys[currentWeek];
			// LEAGUE
			lastLeagueMatches = new Team[journeyMatches.length];
			lastLeagueResults = new byte[journeyMatches.length];
			System.arraycopy(journeyMatches, 0, lastLeagueMatches, 0, journeyMatches.length);
			
			// Preproceso
			for (int i = 0; i < journeyMatches.length / 2; i++) 
			{
				Team teamA = journeyMatches[i*2];
				Team teamB = journeyMatches[(i*2)+1];
				//#ifdef DEBUG
				Debug.println(teamA.name+" VS "+teamB.name);
				//#endif
				if (teamA == userTeam || teamB == userTeam)
				{
					//com.mygdx.mongojocs.lma2007.Debug.println("encontre partido");
					userMatch[0] = teamA;
					userMatch[1] = teamB;
				}
			}
		}
		else
		{
			//com.mygdx.mongojocs.lma2007.Debug.println("RAMA 2");
			if (!isChampJourney(currentJourney)) return;
			//com.mygdx.mongojocs.lma2007.Debug.println("RAMA LA");
			//CHAMPIONS
			lastEuropeanMatches = new Team[journeyMatches.length];
			lastEuropeanResults = new byte[journeyMatches.length];
			System.arraycopy(journeyMatches, 0, lastEuropeanMatches, 0, journeyMatches.length);
			
			// Preproceso
			if (playingChamp)
			{
				for (int i = 0; i < journeyMatches.length / 2; i++) 
				{
					Team teamA = journeyMatches[i*2];
					Team teamB = journeyMatches[(i*2)+1];
					
					if (teamA == userTeam || teamB == userTeam)
					{
						userMatch[0] = teamA;
						userMatch[1] = teamB;					
					}
				}
			}
			else
			{
				//#ifdef DEBUG
				Debug.println("SKIP CHAMPIONS");
				//#endif
				playJourney();
			}
			//com.mygdx.mongojocs.lma2007.Debug.println("TOMA CHAMP: "+ userMatch[0].name+userMatch[1].name);
		}
	}
	
	
	
	public boolean isChampJourney(int _jour)
	{
		if (_jour % 4 == 0 && _jour >= 2) return true;
		//com.mygdx.mongojocs.lma2007.Debug.println("pregutno por: "+_jour);
		return false;
	}
	
	
	public int getScorer(Team t)
	{
		int max = 0;
		int maxId = 0;
		
		for(int p = 1; p < 11;p++)
		{
			int pid = t.playerIds[p];							
			playerGetPosition(pid);							
			playerGetSkill(pid, SKILL_KICK);
			
			int v = positionGoalProbability[playerGetPosition(pid)] * playerGetSkill(pid, SKILL_KICK);
			v -= rand()%((v/2)+1);							
			if (v >= max)
			{
				max = v;
				maxId = pid;
			}
		}
		
		return maxId;
	}
	
	
	public void playJourney()
	{
		//com.mygdx.mongojocs.lma2007.Debug.println("playJourney");
		if (currentWeek >= leagueJourneys.length) return;
		
		
		if (journeyType == LEAGUEJOURNEY)
		{
			//#ifdef DEBUG
			Debug.println("### Partidos liga");
			//#endif
			
			// Preproceso
			for (int i = 0; i < journeyMatches.length / 2; i++) 
			{
				Team teamA = journeyMatches[i*2];
				Team teamB = journeyMatches[(i*2)+1];
				
				boolean isUserMatch = true;
				
				if (teamA != userTeam && teamB != userTeam)
				{
					//#ifdef DEBUG
					System.out.print(".");
					//#endif
					matchPlay(teamA, teamB);
					isUserMatch = false;	
				}
		
				//#ifdef DEBUG
				//com.mygdx.mongojocs.lma2007.Debug.println("... Partido jugado");
				//#endif
				
				lastLeagueResults[(i*2)] = (byte)teamA.matchGoals;
				lastLeagueResults[(i*2)+1] = (byte)teamB.matchGoals;
		
				
				if (isUserMatch)
				{
					if (recordOwnGoals)
					{	
						boolean finished = false;
						while (!finished)							
						{
							int k = rand()%2;
							Team t = journeyMatches[(i*2) + k];
							int begin = countedGoals[k];
							
							for(int g = begin; g < t.matchGoals; g++)
							{																		
								//	GOL: registrar gol
								gc.recordMatchGoal(getScorer(t), k);
								countedGoals[k]++;
								break;
							}
							finished = (countedGoals[0] == userMatch[0].matchGoals && countedGoals[1] == userMatch[1].matchGoals);  
						}
					}
				}
				else
				{
					//#ifndef REM_TOP_SCORERS
					for(int k = 0; k < 2; k++)
					{
						Team t = journeyMatches[(i*2) + k];
										
						for(int g = 0; g < t.matchGoals; g++)
						{
							recordGoal(getScorer(t));								
						}						
					}
					//#endif
				}
				
				
				
				
				if (teamA == userTeam || teamB == userTeam)
				{
					Team opp;
					if (userTeam == teamA) opp = teamB;
					else opp = teamA;
					
					String s = getMenuText(MENTXT_ROUND)[0]+" "+(currentWeek+1);
					
					if( userTeam.matchGoals > opp.matchGoals) s += getMenuText(MENTXT_MATCHRESULT)[0]; 
					else if( userTeam.matchGoals < opp.matchGoals) s += getMenuText(MENTXT_MATCHRESULT)[1];
					else s+= getMenuText(MENTXT_MATCHRESULT)[2];
						
					gc.addMessage(0, s, teamA.name+" "+teamA.matchGoals+ " - " +teamB.name+" "+teamB.matchGoals);
				}
				
		
				teamA.matchesPlayed++;
				teamB.matchesPlayed++;
				teamA.goalsScored += teamA.matchGoals;
				teamB.goalsScored += teamB.matchGoals;
				teamA.goalsReceived += teamB.matchGoals;
				teamB.goalsReceived += teamA.matchGoals;
				
				if (teamA.matchGoals > teamB.matchGoals)
				{
					//TODO: este 3 ha de depender de la liga
					teamA.points += 3;
					teamA.matchesWon++;
					teamB.matchesLost++;
				} 
				else if (teamA.matchGoals < teamB.matchGoals)
				{
					teamB.points += 3;
					teamA.matchesLost++;
					teamB.matchesWon++;
				}
				else
				{
					teamA.points++;
					teamA.matchesDrew++;				
					teamB.points++;
					teamB.matchesDrew++;
				}
			}
		}
		else
		{			
			//#ifdef DEBUG
			Debug.println("### Partidos champions");
			//#endif
			
			//CHAMPIONS JOURNEY
			for (int i = 0; i < journeyMatches.length / 2; i++) 
			{
				Team teamA = journeyMatches[i*2];
				Team teamB = journeyMatches[(i*2)+1];
				
				if (teamA != userTeam && teamB != userTeam)
					matchPlay(teamA, teamB);
				
				lastEuropeanResults[(i*2)] = (byte)teamA.matchGoals;
				lastEuropeanResults[(i*2)+1] = (byte)teamB.matchGoals;						
				
				if (teamA == userTeam || teamB == userTeam)
				{
					//#ifdef DEBUG
					System.out.println("ESTOY JUGANDO YO");
					//#endif
					Team opp;
					if (userTeam == teamA) opp = teamB;
					else opp = teamA;
					
					String s = getMenuText(MENTXT_EUROPEANROUND)[0];
					
					if( userTeam.matchGoals > opp.matchGoals) s += getMenuText(MENTXT_MATCHRESULT)[0]; 
					else if( userTeam.matchGoals < opp.matchGoals) s += getMenuText(MENTXT_MATCHRESULT)[1];
					else s+= getMenuText(MENTXT_MATCHRESULT)[2];
						
					gc.addMessage(0, s, teamA.name+" "+teamA.matchGoals+ " - " +teamB.name+" "+teamB.matchGoals);
					
					if (recordOwnGoals)
					{	
						boolean finished = false;
						while (!finished)							
						{
							int k = rand()%2;
							Team t = userMatch[k];
							int begin = countedGoals[k];
							
							for(int g = begin; g < t.matchGoals; g++)
							{																		
								//	GOL: registrar gol
								gc.recordMatchGoal(getScorer(t), k);
								countedGoals[k]++;
								break;
							}
							finished = (countedGoals[0] == userMatch[0].matchGoals && countedGoals[1] == userMatch[1].matchGoals);  
						}
					}
					
				}
				
			}
			
			
			
			
			// SIGUIENTE RONDA CHAMPIONS
			champRound++;
			//#ifdef DEBUG
			Debug.println("##### champRound: "+champRound);
			//#endif
			boolean newEuropeanRound = ((champRound%2) == 0 || champTeams.length == 2);
			//com.mygdx.mongojocs.lma2007.Debug.println("newEuropeanRound: "+newEuropeanRound);
			
			Team nct[];
			
			if (newEuropeanRound)
			{								
				nct = new Team[champTeams.length/2];
				
				boolean userTeamAlive = false;
				
				for (int i = 0; i < nct.length; i++)
				{
					Team teamA = champTeams[i*2];
					Team teamB = champTeams[(i*2)+1];
					
					//teamA.europeanMatchGoals = 0;
					//teamB.europeanMatchGoals = 0;
					
					int q1 = teamA.getQuality();
					int q2 = teamB.getQuality();
					
					// TODO: ESTO ES LA MANERA RAPIDA
					int totalA = teamA.matchGoals + teamA.europeanMatchGoals;
					int totalB = teamB.matchGoals + teamB.europeanMatchGoals;
					
					if (totalA == totalB)
					{
						totalA = (teamA.matchGoals) + (teamB.europeanMatchGoals*2);
						totalB = (teamB.matchGoals*2) + teamB.europeanMatchGoals;					
					}
					// HACK PARA LA FINAL
					if (champTeams.length == 2)
					{
						totalA = teamA.matchGoals;
						totalB = teamB.matchGoals;							
					}
					
					if (totalA > totalB) {
						nct[i] = teamA;						
					} else if (totalA < totalB) {
						nct[i] = teamB;						
					} else {
						if (q1 > q2) {
							nct[i] = teamA;
						} else {
							nct[i] = teamB;
						}
					}
					
					if (nct[i] == userTeam) userTeamAlive = true;
					
					/*
					if (nct[i] == userTeam) 
					{
						com.mygdx.mongojocs.lma2007.Debug.println("***"+teamA.matchGoals);
						com.mygdx.mongojocs.lma2007.Debug.println("***"+teamB.matchGoals);
						com.mygdx.mongojocs.lma2007.Debug.println("***"+teamA.europeanMatchGoals);
						com.mygdx.mongojocs.lma2007.Debug.println("***"+teamB.europeanMatchGoals);
					}*/
					
				}
				
				if (!userTeamAlive && playingChamp) 
				{
					byeChamp = true;
					//#ifdef DEBUG
					Debug.println("***Adios champions!!!");
					//#endif
					gc.addMessage(0, getMenuText(MENTXT_EUROPEANCHAMP)[0], getMenuText(MENTXT_EUROPEANCHAMP)[2]);
				}
				else if (userTeamAlive && playingChamp && champTeams.length > 2)
				{
					gc.addMessage(0, getMenuText(MENTXT_EUROPEANCHAMP)[0], getMenuText(MENTXT_EUROPEANCHAMP)[3]);
				}
			}
			else
			{
				nct = new Team[champTeams.length];
				
				for (int i = 0; i < champTeams.length/2; i++) 
				{
					Team teamA = champTeams[(i*2)];
					Team teamB = champTeams[(i*2)+1];
					
					teamA.europeanMatchGoals = teamA.matchGoals;
					teamB.europeanMatchGoals = teamB.matchGoals;
										
					nct[i*2] = teamB;
					nct[(i*2)+1] = teamA;
				}
			}
			
			if (!champEnded) champTeams = nct;
			
			if (!champEnded && champTeams.length == 1) 
			{
				
				// Winner of the European Championship
																
				if (champTeams[0] == userTeam)
				{
																
					gc.popupInitInfo(new String[] {gc.getMenuText(gc.MENTXT_WEARETHECHAMPIONS)[0], gc.getMenuText(gc.MENTXT_WEARETHECHAMPIONS)[1]});
					gc.popupWait();
					
					//#ifndef REM_BONUS_CODES
					if(!gc.checkBonusCodes[GameCanvas.BONUS_NOINJURIES]) {
						
						gc.checkBonusCodes[GameCanvas.BONUS_NOINJURIES] = true;
						
						gc.popupInitInfo(new String[] {gc.getMenuText(gc.MENTXT_UNLOCKED_BONUS_CODE)[0], gc.getMenuText(gc.MENTXT_BONUS_NO_INJURIES)[0]});
						gc.popupWait();
						
					}
					//#endif
					gc.addMessage(0, getMenuText(MENTXT_EUROPEANCHAMP)[0], getMenuText(MENTXT_EUROPEANCHAMP)[1]);
				}
				playingChamp = false;
				champEnded = true;
			}
			
				
			
		}
		
		
		//#ifdef DEBUG
		Debug.println("#### Partidos jugados");
		//#endif
	
		//#ifdef FEA_MATCH_REPORT
		//gc.reportText = gc.textosCreate( gc.loadFile("/EN_report.txt") );		
		gc.matchReport = matchReport();
		//#endif
		
		recordOwnGoals = true;
		
		//#ifndef REM_2DMATCH
		gc.goalsA = gc.goalsB = 0;
		//#endif
	}
	
	boolean recordOwnGoals  = true;
	
	public void nextJourney()
	{
		//NEXT JOURNEY
		
		//#ifdef DEBUG
		Debug.println("*** Register current position in league ranking");
		//#endif
		
		// Register current position in league ranking
		sortLeague();
		
		int myPos = 0;
		
		for(int i = 0; i < teams[userLeague].length; i++) {
		
			if(teams[userLeague][i] == userTeam) {
				
				myPos = i;
			}
		}
		
		
		userTeamLeagueHistorial[currentWeek] = myPos;
		
		// Actualiza jornadas restantes de sanci�n / lesi�n por jugador
		
		if(journeyType == LEAGUEJOURNEY || !playingChamp) {
			
			for(int i = 0; i < userTeam.playerCount; i++) {
				
				int playerIdx = extendedPlayer(userTeam.playerIds[i]);
				
				if(userPlayerStats[playerIdx][SANCTION_JOURNEYS] > 0) {
					
					userPlayerStats[playerIdx][SANCTION_JOURNEYS]--;
				}
				
				if(userPlayerStats[playerIdx][INJURY_JOURNEYS] > 0) {
					
					userPlayerStats[playerIdx][INJURY_JOURNEYS]--;
				}				
			}
		}
		
		
		// End
		
		
			
		lastJourneyType = journeyType;
		currentJourney++;
		
		//#ifdef DEBUG
		Debug.println("*** Training");
		//#endif
		
		
		training();
		
		//#ifdef DEBUG
		Debug.println("*** StartJourney");
		//#endif
		
		
		startJourney();	
			
	}
	
	
	public static Random random = null;
	
	
	public int rand() 
	{
		if (random == null) 		
			random = new Random(System.currentTimeMillis());
		
		int i = random.nextInt();
		
		return i < 0 ? -i : i;
	}


	
	
	
	
	
	
	
	
//	*******************
//	-------------------
//	Calendar - Engine
//	===================
//	*******************
	
	
//#ifndef REM_CALENDAR
	
	final static int FEBRUARY = 1;   /* special month during leap years */

	final static int DaysInMonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	  // Month and year entered by user.
	  int userMonth;
	  int userYear;

     int NumberRowsNeeded(int year, int month)
	  /*
	     USE:  Calculates number of rows needed for calendar.
	     IN:   year = given year after 1582 (start of the Gregorian calendar).
	           month = 0 for January, 1 for February, etc.
	     OUT:  Number of rows: 5 or 6, except for a 28 day February with
	           the first of the month on Sunday, requiring only four rows.
	  */
	    {
	    int firstDay;       /* day of week for first day of month */
	    int numCells;       /* number of cells needed by the month */
	  
	    /* Start at 1582, when modern calendar starts. */
	    if (year < 1582) return (-1);
	  
	    /* Catch month out of range. */
	    if ((month < 0) || (month > 11)) return (-1);
	  
	    /* Get first day of month. */
	    firstDay = CalcFirstOfMonth(year, month);
	  
	    /* Non leap year February with 1st on Sunday: 4 rows. */
	    if ((month == FEBRUARY) && (firstDay == 0) && !IsLeapYear(year))
	      return (4);
	  
	    /* Number of cells needed = blanks on 1st row + days in month. */
	    numCells = firstDay + DaysInMonth[month];
	  
	    /* One more cell needed for the Feb 29th in leap year. */
	    if ((month == FEBRUARY) && (IsLeapYear(year))) numCells++;
	  
	    /* 35 cells or less is 5 rows; more is 6. */
	    return ((numCells <= 35) ? 5 : 6);
	 } // NumberRowsNeeded
	  
	  
	  
	  static int CalcFirstOfMonth(int year, int month)
	  /*
	     USE:  Calculates day of the week the first day of the month falls on.
	     IN:   year = given year after 1582 (start of the Gregorian calendar).
	           month = 0 for January, 1 for February, etc.
	     OUT:  First day of month: 0 = Sunday, 1 = Monday, etc.
	  */
	    {
	    int firstDay;       /* day of week for Jan 1, then first day of month */
	    int i;              /* to traverse months before given month */
	  
	    /* Start at 1582, when modern calendar starts. */
	    if (year < 1582) return (-1);
	  
	    /* Catch month out of range. */
	    if ((month < 0) || (month > 11)) return (-1);
	  
	    /* Get day of week for Jan 1 of given year. */
	    firstDay = CalcJanuaryFirst(year);
	  
	    /* Increase firstDay by days in year before given month to get first day
	     * of month.
	     */
	    for (i = 0; i < month; i++)
	      firstDay += DaysInMonth[i];
	  
	    /* Increase by one if month after February and leap year. */
	    if ((month > FEBRUARY) && IsLeapYear(year)) firstDay++;
	  
	    /* Convert to day of the week and return. */
	    return (firstDay % 7);
	  } // CalcFirstOfMonth
	  
	  
	  
	  
	  static boolean IsLeapYear(int year)
	  /*
	     USE:  Determines if given year is a leap year.
	     IN:   year = given year after 1582 (start of the Gregorian calendar).
	     OUT:  TRUE if given year is leap year, FALSE if not.
	     NOTE: Formulas capture definition of leap years; cf CalcLeapYears().
	  */
	    {
	  
	    /* If multiple of 100, leap year iff multiple of 400. */
	    if ((year % 100) == 0) return((year % 400) == 0);
	  
	    /* Otherwise leap year iff multiple of 4. */
	    return ((year % 4) == 0);
	    } // IsLeapYear
	  
	  
	  
	  static int CalcJanuaryFirst(int year)
	  /*
	     USE:  Calculate day of the week on which January 1 falls for given year.
	     IN:   year = given year after 1582 (start of the Gregorian calendar).
	     OUT:  Day of week for January 1: 0 = Sunday, 1 = Monday, etc.
	     NOTE: Formula starts with a 5, since January 1, 1582 was a Friday; then
	           advances the day of the week by one for every year, adding the
	           number of leap years intervening, because those years Jan 1
	           advanced by two days. Calculate mod 7 to get the day of the week.
	  */
	    {
	    /* Start at 1582, when modern calendar starts. */
	    if (year < 1582) return (-1);
	  
	    /* Start Fri 01-01-1582; advance a day for each year, 2 for leap yrs. */
	    return ((5 + (year - 1582) + CalcLeapYears(year)) % 7);
	    } // CalcJanuaryFirst
	  
	  
	  
	  
	  
	  
	  
	  
    static int CalcLeapYears(int year)
	  /*
	     USE:  Calculate number of leap years since 1582.
	     IN:   year = given year after 1582 (start of the Gregorian calendar).
	     OUT:  number of leap years since the given year, -1 if year < 1582
	     NOTE: Count doesn't include the given year if it is a leap year.
	           In the Gregorian calendar, used since 1582, every fourth year
	           is a leap year, except for years that are a multiple of a
	           hundred, but not a multiple of 400, which are no longer leap
	           years. Years that are a multiple of 400 are still leap years:
	           1700, 1800, 1990 were not leap years, but 2000 will be.
	  */
	    {
	    int leapYears;      /* number of leap years to return */
	    int hundreds;       /* number of years multiple of a hundred */
	    int fourHundreds;   /* number of years multiple of four hundred */
	  
	    /* Start at 1582, when modern calendar starts. */
	    if (year < 1582) return (-1);
	  
	    /* Calculate number of years in interval that are a multiple of 4. */
	    leapYears = (year - 1581) / 4;
	  
	    /* Calculate number of years in interval that are a multiple of 100;
	     * subtract, since they are not leap years.
	     */
	    hundreds = (year - 1501) / 100;
	    leapYears -= hundreds;
	  
	    /* Calculate number of years in interval that are a multiple of 400;
	     * add back in, since they are still leap years.
	     */
	    fourHundreds = (year - 1201) / 400;
	    leapYears += fourHundreds;
	  
	    return (leapYears);
	} // CalcLeapYears
	

	public int getMonth(int _week, int _dayInWeek)
	{
		int w = _week;				
		int year = FIRSTWEEKYEAR + season - 1;
		int curMonth = FIRSTWEEKMONTH;
		
		int day = CalcFirstOfMonth(year, curMonth);
		day = FIRSTWEEKDAY + (_dayInWeek);
		
		day += _week*7;
		
		while (day > DaysInMonth[curMonth])
		{
			day -= DaysInMonth[curMonth];
			curMonth++;
			if (curMonth >= 12){year++;curMonth = 0;}
		}
		
		
		
		
		//if (day != 1) day = ((8-day)%7)+1;
		
		
		/*
		while (w > 1)
		{
			while (w > 1 && day <= DaysInMonth[curMonth])
			{
				day += 7;w--;
			}
			if (day > DaysInMonth[curMonth])
			{
				int t = day - DaysInMonth[curMonth];
				curMonth++;
				if (curMonth >= 12){year++;curMonth = 0;}
				day = CalcFirstOfMonth(year, curMonth);
				day += t;
				//if (day != 1) day = ((8-day)%7)+1;				
			}
		}
		*/
		return curMonth;
		
		//FIRSTWEEKMONTH + _week, FIRSTWEEKYEAR + season
	}
	
	
	int lastDay;
	
	public String[] getCurrentDate(int week, int _day)
	{
		
		final int STARTMONTH = 8;
		final int STARTYEAR = 2006 + season - 1;
		
		int d = week+1;
		
		int curMonth = STARTMONTH;
		int year = STARTYEAR;
		int day = CalcFirstOfMonth(year, curMonth);
		//com.mygdx.mongojocs.lma2007.Debug.println("primer dia: "+day);
		if (day != 1) day = ((8-day)%7)+1;
		
		//if (champJourney) d += 1;//ay += 7;
		//if (lastChampJourney) day += 3;
		
		day += _day;
		
		while (d > 1)
		{
			while (d > 1 && day <= DaysInMonth[curMonth])
			{
				day += 7;d--;
			}
			if (day > DaysInMonth[curMonth])
			{
				int t = day - DaysInMonth[curMonth];
				curMonth++;
				if (curMonth >= 12){year++;curMonth = 0;}
				//day = CalcFirstOfMonth(year, curMonth);
				day = t;
				//if (day != 1) day = ((8-day)%7)+1;
				//if (day > 0) day = ((8-day)%7)+1; 
			}
		}
		String m = gameText[HandsetConstants.TEXT_MONTH][curMonth];
		String sday;
		
		//if (lastChampJourney) sday = com.mygdx.mongojocs.lma2007.GameCanvas.getGameText(com.mygdx.mongojocs.lma2007.HandsetConstants.TEXT_WEEKDAY)[1];
		//else sday = com.mygdx.mongojocs.lma2007.GameCanvas.getGameText(com.mygdx.mongojocs.lma2007.HandsetConstants.TEXT_WEEKDAY)[0];
		sday = gameText[HandsetConstants.TEXT_WEEKDAY][_day]; 
		
		String ret[] = new String[4];
		
		ret[0] = ""+year;
		ret[1] = ""+m;
		ret[2] = ""+day;
		ret[3] = ""+sday;
		
		lastDay = day; 
		return ret;//sday + ", " + m + ", " + day;
			            
			            
		//int firstDay = CalcFirstOfMonth(int year, int month);
	}
	
	//#endif
	
	static int season = 0;
	
//	<=- <=- <=- <=- <=-
	
	
	public void sortLeague() 
	{
		Team[] sortedLeagueList = new Team[teams[userLeague].length];
		System.arraycopy(teams[userLeague], 0, sortedLeagueList, 0, teams[userLeague].length);
		sortTeams(sortedLeagueList, true);
		teams[userLeague] = sortedLeagueList;
	}

	
	// Para la liga
	// 
	public void sortTeams(Team tl[], boolean inleague) 
	{
		boolean swap;
		
		for (int i = 0; i < tl.length; i++) 
		{
			for (int j = i; j < tl.length; j++) 
			{
				if (inleague)
					swap = tl[j].compareLeagueStatus(tl[i]) == 1;
				else
					swap = tl[i].cash < tl[j].cash;
				
				if (swap) 
				{
					Team t = tl[j];
					tl[j] = tl[i];
					tl[i] = t;
				}
			}
		}
	}

	
	public int prob(int sum, int rest, int max)
	{
		int total = 0;
		
		for (int i = 0; i < sum; i++)
			total += rand()%2;
		
		for (int i = 0; i < rest; i++)
			total -= rand()%2;
		
		if (total > max) total = max;
		if (total < 0) total = 0;
		
		return total;
			
	}
	
	/*
	 int playerIdx = league.extendedPlayer(textMatchPlayerAtt);
			
			if(playerIdx >= 0) {
				
				//#ifndef NOPLAYERELIMINATION
				
				league.userPlayerStats[playerIdx][league.INJURY_JOURNEYS] = 2 + rnd(4);
				
				//#endif
			}
	 */
	final static int randomFactor = 12; // era 10
	
	int matchInjured[];
	int matchSuspended[];
	
	
	
	public void postSkipMatch()
	{
		
		// Expulsados
		for(int i = 0 ; i < matchSuspendedCount;i++)
		{
			//NO controlo fuera de rango
			int exId = extendedPlayer(matchSuspended[i]);
			userPlayerStats[exId][SANCTION_JOURNEYS] = 2;
		}
		
		// Lesionados
		for(int i = 0 ; i < matchInjuredCount;i++)
		{
			injurePlayer(matchInjured[i]);
			//int exId = extendedPlayer(matchInjured[i]);
			//userPlayerStats[exId][INJURY_JOURNEYS] = 2 + rand()%4;
		}
				
		recordOwnGoals = true;
	}
	
	public boolean playerSanctioned(int pid)
	{
		int exid = extendedPlayer(pid);
		
		if (exid != -1)
			return (userPlayerStats[exid][SANCTION_JOURNEYS] > 0);
		else return true;
		
	}
	
	public boolean playerInjured(int pid)
	{
		int exid = extendedPlayer(pid);
		
		if (exid != -1)
			return (userPlayerStats[exid][INJURY_JOURNEYS] > 0);
		else return true;
		
	}	
		
	public boolean playerCanPlay(int pid)
	{
		int exid = extendedPlayer(pid);
		
		if (exid != -1)
			return (userPlayerStats[exid][SANCTION_JOURNEYS] == 0 &&
					userPlayerStats[exid][INJURY_JOURNEYS] == 0);
		else return true;
		
	}
	

	
	
	public void reMatchPlay(Team a, Team b, int precent, int curGoalsA, int curGoalsB)
	{
		rematch = true;
		
		matchPlay(a, b);
		
		a.matchGoals = (byte)Math.max(curGoalsA, a.matchGoals);
		b.matchGoals = (byte)Math.max(curGoalsB, b.matchGoals);
		//#ifdef DEBUG
		Debug.println("pero ahora es: "+a.matchGoals+" - "+b.matchGoals);
		//#endif
		rematch = false;
	}
	
	boolean rematch;
	int matchInjuredCount;
	int matchSuspendedCount;
	
	public void matchPlay(Team a, Team b)
	{
		int i;
		int matchGoalsA = 0;
		int matchGoalsB = 0;

		int qA = a.getQuality();
		int qB = b.getQuality();
		qB -= qB/10;

		int offA = a.getOffensiveQuality();
		int offB = b.getOffensiveQuality();

		int defA = a.getDefensiveQuality();
		int defB = b.getDefensiveQuality();
		
		if (!rematch && (a.isUserTeam() || b.isUserTeam()))
		{				
			matchInjuredCount = prob(2+gc.trainingStyle, 4, 2);  
			matchSuspendedCount = prob(5, 7+(gc.custSkillNum[3]/2), 2);
					
			matchInjured = new int[matchInjuredCount];
			matchSuspended = new int[matchSuspendedCount];
			
			int pId;
			
			// EXPULSADOS
			for (int j = 0; j < matchSuspendedCount;j++)
			{
				do {
					
					pId = 1+rand()%10;
					pId = userPlayerStats[pId][EX_PLAYERID];
					                           
				} while (!playerCanPlay(pId));
			
				matchSuspended[j] = pId;
				
			}
			
			// LESIONADOS
			for (int j = 0; j < matchInjuredCount;j++)
			{
				
				do {
					
					pId = 1+rand()%10;
					pId = userPlayerStats[pId][EX_PLAYERID];
					
				} while (!playerCanPlay(pId));
					
				matchInjured[j] = pId;//userTeam.playerIds[pId];				
			}
			
			//#ifdef DEBUG
			Debug.println("##########matchInjuredCount: "+matchInjuredCount);
			Debug.println("##########matchSuspendedCount: "+matchSuspendedCount);
			//#endif
		}
		
		if (qA > qB+10)
		{
			//qA = qA/2;
			defA = defA/2;
		}

		if (qB > qA+10)
		{
			//qB = qB/2;
			defB = defB/2;
		}

		int t = qA + qB;

		qA = (qA*50)/t;
		qB = (qB*50)/t;

		//SI DEF GRANDE DE UNO Y ATT GRANDE DEL OTRO...
		if (offA > defB)
		{
			int dif = offA - defB;
			offA -= (5*dif)/12;//dif/2;dif/2;//(7*dif)/12;//dif/2;
		}

		if (offB > defA)
		{
			int dif = offB - defA;
			offB -= (5*dif)/12;//dif/2;//dif/2;//(7*dif)/12;//dif/2;
		}


		matchGoalsA = qA+offA-qB-defB;
		matchGoalsB = qB+offB-qA-defA;

		//com.mygdx.mongojocs.lma2007.Debug.println("A: "+matchGoalsA);
		//com.mygdx.mongojocs.lma2007.Debug.println("B: "+matchGoalsB);
			
		matchGoalsA += rand()%(randomFactor+10) - rand()%randomFactor - rand()%qA + rand()%qB;
		matchGoalsB += rand()%(randomFactor+10) - rand()%randomFactor - rand()%qB + rand()%qA;

		if (matchGoalsA < 0) matchGoalsA = 0;
		if (matchGoalsB < 0) matchGoalsB = 0;
		
		a.matchGoals = (byte)(matchGoalsA/10);
		b.matchGoals = (byte)(matchGoalsB/10);
		
		if (a.matchGoals > 5) a.matchGoals = 5;
		if (b.matchGoals > 5) b.matchGoals = 5;
	}
	
	
	
	
	
	
	
	// EXTENDED PLAYER INIT
	
	static int userPlayerStats[][] = new int[Team.MAXPLAYERS][7];
	int userPlayerContracts[][] = new int[Team.MAXPLAYERS][5];
	static String userPlayerNames[] = new String[Team.MAXPLAYERS];
	
	final static int EX_PLAYERID = 0;
	
	final static int SKILL_KICK = 0;
	final static int SKILL_PASS = 1;
	final static int SKILL_SPEED = 2;
	final static int SKILL_DEFENCE = 3;
	
	final static int SANCTION_JOURNEYS = 5;
	final static int INJURY_JOURNEYS = 6;
	// int i = extendedPlayer(playerId)
	// i == -1 => error!!! 
	// userPlayerStats[i][JORNADES_EXPULSAT]
	
	//final static int SKILL_DEFENCE = 4;
	
	
	void initExtendedPlayers(Team t)
	{
		for(int i = 0;i < userPlayerStats.length;i++)	
		{
			userPlayerStats[i][EX_PLAYERID] = -1;
			userPlayerStats[i][SANCTION_JOURNEYS] = 0;
			userPlayerStats[i][INJURY_JOURNEYS] = 0;		
		}
		// pongo los skills primero para evitar usar el hash
		for(int i = 0;i < t.playerCount;i++)		
		{
			userPlayerStats[i][SKILL_KICK+1] = playerGetSkill(t.playerIds[i], SKILL_KICK);
			userPlayerStats[i][SKILL_PASS+1] = playerGetSkill(t.playerIds[i], SKILL_PASS);
			userPlayerStats[i][SKILL_SPEED+1] = playerGetSkill(t.playerIds[i], SKILL_SPEED);
			userPlayerStats[i][SKILL_DEFENCE+1] = playerGetSkill(t.playerIds[i], SKILL_DEFENCE);
			//userPlayerStats[i][SKILL_KICK+1] = playerGetSkill(t.playerIds[i], SKILL_KICK);
			userPlayerNames[i] = playerGetName(t.playerIds[i]);
		}
		
		//Inicializo Id's
		for(int i = 0;i < t.playerCount;i++)		
		{
			userPlayerStats[i][EX_PLAYERID] = t.playerIds[i];
			/*if (userPlayerStats[i][EX_PLAYERID] != -1)
			{
				for(int j = 0;j < 4;j++)
					playerIncSkill(t.playerIds[i], j, -1);
			}*/
		}
		
		/*for(int i = 0;i < t.playerIds.length;i++)	
			if (userPlayerStats[i][EX_PLAYERID] == -1)
				for(int j = 0;j < 4;j++)
					playerIncSkill(t.playerIds[i], j, -2);*/
	}

	
	static boolean addExtendedPlayer(int pid)
	{
		//#ifdef DEBUG
		Debug.println("trying extended player added ("+ pid+", )");
		//#endif
		
		for(int i = 0;i < userPlayerStats.length;i++)
			if (userPlayerStats[i][EX_PLAYERID] == pid) return false;
				
		for(int i = 0;i < userPlayerStats.length;i++)
		{
			//#ifdef DEBUG
			System.out.println(userPlayerNames[i]+":"+userPlayerStats[i][EX_PLAYERID]+ " VS "+pid);
			//#endif
			
			if (userPlayerStats[i][EX_PLAYERID] == -1)
			{
				userPlayerStats[i][SKILL_KICK+1] = playerGetSkill(pid, SKILL_KICK);
				userPlayerStats[i][SKILL_PASS+1] = playerGetSkill(pid, SKILL_PASS);
				userPlayerStats[i][SKILL_SPEED+1] = playerGetSkill(pid, SKILL_SPEED);
				userPlayerStats[i][SKILL_DEFENCE+1] = playerGetSkill(pid, SKILL_DEFENCE);
				userPlayerNames[i] = playerGetName(pid);
				userPlayerStats[i][EX_PLAYERID] = pid;
				//#ifdef DEBUG
				Debug.println("extended player added ("+ i+", "+pid+")");
				//#endif
				return true;
			}
		}	
		return false;
	}
	
	
	static boolean delExtendedPlayer(int pid)
	{		
		for(int i = 0;i < userPlayerStats.length;i++)
		{
			if (userPlayerStats[i][EX_PLAYERID] == pid)
			{
				userPlayerStats[i][EX_PLAYERID] = -1;
				//#ifdef DEBUG
				Debug.println("extended player deleted ("+ i+", "+pid+")");
				//#endif
				return true;
			}
		}
		return false;
	}
	
	
	// es un player currado? si es que si retorna el id, sino -1
	static int extendedPlayer(int playerId)
	{
		for(int i = 0; i < userPlayerStats.length;i++)
		{
			if (userPlayerStats[i][EX_PLAYERID] == playerId) 
				return i;
		}
		return -1;
	}
	//playerGetSkill
	
	// EXTENDED PLAYER END



public void training()
{
//#ifdef DEBUG
	Debug.println("*** AutoTraining Style");
//#endif
	
	if (gc.autoManagement[GameCanvas.AUTO_TRAINING_STYLE]) gc.autoTrainingStyle();	

	
//#ifdef DEBUG
	Debug.println("*** AutoTraining Schedule");
//#endif
	
	if (gc.autoManagement[GameCanvas.AUTO_TRAINING_SCHEDULE]) 
	{
		for(gc.trainingLine = 0; gc.trainingLine < 4;gc.trainingLine++)
			gc.autoTrainingSchedule();
	}
	
	
//#ifdef DEBUG
	Debug.println("*** Taining");
//#endif
	
	//ZNR: NEW TRAINING
	for (int i = 0; i < userTeam.playerIds.length; i++) 
	{					
		//SIDO: Los lesionados no entrenan
		if (userTeam.playerIds[i] != -1)
		{
			//#ifdef DEBUG
			//com.mygdx.mongojocs.lma2007.Debug.println("*** Player "+i);
			//#endif
			
			int pos = playerGetPosition(userTeam.playerIds[i]);
			for(int stat = 1; stat < 5;stat++)
			{		
				int statTraining = 0; 
				int ps = playerGetSkill(userTeam.playerIds[i], stat-1);
				
				for(int exercise = 0; exercise < 7;exercise++)
				{
					int statInc = allTrainings[exercise][stat];
					
					int timeTraining = GameCanvas.trainingSchedule[pos][exercise];
					
					int res = (statInc*timeTraining*2); //2
					int extra = 0;
					if (gc.employees != null) extra = (gc.employees[GameCanvas.EMPLOYEES_COACH]*6); // era 10
					else extra = gc.custSkillNum[1];
					
					statTraining += res - rand()%(res+40+ps) + rand()%(ps+40+extra); //-75//
				}
				//int t = statTraining + rand()%25; //50
				statTraining += gc.trainingStyle*10;
				
				int fac = 200;
				
				fac = fac + (ps*2);
				
				statTraining = statTraining / fac; //SERIA 100, pero lo cambio por tunning
				
				//#ifndef REM_BONUS_CODES
				if (gc.bonusCodes[GameCanvas.BONUS_TRAINING] != 0) statTraining = currentWeek%2;
				//#endif
				if (userPlayerStats[i][INJURY_JOURNEYS] > 0) statTraining = -rand()%2;
				
				//TODO: random pq el numero puede ser 0
				playerIncSkill(userTeam.playerIds[i], stat-1, statTraining);
								
				
			}
			
		}
	}
}
//ZNR: END NEW TRAINING



//*******************
//-------------------
//Champions - Engine
//===================
//*******************

boolean playingChamp;
boolean byeChamp;
Team[] champTeams;
int champRound;
final static int EUROPEANCLUBSCOUNT[] = {3,1,3,3,3,3};


public int leagueOrdinal(Team t) 
{
	for (int i = 0; i < teams[userLeague].length; i++) 	
		if (t == teams[userLeague][i]) return i;
	
	return 0;
}




public void initChampions()
{	
	champRound = 0;
	
	int k = 0;

	playingChamp = leagueOrdinal(userTeam) < EUROPEANCLUBSCOUNT[userLeague];
	
	champTeams = new Team[16];
	for (int i = 0; i < teams.length; i++) 
	{
		for (int j = 0; j < EUROPEANCLUBSCOUNT[i]; j++) 
		{
			champTeams[k] = teams[i][j];				
			k++;
		}
	}
		
	shuffleTeams(champTeams);
}

/*
public com.mygdx.mongojocs.lma2007.Team[] getChampMatches()
{
	matches = new 
	
	return matches;
}
*/

public int getTeamLeaguePosition(Team t)
{
	int pos = -1;
	
	for(int i = 0; i < teams[userLeague].length; i++) {
	
		if(teams[userLeague][i] == t) {
		
			pos = i;
		}	
	}
	
	return pos;
}

public boolean seasonGoalAcomplished() {
	
	boolean succeed;
	
	int teamPosition = getTeamLeaguePosition(userTeam);
	//#ifdef DEBUG
	Debug.println("You placed "+teamPosition+"st");
	//#endif
	switch(playerSeasonGoal) {
		
		case 0 : succeed = teamPosition < 4; break;
		case 1 : succeed = teamPosition < 6; break;
		case 2 : succeed = teamPosition < teams[userLeague].length / 2; break;
		case 3 : succeed = teamPosition < teams[userLeague].length - 8; break;
		case 4 : succeed = teamPosition < teams[userLeague].length - 4; break;
		default : succeed = true;			
	}
	
	return succeed;
}

//<=- <=- <=- <=- <=-




public Team getTeamFromGlobalIdx(int idx) 
{
	for (int i = 0; i < teams.length; i++) 
	{
		for (int j = 0; j < teams[i].length; j++) 
		{
			Team t = teams[i][j];
			if (t.globalIdx == idx)
				return t;
		}
	}
	return null;
}


public Team getTeamWherePlays(short playerId) {
	
	for (int i = 0; i < teams.length; i++) 
	{
		for (int j = 0; j < teams[i].length; j++) 
		{
			Team t = teams[i][j];
			
			for(int k = 0; k < t.playerCount; k++) {
			
				if(t.playerIds[k] == playerId) {
				
					return t;
				}
			}			
		}
	}
	return null;
	
}

public Team findTeamByQuality(int q, Team invalidTeam) {
	
	Team t = null;
	int tolerance = 1;
	
	do {

		for(int i = 0; i < teams.length; i++) {
			
			for(int j = 0; j < teams[i].length; j++) {
				
				if(Math.abs(teams[i][j].getQuality() - q) <= tolerance) {
				
					t = teams[i][j];
					return t;
				}
			}
		}
		
		tolerance++;
		
	} while(t == null || t == invalidTeam);
	
	return t;
}

public static void transferRegister(short playerId, byte teamIdx) {
	
		
	for(int i = 0; i < transfersRecord.length; i++) {
	
		if(transfersRecord[i][0] == playerId) {
		
			transfersRecord[i][1] = teamIdx;
			
			return;
		}
	}

	int auxRecord[][] = new int[transfersRecord.length + 1][2];
	
	System.arraycopy(transfersRecord,0,auxRecord,0,transfersRecord.length);
	
	auxRecord[transfersRecord.length] = new int[] {playerId, teamIdx};
	
	transfersRecord = auxRecord;
	
	/*for(int i = 0; i < transfersRecord.length; i++) {
		
		com.mygdx.mongojocs.lma2007.Debug.println("Transfer : "+playerGetName(transfersRecord[i][0])+" plays with "+transfersRecord[i][1]);
	}*/
}


//#ifndef REM_TOP11
// RUTINAS TOP 11
	public void calculateTopPlayers()
	{
		for (int i = 0; i < topPlayers.length; i++) 		
			topPlayers[i] = -1;
				
		for(int i = 0; i < teams.length; i++)
		{
			for(int j = 0; j < teams[i].length; j++)
			{
				for(int k = 0; k < teams[i][j].playerCount;k++)
				{
					int pid = teams[i][j].playerIds[k];
					insertAndSortTopPlayer(pid);										
				}
			}
		}
	}
	
	public void insertAndSortTopPlayer(int pid)
	{		
		int q = playerGetQuality(pid);		
		
		for(int i = 0; i < topPlayers.length;i++)
		{
			if (topPlayers[i] == -1 || q >= playerGetQuality(topPlayers[i]))
			{
				for(int j = topPlayers.length-1; j > i;j--)
				{
					topPlayers[j] = topPlayers[j-1];				
				}
				
				topPlayers[i] = (short)pid;
				return;
			}
		}
	}
//END RUTINAS TOP 11
//#endif

public boolean injurePlayer(int pid)
{
	//#ifndef REM_BONUS_CODES
	if (gc.bonusCodes[GameCanvas.BONUS_NOINJURIES] != 0) return false; 
	//#endif
	
	int t = 0;
	for(int i = 0; i < userTeam.playerCount;i++)
		if (!playerCanPlay(userTeam.playerIds[i])) t++;
	
	if (userTeam.playerCount-t < 15) return false;
		
	int playerIdx = extendedPlayer(pid);
	
	if(playerIdx >= 0) 
	{
		userPlayerStats[playerIdx][INJURY_JOURNEYS] = 2 + gc.rnd(4);
		return true;
	}
	return false;
}
//#ifndef REM_FOOTBALL_ONE
String f1Title;
String f1Body;
boolean enableMsg = true;

public boolean footballOneMatch()
{
	boolean ret = false;
		
	Team winner=userTeam,  loser=userTeam;
	
	if (userMatch[0].matchGoals > userMatch[1].matchGoals) {winner = userMatch[0];loser = userMatch[1];} 
	if (userMatch[1].matchGoals > userMatch[0].matchGoals) {winner = userMatch[1];loser = userMatch[0]; }
	
	f1Title = getMenuText(MENTXT_FOOTBALL_ONE_TITLES)[0];
	f1Body = gc.substringReplace(getMenuText(MENTXT_THE_MATCH_BEETWEEN_AND_ENDED)[0], "[team1]", userMatch[0].name);
	f1Body = gc.substringReplace(f1Body, "[team2]", userMatch[1].name)+userMatch[0].matchGoals+"-"+userMatch[1].matchGoals+". ";
		//getMenuText(MENTXT_THE_MATCH_BEETWEEN_AND_ENDED)[0]+" "+userMatch[0].name+" "+getMenuText(MENTXT_THE_MATCH_BEETWEEN_AND_ENDED)[1]+" "+userMatch[1].name+" "+getMenuText(MENTXT_THE_MATCH_BEETWEEN_AND_ENDED)[2]+" "+userMatch[0].matchGoals+"-"+userMatch[1].matchGoals+". ";

	
	if (userMatch[0].matchGoals-userMatch[1].matchGoals >= 2)
	{
		f1Title = gc.substringReplace(getMenuText(MENTXT_FOOTBALL_ONE_TITLES)[1], "[team1]", winner.name);
		f1Title =gc.substringReplace(f1Title, "[team2]", loser.name);
			//getMenuText(MENTXT_FOOTBALL_ONE_TITLES)[1] + winner.name + getMenuText(MENTXT_FOOTBALL_ONE_TITLES)[2]+loser.name+getMenuText(MENTXT_FOOTBALL_ONE_TITLES)[3];
		ret = true;
	}		
	
	
	if (userMatch[0].getQuality()+userMatch[1].getQuality() >= 130)
	{
		
		if (winner == userTeam && winner != loser) 
			f1Body += gc.substringReplace(gc.substringReplace(getMenuText(MENTXT_EVERYBODY_IS_TALKING_ABOUT)[0], "[name]", gc.custName), "[team]", winner.name);
		
				//getMenuText(MENTXT_EVERYBODY_IS_TALKING_ABOUT)[0]+" "+gc.custName+ " "+getMenuText(MENTXT_EVERYBODY_IS_TALKING_ABOUT)[1]+" "+winner.name+" "+getMenuText(MENTXT_EVERYBODY_IS_TALKING_ABOUT)[2];
		ret = true;
		
	}
	
	//if (ret == true) return true;
	
	
	if (Math.abs(userMatch[0].getQuality()-userMatch[1].getQuality()) >= 20)
	{
		if (userMatch[0].getQuality() > userMatch[1].getQuality() && winner == userMatch[1] && loser == userMatch[0])
		{
			f1Title = getMenuText(MENTXT_FOOTBALL_ONE_TITLES)[4-2];
			ret =  true;
		}
		else if (userMatch[1].getQuality() > userMatch[0].getQuality() && winner == userMatch[0] && loser == userMatch[1])
		{
			f1Title = getMenuText(MENTXT_FOOTBALL_ONE_TITLES)[4-2];			
			ret = true;
		}
	}

	if (ret)
	{
		if (enableMsg) gc.addMessage(GameCanvas.MSG_FOOTBALLONE, f1Title, f1Body);
		enableMsg = false;
		return true;
	}
	
	return false;
}
//#endif	
	
int countedGoals[] = new int[2];
boolean champEnded;

//////////////////////////////////////////////////////
//#ifdef FEA_MATCH_REPORT

final static int REPTXT_GOAL_QUANTITY = 1;
final static int REPTXT_QUALITY_COMPARISON = 2;
final static int REPTXT_GAME_ENDING = 3;
final static int REPTXT_GOALS = 4;
final static int REPTXT_ADVICE = 5;

public String[] matchReport()
{
	boolean exhibition = gc.exhibitionFlag;
	String [][]text = gc.reportText;
	String goalQuantity;
	String qualityComparison;
	String gameEnding = "";
	String goals = "";
	String advice = "";
	
	//if (gc.reportText == null) System.out.println("reportext null");
	if (userMatch[0].matchGoals + userMatch[1].matchGoals == 0)
		goalQuantity = text[REPTXT_GOAL_QUANTITY][0];  
	else if (userMatch[0].matchGoals + userMatch[1].matchGoals >= 5)
		goalQuantity = text[REPTXT_GOAL_QUANTITY][2];
	else
		goalQuantity = text[REPTXT_GOAL_QUANTITY][1];
	
	goalQuantity = gc.substringReplace(gc.substringReplace(goalQuantity, "[team0]", userMatch[0].name), "[team1]", userMatch[1].name);
	
	//System.out.println(userMatch[0].getOffensiveQuality()+userMatch[0].getDefensiveQuality());
	//System.out.println(userMatch[1].getOffensiveQuality()+userMatch[1].getDefensiveQuality());
	if (Math.abs(userMatch[0].getOffensiveQuality()+userMatch[0].getDefensiveQuality() - (userMatch[1].getOffensiveQuality()+userMatch[1].getDefensiveQuality())) > 25)
		qualityComparison = text[REPTXT_QUALITY_COMPARISON][0];
	else
		qualityComparison = text[REPTXT_QUALITY_COMPARISON][1];
		                     
	
	if (gc.numGoal > 0)
	{
		int firstScorer;
		if (gc.matchGoals[0][0] == 1) 
			firstScorer = 0;
		else
			firstScorer = 1;

		if (userMatch[firstScorer].matchGoals > userMatch[1-firstScorer].matchGoals)
			gameEnding = text[REPTXT_GAME_ENDING][2];
		else if (userMatch[firstScorer].matchGoals < userMatch[1-firstScorer].matchGoals) 
			{gameEnding = text[REPTXT_GAME_ENDING][0];firstScorer = 1-firstScorer;}
		else 
			gameEnding = text[REPTXT_GAME_ENDING][3];
		
		if (Math.abs(userMatch[0].matchGoals - userMatch[1].matchGoals) >= 3)
			gameEnding = text[REPTXT_GAME_ENDING][1];
		
		gameEnding = gc.substringReplace(gameEnding, "[team]", userMatch[firstScorer].name);
		
		if (rand()%256 > 96)
		{
			int gol = rand()%gc.numGoal;
			goals = gc.substringReplace(text[REPTXT_GOALS][0], "[playerName]", gc.matchGoalScorers[gol]);
			goals = gc.substringReplace(goals, "[result]", gc.matchGoals[gol][0]+" - "+gc.matchGoals[gol][1]);
			gol = 1+(rand()%4);
			goals += text[REPTXT_GOALS][gol];
		}
			
	}
	
	
	int team = -1;
	if (userTeam == userMatch[0] && userMatch[0].matchGoals < userMatch[1].matchGoals)
		team = 0; 
	if (userTeam == userMatch[1] && userMatch[0].matchGoals > userMatch[1].matchGoals)
		team = 1; 
				
	if (team != -1 && !exhibition)
	{
		if (gc.trainingStyle < 2 && rand()%256 > 96)
			advice = text[REPTXT_ADVICE][0];
		else if (!seasonGoalAcomplished())
			advice = text[REPTXT_ADVICE][1];
		else 
			advice = text[REPTXT_ADVICE][2];
	}
	advice = gc.substringReplace(advice, "[team]", userTeam.name);
		
	int pos0 = (50+(userMatch[0].matchGoals-userMatch[1].matchGoals)*6)+rand()%10-rand()%10;
	
    return new String[]{goalQuantity, qualityComparison, gameEnding, goals, advice,
    		" ",
    		userMatch[0].name,
    		getMenuText(MENTXT_TEXTMATCH_STATS)[0]+": "+(userMatch[0].matchGoals+rand()%9),
    		getMenuText(MENTXT_TEXTMATCH_STATS)[1]+": "+(4+((matchInjuredCount+matchSuspendedCount)*3)+rand()%5),
    		getMenuText(MENTXT_TEXTMATCH_STATS)[2]+": "+pos0+"%",
    		" ",
    		userMatch[1].name,
    		getMenuText(MENTXT_TEXTMATCH_STATS)[0]+": "+(userMatch[1].matchGoals+rand()%9),
    		getMenuText(MENTXT_TEXTMATCH_STATS)[1]+": "+(4+((matchInjuredCount+matchSuspendedCount)*3)+rand()%5),
    		getMenuText(MENTXT_TEXTMATCH_STATS)[2]+": "+(100-pos0)+"%"
    };
};

//#endif
}