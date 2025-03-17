

// ----------------
// The Mute
// ================
// Textos - English - Rev.2 (6.6.2003)
// ----------------

package com.mygdx.mongojocs.themute;


// *******************
// -------------------
// Texto - Engine
// ===================
// *******************

public class Texto
{

static String Start=	"Start";
static String Sound[]=	new String[] {"Sound is OFF","Sound is ON"};
static String Vibrate[]=new String[] {"Vibrate is OFF","Vibrate is ON"};
static String Controls=	"Controls";
static String Credits=	"Credits";
static String ExitGame=	"Exit game";

static String Continue=	"Continue";
static String Restart=	"Restart";

static String Levels[] = new String[] {"Ducts","FLOOR 1","FLOOR 2","FLOOR 3","Ducts","Terrace"};

static String Lives=	"Credits: ";

static String Congra=	"Congratulations";

static String GameOver=	"GAME OVER";


// -------------------
// Cheats
// ===================

static String CheatNexLev=	"Next Level";

static String CheatInmune[]=new String[] {"Inmune is OFF","Inmune is ON"};


// -------------------
// Texto MenuInfo
// ===================

static public void MenuInfo(Marco marco)
{
	marco.MarcoINI();
	marco.MarcoADD(0x011,"Menu Controls");
	marco.MarcoADD(0x000,"* = Menu");
	marco.MarcoADD(0x000,"2 = up pointer");
	marco.MarcoADD(0x000,"5 = select option");
	marco.MarcoADD(0x000,"8 = down pointer");
	marco.MarcoSET_Texto();
}

// -------------------
// Texto Controls
// ===================

static public void Controls(Marco marco)
{
	marco.MarcoINI();
	marco.MarcoADD(0x011,"Controls");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Key 2:");
	marco.MarcoADD(0x000," - Move Up");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Key 8:");
	marco.MarcoADD(0x000," - Move Down");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Key 4:");
	marco.MarcoADD(0x000," - Move Left");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Key 6:");
	marco.MarcoADD(0x000," - Move Right");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Key 5:");
	marco.MarcoADD(0x000," - Punch");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Key 0:");
	marco.MarcoADD(0x000," - Crawl");

	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000," ");

	marco.MarcoADD(0x011,"Instructions");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000," Your mission is to");
	marco.MarcoADD(0x000,"steal the diamond.");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000," Find the magnetic");
	marco.MarcoADD(0x000,"keys to open main");
	marco.MarcoADD(0x000,"doors and use the");
	marco.MarcoADD(0x000,"lifts to go to the");
	marco.MarcoADD(0x000,"next floor.");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Press 5 to pull the");
	marco.MarcoADD(0x000,"hand-levers to");
	marco.MarcoADD(0x000,"switch off alarms,");
	marco.MarcoADD(0x000,"and to open doors.");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Steal the diamond!");
	marco.MarcoSET_Scroll(0);
}

// -------------------
// Texto Credits
// ===================

static public void Credits(Marco marco)
{
	marco.MarcoINI();
	marco.MarcoADD(0x111,"The Mute");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x011,"Credits");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Programmed:");
	marco.MarcoADD(0x002,"Juan Ant. Gomez");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Graphics:");
	marco.MarcoADD(0x002,"Jordi Palome");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Music:");
	marco.MarcoADD(0x002,"Esteban Moreno");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Remix:");
	marco.MarcoADD(0x002,"Jordi Gutierrez");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x011,"Developed by:");
	marco.MarcoADD(0x001,"Microjocs Mobile");
	marco.MarcoADD(0x001,"2003");
	marco.MarcoSET_Scroll(0);
}

// <=- <=- <=- <=- <=-

}
