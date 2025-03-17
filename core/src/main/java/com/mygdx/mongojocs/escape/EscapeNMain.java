/*
	"Escape Color" for the Nokia 7650 Coded by David Rodriguez 2003
*/

package com.mygdx.mongojocs.escape;

import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextField;


//import com.nokia.mid.ui.*;


public class EscapeNMain extends MIDlet implements CommandListener
{
	//static final String EXIT_COMMAND_LABEL = "Exit";
	//static final String AGAIN_COMMAND_LABEL = "Reset";
	static final String DONE_COMMAND_LABEL = "Done";
		
	static EscapeNCanvas pant;
	public static Display display;
	public static Form scoreForm;
	public static TextField scoreField;
	byte[] PrefsData = new byte[] {0,1};	
	private String Prefsname;
	public static EscapeNMain gameMidl;


	public void startApp()
	{
		pant = new EscapeNCanvas();
		
		gameMidl=this;
		
		PrefsINI("EscapePrefs");
		if(PrefsData[0]==0) pant.snd_on=true;
		else pant.snd_on=false;
		
		if(PrefsData[1]==0) EscapeNCar.vibr_on=true;
		else EscapeNCar.vibr_on=false;
		
		display = Display.getDisplay(this);
		
		//Command exitCommand  = new Command(EXIT_COMMAND_LABEL , Command.EXIT, 0);
		//Command againCommand = new Command(AGAIN_COMMAND_LABEL, Command.OK, 1);
		/*pant.addCommand(exitCommand);
		pant.addCommand(againCommand);
		pant.setCommandListener(this);	*/
				
		// Form for the record name entry				
						
		Command doneCommand = new Command(DONE_COMMAND_LABEL, Command.OK, 1);
		
		scoreForm = new Form("New record");
        	scoreField = new TextField("Type in your name!", "",3, TextField.ANY);
		scoreField.setMaxSize(3);
        	scoreForm.append(scoreField);   
		scoreForm.addCommand(doneCommand);
        	scoreForm.setCommandListener(this);
    		System.gc();
								
		display.setCurrent(pant);
			
	}
	public void commandAction (Command c, Displayable d)
	{
	       String label = c.getLabel();
	       if (label == DONE_COMMAND_LABEL ) {
	      	
	      		display.setCurrent(pant);
	      		pant.set_state(16);	      	        	      	
	       } 
	       
	       /*if (label == EXIT_COMMAND_LABEL ) {
	      	
	      		System.gc();
	      	        destroyApp(false);
	       } 
	       if (label == AGAIN_COMMAND_LABEL ) {
	       	
	       		System.gc();
	       		pant.set_state(0);
	      			      		
	       }*/
	}	
	protected void pauseApp()
	{
	}
	
	public void destroyApp(boolean unconditional)
	{
		if (pant.snd_on==true) PrefsData[0]=0;
		else PrefsData[0]=1;
		if (EscapeNCar.vibr_on==true) PrefsData[1]=0;
		else PrefsData[1]=1;
		PrefsSET();
		
		display.setCurrent(null);
		this.notifyDestroyed();

	}
	// -------------------
	// Prefs INI
	// ===================
	
	public void PrefsINI(String name)
	{
		Prefsname=name;
		RecordStore rs;
	
		try {
			rs = RecordStore.openRecordStore(Prefsname, true);
	
			if (rs.getNumRecords() == 0)
			{
			rs.addRecord(PrefsData, 0, PrefsData.length);
			} else {
			PrefsData=rs.getRecord(1);
			}
			rs.closeRecordStore();
			
		} catch(Exception e) {}
	}
	
	// -------------------
	// Prefs SET
	// ===================
	
	public void PrefsSET()
	{
		RecordStore rs;
	
		try {
			rs = RecordStore.openRecordStore(Prefsname, true);
	
			if (rs.getNumRecords() == 0)
			{
			rs.addRecord(PrefsData, 0, PrefsData.length);
			} else {
			rs.setRecord(1, PrefsData, 0, PrefsData.length);
			}
			rs.closeRecordStore();
			
		} catch(Exception e) {}
	}	
}