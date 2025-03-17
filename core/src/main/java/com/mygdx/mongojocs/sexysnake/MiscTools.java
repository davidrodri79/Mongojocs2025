package com.mygdx.mongojocs.sexysnake;

public class MiscTools {
	
	private static int rndCont = 0;
	private static int[] rndData = new int[] {0x1A45BCD1,0x529ACF6E,0xF37A54C9,0x203FC464,0x31F6B52D};
	
	public static int random(int max) {
		rndData[rndCont % 5] ^= rndData[++rndCont % 5];
		if(rndCont > 23) {
			rndCont = 0;
		}
		return(((rndData[rndCont % 5] >> rndCont) & 0xff) * max) >> 8;
	}
}
