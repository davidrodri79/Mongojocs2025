package com.mygdx.mongojocs.astro;


// *******************
// -------------------
// Texto - Engine
// ===================
// *******************

public class Texto
{

static String Skip=		"Saltar";
static String Select=	"Seleccionar";

static String Loading=	"Cargando";
static String Level=	"Nivel";
static String Complet=	"Completado";
static String GameOver=	"FIN DEL JUEGO";

static String Start=	"Jugar";
static String PlayLev=	"Jugar Desde...";
static String Sound[]=	new String[] {"Sonido en OFF","Sonido en ON"};
static String Vibrate[]=new String[] {"Vibra en OFF","Vibra en ON"};
static String Controls=	"Controles";
static String Credits=	"Créditos";
static String ExitGame=	"Salir";

static String Continue=	"Continuar";
static String TryAgain=	"Desistir";
static String Restart=	"Terminar";

// -------------------
// Texto MenuInfo
// ===================

static public void MenuInfo(Marco marco)
{
	marco.MarcoINI();
	marco.MarcoADD(0x011,"Controles Menú");
	marco.MarcoADD(0x000,"* = Menú");
	marco.MarcoADD(0x000,"2 = arriba opción");
	marco.MarcoADD(0x000,"5 = seleccionar");
	marco.MarcoADD(0x000,"8 = abajo opción");
	marco.MarcoSET_Texto();
}

// -------------------
// Texto Controls
// ===================

static public void Controls(Marco marco)
{
	marco.MarcoINI();
	marco.MarcoADD(0x011,"Controles");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Tecla 2:");
	marco.MarcoADD(0x000," - Saltar");
	marco.MarcoADD(0x000," - Subir");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Tecla 8:");
	marco.MarcoADD(0x000," - Bajar");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Tecla 4:");
	marco.MarcoADD(0x000," - Izquierda");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Tecla 6:");
	marco.MarcoADD(0x000," - Derecha");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x010,"Tecla 5:");
	marco.MarcoADD(0x000," - Usar Objeto");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x011,"Instrucciones");
	marco.MarcoADD(0x000," ");
	marco.MarcoFIX(0x000," Tu misión es coger todos los minerales.");
	marco.MarcoADD(0x000," ");
	marco.MarcoFIX(0x000,"Para eso, utiliza todos los objetos en orden correcto para abrir muros, para volar, para matar, etc..");
	marco.MarcoADD(0x000," ");
	marco.MarcoFIX(0x001,"Usa DESISITIR si te encuentras atrapado en el nivel.");
	marco.MarcoSET_Scroll(0);
}

// -------------------
// Texto Credits
// ===================

static public void Credits(Marco marco)
{
	marco.MarcoINI();
	marco.MarcoADD(0x111,"ASTRO 3003");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x011,"Créditos");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Gráficos:");
	marco.MarcoADD(0x002,"Jordi Palomé");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x000,"Programado:");
	marco.MarcoADD(0x002,"Juan Ant. Gómez");
	marco.MarcoADD(0x000," ");
	marco.MarcoADD(0x011,"Desarrollado por:");
	marco.MarcoADD(0x001,"Microjocs Mobile");
	marco.MarcoADD(0x001,"2003");
	marco.MarcoSET_Scroll(0);
}

// <=- <=- <=- <=- <=-

}
