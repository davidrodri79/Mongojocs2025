

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.6 (25.2.2005)
// ===============================================
// Logica
// ------------------------------------


//#ifdef PACKAGE
package com.mygdx.mongojocs.mr2;
//#endif


//#ifdef J2ME


// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.io.IOException;

//#ifdef J2ME
public class Game extends MIDlet implements Runnable
//#elifdef DOJA
//#endif
{
GameCanvas gc;
public String userid = null;
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef com.mygdx.mongojocs.mr2.Debug
int debugMemIni, debugMenFin;
//#endif

public Game()                                                               // Clase Bios integrada en la Game
{
	System.gc();

//#ifdef com.mygdx.mongojocs.mr2.Debug
	debugMemIni = (int)Runtime.getRuntime().freeMemory();
//#endif
	try {
		//#ifdef J2ME
		userid = "1"; //MONGOFIX getAppProperty("HashId");
		//#else
		//#endif
	} catch(IllegalArgumentException e) {}
	
	//#ifdef com.mygdx.mongojocs.mr2.Debug
	DebugX.setCustom(userid);
	//#endif

	gc = new GameCanvas(this);
	System.gc();

//#ifdef com.mygdx.mongojocs.mr2.Debug
	debugMenFin = (int)Runtime.getRuntime().freeMemory();
//#endif
	
	//#ifdef com.mygdx.mongojocs.mr2.Debug
	DebugX.getInstance().initVersion();
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
//#ifdef DebugConsole
	Debug.println (" -+- startApp()");
//#endif

	Display.getDisplay(this).setCurrent(gc);
	gc.gamePaused=false;
}

public void pauseApp()
{
//#ifdef DebugConsole
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
	//gc.savePrefs();
	gc.gameExit = true;

//#ifdef J2ME
	notifyDestroyed();
//#elifdef DOJA
//#endif
}
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<









	static public byte[] result = null;

	static public boolean finished = false;
	static public boolean running = false;

	private String url;
	private byte[] data;
	private int netRequestCount = 0;

	/**
	 *  Constructor for the FatcatConnectionHTTP object
	 *
	 * @param  u  URL for the remote Fatcat server
	 */
	public void ConnectionHTTP(String u, byte[] in)
	{
		url = u;
		data = in;

		netRequestCount++;

		finished = false;
		running = true;
	}


	public void run()
	{
	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("Thread: HTTP_RUNNING");
	//#endif

		int currentCount = netRequestCount;

		try {
			byte[] temp = post(data);

		// Si este Thread SE cancel�, descartamos la respuesta, para evitar que se pise una conexion nueva contra una cancelada
			if (currentCount != netRequestCount) {return;}
			
			result = temp;

		} catch (Exception e)
		{
		//#ifdef com.mygdx.mongojocs.mr2.Debug
			Debug.println("Thread: HTTP_ERROR:");
			Debug.println(e.toString());
			e.printStackTrace();
		//#endif
			result = null;
		}

		finished = true;

	//#ifdef com.mygdx.mongojocs.mr2.Debug
		Debug.println("Thread: HTTP_FINISHED");
	//#endif
	}


	/**
	 *  Public method
	 *
	 * @exception  IOException  Description of the Exception
	 */
	public byte[] post(byte[] data) throws IOException {
		byte[] res = null;
		return res;
	}







// ------------------------------------------------------------------------------------------------------------------------
// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};