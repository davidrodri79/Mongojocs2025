package com.mygdx.mongojocs.lma2006;// ---------------------------------------------------------------
// Microjocs Football Manager
// ---------------------------------------------------------------
// Strategical Simulation / Pseudo-RPG Football Team Managing Game
// Programming by Carlos Carrasco, Carlos Peris and David Rodr�guez

//#ifdef WEBSUBCRIPTION
//#define NOCHAMPIONS
//#endif


//define TANASTEN



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class League {
	
	final static int TEXT_SENSEI = 293;
	final static int TUPLELENGTH = 3;
	
	
	public Team[][] teams;
	//public Team extraTeam;
	public short[] top40Players;
	public int playingLeague;
	public boolean playingChamp;
	public boolean forceChampPlay;
	public Team[] champTeams;
	private Team[][] journeys;
	public int journeyNumber;
	public int realJourneyNumber;
	public static  boolean champJourney;
	private static  boolean lastChampJourney;
	public int journeyFinal;
	public static Team userTeam;
	private String matchTitle;
	private String roundTitle;
	public Team[] journeyMatch;
	public Team[] journeyAllMatches;
	
	//SENSEI
	//#ifndef NOPRESEASON
	public boolean preSeasonPlaying;
	public int preSeasonMatchesPlayed;
	public int preSeasonOpps[][];
	//#endif
	
	//#ifndef NOFINANCES
	public boolean haveSponsor;
	public int sponsorQuality;	
	public int sponsorBenefit;
	public int journeyIncome;
	public int journeyExpenses;	
	//#endif
	
	//#ifndef NOSEASONGOAL
	public int seasonGoal;
	public int teamInitialPosition; 
	//#endif
	
	//#ifdef NOPRESEASON
	//public static final int PRESEASON_ROUNDS = 0;
	//#else
	public static final int PRESEASON_ROUNDS = 4;
	//#endif

	//public Team[] availHirePlayersTeams;
	//public short[] availHirePlayers;
	public int availHires;

	static private Random random = null;

	public Team[] lastEuropeRound;
	public byte[] lastEuropeResults;
	public byte[] lastEuropeVictories;
	public Team[] lastLeagueRound;
	public byte[] lastLeagueResults;

/////////////////////////////////////////////////////////////////////////////////
// clase player en arrays

	static private byte[] blkPlayerNames;
	static private short[] blkPlayerNameOffsets;
	static private byte[] blkPlayerStats;
	static private byte[] blkPlayerGoals;

	static public int playerGetGoals(int i) {
		return blkPlayerGoals[i];
	}

	static public void playerSetGoals(int i, int g) {
		blkPlayerGoals[i] = (byte)g;
	}

	static public String playerGetName(int i) {
		int len = blkPlayerStats[i*TUPLELENGTH];
		int off = blkPlayerNameOffsets[i];
//#ifdef DBCUSTOMFONT
//#else
		return new String(blkPlayerNames, off, len);
//#endif
	}

	static public int playerGetSpec(int i) {
		return blkPlayerStats[i*TUPLELENGTH+1];
	}

	static public String playerGetSpecAbbr(int i) {
		//return posAbbr[playerGetSpec(i)];
		return gameText[110 + playerGetSpec(i)][0];
	}

	//ZNR
	final static int facTable[][] = {
		{10,10,20,60},
		{10,20,20,50},
		{20,45,25,10},
		{30,20,45,05}
		
	};
	
	static public int playerGetStat(int i, int s) {
		int playerPosition = blkPlayerStats[i*TUPLELENGTH+1];
		int stat = (blkPlayerStats[i*TUPLELENGTH+2]*facTable[playerPosition][s])/100;
		return stat; //ZNR
	}
	

	//ZNR delicado
	/*
	static public void playerSetStat(int i, int stat, byte value) {
		if (value < 0)
			value = 0;
		if (value > 99)
			value = 99;
		int pos = blkPlayerStats[i*(TUPLELENGTH+1)];
		//ZNR TODO: NO SE QUE HACER CON ESTO!!!!!!			
		final static int facTable[][] = {
			{10,10,20,60},
			{10,20,20,50},
			{20,45,25,10},
			{30,20,45,05}		
		};
		//blkPlayerStats[i*6+2+s] = v;
	}*/

	static private int[] blkSanctionedPlayers = null;
	static private int[] blkInjuredPlayers = null;

	static public int playerDowntime(int p) {
		for (int i = 0; i < blkSanctionedPlayers.length; i++) {
			if (((short)(blkSanctionedPlayers[i] & 0xffff)) == p) {
				return (blkSanctionedPlayers[i] & 0xffff0000)>>16;
			}
		}
		for (int i = 0; i < blkInjuredPlayers.length; i++) {
			if (((short)(blkInjuredPlayers[i] & 0xffff)) == p) {
				return (blkInjuredPlayers[i] & 0xffff0000)>>16;
			}
		}
		return 0;
	}

	static public boolean playerIsSanctioned(int p) {
		return playerIsInBlk(blkSanctionedPlayers, p);
	}

	static public boolean playerIsInjured(int p) {
		return playerIsInBlk(blkInjuredPlayers, p);
	}

	static private boolean playerIsInBlk(int[] blk, int p) {
		if (blk == null)
			return false;
		for (int i = 0; i < blk.length; i++) {
			if (((short)(blk[i] & 0xffff)) == p) {
				return ((blk[i] & 0xffff0000)>>16) > 0;
			}
		}
		return false;
	}

	public int countDownPlayers(int max) {
		int downPlayers = 0;
		for (int i = 0; i < max; i++) {
			if (userTeam.players[i] != -1 && (playerIsSanctioned(userTeam.players[i]) || playerIsInjured(userTeam.players[i])))
				downPlayers++;
		}
		return downPlayers;
	}

	static public void playerSetSanctioned(int p, int d) {
		if (blkSanctionedPlayers == null) {
			blkSanctionedPlayers = new int[30];
		}
		playerSetInBlk(blkSanctionedPlayers, p, d);
	}

	static public void playerSetInjured(int p, int d) {
		if (blkInjuredPlayers == null) {
			blkInjuredPlayers = new int[30];
		}
		playerSetInBlk(blkInjuredPlayers, p, d);
	}

	static private void playerSetInBlk(int[] blk, int p, int d) {
		for (int i = 0; i < blk.length; i++) {
			int tp = blk[i] & 0xffff;
			if (tp == 0xffff) {
				blk[i] = (d << 16) | p;
				return;
			}
		}
	}

	/*static private void sanctionsInjuriesEndJourney() {
		blkEndJourney(blkSanctionedPlayers);
		blkEndJourney(blkInjuredPlayers);
	}*/
	
	
	static private void blkEndJourney(int[] blk) {
		if (blk == null)
			return;
		for (int i = 0; i < blk.length; i++) {
			int p = blk[i] & 0xffff;
			if (p != 0xffff) {
				int d = (blk[i] & 0xffff0000) >> 16;
				d--;
				if (d <= 0) {
					blk[i] = -1;
				} else {
					blk[i] = (d << 16) | p;
				}
			}
		}
	}

	static private void resetBlk(int[] blk) {
		if (blk == null)
			return;
		for (int i = 0; i < blk.length; i++) {
			blk[i] = -1;
		}
	}
		
	
	
	static private void sanctionsInjuriesStartSeason() {
		
		if (blkSanctionedPlayers == null) {
			blkSanctionedPlayers = new int[30];
		}
		resetBlk(blkSanctionedPlayers);
		if (blkInjuredPlayers == null) {
			blkInjuredPlayers = new int[30];
		}
		resetBlk(blkInjuredPlayers);
	}

	static public byte playerGetTextAtt(int i) {
		if (playerIsSanctioned(i)) {
			return 4;
		} else if (playerIsInjured(i)) {
			return 2;
		}
		return 0;
	}

	static public void playerIncStat(int i, int s, int inc) {
		/*final static int facTable[][] = {
		{10,10,20,60},
		{10,20,20,50},
		{20,45,25,10},
		{30,20,45,05}		
		};*/	
		//ZNR
		int pos = blkPlayerStats[i*TUPLELENGTH+1];
		inc = inc * facTable[pos][s];
		int p = 0;
		while (inc >= 100)
		{
			inc -= 100;
			p++;
		}
		
		if (inc >= (rand() % 100)) inc = 1;
		else inc = 0;
		
		         
				
		int t = getPlayerQuality(i) + inc + p;
		//ZNR AYYYYYYYYYYYYYYY
		setPlayerQuality(i, (byte)t);
		//blkPlayerStats[i*TUPLELENGTH+2] = (byte)t; 
	}

	//ZNR
	public static int getPlayerQuality(int i)
	{
		return blkPlayerStats[(i*TUPLELENGTH)+2];
	}
	
	public static void setPlayerQuality(int i, byte v)
	{
		if (v > 99)	v = 99;
		if (v < 1)	v = 1;
		blkPlayerStats[(i*TUPLELENGTH)+2] = v;
	}
	
	
	//ZNR: Mirate esto, la j parece el indice de caracteristica.
	static public int playerBalance(int i, int sp) {
		//ZNR
		/*
		if (sp == -1) {
			sp = playerGetSpec(i);
		}*/
		//ZNR
		//Intento subir un poc el stat como CCM
		int b = (blkPlayerStats[(i*TUPLELENGTH)+2]*100)/95;
		//ZNR
		/*int acc = 0;
		for (int j = 0; j < 4; j++) {
			acc += (League.statsWeight[sp][j] * playerGetStat(i, j)) / 17;
		}
		int b = acc/4;
		*/
		
		if (b < 1)
			b = 1;
		if (b > 99)
			b = 99;
		return b;
	}

	static public int playerValue(int i) 
	{
		int v = playerBalance(i, -1) * 2;
		return v > 1 ? v : 1;
	}

	
	//ZNR
	static int offertedPlayerValue;
	static public int offertedPlayerValue(int i) 
	{
		int v = playerValue(i);
		//System.out.println("real value:"+v);
		v += 	(((rand()-rand())%256)*v*20)
				/ (256*100);
		//System.out.println("fake value:"+v);
		offertedPlayerValue = v > 1 ? v : 1; 		
		return offertedPlayerValue;
	}

	
	
	static public String cashStr(int cash) {
		// 1 unidad = 200000 EUR
		int dec = (cash % 5) * 2;
		int uni = cash / 5;
		String s = "" + uni;
		if (uni < 9) {
			s += "." + dec;
		}
		return s;
	}
	
	// SENSEI
	//#ifndef NOSEASONGOAL
	public boolean seasonGoalAcomplished() {
		
		int pos;
				
		pos = userTeamPos();
		
		switch(seasonGoal)
		{			
			case 1 : return pos < 3;
			case 2 : return pos < 10;
			case 3 : return pos < teams[playingLeague].length / 2;
			case 4 : return pos < teams[playingLeague].length - 4;
			default: return false;
		}
	}
	//#endif
	
	

/////////////////////////////////////////////////////////////////////////////////
// constantes del juego

	public static int[] leagueTeams = null;
	public static int[][] statsWeight = null;
	public static int[][] positionPenalization = null;
	public static int[] positionGoalProbability = null;
	public static int[][] allTrainings = null;
	public static byte[][] formationsXY = null;
	public static byte[][] formationsSpec = null;

/*
	// info estatica de ligas - mejor en un file pero de momento
	// aqui es mas facil
	public static final int[] leagueTeams = {
		20, 12, 18, 20, 20, 20
	};

	// estas stats no tienen por que ser las definitivas
	// cada fila ha de sumar 100
	public static final int[][] statsWeight = {
	        // ShotPow  Passing      Pace  Defending
		{       10,      10,       20,        60 },   // Portero
		{       10,      20,       20,        50 },   // Defensa
		{       20,      45,       25,        10 },   // Medio
		{       30,      20,       45,         5 }    // Delantero
	};

	// ponderado punto a punto, no ha de sumar 100
	public static final int[][] positionPenalization = {
	        // Portero  Defensa     Medio  Delantero
		{      100,      30,       10,         5 },   // Portero
		{        5,     100,       30,        10 },   // Defensa
		{        5,      15,      100,        20 },   // Medio
		{       10,       5,       20,       100 }    // Delantero
	};

	// esta tabla ha de sumar 100
	public static final int[] positionGoalProbability = {
		0,    // Portero
		10,   // Defensa
		20,   // Medio
		70    // Delantero
	};

	// todos los entrenamientos disponibles. N es a cuantos jugadores se puede 
	// aplicar a la vez. las stats son o 0 (no mejora nada) o un numero que se
	// suma tal cual a la stat del jugador
	public static final int[][] allTrainings = {
		// N   ShotPow  Passing     Pace  Defending
		{  2,        0,       0,       3,       0 },
		{  3,        0,       0,       2,       0 },
		{  2,        0,       1,       2,       0 },
		{  2,        1,       1,       1,       1 },
		{  5,        0,       1,       0,       1 },

		{  2,        3,       0,       0,       0 },
		{  2,        2,       0,       1,       0 },
		{  3,        1,       1,       1,       0 },

		{  3,        0,       2,       1,       0 },
		{  2,        1,       2,       0,       0 },
		{  3,        0,       1,       0,       2 },

		{  2,        0,       0,       1,       3 },
		{  2,        0,       1,       1,       2 }
	};

	// XY visuales en el mapa
	public static final byte[][] formationsXY = {
		{  3, 18,
		   16, 31,   16, 18,   16, 4,
		   30, 32,   30, 22,   30, 12,   30, 2,
		   44, 30,   44, 8,
		   54, 17
		},
		{  3, 18,
		   13, 18,
		   22, 32,   22, 22,   22, 12,   22, 2,
		   34, 31,   34, 18,   34, 4,
		   48, 30,   48, 8
		},
		{  3, 18,
		   16, 32,   16, 22,   16, 12,   16, 2,
		   30, 32,   30, 22,   30, 12,   30, 2,
		   44, 30,   44, 8
		},
		{  3, 18,
		   16, 32,   16, 22,   16, 12,   16, 2,
		   30, 31,   30, 18,   30, 4,
		   44, 30,   44, 8,
		   54, 17
		},
		{  3, 18,
		   16, 31,   16, 18,   16, 4,
		   28, 18,   
		   38, 32,   38, 22,   38, 12,   38, 2,
		   50, 30,   50, 8
		},
		{  3, 18,
		   13, 18,
		   22, 32,   22, 22,   22, 12,   22, 2,
		   38, 32,   38, 22,   38, 12,   38, 2,
		   54, 17
		}
	};

	// que representa cada posicion en el mapa
	// 1-11: jugadores en el campo, que spec tienen: "GK", "DE", "CF", "AT"
	// 12-17: convocados, spec 64
	// 18-25: no convocados, spec 96
	public static final byte[][] formationsSpec = {
		{ 0,
		  1, 1, 1,
		  2, 2, 2, 2,
		  3, 3, 3,
		  64, 64, 64, 64, 64,    96, 96, 96, 96, 96,    96, 96, 96, 96, 96,
		  96, 96, 96, 96 },
		{ 0,
		  1, 1, 1, 1, 1,
		  2, 2, 2,
		  3, 3,
		  64, 64, 64, 64, 64,    96, 96, 96, 96, 96,    96, 96, 96, 96, 96,
		  96, 96, 96, 96 },
		{ 0,
		  1, 1, 1, 1,
		  2, 2, 2, 2,
		  3, 3,
		  64, 64, 64, 64, 64,    96, 96, 96, 96, 96,    96, 96, 96, 96, 96,
		  96, 96, 96, 96 },
		{ 0,
		  1, 1, 1, 1,
		  2, 2, 2,
		  3, 3, 3,
		  64, 64, 64, 64, 64,    96, 96, 96, 96, 96,    96, 96, 96, 96, 96,
		  96, 96, 96, 96 },
		{ 0,
		  1, 1, 1,
		  2, 2, 2, 2, 2,
		  3, 3,
		  64, 64, 64, 64, 64,    96, 96, 96, 96, 96,    96, 96, 96, 96, 96,
		  96, 96, 96, 96 },
		{ 0,
		  1, 1, 1, 1, 1,
		  2, 2, 2, 2,
		  3,
		  64, 64, 64, 64, 64,    96, 96, 96, 96, 96,    96, 96, 96, 96, 96,
		  96, 96, 96, 96 },
	};
*/
	/*
	public static final String[] formationsNames = {
		"3-4-3", "5-3-2", "4-4-2", "4-3-3", "3-5-2", "5-4-1"
	};*/

/////////////////////////////////////////////////////////////////////////////////
// entrenador

	public int coachTopTraining;
	//public int coachRemainingTrainings;
	//public int coachRemainingFires;
	public int coachFormation;
	public int coachGeneralTactic;
	public int coachXP;
	public int coachCash;

	public void initCoach() {
		//coachRemainingTrainings = 3;
		//coachRemainingFires = 1;
		coachFormation = 0;
		coachXP = 0;
		coachGeneralTactic = 0;
		coachCash = 0;
	}

	public void calcTopTraining() {
		//coachTopTraining = 3 + coachXP/1000;
		coachTopTraining = 10;
		/*if (coachTopTraining > (trainingsNames.length - 1)) {
			coachTopTraining = trainingsNames.length - 1;
		}*/
	}

	public void coachStartSeason() {
		//coachRemainingTrainings = 3;
		//coachRemainingFires = 1;
		calcTopTraining();
		
		// SENSEI
		//#ifndef NOSEASONGOAL
		int q = (20 * teamInitialPosition) / teams[playingLeague].length;
		
		//System.out.println("Par�metro de calidad:"+q);
		//System.out.println("Team position:"+teamInitialPosition);
				
		if(q < 3)
			seasonGoal = 1;
		else if(q < 10)
			seasonGoal = 2;
		else if(q < 16)
			seasonGoal = 3;
		else
			seasonGoal = 4;
		//#endif
	}

	public void coachStartJourney() {
		//coachRemainingTrainings = 3;
		//coachRemainingFires = 1;
		/*if (coachRemainingFires <= 0) {
			coachRemainingFires = (journeyNumber & 3) == 1 ? 1 : 0;
		}*/
		calcTopTraining();
				
		// SENSEI
		
		//#ifndef NOFINANCES
		
		journeyIncome = 0;
		journeyExpenses = 0;
		
		if(haveSponsor){
			coachCash += sponsorBenefit;
			//journeyIncome += sponsorBenefit;
		}else{
			sponsorQuality = (rand() % 4) + ((userTeam.realQuality()-35)/8);	
			sponsorBenefit = 4 + (sponsorQuality * 2) + (rand() % 3);
			
			//System.out.println("Team quality:"+ userTeam.realQuality() + "QUality:" + sponsorQuality + " Benefit:"+sponsorBenefit);			
		}
	
		// Gastos por sueldos del equipo		
		coachCash -= teamPlayerWage();
		
		//#endif
		
		//journeyExpenses += teamPlayerWage();
				
	}

/////////////////////////////////////////////////////////////////////////////////

	private static String[][] gameText;
	public League(String[][] l) {
		gameText = l;
		teams = null;
		journeys = null;
		journeyNumber = 0;
		journeyFinal = 0;
		userTeam = null;
		matchTitle = null;
		roundTitle = null;
		journeyMatch = null;
		journeyAllMatches = null;
		playingLeague = 0;
		top40Players = new short[40];
		//#ifndef NOFINANCES
		haveSponsor = false;
		//#endif
		initCoach();				
	}

	public void load(byte[] cons, byte[] plyn, byte[] plys, byte[] tms) {
		//System.out.println("league.load");
		blkPlayerNames = null;
		blkPlayerStats = null;
		teams = null;
		System.gc();

		//System.out.println("load 1");
		// cargar constantes
		ByteArrayInputStream cais = new ByteArrayInputStream(cons);
		DataInputStream cis = new DataInputStream(cais);

		try {
			//System.out.println("leagueTeams");
			leagueTeams = new int[6];
			for (int i = 0; i < 6; i++) {
				leagueTeams[i] = cis.readByte();
				//System.out.println("" +leagueTeams[i]); 
			}

			//System.out.println("statsWeight");
			statsWeight = new int[4][];
			for (int j = 0; j < 4; j++) {
				statsWeight[j] = new int[4];
				for (int i = 0; i < 4; i++) {
					statsWeight[j][i] = cis.readByte();
					//System.out.println("" +statsWeight[j][i]);
				}
			}

			//System.out.println("load 4");
			//System.out.println("positionPenalization");
			positionPenalization = new int[4][];
			for (int j = 0; j < 4; j++) {
				positionPenalization[j] = new int[4];
				for (int i = 0; i < 4; i++) {
					positionPenalization[j][i] = cis.readByte();
					//System.out.println("" +positionPenalization[j][i]);
				}
			}

			//System.out.println("positionGoalProbability");
			positionGoalProbability = new int[4];
			for (int i = 0; i < 4; i++) {
				positionGoalProbability[i] = cis.readByte();
				//System.out.println("" +positionGoalProbability[i]);
			}

			//System.out.println("load 6");
			//System.out.println("allTrainings");
			allTrainings = new int[13][];
			for (int j = 0; j < 13; j++) {
				allTrainings[j] = new int[5];
				for (int i = 0; i < 5; i++) {
					allTrainings[j][i] = cis.readByte();
					//System.out.println("" +allTrainings[j][i]);
				}
			}

			//System.out.println("load 7");
			//System.out.println("formationsXY");
			formationsXY = new byte[6][];
			for (int j = 0; j < 6; j++) {
				formationsXY[j] = new byte[22];
				for (int i = 0; i < 22; i++) {
					formationsXY[j][i] = cis.readByte();
					//System.out.println("" +formationsXY[j][i]);
				}
			}

			//System.out.println("formationsSpec");
			formationsSpec = new byte[6][];
			for (int j = 0; j < 6; j++) {
				formationsSpec[j] = new byte[30];
				for (int i = 0; i < 30; i++) {
					formationsSpec[j][i] = cis.readByte();
					//System.out.println("" +formationsSpec[j][i]);
				}
			}
		} catch (IOException e) {}
		
		//System.out.println("load 9");
		// init estatico de ligas
		teams = new Team[League.leagueTeams.length][];
		//extraTeam = new Team();
		int[] teamsIdx = new int[League.leagueTeams.length];
		for (int i = 0; i < League.leagueTeams.length; i++) {
			teams[i] = new Team[League.leagueTeams[i]];
			teamsIdx[i] = 0;
		}
		int globalidx = 0;
		blkPlayerNames = plyn;
//#ifndef BUILDSTATS
		blkPlayerStats = plys;
//#else
	/*	blkPlayerStats = new byte[plys.length*2];
		int ni = plys.length/3;
		for (int i = 0; i < ni; i++) {
			//System.out.println("" + i);
			blkPlayerStats[i*TUPLELENGTH + 0] = plys[i*3 + 0];
			byte pos = plys[i*3 + 1];
			blkPlayerStats[i*TUPLELENGTH + 1] = pos;
			int overall = plys[i*3 + 2];		
			for (int j = 0; j < 4; j++) {
				//ZNR : MARRON

			}
		}
		plys = null;
		//System.out.println("Se calculan stats");*/
//#endif
		ByteArrayInputStream bais = new ByteArrayInputStream(tms); 
		DataInputStream dis = new DataInputStream(bais);
		int base = 0;
		try {
			
			int totalTeams = dis.readUnsignedByte();
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
				Team t = null;
				/*if (league == 5) {
					t = extraTeam;
				} else {*/
					t = new Team();
				//}
					//System.out.println("No se calculan stats 2");
				//#ifndef NOCLUBBAR
				int r1 =	dis.readUnsignedByte();
				int g1 =	dis.readUnsignedByte();
				int b1 =	dis.readUnsignedByte();
				int r2 =	dis.readUnsignedByte();
				int g2 =	dis.readUnsignedByte();
				int b2 =	dis.readUnsignedByte();			
				//System.out.println("No se calculan stats 3");
				t.load(namet, total, base, money, r1, g1, b1, r2, g2, b2);
				//#else
				//#endif
				t.loadLeague = (byte)league;
				t.globalIdx = (byte)globalidx;
				//if (league < 5) {
					teams[league][teamsIdx[league]] = t;
					teamsIdx[league]++;
				//}
				base += total;
				globalidx++;
				//System.out.println("No se calculan stats 4");
			}
		} catch (IOException e) { }
		//System.out.println("DEBUG 1");
		// construir offsets de nombres
		blkPlayerNameOffsets = new short[base];
		short accSize = 0;
		for (int i = 0; i < base; i++) {
			blkPlayerNameOffsets[i] = accSize;
			accSize += blkPlayerStats[i*TUPLELENGTH]; //ZNR
		}
		//System.out.println("DEBUG 2");
		// allocar goles
		blkPlayerGoals = new byte[base];
		for (int i = 0; i < base; i++) {
			blkPlayerGoals[i] = 0;
		}
		//System.out.println("Ordenar equipos por pasta");
		// ordenar equipos por pasta disponible
		for (int i = 0; i < League.leagueTeams.length; i++) {
			bubbleSortTeams(teams[i], -1, teams[i].length - 1, 1);
		}

		//System.out.println("DEBUG 4");		
	}

	
//#ifdef LOWMEM
//#endif	
	
	
	
	
	
	
	private void shuffleTeams(Team[] t) {
		for (int i = 0; i < t.length; i++) {
			int t2i = rand() % t.length;
			Team t2t = t[t2i];
			t[t2i] = t[i];
			t[i] = t2t;
		}
	}

	private void shuffleShorts(short[] t) {
		for (int i = 0; i < t.length; i++) {
			int t2i = rand() % t.length;
			short t2t = t[t2i];
			t[t2i] = t[i];
			t[i] = t2t;
		}
	}

	public void startSeason() {
		//#ifndef NOCALENDAR
		season++;
		//#endif
		
		champRound = 0;
		
		for (int i = 0; i < teams[playingLeague].length; i++) {
			teams[playingLeague][i].startSeason();
		}
		//#ifndef NOCHAMPIONS
		// championship - se sortea primero para conservar las ultimas posiciones
		playingChamp = true;
		if (leagueOrdinal(userTeam) < 4) {
			// 16 equipos: 4 de la actual, 3 de las 4 restantes <- NO
			// 16 equipos: 6 de la actual, 2 de las 5 restantes <- SI
			champTeams = new Team[16];
			for (int i = 0, k = 0; i < teams.length; i++) {
				for (int j = 0; j < 6; j++) {
					//if (j == 1 && i == 1) break;
					if (j == 2 && i != playingLeague)
						break;
					champTeams[k] = teams[i][j];
					//System.out.println("ct: "+champTeams[k].name);
					k++;
				}
			}
		} else {
			// 64 equipos: todos los de la actual (menos 4 primeros), equilibrado del resto (menos los 4 primeros)
			champTeams = new Team[64];
			int k = 0;
			for (int i = 4; i < teams[playingLeague].length; i++, k++) {
				champTeams[k] = teams[playingLeague][i];
			}
			int j = 4;
			while (k < 64) {
				//for (int l = 0; l < 5; l++) {
				for (int l = 0; l < 6; l++) {
					if (l != playingLeague && j < teams[l].length && k < 64) {
						champTeams[k] = teams[l][j];
						//System.out.println("CT: "+champTeams[k].name);
						k++;
					}
				}	
				j++;
			}
		}
		//#endif
		//SENSEI
		//#ifndef NOSEASONGOAL
		teamInitialPosition = userTeamPos();
		//#endif
		
		//#ifndef NOCHAMPIONS
		shuffleTeams(champTeams);
		//#endif

		shuffleTeams(teams[playingLeague]);

		int half = teams[playingLeague].length-1;

		Team[] left = new Team[teams[playingLeague].length/2];
		for (int i = 0; i < teams[playingLeague].length/2; i++) {
			left[i] = teams[playingLeague][i];
		}
		Team[] right = new Team[teams[playingLeague].length/2];
		// reverse order
		for (int i = teams[playingLeague].length/2, j = right.length-1; i < teams[playingLeague].length; i++, j--) {
			right[j] = teams[playingLeague][i];
		}

		journeys = new Team[half*2][];
		int top = (teams[playingLeague].length/2)-1;
		int phase = 0;
		for (int i = 0; i < half; i++) {
			// first = right.shift <>  [ 1 2 3 ... 10 ] ---> [ 2 3 ... 10 ] : 1
			Team first = right[0];
			Team[] nr = new Team[right.length];
			System.arraycopy(right, 1, nr, 0, right.length - 1);
			right = nr;

			// right.push(left.pop) <>  
			// left.pop <> [ 1 2 3 ... 10 ] ---> [ 1 2 3 ... 9 ] : 10
			// right.push(X) <> [ 1 2 3 ... 10 ] ---> [ 1 2 3 ... 10 X ]
			right[top] = left[top];
			left[top] = null;

			// left = left.unshift(left.first) <> [ 1 2 3 ... 9 null ] --> [ 1 1 2 3 ... 9 ]
			Team leftFirst = left[0];
			Team[] nl = new Team[left.length];
			System.arraycopy(left, 0, nl, 1, left.length - 1);
			nl[0] = leftFirst;
			left = nl;

			left[1] = first;

			journeys[i] = new Team[teams[playingLeague].length];
			journeys[i+half] = new Team[teams[playingLeague].length];
			for (int j = 0; j < left.length; j++) {
				if (phase == 0) {
					journeys[i][j*2] = left[j];
					journeys[i][j*2+1] = right[j];
					journeys[i+half][j*2] = right[j];
					journeys[i+half][j*2+1] = left[j];
				} else {
					journeys[i][j*2] = right[j];
					journeys[i][j*2+1] = left[j];
					journeys[i+half][j*2] = left[j];
					journeys[i+half][j*2+1] = right[j];
				}
			}
			if (phase == 0) {
				phase = 1;
			} else {
				phase = 0;
			}
		}
				
		journeyFinal = half*2 - 1;
		journeyNumber = 0;
		champJourney = false;
		forceChampPlay = false;
		
		// resetear top 40
		for (int i = 0; i < 40; i++) {
			top40Players[i] = -1;
		}

		coachStartSeason();
		availHires = 1;

		// sanciones y lesiones
		sanctionsInjuriesStartSeason();

		// resetear resultados
		lastEuropeRound = null;
		lastEuropeResults = null;
		lastEuropeVictories = null;
		lastLeagueRound = null;
		lastLeagueResults = null;
		
		// SENSEI
		
		//#ifndef NOPRESEASON
		// Elige equipos aleatorios para jugar partidos de pre-temporada
						
		preSeasonMatchesPlayed = 0;		
		preSeasonPlaying = true;			
		preSeasonOpps = new int[PRESEASON_ROUNDS][2];
		
		for(int i = 0; i < PRESEASON_ROUNDS; i++)
		{
			boolean repeated;
			
			do {
												
				preSeasonOpps[i][0] = rand() % teams.length;
				preSeasonOpps[i][1] = rand() % teams[preSeasonOpps[i][0]].length;
				
				repeated = false;
				
				if(userTeam == teams[preSeasonOpps[i][0]][preSeasonOpps[i][1]])
					repeated = true;
				
				for(int j = 0; j < i; j++)
					if(preSeasonOpps[i][0] == preSeasonOpps[j][0] && preSeasonOpps[i][1] == preSeasonOpps[j][1])
						repeated = true;
										
			} while(repeated);
			
			//System.out.println("Partido "+(i+1)+"� de pretemporada contra equipo "+preSeasonOpps[i][0]+"-"+preSeasonOpps[i][1]);
		}
		//#endif
		for(int i = 0; i < 30;i++) GameCanvas.triedToSell[i] = 0;		
	}

	private int leagueOrdinal(Team t) {
		for (int i = 0; i < teams[playingLeague].length; i++) {
			if (t == teams[playingLeague][i])
				return i;
		}
		return 0;
	}


	private Team anyTeam() {
		int l = rand() % teams.length;
		int i = rand() % teams[l].length;
		return teams[l][i];
	}

	public Team vacantTeam() {
		Team t = anyTeam();
		while (t.countPlayers() >= 30) {
			t = anyTeam();
		}
		return t;
	}

	public Team vacantTeamQA(int q) {
		Team t = vacantTeam();
		while (t.realQuality() < q) {
			t = vacantTeam();
		}
		return t;
	}

	public void startJourney() {
		startJourney(true);
	}

	public void startJourney(boolean resetCoach) {
		/*int sameLeagueN = rand() % 3;
		int otherLeaguesN = 1 + rand() % 3;
		int n =	sameLeagueN + otherLeaguesN;
		availHirePlayersTeams = new Team[n];
		availHirePlayers = new short[n];
		int i = 0;
		for (int j = 0; j < sameLeagueN; j++, i++) {
			Team t = userTeam;
			while (t == userTeam || t.countPlayers() < 17) {
				t = teams[playingLeague][rand() % teams[playingLeague].length];
			}
			availHirePlayersTeams[i] = t;
			availHirePlayers[i] = t.randPlayer(0);
		}
		for (int j = 0; j < otherLeaguesN; j++, i++) {
			int l = rand() % teams.length;
			if (l == playingLeague)
				l = (l + 1) % teams.length;
			Team t = teams[l][rand() % teams[l].length];
			availHirePlayersTeams[i] = t;
			availHirePlayers[i] = t.randPlayer(0);
		}*/
		if (resetCoach)
			coachStartJourney();
		calcBanner();
		
		// SENSEI
		//#ifndef NOPRESEASON
		if(!preSeasonPlaying)
			journeyAllMatches = journeys[journeyNumber];
		//#else
		//#endif
				
	}

	/*public void clearHires() {
		availHirePlayersTeams = null;
		availHirePlayers = null;
	}*/

	public void endJourney() {
		int chint = 2;
		if (champTeams != null && champTeams.length <= 16 && journeyNumber < 15) {
			chint = 3;
		}
		realJourneyNumber++;
		lastChampJourney = champJourney;
		//#ifndef NOPRESEASON
		if(!preSeasonPlaying)
		//#endif
			//#ifndef NOCHAMPIONS	
		if (((realJourneyNumber % chint) == 1) && playingChamp) {
			champJourney = true;
			forceChampPlay = false;
		} else if (((realJourneyNumber % chint) == 1) && !playingChamp
			&& champTeams != null && champTeams.length > 1) {
			champJourney = false;
			forceChampPlay = true;
			journeyNumber++;
		} else {
			champJourney = false;
			forceChampPlay = false;
			journeyNumber++;
		}
		//#else
		//#endif
		
		
		//#ifndef NOPRESEASON		
		if(!preSeasonPlaying)
        //#endif			
			sortLeague();
		//sanctionsInjuriesEndJourney();
		//=================================
		blkEndJourney(blkSanctionedPlayers);
		blkEndJourney(blkInjuredPlayers);
		availHires = 1;
		
		// SENSEI
		//#ifndef NOPRESEASON
		if(preSeasonPlaying) preSeasonMatchesPlayed++;
		if(preSeasonMatchesPlayed >= 4) preSeasonPlaying = false;
		//#endif
						
		GameCanvas.tries = 30;
	}
	
	
	public int teamPlayerWage() {
		
		return userTeam.realQuality() / 10;		
	}

	public int transferRange(int r) {
		if (r == 0) {
			return 0;
		}
		if (r == 1) {
			return (journeyFinal/2) - 3;
		}
		if (r == 2) {
			return journeyFinal - 4;
		}
		return -1;
	}

//#ifndef NOCHAMPIONS
	public String[] getChampMatches() {
		//System.out.println("getChampMatches+"+champTeams.length);
		String[] r = null;
		if (champTeams.length == 0) {
			r = new String[1];
			r[0] = gameText[TEXT_SENSEI - 1][0];
		} else if (champTeams.length == 1) {
			r = new String[1];
			r[0] = gameText[217][0] + ": " + champTeams[0].name;
		} else {
			r = new String[champTeams.length/2];
			for (int i = 0, j = 0; i < champTeams.length-1; i+=2, j++) {
				r[j] = champTeams[i].name + " vs. " + champTeams[i+1].name;
				//System.out.println("list: "+champTeams[i].name + " vs. " + champTeams[i+1].name);
			}
		}
		return r;
	}

	public String getChampJourneyNameProto(int m) {
		//System.out.println("ch:"+(champTeams.length*m));
		switch ((champTeams.length*m)) {
			case 64: return gameText[218][0];
			case 32: return gameText[219][0];
			case 16: return gameText[220][0];
			case 8:  return gameText[221][0];
			case 4:  return gameText[222][0];
			case 2:  return gameText[223][0];
		}
		return "";
	}

	public String getChampJourneyName() {
		return getChampJourneyNameProto(1);
	}

	//#endif
	
	public void calcBanner() {
		Team[] match = null;
		
		// SENSEI
		//#ifndef NOPRESEASON
		if(preSeasonPlaying) {
															
			match = new Team[2];
			match[0] = userTeam;
			match[1] = teams[preSeasonOpps[preSeasonMatchesPlayed][0]][preSeasonOpps[preSeasonMatchesPlayed][1]];
									
			journeyMatch = match;
			
			roundTitle = gameText[GameCanvas.TEXT_SENSEI + 4][3] + match[0].name + GameCanvas.spaceString + gameText[79][0] + GameCanvas.spaceString + match[1].name;

			return;
		}
		//#endif
			
		//#ifndef NOCHAMPIONS
		if (champJourney) {
			for (int i = 0; i < champTeams.length-1; i+=2) {
				if (champTeams[i] == userTeam || champTeams[i+1] == userTeam) {
					match = new Team[2];
					match[0] = champTeams[i];
					match[1] = champTeams[i+1];
				}
			}
			roundTitle = gameText[122][0] + GameCanvas.spaceString + getChampJourneyNameProto(1) + ": " + match[0].name + GameCanvas.spaceString + gameText[79][0] + GameCanvas.spaceString + match[1].name;
			journeyMatch = match;
			return;
		}
		//#endif
		// buscar contra quien nos enfrentamos
		for (int i = 0; i < journeys[journeyNumber].length/2; i++) {
			if (journeys[journeyNumber][i*2] == userTeam || journeys[journeyNumber][i*2+1] == userTeam) {
				match = new Team[2];
				match[0] = journeys[journeyNumber][i*2];
				match[1] = journeys[journeyNumber][i*2+1];
			}
		}
		roundTitle = gameText[123][0] + GameCanvas.spaceString + (journeyNumber + 1) +  GameCanvas.spaceString + gameText[202][0] + GameCanvas.spaceString + (journeyFinal + 1) + ": " + match[0].name + GameCanvas.spaceString + gameText[79][0] + GameCanvas.spaceString + match[1].name;
		journeyMatch = match;
	}

	public String getRoundTitle() {
		return roundTitle;
	}

	private int teamCompare(Team t1, Team t2) {
		int p = 0;
		if (t2.points == t1.points) {
			if ((t2.posGoals - t2.negGoals) == (t1.posGoals - t1.negGoals)) {
				if (t2.posGoals > t1.posGoals) {
					p = 1;
				} else if (t2.posGoals < t1.posGoals) {
					p = -1;
				}
			} else {
				if ((t2.posGoals - t2.negGoals) > (t1.posGoals - t1.negGoals)) {
					p = 1;
				} else if ((t2.posGoals - t2.negGoals) < (t1.posGoals - t1.negGoals)) {
					p = -1;
				}
			}
		} else {
			if (t2.points > t1.points) {
				p = 1;
			} else if (t2.points < t1.points) {
				p = -1;
			}
		}
		return p;
	}

	private boolean teamGT(Team t1, Team t2) {
		return teamCompare(t1, t2) == 1;
	}

	private void bubbleSortTeams(Team a[], int l, int r, int criterion) {
		for (int i = 0; i < a.length; i++) {
			for (int j = i; j < a.length; j++) {
				boolean b = false;
				if (criterion == 0) {
					b = teamGT(a[i], a[j]);
				} else if (criterion == 1) {
					b = a[i].cash < a[j].cash;
				}
				if (b) {
					Team t = a[j];
					a[j] = a[i];
					a[i] = t;
				}
			}
		}
	}

	private void sortLeague() {
		Team[] r = new Team[teams[playingLeague].length];
		System.arraycopy(teams[playingLeague], 0, r, 0, teams[playingLeague].length);
		bubbleSortTeams(r, -1, r.length - 1, 0);
		teams[playingLeague] = r;
	}

	public Team[] classificationInfo() {
		return teams[playingLeague];
	}

	public int userTeamPos() {
		sortLeague();
		for (int i = 0; i < teams[playingLeague].length; i++) {
			if (teams[playingLeague][i] == userTeam) {
				return i;
			}
		}
		return 1;
	}

	public String userTeamPosStr() {
		sortLeague();
		String r = gameText[TEXT_SENSEI - 1][0];
		for (int i = 0; i < teams[playingLeague].length; i++) {
			if (teams[playingLeague][i] == userTeam) {
				if (i == 0) {
					r = gameText[124][0];
				} else if (i == 1) {
					r = gameText[125][0];
				} else if (i == 2) {
					r = gameText[126][0];
				} else {
					r = "" + (i + 1);
				}
			}
		}
		return r + GameCanvas.spaceString + gameText[127][0];
	}

	private void bubbleSortPlayersByGoals(short[] a) {
		for (int i = 0; i < a.length; i++) {
			//if (a[i] == -1)
			//	break;
			for (int j = i; j < a.length; j++) {
				//if (a[j] == -1)
				//	break;
				int aig = a[i] == -1 ? -9999 : playerGetGoals(a[i]);
				int ajg = a[j] == -1 ? -9999 : playerGetGoals(a[j]);
				if (aig < ajg) {
					short t = a[j];
					a[j] = a[i];
					a[i] = t;
				}
			}
		}
	}

	public void bubbleSortTop40() {
		bubbleSortPlayersByGoals(top40Players);
	}

	static public int rand() {
		if (random == null) {
			random = new Random(System.currentTimeMillis());
		}
		int i = random.nextInt();
		return i < 0 ? -i : i;
	}


	private void recordGoal(int p) {
		// si ya estaba el tio en el top 40 se reordena y punto
		for (int i = 0; i < 40; i++) {
			if (top40Players[i] == p) {
				bubbleSortTop40();
				return;
			}
		}
		// sino se intenta insertar al final
		if (top40Players[39] == -1) {
			top40Players[39] = (short)p;
			bubbleSortTop40();
		} else {
			if (playerGetGoals(top40Players[39]) < playerGetGoals(p)) {
				top40Players[39] = (short)p;
				bubbleSortTop40();
			}
		}
		/*if (top40Players[39] == -1) {
			for (int i = 0; i < 40; i++) {
				if (top40Players[i] == p) {
					bubbleSortPlayersByGoals(top40Players);
					return;
				}
				if (top40Players[i] == -1) {
					top40Players[i] = (short)p;
					bubbleSortPlayersByGoals(top40Players);
					break;
				}
			}
			return;
		}
		if (playerGetGoals(top40Players[39]) > playerGetGoals(p))
			return;
		for (int i = 0; i < 40; i++) {
			if (top40Players[i] == p) {
				bubbleSortPlayersByGoals(top40Players);
				return;
			}
		}
		top40Players[39] = (short)p;
		bubbleSortPlayersByGoals(top40Players);*/
	}

	public GameCanvas gamecanvas;
	public boolean matchPopup;
	private String eventMinute(Team teamA, Team teamB, int teamAVal, boolean recordGoals, boolean events) {
		// esta funcion solo se puede llamar por un match donde participa userTeam 
		if (teamA != userTeam && teamB != userTeam) {
			return null;
		}
		if (GameCanvas.cheatActive) userTeam.matchGoals = 7;
		// jugado partida "real", tener en cuenta las bajas
		int downPlayers = 0;
		for (int i = 0; i < 11; i++) {
			if (playerIsSanctioned(userTeam.players[i]) || playerIsInjured(userTeam.players[i]))
				downPlayers++;
		}
		if (teamA == userTeam) {
			teamAVal -= 15*downPlayers;
		} else {
			teamAVal += 15*downPlayers;
		}
		// bonus ofensivo para el contrario cuando el portero esta lesionado
		if (playerIsSanctioned(userTeam.players[0]) || playerIsInjured(userTeam.players[0])) {
			if (teamA != userTeam) {
				teamAVal += 60;
			} else {
				teamAVal -= 60;
			}
		} else
		// bonus ofensivo para el contrario cuando el portero es un no-portero
		if (playerGetSpec(userTeam.players[0]) != 0) {
			if (teamA != userTeam) {
				teamAVal += 30;
			} else {
				teamAVal -= 30;
			}
		}
		// ahora la actitud
		// agresivo
		if (coachGeneralTactic == 1) {
			if (teamA == userTeam) {
				teamAVal += 10;
			} else {
				teamAVal -= 10;
			}
		}
		// defensivo
		if (coachGeneralTactic == 2) {
			if (teamA == userTeam) {
				teamAVal -= 20;
			} else {
				teamAVal += 20;
			}
		}
		if (teamAVal < 0) teamAVal = 0;
		if (teamAVal > 100)	teamAVal = 100;
		//#ifdef ALWAYS_WIN
		//#endif
		//ZNR
		//#ifndef REMOVE_FEATURES
		if (GameCanvas.minute == 1) {
			//#ifdef DOJA
			//#else
			if (gameText[243].length > 2)
				gamecanvas.addMatchEvent(gameText[243][2], null);
			else
				gamecanvas.addMatchEvent(gameText[243][0]+teamA.name+gameText[243][1], null);
			//#endif
		}else if (GameCanvas.minute == 46) {
			gamecanvas.addMatchEvent(gameText[244][0], null);
		}else if (GameCanvas.minute == 85) {
			gamecanvas.addMatchEvent(gameText[244][1], null);
		}else
		//#endif
		//END ZNR
		if ((rand() % 100) < 5) {
			// simular una jugada de gol (o no)
			Team attTeam = null;
			Team defTeam = null;
			if (teamAVal < (rand()%100)) {
				// ataca B
				attTeam = teamB;
				defTeam = teamA;
			} else {
				// ataca A
				attTeam = teamA;
				defTeam = teamB;
			}
			// dificultad de la jugada
			int diff = rand()%100;
			if (attTeam == userTeam) {
				if (coachGeneralTactic == 1) {
					diff -= 20;
				} else if (coachGeneralTactic == 2) {
					diff += 20;
				}
			} else if (defTeam == userTeam) {
				if (coachGeneralTactic == 1) {
					diff -= 20;
				} else if (coachGeneralTactic == 2) {
					diff += 20;
				}
			}

			// si el atacante ya lleva 6, no permitir mas!
			if (attTeam.matchGoals >= 6) {
				diff = 100000000;
			}
			// copia privada de los 11 jugando, shuffleada tb
			short[] attPlayers = new short[10];
			System.arraycopy(attTeam.players, 1, attPlayers, 0, 10);
			shuffleShorts(attPlayers);
			int i = 0;
			short loser = -1;
			while (i < attPlayers.length) {
				if (attPlayers[i] == -1) {
					i++;
					continue;
				}
				if (playerIsInjured(attPlayers[i])) {
					i++;
					continue;
				}
				if (playerIsSanctioned(attPlayers[i])) {
					i++;
					continue;
				}
				// !!!!!! CAMBIAR SEGUN LA TACTICA, NO LA SPEC !!!!!!!
				// ---> combinar con positionPenalization
				int balance = (playerBalance(attPlayers[i], -1) * League.positionGoalProbability[playerGetSpec(attPlayers[i])]) / 90;//75 en la era CCM
				if (balance > diff) {
					// player marca
					if (events) {
//#ifdef DBCUSTOMFONT
//#else
						gamecanvas.addMatchEvent(playerGetName(attPlayers[i]) +
							" (" + attTeam.name + ") " + gameText[128][0], gameText[129][0]);
//#endif
						gamecanvas.hackSound = 3;
					}
					if (recordGoals) {
						playerSetGoals(attPlayers[i], playerGetGoals(attPlayers[i]) + 1);
						recordGoal(attPlayers[i]);
						attTeam.posGoals++;
						defTeam.negGoals++;
					}
					attTeam.matchGoals++;
					loser = -1;
					break;
				}
				// posible perdedor
				loser = attPlayers[i];
				
				i++;
			}
			if (loser != -1 && events) {
				gamecanvas.addMatchEvent(playerGetName(loser) +
					" (" + attTeam.name + ") " + gameText[128][0], gameText[130][0]);
				gamecanvas.hackSound = 2;
			}
		} else if (((rand() % 1000) < 3) && (teamA == userTeam || teamB == userTeam)
			&& (
				userTeam.countPlayers() - countDownPlayers(30) >= 12
				//#ifdef TANASTEN//&& false
				//#endif
			) ) {
			//#ifdef ALWAYS_WIN
			//#else
			int p = userTeam.players[rand() % 11];
			int r = 2 + (rand() % 2);
			if (p != -1 && !playerIsInjured(p) && !playerIsSanctioned(p)) {
				String m = null;
				if (events) {
//#ifdef DBCUSTOMFONT
//#else
					m = playerGetName(p) + " (" + userTeam.name + ") " + gameText[131][0] +
					GameCanvas.spaceString + r + GameCanvas.spaceString + gameText[132][0];
					gamecanvas.addMatchEvent(m, null);
//#endif
					gamecanvas.hackSound = 4;
				}
				playerSetInjured(p, r+1);
				addDownPlayer((short)p);
				matchPopup = true;
				return m;
			}
			//#endif
		}  else if (
				//#ifndef NOPRESEASON
				!preSeasonPlaying && 
				//#endif
				((rand() % 1000) < 5) && (teamA == userTeam || teamB == userTeam)
			&& (
				userTeam.countPlayers() - countDownPlayers(30) >= 12
				//#ifdef TANASTEN
				//&& false
				//#endif
			) ) {
			//#ifdef ALWAYS_WIN
			//#else
			int p = userTeam.players[rand() % 11];
			int r = 1 + rand() % 3;
			if (p != -1 && !playerIsInjured(p) && !playerIsSanctioned(p)) {
				String m = null;
				if (events) {
//#ifdef DBCUSTOMFONT
//#else
					m = playerGetName(p) + " (" + userTeam.name + ") " +
					gameText[133][0] + GameCanvas.spaceString + r + GameCanvas.spaceString + gameText[132][0];
					gamecanvas.addMatchEvent(m, null);
//#endif
					gamecanvas.hackSound = 4;
				}
				playerSetSanctioned(p, r+1);
				addDownPlayer((short)p);
				matchPopup = true;
				return m;
			}
			//#endif
		}
//#ifndef REMOVE_FEATURES
		else if ((rand() % 100) < 15) {
			Team t = (rand() & 1) == 0 ? teamA : teamB;
			int p = t.players[rand() % 11];
			if (p != -1 && !playerIsInjured(p) && !playerIsSanctioned(p)) {
				String m = null;
				if (events) {
					m = playerGetName(p) + " (" + t.name + ") ";
					int i = rand() & 3;
					if (playerGetSpec(p) == 0) { // goalkeeper
						if (i == 0) {
							m += gameText[241][0];
						} else {//if (i == 1) {
							m += gameText[242][0];
						/*} else if (i == 2) {
							m += gameText[243][0];
						} else {
							m += gameText[244][0];*/
						}
					} else if (playerGetSpec(p) == 1) { // defender
						if (i == 0) {
							m += gameText[245][0];
						} else if (i == 1) {
							m += gameText[246][0];
						} else if (i == 2) {
							m += gameText[247][0];
						} else {
							m += gameText[248][0];
						}
					} else if (playerGetSpec(p) == 2) { // midfielder
						if (i == 0) {
							m += gameText[249][0];
						} else if (i == 1) {
							m += gameText[250][0];
						} else if (i == 2) {
							m += gameText[251][0];
						} else {
							m += gameText[252][0];
						}
					} else if (playerGetSpec(p) == 3) { // attacker
						if (i == 0) {
							m += gameText[253][0];
						} else if (i == 1) {
							m += gameText[254][0];
						} else if (i == 2) {
							m += gameText[255][0];
						} else {
							m += gameText[256][0];
						}
					}/* else {
						m += "ERROR";
					}*/
					gamecanvas.addMatchEvent(m, null);
					gamecanvas.hackSound = 1;
				}
				matchPopup = false;
				return m;
			}
		}
//#endif
		return null;
	}

	public boolean eventMatch(Team teamA, Team teamB, boolean recordGoals) {
		int teamAVal = teamA.howMuchBetter(teamB);
		if (teamAVal <= 0)
			teamAVal = 1;
		teamA.matchGoals = 0;
		teamB.matchGoals = 0;
		//int dur = 90 + (rand()%5);
		/*for (int i = 0; i < dur; i++) {
			eventMinute(teamA, teamB, teamAVal, recordGoals, false);
		}*/
		// calculo supersimplificado que mejora bastante la "pinta" de las
		// ligas simuladas. gracias a esto ahora se puede hackear eventMinute
		// tanto como se quiera para matches "de simulacion" (los que se pintan)
		int div = 1 + (rand()%7);
		teamA.matchGoals = (teamAVal / 10) / div;
		teamB.matchGoals = ((100 - teamAVal) / 10) / div;
		if (recordGoals) {
			teamA.posGoals += teamA.matchGoals;
			teamA.negGoals += teamB.matchGoals;
			teamB.posGoals += teamB.matchGoals;
			teamB.negGoals += teamA.matchGoals;
		}
		
		// sortear goles!
		short[] aPlayers = new short[teamA.players.length];
		System.arraycopy(teamA.players, 0, aPlayers, 0, teamA.players.length);
		shuffleShorts(aPlayers);
		// primer delantero
		int rgoals = teamA.matchGoals;
		while (rgoals > 0) {
			for (int i = 0; i < aPlayers.length; i++) {
				if (aPlayers[i] > -1 && playerGetSpec(aPlayers[i]) == 3 && rgoals > 0) {
					if (recordGoals) {
						playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
						recordGoal(aPlayers[i]);
					}
					rgoals--;
				}
				if (rgoals <= 0)
					break;
			}
			for (int i = 0; i < aPlayers.length; i++) {
				if (aPlayers[i] > -1 && playerGetSpec(aPlayers[i]) != 0 && rgoals > 0) {
					if (recordGoals) {
						playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
						recordGoal(aPlayers[i]);
					}
					rgoals--;
				}
				if (rgoals <= 0)
					break;
			}
		}
		// sortear goles!
		aPlayers = new short[teamB.players.length];
		System.arraycopy(teamB.players, 0, aPlayers, 0, teamB.players.length);
		shuffleShorts(aPlayers);
		// primer delantero
		rgoals = teamB.matchGoals;
		while (rgoals > 0) {
			for (int i = 0; i < aPlayers.length; i++) {
				if (aPlayers[i] > -1 && playerGetSpec(aPlayers[i]) == 3 && rgoals > 0) {
					if (recordGoals) {
						playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
						recordGoal(aPlayers[i]);
					}
					rgoals--;
				}
				if (rgoals <= 0)
					break;
			}
			for (int i = 0; i < aPlayers.length; i++) {
				if (aPlayers[i] > -1 && playerGetSpec(aPlayers[i]) != 0 && rgoals > 0) {
					if (recordGoals) {
						playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
						recordGoal(aPlayers[i]);
					}
					rgoals--;
				}
				if (rgoals <= 0)
					break;
			}
		}
		//System.out.println("a: " + teamA.matchGoals + ", b: " + teamB.matchGoals);
		if (teamA.matchGoals > teamB.matchGoals && recordGoals) {
			teamA.points += 3;
			teamA.PG++;
			teamB.PP++;
		} else if (teamA.matchGoals < teamB.matchGoals && recordGoals) {
			teamB.points += 3;
			teamA.PP++;
			teamB.PG++;
		} else if (recordGoals) {
			teamA.points++;
			teamB.points++;
			teamA.PE++;
			teamB.PE++;
		}
		if (recordGoals) {
			teamA.PJ++;
			teamB.PJ++;
		}
		return teamA.matchGoals > teamB.matchGoals;
	}

	private int llrTeamAIndex;
	private int lerTeamAIndex;
	private int champJourneyUserSlot = 0;
	public int champRound = 0;
	
	public void playJourney() {
		//#ifndef NOPRESEASON
		if(!preSeasonPlaying)
		//#endif
		{
			//#ifndef NOCHAMPIONS
			if (champJourney || forceChampPlay) {
				champRound++;
				lastEuropeRound = new Team[champTeams.length];
				lastEuropeResults = new byte[champTeams.length];
				lastEuropeVictories = new byte[champTeams.length];
				System.arraycopy(champTeams, 0, lastEuropeRound, 0, champTeams.length);
				Team[] nct;
				boolean newEuropeanRound = ((champRound%2) == 0 || champTeams.length == 2);
				if (newEuropeanRound)
					nct = new Team[champTeams.length/2];
				else
					nct = new Team[champTeams.length];
				
				if (champTeams.length == 1) {
					playingChamp = false;
				}
				for (int i = 0, j = 0; i < champTeams.length-1; i+=2, j++) {
					if (champTeams[i] != userTeam && champTeams[i+1] != userTeam) {
						
							
						/*if (eventMatch(champTeams[i], champTeams[i+1], false)) {
							nct[j] = champTeams[i];
						} else {
							nct[j] = champTeams[i+1];
						}*/
						eventMatch(champTeams[i], champTeams[i+1], false);
						
						if (newEuropeanRound)
						{
							//System.out.println("NEW ROUND: "+champTeams[i].name+" VS "+champTeams[i+1].name+" - "+nct.length);
							
							int q1 = champTeams[i].realQuality();
							int q2 = champTeams[i+1].realQuality();
							
							//System.out.println("Resultado ida"+champTeams[i].name+": "+champTeams[i].frGoals+" a "+champTeams[i+1].name+": "+champTeams[i+1].frGoals);
							//System.out.println("Resultado vuelta"+champTeams[i].name+": "+champTeams[i].matchGoals+" a "+champTeams[i+1].name+": "+champTeams[i+1].matchGoals);
							int totalA = champTeams[i].matchGoals + champTeams[i].frGoals;
							int totalB = champTeams[i+1].matchGoals + champTeams[i+1].frGoals;
							if (totalA == totalB)
							{
								//System.out.println("Empate de goles entre"+champTeams[i].name+" i "+champTeams[i+1].name);
								totalA = (champTeams[i].matchGoals) + (champTeams[i].frGoals*2);
								totalB = (champTeams[i+1].matchGoals*2) + (champTeams[i+1].frGoals);
								//System.out.println("Desempate: "+totalA+" a "+totalB);
							}
							// HACK PARA LA FINAL
							if (champTeams.length == 2)
							{
								//System.out.println("hack ejecutado");
								totalA = champTeams[i].matchGoals;
								totalB = champTeams[i+1].matchGoals;							
							}
							
							
							if (totalA > totalB) {
								nct[j] = champTeams[i];
								lastEuropeVictories[i] = 1;
								lastEuropeVictories[i+1] = 0;
							} else if (totalA < totalB) {
								nct[j] = champTeams[i+1];
								lastEuropeVictories[i] = 0;
								lastEuropeVictories[i+1] = 1;
							} else {
								//EMPATE
								if (q1 > q2) {
									nct[j] = champTeams[i];
									lastEuropeVictories[i] = 1;
									lastEuropeVictories[i+1] = 0;
								} else {
									nct[j] = champTeams[i+1];
									lastEuropeVictories[i] = 0;
									lastEuropeVictories[i+1] = 1;
								}
							}
							lastEuropeResults[i] = (byte)champTeams[i].matchGoals;
							lastEuropeResults[i+1] = (byte)champTeams[i+1].matchGoals;
						}
						else //NO ES NUEVA RONDA
						{
							int k = j*2;
							nct[k] = champTeams[i+1];
							nct[k+1] = champTeams[i];
							//System.out.println("REPEAT ROUND: "+nct[k].name+" VS "+nct[k+1].name+" - "+nct.length);
							
							//int q1 = champTeams[i].realQuality();
							//int q2 = champTeams[i+1].realQuality();
							if (nct[i].matchGoals > nct[i+1].matchGoals) {
								lastEuropeVictories[i] = 1;
								lastEuropeVictories[i+1] = 0;
							} else if (nct[i].matchGoals < nct[i+1].matchGoals) {
								lastEuropeVictories[i] = 0;
								lastEuropeVictories[i+1] = 1;
							}
							else
							{
								lastEuropeVictories[i] = 0;
								lastEuropeVictories[i+1] = 0;						
							}
							/*} else {
								if (q1 > q2) {
									lastEuropeVictories[i] = 1;
									lastEuropeVictories[i+1] = 0;
								} else {
									lastEuropeVictories[i] = 0;
									lastEuropeVictories[i+1] = 1;
								}
							}*/
							lastEuropeResults[i] = (byte)nct[i].matchGoals;
							lastEuropeResults[i+1] = (byte)nct[i+1].matchGoals;
							nct[i].frGoals = nct[i+1].matchGoals;
							nct[i+1].frGoals = nct[i].matchGoals;							
						}
					} else {
						//ZNRHACK
						if (newEuropeanRound)
						{
							lerTeamAIndex = i;
							nct[j] = null;
							champJourneyUserSlot = j;
						}
						else
						{
							int k = j*2;
							nct[k] = champTeams[i+1];
							nct[k+1] = champTeams[i];
							
							if (champTeams[i] == userTeam)
							{
								lerTeamAIndex = i;
								champJourneyUserSlot = k+1;
								//nct[k] = champTeams[i];
								//nct[k+1] = champTeams[i+1];
																
								//System.out.println("REPEAT ROUND: Player VS "+nct[k+1].name);								
							}
							else
							{
								lerTeamAIndex = i;
								champJourneyUserSlot = k;
								
								//System.out.println("REPEAT ROUND: Player VS "+nct[k].name);
							}
						}
					}
				}
				/*if (nct.length == 1) {
					playingChamp = false;
				}*/
				champTeams = nct;
				// volver solo si era una ronda de champions donde el user participaba
				if (champJourney) 
					return;
			}
			//#endif
			lastLeagueRound = new Team[journeyAllMatches.length];
			lastLeagueResults = new byte[journeyAllMatches.length];
			System.arraycopy(journeyAllMatches, 0, lastLeagueRound, 0, journeyAllMatches.length);
			for (int i = 0; i < journeyAllMatches.length / 2; i++) {
				if (journeyAllMatches[i*2] != userTeam && journeyAllMatches[i*2+1] != userTeam) {
					//#ifndef NOPRESEASON
					eventMatch(journeyAllMatches[i*2], journeyAllMatches[i*2+1], !preSeasonPlaying);
					//#else
					//#endif
					lastLeagueResults[i*2] = (byte)journeyAllMatches[i*2].matchGoals;
					lastLeagueResults[i*2+1] = (byte)journeyAllMatches[i*2+1].matchGoals;
				} else {
					llrTeamAIndex = i*2;
				}
			}
		}
	}

	private Team pmTeamA;
	private Team pmTeamB;
	private int pmTeamAVal;
	public short[] lastMatchDownPlayers;

	private void addDownPlayer(short p) {
		int n = lastMatchDownPlayers.length + 1;
		short[] nl = new short[n];
		System.arraycopy(lastMatchDownPlayers, 0, nl, 0, n - 1);
		nl[n - 1] = p;
		lastMatchDownPlayers = nl;
	}

	public void setupPlayerMatch() {
		lastMatchDownPlayers = new short[0];
		pmTeamA = journeyMatch[0];
		pmTeamB = journeyMatch[1];
		pmTeamA.matchGoals = 0;
		pmTeamB.matchGoals = 0;
		pmTeamAVal = pmTeamA.howMuchBetter(pmTeamB);
	}

	public String tickPlayerMatch(boolean report) {
		//#ifndef NOPRESEASON
		return eventMinute(pmTeamA, pmTeamB, pmTeamAVal, !champJourney && !preSeasonPlaying, report);
		//#else
		//#endif
	}

	public String ppmResultMessage;
	public void postPlayerMatch() {
		// hack: ganar siempre!!
		//System.out.println("HACK!!! GANAS SIEMPRE!!!!!!!!!!!!!!!!!!!");
		//userTeam.matchGoals = 9;

		//System.out.println("postplayermatch");
		
		//ZNR: NEW TRAINING
		for (int i = 0; i < userTeam.players.length; i++) 
		{			
			if (userTeam.players[i] != -1 && !playerIsInjured(userTeam.players[i]))
			{
				int pos = playerGetSpec(userTeam.players[i]);
				//System.out.println("p:"+userTeam.players[i]+" -pos: "+pos);
				//int[] trainCharTotal = new int[5];
				for(int stat = 1; stat < 5;stat++)
				{		
					int statTraining = 0; 
					for(int exercise = 0; exercise < 7;exercise++)
					{
						int statInc = allTrainings[exercise][stat];
						int timeTraining = GameCanvas.trainingSchedule[pos][exercise];
						int res =  	(statInc*timeTraining);
						statTraining += res;
					}
					int t = statTraining;
					statTraining = statTraining / 150; //SERIA 100, pero lo cambio por tunning
					playerIncStat(userTeam.players[i], stat-1, statTraining);
					//System.out.println("st"+(stat-1)+": "+statTraining+" / "+t);
				}
				
			}
		}
		
		//System.out.println("control 1");
        //ZNR: END NEW TRAINING
		
		// quitar entre 3 y 7 puntos entre todos los jugadores
		//#ifndef NOTIREDPLAYERS
		int points = 3 + (rand() % 3);
		while (points > 0) {
			int p = userTeam.randPlayerExtra();
			/*int stat = rand() & 3;
			byte val =  (byte)(playerGetStat(p, stat) - 1); 
			playerSetStat(p, stat, val);*/
			//int stat = rand() & 3;
			byte val =  (byte)(getPlayerQuality(p) - 1);
			//if (val < 1) val = 1;
			setPlayerQuality(p, val);
			points--;
		}
		//#endif
		
		coachXP += 100;		// ganancia fija, en 10 rondas te da 1 entreno extra 
		Team teamA = journeyMatch[0];
		Team teamB = journeyMatch[1];
		//ZNR
		String v = gameText[224][0];//+ gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals; // por defecto pierdes
		
		//System.out.println(champJourney);
		//System.out.println(playingChamp);
		//System.out.println("control 2");
		//#ifndef NOPRESEASON
		if (!preSeasonPlaying)
		//#endif
//#ifndef NOCHAMPIONS
		if (champJourney && playingChamp) {
			lastEuropeResults[lerTeamAIndex] = (byte)teamA.matchGoals;
			lastEuropeResults[lerTeamAIndex+1] = (byte)teamB.matchGoals;
			lastEuropeVictories[lerTeamAIndex] = 0;
			lastEuropeVictories[lerTeamAIndex+1] = 0;
			Team teamV = null;
			
			//ZNRHACK
			/*
			if (teamA.matchGoals == teamB.matchGoals) { // empate de champions
				if ((rand() & 1) == 0) { // empatas y pierdes en la champions
					if (teamA != userTeam)
						teamV = teamA;
					else
						teamV = teamB;
					//if (champTeams.length == 1) {
						// empatas, pierdes y pierdes la champions
						v = gameText[229][0] + " - " + gameText[231][0] + ": " + playerGetName(userTeam.worstAblePlayer(11)) + " " + gameText[232][0];
					//} else {
						// empatas y pierdes en la champions
					//	v = loc[231] + ": " + playerGetName(userTeam.worstAblePlayer(11)) + " " + loc[232] + ". " + loc[229];
					//}
				} else {// empatas y ganas en la champions
					teamV = userTeam;
					Team teamW = teamA;
					if (teamB != userTeam)
						teamW = teamB;

					if (champTeams.length == 1) {
						// empatas y ganas en la champions					
						v = gameText[226][0] + " - " + gameText[225][0] + ": " + playerGetName(teamW.worstAblePlayer(11)) + " " + gameText[232][0];
					} else {
						// empatas y ganas en la champions					
						v = gameText[225][0] + ": " + playerGetName(teamW.worstAblePlayer(11)) + " " + gameText[257][0];
					}
				}
				coachXP += 100;
			} else if (teamA.matchGoals > teamB.matchGoals) {
				teamV = teamA;
				if (teamA == userTeam) {
					if (champTeams.length == 1) {
						v = gameText[226][0];  // 1 - ganas y ganas la champions
					} else {
						v = gameText[227][0] + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;  // 1 - ganas
					}
					coachXP += 500;
				} else {
					v = gameText[229][0]; // pieredes y fuera de la champions
				}
			} else {
				teamV = teamB;
				if (teamB == userTeam) {
					if (champTeams.length == 1) {
						v = gameText[226][0];  // 1 - ganas y ganas la champions
					} else {
						v = gameText[227][0] + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamB.matchGoals + " - " + teamA.matchGoals;;  // 1 - ganas
					}
					coachXP += 500;
				} else {
					v = gameText[229][0]; // pieredes y fuera de la champions
				}
			}
			champTeams[champJourneyUserSlot] = teamV;
			if (teamV == teamA) {
				lastEuropeVictories[lerTeamAIndex] = 1;
			} else {
				lastEuropeVictories[lerTeamAIndex+1] = 1;
			}
			if (champTeams.length == 1) {
				playingChamp = false;
			}
			if (teamV != userTeam) {
				playingChamp = false;
			}
			*/
			
			
			//System.out.println("control 3 nteams: "+ champTeams.length);
			
			if ((champRound%2) == 0 || champTeams.length < 2)
			{
				//ZENER MARK 4
				//NUEVA RONDA
				//#ifdef TANASTEN
				//teamA.matchGoals = teamA.frGoals = teamB.matchGoals = teamB.frGoals = 1;
				//#endif
				
				int totalA = teamA.matchGoals + teamA.frGoals;
				int totalB = teamB.matchGoals + teamB.frGoals;
				if (totalA == totalB)
				{
					totalA = (teamA.matchGoals) + (teamA.frGoals*2);
					totalB = (teamB.matchGoals*2) + (teamB.frGoals);
				}
				
				//HACK PARA LA FINAL
				if (champTeams.length < 2)
				{					
					totalA = teamA.matchGoals;
					totalB = teamB.matchGoals;					
				}
				
				if (totalA == totalB) { // empate de champions
					//#ifndef TANASTEN
					//if ((rand() & 1) == 0) { // empatas y pierdes en la champions
					//#else
					if (true) { // empatas y pierdes en la champions
					//#endif
						if (teamA != userTeam)
							teamV = teamA;
						else
							teamV = teamB;
						// empatas, pierdes y pierdes la champions
						v = 
							//#ifndef SHORTEUROPETEXT
							gameText[229][0] + gameText[90][2] +
							//#endif
						 gameText[231][0];// + ": " + playerGetName(userTeam.worstAblePlayer(11)) + " " + gameText[232][0];
					
					} else {// empatas y ganas en la champions
						teamV = userTeam;
						Team teamW = teamA;
						if (teamB != userTeam)
							teamW = teamB;

						if (champTeams.length == 1) {
							// empatas y ganas en la champions					
							v = gameText[226][0] + gameText[90][2] + gameText[225][0]+ ": " + playerGetName(teamW.worstAblePlayer(11)) + GameCanvas.spaceString + gameText[232][0];
						} else {
							// empatas y ganas en la champions					
							 
							//#ifndef SHORTEUROPETEXT          
							v = gameText[225][0] + ": " + playerGetName(teamW.worstAblePlayer(11)) + GameCanvas.spaceString + gameText[257][0];
							//#else
							//#endif
						}
					}
					coachXP += 100;
				}
				else if (totalA > totalB) 
				{
					teamV = teamA;
					if (teamA == userTeam) 
					{
						if (champTeams.length == 1) 
						{
							v = gameText[226][0];  // 1 - ganas y ganas la champions
						} else {
							v = gameText[85][0];  // 1 - ganas 227
						}
						coachXP += 500;
					} else 
					{
						v = gameText[229][0]; // pieredes y fuera de la champions
					}
				} 
				else 
				{
					teamV = teamB;
					if (teamB == userTeam) 
					{
						if (champTeams.length == 1) 
						{
							v = gameText[226][0];  // 1 - ganas y ganas la champions
						} 
						else 
						{
							v = gameText[85][0];  // 1 - ganas 227
						}
						coachXP += 500;
					} 
					else
					{
						v = gameText[229][0]; // pieredes y fuera de la champions
					}
				}
				if (champTeams.length >= 2)
				{
					v = v+
					gameText[90][0]+ /*. first match: */
					teamB.name+GameCanvas.spaceString+teamB.frGoals+gameText[90][2]+teamA.name+GameCanvas.spaceString+teamA.frGoals+
					gameText[90][1]+/*. second match: */
					teamA.name+GameCanvas.spaceString+teamA.matchGoals+gameText[90][2]+teamB.name+GameCanvas.spaceString+teamB.matchGoals;
				}
				champTeams[champJourneyUserSlot] = teamV;
				if (teamV == teamA) {
					lastEuropeVictories[lerTeamAIndex] = 1;
				} else {
					lastEuropeVictories[lerTeamAIndex+1] = 1;
				}
				if (champTeams.length == 1) {playingChamp = false;}
				if (teamV != userTeam) {playingChamp = false;}
			}else{
				//System.out.println("Entro akissss partido ida "+teamA.matchGoals+" a "+teamB.matchGoals);
				//FALTA PARTIDO DE VUELTA
				v = gameText[230][0];// + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;
				coachXP += 100;
				if (teamA.matchGoals > teamB.matchGoals)
				{
					if (teamA == userTeam)
						v = gameText[227][0];//+ gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;
					else
						v = gameText[224][0];//+ gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;
				}
				else if (teamA.matchGoals < teamB.matchGoals)
				{
					if (teamB == userTeam)
						v = gameText[227][0];//+ gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;
					else
						v = gameText[224][0];//+ gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;
				}
				v +=  gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + gameText[90][2] + teamB.matchGoals;
				teamA.frGoals = teamA.matchGoals;
				teamB.frGoals = teamB.matchGoals;
			}
		} else	
//#endif			
		{
			lastLeagueResults[llrTeamAIndex] = (byte)teamA.matchGoals;
			lastLeagueResults[llrTeamAIndex+1] = (byte)teamB.matchGoals;
			if (teamA.matchGoals > teamB.matchGoals) {
				teamA.points += 3;
				teamA.PG++;
				teamB.PP++;
				if (teamA == userTeam) {
					v = gameText[227][0];// + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;  // 1 - ganas
					coachXP += 350;
				}
			} else if (teamA.matchGoals < teamB.matchGoals) {
				teamB.points += 3;
				teamA.PP++;
				teamB.PG++;
				if (teamB == userTeam) {
					v = gameText[227][0];// + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamB.matchGoals + " - " + teamA.matchGoals;  // 1 - ganas
					coachXP += 350;
				}
			} else {
				teamA.points++;
				teamB.points++;
				teamA.PE++;
				teamB.PE++;
				v = gameText[230][0];// + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals; // empate en la liga
				coachXP += 50;
			}
			v +=  gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + gameText[90][2] + teamB.matchGoals; 
			teamA.PJ++;
			teamB.PJ++;
		}
		//#ifndef NOPRESEASON
		 else {
			
			// SENSEI
			// Partido de pre-temporada
			
			if (teamA.matchGoals > teamB.matchGoals) {				
				if (teamA == userTeam) {
					v = gameText[227][0];// + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamA.matchGoals + " - " + teamB.matchGoals;  // 1 - ganas				
				}
			} else if (teamA.matchGoals < teamB.matchGoals) {				
				if (teamB == userTeam) {
					v = gameText[227][0];// + gameText[GameCanvas.TEXT_SENSEI + 7][0] + teamB.matchGoals + " - " + teamA.matchGoals;  // 1 - ganas					
				}
			} else {				
				v = gameText[230][0]; // empate en la liga				
			}
			v += gameText[GameCanvas.TEXT_SENSEI + 7][0]+ teamA.matchGoals + gameText[90][2] + teamB.matchGoals;; 
		}
		//#endif
		
		// sumar pasta
		if (journeyMatch[0] == userTeam) {
			int p = teams[playingLeague].length - userTeamPos();
			int val = (p * 35) / teams[playingLeague].length;
			coachCash += val;
			//#ifndef NOFINANCES
			journeyIncome += val;
			//#endif
		}
		
		//CHAPU//CHAPU//CHAPU//CHAPU//CHAPU//CHAPU//CHAPU//CHAPU//CHAPUPORTA		
		//v = gameText[229][0] + gameText[90][2] + gameText[231][0];
		//v = v+gameText[90][0]+teamB.name+" 3"+gameText[90][2]+teamA.name+" 3"+gameText[90][1]+teamA.name+"4"+gameText[90][2]+teamB.name+" 4"; 
		
		ppmResultMessage = v;
		//System.out.println("control 4");
	}

	public byte[] saveState() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			// version - 1
			dos.writeByte(14);
			// reservado checksum - 2
			dos.writeShort(0);
			// liga + equipo user - 2
			dos.writeByte(userTeam.loadLeague);
			dos.writeByte(userTeam.globalIdx);
			// jornadas - 3
			dos.writeByte(journeyNumber);
			dos.writeByte(journeyFinal);
			dos.writeByte(realJourneyNumber);
			dos.writeInt(availHires);
			// liga de campeones - maximo de 257
			dos.writeByte(champTeams.length);
			for (int i = 0; i < champTeams.length; i++) {
				dos.writeByte(champTeams[i].globalIdx);
				dos.writeByte(champTeams[i].frGoals);
			}
			dos.writeByte(champRound);
			
			// alineacion equipo user - 50
			for (int i = 0; i < 30; i++) {
				dos.writeShort(userTeam.players[i]);
			}
			// stats jugadores del user - 100
			//ZNR
			for (int i = 0; i < 30; i++) {
				short p = userTeam.players[i];
				if (p >= 0) {
					dos.writeByte(getPlayerQuality(p));					
				} else {
					dos.writeByte(0);					
				}
			}
			// jornadas - unos 800 bytes
			dos.writeByte(journeys.length);
			dos.writeByte(journeys[0].length);
			for (int i = 0; i < journeys.length; i++) {
				for (int j = 0; j < journeys[0].length; j++) {
					dos.writeByte(journeys[i][j].globalIdx);
				}
			}
			// stats equipos en la liga - media 140
			for (int i = 0; i < teams[playingLeague].length; i++) {
				Team t = teams[playingLeague][i];
				dos.writeByte((byte)t.globalIdx);
				dos.writeByte((byte)t.points);
				dos.writeShort((short)t.posGoals);
				dos.writeShort((short)t.negGoals);
				dos.writeByte((byte)t.PJ);
				dos.writeByte((byte)t.PG);
				dos.writeByte((byte)t.PP);
				dos.writeByte((byte)t.PE);
			}
			// top 40 + goles - 120
			for (int i = 0; i < 40; i++) {
				short p = top40Players[i];
				if (p >= 0) {
					dos.writeShort(p);
					dos.writeByte(playerGetGoals(p));
				} else {
					dos.writeShort(-1);
					dos.writeByte(0);
				}
			}
			// jugando champions?
			dos.writeByte(playingChamp ? 1 : 0);
			dos.writeByte(champJourney ? 1 : 0);
			dos.writeByte(forceChampPlay ? 1 : 0);
			// ultimos resultados europa
			if (lastEuropeRound == null) {
				dos.writeByte(0);
			} else {
				dos.writeByte(1);
				dos.writeByte(lastEuropeRound.length);
				for (int i = 0; i < lastEuropeRound.length; i++) {
					dos.writeByte(lastEuropeRound[i].globalIdx);
				}
			}
			if (lastEuropeResults == null) {
				dos.writeByte(0);
			} else {
				dos.writeByte(1);
				dos.writeByte(lastEuropeResults.length);
				for (int i = 0; i < lastEuropeResults.length; i++) {
					dos.writeByte(lastEuropeResults[i]);
				}
			}
			if (lastEuropeVictories == null) {
				dos.writeByte(0);
			} else {
				dos.writeByte(1);
				dos.writeByte(lastEuropeVictories.length);
				for (int i = 0; i < lastEuropeVictories.length; i++) {
					dos.writeByte(lastEuropeVictories[i]);
				}
			}
			if (lastLeagueRound == null) {
				dos.writeByte(0);
			} else {
				dos.writeByte(1);
				dos.writeByte(lastLeagueRound.length);
				for (int i = 0; i < lastLeagueRound.length; i++) {
					dos.writeByte(lastLeagueRound[i].globalIdx);
				}
			}
			if (lastLeagueResults == null) {
				dos.writeByte(0);
			} else {
				dos.writeByte(1);
				dos.writeByte(lastLeagueResults.length);
				for (int i = 0; i < lastLeagueResults.length; i++) {
					dos.writeByte(lastLeagueResults[i]);
				}
			}
			dos.writeInt(llrTeamAIndex);
			dos.writeInt(lerTeamAIndex);
			// entrenador
			dos.writeInt(coachXP);
			//dos.writeByte(coachRemainingTrainings);
			//dos.writeByte(coachRemainingFires);
			dos.writeByte(coachFormation);
			dos.writeByte(coachGeneralTactic);
			dos.writeInt(coachCash);
			
			// SENSEI
			//#ifndef NOPRESEASON
			dos.writeBoolean(preSeasonPlaying);			
			dos.writeInt(preSeasonMatchesPlayed);					
			for(int i = 0; i < PRESEASON_ROUNDS; i++)
			for(int j = 0; j < 2; j++)
					dos.writeInt(preSeasonOpps[i][j]);
			//#endif
			
			//#ifndef NOFINANCES
			dos.writeBoolean(haveSponsor);
			dos.writeInt(sponsorQuality);	
			dos.writeInt(sponsorBenefit);
			//#endif
			
			//#ifndef NOSEASONGOAL
			dos.writeInt(seasonGoal);
			//#endif
			
			//#ifndef NOFINANCES
			dos.writeInt(journeyIncome);
			dos.writeInt(journeyExpenses);
			//#endif
			
			// sanciones y lesiones
			if (blkSanctionedPlayers == null) {
				for (int i = 0; i < 30; i++) {
					dos.writeInt(-1);
				}
			} else {
				for (int i = 0; i < blkSanctionedPlayers.length; i++) {
					dos.writeInt(blkSanctionedPlayers[i]);
				}
			}
			if (blkInjuredPlayers == null) {
				for (int i = 0; i < 30; i++) {
					dos.writeInt(-1);
				}
			} else {
				for (int i = 0; i < blkInjuredPlayers.length; i++) {
					dos.writeInt(blkInjuredPlayers[i]);
				}
			}
			
			//#ifndef NOCALENDAR
			dos.writeInt(season);
			//#endif
			for (int i = 0; i < 30; i++) {
				dos.writeInt(GameCanvas.triedToSell[i]);
			}		
			
			for(int i = 0; i < GameCanvas.trainingSchedule.length; i++) {			
				for(int j = 0; j < GameCanvas.trainingSchedule[i].length; j++) {
					dos.writeInt(GameCanvas.trainingSchedule[i][j]);
				}
			}
				
							
			
			
		} catch (IOException e) { }
		byte[] a = baos.toByteArray();		
		//System.out.println("save " + a.length);
		return a;
	}

	
	private Team teamForGlobalIdx(int idx) {
		for (int i = 0; i < teams.length; i++) {
			for (int j = 0; j < teams[i].length; j++) {
				Team t = teams[i][j];
				if (t.globalIdx == idx)
					return t;
			}
		}
		return null;
	}

	public Team teamForPlayer(short p) {
		for (int i = 0; i < teams.length; i++) {
			for (int j = 0; j < teams[i].length; j++) {
				Team t = teams[i][j];
				if (t.hasPlayer(p))
					return t;
			}
		}
		return null;
	}

	public boolean loadState(byte[] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
		boolean r = false;
		try {
			// version - 1
			int version = dis.readByte();
			// reservado checksum - 2
			int checksum = dis.readShort();
			// liga + equipo user - 2
			playingLeague = dis.readByte();
			userTeam = teamForGlobalIdx(dis.readByte());
			// jornadas - 4
			journeyNumber = dis.readByte();
			journeyFinal = dis.readByte();
			realJourneyNumber = dis.readByte();
			availHires = dis.readInt();
			// liga de campeones - maximo de 257
			int s = (int)dis.readByte();
			champTeams = new Team[s];
			for (int i = 0; i < champTeams.length; i++) {
				champTeams[i] = teamForGlobalIdx(dis.readByte());
				champTeams[i].frGoals = dis.readByte();
			}
			champRound = dis.readByte();
			// alineacion equipo user - 50
			// primero se deja a 0 el equipo del user
			for (int i = 0; i < 30; i++) {
				short p = userTeam.players[0];
				if (p == -1)
					break;
				userTeam.removePlayer(p);
				Team t = vacantTeam();
				while (t == userTeam)
					t = vacantTeam();
				t.addPlayer(p);
			}
			// ahora se anyaden uno a uno, sacandolos de donde esten
			for (int i = 0; i < 30; i++) {
				short p = dis.readShort();
				if (p >= 0) {
					teamForPlayer(p).removePlayer(p);
					userTeam.addPlayer(p);
				}
			}
			
			// stats jugadores del user - 100
			for (int i = 0; i < 30; i++) {
				short p = userTeam.players[i];
				//ZNR
				if (p >= 0) {
					setPlayerQuality(p, dis.readByte());					
				} else {
					dis.readByte();					
				}
			}
			// jornadas - unos 800 bytes
			int l1 = dis.readByte();
			int l2 = dis.readByte();
			journeys = new Team[l1][];
			for (int i = 0; i < journeys.length; i++) {
				journeys[i] = new Team[l2];
				for (int j = 0; j < journeys[0].length; j++) {
					journeys[i][j] = teamForGlobalIdx(dis.readByte());
				}
			}
			
			// stats equipos en la liga - media 140
			Team[] tt = new Team[teams[playingLeague].length];
			for (int i = 0; i < teams[playingLeague].length; i++) {
				int idx = dis.readByte();
				Team t = teamForGlobalIdx(idx);
				tt[i] = t;
				t.points = dis.readByte();
				t.posGoals = dis.readShort();
				t.negGoals = dis.readShort();
				t.PJ = dis.readByte();
				t.PG = dis.readByte();
				t.PP = dis.readByte();
				t.PE = dis.readByte();
			}
			teams[playingLeague] = tt;
			// top 40 + goles - 120
			for (int i = 0; i < 40; i++) {
				short p = dis.readShort();
				byte g = dis.readByte();
				top40Players[i] = p;
				if (p >= 0) {
					playerSetGoals(p, g);
				}
			}
			// jugando champions?
			playingChamp = dis.readByte() == 1;
			champJourney = dis.readByte() == 1;
			forceChampPlay = dis.readByte() == 1;
			// ultimos resultados europa/liga
			if (dis.readByte() == 0) {
				lastEuropeRound = null;
			} else {
				lastEuropeRound = new Team[dis.readByte()];
				for (int i = 0; i < lastEuropeRound.length; i++) {
					lastEuropeRound[i] = teamForGlobalIdx(dis.readByte());
				}
			}
			
			if (dis.readByte() == 0) {
				lastEuropeResults = null;
			} else {
				lastEuropeResults = new byte[dis.readByte()];
				for (int i = 0; i < lastEuropeResults.length; i++) {
					lastEuropeResults[i] = dis.readByte();
				}
			}
			if (dis.readByte() == 0) {
				lastEuropeVictories = null;
			} else {
				lastEuropeVictories = new byte[dis.readByte()];
				for (int i = 0; i < lastEuropeVictories.length; i++) {
					lastEuropeVictories[i] = dis.readByte();
				}
			}
			if (dis.readByte() == 0) {
				lastLeagueRound = null;
			} else {
				lastLeagueRound = new Team[dis.readByte()];
				for (int i = 0; i < lastLeagueRound.length; i++) {
					lastLeagueRound[i] = teamForGlobalIdx(dis.readByte());
				}
			}
			if (dis.readByte() == 0) {
				lastLeagueResults = null;
			} else {
				lastLeagueResults = new byte[dis.readByte()];
				for (int i = 0; i < lastLeagueResults.length; i++) {
					lastLeagueResults[i] = dis.readByte();
				}
			}
			
			llrTeamAIndex = dis.readInt();
			lerTeamAIndex = dis.readInt();
			// variables del entrenador, cuidado no machacar
			coachXP = dis.readInt();
			//coachRemainingTrainings = dis.readByte();
			//coachRemainingFires = dis.readByte();
			coachFormation = dis.readByte();
			coachGeneralTactic = dis.readByte();
			coachCash = dis.readInt();
			
			// SENSEI
			//#ifndef NOPRESEASON
			preSeasonPlaying = dis.readBoolean();
			preSeasonMatchesPlayed = dis.readInt();
			
			preSeasonOpps = new int[PRESEASON_ROUNDS][2];
			
			for(int i = 0; i < PRESEASON_ROUNDS; i++)
			for(int j = 0; j < 2; j++)
				preSeasonOpps[i][j] = dis.readInt();
			//#endif
			
			//#ifndef NOFINANCES
			haveSponsor = dis.readBoolean();
			sponsorQuality = dis.readInt();	
			sponsorBenefit = dis.readInt();
			//#endif
			
			//#ifndef NOSEASONGOAL
			seasonGoal = dis.readInt();
			//#endif
			
			//#ifndef NOFINANCES
			journeyIncome = dis.readInt();
			journeyExpenses = dis.readInt();
			//#endif
						
			calcTopTraining();
			
			// sanciones y lesiones
			blkSanctionedPlayers = new int[30];
			for (int i = 0; i < blkSanctionedPlayers.length; i++) {
				blkSanctionedPlayers[i] = dis.readInt();
			}
			blkInjuredPlayers = new int[30];
			for (int i = 0; i < blkInjuredPlayers.length; i++) {
				blkInjuredPlayers[i] = dis.readInt();
			}
			//#ifndef NOCALENDAR
			season = dis.readInt();
			//#endif
			
			for (int i = 0; i < 30; i++) {
				GameCanvas.triedToSell[i] = dis.readInt();
			}		
			
			for(int i = 0; i < GameCanvas.trainingSchedule.length; i++) {			
				for(int j = 0; j < GameCanvas.trainingSchedule[i].length; j++) {
					GameCanvas.trainingSchedule[i][j] = dis.readInt();
				}
			}
			
			
		} catch (Exception e) {
			r = true;
		}
		return r;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//#ifndef NOCALENDAR
	
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
	

	
	
	
	
	
	static String[] getCurrentDate(int week, int season)
	{
		final int STARTMONTH = 8;
		final int STARTYEAR = 2005;
		
		int d = week;
		
		int curMonth = STARTMONTH;
		int year = 2005;
		int day = CalcFirstOfMonth(year, curMonth);
		//System.out.println("primer dia: "+day);
		if (day != 1) day = ((8-day)%7)+1;
		
		if (champJourney) d += 1;//ay += 7;
		if (lastChampJourney) day += 3;
		
		
		while (d > 1) 
		{
			while (d > 1 && day <= DaysInMonth[curMonth])
			{
				day += 7;d--;
			}
			if (day > DaysInMonth[curMonth])
			{
				curMonth++;
				if (curMonth >= 12){year++;curMonth = 0;}
				day = CalcFirstOfMonth(year, curMonth);
				if (day != 1) day = ((8-day)%7)+1;
				//if (day > 0) day = ((8-day)%7)+1; 
			}
		}
		String m = GameCanvas.gameText[GameCanvas.TEXT_MONTH][curMonth];
		//String dNames[]={"Monday","Tuesday","Wednesday","Thrusday","Friday","Saturday","Sunday"}
		String sday;
		
		if (lastChampJourney) sday = GameCanvas.gameText[GameCanvas.TEXT_WEEKDAY][1];
		else sday = GameCanvas.gameText[GameCanvas.TEXT_WEEKDAY][0];
		String ret[] = new String[4];
		
		ret[0] = ""+year;
		ret[1] = ""+m;
		ret[2] = ""+day;
		ret[3] = ""+sday;
		
		return ret;//sday + ", " + m + ", " + day;
			            
			            
		//int firstDay = CalcFirstOfMonth(int year, int month);
	}
	//#endif
	
	//#ifndef NOCALENDAR
	static int season = 0;
	//#endif
	static int tryingToSell = -1;
	
}

