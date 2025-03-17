package com.mygdx.mongojocs.fbi;


// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================


import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class GameCanvas extends BiosCanvas
{
public GameCanvas(Game ga) {super(ga);}

// **************************************************************************//
// Inicio Clase GameCanvas
// **************************************************************************//

// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
// *******************************************
// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego: 
// Terminal: 

// ---------------------------------
//  Bits de Control de cada Terminal
// =================================
final boolean deviceSound = true;
final boolean deviceVibra = false;
// ----------------------------------

// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


// *******************
// -------------------
// canvas - Engine
// ===================
// *******************

Image[] BackGround,WantedB,WantedP,Freak,Bocadillo,Cardenas;
Image WantedT,ScoreB,ScoreT;

public void loadScores()
{
	
}

public void loadDead()
{
	
}

public void loadGame()
{
	
}

public void loadMenu()
{
}

public void canvasInit()
{
	ProgBarINI( 94, 6,  (canvasWidth/2)-47, (canvasHeight/2)+16 );

	ProgBarSET(0,10);
	
	Freak = FS_LoadImage(4,(byte)8);
	
	ProgBarADD();
		
	Bocadillo = FS_LoadImage(6,(byte)3);
	
	ProgBarADD();
	
	ScoreT = loadImage("/ScoreT.png");
	
	ProgBarADD();
		
	ScoreB = loadImage("/ScoreB.png");
	
	Cardenas = FS_LoadImage(2,(byte)2);
	
	ProgBarADD();
	
	BackGround = FS_LoadImage(5,(byte)2);
	
	ProgBarADD();
	
	WantedT = loadImage("/WantedT.png");
	
	ProgBarADD();	
	
	WantedP = FS_LoadImage(7,(byte)2);
	
	ProgBarADD();	
	
	WantedB = FS_LoadImage(8,(byte)3);	
	
	ProgBarADD();
	//setSoftLabel(Frame.SOFT_KEY_1, "Menu");
	//setSoftLabel(Frame.SOFT_KEY_2, "Menu");
}

// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{
	if (ga.playShow) { ga.playShow=false; playDraw(); }
}

// <=- <=- <=- <=- <=-




int[] PuertaX = new int[] {(canvasWidth/2-40),(canvasWidth/2),(canvasWidth/2+40)};
int[] PuertaY = new int[] {8+24+((canvasHeight>150)?27:0),8+24+((canvasHeight>150)?27:0),8+24+((canvasHeight>150)?27:0)};





// *******************
// -------------------
// play - Engine - Gfx
// ===================
// *******************

// -------------------
// play Create Gfx
// ===================

public void playCreate_Gfx()
{
	
}

// -------------------
// play Destroy Gfx
// ===================

public void playDestroy_Gfx()
{
}

// -------------------
// play Init Gfx
// ===================

public void playInit_Gfx()
{
}

// -------------------
// play Release Gfx
// ===================

public void playRelease_Gfx()
{
}

// -------------------
// play Draw Gfx
// ===================

public void playDraw()
{
	ActFrame++;
	if (ActFrame>=4) ActFrame=0;
	switch (ga.gameStatus)
	{
		case 0 :
		case 45 :
		case 50 :
		case 75 : 
			scr.setColor(0x000000);
			scr.setClip(0,0,canvasWidth,canvasHeight);
			scr.fillRect(0,0,canvasWidth,canvasHeight);
			Image im = new Image();
			im._createImage("/Loading.png");
			showImage(im,(canvasWidth-96)/2,35);
			scr.setClip(0,0,canvasWidth,canvasHeight);
			scr.setColor(0xFFFFFF);
			textDraw(ga.gameText[ga.TEXT_LOADING][0], 10,10, 90, 0xFFFFFF, 0x210);
		break;
		
		case 100 :
			PintaFondo(0);
			PintaPuertas();
			PintaBurbujas();
			PintaRayo();
			PintaCardenas(ga.State);
			PintaScores();
			PintaTextos();
			PintaComentarios();
		break;	
		
		case 250 :
			PintaFondo(1);
			PintaCardenas(ga.State);
			PintaTextos();
		break;	
		
		case 260 :
			PintaFondo(1);
			PintaFreak(ActFre, (canvasWidth/2)-40-(ga.timeToChange/2), 56+(10*(ActFre%2)), 2);
			PintaCardenas(ga.State);
			PintaTextos();
		break;	
		
		case 270 :
			PintaFondo(1);
			PintaFreak(ActFre, (canvasWidth/2)-(ga.timeToChange/2), 56+(10*(ActFre%2)), 2);
			PintaCardenas(ga.State);
			PintaComentarios();
		break;	
		
		case 280 :
			PintaFondo(1);
			for (int ActFreak=0; ActFreak<ga.MaxEnem; ActFreak++)
			{
				if ((ActFreak%2)==0) PintaFreak(ActFreak, ((((ActFreak==ActFre)?(canvasWidth/2):-16)*ga.timeToChange)+(((ActFreak+1)*(canvasWidth/(ga.MaxEnem+1)))*(100-ga.timeToChange)))/100, 56+(10*(ActFreak%2)), 2);
			}
			for (int ActFreak=0; ActFreak<ga.MaxEnem; ActFreak++)
			{
				if ((ActFreak%2)!=0) PintaFreak(ActFreak, ((((ActFreak==ActFre)?(canvasWidth/2):-16)*ga.timeToChange)+(((ActFreak+1)*(canvasWidth/(ga.MaxEnem+1)))*(100-ga.timeToChange)))/100, 56+(10*(ActFreak%2)), 2);
			}
			PintaCardenas(ga.State);
		break;	
		
		case 290 :
			PintaFondo(1);
			ActFre += ga.RND(ga.MaxEnem-1)+1;
			ActFre = ActFre%ga.MaxEnem;
			if (ActFrame==0) InitTexto(ga.gameText[ga.TEXT_LEVEL+ga.MaxEnem+2][0],((ActFre+1)*(canvasWidth/(ga.MaxEnem+1)))-10,56+(10*(ActFre%2))-16,0xDDDDDD,20);
			for (int ActFreak=0; ActFreak<ga.MaxEnem; ActFreak++)
			{
				if ((ActFreak%2)==0) PintaFreak(ActFreak, ((ActFreak+1)*(canvasWidth/(ga.MaxEnem+1))), 56+(10*(ActFreak%2)), 2);
			}
			for (int ActFreak=0; ActFreak<ga.MaxEnem; ActFreak++)
			{
				if ((ActFreak%2)!=0) PintaFreak(ActFreak, ((ActFreak+1)*(canvasWidth/(ga.MaxEnem+1))), 56+(10*(ActFreak%2)), 2);
			}
			PintaCardenas(ga.State);
			PintaTextos();
		break;	
		
		
		
		case 560 :
			ScoreScroll--;
			if ((ga.timeToChange>5)&&(ga.keyY!=0))
			{
				ScoreScroll-=5;
				ga.timeToChange-=5;
			}
		case 550 :
		case 570 :
			PintaFondo(-1);
			PintaFinalScores();
		break;
		
		/*default :
			canvasFill(0);
				
			scr.setColor(0xff0000);
			scr.fillRect(0,canvasHeight/2, 16,16);
		
			scr.setColor(0x0000ff);
			scr.fillRect(canvasWidth-16,canvasHeight/2, 16,16);
		
		
			textDraw(new String[] {"uno","dos","tres"}, 0, 0, 0xff0000, TEXT_HCENTER|TEXT_VCENTER);
		break;*/
	}
}

// <=- <=- <=- <=- <=-
int ActFrame = 0;
public void PintaFondo(int F)
{
	scr.setClip(0,0,canvasWidth,canvasHeight);
	if (F<0) 
	{
		scr.setColor(0x000000);
		scr.fillRect(0,0,canvasWidth,canvasHeight);
	} else
	{
		if (F==1) 
		{
			scr.setColor(0x000000);
			scr.fillRect(0,0,canvasWidth,canvasHeight);
			showImage(BackGround[F],(canvasWidth-96)/2,(canvasHeight-40)/2);	
		} else
		{
			scr.setColor(0xD8C7B6);
			scr.fillRect(0,0,canvasWidth,canvasHeight);
			showImage(BackGround[F],0,0);
		}
	}
}

public void PintaPuertas()
{
	PintaPuerta(ga.PuertaS[0],PuertaX[0],PuertaY[0]);
	PintaPuerta(ga.PuertaS[1],PuertaX[1],PuertaY[1]);
	PintaPuerta(ga.PuertaS[2],PuertaX[2],PuertaY[2]);
}

public void PintaPuerta(int S, int X, int Y)
{
	switch (S)
	{
		case 0 : 
			showImage(Freak[0],0,0,32,48,X-16,Y-24);
		break;
		
		default:
			if (S<1) showImage(Freak[0],32*((1+(ActFrame/2))%3),0,32,48,X-16,Y-24); 
			else if (S>=100) showImage(Freak[((ga.MaxEnem*2)+3+(S-100))/3],32*(((ga.MaxEnem*2)+3+(S-100))%3),0,32,48,X-16,Y-24); else PintaFreak(S-1,X,Y,2);	
		break;	
	}
}

public void PintaFreak(int F, int X, int Y, int S)
{
	showImage(Freak[(3+(F*2)+((ActFrame/S)%2))/3],32*((3+(F*2)+((ActFrame/S)%2))%3),0,32,48,X-16,Y-24);
}

public void PintaRayo()
{
	scr.setClip(0,0,canvasWidth,canvasHeight);
	if ((ga.State>=2)&&(ga.State<=4))
	{
		int P = ga.State-2;
		if (ga.PuertaT[P]>0)
		{
			int Desp=16*(P-1);
			
			Rayo(PuertaX[P],PuertaY[P],(CX+Desp),(CY),4,2);
			Rayo(PuertaX[P],PuertaY[P],(CX+Desp),(CY),4,2);
			Rayo(PuertaX[P],PuertaY[P],(CX+Desp),(CY),25,0);
		}	
	}
}

public void Rayo(int XA, int YA, int XB, int YB, int P, int C)
{
	int X,Y,X2,Y2;
	X = XA;
	Y = YA;
	scr.setColor(0xFFFF00+(0x33)*(ga.RND(4)+2));
	if (C == 1) scr.setColor(0xFF00FF+(0x3300)*(ga.RND(4)+2));
	if (C == 2) scr.setColor(0x00FFFF+(0x330000)*(ga.RND(4)+2));
	for (int ActS=0; ActS<=P; ActS++)
	{
		X2 = X;
		Y2 = Y;
		
		X = ga.RND(7)-4+((XA*(P-ActS))+(XB*(ActS)))/P;
		Y = ga.RND(7)-4+((YA*(P-ActS))+(YB*(ActS)))/P;
		
		scr.drawLine(X2,Y2,X,Y);
	}
}
			
int CX=canvasWidth/2;
int CY=(((canvasHeight-48-8)>(32+24+8))?(canvasHeight-48-8):(32+24+8));
public void PintaCardenas(int S)
{
	if (S<2) S=3;
	if (S>4) S++;
	showImage(Cardenas[((S-2+((S>4)?(ActFrame/2):0))/4)],32*((S-2+((S>4)?(ActFrame/2):0))%4),0,32,48,CX-16,CY);
	/*scr.setColor(0xAA0000);	
	scr.fillRect(canvasWidth/2-16,(((canvasHeight-48-8)>(32+24+8))?(canvasHeight-48-8):(32+24+8)),32,48);*/
}
			
public void PintaScores()
{
	scr.setClip(0,canvasHeight-16,canvasWidth,16);
	scr.setColor(0x000000);
	scr.fillRect(1,canvasHeight-15,45,14);	
	scr.fillRect(canvasWidth-46,canvasHeight-15,45,14);
	
	PintaNumero(1,canvasHeight-14,ga.Score);
	PintaNumero(canvasWidth-46,canvasHeight-14,ga.MaxScore);
	
	showImage(ScoreB,47*ActFrame,0,47,16,0,canvasHeight-16);	
	showImage(ScoreB,47*ActFrame,0,47,16,canvasWidth-47,canvasHeight-16);
}

int MaxTextos=10;
String[] Textos = new String[MaxTextos];
int[] TextosX = new int[MaxTextos];
int[] TextosY = new int[MaxTextos];
int[] TextosC = new int[MaxTextos];
int[] TextosT = new int[MaxTextos];

public void InitTexto(String Text, int X, int Y, int C, int T)
{
	for (int Counter=0; Counter<MaxTextos; Counter++)
	{
		//System.out.println(((Textos[Counter]==null)?"null":Textos[Counter]));
		if (Textos[Counter]==null)
		{
			Textos[Counter] = Text;
			TextosX[Counter] = X;
			TextosY[Counter] = Y;
			TextosC[Counter] = C;
			TextosT[Counter] = T;
			Counter = MaxTextos;	
		}	
	}	
}

public void PintaTextos()
{
	scr.setClip(0,0,canvasWidth,canvasHeight);
	for (int Counter=0; Counter<MaxTextos; Counter++) 
	{
		if (Textos[Counter]!=null)
		{
			if ((ga.State!=5)&&(ga.gameStatus!=250)&&(ga.gameStatus!=260)) textDraw(Textos[Counter], TextosX[Counter], TextosY[Counter], TextosC[Counter], 0x110);
			TextosY[Counter]-=3;
			if (TextosT[Counter]>0) TextosT[Counter]--; else Textos[Counter]=null;
		} else
		{
			TextosT[Counter] = 0;
		}
	}
}

public void PintaBocadillo(int Y, int X, int T)
{
	showImage(Bocadillo[0],(canvasWidth/2)-47,Y);
	/*scr.setColor(0x000000);
	scr.fillRect((canvasWidth/2)-47,Y,96,40);
	scr.setColor(0xFFFFFF);
	scr.fillRect((canvasWidth/2)-46,Y+1,94,38);*/
	if (T>0)
	{
		showImage(Bocadillo[1],X,Y-15);
	} else
	{
		showImage(Bocadillo[2],X,Y+39);
	}
}

int ActCom,ActFre;
public void PintaComentarios()
{
	if (ga.State==5)
	{
		PintaBocadillo(CY-56,canvasWidth/2,0);
		scr.setClip(0,0,canvasWidth,canvasHeight);
		textDraw(ga.gameText[ga.TEXT_LEVEL+1][ActCom], ((canvasWidth-84)/2), CY-56+5, 90, 0x000000, 0x010);
	}
	if (ga.State==7)
	{
		PintaBocadillo(2,((canvasWidth/2)-(ga.timeToChange/2))+16,0);
		scr.setClip(0,0,canvasWidth,canvasHeight);
		textDraw(ga.gameText[ga.TEXT_LEVEL+2+ActFre][ActCom], ((canvasWidth-84)/2), 7, 90, 0x000000, 0x010);
	}
}

public void PintaNumero(int X, int Y, int N)
{
	int C,M;
	for (int Counter=0; Counter<5; Counter++)
	{
		M=1;
		for (int EX=0; EX<(4-Counter); EX++) M*=10;
		N=N%(M*10);
		C = N/M;
		showImage(ScoreT,9*C,0,9,12,X+(Counter*9),Y);
	}
}

public void PintaNumeroW(int X, int Y, int N)
{
	int C,M;
	boolean PintaCero=false;
	showImage(WantedT,0,0,10,11,X,Y-5);
	for (int Counter=0; Counter<3; Counter++)
	{
		M=1;
		for (int EX=0; EX<(2-Counter); EX++) M*=10;
		N=N%(M*10);
		C = N/M;
		if ((C>0)||(Counter==2)) PintaCero = true;
		if (PintaCero) showImage(WantedT,10*(C+1),0,10,11,X+((1+Counter)*10),Y-5);
	}
}

public void PintaBurbujas()
{
	for (int Counter=0; Counter<ga.butterFlyV.length; Counter++)
	{
		if (ga.butterFlyV[Counter]) showImage(Freak[7],ga.butterFlyX[Counter]-(ga.butterFlyW/2)-3*(2-ga.sinoidal), ga.butterFlyY[Counter]-(ga.butterFlyH/2));
	}	
}

int ScoreScroll=0;
public void PintaFondoScore()
{
	showImage(WantedB[0],((canvasWidth-96)/2),ScoreScroll);
	for (int Counter=0; Counter<=(ga.MaxEnem); Counter++)
	{
		showImage(WantedB[1],((canvasWidth-96)/2),ScoreScroll+25+(65*Counter));
	}
	showImage(WantedB[2],((canvasWidth-96)/2),ScoreScroll+25+(65*(ga.MaxEnem+1)));
	scr.setClip(0,0,canvasWidth,canvasHeight);
	if ((ga.NewRecord)&&((ga.timeToChange % 2) == 0)) textDraw(ga.gameText[ga.TEXT_SCORES+1][0],((canvasWidth-80)/2),ScoreScroll+25-15+(65*(ga.MaxEnem+2)), 80, 0xFF0000, 0x010);
}

public void PintaCarasScore()
{
	for (int Counter=0; Counter<ga.MaxEnem; Counter++)
	{
		showImage(WantedP[Counter/4],32*(Counter%4),0,32,48,((canvasWidth-96)/2)+10,ScoreScroll+25+8+(65*Counter));
	}
	showImage(WantedP[ga.MaxEnem/4],32*(ga.MaxEnem%4),0,32,48,((canvasWidth-96)/2)+10,ScoreScroll+25+8+(65*ga.MaxEnem));
}

public void PintaTotalScore()
{
	if (ga.Record==false)
	{
		for (int Counter=0; Counter<ga.MaxEnem; Counter++)
		{
			PintaNumeroW(((canvasWidth-96)/2)+48,ScoreScroll+25+(65*Counter)+32,ga.Recuento[Counter+1]);
		}
		PintaNumeroW(((canvasWidth-96)/2)+48,ScoreScroll+25+(65*(ga.MaxEnem))+32,ga.Recuento[0]);
		scr.setClip(0,0,canvasWidth,canvasHeight);
		textDraw(ga.gameText[ga.TEXT_SCORES][1], ((canvasWidth-86)/2),ScoreScroll+35+(65*(ga.MaxEnem+1))+3, 90, 0xFFFFFF, 0x110);
		Rayo(((canvasWidth-80)/2),ScoreScroll+23+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+23+(65*(ga.MaxEnem+1))+42,10,2);
		Rayo(((canvasWidth-80)/2),ScoreScroll+27+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+27+(65*(ga.MaxEnem+1))+42,10,2);
		Rayo(((canvasWidth-80)/2),ScoreScroll+30+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+30+(65*(ga.MaxEnem+1))+42,10,2);
		Rayo(((canvasWidth-80)/2),ScoreScroll+20+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+32+(65*(ga.MaxEnem+1))+42,10,2);
		PintaNumero(((canvasWidth-45)/2),ScoreScroll+20+(65*(ga.MaxEnem+1))+42,ga.MaxScore);
	} else
	{
		for (int Counter=0; Counter<ga.MaxEnem; Counter++)
		{
			PintaNumeroW(((canvasWidth-96)/2)+48,ScoreScroll+25+(65*Counter)+32,ga.RRecuento[Counter+1]);
		}
		PintaNumeroW(((canvasWidth-96)/2)+48,ScoreScroll+25+(65*ga.MaxEnem)+32,ga.RRecuento[0]);
		scr.setClip(0,0,canvasWidth,canvasHeight);
		textDraw(ga.gameText[ga.TEXT_SCORES][1], ((canvasWidth-86)/2),ScoreScroll+35+(65*(ga.MaxEnem+1))+3, 90, 0xFFFFFF, 0x110);
		Rayo(((canvasWidth-80)/2),ScoreScroll+23+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+23+(65*(ga.MaxEnem+1))+42,10,2);
		Rayo(((canvasWidth-80)/2),ScoreScroll+27+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+27+(65*(ga.MaxEnem+1))+42,10,2);
		Rayo(((canvasWidth-80)/2),ScoreScroll+30+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+30+(65*(ga.MaxEnem+1))+42,10,2);
		Rayo(((canvasWidth-80)/2),ScoreScroll+20+(65*(ga.MaxEnem+1))+42,((canvasWidth+80)/2),ScoreScroll+32+(65*(ga.MaxEnem+1))+42,10,2);
		PintaNumero(((canvasWidth-45)/2),ScoreScroll+20+(65*(ga.MaxEnem+1))+42,ga.RMaxScore);
	}
}


public void PintaFinalScores()
{
	PintaFondoScore();
	PintaCarasScore();
	PintaTotalScore();
}
// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//
};