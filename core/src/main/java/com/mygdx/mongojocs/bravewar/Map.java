package com.mygdx.mongojocs.bravewar;
/**
 * 
 */

import java.util.*;

public class Map {

  ///////////////////////////////////////
  // attributes

	public int		hexaWidth = 1;
	public int		hexaHeight = 1;
	public byte		hexaMap [][]= { {0}};
	/* = { 
							 		 {0,0,0,0,3,0,0,0,1,1,1},
									 {0,0,0,0,1,1,1,0,1,1,1},
									 {0,0,0,1,1,1,1,0,1,1,1},
									 {0,0,2,1,3,3,1,0,1,1,1},
									 {0,0,1,3,4,5,4,3,1,1,1},
									 {0,0,1,3,0,0,3,1,1,1,1},
									 {0,0,1,2,3,3,1,1,1,1,1},
									 {0,0,1,3,0,0,3,1,1,1,1},
									 {0,0,1,3,4,5,4,3,1,1,1}};
	*/

    public int numUnitsOfFood				= 0; 
    public int numUnitsOfMoney				= 0;
    public int numUnitsOfReservedMoney		= 0; 

   ///////////////////////////////////////
   // associations

    public GameCanvas gameCanvas; 
    public Region regions[]; 	// of type com.mygdx.mongojocs.bravewar.Region
    //public Hashtable players = new Hashtable(); 	// of type Player


  ///////////////////////////////////////
  // operations

	/// Simple X*Y QuadMap
	public Map (int regCols, int regRows)
	{
		regions = new Region[regCols*regRows];
		
		for (int i = 0; i < regCols * regRows; i++)
		{
			Region newReg = new Region();//this);
			
			//	regions.put (new Integer (i), newReg);
			regions [i] = newReg;
		}
	}

	public	void init (int regCols, int regRows)
	{
		regions = new Region[regCols*regRows];
		
		for (int i = 0; i < regCols * regRows; i++)
		{
			Region newReg = new Region();//this);
			
		//	regions.put (new Integer (i), newReg);
			regions [i] = newReg;
		}
	}
	
	public byte checkHexaMap(int x, int y)
	{
		if(x>=0 && y>=0 && x<hexaWidth && y<hexaHeight){
			return hexaMap[y][x];	
		}
		else return 0;
		
	}


	// com.mygdx.mongojocs.bravewar.Map definition   <id,com.mygdx.mongojocs.bravewar.Region>
	
	// Player definition <idPlayer, Player>
	
	/*void	addPlayer (int idPlayer, String name)
	{
		Player 	p = new Player (idPlayer, name);
		players.put (new Integer (idPlayer), p);
	}
	
	void	deletePlayer (int idPlayer)
	{
		players.remove (new Integer (idPlayer));
	}*/
	
	/*String	getPlayerName (int idPlayer)
	{
		return ((Player)players.get (new Integer (idPlayer))).strPlayerName;
	}*/
	
	public Region getRegionById (int idRegion)
	{
		//return (com.mygdx.mongojocs.bravewar.Region) regions.get (new Integer(idRegion));
		return regions [idRegion];
	}
	
	public Region getRegionByPos (int xp, int yp)
	{
		return 	getRegionById (xp+yp*hexaWidth);
	}
	
	public int		getRegionIdByPos (int xp, int yp)
	{
		return xp+yp*hexaWidth;
	}

	public void buyFood (int unitsFood)
	{
		numUnitsOfFood += unitsFood;
	}
	
/**
 * 
 * 
 */
    public void moveTroops(int idRegSrc, int idRegDst, int unitsCat, int unitsArc, int unitsCav, int unitsInf)
    {   
    	try {
    	Region rSrc = regions [idRegSrc];
    	Region rDst = regions [idRegDst];
    	
    	rSrc.numUnitsCatapults	-= unitsCat;
    	rSrc.numUnitsArchers	-= unitsArc;
    	rSrc.numUnitsCavalry	-= unitsCav;
    	rSrc.numUnitsInfantry	-= unitsInf;

    	rDst.numUnitsCatapults	+= unitsCat;
    	rDst.numUnitsArchers	+= unitsArc;
    	rDst.numUnitsCavalry	+= unitsCav;
    	rDst.numUnitsInfantry	+= unitsInf;
    	} catch (Exception e) { e.printStackTrace (); }
        
    } // end moveTroops        

/**
 * 
 */
    public void buyTroops(int idRegDst, int unitsCat, int unitsArc, int unitsCav, int unitsInf) {
    	try {
    	Region rDst = regions [idRegDst];
    	rDst.numUnitsCatapults	+= unitsCat;
    	rDst.numUnitsArchers	+= unitsArc;
    	rDst.numUnitsCavalry	+= unitsCav;
    	rDst.numUnitsInfantry	+= unitsInf;
    	} catch (Exception e) { e.printStackTrace (); }
    } // end buyTroops

/**
 * 
 * 
 */
    public void updateRegion(int idReg, int idPlayer, int unitsCat, int unitsArc, int unitsCav, int unitsInf) {        
    	try {
    	Region reg = regions [idReg];
    	
    	if (reg==null)		return;
    	
    	reg.idPlayer			= idPlayer;
    	reg.numUnitsCatapults	= unitsCat;
    	reg.numUnitsArchers		= unitsArc;
    	reg.numUnitsCavalry		= unitsCav;
    	reg.numUnitsInfantry	= unitsInf;
    	} catch (Exception e) { e.printStackTrace (); }
    } // end updateRegion        
    
	public int gainPerRegions(int plId)
	{
		Region r;
		int am=0, reward[]={10,10,30,100,200,500};
		
		for(int i=0; i<hexaWidth; i++)
			for(int j=0; j<hexaHeight; j++){
				r=getRegionById(getRegionIdByPos(i,j));
				if(r.idPlayer==plId)
					am+=reward[hexaMap[j][i]-1];
			}
		return am;
	}
    

} // end com.mygdx.mongojocs.bravewar.Map



