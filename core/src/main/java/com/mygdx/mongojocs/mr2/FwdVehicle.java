package com.mygdx.mongojocs.mr2;

public class FwdVehicle {
	//DOJAPORT SUCKS
	public static final int MIN_TORQUE = 6 << 15;		//6
	public static final int GRASS_RESISTANCE = 10;
	public static final int ITEM_POWER_UP = 5 << 15;	//5
	public static final int POWER_DURATION_FACTOR = GameCanvas.ONE >> 4;
	public static final int LAT_FACTOR = GameCanvas.PI >> 1;
	public static final int LAT_FACTOR_180 = LAT_FACTOR/180; // 2 / 3
	
	public static final int PIDiv2 = GameCanvas.PI >> 1;
	//#ifdef BIG_GRASS
	//#else
	public static int LAT_BORDER = 34000;
	//#endif
	
	public int lastGameMode = -1;
	public boolean restoreGM = false;
	public boolean lateralFall = false;
	public boolean collision = false;
	public boolean forceBrake = false;
	public int fallFrame = 0;
	public boolean slowDown = false;
	public int fov = 0;
	public int usid = 0;
	public int subUsid = 0;
	public int ang = 0;
	public int status = 0;
	public int wheelRotation = 0;
	public int last_accel = 0;
	public boolean boost = false;
	public boolean boosted = false;
	public boolean boostMode = false;
	public boolean haltBoost = false;
	public boolean crashSound = false;
	public int boostTick = 0;
	public int lastGM = 0;
	public int maxRollTicks = 0;
	public int flash = 0;
	public int vel = 0;
	public int latDisplace = 0;
	public boolean smoke = false;
	public int dirX = 0;
	public int dirY = 0;
	public int dirZ = 0;
	public int roll = 0;
	public int accel = 0;
	public int fpViewAng = 0;
	public int divCntDrag = 4000;
	public final int divCntRR = 100;
	public int torque = MIN_TORQUE;
	public int iTime = -1;
	public	int iAng = 0;
	public int rollDeg = 0;
	public int lastTrackAng = 0;
	
	public int old_roll = 0;
	public int old_subusid = 0;
	public int old_vel = 0;
	public int old_ang = 0;
	public int old_latdisplace = 0;
	public int old_dirx = 0;
	public int old_diry = 0;
	public int old_dirz = 0;
	public int old_fpviewang = 0;
	
	public int w_roll = 0;
	public int w_subusid = 0;
	public int w_vel = 0;
	public int w_ang = 0;
	public int w_latdisplace = 0;
	public int w_dirx = 0;
	public int w_diry = 0;
	public int w_dirz = 0;
	public int w_fpviewang = 0;
	
	public int colRoll = 0;
	public int colX = 0;
	
	public int tRow = 0;
	
	public int PIDiv180=GameCanvas.PI/180;
	
	public boolean isPhantom = false;
	public short[] phantom_bestTrack;
	public short[] phantom_tracking;
	public short[] phantom_phantom;
	public int phantom_phantomTime;
	public int phantom_tPos;
	public int phantom_tingPos;
	public long phantom_bestTime;
	public int phantom_lastIndex = 0;
	public boolean phantom_isSet = false;
	public boolean phantom_isBest = false;
	public boolean phantom_stillRunning = true;
	public int phantom_c_subusid;
	public int phantom_n_subusid;
	public int phantom_c_latdisplace;
	public int phantom_n_latdisplace;
	public int phantom_tick;
	public GameCanvas gc;
	public static final int MAX_PHANTOM_TICKS = 5;
	
	public FwdVehicle(boolean instanceIsPhantom) {
		isPhantom = instanceIsPhantom;
		gc = GameCanvas.instance;
		
		if(isPhantom) {
			//DOJAPORT
			
			phantom_bestTrack = new short[1001];	//500 * 2 + 1
			phantom_tracking = new short[1001];	//500 * 2 + 1
			phantom_phantom = new short[1001];	//500 * 2 + 1
			
			phantom_tPos = 0;
			phantom_tingPos = 0;
			phantom_isSet = false;
			phantom_isBest = false;
		}
	}
	
	public void saveState() {
		old_roll = roll;
		old_subusid = subUsid;
		old_vel = vel;
		old_ang = ang;
		old_latdisplace = latDisplace;
		old_dirx = dirX;
		old_diry = dirY;
		old_dirz = dirZ;
		old_fpviewang = fpViewAng;
	}
	
	public void adjustVars(int alpha) {
		if(isPhantom) {
			if(phantom_isSet && phantom_stillRunning && gc.lapCounter > 0) {
				w_subusid     = gc.mul(phantom_n_subusid - phantom_c_subusid, alpha) + phantom_c_subusid;
				w_latdisplace = gc.mul(phantom_n_latdisplace - phantom_c_latdisplace, alpha) + phantom_c_latdisplace;
				usid = w_subusid >> 7;
				w_roll = 0;
			}
		} else {
			int invAlpha = gc.ONE - alpha;
			
			//f = a+t*(b-a);
			w_roll = gc.mul(roll - old_roll, alpha) + old_roll;
			w_vel = gc.mul(vel - old_vel, alpha) + old_vel;
			w_ang = gc.mul(ang - old_ang, alpha) + old_ang;
			
			if(fpViewAng - old_fpviewang > gc.PI || fpViewAng - old_fpviewang < -GameCanvas.PI) {
				w_fpviewang = gc.mul(-fpViewAng - old_fpviewang, alpha) + old_fpviewang;
			} else {
				w_fpviewang = gc.mul(fpViewAng - old_fpviewang, alpha) + old_fpviewang;
			}
			
			w_latdisplace = (((latDisplace - old_latdisplace) * alpha) >> 15) + old_latdisplace;
			w_subusid = (((subUsid - old_subusid) * alpha) >> 15) + old_subusid;;
			w_dirx = (((dirX - old_dirx) * alpha) >> 15) + old_dirx;
			w_diry = (((dirY - old_diry) * alpha) >> 15) + old_diry;
			w_dirz = (((dirZ - old_dirz) * alpha) >> 15) + old_dirz;
		
			usid = w_subusid >> 7;
		}
	}
	
	public void init() {
		flash = 0;
		subUsid = (gc.track_typeSectionArray.length - 7) << 7;
		usid = subUsid >> 7;	//!!
		forceBrake = false;
		resetDirection();
	}
	
	public void resetDirection() {
		dirX = getTrackDirXYZ((byte)(0));
		dirY = getTrackDirXYZ((byte)(1));
		dirZ = getTrackDirXYZ((byte)(2));
		int mod = gc.sqrt(gc.mul(dirX,dirX) + gc.mul(dirY,dirY));
		if(mod != 0) {
			int invMod = gc.div(GameCanvas.ONE, mod);
			
			dirX = gc.mul(dirX, invMod);
			dirY = gc.mul(dirY, invMod);
		}
		
		ang = gc.atan2(dirX, dirY);
		//ang = myFastAtan2(dirX, dirY);
		fpViewAng = ang;
	}
	
	//retorna amb quin usid ha de col.lisionar, hi ha diferencies segons si es primera persona o 3era
	public int getRealUsid(int gameMode) {
		lastGM = gameMode;
		int m = 640 + 64 + w_subusid;				//5 * 128
		if(gameMode == 0) m += 128;		//6 * 128
		return m >> 7;
	//DOJAPORT hacked by rai
		/*lastGM = gameMode;
		if(gameMode == 0) {
			return (w_subusid + 6 * 128 + 64) >> 7;	// + 7.5
		} else {
			return (w_subusid + 5 * 128 + 64) >> 7;	// + 6.5
		}*/
	}
	
	public void fastTick() {
		if(boostMode) {
			boostTick++;
			if(boostTick > 20) boostTick = 20;
		} else {
			boostTick--;
			if(boostTick < 0) boostTick = 0;
		}
		
		// DOJAPORT
		/*if ((lateralFall) || (collision)) {
			wheelRotation = 0;
			status = -1;
			if (lateralFall) fallFrame--; else fallFrame++;
			boosted = false;
			torque = MIN_TORQUE;
			// DOJAPORT
			if (
					((lateralFall)	&&	(fallFrame == -9)) ||
					((collision) 		&& 	(fallFrame == 36))
				 )
			{		//8 mes per que la moto estigui una estona parada
				fallFrame = 0;
				lateralFall = false;
				latDisplace = 0;
				roll = 0;
				restoreGM = true;
				vel = 0;
				//resetDirection();
			}
		}
		*/
		if(lateralFall) {
			wheelRotation = 0;
			status = -1;
			fallFrame--;
			boosted = false;
			torque = MIN_TORQUE;
			if(fallFrame == -1 - 8) {		//8 mes per que la moto estigui una estona parada
				fallFrame = 0;
				lateralFall = false;
				latDisplace = 0;
				roll = 0;
				restoreGM = true;
				vel = 0;
				//resetDirection();
			}
		}
		
		if(collision) {
			wheelRotation = 0;
			status = -1;
			fallFrame++;
			boosted = false;
			torque = MIN_TORQUE;
			if(fallFrame == 28 + 8) {		//8 mes per que la moto estigui una estona parada
				fallFrame = 0;
				collision = false;
				latDisplace = 0;
				roll = 0;
				restoreGM = true;
				vel = 0;
				//resetDirection();
			}
		}
	}

	public int[] rollFactor = {1,5};
	public void timedTick() {
		
		if(vel != 0) {
			iAng = wheelRotation * 2;
			tRow++;
			if(tRow > 1) tRow = 1;
		} else {
			iAng = 0;
		}
		
		roll += iAng * PIDiv180 * rollFactor[tRow];
		
		if(iAng == 0) tRow = 0;
		
		if(roll > 11438) roll = 11438;		//20�
		if(roll < -11438) roll = -11438;	//20�
		
		int maxRollCheck = Math.abs(roll);
		if(!lateralFall && maxRollCheck > 11000) {
			maxRollTicks++;
		} else {
			maxRollTicks = 0;
		}
		
		smoke = false;
		if(maxRollTicks > 10 && !collision) {
			smoke = true;
		}
		
		if(maxRollTicks > 14 && !collision) {		//70 / 5 = 14
			//DOJAPORT
			haltBoost = lateralFall = crashSound = true;
			colRoll = roll;
			maxRollTicks = 0;
			//vel = 0;
			boosted = boost = false;
			fallFrame = 14;
			return;
		}
		
		//rollDeg = GameCanvas.instance.toInt(GameCanvas.instance.div(GameCanvas.instance.mul(roll,GameCanvas.instance.toFP(180)), GameCanvas.instance.PI));
		//rollDeg = GameCanvas.instance.toInt(GameCanvas.instance.div(roll*180, GameCanvas.instance.PI));
		rollDeg = 180*roll/GameCanvas.PI;
		
		
		divCntDrag = 4000 - Math.abs(rollDeg << 5);
		int resistance = gc.mul(vel, vel) / divCntDrag + vel / divCntRR;
		
		if(slowDown) {
			resistance += vel / GRASS_RESISTANCE; 
		}
		
		//blue powered pill
		if(boost) {
			status = 1;
			torque = MIN_TORQUE + ITEM_POWER_UP;
			boost = false;
		}
		
		if(torque > MIN_TORQUE) {
			torque = torque - POWER_DURATION_FACTOR;
			//boosted = true;
		} else {
			torque = MIN_TORQUE;
			boosted = false;
		}
		
		accel = 0;
		if(status == 1) {
			accel = torque - resistance;
		}
		
		if(status == -1) {
			accel = -(MIN_TORQUE >> 2) - resistance;
			smoke = true;
		}
		
		if(lateralFall || collision || forceBrake) accel -= (MIN_TORQUE >> 2);
		
		subUsid = subUsid + ((3 * vel >> 16));  //>>15*2
		vel += (accel >> 1) * 4;		//5
		
		//set new position
		usid = subUsid >> 7;
		
		int dx = getTrackDirXYZ((byte)(0));
		int dy = getTrackDirXYZ((byte)(1));
		ang = gc.atan2(dx, dy);
		//ang = myFastAtan2(dx, dy);
		
		int deltaTrack = ang - fpViewAng;
		if (deltaTrack > GameCanvas.PI || deltaTrack < -GameCanvas.PI) {
			if(ang < 0) {
				fpViewAng = (fpViewAng + fpViewAng + fpViewAng + ang + (GameCanvas.PI << 1)) >> 2;
				deltaTrack += (GameCanvas.PI << 1);
			} else {
				fpViewAng = fpViewAng + (GameCanvas.PI << 1);
				fpViewAng = (fpViewAng + fpViewAng + fpViewAng + ang) >> 2;
				deltaTrack -= (GameCanvas.PI << 1);
			}
			
			if(fpViewAng > GameCanvas.PI) fpViewAng -= (GameCanvas.PI << 1);
			if(fpViewAng < -GameCanvas.PI) fpViewAng += (GameCanvas.PI << 1);
		} else {
			fpViewAng = (fpViewAng + fpViewAng + fpViewAng + ang) >> 2;
		}
		lastTrackAng = ang;
		
		dirX = gc.sin(fpViewAng);
		dirY = gc.cos(fpViewAng);
		
		if(vel < 0) vel = 0;
		if (vel != 0) {
			int vi = (vel >> 15);
			latDisplace -= (9 * ((rollDeg << 7) * vi) >> 6) / 5;	///3 / 2
			int fc = (15 * (((vi*vi) >> 6) * deltaTrack / LAT_FACTOR_180)) >> 4; // * 7 i / 8 per fer-lo mes facil o_O (indignacio de testers)
			//int fc = (((vi*vi) >> 6) * deltaTrack / LAT_FACTOR_180);
			
			//si es poc despla�ament no despla�ar
			if((fc < -1500) || (fc > 1500)) {
				latDisplace -= fc;
			}
			
			//latDisplace = 0;
		}
		//DOJAPORT
		/*
		if(latDisplace > 512*16*5) latDisplace = 512*16*5;
		if(latDisplace < -512*16*5) latDisplace = -512*16*5;*/
		if(latDisplace > 40960) latDisplace = 40960;
		if(latDisplace < -40960) latDisplace = -40960;
		
		if(roll > 0) {
			//DOJAPORT 220*8
			roll -= 1760;
			if(roll < 0) roll = 0;
		}
		
		if(roll < 0) {
			//DOJAPORT 220*8
			roll += 1760;
			if(roll > 0) roll = 0;
		}

		int vsid = (getRealUsid(lastGM) - 4) % gc.track_typeSectionArray.length;
		int vsido = (getRealUsid(lastGM) - 5) % gc.track_typeSectionArray.length;
		
		int kvts = gc.track_verticalTypeSectionArray[vsid];
		int kvtso = gc.track_verticalTypeSectionArray[vsido];
		
		//TrackSection vts = gc.track_verticalTrackSectionsMap[kvts];
		//TrackSection vtso = gc.track_verticalTrackSectionsMap[kvtso];
		
		slowDown = false;
		
		int typeAcc = 0;
		int[] patchList = gc.track_verticalTrackSectionsMap_patchList[kvtso];
		for (int j = 0; j < patchList.length; j+= gc.TRACK_INTS_PER_PATCH) {
			int type = patchList[j + 7];
			if ((type & gc.TRACK_SLOWDOWN_BIT) != 0) typeAcc++;
		}
		
		patchList = gc.track_verticalTrackSectionsMap_patchList[kvts];
		for (int j = 0; j < patchList.length; j+= gc.TRACK_INTS_PER_PATCH) {
			int type = patchList[j + 7];
			if ((type & gc.TRACK_SLOWDOWN_BIT) != 0) typeAcc++;
		}
		
		//#ifdef CHEATS
		//if(!gc.cheatsEnabled) {
		//if (-latDisplace < -LAT_BORDER || -latDisplace > LAT_BORDER) {
		//#else
		if (-latDisplace < -LAT_BORDER || -latDisplace > LAT_BORDER) {
		//#endif
			if(typeAcc > 0) {
				//#ifndef NO_COLLISION
				if(collision == false && lateralFall == false) {
					//DOJAPORT
					collision = crashSound = haltBoost = true;
					boost = boosted = false;
					fallFrame = 0;
					colRoll = roll;
					flash = 0xFFFFFF;
				}
				//#else
				//#endif
			} else {
				slowDown = true;
			}
		}
		//#ifdef CHEATS
		//}
		//#endif
	}
	
	public int getXYZ(byte D)
	{
		int w_subusid_temp = w_subusid;
		if(isPhantom) w_subusid_temp += 512;
		return getXYZAt(D,w_subusid_temp);
		/*{ 
			//DOJAPORT (4 << 7)
			return getXYZAt(D,w_subusid + 512);
		} else {
			return getXYZAt(D,w_subusid);
		}*/
	}
	//DOJAPORT
	/*
	public int getX() {
		if(isPhantom) {
			//DOJAPORT (4 << 7)
			return getXYZAt((byte)(0),w_subusid + 512);
		} else {
			return getXYZAt((byte)(0),w_subusid);
		}
	}
	
	public int getY() {
		if(isPhantom) {
			//DOJAPORT (4 << 7)
			return getXYZAt((byte)(1),w_subusid + 512);
		} else {
			return getXYZAt((byte)(1),w_subusid);
		}
	}
	
	public int getZ() {
		if(isPhantom) {
			//DOJAPORT (4 << 7)
			return getXYZAt((byte)(2),w_subusid + 512);
		} else {
			return getXYZAt((byte)(2),w_subusid);
		}
	}*/
	
	public int getDirX() {
		return w_dirx;
	}
	
	public int getDirY() {
		return w_diry;
	}
	
	public int getDirZ() {
		return w_dirz;
	}
	
	//DOJAPORT
	/*
	protected int getXAt(int susid) {
		int usid = susid>>7;//SUBUSIDS_PER_USID;
		int vsalen = gc.track_vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = gc.track_vertexSectionArray[(iu%vsalen)];
		int n = gc.track_vertexSectionArray[((iu+3)%vsalen)];
		
		int t = (susid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;
	}

	protected int getYAt(int susid) {
		int usid = susid>>7;//SUBUSIDS_PER_USID;
		int vsalen = gc.track_vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = gc.track_vertexSectionArray[(iu%vsalen)+1];
		int n = gc.track_vertexSectionArray[((iu+3)%vsalen)+1];
		
		int t = (susid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;
	}
	
	protected int getZAt(int susid) {
		int usid = susid>>7;//SUBUSIDS_PER_USID;
		int vsalen = gc.track_vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = gc.track_vertexSectionArray[(iu%vsalen)+2];
		int n = gc.track_vertexSectionArray[((iu+3)%vsalen)+2];
		
		int t = (susid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;			// /SUBUSIDS		
	}
*/	
	protected int getXYZAt(byte D, int susid) //D = Direccion 0 -> X, 1 -> Y, 2 -> Z
	{
		int usid = susid>>7;//SUBUSIDS_PER_USID;
		int vsalen = gc.track_vertexSectionArray.length;

		int	iu = usid*3;		
		
		int p = gc.track_vertexSectionArray[(iu%vsalen)+D];
		int n = gc.track_vertexSectionArray[((iu+3)%vsalen)+D];
		
		int t = (susid & 0x7f); // % SUBUSIDS_PER_USID);
		
		return (((n-p)*t)>>7) + p;			// /SUBUSIDS		
	}
	
	public int getTrackDirXYZ(byte D) { return getXYZAt(D,w_subusid+256)-getXYZAt(D,w_subusid); }
	/*public int getTrackDirXYZ((byte)(0)) {
		return getXYZAt((byte)(0),w_subusid+256)-getXYZAt((byte)(0),w_subusid);
	}
	
	public int getTrackDirXYZ((byte)(1)) {
		return getXYZAt((byte)(1),w_subusid+256)-getXYZAt((byte)(1),w_subusid);
	}
	
	public int getTrackDirXYZ((byte)(2)) {
		return getXYZAt((byte)(2),w_subusid+256)-getXYZAt((byte)(2),w_subusid);
	}*/
	
	//DOJAPORT
	/*public int getLateralDisplace() {
		return w_latdisplace >> 8;
	}*/
	
/*	public int getLateralPos() {
		//DOJAPORT
		//int ld = getLateralDisplace();
		//int TempVar = ld >> 4;
		//int rd = (ld >> 4) & 1;
		//int rd = TempVar & 1;
		//sembla no funcionar pas millor
		return -(ld >> 4);	//5
		//return -TempVar;	//5
	}*/

	//DOJAPORT
	/*
	//copy paste permuta from toca
	public int toDisplace(int value) {
		//return ((value*6)*2*16*2)/150;
		//return ((6*2*16*2)*value)/150;
		//DOJAPORT
		return (384*value)/150;
	}*/
	
	
	//phantom
	public void phantom_addKey(int subusid, int lateralDisplace) {
		//System.out.println("adding key: " + ms + ", " + subusid + ", " + lateralDisplace);  
		if(phantom_tingPos >= phantom_bestTrack.length / 2) return;
		phantom_tracking[phantom_tingPos * 2     + 1] = (short) ((subusid % ((gc.track_vertexSectionArray.length / 3) << 7)) >> 8);
		phantom_tracking[phantom_tingPos * 2 + 1 + 1] = (short) (lateralDisplace >> 8);
		phantom_tingPos++;
	}
	
	
	public void phantom_setPhantomTrack(int time) {
		System.arraycopy(phantom_tracking, 0, phantom_phantom, 0, phantom_tracking.length);
		phantom_tPos = phantom_tracking[0];
		phantom_phantomTime = time;
		phantom_isSet = true;
		phantom_resetTracking();
	}
	
	public void phantom_setBestTrack(int time) {
		phantom_tracking[0] = (short) phantom_tingPos;
		System.arraycopy(phantom_tracking, 0, phantom_bestTrack, 0, phantom_tracking.length);	// tingPos * 2 + 1);
		phantom_tPos = phantom_tracking[0];
		// DOJAPORT
		phantom_isSet = phantom_isBest = true;
		phantom_bestTime = time;
		phantom_resetTracking();
	}
	
	public void phantom_resetTracking() {
		phantom_tingPos = phantom_lastIndex = 0;
		phantom_tick = -1;
		phantom_stillRunning = gc.forceTick = true;
		phantom_incTickBase();
	}
	
	//falten comprovacions de seguretat :U_ arrayindexoutofbounds i tal
	public int phantom_getState(int tick, int var) {
		int index = tick / MAX_PHANTOM_TICKS;
		int rmdr = tick % MAX_PHANTOM_TICKS;
		int maskShift = 0;
		int mask = 0;
		int shiftAfter = 0;
		int out = 0;
		short getter[] = phantom_phantom;
		
		if(phantom_isBest) {
			getter = phantom_bestTrack;
		}
		
		if(!phantom_stillRunning) return 0;
		
		if(index > phantom_tPos) {
			//#ifdef com.mygdx.mongojocs.mr2.Debug
			System.out.println("out of bounds");
			//#endif
			phantom_stillRunning = false;
			return 0;
		}
		
		if(rmdr == 0) {
			return getter[index * 2 + var + 1] << 8; 
		} else {
			int k0 = getter[ index      * 2 + var + 1] << 8;
			int k1 = getter[(index + 1) * 2 + var + 1] << 8;
			
			return ((rmdr * (k1 - k0)) / MAX_PHANTOM_TICKS) + k0;
		}
	}
	
	public void phantom_incTickBase() {
		if(phantom_isSet) {
			phantom_tick++;
			if((phantom_tick / MAX_PHANTOM_TICKS) >= phantom_tPos - 1) phantom_stillRunning = false;
			phantom_c_subusid = phantom_getState(phantom_tick    , 0);
			phantom_n_subusid = phantom_getState(phantom_tick + 1, 0);
			phantom_c_latdisplace = phantom_getState(phantom_tick    , 1);
			phantom_n_latdisplace = phantom_getState(phantom_tick + 1, 1);
		}
	}
}
