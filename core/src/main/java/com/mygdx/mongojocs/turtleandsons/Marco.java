package com.mygdx.mongojocs.turtleandsons;


// -------------------------------------
// Marco Class v1.2 - Rev.2 (28.1.2003)
// =====================================
// iMode Rev.1 (11.11.2003);
// ------------------------------------
// Programado por Juan Antonio G�mez
// Para uso exclusivo de Microjocs S.L.
// -------------------------------------






// ---------------------
// Instrucciones de uso:
// =====================

// Constructor:
// ------------

// Marco( CanvasSizeX , CanvasSizeY )


// Inicializadores
// ---------------

// MarcoINI()				El Marco se Autoajusta al tama�o necesario segun los textos a mostrar.
// MarcoINI(SizeY)			El Marco tendr� el SizeY indicado, y el SizeX se ajustar� a los textos.
// MarcoINI(SizeX , SizeY)	El Marco tendr� los sizes indicados.


// Agrega Lineas de texto
// ----------------------

// MarcoADD(Formato, String)
// MarcoADD(Formato, String, Comando)
// MarcoADD(Formato, String[], Comando, Opcion)

// String = Texto. (ej: "Start")
// String[] = para los textos de opciones multiples.  ej: new String[] {"Vibrate ON","Vibrate OFF"}
// Opcion = Opcion inicial a mostrar (0=Vibrate ON ; 1=Vibrate OFF ; etc...)
// Comando = 'int' que DEVUELVE: [int MostrarRUN(int)] al Seleccionar este texto. ej: MarcoADD(0,"Start",1) al pulsar "5" [int=MarcoRUN(5)] nos devuelve int=1.

// El 'Formato' es como se muestra:
// --------------------------------
// xx0 = ADJUST_LEFT
// xx1 = ADJUST_CENTER
// xx2 = ADJUST_RIGHT

// x0x = TYPE_PLAIN
// x1x = TYPE_BOLD

// 0xx = SIZE_SMALL
// 1xx = SIZE_MEDIUM

// ej: MarcoADD(0x010,"Start",1)  => "Start" ser� SIZE_SMALL + TYPE_BOLD + ADJUST_LEFT


// Activar el marco:
// -----------------

// MarcoSET_Texto()		Muestra el Marco como un texto estatico.

// MarcoSET_Scroll()	Muestra el Marco haciendo scroll con el texto. (Speed default=1)
// MarcoSET_Scroll(Speed)	// Speed = retardo ( 0=Sin retardo ; 1 = Retardo de 1 frame ; etc... )

// MarcoSET_Option()	Muestra el Marco como un menu de opciones.


// Quitar el Marco:
// ----------------

// MarcoEND();


// Correr el Marco:
// ----------------

// Interpreta los movimientos del Marco_Option y nos informa de la linea seleccionada.
// Mueve el Marco Scroll y nos informa si ha terminado el scroll. (-2=Scroll finalizado)

// output = MarcoRUN(input)

// Input:	8=Abajo, 2=Arriba, 5=Seleccionar.
// Output:	-1=Sin novedad,  -2=Scroll ha llegado al final,
//			x = Seleccionado el comando x (SOLO para Marco tipo Option)


// Coger el estado de "Sound ON" u "Sound OFF":
// --------------------------------------------

// int = getOption()

// int:	0 para el primer texto, 1 para el segundo texto, 2 para el tercero, etc...

// Ejemplo:
//	0 = seleccionado "Sound ON"
//	1 = seleccionado "Sound OFF"



// *******************
// -------------------
// Marco - Engine
// ===================
// *******************

import com.mygdx.mongojocs.iapplicationemu.Font;
import com.mygdx.mongojocs.iapplicationemu.Graphics;

public class Marco
{

int Marco_ON;		// Estado del Marco Engine (0 = OFF)
int MarcoUpdate=0;	// Obligamos a Actualizar a MarcoIMP

int MarLcdSizeX=96;	// Size X del LCD
int MarLcdSizeY=65;	// Size Y del LCD

int MarMaxSizeY=65-3;	// Size Y m�ximo para el marco.

int MarcoX;			// Coordenadas y Size del Marco (cuadrado negro)
int MarcoY;
int MarcoSizeX;
int MarcoSizeY;

int MarTexX;		// Coordenadas y Size usables para imprimir los textos.
int MarTexY;
int MarTexSizeX;
int MarTexSizeY;

int TextoSizeX;		// Size m�ximo de los textos a imprimir
int TextoSizeY;

int MarTexScrY;		// Coordenada Y base para imprimir los textos.
int MarTexSpd;		// Velocidad del Scroll.
int MarTexCnt;		// Velocidad del Scroll.
int MarTexPos;		// Posicion actual de la linea de opciones.
int MarTexDesp;		// Desplazamiento en Pixels del Scroll

int MarMaySizeY;	// Size Y del caracter ">"

String	MarTexStr[][];
int		MarTexDat[][];


// -------------------
// Marco Constructor
// ===================

public Marco()
{
	Font f=Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
	MarMaySizeY=f.getHeight();
}

public Marco(int SizeX, int SizeY)
{
	MarLcdSizeX=SizeX;
	MarLcdSizeY=SizeY;

	MarMaxSizeY=MarLcdSizeY-5;

	Font f=Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
	MarMaySizeY=f.getHeight();
}

// -------------------
// Marco INI
// ===================

public void MarcoINI()
{
	MarcoINI(0,0);
}

public void MarcoINI(int SizeY)
{
	MarcoINI(0,SizeY);
}

public void MarcoINI(int SizeX, int SizeY)
{
	Marco_ON=0;
	MarTexStr=null;
	MarTexDat=null;

	MarcoSizeX=SizeX;
	MarcoSizeY=SizeY;
}



// -------------------
// Marco ADD
// ===================

public void MarcoADD(int Dato, String Texto)
{
	MarcoADD(Dato, new String[] {Texto}, 0, 0);
}

public void MarcoADD(int Dato, String Texto[])
{
	for (int i=0 ; i<Texto.length ; i++)
	{
	MarcoFIX(Dato, Texto[i]);
	}
}

public void MarcoADD(int Dato, String Texto, int Dat1)
{
	MarcoADD(Dato, new String[] {Texto}, Dat1, 0);
}

public void MarcoADD(int Dato, String[] Texto, int Dat1)
{
	MarcoADD(Dato, new String[] {Texto[0]}, Dat1, 0);
}

public void MarcoADD(int Dat0, String[] Texto, int Dat1, int Dat2)
{
	int Lines=(MarTexDat!=null)?MarTexDat.length:0;

	String Str[][] = new String[Lines+1][];
	int Dat[][] = new int[Lines+1][];

	int i=0;

	for (; i<Lines; i++)
	{
	Dat[i] = MarTexDat[i];
	Str[i] = MarTexStr[i];
	}

	Str[i] = Texto;
	Dat[i] = new int[] {Dat0, Dat1, Dat2};

	MarTexStr=Str;
	MarTexDat=Dat;
}


public void MarcoFIX(int Dato, String Texto)
{
	int Pos=0, PosIni=0, PosOld=0, Size=0;

	Font f=Font.getFont(Font.FACE_PROPORTIONAL | (((Dato&0x0F0)==0)?Font.STYLE_PLAIN:Font.STYLE_BOLD) | (((Dato&0xF00)==0)?Font.SIZE_SMALL:Font.SIZE_MEDIUM));

	while ( PosOld < Texto.length() )
	{
	Size=0;

	Pos=PosIni;

	while ( Size < (MarLcdSizeX-6) )
	{
		if ( Pos == Texto.length() ) {PosOld=Pos; break;}

		int Dat = Texto.charAt(Pos++);
		if (Dat==0x20) {PosOld=Pos-1;}

		Size += f.stringWidth(new String(new char[] {(char)Dat}));
	}

	if (PosOld-PosIni < 1) { while ( Pos < Texto.length() && Texto.charAt(Pos) >= 0x30 ) {Pos++;} PosOld=Pos; }

	MarcoADD(Dato, Texto.substring(PosIni,PosOld));

	PosIni=PosOld+1;
	}
}



// -------------------
// Marco SET
// ===================

public void MarcoSET(int SizeX, int SizeY)
{
	if (MarcoSizeX==0) {MarcoSizeX=SizeX;}
	if (MarcoSizeY==0) {MarcoSizeY=SizeY;}

	if (MarcoSizeY > MarMaxSizeY) {MarcoSizeY=MarMaxSizeY;}

	MarcoX=(MarLcdSizeX - MarcoSizeX)/2;
	MarcoY=(MarLcdSizeY - MarcoSizeY)/2;

	MarTexX=MarcoX+3;
	MarTexY=MarcoY+3;

	MarTexSizeX=MarcoSizeX-6;
	MarTexSizeY=MarcoSizeY-6;

	if (Marco_ON!=2 && MarTexSizeY > TextoSizeY)
		{
		MarTexY+=((MarTexSizeY-TextoSizeY)/2);
		MarTexSizeY=TextoSizeY;
		}

	MarcoUpdate=1;
}



// -------------------
// Marco SET Texto
// ===================

public void MarcoSET_Texto()
{
	if (MarTexDat == null) {return;}

	Marco_ON=1;

	MarcoCalc(MarTexStr, MarTexDat);

	MarcoSET( TextoSizeX+6 , TextoSizeY+6 );

	MarTexScrY=0;
}



// -------------------
// Marco SET Scroll
// ===================

public void MarcoSET_Scroll()
{
	MarcoSET_Scroll(0,1);
}

public void MarcoSET_Scroll(int Speed)
{
	MarcoSET_Scroll(Speed,1);
}

public void MarcoSET_Scroll(int Speed, int Desp)
{
	if (MarTexDat == null) {return;}

	Marco_ON=2;

	MarTexSpd=Speed;
	MarTexCnt=MarTexSpd;
	MarTexDesp=Desp;

	MarcoCalc(MarTexStr, MarTexDat);

	MarcoSET( TextoSizeX+6 , TextoSizeY+6 );

	MarTexScrY=MarTexSizeY;
}



// -------------------
// Marco SET Option
// ===================

public void MarcoSET_Option()
{
	MarcoSET_Option(0);
}

public void MarcoSET_Option(int Pos)
{
	if (MarTexDat == null) {return;}

	Marco_ON=3;

	for (int i=0 ; i<MarTexDat.length ; i++) {MarTexDat[i][0]=0x010;}

	MarcoCalc(MarTexStr, MarTexDat);

	for (int i=0 ; i<MarTexDat.length ; i++) {MarTexDat[i][0]=0x000;}

	TextoSizeX+=10;

	MarcoSET( TextoSizeX+6 , TextoSizeY+6 );

	MarTexX+=10;TextoSizeX-=10;

	MarTexScrY=0;
	MarTexPos=Pos;
	MarTexDat[MarTexPos][0]=0x010;
}


// -------------------
// Marco SET Listener
// ===================



// -------------------
// Marco Calc
// ===================

public void MarcoCalc(String Str[][], int Dat[][])
{
	TextoSizeX=8;
	TextoSizeY=0;

	for (int i=0 ; i<Str.length ; i++)
	{
	Font f=Font.getFont(Font.FACE_PROPORTIONAL | (((Dat[i][0]&0x0F0)==0)?Font.STYLE_PLAIN:Font.STYLE_BOLD) | (((Dat[i][0]&0xF00)==0)?Font.SIZE_SMALL:Font.SIZE_MEDIUM));
	TextoSizeY+=f.getHeight();
	for (int SizeX, t=0 ; t<Str[i].length ; t++)
	{
	SizeX=f.stringWidth(Str[i][t]);
	if ( TextoSizeX < SizeX ) {TextoSizeX=SizeX;}
	}
	}
}



// -------------------
// Marco RUN
// ===================

// Retorno:
//	-1 = Sin novedad.
//	-2 = El Scroll ha llegado al final.
//	 x = Numero asociado a la seleccion en modo "Option".

public int MarcoRUN(int Key)
{
	return MarcoRUN(Key,0);
}

public int MarcoRUN(int Key, int KeyY)
{
	int Ret=-1;

	switch (Marco_ON)
	{
	case 2:
		if (--MarTexCnt < 0)
		{
		MarTexCnt=MarTexSpd;
		MarcoUpdate=1;

		if (KeyY==1) {MarTexScrY-=MarTexDesp*2;}

		if (Key==5 || (KeyY!=-1 && (MarTexScrY-=MarTexDesp) < -TextoSizeY) ) {Ret=-2;}
		}
	break;

	case 3:
		switch (Key)
		{
		case 2:
			if (MarTexPos > 0) {MarTexPos--; MarcoUpdate=1;}
		break;

		case 8:
			if (MarTexPos < MarTexDat.length-1) {MarTexPos++; MarcoUpdate=1;}
		break;

		case 5:
			if (++MarTexDat[MarTexPos][2] == MarTexStr[MarTexPos].length) {MarTexDat[MarTexPos][2]=0;}
			Ret=MarTexDat[MarTexPos][1];
			MarcoUpdate=1;
		break;
		}

		for (int i=0 ; i<MarTexDat.length ; i++) {MarTexDat[i][0]=0x000;}
		MarTexDat[MarTexPos][0]=0x010;
	break;
	}

	return(Ret);
}



// -------------------
// Marco IMP
// ===================

public void MarcoIMP_Now(Graphics LCD_Gfx)
{
	MarcoUpdate=1;
	MarcoIMP(LCD_Gfx);
}

public void MarcoIMP(Graphics LCD_Gfx)
{
	if (Marco_ON == 0 ) {return;}

	if (MarcoUpdate == 0 ) {return;} else {MarcoUpdate=0;}


	LCD_Gfx.setColor(0);
 	LCD_Gfx.fillRect(MarcoX, MarcoY,  MarcoSizeX, MarcoSizeY);

	LCD_Gfx.setColor(0xFFFFFF);
 	LCD_Gfx.drawRect(MarcoX, MarcoY,  MarcoSizeX-1, MarcoSizeY-1);

	MarcoTextoIMP(LCD_Gfx, MarTexStr, MarTexDat);
}



// -------------------
// Marco Texto IMP
// ===================

public void MarcoTextoIMP(Graphics LCD_Gfx, String Str[][], int Dat[][])
{
	LCD_Gfx.setColor(0);
	LCD_Gfx.fillRect(0, 0,  MarLcdSizeX, MarLcdSizeY);


	LCD_Gfx.setColor(0xFFFFFF);

	if (Marco_ON==3)
	{
	Font f = Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
	LCD_Gfx.setFont(f);
	int Ascent = f.getAscent();

	int BaseY = ((((MarTexSizeY-MarMaySizeY)*256) / (Dat.length-1) ) * MarTexPos) / 256;

	LCD_Gfx.drawString(">",  MarTexX-8, MarTexY+BaseY + Ascent );

	MarTexScrY=(-MarMaySizeY*MarTexPos)+BaseY;
	}

	int SumaY=MarTexScrY;

	for (int i=0 ; i<Str.length ; i++)
	{
	Font f=Font.getFont(Font.FACE_PROPORTIONAL | (((Dat[i][0]&0x0F0)==0)?Font.STYLE_PLAIN:Font.STYLE_BOLD) | (((Dat[i][0]&0xF00)==0)?Font.SIZE_SMALL:Font.SIZE_MEDIUM));
	int Ascent = f.getAscent();
	if (SumaY >= -2  &&  SumaY <= MarTexSizeY-Ascent)
	{
	LCD_Gfx.setFont(f);
	int t=Dat[i][2];
	if ((Dat[i][0]&0xF)==0) {LCD_Gfx.drawString(Str[i][t],  MarTexX, MarTexY+SumaY + Ascent );}
	if ((Dat[i][0]&0xF)==1) {LCD_Gfx.drawString(Str[i][t],  MarTexX+((MarTexSizeX-f.stringWidth(Str[i][0]))/2), MarTexY+SumaY + Ascent );}
	if ((Dat[i][0]&0xF)==2) {LCD_Gfx.drawString(Str[i][t],  MarTexX+ (MarTexSizeX-f.stringWidth(Str[i][0]))   , MarTexY+SumaY + Ascent );}
	}
	SumaY+=f.getHeight();
	}
}



// -------------------
// Marco END
// ===================

public void MarcoEND()
{
	MarcoINI();
}



// -------------------
// Marco getOption
// ===================

public int getOption()
{
	return(MarTexDat[MarTexPos][2]);
}



// ---------------------------------
// Implementacion de CommandListener
// =================================


// <=- <=- <=- <=- <=-


};