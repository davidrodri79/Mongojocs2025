package com.mygdx.mongojocs.rescue;

public class SubMisObj
{
	long	timeStart;
	
	int		timeToComplete;
	int		personsToRescue;
	boolean		bPutOutFire;

	SubMisObj (int ttc, boolean pof)
	{
		timeToComplete = ttc;
		bPutOutFire = pof;

		timeStart = System.currentTimeMillis ();
	}	
	
	SubMisObj (int ttc, int ptr, boolean pof)
	{
		timeToComplete = ttc;
		personsToRescue = ptr;
		bPutOutFire = pof;

		timeStart = System.currentTimeMillis ();
	}
}