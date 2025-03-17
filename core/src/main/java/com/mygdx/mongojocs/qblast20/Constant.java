package com.mygdx.mongojocs.qblast20;

public interface Constant
{
	
	// Menu colors
		
	//#ifndef MONOCHROME	
	public static final int RGB_BKG = 0x2a354b;
	public static final int RGB_GOLD = 0xddb641;
	public static final int RGB_SILVER = 0xbcbcbc;
	public static final int RGB_BRONZE = 0xb78b88;
	public static final int RGB_BOX = 0xacb2b2;
	public static final int RGB_LIGHT = 0xffffff;
	public static final int RGB_SHADE = 0x747a7a;
	public static final int RGB_DARK = 0x4d5151;
	public static final int RGB_RED = 0xff0000;
	public static final int RGB_NOTAVAILABLE = 0x768094;
	//#else
	//#endif


	// Text engine

	static final int TEXT_PLAIN =	0x000;
	static final int TEXT_BOLD =	0x010;
	
	static final int TEXT_SMALL =	0x000;
	static final int TEXT_MEDIUM =	0x100;
	static final int TEXT_LARGER =	0x200;
	
	static final int TEXT_LEFT =	0x000;
	static final int TEXT_RIGHT =	0x001;
	static final int TEXT_HCENTER =	0x002;
	static final int TEXT_TOP =		0x000;
	static final int TEXT_BOTTON =	0x004;
	static final int TEXT_VCENTER =	0x008;
	
	static final int TEXT_OUTLINE =	0x1000;
	static final int TEXT_BOXED =	0x10000;

	// Menu engine
	
	static final int BIOS_GAME = 0;
	static final int BIOS_LOGOS = 11;
	static final int BIOS_MENU = 22;
	
	static final int ML_TEXT = 1;
	static final int ML_SCROLL = 2;
	static final int ML_OPTION = 3;
	static final int ML_SCREEN = 4;
	
	
	// Menu hierarchy ids
	
	static final int MENU_MAIN = 0;
	static final int MENU_SECOND = 1;
	static final int MENU_SCROLL_HELP = 2;
	static final int MENU_SCROLL_ABOUT = 3;
	static final int MENU_SHOW_TIP = 4;
	static final int MENU_RETRY_ASK = 5;
	static final int MENU_NEXT_ASK = 6;
	static final int MENU_CHEATS = 7;
	static final int MENU_QUIT_ASK = 8;
	static final int MENU_RESTART_ASK = 9;
	
	//#ifndef NONETWORK
	static final int MENU_NETWORK_ERROR = 10;
	static final int MENU_NETWORK_RECORDS = 11;
	static final int MENU_PACK_DOWNLOAD_SELECT = 12;
	static final int MENU_PACK_DOWNLOAD_INSTRUCTIONS = 13;
	//#endif
	
	static final int MENU_ACTION_PLAY = 0;
	static final int MENU_ACTION_CONTINUE = 1;
	static final int MENU_ACTION_SHOW_HELP = 2;
	static final int MENU_ACTION_SHOW_ABOUT = 3;
	static final int MENU_ACTION_EXIT_GAME = 4;
	static final int MENU_ACTION_RESTART = 5;
	static final int MENU_ACTION_SOUND_CHG = 6;
	static final int MENU_ACTION_VIBRA_CHG = 7;
	static final int MENU_ACTION_PLAY_LOCAL 	=  8;
	static final int MENU_ACTION_PLAY_REMOTE 	=  9;
	static final int MENU_ACTION_PLAY_REMOTE_DEBUG  = 10;
	static final int MENU_ACTION_NEXT_LEVEL  = 11;
	static final int MENU_ACTION_RETRY_CONFIRM  = 12;
	static final int MENU_ACTION_PACK_RESULTS  = 13;
	static final int MENU_ACTION_CHEAT_MENU  = 14;
	static final int MENU_ACTION_BACK_TO_LEVEL_SELECT  = 23;
	static final int MENU_ACTION_QUIT_CONFIRM  = 24;
	static final int MENU_ACTION_RESTART_CONFIRM  = 25;
	
	//#ifndef NONETWORK
	static final int MENU_ACTION_RETRIEVE_NETWORK_RECORDS  = 26;
	static final int MENU_ACTION_PACK_DOWNLOAD_INSTRUCTIONS  = 27;
	static final int MENU_ACTION_DOWNLOAD_ENTER_CODE  = 28;
	static final int MENU_ACTION_FORCE_PACK_REDOWNLOAD = 29;
	//#endif


	// Map tile IDs
	
	public static final byte 
		EMPTY = 0,
		TILE = 1,
		ICE = 2,
		BROKEN = 3,
		BOX = 4,
		CROSS = 5,
		RRAMPA = 6,
		RRAMPB = 7,
		RRAMPC = 8,
		RRAMPD = 9,
		RRAMPE = 10,		
		DRAMPA = 11,
		DRAMPB = 12,
		DRAMPC = 13,
		DRAMPD = 14,
		DRAMPE = 15,		
		LRAMPA = 16,
		LRAMPB = 17,
		LRAMPC = 18,
		LRAMPD = 19,
		LRAMPE = 20,		
		URAMPA = 21,
		URAMPB = 22,
		URAMPC = 23,
		URAMPD = 24,
		URAMPE = 25,
		RBELT = 26,
		DBELT = 27,
		LBELT = 28,
		UBELT = 29,		
		HOLE = 30,		
		SPRING = 31,
		RING = 32,
		
		POWER_IT = 33,
		BOMB_IT = 34,
		INVUL_IT = 35,
		TIME_IT = 36,
		TRACTION_IT = 37,
		GLASSES_IT = 38,
		JEWEL_IT = 39,
		HIDDEN = 40,
		GOAL = 41,
		REDUCER_MA = 42,
		
		LIFTPATH = 50,		
		REDUCER = 51,		
		STARTPOS = 53,
		LIFT_EN = 54,
		BLACK_EN = 55, 
		ELECTRIC_EN = 56,
		MAGNETIC_EN = 57, 
		RUBBER_EN = 58,
		MIRROR_EN = 59				
	;
	
	public static final byte	
		PLAYER = 0,
		BLOCK = 1,
		BOMB = 2,
		EXPLOSION = 3,
		ENEMY = 4,
		PLAYER_PIECE = 5,
		LIFT = 6,
		TILE_PIECE = 7		
	;
	
	
	// Network stuff
	
	//#ifndef NONETWORK
	static final int protocolVersion = 100;
	
	static final int MCSERVER_ERROR_GENERIC = 0xF001;
	static final int MCSERVER_ERROR_NO_SUBSCRIPTION = 0xF002;
	static final int MCSERVER_ERROR_OLD_VERSION = 0xF003;
	static final int MCSERVER_ERROR_INVALID_PACK = 0xF004;
	
	// juego: qblast2
	public static final int MCGAME_QBLAST2 = 0x20;
	
	// controlador: packs
	static final int MCCONTROLLER_PACKS = 0x1000;
	
	// metodos
	static final int MCCLIENT_GET_PACK_LIST = 0x1001;
	static final int MCSERVER_PACK_LIST = 0x1002;		// reply
	static final int MCCLIENT_GET_PACK = 0x1003;
	static final int MCSERVER_PACK = 0x1004;		// reply
	static final int MCCLIENT_GET_PACK_ALREADY_PAID = 0x1005;		
	
	// controlador: scores
	static final int MCCONTROLLER_SCORES = 0x2000;
	
	// metodos
	static final int MCCLIENT_GET_SCORES = 0x2001;
	static final int MCSERVER_SCORES = 0x2002;		// reply
	static final int MCCLIENT_REPORT_SCORE = 0x2003;
	static final int MCSERVER_SCORE_OK = 0x2004;		// reply
	
	static final long NETWORK_TIMEOUT = 60*1000;
	//#endif
	


	
	
};