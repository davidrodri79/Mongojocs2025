package com.mygdx.mongojocs.lma2005;// ---------------------------------------------------------------
// Microjocs Football Manager
// ---------------------------------------------------------------
// Strategical Simulation / Pseudo-RPG Football Team Managing Game
// Programming by Carlos Carrasco


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class League {
	public Team[][] teams;
	//public Team extraTeam;
	public short[] top40Players;
	public int playingLeague;
	public boolean playingChamp;
	public boolean forceChampPlay;
	public Team[] champTeams;
	private Team[][] journeys;
	public int journeyNumber;
	private int realJourneyNumber;
	private boolean champJourney;
	public int journeyFinal;
	public Team userTeam;
	private String matchTitle;
	private String roundTitle;
	public Team[] journeyMatch;
	public Team[] journeyAllMatches;

	public Team[] availHirePlayersTeams;
	public short[] availHirePlayers;

	static private Random random = null;

	public Team[] lastEuropeRound;
	public byte[] lastEuropeResults;
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
		int len = blkPlayerStats[i*6];
		int off = blkPlayerNameOffsets[i];
//#ifdef DBCUSTOMFONT
//#else
		return new String(blkPlayerNames, off, len);
//#endif
	}

	static public int playerGetSpec(int i) {
		return blkPlayerStats[i*6+1];
	}

	static public String playerGetSpecAbbr(int i) {
		return posAbbr[playerGetSpec(i)];
	}

	static public int playerGetStat(int i, int s) {
		return blkPlayerStats[i*6+2+s];
	}

	static public void playerSetStat(int i, int s, byte v) {
		blkPlayerStats[i*6+2+s] = v;
	}

	static private int[] blkSanctionedPlayers = null;
	static private int[] blkInjuredPlayers = null;

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

	static private void sanctionsInjuriesEndJourney() {
		blkEndJourney(blkSanctionedPlayers);
		blkEndJourney(blkInjuredPlayers);
	}
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
		int t = playerGetStat(i, s) + inc;
		if (t > 99)
			t = 99;
		if (t < 0)
			t = 0;
		blkPlayerStats[i*6+2+s] = (byte)t; 
	}

	static public int playerBalance(int i, int sp) {
		if (sp == -1) {
			sp = playerGetSpec(i);
		}
		int acc = 0;
		for (int j = 0; j < 4; j++) {
			acc += (League.statsWeight[sp][j] * playerGetStat(i, j)) / 17;
		}
		int b = acc/4;
		if (b < 1)
			b = 1;
		if (b > 99)
			b = 99;
		return b;
	}

	static public int playerValue(int i) {
		int v = playerBalance(i, -1) * 2;
		return v > 1 ? v : 1;
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

/////////////////////////////////////////////////////////////////////////////////
// constantes del juego

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
		{  3,        0,       0,       4,       0 },
		{  5,        0,       0,       3,       0 },
		{  3,        1,       2,       2,       1 },
		{  5,        1,       1,       1,       1 },
		{  9,        0,       1,       0,       1 },

		{  1,        4,       0,       0,       0 },
		{  2,        2,       0,       1,       0 },
		{  3,        2,       1,       1,       0 },

		{  4,        0,       2,       1,       0 },
		{  2,        1,       3,       0,       0 },
		{  3,        0,       2,       0,       1 },

		{  1,        0,       0,       1,       5 },
		{  3,        0,       1,       1,       3 }
	};

	public static final String[] trainingsNames = {
		null, null, null,
		null, null, null,
		null, null, null,
		null, null, null,
		null
	};

	public static final String[] posAbbr = {
		//"GK", "DE", "CF", "AT"
		null, null, null, null
	};

	public static final String[] statsAbbr = {
		//"PT", "SH", "PA", "GK"
		null, null, null, null
	};

	public static final String[] statsNames = {
		//"PACE TACKLING", "SHOOTING", "PASSING", "GOAL KEEPING"
		null, null, null, null
	};

	public static final String[] formationsNames = {
		"3-4-3", "5-3-2", "4-4-2", "4-3-3", "3-5-2", "5-4-1"
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

/////////////////////////////////////////////////////////////////////////////////
// entrenador

	public int coachTopTraining;
	public int coachRemainingTrainings;
	public int coachRemainingFires;
	public int coachFormation;
	public int coachGeneralTactic;
	public int coachXP;
	public int coachCash;

	public void initCoach() {
		coachRemainingTrainings = 3;
		coachRemainingFires = 1;
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
		coachRemainingTrainings = 3;
		coachRemainingFires = 1;
		calcTopTraining();
	}

	public void coachStartJourney() {
		coachRemainingTrainings = 3;
		coachRemainingFires = 1;
		/*if (coachRemainingFires <= 0) {
			coachRemainingFires = (journeyNumber & 3) == 1 ? 1 : 0;
		}*/
		calcTopTraining();
	}

/////////////////////////////////////////////////////////////////////////////////

	public League() {
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
		initCoach();
	}

	public void load(byte[] plyn, byte[] plys, byte[] tms) {
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
		blkPlayerStats = plys;
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

				//MONGOFIX=========================================
				char char_namea[] = new char[namea.length];
				for(int j = 0; j < namea.length; j++)
				{
					char_namea[j] = (char)(namea[j] < 0 ? namea[j] + 256 : namea[j]);
				}

				String namet = new String(char_namea);
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
				t.load(namet, total, base, money);
				t.loadLeague = (byte)league;
				t.globalIdx = (byte)globalidx;
				//if (league < 5) {
					teams[league][teamsIdx[league]] = t;
					teamsIdx[league]++;
				//}
				base += total;
				globalidx++;
			}
		} catch (IOException e) { }
		// construir offsets de nombres
		blkPlayerNameOffsets = new short[base];
		short accSize = 0;
		for (int i = 0; i < base; i++) {
			blkPlayerNameOffsets[i] = accSize;
			accSize += blkPlayerStats[i*6];
		}
		// allocar goles
		blkPlayerGoals = new byte[base];
		for (int i = 0; i < base; i++) {
			blkPlayerGoals[i] = 0;
		}
		// ordenar equipos por pasta disponible
		for (int i = 0; i < League.leagueTeams.length; i++) {
			bubbleSortTeams(teams[i], -1, teams[i].length - 1, 1);
		}
	}

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
		for (int i = 0; i < teams[playingLeague].length; i++) {
			teams[playingLeague][i].startSeason();
		}
		//shuffleTeams(teams[playingLeague]);

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

		// championship
		playingChamp = true;
		if (leagueOrdinal(userTeam) < 4) {
			// 16 equipos: 4 de la actual, 3 de las 4 restantes <- NO
			// 16 equipos: 6 de la actual, 2 de las 5 restantes <- SI
			champTeams = new Team[16];
			for (int i = 0, k = 0; i < teams.length; i++) {
				for (int j = 0; j < 6; j++) {
					if (j == 2 && i != playingLeague)
						break;
					champTeams[k] = teams[i][j];
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
						k++;
					}
				}	
				j++;
			}
		}

		shuffleTeams(champTeams);

		// sanciones y lesiones
		sanctionsInjuriesStartSeason();
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
		int sameLeagueN = rand() % 3;
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
		}
		if (resetCoach)
			coachStartJourney();
		calcBanner();
		journeyAllMatches = journeys[journeyNumber];
	}

	public void clearHires() {
		availHirePlayersTeams = null;
		availHirePlayers = null;
	}

	public void endJourney() {
		int chint = 3;
		if (champTeams != null && champTeams.length <= 16 && journeyNumber < 15) {
			chint = 5;
		}
		realJourneyNumber++;
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
		sortLeague();
		sanctionsInjuriesEndJourney();
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

	public String[] getChampMatches() {
		String[] r = null;
		if (champTeams.length == 0) {
			r = new String[1];
			r[0] = "ERROR";
		} else if (champTeams.length == 1) {
			r = new String[1];
			r[0] = GameCanvas.loc[217] + ": " + champTeams[0].name;
		} else {
			r = new String[champTeams.length/2];
			for (int i = 0, j = 0; i < champTeams.length-1; i+=2, j++) {
				r[j] = champTeams[i].name + " vs. " + champTeams[i+1].name;
			}
		}
		return r;
	}

	public String getChampJourneyNameProto(int m) {
		switch (champTeams.length*m) {
			case 64: return GameCanvas.loc[218];
			case 32: return GameCanvas.loc[219];
			case 16: return GameCanvas.loc[220];
			case 8:  return GameCanvas.loc[221];
			case 4:  return GameCanvas.loc[222];
			case 2:  return GameCanvas.loc[223];
		}
		return "ERROR";
	}

	public String getChampJourneyName() {
		return getChampJourneyNameProto(1);
	}

	public void calcBanner() {
		Team[] match = null;
		if (champJourney) {
			for (int i = 0; i < champTeams.length-1; i+=2) {
				if (champTeams[i] == userTeam || champTeams[i+1] == userTeam) {
					match = new Team[2];
					match[0] = champTeams[i];
					match[1] = champTeams[i+1];
				}
			}
			roundTitle = GameCanvas.loc[122] + " " + getChampJourneyName() + ": " + match[0].name + " " + GameCanvas.loc[79] + " " + match[1].name;
			journeyMatch = match;
			return;
		}
		// buscar contra quien nos enfrentamos
		for (int i = 0; i < journeys[journeyNumber].length/2; i++) {
			if (journeys[journeyNumber][i*2] == userTeam || journeys[journeyNumber][i*2+1] == userTeam) {
				match = new Team[2];
				match[0] = journeys[journeyNumber][i*2];
				match[1] = journeys[journeyNumber][i*2+1];
			}
		}
		roundTitle = GameCanvas.loc[123] + " " + (journeyNumber + 1) +  " " + GameCanvas.loc[202] + " " + (journeyFinal + 1) + ": " + match[0].name + " " + GameCanvas.loc[79] + " " + match[1].name;
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
		String r = "ERROR";
		for (int i = 0; i < teams[playingLeague].length; i++) {
			if (teams[playingLeague][i] == userTeam) {
				if (i == 0) {
					r = GameCanvas.loc[124];
				} else if (i == 1) {
					r = GameCanvas.loc[125];
				} else if (i == 2) {
					r = GameCanvas.loc[126];
				} else {
					r = i + " " + GameCanvas.loc[127];
				}
			}
		}
		return r;
	}

	private void bubbleSortPlayersByGoals(short[] a) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] == -1)
				break;
			for (int j = i; j < a.length; j++) {
				if (a[j] == -1)
					break;
				if (playerGetGoals(a[i]) < playerGetGoals(a[j])) {
					short t = a[j];
					a[j] = a[i];
					a[i] = t;
				}
			}
		}
	}

	static public int rand() {
		if (random == null) {
			random = new Random(System.currentTimeMillis());
		}
		int i = random.nextInt();
		return i < 0 ? -i : i;
	}


	private void recordGoal(int p) {
		if (top40Players[39] == -1) {
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
		bubbleSortPlayersByGoals(top40Players);
	}

	public GameCanvas gamecanvas;
	public boolean matchPopup;
	private String eventMinute(Team teamA, Team teamB, int teamAVal, boolean recordGoals, boolean events) {
		// esta funcion solo se puede llamar por un match donde participa userTeam 
		if (teamA != userTeam && teamB != userTeam) {
			return null;
		}
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
		// ahora la actitud
		// agresivo
		if (coachGeneralTactic == 1) {
			if (teamA == userTeam) {
				teamAVal += 20;
			} else {
				teamAVal -= 20;
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
		if (teamAVal < 0)
			teamAVal = 0;
		if (teamAVal > 100)
			teamAVal = 100;
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

			// si el atacante ya lleva 9, no permitir mas!
			if (attTeam.matchGoals >= 9) {
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
				int balance = (playerBalance(attPlayers[i], -1) * League.positionGoalProbability[playerGetSpec(attPlayers[i])]) / 75;
				if (balance > diff) {
					// player marca
					if (events) {
//#ifdef DBCUSTOMFONT
// #else
						gamecanvas.addMatchEvent(playerGetName(attPlayers[i]) +
							" (" + attTeam.name + ") " + GameCanvas.loc[128], GameCanvas.loc[129]);
//#endif
						gamecanvas.hackSound = 3;
					}
					playerSetGoals(attPlayers[i], playerGetGoals(attPlayers[i]) + 1);
					if (recordGoals) {
						recordGoal(attPlayers[i]);
					}
					attTeam.matchGoals++;
					attTeam.posGoals++;
					defTeam.negGoals++;
					loser = -1;
					break;
				} else {
					// posible perdedor
					loser = attPlayers[i];
				}
				i++;
			}
			if (loser != -1 && events) {
				gamecanvas.addMatchEvent(playerGetName(loser) +
					" (" + attTeam.name + ") " + GameCanvas.loc[128], GameCanvas.loc[130]);
				gamecanvas.hackSound = 2;
			}
		} else if (((rand() % 1000) < 8) && (teamA == userTeam || teamB == userTeam)) {
			int p = userTeam.players[rand() % 11];
			int r = 1 + rand() % 3;
			if (p != -1 && !playerIsInjured(p) && !playerIsSanctioned(p)) {
				String m = null;
				if (events) {
//#ifdef DBCUSTOMFONT
//#else
					m = playerGetName(p) + " (" + userTeam.name + ") " + GameCanvas.loc[131] +
						" " + r + " " + GameCanvas.loc[132];
					gamecanvas.addMatchEvent(m, null);
//#endif
					gamecanvas.hackSound = 4;
				}
				playerSetInjured(p, r+1);
				matchPopup = true;
				return m;
			}
		}  else if (((rand() % 1000) < 8) && (teamA == userTeam || teamB == userTeam)) {
			int p = userTeam.players[rand() % 11];
			int r = 1 + rand() % 3;
			if (p != -1 && !playerIsInjured(p) && !playerIsSanctioned(p)) {
				String m = null;
				if (events) {
//#ifdef DBCUSTOMFONT
//#else
					m = playerGetName(p) + " (" + userTeam.name + ") " +
						GameCanvas.loc[133] + " " + r + " " + GameCanvas.loc[132];
					gamecanvas.addMatchEvent(m, null);
//#endif
					gamecanvas.hackSound = 4;
				}
				playerSetSanctioned(p, r+1);
				matchPopup = true;
				return m;
			}
		} else if ((rand() % 100) < 15) {
			Team t = (rand() & 1) == 0 ? teamA : teamB;
			int p = t.players[rand() % 11];
			if (p != -1 && !playerIsInjured(p) && !playerIsSanctioned(p)) {
				String m = null;
				if (events) {
					m = playerGetName(p) + " (" + t.name + ") ";
					int i = rand() & 3;
					if (playerGetSpec(p) == 0) { // goalkeeper
						if (i == 0) {
							m += "stops a corner shoot";
						} else if (i == 1) {
							m += "argues with the public";
						} else if (i == 2) {
							m += "looks bored";
						} else {
							m += "dodges an object thrown from the stadium";
						}
					} else if (playerGetSpec(p) == 1) { // defender
						if (i == 0) {
							m += "stops the ball on the goal area";
						} else if (i == 1) {
							m += "goes out the playfield blocking a player";
						} else if (i == 2) {
							m += "gets angry at a rival attacker";
						} else {
							m += "talks with his goalkeeper";
						}
					} else if (playerGetSpec(p) == 2) { // midfielder
						if (i == 0) {
							m += "organizes an attack";
						} else if (i == 1) {
							m += "does a long pass";
						} else if (i == 2) {
							m += "argues with the referee";
						} else {
							m += "passes the ball to the attackers";
						}
					} else if (playerGetSpec(p) == 3) { // attacker
						if (i == 0) {
							m += "was in out-of-game position.";
						} else if (i == 1) {
							m += "dodges an angry defender";
						} else if (i == 2) {
							m += "gets the ball";
						} else {
							m += "decides to not pass the ball to another attacker";
						}
					} else {
						m += "ERROR";
					}
					gamecanvas.addMatchEvent(m, null);
					gamecanvas.hackSound = 1;
				}
				matchPopup = false;
				return m;
			}
		}
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
		teamA.posGoals += teamA.matchGoals;
		teamA.negGoals += teamB.matchGoals;
		teamB.posGoals += teamB.matchGoals;
		teamB.negGoals += teamA.matchGoals;
		
		// sortear goles!
		short[] aPlayers = new short[teamA.players.length];
		System.arraycopy(teamA.players, 0, aPlayers, 0, teamA.players.length);
		shuffleShorts(aPlayers);
		// primer delantero
		int rgoals = teamA.matchGoals;
		while (rgoals > 0) {
			for (int i = 0; i < aPlayers.length; i++) {
				if (aPlayers[i] > -1 && playerGetSpec(aPlayers[i]) == 3 && rgoals > 0) {
					playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
					if (recordGoals) {
						recordGoal(aPlayers[i]);
					}
					rgoals--;
				}
				if (rgoals <= 0)
					break;
			}
			for (int i = 0; i < aPlayers.length; i++) {
				if (aPlayers[i] > -1 && playerGetSpec(aPlayers[i]) != 0 && rgoals > 0) {
					playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
					if (recordGoals) {
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
					playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
					if (recordGoals) {
						recordGoal(aPlayers[i]);
					}
					rgoals--;
				}
				if (rgoals <= 0)
					break;
			}
			for (int i = 0; i < aPlayers.length; i++) {
				if (aPlayers[i] > -1 && playerGetSpec(aPlayers[i]) != 0 && rgoals > 0) {
					playerSetGoals(aPlayers[i], playerGetGoals(aPlayers[i]) + 1);
					if (recordGoals) {
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
	public void playJourney() {
		if (champJourney || forceChampPlay) {
			lastEuropeRound = new Team[champTeams.length];
			lastEuropeResults = new byte[champTeams.length];
			System.arraycopy(champTeams, 0, lastEuropeRound, 0, champTeams.length);
			Team[] nct = new Team[champTeams.length/2];
			for (int i = 0, j = 0; i < champTeams.length-1; i+=2, j++) {
				if (champTeams[i] != userTeam && champTeams[i+1] != userTeam) {
					if (eventMatch(champTeams[i], champTeams[i+1], false)) {
						nct[j] = champTeams[i];
					} else {
						nct[j] = champTeams[i+1];
					}
					lastEuropeResults[i] = (byte)champTeams[i].matchGoals;
					lastEuropeResults[i+1] = (byte)champTeams[i+1].matchGoals;
				} else {
					lerTeamAIndex = i;
					nct[j] = null;
					champJourneyUserSlot = j;
				}
			}
			if (nct.length == 1) {
				playingChamp = false;
			}
			champTeams = nct;
			// volver solo si era una ronda de champions donde el user participaba
			if (champJourney) 
				return;
		}
		lastLeagueRound = new Team[journeyAllMatches.length];
		lastLeagueResults = new byte[journeyAllMatches.length];
		System.arraycopy(journeyAllMatches, 0, lastLeagueRound, 0, journeyAllMatches.length);
		for (int i = 0; i < journeyAllMatches.length / 2; i++) {
			if (journeyAllMatches[i*2] != userTeam && journeyAllMatches[i*2+1] != userTeam) { 
				eventMatch(journeyAllMatches[i*2], journeyAllMatches[i*2+1], true);
				lastLeagueResults[i*2] = (byte)journeyAllMatches[i*2].matchGoals;
				lastLeagueResults[i*2+1] = (byte)journeyAllMatches[i*2+1].matchGoals;
			} else {
				llrTeamAIndex = i*2;
			}
		}
	}

	private Team pmTeamA;
	private Team pmTeamB;
	private int pmTeamAVal;

	public void setupPlayerMatch() {
		pmTeamA = journeyMatch[0];
		pmTeamB = journeyMatch[1];
		pmTeamA.matchGoals = 0;
		pmTeamB.matchGoals = 0;
		pmTeamAVal = pmTeamA.howMuchBetter(pmTeamB);
	}

	public String tickPlayerMatch(boolean report) {
		return eventMinute(pmTeamA, pmTeamB, pmTeamAVal, !champJourney, report);
	}

	public String ppmResultMessage;
	public void postPlayerMatch() {
		// quitar entre 3 y 7 puntos entre todos los jugadores
		int points = 3 + (rand() % 5);
		while (points > 0) {
			int p = userTeam.randPlayerExtra();
			int stat = rand() & 3;
			byte val =  (byte)(playerGetStat(p, stat) - 1); 
			playerSetStat(p, stat, val);
			points--;
		}
		
		coachXP += 100;		// ganancia fija, en 10 rondas te da 1 entreno extra 
		String v = GameCanvas.loc[224]; // por defecto pierdes
		Team teamA = journeyMatch[0];
		Team teamB = journeyMatch[1];
		if (champJourney && playingChamp) {
			lastEuropeResults[lerTeamAIndex] = (byte)teamA.matchGoals;
			lastEuropeResults[lerTeamAIndex+1] = (byte)teamB.matchGoals;
			Team teamV = null;
			if (teamA.matchGoals == teamB.matchGoals) { // empate de champions
				if ((rand() & 1) == 0) {
					if (teamA != userTeam)
						teamV = teamA;
					else
						teamV = teamB;
					v = GameCanvas.loc[231] + ": " + playerGetName(userTeam.worstAblePlayer(11)) + " " + GameCanvas.loc[232] + ". " + GameCanvas.loc[229]; // empatas y pierdes en la champions
				} else {
					teamV = userTeam;
					Team teamW = teamA;
					if (teamB != userTeam)
						teamW = teamB;
					v = GameCanvas.loc[225] + ": " + playerGetName(teamW.worstAblePlayer(11)) + " failed."; // empatas y ganas en la champions
				}
				coachXP += 100;
			} else if (teamA.matchGoals > teamB.matchGoals) {
				teamV = teamA;
				if (teamA == userTeam) {
					if (champTeams.length == 1) {
						v = GameCanvas.loc[226];  // 1 - ganas y ganas la champions
					} else {
						v = GameCanvas.loc[227];  // 1 - ganas
					}
					coachXP += 500;
				} else {
					v = GameCanvas.loc[229]; // pieredes y fuera de la champions
				}
			} else {
				teamV = teamB;
				if (teamB == userTeam) {
					if (champTeams.length == 1) {
						v = GameCanvas.loc[228];  // 1 - ganas y ganas la champions
					} else {
						v = GameCanvas.loc[227];  // 1 - ganas
					}
					coachXP += 500;
				} else {
					v = GameCanvas.loc[229]; // pieredes y fuera de la champions
				}
			}
			champTeams[champJourneyUserSlot] = teamV;
			if (teamV != userTeam) {
				playingChamp = false;
			}
		} else {
			lastLeagueResults[llrTeamAIndex] = (byte)teamA.matchGoals;
			lastLeagueResults[llrTeamAIndex+1] = (byte)teamB.matchGoals;
			if (teamA.matchGoals > teamB.matchGoals) {
				teamA.points += 3;
				teamA.PG++;
				teamB.PP++;
				if (teamA == userTeam) {
					v = GameCanvas.loc[227];  // 1 - ganas
					coachXP += 350;
				}
			} else if (teamA.matchGoals < teamB.matchGoals) {
				teamB.points += 3;
				teamA.PP++;
				teamB.PG++;
				if (teamB == userTeam) {
					v = GameCanvas.loc[227];  // 1 - ganas
					coachXP += 350;
				}
			} else {
				teamA.points++;
				teamB.points++;
				teamA.PE++;
				teamB.PE++;
				v = GameCanvas.loc[230]; // empate en la liga
				coachXP += 50;
			}
			teamA.PJ++;
			teamB.PJ++;
		}
		ppmResultMessage = v;
	}

	public byte[] saveState() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			// version - 1
			dos.writeByte(7);
			// reservado checksum - 2
			dos.writeShort(0);
			// liga + equipo user - 2
			dos.writeByte(userTeam.loadLeague);
			dos.writeByte(userTeam.globalIdx);
			// jornadas - 3
			dos.writeByte(journeyNumber);
			dos.writeByte(journeyFinal);
			dos.writeByte(realJourneyNumber);
			// liga de campeones - maximo de 257
			dos.writeByte(champTeams.length);
			for (int i = 0; i < champTeams.length; i++) {
				dos.writeByte(champTeams[i].globalIdx);
			}
			// alineacion equipo user - 50
			for (int i = 0; i < 30; i++) {
				dos.writeShort(userTeam.players[i]);
			}
			// stats jugadores del user - 100
			for (int i = 0; i < 30; i++) {
				short p = userTeam.players[i];
				if (p >= 0) {
					dos.writeByte(playerGetStat(p, 0));
					dos.writeByte(playerGetStat(p, 1));
					dos.writeByte(playerGetStat(p, 2));
					dos.writeByte(playerGetStat(p, 3));
				} else {
					dos.writeByte(0);
					dos.writeByte(0);
					dos.writeByte(0);
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
				dos.writeByte((byte)t.posGoals);
				dos.writeByte((byte)t.negGoals);
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
			// entrenador
			dos.writeInt(coachXP);
			dos.writeByte(coachRemainingTrainings);
			dos.writeByte(coachRemainingFires);
			dos.writeByte(coachFormation);
			dos.writeByte(coachGeneralTactic);
			dos.writeInt(coachCash);
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
		} catch (IOException e) { }
		byte[] a = baos.toByteArray();
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
			// jornadas - 3
			journeyNumber = dis.readByte();
			journeyFinal = dis.readByte();
			realJourneyNumber = dis.readByte();
			// liga de campeones - maximo de 257
			int s = (int)dis.readByte();
			champTeams = new Team[s];
			for (int i = 0; i < champTeams.length; i++) {
				champTeams[i] = teamForGlobalIdx(dis.readByte());
			}
			// alineacion equipo user - 50
			// primero se deja a 0 el equipo del user
			for (int i = 0; i < 30; i++) {
				short p = userTeam.players[0];
				if (p == -1)
					break;
				userTeam.removePlayer(p);
				vacantTeam().addPlayer(p);
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
				if (p >= 0) {
					playerSetStat(p, 0, dis.readByte());
					playerSetStat(p, 1, dis.readByte());
					playerSetStat(p, 2, dis.readByte());
					playerSetStat(p, 3, dis.readByte());
				} else {
					dis.readByte();
					dis.readByte();
					dis.readByte();
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
				t.posGoals = dis.readByte();
				t.negGoals = dis.readByte();
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
			// variables del entrenador, cuidado no machacar
			coachXP = dis.readInt();
			coachRemainingTrainings = dis.readByte();
			coachRemainingFires = dis.readByte();
			coachFormation = dis.readByte();
			coachGeneralTactic = dis.readByte();
			coachCash = dis.readInt();
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
		} catch (Exception e) {
			r = true;
		}
		return r;
	}
}

