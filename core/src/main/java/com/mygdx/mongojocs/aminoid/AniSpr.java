

// *****************************************
// -----------------------------------------
// Anim Sprite - Engine - Rev.1 (15.04.2003)
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

/*
final int AnimSpriteMAX=128;
int AnimSpriteC;
int[] AnimSpriteP;
AnimSprite[] AnimSprites;
*/


package com.mygdx.mongojocs.aminoid;


public class AniSpr
{
int CoorX, CoorY;
int FrameIni, Frames, FrameAct;
int Speed, SpeedCnt;
int Mode, Side, Pause, Bank, Prio;
int Repetir;


// ----------------
// Anim Sprite SET
// ================

public void AniSprSET(int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	this.CoorX=CoorX;
	this.CoorY=CoorY;
	this.FrameIni=FrameIni;
	this.Frames=Frames; this.FrameAct=0;
	this.Speed=Speed; this.SpeedCnt=Speed;
	this.Mode=Mode;
	this.Pause=((Mode&0x20)==0)?0:-1;
	this.Bank=(Mode>>8) & 0xF;
//	this.Prio=(Mode>>12) & 0x3F;

	if ((Mode&0x10)==0)
	{
	this.Side=1;
	} else {
	this.Side=-1;
	this.FrameAct=Frames-1;
	}
}


public void AniSprSET(int FrameIni, int Frames, int Speed, int Mode)
{
	AniSprSET(CoorX, CoorY, FrameIni, Frames, Speed, Mode);
}



// ----------------
// Anim Sprite Play
// ================

public boolean Play()
{
	if (Pause != 0) {if (Pause==-1)	{return(false);} else {Pause=-1;}}
	if (Frames == 0) {return(true);}
	if (SpeedCnt > 0) {SpeedCnt--; return(false);}

	SpeedCnt=Speed; FrameAct+=Side;

	if (FrameAct >= 0 && FrameAct < Frames) {return(false);}

	switch (Mode & 0x07)
	{
	case 0: return(true);													// Auto-Destruct
	case 1: if (FrameAct < 0) {FrameAct=Frames-1;} else {FrameAct=0;} break;// Loop
	case 2: FrameAct-=Side;Pause=-1;break;									// Stop-Fin
	case 3: FrameAct=0;Pause=-1;break;										// Stop-Ini
	case 4: if (--Repetir < 1) {return true;} else {if (FrameAct < 0) {FrameAct=Frames-1;} else {FrameAct=0;} break;}// Loop X
	case 5: FrameAct-=Side*2;Side*=-1;break;									// Loop-Ping Pong
	case 7: if (--Repetir < 1) {FrameAct-=Side;Pause=-1;break;} else {if (FrameAct < 0) {FrameAct=Frames-1;} else {FrameAct=0;} break;}// Loop X
	}
	return (false);
}

};




/*

// ---------------
// Anim Sprite SET
// ===============

public int AnimSpriteSET(int i, int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	if (i<0)
	{
	if (AnimSpriteC == AnimSpriteMAX) {return(-1);}
	i=AnimSpriteP[AnimSpriteC++];
	AnimSprites[i] = new AnimSprite();
	}

	AnimSprites[i].CoorX=CoorX;
	AnimSprites[i].CoorY=CoorY;
	AnimSprites[i].FrameIni=FrameIni;
	AnimSprites[i].Frames=Frames; AnimSprites[i].FrameAct=0;
	AnimSprites[i].Speed=Speed; AnimSprites[i].SpeedCnt=Speed;
	AnimSprites[i].Mode=Mode;
	AnimSprites[i].Pause=((Mode&0x20)==0)?0:-1;
	AnimSprites[i].Bank=(Mode>>8) & 0xF;
//	AnimSprites[i].Prio=(Mode>>12) & 0x3F;

	if ((Mode&0x10)==0)
	{
	AnimSprites[i].Side=1;
	} else {
	AnimSprites[i].Side=-1;
	AnimSprites[i].FrameAct=Frames-1;
	}

	return (i);
}

// ---------------
// Anim Sprite INI
// ===============

public void AnimSpriteINI()
{
	AnimSpriteC = 0;
	AnimSpriteP = new int[AnimSpriteMAX];
	AnimSprites = new AnimSprite[AnimSpriteMAX];
	for (int i=0 ; i<AnimSpriteMAX ; i++) {AnimSpriteP[i]=i;}
}

// ---------------
// Anim Sprite RES
// ===============

public void AnimSpriteRES(int i)
{
	AnimSprites[i].Frames=0; AnimSprites[i].Pause=0;
}

// ---------------
// Anim Sprite END
// ===============

public void AnimSpriteEND()
{
	AnimSprites = null; AnimSpriteP = null; AnimSpriteC = 0;
}

// ---------------
// Anim Sprite RUN
// ===============

public void AnimSpriteRUN()
{
	for (int i=0 ; i<AnimSpriteC ; i++)
	{
	if ( AnimSprites[AnimSpriteP[i]].Play() )
		{
		int t=AnimSpriteP[i]; AnimSprites[t] = null;
		AnimSpriteP[i--] = AnimSpriteP[--AnimSpriteC]; AnimSpriteP[AnimSpriteC] = t;
		}
	}
}

// -------------------
// Anim Sprite Ordenar
// ===================

public void AnimSpriteOrdenar()
{
	int Conta=AnimSpriteC-1;

	for (int t=0 ; t<AnimSpriteC ; t++)
	{
		for (int i=0 ; i<Conta ; i++)
		{
			if (AnimSprites[AnimSpriteP[i]].Prio < AnimSprites[AnimSpriteP[i+1]].Prio)
			{
			int dato=AnimSpriteP[i+1];
			AnimSpriteP[i+1] = AnimSpriteP[i];
			AnimSpriteP[i] = dato;
			}
		}
	Conta--;
	}
}

*/

// <=- <=- <=- <=- <=-
