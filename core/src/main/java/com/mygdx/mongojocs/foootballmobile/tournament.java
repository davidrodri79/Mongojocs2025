package com.mygdx.mongojocs.foootballmobile;


import java.io.DataInputStream;
import java.io.DataOutputStream;

public class tournament
{

	int evol[][], goals[][], current, total, levels;
	

	public tournament(int l)
	{
		levels = l;
		
		evol = new int[levels][];
		goals = new int[levels][];
		
		evol[0] = new int[1]; evol[0][0] = -1; goals[0] = new int[1]; goals[0][0] = 0;
		
		for(int i = 1; i< evol.length; i++){
			
			evol[i] = new int[evol[i-1].length*2];
			goals[i] = new int[evol[i-1].length*2];
			
			for(int j = 0; j<evol[i].length; j++){
				evol[i][j] = -1;
				goals[i][j] = 0;
			}
		}
				
		current = evol.length - 1;
		
		total = evol[current].length;
		
		for(int i = 0; i<total; i++)
			evol[current][i] = i;
			
		//System.out.println("Nivells : "+levels+", totals:"+total+" Actual:"+current);
		
		for(int i=0; i<total*total; i++)
		{
			int i1, i2, aux;
			i1 = Game.RND(total);
			i2 = Game.RND(total);
			
			aux = evol[current][i1];
			evol[current][i1] = evol[current][i2];
			evol[current][i2] = aux;
						
		}
					
	}
	
	public void simulate()
	{
	
		if(current>0)
		{
			
			for(int i = 0; i<evol[current].length; i+=2)
			{
				goals[current][i] = Game.RND(4);
				
				do{
					goals[current][i+1] = Game.RND(4);
					
				}while(goals[current][i] == goals[current][i+1]);
			}
		}
	}
	
	public void step()
	{
		if(current>0)
		{
							
			for(int i = 0; i<evol[current].length; i+=2)
			{
				if(goals[current][i] > goals[current][i+1])
				{
					evol[current-1][i/2] = evol[current][i]; 
				}else{
					evol[current-1][i/2] = evol[current][i+1]; 			
				}
			}
			
			current--;	
		}
				
	}
	
	public int myOpp(int myTeam)
	{
		int pos=0;
		
		for(int i=0; i<evol[current].length; i++)
			if(evol[current][i] == myTeam) pos = i;
			
		if(pos%2 == 0) return evol[current][pos+1];
		else return evol[current][pos-1];
			
	}
	
	public void setGoals(int team, int goal)
	{
		int pos=0;
		
		for(int i=0; i<evol[current].length; i++)
			if(evol[current][i] == team) pos = i;
			
		if(pos< goals[current].length) goals[current][pos] = goal;
			
	}
	
	public void write(DataOutputStream ostream)
	{
		try{
			ostream.writeInt(levels);
			ostream.writeInt(current);
			ostream.writeInt(total);
		
			for(int i = 0; i<evol.length; i++)
			for(int j = 0; j<evol[i].length; j++)
				ostream.writeInt(evol[i][j]);
			for(int i = 0; i<evol.length; i++)
			for(int j = 0; j<evol[i].length; j++)
				ostream.writeInt(goals[i][j]);						
				
		}catch(Exception e){System.out.println("Error en la escriptura");}
	}
	
	public void read(DataInputStream istream)
	{
		try{
			levels = istream.readInt();
			current = istream.readInt();
			total = istream.readInt();
			
			for(int i = 0; i<evol.length; i++)
			for(int j = 0; j<evol[i].length; j++)
				evol[i][j] = istream.readInt();
			for(int i = 0; i<evol.length; i++)
			for(int j = 0; j<evol[i].length; j++)
				goals[i][j] = istream.readInt();					
				
		}catch(Exception e){System.out.println("Error en la lectura");}
	}

}