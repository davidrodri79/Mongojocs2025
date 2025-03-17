package com.mygdx.mongojocs.clubfootball2006;/////////////////////////////////////////////////////////////////////////////
// Basado en
// Fatcat - A massive multiplayer online game platform
// Copyright Microjocs Mobile S.L. 2004
/////////////////////////////////////////////////////////////////////////////



//#ifdef J2ME
import java.io.*;
//#endif


//#ifdef DOJA


import java.io.InputStream;			// clase GENERICA (soportada por TODOS los devices)
//#endif



/**
 *  Client connection to the Fatcat platform
 *
 * @author    ccm
 */
public class ConnectionHTTP extends Thread
{
	public byte[] result = null;

	public boolean finished = false;

	private String url;
	private byte[] data;

	/**
	 *  Constructor for the FatcatConnectionHTTP object
	 *
	 * @param  u  URL for the remote Fatcat server
	 */
	public ConnectionHTTP(String u, byte[] in) {
		url = u;
		data = in;
		start();
	}


	public void run() {
		try {
			result = post(data);
		} catch (Exception e)
		{
		//#ifdef com.mygdx.mongojocs.clubfootball2006.com.mygdx.mongojocs.sanfermines2006.Debug
			Debug.println("HTTP_Error:");
			Debug.println(e.toString());
			e.printStackTrace();
		//#endif
			result = null;
		}

		finished = true;
	}


	/**
	 *  Public method
	 *
	 * @exception  IOException  Description of the Exception
	 */
	public byte[] post(byte[] data) throws IOException {
		byte[] res = null;
		/*HttpConnection c = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			c = (HttpConnection)Connector.open(url);
			c.setRequestProperty("Connection", "close");
			c.setRequestMethod(HttpConnection.POST);
			c.setRequestProperty("Content-Type", "application/mr2");

			// Set the POST data
			// nota -> a partir de open output stream no se puede llamar a metodos que impliquen
			// cambiar los headers. en el wtk rula pero en el resto del trastos peta.

			os = c.openOutputStream();

			// Si ya estamos logados; lo primero que hay que enviar es la cookie y el
			// ultimo server time

			os.write(data);

			os.flush();

			os.close();
			os = null;

		//#ifdef DOJA
			c.connect();
		//#endif
			// Get the status code, causing the connection to be made
			int status = c.getResponseCode();
			// Only HTTP_OK (200) means the content is returned.
			if (status != HttpConnection.HTTP_OK) {
				throw new IOException("Response status not OK [" + status + "]");
			}

			// Agregar la respuesta al buffer de recepcion
			is = c.openInputStream();
			ByteArrayOutputStream tos = new ByteArrayOutputStream();
			try {
				int b = 0;
				while (b != -1) {
					b = is.read();
					if (b != -1) {
						tos.write(b);
					}
				}

				tos.flush();
				
				res = tos.toByteArray();
				
				tos.close();
				tos = null;
			} finally {
				if (tos != null) {
					tos.close();
				}
			}
			
			is.close();
			c.close();
			is = null;
			c = null;
		} finally {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
			if (c != null) {
				c.close();
			}
			
			System.gc();
		}*/

		return res;
	}
}


