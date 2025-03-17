package com.mygdx.mongojocs.toca;

public class Track {

	static final int CENTRAL_BIT = 0x01;
	static final int LEFT_BIT = 0x02;
	static final int RIGHT_BIT = 0x04;
	static final int SPEEDUP_BIT = 0x08;
	static final int SLOWDOWN_BIT = 0x10;
	static final int SKID_BIT = 0x20;

	static final int INTS_PER_PATCH = 9;
	static final int INTS_PER_LINE = 6;
	
	protected int[] arrayVxy;
	protected byte[] arrayAlt;
	
	protected int[] arrayLong;
	
	public int[] vertexSectionArray;
	public byte[] typeSectionArray;
	public byte[] verticalTypeSectionArray;
	
	public int shadeColor;
	public int shadeLerpFactor=32;

	public TrackSection[] trackSectionsMap;
	public TrackSection[] verticalTrackSectionsMap;

	Track() {
		arrayVxy = new int[0];
		arrayAlt = new byte[0];		
	}
	
	Track(int[] avxy, byte[] aalt) {
		arrayVxy = avxy;
		arrayAlt = aalt;
	}

	public int getNumOfPoints() {
		return arrayVxy.length >> 1;
	}

	public int getNumOfSegments() {
		return (getNumOfPoints() - 1) / 3;
	}

	public int getPointPosX(int psg) {
		int numsg  = arrayVxy.length >> 1;

		int index  = (psg % numsg) << 1;
		return arrayVxy[index];
	}

	public int getPointPosY(int psg) {
		int numsg  = arrayVxy.length >> 1;

		int index  = (psg % numsg) << 1;
		return arrayVxy[index + 1];
	}

	public byte getPointAlt(int psg) {
		int numsg  = arrayVxy.length >> 1;

		int index  = (psg % numsg);
		return arrayAlt[index];
	}
	
	public int getNumOfSubsegments(int sg) {
		return arrayLong[sg % getNumOfSegments()];
	}
	
	public int getNumOfAllSubsegments() {
		int r=0;
		for (int i = 0; i < getNumOfSegments(); ++i) {
			r += getNumOfSubsegments(i);
		}
		return r;
	}


	public int calcPosX;
	public int calcPosY;
	public int calcPosZ;
	public int fpCalcPosX;
	public int fpCalcPosY;
	
	public void calcPosition(int sg, int fpsubsg) {
		if (getNumOfPoints() > 3) {
			sg %= (getNumOfPoints() - 1) / 3;

			int i    = sg * 3;

			int v1x  = getPointPosX(i);
			int v1y  = getPointPosY(i);
			int v2x  = getPointPosX(i + 1);
			int v2y  = getPointPosY(i + 1);
			int v3x  = getPointPosX(i + 2);
			int v3y  = getPointPosY(i + 2);
			int v4x  = getPointPosX(i + 3);
			int v4y  = getPointPosY(i + 3);

			int t    = fpsubsg;

			int ti   = oMathFP.toFP(1) - t;
			int t2   = oMathFP.mul(t, t);
			int ti2  = oMathFP.mul(ti, ti);

			int h1   = oMathFP.mul(ti2, ti);
			int h2   = 3 * oMathFP.mul(t, ti2);
			int h3   = 3 * oMathFP.mul(t2, ti);
			int h4   = oMathFP.mul(t2, t);

			int fx   = h1 * v1x + h2 * v2x + h3 * v3x + h4 * v4x;
			int fy   = h1 * v1y + h2 * v2y + h3 * v3y + h4 * v4y;

			// store result
			fpCalcPosX = fx;
			fpCalcPosY = fy;
			calcPosX = oMathFP.toInt(fx);
			calcPosY = oMathFP.toInt(fy);
			calcPosZ = 0;
		}
	}

	public int calcHeight(int sg, int fpsubsg) {

		if (getNumOfPoints() > 3) {
			sg %= (getNumOfPoints() - 1) / 3;

			int i    = sg * 3;
			int v1x  = getPointAlt(i);
			int v2x  = getPointAlt(i+1);
			int v3x  = getPointAlt(i+2);
			int v4x  = getPointAlt(i+3);

			int t    = fpsubsg;

			int ti   = oMathFP.toFP(1) - t;
			int t2   = oMathFP.mul(t, t);
			int ti2  = oMathFP.mul(ti, ti);

			int h1   = oMathFP.mul(ti2, ti);
			int h2   = 3 * oMathFP.mul(t, ti2);
			int h3   = 3 * oMathFP.mul(t2, ti);
			int h4   = oMathFP.mul(t2, t);

			int fx   = h1 * v1x + h2 * v2x + h3 * v3x + h4 * v4x;
			// store result
			calcPosZ = oMathFP.toInt(fx);
			return fx;
		}
		return 0;
	}
	
	//#ifdef UBER_LOW_QUALITY
	//#else
	static final int NUM_SECTIONS_AHEAD = 11;	
	//#endif
	
	int[] posArrayX = new int[NUM_SECTIONS_AHEAD];
	int[] posArrayY = new int[NUM_SECTIONS_AHEAD];
	int[] posArrayZ = new int[NUM_SECTIONS_AHEAD];
	
	public void calcPositionArray(int usid) {
		int index=0;
		int iseg=0;
		int numSegs = getNumOfSegments();
		
		try { 
			while (index + arrayLong[iseg] < usid) {
				index += arrayLong[iseg++];
			}
			
			int ipa = 0;
			int lastUsid = usid + NUM_SECTIONS_AHEAD;
			int currentFpSub = usid - index;
			int currentNumSub = arrayLong[iseg];
			int deltaSub = oMathFP.div(oMathFP.ONE, oMathFP.toFP(arrayLong[iseg]));
			
			while (usid < lastUsid) {
				if (currentFpSub >= currentNumSub) {
					currentFpSub = 0;
					currentNumSub = arrayLong[++iseg];
				}
				
				// calculate point position
				calcPosition(iseg, currentFpSub * deltaSub);
				
				posArrayX[ipa] = calcPosX;
				posArrayY[ipa] = calcPosY;
				posArrayZ[ipa] = calcHeight(iseg, currentFpSub * deltaSub);
				
				++currentFpSub;
				++ipa;
				++usid;
			}
		} catch (Exception e) {
			System.out.println("Track not large enough");
		}	
	}
	
	public void precalculateSectionArray() {
		
		int index = 0;
		int len = getNumOfAllSubsegments();
		
		vertexSectionArray = new int[len*3];
		if (typeSectionArray==null || typeSectionArray.length != len)
			typeSectionArray = new byte[len];

		if (verticalTypeSectionArray==null || verticalTypeSectionArray.length != len)
			verticalTypeSectionArray = new byte[len];
		
		for (int sg = 0; sg < this.getNumOfSegments(); ++sg) {			
			int currentNumSub = arrayLong[sg];
			int deltaSub = oMathFP.div(oMathFP.ONE, oMathFP.toFP(currentNumSub));
			
			for (int ns = 0; ns < currentNumSub; ++ns) {

				// calculate point position
				calcPosition(sg, ns * deltaSub);
				
				vertexSectionArray[index  ] = calcPosX;
				vertexSectionArray[index+1] = calcPosY;
				vertexSectionArray[index+2] = calcHeight(sg, ns * deltaSub);
				
				index += 3;
			}
		}
	}
		
	public void update() {
		calculateLongitudes();
		precalculateSectionArray();				
	}
	
	
	public int calcDirX;
	public int calcDirY;

	public void calcDirection(int sg, int fpsubsg) {

		calcPosition(sg, fpsubsg);

		int curPosX  = calcPosX;
		int curPosY  = calcPosY;

		calcPosition(sg, fpsubsg + oMathFP.ONE/10);

		int dirPosX  = calcPosX - curPosX;
		int dirPosY  = calcPosY - curPosY;

		// Normalize vector
		int fpudiv   = oMathFP.div(oMathFP.ONE, oMathFP.sqrt((dirPosX * dirPosX) + (dirPosY * dirPosY)));

		dirPosX *= fpudiv;
		dirPosY *= fpudiv;

		calcDirX = dirPosX;
		calcDirY = dirPosY;
	}


	public int addPoint(int px, int py, byte alt) {

		System.gc();
		//System.out.println(" Adding point "+arrayAlt.length+"  (mem "+Runtime.getRuntime().freeMemory()+")");
		
		// recreate arrays
		int lavxy     = arrayVxy.length + 2;
		int laalt     = arrayAlt.length + 1;
		int[] tavxy   = new int[lavxy];
		byte[] taalt  = new byte[laalt];

		System.arraycopy(arrayVxy, 0, tavxy, 0, arrayVxy.length);
		System.arraycopy(arrayAlt, 0, taalt, 0, arrayAlt.length);

		// Add the new point
		tavxy[lavxy - 2] = px;
		tavxy[lavxy - 1] = py;

		taalt[laalt - 1] = alt;

		// replace arrays
		arrayVxy = tavxy;
		arrayAlt = taalt;	
		
		return laalt - 1;
	}
	
	final static int LONGITUDE_FACTOR_DIV = 8; //10

	public void calculateLongitudes() {
		int numSegs  = getNumOfSegments();
		
		arrayLong = new int[numSegs];
		for (int i = 0; i < numSegs; ++i) {
			arrayLong[i] = oMathFP.toInt(getLongitudeFactor(i) / LONGITUDE_FACTOR_DIV);
		}
	}

	public int getLongitudeFactor(int sg) {
		int longitude  = 0;
		sg %= (getNumOfPoints() - 1) / 3;
		int i          = sg * 3;
		if (getNumOfPoints() > 0) {
			int ox        = oMathFP.toFP(getPointPosX(i));
			int oy        = oMathFP.toFP(getPointPosY(i));
			int v1x       = getPointPosX(i);
			int v1y       = getPointPosY(i);
			int v2x       = getPointPosX(i + 1);
			int v2y       = getPointPosY(i + 1);
			int v3x       = getPointPosX(i + 2);
			int v3y       = getPointPosY(i + 2);
			int v4x       = getPointPosX(i + 3);
			int v4y       = getPointPosY(i + 3);

			int numSteps  = 10;
			for (int steps = 0; steps < numSteps; ++steps) {

				int t    = oMathFP.toFP(steps) / ((int) numSteps - 1);
				int t2   = oMathFP.mul(t, t);
				int t3   = oMathFP.mul(t, t2);

				int mit  = oMathFP.toFP(1) - t;

				int h1   = oMathFP.mul(oMathFP.mul(mit, mit), mit);
				int h2   = 3 * oMathFP.mul(oMathFP.mul(t, mit), mit);
				int h3   = 3 * oMathFP.mul(oMathFP.mul(t, t), mit);
				int h4   = oMathFP.mul(oMathFP.mul(t, t), t);

				int fx   = (int) (h1 * v1x + h2 * v2x + h3 * v3x + h4 * v4x);
				int fy   = (int) (h1 * v1y + h2 * v2y + h3 * v3y + h4 * v4y);

				longitude += oMathFP.sqrt(oMathFP.mul((fx - ox), (fx - ox)) + oMathFP.mul((fy - oy), (fy - oy)));

				ox = fx;
				oy = fy;
			}
		}
		return longitude;
	}
}

