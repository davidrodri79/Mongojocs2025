package com.mygdx.mongojocs.ninjarun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;

import java.io.InputStream;

public class ITUtils01
{

ITGame01 ga;

public ITUtils01(ITGame01 Game)
{
	ga = Game;
}

public Image LoadImage(String FileName)
{
	System.gc();
	Image Img = null;

	try	{
		Img = Image.createImage(FileName);
	} catch (Exception e) {System.out.println("Error leyendo PNG: "+FileName);}

	System.gc();
	return Img;
}

// File Loading *****************************************************************

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void LoadFile(byte[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		is.read(buffer, 0, buffer.length);
		is.close();
	}catch(Exception e) {}
	System.gc();

}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public byte[] LoadFile(String Nombre, int Pos, short[] Size)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] Dest = new byte[Size[Pos]];

	try {
		for (int i=0 ; i<Pos ; i++) {is.skip(Size[i]);}
		is.read(Dest, 0, Dest.length);
		is.close();
	} catch(Exception e) {}
	System.gc();
	return Dest;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

public void LoadFile(short[] buffer, String Nombre)
{
	System.gc();
	InputStream is = getClass().getResourceAsStream(Nombre);

	try	{
		for (int i=0 ; i<buffer.length ; i++)
		{
		buffer[i]  = (short) (is.read() << 8);
		buffer[i] |= is.read();
		}
	is.close();
	}catch(Exception exception) {}
	System.gc();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public byte[] LoadFile(String Nombre)
{
	/*System.gc();

	InputStream is = getClass().getResourceAsStream(Nombre);

	byte[] buffer = null;

	try
	{
		int Size = 0;
		while (true)
		{
		int Desp = (int) is.skip(1024);
		if (Desp <= 0) {break;}
		Size += Desp;
		}

		is = null; System.gc();

		buffer = new byte[Size];

		is = getClass().getResourceAsStream(Nombre);
		Size = is.read(buffer, 0, buffer.length);

		while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}

		is.close();
	}
	catch(Exception exception) {}

	System.gc();

	return buffer;*/
	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
	byte[] bytes = file.readBytes();

	return bytes;
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

// File Loading End *************************************************************




//<-------------------------------------------------------------------------->

// ********************
// --------------------
// Anim Sprite - Engine
// ====================
// ********************

final int AniSprMAX=200;
int AniSprC;
int[] AniSprP;
AnimSprite[] AniSprs;

// ---------------
// Anim Sprite SET
// ===============

public int AniSprSET(int i, int CoorX, int CoorY, int FrameIni, int Frames, int Speed, int Mode)
{
	if (i<0)
	{
	if (AniSprC == AniSprMAX) {return(-1);}
	i=AniSprP[AniSprC++];
	AniSprs[i] = new AnimSprite();
	}

	AniSprs[i].SpriteSET(CoorX, CoorY, FrameIni, Frames, Speed, Mode);

	return (i);
}


public int AniSprSET(int i, int FrameIni, int Frames, int Speed, int Mode)
{
	return AniSprSET(i, AniSprs[i].CoorX, AniSprs[i].CoorY, FrameIni, Frames, Speed, Mode);
}


// ---------------
// Anim Sprite INI
// ===============

public void AniSprINI()
{
	AniSprC = 0;
	AniSprP = new int[AniSprMAX];
	AniSprs = new AnimSprite[AniSprMAX];
	for (int i=0 ; i<AniSprMAX ; i++) {AniSprP[i]=i;}
}

// ---------------
// Anim Sprite RES
// ===============

public int AniSprRES(int i)
{
	if (i!=-1) {AniSprs[i].Frames=0; AniSprs[i].Pause=0;}
	return -1;
}

// ---------------
// Anim Sprite END
// ===============

public void AniSprEND()
{
	AniSprs = null; AniSprP = null; AniSprC = 0;
}

// ---------------
// Anim Sprite RUN
// ===============

public void AniSprRUN()
{
	for (int i=0 ; i<AniSprC ; i++)
	{
	if ( AniSprs[AniSprP[i]].Play() )
		{
		int t=AniSprP[i]; AniSprs[t] = null;
		AniSprP[i--] = AniSprP[--AniSprC]; AniSprP[AniSprC] = t;
		}
	}
}


// -------------------
// Anim Sprite Ordenar
// ===================

public void AniSprOrdenar()
{
	int Conta=AniSprC-1;

	for (int t=0 ; t<AniSprC ; t++)
	{
		for (int i=0 ; i<Conta ; i++)
		{
			if (AniSprs[AniSprP[i]].Prio < AniSprs[AniSprP[i+1]].Prio)
			{
			int dato=AniSprP[i+1];
			AniSprP[i+1] = AniSprP[i];
			AniSprP[i] = dato;
			}
		}
	Conta--;
	}
}


// *******************
// -------------------
// Panel - Engine
// ===================
// *******************

Marco ma;
int PanelStatus;
int PanelType;
int PanelTypeOld;
// -------------------
// Panel SET
// ===================

public void PanelSET(int Type)
{
	PanelStatus = ga.GameStatus;
	ga.GameStatus = 900;

	PanelTypeOld = PanelType;
	PanelType = Type;

	switch (Type)
	{
	case 0:
		ma.MarcoINI();
		if (ga.GameConti>0) ma.MarcoADD(0x000,ga.Textos[1][0],1);
		ma.MarcoADD(0x000,ga.Textos[0][0],0);
		if (ga.gc.DeviceSound) {ma.MarcoADD(0x000,ga.Textos[2],10, ga.GameSound);}
		//if (ga.gc.DeviceVibra) {ma.MarcoADD(0x000,ga.Textos[3],11, ga.GameVibra);}
		ma.MarcoADD(0x000,ga.Textos[6][0],6);
		ma.MarcoADD(0x000,ga.Textos[7][0],7);
		ma.MarcoADD(0x000,ga.Textos[9][0],9);
		ma.MarcoSET_Option();
	break;

	case 1:
		ma.MarcoINI();
		ma.MarcoADD(0x000,ga.Textos[1][0],1);
		if (ga.gc.DeviceSound) {ma.MarcoADD(0x000,ga.Textos[2],10, ga.GameSound);}
		//if (ga.gc.DeviceVibra) {ma.MarcoADD(0x000,ga.Textos[3],11, ga.GameVibra);}
		ma.MarcoADD(0x000,ga.Textos[6][0],6);
		ma.MarcoADD(0x000,ga.Textos[13][0],12);
		ma.MarcoADD(0x000,ga.Textos[8][0],8);
		ma.MarcoADD(0x000,ga.Textos[9][0],9);
		ma.MarcoSET_Option();
	break;

	case 6:
		ma.MarcoINI();
		sc_Controles();
		ma.MarcoSET_Scroll(0,1);
	break;

	case 7:
		ma.MarcoINI();
		sc_Credits();
		ma.MarcoSET_Scroll(0,1);
	break;
	case 8:
		ma.MarcoINI();
		sc_Credits();
		ma.MarcoSET_Scroll(0,1);
	break;
	case 9:
		ma.MarcoINI();
		sc_History();
		ma.MarcoSET_Scroll(0,1);
	break;
	}

}

// -------------------
// Panel END
// ===================

public void PanelEND()
{
	ma.MarcoEND();
	ga.GameStatus = PanelStatus;
}


// -------------------
// Panel RUN
// ===================

public void PanelRUN()
{
	int MarcoKey=0;
	if (ga.KeybY1==-1 && ga.KeybY2!=-1) {MarcoKey=8;}
	if (ga.KeybY1== 1 && ga.KeybY2!= 1) {MarcoKey=2;}
	if (ga.KeybM1!= 0 && ga.KeybM2== 0) {MarcoKey=5;}

	int Result = ma.MarcoRUN(MarcoKey, ga.KeybY1);
	PanelEXE(Result);
}

// ---------------
// Panel EXE
// ===============

public void PanelEXE(int CMD)
{
	switch (CMD)
	{
	case -2: // Scroll ha llegado al final
		PanelEND();

		ga.Move = true;
		ga.gc.CanvasPaint=true;
		ga.gc.repaint();
		ga.gc.serviceRepaints();
		
		if (ga.GameStatus==3)
		{
			ga.GameStatus = 4;
		} else if (ma.BackGround == true) PanelSET(PanelTypeOld);
	break;

	case 0:	// Jugar de 0
		PanelEND();
		ga.GameStatus = 3;
		ga.GameConti = 0;
	break;

	case 1:	// Continuar
		PanelEND();
		if (ga.GameStatus < 50) 
		{
			ga.GameStatus = 4;
		}
	break;

	case 5:	// Siguiente Nivel
	break;

	case 6:	// Controles...
	case 7:	// About...
		PanelEND();
		PanelSET(CMD);
	break;

	case 8:	// Restart
		PanelEND();
		ga.GameStatus = 99;
	break;

	case 9:	// Exit Game
		ga.GameExit = 1;
	break;	

	case 10:
		ga.GameSound = ma.getOption();
		if (ga.GameSound==0) ga.gc.SoundRES(); else if (PanelStatus==2) ga.gc.SoundSET(0,0);
	break;

	case 11:
		ga.GameVibra = ma.getOption();
	break;
	
	case 12:
		PanelEND();
		ga.GameStatus = 75;
		ga.LevelFinalized = true;
	break;
	}

}

public void sc_Controles()
{
	ma.MarcoADD(0x111,ga.Textos[11][0]);
	ma.MarcoADD(0x000,ga.Textos[11][1]);
	ma.MarcoFIX(0x010,ga.Textos[11][2]);
	ma.MarcoFIX(0x000,ga.Textos[11][3]);
	ma.MarcoADD(0x000,"");//ga.Textos[11][4]);
	ma.MarcoFIX(0x010,ga.Textos[11][5]);
	ma.MarcoFIX(0x000,ga.Textos[11][6]);
	ma.MarcoADD(0x000,ga.Textos[11][7]);
	ma.MarcoFIX(0x010,ga.Textos[11][8]);
	ma.MarcoFIX(0x000,ga.Textos[11][9]);
	ma.MarcoADD(0x000,ga.Textos[11][10]);
	ma.MarcoFIX(0x010,ga.Textos[11][11]);
	ma.MarcoFIX(0x000,ga.Textos[11][12]);
	ma.MarcoADD(0x000,ga.Textos[11][13]);
	ma.MarcoFIX(0x010,ga.Textos[11][14]);
	ma.MarcoFIX(0x000,ga.Textos[11][15]);
	ma.MarcoADD(0x000,ga.Textos[11][16]);
	ma.MarcoFIX(0x010,ga.Textos[11][17]);
	ma.MarcoFIX(0x000,ga.Textos[11][18]);
	ma.MarcoADD(0x000,ga.Textos[11][19]);
	ma.MarcoFIX(0x010,ga.Textos[11][20]);
	ma.MarcoFIX(0x000,ga.Textos[11][21]);
	ma.MarcoADD(0x000,ga.Textos[11][22]);
	ma.MarcoFIX(0x010,ga.Textos[11][23]);
	ma.MarcoFIX(0x000,ga.Textos[11][24]);
	ma.MarcoADD(0x000,ga.Textos[11][25]);
}

// -------------------------------------------

public void sc_Credits()
{
	ma.MarcoADD(0x111,ga.Textos[12][0]);
	ma.MarcoADD(0x111,ga.Textos[12][1]);
	ma.MarcoADD(0x000,ga.Textos[12][2]);
	ma.MarcoFIX(0x010,ga.Textos[12][3]);
	ma.MarcoFIX(0x000,ga.Textos[12][4]);
	ma.MarcoADD(0x000,ga.Textos[12][5]);
	ma.MarcoFIX(0x002,ga.Textos[12][6]);
	ma.MarcoFIX(0x000,ga.Textos[12][7]);
	ma.MarcoADD(0x000,ga.Textos[12][8]);
	ma.MarcoFIX(0x002,ga.Textos[12][9]);
	ma.MarcoFIX(0x000,ga.Textos[12][10]);
	ma.MarcoADD(0x000,ga.Textos[12][11]);
	ma.MarcoFIX(0x002,ga.Textos[12][12]);
	ma.MarcoFIX(0x002,ga.Textos[12][13]);
	ma.MarcoFIX(0x000,ga.Textos[12][14]);
	ma.MarcoADD(0x000,ga.Textos[12][15]);
	ma.MarcoFIX(0x002,ga.Textos[12][16]);
	ma.MarcoFIX(0x000,ga.Textos[12][17]);
	ma.MarcoADD(0x011,ga.Textos[12][18]);
	ma.MarcoFIX(0x001,ga.Textos[12][19]);
	ma.MarcoFIX(0x001,ga.Textos[12][20]);
}

public void sc_History()
{
	ma.MarcoFIX(0x001,ga.Textos[28][0]);
	ma.MarcoFIX(0x001,ga.Textos[28][1]);
	ma.MarcoADD(0x000,""/*ga.Textos[28][2]*/);
	ma.MarcoFIX(0x001,ga.Textos[28][3]);
	ma.MarcoADD(0x000,""/*ga.Textos[28][4]*/);
	ma.MarcoFIX(0x001,ga.Textos[28][5]);
}

//MANAGER DE TEXTOS//

String[][] textosCreate(byte[] textos)
{
	int dataPos = 0;
	int dataBak = 0;
	short[] data = new short[1024];

	boolean campo = false;
	boolean subCampo = false;
	boolean primerEnter = true;

	// MONGOFIX ==========================================
	char tex_char[] = new char[textos.length];
	for(int i = 0; i < textos.length; i++)
		if(textos[i] < 0)
			tex_char[i]=(char)(textos[i]+256);
		else
			tex_char[i]=(char)textos[i];
	//=================================================

	int size = 0;

	for (int i=0 ; i<tex_char.length ; i++)
	{
		if (campo)
		{
			if (tex_char[i] == 0x7D)
			{
			subCampo = false;
			campo = true;
			}

			if ((tex_char[i] < 0x20)&&(tex_char[i] >= 0) || tex_char[i] == 0x7C || tex_char[i] == 0x7D)	// Buscamos cuando Termina un campo
			{
			data[dataPos+1] = (short) (i - data[dataPos]);

			dataPos+=2;

			campo=false;
			}

		} else {

			if (tex_char[i] == 0x7D)
			{
			subCampo = false;
			continue;
			}

			if (tex_char[i] == 0x7B)
			{
			dataBak = dataPos;
			data[dataPos++] = 0;
			campo = false;
			subCampo = true;
			size++;
			continue;
			}

			if (subCampo && tex_char[i] == 0x0A)
			{
				if (primerEnter)
				{
					primerEnter = false;
				} else {
					data[dataPos++] = (short) i;
					data[dataPos++] = 1;
					if (!subCampo) {size++;} else {data[dataBak]--;}
				}
				continue;
			}

			if (tex_char[i] >= 0x20)	// Buscamos cuando Empieza un campo nuevo
			{
				campo=true;
				data[dataPos] = (short) i;
				if (!subCampo) {size++;} else {data[dataBak]--;}
				primerEnter = true;
			}
		}
	}

	String[][] strings = new String[size][];

	dataPos=0;

	for (int i=0 ; i<size ; i++)
	{
	int num = data[dataPos];

	if (num<0) {num*=-1;dataPos++;} else {num = 1;}

	strings[i] = new String[num];

	for (int t=0 ; t<num; t++) {strings[i][t] = new String(tex_char, data[dataPos++], data[dataPos++]);}
	}

	return strings;
}




// *******************
// -------------------
// Prefs - Engine
// ===================
// *******************

byte[] PrefsData = new byte[] {1,1,0};

// -------------------
// Prefs INI
// ===================

public void PrefsINI()
{
	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("NinjaRun_Prefs", true);

		if (rs.getNumRecords() == 0)
		{
		rs.addRecord(PrefsData, 0, PrefsData.length);
		} else {
		PrefsData=rs.getRecord(1);
		}
		rs.closeRecordStore();
	} catch(Exception e) {}

	ga.GameSound=PrefsData[0];
	ga.GameVibra=PrefsData[1];
	ga.GameConti=PrefsData[2]; if (ga.GameConti < 0 || ga.GameConti >= ga.LastLevel) {ga.GameConti=0;}
}

// -------------------
// Prefs SET
// ===================

public void PrefsSET()
{
	PrefsData[0]=(byte)ga.GameSound;
	PrefsData[1]=(byte)ga.GameVibra;
	PrefsData[2]=(byte)ga.GameConti;

	RecordStore rs;

	try {
		rs = RecordStore.openRecordStore("NinjaRun_Prefs", true);

		if (rs.getNumRecords() == 0)
		{
		rs.addRecord(PrefsData, 0, PrefsData.length);
		} else {
		rs.setRecord(1, PrefsData, 0, PrefsData.length);
		}
		rs.closeRecordStore();
	} catch(Exception e) {}
}

// <=- <=- <=- <=- <=-



}
