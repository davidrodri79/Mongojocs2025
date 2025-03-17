package com.mygdx.mongojocs.poyogarden;//////////////////////////////////////////////////////////////////////////
// KInsectors - Nokia ColorVersion 0.01
// By Carlos Peris
// MICROJOCS MOBILE
//////////////////////////////////////////////////////////////////////////

import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextField;

public class PoyoP extends MIDlet implements CommandListener
{
   Command doneCommand = new Command("Done", Command.SCREEN, 1);
      static Form scoreForm;
   static Display display;
   static boolean sfx = true;
   static boolean rocco = true;
   static boolean help = true;
   static int lastfase;
   static PoyoP kins;
   static Game pantgame;
  // static Sound bp[];
  int puntos;
  TextField scoreField;   
   
   
   
    public void commandAction(Command c, Displayable d)
	{	   		
		if (c == doneCommand) 
		{		    
           pantgame.scores.addHighScore(puntos, scoreField.getString());
           display.setCurrent(pantgame);
           //pantgame.sigue = true;
       }
	}

   
   
   public void enterHighScore(int p)    
   {        
        puntos = p;
        scoreField.setString("");
        display.setCurrent(scoreForm);        
   }
   
   
   
   public void destroyApp (boolean b)
   {	
   	PrefsData[0] = 0;
   	PrefsData[1] = 0;
   	PrefsData[2] = 0;
   	if (!sfx) PrefsData[0] = 1;
   	if (!rocco) PrefsData[1] = 1;    	
   	if (!help) PrefsData[2] = 1;    	
   	PrefsData[3] = (byte)lastfase;
    	PrefsSET();
   	display.setCurrent(null);
	   this.notifyDestroyed();       // notify KVM
   }

   public void pauseApp () {}

   public void startApp ()
   {
    	System.gc();
    	kins = this;
 	   display = Display.getDisplay(this);
 	    scoreForm = new Form(MMenu.score[0]);
      scoreField = new TextField(MMenu.score[1], "", 3, TextField.ANY);
      scoreForm.append(scoreField);   
      scoreForm.addCommand(doneCommand);
      scoreForm.setCommandListener(this);
 	   
 	   
 	   PrefsINI();//abre y recupera 
 	   sfx= (PrefsData[0] == 0); 
      rocco = (PrefsData[1] == 0); 
      help = (PrefsData[2] == 0); 
      lastfase = PrefsData[3];     
      Intro intr = new Intro();
      display.setCurrent(intr);      
      
  }

   public static void NewGame()
   {
        System.gc();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                pantgame = new Game();
                display.setCurrent(pantgame);  	//hacemos display del pantgame.
            }
        });
   }
  
 
byte[] PrefsData = {0,0,0,0}; //0=si 1=no 
// ------------------- 
// Prefs INI 
// =================== 
 
public void PrefsINI() 
{ 		   
	 RecordStore rs;
	 
	 try { 
	 rs = RecordStore.openRecordStore("PPPrefs", true); 
	 
	 if (rs.getNumRecords() != 0) 
	 //{ 
  	 //   rs.addRecord(PrefsData, 0, PrefsData.length); 
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
		 rs = RecordStore.openRecordStore("PPPrefs", true); 
		 
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
