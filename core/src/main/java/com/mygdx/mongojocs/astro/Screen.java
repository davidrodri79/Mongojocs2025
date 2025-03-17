package com.mygdx.mongojocs.astro;

// Astro 3003

// Clase SCREEN para Nokia 7650 Color Scroll


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.mongojocs.midletemu.DeviceControl;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

public class Screen
{

GamePlay gp;

Image LCD_Img;
Graphics LCD_Gfx;

Scroll scroll;

class CreateDoubleBufferTask implements Runnable {

	Screen scr;
	int sizex, sizey;

	CreateDoubleBufferTask(Screen s, int sx, int sy)
	{
		scr = s; sizex = sx; sizey = sy;
	}

	@Override
	public void run() {
		scr.LCD_Img = new Image();
		scr.LCD_Img._createImage((sizex),sizey);	// El Size de X DEBE ser multiplo de 8
		scr.LCD_Gfx = LCD_Img.getGraphics();
	}
};

public Screen(GamePlay gp)
{
	this.gp=gp;


//	Creamos Doble Bufer
// --------------------
	int SizeX=gp.GameSizeX/8;
	if (gp.GameSizeX%8 != 0) {SizeX++;}
	/*LCD_Img = Image.createImage((SizeX*8),gp.GameSizeY);	// El Size de X DEBE ser multiplo de 8
	LCD_Gfx = LCD_Img.getGraphics(); MONGOFIX*/
	Gdx.app.postRunnable(new CreateDoubleBufferTask(this, SizeX*8, gp.GameSizeY));
	
	scroll = new Scroll();


	scroll.ScrollINI(gp.GameSizeX, gp.GameSizeY);

}


// ---------------
// TileMap - SET
// ===============

public void setTileMap()
{

	scroll.ScrollSET(gp.ChaPat, gp.FaseTilSizeX, gp.FaseTilSizeY,  TilesImg, 32);

}


// ----------------------
// TileMap - Scroll - SET
// ======================

public void setScroll()
{
}


// ---------
// GOM - SET
// =========

public void setGOM()
{
}


// ---------
// GOM - RUN
// =========

int LCD_Dir;
int Exit,Cor;

public void runGOM()
{

//	Imprimidos la Fase segun el scroll X,Y en el doble buffer
// ----------------------------------------------------------
//	Graphics LCD_Gfx = LCD_Img.getGraphics();
	LCD_Gfx.setClip (0,0, gp.GameSizeX, gp.GameSizeY);


	if (gp.JugarShow!=0)
	{
	scroll.ScrollRUN(gp.FaseX, gp.FaseY);
	scroll.ScrollIMP(LCD_Gfx);
	} else {
	LCD_Gfx.setColor(0);
	LCD_Gfx.fillRect(0,0,gp.GameSizeX,gp.GameSizeY);
	}



//	Imprimimos Cohete en el doble buffer
// -------------------------------------
	if (gp.Cohe_ON!=0) {CoheteIMP();}



//	Imprimimos Sprites en el doble buffer
// --------------------------------------
	for (int i=0 ; i<gp.AnimSpriteMAX ; i++)
	{
	if (gp.AnimSprites[i] != null)
	{
	PutSprite(SprObjImg, gp.AnimSprites[i].CoorX-(gp.FaseX), gp.AnimSprites[i].CoorY-(gp.FaseY), 16, 16, gp.AnimSprites[i].FrameIni+gp.AnimSprites[i].FrameAct);
	}
	}


//	Imprimimos Protagonista en el doble buffer
// -------------------------------------------
	if (gp.Prot_ON != 0)
	{
		if ( (gp.JugarInmune & 0x01) ==0 ) 
		{
		PutSprite(SprProtImg, gp.ProtSprX-(gp.FaseX), gp.ProtSprY-(gp.FaseY)+(gp.ProtSumaY), 16, 24, gp.ProtSprFrame);
		}
	}



//	Imprimimos Marcador en el doble buffer
// ---------------------------------------
	if (gp.Cohe_ON == 0)
	{

	if (gp.FaseMineral==0)
	{
		if (--Exit > 7)
		{
		PutSprite(SprObjImg, 16,   0, 1,  16,16, 0x3A);
		PutSprite(SprObjImg, 16,  16, 1,  16,16, 0x3B);
		}
		else if (Exit < 0) {Exit=16;}
	} else {
	PutSprite(SprObjImg, 16,   0, 1,  16,16, 0x2A);
	PutSprite(SprObjImg, 16,  16, 1,  16,16, 0x30+gp.FaseMineral);
	}

	int FuelX=(gp.GameSizeX-56)/2;

	PutImage(SprObjImg, 196,48,  56,7,  FuelX,1);

	if (gp.ProtVolarCnt != 0)
	{PutImage(SprObjImg, 210,34,  (gp.ProtVolarCnt/2),3,  FuelX+15,3);}

	PutSprite(SprObjImg, 16, gp.GameSizeX-30, 1,   16,16, 0x2B+(Cor>>4)); Cor++;Cor&=0x1F;
	PutSprite(SprObjImg, 16, gp.GameSizeX-15, 1,   16,16, 0x30+gp.JugarVidas);
	}


}



// -------------
// Sprites - SET
// =============

public void setSprites()
{
}

// -------------
// Sprites - RUN
// =============

public void runSprites()
{
}



// --------------------
// MIDlet - Paint - RUN
// ====================

public void paint(Graphics g)
{

//	g.drawImage (LCD_Img,  gp.GameX, gp.GameY,  Graphics.TOP|Graphics.LEFT);	// Pintamos en el LCD

	SoundRUN();

	LCD_Gfx=g;

	if (gp.JugarUpdate!=0)
	{
	gp.JugarUpdate=0;
	setScroll();
	runSprites();
	runGOM();
	}


	if (gp.LoadImg!=null)
	{
	LCD_Gfx.setClip(0,0, gp.getWidth(), gp.getHeight() );
	LCD_Gfx.setColor(-1);
	LCD_Gfx.fillRect(0,0, gp.getWidth(), gp.getHeight() );
	LCD_Gfx.drawImage(gp.LoadImg, (gp.GameSizeX-gp.LoadImg.getWidth() )/2, gp.GameY+(gp.GameSizeY-gp.LoadImg.getHeight() )/2, Graphics.LEFT|Graphics.TOP);
	gp.LoadImg = null;
	}

}




// ---------------
// Leemos Archivos
// ===============

Image TilesImg;
Image SprProtImg;
Image SprObjImg;
Image SprCohetImg;
Image SprCohetFocImg;
Image SprPortaImg;

public void loadFiles()
{

	try	{
	TilesImg = Image.createImage("/gfx/Tiles.png");
	SprProtImg = Image.createImage("/gfx/SprProt.png");
	SprObjImg = Image.createImage("/gfx/SprObj.png");
	SprCohetImg = Image.createImage("/gfx/SprCohet.png");
	SprCohetFocImg = Image.createImage("/gfx/SprCohetFoc.png");
	SprPortaImg = Image.createImage("/gfx/SprPorta.png");
	} catch (Exception e) {}

/*
	TilesImg = LCD_Img;
	SprProtImg = LCD_Img;
	SprObjImg = LCD_Img;
	SprCohetImg = LCD_Img;
	SprCohetFocImg = LCD_Img;
	SprPortaImg = LCD_Img;
*/

}


public void PutSprite(byte[] Gfx, byte[] Msk,  int X, int Y,  int SizeX, int SizeY,  int Frame)
{
//	Graphics LCD_Gfx = LCD_Img.getGraphics();
	LCD_Gfx.setColor(0);
	LCD_Gfx.setClip(X,Y,SizeX+2,SizeY+2);
	LCD_Gfx.fillRect(X,Y,SizeX,SizeY);
}




public void PutSprite(Image Img,  int X, int Y,  int SizeX, int SizeY,  int Frame)
{
//	Graphics LCD_Gfx = LCD_Img.getGraphics();
	LCD_Gfx.setClip(X,Y, SizeX,SizeY);
	LCD_Gfx.drawImage (Img, X-((Frame%16)*SizeX), Y-((Frame/16)*SizeY), Graphics.TOP|Graphics.LEFT);	// Pintamos en el LCD
}

public void PutSprite(Image Img, int Width, int X, int Y,  int SizeX, int SizeY,  int Frame)
{
//	Graphics LCD_Gfx = LCD_Img.getGraphics();
	LCD_Gfx.setClip(X,Y, SizeX,SizeY);
	LCD_Gfx.drawImage (Img, X-((Frame%Width)*SizeX), Y-((Frame/Width)*SizeY), Graphics.TOP|Graphics.LEFT);	// Pintamos en el LCD
}


public void PutImage(Image Sour, int SourX, int SourY, int SizeX, int SizeY, int DestX, int DestY)
{
//	Graphics LCD_Gfx = LCD_Img.getGraphics();
	LCD_Gfx.setClip(DestX, DestY, SizeX, SizeY);
	LCD_Gfx.drawImage(Sour, DestX-SourX, DestY-SourY, Graphics.TOP|Graphics.LEFT);
}





// -------------------
// Cohete IMP
// ===================

public void CoheteIMP()
{
	if (gp.CoheFrame!=-1)
	{
	PutSprite(SprCohetImg, 1, (gp.CoheX-gp.FaseX) , (gp.CoheY-gp.FaseY), 72,87, gp.CoheFrame);
	}

	if (gp.CoheFocFrame!=-1)
	{
	PutSprite(SprCohetFocImg, 1, (gp.CoheX-gp.FaseX)+14 , (gp.CoheY-gp.FaseY)+86, 40,13, gp.CoheFocFrame);
	}

	if (gp.CohePortaFrame!=-1)
	{
	PutSprite(SprPortaImg, 1,  (gp.GameSizeX-128)/2 ,gp.CohePortaY,  128,128,  0);
	}
}

// <=- <=- <=- <=- <=-




/* ===================================================================

	SoundINI()
	----------
		Inicializamos TODO lo referente al los sonidos (cargar en memoria, crear bufers, etc...)

	SoundSET(n� Sonido , Repetir)
	-----------------------------
		Hacemos que suene un sonido (0 a x) y que se repita x veces.
		Repetir == 0: Repeticion infinita

	SoundRES()
	----------
		Paramos el ultimo sonido.

	SoundRUN()
	----------
		Debemos ejecutar este metodo en CADA ciclo para gestionar 'tiempos'

	VibraSET(microsegundos)
	-----------------------
		Hacemos vibrar el mobil x microsegundos

=================================================================== */



// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: MIDP 2.0 - Rev.0 (28.11.2003)
// -------------------
// Version ESPECIAL: Motorola V300 - Rev.0 (03.1.2004)
// ===================
// *******************

Music[] Sonidos;

int SoundOld = -1;
int SoundCache = -1;

// -------------------
// Sound INI
// ===================

public void SoundINI()
{
	Sonidos = new Music[5];

	Sonidos[0] = SoundCargar("/Astro_muerte.mid");
	Sonidos[1] = SoundCargar("/Astro_intro.mid");
	Sonidos[2] = SoundCargar("/Astro_item.mid");
	Sonidos[3] = SoundCargar("/Astro_level.mid");
	Sonidos[4] = SoundCargar("/Astro_explosion.mid");
}

public Music SoundCargar(String Nombre)
{
	return Gdx.audio.newMusic(Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre));
}

// -------------------
// Sound SET
// ===================

public void SoundSET(int Ary, int Loop)
{
	if (Loop<1) {Loop=-1;}

	SoundRES();

	if (gp.GameSound!=0)
	{
		try
		{
			if (SoundCache != Ary)
			{
			//if (SoundCache!=-1) {Sonidos[SoundCache].deallocate();}
			//Sonidos[Ary].prefetch();
			SoundCache = Ary;
			}

			//VolumeControl vc = (VolumeControl) Sonidos[Ary].getControl("VolumeControl"); if (vc != null) {vc.setLevel(80);}
			Sonidos[Ary].setLooping(Loop == 0);

			Sonidos[Ary].play();
		}
		catch(Exception exception) {exception.printStackTrace();}
	}

	SoundOld=Ary;
}

// -------------------
// Sound RES
// ===================

public void SoundRES()
{
	if (SoundOld != -1)
	{
		try
		{
			Sonidos[SoundOld].stop();
		}
		catch (Exception e) {}

		SoundOld = -1;
	}
}

// -------------------
// Sound RUN
// ===================

public void SoundRUN()
{
}

// -------------------
// Vibra SET
// ===================

public void VibraSET(int Time)
{
	DeviceControl.startVibra(100, Time);
}

// <=- <=- <=- <=- <=-





// -------------------
// Device INI
// ===================

public void DeviceINI()
{
}

// -------------------
// Device END
// ===================

public void DeviceEND()
{
}

// <=- <=- <=- <=- <=-


};