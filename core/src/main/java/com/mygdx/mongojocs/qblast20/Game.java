

// -----------------------------------------------
// Microjocs BIOS v4.0 Rev.3 (1.4.2004)
// ===============================================
// Logica
// ------------------------------------


//#ifdef PACKAGE
package com.mygdx.mongojocs.qblast20;
//#endif


//#ifdef J2ME
//#elifdef DOJA
//#endif

import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
//import com.mygdx.mongojocs.midletemu.Runnable;
import com.mygdx.mongojocs.midletemu.TextField;
//import com.mygdx.mongojocs.midletemu.Thread;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.io.*;







// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - com.mygdx.mongojocs.sanfermines2006.Game
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME
public class Game extends MIDlet        // Clase Bios integrada en la com.mygdx.mongojocs.sanfermines2006.Game
	//#ifndef NONETWORK
	 implements Runnable, CommandListener
	//#endif
		   
//#elifdef DOJA
//#endif
{
static GameCanvas gc;


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Contructor - Metodo que ARRANCA al ejecutar el MIDlet
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


public Game()                                                               // Clase Bios integrada en la com.mygdx.mongojocs.sanfermines2006.Game
{		
		
	System.gc();
	gc = new GameCanvas(this);
	
	//#ifdef J2ME
	Display.getDisplay(this).setCurrent(gc);
	//#endif
	
}

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// startApp - Al inicio y cada vez que se hizo pauseApp()
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//#ifdef J2ME

public void startApp()
{
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("--startApp()");
	//#endif
	
	Display.getDisplay(this).setCurrent(gc);
	gc.gamePaused=false;
}

public void pauseApp() 
{
	//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
	Debug.println("--pauseApp()");
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

//#ifndef NONETWORK
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
	//#ifdef INPUTDIALOGDELAY
	try{
		gc.thread.sleep(500);
	}catch(Exception e){};
	//#endif
	
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

public void inputDialogNotify(String s)
{
	if(gc.gameStatus == gc.GAME_MENU_INPUT_RECORD_NAME)
		gc.myRecordName = s;
	if(gc.gameStatus == gc.GAME_MENU_INPUT_DOWNLOAD_CODE)
		gc.downloadCode = s;
		
	gc.gameStatus++;
}

//#elifdef DOJA
//#endif


// Communication stuff ===========================================================================================


private static final int protocolVersion = 100;

//#ifdef J2ME
private static final String url = "http://titanic.mjlabs.net:8080/QBLAST2/QBlast2Access";
//#endif

int controllerID = 0, methodID = 0;
byte[] methodArgs, serverResponse;
public boolean threadBlocked, answerIsOk;
Thread clientThread;


public byte[] invokeServer(int gameID, int controllerID, int methodID, byte[] args) throws IOException 
{
	
	byte[] res = null;
	/*
	HttpConnection c = null;
	OutputStream os = null;
	InputStream is = null;
	DataOutputStream dos = null;
	try {
		//#ifdef DOJA
		//#endif
		//#ifdef J2ME
		c = (HttpConnection)Connector.open(url);
		//#endif
		c.setRequestProperty("Connection", "close");
		c.setRequestMethod(HttpConnection.POST);
		c.setRequestProperty("Content-Type", "application/mccom");

		// Set the POST data
		// nota -> a partir de open output stream no se puede llamar a metodos que impliquen
		// cambiar los headers. en el wtk rula pero en el resto del trastos peta.

		os = c.openOutputStream();
		dos = new DataOutputStream(os);
		dos.writeInt(protocolVersion);
		dos.writeInt(gameID);
		dos.writeInt(controllerID);
		dos.writeInt(methodID);
		dos.writeLong(com.mygdx.mongojocs.sanfermines2006.GameCanvas.userID);
		dos.write(args);
		
		dos.flush();

		dos.close();
		dos = null;
		os = null;

		//#ifdef DOJA
		//#endif
		// Get the status code, causing the connection to be made
		int status = c.getResponseCode();
		// Only HTTP_OK (200) means the content is returned.
		if (status != HttpConnection.HTTP_OK) {			
			throw new IOException(
				//#ifdef com.mygdx.mongojocs.qblast20.com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
				"Response status not OK [" + status + "]"
				//#endif
				);			
		}

		// Agregar la respuesta al buffer de recepcion
		is = c.openInputStream();
		ByteArrayOutputStream tos = new ByteArrayOutputStream();
		try {
			int b = 0;
			while (b != -1) {
				b = is.read();
				if (b != -1) {
					tos.write(b);
				}
			}

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
	}catch(Exception e){
		
		res = null;
		
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
	}
		*/
	return res;
}

public void runInit()
{
	//#ifdef NETWORKDEBUG
	Debug.println("Inicia el thread de Network");
	//#endif NETWORKDEBUG

	try{

		serverResponse = invokeServer(GameCanvas.MCGAME_QBLAST2, controllerID, methodID, methodArgs);

		answerIsOk = true;

	} catch (IOException e) {

		//#ifdef NETWORKDEBUG
		Debug.println("Error de conexion");
		//#endif NETWORKDEBUG
		answerIsOk = false;
	}

	threadBlocked = false;

	//#ifdef NETWORKDEBUG
	Debug.println("Termina el thread de Network");
	//#endif NETWORKDEBUG
}

public void runTick()
{


}

public void runEnd()
{

}

public void run()
{	
	//#ifdef NETWORKDEBUG
	Debug.println("Inicia el thread de Network");
	//#endif NETWORKDEBUG
		
	try{
	
		serverResponse = invokeServer(GameCanvas.MCGAME_QBLAST2, controllerID, methodID, methodArgs);				
		
		answerIsOk = true;
	
	} catch (IOException e) {
			
			//#ifdef NETWORKDEBUG
			Debug.println("Error de conexion");
			//#endif NETWORKDEBUG				
			answerIsOk = false;		
	}
	
	threadBlocked = false;
		
	//#ifdef NETWORKDEBUG
	Debug.println("Termina el thread de Network");
	//#endif NETWORKDEBUG
}

//#endif

// <=- <=- <=- <=- <=-



// ------------------------------------------------------------------------------------------------------------------------
// **************************************************************************//
// Final Clase com.mygdx.mongojocs.sanfermines2006.Game
// **************************************************************************//

//#ifdef DOJA
    
//#endif

};