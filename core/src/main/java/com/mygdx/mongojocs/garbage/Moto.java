package com.mygdx.mongojocs.garbage;

import com.mygdx.mongojocs.iapplicationemu.Display;
import com.mygdx.mongojocs.iapplicationemu.IApplication;

/**
 *Microjocs Mobile,S.L.
 */

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



    
   
    
    
   
   protected void destroyApp (boolean b)
   {
        PrefsSET();
        terminate();
	   //i/display.setCurrent(null);
	   //i/this.notifyDestroyed();       // notify KVM
   }


   protected void pauseApp () {}


    public Moto()
    {
        start();
    }

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
	        
        
    /*    byte data[] = pantgame.FS_LoadFile(10, 0);
	    
	    try { 
	    
	        if (data[0] == '0') 
	        { 
	            //DEFAULT SETTINGS
	            sfx = true;
	            rocco = true;
	            stage = 0;
	            dificultad = 2;
	            gpdificultad = 2;
	            
	            for(int i = 0;i < 8;i++)
    		    {		 
    		        racetimes[i] = 3600000-1;    		        
    		        laptimes[i] = 3600000-1; 
    		        racenames[i] = "AAA";    		                    		            
    		        lapnames[i] = racenames[i];
    		    }		 		 
    		    joculto[1] = true;
    		    gpname = "Toni Elias";//PPP
	        } else { 
	            
	            ByteArrayInputStream bais = new ByteArrayInputStream(data);
	            DataInputStream is = new DataInputStream(bais);
      	        sfx = is.readBoolean();    
      	        rocco = is.readBoolean();    
      	        climate = is.readBoolean();    
      	        gpclimate = is.readBoolean();    
      	        stage = is.readInt();    
      	        dificultad = is.readInt();    
      	        gpdificultad = is.readInt();    
    		    for(int i = 0;i < 8;i++)
    		    {		 
    		        racetimes[i] = is.readLong();
    		        laptimes[i] = is.readLong();    
    		        racenames[i] = is.readUTF();
    		        lapnames[i] = is.readUTF();    
    		        points[i] = is.readInt();    
    		    }		 		 
    		    for(int i = 0;i < 3;i++) joculto[i] = is.readBoolean();
    		    gpname = is.readUTF();//PPP
    		    jugador = is.readUTF();        		
    		    is.close();
	        } 	       
	    } catch(Exception e) {} 	*/
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

		 
		 /*try {     		 
    		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		 DataOutputStream os = new DataOutputStream(baos); 
      	     os.writeBoolean(sfx);    
      	     os.writeBoolean(rocco);    
      	     os.writeBoolean(climate);    
      	     os.writeBoolean(gpclimate);    
      	     os.writeInt(stage);    
      	     os.writeInt(dificultad);    
      	     os.writeInt(gpdificultad);    
    		 for(int i = 0;i < 8;i++)
    		 {		 
        		    os.writeLong(racetimes[i]);
        		    os.writeLong(laptimes[i]);    
        		    os.writeUTF(racenames[i]);
        		    os.writeUTF(lapnames[i]);    
        		    os.writeInt(points[i]);    
    		 }		 	
    		 for(int i = 0;i < 3;i++) os.writeBoolean(joculto[i]);	 
    		 os.writeUTF(gpname);    //PPP
    		 os.writeUTF(jugador);    
    		 os.flush();
    		 os.close();
    		 byte[] data = baos.toByteArray();
             pantgame.FS_SaveFile(10, 0, data);    		 
    		 baos.close();
 		 } catch(Exception e) {} */
	} 

   
   
}
