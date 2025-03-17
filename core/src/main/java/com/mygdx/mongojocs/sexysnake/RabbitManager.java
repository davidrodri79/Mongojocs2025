package com.mygdx.mongojocs.sexysnake;


import com.mygdx.mongojocs.midletemu.Image;

public class RabbitManager {
	private static int maxTime;
	private static int maxRabbits;
	private static byte rabbitX[];
	private static byte rabbitY[];
	private static byte rabbitFrame[];
	private static int rabbitDelay[];
	private static byte rabbitType[];
	private static byte rabbitStatus[];
	private static byte restoreBuffer[];
	private static byte rbCnt[];
	private static int maxFreq;
	private static Image rabbits;
	//private static final byte[] rbAnim = {5,5,6,7,6,5,5,8,9,8,5,5};
	private static final byte[] rbAnim = {4,4,5,6,5,4,4,7,8,7,4,4};
	private static byte rbToSpawn[];
	private static byte rbtsc;
	
	
	private RabbitManager() {}
	
	public static void start(int nRab, int maxt, int freq, Image rb) {
		clearMemory();
		maxTime = maxt;
		maxRabbits = nRab;
		
		rabbitX = new byte[nRab];
		rabbitY = new byte[nRab];
		rabbitFrame = new byte[nRab];
		rabbitDelay = new int[nRab];
		rabbitType = new byte[nRab];
		rabbitStatus = new byte[nRab];
		rbCnt = new byte[nRab];
		rbToSpawn = new byte[nRab];
		restoreBuffer = new byte[nRab];
		rbtsc = 0;
		
		byte[] map = BackgroundManager.map;
		
		for(int i = 0; i < nRab; i++) {
			spawnRabbit(i, map);
			rbCnt[i] = 0;
			rabbitType[i] = 4;
			rabbitDelay[i] = 10 + MiscTools.random(80);
		}
		
		map = null;
		rabbits = rb;
		System.gc();
	}
	
	private static void spawnRabbit(int i, byte[] map) {
		boolean okRabbit = false;
		byte x = 0;
		byte y = 0;
		byte c = 0;
		
		while(!okRabbit) {
			x = (byte) (MiscTools.random(BackgroundManager.MAP_WIDTH - 2) + 1);
			y = (byte) (MiscTools.random(BackgroundManager.MAP_HEIGHT - 2) + 1);
			
			c = map[x + y * BackgroundManager.MAP_WIDTH];
			okRabbit = ((c == 0) || (c == 21) || (c == (21 + 22))); 
		}
		rabbitX[i] = x;
		rabbitY[i] = y;
		restoreBuffer[i] = map[x + y * BackgroundManager.MAP_WIDTH];
		rabbitType[i] = 0;
		rabbitStatus[i] = 0;
		rabbitFrame[i] = 0;
		rabbitDelay[i] = 0;
	}
	
	public static void killRabbit(int x, int y) {
		int nr = 0;
		boolean found = false;
		
		for(int i = 0; i < maxRabbits && !found; i++) {
			if(rabbitX[i] == x && rabbitY[i] == y) {
				found = true;
				nr = i;
			}
		}
		
		if(!found) {
			return;
		}
		BackgroundManager.map[rabbitX[nr] + rabbitY[nr] * BackgroundManager.MAP_WIDTH] = restoreBuffer[nr];
		rbToSpawn[rbtsc++] = (byte) nr;
	}
	
	public static void drawRabbits() { //int minx, int miny, int maxx, int maxy) {
		byte[] map = BackgroundManager.map;
		
		for(int i = 0; i < rabbitX.length; i++) {
			int offs = rabbitX[i] + rabbitY[i] * BackgroundManager.MAP_WIDTH;
			
			if(rabbitFrame[i] > 0) {
				map[offs] = (byte) (rabbitFrame[i] + 1 + 22);
			}
		}
	}
	
	public static void drawRabbitsEars(GameCanvas gc) {
		int x,y;
		int snakeY = SnakeManager.snakeY * 8;
		int idAft[] = new int[maxRabbits];
		int ia = 0;
		
		for(int i = 0; i < rabbitX.length; i++) {
			if(rabbitFrame[i] > 0) {
				x = rabbitX[i] * 8 - Scroll.instance.ScrollX;
				y = (rabbitY[i] - 1) * 8 - Scroll.instance.ScrollY;
				//do not draw those rabbits out of the screen!
				if(x > -8 && x < gc.canvasWidth && y > -8 && y < gc.canvasHeight) {
					if(((rabbitY[i] - 1) * 8) < snakeY) {
						gc.showImage(gc.scr, rabbits, (rabbitFrame[i] + 1) * 8, 0, 8, 8, x, y);
					} else {
						idAft[ia++] = i;
					}
				}
			}
		}
		
		SnakeManager.drawSnakeHead(gc);
		
		for(int i = 0; i < ia; i++) {
			int k = idAft[i];
			
			x = rabbitX[k] * 8 - Scroll.instance.ScrollX;
			y = (rabbitY[k] - 1) * 8 - Scroll.instance.ScrollY;
			//do not draw those rabbits out of the screen!
			if(x > -8 && x < gc.canvasWidth && y > -8 && y < gc.canvasHeight) {
				gc.showImage(gc.scr, rabbits, (rabbitFrame[k] + 1) * 8, 0, 8, 8, x, y);
			}
		}
	}
	
	public static void clearMemory() {
		rabbitX = null;
		rabbitY = null;
		System.gc();
	}
	
	public static void clearRabbits() {
		byte[] map = BackgroundManager.map;
		
		for(int i = 0; i < rabbitX.length; i++) {
			map[rabbitX[i] + rabbitY[i] * BackgroundManager.MAP_WIDTH] = restoreBuffer[i];
		}
	}
	
	public static void logic() {
		//System.out.println(rabbitType[0] + "|" + rabbitStatus[0] + "|" + rabbitFrame[0] + "|" + rabbitDelay[0]);
		byte[] map = BackgroundManager.map;
		for(int i = 0; i < maxRabbits; i++) {
			switch(rabbitType[i]) {
				case 0:
					switch(rabbitStatus[i]) {
						case 0:
							rabbitFrame[i]++;
							if(rabbitFrame[i] == 4) {
								rabbitFrame[i] = rbAnim[0]; // no se si cal  :U_
								rabbitStatus[i] = 1;
								rbCnt[i] = 0;
								rabbitDelay[i] = 400 + MiscTools.random(500);
							}
							break;
						case 1:
							rabbitDelay[i]--;
							rabbitFrame[i] = rbAnim[rbCnt[i] >> 2];
							rbCnt[i]++;
							if(rbCnt[i] > (rbAnim.length - 1) * 4) rbCnt[i] = 0;
							
							if(rabbitDelay[i] == 0) {
								rabbitStatus[i] = 2;
								rabbitFrame[i] = 4;
							}
							
							break;
						case 2:
							rabbitFrame[i]--;
							if(rabbitFrame[i] == 0) {
								rabbitType[i] = 4;
								rabbitFrame[i] = -1;
								map[rabbitX[i] + rabbitY[i] * BackgroundManager.MAP_WIDTH] = 0;
								rabbitDelay[i] = 100 + MiscTools.random(200);
							}
							break;
					}
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					rabbitDelay[i]--;
					if(rabbitDelay[i] == 0) {
						rbToSpawn[rbtsc++] = (byte) i;
					}
					break;
			}
		}
	}
	
	public static void release() {
		rabbitX = null;
		rabbitY = null;
		rabbitFrame = null;
		rabbitDelay = null;
		rabbitType = null;
		rabbitStatus = null;
		rbCnt = null;
		rbToSpawn = null;
		System.gc();
	}
	
	public static void spawnRabbits() {
		byte map[] = BackgroundManager.map;
		
		for(int i = 0; i < rbtsc; i++) {
			spawnRabbit(rbToSpawn[i], map);
		}
		rbtsc = 0;
	}
}
