package com.mygdx.mongojocs.bravewar;

class BattleAction {
	
	public int type, xori, zori, xdes, zdes, amount, who, timer;
	
	public BattleAction(int ti, int w, int t, int x1, int z1, int x2, int z2, int am)
	{
	
		type=t; xori=x1; zori=z1; xdes=x2; zdes=z2; amount=am; who=w; timer=ti;
		
	}	
}

public class Round {

	public static final int MOVE = 0;	
	public static final int ATTACK = 1;

	public BattleAction ac[];
	public int nActions, currentAction, localPlayerId;

	public Round(int Id)
	{
		ac=new BattleAction[20];
		nActions=0; currentAction=0;
		localPlayerId=Id;				
	}
	
	
	public void addAttackAction(int ti, int x1, int z1, int x2, int z2)
	{
		ac[nActions]=new BattleAction(ti,localPlayerId,Round.ATTACK,x1,z1,x2,z2,0);
		nActions++;
	}	
	
	public void addMoveAction(int ti,int x1, int z1, int x2, int z2, int am)
	{
		ac[nActions]=new BattleAction(ti,localPlayerId,Round.MOVE,x1,z1,x2,z2,am);
		nActions++;
	}		
	
	public boolean notUsedTile(int x, int z)
	{
		for(int i=0; i<nActions; i++)
			if(ac[i].xori==x && ac[i].zori==z) return false;
			
		return true;		
	}
	
	public void send ()
	{
		GameMidletLogic.theGameMidlet.sendRound (this);
	}
	
	public void reverseOpponentActions(WarField wf)
	{
		for(int i=0; i<nActions; i++)
			if(ac[i].who!=localPlayerId){				
					ac[i].xori=wf.sx-2-ac[i].xori;
					ac[i].zori=wf.sz-2-ac[i].zori;
					ac[i].xdes=wf.sx-2-ac[i].xdes;
					ac[i].zdes=wf.sz-2-ac[i].zdes;
					//System.out.println("("+ac[i].xori+","+ac[i].zori+")->("+ac[i].xdes+","+ac[i].zdes+")");
				
				}		
	}
	
	public  void sortByTime()
	{	
		BattleAction b;

		//display();
			
		for(int j=0; j<nActions; j++)	
		for(int i=0; i<nActions-1; i++)
			
			if(ac[i+1].timer<ac[i].timer){
				
				b=ac[i+1];
				ac[i+1]=ac[i];
				ac[i]=b;		
			}	
		
		//System.out.println("Sorted");	
		//display();
								
	}
	
	/*public void display()
	{
		for(int i=0; i<nActions; i++)
			System.out.println("Accion "+i+": Jugador "+ac[i].who+" Timer="+ac[i].timer);
				
	}*/

}