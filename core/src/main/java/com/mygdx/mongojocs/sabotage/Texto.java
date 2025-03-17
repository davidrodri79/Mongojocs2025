

// --------------
// Sabotage Color
// ==============
// Textos - Spanish - Rev.1 (29.9.2003)
// ----------

package com.mygdx.mongojocs.sabotage;


// *******************
// -------------------
// Texto - Engine
// ===================
// *******************

public class Texto
{

static String Level=	"Nivel";
static String Loading=	"Cargando";
static String GameOver=	"GAME OVER";

static String Start=	"Jugar";
static String Sound[]=	new String[] {"Sonido en OFF","Sonido en ON"};
static String Vibrate[]=new String[] {"Vibra en OFF","Vibra en ON"};
static String Controls=	"Controles";
static String Credits=	"Créditos";
static String ExitGame=	"Salir";

static String Continue=	"Continuar";
static String TryAgain=	"Empezar";
static String Restart=	"Terminar";

// -------------------
// Texto MenuInfo
// ===================

static public void MenuInfo(Game marco)
{
	marco.ParcoINI();
	marco.ParcoADD(0x011,"Menu Controls");
	marco.ParcoADD(0x000,"* = Menu");
	marco.ParcoADD(0x000,"2 = up pointer");
	marco.ParcoADD(0x000,"5 = select option");
	marco.ParcoADD(0x000,"8 = down pointer");
	marco.ParcoSET_Texto();
}

// -------------------
// Texto Controls
// ===================

static public void Controls(Game marco)
{
	marco.ParcoINI();
	marco.ParcoADD(0x010,"Tecla de");
	marco.ParcoADD(0x010,"Control Izquierdo:");
	marco.ParcoADD(0x000,"- Ir al Menu");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x010,"Teclas 2 y");
	marco.ParcoADD(0x010,"Desp. Arriba:");
	marco.ParcoADD(0x000,"- Saltar");
	marco.ParcoADD(0x000,"- Subir Escalera");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x010,"Teclas 8 y");
	marco.ParcoADD(0x010,"Desp. Abajo:");
	marco.ParcoADD(0x000,"- Agachar");
	marco.ParcoADD(0x000,"- Bajar Escalera");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x010,"Teclas 4 y");
	marco.ParcoADD(0x010,"Desp. Izquierda:");
	marco.ParcoADD(0x000,"- Caminar Izquierda");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x010,"Teclas 6 y");
	marco.ParcoADD(0x010,"Desp. Derecha:");
	marco.ParcoADD(0x000,"- Caminar Derecha");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x010,"Teclas 5 y");
	marco.ParcoADD(0x010,"Control Derecho:");
	marco.ParcoADD(0x000,"- Lanzar Shuriken");
	marco.ParcoADD(0x000,"- Usar Sable");
	marco.ParcoSET_Scroll(0);
}

// -------------------
// Texto Credits
// ===================

static public void Credits(Game marco)
{
	marco.ParcoINI();
	marco.ParcoADD(0x111,"SABOTAGE");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x011,"Créditos");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x000,"Programado:");
	marco.ParcoADD(0x002,"Juan Ant. Gómez");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x000,"Gráficos:");
	marco.ParcoADD(0x002,"Jordi Palomé");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x000,"Sonido:");
	marco.ParcoADD(0x002,"Esteban Moreno");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x000,"Remix:");
	marco.ParcoADD(0x002,"Jordi Gutierrez");
	marco.ParcoADD(0x000," ");
	marco.ParcoADD(0x011,"Desarrollado por:");
	marco.ParcoADD(0x001,"Microjocs Mobile");
	marco.ParcoADD(0x001,"2003");
	marco.ParcoSET_Scroll(0);
}

// <=- <=- <=- <=- <=-

}
