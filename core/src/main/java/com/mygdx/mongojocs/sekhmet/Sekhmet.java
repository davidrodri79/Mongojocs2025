package com.mygdx.mongojocs.sekhmet;//////////////////////////////////////////////////////////////////////////
// Wrath Of Sekhmet - Nokia Version 1.0
// By Carlos Peris
// MICROJOCS MOBILE
//////////////////////////////////////////////////////////////////////////

import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;

/**
 *Microjocs Mobile,S.L.
 */

public class Sekhmet extends MIDlet// implements CommandListener
{
 
   Display display;
   //Game pantgame;
   static boolean sfx = true;
   static boolean rocco = true;
   static Sekhmet sek;
   //static Sound bp[];
   
   
   
   
   protected void destroyApp (boolean b)
   {	
   	PrefsData[0] = 0;
   	PrefsData[1] = 0;
   	if (!sfx) PrefsData[0] = 1;
   	if (!rocco) PrefsData[1] = 1;    	
    	PrefsSET();
   	display.setCurrent(null);
	   this.notifyDestroyed();       // notify KVM
   }

   protected void pauseApp () {}

   public void startApp ()
   {
    	//System.gc();
    	sek = this;
 	   display = Display.getDisplay(this);
 	   PrefsINI();//abre y recupera 
 	   //PrefsSET();
      sfx= (PrefsData[0] == 0); 
      rocco = (PrefsData[1] == 0); 
      //if (PrefsData[0] == (byte)1) sfx = false;
 	   //pantgame = new Game();
 	   display.setCurrent(new Game());  	//hacemos display del pantgame.
  }

  
 
byte[] PrefsData = {0,0}; //0=si 1=no 
// ------------------- 
// Prefs INI 
// =================== 
 
public void PrefsINI() 
{ 		   
	 RecordStore rs;
	 
	 try { 
	 rs = RecordStore.openRecordStore("SPrefs", true); 
	 
	 if (rs.getNumRecords() == 0) 
	 { 
  	    rs.addRecord(PrefsData, 0, PrefsData.length); 
	 } else { 
	    PrefsData = rs.getRecord(1); 
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
		 rs = RecordStore.openRecordStore("SPrefs", true); 
		 
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
