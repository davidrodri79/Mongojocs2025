package com.mygdx.mongojocs.snaiky;


import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.IApplication;

public class Moto extends IApplication //i/implements CommandListener
{
    Display display;
    Game pantgame;
    static Moto joc;
    static boolean   sfx = true;
    static boolean   rocco = true;
    
// *******************
// -------------------
// Prefs - Engine
// ===================
// *******************

byte[] prefsData = new byte[] {1,1};


    public Moto()
    {
        start();
    }

   
   protected void destroyApp (boolean b)
   {
        PrefsSET();
        terminate();
	   //i/display.setCurrent(null);
	   //i/this.notifyDestroyed();       // notify KVM
   }


   protected void pauseApp () {}


   public void start ()
   {
    	System.gc();
    	joc = this;
    	
 	    pantgame = new Game();    
        display.setCurrent(pantgame);  	//hacemos display del pantgame.	      
   }
   
   
    // ------------------- 
    // Prefs INI 
    // =================== 
 
    public void PrefsINI() 
    { 		   
        
        	prefsData = pantgame.FS_LoadFile(0,0);

            if (prefsData[0] != 0) sfx = true;
            else sfx = false;
	        if (prefsData[1] != 0) rocco = true;
            else rocco = false;

    } 
 
 
	// ------------------- 
	// Prefs SET 
	// =================== 
 
	public void PrefsSET() 
	{ 	
	    if (sfx) prefsData[0]= (byte)1;
	    else prefsData[0]= (byte)0;
    	if (rocco) prefsData[1]= (byte)1;
	    else prefsData[1]= (byte)0;
	
	    pantgame.FS_SaveFile(0,0, prefsData);

	} 

   
   
}
