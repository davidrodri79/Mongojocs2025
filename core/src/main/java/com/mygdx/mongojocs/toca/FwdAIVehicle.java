package com.mygdx.mongojocs.toca;

public class FwdAIVehicle extends FwdVehicle {

	int lastRandLerpedL, lastRandLerpedR;
	
	int velMin = 50;
	int velMinAccel = 0;
	int offAng=0;
		
	FwdAIVehicle(Track t) {
		super(t);
	}

	FwdAIVehicle(Track t, int initialSubUsid, int minvel) {
		this(t);
		subUsid = initialSubUsid;
		velMin = minvel - 33;
		velMinAccel = (Game.instance.rand.nextInt() & 0x1f) + 28;
		offAng = (Game.instance.rand.nextInt() & 0x1f) - 0x0f;
	}
		
	public void tickAI(int deltaTime, boolean hasDamage) {
		boolean isAccelerating = false;
		boolean isBraking = false;
		
		int wheelRotation = 0;
		
		int rvl = (Game.instance.rand.nextInt() & 0x1ff) - 256;
		int rvr = (Game.instance.rand.nextInt() & 0x1ff) - 256;
		
		lastRandLerpedL = lastRandLerpedL + rvl >> 1;
		lastRandLerpedR = lastRandLerpedL + rvr >> 1;
		
		if (vel < velMin/*120*/) {
			isAccelerating = true;
		}
				
		if (trackAng - fpAng + offAng > 500+lastRandLerpedL) {
			if (vel > velMin>>1)
				isAccelerating = (rvl & 1)==0;
			wheelRotation = -1;
		}
		else if (trackAng - fpAng + offAng < -500+lastRandLerpedR) {
			if (vel > velMin>>1)
				isAccelerating = (rvl & 1)==0;
			wheelRotation = 1;
		}

		if (latDisplace > lastRandLerpedL+512*16/2) {
			wheelRotation = 1;
		}
		else if (latDisplace < lastRandLerpedR-512*16/2) {
			wheelRotation = -1;
		}
		
		if (vel < velMinAccel) {
			isAccelerating = true;
		}
		
		aiExtraVel = 0;
		if (hintAccelerate) {
			isAccelerating = true;
			//aiExtraVel = 3+Game.getInstance().incAiSpeed[Game.getInstance().currentTrackNum];
			aiExtraVel += Game.instance.incAiSpeed[Game.instance.currentTrackNum];
			
			hintAccelerate = false;
		}
		if (hintAcc2) {
			++aiExtraVel;			
			hintAcc2 = false;
		}
		if (hintBrake) {
			isBraking = true;
			aiExtraVel += -4;
			
			hintBrake = false;
		}
		
		//isColliding = false;
		
		aiExtraVel += Game.instance.incAiSpeed[Game.instance.currentTrackNum];
		
		tick(deltaTime, isAccelerating, isBraking, wheelRotation, hasDamage);
	}	
}
