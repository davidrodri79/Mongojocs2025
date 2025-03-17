

// ----------
// Aminoid X
// ==========
// Textos - Nokia 7650 - Espa�ol - Rev.4 (5.6.2003)
// ----------

package com.mygdx.mongojocs.aminoid;


// *******************
// -------------------
// Texto - Engine
// ===================
// *******************

public class Texto
{

// ----------------------
// Caracteres permitidos:
// ----------------------
//	-=> 0 a 9 <=-
//	-=> A a Z <=-
//	-=> ?  <=-
//	-=> :  <=-
//	-=> ( <=- se escribe con -=> < <=-
//	-=> ) <=- se escribe con -=> > <=-
//	-=> � <=- se escribe con -=> @ <=-
//	-=> , <=- se escribe con -=> \\ <=-
//	-=> . <=- se escribe con -=> [ <=-
//	-=> ! <=- se escribe con -=> ; <=-
// ===================



// ---------------------------------------------
// Intro
// ---------------------------------------------
static String PressFire="PULSA 5 PARA JUGAR";
static String Firma=	"2003 MICROJOCS MOBILE";
// =============================================



// ---------------------------------------------
// Textos del panel de OPCIONES
// ---------------------------------------------

static String Sound[]=	new String[] {"SONIDO OFF","SONIDO ON"};
static String Inmune[]=	new String[] {"INMUNE OFF","INMUNE ON"};

// ---------------------------------------------

static String MenuOptTxt0[] = new String[] {	// Menu Principal (Nada mas arrancar el juego)
"JUGAR",
" ",	// SOUND
"CONTROLES",
"CREDITOS",
"SALIR",
};

// ---------------------------------------------

static String MenuOptTxt2[] = new String[] {	// Menu Continuar Partida (SI hay partida guardada)
"CONTINUAR",
"JUEGO NUEVO",
"VOLVER MENU",
};

// ---------------------------------------------

static String MenuOptTxt1[] = new String[] {	// Menu Secunadario (mientras juegas y detines el juego)
"CONTINUAR",
" ",	// SOUND
"CONTROLES",
"SALIR",
"GUARDAR",
};

// ---------------------------------------------

static String MenuOptTxt3[] = new String[] {	// Menu Secunadario (mientras juegas y detines el juego) PARA CHEATS.
"CONTINUAR",
" ",	// SOUND
"CONTROLES",
"SALIR",
"GUARDAR",
" ",	// INMUNE
"FULL ARMY",
"NEXT LEVEL",
};

// ---------------------------------------------

static String MenuTxtOver[] = new String[] {	// Texto cuando te matan y haces continue...
"INFORME",
" ",
"bAMINOID X",
"VEHICULO HX300",
"DESTRUIDO[[[",
" ",
"CREDITOS: 9",			//  <==---  *** NO CAMBIAR EL espacio y el 9 del final ***
" ",					//  <==---  *** NO quitar/agregar entre estas TRES ultimas lineas ***
"bCONTINUAR?  "			//  <==---  *** el CONTINUAR DEBE ser la ultima linea de todas y con "?" y 2 espacios finales ***
};

// ---------------------------------------------

static String MenuTxtOve2[] = new String[] {	// Texto cuando te matan y haces continue...

"INFORME",
" ",
"bAMINOID X",
" ",
"LA MISION HA FALLADO\\",
"EL VEHICULO HX300",
"HA SIDO DESTRUIDO",
"Y LOS PRISIONEROS",
"HAN SIDO TRANSPORTADOS",
"A ALGUN SITIO",
"DESCONOCIDO[",
" ",
"EL FALLO DE ESTA",
"MISION HA SIDO MUY",
"PERJUDICIAL PARA",
"LA FEDERACION[",
" ",
" ",
"b ",
"bFIN DE JUEGO[ ",
"b ",
"b ",
};

// ---------------------------------------------

static String MenuOptOver[] = new String[] {	// Menu del Game-Over / Continue
"SI",
"NO",
};

// ---------------------------------------------

static String MenuOptFases[] = new String[] {	// Menu para la seleccion de las fases en orden MODO CHEAT
"MISION 1",
"MISION 2[1",
"MISION 2[2",
"MISION 2[3",
"MISION 3[1",
"MISION 3[2",
"MISION 3[3",
"MISION 4",
"MISION 5",
};

// ---------------------------------------------

static String MenuOptMision[] = new String[] {	// Menu para la seleccion de las fases en orden
"MISION 001",
"MISION 002",
"MISION 003",
"MISION 004",
"MISION 005",
};

// ---------------------------------------------

static String MenuPowerOff[] = new String[] {	// Testo para cuando QUITAMOS el midlet
"POWER OFF    "
};

// =============================================


static String Cheat[] = new String[] {	// 
"bCHEAT MODE:",
"ENABLED[ ",
};


// ---------------------------------------------
// Creditos
// ---------------------------------------------
static String Creditos[] = new String[] {
"bCREDITOS",
" ",
" ",
"bMUSICAS",
"ESTEBAN MORENO[",
" ",
" ",
"bGRAFICOS",
"JORDI PALOME[",
" ",
" ",
"bPROGRAMACION",
"JUAN ANTONIO GOMEZ[",
" ",
" ",
"bDESARROLLADO",
"2003 MICROJOCS MOBILE",
};
// =============================================

// ---------------------------------------------
// Controles / instrucciones
// ---------------------------------------------

static String Controles[] = new String[] {
"bCONTROLES",
"b ",
"bTECLAS 1\\2\\3:",
"SALTAR[",
"b ",
"bTECLAS 4 Y 5:",
"MOVER IZQUIERDA",
"Y DERECHA[",
"b ",
"bTECLAS 7\\8\\9:",
"CAER DEL TECHO[",
"LIBERAR REHEN[",
"b ",
"bTECLA 5:",
"DISPARAR[",
"b ",
"bTECLA 0:",
"CAMBIAR DE ARMA[",
"b ",
"bTECLA MENU IZQUIERDA:",
"IR A MENU[",
"b ",
"bTECLA MENU DERECHA:",
"SELECCIONAR OPCION[",
"CAMBIAR DE ARMA[",
};

// =============================================




// ---------------------------------------------
// Introduccion del juego
// ---------------------------------------------

static String Intro[] = new String[] {		// Texto de Introduccion al juego
"FEDERACION",
" ",
"bAMINOID X",
"REPORTE DE MISION",
" ",
"A@O 3902",
" ",
"PLANETA AMINOID X",
" ",
"DURANTE LA BATALLA DE",
"GALGA2\\ UN GRUPO DE",
"HUMANOS FUERON",
"ENVIADOS AL PLANETA",
"AMINOID X PARA",
"DESACTIVAR LOS",
"GENERADORES DE",
"POTENCIA PARA LOS",
"ESCUDOS DEL ENEMIGO,",
"PERO HUBO ALGUN ESCAPE,",
"Y EL ENEMIGO TENDIO UNA",
"TRAMPA A NUESTRO EQUIPO[",
" ",
"SABEMOS QUE TODAVIA LOS",
"TIENEN PRISIONEROS EN",
"DICHO PLANETA\\ LA NAVE",
"ULTRAARCON DE LA",
"FEDERACION HA",
"CONSEGUIDO LLEGAR",
"HASTA EL PLANETA",
"AMINOIDX\\ Y HA LANZADO",
"UN VEHICULO HX300 HASTA",
"LA SUPERFICIE[[[",
" ",
" ",
"TU MISION SERA CONDUCIR",
"EL VEHICULO HASTA LOS",
"REHENES PARA PODER",
"TELETRANSPORTARLOS A",
"LA NAVE NODRIZA[",
" ",
"DEBERAS DESTRUIR",
"CUALQUIER FUERZA",
"ENEMIGA QUE OPONGA",
"RESISTENCIA A LA",
"MISION[",
" ",
"ES POSIBLE QUE EL ENEMIGO",
"SEPA QUE LA FEDERACION",
"INTENTARA RESCATAR A",
"LOS HUMANOS\\ DE MODO",
"QUE ESTARAN",
"ESPERANDOTE[",
" ",
" ",
"FIN DE ARCHIVO[[[",
};

// =============================================



static String Fase[][] = {

// --------------------------------------------------------------------
// Fase 1:	( Bajar por los asteroides )
// --------------------------------------------------------------------
{
"INFORME",
" ",
"bAMINOID X",
" ",
"VEHICULO HX300\\",
"DIRIGETE A LA",
"SUPERFICIE DEL",
"PLANETA\\ ESPERA",
"NUEVAS ORDENES ALLI["
},
// ====================================================================

// --------------------------------------------------------------------
// Fase 2:	(Superficie del Planeta hasta llegar a entrada subterranea)
// --------------------------------------------------------------------
{
"INFORME",
" ",
"bAMINOID X",
" ",
"HAS LLEGADO",
"A LA SUPERFICIE\\",
"AHORA ENCUENTRA UNA",
"ENTRADA SUBTERRANEA[",
" ",
"RECOGE TODOS LOS",
"ITEMS PARA MEJORAR",
"TU ARMAMENTO[",
},
// ====================================================================

// --------------------------------------------------------------------
// Fase 3:	( Fases donde hay que rescatar Humanos )
// --------------------------------------------------------------------
{
"INFORME",
" ",
"bAMINOID X",
" ",
"HAS ENTRADO EN LA",
"BASE ENEMIGA",
"SUBTERRANEA[",
" ",
"BUSCA A LOS REHENES",
"Y TELETRANSPORTALOS",
"A LA NAVE NODRIZA[",
" ",
"PARA PASAR AL",
"SIGUIENTE SECTOR",
"UTILIZA LOS",
"TELETRANSPORTADORES[",
},
// ====================================================================

// --------------------------------------------------------------------
// Fase 4:	( Hay que subir hasta la superficie del Planeta )
// --------------------------------------------------------------------
{
"INFORME",
" ",
"bAMINOID X",
" ",
"ESTAS EN EL ULTIMO",
"SECTOR[",
" ",
"LIBERA LOS REHENES",
"Y DIRIGETE A LA",
"SALIDA SUPERIOR[",
},
// ====================================================================

// -------------------------------------------------------------------------
// Fase 5:	( Superficie del Planeta donde destruir enemigo FINAL (Nostromo)
// -------------------------------------------------------------------------
{
"INFORME",
" ",
"bAMINOID X",
" ",
"ESTAS FUERA\\",
"HEMOS CONSEGUIDO",
"TELETRANSPORTAR A",
"TODOS LOS REHENES\\",
"Y AHORA YA[[[[",
" ",
"ALERTA[[[[",
"ALERTA[[[[",
"ALERTA[[[[",
" ",
"HEMOS DETECTADO UNA",
"NAVE NODRIZA ENEMIGA",
"EN LA SUPERFICIE[",
" ",
"DESTRUYELA ANTES DE",
"QUE TE DESTRUYA A TI",
"O ESCAPE[[[",
}
// ====================================================================

};



// -------------------------------------------------------------------------
// Comentarios:	( Textos que aparecen DURANTE el juego)
// -------------------------------------------------------------------------

static String Info[][] = {

// Info 00
{
"DEBES LLEGAR A LA",
"SUPERCIFIE DEL PLANETA",
},

// --------------------------------------

// Info 01
{
"CONSIGUE LA BOLA PARA",
"MEJORAR TU ARMAMENTO[",
},

// --------------------------------------

// Info 02
{
"bHX300 DESTRUIDO[",
},

// --------------------------------------

// Info 03
{
"PULSA ABAJO PARA",
"LIBERAR AL REHEN[",
},

// --------------------------------------

// Info 04
{
"REHENES LIBERADOS[ VE",
"AL TELETRANSPORTADOR",
},

// --------------------------------------

// Info 05
{
"REHENES LIBERADOS[",
"DIRIGETE A LA SUPERFICIE",
},

// --------------------------------------

// Info 06
{
"DEBES DESTRUIR A",
"TODOS LOS GUSANOS",
},

// --------------------------------------

// Info 07
{
"bSALTA EN LA ENTRADA",
},

// --------------------------------------

// Info 08
{
"LA NAVE HUYE\\ DESTRUYELA",
"ANTES DE QUE SE ESCAPE[[[",
},

// --------------------------------------

// Info 09
{
"bNAVE DESTRUIDA!!!",
},

// --------------------------------------

// Info 10
{
"DEBES DIRIGIRTE",
"A LA SUPERFICIE[",
},

// --------------------------------------

// Info 11
{
"DESTRUYE AL GUARDIAN",
"DE LA ENTRADA[",
},

// --------------------------------------

// Info 12	- Canon Amarillo
{
"bMAGMA"
},

// --------------------------------------

// Info 13	- Canon Azul
{
"bPLASMA"
},

// --------------------------------------

// Info 14	- Bola
{
"bOPTION"
},

// --------------------------------------

// Info 15	- Bola Arma Verde
{
"bMULTIGUN"
},

// --------------------------------------

// Info 16	- Bola Arma Lila
{
"bSPHERE PURPURE"
},

// --------------------------------------

// Info 17	- Bola Arma Azul
{
"bBLUEMAGMA"
},

// --------------------------------------

// Info 18	- Bola Arma Misil
{
"bMISIL"
},

// --------------------------------------

// Info 19	- Vida Extra
{
"bVIDA EXTRA"
},

// --------------------------------------

// Info 20	- Energia
{
"bENERGIA"
},

// --------------------------------------

};

// ====================================================================



// -------------------------------------------------------------------------
// Final (Texto del final de juego)
// -------------------------------------------------------------------------
static String Final[] = new String[] {
"FEDERACION",
" ",
"INFORME",
"bAMINOIDX",
" ",
"MUY BIEN\\",
"MISION COMPLETADA[[[",
" ",
"HA COMPLETADO",
"LA MISION CON EXITO\\",
"ESTA MISION HA SIDO DE",
"VITAL IMPORTANCIA PARA",
"LA FEDERACION\\",
"Y POR ELLO",
"LA FEDERACION HA",
"CONSIDERADO ASCENDERLE",
"Y CONDECORARLE CON",
"LA MEDALLA AL HONOR[",
" ",
"SUERTE[",
" ",
"FIN INFORME[[[",
};
// ====================================================================


// <=- <=- <=- <=- <=-

}
