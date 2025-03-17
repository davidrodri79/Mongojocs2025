package com.mygdx.mongojocs.kinsectors;//////////////////////////////////////////////////////////////////////////
// KInsectors - Nokia ColorVersion 0.01
// By Carlos Peris
// MICROJOCS MOBILE
//////////////////////////////////////////////////////////////////////////


import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextField;

public class KInsectors extends MIDlet implements CommandListener
{
 
   Display display;
   static boolean sfx = true;
  // static boolean rocco = true;
   static int cfase = 1;
   static KInsectors kins;

   int puntos;
   Command doneCommand;
   Form scoreForm;
   static TextField scoreField;
	Game joc;
	static Scores sc;

   
   public void enterHighScore(int p)    
   {
      puntos = p;
      scoreField.setString("");
      Display.getDisplay(this).setCurrent(scoreForm);      
   }
   
   public void commandAction(Command c, Displayable d)
	{	   	 	
		if (c == doneCommand) {
           sc.addHighScore(puntos, scoreField.getString().substring(0,3));
           Display.getDisplay(this).setCurrent(joc);
           //joc.sigue = true;
       }
	} 
   
   
   protected void destroyApp (boolean b)
   {	
   	PrefsData[0] = 0;
   	//PrefsData[1] = 0;
   	if (!sfx) PrefsData[0] = 1;
   	PrefsData[1] = (byte)cfase;
   	//if (!rocco) PrefsData[1] = 1;    	
    	PrefsSET();
   	display.setCurrent(null);
	   this.notifyDestroyed();       // notify KVM
   }

   protected void pauseApp () {}

   public void startApp ()
   {
    	System.gc();
    	kins = this;
 	   display = Display.getDisplay(this);
 	   PrefsINI();//abre y recupera 
 	   sfx= (PrefsData[0] == 0); 
 	   cfase = PrefsData[1];
      //rocco = (PrefsData[1] == 0);       
      
      sc = new Scores("kinsscores");
      //doneCommand = new Command("Done", Command.SCREEN, 1);
      doneCommand = new Command(Lang.scores[0], Command.SCREEN, 1);
      //scoreForm = new Form("Congratulations");
      scoreForm = new Form(Lang.scores[1]);
      //scoreField = new TextField("Your score is one of the 3 best. Enter your initials", "", 3, TextField.ANY);
      scoreField = new TextField(Lang.scores[2], "", 4, TextField.ANY);
      scoreForm.append(scoreField);   
      scoreForm.addCommand(doneCommand);
      scoreForm.setCommandListener(this);
      joc = new Game();
      //joc = new Game();
      display.setCurrent(joc);  	//hacemos display del pantgame.
  }

  
 
byte[] PrefsData = {0,1}; //0=si 1=no 
// ------------------- 
// Prefs INI 
// =================== 
 
public void PrefsINI() 
{ 		   
	 RecordStore rs;
	 
	 try { 
	 rs = RecordStore.openRecordStore("KIPrefs", true); 
	 
	 if (rs.getNumRecords() != 0) 
	 //{ 
  	    //rs.addRecord(PrefsData, 0, PrefsData.length); 
	 //} else { 
	    PrefsData = rs.getRecord(1); 
	 //} 
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
		 rs = RecordStore.openRecordStore("KIPrefs", true); 
		 
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
