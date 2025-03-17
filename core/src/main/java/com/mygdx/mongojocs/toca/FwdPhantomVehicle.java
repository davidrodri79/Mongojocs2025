package com.mygdx.mongojocs.toca;

public class FwdPhantomVehicle extends FwdVehicle {
	public int[] bestTrack;
	public int[] tracking;
	public int tPos;
	public int tingPos;
	public long time;
	public int rx,ry,rz,rusid,rldisp,rang;
	public int rdx,rdy,rdz;
	public int lastIndex = 0;
	public boolean isSet = false;
	
	public FwdPhantomVehicle(Track t) {
		super(t);
		//#ifdef LOWFREQ_SAMPLING
		//#else
		bestTrack = new int[300 * 7];
		tracking = new int[300 * 7];
		//#endif
		tPos = 0;
		tingPos = 0;
		carType = 0;
		isSet = false;
	}
	
	//realment es fa servir el subusid, no el usid
	public void addKey(int ms, int x, int y, int z, int usid, int lateralDisplace, int ang) {
		if(tingPos >= bestTrack.length / 7) return;
		
		tracking[tingPos * 7 + 0] = ms;
		tracking[tingPos * 7 + 1] = x;
		tracking[tingPos * 7 + 2] = y;
		tracking[tingPos * 7 + 3] = z;
		tracking[tingPos * 7 + 4] = usid;
		tracking[tingPos * 7 + 5] = lateralDisplace;
		tracking[tingPos * 7 + 6] = ang;
		
		tingPos++;
	}
	
	public void tick(int deltaTime, int wheelRotation) {}
	public void tick(int deltaTime, boolean isAccelerating, boolean isBraking, int wheelRotation) {}
		
	
	public void setBestTrack() {
		System.arraycopy(tracking, 0, bestTrack, 0, tingPos * 7);
		tPos = tingPos;
		isSet = true;
		resetTracking();
	}
	
	public void resetTracking() {
		tingPos = 0;
		rx = 0;
		ry = 0;
		rz = 0;
		rusid = 0;
		rldisp = 0;
		rang = 0;
		rdx = 0;
		rdy = 0;
		rdz = 0;
		lastIndex = 0;
	}
	
	public void setCurrentTime(long ms) {
		int prevTime;
		int nextTime;
		int maxmin;
		int factor;
		
		time = ms;
		if(time < 0) time = 0;
		
		if(tPos == 0) return;
		
		int i = 0;
		while(i < tPos && bestTrack[i * 7 + 0] < time) {
			i++;
		}
		
		if(i >= tPos) return;
		
		if(i > 0) i--;
		
		lastIndex = i;
		prevTime = bestTrack[lastIndex * 7 + 0];
		nextTime = bestTrack[(lastIndex + 1) * 7 + 0];
		maxmin = nextTime - prevTime;
		if(maxmin == 0) maxmin = 1;
		if(maxmin < 0) { 
			//System.out.println("temps negatiu: " + maxmin + "nt: " +nextTime + " pv: " + prevTime + " time: " + time);
		}
		
		factor = (int) ((time - prevTime) << 12) / maxmin;
		int k = lastIndex * 7;
		if(k < 0) k = 0;
		
		if(factor < 0) return;
			
		//faltaria interpolar.. o no :U_

		rx = ((bestTrack[k + 1 + 7] - bestTrack[k + 1]) * factor + (bestTrack[k + 1] << 12)) >> 12;
		ry = ((bestTrack[k + 2 + 7] - bestTrack[k + 2]) * factor + (bestTrack[k + 2] << 12)) >> 12;
		rz = ((bestTrack[k + 3 + 7] - bestTrack[k + 3]) * factor + (bestTrack[k + 3] << 12)) >> 12;
		rusid = ((bestTrack[k + 4 + 7] - bestTrack[k + 4]) * factor + (bestTrack[k + 4] << 12)) >> 12;
		rldisp = ((bestTrack[k + 5 + 7] - bestTrack[k + 5]) * factor + (bestTrack[k + 5] << 12)) >> 12;
		rang = ((bestTrack[k + 6 + 7] - bestTrack[k + 6]) * factor + (bestTrack[k + 6] << 12)) >> 12;
		
		rdx = bestTrack[k + 1 + 7] - bestTrack[k + 1];
		rdy = bestTrack[k + 2 + 7] - bestTrack[k + 2];
		rdz = bestTrack[k + 3 + 7] - bestTrack[k + 3];
		subUsid = rusid;
		//System.out.println("phantom subusid: " + subUsid + "::"+bestTrack[k+4+7] +","+bestTrack[k+4]);
		usid = subUsid >> 7;
		
		//System.out.println("phantom usid: " + usid);
		//System.out.println("time: " + time + " [" + bestTrack[k + 0] + "," + bestTrack[k + 7 + 0] + "] :: " + (( 100 * factor) >> 16));
	}
	
	public int getX() {
		return rx;
	}
	
	public int getY() {
		return ry;
	}
	
	public int getZ() {
		return rz;
	}
	
	public int getDirX() {
		return rdx;
	}
	
	public int getDirY() {
		return rdy;
	}
	
	public int getDirZ() {
		return rdz;
	}
	
	public int getLateralDisplace() {
		return rldisp;
	}
}
