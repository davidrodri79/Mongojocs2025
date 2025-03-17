package com.mygdx.mongojocs.juegoverdad;// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.1 (1.3.2004)
// ===============================================


//#ifdef J2ME

import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.TextBox;
import com.mygdx.mongojocs.midletemu.TextField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

//#ifdef J2ME
public class Game extends Bios implements CommandListener
//#else
//#endif
{

// **************************************************************************//
// Inicio Clase Game
// **************************************************************************//

// -------------------------------------------
// Picar el c�digo del juego a partir de AQUI:
// ===========================================
// Juego:
// ===========================================



// *******************
// -------------------
// game - Engine
// ===================
// *******************


final static int GAME_LOGOS = 0;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
final static int GAME_PLAY = 20;

final static int MAX_PLAYERS = 20;

// nuevos estados
// pantalla k nos muestra las caracteristicas del juego
final static int GAME_MENU_MINI_GAMES = 34;
final static int LOADING_SCREEN 	  = 35;
final static int GAME_FEATURES_NUM_PLAYERS= 40;
final static int GAME_FEATURES_NAME_PLAYERS= 42;
final static int INTRO_NUM_PLAYERS  =  45;
final static int INTRO_NAME_PLAYER  =  50;
final static int MODIFY_PLAYER      =  52;
final static int SHOW_PLAYERS_NAMES =  55;
// pantalla k nos muestra: "SELECCIONANDO QUIEN VA A EMPEZAR..."
final static int SHOW_FIRST_PLAYER  =  60;
final static int SHOW_NEXT_PLAYER   =  65;
final static int PLAY_GAME1   		= 100;
final static int PLAY_GAME2   		= 200;
final static int PLAY_GAME3   		= 300;
final static int SHOW_RANKING		=  70;
final static int SAVING_GAME		=  72;
final static int SHOW_GAME_LOST	  	=  75;
final static int LOAD_SPRITES		=  77;
final static int SHOW_BOTTLE_SCREEN =  80;
final static int WHICH_ACTION		  =  83;
final static int SHOW_QUESTION_OR_ACTION = 85;
final static int SHOW_PRENDA 			= 87;
final static int GAME_SAVED 			= 88;
final static int DELETE_PLAYER 			= 89;
final static int CONFIRM_DELETE			= 90;
final static int SHOW_SCORE			= 92;
final static int SHOW_FINAL_SCORE		= 93;
final static int GET_PLAYER_NAME		= 94;
final static int LOAD_GAME		= 96;
final static int ANY_GAME_SAVED		= 97;

// TEXTOS
final static int TEXT_CARACTERISTICAS 	= 13;
final static int TEXT_JUEGO  	      	= 14;
final static int TEXT_CUANTOS  	      	= 15;
final static int TEXT_JUGADORES       	= 16;
final static int TEXT_MODIFICAR	      	= 17;
final static int TEXT_SIGUIENTE	      	= 18;
final static int TEXT_GUARDAR	      	= 19;
final static int TEXT_SALIR	      		= 20;
final static int TEXT_ACEPTAR      		= 21;
final static int TEXT_NOMBRE  	      	= 23;
final static int TEXT_JUGADOR  	      	= 24;
final static int TEXT_JUEGODELAVERDAD 	= 25;
final static int TEXT_NUM_JUGADORES   	= 26;
final static int TEXT_NOMBRE_JUGADOR  	= 27;
final static int TEXT_N0	      		= 28;
final static int TEXT_SI	      		= 29;
final static int TEXT_SELECCIONANDO   	= 30;
final static int TEXT_QUIEN 	      	= 31;
final static int TEXT_VA_EMPEZAR      	= 32;
final static int TEXT_MENU	      	 	= 33;
final static int TEXT_EMPIEZA        	= 34;
final static int TEXT_SIGUIENTE_MAY   	= 35;
final static int TEXT_GUIONES         	= 36;
final static int TEXT_OOOHHH   	      	= 37;
final static int TEXT_NO_SUPERADA     	= 38;
final static int TEXT_ASI_QUE	     	= 39;
final static int TEXT_A_LA_RULETA    	= 40;
final static int TEXT_PREGUNTA	      	= 41;
final static int TEXT_ACCION	      	= 42;
final static int TEXT_PASAR	      	  	= 43;
final static int TEXT_TURNO	      	  	= 44;
final static int TEXT_PRENDA	      	= 45;
final static int TEXT_QUITARTE        	= 46;
final static int TEXT_UNA_PRENDA      	= 47;
final static int TEXT_PULSA_TECLA     	= 48;
final static int TEXT_PARA_CONTINUAR	= 49;
final static int TEXT_RANKING      		= 50;
final static int TEXT_MINI_JUEGOS 		= 51;
final static int TEXT_MODIFY_GAME = 52;
final static int TEXT_SAVE_GAME = 53;
final static int TEXT_LOAD_GAME = 54;
final static int TEXT_SUPER_INSTRUC = 55;
final static int TEXT_CARRERA_INSTRUC = 56;
final static int TEXT_CARRERA_SELECT_PLAYER = 57;
final static int TEXT_CARRERA_UWON = 58;
final static int TEXT_CARRERA_ULOST = 59;
final static int TEXT_PREGUNTA_INSTRUC = 60;
final static int TEXT_PREGUNTA_CORRECTO = 61;
final static int TEXT_PREGUNTA_INCORRECTO = 62;
final static int TEXT_PREGUNTA_INTRO_WORD = 63;
final static int TEXT_ELSUPER = 64;
final static int TEXT_LACARRERA = 65;
final static int TEXT_LASPALABRAS = 66;
final static int TEXT_DELETE = 67;
final static int TEXT_PLAYER_DELETED     = 68;
final static int TEXT_PLAYER_NOT_DELETED = 69;
final static int TEXT_CONFIRM_DELETE = 70;
final static int TEXT_GAME_SAVED = 71;
final static int TEXT_ENTER = 72;
final static int TEXT_LEVEL = 73;
final static int TEXT_SCORE = 74;
final static int TEXT_INTRO_NAME = 75;
final static int TEXT_FINAL = 76;
final static int TEXT_ANY_GAME_SAVED = 77;
final static int TEXT_NO_SAVE_ALLOWED = 78;

final static int TIME_MINI_GAMES = 30;

final static int MAX_RS_MINIGAMES = 5;
final static int NUM_MINIGAMES    = 3;



int level=1, initialPlayer;
int scoreMiniGames[][]=new int[MAX_RS_MINIGAMES][NUM_MINIGAMES];
String scoreNamesMiniGames[][]=new String[MAX_RS_MINIGAMES][NUM_MINIGAMES];

// FIN TEXTOS

// nue
final static int NUMBER_QUESTIONS=14;
final static int NUMBER_ACTIONS = 13;

// final nuevos
final static int BGCOLOR_INI   = 0x00013E;
final static int BGCOLOR_FINAL = 0x272CFF;

final static int SOFTKEY_COLOR = 0xBDBDBF;

boolean numPlayersTextFieldLoaded=false;
public static int score[];
public Image spritesBottle[]=new Image[26];
boolean firstPlayer=true;
public static String namePlayers[];
int numPlayers=-1;

int currentPlayer = 0;
int currentNamePlayer;
int time=0;
byte[] bootleCoordenates;
// questions and action
String[][] questions;
int whichQuestion;
boolean playStandAlone=false;
// estamos modificando las opciones del juego
boolean modifyGame=false;

















int gameStatus = 0;
int gameStatusSaved;



// -------------------
// game Create
// ===================

public void gameCreate() {

}

// -------------------
// game Destroy
// ===================

public void gameDestroy()
{
}

// -------------------
// game Tick
// ===================
int ticks=0;
int vel;
int currentAngle;
int numRound=5;
int v1,v2;
int currentMenuOption;

public void gameTick()
{

//	System.out.println("GAME STATUS0= " + gameStatus);
//	if (timeToChange>0) timeToChange--;
	switch (gameStatus)
	{
	case GAME_LOGOS:
//		logosInit(2, new int[] {0xffffff,0xffffff}, 2000);
		loadGame();
		gameStatus = GAME_MENU_MAIN;
	break;


	case GAME_MENU_MAIN:
//#ifdef J2ME
		menuImg = gc.loadImage("/Caratula.png");
//#elifdef DOJA
//#endif
		gc.soundPlay(5,0);
		
		
		saveSKState(gameText[TEXT_ACEPTAR][0], gameText[TEXT_ACEPTAR][0]);
		menuInit(MENU_MAIN);
		gameStatus = GAME_PLAY;
	break;


	case GAME_PLAY:
		playCreate();
		playInit();
		RNDSet();
		if (gc.skPressed == -1) {gc.skPressed = 0; gameStatus = GAME_MENU_SECOND; break;}
		playShow = true;
		gameStatus=GAME_FEATURES_NUM_PLAYERS;
		gc.soundStop();
		break;

	case GAME_FEATURES_NUM_PLAYERS:

		currentNamePlayer=0;

		if (textBox!=null && textBox.getString()!=null && !textBox.getString().trim().equals("") && 
					Integer.parseInt(textBox.getString().trim()) > 1) {
			if (Integer.parseInt(textBox.getString().trim()) > MAX_PLAYERS)
				numPlayers = MAX_PLAYERS;
			else
				numPlayers = Integer.parseInt(textBox.getString().trim());

			textBox.setString(null);
		}
		else {
			if (numPlayers==-1) numPlayers = 2;
		}


   		int key=gc.showNumPlayers();

		if (key == -1 && !numPlayersTextFieldLoaded) {
			playShow = true;
			gameStatus=INTRO_NUM_PLAYERS;
		}

		if (key ==  1 && !numPlayersTextFieldLoaded) {
			playShow = true;
			gameStatus=GAME_FEATURES_NAME_PLAYERS;
			// resetear textBox
			textBox=null;
			// inicializar scores i Arra
			if (namePlayers==null) {
				namePlayers = new String[numPlayers];
				score = new int[numPlayers];
				for (int i=0; i<numPlayers; i++)
					score[i]=0;

			} else if (namePlayers.length != numPlayers) {
				int length=numPlayers;
				String[] npaux=namePlayers;
				int[] scaux=score;
				if (npaux.length<length) length=npaux.length;
	             namePlayers = new String[numPlayers];
	             score = new int[numPlayers];
	             		for (int i=0; i<length; i++) {
	             			namePlayers[i]=npaux[i];
					score[i]=scaux[i];
	             		}
			}
		}

		break;

	case GAME_FEATURES_NAME_PLAYERS:


		if (textBox!=null && textBox.getString()!=null &&
		    !textBox.getString().trim().equals("")) {
	             String name=textBox.getString().trim();
	             String nameAux=name;
	             int i=1;
	             while (existName(currentNamePlayer,name)) { name=nameAux+i; i++; }
		     namePlayers[currentNamePlayer]=name;
		     textBox.setString(null);
		} else {
			if (namePlayers[currentNamePlayer]==null)
			    namePlayers[currentNamePlayer]=gameText[TEXT_JUGADOR][0] + (currentNamePlayer + 1);
		}

		key=gc.showPlayerName(false);

		if (key == -1 && !numPlayersTextFieldLoaded) {
			playShow = true;
			if (modifyGame)
				gameStatus=MODIFY_PLAYER;
			else
			gameStatus=INTRO_NAME_PLAYER;
		}

		if (key ==  1 && !numPlayersTextFieldLoaded) {
			playShow = true;
			currentNamePlayer++;

			if (numPlayers <= currentNamePlayer) {
				gameStatus=SHOW_PLAYERS_NAMES;
				// reset textBox
				textBox=null;
			}
			else {
				gameStatus=GAME_FEATURES_NAME_PLAYERS;
			}
		}
		break;

	case MODIFY_PLAYER:
		key=gc.showPlayerName(true);
		if (key == -1 && !numPlayersTextFieldLoaded) {
			playShow = true;
			gameStatus=INTRO_NAME_PLAYER;
		}
		if (key == 1 && !numPlayersTextFieldLoaded) {
			playShow = true;
			gameStatus=CONFIRM_DELETE;
		}

	break;
	//case INTRO_NAME_PLAYER:
	//	playShow=true;
	//	gameStatus = GAME_FEATURES_NAME_PLAYERS;
	//	break;

	case CONFIRM_DELETE:
		key=gc.confirmDelete();
		if (key==1) {
			playShow = true;
			gameStatus =DELETE_PLAYER;
		}
		if (key==-1) {
			playShow = true;
			gameStatus=GAME_FEATURES_NAME_PLAYERS;
		}

	break;

	case DELETE_PLAYER:
		playShow=true;
		if (namePlayers.length>2) {
			key=gc.deletePlayer(true);
                        String [] auxnp = namePlayers;
                        int[] auxsc     = score;
                        numPlayers--;
                        namePlayers = new String[numPlayers];
                        score = new int[numPlayers];
                        for (int i=0; i<currentPlayer-1; i++) {
                        	namePlayers[i]=auxnp[i];
                        	score[i]=auxsc[i];
                        }

                        for (int i=currentPlayer; i<numPlayers; i++) {
                        	namePlayers[i]=auxnp[i+1];
                        	score[i]=auxsc[i+1];
                        }
		}
		else {
			key=gc.deletePlayer(false);
		}
		gameStatus=GAME_FEATURES_NAME_PLAYERS;

		break;

	case SHOW_PLAYERS_NAMES:
		key=gc.showPlayersName();
		if (key == 1) {
			playShow = true;
			if (modifyGame) {
				gameStatus=gameStatusSaved;
				modifyGame=false;
			} else {
			gameStatus=SHOW_FIRST_PLAYER;
		}
		}
		if (key == -1) {
			playShow = true;
			gameStatus=GAME_FEATURES_NUM_PLAYERS;
		}
		break;

	// pantalla k nos muestra: "SELECCIONANDO QUIEN VA A EMPEZAR..."
	case SHOW_FIRST_PLAYER:
		gc.selectingFirstPlayer();
		playShow = true;
		gameStatus=SHOW_NEXT_PLAYER;
		break;

	case SHOW_NEXT_PLAYER:
		releaseMiniGames();
		questions = null;
		gc.showNextPlayer();
		playShow = true;
		gameStatus=PLAY_GAME1*(RND(3) + 1);
		break;

	case PLAY_GAME1: case PLAY_GAME2: case PLAY_GAME3:
		if (gc.skPressed == -1) {
			gc.skPressed = 0;
			currentMenuOption=gameStatus;
	     	gameStatus = GAME_MENU_SECOND; 
	     	break; 
	    }
		playExit = 0;


	    if ( !playTick() ) {break;}


		playShow = true;
	    switch (playExit) {
	      		case 1:	// Pasar de Nivel
//#if DOJA || LISTENER
//#endif
					gc.soundPlay(6,1);
					if (sp!=null) {
						if (!playStandAlone) {
							score[currentPlayer]+=sp.scores;
							gameStatus = SHOW_RANKING;
						} else {
							gameStatus = SHOW_SCORE;
						}
					}
					if (cg!=null) {
						if (!playStandAlone) {
							score[currentPlayer]+=cg.scores;
							gameStatus = SHOW_RANKING;
						} else {
							gameStatus = SHOW_SCORE;
						}
					}
					if (pg!=null) {
						if (!playStandAlone) {
							score[currentPlayer]+=pg.scores;
							gameStatus = SHOW_RANKING;
						} else {
							gameStatus = SHOW_SCORE;
						}
					}
					System.gc();
				break;

			//case 2:	// Una vida menos
			//break;
			case 3:	// Producir Game Over
				gc.soundPlay(7,1);
				if (playStandAlone) {
					gameStatus= SHOW_FINAL_SCORE;
				} else {
					gameStatus = SHOW_GAME_LOST;
				}
				break;
			}

			playExit=0;

		break;


	case SHOW_GAME_LOST:
			System.gc();
			gc.showGameLost();
	        playShow = true;
	        gameStatus = LOAD_SPRITES;
	 	break;


	case LOAD_SPRITES:
			System.gc();

			gc.canvasFillInit(0);
			gc.canvasTextInit(gameText[TEXT_LOADING][0], 0, 0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER);
			gc.gameDraw();
		
			try {
//#ifdef J2ME


	//#ifdef NOKIAUI
				spritesBottle = gc.loadImage("/2_botella", 6);
	//#else
	//#endif


//#else
//#endif

//#ifdef J2ME
				bootleCoordenates = loadFile("/0_1_botella.cor");
//#else
//#endif

//#if DOJA || LISTENER
//#endif
			} catch (Exception e) {}

			playShow=true;
			gameStatus = SHOW_BOTTLE_SCREEN;
			vel=RND(2048)+1024;
			gc.currentBottle = ( (currentAngle>>7) % 24 );
			currentAngle = gc.currentBottle << 7;
			break;

	case SHOW_BOTTLE_SCREEN:
		vel -= (400 * FRAMETIME) >> 10;		
		if (vel < 0) vel = 0;
		currentAngle += (vel * FRAMETIME) >> 10;
		gc.currentBottle = ( (2400 + (currentAngle>>7)) % 24 );

		if ( ( (gc.currentBottle%6) == 0 ) && ( (vel - (400 * FRAMETIME) >> 10) <= 0) ) {
			gc.currentBottle = (gc.currentBottle + 1)%24;
			currentAngle += 128;
			gc.gameDraw();
		}

		if ( vel == 0 ) {
			spritesBottle = null;
			bootleCoordenates = null;
			System.gc();
			switch (gc.currentBottle/6) {
				// ACCION
				case 0:
					gc.soundPlay(0,1);
					break;

				// PRENDA
				case 1:
					gc.soundPlay(1,1);
					break;
	
				// PASAR TURNO
				case 2:
					gc.soundPlay(3,1);
					break;
	
				// PREGUNTA
				case 3:
					gc.soundPlay(2,1);
					break;
			}
			waitTime(2000);
			playShow=true;
			gameStatus = WHICH_ACTION;
		} else {
			playShow = true;
		}
		
		break;

	case WHICH_ACTION:
		switch (gc.currentBottle/6) {
			// ACCION
			case 0:
//#ifdef DOJA
//#else 
		questions = textosCreate( loadFile("/acciones.txt") );
//#endif
				gc.gameOption=1;
				gameStatus=SHOW_QUESTION_OR_ACTION;
				whichQuestion=RND(questions[2].length);
				break;

			// PRENDA
			case 1:
				gameStatus=SHOW_PRENDA;
				gc.showPrenda();
				break;

			// PASAR TURNO
			case 2:
				gameStatus=SHOW_NEXT_PLAYER;
				break;

			// PREGUNTA
			case 3:
//#ifdef DOJA
//#else 
		questions = textosCreate( loadFile("/preguntas.txt") );
//#endif
				gc.gameOption=0;
				gameStatus=SHOW_QUESTION_OR_ACTION;
				whichQuestion=RND(questions[1].length);
				break;
		}
		playShow=true;
	break;

	case SHOW_PRENDA:
		key=gc.showPrenda();
		if (key!=0) {
			playShow=true;
			gameStatus=SHOW_NEXT_PLAYER;
		}
		break;

	case SHOW_QUESTION_OR_ACTION:
		key=gc.showQuestionOrAction();
		if (key!=0) {
			playShow=true;
			gameStatus=SHOW_NEXT_PLAYER;
		}
		break;

	case SHOW_RANKING:
        key=0;
		if (!playStandAlone) {
			key=gc.showRanking(-1);
		} else {
			int game=0; int score=0;
			if (sp!=null) { game=0; score=sp.scores; }
			if (cg!=null) {	game=1; score=cg.scores; }
			if (pg!=null) {	game=2; score=pg.scores; }
			updateScoresMiniGames(game, score);
           	key=gc.showRanking(game);
        }
		if (key==-1) {
			playShow=true;
			currentMenuOption=SHOW_RANKING;
			gameStatus = GAME_MENU_SECOND;
		} else if (key==2) {
			playShow=true;
			if (!playStandAlone) {
				gameStatus=SHOW_NEXT_PLAYER;
			} else {
			   	gameStatus = GAME_MENU_MINI_GAMES;
			}
		}
		releaseMiniGames();
		break;
/*
	case GAME_PLAY+2:
		if (keyMenu == 0 && lastKeyMenu == -1) {gameStatus = GAME_MENU_SECOND; break;}

		if ( !playTick() ) {break;}

 		playRelease();

		gameStatus--;

		switch (playExit)
		{
		case 1:	// Pasar de Nivel
		break;

		//case 2:	// Una vida menos
		//break;

		case 3:	// Producir Game Over
			playDestroy();
			gameStatus = GAME_MENU_GAMEOVER;
		break;
		}

		playExit=0;
	break;
*/
	case GAME_MENU_SECOND:
		saveSKState(gameText[TEXT_ACEPTAR][0], gameText[TEXT_ACEPTAR][0]);
		menuInit(MENU_SECOND);		
		playShow=true;
		gameStatus=currentMenuOption;
		//gameStatus = GAME_PLAY+2;
	break;

	case GAME_MENU_MINI_GAMES:
		saveSKState(gameText[TEXT_ACEPTAR][0], gameText[TEXT_ACEPTAR][0]);
		menuInit(MENU_MINI_GAMES);

	break;

	case GAME_MENU_GAMEOVER:
		saveSKState(null, null);
		gc.canvasFillInit(0x000000);
		gc.canvasTextInit(gameText[TEXT_GAMEOVER][0], 0,0, 0xFFFFFF, gc.TEXT_VCENTER|gc.TEXT_HCENTER | gc.TEXT_OUTLINE);
		gc.gameDraw();
		waitTime(3000);
		restoreSKState();
		gameStatus = GAME_MENU_MAIN;
	break;

	case SAVING_GAME:
		if (playStandAlone) {
			gc.noSaveAllowed();
		} else {
			saveGame();
			gc.gameSaved();
		}
		playShow=true;
		gameStatus = GAME_MENU_SECOND;
		//gameStatus = GAME_SAVED;
	break;
	/*
	case GAME_SAVED:
		playShow=true;
		gameStatus = GAME_MENU_SECOND;
		waitTime(3000);
		break;
        */
	case SHOW_SCORE:
		playShow=true;
		level++;
		if (sp!=null) {
			gc.showScore(sp.scores,level);
			sp.playInit (sp.scores, (1024*(3+level))>>2, TIME_MINI_GAMES);
			gameStatus= PLAY_GAME1;
			saveSKState(gameText[TEXT_MENU][0], null);
		}
		if (cg!=null) {
			gc.showScore(cg.scores,level);
			cg.playInit(cg.scores, (1024*(3+level))>>2, TIME_MINI_GAMES);
			gameStatus= PLAY_GAME2;
			saveSKState(gameText[TEXT_MENU][0], null);
		}
		if (pg!=null) {
			gc.showScore(pg.scores,level);
			pg.playInit (pg.scores, 1024, TIME_MINI_GAMES);
			gameStatus= PLAY_GAME3;
			saveSKState(gameText[TEXT_MENU][0], gameText[TEXT_ENTER][0]);
	    }
        break;

	case SHOW_FINAL_SCORE:
		playShow=true;
		int game=0;
		int score=0;
		if (sp!=null) {
			game=0;
			score=sp.scores;
			gc.showScore(sp.scores,-1);
		}

		if (cg!=null) {
			game=1;
			score=cg.scores;
			gc.showScore(cg.scores,-1);
		}

		if (pg!=null) {
			game=2;
			score=pg.scores;
			gc.showScore(pg.scores,-1);
	    }

		if (newScoreRecord(game,score)) {
			gameStatus=GET_PLAYER_NAME;
    	} else {
			gameStatus=GAME_MENU_MINI_GAMES;
    	}

		//releaseMiniGames();
		break;

	case GET_PLAYER_NAME:
		playShow=true;
		//gameStatus = SHOW_RANKING;
		break;

	}
	/*
	case ANY_GAME_SAVED:
		playShow=true;
		gc.
	break;
	*/
}


public boolean existName(int currentNamePlayer, String name) {
       for (int i=0; i<currentNamePlayer; i++) {
	    if (namePlayers[i].equals(name)) return true;
       }
       return false;
}

// -------------------
// game Refresh
// ===================

public void gameRefresh()
{
/*
	switch (gameStatus)
	{
	}
*/
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// prefs - Engine
// ===================
// *******************

byte[] prefsData;

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{

        loadGame();

// Cargamos un byte[] con las ultimas prefs grabadas

	//prefsData = updatePrefs(null);	// Recuperamos byte[] almacenado la ultima vez

// Si es null es que nunca se han grabado prefs, por lo cual INICUALIZAMOS las prefs del juego

	//if (prefsData == null)
	//{
	//	prefsData = new byte[] {1,1,0};		// Inicializamos preferencias ya que NO estaban grabadas
	//}

// Actualizamos las variables del juego segun las prefs leidas / inicializadas

	//gameSound=prefsData[0]!=0;
	//gameVibra=prefsData[1]!=0;
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{

// Ponemos las varibles del juego a salvar como prefs.

	//prefsData[0]=(byte)(gameSound?1:0);
	//prefsData[1]=(byte)(gameVibra?1:0);

// Almacenamos las prefs

	//updatePrefs(prefsData);		// Almacenamos byte[]
	saveGame();
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// menu - Engine
// ===================
// *******************

static final int MENU_MAIN = 0;
static final int MENU_SECOND = 1;
static final int MENU_SCROLL_HELP = 2;
static final int MENU_SCROLL_ABOUT = 3;

static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;
static final int MENU_ACTION_MODIFY_GAME = 9;
static final int MENU_ACTION_SAVE_GAME = 10;
static final int MENU_ACTION_LOAD_GAME = 11;
static final int MENU_MINI_GAMES = 12;
static final int MENU_ACTION_MINI_GAMES = 13;

int menuType;
int menuTypeBack;

// -------------------
// menu Init
// ===================

public void menuInit(int type)
{
	menuTypeBack = menuType;
	menuType = type;

	menuExit = false;

	menuListInit(0, 0, gc.canvasWidth, gc.canvasHeight);


	switch (type)
	{
	case MENU_MAIN:
		menuListClear();
		menuListAdd(0, gameText[TEXT_PLAY], MENU_ACTION_PLAY);
		menuListAdd(0, gameText[TEXT_LOAD_GAME], MENU_ACTION_LOAD_GAME);
		menuListAdd(0, gameText[TEXT_MINI_JUEGOS], MENU_ACTION_MINI_GAMES);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_ABOUT], MENU_ACTION_SHOW_ABOUT);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_SECOND:
		menuListClear();
		menuListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
		menuListAdd(0, gameText[TEXT_MODIFY_GAME], MENU_ACTION_MODIFY_GAME);
		menuListAdd(0, gameText[TEXT_SAVE_GAME], MENU_ACTION_SAVE_GAME);
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_HELP], MENU_ACTION_SHOW_HELP);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_MINI_GAMES:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ELSUPER],		PLAY_GAME1);
		menuListAdd(0, gameText[TEXT_LACARRERA],	PLAY_GAME2);
		menuListAdd(0, gameText[TEXT_LASPALABRAS],	PLAY_GAME3);
		menuListAdd(0, gameText[TEXT_RESTART],		MENU_ACTION_RESTART);
		menuListSet_Option();
		break;

	case MENU_SCROLL_HELP:
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		menuListSet_Screen();
	break;


	case MENU_SCROLL_ABOUT:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		menuListSet_Screen();
	break;

	}


	biosStatus = 22;
}


// -------------------
// menu Action
// ===================

public void menuAction(int cmd)
{

	switch (cmd)
	{
	case -3: // Scroll ha sido cortado por usario
	case -2: // Scroll ha llegado al final
		menuInit(menuTypeBack);
	break;


	case MENU_ACTION_PLAY:		// Jugar de 0
	case MENU_ACTION_CONTINUE:	// Continuar
		gc.soundStop();
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_HELP:		// Controles...
		menuInit(MENU_SCROLL_HELP);
	break;


	case MENU_ACTION_SHOW_ABOUT:	// About...
		menuInit(MENU_SCROLL_ABOUT);
	break;


	case MENU_ACTION_RESTART:	// Restart
		gc.soundStop();
		playExit = 3;
		menuExit = true;
		gameStatus = GAME_MENU_MAIN;
	break;


	case MENU_ACTION_EXIT_GAME:	// Exit Game
		gameExit = true;
	break;


	case MENU_ACTION_SOUND_CHG:
		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) { gc.soundPlay(5,0); }
	break;


	case MENU_ACTION_VIBRA_CHG:
		gameVibra = menuListOpt() != 0;
	break;

	case MENU_ACTION_MODIFY_GAME:
		modifyGame=true;
		menuExit = true;
		playShow=true;
		gameStatusSaved=gameStatus;
		gameStatus =GAME_FEATURES_NUM_PLAYERS;
	break;

	case MENU_ACTION_SAVE_GAME:
		gameStatusSaved=gameStatus;
		gameStatus = SAVING_GAME;
		menuExit = true;
	break;


	case MENU_ACTION_LOAD_GAME:
		gc.soundStop();
		menuExit = true;
		gameStatus= LOAD_GAME;
			playShow=true;
		if (loadGame()) {
			firstPlayer=false;
	   	//	gameStatus=SHOW_NEXT_PLAYER;
		} else {
			gc.anyGameSaved();
			gameStatus=GAME_MENU_MAIN;
			//gameStatus=GAME_PLAY;
		}
	break;

	case MENU_ACTION_MINI_GAMES:
		gc.soundStop();
		menuInit(MENU_MINI_GAMES);
	break;

	case PLAY_GAME1:
		releaseMiniGames();
		playStandAlone=true;
		menuExit=true;
		playShow=true;
		level=1;
		gameStatus= PLAY_GAME1;
		break;
	case PLAY_GAME2:
		releaseMiniGames();
		playStandAlone=true;
		menuExit=true;
		playShow=true;
		level=1;
		gameStatus= PLAY_GAME2;
		break;
	case PLAY_GAME3:
		releaseMiniGames();
		playStandAlone=true;
		menuExit=true;
		playShow=true;
		level=1;
		gameStatus= PLAY_GAME3;
		break;
	}

}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// play - Engine
// ===================
// *******************

boolean playShow;
int playExit;



// -------------------
// play Create
// ===================

public void playCreate()
{
	gc.canvasFillInit(0);
	gc.playCreate_Gfx();
}


// -------------------
// play Destroy
// ===================

public void playDestroy()
{
	gc.playDestroy_Gfx();
}

// -------------------
// play Init
// ===================

public void playInit()
{
	modifyGame=false;
	namePlayers=null;
	numPlayers=-1;
	score=null;
	playStandAlone=false;
	level=1;
	gc.playInit_Gfx();
}


// -------------------
// play Release
// ===================

public void playRelease()
{
	spritesBottle = null;
	bootleCoordenates = null;
	questions = null;
	gc.playRelease_Gfx();
	System.gc();
}

// -------------------
// play Tick
// ===================

SuperGame   sp;
CarreraGame cg;
PreguntaGame	pg;
public boolean playTick()
{
//	System.out.println("GAME STATUS1= " + gameStatus);
//	System.out.println("*************************GAME_STATUS=>"+gameStatus);

	int time=TIME_MINI_GAMES;

	switch (gameStatus) {
		case (PLAY_GAME1) :
			if (sp==null) {
				sp = new SuperGame(this);
				sp.playInit(0,(1024*(3+level))>>2, TIME_MINI_GAMES);
				saveSKState(gameText[TEXT_MENU][0], null);
			} else {
				sp.playTick();
				playExit=sp.gameExit;
			}
			break;
		case (PLAY_GAME2) :
			if (cg==null) {
				cg = new CarreraGame(this);
				cg.playInit(0, (1024*(3+level))>>2, TIME_MINI_GAMES);
				saveSKState(gameText[TEXT_MENU][0], null);
			} else {
				cg.playTick();
				playExit=cg.gameExit;
			}
			break;
		case (PLAY_GAME3) :
			if (pg == null) {
				pg = new PreguntaGame(this);
				pg.playInit(0, (1024*(level))>>1, (playStandAlone?TIME_MINI_GAMES:TIME_MINI_GAMES/3));
				saveSKState(gameText[TEXT_MENU][0], gameText[TEXT_ENTER][0]);
			}
			else if (pg.introWord==false) {
				pg.playTick();
				playExit = pg.gameExit;
			} else {
				if (inputCalled == false) {
					inputDialogCreate(gameText[TEXT_PREGUNTA_INTRO_WORD][0],"", pg.disorderWord.length(), TEXTBOX_ANY);
					inputDialogInit();
				}
			}
			break;
	}



	if (playExit!=0) {return true;}

	playShow = true;
	
	return false;
}

// <=- <=- <=- <=- <=-


//#ifdef J2ME

//#ifdef LISTENER
//#endif
Display display;
public void commandAction (Command c, Displayable d)
{
	if(c.getLabel()==gameText[TEXT_GUARDAR][0]) {
		performGuardar();
	}
	
//#ifdef LISTENER
//#endif
}
//#endif

/**
 * Method performGuardar.
 * nos maneja la accion guardar de
 * los TextFields, ya sea el numero
 * de jugadores, como su nombre
 */
public void performGuardar() {
	playShow = true;
	inputCalled = false;

	switch (gameStatus) {
		case (INTRO_NUM_PLAYERS):
			gameStatus=GAME_FEATURES_NUM_PLAYERS;
			numPlayersTextFieldLoaded=true;
			break;

			case (INTRO_NAME_PLAYER):
				if (numPlayers < currentNamePlayer)
					gameStatus=SHOW_PLAYERS_NAMES;
				else
					gameStatus=GAME_FEATURES_NAME_PLAYERS;
				break;

	case (PLAY_GAME3):
		pg.introWord=false;
		pg.userWord=textBox.getString().trim();
		if (pg.userWord==null) pg.userWord="";
		break;

	case (GET_PLAYER_NAME):
		gameStatus = SHOW_RANKING;
	break;

	}
//#ifdef J2ME
	Display.getDisplay(this).setCurrent(gc);
//#endif
}

//#ifdef J2ME

public static TextBox textBox;

public final static int TEXTBOX_NUMERIC = TextField.NUMERIC;
public final static int TEXTBOX_ANY = TextField.ANY;

public boolean inputCalled = false;


public void inputDialogCreate(String title, String text, int maxSize, int constraints) {
	Command backCommand =  new Command(gameText[TEXT_GUARDAR][0],  Command.BACK, 1);

	textBox = new TextBox(title, text, maxSize + 1, constraints);

	textBox.addCommand(backCommand);
	textBox.setCommandListener(this);
}

public void inputDialogInit()
{
	Display.getDisplay(this).setCurrent(textBox);
	inputCalled = true;
	System.gc();
}

//#else
//#endif


private boolean newScoreRecord(int game,int score) {
 	for (int i=0; i<MAX_RS_MINIGAMES; i++) {
 		if (score>scoreMiniGames[i][game]) {
			return true;
		}
 	}
 	return false;
}

private void updateScoresMiniGames(int game,int score) {

    int i=0;
	while (scoreMiniGames[i][game]>score && i<MAX_RS_MINIGAMES) i++;

	for (int j=MAX_RS_MINIGAMES-1; j>i; j--) {
	        scoreMiniGames[j][game]=scoreMiniGames[j-1][game];
		scoreNamesMiniGames[j][game]=scoreNamesMiniGames[j-1][game];
 	}

 	scoreMiniGames[i][game]=score;

 	if (textBox!=null && !textBox.getString().trim().equals("")) {
		scoreNamesMiniGames[i][game]=textBox.getString().trim();
	} else {
		scoreNamesMiniGames[i][game]=gameText[TEXT_JUGADOR][0]+" "+i;
	}
	textBox=null;
}




private void saveGame() {

	try {
		byte ba[];

		// si no estamos guardando el juego recupermos
		// los valores de la �ltima partida guardada
		// si no los perderemos al llamar saveGame() desde
		// destroyApp()
		if (gameStatus!=SAVING_GAME) {
			ba = updatePrefs(null);
			
			if (ba == null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream(500);
				DataOutputStream dos = new DataOutputStream(baos);
				
				dos.writeByte((byte)(gameSound?1:0));
				dos.writeByte((byte)(gameVibra?1:0));
		
				for (int i=0; i<3; i++) {
					for (int j=0; j<5; j++) {
						dos.writeUTF(scoreNamesMiniGames[j][i]);
						dos.writeInt(scoreMiniGames[j][i]);
					}
				}
		
				dos.close();
				
				ba = baos.toByteArray();
			} else {
				ba[0] = (byte)(gameSound?1:0);
				ba[1] = (byte)(gameVibra?1:0);
			}
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(500);
			DataOutputStream dos = new DataOutputStream(baos);
			
			dos.writeByte((byte)(gameSound?1:0));
			dos.writeByte((byte)(gameVibra?1:0));
	
			for (int i=0; i<NUM_MINIGAMES; i++) {
				for (int j=0; j<MAX_RS_MINIGAMES; j++) {
					dos.writeUTF(scoreNamesMiniGames[j][i]);
					dos.writeInt(scoreMiniGames[j][i]);
				}
			}
	
			dos.writeInt(gameStatusSaved);
			dos.writeInt(numPlayers);
			dos.writeInt(currentPlayer);
			dos.writeInt(level);
	
			if (namePlayers != null && namePlayers.length > 0) { 
				for (int i=0; i<namePlayers.length; i++) {
					dos.writeUTF(namePlayers[i]);
					dos.writeInt(score[i]);
				}
			}
			
			dos.close();
			
			ba = baos.toByteArray();
		}
		
		updatePrefs(ba);

	} catch (Exception e) {}
}

private boolean loadGame() {
// Leemos datos

// -------------------
// prefs INI
// ===================
	try {
	       /*
              prefsData = new byte[] {1,1,0};		// Inicializamos preferencias ya que NO estaban grabadas
	       gameSound=prefsData[0]!=0;
	       gameVibra=prefsData[1]!=0;
               */

		byte[] data=null;
		data=updatePrefs(null);

	       	for (int i=0; i<NUM_MINIGAMES; i++) {
				for (int j=0; j<MAX_RS_MINIGAMES; j++) {
					scoreNamesMiniGames[j][i]="";
					scoreMiniGames[j][i]=0;
				}
	       	}


		if (data!=null) {

			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			DataInputStream dis = new DataInputStream(bais);

			byte sound;
			byte vibra;

			sound = dis.readByte();
			vibra = dis.readByte();

			gameSound=sound!=0;
			gameVibra=vibra!=0;

			for (int i=0; i<NUM_MINIGAMES; i++) {
	        	   for (int j=0; j<MAX_RS_MINIGAMES; j++) {
					scoreNamesMiniGames[j][i]=dis.readUTF();
					scoreMiniGames[j][i]=dis.readInt();
	           	   }
	       		}

			int status=dis.readInt();

			if (gameStatus==LOAD_GAME) {
				gameStatus=status;
			}


			gameStatusSaved=status;

			numPlayers=dis.readInt();
			currentPlayer=dis.readInt();
			level=dis.readInt();

			namePlayers = new String[numPlayers];
			score 	    = new int[numPlayers];
			for (int i=0; i<numPlayers; i++) {
				namePlayers[i]=dis.readUTF();
				score[i]=dis.readInt();
			}

			dis.close();
			return true;
		}
	}
	catch (Exception e) {}
	return false;
}


public void releaseMiniGames() {

	gc.softKey[0] = null; gc.softKey[1] = null;
//#if DOJA || LISTENER
//#endif

	if (sp!=null) { sp.releaseGame(); sp=null; }
	if (cg!=null) { cg.releaseGame(); cg=null; }
	if (pg!=null) { pg.releaseGame(); pg=null; }

	System.gc();
}

String softKeyBackup0 = null;
String softKeyBackup1 = null;


void saveSKState(String sk0, String sk1) {
	if (gc.softKey != null) {
		softKeyBackup0 = gc.softKey[0];
		softKeyBackup1 = gc.softKey[1];
	}
	
//#if J2ME && !LISTENER
	gc.softKey = new String[] {sk0, sk1};
//#endif

//#if DOJA || LISTENER
//#endif
}


void restoreSKState() {
//#if J2ME && !LISTENER
	if (gc.softKey != null) {
		gc.softKey[0] = softKeyBackup0;
		gc.softKey[1] = softKeyBackup1;
	}
//#endif

//#if DOJA || LISTENER
//#endif
}

// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};



//#ifdef DOJA
//#endif

