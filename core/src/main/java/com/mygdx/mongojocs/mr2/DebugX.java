package com.mygdx.mongojocs.mr2;//#ifdef com.mygdx.mongojocs.mr2.Debug
import com.mygdx.mongojocs.midletemu.Font;
import com.mygdx.mongojocs.midletemu.Graphics;

import java.io.*;
//#ifdef J2ME

//#endif

public class DebugX {
	public static String versionStr = null;
	public static DebugX instance = new DebugX();
	public static int fps = 0;
	public static long frame = 0;
	public static int buildNumber = 0;
	public static String custom = "";
	
	public DebugX() {
		frame = 0;
	}
	
	public static void setCustom(String cu) {
		custom = cu;
	}
	
	public static DebugX getInstance() {
		return instance;
	}
	
	public static void println(String str) {
		System.out.println(str);
	}
	
	public void initVersion() {
		if(versionStr == null) {
			ByteArrayOutputStream baos = null;
			try {
				InputStream is = getClass().getResourceAsStream("/build.txt");
				DataInputStream dis = new DataInputStream(is);
				
				baos = new ByteArrayOutputStream();
				int br = -1;
				//skip bytes until we find and =
				while(br != 0x3D) {
					br = dis.readUnsignedByte();
				}
				br = -1;
				while(br != 0x0A) {
					br = dis.readUnsignedByte();
					if(br != 0x0A) {
						br = br - '0';
						//System.out.println(br);
						baos.write(br);
					}
				}
				
				int k = 1;
				int total = 0;
				byte[] num = baos.toByteArray();
				for(int i = 0; i < num.length; i++) {
					total += num[num.length - 1 - i] * k;
					k *= 10;
				}
				
				buildNumber = total;
				versionStr = String.valueOf(total);
			} catch(Exception e) {}
		}
		frame = 0;
	}
	
	public void drawVersion(Graphics g, int cW, int cH) {
		//#ifdef J2ME
		g.setClip(0,0,cW,cH);
		g.setFont(Font.getFont( Font.FACE_PROPORTIONAL , Font.STYLE_PLAIN, Font.SIZE_SMALL));
		g.setColor(0x000000);
		g.drawString("V.  : " + versionStr, 8, cH - 48, 20);
		g.drawString("FPS : " + String.valueOf(fps), 8, cH - 36, 20);
		g.drawString("frame : " + String.valueOf(frame), 8, cH - 24, 20);
		g.drawString("userid : " + custom, 8, cH - 12, 20);
		//#endif
		frame++;
	}
}
//#endif