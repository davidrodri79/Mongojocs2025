package com.mygdx.mongojocs.cerberus;


public class CerberusItem {
	
	public static final int ENERGY = 0;
	public static final int HEART = 1;
	public static final int HIKARU = 2;
	public static final int LIFE = 3;
	public static final int IT_SWORD = 4;
	public static final int IT_WHIP = 5;
	public static final int IT_BOOMER = 6;
	public static final int HELMET = 7;
	public static final int ARMOR = 8;
	public static final int GAUNTLETS = 9;
	public static final int BOOTS = 10;
	public static final int CAPE = 11;
	public static final int PHONE = 12;

	public int x, y, type, cnt, fading, vely;
	boolean out_of_world, temp;

	public CerberusItem (int xx, int yy, int t, boolean temporal)
	{
		cnt=0; x=xx; y=yy; type=t; out_of_world=false;
		temp=temporal; vely=0;
	}
	
	public void update(CerberusWorld w)
	{
		cnt++;		
			
		if(temp)
		if(x<w.pl.x-300 || x>=w.pl.x+300 || y<w.pl.y-200 || y>w.pl.y+200) out_of_world=true;
		
		if(y<w.y_at_tile(x,y+1)) {if(cnt%2==0)vely++; y+=vely;}
		
		if(fading>0 && cnt%3==0) fading++; if (fading>4) out_of_world=true;
	}
}