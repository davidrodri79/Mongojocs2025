package com.mygdx.mongojocs.mr;

import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;
import com.mygdx.mongojocs.midletemu.TextField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *Microjocs Mobile,S.L.
 */

public class Moto extends MIDlet implements CommandListener
{
    static Display display;
    Game pantgame;
    static Moto joc;
    static boolean   sfx = true;
    static boolean   rocco = false;
    static boolean   climate = true;
    static boolean   gpclimate = true;
    
    static long []   racetimes = new long[8];
    static String [] racenames = new String[8];
    static long []   laptimes = new long[8];
    static String [] lapnames = new String[8];
    static int []    points = new int[8];
    static int       stage;
    static int       dificultad;
    static int       gpdificultad;
    static String    jugador = "ZNR";
    Command doneCommand;
    Form nameForm;
    static TextField nameField;      
    boolean joculto[] = new boolean[3];
    static String gpname;//PPP
    
	
    public void enterName()    
    {
        nameField.setString(jugador);
        Display.getDisplay(this).setCurrent(nameForm);      
    }
   
   
    public void commandAction(Command c, Displayable d)
	{	   	 	
		if (c == doneCommand) 		
		{
		    jugador = nameField.getString();
            Display.getDisplay(this).setCurrent(pantgame);                  
        }
	}
   
   
   protected void destroyApp (boolean b)
   {
        PrefsSET();
	   display.setCurrent(null);
	   this.notifyDestroyed();       // notify KVM
   }


   protected void pauseApp () {}


   public void startApp ()
   {
    	System.gc();
    	joc = this;
    	PrefsINI();
 	    display = Display.getDisplay(this);   
 	    doneCommand = new Command(Lang.scores[0], Command.SCREEN, 1);
        nameForm = new Form(Lang.scores[1]);
      
        nameField = new TextField(Lang.scores[2], "", 3, TextField.ANY);
        nameForm.append(nameField);   
        nameForm.addCommand(doneCommand);
        nameForm.setCommandListener(this);
        pantgame = new Game();
        display.setCurrent(pantgame);  	//hacemos display del pantgame.	      
   }
   
   
    // ------------------- 
    // Prefs INI 
    // =================== 
 
    public void PrefsINI() 
    { 		   
	    RecordStore rs;
	 
	    try { 
	        rs = RecordStore.openRecordStore("MRagePrefs", true); 
	 
	        if (rs.getNumRecords() == 0) 
	        { 
	            //rs.addRecord(PrefsData, 0, PrefsData.length);   	            
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
	            byte data[] = rs.getRecord(1); 
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
    		    is.close();    		    
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
    		 rs = RecordStore.openRecordStore("MRagePrefs", true); 
    		 
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
    		 os.flush();
    		 os.close();
    		 byte[] data = baos.toByteArray();
    		 
    		 if (rs.getNumRecords() == 0) 
    		 { 
    		    rs.addRecord(data, 0, data.length); 
    		 } else { 
    		    rs.setRecord(1, data, 0, data.length); 
    		 } 
    		 baos.close();
    		 rs.closeRecordStore(); 
		 } catch(Exception e) {} 
	} 

   
   
}
