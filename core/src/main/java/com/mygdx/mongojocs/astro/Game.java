package com.mygdx.mongojocs.astro;//
//
// Astro 3003 Nokia 7650 v1.1 22/01/2003
// v1.5 try catch fixed
//	sound.init fixed
	// Indicamos que ESTE fichero.java hay que incluirlo en el Archivo 'base.JAR'


import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.MIDlet;

public class Game extends MIDlet
{

Display display;

GamePlay pantgame;


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void startApp ()
{
	
	pantgame = new GamePlay(this);		// Creamos Objeto pantalla


	System.gc();	// Liberamos memoria 'sucia' (gc = GarbageClear)


	display = Display.getDisplay(this);
	display.setCurrent(pantgame);  	// hacemos display de pantgame.
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Ejecutamos este metodo para hacer QUIT del MIDlet ('boolean = false' para QUIT)
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

protected void destroyApp (boolean b)
{
	pantgame.GameExit=1;
	pantgame.GameEND();
	display.setCurrent(null);
	this.notifyDestroyed();       // notify KVM
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<




// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Se ejecuta cuando se pausa el MIDlet (igual que los applets)
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

protected void pauseApp() {}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



} // Final Clase MIDlet
