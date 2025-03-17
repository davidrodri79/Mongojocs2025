package com.mygdx.mongojocs.toca;
//#endif


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

import java.io.InputStream;

public class TrackGameRenderer {
	private long timeStart = System.currentTimeMillis();	
	private int frameCounter;

	Graphics gBuffer;
	GameCanvas gc;
	
	//int numRects;
	//int numQuads;
	int frames = 0;
	
	//int directQuads = 0;
	//int skippedQuads = 0;

	//#ifdef TOCA
	//#ifdef TWO_COLORS
	String[] carFiles = {
			"/skyline_azul",
			"/koening_rojo",
			"/skyline_azul",
			"/koening_rojo" };
	//#else
	//#endif
	//#else
	//#endif
	Image[] carImages;
	byte[][] carCoords;
	
	
	int flagCnt = 0;
	
	Image[] brokenWindowsImages;
	byte[][] brokenWindowsCoords;
	
	Image[] lightsImages;
	byte[][] lightsCoords;
	
	//#ifdef TOCA
	String[] extraFiles = { 
			"/skyline",
			"/koening"
		};
		//#ifdef TWO_COLORS
			int []extraCarFilesMap = { 0, 1, 0, 1 };
		//#else
	//#else
	//#endif
	
	
	byte[][] collisionRects;
	
	
	public Image skyImage;
	public Image horizonImage;
	Image ckmImage;
	
	int lastUsid;
	
	Track track;


	public void loadCarImages() {
		carImages = new Image[carFiles.length];
		carCoords = new byte[carFiles.length][];
		
		for (int i = 0; i < carFiles.length; ++i) {
			System.gc();
			carCoords[i] = loadFile(carFiles[i]+".cor");
			System.gc();
			
			try {
				//#ifdef PAL_TRICK
				if(i < carFiles.length / 2) {
					carImages[i] = new Image();
					carImages[i]._createImage(carFiles[i]+".png");
				} else {
					carImages[i] = createImageFromPalette(carFiles[i]+".png", carFiles[i]+".pal");
				}
				//#else
				//#endif
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//Image pocafeina = null;
	int[] xformedVertexs = new int[Track.NUM_SECTIONS_AHEAD*4];
	
	public TrackGameRenderer() {
		
		//#ifdef J2ME
		try {
			//System.gc();
			//horizonImage	= Image.createImage("/horizon.png");
			System.gc();
			ckmImage		= new Image();
			ckmImage._createImage("/cklm.png");
			System.gc();
			//pocafeina		= Image.createImage("/doom.png");

			
			loadCarImages();
			
			//#ifndef NO_BROKEN_GLASS
			brokenWindowsImages = new Image[extraCarFilesMap.length];
			brokenWindowsCoords = new byte[extraCarFilesMap.length][];
			//#endif
			//#ifndef NO_LIGHTS
			lightsImages = new Image[extraCarFilesMap.length];
			lightsCoords = new byte[extraCarFilesMap.length][];
			//#endif
			collisionRects = new byte[extraCarFilesMap.length][];
			
			for (int i = 0; i < extraFiles.length; ++i) {
				
				try {
					//System.out.println("Loading "+extraFiles[i]);
					//#ifndef NO_LIGHTS
					System.gc();
					Image imgLuz = new Image();
					imgLuz._createImage(extraFiles[i]+"_luces.png");
					System.gc();
					byte[] corLuz = loadFile(extraFiles[i]+"_luces.cor");
					//#endif
					
					//#ifndef NO_BROKEN_GLASS
					System.gc();
					Image imgCris = new Image();
					imgCris._createImage(extraFiles[i]+"_cristales.png");
					System.gc();
					byte[] corCris= loadFile(extraFiles[i]+"_cristales.cor");
					//#endif
					
					System.gc();
					byte[] colRct= loadFile(extraFiles[i]+"_mask.crt");

					for (int j = 0; j < extraCarFilesMap.length; ++j) {
						if (extraCarFilesMap[j] == i) {
							//#ifndef NO_LIGHTS
							lightsImages[j] = imgLuz;
							lightsCoords[j] = corLuz;
							//#endif
							//#ifndef NO_BROKEN_GLASS
							brokenWindowsImages[j] = imgCris;
							brokenWindowsCoords[j] = corCris;
							//#endif
							
							collisionRects[j] = colRct;
							
							//System.out.println("Setting "+j+" "+extraFiles[i]);
						}
					}
					
				} catch (Exception e) {
					System.out.println("Unable to load all");
				}
				
			}
			//skyImage		= Image.createImage("/sky.png");
			//skyImage.getWidth();

		} catch (Exception e) {
			System.out.println("Error loading files");
			//e.printStackTrace();
		}
		//#endif
	}
				

	public byte[] loadFile(String Nombre) {
		/*System.gc();
	
		InputStream is = getClass().getResourceAsStream(Nombre);
	
		byte[] buffer = new byte[1024];
	
		try
		{
			int Size = 0;
	
			while (true)
			{
			int Desp = is.read(buffer, 0, buffer.length);
			if (Desp <= 0) {break;}
			Size += Desp;
			}
	
			is = null; System.gc();
	
			buffer = new byte[Size];
	
			is = getClass().getResourceAsStream(Nombre);
			Size = is.read(buffer, 0, buffer.length);
	
			while (Size < buffer.length) {Size += is.read(buffer, Size, buffer.length-Size);}
	
			is.close();
		}
		catch(Exception exception) {}
	
		System.gc();
	
		return buffer;*/

		FileHandle file = Gdx.files.internal(MIDlet.assetsFolder+"/"+Nombre.substring(1));
		byte[] bytes = file.readBytes();

		return bytes;
	}
	
	/**
	 *  Renders track
	 *
	 * @param  g       graphics objects
	 * @param  width   render view width
	 * @param  height  render view height
	 */
	public void render(int timeValue, Graphics g, int width, int height, FwdVehicle myVehicle) {
		int fps = 0;
		
		if(carImages == null) {
			loadCarImages();
		}
		//directQuads = 0;
		//skippedQuads = 0;
		
		heightCanvas = height;
		gBuffer = g;		
		
		//long msRender = System.currentTimeMillis();
		

		Game game = Game.instance;
		if (game==null) return;
		
		if(game.raceTime <= 0) {
			frames = 0;
		} else {
			fps = (100000 * frames) / game.raceTime;
		}
		
		int fov = game.fov;
		int camHeight = game.height;
		int pitch = game.pitch;
		
		fov = fov - (myVehicle.fv >> 3);
		
		camHeight -=  (myVehicle.fv >> 4);
		
		fov = fov * width/128;
			
		// draw background
		//
		g.setColor(0x10A0C0);
		g.fillRect(0, 0, width, height);
						
						//long msCam = System.currentTimeMillis();
		
		Track track = myVehicle.track;
				
		int shadeLerpInvFactor = 65536/track.shadeLerpFactor;
		int lerpCol = track.shadeColor; //0x808080;
		
		if (track.vertexSectionArray.length < 4) return;
		
		int curValue = myVehicle.getZ();
		int lateralDisplace = myVehicle.getLateralDisplace();
		
		int usid = myVehicle.usid; //subUsid/FwdVehicle.SUBUSIDS_PER_USID;
		lastUsid = usid;

		
		// get vehicle pos
		int pvx = myVehicle.getX();
		int pvy = myVehicle.getY();
		int pvz = myVehicle.getZ();
		
		int pdirx = myVehicle.getDirX();
		int pdiry = myVehicle.getDirY();
		int pdirz = myVehicle.getDirZ();
			
			// Correct displacement
			pvx += game.myVehicle.ang*oMathFP.toInt(pdiry*2048)/4096;
			pvy -= game.myVehicle.ang*oMathFP.toInt(pdirx*2048)/4096;
				
		int width2 = width>>1;
		int height2 = height>>1;

		// draw track
		//
		
		int ipdl = oMathFP.div(oMathFP.ONE, oMathFP.sqrt(oMathFP.mul(pdirx, pdirx) + oMathFP.mul(pdiry, pdiry)));
		
		int CAMHEIGHT = camHeight/5; //10;
		int CAMHDIVFACTOR = 50;
		
		int[] matCam = calcMatrixCamera(0, (oMathFP.toFP(CAMHEIGHT))/CAMHDIVFACTOR, 0,
			oMathFP.mul(pdiry, ipdl),
			oMathFP.toFP(pitch)/10 +
				//game.myVehicle.getDirZ()/8860,
			(pdirz - pvz)/60,
			oMathFP.mul(pdirx, ipdl)
			);		

						//msCam = System.currentTimeMillis() - msCam;
			int horizonY=0;
					
		// Draw Sky
		{
		g.setColor(0x0000ff);		
			int vttx = 0;
			int vtty = 0;
			int vttz = 0;		
		
			int tvttx = pvy;
			int tvtty = CAMHEIGHT;
			int tvttz = pvx;
			
			vttx -= tvttx;
			vtty -= tvtty;
			vttz -= tvttz;
			
			vtty -= oMathFP.toInt(pvz);
								
			xformVector(matCam, vttx, vtty, vttz);
			
			vttx = xformX;
			vtty = xformY;
			vttz = xformZ;
			
			vtty += tvtty;
		
		horizonY = (fov*vtty)/500;

		}
		
		int off = oMathFP.toInt(myVehicle.fpViewAng*64);
		//
		if (skyImage!=null) {
			while (off < 0) off += skyImage.getWidth();		
			off %= skyImage.getWidth();
			
			//#ifdef J2ME
			g.drawImage(skyImage, off-skyImage.getWidth(), 0, Graphics.LEFT|Graphics.TOP);
			g.drawImage(skyImage, off, 0, Graphics.LEFT|Graphics.TOP);
			//#elifdef DOJA
			//#endif
		}
		//
		
				
		// draw sections ahead
		//
		//long msXform = System.currentTimeMillis();

		int indexVertexs = 0;
		
		for(int i = 0; i < track.NUM_SECTIONS_AHEAD; ++i) {			
			int sid = (usid+i)*3;
			sid %= track.vertexSectionArray.length;		// hack
			
			int fvx = track.vertexSectionArray[sid];	//posArrayX[i];
			int fvy = track.vertexSectionArray[sid+1];	//posArrayY[i];
			int fh  = track.vertexSectionArray[sid+2];	//posArrayZ[i];
			int fvz  = oMathFP.toInt(fh);
			
			int sectype = track.typeSectionArray[sid/3]; // AIOOB 357
			int vertsectype = track.verticalTypeSectionArray[sid/3]; // AIOOB 357

			int vttx = fvy;
			int vtty = fvz;
			int vttz = fvx;
			
			int tvttx = pvy;
			int tvtty = CAMHEIGHT;
			int tvttz = pvx;
			
			vttx -= tvttx;
			vtty -= tvtty;
			vttz -= tvttz;
			
			vtty -= oMathFP.toInt(pvz);
								
			xformVector(matCam, vttx, vtty, vttz);
			
			vttx = xformX;
			vtty = xformY;
			vttz = xformZ;
			
			vtty += tvtty;
			
			xformedVertexs[indexVertexs] = vttx*vttz/25 + lateralDisplace;
			xformedVertexs[indexVertexs+1] = vtty;
			xformedVertexs[indexVertexs+2] = vttz;
			xformedVertexs[indexVertexs+3] = sectype + (vertsectype<<8);
			indexVertexs+=4;
		}
						//msXform = System.currentTimeMillis() - msXform;

		
		int lpty = xformedVertexs[track.NUM_SECTIONS_AHEAD*4+1-4];
		int lptz = xformedVertexs[track.NUM_SECTIONS_AHEAD*4+2-4];
		if (lptz <= 0) lptz = 1;


		int lastYproj = height2 + (fov*lpty)/lptz;
		if (horizonImage!=null) {
			//#ifdef J2ME
			int lastYprojImg = lastYproj - horizonImage.getHeight();
			
			off = oMathFP.toInt(myVehicle.fpViewAng*32);
			while (off < 0) off += horizonImage.getWidth();
			off %= horizonImage.getWidth();
	
			int yHorizStart = horizonY + height2 - horizonImage.getHeight();
			yHorizStart = (yHorizStart + 2*height/3 - horizonImage.getHeight() +  lastYprojImg ) / 3;
			if (yHorizStart < lastYprojImg) yHorizStart = lastYprojImg;
			g.drawImage(horizonImage, off-horizonImage.getWidth(), yHorizStart, Graphics.LEFT|Graphics.TOP);
			g.drawImage(horizonImage, off, yHorizStart, Graphics.LEFT|Graphics.TOP);
			//#endif
	
		}
		g.setColor(0x101010);
		g.fillRect(0, lastYproj, width, height);
				
		
				//long msProjDraw = System.currentTimeMillis();
		
					int lerpColRB = lerpCol & 0xff00ff;
					int lerpColG  = lerpCol & 0x00ff00;
		
		for(int i = track.NUM_SECTIONS_AHEAD-1; i > 0; --i) {
			int indx = i<<2;
			int ottx = xformedVertexs[indx-4];
			int otty = xformedVertexs[indx-4+1];
			int ottz = xformedVertexs[indx-4+2];
			
			int vttx = xformedVertexs[indx];
			int vtty = xformedVertexs[indx+1];
			int vttz = xformedVertexs[indx+2];
			
			int sectype = xformedVertexs[indx+3] & 0xff;
			int vertsectype = (xformedVertexs[indx+3]>>8) & 0xff;

			TrackSection ts = (TrackSection) track.trackSectionsMap[sectype];
			if(vertsectype < 0 || vertsectype >= track.verticalTrackSectionsMap.length) {
				vertsectype = 0;
			}
			TrackSection vts = (TrackSection) track.verticalTrackSectionsMap[vertsectype];
			
			if (ts == null) continue;
			int[] patchList = ts.patchList;
			int[] lineList = ts.lineList;
			
						
			if (vttz > 0) {
								
				if (ottz <= 0)		ottz = 1;
								
				int dttx = ottx - vttx;
				int dtty = otty - vtty;
				int dttz = ottz - vttz;
				
				int vsttx = vttx << 8;
				int vstty = vtty << 6;
				int vsttz = vttz << 6;
								
				int lerp = shadeLerpInvFactor*vttz >>10;

				if (lerp > 255) lerp = 255;
				else if (lerp < 0) lerp = 0;

				g.startPrimitives();
				
				for (int j = 0; j <patchList.length; j+= Track.INTS_PER_PATCH) {
					int xlt = patchList[j];
					int xrt = patchList[j+1];
					int xlb = patchList[j+2];
					int xrb = patchList[j+3];
					int yi = patchList[j+4];
					int yf = patchList[j+5];
					int col = patchList[j+6];
					int type = patchList[j+7];
					int altCol = patchList[j+8];
					
					if ((type & Track.CENTRAL_BIT) != 0) {
						continue;
					}
					
					int ptdz = ((dttz*yi)>>10) + vsttz;
					int otdz = ((dttz*yf)>>10) + vsttz;
					if(ptdz <= 0) {
						ptdz = 1;
					}
					if(otdz <= 0) {
						otdz = 1;
					}
					
					int ptdy = ((dtty*yi)>>10) + vstty;
					int otdy = ((dtty*yf)>>10) + vstty;
					
					int iptdz = fov*65536/ptdz;
					int iotdz = fov*65536/otdz;

					int ptdx = ((dttx*yi)>>8) + vsttx;
					int otdx = ((dttx*yf)>>8) + vsttx;
					
					int spjy = ptdy*iptdz >> 16; // /ptdz;
					int sopjy = otdy*iotdz >> 16; // /otdz;
		
					xlt = iptdz*(xlt+ptdx) >>18;
					xrt = iptdz*(xrt+ptdx) >>18;
					xlb = iotdz*(xlb+otdx) >>18;
					xrb = iotdz*(xrb+otdx) >>18;
															
					if (((i+usid) & 1) == 0)	col = altCol;
															
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
					
					colRB += (lerpColRB-colRB)*lerp >> 8;
					colG  += (lerpColG-colG)*lerp >> 8;
					
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
					
					g.setColor(col);
					quadFill( 
						xlt + width2,
						xrt + width2,
						xlb + width2,
						xrb + width2,						
						spjy + height2,
						sopjy + height2,
						col);						
				}

				g.endPrimitives();
				
				for (int j = 0; j <lineList.length; j+= Track.INTS_PER_LINE) {
					int xi = lineList[j];
					int yi = lineList[j+1];
					int xf = lineList[j+2];
					int yf = lineList[j+3];
					int col = lineList[j+4];
					int altCol = lineList[j+5];					
										
					int ptdz = ((dttz*yi)>>10) + vsttz;
					int otdz = ((dttz*yf)>>10) + vsttz;
					
					int ptdy = ((dtty*yi)>>10) + vstty;
					int otdy = ((dtty*yf)>>10) + vstty;
					
					int ptdx = ((dttx*yi)>>8) + vsttx;
					int otdx = ((dttx*yf)>>8) + vsttx;

					if(ptdz <= 0) {
						ptdz = 1;
					}
					if(otdz <= 0) {
						otdz = 1;
					}
					
					int iptdz = fov*65536/ptdz;
					int iotdz = fov*65536/otdz;

					int spjy = ptdy*iptdz >> 16;
					int sopjy =otdy*iotdz >> 16;
										
					xi = iptdz*(xi+ptdx) >> 18;
					xf = iotdz*(xf+otdx) >> 18;

					if (((i+usid) & 1) == 0)	col = altCol;
										
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
					
					colRB += (lerpColRB-colRB)*lerp >> 8;
					colG  += (lerpColG-colG)*lerp >> 8;
					
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
										
					g.setColor(col);					
					g.drawLine(xi + width2, spjy + height2,
							xf + width2, sopjy + height2);
				}
				
				
				/// VERTICAL ////////////////--
				if (vts==null) continue;
				
				patchList = vts.patchList;
				lineList = vts.lineList;

				g.startPrimitives();

				for (int j = 0; j <patchList.length; j+= Track.INTS_PER_PATCH) {
					int xlt = patchList[j];
					int xrt = patchList[j+1];
					int xlb = patchList[j+2];
					int xrb = patchList[j+3];
					int yi = patchList[j+4];
					int yf = patchList[j+5];
					int col = patchList[j+6];
					int altCol = patchList[j+8];										

					int ptdy = (yi)+vtty;
					int otdy = (yf)+vtty;
					
					int spjy = (fov*ptdy)/vttz;
					int sopjy = (fov*otdy)/vttz;

					int pjdx = (fov*vttx)/vttz;

					xlt = (fov*(xlt))/(vttz) >>8;
					xrt = (fov*(xrt))/(vttz) >>8;
					xlb = (fov*(xlb))/(vttz) >>8;
					xrb = (fov*(xrb))/(vttz) >>8;
										
					if (lerp > 255) lerp = 255;
					if (lerp < 0) lerp = 0;
										
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
					
					colRB += (lerpColRB-colRB)*lerp >> 8;
					colG  += (lerpColG-colG)*lerp >> 8;
					
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
					
					g.setColor(col);
					quadFill(
						xlt+pjdx + width2,
						xrt+pjdx + width2,
						xlb+pjdx + width2,
						xrb+pjdx + width2,						
						spjy + height2,
						sopjy + height2,
						col
						);							
				}

				g.endPrimitives();
				
				for (int j = 0; j <lineList.length; j+= Track.INTS_PER_LINE) {
					int xi = lineList[j];
					int yi = lineList[j+1];
					int xf = lineList[j+2];
					int yf = lineList[j+3];
					int col = lineList[j+4];
					int altCol = lineList[j+5];

					int ptdy = (yi)+vtty;
					int otdy = (yf)+vtty;
					
					int spjy = (fov*ptdy)/vttz;
					int sopjy = (fov*otdy)/vttz;

					int pjdx = (fov*vttx)/vttz;
					
					xi = (fov*(xi))/(vttz) >>8;
					xf = (fov*(xf))/(vttz) >>8;
					
					if (lerp > 255) lerp = 255;
					if (lerp < 0) lerp = 0;
										
					int colRB = col & 0xff00ff;
					int colG  = col & 0x00ff00;
					
					colRB += (lerpColRB-colRB)*lerp >> 8;
					colG  += (lerpColG-colG)*lerp >> 8;
					
					col = (colRB & 0x0ff00ff) | (colG & 0x000ff00);
										
					g.setColor(col);
					
					g.drawLine(xi+pjdx + width2, spjy + height2,
							xf+pjdx + width2, sopjy + height2);
				}
				
			}			
		}
//						msProjDraw = System.currentTimeMillis()-msProjDraw;

		// draw other vehicles ///////////////////////////////////////////////////////////
		//
		
		int myVehicleDepth = myVehicle.fv >> 5;

		try {
			FwdVehicle[] v = game.vehiclesByDepth;
			
			//hack of the death!!
			if(game.nPhant != 0 && game.phantom.isSet) {
				 v = new FwdVehicle[1];
				 v[0] = game.phantom;
			}
			
			for (int i = 0; i < v.length; ++i) {
				if (v[i] == myVehicle)	continue;
				
				int vu = v[i].subUsid;	
				int modUsid = myVehicle.subUsid;
	
				if (game.nPhant !=0) {						
					vu %= track.typeSectionArray.length<<7;
					modUsid %= track.typeSectionArray.length<<7;
				}
	
				//#ifdef NK-s30
				//#else
				int cy = height-6-height/10  - ((height2*myVehicle.realVel >> 9));
				//#endif
				
				
				if (vu >= modUsid && vu < modUsid+(track.NUM_SECTIONS_AHEAD<<7)) {
					int sid = (vu)*3;
					sid %= track.vertexSectionArray.length;		// hack
		
					int fvx = v[i].getX();
					int fvy = v[i].getY();
					int fh  = v[i].getZ();
					int fvz  = oMathFP.toInt(fh);
					
					int vttx = fvy;
					int vtty = fvz;
					int vttz = fvx;
					
					int tvttx = pvy;
					int tvtty = CAMHEIGHT;
					int tvttz = pvx;
					
					vttx -= tvttx;
					vtty -= tvtty;
					vttz -= tvttz;
					
					vtty -= oMathFP.toInt(pvz);
										
					xformVector(matCam, vttx, vtty, vttz);
					
					vttx = xformX;
					vtty = xformY;
					vttz = xformZ;
					
					vtty += tvtty;
					
					vttx = vttx*vttz/25 - v[i].getLateralDisplace() + lateralDisplace;
					
					if (vttz > 0) {
						int pjx = (fov*vttx)/vttz;
						int pjy = (fov*vtty)/vttz;
						
						int vsu = v[i].subUsid;
						int msu = myVehicle.subUsid;
	
						if (game.nPhant !=0) {
							vsu %= (track.typeSectionArray.length<<7);
							msu %= (track.typeSectionArray.length<<7);
						}
						
						int depth = /*120*/(vsu-msu)/track.NUM_SECTIONS_AHEAD;
						int depthLevel;
						for (depthLevel = 0; depthLevel < Game.instance.depthLevels.length; ++depthLevel) {						
							if (Game.instance.depthLevels[depthLevel] > depth) break; 
						}
						int depthIndLevel = depthLevel;
						depthLevel = (depthLevel<<1) + ((cy+height2>pjy)?(myVehicleDepth>>1):0);
						if (depthIndLevel > 0 && depthIndLevel < Game.instance.depthLevels.length) {
							int depthHalf = Game.instance.depthLevels[depthIndLevel-1] + Game.instance.depthLevels[depthIndLevel] >> 1;
							
							int dl = Game.instance.depthLevels[depthIndLevel-1];
							int dh = Game.instance.depthLevels[depthIndLevel];
							
							if (depth > depthHalf) {
								++depthLevel;
							}

							//Someone is going to kill Pau }:�
							if(game.nPhant != 0) {
								drawCarWithCollision(v[i], g, pjx + width2, pjy + height2, -v[i].ang>>3, depthLevel);
							}
							
						}
						v[i].cx = pjx + width2;
						v[i].ca = -v[i].ang>>3;
						v[i].lY = pjy + height2;
						v[i].dl = depthLevel;
					} else {
						v[i].cx = -111;
						v[i].dl = -111;
						if (v[i].lY > height-20)	v[i].lY += height2;
						v[i].curColRectXi = -1000;
						v[i].curColRectXf = -1000;
						v[i].curColRectYi = -1000;
						v[i].curColRectYf = -1000;
					}
				} else {
					v[i].cx = -111;
					v[i].dl = -111;
					if (v[i].lY > height-20)	v[i].lY += height2;
					v[i].curColRectXi = -1000;
					v[i].curColRectXf = -1000;
					v[i].curColRectYi = -1000;
					v[i].curColRectYf = -1000;
				}
			}
		} catch (Exception e) {
			//#ifdef EXCEPTION_CONTROL
			//Game.instance.gc.excp = e;
			//#endif
		}
		//////////////////////////////////////////////////////////////////////////////
		
		// Draw my vehicle ///////////////////////////////////////////////////////////
		try {
			FwdVehicle[] v = game.vehiclesByDepth;
			int carx = width/2;
			//#ifdef NK-s30
			//#else
			int cary =   height-height/10  - ((height2*myVehicle.realVel >> 9));//<<1);//4);
			//#endif
			
			myVehicle.carType = Game.instance.selectedCar;
			myVehicle.lY = cary + 10;
			
			boolean wp = false;
			
			for (int i = 0; i < v.length; ++i) {
				if (v[i] == myVehicle)	continue;
				
				if (v[i].lY < myVehicle.lY) {
					if (v[i].dl!=-111)
						drawCarWithCollision(v[i], g, v[i].cx, v[i].lY, v[i].ca, v[i].dl);
				} else if (!wp) {
					wp = true;
					drawCarWithCollision(myVehicle, g, carx, cary, -myVehicle.ang >> 3, myVehicleDepth);
					if (v[i].dl!=-111)
						drawCarWithCollision(v[i], g, v[i].cx, v[i].lY, v[i].ca, v[i].dl);
				}
			}
			if (!wp) {
				drawCarWithCollision(myVehicle, g, carx, cary, -myVehicle.ang >> 3, myVehicleDepth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//#ifdef J2ME
		g.setClip(0, 0, width, height);
		//#endif
		
		//////////////////////////////////////////////////////////////////////////////
		
		// draws track -optional- //////////////////////////////////////////////////
		
		//#ifdef TRACK_INGAME
			//#ifdef J2ME
			int CRVDIV = 600/width2;
	
			g.setColor(0xffffff);
			int min = ((myVehicle.subUsid+myVehicle.dsu)>>7) - 10 * 4;
			
			if(min < 0) {
				min = track.typeSectionArray.length + min;		//NORL!!
			}
			
			//min *= 3;
			try {
				int p = (((myVehicle.subUsid+myVehicle.dsu)>>7) * 3) % track.vertexSectionArray.length;
				int centerx = track.vertexSectionArray[p];
				int centery = track.vertexSectionArray[p + 1];
				
				//comprovar min i max!!!
				g.setClip(0,0,50,50);
				
				for(int j = 0; j < 2; j++) {
					int ox = track.vertexSectionArray[(min * 3) % track.vertexSectionArray.length] - centerx;
					int oy = track.vertexSectionArray[(min * 3) % track.vertexSectionArray.length + 1] - centery;
					
					if(j == 0) {
						g.setColor(0);
					} else {
						g.setColor(0xffffff);
					}
					
					for (int i = 0; i < 20; i ++) {
						p = (i * 12 + min * 3) % track.vertexSectionArray.length;
						int cx = track.vertexSectionArray[p] - centerx;
						int cy = track.vertexSectionArray[p + 1] - centery;
						
						int oxcr = ox/CRVDIV;
						int oycr = oy/CRVDIV;
						int cxcr = cx/CRVDIV;
						int cycr = cy/CRVDIV;
						
						if(j == 0){
							//reborde!!							
							g.drawLine(oxcr + 25, oycr + 25 + 1, cxcr + 25, cycr + 25 + 1);
							g.drawLine(oxcr + 25, oycr + 25 - 1, cxcr + 25, cycr + 25 - 1);
							g.drawLine(oxcr + 25 + 1, oycr + 25, cxcr + 25 + 1, cycr + 25);
							g.drawLine(oxcr + 25 - 1, oycr + 25, cxcr + 25 - 1, cycr + 25);
						} else {
							g.drawLine(oxcr + 25 , oycr + 25 , cxcr + 25 , cycr + 25);
						}
						
						ox = cx;
						oy = cy;
					}
					
				}
				
				// draw vehicles positions
				//g.setClip(0,0,width,height);
				//hack of the death strikes back
				int max = game.oppoVehicles.length;
				if(game.nPhant != 0 && game.phantom.isSet) {
					max = 1;
				}
				
				for (int i = 0; i < max; ++i) {
					int vu;
					if(game.nPhant == 0) {
						vu = game.oppoVehicles[i].subUsid>>7;
					} else {
						vu = game.phantom.usid % (track.vertexSectionArray.length / 3);	//subUsid/FwdVehicle.SUBUSIDS_PER_USID;
						min %= (track.vertexSectionArray.length / 3);
					}
					
					//vigilar amb aixo! abans fallava i ho vaig desactivar ar a ho he activat sac --> yes
					if(vu >= min && vu <= min + 20*4) {
						
						vu %= track.typeSectionArray.length;
						vu *= 3;
						int cx = track.vertexSectionArray[vu] - centerx;
						int cy = track.vertexSectionArray[vu + 1] - centery;
						
						cx = cx/CRVDIV + 25;
						cy = cy/CRVDIV + 25;
						
						g.setColor(0x4040ff);
						g.fillRect(cx-2, cy-2, 5, 5);
					}
				}
				
				g.setColor(0xff0000);
				g.fillRect(-2 + 25, - 2 + 25, 5, 5);		
			} catch (Exception e) {
				//Game.getInstance().lastException = e.getMessage();
				e.printStackTrace();
			}
			//#endif
		//#endif
		
		/////////////////////////////////////////////////////////////////////////////////		
		
		// Draw km/h counter ////////////////////////////////////////////////////////////
		
		//#ifdef J2ME
		g.setClip(0, 0, width, height);
		//#endif
		//#ifdef J2ME
		int kmX = width-ckmImage.getWidth()-1;
		int kmY = height-ckmImage.getHeight()-1;
		
		g.drawImage(ckmImage, kmX, kmY, Graphics.LEFT|Graphics.TOP);
		
		int degKm = (oMathFP.PI>>3)+(oMathFP.PI>>1)+(oMathFP.toFP(myVehicle.realVel)>>5);
		
		int x = oMathFP.toInt(oMathFP.cos(degKm)*(ckmImage.getWidth()>>1));
		int y = oMathFP.toInt(oMathFP.sin(degKm)*(ckmImage.getWidth()>>1));
		
		int kmAddX=0,kmAddY=0;
		
		if (ckmImage.getWidth()==52) {
			kmAddX = 24;
			kmAddY = 23;
		} else if (ckmImage.getWidth()==40) {
			kmAddX = 18;
			kmAddY = 18;
		} else if (ckmImage.getWidth()==30) {
			kmAddX = 14;
			kmAddY = 13;
		} else if (ckmImage.getWidth()==22) {
			kmAddX = 10;
			kmAddY = 9;
		}
		
		kmX += kmAddX;
		kmY += kmAddY;
		
		g.setColor(0xff0000);
		g.drawLine(kmX-(x>>2), kmY-(y>>2), kmX+x, kmY+y);
		
		kmX-= kmAddX;
		kmY-= kmAddY;
		
		// NK-s6600
		// (32,16)  W, H = 3, 22
		
		g.setColor(0x00f000);
				
		int kdxi=0, kdyi=0, kdxl=0, kdyl=0;
		
		if (ckmImage.getWidth()==52) {
                        kdxi = 41;
                        kdyi = 20;
                        kdxl = 5;
                        kdyl = 30;
		} else if (ckmImage.getWidth()==40) {
			kdxi = 32;
			kdyi = 16;
			kdxl = 3;
			kdyl = 22;
		} else if (ckmImage.getWidth()==30) {
			kdxi = 24;
			kdyi = 12;
			kdxl = 2;
			kdyl = 16;
		} else if (ckmImage.getWidth()==22) {
			kdxi = 18;
			kdyi = 10;
			kdxl = 2;
			kdyl = 10;
		}
		
		int damY = kdyl*myVehicle.damageIndicator/10;
		
		g.fillRect(kmX + kdxi, kmY + kdyi + damY, kdxl, kdyl-damY);  
		
		//#endif
		/////////////////////////////////////////////////////////////////////////////////		
		
		
		if (game.raceTime < 0) {
			int t = -game.raceTime/1000;			
			String str = String.valueOf(t);
			
			if (t == 0) {
				str = "GO";
				flagCnt = 0;
			}
			drawOutlineCenterString(str, width2, height2);
		}
		
		if (myVehicle.hasCrashed) {
			String str = "Crashed!";
			int tx = width2;
			int ty = height2;

			drawOutlineCenterString(str, tx, ty);
		}
		else if(game.endRace == 1) {
			String str = "Finish";
			int tx = width2;
			int ty = height2;
			
			//#ifdef J2ME
			g.setClip(0,0,width,height);
			for(int i = height2 - 2 * 10; i <=  height2 + 2 * 10; i += 10) {
				for(int j = 0; j < (width / 10) + 5; j++) {
					if(((i / 10)+j) % 2 == 0) {
						g.setColor(0x0);
					} else {
						g.setColor(0xffffff);
					}
					g.fillRect(j * 10 - flagCnt, i, 10, 10);
				}
			}
			//#endif
			drawOutlineCenterString(str, tx, ty);
			
			flagCnt = (flagCnt + 1) % 20;
		}
		
		//msRender = System.currentTimeMillis() - msRender;
		
		
		//System.out.println("Rects "+numRects+" Quads "+numQuads);
		//numRects = 0;
		//numQuads = 0;
		
		//#ifdef J2ME
		g.setClip(0, 0, width, height);
		//#endif
		
			//#ifdef J2ME
			/*
			// Output Game Timings
			
			g.drawString("L "+game.msLogic, 3, 2, Graphics.LEFT|Graphics.TOP);
			g.drawString("R "+msRender, 3, 12, Graphics.LEFT|Graphics.TOP);

			g.drawString("C "+msCam, width-6, 2, Graphics.RIGHT|Graphics.TOP); 
			g.drawString("X "+msXform, width-6, 12, Graphics.RIGHT|Graphics.TOP);
			g.drawString("P "+msProjDraw, width-6, 22, Graphics.RIGHT|Graphics.TOP);
			*/
			
			/////////////////////////////////////////////////////////// calculate pos
			//int pos = game.nOppo + 1;
			int pos = 1;
			int carP = (myVehicle.subUsid + 127);
			for(int i = 0; i < game.vehiclesByDepth.length;i++) {
				if(game.vehiclesByDepth[i] != myVehicle) {
					//System.out.println(i + " : " + game.vehiclesByDepth[i].subUsid + " : " + carP);
					if(game.vehiclesByDepth[i].subUsid > carP) {
						pos++;
					}
				}
			}
			//////////////////////////////////////////////////////////////////////////
			
			//#ifdef MENU_SMALL_FONT
			//#endif
			
			
			////////////////////////////////////////////////////////////////// print
			game.position = pos;
			
			g.setColor(0xffffff);
			//drawTime(g, "total time: ", game.raceTime, width - 6, 2);
			int lt = (int) game.lapTime;
			if(game.lapCounter == 0) lt = 0;
			
			//#ifndef ONLY_SHOW_POS
			drawTime(g, "lap time: ",lt, width - 6, 2);
			//#endif
			if(game.historyMode == 1) {
				//#ifndef NO_BEST_LAP 
				if(((game.bestTimeBlink >> 2) & 1) > 0) {
					g.setColor(0xff0000);
				} else {
					g.setColor(0xffffff);
				}
				if(game.bestTimeBlink > 0) game.bestTimeBlink--;
				//#ifdef NK-s30
				//#else
				drawTime(g, "best lap: ", (int) game.bestTime, width - 6, 19);
				//#endif
				//#endif
				if(game.endRace == 1) {
					pos = game.endPosition;
					if(pos == 0) pos = 4;
				}
				//#ifdef ONLY_SHOW_POS
				//#else
				//#ifdef NO_BEST_LAP
				//#else
				drawStringWithBorder(g, "pos " + pos + "/" + (game.nOppo + 1) + " lap " + game.lapCounter + "/" + game.totalLaps, width - 6, 36, Graphics.RIGHT | Graphics.TOP);
				//#endif
				//#endif
			} else {
				switch(game.ttplayers) {
					case 0:
					case 1:
						//#ifndef ONLY_SHOW_POS
						drawTime(g, "last lap: ", (int) game.lastLapTime, width - 6, 18);	//4
						drawTime(g, "best lap: ", (int) game.bestTime, width - 6, 36);		//
						//#else
						//#ifndef NK-s30
						drawTime(g, "best lap: ", (int) game.bestTime, width - 6, 4);
						//#else
						//#endif
						//#endif
						break;
					case 2:	
					case 3:
					case 4:
						int bestTime = (int) game.ttpl[0];
						int bestPlIdx = 0;
						
						for(int i = 1; i < game.ttplayers; i++) {
							if(game.ttpl[i] < bestTime && game.ttpl[i] != 0) {
								bestTime = (int) game.ttpl[i];
								bestPlIdx = i;
							}
							//drawTime(g, "player" + (i + 1) + " best lap: ", (int) game.ttpl[i], width - 6, 30 + i * 14);
						}
						int result = 0;
						
						for(int i = 0; i < game.ttplayers; i++) {
							result += game.ttpl[i];
						}
						//#ifdef SHORT_TEXT_BOX
						//#else
						String blap = "best lap: ";
						//#endif
						if(result != 0) {
							g.setColor(game.playerColors[bestPlIdx]);
						} else {
							g.setColor(0xffffff);
						}
							//#ifndef ONLY_SHOW_POS
							drawTime(g, blap, bestTime, width - 6, 20);	//30
							//#else
							//#ifndef NK-s30
							drawTime(g, blap, bestTime, width - 6, 4);
							//#else
							drawTime(g, blap, bestTime, width - 1, 1);
							//#endif
							//#endif
//						} else {
//							g.setColor(0xffffff);
							//#ifndef ONLY_SHOW_POS
//							drawTime(g, blap, 0, width - 6, 30);
							//#else
//							drawTime(g, blap, 0, width - 6, 4);
							//#endif
//						}
						g.setColor(game.playerColors[game.currPlayer]);
						//#ifdef SHORT_TEXT_BOX
						//#else
						g.drawString("player " + (game.currPlayer + 1), 6, 4, Graphics.LEFT | Graphics.TOP);
						//#endif
						break;
				}
			}
			///////////////////////////////////////////////////////////////////////////
			
			/*
			//remove!!! debug!!
			g.setClip(0, 0, width, height);
			g.setColor(0);
			int u = myVehicle.usid % (track.vertexSectionArray.length / 3);
			g.drawString("DEBUG: " + u, 10 + 1, height - 20 + 1, 20);
			g.drawString("DEBUG: " + u, 10 - 1, height - 20 - 1, 20);
			g.setColor(0xffffff);
			g.drawString("DEBUG: " + u, 10, height - 20, 20);
			//remove!!! debug!!
			*/
			//g.drawString("lap: " + game.lapCounter + "/" + totalLaps, width - 6, 64, Graphics.RIGHT | Graphics.TOP);
			//g.drawString("db1: " + game.lastLPusid + "/" + game.vu, width -6, 80, Graphics.RIGHT | Graphics.TOP);
			//#endif
			
		if (game.myVehicle == myVehicle)
			game.calculateCollisions();
		
		//g.setClip(0,0,width,height);
		//g.drawImage(pocafeina,(176-90)/2,208-80, 20);
		
		//g.setColor(0xff0000);
		//g.drawString("FPS: " + fps, 6, 50, Graphics.LEFT | Graphics.TOP);
		
//		System.out.println("direct quads: " + directQuads);
//		System.out.println("skipped quads: " + skippedQuads);
		frames++;
	}

	void drawOutlineCenterString(String str, int tx, int ty) {	
		//#ifdef J2ME
		gBuffer.setColor(0x00);
		gBuffer.drawString(str, tx-2, ty-2, Graphics.HCENTER|Graphics.TOP);
		gBuffer.drawString(str, tx-1, ty-1, Graphics.HCENTER|Graphics.TOP);
		gBuffer.drawString(str, tx, ty, Graphics.HCENTER|Graphics.TOP); 
		gBuffer.drawString(str, tx+1, ty+1, Graphics.HCENTER|Graphics.TOP);
		gBuffer.drawString(str, tx+2, ty+2, Graphics.HCENTER|Graphics.TOP);
		gBuffer.setColor(0xffffff);
		gBuffer.drawString(str, tx, ty, Graphics.HCENTER|Graphics.TOP);
		//#endif
	}
	
	void drawTime(Graphics g, String base, int time, int x, int y) {
		Game game = Game.instance;
		String str = base + game.tstamp2String(time);
		g.drawString(str, x, y, Graphics.RIGHT | Graphics.TOP);
		//drawStringWithBorder(g, str, x, y, Graphics.RIGHT | Graphics.TOP);
	}
	
	void drawStringWithBorder(Graphics g, String str, int x, int y, int mode) {
		//#ifdef J2ME
		/*
		g.setColor(0);
		g.drawString(str, x    , y + 1, mode);
		g.drawString(str, x    , y - 1, mode);
		g.drawString(str, x + 1, y    , mode);
		g.drawString(str, x - 1, y    , mode);
		*/
		g.setColor(0xffffff);
		g.drawString(str, x, y, mode);
		//#elifdef DOJA
		//#endif
	}
	
	
	void drawCarWithCollision(FwdVehicle vehicle, Graphics g, int x, int y, int rot, int depthLevel) {
		
		if (vehicle.lcd > 4 + depthLevel) {
			if (vehicle.ncd < 6) {
				//depthLevel = vehicle.lcd;
				++vehicle.ncd;
			} else {
				vehicle.lcd = depthLevel;
				vehicle.ncd = 0;
			}
		} else {
			vehicle.lcd = depthLevel;
			vehicle.ncd = 0;
		}
		
		//depthLevel += 8;
		if (rot < -2)
			rot = -2;
		if (rot > 2)
			rot = 2;
		
		//#ifdef NK-s30
		//#endif
		
		if (depthLevel < 0)
			depthLevel = 0;
		
		//#ifdef CARS_S40
		//#else
		if (depthLevel > 17)
			depthLevel = 17;
		//#endif

		int carType = vehicle.carType % carFiles.length;
		
	//#ifdef J2ME
	int dl = depthLevel & 1;

			int numFrame = (rot+3)*2 + (depthLevel>>1)*7*4;

			Image cImage = carImages[carType];
			byte[] cCoord = carCoords[carType];

			gc.PutSprite(cImage, x-50+dl, y-50+dl, numFrame, cCoord, 1);
			gc.PutSprite(cImage, x-dl, y-50+dl, numFrame+1, cCoord, 1);
			gc.PutSprite(cImage, x-50+dl, y-dl, numFrame+14, cCoord, 1);
			gc.PutSprite(cImage, x-dl, y-dl, numFrame+15, cCoord, 1);
			
		
		//#ifndef NO_BROKEN_GLASS
		if (brokenWindowsImages[carType]!=null && vehicle.hasBrokenWindows) {
			int carFrame = (rot+3)*2 + (depthLevel>>1)*7*4;

			gc.PutSprite(brokenWindowsImages[carType], x-50, y-50, carFrame, brokenWindowsCoords[carType], 1);
			gc.PutSprite(brokenWindowsImages[carType], x, y-50, carFrame+1, brokenWindowsCoords[carType], 1);
			gc.PutSprite(brokenWindowsImages[carType], x-50, y, carFrame+14, brokenWindowsCoords[carType], 1);
			gc.PutSprite(brokenWindowsImages[carType], x, y, carFrame+15, brokenWindowsCoords[carType], 1);
		}
		//#endif
	
		//#ifndef NO_LIGHTS
		if (lightsImages[carType]!=null && vehicle.isBraking) {
			int carFrame = (rot+3)*2 + (depthLevel>>1)*7*4;

			gc.PutSprite(lightsImages[carType], x-50, y-50, carFrame, lightsCoords[carType], 1);
			gc.PutSprite(lightsImages[carType], x, y-50, carFrame+1, lightsCoords[carType], 1);
			gc.PutSprite(lightsImages[carType], x-50, y, carFrame+14, lightsCoords[carType], 1);
			gc.PutSprite(lightsImages[carType], x, y, carFrame+15, lightsCoords[carType], 1);
		}
		//#endif
		
	//#endif
	
		//#ifdef CARS_S40
		//#endif

		// - Z1010 BLOCK TO FIX -
		if (carType < 0 || carType >= collisionRects.length) return;
		// - Z1010 REPFIX
		
		byte[] crt = collisionRects[carType];
		int crtindex = ((rot+3)+(depthLevel>>1)*7)<<2;

		// - Z1010 BLOCK TO FIX -
		if (crtindex < 0 || crtindex+3 >= crt.length) return;
		// - Z1010 REPFIX
		
		int xicrt = crt[crtindex];
		int yicrt = crt[crtindex+1];
		int xfcrt = crt[crtindex+2];
		//#ifdef CARS_S40
		//#else
		int yfcrt = crt[crtindex+3]-2;
		//#endif
		
		if ((depthLevel & 1) != 0) {
			// refine colrects
			++xicrt;
			++yicrt;
			--xfcrt;
			--yfcrt;
		}
		
		vehicle.curColRectXi = xicrt+x-50;
		vehicle.curColRectYi = yicrt+y-50;
		vehicle.curColRectXf = xfcrt+x-50;//-xicrt;
		vehicle.curColRectYf = yfcrt+y-50;//-yicrt;
		
		g.setClip(0, 0, 500, 500);
	/*		
		if (vehicle.isColliding)	g.setColor(255, 0, 0);			
		else 						g.setColor(0, 0, 0);

		if (Game.instance.myVehicleHasCollidedIndeed) {
			Game.instance.myVehicleHasCollidedIndeed = false;
			g.fillRect(vehicle.curColRectXi, vehicle.curColRectYi,
				vehicle.curColRectXf - vehicle.curColRectXi, vehicle.curColRectYf - vehicle.curColRectYi);
		}			
		
		g.drawRect(vehicle.curColRectXi, vehicle.curColRectYi,
			vehicle.curColRectXf - vehicle.curColRectXi, vehicle.curColRectYf - vehicle.curColRectYi);
	*/
		
	}

	
	int heightCanvas=666;

	/*
	 *   x0      x1
	 *    +------+   top
	 *   /        \
	 *  +----------+	 bottom
	 *  x2          x3
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  gBuffer  Description of the Parameter
	 * @param  x0       Description of the Parameter
	 * @param  x1       Description of the Parameter
	 * @param  x2       Description of the Parameter
	 * @param  x3       Description of the Parameter
	 * @param  top      Description of the Parameter
	 * @param  bottom   Description of the Parameter
	 */
	 
	//#ifdef QUAD_NOKIA
	/*void quadFill(int x0, int x1, int x2, int x3, int top, int bottom,int col) {
		int dxle;
		int dxre;
		int oxle;
		int oxre;
		int yRun  = 0;
		int dy    = bottom - top;
		//int col2 = col | 0xff000000;
		
		if (dy < 1) {
			return;
		}
		
		if(top > heightCanvas) {
			return;
		}

		//pure rect
		if(x0 == x2 && x1 == x3) {
			gBuffer.fillRect(x0, top, x1 - x0, bottom - top);
			return;
		}
		gc.LCD_dGfx.fillTriangle(x0, top, x1, top, x3, bottom, 0xFF000000 | col);
		gc.LCD_dGfx.fillTriangle(x0, top, x2, bottom, x3, bottom, 0xFF000000 | col);
	}*/
	//#endif
	
	//#ifdef QUAD_NOKIA2
	/*void quadFill(int x0, int x1, int x2, int x3, int top, int bottom,int col) {
		int dxle;
		int dxre;
		int oxle;
		int oxre;
		int yRun  = 0;
		int dy    = bottom - top;
		
		if (dy < 1) {
			return;
		}
		
		if(top > heightCanvas) {
			return;
		}
		
		//pure rect
		if(x0 == x2 && x1 == x3) {
			gBuffer.fillRect(x0, top, x1 - x0, bottom - top);
			return;
		}
		
		int minx = x2;
		int maxx = x3;
		int not_minx = x0;
		int not_maxx = x1;
		int miny = bottom;
		int not_miny = top;
		int maxy = bottom;
		int not_maxy = top;
		
		if(x0 < minx) {minx = x0; not_minx = x2; miny = top; not_miny = bottom;}
		if(x1 > maxx) {maxx = x1; not_maxx = x3; maxy = top; not_maxy = bottom;}
		
		int qw = not_maxx - not_minx + 1;
		
		if(qw > 0) {
			if(minx != not_minx) {
				gc.LCD_dGfx.fillTriangle(minx,miny, not_minx,not_miny, not_minx,miny, 0xFF000000 | col);
			}
			
			if(maxx != not_maxx) {
				gc.LCD_dGfx.fillTriangle(maxx,maxy, not_maxx,not_maxy, not_maxx,maxy, 0xFF000000 | col);
			}
			gBuffer.fillRect(not_minx, top, qw, bottom - top);
		} else {
			gc.LCD_dGfx.fillTriangle(x0, top, x1, top, x3, bottom, 0xFF000000 | col);
			gc.LCD_dGfx.fillTriangle(x0, top, x2, bottom, x3, bottom, 0xFF000000 | col);
		}
	}*/
	//#endif
	
	//#ifdef QUAD_MIDP20
	/*void quadFill(int x0, int x1, int x2, int x3, int top, int bottom,int col) {
		int dxle;
		int dxre;
		int oxle;
		int oxre;
		int yRun  = 0;
		int dy    = bottom - top;
		int col2 = col | 0xff000000;
		
		if (dy < 1) {
			return;
		}
		
		if(top > heightCanvas) {
			return;
		}

		//pure rect
		if(x0 == x2 && x1 == x3) {
			gBuffer.fillRect(x0, top, x1 - x0, bottom - top);
			return;
		}
		
		int minx = x2;
		int maxx = x3;
		int not_minx = x0;
		int not_maxx = x1;
		int miny = bottom;
		int not_miny = top;
		int maxy = bottom;
		int not_maxy = top;
		
		if(x0 < minx) {minx = x0; not_minx = x2; miny = top; not_miny = bottom;}
		if(x1 > maxx) {maxx = x1; not_maxx = x3; maxy = top; not_maxy = bottom;}
		
		int qw = not_maxx - not_minx;
		
		if(qw > 0) {
			if(minx != not_minx) {
				gBuffer.fillTriangle(minx,miny, not_minx,not_miny, not_minx,miny);
			}
			
			if(maxx != not_maxx) {
				gBuffer.fillTriangle(maxx,maxy, not_maxx,not_maxy, not_maxx,maxy);
			}
			gBuffer.fillRect(not_minx,top, qw, bottom - top);
		} else {
			gBuffer.fillTriangle(x0, top, x1, top, x3, bottom);
			gBuffer.fillTriangle(x0, top, x2, bottom, x3, bottom);
		}
	}*/
	//#endif
	
	//#ifdef QUAD_MIDP20_2
	/*void quadFill(int x0, int x1, int x2, int x3, int top, int bottom,int col) {
		int dxle;
		int dxre;
		int oxle;
		int oxre;
		int yRun  = 0;
		int dy    = bottom - top;
		
		if (dy < 1) {
			return;
		}
		
		if(top > heightCanvas) {
			return;
		}

		//pure rect
		if(x0 == x2 && x1 == x3) {
			gBuffer.fillRect(x0, top, x1 - x0, bottom - top);
			return;
		}
		gBuffer.fillTriangle(x0, top, x1, top, x3, bottom);
		gBuffer.fillTriangle(x0, top, x2, bottom, x3, bottom);
	}*/
	//#endif
			
	//#ifdef QUAD_RASTER_SEMI
	/*void quadFill(int x0, int x1, int x2, int x3, int top, int bottom,int col) {
		int dxle;
		int dxre;
		int oxle;
		int oxre;
		int yRun  = 0;
		int dy    = bottom - top;
		int col2 = col | 0xff000000;
		
		if (dy < 1) {
			return;
		}
		
		if(top > heightCanvas) {
			return;
		}

		//pure rect
		if(x0 == x2 && x1 == x3) {
			gBuffer.fillRect(x0, top, x1 - x0, bottom - top);
			return;
		}
		
		int minx = x2;
		int maxx = x3;
		int not_minx = x0;
		int not_maxx = x1;
		int miny = bottom;
		int not_miny = top;
		int maxy = bottom;
		int not_maxy = top;
		
		if(x0 < minx) {minx = x0; not_minx = x2; miny = top; not_miny = bottom;}
		if(x1 > maxx) {maxx = x1; not_maxx = x3; maxy = top; not_maxy = bottom;}
		
		int qw = not_maxx - not_minx;
		
		if(qw > 0) {
			if(minx != not_minx) {
				fillRightRectTriangle(gBuffer, x0, x2, top, bottom);
			}
			
			if(maxx != not_maxx) {
				fillLeftRectTriangle(gBuffer, x1, x3, top, bottom);
			}
			gBuffer.fillRect(not_minx,top, qw, bottom - top);
		} else {
			int xle = x0 << 8;
			int xre = x1 << 8;
			
			//++numQuads;
			
			if (bottom > heightCanvas) 
				bottom = heightCanvas;
			
			dxle = ((x2 - x0) << 8) / dy;
			dxre = ((x3 - x1) << 8) / dy;
	
			while (bottom > top) {
				oxle = (xle + 127) >> 8;
				oxre = (xre + 127) >> 8;
	
				yRun = 0;
				do {
					xle += dxle;
					xre += dxre;
					++yRun;
					++top;
					//#ifdef QFX2
					xle += dxle;
					xre += dxre;
					++yRun;
					++top;
					//#endif
					//#ifdef QFX3
					xle += dxle;
					xre += dxre;
					++yRun;
					++top;
	
					xle += dxle;
					xre += dxre;
					++yRun;
					++top;
					//#endif
					
					//#ifdef QFX4
					xle += dxle; xre += dxre; ++yRun; ++top;
					xle += dxle; xre += dxre; ++yRun; ++top;
					xle += dxle; xre += dxre; ++yRun; ++top;
					//#endif
				} while (oxle == ((xle + 127) >> 8) && oxre == ((xre + 127) >> 8) && bottom > top);
	
				//#ifdef J2ME
				gBuffer.fillRect(oxle, top - yRun, (oxre - oxle), yRun);
				//#elifdef DOJA
				int w = oxre - oxle;
				int h = yRun;
				if (w > 0)
					gBuffer.fillRect(oxle, top - yRun, w, h);
				//#endif
				//++numRects;
			}
		}
	}
	
	public void fillRightRectTriangle(Graphics g, int xtop,int xbottom, int top, int bottom) {
		int dx,dy;
		int oxle,xle;
		int yRun;
		int maxx = xbottom;
		
		if(xtop > maxx) maxx = xtop;
		
		dy = bottom - top;
		
		if(dy < 1) return;
		
		xle = xtop << 8;
		dx = ((xbottom - xtop) << 8) / dy;
		
		while(bottom > top) {
			oxle = (xle + 127) >> 8;
			
			yRun = 0;
			do {
				xle += dx;
				yRun++;
				top++;
			} while (oxle == ((xle + 127) >> 8) && bottom > top);
			
			g.fillRect(oxle, top - yRun, (maxx - oxle), yRun);
		}
	}
	public void fillLeftRectTriangle(Graphics g,int xtop,int xbottom, int top, int bottom) {
		int dx,dy;
		int oxle,xle;
		int yRun;
		int minx = xtop;
		
		if(xbottom < minx) minx = xbottom;
		
		dy = bottom - top;
		
		if(dy < 1) return;
		
		xle = xtop << 8;
		dx = ((xbottom - xtop) << 8) / dy;
		
		while(bottom > top) {
			oxle = (xle + 127) >> 8;
			
			yRun = 0;
			do {
				xle += dx;
				yRun++;
				top++;
			} while (oxle == ((xle + 127) >> 8) && bottom > top);

			g.fillRect(minx, top - yRun, (oxle - minx), yRun);
		}
	}*/
	//#endif
	
	//#ifdef QUAD_RASTER_FULL
	void quadFill(int x0, int x1, int x2, int x3, int top, int bottom, int col) {
		int dxle;
		int dxre;
		int oxle;
		int oxre;
		int yRun  = 0;
		int dy    = bottom - top;

		if (dy < 1) {
			return;
		}

		int xle   = x0 << 8;
		int xre   = x1 << 8;
		
		//++numQuads;
		
		if (bottom > heightCanvas) 
			bottom = heightCanvas;

		dxle = ((x2 - x0) << 8) / dy;
		dxre = ((x3 - x1) << 8) / dy;

		while (bottom > top) {
			oxle = (xle + 127) >> 8;
			oxre = (xre + 127) >> 8;

			yRun = 0;
			do {
				xle += dxle;
				xre += dxre;
				++yRun;
				++top;
//#ifdef QFX2
				xle += dxle;
				xre += dxre;
				++yRun;
				++top;
//#endif
//#ifdef QFX3
				xle += dxle;
				xre += dxre;
				++yRun;
				++top;

				xle += dxle;
				xre += dxre;
				++yRun;
				++top;
//#endif
			} while (oxle == ((xle + 127) >> 8) && oxre == ((xre + 127) >> 8) && bottom > top);

			//#ifdef J2ME
			gBuffer.fillRectPrimitive(gBuffer, oxle, top - yRun, (oxre - oxle), yRun);
			//#elifdef DOJA
			//#endif
			//++numRects;
		}
	}
	//#endif

	
	int[] calcMatrixCamera(int px, int py, int pz, int tx, int ty, int tz) {
				
		// z-axis = target - pos
		int zax = tx - px;
		int zay = ty - py;
		int zaz = tz - pz;
		
		int fpuinv = oMathFP.div(oMathFP.ONE, oMathFP.sqrt(oMathFP.mul(zax,zax) + oMathFP.mul(zay,zay) + oMathFP.mul(zaz,zaz)));
						
		zax = oMathFP.mul(zax,fpuinv);
		zay = oMathFP.mul(zay,fpuinv);
		zaz = oMathFP.mul(zaz,fpuinv);
				
		// x-axis = za x (0, 1, 0)
		int upx = 0;
		int upy = oMathFP.ONE;
		int upz = 0;

		int xax = oMathFP.mul(zay,upz) - oMathFP.mul(zaz,upy);
		int xay = oMathFP.mul(zaz,upx) - oMathFP.mul(zax,upz);
		int xaz = oMathFP.mul(zax,upy) - oMathFP.mul(zay,upx);

		
		fpuinv = oMathFP.div(oMathFP.ONE, oMathFP.sqrt(oMathFP.mul(xax,xax) + oMathFP.mul(xay,xay) + oMathFP.mul(xaz,xaz)));
		xax = oMathFP.mul(xax,fpuinv);
		xay = oMathFP.mul(xay,fpuinv);
		xaz = oMathFP.mul(xaz,fpuinv);
		
		// y-axis = ya x za
		int yax = oMathFP.mul(xay,zaz) - oMathFP.mul(xaz,zay);
		int yay = oMathFP.mul(xaz,zax) - oMathFP.mul(xax,zaz);
		int yaz = oMathFP.mul(xax,zay) - oMathFP.mul(xay,zax);
		
		int[] matCam = new int[3*3];
		
		matCam[0] = -xax;
		matCam[1] = -xay;
		matCam[2] = -xaz;
		
		matCam[3] = -yax;
		matCam[4] = -yay;
		matCam[5] = -yaz;
		
		matCam[6] = zax;
		matCam[7] = zay;
		matCam[8] = zaz;
		
		return matCam;
	}
	
	int xformX;
	int xformY;
	int xformZ;
	
	void xformVector(int[] matrix, int px, int py, int pz) {
		int rx, ry, rz;
		
		rx = matrix[0]*px + matrix[1]*py + matrix[2]*pz;
		ry = matrix[3]*px + matrix[4]*py + matrix[5]*pz;
		rz = matrix[6]*px + matrix[7]*py + matrix[8]*pz;
		
		xformX = oMathFP.toInt(rx);
		xformY = oMathFP.toInt(ry);
		xformZ = oMathFP.toInt(rz);
	}

	//#ifdef PAL_TRICK	
	public Image createImageFromPalette(String filename, String palFile) {
		byte[] ti = loadFile(filename);
		byte[] pal = loadFile(palFile);
		
		Image img = null;
		int pos = (pal[0] << 24) + (pal[1] << 16) + (pal[2] << 8) + pal[3];
		System.arraycopy(pal, 4, ti, pos, pal.length - 4);
		try {
			img = new Image();
			img._createImage(ti, 0, ti.length);
		} catch (Exception e) {
			System.out.println("exception while creating image from palette: " + e.getMessage() + " from " + filename + " : " + palFile);
		}
		pal = null;
		ti = null;
		System.gc();
		
		return img;
	}
	//#endif
}


