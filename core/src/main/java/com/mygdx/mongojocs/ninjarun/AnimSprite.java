package com.mygdx.mongojocs.ninjarun;

// *****************************************
// -----------------------------------------
// Anim Sprite - Engine - Rev.3 (23.05.2003)
// =========================================
// *****************************************

// Mode = xxx0 = Anim Auto-Destruct
// Mode = xxx1 = Anim Loop
// Mode = xxx2 = Anim Stop-Fin
// Mode = xxx3 = Anim Stop-Inicio

// Mode = xx1x = Anim-Play hacia ATRAS
// Mode = xx2x = Anim en PAUSA

// Mode = .x.. = Bank - Source (PNGs)
// Mode = x... = Prioridad entre Sprites (0: Inferior, F:Superior)




public class AnimSprite
{
int CoorX, CoorY;
int FrameIni, Frames, FrameAct;
int Speed, SpeedCnt;
int Mode, Side, Pause, Bank, Prio;
int Repetir;
int FrameBase=0;

// ----------------
// Anim Sprite Play
// ================

public boolean Play()
{
	if (Frames == 0) {return(true);}
	if (SpeedCnt > 0) {SpeedCnt--; return(false);}
	if (Pause >= 0) {if (Pause==0)	{return false;} else {Pause--;}}

	SpeedCnt=Speed; FrameAct+=Side;

	if (FrameAct >= 0 && FrameAct < Frames) {return(false);}

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
	return (false);
}


// ----------------
// Anim Sprite SET
// ================

public void SpriteSET(int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	this.CoorX=CoorX;
	this.CoorY=CoorY;
	this.FrameIni=FrameIni;
	this.Frames=Frames; this.FrameAct=0;
	this.Speed=Speed; this.SpeedCnt=Speed;
	this.Mode=Mode;
	this.Pause=((Mode&0x20)==0)?-1:0;
	this.Bank=(Mode>>8) & 0xF;
	this.Prio=(Mode>>12) & 0x3F;

	if ((Mode&0x10)==0)
	{
	this.Side=1;
	} else {
	this.Side=-1;
	this.FrameAct=Frames-1;
	}
}


public void SpriteSET(int FrameIni, int Frames, int Speed, int Mode)
{
	SpriteSET(CoorX, CoorY, FrameIni, Frames, Speed, Mode);
}


};

// <=- <=- <=- <=- <=-
