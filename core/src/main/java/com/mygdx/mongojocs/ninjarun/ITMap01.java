package com.mygdx.mongojocs.ninjarun;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class ITMap01
{

byte[] MapInfo;
int MW,MH,TW,TH,TMW,TMH,StartX,StartY;
byte[] ColisingTiles;
Image Img;
Scroll sc;
ITGame01 ga;

public ITMap01(ITGame01 ITGame, int TiledMapWidth, int TiledMapHeight, String FileName, byte[] LevelInfo)
{
	ga = ITGame;
	
	MW = TiledMapWidth*12;	MH = TiledMapHeight*12;
	TW = 12;		TH = 12;
	TMW = TiledMapWidth;	TMH = TiledMapHeight;
	
	StartX = 80/*MW/2*/;
	StartY = 25;
		
	MapInfo = new byte[TMW*TMH];
	LoadInfo(FileName,LevelInfo,112);
}

public ITMap01(ITGame01 ITGame,int MapWidth, int MapHeight, int TileWidth, int TileHeight, String FileName, byte[] LevelInfo)
{
	ga = ITGame;
	
	MW = MapWidth;		MH = MapHeight;
	TW = TileWidth;		TH = TileHeight;
	TMW = MW/TW;		TMH = MH/TH;
	
	StartX = 80/*MW/2*/;
	StartY = 25;
	
	MapInfo = new byte[TMW*TMH];
	LoadInfo(FileName,LevelInfo,112);
}

public void LoadInfo(String FileName, byte[] LevelInfo, int MapTileSize)
{
	byte[] MapTilesInfo;
	MapTilesInfo = ga.ITU.LoadFile(FileName);	
	//MapInfo = new byte[NumMapTiles*MapTileSize];
	
	for (int Counter=0; Counter < LevelInfo.length; Counter++)
	{
		for (int Counter2=0; Counter2<MapTileSize; Counter2++)
		{
			MapInfo[(MapTileSize*Counter)+Counter2] = MapTilesInfo[((LevelInfo[Counter]-1)*MapTileSize)+Counter2];
		}	
	}
	MapTilesInfo = null;
}

public int PosXOf(int PosX, int PosY)
{
	return (PosX/TW);
}

public int PosYOf(int PosX, int PosY)
{
	return (PosY/TH);
}

public int PosOf(int PosX, int PosY)
{
	return ((PosX/TW) + ((PosY/TH)*TMW));
}

public int TileOf(int PosX, int PosY)
{
	return (MapInfo[PosOf(PosX,PosY)]);
}

public byte[] TilesOf(int PosX, int PosY, int Width, int Height)
{
	int FX,LX,FY,LY,ListIndex,ActualX,ActualY;
	
	FX = PosXOf(PosX	,PosY		);
	FY = PosYOf(PosX	,PosY		);
	LX = PosXOf(PosX+Width	,PosY+Height	);
	LY = PosYOf(PosX+Width	,PosY+Height	);
	
	byte[] ListOfTiles = new byte[(((LX-FX)+1)*((LY-FY)+1))];

	ListIndex = 0;
	
	for (ActualY=FY;ActualY<=LY;ActualY++)
	{
		for (ActualX=FX;ActualX<=LX;ActualX++) 
		{
		 	ListOfTiles[ListIndex++]=MapInfo[ActualX+(ActualY*TMW)];
		}
	}
	return (ListOfTiles);	
}

public byte[] ColisionOf(int PosX, int PosY, int Width, int Height)
{
	int FX,LX,FY,LY,ListIndex,ActualX,ActualY;
	
	FX = PosXOf(PosX	,PosY		);
	FY = PosYOf(PosX	,PosY		);
	LX = PosXOf(PosX+Width	,PosY+Height	);
	LY = PosYOf(PosX+Width	,PosY+Height	);
	
	byte[] ListOfColisions = new byte[(((LX-FX)+1)*((LY-FY)+1))];

	ListIndex = 0;
	
	for (ActualY=FY;ActualY<=LY;ActualY++)
	{
		for (ActualX=FX;ActualX<=LX;ActualX++) 
		{
		 	ListOfColisions[ListIndex++]= ColisingTiles[ActualX+(ActualY*TMW)];
		}
	}
	return (ListOfColisions);	
}

public boolean SomeTileOfAre(int PosX, int PosY, int Width, int Height, byte[] Values)
{
	byte[] ListOfTiles;
	
	ListOfTiles = TilesOf(PosX,PosY,Width,Height);
	
	for (int Counter=0; Counter<(ListOfTiles.length); Counter++)
	{
		for (int Counter2=0; Counter<(Values.length); Counter2++) if (ListOfTiles[Counter]==Values[Counter2]) return true;	
	}
	
	return false;	
}

public boolean AllTilesOfAre(int PosX, int PosY, int Width, int Height, byte[] Values)
{
	byte[] ListOfTiles;
	boolean TempResult;
	
	ListOfTiles = TilesOf(PosX,PosY,Width,Height);
	
	for (int Counter=0; Counter<(ListOfTiles.length); Counter++)
	{
		TempResult = false;
		for (int Counter2=0; Counter2<Values.length; Counter++) TempResult = (TempResult||(ListOfTiles[Counter]==Values[Counter2]));
		if (TempResult==false) return false;
	}
	return true;	
}

public byte TileColisionOf(int PosX, int PosY, int Width, int Height)
{
	byte Temp=0;
	if (ga.LookColision)
	{
		int FX,LX,FY,LY,ListIndex,ActualX,ActualY;
		
		if ((PosX>0)&&(PosY>0)&&((PosX+Width)<MW)&&((PosY+Height)<MH))
		{
			FX = PosXOf(PosX	,PosY		);
			FY = PosYOf(PosX	,PosY		);
			LX = PosXOf(PosX+Width	,PosY+Height	);
			LY = PosYOf(PosX+Width	,PosY+Height	);
		
			for (ActualY=FY;ActualY<=LY;ActualY++)
			{
				for (ActualX=FX;ActualX<=LX;ActualX++) 
				{
				 	if (ColisingTiles[ActualX+(ActualY*TMW)]==1) return 1; else
				 	if (ColisingTiles[ActualX+(ActualY*TMW)]==2) Temp = 2;
				}
			}
		}
	}
	return Temp;
}

public void InitGraph()
{
	sc = new Scroll();
	sc.ScrollINI(((MW<ga.gc.CanvasSizeX) ? MW : ga.gc.CanvasSizeX),((MH<ga.gc.CanvasSizeY) ? MH : ga.gc.CanvasSizeY));
	sc.ScrollSET(MapInfo,TMW,TMH,Img,16);	
}

public void InitColision(short[] ColisionArray)
{
	ColisingTiles = new byte[TMW*TMH];
	for (int Counter=0; Counter<ColisingTiles.length; Counter++) ColisingTiles[Counter] = 1;
	for (int Counter=0; Counter<ColisingTiles.length; Counter++)
	{
		for (int Counter2=0; Counter2<ColisionArray.length; Counter2++)
		{
			if (ColisionArray[Counter2]==MapInfo[Counter]) 
			{
				ColisingTiles[Counter] = 0;
				Counter2 = ColisionArray.length;
			}
		}
	}
}

public void InitColision2(short[] ColisionArray)
{
	for (int Counter=0; Counter<ColisingTiles.length; Counter++)
	{
		for (int Counter2=0; Counter2<ColisionArray.length; Counter2++)
		{
			if (ColisionArray[Counter2]==MapInfo[Counter]) 
			{
				ColisingTiles[Counter] = 2;
				Counter2 = ColisionArray.length;
			}
		}
	}
}


public void Refresh()
{
	if (ga.LevelFinalized==false) sc.ScrollRUN_Centro_Max(ga.Player.X,MH-ga.Player.Y-20);
}

public void Paint(Graphics GraSource)
{
	Refresh();
	sc.ScrollIMP(GraSource);
}

public void Destroy()
{
	sc.ScrollEND();
	ColisingTiles = null;
	Img = null;
	System.gc();
}

}
