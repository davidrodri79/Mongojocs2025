package com.mygdx.mongojocs.sexysnake;

import com.mygdx.mongojocs.midletemu.Image;

public class SnakeManager {
	public static int snakeX, snakeY;
	private static int oldX, oldY;
	private static int snakeXi, snakeYi;
	private static int snakeL;
	private static int snakeD;
	private static byte oldPosX[];
	private static byte oldPosY[];
	private static byte oldDir[];
	private static byte restoreBuffer[];
	private static int snakeHD, snakeVD;
	private static int maxLength;
	public static int rabbitsEaten;
	//private static int lastNotCounts = 0;
	private static int eatinRabbit = 0;
	private static int snakeState = 0;
	private static Image tileset;
	public static int lives;
	private static int pendingL = 0;
	public static int rcombo;
	public static int combon;
	public static int emptySq;
	public static int comboFTG;
	
	private SnakeManager() {}
	
	public static void startSnakePos(int level) {
		snakeX = (BackgroundManager.MAP_WIDTH) - 4;
		snakeY = (BackgroundManager.MAP_HEIGHT >> 1);
		snakeXi = snakeX * BackgroundManager.TILE_SIZE;
		snakeYi = snakeY * BackgroundManager.TILE_SIZE;
		
		if(level == 3) {
			snakeY += 2;
			snakeYi += BackgroundManager.TILE_SIZE * 2;
		}
		
		oldX = snakeX;
		oldY = snakeX;
		snakeHD = -1;
		snakeVD = 0;
		snakeL = 3;
		snakeD = 0;
		rcombo = 0;
		emptySq = 0;
		pendingL = 0;
		combon = -1;
		comboFTG = 0;
		
		oldPosX[0] = (byte) (snakeX);
		oldPosY[0] = (byte) (snakeY);
		oldDir[0] = (byte) (3);
		oldPosX[1] = (byte) (snakeX + 1);
		oldPosY[1] = (byte) (snakeY);
		oldDir[1] = (byte) (3);
		oldPosX[2] = (byte) (snakeX + 2);
		oldPosY[2] = (byte) (snakeY);
		oldDir[2] = (byte) (3);
		rabbitsEaten = 0;
	}
	
	public static void start(int maxL, Image img, int level) {
		rabbitsEaten = 0;
		maxLength = maxL;
		 
		oldPosX = new byte[maxLength];
		oldPosY = new byte[maxLength];
		oldDir = new byte[maxLength];
		restoreBuffer = new byte[maxLength];
		
		startSnakePos(level);
		tileset = img;
	}
	
	public static void moveSnake(int x, int y) {
		int	osd = snakeD;
		int	osH = snakeHD;
		int	osV = snakeVD;
		
		if(x != 0 && y != 0) {
			if(x > 0 && y > 0) {
				y = 0;
			} else {
				x = 0;
			}
		}
		
		//if(x != 0 && y != 0) return; //ignore multiple directions at the same time.
		
		if(x != 0 || y != 0) {
			snakeHD = x;
			snakeVD = y;
			if(snakeVD == 0) {
				if(snakeHD > 0) {
					snakeD = 1;
				} else {
					snakeD = 0;
				}
			}
			
			if(snakeHD == 0) {
				if(snakeVD > 0) {
					snakeD = 3;
				} else {
					snakeD = 2;
				}
			}
			//snakeD = ((snakeHD + 1) >> 1) + (((snakeVD + 1) >> 1) << 1);
		}
		//prevent snake from going to the oposite direction, colliding with herself
		if(osd == 0 && snakeD == 1) {snakeD = osd; snakeHD = osH; snakeVD = osV;}
		if(osd == 1 && snakeD == 0) {snakeD = osd; snakeHD = osH; snakeVD = osV;}
		if(osd == 2 && snakeD == 3) {snakeD = osd; snakeHD = osH; snakeVD = osV;}
		if(osd == 3 && snakeD == 2) {snakeD = osd; snakeHD = osH; snakeVD = osV;}
		
		snakeXi += snakeHD * 8;
		snakeYi += snakeVD * 8;
		
		snakeX = snakeXi >> 3;
		snakeY = snakeYi >> 3;
		
		//here!
		if(oldX != snakeX || oldY != snakeY) {
			if(pendingL == 1) {
				pendingL = 0;
				incLength();
			} else {
				for(int i = snakeL - 1; i > 0; i--) {
					oldPosX[i] = oldPosX[i - 1];
					oldPosY[i] = oldPosY[i - 1];
					oldDir[i] = oldDir[i - 1];
				}
			}
		}
		
		if(snakeX < 0) {
			snakeX = 0;
		}
		
		if(snakeY < 0) {
			snakeY = 0;
		}
		
		if(snakeX == BackgroundManager.MAP_WIDTH) {
			snakeX = BackgroundManager.MAP_WIDTH - 1;
		}
		
		if(snakeY == BackgroundManager.MAP_HEIGHT) {
			snakeY = BackgroundManager.MAP_HEIGHT - 1;
		}
		
		oldX = snakeX;
		oldY = snakeY;
		
		oldPosX[0] = (byte) snakeX;
		oldPosY[0] = (byte) snakeY;
		oldDir[0] = (byte) snakeD;
	}
	
	public static void incLength() {
		if(snakeL < maxLength) {
			snakeL++;
			
			for(int i = snakeL - 1; i > 0; i--) {
				oldPosX[i] = oldPosX[i - 1];
				oldPosY[i] = oldPosY[i - 1];
				oldDir[i] = oldDir[i - 1];
			}
			
			oldPosX[0] = (byte) snakeX;	//oldPosX[1];
			oldPosY[0] = (byte) snakeY;	//oldPosY[1];
			oldDir[0] = (byte) snakeD;
		}
	}
	
	public static void clearSnake() {
		for(int i = 0; i < snakeL; i++) {
			int offs = oldPosX[i] + oldPosY[i] * BackgroundManager.MAP_WIDTH;
			BackgroundManager.map[offs] = restoreBuffer[i];
		}
	}
	
	public static void drawSnake(int status) {
		byte[] map = BackgroundManager.map;
		int newType = 0;
		int oldType = 0;
		int c = 0;
		int t = -1;
		int type = 0;
		int max = snakeL - 1;
		int oldt = 0;
		
		for(int i = 0; i < snakeL; i++) {
			int offs = oldPosX[i] + oldPosY[i] * BackgroundManager.MAP_WIDTH;
			restoreBuffer[i] = map[offs];
		}
		
		if(snakeState == 1) {
			eatinRabbit++;
			if(eatinRabbit > 5) {
				snakeState = 2;
			}
		}
		
		if(snakeState == 2) {
			eatinRabbit--;
			if(eatinRabbit < 0) {
				snakeState = 0;
			}
		}
		
		if(status >= 0 && status < 0xff) {
			eatinRabbit = 6 - (status << 1);
			if(eatinRabbit == 6) eatinRabbit = 5;
			//0 --> 5
			//1 --> 4
			//2 --> 2
			//3 --> 0
			snakeState = 1;
		}
		
		if(status == -1) {
			snakeState = 0;
			eatinRabbit = 0;
		}
		
		for(int i = 1; i < max; i++) {
			//old
			if(oldPosX[i - 1] == oldPosX[i] && oldPosY[i - 1] == oldPosY[i]) {
				oldType = newType;
			} else {
				if(oldPosX[i - 1] == oldPosX[i]) {
					if(oldPosY[i - 1] > oldPosY[i]) {
						oldType = 3;
					} else {
						oldType = 2;
					}
				}
				
				if(oldPosY[i - 1] == oldPosY[i]) {
					if(oldPosX[i - 1] > oldPosX[i]) {
						oldType = 1;
					} else {
						oldType = 0;
					}
				}
				
				//new
				if(oldPosX[i + 1] == oldPosX[i]) {
					if(oldPosY[i + 1] > oldPosY[i]) {
						newType = 3;
					} else {
						newType = 2;
					}
				}
				
				if(oldPosY[i + 1] == oldPosY[i]) {
					if(oldPosX[i + 1] > oldPosX[i]) {
						newType = 1;
					} else {
						newType = 0;
					}
				}
			}
			
			type = newType * 10 + oldType;
			switch(type) {
				case 00:
				case 01:
					c = 0 + 22;
					t = 1;
					break;
				case 11:
				case 10:
					c = 0 + 22;
					t = 0;
					break;
				case 32:
				case 22:
					c = 1 + 22;
					t = 2;
					break;
				case 23:
				case 33:
					c = 1 + 22;
					t = 3;
					break;
				case 02:
					c = 3;
					t = 1;
					break;
				case 20:
					c = 3;
					t = 3;
					break;
				case 03:
					c = 1;
					t = 1;
					break;
				case 30:
					c = 1;
					t = 2;
					break;
				case 12:
					c = 2;
					t = 0;
					break;
				case 21:
					c = 2;
					t = 3;
					break;
				case 13:
					c = 0;
					t = 0;
					break;
				case 31:
					c = 0;
					t = 2;
					break;
			}
			int offs = oldPosX[i] + oldPosY[i] * BackgroundManager.MAP_WIDTH;
			map[offs] = (byte) (c + 11);
		}
		
		//tail
		if(t >= 0) {
			int offs = oldPosX[snakeL - 1] + oldPosY[snakeL - 1] * BackgroundManager.MAP_WIDTH;
			map[offs] = (byte) (13 + 22 + t); //snake tail
		}
	}
	
	public static void drawSnakeHead(GameCanvas gc) {
		int eatFrame = eatinRabbit >> 1;
		switch(snakeD) {
		case 0:
			if(snakeState == 0) {
				gc.showImage(gc.scr, tileset, 0 * 8, 2 * 8, 16, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX - BackgroundManager.TILE_SIZE, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
			} else {
				if(eatFrame == 2) {
					gc.showImage(gc.scr, tileset, 16 + (eatFrame) * 16, 2 * 8, 24, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX - BackgroundManager.TILE_SIZE * 2, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
				} else {
					gc.showImage(gc.scr, tileset, 16 + (eatFrame) * 16, 2 * 8, 16, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX - BackgroundManager.TILE_SIZE, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
				}
			}
			break;
		case 1:
			if(snakeState == 0) {
				gc.showImage(gc.scr, tileset, 9 * 8, 2 * 8, 16, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
			} else {
				if(eatFrame == 2) {
					gc.showImage(gc.scr, tileset, 9 * 8 + 16 + (eatFrame) * 16, 2 * 8, 24, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
				} else {
					gc.showImage(gc.scr, tileset, 9 * 8 + 16 + (eatFrame) * 16, 2 * 8, 16, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
				}
			}
			break;
		case 2:
			if(snakeState == 0) {
				gc.showImage(gc.scr, tileset, 0, 4 * 8, 24, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX - BackgroundManager.TILE_SIZE, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
			} else {
				if(eatFrame == 1) eatFrame = 0;		//fix xungo!! nomes te 2 frames enlloc de 3
				if(eatFrame == 2) eatFrame = 1;
				gc.showImage(gc.scr, tileset, 0 + 24 + (eatFrame) * 24, 4 * 8, 24, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX - BackgroundManager.TILE_SIZE, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY - BackgroundManager.TILE_SIZE);
			}
			break;
		case 3:
			if(snakeState == 0) {
				gc.showImage(gc.scr, tileset, 9 * 8, 4 * 8, 24, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX - BackgroundManager.TILE_SIZE, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY);
			} else {
				gc.showImage(gc.scr, tileset, 9 * 8 + 24 + (eatFrame) * 24, 4 * 8, 24, 16, snakeX * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollX - BackgroundManager.TILE_SIZE, snakeY * BackgroundManager.TILE_SIZE - Scroll.instance.ScrollY);
			}
			break;
		}
	}
	
	private static int distanceTo(int maxD,int minT, int maxT, int[] dirFlag) {
		int rt = -1;
		byte c;
		int offs = 0;
		byte[] map = BackgroundManager.map;
		int max = BackgroundManager.map.length;
		
		for(int i = maxD; i >= 1; i--) {
			offs = snakeX + snakeY * BackgroundManager.MAP_WIDTH;
			
			switch(snakeD) {
				case 0:
					offs -= i;
					break;
				case 1:
					offs += i;
					break;
				case 2:
					offs -= i * BackgroundManager.MAP_WIDTH;
					break;
				case 3:
					offs += i * BackgroundManager.MAP_WIDTH;
					break;
			}
			
			//predictor (c) bp
			if(offs < 0 || offs >= max) {
				c = -1;
			}  else {
				c = map[offs];
			}
			
			if(c > minT && c <= maxT) { //22, 32
				rt = i;
				if(dirFlag != null) {
					dirFlag[snakeD] = 1;
				}
			}
		}
		
		return rt;
	}
	
	public static int logic() {
		int offs;
		byte[] map = BackgroundManager.map;
		int rt = -1;
		byte c;
		int max = BackgroundManager.map.length;
		
		rt = distanceTo(3, 22, 32, null);	//find a rabbit
		int k1 = distanceTo(3, 10, 20, null);	//find a collision
		int k2 = distanceTo(3, 32, 42, null);	//find a collision
		
		
		//the snake won't open the mouth if a wall is between him and a rabbit 
		if(k1 > 0 && (k1 < rt)) {
			rt = -1;
		}
		
		if(k2 > 0 && (k2 < rt)) {
			rt = -1;
		}
		
		//real
		c = map[snakeX + snakeY * BackgroundManager.MAP_WIDTH];
		
		if(c > 22 && c <= 32) {
			RabbitManager.killRabbit(snakeX, snakeY);
			rabbitsEaten++;
			
			if(emptySq < 7) {
				rcombo++;
				emptySq = 0;
			} else {
				//combon = -1;
				emptySq = 0;	//reset the counter of n� of empty squares
				rcombo = 1;
			}
			
			pendingL = 1;
			rt = 0;
		} else {
			emptySq++;
			if(emptySq >= 7 && rcombo > 1) {
				combon = rcombo;
				comboFTG = 16;
				rcombo = 0;
			}
		}
		
		return rt;
	}
	
	public static int snakeCollision() {
		int rt = 0;
		byte c = BackgroundManager.map[snakeX + snakeY * BackgroundManager.MAP_WIDTH];
		
		if(c >= 11 && c <= 20) {
			rt = 0xff;
		}
		
		if(c >= 33 && c <= 42) {
			rt = 0xff;
		}
		
		return rt;
	}
	
	public static void release() {
		oldPosX = null;
		oldPosY = null;
		oldDir = null;
		tileset = null;
		System.gc();
	}
}
