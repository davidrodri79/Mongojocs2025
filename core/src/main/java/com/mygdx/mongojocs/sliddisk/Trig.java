package com.mygdx.mongojocs.sliddisk;/*

	Clase Trig.java - Por David Rodr�guez Pellicer

	Clase que implementa las funciones trigonom�tricas m�s usuales basadas en una 
	aritm�tica binaria orientada al maximo rendimiento para las arquitecturas de
	Java Wireless.
	
	Notas:
	
	-Los angulos (0 a 360 grados) se mapean como 256 valores (0 a 255) 
	 
	-Los n�meros en coma flotante son en realidad valores enteros. De modo que valores
	que oscilan entre 0.0 y 1.0 aqui ser�n enteros entre 0 y 1024. La multiplicacion
	por 1024 o la division se resuelven como <<10 o >>10
*/


public class Trig
{
	
	private static int sintable[]={
	0,25,50,75,100,125,150,175,199,224,248,273,297,321,344,368,391,414,437,460,482,504,
	526,547,568,589,609,629,649,668,687,706,724,741,758,775,791,807,822,837,851,865,
	878,890,903,914,925,936,946,955,964,972,979,986,993,999,1004,1008,1012,1016,1019,1021,
	1022,1023};
	
	private static int costable[]={
	1024,1023,1022,1021,1019,1016,1012,1008,1004,999,993,986,979,972,964,955,946,936,
	925,914,903,890,878,865,851,837,822,807,791,775,758,741,724,706,687,668,649,629,
	609,589,568,547,526,504,482,460,437,414,391,368,344,321,297,273,248,224,199,175,
	150,125,100,75,50,25};
	
	private static int tantable[]={
	0,25,50,75,100,126,151,177,203,229,256,283,310,338,366,395,424,453,484,515,547,580,
	613,648,684,721,759,799,840,883,928,974,1024,1075,1129,1187,1247,1312,1380,1453,
	1532,1617,1708,1807,1915,2034,2165,2310,2472,2654,2861,3099,3375,3700,4088,4560,
	5148,5901,6903,8302,10397,13882,20845,41719
	};
			
	static int sin(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=sintable[a%64];
		else 	v=sintable[63-(a%64)];	
		if(a>=128) v=-v;
		return v;
	}

	static int cos(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=costable[a%64];
		else 	v=costable[63-(a%64)];	
		if((a>>6==1) || (a>>6==2)) v=-v;
		return v;
	}
	
	static int tan(int a)
	{
		int v;
		while(a<0) a+=256;
		a=a%256;
		if((a>>6)%2==0)
			v=tantable[a%64];
		else 	v=tantable[63-(a%64)];	
				
		if((a>>6)%2==1) v=-v;		
		return v;
	}
	static int asin(int v)
	{
		int a=0, vabs;
		if(v<0) vabs=-v;
		else vabs=v;
		
		while((sintable[a]<v) && (a<63)) a++;	
		
		if(v>=0) return a;
		else return (256-a)%256;
		
	}
	static int acos(int v)
	{
		int a=0, vabs;
		if(v<0) vabs=-v;
		else vabs=v;
		
		while((costable[a]>v) && (a<63)) a++;	
		
		if(v>=0) return a;
		else return 128-a;
		
	}
	static int atan(int s, int c)
	{
		int v,a=0;
		if(c!=0) v=(s<<10)/c;
		else v=99999;
		
		if(v<0) v=-v;
				
		while((tantable[a]<v) && (a<63)) a++;
		
		if(s>=0 && c>=0)
			return a;
		else if(s>=0 && c<0)
			return 128-a;
		else if(s<0 && c<0)
			return 128+a;		
		return (256-a)%256;		
	}	
}