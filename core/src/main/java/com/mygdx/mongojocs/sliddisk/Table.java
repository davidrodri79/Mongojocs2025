package com.mygdx.mongojocs.sliddisk;

class Square {
	
	int x1, x2, y1, y2;
	
	public Square(int x, int y, int w, int h)
	{
		x1 = x; x2 = x+w; y1 = y; y2 = y+h;
		
		
	}
	
	public boolean inside(int x, int y)
	{
		return (x>=x1 && x<=x2 && y>=y1 && y<=y2);		
	}
	
}

public class Table {
	
	public static final int GOALWIDTH = 32;

	public int width, height, left, right, upGoalTimer, downGoalTimer;	
	public Square upGoal, downGoal;

	public Table(int w, int h)
	{	
		width = w; height = h;
		left = 16; right = width - 16;
		
		upGoal = new Square(width/2 - GOALWIDTH/2,0,GOALWIDTH,8);
		downGoal = new Square(width/2 - GOALWIDTH/2,height-8,GOALWIDTH,8);
		
		upGoalTimer = 0;
		downGoalTimer = 0;
	}

	
}