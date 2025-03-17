package com.mygdx.mongojocs.camaraoscura;

// --------------
// Camara Oscura
// ==============
// Textos - Spanish - Rev.0 (13.1.2004)
// -------------------


// *******************
// -------------------
// Texto - Engine
// ===================
// *******************

public class Texto
{

// -------------------------------------------

// Textos Menus

static String Start=	"Jugar";
static String Continue=	"Continuar";
static String Sound[]=	new String[] {"Sonido en OFF","Sonido en ON"};
static String Vibra[]=	new String[] {"Vibracion en OFF","Vibracion en ON"};
static String Controls=	"Instrucciones";
static String Restart=	"Terminar";
static String Credits=	"Acerca de...";
static String ExitGame=	"Salir";

static String GameOver=	"Fin del juego";

static String Loading=	"Cargando";

// -------------------------------------------

static public void sc_Controles(Marco ma)
{
	ma.MarcoADD(0x111,"Instrucciones");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Tecla de Control Izquierdo:");
	ma.MarcoFIX(0x000,"- Ir al Menu");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Tecla de Control Derecha:");
	ma.MarcoFIX(0x000,"- Seleccionar Opción");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Teclas 2 y Desp. Arriba:");
	ma.MarcoFIX(0x000,"- Saltar");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Teclas 8 y Desp. Abajo:");
	ma.MarcoFIX(0x000,"- Usar Puertas y Objetos");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Teclas 4 y Desp. Izquierda:");
	ma.MarcoFIX(0x000,"- Caminar Izquierda");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Teclas 6 y Desp. Derecha:");
	ma.MarcoFIX(0x000,"- Caminar Derecha");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Teclas 5 y Control Derecho:");
	ma.MarcoFIX(0x000,"- Usar Cuchillo");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x010,"Tecla 0:");
	ma.MarcoFIX(0x000,"- Correr");
}

// -------------------------------------------

static public void sc_Credits(Marco ma)
{
	ma.MarcoADD(0x111,"Cámara Oscura");
	ma.MarcoADD(0x000," ");
	ma.MarcoFIX(0x001,"Una producción de IRIS STAR, MANGA FILMS y ESTUDIOS PICASSO con la participación de Tele 5 y TVC.");
	ma.MarcoADD(0x000," ");
	ma.MarcoADD(0x011,"Créditos");
	ma.MarcoADD(0x000," ");
	ma.MarcoADD(0x000,"Productores:");
	ma.MarcoADD(0x002,"Pere Torrents");
	ma.MarcoADD(0x002,"Gerard Fernandez");
	ma.MarcoADD(0x000," ");
	ma.MarcoADD(0x000,"Gráficos:");
	ma.MarcoADD(0x002,"Raúl Durán");
	ma.MarcoADD(0x002,"Elías Lozano");
	ma.MarcoADD(0x000," ");
	ma.MarcoADD(0x000,"Programación:");
	ma.MarcoADD(0x002,"Juan Ant. Gómez");
	ma.MarcoADD(0x000," ");
	ma.MarcoADD(0x000,"Sonido:");
	ma.MarcoADD(0x002,"Jordi Gutierrez");
	ma.MarcoADD(0x000," ");
	ma.MarcoADD(0x011,"Desarrollo:");
	ma.MarcoADD(0x001,"Pebegat Six");
	ma.MarcoADD(0x001,"Microjocs Mobile");
	ma.MarcoADD(0x001,"2004");
}

// -------------------------------------------

// Textos inicio del Juego

static String[] Inicio1 = new String[] {
"No me fío, aquí suceden cosas extrañas, creo que han matado a aquel hombre!!",
"Lo mejor será permanecer aqu� ocultos, hasta llegar a tierra...",
};

static String[] Inicio2 = new String[] {
"AAHH !!! Me ha mordido una rata !!",
"Carajo !! Hay que desinfectar eso, o podrías coger el tifus!",
"Ya... pero cómo???",
"Tendré que salir a buscar un botiquín..",
"Pero...",
"Espérame aquí, yo subiré arriba a ver si encuentro algo.",
};

// -------------------------------------------

// Pillas el primer Botiquin

static String[] Botiquin = new String[] {
"Lo encontré!",
"Debo darme prisa o podria morir por la infección.",
};

// -------------------------------------------

static String[] Intro1 = new String[] {
"Tras vuestro naufragio, vuestra vida pende de un hilo.",
"Horas más tarde avistáis un barco en el horizonte...",
"Estamos salvados!!!",
"Los marineros tiran un cuerpo por la borda.",
"Un barco: la salvación o la muerte.",
"Elige jugador...",
};

// -------------------------------------------

// Hablas con tu Amigo:

// No has encontrado el Botiquin

static String[] Charla1 = new String[] {
"Lo has encontrado???",
"No, aún no.",
"No me queda mucho tiempo.",
};

// Has encontrado el Botiquin

static String[] Charla2 = new String[] {
"Ahora ya está desinfectado...",
"Me han visto.",
"Nos están buscando...",
"La herida no tiene buena pinta... tenemos que pedir ayuda, iré a buscar una radio.",
};

// No has Encontrado la Radio

static String[] Charla3 = new String[] {
"Has podido pedir ayuda???",
"No encuentro la radio.",
"Busca.. estará en el puente de mando.",
};

// No has encontrado el GPS

static String[] Charla4 = new String[] {
"Has podido pedir ayuda???",
"Necesito encontrar el GPS para saber la posición...",
"Quizás esté en el camarote del capitán...",
};

// Antes de hablar por Radio con el GPS

static String[] Charla5 = new String[] {
"Qué ha pasado?????",
"Ya tengo el GPS, voy a la radio a pedir ayuda.",
"De acuerdo, Ten cuidado.",
};

// Despues de hablar por Radio con el GPS

static String[] Charla6 = new String[] {
"Que ha pasado?????",
"Tenemos que salir de aqui... en cuanto encuentre la forma de soltar el bote te vengo a buscar.",
"De acuerdo, pero date prisa.",
};

// El Bote ya esta bajando al agua...

static String[] Charla7 = new String[] {
"�?? �A?",
"Una nota....",
"Tenemos a un rehen, entregate o le tiraremos por la borda",
"Jo***!!! Estaran en cubierta !!!",
};

// -------------------------------------------

// Hablamos por la radio

// SIN GPS

static String[] Radio = new String[] {
"Mayday, Mayday... Necesitamos ayuda... Hay alguien?",
"Le oigo, indiqueme su posicion por favor",
"Posicion???? Jo***... tengo que encontrar el GPS!!!",
};

// CON GPS

static String[] Radio2 = new String[] {
"Mayday, Mayday... Necesitamos ayuda... Hay alguien?",
"Le oigo, indiqueme su posicion por favor",
"Latitud: 20 grados 15�N, Longitud: 30 grados, 20' W",
"por favoor repi.... a... icio.. ..xrrr. .rr. xrx.",
"Me oye????",
"Han cortado la radio!!!",
"Tenemos que salir de aqui...",
"El bote esta arriba...",
};

// Radio Apagada

static String[] Radio3 = new String[] {
"Mayday, Mayday... Necesitamos ayuda... Hay alguien?",
"... ... ...",
"Han cortado la radio!!!",
"Tenemos que salir de aqui...",
"El bote esta arriba...",
};


// -------------------------------------------

// Encontramos GPS

static String[] GPS = new String[] {
"GPS. Posicion. Latitud: 20 grados 15�N, Longitud: 30 grados, 20' W",
};

// -------------------------------------------

// Mensajes para pulsar la tecla Abajo:

static String[] MsgPorta = new String[] {
"Pulsa Abajo para pasar por la puerta.",
};

static String[] MsgTelefono = new String[] {
"Pulsa Abajo para usar la radio.",
};

static String[] MsgGPS = new String[] {
"Pulsa Abajo para usar el GPS.",
};

static String[] MsgPalanca = new String[] {
"Pulsa Abajo para accionar las palancas.",
};

static String[] MsgCorrer = new String[] {
"Pulsa 0 para correr.",
};

static String[] MsgGrua = new String[] {
"Pulsa Abajo sobre las palancas para, subir, bajar y mover la jaula.",
};



static String[] JaulaAbierta = new String[] {
"Ya esta abierta.",
"Debemos escapar de aqui.",
"Vamos a por el bote salvavidas.",
};


static String[] BoteWait = new String[] {
"Pulsa Abajo para saltar al bote.",
};


// -------------------------------------------

static String[] Jaula1 = new String[] {
"Has cometido un error.",
};

static String[] Jaula2 = new String[] {
"Subete por la torre y para esto !!!",
"Usa las palancas!!!",
};

// -------------------------------------------


static String[] PortaTancada = new String[] {
"Esta cerrada con llave.",
};


static String[] PortaCabina = new String[] {
"Esta es la unica forma de acceder al bote... tiene que haber alguna otra entrada...",
};


static String[] AmigoMuere = new String[] {
"Llegue demasiado tarde.",
"Tengo que salir de aqui..."
};


static String[] JaulaCerrada = new String[] {
"¿Como te encuentras?",
"Bien. Date prisa, abre la cerradura con algun cuchillo."
};

// -------------------------------------------


static String[] JaulaOk = new String[] {
"La jaula ahora reposara sobre el barco.",
};

static String[] JaulaMal = new String[] {
"Tengo que subir la jaula para que repose sobre el barco.",
};

static String[] JaulaSube = new String[] {
"La jaula sube!",
};

static String[] JaulaBaja = new String[] {
"La jaula baja!",
};



}

// <=- <=- <=- <=- <=-

