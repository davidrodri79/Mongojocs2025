package com.mygdx.mongojocs.juegoverdad;

import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.DirectGraphics;
import com.mygdx.mongojocs.midletemu.DirectUtils;
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

public class GameCanvas extends BiosCanvas {

public GameCanvas(Game ga) { super(ga); }

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

//#ifdef SOUND_ON
final boolean deviceSound = true;		// Terminal con Sonido
//#else
//#endif

//#ifdef VIBRA_ON
  final boolean deviceVibra = true;			// Terminal con Vibracion
//#else
//#endif
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

//#ifdef LISTENER
//#endif

//#ifdef DOJA
//#endif

//#if J2ME && !LISTENER
	softKey = new String[] { ga.gameText[ga.TEXT_ACEPTAR][0], ga.gameText[ga.TEXT_ACEPTAR][0] };
//#endif

//#if DOJA || LISTENER
//#endif

//#ifdef J2ME
  	soundCreate();
//#else
//#endif
}


private static final int GRADIENTSTEPS = 16;


// -------------------
// Draw
// ===================

public void Draw(Graphics g)
{
	int height=canvasHeight;

	if (((softKey[0]!=null&&!softKey[0].equals(""))) ||
	    ((softKey[1]!=null&&!softKey[1].equals(""))))  {

//#if J2ME && !LISTENER
	    height -= scr.getFont().getHeight();
//#endif
	}

	if (showBackground) {
//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif

		// Dibujar Fondo
		int gradTmp = (canvasHeight + GRADIENTSTEPS - 1) / GRADIENTSTEPS;
		for(int count = 0; count < GRADIENTSTEPS; count++) {
			int cr = ( ( (0xFF0000 & miniScrollColor1) * (GRADIENTSTEPS - count) + (0xFF0000 & miniScrollColor2) * count ) / GRADIENTSTEPS ) & 0xFF0000;
			int cg = ( ( (0xFF00 & miniScrollColor1) * (GRADIENTSTEPS - count) + (0xFF00 & miniScrollColor2) * count ) / GRADIENTSTEPS ) & 0xFF00;
			int cb = ( ( (0xFF & miniScrollColor1) * (GRADIENTSTEPS - count) + (0xFF & miniScrollColor2) * count ) / GRADIENTSTEPS ) & 0xFF;

			scr.setColor(cr | cg | cb);
			scr.fillRect(0, gradTmp * count, canvasWidth, gradTmp);
		}

		showBackground = false;
	}

	if (showMiniScroll) {
		int cH = canvasHeight;
		
//#if 	J2ME && !LISTENER
		if ((softKey[0] != null) || (softKey[1] != null)) {
			cH -= font.getHeight();
		}
//#endif
	
		if (miniScrollImage!=null) {
			showImage(miniScrollImage, (canvasWidth - miniScrollImage.getWidth())>>1, - miniScrollY);
		}

//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, cH);
//#endif
		int imageHeight = 0;
		if (miniScrollImage!=null) imageHeight = miniScrollImage.getHeight();
		if (miniScrollText != null) {
			textDraw(miniScrollText, 0, - miniScrollY + imageHeight, 0xFFFFFF, TEXT_TOP | TEXT_HCENTER);
		}

		showMiniScroll = false;
//#ifdef J2ME
		scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif
	}


	if (ga.playShow) { ga.playShow=false; playDraw(); }

//#if J2ME && !LISTENER 
	scr.setClip(0, 0, canvasWidth, canvasHeight);
	
	if (!(ga.menuList_ON && (ga.menuListMode == ga.ML_SCROLL || ga.menuListMode == ga.ML_SCREEN))) {
		if (softKey[0]!=null && !softKey[0].equals("")) {
			textDraw(softKey[0], 1, 0, ga.SOFTKEY_COLOR, TEXT_LEFT | TEXT_BOTTOM | TEXT_OUTLINE);
		}

		if (softKey[1]!=null && !softKey[1].equals("")) {
			textDraw(softKey[1], -1, 0,  ga.SOFTKEY_COLOR, TEXT_RIGHT | TEXT_BOTTOM | TEXT_OUTLINE);
		}
	}
//#endif


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

	// OPTION
	// 0=> ACCION
	// 1=> PREGUNTA
	int gameOption;

	public void playDraw() {
		
		switch (ga.gameStatus) {

				// INTRO_NUM_PLAYERS
			case 45 :
				introNumPlayersTextField();
				break;

				// INTRO_NAME_PLAYER
			case 50 :
				introNamePlayer(null);
				break;

				// PLAY_GAME1
			case 100 :
				if (ga.sp != null)
					ga.sp.playDraw();
				break;

				// PLAY_GAME2
			case 200 :
				if (ga.cg != null)
					ga.cg.playDraw();
				break;

				// PLAY_GAME3
			case 300 :
				if (ga.pg != null && ga.pg.introWord==false)
					ga.pg.playDraw();
				//if (ga.pg != null && ga.pg.introWord==true)
                                //        ga.inputDialogInit();
				break;

				// SHOW_GAME_LOST
			case 75 :
				//showGameLost();
				break;

				// SHOW_BOTTLE_SCREEN
			case 80 :
				showBottleScreen();
				break;

				// GAME_SAVED
			//case 88:
				//gameSaved();
				//break;

				// GET_PLAYER_NAME
			case 94:
				introNamePlayer(ga.gameText[ga.TEXT_INTRO_NAME][0]);
				break;
		}
	}

	// <=- <=- <=- <=- <=

	/**
	 * Method showshowNumPlayers.
	 * pantalla CARACTERISTICAS DEL JUEGO
	 * que nos muestra el numero de jugadores
	 * del juego
	 */
	public int showNumPlayers() {

		StringBuffer sb = new StringBuffer();
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_CARACTERISTICAS][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_JUEGO][0]);
		sb.append("\\n\\n");
		sb.append(ga.gameText[ga.TEXT_CUANTOS][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_JUGADORES][0]);
		sb.append("?\\n");
		sb.append(String.valueOf(ga.numPlayers));
		sb.append("\\n--");
		ga.numPlayersTextFieldLoaded = false;
		Image img=null;
		showBackground=true;
		return miniGameScroll(ga.BGCOLOR_INI, ga.BGCOLOR_FINAL, img, sb.toString(), new String[] {ga.gameText[ga.TEXT_MODIFICAR][0],ga.gameText[ga.TEXT_SIGUIENTE][0]});
	}

public boolean showBackground = false;
private boolean showMiniScroll = false;
//private boolean putSoftKeys    = false;
public String[] softKey = new String[] {null,null};
private int miniScrollColor1;
private int miniScrollColor2;
private Image miniScrollImage;
private String[] miniScrollText;
private int miniScrollY = 0;



	/**
	 * Method introNumPlayersTextField.
	 * m�todo que pasa el flujo de la
	 * aplicaci�n a un TextField para
	 * introducir el n�mero total de
	 * jugadores
	 */
	private void introNumPlayersTextField() {
		ga.inputDialogCreate(ga.gameText[ga.TEXT_NUM_JUGADORES][0], "", 2, Game.TEXTBOX_NUMERIC);
		ga.inputDialogInit();
	}

	/**
	 * Method introNamePlayer.
	 * m�todo que pasa el flujo de la
	 * aplicaci�n a un TextField para
	 * introducir el nombre de un jugador
	 */
	private void introNamePlayer(String label) {
		if (label==null) label= ga.gameText[ga.TEXT_JUEGODELAVERDAD][0];
		ga.inputDialogCreate(label, "", 15, Game.TEXTBOX_ANY);
		ga.inputDialogInit();
	}

	/**
	 * Method showPlayerName.
	 * pantalla CARACTERISTICAS DEL JUEGO
	 * que nos muestra el nombre del
	 * jugador en curso
	 */
	public int showPlayerName(boolean update) {

		String line = "--------------------";
        StringBuffer sb = new StringBuffer();

        sb.append(" \\n");
		sb.append(ga.gameText[ga.TEXT_CARACTERISTICAS][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_JUEGO][0]);
		sb.append("\\n\\n");
		if (!update) sb.append(ga.gameText[ga.TEXT_NOMBRE][0]);
		if (!update) sb.append(" ");
		if (!update) sb.append(ga.gameText[ga.TEXT_JUGADOR][0]);
		if (!update) sb.append(" ");
		if (!update) sb.append(ga.currentNamePlayer + 1);
		if (!update) sb.append(":\\n");
		sb.append(ga.namePlayers[ga.currentNamePlayer]);
		sb.append("\\n");
		sb.append(line.substring(0,ga.namePlayers[ga.currentNamePlayer].length() + 2));
                Image img=null;
		showBackground=true;
		String[] sk = new String[] {ga.gameText[ga.TEXT_MODIFICAR][0],ga.gameText[ga.TEXT_SIGUIENTE][0]};
		if (update) sk[1]=ga.gameText[ga.TEXT_DELETE][0];
		return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),sk);

	}

	/**
	 * Method deletePlayer.
	 */
	public int deletePlayer(boolean canDelete) {

		String line = "--------------------";
	        StringBuffer sb = new StringBuffer();
	        sb.append("\\n");
	        if (canDelete) {
	        	sb.append(ga.namePlayers[ga.currentNamePlayer]);
			sb.append("\\n");
			sb.append(line.substring(0,ga.namePlayers[ga.currentNamePlayer].length() + 2));
			sb.append("\\n");
			sb.append(ga.gameText[ga.TEXT_PLAYER_DELETED][0]);
	        } else {
			sb.append(ga.gameText[ga.TEXT_PLAYER_NOT_DELETED][0]);
	        }
	        sb.append("\\n");

	        Image img=null;
		showBackground=true;
		return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),null,1000);

	}


	public int confirmDelete() {

	 	StringBuffer sb = new StringBuffer();
	        sb.append("\\n");
	        sb.append(ga.gameText[ga.TEXT_CONFIRM_DELETE][0]);
	        sb.append("\\n");

	        Image img=null;
		showBackground=true;
	        return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),
			new String[] {ga.gameText[ga.TEXT_N0][0],ga.gameText[ga.TEXT_SI][0],});

	}

       /**
        * Method showScore.
        * muestra la puntuaci�n cuando
        * estamos jugando en modo StandAlone
        */
	public int showScore(int score, int level) {
	 	StringBuffer sb = new StringBuffer();
	        sb.append("\\n");
	        sb.append(ga.gameText[ga.TEXT_SCORE][0]);
	        if (level==-1) {
	        	sb.append(" ");
	        	sb.append(ga.gameText[ga.TEXT_FINAL][0]);
		}
	        sb.append(" : ");
	        sb.append(" \\n");
	        sb.append(score);
	        if (level!=-1) {
	        	sb.append("\\n \\n \\n \\n");
	        	sb.append(ga.gameText[ga.TEXT_LEVEL][0]);
	        	sb.append(" ");
	        	sb.append(level);
		}
		sb.append("\\n");
	        Image img=null;
		showBackground=true;
	        return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),null,3000);

	}


	/**
	 * Method showPlayersName.
	 * pantalla CARACTERISTICAS DEL JUEGO que
	 * nos muestra la pantalla con todos los
	 * nombres de los jugadores
	 */
	public int showPlayersName() {

        StringBuffer sb = new StringBuffer();
        sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_CARACTERISTICAS][0]);
		sb.append( "\\n");
        sb.append(ga.gameText[ga.TEXT_JUEGO][0]);
        sb.append("\\n\\n");
		sb.append(ga.gameText[ga.TEXT_JUGADORES][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_GUIONES][0]);
		sb.append("\\n");
		for (int i = 0; i < ga.numPlayers; i++) {
			if (ga.namePlayers[i]==null||ga.namePlayers[i].equals("")) {
		   	    ga.namePlayers[i]=ga.gameText[ga.TEXT_JUGADOR][0] + " " + (i + 1);
		    }
			sb.append(ga.namePlayers[i]);
			sb.append("\\n");
		}
		Image img=null;
		showBackground=true;
		return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),
			new String[] {ga.gameText[ga.TEXT_N0][0],ga.gameText[ga.TEXT_SI][0],});
	}

	/**
	 * Method selectingFirstPlayer.
	 * Nos muestra quien es el primer
	 * jugador
	 */
	public int selectingFirstPlayer() {
		StringBuffer sb = new StringBuffer();
        sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_SELECCIONANDO][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_QUIEN][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_VA_EMPEZAR][0]);
		sb.append("\\n");
		Image img=null;
		showBackground=true;
		return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),
			new String[] {null,null},1000);
	}

	/**
	 * Method showNamePlayer.
	 * nos muestra el nombre del
	 * siguiente jugador
	 *
	 */
	public int showNextPlayer() {
		String next=ga.gameText[ga.TEXT_SIGUIENTE_MAY][0];
		String name = "";
		if (ga.firstPlayer==true) {
			ga.firstPlayer=false;
			next=ga.gameText[ga.TEXT_EMPIEZA][0];
			ga.currentPlayer = ga.RND(ga.numPlayers);
			ga.initialPlayer = ga.currentPlayer;
		}
		else {
			ga.currentPlayer=++ga.currentPlayer%(ga.numPlayers);
			if (ga.currentPlayer==ga.initialPlayer) ga.level++;
		}
		name = ga.namePlayers[ga.currentPlayer];

		StringBuffer sb = new StringBuffer();
       	sb.append("\\n\\n");
		sb.append(next);
		sb.append("\\n\\n");
		sb.append(name);
		sb.append("\\n");
		Image img=null;
		showBackground=true;
		return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),
			new String[] {null,null},1000);
	}

	/**
	 * Method showGameLost.
	 * Nos muestra el mensaje indicando
	 * que el jugador a perdido
	 */
	public void showGameLost() {
		String line = "-----------------------";
		StringBuffer sb = new StringBuffer();
		String str[] = new String[8];
		sb.append(" \\n");
		sb.append(ga.gameText[ga.TEXT_OOOHHH][0]);
		sb.append("\\n");
		sb.append(" \\n");
		sb.append(ga.namePlayers[ga.currentPlayer]);
		sb.append("\\n");
		sb.append(line.substring(0,ga.namePlayers[ga.currentPlayer].length() + 1));
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_NO_SUPERADA][0]);
		sb.append(" ");
		sb.append(ga.gameText[ga.TEXT_ASI_QUE][0]);
		sb.append("\\n");
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_A_LA_RULETA][0]);

		miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, null, sb.toString(),
				new String[] {null,null}, 3500);
	}

	int currentBottle = 1;
	/**
	 * Method showBottleScreen.
	 * nos muestra la pantalla con
	 * la botella
	 */
	public void showBottleScreen() {

//#ifdef J2ME
		Font f = Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL);
//#else
//#endif

		scr.setFont(f);

		// PREGUNTA
		canvasFill(0);
		scr.setColor(0xED145B);
		scr.fillRect(0, 0, canvasWidth>>1, canvasHeight>>1);
		// ACCION
		scr.setColor(0x00AEEF);
		scr.fillRect(canvasWidth>>1, 0, canvasWidth>>1, canvasHeight>>1);
		// PASAR TURNO
		scr.setColor(0xDFCD03);
		scr.fillRect(0, canvasHeight>>1, canvasWidth>>1, canvasHeight>>1);
		// PRENDA
		scr.setColor(0x00FF00);
		scr.fillRect(canvasWidth>>1, canvasHeight>>1, canvasWidth>>1, canvasHeight>>1);


		// PREGUNTA
//#ifndef	SMALL_GFX
		drawLabel(ga.gameText[ga.TEXT_PREGUNTA][0], (canvasWidth>>2), (canvasHeight>>2));
//#else
//#endif


		// ACCION
		drawLabel(ga.gameText[ga.TEXT_ACCION][0], canvasWidth - (canvasWidth>>2), (canvasHeight>>2));

		// PASAR TURNO
		drawLabel(ga.gameText[ga.TEXT_PASAR][0], (canvasWidth>>2), canvasHeight - (canvasHeight>>2) - (font.getHeight()>>1) );
		drawLabel(ga.gameText[ga.TEXT_TURNO][0], (canvasWidth>>2), canvasHeight - (canvasHeight>>2) + (font.getHeight()>>1) );

		// PRENDA
		drawLabel(ga.gameText[ga.TEXT_PRENDA][0],(canvasWidth>>1)+(canvasWidth>>2),(canvasHeight - (canvasHeight>>2)) );

//#ifdef NOKIAUI
		DirectGraphics dg = DirectUtils.getDirectGraphics(scr);
		Image img = ga.spritesBottle[currentBottle%6];
		byte[] bc =  ga.bootleCoordenates;
		dg.drawImage(	img,
						canvasWidth>>1,
						canvasHeight>>1,
						Graphics.VCENTER | Graphics.HCENTER, 270 - (((currentBottle / 6) + 3)&3)*90);
//#else
//#endif

	}


	/**
	 * Method drawLabel.
	 * nos escribe un texto blanco
	 * con borde negro por pantalla
	 * @param str string a escribir
	 * @param x pos. X de la pantalla
	 * @param y pos. Y de la pantalla
	 */
	private void drawLabel(String str, int xaux, int y) {
//#ifdef J2ME
		textDraw(str, xaux - (canvasWidth>>1), y - (canvasHeight>>1), 0xFFFFFF, TEXT_VCENTER | TEXT_HCENTER | TEXT_OUTLINE);
//#else
//#endif
	}


	/**
	 * Method showTextOrAction.
	 * pantalla que nos muestra el mensaje de una
	 * accion o pregunta
	 */
	public int showQuestionOrAction() {

		String whichAction;
		// ACCION
		if (gameOption==1) {
			whichAction=ga.gameText[ga.TEXT_ACCION][0];
		}
		// PREGUNTA
		else {
			whichAction=ga.gameText[ga.TEXT_PREGUNTA][0];
		}


		StringBuffer sb = new StringBuffer();
       	sb.append("\\n");
		sb.append(whichAction);
		sb.append("\\n");
		sb.append(ga.questions[gameOption + 1][ga.whichQuestion]);
		sb.append("\\n\\n");
		sb.append(ga.gameText[ga.TEXT_PULSA_TECLA][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_PARA_CONTINUAR][0]);

		Image img=null;
		showBackground=true;
		return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),
			new String[] {null,null}, -1);
	}

	/**
	 * Method showPrenda.
	 * nos muestra el mensaje de mostrar prenda
	 */
	public int showPrenda() {

		String line = "-----------------------";

		StringBuffer sb = new StringBuffer();
        sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_PRENDA][0]);
		sb.append("\\n");
		sb.append(ga.namePlayers[ga.currentPlayer]);
		sb.append("\\n");
		sb.append(line.substring(0,ga.namePlayers[ga.currentPlayer].length() + 1));
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_QUITARTE][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_UNA_PRENDA][0]);
		sb.append("\\n\\n");
		sb.append(ga.gameText[ga.TEXT_PULSA_TECLA][0]);
		sb.append("\\n");
		sb.append(ga.gameText[ga.TEXT_PARA_CONTINUAR][0]);
		

		Image img=null;
		showBackground=true;
		return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),
			new String[] {null,null},1000);

	}
        /*
        private void gameSaved() {
        	drawBackground();
                textDraw( textoBreak(ga.gameText[ga.TEXT_GAME_SAVED][0], canvasWidth, font),
                	 0, 0, 0xff0000, TEXT_HCENTER | TEXT_VCENTER);
        }
        */


        public int gameSaved() {
        	StringBuffer sb = new StringBuffer("\\n\\n\\n");
        	sb.append(ga.gameText[ga.TEXT_GAME_SAVED][0]);

        	Image img=null;
			showBackground=true;
			return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img,
			sb.toString(),
			null,1000);
        }

        public int anyGameSaved() {
        	StringBuffer sb = new StringBuffer("\\n\\n\\n");
        	sb.append(ga.gameText[ga.TEXT_ANY_GAME_SAVED][0]);
        	Image img=null;
			showBackground=true;
			return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img,
			sb.toString(),
			null,1000);
        }


	public int noSaveAllowed() {
        	StringBuffer sb = new StringBuffer("\\n\\n\\n");
        	sb.append(ga.gameText[ga.TEXT_NO_SAVE_ALLOWED][0]);
        	Image img=null;
			showBackground=true;
			return miniGameScroll(ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img,
			sb.toString(),
			null,1000);
        }

	/**
	 * Method showRanking.
	 * Nos muestra el ranking de todos
	 * los jugadores
	 */
	public int showRanking(int miniGame) {

		String[] names=ga.namePlayers;
		int[] scores=ga.score;

		if (miniGame!=-1) {
			names =new String[5];
			scores=new int[5];
			for (int i=0; i<5; i++) {
				names[i]=ga.scoreNamesMiniGames[i][miniGame];
				scores[i]=ga.scoreMiniGames[i][miniGame];
			}
		}

		String line = " ---------------";
		StringBuffer sb = new StringBuffer();
		sb.append(" \\n");
		sb.append(ga.gameText[ga.TEXT_RANKING][0]);
		sb.append("\\n============= \\n \\n");
		int j;

		for (int i = 0; i < names.length; i++) {
			if (names[i]!=null && !names[i].equals(""))  {
				j = 14 - names[i].length() - Integer.toString(scores[i]).length();
				if (j < 0) { j = 0; }
				sb.append(names[i]);
//#ifdef SMALL_GFX
//#else 
				sb.append(line.substring(0,j));
				sb.append("> ");
//#endif
				sb.append(scores[i]);
				sb.append("\\n ");
			}
		}
		Image img = null;
		showBackground = true;

		String [] softKeys=null;
		if (miniGame==-1) softKeys=new String[] {ga.gameText[ga.TEXT_MENU][0], null};

		return miniGameScroll(	ga.BGCOLOR_INI,ga.BGCOLOR_FINAL, img, sb.toString(),
					softKeys, -1);

	}
	/*
	private void drawBackground() {

		int gradTmp = (canvasHeight + GRADIENTSTEPS - 1) / GRADIENTSTEPS;

		for (int count = 0; count < GRADIENTSTEPS; count++) {
			//25
			//112
			int g =
				(35 * (GRADIENTSTEPS - count) + 57 * (count)) / GRADIENTSTEPS;
			int b =
				(132 * (GRADIENTSTEPS - count) + 255 * (count)) / GRADIENTSTEPS;
			scr.setColor((g << 8) + b);
			scr.fillRect(0, gradTmp * count, canvasWidth, gradTmp);
		}
	}
        */


public int miniGameScroll(int color1, int color2, Image banner, String texto) {
       return miniGameScroll(color1, color2, banner, texto, null);
}

public int miniGameScroll(int color1, int color2, Image banner, String texto, String[] sk) {
       return miniGameScroll(color1, color2, banner, texto, sk, -1);
}

/**
 * Method miniGameScroll.
 * nos escribe un texto blanco
 * con borde negro por pantalla
 * @param color1
 * @param color2
 * @param banner
 * @param texto
 * @param softKeys softKeys[0] => softKey de la parte inferior izquierda
 * @param softKeys softKeys[1] => softKey de la parte inferior derecha
 */
public int miniGameScroll(int color1, int color2, Image banner, String texto, String[] sk, int millis) {
	boolean cont = true;
	if (sk != null) {
		softKey[0]=sk[0]; softKey[1]=sk[1];

	} else {
		softKey[0] = null;
		softKey[1] = null;
	}

	if (softKey[0] == "") {
		softKey[0] = null;
	}

	if (softKey[1] == "") {
		softKey[1] = null;
	}
	
//#if DOJA || LISTENER
//#endif


	String text[] = _textoBreak(texto, canvasWidth, font);
	yPressed = 0;
	xPressed = 0;
	fPressed = 0;


	miniScrollImage = banner;
	miniScrollText = text;
	miniScrollY = 0;

	miniScrollColor1 = color1;
	miniScrollColor2 = color2;


	int imageHeight = 0;
	if (miniScrollImage != null) {
		imageHeight = miniScrollImage.getHeight();
	}

	int maxY = (text.length  * font.getHeight()) + imageHeight - canvasHeight;

//#if 	J2ME && !LISTENER
	if ((softKey[0] != null) || (softKey[1] != null)) {
		maxY += font.getHeight();
	}
//#endif

	if (maxY < 0) {
		maxY = 0;
	}

	//System.out.println("Bucle miniGameScroll");

	int res = 0;

	if (millis != -1) {
		millis /= 25;
	}

	while (cont) {
		int yP = yPressed;
		int xP = xPressed;
		int fP = fPressed;
		int skP = skPressed;

		xPressed -= xP;
		yPressed -= yP;
		fPressed -= fP;
		skPressed = 0;


		miniScrollY += (yP + xP) * font.getHeight();

		if (miniScrollY > maxY ) miniScrollY = maxY;
		if (miniScrollY < 0) miniScrollY = 0;


		if (fP != 0 && !(softKey[0] != null && softKey[1] != null) ) {
			cont = false;
			res = 2;
		} else {
			if ((softKey[0] != null && skP == -1) || (softKey[1] != null && skP == 1)) {
				cont = false;
				res = skP;
			} else {
				showMiniScroll = true;
				ga.playShow = false;
				showBackground = true;

				gameDraw();

				try { Thread.sleep(20); } catch(Exception e) { }
			}
		}
		if (millis == 0) {
			cont = false;
		}

		if (millis > 0) {
			millis--;
		}
	}

	ga.RNDSet();
	
	miniScrollImage = null;
	miniScrollText = null;
	miniScrollY = 0;
	softKey[0]=softKey[1]=null;

//#if DOJA || LISTENER
//#endif

	return res;
}


//#if J2ME && LISTENER
//#endif

	// **************************************************************************//
	// Final Clase GameCanvas
	// **************************************************************************//

//#ifdef DOJA
  //#include src-temp/dirjuego.sux
  //#endinclude
//#endif
};


