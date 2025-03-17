package com.mygdx.mongojocs.hotspeed;// -----------------------------------------------
// Source Base iMode v1.1 Rev.1 (14.11.2003) - Canvas
// ===============================================
// iMode
// ------------------------------------
// Programado por Juan Antonio G�mez
// ------------------------------------




// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game Canvas - Bios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.iapplicationemu.AudioPresenter;
import com.mygdx.mongojocs.iapplicationemu.Canvas;
import com.mygdx.mongojocs.iapplicationemu.Connector;
import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.Font;
import com.mygdx.mongojocs.iapplicationemu.Frame;
import com.mygdx.mongojocs.iapplicationemu.Graphics;
import com.mygdx.mongojocs.iapplicationemu.HttpConnection;
import com.mygdx.mongojocs.iapplicationemu.IApplication;
import com.mygdx.mongojocs.iapplicationemu.Image;
import com.mygdx.mongojocs.iapplicationemu.MediaImage;
import com.mygdx.mongojocs.iapplicationemu.MediaListener;
import com.mygdx.mongojocs.iapplicationemu.MediaManager;
import com.mygdx.mongojocs.iapplicationemu.MediaPresenter;
import com.mygdx.mongojocs.iapplicationemu.MediaSound;
import com.mygdx.mongojocs.iapplicationemu.PhoneSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class GameCanvas extends Canvas implements MediaListener
{

Game ga;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Constructor
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public GameCanvas(Game ga)
{
	this.ga = ga;
	ga.scroll = scroll;
	CanvasINI();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Refresco de la pantalla (repaint)
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Graphics LCD_Gfx;


boolean bImpDesplazamiento = true;

public void paint(Graphics g)
{       
	SoundRUN();
	if (CanvasPaint)
	{
	CanvasPaint=false;

	g.lock();

	LCD_Gfx=g;

	CanvasRUN();

	g.unlock(true);
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Gestion de Eventos (Teclado, Timers, etc...)
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

int KeybX, KeybY, KeybB, KeybM;

public void processEvent(int type, int param)
{
	if (type == Display.KEY_RELEASED_EVENT)
	{
		switch (param)
		{
		case Display.KEY_SOFT1:
		case Display.KEY_SOFT2:
			KeybM=-1;
		return;

		case Display.KEY_UP: KeybY= 0; return;
		case Display.KEY_DOWN: KeybY= 0; return;
		case Display.KEY_LEFT: KeybX= 0; return;
		case Display.KEY_RIGHT: KeybX= 0; return;
		case Display.KEY_SELECT: KeybB= 0; KeybM= 0; return;

		case Display.KEY_0: KeybB=-1; return;
		case Display.KEY_1: KeybB=-1; return;
		case Display.KEY_2: KeybB=-1; return;
		case Display.KEY_3: KeybB=-1; return;
		case Display.KEY_4: KeybB=-1; KeybX= 0; return;
		case Display.KEY_5: KeybB=-1; return;
		case Display.KEY_6: KeybB=-1; KeybX= 0; return;
		case Display.KEY_7: KeybB=-1; return;
		case Display.KEY_8: KeybB=-1; return;
		case Display.KEY_9: KeybB=-1; return;
		}
	}
	else if (type == Display.KEY_PRESSED_EVENT)
	{
		switch (param)
		{
		case Display.KEY_SOFT1:
		case Display.KEY_SOFT2:
			KeybM=-1;
		return;

		case Display.KEY_UP:	KeybY=-1; return;
		case Display.KEY_DOWN:	KeybY= 1; return;
		case Display.KEY_LEFT:	KeybX=-1; return;
		case Display.KEY_RIGHT: KeybX= 1; return;
		case Display.KEY_SELECT:KeybB= 5; KeybM= 2; return;

		case Display.KEY_0: KeybB=10; return;
		case Display.KEY_1: KeybB=1; KeybX=-1; KeybY=-1; return;
		case Display.KEY_2: KeybB=2; KeybY=-1; return;
		case Display.KEY_3: KeybB=3; KeybX= 1; KeybY=-1; return;
		case Display.KEY_4: KeybB=4; KeybX=-1; return;
		case Display.KEY_5: KeybB=5; return;
		case Display.KEY_6: KeybB=6; KeybX= 1; return;
		case Display.KEY_7: KeybB=7; return;
		case Display.KEY_8: KeybB=8; KeybY= 1; return;
		case Display.KEY_9: KeybB=9; return;
		}
	}

}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Rutinas de Impresion en pantalla
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void ImageSET(Image Img)
{
	ImageSET(Img, (CanvasSizeX-Img.getWidth())/2, (CanvasSizeY-Img.getHeight())/2);
}

// ---------------------------------------------------------

public void ImageSET(Image Img, int X, int Y)
{
	LCD_Gfx.drawImage(Img, X,Y);
}


public void PutSprite(Image[] Img,  int X, int Y,  int _Frame)
{
	int Frame2=_Frame;

	try{

	_Frame*=6;
	X+=spritesCor[_Frame++];
	Y+=spritesCor[_Frame++];
	_Frame += 2;

	LCD_Gfx.drawImage (Img[spritesCor[_Frame]], X, Y);
	} catch (Exception e) {
		System.out.println("putesprite hace aguas...");
		System.out.println("_Frame = "+_Frame);
		
	}
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas de Entrada y Salida de Medios
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Image[] LoadImage(String FileName, int Frames)
{
	Image Img[] = new Image[Frames];
	for (int i=0 ; i<Frames ; i++) {Img[i] = LoadImage(FileName+i+".gif");}
	return Img;
}

// ---------------------------------------------------------

public Image LoadImage(String FileName)
{
	MediaImage mimage = MediaManager.getImage("resource://"+FileName);

	try {
		mimage.use();
	} catch (Exception ui) {}

	return mimage.getImage();
}

// ---------------------------------------------------------

public Image LoadImage(int Pos)
{
	MediaImage mimage = MediaManager.getImage("scratchpad:///0;pos="+Pos);

	try {
		mimage.use();
	} catch (Exception e) {/*Handle UI problem here*/}

	return mimage.getImage();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Rutinas de 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// ---------------------------------------------------------
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// ---------------------------------------------------------







// *******************
// -------------------
// Canvas - Engine
// ===================
// *******************

//			LCD_Gfx.setOrigin(17,25);

int CanvasSizeX=getWidth();
int CanvasSizeY=getHeight();
//int CanvasSizeX=128;
//int CanvasSizeY=130;

boolean CanvasPaint=false;
boolean CanvasFillPaint=false;
int CanvasFillRGB;
Image CanvasImg;
//Image sprMarcador;

// -------------------
// Canvas INI
// ===================




public void CanvasINI()
{


	logoMJImg = LoadImage("/logoMJ.gif");

	CanvasImg = logoMJImg;
	
	CanvasPaint=true;
	
	ProgBarINI(100,5, /*14*/31,/*100*/130 ,false);
}


// -------------------
// Canvas SET
// ===================

	boolean	bFinCarga = false;
	public boolean repintarContadores = false;
	
	Scroll scroll = new Scroll(this);
	public byte scrollMap[] = new byte[2240];	
//	public byte scrollXMap[] = new byte[68];	
	public byte scrollXMap[] = new byte[172];	
	byte scrollPal[] = new byte[768];	
	byte scrollRaw[] = new byte[8192];
	
	byte spritesCor[] = new byte[486];
	Image spritesImg[];

	Image contadoresImg, tituloImg, logoMJImg, cambioFaseImg,cambioFaseRectImg;
	Image cambioFaseCocheImg, gameoverImg, congratulationsImg;

	byte durezaTiles[] = new byte[256];

	boolean b = true;


public void CanvasSET(int nivel)
{

	if(b) {

		ProgBarSET(0,1);
		System.out.println("kk0");
		if(FS_Create("hotspeed/Data")) ga.terminate();
		ProgBarEND();

		// FILES DE SPRITES ////////////////////////////////////////////////////////
		spritesCor = FS_LoadFile(0,9);
		// carga de settings
		ga.GAMESETTINGS = FS_LoadFile(0,0);
		
		System.out.println("Game Settings:"+ga.GAMESETTINGS[0]+","+ga.GAMESETTINGS[1]+
							","+ga.GAMESETTINGS[2]+","+ga.GAMESETTINGS[3]+".");
		

		b = false;
		bFinCarga = false;
		SoundINI();
	}


	setSoftLabel(Frame.SOFT_KEY_1, "Menu");
	setSoftLabel(Frame.SOFT_KEY_2, "Menu");

	ProgBarEND(); 

	scroll.ScrollINI(112,128);
	if(nivel<0) {
		bFinCarga = true;
		cargarNivel(nivel);
		return;
	}
	cargarNivel(nivel);

	bFinCarga = true;
}


// -------------------
// Canvas Img SET
// ===================

public void CanvasImageSET(String FileName, int RGB)
{
	CanvasFillRGB=RGB; CanvasFillPaint=true;
	CanvasImg = LoadImage(FileName);
}


// -------------------
// Canvas Img PUT
// ===================

public void CanvasImagePUT(String FileName, int RGB)
{
	CanvasFill(RGB);

	CanvasImg = LoadImage(FileName);

	if (CanvasImg!=null) {ImageSET(CanvasImg); CanvasImg=null; System.gc();}
}


// -------------------
// Canvas Fill
// ===================

public void CanvasFill(int RGB)
{
	LCD_Gfx.setColor(RGB & 0xFFFFFF);
	LCD_Gfx.fillRect(0,0, CanvasSizeX,CanvasSizeY );
}



// -------------------
// Canvas RUN
// ===================

public void CanvasRUN()
{
	if (CanvasFillPaint) { CanvasFillPaint=false; CanvasFill(CanvasFillRGB); }

	if (CanvasImg!=null) { ImageSET(CanvasImg); CanvasImg=null; System.gc(); }

	CanvasIMP();
}


// -------------------
// Canvas IMP
// ===================


int protaYOld, protaFuelOld, protaVelOld;

boolean bImpCaratula = false;
boolean bImpMarcador = false;
boolean bImpObjetos = false;
boolean bImpScroll = false;
boolean bImpMarco = false;
boolean bImpTexto = false;
boolean bImpCambioFase = false;
boolean bImpTransicion = false; 
int 	iImpTransicionTipo = 0;
boolean bImpTransicionO_I = true; //false:out, true:in
boolean bImpSlider = false;
boolean bImpSemaforo = false;
boolean bDummy = false;
boolean bDeleteImages = false;
boolean bImpTextoFuel = false;

int counter = -14;
int counter2;

long textoFuelTimer = -1;
int textoFuelCounter = 0;
private void impTextoFuel() {
	if(textoFuelTimer < 0) textoFuelTimer = System.currentTimeMillis();
	
	if(System.currentTimeMillis() - textoFuelTimer < 1000) {
		LCD_Gfx.setColor(0xFFFFFF);
		LCD_Gfx.drawString("+50",ga.PROTA.x,ga.PROTA.Y()-textoFuelCounter++);
	} else {
		textoFuelTimer = -1;
		textoFuelCounter = 0;
		bImpTextoFuel = false;
	}
}


private void impTransicion() {
	
	if(counter == -14) {
		iImpTransicionTipo = ga.RND(150);
		if(iImpTransicionTipo < 50) counter = 162/2;
		else if(iImpTransicionTipo < 100) counter = 180/2;
		else {
			counter = 162/2;
			counter2 = 180/2;	
		}
	}
	
	LCD_Gfx.setColor(0x0);
	
	if(iImpTransicionTipo < 50) {
		if(bImpTransicionO_I) {
			LCD_Gfx.fillRect(0,0,162/2-counter,180);
			LCD_Gfx.fillRect(162-(162/2-counter),0,162,180);
		} else {
			LCD_Gfx.fillRect(0,0,162/2-counter,180);
			LCD_Gfx.fillRect(162-(162/2-counter),0,162,180);
		}
	} else if(iImpTransicionTipo < 100) {
		if(bImpTransicionO_I) {
			LCD_Gfx.fillRect(0,0,162,180/2-counter);
			LCD_Gfx.fillRect(0,180-(180/2-counter),162,180);
		} else {
			LCD_Gfx.fillRect(0,0,162,180/2-counter);
			LCD_Gfx.fillRect(0,180-(180/2-counter),162,180);
		}
	} else {
		if(bImpTransicionO_I) {
			LCD_Gfx.fillRect(0,0,162/2-counter,180);
			LCD_Gfx.fillRect(162-(162/2-counter),0,162,180);
			LCD_Gfx.fillRect(0,0,162,180/2-counter2);
			LCD_Gfx.fillRect(0,162-(180/2-counter2),162,180);
		} else {
			LCD_Gfx.fillRect(0,0,162/2-counter,180);
			LCD_Gfx.fillRect(162-(162/2-counter),0,162,180);
			LCD_Gfx.fillRect(0,0,162,180/2-counter2);
			LCD_Gfx.fillRect(0,180-(180/2-counter2),162,180);
		}	
	}	 
	
	counter -= 3;
	counter2 -= 3;
	
	if(counter <= -10) {
		bImpTransicion = false;
		counter = -14;
	}
}

//public String sImpTexto;

private void impTexto() {
	LCD_Gfx.setColor(0xFFFFFF);
	
	if(ga.GameStatus == Game.EJ_SIN_FUEL) {
		Font f = Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
		LCD_Gfx.setFont(f);
		LCD_Gfx.setColor(Graphics.getColorOfName(Graphics.WHITE));
		LCD_Gfx.drawString(	ga.TEXTO[ga.TEXT_OUT_OF_FUEL][0],
							(ga.PROTA.x+10)-f.stringWidth(ga.TEXTO[ga.TEXT_OUT_OF_FUEL][0])/2,
							ga.PROTA.Y() );


						
	} else if(ga.GameStatus == Game.EJ_FASE_COMPLETADA) {
		Font f = Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
		LCD_Gfx.setFont(f);
		LCD_Gfx.setColor(Graphics.getColorOfName(Graphics.WHITE));
		LCD_Gfx.drawString(	ga.TEXTO[ga.TEXT_COMPLETED][0],
							(112-f.stringWidth(ga.TEXTO[ga.TEXT_COMPLETED][0]))/2,
							30);
	}
}


private void impSemaforo() {

	int b1,b2,b3;
	
	if(ga.COUNT	> -40){
		b1 = b2 = b3 = 69;
	} else if(ga.COUNT > -64) { 
		b1 = 70; b2 = b3 = 69;
	} else if(ga.COUNT > -79) { 
		b1 = b2 = 70; b3 = 69;
	} else {
		b1 = b2 = b3 = 70;
	}
	
	PutSprite(spritesImg,30,5+28,b1);	PutSprite(spritesImg,10+30,5+28,b1);
	PutSprite(spritesImg,30,12+28,b2);	PutSprite(spritesImg,10+30,12+28,b2);
	PutSprite(spritesImg,30,19+28,b3);	PutSprite(spritesImg,10+30,19+28,b3);
	
	if(ga.COUNT%3==0) PutSprite(spritesImg,50,130/*80*/,0); //prota de plastico :-p
}

private void impCambioFase() {
	
	LCD_Gfx.setColor(0);
	LCD_Gfx.fillRect(0,0,162,180);

	if(ga.COUNT%6==0) bDummy = !bDummy;
	if(cambioFaseImg == null) System.out.println("impCambioFase() -> imagen nula!");
	else {
		
		LCD_Gfx.setColor(0x0);
		LCD_Gfx.fillRect(0,0,162,180);
		
		if((ga.COUNT-27)*4 > 25) {
			try{
			LCD_Gfx.drawImage(cambioFaseImg,17,((ga.COUNT-27)*4));
			}catch(Exception e) {}
		} else {
			try{			
			if(bDummy) LCD_Gfx.drawImage(cambioFaseRectImg,6+17,/*160*/185-32*(ga.NIVEL+1));
			LCD_Gfx.drawImage(cambioFaseImg,17,25);
			}catch(Exception e) {}
		}
		
		if(ga.COUNT >= 0 && ga.COUNT < 27) //transicion
			try{			
			LCD_Gfx.drawImage(cambioFaseCocheImg,105+17,/*150*/175-32*(ga.NIVEL)+30-(26-ga.COUNT));
			}catch(Exception e) {}
		else if (ga.COUNT < 27) //posicion final
			try{
			LCD_Gfx.drawImage(cambioFaseRectImg,6+17,/*160*/185-32*(ga.NIVEL+1));
			LCD_Gfx.drawImage(cambioFaseImg,17,25);
			LCD_Gfx.drawImage(cambioFaseCocheImg,105+17,/*150*/175-32*(ga.NIVEL)+30-26);
			}catch(Exception e) {}
		else if((ga.COUNT-27)*8 < 0)//posicion inicial
			try{
			LCD_Gfx.drawImage(cambioFaseCocheImg,105+17,/*150*/175-32*(ga.NIVEL)+30);
			}catch(Exception e) {}			
	}
}

private void impCaratula() {
	/*
	if(tituloImg == null) System.out.println("impFondo() -> imagen nula!");
	else LCD_Gfx.drawImage(tituloImg,0,0);
	*/
/*
	LCD_Gfx.setColor(0x0); 
	LCD_Gfx.fillRect(0,0,128,130); 
*/
	
	switch(ga.GameStatus) {
		
		case Game.EJ_MENU:
		case Game.EJ_MENU_1:
		case Game.EJ_CERDITOS:
		case Game.EJ_AYUDA: 
//		case Game.EJ_PRESENTACION:
			tituloImg = _FS_LoadImage(3,4);
			try{
			LCD_Gfx.setColor(0);
			LCD_Gfx.fillRect(0,0,162,180);
			LCD_Gfx.drawImage(tituloImg,0,0);//17,25); 
			}catch(Exception e) {}			
			safeNull(tituloImg);
			System.gc();
			break;
		case Game.EJ_PRESENTACION:
			tituloImg = _FS_LoadImage(3,4);
			try{
			LCD_Gfx.setColor(0);
			LCD_Gfx.fillRect(/*-17,-25,*/0,0,162,180);
			LCD_Gfx.drawImage(tituloImg,0,0); 
			}catch(Exception e) {}			
			safeNull(tituloImg);
			System.gc();
			break;
		case Game.EJ_GAMEOVER: 
			gameoverImg = _FS_LoadImage(3,6);
			try{			
			LCD_Gfx.setColor(0);
			LCD_Gfx.fillRect(/*-17,-25,*/0,0,162,180);
			LCD_Gfx.drawImage(gameoverImg,0,0); 
			}catch(Exception e) {}
			safeNull(gameoverImg);
			System.gc();
			break;
		case Game.EJ_CONGRATULATIONS: 
			congratulationsImg = _FS_LoadImage(3,5);
			try{
			LCD_Gfx.setColor(0);
			LCD_Gfx.fillRect(0,0,162,180);
			LCD_Gfx.drawImage(congratulationsImg,0,0); 
			}catch(Exception e) {}			
			safeNull(congratulationsImg);
			System.gc();
			break;
	}
}

private void impMarcador() {
	
/*
	if(protaYOld != 10 + (38-(43-ga.PROTA.realY()/200)) ||
	   protaFuelOld != 68+(10-ga.PROTA.combustible()/100)*6 ||
	   protaVelOld != 68+(15-ga.PROTA.velocidad())*4 ) {
*/				
//		try{
		LCD_Gfx.drawImage(contadoresImg,112,0);
//		}catch(Exception e) {}			
		pintarContadores(LCD_Gfx,ga.PROTA.velocidad(),ga.PROTA.combustible(),ga.PROTA.realY());

/*S
		//System.out.println("Repintando marcadores...");
		protaYOld = 10 + (38-(43-ga.PROTA.realY()/200));
		protaFuelOld = 68+(10-ga.PROTA.combustible()/100)*6;
		protaVelOld = 68+(15-ga.PROTA.velocidad())*4;
	}	
*/
}

private void impObjetos() {

/*
	if(ga.GameStatus == Game.EJ_FASE_COMPLETADA) {

		System.out.println("entra a pintar...");
		System.out.println("Y: "+ga.PROTA.Y());
	}
*/	
	// ITEMS			
	if(ga.GameStatus == Game.EJ_JUEGO)
		for(int i=0;i<ga.NUM_ITEMS;i++) {
			if(ga.ITEMS[i] != null)	{
				if(ga.ITEMS[i].tipo == Item.IT_MARCA) {
					
					if(ga.ENEMIGOS[ga.buscarObjeto(ga.ITEMS[i].id2,true)] != null) {
					//if(ga.buscarObjeto(ga.ITEMS[i].id2,true)>0) {
						//System.out.println("KAKAKA "+ga.buscarObjeto(ga.ITEMS[i].id2,true));
						LCD_Gfx.setColor(0x0);
						LCD_Gfx.drawLine(	
									ga.ITEMS[i].x+6,
									ga.ITEMS[i].y,
									ga.ENEMIGOS[ga.buscarObjeto(ga.ITEMS[i].id2,true)].x+6,
									ga.ENEMIGOS[ga.buscarObjeto(ga.ITEMS[i].id2,true)].y+20
										);
						LCD_Gfx.drawLine(	
									ga.ITEMS[i].x+10,
									ga.ITEMS[i].y,
									ga.ENEMIGOS[ga.buscarObjeto(ga.ITEMS[i].id2,true)].x+10,
									ga.ENEMIGOS[ga.buscarObjeto(ga.ITEMS[i].id2,true)].y+20
										);
						//System.out.println("KAKAKA "+ga.buscarObjeto(ga.ITEMS[i].id2,true));

					}
				} else {
 					PutSprite(	spritesImg,
								ga.ITEMS[i].X(),
								ga.ITEMS[i].Y(),
								ga.ITEMS[i].sprite()	);
				}
			}
		}
		
	// PROTA
	if(ga.PROTA.explotando) {
		PutSprite(spritesImg,ga.PROTA.X(),ga.PROTA.Y(),ga.PROTA.spriteLast());
		//System.out.println("pintando coche bajo explosion...");
	}


	if(!ga.PROTA.invulnerable || (ga.PROTA.invulnerable && ga.gameCounter%2==0)) {
		//if(spritesImg[ga.PROTA.sprite()]!=null)
		if(ga.GameStatus == Game.EJ_FASE_COMPLETADA)
			PutSprite(spritesImg,ga.PROTA.X(),ga.PROTA.realY(),ga.PROTA.sprite());
		else if(ga.GameStatus != Game.EJ_FASE_INICIO_2)
			PutSprite(spritesImg,ga.PROTA.X(),ga.PROTA.Y(),ga.PROTA.sprite());									
	}
	
	// COCHES SALIDA				
	for(int i=0;i<5;i++) 
		if(ga.ENEMIGOS_INI[i] != null) 
			PutSprite(spritesImg,ga.ENEMIGOS_INI[i].X(),ga.ENEMIGOS_INI[i].Y(),ga.ENEMIGOS_INI[i].sprite());

		
	// COCHES CARRERA
	for(int i=0;i<ga.NUM_ENEMIGOS;i++)
		if(ga.ENEMIGOS[i] != null)
		if(ga.ID_ENEMIGOS[i] != -1)
				if(ga.ENEMIGOS[i].tipo == Enemigo.ET_CAMION) {
						PutSprite(	spritesImg,
									ga.ENEMIGOS[i].X(),
									ga.ENEMIGOS[i].Y()-20,
									8	);
						PutSprite(	spritesImg,
									ga.ENEMIGOS[i].X(),
									ga.ENEMIGOS[i].Y(),
									17	);
				} else {
						PutSprite(	spritesImg,
									ga.ENEMIGOS[i].X(),
									ga.ENEMIGOS[i].Y(),
									ga.ENEMIGOS[i].sprite()	);
				}

			
}

private void impScroll() {

	if(ga.GameStatus == Game.EJ_FASE_COMPLETADA) scroll.ScrollRUN(0,0);
	else if(ga.GameStatus != Game.EJ_FASE_INICIO_2)
			scroll.ScrollRUN(0,ga.PROTA.realY()-18*8);
	
	scroll.ScrollIMP(LCD_Gfx);
}

private void impMarco() {
	ga.marco.MarcoIMP(LCD_Gfx);	
}


public void CanvasIMP() {

	
	if(!bFinCarga) {
		LCD_Gfx.setColor(0xFFFFFF);
		LCD_Gfx.fillRect(0,0, CanvasSizeX, CanvasSizeY);
		try{
		LCD_Gfx.drawImage(logoMJImg,12+17,30+25);	
		}catch(Exception e) {}			
		ProgBarIMP(LCD_Gfx);
		FS_ErrorDraw(LCD_Gfx,CanvasSizeX,CanvasSizeY);
		return;
	}

	if(bImpCaratula)	{	impCaratula(); 	bImpCaratula	= false;	}
	if(bImpCambioFase)	{	impCambioFase();bImpCambioFase	= false;	}
	if(bImpMarco) 		{	impMarco(); 	bImpMarco 		= false;	}
	if(bImpMarcador /*&& ga.GameTicks%2==0*/) 	{ 	impMarcador(); 	bImpMarcador 	= false; 	}
	if(bImpScroll) 		{ 	impScroll(); 	/*bImpScroll 		= false;*/ 	}
	if(bImpTransicion)  {	impTransicion();	}

	// DEBUG - Mostrar durezas
	
	if(bImpScroll) {
	/*
		for (int j=0;j<14*2;j++) {	
			for (int k=0;k<16*2;k++) {	
				if(!ga.esPisable(j*4,k*4)) LCD_Gfx.setColor(23461);
				else LCD_Gfx.setColor(0xFFFFFF);
				LCD_Gfx.fillRect(j*4+1,k*4+1,4,3);
			}
		}
	*/
		bImpScroll 		= false;
	}


	
	if(bImpObjetos) 	{ 	
		impObjetos(); 	
		bImpObjetos 	= false; 	
	}

	
	if(bImpTexto) 		{ 	impTexto(); 	/*bImpTexto	 	= false;*/ 	}
	if(bImpTextoFuel)	{ 	impTextoFuel(); 	/*bImpTexto	 	= false;*/ 	}
	if(bImpSemaforo)	{	impSemaforo();	bImpSemaforo 	= false;	}
	
	if(bImpSlider) ProgBarIMP(LCD_Gfx);


}


// <=- <=- <=- <=- <=-


// ----------------------------------------------------------------------------

// *******************
// -------------------
// ===================
// *******************

// -------------------
// ===================

// <=- <=- <=- <=- <=-

// ----------------------------------------------------------------------------


//BGR

void pintarContadores(Graphics g, int v, int c, int y) {

	int size;
	
	//VELOCIDAD
	LCD_Gfx.setColor(0x00FF00);
	size = v*14/3 - 2;
	if(size < 4 ) size = 0;
	
	g.fillRect(119,175-size,5,size);
	
	
	//FUEL
	LCD_Gfx.setColor(0x0000FF);
	
	size = (c/7)+12;
	if(size == 12) size = 0;
	
	if(c > 0)

	g.fillRect(	112+18,
				175-((c/7)+12),
				5,
				(c/7)+12);

	//POSICION

	LCD_Gfx.setColor(0xFF0000);
	size = y/200+10;
	g.fillRect(	112+7,
				65-(65-size)
				,4,4);

}


void safeNull(Image img) {
	System.gc();
	if(img != null) {
		img.dispose();
		img = null;
		System.gc();
	}
}


void cargarNivel(int nivel) {


	System.out.println("Mem Total: "+Runtime.getRuntime().totalMemory());

	
	scrollMap = null; scrollXMap = null; scrollPal = null; scrollRaw = null;
	durezaTiles = null; /*spritesCor = null;  spritesImg = null;*/
	
	
	safeNull(contadoresImg); safeNull(tituloImg); safeNull(cambioFaseImg);
	safeNull(cambioFaseRectImg); safeNull(cambioFaseCocheImg);
	safeNull(gameoverImg); safeNull(congratulationsImg);
	System.gc();

	System.out.println("Mem Total: "+Runtime.getRuntime().totalMemory());

	scrollMap = new byte[2240];	
	scrollXMap = new byte[172];	
	scrollPal = new byte[768];	
	scrollRaw = new byte[8192];
	durezaTiles = new byte[256];


	// FILES DE SCROLL /////////////////////////////////////////////////////////
	//0   1   2   3   4   5
	//DUR,PAL,RAW,DUR,PAL,RAW...

	if(nivel > 0) {
		scrollMap = FS_LoadFile(0,nivel);
		scrollXMap = FS_LoadFile(0,nivel+4);
		durezaTiles = 	FS_LoadFile(2,3*(nivel-1)+0);
		scrollPal = 	FS_LoadFile(2,3*(nivel-1)+1);
		scrollRaw = 	FS_LoadFile(2,3*(nivel-1)+2);
	}	
	contadoresImg = FS_LoadImage(3,3);
	
	cambioFaseImg = FS_LoadImage(3,0);
	cambioFaseRectImg = FS_LoadImage(3,2);
	cambioFaseCocheImg = FS_LoadImage(3,1);
}


void finalizarScroll() {
	
	for(int i=0;i<34;safeNull(spritesImg[i++]))
	scroll.ScrollEND();	
}

void preparaScroll() {
						
	safeNull(cambioFaseImg);
	safeNull(cambioFaseRectImg); 
	safeNull(cambioFaseCocheImg);
	System.gc();
	///////////////////////////////////////////

	ProgBarSET(0,21);
	scroll.ScrollSET(scrollMap,scrollXMap,14,160,scrollRaw,scrollPal);
	ProgBarEND();
	spritesImg = FS_LoadImage(1); /*70 gifs...*/
}



// <=- <=- <=- <=- <=-


// *************************
// -------------------------
// iMode Microjocs FileSystem v2.0 - Engine - Rev.7 (26.1.2004)
// =========================
// *************************

// ------------------------
// HEAD Data Format
// ------------------------
// 00 - (C) - ID - "MICROJOCS_FS"
// 0C - (1) - Version del FileSystem
// 0D - (1) - Tipo de Compresion DATA
// 0E - (1) - Checksum HEAD
// 0F - (1) - Checksum DATA
// 10 - (4) - Size Total Scratch-pad consumido
// 14 - (4) - Size HEAD / Index FAT-Directorios)
// 18 - (4) - Size Total FAT
// 1C - (4) - Size Total DATA (Todos los archivos)

// 20 - (1) - n� de Bloques
// 21 - (1) - n� de Bloques cargados Ok
// 22 - (4) - Size x Bloque
// 26 - (A) - Checksums para 10 Bloques (0 a 9)

// (0x30 = Size del HEAD)
// ========================

// ------------------------
// Errores: SI FS_Create() devuelve TRUE:
// ------------------------
// FS_Error:
// 1: Error de Conexion inicial (No se puede crear una conexion / user DEBE activar las conexiones)
// 2: Error descargando los archivos (Checksum error)
// ========================

	int FS_Error;
	boolean FS_ErrorShow = false;

	int[] FS_Data;
	byte[] FS_Head;

// ---------------
// FS_Create
// ===============

	public boolean FS_Create(String FileName) {
		FS_Head = FS_LoadData(0, 0x30);

		if (FS_Head == null || FS_Head[0] != 0x4D || FS_Head[1] != 0x49) {
			int Retry = 5;
			while (true) {
				if (Retry-- == 0) {
					FS_ErrorCreate();
					return true;
				}

				byte[] Bufer = FS_DownloadData(FileName + ".mfs");

				if (Bufer == null || Bufer.length != 0x30) {
					FS_Error = 1;
					continue;
				}    // Controlamos que el Size sea correcto.

				int Checksum = Bufer[0x0E];
				Bufer[0x0E] = 0;
				if (Checksum != FS_Checksum(Bufer, 0, Bufer.length)) {
					FS_Error = 2;
					continue;
				}    // Comprobamos Checksum

				FS_SaveData(0, Bufer);
				FS_Head = FS_LoadData(0, 0x30);
				break;
			}
		}

		ProgBarINS(FS_Head[0x20]);    // Agregamos numero de bloques para cargar en el Slider
		FS_Notify();                // Se ha descargado un .mfs Ok (HEAD)

		int BankSize = ((FS_Head[0x22] & 0xFF) << 24) | ((FS_Head[0x23] & 0xFF) << 16) | ((FS_Head[0x24] & 0xFF) << 8) | (FS_Head[0x25] & 0xFF);

		for (int i = 0; i < FS_Head[0x21]; i++) {
			FS_Notify();
		}

		for (int i = FS_Head[0x21]; i < FS_Head[0x20]; i++) {
			int Retry = 5;
			while (true) {
				if (Retry-- == 0) {
					FS_ErrorCreate();
					return true;
				}

				byte[] Bufer = FS_DownloadData(FileName + i + ".mfs");

				if (Bufer == null || Bufer.length != BankSize) {
					FS_Error = 1;
					continue;
				}    // Controlamos que el Size sea correcto.

				if (FS_Head[0x26 + i] != FS_Checksum(Bufer, 0, Bufer.length)) {
					FS_Error = 2;
					continue;
				}    // Comprobamos Checksum

				FS_SaveData(FS_Head.length + (i * BankSize), Bufer);
				FS_SaveData(0x21, new byte[]{(byte) (i + 1)});
				break;
			}
			FS_Notify();    // Se ha descargado un .mfs Ok (DATA)
		}

// Cargamos la "FAT" del MFS
// -------------------------
		int Pos = ((FS_Head[0x14] & 0xFF) << 24) | ((FS_Head[0x15] & 0xFF) << 16) | ((FS_Head[0x16] & 0xFF) << 8) | (FS_Head[0x17] & 0xFF);
		int Size = ((FS_Head[0x18] & 0xFF) << 24) | ((FS_Head[0x19] & 0xFF) << 16) | ((FS_Head[0x1A] & 0xFF) << 8) | (FS_Head[0x1B] & 0xFF);

		FS_Data = new int[(Size / 4)];

		try {

			//InputStream in = Connector.openInputStream("scratchpad:///0;pos=" + Pos);
			// MONGOFIX
			File file = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder, "scratchpad");
			FileInputStream in = new FileInputStream(file);
			in.skip(Pos);

			for (int i = 0; i < FS_Data.length; i++) {
				short s;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] = (s & 0xFF) << 24;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] |= (s & 0xFF) << 16;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] |= (s & 0xFF) << 8;
				s = (short)in.read(); if(s<0) s+=256;
				FS_Data[i] |= (s & 0xFF);
			}

			in.close();
		} catch (IOException e) {
		}

		FS_Error = 0;
		return false;
	}


// ---------------
// FS_LoadData
// ===============

	public byte[] CargaFichero(String f)
	{
		FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+f);
		byte[] bytes = file.readBytes();

		return bytes;
	}

	public byte[] FS_LoadData(int Pos, int Size)
	{
	/*
	byte[] Bufer = new byte[Size];

	try {
		InputStream in = Connector.openInputStream("scratchpad:///0;pos="+Pos);
		in.read(Bufer,0,Bufer.length);
		in.close();
	} catch (IOException e) {return null;}

	return Bufer;
*/

	/*File file = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder, "scratchpad");

	if(file.exists())
	{
		try {
			FileInputStream fis = new FileInputStream(file);
			byte b[] = new byte[Size];
			fis.read(b, Pos, Size);
			return b;
		}
		catch(IOException e)
		{
			return null;
		}
	}
	else return null;*/

		RandomAccessFile file;

		try {
			file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder + "/scratchpad", "r");

			byte b[] = new byte[Size];

			try {
				file.seek(Pos);
				file.read(b, 0, Size);
				file.close();
				return b;

			} catch (IOException e2) {
				e2.printStackTrace();
				return null;
			}

		}catch(FileNotFoundException e)
		{
			return null;
		}
	}

// ---------------
// FS_SaveData
// ===============

	public int FS_SaveData(int Pos, byte[] Bufer)
	{
	/*try {
		OutputStream out=Connector.openOutputStream("scratchpad:///0;pos="+Pos);
		out.write(Bufer,0,Bufer.length);
		out.close();
	} catch (Exception e) {return 0;}

	return Bufer.length;*/

		RandomAccessFile file;
		try {
			file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder + "/scratchpad", "rw");

		}catch(FileNotFoundException e)
		{
			File dirs = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder);
			dirs.mkdirs();
			try {
				file = new RandomAccessFile(IApplication.appFilesFolder + "/" + IApplication.assetsFolder+ "/scratchpad", "rw");

			} catch (IOException e2) {
				e2.printStackTrace();
				return 0;
			}
		}

		try {
			file.seek(Pos);
			file.write(Bufer);
			return Bufer.length;

		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
/*
	if(!file.())
	{
		File dirs = new File(IApplication.appFilesFolder+"/"+IApplication.assetsFolder);
		dirs.mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	try {
		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(Bufer, Pos, Bufer.length);
		fos.close();
		return Bufer.length;

	} catch(Exception e)
	{
		return 0;
	}*/
	}

// ---------------
// FS_LoadFile
// ===============

	public byte[] FS_LoadFile(int Pos, int SubPos)
	{
		Pos = (FS_Data[Pos] / 4 ) + SubPos;

		return FS_LoadData(FS_Data[Pos], FS_Data[Pos+1] - FS_Data[Pos]);
	}


// ---------------
// FS_SaveFile
// ===============

	public void FS_SaveFile(int Pos, int SubPos, byte[] Bufer)
	{
		Pos = (FS_Data[Pos] / 4 ) + SubPos;

		FS_SaveData(FS_Data[Pos], Bufer);
	}


// ---------------
// FS_LoadImage
// ===============

	public Image[] FS_LoadImage(int Pos)
	{
		int Ini = FS_Data[Pos];
		int Size = (FS_Data[Pos+1]-Ini)/4;

		Ini/=4;

		Image[] Img = new Image[Size];

		for (int i=0 ; i<Size ; i++) {
			Img[i] = Image.createImage(IApplication.assetsFolder+"/"+Pos+"/"+i+".gif");
			//Img[i] = loadImage(FS_Data[Ini++]);
		}

		return Img;
	}


// ---------------
// FS_LoadImage
// ===============

	public Image FS_LoadImage(int Pos, int SubPos)
	{
		Image im = Image.createImage(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".gif");
		return im;

		//return loadImage(FS_Data[(FS_Data[Pos]/4)+SubPos]);
	}

	public Image _FS_LoadImage(int Pos, int SubPos)
	{
		Image im = new Image();
		im._createImage(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".gif");
		return im;

		//return loadImage(FS_Data[(FS_Data[Pos]/4)+SubPos]);
	}


// ---------------
// FS_DownloadData
// ===============

	public byte[] FS_DownloadData(String Filename)
	{
		FileHandle file = Gdx.files.internal(IApplication.assetsFolder + "/"+Filename);
		byte[] bytes = file.readBytes();

		return bytes;
	/*byte[] dat = null;

	int Retry=5;
	boolean FileOk = false;

	do {
		HttpConnection conn = null;
		try {
			conn=(HttpConnection)(Connector.open(IApplication.getCurrentApp().getSourceURL()+"../mfs_file/" + Filename, (Integer) Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://www.iwapserver.com/microjocs/games/mfs_file/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://mjj.no-ip.biz:8085/microjocs/games/mfs_file/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open("http://www.arrakis.es/%7ejoanant/" + Filename, Connector.READ));
//			conn=(HttpConnection)(Connector.open(IApplication.getCurrentApp().getSourceURL() + Filename, Connector.READ));
			conn.setRequestMethod(HttpConnection.GET);
			conn.connect();

			try {
				InputStream in=conn.openInputStream();
				dat = new byte[(int)conn.getLength()];
				in.read(dat);
				in.close();
				FileOk = true;
			} catch (Exception e) {}

		} catch (Exception e) {}

		try {
			if (conn!=null) {conn.close(); conn=null;}
		} catch (Exception e) {}

		if (!FileOk && --Retry==0) {return null;}
	}
	while (!FileOk);

	return dat;*/
	}

// ---------------
// FS_Checksum
// ===============

	public byte FS_Checksum(byte[] Bufer, int Pos, int Size)
	{
		int Checksum=0;
		for (int i=0 ; i<Size ; i++) {Checksum+=Bufer[Pos+i]; Checksum = (Checksum^i)&0xFF;}
		return (byte)(Checksum & 0xFF);
	}

// ---------------
// FS_Notify - Notifica que un Bloque ha sido leido OK, PONER aqu� el "repaint()" para actualizar el 'Slider' de Descarga.
// ===============

	public void FS_Notify()
	{
		ProgBarADD();
	}

// ---------------
// FS_Error Create
// ===============

	public void FS_ErrorCreate()
	{
		FS_ErrorShow = true;

		long time = System.currentTimeMillis();

		while ((System.currentTimeMillis()-time) < 16000 )
		{
			//canvasShow=true; repaint();
			try {Thread.sleep(2000);} catch (Exception e) {}
		}
	}

// ---------------
// FS_Error Draw
// ===============

	public void FS_ErrorDraw(Graphics g, int cSizeX, int cSizeY)
	{
		if (FS_ErrorShow)
		{
			g.setColor(0xFFFFFF);
			g.fillRect(0,0, cSizeX, cSizeY);
			g.setColor(0x0);

			Font f = Font.getFont(Font.FACE_PROPORTIONAL | Font.STYLE_BOLD | Font.SIZE_SMALL);
			g.setFont(f);
			int y = f.getAscent();
			int height = f.getHeight();

//	for (int i=0 ; i<FS_strError.length ; i++) {g.drawString(FS_strError[i], 4, y); y += height;}

			for (int i=0 ; i<FS_strError.length ; i++)
			{
				int Pos=0, PosIni=0, PosOld=0, Size=0;

				while ( PosOld < FS_strError[i].length() )
				{
					Size=0;

					Pos=PosIni;

					while ( Size < (cSizeX-6) )
					{
						if ( Pos == FS_strError[i].length() ) {PosOld=Pos; break;}

						int Dat = FS_strError[i].charAt(Pos++);
						if (Dat==0x20) {PosOld=Pos-1;}

						Size += f.stringWidth(new String(new char[] {(char)Dat}));
					}

					if (PosOld-PosIni < 1) { while ( Pos < FS_strError[i].length() && FS_strError[i].charAt(Pos) >= 0x30 ) {Pos++;} PosOld=Pos; }

					g.drawString(FS_strError[i].substring(PosIni,PosOld), 3, y); y += height;

					PosIni=PosOld+1;
				}
			}
		}
	}

	String[] FS_strError = new String[] {
			"- ERROR -",
			" ",
			"- Si es la primera vez que ejecuta esta aplicaci�n, aseg�rese de tener activado el acceso a la red.",
			" ",
			"- Posible problema de cobertura GPRS. Int�ntelo de nuevo m�s tarde.",
	};

// <=- <=- <=- <=- <=-




// *******************
// -------------------
// ProgBar - Engine - Rev.2 (20.1.2004)
// ===================
// *******************

int ProgBarX;
int ProgBarY;
int ProgBarSizeX;
int ProgBarSizeY;
int ProgBarPos;
int ProgBarTotal;
boolean ProgBar_ON = false;
boolean ProgBarOrient;


// -------------------
// ProgBar INI
// ===================

public void ProgBarINI(int SizeX, int SizeY, int DestX, int DestY, boolean h_v)
{
	ProgBarX = DestX;
	ProgBarY = DestY;
	ProgBarSizeX = SizeX;
	ProgBarSizeY = SizeY;
	ProgBarOrient = h_v;
}

// -------------------
// ProgBar END
// ===================

public void ProgBarEND()
{
	ProgBarSET(1,1);
	ProgBar_ON = false;
}

// -------------------
// ProgBar SET
// ===================

public void ProgBarSET(int Pos)
{
	ProgBarSET(Pos, ProgBarTotal);
}

public void ProgBarSET(int Pos, int Total)
{
	ProgBarPos = Pos;
	ProgBarTotal = Total;

	ProgBar_ON = true;

	CanvasPaint=true; repaint(); //while (CanvasPaint) {try {Thread.sleep(1);} catch (Exception e) {}}
}

// -------------------
// ProgBar INS
// ===================

public void ProgBarINS(int suma)
{
	ProgBarTotal += suma;
}

// -------------------
// ProgBar ADD
// ===================

public void ProgBarADD()
{
	ProgBarSET(++ProgBarPos);
}

// -------------------
// ProgBar IMP
// ===================

public void ProgBarIMP(Graphics Gfx)
{
	if (ProgBar_ON)
	{
	Gfx.setColor(0xFFFFFF);
	Gfx.fillRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+6, ProgBarSizeY+6);
	Gfx.setColor(0);
	Gfx.drawRect(ProgBarX-3, ProgBarY-3, ProgBarSizeX+5, ProgBarSizeY+5);

	if(ProgBarOrient) //vertical
		Gfx.fillRect(ProgBarX, ProgBarY, ProgBarSizeX, ((ProgBarPos*ProgBarSizeY)/ProgBarTotal) );
	else
		Gfx.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);
	

	//Gfx.fillRect(ProgBarX, ProgBarY, ((ProgBarPos*ProgBarSizeX)/ProgBarTotal), ProgBarSizeY);
	}
}

// <=- <=- <=- <=- <=-


// *******************
// -------------------
// Sound - Engine - Rev.0 (28.11.2003)
// -------------------
// Adaptacion: iMode - Rev.0 (28.11.2003)
// ===================
// *******************

AudioPresenter SoundPlayer;
MediaSound[] Sound;
int SoundOld = -1;
int SoundLoop;

// -------------------
// Sound INI
// ===================

public void SoundINI()
{
	SoundPlayer = AudioPresenter.getAudioPresenter();

	Sound = new MediaSound[7];

	for (int i=0 ; i<Sound.length ; i++) {Sound[i] = LoadSound(4,i);}

	SoundPlayer.setMediaListener(this);
}

// -------------------
// Sound SET
// ===================

public void SoundSET(int Ary, int Loop)
{
	SoundRES();

	try
	{
		SoundPlayer.setSound(Sound[Ary]);
		SoundPlayer.play();
		SoundOld=Ary;
		SoundLoop=Loop-1;
	}
	catch(Exception e) {}
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
			SoundPlayer.stop();
			SoundOld = -1;
		}
		catch(Exception e) {}
	}
}

// -------------------
// Sound RUN
// ===================

public void SoundRUN()
{
	if ( Vibra_ON && (System.currentTimeMillis() - VibraTimeIni) > VibraTimeFin)
	{
	Vibra_ON=false;
		try {
		PhoneSystem.setAttribute(PhoneSystem.DEV_VIBRATOR, PhoneSystem.ATTR_VIBRATOR_OFF);
		} catch (Exception e) {}
	}
}

// -------------------
// Vibra SET
// ===================

boolean Vibra_ON;
long VibraTimeIni;
int VibraTimeFin;

public void VibraSET(int Time)
{


	VibraTimeIni = System.currentTimeMillis();
	VibraTimeFin = Time;
	Vibra_ON=true;

	try
	{
		PhoneSystem.setAttribute(PhoneSystem.DEV_VIBRATOR, PhoneSystem.ATTR_VIBRATOR_ON);
	}
	catch (Exception e) {}
}


// -------------------
// mediaAction para controlar Start, Stop, Complete y as� hacer loops
// ===================

public void mediaAction(MediaPresenter mp, int type, int value)
{
	if (SoundOld!=-1 && mp == SoundPlayer && type == AudioPresenter.AUDIO_COMPLETE)
	{
		if (SoundLoop>0) {SoundLoop--;}

		if (SoundLoop!=0)
		{
			try {
				SoundPlayer.play();
			} catch(Exception e) {}
		}
	}
}


public MediaSound LoadSound(int Pos)
{
	MediaSound msound = MediaManager.getSound("scratchpad:///0;pos="+Pos);

	try {
		msound.use();
	} catch (Exception e) {}

	return msound;
}


public MediaSound LoadSound(int Pos, int SubPos) {
	//Pos = (FS_Data[Pos] / 4) + SubPos;
	//return LoadSound(FS_Data[Pos]);
	// // MONGOFIX

	MediaSound s = new MediaSound();
	s.music = Gdx.audio.newMusic(Gdx.files.internal(IApplication.assetsFolder+"/"+Pos+"/"+SubPos+".mid"));
	return s;
}

// <=- <=- <=- <=- <=-


// **************************************************************************//
} // Cierra la Clase GameCanvas
// **************************************************************************//