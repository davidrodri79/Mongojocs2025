package com.mygdx.mongojocs.sanfermines2006;

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.11 (11.7.2005)
// ===============================================

//#ifdef DOJA
//#define HIDE_CONNECTIVITY
//#endif


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.sanfermines2006.Game
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.MIDlet;
//import com.mygdx.mongojocs.midletemu.Runnable;
//import com.mygdx.mongojocs.midletemu.Thread;



//#ifdef J2ME
public class Game extends MIDlet implements Runnable
//#elifdef DOJA
//#endif
{
GameCanvas gc;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()                                                               // Clase Bios integrada en la com.mygdx.mongojocs.sanfermines2006.Game
{
//#if DEBUG && DEBUG_SHOW_INFO
	Debug.printFreeMem("GameConst");
//#endif


	System.gc();
	gc = new GameCanvas(this);

//#if DEBUG && DEBUG_SHOW_INFO
	Debug.printFreeMem("CanvasConst");
//#endif
	
//#ifdef J2ME
	Display.getDisplay(this).setCurrent(gc);
	new Thread(gc).start();
//#endif
}

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// startApp - Al inicio y cada vez que se hizo pauseApp()
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

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

//#elifdef DOJA
//#endif

// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// destroyApp - Para DESTRUIR el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public void destroyApp (boolean b)
{
	gc.savePrefs();
	gc.gameExit = true;

//#ifdef J2ME
	notifyDestroyed();
//#elifdef DOJA
//#endif
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<






// *******************
// -------------------
// connect - Engine
// ===================
// *******************


	public void run()
	{
    //#ifndef HIDE_CONNECTIVITY
		gc.connectRun();
	//#endif
	}

	public void runInit()
	{
		gc.connectRun();
	}

	public void runTick()
	{

	}

	public void runEnd()
	{

	}


// <=- <=- <=- <=- <=-








// ------------------------------------------------------------------------------------------------------------------------
// **************************************************************************//
// Final Clase com.mygdx.mongojocs.sanfermines2006.Game
// **************************************************************************//
};