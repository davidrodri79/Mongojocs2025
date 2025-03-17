package com.mygdx.mongojocs.camaraoscura;

// *****************************************
// -----------------------------------------
// Anim Tile - Engine - Rev.2 (18.06.2003)
// =========================================
// *****************************************

// Mode = xxx0 = Anim Auto-Destruct
// Mode = xxx1 = Anim Loop
// Mode = xxx2 = Anim Stop-Fin
// Mode = xxx3 = Anim Stop-Inicio

// Mode = xx1x = Anim-Play hacia ATRAS
// Mode = xx2x = Anim en PAUSA



public class AnimTile
{
int Sour, Dest, SizeX, SizeY;
int Frames, FrameAct;
int Speed, SpeedCnt;
int Mode, Side, Pause;
int Repetir;

// ----------------------
// Anim Tile Source Bufer
// ======================

static int AnimSizeX=15;
static int AnimSizeY=6;
static byte[] AnimMap = new byte[AnimSizeX*AnimSizeY];


// --------------
// Anim Tile Play
// ==============

public boolean Play()
{
	if (Frames == 0) {return(true);}
	if (SpeedCnt > 0) {SpeedCnt--; return(false);}
	if (Pause >= 0) {if (Pause==0)	{return false;} else {Pause--;}}

	SpeedCnt=Speed; FrameAct+=Side;

	if (FrameAct < 0 || FrameAct >= Frames)
	{
		switch (Mode & 0x07)
		{
		case 0: return(true);													// Auto-Destruct
		case 1: if (FrameAct < 0) {FrameAct=Frames-1;} else {FrameAct=0;} break;// Loop
		case 2: FrameAct-=Side;Pause=0;break;									// Stop-Fin
		case 3: FrameAct=0;Pause=0;break;										// Stop-Ini
		case 4: if (--Repetir < 1) {return true;} else {if (FrameAct < 0) {FrameAct=Frames-1;} else {FrameAct=0;} break;}// Loop X
		case 5: FrameAct-=Side*2;Side*=-1;break;									// Loop-Ping Pong
		case 7: if (--Repetir < 1) {FrameAct-=Side;Pause=0;break;} else {if (FrameAct < 0) {FrameAct=Frames-1;} else {FrameAct=0;} break;}// Loop X
		}
	}

// =--
	int Source=Sour+(SizeX*FrameAct);
	int Destin=Dest;

	for (int t=0 ; t<SizeY ; t++ )
	{
		for (int i=0 ; i<SizeX ; i++ )
		{
			Game.FaseMap[Destin+i] = AnimMap[Source+i];
		}
	Source+=AnimSizeX;
	Destin+=Game.FaseSizeX;
	}
// =--

	return (false);
}


// --------------
// Anim Tile SET
// ==============

public void AniTilSET(int Dest, int CoorX, int CoorY, int SizeX, int SizeY, int Frames, int Speed, int Mode)
{
	this.Dest=Dest;
	this.Sour=(CoorY*AnimSizeX)+CoorX;
	this.SizeX=SizeX; this.SizeY=SizeY;
	this.Frames=Frames; this.FrameAct=0;
	this.Speed=Speed; this.SpeedCnt=0;
	this.Mode=Mode;
	this.Pause=((Mode&0x20)==0)?-1:0;

	if ((Mode&0x10)==0)
	{
	this.Side=1;
	} else {
	this.Side=-1;
	this.FrameAct=Frames-1;
	}
}

};

// <=- <=- <=- <=- <=-
