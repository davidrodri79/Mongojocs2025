package com.mygdx.mongojocs.waterrace;


public class Moto {
	// Velocidad relativa (dependiente de la version del movil)
	static final int reVel =  20;


	int x, y;        // coordenadas x, y
	int vx, vy;      // velocidad x, y

	int stat;        // estado: acelerando, girando der, girando izq, ...
	int jetSpr;      // sprite actual
	int splashSpr;   // sprite del agua


	int fac;	       // frame anim counter, contador utilizado para ralentizar
	// las animaciones de sprites
	int frameCounter;// contador de frames de este estado

	int tioy, tiovy; // coordenada y, y velodidad y del motorista

	int compx, compy;  // computador girando acelerando, girando?
	int compx2, compy2;

	int pos;              // puesto (1� posici�n?)


	int color;

	boolean isComputer;

	int vyCount;
	int vxCount;

	int velMax;
	int agre;

	int agrex, agrex2;

	int ftime;
	boolean blinks;
	long blinksTime;

	public Moto() {}

	public void init(int xx, int yy, int c, boolean isComp, Game ga, int difficulty) {
		vx = vy = 0;
		x = xx * 8;
		y = yy * 8;

		isComputer = isComp;

		color = c;

		jetSpr = ga.RND(3) + 2;
		stat = 0;

		splashSpr = 0;
		fac = 0;
		frameCounter = 0;

		pos = 0;

		vyCount = 0;
		vxCount = 0;

		blinks = false;
		ftime = 0;

		switch(difficulty) {
		case 0:
			agre = 0;
			velMax = 23 - ((ga.RND(4) + ga.RND(4))>>2);
			break;
		case 1:
			agre = ga.RND(2);
			velMax = 23 - ((ga.RND(16)*ga.RND(8))>>4);
			break;
		case 2:
			agre = ga.RND(3);
			velMax = 24 - ((ga.RND(16)*ga.RND(4))>>4);
			break;
		}

		if(!isComp) {
			velMax = 24;
			setBlinks(System.currentTimeMillis());
		}

		agrex = 0;
	}

	int acelerar(int acc) {
		int tmp;
		int res = 0;
		if(acc < 0) {
			// Acelerar con marchas
			tmp = -vy / 8; // ahora tmp tiene el numero de marcha (0, 1, 2, 3)

			vyCount++;

			if(vyCount < 0)
				vyCount = 0;

			tmp = tmp*tmp*16/reVel + 1;
			if(vyCount >= tmp) {
				res = -vyCount / tmp;
				vyCount %= tmp;
			}
		} else {
			tmp = -64/reVel + 1;

			if(acc == 0)
				tmp *= 3;

			vyCount--;

			if(vyCount > 0)
				vyCount = 0;

			if(vyCount <= tmp) {
				res = vyCount / tmp;
				vyCount %= tmp;
			}
		}

		return res;
	}

	int girar(int acc) {
		int res = 0;

		vxCount += acc*reVel/3;

		res = vxCount / 3;
		vxCount %= 3;

		return res;
	}



	private void moveByKeyb(Game ga) {

		if(stat == 3) {
			return;
		}

		if(stat == 4) {
			moveByComputer(ga);
			return;
		}

		compx = ga.KeybX1;
		compy = ga.KeybY1;

		vx += girar(compx);
		vy += acelerar(compy);

		if(vx > 12)
			vx = 12;
		if(vx < -12)
			vx = -12;
		if(vy > 0)
			vy = 0;
		if(vy < -velMax)
			vy = -velMax;

		if(ga.KeybX1==0) {
			if(vx != 0)
				vx /= 2;
		}

		if(ga.KeybX1!=ga.KeybX2) {
			jetSpr = 2;
			fac = 0;
		}

		if(ga.KeybX1 == 0) {
			stat = 0;
		} else {
			if(ga.KeybX1 < 0) {// Derecha
				stat = 1;
			} else {		// Izquierda
				stat = 2;
			}
		}
	}

	private int abs (int i) {
		return (i > 1)?i:-i;
	}

	private void moveByComputer(Game ga) {
		int tmpx = ((x<<3) + vx*reVel)>>6;
		int tmpy = ((y<<3) + vy*reVel)>>6;
		int tmpyDist;

		if(stat == 3)
			return;

		compx2 = compx;
		compy2 = compy;

		// Aceleramos?
		tmpyDist = 16 - reVel*vy/18; // - reVel*vy/24; // (12)

		compy = ga.collides(tmpx - 5, tmpy - 9 - 4 - tmpyDist,     11,  4 + tmpyDist  )?0:-1;
		if(     ga.collides(tmpx - 3, tmpy - 9      - 8 - (tmpyDist>>1), 7,  - 8 - (tmpyDist>>1))) {
			compy = 1;
		}

		if(compy == -1 && vy > -3) {
			compy = -ga.RND(3);
			if(compy < -1)
				compy = -1;
		}

		// Giramos?
		compx = 0;
		tmpyDist = 28 - reVel*vy/48;// max 24
		if( ga.collides(tmpx + 3,   tmpy - 9 - tmpyDist*2,  7, tmpyDist ) &&
				ga.collides(tmpx + 10,  tmpy - 9 - (tmpyDist>>1), 7, tmpyDist ) ) {
			compx += -1;
		}

		if(ga.collides(tmpx - 10, tmpy - 9 - (tmpyDist<<1), 7, tmpyDist ) &&
				ga.collides(tmpx - 17, tmpy - 9 - tmpyDist,  7, tmpyDist     ))
			compx += 1;


		tmpyDist = tmpyDist * 2 / 3;
		// estamos a punto de colisionar?
		if(compx == 0 && compy != -1) {
			boolean r = false;
			boolean l = false;

			for(int c = 0; (c < 4) && !r && !l; c++) {
				r = ga.collides((x >> 3) - 10 - c*6, tmpy - 9 - 4 - tmpyDist, 7,  4 + tmpyDist );
				l = ga.collides((x >> 3) + 3  + c*6, tmpy - 9 - 4 - tmpyDist, 7,  4 + tmpyDist );
			}

			if(r && !l) {
				compx = 1;
				if(!ga.collides((x>>3) - 3, (y>>3) - 9 - tmpyDist/3, 7, tmpyDist/3)) {
					compy = -1;
				}
			}

			if(l && !r) {
				compx = -1;
				if(!ga.collides((x>>3) - 3, (y>>3) - 9 - tmpyDist/3, 7, tmpyDist/3)) {
					compy = -1;
				}
			}

			if(!l && !r) {
				compx = ga.RND(2)*2 - 1;
			}
		}

		boolean agreCambiada = false;

		// Tenemos en cuenta el resto de jugadores (solamente si no necesitamos girar)
		// y si somos agresivos o estamos parados
		if(compx == 0) {
			if((ga.playerCollides(this, tmpx, tmpy - 9) != null) || vy > -2) {
				if(vy <= -2) {
					Moto r = ga.playerCollides(this, tmpx - 12,  tmpy - 13);
					Moto l = ga.playerCollides(this, tmpx + 5,   tmpy - 13);

					if(l == r && l != null) {
						boolean tr = r != null;
						boolean tl = l != null;

						if(tr && !tl) {
							agrex = +1;
							agreCambiada = true;
						}
						if(tl && !tr) {
							agrex = -1;
							agreCambiada = true;
						}
					}
				}

				if(!agreCambiada) {
					agreCambiada = true;
					if(ga.collides(tmpx - 3 - 14, tmpy - 9, 14, 13)) {
						agrex += (ga.RND(100)&1)*2;
					} else {
						if(ga.collides(tmpx - 3,    tmpy - 9, 14, 13)) {
							agrex += -(ga.RND(100)&1)*2;
						} else {
							agrex += (ga.RND(100)%5) - 2;
						}
					}
				}
			}
		}


		// enemigos por detras? cerremosles el camino!!! (solo si somos agresivos)
		if((agre != 0) && (compx == 0) && (vy < -8)) {
			Moto r = ga.playerCollides(this, (x>>3) - 12,  (y>>3) + 8);
			Moto l = ga.playerCollides(this, (x>>3) + 5,   (y>>3) + 8);

			boolean br = r != null;
			boolean bl = l != null;

			if(l == r && l != null) {
				if(br && !bl) {
					agrex = -3;
					agreCambiada = true;
				}
				if(bl && !br) {
					agrex = +3;
					agreCambiada = true;
				}
			}
		}

		if(agrex > 8)
			agrex = 8;
		if(agrex < -8)
			agrex = -8;
		if(agrex > 4) {
			agrex2 = 1;
		}
		if(agrex < -4) {
			agrex2 = -1;
		}

		if(agrex2 != 0 && compx == 0) {
			compx = agrex2;
		} else {
			agrex2 = 0;
		}

		if(!agreCambiada) {
			if(agrex != 0) {
				agrex -= (agrex > 0)?1:-1;
			} else {
				agrex2 = 0;
			}
		}

		vx += girar(compx);
		vy += acelerar(compy);

		if(vx > 8)
			vx = 8;
		if(vx < -8)
			vx = -8;
		if(vy > 0)
			vy = 0;
		if(vy < -velMax)
			vy = -velMax;

		if(compx == 0) {
			if(vx != 0)
				vx -= ((vx > 0)?1:-1);
		}

		if(compx != compx2) {
			jetSpr = 2;
			fac = 0;
		}

		if(compx == 0) {
			stat = 0;
		} else {
			if(compx < 0) {// Derecha
				stat = 1;
			} else {		// Izquierda
				stat = 2;
			}
		}
	}


	private int checkCollisions(Game ga) {
		if(stat >= 3) {
			return 0;
		}

		int fuerzaxCol = 0, fuerzayCol = 0;
		boolean col = ga.collides((x>>3) - 3, (y>>3) - 9, 7, 13);

		if (!col) {
			int tmpx = ((x<<3) + vx*reVel)>>6;
			int tmpy = ((y<<3) + vy*reVel)>>6;

			boolean mapaCol = ga.collides(tmpx - 3, tmpy - 9, 7, 13);
			Moto playerCol = ga.playerCollides(this, tmpx, tmpy);
			Moto playerColx = null, playerColy = null;
			if( mapaCol || playerCol != null) {
				int tvx = vx, tvy = vy;

				// Comprovar colisiones con el mapa
				if(mapaCol) {
					if(ga.collides(tmpx - 3, (y>>3) - 9, 7, 13)) {
						fuerzaxCol += abs(vx);
					}

					if(ga.collides((x>>3) - 3, tmpy - 9, 7, 13)) {
						fuerzayCol +=  abs(vy);
					}

					// Comprovamos colisiones en x
					if(fuerzaxCol != 0) {
						while(ga.collides((((x<<3) + tvx*reVel)>>6) - 3, (y>>3) - 9, 7, 13) && (tvx != 0) ) {
							// optimizacion s40
							tvx=tvx/2;
							/*
							              if((tvx>>3) == 0) {
							                tvx-=(tvx>0)?1:-1;
							              } else {
							                tvx-=(tvx>0)?8:-8;
							              }
							*/
						}
					}

					// Comprobamos colision en y
					if(fuerzayCol != 0) {
						while(ga.collides((x>>3) - 3, (((y<<3) + tvy*reVel)>>6) - 9, 7, 13) && (tvy != 0)) {
							// optimizacion s40
							tvy=tvy/2;
							/*
							              if((tvy>>3) == 0) {
							                tvy-=(tvy>0)?1:-1;
							              } else {
							                tvy-=(tvy>0)?8:-8;
							              }
							*/
						}
					}
				}

				if(mapaCol) {
					playerCol = ga.playerCollides(this, ((x<<3) + tvx*reVel)>>6, ((y>>3) + tvy*reVel)>>6);
				}

				// Comprovar colisiones con el resto de jugadores
				if(playerCol != null) {

					// Comprovamos colisiones en x
					if(tvx != 0) {
						Moto mtmp;
						while(((mtmp=ga.playerCollides(this, ((x<<3) + tvx*reVel)>>6, (y>>3))) != null) && (tvx != 0) ) {
							// optimizacion s40
							tvx=tvx/2;
							/*
							              if((tvx>>3) == 0) {
							                tvx-=(tvx>0)?1:-1;
							              } else {
							                tvx-=(tvx>0)?8:-8;
							              }
							*/
							playerColx = mtmp;
						}
					}

					// Comprobamos colision en y
					if(tvy != 0) {
						Moto mtmp;

						while(((mtmp = ga.playerCollides(this, (x>>3), ((y<<3) + tvy*reVel)>>6)) != null) && (tvy != 0)) {
							// optimizacion s40
							tvy=tvy/2;
							/*
							            if((tvy>>3) == 0) {
							                tvy-=(tvy>0)?1:-1;
							              } else {
							                tvy-=(tvy>0)?8:-8;
							              }
							*/
							playerColy = mtmp;
						}
					}
				}

				x += (tvx*reVel)>>3;
				y += (tvy*reVel)>>3;

			} else {
				// si no ha habido ninguna colisi�n podemos actualizar la posici�n del jugador
				x+=(vx*reVel)>>3;
				y+=(vy*reVel)>>3;
			}


			if(x < 16)
				x = 16;
			if(x > 880)
				x = 880;

			if (fuerzayCol > 0) {
				boolean r = false;
				boolean l = false;

				for(int c = 0; (c < 4) && !r && !l; c++) {
					r = ga.collides((x>>3) - 11 - c*6, (y>>3) - 10, 9, 13);
					l = ga.collides((x>>3) + 4  + c*6, (y>>3) - 10, 9, 13);
				}

				if(r && !l) {
					x += 8;
				}

				if(l && !r) {
					x -= 8;
				}

				vy = 0;
				y += 8;
			} else {
				if(playerColy != null) {
					int tmp;
					tmp = playerColy.vy;
					playerColy.vy = vy;
					vy = tmp;
				}
			}
			if (fuerzaxCol > 0) {
				vx = (-vx * 3)>>2;
			} else {
				if(playerColx != null) {
					int tmp;
					tmp = playerColx.vx;
					playerColx.vx = vx;
					vx = tmp;
				}
			}
		}


		// Si estamos colisionando actualmente necesitamos rectificar
		int count = 0;
		while(ga.collides((x>>3) - 3, (y>>3) - 9, 7, 13) && (count < 2) ) {
			if((x>>2) != 112) {
				x -= (x > 112*4)? 4: -4;
			}

			y += 4;

			vy = 0;
			vx = 0;

			boolean l = ga.collides(x/8 - 10, y/8 - 9, 7, 13);
			boolean r = ga.collides(x/8 + 3,  y/8 - 9, 7, 13);

			if( !l && r) {
				x -= 12;
			}

			if( !r && l) {
				x += 12;
			}

			count++;
		}

		return fuerzaxCol + fuerzayCol;
	}


	private void animSprites() {
		fac++;

		if(stat <= 2) {
			if(fac > ((48 + vy)/reVel) ) {
				jetSpr++;
				splashSpr = 1 - splashSpr;
				fac -= ((48 + vy)/reVel);
			}
		} else {
			if(fac > (36*2 / reVel)) {
				jetSpr++;
				fac -= (36*2 / reVel);
			}
		}

		switch(stat) {
		case 0:
			if(vy == 0) {
				jetSpr = ((jetSpr - 3)&1) + 3;
			} else {
				jetSpr = ((jetSpr - 2)&3) + 2;
			}
			break;

		case 1:
			jetSpr = jetSpr&1;
			break;

		case 2:
			jetSpr = ((jetSpr-6)&1) + 6;
			break;

		case 3:
			jetSpr = (jetSpr<8)?jetSpr:7;
			break;

		case 4:
			jetSpr = 4;
			break;
		}
	}

	public int movePlayer(Game ga) {

		if(pos != 0) {
			y += (vy*reVel)>>3;
			return 0;
		}

		moveByKeyb(ga);

		int fuerzaCol = checkCollisions(ga);

		if(fuerzaCol > 16 && !blinks) {		// muerte
			stat = 3;	// Dead
			fac = 0;
			frameCounter = 0;
			jetSpr = 0;

			vx = vy = 0;

			ga.setVibra(1000);
			ga.setSound(2, 1);

			return 1;	// comunicamos a JuegoRUN que hemos muerto
		}
		else {
			if(fuerzaCol > 0) {
				ga.setVibra(200);
				ga.setSound(4, 1);
			}
		}

		if(y <= 48*8 && pos == 0) {					// Meta
			stat = 4;	// Finish
			fac = 0;
			frameCounter = 0;
			jetSpr = 0;

			pos = ++ga.lastPosition;
			ftime = (int)(ga.currentTimeGame - ga.timeGameInit);

			return 2;	// Comunicamos a JuegoRUN que hemos finalizado la partida
		}

		return 0;
	}

	public void setBlinks(long currentTime) {
		blinksTime = currentTime + 5000;
		blinks = true;
	}

	public int moveComputer(Game ga) {

		if(pos != 0) {
			y += (vy*reVel)>>3;
			return 0;
		}

		moveByComputer(ga);

		int fuerzaCol = checkCollisions(ga);

		if(fuerzaCol > 16) {		// muerte
			stat = 3;	// Dead
			fac = 0;
			frameCounter = 0;
			jetSpr = 0;

			vx = vy = 0;

			return 1;	// comunicamos a JuegoRUN que hemos muerto
		}

		if(y <= 48*8 && pos == 0) {					// Meta
			stat = 4;	// Finish
			fac = 0;
			frameCounter = 0;
			jetSpr = 0;

			pos = ++ga.lastPosition;
			ftime = (int)(ga.currentTimeGame - ga.timeGameInit);

			return 2;	// Comunicamos a JuegoRUN que hemos finalizado la partida
		}

		return 0;
	}


	public void draw(GameCanvas gc) {
		animSprites();
		int sangre = (gc.currentStage==0)?1:0;

		// draw moto

		if(blinks) {
			if(blinksTime < gc.ga.currentTimeGame) {
				blinks = false;
			} else {
				if((frameCounter&1) == 0) {
					frameCounter++;
					return;
				}
			}
		}

		if(stat != 3) {
			gc.putSprite(jetSpr + (color%3)*3*9, ((x*3)>>4) - 13 - gc.sc.ScrollX, ((y*3)>>4) - 13 - gc.sc.ScrollY);
			if(compy==-1) {
				gc.putSprite(splashSpr*9 + 8,  ((x*3)>>4) - 13 - compx - gc.sc.ScrollX, ((y*3)>>4) + 13 - gc.sc.ScrollY);
			}
		} else { // muerte
			if(isComputer || frameCounter < 32 || (((frameCounter * 8 / gc.ga.fps)&1) != 0 )) {
				int tmp = (frameCounter >= 16)?16:frameCounter;

				gc.putSprite(jetSpr + ((color%3)*3 + 1 + sangre)*9,  ((x*3)>>4) - 13 - gc.sc.ScrollX, ((y*3)>>4) - 13 - gc.sc.ScrollY - tmp);
				gc.putSprite(jetSpr + (9 + sangre)*9,            ((x*3)>>4) - 13 - gc.sc.ScrollX, ((y*3)>>4) - 21 - gc.sc.ScrollY - (tmp<<1));
			}
		}

		frameCounter++;
	}
}

