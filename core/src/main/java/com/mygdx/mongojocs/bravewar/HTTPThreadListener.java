package com.mygdx.mongojocs.bravewar;
/**
 * 
 */

import com.mygdx.mongojocs.bravewar.Connector;
import com.mygdx.mongojocs.bravewar.GameMidletLogic;


import java.util.*;

/**
 * 
 * Outgoing: Attack (First/Accept/Ack), Buy Food, Buy Troops, Move Troops, Message (to All/to Player), Exit.
 * Incoming: Attack (First/Accept/Ack), Buy Food ACK, Buy Troops ACK, Move Troops ACK, End of Month (money & men & food update), com.mygdx.mongojocs.bravewar.Region Update, Message, Exit Player.
 * Messages
 */
public class HTTPThreadListener extends Thread {
	
  ///////////////////////////////////////
  // attributes


    private int 		sleepTime=10000;
    private Connector 	connector; 
    private GameMidletLogic midlet;
    
    
    boolean		bPollData=true;
    boolean		bPolling=false;
    
    boolean		bDisconnectWhenPossible=false;
    
    
    Vector		vecMsgToSend = new Vector();
	Vector		vecMsgToParse = new Vector();


	int		sleepCount = 0;


  ///////////////////////////////////////
  // operations
 
 	HTTPThreadListener(GameMidletLogic m, Connector c)
 	{
 		midlet		= m;
 		connector	= c;
 	} 


	public void run()
	{
	    Object inMessage = null;
		Hashtable 	msgOut;
	    
	    int		pollCount = 0;
	    
	    for(;;)
	    {
	    	while (vecMsgToSend.size() == 0 && sleepCount < 4)
	    	{
		    	try {
		    		sleep(500);
		    		sleepCount ++;
		    	} catch (Exception e) {}
			}
			
			sleepCount = 0;
			
			if (bPollData)
			{
				bPolling = true;
				if (vecMsgToSend.size() == 0)
				{
					msgOut = new Hashtable();
					//midlet.strError = midlet.strError + ".";
				}
				else
				{
					synchronized(vecMsgToSend)
					{
						msgOut = (Hashtable) vecMsgToSend.elementAt(0);	
						vecMsgToSend.removeElementAt(0);
					}
					//midlet.strError = midlet.strError + ":";
				}
				System.gc();
				try {
						pollCount ++;
						inMessage = connector.sendData(msgOut);//poll (midlet.lobby_arl);
						System.out.println("Poll: "+(String) connector.pecan.getValue(inMessage, "commBuffer"));
						
						synchronized (vecMsgToParse)
						{
							if (bDisconnectWhenPossible==false)
								vecMsgToParse.addElement(inMessage);
							inMessage = null;
						}
						
						//for (Enumeration e=inMessage.params.elements (); e.hasMoreElements ();)	System.out.println (e.nextElement ());	
				} catch (Exception e) 
				{ 
					System.out.println("Error At poll:"); e.printStackTrace ();
				   //midlet.strError = "EPoll:"+pollCount+" "+e.toString ();
				   continue;
				}
				
				bPolling = false;
			}
						
			//sendToParse (inMessage);
			//inMessage = null;
			
			if (bDisconnectWhenPossible)
			{
				try {
					connector.disconnect();
				} catch (Exception e) { e.printStackTrace(); }
				bDisconnectWhenPossible = false;
				break;
			}
	    }
	}


	void sendToParse(Object inMessage)
	{
		int	iMsgPos;
		
		if (inMessage==null)		return;
		
		//String commBuffer = (String) inMessage.get ("commBuffer");
		String commBuffer = (String) connector.pecan.getValue(inMessage,"commBuffer");		
		
		if (commBuffer==null || commBuffer.length() < 2)		return;

		iMsgPos = 0;
		while (iMsgPos < commBuffer.length())
		{
			short	len		= midlet.readShort (commBuffer, iMsgPos);
			byte	type	= midlet.readByte (commBuffer, iMsgPos+4);
			
			midlet.parseMessage(type, commBuffer.substring (iMsgPos+6, iMsgPos+6+len));
		
			iMsgPos += len+6;
		}
	}	

/**
 * Does ...
 * 
 * @param msTime ...
 * 
 */
    public void setSleepTime (int msTime) {        
        // your code here
    } // end setSleepTime        

/**
 * Does ...
 * 
 * @param data ...
 * 
 */
/*
    public void sendMessage (byte[] data) {
    	Message		msgOut = new Message ();
    	
    	msgOut.ARL = midlet.lobby_arl;
    	msgOut.params = new Hashtable ();

    	String	strMsg = new String (data);
    	    	
    	msgOut.params.put ("commBuffer", strMsg);
    	
    	try
    	{
    		Message	msgIn = connector.sendData (msgOut);
			//for (Enumeration e=msgIn.params.elements (); e.hasMoreElements ();)	System.out.println (e.nextElement ());	

 			sendToParse (msgIn);
    	} 
    	catch (Exception e)
    	{
    		midlet.strError = e.toString ();
    		e.printStackTrace ();
    	}
    } // end sendMessage        

*/

    public void sendMessage(StringBuffer strBufferMsg) {
    	Hashtable		msgOut = new Hashtable();
    	
    	// Ojo, utilizamos SendRecv luego!!
    	msgOut.put("U_commBuffer", new String(strBufferMsg));
    	
    	//midlet.strError = "aMsg";
    	
    	synchronized(vecMsgToSend)
    		{
    		vecMsgToSend.addElement(msgOut);
    		}
    	
    	/*
    	try
    	{
    		Message	msgIn = connector.sendData (msgOut);
			//for (Enumeration e=msgIn.elements (); e.hasMoreElements ();)	System.out.println (e.nextElement ());	

 			sendToParse (msgIn);
    	} 
    	catch (Exception e)
    	{
    		midlet.strError = e.toString ();
    		e.printStackTrace ();
    	}
    	*/
    } // end sendMessage        

} // end com.mygdx.mongojocs.bravewar.HTTPThreadListener



