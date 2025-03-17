package com.mygdx.mongojocs.mr;
//////////////////////////////////////////////////////////////////////////
// Movistar MotoGP - Color Version 1.0
// By Carlos Peris
// MICROJOCS MOBILE , 2003
//////////////////////////////////////////////////////////////////////////


import java.util.Random;

public class Tramo
{
	Punto pos = new Punto();
	
	int altura;
	int curva;
	int extra;
	final static int MAXP = 7;
	Punto pextra[] = new Punto[MAXP];
	int jextra[];
	int rand;
	int clado;
	
	
	public Tramo()
	{		
		for (int i = 0; i < pextra.length;i++)
			pextra[i] = new Punto();		
		jextra = new int[MAXP*2];	
		Random rnd = new Random();
		rand = rnd.nextInt();		
	}
	
	/*public void setStuff(int _x, int _y, int _z)
	{
				pextra[0].x = _x;
				pextra[0].y = _y;
				pextra[0].z = _z;			
	}*/
	
	public void SetPos(int x, int y, int z)
	{
		pos.x = x;
		pos.y = y;
		pos.z = z;
	}
	
	/*public void SetColor(int _r, int _g, int _b)
	{
		r = _r;
		g = _g;
		b = _b;
	}
	
	public void SetOuterColor(int _r, int _g, int _b)
	{
		or = _r;
		og = _g;
		ob = _b;
	}
	
	public void SetBorderColor(int _r, int _g, int _b)
	{
		lr = _r;
		lg = _g;
		lb = _b;
	}*/
	
	/*
	public void Copy(Tramo src)
	{
		pos.x = src.pos.x;
		pos.y = src.pos.y;
		pos.z = src.pos.z;
		r = src.r;
		g = src.g;
		b = src.b;
		or = src.or;
		og = src.og;
		ob = src.ob;
	}*/
}