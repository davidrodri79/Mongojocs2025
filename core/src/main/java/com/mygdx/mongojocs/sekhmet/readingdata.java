package com.mygdx.mongojocs.sekhmet;//ESTO SOLO FUNCIONA CON EL SIEMENS!
/*
InputStream is = getClass().getResourceAsStream("/data/level0.dat");
readingdata.leerdata(is,mural,(30+2)*(8+3));
*/


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.io.InputStream;

public class readingdata
{


static void readingdata(){}

static void leerdata(String is, byte[] buffer, int size)
{
	/*try{
	is.read(buffer, 0,size);
	is.close();
	}catch(Exception exception){}*/

	FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+is.substring(1));
	byte[] data = file.readBytes();

	for(int i = 0; i < size; i++)
		buffer[i] = data[i];

}

//***********************************************************************
}//end of class

	
//readingdata.leerdata("resource:data\\level1.dat",mural,(9+2)*(32+3));
//	InputStream   is= getClass().getResourceAsStream("/data.dat");
//import javax.microedition.io.Connector;
//InputStream   is=  Connector.openInputStream(str); //esta es la funcion generica
