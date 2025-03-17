package com.mygdx.mongojocs.toca;

public class FwdVehicle {

//	static final int SUBUSIDS_PER_USID = 128;
	
	int xp, zp;
	int xd, zd;

	int vel;
	int extraVel;
	int realVel;
	int sumAng;
	int ang, fpAng, fpViewAng, trackAng;

	int subUsid;
	int lastSubUsid;

	int usid;
	int lastUsid;
	
	int damage;
	int damageIndicator;		// >10 : Crash!
		
	int latDisplace;
	int quadStatus = 0;
		
	boolean hasBrokenWindows;	
	boolean hasCrashed;	
	boolean isBraking;
	boolean isColBack=false;
	
	int numFrames;
	int da;
	int lY;
	int llY;
	
	Track track;
	
	int curColRectXi, curColRectYi, curColRectXf, curColRectYf;
	
	boolean isColliding;
	int hitPosition;		// 0-front 1-back 2-left 3-right
	FwdVehicle collideVehicle;
	int status;
	
	int carType;

	//#ifdef MO-v3xx
	//#elifdef CARS_S40
	//#else
	int dsu = 220;
	//#endif

	boolean hintAccelerate;
	boolean hintBrake;

	boolean enculada;	
	boolean hintAcc2;
	int aiExtraVel;
	
	int lcd, ncd;	
	int fv;
	int ca;
	int dl;
	int cx;
		
	FwdVehicle(Track t) {
		track = t;
		status = 0;
	}
	
	int lastTrackAng;
	
	public void updateDirection() {
		trackAng = oMathFP.atan2(getTrackDirX(), getTrackDirY());
		
		int deltaTrack = trackAng - lastTrackAng;
		
		if (deltaTrack > oMathFP.PI || deltaTrack < -oMathFP.PI) {
			fpAng = -fpAng;
			fpViewAng = -fpViewAng;
		}
				
		fpViewAng = (fpViewAng+fpViewAng+fpViewAng + trackAng) >> 2;
		//fpAng = (fpViewAng + fpAng) >> 1;
		fpAng = (fpViewAng*7 + fpAng) >> 3; 		

		lastTrackAng = trackAng;
		llY = lY;
	}
	
	public int getXAt(int susid) {
		int usid = susid>>7;//SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = track.vertexSectionArray[(iu%vsalen)];
		int n = track.vertexSectionArray[((iu+3)%vsalen)];
		
		int t = (susid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;
	}

	public int getYAt(int susid) {
		int usid = susid>>7;//SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = track.vertexSectionArray[(iu%vsalen)+1];
		int n = track.vertexSectionArray[((iu+3)%vsalen)+1];
		
		int t = (susid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;
	}
	
	public int getZAt(int susid) {
		int usid = susid>>7;//SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = track.vertexSectionArray[(iu%vsalen)+2];
		int n = track.vertexSectionArray[((iu+3)%vsalen)+2];
		
		int t = (susid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;			// /SUBUSIDS		
	}

	
	public int getX() {
		int usid = subUsid>>7;//SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = track.vertexSectionArray[(iu%vsalen)];
		int n = track.vertexSectionArray[((iu+3)%vsalen)];
		
		int t = (subUsid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;
	}
	
	public int getY() {
		int usid = subUsid>>7; //SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = track.vertexSectionArray[(iu%vsalen)+1];
		int n = track.vertexSectionArray[((iu+3)%vsalen)+1];
		
		int t = (subUsid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;
	}
	
	public int getZ() {
		int usid = subUsid>>7; //SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;
		
		int p = track.vertexSectionArray[(iu%vsalen)+2];
		int n = track.vertexSectionArray[((iu+3)%vsalen)+2];
		
		int t = (subUsid & 0x7f); //% SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;		
	}

	
	public int getDirX() {
		return oMathFP.sin(fpViewAng);
	}

	public int getDirY() {
		return oMathFP.cos(fpViewAng);		
	}

	public int getDirZ() {
		return getTrackDirZ();
	}
	
	public int getTrackDirX() {
		int usid = subUsid>>7; //SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = track.vertexSectionArray[(iu%vsalen)];
		int n = track.vertexSectionArray[((iu+3)%vsalen)];
		
		//return (n-p);
		return getXAt(subUsid+512)-getXAt(subUsid);
	}
	
	public int getTrackDirY() {
		int usid = subUsid>>7; //SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = track.vertexSectionArray[(iu%vsalen)+1];
		int n = track.vertexSectionArray[((iu+3)%vsalen)+1];
		
		//return (n-p);
		return getYAt(subUsid+512)-getYAt(subUsid);		
	}
	
	public int getTrackDirZ() {
		int usid = subUsid>>7; //SUBUSIDS_PER_USID;
		int vsalen = track.vertexSectionArray.length;

		int iu = usid*3;
		
		int p = track.vertexSectionArray[(iu%vsalen)+2];
		int n = track.vertexSectionArray[((iu+3)%vsalen)+2];
		
		//return (n-p);
		return getZAt(subUsid+512)-getZAt(subUsid);		
	}
	
	public int getLateralDisplace() {
		return latDisplace >> 8;
	}
			
	
	public void tick(int deltaTime, int wheelRotation, boolean hasDamage) {
		tick(deltaTime, status == 1, status == -1, wheelRotation, hasDamage);
	}
	
	public void tick(int deltaTime, boolean isAccelerating, boolean isBraking, int wheelRotation, boolean hasDamage) {
		
		this.isBraking = isBraking;
		int velDam = realVel;
		
		boolean isPlayer = Game.instance.myVehicle == this;
		final int divCntDrag = 2500, divCntRR = 70;//78;
		
		usid = subUsid>>7; //SUBUSIDS_PER_USID;

		/*
		if (!isPlayer && isColliding) {
			//System.out.println(" Is colliding");
			if (collideVehicle.hitPosition == 1 && !isColBack) {
				vel >>= 2; //-= 10;
				if (isAccelerating) 	vel -=4;
				if (vel < 0) vel = 0;					
			}
			if ( realVel - 20 > collideVehicle.realVel && !isColBack) {					
				vel >>= 2; //-= 10;
				if (isAccelerating) 	vel -=4;
				if (vel < 0) vel = 0;
				//System.out.println(" REAR HIT");
			}			
		}
		else */
		if (hasDamage && isColliding && !enculada) {
			if (isPlayer) Game.instance.gc.vibraInit(250);
			
			if (hitPosition == 2) {
				// left
				//System.out.print(" HIT Left "+(isPlayer?"-M-":"")+enculada);//
				ang += 2;
				latDisplace += 25;
				isAccelerating = false;
				if (realVel - 20 > collideVehicle.realVel) {
					System.out.println(" RED ");
					collideVehicle.isColBack = true;
					vel -= 25;
					damage += 15*velDam >> 6;
					da += 15;
				}
				System.out.println("");
			}
			if (hitPosition == 3) {
				// right
				//System.out.print(" HIT Right "+(isPlayer?"-M-":"")+enculada);//
				ang -= 2;
				latDisplace -= 25;
				isAccelerating = false;
				if (realVel - 20 > collideVehicle.realVel) {
					//System.out.println(" RED ");//
					collideVehicle.isColBack = true;
					vel -= 25;
					damage += 15*velDam >> 6;
					da += 15;
				}
				//System.out.println("");//
			}
			else {
				if (hitPosition==0 ||/*lastSubUsid < collideVehicle.lastSubUsid ||*/ realVel - 20 > collideVehicle.realVel) {
					//System.out.println(" HIT Back "+(isPlayer?"-M-":"")+enculada);//
					collideVehicle.isColBack = true;
					
					vel >>= 1; //-= 10;
					if (isAccelerating) 	vel -=4;
					if (vel < 0) vel = 0;
					
					damage += 20*velDam >> 6;
					da += 20;
				}
			}
			if (vel < 0) vel = 0;
		}
				
		if (!hasCrashed && isAccelerating) {
			vel += 5;
		}
		
		if (isBraking) {
			if (vel > 2) {
				vel -=3;
			} else {
				vel = 0;
			}
		}
		
		if (wheelRotation < 0) {
			ang -= 6;
			if (ang < -18)	
				ang = -18;
		}

		if (wheelRotation > 0) {
			ang += 6;
			if (ang > 18)
				ang = 18;
		}
		
		//if (ang > 0) ang --;
		//else if (ang < 0) ang ++;
		if(ang > 0) {
			ang -= 2;
			if(ang < 0) ang = 0;
		} else if(ang < 0) {
			ang += 2;
			if(ang > 0) ang = 0;
		}
		
			if (!isPlayer) {
				if (/*(Game.instance.myVehicle.subUsid+25 < subUsid &&
					Game.instance.myVehicle.lastSubUsid+25 > lastSubUsid) ||*/
					(Game.instance.myVehicle.llY < llY && Game.instance.myVehicle.lY+curColRectYf-curColRectYi >= lY)) {
						
						//System.out.print("NP: take over "+System.currentTimeMillis());//
				
					int oxi = curColRectXi;
					int oxf = curColRectXf;
					//#ifdef CARS_S40
					//#else
					int axi = Game.instance.myVehicle.curColRectXi+5;
					int axf = Game.instance.myVehicle.curColRectXf-5;
					//#endif

					axi = Game.instance.myVehicle.curColRectXi;
					axf = Game.instance.myVehicle.curColRectXf;
					
					boolean isLeftInside = (oxi >= axi && oxi <= axf);
					boolean isRightInside = (oxf >= axi && oxf <= axf);
					
					int omx = oxi+oxf >> 1;
					int amx = axi+axf >> 1;
										
					if ((omx-amx) > 0) {
						latDisplace-=16;
					} else {
						latDisplace+=16;
					}
					
					if (isLeftInside || isRightInside) {
						if ((omx-amx) > 0) {
							latDisplace-=32;
						} else {
							latDisplace+=32;
						}
						Game.instance.myVehicle.enculada = true;	// remember this
						//System.out.println(" HIT ");//
						vel -= 15;
						vel >>= 2; //-= 10;
						if (vel < 0) vel = 0;
					}
					//System.out.println("");//
				}
			}
		
		
		if (vel > 0) {
			vel -= vel*vel/divCntDrag;
			vel -= vel/divCntRR;
		}
		else {
			vel += vel*vel/divCntDrag;
			vel -= vel/divCntRR;
		}
		
		sumAng = (sumAng + ang*ang) >> 1;
		extraVel = (200-sumAng)*vel >> 10;
		
		if (isPlayer && vel > 40 && sumAng < 13) {
			if (Game.instance.bs)
				++extraVel;
			++extraVel;
		}
		if (!isPlayer && Game.instance.myVehicle.subUsid - subUsid > 1111) {
			extraVel += 11;
		}
		
		realVel = vel + extraVel + aiExtraVel;
		
		subUsid += realVel;
		
		//marxa endarrera
		if (subUsid < 0) subUsid += track.typeSectionArray.length<<7;
		
		
		enculada = false;
		
		if (vel < 0) vel = 0;
		
		/*
		latDisplace += oMathFP.toInt(350*(-trackAng + fpAng))*(oMathFP.toInt(oMathFP.sqrt(oMathFP.toFP(vel)))<<4) >> 3; 
		fpViewAng -= oMathFP.toFP(ang)/150;
		
		fpAng -= oMathFP.toFP(ang)/500;
		*/
		
		fv = (fv+fv+fv+realVel)>>2;

		if (vel != 0) 
			latDisplace += oMathFP.toInt(359*(-trackAng + fpAng))*(oMathFP.toInt(oMathFP.log(oMathFP.toFP(vel)))<<4) >> 3;
		
		fpViewAng -= oMathFP.toFP(ang)/150;
		
		fpAng -= oMathFP.toFP(ang)>>9; // /500;

		
		if (fpViewAng < trackAng - oMathFP.PI/4) fpViewAng = trackAng - oMathFP.PI/4;
		if (fpViewAng > trackAng + oMathFP.PI/4) fpViewAng = trackAng + oMathFP.PI/4;
		

		if (vel != 0)
			latDisplace -= ang*(oMathFP.toInt(oMathFP.log(oMathFP.toFP(vel)))<<4) >> 3;
		
		
		if (latDisplace > 512*16*3) latDisplace = 512*16*3;
		if (latDisplace < -512*16*3) latDisplace = -512*16*3;
		
		
		// Track Collisions
		
		int leftLateral = 0;
		int rightLateral = 400;
		
		// get current track section		
		
		int its = ((subUsid+dsu)>>7) % track.typeSectionArray.length;
		
		int kgts = track.typeSectionArray[its];
		int kvts = track.verticalTypeSectionArray[its];
		
		TrackSection gts = track.trackSectionsMap[kgts];
		TrackSection vts = track.verticalTrackSectionsMap[kvts];
		
		
		int []patchList = gts.patchList;
		
		for (int j = 0; j < patchList.length; j+= Track.INTS_PER_PATCH) {
			int xlt = patchList[j];
			int xrt = patchList[j+1];
			int xlb = patchList[j+2];
			int xrb = patchList[j+3];
			int type = patchList[j+7];
			
			if ((type & Track.SLOWDOWN_BIT)==0)	continue;

			int xl = xlt+xlb >> 1;
			int xr = xrt+xrb >> 1;
			
			if (-latDisplace > toDisplace(xl) && -latDisplace < toDisplace(xr)) {
				vel = 7*vel >> 3;
				if (vel > 10) {
					if (hasDamage)
						++damage;	//Testers wants more grass
				}
			}										
		}		

		patchList = vts.patchList;
		
		int CAR_WIDTH = 987;
		boolean iv=false;
		
		for (int j = 0; j < patchList.length; j+= Track.INTS_PER_PATCH) {
			int xlt = patchList[j];
			int xrt = patchList[j+1];
			int xlb = patchList[j+2];
			int xrb = patchList[j+3];
			int type = patchList[j+7];
			
			int xl = xlt+xlb >> 1;
			int xr = xrt+xrb >> 1;
				
			if ((type & Track.SLOWDOWN_BIT)!=0) {
				int oxi = -toDisplace(xl);
				int oxf = -toDisplace(xr);
				
				int axi = latDisplace - CAR_WIDTH;
				int axf = latDisplace + CAR_WIDTH;
	
				boolean isLeftInside = (oxi >= axi && oxi <= axf);
				boolean isRightInside = (oxf >= axi && oxf <= axf); 
				
				if (isLeftInside || isRightInside) {
					vel = vel >> 2;
					if (hasDamage) {
						damage += 9*velDam >> 6;
						da += 9;
					}
					if (isPlayer && !iv && vel > 0) {
						iv = true;
						Game.instance.gc.vibraInit(250);
					}
				}
			}

			if ((type & Track.LEFT_BIT)!=0) {
				if (-latDisplace < toDisplace(xr) + CAR_WIDTH) {

					latDisplace = (-toDisplace(xr) - CAR_WIDTH + latDisplace) >> 1;
					
					if (hasDamage) {
						damage += 3*velDam >> 6;
						da += 5;
					}
					if (isPlayer && !iv && vel > 1) {
						if (vel > 3) vel-=3;
						iv = true;
						Game.instance.gc.vibraInit(250);
					}
				}
			}
			else if ((type & Track.RIGHT_BIT)!=0) {
				if (-latDisplace > toDisplace(xl) - CAR_WIDTH) {

					latDisplace = (-toDisplace(xl) + CAR_WIDTH + latDisplace) >> 1; 
					
					if (hasDamage) {
						damage += 3*velDam >> 6;
						da += 5;
					}
					if (isPlayer && !iv && vel > 1) {
						if (vel > 3) vel-=3;
						iv = true;
						Game.instance.gc.vibraInit(250);
					}
				}
			}
		}

		updateDirection();

		++numFrames;

		damageIndicator = damage >> 5;

//<TESTERS WANT MORE GRASS IN THEIR VEINS>
	//#ifdef CHEATS
		damageIndicator = 0;
	//#endif
//</TESTERS WANT MORE GRASS IN THEIR VEINS>

		if (!hasBrokenWindows && damageIndicator > 5) {
			if (da > 9) {
				hasBrokenWindows = true;
				da = 0;
			}
		}
		
		if (damageIndicator > 10) {
			if (!hasCrashed) {
				if (isPlayer) {
					hasCrashed = true;
					Game.instance.endRace = 1;
					if(Game.instance.nPhant != 0) {
						Game.instance.gameTickCounter = 0;
					}
					Game.instance.gc.vibraInit(350);
				}
			}
			if (isPlayer)	this.vel >>= 1;

			if (da > 10) {
				hasCrashed = true;
				damageIndicator = 10;
				da = 0;
			}
		}
		
		da = (da+da+da) >> 2;
		
		lastSubUsid = subUsid;
	}

	public int toDisplace(int value) {
		return ((value*6)*2*16)/200;
	}
		
	// depen tamany cotxe
	public int compareLeftWheel(int patchValue) {
		int v = toDisplace(patchValue);
		if (-latDisplace - 1101 < v)	return -1;
		else return 1;
	}
	
	public int compareRightWheel(int patchValue) {
		int v = toDisplace(patchValue);
		if (-latDisplace + 1101 < v)	return -1;
		else return 1;
	}
	
}
