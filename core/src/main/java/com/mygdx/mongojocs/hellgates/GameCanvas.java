package com.mygdx.mongojocs.hellgates;


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
final boolean deviceSound = true;		// Terminal con Sonido
final boolean deviceVibra = true;		// Terminal con Vibracion
final boolean deviceLight = true;		// Terminal con opciones de Luz en LCD ON/OFF
final boolean deviceMenus = false;		// Terminal con Texto inferior para menus (CommandListener / softKeyMenu)
// ----------------------------------

// ===========================================
// *******************************************
// -*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


// *******************
// -------------------
// canvas - Engine
// ===================
// *******************

// -------------------
// canvas Init
// ===================

public void canvasInit()
{
	soundCreate();
	
	tiles = loadImage("/tiles.png");
}


// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{

	switch(ga.gameStatus)
	{		
		case Game.LEVEL_CHOOSE :
		canvasFill(0);
		textDraw(ga.gameText[13][ga.currentLevel],0,canvasWidth - 40,0xFFFFFF,TEXT_HCENTER | TEXT_BOLD | TEXT_MEDIUM);		
		break;
		
		case Game.GAME_PLAY :	
		playDraw(); 
		break;
		
		
	}

}

// <=- <=- <=- <=- <=-










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

public void showChar(rect s, int r, int g, int b)
{
	scr.setColor(r,g,b);
	scr.fillRect(s.x1 - ga.scrollX, s.y1 - ga.scrollY, s.x2 - s.x1, s.y2 - s.y1);		
}

public void playDraw()
{
	int x, y;
	
	canvasFill(0);

	drawTiledLevel();
	
	scr.setClip(0,0,canvasWidth,canvasHeight);
	
	
	if(!(ga.pl.cnt%2==0 && ga.pl.invulCount>0)) showChar(ga.pl.body,(ga.lev.rectCollide(ga.pl.body,8) ? 0 : 255),255,255);
		
	for(int i = 0; i<ga.en.length; i++)
		if(!ga.en[i].outOfWorld)
		if(ga.en[i].visible && (ga.en[i].state != enemy.EN_DEAD || ga.en[i].cnt%2==0))
			showChar(ga.en[i].body,0,128,0);
		
	for(int i = 0; i<ga.pr.length; i++)
		if(ga.pr[i] != null)
			if(ga.pr[i].visible)
				showChar(ga.pr[i].body,128,128,0);		
	
	showChar(ga.pl.damage,255,0,0);
	
	scr.setClip(0,0,canvasWidth,canvasHeight);
	scr.setColor(128,128,128);
	x=(canvasWidth-100)/2; y=10;
	scr.fillRect(x,y,100,10);
	scr.setColor(255,0,0);
	scr.fillRect(x,y,(100*ga.pl.life)/ga.pl.maximumLife(),10);	
	scr.setColor(254,187,73);
	scr.fillRect(x,y+12,(100*ga.pl.charge)/100,4);
	
	textDraw(""+ga.lives, x-20, y, 0xFFFF00, TEXT_BOLD);
	
	/*scr.setColor(0xffffff);
	scr.fillRect(ga.protX,ga.protY, 16,16);


	scr.setColor(0xff0000);
	scr.fillRect(0,canvasHeight/2, 16,16);

	scr.setColor(0x0000ff);
	scr.fillRect(canvasWidth-16,canvasHeight/2, 16,16);


	textDraw(new String[] {"uno","dos","tres"}, 0, 0, 0xff0000, TEXT_HCENTER|TEXT_VCENTER);*/
	

}

// <=- <=- <=- <=- <=-

void drawTiledLevel()
{
	int bi = ga.scrollX/24, bj = ga.scrollY/24, tilesW = 2 + canvasWidth/24, tilesH = 2 + canvasHeight/24;
	byte t;
	
	for(int j = bj; j<bj+tilesH; j++)	
		for(int i = bi; i<bi+tilesW; i++)
		
			if(i<ga.lev.sizeX && j<ga.lev.sizeY){
						
				t = ga.lev.map[j*ga.lev.sizeX + i];
					
				if(t!=0)
				if(t<64)
					showImage(tiles,(t%8)*24,(t/8)*24,24,24,24*i - ga.scrollX, 24*j - ga.scrollY);
				else
					{
						showImage(tiles,24,0,16,16,24*i + 4 - ga.scrollX, 24*j + 8 - ga.scrollY);
					}
				
			}
	
}


// My data

Image tiles;





// **************************************************************************//
// Final Clase GameCanvas
// **************************************************************************//
};