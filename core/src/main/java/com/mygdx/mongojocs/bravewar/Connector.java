package com.mygdx.mongojocs.bravewar;

import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Form;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.midletemu.TextField;
import com.mygdx.mongojocs.pecan.Authentication;
import com.mygdx.mongojocs.pecan.Pecan;

import java.util.*;

public class Connector implements CommandListener
{
	//String ARL="BWNL";
	Pecan pecan;
	
	HTTPThreadListener	threadCommListener;
	
	GameMidletLogic	gameMidlet;
	
	Form loginForm;
	TextField loginField;
	TextField	passField;
	Command cmdOk;
	
	String		strLoginUser=null;
	String		strLoginPass=null;
    
	void setUpLoginForm(MIDlet midlet)
	{
		System.out.println("Login form");
		System.gc();
		loginForm = new Form("Registro");
		
		loginField	= new TextField("Usuario:", "", 7, TextField.ANY);
		passField	= new TextField("Password:", "", 7, TextField.PASSWORD);
		
		loginForm.append(loginField);
		loginForm.append(passField);
		
		cmdOk = new Command("Registrarse", Command.SCREEN, 1);
		
		loginForm.addCommand(cmdOk);
		loginForm.setCommandListener(this);
	
		Display.getDisplay(midlet).setCurrent(loginForm);
		System.gc();
	}

	public void commandAction(Command c, Displayable d)
	{
		if (c == cmdOk)
		{
			System.out.println("Registrarse commmandaction");
			strLoginUser = loginField.getString();
			strLoginPass = passField.getString();    		
			Display.getDisplay(gameMidlet).setCurrent(gameMidlet.gameCanvas);
		}
	}
    

	void doLogin(GameMidletLogic midlet)
	{
		gameMidlet = midlet;

		if (pecan==null)
			pecan = new Pecan(midlet);
		
		System.out.println("Pecan Created");

		// Try to send one message
		Hashtable m = new Hashtable();
		m.put("U_u", "null");
		m.put("U_p", "null");
		m.put("Reg", "true");
		//This command is implemented in the TMEDefaultProxy
		//This call will throw an exception if the user is not registered
		m.put("U_command", "CHECKUSER");
		m.put("A", pecan.getAppProperty("AUTH"));
		Object response = null;
		try {
			System.out.println("Sending registrationCheck:"+m.toString());
			//throw new Exception("User Not Registered");
			response = pecan.sendRecv(m);
			System.out.println("First Message Sent: " + (String) pecan.getValue(response, "commBuffer"));
			onLoginSuccessful(response);
		} catch (Exception e)
		{
			// If not registered, try registering before sending the message
			if (e.getMessage().equals("User Not Registered"))
			{
				setUpLoginForm(midlet);
				while (strLoginUser == null) {}
				
				try {

					String login = Authentication.registerUser(pecan, strLoginUser, strLoginPass);
					
					System.out.println("User Registered as: " + login);
					
					// retry????
					Object reply = pecan.sendRecv(m);
							
					System.out.println("First Message Sent: " + (String) pecan.getValue(reply, "commBuffer"));
					onLoginSuccessful(reply);
					
				} catch (Exception e2)
				{
					e2.printStackTrace();
					onLoginFailed();
				}
			}
			else
			{
				//System.out.println("Another exception");
				e.printStackTrace();
				onLoginFailed();
			}
		}
	}
	
	void sendMessage(StringBuffer message)
	{
		/*
		Hashtable msgData = new Hashtable();
		
		msgData.put("commBuffer", message);
		return sendData(msgData);
		*/
		
		threadCommListener.sendMessage(message);
	}
	
	Object sendData(Hashtable msgData)
	{
		Object replyObject=null;		// use pecan.getValue(Object, Key);
	
		try {			
			// Try to send the message			
			replyObject = pecan.sendRecv(msgData); //replyObject = pecan.sendToARL(msgData, ARL);
		} catch (Exception e)
		{
		}		
		
		return replyObject;
	}

	void disconnect()
	{
		Hashtable msgDisconnect = new Hashtable();
		
		//msgDisconnect.put("A", ARL);
		msgDisconnect.put("command", "DELPLAYER");
		
		try {
			pecan.sendRecv(msgDisconnect);
		} catch (Exception e)
		{
		}
	}

	public void onLoginFailed()
	{
		//notifyLoginFailed();
		gameMidlet.set_state(GameMidletLogic.LOGIN_FAILED);
	}
	    
	// Pecan LoginListener handler
	public void onLoginSuccessful(Object reply) //com.mygdx.mongojocs.bravewar.Connector connector, Message[] availablePlayers, java.lang.Object context)
	{
		//System.out.println ("Login successful.");
	
	    try {
			threadCommListener = new HTTPThreadListener(gameMidlet, this); ///this, connector);
	
			threadCommListener.vecMsgToParse.addElement(reply);
			threadCommListener.start();
			
			gameMidlet.isConnected = true;
		}
	    catch(Exception e)
	        {
	        e.printStackTrace();
	        }
	}
	
	void readAndDispatch()
	{
		if (threadCommListener==null)	return;
		
		synchronized (threadCommListener.vecMsgToParse)
		{
			int	size = threadCommListener.vecMsgToParse.size();			
			for (int i = 0; i < size; i++)
			{
				Object objParse = threadCommListener.vecMsgToParse.elementAt(0);
				threadCommListener.vecMsgToParse.removeElementAt(0);
				
				threadCommListener.sendToParse(objParse);
			}
		}
	}
}

