package com.mygdx.mongojocs.rescue;// RESCUE 7650. v1.5 01/03/2003

//package rescue;


import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.RecordStore;

public class rescue
	extends MIDlet
	implements CommandListener
{
	Display display;
	GameCanvas gameCanvas;
	
	protected void destroyApp (boolean b)
	{
		PrefsSave ();
		display.setCurrent(null);
		this.notifyDestroyed();       // notify KVM
	}
	
	protected void pauseApp ()
	{
	}
	
	public void startApp ()
	{
	      	System.gc ();
	 	display = Display.getDisplay(this);
		PrefsLoad();
		gameCanvas = new GameCanvas (this);
	     	System.gc ();
		display.setCurrent (gameCanvas);  	//hacemos display del pantgame.	
	}
	
	public void commandAction (Command c, Displayable d)
	{
	}
	//******************************* end midlet


	// *******************
	// -------------------
	// Prefs - Engine
	// ===================
	// *******************
	
	//int	bestTimes [] = { 599, 599, 599 };
	//boolean	bVibra=true, bSound=true;

	byte[] PrefsData = new byte[8];
			// Sound, Vibra, Time1Lev0, Time0Lev0, Time1Lev1, Time0Lev1, Time1Lev2, Time0Lev2
		
	public void PrefsLoad()
	{
		RecordStore rs;
	
		try {
			rs = RecordStore.openRecordStore("RescuePrefs", true);
	
			if (rs.getNumRecords() == 0)
			{
			//rs.addRecord(PrefsData, 0, PrefsData.length);
			PrefsData [0] = (byte)(1);
			PrefsData [2] = 59;
			PrefsData [3] = 9;
			PrefsData [4] = 59;
			PrefsData [5] = 9;
			PrefsData [6] = 59;
			PrefsData [7] = 9;

			} else {
			PrefsData=rs.getRecord(1);
			// Save to GCData
	/*		gameCanvas.bSound = PrefsData [0]==1;
			gameCanvas.bVibra = PrefsData [1]==1;
			gameCanvas.bestTimes [0] = PrefsData [2]*10+PrefsData[3];
			gameCanvas.bestTimes [1] = PrefsData [4]*10+PrefsData[5];
			gameCanvas.bestTimes [2] = PrefsData [6]*10+PrefsData[7];	
	*/		}
			rs.closeRecordStore();		
			
		} catch(Exception e) {}
	}
		
	public void PrefsSave()
	{
		RecordStore rs;
	
		try {
			// Load from GCData
			PrefsData [0] = gameCanvas.bSound ? (byte)(1):(byte)(0);
			PrefsData [1] = gameCanvas.bVibra ? (byte)(1):(byte)(0);
			PrefsData [2] = (byte) (gameCanvas.bestTimes [0]/10);
			PrefsData [3] = (byte) (gameCanvas.bestTimes [0]%10);
			PrefsData [4] = (byte) (gameCanvas.bestTimes [1]/10);
			PrefsData [5] = (byte) (gameCanvas.bestTimes [1]%10);
			PrefsData [6] = (byte) (gameCanvas.bestTimes [2]/10);
			PrefsData [7] = (byte) (gameCanvas.bestTimes [2]%10);
			
			
			rs = RecordStore.openRecordStore("RescuePrefs", true);
	
			if (rs.getNumRecords() == 0)
			{
			rs.addRecord(PrefsData, 0, PrefsData.length);
			} else {
			rs.setRecord(1, PrefsData, 0, PrefsData.length);
			}
			rs.closeRecordStore();
		} catch(Exception e) {}
	}
	
	// <=- <=- <=- <=- <=-
}
