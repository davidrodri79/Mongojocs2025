package com.mygdx.mongojocs.ninjarun;

class ITPlayer01
{

int X,Y,ActualFrame,TimeToChange,TimeToFall;
boolean ChangeState,Controlable,SandV[];

int SpriteID,ShadowID,SandID[],SandT[];
int[] Values;
ITGame01 ga;

public ITPlayer01(ITGame01 ITGame, int[] OtherValues)
{
	ga  = ITGame;
	
	Values = new int[OtherValues.length];
	for (int Counter=0;Counter<OtherValues.length;Counter++) Values[Counter] = OtherValues[Counter];

	X = Values[8];
	Y = Values[9];
	
	LoadSprites();
}

public void LoadSprites()
{
	SandID = new int[Values[5]];
	SandT  = new int[Values[5]];
	SandV  = new boolean[Values[5]];
	
	switch (Values[7])
	{
		case 0 :
		case 1 :
		case 2 :
		case 3 :
		case 4 :
		case 5 :
		case 6 :
		case 7 :
		case 8 :
		case 9 :
		{
			ShadowID = ga.ITU.AniSprSET(-1, X-6, ga.FaseMap.MH-Y-5, 81, 1, 1, 0xF001);
			for (int Counter=0; Counter<Values[5]; Counter++)
			{
				SandID[Counter] = ga.ITU.AniSprSET(-1, X, ga.FaseMap.MH-Y-10, 82, 7, 2, 0xD002);
				ga.ITU.AniSprs[SandID[Counter]].FrameAct = (Counter%7);
				SandT[Counter] = (5*2)+ga.RND(4)-(ga.ITU.AniSprs[SandID[Counter]].FrameAct*2)+(Counter%3);
				SandV[Counter] = false;
			}
			SpriteID = ga.ITU.AniSprSET(-1, X, ga.FaseMap.MH-Y-Values[3]+ 10, 0+(Values[6]*18), 2, 2, 0x001);
		}
		break;
		case 10 :
		{
			SpriteID = ga.ITU.AniSprSET(-1, X, ga.FaseMap.MH-Y, 72, 1, 1, 0xB002);
		}
		break;
		case 11 :
		{
			SpriteID = ga.ITU.AniSprSET(-1, X, ga.FaseMap.MH-Y, 88, 1, 1, 0x0002);
		}
		break;
		case 12 :
		{
			SpriteID = ga.ITU.AniSprSET(-1, X, ga.FaseMap.MH-Y, 78, 1, 1, 0xB002);
		}
		break;
		case 13 :
		{
			SpriteID = ga.ITU.AniSprSET(-1, X, ga.FaseMap.MH-Y, 53, 1, 1, 0xB001);
		}
		break;
	}
}

public void ChangeAnim(int NewAnim)
{
	switch	(NewAnim)
	{
	// Saludar
	case 0 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=0+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 0+(18*Values[6]), 2, 2, 0x001);
	break;
	// Quieto
	case 1 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=2+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 2+(18*Values[6]), 1, 1, 0x002);
	break;
	// Correr1
	case 2 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=3+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 3+(18*Values[6]), 2, ((10-Values[1])/3)+1, 0x001);
	break;
	// Atacar Derecha
	case 3 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=5+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 5+(18*Values[6]), 2, 2, 0x002);
	break;
	// Atacar Izquierda
	case 4 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=7+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 7+(18*Values[6]), 2, 2, 0x002);
	break;
	// Salto
	case 5 : 
	{
		switch (Values[6])
		{
			case 2 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=14+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 14+(18*Values[6]), 1, 3, 0x001);
			break;
			case 3 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=4+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 4+(18*Values[6]), 1, 3, 0x001);
			break;
			default: if (ga.ITU.AniSprs[SpriteID].FrameIni!=9+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 9+(18*Values[6]), 5, 3, 0x001);
			break;
		}
	}
	break;
	// Salto+Caida
	case 8 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=14+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 14+(18*Values[6]), 1, 1, 0x001);
	break;
	// Caer
	case 6 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=14+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 14+(18*Values[6]), 2, 2, 0x001);
	break;
	// Frenar
	case 7 : if (ga.ITU.AniSprs[SpriteID].FrameIni!=16+(18*Values[6])) ga.ITU.AniSprSET(SpriteID, 16+(18*Values[6]), 1, 1, 0x001);
	break;
	case 10: ga.ITU.AniSprSET(SpriteID, 72, 1, 1, 0xB001);
	break;
	case 11: ga.ITU.AniSprSET(SpriteID, 73, 2, 4, 0xB002);
	break;
	case 12: ga.ITU.AniSprSET(SpriteID, 73, 2, -2, 0xB013);
	break;
	case 20: ga.ITU.AniSprSET(SpriteID, 88, 1, 1, 0xB001);
	break;
	case 21: ga.ITU.AniSprSET(SpriteID, 63, 3, 2, 0x0002);
	break;
	case 22: ga.ITU.AniSprSET(SpriteID, 65, 2, 2, 0x0001);
	break;
	case 23: ga.ITU.AniSprSET(SpriteID, 63, 3, -2, 0x0013);
	break;
	case 30: ga.ITU.AniSprSET(SpriteID, 78, 1, 1, 0xB001);
	break;
	case 31: ga.ITU.AniSprSET(SpriteID, 79, 1, 4, 0xB002);
	break;
	case 40: ga.ITU.AniSprSET(SpriteID, 53, 1, 1, 0xB001);
	break;
	case 41: ga.ITU.AniSprSET(SpriteID, 45, 2, 3, 0xB001);
	break;
	case 42: ga.ITU.AniSprSET(SpriteID, 47, 2, 3, 0xB001);
	break;
	}
}

public void Move()
{
	int Who,Col;
	if (TimeToChange>0) TimeToChange--;
	if (Values[15]<10) Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-Y+2,((9)*12)/8,((7)*12)/8); else Col = 0;
	if ((Col!=0)&&(Values[0]!=5)&&(Values[0]!=4)&&(Values[0]!=8)&&(Values[7]!=11))
	{
		if (Col == 2) TimeToFall = 0;
		if (Values[1] > 0) Values[1]--; else TimeToFall = 0;
		if (X>ga.FaseMap.MW/2) X-=(4*12)/8; else X+=(4*12)/8;
		if (TimeToFall==0) 
		{
			Damage(0);
			TimeToFall = 3;
		} else TimeToFall--;
		
	} else TimeToFall = 3;
	if ((Values[2]==0)&&(Values[0]!=5)&&(Values[0]!=8)) Damage(0);
	switch (Values[7])
	{
		case 0:
		case 1:
		{
			switch (Values[0])
			{
				case 0: 
				{
					if ((ga.KeybB1 != 0)||(ga.KeybX1 != 0)||(ga.KeybY1 != 0))
					{
						Values[0] = 1;
					}
				}
				break;
				case 1: 
				{
					if (Values[7]==0)
					{
						Values[3] = 0;
						if ((ga.KeybB1 == 1)||(ga.KeybB1 == 3)||(ga.KeybB1 == 7)||(ga.KeybB1 == 9))
						{
							TimeToChange = 4;
							if ((ga.KeybB1 == 1)||(ga.KeybB1 == 7)) Values[0] = 3; else Values[0] = 2;
						} else
						{
							if (ga.KeybM1 > 0)
							{
								Values[0] = 4;
								TimeToChange = 15;
								Values[4] = 7;
							} else
							{
								if (((ga.KeybY1 < 0)&&(Values[1]>0))||((ga.KeybY1 > 0)&&(Values[1]<Values[11]))) 
								{
									if ((ga.KeybY1 == -1)&&((Values[1]>1))) 
									{
										Values[14]=1;
									}
									Values[1] += ga.KeybY1*(Values[12]);
								}
							}
							X += (ga.KeybX1*3*12)/8;
						}
					} else
					{
						Values[3] = 0;
						if (((ga.Player.X-X)<14)&&((ga.Player.X-X)>(-14))&&((ga.Player.Y-Y)<14)&&((ga.Player.Y-Y)>(-14))&&(ga.Player.Values[0]!=5))
						{
							TimeToChange = 4;
							if (X>ga.Player.X) Values[0] = 3; else Values[0] = 2;
						} else
						{
							Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-(Y-2+10),((9)*12)/8,((4)*12)/8);
							if (Col!=0)
							{
								Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-Y+2-3,((9)*12)/8,((2)*12)/8);
								if ((Values[1]>5)&&(Col!=0))
								{
									Values[0] = 4;
									TimeToChange = 15;
									Values[4] = 7;
								} else
								{
									if (Values[1] == 0)
									{
										if (Values[13] == 0) Values[13] = (X<60) ? 3 : (-3);
									} else
									{
										Values[1]--;
										Values[14]=1;
										Values[13]=0;
									}
								}
							} else
							{
								Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-(Y-2+30),((9)*12)/8,((30)*12)/8);
								if (Col!=0)
								{
									if (Values[13] == 0) Values[13] = (X<60) ? 3 : (-3);
								} else
								{
									Values[13] = 0;
									if (Values[1]<(((ga.Player.X-60>X)) ? Values[11] : ((Values[11]*15)/10)))
									{
										Values[1] += Values[12];
									} else
									{
										Values[1] = Values[11];	
									} 
								}
							}
							Col = ga.FaseMap.TileColisionOf(X-5+Values[13],ga.FaseMap.MH-(Y-2+(Values[1]/3)),((9)*12)/8,((2)*12)/8);
							if (Col!=0) Values[13] *= (-1);
							X += (Values[13]*12)/8;
						}
					}
					
					Who = ga.WhoIn(Values[15],X-4,Y+7+8,9,8);
					if ((Values[0]!=4)&&(Values[0]!=8))
					{
						if ((Who!=100)&&(Who!=11))
						{
							switch (Who)
							{
								
							case 10 : 
							case 11 : 
							case 13 :
							{
								Damage(0);
							}
							break; 
																	
							case 12 : 
							{
								Values[0] = 8;
								Values[1] = 15;
								Values[3] = 0;
								Values[4] = 10;	
								TimeToChange = 21;
							}
							break;
							
							case 20 : 
							case 21 : 	
							case 22 : 
							case 23 : 	
							case 24 : {	
								
								if (((Values[0]==2)&&(Who==23))||((Values[0]==3)&&(Who==22)))
								{
									if (Values[0]==2)
									{
										ga.ITU.AniSprSET(-1,X-2+7,ga.FaseMap.MH-Y-2,75,2,3,0x0000);
										ga.ITU.AniSprSET(-1,X-2+6,ga.FaseMap.MH-Y-4,75,2,3,0x0000);
										ga.ITU.AniSprSET(-1,X-2+5,ga.FaseMap.MH-Y-3,75,2,3,0x0000);
										X -= (3*12)/8;
									} else
									{
										ga.ITU.AniSprSET(-1,X-2-7,ga.FaseMap.MH-Y-2,75,2,3,0x0000);
										ga.ITU.AniSprSET(-1,X-2-6,ga.FaseMap.MH-Y-4,75,2,3,0x0000);
										ga.ITU.AniSprSET(-1,X-2-5,ga.FaseMap.MH-Y-3,75,2,3,0x0000);
										X += (3*12)/8;
									}
								} else
								{
									if (Who == 20)
									{
										Damage(ga.Player.Values[10]);
									} else
									{
										Damage(ga.Enemies[0].Values[10]/*(ga.Enemies[Who-21].Values[10])*/);
									}
								}
							}
							break;
							
							case 51 : 
							{ 
								if (Values[15]==0)
								{
									ga.gc.CanvasText = ga.Textos[18][0];
									ga.gc.TimeToPaintText = 15;
									ga.gc.TextStyle = 1;
									ga.gc.SoundRES();
									ga.gc.SoundSET(4,1);
								}
								
								Values[2]+=30; 
								if ((Values[15]==0)&&(Values[2]>100)) Values[2] = 100;
							}
							break;
							
							case 52 : 
							{
								if (Values[15]==0)
								{
									ga.gc.CanvasText = ga.Textos[19][0];
									ga.gc.TimeToPaintText = 15;
									ga.gc.TextStyle = 1;
									ga.gc.SoundRES();
									ga.gc.SoundSET(4,1);
								}

								Values[11] += 3;	
							}
							break;
								
							default : {
								
								if (Values[7]==0)
								{
									Values[1] = ga.Enemies[Who-1].Values[1]; 
								} else
								{
									if (Who!=0)
									{
										if (Values[13] == 0) Values[13] = (X<60) ? 3 : (-3);	
									} else
									{
										Values[0] = 4;
										TimeToChange = 15;
										Values[4] = 7;
									}
								}
								
								}
							break;
							}
		
						} else
						{
					
						}
					} else
					{
						if (Who==11)
						{
							Values[0]=8;	
						} 	
					}
					Y += ((Values[1]/3)*12)/8; 
					if ((Values[11]-Values[1])>=0) ga.ITU.AniSprs[SpriteID].Speed = ((Values[11]-Values[1]) /3)+1; else ga.ITU.AniSprs[SpriteID].Speed = 1;
				}
				break;
				case 2: 
				{
					if (TimeToChange==0)
					{
						Values[0] = 1;
						if (Values[7]>0) Values[1] = (Values[1]*9)/10;
					}
					Y += ((Values[1]/3)*12)/8;
					if ((Values[7]==0)&&(ga.KeybB1 == 9)) X += (ga.KeybX1*3*12)/8;
				}	
				break;
				case 3: 
				{
					if (TimeToChange==0)	
					{
						Values[0] = 1;
						if (Values[7]>0) Values[1] = (Values[1]*9)/10;
					}
					if ((Values[7]==0)&&(ga.KeybB1 == 7)) X += (ga.KeybX1*3*12)/8;
					Y += ((Values[1]/3)*12)/8;
				}	
				break;
				case 4: 
				{
					ga.LookColision = true;
					Who = ga.WhoIn(Values[15],X,Y+(Values[1]/3),9,17);
					if (TimeToChange==0)
					{
						if ((Who==11)||(Who==100))
						{
							ga.Combo = 0;
							Values[0] = 1;
							Values[1] = (Values[1]*8)/10;
						} else
						{
							if (Values[15] == 0) ga.Combo++;
							if (ga.Combo>1) 
							{
								ga.gc.CanvasText = ga.Combo + ga.Textos[17][0];
								ga.gc.TimeToPaintText = 15;
								ga.gc.TextStyle = 1;	
							}
							ga.gc.SoundRES();
							ga.gc.SoundSET(6,1);
							if (Who==0)
							{
								ga.Player.Damage(20);
							} else
							{
								if (Who<10) ga.Enemies[Who-1].Damage(20);
								else 
								{
									if (Who == 12)
									{
										TimeToChange = 21;
										Values[1] = 15;
										Values[3] = 0;
										Values[4] = 10;	
									}
								}
							}
							TimeToChange=15;
							Values[1] = Values[1]+3;
							Values[4] = 7;
						}
					}
					if (Who==11) Values[0] = 8;
					Y += ((Values[1]/3)*12)/8;
					Values[3] += Values[4];
					Values[4]--;
				}	
				break;
				case 5: 
				{
					if (Values[2]==0)
					{
						if ((TimeToChange==0)&&(Values[15]==0)&&(ga.LevelFinalized==false))
						{
							ga.GameStatus = 99;	
							ga.GameConti = 0;
						}
					} else
					{
						if ((TimeToChange==0))
						{
							X = ga.FaseMap.StartX;
							Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-Y+2,((9)*12)/8,((7)*12)/8);
							while (Col!=0)
							{
								Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-Y+2,((9)*12)/8,((7)*12)/8);
								while (Col!=0) 
								{
									Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-Y+2,((9)*12)/8,((7)*12)/8);
									Y-= (1*12)/8;
								}
								Y -= (25*12)/8;
								Col = ga.FaseMap.TileColisionOf(X-5,ga.FaseMap.MH-Y+2,((9)*12)/8,((7)*12)/8);
							}
							Values[0] = 1;
						}
						if ((Values[2]>0)&&(TimeToChange<15)&&(Values[1]>0)) Values[2]--;
						if ((Values[1]>0)&&(TimeToChange<15)) Values[1]--;
						Y += ((Values[1]/3)*12)/8;
						if ((Values[2]==0)&&(ga.LevelFinalized==false))
						{
							if (Values[15]==0)
							{
								ga.gc.CanvasText = ga.Textos[22][0];
								ga.gc.TimeToPaintText = 30;
								ga.gc.TextStyle = 0;
							} else
							{
								ga.gc.CanvasText = ga.Textos[21][0];
								ga.gc.TimeToPaintText = 15;
								ga.gc.TextStyle = 1;
							}
							TimeToChange = 30;
						}
					}
				}	
				break;
				case 8: 
				{
					if (TimeToChange==0)
					{
						Damage(10);
					}
					Y += ((Values[1]/3)*12)/8;
					Values[3] += Values[4];
					Values[4]--;
				}	
				break;
			}
		}
		break;
		case 10:
		{
			switch (Values[0])
			{
				case 0: 
				{
					if (TimeToChange==0)
					{
						TimeToChange = 30;
						Values[0] = 1;
						ChangeAnim(11);
						X += (2*12)/8;
						Y += (3*12)/8;
					}
				}
				break;
				case 1:
				{
					if (TimeToChange==0)
					{
						TimeToChange = 4;
						Values[0] = 2;
						ChangeAnim(12);
						
					}
				}
				break;
				case 2:
				{
					if (TimeToChange==0)
					{
						TimeToChange = 100;
						Values[0] = 0;
						ChangeAnim(10);
						X -= (2*12)/8;
						Y -= (3*12)/8;
					}
				}
				break;
			}	
		}
		break;
		case 11:
		{
			switch (Values[0])
			{
				case 0: 
				{
					if (TimeToChange==0)
					{
						TimeToChange = 4;
						Values[0] = 1;
						ChangeAnim(21);
					}
				}
				break;
				case 1:
				{
					if (TimeToChange==0)
					{
						TimeToChange = 30;
						Values[0] = 2;
						ChangeAnim(22);
						Values[13]= (138/30)*((X > 60) ? (-1) : 1);
						Values[4] = 15;
					}
				}
				break;
				case 2:
				{
					if (TimeToChange==0)
					{
						TimeToChange = 4;
						Values[0] = 3;
						ChangeAnim(23);
						Values[3]=0;
					} else
					{
						int Resto;
						Resto = ((Values[13] > 0) ? (142-X) : (-1*(X+5)));
						Values[13]= (Resto/TimeToChange);
						X += (Values[13]*12)/8;
						Values[3] += Values[4];
						Values[4]--;
					}
				}
				break;
				case 3:
				{
					if (TimeToChange==0)
					{
						TimeToChange = 50;
						Values[0] = 0;
						ChangeAnim(20);
					}
				}
				break;
			}	
		}
		break;
		case 12 :
		{
			switch (Values[0])
			{
				case 0: 
				{
					ga.LookColision = true;
					Who = ga.WhoIn(Values[6],X+3,Y,14,20);
					if (Who!=100)
					{
						TimeToChange = 4;
						Values[0] = 1;
						ChangeAnim(31);
					}
				}
				break;
				case 1:
				{
					if (TimeToChange==0)
					{
						Values[0] = 0;
						ChangeAnim(30);
					}
				}
				break;
			}	
		}
		break;
		case 13 :
		{
			switch (Values[0])
			{
				case 0: 
				{
					if (TimeToChange==0)
					{
						Values[13] *= (-1);
						if (Values[13]>0) ChangeAnim(41); else ChangeAnim(42);
						Values[0] = 1;
						TimeToChange = 40; //Cambiar!!
					}
				}
				break;
				case 1:
				{
					if (TimeToChange>0)
					{
						X += (Values[13]*12)/8;
					} else
					{
						ChangeAnim(40);
						TimeToChange = 20;
						Values[0] = 0;
					}
				}
				break;
			}	
		break;
		}
	}
}

public void Refresh()
{
	switch (Values[7])
	{
		case 0:
		case 1:
		{
			switch (Values[0]) //State
			{
				//Waiting for start
				case 0 : ChangeAnim(0);
				break;
			
				//Playing
				case 1 : 
				{
					if ((Values[1]<=2)&&(ga.KeybX1==0)) //Vel
					{
						ChangeAnim(1);
					} else
					{ 
						ChangeAnim(2);
					}
				}
				break;
					//Atacking right
				case 2 : ChangeAnim(3);
				break;
				
				//Atacking left
				case 3 : ChangeAnim(4);
				break;
			
				//Jumping
				case 4 : ChangeAnim(5);
				break;
				
				//Falling
				case 5 : ChangeAnim(6);
				break;
				
				//Falling
				case 8 : ChangeAnim(8);
				break;
				
			}	
			
			if (Values[14] == 1) ChangeAnim(7);
			Values[14] = 0;
			
			for (int Counter=0;Counter<Values[5];Counter++)
			{
				SandT[Counter]--;
				if (SandT[Counter]==0) 
				{
					ga.ITU.AniSprSET(SandID[Counter], 82, 7, 2, 0xD002);
					SandT[Counter] = (5*2)+ga.RND(4);
					ga.ITU.AniSprs[SandID[Counter]].CoorX = X-5+ga.RND(12);//-6+(2*((Counter+(Counter%3))%5));//CaX;
					ga.ITU.AniSprs[SandID[Counter]].CoorY = ga.FaseMap.MH-Y;//CaY;
					SandV[Counter] = !((Values[1]<=2)||(Values[0]==0)||(Values[0]==4)||(Values[0]==8));
				}
				if (SandV[Counter]) ga.ITU.AniSprs[SandID[Counter]].Bank = 0; else ga.ITU.AniSprs[SandID[Counter]].Bank = 10;
			}
			
			ga.ITU.AniSprs[SpriteID].CoorX = X-10;//CaX;
			ga.ITU.AniSprs[SpriteID].CoorY = ga.FaseMap.MH-Y-Values[3]-10;//CaY;
			ga.ITU.AniSprs[ShadowID].CoorX = X-4;//CaX;
			ga.ITU.AniSprs[ShadowID].CoorY = ga.FaseMap.MH-Y+12+(((Values[0]==1)&&(Values[1]>1)&&(ga.KeybY1==-1)) ? -3 : 0);//CaY;
		}
		break;
		case 10 :
		{
			ga.ITU.AniSprs[SpriteID].CoorY = ga.FaseMap.MH-Y;//CaY;
		}
		break;
		case 11 :
		{
			ga.ITU.AniSprs[SpriteID].CoorX = X;
			ga.ITU.AniSprs[SpriteID].CoorY = ga.FaseMap.MH-(Y+Values[3]/3);//CaY;
		}
		break;
		case 13 :
		{
			ga.ITU.AniSprs[SpriteID].CoorX = X;
		}
		break;
	}
}	
	
public void Paint()
{
	Refresh();
}

public void Damage(int Damage)
{
	if (Values[7]<=1)
	{
		Values[0] = 5;
		TimeToChange = 20;
		if (Values[2]>0)
		{
			if (Values[15]==0)
			{
				ga.gc.VibraSET(200);
				ga.gc.SoundRES();
				ga.gc.SoundSET(5,1);
				ga.gc.CanvasText = ga.Textos[20][ga.RND(ga.Textos[20].length)];
				ga.gc.TimeToPaintText = 5;
				ga.gc.TextStyle = 1;
			}
			if (Values[2]>=Damage) Values[2] -= Damage; else
			{
				Values[2] = 0;
				if (Values[15]==0)
				{
					ga.gc.CanvasText = ga.Textos[22][0];
					ga.gc.TimeToPaintText = 30;
					ga.gc.TextStyle = 0;
				} else
				{
					ga.gc.CanvasText = ga.Textos[21][0];
					ga.gc.TimeToPaintText = 15;
					ga.gc.TextStyle = 1;
				}
			}
			ga.ITU.AniSprSET(-1,X-2,ga.FaseMap.MH-Y-7,67,1,6,0x0000);
		}
	}
}

}