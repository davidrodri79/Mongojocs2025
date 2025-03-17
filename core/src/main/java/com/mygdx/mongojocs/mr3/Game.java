package com.mygdx.mongojocs.mr3;

// -----------------------------------------------
// Microjocs BIOS v5.0 Rev.11 (11.7.2005)
// ===============================================


//#ifdef PACKAGE

//#endif


//#ifdef J2ME
//#elifdef DOJA
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.TextField;
//#endif

import java.io.InputStream;



// ---------------------------------------------------------
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// MIDlet - Game 
// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// ---------------------------------------------------------

//#ifdef J2ME
public class Game extends MIDlet implements CommandListener    // Clase Bios integrada en la Game
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




// ------------------------------------------------------------------------------------------------------------------------
// **************************************************************************//
// Final Clase Game
// **************************************************************************//
};