package com.mygdx.mongojocs.lma2007;
//#ifndef BUILD_ONE_CLASS

	//#ifdef REM_ADVANCED_FINANCES
		//#define REM_FINANCES_PRICESSET
		//#define REM_FINANCES_ATTENDANCE
		//#define REM_FINANCES_EMPLOYEES
	//#endif
	
	//#ifdef REM_ADVANCED_TRANSFERS
		//#define REM_TRANSFERS_FILTER
		//#define REM_TRANSFERS_SCOUT
		//#define REM_TRANSFERS_NEGOTIATIONS
		//#define REM_BANK_INFO_CURRENCY
	//#endif
	
	//#if REM_FANTASY && REM_TRANSFERS_FILTER
		//#define REM_FILTER
	//#endif


import com.mygdx.mongojocs.midletemu.Image;

import java.util.Random;

public class GameLaunch extends GameCanvas
		{
		public GameLaunch(Game ga) {super(ga);}
// ------------ NO TOCAR -----------
//#define INSERT_EXTRA_CODE
//#endif
//#ifdef INSERT_EXTRA_CODE
// ------------ NO TOCAR -----------



public void menuInit(int type, int pos)
{
//#ifdef DEBUG
	Debug.println("menuInit("+type+")");
//#endif

	biosStatusOld = biosStatus;
	biosStatus = BIOS_MENU;

	menuType = type;

	menuExit = false;

// Limpiamos todas las variables usados por formEngine
	formClear();

	switch (type)
	{
// --===--
// :: Pantallas :: SQUAD ::
// --===--

	case MENU_SQUAD_INJURED_SUBS_NEEDED:
	case MENU_SQUAD_CHANGE_NEEDED:
	case MENU_SUBSTITUTE:
	case MENU_SQUAD_SELECT_TEAM:
	case MENU_SQUAD_EDIT_NAMES:
				/*
		for(int i = 0; i < league.userTeam.playerCount;i++)
		{
			int ex = league.extendedPlayer(league.userTeam.playerIds[i]);
			System.out.println(i+": "+league.userPlayerNames[ex]+" - "+league.userPlayerStats[ex][com.mygdx.mongojocs.lma2007.League.INJURY_JOURNEYS]);
		}
		*/
		formBaseColor = FORM_RGB_INDEX_SQUAD;

		menuListTitleA = getMenuText(MENTXT_SQUAD)[0];
		
		if(menuType == MENU_SQUAD_CHANGE_NEEDED) {
					
			menuListTitleB = getMenuText(MENTXT_SUBSTITUTE_INJURED_OR_SUSPENDED_PLAYERS)[0];
			
		} else if(menuType == MENU_SQUAD_INJURED_SUBS_NEEDED) {
			
			menuListTitleB = getMenuText(MENTXT_SUBSTITUTE_INJURED_PLAYERS)[0];

		} else if(menuType == MENU_SQUAD_EDIT_NAMES) {

			menuListTitleB = getMenuText(MENTXT_EDIT_NAMES)[0];
			
		} else {
			
			menuListTitleB = playingMatch ? substitutionsLeft+" "+getMenuText(MENTXT_CHANGES_LEFT)[0] : getMenuText(MENTXT_SELECT_SQUAD)[0];
		}


		formSwapEnabled = true;

	//#ifndef REM_SQUAD_SEPARATOR
		formCampoImg = campoImg;	// Mostramos campo a la izquierda
	//#endif

		formTableItems = new int[] {
			(FONT_ORANGE <<8)	| (PRINT_HCENTER),
			0x8000	| (2<<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};


		formTableMasterCell = new int[] {2};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_PS)[0], "", getMenuText(MENTXT_NAME)[0]}, 0,0);

 		int listLength = menuType == MENU_SQUAD_INJURED_SUBS_NEEDED || menuType == MENU_SUBSTITUTE ? 16 : league.userTeam.playerCount; 
 
		for(int i = 0; i < listLength ; i++)
		{			
			int playerId = league.userTeam.playerIds[i];
			
			boolean noJuega = league.userPlayerStats[league.extendedPlayer(playerId)][league.SANCTION_JOURNEYS] > 0
			|| league.userPlayerStats[league.extendedPlayer(playerId)][league.INJURY_JOURNEYS] > 0;

			String banquillo = "";
			if (i > 10 && i<16) {banquillo = getMenuText(MENTXT_LST_RESERVE_SUB)[0] + " ";}
			else
			if (i > 15) {banquillo = getMenuText(MENTXT_LST_RESERVE_SUB)[1] + " ";}


		// Calculamos si el jugador tiene alguna tarjeta (roja a amarilla)
			boolean redcard = league.playerSanctioned(playerId);
			          boolean yellowcard = false;
			          if(!playingMatch) {
			                  yellowcard = false;
			              } else {
			              	//#ifndef REM_2DMATCH
			                  if(gameStatus == GAME_CONSOLE) {
			                //#endif
								//#ifndef REM_TEXTMATCH
			                          yellowcard = existsValueInArray(textMatchYellowCards, (short)playerId);
								//#endif
			                //#ifndef REM_2DMATCH
			                      } else {
			                          yellowcard = false;
			                          for(int j = 0; j < 11; j++) {
			                                  soccerPlayer sp = (userTeamIsA ? teamA : teamB)[j];
			                              if(sp.pid == playerId && sp.numFaults == 2) {
			                                      yellowcard = true;
			                
			                              }
			                          }
			                      }
			               //#endif
			}


			formListAdd((!noJuega?0:FONT_ORANGE <<8), new String[] {
				getMenuText(MENTXT_LST_POSITION)[league.playerGetPosition(playerId)],
				(redcard? "1":(yellowcard? "0":"")),
				banquillo + league.playerGetName(playerId)
			}, 0,0);
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_SQUAD_SELECT_TEAM_CANCEL, MENU_ACTION_SQUAD_SELECT_TEAM_ACEPT);

		int lsk = SOFTKEY_BACK;
		if (menuType == MENU_SQUAD_SELECT_TEAM || menuType == MENU_SQUAD_EDIT_NAMES) {lsk = SOFTKEY_MENU;}
		else
		if (menuType == MENU_SQUAD_CHANGE_NEEDED) {lsk = SOFTKEY_CONTINUE;}

		listenerInit(lsk, (menuType == MENU_SQUAD_EDIT_NAMES? SOFTKEY_EDIT:SOFTKEY_SELECT));
	break;


//#ifndef REM_SQUAD_TACTICS
	case MENU_SQUAD_TACTICS:

		formBaseColor = FORM_RGB_INDEX_SQUAD;

		menuListTitleA = getMenuText(MENTXT_SQUAD)[0];
		menuListTitleB = getMenuText(MENTXT_TACTICS)[0];


		formCampoImg = campoImg;	// Mostramos campo a la izquierda


		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
//		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd(0, getMenuText(MENTXT_DEFENCE), 0,0);
 		formListAdd(0, getMenuText(MENTXT_FOCUS), 0,0);
 		formListAdd(0, getMenuText(MENTXT_PASSING), 0,0);
 		formListAdd(0, getMenuText(MENTXT_ATTACKING), 0,0);
 		formListAdd(0, getMenuText(MENTXT_DISTRIBUTION), 0,0);

		formInit(FORM_TABLE, squadTactic, MENU_ACTION_MENU_EXIT, MENU_ACTION_SQUAD_TACTICS_ACEPT);

		formListDat[formListPos][0] = (FONT_ORANGE<<8);		// La opcion actualmente activa, la mostramos de color naranja

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;
//#endif


	case MENU_SQUAD_FORMATION:

		formBaseColor = FORM_RGB_INDEX_SQUAD;

		menuListTitleA = getMenuText(MENTXT_SQUAD)[0];
		menuListTitleB = getMenuText(MENTXT_FORMATION)[0];


		formCampoImg = campoImg;	// Mostramos campo a la izquierda


		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
//		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

		for(int i = 0; i < gameText[TEXT_FORMATIONS].length;i++)
		{
			formListAdd(0, new String[] {gameText[TEXT_FORMATIONS][i]}, 0,0);
		}

//#ifndef REM_BONUS_CODES
		if (bonusCodes[BONUS_MOREFORMATIONS] != 0)
		{
			formListAdd(0, new String[] {getMenuText(MENTXT_EXTRA_FORMATIONS)[0]}, 0,0);
			formListAdd(0, new String[] {getMenuText(MENTXT_EXTRA_FORMATIONS)[1]}, 0,0);

		}
//#endif
		formInit(FORM_TABLE, userFormation, MENU_ACTION_MENU_EXIT, MENU_ACTION_SQUAD_FORMATION_ACEPT);

		formListDat[formListPos][0] = (FONT_ORANGE<<8);		// La opcion actualmente activa, la mostramos de color naranja

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;


	case MENU_SQUAD_STYLE:

		formBaseColor = FORM_RGB_INDEX_SQUAD;

		menuListTitleA = getMenuText(MENTXT_SQUAD)[0];
		menuListTitleB = getMenuText(MENTXT_STYLE)[0];


		formCampoImg = campoImg;	// Mostramos campo a la izquierda


		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
//		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();
		for (int i=0 ; i<getMenuText(MENTXT_LST_SQUAD_STYLE).length ; i++)
		{
	 		formListAdd(0, getMenuText(MENTXT_LST_SQUAD_STYLE)[i], 0);
		}

		formInit(FORM_TABLE, squadStyle, MENU_ACTION_MENU_EXIT, MENU_ACTION_SQUAD_STYLE_ACEPT);

		formListDat[formListPos][0] = (FONT_ORANGE<<8);		// La opcion actualmente activa, la mostramos de color naranja

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;


	case MENU_SQUAD_INJURED:

		formBaseColor = FORM_RGB_INDEX_SQUAD;

		menuListTitleA = getMenuText(MENTXT_SQUAD)[0];
		menuListTitleB = getMenuText(MENTXT_INJURED)[0];

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};


		formTableMasterCell = new int[] {2};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_PS)[0], getMenuText(MENTXT_NAME)[0], getMenuText(MENTXT_WEEKS)[0]}, 0,0);


		for (int i=0 ; i<league.userTeam.playerCount ; i++)
		{
			int playerId = league.userTeam.playerIds[i];
			boolean noJuega = league.userPlayerStats[league.extendedPlayer(playerId)][league.INJURY_JOURNEYS] > 0;

			if (noJuega)
			{
		 		formListAdd(0, new String[] {getMenuText(MENTXT_LST_POSITION)[league.playerGetPosition(playerId)], league.playerGetName(playerId), ""+league.userPlayerStats[league.extendedPlayer(playerId)][league.INJURY_JOURNEYS]}, 0,0);
			}
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		if (formListStr.length == 1)
		{
			popupInitBack(getMenuText(MENTXT_YOU_HAVE_NO_INJURED_PLAYERS));
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();
			break;
		}

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;


	case MENU_SQUAD_SUSPENDED:

		formBaseColor = FORM_RGB_INDEX_SQUAD;

		menuListTitleA = getMenuText(MENTXT_SQUAD)[0];
		menuListTitleB = getMenuText(MENTXT_SUSPENDED)[0];

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};


		formTableMasterCell = new int[] {2};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_PS)[0], getMenuText(MENTXT_NAME)[0], getMenuText(MENTXT_WEEKS)[0]}, 0,0);

		for (int i=0 ; i<league.userTeam.playerCount ; i++)
		{
			int playerId = league.userTeam.playerIds[i];
			boolean noJuega = league.userPlayerStats[league.extendedPlayer(playerId)][league.SANCTION_JOURNEYS] > 0;

			if (noJuega)
			{
		 		formListAdd(0, new String[] {getMenuText(MENTXT_LST_POSITION)[league.playerGetPosition(playerId)], league.playerGetName(playerId), ""+league.userPlayerStats[league.extendedPlayer(playerId)][league.SANCTION_JOURNEYS]}, 0,0);
			}
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		if (formListStr.length == 1)
		{
			popupInitBack(getMenuText(MENTXT_YOU_HAVE_NO_SUSPENDED_PLAYERS));
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();
			break;
		}

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;


	case MENU_SQUAD_EDIT_NAME:

		menuListTitleA = getMenuText(MENTXT_SQUAD)[0];
		menuListTitleB = getMenuText(MENTXT_EDIT_NAME)[0];

		formInit(FORM_EDIT_NAME, pos, MENU_ACTION_BACK, MENU_ACTION_BACK);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;






// --===--
// :: Pantallas :: TRAINING ::
// --===--

	case MENU_TRAINING_LINE:

		formBaseColor = FORM_RGB_INDEX_TRAINING;

		menuListTitleA = getMenuText(MENTXT_TRAINING)[0];
		menuListTitleB = getMenuText(MENTXT_LINE)[0];


		formCampoImg = campoImg;	// Mostramos campo a la izquierda


		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

//		formTableMasterCell = 2;	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
//		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();
		for(int i = 0; i < gameText[TEXT_TRAININGLINES].length;i++)
		{
			formListAdd(0, gameText[TEXT_TRAININGLINES][i], 0);
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_BACK, MENU_ACTION_TRAININGSCHEDULE_LINE);

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;


	case MENU_TRAINING_SCHEDULE:

	//#ifndef REM_TRAINING_IMAGE
		if (trainingImg == null)
		{
			formLoading();
			trainingImg = loadImage("/training");
			trainingCor = loadFile("/training.cor");
		}
	//#endif


		formBaseColor = FORM_RGB_INDEX_TRAINING;

		menuListTitleA = getMenuText(MENTXT_TRAINING)[0];
		menuListTitleB = getMenuText(MENTXT_SCHEDULE)[0];


		formRestEnabled = true;		// Mostramos REST (imagen y resto de puntos de cada training schedule)


		formTableItems = new int[] {
			0x00000	| (FONT_WHITE <<8)		| (PRINT_LEFT),
			0x18000	| (ITEM_ARROW_LEFT<<8)	| (PRINT_HCENTER),
			0x00000	| (FONT_WHITE <<8)		| (PRINT_RIGHT),
			0x18000	| (ITEM_ARROW_RIGHT<<8)	| (PRINT_HCENTER),
		//#ifndef REM_TRAINING_IMAGE
			0x00000	| (FONT_WHITE <<8)		| (PRINT_RIGHT),
			0x00000	| (FONT_WHITE <<8)		| (PRINT_RIGHT),
			0x00000	| (FONT_WHITE <<8)		| (PRINT_RIGHT),
			0x00000	| (FONT_WHITE <<8)		| (PRINT_RIGHT),
	    //#endif
		};

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda


			formListAdd(LINE_SOMBRA, new String[] {
					" ",
					null,
					"199",	// "99" para que pille el ancho maximo. luego el "99" se elimina por ""
					null,
				//#ifndef REM_TRAINING_IMAGE
					getMenuText(MENTXT_LST_TRAINING_SCHEDULE)[0],
					getMenuText(MENTXT_LST_TRAINING_SCHEDULE)[1],
					getMenuText(MENTXT_LST_TRAINING_SCHEDULE)[2],
					getMenuText(MENTXT_LST_TRAINING_SCHEDULE)[3]
				//#endif
			}
					, 0, 0);


		for(int i = 1; i < gameText[TEXT_TRAININGS].length;i++)
		{
			formListAdd(0, new String[] {
					gameText[TEXT_TRAININGS][i]+":",
					null,
					""+trainingSchedule[trainingLine][i],
					null,
				//#ifndef REM_TRAINING_IMAGE
					""+league.allTrainings[i][1],
					""+league.allTrainings[i][2],
					""+league.allTrainings[i][3],
					""+league.allTrainings[i][4]
				//#endif
			}
					, MENU_ACTION_TRAININGSCHEDULE_CHANGE, 0);
		}		
		
		if (employees[EMPLOYEES_COACH] > 0)
		{
			formInit(FORM_TABLE, pos, MENU_ACTION_BACK, MENU_ACTION_TRAINING_SCHEDULE_AUTO);
			listenerInit(SOFTKEY_BACK, SOFTKEY_AUTO);
		}
		else
		{
			formInit(FORM_TABLE, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);
			listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
		}
		
		formListStr[0][2] = "";	// Hack para calcular el ancho de "99" como anchura maxima para esta colunma

	break;


	case MENU_TRAINING_STYLE:

		formBaseColor = FORM_RGB_INDEX_TRAINING;

		menuListTitleA = getMenuText(MENTXT_TRAINING)[0];
		menuListTitleB = getMenuText(MENTXT_STYLE)[0];

		formCampoImg = campoImg;	// Mostramos campo a la izquierda

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

//		formTableMasterCell = 2;	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
//		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();
		formListAdd(0, getMenuText(MENTXT_LST_TRAINING_STYLE)[0], 0);
		formListAdd(0, getMenuText(MENTXT_LST_TRAINING_STYLE)[1], 0);
		formListAdd(0, getMenuText(MENTXT_LST_TRAINING_STYLE)[2], 0);

		formInit(FORM_TABLE, trainingStyle, MENU_ACTION_BACK, MENU_ACTION_TRAINING_STYLE_ACEPT);

		formListDat[formListPos][0] = (FONT_ORANGE<<8);		// La opcion actualmente activa, la mostramos de color naranja

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;






// --===--
// :: Pantallas :: LAPTOP ::
// --===--

	case MENU_LAPTOP:

		formBaseColor = FORM_RGB_INDEX_LAPTOP;

		menuListTitleA = getMenuText(MENTXT_LAPTOP)[0];
		menuListTitleB = getMenuText(MENTXT_INBOX)[0];

		formTableItems = new int[] {
			0x08000	| (ITEM_MAIL<<8)		| (PRINT_HCENTER),
			0x00000 | (FONT_WHITE <<8)		| (PRINT_LEFT),
			0x18000	| (ITEM_ARROW_RIGHT<<8)	| (PRINT_HCENTER),
			0x18000	| (ITEM_PAPELERA<<8)	| (PRINT_HCENTER),
		};

		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
//		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();
		for(int i = 0; i < messageCount;i++)
		{
		//#ifdef DEBUG
			if (messageTitle[i] == null) {messageTitle[i] = "null";}
		//#endif
			formListAdd(0, new String[] {(messageAge[i]&0x800000)==0? null:"", messageTitle[i], null, null}, 0, 0);
		}

	// A�adimos lineas en blanco en el caso que tengamos menos de 5 mails
		for (int i=messageCount ; i<5 ; i++)
		{
			formListAdd(0, new String[] {"", " ", "", ""}, 0, 0);
		}

		formActionCicle = MENU_ACTION_LAPTOP_CICLE;

		formInit(FORM_TABLE, pos, MENU_ACTION_BACK, messageCount>0?MENU_ACTION_READMESSAGE:0);

		listenerInit(SOFTKEY_MENU, messageCount>0?SOFTKEY_SELECT:SOFTKEY_NONE);
	break;


	case MENU_READMESSAGE:

		formListClear();

		formListAdd(0, gameText[TEXT_WAIT]);
		formInit(FORM_LIST, pos, 0, 0);

		forceRender();

		formListClear();
		
		menuListTitleA = getMenuText(MENTXT_MESSAGE)[0];
		menuListTitleB = messageTitle[currentMessage];

		formListAdd(0, messageBody[currentMessage]);

		// Buy offer message
		
		if(false) {
					
		/*} else 
		if(messageType[currentMessage] == MSG_NEGOTIATE_ACCEPT) {
			
			formInit(FORM_LIST, pos, MENU_ACTION_BUY_COMPLETE, 0);			
			listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
			*/
//#ifndef REM_TRANSFERS_NEGOTIATIONS			
		
		} else 
		if(messageType[currentMessage] == MSG_NEGOTIATE_OFFER) {
			
			formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_LAPTOP_NEGOTIATE);			
			listenerInit(SOFTKEY_NO, SOFTKEY_NEGOTIATE);
			
//#endif

		} else if(messageType[currentMessage] == MSG_BUY_OFFER) {
			
			formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_LAPTOP_SELL);			
			listenerInit(SOFTKEY_NO, SOFTKEY_YES);
			
		} else if (messageType[currentMessage] == MSG_SHIRT_SPONSOR_OFFER) {
			
			formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_SHIRT_SPONSOR_ACCEPT);			
			listenerInit(SOFTKEY_NO, SOFTKEY_YES);
			
		} else if (messageType[currentMessage] == MSG_FENCE_SPONSOR_OFFER) {
			
			formInit(FORM_LIST, pos, MENU_ACTION_BACK, MENU_ACTION_FENCE_SPONSOR_ACCEPT);			
			listenerInit(SOFTKEY_NO, SOFTKEY_YES);
			
		} else if (messageType[currentMessage] == MSG_SPONSOR_NOTIFY) {
			
			formInit(FORM_LIST, pos, MENU_ACTION_BACK, autoManagement[AUTO_FINANCES_SPONSORS] ? MENU_ACTION_SPONSOR_SWITCH_TO_EXPERT : 0);			
			listenerInit(SOFTKEY_BACK, autoManagement[AUTO_FINANCES_SPONSORS] ? SOFTKEY_DISABLE : SOFTKEY_NONE);
			
		} else {
			
			formInit(FORM_LIST, pos, MENU_ACTION_BACK, 0);			
			listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
		}	
	break;




// --===--
// :: Pantallas :: INFO ::
// --===--

	case MENU_INFO_LEAGUE_TABLE1:

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_LEAGUE_TABLE)[0];

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
		};

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), getMenuText(MENTXT_LST_INFO_LEAGUE_TABLE1), 0,0);
 		
 		for(int i = 0; i < league.teams[league.userLeague].length; i++) {
 			
 			Team currentTeam = league.teams[league.userLeague][i]; 
 			
 			formListAdd((league.userTeam != currentTeam? 0:(FONT_ORANGE<<8)), new String[] {
 						currentTeam.name,
 						""+currentTeam.points,
 						""+currentTeam.matchesWon,
 						""+currentTeam.matchesDrew,
 						""+currentTeam.matchesLost,				
 						}
						, 0, 0);
 			
 		}
 			 	
		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;
		
		
	case MENU_INFO_LEAGUE_TABLE2:

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_LEAGUE_TABLE)[1];

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
		};

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), getMenuText(MENTXT_LST_INFO_LEAGUE_TABLE2), 0,0);


 		for(int i = 0; i < league.teams[league.userLeague].length; i++) {
 			
 			Team currentTeam = league.teams[league.userLeague][i]; 
 			
 			formListAdd((league.userTeam != currentTeam? 0:(FONT_ORANGE<<8)), new String[] {
 						currentTeam.name,
 						""+currentTeam.points,
 						""+currentTeam.goalsScored,
 						""+currentTeam.goalsReceived,
 						}
						, 0, 0);
 			
 		}

		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;
		
//#ifndef REM_LEAGUE_GRAPHIC		
	case MENU_INFO_LEAGUE_GRAPHIC:

 		if (league.currentWeek == 0)
		{
			formClear();

			popupInitBack(getMenuText(MENTXT_LEAGUE_NOT_STARTED));
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();

formBodyMode = -10; // SUPER HACK, pero mucho mucho...

			break;
		}

		formListClear();

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_LEAGUE_GRAPHIC)[0];

		formInit(FORM_GRAPHIC, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;
//#endif


	case MENU_INFO_CHAMPIONS:

	    // Si se ha terminado la liga Europea, mostramos popup informando quien la ha ganado...
		if (league.champTeams.length < 2)
		{
			popupInitBack(new String[] {getMenuText(MENTXT_EUROPEAN_CHAMPIONSHIP_OVER)[0], getMenuText(MENTXT_EUROPEAN_CHAMPIONSHIP_OVER)[1]+" "+league.champTeams[0].name});
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();
			break;
		}

		formListClear();

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_CHAMPIONS)[0];

		formTableMasterCell = new int[] {0,2};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_ORANGE <<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();
 		formListAdd(LINE_NO_CALC | LINE_SOMBRA | (FONT_ORANGE<<8), 
 				new String[] {league.playingChamp?getMenuText(MENTXT_EUROPE_STATUS)[0]:getMenuText(MENTXT_EUROPE_STATUS)[1]}
 				         , 0,0);

		for (int i=0 ; i<league.champTeams.length ; )
		{
			boolean isMyTeam = (league.userTeam.name == league.champTeams[i].name) || (league.userTeam.name == league.champTeams[i+1].name);

	 		formListAdd((isMyTeam? FONT_ORANGE<<8:0), new String[] {league.champTeams[i++].name, getMenuText(MENTXT_VS)[0], league.champTeams[i++].name}, 0,0);
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;


	case MENU_INFO_CLUB_DETAILS:
	{
		formListClear();

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_CLUB_DETAILS)[0];

		formTableItems = new int[] {
			(FONT_ORANGE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

//		formTableTitle = true;		// La primera linea hace de titulo

		formTableRender = TABLE_INFO;	// Indicamos que se use el render tipo listado de informacion (xxx: yyy)

		int totalIncome = clubIncome[0]+clubIncome[1]+clubIncome[2]+clubIncome[3];
		int totalExpenses = clubExpenses[0]+clubExpenses[1]+clubExpenses[2]+clubExpenses[3];

		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_CLUB_DETAILS2)[0]}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_CLUB)[0],	league.userTeam.name}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_MANAGER_MIN)[0],custName}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_CLUB_FUNDS)[0], ""+moneyStr(coachCash + totalIncome - totalExpenses, false) + moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_CLUB_SPONSORS_STADIUM)[0]}, 0,0);

 		 
 		formListAdd(0, new String[] {getMenuText(MENTXT_MAIN_SPONSOR)[0], sponsors[0][1] > 0?getMenuText(MENTXT_SHIRTSPONSOR_NAMES)[sponsors[0][2]]:getMenuText(MENTXT_NONE)[0]}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_ADVERTISEMENTS)[0], sponsors[1][1] > 0?getMenuText(MENTXT_FENCESPONSOR_NAMES)[sponsors[1][2]]:getMenuText(MENTXT_NONE)[0]}, 0,0);
 		
 		formListAdd(0, new String[] {getMenuText(MENTXT_CAPACITY)[0], ""+getStadiumCapacity()}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_CLUB_PERSONAL)[0]}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_STAFF)[0],	""+regla3(league.userTeam.playerCount, 100, 500)}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_PLAYERS)[0],""+league.userTeam.playerCount}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);

		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	}
	break;


//#ifndef REM_TOP11
	case MENU_INFO_TOP11:

		formListClear();

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_TOP11)[0];

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
		};

		formTableTitle = true;		// La primera linea hace de titulo

		formTableMasterCell = new int[] {2};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), getMenuText(MENTXT_LST_INFO_TOP11), 0,0);
 		for(int i = 0; i < league.topPlayers.length;i++)
 		{
 			int pid = league.topPlayers[i];
 			int exid = league.extendedPlayer(pid);
 			formListAdd(exid == -1?0:(FONT_ORANGE<<8), new String[] {""+(i+1),	getMenuText(MENTXT_LST_POSITION)[ league.playerGetPosition( pid ) ],	
 					league.playerGetName(pid),	""+league.playerGetQuality(pid)}, 0,0);
 		}

 		
 		if (league.currentWeek == 0)
		{
			formClear();

			popupInitBack(getMenuText(MENTXT_LEAGUE_NOT_STARTED));
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();
			break;
		}
 		
		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;
//#endif


//#ifndef REM_TOP_SCORERS
	case MENU_INFO_TOP_SCORERS:

		formListClear();

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_TOP_SCORERS)[0];

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)

		formTableTitle = true;		// La primera linea hace de titulo

		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), getMenuText(MENTXT_LST_INFO_TOP_SCORERS), 0,0);
 		
 		for(int i = 0; i < league.topScorers.length;i++)
 		{
 			if (league.topScorers[i] != -1)
 			{
 				int exid = league.extendedPlayer(league.topScorers[i]); 	 			
 				formListAdd(exid == -1?0:(FONT_ORANGE<<8), new String[] {""+league.playerGetName((int)league.topScorers[i]),""+league.playerGetGoals(league.topScorers[i])}, 0,0);
 			}
 		}

		if (formListStr.length <= 1)
		{
			formClear();

			popupInitBack(getMenuText(MENTXT_LEAGUE_NOT_STARTED));
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();
			break;
		}
 		
		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;
//#endif


	case MENU_INFO_MANAGER:

		formListClear();

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_INFO)[0];
		menuListTitleB = getMenuText(MENTXT_MANAGER)[0];

		formTableItems = new int[] {
			(FONT_ORANGE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

//		formTableTitle = true;		// La primera linea hace de titulo

		formTableRender = TABLE_INFO;	// Indicamos que se use el render tipo listado de informacion (xxx: yyy)

		int sum = custSkillPoints; for (int i=0 ; i<custSkillNum.length ; i++) {sum += custSkillNum[i];}

		formListClear();
		//#ifndef REM_MANAGER_CUSTOMIZATION
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8),
 					   new String[] {getMenuText(MENTXT_INFO)[0],				}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_NAME)[0],			custName}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_NATIONALY)[0],		gameText[TEXT_NATIONALITIES][custNationality]}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_PREFERRED_CLUB)[0],	
 									league.getTeamFromGlobalIdx(preferedClubId).name},
 									0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_AGE)[0],			""+(2006 - custYearBase - custCurrentAge)}, 0,0); 		
 		formListAdd(0, new String[] {" "}, 0,0);
 		//#endif
 		formListAdd(LINE_SOMBRA, new String[] {getMenuText(MENTXT_IM_SKILLS)[0]}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_MOTIVATION)[0],		custSkillNum[0]+"0%"}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_COACHING)[0],		custSkillNum[1]+"0%"}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_PLAYER_JUDGEMENT)[0],custSkillNum[2]+"0%"}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_IM_DISCIPLINE)[0],		custSkillNum[3]+"0%"}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);

		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;





//	 --===--
//	 :: Pantallas :: TRANSFERS ::
//	 --===--







	
	
//	 --===--
//	 :: Pantallas :: FINANCE ::
//	 --===--
	
	case MENU_FINANCE_BANK_INFO:
		
			
		int totalIncome = clubIncome[0]+clubIncome[1]+clubIncome[2]+clubIncome[3];
		int totalExpenses = clubExpenses[0]+clubExpenses[1]+clubExpenses[2]+clubExpenses[3];
			
		formListClear();

		formBaseColor = FORM_RGB_INDEX_FINANCE;

		menuListTitleA = getMenuText(MENTXT_FINANCE)[0];
		menuListTitleB = getMenuText(MENTXT_BANK_INFO)[0];

		formTableItems = new int[] {			
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_RIGHT),
//#ifndef REM_BANK_INFO_CURRENCY			
			//(FONT_WHITE <<8)	| (PRINT_HCENTER),
//#endif
		};

		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

		formTableTitle = false;		// La primera linea hace de titulo


		formListClear();
//#ifndef REM_BANK_INFO_CURRENCY		
		formListAdd((FONT_ORANGE <<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[0],moneyStr(getAvailableCash(),false)+moneyUnit()}, 0,0);
		formListAdd(0, new String[] {" "," "}, 0,0);
		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd(LINE_NO_CALC|LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[1]," "}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[2],moneyStr(clubIncome[0],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[3],moneyStr(clubIncome[1],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[4],moneyStr(clubIncome[2],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[5],moneyStr(clubIncome[3],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd((FONT_ORANGE <<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[6],moneyStr(totalIncome,false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd(LINE_NO_CALC|LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[7]}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[8],moneyStr(clubExpenses[0],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[9],moneyStr(clubExpenses[1],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[10],moneyStr(clubExpenses[2],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[11],moneyStr(clubExpenses[3],false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd((FONT_ORANGE <<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[12],moneyStr(totalExpenses,false)+moneyUnit()}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);
 		formListAdd(0, new String[] {" "," "}, 0,0);

 		formTableRender = TABLE_INFO;	// Indicamos que se use el render tipo listado de informacion (xxx: yyy)
//#else
		formListAdd((FONT_ORANGE <<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[0],moneyStr(coachCash + totalIncome - totalExpenses,false)}, 0,0);
		formListAdd(0, new String[] {" "}, 0,0);
		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd(LINE_NO_CALC|LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[1]}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[2],moneyStr(clubIncome[0],false)}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[3],moneyStr(clubIncome[1],false)}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[4],moneyStr(clubIncome[2],false)}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[5],moneyStr(clubIncome[3],false)}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd((FONT_ORANGE <<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[6],moneyStr(totalIncome,false)}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd(LINE_NO_CALC|LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[7]}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[8],moneyStr(clubExpenses[0],false)}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[9],moneyStr(clubExpenses[1],false)}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[10],moneyStr(clubExpenses[2],false)}, 0,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[11],moneyStr(clubExpenses[3],false)}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd((FONT_ORANGE <<8), new String[] {getMenuText(MENTXT_BANKINFO_DETAILS)[12],moneyStr(totalExpenses,false)}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0);
 		formListAdd(0, new String[] {" "}, 0,0); 		

		formTableRender = TABLE_INFO;	// Indicamos que se use el render tipo listado de informacion (xxx: yyy)
 //#endif

		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		
	break;


//#ifndef REM_FINANCES_EMPLOYEES	
	
	case MENU_FINANCE_EMPLOYEE_LIST:

		formListClear();

		formBaseColor = FORM_RGB_INDEX_FINANCE;

		menuListTitleA = getMenuText(MENTXT_FINANCE)[0];
		menuListTitleB = getMenuText(MENTXT_EMPLOYEE_LIST)[0];

		formTableItems = new int[] {			
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

		formTableTitle = true;		// La primera linea hace de titulo
		
		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		
		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_JOBS)[0],moneyUnit()}, 0,0);
 		
 		
 		for(int i = 0; i < employees.length; i++) {
 			 			
 			formListAdd((employees[i] > 0 ? FONT_ORANGE<<8 : 0),
 					new String[] {getMenuText(MENTXT_EMPLOYEE_TYPES)[i],
 					 			 (employees[i] > 0 ? moneyStr(employeeCost[i][employees[i] - 1],false) : getMenuText(MENTXT_NONE)[0])}, 								  
 								 (employees[i] > 0 ? MENU_ACTION_EMPLOYEE_INFO : MENU_ACTION_EMPLOYEE_CHOOSE),
 								 0);
 		}
 		 	 		 		 	
		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, MENU_ACTION_COMMAND);

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;

	
	case MENU_FINANCE_EMPLOYEE_CHOOSE:

		formListClear();

		formBaseColor = FORM_RGB_INDEX_FINANCE;

		menuListTitleA = getMenuText(MENTXT_FINANCE)[0];
		menuListTitleB = getMenuText(MENTXT_EMPLOYEE_HIRE)[0];

		formTableItems = new int[] {			
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_WHITE <<8)	| (PRINT_HCENTER),
		};

		formTableTitle = true;		// La primera linea hace de titulo
		
		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		
		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_QUALITY)[0],getMenuText(MENTXT_COST_PER_WEEK)[0]}, 0,0);
 		 		 		 		
 		formListAdd(0, new String[] {getMenuText(MENTXT_EMPLOYEE_QUALITIES)[0], moneyStr(employeeCost[currentChoosingEmployee][0],true)}, 0, 0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_EMPLOYEE_QUALITIES)[1], moneyStr(employeeCost[currentChoosingEmployee][1],true)}, 0, 0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_EMPLOYEE_QUALITIES)[2], moneyStr(employeeCost[currentChoosingEmployee][2],true)}, 0, 0);
 		 	 		 		 	
		formInit(FORM_TABLE, pos, MENU_ACTION_BACK, MENU_ACTION_EMPLOYEE_HIRE);

		listenerInit(SOFTKEY_BACK, SOFTKEY_ACEPT);
	break;
//#endif


	case MENU_FINANCE_SPONSORS:

		formListClear();

		formBaseColor = FORM_RGB_INDEX_FINANCE;

		menuListTitleA = getMenuText(MENTXT_FINANCE)[0];
		menuListTitleB = getMenuText(MENTXT_SPONSORS)[0];

		formTableItems = new int[] {						
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

		formTableTitle = false;		// La primera linea hace de titulo
		
		formTableRender = TABLE_INFO;	// Indicamos que se use el render tipo listado de informacion (xxx: yyy)

		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_SHIRTSPONSOR)[0]}, 0,0);
 		
 		formListAdd(0, new String[] {" "}, 0,0);
 		
 		if(sponsors[SPONSOR_SHIRTS][1] > 0) {
 		 			 			
 			formListAdd(0, new String[] {getMenuText(MENTXT_SHIRTSPONSOR_DETAILS)[0]+getMenuText(MENTXT_SHIRTSPONSOR_NAMES)[sponsors[SPONSOR_SHIRTS][2]]}, 0,0);
 			formListAdd(0, new String[] {substringReplace(getMenuText(MENTXT_SHIRTSPONSOR_DETAILS)[1], "[money]", moneyStr(sponsors[SPONSOR_SHIRTS][0],true))}, 0,0);
 			formListAdd(0, new String[] {substringReplace(getMenuText(MENTXT_SHIRTSPONSOR_DETAILS)[2], "[time]", ""+(sponsors[SPONSOR_SHIRTS][1] / 4))}, 0,0); 			
 		
 		} else {
 			
 			formListAdd(0, new String[] {getMenuText(MENTXT_SHIRTSPONSOR_DETAILS)[3]}, 0,0);
 		}
 		
 		formListAdd(0, new String[] {" "}, 0,0);
 		
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_FENCESPONSOR)[0]}, 0,0);
 		
 		formListAdd(0, new String[] {" "}, 0,0);
 		
 		if(sponsors[SPONSOR_FENCES][1] > 0) {
 			 			 			
 			formListAdd(0, new String[] {getMenuText(MENTXT_FENCESPONSOR_DETAILS)[0]+getMenuText(MENTXT_FENCESPONSOR_NAMES)[sponsors[SPONSOR_FENCES][2]]}, 0,0);
 			formListAdd(0, new String[] {substringReplace(getMenuText(MENTXT_FENCESPONSOR_DETAILS)[1], "[money]", moneyStr(sponsors[SPONSOR_FENCES][0],true))}, 0,0);
 			formListAdd(0, new String[] {substringReplace(getMenuText(MENTXT_FENCESPONSOR_DETAILS)[2], "[time]", ""+(sponsors[SPONSOR_FENCES][1]))}, 0,0); 			
 			
 		} else {
 			
 			formListAdd(0, new String[] {getMenuText(MENTXT_FENCESPONSOR_DETAILS)[3]}, 0,0);
 		}
 		 	
		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;


//#ifndef REM_FINANCES_PRICESSET	
	
	case MENU_FINANCE_PRICES_SET:
		
		if(autoManagement[AUTO_FINANCES_SETPRICES]) {
			
			league.preMatch();
			autoManageSetPrices();
		}

		formListClear();

		formBaseColor = FORM_RGB_INDEX_FINANCE;

		menuListTitleA = getMenuText(MENTXT_FINANCE)[0];
		menuListTitleB = getMenuText(MENTXT_PRICES_SET)[0];

		formTableItems = new int[] {			
			0x00000	| (FONT_WHITE<<8)			| (PRINT_LEFT),
			0x18000	| (ITEM_ARROW_LEFT<<8)		| (PRINT_HCENTER),
			0x00000	| (FONT_WHITE<<8)			| (PRINT_HCENTER),
			0x18000	| (ITEM_ARROW_RIGHT<<8)		| (PRINT_HCENTER),
		};

		formTableTitle = true;		// La primera linea hace de titulo
		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones

		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda

		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_TICKETPRICE)[0],null,moneyUnit(),null}, 0,0); 		
 		
 		formListAdd(0, new String[] {getMenuText(MENTXT_TICKET_TYPE)[0],null,""+moneyStr(ticketPrices[0],false),null}, MENU_ACTION_FINANCE_CICLE,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_TICKET_TYPE)[1],null,""+moneyStr(ticketPrices[1],false),null}, MENU_ACTION_FINANCE_CICLE,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_TICKET_TYPE)[2],null,""+moneyStr(ticketPrices[2],false),null}, MENU_ACTION_FINANCE_CICLE,0);
 		formListAdd(0, new String[] {getMenuText(MENTXT_TICKET_TYPE)[3],null,""+moneyStr(ticketPrices[3],false),null}, MENU_ACTION_FINANCE_CICLE,0);
 		 		 		 
		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;

//#endif


//#ifndef REM_FINANCES_ATTENDANCE
	
	case MENU_FINANCE_ATTENDANCE:

		if (league.currentWeek == 0)
		{
			formClear();

			popupInitBack(getMenuText(MENTXT_LEAGUE_NOT_STARTED));
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();

			formBodyMode = -10; // SUPER HACK, pero mucho mucho...

			break;
		}

		
		formListClear();

		formBaseColor = FORM_RGB_INDEX_INFO;

		menuListTitleA = getMenuText(MENTXT_FINANCE)[0];
		menuListTitleB = getMenuText(MENTXT_ATTENDANCE)[0];

		formInit(FORM_GRAPHBAR, pos, MENU_ACTION_MENU_EXIT, 0);

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
	break;

//#endif


// --===--
// :: Pantallas :: STADIUM ::
// --===--
//#ifndef REM_STADIUM
	case MENU_STADIUM:
		
		formListClear();

		formBaseColor = FORM_RGB_INDEX_STADIUM;

		menuListTitleA = getMenuText(MENTXT_STADIUM)[0];
		menuListTitleB = gameText[TEXT_BAR1+7][0];

		formStadiumEnabled = true;

		formTableItems = new int[] {			
			0x00000	| (FONT_WHITE<<8)			| (PRINT_LEFT),
			0x00000	| (FONT_WHITE<<8)			| (PRINT_HCENTER),
		};

		formTableTitle = true;		// La primera linea hace de titulo
		
		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones

//		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda

		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), getMenuText(MENTXT_STADIUM_UPGRADE), 0,0); 		
 		
 		for(int i = 0; i < getMenuText(MENTXT_STADIUM_FACILITIES).length;i++)
 		{
 			String cadena;
 			//String calidad[] = {"BAD", "POOR", "AVERAGE", "GOOD", "EXCELLENT"};
 			
 			if (i < 5) 			
 				cadena = ""+stadiumRecordValue[i][stadiumLevel[i]]; 			
 			else 			
 				cadena = getMenuText(MENTXT_STADIUM_QUALITY)[stadiumRecordValue[i][stadiumLevel[i]]];
 				 			
 			formListAdd((stadiumLevel[i] == 4 || stadiumBuildStage[i] > 0)?(FONT_ORANGE<<8):0, new String[] {getMenuText(MENTXT_STADIUM_FACILITIES)[i], cadena}, 0, 0);
 		}
 		
		formInit(FORM_TABLE, pos, MENU_ACTION_MENU_EXIT, MENU_ACTION_STADIUM_UPGRADE);

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;
//#endif












//	 --===--
//	 :: Pantallas :: TRANSFERS ::
//	 --===--

	
	case MENU_TRANSFERS_SELL:

		transfersMarkedPlayers = sellingPlayers.length;
		
		formListClear();

		formBaseColor = FORM_RGB_INDEX_TRANSFERS;

		menuListTitleA = getMenuText(MENTXT_TRANSFERS)[0];
		menuListTitleB = getMenuText(MENTXT_SELL)[0];

		formTableItems = new int[] {		
			(FONT_WHITE <<8) | (PRINT_HCENTER),
			(FONT_WHITE <<8) | (PRINT_LEFT),			
			(FONT_ORANGE <<8) | (PRINT_HCENTER),
			(FONT_WHITE <<8) | (PRINT_HCENTER),						
		};

		formTableTitle = true;		// La primera linea hace de titulo
		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {" ",getMenuText(MENTXT_NAME)[0],getMenuText(MENTXT_PS)[0],moneyUnit()}, 0,0); 		
 		 		
 		for(int i = 0; i < league.userTeam.playerCount; i++) {
 			
 			//String posStr[] = {"GK", "DF", "MF", "AT"};
 			
 			short currentId = league.userTeam.playerIds[i];
 			
 			formListAdd(0, new String[] {(isInArray(sellingPlayers, currentId) ? "<<" : ""),
 										 league.playerGetName( currentId ), 
 										 getMenuText(MENTXT_LST_POSITION)[ league.playerGetPosition( currentId ) ],  
 										 ""+moneyStr(playerCost( currentId ), false)}, 
 										 MENU_ACTION_TRANSFERS_MARK, 0);
 			
 		}
 		 		 		 
		formInit(FORM_TABLE, pos, MENU_ACTION_TRANSFERS_SELL_CONFIRM, MENU_ACTION_COMMAND);

		listenerInit(transfersMarkedPlayers == 0 ? SOFTKEY_MENU : SOFTKEY_SELL, SOFTKEY_MARK);
		
	break;	
	
	
	case MENU_TRANSFERS_TEAM_SELECT:

		formListClear();

		formBaseColor = FORM_RGB_INDEX_TRANSFERS;

		menuListTitleA = getMenuText(MENTXT_TRANSFERS)[0];
		menuListTitleB = getMenuText(MENTXT_SEARCH)[0];


		formCicleTitle = getMenuText(MENTXT_CHOOSE_LEAGUE)[0];
		formCicleStr = gameText[TEXT_LEAGUES];
		formActionCicle = MENU_ACTION_TRANSFERS_SEARCH_CICLE;
		formCiclePos = pos;


		formTableItems = new int[] {			
			(FONT_WHITE <<8) | (PRINT_LEFT),												
		};

		formTableTitle = true;		// La primera linea hace de titulo
		
		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		
		formListClear();
		
//		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {"Choose com.mygdx.mongojocs.lma2007.League"}, 0,0);
//		formListAdd(0, new String[] {"England"}, 0,0);
		
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_CHOOSE_TEAM)[0]}, 0,0); 		
 		 		 			
 		for(int i = 0; i < league.teams[formCiclePos].length; i++) {
 		
 			formListAdd(0, new String[] {league.teams[formCiclePos][i].name}, MENU_ACTION_TRANSFERS_SELECT_TEAM,0);
 		}
 			 		 		 		 		 
		formInit(FORM_TABLE, 0, MENU_ACTION_MENU_EXIT, MENU_ACTION_COMMAND);

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
		
	break;	
	
//#ifndef REM_FANTASY
	case MENU_FANTASY_PLAYER_SELECT :
		
		buySelectionListSort();
		
		transfersMarkedPlayers = buyingPlayers.length;
		
		formListClear();

		formBaseColor = FORM_RGB_INDEX_TRANSFERS;

		menuListTitleA = getMenuText(MENTXT_TRANSFERS)[0];
		menuListTitleB = getMenuText(MENTXT_BUY)[0];

		formTableItems = new int[] {		
			(FONT_WHITE <<8) | (PRINT_LEFT),
			(FONT_WHITE <<8) | (PRINT_LEFT),	
			(FONT_ORANGE <<8) | (PRINT_LEFT),
			(FONT_WHITE <<8) | (PRINT_LEFT),
			(FONT_WHITE <<8) | (PRINT_LEFT)
		};

		formTableTitle = true;		// La primera linea hace de titulo
		
		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones

		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda

		formListClear();
				
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {" ",getMenuText(MENTXT_NAME)[0],getMenuText(MENTXT_PS)[0],getMenuText(MENTXT_QA)[0],moneyUnit()}, 0,0); 		
 		 		 			
 		for(int i = 0; i < buySelectionList.length; i++) {
 		
 			short playerId = buySelectionList[i];
 			 			
 			formListAdd(0, new String[] { (isInArray(buyingPlayers, playerId) ? ">>" : ""),
 										  league.playerGetName(playerId),
 										  ""+getMenuText(MENTXT_FILTER_POSITION)[league.playerGetPosition(playerId)],
 										  ""+league.playerGetQuality(playerId),
 										  moneyStr(playerCost(playerId),false)} 										 
 										, MENU_ACTION_FANTASY_PLAYER_CONFIRM, 0);
 		
 		}
 			 		 		 		 		 
		formInit(FORM_TABLE, pos, MENU_ACTION_BACK, MENU_ACTION_COMMAND);

		listenerInit(SOFTKEY_BACK, SOFTKEY_BUY);
		
	break;
//#endif
		
	case MENU_TRANSFERS_PLAYER_SELECT:
		
		buySelectionListSort();
		
		if(buySelectionList.length > PLAYER_LIST_MAX) {
		
			short temp[] = new short[PLAYER_LIST_MAX];			
			System.arraycopy(buySelectionList, 0, temp, 0, PLAYER_LIST_MAX);			
			buySelectionList = temp;
		}
		
		transfersMarkedPlayers = 0;
		
		for(int i = 0; i < buyingPlayers.length; i++) {
		
			if(existsValueInArray(buySelectionList,buyingPlayers[i])) {
			
				transfersMarkedPlayers++;
			}
		}
				
		formListClear();

		formBaseColor = FORM_RGB_INDEX_TRANSFERS;

		menuListTitleA = getMenuText(MENTXT_TRANSFERS)[0];
		menuListTitleB = getMenuText(MENTXT_BUY)[0];

		formTableItems = new int[] {		
			(FONT_WHITE <<8) | (PRINT_LEFT),
			(FONT_WHITE <<8) | (PRINT_LEFT),	
			(FONT_ORANGE <<8) | (PRINT_LEFT),
			(FONT_WHITE <<8) | (PRINT_LEFT),
			(FONT_WHITE <<8) | (PRINT_LEFT)
		};

		formTableTitle = true;		// La primera linea hace de titulo
		
		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones

		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda

		formListClear();
				
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {" ",getMenuText(MENTXT_NAME)[0],getMenuText(MENTXT_PS)[0],getMenuText(MENTXT_QA)[0],moneyUnit()}, 0,0); 		
 		 		 			
 		for(int i = 0; i < buySelectionList.length; i++) {
 		
 			short playerId = buySelectionList[i];
 			 			
 			formListAdd(0, new String[] { (isInArray(buyingPlayers, playerId) ? ">>" : ""),
 										  league.playerGetName(playerId),
 										  ""+getMenuText(MENTXT_FILTER_POSITION)[league.playerGetPosition(playerId)],
 										  ""+league.playerGetQuality(playerId),
 										  moneyStr(playerCost(playerId),false)} 										 
 										, MENU_ACTION_TRANSFERS_MARK, 0);
 		
 		}
 			 		 		 		 		 
		formInit(FORM_TABLE, pos, MENU_ACTION_TRANSFERS_BUY_CONFIRM, MENU_ACTION_COMMAND);

		listenerInit(transfersMarkedPlayers == 0 ? SOFTKEY_BACK : SOFTKEY_BUY, SOFTKEY_MARK);
		
	break;


//#ifndef REM_TRANSFERS_FILTER	
	case MENU_TRANSFERS_FILTER_SEARCH:
								
		formListClear();

		formBaseColor = FORM_RGB_INDEX_TRANSFERS;

		menuListTitleA = getMenuText(MENTXT_TRANSFERS)[0];
		menuListTitleB = getMenuText(MENTXT_FILTERED_SEARCH)[0];

		formTableItems = new int[] {						
			0x18000	| (ITEM_ARROW_LEFT<<8)		| (PRINT_HCENTER),
			0x00000	| (FONT_WHITE<<8)			| (PRINT_LEFT),
			0x18000	| (ITEM_ARROW_RIGHT<<8)		| (PRINT_HCENTER),
		};

//		formTableTitle = true;		// La primera linea hace de titulo
		
		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones

		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda

		formListClear();
 		formListAdd(LINE_SOMBRA|LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_FILTER_DETAILS)[0],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,getMenuText(MENTXT_FILTER_LEAGUE)[filterLeague],null}, MENU_ACTION_FILTER_OPTIONS_CICLE, 0);
 		
 		formListAdd(LINE_SOMBRA|LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_FILTER_DETAILS)[1],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,getMenuText(MENTXT_FILTER_POSITION)[filterPosition],null}, MENU_ACTION_FILTER_OPTIONS_CICLE, 0);
 		
 		formListAdd(LINE_SOMBRA|LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_FILTER_DETAILS)[2],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,moneyStr(filterMaxCost,true),null}, MENU_ACTION_FILTER_OPTIONS_CICLE, 0);
 
		formInit(FORM_TABLE, 1, MENU_ACTION_BACK, MENU_ACTION_FILTERED_SEARCH);

		listenerInit(SOFTKEY_MENU, SOFTKEY_CONFIRM);
	break;
//#endif

//#ifndef REM_TRANSFERS_SCOUT

	case MENU_TRANSFERS_SCOUTS:
		
		menuAction(MENU_ACTION_SCOUT_PLAYERLIST);
		/*break;
		
		// Construct buy selection list: Players good for our team
		
		short tempList[] = new short[100];		
		int tempCount = 0;
		short playerId;
		int myTeamQuality = league.userTeam.getQuality();
		
		for(int i = 0; i < league.teams.length; i++) {
		
			for(int j = 0; j < league.teams[i].length; j++) {
				
				for(int k = 0; k < league.teams[i][j].playerCount; k++) {
				
					playerId = league.teams[i][j].playerIds[k];
					
					if(tempCount < 100 && Math.abs(league.playerGetQuality(playerId) - myTeamQuality) < 4) {
						
						tempList[tempCount] = playerId;
						tempCount++;
						
					}
				}
			}						
		}
		
		if(tempCount == 0) {
			
			// No players found
			
			popupInitInfo(null, new String[] {getMenuText(MENTXT_FILTER_NOPLAYERS)[0]});
			popupWait();
			
														
		} else {
			
			buySelectionList = new short[tempCount];
			
			System.arraycopy(tempList,0,buySelectionList,0,tempCount);
									
			menuRelease();
					
			menuInit(MENU_TRANSFERS_PLAYER_SELECT);
									
		}*/
	break;
	
//#endif	
	
//#ifndef REM_TRANSFERS_NEGOTIATIONS
	
	case MENU_TRANSFERS_NEGOTIATE:
		
		negotiateYears = messageData[currentMessage][1];
		negotiateSalary = messageData[currentMessage][2];
		negotiateBonus = messageData[currentMessage][3];
		negotiatePatience = messageData[currentMessage][4];
		
		formListClear();

		formBaseColor = FORM_RGB_INDEX_TRANSFERS;

		menuListTitleA = getMenuText(MENTXT_TRANSFERS)[0];
		menuListTitleB = getMenuText(MENTXT_NEGOTIATE)[0];

		formTableItems = new int[] {						
			0x18000	| (ITEM_ARROW_LEFT<<8)		| (PRINT_HCENTER),
			0x00000	| (FONT_WHITE<<8)			| (PRINT_LEFT),
			0x18000	| (ITEM_ARROW_RIGHT<<8)		| (PRINT_HCENTER),
		};

//		formTableTitle = true;		// La primera linea hace de titulo
		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda

		formListClear();
 		formListAdd(LINE_SOMBRA | LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_CONTRACT_DETAILS)[0],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,negotiateYears+getMenuText(MENTXT_CONTRACT_DETAILS)[1],null}, MENU_ACTION_NEGOTIATE_OPTIONS_CICLE, 0);
 		
 		formListAdd(LINE_SOMBRA | LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_CONTRACT_DETAILS)[2],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,moneyStr(negotiateSalary,true),null}, MENU_ACTION_NEGOTIATE_OPTIONS_CICLE, 0);
 		
 		formListAdd(LINE_SOMBRA | LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_CONTRACT_DETAILS)[3],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,moneyStr(negotiateBonus,true),null}, MENU_ACTION_NEGOTIATE_OPTIONS_CICLE, 0);
 		 		 		 		 
		formInit(FORM_TABLE, 1, MENU_ACTION_BACK, MENU_ACTION_NEGOTIATE_CONFIRM);

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_CONFIRM);
	break;
//#endif



//#ifndef REM_FANTASY
	case MENU_FANTASY_FILTER:
		
		formListClear();

		formBaseColor = FORM_RGB_INDEX_TRANSFERS;

		menuListTitleA = getMenuText(MENTXT_TRANSFERS)[0];
		menuListTitleB = getMenuText(MENTXT_FILTERED_SEARCH)[0];

		formTableItems = new int[] {						
			0x18000	| (ITEM_ARROW_LEFT<<8)		| (PRINT_HCENTER),
			0x00000	| (FONT_WHITE<<8)			| (PRINT_LEFT),
			0x18000	| (ITEM_ARROW_RIGHT<<8)		| (PRINT_HCENTER),
		};

//		formTableTitle = true;		// La primera linea hace de titulo
		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formActionCicle = MENU_ACTION_COMMAND;	// Accion a ejecutar cuando pulsamos pad/teclas de derecha o izquierda

		formListClear();
 		formListAdd(LINE_SOMBRA|LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_FILTER_DETAILS)[0],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,getMenuText(MENTXT_FILTER_LEAGUE)[filterLeague],null}, MENU_ACTION_FILTER_OPTIONS_CICLE, 0);
 		
 		 		 		 		
 		formListAdd(LINE_SOMBRA|LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_FILTER_DETAILS)[1],null}, 0,0); 		 		
 		formListAdd(0, new String[] {(fantasyPlayerSelecting >= 11 ? null : " "),getMenuText(MENTXT_FILTER_POSITION)[filterPosition],(fantasyPlayerSelecting >= 11 ? null : " ")}, MENU_ACTION_FILTER_OPTIONS_CICLE, 0); 		
 		
 		formListAdd(LINE_SOMBRA|LINE_NO_SELECT | (FONT_ORANGE<<8), new String[] {null,getMenuText(MENTXT_FILTER_DETAILS)[2],null}, 0,0); 		 		
 		formListAdd(0, new String[] {null,moneyStr(filterMaxCost,true),null}, MENU_ACTION_FILTER_OPTIONS_CICLE, 0);
 
		formInit(FORM_TABLE, 1, MENU_ACTION_BACK, MENU_ACTION_FANTASY_FILTER);

		listenerInit(SOFTKEY_CANCEL, SOFTKEY_CONFIRM);
	break;	
//#endif













// --===--
// :: Pantallas :: STATS ::
// --===--

	case MENU_STATS_MATCH:

		
		formBaseColor = FORM_RGB_INDEX_STATS;

		menuListTitleA = getMenuText(MENTXT_MATCH_STATS)[0];

		formListClear();

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

		formTableTitle = true;		// La primera linea hace de titulo
		formTableRender = TABLE_INFO;	// Indicamos que se use el render tipo listado de informacion (xxx: yyy)

		formListClear();
 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {" "}, 0,0);

 		
 		if (!exhibitionFlag)
 		{
		// Ha sido empate?
			if (league.userMatch[0].matchGoals == league.userMatch[1].matchGoals)
			{
		 		formListAdd(0, new String[] {getMenuText(MENTXT_YOU_X_THE_MATCH)[0]}, 0,0);
			} else {
	
			// Ha ganado el primer equipo?
				boolean win = (league.userMatch[0].matchGoals > league.userMatch[1].matchGoals);
	
			// Si somos el primer equipo, hemos ganado O si somos el segundo y el primero ha perdido, pues hemos ganado
				if ((win && league.userMatch[0].isUserTeam()) || (!win && !league.userMatch[0].isUserTeam()))
				{
			 		formListAdd(0, new String[] {getMenuText(MENTXT_YOU_X_THE_MATCH)[1]}, 0,0);
				} else {
			 		formListAdd(0, new String[] {getMenuText(MENTXT_YOU_X_THE_MATCH)[2]}, 0,0);
				}
			}
 		}
 		else formListAdd(0, new String[] {getMenuText(MENTXT_CUST_EXHIBITION_MATCH)[0]}, 0,0);
 			
		formListAdd((FONT_ORANGE<<8), new String[] {""+league.userMatch[0].matchGoals+" - "+league.userMatch[1].matchGoals}, 0,0);
		formListAdd(0, new String[] {" "}, 0,0);

		//#ifdef FEA_MATCH_REPORT
		if (exhibitionFlag) matchReport = league.matchReport();
		//{
			//System.out.println("MOSTRANDO LOS TEXTOS");
			formListAdd((FONT_WHITE<<8), reportText[0], 0,0);
			formListAdd((FONT_ORANGE<<8), matchReport);

			formListAdd(0, new String[] {" "}, 0,0);
		//}
		//#endif
		
		for(int i = 0;i < numGoal;i++)
		{
			formListAdd((FONT_ORANGE<<8), new String[] {matchGoals[i][0]+" - "+matchGoals[i][1] + " ("+matchGoalScorers[i]+")"}, 0,0);
		}
 		
		
		if (!exhibitionFlag)
		{
			// Localizamos y agregamos jugadores lesionados
			boolean minimoUno = false;
			for (int i=0 ; i<league.userTeam.playerCount ; i++)
			{
				short playerId = league.userTeam.playerIds[i];
				int noJuega = league.userPlayerStats[league.extendedPlayer(playerId)][league.INJURY_JOURNEYS] 
				                                                                      + league.userPlayerStats[league.extendedPlayer(playerId)][league.SANCTION_JOURNEYS];

				if (noJuega > 0)
				{
					if (!minimoUno)
					{
						minimoUno = true;
						formListAdd(0, new String[] {" "}, 0,0);
						formListAdd(0, getMenuText(MENTXT_PLAYERS_INJURED_OR_SUSPENDED), 0,0);
					}

					formListAdd((FONT_ORANGE<<8), new String[] {league.playerGetName(playerId)+ " ("+noJuega+" "+getMenuText(MENTXT_MATCHES)[0]+")"}, 0,0);
				}
			}
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;





	case MENU_STATS_LEAGUE:

		formBaseColor = FORM_RGB_INDEX_STATS;

		menuListTitleA = getMenuText(MENTXT_LEAGUE_STATS)[0];

		formTableMasterCell = new int[] {0,2};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_ORANGE <<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd((LINE_SOMBRA | LINE_NO_CALC) | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_WEEK)[0]+" "+(league.currentWeek)}, 0,0);

		for(int i = 0; i < league.lastLeagueMatches.length/2;i++)
		{
			boolean isMyTeam = (league.userTeam.name == league.lastLeagueMatches[i*2].name) || (league.userTeam.name == league.lastLeagueMatches[(i*2)+1].name);

	 		formListAdd((isMyTeam? FONT_ORANGE<<8:0), new String[] {
	 			league.lastLeagueMatches[i*2].name,
	 			league.lastLeagueResults[i*2] + " - " + league.lastLeagueResults[(i*2)+1],
	 			league.lastLeagueMatches[(i*2)+1].name},
	 			0, 0);
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;



	case MENU_STATS_EUROPE:

	// Si no se ha jugado ningun partido de la liga Europea, mostramos popup informandolo
		if (league.champRound == 0)
		{
			popupInitBack(getMenuText(MENTXT_EUROPEAN_CHAMPIONSHIP_NOT_STARTED));
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();
			break;
		}

	// Si se ha terminado la liga Europea, mostramos popup informando quien la ha ganado...
		if (league.lastEuropeanMatches.length < 2)
		{
			popupInitBack(new String[] {getMenuText(MENTXT_EUROPEAN_CHAMPIONSHIP_OVER)[0], getMenuText(MENTXT_EUROPEAN_CHAMPIONSHIP_OVER)[1]+" "+league.lastEuropeanMatches[0].name});
			popupRefreshOnClose = false;
			popupWait();
			popupRefreshOnClose = true;

			menuRelease();
			menuInitBack();
			break;
		}


		formBaseColor = FORM_RGB_INDEX_STATS;

		menuListTitleA = getMenuText(MENTXT_EUROPE_STATS)[0];

		formTableMasterCell = new int[] {0,2};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre

		formTableItems = new int[] {
			(FONT_WHITE <<8)	| (PRINT_LEFT),
			(FONT_ORANGE <<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd((LINE_SOMBRA | LINE_NO_CALC) | (FONT_ORANGE<<8), getMenuText(MENTXT_STATS), 0,0);

		for(int i = 0; i < league.lastEuropeanMatches.length/2;i++)
		{
			boolean isMyTeam = (league.userTeam.name == league.lastEuropeanMatches[i*2].name) || (league.userTeam.name == league.lastEuropeanMatches[(i*2)+1].name);

	 		formListAdd((isMyTeam? FONT_ORANGE<<8:0), new String[] {
	 			league.lastEuropeanMatches[i*2].name,
	 			league.lastEuropeanResults[i*2] + " - "+league.lastEuropeanResults[(i*2)+1],
	 			league.lastEuropeanMatches[(i*2)+1].name}, 0,0);
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;





//#ifndef REM_CALENDAR
	case MENU_CALENDAR_MATCHES:

		menuListTitleA = gameText[TEXT_BAR1][8];
		menuListTitleB = gameText[TEXT_BAR1+option+1][0];
		
		formCicleTitle = getMenuText(MENTXT_ADVANCE)[0];
		formCicleStr = getMenuText(MENTXT_NEXT_MATCH);

		formActionCicle = MENU_ACTION_CALENDAR_MATCHES_CICLE;

		formInit(FORM_CALENDAR, 0, MENU_ACTION_BACK, MENU_ACTION_CALENDAR_MATCHES_ACCEPT);

		listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
	break;
//#endif



	case MENU_PREMATCH:	

		if (league.journeyType == League.LEAGUEJOURNEY)
			menuListTitleA = getMenuText(MENTXT_WEEK)[0]+" "+(league.currentWeek+1);
		else
			menuListTitleA = getMenuText(MENTXT_EUROPEAN_CHAMPIONSHIP)[0];

		formCicleTitle = getMenuText(MENTXT_VISUALIZATION)[0];
		
		//#ifndef REM_2DMATCH
		//#ifndef REM_TEXTMATCH
		formCicleStr = getMenuText(MENTXT_VISUALIZATION_OPTS);
		//#endif
		//#endif
		
		//#ifndef REM_2DMATCH
		//#ifdef REM_TEXTMATCH
		//#endif
		//#endif
		
		//#ifdef REM_2DMATCH
		//#endif
		
		//#ifdef REM_2DMATCH
		//#endif
		
		formActionCicle = MENU_ACTION_PREMATCH_CICLE;

		formListClear();

		formInit(FORM_VERSUS, 0, MENU_ACTION_BACK, MENU_ACTION_MATCH);		

		listenerInit(SOFTKEY_BACK, SOFTKEY_CONTINUE);
	break;


	case MENU_PLAY_MATCH_RESULT:	


//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Despues de liberar el Match");
//#endif


	//#ifndef REM_FOOTBALL_ONE
		boolean footballOne = exhibitionFlag?false:league.footballOneMatch();
		if (footballOne)
		{
			formLoading();

			if (fOneMainImg == null)	fOneMainImg = loadImage("/fOneMain");
			if (fOneLogoImg == null)	fOneLogoImg = loadImage("/fOneLogo");
		}
	//#endif

		if (!exhibitionFlag)
		{
			if (league.lastJourneyType == League.LEAGUEJOURNEY)
				menuListTitleA = getMenuText(MENTXT_WEEK)[0]+" "+(league.currentWeek);
			else
				menuListTitleA = getMenuText(MENTXT_EUROPEAN_CHAMPIONSHIP)[0];
			menuListTitleB = null;
		}
		else
		{
			menuListTitleB = getMenuText(MENTXT_CUST_EXHIBITION_MATCH)[0];			
		}
		
			
		formCicleTitle = getMenuText(MENTXT_STATS)[0];

		if (exhibitionFlag)
		{
			formCicleStr = new String[] {getMenuText(MENTXT_STATS_OPTS)[0], getMenuText(MENTXT_STATS_OPTS)[1]};
		} else {
			formCicleStr = getMenuText(MENTXT_STATS_OPTS);
		}

		formActionCicle = MENU_ACTION_PLAY_MATCH_RESULT_CICLE;

		formVersusResult = true;

		formListClear();

	//#ifndef REM_FOOTBALL_ONE
		if (!footballOne)
		{
	//#endif
			formInit(FORM_VERSUS, 0, MENU_ACTION_NONE, MENU_ACTION_PLAY_MATCH_RESULT_ACCEPT);
	//#ifndef REM_FOOTBALL_ONE
		} else{
			menuListTitleA = menuListTitleB = null;
			formListClear();
			formListAdd(0, new String[] {league.f1Title, league.f1Body});
			formInit(FORM_FOOTBALL_ONE, 0, MENU_ACTION_NONE, MENU_ACTION_PLAY_MATCH_RESULT_ACCEPT);
		}
	//#endif

		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);
	break;




/*
	case MENU_FOO:

		formListClear();

		menuListTitleA = "LMA 2007";
		menuListTitleB = "Work in progress";

		formListAdd(0, "TBD", 0);
		formInit(FORM_LIST, 0, MENU_ACTION_BACK, 0);		

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
*/










// --===--
// :: Pantallas :: MENUS genericos antes de jugar ::
// --===--

	case MENU_MAIN:

		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_CUST_MAIN_MENU);

		formListClear();
 		formListAdd((SOFTKEY_SELECT<<16), gameText[TEXT_PLAY], MENU_ACTION_NEW_MENU | MENU_PLAY<<16);
 		formListAdd((SOFTKEY_SELECT<<16), gameText[TEXT_EXHIBITION], MENU_ACTION_NEW_MENU | MENU_EXHI_LEAGUE_TEAM_A<<16 /*MENU_ACTION_EXHIBITION*/);
		formListAdd((SOFTKEY_SELECT<<16), gameText[TEXT_OPTIONS], MENU_ACTION_NEW_MENU | MENU_OPTIONS<<16);
		formListAdd((SOFTKEY_SELECT<<16), gameText[TEXT_HELP], MENU_ACTION_NEW_MENU | MENU_HELP<<16);
		formListAdd((SOFTKEY_READ<<16),   gameText[TEXT_ABOUT], MENU_ACTION_NEW_MENU | MENU_SCROLL_ABOUT<<16);
		
		formListAdd((SOFTKEY_SELECT<<16), gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, 0);

		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);
	break;


	case MENU_PLAY:


		option = subOption = cursorY = 0;		// HACK


		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_CUST_PLAY_GAME);

		formListClear();
		if (playgameStarted)
		{
 			formListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_MENU_EXIT);
		}
		formListAdd(0, gameText[TEXT_NEWGAME], MENU_ACTION_NEW_GAME);
//#ifndef REM_FANTASY
		formListAdd(0, getMenuText(MENTXT_CUST_FANTASY_TEAM), MENU_ACTION_NEW_GAME_FANTASY);
//#endif
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;


	case MENU_CUST_DIFFICULT:

		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_CUST_DIFFICULTY);

		formListClear();
 		formListAdd(0, getMenuText(MENTXT_CUST_NORMAL), MENU_ACTION_CUSTOMIZATION);
		formListAdd(0, getMenuText(MENTXT_CUST_EXPERT), MENU_ACTION_CUSTOMIZATION);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;


	case MENU_CUST_LEAGUE_TEAM:
	case MENU_EXHI_LEAGUE_TEAM_A:
	case MENU_EXHI_LEAGUE_TEAM_B:

		menuListTitleB = custName;

		formTitleStr = new String[] {getMenuText(MENTXT_CUST_SELECT_TEAM)[0]};

		switch (type)
		{
		case MENU_CUST_LEAGUE_TEAM:
			xLeague = custLeague;
			xTeam = custTeam;
		break;
		case MENU_EXHI_LEAGUE_TEAM_A:
			menuListTitleB = getMenuText(MENTXT_CUST_EXHIBITION_MATCH)[0];
			xLeague = exhiLeagueA;
			xTeam = exhiTeamA;
			formTitleStr[0] += " 1";
		break;
		case MENU_EXHI_LEAGUE_TEAM_B:
			menuListTitleB = getMenuText(MENTXT_CUST_EXHIBITION_MATCH)[0];
			xLeague = exhiLeagueB;
			xTeam = exhiTeamB;
			formTitleStr[0] += " 2";

		// Controlamos que el primer equipo seleccionado no aparezca en el listado del segundo
			if (xLeague == exhiLeagueA && xTeam == exhiTeamA && ++xTeam >= league.teams[xLeague].length) {xTeam = 0;}
		break;
		}


	// cargamos escudo de la opcion seleccionada
		//#ifndef REM_TEAMSHIELDS
		formShieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.teams[xLeague][xTeam].flagColor);
		//#endif
		
		formListClear();

	// Cargamos ligas
		formListAdd(0, gameText[TEXT_LEAGUES], MENU_ACTION_CUSTOMIZATION, xLeague);

	// Cargamos equipos de la liga 'xLeague'
		String[] str = new String[league.teams[xLeague].length];
		if (xTeam >= str.length) {xTeam = 0;}
		for (int i=0 ; i<str.length ; i++) {str[i] = league.teams[xLeague][i].name;}
		formListAdd(0, str, MENU_ACTION_CUSTOMIZATION, xTeam);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_CUST_LEAGUE_TEAM_ACCEPT);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;


//#ifndef REM_MANAGER_CUSTOMIZATION
	case MENU_CUST_MANAGER:

		menuListTitleB = custName;//getMenuText(MENTXT_MANAGER)[0];

		formTitleStr = getMenuText(MENTXT_CUST_CUSTOMIZATION);

		formListClear();
 		formListAdd(0, getMenuText(MENTXT_CUST_INSERT_NAME), MENU_ACTION_NEW_MENU | MENU_CUST_EDIT_NAME<<16);
		formListAdd(0, getMenuText(MENTXT_CUST_NATIONALITY), MENU_ACTION_NEW_MENU | MENU_CUST_NATIONALITY<<16);
		formListAdd(0, getMenuText(MENTXT_CUST_AGE), MENU_ACTION_NEW_MENU | MENU_CUST_AGE<<16);
		formListAdd(0, getMenuText(MENTXT_CUST_PREFERRED_CLUB), MENU_ACTION_NEW_MENU | MENU_CUST_PREFERRED_CLUB<<16);
		formListAdd(0, getMenuText(MENTXT_CUST_NEXT), MENU_ACTION_NEW_MENU | MENU_CUST_SKILLS<<16);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;


	case MENU_CUST_EDIT_NAME:

		menuListTitleB = custName;

		formInit(FORM_EDIT_NAME, pos, MENU_ACTION_BACK, MENU_ACTION_BACK);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;


	case MENU_CUST_NATIONALITY:

		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_CUST_NATIONALITY);

		formListClear();
 		formListAdd(0, gameText[TEXT_NATIONALITIES], MENU_ACTION_CUSTOMIZATION, custNationality);		// TBD: poner los textos correctos de nacionalidad

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_BACK);

		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);
	break;


	case MENU_CUST_AGE:

		menuListTitleB = custName;

		formTitleStr = new String[] {getMenuText(MENTXT_CUST_AGE)[0]+": "+ (2006 - custYearBase - custCurrentAge)};

		String[] yearsStr = new String[custYearDisp];
		for (int i=0 ; i<custYearDisp ; i++) {yearsStr[i] = ""+(custYearBase+i);}

		formListClear();
 		formListAdd(0, gameText[TEXT_MONTH],	MENU_ACTION_CUSTOMIZATION,	custCurrentMonth);
 		formListAdd(0, yearsStr,				MENU_ACTION_CUSTOMIZATION,	custCurrentAge);

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_BACK);

		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);
	break;


	case MENU_CUST_PREFERRED_CLUB:

		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_CUST_PREFERRED_CLUB);

		formListClear();

	// Cargamos ligas
		formListAdd(0, gameText[TEXT_LEAGUES], MENU_ACTION_CUSTOMIZATION, custPreferredClubLeague);

	// Cargamos equipos de la liga 'custPreferredClubLeague'
		str = new String[league.teams[custPreferredClubLeague].length];
		if (custPreferredClubTeam >= str.length) {custPreferredClubTeam = 0;}
		for (int i=0 ; i<str.length ; i++) {str[i] = league.teams[custPreferredClubLeague][i].name;}
		formListAdd(0, str, MENU_ACTION_CUSTOMIZATION, custPreferredClubTeam);

		formInit(FORM_OPTION, pos, MENU_ACTION_NONE, MENU_ACTION_BACK);

		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);
	break;
//#endif


	case MENU_CUST_SKILLS:

		menuListTitleB = custName;

		formTitleStr = new String[] {getMenuText(MENTXT_CUST_SKILL_POINTS)[0]+ " " + custSkillPoints};

		formListNum = custSkillNum;

		formActionCicle = MENU_ACTION_SKILLS_CICLE;

		formListClear();
 		formListAdd((SOFTKEY_DONE<<16), getMenuText(MENTXT_CUST_MOTIVATION), 0, 0);
 		formListAdd((SOFTKEY_DONE<<16), getMenuText(MENTXT_CUST_COACHING), 0, 0);
 		formListAdd((SOFTKEY_DONE<<16), getMenuText(MENTXT_CUST_JUDGEMENT), 0, 0);
 		formListAdd((SOFTKEY_DONE<<16), getMenuText(MENTXT_CUST_DISCIPLINE), 0, 0);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_CUST_SKILLS_ACCEPT);

		listenerInit(SOFTKEY_BACK, SOFTKEY_DONE);
	break;



	case MENU_READY_TO_PLAY:

//#ifdef DEBUG
	Debug.println("==================================");
	Debug.println("custCurrentMonth:" + custCurrentMonth);
	Debug.println("custCurrentAge:" + custCurrentAge);
	Debug.println("custNacionalidad:" + custNationality);
	Debug.println("----------------------------------");
	Debug.println("custDifficulty:" + custDifficulty);
	Debug.println("custLeague:" + custLeague);
	Debug.println("custTeam:" + custTeam);
	Debug.println("custPreferredClubLeague:" + custPreferredClubLeague);
	Debug.println("custPreferredClubTeam:" + custPreferredClubTeam);
	Debug.println("custSkillMotovation:" + custSkillNum[0]);
	Debug.println("custSkillCoaching:" + custSkillNum[1]);
	Debug.println("custSkillPlayerJudjament:" + custSkillNum[2]);
	Debug.println("custSkillDiscipline:" + custSkillNum[3]);
	Debug.println("----------------------------------");
	Debug.println("==================================");
//#endif


	// Mostramos pantalla de loading
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		formLoading();



		option = subOption = cursorY = 0;


		playgameStarted = true;


	// Cargamos valores del customization manager en el motor de ligas
		league.userLeague = custLeague;
		preferedClubId = league.teams[custPreferredClubLeague][custPreferredClubTeam].globalIdx;
		selectTeam(league.season == 0 ? custTeam : -1);

		if (league.season == 1) //if de ZNR
		{
			gameVariablesStartup();
		
			// Valores iniciales para la gesti�n autom�tica
		
			autoManageEmployees();
		}
		
	// Cargamos valores de Fantasy com.mygdx.mongojocs.lma2007.Game
//#ifndef REM_FANTASY
		if (gameMode == FANTASY)
		{
		
			//
			if (league.season == 1)
			{
				league.userTeam.name = fantasyName;

				//#ifndef REM_TEAMCOLORS			
				league.userTeam.flagColor[0] = fantasyRGB[fantasyRGBidx[0]];
				league.userTeam.flagColor[1] = fantasyRGB[fantasyRGBidx[1]];			
				//#endif
			}
			
			
			//#ifndef REM_TEAMSHIELDS
			shieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.userTeam.flagColor);
			//#endif
			
			// David			
			if (league.season == 1) //if de ZNR
			{
				//league.initExtendedPlayers(league.userTeam);
				league.playingChamp = false;
				
				while(league.userTeam.playerCount > 0) {
				
					league.userTeam.removePlayer(league.userTeam.playerIds[0]);	
					
				}
				
				//league.userTeam.playerIds = new short[] {};
				//league.userTeam.playerCount = 0;
				
				for(int i = 0; i < fantasyPlayersId.length; i++) {
	
					//#ifdef DEBUG				
					Debug.println("Adding player "+i+"("+league.playerGetName(fantasyPlayersId[i])+") to user team");				
					//#endif
	
					Team t = league.getTeamWherePlays(fantasyPlayersId[i]);
					if (t != null) t.removePlayer(fantasyPlayersId[i]);
									
					league.userTeam.addPlayer(fantasyPlayersId[i]);
	
					//#ifdef DEBUG				
					Debug.println("coachCash:"+coachCash);
					//#endif
	
					coachCash -= playerCost(fantasyPlayersId[i]);
	
				}
				//ZNR
				league.initExtendedPlayers(league.userTeam);
				gameVariablesStartup();
				autoManageEmployees();
			}
		}
//#endif
		
		//#ifdef DEBUG
		Debug.println("//////////TEMPORADA: "+league.season);
		//#endif
//		Mensaje de bienvenida
		if (league.season == 1) addMessage(0, getMenuText(MENTXT_WELCOMEMSG)[0]+league.userTeam.name, getMenuText(MENTXT_WELCOMEMSG)[1]+league.userTeam.name+getMenuText(MENTXT_WELCOMEMSG)[2]);

//		savePrefs();


		rndObject = new Random(System.currentTimeMillis());

		formInit(FORM_NONE, pos, 0, 0);

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);


		
		
		//popupInitAsk(new String[] {substringReplace(getMenuText(MENTXT_WELCOME)[0], "[team]",league.userTeam.name), getMenuText(MENTXT_WELCOME)[1], getMenuText(MENTXT_SEASONGOALS)[league.playerSeasonGoal]}, SOFTKEY_NONE, SOFTKEY_CONTINUE);
		//popupWait(); MONGOFIX


		menuRelease();
		menuExit = true;
	break;







//#ifndef REM_FANTASY
	case MENU_FANTASY_LEAGUE:

		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_FANT_SELECT_LEAGUE);

		formListClear();

	// Cargamos ligas
		formListAdd(0, gameText[TEXT_LEAGUES], MENU_ACTION_CUSTOMIZATION, custLeague);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NEW_MENU | MENU_FANTASY_CUSTOMIZATION<<16);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;



	case MENU_FANTASY_CUSTOMIZATION:

		custTeam = league.teams[custLeague].length-1;		
		league.userTeam = league.teams[custLeague][custTeam];


		menuListTitleB = getMenuText(MENTXT_TEAM)[0];

		formTitleStr = getMenuText(MENTXT_FANT_CUSTOMIZATION);

		formListClear();
 		formListAdd(0, getMenuText(MENTXT_FANT_EDIT_NAME), MENU_ACTION_NEW_MENU | MENU_FANTASY_EDIT_NAME<<16);
		formListAdd(0, getMenuText(MENTXT_FANT_EDIT_COLOUR), MENU_ACTION_NEW_MENU | MENU_FANTASY_COLOUR<<16);
		formListAdd(0, getMenuText(MENTXT_FANT_CHOOSE_PLAYERS), MENU_ACTION_NEW_MENU | MENU_FANTASY_CHOOSE_PLAYERS<<16);
		formListAdd(0, getMenuText(MENTXT_CUST_NEXT), MENU_ACTION_FANTASY_CUSTOMIZATION_ACCEPT);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;



	case MENU_FANTASY_EDIT_NAME:

		menuListTitleB = custName;

		formInit(FORM_EDIT_NAME, pos, MENU_ACTION_BACK, MENU_ACTION_BACK);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;



	case MENU_FANTASY_COLOUR:

		menuListTitleB = custName;

	// cargamos escudo de la opcion seleccionada
		//#ifndef REM_TEAMCOLORS
		league.userTeam.flagColor[0] = fantasyRGB[fantasyRGBidx[0]];
		league.userTeam.flagColor[1] = fantasyRGB[fantasyRGBidx[1]];
		//#endif
		//#ifndef REM_TEAMSHIELDS
		formShieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.userTeam.flagColor);
		//#endif
		formActionCicle = MENU_ACTION_FANTASY_COLOUR_CICLE;

		formListClear();

	// Cargamos ligas
		formListAdd(0, getMenuText(MENTXT_FANT_LEFT_COLOUR), MENU_ACTION_NONE, 0);
		formListAdd(0, getMenuText(MENTXT_FANT_RIGHT_COLOUR), MENU_ACTION_NONE, 0);

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_NONE);

		xLeague = custLeague;
		
		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;



	case MENU_FANTASY_CHOOSE_PLAYERS:

		formBaseColor = FORM_RGB_INDEX_SQUAD;

		menuListTitleB = getMenuText(MENTXT_FANT_CHOOSE_PLAYERS_MAY)[0];

		formSwapEnabled = true;
		formCampoImg = campoImg;	// Mostramos campo a la izquierda

		formTableItems = new int[] {
			(FONT_ORANGE <<8)	| (PRINT_HCENTER),
			(FONT_WHITE <<8)	| (PRINT_LEFT),
		};

		formTableMasterCell = new int[] {1};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
//		formTableNumerar = true;	// Indicamos que las lineas sean numeradas (columna numeria a la izquierda de la tabla)
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		formTableTitle = true;		// La primera linea hace de titulo

		formListClear();

 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {getMenuText(MENTXT_PS)[0], getMenuText(MENTXT_NAME)[0]}, 0,0);

		for(int i = 0; i < fantasyPlayersId.length ; i++)
		{
			if (fantasyPlayersId[i] >= 0)
			{
				String banquillo = "";
				if (i > 10 && i<16) {banquillo = getMenuText(MENTXT_LST_RESERVE_SUB)[0] + " ";}
				else
				if (i > 15) {banquillo = getMenuText(MENTXT_LST_RESERVE_SUB)[1] + " ";}

				formListAdd(0, new String[] {
					getMenuText(MENTXT_LST_POSITION)[league.playerGetPosition(fantasyPlayersId[i])],
					banquillo + league.playerGetName(fantasyPlayersId[i])
					}, 0,0);
			} else {
				formListAdd(0, new String[] {" ", " "}, 0,0);
			}
		}

		formInit(FORM_TABLE, pos, MENU_ACTION_FANTASY_CHOOSE_PLAYERS_CANCEL, MENU_ACTION_FANTASY_CHOOSE_PLAYERS_ACCEPT);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SEARCH);
	break;
//#endif





	
	// MENU DURANTE EL PARTIDO DE TEXTO

	case MENU_TEXTMATCH:

		formListClear();

		//#ifndef REM_STADIUM
		formBaseColor = FORM_RGB_INDEX_LISTENER;
		//#else
		formBaseColor = FORM_RGB_INDEX_LISTENER - 1;
		//#endif

//		menuListTitleA = "FINANCE";
//		menuListTitleB = "EMPLOYEE LIST";

		formTableItems = new int[] {			
			0x00000 | (FONT_WHITE <<8)			| (PRINT_LEFT),
			0x18000	| (ITEM_ARROW_LEFT<<8)		| (PRINT_HCENTER),
			0x00000 | (FONT_WHITE <<8)			| (PRINT_HCENTER),
			0x18000	| (ITEM_ARROW_RIGHT<<8)		| (PRINT_HCENTER),
		};

//		formTableTitle = true;		// La primera linea hace de titulo
		formTableMasterCell = new int[] {0};	// Indicamos la celda "maestra" la cual se expandera o cortara segun el area que falte o sobre
		formTableOption = true;		// Indicamos que la tabla funciona como listado de opciones
		floatingForm = true;
		
		formListClear();
// 		formListAdd(LINE_SOMBRA | (FONT_ORANGE<<8), new String[] {"JOBS",moneyUnit()}, 0,0);
		
		formActionCicle = MENU_ACTION_TEXTMATCH_CICLE;
 

		formListAdd((SOFTKEY_SELECT<<16), getMenuText(MENTXT_MATCH_CONTINUE), MENU_ACTION_MENU_EXIT,0);
		formListAdd((SOFTKEY_SELECT<<16), getMenuText(MENTXT_MATCH_SKIP_MATCH), MENU_ACTION_TEXTMATCH_SKIP,0);

		formationStr = gameText[TEXT_FORMATIONS];

//#ifndef REM_BONUS_CODES
		if (bonusCodes[BONUS_MOREFORMATIONS] != 0)
		{
			formationStr = new String[8];
			for(int i = 0; i < 6;i++)
				formationStr[i] = gameText[TEXT_FORMATIONS][i];
			formationStr[6] = getMenuText(MENTXT_EXTRA_FORMATIONS)[0];
			formationStr[7] = getMenuText(MENTXT_EXTRA_FORMATIONS)[1];
			//formListAdd(0, new String[] {getMenuText(MENTXT_EXTRA_FORMATIONS)[0]}, 0,0);
		}
		userFormation = userFormation%formationStr.length;
//#endif
		
		if (!exhibitionFlag)
		{
			formListAdd((SOFTKEY_CHANGE<<16), new String[] {getMenuText(MENTXT_MATCH_FORMATION)[0], null, formationStr[userFormation], null},MENU_ACTION_TEXTMATCH_CICLE,0);
			formListAdd((SOFTKEY_CHANGE<<16), new String[] {getMenuText(MENTXT_MATCH_ORDERS)[0], null, getMenuText(MENTXT_ATTACK_STYLE)[squadStyle], null},MENU_ACTION_TEXTMATCH_CICLE,0);
			formListAdd((SOFTKEY_SELECT<<16), getMenuText(MENTXT_MATCH_SQUAD), MENU_ACTION_NEW_MENU | (MENU_SUBSTITUTE<<16),0);
		}
		
		formListAdd((SOFTKEY_CHANGE<<16), new String[] {getMenuText(MENTXT_SOUND)[0], null, getMenuText(gameSound ? MENTXT_ENABLED : MENTXT_DISABLED)[0] ,null}, MENU_ACTION_TEXTMATCH_CICLE,0); 		

		formInit(FORM_TABLE, pos, MENU_ACTION_NONE, MENU_ACTION_COMMAND);

		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);
		
		if (gameStatus == GAME_GFXMATCH) gfxMatchShow = true;
	break;









	case MENU_OPTIONS:

		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_OPTI_OPTIONS);

		formListClear();
	//#ifndef PLAYER_NONE
		formListAdd((SOFTKEY_CHANGE<<16), gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
		//#ifndef REM_VOLUME
			formListAdd((SOFTKEY_CHANGE<<16), getMenuText(MENTXT_OPTI_VOLUME), MENU_ACTION_VOLUME_CHG, gameVolume);
		//#endif
	//#endif
	//#ifdef VIBRATION
		//formListAdd((SOFTKEY_CHANGE<<16), gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		formListAdd((SOFTKEY_CHANGE<<16), getMenuText(MENTXT_OPTI_LST_AUTOSAVE), MENU_ACTION_AUTOSAVE_CHG, gameAutosave? 1:0);
		formListAdd((SOFTKEY_READ<<16), getMenuText(MENTXT_OPTI_CREDITS), MENU_ACTION_NEW_MENU | MENU_SCROLL_CREDITS<<16);		
		//#ifndef REM_BONUS_CODES
		formListAdd((SOFTKEY_SELECT<<16), getMenuText(MENTXT_OPTI_BONUS_CODES), MENU_ACTION_NEW_MENU | MENU_BONUS_CODES<<16);
		//#endif
		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;


	case MENU_HELP:

		formTitleStr = gameText[TEXT_HELP];

		formListClear();
		//#ifndef REM_BONUS_CODES
		for (int i=0 ; i<getMenuText(MENTXT_HELP_SECTIONS).length ; i++)
		//#else
		//#endif
		{
			formListAdd((SOFTKEY_READ<<16), new String[] {getMenuText(MENTXT_HELP_SECTIONS)[i]}, MENU_ACTION_SHOW_HELP);
		}

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK_HELP, 0);

		listenerInit(gameStatus == GAME_PLAY_INIT? SOFTKEY_MENU:SOFTKEY_BACK, SOFTKEY_SELECT);
	break;


	case MENU_SHOW_TEXT:

	// Mostramos texto...
		formTitleStr = menuShowTextTitleStr;

		formListClear();
		formListAdd(0, menuShowTextBodyStr);
		formInit(FORM_LIST, 0, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;


	case MENU_SCROLL_ABOUT:

	// Mostramos pantalla de loading
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		formLoading();

		String[][] legalText2 = textosCreate(loadFile("/"+langStr+"legal.txt"));

		menuShowTextTitleStr = gameText[TEXT_ABOUT];
		menuShowTextBodyStr	= legalText2[0];

		menuInit(MENU_SHOW_TEXT);
	break;


	case MENU_SCROLL_CREDITS:

	// Mostramos pantalla de loading
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		formLoading();

		menuShowTextTitleStr = getMenuText(MENTXT_OPTI_CREDITS);
		menuShowTextBodyStr	= getMenuText(MENTXT_CREDITS_SCROLL);

		menuInit(MENU_SHOW_TEXT);
	break;


//#ifndef REM_BONUS_CODES
	case MENU_BONUS_CODES:

		menuListTitleB = custName;

		formTitleStr = getMenuText(MENTXT_OPTI_BONUS_CODES);

		formBonusCodes = bonusCodes;

		formListClear();
		formListAdd((bonusCodes[0]==0? SOFTKEY_ENABLE:SOFTKEY_DISABLE)<<16, getMenuText(MENTXT_BONUS_RAIN_IN_MATCH));
		formListAdd((bonusCodes[1]==0? SOFTKEY_ENABLE:SOFTKEY_DISABLE)<<16, getMenuText(MENTXT_BONUS_EXTRA_FORMATIONS));
		formListAdd((bonusCodes[2]==0? SOFTKEY_ENABLE:SOFTKEY_DISABLE)<<16, getMenuText(MENTXT_BONUS_FAST_TRAINING));
		formListAdd((bonusCodes[3]==0? SOFTKEY_ENABLE:SOFTKEY_DISABLE)<<16, getMenuText(MENTXT_BONUS_NO_INJURIES));
		formListAdd((bonusCodes[4]==0? SOFTKEY_ENABLE:SOFTKEY_DISABLE)<<16, getMenuText(MENTXT_BONUS_UNLIMITED_FUNDS));

	//#ifndef REM_TIPS_INFO
		formListAdd(SOFTKEY_SELECT<<16, getMenuText(MENTXT_LMA_TIPS), MENU_ACTION_NEW_MENU | MENU_LMA_TIPS<<16, 0);
    //#endif

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, MENU_ACTION_BONUS_CODES_ACCEPT);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;
//#endif


//#ifndef REM_TIPS_INFO
	case MENU_LMA_TIPS:

	// Mostramos pantalla de loading
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		formLoading();

		String[][] tmpStr = textosCreate( loadFile("/"+langStr+"tips.txt") );

	// Mostramos texto...
		formTitleStr = getMenuText(MENTXT_LMA_TIPS);

		formListClear();

		boolean none = true;
		//#ifndef REM_BONUS_CODES
		for (int i=0 ; i<5 ; i++)
		{
			if (checkBonusCodes[i]) {formListAdd(0, tmpStr[i]); none = false;}
		}
		//#endif

	// Si no se muestra ningun texto, mostramos popup...
		if (none)
		{
			menuRelease();
			menuInitBack();

			popupInitBack(tmpStr[5]);
			popupWait();
			break;
		}

		formInit(FORM_LIST, 0, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
//#endif


	case MENU_INGAME:

		formListClear();
		formListAdd(0, gameText[TEXT_CONTINUE], MENU_ACTION_CONTINUE);
	//#ifndef PLAYER_NONE
		formListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
	//#endif
	//#ifdef VIBRATION
		//formListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra? 1:0);
	//#endif
		//formListAdd(0, gameText[TEXT_HELP], MENU_ACTION_NEW_MENU | MENU_HELP<<16);
		formListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);

		formInit(FORM_OPTION, pos, 0, 0);		

		listenerInit(SOFTKEY_NONE, SOFTKEY_SELECT);
	break;



	case MENU_SOUND:

		formTitleStr = getMenuText(MENTXT_OPTI_OPTIONS);

		formListClear();
	//#ifndef PLAYER_NONE
		formListAdd((SOFTKEY_CHANGE<<16), gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound? 1:0);
		//#ifndef REM_VOLUME
			formListAdd((SOFTKEY_CHANGE<<16), getMenuText(MENTXT_OPTI_VOLUME), MENU_ACTION_VOLUME_CHG, gameVolume);
		//#endif
	//#endif

		formInit(FORM_OPTION, pos, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_SELECT);
	break;




//#ifndef REM_FOOTBALL_ONE
	case MENU_FOOTBALL_ONE:

		formLoading();

		menuListTitleA = menuListTitleB = null;
		if (fOneMainImg == null)	fOneMainImg = loadImage("/fOneMain");
		if (fOneLogoImg == null)	fOneLogoImg = loadImage("/fOneLogo");

		formListClear();

		formListAdd(0, footballOneStr);

		formInit(FORM_FOOTBALL_ONE, 0, MENU_ACTION_BACK, 0);

		listenerInit(SOFTKEY_BACK, SOFTKEY_NONE);
	break;
//#endif
	}

}


String formationStr[];



public void menuAction(int cmd)
{
	int newMenuId = cmd>>16; if (cmd >= 0 ) {cmd &= 0xffff;}

	short playerId;

//#ifdef DEBUG
	Debug.println("menuAction("+cmd+")");
//#endif

	switch (cmd)
	{
	case MENU_ACTION_NEW_MENU:	// lanzar nuevo menu, segun parametro en menuCMD

		menuRelease();
		menuInit(newMenuId);
	break;


	case MENU_ACTION_COMMAND:

		menuAction(formListCMD = formListDat[formListPos][1]);
	break;



	case MENU_ACTION_NEW_GAME:
		//#ifndef REM_FANTASY
	case MENU_ACTION_NEW_GAME_FANTASY:
		//#endif

		exhibitionFlag = false;
	// Preguntamos si estamos seguros de perder las partida anterior
		if (playgameStarted)
		{
			popupInitAsk(getMenuText(MENTXT_WARNING_ALL_PREVIOUSLY_STORED), SOFTKEY_NO, SOFTKEY_YES);
			popupWait();

			if (!popupResult) {break;}
		}
		playgameStarted = false;


	// Mostramos pantalla de loading
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		formLoading();


	// RESET GAME
		//#ifndef REM_FANTASY
		gameMode = (cmd==MENU_ACTION_NEW_GAME? CAREER:FANTASY);
		//#else
		//#endif
		
		league.reset();
		
		custSkillPoints = 0;
		custSkillNum = new int[] {5,5,5,5};


	// Salvamos com.mygdx.mongojocs.lma2007.Game, recien inicializado
		saveGame();

		menuRelease();
		menuInit(MENU_CUST_DIFFICULT);
	break;


	case MENU_ACTION_SHOW_HELP:		// Mostramos Instrucciones...

	// Mostramos pantalla de loading
		formLoading();

	//#ifndef REM_GC_GRAPHICS
		menuIconsImg1 = null; menuIconsImg = null;
		//#ifndef OLD_MENU_BAR
		menuIconsImg2 = null;
		//#endif
	//#endif

		league.rawPlayerStats = null;
		System.gc();
		
		if (gameTextHelp == null) {gameTextHelp = textosCreate(loadFile("/"+langStr+"help.txt"));}

		menuRelease();

		menuShowTextTitleStr = new String[] {getMenuText(MENTXT_HELP_SECTIONS)[formListPos]};
		menuShowTextBodyStr	= gameTextHelp[formListPos];

		menuInit(MENU_SHOW_TEXT);
	break;


	case MENU_ACTION_CUST_SKILLS_ACCEPT:

		if (custSkillPoints > 0)
		{
			popupInitAsk(getMenuText(MENTXT_YOU_MUST_DISTRIBUTE_ALL_SKILL), SOFTKEY_NONE, SOFTKEY_SELECT);
			popupWait();
			break;
		}

		menuRelease();
		menuInit(MENU_READY_TO_PLAY);
	break;


	case MENU_CUST_LEAGUE_TEAM_ACCEPT:

		switch (menuType)
		{
		case MENU_CUST_LEAGUE_TEAM:

			custLeague = xLeague;
			custTeam = xTeam;
			menuRelease();

		//#ifndef REM_MANAGER_CUSTOMIZATION
			menuInit(MENU_CUST_MANAGER);
	    //#else
	    //#endif
		break;

		case MENU_EXHI_LEAGUE_TEAM_A:

			exhiLeagueA = xLeague;
			exhiTeamA = xTeam;
			menuRelease();
			menuInit(MENU_EXHI_LEAGUE_TEAM_B);
		break;

		case MENU_EXHI_LEAGUE_TEAM_B:

			exhiLeagueB = xLeague;
			exhiTeamB = xTeam;

			menuRelease();

			league.userMatch[0] = league.teams[exhiLeagueA][exhiTeamA];
			league.userMatch[1] = league.teams[exhiLeagueB][exhiTeamB];

		// Generamos escudos con sus colores correctos
			//#ifndef REM_TEAMSHIELDS
			matchShieldsImg[0] = changePal(loadFile("/escudos.png"), shieldRgbs, league.userMatch[0].flagColor);
			matchShieldsImg[1] = changePal(loadFile("/escudos.png"), shieldRgbs, league.userMatch[1].flagColor);
			//#endif
			//gameMode = EXHIBITION;
			exhibitionFlag = true;

			menuInit(MENU_PREMATCH);
		break;
		}
	break;

//#ifndef REM_FINANCES_PRICESSET	

	case MENU_ACTION_FINANCE_CICLE:
		
		if(autoManagement[AUTO_FINANCES_SETPRICES])
		{
			popupInitAsk(getMenuText(MENTXT_SWITCH_TO_EXPERT), SOFTKEY_CANCEL, SOFTKEY_ACEPT);
			popupWait();

			if (!popupResult) {
				
				break;
				
			} else {
				
				autoManagement[AUTO_FINANCES_SETPRICES] = false;
			}
		}
		
		ticketPrices[formListPos - 1] += (5*keyX);
		
		if(ticketPrices[formListPos - 1] < 5) {
			ticketPrices[formListPos - 1] = 5;
		}
		
		if(ticketPrices[formListPos - 1] > 250) {
			ticketPrices[formListPos - 1] = 250;
		}
		
		formListStr[formListPos][2] = ""+moneyStr(ticketPrices[formListPos - 1],false);
		formShow = true;
	break;
	
//#endif	
	
	case MENU_ACTION_TRANSFERS_MARK:
		
	//#ifdef DEBUG
		Debug.println("Marking:"+formListStr[formListPos][0]);
    //#endif




		if(formListStr[formListPos][0].equals("")) {
			
			// Already negotiating with this player
			
//#ifndef REM_TRANSFERS_NEGOTIATIONS			
			if(menuType == MENU_TRANSFERS_PLAYER_SELECT && pendingNegotiationIndex(buySelectionList[formListPos - 1]) >= 0) {
				
				popupInitBack(new String[] {getMenuText(MENTXT_OFFER_ANSWER_WAITING)[0]});
				popupWait();				
			
			} else 
//#endif
			// Not enough money to buy this player	
			if(menuType == MENU_TRANSFERS_PLAYER_SELECT && getAvailableCash() < playerCost(buySelectionList[formListPos - 1])) {
							
				popupInitBack(new String[] {getMenuText(MENTXT_BUY_PLAYER_NOT_ENOUGH_CASH)[0]});
				popupWait();
				
			// Mark to buy
			} else {
				
				formListStr[formListPos][0] = (menuType == MENU_TRANSFERS_PLAYER_SELECT ?  ">>" : "<<");
				transfersMarkedPlayers++;
			}
				
		} else {
				
			formListStr[formListPos][0] = "";
			transfersMarkedPlayers--;
		}
			
		if(transfersMarkedPlayers == 0) {
				
			listenerInit(SOFTKEY_BACK, SOFTKEY_MARK);
				
		} else {
				
			listenerInit(menuType == MENU_TRANSFERS_SELL ? SOFTKEY_SELL : SOFTKEY_BUY, SOFTKEY_MARK);			
		}
	
		
		formShow = true;
	break;
	
	case MENU_ACTION_TRANSFERS_SELECT_TEAM:
		
		transfersSelectedLeague = formCiclePos;
		transfersSelectedTeam = formListPos - 1;
		
		if(league.teams[transfersSelectedLeague][transfersSelectedTeam] == league.userTeam) {
			
			popupInitBack(getMenuText(MENTXT_CANT_BUY_PLAYERS_OWN_TEAM));
			popupWait();
			
		} else {
		
			// Construct buy selection list: All selected team players
			
			buySelectionList = new short[league.teams[transfersSelectedLeague][transfersSelectedTeam].playerCount];
			
			for(int i = 0; i < buySelectionList.length; i++) {
				
				buySelectionList[i] =  league.teams[transfersSelectedLeague][transfersSelectedTeam].playerIds[i];
					
			}
			
			formListPos = formCiclePos;
			menuRelease();
			
			menuInit(MENU_TRANSFERS_PLAYER_SELECT);
		}
		break;
		
//#ifndef REM_TRANSFERS_NEGOTIATIONS
		
	case MENU_ACTION_NEGOTIATE_OPTIONS_CICLE:
		
		switch(formListPos) {
		
			case 1:
				negotiateYears += keyX;
				
				if(negotiateYears < 1) {
					
					negotiateYears = 1;
				}
				if(negotiateYears > 10) {
					
					negotiateYears = 10;
				}
				
				formListStr[formListPos][1] = negotiateYears+getMenuText(MENTXT_CONTRACT_DETAILS)[1]; 
			break;
			
			case 3:
				negotiateSalary += keyX*1000;
				
				if(negotiateSalary < 1000) {
					
					negotiateSalary = 1000;
				}
				if(negotiateSalary > 35000) {
					
					negotiateSalary = 35000;
				}
				
				formListStr[formListPos][1] = moneyStr(negotiateSalary,true);
			break;
			
			case 5:
				negotiateBonus += keyX*1000;
				
				if(negotiateBonus < 1000) {
					
					negotiateBonus = 1000;
				}
				if(negotiateBonus > 10000) {
					
					negotiateBonus = 10000;
				}
				
				formListStr[formListPos][1] = moneyStr(negotiateBonus,true);
			break;
		
		}
				
		formShow = true;
		break;
				
	case MENU_ACTION_NEGOTIATE_CONFIRM:
		
		// Confirm and register player selling
		
		playerId = (short)messageData[currentMessage][0];
		
		if(pendingNegotiationIndex(playerId) < 0) {
								
			popupInitBack(new String[] {substringReplace(getMenuText(MENTXT_CONTRACTOFFER_SENT)[0],"[player]",league.playerGetName(playerId))});
			popupWait();
			
			//league.userTeam.playerIds = removeValueFromArray(league.userTeam.playerIds, playerId);
			//league.userTeam.playerCount--;
			
			deleteMessage(currentMessage);
			
			buyingPlayers = removeValueFromArray(buyingPlayers, playerId);
			
			// Construct the new negotiation info
			
			int auxNeg[][];
			
			auxNeg = new int[negotiations.length + 1][];
			
			//System.arraycopy(auxNeg,0,negotiations,0,negotiations.length);
			System.arraycopy(negotiations,0,auxNeg,0,negotiations.length);
			//#ifdef DEBUG
			System.out.println("NEGOTIATION: "+playerId+" "+league.playerGetName(playerId));
			//#endif
			auxNeg[auxNeg.length - 1] = new int[] {playerId, negotiateYears, negotiateSalary, negotiateBonus, negotiatePatience - 1};
			
			negotiations = auxNeg;
			
			auxNeg = null;
			
		} else {
		
			popupInitBack(new String[] {getMenuText(MENTXT_OFFER_ANSWER_WAITING)[0]});
			popupWait();
			deleteMessage(currentMessage);
		}
		
		menuAction(MENU_ACTION_BACK);
		
	break;
	
//#endif	
	
	case MENU_ACTION_BUY_COMPLETE:
		
		if(league.userTeam.playerCount < 30) {
		
			currentMessage = 0;
			//System.out.println("MES: "+currentMessage);
			
			//System.out.println("1");
			// Remove player from its original team
			Team where = league.getTeamWherePlays((short)messageData[currentMessage][0]);
			if (where.globalIdx != league.userTeam.globalIdx)
			{
				clubExpenses[INCOME_TRANSFERS] += playerCost((short) messageData[currentMessage][0]);
				
				where.removePlayer(messageData[currentMessage][0]);					
				league.userTeam.addPlayer(messageData[currentMessage][0]);
			}
			//System.out.println("2 "+messageData[currentMessage].length +" - "+messageType[currentMessage]);		 						
			// Create the contract data		
			addContract(messageData[currentMessage][0], messageData[currentMessage][1], messageData[currentMessage][2], messageData[currentMessage][3]);
			
			// TBD: Register transfer
			/*
			deleteMessage(currentMessage);
		
			menuAction(MENU_ACTION_BACK);
			*/
			
		} 		
		/*else {
		
			popupInitBack(getMenuText(MENTXT_YOUR_TEAM_HAS_30_PLAYERS_ALREADY));
			popupWait();			
		}*/
		//System.out.println("3");
	break;
	
	case MENU_ACTION_SHIRT_SPONSOR_ACCEPT:
		
		if(sponsors[SPONSOR_SHIRTS][1] > 0) {

			popupInitBack(getMenuText(MENTXT_YOU_CANNOT_CONTRACT_NEW_SHIRT));
			popupWait();
				
		} else {
			
			sponsors[SPONSOR_SHIRTS][0] = messageData[currentMessage][0];
			sponsors[SPONSOR_SHIRTS][1] = messageData[currentMessage][1];
			sponsors[SPONSOR_SHIRTS][2] = messageData[currentMessage][2];
		
			popupInitBack(getMenuText(MENTXT_SHIRTSPONSOR_CONTRACTED));
			popupWait();
			
		}
		
		deleteMessage(currentMessage);
		
		menuAction(MENU_ACTION_BACK);
		
	break;
	
	case MENU_ACTION_FENCE_SPONSOR_ACCEPT:
		
		if(sponsors[SPONSOR_FENCES][1] > 0) {

			popupInitBack(getMenuText(MENTXT_YOU_CANNOT_CONTRACT_NEW_FENCES));
			popupWait();
				
		} else {

		
			sponsors[SPONSOR_FENCES][0] = messageData[currentMessage][0];
			sponsors[SPONSOR_FENCES][1] = messageData[currentMessage][1];
			sponsors[SPONSOR_FENCES][2] = messageData[currentMessage][2];
			
			popupInitBack(getMenuText(MENTXT_FENCESPONSOR_CONTRACTED));
			popupWait();
			
		}
		
		deleteMessage(currentMessage);
		
		menuAction(MENU_ACTION_BACK);
		
	break;	
		
	case MENU_ACTION_TEXTMATCH_CICLE:
				
		if(formListPos == 2 && !exhibitionFlag) {
					//TODO: ESTO CON BONUS CODES NO FUNCIONA
			userFormation = (formationStr.length + userFormation + (keyX == 0 ? 1 : keyX)) %formationStr.length;  
			formListStr[formListPos][2] = formationStr[userFormation];
						
		}
		
		if(formListPos == 3) {
			
			squadStyle = (3 + squadStyle + (keyX == 0 ? 1 : keyX)) %3;  
			formListStr[formListPos][2] = getMenuText(MENTXT_ATTACK_STYLE)[squadStyle];
		}
		
		if(formListPos == 5 && !exhibitionFlag || formListPos == 2 && exhibitionFlag) {
			
			gameSound = !gameSound;
			if (!gameSound) {soundStop();}
			soundPlay(SOUND_WHISTLE,1);
			formListStr[formListPos][2] = getMenuText(gameSound ? MENTXT_ENABLED : MENTXT_DISABLED)[0];
		}
								
		formShow = true;
		break;
		
	case MENU_ACTION_TEXTMATCH_SKIP:

	// Limpiamos pantalla
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		fillCanvasRGB = 0x000000;
		forceRenderTask(RENDER_FILL_CANVAS);

		canvasImg = loadImage("/loading");
		forceRender();


		if (gameStatus == GAME_GFXMATCH)
		{	
//#ifndef REM_2DMATCH			
			gfxMatchSkip();
			menuAction(MENU_ACTION_TEXTMATCHEND);
//#endif			
		}
		else
		{
//#ifndef REM_TEXTMATCH				
			textMatchDisableLog = true;
			while(!textMatchTick()) {
			}
			textMatchDisableLog = false;
			menuAction(MENU_ACTION_TEXTMATCHEND);
//#endif			
		}
		
		break;
		
		

	case ACTION_MENUBAR:
		biosStatus = BIOS_MENUBAR;
		menuBarShow = true;
	break;


	case MENU_ACTION_MENU_EXIT:

		//if (menuType == MENU_PLAY) {gameMode = CAREER;}		// HACK
		if (menuType == MENU_PLAY) exhibitionFlag = false;
		
		menuRelease();

		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		menuExit = true;
	break;

		
	case MENU_ACTION_BACK_HELP:	// Ir "Atras" desde el menu de Ayuda

		gameTextHelp = null;

		if (!borderTypeMenu)
		{
			if (menuIconsImg1 == null)	menuIconsImg1 = loadImage("/iconos");
		//#ifndef OLD_MENU_BAR
			if (menuIconsImg2 == null)	menuIconsImg2 = loadImage("/iconosx");
		//#endif
		}

		if (league.rawPlayerStats == null) league.rawPlayerStats = loadFile("/plys");

	case MENU_ACTION_BACK:	// Ir "Atras" entre los menus

		menuRelease();
		menuInitBack();
	break;


	case MENU_ACTION_EXHIBITION:

		menuRelease();

		//gameMode = EXHIBITION;
		exhibitionFlag = true;

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_CONTINUE:	// Continuar

		
		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;
	break;


	case MENU_ACTION_READMESSAGE:

		if (formListPos < messageCount)
		{
			menuRelease();
			readMessage(formListPos);
		}
	break;

	case MENU_ACTION_MAINMENU:	// Provocamos GAME OVER desde menu (Accion)

		menuRelease();

		menuListTitleA = null;
		menuListTitleB = null;
		
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		gameStatus = GAME_MENU_START;
		menuExit = true;
	break;

	case MENU_ACTION_MATCH:

	// Mostramos pantalla de loading
		listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
		formLoading();

//#ifdef DEBUG_PC_USED_MEM
	showMemUsed("Antes de lanzar el Match");
//#endif
		
		//#ifdef DEBUG			
		Debug.println("MENU_ACTION_MATCH");			
		//#endif
		
//#ifndef REM_FOOTBALL_ONE
		league.enableMsg = true;
//#endif		
		initRecordMatch();
		matchGoalScorers = new String[20];
		matchGoals = new int[10][2];
		numGoal = 0;
		numGoals = new int[2];

		
		//#ifdef DEBUG			
		Debug.println("initRecordMatch()");			
		//#endif
		
		//#ifdef REM_2DMATCH
		//#endif
		
		switch (formCiclePos)
		{
			case 0:		// Skip Match
	
				league.matchPlay(league.userMatch[0], league.userMatch[1]);
				menuAction(MENU_ACTION_TEXTMATCHEND);
			break;

//#ifndef REM_2DMATCH			
			case 1:		// 2D Match
				
			playingMatch = true;
			substitutionsLeft = 3;
			substitutedPlayers = new short[] {};

//#ifdef DEBUG			
			Debug.println("menuRelease()");			
//#endif
								
			menuRelease();
			
//#ifdef DEBUG			
			Debug.println("league.matchPlay");			
//#endif
			
			league.matchPlay(league.userMatch[0], league.userMatch[1]);
			
			
			//#ifdef DEBUG
			if (cheatAlwaysWin)
			{
				if (league.userMatch[0] == league.userTeam)
					league.userMatch[0].matchGoals = (byte)(league.userMatch[1].matchGoals+1);
				else
					league.userMatch[1].matchGoals = (byte)(league.userMatch[0].matchGoals+1);
			}
			//#endif
			
			//#ifdef DEBUG			
			Debug.println("Resultado esperado : "+league.userMatch[0].matchGoals+" - "+league.userMatch[1].matchGoals);			
			//#endif

//#ifdef DEBUG			
			Debug.println("consolelistclear");			
//#endif

//#ifndef REM_TEXTMATCH			
			consoleListClear();
//#endif		
			
//#ifdef DEBUG			
			Debug.println("initialResourcesInit()");			
//#endif
			
			initialResourcesInit();
//#ifdef DEBUG			
			Debug.println("initResources()");			
//#endif
			

			//#ifndef ISOMETRICMATCH
			//initResources();
			//#endif
			
			//#ifdef DEBUG			
			Debug.println("gfxMatchInit()");			
			//#endif
			
			gfxMatchInit();
			//#ifdef ISOMETRICMATCH
			
			//#ifdef DEBUG			
			Debug.println("isometricMatchInit()");			
			//#endif
			
			
			//#ifndef FEA_CHAPAS
			isometricMatchInit();
			//#else
			/*grassImg = loadImage("/grass");
			chapasImg = loadImage("/chapas");
			goalsImg = loadImage("/goals");*/
			//#endif
			//#endif
			
			//#ifdef DEBUG			
			Debug.println("END isometricMatchInit()");			
			//#endif
			
			//soundStop();
			soundPlayEx(SOUND_APPLAUSE);
			
			listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
			
			gameStatus = GAME_GFXMATCH;
			menuExit = true;

		break;
//#endif

//#ifndef REM_TEXTMATCH
		
		case 2:		// Text Match

			menuRelease();
			
			//league.playJourney();
			league.matchPlay(league.userMatch[0], league.userMatch[1]);
			
			//if (league.journeyType == com.mygdx.mongojocs.lma2007.League.LEAGUEJOURNEY)
			//	menuInit(MENU_MATCHLIST);
			//#ifdef DEBUG
			Debug.println("Resultado esperado : "+league.userMatch[0].matchGoals+" - "+league.userMatch[1].matchGoals);
			//#endif
			
			consoleListClear();
	
			//menuListTitleA = "Match";
			//menuListTitleB = "Text Match";
							
			textMatchInit();
	
			listenerInit(SOFTKEY_MENU, SOFTKEY_SELECT);
			
			gameStatus = GAME_CONSOLE;
			menuExit = true;
					
			//league.nextJourney();
		break;
		
//#endif
		
		}
	break;

	case MENU_ACTION_TRAININGSCHEDULE_LINE:

		if(autoManagement[AUTO_TRAINING_SCHEDULE])
		{
			popupInitAsk(getMenuText(MENTXT_SWITCH_TO_EXPERT), SOFTKEY_CANCEL, SOFTKEY_ACEPT);
			popupWait();

			if (!popupResult)
			{
				break;
			} else {
				autoManagement[AUTO_TRAINING_SCHEDULE] = false;
			}
		}

		trainingLine = 3-formListPos;

		menuRelease();
		menuInit(MENU_TRAINING_SCHEDULE, 1);
	break;


	case MENU_ACTION_TRAININGSCHEDULE_CHANGE:

		if (formListPos > 0)
		{
			if ((keyX > 0 && trainingSchedule[trainingLine][0] > 0)
			||	(keyX < 0 && trainingSchedule[trainingLine][formListPos] > 0))
			{
				trainingSchedule[trainingLine][0] -= keyX;
				trainingSchedule[trainingLine][formListPos] += keyX;
			}
		}
		menuRelease(true);
		menuInit(MENU_TRAINING_SCHEDULE, formListPos);
		break;


	case MENU_ACTION_EXIT_GAME:	// Exit com.mygdx.mongojocs.lma2007.Game???

		popupInitAsk(gameText[TEXT_ARE_YOU_SURE], SOFTKEY_NO, SOFTKEY_YES);
		popupWait();

		if (popupResult)
		{
			menuRelease();
	
			gameStatus = GAME_EXIT;
			menuExit = true;
		}
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = formListOpt() != 0;
		if (!gameSound) {soundStop();}
		else
		if (menuType == MENU_OPTIONS || (gameStatus == GAME_PLAY_INIT && menuType == MENU_SOUND)) {soundPlay(0,0);}
	break;

//#ifndef REM_VOLUME
	case MENU_ACTION_VOLUME_CHG:

		gameVolume = formListOpt();
		gameVolumePerCent = new int[] {25, 50, 100}[gameVolume];
		soundPlay(soundOld, soundLoop);
	break;
//#endif


	case MENU_ACTION_AUTOSAVE_CHG:

		gameAutosave = formListOpt() != 0;
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = formListOpt() != 0;
		if (gameVibra) {vibraInit(300);}
	break;


	case MENU_ACTION_ASK:

		menuAction(menuActionAsk);
	break;
	
	
	case MENU_ACTION_TEXTMATCHEND:

		playingMatch = false;
		
		if (gameStatus == GAME_GFXMATCH)
		{
//#ifndef REM_2DMATCH			
			/*
			System.out.println("matchGoalsA"+league.userMatch[0].matchGoals);
			System.out.println("matchGoalsB"+league.userMatch[1].matchGoals);
			System.out.println("goalsA"+goalsA);
			System.out.println("goalsB"+goalsB);
			*/
			league.userMatch[0].matchGoals = goalsA;
			league.userMatch[1].matchGoals = goalsB;
			//league.recordOwnGoals = false;
			
			//league.rawPlayerNames = loadFile("/plyn");
			//league.rawPlayerStats = loadFile("/plys");
		
			//#ifdef ISOMETRICMATCH
			//#ifndef FEA_CHAPAS
			isometricMatchdestroy();
			//#endif
			//#endif
			
			gfxMatchDestroy();
			gfxMatchShow = false;
//#endif
		}		
		else if (gameStatus == GAME_CONSOLE)
		{
//#ifndef REM_TEXTMATCH			
			league.userMatch[0].matchGoals = (byte)textMatchGoalsA;
			league.userMatch[1].matchGoals = (byte)textMatchGoalsB;
			
			textMatchDestroy();
//#endif			
			
		}
		else
		{			
			// SKIP MATCH DE TODO EL PARTIDO
			league.postSkipMatch();						
			
		}
		
		//#ifdef FEA_MATCH_REPORT
		reportText = textosCreate( loadFile("/"+langStr+"report.txt") );		
		//#endif
		
		
		if (!exhibitionFlag)
		{
						
			league.playJourney();
			
			//#ifdef DEBUG
			Debug.println("*** Winning bonus");
			//#endif
			// Winning bonus
		
			clubExpenses[EXPENSES_SQUADFEES] = 0;
		
			if(league.userMatch[0] == league.userTeam && league.userMatch[0].matchGoals > league.userMatch[1].matchGoals ||
			   league.userMatch[1] == league.userTeam && league.userMatch[1].matchGoals > league.userMatch[0].matchGoals) {
				//#ifndef RES_INGAMESOUNDS
				soundPlayEx(SOUND_WINNING);
				//#endif
				for(int i = 0; i < contracts.length; i++) {
				
					clubExpenses[EXPENSES_SQUADFEES] += contracts[i][CONTRACT_BONUS];			
				}
			}
			//#ifndef RES_INGAMESOUNDS
			else soundPlayEx(SOUND_LOSING);
			//#endif
		
			//#ifdef DEBUG
			Debug.println("*** Attendance");
			//#endif
											
			// Calc attendance
			
			try {
				
				if(league.userMatch[0].isUserTeam()) {
				
					int matchQuality = league.userMatch[0].getQuality() + league.userMatch[1].getQuality();
										
					int expensiveness =		
						(ticketPrices[TICKET_NORMAL] - (3*matchQuality / 4)) +
						(ticketPrices[TICKET_VIP] - (3*matchQuality / 2)) +
						(ticketPrices[TICKET_STANDING] - (2*matchQuality / 3)) +
						(ticketPrices[TICKET_PARKING] - (5));
					
					lastMatchAttendance = 3*getStadiumCapacity()/4 - (expensiveness * getStadiumCapacity()/250);
					
					if(lastMatchAttendance > getStadiumCapacity()) {
					
						lastMatchAttendance = getStadiumCapacity() - rnd(50);
					}
					
					if(lastMatchAttendance < 1) {
						
						lastMatchAttendance = 1;
					}
					// DAVRIK PREPROCESA ESTO
					//com.mygdx.mongojocs.lma2007.Debug.println("Match quality:"+matchQuality);
					//com.mygdx.mongojocs.lma2007.Debug.println("Expensiveness:"+expensiveness);
					
				} else {
				
					lastMatchAttendance = 0;
				}
//				 DAVRIK PREPROCESA ESTO
				//com.mygdx.mongojocs.lma2007.Debug.println("Match attendance:"+lastMatchAttendance+"/"+getStadiumCapacity());
				
			} catch(Exception e) {
				
				lastMatchAttendance = 0;
			}
			
			// Register 
			attendanceHistorial[league.currentWeek] = lastMatchAttendance;
			
//			 DAVRIK PREPROCESA ESTO
			//com.mygdx.mongojocs.lma2007.Debug.println("Current storing week:"+league.currentWeek);
			
			// Advance day
			
			//#ifdef DEBUG
			Debug.println("*** Next Journey");
			//#endif
		
			//System.out.println("matchGoalsA Despues "+league.userMatch[0].matchGoals);
			//System.out.println("matchGoalsB Despuies"+league.userMatch[1].matchGoals);
			league.nextJourney();
			//System.out.println("matchGoalsA Despues "+league.userMatch[0].matchGoals);
			//System.out.println("matchGoalsB Despuies"+league.userMatch[1].matchGoals);
		
		}
				
//#ifndef REM_TEXTMATCH		
		consoleDestroy();
//#endif

		consoleShow = false;

		gameStatus = GAME_PLAY_TICK;

		//#ifdef DEBUG
		Debug.println("*** End part");
		//#endif
		
		
 		menuInit(MENU_PLAY_MATCH_RESULT);
	break;


	
	case MENU_ACTION_TRANSFERS_SELL_CONFIRM:
		
		// Register selling players
		
		short aux[] = new short[40];
		int counter = 0;
		
		for(int i = 1; i < formListStr.length; i++) {
			
			if(!formListStr[i][0].equals("")) {
				
				//#ifdef DEBUG				
				Debug.println("Selling "+league.userTeam.playerIds[i - 1]);				
				//#endif
				
				aux[counter] = league.userTeam.playerIds[i - 1];
				counter++;
			}
			
		}
		
		sellingPlayers = new short[counter];
		
		System.arraycopy(aux,0,sellingPlayers,0,counter);
		
		if(counter > 0) {
		
			popupInitInfo(new String[] {substringReplace(getMenuText(MENTXT_SELL_SELECTION_INFO)[0], "[amount]", ""+counter)});
			popupWait();
			
		}
				
		menuRelease();

		listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
		menuExit = true;				
	break;
	
	case MENU_ACTION_TRANSFERS_BUY_CONFIRM:
						
		// Register selling players
		
		counter = 0;
		
		for(int i = 1; i < formListStr.length; i++) {
			
			if(formListStr[i][0].equals("")) {
								
				buyingPlayers = removeValueFromArray(buyingPlayers, buySelectionList[i - 1]);				
								
			} else {
				
				buyingPlayers = addValueToArray(buyingPlayers, buySelectionList[i - 1]);
				counter++;
			}
			
		}
									
		if(counter > 0) {
		
			popupInitInfo(new String[] {substringReplace(getMenuText(MENTXT_BUY_SELECTION_INFO)[0], "[amount]", ""+counter)});
			popupWait();
			menuRelease();
			listenerInit(SOFTKEY_MENU, SOFTKEY_NONE);
			menuExit = true;
			
		} else {
			
			menuRelease();
			menuInitBack();			
		}						
	break;
	
//#ifndef REM_FINANCES_EMPLOYEES	
	
	case MENU_ACTION_EMPLOYEE_INFO:
		
		if(autoManagement[AUTO_FINANCES_EMPLOYEE]) {
			
			popupInitAsk(new String[] {getMenuText(MENTXT_SWITCH_TO_EXPERT)[0]}, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
			popupWait();

			if (!popupResult){
				
				break;
				
			} else {
			
				autoManagement[AUTO_FINANCES_EMPLOYEE] = false;
			}
		}

		popupInitAsk(new String[] {getMenuText(MENTXT_EMPLOYEE_ACTIONS)[0]}, SOFTKEY_CANCEL, SOFTKEY_FIRE);
		popupWait();

		if (popupResult)
		{
			employees[formListPos - 1] = 0;
			menuRelease();
			menuInit(MENU_FINANCE_EMPLOYEE_LIST, formListPos);
		}		
	break;
	
	case MENU_ACTION_EMPLOYEE_CHOOSE:
				
		if(autoManagement[AUTO_FINANCES_EMPLOYEE]) {
			
			popupInitAsk(new String[] {getMenuText(MENTXT_SWITCH_TO_EXPERT)[0]}, SOFTKEY_CANCEL, SOFTKEY_ACEPT);
			popupWait();

			if (!popupResult){
							
				break;
				
			} else {
							
				autoManagement[AUTO_FINANCES_EMPLOYEE] = false;
			}
		}
						
		currentChoosingEmployee = formListPos - 1;
		
		menuRelease();
		menuInit(MENU_FINANCE_EMPLOYEE_CHOOSE, 1);
				
	break;	
	
	case MENU_ACTION_EMPLOYEE_HIRE:

		popupInitAsk(new String[] {getMenuText(MENTXT_EMPLOYEE_ACTIONS)[1]}, SOFTKEY_CANCEL, SOFTKEY_HIRE);
		popupWait();

		if (popupResult)
		{
			employees[currentChoosingEmployee] = (byte) formListPos;
			menuRelease();
			menuInit(MENU_FINANCE_EMPLOYEE_LIST, currentChoosingEmployee + 1);
		}		
	break;	
		
//#endif	
			
	case MENU_ACTION_JOURNEY_END:

		soundPlay(0,0);

	// Mostramos pantalla de loading/espera
		menuListTitleA = menuListTitleB = " ";
		formLoading();

		menuType = -1;



	//#ifdef DEBUG_TIME_PASSED
		showTimePassed("journeyEndProcess() start");
	//#endif

		journeyEndProcess();

	//#ifdef DEBUG_TIME_PASSED
		showTimePassed("journeyEndProcess() end");
	//#endif



	// Liberamos imagenes de los escudos de la memoria
		//#ifndef REM_TEAMSHIELDS
		matchShieldsImg[0] = null;
		matchShieldsImg[1] = null;
		//#endif
		
		
		if(league.currentWeek == league.leagueJourneys.length) {
			
			if(league.seasonGoalAcomplished()) {
				
				popupInitInfo(new String[] {getMenuText(MENTXT_SEASONGOAL_DONE)[1]});
				popupWait();
				
				
				//TBD: Enable bonus code
				//selectTeam(league.userTeam.globalIdx);
				//menuAction(MENU_ACTION_PLAY);
				gameStatus = GAME_PLAY_INIT;
				biosStatus = BIOS_GAME;
								
				//league.startSeason();
				
				//menuAction(MENU_ACTION_PLAY);
				menuInit(MENU_READY_TO_PLAY);
				
			} else {
			
				popupInitInfo(new String[] {getMenuText(MENTXT_SEASONGOAL_FAILED)[1]});
				popupWait();
				
				loadGame();
				
				gameStatus = GAME_MENU_START;
				biosStatus = BIOS_GAME;
																			
			}		
		
		} else {
			
			if(getAvailableCash() < 0) {
			
				popupInitInfo(new String[] {getMenuText(MENTXT_NO_MONEY)[0]});
				popupWait();
				loadGame();
				gameStatus = GAME_MENU_START;
				biosStatus = BIOS_GAME;
				
			} else {
	
			// Si autosave, guardamos juego
				if (gameAutosave)
				{
					saveGame();
				}
	
			
				gameStatus = GAME_PLAY_INIT;
				menuAction(MENU_ACTION_MENU_EXIT);
			}
		}
	break;
	
	case MENU_ACTION_LAPTOP_SELL:
		
		playerId = (short)messageData[currentMessage][0];
		
		if(league.userTeam.playerCount <= 16) {
			
			sellingPlayers = removeValueFromArray(sellingPlayers, playerId);
			popupInitBack(new String[] {getMenuText(MENTXT_TOO_FEW_PLAYERS)[0]});
			popupWait();
			
			deleteMessage(currentMessage);
			menuRelease();
			menuInitBack();
									
		} else {
		
			// Confirm and register player selling
						
			int amount = messageData[currentMessage][1];
			
			if(existsValueInArray(league.userTeam.playerIds, playerId)) {
			
				clubIncome[INCOME_TRANSFERS] += amount;
			
				popupInitBack(new String[] {substringReplace(substringReplace(getMenuText(MENTXT_SELL_CONFIRM)[0], "[player]", league.playerGetName(playerId)) ,"[money]", moneyStr(amount,true))});
				popupWait();
			
				formLoading();
				
				sellingPlayers = removeValueFromArray(sellingPlayers, playerId);
				
				league.userTeam.removePlayer(playerId);
				
				Team t = league.findTeamByQuality(league.playerGetQuality(playerId), league.userTeam);
				
				t.addPlayer(playerId);
				
				deleteContract(playerId);
							
				// TBD: Register transfer
			
				deleteMessage(currentMessage);
				
				for(int i = 0; i < messageCount;i++)
				{
					if (messageType[i] == MSG_NEGOTIATE_OFFER && messageData[i][0] == playerId)
						deleteMessage(i);
				}
				
			} else {
				
				popupInitBack(new String[] {getMenuText(MENTXT_SELL_CONFIRM)[1]});
				popupWait();
				deleteMessage(currentMessage);
			}
			menuRelease();
			menuInitBack();
		}
		break;

//#ifndef REM_TRANSFERS_NEGOTIATIONS
		
	case MENU_ACTION_LAPTOP_NEGOTIATE:
						
		playerId = (short)messageData[currentMessage][0];
		
		//if(existsValueInArray(league.userTeam.playerIds, playerId)) {
		
			menuInit(MENU_TRANSFERS_NEGOTIATE);
		/*	
		} else {
			
			sellingPlayers = removeValueFromArray(sellingPlayers, playerId);
			popupInitBack(new String[] {getMenuText(MENTXT_SELL_CONFIRM)[1]});
			popupWait();
			
			deleteMessage(currentMessage);
			menuRelease();
			menuInitBack();
			
		}*/
		break;		

//#endif



// ---===---
// Actions para MANAGER CUSTOMIZATION
// ---===---

	case MENU_ACTION_CUSTOMIZATION:

		switch (menuType)
		{
		case MENU_CUST_DIFFICULT:

			custDifficulty = formListPos;
			menuRelease();
			//#ifndef REM_FANTASY
			menuInit(gameMode != FANTASY? MENU_CUST_LEAGUE_TEAM:MENU_FANTASY_LEAGUE);
			//#else
			//#endif
			for (int i=0 ; i<5 ; i++)
			{
				autoManagement[i] = custDifficulty == 0;
			}
			
			//#ifndef REM_FANTASY
			if (gameMode == FANTASY)
			{
				userFormation = 2;	// Formacion por defecto: 4-4-2
			}

		    // Inicializamos Ids de los jugadores para fantasyTeam
			for (int i=0 ; i<fantasyPlayersId.length ; i++) {fantasyPlayersId[i] = -1;}
						
			if(gameMode == FANTASY) {
				
				fantasyTotalCost = 0;
								
				if (custDifficulty == 0)
					coachCash = 500000000;
				else
					coachCash = 350000000;
				fantasyName = gameText[TEXT_FANTASY_TEAM_NAME][0];
			}
			//#endif

		break;


		case MENU_CUST_LEAGUE_TEAM:
		case MENU_EXHI_LEAGUE_TEAM_A:
		case MENU_EXHI_LEAGUE_TEAM_B:

			if (formListPos == 0)
			{
			// Marcamos liga seleccionada
				xLeague = formListOpt();

			// Cargamos equipos de la liga seleccionada
				String[] str = new String[league.teams[xLeague].length];
				for (int i=0 ; i<str.length ; i++) {str[i] = league.teams[xLeague][i].name;}
				formListStr[1] = str;			// Nueva lista de equipos para la liga seleccionada
				xTeam = 0;
				formListDat[1][2] = xTeam;	// Opcion seleccionada por defecto
			} else {
				xTeam = formListOpt();
			}

		// Controlamos que el primer equipo seleccionado no aparezca en el listado del segundo
			if (menuType == MENU_EXHI_LEAGUE_TEAM_B && xLeague == exhiLeagueA && xTeam == exhiTeamA)
			{
				formListDat[1][2] += (keyX < 0? -1:1);

				if (formListDat[1][2] < 0) {formListDat[1][2] = formListStr[1].length-1;}
				else
				if (formListDat[1][2] >= formListStr[1].length) {formListDat[1][2] = 0;}
				xTeam = formListDat[1][2];
			}

		// cargamos escudo de la opcion seleccionada
			//#ifndef REM_TEAMSHIELDS
			formShieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.teams[xLeague][xTeam].flagColor);
			//#endif
		break;


	//#ifndef REM_MANAGER_CUSTOMIZATION
		case MENU_CUST_PREFERRED_CLUB:

			if (formListPos == 0)
			{
			// Marcamos liga seleccionada
				custPreferredClubLeague = formListOpt();

			// Cargamos equipos de la liga seleccionada
				String[] str = new String[league.teams[custPreferredClubLeague].length];
				for (int i=0 ; i<str.length ; i++) {str[i] = league.teams[custPreferredClubLeague][i].name;}
				formListStr[formListPos+1] = str;			// Nueva lista de equipos para la liga seleccionada
				custPreferredClubTeam = 0;
				formListDat[formListPos+1][2] = custPreferredClubTeam;	// Opcion seleccionada por defecto
			} else {
				custPreferredClubTeam = formListOpt();
			}
		break;


		case MENU_CUST_NATIONALITY:

			custNationality = formListDat[0][2];
		break;


		case MENU_CUST_AGE:

			custCurrentMonth = formListDat[0][2];
			custCurrentAge = formListDat[1][2];
			formTitleStr = new String[] {getMenuText(MENTXT_CUST_AGE)[0]+": "+ (2006 - custYearBase - custCurrentAge)};
		break;
	//#endif

		//#ifndef REM_FANTASY
		case MENU_FANTASY_LEAGUE:
			custLeague = formListOpt();			
		break;
		//#endif
		}
	break;


	case MENU_ACTION_SKILLS_CICLE:

		if ((keyX > 0 && custSkillPoints > 0 && formListNum[formListPos] < 10)
		||	(keyX < 0 && formListNum[formListPos] > 0))
		{
			formListNum[formListPos] += keyX;
			custSkillPoints -= keyX;

			formTitleStr = new String[] {getMenuText(MENTXT_CUST_SKILL_POINTS)[0] + " " + custSkillPoints};
			formShow = true;
		}
	break;


	case MENU_ACTION_TRANSFERS_SEARCH_CICLE:

		formCiclePos += keyX;
		if (formCiclePos >= formCicleStr.length) {formCiclePos -= formCicleStr.length;}
		else
		if (formCiclePos < 0) {formCiclePos += formCicleStr.length;}

		int last = formCiclePos;
		menuRelease(true);
		menuInit(MENU_TRANSFERS_TEAM_SELECT, last);
	break;
	
	case MENU_ACTION_PREMATCH_CICLE:
	case MENU_ACTION_PLAY_MATCH_RESULT_CICLE:
	case MENU_ACTION_CALENDAR_MATCHES_CICLE:

		formCiclePos += keyX;
		if (formCiclePos >= formCicleStr.length) {formCiclePos -= formCicleStr.length;}
		else
		if (formCiclePos < 0) {formCiclePos += formCicleStr.length;}

		formShow = true;
	break;	

//#ifndef REM_TRANSFERS_FILTER	
	case MENU_ACTION_FILTER_OPTIONS_CICLE:
			
		if(formListPos == 1) {
		
			filterLeague = (7 + filterLeague + keyX) % 7;
			formListStr[formListPos][1] = getMenuText(MENTXT_FILTER_LEAGUE)[filterLeague];
			formShow = true;
		}
		if(formListPos == 3) {
			
			//#ifndef REM_FANTASY
			if(menuType == MENU_TRANSFERS_FILTER_SEARCH || fantasyPlayerSelecting >= 11) {
			//#else
			//#endif
				
				filterPosition = (5 + filterPosition + keyX) % 5;			
				formListStr[formListPos][1] = getMenuText(MENTXT_FILTER_POSITION)[filterPosition];
				formShow = true;
			}
		}
		if(formListPos == 5) {
			
			filterMaxCost += 1000000 * keyX;
			
			if(filterMaxCost < 5000000) {
				
				filterMaxCost = 5000000;
			}
			
			if(filterMaxCost > 50000000) {
				
				filterMaxCost = 50000000;
			}
			
			formListStr[formListPos][1] = moneyStr(filterMaxCost,true);
			formShow = true;
		}
	break;
//#endif
	
//#ifndef REM_FILTER
	
	//#ifndef REM_FANTASY
	case MENU_ACTION_FANTASY_FILTER:
	//#endif	
	case MENU_ACTION_FILTERED_SEARCH:


		// Mostramos pantalla de loading
//			listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
			formLoading();
			formBodyMode = FORM_TABLE;
		
		// Construct buy selection list: Players matching search criteria
						
		short tempList[] = new short[FILTER_LIST_SIZE];
		int tempCount = 0;
		
		
		for(int j = 0; j < 22; j++) {
			
			for(int i = 0; i < league.teams.length; i++) {
				
				if(j >= league.teams[i].length) {
				
					continue;
				}
				//#ifndef REM_FANTASY						
				if(cmd == MENU_ACTION_FANTASY_FILTER || league.teams[i][j] != league.userTeam) {
				//#else
				//#endif
					
					for(int k = 0; k < league.teams[i][j].playerCount; k++) {
					
						playerId = league.teams[i][j].playerIds[k];
				
						//#ifndef REM_FANTASY
						if(tempCount < FILTER_LIST_SIZE &&
						   playerCost(playerId) < filterMaxCost &&
						   (league.playerGetPosition(playerId) == filterPosition || filterPosition == 4) &&
						   (i == filterLeague || filterLeague == 6) && 
						   !existsValueInArray(fantasyPlayersId,playerId)
						   
						) {
							
							tempList[tempCount] = playerId;
							tempCount++;
							
						}
						//#endif
					}
				}
			}						
		}
		
		if(tempCount == 0) {
			
			// No players found
			
			popupInitBack(new String[] {getMenuText(MENTXT_FILTER_NO_PLAYERS_FOUND)[0]});
			popupWait();
			
														
		} else {
			
			buySelectionList = new short[tempCount];
			
			System.arraycopy(tempList,0,buySelectionList,0,tempCount);
			
			if(tempCount > PLAYER_LIST_MAX) {
				
				popupInitInfo(new String[] {getMenuText(MENTXT_FILTER_TOO_MANY_PLAYERS)[0]});
				popupWait();

				formLoading();
				formBodyMode = FORM_TABLE;
			}
			
			menuRelease();
			
			//#ifndef REM_FANTASY			
			if(cmd == MENU_ACTION_FILTERED_SEARCH) {
				
				menuInit(MENU_TRANSFERS_PLAYER_SELECT);
				
			} else {
				
				menuInit(MENU_FANTASY_PLAYER_SELECT);
			}
			//#else
			//#endif
		}				
	break;	
//#endif

//#ifndef REM_FANTASY	
	case MENU_ACTION_FANTASY_PLAYER_CONFIRM:
		
		fantasyTotalCost = 0;
		
		for(int i = 0; i < fantasyPlayersId.length; i++) {
			
			if(fantasyPlayersId[i] >= 0) {
				
				fantasyTotalCost += playerCost(fantasyPlayersId[i]); 
			}
		}
		
		if (fantasyPlayersId[fantasyPlayerSelecting] >= 0) fantasyTotalCost -= playerCost(fantasyPlayersId[fantasyPlayerSelecting]); 
		
		// No enough money for buying this player
		
		if(fantasyTotalCost + playerCost(buySelectionList[formListPos - 1]) >= getAvailableCash()) {
			
			popupInitBack(new String[] {getMenuText(MENTXT_BUY_PLAYER_NOT_ENOUGH_CASH)[0]});
			popupWait();
			
		} else {
		
			// Do buy
			
			fantasyTotalCost += playerCost(buySelectionList[formListPos - 1]);
			fantasyPlayersId[fantasyPlayerSelecting] = buySelectionList[formListPos - 1];		
			menuInitBack();
		}
	break;
//#endif
//#ifndef REM_TRANSFERS_SCOUT
	
	case MENU_ACTION_SCOUT_PLAYERLIST:
					
		if(employees[EMPLOYEES_SCOUT] <= 0) {
		
			popupInitBack(new String[] {getMenuText(MENTXT_SCOUT_MSG)[2]});
			popupWait();
					
			menuAction(MENU_ACTION_MENU_EXIT);
			break;
		}
		
		// Construct buy selection list: Players good for our team
		
		tempList = new short[PLAYER_LIST_MAX];		
		tempCount = 0;		
		int myTeamQuality = league.userTeam.getQuality();
		
		for(int i = 0; i < league.teams.length; i++) {
		
			for(int j = 0; j < league.teams[i].length; j++) {
				
				if(league.teams[i][j] != league.userTeam) {
				
					for(int k = 0; k < league.teams[i][j].playerCount; k++) {
					
						playerId = league.teams[i][j].playerIds[k];
						
						if(tempCount < PLAYER_LIST_MAX && Math.abs(league.playerGetQuality(playerId) - myTeamQuality + 3) < 1) {
							
							if(league.currentWeek % employees[EMPLOYEES_SCOUT] == k % employees[EMPLOYEES_SCOUT]) {
							
								tempList[tempCount] = playerId;
								tempCount++;
							
							}							
						}
					}
				}
			}						
		}
		
		if(tempCount == 0) {
			
			// No players found
			
			popupInitBack(new String[] {getMenuText(MENTXT_SCOUT_MSG)[3]});
			popupWait();
			
			menuAction(MENU_ACTION_MENU_EXIT);
																	
		} else {
			buySelectionList = new short[tempCount];
			
			System.arraycopy(tempList,0,buySelectionList,0,tempCount);
												
			popupInitInfo(new String[] {substringReplace(getMenuText(MENTXT_SCOUT_MSG)[4],"[amount]",""+tempCount)});
			popupWait();
			
			menuRelease(true);			
					
			menuInit(MENU_TRANSFERS_PLAYER_SELECT);
									
		}
	break;
	
//#endif	
	

	case MENU_ACTION_PLAY_MATCH_RESULT_ACCEPT:
		int opt = formCiclePos;

		menuRelease();

		switch (opt)
		{
		case 0:

			//#ifdef FEA_MATCH_REPORT
			reportText = null;
			matchReport = null;
			//System.out.println("PONGO A NUL LOS TEXTOS");
			//#endif
			if (exhibitionFlag)
			{
				soundPlay(0,0);
			// Salimos del juego al menu principal
				biosStatus = BIOS_GAME;
				gameStatus = GAME_MENU_MAIN;
			} else {
			// Salimos a menu del juego (menuBar)
				menuAction(MENU_ACTION_JOURNEY_END);
			}
		break;

		case 1:
			menuInit(MENU_STATS_MATCH);
		break;

		case 2:
			menuInit(MENU_STATS_LEAGUE);
		break;

		case 3:
			menuInit(MENU_STATS_EUROPE);
		break;
		}
	break;


	case MENU_ACTION_SQUAD_FORMATION_ACEPT:

		userFormation = formListPos;

		menuRelease();
		menuInitBack();
	break;


	case MENU_ACTION_SQUAD_STYLE_ACEPT:

		squadStyle = formListPos;

		menuRelease();
		menuInitBack();
	break;

//#ifndef REM_SQUAD_TACTICS
	case MENU_ACTION_SQUAD_TACTICS_ACEPT:

		squadTactic = formListPos;
		//com.mygdx.mongojocs.lma2007.Debug.println("STACTIC     "+squadTactic);
		switch(squadTactic)
		{
			case TACTIC_DEFENSE:
				facCloseToSide = 0;
				facCloseToEnemy = -200;
				facCloseToGoal = -100;
				facPress = 0;
				break;
			case TACTIC_FOCUS:
				facCloseToSide = 0;
				facCloseToEnemy = 0;
				facCloseToGoal = 0;
				facPress = 0;				
				break;
			case TACTIC_PASSING:
				facCloseToSide = 100;
				facCloseToEnemy = -300;
				facCloseToGoal = 200;
				facPress = 0;				
				break;
			case TACTIC_ATTACKING:
				facCloseToSide = 100;
				facCloseToEnemy = 0;
				facCloseToGoal = 500;
				facPress = 0;				
				break;
			case TACTIC_DISTRIBUTION:
				facCloseToSide = 500;
				facCloseToEnemy = 0;
				facCloseToGoal = 0;
				facPress = 0;				
				break;
 
		}
		menuRelease(true);
		menuInit(menuType, formListPos);
		//menuRelease();
		//menuInitBack();
	break;
//#endif

	case MENU_ACTION_TRAINING_STYLE_ACEPT:

		if(autoManagement[AUTO_TRAINING_STYLE])
		{
			popupInitAsk(getMenuText(MENTXT_SWITCH_TO_EXPERT), SOFTKEY_CANCEL, SOFTKEY_ACEPT);
			popupWait();

			if (!popupResult)
			{
				break;
			} else {
				autoManagement[AUTO_TRAINING_STYLE] = false;
			}
		}

		trainingStyle = formListPos;

		menuRelease();
		menuInitBack();
	break;


	case MENU_ACTION_SQUAD_SELECT_TEAM_ACEPT:

	// Si estamos en menu para editar nombres, saltamos al editor
		if (menuType == MENU_SQUAD_EDIT_NAMES)
		{
			playerSelectedToChangeName = formListPos-1;
			menuRelease();
			menuInit(MENU_SQUAD_EDIT_NAME);
			break;
		}
		

		//#if !REM_2DMATCH || !REM_TEXTMATCH 
	// Si estamos jugando... Controlamos si puedes hacer mas sustituciones
		if(playingMatch && substitutionsLeft == 0) {
		
			popupInitBack(new String[] {getMenuText(MENTXT_SUBSTITUTE_INFO)[0]});
			popupWait();
			break;
		}
		//#endif

	// Acabamos de seleccionar un jugador para ser intercambiado...
		if (formSwapSelected < 0)
		{
			if (menuType == MENU_SQUAD_CHANGE_NEEDED)
			{
				playerId = league.userTeam.playerIds[formListPos-1];
				boolean noJuega = league.userPlayerStats[league.extendedPlayer(playerId)][league.SANCTION_JOURNEYS] > 0
				|| league.userPlayerStats[league.extendedPlayer(playerId)][league.INJURY_JOURNEYS] > 0;
	
				if(!noJuega)
				{
					popupInitBack(new String[] {getMenuText(MENTXT_SUBSTITUTE_INFO)[1]});
					popupWait();
					break;
				}
			}
			
			if(menuType == MENU_SQUAD_INJURED_SUBS_NEEDED) {

				playerId = league.userTeam.playerIds[formListPos-1];
				boolean noJuega = league.userPlayerStats[league.extendedPlayer(playerId)][league.INJURY_JOURNEYS] > 0;
	
				if(!noJuega)
				{
					popupInitBack(new String[] {getMenuText(MENTXT_SUBSTITUTE_INFO)[2]});
					popupWait();
					break;
				}
			}

			formSwapSelected = formListPos;

		// Marcamos el jugador seleccionado de color naranja
			formListDat[formListPos][0] |= (FONT_ORANGE<<8);

			listenerInit(SOFTKEY_CANCEL, SOFTKEY_CHANGE);
			formShow = true;

		} else {
			
			// Si estamos jugando y se intenta substituir a un expulsado, avisamos
			//#if !REM_2DMATCH || !REM_TEXTMATCH 
			if(playingMatch && (formListPos - 1 >= 11 && league.playerSanctioned(league.userTeam.playerIds[formSwapSelected - 1]) ||
						        formSwapSelected - 1 >= 11 && league.playerSanctioned(league.userTeam.playerIds[formListPos - 1]))) {
				
				formSwapSelected = -1;
				popupInitBack(new String[] {getMenuText(MENTXT_SUBSTITUTE_INFO)[4]});
				popupWait();
				break;
			}
	
			
			
			
			// Si estamos jugando y se intenta poner a un lesionado de nuevo en juego, avisamos
			
			if(playingMatch && (formSwapSelected - 1 < 11 && league.playerInjured(league.userTeam.playerIds[formListPos - 1]) ||
							    formListPos - 1 < 11 && league.playerInjured(league.userTeam.playerIds[formSwapSelected - 1]))) {
			/*
			if(playingMatch && 
				(formSwapSelected - 1 < 11 && !league.playerCanPlay(league.userTeam.playerIds[formListPos - 1] && formListPos - 1 >= 11) 
						||
				formListPos - 1 < 11 && !league.playerCanPlay(league.userTeam.playerIds[formSwapSelected - 1]))) {
				*/
				formSwapSelected = -1;
				popupInitBack(new String[] {getMenuText(MENTXT_SUBSTITUTE_INFO)[5]});
				popupWait();
				break;
			}
			
			// Si se intenta substituir a un jugador por otro que ya hubiese sido retirado del campo, avisamos
			
			if(playingMatch && (formSwapSelected - 1 < 11 && existsValueInArray(substitutedPlayers, league.userTeam.playerIds[formListPos - 1]) ||
							    formListPos - 1 < 11 && existsValueInArray(substitutedPlayers, league.userTeam.playerIds[formSwapSelected - 1]))) {
				
				formSwapSelected = -1;
				popupInitBack(new String[] {getMenuText(MENTXT_ALREADY_SUBBED_PLAYER)[0]});
				popupWait();
				break;					
			}
			
			
			//	 se quiere meter a un paria en el campo
			
			if(playingMatch && (formListPos - 1 >= 11 && !league.playerCanPlay(league.userTeam.playerIds[formListPos - 1]) ||
			        formSwapSelected - 1 >= 11 && !league.playerCanPlay(league.userTeam.playerIds[formSwapSelected - 1]))) {
	
				formSwapSelected = -1;
				popupInitBack(new String[] {getMenuText(MENTXT_SUBSTITUTE_INFO)[3]});
				popupWait();
				break;
			}

			
			//#endif
			
		// -----------------------------------------------------------
		// Aplicamos el intercambio de los dos jugadores seleccionados
		// ===========================================================
			short swap = league.userTeam.playerIds[formSwapSelected - 1];
			league.userTeam.playerIds[formSwapSelected - 1] = league.userTeam.playerIds[formListPos - 1];
			league.userTeam.playerIds[formListPos - 1] = swap;
			
			if(menuType != MENU_SQUAD_CHANGE_NEEDED && menuType != MENU_SQUAD_INJURED_SUBS_NEEDED && playingMatch && ((formListPos - 1 >= 11) != (formSwapSelected - 1 >= 11))) {
				
				substitutionsLeft--;
			}

			boolean applyChange = true;
			String name1 = null, name2 = null;

		// Si la nueva posicion para el primer jugador no es adecuada, avisamos con popup, (ej: un delantero lo ponemos como portero)
			if (formSwapSelected-1 < 11)
			{
				int currentSel = formSwapSelected-1;
				int currentPos = 0;
				for (int i=formCampoEjes[userFormation].length-1 ; i>=0 ; i--)
				{
					if ((currentSel -= (formCampoEjes[userFormation][i].length/2)) < 0) {break;}
					currentPos++;
				}

				if (currentPos != league.playerGetPosition(league.userTeam.playerIds[formSwapSelected-1]))
				{
					name1 = league.playerGetName(league.userTeam.playerIds[formSwapSelected-1]);
//					popupInitAsk(null, new String[] {"Mala posicion para "+league.playerGetName(league.userTeam.playerIds[formSwapSelected-1]),"-25% de eficacia"}, SOFTKEY_NONE, SOFTKEY_SELECT);
//					popupWait();
				}
			}


		// Si la nueva posicion para el segundo jugador no es adecuada, avisamos con popup, (ej: un delantero lo ponemos como portero)
			if (formListPos-1 < 11)
			{
				int currentSel = formListPos-1;
				int currentPos = 0;
				for (int i=formCampoEjes[userFormation].length-1 ; i>=0 ; i--)
				{
					if ((currentSel -= (formCampoEjes[userFormation][i].length/2)) < 0) {break;}
					currentPos++;
				}

				if (currentPos != league.playerGetPosition(league.userTeam.playerIds[formListPos-1]))
				{
					name2 = league.playerGetName(league.userTeam.playerIds[formListPos-1]);
//					popupInitAsk(null, new String[] {"Mala posicion para "+league.playerGetName(league.userTeam.playerIds[formListPos-1]),"-25% de eficacia"}, SOFTKEY_NONE, SOFTKEY_SELECT);
//					popupWait();
				}
			}

		// Si algun queda mala posicion lo notificamos y pedimos confirmacion
			if (name1 != null || name2 != null)
			{
				if (name1 != null && name2 != null)
				{
					popupInitAsk(new String[] {getMenuText(MENTXT_LST_BAD_POSITION)[0] + " " + name1 + " " + getMenuText(MENTXT_LST_BAD_POSITION)[1] + " " + name2, "-50% " + getMenuText(MENTXT_LST_BAD_POSITION)[2], getMenuText(MENTXT_LST_BAD_POSITION)[3]}, SOFTKEY_NO, SOFTKEY_YES);
				} else {
					popupInitAsk(new String[] {getMenuText(MENTXT_LST_BAD_POSITION)[0] + " " + (name1 != null? name1:name2), "-25% " + getMenuText(MENTXT_LST_BAD_POSITION)[2], getMenuText(MENTXT_LST_BAD_POSITION)[3]}, SOFTKEY_NO, SOFTKEY_YES);
				}
				popupWait();
				applyChange = popupResult;
			}

		// Deshacemos el cambio si se responde no al popup
			if (!applyChange)
			{
				swap = league.userTeam.playerIds[formSwapSelected - 1];
				league.userTeam.playerIds[formSwapSelected - 1] = league.userTeam.playerIds[formListPos - 1];
				league.userTeam.playerIds[formListPos - 1] = swap;
				
//#if !REM_2DMATCH || !REM_TEXTMATCH 				
				if(menuType != MENU_SQUAD_CHANGE_NEEDED && menuType != MENU_SQUAD_INJURED_SUBS_NEEDED && playingMatch && (formListPos - 1 >= 11 || formSwapSelected - 1 >= 11)) {
					
					substitutionsLeft++;
										
				}				
//#endif				
				
			} else {
					
//#if !REM_2DMATCH || !REM_TEXTMATCH 	
				
				if(menuType != MENU_SQUAD_CHANGE_NEEDED && menuType != MENU_SQUAD_INJURED_SUBS_NEEDED && playingMatch && ((formListPos - 1 >= 11) != (formSwapSelected - 1 >= 11))) {
																							
					if(formListPos - 1 < 11) {
																	
						substitutedPlayers = addValueToArray(substitutedPlayers, league.userTeam.playerIds[formSwapSelected - 1]);
					}
					
					if(formSwapSelected - 1 < 11) {
																		
						substitutedPlayers = addValueToArray(substitutedPlayers, league.userTeam.playerIds[formListPos - 1]);
					}				
					
					/*
					System.out.println(""+substitutedPlayers.length);
					
					for(int i = 0; i < substitutedPlayers.length; i++) {
					
						System.out.println("Substituted players: "+league.playerGetName(substitutedPlayers[i]));
					}
					*/
				}
//#endif					
			}
		// ===========================================================


			menuRelease(true);
			menuInit(menuType, formListPos);
		}
	break;

	case MENU_ACTION_SQUAD_SELECT_TEAM_CANCEL:

		if (menuType != MENU_SQUAD_EDIT_NAMES) 
		if ((menuType == MENU_SQUAD_INJURED_SUBS_NEEDED && !playerSquadNoInjured()) || (menuType != MENU_SQUAD_INJURED_SUBS_NEEDED && !playingMatch && !playerSquadOk()))
		{
			popupInitBack(new String[] {getMenuText(MENTXT_SUBSTITUTE_INFO)[3]});
			popupWait();
			break;
		}

		if (formSwapSelected < 0)
		{
		// Si es la pantalla de cambiar lesionados tras haver ido al calendario, saltamos directamente a este...
			if (menuType == MENU_SQUAD_CHANGE_NEEDED)
			{
				menuRelease(true);
			//#ifndef REM_CALENDAR
				menuInit(MENU_CALENDAR_MATCHES);
			//#else
			//#endif
				break;
			}
			menuListTitleA = menuListTitleB = null;
			menuAction(MENU_ACTION_BACK);
		} else {

		// DES-Marcamos el jugador seleccionado de color naranja
			formListDat[formSwapSelected][0] &= (FONT_ORANGE<<8)*-1;

			formSwapSelected = -1;
			listenerInit((menuType == MENU_SQUAD_CHANGE_NEEDED? SOFTKEY_CONTINUE:SOFTKEY_BACK), SOFTKEY_SELECT);

			formShow = true;
		}
	break;


	case MENU_ACTION_CALENDAR_MATCHES_ACCEPT:

		//com.mygdx.mongojocs.lma2007.Debug.println("DEB 0");
		league.preMatch();
		autoManageSetPrices();
		
	// Generamos escudos con sus colores correctos
		//com.mygdx.mongojocs.lma2007.Debug.println("DEB 1");
		
		//#ifndef REM_TEAMSHIELDS
		int it = 0;
		while (it < 2)
		{
			if (league.userMatch[it].isUserTeam())
				matchShieldsImg[it] = shieldImg;
			else
				matchShieldsImg[it] = changePal(loadFile("/escudos.png"), shieldRgbs, league.userMatch[it].flagColor);
			it++;
		}
		//#endif
		//com.mygdx.mongojocs.lma2007.Debug.println("DEB 2");
		/*
		if (league.userMatch[1].isUserTeam())
			matchShieldsImg[1] = shieldImg;	
		else
			matchShieldsImg[1] = changePal(loadFile("/escudos.png"), shieldRgbs, league.userMatch[1].flagColor);
			*/				
		//com.mygdx.mongojocs.lma2007.Debug.println("DEB 3");

	//#ifndef REM_CALENDAR
		menuRelease();
	//#endif

		menuInit(MENU_PREMATCH);
	break;



	case MENU_ACTION_TRAINING_SCHEDULE_AUTO:
		
		// Carlos mio, aqui tienes que programar el modo AUTO si tienes el coatch
		autoTrainingSchedule();
				
		menuRelease(true);
		menuInit(menuType);

	break;

//#ifndef REM_STADIUM
	case MENU_ACTION_STADIUM_UPGRADE:
		
		int facility = formListPos-1;
		int level = stadiumLevel[facility];
		int cost = stadiumCost[facility][level];
		
		// Comprovar si el nivel es el maximo
		if (stadiumLevel[facility] == 4)
		{
			popupInitBack(new String[] {getMenuText(MENTXT_STADIUM_FACILITIES)[facility]+" "+getMenuText(MENTXT_CANNOT_BE_IMPROVED_FURTHER)[0]});
			popupWait();
			
			break;
		}
		
        // Comprovar si ya hay upgrade haciendose
		if (stadiumBuildStage[facility] > 0)
		{
			popupInitAsk(new String[] {getMenuText(MENTXT_UNDER_CONSTRUCTION_WEEKS)[0]+" "+(stadiumBuildTime[facility][level]-stadiumBuildStage[facility]+1)+" "+getMenuText(MENTXT_UNDER_CONSTRUCTION_WEEKS)[1]
				+getMenuText(MENTXT_UNDER_CONSTRUCTION_WEEKS)[2]
				}, SOFTKEY_NO, SOFTKEY_YES);
			popupWait();

			if (popupResult)
			{
				// Cancelar Construccion
				stadiumBuildStage[facility] = 0;
				coachCash += cost/2;
				menuRelease(true);
				menuInit(menuType, formListPos);
				
			}
			break;
		}		
		
		// Comprovar si hay pasta
		if (cost > getAvailableCash())
		{
			popupInitBack(getMenuText(MENTXT_NOT_ENOUGH_MONEY));
			popupWait();
			
			break;
		}
		
		
		
		popupInitAsk(new String[] {				
				getMenuText(MENTXT_IMPROVING_WORK_WEEKS)[0]+
				stadiumBuildTime[facility][level]+
				getMenuText(MENTXT_IMPROVING_WORK_WEEKS)[1]+
				moneyStr(cost, true)+
				getMenuText(MENTXT_IMPROVING_WORK_WEEKS)[2]}, SOFTKEY_NO, SOFTKEY_YES);
		popupWait();
			
		if (!popupResult) {break;}

		
		// Empezar a construir
		stadiumBuildStage[facility] = 1;
		clubExpenses[EXPENSES_FACILITIES] += cost;
		
		menuRelease(true);
		menuInit(menuType, formListPos);
		
		break;
//#endif

//#ifndef REM_BONUS_CODES
		case MENU_ACTION_BONUS_CODES_ACCEPT:

			if (formListPos < checkBonusCodes.length)
			{
				/*if (!checkBonusCodes[formListPos])
				{
					popupInitBack(new String[] {getMenuText(MENTXT_LST_NEEDED_TO_UNLOCK)[formListPos]});
					popupWait();
				} else*/ {
					bonusCodes[formListPos] = (byte)(bonusCodes[formListPos]!=0? 0:1);
					formListDat[formListPos][0] = (bonusCodes[formListPos]==0? SOFTKEY_ENABLE:SOFTKEY_DISABLE)<<16;
					formShow = true;
				}
			} else {
				menuAction(formListDat[formListPos][1]);
			}
		break;
//#endif

//#ifndef REM_FANTASY
		case MENU_ACTION_FANTASY_CUSTOMIZATION_ACCEPT:

			boolean faltanJugadores = false;
			for (int i=0 ; i<fantasyPlayersId.length ; i++) {if (fantasyPlayersId[i] < 0) {faltanJugadores = true; break;}}

			if (faltanJugadores)
			{
				popupInitAsk(getMenuText(MENTXT_NOT_ENOUGH_PLAYERS_IN_YOUR_SQUAD), SOFTKEY_NO, SOFTKEY_YES);
				popupWait();

				if (!popupResult) {break;}

		// TODO: Asignamos jugadores con id: -1

			// Mostramos pantalla de loading
				listenerInit(SOFTKEY_NONE, SOFTKEY_NONE);
				formLoading();
				
				boolean result = fantasyTeamAutoComplete();
				
				if(!result) {
					
					popupInitBack(new String[] {getMenuText(MENTXT_AUTOCOMPLETION_FAILED)[0]});
					popupWait();
					menuRelease(true);
					menuInit(menuType);
					//MENU_FANTASY_CUSTOMIZATION
					break;
				}
			}
					
			menuRelease();
			menuInit(MENU_CUST_MANAGER);
		break;




		case MENU_ACTION_FANTASY_CHOOSE_PLAYERS_CANCEL:

			menuRelease();
			menuInitBack();
		break;


		case MENU_ACTION_FANTASY_CHOOSE_PLAYERS_ACCEPT:

			fantasyPlayerSelecting = formListPos - 1;
			
			switch(fantasyPlayerSelecting) {
			
				case 0 : filterPosition = 0; break;
				case 1 : 
				case 2 : 
				case 3 : 
				case 4 : filterPosition = 1; break;
				case 5 : 
				case 6 : 
				case 7 : 
				case 8 : filterPosition = 2; break;
				case 9 : 
				case 10: filterPosition = 3; break;
				default: filterPosition = 4; break;				
			}
			
			menuRelease();
			//menuInit(MENU_FOO);
			menuInit(MENU_FANTASY_FILTER);
		break;



		case MENU_ACTION_FANTASY_COLOUR_CICLE:

			fantasyRGBidx[formListPos] += keyX;

			if (fantasyRGBidx[formListPos] < 0) {fantasyRGBidx[formListPos] = (byte)(fantasyRGB.length-1);}
			else
			if (fantasyRGBidx[formListPos] >= fantasyRGB.length) {fantasyRGBidx[formListPos] = 0;}

		// cargamos escudo con los nuevos colores
			//#ifndef REM_TEAMCOLORS
			league.userTeam.flagColor[0] = fantasyRGB[fantasyRGBidx[0]];
			league.userTeam.flagColor[1] = fantasyRGB[fantasyRGBidx[1]];
			//#endif
			//#ifndef REM_TEAMSHIELDS
			formShieldImg = changePal(loadFile("/escudos.png"), shieldRgbs, league.userTeam.flagColor);
			//#endif
			formShow = true;
		break;
//#endif


		case MENU_ACTION_LAPTOP_CICLE:

			if (formListPos < messageCount && keyX > 0)
			{
				popupInitAsk(getMenuText(MENTXT_ARE_YOU_SURE_YOU_WANT_TO_DELETE_THIS_MAIL), SOFTKEY_NO, SOFTKEY_YES);
				popupWait();

				if (popupResult)
				{
					deleteMessage(formListPos);

					menuRelease(true);
					menuInit(menuType, formListPos);
				}
			}
		break;
		
		case MENU_ACTION_SPONSOR_SWITCH_TO_EXPERT:
			
			if(autoManagement[AUTO_FINANCES_SPONSORS])
			{
				popupInitAsk(getMenuText(MENTXT_SWITCH_TO_EXPERT), SOFTKEY_CANCEL, SOFTKEY_ACEPT);
				popupWait();

				if (!popupResult) {
					
					break;
															
				} else {
					
					sponsors[SPONSOR_SHIRTS][1] = 0;
					sponsors[SPONSOR_FENCES][1] = 0;
					
					autoManagement[AUTO_FINANCES_SPONSORS] = false;
					
					menuRelease(true);
					menuInit(menuType, formListPos);
					
				}
			}
									
		break;
// ---===---
// ---===---
	}

}






// ------------ NO TOCAR -----------
//#endif
//#ifndef BUILD_ONE_CLASS
	};
//#endif
// ------------ NO TOCAR -----------
