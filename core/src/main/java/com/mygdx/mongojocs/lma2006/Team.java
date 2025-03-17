package com.mygdx.mongojocs.lma2006;// ---------------------------------------------------------------
// Microjocs Football Manager
// ---------------------------------------------------------------
// Strategical Simulation / Pseudo-RPG Football Team Managing Game
// Programming by Carlos Carrasco, Carlos Peris and David Rodr�guez

//#ifdef WEBSUBCRIPTION
//#define NOCHAMPIONS
//#endif

import java.io.IOException;

public class Team {
	public byte loadLeague;
	public byte globalIdx;
	public String name;
	private int cachedQuality;
	public int points;
	public int matchGoals;
	public int frGoals;
	public int posGoals;
	public int negGoals;
	public int PJ;
	public int PG;
	public int PP;
	public int PE;
	public int cash;
	private boolean dirtyQuality;
	public short[] players = null;
	//#ifndef NOCLUBBAR
	public int color1;
	public int color2;
	//#endif
	
	//#ifdef LOWMEM
	//#endif
	
	public Team() {
		name = null;
		cachedQuality = 0;
		cash = 0;
		players = new short[30];
		dirtyQuality = true;
	}

//#ifndef NOCLUBBAR
	public void load(String n, int total, int base, int money, int r1, int g1, int b1, int r2, int g2, int b2) throws IOException {
//#else
//#endif
		//#ifndef NOCLUBBAR
		color1 = (r1<<16) + (g1<<8) + b1;
		color2 = (r2<<16) + (g2<<8) + b2;
		//#endif
		cash = money;
		name = n;
		for (int i = 0; i < 30; i++) {
			if (i < total) {
				players[i] = (short)(i + base);
			} else {
				players[i] = -1;
			}
		}
		//#ifdef LOWMEM
		//#endif
	}

	public void startSeason() {
		points = 0;
		matchGoals = 0;
		posGoals = 0;
		negGoals = 0;
		PJ = 0;
		PG = 0;
		PP = 0;
		PE = 0;
		dirtyQuality = true;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == -1)
				break;
			League.playerSetGoals(players[i], 0);
		}
	}

	public int countPlayers() {
		int i = 0;
		for (i = 0; i < players.length; i++) {
			if (players[i] == -1)
				break;
		}
		return i;
	}

	public int realQuality() {
		if (!dirtyQuality)
			return cachedQuality;
		int  acc = 0;
		int i = 0;
		for (; i < players.length; i++) {
			if (players[i] == -1)
				break;
			//ZNR
			acc += League.getPlayerQuality(players[i]);
			//System.out.println("qcal:"+League.getPlayerQuality(players[i]));
			//ZNR
			/*
			for (int j = 0; j < 4; j++) {
				//System.out.println("q " + j + ": " + League.playerGetStat(players[i], j));
				//System.out.println("w " + League.statsWeight[League.playerGetSpec(players[i])][j]);
				acc += (League.statsWeight[League.playerGetSpec(players[i])][j] * League.playerGetStat(players[i], j)) / 6;
			}*/
		}
		if (i == 0)
			i = 1;
		//cachedQuality = acc / (i*7);
		//ZNR
		cachedQuality = acc / (i);
		//System.out.println("cachedQuality:"+cachedQuality);
		dirtyQuality = false;
		if (cachedQuality == 0)
			cachedQuality = 1;
		return cachedQuality;
	}

	public void markDirtyQuality() {
		dirtyQuality = true;
	}

	public int howMuchBetter(Team other) {
		int q = realQuality();
		// valoracion general de que equipo es el mejor (this or other)
		// bonus x2 para el mejor, siempre que haya mas de 10 puntos de diferencia
		int diff = (q*100)/(q + other.realQuality());
		if (Math.abs(q - other.realQuality()) > 10) {
			if (diff > 50) {
				diff += (100 - diff) / 2;
			}
		}
		return diff;
	}

	public void swapOrdinalToOrdinal(int from, int to) {
		if ((players[from] == -1) || (players[to] == -1))
			return;
		short p = players[from];
		players[from] = players[to];
		players[to] = p;
		int t = GameCanvas.triedToSell[from];
		GameCanvas.triedToSell[from] = GameCanvas.triedToSell[to];
		GameCanvas.triedToSell[to] = t;
	}

	public void removePlayer(int p) {
		short[] np = new short[30];
		int j = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != p) {
				np[j] = players[i];
				if (League.userTeam == this) GameCanvas.triedToSell[j] = GameCanvas.triedToSell[i]; 
				j++;
			}			
		}
		for (; j < np.length; j++) {
			np[j] = -1;
			if (League.userTeam == this) GameCanvas.triedToSell[j] = 0;
		}
		players = np;
	}

	public void addPlayer(int p) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == -1) {
				players[i] = (short)p;				
				break;
			}
		}
	}

	public short randPlayer(int aproxQuality) {
		// TODO: considerar calidad
		short p = -1;
		while (p == -1) {
			p = players[League.rand() % 30];
		}
		return p;
	}

	public short randPlayerExtra() {
		short p = -1;
		if (countPlayers() <= 16)
			return randPlayer(0);
		while (p == -1) {
			p = players[16 + (League.rand() % 14)];
		}
		return p;
	}

	public boolean hasPlayer(short p) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == -1) {
				break;
			}
			if (players[i] == p) {
				return true;
			}
		}
		return false;
	}

	public short worstAblePlayer(int top) {
		short p = 0;
		int qa = 2000;
		for (int i = 0; i < top; i++) {
			if (players[i] == -1) {
				break;
			}
			int q = League.playerBalance(players[i], -1);
			if (!League.playerIsInjured(players[i]) &&
				!League.playerIsSanctioned(players[i]) &&
				q < qa) {
				qa = q;
				p = players[i];
			}
		}
		return p;
	}

}

