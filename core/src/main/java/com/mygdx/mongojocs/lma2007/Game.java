package com.mygdx.mongojocs.lma2007;

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.11 (11.7.2005)
// ===============================================



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.lma2007.Game
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.MIDlet;


public class Game extends MIDlet                        // Clase Bios integrada en la com.mygdx.mongojocs.lma2007.Game
{
//#ifdef BUILD_ONE_CLASS
//#else
	GameLaunch gc;
//#endif

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()                                                               // Clase Bios integrada en la com.mygdx.mongojocs.lma2007.Game
{
	System.gc();
//#ifdef BUILD_ONE_CLASS
//#else
	gc = new GameLaunch(this);
//#endif
	
	Display.getDisplay(this).setCurrent(gc);
	new Thread(gc).start();
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// startApp - Al inicio y cada vez que se hizo pauseApp()
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void startApp()
{
//#ifdef DEBUG
	Debug.println (" -+- startApp()");
//#endif

	Display.getDisplay(this).setCurrent(gc);
	gc.gamePaused=false;
}

public void pauseApp()
{
//#ifdef DEBUG
	Debug.println (" -+- pauseApp()");
//#endif

	gc.gamePaused=true;
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// destroyApp - Para DESTRUIR el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	gc.savePrefs();
	gc.gameExit = true;

	notifyDestroyed();
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<







// <=- <=- <=- <=- <=-




// ------------------------------------------------------------------------------------------------------------------------
// **************************************************************************//
// Final Clase com.mygdx.mongojocs.lma2007.Game
// **************************************************************************//
};