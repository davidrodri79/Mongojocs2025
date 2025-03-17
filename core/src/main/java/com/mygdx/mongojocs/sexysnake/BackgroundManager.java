package com.mygdx.mongojocs.sexysnake;

import com.mygdx.mongojocs.midletemu.Image;

public class BackgroundManager {
	public static final int TILE_SIZE = 8;
	public static int MAP_WIDTH;
	public static int MAP_HEIGHT;
	public static byte[] map;
	private static int canvasW;
	private static int canvasH;
	private static int tilesW;
	private static int tilesH;
	private static GameCanvas gc;
	private static Image rabbits;
	
	//tile info:
	//0: free
	//1,2,3,4,5,6: edges of the map
	//7,8,9,10...: snake
	//20....: animation of the rabbit.
	private BackgroundManager() {}
	
	public static void start(GameCanvas gcv, int cw, int ch, int mx, int my, Image rb) {
		MAP_WIDTH = mx;
		MAP_HEIGHT = my;
		
		map = new byte[MAP_WIDTH * MAP_HEIGHT];
		
		for(int i = 0; i < MAP_WIDTH * MAP_HEIGHT; i++) {
			map[i] = 0;
		}
		
		for(int i = 0; i < (MAP_WIDTH * MAP_HEIGHT >> 1); i++) {
			int x = MiscTools.random(MAP_WIDTH);
			int y = MiscTools.random(MAP_WIDTH);
			int c = MiscTools.random(20);
			if(c > 10) c = 1;
			else c = 0;
			
			map[x + y * MAP_WIDTH] = (byte) (21 + c * 22);
		}
		
		for(int i = 0; i < MAP_WIDTH; i++) {
			map[i] = (byte) 18;
			map[i + (MAP_HEIGHT - 1) * MAP_WIDTH] = (byte) (18 + 22);
		}
		
		for(int i = 0; i < MAP_HEIGHT; i++) {
			map[i * MAP_WIDTH] = (byte) 20;
			map[i * MAP_WIDTH + (MAP_WIDTH - 1)] = (byte) (20 + 22);
		}
		
		map[0] = (byte) 17;
		map[MAP_WIDTH - 1] = (byte) 19;
		
		map[(MAP_HEIGHT - 1) * MAP_WIDTH] = (byte) (17 + 22);
		map[(MAP_HEIGHT - 1) * MAP_WIDTH + MAP_WIDTH - 1] = (byte) (19 + 22);
		
		canvasW = cw;
		canvasH = ch;
		tilesW = cw / TILE_SIZE;
		tilesH = ch / TILE_SIZE;
		gc = gcv;
		rabbits = rb;
		
		gc.sc.ScrollSET(map, MAP_WIDTH, MAP_HEIGHT, rabbits, 22);
	}
	
	public static void addStuff(int level) {
		int	mx = MAP_WIDTH >> 1;
		int	my = MAP_HEIGHT >> 1;
		int 	k;
		
		switch(level) {
			case 0:
				//nothing to do here!!
				break;
			case 1:
				k = (my - 2) * MAP_WIDTH + mx;
				map[-2 + k            ] = (byte) 17;
				map[-2 + k + 1        ] = (byte) 18;
				map[-2 + k + MAP_WIDTH] = (byte) 20;
				map[2 + k             ] = (byte) 19;
				map[2 + k - 1         ] = (byte) 18;
				map[2 + k + MAP_WIDTH ] = (byte) 20;
				
				k = (my + 2) * MAP_WIDTH + mx;
				map[-2 + k            ] = (byte) 39;
				map[-2 + k + 1        ] = (byte) 18;
				map[-2 + k - MAP_WIDTH] = (byte) 42;
				map[2 + k             ] = (byte) 41;
				map[2 + k - 1         ] = (byte) 18;
				map[2 + k - MAP_WIDTH ] = (byte) 42;
				break;
			case 2:
				k = (my - 6) * MAP_WIDTH + mx;
				map[-6 + k] = (byte) 17;
				map[-6 + k + 1            ] = (byte) 18;
				map[-6 + k + 2            ] = (byte) 18;
				map[-6 + k + 3            ] = (byte) 18;
				map[-6 + k + MAP_WIDTH    ] = (byte) 20;
				map[-6 + k + MAP_WIDTH * 2] = (byte) 20;
				map[-6 + k + MAP_WIDTH * 3] = (byte) 20;
				
				map[6 + k                ] = (byte) 19;
				map[6 + k - 1            ] = (byte) 18;
				map[6 + k - 2            ] = (byte) 18;
				map[6 + k - 3            ] = (byte) 18;
				map[6 + k + MAP_WIDTH    ] = (byte) 20;
				map[6 + k + MAP_WIDTH * 2] = (byte) 20;
				map[6 + k + MAP_WIDTH * 3] = (byte) 20;
				
				k = (my + 6) * MAP_WIDTH + mx;
				map[-6 + k                ] = (byte) 39;
				map[-6 + k + 1            ] = (byte) 18;
				map[-6 + k + 2            ] = (byte) 18;
				map[-6 + k + 3            ] = (byte) 18;
				map[-6 + k - MAP_WIDTH    ] = (byte) 42;
				map[-6 + k - MAP_WIDTH * 2] = (byte) 42;
				map[-6 + k - MAP_WIDTH * 3] = (byte) 42;
				
				map[6 + k                ] = (byte) 41;
				map[6 + k - 1            ] = (byte) 18;
				map[6 + k - 2            ] = (byte) 18;
				map[6 + k - 3            ] = (byte) 18;
				map[6 + k - MAP_WIDTH    ] = (byte) 42;
				map[6 + k - MAP_WIDTH * 2] = (byte) 42;
				map[6 + k - MAP_WIDTH * 3] = (byte) 42;
				
				//BackgroundManager.addStuff(1);
				break;
			case 3:
				int g_offs = (MAP_HEIGHT >> 1) * MAP_WIDTH;
				for(int i = 3; i < (MAP_WIDTH - 3); i++) {
					if(i != MAP_WIDTH >> 1) {
						map[i + g_offs] = (byte) 18;
					}
				}
				
				for(int i = 3; i < (MAP_HEIGHT >> 1) - 3; i++) {
					map[(MAP_WIDTH >> 1) - 1 + i * MAP_WIDTH] = (byte) 20;
					map[(MAP_WIDTH >> 1) + 1 + i * MAP_WIDTH] = (byte) 42;
				}
				
				for(int i = (MAP_HEIGHT >> 1) + 3; i < MAP_HEIGHT - 3; i++) {
					map[(MAP_WIDTH >> 1) - 1 + i * MAP_WIDTH] = (byte) 20;
					map[(MAP_WIDTH >> 1) + 1 + i * MAP_WIDTH] = (byte) 42;
				}
				break;
	//#ifndef NK-s40
			case 4:
				BackgroundManager.addStuff(1);
				BackgroundManager.addStuff(2);
	//#ifdef NK-s60
				k = (MAP_HEIGHT >> 3) * MAP_WIDTH;
				map[(MAP_WIDTH >> 1) - 1 + k] = (byte) 20;
				map[(MAP_WIDTH >> 1) + 1 + k] = (byte) 42;
				
				k = (MAP_HEIGHT - (MAP_HEIGHT >> 3)) * MAP_WIDTH;
				map[(MAP_WIDTH >> 1) - 1 + k] = (byte) 20;
				map[(MAP_WIDTH >> 1) + 1 + k] = (byte) 42;
				
				k = (MAP_HEIGHT >> 1) * MAP_WIDTH;
				map[(MAP_WIDTH >> 3) + k - MAP_WIDTH] = (byte) 20;
				map[(MAP_WIDTH >> 3) + k + MAP_WIDTH] = (byte) 42;
				
				map[(MAP_WIDTH - (MAP_WIDTH >> 3)) + k - MAP_WIDTH] = (byte) 20;
				map[(MAP_WIDTH - (MAP_WIDTH >> 3)) + k + MAP_WIDTH] = (byte) 42;
	//#endif
				break;
	//#endif
		}
	}
	
	public static void release() {
		map = null;
		map = null;
		gc = null;
		rabbits = null;
		System.gc();
	}
}
