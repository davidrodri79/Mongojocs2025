package com.mygdx.mongojocs.ultranaus;

public class Control
{
	public boolean up, down, left, right, but1, but2, but3, but4, any, anybut;	
	
	public Control()
	{
		reset();		
	}
	public void reset()
	{
		up=false; down=false; left=false; right=false; 
		but1=false; but2=false; but3=false; but4=false;		
		any=false; anybut=false;
	}

}