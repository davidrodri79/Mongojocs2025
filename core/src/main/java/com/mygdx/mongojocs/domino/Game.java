

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.11 (11.7.2005)
// ===============================================


//#ifdef PACKAGE
package com.mygdx.mongojocs.domino;
//#endif


//#ifdef J2ME
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.TextField;
//#endif

import java.io.*;
import java.io.InputStream;

// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME
public class Game extends MIDlet implements CommandListener, Runnable
//#elifdef DOJA
//#endif
{
GameCanvas gc;

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

public Game()                                                               // Clase Bios integrada en la Game
{
	System.gc();
	gc = new GameCanvas(this);
	
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
// inputDialog - Engine
// ===================
// *******************

//#ifdef J2ME

public static Form inputForm;
public static TextField inputField;
public static String inputDialogLabel;

public void inputDialogCreate(String title, String subtitle, String lab, int numChars)
{

	inputDialogLabel = lab;
	
	Command doneCommand = new Command(lab, Command.OK, 1);
		
	inputForm = new Form(title);
	inputField = new TextField(subtitle, "",numChars,TextField.ANY);
	inputField.setMaxSize(numChars);
	inputForm.append(inputField);   
	inputForm.addCommand(doneCommand);
	inputForm.setCommandListener(this);
		
}

public void inputDialogInit()
{
	Display.getDisplay(this).setCurrent(inputForm);		
}

public void commandAction (Command c, Displayable d)
{
	if(c.getLabel()==inputDialogLabel)
	{								
		Display.getDisplay(this).setCurrent(gc);	
		inputDialogNotify(inputField.getString());
	}  	 	
}

public void inputDialogNotify(String s){}

//#elifdef DOJA
//#endif

// <=- <=- <=- <=- <=-





// *******************
// -------------------
// connect - Engine
// ===================
// *******************

static public byte[] connectResult = null;

static public boolean connectFinished = false;
static public boolean connectRunning = false;

private String connectUrl;
private byte[] connectData;
private int connectNetRequestCount = 0;

// -------------------
// connect Create
// ===================

public void connectCreate(String u, byte[] in)
{
	connectUrl = u;
	connectData = in;

	connectNetRequestCount++;

	connectFinished = false;
	connectRunning = true;
}

// -------------------
// connect run :: Creamos un nueva Thread para la conexion
// ===================

public void run()
{
//#ifdef DEBUG
	Debug.println("Thread: HTTP_RUNNING");
//#endif

	int currentCount = connectNetRequestCount;

	try {
		byte[] temp = connectPost(connectData);

	// Si este Thread SE cancela, descartamos la respuesta, para evitar que se pise una conexion nueva contra una cancelada
		if (currentCount != connectNetRequestCount) {return;}
		
		connectResult = temp;

	} catch (Exception e)
	{
	//#ifdef DEBUG
		Debug.println("Thread: HTTP_ERROR:");
		Debug.println(e.toString());
		e.printStackTrace();
	//#endif
		connectResult = null;
	}

	connectFinished = true;

//#ifdef DEBUG
	Debug.println("Thread: HTTP_FINISHED");
//#endif
}

// -------------------
// connect Post
// ===================

public byte[] connectPost(byte[] data) throws IOException
{
	byte[] res = null;
	/*HttpConnection c = null;
	OutputStream os = null;
	InputStream is = null;
	try {
		c = (HttpConnection)Connector.open(connectUrl);
		c.setRequestProperty("Connection", "close");
		c.setRequestMethod(HttpConnection.POST);
		c.setRequestProperty("Content-Type", "application/mr2");

		// Set the POST data
		// nota -> a partir de open output stream no se puede llamar a metodos que impliquen
		// cambiar los headers. en el wtk rula pero en el resto del trastos peta.

		os = c.openOutputStream();

		// Si ya estamos logados; lo primero que hay que enviar es la cookie y el
		// ultimo server time

		os.write(data);

		os.flush();

		os.close();
		os = null;

	//#ifdef DOJA
		c.connect();
	//#endif
		// Get the status code, causing the connection to be made
		int status = c.getResponseCode();

		// Only HTTP_OK (200) means the content is returned.
		if (status != HttpConnection.HTTP_OK)
		{
		//#ifdef DEBUG
			throw new IOException("Response status not OK [" + status + "]");
		//#else
			throw new IOException();
		//#endif
		}

	// Agregar la respuesta al buffer de recepcion
		is = c.openInputStream();
		ByteArrayOutputStream tos = new ByteArrayOutputStream();
		try {

			byte[] buffer = new byte[512];
			int size = 0;
			do {
				size = is.read(buffer, 0, buffer.length);
				if (size > 0) {tos.write(buffer, 0, size);}
			} while (size >= 0);

			tos.flush();
			
			res = tos.toByteArray();
			
			tos.close();
			tos = null;
		} finally {
			if (tos != null) {
				tos.close();
			}
		}
		
		is.close();
		c.close();
		is = null;
		c = null;
	} finally {
		if (os != null) {
			os.close();
		}
		if (is != null) {
			is.close();
		}
		if (c != null) {
			c.close();
		}
		
		System.gc();
	}*/
	
	return res;
}

// <=- <=- <=- <=- <=-








// ------------------------------------------------------------------------------------------------------------------------
// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};