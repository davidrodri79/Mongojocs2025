package com.mygdx.mongojocs.poyoolimpix;


public class Game extends Bios
{

// -------------------
// game Tick
// ===================
//int last=0;
public void gameTick()
{
	//System.out.println(gameStatus+" "+panelMove);
	if (playPaused==false)
	{
		if ((sinoidal<=0)||(sinoidal>=5)) incSinus *= -1;
		sinoidal += incSinus;
	}
	playPaused = true;
	if (timeToChange>0) timeToChange--;
	if (panelMove != 0) 
	{
//		System.out.println("panel");
		playShow = true;
		panelPos += panelMove;
		if (gameStatus==GAME_COUNTDOWN) timeToChange++;
		if (((keyMenu!=0)&&(lastKeyMenu==0))||(((panelPos-panelMove)>panelMaxPos)!=(panelPos>panelMaxPos)))
		{
			panelMove = 0;
			panelPos = panelMaxPos;
			if (panelAct==false) 
			{
				gc.PintaMapa = false;
				panelTitle = "";
				gc.TempString = "";
				panelInfo = NULL_TEXT;
				gc.PintaEvent = -1;
			} else { if (panelType>0) gc.vibraInit(50); }
		}
	} else
	{
//		if (last!=gameStatus) System.out.println("gameStatus: "+gameStatus);
//		last=gameStatus;
		gc.DespY = 0;
		switch (gameStatus)
		{
		case GAME_LOGOS:
			logosInit(1, new int[] {0xffffff}, 2000);
			gameStatus = GAME_MENU_INIT_MAIN;
			menuImg = gc.Imagenes[0][1];
			initData();
			loadData();
		break;
			
		case GAME_MENU_INIT_MAIN:
			gameStatus = GAME_MENU_MAIN;
			gc.DescargaEscena();
			gc.DescargaPlayers();
			gc.DescargaMenu();
			gc.DescargaMenuImg();
			System.gc();
			gc.CargaMenu();
			gc.CargaMenuImg();
			gc.CargaBanderas();
			gc.ActAnim = ANIM_ESTATIC;
			setPanel(0);
			gc.soundPlay(0,0);
		break;
			
		case GAME_MENU_MAIN:
			menuInit(MENU_MAIN);
		break;
	
		case GAME_TRY_END :
			String Resto = "";
			playShow = true;
			playPaused = false;
			switch (ActualEvent)
			{
				case 0 :
					ProtVX = ProtMaxV;
					playTick();
					if (ProtX>12500)
					{
						int TiempoCarrera = 30000;
						if (NumEnems>0)
						{
							for (int i=0; i<NumEnems; i++)
							{
								TiempoCarrera = Math.min(TiempoCarrera,Enems[i][4]);
							}
						}
						MaxScore = ActScore[0];
						if ((MaxScore<=TiempoCarrera)&&(gameType!=3)) SuficientScore= true;
						//#ifndef SMALLCANVAS
						//#ifdef SMALLTEXT
						//#else
						Resultados = new String[4+NumEnems];
						Resultados[0] = gameText[TEXT_ACT_TIME][((SuficientScore)?1:0)];
						Resultados[1] = "";
						if ((ActScore[0]%1000) < 100) Resto = "0";
						if ((ActScore[0]%1000) < 10) Resto = "00";
						Resultados[2] = gameText[TEXT_PLAYER_INFO][ActualPlayer]+": "+(ActScore[0]/1000)+"."+Resto+ActScore[0]%1000+" s";
						for (int i=0; i<NumEnems; i++)
						{
							Resto = "";
							if ((Enems[i][4]%1000) < 100) Resto = "0";
							if ((Enems[i][4]%1000) < 10) Resto = "00";
							Resultados[3+i] = gameText[TEXT_PLAYER_INFO][Enems[i][5]]+": "+(Enems[i][4]/1000)+"."+Resto+Enems[i][4]%1000+" s";
						}
						Resultados[Resultados.length-1] = (((MaxScore>0)&&((MaxScore<MaxScores[ActualEvent])||(MaxScores[ActualEvent]<=0)))?gameText[TEXT_NEW_RECORD][0]:"");
						//#endif
						//#else
						//#endif
						//#ifdef MO-C450
						//#endif
						if ((MaxScore>0)&&((MaxScore<MaxScores[ActualEvent])||(MaxScores[ActualEvent]<=0))) MaxScores[ActualEvent]=MaxScore;
						gameStatus = GAME_PRE_EVENT_END;
					}
				break;
				case 1 :
					playTick();
					if ((Caer)&&(timeToChange==0)&&(ProtVX==0))
					{
						ActualTry++;
						if (ActualTry<3) gameStatus = GAME_INIT; else
						{
							MaxScore = Math.max(Math.max(ActScore[0],ActScore[1]),ActScore[2]);
							if ((MaxScore>=550)&&(gameType!=3)) SuficientScore= true;
							//#ifndef SMALLCANVAS
							Resultados = new String[] {(((MaxScore>=550)&&(MaxScore>MaxScores[ActualEvent]))?gameText[TEXT_NEW_RECORD][0]:gameText[TEXT_ACT_LONG][0]),
										   "",
										   gameText[TEXT_TRY][0]+1+": "+((ActScore[0]>0)?(ActScore[0]/100+"."+(((ActScore[0]%100)<10)?"0":"")+(ActScore[0]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+2+": "+((ActScore[1]>0)?(ActScore[1]/100+"."+(((ActScore[1]%100)<10)?"0":"")+(ActScore[1]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+3+": "+((ActScore[2]>0)?(ActScore[2]/100+"."+(((ActScore[2]%100)<10)?"0":"")+(ActScore[2]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   ((MaxScore>0)?(gameText[TEXT_MAX_SCORE][((SuficientScore)?0:1)]):""),
										   ((MaxScore>0)?(MaxScore/100+"."+(((MaxScore%100)<10)?"0":"")+(MaxScore%100)+" m"):"")};
							//#else
							//#endif
							//#ifdef MO-C450
							//#endif
							if ((MaxScore>=550)&&(MaxScores[ActualEvent]<MaxScore)) MaxScores[ActualEvent]=MaxScore;
							gameStatus = GAME_PRE_EVENT_END;
						}
					}
				break;
				case 2 :
					playTick();
					if (((ProtVX==0)&&((ItemY==0)||(Caer)))&&(timeToChange==0)) 
					{
						ActualTry++;
						if (ActualTry<3) gameStatus = GAME_INIT; else
						{
							MaxScore = Math.max(Math.max(ActScore[0],ActScore[1]),ActScore[2]);
							if ((MaxScore>=7800)&&(gameType!=3)) SuficientScore= true;
							//#ifndef SMALLCANVAS
							Resultados = new String[] {(((MaxScore>=7800)&&(MaxScore>MaxScores[ActualEvent]))?gameText[TEXT_NEW_RECORD][0]:gameText[TEXT_ACT_LONG][0]),
										   "",
										   gameText[TEXT_TRY][0]+1+": "+((ActScore[0]>0)?(ActScore[0]/100+"."+(((ActScore[0]%100)<10)?"0":"")+(ActScore[0]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+2+": "+((ActScore[1]>0)?(ActScore[1]/100+"."+(((ActScore[1]%100)<10)?"0":"")+(ActScore[1]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+3+": "+((ActScore[2]>0)?(ActScore[2]/100+"."+(((ActScore[2]%100)<10)?"0":"")+(ActScore[2]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   ((MaxScore>0)?(gameText[TEXT_MAX_SCORE][((SuficientScore)?0:1)]):""),
										   ((MaxScore>0)?(MaxScore/100+"."+(((MaxScore%100)<10)?"0":"")+(MaxScore%100)+" m"):"")};
							//#else
							//#endif
							//#ifdef MO-C450
							//#endif
							if ((MaxScore>=7800)&&(MaxScores[ActualEvent]<MaxScore)) MaxScores[ActualEvent]=MaxScore;
							gameStatus = GAME_PRE_EVENT_END;
						}
					}
				break;
				case 3 :
					ProtVX = ProtMaxV;
					playTick();
					if (ProtX>12500)
					{
						int TiempoCarrera = 30000;
						if (NumEnems>0)
						{
							for (int i=0; i<NumEnems; i++)
							{
								TiempoCarrera = Math.min(TiempoCarrera,Enems[i][4]);
							}
						}
						MaxScore = ActScore[0];
						if ((MaxScore<=TiempoCarrera)&&(gameType!=3)) SuficientScore= true;
						//#ifndef SMALLCANVAS
						//#ifdef SMALLTEXT
						//#else
						Resultados = new String[4+NumEnems];
						Resultados[0] = gameText[TEXT_ACT_TIME][((SuficientScore)?1:0)];
						Resultados[1] = "";
						if ((ActScore[0]%1000) < 100) Resto = "0";
						if ((ActScore[0]%1000) < 10) Resto = "00";
						Resultados[2] = gameText[TEXT_PLAYER_INFO][ActualPlayer]+": "+(ActScore[0]/1000)+"."+Resto+ActScore[0]%1000+" s";
						for (int i=0; i<NumEnems; i++)
						{
							Resto = "";
							if ((Enems[i][4]%1000) < 100) Resto = "0";
							if ((Enems[i][4]%1000) < 10) Resto = "00";
							Resultados[3+i] = gameText[TEXT_PLAYER_INFO][Enems[i][5]]+": "+(Enems[i][4]/1000)+"."+Resto+Enems[i][4]%1000+" s";
						}
						Resultados[Resultados.length-1] = (((MaxScore>0)&&((MaxScore<MaxScores[ActualEvent])||(MaxScores[ActualEvent]<=0)))?gameText[TEXT_NEW_RECORD][0]:"");
						//#endif
						//#else
						//#endif
						//#ifdef MO-C450
						//#endif
						if ((MaxScore>0)&&((MaxScore<MaxScores[ActualEvent])||(MaxScores[ActualEvent]<=0))) MaxScores[ActualEvent]=MaxScore;
						gameStatus = GAME_PRE_EVENT_END;
					}
				break;
				case 4 :
					playTick();
					if ((ProtVX==0)&&(ProtY==0)&&(Caer)&&(timeToChange==0)) 
					{
						ActualTry++;
						if (ActualTry<3) gameStatus = GAME_INIT; else
						{
							MaxScore = Math.max(Math.max(ActScore[0],ActScore[1]),ActScore[2]);
							if ((MaxScore>=400)&&(gameType!=3)) SuficientScore= true;
							//#ifndef SMALLCANVAS
							Resultados = new String[] {(((MaxScore>=400)&&(MaxScore>MaxScores[ActualEvent]))?gameText[TEXT_NEW_RECORD][0]:gameText[TEXT_ACT_HIGH][0]),
										   "",
										   gameText[TEXT_TRY][0]+1+": "+((ActScore[0]>0)?(ActScore[0]/100+"."+(((ActScore[0]%100)<10)?"0":"")+(ActScore[0]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+2+": "+((ActScore[1]>0)?(ActScore[1]/100+"."+(((ActScore[1]%100)<10)?"0":"")+(ActScore[1]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+3+": "+((ActScore[2]>0)?(ActScore[2]/100+"."+(((ActScore[2]%100)<10)?"0":"")+(ActScore[2]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   ((MaxScore>0)?(gameText[TEXT_MAX_SCORE][((SuficientScore)?0:1)]):""),
										   ((MaxScore>0)?(MaxScore/100+"."+(((MaxScore%100)<10)?"0":"")+(MaxScore%100)+" m"):"")};
							//#else
							//#endif
							//#ifdef MO-C450
							//#endif
							if ((MaxScore>=400)&&(MaxScores[ActualEvent]<MaxScore)) MaxScores[ActualEvent]=MaxScore;
							gameStatus = GAME_PRE_EVENT_END;
						}
					}
				break;
				case 5 :
					playTick();
					if (((ItemY==0)||(Caer))&&((timeToChange==0)&&(ItemVX==0))) 
					{
						ActualTry++;
						if (ActualTry<3) gameStatus = GAME_INIT; else
						{
							MaxScore = Math.max(Math.max(ActScore[0],ActScore[1]),ActScore[2]);
							if ((MaxScore>=2200)&&(gameType!=3)) SuficientScore= true;
							//#ifndef SMALLCANVAS
							Resultados = new String[] {(((MaxScore>=2200)&&(MaxScore>MaxScores[ActualEvent]))?gameText[TEXT_NEW_RECORD][0]:gameText[TEXT_ACT_LONG][0]),
										   "",
										   gameText[TEXT_TRY][0]+1+": "+((ActScore[0]>0)?(ActScore[0]/100+"."+(((ActScore[0]%100)<10)?"0":"")+(ActScore[0]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+2+": "+((ActScore[1]>0)?(ActScore[1]/100+"."+(((ActScore[1]%100)<10)?"0":"")+(ActScore[1]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+3+": "+((ActScore[2]>0)?(ActScore[2]/100+"."+(((ActScore[2]%100)<10)?"0":"")+(ActScore[2]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   ((MaxScore>0)?(gameText[TEXT_MAX_SCORE][((SuficientScore)?0:1)]):""),
										   ((MaxScore>0)?(MaxScore/100+"."+(((MaxScore%100)<10)?"0":"")+(MaxScore%100)+" m"):"")};
							//#else
							//#endif
							//#ifdef MO-C450
							//#endif
							if ((MaxScore>=2200)&&(MaxScores[ActualEvent]<MaxScore)) MaxScores[ActualEvent]=MaxScore;
							gameStatus = GAME_PRE_EVENT_END;
						}
					}
				break;
				case 6 :
					playTick();
					if ((ProtVX==0)&&(ProtY==0)&&(Caer)&&(timeToChange==0)) 
					{
						ActualTry++;
						if (ActualTry<3) gameStatus = GAME_INIT; else
						{
							MaxScore = Math.max(Math.max(ActScore[0],ActScore[1]),ActScore[2]);
							if ((MaxScore>=800)&&(gameType!=3)) SuficientScore= true;
							//#ifndef SMALLCANVAS
							Resultados = new String[] {(((MaxScore>=800)&&(MaxScore>MaxScores[ActualEvent]))?gameText[TEXT_NEW_RECORD][0]:gameText[TEXT_ACT_HIGH][0]),
										   "",
										   gameText[TEXT_TRY][0]+1+": "+((ActScore[0]>0)?(ActScore[0]/100+"."+(((ActScore[0]%100)<10)?"0":"")+(ActScore[0]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+2+": "+((ActScore[1]>0)?(ActScore[1]/100+"."+(((ActScore[1]%100)<10)?"0":"")+(ActScore[1]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   gameText[TEXT_TRY][0]+3+": "+((ActScore[2]>0)?(ActScore[2]/100+"."+(((ActScore[2]%100)<10)?"0":"")+(ActScore[2]%100)+" m"):(gameText[TEXT_SCORE_NULL][0])),
										   ((MaxScore>0)?(gameText[TEXT_MAX_SCORE][((SuficientScore)?0:1)]):""),
										   ((MaxScore>0)?(MaxScore/100+"."+(((MaxScore%100)<10)?"0":"")+(MaxScore%100)+" m"):"")};
							//#else
							//#endif
							//#ifdef MO-C450
							//#endif
							if ((MaxScore>=800)&&(MaxScores[ActualEvent]<MaxScore)) MaxScores[ActualEvent]=MaxScore;
							gameStatus = GAME_PRE_EVENT_END;
						}
					}
				break;
			}
			
			if (gameStatus!=GAME_TRY_END)
			{
				if (SuficientScore)
				{
					gc.soundPlay(6,1);
					NumSavedEvents = Math.min(7,Math.max(NumSavedEvents,ActualEvent+2));
					SavedEvent = ActualEvent+1;
					if (SavedEvent >= 7)
					{	
						SavedEvent = 0;
					}
				} else
				{
					gc.soundPlay(7,1);
				}
			}
		break;
		
		case GAME_PRE_EVENT_END :
			setPanel(1,gameText[TEXT_SCORES][0],Resultados);
			ActualTry = 0;
			playShow = true;
			gameStatus = GAME_EVENT_END;
			saveData();
		break;
		
		case GAME_EVENT_END :
			playShow = true;
			if (((keyMisc!=0)&&(lastKeyMisc==0))||((keyMenu!=0)&&(lastKeyMenu==0)))
			{
				gameStatus = GAME_PRE_NEXT;
				setPanel(3);
			}
		break;
	
		case GAME_PRE_NEXT :
			gameStatus = GAME_NEXT;
			setPanel(0,"");
		break;
		
		case GAME_NEXT :
			playShow = true;
			menuInit(MENU_RETRY);
			ActScore[0] = 0;
			ActScore[1] = 0;
			ActScore[2] = 0;
		break;
	
		case GAME_CREATE:
			gc.MenuTextColor = (0xFFFFFF);
			System.gc();
			gc.Vacia(true);
			initRND();
			playCreate();
			gameStatus = GAME_INIT;
	
		case GAME_INIT:
			playInit();
			playExit=0;
			if (ActualTry==0)
			{
				setPanel(0,gameText[TEXT_EVENT_INFO][ActualEvent]);
				gc.TempString = gameText[TEXT_EVENT_INFO][ActualEvent];
				gc.soundPlay(1,1);
			} else
			{
				setPanel(0,gameText[TEXT_TRY][0]+(ActualTry+1));
				gc.TempString = gameText[TEXT_TRY][0]+(ActualTry+1);
			}
			gameStatus = GAME_RETURN;
		break;
	
		case GAME_RETURN :
			if ((((lastKeyMisc==0)&&(keyMisc!=0))||((lastKeyMenu==0)&&(keyMenu!=0)))||(panelAct==false))
			{
				gc.soundPlay(2,1);
				gameStatus = GAME_COUNTDOWN;
				setPanel(2);
				timeToChange = ((500)/SleepTime)+1;
				gc.InitTexto(gameText[TEXT_START][1+(((ActualTry==0)&&(ProtX==0)&&(ItemX==0))?ActualEvent:0)],gc.canvasWidth/2,gc.canvasHeight/2,0xFFFFFF,(400/SleepTime));
			}
		break;
		
		case GAME_COUNTDOWN :
			if (timeToChange==0)
			{
				gc.InitTexto(gameText[TEXT_START][0],gc.canvasWidth/2,gc.canvasHeight/2,0xFFFFFF,(400/SleepTime));
				gameStatus = GAME_PLAY;
				//StartTime = PlayMilis;
				timeToChange = ((500)/SleepTime);
			}
		break;

		case GAME_PLAY:
			if ((keyMisc!=lastKeyMisc)&&(keyMisc!=0)) CheatRUN(keyMisc);
			playPaused = false;
			if (keyMenu == -1 && lastKeyMenu == 0) {gameStatus = GAME_MENU_INIT_SECOND; break;}
	
			if ( !playTick() ) {break;}
	
			playRelease();
	
			playExit=0;
		break;
		
		case GAME_ANGLE :
			playShow = true;
			if ((ActAngle+=2)>63) ActAngle = 1;
			if (((lastKeyMenu != 2)&&(keyMenu == 2))||((lastKeyMisc == 0)&&(keyMisc == 5)))
			{
				gameStatus = GAME_PLAY;
				switch (ActualEvent)
				{
					case 6 :
						ProtVY = 0+(4*ProtVX*sin(ActAngle))/(3*1024);
						ProtVX = (4*ProtVX*cos(ActAngle))/(3*1024);
						ProtY += ProtVY;
						ProtPX = 3990;
						InitArrow(20+RND(5),-25);
						InitArrow(20+RND(5),-25);
						InitArrow(20+RND(5),-25);
						InitArrow(20+RND(5),-25);
						InitArrow(20+RND(5),-25);
						InitArrow(20+RND(5),-25);
					break;
					
					case 5 :
						gc.ActAnim = ANIM_LANZA_M;
					break;
					
					case 4 :
						ProtVY = 0+(ProtVX*sin(ActAngle))/(1024);
						ProtVX = (ProtVX*cos(ActAngle))/(1024);
						ProtY += ProtVY;
						InitArrow(20+RND(5),-10);
						InitArrow(20+RND(5),-10);
						InitArrow(20+RND(5),-10);
						InitArrow(20+RND(5),-10);
					break;
					
					case 2 :
						gc.ActAnim = ANIM_LANZA_J;
					break;
					
					case 1 :
						ProtVY = 0+(ProtVX*sin(ActAngle))/(1024);
						ProtVX = (ProtVX*cos(ActAngle))/(1024);
						ProtY += ProtVY;
					break;
				}
				gc.soundPlay(3,1);
			}
		break;
	
		case GAME_NEW_OLIMPIX :
			gameType = 1; //Nueva Olimpiada
			ActualTry = 0;
			ActualEvent = 0;
			gameStatus = GAME_PRE_PLAYER_SELECT;
			gc.ActAnim = ANIM_ESTATIC;
			setPanel(2);
		break;
	
		case GAME_CONTINUE_OLIMPIX :
			gameType = 2; //Continuar Olimpiada
			ActualTry = 0;
			ActualEvent = SavedEvent;
			ActualPlayer = SavedPlayer;
			setPanel(2);
			gameStatus = GAME_CREATE;
		break;
		
		case GAME_TRAIN :
			ActualTry = 0;
			gameType = 3; //Entrenamiento
			gameStatus = GAME_PRE_PLAYER_SELECT;
			gc.ActAnim = ANIM_ESTATIC;
			setPanel(2);
		break;
		
		case GAME_PRE_PLAYER_SELECT :
			gameStatus = GAME_PLAYER_SELECT;
			gc.ActAnim = ANIM_ESTATIC;
			setPanel(1,gameText[TEXT_SELECT_PLAYER][0],new String[] {gameText[TEXT_PLAYER_INFO][ActualPlayer]});
			gc.PintaMapa = true;
		break;
			
		case GAME_PLAYER_SELECT :
			playPaused = false;
			playShow = true;
			if ((keyX!=0)&&(lastKeyX==0)) ActualPlayer += keyX;
			ActualPlayer = (ActualPlayer + NumPlayers)%NumPlayers;
			setPanel(-1,gameText[TEXT_SELECT_PLAYER][0],new String[] {gameText[TEXT_PLAYER_INFO][ActualPlayer]});
			if ((lastKeyMenu != -1)&&(keyMenu == -1)) 
			{
				gameStatus = GAME_MENU_INIT_MAIN;
				setPanel(3);
			}
			if ((lastKeyMenu != 2)&&(keyMenu == 2))
			{
				switch (gameType)
				{
					case 1 :
						gameStatus = GAME_PRE_TEXT_INTRO;
						SavedPlayer = ActualPlayer;
					break;
					
					case 2 :
						gameStatus = GAME_CREATE;
					break;
					
					case 3 :
						gameStatus = GAME_PRE_EVENT_SELECT;
					break;
				}
				setPanel(3);
			}
		break;
		
		case GAME_PRE_EVENT_SELECT :
			gameStatus = GAME_EVENT_SELECT;
			ActualEvent = (ActualEvent + NumSavedEvents)%NumSavedEvents;
			gc.PintaEvent = fadeTime*2;
			gc.TempString = gameText[TEXT_EVENT_INFO][ActualEvent];
			setPanel(1,gameText[TEXT_SELECT_EVENT][0],new String[] {gameText[TEXT_PLAYER_INFO][ActualPlayer]});
		break;
		
		case GAME_EVENT_SELECT :
			playShow = true;
			if ((keyX!=0)&&(lastKeyX==0)&&(gc.PintaEvent==fadeTime+1))
			{
				ActualEvent += keyX;
				gc.PintaEvent = fadeTime;
			}
			ActualEvent = (ActualEvent + NumSavedEvents)%NumSavedEvents;
			setPanel(-1,gameText[TEXT_SELECT_EVENT][0],new String[] {gameText[TEXT_PLAYER_INFO][ActualPlayer]});
			if ((lastKeyMenu != -1)&&(keyMenu == -1))
			{
				gameStatus = GAME_PRE_PLAYER_SELECT;
				setPanel(3);
			}
			if ((lastKeyMenu != 2)&&(keyMenu == 2))
			{
				gameStatus = GAME_CREATE;
				setPanel(3);
			}
		break;
		
		case GAME_PRE_TEXT_INTRO :
			setPanel(1,"",NULL_TEXT);
			gameStatus = GAME_TEXT_INTRO;
		break;
		
		case GAME_TEXT_INTRO :
			menuInit(MENU_TEXT_INTRO);
			setPanel(-1,gameText[TEXT_NEW_OLIMPIX][0]);
		break;
		
		case GAME_PRE_END :
			setPanel(1,"",NULL_TEXT);
			gc.Vacia(true);
			gc.CargaMenu();
			gc.CargaMenuImg();
			System.gc();
			gc.ActAnim = ANIM_ESTATIC;
			gameStatus = GAME_END;
		break;
		
		case GAME_END :
			menuInit(MENU_TEXT_END);
			setPanel(-1,gameText[TEXT_FELICIDADES][0]);
		break;
	
		case GAME_MENU_INIT_SECOND:
			gc.CargaPanel();
			gameStatus = GAME_MENU_SECOND;
			//#ifndef NK-s60
			//#endif
			setPanel(0);
		break;
		
		case GAME_MENU_SECOND:
			menuInit(MENU_SECOND);
		break;
	
		case GAME_PRE_SHOW_HELP :
			setPanel(1,"",NULL_TEXT);
			gameStatus = GAME_SHOW_HELP;
		break;
		
		case GAME_SHOW_HELP :
			menuInit(MENU_SCROLL_HELP);
			setPanel(-1,gameText[TEXT_HELP_TITLE][0]);
		break;
		
		case GAME_PRE_SHOW_ABOUT :
			setPanel(1,"",NULL_TEXT);
			gameStatus = GAME_SHOW_ABOUT;
		break;
		
		case GAME_SHOW_ABOUT :
			menuInit(MENU_SCROLL_ABOUT);
			setPanel(-1,gameText[TEXT_ABOUT_TITLE][0]);
		break;
		
		case GAME_MENU_GAMEOVER:
			playShow = true;
			gameStatus = GAME_MENU_INIT_MAIN;
		break;
		}
	}
}

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

// -------------------
// game Create
// ===================

public void gameCreate()
{
}

// -------------------
// game Destroy
// ===================

public void gameDestroy()
{
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

// -------------------
// prefs INI
// ===================

public void loadPrefs()
{
	prefsData = updatePrefs(null);	// Recuperamos byte[] almacenado la ultima vez

	if (prefsData == null)
	{
		prefsData = new byte[] {1,1,0};		// Inicializamos preferencias ya que NO estaban grabadas
	}

	gameSound=prefsData[0]!=0;
	gameVibra=prefsData[1]!=0;
}

// -------------------
// prefs SET
// ===================

public void savePrefs()
{
	/*prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);

	//updatePrefs(prefsData);		// Almacenamos byte[]*/
}

// <=- <=- <=- <=- <=-



// *******************
// -------------------
// menu - Engine
// ===================
// *******************

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
		//#ifndef AL-756
		if (SavedEvent>0) menuListAdd(0, gameText[TEXT_CONTINUAR_OLIMPIADA][0], MENU_ACTION_CONTINUE_FROM);
		//#else
		//#endif
		menuListAdd(0, gameText[TEXT_NUEVA_OLIMPIADA][0], MENU_ACTION_PLAY);
		menuListAdd(0, gameText[TEXT_ENTRENAMIENTO][0], MENU_ACTION_TRAIN);
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
		if (gc.deviceSound) {menuListAdd(0, gameText[TEXT_SOUND], MENU_ACTION_SOUND_CHG, gameSound?1:0);}
		if (gc.deviceVibra) {menuListAdd(0, gameText[TEXT_VIBRA], MENU_ACTION_VIBRA_CHG, gameVibra?1:0);}
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RESTART);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_RETRY :
		menuListClear();
		menuListAdd(0, gameText[(((SuficientScore)&&(gameType!=3))?TEXT_CONTINUE:TEXT_TRYAGAIN)], MENU_ACTION_NEXT);
		menuListAdd(0, gameText[TEXT_RESTART], MENU_ACTION_RETURN);
		menuListAdd(0, gameText[TEXT_EXIT], MENU_ACTION_EXIT_GAME);
		menuListSet_Option();
	break;

	case MENU_SCROLL_HELP:
		menuListClear();
		menuListAdd(0, gameText[TEXT_HELP_SCROLL]);
		if (TEXT_SCROLL) menuListSet_Scroll(); else menuListSet_Screen();
	break;


	case MENU_SCROLL_ABOUT:
		menuListClear();
		menuListAdd(0, gameText[TEXT_ABOUT_SCROLL]);
		if (TEXT_SCROLL) menuListSet_Scroll(); else menuListSet_Screen();
	break;
	
	case MENU_TEXT_INTRO:
		menuListClear();
		menuListAdd(0, gameText[TEXT_INTRO]);
		if (TEXT_SCROLL) menuListSet_Scroll(); else menuListSet_Screen();
	break;
	
	case MENU_TEXT_END:
		/*gc.Vacia(true);
		gc.CargaMenu();
		gc.CargaMenuImg();
		menuImg = gc.Imagenes[0][1];*/
		menuListClear();
		menuListAdd(0, gameText[TEXT_END_OLIMPIX]);
		if (TEXT_SCROLL) menuListSet_Scroll(); else menuListSet_Screen();
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
		if (menuType<500) 
		{
			if ((menuType==MENU_TEXT_END)||(menuTypeBack==MENU_MAIN)) gameStatus = GAME_MENU_INIT_MAIN;
			if (menuTypeBack==MENU_SECOND) gameStatus = GAME_MENU_INIT_SECOND; 
		} else
		{
			System.gc();
			gameStatus = GAME_CREATE;
		}
		if ((gameStatus==GAME_MENU_INIT_MAIN)&&(menuImg==null)) menuImg = gc.Imagenes[0][1];
		setPanel(3);
		menuExit = true;
	break;


	case MENU_ACTION_CONTINUE:	// Continuar
		if (Order>0)
		{
			gameStatus = GAME_RETURN;
			timeToChange = ((2*500)/SleepTime);
		} else gameStatus = GAME_PLAY;
		gc.soundStop();
		setPanel(2);
		menuExit = true;
	break;
	
	case MENU_ACTION_NEXT:
		if ((gameType == 3)||(SuficientScore==false))
		{
			playInit();
			ActualTry = 0;
			gameStatus = GAME_INIT;
			setPanel(2);
			playExit = 3;
			menuExit = true;
			SuficientScore = false;
		} else
		{
			ActualEvent++;
			setPanel(2);
			playExit = 3;
			menuExit = true;
			if (ActualEvent<NumEvents)
			{
				ActualTry = 0;
				gameStatus = GAME_CREATE;
				SuficientScore = false;
			} else
			{
				ActualTry = 0;
				ActualEvent = 0;
				gameStatus = GAME_PRE_END;
				SuficientScore = false;
			}
		}
	break;
	
	case MENU_ACTION_PLAY:
		menuExit = true;
		gameType = 1;
		gameStatus = GAME_NEW_OLIMPIX;
	break;
	
	case MENU_ACTION_CONTINUE_FROM:
		menuExit = true;
		gameType = 2;
		gameStatus = GAME_CONTINUE_OLIMPIX;
	break;

	case MENU_ACTION_TRAIN:
		menuExit = true;
		gameType = 3;
		gameStatus = GAME_TRAIN;
	break;
	
	case MENU_ACTION_SHOW_HELP:		// Controles...
		setPanel(2);
		gameStatus = GAME_PRE_SHOW_HELP;
		menuExit = true;
	break;


	case MENU_ACTION_SHOW_ABOUT:	// About...
		setPanel(2);
		gameStatus = GAME_PRE_SHOW_ABOUT;
		menuExit = true;
	break;

	case MENU_ACTION_RESTART:	// Restart
		gameStatus = GAME_MENU_INIT_MAIN;
		gc.ActAnim = ANIM_ESTATIC;
		gc.Vacia(true);
		gc.CargaMenu();
		gc.CargaMenuImg();
		setPanel(2);
		playExit = 3;
		menuExit = true;
	break;

	case MENU_ACTION_RETURN:
		gameStatus = GAME_MENU_INIT_MAIN;
		gc.ActAnim = ANIM_ESTATIC;
		gc.Vacia(true);
		gc.CargaMenu();
		gc.CargaMenuImg();
		setPanel(2);
		playExit = 3;
		menuExit = true;
	break;
	
	case MENU_ACTION_EXIT_GAME:	// Exit Game
		gameExit = true;
	break;	


	case MENU_ACTION_SOUND_CHG:

		gameSound = menuListOpt() != 0;
		if (!gameSound) {gc.soundStop();} else if (menuType==0) {gc.soundPlay(0,0);}
		saveData();
	break;


	case MENU_ACTION_VIBRA_CHG:

		gameVibra = menuListOpt() != 0;
		saveData();
	break;
	}

}

// <=- <=- <=- <=- <=-






// *******************
// -------------------
// play - Engine
// ===================
// *******************

// -------------------
// play Create
// ===================

public void playCreate()
{
	//gc.canvasFillInit(gc.BackGroundColor);

	ActualTry = 0;
	ActScore[0] = 0;
	ActScore[1] = 0;
	ActScore[2] = 0;
	SuficientScore=false;
	initEnems(true);
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
	ResetArrows();
	ProtVX = 0;
	ProtVY = 0;
	ProtX = 0;
	ProtY = 0;
	ActAngle = 0;

	int MaxButterFly = 6;
	butterFlyX   = new int[MaxButterFly];
	butterFlyY   = new int[MaxButterFly];
	butterFlyF   = new int[MaxButterFly];
	butterFlyDX  = 0;
	butterFlyDY  = -2;
	butterFlyW   = 16;
	butterFlyH   = 16;
	butterFlyV   = new boolean[MaxButterFly];
	
	for (int Counter = 0; Counter<butterFlyV.length; Counter++)
	{
		butterFlyX[Counter] = 0;
		butterFlyY[Counter] = 0;
		butterFlyV[Counter] = false;
	}
	ItemX = 0;
	ItemY = (gc.PlayersHeight-gc.PerPos[ActualPlayer][0])*10;
	ItemVX = 0;
	ItemVY = 0;

	initEnems(false);
	Arrows = 0;
	int[] ActScore = new int[3];
	ActScore[0] = 0;
	ActScore[1] = 0;
	ActScore[2] = 0;
	if (ActualEvent==5) gc.ActAnim = ANIM_GIRA; else if ((ActualEvent==2)||(ActualEvent==6)) gc.ActAnim = ANIM_CAMINA_J; else gc.ActAnim = ANIM_ESTATIC;
	gc.ActFrame = 0;
	
	sinoidal = 2;
	incSinus = 1;
	
	Caer = false;
	gc.playInit_Gfx();
	gc.gameDraw();

	waitTime(10);

}

// -------------------
// play Release
// ===================

public void playRelease()
{
	gc.playRelease_Gfx();
}

// -------------------
// play Tick
// ===================

int CheatPos_1=0;
boolean cheatActive=false;
	
public void CheatRUN(int keycode)
{
	//#ifdef VI-TSM100
	//#else
	byte[] Cheat_1= {7,6,6,6,7,7,7,8,6,2,6,6};
	//#endif
	if (Cheat_1[CheatPos_1++] != keycode) {CheatPos_1=0;}
	if (Cheat_1.length==CheatPos_1) {cheatActive = true; CheatPos_1=0; NumSavedEvents = 7;/*Cheat*/}
}

//////////////////////////////////////////////

public void ResetArrows()
{
	Action = false;
	Order = 0;
	ArrowsA = new boolean[MaxArrows];
	ArrowsV = new boolean[MaxArrows];
	ArrowsI = new long[MaxArrows];
	ArrowsT = new int[MaxArrows];
	ArrowsD = new int[MaxArrows];
	ArrowsO = new int[MaxArrows];
	System.gc();
	for (int i = 0; i<MaxArrows; i++)
	{
		ArrowsA[i] = false;
		ArrowsV[i] = false;
		ArrowsI[i] = 0;
		ArrowsT[i] = 0;
		ArrowsD[i] = 0;
		ArrowsO[i] = 0;
	}
}

public void InitArrow(int D, int T)
{
	for (int i = 0; i<MaxArrows; i++)
	{
		if ((ArrowsV[i]==false)&&(ArrowsA[i] == false))
		{
			ArrowsA[i] = true;
			ArrowsV[i] = true;
			ArrowsO[i] = Order;
			ArrowsI[i] = PlayMilis;
			ArrowsD[i] = D;
			ArrowsT[i] = T;
			Order++;
			i = MaxArrows;
			if (D>=5) Action = true;
		}
	}
}

public void CheckArrowsTime()
{
	for (int i=0; i<MaxArrows; i++)
	{
		switch (ArrowsD[i])
		{
			case 29 :
			case 28 :
			case 27 :
			case 26 :
			case 25 :
				if ((ArrowsA[i])&&(PlayMilis>=(ArrowsI[i]+ArrowsT[i])))
				{
					ArrowsA[i] = false;
					ArrowsT[i] = 5;
					Action = false;
					for (int j=0; j<MaxArrows; j++)
					{
						if (ArrowsO[i]<ArrowsO[j]) ArrowsO[j]--;
					}
					Order--;
					ArrowsO[i] = gc.canvasWidth-(gc.ArrowsWidth*Order+gc.ArrowsWidth)+(ArrowsO[i]*gc.ArrowsWidth);
					ProtVX --;
					Arrows++;
				}
			break;
			
			case 24 :
			case 23 :
			case 22 :
			case 21 :
			case 20 :
				//if ((ArrowsA[i])&&(ProtVY<ArrowsT[i]))
				if ((ArrowsA[i])&&(ProtY<=0))
				{
					Caer = true;
					gc.vibraInit(200);
					ProtVY = 1;
					ResetArrows();
					gameStatus = GAME_TRY_END;
					ActScore[ActualTry] = -1;
				}
			break;
			
			case 15 :
				if ((ArrowsA[i])&&(ProtX>ArrowsT[i]))
				{
					Caer = true;
					gc.vibraInit(200);
					ResetArrows();
					ActScore[ActualTry] = -1;
					gameStatus = GAME_TRY_END;
				}
			case 10 :
				if ((ArrowsA[i])&&(ProtX>ArrowsT[i]))
				{
					ProtPX = 3990;
					Caer = true;
					gc.vibraInit(200);
					ProtVY = 1;
					ProtVX = 20;
					ResetArrows();
				}
			break;
			default :
				if ((ArrowsA[i])&&(PlayMilis>=(ArrowsI[i]+ArrowsT[i])))
				{
					ArrowsA[i] = false;
					ArrowsT[i] = 5;
					if (ArrowsD[i]>=5) Action = false; else ProtVX -= 4;
					
					for (int j=0; j<MaxArrows; j++)
					{
						if (ArrowsO[i]<ArrowsO[j]) ArrowsO[j]--;
					}
					Order--;
					ArrowsO[i] = gc.canvasWidth-(gc.ArrowsWidth*Order+gc.ArrowsWidth)+(ArrowsO[i]*gc.ArrowsWidth);
				}
			break;
		}
	}
}

public void CheckArrowsKey()
{
	if (Order>0) Order--;
	for (int i=0; i<MaxArrows; i++)
	{
		if (ArrowsA[i])
		{
			if (ArrowsO[i] == 0)
			{
				boolean keyOk = false;
				switch (ArrowsD[i]%5)
				{
					case 0 :
						if (keyMenu==2) keyOk = true;
					break;
					case 1 :
						if (keyY==-1) keyOk = true;
					break;
					case 2 :
						if (keyX==1) keyOk = true;
					break;
					case 3 :
						if (keyY==1) keyOk = true;
					break;
					case 4 :
						if (keyX==-1) keyOk = true;
					break;
				}
				if (keyOk) 
				{
					switch (ArrowsD[i])
					{
						default :
							ProtVX += (int)(ArrowsT[i]-(PlayMilis-ArrowsI[i]))/250;
						break;
						case 5 :
						case 6 :
						case 7 :
						case 8 :
						case 9 :
							ProtVX += (int)(ArrowsT[i]-(PlayMilis-ArrowsI[i]))/250;
							ProtVX += 4;
							Action = false;
						break;
						case 10:
							ProtVY = 25-((ProtVX*10)/ProtMaxV);
							gc.soundPlay(3,1);
							Action = false;
						break;
						case 15:
							Action = false;
							ActAngle = 0;
							gameStatus = GAME_ANGLE;
							ResetArrows();
							gc.ActFrame = 0;
							switch (ActualEvent)
							{
								case 0:
								case 3:
									gc.ActAnim = ANIM_ESTATIC;
								break;
								case 6:
									gc.ActAnim = ANIM_CAMINA_J;
								break;
								case 1:
									gc.ActAnim = ANIM_SUBIENDO_L;
								break;
								case 4:
									gc.ActAnim = ANIM_SUBIENDO_A;
								break;
								case 2:
									gc.ActAnim = ANIM_ESTATICO_J;
								break;
								case 5:
									gc.ActAnim = ANIM_ESTATICO_M;
								break;
							}
						break;
						case 20:
						case 21:
						case 22:
						case 23:
						case 24:
							ProtVY += 1;
						break;
						case 25 :
						case 26 :
						case 27 :
						case 28 :
						case 29 :
							Arrows++;
							ProtVX += ((ProtMaxV)/14);
							Action = false;
						break;
					}
					ArrowsT[i] = -3;
				} else
				{	
					ArrowsT[i] = 5;
					ProtVX -= 6;
					switch (ArrowsD[i])
					{
						case 5 :
						case 6 :
						case 7 :
						case 8 :
						case 9 :
							Action = false;
						break;
						case 15:
							ActScore[ActualTry] = -1;
							gameStatus = GAME_TRY_END;
						case 10:
							Action = false;
							Caer = true;
							gc.vibraInit(200);
							ProtVY = 10;
							ProtVX = 20;
							ProtPX = 3990;
							timeToChange = 10;
						break;
						case 20 :
						case 21 :
						case 22 :
						case 23 :
						case 24 :
							Action = false;
							Caer = true;
							gc.vibraInit(200);
							ProtVY = 1;
							gameStatus = GAME_TRY_END;
							timeToChange = 10;
							ActScore[ActualTry] = -1;
						break;
						case 25 :
						case 26 :
						case 27 :
						case 28 :
						case 29 :
							Action = false;
							ActAngle = 0;
							gameStatus = GAME_ANGLE;
							ResetArrows();
							gc.ActAnim = ANIM_ESTATICO_M;
						break;
					}
				}
				ArrowsO[i] = gc.canvasWidth-(gc.ArrowsWidth*Order+gc.ArrowsWidth)+(ArrowsO[i]*gc.ArrowsWidth);
				ArrowsA[i] = false;
			}
			ArrowsO[i]--;
		}
	}
}

//////////////////////////////////////////////

public boolean playTick()
{
	if ((ActualEvent==0)||(ActualEvent==3)) playEnems();
	if (Caer==false)
	{
		switch (ActualEvent)
		{
			case 6 :
				if (ProtY == 0)
				{
					gc.ActAnim = ANIM_CAMINA_J;
				} else
				{
					if (Math.abs(ProtVY)<16) gc.ActAnim = ANIM_SALTANDO_P; else
					{
						if (ProtVY>0) gc.ActAnim = ANIM_SUBIENDO_P;
						if (ProtVY<0) gc.ActAnim = ANIM_CAER_P;
					}
				}
				if ((ProtX>4000)&&(ProtX<4100)&&(ProtY<((SPertiga)*10)))
				{
					Caer = true;
					gc.vibraInit(200);
					ProtVY = 1;
					Action = false;
					ActScore[ActualTry] = -1;
					timeToChange=10;
					gameStatus = GAME_TRY_END;
				}
				if ((timeToChange==0)&&(ProtX<3500))
				{
					timeToChange = (600/SleepTime);
					if ((Action==false)&&(ProtVX<10)) InitArrow(5, 1500); else InitArrow(1+RND(4), 1500);
				}
				if ((Action == false)&&(ProtX>3500)&&(ProtY==0))
				{
					InitArrow(15,4000);
				}
			break;
			case 4 :
				if (ProtY == 0)
				{
					if (ProtVX > (ProtMaxV/10)) gc.ActAnim = ANIM_CAMINANDO; else gc.ActAnim = ANIM_ESTATIC;
				} else
				{
					if (Math.abs(ProtVY)<12) gc.ActAnim = ANIM_SALTANDO_A; else
					{
						if (ProtVY>0) gc.ActAnim = ANIM_SUBIENDO_A;
						if (ProtVY<0) gc.ActAnim = ANIM_CAER_A;
					}
				}
				if ((ProtX>4000)&&(ProtX<4100)&&(ProtY<((SAltura)*10)))
				{
					Caer = true;
					gc.vibraInit(200);
					ProtVY = 1;
					Action = false;
					ActScore[ActualTry] = -1;
					timeToChange=10;
					gameStatus = GAME_TRY_END;
				}
				if ((timeToChange==0)&&(ProtX<3500))
				{
					timeToChange = (600/SleepTime);
					if ((Action==false)&&(ProtVX<10)) InitArrow(5, 1500); else InitArrow(1+RND(4), 1500);
				}
				if ((Action == false)&&(ProtX>3500)&&(ProtY==0))
				{
					InitArrow(15,4000);
				}
			break;
			case 1 :
				if (ProtY == 0)
				{
					if (ProtVX > (ProtMaxV/10)) gc.ActAnim = ANIM_CAMINANDO; else gc.ActAnim = ANIM_ESTATIC;
				} else
				{
					if (Math.abs(ProtVY)<8) gc.ActAnim = ANIM_SALTANDO_L; else
					{
						if (ProtVY>0) gc.ActAnim = ANIM_SUBIENDO_L;
						if (ProtVY<0) gc.ActAnim = ANIM_CAER_L;
					}
				}
				if ((timeToChange==0)&&(ProtX<3500))
				{
					timeToChange = (600/SleepTime);
					if ((Action==false)&&(ProtVX<10)) InitArrow(5, 1500); else InitArrow(1+RND(4), 1500);
				}
				if ((Action == false)&&(ProtX>3500)&&(ProtY==0))
				{
					InitArrow(15,4000);
				}
			break;
			case 5 :
				if (gc.ActAnim!=ANIM_LANZA_M)
				{
					gc.ActAnim = ANIM_GIRA;
					if (Arrows<15)
					{
						if ((Action == false)&&(Caer==false)&&(ItemVY==0))
						{
							//#ifdef SI-x55
							InitArrow(25+RND(5),1200);
							//#elifdef VI-TSM30i
							InitArrow(25+RND(5),1000);
							//#else
							InitArrow(25+RND(5),850);
							//#endif
						}
					} else
					{
						gc.ActAnim = ANIM_ESTATICO_M;
						Action = false;
						ActAngle = 0;
						gameStatus = GAME_ANGLE;
						ResetArrows();
					}
				} else
				{
					if (gc.AnimFinalized)
					{
						ItemX = ProtX+1;
						ItemVY = 0+(3*ProtVX*sin(ActAngle))/(2*1024);
						ItemVX = (3*ProtVX*cos(ActAngle))/(2*1024);
						Caer = true;
						gc.vibraInit(200);
					}	
				}
			break;
			case 2 :
				if (gc.ActAnim!=ANIM_LANZA_J)
				{
					gc.ActAnim = ANIM_CAMINA_J;
					if ((timeToChange==0)&&(ProtX<1600))
					{
						timeToChange = (600/SleepTime);
						InitArrow(6+RND(4), 1500);
					}
					if ((Action == false)&&(ProtX>1600)&&(ProtY==0))
					{
						InitArrow(15,2000);
					}
				} else
				{
					if (gc.AnimFinalized)
					{
						ItemX = ProtX;
						ItemVY = 0+(6*ProtVX*sin(ActAngle))/(2*1024);
						ItemVX = (6*ProtVX*cos(ActAngle))/(2*1024);
						Caer = true;
						gc.vibraInit(200);
					}
				}
			break;
			case 3 :
				if (ProtY==0)
				{
					if (ProtVX > (ProtMaxV/10)) gc.ActAnim = ANIM_CAMINANDO; else gc.ActAnim = ANIM_ESTATIC;
					if ((ProtX>500)&&(ProtX<10000)&&(((ProtX%1500)>1470)||((ProtX%1500)<60)))
					{
						ResetArrows();
						Action = false;
						Caer = true;
						gc.vibraInit(200);
						ProtVY = 10;
						ProtVX = 20;
					}
					if (ProtX < 10000)
					{
						if (Action==false)
						{
							if ((ProtX>100)&&(ProtX<9500)&&((ProtX%1500)>(1100-ProtVX*2))&&(ProtVX>5))
							{
								InitArrow(10, (1500*(ProtX/1500))+1500);
							}
						}
						if ((Action==false)&&(ProtVX<10)) InitArrow(5, 1500);
						else
						{
							if ((Action==false)&&(timeToChange==0))
							{
								timeToChange = (600/SleepTime);
								InitArrow(1+RND(4), 1500);
							}
						}
					} else
					{
						gameStatus = GAME_TRY_END;
					}
				} else
				{
					if (Order>0) ResetArrows();
					if (Math.abs(ProtVY)<8) gc.ActAnim = ANIM_SALTANDO_L; else
					{
						if (ProtVY>0) gc.ActAnim = ANIM_SUBIENDO_L;
						if (ProtVY<0) gc.ActAnim = ANIM_CAER_L;
					}
				}
			break;
			case 0 :
				if (ProtVX > (ProtMaxV/10)) gc.ActAnim = ANIM_CAMINANDO; else gc.ActAnim = ANIM_ESTATIC;
				if (ProtX<10000)
				{
					if (timeToChange==0)
					{
						timeToChange = (600/SleepTime);
						if ((Action==false)&&(ProtVX<10)&&(ProtX>1000)) InitArrow(5, 1500); else InitArrow(1+RND(4), 1500);
					}
				} else
				{
					if (Order>0) ResetArrows();
					gameStatus = GAME_TRY_END;
				}
			break;
		}
		if (((lastKeyX==0)&&(keyX!=0))||((lastKeyY==0)&&(keyY!=0))||((lastKeyMisc==0)&&(keyMisc!=0))) CheckArrowsKey();
		CheckArrowsTime();
	} else
	{
		if ((gc.ActAnim!=ANIM_LANZA_M)||(ActualPlayer!=2)) gc.ActAnim = ANIM_CAER;
		if ((ProtY == 0)&&(ProtVX>0)) butterFlyCreate(ProtX/10);
		if (Order>0) ResetArrows();
		if ((ProtY == 0)&&(ProtVY<=0))
		{
			if (gameStatus != GAME_TRY_END)	timeToChange=1000/SleepTime;
			ResetArrows();
			ProtVX -= 4;
		}
	}

	if (ProtVX<0)
	{
		if (gameStatus != GAME_TRY_END)
		{
			if (Caer)
			{
				switch (ActualEvent)
				{
					case 1 :
					case 4 :
					case 6 :
						timeToChange = 500/SleepTime;
						gameStatus = GAME_TRY_END;
					break;
					case 3 :
						Caer = false;
						ProtX = 1500*((ProtX-1000)/1500)+400;
					break;
				}
			}
		}
		ProtVX = 0;
	}
	if ((ProtVX>ProtMaxV)&&(ProtY==0)) ProtVX = ProtMaxV;
	if (ActualEvent != 5) ProtX += ProtVX;
	if ((ProtY+ProtVY)>0) ProtY += ProtVY; else 
	{
		if (ProtY>0)
		{
			ProtY = 0;
			switch (ActualEvent)
			{
				case 6 :
				case 4 :
					if (ProtX<4000) ActScore[ActualTry] = -1;
					if (Caer==false) ProtVY = (ProtVY*(-1))/2;
					ResetArrows();
					Action = false;
					Caer = true;
					gc.vibraInit(200);
					gc.soundPlay(5,1);
				break;
				
				case 1 :
					if (ProtX<2000) ActScore[ActualTry] = -1;
					if (ActScore[ActualTry]==0) ActScore[ActualTry]=-1;
					ResetArrows();
					Action = false;
					Caer = true;
					gc.vibraInit(200);
					ProtVY = 0;
					gc.soundPlay(4,1);
				break;
				
				/*case 3 :
					if (((ProtX%1500)>1400)||((ProtX%1500)<50))
					{
					
					}
				break;*/
				default :
					ProtVY = 0;
				break;
			}
		}
	}

	if (ItemX!=0)
	{
		if ((gameStatus==GAME_TRY_END)&&(ActualEvent==5))
		{
			ItemX+=ItemVX;
			if (ItemVX>6)
			{
				if (Math.abs(sinoidal)%2==0) butterFlyCreate(ItemX/10+(gc.PlayersWidth/2)-8);
				ItemVX -= 6;
				timeToChange = 200/SleepTime;
			}
			else ItemVX = 0;
		}
		if (gameStatus==GAME_PLAY)
		{
			ItemX+=ItemVX;
			ItemY+=ItemVY;
			ItemVY -= 2;
			if (ItemY<=0)
			{
				gc.vibraInit(200);
				timeToChange = 500/SleepTime;
				ItemY=0;
				gameStatus = GAME_TRY_END;
			}
		}
	}
	ProtVY-=2;
	//gc.ScrollPos += keyX*2;
	//gc.PerspectiveCenter += keyY;
	
	switch (ActualEvent)
	{
		case 0 :
		case 3 :
			//if (ProtX<10000) ActScore[ActualTry] = (int)(PlayMilis-StartTime);
			if (ProtX<10000) ActScore[ActualTry] += 40;
		break;
		case 1 :
			if ((ProtY>0)&&(ActScore[ActualTry]!=-1))
			{
				if (ProtX>4200) ActScore[ActualTry] = ProtX-4200;
			}
		break;
		case 2 :
			if (ActScore[ActualTry]<(ItemX-2000)) ActScore[ActualTry] = ItemX-2000;
		break;
		case 5 :
			if ((gameStatus==GAME_PLAY)&&(ActScore[ActualTry]<(ItemX-50))) ActScore[ActualTry] = ItemX-50;
		break;
		case 4 :
		case 6 :
			if ((ActScore[ActualTry]!=-1)&&(ActScore[ActualTry]<ProtY)) ActScore[ActualTry] = ProtY;
		break;
	}

	moverBurbujas();
	if (playExit!=0) {return true;}

	playShow = true;

	return false;
}

public void initEnems(boolean type)
{
	if (type) Enems = null;
	if (type) System.gc();
	if (type) Enems = new int[NumEnems][10];
	if (type) Used = new boolean[NumPlayers];
	int j;
	for (j=0; j<NumPlayers; j++)
	{
		if (type) { if (j!=ActualPlayer) Used[j] = false; else Used[j] = true;}
	}
	j = 0;
	for (int i=0; i<NumEnems; i++)
	{
		Enems[i][0] = 0;
		Enems[i][1] = 0;
		Enems[i][2] = 0;
		Enems[i][3] = 0;
		Enems[i][6] = 0;
		Enems[i][7] = 0;
		Enems[i][8] = 0;
		Enems[i][9] = 0;
		Enems[i][4] = 13500+RND(3250)+300*ActualEvent;
		//#ifdef SG-Z105
		//#endif
		if (type)
		{
			while ((j<NumPlayers)&&(Used[j]))
			{
				j++;
			}
			if (j<NumPlayers)
			{
				Enems[i][5] = j;
				Used[j] = true;
			} else Enems[i][5] = RND(NumPlayers);
		}
		//System.out.println("Init["+i+"]: "+Enems[i][0]+" "+Enems[i][1]+" "+Enems[i][2]+" "+Enems[i][3]+" "+Enems[i][4]+" "+Enems[i][5]);
	}
}

public void playEnems()
{
	for (int i=0; i<NumEnems; i++)
	{
		Enems[i][6] += 40;
		if (Enems[i][6]>=Enems[i][4])
		{
			Enems[i][1] = ProtMaxV;
		} else
		{
			if ((ActualEvent==3)&&(Enems[i][0]>500)&&((Enems[i][0]%1500)>1420)&&((Enems[i][0]%1500)<1480)&&(Enems[i][0]<10000)&&(Enems[i][2]==0))
			{
				Enems[i][3] = 25-((Enems[i][1]*10)/ProtMaxV);
			}
			if ((Enems[i][2]==0)&&(Enems[i][6]>(Enems[i][4]/2)))
			{
				Enems[i][1] = (9*(Enems[i][1])+1*(((10000-Enems[i][0])*40)/(Enems[i][4]-Enems[i][6])))/10;
			} else
			{
				if ((Enems[i][2]==0)&&((timeToChange == 0)&&(Enems[i][1] < ((ProtMaxV*9)/10)))) Enems[i][1] += ((Enems[i][1]>10)?1:(4-ActualEvent))+RND(5-ActualEvent);
			}
		}
		
		if (Enems[i][2]==0)
		{
			if (Enems[i][1] > (ProtMaxV/10)) Enems[i][7] = ANIM_CAMINANDO; else Enems[i][7] = ANIM_ESTATIC;
		} else
		{
			if (Math.abs(Enems[i][3])<8) Enems[i][7] = ANIM_SALTANDO_L; else
			{
				if (Enems[i][3]>0) Enems[i][7] = ANIM_SUBIENDO_L;
				if (Enems[i][3]<0) Enems[i][7] = ANIM_CAER_L;
			}
		}
					
		Enems[i][0] += Enems[i][1]; //X+=VX
		Enems[i][3] -= 2;
		if ((Enems[i][2]+Enems[i][3])<=0)
		{
			Enems[i][2] = 0;
			Enems[i][3] = 0;
		} else Enems[i][2] += Enems[i][3]; //Y+=VY
	}
}

////////////////////////////////////////////////////////
public void moverBurbujas()
{
	for (int Counter=0; Counter<butterFlyV.length; Counter++)
	{
		if (butterFlyV[Counter]) 
		{
			butterFlyX[Counter] += butterFlyDX;
			butterFlyY[Counter] += butterFlyDY;
			butterFlyF[Counter]++;
			if (butterFlyF[Counter]>5) butterFlyV[Counter] = false;
		}
	}	
}

int butterFlyX[];
int butterFlyY[];
int butterFlyDX;
int butterFlyDY;
int butterFlyF[];
int butterFlyW;
int butterFlyH;
boolean[] butterFlyV;
public void butterFlyCreate(int BX)
{
	int Top=0;
	for (int Counter=0; Counter<butterFlyV.length; Counter++)
	{
		if (butterFlyV[Counter]==false)
		{
			Top = Counter;
			Counter = butterFlyV.length;
		} else
		{
			if (butterFlyY[Counter] < butterFlyY[Top]) Top = Counter;
		}
	}
	butterFlyF[Top] = 0;
	butterFlyX[Top] = BX;
	butterFlyY[Top] = 0;
	butterFlyV[Top] = true;
}


public void initData()
{
	setData(1, 2, (4+4+4/*NumSavedEvents SavedPlayer SavedEvent*/)+(4*NumEvents));
	prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);
}

public void saveData()
{
	prefsData[0]=(byte)(gameSound?1:0);
	prefsData[1]=(byte)(gameVibra?1:0);
	
	startData();
	writeInt(NumSavedEvents);
	writeInt(SavedPlayer);
	writeInt(SavedEvent);
	for (int i=0; i<NumEvents; i++) writeInt(MaxScores[i]);
	
	updatePrefs(prefsData);
}

public void loadData()
{
	byte[] TempData = updatePrefs(null);
	if (TempData != null) prefsData = TempData;
	startData();
	NumSavedEvents = Math.max(readInt(),NumSavedEvents);
	SavedPlayer = readInt();
	SavedEvent = readInt();
	for (int i=0; i<NumEvents; i++) MaxScores[i] = readInt();
}

// <=- <=- <=- <=- <=-


int playExit = 0;

final static int GAME_LOGOS = 0;
final static int GAME_MENU_INIT_MAIN = 9;
final static int GAME_MENU_MAIN = 10;
final static int GAME_MENU_INIT_SECOND = 24;
final static int GAME_MENU_SECOND = 25;
final static int GAME_MENU_GAMEOVER = 30;
final static int GAME_TRY_END = 35;
final static int GAME_CREATE = 20;
final static int GAME_INIT = 40;
final static int GAME_RETURN = 50;
final static int GAME_COUNTDOWN = 55;
final static int GAME_PLAY = 60;
final static int GAME_ANGLE = 61;
final static int GAME_NEW_OLIMPIX = 100;
final static int GAME_CONTINUE_OLIMPIX = 110;
final static int GAME_TRAIN = 200;
final static int GAME_PRE_PLAYER_SELECT = 400;
final static int GAME_PLAYER_SELECT = 410;
final static int GAME_PRE_TEXT_INTRO = 149;
final static int GAME_TEXT_INTRO = 150;
final static int GAME_PRE_EVENT_SELECT = 500;
final static int GAME_EVENT_SELECT = 510;
final static int GAME_PRE_SHOW_HELP = 600;
final static int GAME_SHOW_HELP = 610;
final static int GAME_PRE_SHOW_ABOUT = 601;
final static int GAME_SHOW_ABOUT = 611;
final static int GAME_PRE_EVENT_END = 619;
final static int GAME_EVENT_END = 620;
final static int GAME_PRE_NEXT = 629;
final static int GAME_NEXT = 630;
final static int GAME_PRE_END = 700;
final static int GAME_END = 710;

static final int MENU_MAIN = 0;
static final int MENU_SECOND = 1;
static final int MENU_RETRY = 4;
static final int MENU_SCROLL_HELP = 2;
static final int MENU_SCROLL_ABOUT = 3;
static final int MENU_TEXT_INTRO = 500;
static final int MENU_TEXT_END = 300;

static final int MENU_ACTION_PLAY = 0;
static final int MENU_ACTION_CONTINUE = 1;
static final int MENU_ACTION_SHOW_HELP = 2;
static final int MENU_ACTION_CONTINUE_FROM = 8;
static final int MENU_ACTION_TRAIN = 9;
static final int MENU_ACTION_SHOW_ABOUT = 3;
static final int MENU_ACTION_EXIT_GAME = 4;
static final int MENU_ACTION_RESTART = 5;
static final int MENU_ACTION_SOUND_CHG = 6;
static final int MENU_ACTION_VIBRA_CHG = 7;
static final int MENU_ACTION_NEXT = 15;
static final int MENU_ACTION_RETURN = 16;

final static int TEXT_LEVEL = 13;
final static int TEXT_CONGRATULATIONS = 14;
final static int TEXT_NUEVA_OLIMPIADA = 15;
final static int TEXT_CONTINUAR_OLIMPIADA = 16;
final static int TEXT_ENTRENAMIENTO = 17;
final static int TEXT_INTRO = 18;
final static int TEXT_SELECT_PLAYER = 19;
final static int TEXT_SELECT_EVENT = 20;
final static int TEXT_NEW_OLIMPIX = 21;
final static int TEXT_HELP_TITLE = 22;
final static int TEXT_ABOUT_TITLE = 23;
final static int TEXT_PLAYER_INFO = 24;
final static int TEXT_EVENT_INFO = 25;
final static int TEXT_START = 26;
final static int TEXT_TRY = 27;
final static int TEXT_SCORES = 28;
final static int TEXT_TRYAGAIN = 29;
final static int TEXT_ACT_TIME = 30;
final static int TEXT_ACT_LONG = 31;
final static int TEXT_ACT_HIGH = 32;
final static int TEXT_SCORE_NULL = 33;
final static int TEXT_NEW_RECORD = 34;
final static int TEXT_MAX_SCORE = 35;
final static int TEXT_FELICIDADES = 36;
final static int TEXT_END_OLIMPIX = 37;

final static String[] NULL_TEXT = new String[] {""};
String[] Resultados = new String[] {""};

int gameStatus = 0;
int sinoidal=3, incSinus=1;
int timeToChange = 0;
int gameType = 0;
int ActualPlayer = 0;
int ActualTry = 0;
int ActualEvent = 0;
int SavedPlayer = 0;
int SavedEvent = 0;
int NumSavedEvents = 3;
//#ifdef SMALLJAR
//#else
final static int NumPlayers = 6;
//#endif

final static int NumEvents = 7;
boolean SuficientScore=false;
int[] ActScore = new int[3];
int[] MaxScores = new int[NumEvents];
int MaxScore;

final static int MaxArrows = 20;

int Order = 0;
boolean[] ArrowsA = new boolean[MaxArrows];
boolean[] ArrowsV = new boolean[MaxArrows];
long[] ArrowsI = new long[MaxArrows];
int[] ArrowsT = new int[MaxArrows];
int[] ArrowsD = new int[MaxArrows];
int[] ArrowsO = new int[MaxArrows];

final static int ProtMaxV = 45;
int ProtVX = 0;
int ProtVY = 0;
int ProtX = 0;
int ProtY = 0;
final static int SAltura = 30;
final static int SPertiga = 60;
int ActAngle = 0;
boolean Action = false;
boolean Caer = false;
int ProtPX = 0;
int ItemX = 0;
int ItemY = 0;
int ItemVX = 0;
int ItemVY = 0;
int Arrows = 0;
boolean []Used = new boolean[NumPlayers];

//#ifdef NE-N410i
//#elifdef BIGCANVAS
final static int NumEnems = 4;
//#elifdef SE-T6xx
//#else
//#endif

int [][]Enems = new int[NumEnems][10]; //X VX Y VY Score Type ActScore ActAnim LastAnim ActFrame

final static int ANIM_ESTATIC = 0;
final static int ANIM_CAMINANDO = 1;
final static int ANIM_CAER = 2;
final static int ANIM_SUBIENDO_L = 3;
final static int ANIM_SALTANDO_L = 4;
final static int ANIM_CAER_L = 5;
final static int ANIM_SUBIENDO_A = 6;
final static int ANIM_SALTANDO_A = 7;
final static int ANIM_CAER_A = 8;
final static int ANIM_SUBIENDO_P = 9;
final static int ANIM_SALTANDO_P = 10;
final static int ANIM_CAER_P = 11;
final static int ANIM_GIRA = 12;
final static int ANIM_ESTATICO_M = 13;
final static int ANIM_LANZA_M = 14;
final static int ANIM_CAMINA_J = 15;
final static int ANIM_ESTATICO_J = 16;
final static int ANIM_LANZA_J = 17;

final static boolean TEXT_SCROLL = false;
// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};